package com.ration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/updateStock")
public class StockServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String item = request.getParameter("item");
        int qty = Integer.parseInt(request.getParameter("qty"));
        double price = Double.parseDouble(request.getParameter("price"));

        try {
            Connection con = DBConnection.getConnection();

            // 1. Try updating existing item
            String updateQuery = "UPDATE stock SET quantity = quantity + ?, price = ? WHERE LOWER(item_name) = LOWER(?)";
            PreparedStatement psUpdate = con.prepareStatement(updateQuery);
            psUpdate.setInt(1, qty);
            psUpdate.setDouble(2, price);
            psUpdate.setString(3, item);

            int rows = psUpdate.executeUpdate();

            // 2. If item does not exist → insert
            if (rows == 0) {
                String insertQuery = "INSERT INTO stock (item_name, quantity, price) VALUES (?, ?, ?)";
                PreparedStatement psInsert = con.prepareStatement(insertQuery);
                psInsert.setString(1, item);
                psInsert.setInt(2, qty);
                psInsert.setDouble(3, price);
                psInsert.executeUpdate();
            }

            // 3. 🔥 AUTO DELETE if quantity <= 0
            String deleteZeroQuery = "DELETE FROM stock WHERE quantity <= 0";
            PreparedStatement psDeleteZero = con.prepareStatement(deleteZeroQuery);
            psDeleteZero.executeUpdate();

            // 4. Redirect to stock view
            response.sendRedirect(request.getContextPath() + "/viewStock");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}