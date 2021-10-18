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
import model.Exam;
import org.apache.catalina.tribes.util.Arrays;

public class DataAccessObject {

    public static Authentication login(String username, String password, String role) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from userTbl where username like ? and userRole like ?";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setString(1, username);
                    stm.setString(2, role);
                    ResultSet resset = stm.executeQuery();
                    if (!resset.next()) {
                        return null;  //Username is not existed      
                    }
                    int id = resset.getInt("userID");
                    byte[] hashed = resset.getBytes("hashedPassword");
                    byte[] salt = resset.getBytes("salt");
                    byte[] computedHash = Crypto.computeHash(password, salt);
                    return Arrays.equals(hashed, computedHash) ? new Authentication(id, username, role) : null;
                }
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
                byte[] salt;
                byte[] hashed;
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setString(1, username);
                    ResultSet resset = stm.executeQuery();
                    if (resset.next()) {
                        return false; //Username is existed  
                    }
                    salt = Crypto.getSalt();
                    hashed = Crypto.computeHash(password, salt);
                }

                query = "insert into userTbl(username, userRole, hashedPassword, salt) values (?, ?, ?, ?)";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setString(1, username);
                    stm.setString(2, role);
                    stm.setBytes(3, hashed);
                    stm.setBytes(4, salt);
                    return stm.executeUpdate() > 0;
                }
            }
        } catch (SQLException ex) {
            return false;
        }
    }

    public static String getUserFullName(int userID) {
        try {

            try (Connection conn = DBContext.getConnection()) {
                String query = "select fullname from userDetailTbl where userID = ?";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setInt(1, userID);
                    ResultSet resset = stm.executeQuery();
                    if (!resset.next()) {
                        return "Unname";
                    }
                    return resset.getString(1);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + " " + userID);
            return null;
        }
    }

    public static List<Exam> getExam(String examCode) {
        List<Exam> exams = new ArrayList<>();
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from examTbl et \n"
                        + "inner join examDetailTbl edt on et.examCode = edt.examCode \n"
                        + "inner join questionTbl qt on qt.questionID = edt.questionID \n"
                        + "inner join answerTbl at2 on qt.questionID = at2.questionID\n"
                        + (examCode != null ? "where et.examCode = ?\n" : "")
                        + "order by et.examCode, qt.questionID";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    if (examCode != null) {
                        stm.setString(1, examCode);
                    }
                    ResultSet resset = stm.executeQuery();
                    Exam exam = null;
                    Question question = null;
                    Answer answer = null;
                    int maxChoose = 0;
                    while (resset.next()) {
                        examCode = resset.getString("examCode");
                        int questionID = resset.getInt("questionID");
                        int answerID = resset.getInt("answerID");
                        if (exam == null || !exam.getExamCode().equals(examCode)) {
                            if (exam != null) {
                                exams.add(exam);
                            }
                            exam = new Exam(examCode, new ArrayList<>(), null, resset.getInt("duration"));
                            question = new Question(questionID, resset.getString("questionContent"), resset.getInt("mark"), 0, new ArrayList<>());
                            answer = new Answer(answerID, resset.getString("answerContent"));
                            maxChoose = 0;
                        }
                        if (question.getQuestionID() != questionID) {
                            question.setMaxChoose(maxChoose);
                            exam.getQuestions().add(question);
                            question = new Question(questionID, resset.getString("questionContent"), resset.getInt("mark"), 0, new ArrayList<>());
                            maxChoose = 0;
                        }
                        answer = new Answer(answerID, resset.getString("answerContent"));
                        question.getAnswers().add(answer);
                        if (resset.getBoolean("isCorrect")) {
                            maxChoose++;
                        }
                    }
                    if (exam == null) {
                        return exams;
                    }
                    question.setMaxChoose(maxChoose);
                    exam.getQuestions().add(question);
                    exams.add(exam);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + "\n" + ex.getStackTrace());
        }
        return exams;
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
                try (PreparedStatement stm = conn.prepareStatement(query)) {
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
                        if (resset.getBoolean("isCorrect")) {
                            maxChoose++;
                        }
                    }
                    if (bank == null) {
                        return banks;
                    }
                    question.setMaxChoose(maxChoose);
                    bank.getQuestions().add(question);
                    banks.add(bank);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + "\n" + ex.getStackTrace());
        }
        return banks;
    }

    public static boolean initRecord(String examCode, int studentID) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from recordTbl where examCode = ? and studentID = ?";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setString(1, examCode);
                    stm.setInt(2, studentID);
                    ResultSet resset = stm.executeQuery();
                    if (resset.next()) {
                        return false; //Record is existed  
                    }
                }
                query = "insert into recordTbl(examCode, studentID, examDate) values (?, ?, getDate())";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setString(1, examCode);
                    stm.setInt(2, studentID);
                    return stm.executeUpdate() > 0;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public static boolean saveRecord(String examCode, int studentID, Exam exam, int selectedCount) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "update recordTbl \n"
                        + "set dateSummited = getdate()\n"
                        + "output inserted.recordID\n"
                        + "where examCode = ? and studentID = ? and dateSummited is null";
                int recordID;
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setString(1, examCode);
                    stm.setInt(2, studentID);
                    ResultSet resset = stm.executeQuery();
                    if (!resset.next()) {
                        stm.close();
                        return false;
                    }

                    recordID = resset.getInt(1);
                }

                if (selectedCount == 0) {
                    return true;
                }
                query = "insert into recordDetailTbl(recordID, answerID) values ";
                for (int i = 1; i < selectedCount; i++) {
                    query += "(?,?),";
                }
                query += "(?,?)";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    int counter = 1;
                    for (Question q : exam.getQuestions()) {
                        for (Answer a : q.getAnswers()) {
                            if (a.isSelected()) {
                                stm.setInt(counter, recordID);
                                stm.setInt(counter + 1, a.getAnswerID());
                                counter += 2;
                            }
                        }
                    };
                    return stm.executeUpdate() > 0;
                }

            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }
}
