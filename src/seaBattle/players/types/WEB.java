package seaBattle.players.types;

import seaBattle.elements.Boat;
import seaBattle.players.Player;

public class WEB extends Player {
    public WEB(String name) {
        super(name);
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    @Override
    public Boat getBoat() throws UI.input.CommandException {
        return null;
    }

    @Override
    public int[] getAction(Player enemy) throws UI.input.CommandException {
        return new int[0];
    }

    @Override
    public void retAnswer(int code) {

    }
}
