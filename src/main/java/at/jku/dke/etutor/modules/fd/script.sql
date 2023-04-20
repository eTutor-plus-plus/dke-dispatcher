create table exercise
(
    id       bigint generated always as identity
        primary key,
    relation text[] not null
);

alter table exercise
    owner to etutor;

create table dependency
(
    id                  bigint generated always as identity
        primary key,
    left_side           text[] not null,
    right_side          text[] not null,
    violated_normalform text,
    has_redundant_part  boolean,
    is_trivial          boolean
);

alter table dependency
    owner to etutor;

create table assignment
(
    student_id  bigint  not null,
    exercise_id bigint  not null
        constraint exercise_id
            references exercise,
    type        varchar not null,
    is_solved   boolean not null,
    is_exam     boolean not null,
    primary key (student_id, exercise_id)
);

alter table assignment
    owner to etutor;

create table solution
(
    id                  bigint not null
        primary key
        constraint id
            references exercise,
    keys                text[] not null,
    closure_combination text[],
    closure_results     text[]
);

alter table solution
    owner to etutor;

create table exercise_depencencies
(
    exercise_id   bigint not null
        constraint exercise
            references exercise,
    dependency_id bigint not null
        constraint dependency
            references dependency,
    constraint exercise_to_depencencies_pkey
        primary key (exercise_id, dependency_id)
);

alter table exercise_depencencies
    owner to etutor;

