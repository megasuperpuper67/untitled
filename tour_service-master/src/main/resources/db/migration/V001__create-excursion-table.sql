CREATE TABLE IF NOT EXISTS public.excursion
(
    id serial NOT NULL,
    name character varying(255) NOT NULL,
    price numeric(12,2) NOT NULL,
    "from" date NOT NULL,
    "to" date NOT NULL,
    guide_name character varying(255) NOT NULL,
    excursion_type character varying(255) NOT NULL,
    lunch_included boolean NOT NULL DEFAULT false,
    CONSTRAINT excursion_pkey PRIMARY KEY (id)
)