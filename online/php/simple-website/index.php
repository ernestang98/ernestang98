<?php include_once 'config/init.php'; ?>

<?php
	$job = new Job;
	$template = new Template('/pages/frontpage.php');
	$category = isset($_GET['category']) ? $_GET['category'] : null;
    if ($category) {
        $template->jobs = $job->getJobsByCategory($category);
        $template->title = 'Jobs in ' . $job->getCategory($category)->name;
    } else {
        $template->title = 'Latest Jobs';
        $template->jobs = $job->getAllJobs();
    }

    $template->categories = $job->getAllCategories();
//	 echo $template->title;
	echo $template;


