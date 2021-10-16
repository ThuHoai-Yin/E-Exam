package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import model.Answer;
import model.Authentication;
import model.Bank;
import model.Question;
import model.Test;
import org.apache.catalina.tribes.util.Arrays;

public class DataAccessObject {

    public static Authentication login(String username, String password, String role) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from userTbl where username like ? and userRole like ?";
                PreparedStatement stm = conn.prepareStatement(query);
                stm.setString(1, username);
                stm.setString(2, role);
                ResultSet resset = stm.executeQuery();
                if (!resset.next()) {
                    return null;  //Username is not existed      
                }
                int id = resset.getInt("userID");
                byte[] hashed = resset.getBytes("hashedPassword");
                byte[] salt = resset.getBytes("salt");

                stm.close();

                byte[] computedHash = Crypto.computeHash(password, salt);
                return Arrays.equals(hashed, computedHash) ? new Authentication(id, username, role) : null;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public static boolean register(String username, String password, String role) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from userTbl where username like ?";
                PreparedStatement stm = conn.prepareStatement(query);
                stm.setString(1, username);
                ResultSet resset = stm.executeQuery();
                if (resset.next()) {
                    return false; //Username is existed  
                }
                byte[] salt = Crypto.getSalt();
                byte[] hashed = Crypto.computeHash(password, salt);

                stm.close();

                query = "insert into userTbl(username, userRole, hashedPassword, salt) values (?, ?, ?, ?)";
                stm = conn.prepareStatement(query);
                stm.setString(1, username);
                stm.setString(2, role);
                stm.setBytes(3, hashed);
                stm.setBytes(4, salt);
                boolean res = stm.executeUpdate() > 0;

                stm.close();

                return res;
            }
        } catch (SQLException ex) {
            return false;
        }
    }

    public static String getUserFullName(int userID) {
        try {

            try (Connection conn = DBContext.getConnection()) {
                String query = "select fullname from userDetailTbl where userID = ?";
                PreparedStatement stm = conn.prepareStatement(query);
                stm.setInt(1, userID);
                ResultSet resset = stm.executeQuery();
                if (!resset.next()) {
                    return null;
                }
                String res = resset.getString(1);

                stm.close();
                return res;
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + " " + userID);
            return null;
        }
    }

    public static Test getTest(String testCode) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from testTbl where testCode = ?";
                PreparedStatement stm = conn.prepareStatement(query);
                stm.setString(1, testCode);
                ResultSet resset = stm.executeQuery();
                if (resset.next()) {
                    List<Question> questions = getTestQuestions(testCode);
                    return new Test(testCode, questions, null, resset.getInt("duration"));
                }

                stm.close();
            }
        } catch (SQLException ex) {
        }
        return null;
    }

    public static List<Bank> getBanks() {
        List<Bank> banks = new ArrayList<>();
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from bankTbl";
                PreparedStatement stm = conn.prepareStatement(query);
                ResultSet resset = stm.executeQuery();
                while (resset.next()) {
                    int bankID = resset.getInt("bankID");
                    List<Question> questions = getBankQuestions(bankID);
                    banks.add(new Bank(bankID, questions, resset.getDate("dateCreated")));
                }

                stm.close();
            }
        } catch (SQLException ex) {
        }
        return banks;
    }

    public static boolean createRecord(String testCode, int studentID) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from recordTbl where testCode = ? and studentID = ?";
                PreparedStatement stm = conn.prepareStatement(query);
                stm.setString(1, testCode);
                stm.setInt(2, studentID);
                ResultSet resset = stm.executeQuery();
                if (resset.next()) {

                    return false; //Record is existed  
                }
                stm.close();

                query = "insert into recordTbl(testCode, studentID, examDate) values (?, ?, ?)";
                stm = conn.prepareStatement(query);
                stm.setString(1, testCode);
                stm.setInt(2, studentID);
                stm.setObject(3, new Timestamp(System.currentTimeMillis()));
                boolean res = stm.executeUpdate() > 0;

                stm.close();

                return res;
            }
        } catch (SQLException ex) {
            return false;
        }
    }

    public static List<Question> getTestQuestions(String testCode) {
        List<Question> questions = new ArrayList<>();
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select a.questionID, b.questionContent, b.mark from testDetailTbl a\n"
                        + "inner join questionTbl b on a.questionID = b.questionID \n"
                        + "where a.testCode = ?";
                PreparedStatement stm = conn.prepareStatement(query);
                stm.setString(1, testCode);
                ResultSet resset = stm.executeQuery();
                while (resset.next()) {
                    int questionID = resset.getInt("questionID");
                    List<Answer> answers = getAnswers(questionID);
                    questions.add(new Question(questionID, resset.getString("questionContent"), resset.getInt("mark"), answers));
                }

                stm.close();
            }
        } catch (SQLException ex) {
        }
        return questions;
    }

    public static List<Question> getBankQuestions(int bankID) {
        List<Question> questions = new ArrayList<>();
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from questionTbl where bankID = ?";
                PreparedStatement stm = conn.prepareStatement(query);
                stm.setInt(1, bankID);
                ResultSet resset = stm.executeQuery();
                while (resset.next()) {
                    int questionID = resset.getInt("questionID");
                    List<Answer> answers = getAnswers(questionID);
                    questions.add(new Question(questionID, resset.getString("questionContent"), resset.getInt("mark"), answers));
                }

                stm.close();
            }
        } catch (SQLException ex) {
        }
        return questions;
    }

    public static List<Answer> getAnswers(int questionID) {
        List<Answer> answers = new ArrayList<>();
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from answerTbl where questionID = ?";
                PreparedStatement stm = conn.prepareStatement(query);
                stm.setInt(1, questionID);
                ResultSet resset = stm.executeQuery();
                while (resset.next()) {
                    answers.add(new Answer(resset.getInt("answerID"), resset.getString("answerContent")));
                }

                stm.close();
            }
        } catch (SQLException ex) {
        }
        return answers;
    }
    
     public static int getRecordID(String testCode, int studentID) {
        try {
            try (Connection conn = DBContext.getConnection()) {
               String query = "select recordID from recordTbl where testCode = ? and studentID = ?";
                PreparedStatement stm = conn.prepareStatement(query);
                stm.setString(1, testCode);
                stm.setInt(2, studentID);
                ResultSet resset = stm.executeQuery();
                if (!resset.next()) return -1;
                int res = resset.getInt(1); 
                stm.close();
                return res;
            }
        } catch (SQLException ex) {
        }
        return -1;
    }    

    public static void saveRecord(int recordID, Test test, int selectedCount) {
        try {
            try (Connection conn = DBContext.getConnection()) {

                String query = "insert into recordDetailTbl(recordID, answerID) values ";
                for (int i = 1; i < selectedCount; i++) {
                    query += "(?,?),";
                }
                query += "(?,?)";
                PreparedStatement stm = conn.prepareStatement(query);

                int counter = 1;
                for (Question q : test.getQuestions()) {
                    for (Answer a : q.getAnswers()) {
                        if (a.isSelected()) {
                            stm.setInt(counter, recordID);
                            stm.setInt(counter + 1, a.getAnswerID());
                            counter += 2;
                        }
                    }
                }
                stm.executeUpdate();
                stm.close();

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
}
