package com.dmart.support_intake.service;

import com.dmart.support_intake.dto.EventRequest;
import org.springframework.stereotype.Service;

@Service
public class ClassificationService {

    public String classify(EventRequest request) {
        if (request.getIntegrationName() != null && 
            request.getIntegrationName().equalsIgnoreCase("AEL_StartTripECPSTO")) {
            return "WMS Exception";
        }

        if (request.getErrorCode() != null && 
            request.getErrorCode().equalsIgnoreCase("MQ_CONNECTION_ERROR")) {
            return "MQ Exception";
        }

        return "Unknown";
    }
}