package com.rutuj.EmployeeMavenProject.controller;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            Model model, HttpSession session) {
    	
    	String role = (String) session.getAttribute("role");
    	
    	if(role == null) {
    		return "redirect:/login";
    	}
    	
    	if("EMPLOYEE".equals(role)) {
    		Integer empId = (Integer) session.getAttribute("employeeId");
    		return "redirect:/employees/profile/"+empId;
    	}

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
    
 // ================= PROFILE PAGE =================
    @GetMapping("/profile/{id}")
    public String viewProfile(@PathVariable int id,
                              Model model,
                              HttpSession session) {

        String sessionRole = (String) session.getAttribute("role");

        if (sessionRole == null) {
            return "redirect:/login";
        }

        // 🔥 If EMPLOYEE → only allow own profile
        if ("EMPLOYEE".equals(sessionRole)) {
            Integer sessionId = (Integer) session.getAttribute("employeeId");
            if (sessionId == null || sessionId != id) {
                return "redirect:/accessDenied";
            }
        }

        try (Connection con = getConnection()) {

            PreparedStatement empPs =
                    con.prepareStatement("SELECT * FROM employees WHERE id=?");

            empPs.setInt(1, id);
            ResultSet empRs = empPs.executeQuery();

            if (empRs.next()) {

                model.addAttribute("firstName", empRs.getString("firstName"));
                model.addAttribute("lastName", empRs.getString("lastName"));
                model.addAttribute("username", empRs.getString("username"));
                model.addAttribute("gender", empRs.getString("gender"));
                model.addAttribute("address", empRs.getString("address"));
                model.addAttribute("contactNo", empRs.getString("contactNo"));
                model.addAttribute("profileRole", "EMPLOYEE");
                model.addAttribute("photo", empRs.getString("photo"));

                // ================= AGE CALCULATION =================
                Date dobDate = empRs.getDate("dob");
                if (dobDate != null) {
                    LocalDate dob = dobDate.toLocalDate();
                    int age = Period.between(dob, LocalDate.now()).getYears();
                    model.addAttribute("age", age);
                    model.addAttribute("dob", dobDate);
                } else {
                    model.addAttribute("age", "-");
                    model.addAttribute("dob", "-");
                }

                // ================= EXPERIENCE CALCULATION =================
                Date joinDate = empRs.getDate("joining_date");
                if (joinDate != null) {
                    LocalDate joining = joinDate.toLocalDate();
                    Period exp = Period.between(joining, LocalDate.now());

                    String experience = exp.getYears() + " Years " +
                                        exp.getMonths() + " Months";

                    model.addAttribute("experience", experience);
                    model.addAttribute("joiningDate", joinDate);
                } else {
                    model.addAttribute("experience", "-");
                    model.addAttribute("joiningDate", "-");
                }

                return "profile";
            }

            // 🔥 If not employee, check admin table
            PreparedStatement adminPs =
                    con.prepareStatement("SELECT * FROM admin WHERE id=?");

            adminPs.setInt(1, id);
            ResultSet adminRs = adminPs.executeQuery();

            if (adminRs.next()) {

                model.addAttribute("firstName", adminRs.getString("username"));
                model.addAttribute("lastName", "");
                model.addAttribute("username", adminRs.getString("username"));
                model.addAttribute("gender", "-");
                model.addAttribute("address", "-");
                model.addAttribute("contactNo", "-");
                model.addAttribute("profileRole", adminRs.getString("role"));
                model.addAttribute("age", "-");
                model.addAttribute("experience", "-");

                return "profile";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/employees";
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
    	Integer sessionId = (Integer) session.getAttribute("employeeId");

    	if (role == null) {
    	    return "redirect:/login";
    	}

    	// 🔥 Employee can edit only own profile
    	if ("EMPLOYEE".equals(role) && (sessionId == null || sessionId != id)) {
    	    return "redirect:/accessDenied";
    	}

    	// HR and ADMIN allowed
    	if (!"ADMIN".equals(role) && !"HR".equals(role) && !"EMPLOYEE".equals(role)) {
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
                model.addAttribute("dob", rs.getDate("dob"));
                model.addAttribute("address", rs.getString("address"));
                model.addAttribute("contactNo", rs.getString("contactNo"));
                model.addAttribute("joiningDate", rs.getDate("joining_date"));               
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
            @RequestParam(required = false) String password,
            @RequestParam String dob,
            @RequestParam String gender,
            @RequestParam String address,
            @RequestParam String contactNo,
            @RequestParam Integer countryId,
            @RequestParam Integer stateId,
            @RequestParam Integer cityId,
            @RequestParam String joiningDate,
            @RequestParam("photoFile") MultipartFile photoFile

    ) {

        try (Connection con = getConnection()) {

            String hashedPassword = null;

            if (password != null && !password.isEmpty()) {
                BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                hashedPassword = encoder.encode(password);
            }

            String photoName = null;

            if (!photoFile.isEmpty()) {
                photoName = photoFile.getOriginalFilename();
                String uploadPath = "C:/employee_uploads/" + photoName;
                photoFile.transferTo(new java.io.File(uploadPath));
            }

            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO employees(firstName,lastName,username,password,dob,gender,address,contactNo,country_id,state_id,city_id,joining_date,photo) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)"
            );

            int index = 1;

            ps.setString(index++, firstName);
            ps.setString(index++, lastName);
            ps.setString(index++, username);
            ps.setString(index++, hashedPassword);

            if (dob != null && !dob.isEmpty()) {
                ps.setDate(index++, Date.valueOf(dob));
            } else {
                ps.setNull(index++, java.sql.Types.DATE);
            }

            ps.setString(index++, gender);
            ps.setString(index++, address);
            ps.setString(index++, contactNo);
            ps.setInt(index++, countryId);
            ps.setInt(index++, stateId);
            ps.setInt(index++, cityId);

            if (joiningDate != null && !joiningDate.isEmpty()) {
                ps.setDate(index++, Date.valueOf(joiningDate));
            } else {
                ps.setNull(index++, java.sql.Types.DATE);
            }

            ps.setString(index++, photoName);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/employees";
    }


    // ================= UPDATE =================
    @PostMapping("/update")
    public String updateEmployee(
            @RequestParam Integer id,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String username,
            @RequestParam String gender,
            @RequestParam(required = false) String dob,
            @RequestParam String address,
            @RequestParam String contactNo,
            @RequestParam(required = false) String joiningDate,
            @RequestParam Integer countryId,
            @RequestParam Integer stateId,
            @RequestParam Integer cityId,
            @RequestParam("photoFile") MultipartFile photoFile
    ) {

        try (Connection con = getConnection()) {

            String photoName = null;

            if (!photoFile.isEmpty()) {
                photoName = photoFile.getOriginalFilename();
                String uploadPath = "C:/employee_uploads/" + photoName;
                photoFile.transferTo(new java.io.File(uploadPath));
            }

            String sql = "UPDATE employees SET firstName=?, lastName=?, username=?, gender=?, dob=?, joining_date=?, address=?, contactNo=?, country_id=?, state_id=?, city_id=?";

            if (photoName != null) {
                sql += ", photo=?";
            }

            sql += " WHERE id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            int index = 1;

            ps.setString(index++, firstName);
            ps.setString(index++, lastName);
            ps.setString(index++, username);
            ps.setString(index++, gender);

            // DOB
            if (dob != null && !dob.isEmpty()) {
                ps.setDate(index++, Date.valueOf(dob));
            } else {
                ps.setNull(index++, Types.DATE);
            }

            // JOINING DATE
            if (joiningDate != null && !joiningDate.isEmpty()) {
                ps.setDate(index++, Date.valueOf(joiningDate));
            } else {
                ps.setNull(index++, Types.DATE);
            }

            ps.setString(index++, address);
            ps.setString(index++, contactNo);
            ps.setInt(index++, countryId);
            ps.setInt(index++, stateId);
            ps.setInt(index++, cityId);

            if (photoName != null) {
                ps.setString(index++, photoName);
            }

            ps.setInt(index, id);

            int rows = ps.executeUpdate();
            System.out.println("Rows Updated: " + rows);

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
    
    @PostMapping("/changePhoto")
    public String changePhoto(
            @RequestParam int id,
            @RequestParam("photoFile") MultipartFile photoFile,
            HttpSession session) {

        try (Connection con = getConnection()) {

            if (!photoFile.isEmpty()) {

                String photoName = photoFile.getOriginalFilename();
                String uploadPath = "C:/employee_uploads/" + photoName;
                photoFile.transferTo(new java.io.File(uploadPath));

                PreparedStatement ps = con.prepareStatement(
                        "UPDATE employees SET photo=? WHERE id=?");

                ps.setString(1, photoName);
                ps.setInt(2, id);
                ps.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:/employees/profile/" + id;
    }
    
    @GetMapping("/apply-leave")
    public String showLeaveForm(HttpSession session) {

        String role = (String) session.getAttribute("role");

        if (role == null || !"EMPLOYEE".equals(role)) {
            return "redirect:/accessDenied";
        }

        return "applyLeave";
    }
    
    @PostMapping("/apply-leave")
    public String applyLeave(
            @RequestParam String fromDate,
            @RequestParam String toDate,
            @RequestParam String reason,
            HttpSession session) {

        Integer employeeId = (Integer) session.getAttribute("employeeId");

        if (employeeId == null) {
            return "redirect:/login";
        }

        try (Connection con = getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "INSERT INTO leave_requests(employee_id, from_date, to_date, reason) VALUES(?,?,?,?)")) {

            ps.setInt(1, employeeId);
            ps.setDate(2, Date.valueOf(fromDate));
            ps.setDate(3, Date.valueOf(toDate));
            ps.setString(4, reason);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Role: " + session.getAttribute("role"));
        System.out.println("EmployeeId: " + session.getAttribute("employeeId"));

        return "redirect:/employees";
    }

}
