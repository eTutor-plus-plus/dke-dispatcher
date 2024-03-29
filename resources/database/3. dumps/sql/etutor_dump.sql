--
-- PostgreSQL database dump
--

-- Dumped from database version 13.2
-- Dumped by pg_dump version 13.2

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

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: grading; Type: TABLE; Schema: public; Owner: etutor
--

CREATE TABLE public.grading (
    submission_id character varying(255) NOT NULL,
    max_points double precision NOT NULL,
    points double precision NOT NULL,
    report_submission_id character varying(255)
);


ALTER TABLE public.grading OWNER TO etutor;

--
-- Name: report; Type: TABLE; Schema: public; Owner: etutor
--

CREATE TABLE public.report (
    submission_id character varying(255) NOT NULL,
    description character varying(2048),
    error character varying(255),
    hint character varying(255)
);


ALTER TABLE public.report OWNER TO etutor;

--
-- Name: sql_solution_problems; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.sql_solution_problems (
    exercise_id integer NOT NULL,
    solution character varying(2048),
    syntax_error character varying(2048)
);


ALTER TABLE public.sql_solution_problems OWNER TO postgres;

--
-- Name: submission; Type: TABLE; Schema: public; Owner: etutor
--

CREATE TABLE public.submission (
    submission_id character varying(255) NOT NULL,
    exercise_id integer NOT NULL,
    max_points integer NOT NULL,
    task_type character varying(255),
    user_id integer NOT NULL
);


ALTER TABLE public.submission OWNER TO etutor;

--
-- Name: submission_attribute_mapping; Type: TABLE; Schema: public; Owner: etutor
--

CREATE TABLE public.submission_attribute_mapping (
    submission character varying(255) NOT NULL,
    attribute_value character varying(2048),
    attribute_key character varying(255) NOT NULL
);


ALTER TABLE public.submission_attribute_mapping OWNER TO etutor;

--
-- Name: submission_parameter_mapping; Type: TABLE; Schema: public; Owner: etutor
--

CREATE TABLE public.submission_parameter_mapping (
    submission character varying(255) NOT NULL,
    parameter_value character varying(255),
    parameter_key character varying(255) NOT NULL
);


ALTER TABLE public.submission_parameter_mapping OWNER TO etutor;

--
-- Data for Name: grading; Type: TABLE DATA; Schema: public; Owner: etutor
--

COPY public.grading (submission_id, max_points, points, report_submission_id) FROM stdin;
b20874af-21ac-3839-9acd-8fab418bb04d	1	1	\N
d380e60e-c536-3da4-abbb-4518c3289e08	1	0	d380e60e-c536-3da4-abbb-4518c3289e08
0cf1a3fe-ec88-382d-a872-900e4212191b	1	1	\N
74b06912-be4e-3fd4-8c3e-00b6a3ea0f22	1	1	\N
25a14579-8f42-334b-99b1-cb91c315737c	1	1	\N
60523b87-ed10-34f7-9a79-0839b8298dc5	1	1	\N
ac79124e-36a4-344c-842c-029ba67082df	1	1	\N
f5293529-7826-361b-a79a-0d7227160c4c	1	1	\N
d980a80c-83aa-3f77-bed4-da2fd4d14e1a	1	1	\N
cf0f6043-5822-345c-a256-3cbff0a1777f	1	1	\N
e2ebbcaa-d608-33c4-a573-37f5c31f2b51	1	1	\N
1256ab7c-3fa5-3cfc-aa2f-e75add1adf5d	1	1	\N
21b54b6c-3a05-3817-b008-37443cbaeac3	1	1	\N
ed8a7ded-377a-3699-8267-72c3ee815188	1	1	\N
947703da-3bd9-3ee0-ac07-96780928e6c0	1	1	\N
8b0388b1-2c3e-3b94-8a8a-bddfff00b4da	1	1	\N
9e9dc8a4-6793-396f-9343-f4c754a6dc7c	1	0	9e9dc8a4-6793-396f-9343-f4c754a6dc7c
104e2f83-5535-32fa-bcd8-d2b2ebc5b7e6	1	1	\N
e7d02e84-d76d-3a6b-a4d7-da996003e696	1	0	e7d02e84-d76d-3a6b-a4d7-da996003e696
e8a36ddf-bd98-373e-af93-18a18778e078	1	0	e8a36ddf-bd98-373e-af93-18a18778e078
1ac08f67-7dbf-3925-b36b-53aad8c45717	1	1	\N
20a2c5b0-a3dd-3555-ba16-c16254a70c47	1	1	\N
efdd47c4-f1b0-316a-bf4b-ba2adc3f172a	1	0	efdd47c4-f1b0-316a-bf4b-ba2adc3f172a
53a4040f-84c9-3b51-98ed-62992bda6951	1	1	\N
dfb3d354-ba9c-36a6-844e-f1faf127c662	1	0	dfb3d354-ba9c-36a6-844e-f1faf127c662
c5f9cfb0-0ee4-30c1-9b70-69d0c3f02dec	1	1	\N
0e1decde-6009-3817-a74d-3f239f2c04bd	1	0	0e1decde-6009-3817-a74d-3f239f2c04bd
47582ccd-6703-3fe1-98b1-ada4c080e1b9	1	1	\N
26800922-e577-3d1a-93da-84f4bc6fa87d	1	1	\N
2650e6ee-f1fa-37fb-ab25-46a0569e0179	1	1	\N
479b2a11-a991-3480-b365-b3e5354694a2	1	1	\N
623e4382-17fc-3511-b4d7-8b7e25571d55	1	1	\N
2d6dceff-14ce-37b7-a57a-a35a6a4b741a	1	0	2d6dceff-14ce-37b7-a57a-a35a6a4b741a
f9face7d-589e-376e-93dc-d12f4df64a83	1	1	\N
87bd0548-53ef-3b0f-8415-c6d31e43fbc6	1	1	\N
693ebc8d-7561-3634-a2cc-4ec5b5f16ef3	1	1	\N
7711a2d4-b7dd-382a-b10c-1028a949501a	1	1	\N
12aa43e6-0527-34ff-ad4e-4ac630239e0b	1	1	\N
d5d7a5b1-9c6e-3a50-934d-c9b7cedb8f9d	1	1	\N
3698b07a-9e3e-3e63-9d27-6cb2e6a22a96	1	1	\N
40cdd631-9ff8-3ce0-b250-838c7acacb90	1	1	\N
3846570b-4f6a-33f4-92e6-9fc40239288e	1	1	\N
c4df6363-5c9e-334b-8224-6290f9b62e41	1	0	c4df6363-5c9e-334b-8224-6290f9b62e41
ee78463e-dd63-3e1e-8f99-80a7726ea625	1	1	\N
866ba8a9-9740-3f3b-86d8-dc23a72e97c1	1	1	\N
ff177f44-9970-34d8-9dc7-194150b47c29	1	1	\N
662d9b0c-04dc-34de-b83e-42443c3e97e1	1	0	662d9b0c-04dc-34de-b83e-42443c3e97e1
7705947c-374b-3075-ab7c-bafaca31a5d5	1	1	\N
649aa14f-c8a7-3277-a1c0-f339d30408ba	1	1	\N
0cb959d4-6663-387c-85b9-8478d0a10495	1	1	\N
20df41e6-b7cc-37a2-9ab0-fb4e5132cd10	1	1	\N
1f9840a5-5f5a-37e4-9026-f19c50f8a599	1	1	\N
d047e565-7013-33fd-9ba8-7234ffffb3f1	1	1	\N
2519dbc8-6f96-3669-a6db-b3e9e35ffbaa	1	1	\N
533fefb5-0faa-360f-9f2b-603d7ca70b7d	1	1	\N
adf392ee-1784-38d9-bf22-11f502bdbe33	1	0	adf392ee-1784-38d9-bf22-11f502bdbe33
e77f4e07-5c19-3922-bc1c-40e786c109d4	1	1	\N
a18d294b-49ad-3e9f-951c-f37daaa028f1	1	1	\N
37fbaae0-6415-3b70-8844-10461178f95f	1	0	37fbaae0-6415-3b70-8844-10461178f95f
f549d43e-5b0b-3c72-8d0d-917ce3e86642	1	1	\N
dedc6364-d8d5-3388-be4a-b860425c5655	1	1	\N
2ae5dc15-39fd-3fed-9093-8bd31fdda2d8	1	1	\N
fbb7495e-29d0-3d15-86c8-7b1c243b6c02	1	1	\N
6731921f-2cef-37b2-824a-ff6966e7e799	1	1	\N
7d1930b2-f24b-357b-934d-e4d2002806bc	1	1	\N
32b4976a-e66a-3ada-8caf-753773bea24d	1	1	\N
53a833bd-a168-3045-8aab-d88b814ae511	1	1	\N
dcf1af3a-56ad-3ab0-89c7-3765ecc15598	1	1	\N
de15644c-821a-30f4-873b-62c5e2865dcb	1	1	\N
696f0b0b-f034-37ac-8ab0-d6ccdb014f75	1	1	\N
1b794f82-9131-3be2-9973-3ff12ba0c7ac	1	1	\N
c135e40a-eb82-3b54-91bc-1991d642ced5	1	1	\N
02eef84e-e84b-31dd-a8f7-60b064f5bbb1	1	1	\N
f0630838-1b96-376b-bade-5a2d89d6e682	1	1	\N
2ddd7ad3-a07a-3538-b241-7fd043b73407	1	1	\N
31694bb7-3e46-34f1-b1a5-24e31db7cade	1	1	\N
53138fed-2054-36ec-a95e-3580af83ebda	1	1	\N
77054254-8a71-3fe9-acaf-490a81e20d48	1	1	\N
07caa74c-0caa-346a-8f23-d00a8b5411e8	1	1	\N
6b4c1045-ebd3-3c94-b356-56f242f16228	1	1	\N
6bd1db14-7b2a-30fa-9093-9c92cfc164b7	1	1	\N
1c398510-c7ff-3b4a-bd1c-c0f0320e5ce4	1	1	\N
e5041203-afbe-368b-8850-42faf1da6d66	1	1	\N
da7eba9b-86c4-3171-9462-f074dbbfd860	1	1	\N
5c5ac619-aeb9-319b-ad1b-35d046a67ac3	1	1	\N
691a82fa-a370-3adc-b1f1-30388b01332e	1	1	\N
c76eb612-b535-36ac-b5a2-922fca83e3fb	1	1	\N
7b5899f4-962b-39ab-9cd1-d044fd18e81b	1	0	7b5899f4-962b-39ab-9cd1-d044fd18e81b
f0fc60f4-e522-33e5-9b2b-4b618c53d511	1	1	\N
1d58b805-df19-3264-abb4-d2837e993213	1	1	\N
b3d8ca0b-472a-3656-b70c-2397136e5c85	1	1	\N
f8716ca0-649e-368a-839c-a291cac21c85	1	0	f8716ca0-649e-368a-839c-a291cac21c85
0c3ab6ae-921e-3146-a40a-9aeda7c4e80d	1	1	\N
d7a2aa9d-09d1-3d10-874a-18d9f50eede8	1	1	\N
00b1eaca-9e32-38f0-853d-dafcd9bc6cc9	1	1	\N
cb0497f5-1cfe-3fa9-8ac2-209619528fbd	1	1	\N
e8176054-f56e-39c8-b136-a92002ecd151	1	1	\N
12242cdd-61eb-30b2-9236-d8c966ea66fc	1	1	\N
2e57fd44-d7bd-3f05-ab10-fa6c1af6d1d0	1	1	\N
b1fb1972-ec52-39fc-b876-43df992ac628	1	1	\N
431f102c-7f92-39cb-87cf-176b1df10241	1	1	\N
a4fc6c18-4f8d-301b-9978-78797b4a09e4	1	1	\N
c86300e9-b481-3514-98cd-11ac86e617e7	1	1	\N
156a24ee-665e-3234-b3ee-9bd8e0e33799	1	1	\N
9e66e323-edca-371f-a273-30ae45c23b97	1	1	\N
ccbf78a7-165e-3dfd-a778-2e9f16d923fd	1	1	\N
9c36ffa8-038f-3fa9-86b6-90dff42443ce	1	1	\N
332fc967-993a-307f-a4cc-ac2b7a0d424b	1	1	\N
3a047152-16f1-301c-9372-60c3b3a7e7c6	1	1	\N
24124851-6cb6-3794-ae06-f8edcdad3c28	1	1	\N
251c804f-2674-3444-b930-8743f3ed1339	1	1	\N
d721f1da-2074-3a1f-ad04-a6283e9e34e8	1	1	\N
b5181d9d-adc9-3bde-a4c2-a021a1209da3	1	1	\N
01d511e5-c68d-36a5-9009-fe4aeb58a313	1	1	\N
e4c85d9f-ad99-3eca-8a31-ac79ed6f2a32	1	1	\N
87df45bd-11e3-3086-a6bd-6149301984b1	1	1	\N
8be67da6-f2c0-33cc-a54f-a80434ce313d	1	1	\N
681b9051-7455-3b7d-a6d7-a767063ff177	1	1	\N
a04204aa-1f08-31ff-875f-ff3c5db20006	1	1	\N
e101032e-3f01-3d44-832c-aff0bc090a1a	1	1	\N
480a4b86-efd1-3cd6-9406-80f34c408a98	1	1	\N
447051b0-7db4-3708-9c48-417eb426d205	1	1	\N
873403e3-6180-3964-b57a-4926a2a47c0b	1	1	\N
0fe0ffd8-9496-3ca4-94d0-1f25067e3a55	1	1	\N
33270cdb-de34-36b7-8256-106d87d594b7	1	1	\N
7e192773-2563-32f5-8aee-58cd7b652a27	1	1	\N
535c86de-3c70-38ef-acf7-fb4189b796e5	1	1	\N
88cfed64-f772-3238-a009-cd5a3f32a5b3	1	1	\N
d116f4ee-8961-3a6a-9349-fbed5e049a64	1	1	\N
4e27141b-57fc-3b3d-84cc-6955b56538de	1	1	\N
76c79c0f-ca88-3a19-ab15-6cb451b7901e	1	1	\N
c51b4295-83c7-3b1e-b64a-3f9d1860def5	1	1	\N
543e0e63-e8d4-32d3-b4c6-a0d7ee1989b0	1	1	\N
611f41c9-857d-3bcb-bd9e-712dd91d78db	1	1	\N
d9243503-c384-3312-89d4-12ed2aca561c	1	1	\N
d816aa5d-3fe7-3f44-95c0-4e02dae8a973	1	0	d816aa5d-3fe7-3f44-95c0-4e02dae8a973
bfc012f2-804f-3f94-9f6f-818107d72aec	1	1	\N
b86abe04-ebab-3591-88e8-784f3453dba1	1	1	\N
29cd8fa5-732f-33d7-b614-ab2c6ba7323b	1	0	29cd8fa5-732f-33d7-b614-ab2c6ba7323b
99ec3e40-624c-3794-a3cf-8736298cad90	1	0	99ec3e40-624c-3794-a3cf-8736298cad90
78589ebe-5c7b-3362-811d-32635f4ad708	1	0	78589ebe-5c7b-3362-811d-32635f4ad708
a6844930-efba-3884-bd34-55180f5750d3	1	0	a6844930-efba-3884-bd34-55180f5750d3
16d481d9-2771-3298-a893-a7a4e517c7d6	1	1	\N
5d9a43ea-7b79-37af-8fc1-1db1cf369348	1	1	\N
e0b339c9-7714-3af2-8ae8-4e854102a411	1	1	\N
910a455a-50ab-3589-9404-284e88738b9d	1	1	\N
613998c3-32db-3d73-9570-49a1af2a8e8a	1	1	\N
570f24d8-a300-3360-8766-6cbbec0f09e6	1	1	\N
80346873-1801-3fb7-9e76-5bc0cf4fbdd7	1	1	\N
917219a1-6363-39e6-97ed-07d36635a3da	1	1	\N
1e53f2ee-bade-36e6-9f19-3ab6708f6415	1	1	\N
d4d243a2-fdcd-343a-ad94-326141719416	1	1	\N
d6370d52-c333-35d5-abad-b4a74db63f86	1	1	\N
7185d292-3612-3f53-b8fb-2efa311af6e6	1	1	\N
75b00034-b57a-3e6f-b396-62633b110be9	1	1	\N
8b2a4bbd-6831-3735-afa2-9be409e6370d	1	1	\N
d612ba6d-d63c-3865-b150-9976afcc79bb	1	0	d612ba6d-d63c-3865-b150-9976afcc79bb
045af682-8c08-3aa0-972d-89e0db6cd8e1	1	1	\N
c1f571f0-c6b8-30cc-adba-66b1f2af30e5	1	1	\N
464b2677-c9ac-32bf-be33-ba6a60caf030	1	0	464b2677-c9ac-32bf-be33-ba6a60caf030
97522ed5-d479-3307-becd-48d9b66aef84	1	1	\N
590a9f43-0b85-3917-8395-bfdb3011a9ed	1	1	\N
d5f43d1e-790c-3a3a-bbb8-a6bb93c05900	1	1	\N
d0214ec9-1cec-396c-8c41-58e205e0c8a7	1	0	d0214ec9-1cec-396c-8c41-58e205e0c8a7
65d29691-0b9b-3f9b-99cf-67298bbf7f13	1	1	\N
6f83f479-7ea1-3a58-a89f-33169dc5abe1	1	1	\N
b7c076ac-cc41-3e98-b333-5193d01f5aa4	1	1	\N
76f21ab3-eac9-3aff-8834-974ff0e722cc	1	1	\N
cfb2da00-d3ad-3fc4-b1b9-a7ccfaa963c6	1	1	\N
1b6d2dc5-960b-3913-9df3-9a61cd3e2469	1	1	\N
fc14a31d-d55b-3e30-8ddd-afa2855abde9	1	1	\N
72e900a3-f63c-3119-9278-ecc2b25d00d6	1	1	\N
594649ae-dde9-3396-95c6-42ba70de1176	1	1	\N
4f3f57d6-d5e7-3f7a-a0c6-835589e969da	1	1	\N
12ef6652-5ecb-37cf-9994-c45810d27ae2	1	1	\N
6f4c6d6c-c02a-3d77-bdbd-730cae7f51ac	1	1	\N
0eddc426-527c-3ce4-9294-24d996cf98b7	1	1	\N
4f007285-7a17-3e55-a6ce-37e9c889e499	1	1	\N
75c1c369-d9f8-30e2-b624-59fc6470f4e1	1	1	\N
cae8e632-c4ed-34f0-9a3a-06b8ad3ab8b0	1	1	\N
91028944-8c85-3ff8-9fcb-ded595d01386	1	1	\N
b5a7571a-fab0-3c13-b4a9-37329783f886	1	1	\N
31b99df9-9e60-33dc-b0ee-862290b25980	1	1	\N
74334967-a3e4-3201-90ec-84c4c717ca6d	1	1	\N
4c2584f1-1c2e-3190-93ec-9e353da6b2f3	1	1	\N
3a9f7f98-f0a7-3299-b846-a4b8be1824d4	1	1	\N
e9e8536f-7ccb-357c-b064-8621f1edd324	1	1	\N
e2cbb011-c4c2-3e08-bb38-5045bcaeb8c6	1	1	\N
347cdfe0-ec6f-3abc-bc6c-deaccab72fc7	1	1	\N
9fb736c1-6d67-33bc-b8db-2fe03170f7d9	1	1	\N
58b10e80-8206-3ed7-ac23-2b5e7a9cdfa0	1	1	\N
e717ad1f-4d3f-31a3-8fa1-bb6d74e9ed4a	1	1	\N
140de0c6-7fb0-363f-a993-cf1e576f8380	1	1	\N
2304457a-55e3-3c44-b669-ca3338837c2c	1	1	\N
cb1b8192-6e48-340a-b4ca-0a9b2f884c78	1	1	\N
98edded9-93bd-31ab-849d-73bbdff2214c	1	1	\N
112a22cc-f152-3a3a-a9a3-c03fcfc57ac4	1	1	\N
7662cfdc-6fb2-35cd-ad14-4564086c49df	1	1	\N
8173284c-697d-317c-ad5e-dd7f8b64534e	1	1	\N
a351e215-1946-33b1-80c2-60079ae326f3	1	1	\N
80d583c6-ecd8-392e-aa53-6eebf027c5e4	1	1	\N
326509c8-c3b9-3d39-a6cd-3a229958c8ca	1	1	\N
c3494654-74db-3e92-bb6e-2f529bfefa1d	1	1	\N
09608be6-6659-3824-8e9e-e046954c32d4	1	1	\N
90f0e802-3c13-39e0-b5b7-cd89f54cd605	1	1	\N
a96235db-1603-33e4-839f-9e9bbefcee9b	1	1	\N
d4f5ebc3-4fbd-3229-9b39-833e634e5949	1	0	d4f5ebc3-4fbd-3229-9b39-833e634e5949
1cda4c48-e509-354f-871e-d451395eb79d	1	1	\N
fc915d0a-66e0-3bbe-aeaa-6aa5ac434eae	1	1	\N
3ae55a2a-fcfb-3863-8821-4c897d3b7d01	1	1	\N
57cf9a95-b789-3872-8f7e-6ee68b55a700	1	1	\N
9f8faea7-99ce-38ab-9260-49eec92424a0	1	1	\N
316c20e6-50be-36f2-87ee-f529f5f90b8d	1	1	\N
99057421-6877-39b3-90d6-919023ee0185	1	1	\N
cdaa7dd5-0f50-3fcc-8047-d9e141d67356	1	1	\N
a3ea0ac2-12d7-319b-85e2-28f590f8d6de	1	1	\N
690ebcf6-a3f1-35fa-8215-5bfa287a4f58	1	1	\N
fc16e236-8c1f-36e2-8fb0-6fa5f76762c7	1	1	\N
e9a68665-7752-3dc0-8315-194d80bbec42	1	1	\N
1459a00b-d9df-3fcb-8f50-c488dd648d0a	1	1	\N
10841f51-f738-3fb7-a4a8-fbb74e8cbf07	1	1	\N
79c560d2-e98c-32ec-9182-39f59de1b0bc	1	1	\N
5b7eb953-03a8-39b3-a6d1-24bc7647c890	1	0	5b7eb953-03a8-39b3-a6d1-24bc7647c890
a2c40e07-dbdd-3c4d-a593-c6e6cd1473b5	1	1	\N
965cba69-a2dd-3159-b43d-b560a69a1101	1	0	965cba69-a2dd-3159-b43d-b560a69a1101
4700c669-3168-38aa-82d0-ad9fad13297e	1	1	\N
4ceb9215-112f-3b8c-bbb9-41d8810b905a	1	1	\N
9f8ac15a-4e36-3c7a-a5d4-cd511ca389e5	1	1	\N
810da09d-57a6-3f4b-9101-43a9f2f5d654	1	1	\N
37324db8-bc35-39db-8e00-43de4cc7e3fa	1	1	\N
1b6facbf-3949-3e74-a00e-3c0eddaf75a1	1	1	\N
c2c6ab58-a00e-3811-b87e-f0a40891bf19	1	0	c2c6ab58-a00e-3811-b87e-f0a40891bf19
065b6d84-0727-3a0c-a8b3-c696c983683d	1	1	\N
38a7a0ef-9cc4-324f-adce-829b3af115b9	1	1	\N
7e3af2f3-4168-3445-ba22-5d543d627d35	1	1	\N
1c6958b6-6312-3218-93d6-4b9096be9363	1	1	\N
519b4807-0a14-3d55-8da8-707ee605f068	1	0	519b4807-0a14-3d55-8da8-707ee605f068
f4bb9b15-4e0a-318a-a3e3-fa1be9a40918	1	0	f4bb9b15-4e0a-318a-a3e3-fa1be9a40918
fd404a35-7cc5-3501-b50d-2e2b0d7a488c	1	0	fd404a35-7cc5-3501-b50d-2e2b0d7a488c
8e3aed5a-8cf2-3d7d-92b6-8651f11c461a	1	0	8e3aed5a-8cf2-3d7d-92b6-8651f11c461a
5dc6bedb-b927-3e11-b38e-f973a126414b	1	1	\N
03463af8-71e3-3a0d-94ec-5f1501ca48c3	1	0	03463af8-71e3-3a0d-94ec-5f1501ca48c3
2210af29-53b2-309f-b698-ae4599e1151a	1	1	\N
44d53152-80e1-38ac-83cb-dcb720361689	1	1	\N
fa9fdaef-8e31-3249-bc75-5291e99b073a	1	1	\N
da538715-091b-3528-9f1d-7837823331db	1	1	\N
524c806b-3abc-3462-9617-446060265201	1	1	\N
\.


--
-- Data for Name: report; Type: TABLE DATA; Schema: public; Owner: etutor
--

