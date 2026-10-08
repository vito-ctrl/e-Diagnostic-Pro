package ma.teleexpertise.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.teleexpertise.model.User;
import ma.teleexpertise.model.enums.Specialite;
import ma.teleexpertise.service.SpecialisteService;
import ma.teleexpertise.service.UserService;
import ma.teleexpertise.util.CSRFTokenUtil;

import java.io.IOException;
import java.util.Arrays;

@WebServlet("/specialiste/profil")
public class SpecialisteProfilServlet extends HttpServlet {

    private final SpecialisteService specialisteService = new SpecialisteService();
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        request.setAttribute("csrfToken", CSRFTokenUtil.getToken(session));

        User refreshedUser = userService.findById(currentUser.getId()).orElse(currentUser);
        session.setAttribute("user", refreshedUser);

        request.setAttribute("specialiste", refreshedUser);
        request.setAttribute("specialites", Arrays.asList(Specialite.values()));

        request.getRequestDispatcher("/WEB-INF/views/specialiste/profil.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");

        try {
            String specialiteStr = request.getParameter("specialite");
            String tarifStr = request.getParameter("tarif");

            Specialite specialite = (specialiteStr != null && !specialiteStr.isEmpty())
                    ? Specialite.valueOf(specialiteStr) : null;

            Double tarif = (tarifStr != null && !tarifStr.isEmpty())
                    ? Double.parseDouble(tarifStr.replace(',', '.')) : null;

            User updated = specialisteService.configurerProfil(currentUser.getId(), specialite, tarif);
            session.setAttribute("user", updated);

            response.sendRedirect(request.getContextPath() + "/specialiste/profil?success=1");
        } catch (Exception e) {
            request.setAttribute("error", "Erreur lors de la mise à jour du profil : " + e.getMessage());
            doGet(request, response);
        }
    }
}
