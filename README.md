# Intuit Comment Design System

### Brief

A comments system service for a social media post which can support scalable levels
of nesting. All the comments have associated likes, dislikes and also nested replies.
 Service uses for now basic auth for authentication and also allows registration of new users.
Service also provides to show the posts along with comments, used for demonstration purpose only

Refer postman collection for api documentation 

[API Postman Collection](./artifacts/Inuit%20Comment%20Design%20System.postman_collection.json)

Refer the [Help Documentation](./HELP.md) to set up the service and run it

## High Level Details

### Assumptions
 - The system is designed to provide only http apis and doesn't provide dynamic update pushes
 - For demo purpose horizontal partition mechanism binds to only 1 data source
 - This setup doesn't include load balancer or server cluster setup which is important for production level
 - No Instrumentation or alert mechanisms which are required at production level

We would explain with layered architecture to show our services design

### Application Layer
This layer contains all stuff required to communicate with outside service. 
It includes the rest apis, authentication, api level validations, frontend urls etc..
The classes in this layer call the service layer and depend on service layer for performing operations
This layers includes
 - Spring Security provided basic auth
 - Rest API Controllers for APIs. Refer postman collection for api level details
 - Frontend urls provided Spring MVC Thymeleaf framework
   - Shows Sample Posts
   - Allows users to register
 
Application layer provides for all these features using service layer

### Service Layer
The Service layer provides the main features directly used application layer
This layer includes
* PostCommentService
  * Creates and updates comments
* UserAuthDetailsService
  * Provides user details to application layer basic auth
* PostPartitionService
  * Uses consistent hashing to provide horizontal partitioning for comments to provide independent DAO Layers
* UserPartitionService
   * Uses consistent hashing to provide horizontal partitioning for user details to provide independent DAO Layers

### Dao Layer
DAO layer implements the required persistence and data object query operations based on partitioning on service layer. We use
 spring data jpa persistence framework connected with mysql data sources. Refer ERP diagram for schema details


## Low Level Design

#### Class Diagram

![Not Loading](./artifacts/classDiagram.png?raw=true)

#### Database Diagram

![Not Loading](./artifacts/dbSchemaDiagram.PNG?raw=true)


### Tech Stack
 Service stack includes
 - Spring Boot
   - Spring Security
   - Spring Web
   - Spring Thymeleaf
   - Spring Data JPA
   - Spring Web Validation
 - Java - Programming language
 - MySQL - Database
 - Gradle - Build tool
 - Liquibase for migrations
 - Lombok
 - Frontend Components 
   - JQuery 
   - HTML
   - JS
   - CSS
   - Materialize
 
 Testing stack includes
 - JUnit
 - Test containers
 - Jacoco - code coverage

### Project Guidelines
- [Code Style Guide Followed](https://google.github.io/styleguide/javaguide.html)




