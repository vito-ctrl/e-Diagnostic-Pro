package ma.teleexpertise.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.teleexpertise.model.Creneau;
import ma.teleexpertise.model.User;
import ma.teleexpertise.model.enums.StatutCreneau;
import ma.teleexpertise.service.SpecialisteService;
import ma.teleexpertise.util.CSRFTokenUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@WebServlet("/specialiste/creneaux")
public class SpecialisteCreneauxServlet extends HttpServlet {

    private final SpecialisteService specialisteService = new SpecialisteService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        request.setAttribute("csrfToken", CSRFTokenUtil.getToken(session));

        String dateParam = request.getParameter("date");
        LocalDate targetDate = (dateParam != null && !dateParam.isEmpty())
                ? LocalDate.parse(dateParam)
                : LocalDate.now();

        // Make sure slots exist for target date
        specialisteService.genererCreneauxJournee(currentUser.getId(), targetDate);

        List<Creneau> creneaux = specialisteService.getCreneauxParDate(currentUser.getId(), targetDate);

        request.setAttribute("creneaux", creneaux);
        request.setAttribute("selectedDate", targetDate.toString());

        request.getRequestDispatcher("/WEB-INF/views/specialiste/creneaux.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String creneauIdStr = request.getParameter("creneauId");
        String nouveauStatutStr = request.getParameter("statut");
        String dateParam = request.getParameter("date");

        if (creneauIdStr != null && nouveauStatutStr != null) {
            try {
                Long creneauId = Long.parseLong(creneauIdStr);
                StatutCreneau statut = StatutCreneau.valueOf(nouveauStatutStr);
                specialisteService.updateStatutCreneau(creneauId, statut);
            } catch (Exception ignored) {
            }
        }

        response.sendRedirect(request.getContextPath() + "/specialiste/creneaux?date=" + (dateParam != null ? dateParam : ""));
    }
}