COPY public.report (submission_id, description, error, hint) FROM stdin;
d380e60e-c536-3da4-abbb-4518c3289e08	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 209 \n	 \n	
9e9dc8a4-6793-396f-9343-f4c754a6dc7c	org.postgresql.util.PSQLException: FEHLER: Syntaxfehler bei »MINUS«\n  Position: 456 \n	 \n	
e7d02e84-d76d-3a6b-a4d7-da996003e696	org.postgresql.util.PSQLException: FEHLER: Funktion to_date(date) existiert nicht\n  Hinweis: Keine Funktion stimmt mit dem angegebenen Namen und den Argumenttypen überein. Sie müssen möglicherweise ausdrückliche Typumwandlungen hinzufügen.\n  Position: 12 \n	 \n	
e8a36ddf-bd98-373e-af93-18a18778e078	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 278 \n	 \n	
efdd47c4-f1b0-316a-bf4b-ba2adc3f172a	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 12 \n	 \n	
dfb3d354-ba9c-36a6-844e-f1faf127c662	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 190 \n	 \n	
0e1decde-6009-3817-a74d-3f239f2c04bd	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 12 \n	 \n	
2d6dceff-14ce-37b7-a57a-a35a6a4b741a	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 253 \n	 \n	
c4df6363-5c9e-334b-8224-6290f9b62e41	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 190 \n	 \n	
662d9b0c-04dc-34de-b83e-42443c3e97e1	org.postgresql.util.PSQLException: FEHLER: Unteranfrage in FROM muss Aliasnamen erhalten\n  Hinweis: Zum Beispiel FROM (SELECT ...) [AS] xyz.\n  Position: 52 \n	 \n	
adf392ee-1784-38d9-bf22-11f502bdbe33	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 166 \n	 \n	
37fbaae0-6415-3b70-8844-10461178f95f	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 243 \n	 \n	
7b5899f4-962b-39ab-9cd1-d044fd18e81b	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 12 \n	 \n	
f8716ca0-649e-368a-839c-a291cac21c85	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 289 \n	 \n	
d816aa5d-3fe7-3f44-95c0-4e02dae8a973	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 14 \n	 \n	
29cd8fa5-732f-33d7-b614-ab2c6ba7323b	org.postgresql.util.PSQLException: FEHLER: Syntaxfehler bei »month«\n  Position: 23 \n	 \n	
99ec3e40-624c-3794-a3cf-8736298cad90	org.postgresql.util.PSQLException: FEHLER: Syntaxfehler bei »month«\n  Position: 23 \n	 \n	
78589ebe-5c7b-3362-811d-32635f4ad708	org.postgresql.util.PSQLException: FEHLER: Syntaxfehler bei »year«\n  Position: 23 \n	 \n	
a6844930-efba-3884-bd34-55180f5750d3	org.postgresql.util.PSQLException: FEHLER: Syntaxfehler bei »month«\n  Position: 23 \n	 \n	
d612ba6d-d63c-3865-b150-9976afcc79bb	org.postgresql.util.PSQLException: FEHLER: Syntaxfehler bei »CREATE«\n  Position: 43 \n	 \n	
464b2677-c9ac-32bf-be33-ba6a60caf030	org.postgresql.util.PSQLException: FEHLER: Spalte »sysdate« existiert nicht\n  Position: 113 \n	 \n	
d0214ec9-1cec-396c-8c41-58e205e0c8a7	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 195 \n	 \n	
d4f5ebc3-4fbd-3229-9b39-833e634e5949	org.postgresql.util.PSQLException: FEHLER: Spalte »sysdate« existiert nicht\n  Position: 195 \n	 \n	
5b7eb953-03a8-39b3-a6d1-24bc7647c890	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 234 \n	 \n	
965cba69-a2dd-3159-b43d-b560a69a1101	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 261 \n	 \n	
c2c6ab58-a00e-3811-b87e-f0a40891bf19	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 196 \n	 \n	
519b4807-0a14-3d55-8da8-707ee605f068	org.postgresql.util.PSQLException: FEHLER: Syntaxfehler bei »MINUS«\n  Position: 151 \n	 \n	
f4bb9b15-4e0a-318a-a3e3-fa1be9a40918	org.postgresql.util.PSQLException: FEHLER: Relation »cat« existiert nicht\n  Position: 22 \n	 \n	
fd404a35-7cc5-3501-b50d-2e2b0d7a488c	org.postgresql.util.PSQLException: FEHLER: Relation »cat« existiert nicht\n  Position: 22 \n	 \n	
8e3aed5a-8cf2-3d7d-92b6-8651f11c461a	org.postgresql.util.PSQLException: FEHLER: Relation »cat« existiert nicht\n  Position: 15 \n	 \n	
03463af8-71e3-3a0d-94ec-5f1501ca48c3	org.postgresql.util.PSQLException: FEHLER: Relation »cat« existiert nicht\n  Position: 15 \n	 \n	
\.


--
-- Data for Name: sql_solution_problems; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.sql_solution_problems (exercise_id, solution, syntax_error) FROM stdin;
2	SELECT r1.recordId, r1.artistName, r1.title\r\nFROM   record r1, track t1\r\nWHERE  r1.type='Album' AND r1.recordId=t1.recordId\r\nGROUP BY r1.recordId, r1.artistName, r1.title\r\nHAVING SUM(length) <\r\n  (SELECT AVG(SUM(t2.length))\r\n  FROM record r2, track t2\r\n  WHERE r2.type='Album' AND r2.recordId=t2.recordId\r\n  GROUP BY r2.recordId);	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 209 \n
23	(SELECT k.kundenr, k.name\r\nFROM   kunde k, rechnung r, rechnungpos rp, produkt p\r\nWHERE  k.kundenr=r.kundenr\r\nAND    r.rechnungnr=rp.rechnungnr\r\nAND    r.datum=rp.datum\r\nAND    rp.ean=p.ean\r\nAND    p.kategorie='Audio')\r\nINTERSECT\r\n(SELECT k.kundenr, k.name\r\nFROM   kunde k, rechnung r, rechnungpos rp, produkt p\r\nWHERE  k.kundenr=r.kundenr\r\nAND    r.rechnungnr=rp.rechnungnr\r\nAND    r.datum=rp.datum\r\nAND    rp.ean=p.ean\r\nAND    p.kategorie='Sonstiges')\r\nMINUS\r\n(SELECT k.kundenr, k.name\r\nFROM   kunde k, rechnung r, rechnungpos rp, produkt p\r\nWHERE  k.kundenr=r.kundenr\r\nAND    r.rechnungnr=rp.rechnungnr\r\nAND    r.datum=rp.datum\r\nAND    rp.ean=p.ean\r\nAND    p.kategorie='Pflege')\r\n	org.postgresql.util.PSQLException: FEHLER: Syntaxfehler bei »MINUS«\n  Position: 456 \n
25	SELECT MIN(TO_DATE(rp.datum)) as Tag\r\nFROM   rechnungpos rp\r\nWHERE  (SELECT SUM(rp2.menge*rp2.einzelpreis)\r\n        FROM   rechnungpos rp2\r\n        WHERE  rp2.datum<=rp.datum) > 0.5 *\r\n       (SELECT SUM(rp3.menge*rp3.einzelpreis)\r\n        FROM   rechnungpos rp3)\r\n	org.postgresql.util.PSQLException: FEHLER: Funktion to_date(date) existiert nicht\n  Hinweis: Keine Funktion stimmt mit dem angegebenen Namen und den Argumenttypen überein. Sie müssen möglicherweise ausdrückliche Typumwandlungen hinzufügen.\n  Position: 12 \n
26	SELECT f.filnr, f.plz, SUM(p.ekpreis * s.bestand) as Lagerwert\r\nFROM   filiale f, produkt p, sortiment s\r\nWHERE  f.filnr = s.filnr\r\nAND    p.ean = s.ean\r\nAND    p.kategorie = 'Ersatz'\r\nGROUP BY f.filnr, f.plz\r\nHAVING SUM(p.ekpreis * s.bestand) > \r\n                 (SELECT AVG(SUM(p1.ekpreis * s1.bestand))\r\n                  FROM   produkt p1, sortiment s1\r\n                  WHERE  p1.ean = s1.ean\r\n                  AND    p1.kategorie = 'Ersatz'\r\n                  GROUP BY s1.filnr)	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 278 \n
29	SELECT AVG(COUNT (*)) AS ANZAHL FROM human p, parent par WHERE ParentName = name AND gender = 'f' GROUP BY name	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 12 \n
31	SELECT p.ChildName As Name FROM parent p, parent geschwister WHERE p.ParentName = geschwister.ParentName \r\nGROUP BY p.ChildName HAVING COUNT (distinct geschwister.ChildName) >\r\n(SELECT AVG(COUNT(distinct g.ChildName)) FROM parent p2, parent g WHERE p2.ParentName = g.ParentName \r\nGROUP BY p2.ChildName)	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 190 \n
33	SELECT AVG(MIN(p.age - enkel.age)) AS DURCHSCHNITTSALTER\r\nFROM human p, human enkel, parent ist_kind, parent ist_enkel\r\nWHERE (p.name = ist_kind.ParentName)\r\nAND (ist_kind.ChildName = ist_enkel.ParentName)\r\nAND (ist_enkel.ChildName = enkel.name)\r\nAND (p.gender = 'm') GROUP BY p.name	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 12 \n
39	select w.wohnnr, w.gross, count(*) Anzahlmietverh, avg(m.preis/w.gross) D_Miete, max(m.preis/w.gross) Hoechstmiete\r\nfrom   wohnung w, mietet m\r\nwhere  w.wohnnr=m.wohnnr\r\ngroup by w.wohnnr, w.gross\r\nhaving max(m.preis/w.gross) > \r\n          (select avg(max(m1.preis/w1.gross))\r\n           from   wohnung w1, mietet m1\r\n           where  w1.wohnnr=m1.wohnnr\r\n           group by w1.wohnnr)\r\norder by w.wohnnr	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 253 \n
55	SELECT w.bezirk, SUM(w.gross) FLAECHE\r\nFROM   wohnung w, mietet m\r\nWHERE  w.wohnnr=m.wohnnr AND m.bis=TO_DATE('31-12-2099','DD-MM-YYYY') GROUP BY w.bezirk\r\nHAVING SUM(w.gross)> (SELECT AVG(SUM(w1.gross))\r\n                      FROM   wohnung w1, mietet m1\r\n                      WHERE  w1.wohnnr=m1.wohnnr AND m1.bis=TO_DATE('31-12-2099','DD-MM-YYYY')                      GROUP BY w1.bezirk)\r\n	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 190 \n
59	SELECT bezirk, SUM(erloes) as MIETEINNAHMEN FROM \r\n((SELECT w.bezirk, SUM(ROUND(MONTHS_BETWEEN(m.bis, m.von))*m.preis) erloes\r\nFROM   wohnung w, mietet m\r\nWHERE  w.wohnnr=m.wohnnr\r\nAND    m.bis <> TO_DATE('31-12-2099','DD-MM-YYYY')\r\nGROUP BY w.bezirk)\r\nUNION\r\n(SELECT w.bezirk, SUM(ROUND(MONTHS_BETWEEN(SYSDATE, m.von))*m.preis) erloes\r\nFROM   wohnung w, mietet m\r\nWHERE  w.wohnnr=m.wohnnr\r\nAND    m.bis = TO_DATE('31-12-2099','DD-MM-YYYY')\r\nGROUP BY w.bezirk))\r\nGROUP BY bezirk\r\nHAVING SUM(erloes)=(SELECT MAX(SUM(erloes)) \r\n                    FROM ((SELECT w.bezirk, SUM(ROUND(MONTHS_BETWEEN(m.bis, m.von))*m.preis) erloes\r\n                           FROM   wohnung w, mietet m\r\n                           WHERE  w.wohnnr=m.wohnnr\r\n                           AND    m.bis <> TO_DATE('31-12-2099','DD-MM-YYYY')\r\n                           GROUP BY w.bezirk)\r\n                          UNION\r\n                          (SELECT w.bezirk, SUM(ROUND(MONTHS_BETWEEN(SYSDATE, m.von))*m.preis) erloes\r\n                           FROM   wohnung w, mietet m\r\n                           WHERE  w.wohnnr=m.wohnnr\r\n                           AND    m.bis = TO_DATE('31-12-2099','DD-MM-YYYY')\r\n                           GROUP BY w.bezirk))\r\n                          GROUP BY bezirk)\r\n	org.postgresql.util.PSQLException: FEHLER: Unteranfrage in FROM muss Aliasnamen erhalten\n  Hinweis: Zum Beispiel FROM (SELECT ...) [AS] xyz.\n  Position: 52 \n
68	select r.recordid, r.title, count(*) Tracks \r\nfrom   record r, track t \r\nwhere  r.recordid=t.recordid \r\ngroup by r.recordid, r.title \r\nhaving count(*) = (select max(count(*)) \r\n                    from   record r1, track t1 \r\n                    where  r1.recordid=t1.recordid \r\n                    group by r1.recordid) order by r.title;	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 166 \n
71	select f.kurzbez, f.bezeichnung, count(*) Anzahl\r\nfrom fakultaet f, studienrichtung s, koje k\r\nwhere s.gehoert_zu = f.kurzbez AND\r\n      k.gemietet_von = s.kennzahl\r\ngroup by f.kurzbez, f.bezeichnung\r\nhaving count (standnr) = (\r\n select MAX (count(*))\r\n from studienrichtung s2, koje k2\r\n where k2.gemietet_von = s2.kennzahl\r\n group by s2.gehoert_zu);	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 243 \n
104	SELECT AVG(COUNT(*)) AS AVG_Anzahl FROM   konto k GROUP BY k.inhname, k.gebdat	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 12 \n
108	select i1.name, i1.gebdat, i1.adresse, count(*) anzahl, sum(betrag) sum_betrag\r\nfrom   inhaber i1, konto k1, buchung b1\r\nwhere  i1.name = k1.inhname and i1.gebdat = k1.gebdat\r\nand    k1.kontonr = b1.aufkonto\r\ngroup by i1.name, i1.gebdat, i1.adresse\r\nhaving count(*) >\r\n       (select avg(count(*))\r\n        from   konto k2, buchung b2\r\n        where  k2.kontonr = b2.aufkonto\r\n        group by k2.inhname, k2.gebdat)	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 289 \n
153	SELECT   AVG(count(*)) AS avgsupervised\r\nFROM     staff s1, staff s2\r\nWHERE    s1.snum = s2.supervisor \r\nGROUP BY s1.snum;	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 14 \n
10009	select pu.prodno, t.m month, SUM(pu.qty*pu.price) value\r\nfrom   purchase pu, time t\r\nwhere  pu.dayno=t.dayno\r\ngroup by pu.prodno, t.m	org.postgresql.util.PSQLException: FEHLER: Syntaxfehler bei »month«\n  Position: 23 \n
10010	select pu.prodno, t.m month, SUM(pu.qty*pu.price) value\r\nfrom   purchase pu, time t\r\nwhere  pu.dayno=t.dayno\r\ngroup by grouping sets ((pu.prodno, t.m), (pu.prodno))	org.postgresql.util.PSQLException: FEHLER: Syntaxfehler bei »month«\n  Position: 23 \n
10011	select pu.prodno, t.y year, t.m month, SUM(pu.qty*pu.price) value\r\nfrom   purchase pu, time t\r\nwhere  pu.dayno=t.dayno\r\ngroup by rollup (pu.prodno, t.y, t.m)\r\norder by pu.prodno, t.y, t.m	org.postgresql.util.PSQLException: FEHLER: Syntaxfehler bei »year«\n  Position: 23 \n
10012	select pu.prodno, t.m month, SUM(pu.qty*pu.price) value,\r\nSUM(SUM(pu.qty*pu.price)) OVER \r\n(PARTITION BY pu.prodno ORDER BY t.m ROWS UNBOUNDED PRECEDING) cumvalue\r\nfrom   purchase pu, time t\r\nwhere  pu.dayno=t.dayno\r\ngroup by pu.prodno, t.m;\r\n	org.postgresql.util.PSQLException: FEHLER: Syntaxfehler bei »month«\n  Position: 23 \n
13089	\t\r\nDROP TABLE ETUTOR_JESS.JessExercise;\r\n\r\nCREATE ETUTOR_JESS.TABLE JessExercise (\r\n\tid\tNUMBER,\r\n\tprivate_facts VARCHAR(4000),\r\n\tpublic_facts VARCHAR(4000),\r\n\treference_solution VARCHAR(4000),\r\n\ttransitivity NUMBER,\r\n\trecursion NUMBER,\r\n\tnegation NUMBER,\r\n\tsearchtree NUMBER,\r\n\texampletype NUMBER,\r\n\tmaximumpoints NUMBER,\r\n\tPRIMARY KEY(id)\r\n\t);\r\n	org.postgresql.util.PSQLException: FEHLER: Syntaxfehler bei »CREATE«\n  Position: 43 \n
13171	select buchngnr, betrag\r\nfrom buchung\r\nwhere vonkonto in\r\n  (select kontonr\r\n   from konto\r\n   where ADD_MONTHS(sysdate, -360)<gebdat)\r\nor aufkonto in \r\n  (select kontonr\r\n   from konto\r\n   where ADD_MONTHS(sysdate, -360)<gebdat)\r\norder by betrag desc;\r\n	org.postgresql.util.PSQLException: FEHLER: Spalte »sysdate« existiert nicht\n  Position: 113 \n
13175	select i.name, i.gebdat, i.adresse, count(*)anzahl\r\nfrom inhaber i, konto k\r\nwhere i.name=k.inhname and i.gebdat=k.gebdat\r\ngroup by i.name, i.gebdat, i.adresse\r\nhaving count(*) >\r\n  (select avg(count(*))\r\n   from konto\r\n   group by inhname, gebdat);	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 195 \n
13783	select e.entlngnr, e.von, be.name, bu.titel\r\nfrom entlehng e, benutzer be, buch bu\r\nwhere (e.buch = bu.buchnr) and (e.benutzer = be.bennr) and\r\n      (e.bis = '01-01-2999') and ((e.von + 100) < SYSDATE)\r\norder by be.name;	org.postgresql.util.PSQLException: FEHLER: Spalte »sysdate« existiert nicht\n  Position: 195 \n
13868	Select rp.rechnungnr, rp.datum, sum((rp.einzelpreis-p.ekpreis)*rp.menge) RG\r\nfrom rechnungpos rp, produkt p\r\nwhere p.ean=rp.ean\r\ngroup by rp.rechnungnr, rp.datum\r\nhaving sum((rp.einzelpreis-p.ekpreis)*rp.menge)\r\n       > (select avg(sum((rp2.einzelpreis-p2.ekpreis)*rp2.menge))\r\n          from rechnungpos rp2, produkt p2\r\n          where rp2.ean=p2.ean\r\n          group by rp2.rechnungnr, rp2.datum);	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 234 \n
13870	select f.filnr, f.plz, sum(rp.menge*rp.einzelpreis) Umsatz\r\nfrom filiale f, rechnung r, rechnungpos rp\r\nwhere f.filnr=r.filnr\r\nand r.rechnungnr=rp.rechnungnr and r.datum = rp.datum\r\ngroup by f.filnr, f.plz\r\nhaving sum(rp.menge*rp.einzelpreis) >\r\n  (select avg(sum(rp2.menge*rp2.einzelpreis))\r\n   from filiale f2, rechnung r2, rechnungpos rp2\r\n   where f2.filnr=r2.filnr and r2.rechnungnr=rp2.rechnungnr and r2.datum=rp2.datum\r\n   group by f2.filnr)\r\norder by umsatz desc;	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 261 \n
13877	select distinct k.gebiet\r\nfrom kunde k, auftragskopf ak, auftragszeile az\r\nwhere k.nr = ak.kunde AND\r\n      ak.nr = az.nr\r\ngroup by k.gebiet\r\nhaving sum(az.vkpreis * az.menge) = (\r\n  select max (sum(az2.vkpreis * az2.menge))\r\n  from kunde k2, auftragskopf ak2, auftragszeile az2\r\n  where k2.nr = ak2.kunde AND\r\n        ak2.nr = az2.nr\r\ngroup by k2.gebiet);	org.postgresql.util.PSQLException: FEHLER: Aufrufe von Aggregatfunktionen können nicht geschachtelt werden\n  Position: 196 \n
13882	(select k.nr, k.name\r\n from kunde k, auftragskopf ak, auftragszeile az\r\n where k.nr = ak.kunde and\r\n       ak.nr = az.nr and\r\n       az.menge > 0\r\n)\r\nMINUS\r\n(select k2.nr, k2.name\r\n from kunde k2, auftragskopf ak2\r\n where k2.nr = ak2.kunde and ak2.vertreter = 'GP');	org.postgresql.util.PSQLException: FEHLER: Syntaxfehler bei »MINUS«\n  Position: 151 \n
13883	select count(*) from cat	org.postgresql.util.PSQLException: FEHLER: Relation »cat« existiert nicht\n  Position: 22 \n
13884	select count(*) from cat	org.postgresql.util.PSQLException: FEHLER: Relation »cat« existiert nicht\n  Position: 22 \n
13885	select * from cat	org.postgresql.util.PSQLException: FEHLER: Relation »cat« existiert nicht\n  Position: 15 \n
13887	select * from cat	org.postgresql.util.PSQLException: FEHLER: Relation »cat« existiert nicht\n  Position: 15 \n
\.


--
-- Data for Name: submission; Type: TABLE DATA; Schema: public; Owner: etutor
--

