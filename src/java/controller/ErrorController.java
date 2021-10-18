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
                request.setAttribute("Code", "403");
                request.setAttribute("Msg", "Forbidden");
                request.setAttribute("Detail", "Your role cannot access this content");
                request.getRequestDispatcher("httperror.jsp").forward(request, response);
                break;
            case 404:
                request.setAttribute("Code", "404");
                request.setAttribute("Msg", "Not Found");
                request.setAttribute("Detail", "Please check URL in address bar and try again");
                request.getRequestDispatcher("httperror.jsp").forward(request, response);
                break;
        }
    }
}
