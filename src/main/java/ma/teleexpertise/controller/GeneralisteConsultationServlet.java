package ma.teleexpertise.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ma.teleexpertise.model.*;
import ma.teleexpertise.model.enums.*;
import ma.teleexpertise.service.ConsultationService;
import ma.teleexpertise.service.PatientService;
import ma.teleexpertise.service.SpecialisteService;
import ma.teleexpertise.util.CSRFTokenUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {
    "/generaliste/file-attente",
    "/generaliste/consultations",
    "/generaliste/nouvelle-consultation",
    "/generaliste/cloturer-directe",
    "/generaliste/demander-expertise"
})
public class GeneralisteConsultationServlet extends HttpServlet {

    private final ConsultationService consultationService = new ConsultationService();
    private final PatientService patientService = new PatientService();
    private final SpecialisteService specialisteService = new SpecialisteService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute("user");
        request.setAttribute("csrfToken", CSRFTokenUtil.getToken(session));

        if ("/generaliste/consultations".equals(path)) {
            List<Consultation> consultations = consultationService.findByGeneraliste(currentUser.getId());
            request.setAttribute("consultations", consultations);
            request.getRequestDispatcher("/WEB-INF/views/generaliste/consultations.jsp").forward(request, response);
            return;
        }

        if ("/generaliste/nouvelle-consultation".equals(path)) {
            String fileIdStr = request.getParameter("fileId");
            if (fileIdStr != null) {
                try {
                    Long fileId = Long.parseLong(fileIdStr);
                    Optional<FileAttente> fileItemOpt = patientService.getFileItem(fileId);
                    if (fileItemOpt.isPresent()) {
                        FileAttente fileItem = fileItemOpt.get();
                        request.setAttribute("fileItem", fileItem);
                        request.setAttribute("patient", fileItem.getPatient());
                        request.setAttribute("signesVitaux", fileItem.getSignesVitaux());
                    }
                } catch (NumberFormatException ignored) {
                }
            }

            // Provide technical acts list
            request.setAttribute("actesTechniques", consultationService.getAllActesTechniques());

            // Provide specialties list
            request.setAttribute("specialites", Arrays.asList(Specialite.values()));

            // Filter specialists if specialty selected
            String specialiteParam = request.getParameter("specialite");
            Specialite selectedSpecialite = null;
            if (specialiteParam != null && !specialiteParam.isEmpty()) {
                try {
                    selectedSpecialite = Specialite.valueOf(specialiteParam);
                } catch (IllegalArgumentException ignored) {
                }
            }

            // Stream API filtered & sorted specialists
            List<User> specialistes = consultationService.getSpecialistesFiltresEtTries(selectedSpecialite, null);
            request.setAttribute("selectedSpecialite", selectedSpecialite);
            request.setAttribute("specialistes", specialistes);

            // If a specialist is selected, load available time slots
            String specIdParam = request.getParameter("specialisteId");
            if (specIdParam != null && !specIdParam.isEmpty()) {
                try {
                    Long specId = Long.parseLong(specIdParam);
                    List<Creneau> creneaux = specialisteService.getCreneauxDisponibles(specId);
                    request.setAttribute("creneauxDisponibles", creneaux);
                    request.setAttribute("selectedSpecialisteId", specId);
                } catch (NumberFormatException ignored) {
                }
            }

            request.getRequestDispatcher("/WEB-INF/views/generaliste/consultation-form.jsp").forward(request, response);
            return;
        }

        // Default: File d'attente active
        List<FileAttente> fileActive = patientService.getFileAttenteActive();
        request.setAttribute("fileActive", fileActive);
        request.getRequestDispatcher("/WEB-INF/views/generaliste/file-attente.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String ctx = request.getContextPath();
        User currentUser = (User) request.getSession().getAttribute("user");

        try {
            Long patientId = Long.parseLong(request.getParameter("patientId"));
            String fileIdStr = request.getParameter("fileId");
            Long fileId = (fileIdStr != null && !fileIdStr.isEmpty()) ? Long.parseLong(fileIdStr) : null;
            String motif = request.getParameter("motif");
            String observations = request.getParameter("observations");

            List<Long> acteIds = parseActeIds(request.getParameterValues("actes"));

            // Création de la consultation initiale
            Consultation consultation = consultationService.creerConsultation(patientId, currentUser.getId(), motif, observations, fileId);

            if ("/generaliste/cloturer-directe".equals(path)) {
                // Scénario A : Prise en charge directe
                String diagnostic = request.getParameter("diagnostic");
                String prescription = request.getParameter("prescription");

                consultationService.cloturerPriseEnChargeDirecte(
                        consultation.getId(),
                        diagnostic,
                        prescription,
                        acteIds,
                        fileId
                );

                response.sendRedirect(ctx + "/generaliste/consultations?success=direct");
                return;
            }

            if ("/generaliste/demander-expertise".equals(path)) {
                // Scénario B : Télé-expertise
                Long specialisteId = Long.parseLong(request.getParameter("specialisteId"));
                String creneauIdStr = request.getParameter("creneauId");
                Long creneauId = (creneauIdStr != null && !creneauIdStr.isEmpty()) ? Long.parseLong(creneauIdStr) : null;
                String question = request.getParameter("question");
                PrioriteExpertise priorite = PrioriteExpertise.valueOf(request.getParameter("priorite"));

                consultationService.demanderExpertise(
                        consultation.getId(),
                        specialisteId,
                        creneauId,
                        question,
                        priorite,
                        acteIds,
                        fileId
                );

                response.sendRedirect(ctx + "/generaliste/consultations?success=expertise");
                return;
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Erreur lors de l'enregistrement de la consultation : " + e.getMessage());
            doGet(request, response);
            return;
        }

        response.sendRedirect(ctx + "/generaliste/file-attente");
    }

    private List<Long> parseActeIds(String[] values) {
        if (values == null || values.length == 0) {
            return new ArrayList<>();
        }
        return Arrays.stream(values)
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }
}
