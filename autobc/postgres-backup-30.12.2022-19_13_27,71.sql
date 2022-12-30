--
-- PostgreSQL database dump
--

-- Dumped from database version 14.5
-- Dumped by pg_dump version 14.5

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: bds; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA bds;


ALTER SCHEMA bds OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: actor; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.actor (
    actor_id integer NOT NULL,
    actor_given_name character varying(45) NOT NULL,
    actor_family_name character varying(45) NOT NULL,
    actor_date_of_birth date,
    number_of_oscars integer
);


ALTER TABLE bds.actor OWNER TO postgres;

--
-- Name: actor_actor_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.actor_actor_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bds.actor_actor_id_seq OWNER TO postgres;

--
-- Name: actor_actor_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.actor_actor_id_seq OWNED BY bds.actor.actor_id;


--
-- Name: category; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.category (
    category_id integer NOT NULL,
    category_title character varying(45) NOT NULL
);


ALTER TABLE bds.category OWNER TO postgres;

--
-- Name: category_category_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.category_category_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bds.category_category_id_seq OWNER TO postgres;

--
-- Name: category_category_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.category_category_id_seq OWNED BY bds.category.category_id;


--
-- Name: director; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.director (
    director_id integer NOT NULL,
    director_given_name character varying(45) NOT NULL,
    director_family_name character varying(45) NOT NULL
);


ALTER TABLE bds.director OWNER TO postgres;

--
-- Name: director_director_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.director_director_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bds.director_director_id_seq OWNER TO postgres;

--
-- Name: director_director_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.director_director_id_seq OWNED BY bds.director.director_id;


--
-- Name: genre; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.genre (
    genre_id integer NOT NULL,
    genre_name character varying(45) NOT NULL
);


ALTER TABLE bds.genre OWNER TO postgres;

--
-- Name: genre_genre_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.genre_genre_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bds.genre_genre_id_seq OWNER TO postgres;

--
-- Name: genre_genre_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.genre_genre_id_seq OWNED BY bds.genre.genre_id;


--
-- Name: login; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.login (
    user_id integer NOT NULL,
    user_name character varying(30) NOT NULL,
    user_password character varying(256) NOT NULL
);


ALTER TABLE bds.login OWNER TO postgres;

--
-- Name: membership_card; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.membership_card (
    card_id integer NOT NULL,
    user_id integer NOT NULL,
    card_issue_date timestamp without time zone NOT NULL,
    card_exp_date timestamp without time zone NOT NULL
);


ALTER TABLE bds.membership_card OWNER TO postgres;

--
-- Name: membership_card_card_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.membership_card_card_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bds.membership_card_card_id_seq OWNER TO postgres;

--
-- Name: membership_card_card_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.membership_card_card_id_seq OWNED BY bds.membership_card.card_id;


--
-- Name: movie; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.movie (
    movie_id integer NOT NULL,
    movie_name character varying(45) NOT NULL,
    duration integer NOT NULL,
    director_id integer,
    release_date date NOT NULL,
    country_of_origin character varying(45)
);


ALTER TABLE bds.movie OWNER TO postgres;

--
-- Name: projection; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.projection (
    projection_id integer NOT NULL,
    room_id integer NOT NULL,
    movie_id integer,
    projection_start timestamp without time zone NOT NULL,
    vip_projection boolean,
    "3D_projection" boolean
);


ALTER TABLE bds.projection OWNER TO postgres;

--
-- Name: reservation; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.reservation (
    reservation_id integer NOT NULL,
    user_id integer,
    projection_id integer,
    is_active boolean NOT NULL,
    reservation_date timestamp without time zone NOT NULL
);


ALTER TABLE bds.reservation OWNER TO postgres;

--
-- Name: most_attended_projections; Type: MATERIALIZED VIEW; Schema: bds; Owner: postgres
--

CREATE MATERIALIZED VIEW bds.most_attended_projections AS
 SELECT m.movie_name,
    count(r.reservation_id) AS number_of_attendants,
    p.projection_id,
    p.projection_start
   FROM ((bds.reservation r
     JOIN bds.projection p ON ((r.projection_id = p.projection_id)))
     JOIN bds.movie m ON ((p.movie_id = m.movie_id)))
  GROUP BY m.movie_name, p.projection_id, p.projection_start
 HAVING (count(r.reservation_id) > 0)
  ORDER BY (count(r.reservation_id)) DESC
  WITH NO DATA;


ALTER TABLE bds.most_attended_projections OWNER TO postgres;

--
-- Name: movie_has_actor; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.movie_has_actor (
    actor_id integer,
    movie_id integer
);


ALTER TABLE bds.movie_has_actor OWNER TO postgres;

--
-- Name: movie_has_genre; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.movie_has_genre (
    movie_id integer,
    genre_id integer
);


ALTER TABLE bds.movie_has_genre OWNER TO postgres;

--
-- Name: movie_movie_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.movie_movie_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bds.movie_movie_id_seq OWNER TO postgres;

--
-- Name: movie_movie_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.movie_movie_id_seq OWNED BY bds.movie.movie_id;


--
-- Name: price; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.price (
    projection_id integer NOT NULL,
    category_id integer NOT NULL,
    price numeric NOT NULL
);


ALTER TABLE bds.price OWNER TO postgres;

--
-- Name: projection_projection_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.projection_projection_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bds.projection_projection_id_seq OWNER TO postgres;

