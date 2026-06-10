package com.accident;

import java.time.Instant;
import java.util.UUID;

public class EmergencyEvent {
    public final String id;
    public final Instant createdAt;
    public final String status; // QUEUED, SENT, FAILED
    public final String severity;
    public final String message;

    public EmergencyEvent(String id, Instant createdAt, String status, String severity, String message) {
        this.id = id;
        this.createdAt = createdAt;
        this.status = status;
        this.severity = severity;
        this.message = message;
    }

    public static EmergencyEvent queued(String severity, String message) {
        return new EmergencyEvent(UUID.randomUUID().toString(), Instant.now(), "QUEUED", severity, message);
    }

    public String toJson() {
        return "{"+
            "\"id\":\""+escapeJson(id)+"\","+
            "\"createdAt\":\""+escapeJson(createdAt.toString())+"\","+
            "\"status\":\""+escapeJson(status)+"\","+
            "\"severity\":\""+escapeJson(severity)+"\","+
            "\"message\":\""+escapeJson(message)+"\""+
        "}";
    }

    private static String escapeJson(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ");
    }
}

