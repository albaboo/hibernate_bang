package hibernate.projects.Enum;

public enum TypeRole {
    SHERIFF("Los malhechores y el renegado deben quedar eliminados. Identidad publica desde el principio."),
    RENEGADE("Ser el último jugador vivo junto al sheriff."),
    MALFACTOR("El sheriff debe quedar eliminado."),
    ASSISTANT("Los malhechores y el renegado deben quedar eliminados. Ayuda al sheriff.");

    public final String objective;

    TypeRole(String objective) {
        this.objective = objective;
    }

}
