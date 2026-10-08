package ma.teleexpertise.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.teleexpertise.util.CSRFTokenUtil;

import java.io.IOException;

@WebFilter("/*")
public class CSRFFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // Ensure session has a CSRF token
        if (req.getSession(false) != null) {
            CSRFTokenUtil.getToken(req.getSession(true));
        }

        // Validate POST requests
        if ("POST".equalsIgnoreCase(req.getMethod())) {
            String path = req.getRequestURI().substring(req.getContextPath().length());

            // Exclude public login form submit if desired, or validate if token present
            if (!path.equals("/login")) {
                if (!CSRFTokenUtil.isValid(req)) {
                    res.sendError(HttpServletResponse.SC_FORBIDDEN, "Validation CSRF échouée : Jeton invalide ou manquant.");
                    return;
                }
            }
        }

        chain.doFilter(request, response);
    }
}
