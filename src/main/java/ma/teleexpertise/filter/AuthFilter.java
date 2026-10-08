package ma.teleexpertise.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.teleexpertise.model.User;
import ma.teleexpertise.model.enums.Role;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = uri.substring(contextPath.length());

        // Allow static resources and public endpoints
        if (path.startsWith("/assets/") || path.startsWith("/css/") || path.startsWith("/js/")
                || path.equals("/login") || path.equals("/") || path.equals("/index.jsp")) {
            chain.doFilter(request, response);
            return;
        }

        HttpSession session = req.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("user") : null;

        if (user == null) {
            res.sendRedirect(contextPath + "/login");
            return;
        }

        // Role-based access control
        Role role = user.getRole();

        if (path.startsWith("/infirmier") && role != Role.INFIRMIER && role != Role.ADMIN) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé au personnel infirmier.");
            return;
        }

        if (path.startsWith("/generaliste") && role != Role.GENERALISTE && role != Role.ADMIN) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé aux médecins généralistes.");
            return;
        }

        if (path.startsWith("/specialiste") && role != Role.SPECIALISTE && role != Role.ADMIN) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé aux médecins spécialistes.");
            return;
        }

        if (path.startsWith("/admin") && role != Role.ADMIN) {
            res.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé aux administrateurs.");
            return;
        }

        chain.doFilter(request, response);
    }
}
