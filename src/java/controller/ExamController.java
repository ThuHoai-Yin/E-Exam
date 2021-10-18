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
        Test test = (Test) session.getAttribute("Test");
        if (test == null) {
            response.sendRedirect("home");
        } else if (test.getExamEndTime().before(current)) {
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
        Test test = (Test) session.getAttribute("Test");
        Authentication auth = (Authentication) session.getAttribute("Auth");
        if (test == null) {
            request.setAttribute("Msg", "Your test is overdue or has already been submitted");
            request.setAttribute("Detail", "Please contact your teacher for more information");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }
        int count = 0;
        for (Question q : test.getQuestions()) {
            int count1 = 0;
            for (Answer a : q.getAnswers()) {
                a.setSelected(Objects.deepEquals(request.getParameter("answerID-" + a.getAnswerID()), "on"));
                count1 += (a.isSelected() ? 1 : 0);
            }
            if (count1 > q.getMaxChoose()) {
                request.setAttribute("Msg", "Your exam was submitted is invalid!");
                request.setAttribute("Detail", "Please retake another exam");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                session.setAttribute("Test", null);
                return;
            }
            count += count1;
        }
        if (DataAccessObject.saveRecord(test.getTestCode(), auth.getID(), test, count)) {
            request.setAttribute("Msg", "Your test has been recorded");
            request.setAttribute("Detail", "The test result will be announced later");
            request.getRequestDispatcher("success.jsp").forward(request, response);
        } else {
            request.setAttribute("Msg", "Your test has already been sumbited");
            request.setAttribute("Detail", "Please don't submit again");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
        session.setAttribute("Test", null);
    }
}
