# Mobile-to-Backend Integration Review

Review date: 2026-11-20

Target base: `feature/backend`
Working branch: `integration/mobile-backend`

## Safety
- This branch was created from `feature/backend`.
- Do not merge `feature/mobile` wholesale: its history diverges from the backend branch.
- Keep `feature/backend` unchanged until the integration branch is locally built and tested.

## Backend endpoints verified on feature/backend
- `GET /api/buses`
- `GET /api/routes`
- `GET /api/stops`
- `GET /api/trips`
- `GET /api/trips/{id}`
- `PATCH /api/trips/{id}/start`
- `PATCH /api/trips/{id}/complete`
- `POST /api/gps`
- `GET /api/gps/trip/{tripId}/latest`
- `GET /api/schedules/journey?source=...&destination=...&travelDate=YYYY-MM-DD`

## GPS contract verified
POST `/api/gps` accepts a GPSLocation entity with a nested trip reference, for example:
```json
{
  "trip": { "id": 12 },
  "latitude": 28.8957,
  "longitude": 76.6068,
  "speed": 30
}
```

The response DTO returns scalar `tripId`, `latitude`, `longitude`, `speed`, and `recordedAt`.

## WebSocket contract verified
- SockJS endpoint: `/ws`
- STOMP broker prefix: `/topic`
- Trip topic: `/topic/trips/{tripId}`
- The backend broadcasts after the GPS record has been saved.

The mobile raw-WebSocket URL `/ws/websocket` needs an actual device/emulator integration test against the SockJS endpoint. Do not assume this transport path is compatible until tested.

## Mobile issues to resolve
1. Configure backend host for the device's network; do not rely on a hard-coded LAN address.
2. Verify WebSocket connection and reconnection on Android/iOS against the backend SockJS endpoint.
3. Distinguish fallback/sample bus data from live API data in the UI.
4. Remove or label placeholder map coordinates and inferred route-stop values.
5. Verify that GPS uploads start only after trip start succeeds and stop after trip completion succeeds.
6. Check API errors, loading states, permissions, and stale GPS timestamps.

## Important repository discrepancy
The currently pushed `feature/backend/backend/pom.xml` declares Java 17 and H2 runtime, whereas the previously tested local backend setup was described as Java 21 and PostgreSQL. The pushed `backend/src/main/resources/application.properties` was not found at the expected path during review. Reconcile the local working tree and pushed branch before changing backend dependencies or configuration.

## Not yet verified
- Mobile build and TypeScript compilation.
- Real Android device GPS upload.
- End-to-end STOMP/SockJS live tracking.
- ML prediction accuracy or model loading.
- Production CORS/origin and deployment settings.

No production code or existing backend branch has been modified by this review.
