<!DOCTYPE HTML>
<html lang="en">
    <head>
        <title>You first Spring application</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    </head>
    <body>
    <h1>${someAttribute}</h1>
    <h2><a href="/home">Home Directory</a></h2>
    <p><a href="/hello">Greet the world!</a></p>

    <form action="/hello" method="GET" id="nameForm">
        <div>
            <label for="nameField">How should the app call you?</label>
            <input name="myName" id="nameField">
            <button>Greet me!</button>
        </div>
    </form>
    </body>
</html>