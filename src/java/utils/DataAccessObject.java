package utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.Answer;
import model.User;
import model.Bank;
import model.Question;
import model.Exam;
import model.Record;
import model.Role;
import org.apache.catalina.tribes.util.Arrays;

public class DataAccessObject {

    public static User login(String username, String password, String roleName) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from userTbl ut \n"
                        + "inner join roleTbl rt on ut.roleID = rt.roleID\n"
                        + "where ut.username like ? and rt.roleName like ?";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setString(1, username);
                    stm.setString(2, roleName);
                    ResultSet resset = stm.executeQuery();
                    if (!resset.next()) {
                        return null;  //Username is not existed      
                    }
                    int userID = resset.getInt("userID");
                    byte[] hashed = resset.getBytes("hashedPassword");
                    byte[] salt = resset.getBytes("salt");
                    byte[] computedHash = Crypto.computeHash(password, salt);
                    return Arrays.equals(hashed, computedHash)
                            ? new User(userID, username,
                                    resset.getString("fullname"),
                                    resset.getString("email"),
                                    new Role(resset.getInt("roleID"),
                                            resset.getString("roleName"),
                                            resset.getBoolean("canTakeExam"),
                                            resset.getBoolean("canManageAccount"),
                                            resset.getBoolean("canManageBank"),
                                            resset.getBoolean("canManageExam"))) : null;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public static boolean register(String username, String password, String fullname, String email, String roleName) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select username from userTbl where username like ?";
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

