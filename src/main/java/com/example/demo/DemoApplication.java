package com.example.demo;

import com.example.demo.Model.Adult;
import com.example.demo.Model.Child;
import com.example.demo.Model.Family;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
@EnableWebMvc
public class DemoApplication {
	public static void main(String[] args){

		SpringApplication.run(DemoApplication.class, args);

	}

}


