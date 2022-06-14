<?php include_once 'config/init.php'; ?>
<?php
$job = new Job;

if (isset($_POST['del_id'])) {
    $del_id = $_POST['del_id'];
    if ($job->deleteJob($del_id)) {
        redirect('index.php', 'Your job listing has been deleted', 'success');
    } else {
        redirect('index.php', 'Something went wrong :(', 'failure');
    }
}