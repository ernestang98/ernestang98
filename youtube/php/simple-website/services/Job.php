<?php

class Job {

    private $db;

    public function __construct() {
        $this->db = new Database;
    }

    public function getAllJobs() {
//        echo "2. Calling getAllJobs()...<br>";
        $this->db->query("SELECT jobs.*, categories.name AS cname FROM jobs 
                                 INNER JOIN categories 
                                 ON jobs.category_id = categories.id");
        $results = $this->db->resultSet();
//        echo $results . "<br>";

        return $results;
    }

    public function getAllCategories() {
        $this->db->query("SELECT * FROM categories");
        $results = $this->db->resultSet();

        return $results;
    }

    public function getJobsByCategory($category) {
        $this->db->query("SELECT jobs.*, categories.name AS cname FROM jobs 
                                 INNER JOIN categories 
                                 ON jobs.category_id = categories.id
                                 WHERE jobs.category_id = $category");
        $results = $this->db->resultSet();

        return $results;
    }

    public function getCategory($category_id) {
        $this->db->query("SELECT * from categories where id = :category_id");
        $this->db->bind(':category_id', $category_id);
        $row = $this->db->single();
        return $row;
    }

    public function getJob($id) {
        $this->db->query("SELECT * from jobs where id = :id");
        $this->db->bind(':id', $id);
        $row = $this->db->single();
        return $row;
    }

    public function createJob($data) {
        $this->db->query("INSERT INTO jobs (category_id, job_title, company, 
                                description, location, salary, contact_user, contact_email,
                                post_date)
                                
                                VALUES (:category_id, :job_title, :company, 
                                :description, :location, :salary, :contact_user, :contact_email,
                                :post_date)
                                
                                ");

        $this->db->bind(':category_id', $data['category_id']);
        $this->db->bind(':job_title', $data['job_title']);
        $this->db->bind(':company', $data['company']);
        $this->db->bind(':description', $data['description']);
        $this->db->bind(':location', $data['location']);
        $this->db->bind(':salary', $data['salary']);
        $this->db->bind(':contact_user', $data['contact_user']);
        $this->db->bind(':contact_email', $data['contact_email']);
        $this->db->bind(':post_date', $data['post_date']);

        if ($this->db->run()) {
            return true;
        } else {
            return false;
        }

    }

    public function deleteJob($id) {
        $this->db->query("DELETE FROM jobs where id = $id");

        if ($this->db->run()) {
            return true;
        } else {
            return false;
        }

    }

    public function updateJob($id, $data) {
        $this->db->query("UPDATE jobs
                                SET
                                category_id = :category_id,
                                job_title = :job_title,
                                company = :company,
                                description = :description,
                                location = :location,
                                salary = :salary,
                                contact_user = :contact_user,
                                contact_email = :contact_email,
                                post_date = :post_date
                                WHERE
                                id = $id");

        $this->db->bind(':category_id', $data['category_id']);
        $this->db->bind(':job_title', $data['job_title']);
        $this->db->bind(':company', $data['company']);
        $this->db->bind(':description', $data['description']);
        $this->db->bind(':location', $data['location']);
        $this->db->bind(':salary', $data['salary']);
        $this->db->bind(':contact_user', $data['contact_user']);
        $this->db->bind(':contact_email', $data['contact_email']);
        $this->db->bind(':post_date', $data['post_date']);

        if ($this->db->run()) {
            return true;
        } else {
            return false;
        }
    }


}