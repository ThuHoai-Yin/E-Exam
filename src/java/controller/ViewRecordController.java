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

@WebServlet(name = "ViewRecord", urlPatterns = {"/viewRecord"})
public class ViewRecordController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        String examCode = request.getParameter("examCode");
        String recordIDTxt = request.getParameter("recordID");
        try {
            if (examCode == null || recordIDTxt == null) {
                throw new Exception("Null");
            }

            int recordID = Integer.parseInt(recordIDTxt);
            request.setAttribute("questions", DataAccessObject.getQuestionsAndCorrectAnswer(examCode, user.getUserID()));
            request.setAttribute("detail", DataAccessObject.getRecordDetail(recordID, user.getUserID()));
            request.getRequestDispatcher("viewRecord.jsp").forward(request, response);
        } catch (Exception ex) {
            request.setAttribute("msg", "Invalid request!");
            request.setAttribute("backURL", examCode != null ? "viewExam?examCode=" + examCode : "home");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            System.out.println(ex.getMessage());
        }
    }
}
