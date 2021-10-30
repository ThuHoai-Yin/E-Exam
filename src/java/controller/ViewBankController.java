package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Bank;
import utils.DataAccessObject;

@WebServlet(name = "ViewBank", urlPatterns = {"/viewBank"})
public class ViewBankController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bankIDTxt = request.getParameter("bankID");
        int bankID = Integer.parseInt(bankIDTxt);
        List<Bank> result = DataAccessObject.getBank(bankID);
        request.setAttribute("bank", result.get(0));
        request.getRequestDispatcher("viewBank.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {        
    }
}
