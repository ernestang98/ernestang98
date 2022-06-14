package com.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.*;
import java.util.Properties;

/**
 * Servlet implementation class TestDbServlet
 */
@WebServlet("/TestDbServlet")
public class DatabaseConnection extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private Properties properties;

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String jdbcUrl = "jdbc:mysql://localhost:3306/web_customer_tracker?useSSL=false&serverTimezone=UTC";
        String driver = "com.mysql.cj.jdbc.Driver";

        // get connection to database
        try {

            Resource resource = new ClassPathResource("/credential.properties");

            this.properties = PropertiesLoaderUtils.loadProperties(resource);

            String user = this.properties.getProperty("username");

            String pass = this.properties.getProperty("password");

            if (user == null) throw new NullPointerException("SOMETHING WENT WRONG");

            PrintWriter out = response.getWriter();

            out.println("Connecting to database: " + jdbcUrl);

            Class.forName(driver);

            Connection myConn = DriverManager.getConnection(jdbcUrl, user, pass);

            out.println("SUCCESS!!!");

            myConn.close();

        }
        catch (Exception exc) {
            exc.printStackTrace();
            throw new ServletException(exc);
        }


    }

}







