package com.rutuj.EmployeeMavenProject.controller;

import java.sql.*;
import java.util.*;
import javax.servlet.http.HttpSession;
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
    
    

    // ================= LIST =================
    @GetMapping
    public String showEmployees(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(required = false) String keyword,
            Model model) {

        int pageSize = 5;
        int offset = (page - 1) * pageSize;

        List<Map<String, Object>> employeeList = new ArrayList<>();
        int totalEmployees = 0;

        try (Connection con = getConnection()) {

            String countSql = "SELECT COUNT(*) FROM employees";
            String dataSql = "SELECT * FROM employees LIMIT ? OFFSET ?";

            // 🔎 SEARCH SUPPORT
            if (keyword != null && !keyword.isEmpty()) {
                countSql = "SELECT COUNT(*) FROM employees WHERE firstName LIKE ? OR username LIKE ? OR contactNo LIKE ?";
                dataSql = "SELECT * FROM employees WHERE firstName LIKE ? OR username LIKE ? OR contactNo LIKE ? LIMIT ? OFFSET ?";
            }

            // 🔹 COUNT QUERY
            PreparedStatement countPs = con.prepareStatement(countSql);

            if (keyword != null && !keyword.isEmpty()) {
                String search = "%" + keyword + "%";
                countPs.setString(1, search);
                countPs.setString(2, search);
                countPs.setString(3, search);
            }

            ResultSet countRs = countPs.executeQuery();
            if (countRs.next()) {
                totalEmployees = countRs.getInt(1);
            }

            // 🔹 DATA QUERY
            PreparedStatement dataPs = con.prepareStatement(dataSql);

            if (keyword != null && !keyword.isEmpty()) {
                String search = "%" + keyword + "%";
                dataPs.setString(1, search);
                dataPs.setString(2, search);
                dataPs.setString(3, search);
                dataPs.setInt(4, pageSize);
                dataPs.setInt(5, offset);
            } else {
                dataPs.setInt(1, pageSize);
                dataPs.setInt(2, offset);
            }

            ResultSet rs = dataPs.executeQuery();

            while (rs.next()) {
                Map<String, Object> emp = new HashMap<>();
                emp.put("id", rs.getInt("id"));
                emp.put("firstName", rs.getString("firstName"));
                emp.put("lastName", rs.getString("lastName"));
                emp.put("username", rs.getString("username"));
                emp.put("contactNo", rs.getString("contactNo"));
                employeeList.add(emp);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        int totalPages = (int) Math.ceil((double) totalEmployees / pageSize);

        model.addAttribute("employees", employeeList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("keyword", keyword);

        return "employees";
    }


    // ================= ADD PAGE =================
    @GetMapping("/add")
    public String addPage(Model model) {

        model.addAttribute("id", 0);
        model.addAttribute("firstName", "");
        model.addAttribute("lastName", "");
        model.addAttribute("username", "");
        model.addAttribute("address", "");
        model.addAttribute("contactNo", "");
        model.addAttribute("mode", "add");

        return "editEmployee";
    }

    // ================= EDIT PAGE =================
    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable int id, Model model) {

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

            model.addAttribute("mode", "edit"); // 🔥 MOST IMPORTANT

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "editEmployee";
    }

    // ================= SAVE =================
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

    // ================= UPDATE =================
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

    // ================= DELETE =================
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
}