COPY public.submission (submission_id, exercise_id, max_points, task_type, user_id) FROM stdin;
b20874af-21ac-3839-9acd-8fab418bb04d	1	1	sql	1
d380e60e-c536-3da4-abbb-4518c3289e08	2	1	sql	1
0cf1a3fe-ec88-382d-a872-900e4212191b	5	1	sql	1
74b06912-be4e-3fd4-8c3e-00b6a3ea0f22	6	1	sql	1
25a14579-8f42-334b-99b1-cb91c315737c	7	1	sql	1
60523b87-ed10-34f7-9a79-0839b8298dc5	8	1	sql	1
ac79124e-36a4-344c-842c-029ba67082df	9	1	sql	1
f5293529-7826-361b-a79a-0d7227160c4c	13	1	sql	1
d980a80c-83aa-3f77-bed4-da2fd4d14e1a	14	1	sql	1
cf0f6043-5822-345c-a256-3cbff0a1777f	15	1	sql	1
e2ebbcaa-d608-33c4-a573-37f5c31f2b51	17	1	sql	1
1256ab7c-3fa5-3cfc-aa2f-e75add1adf5d	18	1	sql	1
21b54b6c-3a05-3817-b008-37443cbaeac3	19	1	sql	1
ed8a7ded-377a-3699-8267-72c3ee815188	20	1	sql	1
947703da-3bd9-3ee0-ac07-96780928e6c0	21	1	sql	1
8b0388b1-2c3e-3b94-8a8a-bddfff00b4da	22	1	sql	1
9e9dc8a4-6793-396f-9343-f4c754a6dc7c	23	1	sql	1
104e2f83-5535-32fa-bcd8-d2b2ebc5b7e6	24	1	sql	1
e7d02e84-d76d-3a6b-a4d7-da996003e696	25	1	sql	1
e8a36ddf-bd98-373e-af93-18a18778e078	26	1	sql	1
1ac08f67-7dbf-3925-b36b-53aad8c45717	27	1	sql	1
20a2c5b0-a3dd-3555-ba16-c16254a70c47	28	1	sql	1
efdd47c4-f1b0-316a-bf4b-ba2adc3f172a	29	1	sql	1
53a4040f-84c9-3b51-98ed-62992bda6951	30	1	sql	1
dfb3d354-ba9c-36a6-844e-f1faf127c662	31	1	sql	1
c5f9cfb0-0ee4-30c1-9b70-69d0c3f02dec	32	1	sql	1
0e1decde-6009-3817-a74d-3f239f2c04bd	33	1	sql	1
47582ccd-6703-3fe1-98b1-ada4c080e1b9	34	1	sql	1
26800922-e577-3d1a-93da-84f4bc6fa87d	35	1	sql	1
2650e6ee-f1fa-37fb-ab25-46a0569e0179	36	1	sql	1
479b2a11-a991-3480-b365-b3e5354694a2	37	1	sql	1
623e4382-17fc-3511-b4d7-8b7e25571d55	38	1	sql	1
2d6dceff-14ce-37b7-a57a-a35a6a4b741a	39	1	sql	1
f9face7d-589e-376e-93dc-d12f4df64a83	40	1	sql	1
87bd0548-53ef-3b0f-8415-c6d31e43fbc6	41	1	sql	1
693ebc8d-7561-3634-a2cc-4ec5b5f16ef3	42	1	sql	1
7711a2d4-b7dd-382a-b10c-1028a949501a	43	1	sql	1
12aa43e6-0527-34ff-ad4e-4ac630239e0b	44	1	sql	1
d5d7a5b1-9c6e-3a50-934d-c9b7cedb8f9d	51	1	sql	1
3698b07a-9e3e-3e63-9d27-6cb2e6a22a96	52	1	sql	1
40cdd631-9ff8-3ce0-b250-838c7acacb90	53	1	sql	1
3846570b-4f6a-33f4-92e6-9fc40239288e	54	1	sql	1
c4df6363-5c9e-334b-8224-6290f9b62e41	55	1	sql	1
ee78463e-dd63-3e1e-8f99-80a7726ea625	56	1	sql	1
866ba8a9-9740-3f3b-86d8-dc23a72e97c1	57	1	sql	1
ff177f44-9970-34d8-9dc7-194150b47c29	58	1	sql	1
662d9b0c-04dc-34de-b83e-42443c3e97e1	59	1	sql	1
7705947c-374b-3075-ab7c-bafaca31a5d5	60	1	sql	1
649aa14f-c8a7-3277-a1c0-f339d30408ba	61	1	sql	1
0cb959d4-6663-387c-85b9-8478d0a10495	62	1	sql	1
20df41e6-b7cc-37a2-9ab0-fb4e5132cd10	63	1	sql	1
1f9840a5-5f5a-37e4-9026-f19c50f8a599	64	1	sql	1
d047e565-7013-33fd-9ba8-7234ffffb3f1	65	1	sql	1
2519dbc8-6f96-3669-a6db-b3e9e35ffbaa	66	1	sql	1
533fefb5-0faa-360f-9f2b-603d7ca70b7d	67	1	sql	1
adf392ee-1784-38d9-bf22-11f502bdbe33	68	1	sql	1
e77f4e07-5c19-3922-bc1c-40e786c109d4	69	1	sql	1
a18d294b-49ad-3e9f-951c-f37daaa028f1	70	1	sql	1
37fbaae0-6415-3b70-8844-10461178f95f	71	1	sql	1
f549d43e-5b0b-3c72-8d0d-917ce3e86642	72	1	sql	1
dedc6364-d8d5-3388-be4a-b860425c5655	73	1	sql	1
2ae5dc15-39fd-3fed-9093-8bd31fdda2d8	74	1	sql	1
fbb7495e-29d0-3d15-86c8-7b1c243b6c02	79	1	sql	1
6731921f-2cef-37b2-824a-ff6966e7e799	80	1	sql	1
7d1930b2-f24b-357b-934d-e4d2002806bc	81	1	sql	1
32b4976a-e66a-3ada-8caf-753773bea24d	82	1	sql	1
53a833bd-a168-3045-8aab-d88b814ae511	83	1	sql	1
dcf1af3a-56ad-3ab0-89c7-3765ecc15598	84	1	sql	1
de15644c-821a-30f4-873b-62c5e2865dcb	85	1	sql	1
696f0b0b-f034-37ac-8ab0-d6ccdb014f75	86	1	sql	1
1b794f82-9131-3be2-9973-3ff12ba0c7ac	87	1	sql	1
c135e40a-eb82-3b54-91bc-1991d642ced5	88	1	sql	1
02eef84e-e84b-31dd-a8f7-60b064f5bbb1	89	1	sql	1
f0630838-1b96-376b-bade-5a2d89d6e682	90	1	sql	1
2ddd7ad3-a07a-3538-b241-7fd043b73407	91	1	sql	1
31694bb7-3e46-34f1-b1a5-24e31db7cade	92	1	sql	1
53138fed-2054-36ec-a95e-3580af83ebda	93	1	sql	1
77054254-8a71-3fe9-acaf-490a81e20d48	94	1	sql	1
07caa74c-0caa-346a-8f23-d00a8b5411e8	95	1	sql	1
6b4c1045-ebd3-3c94-b356-56f242f16228	96	1	sql	1
6bd1db14-7b2a-30fa-9093-9c92cfc164b7	97	1	sql	1
1c398510-c7ff-3b4a-bd1c-c0f0320e5ce4	98	1	sql	1
e5041203-afbe-368b-8850-42faf1da6d66	99	1	sql	1
da7eba9b-86c4-3171-9462-f074dbbfd860	100	1	sql	1
5c5ac619-aeb9-319b-ad1b-35d046a67ac3	101	1	sql	1
691a82fa-a370-3adc-b1f1-30388b01332e	102	1	sql	1
c76eb612-b535-36ac-b5a2-922fca83e3fb	103	1	sql	1
7b5899f4-962b-39ab-9cd1-d044fd18e81b	104	1	sql	1
f0fc60f4-e522-33e5-9b2b-4b618c53d511	105	1	sql	1
1d58b805-df19-3264-abb4-d2837e993213	106	1	sql	1
b3d8ca0b-472a-3656-b70c-2397136e5c85	107	1	sql	1
f8716ca0-649e-368a-839c-a291cac21c85	108	1	sql	1
0c3ab6ae-921e-3146-a40a-9aeda7c4e80d	109	1	sql	1
d7a2aa9d-09d1-3d10-874a-18d9f50eede8	110	1	sql	1
00b1eaca-9e32-38f0-853d-dafcd9bc6cc9	111	1	sql	1
cb0497f5-1cfe-3fa9-8ac2-209619528fbd	112	1	sql	1
e8176054-f56e-39c8-b136-a92002ecd151	114	1	sql	1
12242cdd-61eb-30b2-9236-d8c966ea66fc	115	1	sql	1
2e57fd44-d7bd-3f05-ab10-fa6c1af6d1d0	116	1	sql	1
b1fb1972-ec52-39fc-b876-43df992ac628	117	1	sql	1
431f102c-7f92-39cb-87cf-176b1df10241	118	1	sql	1
a4fc6c18-4f8d-301b-9978-78797b4a09e4	119	1	sql	1
c86300e9-b481-3514-98cd-11ac86e617e7	120	1	sql	1
156a24ee-665e-3234-b3ee-9bd8e0e33799	121	1	sql	1
9e66e323-edca-371f-a273-30ae45c23b97	122	1	sql	1
ccbf78a7-165e-3dfd-a778-2e9f16d923fd	123	1	sql	1
9c36ffa8-038f-3fa9-86b6-90dff42443ce	124	1	sql	1
332fc967-993a-307f-a4cc-ac2b7a0d424b	125	1	sql	1
3a047152-16f1-301c-9372-60c3b3a7e7c6	126	1	sql	1
24124851-6cb6-3794-ae06-f8edcdad3c28	127	1	sql	1
251c804f-2674-3444-b930-8743f3ed1339	128	1	sql	1
d721f1da-2074-3a1f-ad04-a6283e9e34e8	129	1	sql	1
b5181d9d-adc9-3bde-a4c2-a021a1209da3	130	1	sql	1
01d511e5-c68d-36a5-9009-fe4aeb58a313	131	1	sql	1
e4c85d9f-ad99-3eca-8a31-ac79ed6f2a32	132	1	sql	1
87df45bd-11e3-3086-a6bd-6149301984b1	133	1	sql	1
8be67da6-f2c0-33cc-a54f-a80434ce313d	134	1	sql	1
681b9051-7455-3b7d-a6d7-a767063ff177	135	1	sql	1
a04204aa-1f08-31ff-875f-ff3c5db20006	136	1	sql	1
e101032e-3f01-3d44-832c-aff0bc090a1a	137	1	sql	1
480a4b86-efd1-3cd6-9406-80f34c408a98	138	1	sql	1
447051b0-7db4-3708-9c48-417eb426d205	139	1	sql	1
873403e3-6180-3964-b57a-4926a2a47c0b	140	1	sql	1
0fe0ffd8-9496-3ca4-94d0-1f25067e3a55	141	1	sql	1
33270cdb-de34-36b7-8256-106d87d594b7	142	1	sql	1
7e192773-2563-32f5-8aee-58cd7b652a27	143	1	sql	1
535c86de-3c70-38ef-acf7-fb4189b796e5	144	1	sql	1
88cfed64-f772-3238-a009-cd5a3f32a5b3	145	1	sql	1
d116f4ee-8961-3a6a-9349-fbed5e049a64	146	1	sql	1
4e27141b-57fc-3b3d-84cc-6955b56538de	147	1	sql	1
76c79c0f-ca88-3a19-ab15-6cb451b7901e	148	1	sql	1
c51b4295-83c7-3b1e-b64a-3f9d1860def5	149	1	sql	1
543e0e63-e8d4-32d3-b4c6-a0d7ee1989b0	150	1	sql	1
611f41c9-857d-3bcb-bd9e-712dd91d78db	151	1	sql	1
d9243503-c384-3312-89d4-12ed2aca561c	152	1	sql	1
d816aa5d-3fe7-3f44-95c0-4e02dae8a973	153	1	sql	1
bfc012f2-804f-3f94-9f6f-818107d72aec	10003	1	sql	1
b86abe04-ebab-3591-88e8-784f3453dba1	10004	1	sql	1
29cd8fa5-732f-33d7-b614-ab2c6ba7323b	10009	1	sql	1
99ec3e40-624c-3794-a3cf-8736298cad90	10010	1	sql	1
78589ebe-5c7b-3362-811d-32635f4ad708	10011	1	sql	1
a6844930-efba-3884-bd34-55180f5750d3	10012	1	sql	1
16d481d9-2771-3298-a893-a7a4e517c7d6	10013	1	sql	1
5d9a43ea-7b79-37af-8fc1-1db1cf369348	10014	1	sql	1
e0b339c9-7714-3af2-8ae8-4e854102a411	10015	1	sql	1
910a455a-50ab-3589-9404-284e88738b9d	10042	1	sql	1
613998c3-32db-3d73-9570-49a1af2a8e8a	10043	1	sql	1
570f24d8-a300-3360-8766-6cbbec0f09e6	10044	1	sql	1
80346873-1801-3fb7-9e76-5bc0cf4fbdd7	10045	1	sql	1
917219a1-6363-39e6-97ed-07d36635a3da	10046	1	sql	1
1e53f2ee-bade-36e6-9f19-3ab6708f6415	10047	1	sql	1
d4d243a2-fdcd-343a-ad94-326141719416	10048	1	sql	1
d6370d52-c333-35d5-abad-b4a74db63f86	10049	1	sql	1
7185d292-3612-3f53-b8fb-2efa311af6e6	10050	1	sql	1
75b00034-b57a-3e6f-b396-62633b110be9	10051	1	sql	1
8b2a4bbd-6831-3735-afa2-9be409e6370d	10052	1	sql	1
d612ba6d-d63c-3865-b150-9976afcc79bb	13089	1	sql	1
045af682-8c08-3aa0-972d-89e0db6cd8e1	13169	1	sql	1
c1f571f0-c6b8-30cc-adba-66b1f2af30e5	13170	1	sql	1
464b2677-c9ac-32bf-be33-ba6a60caf030	13171	1	sql	1
97522ed5-d479-3307-becd-48d9b66aef84	13172	1	sql	1
590a9f43-0b85-3917-8395-bfdb3011a9ed	13173	1	sql	1
d5f43d1e-790c-3a3a-bbb8-a6bb93c05900	13174	1	sql	1
d0214ec9-1cec-396c-8c41-58e205e0c8a7	13175	1	sql	1
65d29691-0b9b-3f9b-99cf-67298bbf7f13	13176	1	sql	1
6f83f479-7ea1-3a58-a89f-33169dc5abe1	13597	1	sql	1
b7c076ac-cc41-3e98-b333-5193d01f5aa4	13598	1	sql	1
76f21ab3-eac9-3aff-8834-974ff0e722cc	13599	1	sql	1
cfb2da00-d3ad-3fc4-b1b9-a7ccfaa963c6	13600	1	sql	1
1b6d2dc5-960b-3913-9df3-9a61cd3e2469	13601	1	sql	1
fc14a31d-d55b-3e30-8ddd-afa2855abde9	13602	1	sql	1
72e900a3-f63c-3119-9278-ecc2b25d00d6	13603	1	sql	1
594649ae-dde9-3396-95c6-42ba70de1176	13604	1	sql	1
4f3f57d6-d5e7-3f7a-a0c6-835589e969da	13605	1	sql	1
12ef6652-5ecb-37cf-9994-c45810d27ae2	13606	1	sql	1
6f4c6d6c-c02a-3d77-bdbd-730cae7f51ac	13607	1	sql	1
0eddc426-527c-3ce4-9294-24d996cf98b7	13608	1	sql	1
4f007285-7a17-3e55-a6ce-37e9c889e499	13609	1	sql	1
75c1c369-d9f8-30e2-b624-59fc6470f4e1	13610	1	sql	1
cae8e632-c4ed-34f0-9a3a-06b8ad3ab8b0	13611	1	sql	1
91028944-8c85-3ff8-9fcb-ded595d01386	13612	1	sql	1
b5a7571a-fab0-3c13-b4a9-37329783f886	13613	1	sql	1
31b99df9-9e60-33dc-b0ee-862290b25980	13614	1	sql	1
74334967-a3e4-3201-90ec-84c4c717ca6d	13615	1	sql	1
4c2584f1-1c2e-3190-93ec-9e353da6b2f3	13616	1	sql	1
3a9f7f98-f0a7-3299-b846-a4b8be1824d4	13617	1	sql	1
e9e8536f-7ccb-357c-b064-8621f1edd324	13618	1	sql	1
e2cbb011-c4c2-3e08-bb38-5045bcaeb8c6	13619	1	sql	1
347cdfe0-ec6f-3abc-bc6c-deaccab72fc7	13620	1	sql	1
9fb736c1-6d67-33bc-b8db-2fe03170f7d9	13621	1	sql	1
58b10e80-8206-3ed7-ac23-2b5e7a9cdfa0	13622	1	sql	1
e717ad1f-4d3f-31a3-8fa1-bb6d74e9ed4a	13623	1	sql	1
140de0c6-7fb0-363f-a993-cf1e576f8380	13624	1	sql	1
2304457a-55e3-3c44-b669-ca3338837c2c	13645	1	sql	1
cb1b8192-6e48-340a-b4ca-0a9b2f884c78	13646	1	sql	1
98edded9-93bd-31ab-849d-73bbdff2214c	13647	1	sql	1
112a22cc-f152-3a3a-a9a3-c03fcfc57ac4	13648	1	sql	1
7662cfdc-6fb2-35cd-ad14-4564086c49df	13649	1	sql	1
8173284c-697d-317c-ad5e-dd7f8b64534e	13650	1	sql	1
a351e215-1946-33b1-80c2-60079ae326f3	13651	1	sql	1
80d583c6-ecd8-392e-aa53-6eebf027c5e4	13652	1	sql	1
326509c8-c3b9-3d39-a6cd-3a229958c8ca	13653	1	sql	1
c3494654-74db-3e92-bb6e-2f529bfefa1d	13654	1	sql	1
09608be6-6659-3824-8e9e-e046954c32d4	13777	1	sql	1
90f0e802-3c13-39e0-b5b7-cd89f54cd605	13778	1	sql	1
a96235db-1603-33e4-839f-9e9bbefcee9b	13779	1	sql	1
d4f5ebc3-4fbd-3229-9b39-833e634e5949	13783	1	sql	1
1cda4c48-e509-354f-871e-d451395eb79d	13788	1	sql	1
fc915d0a-66e0-3bbe-aeaa-6aa5ac434eae	13795	1	sql	1
3ae55a2a-fcfb-3863-8821-4c897d3b7d01	13805	1	sql	1
57cf9a95-b789-3872-8f7e-6ee68b55a700	13811	1	sql	1
9f8faea7-99ce-38ab-9260-49eec92424a0	13815	1	sql	1
316c20e6-50be-36f2-87ee-f529f5f90b8d	13818	1	sql	1
99057421-6877-39b3-90d6-919023ee0185	13823	1	sql	1
cdaa7dd5-0f50-3fcc-8047-d9e141d67356	13824	1	sql	1
a3ea0ac2-12d7-319b-85e2-28f590f8d6de	13825	1	sql	1
690ebcf6-a3f1-35fa-8215-5bfa287a4f58	13826	1	sql	1
fc16e236-8c1f-36e2-8fb0-6fa5f76762c7	13863	1	sql	1
e9a68665-7752-3dc0-8315-194d80bbec42	13864	1	sql	1
1459a00b-d9df-3fcb-8f50-c488dd648d0a	13865	1	sql	1
10841f51-f738-3fb7-a4a8-fbb74e8cbf07	13866	1	sql	1
79c560d2-e98c-32ec-9182-39f59de1b0bc	13867	1	sql	1
5b7eb953-03a8-39b3-a6d1-24bc7647c890	13868	1	sql	1
a2c40e07-dbdd-3c4d-a593-c6e6cd1473b5	13869	1	sql	1
965cba69-a2dd-3159-b43d-b560a69a1101	13870	1	sql	1
4700c669-3168-38aa-82d0-ad9fad13297e	13871	1	sql	1
4ceb9215-112f-3b8c-bbb9-41d8810b905a	13872	1	sql	1
9f8ac15a-4e36-3c7a-a5d4-cd511ca389e5	13873	1	sql	1
810da09d-57a6-3f4b-9101-43a9f2f5d654	13874	1	sql	1
37324db8-bc35-39db-8e00-43de4cc7e3fa	13875	1	sql	1
1b6facbf-3949-3e74-a00e-3c0eddaf75a1	13876	1	sql	1
c2c6ab58-a00e-3811-b87e-f0a40891bf19	13877	1	sql	1
065b6d84-0727-3a0c-a8b3-c696c983683d	13878	1	sql	1
38a7a0ef-9cc4-324f-adce-829b3af115b9	13879	1	sql	1
7e3af2f3-4168-3445-ba22-5d543d627d35	13880	1	sql	1
1c6958b6-6312-3218-93d6-4b9096be9363	13881	1	sql	1
519b4807-0a14-3d55-8da8-707ee605f068	13882	1	sql	1
f4bb9b15-4e0a-318a-a3e3-fa1be9a40918	13883	1	sql	1
fd404a35-7cc5-3501-b50d-2e2b0d7a488c	13884	1	sql	1
8e3aed5a-8cf2-3d7d-92b6-8651f11c461a	13885	1	sql	1
5dc6bedb-b927-3e11-b38e-f973a126414b	13886	1	sql	1
03463af8-71e3-3a0d-94ec-5f1501ca48c3	13887	1	sql	1
2210af29-53b2-309f-b698-ae4599e1151a	13888	1	sql	1
44d53152-80e1-38ac-83cb-dcb720361689	13889	1	sql	1
fa9fdaef-8e31-3249-bc75-5291e99b073a	13890	1	sql	1
da538715-091b-3528-9f1d-7837823331db	13891	1	sql	1
524c806b-3abc-3462-9617-446060265201	13892	1	sql	1
\.


--
-- Data for Name: submission_attribute_mapping; Type: TABLE DATA; Schema: public; Owner: etutor
--

