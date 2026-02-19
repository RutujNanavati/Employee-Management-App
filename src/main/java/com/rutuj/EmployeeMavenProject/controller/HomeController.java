package com.rutuj.EmployeeMavenProject.controller;

import java.sql.*;
import java.util.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "redirect:/login";
    }

    // ================= REGISTER PAGE =================
    @GetMapping("/register")
    public String showRegisterPage(
            @RequestParam(value="countryId", required=false) Integer countryId,
            @RequestParam(value="stateId", required=false) Integer stateId,
            Model model) {

        List<Map<String,Object>> countries = new ArrayList<>();
        List<Map<String,Object>> states = new ArrayList<>();
        List<Map<String,Object>> cities = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/employeedb",
                    "root",
                    "@Rutuj2005");

            // LOAD COUNTRIES
            PreparedStatement ps1 = con.prepareStatement("SELECT * FROM country");
            ResultSet rs1 = ps1.executeQuery();

            while(rs1.next()){
                Map<String,Object> map = new HashMap<>();
                map.put("id", rs1.getInt("id"));
                map.put("name", rs1.getString("name"));
                countries.add(map);
            }

            // LOAD STATES
            if(countryId != null){
                PreparedStatement ps2 = con.prepareStatement(
                        "SELECT * FROM state WHERE country_id=?");
                ps2.setInt(1, countryId);
                ResultSet rs2 = ps2.executeQuery();

                while(rs2.next()){
                    Map<String,Object> map = new HashMap<>();
                    map.put("id", rs2.getInt("id"));
                    map.put("name", rs2.getString("name"));
                    states.add(map);
                }
            }

            // LOAD CITIES
            if(stateId != null){
                PreparedStatement ps3 = con.prepareStatement(
                        "SELECT * FROM city WHERE state_id=?");
                ps3.setInt(1, stateId);
                ResultSet rs3 = ps3.executeQuery();

                while(rs3.next()){
                    Map<String,Object> map = new HashMap<>();
                    map.put("id", rs3.getInt("id"));
                    map.put("name", rs3.getString("name"));
                    cities.add(map);
                }
            }

            con.close();

        } catch(Exception e){
            e.printStackTrace();
        }

        model.addAttribute("countries", countries);
        model.addAttribute("states", states);
        model.addAttribute("cities", cities);

        return "register";
    }

    // ================= SAVE =================
    @PostMapping("/register")
    public String registerEmployee(
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String gender,
            @RequestParam String address,
            @RequestParam String contactNo,
            @RequestParam(required=false) Integer countryId,
            @RequestParam(required=false) Integer stateId,
            @RequestParam(required=false) Integer cityId,
            @RequestParam("photoFile") MultipartFile photoFile
    ) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/employeedb",
                    "root",
                    "@Rutuj2005");

            // 🔐 PASSWORD HASH
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            String hashedPassword = encoder.encode(password);

            // 📸 PHOTO SAVE
            String photoName = null;

            if (!photoFile.isEmpty()) {
                photoName = photoFile.getOriginalFilename();
                String uploadPath = "C:/employee_uploads/" + photoName;
                photoFile.transferTo(new java.io.File(uploadPath));
            }

            String sql = "INSERT INTO employees(firstName,lastName,username,password,gender,address,contactNo,country_id,state_id,city_id,photo) VALUES(?,?,?,?,?,?,?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, username);
            ps.setString(4, hashedPassword);
            ps.setString(5, gender);
            ps.setString(6, address);
            ps.setString(7, contactNo);
            ps.setObject(8, countryId);
            ps.setObject(9, stateId);
            ps.setObject(10, cityId);
            ps.setString(11, photoName);

            ps.executeUpdate();

            con.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/login";
    }

}