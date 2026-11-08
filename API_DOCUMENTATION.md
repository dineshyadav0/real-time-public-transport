# Haryana Roadways Backend API Reference

Local base URL: `http://localhost:8081`

This reference follows the inspected controller mappings and DTOs.
Entity request fields not shown by the inspected DTOs are deliberately
not guessed.

## Health

-   `GET /api/health` --- returns
    `Where Is My Haryana Roadways Backend is Running!`.

## Buses --- `/api/buses`

  Method   Path                Purpose
  -------- ------------------- --------------------------
  POST     `/api/buses`        Create (`Bus` JSON body)
  GET      `/api/buses`        List all
  GET      `/api/buses/{id}`   Get by ID
  PUT      `/api/buses/{id}`   Update (`Bus` JSON body)
  DELETE   `/api/buses/{id}`   Delete; 204

## Routes --- `/api/routes`

  Method   Path                 Purpose
  -------- -------------------- ----------------------------
  POST     `/api/routes`        Create (`Route` JSON body)
  GET      `/api/routes`        List all
  GET      `/api/routes/{id}`   Get by ID
  PUT      `/api/routes/{id}`   Update (`Route` JSON body)
  DELETE   `/api/routes/{id}`   Delete; 204

## Bus stops --- `/api/stops`

  Method   Path                               Purpose
  -------- ---------------------------------- ------------------------------
  POST     `/api/stops`                       Create (`BusStop` JSON body)
  GET      `/api/stops`                       List all
  GET      `/api/stops/{id}`                  Get by ID
  GET      `/api/stops/district/{district}`   List by district
  PUT      `/api/stops/{id}`                  Update (`BusStop` JSON body)
  DELETE   `/api/stops/{id}`                  Delete; 204

## Route-stop associations --- `/api/route-stops`

  -----------------------------------------------------------------------------
  Method                  Path                          Parameters / purpose
  ----------------------- ----------------------------- -----------------------
  POST                    `/{routeId}/stops/{stopId}`   Add association; query
                                                        parameter `stopOrder`
                                                        (integer)

  GET                     `/{routeId}`                  List stops for route

  GET                     `/stop/{stopId}`              List route associations
                                                        for stop

  DELETE                  `/{id}`                       Remove association
  -----------------------------------------------------------------------------

Example: `POST /api/route-stops/3/stops/8?stopOrder=1`

## Schedules and journey search --- `/api/schedules`

  ---------------------------------------------------------------------------------------------------------------------------
  Method                  Path                                                                        Parameters / purpose
  ----------------------- --------------------------------------------------------------------------- -----------------------
  POST                    `/api/schedules`                                                            Create (`BusSchedule`
                                                                                                      JSON body)

  GET                     `/api/schedules`                                                            List all

  GET                     `/api/schedules/{id}`                                                       Get by ID

  GET                     `/api/schedules/route/{routeId}`                                            List by route

  GET                     `/api/schedules/depot/{depotName}`                                          List by depot

  GET                     `/api/schedules/time?start=...&end=...`                                     Time range
                                                                                                      (`LocalTime`)

  GET                     `/api/schedules/search?source=...&destination=...&travelDate=YYYY-MM-DD`    Schedule search

  GET                     `/api/schedules/journey?source=...&destination=...&travelDate=YYYY-MM-DD`   Journey search DTO

  PUT                     `/api/schedules/{id}`                                                       Update (`BusSchedule`
                                                                                                      JSON body)

  DELETE                  `/api/schedules/{id}`                                                       Delete; 204
  ---------------------------------------------------------------------------------------------------------------------------

Example:
`GET /api/schedules/journey?source=Rohtak&destination=Delhi&travelDate=2026-11-08`

Journey results expose: `scheduleId`, `routeNumber`, `routeHeading`,
`depotName`, `source`, `destination`, `via`, `serviceType`,
`operatingDays`, `travelDate`, `boardingTime`, `arrivalTime`,
`sourceDocument`, `sourcePage`, `verified`. `arrivalTime` may be null
where the source timetable does not provide it; do not invent missing
times.

## Schedule-stop associations --- `/api/schedule-stops`

  --------------------------------------------------------------------------------
  Method                  Path                             Parameters / purpose
  ----------------------- -------------------------------- -----------------------
  POST                    `/{scheduleId}/stops/{stopId}`   Add stop; required
                                                           `stopOrder`; optional
                                                           `arrivalTime`,
                                                           `departureTime`

  GET                     `/{scheduleId}`                  List stops for schedule
                                                           in order

  GET                     `/stop/{stopId}`                 Find schedule
                                                           associations for stop

  DELETE                  `/{id}`                          Remove association; 204
  --------------------------------------------------------------------------------

Time parameters are parsed as Java `LocalTime` strings, e.g. `05:10` or
`05:10:00`.

## Timetable imports --- `/api/timetable-import`

Both endpoints consume `multipart/form-data`. -
`POST /api/timetable-import/upload`: form fields `schedulesFile` and
`stopsFile`. - `POST /api/timetable-import/rohtak`: form field `file`
(CSV input).

Example PowerShell upload:

