package com.rutuj.EmployeeMavenProject.controller;

import java.sql.*;
import java.util.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
public class LocationController {

    private Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/employeedb",
                "root",
                "@Rutuj2005"
        );
    }

    @GetMapping("/countries")
    public List<Map<String,Object>> getCountries() {

        List<Map<String,Object>> list = new ArrayList<>();

        try(Connection con = getConnection();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM country");
            ResultSet rs = ps.executeQuery()) {

            while(rs.next()){
                Map<String,Object> map = new HashMap<>();
                map.put("id", rs.getInt("id"));
                map.put("name", rs.getString("name"));
                list.add(map);
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }

    @GetMapping("/states/{countryId}")
    public List<Map<String,Object>> getStates(@PathVariable int countryId){

        List<Map<String,Object>> list = new ArrayList<>();

        try(Connection con = getConnection();
            PreparedStatement ps =
                con.prepareStatement("SELECT * FROM state WHERE country_id=?")) {

            ps.setInt(1, countryId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Map<String,Object> map = new HashMap<>();
                map.put("id", rs.getInt("id"));
                map.put("name", rs.getString("name"));
                list.add(map);
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }

    @GetMapping("/cities/{stateId}")
    public List<Map<String,Object>> getCities(@PathVariable int stateId){

        List<Map<String,Object>> list = new ArrayList<>();

        try(Connection con = getConnection();
            PreparedStatement ps =
                con.prepareStatement("SELECT * FROM city WHERE state_id=?")) {

            ps.setInt(1, stateId);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                Map<String,Object> map = new HashMap<>();
                map.put("id", rs.getInt("id"));
                map.put("name", rs.getString("name"));
                list.add(map);
            }

        } catch(Exception e){
            e.printStackTrace();
        }

        return list;
    }
}
