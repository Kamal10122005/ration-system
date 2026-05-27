package com.ration;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/distribution")
public class StockDropdownServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head>");
        out.println("<link rel='stylesheet' href='/ration-system/style.css'>");
        out.println("</head><body>");

        out.println("<div class='top-section'><h1>Distribute Ration</h1></div>");

        out.println("<div class='form-container'>");

        out.println("<form action='distribute' method='post'>");

        out.println("<div class='form-group'>");
        out.println("<label>User ID</label>");
        out.println("<input type='number' name='userId' required>");
        out.println("</div>");

        out.println("<div class='form-group'>");
        out.println("<label>Item</label>");
        out.println("<select name='item'>");

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT item_name FROM stock");
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String item = rs.getString("item_name");
                out.println("<option value='" + item + "'>" + item + "</option>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        out.println("</select>");
        out.println("</div>");

        out.println("<div class='form-group'>");
        out.println("<label>Quantity</label>");
        out.println("<input type='number' name='qty' required>");
        out.println("</div>");

        out.println("<button type='submit'>Distribute</button>");

        out.println("</form>");

        out.println("<br>");
        out.println("<a href='dashboard.html'>Back to Dashboard</a>");

        out.println("</div>");

        out.println("</body></html>");
    }
}