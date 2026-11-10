package de.pruefprot.exam;

import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/exams")
public class ExamController {

    private final ExamService examService;

    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ExamResponse upload(@RequestPart("file") MultipartFile file,
                               @Valid @ModelAttribute UploadExamRequest request) {
        return examService.upload(request, file);
    }

    @GetMapping
    public List<ExamResponse> list(@RequestParam(required = false) String university,
                                   @RequestParam(required = false) String faculty,
                                   @RequestParam(required = false) String studyProgram,
                                   @RequestParam(required = false) Degree degree,
                                   @RequestParam(required = false) String subject,
                                   @RequestParam(required = false) String lecturer,
                                   @RequestParam(required = false) String semester) {
        return examService.list(
                new ExamFilter(university, faculty, studyProgram, degree, subject, lecturer, semester));
    }

    /** Existing universities, faculties and study programs for input suggestions. */
    @GetMapping("/suggestions")
    public ExamSuggestions suggestions() {
        return examService.suggestions();
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> download(@PathVariable UUID id) {
        Exam exam = examService.getForDownload(id);
        ContentDisposition disposition = ContentDisposition.attachment()
                .filename(exam.getOriginalFilename(), StandardCharsets.UTF_8)
                .build();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(exam.getSizeBytes())
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition.toString())
                .body(exam.getContent());
    }
}
