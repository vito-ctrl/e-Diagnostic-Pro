package ma.teleexpertise.model;

import jakarta.persistence.*;
import ma.teleexpertise.model.enums.StatutCreneau;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "creneaux")
public class Creneau {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialiste_id", nullable = false)
    private User specialiste;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private LocalTime heureDebut;

    @Column(nullable = false)
    private LocalTime heureFin;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatutCreneau statut = StatutCreneau.DISPONIBLE;

    public Creneau() {
    }

    public Creneau(User specialiste, LocalDate date, LocalTime heureDebut, LocalTime heureFin, StatutCreneau statut) {
        this.specialiste = specialiste;
        this.date = date;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.statut = statut;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getSpecialiste() {
        return specialiste;
    }

    public void setSpecialiste(User specialiste) {
        this.specialiste = specialiste;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalTime heureFin) {
        this.heureFin = heureFin;
    }

    public StatutCreneau getStatut() {
        return statut;
    }

    public void setStatut(StatutCreneau statut) {
        this.statut = statut;
    }

    public String getPlageHoraire() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH'h'mm");
        return heureDebut.format(dtf) + " - " + heureFin.format(dtf);
    }
}
