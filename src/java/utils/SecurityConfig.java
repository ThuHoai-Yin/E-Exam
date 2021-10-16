package utils;

import java.util.Arrays;
import java.util.HashMap;

public class SecurityConfig {
    private static final String[] needAuthPages = new String[] {"/", "/home"};
    
    private static final HashMap<String, String> mapping = new HashMap<String, String>() {{
       put("/exam", "student"); 
    }};
    
    public static boolean needAuthentication(String servletPath) {
        return Arrays.stream(needAuthPages).anyMatch(el -> el.equals(servletPath));
    }
    
    public static boolean checkAuthorization(String servletPath, String role) {
        String requiredRole = mapping.get(servletPath);
        return requiredRole == null || requiredRole.equalsIgnoreCase(requiredRole);
    }
}
