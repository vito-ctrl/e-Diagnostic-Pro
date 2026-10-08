package ma.teleexpertise.model.enums;

public enum PrioriteExpertise {
    URGENTE("Urgente"),
    NORMALE("Normale"),
    NON_URGENTE("Non urgente");

    private final String libelle;

    PrioriteExpertise(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