``` powershell
curl.exe -X POST "http://localhost:8081/api/timetable-import/rohtak" `
  -F "file=@C:\path\to\rohtak-timetable.csv"
```

Import response fields: `totalRecords`, `importedRecords`,
`skippedRecords`, `failedRecords`, `messages`, `errors`. The Rohtak
endpoint accepts CSV, not the original PDF.

## Trips --- `/api/trips`

  Method   Path                         Purpose
  -------- ---------------------------- ---------------------------
  POST     `/api/trips`                 Create (`Trip` JSON body)
  GET      `/api/trips`                 List all
  GET      `/api/trips/{id}`            Get by ID
  PUT      `/api/trips/{id}`            Update (`Trip` JSON body)
  PATCH    `/api/trips/{id}/start`      Start trip; no body
  PATCH    `/api/trips/{id}/complete`   Complete trip; no body
  PATCH    `/api/trips/{id}/cancel`     Cancel trip; no body
  DELETE   `/api/trips/{id}`            Delete; 204

Lifecycle transitions are validated. GPS submissions are accepted only
while the trip is `RUNNING`; invalid transitions/non-running GPS
submissions return HTTP 409.

## GPS tracking --- `/api/gps`

  Method   Path                              Purpose
  -------- --------------------------------- -------------------------------------------
  POST     `/api/gps`                        Save GPS reading and broadcast after save
  GET      `/api/gps`                        List readings
  GET      `/api/gps/trip/{tripId}`          List readings for trip
  GET      `/api/gps/trip/{tripId}/latest`   Latest reading for trip
  GET      `/api/gps/{id}`                   Get reading by ID
  DELETE   `/api/gps/{id}`                   Delete; 204

GPS POST accepts a `GPSLocation` entity. The tested request shape uses
nested `trip.id`:

``` json
{
  "trip": { "id": 12 },
  "latitude": 28.8957,
  "longitude": 76.6068,
  "speed": 30
}
```

The server assigns `recordedAt` when omitted. GPS response fields are
scalar: `id`, `tripId`, `latitude`, `longitude`, `speed`, `recordedAt`
(not a nested `trip` object). Example:

``` json
{
  "id": 7,
  "tripId": 12,
  "latitude": 28.8957,
  "longitude": 76.6068,
  "speed": 30.0,
  "recordedAt": "2026-11-08T22:11:21.626218800"
}
```

Agree with the mobile team on the speed unit and use it consistently.

## Live GPS WebSocket

-   SockJS endpoint: `http://localhost:8081/ws`
-   STOMP topic: `/topic/trips/{tripId}`, e.g. `/topic/trips/12`
-   Message fields: `tripId`, `latitude`, `longitude`, `speed`,
    `recordedAt`.
-   A message is broadcast only after the database save succeeds.
-   Configure allowed origins with `WEBSOCKET_ALLOWED_ORIGINS`. The
    default `*` is for local development; restrict it before deployment.

## Admin schedule verification --- `/api/admin/schedules`

Both endpoints require header `X-Admin-Key`. \| Method \| Path \|
Parameters / purpose \| \|---\|---\|---\| \| GET \|
`/api/admin/schedules?verified=false` \| List schedules by verification
state \| \| PATCH \|
`/api/admin/schedules/{id}/verification?verified=true` \| Set
verification state \|

Missing/invalid key returns HTTP 401. Configure the server-side key with
`ROADWAYS_ADMIN_API_KEY`. Never bundle this key in the passenger or
driver app. This is a basic API-key gate, not full user
authentication/authorization.

## Mobile integration flow

1.  Load buses/routes from `GET /api/buses` and `GET /api/routes`.
2.  Search journeys with `GET /api/schedules/journey` and `source`,
    `destination`, `travelDate`.
3.  Driver starts trip using `PATCH /api/trips/{id}/start`.
4.  Driver posts GPS readings to `POST /api/gps` while trip is running.
5.  Passenger app subscribes to `/topic/trips/{tripId}` over SockJS
    `/ws` and updates the map.
6.  Driver completes trip using `PATCH /api/trips/{id}/complete`.

On a physical Android device, `localhost` means the phone, not the
development PC. Use the PC's reachable LAN IP during local testing or a
deployed backend URL. Use the same host for HTTP and WebSocket.

## Configuration and deployment cautions

-   PostgreSQL settings: `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`.
-   Admin key: `ROADWAYS_ADMIN_API_KEY`.
-   WebSocket origins: `WEBSOCKET_ALLOWED_ORIGINS`.
-   Current development configuration has SQL statement logging
    disabled.
-   `spring.jpa.hibernate.ddl-auto=update` is convenient for
    development; use controlled migrations in production.
-   Use HTTPS/WSS, restrict WebSocket origins, rotate credentials that
    may have been exposed, and never ship database credentials/admin key
    in the app.
-   The inspected CRUD controllers do not show authentication for bus,
    route, stop, schedule, or trip management. Add proper authorization
    before public deployment.
-   Exact JSON fields for `Bus`, `Route`, `BusStop`, `BusSchedule`, and
    `Trip` are defined by their entity classes; this reference does not
    fabricate those fields.
