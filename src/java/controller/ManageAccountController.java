package controller;

import java.io.IOException;
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

        try {
            if (action == null) {
                throw new Exception("Null");
            }

            switch (action) {
                case "add":
                    String username = request.getParameter("username");
                    String password = request.getParameter("password");
                    String fullName = request.getParameter("fullName");
                    String email = request.getParameter("email");
                    String roleName = request.getParameter("roleName");

                    // Validation                
                    if (username == null || password == null || fullName == null || email == null || roleName == null) {
                        throw new Exception("Null");
                    }

                    if (!DataAccessObject.register(username, password, fullName, email, roleName)) {
                        throw new Exception("Failed");
                    }
                    break;
                case "edit":
                    String userIDStr = request.getParameter("userID");
                    password = request.getParameter("password");
                    fullName = request.getParameter("fullName");
                    email = request.getParameter("email");
                    roleName = request.getParameter("roleName");

                    // Validation
                    if (userIDStr == null || password == null || fullName == null || email == null || roleName == null) {
                        throw new Exception("Null");
                    }

                    int userID = Integer.parseInt(userIDStr);
                    if (!DataAccessObject.updateAccount(userID, password, fullName, email, roleName)) {
                        throw new Exception("Failed");
                    }
                    break;

                case "remove":
                    userIDStr = request.getParameter("userID");

                    // Validation
                    if (userIDStr == null) {
                        throw new Exception("Null");
                    }

                    userID = Integer.parseInt(userIDStr);
                    if (!DataAccessObject.removeAccount(userID)) {
                        throw new Exception("Failed");
                    }
                    break;
            }
            response.sendRedirect("manageAccount");
        } catch (Exception ex) {
            request.setAttribute("msg", "Invalid request!");
            request.setAttribute("backURL", "manageAccount");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            System.out.println(ex.getMessage());
        }
    }
}
