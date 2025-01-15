package domain;

public class RoomSettings {
    private Integer numRounds;
    private Integer timePerTurn;
    private Integer wordCount;

    public RoomSettings() {
    }

    public RoomSettings(Integer numRounds, Integer timePerTurn, Integer wordCount) {
        this.numRounds = numRounds;
        this.timePerTurn = timePerTurn;
        this.wordCount = wordCount;
    }

    public Integer getNumRounds() {
        return this.numRounds;
    }

    public void setNumRounds(Integer numRounds) {
        this.numRounds = numRounds;
    }

    public Integer getTimePerTurn() {
        return this.timePerTurn;
    }

    public void setTimePerTurn(Integer timePerTurn) {
        this.timePerTurn = timePerTurn;
    }

    public Integer getWordCount() {
        return this.wordCount;
    }

    public void setWordCount(Integer wordCount) {
        this.wordCount = wordCount;
    }


}
