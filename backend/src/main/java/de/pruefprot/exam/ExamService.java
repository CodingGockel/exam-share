package de.pruefprot.exam;

import de.pruefprot.ranking.RankingService;
import de.pruefprot.upload.FilenameSanitizer;
import de.pruefprot.upload.PdfUploadValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ExamService {

    private static final Logger log = LoggerFactory.getLogger(ExamService.class);

    private static final Comparator<ExamResponse> BY_SCORE_THEN_NEWEST = Comparator
            .comparingDouble(ExamResponse::score).reversed()
            .thenComparing(ExamResponse::createdAt, Comparator.reverseOrder());

    /** Upper bound for the number of suggestions per field. */
    private static final int SUGGESTION_LIMIT = 500;

    private final ExamRepository examRepository;
    private final PdfUploadValidator uploadValidator;
    private final RankingService rankingService;

    public ExamService(ExamRepository examRepository, PdfUploadValidator uploadValidator,
                       RankingService rankingService) {
        this.examRepository = examRepository;
        this.uploadValidator = uploadValidator;
        this.rankingService = rankingService;
    }

    @Transactional
    public ExamResponse upload(UploadExamRequest request, MultipartFile file) {
        uploadValidator.validate(file);
        Exam exam = new Exam(
                request.university(),
                request.faculty(),
                request.studyProgram(),
                request.degree(),
                request.subject(),
                request.lecturer(),
                request.semester(),
                FilenameSanitizer.sanitize(file.getOriginalFilename()),
                readBytes(file));
        Exam saved = examRepository.save(exam);
        log.info("Exam uploaded: id={} university=\"{}\" subject=\"{}\" semester={} sizeBytes={}",
                saved.getId(), saved.getUniversity(), saved.getSubject(), saved.getSemester(), saved.getSizeBytes());
        return toResponse(ExamSummary.of(saved));
    }

    /** Filtered list, sorted by ranking score (descending), newest first on ties. */
    @Transactional(readOnly = true)
    public List<ExamResponse> list(ExamFilter filter) {
        return examRepository.search(filter).stream()
                .map(this::toResponse)
                .sorted(BY_SCORE_THEN_NEWEST)
                .toList();
    }

    @Transactional(readOnly = true)
    public ExamSuggestions suggestions() {
        return new ExamSuggestions(
                examRepository.distinctValues(ExamSearch.SuggestionField.UNIVERSITY, SUGGESTION_LIMIT),
                examRepository.distinctValues(ExamSearch.SuggestionField.FACULTY, SUGGESTION_LIMIT),
                examRepository.distinctValues(ExamSearch.SuggestionField.STUDY_PROGRAM, SUGGESTION_LIMIT));
    }

    @Transactional(readOnly = true)
    public Exam getForDownload(UUID id) {
        return examRepository.findById(id).orElseThrow(() -> new ExamNotFoundException(id));
    }

    private ExamResponse toResponse(ExamSummary exam) {
        return new ExamResponse(
                exam.id(),
                exam.university(),
                exam.faculty(),
                exam.studyProgram(),
                exam.degree(),
                exam.subject(),
                exam.lecturer(),
                exam.semester(),
                exam.originalFilename(),
                exam.sizeBytes(),
                exam.upvotes(),
                exam.downvotes(),
                rankingService.score(exam.upvotes(), exam.downvotes()),
                rankingService.isCollapsed(exam.upvotes(), exam.downvotes()),
                exam.createdAt());
    }

    private static byte[] readBytes(MultipartFile file) {
        try {
            return file.getBytes();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
