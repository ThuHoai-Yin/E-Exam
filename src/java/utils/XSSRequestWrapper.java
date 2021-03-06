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
        String[] unsafeValues = super.getParameterValues(parameter);
        return unsafeValues == null ? null : Arrays.stream(unsafeValues).map(unsafe -> Jsoup.clean(unsafe, Safelist.basicWithImages())).toArray(String[]::new);
    }

    @Override
    public String getParameter(String parameter) {
        String unsafe = super.getParameter(parameter);
        return unsafe == null ? null : Jsoup.clean(unsafe, Safelist.basic());
    }

    @Override
    public String getHeader(String name) {
        String unsafe = super.getHeader(name);
        return unsafe == null ? null : Jsoup.clean(unsafe, Safelist.basic());
    }
}
