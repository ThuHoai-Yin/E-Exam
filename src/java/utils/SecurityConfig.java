package utils;

import java.util.Arrays;
import java.util.HashMap;

public class SecurityConfig {
    private static final String[] needAuthPages = new String[] {"/", "/home"};
    
    private static final HashMap<String, String> roles = new HashMap<String, String>() {{
       put("/exam", "student"); 
    }};
    
    private static final String[] prohibited = new String[] { "^.*\\.jsp$" };
    
    public static boolean prohibitedPatterns(String servletPath) {
        return Arrays.stream(prohibited).anyMatch(el -> servletPath.matches(el));        
    }
    
    public static boolean needAuthentication(String servletPath) {
        return Arrays.stream(needAuthPages).anyMatch(el -> el.equals(servletPath));
    }
    
    public static boolean checkAuthorization(String servletPath, String role) {
        String requiredRole = roles.get(servletPath);
        return requiredRole == null || requiredRole.equalsIgnoreCase(role);
    }
}
