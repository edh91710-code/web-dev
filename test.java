
/*test*/

public class CollaborationScore {
    private String userId;
    private int messageCount;
    private int taskCompleted;
    private int documentCount;
    private double totalScore;

    // Constructor, Getter, Setter 생략
    public void calculate(double w1, double w2, double w3) {
        this.totalScore = (messageCount * w1) + (taskCompleted * w2) + (documentCount * w3);
    }
}


import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CollaborationService {

    // 가중치 설정 (메시지: 0.2, 업무: 0.5, 문서: 0.3)
    private static final double W_MSG = 0.2;
    private static final double W_TASK = 0.5;
    private static final double W_DOC = 0.3;

    public List<CollaborationScore> analyzeTeamPerformance(List<CollaborationScore> rawData) {
        return rawData.stream()
            .peek(data -> data.calculate(W_MSG, W_TASK, W_DOC))
            .sorted(Comparator.comparingDouble(CollaborationScore::getTotalScore).reversed())
            .collect(Collectors.toList());
    }
}



import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/stats")
public class CollaborationController {

    private final CollaborationService collaborationService;

    public CollaborationController(CollaborationService collaborationService) {
        this.collaborationService = collaborationService;
    }

    @PostMapping("/analyze")
    public List<CollaborationScore> getCollaborationAnalysis(@RequestBody List<CollaborationScore> data) {
        return collaborationService.analyzeTeamPerformance(data);
    }
}



/*test*/
