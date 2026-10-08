package ma.teleexpertise.model.enums;

public enum StatutCreneau {
    DISPONIBLE("Disponible"),
    INDISPONIBLE("Indisponible"),
    ARCHIVE("Archivé");

    private final String libelle;

    StatutCreneau(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
