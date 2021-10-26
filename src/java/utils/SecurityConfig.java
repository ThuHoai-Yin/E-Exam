package utils;

import java.util.Arrays;
import model.Role;

public class SecurityConfig {

    private static final String[] needAuthPages = new String[]{"/", "/home", "/takeExam", "/manageAccount", "/manageBank", "/manageExam", "/createBank", "/viewExam", "/viewRecord"};
    private static final String[] prohibited = new String[]{"^.*\\.jsp$"};

    public static boolean prohibitedPatterns(String servletPath) {
        return Arrays.stream(prohibited).anyMatch(el -> servletPath.matches(el));
    }

    public static boolean needAuthentication(String servletPath) {
        return Arrays.stream(needAuthPages).anyMatch(el -> el.equals(servletPath));
    }

    public static boolean checkAuthorization(String servletPath, Role role) {
        switch (servletPath) {
            case "/takeExam":
                return role.canTakeExam();
            case "/manageAccount":
                return role.canManageAccount();
            case "/manageBank":
            case "/createBank":
                return role.canManageBank();
            case "/manageExam":
            case "/viewExam":
            case "/viewRecord":
                return role.canManageExam();
            default:
                return true;
        }
    }
}
