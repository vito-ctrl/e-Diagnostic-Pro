package ma.teleexpertise.model.enums;

public enum StatutFileAttente {
    EN_ATTENTE("En attente"),
    EN_CONSULTATION("En consultation"),
    TERMINE("Terminé");

    private final String libelle;

    StatutFileAttente(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
