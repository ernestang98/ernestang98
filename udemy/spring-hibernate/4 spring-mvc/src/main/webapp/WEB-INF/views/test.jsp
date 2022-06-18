<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.time.LocalDateTime" %>
<html lang="en">
    <head>
        <title>Simple JSP Application</title>
    </head>
    <body>
        <h1>Test Page!</h1>
        <h2>Current time is <%= LocalDateTime.now() %></h2>
    </body>
</html>