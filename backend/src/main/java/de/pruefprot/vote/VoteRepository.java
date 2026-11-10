package de.pruefprot.vote;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * Plain SQL for votes. Counters are incremented atomically in the database, never read-modify-write in Java.
 */
@Repository
public class VoteRepository {

    private final JdbcClient jdbc;

    public VoteRepository(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Records the vote unless this voter has already voted on the exam.
     *
     * @return {@code true} if the vote was recorded, {@code false} if it already existed
     */
    public boolean insertIfAbsent(UUID examId, UUID voterId, VoteDirection direction) {
        int inserted = jdbc.sql("""
                        insert into vote (exam_id, voter_id, direction)
                        values (:examId, :voterId, :direction)
                        on conflict (exam_id, voter_id) do nothing
                        """)
                .param("examId", examId)
                .param("voterId", voterId)
                .param("direction", direction.name())
                .update();
        return inserted == 1;
    }

    public void incrementCounter(UUID examId, VoteDirection direction) {
        String sql = switch (direction) {
            case UP -> "update exam set upvotes = upvotes + 1 where id = :examId";
            case DOWN -> "update exam set downvotes = downvotes + 1 where id = :examId";
        };
        jdbc.sql(sql).param("examId", examId).update();
    }

    public Optional<VoteCounts> findCounts(UUID examId) {
        return jdbc.sql("select upvotes, downvotes from exam where id = :examId")
                .param("examId", examId)
                .query((rs, rowNum) -> new VoteCounts(rs.getInt("upvotes"), rs.getInt("downvotes")))
                .optional();
    }

    public record VoteCounts(int upvotes, int downvotes) {
    }
}
