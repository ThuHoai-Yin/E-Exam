package controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Authentication;
import model.Test;
import utils.DataAccessObject;

@WebServlet(name = "Home", urlPatterns = {"/home"})
public class HomeController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (response.getStatus() != 302) {
            Timestamp current = new Timestamp(System.currentTimeMillis());
            HttpSession session = request.getSession();
            Test test = (Test) session.getAttribute("Test");

            if (test != null) {
                if (test.getExamEndTime().after(current)) {
                    response.sendRedirect("exam");
                    return;
                } else {
                    session.setAttribute("Test", null);
                }
            }

            request.getRequestDispatcher("home.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String testCode = request.getParameter("testCode");
        System.out.println(testCode);
        Test test = DataAccessObject.getTest(testCode);
        if (test != null) {
            Authentication auth = (Authentication) session.getAttribute("Auth");
            if (DataAccessObject.createRecord(testCode, auth.getID())) {
                Timestamp current = new Timestamp(System.currentTimeMillis());

                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(current.getTime());
                cal.add(Calendar.SECOND, test.getDuration());
                Timestamp later = new Timestamp(cal.getTime().getTime());
                test.setExamEndTime(later);
                test.shuffle();
                session.setAttribute("Test", test);
                response.sendRedirect("exam");
            } else {
                request.setAttribute("ErrCode", "409");
                request.setAttribute("ErrDetail", "Conflict");
                request.setAttribute("ErrMsg", " Please contact your teacher for more information");
                request.getRequestDispatcher("error.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("ErrCode", "404");
            request.setAttribute("ErrDetail", "Not Found");
            request.setAttribute("ErrMsg", "Please check test code and try again");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }
    }
}
