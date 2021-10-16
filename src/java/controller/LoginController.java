package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Authentication;
import utils.DataAccessObject;
import utils.Jwt;

@WebServlet(name = "Login", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (response.getStatus() != 302) {
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        System.out.println(username);
        System.out.println(password);
        Authentication auth = DataAccessObject.login(username, password, role);
        if (auth == null) {
            request.setAttribute("errMsg", " - Username or Password is invalid.");
            request.setAttribute("lastUser", username);
            request.setAttribute("userRole", role);
            request.getRequestDispatcher("login.jsp").forward(request, response);
        } else {
            String token = Jwt.generateToken(auth);
            response.addCookie(new Cookie("jwt", token) {
                {
                    setHttpOnly(true);
                    setMaxAge((int) Jwt.JWT_TOKEN_VALIDITY);
                }
            });
            response.sendRedirect("home");
        }

    }
}
