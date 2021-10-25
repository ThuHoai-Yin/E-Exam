package controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import utils.DataAccessObject;

@WebServlet(name = "Admin", urlPatterns = {"/admin"})
public class AdminController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("admin.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        User user = DataAccessObject.login(username, password, "admin");
        if (user == null) {
            request.setAttribute("errMsg", " - Username or Password is invalid.");
            request.setAttribute("lastUser", username);
            Object obj = request.getServletContext().getAttribute("loginFailureTimes");         
            int loginFailureTimes = obj != null ? (Integer) obj : 0;
            request.getServletContext().setAttribute("loginFailureTimes", loginFailureTimes + 1);
            request.getRequestDispatcher("admin.jsp").forward(request, response);
        } else {
            session.setAttribute("user", user);
            response.sendRedirect("home");
        }
    }
}