--
-- Name: projection_projection_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.projection_projection_id_seq OWNED BY bds.projection.projection_id;


--
-- Name: reservation_reservation_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.reservation_reservation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bds.reservation_reservation_id_seq OWNER TO postgres;

--
-- Name: reservation_reservation_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.reservation_reservation_id_seq OWNED BY bds.reservation.reservation_id;


--
-- Name: reserved_seat; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.reserved_seat (
    reservation_id integer NOT NULL,
    seat_id integer NOT NULL,
    category_id integer NOT NULL
);


ALTER TABLE bds.reserved_seat OWNER TO postgres;

--
-- Name: role; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.role (
    role_id integer NOT NULL,
    role character varying(45) NOT NULL
);


ALTER TABLE bds.role OWNER TO postgres;

--
-- Name: role_role_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.role_role_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bds.role_role_id_seq OWNER TO postgres;

--
-- Name: role_role_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.role_role_id_seq OWNED BY bds.role.role_id;


--
-- Name: room; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.room (
    room_id integer NOT NULL,
    room_name character varying(45) NOT NULL,
    capacity integer NOT NULL
);


ALTER TABLE bds.room OWNER TO postgres;

--
-- Name: room_room_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.room_room_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bds.room_room_id_seq OWNER TO postgres;

--
-- Name: room_room_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.room_room_id_seq OWNED BY bds.room.room_id;


--
-- Name: user; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds."user" (
    user_id integer NOT NULL,
    given_name character varying(45) NOT NULL,
    family_name character varying(45) NOT NULL,
    is_adult boolean NOT NULL,
    email character varying(45),
    phone_number character varying(20)
);


ALTER TABLE bds."user" OWNER TO postgres;

--
-- Name: user_has_role; Type: TABLE; Schema: bds; Owner: postgres
--

CREATE TABLE bds.user_has_role (
    user_id integer,
    role_id integer,
    expiration_date date
);


ALTER TABLE bds.user_has_role OWNER TO postgres;

--
-- Name: user_user_id_seq; Type: SEQUENCE; Schema: bds; Owner: postgres
--

CREATE SEQUENCE bds.user_user_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bds.user_user_id_seq OWNER TO postgres;

--
-- Name: user_user_id_seq; Type: SEQUENCE OWNED BY; Schema: bds; Owner: postgres
--

ALTER SEQUENCE bds.user_user_id_seq OWNED BY bds."user".user_id;


--
-- Name: dummy_table; Type: TABLE; Schema: public; Owner: dbs
--

CREATE TABLE public.dummy_table (
    string character varying
);


ALTER TABLE public.dummy_table OWNER TO dbs;

--
-- Name: actor actor_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.actor ALTER COLUMN actor_id SET DEFAULT nextval('bds.actor_actor_id_seq'::regclass);


--
-- Name: category category_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.category ALTER COLUMN category_id SET DEFAULT nextval('bds.category_category_id_seq'::regclass);


--
-- Name: director director_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.director ALTER COLUMN director_id SET DEFAULT nextval('bds.director_director_id_seq'::regclass);


--
-- Name: genre genre_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.genre ALTER COLUMN genre_id SET DEFAULT nextval('bds.genre_genre_id_seq'::regclass);


--
-- Name: membership_card card_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.membership_card ALTER COLUMN card_id SET DEFAULT nextval('bds.membership_card_card_id_seq'::regclass);


--
-- Name: movie movie_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.movie ALTER COLUMN movie_id SET DEFAULT nextval('bds.movie_movie_id_seq'::regclass);


--
-- Name: projection projection_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.projection ALTER COLUMN projection_id SET DEFAULT nextval('bds.projection_projection_id_seq'::regclass);


--
-- Name: reservation reservation_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.reservation ALTER COLUMN reservation_id SET DEFAULT nextval('bds.reservation_reservation_id_seq'::regclass);


--
-- Name: role role_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.role ALTER COLUMN role_id SET DEFAULT nextval('bds.role_role_id_seq'::regclass);


--
-- Name: room room_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.room ALTER COLUMN room_id SET DEFAULT nextval('bds.room_room_id_seq'::regclass);


--
-- Name: user user_id; Type: DEFAULT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds."user" ALTER COLUMN user_id SET DEFAULT nextval('bds.user_user_id_seq'::regclass);


--
-- Data for Name: actor; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.actor (actor_id, actor_given_name, actor_family_name, actor_date_of_birth, number_of_oscars) FROM stdin;
1	Vince	Sherratt	1979-01-13	0
2	Gwenora	Benwell	1989-11-30	2
3	Hunt	Golda	1990-05-17	4
4	Sybille	Leatherbarrow	1984-10-27	1
5	Ali	Mandy	2004-01-15	0
6	Harlen	Rookes	1998-04-04	1
7	Marrilee	McCrory	1982-04-19	0
8	Giustino	Fishbourne	1975-08-01	0
9	Antonio	Saffin	1983-04-23	0
10	Jasmine	Tremolieres	1976-06-07	2
11	Paolina	Axtell	1982-11-12	0
12	Devina	Genery	1972-07-29	1
13	Elvis	Richardson	1989-02-24	2
\.


--
-- Data for Name: category; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.category (category_id, category_title) FROM stdin;
1	adult
2	student
3	child
4	senior
5	blank1
\.


