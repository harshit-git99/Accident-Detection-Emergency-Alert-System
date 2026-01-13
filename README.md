# 🚨 Accident Detection & Emergency Alert System

This project is a Python-based prototype for an **Accident Detection &
Emergency Alert System** designed to improve road safety by
automatically detecting accidents and sending emergency alerts with
real-time GPS location.

It simulates sensor data and uses IP-based geolocation to demonstrate
how such a system can work in real-world scenarios. This project is
ideal for **resume, academic projects, and demos** related to automobile
automation, IoT, and safety systems.

------------------------------------------------------------------------

## 🔥 Features

-   🚗 Sudden impact / accident detection using acceleration threshold
-   📍 GPS location tracking (via IP-based API)
-   🗺 Google Maps live location sharing
-   📧 Automatic emergency alert via email
-   ⏳ Cooldown mechanism to prevent repeated alerts
-   🧪 Sensor data simulation (can be replaced with real hardware)

------------------------------------------------------------------------

## 🛠 Tech Stack

-   Python 3.x
-   SMTP (for sending email alerts)
-   REST APIs
-   Requests library

------------------------------------------------------------------------

## 📂 Project Structure

    accident_detection/
    │
    ├── main.py               # Main accident detection script
    ├── README.md            # Project documentation
    └── requirements.txt     # Dependencies

------------------------------------------------------------------------

## ⚙️ Installation

``` bash
pip install -r requirements.txt
```

------------------------------------------------------------------------

## ▶️ How to Run

``` bash
python main.py
```

------------------------------------------------------------------------

## 🔧 Configuration

Update these variables in the script:

``` python
ACCIDENT_THRESHOLD = 25.0
EMERGENCY_EMAIL = "receiver@gmail.com"
SENDER_EMAIL = "your_email@gmail.com"
SENDER_PASSWORD = "your_app_password"
```

------------------------------------------------------------------------

## 🚀 Future Enhancements

-   SMS alerts using Twilio
-   WhatsApp notifications
-   Raspberry Pi + Accelerometer + GPS module integration
-   Firebase live tracking
-   Mobile app interface
-   AI-based crash severity detection
-   Emergency voice alerts

------------------------------------------------------------------------

## ⚠ Disclaimer

This is a prototype and should not be used as a real-life safety system
without proper testing.
