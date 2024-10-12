public class Solution {
    private PlayerAction playerAction;
    private AnalysisData analysisData;

    public Solution(PlayerAction playerAction, AnalysisData analysisData) {
        this.playerAction = playerAction;
        this.analysisData = analysisData;
    }

    public PlayerAction getPlayerAction() {
        return playerAction;
    }

    public void setPlayerAction(PlayerAction playerAction) {
        this.playerAction = playerAction;
    }

    public AnalysisData getAnalysisData() {
        return analysisData;
    }

    public void setAnalysisData(AnalysisData analysisData) {
        this.analysisData = analysisData;
    }
}
