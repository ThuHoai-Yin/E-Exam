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
        List<Test> result = DataAccessObject.getTest(testCode);
        if (result.size() > 0) {            
            Test test = DataAccessObject.getTest(testCode).get(0);
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
                request.setAttribute("Code", "409");
                request.setAttribute("Detail", "Conflict");
                request.setAttribute("Msg", " Please contact your teacher for more information");
                request.getRequestDispatcher("notification.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("Code", "404");
            request.setAttribute("Detail", "Not Found");
            request.setAttribute("Msg", "Please check test code and try again");
            request.getRequestDispatcher("notification.jsp").forward(request, response);
        }
    }
}
