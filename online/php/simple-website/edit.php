<?php include_once 'config/init.php'; ?>
<?php
$job = new Job;

$job_id = isset($_GET['id']) ? $_GET['id'] : null;

if (isset($_POST['submit'])) {
    $data = array();
    $data['job_title'] = $_POST['job_title'];
    $data['company'] = $_POST['company'];
    $data['category_id'] = $_POST['category'];
    $data['description'] = $_POST['description'];
    $data['location'] = $_POST['location'];
    $data['salary'] = $_POST['salary'];
    $data['contact_user'] = $_POST['contact_user'];
    $data['contact_email'] = $_POST['contact_email'];
    $data['post_date'] = $_POST['post_date'];

    if ($job->updateJob($job_id, $data)) {
        redirect('index.php', 'Your job listing has been edited!', 'success');
    }
    else {
        redirect('index.php', 'Something went wrong :(', 'failure');
    }

}

$template = new Template('/pages/editjob.php');
$template->job = $job->getJob($job_id);
$template->categories = $job->getAllCategories();

echo $template;
