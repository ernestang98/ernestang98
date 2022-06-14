<?php
    if ($_SERVER['REQUEST_METHOD'] == 'POST') {
        echo "<body>
                <header>
                  <h1>Survey Form</h1>
                  <p>A confirmation email containing the details of your application will be sent to you!
                  We will get back to you shortly!</p>
                </header>
              </body>";

        $name=$_POST['name'];
        $email=$_POST['email'];
        $age=$_POST['age'];
        $job=$_POST['job'];
        $gender=$_POST['gender'];
        $interest=$_POST['interest'];
        $to=$email;
        $subject='Form Submission';
        $message="Name: ".$name."\n"."Email: ".$email."\n"."Age: ".$age."\n";
        $header="From: ernestang98@gmail.com";

        if (mail($to, $subject, $message, $header)) {
          echo "<p>Successful!</p>";
        }
        else {
          echo "<p>Error!</p>";
        }

    }
    else {
        echo "<h1>BAD ERROR 404!</h1>";
    }
?>
