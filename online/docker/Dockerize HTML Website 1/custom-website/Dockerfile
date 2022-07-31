# Use the official image as a parent image.
FROM centos:latest

# Declare the maintainer of the Dockerfile
MAINTAINER Prasanjit-Singh www.binpipe.org

#Install Apache Webserver
RUN yum -y install httpd

# Set the working directory to webroot
WORKDIR /var/www/html

# Copy the code to the webroot directory 
COPY html /var/www/html

# Run the command to launch Apache Webserver daemon
CMD ["/usr/sbin/httpd", "-D", "FOREGROUND"]

# Expose port 80  for the website 
EXPOSE 80
