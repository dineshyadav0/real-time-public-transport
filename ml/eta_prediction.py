
import pandas as pd
from pathlib import Path

DATASET_DIR = Path(__file__).parent / "dataset"


def predict_passenger_eta(
    bus_id: str,
    boarding_stop_id: str,
    destination_stop_id: str,
    already_onboard: bool = False,
    traffic_level: str = "MEDIUM"
):
    # Load datasets
    buses = pd.read_csv(DATASET_DIR / "bus_positions.csv")
    stops = pd.read_csv(DATASET_DIR / "stops.csv")
    history = pd.read_csv(DATASET_DIR / "journey_history.csv")
    traffic = pd.read_csv(DATASET_DIR / "traffic_data.csv")

    # Find bus
    bus_data = buses[buses["bus_id"] == bus_id]

    if bus_data.empty:
        return {"error": "Bus not found"}

    bus = bus_data.iloc[0]
    route_id = bus["route_id"]

    current_position = float(bus["distance_from_start_km"])
    current_speed = float(bus["current_speed_kmph"])

    if current_speed <= 0:
        return {"error": "Bus speed must be greater than zero"}

    # Find traffic settings for this route and traffic level
    traffic_level = traffic_level.upper()

    traffic_data = traffic[
        (traffic["route_id"] == route_id) &
        (traffic["traffic_level"] == traffic_level)
    ]

    if traffic_data.empty:
        return {"error": "Traffic data not found for this route and level"}

    traffic_data = traffic_data.iloc[0]

    speed_factor = float(traffic_data["speed_factor"])
    stop_wait = float(traffic_data["stop_wait_minutes"])

    if speed_factor <= 0 or stop_wait < 0:
        return {"error": "Invalid traffic settings"}

    # Find route stops
    route_stops = stops[stops["route_id"] == route_id].copy()

    # Get historical average speed
    route_history = history[history["route_id"] == route_id]

    if route_history.empty:
        return {"error": "No historical speed data available"}

    average_speed = float(
        route_history["average_speed_kmph"].mean()
    )

    if average_speed <= 0:
        return {"error": "Invalid historical average speed"}

    # Traffic-adjusted speeds
    adjusted_bus_speed = current_speed * speed_factor
    adjusted_journey_speed = average_speed * speed_factor

    # Already onboard: calculate only remaining journey
    if already_onboard:
        destination = route_stops[
            route_stops["stop_id"] == destination_stop_id
        ]

        if destination.empty:
            return {"error": "Destination stop not found on this route"}

        destination_position = float(
            destination.iloc[0]["distance_from_start_km"]
        )

        remaining_distance = destination_position - current_position

        if remaining_distance < 0:
            return {"error": "Destination is behind the bus"}

        intermediate_stops = route_stops[
            (route_stops["distance_from_start_km"] > current_position) &
            (route_stops["distance_from_start_km"] < destination_position)
        ]

        waiting_time = len(intermediate_stops) * stop_wait

        journey_eta = (
            (remaining_distance / adjusted_journey_speed) * 60
            + waiting_time
        )

        return {
            "bus_id": bus_id,
            "already_onboard": True,
            "traffic_level": traffic_level,
            "adjusted_speed_kmph": round(adjusted_journey_speed, 2),
            "remaining_distance_km": round(remaining_distance, 2),
            "intermediate_stops": len(intermediate_stops),
            "stop_waiting_minutes": round(waiting_time, 2),
            "journey_eta_minutes": round(journey_eta, 2),
            "total_eta_minutes": round(journey_eta, 2),
            "prediction_type": "Traffic-adjusted baseline"
        }

    # Find boarding and destination stops
    boarding = route_stops[
        route_stops["stop_id"] == boarding_stop_id
    ]

    destination = route_stops[
        route_stops["stop_id"] == destination_stop_id
    ]

    if boarding.empty or destination.empty:
        return {"error": "Boarding or destination stop not found on this route"}

    boarding_position = float(
        boarding.iloc[0]["distance_from_start_km"]
    )

    destination_position = float(
        destination.iloc[0]["distance_from_start_km"]
    )

    if boarding_position < current_position:
        return {"error": "This bus has already passed your boarding stop"}

    if destination_position <= boarding_position:
        return {"error": "Destination must be after boarding stop"}

    # Calculate distances
    bus_to_boarding = boarding_position - current_position
    passenger_journey = destination_position - boarding_position

    # Count intermediate stops on each part of the journey
    stops_before_boarding = route_stops[
        (route_stops["distance_from_start_km"] > current_position) &
        (route_stops["distance_from_start_km"] < boarding_position)
    ]

    stops_during_journey = route_stops[
        (route_stops["distance_from_start_km"] > boarding_position) &
        (route_stops["distance_from_start_km"] < destination_position)
    ]

    bus_stop_wait = len(stops_before_boarding) * stop_wait
    journey_stop_wait = len(stops_during_journey) * stop_wait

    # Calculate traffic-adjusted ETAs
    bus_arrival_eta = (
        (bus_to_boarding / adjusted_bus_speed) * 60
        + bus_stop_wait
    )

    journey_eta = (
        (passenger_journey / adjusted_journey_speed) * 60
        + journey_stop_wait
    )

    total_eta = bus_arrival_eta + journey_eta

    return {
        "bus_id": bus_id,
        "route_id": route_id,
        "boarding_stop": boarding_stop_id,
        "destination_stop": destination_stop_id,
        "traffic_level": traffic_level,
        "adjusted_bus_speed_kmph": round(adjusted_bus_speed, 2),
        "adjusted_journey_speed_kmph": round(adjusted_journey_speed, 2),
        "bus_to_boarding_distance_km": round(bus_to_boarding, 2),
        "passenger_journey_distance_km": round(passenger_journey, 2),
        "stops_before_boarding": len(stops_before_boarding),
        "stops_during_journey": len(stops_during_journey),
        "bus_stop_wait_minutes": round(bus_stop_wait, 2),
        "journey_stop_wait_minutes": round(journey_stop_wait, 2),
        "bus_arrival_eta_minutes": round(bus_arrival_eta, 2),
        "journey_eta_minutes": round(journey_eta, 2),
        "total_eta_minutes": round(total_eta, 2),
        "prediction_type": "Traffic-adjusted baseline"
    }


def find_best_buses(
    boarding_stop_id: str,
    destination_stop_id: str,
    traffic_level: str = "MEDIUM"
):
    buses = pd.read_csv(DATASET_DIR / "bus_positions.csv")

    available_buses = []

    for _, bus in buses.iterrows():
        result = predict_passenger_eta(
            bus_id=bus["bus_id"],
            boarding_stop_id=boarding_stop_id,
            destination_stop_id=destination_stop_id,
            already_onboard=False,
            traffic_level=traffic_level
        )

        if "error" not in result:
            available_buses.append(result)

    available_buses.sort(
        key=lambda bus: bus["total_eta_minutes"]
    )

    return {
        "boarding_stop": boarding_stop_id,
        "destination_stop": destination_stop_id,
        "traffic_level": traffic_level.upper(),
        "available_bus_count": len(available_buses),
        "buses": available_buses
    }
