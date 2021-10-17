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

    public static List<Test> getTest(String testCode) {
        List<Test> tests = new ArrayList<>();
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from testTbl tt \n"
                        + "inner join testDetailTbl tdt on tt.testCode = tdt.testCode \n"
                        + "inner join questionTbl qt on qt.questionID = tdt.questionID \n"
                        + "inner join answerTbl at2 on qt.questionID = at2.questionID\n"
                        + (testCode != null ? "where tt.testCode = ?\n" : "")
                        + "order by tt.testCode, qt.questionID";
                PreparedStatement stm = conn.prepareStatement(query);
                if (testCode != null) {
                    stm.setString(1, testCode);
                }
                ResultSet resset = stm.executeQuery();
                Test test = null;
                Question question = null;
                Answer answer = null;
                int maxChoose = 0;
                while (resset.next()) {
                    testCode = resset.getString("testCode");
                    int questionID = resset.getInt("questionID");
                    int answerID = resset.getInt("answerID");
                    if (test == null || !test.getTestCode().equals(testCode)) {
                        if (test != null) {
                            tests.add(test);
                        }
                        test = new Test(testCode, new ArrayList<>(), null, resset.getInt("duration"));
                        question = new Question(questionID, resset.getString("questionContent"), resset.getInt("mark"), 0, new ArrayList<>());
                        answer = new Answer(answerID, resset.getString("answerContent"));
                        maxChoose = 0;
                    }
                    if (question.getQuestionID() != questionID) {
                        question.setMaxChoose(maxChoose);
                        test.getQuestions().add(question);
                        question = new Question(questionID, resset.getString("questionContent"), resset.getInt("mark"), 0, new ArrayList<>());
                        maxChoose = 0;
                    }
                    answer = new Answer(answerID, resset.getString("answerContent"));
                    question.getAnswers().add(answer);
                    if (resset.getBoolean("isCorrect")) maxChoose++;
                }
                if (test == null) {
                    return tests;
                }
                question.setMaxChoose(maxChoose);
                test.getQuestions().add(question);
                tests.add(test);
                stm.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + "\n" + ex.getStackTrace());
        }
        return tests;
    }

    public static List<Bank> getBank(int bankID) {
        List<Bank> banks = new ArrayList<>();
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from bankTbl bt \n"
                        + "inner join questionTbl qt on bt.bankID = qt.bankID \n"
                        + "inner join answerTbl at2 on qt.questionID = at2.questionID \n"
                        + (bankID > 0 ? "where bt.bankID = ?\n" : "")
                        + "order by bt.bankID,qt.questionID";
                PreparedStatement stm = conn.prepareStatement(query);
                if (bankID > 0) {
                    stm.setInt(1, bankID);
                }
                ResultSet resset = stm.executeQuery();
                Bank bank = null;
                Question question = null;
                Answer answer = null;
                int maxChoose = 0;
                while (resset.next()) {
                    bankID = resset.getInt("bankID");
                    int questionID = resset.getInt("questionID");
                    int answerID = resset.getInt("answerID");
                    if (bank == null || bank.getBankID() != bankID) {
                        if (bank != null) {
                            banks.add(bank);
                        }
                        bank = new Bank(bankID, resset.getInt("creatorID"), new ArrayList<>(), resset.getDate("dateCreated"));
                        question = new Question(questionID, resset.getString("questionContent"), resset.getInt("mark"), 0, new ArrayList<>());
                        answer = new Answer(answerID, resset.getString("answerContent"));
                        maxChoose = 0;
                    }
                    if (question.getQuestionID() != questionID) {
                        question.setMaxChoose(maxChoose);
                        bank.getQuestions().add(question);
                        question = new Question(questionID, resset.getString("questionContent"), resset.getInt("mark"), 0, new ArrayList<>());
                        maxChoose = 0;
                    }
                    answer = new Answer(answerID, resset.getString("answerContent"));
                    question.getAnswers().add(answer);
                    if (resset.getBoolean("isCorrect")) maxChoose++;
                }
                if (bank == null) {
                    return banks;
                }
                question.setMaxChoose(maxChoose);
                bank.getQuestions().add(question);
                banks.add(bank);
                stm.close();
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + "\n" + ex.getStackTrace());
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

    public static int getRecordID(String testCode, int studentID) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select recordID from recordTbl where testCode = ? and studentID = ?";
                PreparedStatement stm = conn.prepareStatement(query);
                stm.setString(1, testCode);
                stm.setInt(2, studentID);
                ResultSet resset = stm.executeQuery();
                if (!resset.next()) {
                    return -1;
                }
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
                if (selectedCount == 0) {
                    return;
                }
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
            ex.printStackTrace();
        }
    }
}
