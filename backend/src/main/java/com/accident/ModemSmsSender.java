package com.accident;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ModemSmsSender {
    private final ModemConfig config;

    public ModemSmsSender(ModemConfig config) {
        this.config = config;
    }

    public void sendEmergencySms(String severity, String message) throws Exception {
        // SMS send includes minimal modem initialization.
        try (SerialPort port = openPort()) {
            if (!sendAt(port, "AT", "OK", 2000)) {
                throw new IOException("Modem not responding to AT");
            }

            // Ensure SIM ready (optional, but helpful)
            // If modem requires PIN, AT+CPIN? may be returned as "SIM PIN".
            sendAt(port, "AT+CPIN?", null, 2000);

            // Set SMS text mode
            sendAt(port, "AT+CMGF=1", "OK", 2000);

            String sms = buildSms(severity, message);

            // Initiate message
            String cmd = "AT+CMGS=\"" + escapeNumber(config.targetPhoneNumber()) + "\"";
            String prompt = waitForContains(port, cmd, ">", 5000);
            if (prompt == null) {
                throw new IOException("No SMS prompt (>) from modem");
            }

            // Send message + Ctrl+Z (ASCII 26)
            port.getOutputStream().write((sms + "\r").getBytes(StandardCharsets.US_ASCII));
            port.getOutputStream().write(new byte[] {26});
            port.getOutputStream().flush();

            // Wait for completion
            String resp = readAvailable(port, 15000);
            if (!(resp.contains("OK") || resp.contains("+CMGS") || resp.contains("ERROR") == false)) {
                // If modem returns ERROR, throw.
                if (resp.toUpperCase().contains("ERROR")) {
                    throw new IOException("Modem SMS send error: " + resp.trim());
                }
            }
            // If no explicit OK but also no ERROR, treat as success.
        }
    }

    private SerialPort openPort() throws IOException {
        SerialPort port = SerialPort.getCommPort(config.serialPort());
        port.setComPortParameters(config.baudRate(), 8, SerialPort.ONE_STOP_BIT, SerialPort.NO_PARITY);
        port.setComPortTimeouts(SerialPort.TIMEOUT_NONBLOCKING, 0, 0);
        if (!port.openPort()) {
            throw new IOException("Failed to open serial port: " + config.serialPort());
        }
        // Some modems need a short delay after opening
        sleep(300);
        port.getInputStream(); // touch
        return port;
    }

    private boolean sendAt(SerialPort port, String command, String expectedSubstring, long timeoutMs) throws IOException {
        port.getOutputStream().write((command + "\r").getBytes(StandardCharsets.US_ASCII));
        port.getOutputStream().flush();

        String resp = readUntilOrTimeout(port, timeoutMs);
        if (expectedSubstring == null) return true;
        return resp != null && resp.contains(expectedSubstring);
    }

    private String waitForContains(SerialPort port, String command, String contains, long timeoutMs) throws IOException {
        port.getOutputStream().write((command + "\r").getBytes(StandardCharsets.US_ASCII));
        port.getOutputStream().flush();

        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        while (System.currentTimeMillis() - start < timeoutMs) {
            String chunk = readAvailable(port, 200);
            if (chunk != null && !chunk.isEmpty()) {
                sb.append(chunk);
                if (sb.toString().contains(contains)) {
                    return sb.toString();
                }
            }
            sleep(50);
        }
        return null;
    }

    private String buildSms(String severity, String message) {
        // Keep SMS short. Allow customization via frontend message.
        return "[EMERGENCY] " + (severity != null ? severity : "HIGH") + ": " + message;
    }

    private String escapeNumber(String phone) {
        // Basic escaping; AT number should be plain + digits.
        if (phone == null) return "";
        return phone.replace("\"", "");
    }

    private String readUntilOrTimeout(SerialPort port, long timeoutMs) throws IOException {
        long start = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        while (System.currentTimeMillis() - start < timeoutMs) {
            String chunk = readAvailable(port, 200);
            if (chunk != null && !chunk.isEmpty()) {
                sb.append(chunk);
                // small heuristic: if OK/ERROR appears early, stop
                if (sb.toString().contains("OK") || sb.toString().toUpperCase().contains("ERROR")) {
                    break;
                }
            }
            sleep(50);
        }
        return sb.toString();
    }

    private String readAvailable(SerialPort port, long maxWaitMs) throws IOException {
        long start = System.currentTimeMillis();
        byte[] buffer = new byte[4096];
        int total = 0;
        while (System.currentTimeMillis() - start < maxWaitMs) {
            int available = port.bytesAvailable();
            if (available > 0) {
                int read = port.getInputStream().read(buffer, 0, Math.min(buffer.length, available));
                if (read > 0) {
                    total += read;
                    return new String(buffer, 0, read, StandardCharsets.US_ASCII);
                }
            }
            sleep(20);
        }
        return "";
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }
}

