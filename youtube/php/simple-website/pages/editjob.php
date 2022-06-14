<html lang="en">
  <head>
      <?php include "inc/head.php"; ?>
  </head>
  <header>
      <?php include "inc/header.php"; ?>
  </header>
    <body>
    <div class="container">
        <h2 class="page-header">Edit Job Listing</h2>
        <form method="post" action="edit.php?id=<?php echo $job->id; ?>">
            <div class="form-group">
                <label for="company">Company</label>
                <input type="text" class="form-control" name="company" value="<?php echo $job->company ;?>">
            </div>
            <div>
                <label for="category">Category</label>
                <select name="category" class="form-control">
                    <option value="0">Choose Category</option>
                    <?php foreach ($categories as $category): ?>
                        <?php if($job->category_id == $category->id): ?>
                            <option value="<?php echo $category->id; ?>" selected><?php echo $category->name; ?></option>
                        <?php else: ?>
                            <option value="<?php echo $category->id; ?>"><?php echo $category->name; ?></option>
                        <?php endif ?>
                    <?php endforeach; ?>
                </select>
                <br/>
            </div>
            <div class="form-group">
                <label for="job_title">Job Title</label>
                <input type="text" class="form-control" name="job_title" value="<?php echo $job->job_title ;?>"/>
            </div>
            <div class="form-group">
                <label for="description">Description</label>
                <textarea maxlength="2000" class="form-control" name="description"><?php echo $job->description ;?></textarea>
            </div>
            <div class="form-group">
                <label for="salary">Salary</label>
                <input type="text" class="form-control" name="salary" value="<?php echo $job->salary ;?>">
            </div>
            <div class="form-group">
                <label for="location">Location</label>
                <input type="text" class="form-control" name="location" value="<?php echo $job->location ;?>">
            </div>
            <div class="form-group">
                <label for="contact_user">Contact User</label>
                <input type="text" class="form-control" name="contact_user" value="<?php echo $job->contact_user ;?>">
            </div>
            <div class="form-group">
                <label for="contact_email">Contact Email</label>
                <input type="text" class="form-control" name="contact_email" value="<?php echo $job->contact_email ;?>">
            </div>
            <div class="form-group">
                <label for="post_date">Post Date</label>
                <input type="text" class="form-control" name="post_date" value="<?php echo $job->post_date ;?>">
            </div>
            <input type="submit" class="btn btn-default" value="Submit" name="submit"/>
        </form>
        <br/>

    </div>
    </body>
    </html>
<?php include "inc/footer.php"; ?>