COPY public.submission_attribute_mapping (submission, attribute_value, attribute_key) FROM stdin;
b20874af-21ac-3839-9acd-8fab418bb04d	3	diagnoseLevel
b20874af-21ac-3839-9acd-8fab418bb04d	diagnose	action
b20874af-21ac-3839-9acd-8fab418bb04d	SELECT a.name, a.nationality\r\nFROM   artist a \r\nWHERE  NOT EXISTS\r\n        (SELECT r.recordId \r\n         FROM record r, distribute d\r\n         WHERE r.artistName = a.name AND \r\n               r.recordId = d.recordId AND \r\n               d.media = 'CD' AND \r\n               d.price < 20);	submission
d380e60e-c536-3da4-abbb-4518c3289e08	3	diagnoseLevel
d380e60e-c536-3da4-abbb-4518c3289e08	diagnose	action
d380e60e-c536-3da4-abbb-4518c3289e08	SELECT r1.recordId, r1.artistName, r1.title\r\nFROM   record r1, track t1\r\nWHERE  r1.type='Album' AND r1.recordId=t1.recordId\r\nGROUP BY r1.recordId, r1.artistName, r1.title\r\nHAVING SUM(length) <\r\n  (SELECT AVG(SUM(t2.length))\r\n  FROM record r2, track t2\r\n  WHERE r2.type='Album' AND r2.recordId=t2.recordId\r\n  GROUP BY r2.recordId);	submission
0cf1a3fe-ec88-382d-a872-900e4212191b	3	diagnoseLevel
0cf1a3fe-ec88-382d-a872-900e4212191b	diagnose	action
0cf1a3fe-ec88-382d-a872-900e4212191b	SELECT p.ean, p.bezeichnung, f.filnr, f.inhname, s.bestand\r\nFROM   produkt p, filiale f, sortiment s\r\nWHERE  p.ean=s.ean\r\nAND    f.filnr=s.filnr\r\nAND    s.bestand >= 50\r\nORDER BY p.ean DESC, f.filnr	submission
74b06912-be4e-3fd4-8c3e-00b6a3ea0f22	3	diagnoseLevel
74b06912-be4e-3fd4-8c3e-00b6a3ea0f22	diagnose	action
74b06912-be4e-3fd4-8c3e-00b6a3ea0f22	SELECT DISTINCT b.bestell_nr\r\nFROM   Bestellung b, Bestellposition p\r\nWHERE  b.bestell_nr = p.bestell_nr\r\n       AND b.Datum = TO_DATE('13-10-03','DD-MM-YY')\r\n       AND p.Menge > 100;	submission
25a14579-8f42-334b-99b1-cb91c315737c	3	diagnoseLevel
25a14579-8f42-334b-99b1-cb91c315737c	diagnose	action
25a14579-8f42-334b-99b1-cb91c315737c	SELECT bestell_nr, Datum\r\n    FROM Bestellung\r\n    WHERE bestell_nr NOT IN\r\n        (SELECT bestell_nr \r\n         FROM Lieferposition);	submission
60523b87-ed10-34f7-9a79-0839b8298dc5	3	diagnoseLevel
60523b87-ed10-34f7-9a79-0839b8298dc5	diagnose	action
60523b87-ed10-34f7-9a79-0839b8298dc5	SELECT p.Bezeichnung\r\nFROM   Bestellposition bp, Bauprodukt p, Bestellung b\r\nWHERE  bp.prod_nr = p.prod_nr\r\n       AND bp.bestell_nr = b.bestell_nr\r\n       AND b.Datum = TO_DATE('13-10-03','DD-MM-YY')\r\nGROUP BY p.prod_nr, p.Bezeichnung\r\nHAVING COUNT(DISTINCT bp.bestell_nr) > 5;	submission
ac79124e-36a4-344c-842c-029ba67082df	3	diagnoseLevel
ac79124e-36a4-344c-842c-029ba67082df	diagnose	action
ac79124e-36a4-344c-842c-029ba67082df	SELECT ls.Datum, p.Bezeichnung, SUM(lp.Menge) Liefermenge\r\nFROM   Bauprodukt p, Bestellposition bp, Lieferposition lp, Lieferschein ls\r\nWHERE  p.prod_nr = bp.prod_nr\r\n       AND bp.bestell_nr = lp.bestell_nr\r\n       AND bp.pos_nr = lp.bestellpos_nr\r\n       AND lp.liefer_nr = ls.liefer_nr\r\nGROUP BY ls.Datum, p.prod_nr, p.Bezeichnung;	submission
f5293529-7826-361b-a79a-0d7227160c4c	3	diagnoseLevel
f5293529-7826-361b-a79a-0d7227160c4c	diagnose	action
f5293529-7826-361b-a79a-0d7227160c4c	select sa.art, sa.bezeichnung, count (s.name) as ANZAHL, sum (s.laenge) as LAENGE\r\nfrom strassenart sa, strasse s\r\nwhere sa.art = s.strassenart\r\ngroup by sa.art, sa.bezeichnung\r\nhaving sum (s.laenge) >= ALL\r\n  (select sum (s2.laenge)\r\n   from strasse s2\r\n   group by s2.strassenart)	submission
d980a80c-83aa-3f77-bed4-da2fd4d14e1a	3	diagnoseLevel
d980a80c-83aa-3f77-bed4-da2fd4d14e1a	diagnose	action
d980a80c-83aa-3f77-bed4-da2fd4d14e1a	select *\r\nfrom ort o\r\nwhere o.plz in (\r\n  (select ortVon\r\n   from strasse s, strassenart sa\r\n   where s.strassenart = sa.art AND\r\n         sa.bezeichnung = 'Landesstrasse')\r\n  UNION\r\n  (select ortNach\r\n   from strasse s, strassenart sa\r\n   where s.strassenart = sa.art AND\r\n         sa.bezeichnung = 'Landesstrasse') )	submission
cf0f6043-5822-345c-a256-3cbff0a1777f	3	diagnoseLevel
cf0f6043-5822-345c-a256-3cbff0a1777f	diagnose	action
cf0f6043-5822-345c-a256-3cbff0a1777f	select *\r\nfrom ort o\r\nwhere not exists\r\n  (select *\r\n   from strassenart sa\r\n   where not exists\r\n     (select *\r\n      from liegtAnStrasse las, strasse s\r\n      where las.strassenname = s.name AND\r\n            s.strassenart = sa.art AND\r\n            o.plz = las.ort))	submission
e2ebbcaa-d608-33c4-a573-37f5c31f2b51	3	diagnoseLevel
e2ebbcaa-d608-33c4-a573-37f5c31f2b51	diagnose	action
e2ebbcaa-d608-33c4-a573-37f5c31f2b51	SELECT s1.filnr, s1.ean as EAN_1, p1.bezeichnung as BEZ_1, s2.ean as EAN_2, p2.bezeichnung as BEZ_2,\r\n       (s1.vkpreis-s1.preisred) as preis\r\nFROM   sortiment s1, sortiment s2, produkt p1, produkt p2\r\nWHERE  s1.ean = p1.ean\r\nAND    s2.ean = p2.ean \r\nAND    s1.ean <> s2.ean\r\nAND    s1.filnr = s2.filnr\r\nAND    s1.vkpreis-s1.preisred = s2.vkpreis-s2.preisred	submission
1256ab7c-3fa5-3cfc-aa2f-e75add1adf5d	3	diagnoseLevel
1256ab7c-3fa5-3cfc-aa2f-e75add1adf5d	diagnose	action
1256ab7c-3fa5-3cfc-aa2f-e75add1adf5d	SELECT p.ean, p.bezeichnung\r\nFROM   produkt p\r\nWHERE  p.ean NOT IN (SELECT s.ean\r\n                     FROM   sortiment s, filiale f\r\n                     WHERE  s.filnr = f.filnr\r\n                     AND    f.plz BETWEEN 4010 AND 4049)	submission
21b54b6c-3a05-3817-b008-37443cbaeac3	3	diagnoseLevel
21b54b6c-3a05-3817-b008-37443cbaeac3	diagnose	action
21b54b6c-3a05-3817-b008-37443cbaeac3	SELECT f.filnr, f.inhname\r\nFROM   filiale f\r\nWHERE  NOT EXISTS (SELECT *\r\n                   FROM   produkt p\r\n                   WHERE  p.kategorie NOT IN (SELECT p2.kategorie\r\n                                              FROM   sortiment s, produkt p2\r\n                                              WHERE  s.ean=p2.ean\r\n                                              AND    s.filnr=f.filnr))	submission
ed8a7ded-377a-3699-8267-72c3ee815188	3	diagnoseLevel
ed8a7ded-377a-3699-8267-72c3ee815188	diagnose	action
ed8a7ded-377a-3699-8267-72c3ee815188	SELECT COUNT(*) as AnzahlSortiment, COUNT(DISTINCT s.ean)As AnzahlEAN, SUM(s.bestand*p.ekpreis) as Bestandwert\r\nFROM   sortiment s, produkt p\r\nWHERE  s.ean=p.ean	submission
947703da-3bd9-3ee0-ac07-96780928e6c0	3	diagnoseLevel
947703da-3bd9-3ee0-ac07-96780928e6c0	diagnose	action
947703da-3bd9-3ee0-ac07-96780928e6c0	SELECT f.filnr, f.plz, SUM(p.ekpreis * s.bestand) as Lagerwert\r\nFROM   filiale f, produkt p, sortiment s\r\nWHERE  f.filnr = s.filnr\r\nAND    p.ean = s.ean\r\nAND    p.kategorie = 'Ersatz'\r\nGROUP BY f.filnr, f.plz\r\nHAVING SUM(p.ekpreis * s.bestand) > 300000	submission
8b0388b1-2c3e-3b94-8a8a-bddfff00b4da	3	diagnoseLevel
8b0388b1-2c3e-3b94-8a8a-bddfff00b4da	diagnose	action
533fefb5-0faa-360f-9f2b-603d7ca70b7d	3	diagnoseLevel
533fefb5-0faa-360f-9f2b-603d7ca70b7d	diagnose	action
8b0388b1-2c3e-3b94-8a8a-bddfff00b4da	SELECT rp.rechnungnr, rp.datum as Datum, p.kategorie, COUNT(*) as Anzahl, \r\n       SUM(rp.einzelpreis*rp.menge) AS rechnungsbetrag\r\nFROM   rechnungpos rp, produkt p\r\nWHERE  rp.ean=p.ean\r\nAND    NOT EXISTS (SELECT *\r\n                   FROM   rechnungpos rp2, produkt p2\r\n                   WHERE  rp2.ean=p2.ean\r\n                   AND    (rp2.rechnungnr=rp.rechnungnr AND rp2.datum=rp.datum)AND p2.kategorie<>p.kategorie)\r\nGROUP BY rp.rechnungnr, rp.datum, p.kategorie\r\n	submission
9e9dc8a4-6793-396f-9343-f4c754a6dc7c	3	diagnoseLevel
9e9dc8a4-6793-396f-9343-f4c754a6dc7c	diagnose	action
9e9dc8a4-6793-396f-9343-f4c754a6dc7c	(SELECT k.kundenr, k.name\r\nFROM   kunde k, rechnung r, rechnungpos rp, produkt p\r\nWHERE  k.kundenr=r.kundenr\r\nAND    r.rechnungnr=rp.rechnungnr\r\nAND    r.datum=rp.datum\r\nAND    rp.ean=p.ean\r\nAND    p.kategorie='Audio')\r\nINTERSECT\r\n(SELECT k.kundenr, k.name\r\nFROM   kunde k, rechnung r, rechnungpos rp, produkt p\r\nWHERE  k.kundenr=r.kundenr\r\nAND    r.rechnungnr=rp.rechnungnr\r\nAND    r.datum=rp.datum\r\nAND    rp.ean=p.ean\r\nAND    p.kategorie='Sonstiges')\r\nMINUS\r\n(SELECT k.kundenr, k.name\r\nFROM   kunde k, rechnung r, rechnungpos rp, produkt p\r\nWHERE  k.kundenr=r.kundenr\r\nAND    r.rechnungnr=rp.rechnungnr\r\nAND    r.datum=rp.datum\r\nAND    rp.ean=p.ean\r\nAND    p.kategorie='Pflege')\r\n	submission
104e2f83-5535-32fa-bcd8-d2b2ebc5b7e6	3	diagnoseLevel
104e2f83-5535-32fa-bcd8-d2b2ebc5b7e6	diagnose	action
104e2f83-5535-32fa-bcd8-d2b2ebc5b7e6	SELECT audio.kundenr, audio.umsatz As Umsatz_Audio, sonst.umsatz as Umsatz_Sonstiges\r\nFROM\r\n(SELECT k.kundenr, k.name, SUM(rp.menge*rp.einzelpreis) umsatz\r\nFROM   kunde k, rechnung r, rechnungpos rp, produkt p\r\nWHERE  k.kundenr=r.kundenr\r\nAND    r.rechnungnr=rp.rechnungnr\r\nAND    r.datum=rp.datum\r\nAND    rp.ean=p.ean\r\nAND    p.kategorie='Audio'\r\nGROUP BY k.kundenr, k.name) audio,\r\n(SELECT k.kundenr, k.name, SUM(rp.menge*rp.einzelpreis) umsatz\r\nFROM   kunde k, rechnung r, rechnungpos rp, produkt p\r\nWHERE  k.kundenr=r.kundenr\r\nAND    r.rechnungnr=rp.rechnungnr\r\nAND    r.datum=rp.datum\r\nAND    rp.ean=p.ean\r\nAND    p.kategorie='Sonstiges'\r\nGROUP BY k.kundenr, k.name) sonst\r\nWHERE  audio.kundenr=sonst.kundenr\r\n	submission
e7d02e84-d76d-3a6b-a4d7-da996003e696	3	diagnoseLevel
e7d02e84-d76d-3a6b-a4d7-da996003e696	diagnose	action
e7d02e84-d76d-3a6b-a4d7-da996003e696	SELECT MIN(TO_DATE(rp.datum)) as Tag\r\nFROM   rechnungpos rp\r\nWHERE  (SELECT SUM(rp2.menge*rp2.einzelpreis)\r\n        FROM   rechnungpos rp2\r\n        WHERE  rp2.datum<=rp.datum) > 0.5 *\r\n       (SELECT SUM(rp3.menge*rp3.einzelpreis)\r\n        FROM   rechnungpos rp3)\r\n	submission
e8a36ddf-bd98-373e-af93-18a18778e078	3	diagnoseLevel
e8a36ddf-bd98-373e-af93-18a18778e078	diagnose	action
e8a36ddf-bd98-373e-af93-18a18778e078	SELECT f.filnr, f.plz, SUM(p.ekpreis * s.bestand) as Lagerwert\r\nFROM   filiale f, produkt p, sortiment s\r\nWHERE  f.filnr = s.filnr\r\nAND    p.ean = s.ean\r\nAND    p.kategorie = 'Ersatz'\r\nGROUP BY f.filnr, f.plz\r\nHAVING SUM(p.ekpreis * s.bestand) > \r\n                 (SELECT AVG(SUM(p1.ekpreis * s1.bestand))\r\n                  FROM   produkt p1, sortiment s1\r\n                  WHERE  p1.ean = s1.ean\r\n                  AND    p1.kategorie = 'Ersatz'\r\n                  GROUP BY s1.filnr)	submission
1ac08f67-7dbf-3925-b36b-53aad8c45717	3	diagnoseLevel
1ac08f67-7dbf-3925-b36b-53aad8c45717	diagnose	action
1ac08f67-7dbf-3925-b36b-53aad8c45717	SELECT f.filnr, p.kategorie, SUM(rp.menge*rp.einzelpreis) Umsatz, d.avg_umsatz\r\nFROM   filiale f, produkt p, rechnung r, rechnungpos rp,\r\n       (SELECT p1.kategorie, \r\n               SUM(rp1.menge*rp1.einzelpreis)/COUNT(DISTINCT r1.filnr) \r\n               AS avg_umsatz\r\n        FROM   rechnung r1, rechnungpos rp1, produkt p1\r\n        WHERE  r1.rechnungnr = rp1.rechnungnr AND r1.datum = rp1.datum\r\n        AND    rp1.ean = p1.ean\r\n        GROUP BY p1.kategorie) d\r\nWHERE  f.filnr=r.filnr\r\nAND    rp.ean=p.ean\r\nAND    r.rechnungnr=rp.rechnungnr AND r.datum = rp.datum\r\nAND    p.kategorie=d.kategorie\r\nGROUP BY f.filnr, p.kategorie, d.avg_umsatz\r\n	submission
20a2c5b0-a3dd-3555-ba16-c16254a70c47	3	diagnoseLevel
20a2c5b0-a3dd-3555-ba16-c16254a70c47	diagnose	action
20a2c5b0-a3dd-3555-ba16-c16254a70c47	SELECT (Bestell.BMenge - Liefer.LMenge) As Menge\r\n\tFROM\r\n\t(SELECT SUM(bp.Menge) AS BMenge \r\n\tFROM Bauprodukt p, Bestellposition bp, Bestellung b \tWHERE p.prod_nr = bp.prod_nr\r\n\tAND bp.bestell_nr = b.bestell_nr\r\n\tAND p.Bezeichnung = 'Zementsack'\r\n\tAND b.Anschrift = 'Uniweg 1')  Bestell,\r\n\t(SELECT SUM(lp.Menge) AS LMenge\r\n\tFROM Bauprodukt p, Bestellposition bp, Lieferposition lp, Bestellung b\r\n\tWHERE p.prod_nr = bp.prod_nr\r\n\tAND bp.bestell_nr = lp.bestell_nr\r\n\tAND bp.pos_nr = lp.bestellpos_nr\r\n\tAND bp.bestell_nr = b.bestell_nr\r\n\tAND p.Bezeichnung = 'Zementsack'\r\nAND b.Anschrift = 'Uniweg 1') Liefer;	submission
efdd47c4-f1b0-316a-bf4b-ba2adc3f172a	3	diagnoseLevel
efdd47c4-f1b0-316a-bf4b-ba2adc3f172a	diagnose	action
efdd47c4-f1b0-316a-bf4b-ba2adc3f172a	SELECT AVG(COUNT (*)) AS ANZAHL FROM human p, parent par WHERE ParentName = name AND gender = 'f' GROUP BY name	submission
53a4040f-84c9-3b51-98ed-62992bda6951	3	diagnoseLevel
53a4040f-84c9-3b51-98ed-62992bda6951	diagnose	action
53a4040f-84c9-3b51-98ed-62992bda6951	SELECT name FROM human WHERE (age < 18) AND NOT EXISTS (SELECT * FROM parent WHERE name = ChildName)	submission
dfb3d354-ba9c-36a6-844e-f1faf127c662	3	diagnoseLevel
dfb3d354-ba9c-36a6-844e-f1faf127c662	diagnose	action
dfb3d354-ba9c-36a6-844e-f1faf127c662	SELECT p.ChildName As Name FROM parent p, parent geschwister WHERE p.ParentName = geschwister.ParentName \r\nGROUP BY p.ChildName HAVING COUNT (distinct geschwister.ChildName) >\r\n(SELECT AVG(COUNT(distinct g.ChildName)) FROM parent p2, parent g WHERE p2.ParentName = g.ParentName \r\nGROUP BY p2.ChildName)	submission
c5f9cfb0-0ee4-30c1-9b70-69d0c3f02dec	3	diagnoseLevel
c5f9cfb0-0ee4-30c1-9b70-69d0c3f02dec	diagnose	action
c5f9cfb0-0ee4-30c1-9b70-69d0c3f02dec	SELECT count (*) As Anzahl FROM human vater, human mutter, parent vrel, parent mrel WHERE (vater.age - mutter.age >= 4) AND vater.name = vrel.ParentName AND mutter.name = mrel.ParentName AND vrel.ChildName = mrel.ChildName AND mutter.gender = 'f' AND vater.gender = 'm'	submission
0e1decde-6009-3817-a74d-3f239f2c04bd	3	diagnoseLevel
0e1decde-6009-3817-a74d-3f239f2c04bd	diagnose	action
0e1decde-6009-3817-a74d-3f239f2c04bd	SELECT AVG(MIN(p.age - enkel.age)) AS DURCHSCHNITTSALTER\r\nFROM human p, human enkel, parent ist_kind, parent ist_enkel\r\nWHERE (p.name = ist_kind.ParentName)\r\nAND (ist_kind.ChildName = ist_enkel.ParentName)\r\nAND (ist_enkel.ChildName = enkel.name)\r\nAND (p.gender = 'm') GROUP BY p.name	submission
47582ccd-6703-3fe1-98b1-ada4c080e1b9	3	diagnoseLevel
47582ccd-6703-3fe1-98b1-ada4c080e1b9	diagnose	action
31694bb7-3e46-34f1-b1a5-24e31db7cade	SELECT AVG(gross) AS durchschnittsgroesse FROM wohnung	submission
47582ccd-6703-3fe1-98b1-ada4c080e1b9	select distinct p.persnr, p.name, p.beruf\r\nfrom   person p, mietet m, wohnung w\r\nwhere  p.persnr=m.mieternr\r\nand    m.wohnnr=w.wohnnr\r\nand    m.bis=TO_DATE('31-12-2099','DD-MM-YYYY') and    m.preis <= 1100\r\nand    w.bezirk=4\r\norder by p.name	submission
26800922-e577-3d1a-93da-84f4bc6fa87d	3	diagnoseLevel
26800922-e577-3d1a-93da-84f4bc6fa87d	diagnose	action
26800922-e577-3d1a-93da-84f4bc6fa87d	select distinct w.wohnnr, w.gross, p.name\r\nfrom   wohnung w, person p, mietet m, person p1\r\nwhere  w.eigentuemer=p.persnr\r\nand    w.gross > 50\r\nand    m.wohnnr=w.wohnnr \r\nand    p1.persnr=m.mieternr\r\nand    p1.stand='verheiratet'\r\norder by w.wohnnr	submission
2650e6ee-f1fa-37fb-ab25-46a0569e0179	3	diagnoseLevel
2650e6ee-f1fa-37fb-ab25-46a0569e0179	diagnose	action
2650e6ee-f1fa-37fb-ab25-46a0569e0179	select count(*) Anzahl, sum(m.preis) Gesamtsumme, avg(m.preis/w.gross) Durchschnittsmiete\r\nfrom   mietet m, wohnung w\r\nwhere  m.wohnnr=w.wohnnr\r\nand    m.bis=TO_DATE('31-12-2099','DD-MM-YYYY');	submission
479b2a11-a991-3480-b365-b3e5354694a2	3	diagnoseLevel
479b2a11-a991-3480-b365-b3e5354694a2	diagnose	action
479b2a11-a991-3480-b365-b3e5354694a2	select p.persnr, p.name, count(*) AnzahlWohnungen, sum(w.gross) Gesamtwohnflaeche\r\nfrom   person p, wohnung w\r\nwhere  p.persnr=w.eigentuemer\r\ngroup by p.persnr, p.name\r\norder by count(*) desc, p.persnr	submission
623e4382-17fc-3511-b4d7-8b7e25571d55	3	diagnoseLevel
623e4382-17fc-3511-b4d7-8b7e25571d55	diagnose	action
623e4382-17fc-3511-b4d7-8b7e25571d55	select w.wohnnr, w.gross, count(*) AnzahlMietverh, avg(m.preis/w.gross) D_Miete, max(m.preis/w.gross) Hoechstmiete\r\nfrom   wohnung w, mietet m\r\nwhere  w.wohnnr=m.wohnnr\r\ngroup by w.wohnnr, w.gross\r\nhaving avg(m.preis/w.gross) > 15\r\norder by w.wohnnr	submission
2d6dceff-14ce-37b7-a57a-a35a6a4b741a	3	diagnoseLevel
2d6dceff-14ce-37b7-a57a-a35a6a4b741a	diagnose	action
2d6dceff-14ce-37b7-a57a-a35a6a4b741a	select w.wohnnr, w.gross, count(*) Anzahlmietverh, avg(m.preis/w.gross) D_Miete, max(m.preis/w.gross) Hoechstmiete\r\nfrom   wohnung w, mietet m\r\nwhere  w.wohnnr=m.wohnnr\r\ngroup by w.wohnnr, w.gross\r\nhaving max(m.preis/w.gross) > \r\n          (select avg(max(m1.preis/w1.gross))\r\n           from   wohnung w1, mietet m1\r\n           where  w1.wohnnr=m1.wohnnr\r\n           group by w1.wohnnr)\r\norder by w.wohnnr	submission
f9face7d-589e-376e-93dc-d12f4df64a83	3	diagnoseLevel
f9face7d-589e-376e-93dc-d12f4df64a83	diagnose	action
f9face7d-589e-376e-93dc-d12f4df64a83	select distinct w.bezirk\r\nfrom   wohnung w\r\nwhere  not exists\r\n       (select * \r\n        from   wohnung w1\r\n        where  w1.bezirk=w.bezirk\r\n        and    not exists\r\n               (select *\r\n                from   mietet m\r\n                where  m.bis=TO_DATE('31-12-2099','DD-MM-YYYY')                and    m.wohnnr=w1.wohnnr))\r\norder by w.bezirk	submission
87bd0548-53ef-3b0f-8415-c6d31e43fbc6	3	diagnoseLevel
87bd0548-53ef-3b0f-8415-c6d31e43fbc6	diagnose	action
87bd0548-53ef-3b0f-8415-c6d31e43fbc6	select w.bezirk, count(*) Anzahl_Wohnungen\r\nfrom   wohnung w\r\nwhere  not exists\r\n       (select *\r\n        from   mietet m, wohnung w1\r\n        where  m.wohnnr=w1.wohnnr\r\n        and    m.bis=TO_DATE('31-12-2099','DD-MM-YYYY')\r\n        and    w1.bezirk=w.bezirk)\r\ngroup by w.bezirk\r\norder by w.bezirk	submission
693ebc8d-7561-3634-a2cc-4ec5b5f16ef3	3	diagnoseLevel
693ebc8d-7561-3634-a2cc-4ec5b5f16ef3	diagnose	action
693ebc8d-7561-3634-a2cc-4ec5b5f16ef3	select w.eigentuemer, COUNT(*) Anzahl_Mietverh, COUNT(DISTINCT w.wohnnr) Anzahl_Wohnungen, MAX(m.preis/w.gross) Hoechstmiete\r\nfrom   wohnung w, mietet m\r\nwhere  w.wohnnr=m.wohnnr\r\ngroup by w.eigentuemer\r\norder by w.eigentuemer	submission
7711a2d4-b7dd-382a-b10c-1028a949501a	3	diagnoseLevel
7711a2d4-b7dd-382a-b10c-1028a949501a	diagnose	action
7711a2d4-b7dd-382a-b10c-1028a949501a	select w.eigentuemer, m.preis\r\nfrom   wohnung w, mietet m\r\nwhere  w.wohnnr=m.wohnnr\r\nand    not exists \r\n          (select *\r\n           from   mietet m1, wohnung w1\r\n           where  m1.wohnnr=w1.wohnnr\r\n           and    w1.eigentuemer=w.eigentuemer\r\n           and    m1.preis > m.preis)\r\nand    not exists\r\n          (select *\r\n           from   mietet m1, wohnung w1\r\n           where  m1.wohnnr=w1.wohnnr\r\n           and    w1.eigentuemer=w.eigentuemer\r\n           and    m1.von > m.von)   	submission
12aa43e6-0527-34ff-ad4e-4ac630239e0b	3	diagnoseLevel
12aa43e6-0527-34ff-ad4e-4ac630239e0b	diagnose	action
12aa43e6-0527-34ff-ad4e-4ac630239e0b	select o1.plz as plz1, o1.name ort1, o2.plz plz2, o2.name ort2,\r\n       l2.beiKM - l1.beiKM as entfernung,\r\n       s.name, s.bezeichnung\r\nfrom ort o1, ort o2, liegtAnStrasse l1, liegtAnStrasse l2, strasse s\r\nwhere o1.plz = l1.ort AND\r\n      o2.plz = l2.ort AND\r\n      s.name = l1.strassenname AND\r\n      l1.strassenname = l2.strassenname AND\r\n      l2.beiKM > l1.beiKM AND\r\n      NOT exists (\r\n        select *\r\n        from liegtAnStrasse ldazw\r\n        where ldazw.strassenname = l1.strassenname AND\r\n              ldazw.beiKM > l1.beiKM AND\r\n              ldazw.beiKM < l2.beiKM)	submission
d5d7a5b1-9c6e-3a50-934d-c9b7cedb8f9d	3	diagnoseLevel
d5d7a5b1-9c6e-3a50-934d-c9b7cedb8f9d	diagnose	action
d5d7a5b1-9c6e-3a50-934d-c9b7cedb8f9d	SELECT DISTINCT p.persnr, p.name\r\nFROM   person p, wohnung w, mietet m\r\nWHERE  p.persnr=w.eigentuemer AND w.wohnnr=m.wohnnr\r\nAND    m.bis=TO_DATE('31-12-2099','DD-MM-YYYY') AND p.stand='ledig'\r\nAND    m.preis/w.gross > 7\r\nORDER BY p.persnr\r\n	submission
3698b07a-9e3e-3e63-9d27-6cb2e6a22a96	3	diagnoseLevel
3698b07a-9e3e-3e63-9d27-6cb2e6a22a96	diagnose	action
3698b07a-9e3e-3e63-9d27-6cb2e6a22a96	SELECT w.wohnnr, w.eigentuemer, p1.name as VName, m.mieternr, p2.name as MName\r\nFROM   wohnung w, person p1, person p2, mietet m\r\nWHERE  w.wohnnr=m.wohnnr AND w.eigentuemer=p1.persnr AND m.mieternr=p2.persnr\r\nAND    w.bezirk=1 AND m.bis=TO_DATE('31-12-2099','DD-MM-YYYY') ORDER BY w.wohnnr\r\n	submission
40cdd631-9ff8-3ce0-b250-838c7acacb90	3	diagnoseLevel
40cdd631-9ff8-3ce0-b250-838c7acacb90	diagnose	action
40cdd631-9ff8-3ce0-b250-838c7acacb90	SELECT DISTINCT w.wohnnr, w.bezirk FROM   wohnung w, mietet m WHERE  w.wohnnr=m.wohnnr AND    w.wohnnr NOT IN (SELECT m2.wohnnr FROM   mietet m2 WHERE  m2.bis=TO_DATE('31-12-2099','DD-MM-YYYY'))ORDER BY w.bezirk, w.wohnnr	submission
3846570b-4f6a-33f4-92e6-9fc40239288e	3	diagnoseLevel
3846570b-4f6a-33f4-92e6-9fc40239288e	diagnose	action
2ddd7ad3-a07a-3538-b241-7fd043b73407	SELECT p.persnr, p.name, w.gross, m.preis FROM person p, wohnung w, mietet m WHERE w.bezirk = 2 AND p.persnr = w.eigentuemer AND w.wohnnr = m.wohnnr	submission
31694bb7-3e46-34f1-b1a5-24e31db7cade	3	diagnoseLevel
3846570b-4f6a-33f4-92e6-9fc40239288e	SELECT DISTINCT p.persnr, p.name\r\nFROM   person p, wohnung w\r\nWHERE  p.persnr=w.eigentuemer\r\nAND    NOT EXISTS (SELECT * \r\n                   FROM   wohnung w2\r\n                   WHERE  w2.eigentuemer=p.persnr\r\n                   AND NOT EXISTS (SELECT * \r\n                                   FROM   mietet m\r\n                                   WHERE  m.wohnnr=w2.wohnnr))\r\n	submission
c4df6363-5c9e-334b-8224-6290f9b62e41	3	diagnoseLevel
c4df6363-5c9e-334b-8224-6290f9b62e41	diagnose	action
c4df6363-5c9e-334b-8224-6290f9b62e41	SELECT w.bezirk, SUM(w.gross) FLAECHE\r\nFROM   wohnung w, mietet m\r\nWHERE  w.wohnnr=m.wohnnr AND m.bis=TO_DATE('31-12-2099','DD-MM-YYYY') GROUP BY w.bezirk\r\nHAVING SUM(w.gross)> (SELECT AVG(SUM(w1.gross))\r\n                      FROM   wohnung w1, mietet m1\r\n                      WHERE  w1.wohnnr=m1.wohnnr AND m1.bis=TO_DATE('31-12-2099','DD-MM-YYYY')                      GROUP BY w1.bezirk)\r\n	submission
ee78463e-dd63-3e1e-8f99-80a7726ea625	3	diagnoseLevel
ee78463e-dd63-3e1e-8f99-80a7726ea625	diagnose	action
ee78463e-dd63-3e1e-8f99-80a7726ea625	SELECT w.wohnnr, w.bezirk, w.gross, (m.preis/w.gross) qm_miete, d.d_miete \r\nFROM   wohnung w, mietet m,\r\n       (SELECT w1.bezirk, AVG(m1.preis/w1.gross) d_miete\r\n        FROM   wohnung w1, mietet m1\r\n        WHERE  w1.wohnnr=m1.wohnnr AND m1.bis=TO_DATE('31-12-2099','DD-MM-YYYY')\r\n        GROUP BY w1.bezirk) d\r\nWHERE  w.wohnnr=m.wohnnr AND m.bis=TO_DATE('31-12-2099','DD-MM-YYYY') AND    w.bezirk=d.bezirk\r\n	submission
866ba8a9-9740-3f3b-86d8-dc23a72e97c1	3	diagnoseLevel
866ba8a9-9740-3f3b-86d8-dc23a72e97c1	diagnose	action
866ba8a9-9740-3f3b-86d8-dc23a72e97c1	SELECT p.persnr, p.name, COUNT(*) as MIETVERHAELTNISSE\r\nFROM   person p, wohnung w, mietet m\r\nWHERE  p.persnr=w.eigentuemer AND w.wohnnr=m.wohnnr\r\nGROUP BY p.persnr, p.name\r\nHAVING COUNT(*)=COUNT(DISTINCT m.mieternr)\r\n	submission
ff177f44-9970-34d8-9dc7-194150b47c29	3	diagnoseLevel
ff177f44-9970-34d8-9dc7-194150b47c29	diagnose	action
ff177f44-9970-34d8-9dc7-194150b47c29	SELECT m1.mieternr, p.name, m1.wohnnr Wohnung1nr, m1.von Wohnung1von, m1.bis Wohnung1bis, m2.wohnnr Wohnung2nr, m2.von Wohnung2von, m2.bis Wohnung2bis\r\nFROM   mietet m1, person p, mietet m2, wohnung w1, wohnung w2\r\nWHERE  m1.mieternr=p.persnr AND m1.mieternr=m2.mieternr \r\nAND    m1.wohnnr=w1.wohnnr AND m2.wohnnr=w2.wohnnr\r\nAND    w1.bezirk=w2.bezirk\r\nAND    m1.wohnnr < m2.wohnnr\r\nAND    ((m1.von BETWEEN m2.von AND m2.bis) OR (m2.von BETWEEN m1.von AND m1.bis))\r\n	submission
662d9b0c-04dc-34de-b83e-42443c3e97e1	3	diagnoseLevel
662d9b0c-04dc-34de-b83e-42443c3e97e1	diagnose	action
662d9b0c-04dc-34de-b83e-42443c3e97e1	SELECT bezirk, SUM(erloes) as MIETEINNAHMEN FROM \r\n((SELECT w.bezirk, SUM(ROUND(MONTHS_BETWEEN(m.bis, m.von))*m.preis) erloes\r\nFROM   wohnung w, mietet m\r\nWHERE  w.wohnnr=m.wohnnr\r\nAND    m.bis <> TO_DATE('31-12-2099','DD-MM-YYYY')\r\nGROUP BY w.bezirk)\r\nUNION\r\n(SELECT w.bezirk, SUM(ROUND(MONTHS_BETWEEN(SYSDATE, m.von))*m.preis) erloes\r\nFROM   wohnung w, mietet m\r\nWHERE  w.wohnnr=m.wohnnr\r\nAND    m.bis = TO_DATE('31-12-2099','DD-MM-YYYY')\r\nGROUP BY w.bezirk))\r\nGROUP BY bezirk\r\nHAVING SUM(erloes)=(SELECT MAX(SUM(erloes)) \r\n                    FROM ((SELECT w.bezirk, SUM(ROUND(MONTHS_BETWEEN(m.bis, m.von))*m.preis) erloes\r\n                           FROM   wohnung w, mietet m\r\n                           WHERE  w.wohnnr=m.wohnnr\r\n                           AND    m.bis <> TO_DATE('31-12-2099','DD-MM-YYYY')\r\n                           GROUP BY w.bezirk)\r\n                          UNION\r\n                          (SELECT w.bezirk, SUM(ROUND(MONTHS_BETWEEN(SYSDATE, m.von))*m.preis) erloes\r\n                           FROM   wohnung w, mietet m\r\n                           WHERE  w.wohnnr=m.wohnnr\r\n                           AND    m.bis = TO_DATE('31-12-2099','DD-MM-YYYY')\r\n                           GROUP BY w.bezirk))\r\n                          GROUP BY bezirk)\r\n	submission
7705947c-374b-3075-ab7c-bafaca31a5d5	3	diagnoseLevel
7705947c-374b-3075-ab7c-bafaca31a5d5	diagnose	action
7705947c-374b-3075-ab7c-bafaca31a5d5	SELECT a1.name, a1.nationality, r1.title, r1.releaseDate FROM artist a1, record r1, distribute d1 WHERE a1.name=r1.artistName AND r1.recordId=d1.recordId AND d1.media='MD' AND NOT EXISTS (SELECT * FROM record r2, distribute d2 WHERE r2.recordId=d2.recordId AND d2.media='MD' AND r2.releaseDate<r1.releaseDate)	submission
649aa14f-c8a7-3277-a1c0-f339d30408ba	3	diagnoseLevel
649aa14f-c8a7-3277-a1c0-f339d30408ba	diagnose	action
649aa14f-c8a7-3277-a1c0-f339d30408ba	SELECT r.recordid, r.artistName, r.title, r.releaseDate, r.type FROM record r, distribute d WHERE d.recordId=r.recordId GROUP BY r.recordId, r.artistName, r.title, r.releaseDate, r.type HAVING COUNT(*) = (SELECT COUNT(DISTINCT media) FROM distribute)	submission
0cb959d4-6663-387c-85b9-8478d0a10495	3	diagnoseLevel
0cb959d4-6663-387c-85b9-8478d0a10495	diagnose	action
0cb959d4-6663-387c-85b9-8478d0a10495	SELECT g.genreId, g.name\r\nFROM genre g\r\nWHERE NOT EXISTS (\r\nSELECT *\r\nFROM record r, distribute d\r\nWHERE r.genreId=g.genreId AND r.recordId=d.recordId AND d.media='DVD'\r\n);	submission
20df41e6-b7cc-37a2-9ab0-fb4e5132cd10	3	diagnoseLevel
20df41e6-b7cc-37a2-9ab0-fb4e5132cd10	diagnose	action
20df41e6-b7cc-37a2-9ab0-fb4e5132cd10	SELECT r1.recordId, r1.artistName, r1.title FROM record r1 WHERE \r\nr1.genreId IN ( \r\n   SELECT r2.genreId \r\n   FROM record r2 \r\n   WHERE r2.type='Single' \r\n   GROUP BY r2.genreId \r\n   HAVING COUNT(*) >= ALL \r\n     (SELECT COUNT(*) FROM record r3 WHERE r3.type='Single' GROUP BY \r\n     r3.genreId)) \r\nORDER BY r1.artistName, r1.title;	submission
1f9840a5-5f5a-37e4-9026-f19c50f8a599	3	diagnoseLevel
1f9840a5-5f5a-37e4-9026-f19c50f8a599	diagnose	action
1f9840a5-5f5a-37e4-9026-f19c50f8a599	SELECT r1.artistname, AVG(v.vkCD)*100/SUM(d1.price) AS CDAnteil \r\nFROM record r1, distribute d1, \r\n     (SELECT r2.artistname, SUM(d2.price) AS vkCD \r\n     FROM record r2, distribute d2 \r\n     WHERE r2.recordId=d2.recordId AND d2.media='CD' \r\n     GROUP BY r2.artistname \r\n     ) v\r\nWHERE r1.recordId=d1.recordId AND r1.artistname=v.artistname \r\nGROUP BY r1.artistname;	submission
d047e565-7013-33fd-9ba8-7234ffffb3f1	3	diagnoseLevel
d047e565-7013-33fd-9ba8-7234ffffb3f1	diagnose	action
d047e565-7013-33fd-9ba8-7234ffffb3f1	select a.name, a.nationality, r.title \r\nfrom artist a, record r, distribute d \r\nwhere a.name=r.artistname and r.recordid=d.recordid and d.media='MD' \r\norder by a.name, r.title;	submission
2519dbc8-6f96-3669-a6db-b3e9e35ffbaa	3	diagnoseLevel
2519dbc8-6f96-3669-a6db-b3e9e35ffbaa	diagnose	action
2519dbc8-6f96-3669-a6db-b3e9e35ffbaa	select a.name, a.nationality, min(r.releasedate) as Datum \r\nfrom artist a, record r, distribute d \r\nwhere a.name=r.artistname and r.recordid=d.recordid and d.media='CD' \r\ngroup by a.name, a.nationality \r\norder by a.name, a.nationality;	submission
533fefb5-0faa-360f-9f2b-603d7ca70b7d	select r.title, r.artistname, d.price \r\nfrom  record r, distribute d \r\nwhere r.recordid=d.recordid and d.media='CD' \r\nand not exists \r\n      (select * \r\n       from   record r1, distribute d1 \r\n       where  r1.recordid=d1.recordid \r\n       and    d1.media='CD' \r\n       and    d1.price>d.price) \r\norder by r.title; 	submission
adf392ee-1784-38d9-bf22-11f502bdbe33	3	diagnoseLevel
adf392ee-1784-38d9-bf22-11f502bdbe33	diagnose	action
adf392ee-1784-38d9-bf22-11f502bdbe33	select r.recordid, r.title, count(*) Tracks \r\nfrom   record r, track t \r\nwhere  r.recordid=t.recordid \r\ngroup by r.recordid, r.title \r\nhaving count(*) = (select max(count(*)) \r\n                    from   record r1, track t1 \r\n                    where  r1.recordid=t1.recordid \r\n                    group by r1.recordid) order by r.title;	submission
e77f4e07-5c19-3922-bc1c-40e786c109d4	3	diagnoseLevel
e77f4e07-5c19-3922-bc1c-40e786c109d4	diagnose	action
e77f4e07-5c19-3922-bc1c-40e786c109d4	select s.kennzahl as STR_KZ, s.bezeichnung as STR_BEZ, f.kurzbez as FK_KBEZ, f.bezeichnung as FK_BEZ\r\nfrom studienrichtung s, fakultaet f\r\nwhere s.gehoert_zu = f.kurzbez AND\r\n  not exists(\r\n     select *\r\n     from koje k\r\n     where k.gemietet_von = s.kennzahl);	submission
a18d294b-49ad-3e9f-951c-f37daaa028f1	3	diagnoseLevel
a18d294b-49ad-3e9f-951c-f37daaa028f1	diagnose	action
a18d294b-49ad-3e9f-951c-f37daaa028f1	select s.kennzahl, s.bezeichnung, sum(flaeche) as Flaeche\r\nfrom studienrichtung s, koje k\r\nwhere k.gemietet_von = s.kennzahl\r\ngroup by s.kennzahl, s.bezeichnung;	submission
37fbaae0-6415-3b70-8844-10461178f95f	3	diagnoseLevel
37fbaae0-6415-3b70-8844-10461178f95f	diagnose	action
37fbaae0-6415-3b70-8844-10461178f95f	select f.kurzbez, f.bezeichnung, count(*) Anzahl\r\nfrom fakultaet f, studienrichtung s, koje k\r\nwhere s.gehoert_zu = f.kurzbez AND\r\n      k.gemietet_von = s.kennzahl\r\ngroup by f.kurzbez, f.bezeichnung\r\nhaving count (standnr) = (\r\n select MAX (count(*))\r\n from studienrichtung s2, koje k2\r\n where k2.gemietet_von = s2.kennzahl\r\n group by s2.gehoert_zu);	submission
f549d43e-5b0b-3c72-8d0d-917ce3e86642	3	diagnoseLevel
f549d43e-5b0b-3c72-8d0d-917ce3e86642	diagnose	action
f549d43e-5b0b-3c72-8d0d-917ce3e86642	select s1.kennzahl SKZ1, s1.bezeichnung BEZ1, k1.standnr SN1, k2.standnr SN2, s2.kennzahl SKZ2, s2.bezeichnung BEZ2\r\nfrom studienrichtung s1, koje k1, studienrichtung s2, koje k2\r\nwhere k1.gemietet_von = s1.kennzahl AND\r\n      k2.gemietet_von = s2.kennzahl AND\r\n      k1.linkernachbar = k2.standnr AND\r\n      k1.standnr > k2.standnr AND\r\n      k1.gemietet_von <> k2.gemietet_von;	submission
dedc6364-d8d5-3388-be4a-b860425c5655	3	diagnoseLevel
dedc6364-d8d5-3388-be4a-b860425c5655	diagnose	action
dedc6364-d8d5-3388-be4a-b860425c5655	select g.genreId, g.name, count(*) anzahl\r\nfrom genre g, record r, distribute d\r\nwhere g.genreid=r.genreid and r.recordid=d.recordid and d.media='CD' \r\ngroup by g.genreid, g.name;	submission
2ae5dc15-39fd-3fed-9093-8bd31fdda2d8	3	diagnoseLevel
2ae5dc15-39fd-3fed-9093-8bd31fdda2d8	diagnose	action
2ae5dc15-39fd-3fed-9093-8bd31fdda2d8	SELECT name FROM  human WHERE  gender='m' AND    age > 30	submission
fbb7495e-29d0-3d15-86c8-7b1c243b6c02	3	diagnoseLevel
fbb7495e-29d0-3d15-86c8-7b1c243b6c02	diagnose	action
fbb7495e-29d0-3d15-86c8-7b1c243b6c02	SELECT KursNr FROM kurs WHERE Lektor='Mueller'	submission
6731921f-2cef-37b2-824a-ff6966e7e799	3	diagnoseLevel
6731921f-2cef-37b2-824a-ff6966e7e799	diagnose	action
6731921f-2cef-37b2-824a-ff6966e7e799	SELECT * FROM student WHERE Land='Deutschland' OR Land='Schweiz' ORDER BY land, name	submission
7d1930b2-f24b-357b-934d-e4d2002806bc	3	diagnoseLevel
7d1930b2-f24b-357b-934d-e4d2002806bc	diagnose	action
7d1930b2-f24b-357b-934d-e4d2002806bc	SELECT DISTINCT(s.Land) AS Land FROM student s, kurs k, belegung b WHERE b.MatrikelNr=s.MatrikelNr AND b.KursNr=k.KursNr AND k.Name='Datenbanken'	submission
32b4976a-e66a-3ada-8caf-753773bea24d	3	diagnoseLevel
32b4976a-e66a-3ada-8caf-753773bea24d	diagnose	action
32b4976a-e66a-3ada-8caf-753773bea24d	SELECT Land, count(*) AS AnzahlStudierende FROM student GROUP BY Land	submission
53a833bd-a168-3045-8aab-d88b814ae511	3	diagnoseLevel
53a833bd-a168-3045-8aab-d88b814ae511	diagnose	action
53a833bd-a168-3045-8aab-d88b814ae511	SELECT Land, count(*) AS AnzahlStudierende FROM student WHERE Land <> 'Austria' GROUP BY Land	submission
dcf1af3a-56ad-3ab0-89c7-3765ecc15598	3	diagnoseLevel
dcf1af3a-56ad-3ab0-89c7-3765ecc15598	diagnose	action
dcf1af3a-56ad-3ab0-89c7-3765ecc15598	SELECT Land, count(*) AS AnzahlStudierende FROM student GROUP BY Land HAVING count(*)>10	submission
de15644c-821a-30f4-873b-62c5e2865dcb	3	diagnoseLevel
de15644c-821a-30f4-873b-62c5e2865dcb	diagnose	action
de15644c-821a-30f4-873b-62c5e2865dcb	SELECT * FROM student WHERE MatrikelNr NOT IN (SELECT MatrikelNr FROM belegung)	submission
696f0b0b-f034-37ac-8ab0-d6ccdb014f75	3	diagnoseLevel
696f0b0b-f034-37ac-8ab0-d6ccdb014f75	diagnose	action
696f0b0b-f034-37ac-8ab0-d6ccdb014f75	SELECT * FROM person ORDER BY Name, Beruf	submission
1b794f82-9131-3be2-9973-3ff12ba0c7ac	3	diagnoseLevel
1b794f82-9131-3be2-9973-3ff12ba0c7ac	diagnose	action
1b794f82-9131-3be2-9973-3ff12ba0c7ac	SELECT DISTINCT beruf FROM person	submission
c135e40a-eb82-3b54-91bc-1991d642ced5	3	diagnoseLevel
c135e40a-eb82-3b54-91bc-1991d642ced5	diagnose	action
c135e40a-eb82-3b54-91bc-1991d642ced5	SELECT name, beruf FROM person WHERE persnr = 1	submission
02eef84e-e84b-31dd-a8f7-60b064f5bbb1	3	diagnoseLevel
02eef84e-e84b-31dd-a8f7-60b064f5bbb1	diagnose	action
02eef84e-e84b-31dd-a8f7-60b064f5bbb1	SELECT eigentuemer AS persnr FROM wohnung WHERE bezirk = 2 OR bezirk = 3	submission
f0630838-1b96-376b-bade-5a2d89d6e682	3	diagnoseLevel
f0630838-1b96-376b-bade-5a2d89d6e682	diagnose	action
f0630838-1b96-376b-bade-5a2d89d6e682	SELECT persnr, name FROM person, wohnung WHERE (bezirk = 2 OR bezirk = 3) AND person.persnr = wohnung.eigentuemer	submission
2ddd7ad3-a07a-3538-b241-7fd043b73407	3	diagnoseLevel
2ddd7ad3-a07a-3538-b241-7fd043b73407	diagnose	action
31694bb7-3e46-34f1-b1a5-24e31db7cade	diagnose	action
53138fed-2054-36ec-a95e-3580af83ebda	3	diagnoseLevel
53138fed-2054-36ec-a95e-3580af83ebda	diagnose	action
53138fed-2054-36ec-a95e-3580af83ebda	SELECT bezirk, AVG(gross) AS durchschnittsgroesse FROM wohnung GROUP BY bezirk	submission
77054254-8a71-3fe9-acaf-490a81e20d48	3	diagnoseLevel
77054254-8a71-3fe9-acaf-490a81e20d48	diagnose	action
77054254-8a71-3fe9-acaf-490a81e20d48	SELECT bezirk, AVG(gross) AS durchschnittsgroesse FROM wohnung GROUP BY bezirk HAVING count(*) > 1	submission
07caa74c-0caa-346a-8f23-d00a8b5411e8	3	diagnoseLevel
07caa74c-0caa-346a-8f23-d00a8b5411e8	diagnose	action
07caa74c-0caa-346a-8f23-d00a8b5411e8	SELECT bezirk, AVG (gross) as durchschnittsgroesse FROM wohnung WHERE wohnung.eigentuemer IN (SELECT persnr FROM person WHERE stand = 'ledig') GROUP BY bezirk	submission
6b4c1045-ebd3-3c94-b356-56f242f16228	3	diagnoseLevel
6b4c1045-ebd3-3c94-b356-56f242f16228	diagnose	action
6b4c1045-ebd3-3c94-b356-56f242f16228	SELECT DISTINCT p.persnr, p.name FROM person p, wohnung w, mietet m WHERE  p.persnr=w.eigentuemer AND w.wohnnr=m.wohnnr AND m.bis=TO_DATE('31-12-2099','dd-mm-yyyy') AND p.stand='ledig' AND m.preis/w.gross > 7 ORDER BY p.persnr	submission
6bd1db14-7b2a-30fa-9093-9c92cfc164b7	3	diagnoseLevel
6bd1db14-7b2a-30fa-9093-9c92cfc164b7	diagnose	action
6bd1db14-7b2a-30fa-9093-9c92cfc164b7	SELECT w.wohnnr, w.eigentuemer AS v_persnr, p1.name as v_name, m.mieternr AS m_persnr, p2.name as m_name FROM   wohnung w, person p1, person p2, mietet m WHERE  w.wohnnr=m.wohnnr AND w.eigentuemer=p1.persnr AND m.mieternr=p2.persnr AND w.bezirk=1 AND m.bis=TO_DATE('31-12-2099','dd-mm-yyyy') ORDER BY w.wohnnr	submission
1c398510-c7ff-3b4a-bd1c-c0f0320e5ce4	3	diagnoseLevel
1c398510-c7ff-3b4a-bd1c-c0f0320e5ce4	diagnose	action
1c398510-c7ff-3b4a-bd1c-c0f0320e5ce4	SELECT DISTINCT w.wohnnr, w.bezirk FROM wohnung w, mietet m WHERE w.wohnnr=m.wohnnr AND w.wohnnr NOT IN (SELECT m2.wohnnr FROM mietet m2 WHERE  m2.bis=TO_DATE('31-12-2099','dd-mm-yyyy')) ORDER BY w.bezirk, w.wohnnr	submission
e5041203-afbe-368b-8850-42faf1da6d66	3	diagnoseLevel
e5041203-afbe-368b-8850-42faf1da6d66	diagnose	action
e5041203-afbe-368b-8850-42faf1da6d66	SELECT DISTINCT p.persnr, p.name FROM person p, wohnung w WHERE  p.persnr=w.eigentuemer AND NOT EXISTS (SELECT * FROM wohnung w2 WHERE w2.eigentuemer=p.persnr AND NOT EXISTS (SELECT * FROM mietet m WHERE  m.wohnnr=w2.wohnnr))	submission
da7eba9b-86c4-3171-9462-f074dbbfd860	3	diagnoseLevel
da7eba9b-86c4-3171-9462-f074dbbfd860	diagnose	action
da7eba9b-86c4-3171-9462-f074dbbfd860	SELECT k.kontonr, i.name, i.gebdat, i.adresse FROM   konto k, inhaber i WHERE  k.inhname=i.name AND k.gebdat=i.gebdat AND    i.adresse LIKE '%Linz%'	submission
5c5ac619-aeb9-319b-ad1b-35d046a67ac3	3	diagnoseLevel
5c5ac619-aeb9-319b-ad1b-35d046a67ac3	diagnose	action
5c5ac619-aeb9-319b-ad1b-35d046a67ac3	select k1.inhname, k1.gebdat, b.buchngnr from   konto k1, konto k2, buchung b where  k1.inhname = k2.inhname and    k1.gebdat = k2.gebdat and    k1.kontonr = b.vonkonto and    k2.kontonr = b.aufkonto order by k1.inhname, k1.gebdat, b.buchngnr desc	submission
691a82fa-a370-3adc-b1f1-30388b01332e	3	diagnoseLevel
691a82fa-a370-3adc-b1f1-30388b01332e	diagnose	action
691a82fa-a370-3adc-b1f1-30388b01332e	SELECT COUNT(*) AS Anzahl FROM konto	submission
c76eb612-b535-36ac-b5a2-922fca83e3fb	3	diagnoseLevel
c76eb612-b535-36ac-b5a2-922fca83e3fb	diagnose	action
c76eb612-b535-36ac-b5a2-922fca83e3fb	SELECT k.inhname, k.gebdat, COUNT(*) AS Anzahl FROM   konto k GROUP BY k.inhname, k.gebdat	submission
7b5899f4-962b-39ab-9cd1-d044fd18e81b	3	diagnoseLevel
7b5899f4-962b-39ab-9cd1-d044fd18e81b	diagnose	action
7b5899f4-962b-39ab-9cd1-d044fd18e81b	SELECT AVG(COUNT(*)) AS AVG_Anzahl FROM   konto k GROUP BY k.inhname, k.gebdat	submission
f0fc60f4-e522-33e5-9b2b-4b618c53d511	3	diagnoseLevel
f0fc60f4-e522-33e5-9b2b-4b618c53d511	diagnose	action
f0fc60f4-e522-33e5-9b2b-4b618c53d511	select count(distinct aufkonto) as Anzahl\r\nfrom buchung	submission
1d58b805-df19-3264-abb4-d2837e993213	3	diagnoseLevel
1d58b805-df19-3264-abb4-d2837e993213	diagnose	action
1d58b805-df19-3264-abb4-d2837e993213	select i1.name, i1.gebdat, i1.adresse, count(*) as Anzahl, sum(betrag) SUM_Betrag from   inhaber i1, konto k1, buchung b1\r\nwhere  i1.name = k1.inhname and i1.gebdat = k1.gebdat\r\nand    k1.kontonr = b1.aufkonto\r\ngroup by i1.name, i1.gebdat, i1.adresse\r\nhaving count(*)>4	submission
b3d8ca0b-472a-3656-b70c-2397136e5c85	3	diagnoseLevel
b3d8ca0b-472a-3656-b70c-2397136e5c85	diagnose	action
b3d8ca0b-472a-3656-b70c-2397136e5c85	select distinct inhname, gebdat from konto where saldo> 10000	submission
f8716ca0-649e-368a-839c-a291cac21c85	3	diagnoseLevel
f8716ca0-649e-368a-839c-a291cac21c85	diagnose	action
f8716ca0-649e-368a-839c-a291cac21c85	select i1.name, i1.gebdat, i1.adresse, count(*) anzahl, sum(betrag) sum_betrag\r\nfrom   inhaber i1, konto k1, buchung b1\r\nwhere  i1.name = k1.inhname and i1.gebdat = k1.gebdat\r\nand    k1.kontonr = b1.aufkonto\r\ngroup by i1.name, i1.gebdat, i1.adresse\r\nhaving count(*) >\r\n       (select avg(count(*))\r\n        from   konto k2, buchung b2\r\n        where  k2.kontonr = b2.aufkonto\r\n        group by k2.inhname, k2.gebdat)	submission
0c3ab6ae-921e-3146-a40a-9aeda7c4e80d	3	diagnoseLevel
0c3ab6ae-921e-3146-a40a-9aeda7c4e80d	diagnose	action
0c3ab6ae-921e-3146-a40a-9aeda7c4e80d	select i.name, i.gebdat\r\nfrom inhaber i\r\nwhere not exists (select *\r\n                  from   konto k\r\n                  where  k.inhname = i.name\r\n                  and    k.gebdat = i.gebdat\r\n                  and    not exists (select *\r\n                                     from   buchung b\r\n                                     where  k.kontonr = b.aufkonto))\r\norder by i.name, i.gebdat	submission
d7a2aa9d-09d1-3d10-874a-18d9f50eede8	3	diagnoseLevel
d7a2aa9d-09d1-3d10-874a-18d9f50eede8	diagnose	action
d7a2aa9d-09d1-3d10-874a-18d9f50eede8	select k.kontonr, k.filiale, sum(b.betrag) ksum, f.filsum\r\nfrom   konto k, buchung b,\r\n       (select k1.filiale,\r\n               sum(b1.betrag) filsum\r\n        from   konto k1, buchung b1\r\n        where  k1.kontonr = b1.aufkonto\r\n        or     k1.kontonr = b1.vonkonto\r\n        group by k1.filiale) f\r\n where  (k.kontonr=b.vonkonto\r\n or      k.kontonr=b.aufkonto)\r\n and    k.filiale = f.filiale\r\n group by k.kontonr, k.filiale, f.filsum	submission
00b1eaca-9e32-38f0-853d-dafcd9bc6cc9	3	diagnoseLevel
00b1eaca-9e32-38f0-853d-dafcd9bc6cc9	diagnose	action
00b1eaca-9e32-38f0-853d-dafcd9bc6cc9	select k.kontonr, k.filiale\r\nfrom   konto k\r\nwhere  k.kontonr not in\r\n         (select b.vonkonto\r\n          from   buchung b, konto k2\r\n          where  b.aufkonto=k2.kontonr\r\n          and    k2.inhname='Hofreiter Martin'\r\n          and    k2.gebdat='12-FEB-80')	submission
cb0497f5-1cfe-3fa9-8ac2-209619528fbd	3	diagnoseLevel
cb0497f5-1cfe-3fa9-8ac2-209619528fbd	diagnose	action
cb0497f5-1cfe-3fa9-8ac2-209619528fbd	select k.inhname, k.gebdat, b.buchngnr, b.aufkonto\r\nfrom   konto k, buchung b\r\nwhere  k.kontonr = b.aufkonto\r\nand    not exists\r\n         (select *\r\n          from   konto k2, buchung b2\r\n          where k2.kontonr=b2.aufkonto\r\n          and   k2.inhname=k.inhname\r\n          and   k2.gebdat=k.gebdat\r\n          and   b2.buchngnr < b.buchngnr)	submission
e8176054-f56e-39c8-b136-a92002ecd151	3	diagnoseLevel
e8176054-f56e-39c8-b136-a92002ecd151	diagnose	action
e8176054-f56e-39c8-b136-a92002ecd151	select coursecode from course where lecturer='Miller'	submission
12242cdd-61eb-30b2-9236-d8c966ea66fc	3	diagnoseLevel
12242cdd-61eb-30b2-9236-d8c966ea66fc	diagnose	action
12242cdd-61eb-30b2-9236-d8c966ea66fc	select * from students where country='Thailand' or country='Japan' order by country, name;	submission
2e57fd44-d7bd-3f05-ab10-fa6c1af6d1d0	3	diagnoseLevel
2e57fd44-d7bd-3f05-ab10-fa6c1af6d1d0	diagnose	action
2e57fd44-d7bd-3f05-ab10-fa6c1af6d1d0	select distinct (s.country) from students s, course c, enrollment e where e.studentId=s.studentId and e.courseCode=c.courseCode and c.name='Database';	submission
b1fb1972-ec52-39fc-b876-43df992ac628	3	diagnoseLevel
b1fb1972-ec52-39fc-b876-43df992ac628	diagnose	action
b1fb1972-ec52-39fc-b876-43df992ac628	select country, count(*) as numberOfStudents from students group by country;	submission
431f102c-7f92-39cb-87cf-176b1df10241	3	diagnoseLevel
431f102c-7f92-39cb-87cf-176b1df10241	diagnose	action
431f102c-7f92-39cb-87cf-176b1df10241	select country, count(*) as numberOfStudents from students where country <> 'Australia' group by country;	submission
a4fc6c18-4f8d-301b-9978-78797b4a09e4	3	diagnoseLevel
a4fc6c18-4f8d-301b-9978-78797b4a09e4	diagnose	action
a4fc6c18-4f8d-301b-9978-78797b4a09e4	select country, count(*) as numberOfStudents from students group by country having count(*)>=10;	submission
c86300e9-b481-3514-98cd-11ac86e617e7	3	diagnoseLevel
c86300e9-b481-3514-98cd-11ac86e617e7	diagnose	action
c86300e9-b481-3514-98cd-11ac86e617e7	select * from students where studentId not in(select studentId from enrollment);	submission
156a24ee-665e-3234-b3ee-9bd8e0e33799	3	diagnoseLevel
156a24ee-665e-3234-b3ee-9bd8e0e33799	diagnose	action
156a24ee-665e-3234-b3ee-9bd8e0e33799	SELECT * FROM hotel;	submission
9e66e323-edca-371f-a273-30ae45c23b97	3	diagnoseLevel
9e66e323-edca-371f-a273-30ae45c23b97	diagnose	action
9e66e323-edca-371f-a273-30ae45c23b97	SELECT * FROM hotel WHERE address LIKE '%London%';	submission
ccbf78a7-165e-3dfd-a778-2e9f16d923fd	3	diagnoseLevel
ccbf78a7-165e-3dfd-a778-2e9f16d923fd	diagnose	action
ccbf78a7-165e-3dfd-a778-2e9f16d923fd	SELECT name, address FROM guest WHERE UPPER(address) LIKE '%LONDON%';	submission
9c36ffa8-038f-3fa9-86b6-90dff42443ce	3	diagnoseLevel
9c36ffa8-038f-3fa9-86b6-90dff42443ce	diagnose	action
9c36ffa8-038f-3fa9-86b6-90dff42443ce	SELECT * FROM room WHERE type IN ('double','family') AND price < 40;	submission
332fc967-993a-307f-a4cc-ac2b7a0d424b	3	diagnoseLevel
332fc967-993a-307f-a4cc-ac2b7a0d424b	diagnose	action
332fc967-993a-307f-a4cc-ac2b7a0d424b	SELECT * FROM booking WHERE dateto IS NULL;	submission
3a047152-16f1-301c-9372-60c3b3a7e7c6	3	diagnoseLevel
3a047152-16f1-301c-9372-60c3b3a7e7c6	diagnose	action
3a047152-16f1-301c-9372-60c3b3a7e7c6	SELECT name, address FROM guest WHERE UPPER(address) LIKE '%LONDON%' ORDER BY name;	submission
24124851-6cb6-3794-ae06-f8edcdad3c28	3	diagnoseLevel
24124851-6cb6-3794-ae06-f8edcdad3c28	diagnose	action
24124851-6cb6-3794-ae06-f8edcdad3c28	SELECT * FROM booking WHERE dateto IS NULL ORDER BY hno, datefrom;	submission
251c804f-2674-3444-b930-8743f3ed1339	3	diagnoseLevel
251c804f-2674-3444-b930-8743f3ed1339	diagnose	action
251c804f-2674-3444-b930-8743f3ed1339	SELECT rno, hno, price FROM room WHERE type = 'family' ORDER BY hno, price DESC;	submission
d721f1da-2074-3a1f-ad04-a6283e9e34e8	3	diagnoseLevel
d721f1da-2074-3a1f-ad04-a6283e9e34e8	diagnose	action
d721f1da-2074-3a1f-ad04-a6283e9e34e8	SELECT r.price, r.type FROM room r, hotel h WHERE r.hno = h.hno AND h.name = 'Grosvenor Hotel';	submission
b5181d9d-adc9-3bde-a4c2-a021a1209da3	3	diagnoseLevel
b5181d9d-adc9-3bde-a4c2-a021a1209da3	diagnose	action
b5181d9d-adc9-3bde-a4c2-a021a1209da3	SELECT b.datefrom, b.dateto FROM booking b, room r WHERE b.hno = r.hno AND b.rno = r.rno AND r.type = 'family';	submission
01d511e5-c68d-36a5-9009-fe4aeb58a313	3	diagnoseLevel
01d511e5-c68d-36a5-9009-fe4aeb58a313	diagnose	action
01d511e5-c68d-36a5-9009-fe4aeb58a313	SELECT DISTINCT g.name, g.address FROM guest g, booking b, hotel h WHERE b.gno = g.gno AND b.hno = h.hno AND h.name = 'Grosvenor Hotel';	submission
e4c85d9f-ad99-3eca-8a31-ac79ed6f2a32	3	diagnoseLevel
e4c85d9f-ad99-3eca-8a31-ac79ed6f2a32	diagnose	action
e4c85d9f-ad99-3eca-8a31-ac79ed6f2a32	SELECT g.name, g.address FROM guest g, booking b, room r WHERE g.gno = b.gno AND b.hno = r.hno AND b.rno = r.rno AND r.type = 'family';	submission
87df45bd-11e3-3086-a6bd-6149301984b1	3	diagnoseLevel
87df45bd-11e3-3086-a6bd-6149301984b1	diagnose	action
87df45bd-11e3-3086-a6bd-6149301984b1	SELECT COUNT(*) AS noOfHotels FROM hotel;	submission
8be67da6-f2c0-33cc-a54f-a80434ce313d	3	diagnoseLevel
8be67da6-f2c0-33cc-a54f-a80434ce313d	diagnose	action
8be67da6-f2c0-33cc-a54f-a80434ce313d	SELECT AVG(price) AS AVGFamily FROM room WHERE type = 'family';	submission
681b9051-7455-3b7d-a6d7-a767063ff177	3	diagnoseLevel
681b9051-7455-3b7d-a6d7-a767063ff177	diagnose	action
58b10e80-8206-3ed7-ac23-2b5e7a9cdfa0	3	diagnoseLevel
681b9051-7455-3b7d-a6d7-a767063ff177	SELECT SUM(price) AS revenue FROM room WHERE type = 'double' AND hno = 'H001';	submission
a04204aa-1f08-31ff-875f-ff3c5db20006	3	diagnoseLevel
a04204aa-1f08-31ff-875f-ff3c5db20006	diagnose	action
a04204aa-1f08-31ff-875f-ff3c5db20006	SELECT COUNT(DISTINCT gno) AS guests FROM booking WHERE datefrom < TO_DATE('01-05-1997','dd-mm-yyyy') AND dateto > TO_DATE('31-03-1997','dd-mm-yyyy');	submission
e101032e-3f01-3d44-832c-aff0bc090a1a	3	diagnoseLevel
e101032e-3f01-3d44-832c-aff0bc090a1a	diagnose	action
e101032e-3f01-3d44-832c-aff0bc090a1a	SELECT SUM(price) AS income FROM room r, booking b, hotel h WHERE r.hno = h.hno AND r.rno = b.rno AND r.hno = b.hno AND h.name = 'Grosvenor Hotel' AND b.datefrom <= TO_DATE('26-03-1997','dd-mm-yyyy') AND b.dateto > TO_DATE('26-03-1997','dd-mm-yyyy');	submission
480a4b86-efd1-3cd6-9406-80f34c408a98	3	diagnoseLevel
480a4b86-efd1-3cd6-9406-80f34c408a98	diagnose	action
480a4b86-efd1-3cd6-9406-80f34c408a98	SELECT r.rno FROM room r, hotel h WHERE r.hno = h.hno AND h.name = 'Grosvenor Hotel' AND r.rno NOT IN (SELECT b.rno FROM booking b WHERE b.rno = r.rno AND b.hno = r.hno AND datefrom <= TO_DATE('26-03-1997','dd-mm-yyyy'));	submission
447051b0-7db4-3708-9c48-417eb426d205	3	diagnoseLevel
447051b0-7db4-3708-9c48-417eb426d205	diagnose	action
447051b0-7db4-3708-9c48-417eb426d205	SELECT SUM(r.price) AS lost FROM room r, hotel h WHERE r.hno = h.hno AND h.name = 'Grosvenor Hotel' AND r.rno NOT IN (SELECT b.rno FROM booking b WHERE b.rno = r.rno AND b.hno = r.hno AND datefrom <= TO_DATE('26-03-1997','dd-mm-yyyy') AND dateto > TO_DATE('26-03-1997','dd-mm-yyyy'));	submission
873403e3-6180-3964-b57a-4926a2a47c0b	3	diagnoseLevel
873403e3-6180-3964-b57a-4926a2a47c0b	diagnose	action
873403e3-6180-3964-b57a-4926a2a47c0b	SELECT r.hno, r.rno, r.type, r.price, g.name\r\nFROM   hotel h JOIN room r ON h.hno = r.hno \r\n               LEFT OUTER JOIN (SELECT * \r\n                                FROM   booking b \r\n                                WHERE  b.datefrom <= TO_DATE('26-03-1997','dd-mm-yyyy') AND \r\n                                       b.dateto > TO_DATE('26-03-1997','dd-mm-yyyy')) b ON (r.rno = b.rno AND r.hno = b.hno)\r\n               LEFT OUTER JOIN guest g ON g.gno = b.gno\r\nWHERE  h.name='Grosvenor Hotel';	submission
0fe0ffd8-9496-3ca4-94d0-1f25067e3a55	3	diagnoseLevel
0fe0ffd8-9496-3ca4-94d0-1f25067e3a55	diagnose	action
0fe0ffd8-9496-3ca4-94d0-1f25067e3a55	SELECT hno, COUNT(*) AS noOfRooms FROM room GROUP BY hno;	submission
33270cdb-de34-36b7-8256-106d87d594b7	3	diagnoseLevel
33270cdb-de34-36b7-8256-106d87d594b7	diagnose	action
33270cdb-de34-36b7-8256-106d87d594b7	SELECT hno, COUNT(*) AS noOfRooms FROM room WHERE hno IN (SELECT hno FROM hotel WHERE UPPER(address) LIKE '%LONDON%') GROUP BY hno;	submission
7e192773-2563-32f5-8aee-58cd7b652a27	3	diagnoseLevel
7e192773-2563-32f5-8aee-58cd7b652a27	diagnose	action
7e192773-2563-32f5-8aee-58cd7b652a27	SELECT h.hno, h.name, COUNT(*) AS occRooms FROM hotel h, booking b WHERE h.hno = b.hno AND b.datefrom <= TO_DATE('26-03-1997','dd-mm-yyyy') AND b.dateto > TO_DATE('26-03-1997','dd-mm-yyyy') GROUP BY h.hno, h.name;	submission
535c86de-3c70-38ef-acf7-fb4189b796e5	3	diagnoseLevel
535c86de-3c70-38ef-acf7-fb4189b796e5	diagnose	action
535c86de-3c70-38ef-acf7-fb4189b796e5	SELECT r1.type, COUNT (*) AS noBooked FROM room r1, booking b1 WHERE r1.hno = b1.hno AND r1.rno = b1.rno GROUP BY r1.type HAVING COUNT(*) >= ALL (SELECT COUNT(*) FROM room r2, booking b2 WHERE r2.hno = b2.hno AND r2.rno = b2.rno GROUP BY r2.type);	submission
88cfed64-f772-3238-a009-cd5a3f32a5b3	3	diagnoseLevel
88cfed64-f772-3238-a009-cd5a3f32a5b3	diagnose	action
88cfed64-f772-3238-a009-cd5a3f32a5b3	SELECT h.name, SUM(r.price) AS lost FROM room r, hotel h WHERE r.hno = h.hno AND (r.rno, r.hno) NOT IN (SELECT rno, hno FROM booking WHERE datefrom <= TO_DATE('26-03-1997','dd-mm-yyyy') AND dateto > TO_DATE('26-03-1997','dd-mm-yyyy')) GROUP BY h.name;	submission
d116f4ee-8961-3a6a-9349-fbed5e049a64	3	diagnoseLevel
d116f4ee-8961-3a6a-9349-fbed5e049a64	diagnose	action
d116f4ee-8961-3a6a-9349-fbed5e049a64	SELECT Name, Address, City FROM Staff WHERE Dnum = 'D001';	submission
4e27141b-57fc-3b3d-84cc-6955b56538de	3	diagnoseLevel
4e27141b-57fc-3b3d-84cc-6955b56538de	diagnose	action
4e27141b-57fc-3b3d-84cc-6955b56538de	SELECT s.Name, d.dname FROM Staff s, Dept d WHERE s.Snum = d.Manager;	submission
76c79c0f-ca88-3a19-ab15-6cb451b7901e	3	diagnoseLevel
76c79c0f-ca88-3a19-ab15-6cb451b7901e	diagnose	action
76c79c0f-ca88-3a19-ab15-6cb451b7901e	SELECT e.Name staff_member, s.Name supervisor FROM Staff s, Staff e WHERE e.Supervisor = s.Snum AND e.City <> s.City;	submission
c51b4295-83c7-3b1e-b64a-3f9d1860def5	3	diagnoseLevel
c51b4295-83c7-3b1e-b64a-3f9d1860def5	diagnose	action
c51b4295-83c7-3b1e-b64a-3f9d1860def5	SELECT Name, Salary FROM Staff WHERE Salary > 65000 AND Snum NOT IN (SELECT DISTINCT Supervisor FROM Staff WHERE Supervisor IS NOT NULL);	submission
543e0e63-e8d4-32d3-b4c6-a0d7ee1989b0	3	diagnoseLevel
543e0e63-e8d4-32d3-b4c6-a0d7ee1989b0	diagnose	action
543e0e63-e8d4-32d3-b4c6-a0d7ee1989b0	SELECT p.Pnum, p.Pname, p.Pcity, p.Dnum FROM Project p WHERE p.Pcity NOT IN(SELECT dl.Dcity FROM DeptLocation dl WHERE dl.Dnum = p.Dnum);	submission
611f41c9-857d-3bcb-bd9e-712dd91d78db	3	diagnoseLevel
611f41c9-857d-3bcb-bd9e-712dd91d78db	diagnose	action
611f41c9-857d-3bcb-bd9e-712dd91d78db	SELECT p.Pname FROM Project p, WorksOn w WHERE p.Pnum = w.Pnum GROUP BY p.Pname HAVING COUNT(*) > 2;	submission
d9243503-c384-3312-89d4-12ed2aca561c	3	diagnoseLevel
d9243503-c384-3312-89d4-12ed2aca561c	diagnose	action
d9243503-c384-3312-89d4-12ed2aca561c	SELECT s1.snum, s1.name, s1.dnum, count(*) AS nsupervised FROM staff s1, staff s2 WHERE s1.snum=s2.supervisor GROUP BY s1.snum, s1.name, s1.dnum;	submission
d816aa5d-3fe7-3f44-95c0-4e02dae8a973	3	diagnoseLevel
d816aa5d-3fe7-3f44-95c0-4e02dae8a973	diagnose	action
d816aa5d-3fe7-3f44-95c0-4e02dae8a973	SELECT   AVG(count(*)) AS avgsupervised\r\nFROM     staff s1, staff s2\r\nWHERE    s1.snum = s2.supervisor \r\nGROUP BY s1.snum;	submission
bfc012f2-804f-3f94-9f6f-818107d72aec	3	diagnoseLevel
bfc012f2-804f-3f94-9f6f-818107d72aec	diagnose	action
58b10e80-8206-3ed7-ac23-2b5e7a9cdfa0	diagnose	action
bfc012f2-804f-3f94-9f6f-818107d72aec	select distinct inhname as name, gebdat\r\nfrom konto\r\nwhere saldo>10000	submission
b86abe04-ebab-3591-88e8-784f3453dba1	3	diagnoseLevel
b86abe04-ebab-3591-88e8-784f3453dba1	diagnose	action
b86abe04-ebab-3591-88e8-784f3453dba1	select i.name, i.gebdat, b.buchngNr, k.kontoNr \r\nfrom inhaber i, konto k, buchung b\r\nwhere i.name=k.inhname and i.gebdat=k.gebdat and b.aufkonto=k.kontonr \r\nand b.datum <= ALL(select datum from buchung b1 where b1.aufkonto=k.kontonr);	submission
29cd8fa5-732f-33d7-b614-ab2c6ba7323b	3	diagnoseLevel
29cd8fa5-732f-33d7-b614-ab2c6ba7323b	diagnose	action
29cd8fa5-732f-33d7-b614-ab2c6ba7323b	select pu.prodno, t.m month, SUM(pu.qty*pu.price) value\r\nfrom   purchase pu, time t\r\nwhere  pu.dayno=t.dayno\r\ngroup by pu.prodno, t.m	submission
99ec3e40-624c-3794-a3cf-8736298cad90	3	diagnoseLevel
99ec3e40-624c-3794-a3cf-8736298cad90	diagnose	action
99ec3e40-624c-3794-a3cf-8736298cad90	select pu.prodno, t.m month, SUM(pu.qty*pu.price) value\r\nfrom   purchase pu, time t\r\nwhere  pu.dayno=t.dayno\r\ngroup by grouping sets ((pu.prodno, t.m), (pu.prodno))	submission
78589ebe-5c7b-3362-811d-32635f4ad708	3	diagnoseLevel
78589ebe-5c7b-3362-811d-32635f4ad708	diagnose	action
78589ebe-5c7b-3362-811d-32635f4ad708	select pu.prodno, t.y year, t.m month, SUM(pu.qty*pu.price) value\r\nfrom   purchase pu, time t\r\nwhere  pu.dayno=t.dayno\r\ngroup by rollup (pu.prodno, t.y, t.m)\r\norder by pu.prodno, t.y, t.m	submission
a6844930-efba-3884-bd34-55180f5750d3	3	diagnoseLevel
a6844930-efba-3884-bd34-55180f5750d3	diagnose	action
a6844930-efba-3884-bd34-55180f5750d3	select pu.prodno, t.m month, SUM(pu.qty*pu.price) value,\r\nSUM(SUM(pu.qty*pu.price)) OVER \r\n(PARTITION BY pu.prodno ORDER BY t.m ROWS UNBOUNDED PRECEDING) cumvalue\r\nfrom   purchase pu, time t\r\nwhere  pu.dayno=t.dayno\r\ngroup by pu.prodno, t.m;\r\n	submission
16d481d9-2771-3298-a893-a7a4e517c7d6	3	diagnoseLevel
16d481d9-2771-3298-a893-a7a4e517c7d6	diagnose	action
16d481d9-2771-3298-a893-a7a4e517c7d6	select l.state, pr.type, sum(s.price*s.qty) turnover,\r\nrank() over (partition by l.state order by sum(s.price*s.qty) desc) R\r\nfrom   location l, product pr, sales s\r\nwhere  l.locno=s.locno and pr.prodno=s.prodno\r\ngroup by l.state, pr.type	submission
5d9a43ea-7b79-37af-8fc1-1db1cf369348	3	diagnoseLevel
5d9a43ea-7b79-37af-8fc1-1db1cf369348	diagnose	action
5d9a43ea-7b79-37af-8fc1-1db1cf369348	select s.prodno, l.state, SUM(s.qty*s.price) TURNOVER, \r\n(SUM(s.qty*s.price)*100/SUM(SUM(s.qty*s.price)) OVER \r\n(PARTITION BY s.prodno ORDER BY l.state ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING)) TShARE\r\nfrom   sales s, location l\r\nwhere  s.locno=l.locno\r\ngroup by s.prodno, l.state\r\norder by s.prodno, l.state	submission
e0b339c9-7714-3af2-8ae8-4e854102a411	3	diagnoseLevel
e0b339c9-7714-3af2-8ae8-4e854102a411	diagnose	action
e0b339c9-7714-3af2-8ae8-4e854102a411	SELECT u.prodno, s.Y,  SUM(u.qty) PQTY, SUM(s.qty) SQTY, SUM(s.qty)-SUM(u.qty) DQTY FROM  (SELECT t1.y, s1.prodno, SUM(s1.qty) qty FROM sales s1, TIME t1 WHERE s1.dayno=t1.dayno GROUP BY t1.y, s1.prodno) s, (SELECT t2.y, u1.prodno, SUM(u1.qty) qty FROM purchase u1, TIME t2 WHERE u1.dayno=t2.dayno GROUP BY t2.y, u1.prodno) u WHERE s.prodno=u.prodno AND u.y=s.y GROUP BY (u.prodno, s.y) ORDER BY u.prodno	submission
910a455a-50ab-3589-9404-284e88738b9d	3	diagnoseLevel
910a455a-50ab-3589-9404-284e88738b9d	diagnose	action
910a455a-50ab-3589-9404-284e88738b9d	select * from segment where code = 'H10';	submission
613998c3-32db-3d73-9570-49a1af2a8e8a	3	diagnoseLevel
613998c3-32db-3d73-9570-49a1af2a8e8a	diagnose	action
613998c3-32db-3d73-9570-49a1af2a8e8a	select distinct longitude, latitude from node;	submission
570f24d8-a300-3360-8766-6cbbec0f09e6	3	diagnoseLevel
570f24d8-a300-3360-8766-6cbbec0f09e6	diagnose	action
570f24d8-a300-3360-8766-6cbbec0f09e6	select max(toKM - fromKM) MaxLen from segment;	submission
80346873-1801-3fb7-9e76-5bc0cf4fbdd7	3	diagnoseLevel
80346873-1801-3fb7-9e76-5bc0cf4fbdd7	diagnose	action
80346873-1801-3fb7-9e76-5bc0cf4fbdd7	select code, max(toKM - fromKM) MaxLen from segment group by code;	submission
917219a1-6363-39e6-97ed-07d36635a3da	3	diagnoseLevel
917219a1-6363-39e6-97ed-07d36635a3da	diagnose	action
917219a1-6363-39e6-97ed-07d36635a3da	SELECT code, MAX(toKM - fromKM) MaxLen\r\nFROM segment\r\nGROUP BY code\r\nHAVING MAX(toKM - fromKM) > 50;	submission
1e53f2ee-bade-36e6-9f19-3ab6708f6415	3	diagnoseLevel
1e53f2ee-bade-36e6-9f19-3ab6708f6415	diagnose	action
1e53f2ee-bade-36e6-9f19-3ab6708f6415	SELECT name, longitude, latitude\r\nFROM highway, node\r\nWHERE startNodeID = nodeID;	submission
d4d243a2-fdcd-343a-ad94-326141719416	3	diagnoseLevel
d4d243a2-fdcd-343a-ad94-326141719416	diagnose	action
d4d243a2-fdcd-343a-ad94-326141719416	SELECT DISTINCT c.name, n1.longitude, n1.latitude\r\nFROM node n1, node n2, city c, exit e\r\nWHERE n1.type != n2.type AND\r\n      n1.longitude = n2.longitude AND\r\n      n1.latitude = n2.latitude AND\r\n      n1.nodeID = e.nodeID AND\r\n      e.ZIP = c.ZIP;	submission
d6370d52-c333-35d5-abad-b4a74db63f86	3	diagnoseLevel
d6370d52-c333-35d5-abad-b4a74db63f86	diagnose	action
d6370d52-c333-35d5-abad-b4a74db63f86	SELECT s.code, s.segId, (s.toKM - s.fromKM) AS "Length" \r\nFROM   segment s WHERE  (s.toKM - s.fromKM) > \r\n       (SELECT (s1.toKM - s1.fromKM)\r\n        FROM   segment s1 \r\n        WHERE  s1.segId = 'S01' AND \r\n               s1.code = 'H10');	submission
7185d292-3612-3f53-b8fb-2efa311af6e6	3	diagnoseLevel
7185d292-3612-3f53-b8fb-2efa311af6e6	diagnose	action
7185d292-3612-3f53-b8fb-2efa311af6e6	SELECT s1.code, s1.segID, (s1.toKM - s1.fromKM) AS LENGTH\r\nFROM segment s1\r\nWHERE (s1.toKM - s1.fromKM) >\r\n      (SELECT (s2.toKM - s2.fromKM)\r\n       FROM segment s2\r\n       WHERE s2.segID = 'S01' AND s2.code = 'H10')	submission
75b00034-b57a-3e6f-b396-62633b110be9	3	diagnoseLevel
75b00034-b57a-3e6f-b396-62633b110be9	diagnose	action
75b00034-b57a-3e6f-b396-62633b110be9	select s1.code, s1.segID, s1.toKM - s1.fromKM Length from segment s1 where s1.toKM - s1.fromKM = (select max (s2.toKM - s2.fromKM) from segment s2 where s1.code = s2.code)	submission
8b2a4bbd-6831-3735-afa2-9be409e6370d	3	diagnoseLevel
8b2a4bbd-6831-3735-afa2-9be409e6370d	diagnose	action
3ae55a2a-fcfb-3863-8821-4c897d3b7d01	3	diagnoseLevel
8b2a4bbd-6831-3735-afa2-9be409e6370d	(SELECT n1.nodeID, n1.longitude, n1.latitude FROM node n1, exit e, highwayExit he WHERE he.code = 'H10' AND       he.nodeID = e.nodeID AND       e.nodeID = n1.nodeID) UNION (SELECT n2.nodeID, n2.longitude, n2.latitude FROM node n2, intersection i, HighwayIntersection hi WHERE hi.code = 'H10' AND       hi.nodeID = i.nodeID AND       i.nodeID = n2.nodeID);\r\n	submission
d612ba6d-d63c-3865-b150-9976afcc79bb	3	diagnoseLevel
d612ba6d-d63c-3865-b150-9976afcc79bb	diagnose	action
d612ba6d-d63c-3865-b150-9976afcc79bb	\t\r\nDROP TABLE ETUTOR_JESS.JessExercise;\r\n\r\nCREATE ETUTOR_JESS.TABLE JessExercise (\r\n\tid\tNUMBER,\r\n\tprivate_facts VARCHAR(4000),\r\n\tpublic_facts VARCHAR(4000),\r\n\treference_solution VARCHAR(4000),\r\n\ttransitivity NUMBER,\r\n\trecursion NUMBER,\r\n\tnegation NUMBER,\r\n\tsearchtree NUMBER,\r\n\texampletype NUMBER,\r\n\tmaximumpoints NUMBER,\r\n\tPRIMARY KEY(id)\r\n\t);\r\n	submission
045af682-8c08-3aa0-972d-89e0db6cd8e1	3	diagnoseLevel
045af682-8c08-3aa0-972d-89e0db6cd8e1	diagnose	action
045af682-8c08-3aa0-972d-89e0db6cd8e1	select k.kontonr, k.inhname, max(betrag), min(betrag)\r\nfrom konto k, buchung b\r\nwhere k.kontonr = b.vonkonto\r\ngroup by k.kontonr, k.inhname;	submission
c1f571f0-c6b8-30cc-adba-66b1f2af30e5	3	diagnoseLevel
c1f571f0-c6b8-30cc-adba-66b1f2af30e5	diagnose	action
c1f571f0-c6b8-30cc-adba-66b1f2af30e5	select buchngnr, betrag\r\nfrom buchung\r\nwhere betrag >\r\n  (select avg(betrag) * 1.5 from Buchung);	submission
464b2677-c9ac-32bf-be33-ba6a60caf030	3	diagnoseLevel
464b2677-c9ac-32bf-be33-ba6a60caf030	diagnose	action
464b2677-c9ac-32bf-be33-ba6a60caf030	select buchngnr, betrag\r\nfrom buchung\r\nwhere vonkonto in\r\n  (select kontonr\r\n   from konto\r\n   where ADD_MONTHS(sysdate, -360)<gebdat)\r\nor aufkonto in \r\n  (select kontonr\r\n   from konto\r\n   where ADD_MONTHS(sysdate, -360)<gebdat)\r\norder by betrag desc;\r\n	submission
97522ed5-d479-3307-becd-48d9b66aef84	3	diagnoseLevel
97522ed5-d479-3307-becd-48d9b66aef84	diagnose	action
97522ed5-d479-3307-becd-48d9b66aef84	select betrag from buchung\r\nwhere vonkonto in\r\n  (select k.kontonr\r\n   from konto k, inhaber i\r\n   where k.inhname=i.name \r\n   and k.gebdat=i.gebdat\r\n   and adresse like '%z');	submission
590a9f43-0b85-3917-8395-bfdb3011a9ed	3	diagnoseLevel
590a9f43-0b85-3917-8395-bfdb3011a9ed	diagnose	action
590a9f43-0b85-3917-8395-bfdb3011a9ed	select inhname, saldo\r\nfrom konto\r\nwhere saldo-80000 >\r\n  (select max(betrag)\r\n   from buchung\r\n   where datum between '01-OCT-95' and '31-OCT-95')\r\norder by inhname, saldo desc;	submission
d5f43d1e-790c-3a3a-bbb8-a6bb93c05900	3	diagnoseLevel
d5f43d1e-790c-3a3a-bbb8-a6bb93c05900	diagnose	action
d5f43d1e-790c-3a3a-bbb8-a6bb93c05900	select kontonr, saldo\r\nfrom konto k1\r\nwhere saldo <\r\n  (select sum(betrag)\r\n   from buchung b1\r\n   where k1.kontonr=b1.aufkonto);	submission
d0214ec9-1cec-396c-8c41-58e205e0c8a7	3	diagnoseLevel
d0214ec9-1cec-396c-8c41-58e205e0c8a7	diagnose	action
d0214ec9-1cec-396c-8c41-58e205e0c8a7	select i.name, i.gebdat, i.adresse, count(*)anzahl\r\nfrom inhaber i, konto k\r\nwhere i.name=k.inhname and i.gebdat=k.gebdat\r\ngroup by i.name, i.gebdat, i.adresse\r\nhaving count(*) >\r\n  (select avg(count(*))\r\n   from konto\r\n   group by inhname, gebdat);	submission
65d29691-0b9b-3f9b-99cf-67298bbf7f13	3	diagnoseLevel
65d29691-0b9b-3f9b-99cf-67298bbf7f13	diagnose	action
65d29691-0b9b-3f9b-99cf-67298bbf7f13	select count(*) entlehnungen\r\nfrom entlehng e\r\nwhere (e.bis = '01-JAN-2999');	submission
6f83f479-7ea1-3a58-a89f-33169dc5abe1	3	diagnoseLevel
6f83f479-7ea1-3a58-a89f-33169dc5abe1	diagnose	action
6f83f479-7ea1-3a58-a89f-33169dc5abe1	Select s.name, s.matrnr, s.kennr\r\nfrom student s, zeugnis z, professor p\r\nwhere p.pname = 'Huber'\r\nand s.matrnr = z.matrnr\r\nand z.profnr = p.profnr\r\nand z.note <>5\r\norder by s.name, s.matrnr;	submission
b7c076ac-cc41-3e98-b333-5193d01f5aa4	3	diagnoseLevel
b7c076ac-cc41-3e98-b333-5193d01f5aa4	diagnose	action
b7c076ac-cc41-3e98-b333-5193d01f5aa4	Select z1.lvanr, s1.matrnr, s1.name, s2.matrnr, s2.name\r\nfrom student s1, student s2, zeugnis z1, zeugnis z2\r\nwhere s1.matrnr = z1.matrnr\r\nand z1.note <> 5\r\nand s2.matrnr = z2.matrnr\r\nand z2.note <> 5\r\nand z1.lvanr = z2.lvanr\r\nand s1.matrnr > s2.matrnr\r\norder by z1.lvanr, s1.name, s2.name;	submission
76f21ab3-eac9-3aff-8834-974ff0e722cc	3	diagnoseLevel
76f21ab3-eac9-3aff-8834-974ff0e722cc	diagnose	action
76f21ab3-eac9-3aff-8834-974ff0e722cc	select s.name, s.matrnr, s.kennr\r\nfrom student s\r\nwhere not exists\r\n(select * \r\n from \r\n zeugnis z\r\n where z.matrnr = s.matrnr\r\n and z.note < 5)\r\norder by s.kennr, s.name, s.matrnr;	submission
cfb2da00-d3ad-3fc4-b1b9-a7ccfaa963c6	3	diagnoseLevel
cfb2da00-d3ad-3fc4-b1b9-a7ccfaa963c6	diagnose	action
cfb2da00-d3ad-3fc4-b1b9-a7ccfaa963c6	Select kennr, min(note), avg(note)\r\nfrom student, zeugnis\r\nwhere student.matrnr = zeugnis.matrnr\r\ngroup by kennr\r\nhaving (avg(note) <= (min(note) + 1))\r\norder by kennr;\r\n	submission
1b6d2dc5-960b-3913-9df3-9a61cd3e2469	3	diagnoseLevel
1b6d2dc5-960b-3913-9df3-9a61cd3e2469	diagnose	action
1b6d2dc5-960b-3913-9df3-9a61cd3e2469	select pname, count(*), avg(note)\r\nfrom professor, zeugnis\r\nwhere professor.profnr = zeugnis.profnr\r\ngroup by pname\r\norder by count(*), pname;	submission
fc14a31d-d55b-3e30-8ddd-afa2855abde9	3	diagnoseLevel
fc14a31d-d55b-3e30-8ddd-afa2855abde9	diagnose	action
fc14a31d-d55b-3e30-8ddd-afa2855abde9	select p.pname, s.kennr\r\nfrom professor p, student s, zeugnis z\r\nwhere p.profnr = z.profnr\r\nand z.matrnr = s.matrnr\r\ngroup by p.pname, s.kennr \r\norder by p.pname, s.kennr;	submission
72e900a3-f63c-3119-9278-ecc2b25d00d6	3	diagnoseLevel
72e900a3-f63c-3119-9278-ecc2b25d00d6	diagnose	action
72e900a3-f63c-3119-9278-ecc2b25d00d6	Select s.matrnr, s.name, s.kennr, l.bez, z.note\r\nfrom student s, zeugnis z, diplomprfg d, voraussetzungen v, lva l\r\nwhere s.matrnr = z.matrnr\r\nand z.lvanr = v.lvanr\r\nand v.pnr = d.pnr\r\nand l.lvanr = z.lvanr\r\nand z.note < 5\r\nand d.bezeichnung = 'Angewandte INF'\r\norder by s.matrnr, s.name, s.kennr;	submission
594649ae-dde9-3396-95c6-42ba70de1176	3	diagnoseLevel
594649ae-dde9-3396-95c6-42ba70de1176	diagnose	action
9fb736c1-6d67-33bc-b8db-2fe03170f7d9	select w.bezirk, avg (v.preis/w.gross)\r\nfrom Wohnung w, vermietet v\r\nwhere w.wohnnr = v.wohnnr\r\ngroup by w.bezirk\r\nhaving avg(v.preis/w.gross) >= ALL\r\n  (select avg(v1.preis/w1.gross)\r\n   from wohnung w1, vermietet v1\r\n   where w1.wohnnr = v1.wohnnr\r\n   group by w1.bezirk);\r\n	submission
594649ae-dde9-3396-95c6-42ba70de1176	select s.matrnr, s.name, s.kennr\r\nfrom student s\r\nwhere not exists\r\n(Select * from diplomprfg d, voraussetzungen v\r\n where d.pnr = v.pnr\r\n and d.bezeichnung = 'Angewandte INF'\r\n and not exists (Select * from zeugnis z\r\n                 where z.matrnr = s.matrnr\r\n                 and z.lvanr = v.lvanr\r\n                 and z.note <> 5))\r\norder by s.matrnr, s.name, s.kennr;	submission
4f3f57d6-d5e7-3f7a-a0c6-835589e969da	3	diagnoseLevel
4f3f57d6-d5e7-3f7a-a0c6-835589e969da	diagnose	action
4f3f57d6-d5e7-3f7a-a0c6-835589e969da	select s.matrnr, s.name, avg(z.note), count(*)\r\nfrom student s, zeugnis z\r\nwhere s.matrnr = z.matrnr\r\ngroup by s.matrnr, s.name\r\nhaving count(*) >= All \r\n(Select count(*) from zeugnis z2\r\n group by z2.matrnr);	submission
12ef6652-5ecb-37cf-9994-c45810d27ae2	3	diagnoseLevel
12ef6652-5ecb-37cf-9994-c45810d27ae2	diagnose	action
12ef6652-5ecb-37cf-9994-c45810d27ae2	select distinct s.mano, s.name, t.tno\r\nfrom studenten s, reserviert r, terminal t\r\nwhere s.mano = r.mano\r\nand r.tno = t.tno\r\nand not (t.rechner = s.rechner)\r\norder by s.mano;	submission
6f4c6d6c-c02a-3d77-bdbd-730cae7f51ac	3	diagnoseLevel
6f4c6d6c-c02a-3d77-bdbd-730cae7f51ac	diagnose	action
6f4c6d6c-c02a-3d77-bdbd-730cae7f51ac	select s1.mano, s1.name, r1.tno, r1.tag, r1.stunde, r2.mano as mano2\r\nfrom studenten s1, reserviert r1, reserviert r2\r\nwhere s1.mano = r1.mano \r\nand r1.tno = r2.tno\r\nand ((r1.tag=r2.tag and r1.stunde+1=r2.stunde) \r\n OR (r1.tag+1=r2.tag and r1.stunde=23 and r2.stunde=0))\r\norder by s1.mano, r1.tno, r1.tag, r1.stunde;\r\n	submission
0eddc426-527c-3ce4-9294-24d996cf98b7	3	diagnoseLevel
0eddc426-527c-3ce4-9294-24d996cf98b7	diagnose	action
0eddc426-527c-3ce4-9294-24d996cf98b7	select s.mano\r\nfrom studenten s\r\nwhere not exists\r\n(select * from reserviert r\r\nwhere s.mano = r.mano)\r\norder by s.mano;	submission
4f007285-7a17-3e55-a6ce-37e9c889e499	3	diagnoseLevel
4f007285-7a17-3e55-a6ce-37e9c889e499	diagnose	action
4f007285-7a17-3e55-a6ce-37e9c889e499	select t.rechner, r.tag, r.stunde, count(r.tno) terminals\r\nfrom terminal t, reserviert r\r\nwhere t.tno = r.tno\r\ngroup by t.rechner, r.tag, r.stunde\r\norder by r.tag, r.stunde;	submission
75c1c369-d9f8-30e2-b624-59fc6470f4e1	3	diagnoseLevel
75c1c369-d9f8-30e2-b624-59fc6470f4e1	diagnose	action
75c1c369-d9f8-30e2-b624-59fc6470f4e1	Select distinct s.mano, s.name\r\nfrom studenten s, reserviert r\r\nwhere s.mano = r.mano\r\nand r.stunde <=11\r\norder by s.mano;	submission
cae8e632-c4ed-34f0-9a3a-06b8ad3ab8b0	3	diagnoseLevel
cae8e632-c4ed-34f0-9a3a-06b8ad3ab8b0	diagnose	action
cae8e632-c4ed-34f0-9a3a-06b8ad3ab8b0	select distinct s.mano, s.name\r\nfrom studenten s, reserviert r\r\nwhere s.mano = r.mano\r\ngroup by s.mano, s.name\r\nhaving count(distinct r.tag) = 1\r\norder by s.mano;	submission
91028944-8c85-3ff8-9fcb-ded595d01386	3	diagnoseLevel
91028944-8c85-3ff8-9fcb-ded595d01386	diagnose	action
91028944-8c85-3ff8-9fcb-ded595d01386	select s.mano, s.name, count(*) stunden\r\nfrom studenten s, reserviert r\r\nwhere s.mano = r.mano\r\ngroup by s.mano, s.name\r\nhaving count(*) >= ALL\r\n (Select count(*) from reserviert r1\r\n  group by r1.mano);\r\n	submission
b5a7571a-fab0-3c13-b4a9-37329783f886	3	diagnoseLevel
b5a7571a-fab0-3c13-b4a9-37329783f886	diagnose	action
b5a7571a-fab0-3c13-b4a9-37329783f886	select s.mano, s.name, r.tag, r.stunde, r.tno\r\nfrom studenten s, reserviert r\r\nwhere s.mano = r.mano \r\nand NOT Exists\r\n (Select * from terminal t, wartung w\r\n  where r.tno = t.tno\r\n  and t.rechner = w.rechner\r\n  and r.tag = w.tag\r\n  and r.stunde between w.vonstunde and w.bisstunde)\r\norder by s.mano, r.tag, r.stunde;	submission
31b99df9-9e60-33dc-b0ee-862290b25980	3	diagnoseLevel
31b99df9-9e60-33dc-b0ee-862290b25980	diagnose	action
31b99df9-9e60-33dc-b0ee-862290b25980	Select s.mano, s.name\r\nfrom studenten s\r\nwhere exists\r\n (select * from reserviert r\r\n  where s.mano = r.mano)\r\nAND NOT EXISTS\r\n (select * from reserviert r\r\n  Where s.mano = r.mano \r\n  AND NOT EXISTS\r\n   (Select * from Terminal t, wartung w\r\n    where r.tno = t.tno \r\n    and t.rechner = w.rechner\r\n    and r.tag = w.tag\r\n    and r.stunde between w.vonstunde and w.bisstunde));\r\n	submission
74334967-a3e4-3201-90ec-84c4c717ca6d	3	diagnoseLevel
74334967-a3e4-3201-90ec-84c4c717ca6d	diagnose	action
74334967-a3e4-3201-90ec-84c4c717ca6d	select p.name, p.beruf, w.gross, p.stand\r\nfrom person p, vermietet v, wohnung w\r\nwhere p.persnr = v.mieternr\r\nand v.wohnnr = w.wohnnr\r\nand w.gross > 80\r\nand p.stand = 'ledig'\r\norder by p.name, w.gross;\r\n	submission
4c2584f1-1c2e-3190-93ec-9e353da6b2f3	3	diagnoseLevel
4c2584f1-1c2e-3190-93ec-9e353da6b2f3	diagnose	action
4c2584f1-1c2e-3190-93ec-9e353da6b2f3	select p1.name, p1.stand, v1.preis, v2.preis\r\nfrom person p1, vermietet v1, person p2, vermietet v2\r\nwhere p1.persnr = v1.mieternr\r\nand p2.persnr = v2.mieternr\r\nand v1.vermieternr = v2.mieternr\r\nand v1.preis < v2.preis\r\nand p1.stand = p2.stand\r\norder by p1.name, p1.stand;	submission
3a9f7f98-f0a7-3299-b846-a4b8be1824d4	3	diagnoseLevel
3a9f7f98-f0a7-3299-b846-a4b8be1824d4	diagnose	action
3a9f7f98-f0a7-3299-b846-a4b8be1824d4	select p.name, p.stand, p.beruf\r\nfrom person p\r\nwhere p.persnr not in\r\n (Select v1.vermieternr from vermietet v1\r\n  union\r\n  select v2.mieternr from vermietet v2)\r\norder by p.name, p.stand, p.beruf;	submission
e9e8536f-7ccb-357c-b064-8621f1edd324	3	diagnoseLevel
e9e8536f-7ccb-357c-b064-8621f1edd324	diagnose	action
e9e8536f-7ccb-357c-b064-8621f1edd324	select w1.bezirk, min (v1.preis/w1.gross), max (v2.preis/w2.gross)\r\nfrom wohnung w1, wohnung w2, vermietet v1, vermietet v2\r\nwhere w1.wohnnr = v1.wohnnr \r\nand w2.wohnnr = v2.wohnnr\r\nand w1.bezirk = w2.bezirk\r\nand v1.preis/w1.gross * 1.5 < v2.preis/w2.gross\r\ngroup by w1.bezirk\r\norder by w1.bezirk;	submission
e2cbb011-c4c2-3e08-bb38-5045bcaeb8c6	3	diagnoseLevel
e2cbb011-c4c2-3e08-bb38-5045bcaeb8c6	diagnose	action
e2cbb011-c4c2-3e08-bb38-5045bcaeb8c6	select w.bezirk, count (*)\r\nfrom wohnung w, vermietet v\r\nwhere w.wohnnr = v.wohnnr\r\ngroup by w.bezirk\r\norder by count (*);	submission
347cdfe0-ec6f-3abc-bc6c-deaccab72fc7	3	diagnoseLevel
347cdfe0-ec6f-3abc-bc6c-deaccab72fc7	diagnose	action
347cdfe0-ec6f-3abc-bc6c-deaccab72fc7	select p.name, avg(v.preis) Dpreis, avg(w.gross) DGroesse\r\nfrom person p, vermietet v, wohnung w\r\nwhere p.persnr = v.vermieternr\r\nand v.wohnnr = w.wohnnr\r\ngroup by p.name \r\norder by p.name;	submission
9fb736c1-6d67-33bc-b8db-2fe03170f7d9	3	diagnoseLevel
9fb736c1-6d67-33bc-b8db-2fe03170f7d9	diagnose	action
58b10e80-8206-3ed7-ac23-2b5e7a9cdfa0	select p.name, w.bezirk, sum (v.preis)\r\nfrom person p, wohnung w, vermietet v\r\nwhere p.persnr = v.vermieternr\r\nand w.wohnnr = v.wohnnr\r\ngroup by p.name, w.bezirk\r\nhaving sum (v.preis) > ANY\r\n (Select sum (v1.preis) from vermietet v1, wohnung w1\r\n  where v1.wohnnr = w1.wohnnr\r\n  group by v1.vermieternr);\r\n	submission
e717ad1f-4d3f-31a3-8fa1-bb6d74e9ed4a	3	diagnoseLevel
e717ad1f-4d3f-31a3-8fa1-bb6d74e9ed4a	diagnose	action
e717ad1f-4d3f-31a3-8fa1-bb6d74e9ed4a	select p1.persnr, p1.name, sum (v1.preis)\r\nfrom person p1, vermietet v1\r\nwhere p1.stand = 'verheiratet'\r\nand p1.persnr = v1.vermieternr\r\nand p1.beruf IN\r\n (select p2.beruf\r\n  from person p2\r\n  where p2.persnr <> p1.persnr)\r\ngroup by p1.persnr, p1.name;\r\n\r\n	submission
140de0c6-7fb0-363f-a993-cf1e576f8380	3	diagnoseLevel
140de0c6-7fb0-363f-a993-cf1e576f8380	diagnose	action
140de0c6-7fb0-363f-a993-cf1e576f8380	select w2.bezirk, count(*)\r\nfrom wohnung w2, vermietet v2\r\nwhere w2.wohnnr = v2.wohnnr\r\ngroup by w2.bezirk\r\nhaving count (*) > \r\n (select count(*) from person p, vermietet v, wohnung w\r\n  where p.persnr = v.mieternr\r\n  and v.wohnnr = w.wohnnr\r\n  and p.stand = 'ledig'\r\n  and w.bezirk = \r\n   (Select w1.bezirk\r\n    from wohnung w1\r\n    group by w1.bezirk\r\n    having count(*) <= ALL\r\n     (Select count(*) from wohnung w3\r\n      group by w3.bezirk)));\r\n	submission
2304457a-55e3-3c44-b669-ca3338837c2c	3	diagnoseLevel
2304457a-55e3-3c44-b669-ca3338837c2c	diagnose	action
2304457a-55e3-3c44-b669-ca3338837c2c	select avg(tokm - fromkm) from segment	submission
cb1b8192-6e48-340a-b4ca-0a9b2f884c78	3	diagnoseLevel
cb1b8192-6e48-340a-b4ca-0a9b2f884c78	diagnose	action
cb1b8192-6e48-340a-b4ca-0a9b2f884c78	select k.kontoNr, sum(b.betrag) as Gesamtsumme from konto k , buchung b where k.kontoNr=b.aufKonto group by kontoNr	submission
98edded9-93bd-31ab-849d-73bbdff2214c	3	diagnoseLevel
98edded9-93bd-31ab-849d-73bbdff2214c	diagnose	action
98edded9-93bd-31ab-849d-73bbdff2214c	select i.name, i.gebdat, i.adresse, count(*) as Anzahl, sum(b.betrag) as Betragssumme from buchung b, konto k, inhaber i where i.name=k.inhname and i.gebdat=k.gebdat and k.kontoNr=b.aufKonto group by i.name, i.gebdat, i.adresse	submission
112a22cc-f152-3a3a-a9a3-c03fcfc57ac4	3	diagnoseLevel
112a22cc-f152-3a3a-a9a3-c03fcfc57ac4	diagnose	action
112a22cc-f152-3a3a-a9a3-c03fcfc57ac4	select k.kontoNr from konto k\r\nwhere k.kontoNr not in (select distinct b.aufKonto from buchung b)	submission
7662cfdc-6fb2-35cd-ad14-4564086c49df	3	diagnoseLevel
7662cfdc-6fb2-35cd-ad14-4564086c49df	diagnose	action
7662cfdc-6fb2-35cd-ad14-4564086c49df	SELECT segid, (toKM - fromKM) AS Length FROM segment WHERE   (toKM - fromKM) > (SELECT avg(toKM - fromKM) FROM segment)	submission
8173284c-697d-317c-ad5e-dd7f8b64534e	3	diagnoseLevel
8173284c-697d-317c-ad5e-dd7f8b64534e	diagnose	action
8173284c-697d-317c-ad5e-dd7f8b64534e	SELECT code, AVG(toKM - fromKM) AS AVGSegmentLength FROM segment GROUP BY code	submission
a351e215-1946-33b1-80c2-60079ae326f3	3	diagnoseLevel
a351e215-1946-33b1-80c2-60079ae326f3	diagnose	action
a351e215-1946-33b1-80c2-60079ae326f3	SELECT s1.code, s1.segID, s2.code, s2.segID FROM segment s1, segment s2 WHERE (s1.segID <> s2.segID OR s1.code <> s2.code) AND (s1.toKM - s1.fromKM) = (s2.toKM - s2.fromKM)	submission
80d583c6-ecd8-392e-aa53-6eebf027c5e4	3	diagnoseLevel
80d583c6-ecd8-392e-aa53-6eebf027c5e4	diagnose	action
80d583c6-ecd8-392e-aa53-6eebf027c5e4	SELECT segID, s1.toKM - s1.fromKM AS SegmentLength, s2.HighwayLength FROM segment s1, ( SELECT code, SUM(toKM-fromKM) AS HighwayLength FROM segment GROUP BY code) s2\r\n\tWHERE   s2.code = s1.code;	submission
326509c8-c3b9-3d39-a6cd-3a229958c8ca	3	diagnoseLevel
326509c8-c3b9-3d39-a6cd-3a229958c8ca	diagnose	action
326509c8-c3b9-3d39-a6cd-3a229958c8ca	SELECT DISTINCT longitude, latitude FROM node ORDER BY  longitude ASC, latitude DESC;\r\n	submission
c3494654-74db-3e92-bb6e-2f529bfefa1d	3	diagnoseLevel
c3494654-74db-3e92-bb6e-2f529bfefa1d	diagnose	action
c3494654-74db-3e92-bb6e-2f529bfefa1d	(SELECT\tn1.nodeID, n1.longitude, n1.latitude FROM node n1, exit e, highwayExit he WHERE\the.code = 'H10' AND he.nodeID = e.nodeID AND e.nodeID = n1.nodeID) UNION (SELECT n2.nodeID, n2.longitude, n2.latitude FROM node n2, intersection i, HighwayIntersection hi WHERE hi.code = 'H10' AND hi.nodeID = i.nodeID AND i.nodeID = n2.nodeID)	submission
09608be6-6659-3824-8e9e-e046954c32d4	3	diagnoseLevel
09608be6-6659-3824-8e9e-e046954c32d4	diagnose	action
09608be6-6659-3824-8e9e-e046954c32d4	select count(*) ENTLEHNUNGEN from entlehng e where (e.bis = '01-01-2999');	submission
90f0e802-3c13-39e0-b5b7-cd89f54cd605	3	diagnoseLevel
90f0e802-3c13-39e0-b5b7-cd89f54cd605	diagnose	action
90f0e802-3c13-39e0-b5b7-cd89f54cd605	select B.NAME, B.Adresse, COUNT(*) ANZAHL\r\nfrom BENUTZER B, ENTLEHNG E\r\nwhere (B.BENNR = E.BENUTZER)\r\ngroup by B.NAME, B.ADRESSE\r\norder by 3 desc;	submission
a96235db-1603-33e4-839f-9e9bbefcee9b	3	diagnoseLevel
a96235db-1603-33e4-839f-9e9bbefcee9b	diagnose	action
a96235db-1603-33e4-839f-9e9bbefcee9b	select b.bennr, b.name, b.gebdat, b.adresse, count(*) ANZAHL\r\nfrom benutzer b, entlehng e\r\nwhere (b.bennr = e.benutzer) and (e.bis = '01-01-2999')\r\ngroup by b.bennr, b.name, b.gebdat, b.adresse\r\nhaving count(*) > 2\r\norder by 2;	submission
d4f5ebc3-4fbd-3229-9b39-833e634e5949	3	diagnoseLevel
d4f5ebc3-4fbd-3229-9b39-833e634e5949	diagnose	action
d4f5ebc3-4fbd-3229-9b39-833e634e5949	select e.entlngnr, e.von, be.name, bu.titel\r\nfrom entlehng e, benutzer be, buch bu\r\nwhere (e.buch = bu.buchnr) and (e.benutzer = be.bennr) and\r\n      (e.bis = '01-01-2999') and ((e.von + 100) < SYSDATE)\r\norder by be.name;	submission
1cda4c48-e509-354f-871e-d451395eb79d	3	diagnoseLevel
1cda4c48-e509-354f-871e-d451395eb79d	diagnose	action
1cda4c48-e509-354f-871e-d451395eb79d	select b.titel, count(*) ANZAHL,\r\n  ROUND(AVG(e.bis - e.von)) DURCHSCHNITT\r\nfrom buch b, entlehng e\r\nwhere (b.buchnr = e.buch) and (e.bis <> '01-01-2999')\r\ngroup by b.buchnr, b.titel\r\norder by 2 desc;	submission
fc915d0a-66e0-3bbe-aeaa-6aa5ac434eae	3	diagnoseLevel
fc915d0a-66e0-3bbe-aeaa-6aa5ac434eae	diagnose	action
fc915d0a-66e0-3bbe-aeaa-6aa5ac434eae	select b.buchnr, b.titel\r\nfrom buch b\r\nwhere b.buchnr not in\r\n  (select distinct e.buch\r\n   from entlehng e\r\n   where e.von <= '31-12-1994' and e.bis >= '01-01-1994');	submission
3ae55a2a-fcfb-3863-8821-4c897d3b7d01	diagnose	action
3ae55a2a-fcfb-3863-8821-4c897d3b7d01	select f.filNr, f.inhName, p.ean, p.bezeichnung, (p.ekPreis * s.bestand) "Wert"\r\nfrom filiale f, produkt p, sortiment s\r\nwhere p.ean = s.ean and s.filNr = f.filNr and p.listPreis = s.vkPreis;	submission
57cf9a95-b789-3872-8f7e-6ee68b55a700	3	diagnoseLevel
57cf9a95-b789-3872-8f7e-6ee68b55a700	diagnose	action
57cf9a95-b789-3872-8f7e-6ee68b55a700	select distinct k.kundeNr, k.name, k.bonStufe\r\nfrom kunde k, rechnung r\r\nwhere k.kundeNr = r.kundeNr\r\nand k.kundeNr NOT IN\r\n(select r1.kundeNr from rechnung r1 where bezahlt = 'N');	submission
9f8faea7-99ce-38ab-9260-49eec92424a0	3	diagnoseLevel
9f8faea7-99ce-38ab-9260-49eec92424a0	diagnose	action
9f8faea7-99ce-38ab-9260-49eec92424a0	select p.ean, p.bezeichnung, p.ekPreis\r\nfrom produkt p\r\nwhere p.ekPreis > \r\n(select avg(p1.ekPreis)\r\n from produkt p1\r\n where p1.kategorie = p.kategorie);	submission
316c20e6-50be-36f2-87ee-f529f5f90b8d	3	diagnoseLevel
316c20e6-50be-36f2-87ee-f529f5f90b8d	diagnose	action
316c20e6-50be-36f2-87ee-f529f5f90b8d	SELECT ean, bezeichnung, Spanne, kategorie FROM (select p1.ean, p1.bezeichnung,\n((p1.listPreis - p1.ekPreis)/p1.ekPreis)*100 Spanne, p1.kategorie\nfrom produkt p1\nwhere ((p1.listPreis - p1.ekPreis)/p1.ekPreis) =\n    (select MIN((p2.listPreis - p2.ekPreis)/p2.ekPreis)\n     from produkt p2\n     group by p2.kategorie\n     having p2.kategorie = p1.kategorie) ) AS correctQuery ORDER BY ean, bezeichnung, Spanne, kategorie	submission
99057421-6877-39b3-90d6-919023ee0185	3	diagnoseLevel
99057421-6877-39b3-90d6-919023ee0185	diagnose	action
99057421-6877-39b3-90d6-919023ee0185	SELECT p.ean, p.bezeichnung, COUNT(f.filNr)\r\nFROM produkt p\r\nJOIN sortiment s ON p.ean = s.ean\r\nJOIN filiale f ON s.filNr = f.filNr\r\nWHERE p.ean NOT IN (\r\n  SELECT DISTINCT rp.ean\r\n  FROM rechnungPos rp\r\n)\r\nGROUP BY p.ean, p.bezeichnung;	submission
cdaa7dd5-0f50-3fcc-8047-d9e141d67356	3	diagnoseLevel
cdaa7dd5-0f50-3fcc-8047-d9e141d67356	diagnose	action
cdaa7dd5-0f50-3fcc-8047-d9e141d67356	select r.datum, r.rechnungNr, Sum(p.menge * p.einzelPreis) "Summe", k.name\r\nfrom rechnung r, kunde k, rechnungPos p\r\nwhere r.datum = p.datum AND r.rechnungNr = p.rechnungNr AND r.kundeNr = k.kundeNr\r\ngroup by r.datum, r.rechnungNr, k.name\r\nhaving not exists \r\n(select * from rechnungPos p1\r\n where p1.rechnungNr = r.rechnungNr AND p1.datum = r.datum\r\n and (p1.menge * p1.einzelPreis) <= 1000)\r\norder by Sum(p.menge * p.einzelPreis) DESC;	submission
a3ea0ac2-12d7-319b-85e2-28f590f8d6de	3	diagnoseLevel
a3ea0ac2-12d7-319b-85e2-28f590f8d6de	diagnose	action
a3ea0ac2-12d7-319b-85e2-28f590f8d6de	select f.filNr, f.inhName, f.strasse, f.plz\r\nfrom filiale f\r\nwhere not exists(\r\nselect p.kategorie\r\nfrom produkt p\r\nwhere 2*(select count(*) \r\n         from produkt p1, sortiment s where p1.ean = s.ean \r\n         and p1.kategorie = p.kategorie \r\n         and f.filNr = s.filNr)\r\n      <=\r\n        (select count(*)\r\n         from produkt p2\r\n         where p2.kategorie = p.kategorie));	submission
690ebcf6-a3f1-35fa-8215-5bfa287a4f58	3	diagnoseLevel
690ebcf6-a3f1-35fa-8215-5bfa287a4f58	diagnose	action
690ebcf6-a3f1-35fa-8215-5bfa287a4f58	select f.filNr, f.inhName, p.ean, p.bezeichnung, (s.vkPreis - s.preisRed) "Billigst"\r\nfrom filiale f, produkt p, sortiment s\r\nwhere f.filNr = s.filNr and s.ean = p.ean\r\nand (s.vkPreis - s.preisRed) = \r\n(select MIN(s1.vkPreis - s1.preisRed)\r\n from sortiment s1\r\n where s1.ean = p.ean);	submission
fc16e236-8c1f-36e2-8fb0-6fa5f76762c7	3	diagnoseLevel
fc16e236-8c1f-36e2-8fb0-6fa5f76762c7	diagnose	action
fc16e236-8c1f-36e2-8fb0-6fa5f76762c7	select f.filnr, f.inhName, count(*)\r\nfrom filiale f, sortiment s\r\nwhere f.filnr=s.filnr\r\ngroup by f.filnr, f.inhName\r\norder by count(*) desc;	submission
e9a68665-7752-3dc0-8315-194d80bbec42	3	diagnoseLevel
e9a68665-7752-3dc0-8315-194d80bbec42	diagnose	action
e9a68665-7752-3dc0-8315-194d80bbec42	select s.ean, s.filnr, (s.vkpreis-s.preisred) tats_Preis, p.listpreis,\r\n       ROUND(100*((s.vkpreis-s.preisred)-p.listpreis)/p.listpreis) Abweichung\r\nfrom sortiment s, produkt p\r\nwhere s.ean=p.ean\r\nand ABS((s.vkpreis-s.preisred)-p.listpreis)>0.2*p.listpreis\r\norder by s.ean, s.filnr;	submission
1459a00b-d9df-3fcb-8f50-c488dd648d0a	3	diagnoseLevel
1459a00b-d9df-3fcb-8f50-c488dd648d0a	diagnose	action
1459a00b-d9df-3fcb-8f50-c488dd648d0a	select distinct k.kundenr, k.name, k.bonStufe\r\nfrom kunde k, rechnung r\r\nwhere k.kundenr = r.kundenr\r\nand   r.bezahlt='N'\r\nand   k.kundenr NOT IN (select r2.kundenr from rechnung r2 where r2.bezahlt='Y')\r\norder by k.kundenr;	submission
10841f51-f738-3fb7-a4a8-fbb74e8cbf07	3	diagnoseLevel
10841f51-f738-3fb7-a4a8-fbb74e8cbf07	diagnose	action
10841f51-f738-3fb7-a4a8-fbb74e8cbf07	select s.filnr, p.kategorie, Sum(s.bestand*p.ekpreis) lagerwert\r\nfrom sortiment s, produkt p\r\nwhere s.ean=p.ean\r\nand p.kategorie NOT IN\r\n(select p2.kategorie\r\n from produkt p2, rechnungpos rp2, rechnung r2\r\n where p2.ean=rp2.ean\r\n and rp2.datum=r2.datum\r\n and rp2.rechnungnr=r2.rechnungnr\r\n and s.filnr=r2.filnr)\r\ngroup by s.filnr, p.kategorie\r\norder by s.filnr, p.kategorie	submission
79c560d2-e98c-32ec-9182-39f59de1b0bc	3	diagnoseLevel
79c560d2-e98c-32ec-9182-39f59de1b0bc	diagnose	action
79c560d2-e98c-32ec-9182-39f59de1b0bc	select p.kategorie, p.ean, SUM(rp.menge) Stueck, Sum(rp.einzelpreis*rp.menge) Umsatz\r\nfrom produkt p, rechnungpos rp\r\nwhere p.ean=rp.ean\r\ngroup by p.kategorie, p.ean\r\nhaving SUM(rp.menge)>=ALL\r\n (select sum(rp2.menge)\r\n  from produkt p2, rechnungpos rp2\r\n  where p2.ean = rp2.ean\r\n  and p2.kategorie =p.kategorie\r\n  group by p2.ean)\r\norder by p.kategorie;	submission
5b7eb953-03a8-39b3-a6d1-24bc7647c890	3	diagnoseLevel
5b7eb953-03a8-39b3-a6d1-24bc7647c890	diagnose	action
5b7eb953-03a8-39b3-a6d1-24bc7647c890	Select rp.rechnungnr, rp.datum, sum((rp.einzelpreis-p.ekpreis)*rp.menge) RG\r\nfrom rechnungpos rp, produkt p\r\nwhere p.ean=rp.ean\r\ngroup by rp.rechnungnr, rp.datum\r\nhaving sum((rp.einzelpreis-p.ekpreis)*rp.menge)\r\n       > (select avg(sum((rp2.einzelpreis-p2.ekpreis)*rp2.menge))\r\n          from rechnungpos rp2, produkt p2\r\n          where rp2.ean=p2.ean\r\n          group by rp2.rechnungnr, rp2.datum);	submission
a2c40e07-dbdd-3c4d-a593-c6e6cd1473b5	3	diagnoseLevel
a2c40e07-dbdd-3c4d-a593-c6e6cd1473b5	diagnose	action
1c6958b6-6312-3218-93d6-4b9096be9363	select v.kurzzeichen, v.name\r\nfrom vertreter v\r\nwhere not exists (\r\n  select * \r\n  from kunde k\r\n  where not exists (\r\n    select * \r\n    from auftragskopf ak, kunde k2\r\n    where ak.kunde = k2.nr and\r\n          k2.gebiet = k.gebiet and\r\n          ak.vertreter = v.kurzzeichen));	submission
519b4807-0a14-3d55-8da8-707ee605f068	3	diagnoseLevel
a2c40e07-dbdd-3c4d-a593-c6e6cd1473b5	select distinct f.filnr, f.plz, r.datum\r\nfrom filiale f, rechnung r\r\nwhere f.filnr = r.filnr\r\nand (select count(distinct p.kategorie)\r\n     from produkt p, sortiment s\r\n     where p.ean=s.ean and s.filnr=r.filnr) > 2*\r\n    (select count(distinct p2.kategorie)\r\n     from produkt p2, rechnungpos rp2, rechnung r2\r\n     where p2.ean=rp2.ean\r\n     and rp2.rechnungnr=r2.rechnungnr and rp2.datum=r2.datum\r\n     and r2.datum=r.datum and r2.filnr=r.filnr)\r\norder by f.filnr, r.datum;	submission
965cba69-a2dd-3159-b43d-b560a69a1101	3	diagnoseLevel
965cba69-a2dd-3159-b43d-b560a69a1101	diagnose	action
965cba69-a2dd-3159-b43d-b560a69a1101	select f.filnr, f.plz, sum(rp.menge*rp.einzelpreis) Umsatz\r\nfrom filiale f, rechnung r, rechnungpos rp\r\nwhere f.filnr=r.filnr\r\nand r.rechnungnr=rp.rechnungnr and r.datum = rp.datum\r\ngroup by f.filnr, f.plz\r\nhaving sum(rp.menge*rp.einzelpreis) >\r\n  (select avg(sum(rp2.menge*rp2.einzelpreis))\r\n   from filiale f2, rechnung r2, rechnungpos rp2\r\n   where f2.filnr=r2.filnr and r2.rechnungnr=rp2.rechnungnr and r2.datum=rp2.datum\r\n   group by f2.filnr)\r\norder by umsatz desc;	submission
4700c669-3168-38aa-82d0-ad9fad13297e	3	diagnoseLevel
4700c669-3168-38aa-82d0-ad9fad13297e	diagnose	action
4700c669-3168-38aa-82d0-ad9fad13297e	select p.ean, p.kategorie, sum(rp.einzelpreis*rp.menge) Umsatz, a.ges_umsatz\r\nfrom produkt p, rechnungpos rp,\r\n  (select p2.kategorie, sum(rp2.menge*rp2.einzelpreis) as ges_umsatz\r\n   from produkt p2, rechnungpos rp2\r\n   where p2.ean=rp2.ean\r\n   group by p2.kategorie) a\r\nwhere p.kategorie=a.kategorie\r\nand rp.ean=p.ean\r\ngroup by p.ean, p.kategorie, a.ges_umsatz\r\nhaving sum(rp.einzelpreis*rp.menge) > a.ges_umsatz/3\r\norder by p.ean;	submission
4ceb9215-112f-3b8c-bbb9-41d8810b905a	3	diagnoseLevel
4ceb9215-112f-3b8c-bbb9-41d8810b905a	diagnose	action
4ceb9215-112f-3b8c-bbb9-41d8810b905a	select kategorie, sum(vkpreis * menge) as umsatz \r\nfrom artikel a, auftragszeile az\r\nwhere az.artikel=a.ean\r\ngroup by kategorie\r\norder by umsatz;	submission
9f8ac15a-4e36-3c7a-a5d4-cd511ca389e5	3	diagnoseLevel
9f8ac15a-4e36-3c7a-a5d4-cd511ca389e5	diagnose	action
9f8ac15a-4e36-3c7a-a5d4-cd511ca389e5	select k.nr, k.name, sum(vkpreis * menge) as umsatz \r\nfrom kunde k, auftragskopf ak, auftragszeile az\r\nwhere k.nr=ak.kunde and\r\n  ak.nr = az.nr\r\ngroup by k.nr, k.name\r\nhaving sum(vkpreis*menge) > 5000\r\norder by umsatz;	submission
810da09d-57a6-3f4b-9101-43a9f2f5d654	3	diagnoseLevel
810da09d-57a6-3f4b-9101-43a9f2f5d654	diagnose	action
810da09d-57a6-3f4b-9101-43a9f2f5d654	select a.ean, a.bezeichnung\r\nfrom artikel a\r\nwhere\r\n  not exists (\r\n     select * \r\n     from auftragszeile az\r\n     where az.artikel = a.ean);	submission
37324db8-bc35-39db-8e00-43de4cc7e3fa	3	diagnoseLevel
37324db8-bc35-39db-8e00-43de4cc7e3fa	diagnose	action
37324db8-bc35-39db-8e00-43de4cc7e3fa	select distinct v.kurzzeichen, v.name\r\nfrom vertreter v, auftragskopf ak, kunde k\r\nwhere v.kurzzeichen = ak.vertreter AND\r\n      ak.kunde = k.nr AND\r\n      k.gebiet = 'OST';	submission
1b6facbf-3949-3e74-a00e-3c0eddaf75a1	3	diagnoseLevel
1b6facbf-3949-3e74-a00e-3c0eddaf75a1	diagnose	action
1b6facbf-3949-3e74-a00e-3c0eddaf75a1	select a.ean, a.bezeichnung, ekpreis, \r\n  sum(az.vkpreis * az.menge)/sum(Menge) as "d_vkpreis",\r\n  ((sum(az.vkpreis * az.menge)/sum(Menge))-ekpreis) as "d_db"\r\nfrom artikel a, auftragszeile az\r\nwhere a.ean = az.artikel\r\ngroup by ean, bezeichnung, ekpreis\r\norder by ean;	submission
c2c6ab58-a00e-3811-b87e-f0a40891bf19	3	diagnoseLevel
c2c6ab58-a00e-3811-b87e-f0a40891bf19	diagnose	action
c2c6ab58-a00e-3811-b87e-f0a40891bf19	select distinct k.gebiet\r\nfrom kunde k, auftragskopf ak, auftragszeile az\r\nwhere k.nr = ak.kunde AND\r\n      ak.nr = az.nr\r\ngroup by k.gebiet\r\nhaving sum(az.vkpreis * az.menge) = (\r\n  select max (sum(az2.vkpreis * az2.menge))\r\n  from kunde k2, auftragskopf ak2, auftragszeile az2\r\n  where k2.nr = ak2.kunde AND\r\n        ak2.nr = az2.nr\r\ngroup by k2.gebiet);	submission
065b6d84-0727-3a0c-a8b3-c696c983683d	3	diagnoseLevel
065b6d84-0727-3a0c-a8b3-c696c983683d	diagnose	action
065b6d84-0727-3a0c-a8b3-c696c983683d	select v.kurzzeichen as kz, v.name, v.provision, k.gebiet, \r\nsum(az.vkpreis*az.menge) as UMSATZ,\r\nsum(az.vkpreis*az.menge)*v.provision/100 as PROVBETRAG\r\nfrom kunde k, vertreter v, auftragskopf ak, auftragszeile az\r\nwhere k.nr = ak.kunde AND\r\n     v.kurzzeichen = ak.vertreter AND\r\n     ak.nr = az.nr AND\r\n     ak.lieferdatum >= '01-SEP-99'\r\ngroup by v.kurzzeichen, v.name, v.provision, k.gebiet\r\norder by v.kurzzeichen;	submission
38a7a0ef-9cc4-324f-adce-829b3af115b9	3	diagnoseLevel
38a7a0ef-9cc4-324f-adce-829b3af115b9	diagnose	action
38a7a0ef-9cc4-324f-adce-829b3af115b9	(select k.nr, a.kategorie, sum(az.vkpreis*az.menge) as UMSATZ\r\n from kunde k, artikel a, auftragskopf ak, auftragszeile az\r\n where k.nr=ak.kunde and\r\n       ak.nr=az.nr and\r\n       az.artikel=a.ean and\r\n       k.gebiet = 'OST'\r\n group by k.nr, a.kategorie\r\n)\r\nUNION\r\n(select distinct k2.nr, a2.kategorie, 0 as UMSATZ\r\n from kunde k2, artikel a2\r\n where k2.gebiet = 'OST' and\r\n       not exists (\r\n          select * \r\n          from auftragskopf ak3, auftragszeile az3, artikel a3\r\n          where ak3.nr=az3.nr and\r\n                ak3.kunde=k2.nr and\r\n                az3.artikel=a3.ean and\r\n                a3.kategorie=a2.kategorie)\r\n)\r\norder by nr, kategorie;	submission
7e3af2f3-4168-3445-ba22-5d543d627d35	3	diagnoseLevel
7e3af2f3-4168-3445-ba22-5d543d627d35	diagnose	action
7e3af2f3-4168-3445-ba22-5d543d627d35	SELECT a.ean, a.bezeichnung,\r\n       (SUM((az.vkpreis - a.ekpreis) * az.menge)/SUM(az.menge)) AS DB_ARTIKEL,\r\n       kategorie.DB_KATEG\r\nFROM   artikel a, auftragszeile az,\r\n       (SELECT a2.kategorie, (SUM((az2.vkpreis - a2.ekpreis)*az2.menge)/SUM(az2.menge)) AS DB_KATEG\r\n        FROM   artikel a2, auftragszeile az2\r\n        WHERE  az2.artikel=a2.ean\r\n        GROUP BY a2.kategorie) kategorie\r\nWHERE  a.ean=az.artikel AND\r\n       kategorie.kategorie=a.kategorie\r\nGROUP BY a.ean, a.bezeichnung, kategorie.DB_KATEG\r\nHAVING (SUM((az.vkpreis - a.ekpreis) * az.menge)/SUM(az.menge)) > kategorie.DB_KATEG\r\nORDER BY ean;\r\n\r\n/*select a.ean, a.bezeichnung,\r\n(sum (az.vkpreis * az.menge)/sum(menge))-avg(a.ekpreis) as "DB_ARTIKEL",\r\nkategorie."DB_KATEG"\r\nfrom artikel a, auftragszeile az,\r\n(select a2.kategorie, (sum(az2.vkpreis*az2.menge)/sum(az2.menge)) - avg(a2.ekpreis) as "DB_KATEG"\r\n from artikel a2, auftragszeile az2\r\n where az2.artikel=a2.ean\r\n group by a2.kategorie) kategorie\r\nwhere\r\n a.ean=az.artikel and\r\n kategorie.kategorie=a.kategorie\r\ngroup by a.ean, a.bezeichnung, kategorie."DB_KATEG"\r\nhaving (sum(az.vkpreis*az.menge)/sum(Menge))\r\n       - avg(a.ekpreis) > kategorie."DB_KATEG"\r\norder by ean;*/	submission
1c6958b6-6312-3218-93d6-4b9096be9363	3	diagnoseLevel
1c6958b6-6312-3218-93d6-4b9096be9363	diagnose	action
519b4807-0a14-3d55-8da8-707ee605f068	diagnose	action
519b4807-0a14-3d55-8da8-707ee605f068	(select k.nr, k.name\r\n from kunde k, auftragskopf ak, auftragszeile az\r\n where k.nr = ak.kunde and\r\n       ak.nr = az.nr and\r\n       az.menge > 0\r\n)\r\nMINUS\r\n(select k2.nr, k2.name\r\n from kunde k2, auftragskopf ak2\r\n where k2.nr = ak2.kunde and ak2.vertreter = 'GP');	submission
f4bb9b15-4e0a-318a-a3e3-fa1be9a40918	3	diagnoseLevel
f4bb9b15-4e0a-318a-a3e3-fa1be9a40918	diagnose	action
f4bb9b15-4e0a-318a-a3e3-fa1be9a40918	select count(*) from cat	submission
fd404a35-7cc5-3501-b50d-2e2b0d7a488c	3	diagnoseLevel
fd404a35-7cc5-3501-b50d-2e2b0d7a488c	diagnose	action
fd404a35-7cc5-3501-b50d-2e2b0d7a488c	select count(*) from cat	submission
8e3aed5a-8cf2-3d7d-92b6-8651f11c461a	3	diagnoseLevel
8e3aed5a-8cf2-3d7d-92b6-8651f11c461a	diagnose	action
8e3aed5a-8cf2-3d7d-92b6-8651f11c461a	select * from cat	submission
5dc6bedb-b927-3e11-b38e-f973a126414b	3	diagnoseLevel
5dc6bedb-b927-3e11-b38e-f973a126414b	diagnose	action
5dc6bedb-b927-3e11-b38e-f973a126414b	select count(*) from course	submission
03463af8-71e3-3a0d-94ec-5f1501ca48c3	3	diagnoseLevel
03463af8-71e3-3a0d-94ec-5f1501ca48c3	diagnose	action
03463af8-71e3-3a0d-94ec-5f1501ca48c3	select * from cat	submission
2210af29-53b2-309f-b698-ae4599e1151a	3	diagnoseLevel
2210af29-53b2-309f-b698-ae4599e1151a	diagnose	action
2210af29-53b2-309f-b698-ae4599e1151a	select name from course	submission
44d53152-80e1-38ac-83cb-dcb720361689	3	diagnoseLevel
44d53152-80e1-38ac-83cb-dcb720361689	diagnose	action
44d53152-80e1-38ac-83cb-dcb720361689	select * from course	submission
fa9fdaef-8e31-3249-bc75-5291e99b073a	3	diagnoseLevel
fa9fdaef-8e31-3249-bc75-5291e99b073a	diagnose	action
fa9fdaef-8e31-3249-bc75-5291e99b073a	select * from artist	submission
da538715-091b-3528-9f1d-7837823331db	3	diagnoseLevel
da538715-091b-3528-9f1d-7837823331db	diagnose	action
da538715-091b-3528-9f1d-7837823331db	SELECT titel, autor FROM buch WHERE LOWER(titel) LIKE '%database%';	submission
524c806b-3abc-3462-9617-446060265201	3	diagnoseLevel
524c806b-3abc-3462-9617-446060265201	diagnose	action
524c806b-3abc-3462-9617-446060265201	SELECT   g.branche, SUM(umsatz) AS Umsatz\r\nFROM     gewinne g\r\nWHERE    g.bundesland = 'OÖ' AND g.jahr = 2011\r\nGROUP BY g.branche\r\nHAVING   COUNT(g.firma) > 10;	submission
\.