--
-- Data for Name: director; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.director (director_id, director_given_name, director_family_name) FROM stdin;
1	Forster	Imore
2	Gwyneth	Geeritz
3	Cammi	Grealish
4	Israel	Shenton
5	Marisa	Binns
6	Stephi	Drust
7	Jameson	Lickermore
8	Mark	Zuc
\.


--
-- Data for Name: genre; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.genre (genre_id, genre_name) FROM stdin;
1	drama
2	comedy
3	romance
4	science fiction
5	action
6	fantasy
7	horror
8	thriller
9	mystery
10	western
\.


--
-- Data for Name: login; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.login (user_id, user_name, user_password) FROM stdin;
1	tdecourtney0	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
2	kfarlamb1	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
3	mfairman2	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
4	aslott3	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
5	hvesque4	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
6	hfitzpayn5	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
7	cdameisele6	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
8	dpicheford7	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
9	cbiagini8	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
10	rbrookz9	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
11	sclementuccia	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
12	cmacalpyneb	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
13	tzanazzic	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
14	bbarded	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
15	mschulze	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
16	kswanwickf	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
17	ibrettleg	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
18	jtonksh	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
19	hkollatschi	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
20	sscandrootj	$argon2id$v=19$m=65536,t=22,p=1$IA8LCjpq/l9lNtiHb9Nydg$6j4/X6oDAtl2PTEbCUjC55kO7aL6fDQlRG9xpAOiSQgNNWm3u6uTksOyMoT3cxdP0KeA/sSzMu5DUNeuTrdTaw
21	manager	$argon2id$v=19$m=65536,t=22,p=1$Ku9dzlz7xSD90sCUIKsYSw$ePJtl1j3oNCpx687sVL7T4BEwRj51hoHzA+4dDn+XQK09/nuPF9dXWWIsREm5f9xKMF0sIIsHDSoPKoyy9Vxmw
\.


--
-- Data for Name: membership_card; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.membership_card (card_id, user_id, card_issue_date, card_exp_date) FROM stdin;
1	15	2022-06-10 18:12:36	2023-06-24 01:48:59
2	2	2021-11-25 05:42:29	2023-05-16 19:36:45
3	18	2022-05-12 13:14:27	2022-05-03 13:20:01
4	20	2022-09-22 01:29:37	2022-04-05 07:44:18
5	14	2022-08-20 18:40:57	2021-12-03 18:12:23
6	2	2022-08-09 08:41:32	2022-01-30 07:04:42
7	8	2021-12-14 12:06:06	2022-01-07 01:51:38
8	5	2022-02-13 18:24:32	2022-05-23 09:52:31
9	11	2022-08-30 08:23:27	2023-05-09 01:44:05
10	19	2022-10-23 06:45:25	2022-03-20 15:39:33
11	15	2022-08-20 07:36:43	2023-04-14 11:19:00
12	8	2022-08-13 05:36:24	2022-03-01 07:50:06
13	6	2022-03-04 19:56:03	2022-01-31 06:33:52
14	13	2022-05-01 14:41:17	2022-03-01 07:15:33
15	8	2022-01-08 08:51:41	2021-12-21 19:54:52
16	19	2022-05-14 04:22:02	2023-03-09 02:19:23
17	17	2022-01-22 17:46:52	2023-06-21 13:23:55
18	15	2022-04-26 02:30:28	2022-06-18 12:31:32
19	14	2022-04-30 04:10:11	2023-07-09 12:36:30
20	5	2022-03-11 22:42:27	2022-11-03 09:16:28
\.


--
-- Data for Name: movie; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.movie (movie_id, movie_name, duration, director_id, release_date, country_of_origin) FROM stdin;
1	Critters 2: The Main Course	130	1	2020-02-15	China
2	Torment	137	5	2008-08-29	Russia
3	Mob, The	101	4	2008-12-21	China
4	Beauty Shop	60	5	1992-10-21	Portugal
5	Men in White	90	1	1987-11-12	Poland
6	Bedrooms & Hallways	133	2	1995-07-01	China
7	Killer Movie	94	4	2013-02-19	Peru
8	Marva Collins Story, The	104	5	2018-02-23	Uruguay
9	Holidaze	74	1	1995-05-18	Philippines
10	Quatermass Xperiment, The	78	4	2002-04-19	Mongolia
11	Torment 2	145	5	2009-08-29	Russia
12	Torment of souls	112	5	2010-08-29	Russia
13	Koyaanisqatsi	100	1	1982-02-19	Japan
14	Gummo	53	1	1988-07-11	USA
\.


--
-- Data for Name: movie_has_actor; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.movie_has_actor (actor_id, movie_id) FROM stdin;
2	10
10	9
6	8
3	5
8	4
12	3
1	10
3	8
5	1
8	8
4	10
7	2
5	4
1	2
2	8
12	6
12	9
9	1
10	4
9	7
\.


--
-- Data for Name: movie_has_genre; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.movie_has_genre (movie_id, genre_id) FROM stdin;
3	4
4	6
1	6
9	5
5	1
8	8
5	5
2	5
7	4
6	2
5	4
5	3
6	1
10	6
3	5
5	7
\.


