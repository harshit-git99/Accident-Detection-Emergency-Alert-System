package com.accident;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ModemConfig {
    private final String serialPort;
    private final int baudRate;
    private final String targetPhoneNumber;
    private final boolean includeLocationInSms;
    private final int httpPort;

    public ModemConfig(String serialPort, int baudRate, String targetPhoneNumber, boolean includeLocationInSms, int httpPort) {
        this.serialPort = serialPort;
        this.baudRate = baudRate;
        this.targetPhoneNumber = targetPhoneNumber;
        this.includeLocationInSms = includeLocationInSms;
        this.httpPort = httpPort;
    }

    public String serialPort() { return serialPort; }
    public int baudRate() { return baudRate; }
    public String targetPhoneNumber() { return targetPhoneNumber; }
    public boolean includeLocationInSms() { return includeLocationInSms; }
    public int httpPort() { return httpPort; }

    public static ModemConfig load(Path path) throws IOException {
        Properties p = new Properties();
        try (InputStream in = Files.newInputStream(path)) {
            p.load(in);
        }

        String serialPort = p.getProperty("serialPort", "COM3");
        int baudRate = Integer.parseInt(p.getProperty("baudRate", "9600"));
        String targetPhoneNumber = p.getProperty("targetPhoneNumber", "+10000000000");
        boolean includeLocationInSms = Boolean.parseBoolean(p.getProperty("includeLocationInSms", "false"));
        int httpPort = Integer.parseInt(p.getProperty("httpPort", "4567"));

        return new ModemConfig(serialPort, baudRate, targetPhoneNumber, includeLocationInSms, httpPort);
    }

    public static String defaultPropertiesText() {
        return "# GSM modem settings\n" +
               "serialPort=COM3\n" +
               "baudRate=9600\n" +
               "targetPhoneNumber=+10000000000\n" +
               "includeLocationInSms=false\n" +
               "httpPort=4567\n";
    }
}

