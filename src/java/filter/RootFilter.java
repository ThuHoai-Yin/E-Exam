package filter;

import java.io.IOException;
import java.util.Arrays;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Authentication;
import utils.DataAccessObject;
import utils.Jwt;
import utils.SecurityConfig;
import utils.XSSRequestWrapper;

public class RootFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse resp,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();

        String servletPath = request.getServletPath();

        if (SecurityConfig.prohibitedPatterns(servletPath)) {
            response.sendError(404);
            return;
        }

        Cookie cookie = null;
        if (request.getCookies() != null) {
            cookie = Arrays.stream(request.getCookies()).filter((e) -> e.getName().equals("jwt")).findFirst().orElse(null);
        }
        Authentication auth = Jwt.validateToken(cookie);
        System.out.println(servletPath);
        if (auth != null) {
            if (servletPath.equals("/login")) {
                response.sendRedirect("home");
            }
            if (session.getAttribute("UserFullName") == null) {
                session.setAttribute("UserFullName", DataAccessObject.getUserFullName(auth.getID()));
            }
            if (session.getAttribute("Auth") == null) {
                session.setAttribute("Auth", auth);
            }
            if (!SecurityConfig.checkAuthorization(servletPath, auth.getRole())) {
                response.sendError(403);
                return;
            }
        } else if (auth == null && SecurityConfig.needAuthentication(servletPath)) {
            response.sendRedirect("login");
        } else {

        }
        
        chain.doFilter(new XSSRequestWrapper(request), response);
    }
    
    

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
