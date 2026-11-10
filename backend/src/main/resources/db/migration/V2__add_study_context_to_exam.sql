-- Where the exam was written: university (required), faculty, study program and degree (optional)

alter table exam add column university varchar(150);
-- rows from before this migration have no university
update exam set university = 'Unbekannt' where university is null;
alter table exam alter column university set not null;

alter table exam add column faculty varchar(100);
alter table exam add column study_program varchar(100);
alter table exam add column degree varchar(20);

alter table exam
    add constraint exam_degree_valid check (degree in ('BACHELOR', 'MASTER', 'STAATSEXAMEN'));
