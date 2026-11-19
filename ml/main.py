
from fastapi import FastAPI
from eta_prediction import (
    predict_passenger_eta,
    find_best_buses
)

app = FastAPI(
    title="Haryana Roadways ML Service",
    description="Predictive Public Transport System for Charkhi Dadri",
    version="1.0.0"
)


@app.get("/")
def home():
    return {
        "project": "Haryana Roadways",
        "module": "Machine Learning",
        "status": "Running"
    }


@app.get("/health")
def health_check():
    return {
        "status": "healthy",
        "message": "ML service is working properly"
    }



@app.get("/predict/eta")
def get_eta(
    bus_id: str,
    boarding_stop_id: str,
    destination_stop_id: str,
    already_onboard: bool = False,
    traffic_level: str = "MEDIUM"
):
    return predict_passenger_eta(
        bus_id,
        boarding_stop_id,
        destination_stop_id,
        already_onboard,
        traffic_level
    )


@app.get("/predict/buses")
def get_best_buses(
    boarding_stop_id: str,
    destination_stop_id: str,
    traffic_level: str = "MEDIUM"
):
    return find_best_buses(
        boarding_stop_id,
        destination_stop_id,
        traffic_level
    )
