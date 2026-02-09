package com.rutuj.EmployeeMavenProject.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    @GetMapping("/register")
    public String showForm() {
        return "register";
    }
    
    @GetMapping("/success")
    public String showSuccessPage(Model model) {
        model.addAttribute("message", "Employee Successfully Registered!");
        return "success";
    }
    
//    @GetMapping("/employees")
//    public String showemployeesPage(Model model)
//    {
//        model.addAttribute("message", "You are on Employees Page!");
//    	return "employees";
//    }

    @PostMapping("/register")
    public String registerEmployee(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String address,
            @RequestParam String contactNo) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/employeedb",
                    "root",
                    "@Rutuj2005");

            String sql = "INSERT INTO employees(firstName,lastName,username,password,address,contactNo) VALUES(?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, username);
            ps.setString(4, password);
            ps.setString(5, address);
            ps.setString(6, contactNo);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/success";
    }
}
