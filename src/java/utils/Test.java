package utils;

import model.User;
import org.apache.tomcat.util.codec.binary.Base64;

public class Test {

    public static void main(String[] args) throws Exception {
        System.out.println(DataAccessObject.createExam(17, 1, "JP Progress Test", null, null, 1, 10));
    }
}