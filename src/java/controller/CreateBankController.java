package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Answer;
import model.Bank;
import model.Question;
import model.User;
import org.apache.catalina.tribes.util.Arrays;
import utils.DBContext;
import utils.DataAccessObject;

@WebServlet(name = "CreateBank", urlPatterns = {"/createBank"})
public class CreateBankController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("createBank.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        String bankName = request.getParameter("bankName");
        String courseName = request.getParameter("courseName");

        try (Connection conn = DBContext.getConnection()) {
            String query = "insert into bankTbl(bankName, courseName, creatorID, dateCreated)\n"
                    + "output inserted.bankID\n"
                    + "values (?,?,?,getDate())";
            int bankID;
            try (PreparedStatement stm = conn.prepareStatement(query)) {
                stm.setString(1, bankName);
                stm.setString(2, courseName);
                stm.setInt(3, 1);
                ResultSet result = stm.executeQuery();
                if (!result.next()) {
                }
                bankID = result.getInt(1);
            }
            Map<String, String> questions = new HashMap<>();

            for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
                if (entry.getKey().startsWith("question")) {
                    String parentId = entry.getKey().substring(9);
                    questions.put(parentId, entry.getValue()[0]);
                }
            }
            query = "insert into questionTbl(bankID, questionContent)\n"
                    + "output inserted.questionId\n"
                    + "values ";
            for (int i = 1; i < questions.size(); i++) {
                query += "(?,?),";
            }
            query += "(?,?)";

            Map<String, Integer> questionIds = new HashMap<>();
            try (PreparedStatement stm = conn.prepareStatement(query)) {
                int counter = 1;
                for (Map.Entry<String, String> entry : questions.entrySet()) {
                    stm.setInt(counter++, bankID);
                    stm.setString(counter++, entry.getValue());
                }
                ResultSet result = stm.executeQuery();
                for (Map.Entry<String, String> entry : questions.entrySet()) {
                    result.next();
                    questionIds.put(entry.getKey(), result.getInt("questionId"));
                }
            }

            List<Pair<Integer, Pair<String, Boolean>>> answers = new ArrayList<Pair<Integer, Pair<String, Boolean>>>();

            for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
                if (entry.getKey().startsWith("answer")) {
                    String str = entry.getKey().substring(7);
                    String parentId = str.substring(0, 4);
                    answers.add(new Pair(questionIds.get(parentId),
                            new Pair(entry.getValue()[0], Objects.deepEquals(request.getParameter("correct." + str), "on"))));
                }
            }

            query = "insert into answerTbl(questionID, answerContent, isCorrect)\n"
                    + "values ";
            for (int i = 1; i < answers.size(); i++) {
                query += "(?,?,?),";
            }
            query += "(?,?,?)";
            
            
            try (PreparedStatement stm = conn.prepareStatement(query)) {
                int counter = 1;
                for (Pair<Integer, Pair<String, Boolean>> entry : answers) {
                    stm.setInt(counter++, entry.getKey());
                    stm.setString(counter++, entry.getValue().getKey());
                    stm.setBoolean(counter++, entry.getValue().getValue());
                }
                int result = stm.executeUpdate();
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + Arrays.toString(ex.getStackTrace()));
        }
        response.sendRedirect("manageBank");
    }
}
