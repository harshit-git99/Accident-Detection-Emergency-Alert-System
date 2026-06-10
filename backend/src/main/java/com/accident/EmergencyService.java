package com.accident;

import java.time.Instant;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class EmergencyService {
    private final ModemSmsSender modem;
    private final ExecutorService worker = Executors.newSingleThreadExecutor();

    private final AtomicReference<EmergencyEvent> lastEvent = new AtomicReference<>();

    public EmergencyService(ModemSmsSender modem) {
        this.modem = modem;
    }

    public EmergencyEvent trigger(TriggerRequest tr) {
        String severity = tr.severity != null ? tr.severity : "HIGH";
        String msg = tr.message != null ? tr.message : "Accident detected! Immediate assistance required.";
        if (tr.latitude != null && tr.longitude != null) {
            msg = msg + " Location: " + tr.latitude + "," + tr.longitude;
        }

        EmergencyEvent ev = EmergencyEvent.queued(severity, msg);
        lastEvent.set(ev);

        worker.submit(() -> {
            try {
                modem.sendEmergencySms(severity, msg);
                lastEvent.set(new EmergencyEvent(ev.id, Instant.now(), "SENT", severity, msg));
            } catch (Exception e) {
                lastEvent.set(new EmergencyEvent(ev.id, Instant.now(), "FAILED", severity, msg + " (" + e.getMessage() + ")"));
            }
        });

        return ev;
    }

    public String statusJson() {
        EmergencyEvent ev = lastEvent.get();
        if (ev == null) {
            return "{\"status\":\"IDLE\"}";
        }
        return ev.toJson();
    }
}

