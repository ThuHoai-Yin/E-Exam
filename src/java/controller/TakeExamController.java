package controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Answer;
import model.User;
import model.Question;
import model.Exam;
import utils.DataAccessObject;

@WebServlet(name = "TakeExamController", urlPatterns = {"/takeExam"})
public class TakeExamController extends HttpServlet {

    public static final int MAX_DELAY_TIME = 5;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Timestamp current = new Timestamp(System.currentTimeMillis());
        HttpSession session = request.getSession();
        Exam exam = (Exam) session.getAttribute("exam");
        if (exam == null) {
            response.sendRedirect("home");
        } else if (exam.getExamEndTime().before(current)) {
            session.setAttribute("exam", null);
            response.sendRedirect("home");
        } else {
            request.getRequestDispatcher("takeExam.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Exam exam = (Exam) session.getAttribute("exam");
        User user = (User) session.getAttribute("user");
        if (exam == null) {
            request.setAttribute("msg", "Your exam is overdue or has already been submitted");
            request.setAttribute("detail", "Please contact your teacher for more information");
            request.setAttribute("backURL", "home");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(exam.getExamEndTime().getTime());
        cal.add(Calendar.SECOND, MAX_DELAY_TIME);
        Timestamp deadline = new Timestamp(cal.getTime().getTime());

        if (new Timestamp(System.currentTimeMillis()).after(deadline)) {
            request.setAttribute("msg", "Your exam is overdue");
            request.setAttribute("detail", "Please contact your teacher for more information");
            request.setAttribute("backURL", "home");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        int count = 0;
        for (Question q : exam.getQuestions()) {
            int selected = 0;
            for (Answer a : q.getAnswers()) {
                a.setSelected(Objects.deepEquals(request.getParameter("answer." + a.getAnswerID()), "on"));
                selected += (a.isSelected() ? 1 : 0);
            }
            if (selected > q.getMaxChoose()) {
                request.setAttribute("msg", "Your exam was submitted is invalid!");
                request.setAttribute("detail", "Please retake another exam");
                request.setAttribute("backURL", "home");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                session.setAttribute("exam", null);
                return;
            }
            count += selected;
        }
        if (DataAccessObject.saveRecord(exam.getExamCode(), user.getUserID(), exam, count)) {
            request.setAttribute("msg", "Your exam has been recorded");
            request.setAttribute("detail", "The exam result will be announced later");
            request.getRequestDispatcher("success.jsp").forward(request, response);
        } else {
            request.setAttribute("msg", "Your exam has already been submitted");
            request.setAttribute("detail", "Please don't submit again");
            request.setAttribute("backURL", "home");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
        session.setAttribute("exam", null);
    }
}
