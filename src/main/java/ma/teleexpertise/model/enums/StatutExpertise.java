package ma.teleexpertise.model.enums;

public enum StatutExpertise {
    EN_ATTENTE("En attente"),
    TERMINEE("Terminée");

    private final String libelle;

    StatutExpertise(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
