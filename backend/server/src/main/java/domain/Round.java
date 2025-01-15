package domain;

import java.util.List;
import java.util.Objects;

public class Round extends BaseEntity<Long>{
    private Turn currentTurn;
    private List<Turn> turns;


    public Round() {
    }

    public Round(Turn currentTurn, List<Turn> turns) {
        this.currentTurn = currentTurn;
        this.turns = turns;
    }

    public void nextTurn(){
        if(turns.isEmpty()){
            this.currentTurn = null;
        }else{
            this.currentTurn = turns.getFirst();
            turns.remove(0);
        }

    }

    public Turn getCurrentTurn() {
        return this.currentTurn;
    }

    public void setCurrentTurn(Turn currentTurn) {
        this.currentTurn = currentTurn;
    }

    public List<Turn> getTurns() {
        return this.turns;
    }

    public void setTurns(List<Turn> turns) {
        this.turns = turns;
    }

    @Override
    public String toString() {
        return "{" +
            " currentTurn='" + getCurrentTurn() + "'" +
            ", turns='" + getTurns() + "'" +
            "}";
    }
    
}
