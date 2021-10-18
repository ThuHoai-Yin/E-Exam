package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Calendar;
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
import model.Exam;
import utils.DataAccessObject;

@WebServlet(name = "ExamController", urlPatterns = {"/exam"})
public class ExamController extends HttpServlet {

    public static final int MAX_DELAY_TIME = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Timestamp current = new Timestamp(System.currentTimeMillis());
        HttpSession session = request.getSession();
        Exam exam = (Exam) session.getAttribute("Exam");
        if (exam == null) {
            response.sendRedirect("home");
        } else if (exam.getExamEndTime().before(current)) {
            session.setAttribute("Exam", null);
            response.sendRedirect("home");
        } else {
            request.getRequestDispatcher("exam.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Exam exam = (Exam) session.getAttribute("Exam");
        Authentication auth = (Authentication) session.getAttribute("Auth");
        if (exam == null) {
            request.setAttribute("Msg", "Your exam is overdue or has already been submitted");
            request.setAttribute("Detail", "Please contact your teacher for more information");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(exam.getExamEndTime().getTime());
        cal.add(Calendar.SECOND, MAX_DELAY_TIME);
        Timestamp deadline = new Timestamp(cal.getTime().getTime());

        if (new Timestamp(System.currentTimeMillis()).after(deadline)) {
            request.setAttribute("Msg", "Your exam is overdue");
            request.setAttribute("Detail", "Please contact your teacher for more information");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        int count = 0;
        for (Question q : exam.getQuestions()) {
            int count1 = 0;
            for (Answer a : q.getAnswers()) {
                a.setSelected(Objects.deepEquals(request.getParameter("answerID-" + a.getAnswerID()), "on"));
                count1 += (a.isSelected() ? 1 : 0);
            }
            if (count1 > q.getMaxChoose()) {
                request.setAttribute("Msg", "Your exam was submitted is invalid!");
                request.setAttribute("Detail", "Please retake another exam");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                session.setAttribute("Exam", null);
                return;
            }
            count += count1;
        }
        if (DataAccessObject.saveRecord(exam.getExamCode(), auth.getID(), exam, count)) {
            request.setAttribute("Msg", "Your exam has been recorded");
            request.setAttribute("Detail", "The exam result will be announced later");
            request.getRequestDispatcher("success.jsp").forward(request, response);
        } else {
            request.setAttribute("Msg", "Your exam has already been submitted");
            request.setAttribute("Detail", "Please don't submit again");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
        session.setAttribute("Exam", null);
    }
}
