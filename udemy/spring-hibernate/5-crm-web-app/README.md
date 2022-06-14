## Manage Credentials

1. rename `credential-example.properties` to `credential.properties`

2. add username and password in `credentials.properties`

## Run

1. Set up tomcat server in intellij (see Spring MVC for versions and specifications) 

2. Go to http://localhost:8080/TestDbServlet to test for Db connection

3. Go to http://localhost:8080/customer/list to test application

## Errors:

Set up the `dispatcher-servlet.xml` file properly given in the tutorial. Make sure all the red squiggly are resolved then it should run properly.

 1. For Step 1 (ComboPooledDataSource), add the library into pom.xml, [link here](https://stackoverflow.com/questions/27957078/how-to-use-c3p0-0-9-2-1-with-hibernate-3-6-10)
 
 2. For Step 2 - 3 (orm), add hibernate 5 orm library into pom.xml 
 
    - [Spring Framework ORM](https://mvnrepository.com/artifact/org.springframework/spring-orm)
 
    - [Add Spring Framrwork Tx also](https://mvnrepository.com/artifact/org.springframework/spring-tx)
 
    - [Make sure all the versions of spring are in sync if not this will happen](https://stackoverflow.com/questions/47558017/error-starting-a-spring-application-initialization-of-bean-failed-nested-excep)
    
 3. Getting values from .properties file only seems to be working by following this [link](https://stackoverflow.com/questions/1771166/access-properties-file-programmatically-with-spring)
 
 4. [aspectj version: 1.7.4 as seen in this link](https://www.journaldev.com/2583/spring-aop-example-tutorial-aspect-advice-pointcut-joinpoint-annotations)