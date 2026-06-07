# Accident Detection & Emergency Alert System

Offline emergency notification using **SIM/GSM network** (GSM modem via serial AT commands) with a **Java backend** and a simple **frontend UI**.

## What this project does
- Provides a web UI to **simulate an accident**.
- Backend receives `POST /api/trigger-accident` and sends an **SMS** via GSM modem.
- Works without internet (as long as your modem has network + SIM supports SMS).

## Folder structure
- `backend/` - Java (Spark framework)
- `frontend/` - static HTML/CSS/JS
- `backend/config/` - modem configuration (generated on first run)

## Requirements
- Java 17+
- Gradle
- GSM modem connected via USB serial (COM port)

## Configure modem
Edit/verify: `backend/config/modem.properties` after first run.

Example fields:
- `serialPort=COM3`
- `baudRate=9600`
- `targetPhoneNumber=+91XXXXXXXXXX`

## Run backend (Windows)
From this project root:

```bat
cd AccidentDetectionEmergencySystem\backend
gradlew.bat run
```

Backend serves the frontend too.

Open:
- `http://localhost:4567/`

## Test
- Click **Simulate Accident** in the UI.
- Check backend status and ensure SMS was delivered.

## Connecting real accident detection
Replace the UI trigger with your real detector (sensor/camera pipeline) that calls:
- `POST http://localhost:4567/api/trigger-accident`

JSON payload example:
```json
{
  "severity": "HIGH",
  "message": "Accident detected! Immediate assistance required.",
  "latitude": 12.9716,
  "longitude": 77.5946
}
```

