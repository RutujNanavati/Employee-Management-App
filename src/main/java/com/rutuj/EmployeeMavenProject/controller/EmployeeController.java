package com.rutuj.EmployeeMavenProject.controller;

import java.sql.*;
import java.util.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

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
            @RequestParam(defaultValue = "") String gender,
            Model model) {

        List<Map<String, Object>> employeeList = new ArrayList<>();
        int totalRecords = 0;
        int offset = (page - 1) * size;

        String orderBy = "id ASC";

        if (sort.equals("firstName")) orderBy = "firstName ASC";
        if (sort.equals("username")) orderBy = "username ASC";
        if (sort.equals("latest")) orderBy = "id DESC";

        try (Connection con = getConnection()) {

            // 🔥 MULTI-WORD SEARCH SUPPORT
            String[] words = keyword.trim().isEmpty()
                    ? new String[0]
                    : keyword.trim().split("\\s+");

            StringBuilder where = new StringBuilder(" WHERE 1=1 ");
            List<String> params = new ArrayList<>();

            for (String word : words) {
                where.append(" AND (firstName LIKE ? OR lastName LIKE ? OR username LIKE ?) ");
                params.add("%" + word + "%");
                params.add("%" + word + "%");
                params.add("%" + word + "%");
            }

            if (!gender.isEmpty()) {
                where.append(" AND gender = ? ");
            }

            // ================= COUNT QUERY =================
            String countSql = "SELECT COUNT(*) FROM employees " + where;

            PreparedStatement countPs = con.prepareStatement(countSql);

            int index = 1;
            for (String param : params) {
                countPs.setString(index++, param);
            }

            if (!gender.isEmpty()) {
                countPs.setString(index++, gender);
            }

            ResultSet countRs = countPs.executeQuery();
            if (countRs.next()) {
                totalRecords = countRs.getInt(1);
            }

            // ================= SELECT QUERY =================
            String sql = "SELECT * FROM employees " + where +
                    " ORDER BY " + orderBy + " LIMIT ? OFFSET ?";

            PreparedStatement ps = con.prepareStatement(sql);

            index = 1;
            for (String param : params) {
                ps.setString(index++, param);
            }

            if (!gender.isEmpty()) {
                ps.setString(index++, gender);
            }

            ps.setInt(index++, size);
            ps.setInt(index, offset);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Map<String, Object> emp = new HashMap<>();
                emp.put("id", rs.getInt("id"));
                emp.put("firstName", rs.getString("firstName"));
                emp.put("lastName", rs.getString("lastName"));
                emp.put("username", rs.getString("username"));
                emp.put("gender", rs.getString("gender"));
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
        model.addAttribute("selectedGender", gender);

        return "employees";
    }




    // ================= ADD PAGE =================
    @GetMapping("/add")
    public String addPage(Model model) {

        model.addAttribute("id", 0);
        model.addAttribute("firstName", "");
        model.addAttribute("lastName", "");
        model.addAttribute("username", "");
        model.addAttribute("gender", "");
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
    public String editPage(@PathVariable int id, Model model, HttpSession session) {
    	
        String role = (String) session.getAttribute("role");
        if (role == null || 
        	       (!"ADMIN".equals(role) && !"HR".equals(role))) {
        	        return "redirect:/accessDenied";
        	    }

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
                model.addAttribute("gender", rs.getString("gender"));
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
            @RequestParam String gender,
            @RequestParam String address,
            @RequestParam String contactNo,
            @RequestParam Integer countryId,
            @RequestParam Integer stateId,
            @RequestParam Integer cityId) {

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
"INSERT INTO employees(firstName,lastName,username,gender,address,contactNo,country_id,state_id,city_id) VALUES(?,?,?,?,?,?,?,?,?)")) {

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, username);
            ps.setString(4, gender);
            ps.setString(5, address);
            ps.setString(6, contactNo);
            ps.setInt(7, countryId);
            ps.setInt(8, stateId);
            ps.setInt(9, cityId);

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
            @RequestParam String gender,
            @RequestParam String address,
            @RequestParam String contactNo,
            @RequestParam Integer countryId,
            @RequestParam Integer stateId,
            @RequestParam Integer cityId) {

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "UPDATE employees SET firstName=?, lastName=?, username=?, gender=?, address=?, contactNo=?, country_id=?, state_id=?, city_id=? WHERE id=?")) {

            ps.setString(1, firstName);
            ps.setString(2, lastName); 
            ps.setString(3, username);
            ps.setString(4, gender);
            ps.setString(5, address);
            ps.setString(6, contactNo);
            ps.setInt(7, countryId);
            ps.setInt(8, stateId);
            ps.setInt(9, cityId);
            ps.setInt(10, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/employees";
    }

    // ================= DELETE =================
    @GetMapping("/delete/{id}")
    public String deleteEmployee(@PathVariable int id,HttpSession session) {
    	
        String role = (String) session.getAttribute("role");
        
        if (!"ADMIN".equals(role)) {
            return "redirect:/accessDenied";
        }


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
