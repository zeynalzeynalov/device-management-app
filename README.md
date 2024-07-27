# Device management app

## Solution:

Implemented a back end solution with Device management operations over REST endpoints.
API REST Controller persists devices data at MySQL database via Device Service.

### Tech stack:

- Java
- Spring Boot
- Maven
- JUnit
- Lombok (boilerplate code reducer)


- MySQL
- Docker Compose
- Postman

### Starting the app:
Executing following script will start application.
```
./start-app.sh
```

### Supported REST actions for devices:

**Create**
- [POST] http://localhost:8080/api/devices/v1

**Get all devices list**
- [GET] http://localhost:8080/api/devices/v1

**Get a device by id**
- [GET] http://localhost:8080/api/devices/v1/1

**Get all filtered by device brand name**
- [GET] http://localhost:8080/api/devices/v1/filter?brand=test

**Update**
- [PUT] http://localhost:8080/api/devices/v1/1

Delete
- [DELETE] http://localhost:8080/api/devices/v1/1

**Additional: Get Device API status**

- [GET] http://localhost:8080/api/devices/v1/status

## Additional notes

- REST API supports versions via API URI as:
  - `http://localhost:8080/api/devices/v1`


- MySQL Docker container is using external port 3307 which is configurable in Docker Compose file:
  - `docker-compose.yml`


- How to test API endpoints externally:
  - There is a Postman collection file at the root folder that can be imported to Postman and test endpoints.
  - Collection file: `device-management-app-postman-collection.json`


- Unit tests are implemented for Device REST Controller for several scenarions under test file:
  - `src/test/java/org/abc/app/api/DeviceControllerTest.java`