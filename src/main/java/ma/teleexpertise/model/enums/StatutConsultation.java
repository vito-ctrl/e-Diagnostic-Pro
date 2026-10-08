package ma.teleexpertise.model.enums;

public enum StatutConsultation {
    EN_COURS("En cours"),
    TERMINEE("Terminée"),
    EN_ATTENTE_AVIS_SPECIALISTE("En attente avis spécialiste");

    private final String libelle;

    StatutConsultation(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
