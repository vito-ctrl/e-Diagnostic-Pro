package ma.teleexpertise.service;

import ma.teleexpertise.model.FileAttente;
import ma.teleexpertise.model.Patient;
import ma.teleexpertise.model.SignesVitaux;
import ma.teleexpertise.model.enums.StatutFileAttente;
import ma.teleexpertise.repository.FileAttenteRepository;
import ma.teleexpertise.repository.PatientRepository;
import ma.teleexpertise.repository.SignesVitauxRepository;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PatientService {

    private final PatientRepository patientRepository;
    private final SignesVitauxRepository signesVitauxRepository;
    private final FileAttenteRepository fileAttenteRepository;

    public PatientService() {
        this.patientRepository = new PatientRepository();
        this.signesVitauxRepository = new SignesVitauxRepository();
        this.fileAttenteRepository = new FileAttenteRepository();
    }

    public PatientService(PatientRepository patientRepository,
                          SignesVitauxRepository signesVitauxRepository,
                          FileAttenteRepository fileAttenteRepository) {
        this.patientRepository = patientRepository;
        this.signesVitauxRepository = signesVitauxRepository;
        this.fileAttenteRepository = fileAttenteRepository;
    }

    /**
     * US1 - Étape 1 : Recherche du patient
     */
    public Optional<Patient> findByNumSecu(String numSecu) {
        if (numSecu == null || numSecu.trim().isEmpty()) {
            return Optional.empty();
        }
        return patientRepository.findByNumSecu(numSecu);
    }

    public Optional<Patient> findById(Long id) {
        return patientRepository.findById(id);
    }

    public List<Patient> searchPatients(String query) {
        return patientRepository.search(query);
    }

    /**
     * US1 - Étape 2b : Nouveau patient
     * Saisie des données administratives & médicales + signes vitaux
     * Création dossier patient + ajout automatique à la file d'attente
     */
    public Patient enregistrerNouveauPatient(Patient patient,
                                             String tension, Integer fc, Double temp,
                                             Integer fr, Double poids, Double taille) {
        if (patientRepository.findByNumSecu(patient.getNumSecu()).isPresent()) {
            throw new IllegalArgumentException("Un patient avec le numéro de sécurité sociale " +
                    patient.getNumSecu() + " existe déjà.");
        }

        Patient savedPatient = patientRepository.save(patient);

        // Enregistrement des signes vitaux
        SignesVitaux signes = new SignesVitaux(savedPatient, tension, fc, temp, fr, poids, taille);
        SignesVitaux savedSignes = signesVitauxRepository.save(signes);

        // Ajout automatique à la file d'attente
        FileAttente fileItem = new FileAttente(savedPatient, savedSignes);
        fileAttenteRepository.save(fileItem);

        return savedPatient;
    }

    /**
     * US1 - Étape 2a : Patient existant trouvé
     * Saisie uniquement des nouveaux signes vitaux et ajout à la file d'attente
     */
    public FileAttente ajouterPatientExistantAFile(Long patientId,
                                                  String tension, Integer fc, Double temp,
                                                  Integer fr, Double poids, Double taille) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient non trouvé : " + patientId));

        SignesVitaux signes = new SignesVitaux(patient, tension, fc, temp, fr, poids, taille);
        SignesVitaux savedSignes = signesVitauxRepository.save(signes);

        FileAttente fileItem = new FileAttente(patient, savedSignes);
        return fileAttenteRepository.save(fileItem);
    }

    /**
     * US2 : Voir la liste des patients enregistrés du jour
     * - Liste simple des patients du jour
     * - Affichage : nom, prénom, heure d'arrivée, signes vitaux, numéro de sécurité sociale
     * - Tri par heure d'arrivée (du plus ancien au plus récent)
     * - Utilisation Stream API : filtrer les patients par date d'enregistrement
     */
    public List<FileAttente> getPatientsDuJour(LocalDate date) {
        LocalDate targetDate = (date != null) ? date : LocalDate.now();

        List<FileAttente> allQueueItems = fileAttenteRepository.findAll();

        // Stream API: Filter by registration date and sort by arrival time (oldest to newest)
        return allQueueItems.stream()
                .filter(item -> item.getHeureArrivee().toLocalDate().isEqual(targetDate))
                .sorted(Comparator.comparing(FileAttente::getHeureArrivee))
                .collect(Collectors.toList());
    }

    /**
     * Obtenir la file d'attente active pour le médecin généraliste
     */
    public List<FileAttente> getFileAttenteActive() {
        return fileAttenteRepository.findByStatut(StatutFileAttente.EN_ATTENTE);
    }

    public void updateStatutFile(Long fileId, StatutFileAttente statut) {
        fileAttenteRepository.updateStatut(fileId, statut);
    }

    public Optional<FileAttente> getFileItem(Long fileId) {
        return fileAttenteRepository.findById(fileId);
    }
}
