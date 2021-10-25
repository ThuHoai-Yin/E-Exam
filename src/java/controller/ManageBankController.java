package controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        String action = request.getParameter("action");
        if (action == null) {
            return;
        }
        switch (action) {
            case "remove":
                String bankIDTxt = request.getParameter("bankID");
                int bankID = Integer.parseInt(bankIDTxt);
                if (!DataAccessObject.removeBank(bankID)) {

                }
                break;
        }
        response.sendRedirect("manageBank");
    }
}
