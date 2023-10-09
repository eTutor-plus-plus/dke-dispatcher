# Tutorial: Implementing an expert module
This tutorial will showcase how one **could** implement an expert module into the dke-dispatcher.
A simple number guessing game will be used as an example, the module for which is called *binary search*.

Each subsection will conclude with a link to the commit that contains the changes made to the source-code of this subsection.

A dedicated branch feature/binarysearch also exists as a reference.

## 0. Module Introduction - *binarysearch*
The *binarysearch* module is quite simple: it should be possible to create, modify and delete a task defined by a number which student's
have to guess.

When a student's submissions is received, the module will compare the input to the solution
and return whether the searched number is *smaller than, equal or greater* than the input.

By implementing this module we want to show which actions have to be taken to integrate any module.

## 1. Setting up the database
We will use the postgresql docker container contained in the [local-deploy](https://github.com/eTutor-plus-plus/local-deploy).
The image of this container is a plain postgresql image and an [initialization-script](https://github.com/eTutor-plus-plus/local-deploy/blob/main/volumes/postgres/init/dump.sql) is mounted inside the container
upon the first start of the container.

### 1.1 Creating a dedicate database user
We will create a dedicated database user with no permissions other than *login* for the module, called *binarysearch*.
For this we add the necessary *CREATE ROLE* statement to the initialization script.
The default password equals the username.

Furthermore, we set the password in the [env-file]() so we can pass it to 
the container of the *dke-dispatcher* inside the [docker-compose file]().

The environment variable should define the value of the property used as a password for connecting to the database
from the *dke-dispatcher*.
Therefore, we create the necessary properties for the database connection in the property file**s** ([1](), [2]()).
For the local setup, we set both user and password as constants.
For the docker container and the production environment, we refer to the environment variable for the password.


###

