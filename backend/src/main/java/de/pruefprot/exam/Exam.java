package de.pruefprot.exam;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "exam")
public class Exam {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String university;

    @Column(length = 100)
    private String faculty;

    @Column(name = "study_program", length = 100)
    private String studyProgram;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Degree degree;

    @Column(nullable = false, length = 100)
    private String subject;

    @Column(nullable = false, length = 100)
    private String lecturer;

    @Column(nullable = false, length = 7)
    private String semester;

    @Column(name = "original_filename", nullable = false)
    private String originalFilename;

    @Column(name = "size_bytes", nullable = false)
    private long sizeBytes;

    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false)
    private byte[] content;

    // Vote counters are only changed by atomic SQL updates (see VoteRepository), never by JPA
    @Column(nullable = false, updatable = false)
    private int upvotes;

    @Column(nullable = false, updatable = false)
    private int downvotes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected Exam() {
        // for JPA
    }

    public Exam(String university, String faculty, String studyProgram, Degree degree,
                String subject, String lecturer, String semester, String originalFilename, byte[] content) {
        this.university = university;
        this.faculty = faculty;
        this.studyProgram = studyProgram;
        this.degree = degree;
        this.subject = subject;
        this.lecturer = lecturer;
        this.semester = semester;
        this.originalFilename = originalFilename;
        this.content = content;
        this.sizeBytes = content.length;
        this.upvotes = 0;
        this.downvotes = 0;
        this.createdAt = Instant.now();
    }

    public UUID getId() {
        return id;
    }

    public String getUniversity() {
        return university;
    }

    public String getFaculty() {
        return faculty;
    }

    public String getStudyProgram() {
        return studyProgram;
    }

    public Degree getDegree() {
        return degree;
    }

    public String getSubject() {
        return subject;
    }

    public String getLecturer() {
        return lecturer;
    }

    public String getSemester() {
        return semester;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public byte[] getContent() {
        return content;
    }

    public int getUpvotes() {
        return upvotes;
    }

    public int getDownvotes() {
        return downvotes;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
