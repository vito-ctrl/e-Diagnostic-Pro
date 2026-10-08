package ma.teleexpertise.model;

import jakarta.persistence.*;
import ma.teleexpertise.model.enums.PrioriteExpertise;
import ma.teleexpertise.model.enums.StatutExpertise;

import java.time.LocalDateTime;

@Entity
@Table(name = "demandes_expertise")
public class DemandeExpertise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "specialiste_id", nullable = false)
    private User specialiste;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creneau_id")
    private Creneau creneau;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String question;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PrioriteExpertise priorite = PrioriteExpertise.NORMALE;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutExpertise statut = StatutExpertise.EN_ATTENTE;

    @Column(columnDefinition = "TEXT")
    private String avisMedical;

    @Column(columnDefinition = "TEXT")
    private String recommandations;

    @Column(nullable = false)
    private LocalDateTime dateDemande = LocalDateTime.now();

    private LocalDateTime dateReponse;

    public DemandeExpertise() {
    }

    public DemandeExpertise(Consultation consultation, User specialiste, Creneau creneau,
                            String question, PrioriteExpertise priorite) {
        this.consultation = consultation;
        this.specialiste = specialiste;
        this.creneau = creneau;
        this.question = question;
        this.priorite = priorite;
        this.statut = StatutExpertise.EN_ATTENTE;
        this.dateDemande = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Consultation getConsultation() {
        return consultation;
    }

    public void setConsultation(Consultation consultation) {
        this.consultation = consultation;
    }

    public User getSpecialiste() {
        return specialiste;
    }

    public void setSpecialiste(User specialiste) {
        this.specialiste = specialiste;
    }

    public Creneau getCreneau() {
        return creneau;
    }

    public void setCreneau(Creneau creneau) {
        this.creneau = creneau;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public PrioriteExpertise getPriorite() {
        return priorite;
    }

    public void setPriorite(PrioriteExpertise priorite) {
        this.priorite = priorite;
    }

    public StatutExpertise getStatut() {
        return statut;
    }

    public void setStatut(StatutExpertise statut) {
        this.statut = statut;
    }

    public String getAvisMedical() {
        return avisMedical;
    }

    public void setAvisMedical(String avisMedical) {
        this.avisMedical = avisMedical;
    }

    public String getRecommandations() {
        return recommandations;
    }

    public void setRecommandations(String recommandations) {
        this.recommandations = recommandations;
    }

    public LocalDateTime getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }

    public LocalDateTime getDateReponse() {
        return dateReponse;
    }

    public void setDateReponse(LocalDateTime dateReponse) {
        this.dateReponse = dateReponse;
    }
}
