package com.ration;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/viewTransactions")
public class ViewTransactionsServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<html><head>");
        out.println("<link rel='stylesheet' href='/ration-system/style.css'>");
        out.println("</head><body>");

        out.println("<div class='top-section'><h1>Transaction Report</h1></div>");

        out.println("<div class='table-container'>");

        // TABLE START
        out.println("<table>");

        // HEADER
        out.println("<tr>");
        out.println("<th>ID</th>");
        out.println("<th>User ID</th>");
        out.println("<th>Item</th>");
        out.println("<th>Quantity</th>");
        out.println("<th>Date</th>");
        out.println("</tr>");

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement("SELECT * FROM transactions");
            ResultSet rs = ps.executeQuery();

            // ROWS
            while (rs.next()) {

                out.println("<tr>");

                out.println("<td>" + rs.getInt("id") + "</td>");
                out.println("<td>" + rs.getInt("user_id") + "</td>");
                out.println("<td>" + rs.getString("item_name") + "</td>");
                out.println("<td>" + rs.getInt("quantity") + "</td>");
                out.println("<td>" + rs.getTimestamp("date") + "</td>");

                out.println("</tr>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        out.println("</table>");

        // BACK BUTTON (clean, no weird symbol)
        out.println("<br><br>");
        out.println("<div style='text-align:center;'>");
        out.println("<a href='report.html'>Back</a>");
        out.println("</div>");

        out.println("</div>");
        out.println("</body></html>");
    }
}