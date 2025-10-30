package hibernate.projects.Controller;

import java.util.List;

import hibernate.projects.Entity.Card;

public class PlayerDAO {

    public List<Card> showHand(int idPlayer) {

        return null;
    }

    public void useBang(int idAttacker, int idObjective) {

    }

    public void discardCard(int idPlayer, int idCard) {

    }

    public void checkElimination(int idPlayer) {

    }

    public void stealCard(int idPlayer) {

    }

    public void passTurn(int idPlayer) {

    }

    public void equipCard(int idPlayer, int idCard) {

    }

    public int calculateDistance(int idPlayerOrigin, int idPlayerDestination) {

        return idPlayerOrigin - idPlayerDestination;
    }

    public boolean checkDistanceAttack(int idAttacker, int idObjective) {

        return false;
    }

}
