<?php

//define('DS') ? null : define('DS', DIRECTORY_SEPARATOR);

//$url = $_SERVER['REQUEST_URI'];
//$parts = explode('/',$url);
//$dir = $_SERVER['SERVER_NAME'];
//
//for ($i = 0; $i < count($parts) - 1; $i++) {
//    $dir .= $parts[$i] . "/";
//}
//echo $dir;

//define('SITE_ROOT') ? null : define('SITE_ROOT', DS . 'Application' . DS . "MAMP" . DS . "htdocs" . DS . "phpREST1");
//define('INC_PATH') ?  null : define('INC_PATH', SITE_ROOT.DS.'includes');
//define('CORE_PATH') ?  null : define('CORE_PATH', SITE_ROOT.DS.'core');
//
//require_once(INC_PATH.DS.'config.php');
//
//require_once(INC_PATH.DS.'post.php');

require_once('../includes/config.php');

require_once('post.php');

require_once('category.php');
