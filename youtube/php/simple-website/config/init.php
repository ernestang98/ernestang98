<?php

session_start();

// Show PHP errors
ini_set('display_errors',1);
ini_set('display_startup_erros',1);
error_reporting(E_ALL);

require_once 'config.php';

require_once 'services/System.php';

function __autoload($class_name) {
    require_once 'services/'.$class_name.'.php';
}

// echo nl2br("Hello World\n");