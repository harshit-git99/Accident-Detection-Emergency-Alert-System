# GSM/Modem Integration Notes (Offline / No Internet)

This project sends emergency alerts using **GSM modem AT commands** over a **SIM network**.

## Supported approach
- Java backend talks to a GSM modem via a **serial COM port** (e.g., `COM3`).
- Backend issues AT commands to send SMS.
- No internet connection is required.

## Hardware needed
- A GSM modem / cellular module that can:
  - register to the network
  - send SMS via AT commands
- A SIM card with SMS capability
- USB-to-serial driver installed on Windows

## Typical AT commands
- `AT` (basic check)
- `AT+CPIN?` (SIM PIN status)
- `AT+CREG?` (network registration)
- `AT+CMGF=1` (SMS text mode)
- `AT+CMGS="<number>"` then message text + Ctrl+Z

## Common configuration fields
- `serialPort` (e.g., `COM3`)
- `baudRate` (e.g., `9600`)
- `targetPhoneNumber` (emergency contact number)

## Troubleshooting (quick)
- Verify modem appears in Device Manager as a COM port.
- Send `AT` using any serial terminal and confirm `OK` response.
- If `AT+CMGS` fails, check:
  - network registration
  - correct SMS format
  - SIM limitations