--
-- Data for Name: submission_parameter_mapping; Type: TABLE DATA; Schema: public; Owner: etutor
--

COPY public.submission_parameter_mapping (submission, parameter_value, parameter_key) FROM stdin;
\.


--
-- Name: grading grading_pkey; Type: CONSTRAINT; Schema: public; Owner: etutor
--

ALTER TABLE ONLY public.grading
    ADD CONSTRAINT grading_pkey PRIMARY KEY (submission_id);


--
-- Name: report report_pkey; Type: CONSTRAINT; Schema: public; Owner: etutor
--

ALTER TABLE ONLY public.report
    ADD CONSTRAINT report_pkey PRIMARY KEY (submission_id);


--
-- Name: sql_solution_problems sql_solution_problems_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.sql_solution_problems
    ADD CONSTRAINT sql_solution_problems_pkey PRIMARY KEY (exercise_id);


--
-- Name: submission_attribute_mapping submission_attribute_mapping_pkey; Type: CONSTRAINT; Schema: public; Owner: etutor
--

ALTER TABLE ONLY public.submission_attribute_mapping
    ADD CONSTRAINT submission_attribute_mapping_pkey PRIMARY KEY (submission, attribute_key);


--
-- Name: submission_parameter_mapping submission_parameter_mapping_pkey; Type: CONSTRAINT; Schema: public; Owner: etutor
--

