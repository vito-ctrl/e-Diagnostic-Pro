package ma.teleexpertise.service;

import ma.teleexpertise.model.*;
import ma.teleexpertise.model.enums.*;
import ma.teleexpertise.repository.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final DemandeExpertiseRepository demandeExpertiseRepository;
    private final CreneauRepository creneauRepository;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final ActeTechniqueRepository acteTechniqueRepository;
    private final FileAttenteRepository fileAttenteRepository;

    public ConsultationService() {
        this.consultationRepository = new ConsultationRepository();
        this.demandeExpertiseRepository = new DemandeExpertiseRepository();
        this.creneauRepository = new CreneauRepository();
        this.userRepository = new UserRepository();
        this.patientRepository = new PatientRepository();
        this.acteTechniqueRepository = new ActeTechniqueRepository();
        this.fileAttenteRepository = new FileAttenteRepository();
    }

    public ConsultationService(ConsultationRepository consultationRepository,
                               DemandeExpertiseRepository demandeExpertiseRepository,
                               CreneauRepository creneauRepository,
                               UserRepository userRepository,
                               PatientRepository patientRepository,
                               ActeTechniqueRepository acteTechniqueRepository,
                               FileAttenteRepository fileAttenteRepository) {
        this.consultationRepository = consultationRepository;
        this.demandeExpertiseRepository = demandeExpertiseRepository;
        this.creneauRepository = creneauRepository;
        this.userRepository = userRepository;
        this.patientRepository = patientRepository;
        this.acteTechniqueRepository = acteTechniqueRepository;
        this.fileAttenteRepository = fileAttenteRepository;
    }

    public Optional<Consultation> findById(Long id) {
        return consultationRepository.findById(id);
    }

    public List<Consultation> findByGeneraliste(Long generalisteId) {
        return consultationRepository.findByGeneraliste(generalisteId);
    }

    public List<ActeTechnique> getAllActesTechniques() {
        return acteTechniqueRepository.findAll();
    }

    /**
     * US1 : Créer une consultation
     * - Coût consultation fixe : 150 DH
     */
    public Consultation creerConsultation(Long patientId, Long generalisteId, String motif, String observations, Long fileAttenteId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient non trouvé : " + patientId));
        User generaliste = userRepository.findById(generalisteId)
                .orElseThrow(() -> new IllegalArgumentException("Médecin généraliste non trouvé : " + generalisteId));

        Consultation consultation = new Consultation(patient, generaliste, motif, observations);
        consultation.setCoutBase(150.0);
        consultation.setCoutTotal(150.0);
        consultation.setStatut(StatutConsultation.EN_COURS);

        Consultation saved = consultationRepository.save(consultation);

        // Mettre à jour le statut dans la file d'attente
        if (fileAttenteId != null) {
            fileAttenteRepository.updateStatut(fileAttenteId, StatutFileAttente.EN_CONSULTATION);
        }

        return saved;
    }

    /**
     * Scénario A : Prise en charge directe
     * - Établit un diagnostic
     * - Prescrit un traitement
     * - Ajoute actes techniques médicaux optionnels
     * - Calcule le coût total avec Lambda
     * - Clôture la consultation (statut : TERMINEE)
     */
    public Consultation cloturerPriseEnChargeDirecte(Long consultationId, String diagnostic,
                                                     String prescription, List<Long> acteIds,
                                                     Long fileAttenteId) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new IllegalArgumentException("Consultation non trouvée : " + consultationId));

        consultation.setDiagnostic(diagnostic);
        consultation.setPrescription(prescription);

        if (acteIds != null && !acteIds.isEmpty()) {
            List<ActeTechnique> actes = acteTechniqueRepository.findByIds(acteIds);
            consultation.setActesTechniques(actes);
        }

        // Calculer le coût total
        Double coutTotal = calculerCoutTotal(consultation);
        consultation.setCoutTotal(coutTotal);
        consultation.setStatut(StatutConsultation.TERMINEE);

        Consultation saved = consultationRepository.save(consultation);

        if (fileAttenteId != null) {
            fileAttenteRepository.updateStatut(fileAttenteId, StatutFileAttente.TERMINE);
        }

        return saved;
    }

    /**
     * US3 : Demande d'avis spécialiste
     * Filtrer les spécialistes par spécialité et trier par tarif avec Java Stream API
     */
    public List<User> getSpecialistesFiltresEtTries(Specialite specialite, Double maxTarif) {
        List<User> tousLesSpecialistes = userRepository.findByRole(Role.SPECIALISTE);

        // Stream API : filtre par spécialité (et tarif max si spécifié) et trie par tarif croissant
        return tousLesSpecialistes.stream()
                .filter(s -> specialite == null || s.getSpecialite() == specialite)
                .filter(s -> maxTarif == null || (s.getTarifConsultation() != null && s.getTarifConsultation() <= maxTarif))
                .sorted((s1, s2) -> {
                    Double t1 = s1.getTarifConsultation() != null ? s1.getTarifConsultation() : 0.0;
                    Double t2 = s2.getTarifConsultation() != null ? s2.getTarifConsultation() : 0.0;
                    return Double.compare(t1, t2);
                })
                .collect(Collectors.toList());
    }

    /**
     * Scénario B : Demander une télé-expertise
     * - Statut consultation : EN_ATTENTE_AVIS_SPECIALISTE
     * - Réservation du créneau
     * - Création DemandeExpertise
     * - Recalcul du coût total avec Lambda
     */
    public DemandeExpertise demanderExpertise(Long consultationId, Long specialisteId, Long creneauId,
                                              String question, PrioriteExpertise priorite,
                                              List<Long> acteIds, Long fileAttenteId) {
        Consultation consultation = consultationRepository.findById(consultationId)
                .orElseThrow(() -> new IllegalArgumentException("Consultation non trouvée : " + consultationId));

        User specialiste = userRepository.findById(specialisteId)
                .orElseThrow(() -> new IllegalArgumentException("Spécialiste non trouvé : " + specialisteId));

        Creneau creneau = null;
        if (creneauId != null) {
            creneau = creneauRepository.findById(creneauId)
                    .orElseThrow(() -> new IllegalArgumentException("Créneau non trouvé : " + creneauId));

            if (creneau.getStatut() != StatutCreneau.DISPONIBLE) {
                throw new IllegalStateException("Le créneau sélectionné n'est plus disponible");
            }
            // Réserver le créneau : devient indisponible
            creneau.setStatut(StatutCreneau.INDISPONIBLE);
            creneauRepository.save(creneau);
        }

        if (acteIds != null && !acteIds.isEmpty()) {
            List<ActeTechnique> actes = acteTechniqueRepository.findByIds(acteIds);
            consultation.setActesTechniques(actes);
        }

        consultation.setStatut(StatutConsultation.EN_ATTENTE_AVIS_SPECIALISTE);

        DemandeExpertise demande = new DemandeExpertise(consultation, specialiste, creneau, question, priorite);
        demande = demandeExpertiseRepository.save(demande);

        consultation.setDemandeExpertise(demande);

        // Recalculer le coût total
        Double coutTotal = calculerCoutTotal(consultation);
        consultation.setCoutTotal(coutTotal);
        consultationRepository.save(consultation);

        if (fileAttenteId != null) {
            fileAttenteRepository.updateStatut(fileAttenteId, StatutFileAttente.TERMINE);
        }

        return demande;
    }

    /**
     * US4 : Voir le coût total
     * Consultation (150 DH fixe) + Expertise (tarif spécialiste) + Actes techniques médicaux
     * EXIGENCE : Utilisation Lambda : calcul avec map().sum()
     */
    public Double calculerCoutTotal(Consultation consultation) {
        double coutBase = (consultation.getCoutBase() != null) ? consultation.getCoutBase() : 150.0;

        // Tarif spécialiste si demande d'expertise
        double coutExpertise = 0.0;
        if (consultation.getDemandeExpertise() != null &&
            consultation.getDemandeExpertise().getSpecialiste() != null &&
            consultation.getDemandeExpertise().getSpecialiste().getTarifConsultation() != null) {
            coutExpertise = consultation.getDemandeExpertise().getSpecialiste().getTarifConsultation();
        }

        // Somme des tarifs des actes techniques avec Lambda & mapToDouble().sum()
        double coutActes = 0.0;
        if (consultation.getActesTechniques() != null) {
            coutActes = consultation.getActesTechniques().stream()
                    .mapToDouble(ActeTechnique::getTarif)
                    .sum();
        }

        return coutBase + coutExpertise + coutActes;
    }
}
