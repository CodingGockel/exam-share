package de.pruefprot.ranking;

import org.springframework.stereotype.Service;

/**
 * Ranking of exams based on their up- and downvotes.
 */
@Service
public class RankingService {

    /** z value for a 95 % confidence level. */
    private static final double Z = 1.96;

    static final int COLLAPSE_MIN_VOTES = 5;
    /** Collapse when less than 30 % of the votes are positive (as integer ratio 3/10 to avoid rounding issues). */
    private static final int COLLAPSE_POSITIVE_NUMERATOR = 3;
    private static final int COLLAPSE_POSITIVE_DENOMINATOR = 10;

    /**
     * Lower bound of the Wilson score confidence interval for the share of positive votes.
     * Returns 0 without any votes; the result is always within [0, 1].
     */
    public double score(int upvotes, int downvotes) {
        requireNonNegative(upvotes, downvotes);
        long total = (long) upvotes + downvotes;
        if (total == 0) {
            return 0.0;
        }
        double n = total;
        double positiveShare = upvotes / n;
        double z2 = Z * Z;
        double centre = positiveShare + z2 / (2 * n);
        double margin = Z * Math.sqrt((positiveShare * (1 - positiveShare) + z2 / (4 * n)) / n);
        double lowerBound = (centre - margin) / (1 + z2 / n);
        // guard against tiny floating point deviations at the edges
        return Math.min(1.0, Math.max(0.0, lowerBound));
    }

    /**
     * An exam is collapsed when it has at least 5 votes and less than 30 % of them are positive.
     */
    public boolean isCollapsed(int upvotes, int downvotes) {
        requireNonNegative(upvotes, downvotes);
        long total = (long) upvotes + downvotes;
        if (total < COLLAPSE_MIN_VOTES) {
            return false;
        }
        return (long) upvotes * COLLAPSE_POSITIVE_DENOMINATOR < total * COLLAPSE_POSITIVE_NUMERATOR;
    }

    private static void requireNonNegative(int upvotes, int downvotes) {
        if (upvotes < 0 || downvotes < 0) {
            throw new IllegalArgumentException("Vote counts must not be negative");
        }
    }
}
