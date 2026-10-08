package ma.teleexpertise.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.teleexpertise.model.Creneau;
import ma.teleexpertise.model.User;
import ma.teleexpertise.model.enums.Specialite;
import ma.teleexpertise.service.ConsultationService;
import ma.teleexpertise.service.SpecialisteService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/api/specialistes")
public class SpecialisteApiServlet extends HttpServlet {

    private final ConsultationService consultationService = new ConsultationService();
    private final SpecialisteService specialisteService = new SpecialisteService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String specParam = request.getParameter("specialite");
        String doctorIdParam = request.getParameter("doctorId");

        if (doctorIdParam != null && !doctorIdParam.isEmpty()) {
            try {
                Long doctorId = Long.parseLong(doctorIdParam);
                List<Creneau> creneaux = specialisteService.getCreneauxDisponibles(doctorId);
                StringBuilder json = new StringBuilder("[");
                for (int i = 0; i < creneaux.size(); i++) {
                    Creneau c = creneaux.get(i);
                    json.append("{")
                        .append("\"id\":").append(c.getId()).append(",")
                        .append("\"date\":\"").append(c.getDate()).append("\",")
                        .append("\"plageHoraire\":\"").append(c.getPlageHoraire()).append("\",")
                        .append("\"statut\":\"").append(c.getStatut().name()).append("\"")
                        .append("}");
                    if (i < creneaux.size() - 1) json.append(",");
                }
                json.append("]");
                out.print(json.toString());
                return;
            } catch (NumberFormatException ignored) {
            }
        }

        Specialite specialite = null;
        if (specParam != null && !specParam.isEmpty()) {
            try {
                specialite = Specialite.valueOf(specParam);
            } catch (IllegalArgumentException ignored) {
            }
        }

        // Stream API filtered & sorted specialists
        List<User> list = consultationService.getSpecialistesFiltresEtTries(specialite, null);
        StringBuilder json = new StringBuilder("[");
        for (int i = 0; i < list.size(); i++) {
            User u = list.get(i);
            json.append("{")
                .append("\"id\":").append(u.getId()).append(",")
                .append("\"nomComplet\":\"").append(u.getNomComplet()).append("\",")
                .append("\"specialite\":\"").append(u.getSpecialite() != null ? u.getSpecialite().getLibelle() : "").append("\",")
                .append("\"tarif\":").append(u.getTarifConsultation() != null ? u.getTarifConsultation() : 0.0)
                .append("}");
            if (i < list.size() - 1) json.append(",");
        }
        json.append("]");
        out.print(json.toString());
    }
}
