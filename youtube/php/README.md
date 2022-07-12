## Note:

For all of the above projects, to run them, you need to set up XAMPP. Once you have set it up (depending on your OS), drag the project folder under the base directory of the XAMPP server (for me, I use a Mac (hence MAMPP) and so the base directory for me it is under `/htdocs`). Base directory is probably configurable, just Google/Stackoverflow. After that, run XAMPP, and simply access the PHP projects via the browser (e.g. http://localhost:8888/simple-rest-api/api/read.php). If needed, use API testing platforms such as postman to test the endpoints with different HTTP methods (e.g. POST, PUT, DELETE).

## XAMPP Set Up:

- PHP/Laravel requires an Apache HTTP server to work

- For MacOSX, there is a webserver set up already, just need to install MySQL, set up PHPMyAdmin, and connect to it

  - [Set up everything #1](https://vinodpandey.com/installing-apache-php-mysql-phpmyadmin-mac-os-x/)
  
  - [Set up everything #2](https://www.youtube.com/watch?v=vHYh-m4iXw4)
  
  - [Set up everything #3](https://www.youtube.com/watch?v=vb0vQYoeWt0)
  
  - [Set up phpmyadmin](https://www.youtube.com/watch?v=SGRdBhwssU8)

- Install XAMP! (in this case its MAMP for me as I use MacOS)

  - [MAMP MySQL and MacOSX MySQL are separate and running on their own socket](https://stackoverflow.com/questions/41954853/linking-mamp-on-mac-to-a-different-instance-of-mysql-and-phpmyadmin)
  
  - [Install MAMP on MacOSX #1](https://www.cloudways.com/blog/connect-mysql-with-php/)
  
  - [Install MAMP on MACOSX #2](https://www.youtube.com/watch?v=rN7JOs34akU&t=75s)
  
  - [MAMP MySQL Authentication Error, need to change config.php or something](https://stackoverflow.com/questions/45111527/phpmyadmin-access-denied-for-user-rootlocalhost-using-password-no)
  
  - [Resetting MAMP MySQL Password #1](https://www.tech-otaku.com/local-server/resetting-mamp-mysql-root-user-password/)
  
  - [Resetting MAMP MySQL Password #2](https://documentation.mamp.info/en/MAMP-Mac/FAQ/How-do-I-change-the-password-of-the-MySQL-root-user/)
- Connecting to MySQL
  
  - [MySQL PHP Connection Link #1](https://www.codegrepper.com/code-examples/php/php+test+mysql+connection)
  
  - [MySQL PHP Connection Link #2](https://www.cloudways.com/blog/connect-mysql-with-php/)

  - [How to add/change PHP version on MAMP](https://www.igorkromin.net/index.php/2017/08/07/how-to-addchange-php-versions-appearing-in-mamp-preferences/)

- Debugging

  - [Forbidden errors on MacOSX Webserver, basically need to allow under config.php or something](https://coolestguidesontheplanet.com/forbidden-403-you-dont-have-permission-to-access-username-on-this-server/)
  
  - [Switching versions of PHP](https://stackoverflow.com/questions/34909101/how-can-i-easily-switch-between-php-versions-on-mac-osx)
  
  - [Logging errors](https://www.cloudways.com/blog/php-error-logging)
  
  - [Run php script from command line, like how node can be used to run javascript](https://stackoverflow.com/questions/954910/how-to-run-a-php-script-from-the-command-line-with-mamp)