--
-- Data for Name: price; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.price (projection_id, category_id, price) FROM stdin;
1	1	202.00
1	2	171.70
1	3	151.50
1	4	141.40
2	1	222.20
2	2	191.90
2	3	171.70
2	4	161.60
3	1	212.10
3	2	181.80
3	3	161.60
3	4	151.50
4	1	202.00
4	2	171.70
4	3	151.50
4	4	141.40
5	1	232.30
5	2	202.00
5	3	181.80
5	4	171.70
6	1	202.00
6	2	171.70
6	3	151.50
6	4	141.40
7	1	191.90
7	2	161.60
7	3	141.40
7	4	131.30
8	1	202.00
8	2	171.70
8	3	151.50
8	4	141.40
9	1	212.10
9	2	181.80
9	3	161.60
9	4	151.50
10	1	202.00
10	2	171.70
10	3	151.50
10	4	141.40
11	1	222.20
11	2	191.90
11	3	171.70
11	4	161.60
12	1	202.00
12	2	171.70
12	3	151.50
12	4	141.40
13	1	212.10
13	2	181.80
13	3	161.60
13	4	151.50
14	1	202.00
14	2	171.70
14	3	151.50
14	4	141.40
15	1	222.20
15	2	191.90
15	3	171.70
15	4	161.60
16	1	212.10
16	2	181.80
16	3	161.60
16	4	151.50
17	1	202.00
17	2	171.70
17	3	151.50
17	4	141.40
18	1	252.50
18	2	222.20
18	3	202.00
18	4	191.90
19	1	202.00
19	2	171.70
19	3	151.50
19	4	141.40
20	1	212.10
20	2	181.80
20	3	161.60
20	4	151.50
21	1	202.00
21	2	171.70
21	3	151.50
21	4	141.40
22	1	212.10
22	2	181.80
22	3	161.60
22	4	151.50
23	1	202.00
23	2	171.70
23	3	151.50
23	4	141.40
24	1	222.20
24	2	191.90
24	3	171.70
24	4	161.60
25	1	202.00
25	2	171.70
25	3	151.50
25	4	141.40
26	1	212.10
26	2	181.80
26	3	161.60
26	4	151.50
27	1	202.00
27	2	171.70
27	3	151.50
27	4	141.40
28	1	191.90
28	2	161.60
28	3	141.40
28	4	131.30
29	1	202.00
29	2	171.70
29	3	151.50
29	4	141.40
30	1	222.20
30	2	191.90
30	3	171.70
30	4	161.60
\.


--
-- Data for Name: projection; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.projection (projection_id, room_id, movie_id, projection_start, vip_projection, "3D_projection") FROM stdin;
1	1	7	2022-11-08 21:30:00	f	f
2	3	1	2022-11-16 22:00:00	t	f
3	5	8	2022-09-14 11:20:00	f	t
4	1	5	2022-10-02 11:40:00	f	f
5	1	7	2022-09-23 21:30:00	f	t
6	5	5	2022-08-10 21:45:00	f	t
7	1	1	2022-09-24 11:45:00	t	f
8	5	1	2022-11-14 15:00:00	f	f
9	5	3	2022-10-16 17:40:00	f	f
10	1	9	2022-12-23 10:45:00	f	t
11	3	8	2022-10-30 13:50:00	f	f
12	2	9	2022-10-21 11:00:00	f	f
13	1	2	2022-11-26 14:40:00	f	f
14	5	6	2022-09-17 10:30:00	f	f
15	5	2	2022-12-25 13:00:00	f	f
16	5	6	2022-08-22 11:45:00	f	t
17	1	4	2022-08-30 22:00:00	f	f
18	4	4	2022-09-29 21:30:00	t	t
19	2	8	2022-11-07 09:00:00	f	f
20	1	10	2022-11-13 12:40:00	f	f
21	3	8	2022-11-07 21:00:00	f	t
22	5	10	2022-10-10 18:30:00	f	f
23	1	8	2022-10-01 15:00:00	f	f
24	1	5	2022-08-13 09:45:00	f	t
25	2	7	2022-08-20 12:55:00	f	t
26	1	6	2022-10-12 09:15:00	t	f
27	3	10	2022-12-19 22:00:00	f	t
28	1	4	2022-09-17 12:00:00	f	t
29	4	2	2022-10-24 20:30:00	f	f
30	4	8	2022-10-28 17:30:00	f	t
31	1	5	2022-11-07 09:45:00	f	t
32	2	7	2022-11-07 12:55:00	f	t
33	1	6	2022-11-07 09:15:00	t	f
34	3	10	2022-11-07 22:00:00	f	t
35	1	4	2022-11-07 12:00:00	f	t
36	4	2	2022-11-07 20:30:00	f	f
37	4	8	2022-11-07 17:30:00	f	t
38	1	2	2022-12-26 14:40:00	f	f
39	5	6	2022-10-17 10:30:00	f	f
40	5	2	2022-11-25 13:00:00	f	f
41	5	6	2022-09-22 11:45:00	f	t
42	1	4	2022-09-30 22:00:00	f	f
43	4	4	2022-10-29 21:30:00	t	t
44	2	8	2022-12-07 09:00:00	f	f
45	1	10	2022-11-13 12:40:00	f	f
46	4	5	2022-09-10 21:50:00	f	f
47	4	5	2022-09-10 21:45:00	f	f
\.


