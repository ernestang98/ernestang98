<!DOCTYPE html>
<html lang="en">
    <head>
        <title></title>
    </head>
    <body>
        <h1>Java Spring MVC Tutorial</h1>
        <ul>
            <li><a href="/">/ or /index.html</a> Automatically rendering index.html in root directory</li>
            <li><a href="/hello">/hello (root -> hello)</a> Routing from root to hello</li>
            <li><a href="/blahblah">error 404</a> (Rendering error 404 for any invalid paths)</li>

            <li><a href="/home">/home</a> (Demo controller - test rendering JSP)</li>
            <li><a href="/test">/test</a> (Demo controller - test rendering JSP)</li>
            <li><a href="/text">/text</a> (Demo controller - test rendering text)</li>
            <li><a href="/welcomeI">/welcomeI with injection</a> (Demo controller - test rendering with no injections)</li>
            <li><a href="/welcomeNoI">/welcomeNoI with no injection</a> (Demo controller - test rendering with injections)</li>

            <li><a href="/form">/form</a> (Form controller)</li>
            <li><a href="/submission">/submission</a> (Form controller)</li>

            <li><a href="/api/form">/api/form</a> (Map API controller)</li>
            <li><a href="/api/submission">/api/submission </a> (Map API controller)</li>

            <li><a href="/student/form3">/student/form3</a> Student controller (form tags & data models)</li>
            <li><a href="/student/submission3">/student/submission3</a> Student controller (form tags & data models)</li>

            <li><a href="/validation/form4">/validation/form4</a> Student Validation controller (form tags & data models)</li>
            <li><a href="/validation/submission4">/validation/submission4</a> Student Validation controller (form tags & data models)</li>
        </ul>
    </body>
</html>