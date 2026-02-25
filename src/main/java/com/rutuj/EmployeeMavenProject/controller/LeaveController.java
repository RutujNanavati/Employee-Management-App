package com.rutuj.EmployeeMavenProject.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/leaves")
public class LeaveController {

    // ================= DB CONNECTION =================
    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/employeedb",
                "root",
                "@Rutuj2005"
        );
    }

    // ================= HR / ADMIN VIEW =================
    @GetMapping
    public String showLeaves(Model model, HttpSession session) {

        String role = (String) session.getAttribute("role");
        
        session.setAttribute("role", role);

        if (role == null || 
           (!"ADMIN".equals(role) && !"HR".equals(role))) {
            return "redirect:/accessDenied";
        }

        List<Map<String, Object>> leaveList = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT l.*, e.firstName, e.lastName " +
                     "FROM leave_requests l " +
                     "JOIN employees e ON l.employee_id = e.id " +
                     "ORDER BY l.id DESC")) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Map<String, Object> map = new HashMap<>();

                map.put("id", rs.getInt("id"));
                map.put("employeeid", rs.getInt("employee_id"));
                map.put("employeeName",
                        rs.getString("firstName") + " " +
                        rs.getString("lastName"));
                map.put("fromDate", rs.getDate("from_date"));
                map.put("toDate", rs.getDate("to_date"));
                map.put("reason", rs.getString("reason"));
                map.put("status", rs.getString("status"));

                leaveList.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("leaves", leaveList);

        return "leaveList";  // leaveList.jsp
    }


    // ================= APPROVE =================
    @GetMapping("/approve/{id}")
    public String approveLeave(@PathVariable int id,
                               HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (role == null ||
           (!"ADMIN".equals(role) && !"HR".equals(role))) {
            return "redirect:/accessDenied";
        }

        try (Connection con = getConnection();
             PreparedStatement ps =
                     con.prepareStatement(
                             "UPDATE leave_requests SET status='APPROVED' WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/leaves";
    }


    // ================= REJECT =================
    @GetMapping("/reject/{id}")
    public String rejectLeave(@PathVariable int id,
                              HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (role == null ||
           (!"ADMIN".equals(role) && !"HR".equals(role))) {
            return "redirect:/accessDenied";
        }

        try (Connection con = getConnection();
             PreparedStatement ps =
                     con.prepareStatement(
                             "UPDATE leave_requests SET status='REJECTED' WHERE id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/leaves";
    }
    
 // ================= EMPLOYEE LEAVE HISTORY =================
    @GetMapping("/my")
    public String myLeaves(Model model, HttpSession session) {

        String role = (String) session.getAttribute("role");
        Integer empId = (Integer) session.getAttribute("employeeId");

        if (role == null || !"EMPLOYEE".equals(role)) {
            return "redirect:/accessDenied";
        }

        List<Map<String, Object>> leaveList = new ArrayList<>();

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "SELECT * FROM leave_requests WHERE employee_id=? ORDER BY id DESC")) {

            ps.setInt(1, empId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Map<String, Object> map = new HashMap<>();

                map.put("id", rs.getInt("id"));
                map.put("fromDate", rs.getDate("from_date"));
                map.put("toDate", rs.getDate("to_date"));
                map.put("reason", rs.getString("reason"));
                map.put("status", rs.getString("status"));
                map.put("appliedOn", rs.getTimestamp("applied_on"));

                leaveList.add(map);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("leaves", leaveList);

        return "myLeaves";  // new jsp
    }
 // ================= CANCEL LEAVE =================
    @GetMapping("/cancel/{id}")
    public String cancelLeave(@PathVariable int id,
                              HttpSession session) {

        Integer empId = (Integer) session.getAttribute("employeeId");
        String role = (String) session.getAttribute("role");

        if (role == null || !"EMPLOYEE".equals(role)) {
            return "redirect:/accessDenied";
        }

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                 "UPDATE leave_requests SET status='CANCELLED' WHERE id=? AND employee_id=? AND status='PENDING'")) {

            ps.setInt(1, id);
            ps.setInt(2, empId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/leaves/my";
    }
}