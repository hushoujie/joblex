create schema joblex;

use joblex;

create table blacklist
(
    id           int auto_increment
        primary key,
    candidate_id int          not null,
    recruiter_id int          not null,
    reason       varchar(255) null
);

create table users
(
    id              int auto_increment
        primary key,
    username        varchar(255) not null,
    password        varchar(255) not null,
    enabled         bit          not null,
    photo           varchar(255) null,
    name            varchar(255) null,
    headline        varchar(255) null,
    location        varchar(255) null,
    links           varchar(255) null,
    summary         varchar(255) null,
    certifications  varchar(255) null,
    skills          varchar(255) null,
    accomplishments varchar(255) null,
    interests       varchar(255) null,
    constraint users_username_uindex
        unique (username)
);

create table authorities
(
    id        int auto_increment
        primary key,
    username  varchar(255) not null,
    authority varchar(10)  not null,
    constraint authorities_username_uindex
        unique (username),
    constraint authorities_users_username_fk
        foreign key (username) references users (username)
);

create table education
(
    id         int auto_increment
        primary key,
    user_id    int           not null,
    school     varchar(255)  not null,
    field      varchar(255)  not null,
    degree     varchar(255)  not null,
    location   varchar(255)  not null,
    start_date date          null,
    end_date   date          null,
    summary    varchar(1000) null,
    constraint education_users_id_fk
        foreign key (user_id) references users (id)
);

create table experience
(
    id         int auto_increment
        primary key,
    user_id    int           not null,
    company    varchar(255)  not null,
    position   varchar(255)  not null,
    location   varchar(255)  not null,
    start_date date          null,
    end_date   date          null,
    summary    varchar(1000) null,
    user       tinyblob      null,
    constraint experience_users_id_fk
        foreign key (user_id) references users (id)
);

create table job
(
    id           int auto_increment
        primary key,
    user_id      int          not null,
    status       bit          not null,
    logo         varchar(255) not null,
    position     varchar(255) not null,
    company      varchar(255) not null,
    location     varchar(255) not null,
    description  text         null,
    activation   date         null,
    deactivation date         null,
    constraint job_users_id_fk
        foreign key (user_id) references users (id)
);

create table application
(
    id           int auto_increment
        primary key,
    job_id       int           not null,
    user_id      int           not null,
    cover_letter varchar(1000) null,
    status       int           not null,
    constraint application_job_id_fk
        foreign key (job_id) references job (id),
    constraint application_users_id_fk
        foreign key (user_id) references users (id)
);

create table saved_job
(
    id      int auto_increment
        primary key,
    job_id  int           not null,
    user_id int           not null,
    notes   varchar(1000) null,
    constraint saved_job_job_id_fk
        foreign key (job_id) references job (id),
    constraint saved_job_users_id_fk
        foreign key (user_id) references users (id)
);

create table token
(
    id          int auto_increment
        primary key,
    user_id     int          not null,
    token       varchar(255) not null,
    expiry_date datetime     not null,
    constraint token_users_id_fk
        foreign key (user_id) references users (id)
);

