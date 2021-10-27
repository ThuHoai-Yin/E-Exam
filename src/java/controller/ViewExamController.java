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
        User user = (User) session.getAttribute("user");

        String examCode = request.getParameter("examCode");
        String action = request.getParameter("action");

        try {
            if (examCode == null || action == null) {
                throw new Exception("Null");
            }
            switch (action) {
                case "remove":
                    String recordIDTxt = request.getParameter("recordID");
                    if (recordIDTxt == null) {
                        throw new Exception("Null");
                    }
                    int recordID = Integer.parseInt(recordIDTxt);
                    if (!DataAccessObject.removeRecord(recordID, user.getUserID())) {
                        throw new Exception("Failed");
                    }
                    break;
            }
            response.sendRedirect("viewExam?examCode=" + examCode);
        } catch (Exception ex) {
            request.setAttribute("msg", "Invalid request!");
            request.setAttribute("backURL", examCode != null ? "viewExam?examCode=" + examCode : "home");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
