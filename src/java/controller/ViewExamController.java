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

@WebServlet(name = "ViewExam", urlPatterns = {"/viewExam"})
public class ViewExamController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String examCode = request.getParameter("examCode");
        request.setAttribute("records", DataAccessObject.getRecordsOfAExam(examCode));
        request.getRequestDispatcher("viewExam.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        String examCode = request.getParameter("examCode");
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        if (action == null) {
            return;
        }
        switch (action) {
            case "remove":
                String recordIDTxt = request.getParameter("recordID");
                int recordID = Integer.parseInt(recordIDTxt);
                if (!DataAccessObject.removeRecord(recordID, user.getUserID())) {
                    
                }
                break;
        }
        response.sendRedirect("viewExam?examCode=" + examCode);
    }
}
