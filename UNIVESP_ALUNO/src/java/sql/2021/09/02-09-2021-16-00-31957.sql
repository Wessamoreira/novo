alter table if exists tiporequerimento  add column if not exists	msgBloqueioNovaSolicitacaoAproveitamento text;
alter table if exists tiporequerimento  add column if not exists	qtdeMaximaIndeferidoAproveitamento int;
alter table if exists tiporequerimento  add column if not exists	percentualMinimoCargaHorariaAproveitamento numeric(20,2);
alter table if exists tiporequerimento  add column if not exists	qtdeMinimaDeAnosAproveitamento int;
alter table if exists disciplina  add column if not exists	percentualMinimoCargaHorariaAproveitamento numeric(20,2);
alter table if exists disciplina  add column if not exists	qtdeMinimaDeAnosAproveitamento int;

CREATE TABLE public.requerimentodisciplinasaproveitadas (
	codigo serial4 NOT NULL,
	disciplina int4 NOT NULL,
	nota float4 NOT NULL,
	frequencia float4 NOT NULL,
	requerimento int4 NOT NULL,
	motivoSituacao text,
	motivoRevisaoAnaliseAproveitamento text,
	ano varchar(4) NULL,
	semestre varchar(1) NULL,
	instituicao varchar(150) NULL,
	bloquearnovasolicitacoes bool NULL DEFAULT false,
	utilizanotaconceito bool NULL DEFAULT false,
	mediafinalconceito varchar(50) NULL,
	cidade int4 NULL,	
	cargahorariacursada int4 NULL DEFAULT 0,
	cargahoraria int4 NULL DEFAULT 0,
	tipo varchar(4) NULL,	
	nomedisciplinacursada varchar(200) NULL,
	situacaohistorico varchar(100) NULL,
	codigoant int4 NULL,
	nomeprofessor varchar(200) NULL,
	titulacaoprofessor varchar(50) NULL,
	sexoprofessor text NULL,
	created timestamp NULL,
	codigocreated int4 NULL,
	nomecreated varchar(255) NULL,
	updated timestamp NULL,
	codigoupdated int4 NULL,
	nomeupdated varchar(255) NULL,
	situacaoRequerimentoDisciplinasAproveitadasEnum varchar(50) default 'AGUARDANDO_ANALISE',
	responsaveldeferimento int4 NULL,
	datadeferimento timestamp NULL,
	responsavelindeferimento int4 NULL,
	dataindeferimento timestamp NULL,
	arquivoplanoensino int4 NULL,
	CONSTRAINT requerimentodisciplinasaproveitadas_pkey PRIMARY KEY (codigo),
	CONSTRAINT fk_requerimentodisciplinasaproveitadas_requerimento FOREIGN KEY (requerimento) REFERENCES public.requerimento(codigo) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT fk_requerimentodisciplinasaproveitadas_disciplina FOREIGN KEY (disciplina) REFERENCES public.disciplina(codigo) ON DELETE RESTRICT ON UPDATE RESTRICT,	
	CONSTRAINT fk_requerimentodisciplinasaproveitadas_arquivoplanoensino FOREIGN KEY (arquivoplanoensino) REFERENCES public.arquivo(codigo) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT fk_requerimentodisciplinasaproveitadas_responsaveldeferimento FOREIGN KEY (responsaveldeferimento) REFERENCES public.usuario(codigo) ON DELETE RESTRICT ON UPDATE RESTRICT,
	CONSTRAINT fk_requerimentodisciplinasaproveitadas_responsavelindeferimento FOREIGN KEY (responsavelindeferimento) REFERENCES public.usuario(codigo) ON DELETE RESTRICT ON UPDATE RESTRICT
);

