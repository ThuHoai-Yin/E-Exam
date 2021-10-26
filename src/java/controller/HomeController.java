package controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import model.Exam;
import model.Question;
import utils.DataAccessObject;

@WebServlet(name = "Home", urlPatterns = {"/home"})
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("home.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String examCode = request.getParameter("examCode");
        List<Exam> result = DataAccessObject.getExam(examCode, -1);
        if (!result.isEmpty()) {
            Exam exam = result.get(0);
            Timestamp current = new Timestamp(System.currentTimeMillis());
            if (exam.getOpenDate() != null && current.before(exam.getOpenDate())) {
                request.setAttribute("msg", "This exam isn't opened");
                request.setAttribute("detail", "Please wait until " + exam.getOpenDate().toString());
                request.setAttribute("backURL", "home");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }
            if (exam.getCloseDate() != null && current.after(exam.getCloseDate())) {
                request.setAttribute("msg", "This exam have already been closed at<br/>" + exam.getCloseDate().toString());
                request.setAttribute("detail", "Please contact your teacher for more information");
                request.setAttribute("backURL", "home");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            if (DataAccessObject.initRecord(examCode, user.getUserID())) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(current.getTime());
                cal.add(Calendar.SECOND, exam.getDuration());
                Timestamp later = new Timestamp(cal.getTime().getTime());
                exam.setExamEndTime(later);
                exam.shuffle();
                for (Question q : exam.getQuestions()) {
                    q.shuffle();
                }
                session.setAttribute("exam", exam);
                response.sendRedirect("exam");
            } else {
                request.setAttribute("msg", "You have already taken this exam");
                request.setAttribute("detail", "Please contact your teacher for more information");
                request.setAttribute("backURL", "home");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("msg", "This exam code doesn't exist");
            request.setAttribute("detail", "Please check and try again");
            request.setAttribute("backURL", "home");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
