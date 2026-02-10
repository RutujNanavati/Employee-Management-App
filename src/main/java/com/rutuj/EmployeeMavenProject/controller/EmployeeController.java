package com.rutuj.EmployeeMavenProject.controller;

import java.sql.*;
import java.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/employeedb",
                "root",
                "@Rutuj2005"
        );
    }
    
    @GetMapping("/add")
    public String addEmployeePage(Model model) {

        model.addAttribute("id", 0);
        model.addAttribute("firstName", "");
        model.addAttribute("lastName", "");
        model.addAttribute("username", "");
        model.addAttribute("address", "");
        model.addAttribute("contactNo", "");

        model.addAttribute("mode", "add");

        return "editEmployee";
    }


    // 🔹 LIST ALL EMPLOYEES
    @GetMapping
    public String showEmployees(Model model) {

        List<Map<String, Object>> employeeList = new ArrayList<>();

        try (Connection con = getConnection();
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM employees")) {

            while (rs.next()) {
                Map<String, Object> emp = new HashMap<>();
                emp.put("id", rs.getInt("id"));
                emp.put("firstName", rs.getString("firstName"));
                emp.put("lastName", rs.getString("lastName"));
                emp.put("username", rs.getString("username"));
                emp.put("address", rs.getString("address"));
                emp.put("contactNo", rs.getString("contactNo"));
                employeeList.add(emp);
                
                model.addAttribute("mode", "add");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("employees", employeeList);
        return "employees";
    }

    // 🔹 DELETE EMPLOYEE
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable int id) {

        try (Connection con = getConnection();
             PreparedStatement ps =
                     con.prepareStatement("DELETE FROM employees WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();	

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/employees";
    }

    // 🔹 LOAD EDIT PAGE
    @GetMapping("/edit/{id}")
    public String editEmployee(@PathVariable int id, Model model) {

        try (Connection con = getConnection();
             PreparedStatement ps =
                     con.prepareStatement("SELECT * FROM employees WHERE id=?")) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                model.addAttribute("id", rs.getInt("id"));
                model.addAttribute("firstName", rs.getString("firstName"));
                model.addAttribute("lastName", rs.getString("lastName"));
                model.addAttribute("username", rs.getString("username"));
                model.addAttribute("address", rs.getString("address"));
                model.addAttribute("contactNo", rs.getString("contactNo"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "editEmployee";
    }
    
    @PostMapping("/save")
    public String saveEmployee(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String username,
            @RequestParam String address,
            @RequestParam String contactNo) {

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO employees(firstName,lastName,username,address,contactNo) VALUES(?,?,?,?,?)")) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, username);
            ps.setString(4, address);
            ps.setString(5, contactNo);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/employees";
    }


    // 🔹 UPDATE EMPLOYEE
    @PostMapping("/update")
    public String updateEmployee(
            @RequestParam int id,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String username,
            @RequestParam String address,
            @RequestParam String contactNo) {

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE employees SET firstName=?, lastName=?, username=?, address=?, contactNo=? WHERE id=?")) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, username);
            ps.setString(4, address);
            ps.setString(5, contactNo);
            ps.setInt(6, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/employees";
    }
}
