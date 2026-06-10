package com.accident;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Minimal JSON parsing (no external JSON libs) to keep the project lightweight.
 */
public class TriggerRequest {
    public final String severity;
    public final String message;
    public final Double latitude;
    public final Double longitude;

    public TriggerRequest(String severity, String message, Double latitude, Double longitude) {
        this.severity = severity;
        this.message = message;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static TriggerRequest fromJson(String json) {
        if (json == null) return null;
        String s = json.trim();
        if (!s.startsWith("{") || !s.endsWith("}")) return null;

        String severity = extractString(s, "severity");
        String message = extractString(s, "message");
        Double lat = extractDouble(s, "latitude");
        Double lon = extractDouble(s, "longitude");

        if (severity == null && message == null && lat == null && lon == null) return null;
        return new TriggerRequest(severity, message, lat, lon);
    }

    private static String extractString(String s, String key) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*\"([^\"]*)\"");
        Matcher m = p.matcher(s);
        return m.find() ? m.group(1) : null;
    }

    private static Double extractDouble(String s, String key) {
        Pattern p = Pattern.compile("\"" + Pattern.quote(key) + "\"\\s*:\\s*(-?\\d+(?:\\.\\d+)?)");
        Matcher m = p.matcher(s);
        return m.find() ? Double.valueOf(m.group(1)) : null;
    }
}

