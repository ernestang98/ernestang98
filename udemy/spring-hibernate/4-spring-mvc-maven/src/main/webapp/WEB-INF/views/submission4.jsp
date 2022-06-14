<!DOCTYPE html>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<html lang="en">
    <head>
        <title>Student Confirmation</title>
    </head>
    <body>
        The student is confirmed: ${student.firstName} ${student.lastName}
        <br><br>
        Free passes: ${student.freePasses}
        <br><br>
        Postal Code: ${student.postalCode}
        <br><br>
        Course code: ${student.courseCode}
    </body>
</html>