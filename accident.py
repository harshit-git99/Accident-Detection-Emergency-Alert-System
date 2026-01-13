import time
import random
import smtplib
from email.message import EmailMessage
import requests

# --------------- CONFIG ----------------
ACCIDENT_THRESHOLD = 25.0  # sudden acceleration value
CHECK_INTERVAL = 2  # seconds
EMERGENCY_EMAIL = "receiver@gmail.com"
SENDER_EMAIL = "your_email@gmail.com"
SENDER_PASSWORD = "your_app_password"
# --------------------------------------

def get_gps_location():
    """Fetch live GPS location using IP-based API"""
    try:
        response = requests.get("https://ipinfo.io/json")
        data = response.json()
        loc = data["loc"].split(",")
        latitude = loc[0]
        longitude = loc[1]
        return latitude, longitude
    except:
        return None, None

def get_acceleration_data():
    """
    Simulating accelerometer values.
    Replace this with real sensor data if using hardware.
    """
    return random.uniform(0, 40)

def send_emergency_alert(lat, lon):
    try:
        msg = EmailMessage()
        msg["Subject"] = "🚨 Accident Detected - Emergency Alert"
        msg["From"] = SENDER_EMAIL
        msg["To"] = EMERGENCY_EMAIL

        google_maps_link = f"https://www.google.com/maps?q={lat},{lon}"

        msg.set_content(
            f"""
            🚨 ACCIDENT DETECTED!

            Location:
            Latitude: {lat}
            Longitude: {lon}

            Google Maps Link:
            {google_maps_link}

            Please send help immediately!
            """
        )

        server = smtplib.SMTP_SSL("smtp.gmail.com", 465)
        server.login(SENDER_EMAIL, SENDER_PASSWORD)
        server.send_message(msg)
        server.quit()

        print("🚨 Emergency alert sent successfully!")

    except Exception as e:
        print("Error sending alert:", e)

def detect_accident():
    print("Accident Detection System Running...")
    while True:
        accel_value = get_acceleration_data()
        print(f"Acceleration: {accel_value:.2f}")

        if accel_value > ACCIDENT_THRESHOLD:
            print("⚠ Possible accident detected!")

            lat, lon = get_gps_location()
            if lat and lon:
                print(f"Location: {lat}, {lon}")
                send_emergency_alert(lat, lon)
            else:
                print("GPS not available")

            print("Waiting 30 seconds before resuming...")
            time.sleep(30)  # cooldown

        time.sleep(CHECK_INTERVAL)

if __name__ == "__main__":
    detect_accident()
