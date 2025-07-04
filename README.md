# ddd-cqrs-4-java-example
Example Java DDD/CQRS/Event Sourcing microservices with [Quarkus](https://quarkus.io/), [Spring Boot](https://spring.io/projects/spring-boot/) and the [EventStore](https://eventstore.org/) from Greg Young. The code uses the lightweight [ddd-4-java](https://github.com/fuinorg/ddd-4-java) and [cqrs-4-java](https://github.com/fuinorg/cqrs-4-java) libaries. No special framework is used except the well known JEE/Spring standards.

[![Java Development Kit 17](https://img.shields.io/badge/JDK-17-green.svg)](https://openjdk.java.net/projects/jdk/17/)
[![Apache](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://github.com/fuinorg/ddd-cqrs-4-java-example/actions/workflows/maven.yml/badge.svg)](https://github.com/fuinorg/ddd-cqrs-4-java-example/actions/workflows/maven.yml)

## Background
This application shows how to implement [DDD](https://en.wikipedia.org/wiki/Domain-driven_design), [CQRS](https://en.wikipedia.org/wiki/Command%E2%80%93query_separation) and [Event Sourcing](https://martinfowler.com/eaaDev/EventSourcing.html) without a DDD/CQRS framework. It uses just a few small libraries in addition to  standard web application frameworks like [Quarkus](https://quarkus.io/) and [Spring Boot](https://spring.io/projects/spring-boot/).

If you are new to the DDD/CQRS topic, you can use these mindmaps to find out more: 
- [DDD Mindmap](https://www.mindmeister.com/de/177813182/ddd)
- [CQRS Mindmap](https://www.mindmeister.com/de/177815383/cqrs)

Here is an overview of how such an application looks like: 

[![Overview](https://raw.github.com/fuinorg/ddd-cqrs-4-java-example/master/doc/cqrs-overview-small.png)](doc/cqrs-overview.png)

## Modules
- **Quarkus** - *Currently BROKEN* Two microservices (Command & Query) based on [Quarkus](https://quarkus.io/).
- **[Spring Boot](spring-boot)** - Two microservices (Command & Query) based on [Spring Boot](https://spring.io/projects/spring-boot/).

## Getting started
The following instructions are tested on Linux (Ubuntu 24)

**CAUTION:** Building and running on Windows will require some (small) changes.

### Prerequisites
Make sure you have the following tools installed/configured:
* [git](https://git-scm.com/) (VCS)
* [Docker CE](https://docs.docker.com/engine/installation/linux/docker-ce/ubuntu/)
* [Docker Compose](https://docs.docker.com/compose/)
* Hostname should be set in /etc/hosts (See [Find and Change Your Hostname in Ubuntu](https://helpdeskgeek.com/linux-tips/find-and-change-your-hostname-in-ubuntu/) for more information)

### Clone and install project 
1. Clone the git repository
   ```
   git clone https://github.com/fuinorg/ddd-cqrs-4-java-example.git
   ```
2. Change into the project's directory and run a Maven build
   ```
   cd ddd-cqrs-4-java-example
   ./mvnw install
   ```
   Be patient - This may take a while (~5 minutes) as all dependencies and some Docker images must be downloaded and also some integration tests will be executed.
   
### Start Event Store and Maria DB (Console window 1)
Change into the project's directory and run Docker Compose
```
cd ddd-cqrs-4-java-example
docker-compose up
```

### Start command / query implementations
Start one query service and then one command service.
You can mix Quarkus & Spring Boot if you want to!
* Quarkus *currently broken*
* [Spring Boot](spring-boot.md)

### Verify projection and query data
1. Open http://localhost:2113/ to access the event store UI (User: admin / Password: changeit)
   You should see a projection named "qry-person-stream" when you click on "Projections" in the top menu.
2. Opening http://localhost:8080/persons should show an empty JSON array
3. Opening http://localhost:8080/statistics should show an empty JSON array 

### Execute some create commands (Console window 4)
Change into the demo directory and execute the command using cURL (See [shell script](demo/create-persons.sh) and JSON files with commands in [demo](demo)) 
```
cd ddd-cqrs-4-java-example/demo
./create-persons.sh
```   
Command service (Console window 3) should show something like
```
Update aggregate: id=PERSON 954177c4-aeb7-4d1e-b6d7-3e02fe9432cb, version=-1, nextVersion=0
Update aggregate: id=PERSON 568df38c-fdc3-4f60-81aa-d3cce9ebfd7b, version=-1, nextVersion=0
Update aggregate: id=PERSON 84565d62-115e-4502-b7c9-38ad69c64b05, version=-1, nextVersion=0
```   
Query service (Console window 2) should show something like
```
Handle PersonCreatedEvent: Person 'Harry Osborn' (954177c4-aeb7-4d1e-b6d7-3e02fe9432cb) was created
Handle PersonCreatedEvent: Person 'Mary Jane Watson' (568df38c-fdc3-4f60-81aa-d3cce9ebfd7b) was created
Handle PersonCreatedEvent: Person 'Peter Parker' (84565d62-115e-4502-b7c9-38ad69c64b05) was created
```    

### Verify the query data was updated
1. Refreshing http://localhost:8080/persons should show
    ```json
    [
       {
           "id": "568df38c-fdc3-4f60-81aa-d3cce9ebfd7b",
           "name": "Mary Jane Watson"
       },
       {
           "id": "84565d62-115e-4502-b7c9-38ad69c64b05",
           "name": "Peter Parker"
       },
       {
           "id": "954177c4-aeb7-4d1e-b6d7-3e02fe9432cb",
           "name": "Harry Osborn"
       }
    ]
    ```
2. Opening http://localhost:8080/persons/84565d62-115e-4502-b7c9-38ad69c64b05 should show
    ```json
    {"id":"84565d62-115e-4502-b7c9-38ad69c64b05","name":"Peter Parker"}
   ```
3. Opening http://localhost:8080/statistics should show
   ```json
   [ { "type": "person", "count": 3 }  ]
   ```
4. The event sourced data of the person aggregate could be found in a stream named [PERSON-84565d62-115e-4502-b7c9-38ad69c64b05](http://localhost:2113/web/index.html#/streams/PERSON-84565d62-115e-4502-b7c9-38ad69c64b05)

### Execute a delete command (Console window 4)
Change into the demo directory and execute the command using cURL (See [shell script](demo/create-persons.sh) and JSON files with commands in [demo](demo))
```
cd ddd-cqrs-4-java-example/demo
./delete-harry-osborn.sh
```   
### Verify the query data was updated
1. Refreshing http://localhost:8080/persons should show
    ```json
    [
       {
           "id": "568df38c-fdc3-4f60-81aa-d3cce9ebfd7b",
           "name": "Mary Jane Watson"
       },
       {
           "id": "84565d62-115e-4502-b7c9-38ad69c64b05",
           "name": "Peter Parker"
       }
    ]
    ```
    "Harry Osborn" should no longer be present in the list.
2. Opening http://localhost:8080/statistics should show
   ```json
   [ { "type": "person", "count": 2 }  ]
   ```

### Stop Event Store and Maria DB and clean up
1. Stop Docker Compose (Ubuntu shortcut = ctrl c)
2. Remove Docker Compose container
   ```   
   docker-compose rm
   ```
