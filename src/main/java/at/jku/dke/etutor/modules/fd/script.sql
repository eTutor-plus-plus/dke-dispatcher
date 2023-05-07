create sequence closure_id_seq1;

alter sequence closure_id_seq1 owner to etutor;

create sequence exercise_id_seq1;

alter sequence exercise_id_seq1 owner to etutor;

create table exercise
(
    relation text[]                                               not null,
    id       bigint default nextval('exercise_id_seq1'::regclass) not null
        primary key
);

alter table exercise
    owner to etutor;

alter sequence exercise_id_seq1 owned by exercise.id;

create table assignment
(
    id          bigint generated always as identity
        primary key,
    exercise_id bigint  not null,
    type        varchar not null,
    is_solved   boolean not null,
    is_exam     boolean not null,
    student_id  bigint
);

alter table assignment
    owner to etutor;

create table solution
(
    id                  bigint not null
        primary key,
    closure_combination text[],
    closure_results     text[]
);

alter table solution
    owner to etutor;

create table closure
(
    id          bigint generated always as identity
        constraint closure_pk
            primary key,
    exercise_id bigint not null
        constraint closure_exercise_id_fk
            references exercise
            on update cascade on delete cascade
        constraint fkihr5gqvuy67tpgjp2r6v37tk
            references exercise,
    left_side   text[] not null,
    right_side  text[] not null
);

alter table closure
    owner to etutor;

create table minimal_cover
(
    id          bigint generated always as identity
        constraint minimal_cover_pk
            primary key,
    left_side   text[] not null,
    right_side  text[] not null,
    reason      text,
    exercise_id bigint not null
        constraint minimal_cover_exercise_id_fk
            references exercise
            on update cascade on delete cascade
        constraint fk69jqn1yasn46k1ij3fseamcmy
            references exercise
);

alter table minimal_cover
    owner to etutor;

create table functionalDependency
(
    id               bigint generated always as identity
        primary key,
    left_side        text[] not null,
    right_side       text[] not null,
    exercise_id      bigint
        constraint dependency_exercise_id_fk
            references exercise
            on update cascade on delete cascade
        constraint fk5swex7s81wnas42mdjnpkob7t
            references exercise,
    minimal_cover_id bigint
        constraint dependency_minimal_cover_id_fk
            references minimal_cover
            on update cascade on delete cascade
);

alter table functionalDependency
    owner to etutor;

create table key
(
    id          bigint generated always as identity
        constraint key_pk
            primary key,
    left_side   text[] not null,
    right_side  text[] not null,
    exercise_id bigint not null
        constraint key_exercise_id_fk
            references exercise
            on update cascade on delete cascade
        constraint fkpjmplar1n11sahsy5xk4c3s1x
            references exercise
);

alter table key
    owner to etutor;

