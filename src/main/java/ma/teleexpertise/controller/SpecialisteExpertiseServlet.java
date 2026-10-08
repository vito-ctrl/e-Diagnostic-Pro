package ma.teleexpertise.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.teleexpertise.model.DemandeExpertise;
import ma.teleexpertise.model.User;
import ma.teleexpertise.model.enums.PrioriteExpertise;
import ma.teleexpertise.model.enums.StatutExpertise;
import ma.teleexpertise.service.SpecialisteService;
import ma.teleexpertise.util.CSRFTokenUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {
    "/specialiste/expertises",
    "/specialiste/repondre"
})
public class SpecialisteExpertiseServlet extends HttpServlet {

    private final SpecialisteService specialisteService = new SpecialisteService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        request.setAttribute("csrfToken", CSRFTokenUtil.getToken(session));

        String path = request.getServletPath();

        if ("/specialiste/repondre".equals(path)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                try {
                    Long id = Long.parseLong(idStr);
                    Optional<DemandeExpertise> demandeOpt = specialisteService.getDemandeById(id);
                    if (demandeOpt.isPresent()) {
                        request.setAttribute("demande", demandeOpt.get());
                        request.getRequestDispatcher("/WEB-INF/views/specialiste/repondre.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
            response.sendRedirect(request.getContextPath() + "/specialiste/expertises");
            return;
        }

        // US7 : Consulter les demandes d'expertise
        String statutParam = request.getParameter("statut");
        String prioriteParam = request.getParameter("priorite");

        StatutExpertise filtreStatut = null;
        if (statutParam != null && !statutParam.isEmpty()) {
            try {
                filtreStatut = StatutExpertise.valueOf(statutParam);
            } catch (IllegalArgumentException ignored) {
            }
        }

        PrioriteExpertise filtrePriorite = null;
        if (prioriteParam != null && !prioriteParam.isEmpty()) {
            try {
                filtrePriorite = PrioriteExpertise.valueOf(prioriteParam);
            } catch (IllegalArgumentException ignored) {
            }
        }

        // Filtered via Stream API in service
        List<DemandeExpertise> demandes = specialisteService.getDemandesExpertise(currentUser.getId(), filtreStatut, filtrePriorite);

        request.setAttribute("demandes", demandes);
        request.setAttribute("statuts", Arrays.asList(StatutExpertise.values()));
        request.setAttribute("priorites", Arrays.asList(PrioriteExpertise.values()));
        request.setAttribute("selectedStatut", filtreStatut);
        request.setAttribute("selectedPriorite", filtrePriorite);

        request.getRequestDispatcher("/WEB-INF/views/specialiste/expertises.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();

        if ("/specialiste/repondre".equals(path)) {
            try {
                Long demandeId = Long.parseLong(request.getParameter("demandeId"));
                String avisMedical = request.getParameter("avisMedical");
                String recommandations = request.getParameter("recommandations");

                specialisteService.repondreExpertise(demandeId, avisMedical, recommandations);

                response.sendRedirect(request.getContextPath() + "/specialiste/expertises?success=repondu");
                return;
            } catch (Exception e) {
                request.setAttribute("error", "Erreur lors de la soumission de l'avis : " + e.getMessage());
                doGet(request, response);
                return;
            }
        }

        response.sendRedirect(request.getContextPath() + "/specialiste/expertises");
    }
}
