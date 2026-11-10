create table exam
(
    id                uuid primary key,
    subject           varchar(100) not null,
    lecturer          varchar(100) not null,
    semester          varchar(7)   not null,
    original_filename varchar(255) not null,
    size_bytes        bigint       not null,
    content           bytea        not null,
    upvotes           integer      not null default 0,
    downvotes         integer      not null default 0,
    created_at        timestamptz  not null default now(),
    constraint exam_upvotes_non_negative check (upvotes >= 0),
    constraint exam_downvotes_non_negative check (downvotes >= 0)
);

create index exam_semester_idx on exam (semester);

create table vote
(
    exam_id    uuid        not null references exam (id) on delete cascade,
    voter_id   uuid        not null,
    direction  varchar(4)  not null,
    created_at timestamptz not null default now(),
    -- one vote per browser (voter) and exam
    constraint vote_exam_voter_unique unique (exam_id, voter_id),
    constraint vote_direction_valid check (direction in ('UP', 'DOWN'))
);