--
-- Data for Name: reservation; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.reservation (reservation_id, user_id, projection_id, is_active, reservation_date) FROM stdin;
1	19	13	t	2022-07-17 12:08:34
2	13	10	t	2022-06-26 08:48:34
3	17	4	t	2022-06-20 05:39:28
4	10	10	t	2022-06-19 11:53:37
5	9	26	t	2022-06-01 10:21:21
6	12	2	t	2022-06-02 02:38:07
7	12	11	t	2022-06-11 02:12:51
8	12	19	t	2022-07-11 07:25:34
9	6	21	t	2022-07-23 06:01:54
10	6	11	t	2022-06-15 06:45:20
11	15	13	t	2022-06-06 15:53:31
12	9	26	t	2022-06-28 23:40:23
13	13	18	t	2022-07-26 16:16:06
14	15	3	t	2022-06-18 07:52:40
15	16	4	t	2022-07-03 00:12:14
16	8	17	t	2022-06-06 14:28:13
17	8	25	t	2022-07-11 03:35:21
18	8	12	t	2022-06-08 23:03:00
19	10	7	t	2022-06-09 00:13:47
20	7	3	t	2022-07-06 12:17:01
21	11	11	t	2022-06-08 23:20:34
22	19	19	t	2022-06-30 02:45:43
23	8	4	t	2022-07-29 12:23:32
24	8	11	t	2022-06-24 03:36:47
25	15	16	t	2022-07-10 15:40:45
26	8	24	t	2022-07-15 19:21:52
27	10	13	t	2022-07-26 04:45:49
28	7	14	t	2022-06-11 13:42:28
29	11	17	t	2022-06-29 21:48:50
30	20	9	t	2022-06-15 09:34:50
31	14	14	t	2022-07-08 08:34:45
32	19	12	t	2022-06-09 06:14:48
33	17	13	t	2022-07-12 03:45:45
34	6	15	t	2022-07-03 09:20:12
35	17	20	t	2022-07-02 09:09:19
36	5	16	t	2022-06-19 11:39:25
37	20	26	t	2022-06-24 06:30:33
38	9	3	t	2022-06-23 12:06:40
39	8	13	t	2022-07-25 08:28:26
40	17	11	t	2022-07-21 22:55:06
41	15	20	t	2022-06-11 07:36:14
42	19	15	t	2022-07-09 16:38:01
43	7	9	t	2022-06-20 19:57:49
44	10	25	t	2022-06-26 20:26:24
45	12	12	t	2022-07-12 09:16:14
46	9	3	t	2022-06-20 06:48:46
47	8	30	t	2022-06-04 01:57:41
48	9	16	t	2022-07-22 02:58:59
49	20	30	t	2022-06-16 21:03:05
50	18	30	t	2022-06-13 01:48:10
51	16	22	t	2022-07-26 04:26:06
52	12	14	t	2022-06-11 20:22:31
53	17	26	t	2022-07-20 14:28:34
54	17	29	t	2022-07-25 19:52:30
55	10	15	t	2022-06-09 03:07:03
56	19	19	t	2022-07-14 19:52:28
57	16	18	t	2022-07-20 07:58:41
58	7	4	t	2022-07-21 05:21:17
59	5	27	t	2022-06-23 08:34:44
60	19	12	t	2022-07-08 11:22:35
61	13	11	t	2022-07-06 23:12:30
62	14	12	t	2022-06-23 20:54:02
63	11	13	t	2022-07-11 14:33:39
64	15	13	t	2022-06-17 02:57:39
65	18	11	t	2022-07-17 16:18:36
66	16	11	t	2022-06-05 11:36:57
67	8	30	t	2022-07-29 15:31:10
68	13	23	t	2022-06-08 22:46:58
69	10	29	t	2022-07-20 14:43:27
70	20	28	t	2022-06-01 17:14:01
71	7	10	t	2022-07-01 04:03:50
72	7	2	t	2022-07-16 04:13:39
73	5	28	t	2022-07-20 05:34:19
74	18	20	t	2022-06-20 00:26:22
75	9	22	t	2022-07-06 02:56:38
76	17	4	t	2022-06-08 11:25:12
77	11	15	t	2022-06-15 03:25:02
78	17	9	t	2022-06-09 03:07:54
79	20	2	t	2022-07-23 02:01:11
80	11	5	t	2022-06-22 10:43:15
81	11	5	t	2022-06-02 12:32:56
82	18	5	t	2022-06-14 09:37:13
83	12	25	t	2022-06-19 07:37:20
84	12	29	t	2022-07-20 14:07:50
85	8	13	t	2022-06-18 17:33:47
86	5	19	t	2022-07-05 02:53:21
87	14	28	t	2022-07-21 17:39:00
88	11	20	t	2022-07-02 14:30:19
89	15	16	t	2022-06-20 18:10:30
90	13	24	t	2022-06-14 07:36:20
91	7	26	t	2022-07-15 05:33:19
92	9	4	t	2022-06-16 21:50:09
93	9	15	t	2022-07-02 05:24:09
94	19	10	t	2022-07-07 10:58:46
95	9	2	t	2022-06-28 23:53:08
96	17	6	t	2022-06-10 18:41:10
97	20	13	t	2022-06-19 20:52:44
98	17	5	t	2022-07-06 05:03:18
99	18	27	t	2022-07-22 12:21:41
100	20	15	t	2022-07-13 03:39:39
101	19	13	t	2022-11-05 09:26:04
102	15	10	t	2022-11-05 08:48:34
103	14	4	t	2022-11-04 17:53:28
104	20	10	t	2022-11-05 08:53:37
105	3	26	t	2022-11-04 10:21:21
106	6	11	t	2022-10-15 06:45:20
107	15	13	t	2022-10-06 15:53:31
108	9	26	t	2022-10-28 23:40:23
109	13	18	t	2022-10-26 16:16:06
110	15	3	t	2022-10-18 07:52:40
111	16	4	t	2022-10-03 00:12:14
112	8	17	t	2022-10-06 14:28:13
113	7	31	t	2022-07-15 05:33:19
114	9	32	t	2022-06-16 21:50:09
115	9	33	t	2022-07-02 05:24:09
116	19	31	t	2022-07-07 10:58:46
117	9	34	t	2022-06-28 23:53:08
118	17	35	t	2022-06-10 18:41:10
119	20	36	t	2022-06-19 20:52:44
120	17	37	t	2022-07-06 05:03:18
121	18	36	t	2022-07-22 12:21:41
123	1	1	t	2022-09-11 21:00:00
124	1	1	t	2022-11-11 21:00:00
125	1	1	t	2022-11-11 21:00:00
126	1	1	t	2022-11-11 21:00:00
127	5	6	t	2021-11-08 14:14:14
128	1	1	t	2020-12-20 12:12:13
\.


