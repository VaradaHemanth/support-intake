package com.dmart.support_intake.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "incidents")
public class Incident {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "signature_hash", nullable = false, unique = true)
    private String signatureHash;

    @Column(name = "jira_key")
    private String jiraKey;

    @Column(name = "integration_name")
    private String integrationName;

    @Column(name = "node")
    private String node;

    @Column(name = "error_code")
    private String errorCode;

    @Column(name = "env")
    private String env;

    @Column(name = "status")
    private String status;

    @Column(name = "classifier_tier")
    private String classifierTier;

    @Column(name = "first_detected_at")
    private Instant firstDetectedAt;

    @Column(name = "last_detected_at")
    private Instant lastDetectedAt;

    @Column(name = "status_changed_at")
    private Instant statusChangedAt;

    @Column(name = "occurrence_count")
    private Integer occurrenceCount;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSignatureHash() { return signatureHash; }
    public void setSignatureHash(String signatureHash) { this.signatureHash = signatureHash; }

    public String getJiraKey() { return jiraKey; }
    public void setJiraKey(String jiraKey) { this.jiraKey = jiraKey; }

    public String getIntegrationName() { return integrationName; }
    public void setIntegrationName(String integrationName) { this.integrationName = integrationName; }

    public String getNode() { return node; }
    public void setNode(String node) { this.node = node; }

    public String getErrorCode() { return errorCode; }
    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public String getEnv() { return env; }
    public void setEnv(String env) { this.env = env; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getClassifierTier() { return classifierTier; }
    public void setClassifierTier(String classifierTier) { this.classifierTier = classifierTier; }

    public Instant getFirstDetectedAt() { return firstDetectedAt; }
    public void setFirstDetectedAt(Instant firstDetectedAt) { this.firstDetectedAt = firstDetectedAt; }

    public Instant getLastDetectedAt() { return lastDetectedAt; }
    public void setLastDetectedAt(Instant lastDetectedAt) { this.lastDetectedAt = lastDetectedAt; }

    public Instant getStatusChangedAt() { return statusChangedAt; }
    public void setStatusChangedAt(Instant statusChangedAt) { this.statusChangedAt = statusChangedAt; }

    public Integer getOccurrenceCount() { return occurrenceCount; }
    public void setOccurrenceCount(Integer occurrenceCount) { this.occurrenceCount = occurrenceCount; }
}