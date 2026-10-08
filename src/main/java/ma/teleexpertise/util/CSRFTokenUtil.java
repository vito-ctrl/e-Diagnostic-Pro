package ma.teleexpertise.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.UUID;

public class CSRFTokenUtil {

    public static final String CSRF_SESSION_ATTR = "csrfToken";
    public static final String CSRF_PARAM_NAME = "csrfToken";

    private CSRFTokenUtil() {
    }

    public static String getToken(HttpSession session) {
        if (session == null) {
            return null;
        }
        String token = (String) session.getAttribute(CSRF_SESSION_ATTR);
        if (token == null) {
            token = UUID.randomUUID().toString();
            session.setAttribute(CSRF_SESSION_ATTR, token);
        }
        return token;
    }

    public static boolean isValid(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return false;
        }
        String sessionToken = (String) session.getAttribute(CSRF_SESSION_ATTR);
        if (sessionToken == null) {
            return false;
        }
        String paramToken = request.getParameter(CSRF_PARAM_NAME);
        if (paramToken == null) {
            paramToken = request.getHeader("X-CSRF-TOKEN");
        }
        return sessionToken.equals(paramToken);
    }
}
