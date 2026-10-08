package ma.teleexpertise.model;

import jakarta.persistence.*;
import ma.teleexpertise.model.enums.StatutConsultation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "consultations")
public class Consultation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "generaliste_id", nullable = false)
    private User generaliste;

    @Column(nullable = false)
    private LocalDateTime dateConsultation = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String motif;

    @Column(columnDefinition = "TEXT")
    private String observations;

    @Column(columnDefinition = "TEXT")
    private String diagnostic;

    @Column(columnDefinition = "TEXT")
    private String prescription;

    @Column(nullable = false)
    private Double coutBase = 150.0; // Coût fixe consultation généraliste

    private Double coutTotal = 150.0;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutConsultation statut = StatutConsultation.EN_COURS;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "consultation_actes",
        joinColumns = @JoinColumn(name = "consultation_id"),
        inverseJoinColumns = @JoinColumn(name = "acte_id")
    )
    private List<ActeTechnique> actesTechniques = new ArrayList<>();

    @OneToOne(mappedBy = "consultation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private DemandeExpertise demandeExpertise;

    public Consultation() {
    }

    public Consultation(Patient patient, User generaliste, String motif, String observations) {
        this.patient = patient;
        this.generaliste = generaliste;
        this.motif = motif;
        this.observations = observations;
        this.dateConsultation = LocalDateTime.now();
        this.coutBase = 150.0;
        this.coutTotal = 150.0;
        this.statut = StatutConsultation.EN_COURS;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public User getGeneraliste() {
        return generaliste;
    }

    public void setGeneraliste(User generaliste) {
        this.generaliste = generaliste;
    }

    public LocalDateTime getDateConsultation() {
        return dateConsultation;
    }

    public void setDateConsultation(LocalDateTime dateConsultation) {
        this.dateConsultation = dateConsultation;
    }

    public String getMotif() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif = motif;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public String getDiagnostic() {
        return diagnostic;
    }

    public void setDiagnostic(String diagnostic) {
        this.diagnostic = diagnostic;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public Double getCoutBase() {
        return coutBase;
    }

    public void setCoutBase(Double coutBase) {
        this.coutBase = coutBase;
    }

    public Double getCoutTotal() {
        return coutTotal;
    }

    public void setCoutTotal(Double coutTotal) {
        this.coutTotal = coutTotal;
    }

    public StatutConsultation getStatut() {
        return statut;
    }

    public void setStatut(StatutConsultation statut) {
        this.statut = statut;
    }

    public List<ActeTechnique> getActesTechniques() {
        return actesTechniques;
    }

    public void setActesTechniques(List<ActeTechnique> actesTechniques) {
        this.actesTechniques = actesTechniques;
    }

    public DemandeExpertise getDemandeExpertise() {
        return demandeExpertise;
    }

    public void setDemandeExpertise(DemandeExpertise demandeExpertise) {
        this.demandeExpertise = demandeExpertise;
    }
}
