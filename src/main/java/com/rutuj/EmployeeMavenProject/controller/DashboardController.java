package com.rutuj.EmployeeMavenProject.controller;

import java.sql.*;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class DashboardController {

    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/employeedb",
                "root",
                "@Rutuj2005"
        );
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {

        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }

        int totalEmployees = 0;

        try (Connection con = getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM employees")) {

            if (rs.next()) {
                totalEmployees = rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("username", session.getAttribute("user"));
        model.addAttribute("totalEmployees", totalEmployees);

        return "dashboard";
    }
}
	