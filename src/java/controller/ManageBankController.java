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

@WebServlet(name = "ManageBank", urlPatterns = {"/manageBank"})
public class ManageBankController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("banks", DataAccessObject.getBank(-1));
        request.getRequestDispatcher("manageBank.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        if (action == null) {
            request.setAttribute("msg", "Invalid request!");
            request.setAttribute("detail", "");
            request.setAttribute("backURL", "manageBank");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        switch (action) {
            case "remove":
                String bankIDTxt = request.getParameter("bankID");
                if (bankIDTxt == null) {
                    request.setAttribute("msg", "Invalid request!");
                    request.setAttribute("detail", "");
                    request.setAttribute("backURL", "manageBank");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }
                try {
                    int bankID = Integer.parseInt(bankIDTxt);
                    if (!DataAccessObject.removeBank(bankID, user.getUserID())) {
                        request.setAttribute("msg", "Invalid request!");
                        request.setAttribute("detail", "");
                        request.setAttribute("backURL", "manageBank");
                        request.getRequestDispatcher("error.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException ex) {
                    request.setAttribute("msg", "Invalid request!");
                    request.setAttribute("detail", "");
                    request.setAttribute("backURL", "manageBank");
                    request.getRequestDispatcher("error.jsp").forward(request, response);
                    return;
                }
                break;
        }
        response.sendRedirect("manageBank");
    }
}
