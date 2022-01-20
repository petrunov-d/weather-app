# Realtime Weather Application

A realtime weather application that retrieves the current weather for a given city.

## Prerequisites for local development

- JDK 17+ 
- Maven 4.0.0+
- (Optional) Docker for Mac/Windows/Linux if you want to run it locally in a container.

## General Info

#### default credentials are:
#### username: admin
#### password: admin.

To see the available endpoints, start the application then go to [The Local Swagger URL](http://localhost:8080/webjars/swagger-ui/index.html).

### Security

Basic Auth is implemented. To access the Swagger Docs and invoke the endpoint you can use the default credentials: admin/admin. If developed further
ideally they should be kept in a DB provider, or at least encrypted in the YAML configs and retrieved by a Spring config server.

### About the endpoint
A further development task would be to use OpenWeatherAPIs internal city Ids to avoid ambiguity. For ease of use I've done it using 
city name and an optional country parameter. This allows to narrow down results well enough and doesn't cater 
for just a few edge cases where countries have more than 1 city with the same name.

### Todos

To make this absolutely production ready I would also add:

- A Spring Config Server tied with the application - probably orchestrated through Docker compose.
