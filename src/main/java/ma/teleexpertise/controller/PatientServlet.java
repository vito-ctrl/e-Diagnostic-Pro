package ma.teleexpertise.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ma.teleexpertise.model.Patient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/patient")
public class PatientServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        List<Patient> patients = new ArrayList<>();

        patients.add(new Patient(
                1L,
                "Alami",
                "Ahmed",
                "1995-03-12",
                "AB123456",
                "0612345678",
                "Beni Mellal",
                "CNSS",
                "Diabète",
                "Pénicilline",
                "Metformine"
        ));

        patients.add(new Patient(
                2L,
                "Alaoui",
                "Sara",
                "1998-07-20",
                "CD789012",
                "0623456789",
                "Marrakech",
                "CNSS",
                "Asthme",
                "Aucune",
                "Ventoline"
        ));

        patients.add(new Patient(
                3L,
                "Bennani",
                "Youssef",
                "1990-11-05",
                "EF345678",
                "0634567890",
                "Casablanca",
                "Mutuelle privée",
                "Hypertension",
                "Aspirine",
                "Amlodipine"
        ));

        request.setAttribute("patients", patients);

        request.getRequestDispatcher("/WEB-INF/views/patient.jsp")
                .forward(request, response);
    }
}