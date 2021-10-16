package utils;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class XSSRequestWrapper extends HttpServletRequestWrapper {

    public XSSRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }        
        return Arrays.stream(values).map(unsafe -> Jsoup.clean(unsafe, Safelist.basicWithImages())).toArray(String[]::new);
    }

    @Override
    public String getParameter(String parameter) {
        String unsafe = super.getParameter(parameter);
        return unsafe == null ? null : Jsoup.clean(unsafe, Safelist.basicWithImages());
    }

    @Override
    public String getHeader(String name) {
        String unsafe = super.getHeader(name);  
        return unsafe == null ? null : Jsoup.clean(unsafe, Safelist.basicWithImages());
    }
}
