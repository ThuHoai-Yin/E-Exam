package controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import utils.DataAccessObject;

@WebServlet(name = "ManageExam", urlPatterns = {"/manageExam"})
public class ManageExamController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("banks", DataAccessObject.getBank(-1));
        request.setAttribute("exams", DataAccessObject.getExam(null));
        request.getRequestDispatcher("manageExam.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");
        if (action == null) {
            return;
        }
        switch (action) {
            case "add":
                String bankIDTxt = request.getParameter("bankID");
                String examName = request.getParameter("examName");
                String openDateTxt = request.getParameter("openDate");
                String closeDateTxt = request.getParameter("closeDate");
                String numOfQuestionsTxt = request.getParameter("numOfQuestions");
                String durationTxt = request.getParameter("duration");

                DateFormat formatter = new SimpleDateFormat("hh:mm dd/MM/yyyy");
                Timestamp openDate = null,
                 closeDate = null;
                int bankID,
                 numOfQuestions,
                 duration;

                try {
                    bankID = Integer.parseInt(bankIDTxt);
                    if (!openDateTxt.isEmpty()) {
                        openDate = new Timestamp(formatter.parse(openDateTxt).getTime());
                    }
                    if (!closeDateTxt.isEmpty()) {
                        closeDate = new Timestamp(formatter.parse(closeDateTxt).getTime());
                    }
                    numOfQuestions = Integer.parseInt(numOfQuestionsTxt);
                    duration = Integer.parseInt(durationTxt);
                } catch (ParseException ex) {
                    System.out.println(ex.getMessage());
                    return;
                }
                if (!DataAccessObject.createExam(bankID, user.getUserID(), examName, openDate, closeDate, numOfQuestions, duration)) {
                    break;
                }
            case "remove":
                String examCode = request.getParameter("examCode");
                if (!DataAccessObject.removeExam(examCode)) {

                }
                break;
        }
        response.sendRedirect("manageExam");
    }
}
