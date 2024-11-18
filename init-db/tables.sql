-- Table: crwm.crawling_filter

-- DROP TABLE IF EXISTS crwm.crawling_filter;

CREATE TABLE IF NOT EXISTS crwm.crawling_filter
(
    id uuid NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default",
    created_date timestamp(6) without time zone,
    last_modified_by character varying(255) COLLATE pg_catalog."default",
    last_modified_date timestamp(6) without time zone,
    country character varying(255) COLLATE pg_catalog."default",
    department character varying(255) COLLATE pg_catalog."default",
    education_level character varying(255) COLLATE pg_catalog."default",
    school character varying(255) COLLATE pg_catalog."default",
    title character varying(255) COLLATE pg_catalog."default",
    workplace character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT crawling_filter_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS crwm.crawling_filter
    OWNER to postgres;

-- Table: crwm.crawling_master

-- DROP TABLE IF EXISTS crwm.crawling_master;

CREATE TABLE IF NOT EXISTS crwm.crawling_master
(
    id uuid NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default",
    created_date timestamp(6) without time zone,
    last_modified_by character varying(255) COLLATE pg_catalog."default",
    last_modified_date timestamp(6) without time zone,
    description character varying(255) COLLATE pg_catalog."default",
    recursive_depth integer NOT NULL,
    recursive_enable boolean NOT NULL,
    status smallint,
    target character varying(255) COLLATE pg_catalog."default",
    thread_count integer NOT NULL,
    type smallint,
    crawling_filter_id uuid,
    filters character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT crawling_master_pkey PRIMARY KEY (id),
    CONSTRAINT uk26g33m62ek3kd2p09u6gv4on2 UNIQUE (crawling_filter_id),
    CONSTRAINT fk1u47a4l5sjcnyq55o1nx4o190 FOREIGN KEY (crawling_filter_id)
        REFERENCES crwm.crawling_filter (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT crawling_master_type_check CHECK (type >= 0 AND type <= 4),
    CONSTRAINT crawling_master_status_check CHECK (status >= 0 AND status <= 3)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS crwm.crawling_master
    OWNER to postgres;

-- Table: crwm.crawling_result

-- DROP TABLE IF EXISTS crwm.crawling_result;

CREATE TABLE IF NOT EXISTS crwm.crawling_result
(
    id uuid NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default",
    created_date timestamp(6) without time zone,
    last_modified_by character varying(255) COLLATE pg_catalog."default",
    last_modified_date timestamp(6) without time zone,
    profile_id character varying(255) COLLATE pg_catalog."default",
    profile_master_id uuid,
    raw_data_result text COLLATE pg_catalog."default",
    full_name character varying(255) COLLATE pg_catalog."default",
    process_date timestamp(6) without time zone,
    crawl_date timestamp(6) without time zone,
    CONSTRAINT crawling_result_pkey PRIMARY KEY (id)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS crwm.crawling_result
    OWNER to postgres;

-- Table: crwm.linkedin_account

-- DROP TABLE IF EXISTS crwm.linkedin_account;

CREATE TABLE IF NOT EXISTS crwm.linkedin_account
(
    id uuid NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default",
    created_date timestamp(6) without time zone,
    last_modified_by character varying(255) COLLATE pg_catalog."default",
    last_modified_date timestamp(6) without time zone,
    password character varying(255) COLLATE pg_catalog."default",
    status smallint,
    username character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT linkedin_account_pkey PRIMARY KEY (id),
    CONSTRAINT linkedin_account_status_check CHECK (status >= 0 AND status <= 1)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS crwm.linkedin_account
    OWNER to postgres;

-- Table: crwm.proxy_server

-- DROP TABLE IF EXISTS crwm.proxy_server;

CREATE TABLE IF NOT EXISTS crwm.proxy_server
(
    id uuid NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default",
    created_date timestamp(6) without time zone,
    last_modified_by character varying(255) COLLATE pg_catalog."default",
    last_modified_date timestamp(6) without time zone,
    ip character varying(255) COLLATE pg_catalog."default",
    password character varying(255) COLLATE pg_catalog."default",
    port integer NOT NULL,
    status smallint,
    username character varying(255) COLLATE pg_catalog."default",
    type smallint,
    CONSTRAINT proxy_server_pkey PRIMARY KEY (id),
    CONSTRAINT proxy_server_type_check CHECK (type >= 0 AND type <= 1),
    CONSTRAINT proxy_server_status_check CHECK (status >= 0 AND status <= 1)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS crwm.proxy_server
    OWNER to postgres;

----------------------INFM seması-------------------------
-- Table: infm.award

-- DROP TABLE IF EXISTS infm.award;

CREATE TABLE IF NOT EXISTS infm.award
(
    id uuid NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default",
    created_date timestamp(6) without time zone,
    last_modified_by character varying(255) COLLATE pg_catalog."default",
    last_modified_date timestamp(6) without time zone,
    info text COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    linkedin_profile_id uuid,
    CONSTRAINT award_pkey PRIMARY KEY (id),
    CONSTRAINT fk2o0flu4e9avetnqucbnom1e0s FOREIGN KEY (linkedin_profile_id)
        REFERENCES infm.linkedin_profile (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS infm.award
    OWNER to postgres;

-- Table: infm.certification

-- DROP TABLE IF EXISTS infm.certification;

CREATE TABLE IF NOT EXISTS infm.certification
(
    id uuid NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default",
    created_date timestamp(6) without time zone,
    last_modified_by character varying(255) COLLATE pg_catalog."default",
    last_modified_date timestamp(6) without time zone,
    description text COLLATE pg_catalog."default",
    duration character varying(255) COLLATE pg_catalog."default",
    issuer character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    linkedin_profile_id uuid,
    CONSTRAINT certification_pkey PRIMARY KEY (id),
    CONSTRAINT fks146vfkn492k4cs4eulglwty7 FOREIGN KEY (linkedin_profile_id)
        REFERENCES infm.linkedin_profile (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS infm.certification
    OWNER to postgres;

-- Table: infm.education

-- DROP TABLE IF EXISTS infm.education;

CREATE TABLE IF NOT EXISTS infm.education
(
    id uuid NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default",
    created_date timestamp(6) without time zone,
    last_modified_by character varying(255) COLLATE pg_catalog."default",
    last_modified_date timestamp(6) without time zone,
    department character varying(255) COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    duration character varying(255) COLLATE pg_catalog."default",
    school_name character varying(255) COLLATE pg_catalog."default",
    linkedin_profile_id uuid,
    CONSTRAINT education_pkey PRIMARY KEY (id),
    CONSTRAINT fki08gqxe63k0pkb08um237vo5m FOREIGN KEY (linkedin_profile_id)
        REFERENCES infm.linkedin_profile (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS infm.education
    OWNER to postgres;

-- Table: infm.experience

-- DROP TABLE IF EXISTS infm.experience;

CREATE TABLE IF NOT EXISTS infm.experience
(
    id uuid NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default",
    created_date timestamp(6) without time zone,
    last_modified_by character varying(255) COLLATE pg_catalog."default",
    last_modified_date timestamp(6) without time zone,
    company character varying(255) COLLATE pg_catalog."default",
    description text COLLATE pg_catalog."default",
    duration character varying(255) COLLATE pg_catalog."default",
    location character varying(255) COLLATE pg_catalog."default",
    title character varying(255) COLLATE pg_catalog."default",
    linkedin_profile_id uuid,
    CONSTRAINT experience_pkey PRIMARY KEY (id),
    CONSTRAINT fk5kl8vn4hofel7fys6rw6pp8fx FOREIGN KEY (linkedin_profile_id)
        REFERENCES infm.linkedin_profile (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS infm.experience
    OWNER to postgres;

-- Table: infm.language

-- DROP TABLE IF EXISTS infm.language;

CREATE TABLE IF NOT EXISTS infm.language
(
    id uuid NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default",
    created_date timestamp(6) without time zone,
    last_modified_by character varying(255) COLLATE pg_catalog."default",
    last_modified_date timestamp(6) without time zone,
    info character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    linkedin_profile_id uuid,
    CONSTRAINT language_pkey PRIMARY KEY (id),
    CONSTRAINT fk35s7q7lfbx4306uf5lmw7eho7 FOREIGN KEY (linkedin_profile_id)
        REFERENCES infm.linkedin_profile (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS infm.language
    OWNER to postgres;

-- Table: infm.linkedin_profile

-- DROP TABLE IF EXISTS infm.linkedin_profile;

CREATE TABLE IF NOT EXISTS infm.linkedin_profile
(
    id uuid NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default",
    created_date timestamp(6) without time zone,
    last_modified_by character varying(255) COLLATE pg_catalog."default",
    last_modified_date timestamp(6) without time zone,
    about text COLLATE pg_catalog."default",
    connection_count integer NOT NULL,
    crawl_date timestamp(6) without time zone,
    full_name character varying(255) COLLATE pg_catalog."default",
    headline character varying(255) COLLATE pg_catalog."default",
    location character varying(255) COLLATE pg_catalog."default",
    profile_id character varying(255) COLLATE pg_catalog."default",
    profile_picture_url character varying(255) COLLATE pg_catalog."default",
    status smallint,
    CONSTRAINT linkedin_profile_pkey PRIMARY KEY (id),
    CONSTRAINT linkedin_profile_status_check CHECK (status >= 0 AND status <= 1)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS infm.linkedin_profile
    OWNER to postgres;

-- Table: infm.skill

-- DROP TABLE IF EXISTS infm.skill;

CREATE TABLE IF NOT EXISTS infm.skill
(
    id uuid NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default",
    created_date timestamp(6) without time zone,
    last_modified_by character varying(255) COLLATE pg_catalog."default",
    last_modified_date timestamp(6) without time zone,
    approve_count character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    organisation character varying(255) COLLATE pg_catalog."default",
    linkedin_profile_id uuid,
    CONSTRAINT skill_pkey PRIMARY KEY (id),
    CONSTRAINT fkgnb7nrmd0037y8gebwn0drs3d FOREIGN KEY (linkedin_profile_id)
        REFERENCES infm.linkedin_profile (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS infm.skill
    OWNER to postgres;

-- Table: infm.volunteering

-- DROP TABLE IF EXISTS infm.volunteering;

CREATE TABLE IF NOT EXISTS infm.volunteering
(
    id uuid NOT NULL,
    created_by character varying(255) COLLATE pg_catalog."default",
    created_date timestamp(6) without time zone,
    last_modified_by character varying(255) COLLATE pg_catalog."default",
    last_modified_date timestamp(6) without time zone,
    description text COLLATE pg_catalog."default",
    duration character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    organisation character varying(255) COLLATE pg_catalog."default",
    linkedin_profile_id uuid,
    CONSTRAINT volunteering_pkey PRIMARY KEY (id),
    CONSTRAINT fkt33jv57mobpu6lag721ceend6 FOREIGN KEY (linkedin_profile_id)
        REFERENCES infm.linkedin_profile (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS infm.volunteering
    OWNER to postgres;