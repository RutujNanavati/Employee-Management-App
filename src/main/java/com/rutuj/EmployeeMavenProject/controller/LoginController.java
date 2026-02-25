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

//    // ========= PROCESS SIGNUP =========
//    @PostMapping("/signup")
//    public String processSignup(
//            @RequestParam String username,
//            @RequestParam String password,
//            @RequestParam(defaultValue = "ADMIN") String role,
//            Model model) {
//
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String hashedPassword = encoder.encode(password);
//
//        try (Connection con = getConnection();
//             PreparedStatement ps =
//                     con.prepareStatement(
//                         "INSERT INTO admin(username,password,role) VALUES(?,?,?)")) {
//
//            ps.setString(1, username);
//            ps.setString(2, hashedPassword);
//            ps.setString(3, role);
//
//            ps.executeUpdate();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            model.addAttribute("error", "Signup Failed");
//            return "signup";
//        }
//
//        return "redirect:/login";
//    }

    // ========= PROCESS LOGIN =========
    @PostMapping("/login")
    public String processLogin(
            @RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            Model model) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        try (Connection con = getConnection()) {

            // 🔥 First check ADMIN table
            PreparedStatement adminPs =
                    con.prepareStatement("SELECT * FROM admin WHERE username=?");

            adminPs.setString(1, username);
            ResultSet adminRs = adminPs.executeQuery();

            if (adminRs.next()) {

                String dbPassword = adminRs.getString("password");
                String role = adminRs.getString("role");

                if (encoder.matches(password, dbPassword)) {

                    session.setAttribute("user", username);
                    session.setAttribute("role", role);
                    session.setAttribute("photo", null);
                    
                    PreparedStatement logPs = con.prepareStatement(
                    	    "INSERT INTO login_logs(username, role) VALUES(?, ?)"
                    	);
                    	logPs.setString(1, username);
                    	logPs.setString(2, adminRs.getString("role"));
                    	logPs.executeUpdate();

                    return "redirect:/dashboard";
                    
                    
                }
            }

            // 🔥 If not admin, check EMPLOYEES table
            PreparedStatement empPs =
                    con.prepareStatement("SELECT * FROM employees WHERE username=?");

            empPs.setString(1, username);
            ResultSet empRs = empPs.executeQuery();

            if (empRs.next()) {

                String dbPassword = empRs.getString("password");

                
				/* For Users whose passes are not Bcrypted.
				 * boolean correct = false;
				 * 
				 * if (dbPassword != null && dbPassword.startsWith("$2a$")) { correct =
				 * encoder.matches(oldPassword, dbPassword); } else { correct =
				 * oldPassword.equals(dbPassword); }
				 * 
				 * if (!correct) { model.addAttribute("error", "Old password incorrect"); return
				 * "changePassword"; }
				 */
                boolean loginSuccess = false;

                // 🔥 If hashed password
                if (dbPassword != null && dbPassword.startsWith("$2a$")) {

                    if (encoder.matches(password, dbPassword)) {
                        loginSuccess = true;
                    }

                } 
                // 🔥 If plain text password (temporary support)
                else {
                    if (password.equals(dbPassword)) {
                        loginSuccess = true;
                    }
                }

                if (loginSuccess) {

                    session.setAttribute("user", username);
                    session.setAttribute("role", "EMPLOYEE");
                    session.setAttribute("photo", empRs.getString("photo"));
                    session.setAttribute("employeeId", empRs.getInt("id"));
                    
                    PreparedStatement logPs = con.prepareStatement(
                    	    "INSERT INTO login_logs(username, role) VALUES(?, ?)"
                    	);
                    	logPs.setString(1, username);
                    	logPs.setString(2, empRs.getString("role"));
                    	logPs.executeUpdate();


                    return "redirect:/employees/profile/" + empRs.getInt("id");
                    
                    
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("error", "Invalid Credentials");
        return "login";
    }
    
    @GetMapping("/change-password")
    public String changePasswordPage(HttpSession session) {

        if (session.getAttribute("role") == null) {
            return "redirect:/login";
        }

        return "changePassword";
    }

    @PostMapping("/change-password")
    public String changePassword(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            HttpSession session,
            Model model) {

        String username = (String) session.getAttribute("user");
        String role = (String) session.getAttribute("role");

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        try (Connection con = getConnection()) {

            String table = role.equals("EMPLOYEE") ? "employees" : "admin";

            PreparedStatement ps =
                con.prepareStatement("SELECT password FROM " + table + " WHERE username=?");

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                String dbPassword = rs.getString("password");

                if (!encoder.matches(oldPassword, dbPassword)) {
                    model.addAttribute("error", "Old password incorrect");
                    return "changePassword";
                }

                String hashed = encoder.encode(newPassword);

                PreparedStatement updatePs =
                    con.prepareStatement("UPDATE " + table + " SET password=? WHERE username=?");

                updatePs.setString(1, hashed);
                updatePs.setString(2, username);
                updatePs.executeUpdate();

                model.addAttribute("success", "Password updated successfully!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "login";
    }


    // ========= LOGOUT =========
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
