package utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import model.Role;

public class SecurityConfig {

    private static final Set<String> needAuthPages = new HashSet<String>(
            Arrays.asList(new String[]{"/", "/home", "/accountInfo", "/takeExam", "/manageAccount", "/manageBank", "/manageExam", "/createBank", "/viewBank", "/viewExam", "/viewRecord"})
    );
    private static final String[] prohibited = new String[]{"^.*\\.jsp$"};

    public static boolean prohibitedPatterns(String servletPath) {
        return Arrays.stream(prohibited).anyMatch(el -> servletPath.matches(el));
    }

    public static boolean needAuthentication(String servletPath) {
        return needAuthPages.contains(servletPath);
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
