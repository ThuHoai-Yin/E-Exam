package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import utils.DataAccessObject;

@WebServlet(name = "Login", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String roleName = request.getParameter("roleName");

        try {
            if (username == null || password == null || roleName == null) {
                throw new Exception("Null");
            }

            User user = DataAccessObject.login(username, password, roleName);
            if (user == null) {
                request.setAttribute("errMsg", " - Username or Password is invalid.");
                request.setAttribute("lastUser", username);
                request.setAttribute("userRole", roleName);
                request.getRequestDispatcher("login.jsp").forward(request, response);
            } else {
                session.setAttribute("user", user);
                response.sendRedirect("home");
            }
        } catch (Exception ex) {
            request.setAttribute("msg", "Invalid request!");
            request.setAttribute("backURL", "login");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            System.out.println(ex.getMessage());
        }
    }
}
