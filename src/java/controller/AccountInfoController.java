package controller;

import com.microsoft.sqlserver.jdbc.StringUtils;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import utils.DataAccessObject;

@WebServlet(name = "AccountInfo", urlPatterns = {"/accountInfo"})
public class AccountInfoController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String password = request.getParameter("password");

        if (StringUtils.isEmpty(password) || !DataAccessObject.changePassword(user.getUserID(), password)) {
            request.setAttribute("msg", "Invalid request!");
            request.setAttribute("detail", "");
            request.setAttribute("backURL", request.getParameter("backURL"));
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

        response.sendRedirect(request.getParameter("backURL"));
    }
}
