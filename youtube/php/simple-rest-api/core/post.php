<?php

//IMPORTANT to trigger all errors
error_reporting(E_ALL);
ini_set('display_errors', 1);

class Post {
    private $conn;
    private $table = 'posts';

    public $id;
    public $category_id;
    public $category_name;
    public $title;
    public $body;
    public $author;
    public $dateCreated;

    public function __construct($db) {
        $this->conn = $db;
    }

    public function read() {
        $query = "SELECT c.name as category_name,
        p.id,
        p.category_id,
        p.title,
        p.body,
        p.author,
        p.dateCreated
        FROM " . $this->table . " as p 
        left join categories as c on p.category_id = c.id
        order by p.dateCreated DESC
        ";

        $stmt = $this->conn->prepare($query);
        $stmt->execute();
        return $stmt;

    }

    public function readSingle() {
        $query = "SELECT c.name as category_name,
        p.id,
        p.category_id,
        p.title,
        p.body,
        p.author,
        p.dateCreated
        FROM " . $this->table . " as p 
        left join categories as c on p.category_id = c.id
        WHERE p.id = ? LIMIT 1
        ";
        $stmt = $this->conn->prepare($query);
        $stmt->bindParam(1, $this->id);
        $stmt->execute();
        $row = $stmt->fetch(PDO::FETCH_ASSOC);
        $this->title = $row['title'];
        $this->body = $row['body'];
        $this->author = $row['author'];
        $this->category_id = $row['category_id'];
        $this->category_name = $row['category_name'];
    }

    public function create() {
        $query = "INSERT INTO " . $this->table . " (category_id, title, body, 
                                author, dateCreated)
                                
                                VALUES (:category_id, :title, :body, 
                                :author, :dateCreated)
                                
                                ";
        $stmt = $this->conn->prepare($query);
        $this->title = htmlspecialchars(strip_tags($this->title));
        $this->body = htmlspecialchars(strip_tags($this->body));
        $this->author = htmlspecialchars(strip_tags($this->author));
        $this->category_id = htmlspecialchars(strip_tags($this->category_id));
        $stmt->bindParam(':title', $this->title);
        $stmt->bindParam(':body', $this->body);
        $stmt->bindParam(':author', $this->author);
        $stmt->bindParam(':category_id', $this->category_id);
        $stmt->bindParam(':dateCreated', $this->dateCreated);
        if ($stmt->execute()) {
            return true;
        }
        else {
            printf("Error %s. \n", $stmt->error);
            return false;
        }
    }

    public function update() {
        $query = "UPDATE " . $this->table . " SET title = :title, body = :body, author = :author, 
        category_id = :category_id, dateCreated = :dateCreated
        where id = :id;";
        $stmt = $this->conn->prepare($query);
        $this->id = htmlspecialchars(strip_tags($this->id));
        $this->title = htmlspecialchars(strip_tags($this->title));
        $this->body = htmlspecialchars(strip_tags($this->body));
        $this->author = htmlspecialchars(strip_tags($this->author));
        $this->category_id = htmlspecialchars(strip_tags($this->category_id));
        $stmt->bindParam(':title', $this->title);
        $stmt->bindParam(':body', $this->body);
        $stmt->bindParam(':author', $this->author);
        $stmt->bindParam(':category_id', $this->category_id);
        $stmt->bindParam(':id', $this->id);
        $stmt->bindParam(':dateCreated', $this->dateCreated);
        if ($stmt->execute()) {
            return true;
        }
        else {
            printf("Error %s. \n", $stmt->error);
            return false;
        }
    }

    public function delete() {
        $query = "DELETE FROM " . $this->table . " where id = :id;";
        $stmt = $this->conn->prepare($query);
        $this->id = htmlspecialchars(strip_tags($this->id));
        $stmt->bindParam(':id', $this->id);
        if ($stmt->execute()) {
            return true;
        }
        else {
            printf("Error %s. \n", $stmt->error);
            return false;
        }
    }
}