--
-- Data for Name: reserved_seat; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.reserved_seat (reservation_id, seat_id, category_id) FROM stdin;
1	84	2
2	55	1
3	160	3
4	35	2
5	114	2
6	112	1
7	43	2
8	61	1
9	141	2
10	10	3
11	153	3
12	25	3
13	174	3
14	18	4
15	152	3
16	140	1
17	24	2
18	15	3
19	63	1
20	12	3
21	43	1
22	164	1
23	72	4
24	53	3
25	133	1
26	152	1
27	12	4
28	113	1
29	11	3
30	30	1
31	95	4
32	146	1
33	61	4
34	69	3
35	102	2
36	153	2
37	83	3
38	86	1
39	35	2
40	114	4
41	45	3
42	146	4
43	36	2
44	51	2
45	100	4
46	76	3
47	148	4
48	155	4
49	101	4
50	121	3
51	175	3
52	79	1
53	174	4
54	15	2
55	65	2
56	150	3
57	146	2
58	136	4
59	137	2
60	107	2
61	146	3
62	176	3
63	87	1
64	107	3
65	143	3
66	65	2
67	113	4
68	119	3
69	132	4
70	148	2
71	32	4
72	67	4
73	14	2
74	75	3
75	128	1
76	57	2
77	74	1
78	8	1
79	104	1
80	140	2
81	77	1
82	2	2
83	159	3
84	53	4
85	63	1
86	4	3
87	61	1
88	19	3
89	173	3
90	87	2
91	61	1
92	134	3
93	145	1
94	144	3
95	70	1
96	31	3
97	106	2
98	12	2
99	134	1
100	9	4
72	82	2
74	62	1
57	40	1
91	98	4
84	89	4
40	164	3
62	148	1
64	159	4
68	179	3
65	173	3
64	124	3
9	162	2
81	27	3
2	54	4
78	1	1
32	107	3
98	147	1
43	133	2
73	128	1
73	161	2
91	51	2
92	115	2
21	145	1
38	177	3
51	30	3
69	120	1
58	29	1
71	114	4
31	23	2
31	22	1
65	130	1
58	116	3
91	5	4
10	58	2
9	128	3
57	67	1
35	162	2
30	66	1
71	78	3
86	40	2
40	93	2
57	38	1
7	17	1
85	52	4
76	12	4
81	164	4
48	160	2
34	44	2
47	138	1
17	90	3
\.


--
-- Data for Name: role; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.role (role_id, role) FROM stdin;
1	admin
2	customer
3	worker
4	blank1
5	blank2
\.


--
-- Data for Name: room; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.room (room_id, room_name, capacity) FROM stdin;
1	Green Hall	189
2	Blue Hall	200
3	Red Hall	179
4	Yellow Hall	179
5	Purple Hall	195
\.


--
-- Data for Name: user; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds."user" (user_id, given_name, family_name, is_adult, email, phone_number) FROM stdin;
1	Johann	Oakman	t	joakman0@buzzfeed.com	2711359148
2	Loleta	Fleay	t	lfleay1@marriott.com	8587606980
3	Fredek	Jaimez	f	fjaimez2@miibeian.gov.cn	1572689257
4	Milty	Golder	t	mgolder3@fda.gov	7922631975
5	Jan	Lasslett	f	jlasslett4@ocn.ne.jp	8621918346
6	Bride	Niche	t	bniche5@tiny.cc	4831368475
7	Elisabet	Jochanany	f	ejochanany6@php.net	1553066958
8	Zorine	Bust	f	zbust7@telegraph.co.uk	7442959478
9	Monroe	Elacoate	t	melacoate8@sciencedaily.com	1166076229
10	Syd	Brock	t	sbrock9@epa.gov	7326506518
11	Christiane	Swafield	f	cswafielda@jalbum.net	1551350923
12	Lelia	Lauxmann	f	llauxmannb@apple.com	4426591538
13	Madelon	Strickett	f	mstrickettc@slate.com	9753730408
14	Aurelie	Folker	f	afolkerd@patch.com	1939133958
15	Chrystel	Reichert	t	creicherte@wikia.com	2527124007
16	Cissy	Hoyles	f	choylesf@macromedia.com	4878995727
17	Claus	Boxell	t	cboxellg@google.com	4485226763
18	Darb	Antonov	f	dantonovh@comcast.net	5351031666
19	Marijn	Bartolomucci	f	mbartolomuccii@g.co	2986092940
20	Jolene	Feathers	t	jfeathersj@alibaba.com	8886862890
21	John	Oakman	t	jognman0@gmail.com	8463518642
22	Jesse	Oakman	f	jesseOakman0@gmail.com	13534865486
23	Řeřich	Čučka	t	jsdsfaj@alibaba.com	8982311355
\.


