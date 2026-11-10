package de.pruefprot.vote;

import de.pruefprot.exam.ExamNotFoundException;
import de.pruefprot.exam.ExamRepository;
import de.pruefprot.ranking.RankingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class VoteService {

    private static final Logger log = LoggerFactory.getLogger(VoteService.class);

    private final ExamRepository examRepository;
    private final VoteRepository voteRepository;
    private final RankingService rankingService;

    public VoteService(ExamRepository examRepository, VoteRepository voteRepository, RankingService rankingService) {
        this.examRepository = examRepository;
        this.voteRepository = voteRepository;
        this.rankingService = rankingService;
    }

    @Transactional
    public VoteResponse vote(UUID examId, UUID voterId, VoteDirection direction) {
        if (!examRepository.existsById(examId)) {
            throw new ExamNotFoundException(examId);
        }
        if (!voteRepository.insertIfAbsent(examId, voterId, direction)) {
            throw new DuplicateVoteException();
        }
        voteRepository.incrementCounter(examId, direction);
        log.info("Vote counted: examId={} direction={}", examId, direction);

        VoteRepository.VoteCounts counts = voteRepository.findCounts(examId)
                .orElseThrow(() -> new ExamNotFoundException(examId));
        return new VoteResponse(
                examId,
                counts.upvotes(),
                counts.downvotes(),
                rankingService.score(counts.upvotes(), counts.downvotes()),
                rankingService.isCollapsed(counts.upvotes(), counts.downvotes()));
    }
}
