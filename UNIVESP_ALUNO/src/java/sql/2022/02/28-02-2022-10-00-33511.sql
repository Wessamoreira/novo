create table if not exists requerimentodisciplina (
	codigo serial,
	disciplina int not null,
	requerimento int not null,
	variavelNota VARCHAR(50),
	situacao VARCHAR(50)  default 'AGUARDANDO_ANALISE',
	dataDeferimentoIndeferimento timestamp,
	usuarioDeferimentoIndeferimento int,
	motivoIndeferimento text,
	created timestamp,
	codigoCreated int,
	nomeCreated varchar(300),
    updated timestamp,
    codigoUpdated int,
    nomeUpdated  varchar(300),
	
	constraint pk_requerimentodisciplina_codigo primary key (codigo),
	constraint fk_requerimentodisciplina_disciplina foreign key (disciplina) references disciplina (codigo),
	constraint fk_requerimentodisciplina_requerimento foreign key (requerimento) references requerimento (codigo),
	constraint fk_requerimentodisciplina_usuarioDeferimentoIndeferimento foreign key (usuarioDeferimentoIndeferimento) references usuario (codigo),
	constraint unq_requerimentodisciplina_dis_req unique (disciplina, requerimento)
);

create index if not exists    id_requerimentodisciplina_dis_req on requerimentodisciplina(disciplina, requerimento);
