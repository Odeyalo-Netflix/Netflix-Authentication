package com.odeyalo.analog.auth.service.support;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class HttpServletRequestUtils {

    public static HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
    }

    public static String getIPAddress() {
        return getCurrentRequest().getRemoteAddr();
    }


    public static String getBrowserName() {
        UserAgent userAgent = UserAgent.parseUserAgentString(getCurrentRequest().getHeader("User-Agent"));
        Browser browser = userAgent.getBrowser();
        return browser.getName();
    }

    public static String getOperatingSystemName() {
        UserAgent userAgent = UserAgent.parseUserAgentString(getCurrentRequest().getHeader("User-Agent"));
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        return operatingSystem.getName();
    }
}
