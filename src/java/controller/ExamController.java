package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Enumeration;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Answer;
import model.Authentication;
import model.Question;
import model.Test;
import utils.DataAccessObject;

@WebServlet(name = "ExamController", urlPatterns = {"/exam"})
public class ExamController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Timestamp current = new Timestamp(System.currentTimeMillis());
        HttpSession session = request.getSession();
        Test test = (Test)session.getAttribute("Test");
        if (test == null) {
            response.sendRedirect("home");
        }
        else if (test.getExamEndTime().before(current)) {
            session.setAttribute("Test", null);
            response.sendRedirect("home");
        } else {
            request.getRequestDispatcher("exam.jsp").forward(request, response);
        }        
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Test test = (Test)session.getAttribute("Test");
        Authentication auth = (Authentication)session.getAttribute("Auth");
        if (test == null) {
            response.sendRedirect("home");
            return;
        }
        int count = 0;
        for (Question q : test.getQuestions()) {
            for (Answer a : q.getAnswers()) {
                a.setSelected(Objects.deepEquals(request.getParameter("answerID-" + a.getAnswerID()),"on"));
                count += (a.isSelected() ? 1 : 0);
            }
        }
        int recordID = DataAccessObject.getRecordID(test.getTestCode(), auth.getID());
        DataAccessObject.saveRecord(recordID, test, count);
    }
}
