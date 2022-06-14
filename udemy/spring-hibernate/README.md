## [Spring & Hibernate for Beginners (Includes Spring Boot)](https://www.udemy.com/course/spring-hibernate-tutorial/)

### Topics covered:

- Spring Framework Basics

  - Spring Bean

  - IoC Container

- Spring MVC

- Hibernate

- AOP (Aspectj)

- Spring Security

- Spring REST

- Spring Boot

- Thymeleaf

- JPA

### Set up:

- [Intellij MVC Starter with Maven Tutorial 1](https://medium.com/@yuntianhe/create-a-web-project-with-maven-spring-mvc-b859503f74d7)

- [Intellij MVC Starter with Maven Tutorial 2](https://medium.com/panchalprogrammingacademy/create-a-spring-mvc-project-with-maven-and-intellij-idea-community-edition-1d31b3efe078)

- [Intellij MVC Starter with Maven Tutorial 3 (Didn't try this)](https://javapointers.com/spring/spring-mvc/create-spring-mvc-project-using-maven/)

- [Apache Tomcat Intellij Tutorial 1](https://iamsaurabh.wordpress.com/2017/02/11/configure-a-spring-mvc-project-with-intellij-idea/)

- [Apache Tomcat Intellij Tutorial 2](https://www.programmersought.com/article/9379189381/)

- When setting up Tomcat configuration in IntelliJ, if you cannot build artifact/don't see any artefact, go to Project Settings -> Artifacts -> Click on + -> Web Application Exploded -> From Modules -> Select your project.iml file

- Apache Tomcat Application Context: When configuring server under "Edit Configuration", set Application Context to "/"

- [Need to add Java EE 6 Jars to Module dependencies for Intellij](https://intellij-support.jetbrains.com/hc/en-us/community/posts/360003369340-Add-Java-EE-6-Jars-to-Module-Dependencies)

### Content Covered:

- Basics of Spring Bean

  1. Spring Project set up on Intellij
  
  2. Using application context XML and without
  
  3. Spring Library JAR files

- Basics of Spring MVC

  1. IMPORTANT: strictly follow versions of dependencies and technologies used
  
  2. Apache Tomcat 9.0.41, Java 1.8, Language Level 1
  
  3. Spring Project set up on Intellij using Maven - pom.xml
  
  4. Java Static Pages (JSPs) and serving them via dispatcher-servlet.xml & web.xml
  
  5. Bean Validation via Javax Validation, Hibernate Validator
  
  6. Under edit configurations, deployment, add artefact war exploded
  
  7. Under `main` directory, add `java` for controller and `resources` for properties
  
  8. [Application Contexts for Spring MVC](https://www.baeldung.com/spring-web-contexts)
  
- Hibernate

  1. Versioning are important as well, I used Hibernate 5.2.18 & Java 1.8 for this tutorial
  
  2. You need JDBC driver as well!
  
  3. Language level 7
  
  4. https://stackoverflow.com/questions/8621906/is-the-buildsessionfactory-configuration-method-deprecated-in-hibernate
  
  5. https://stackoverflow.com/questions/19319712/hibernate-classnotfoundexception-com-mysql-jdbc-driver
  
  6. https://stackoverflow.com/questions/46280859/intellij-idea-error-java-invalid-source-release-1-9
  
  7. `mappedBy=`  is based on the property name itself, if not configured properly then following error is observed
  
     ```
     Exception in thread "main" org.hibernate.AnnotationException: mappedBy reference an unknown target entity property: com.company.models.Course.instructor123 in com.company.models.Instructor_1.courses
     ```
  
  8. `.addAnnotatedClass(XXXXXX.class)`  if no .class then following error is observed
  
     ```
     Exception in thread "main" org.hibernate.AnnotationException: Use of @OneToMany or @ManyToMany targeting an unmapped class: com.company.models.Instructor_1.courses[com.company.models.Course]
     ```
  
  9. More of such annotation exceptions can be debugged by analyzing the logs and the structure of model classes
  
- AOP (Aspectj)

  - [aspectj weaver 1.9.7](https://mvnrepository.com/artifact/org.aspectj/aspectjweaver/1.9.7)
  - [Spring Framework 5.3.9](https://repo.spring.io/ui/native/release/org/springframework/spring/)
  - Pick the releases with lots of downloads

- Intellij Application Configuration

  1. If don't have configuration, click edit -> add configuration -> application 
     - Working directory should be: `/path/to/src`
     - Main class should be the name of your class with `main(String[] args)`
     - If cannot then rebuild project, remove .idea folder
     - https://stackoverflow.com/questions/10654120/error-could-not-find-or-load-main-class-in-intellij-ide