--
-- Data for Name: user_has_role; Type: TABLE DATA; Schema: bds; Owner: postgres
--

COPY bds.user_has_role (user_id, role_id, expiration_date) FROM stdin;
1	1	2023-04-04
2	3	2023-01-18
3	3	2023-06-23
4	3	2023-01-08
5	2	2023-04-14
6	2	2023-08-29
7	2	2023-01-25
8	2	2023-09-09
9	2	2023-03-10
10	2	2023-05-27
11	2	2023-03-12
12	2	2023-01-17
13	2	2023-06-15
14	2	2023-01-21
15	2	2022-11-08
16	2	2023-04-25
17	2	2023-04-03
18	2	2023-06-28
19	2	2023-05-10
20	2	2023-04-26
\.


--
-- Data for Name: dummy_table; Type: TABLE DATA; Schema: public; Owner: dbs
--

COPY public.dummy_table (string) FROM stdin;
ssss
ssss
aafaf
aafaf
aafaf
aafaf
aafaf
aafaf
aafaf
\.


--
-- Name: actor_actor_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.actor_actor_id_seq', 13, true);


--
-- Name: category_category_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.category_category_id_seq', 5, true);


--
-- Name: director_director_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.director_director_id_seq', 8, true);


--
-- Name: genre_genre_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.genre_genre_id_seq', 10, true);


--
-- Name: membership_card_card_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.membership_card_card_id_seq', 20, true);


--
-- Name: movie_movie_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.movie_movie_id_seq', 14, true);


--
-- Name: projection_projection_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.projection_projection_id_seq', 47, true);


--
-- Name: reservation_reservation_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.reservation_reservation_id_seq', 128, true);


--
-- Name: role_role_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.role_role_id_seq', 5, true);


--
-- Name: room_room_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.room_room_id_seq', 5, true);


--
-- Name: user_user_id_seq; Type: SEQUENCE SET; Schema: bds; Owner: postgres
--

SELECT pg_catalog.setval('bds.user_user_id_seq', 23, true);


--
-- Name: actor actor_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.actor
    ADD CONSTRAINT actor_pkey PRIMARY KEY (actor_id);


--
-- Name: category category_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (category_id);


--
-- Name: director director_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.director
    ADD CONSTRAINT director_pkey PRIMARY KEY (director_id);


--
-- Name: genre genre_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.genre
    ADD CONSTRAINT genre_pkey PRIMARY KEY (genre_id);


--
-- Name: membership_card membership_card_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.membership_card
    ADD CONSTRAINT membership_card_pkey PRIMARY KEY (card_id);


--
-- Name: movie movie_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.movie
    ADD CONSTRAINT movie_pkey PRIMARY KEY (movie_id);


--
-- Name: projection projection_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.projection
    ADD CONSTRAINT projection_pkey PRIMARY KEY (projection_id);


