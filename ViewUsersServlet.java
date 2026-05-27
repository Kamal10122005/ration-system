package com.ration;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/viewUsers")
public class ViewUsersServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head>");
        out.println("<link rel='stylesheet' href='/ration-system/style.css'>");
        out.println("</head><body>");

        out.println("<div class='top-section'><h1>All Beneficiaries</h1></div>");

        out.println("<div class='table-container'>");

        

        out.println("<table>");
        out.println("<tr>");
        out.println("<th>ID</th>");
        out.println("<th>Name</th>");
        out.println("<th>Card No</th>");
        out.println("<th>Category</th>");
        out.println("<th>Family</th>");
        out.println("<th>Action</th>");
        out.println("</tr>");

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT * FROM users");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getString("name") + "</td>");
                out.println("<td>" + rs.getString("ration_card_no") + "</td>");
                out.println("<td>" + rs.getString("category") + "</td>");
                out.println("<td>" + rs.getInt("family_count") + "</td>");
                out.println("<td><a href='deleteUser?id=" + rs.getInt("id") + "' onclick=\"return confirm('Delete this user?')\">Delete</a></td>");
                out.println("</tr>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        out.println("</table>");
        out.println("<a href='dashboard.html'>Back to Dashboard</a><br><br>");
        out.println("</div>");

        out.println("</body></html>");
    }
}