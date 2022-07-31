<?php

//IMPORTANT to trigger all errors
error_reporting(E_ALL);
ini_set('display_errors', 1);

class Category {
    private $conn;
    private $table = 'categories';

    public $id;
    public $name;

    public function __construct($db) {
        $this->conn = $db;
    }

    public function read() {
        $query = "SELECT * 
        FROM " . $this->table;

        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt;

    }



}