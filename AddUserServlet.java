package com.ration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/addUser")
public class AddUserServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String card = request.getParameter("card");
        String category = request.getParameter("category");
        int family = Integer.parseInt(request.getParameter("family"));
        String password = request.getParameter("password");

        try {
            Connection con = DBConnection.getConnection();

            String query = "INSERT INTO users (name, ration_card_no, category, family_count, password) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);

            ps.setString(1, name);
            ps.setString(2, card);
            ps.setString(3, category);
            ps.setInt(4, family);
            ps.setString(5, password);

            ps.executeUpdate();

            response.sendRedirect(request.getContextPath() + "/dashboard.html");

        } catch (Exception e) {
            e.printStackTrace(); // 🔥 VERY IMPORTANT
            response.getWriter().println("<h2 style='color:red;'>Error adding user</h2>");
        }
    }
}