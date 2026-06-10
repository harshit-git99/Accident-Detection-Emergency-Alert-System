# Backend build/run (Java + Spark)

## Build & run
This backend uses Gradle.

```bat
cd AccidentDetectionEmergencySystem\backend
gradle.bat run
```

If `gradle.bat` is not available, install Gradle or add a proper Gradle Wrapper.

## Endpoints
- `POST /api/trigger-accident`
- `GET /api/status`

Backend also serves static frontend from `../frontend`.

## Modem
Edit:
- `config/modem.properties`

First run auto-creates the file if it doesn't exist.

