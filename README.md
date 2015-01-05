# Import project to IDE

mvn eclipse:eclipse

then Import->Existing project into workspace

I suppose similar approach can be used for IDEA project creation, however, this was not tested

# Run tests

mvn test

This will run unit tests and integration tests. Make sure that port 8080 is not busy when running tests

# Run application with in memory DB with development data 

mvn spring-boot:run

or from eclipse 'run as java application' (or spring boot application, if you have spring plugin) by clicking on HomeworkApplication

note: in memory db is not persistent for dev profile, so expect to get a clean set of test data from  /homework/src/main/resources/import.sql with every restart

# Data operation
GET 	/questions						list all accepted questions
	optional url parameter country can be provided to filter by country code(uppercase)

GET		/questions/{id}					get single question by id(regardless of status)

POST 	/questions						submit new question
	parameter 'content' should contain question text

## Dev and verification util
When using dev profile, an additional api endpoint is exposed to check db content. Dev profile is active by default.

GET 	/_questions						list all questions in db, ignoring status

GET		/_blacklist						lists all configured blacklist words


# Production config
Depending on chosen production env. project can be compiled into war file for externally managed container or executable uber jar can be created to use embedded tomcat container as in dev mode. MySQL database sample configuration is provided in application-prod.yml file. To run in production mode spring.profiles.active should be changed to use 'prod' profile. For first production run use the setting ddl-auto: create-drop in application-prod.yml to create schema and tables with initial data. After that change setting to ddl-auto: none

To asseble production ready jar use prod profile 
mvn package -P prod -DskipTests

note: tests are skipped since we do only assemble and do not want to use mysql db yet  