--
-- Name: reservation reservation_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.reservation
    ADD CONSTRAINT reservation_pkey PRIMARY KEY (reservation_id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (role_id);


--
-- Name: room room_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.room
    ADD CONSTRAINT room_pkey PRIMARY KEY (room_id);


--
-- Name: room room_room_name_key; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.room
    ADD CONSTRAINT room_room_name_key UNIQUE (room_name);


--
-- Name: user user_pkey; Type: CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds."user"
    ADD CONSTRAINT user_pkey PRIMARY KEY (user_id);


--
-- Name: name_index; Type: INDEX; Schema: bds; Owner: postgres
--

CREATE INDEX name_index ON bds."user" USING btree (given_name, family_name);


--
-- Name: movie_has_actor actor_id_mha; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.movie_has_actor
    ADD CONSTRAINT actor_id_mha FOREIGN KEY (actor_id) REFERENCES bds.actor(actor_id);


--
-- Name: price category_id_price; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.price
    ADD CONSTRAINT category_id_price FOREIGN KEY (category_id) REFERENCES bds.category(category_id);


--
-- Name: reserved_seat category_id_rs; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.reserved_seat
    ADD CONSTRAINT category_id_rs FOREIGN KEY (category_id) REFERENCES bds.category(category_id);


--
-- Name: movie_has_genre genre_id_mhg; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.movie_has_genre
    ADD CONSTRAINT genre_id_mhg FOREIGN KEY (genre_id) REFERENCES bds.genre(genre_id);


--
-- Name: login login_user_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.login
    ADD CONSTRAINT login_user_id_fkey FOREIGN KEY (user_id) REFERENCES bds."user"(user_id);


--
-- Name: movie movie_director_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.movie
    ADD CONSTRAINT movie_director_id_fkey FOREIGN KEY (director_id) REFERENCES bds.director(director_id);


--
-- Name: movie_has_actor movie_id_mha; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.movie_has_actor
    ADD CONSTRAINT movie_id_mha FOREIGN KEY (movie_id) REFERENCES bds.movie(movie_id);


--
-- Name: movie_has_genre movie_id_mhg; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.movie_has_genre
    ADD CONSTRAINT movie_id_mhg FOREIGN KEY (movie_id) REFERENCES bds.movie(movie_id);


--
-- Name: price projection_id_price; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.price
    ADD CONSTRAINT projection_id_price FOREIGN KEY (projection_id) REFERENCES bds.projection(projection_id);


--
-- Name: projection projection_movie_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.projection
    ADD CONSTRAINT projection_movie_id_fkey FOREIGN KEY (movie_id) REFERENCES bds.movie(movie_id);


--
-- Name: projection projection_room_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.projection
    ADD CONSTRAINT projection_room_id_fkey FOREIGN KEY (room_id) REFERENCES bds.room(room_id);


--
-- Name: reserved_seat reservation_id_rs; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.reserved_seat
    ADD CONSTRAINT reservation_id_rs FOREIGN KEY (reservation_id) REFERENCES bds.reservation(reservation_id);


--
-- Name: reservation reservation_projection_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.reservation
    ADD CONSTRAINT reservation_projection_id_fkey FOREIGN KEY (projection_id) REFERENCES bds.projection(projection_id) ON DELETE CASCADE;


--
-- Name: reservation reservation_user_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.reservation
    ADD CONSTRAINT reservation_user_id_fkey FOREIGN KEY (user_id) REFERENCES bds."user"(user_id);


--
-- Name: user_has_role user_has_role_role_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.user_has_role
    ADD CONSTRAINT user_has_role_role_id_fkey FOREIGN KEY (role_id) REFERENCES bds.role(role_id);


--
-- Name: user_has_role user_has_role_user_id_fkey; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.user_has_role
    ADD CONSTRAINT user_has_role_user_id_fkey FOREIGN KEY (user_id) REFERENCES bds."user"(user_id);


--
-- Name: membership_card user_id_mc; Type: FK CONSTRAINT; Schema: bds; Owner: postgres
--

ALTER TABLE ONLY bds.membership_card
    ADD CONSTRAINT user_id_mc FOREIGN KEY (user_id) REFERENCES bds."user"(user_id);


--
-- Name: SCHEMA bds; Type: ACL; Schema: -; Owner: postgres
--

GRANT USAGE ON SCHEMA bds TO dbs;


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: postgres
--

GRANT USAGE ON SCHEMA public TO dbs;


--
-- Name: TABLE actor; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds.actor TO dbs;


--
-- Name: SEQUENCE actor_actor_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON SEQUENCE bds.actor_actor_id_seq TO dbs;


--
-- Name: TABLE category; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds.category TO dbs;


--
-- Name: SEQUENCE category_category_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON SEQUENCE bds.category_category_id_seq TO dbs;


--
-- Name: TABLE director; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds.director TO dbs;


--
-- Name: SEQUENCE director_director_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON SEQUENCE bds.director_director_id_seq TO dbs;


--
-- Name: TABLE genre; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds.genre TO dbs;


--
-- Name: SEQUENCE genre_genre_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON SEQUENCE bds.genre_genre_id_seq TO dbs;


--
-- Name: TABLE login; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds.login TO dbs;


--
-- Name: TABLE membership_card; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds.membership_card TO dbs;


--
-- Name: SEQUENCE membership_card_card_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON SEQUENCE bds.membership_card_card_id_seq TO dbs;


--
-- Name: TABLE movie; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds.movie TO dbs;


--
-- Name: TABLE projection; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds.projection TO dbs;


--
-- Name: TABLE reservation; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds.reservation TO dbs;


--
-- Name: TABLE most_attended_projections; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds.most_attended_projections TO dbs;


--
-- Name: TABLE movie_has_actor; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds.movie_has_actor TO dbs;


--
-- Name: TABLE movie_has_genre; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds.movie_has_genre TO dbs;


--
-- Name: SEQUENCE movie_movie_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON SEQUENCE bds.movie_movie_id_seq TO dbs;


--
-- Name: TABLE price; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds.price TO dbs;


--
-- Name: SEQUENCE projection_projection_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON SEQUENCE bds.projection_projection_id_seq TO dbs;


--
-- Name: SEQUENCE reservation_reservation_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON SEQUENCE bds.reservation_reservation_id_seq TO dbs;


--
-- Name: TABLE reserved_seat; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds.reserved_seat TO dbs;


--
-- Name: TABLE role; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds.role TO dbs;


--
-- Name: SEQUENCE role_role_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON SEQUENCE bds.role_role_id_seq TO dbs;


--
-- Name: TABLE room; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds.room TO dbs;


--
-- Name: SEQUENCE room_room_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON SEQUENCE bds.room_room_id_seq TO dbs;


--
-- Name: TABLE "user"; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds."user" TO dbs;


--
-- Name: TABLE user_has_role; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON TABLE bds.user_has_role TO dbs;


--
-- Name: SEQUENCE user_user_id_seq; Type: ACL; Schema: bds; Owner: postgres
--

GRANT ALL ON SEQUENCE bds.user_user_id_seq TO dbs;


--
-- PostgreSQL database dump complete
--

