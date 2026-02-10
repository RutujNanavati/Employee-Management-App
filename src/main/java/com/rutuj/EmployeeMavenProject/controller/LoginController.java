package com.rutuj.EmployeeMavenProject.controller;

import java.sql.*;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
public class LoginController {

    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/employeedb",
                "root",
                "@Rutuj2005"
        );
    }

    // ========= LOGIN PAGE =========
    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    // ========= SIGNUP PAGE =========
    @GetMapping("/signup")
    public String signupPage() {
        return "signup";
    }

    // ========= PROCESS SIGNUP =========
    @PostMapping("/signup")
    public String processSignup(
            @RequestParam String username,
            @RequestParam String password,
            Model model) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(password);

        try (Connection con = getConnection();
             PreparedStatement ps =
                     con.prepareStatement("INSERT INTO admin(username,password) VALUES(?,?)")) {

            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.executeUpdate();

           

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Signup Failed");
            return "signup";
        }


        return "redirect:/login";
    }

    // ========= PROCESS LOGIN =========
    @PostMapping("/login")
    public String processLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        try (Connection con = getConnection();
             PreparedStatement ps =
                     con.prepareStatement("SELECT * FROM admin WHERE username=?")) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String dbPassword = rs.getString("password");

                if (encoder.matches(password, dbPassword)) {
                    session.setAttribute("user", username);
                    return "redirect:/dashboard";
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("error", "Invalid Credentials");
        return "login";
    }

    // ========= LOGOUT =========
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