ALTER TABLE ONLY public.submission_parameter_mapping
    ADD CONSTRAINT submission_parameter_mapping_pkey PRIMARY KEY (submission, parameter_key);


--
-- Name: submission submission_pkey; Type: CONSTRAINT; Schema: public; Owner: etutor
--

ALTER TABLE ONLY public.submission
    ADD CONSTRAINT submission_pkey PRIMARY KEY (submission_id);


--
-- Name: submission_parameter_mapping fk2xtdfrgauc4p2wy8kp068ur42; Type: FK CONSTRAINT; Schema: public; Owner: etutor
--

ALTER TABLE ONLY public.submission_parameter_mapping
    ADD CONSTRAINT fk2xtdfrgauc4p2wy8kp068ur42 FOREIGN KEY (submission) REFERENCES public.submission(submission_id);


--
-- Name: submission_attribute_mapping fk385ourb3fpmjx7t1ap7o34tgy; Type: FK CONSTRAINT; Schema: public; Owner: etutor
--

ALTER TABLE ONLY public.submission_attribute_mapping
    ADD CONSTRAINT fk385ourb3fpmjx7t1ap7o34tgy FOREIGN KEY (submission) REFERENCES public.submission(submission_id);


--
-- Name: grading fkhguc7veqa3iewl4c4f9mykn10; Type: FK CONSTRAINT; Schema: public; Owner: etutor
--

ALTER TABLE ONLY public.grading
    ADD CONSTRAINT fkhguc7veqa3iewl4c4f9mykn10 FOREIGN KEY (report_submission_id) REFERENCES public.report(submission_id);


--
-- PostgreSQL database dump complete
--

