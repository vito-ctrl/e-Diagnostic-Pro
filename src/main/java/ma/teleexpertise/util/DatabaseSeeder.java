package ma.teleexpertise.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ma.teleexpertise.model.*;
import ma.teleexpertise.model.enums.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class DatabaseSeeder {

    public static void seed() {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Seed Users if table is empty
            Long userCount = em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
            if (userCount == 0) {
                User infirmier = new User("infirmier", "infirmier@teleexpertise.ma",
                        PasswordUtil.hashPassword("password123"), "Benali", "Karim", Role.INFIRMIER);
                em.persist(infirmier);

                User generaliste = new User("generaliste", "generaliste@teleexpertise.ma",
                        PasswordUtil.hashPassword("password123"), "El Amrani", "Yassine", Role.GENERALISTE);
                em.persist(generaliste);

                User specialisteCardio = new User("cardio", "cardio@teleexpertise.ma",
                        PasswordUtil.hashPassword("password123"), "Tazi", "Fatima", Role.SPECIALISTE,
                        Specialite.CARDIOLOGIE, 300.0);
                em.persist(specialisteCardio);

                User specialistePneumo = new User("pneumo", "pneumo@teleexpertise.ma",
                        PasswordUtil.hashPassword("password123"), "Berrada", "Omar", Role.SPECIALISTE,
                        Specialite.PNEUMOLOGIE, 250.0);
                em.persist(specialistePneumo);

                User specialisteDermato = new User("dermato", "dermato@teleexpertise.ma",
                        PasswordUtil.hashPassword("password123"), "Alami", "Sanae", Role.SPECIALISTE,
                        Specialite.DERMATOLOGIE, 200.0);
                em.persist(specialisteDermato);

                User admin = new User("admin", "admin@teleexpertise.ma",
                        PasswordUtil.hashPassword("admin123"), "Idrissi", "Mehdi", Role.ADMIN);
                em.persist(admin);
            }

            // Seed Technical Acts
            Long actsCount = em.createQuery("SELECT COUNT(a) FROM ActeTechnique a", Long.class).getSingleResult();
            if (actsCount == 0) {
                em.persist(new ActeTechnique("Radiographie", "Imagerie médicale par rayons X", 250.0));
                em.persist(new ActeTechnique("Échographie", "Exploration par ultrasons", 200.0));
                em.persist(new ActeTechnique("IRM", "Imagerie par résonance magnétique", 800.0));
                em.persist(new ActeTechnique("Électrocardiogramme", "Enregistrement de l'activité électrique du cœur", 150.0));
                em.persist(new ActeTechnique("Actes dermatologiques (laser)", "Traitement au laser cutané", 350.0));
                em.persist(new ActeTechnique("Fond d'œil", "Examen de la rétine et du nerf optique", 180.0));
                em.persist(new ActeTechnique("Analyse de sang", "Bilan biologique sanguin complet", 100.0));
                em.persist(new ActeTechnique("Analyse d'urine", "Examen cyto-bactériologique des urines", 80.0));
            }

            // Seed Time Slots for specialists for today and upcoming 7 days
            List<User> specialistes = em.createQuery("SELECT u FROM User u WHERE u.role = :role", User.class)
                    .setParameter("role", Role.SPECIALISTE)
                    .getResultList();

            LocalDate today = LocalDate.now();
            for (User spec : specialistes) {
                Long slotCount = em.createQuery("SELECT COUNT(c) FROM Creneau c WHERE c.specialiste = :spec", Long.class)
                        .setParameter("spec", spec)
                        .getSingleResult();

                if (slotCount == 0) {
                    for (int dayOffset = 0; dayOffset <= 7; dayOffset++) {
                        LocalDate slotDate = today.plusDays(dayOffset);
                        // Standard 30 min fixed slots from 09:00 to 12:00
                        addSlot(em, spec, slotDate, LocalTime.of(9, 0), LocalTime.of(9, 30), StatutCreneau.DISPONIBLE);
                        addSlot(em, spec, slotDate, LocalTime.of(9, 30), LocalTime.of(10, 0), StatutCreneau.DISPONIBLE);
                        addSlot(em, spec, slotDate, LocalTime.of(10, 0), LocalTime.of(10, 30), StatutCreneau.DISPONIBLE);
                        addSlot(em, spec, slotDate, LocalTime.of(10, 30), LocalTime.of(11, 0), StatutCreneau.INDISPONIBLE);
                        addSlot(em, spec, slotDate, LocalTime.of(11, 0), LocalTime.of(11, 30), StatutCreneau.DISPONIBLE);
                        addSlot(em, spec, slotDate, LocalTime.of(11, 30), LocalTime.of(12, 0), StatutCreneau.DISPONIBLE);
                    }
                }
            }

            // Seed sample Patients if none
            Long patientCount = em.createQuery("SELECT COUNT(p) FROM Patient p", Long.class).getSingleResult();
            if (patientCount == 0) {
                Patient p1 = new Patient("Alami", "Ahmed", "1995-03-12", "AB123456",
                        "0612345678", "Casablanca", "CNSS", "Diabète type 2", "Pénicilline", "Metformine 500mg");
                em.persist(p1);

                Patient p2 = new Patient("Alaoui", "Sara", "1998-07-20", "CD789012",
                        "0623456789", "Marrakech", "CNOPS", "Asthme modéré", "Aucune", "Ventoline");
                em.persist(p2);

                Patient p3 = new Patient("Bennani", "Mohamed", "1982-11-05", "EF345678",
                        "0634567890", "Rabat", "CNSS", "Hypertension artérielle", "Aspirine", "Amlodipine 5mg");
                em.persist(p3);

                // Add vital signs & queue for p1 and p2
                SignesVitaux sv1 = new SignesVitaux(p1, "130/85", 78, 37.1, 18, 74.5, 175.0);
                em.persist(sv1);
                FileAttente f1 = new FileAttente(p1, sv1);
                em.persist(f1);

                SignesVitaux sv2 = new SignesVitaux(p2, "115/75", 82, 36.8, 16, 58.0, 163.0);
                em.persist(sv2);
                FileAttente f2 = new FileAttente(p2, sv2);
                em.persist(f2);
            }

            tx.commit();
            System.out.println("Database seeding completed successfully.");
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Database seeding failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    private static void addSlot(EntityManager em, User spec, LocalDate date, LocalTime debut, LocalTime fin, StatutCreneau statut) {
        Creneau creneau = new Creneau(spec, date, debut, fin, statut);
        em.persist(creneau);
    }

    public static void main(String[] args) {
        seed();
        JPAUtil.shutdown();
    }
}
