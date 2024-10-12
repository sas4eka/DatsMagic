import java.util.List;
import java.util.Map;

public class AnalysisData {
    Map<String, List<Vector>> trajectories;

    public AnalysisData(Map<String, List<Vector>> trajectories) {
        this.trajectories = trajectories;
    }

    public Map<String, List<Vector>> getTrajectories() {
        return trajectories;
    }

    public void setTrajectories(Map<String, List<Vector>> trajectories) {
        this.trajectories = trajectories;
    }
}
