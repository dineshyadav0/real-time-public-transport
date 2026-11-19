from flask import Flask, request, jsonify
import joblib
import pandas as pd
import os

app = Flask(__name__)

# Model load karo
model_path = os.path.join('models', 'eta_model.pkl')
if os.path.exists(model_path):
    loaded_data = joblib.load(model_path)
    
    # Check karo ki loaded data dictionary hai ya direct model object
    if isinstance(loaded_data, dict):
        eta_model = loaded_data.get('model', loaded_data) 
    else:
        eta_model = loaded_data
        
    print("ETA Model loaded successfully!")
else:
    eta_model = None
    print("Warning: ETA model not found at", model_path)

@app.route('/predict-eta', methods=['POST'])
def predict_eta():
    if not eta_model:
        return jsonify({'error': 'Model not loaded on server'}), 500
    
    data = request.get_json()
    
    try:
        # Map incoming keys to exact training feature names
        features = pd.DataFrame([{
            'bus_id': 0,
            'route_id': 0,
            'distance_remaining': data.get('distance_remaining', data.get('distance_km', 5.0)),
            'average_speed': data.get('average_speed', data.get('avg_speed_kmh', 30.0)),
            'traffic_level': 1,
            'hour': data.get('hour', 12),
            'day_of_week': data.get('day_of_week', 1),
            'stops_remaining': data.get('stops_remaining', data.get('num_stops', 2))
        }])
        
        prediction = eta_model.predict(features)
        estimated_minutes = float(prediction[0])
        
        return jsonify({
            'success': True,
            'estimated_eta_minutes': round(estimated_minutes, 2)
        })
        
    except Exception as e:
        return jsonify({'success': False, 'error': str(e)}), 400

@app.route('/health', methods=['GET'])
def health_check():
    return jsonify({'status': 'Flask ML Service is running!'})

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000, debug=True)