package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import utils.DataAccessObject;

@WebServlet(name = "ManageAccount", urlPatterns = {"/manageAccount"})
public class ManageAccountController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("accounts", DataAccessObject.getUsers());
        request.setAttribute("roles", DataAccessObject.getRoles());
        request.getRequestDispatcher("manageAccount.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            return;
        }
        switch (action) {
            case "add":
                String username = request.getParameter("username");
                String password = request.getParameter("password");
                String fullName = request.getParameter("fullName");
                String email = request.getParameter("email");
                String roleName = request.getParameter("roleName");
                
                // Validation
                                
                if (!DataAccessObject.register(username, password, fullName, email, roleName)) {
                    request.setAttribute("msg", "This username is already existed");
                    request.setAttribute("detail", "Please use another username");
                    request.setAttribute("backURL", "manageAccount");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                }
                break;
            case "edit":  
                String userIDStr = request.getParameter("userID");
                password = request.getParameter("password");
                fullName = request.getParameter("fullName");
                email = request.getParameter("email");
                roleName = request.getParameter("roleName");
                
                // Validation
                
                try {
                    int userID = Integer.parseInt(userIDStr);
                    if (!DataAccessObject.updateAccount(userID, password, fullName, email, roleName)) {

                    }
                } catch (NumberFormatException ex) {

                }                
                break;
            case "remove":
                userIDStr = request.getParameter("userID");
                try {
                    int userID = Integer.parseInt(userIDStr);
                    if (!DataAccessObject.removeAccount(userID)) {

                    }
                } catch (NumberFormatException ex) {

                }
                break;
        }
        response.sendRedirect("manageAccount");
    }
}
