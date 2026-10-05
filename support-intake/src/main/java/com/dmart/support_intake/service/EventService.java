package com.dmart.support_intake.service;

import com.dmart.support_intake.dto.EventRequest;
import com.dmart.support_intake.dto.EventResponse;
import com.dmart.support_intake.entity.Incident;
import com.dmart.support_intake.repository.IncidentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

@Service
public class EventService {

    private final SignatureService signatureService;
    private final ClassificationService classificationService;
    private final JiraService jiraService;
    private final IncidentRepository incidentRepository;

    public EventService(
            SignatureService signatureService,
            ClassificationService classificationService,
            JiraService jiraService,
            IncidentRepository incidentRepository
    ) {
        this.signatureService = signatureService;
        this.classificationService = classificationService;
        this.jiraService = jiraService;
        this.incidentRepository = incidentRepository;
    }

    @Transactional
    public EventResponse processEvent(EventRequest request) {
        String signatureHash = signatureService.generateSignature(request);
        Optional<Incident> existingIncident = incidentRepository.findBySignatureHash(signatureHash);

        // System captures internal timestamp automatically upon receipt
        Instant now = Instant.now();

        if (existingIncident.isPresent()) {
            Incident incident = existingIncident.get();

            if (incident.getClassifierTier() == null) {
                incident.setClassifierTier(classificationService.classify(request));
            }

            int newCount = incident.getOccurrenceCount() + 1;
            incident.setOccurrenceCount(newCount);
            
            // Updates last_detected_at internally
            incident.setLastDetectedAt(now);

            incidentRepository.save(incident);

            String comment = String.format("Duplicate error detected again on node %s at %s. Total occurrences: %d",
                    request.getNode(), now.toString(), newCount);
            jiraService.addComment(incident.getJiraKey(), comment);

            return new EventResponse(
                    "DUPLICATE_UPDATED",
                    incident.getJiraKey(),
                    true,
                    newCount
            );
        } else {
            String classifierTier = classificationService.classify(request);
            String jiraKey = jiraService.createJiraIssue(request);

            Incident newIncident = new Incident();
            newIncident.setSignatureHash(signatureHash);
            newIncident.setJiraKey(jiraKey);
            newIncident.setIntegrationName(request.getIntegrationName());
            newIncident.setNode(request.getNode());
            newIncident.setErrorCode(request.getErrorCode());
            newIncident.setEnv(request.getEnv());
            
            // Set initial status and stamp current creation time
            newIncident.setStatus("OPEN");
            newIncident.setStatusChangedAt(now);
            
            newIncident.setClassifierTier(classifierTier);

            // Set detection timestamps automatically
            newIncident.setFirstDetectedAt(now);
            newIncident.setLastDetectedAt(now);
            newIncident.setOccurrenceCount(1);

            incidentRepository.save(newIncident);

            return new EventResponse(
                    "JIRA_CREATED",
                    jiraKey,
                    false,
                    1
            );
        }
    }

    @Transactional
    public boolean updateStatusFromJira(String jiraKey, String newStatus) {
        Optional<Incident> incidentOpt = incidentRepository.findByJiraKey(jiraKey);
        if (incidentOpt.isPresent()) {
            Incident incident = incidentOpt.get();

            // Stamp new time whenever status changes
            if (incident.getStatus() == null || !incident.getStatus().equalsIgnoreCase(newStatus)) {
                incident.setStatus(newStatus);
                incident.setStatusChangedAt(Instant.now());
                incidentRepository.save(incident);
                return true;
            }
        }
        return false;
    }
}