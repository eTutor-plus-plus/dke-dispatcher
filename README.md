tes
# eTutor-Grading
The application housing all (most) expert-modules of the etuorr++ system.

Currently available modules:
SQL, Relational Algebra, Datalog, XQuery, Process Mining

Typical features of an expert module:
- Managing exercises
- Grading submissions of exercises

The application is developed as a Spring Boot application with REST-endpoints.
There is one set of endpoints common to all modules, for submitting submissions and fetching gradings of evaluated submissions.
Additionally, there is a set of endpoints for each module, where exercises can be managed.

When the application is running with default configuration, you can explore the REST-interface here:
http://localhost:8081/swagger-ui.html#/ (if you changed the port, you have to adjust the url).

This readme serves as a quickstart for contributors, with the following chapters:
1. Development
2. Building
3. Deployment
4. Integrating a module

# 1. Development
This section explains our GIT-practices and how to set up the local development environment.

## GIT
Obviously, we are using git as VCS.

Branches:
- main: The main branch, never commit to this one without approval.
- develop: The development branch
- feature/xyz: Different features

If you are adding a new feature, create a feature/xyz branch from the development branch.
Discuss before finishing the feature and merging it back to develop.

## Setup
For the setup, you need to install some legacy jars, set-up the database and install the dependency to the [objects-project](https://github.com/eTutor-plus-plus/objects).
Those steps are necessary, independent of the module you are intending to use.
Additionally, the different modules may also require some configurations.

Dependencies:
- Java
- Maven
- Docker (optional but recommended)

### Legacy Jars
Some expert modules require libraries which are not available over public Maven repositories.
Therefore, those libraries, which are located in the project, need to be installed to your private maven repository on your local machine, before you can start the application.

In order to do so, open a terminal in the root-folder and execute the following command:
```shell
./install_legacy_jars.bat
```

This executes the [batch-script](./install_legacy_jars.bat) with the different commands, one for each legacy jar-file.
On a Linux-System, use the [shell-file](./install_legacy_jars.sh) with the .sh extension.

### Database 
To start all necessary databases for the modules and the app, it is best to use Docker.
Then you can execute the following [file](./setup-database.bat) to build the image with the sql-script and start the container:

```shell
./setup-database.bat
```
If you do not want to use Docker, you have to import the [sql-script](./resources/database/4.%20docker/all_dump.sql) into your PostgreSQL-server.

### Objects (Dependency)
Objects common to different service of the etutor++ ecosystem are maintained in the [objects-project](https://github.com/eTutor-plus-plus/objects).
To run a service depending on this project (like this one), you have to clone the repository and build the project:
```shell
git clone  https://github.com/eTutor-plus-plus/objects
cd objects
mvn clean install
```

Alternatively, you could donwload the latest jar from the [github action workflows](https://github.com/eTutor-plus-plus/objects/actions) in the repository of the objects-project. Click on the latest workflow and then on artifacts to do so. However, it would be required to install the jar into your local maven repository, so maybe installing it manually like described above is easier.

Afterwards, dependent projects can be built.

### Modules
This section will detail, for every module, additional steps and configurations that have to be taken.
Most configuration is externalized in the [properties-file](./src/main/resources/application.properties).
The module-specific properties with the id *connUrl* refer to the name of the module-specific database. 

#### SQL and RA
No further steps required. The configuration of the SQL-Module is managed with the following parameters:

```properties
#-----------------------------------------------------------
#SQL: module-specific properties
#-----------------------------------------------------------
# Connection details for the user, with which submissions from students are executed:
application.sql.connPwd=sql
application.sql.connUser=sql

# Further connection details
application.sql.connUrl=sql
# The database where new taskgroups and exercises are created
application.sql.exerciseDatabase=sql_exercises

# Suffixes distinguishing the submission and diagnose-version of exercise definitions
application.sql.submissionSuffix=_submission
application.sql.diagnoseSuffix=_diagnose

# Path-parameter for frontend
application.sql.tableUrlForLink= /sql-tables/
```
#### Datalog
For datalog, [DLV](https://www.dlvsystem.it/dlvsite/dlv-download/) is required.
Download the static executable and configure it as part of the configurations:

```properties
#-----------------------------------------------------------
#Datalog: module-specific properties
#-----------------------------------------------------------
# DB-Connection
application.datalog.connUrl = datalog
# The executable DLV
application.datalog.DLVPathAsCommand = C:\\Users\\Public\\dlvtest\\dlv.mingw
application.datalog.cautiousOptionForDlv = -cautious

# A directory where temporary files are stored for evaluation
application.datalog.workDirForDlv = C:\\Users\\Public\\dlvtest\\
application.datalog.tempFolder = C:\\Users\\Public\\dlvtest\\

# A suffix that gets added to facts when submissions are "submitted" (security measure against cheating)
application.datalog.factEncodingSuffix = 0

# Path-parameter corresponding to the platform
application.datalog.factUrlForLink = /datalog-facts/
```
#### XQuery

```properties
#-----------------------------------------------------------
#XQuery module-specific properties
#-----------------------------------------------------------
# Names of tables
application.xquery.table.error_categories = error_categories
application.xquery.table.error_grading = error_gradings
application.xquery.table.urls = exercise_urls
application.xquery.table.sortings = sortings
application.xquery.table.exercise = exercise
application.xquery.table.taskGroup_fileIds_mapping = taskGroup_fileIds_mapping

application.xquery.modus_debug = false

# Connection details
application.xquery.connUrl = xquery
application.xquery.connPwd = etutor
application.xquery.connUser = etutor

# Directory, where XML-Files are be stored
application.xquery.questionFolderBaseName = C:\\xq_questions
application.xquery.xmlFileURLPrefix = http://etutor.dke.uni-linz.ac.at/etutor/XML?id=

# Reference to other configurations
application.xquery.tempFolderPath = /xquery/temp
application.xquery.propertyFilePath = /xquery/xquery.properties
application.xquery.xslRenderXqPath = /xquery/xml/render-xquery.xsl
application.xquery.xslModifyPath = /xquery/xml/xml-xmlDiff.xsl
application.xquery.xercesJarPath = /xquery/reflect/xerces.jar
application.xquery.DDbeJarPath = /xquery/reflect/DDbE.jar
application.xquery.xsdFileScoresPath = /xquery/xml/xquery-score.xsd

# Path-parameter corersponding to etutor++
application.xquery.xmlUrlForLink = /XML
```

#### Process Mining
```properties
#-----------------------------------------------------------
#ProcessMining: module-specific properties
#-----------------------------------------------------------
# Connection details
application.processMining.connPwd=etutor
application.processMining.connUser=etutor
application.processMining.connUrl=pm
```


# 2. Building
Simply build the project with Maven:

```
mvn clean install -DskipTests
```

# 3. Deployment
Current deployment strategy involves transferring the executable jar to the production server and executing it with a JVM.
Contact kschuetz@dke.uni-linz.ac.at for further details.

## Docker
It is also possible to deploy the application as a Docker image/container.
To build an image from the application, use the [dockerfile](./Dockerfile).

If you want to spin-up the application together with the database as Docker containers in a network, use the [docker-compose file](./docker-compose.yaml).
This copies the jar of the application, so be sure to build the application beforehand.

# 4. Integrating a module
If you are planning on creating a module for the etutor++ system and integrating it into the dispatcher, the basic requirement is to implement the [Evaluator-interface](./src/main/java/at/jku/dke/etutor/core/evaluation/Evaluator.java).
Then, you can let the [module-serivce](./src/main/java/at/jku/dke/etutor/grading/service/ModuleService.java) return an instance of the implemented evaluator, according to the task-type-parameter of a submission that is sent to this application for evaluation and grading.

Further notes:
- use Maven for dependency management
- For managing exercises, create a dedicated controller class for the module
- Use a connection pool for database connections
- Externalize configuration properties in the respective [file](./src/main/resources/application.properties)
- If the module requires a database, provide an SQL-script with the necessary statements
- Avoid coupling with other modules









