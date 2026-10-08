package ma.teleexpertise.model;

import jakarta.persistence.*;
import ma.teleexpertise.model.enums.StatutFileAttente;

import java.time.LocalDateTime;

@Entity
@Table(name = "file_attente")
public class FileAttente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "signes_vitaux_id")
    private SignesVitaux signesVitaux;

    @Column(nullable = false)
    private LocalDateTime heureArrivee = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutFileAttente statut = StatutFileAttente.EN_ATTENTE;

    public FileAttente() {
    }

    public FileAttente(Patient patient, SignesVitaux signesVitaux) {
        this.patient = patient;
        this.signesVitaux = signesVitaux;
        this.heureArrivee = LocalDateTime.now();
        this.statut = StatutFileAttente.EN_ATTENTE;
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

    public SignesVitaux getSignesVitaux() {
        return signesVitaux;
    }

    public void setSignesVitaux(SignesVitaux signesVitaux) {
        this.signesVitaux = signesVitaux;
    }

    public LocalDateTime getHeureArrivee() {
        return heureArrivee;
    }

    public void setHeureArrivee(LocalDateTime heureArrivee) {
        this.heureArrivee = heureArrivee;
    }

    public StatutFileAttente getStatut() {
        return statut;
    }

    public void setStatut(StatutFileAttente statut) {
        this.statut = statut;
    }
}
