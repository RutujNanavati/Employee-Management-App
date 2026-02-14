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

    // ================= LIST =================
    @GetMapping
    public String showEmployees(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sort,
            @RequestParam(defaultValue = "") String keyword,
            Model model) {

        List<Map<String, Object>> employeeList = new ArrayList<>();
        int totalRecords = 0;
        int offset = (page - 1) * size;

        String orderBy = "id ASC";   // default = latest

        if (sort.equals("firstName")) orderBy = "firstName ASC";
        if (sort.equals("username")) orderBy = "username ASC";
        if (sort.equals("latest")) orderBy = "id DESC";

        try (Connection con = getConnection()) {

            // Count total records
            PreparedStatement countPs = con.prepareStatement(
                    "SELECT COUNT(*) FROM employees WHERE firstName LIKE ? OR username LIKE ?");
            countPs.setString(1, "%" + keyword + "%");
            countPs.setString(2, "%" + keyword + "%");
            ResultSet countRs = countPs.executeQuery();

            if (countRs.next()) {
                totalRecords = countRs.getInt(1);
            }

            // Fetch paginated data
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM employees WHERE firstName LIKE ? OR username LIKE ? " +
                            "ORDER BY " + orderBy + " LIMIT ? OFFSET ?");
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ps.setInt(3, size);
            ps.setInt(4, offset);

            ResultSet rs = ps.executeQuery();

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

        int totalPages = (int) Math.ceil((double) totalRecords / size);

        model.addAttribute("employees", employeeList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("size", size);
        model.addAttribute("sort", sort);
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
        model.addAttribute("selectedCountry", "");
        model.addAttribute("selectedState", "");
        model.addAttribute("selectedCity", "");
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

                model.addAttribute("selectedCountry", rs.getInt("country_id"));
                model.addAttribute("selectedState", rs.getInt("state_id"));
                model.addAttribute("selectedCity", rs.getInt("city_id"));
            }

            model.addAttribute("mode", "edit");

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
            @RequestParam String contactNo,
            @RequestParam Integer countryId,
            @RequestParam Integer stateId,
            @RequestParam Integer cityId) {

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO employees(firstName,lastName,username,address,contactNo,country_id,state_id,city_id) VALUES(?,?,?,?,?,?,?,?)")) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, username);
            ps.setString(4, address);
            ps.setString(5, contactNo);
            ps.setInt(6, countryId);
            ps.setInt(7, stateId);
            ps.setInt(8, cityId);

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
            @RequestParam String contactNo,
            @RequestParam Integer countryId,
            @RequestParam Integer stateId,
            @RequestParam Integer cityId) {

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE employees SET firstName=?, lastName=?, username=?, address=?, contactNo=?, country_id=?, state_id=?, city_id=? WHERE id=?")) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, username);
            ps.setString(4, address);
            ps.setString(5, contactNo);
            ps.setInt(6, countryId);
            ps.setInt(7, stateId);
            ps.setInt(8, cityId);
            ps.setInt(9, id);

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
