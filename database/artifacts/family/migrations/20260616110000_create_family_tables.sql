CREATE SCHEMA IF NOT EXISTS family;

CREATE TABLE family.family (
    id uuid NOT NULL,
    name character varying(120) NOT NULL,
    currency character varying(3) NOT NULL,
    created_at timestamp with time zone NOT NULL,
    disabled_at timestamp with time zone NULL,
    CONSTRAINT family_pkey PRIMARY KEY (id),
    CONSTRAINT family_name_not_blank CHECK (char_length(btrim(name)) > 0),
    CONSTRAINT family_currency_iso_code CHECK (currency ~ '^[A-Z]{3}$')
);

CREATE INDEX idx_family_active_created_at
    ON family.family (created_at)
    WHERE disabled_at IS NULL;

CREATE TABLE family.family_member (
    id uuid NOT NULL,
    family_id uuid NOT NULL,
    name character varying(120) NOT NULL,
    member_type character varying(20) NOT NULL,
    birth_date date NULL,
    created_at timestamp with time zone NOT NULL,
    disabled_at timestamp with time zone NULL,
    CONSTRAINT family_member_pkey PRIMARY KEY (id),
    CONSTRAINT fk_family_member_family FOREIGN KEY (family_id)
        REFERENCES family.family (id)
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT family_member_name_not_blank CHECK (char_length(btrim(name)) > 0),
    CONSTRAINT family_member_type_valid CHECK (
        member_type IN ('ADULT', 'CHILD', 'DEPENDENT', 'OTHER')
    )
);

CREATE INDEX idx_family_member_family_active_created_at
    ON family.family_member (family_id, created_at)
    WHERE disabled_at IS NULL;
