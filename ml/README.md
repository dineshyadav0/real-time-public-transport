# Haryana Roadways Machine Learning (AI/ML) Service

This module powers intelligent transit predictions for the Haryana Roadways tracking system:
1. **ETA Prediction Engine (`app.py`)**: Flask microservice serving real-time ETA estimates using the trained Random Forest regression model (`models/eta_model.pkl`) on port `5000`.
2. **Transit Recommendation API (`main.py`)**: FastAPI service computing passenger arrival times and optimal bus route recommendations across stops and traffic levels.
3. **Model Training Pipeline (`train_eta_model.py`)**: Pipeline for training, evaluating, and serializing ETA prediction models on historical transit data.

---

## 1. Setup & Environment

Ensure Python 3.10+ is installed:

```bash
cd ml
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
```

---

## 2. Running the Services

### Option A: Flask ETA Microservice (Default for Backend Integration)
```bash
python app.py
```
- Runs on `http://127.0.0.1:5000`
- **Health Check**: `GET http://127.0.0.1:5000/health`
- **Predict ETA**: `POST http://127.0.0.1:5000/predict-eta`
  ```json
  {
    "distance_remaining": 8.5,
    "average_speed": 35.0,
    "traffic_level": 1,
    "hour": 14,
    "day_of_week": 3,
    "stops_remaining": 4
  }
  ```
  **Response**:
  ```json
  {
    "success": true,
    "estimated_eta_minutes": 14.25
  }
  ```

### Option B: FastAPI Interactive Documentation
```bash
uvicorn main:app --reload --port 8000
```
- Swagger UI available at `http://127.0.0.1:8000/docs`
- Predict ETA: `GET /predict/eta?bus_id=102&boarding_stop_id=1&destination_stop_id=3`
- Best Buses: `GET /predict/buses?boarding_stop_id=1&destination_stop_id=3`

---

## 3. Retraining the Model

To retrain the model on updated historical journey data:
```bash
python generate_dummy_data.py   # Generates simulated fleet logs
python train_eta_model.py       # Trains model and saves to models/eta_model.pkl
```
