import os
import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import LabelEncoder
from sklearn.metrics import mean_absolute_error, mean_squared_error, r2_score
import joblib

def train_model():
    # 1. Load dataset
    data_path = 'eta_training_data.csv'  # Adjust path if located in a subfolder
    if not os.path.exists(data_path):
        raise FileNotFoundError(f"Dataset not found at {data_path}. Run generate_dummy_data.py first.")
    
    df = pd.read_csv(data_path)
    print("Dataset loaded successfully. Shape:", df.shape)

    # 2. Preprocess categorical features (e.g., traffic_level)
    label_encoders = {}
    for col in ['traffic_level', 'bus_id', 'route_id']:
        if col in df.columns:
            le = LabelEncoder()
            df[col] = le.fit_transform(df[col].astype(str))
            label_encoders[col] = le

    # 3. Define features (X) and target (y)
    feature_cols = [
        'bus_id', 'route_id', 'distance_remaining', 
        'average_speed', 'traffic_level', 'hour', 
        'day_of_week', 'stops_remaining'
    ]
    
    X = df[feature_cols]
    y = df['eta_minutes']

    # 4. Train-test split (80:20)
    X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)

    # 5. Initialize and train Random Forest Regressor
    model = RandomForestRegressor(n_estimators=100, random_state=42)
    model.fit(X_train, y_train)

    # 6. Evaluate model performance
    y_pred = model.predict(X_test)
    mae = mean_absolute_error(y_test, y_pred)
    rmse = np.sqrt(mean_squared_error(y_test, y_pred))
    r2 = r2_score(y_test, y_pred)

    print(f"--- Model Evaluation ---")
    print(f"Mean Absolute Error (MAE): {mae:.2f} minutes")
    print(f"Root Mean Squared Error (RMSE): {rmse:.2f}")
    print(f"R² Score: {r2:.2f}")

    # 7. Save model and encoders
    os.makedirs('models', exist_ok=True)
    model_path = os.path.join('models', 'eta_model.pkl')
    joblib.dump({'model': model, 'encoders': label_encoders}, model_path)
    print(f"Model saved successfully to {model_path}")

if __name__ == '__main__':
    train_model()