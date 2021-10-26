package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import utils.DataAccessObject;

@WebServlet(name = "ViewRecord", urlPatterns = {"/viewRecord"})
public class ViewRecordController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {     
        HttpSession session = request.getSession();   
        String examCode = request.getParameter("examCode");
        String recordIDTxt = request.getParameter("recordID");
        User user = (User) session.getAttribute("user");
        int recordID = Integer.parseInt(recordIDTxt); 
        System.out.println(user.getUserID());
        System.out.println(DataAccessObject.getQuestionsAndCorrectAnswer(examCode, user.getUserID()));
        System.out.println(DataAccessObject.getSelectedAnswers(recordID, user.getUserID()));
        request.setAttribute("questions", DataAccessObject.getQuestionsAndCorrectAnswer(examCode, user.getUserID()));
        request.setAttribute("selected", DataAccessObject.getSelectedAnswers(recordID, user.getUserID()));
        request.getRequestDispatcher("viewRecord.jsp").forward(request, response);
    }
}
