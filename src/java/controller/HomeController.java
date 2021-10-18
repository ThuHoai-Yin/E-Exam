package controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Authentication;
import model.Exam;
import utils.DataAccessObject;

@WebServlet(name = "Home", urlPatterns = {"/home"})
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (response.getStatus() != 302) {
            Timestamp current = new Timestamp(System.currentTimeMillis());
            HttpSession session = request.getSession();
            Exam exam = (Exam) session.getAttribute("Exam");

            if (exam != null) {
                if (exam.getExamEndTime().after(current)) {
                    response.sendRedirect("exam");
                    return;
                } else {
                    session.setAttribute("Exam", null);
                }
            }

            request.getRequestDispatcher("home.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String examCode = request.getParameter("examCode");
        System.out.println(examCode);
        List<Exam> result = DataAccessObject.getExam(examCode);
        if (result.size() > 0) {
            Authentication auth = (Authentication) session.getAttribute("Auth");
            if (DataAccessObject.initRecord(examCode, auth.getID())) {
                Exam exam = DataAccessObject.getExam(examCode).get(0);
                Timestamp current = new Timestamp(System.currentTimeMillis());
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(current.getTime());
                cal.add(Calendar.SECOND, exam.getDuration());
                Timestamp later = new Timestamp(cal.getTime().getTime());
                exam.setExamEndTime(later);
                exam.shuffle();
                session.setAttribute("Exam", exam);
                response.sendRedirect("exam");
            } else {
                request.setAttribute("Msg", "You have already taken this exam");
                request.setAttribute("Detail", "Please contact your teacher for more information");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("Msg", "This exam code doesn't exist");
            request.setAttribute("Detail", "Please check and try again");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
