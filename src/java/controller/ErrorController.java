package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "Error", urlPatterns = {"/error"})
public class ErrorController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int statusCode = (Integer) request.getAttribute("javax.servlet.error.status_code");
        switch (statusCode) {
            case 403:                
                request.setAttribute("code", "403");
                request.setAttribute("msg", "Forbidden");
                request.setAttribute("detail", "Your role cannot access this content");
                request.getRequestDispatcher("httperror.jsp").forward(request, response);
                break;
            case 404:
                request.setAttribute("code", "404");
                request.setAttribute("msg", "Not Found");
                request.setAttribute("detail", "Please check URL in address bar and try again");
                request.getRequestDispatcher("httperror.jsp").forward(request, response);
                break;
            case 418:
                request.setAttribute("code", "418");
                request.setAttribute("msg", "I'm a teapot");
                request.setAttribute("detail", ":)");
                request.getRequestDispatcher("httperror.jsp").forward(request, response);
                break;
        }
    }
}
