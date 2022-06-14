## Configuring Environment Variables

- Create `.env` file (or you can reference env.txt)

- Add dbname, user, and pass of MySQL server to the `.env` file since we are using `System.getenv()` seen [here](https://stackoverflow.com/questions/8349717/how-can-i-configure-hibernate-with-environment-variable/57701227)

- Use IntelliJ envfile plugin to compile and run code with .env file, when doing so, ensure you are running and compiling code with IntelliJ

- For hibernate config file, use systemProperties [here](https://stackoverflow.com/questions/31082079/environment-specific-configuration-of-springhibernate-application)

- For our case, remove the `-SAMPLE` at the end of the hibernate config file and rename the credentials to your MySQL credentials

## Other stuff

- SQL Table Things

    ```
    ALTER TABLE schema.table AUTO_INCREMENT=1
    TRUNCATE schema.table
    ```

- Details for every type of mapping except m-to-m  

    - dbname is spring
  
    - StudentController controls Student
    
    - InstructorController controls Instructor
    
    - InstructorDetailController controls InstructorDetail
    
    - InstructorWithInstructorDetailWithCourse controls Instructor_1, InstructorDetail_1, Course
    
    - InstructorEagerController controls Instructor_1_Eager_Loading, InstructorDetail_1_Eager_Loading, Course_Eager_Loading
    
    - CourseReviewController controls Instructor_1_Eager_Loading, InstructorDetail_1_Eager_Loading, Course_Eager_Loading, Review
    
- Details for m-to-m mapping only

    - dbname is spring-m-2-m

    - All M_TO_M_* models and MToMController things
    
- To see schema mappings in MySQL, use the `reverse engineer` functions