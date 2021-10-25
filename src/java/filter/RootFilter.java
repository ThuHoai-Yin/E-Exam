package filter;

import java.io.IOException;
import java.sql.Timestamp;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.Exam;
import model.User;
import utils.SecurityConfig;
import utils.XSSRequestWrapper;

public class RootFilter implements Filter {

    public static final int BLOCK_TIME = 5 * 60 * 1000;

    public void doFilter(ServletRequest req, ServletResponse resp,
            FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        HttpSession session = request.getSession();

        request.setCharacterEncoding("UTF-8");
        String servletPath = request.getServletPath();

        if (SecurityConfig.prohibitedPatterns(servletPath)) {
            response.sendError(404);
            return;
        }

        Timestamp current = new Timestamp(System.currentTimeMillis());
        Exam exam = (Exam) session.getAttribute("exam");

        boolean over = exam == null || exam.getExamEndTime().before(current);

        if (!servletPath.equals("/takeExam") && !over) {
            response.sendRedirect("takeExam");
            return;
        }

        switch (servletPath) {
            case "/ping":
                response.setStatus(200);
                return;
            case "/admin":
                Object obj = request.getServletContext().getAttribute("loginFailureTimes");
                int loginFailureTimes = obj != null ? (Integer) obj : 0;
                if (loginFailureTimes == 3) {
                    request.getServletContext().setAttribute("recentLoginFailure", new Timestamp(System.currentTimeMillis()));
                    request.getServletContext().setAttribute("loginFailureTimes", 0);
                }
                Timestamp recentLoginFailure = (Timestamp) request.getServletContext().getAttribute("recentLoginFailure");
                if (recentLoginFailure != null && System.currentTimeMillis() - recentLoginFailure.getTime() < BLOCK_TIME) {
                    if (request.getMethod().equalsIgnoreCase("get")) {
                        response.sendError(418);
                    } else {
                        response.sendRedirect("admin");
                    }
                    return;
                }
                break;
        }
        User user = (User) session.getAttribute("user");
        if (user == null) {
            if (SecurityConfig.needAuthentication(servletPath)) {
                if (!servletPath.equals("/login")) {
                    response.sendRedirect("login");
                    return;
                }
            }
        } else {
            switch (servletPath) {
                case "/admin":
                case "/login":
                    response.sendRedirect("home");
                    return;
                case "/logout":
                    request.getSession().invalidate();
                    response.sendRedirect("login");
                    return;
            }
            if (!SecurityConfig.checkAuthorization(servletPath, user.getRole())) {
                response.sendError(403);
                return;
            }
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