                query = "declare @roleID tinyint\n"
                        + "select @roleID = roleID from roleTbl rt \n"
                        + "where rt.roleName = ?\n"
                        + "insert into userTbl(username, fullname, email, roleID, hashedPassword, salt)\n"
                        + "values(?, ?, ?, @roleID, ?, ?)";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setString(1, roleName);
                    stm.setString(2, username);
                    stm.setString(3, fullname);
                    stm.setString(4, email);
                    stm.setBytes(5, hashed);
                    stm.setBytes(6, salt);
                    return stm.executeUpdate() > 0;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public static boolean updateAccount(int userID, String password, String fullname, String email, String roleName) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "declare @roleID tinyint\n"
                        + "select @roleID = roleID from roleTbl rt \n"
                        + "where rt.roleName = ?\n";
                if (password.isEmpty()) {
                    query += "update userTbl set fullname=?, email=?, roleID=@roleID\n";
                } else {
                    query += "update userTbl set fullname=?, email=?, roleID=@roleID, hashedPassword=?, salt=?\n";
                }
                query += "where userID=?";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setString(1, roleName);
                    stm.setString(2, fullname);
                    stm.setString(3, email);
                    if (password.isEmpty()) {
                        stm.setInt(4, userID);
                    } else {
                        byte[] salt = Crypto.getSalt();
                        byte[] hashed = Crypto.computeHash(password, salt);
                        stm.setBytes(4, hashed);
                        stm.setBytes(5, salt);
                        stm.setInt(6, userID);
                    }
                    return stm.executeUpdate() > 0;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }
    }

    public static List<User> getUsers() {
        List<User> result = new ArrayList<>();
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from userTbl ut \n"
                        + "inner join roleTbl rt on ut.roleID = rt.roleID\n";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    ResultSet resset = stm.executeQuery();
                    while (resset.next()) {
                        result.add(new User(resset.getInt("userID"), resset.getString("username"),
                                resset.getString("fullname"),
                                resset.getString("email"),
                                new Role(resset.getInt("roleID"),
                                        resset.getString("roleName"),
                                        resset.getBoolean("canTakeExam"),
                                        resset.getBoolean("canManageAccount"),
                                        resset.getBoolean("canManageBank"),
                                        resset.getBoolean("canManageExam"))));
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    public static List<Role> getRoles() {
        List<Role> result = new ArrayList<>();
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from roleTbl";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    ResultSet resset = stm.executeQuery();
                    while (resset.next()) {
                        result.add(new Role(resset.getInt("roleID"),
                                resset.getString("roleName"),
                                resset.getBoolean("canTakeExam"),
                                resset.getBoolean("canManageAccount"),
                                resset.getBoolean("canManageBank"),
                                resset.getBoolean("canManageExam")));
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return result;
    }

    public static boolean removeAccount(int userID) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "delete from userTbl where userID=?";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setInt(1, userID);
                    return stm.executeUpdate() > 0;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public static boolean removeBank(int bankID, int creatorID) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "delete from bankTbl where bankID = ? and creatorID = ?";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setInt(1, bankID);
                    stm.setInt(2, creatorID);
                    return stm.executeUpdate() > 0;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public static boolean removeExam(String examCode, int creatorID) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "delete from examTbl where examCode = ? and creatorID = ?\n"
                        + "delete from questionTbl\n"
                        + "where bankID is null and questionID not in \n"
                        + "(select questionID from examDetailTbl)";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setString(1, examCode);
                    stm.setInt(2, creatorID);
                    return stm.executeUpdate() > 0;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public static List<Record> getRecordsOfAExam(String examCode) {
        List<Record> records = new ArrayList<>();
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select rt.recordID, rt.takerID, ut.fullname, rt.examDate, rt.dateSubmitted, count(rdt.answerID) as numOfCorrectAnswers, count(at2.answerID) as numOfAnswers from recordTbl rt \n"
                        + "inner join userTbl ut on rt.takerID = ut.userID \n"
                        + "inner join examDetailTbl edt on rt.examCode = edt.examCode \n"
                        + "inner join answerTbl at2 on edt.questionID = at2.questionID \n"
                        + "left outer join recordDetailTbl rdt on at2.answerID = rdt.answerID\n"
                        + "where at2.isCorrect = 1 and rt.examCode = ?\n"
                        + "group by rt.recordID, rt.takerID, ut.fullname, rt.examDate, rt.dateSubmitted";

                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setString(1, examCode);
                    ResultSet resset = stm.executeQuery();
                    while (resset.next()) {
                        records.add(new Record(examCode, resset.getInt("recordID"), resset.getInt("takerID"), resset.getString("fullname"),
                                resset.getTimestamp("examDate"), resset.getTimestamp("dateSubmitted"),
                                resset.getInt("numOfCorrectAnswers"), resset.getInt("numOfAnswers")));
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage() + "\n" + ex.getStackTrace());
        }

        return records;
    }

    public static boolean removeRecord(int recordID, int creatorID) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "delete from recordTbl \n"
                        + "where recordID = ? and recordID in (select recordID from examTbl et \n"
                        + "inner join recordTbl rt on et.examCode = rt.examCode \n"
                        + "where et.creatorID = ?)";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setInt(1, recordID);
                    stm.setInt(2, creatorID);
                    return stm.executeUpdate() > 0;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public static List<Exam> getExam(String examCode, int creatorID) {
        List<Exam> exams = new ArrayList<>();
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from examTbl et \n"
                        + "inner join examDetailTbl edt on et.examCode = edt.examCode \n"
                        + "inner join questionTbl qt on qt.questionID = edt.questionID \n"
                        + "inner join answerTbl at2 on qt.questionID = at2.questionID\n"
                        + "inner join userTbl ut on et.creatorID = ut.userID\n";
                if (examCode != null && creatorID > 0) {
                    query += "where ut.userID = ? and et.examCode = ?\n";
                } else if (examCode != null) {
                    query += "where et.examCode = ?\n";
                } else if (creatorID > 0) {
                    query += "where ut.userID = ?\n";
                }
                query += "order by et.examCode, qt.questionID\n";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    if (examCode != null && creatorID > 0) {
                        stm.setInt(1, creatorID);
                        stm.setString(2, examCode);
                    } else if (examCode != null) {
                        stm.setString(1, examCode);
                    } else if (creatorID > 0) {
                        stm.setInt(1, creatorID);
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
                            exam = new Exam(examCode, new ArrayList<>(), resset.getTimestamp("openDate"), resset.getTimestamp("closeDate"), resset.getString("fullName"), null, resset.getInt("duration"));
                            question = new Question(questionID, resset.getString("questionContent"), 0, new ArrayList<>());
                            answer = new Answer(answerID, resset.getString("answerContent"));
                            maxChoose = 0;
                        }
                        if (question.getQuestionID() != questionID) {
                            question.setMaxChoose(maxChoose);
                            exam.getQuestions().add(question);
                            question = new Question(questionID, resset.getString("questionContent"), 0, new ArrayList<>());
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

    public static Set<Integer> getSelectedAnswers(int recordID, int creatorID) {
        Set<Integer> selected = new HashSet<>();
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from recordTbl rt \n"
                        + "inner join recordDetailTbl rdt on rt.recordID = rdt.recordID\n"
                        + "inner join examTbl et on rt.examCode = et.examCode \n"
                        + "where rt.recordID = ? and et.creatorID = ?";
                
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setInt(1, recordID);
                    stm.setInt(2, creatorID);
                    ResultSet resset = stm.executeQuery();
                    while (resset.next()) {
                        selected.add(resset.getInt("answerID"));
                    }
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return selected;
    }

    public static List<Question> getQuestionsAndCorrectAnswer(String examCode, int creatorID) {
        List<Question> questions = new ArrayList<>();
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from examTbl et \n"
                        + "inner join examDetailTbl edt on et.examCode = edt.examCode \n"
                        + "inner join questionTbl qt on qt.questionID = edt.questionID \n"
                        + "inner join answerTbl at2 on qt.questionID = at2.questionID\n"
                        + "inner join userTbl ut on et.creatorID = ut.userID\n"
                        + "where ut.userID = ? and et.examCode = ?\n"
                        + "order by et.examCode, qt.questionID\n";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setInt(1, creatorID);
                    stm.setString(2, examCode);
                    ResultSet resset = stm.executeQuery();
                    Question question = null;
                    Answer answer = null;
                    int maxChoose = 0;
                    while (resset.next()) {
                        int questionID = resset.getInt("questionID");
                        int answerID = resset.getInt("answerID");
                        if (question == null || question.getQuestionID() != questionID) {
                            if (question != null) {
                                question.setMaxChoose(maxChoose);
                                questions.add(question);
                            }
                            maxChoose = 0;
                            question = new Question(questionID, resset.getString("questionContent"), 0, new ArrayList<>());
                        }
                        answer = new Answer(answerID, resset.getString("answerContent"));
                        answer.setSelected(resset.getBoolean("isCorrect"));

                        if (answer.isSelected()) {
                            maxChoose++;
                        }
                        question.getAnswers().add(answer);
                    }
                    question.setMaxChoose(maxChoose);
                    questions.add(question);
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return questions;
    }

    public static List<Bank> getBank(int bankID) {
        List<Bank> banks = new ArrayList<>();
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from bankTbl bt\n"
                        + "inner join questionTbl qt on bt.bankID = qt.bankID\n"
                        + "inner join answerTbl at2 on qt.questionID = at2.questionID\n"
                        + "inner join userTbl ut on bt.creatorID = ut.userID\n"
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
                            bank = new Bank(bankID, resset.getString("bankName"), resset.getString("courseName"), resset.getInt("creatorID"), resset.getString("fullName"), new ArrayList<>(), resset.getTimestamp("dateCreated"));
                            question = new Question(questionID, resset.getString("questionContent"), 0, new ArrayList<>());
                            answer = new Answer(answerID, resset.getString("answerContent"));
                            maxChoose = 0;
                        }
                        if (question.getQuestionID() != questionID) {
                            question.setMaxChoose(maxChoose);
                            bank.getQuestions().add(question);
                            question = new Question(questionID, resset.getString("questionContent"), 0, new ArrayList<>());
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
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return banks;
    }

    public static boolean initRecord(String examCode, int takerID) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "select * from recordTbl where examCode = ? and takerID = ?";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setString(1, examCode);
                    stm.setInt(2, takerID);
                    ResultSet resset = stm.executeQuery();
                    if (resset.next()) {
                        return false; //Record is existed  
                    }
                }
                query = "insert into recordTbl(examCode, takerID, examDate) values (?, ?, getDate())";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setString(1, examCode);
                    stm.setInt(2, takerID);
                    return stm.executeUpdate() > 0;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean createExam(int bankID, int userID, String examName, Timestamp openDate, Timestamp closeDate, int numOfQuestions, int duration) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "insert into examTbl(examCode, examName, openDate, closeDate, creatorID, duration) values (?,?,?,?,?,?)\n"
                        + "insert into examDetailTbl \n"
                        + "select top " + numOfQuestions + " ?, qt.questionID \n"
                        + "from questionTbl qt \n"
                        + "where qt.bankID = ?\n"
                        + "order by rand()";
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    String examCode = Crypto.getRandomString(10);
                    stm.setString(1, examCode);
                    stm.setString(2, examName);
                    stm.setTimestamp(3, openDate);
                    stm.setTimestamp(4, closeDate);
                    stm.setInt(5, userID);
                    stm.setInt(6, duration);
                    stm.setString(7, examCode);
                    stm.setInt(8, bankID);
                    return stm.executeUpdate() > 0;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
        return false;
    }

    public static boolean saveRecord(String examCode, int takerID, Exam exam, int selectedCount) {
        try {
            try (Connection conn = DBContext.getConnection()) {
                String query = "update recordTbl \n"
                        + "set dateSubmitted = getdate()\n"
                        + "output inserted.recordID\n"
                        + "where examCode = ? and takerID = ? and dateSubmitted is null";
                int recordID;
                try (PreparedStatement stm = conn.prepareStatement(query)) {
                    stm.setString(1, examCode);
                    stm.setInt(2, takerID);
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
