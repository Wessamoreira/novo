create table if not exists public.integracaomestregr
(
    codigo SERIAL not null,
    origem VARCHAR(25) not null,
    nomelote VARCHAR(200) not null,
    codigolote INT not null,
    situacao VARCHAR(12) not null,
    qtderegistros INT not null,
    created TIMESTAMP not null,
    datatransmissao TIMESTAMP,
    nomecreated VARCHAR(200) not null,
    codigocreated INT,
    constraint pkey_integracaomestregr primary key (codigo),
    constraint fk_integracaomestregr_usuario foreign key (codigocreated) references public.usuario (codigo)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    );
create table if not exists public.integracaomestregritem
(
    codigo SERIAL not null,
    codigointegracao INT not null,
    dadosenvio TEXT not null,
    dadosretorno TEXT not null,
    mensagemerro TEXT,
    constraint pkey_integracaomestregritem primary key (codigo),
    constraint fk_integracaomestregritem foreign key (codigointegracao) references public.integracaomestregr (codigo)
    ON DELETE CASCADE
    ON UPDATE CASCADE
    );
create table if not exists public.integracaomestregroperacoes (
    codigo SERIAL not null,
    origem VARCHAR(30) not null,
    matricula VARCHAR(40) not null,
    disciplina INT4 null,
    ano VARCHAR(4) null,
    semestre VARCHAR(1) not null,
    bimestre VARCHAR(1) not null,
    dadosenvio TEXT not null,
    dadosretorno TEXT not null,
    mensagemerro TEXT,
    constraint pkey_integracaomestregroperacoesitem primary key (codigo),
    constraint fk_integracaomestregroperacoesitem_disciplina foreign key (disciplina)
    references public.disciplina(codigo)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    constraint fk_integracaomestregroperacoes_matricula foreign key (matricula)
    references public.matricula(matricula)
    ON DELETE CASCADE
    ON UPDATE CASCADE
);