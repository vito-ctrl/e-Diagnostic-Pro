package ma.teleexpertise.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "signes_vitaux")
public class SignesVitaux {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(nullable = false)
    private LocalDateTime datePrise = LocalDateTime.now();

    @Column(nullable = false)
    private String tensionArterielle; // e.g. 120/80

    @Column(nullable = false)
    private Integer frequenceCardiaque; // bpm

    @Column(nullable = false)
    private Double temperature; // °C

    @Column(nullable = false)
    private Integer frequenceRespiratoire; // respirations/min

    private Double poids; // kg (optionnel)
    private Double taille; // cm (optionnel)

    public SignesVitaux() {
    }

    public SignesVitaux(Patient patient, String tensionArterielle, Integer frequenceCardiaque,
                        Double temperature, Integer frequenceRespiratoire, Double poids, Double taille) {
        this.patient = patient;
        this.datePrise = LocalDateTime.now();
        this.tensionArterielle = tensionArterielle;
        this.frequenceCardiaque = frequenceCardiaque;
        this.temperature = temperature;
        this.frequenceRespiratoire = frequenceRespiratoire;
        this.poids = poids;
        this.taille = taille;
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

    public LocalDateTime getDatePrise() {
        return datePrise;
    }

    public void setDatePrise(LocalDateTime datePrise) {
        this.datePrise = datePrise;
    }

    public String getTensionArterielle() {
        return tensionArterielle;
    }

    public void setTensionArterielle(String tensionArterielle) {
        this.tensionArterielle = tensionArterielle;
    }

    public Integer getFrequenceCardiaque() {
        return frequenceCardiaque;
    }

    public void setFrequenceCardiaque(Integer frequenceCardiaque) {
        this.frequenceCardiaque = frequenceCardiaque;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Integer getFrequenceRespiratoire() {
        return frequenceRespiratoire;
    }

    public void setFrequenceRespiratoire(Integer frequenceRespiratoire) {
        this.frequenceRespiratoire = frequenceRespiratoire;
    }

    public Double getPoids() {
        return poids;
    }

    public void setPoids(Double poids) {
        this.poids = poids;
    }

    public Double getTaille() {
        return taille;
    }

    public void setTaille(Double taille) {
        this.taille = taille;
    }

    public String getResumeSignes() {
        StringBuilder sb = new StringBuilder();
        sb.append("TA: ").append(tensionArterielle != null ? tensionArterielle : "-")
          .append(" mmHg, FC: ").append(frequenceCardiaque != null ? frequenceCardiaque : "-")
          .append(" bpm, T°: ").append(temperature != null ? temperature : "-")
          .append(" °C, FR: ").append(frequenceRespiratoire != null ? frequenceRespiratoire : "-")
          .append(" cpm");
        if (poids != null) {
            sb.append(", Poids: ").append(poids).append(" kg");
        }
        if (taille != null) {
            sb.append(", Taille: ").append(taille).append(" cm");
        }
        return sb.toString();
    }
}
