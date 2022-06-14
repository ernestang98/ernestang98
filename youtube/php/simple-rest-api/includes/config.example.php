<?php

// change file to config.php and add user and password

$db_user = '';
$db_password = '';
$db_name = 'php';
$db = new PDO('mysql:host=localhost;dbname='.$db_name.';charset=utf8', $db_user, $db_password);

$db->setAttribute(PDO::ATTR_EMULATE_PREPARES, false);
$db->setAttribute(PDO::MYSQL_ATTR_USE_BUFFERED_QUERY, true);
$db->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);

define("APP NAME", "phpREST1");
