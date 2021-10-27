package controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        request.setAttribute("banks", DataAccessObject.getBank(-1));
        request.setAttribute("exams", DataAccessObject.getExam(null, user.getUserID()));
        request.getRequestDispatcher("manageExam.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String action = request.getParameter("action");

        try {
            if (action == null) {
                throw new Exception("Null");
            }
            switch (action) {
                case "add":
                    String bankIDTxt = request.getParameter("bankID");
                    String examName = request.getParameter("examName");
                    String openDateTxt = request.getParameter("openDate");
                    String closeDateTxt = request.getParameter("closeDate");
                    String numOfQuestionsTxt = request.getParameter("numOfQuestions");
                    String durationTxt = request.getParameter("duration");

                    if (bankIDTxt == null || examName == null || numOfQuestionsTxt == null || durationTxt == null) {
                        throw new Exception("Null");
                    }

                    DateFormat formatter = new SimpleDateFormat("hh:mm dd/MM/yyyy");
                    Timestamp openDate = null,
                     closeDate = null;
                    int bankID,
                     numOfQuestions,
                     duration;
                    bankID = Integer.parseInt(bankIDTxt);
                    if (!openDateTxt.isEmpty()) {
                        openDate = new Timestamp(formatter.parse(openDateTxt).getTime());
                    }
                    if (!closeDateTxt.isEmpty()) {
                        closeDate = new Timestamp(formatter.parse(closeDateTxt).getTime());
                    }
                    numOfQuestions = Integer.parseInt(numOfQuestionsTxt);
                    duration = Integer.parseInt(durationTxt);

                    if (!DataAccessObject.createExam(bankID, user.getUserID(), examName, openDate, closeDate, numOfQuestions, duration)) {
                        throw new Exception("Failed");
                    }
                    break;
                case "remove":
                    String examCode = request.getParameter("examCode");
                    if (examCode == null) {
                        throw new Exception("Null");
                    }
                    if (!DataAccessObject.removeExam(examCode, user.getUserID())) {
                        throw new Exception("Failed");
                    }
                    break;
            }
            response.sendRedirect("manageExam");
        } catch (Exception ex) {
            request.setAttribute("msg", "Invalid request!");
            request.setAttribute("backURL", "manageExam");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            System.out.println(ex.getMessage());
        }
    }
}
