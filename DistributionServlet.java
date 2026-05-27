package com.ration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/distribute")
public class DistributionServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int userId = Integer.parseInt(request.getParameter("userId"));
        String item = request.getParameter("item");
        int qty = Integer.parseInt(request.getParameter("qty"));

        try {
            Connection con = DBConnection.getConnection();

            // 1. Check if item exists in stock
            String checkQuery = "SELECT quantity FROM stock WHERE LOWER(item_name) = LOWER(?)";
            PreparedStatement psCheck = con.prepareStatement(checkQuery);
            psCheck.setString(1, item);

            ResultSet rs = psCheck.executeQuery();

            if (rs.next()) {

                int currentStock = rs.getInt("quantity");

                // 2. Check if enough stock is available
                if (currentStock >= qty) {

                    // 3. Reduce stock
                    String updateStock = "UPDATE stock SET quantity = quantity - ? WHERE LOWER(item_name) = LOWER(?)";
                    PreparedStatement psUpdate = con.prepareStatement(updateStock);
                    psUpdate.setInt(1, qty);
                    psUpdate.setString(2, item);
                    psUpdate.executeUpdate();

                    // 4. 🔥 Auto delete if quantity becomes 0
                    String deleteZero = "DELETE FROM stock WHERE quantity <= 0";
                    PreparedStatement psDelete = con.prepareStatement(deleteZero);
                    psDelete.executeUpdate();

                    // 5. Insert transaction
                    String insert = "INSERT INTO transactions (user_id, item_name, quantity) VALUES (?, ?, ?)";
                    PreparedStatement psInsert = con.prepareStatement(insert);
                    psInsert.setInt(1, userId);
                    psInsert.setString(2, item);
                    psInsert.setInt(3, qty);
                    psInsert.executeUpdate();

                    response.sendRedirect(request.getContextPath() + "/dashboard.html");

                } else {
                    response.setContentType("text/html");
                    response.getWriter().println("<h2 style='color:red;'>Not enough stock</h2>");
                }

            } else {
                response.setContentType("text/html");
                response.getWriter().println("<h2 style='color:red;'>Item not found in stock</h2>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}