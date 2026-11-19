
import pandas as pd
import numpy as np

np.random.seed(42)

routes = {
    "HR01": ("Ambala", "Chandigarh", 45),
    "HR02": ("Ambala", "Kurukshetra", 40),
    "HR03": ("Ambala", "Karnal", 65),
    "HR04": ("Ambala", "Panipat", 95),
    "HR05": ("Ambala", "Yamunanagar", 55)
}

traffic_levels = ["Low", "Medium", "High"]

records = []

for i in range(1000):
    route_id = np.random.choice(list(routes.keys()))
    start, destination, total_distance = routes[route_id]

    distance = np.random.uniform(2, total_distance)
    speed = np.random.uniform(25, 75)
    traffic = np.random.choice(traffic_levels, p=[0.35, 0.45, 0.20])
    hour = np.random.randint(5, 23)
    day = np.random.randint(0, 7)
    stops = np.random.randint(0, 8)

    traffic_delay = {
        "Low": 0,
        "Medium": 5,
        "High": 12
    }[traffic]

    time_factor = 1.2 if 8 <= hour <= 10 or 17 <= hour <= 19 else 1

    eta = (
        (distance / speed) * 60 * time_factor
        + traffic_delay
        + stops * 2
        + np.random.normal(0, 2)
    )

    eta = max(1, round(eta, 2))

    records.append({
        "bus_id": f"HR-{np.random.randint(1000, 1100)}",
        "route_id": route_id,
        "start_location": start,
        "destination": destination,
        "distance_remaining": round(distance, 2),
        "average_speed": round(speed, 2),
        "traffic_level": traffic,
        "hour": hour,
        "day_of_week": day,
        "stops_remaining": stops,
        "eta_minutes": eta
    })

df = pd.DataFrame(records)
df.to_csv("eta_training_data.csv", index=False)

print("Dummy dataset generated!")
print("Total records:", len(df))
print(df.head())
print("Saved as eta_training_data.csv")
