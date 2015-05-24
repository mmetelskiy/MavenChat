--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: message_changes; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE message_changes (
    message_text text NOT NULL,
    id integer NOT NULL,
    message_id integer NOT NULL
);


ALTER TABLE public.message_changes OWNER TO postgres;

--
-- Name: message_deletions; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE message_deletions (
    id integer NOT NULL,
    message_id integer NOT NULL
);


ALTER TABLE public.message_deletions OWNER TO postgres;

--
-- Name: messages; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE messages (
    message_text text,
    username text,
    message_time text,
    message_id integer,
    is_deleted integer DEFAULT 0,
    user_id integer
);


ALTER TABLE public.messages OWNER TO postgres;

--
-- Name: username_changes; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE username_changes (
    id integer NOT NULL,
    username text NOT NULL,
    image_url text
);


ALTER TABLE public.username_changes OWNER TO postgres;

--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE users (
    username text NOT NULL,
    id integer NOT NULL,
    image_url text
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Data for Name: message_changes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY message_changes (message_text, id, message_id) FROM stdin;
\.


--
-- Data for Name: message_deletions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY message_deletions (id, message_id) FROM stdin;
\.


--
-- Data for Name: messages; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY messages (message_text, username, message_time, message_id, is_deleted, user_id) FROM stdin;
Has anyone seen my shotgun?	Hemingway	1432477952117	1	0	1
My beard is awesome	Hemingway	1432478404913	2	0	1
Yea, Hem. Really good!	Olivia Wilde	1432478744367	3	0	2
I don't like it, man. It makes you older!	Stallone	1432478813020	4	0	3
My glasses are better.	Scorsese	1432478910364	5	0	4
Just give me an Oskar!	Dicaprio	1432479007804	6	0	5
\.


--
-- Data for Name: username_changes; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY username_changes (id, username, image_url) FROM stdin;
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY users (username, id, image_url) FROM stdin;
Hemingway	1	https://res.cloudinary.com/dnfei4skc/image/upload/v1432477900/chat/fas8qvfz7xla7lli3flv.jpg
Stallone	3	https://res.cloudinary.com/dnfei4skc/image/upload/v1432478644/chat/ccugdavxolbl0xdfmtyb.jpg
Scorsese	4	https://res.cloudinary.com/dnfei4skc/image/upload/v1432478748/chat/aozev9nf07exh2apaer3.jpg
Dicaprio	5	https://res.cloudinary.com/dnfei4skc/image/upload/v1432478870/chat/bqrpyrdg1ieitvccz0wi.jpg
Wilde	2	https://res.cloudinary.com/dnfei4skc/image/upload/v1432478605/chat/sai1dddovinsy1eljtcp.jpg
\.


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

