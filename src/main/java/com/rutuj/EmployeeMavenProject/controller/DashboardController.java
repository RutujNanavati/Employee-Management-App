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
    public String showDashboard(Model model) {

        int total = 0;
        int male = 0;
        int female = 0;
        int other = 0;

        try (Connection con = getConnection()) {

            // Total employees
            PreparedStatement ps1 = con.prepareStatement(
                    "SELECT COUNT(*) FROM employees");
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) {
                total = rs1.getInt(1);
            }

            // Gender count
            PreparedStatement ps2 = con.prepareStatement(
                    "SELECT gender, COUNT(*) as count FROM employees GROUP BY gender");
            ResultSet rs2 = ps2.executeQuery();

            while (rs2.next()) {
                String g = rs2.getString("gender");
                int count = rs2.getInt("count");

                if ("Male".equalsIgnoreCase(g)) male = count;
                if ("Female".equalsIgnoreCase(g)) female = count;
                if ("Other".equalsIgnoreCase(g)) other = count;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("total", total);
        model.addAttribute("male", male);
        model.addAttribute("female", female);
        model.addAttribute("other", other);

        return "dashboard";
    }
}
	