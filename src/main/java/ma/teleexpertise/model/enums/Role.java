package ma.teleexpertise.model.enums;

public enum Role {
    INFIRMIER("Infirmier"),
    GENERALISTE("Médecin Généraliste"),
    SPECIALISTE("Médecin Spécialiste"),
    ADMIN("Administrateur");

    private final String libelle;

    Role(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
