package ma.teleexpertise.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private String prenom;

    private String dateNaissance;

    @Column(unique = true, nullable = false)
    private String numSecu;

    private String telephone;
    private String adresse;
    private String mutuelle;

    @Column(columnDefinition = "TEXT")
    private String antecedents;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @Column(columnDefinition = "TEXT")
    private String traitements;

    @Column(nullable = false)
    private LocalDateTime dateEnregistrement = LocalDateTime.now();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("datePrise DESC")
    private List<SignesVitaux> signesVitauxList = new ArrayList<>();

    public Patient() {
    }

    public Patient(Long id, String nom, String prenom, String dateNaissance,
                   String numSecu, String telephone, String adresse,
                   String mutuelle, String antecedents, String allergies,
                   String traitements) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.numSecu = numSecu;
        this.telephone = telephone;
        this.adresse = adresse;
        this.mutuelle = mutuelle;
        this.antecedents = antecedents;
        this.allergies = allergies;
        this.traitements = traitements;
        this.dateEnregistrement = LocalDateTime.now();
    }

    public Patient(String nom, String prenom, String dateNaissance,
                   String numSecu, String telephone, String adresse,
                   String mutuelle, String antecedents, String allergies,
                   String traitements) {
        this(null, nom, prenom, dateNaissance, numSecu, telephone, adresse, mutuelle, antecedents, allergies, traitements);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getNumSecu() {
        return numSecu;
    }

    public void setNumSecu(String numSecu) {
        this.numSecu = numSecu;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getMutuelle() {
        return mutuelle;
    }

    public void setMutuelle(String mutuelle) {
        this.mutuelle = mutuelle;
    }

    public String getAntecedents() {
        return antecedents;
    }

    public void setAntecedents(String antecedents) {
        this.antecedents = antecedents;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getTraitements() {
        return traitements;
    }

    public void setTraitements(String traitements) {
        this.traitements = traitements;
    }

    public LocalDateTime getDateEnregistrement() {
        return dateEnregistrement;
    }

    public void setDateEnregistrement(LocalDateTime dateEnregistrement) {
        this.dateEnregistrement = dateEnregistrement;
    }

    public List<SignesVitaux> getSignesVitauxList() {
        return signesVitauxList;
    }

    public void setSignesVitauxList(List<SignesVitaux> signesVitauxList) {
        this.signesVitauxList = signesVitauxList;
    }

    public SignesVitaux getDerniersSignesVitaux() {
        if (signesVitauxList != null && !signesVitauxList.isEmpty()) {
            return signesVitauxList.get(0);
        }
        return null;
    }

    public String getNomComplet() {
        return nom + " " + prenom;
    }
}