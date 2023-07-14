# Load Balancer with Microservices

This project demonstrates a load balancer implementation using Spring Boot and multiple microservices. The load balancer distributes incoming requests across two microservices to achieve better resource utilization and scalability.

This is a implementation on layer 4 load balancer, which establishes TCP connection to the client and server and transfers request from client to server and returns resoponse from server to client.

It doesn't know about the data and other headers of the request, because it works on network layer.
It redirects request from client to the selected server without knowing about the data of the request.

## Project Structure

The project consists of the following modules:

- `L4/load-balancer`: The load balancer module responsible for distributing incoming requests.
- `L4/micro-service-one`: The first microservice module.
- `L4/micro-service-two`: The second microservice module.

## Prerequisites

Before running the project, ensure that you have the following prerequisites installed:

- Java Development Kit (JDK) 8 or later
- Maven

## Running the Microservices

1. Navigate to the `L4/micro-service-one` directory.

2. Run the following command to build and start the microservice:

```shell
mvn spring-boot:run
```

3. Repeat the above steps for the `L4/micro-service-two` directory to start the second 

## Running the Load Balancer

1. Navigate to the `L4/load-balancer` directory.
2. Navigate to the `/L4/load-balancer/src/main/java/com/devops/lb/network/LoadBalance.java` file.
3. Build and Run this java file.

## Usage

1. Open your linux terminal and run following command.

```shell
curl GET "http://localhost/8080/home"
```

You will get `From port 8081` and `From port 8082` alternatively. Because load balancer is distributing incoming request on port 8080, to the ports 8081 and 8082 alternatively using round robin algorithm.

## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvement, please open an issue or submit a pull request in the respective sub-project repository.

## Acknowledgements

- [Spring Boot](https://spring.io/projects/spring-boot): Used for creating the microservices and load balancer.
- [Dilip Thakar](https://github.com/dilipthakkar/): Author
