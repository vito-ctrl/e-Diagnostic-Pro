package ma.teleexpertise.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.teleexpertise.model.FileAttente;
import ma.teleexpertise.model.Patient;
import ma.teleexpertise.service.PatientService;
import ma.teleexpertise.util.CSRFTokenUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@WebServlet(urlPatterns = {
    "/infirmier/patients",
    "/infirmier/recherche",
    "/infirmier/nouveau",
    "/infirmier/existant"
})
public class InfirmierPatientServlet extends HttpServlet {

    private final PatientService patientService = new PatientService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        request.setAttribute("csrfToken", CSRFTokenUtil.getToken(request.getSession()));

        if ("/infirmier/recherche".equals(path)) {
            String query = request.getParameter("query");
            if (query != null && !query.trim().isEmpty()) {
                List<Patient> resultats = patientService.searchPatients(query.trim());
                request.setAttribute("resultats", resultats);
                request.setAttribute("query", query);
            }
            request.getRequestDispatcher("/WEB-INF/views/infirmier/recherche.jsp").forward(request, response);
            return;
        }

        if ("/infirmier/nouveau".equals(path)) {
            request.getRequestDispatcher("/WEB-INF/views/infirmier/nouveau-patient.jsp").forward(request, response);
            return;
        }

        if ("/infirmier/existant".equals(path)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                try {
                    Long id = Long.parseLong(idStr);
                    Optional<Patient> pOpt = patientService.findById(id);
                    if (pOpt.isPresent()) {
                        request.setAttribute("patient", pOpt.get());
                        request.getRequestDispatcher("/WEB-INF/views/infirmier/signes-vitaux-existant.jsp").forward(request, response);
                        return;
                    }
                } catch (NumberFormatException ignored) {
                }
            }
            response.sendRedirect(request.getContextPath() + "/infirmier/recherche");
            return;
        }

        // Default: US2 - Liste des patients enregistrés du jour
        String dateParam = request.getParameter("date");
        LocalDate targetDate = (dateParam != null && !dateParam.isEmpty())
                ? LocalDate.parse(dateParam)
                : LocalDate.now();

        List<FileAttente> patientsDuJour = patientService.getPatientsDuJour(targetDate);
        request.setAttribute("patientsDuJour", patientsDuJour);
        request.setAttribute("selectedDate", targetDate.toString());

        request.getRequestDispatcher("/WEB-INF/views/infirmier/patients-du-jour.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String path = request.getServletPath();
        String ctx = request.getContextPath();

        try {
            if ("/infirmier/nouveau".equals(path)) {
                // US1 - Étape 2b : Nouveau patient
                String nom = request.getParameter("nom");
                String prenom = request.getParameter("prenom");
                String dateNaissance = request.getParameter("dateNaissance");
                String numSecu = request.getParameter("numSecu");
                String telephone = request.getParameter("telephone");
                String adresse = request.getParameter("adresse");
                String mutuelle = request.getParameter("mutuelle");
                String antecedents = request.getParameter("antecedents");
                String allergies = request.getParameter("allergies");
                String traitements = request.getParameter("traitements");

                // Signes vitaux
                String tension = request.getParameter("tension");
                Integer fc = parseInteger(request.getParameter("frequenceCardiaque"));
                Double temp = parseDouble(request.getParameter("temperature"));
                Integer fr = parseInteger(request.getParameter("frequenceRespiratoire"));
                Double poids = parseDouble(request.getParameter("poids"));
                Double taille = parseDouble(request.getParameter("taille"));

                Patient p = new Patient(nom, prenom, dateNaissance, numSecu, telephone, adresse, mutuelle, antecedents, allergies, traitements);
                patientService.enregistrerNouveauPatient(p, tension, fc, temp, fr, poids, taille);

                response.sendRedirect(ctx + "/infirmier/patients?success=1");
                return;
            }

            if ("/infirmier/existant".equals(path)) {
                // US1 - Étape 2a : Patient existant avec nouveaux signes vitaux
                Long patientId = Long.parseLong(request.getParameter("patientId"));
                String tension = request.getParameter("tension");
                Integer fc = parseInteger(request.getParameter("frequenceCardiaque"));
                Double temp = parseDouble(request.getParameter("temperature"));
                Integer fr = parseInteger(request.getParameter("frequenceRespiratoire"));
                Double poids = parseDouble(request.getParameter("poids"));
                Double taille = parseDouble(request.getParameter("taille"));

                patientService.ajouterPatientExistantAFile(patientId, tension, fc, temp, fr, poids, taille);

                response.sendRedirect(ctx + "/infirmier/patients?success=1");
                return;
            }
        } catch (Exception e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("csrfToken", CSRFTokenUtil.getToken(request.getSession()));
            if ("/infirmier/nouveau".equals(path)) {
                request.getRequestDispatcher("/WEB-INF/views/infirmier/nouveau-patient.jsp").forward(request, response);
            } else {
                doGet(request, response);
            }
            return;
        }

        response.sendRedirect(ctx + "/infirmier/patients");
    }

    private Integer parseInteger(String val) {
        if (val == null || val.trim().isEmpty()) return null;
        try {
            return Integer.parseInt(val.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private Double parseDouble(String val) {
        if (val == null || val.trim().isEmpty()) return null;
        try {
            return Double.parseDouble(val.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
