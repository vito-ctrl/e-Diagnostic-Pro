package ma.teleexpertise.model;

import jakarta.persistence.*;
import ma.teleexpertise.model.enums.Role;
import ma.teleexpertise.model.enums.Specialite;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Specialite specialite;

    private Double tarifConsultation;

    @Column(nullable = false)
    private Integer dureeConsultation = 30;

    @Column(nullable = false)
    private boolean actif = true;

    @OneToMany(mappedBy = "specialiste", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Creneau> creneaux = new ArrayList<>();

    public User() {
    }

    public User(String username, String email, String password, String nom, String prenom, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
    }

    public User(String username, String email, String password, String nom, String prenom, Role role,
                Specialite specialite, Double tarifConsultation) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nom = nom;
        this.prenom = prenom;
        this.role = role;
        this.specialite = specialite;
        this.tarifConsultation = tarifConsultation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Specialite getSpecialite() {
        return specialite;
    }

    public void setSpecialite(Specialite specialite) {
        this.specialite = specialite;
    }

    public Double getTarifConsultation() {
        return tarifConsultation;
    }

    public void setTarifConsultation(Double tarifConsultation) {
        this.tarifConsultation = tarifConsultation;
    }

    public Integer getDureeConsultation() {
        return dureeConsultation;
    }

    public void setDureeConsultation(Integer dureeConsultation) {
        this.dureeConsultation = dureeConsultation;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    public List<Creneau> getCreneaux() {
        return creneaux;
    }

    public void setCreneaux(List<Creneau> creneaux) {
        this.creneaux = creneaux;
    }

    public String getNomComplet() {
        return (role == Role.SPECIALISTE || role == Role.GENERALISTE ? "Dr. " : "") + prenom + " " + nom;
    }
}
