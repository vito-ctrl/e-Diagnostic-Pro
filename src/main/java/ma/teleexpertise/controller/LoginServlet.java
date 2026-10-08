package ma.teleexpertise.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.teleexpertise.model.User;
import ma.teleexpertise.service.UserService;
import ma.teleexpertise.util.CSRFTokenUtil;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(true);
        User user = (User) session.getAttribute("user");
        if (user != null) {
            redirectByRole(request, response, user);
            return;
        }

        request.setAttribute("csrfToken", CSRFTokenUtil.getToken(session));
        request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Optional<User> userOpt = userService.authenticate(username, password);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            HttpSession session = request.getSession(true);
            session.setAttribute("user", user);
            CSRFTokenUtil.getToken(session); // Ensure CSRF token is set
            redirectByRole(request, response, user);
        } else {
            request.setAttribute("error", "Identifiants invalides ou compte inactif.");
            request.setAttribute("username", username);
            request.setAttribute("csrfToken", CSRFTokenUtil.getToken(request.getSession(true)));
            request.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(request, response);
        }
    }

    private void redirectByRole(HttpServletRequest request, HttpServletResponse response, User user)
            throws IOException {
        String ctx = request.getContextPath();
        switch (user.getRole()) {
            case INFIRMIER:
                response.sendRedirect(ctx + "/infirmier/patients");
                break;
            case GENERALISTE:
                response.sendRedirect(ctx + "/generaliste/file-attente");
                break;
            case SPECIALISTE:
                response.sendRedirect(ctx + "/specialiste/expertises");
                break;
            case ADMIN:
                response.sendRedirect(ctx + "/admin/staff");
                break;
            default:
                response.sendRedirect(ctx + "/login");
                break;
        }
    }
}
