package hibernate.projects.Enum;

public enum TypeUse {
    BANG("Carta de ataque; se utiliza para hacer daño a un jugador a distancia válida."),
    FAILED("Carta de defensa; puede anular un BANG si se juega a tiempo."),
    BEER("Carta de curación; recupera 1 punto de vida en el jugador que la juega.");

    public final String description;

    TypeUse(String description) {
        this.description = description;
    }
}
