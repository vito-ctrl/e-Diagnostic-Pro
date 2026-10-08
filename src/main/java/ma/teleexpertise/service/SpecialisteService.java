package ma.teleexpertise.service;

import ma.teleexpertise.model.Creneau;
import ma.teleexpertise.model.DemandeExpertise;
import ma.teleexpertise.model.User;
import ma.teleexpertise.model.enums.*;
import ma.teleexpertise.repository.ConsultationRepository;
import ma.teleexpertise.repository.CreneauRepository;
import ma.teleexpertise.repository.DemandeExpertiseRepository;
import ma.teleexpertise.repository.UserRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SpecialisteService {

    private final UserRepository userRepository;
    private final CreneauRepository creneauRepository;
    private final DemandeExpertiseRepository demandeExpertiseRepository;
    private final ConsultationRepository consultationRepository;

    public SpecialisteService() {
        this.userRepository = new UserRepository();
        this.creneauRepository = new CreneauRepository();
        this.demandeExpertiseRepository = new DemandeExpertiseRepository();
        this.consultationRepository = new ConsultationRepository();
    }

    public SpecialisteService(UserRepository userRepository,
                              CreneauRepository creneauRepository,
                              DemandeExpertiseRepository demandeExpertiseRepository,
                              ConsultationRepository consultationRepository) {
        this.userRepository = userRepository;
        this.creneauRepository = creneauRepository;
        this.demandeExpertiseRepository = demandeExpertiseRepository;
        this.consultationRepository = consultationRepository;
    }

    /**
     * US5 : Configurer son profil
     */
    public User configurerProfil(Long specialisteId, Specialite specialite, Double tarif) {
        User specialiste = userRepository.findById(specialisteId)
                .orElseThrow(() -> new IllegalArgumentException("Spécialiste non trouvé : " + specialisteId));

        if (specialite != null) {
            specialiste.setSpecialite(specialite);
        }
        if (tarif != null && tarif >= 0) {
            specialiste.setTarifConsultation(tarif);
        }
        specialiste.setDureeConsultation(30); // Fixe : 30 min
        return userRepository.save(specialiste);
    }

    /**
     * US6 : Voir ses créneaux
     * Les créneaux passés sont automatiquement archivés.
     */
    public List<Creneau> getCreneauxSpecialiste(Long specialisteId) {
        return creneauRepository.findBySpecialiste(specialisteId);
    }

    public List<Creneau> getCreneauxParDate(Long specialisteId, LocalDate date) {
        return creneauRepository.findBySpecialisteAndDate(specialisteId, date);
    }

    public List<Creneau> getCreneauxDisponibles(Long specialisteId) {
        return creneauRepository.findAvailableBySpecialiste(specialisteId);
    }

    public void updateStatutCreneau(Long creneauId, StatutCreneau statut) {
        creneauRepository.updateStatut(creneauId, statut);
    }

    /**
     * Initialiser les créneaux par défaut pour une journée (09h00 à 12h00 par tranches de 30 min)
     */
    public void genererCreneauxJournee(Long specialisteId, LocalDate date) {
        User specialiste = userRepository.findById(specialisteId)
                .orElseThrow(() -> new IllegalArgumentException("Spécialiste non trouvé"));

        List<Creneau> existing = creneauRepository.findBySpecialisteAndDate(specialisteId, date);
        if (existing.isEmpty()) {
            creneauRepository.save(new Creneau(specialiste, date, LocalTime.of(9, 0), LocalTime.of(9, 30), StatutCreneau.DISPONIBLE));
            creneauRepository.save(new Creneau(specialiste, date, LocalTime.of(9, 30), LocalTime.of(10, 0), StatutCreneau.DISPONIBLE));
            creneauRepository.save(new Creneau(specialiste, date, LocalTime.of(10, 0), LocalTime.of(10, 30), StatutCreneau.DISPONIBLE));
            creneauRepository.save(new Creneau(specialiste, date, LocalTime.of(10, 30), LocalTime.of(11, 0), StatutCreneau.INDISPONIBLE));
            creneauRepository.save(new Creneau(specialiste, date, LocalTime.of(11, 0), LocalTime.of(11, 30), StatutCreneau.DISPONIBLE));
            creneauRepository.save(new Creneau(specialiste, date, LocalTime.of(11, 30), LocalTime.of(12, 0), StatutCreneau.DISPONIBLE));
        }
    }

    /**
     * US7 : Consulter les demandes d'expertise
     * Utilisation Stream API : filtrer par statut (EN_ATTENTE, TERMINEE) et par priorité
     */
    public List<DemandeExpertise> getDemandesExpertise(Long specialisteId, StatutExpertise filtreStatut, PrioriteExpertise filtrePriorite) {
        List<DemandeExpertise> allRequests = demandeExpertiseRepository.findBySpecialiste(specialisteId);

        // Stream API filtering
        return allRequests.stream()
                .filter(de -> filtreStatut == null || de.getStatut() == filtreStatut)
                .filter(de -> filtrePriorite == null || de.getPriorite() == filtrePriorite)
                .sorted((d1, d2) -> {
                    // Trier par priorité URGENTE d'abord puis par date
                    if (d1.getPriorite() != d2.getPriorite()) {
                        return d1.getPriorite().compareTo(d2.getPriorite());
                    }
                    return d2.getDateDemande().compareTo(d1.getDateDemande());
                })
                .collect(Collectors.toList());
    }

    public Optional<DemandeExpertise> getDemandeById(Long id) {
        return demandeExpertiseRepository.findById(id);
    }

    /**
     * US8 : Répondre à une expertise
     * - Saisir l'avis médical
     * - Saisir les recommandations
     * - Marquer comme terminée
     */
    public DemandeExpertise repondreExpertise(Long demandeId, String avisMedical, String recommandations) {
        DemandeExpertise demande = demandeExpertiseRepository.findById(demandeId)
                .orElseThrow(() -> new IllegalArgumentException("Demande d'expertise non trouvée : " + demandeId));

        demande.setAvisMedical(avisMedical);
        demande.setRecommandations(recommandations);
        demande.setStatut(StatutExpertise.TERMINEE);
        demande.setDateReponse(LocalDateTime.now());

        DemandeExpertise savedDemande = demandeExpertiseRepository.save(demande);

        // Mettre à jour la consultation liée
        if (demande.getConsultation() != null) {
            demande.getConsultation().setStatut(StatutConsultation.TERMINEE);
            consultationRepository.save(demande.getConsultation());
        }

        return savedDemande;
    }
}
