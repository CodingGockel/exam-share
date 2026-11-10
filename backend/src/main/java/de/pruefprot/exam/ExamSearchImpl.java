package de.pruefprot.exam;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

class ExamSearchImpl implements ExamSearch {

    private static final char LIKE_ESCAPE = '\\';

    private final EntityManager entityManager;

    ExamSearchImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<ExamSummary> search(ExamFilter filter) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<ExamSummary> query = cb.createQuery(ExamSummary.class);
        Root<Exam> exam = query.from(Exam.class);

        query.select(cb.construct(ExamSummary.class,
                exam.get("id"),
                exam.get("university"),
                exam.get("faculty"),
                exam.get("studyProgram"),
                exam.get("degree"),
                exam.get("subject"),
                exam.get("lecturer"),
                exam.get("semester"),
                exam.get("originalFilename"),
                exam.get("sizeBytes"),
                exam.get("upvotes"),
                exam.get("downvotes"),
                exam.get("createdAt")));

        List<Predicate> predicates = new ArrayList<>();
        addContains(predicates, cb, exam.get("university"), filter.university());
        addContains(predicates, cb, exam.get("faculty"), filter.faculty());
        addContains(predicates, cb, exam.get("studyProgram"), filter.studyProgram());
        addContains(predicates, cb, exam.get("subject"), filter.subject());
        addContains(predicates, cb, exam.get("lecturer"), filter.lecturer());
        if (filter.degree() != null) {
            predicates.add(cb.equal(exam.get("degree"), filter.degree()));
        }
        if (hasText(filter.semester())) {
            predicates.add(cb.equal(exam.get("semester"), filter.semester().strip()));
        }
        query.where(predicates.toArray(Predicate[]::new));

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public List<String> distinctValues(SuggestionField field, int limit) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<String> query = cb.createQuery(String.class);
        Root<Exam> exam = query.from(Exam.class);
        Path<String> column = exam.get(field.attribute());
        query.select(column).distinct(true)
                .where(cb.isNotNull(column))
                .orderBy(cb.asc(column));
        return entityManager.createQuery(query).setMaxResults(limit).getResultList();
    }

    /** Case-insensitive partial match; ignored if the term is blank. */
    private static void addContains(List<Predicate> predicates, CriteriaBuilder cb, Path<String> field, String term) {
        if (!hasText(term)) {
            return;
        }
        String pattern = "%" + escapeLike(term.strip().toLowerCase(Locale.ROOT)) + "%";
        predicates.add(cb.like(cb.lower(field), pattern, LIKE_ESCAPE));
    }

    /** User input must not act as a wildcard. */
    private static String escapeLike(String value) {
        return value
                .replace("\\", "\\\\")
                .replace("%", "\\%")
                .replace("_", "\\_");
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
