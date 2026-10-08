package ma.teleexpertise.model.enums;

public enum Specialite {
    CARDIOLOGIE("Cardiologie"),
    PNEUMOLOGIE("Pneumologie"),
    DERMATOLOGIE("Dermatologie"),
    NEUROLOGIE("Neurologie"),
    ENDOCRINOLOGIE("Endocrinologie"),
    OPHTALMOLOGIE("Ophtalmologie"),
    GASTRO_ENTEROLOGIE("Gastro-entérologie"),
    PEDIATRIE("Pédiatrie");

    private final String libelle;

    Specialite(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
