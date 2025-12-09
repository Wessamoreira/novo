
------------------- Pedro Andrade 6.27.0 26/07/2018 11:50 --------------------
alter table itemcondicaodescontorenegociacao add column quantidadediasatrasoparcelafinal integer;
alter table contaReceberNegociado add column valorOriginalJuro numeric(20,2) DEFAULT 0.0;
alter table contaReceberNegociado add column valorOriginalMulta numeric(20,2) DEFAULT 0.0;
alter table contaReceberNegociado add column itemcondicaodescontorenegociacao integer;
ALTER TABLE contaReceberNegociado ADD CONSTRAINT fk_contaReceberNegociado_itemcondicaodescontorenegociacao FOREIGN KEY (itemcondicaodescontorenegociacao) REFERENCES itemcondicaodescontorenegociacao (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;

------------------- Pedro Andrade 6.27.0 27/07/2018 09:10 --------------------
alter table textopadrao add column larguraassinatura real;
alter table textopadrao add column alturaassinatura real;

alter table textopadraodeclaracao add column larguraassinatura real;
alter table textopadraodeclaracao add column alturaassinatura real;

alter table textopadraoprocessoseletivo add column larguraassinatura real;
alter table textopadraoprocessoseletivo add column alturaassinatura real;

----------- Rodrigo 6.27.0 31/07/2018 17:54 ---------------------
alter table controleremessacontareceber  add column dataLimiteConcessaoDesconto2 date;
alter table controleremessacontareceber  add column valorDescontoDataLimite2 numeric(20,2);
alter table controleremessacontareceber  add column dataLimiteConcessaoDesconto3 date;
alter table controleremessacontareceber  add column valorDescontoDataLimite3 numeric(20,2);
alter table controleremessacontareceber  add column valorAbatimento numeric(20,2);

-------------- rodrigo 6.27.0 01/08/2018 09:14 ---------------------
alter table controleremessacontareceber  add column situacaoconta varchar(2);

update controleremessacontareceber set situacaoconta = 'AR' from contareceber 
where contareceber.codigo = controleremessacontareceber.contareceber
and contareceber.situacao in ('AR', 'RE') and situacaoconta is null;

update controleremessacontareceber set situacaoconta = 'CF' from contareceber, controleremessa
where contareceber.codigo = controleremessacontareceber.contareceber
and controleremessa.codigo = controleremessacontareceber.controleremessa
and contareceber.situacao = 'CF' 
and datacancelamento < controleremessa.datageracao
and situacaoconta is null;

update controleremessacontareceber set situacaoconta = 'AR' from contareceber, controleremessa
where contareceber.codigo = controleremessacontareceber.contareceber
and controleremessa.codigo = controleremessacontareceber.controleremessa
and contareceber.situacao = 'CF' 
and datacancelamento >= controleremessa.datageracao
and situacaoconta is null;

update controleremessacontareceber set situacaoconta = 'AR' from contareceber, controleremessa
where contareceber.codigo = controleremessacontareceber.contareceber
and controleremessa.codigo = controleremessacontareceber.controleremessa
and contareceber.situacao = 'CF' 
and datacancelamento is null
and situacaoconta is null;

update controleremessacontareceber set situacaoconta = 'AR' 
where (controleremessacontareceber.contareceber is null
or not exists (select codigo from contareceber where controleremessacontareceber.contareceber = contareceber.codigo ))
and situacaoconta is null;



------------- Carlos 01-08-2018 10:30 ---------------------
create table CondicaoRenegociacaoFuncionario
(
	codigo serial not null,
	condicaoRenegociacao integer not null,
	funcionario integer not null,
	primary key(codigo)
);

ALTER TABLE CondicaoRenegociacaoFuncionario
  ADD CONSTRAINT fk_condicaoRenegociacaoFuncionario_condicaorenegociacao FOREIGN KEY (condicaorenegociacao)
      REFERENCES condicaorenegociacao (codigo) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE condicaoRenegociacaoFuncionario
  ADD CONSTRAINT fk_condicaoRenegociacaoFuncionario_funcionario FOREIGN KEY (funcionario)
      REFERENCES funcionario (codigo) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;

------------------------------------------------------------------------------------------------------------


ALTER TABLE itemcondicaorenegociacao ADD COLUMN tipoorigemmatricula boolean default true;
ALTER TABLE itemcondicaorenegociacao ADD COLUMN tipoorigemmensalidade boolean default true;
ALTER TABLE itemcondicaorenegociacao ADD COLUMN tipoorigembiblioteca boolean default true;
ALTER TABLE itemcondicaorenegociacao ADD COLUMN tipoorigemdevolucaocheque boolean default true;
ALTER TABLE itemcondicaorenegociacao ADD COLUMN tipoorigemnegociacao boolean default true;
ALTER TABLE itemcondicaorenegociacao ADD COLUMN tipoorigembolsacusteadaconvenio boolean default true;
ALTER TABLE itemcondicaorenegociacao ADD COLUMN tipoorigemcontratoreceita boolean default true;
ALTER TABLE itemcondicaorenegociacao ADD COLUMN tipoorigemoutros boolean default true;
ALTER TABLE itemcondicaorenegociacao ADD COLUMN tipoorigeminclusaoreposicao boolean default true;

---------------------------------------------------------------------------------------------------------------

create table CondicaoRenegociacaoUnidadeEnsino
(
	codigo serial not null,
	condicaoRenegociacao integer not null,
	unidadeEnsino integer not null,
	contaCorrente integer not null,
	primary key(codigo)
);

ALTER TABLE CondicaoRenegociacaoUnidadeEnsino
  ADD CONSTRAINT fk_CondicaoRenegociacaoUnidadeEnsino_condicaorenegociacao FOREIGN KEY (condicaorenegociacao)
      REFERENCES condicaorenegociacao (codigo) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE CondicaoRenegociacaoUnidadeEnsino
  ADD CONSTRAINT fk_CondicaoRenegociacaoUnidadeEnsino_unidadeEnsino FOREIGN KEY (unidadeEnsino)
      REFERENCES unidadeEnsino (codigo) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;

ALTER TABLE CondicaoRenegociacaoUnidadeEnsino
  ADD CONSTRAINT fk_CondicaoRenegociacaoUnidadeEnsino_contaCorrente FOREIGN KEY (contaCorrente)
      REFERENCES contaCorrente (codigo) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE;  

------------------------------------------------------------------------------------------------------------------
alter table condicaorenegociacao add column utilizarcontacorrenteespecifica boolean default true;
alter table condicaorenegociacao alter column contacorrentepadrao drop not null;
alter table condicaorenegociacao alter column unidadeensino drop not null;
alter table itemcondicaorenegociacao add column  utilizarVisaoAdministrativa boolean default true;
alter table itemcondicaorenegociacao add column  utilizarVisaoAluno boolean default true;
alter table itemcondicaorenegociacao add column tipoParcelaNegociar varchar(10);
alter table itemcondicaorenegociacao add column valorMinimoPorParcela NUMERIC(20,2);
alter table itemcondicaorenegociacao add column qtdeInicialDiasAtraso integer;
alter table itemcondicaorenegociacao add column qtdeFinalDiasAtraso integer;
-------------------------------------------------------------------------

alter table negociacaocontareceber add column permitirRenegociacaoApenasComCondicaoRenegociacao boolean default false;
alter table negociacaocontareceber add column responsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao integer;
alter table negociacaocontareceber add column dataLiberacaoRenegociarDesativandoCondicaoRenegociacao timestamp;
alter table negociacaocontareceber add column liberarRenegociarDesativandoCondicaoRenegociacao boolean default false;
alter table negociacaocontareceber add column responsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao integer;
alter table negociacaocontareceber add column dataLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao timestamp;
alter table negociacaocontareceber add column liberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao boolean default false;

ALTER TABLE negociacaocontareceber
  ADD CONSTRAINT fk_condicaorenegociacao_responsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao FOREIGN KEY (responsavelLiberacaoRenegociarDesativandoCondicaoRenegociacao)
      REFERENCES usuario (codigo) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE negociacaocontareceber
  ADD CONSTRAINT fk_condicaorenegociacao_responsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao FOREIGN KEY (responsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao)
      REFERENCES usuario (codigo) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT;

	  --------- Rodrigo - 01-08-2018 10:30 -----------
alter table negociacaocontareceber rename column responsavelLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoReal to respLibUsuarioNaoVinculadoCondRenegRealizarNegociacao;
alter table negociacaocontareceber rename column dataLiberacaoUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao to dataLibUsuarioNaoVinculadoCondRenegRealizarNegociacao;
alter table negociacaocontareceber rename column liberarUsuarioNaoVinculadoCondicaoRenegociacaoRealizarNegociacao to liberarUsuarioNaoVinculadoCondRenegRealizarNegociacao;

----- Marcos Paulo 17/07/2018  08:25 ----
alter table configuracaoMobile add column idremetentegoogleprofessor text;
alter table configuracaoMobile add column certificadoapnsappleprofessor bytea;
alter table configuracaoMobile add column certificadoapnsdestribuicaoprofessor boolean DEFAULT false;
alter table configuracaoMobile add column senhacertificadoapnsprofessor character varying(50);

------------ Rodrigo 03/08/2018 15:31 ---------------------
alter table turmadisciplinacomposta  add column configuracaoacademico int;
alter table turmadisciplinacomposta  add constraint fk_turmadisciplinacomposta_configuracaoacademico foreign key (configuracaoacademico) references configuracaoacademico (codigo) ;

----------- Rodrigo 06/08/2018 17:25 --------------
update Requisicao set solicitanteRequisicao = responsavelRequisicao where solicitanteRequisicao is null;

---- Alessandro 07/08/2018 16:30 ----
alter table elastic.sync add column id serial primary key;

----- Renato Borges 16/04/2018 17:52 7.0.0 -----
	
ALTER TABLE public.lancamentofolhapagamento ADD COLUMN secaofolhapagamento integer;
ALTER TABLE public.lancamentofolhapagamento ADD CONSTRAINT "secaoFolhaPagamento_fkey" FOREIGN KEY (secaofolhapagamento)
    REFERENCES public.secaofolhapagamento (codigo) MATCH SIMPLE 
	ON UPDATE NO ACTION ON DELETE NO ACTION;	
    
    
----- Gilberto Nery 18/04/2018 10:26 7.0.0 -----    
    
alter table TemplateLancamentoFolhaPagamento add column funcionariocargo integer;
ALTER TABLE public.templateLancamentoFolhaPagamento ADD CONSTRAINT "templateLancamentoFolhaPagamento_funcionariocargo_fkey" FOREIGN KEY (funcionariocargo)
    REFERENCES public.funcionariocargo (codigo) MATCH SIMPLE 
	ON UPDATE NO ACTION ON DELETE NO ACTION;	

----- Renato Borges 17/04/2018 17:04 7.0.0 -----
	
ALTER TABLE tabelareferenciafolhapagamento RENAME TO valorreferenciafolhapagamento;
ALTER TABLE faixavalor RENAME COLUMN tabelareferenciafolhapagamento TO valorreferenciafolhapagamento;

ALTER TABLE public.valorreferenciafolhapagamento ADD COLUMN sql TEXT;	

----- Renato Borges 17/04/2018 17:04 7.0.0 -----

ALTER TABLE public.valorreferenciafolhapagamento ADD COLUMN tipovalorreferencia varchar(30);

----------------------		SPRINT 3	----------------------

----- Gilberto Nery 18/04/2018 10:26 7.0.0 -----

ALTER TABLE public.lancamentofolhapagamento ADD COLUMN ativo boolean;		
ALTER TABLE public.lancamentofolhapagamento ADD COLUMN competenciaFolhaPagamento integer;
ALTER TABLE public.lancamentofolhapagamento ADD CONSTRAINT "competenciaFolhaPagamento_fkey" FOREIGN KEY (competenciaFolhaPagamento)
    REFERENCES public.competenciaFolhaPagamento (codigo) MATCH SIMPLE 
	ON UPDATE NO ACTION ON DELETE NO ACTION;	
ALTER TABLE public.lancamentofolhapagamento ADD CONSTRAINT "competenciaperiodofolhapagamento_fkey" FOREIGN KEY (periodo)
    REFERENCES public.competenciaperiodofolhapagamento (codigo) MATCH SIMPLE 
	ON UPDATE NO ACTION ON DELETE NO ACTION;	
	
	
----- Renato Borges 20/04/2018 11:52 7.0.0 -----

ALTER TABLE public.funcionariocargo ADD COLUMN databasequinquenio timestamp without time zone;
ALTER TABLE public.funcionariocargo ADD COLUMN previdencia character varying(50);
ALTER TABLE public.funcionariocargo ADD COLUMN optantetotal boolean;
ALTER TABLE public.funcionariocargo ADD COLUMN departamento integer;
ALTER TABLE public.funcionariocargo ADD CONSTRAINT funcionariocargo_departamento_fkey FOREIGN KEY (departamento)
    REFERENCES public.departamento (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;



----- Gilberto Nery 27/04/2018 14:10 7.0.0 -----
ALTER TABLE public.competenciafolhapagamento ADD COLUMN usuarioultimaalteracao integer;
ALTER TABLE public.competenciafolhapagamento ADD COLUMN dataultimaalteracao timestamp without time zone;

ALTER TABLE eventofolhapagamento ADD COLUMN decimoTerceiroPrevidenciaPropria boolean;
ALTER TABLE eventofolhapagamento ADD COLUMN decimoTerceiroPlanoSaude boolean;
ALTER TABLE eventofolhapagamento ADD COLUMN previdenciaPropria boolean;
ALTER TABLE eventofolhapagamento ADD COLUMN planoSaude boolean;


----- Renato Borges 02/05/2018 09:15 7.0.0 -----

CREATE TABLE contracheque
(
  codigo serial NOT NULL,
  lancamentofolhapagamento integer,
  competenciafolhapagamento integer,
  funcionariocargo integer,  
  totalprovento numeric(20,2),
  totaldesconto numeric(20,2),
  totalreceber numeric(20,2),
  CONSTRAINT contracheque_pkey PRIMARY KEY (codigo),
  CONSTRAINT competenciafolhapagamento_fkey FOREIGN KEY (competenciafolhapagamento)
      REFERENCES competenciafolhapagamento (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT funcionariocargo_fkey FOREIGN KEY (funcionariocargo)
      REFERENCES funcionariocargo (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT lancamentofolhapagamento_fkey FOREIGN KEY (lancamentofolhapagamento)
      REFERENCES lancamentofolhapagamento (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE contracheque
  OWNER TO postgres;

ALTER TABLE public.contracheque ADD COLUMN tiporecebimento character varying(50);
ALTER TABLE public.contracheque ADD COLUMN salario numeric(12, 2);
ALTER TABLE public.contracheque ADD COLUMN previdencia character varying(50);
ALTER TABLE public.contracheque ADD COLUMN optantetotal BOOLEAN;
ALTER TABLE public.contracheque ADD COLUMN fgts numeric(12, 2);
ALTER TABLE public.contracheque ADD COLUMN dsr numeric(12, 2);
ALTER TABLE public.contracheque ADD COLUMN salariofamilia numeric(12, 2);
ALTER TABLE public.contracheque ADD COLUMN numerodependentesalariofamilia INTEGER;
ALTER TABLE public.contracheque ADD COLUMN previdenciapropria numeric(12, 2);
ALTER TABLE public.contracheque ADD COLUMN planosaude numeric(12, 2);
ALTER TABLE public.contracheque ADD COLUMN basecalculoinss numeric(12, 2);
ALTER TABLE public.contracheque ADD COLUMN valorinss numeric(12, 2);
ALTER TABLE public.contracheque ADD COLUMN basecalculoirrf numeric(12, 2);
ALTER TABLE public.contracheque ADD COLUMN faixa numeric(12, 2);
ALTER TABLE public.contracheque ADD COLUMN dedutivel numeric(12, 2);
ALTER TABLE public.contracheque ADD COLUMN numerosdependentes INTEGER;
ALTER TABLE public.contracheque ADD COLUMN valordependente numeric(12, 2);
ALTER TABLE public.contracheque ADD COLUMN valorirrf numeric(12, 2);
ALTER TABLE public.contracheque ADD COLUMN informerendimento numeric(12, 2);
ALTER TABLE public.contracheque ADD COLUMN rais numeric(12, 2);

----- Renato Borges 02/05/2018 17:43 7.0.0 -----

ALTER TABLE cargo ADD COLUMN legislacao CHARACTER VARYING(30);


----- Gilberto Nery 03/05/2018 14:26 7.0.0 -----

CREATE TABLE public.templateeventofolhapagamento
(
	codigo serial NOT NULL,
	valor numeric(20,2),
	templatelancamentofolhapagamento integer,
	eventofolhapagamento integer,
	CONSTRAINT templateeventofolhapagamento_pk PRIMARY KEY (codigo),
	CONSTRAINT templateeventofolhapagamento_templatelancamento_fkey FOREIGN KEY (templatelancamentofolhapagamento) REFERENCES templatelancamentofolhapagamento (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)WITH (	OIDS = FALSE );

----- Renato Borges 04/05/2018 09:19 7.0.0 -----
DROP TABLE IF EXISTS grupoeventofolhapagamento;


----- Gilberto Nery 04/05/2018 17:45 7.0.0 -----

CREATE TABLE contrachequeevento
(
  codigo serial NOT NULL,
  contracheque integer,
  eventofolhapagamento integer,
  valorreferencia numeric(20,2),
  provento numeric(20,2),
  desconto numeric(20,2),
  CONSTRAINT contrachequeevento_pkey PRIMARY KEY (codigo),
  CONSTRAINT contrachequeevento_contracheque_fkey FOREIGN KEY (contracheque)
      REFERENCES contracheque (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT contrachequeevento_eventofolhapagamento_fkey FOREIGN KEY (eventofolhapagamento)
      REFERENCES eventofolhapagamento (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
  OIDS=FALSE
);
ALTER TABLE contrachequeevento
  OWNER TO postgres;


----- Gilberto Nery 07/05/2018 15:25 7.0.0 -----

ALTER TABLE public.contracheque ADD COLUMN periodo integer;

ALTER TABLE public.contracheque ADD CONSTRAINT "contracheque_competenciaperiodofolhapagamento_fkey" FOREIGN KEY (periodo)
    REFERENCES public.competenciaperiodofolhapagamento (codigo) MATCH SIMPLE 
	ON UPDATE NO ACTION ON DELETE NO ACTION;	
	

	
----- Renato Borges 08/05/2018 10:16 7.0.0 -----

ALTER TABLE public.templatelancamentofolhapagamento ADD COLUMN secaofolhapagamento integer;
ALTER TABLE public.templatelancamentofolhapagamento ADD CONSTRAINT "secaoFolhaPagamento_fkey" FOREIGN KEY (secaofolhapagamento)
    REFERENCES public.secaofolhapagamento (codigo) MATCH SIMPLE 
	ON UPDATE NO ACTION ON DELETE NO ACTION;		

----- Gilberto Nery 09/05/2018 11:19 7.0.0 -----

ALTER TABLE public.contrachequeevento ADD COLUMN referencia character varying(50);

----- Renato Borges 10/05/2018 17:24 7.0.0 -----

ALTER TABLE public.eventofolhapagamento ADD COLUMN dedutivelirrf boolean NOT NULL DEFAULT FALSE;
ALTER TABLE public.eventofolhapagamento ADD COLUMN estornainss boolean NOT NULL DEFAULT FALSE;
ALTER TABLE public.eventofolhapagamento ADD COLUMN estornairrf boolean NOT NULL DEFAULT FALSE;
ALTER TABLE public.eventofolhapagamento ADD COLUMN estornafgts boolean NOT NULL DEFAULT FALSE;
ALTER TABLE public.eventofolhapagamento ADD COLUMN estornavaletransporte boolean NOT NULL DEFAULT FALSE;
ALTER TABLE public.eventofolhapagamento ADD COLUMN estornasalariofamilia boolean NOT NULL DEFAULT FALSE;
ALTER TABLE public.eventofolhapagamento ADD COLUMN estornairrfFerias boolean NOT NULL DEFAULT FALSE;
ALTER TABLE public.eventofolhapagamento ADD COLUMN dedutivelirrfFerias boolean NOT NULL DEFAULT FALSE;
ALTER TABLE public.eventofolhapagamento ADD COLUMN estornainssdecimoterceiro boolean NOT NULL DEFAULT FALSE;
ALTER TABLE public.eventofolhapagamento ADD COLUMN estornafgtsdecimoterceiro boolean NOT NULL DEFAULT FALSE;
ALTER TABLE public.eventofolhapagamento ADD COLUMN estornairrfdecimoterceiro boolean NOT NULL DEFAULT FALSE;
ALTER TABLE public.eventofolhapagamento ADD COLUMN dedutivelirrfdecimoterceiro boolean NOT NULL DEFAULT FALSE;
ALTER TABLE public.eventofolhapagamento ADD COLUMN folhapensaodesconto boolean NOT NULL DEFAULT FALSE;
ALTER TABLE public.eventofolhapagamento ADD COLUMN feriaspensaodesconto boolean NOT NULL DEFAULT FALSE;
ALTER TABLE public.eventofolhapagamento ADD COLUMN decimentoterceiropensaodesconto boolean NOT NULL DEFAULT FALSE;
ALTER TABLE public.eventofolhapagamento ADD COLUMN participacaolucropensaodesconto boolean NOT NULL DEFAULT FALSE;
ALTER TABLE public.eventofolhapagamento ADD COLUMN previdenciaobrigatoria boolean NOT NULL DEFAULT FALSE;

----- Renato Borges 21/05/2018 17:24 7.0.0 -----

ALTER TABLE public.contrachequeevento ADD COLUMN informadomanual BOOLEAN;

----- Renato Borges 23/05/2018 11:55 7.0.0 -----

ALTER TABLE public.eventofixocargofuncionario RENAME evento TO eventofolhapagamento;
ALTER TABLE public.funcionariodependente RENAME formulacalculo TO formulafolhapagamento;

----- Renato Borges 23/05/2018 16:24 7.0.0 -----

ALTER TABLE public.funcionariodependente DROP CONSTRAINT eventofolhapagamento_formulacalculo_fkey;
ALTER TABLE public.funcionariodependente DROP COLUMN formulafolhapagamento;

----- Hyllner Valadares da Silva 25/05/2018 15:05 7.0.0 -----
ALTER TABLE public.eventofixocargofuncionario ADD COLUMN valor numeric(20,2) NOT NULL;
ALTER TABLE public.eventofixocargofuncionario ADD COLUMN lancamentoFixo boolean NOT NULL;
ALTER TABLE public.eventofixocargofuncionario ADD COLUMN numeroLancamento integer NOT NULL;

----- Renato Borges 25/05/2018 11:48 7.0.0 -----

ALTER TABLE public.eventofolhapagamento ADD COLUMN dataultimaalteracao timestamp without time zone;
ALTER TABLE public.eventofolhapagamento ADD COLUMN usuarioresponsavel integer;
ALTER TABLE public.eventofolhapagamento ADD CONSTRAINT eventofolhapagamento_usuarioresponsavel_fkey FOREIGN KEY (usuarioresponsavel)
    REFERENCES public.usuario (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

----- Gilberto Nery 28/05/2018 18:00 7.0.0 ----- 

ALTER TABLE public.eventofolhapagamento ADD COLUMN usuarioresponsavelalteracao integer;
ALTER TABLE public.eventofolhapagamento ADD COLUMN dataultimaalteracao timestamp without time zone;

ALTER TABLE public.eventofolhapagamento ADD CONSTRAINT eventofolhapagamento_usuario_fkey FOREIGN KEY (usuarioresponsavelalteracao)
    REFERENCES public.usuario (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
    
----- Renato Borges 31/05/2018 08:42 7.0.0 -----

CREATE TABLE public.tipoemprestimo (
    codigo serial NOT NULL,
    descricao character varying(100) NOT NULL,
    banco integer NOT NULL,
    agencia integer,
    tipo character varying(50),
    PRIMARY KEY (codigo),
    CONSTRAINT banco_fkey FOREIGN KEY (banco) REFERENCES public.banco (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT agencia_fkey FOREIGN KEY (agencia) REFERENCES public.agencia (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
);

ALTER TABLE public.tipoemprestimo
    OWNER to postgres;

----- Renato Borges 04/06/2018 09:16 7.0.0 -----
    
CREATE TABLE public.tipotransporte (
    codigo serial NOT NULL,
    descricao character varying(100) NOT NULL,
    PRIMARY KEY (codigo)
)
WITH (
    OIDS = FALSE
);

ALTER TABLE public.tipotransporte
    OWNER to postgres;
	
----- Renato Borges 31/05/2018 15:41 7.0.0 -----

CREATE TABLE public.linhatransporte (
    codigo serial NOT NULL,
    descricao character varying(100) NOT NULL,
    tipolinhatransporte integer,
    tipotarifa character varying(50),
    valor numeric(20, 2),
    itinerario character varying(255),
    situacao character varying(50),
    eventofolhapagamento integer,
	usuarioUltimaAlteracao integer,
	dataUltimaAlteracao timestamp without time zone,
    PRIMARY KEY (codigo),
    CONSTRAINT eventofolhapagamento_fkey FOREIGN KEY (eventofolhapagamento)
        REFERENCES public.eventofolhapagamento (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT tipotransporte_fkey FOREIGN KEY (tipolinhatransporte)
        REFERENCES public.tipotransporte (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT usuarioultimaalteracao_fkey FOREIGN KEY (usuarioUltimaAlteracao)
		REFERENCES public.usuario (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
);

ALTER TABLE public.linhatransporte
    OWNER to postgres;

----- Renato Borges 07/06/2018 17:15 7.0.0 -----

ALTER TABLE funcionariocargo ALTER COLUMN formacontratacao TYPE character varying (50) COLLATE pg_catalog."default";
update funcionariocargo set formacontratacao = 'ESTATUTARIO' where formacontratacao = 'E';
update funcionariocargo set formacontratacao = 'CEDIDO' where formacontratacao = 'I';
update funcionariocargo set formacontratacao = 'NORMAL' where formacontratacao = 'N';
update funcionariocargo set formacontratacao = 'OUTROS' where formacontratacao = 'U';

----- Renato Borges 11/06/2018 18:00 7.0.0 -----

ALTER TABLE funcionariocargo ALTER COLUMN situacaofuncionario TYPE character varying (50) COLLATE pg_catalog."default";
update funcionariocargo set situacaofuncionario = 'ATIVO' where situacaofuncionario = 'A';
update funcionariocargo set situacaofuncionario = 'DEMITIDO' where situacaofuncionario = 'D';
update funcionariocargo set situacaofuncionario = 'LICENSA_MATERNIDADE' where situacaofuncionario = 'E';
update funcionariocargo set situacaofuncionario = 'FERIAS' where situacaofuncionario = 'F';
update funcionariocargo set situacaofuncionario = 'LICENSA_SEM_VENCIMENTO' where situacaofuncionario = 'L';
update funcionariocargo set situacaofuncionario = 'AFASTAMENTO_PREVIDENCIA' where situacaofuncionario = 'P';
update funcionariocargo set situacaofuncionario = 'LICENSA_REMUNERADA' where situacaofuncionario = 'R';


ALTER TABLE funcionariocargo ALTER COLUMN tiporecebimento TYPE character varying (50) COLLATE pg_catalog."default";
update funcionariocargo set tiporecebimento = 'HORISTA' where tiporecebimento = 'H';
update funcionariocargo set tiporecebimento = 'MENSALISTA' where tiporecebimento = 'M';

----- Renato Borges 12/06/2018 09:00 7.0.0 -----

CREATE TABLE public.salariocomposto (
    codigo serial NOT NULL,
    funcionariocargo integer NOT NULL,
    eventofolhapagamento integer,
    jornada integer,
    valorhora numeric(20, 2),
    valormensal numeric(20, 2),
    PRIMARY KEY (codigo),
    CONSTRAINT funcionariocargo_fkey FOREIGN KEY (funcionariocargo) REFERENCES public.funcionariocargo (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT eventofolhapagamento_fkey FOREIGN KEY (eventofolhapagamento) REFERENCES public.eventofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
);

ALTER TABLE public.salariocomposto
    OWNER to postgres;

----- Gilberto Nery 12/06/2018 17:17 7.0.0 -----

CREATE TABLE public.eventoemprestimocargofuncionario
(
	codigo serial NOT NULL,
	eventofolhapagamento integer not null,
	funcionariocargo integer not null,
	numeroParcela integer not null,
	valorParcela NUMERIC(20,2),
	valorTotal NUMERIC(20,2),
	dataEmprestimo timestamp without time zone,
	inicioDesconto timestamp without time zone,
	parcelaPaga integer,	
	tipoEmprestimo integer not null,
	CONSTRAINT eventoemprestimocargofuncionario_pk PRIMARY KEY (codigo),
	CONSTRAINT eventoemprestimocargofuncionario_evento_fkey FOREIGN KEY (eventofolhapagamento) REFERENCES eventofolhapagamento (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE,
	CONSTRAINT eventoemprestimocargofuncionario_funcionariocargo_fkey FOREIGN KEY (funcionariocargo) REFERENCES funcionariocargo (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT eventoemprestimocargofuncionario_tipoemprestimo_fkey FOREIGN KEY (tipoEmprestimo) REFERENCES tipoEmprestimo (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
) WITH ( OIDS = FALSE );

ALTER TABLE public.eventoemprestimocargofuncionario OWNER to postgres;	

----- Renato Borges 13/06/2018 14:15 7.0.0 -----

CREATE TABLE public.eventovaletransportefuncionariocargo
(
    codigo serial NOT NULL,
    funcionariocargo integer NOT NULL,
    linhatransporte integer NOT NULL,
    numeroviagensdia integer,
    diasuteis integer,
    diasuteisproximomes integer,
    eventofolhapagamento integer,
	utilizasalarionominal boolean,
    PRIMARY KEY (codigo),
    CONSTRAINT funcionariocargo_fkey FOREIGN KEY (funcionariocargo) REFERENCES public.funcionariocargo (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT eventofolhapagamento FOREIGN KEY (eventofolhapagamento) REFERENCES public.eventofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
);

ALTER TABLE public.eventovaletransportefuncionariocargo
    OWNER to postgres;

----- Renato Borges 14/06/2018 17:05 7.0.0 -----

ALTER TABLE public.funcionariocargo ADD COLUMN salariocargoatual NUMERIC(20,2);

----- Renato Borges 19/06/2018 09:18 7.0.0 -----

ALTER TABLE public.funcionariodependente ADD COLUMN formulafolhapagamento integer;
ALTER TABLE public.funcionariodependente ADD CONSTRAINT formulafolhapagamento_fkey FOREIGN KEY (formulafolhapagamento) REFERENCES formulafolhapagamento (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;

----- Gilberto Nery 19/06/2018 09:46 7.0.0 -----

ALTER TABLE public.eventofixocargofuncionario ADD COLUMN numerototallancamento integer;    

ALTER TABLE public.funcionariodependente ADD COLUMN eventoFolhaPagamento integer;
ALTER TABLE public.funcionariodependente ADD CONSTRAINT funcionariodependente_evento_fkey FOREIGN KEY (eventoFolhaPagamento) REFERENCES eventofolhapagamento (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;

----- Renato Borges 19/06/2018 11:07 7.0.0 -----	

CREATE TABLE public.eventofolhapagamentoitem (
    codigo serial NOT NULL,
    eventofolha integer NOT NULL,
    eventofolhapagamento integer NOT NULL,
    PRIMARY KEY (codigo),
    CONSTRAINT eventofolha_fkey FOREIGN KEY (eventofolha) REFERENCES public.eventofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT eventofolhapagamento_fkey FOREIGN KEY (eventofolhapagamento) REFERENCES public.eventofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
);

ALTER TABLE public.eventofolhapagamentoitem
    OWNER to postgres;	

----- Renato Borges 19/06/2018 11:07 7.0.0 -----	
	
ALTER TABLE public.eventofolhapagamento ADD COLUMN admissao boolean;
ALTER TABLE public.eventofolhapagamento ADD COLUMN demissao boolean;
ALTER TABLE public.eventofolhapagamento ADD COLUMN ferias boolean;
ALTER TABLE public.eventofolhapagamento ADD COLUMN afastamento boolean;
ALTER TABLE public.eventofolhapagamento ADD COLUMN eventopadrao boolean;
ALTER TABLE public.eventofolhapagamento ADD COLUMN valetransporte boolean;
----- Gilberto Nery 20/06/2018 11:45 7.0.0 -----

alter table TemplateLancamentoFolhaPagamento add column lancarEventosGrupo boolean;
alter table TemplateLancamentoFolhaPagamento add column lancarSalarioComposto boolean;

----- Renato Borges 19/06/2018 11:07 7.0.0 -----	
ALTER TABLE public.competenciafolhapagamento ADD COLUMN quantidadediasuteis integer;

----- Renato Borges 22/06/2018 12:52 7.0.0 -----
ALTER TABLE public.eventofolhapagamentoitem RENAME eventofolha TO eventofolhapagamentoitem;

----- Hyllner Valadares da Silva 21/06/2018 08:56 7.0.0 -----
ALTER TABLE public.contracheque ADD COLUMN basecalculoinss13 numeric(12,2);
ALTER TABLE public.contracheque ADD COLUMN basecalculoirrf13 numeric(12,2);
ALTER TABLE public.contracheque ADD COLUMN basecalculoirrfferias numeric(12,2);
ALTER TABLE public.contracheque ADD COLUMN basefgts numeric(12,2);
ALTER TABLE public.contracheque ADD COLUMN basefgts13 numeric(12,2);
ALTER TABLE public.contracheque ADD COLUMN fgts13 numeric(12,2);
ALTER TABLE public.contracheque ADD COLUMN pensaofolha numeric(12,2);
ALTER TABLE public.contracheque ADD COLUMN pensaoferias numeric(12,2);
ALTER TABLE public.contracheque ADD COLUMN pensao13sal numeric(12,2);
ALTER TABLE public.contracheque ADD COLUMN pensaoparticip numeric(12,2);
ALTER TABLE public.contracheque ADD COLUMN totalbase numeric(12,2);
----- Hyllner Valadares da Silva 05/07/2018 16:16 7.0.0 -----
ALTER TABLE public.contrachequeevento ADD COLUMN basecalculo numeric(12,2);
	
----- Gilberto Nery 03/07/2018 14:49 7.0.0 -----
ALTER TABLE eventofolhapagamento add column categoria varchar(30);
----- Renato Borges 22/06/2018 12:52 7.0.0 -----

ALTER TABLE public.eventovaletransportefuncionariocargo ADD COLUMN numeroviagensmeioexpediente integer;
ALTER TABLE public.eventovaletransportefuncionariocargo DROP COLUMN diasuteisproximomes;

----- Renato Borges 03/07/2018 10:15 7.0.0 -----

ALTER TABLE public.linhatransporte RENAME TO parametrovaletransporte;
ALTER TABLE public.parametrovaletransporte DROP COLUMN dataultimaalteracao;
ALTER TABLE public.parametrovaletransporte DROP COLUMN usuarioultimaalteracao;
ALTER TABLE public.parametrovaletransporte ADD COLUMN iniciovigencia timestamp without time zone;
ALTER TABLE public.parametrovaletransporte ADD COLUMN fimvigencia timestamp without time zone;

ALTER TABLE public.eventovaletransportefuncionariocargo RENAME linhatransporte TO parametrovaletransporte;
ALTER TABLE public.eventovaletransportefuncionariocargo ADD CONSTRAINT parametrovaletransporte_fkey FOREIGN KEY (parametrovaletransporte)
    REFERENCES public.parametrovaletransporte (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
    
CREATE TABLE public.parametrovaletransportehistorico (
    codigo serial NOT NULL,
    valor numeric(20, 2) NOT NULL,
    iniciovigencia timestamp without time zone,
    fimvigencia timestamp without time zone,
    dataalteracao timestamp without time zone,
    usuarioresponsavelalteracao integer NOT NULL,
    parametrovaletransporte integer NOT NULL,
    descricao character varying(50),
    tipotarifa character varying(50),
    tipolinhatransporte character varying(50),
    PRIMARY KEY (codigo),
    CONSTRAINT usuarioresponsavel_fkey FOREIGN KEY (usuarioresponsavelalteracao)
        REFERENCES public.usuario (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT parametrovaletrasnporte_fkey FOREIGN KEY (parametrovaletransporte)
        REFERENCES public.parametrovaletransporte (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
);

----- Gilberto Nery 03/07/2018 16:26 7.0.0 -----
ALTER TABLE eventofolhapagamento add column incideAdicionalTempoServico boolean null;
ALTER TABLE eventofolhapagamento add column incideAssociacaoSindicato boolean null;


----- Gilberto Nery 05/07/2018 11:00 7.0.0 -----
alter table EventoValeTransporteFuncionarioCargo add column quantidadeViagensMeioExpediente integer;

----- Renato Borges 04/07/2018 11:17 7.0.0 -----
CREATE TABLE public.faltasfuncionario (
    codigo serial NOT NULL,
    funcionariocargo integer NOT NULL,
    datainicio timestamp without time zone NOT NULL,
    datafim timestamp without time zone NOT NULL,
    tipofalta character varying(50) NOT NULL,
    motivo character varying(255),
    integral boolean,
    PRIMARY KEY (codigo),
    CONSTRAINT funcionariocargo_fkey FOREIGN KEY (funcionariocargo) REFERENCES public.funcionariocargo (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS = FALSE);
ALTER TABLE public.faltasfuncionario OWNER to postgres;


----- Gilberto Nery 10/07/2018 14:38 7.0.0 -----
ALTER TABLE public.eventofolhapagamento	ADD COLUMN agrupamentoFolhaNormal boolean;
ALTER TABLE public.eventofolhapagamento	ADD COLUMN agrupamentoFerias boolean;
ALTER TABLE public.eventofolhapagamento	ADD COLUMN agrupamentoFeriasProporcionais boolean;
ALTER TABLE public.eventofolhapagamento	ADD COLUMN agrupamentoFeriasVencidas boolean;
ALTER TABLE public.eventofolhapagamento	ADD COLUMN agrupamentoDecimoTerceiro boolean;
ALTER TABLE public.eventofolhapagamento	ADD COLUMN agrupamentoDecimoTerceiroProporcionais boolean;

CREATE TABLE public.eventofolhapagamentomedia (
    codigo serial NOT NULL,
    grupo character varying(30) NOT NULL,
    tipoEventoMedia character varying(50) NOT NULL,
    eventoFolhaPagamento integer NOT NULL,
    PRIMARY KEY (codigo),
    CONSTRAINT eventofolhapagamentomedia_eventofolhapagamentofkey FOREIGN KEY (eventoFolhaPagamento) REFERENCES public.eventoFolhaPagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS = FALSE);


----- Gilberto Nery 12/07/2018 09:19 7.0.0 -----

CREATE TABLE public.periodoaquisitivoferias
(
	codigo serial NOT NULL,
	funcionariocargo integer NOT NULL,
	inicioPeriodo timestamp without time zone NOT NULL,
	finalPeriodo timestamp without time zone NOT NULL,
	situacao VARCHAR(30) NOT NULL,
	PRIMARY KEY (codigo),
	CONSTRAINT periodoaquisitivoferias_funcionariocargo_fkey FOREIGN KEY (funcionariocargo) REFERENCES funcionariocargo (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
	OIDS = FALSE
);
ALTER TABLE public.periodoaquisitivoferias
	OWNER to postgres;

----- Hyllner Valadares da Silva 12/07/2018 16:51 7.0.0 -----

CREATE TABLE public.sindicato
(
    codigo serial NOT NULL,
    parceiro integer NOT NULL,
    tipoentidadesocial character varying(50) COLLATE pg_catalog."default",
    percentualDescontoVT numeric(10,2),
    eventoAdiantamentoFerias integer,
    eventoDescontoAdiantamentoFerias integer,
    eventoLancamentoFalta integer,
    eventoDSRPerdida integer,
    CONSTRAINT sindicato_pkey PRIMARY KEY (codigo),
    CONSTRAINT sindicato_eventoadiantamento_fkey FOREIGN KEY (eventoAdiantamentoFerias)
        REFERENCES public.eventofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT sindicato_eventodesconto_fkey FOREIGN KEY (eventoDescontoAdiantamentoFerias)
        REFERENCES public.eventofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT sindicato_eventolancamentofalta_fkey FOREIGN KEY (eventoLancamentoFalta)
        REFERENCES public.eventofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT sindicato_eventolancamentodsrperdida_fkey FOREIGN KEY (eventoDSRPerdida)
        REFERENCES public.eventofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT sindicato_parceiro_fkey FOREIGN KEY (parceiro)
        REFERENCES public.parceiro (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.sindicato
    OWNER to postgres;

  ----- Hyllner Valadares da Silva 13/07/2018 17:00 7.0.0 -----

CREATE TABLE public.sindicatomediaferias
(
    codigo serial NOT NULL,
    grupo character varying(30) COLLATE pg_catalog."default",
    eventomedia integer,
    sindicato integer,
    CONSTRAINT sindicatomediaferias_pkey PRIMARY KEY (codigo),
    CONSTRAINT sindicatomediaferias_eventomedia_fkey FOREIGN KEY (eventomedia)
        REFERENCES public.eventofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT sindicatomediaferias_sindicato_fkey FOREIGN KEY (sindicato)
        REFERENCES public.sindicato (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.sindicatomediaferias OWNER to postgres;

----- Hyllner Valadares da Silva 16/07/2018 16:25 7.0.0 -----
ALTER TABLE public.eventofolhapagamento	ADD COLUMN agrupamentoRescisao boolean;

  ----- Hyllner Valadares da Silva 17/07/2018 11:57 7.0.0 -----
ALTER TABLE public.funcionariocargo	ADD COLUMN sindicato integer;
ALTER TABLE public.funcionariocargo ADD CONSTRAINT fk_funcionariocargo_sindicato FOREIGN KEY (sindicato) REFERENCES public.sindicato (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;


----- Gilberto Nery 18/07/2018 09:06 7.0.0 -----

CREATE TABLE public.marcacaoferias
(
	codigo serial NOT NULL,
	periodoAquisitivoFerias integer NOT NULL,
	funcionarioCargo integer NOT NULL,
	dataInicioGozo timestamp without time zone NOT NULL,
	dataFinalGozo timestamp without time zone NOT NULL,
	qtdDias integer NOT NULL,
	abono boolean,
	qtdDiasAbono integer,
	pagarParcela13 boolean,
	dataPagamento timestamp without time zone NOT NULL,
	dataInicioAviso timestamp without time zone NOT NULL,
	situacaoMarcacao VARCHAR(30) NOT NULL,
	PRIMARY KEY (codigo),
	CONSTRAINT marcacaoferias_periodoAquisitivoFerias_fkey FOREIGN KEY (periodoAquisitivoFerias) REFERENCES periodoAquisitivoFerias (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT marcacaoferias_funcionariocargo_fkey FOREIGN KEY (funcionariocargo) REFERENCES funcionariocargo (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
	OIDS = FALSE
);
ALTER TABLE public.marcacaoferias OWNER to postgres;    
	
	
CREATE TABLE public.reciboferias
(
	codigo serial NOT NULL,
	marcacaoFerias integer NOT NULL,
	funcionarioCargo integer NULL,
	totalprovento numeric(20,2) NOT NULL,
	totaldesconto numeric(20,2) NOT NULL,
	totalreceber numeric(20,2) NOT NULL,
	tiporecebimento character varying(50),
	salario numeric(12, 2),
	previdencia character varying(50),
	optantetotal BOOLEAN,
	PRIMARY KEY (codigo),
	CONSTRAINT reciboferias_marcacaoFerias_fkey FOREIGN KEY (marcacaoFerias) REFERENCES marcacaoFerias (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT reciboferias_funcionariocargo_fkey FOREIGN KEY (funcionarioCargo) REFERENCES funcionarioCargo (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
	OIDS = FALSE
);
ALTER TABLE public.reciboferias OWNER to postgres;	
	
	
CREATE TABLE public.reciboferiasevento
(
	codigo serial NOT NULL,
	recibo integer NOT NULL,
	evento integer NOT NULL,
	provento numeric(20,2) NOT NULL,
	desconto numeric(20,2) NOT NULL,
	baseCalculo numeric(20,2) NOT NULL,
	referencia character varying(50),
	PRIMARY KEY (codigo),
	CONSTRAINT reciboferiasevento_recibo_fkey FOREIGN KEY (recibo) REFERENCES reciboferias (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
	CONSTRAINT reciboferiasevento_evento_fkey FOREIGN KEY (evento) REFERENCES eventofolhapagamento (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (
	OIDS = FALSE
);
ALTER TABLE public.reciboferiasevento OWNER to postgres;	


----- Hyllner Valadares da Silva 18/07/2018 16:52 7.0.0 -----
CREATE TABLE public.historicoafastamento
(
    codigo serial NOT NULL,
	funcionariocargo integer NOT NULL,
	datainicio timestamp without time zone,
	datafinal timestamp without time zone,
	datarequerimento timestamp without time zone,
	quantidadedias integer,
	tipoafastamento character varying(50) COLLATE pg_catalog."default" NOT NULL,
    motivoafastamento character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT historicoafastamento_pkey PRIMARY KEY (codigo),
    CONSTRAINT historicoafastamento_funcionariocargo_fkey FOREIGN KEY (funcionariocargo)
        REFERENCES public.funcionariocargo (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

  ----- Hyllner Valadares da Silva 18/07/2018 17:42 7.0.0 -----
CREATE TABLE public.historicosituacao
(
    codigo serial NOT NULL,
	funcionariocargo integer NOT NULL,
	datamudanca timestamp without time zone,
    motivomudanca character varying(50) COLLATE pg_catalog."default" NOT NULL,
	situacao character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT historicosituacao_pkey PRIMARY KEY (codigo),
    CONSTRAINT historicosituacao_funcionariocargo_fkey FOREIGN KEY (funcionariocargo)
        REFERENCES public.funcionariocargo (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

  ----- Hyllner Valadares da Silva 19/07/2018 10:36 7.0.0 -----
CREATE TABLE public.historicosecao
(
    codigo serial NOT NULL,
	funcionariocargo integer NOT NULL,
	datamudanca timestamp without time zone,
	secao integer NOT NULL,
    motivomudanca character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT historicosecao_pkey PRIMARY KEY (codigo),
    CONSTRAINT historicosecao_funcionariocargo_fkey FOREIGN KEY (funcionariocargo)
        REFERENCES public.funcionariocargo (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
	CONSTRAINT historicosecao_secao_fkey FOREIGN KEY (secao)
        REFERENCES public.secaofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

  ----- Hyllner Valadares da Silva 19/07/2018 11:55 7.0.0 -----
CREATE TABLE public.historicosalarial
(
    codigo serial NOT NULL,
	funcionariocargo integer NOT NULL,
	datamudanca timestamp without time zone,
	jornada integer,
	salario numeric(20,2),
    	motivomudanca character varying(50) COLLATE pg_catalog."default" NOT NULL,
	percentualvariacaosalarial numeric(15,2),
	salariohora numeric(20,2),
    CONSTRAINT historicosalarial_pkey PRIMARY KEY (codigo),
    CONSTRAINT historicosalarial_funcionariocargo_fkey FOREIGN KEY (funcionariocargo)
        REFERENCES public.funcionariocargo (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

  ----- Hyllner Valadares da Silva 19/07/2018 15:55 7.0.0 -----
CREATE TABLE public.historicofuncao
(
    codigo serial NOT NULL,
	funcionariocargo integer NOT NULL,
	datamudanca timestamp without time zone,
	motivomudanca character varying(50) COLLATE pg_catalog."default" NOT NULL,
	funcao integer NOT NULL,
	nivelsalarial integer,
	faixasalarial integer,
    CONSTRAINT historicofuncao_pkey PRIMARY KEY (codigo),
    CONSTRAINT historicofuncao_funcionariocargo_fkey FOREIGN KEY (funcionariocargo)
        REFERENCES public.funcionariocargo (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

  ----- Hyllner Valadares da Silva 19/07/2018 16:15 7.0.0 -----
CREATE TABLE public.historicodependentes
(
    codigo serial NOT NULL,
	funcionariocargo integer NOT NULL,
	datamudanca timestamp without time zone,
	numerodependentesirrf integer NOT NULL,
	numerodependentessalariofamilia integer NOT NULL,
    CONSTRAINT historicofdependentes_pkey PRIMARY KEY (codigo),
    CONSTRAINT historicodependentes_funcionariocargo_fkey FOREIGN KEY (funcionariocargo)
        REFERENCES public.funcionariocargo (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

  ----- Hyllner Valadares da Silva 20/07/2018 14:05 7.0.0 -----
ALTER TABLE public.marcacaoferias add column justificativa varchar(30);
ALTER TABLE public.reciboferiasevento ADD COLUMN informadomanual BOOLEAN;

----- Renato Borges 24/07/2018 09:40 7.0.0 -----
ALTER TABLE public.periodoaquisitivoferias ADD COLUMN informacoesadionais character varying(255);

----- Gilberto Nery 25/07/2018 17:09 7.0.0 -----
ALTER TABLE public.marcacaoferias add column lancadoAdiantamento boolean;
ALTER TABLE public.marcacaoferias add column lancadoDescontoAdiantamento boolean;


----- Gilberto Nery 26/07/2018 15:37 7.0.0 -----
ALTER TABLE public.templatelancamentofolhapagamento add column lancarFerias boolean;
ALTER TABLE public.templatelancamentofolhapagamento add column lancarAdiantamentoFerias boolean;

ALTER TABLE public.contracheque ADD COLUMN valorIRRFFerias numeric(12, 2);

---------- Pedro Andrade 02/07/2018 14:40 6.27.0 ------------------
alter table departamento add column controlaestoque boolean default false;

----- Renato Borges 01/08/2018 17:41 7.0.0 -----
ALTER TABLE public.funcionariodependente DROP COLUMN formulacalculo;

----- Hyllner Valadares da Silva 03/08/2018 16:00 7.0.0 -----
ALTER TABLE public.eventofolhapagamento DROP COLUMN agrupamentorescisao;

----- Leandro 08/08/2018 16:00 7.0.1 -----
alter table unidadeensino add column informacoesAdicionaisEndereco varchar(250);

------------ Gilberto Nery 09/08/2018 10:54 7.0.1 ------------
alter table faltasfuncionario add column debitado boolean null;


------------ Gilberto Nery 09/08/2018 16:33 7.0.1 ------------
alter table sindicato add column validarFaltas boolean null;
alter table sindicato add column considerarFaltasFerias boolean null;
alter table sindicato add column eventoDevolucaoFalta integer null;
alter table sindicato add CONSTRAINT sindicato_eventoDevolucaoFalta_fkey FOREIGN KEY (eventoDevolucaoFalta)
        REFERENCES public.eventofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;
		
		
------------- Rodrigo Wind 17/07/2018 09:34 ------------
alter table disciplina add column descricaoComplementar varchar(100) default '';

--------------- Rodrigo Wind 13/08/2018 -----------------------
alter table perfilacesso add column possuiAcessoRh boolean default false;
alter table perfilacesso add column possuiAcessoContabil boolean default false;

update perfilacesso set possuiacessorh = true  where possuiacessorh = false
and exists (
	select permissao.codperfilacesso from permissao  where codperfilacesso = perfilacesso.codigo
	and nomeentidade in (
		'CompetenciaFolhaPagamento', 'SecaoFolhaPagamento', 'GrupoLancamentoFolhaPagamento', 'LancamentoFolhaPagamento', 
		'ParametroValeTransporte', 'FormulaFolhaPagamento','FaltasFuncionario','SalarioComposto', 
		'EventoFolhaPagamento', 'EventoValeTransporteFuncionarioCargo', 'EventoFixoCargoFuncionario',
		'EventoEmprestimoCargoFuncionario', 'ValorReferenciaFolhaPagamento', 'TipoTransporte', 
		'NivelSalarial', 'FaixaSalarial', 'ProgressaoSalarial', 'FichaFinanceira', 
		'TipoEmprestimo', 'FichaFinanceiraRel', 'PeriodoAquisitivoFerias', 'MarcacaoFerias', 'Sindicato'		
	) limit 1
) ;

update perfilacesso set possuiAcessoContabil = true  where possuiAcessoContabil = false
and exists (
	select permissao.codperfilacesso from permissao  where codperfilacesso = perfilacesso.codigo
	and nomeentidade in (
		'PlanoConta', 'LayoutIntegracao', 'IntegracaoContabil', 'FechamentoMes', 'ConfiguracaoContabil'
	) limit 1
) ;


------------ Gilberto Nery 13/08/2018 11:43 7.0.1 ------------		
alter table periodoaquisitivoferias add column qtdFalta integer null;

------------ Thyago Jayme 13-08-2018  15:25 ------------


CREATE TABLE horarioturmadiaitemlog
(
  codigo serial NOT NULL,
  nraula integer ,
  duracaoaula integer ,
  horario character varying(100),
  horarioinicio character varying(100),
  horariotermino character varying(100),
  professor integer,
  disciplina integer,
  sala integer,
  data date,
  horarioturmadia integer NOT NULL,
  dataultimaalteracao timestamp without time zone,
  turma integer,
  CONSTRAINT horarioturmadiaitemlog_pkey PRIMARY KEY (codigo)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE horarioturmadiaitemlog
  OWNER TO postgres;
GRANT ALL ON TABLE horarioturmadiaitem TO postgres;
GRANT SELECT ON TABLE horarioturmadiaitem TO willian;
GRANT SELECT ON TABLE horarioturmadiaitem TO ozanoneto;

insert into horarioturmadiaitemlog (turma, nraula, duracaoaula, horario, horarioinicio, horariotermino, professor, disciplina, sala, data, horarioturmadia, dataultimaalteracao)
select horarioturma.turma, horarioturmadiaitem.nraula, horarioturmadiaitem.duracaoaula, horarioturmadiaitem.horario, horarioturmadiaitem.horarioinicio, 
horarioturmadiaitem.horariotermino, horarioturmadiaitem.professor, horarioturmadiaitem.disciplina, horarioturmadiaitem.sala, horarioturmadiaitem.data, horarioturmadiaitem.horarioturmadia, current_date from horarioturmadiaitem  
inner join horarioturmadia on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia
inner join horarioturma on horarioturma.codigo = horarioturmadia.horarioturma
left join horarioturmadiaitemlog on horarioturmadiaitemlog.turma = horarioturma.turma and horarioturmadiaitemlog.professor = horarioturmadiaitem.professor 
and horarioturmadiaitemlog.disciplina = horarioturmadiaitem.disciplina
where horarioturmadiaitemlog.codigo is null;

----- Renato Borges 13/08/2018 15:48 7.0.1 -----

CREATE TABLE public.marcacaoferiascoletivas(
	codigo serial NOT NULL,
    datafechamento timestamp without time zone,
	datainiciogozo timestamp without time zone,
	datafinalgozo timestamp without time zone,
	datapagamento timestamp without time zone,
	datainicioaviso timestamp without time zone,
	quantidadediasabono integer,
	quantidadedias integer,
	abono boolean,
	pagarprimeiraparcela13 boolean,
	encerrarperiodoaquisitivo boolean,
	templatelancamentofolha integer,
	informacoesadicionais text,
	situacao character varying (50),
	descricao character varying (100),
    PRIMARY KEY (codigo),
    CONSTRAINT templatelancamentofolha_fkey FOREIGN KEY (templatelancamentofolha) REFERENCES public.templatelancamentofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION )
WITH (OIDS = FALSE); 
ALTER TABLE public.marcacaoferiascoletivas OWNER to postgres;


CREATE TABLE public.historicomarcacaoferiascoletivas (
    codigo serial NOT NULL,
    funcionariocargo integer NOT NULL,
    marcacaoferias integer NOT NULL,
    marcacaoferiascoletivas integer NOT NULL,
	nome character varying (50),
	cargo character varying (50),
	situacao character varying (50),
	formacontratacao character varying (50),
	matriculacargo character varying (50),
	datahistorico timestamp without time zone,
    PRIMARY KEY (codigo),
    CONSTRAINT funcionariocargon_fkey FOREIGN KEY (funcionariocargo) 
		REFERENCES public.funcionariocargo (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT marcacaoferias_fkey FOREIGN KEY (marcacaoferias) 
		REFERENCES public.marcacaoferias (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT marcacaoferiascoletiva_fkey FOREIGN KEY (marcacaoferiascoletivas)
        REFERENCES public.marcacaoferiascoletivas (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS = FALSE);
ALTER TABLE public.historicomarcacaoferiascoletivas OWNER to postgres;


---- Pedro 14/08/2018 10:10 ----
ALTER TABLE notafiscalentrada DROP CONSTRAINT unique_notafiscalentrada_numero_fornecedor;
ALTER TABLE notafiscalentrada ADD CONSTRAINT unique_notafiscalentrada_numero_serie_fornecedor UNIQUE(numero, serie, fornecedor);



---------- Rodrigo 14/08/2018 11:12 7.0.1 -----------------------
update funcionariocargo  set departamento = cargo.departamento from cargo
where cargo.codigo = funcionariocargo.cargo and funcionariocargo.departamento is null;

update funcionariocargo  set situacaofuncionario   =  case when ativo then 'ATIVO' else 'DEMITIDO' end
where funcionariocargo.situacaofuncionario is null;

update funcionariocargo set matriculacargo = t.matriculacargo from (
select funcionariocargo.codigo,  funcionario.matricula||'-'|| row_number() over(partition by funcionario.codigo 
order by funcionario.codigo, funcionario.dataadmissao,  funcionariocargo.cargo) as matriculacargo from funcionariocargo
inner join funcionario on funcionario.codigo = funcionariocargo.funcionario
where (funcionariocargo.matriculacargo is null or length(funcionariocargo.matriculacargo) = 0)
) as t where t.codigo = funcionariocargo.codigo;


---- Pedro 15/08/2018 09:50----------------------------- ----
alter table movimentacaoestoque add column unidadeensinodestino integer;
alter table movimentacaoestoque add constraint fk_movimentacaoestoque_unidadeensinodestino foreign key (unidadeensinodestino) references unidadeensino (codigo) match simple on update restrict on delete restrict;

alter table movimentacaoestoque add column centroresultadoestoque integer;
alter table movimentacaoestoque add constraint fk_movimentacaoestoque_centroresultadoestoque foreign key (centroresultadoestoque) references centroresultado (codigo) match simple on update restrict on delete restrict;

alter table movimentacaoestoque add column centroresultadoestoquedestino integer;
alter table movimentacaoestoque add constraint fk_movimentacaoestoque_centroresultadoestoquedestino foreign key (centroresultadoestoquedestino) references centroresultado (codigo) match simple on update restrict on delete restrict;

delete from estoque where quantidade = 0 and precounitario = 0.0;
ALTER TABLE estoque ALTER COLUMN precounitario SET NOT NULL;
ALTER TABLE estoque ADD CONSTRAINT check_precounitario_maior_zero CHECK (precounitario>0.0);

----------- Rodrigo 16/08/2018 08:37 7.0.1 ----------
ALTER TABLE public.periodoaquisitivoferias drop COLUMN informacoesadionais;
ALTER TABLE public.periodoaquisitivoferias ADD COLUMN informacoesadicionais character varying(255);

---------- Gilberto Nery 15/08/2018 tronco integracao amazon s3 ------------------
alter table configuracaogeralsistema add column nomerepositorio varchar(100);


---------- Gilberto Nery 22/08/2018 RH ------------------
-- Exclui o vinculo com marcacao de ferias deixando o vinculo fraco
-- Medida visa performance devido a necessidade de excluir a marcacao de feiras antes de excluir o historico
ALTER TABLE public.historicomarcacaoferiascoletivas DROP CONSTRAINT marcacaoferias_fkey;

ALTER TABLE public.historicomarcacaoferiascoletivas ADD COLUMN situacaoMarcacaoFerias varchar(30);
ALTER TABLE public.historicomarcacaoferiascoletivas ADD COLUMN lancadoAdiantamento boolean;
ALTER TABLE public.historicomarcacaoferiascoletivas ADD COLUMN lancadoReciboNoContraCheque boolean;

alter table historicomarcacaoferiascoletivas alter column marcacaoferiascoletivas drop not null;

ALTER TABLE historicomarcacaoferiascoletivas RENAME TO controlemarcacaoferias;


---- Alessandro 23/08/2018 7.0.1 ----
CREATE INDEX ch_contareceber_tipoorigem ON contareceber USING btree (tipoorigem);
create type nfeipog as (cpf text, tomador text, cidade text, uf text, im text, ie text, tipoendereco text, endereco text, numero text, complemento text,
	cep text, tipobairro text, bairro text, ddd text, telefone text, email text, cnpj text, codnota bigint, idservico text, idemissao date, referencia text, curso text,
	responsavelfinanceiro text, valor numeric(20,2));
create or replace function emitirnotafiscalipogpelaunidadeensino(unidade integer, inicio date, fim date, persistir boolean) returns setof nfeipog as $$
declare
	r nfeipog;
	ultimo_numero_enviado integer := (select cnf.numeronota from configuracaonotafiscal cnf inner join unidadeensino ue on ue.configuracaonotafiscal = cnf.codigo and ue.codigo = unidade);
	ultimo_numero_gerado integer;
begin
	for r in
		select p.cpf::text, p.nome::text tomador, cid.nome::text cidade, e.sigla::text uf, ''::text im, ''::text ie, substr(p.endereco||' ',1, position(' ' in p.endereco||' '))::text tipoendereco,
			p.endereco::text, p.numero::text, p.complemento::text, p.cep::text, substr(p.setor||' ',1, position(' ' in p.setor||' '))::text tipobairro, p.setor::text bairro, ''::text ddd,
			(case when trim(p.celular) <> '' then p.celular when trim(p.telefoneres) <> '' then p.telefoneres when (p.telefonerecado) <> '' then p.telefonerecado when (p.telefonecomer) <> '' then p.telefonecomer when (p.celularfiador) <> '' then p.celularfiador when (p.telefonefiador) <> '' then p.telefonefiador else '' end)::text as telefone,
			(case when trim(p.email) <> '' then p.email when trim(p.email2) <> '' then p.email2 else '' end)::text as emailtomador,
			t.cnpj::text, (row_number() over (order by p.cpf) + ultimo_numero_enviado)::bigint codnota, 'SERV_EDU_SUP'::text idservico, current_date idemissao,
			(with atual as (select extract(year from current_date) ano, extract(month from current_date) mes)
			select atual.ano||'/'||case when atual.mes = 1 then 'JANEIRO' when atual.mes = 2 then 'FEVEREIRO' when atual.mes = 3 then 'MAR�O' when atual.mes = 4 then 'ABRIL' when atual.mes = 5 then 'MAIO' when atual.mes = 6 then 'JUNHO' when atual.mes = 7 then 'JULHO' when atual.mes = 8 then 'AGOSTO' when atual.mes = 9 then 'SETEMBRO' when atual.mes = 10 then 'OUTUBRO' when atual.mes = 11 then 'NOVEMBRO' when atual.mes = 12 then 'DEZEMBRO'	else '' end from atual)::text referencia,
			t.curso::text, rf.nome::text responsavelfinanceiro, sum(t.crr_valorRecebimento)::numeric(20,2) as valor --, min(t.crr_contaReceber)
		from (select distinct cr.codigo as contareceber, cr.situacao as contareceber_situacao, crr.formaPagamento as crr_formaPagamento, 
			crr.contaReceber as crr_contaReceber, cr.matriculaAluno as matricula, cr.tipoPessoa as tipopessoa,
			cr.pessoa, cr.responsavelfinanceiro, crr.formaPagamentoNegociacaoRecebimento as crr_formaPagamentoNegociacaoRecebimento,
			crr.dataRecebimento as crr_dataRecebimento, crr.responsavel as crr_responsavel, crr.motivo as crr_motivo,
			(case when fp.tipo = 'CH' and ch.pago = true then crr.valorrecebimento when fp.tipo = 'CA' then fpnrcc.valorparcela::numeric(20,2) when fp.tipo != 'CH' and fp.tipo != 'CA' then crr.valorrecebimento end) as crr_valorRecebimento, (case when fp.tipo = 'CH' and ch.pago = true then ch.dataprevisao::Date when fp.tipo = 'CA' then cr.datavencimento::Date + ('' || cfc.diabasecreditoconta::varchar || 'days''')::interval when fp.tipo = 'BO' then  (case when fpnr.datacredito is null then  nr.data::Date else fpnr.datacredito::DATE end)  when fp.tipo != 'CH' and fp.tipo != 'CA' and fp.tipo != 'BO' then  nr.data::Date end) as dataRecebimento,
			ue.cnpj, curso.nome curso
			from contareceberrecebimento crr
			inner join contareceber cr on cr.codigo = crr.contareceber
			inner join contarecebernegociacaorecebimento crnr on cr.codigo = crnr.contareceber
			inner join negociacaorecebimento nr on nr.codigo = crnr.negociacaorecebimento
			inner join formapagamentonegociacaorecebimento fpnr on fpnr.codigo = crr.formapagamentonegociacaorecebimento
			inner join formapagamento fp on fp.codigo = fpnr.formapagamento
			left join cheque ch on ch.codigo = fpnr.cheque
			left join formapagamentonegociacaorecebimentocartaocredito fpnrcc on fpnrcc.formapagamentonegociacaorecebimento = fpnr.codigo
			left join operadoracartao oc on oc.codigo = fpnr.operadoracartao
			left join unidadeensino ue on ue.codigo = cr.unidadeensino
			left join configuracaofinanceiro cf on cf.configuracoes = ue.configuracoes
			left join configuracaofinanceirocartao cfc on cfc.configuracaofinanceiro = cf.codigo  and cfc.operadoracartao = oc.codigo
			left join matriculaperiodo mp on mp.codigo = cr.matriculaperiodo
			left join turma on turma.codigo = mp.turma
			left join curso on curso.codigo = turma.curso
			where cr.tipoOrigem = 'MEN' and cr.situacao = 'RE'
			and cr.unidadeEnsino = unidade
		) as t
		inner join pessoa p on p.codigo = t.pessoa
		left join pessoa rf on rf.codigo = t.responsavelfinanceiro
		left join cidade cid on cid.codigo = p.cidade
		left join estado e on e.codigo = cid.estado
		where t.crr_valorRecebimento > 0
		and t.dataRecebimento between inicio and fim
		group by p.cpf, p.nome, cid.nome, e.sigla, im, ie, tipoendereco, p.endereco, p.numero, p.complemento, p.cep, tipobairro, p.setor, ddd, telefone, emailtomador, t.cnpj, t.curso, rf.nome
		order by p.cpf
	loop
		ultimo_numero_gerado := r.codnota;
		return next r;
	end loop;
	update configuracaonotafiscal set numeronota = ultimo_numero_gerado where persistir;
	return;
end
$$ language plpgsql;
create or replace function emitirnotafiscalipog(unidades integer[], inicio date, fim date, persistir boolean) returns setof nfeipog as $$
declare
	u integer;
	r nfeipog;
begin
	foreach u in array unidades loop
		for r in select * from emitirnotafiscalipogpelaunidadeensino(u, inicio, fim, persistir) loop
			return next r;
		end loop;
	end loop;
	return;
end
$$ language plpgsql;



----------- Rodrigo Wind 7.0.1 24/08/2018 15:08 --------------------------
create table turmacontrato (
 codigo serial,
 turma int not null,
 textopadrao int not null,
 tipocontratomatricula varchar(20) not null default 'NORMAL',
 padrao boolean not null default false,

 constraint pk_turmacontrato_codigo  primary key (codigo),
 constraint fk_turmacontrato_turma foreign key (turma) references turma(codigo),
 constraint fk_turmacontrato_textopadrao foreign key (textopadrao) references textopadrao(codigo)

);

create unique index unq_turmacontrato_turma_tipo_padrao on turmacontrato (turma, tipocontratomatricula, padrao) where padrao = true;
create index idx_turmacontrato_turma on turmacontrato(turma);
create index idx_turmacontrato_turma_tipo on turmacontrato(turma, tipocontratomatricula);

-------------------
ALTER TABLE public.funcionariocargo ADD COLUMN salariocargoatual NUMERIC(20,2);

----- Renato Borges 24/08/2018 11:08 7.0.1 -----

CREATE TABLE public.controlelancamentofolhapagamento(
    codigo serial NOT NULL,
    lancamentofolhapagamento integer NOT NULL,
    contracheque integer NOT NULL,
    funcionariocargo integer NOT NULL,
    PRIMARY KEY (codigo),
    CONSTRAINT lancamentofolhapagamento_fkey FOREIGN KEY (lancamentofolhapagamento)
        REFERENCES public.lancamentofolhapagamento (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT contracheque_fkey FOREIGN KEY (contracheque)
        REFERENCES public.contracheque (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT funcionariocargo_fkey FOREIGN KEY (funcionariocargo)
        REFERENCES public.funcionariocargo (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
) WITH (OIDS = FALSE);
ALTER TABLE public.controlelancamentofolhapagamento OWNER to postgres;

INSERT INTO public.valorreferenciafolhapagamento(
	identificador, descricao, datainiciovigencia, datafimvigencia, imposto, 
	valorfixo, valor, referencia, atualizarfinalvigencia, situacao, sql, tipovalorreferencia)
	VALUES ('EVT_FIXO', 'Evento Fixo', '2018-08-01', '2019-01-01', null,
			false, 0.00, 'OUTRAS_FINALIDADES', false, 'ATIVO', 'select * from eventofixocargofuncionario as eventofixo
inner join funcionariocargo as funcionario on eventofixo.funcionariocargo = funcionario.codigo
inner join eventofolhapagamento as evento  on eventofixo.eventofolhapagamento = evento.codigo
where funcionario.matriculacargo = ? and evento.identificador = ?;', 'SQL');


ALTER TABLE public.controlemarcacaoferias
    ADD COLUMN adiantamentoferias integer;

ALTER TABLE public.controlemarcacaoferias ADD COLUMN reciboferias integer;
ALTER TABLE public.controlemarcacaoferias ADD CONSTRAINT adiantamentoferias_fkey FOREIGN KEY (adiantamentoferias)
    REFERENCES public.contracheque (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE public.controlemarcacaoferias ADD CONSTRAINT reciboferias_fkey FOREIGN KEY (reciboferias)
    REFERENCES public.contracheque (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
	
ALTER TABLE public.contracheque ADD COLUMN templatelancamentofolhapagamento integer;
ALTER TABLE public.contracheque ADD CONSTRAINT templatelancamentofolhapagamento_fkey FOREIGN KEY (templatelancamentofolhapagamento)
    REFERENCES public.templatelancamentofolhapagamento (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

----- Renato Borges 28/08/2018 11:17 7.0.1 -----
ALTER TABLE public.faltasfuncionario DROP COLUMN datafim;

---------- Rodrigo Wind 28/08/2018 19:17 7.0.1 ------
create table fechamentofinanceiro(
	codigo serial, 
	datafechamento timestamp,
	usuariofechamento int,
	descricaoFechamento text,

	constraint pk_fechamentofinanceiro_codigo primary key (codigo),
	constraint fk_fechamentofinanceiro_usuariofechamento foreign key (usuariofechamento) references usuario(codigo)
);

create table fechamentofinanceirocentroresultado(
	codigo serial, 
	tipoCentroResultadoOrigem varchar(50),
	centroResultado int,
	valor numeric(20,2),
	valorContaReceberRecebido numeric(20,2),
	valorContaPagarPago numeric(20,2),
	valorContaReceberNegociado numeric(20,2),
	valorContaReceberCancelado numeric(20,2),
	valorContaReceberAReceber numeric(20,2),
	valorContaPagarAPagar numeric(20,2),
	valorContaPagarNegociado numeric(20,2),
	valorContaPagarCancelado numeric(20,2),	
	tipoMovimentacaoCentroResultadoOrigem varchar(50),
	origemFechamentoFinanceiroCentroResultadoOrigem varchar(50),
	codigoOrigem int,
	

	constraint pk_fechamentofinanceirocentroresultado_codigo primary key (codigo),
	constraint fk_fechfinancentroresultado_centroResultado foreign key (centroResultado) references centroResultado(codigo)
	
);

create table fechamentoFinanceiroConta(

	codigo serial, 	
	tipoOrigemContaReceber varchar(50),
	codigoOrigem varchar(50),
	nossoNumero varchar(100),
	parcela varchar(20),
	situacaoContaReceber varchar(30),
	dataVencimento date,
	dataCompetencia date,
	dataCancelamento date,
	dataRecebimento date,
	dataNegociacao date,
	matriculaPeriodo int,
	pessoa int,
	responsavelFinanceiro int,
	parceiro int,
	fornecedor int,
	unidadeEnsinoFinanceira int,
	unidadeEnsinoAcademica int,
	nomeSacado varchar(300),
	cpfCnpjSacado varchar(25),
	matricula varchar(100),
	fechamentoFinanceiro int,
	valor numeric(20,2),
	tipoPessoa varchar(40),
	origemFechamentoFinanceiroConta varchar(40) default 'CONTA_RECEBER',

	constraint pk_fechFinanConta_codigo primary key (codigo),
	constraint fk_fechFinanConta_fechFinan foreign key (fechamentoFinanceiro) references fechamentoFinanceiro(codigo)
);

create table fechamentoFinanceiroContaCentroResultado(
	codigo serial, 	
	fechamentoFinanceiroCentroResultado int,
	fechamentoFinanceiroConta int,

	constraint pk_fechFinanContaCentroResult_codigo primary key (codigo),
	constraint fk_fechFinanContaCentroResult_fechFinanCentroResultado foreign key (fechamentoFinanceiroCentroResultado) references fechamentoFinanceiroCentroResultado(codigo),
	constraint fk_fechFinanContaCentroResult_fechamentoFinanceiroConta foreign key (fechamentoFinanceiroConta) references fechamentoFinanceiroConta(codigo)
);

create table fechamentoFinanceiroDetalhamentoValor(

	codigo serial, 	
	fechamentoFinanceiroCentroResultado int,
	tipoCentroResultadoOrigemDetalhe VARCHAR(50),
	faixaDescontoProgressivo VARCHAR(20),
	nomeOrigemDoTipoDetalhe VARCHAR(200),
	codOrigemDoTipoDetalhe INT,
	valor NUMERIC(20,2),
	dataLimiteAplicacaoDesconto DATE,
	ordemApresentacao INT,
	utilizado boolean,

	constraint pk_fechFinanDetalhamentoValor_codigo primary key (codigo),
	constraint fk_fechFinanDetalhamentoValor_fechFinanCentroResultado foreign key (fechamentoFinanceiroCentroResultado) references fechamentoFinanceiroCentroResultado(codigo)	
);

create table fechamentoFinanceiroFormaPagamento(

	codigo serial, 	
	fechamentoFinanceiroConta int,
	valor numeric(20,2),
	formaPagamento INT,
	contaCorrente INT,
	dataCompensacao DATE,

	constraint pk_fechFinanFormaPagamento_codigo primary key (codigo),
	constraint fk_fechFinanFormaPagamento_fechamentoFinanceiroConta foreign key (fechamentoFinanceiroConta) references fechamentoFinanceiroConta(codigo)	
);


---------- Pedro Andrade 29/08/2018 08:10 7.0.1 ------
ALTER TABLE centroresultadoorigem ALTER COLUMN categoriadespesa DROP NOT NULL;
alter table centroresultadoorigem add column centroreceita integer;
ALTER TABLE centroresultadoorigem ADD CONSTRAINT fk_centroresultadoorigem_centroreceita FOREIGN KEY (centroreceita) REFERENCES centroreceita (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;


alter table unidadeensino add column centroResultadoRequerimento integer;
ALTER TABLE unidadeensino ADD CONSTRAINT fk_unidadeensino_centroResultadoRequerimento FOREIGN KEY (centroResultadoRequerimento) REFERENCES centroResultado (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;

create table public.unidadeensinotiporequerimentocentroresultado (
    codigo serial not null,
    unidadeensino integer not null,
    centroresultado integer not null,
    tiporequerimento integer not null,
    constraint unidadeensinotiporequerimentocentroresultado_pkey primary key (codigo),
    constraint fk_unidadeensinotiporequerimentocentroresultado_unidadeensino foreign key (unidadeensino)  references public.unidadeensino (codigo) match simple on update no action on delete no action,
    constraint fk_unidadeensinotiporequerimentocentroresultado_centroresultado foreign key (centroresultado)  references public.centroresultado (codigo) match simple on update no action on delete no action,
    constraint fk_unidadeensinotiporequerimentocentroresultado_tiporequerimento foreign key (tiporequerimento)  references public.tiporequerimento (codigo) match simple on update no action on delete no action
)
with (oids = false);
alter table public.unidadeensinotiporequerimentocentroresultado owner to postgres;

create table public.unidadeensinoniveleducacionalcentroresultado (
    codigo serial not null,
    unidadeensino integer not null,
    centroresultado integer not null,
    tiponiveleducacional character varying (50),
    constraint unidadeensinoniveleducacionalcentroresultado_pkey primary key (codigo),
    constraint fk_unidadeensinoniveleducacionalcentroresultado_unidadeensino foreign key (unidadeensino)  references public.unidadeensino (codigo) match simple on update no action on delete no action,
    constraint fk_unidadeensinoniveleducacionalcentroresultado_centroresultado foreign key (centroresultado)  references public.centroresultado (codigo) match simple on update no action on delete no action
)
with (oids = false);
alter table public.unidadeensinoniveleducacionalcentroresultado owner to postgres;


alter table tiporequerimento add column usarCentroResultadoTurma boolean default true;

alter table biblioteca  add column centroResultado integer;
ALTER TABLE biblioteca ADD CONSTRAINT fk_biblioteca_centroResultado FOREIGN KEY (centroResultado) REFERENCES centroResultado (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;

alter table contratosreceitas  add column departamento integer;
ALTER TABLE contratosreceitas ADD CONSTRAINT fk_contratosreceitas_departamento FOREIGN KEY (departamento) REFERENCES departamento (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE centroresultadoorigem ALTER COLUMN centroresultadoadministrativo SET NOT NULL;

CREATE TABLE centroresultadoorigemdetalhe
(
  codigo serial NOT NULL,
  centroresultadoorigem integer NOT NULL,
  tipocentroresultadoorigemdetalhe character varying(50) NOT NULL,
  valor numeric(20,2) NOT NULL,
  datalimiteaplicacaodesconto timestamp without time zone,
  utilizado boolean,
  faixadescontoprogressivo character varying(50) NOT NULL DEFAULT 'NENHUM'::character varying,
  codorigemdotipodetalhe integer,
  ordemapresentacao integer,
  nomeorigemdotipodetalhe character varying(250),
  CONSTRAINT centroresultadoorigemdetalhe_pkey PRIMARY KEY (codigo),
  CONSTRAINT fk_centroresultadoorigemdetalhe_centroresultadoorigem FOREIGN KEY (centroresultadoorigem)
      REFERENCES centroresultadoorigem (codigo) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE CASCADE
)
WITH (
  OIDS=FALSE
);
ALTER TABLE centroresultadoorigemdetalhe
  OWNER TO postgres;

  
	
	
---------------- Rodrigo Wind 29/08/218 10:45 7.0.1 --------------
alter table fechamentofinanceirocentroresultado  add column categoriaDespesa int;
alter table fechamentofinanceirocentroresultado  add constraint fk_fechfinancentroresultado_categoriaDespesa foreign key (categoriaDespesa) references categoriaDespesa(codigo);
alter table fechamentofinanceirocentroresultado  add column centroReceita int;
alter table fechamentofinanceirocentroresultado  add constraint fk_fechfinancentroresultado_centroReceita foreign key (centroReceita) references centroReceita(codigo);


---------- Gilberto Nery 29/08/2018 RH ------------------
alter table TemplateLancamentoFolhaPagamento add column dataHora timestamp without time zone null;
alter table TemplateLancamentoFolhaPagamento add column lancarEventosFolhaNormal boolean null;
alter table public.contracheque drop column periodo;

----- Renato Borges 29/08/2018 15:49 7.0.1 -----
ALTER TABLE public.valorreferenciafolhapagamento ADD COLUMN valorreferenciapadrao boolean DEFAULT false;

------------ Rodrigo Wind 29/08/2018 14:36 7.0.1 -------------
alter table fechamentofinanceiro add column fechamentoMes int;
alter table fechamentofinanceiro add constraint fk_fechamentofinanceiro_fechamentoMes foreign key (fechamentoMes) references fechamentoMes(codigo)  ;
 
 
 -------------- Rodrigo Wind 29/08/2018 29/08/2018 16:35 7.0.1 -------------
alter table centroresultadoorigemdetalhe add column tipovalor varchar(20);
alter table centroresultadoorigemdetalhe add column valortipovalor numeric(20,2);
alter table centroresultadoorigemdetalhe add column origemDetalhamentoConta varchar(30) default 'CONTA_RECEBER';
alter table centroresultadoorigemdetalhe add column codigoorigem int;


update centroresultadoorigemdetalhe set codigoorigem =  centroresultadoorigem.codorigem::INT from centroresultadoorigem
where centroresultadoorigem.codigo = centroresultadoorigemdetalhe.centroresultadoorigem
and centroresultadoorigemdetalhe.codigoorigem is null
and centroresultadoorigem.tipocentroresultadoorigem = 'CONTA_RECEBER';

 alter table centroresultadoorigemdetalhe rename to detalhamentovalorconta;

 -------------- Rodrigo Wind 29/08/2018 29/08/2018 17:38 7.0.1 -------------
alter table planodescontocontareceber alter column tipodesconto set default 'VA';
alter table detalhamentovalorconta alter column tipovalor  set default 'VALOR';

---------- Pedro Andrade 29/08/2018 17:45 7.0.1 ------

 alter table fechamentofinanceiroconta add column codorigemfechamentofinanceiro integer;
 alter table fechamentofinanceiroconta add column funcionario integer;
 alter table fechamentofinanceirocentroresultado add column codorigemfechamentofinanceiro integer;
  
 ALTER TABLE fechamentofinanceirodetalhamentovalor DROP COLUMN fechamentofinanceirocentroresultado;
 ALTER TABLE fechamentofinanceirodetalhamentovalor DROP CONSTRAINT fk_fechfinandetalhamentovalor_fechfinancentroresultado;
  
ALTER TABLE fechamentofinanceirodetalhamentovalor ADD COLUMN fechamentofinanceiroconta integer;
ALTER TABLE fechamentofinanceirodetalhamentovalor ADD CONSTRAINT fk_fechfinandetalhamentovalor_fechamentofinanceiroconta FOREIGN KEY (fechamentofinanceiroconta) REFERENCES fechamentofinanceiroconta (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE detalhamentovalorconta DROP CONSTRAINT fk_centroresultadoorigemdetalhe_centroresultadoorigem;
ALTER TABLE detalhamentovalorconta ALTER COLUMN centroresultadoorigem DROP NOT NULL;
ALTER TABLE detalhamentovalorconta DROP COLUMN centroresultadoorigem;
  

-------------------- Rodrigo Wind 29/08/2018 18:15 7.0.1 ---------------
create index idx_fechamentofinanceiro_fechamentoMes on fechamentofinanceiro(fechamentoMes);
create index idx_fechamentofinanceiroconta_fechamentofinanceiro on fechamentofinanceiroconta(fechamentofinanceiro);
create index idx_fechfinandetalhamentovalor_fechamentofinanceiroconta on fechamentofinanceirodetalhamentovalor(fechamentofinanceiroconta);
create index idx_fechfinandetalhamentovalor_fechamentofinanceiroconta_tipo on fechamentofinanceirodetalhamentovalor(fechamentofinanceiroconta, tipoCentroResultadoOrigemDetalhe);
create index idx_fechamentofinanceirocentroresultado_origem_codorigem on fechamentofinanceirocentroresultado(codigoorigem, origemFechamentoFinanceiroCentroResultadoOrigem);
create index idx_fechamentofinanceiroconta_matricula on fechamentofinanceiroconta(matricula);
create index idx_fechamentofinanceiroconta_situacaocontareceber on fechamentofinanceiroconta(situacaocontareceber);
create index idx_fechfinanformapagamento_fechamentofinanceiroconta on fechamentofinanceiroformapagamento(fechamentofinanceiroconta);
create index idx_fechfinanformapagamento_formapagamento on fechamentofinanceiroformapagamento(formapagamento);
create index idx_fechfinanformapagamento_contacorrente on fechamentofinanceiroformapagamento(contacorrente);

alter table fechamentofinanceirodetalhamentovalor add column tipoValor varchar(30) default 'VALOR';
alter table fechamentofinanceirodetalhamentovalor add column valorTipoValor NUMERIC(20,2);

---------- Pedro Andrade 30/08/2018 11:33 7.0.1 ------

ALTER TABLE fechamentomeshistoricomodificacao DROP CONSTRAINT fechamentomeshistoricomodificacao_usuarioresponsavel_fkey;

ALTER TABLE fechamentomeshistoricomodificacao ADD CONSTRAINT fechamentomeshistoricomodificacao_fechamentomes_fkey FOREIGN KEY (fechamentomes)
      REFERENCES fechamentomes (codigo) MATCH SIMPLE ON UPDATE CASCADE ON DELETE NO ACTION;

ALTER TABLE fechamentomesunidadeensino ADD CONSTRAINT fechamentomesunidadeensino_fechamentomes_fkey FOREIGN KEY (fechamentomes)
      REFERENCES fechamentomes (codigo) MATCH SIMPLE ON UPDATE CASCADE ON DELETE NO ACTION;	  
	  
---------------------------------------------------------- Rodrigo Wind 30/08/2018 12:00 7.0.1 Inclusao centro resultado da conta pagar ------------------------------------------------------------------------------------------------------
INSERT INTO centroresultadoorigem(
            codorigem, tipocentroresultadoorigem, categoriadespesa, 
            centroresultadoadministrativo, 
            quantidade, valor, unidadeensino, 
            departamento, funcionariocargo, curso, turno, turma, porcentagem, tipomovimentacaocentroresultadoorigemenum, tiponivelcentroresultadoenum, datamovimentacao)
    (
	select contapagar.codigo,  'CONTA_PAGAR', centrodespesa,
	case when turma.codigo is not null then  turma.centroresultado else
	case when curso.codigo is not null then  unidadeensinocursocentroresultado.centroresultado else
	case when departamento.codigo is not null then  departamento.centroresultado else unidadeensino.centroresultado end end end as centroresultadoacademico,
	1, valor, contapagar.unidadeensino, 
        departamento.codigo, null, contapagar.curso, contapagar.turno, turma.codigo, 100, 'ENTRADA' as tipomovimentacaocentroresultadoorigemenum, 
		case when turma.codigo is not null then 'TURMA' else 
		case when curso.codigo is not null then 'CURSO' else
		case when departamento.codigo is not null then	'DEPARTAMENTO' else 'UNIDADE_ENSINO' end end end as tiponivelcentroresultadoenum, 
		contapagar.datavencimento
	from contapagar  
	inner join categoriadespesa on  categoriadespesa.codigo = contapagar.centrodespesa
	inner join unidadeensino on unidadeensino.codigo = contapagar.unidadeensino
	left join departamento on departamento.codigo = contapagar.departamento
	left join curso on curso.codigo = contapagar.curso
	left join unidadeensinocursocentroresultado on unidadeensinocursocentroresultado.curso = curso.codigo and unidadeensinocursocentroresultado.unidadeensino = unidadeensino.codigo
	left join turma on turma.codigo = contapagar.turma
	where not exists (select codigo from centroresultadoorigem 
	where codorigem  = contapagar.codigo::VARCHAR 
	and tipocentroresultadoorigem = 'CONTA_PAGAR')
	and (case when turma.codigo is not null then  turma.centroresultado else
	case when curso.codigo is not null then  unidadeensinocursocentroresultado.centroresultado else
	case when departamento.codigo is not null then  departamento.centroresultado else unidadeensino.centroresultado end end end) is not null
	order by contapagar.codigo
	
    );
	
	
update configuracaofinanceirocartao  set centroresultadoadministrativo = (
select centroresultado from unidadeensino     
inner join configuracaofinanceiro on configuracaofinanceiro.configuracoes = unidadeensino.configuracoes
and configuracaofinanceiro.codigo = configuracaofinanceirocartao.configuracaofinanceiro
order by case when matriz then 0 else 1 end  limit 1
)
where centroresultadoadministrativo is null;


INSERT INTO centroresultadoorigem(
            codorigem, tipocentroresultadoorigem, categoriadespesa, 
            centroresultadoadministrativo, 
            quantidade, valor, unidadeensino, 
            departamento, funcionariocargo, curso, turno, turma, porcentagem, 
            tipomovimentacaocentroresultadoorigemenum, tiponivelcentroresultadoenum, datamovimentacao)
    (
	select estoque.codigo,  'ESTOQUE', categoriaproduto.categoriadespesa,
	departamento.centroresultado, estoque.quantidade, 
	estoque.quantidade*estoque.precounitario, estoque.unidadeensino, 
        departamento.codigo, null, null, null, null, 100, 'ENTRADA' as tipomovimentacaocentroresultadoorigemenum, 
	'DEPARTAMENTO' as tiponivelcentroresultadoenum, estoque.dataentrada
	from estoque  	
	inner join unidadeensino on unidadeensino.codigo = estoque.unidadeensino
	inner join produtoservico on produtoservico.codigo = estoque.produto
	inner join categoriaproduto on produtoservico.categoriaproduto = categoriaproduto.codigo
	inner join departamento on departamento.controlaestoque 
	and ((departamento.unidadeensino is not null and departamento.unidadeensino = unidadeensino.codigo)
	or (departamento.unidadeensino is null)
	)
	and departamento.codigo = (
		select dep.codigo from departamento  as dep
		where dep.controlaestoque
		and ((dep.unidadeensino is not null and dep.unidadeensino = unidadeensino.codigo)
		or (dep.unidadeensino is null)
		)
		order by case when dep.unidadeensino is not null then 0 else 1 end, dep.codigo limit 1
	)
	where estoque.quantidade > 0 and not exists (select codigo from centroresultadoorigem 
	where codorigem  = estoque.codigo::VARCHAR 
	and tipocentroresultadoorigem = 'ESTOQUE')
	order by estoque.dataentrada
	
    );
	
----------- Rodrigo Wind 30/08/2018 14:27 7.0.1 -------------------

UPDATE unidadeensino  set centroresultadorequerimento  = centroresultado where centroresultadorequerimento is null;


update centroresultado set centroresultadoprincipal = t.centroresultadoprincipal from (
select centroresultado.codigo, crne.codigo as centroresultadoprincipal from centroresultado  
inner join unidadeensinocursocentroresultado on unidadeensinocursocentroresultado.centroresultado = centroresultado.codigo
inner join curso on unidadeensinocursocentroresultado.curso = curso.codigo
inner join unidadeensino on unidadeensinocursocentroresultado.unidadeensino = unidadeensino.codigo
inner join centroresultado as crne on crne.descricao = (case curso.niveleducacional 
when 'IN' then 'Educação Infantil'
when 'BA' then 'Ensino Fundamental'
when 'ME' then 'Ensino Médio'
when 'EX' then 'Extensão'
when 'SE' then 'Sequencial'
when 'GT' then 'Graduação Tecnológica'
when 'SU' then 'Graduação'
when 'PO' then 'Pós-graduação'
when 'MT' then 'Pós-graduação(Stricto Sensu) - Mestrado'
when 'PR' then 'Técnico/Profissionalizante' else '' end ||' - '||unidadeensino.nome)
where centroresultado.centroresultadoprincipal is null
) as t where t.codigo = centroresultado.codigo;

insert into unidadeensinoniveleducacionalcentroresultado (unidadeensino, centroresultado, tiponiveleducacional) (
select distinct unidadeensino, centroresultado.centroresultadoprincipal , 

case curso.niveleducacional 
when 'IN' then 'INFANTIL'
when 'BA' then 'BASICO'
when 'ME' then 'MEDIO'
when 'EX' then 'EXTENSAO'
when 'SE' then 'SEQUENCIAL'
when 'GT' then 'GRADUACAO_TECNOLOGICA'
when 'SU' then 'SUPERIOR'
when 'PO' then 'POS_GRADUACAO'
when 'MT' then 'MESTRADO'
when 'PR' then 'PROFISSIONALIZANTE' end

from unidadeensinocursocentroresultado  
inner join curso on curso.codigo =  unidadeensinocursocentroresultado.curso
inner join centroresultado on centroresultado.codigo =  unidadeensinocursocentroresultado.centroresultado
where not exists (
	select codigo from unidadeensinoniveleducacionalcentroresultado uecr 
	where uecr.unidadeensino = unidadeensinocursocentroresultado.unidadeensino
	and uecr.tiponiveleducacional = case curso.niveleducacional 
when 'IN' then 'INFANTIL'
when 'BA' then 'BASICO'
when 'ME' then 'MEDIO'
when 'EX' then 'EXTENSAO'
when 'SE' then 'SEQUENCIAL'
when 'GT' then 'GRADUACAO_TECNOLOGICA'
when 'SU' then 'SUPERIOR'
when 'PO' then 'POS_GRADUACAO'
when 'MT' then 'MESTRADO'
when 'PR' then 'PROFISSIONALIZANTE' end
)
);

INSERT INTO centroresultado( identificadorcentroresultado, descricao, restricaousocentroresultado, situacao ) (
	select (select count(codigo) from unidadeensino) + case when exists(select 1 from departamento  where unidadeensino is null limit 1) 
	then 1 else 0 end +1, 'BIBLIOTECAS', 'NENHUM', 'ATIVO'
	from biblioteca 
	where not exists (select codigo from biblioteca bib where bib.centroresultado is not null )
	and not exists (select codigo from centroresultado where descricao = 'BIBLIOTECAS')
	LIMIT 1 	
	
);


INSERT INTO centroresultado( identificadorcentroresultado, descricao, restricaousocentroresultado, situacao, centroresultadoprincipal ) (
	select (((select count(codigo) from unidadeensino) + case when exists(select 1 from departamento  where unidadeensino is null limit 1) then 1 else 0 end)
	+ (row_number() over (order by biblioteca.nome))), biblioteca.nome, 'NENHUM', 'ATIVO', centroresultado.codigo
	from biblioteca  
	inner join centroresultado on centroresultado.descricao = 'BIBLIOTECAS'
	where biblioteca.centroresultado is null	
);
update biblioteca  set centroresultado = centroresultado.codigo from centroresultado where centroresultado.descricao = biblioteca.nome and biblioteca.centroresultado is null;

alter table fechamentomes  add column gerarFechamentoMesTodasUnidades boolean default false;
ALTER TABLE fechamentomes ALTER COLUMN datafechamento DROP NOT NULL;

----- Renato Borges 03/09/2018 10:53 7.0.1 -----
ALTER TABLE HISTORICOFUNCAO RENAME COLUMN funcao TO cargo;

ALTER TABLE public.historicofuncao ADD CONSTRAINT cargo_fkey FOREIGN KEY (cargo)
	REFERENCES public.cargo (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

---- Alessandro 7.0.1 05/09/2018 ----
alter table unidadeensinocurso alter column codigoitemlistaservico type varchar(50);
alter table integracaoginfescursoitem alter column itemlistaservico type varchar(50);
alter table integracaoginfesalunoitem add column datainicial varchar(10);
alter table integracaoginfesalunoitem add column datafinal varchar(10);
alter table integracaoginfesalunoitem add column valordescontocondicionado numeric(15,2);
alter table integracaoginfesalunoitem add column valordescontoincondicionado numeric(15,2);
alter table integracaoginfesalunoitem alter column numeroidentificacao type varchar(25);

------------Uendel Santos 05/09/2018 16:14 7.0.1 -----------------------------------------------------
alter table planoconta drop CONSTRAINT unq_planoconta_indentificador;

------------Pedro Andrade 06/09/2018 10:33 7.0.1 -----------------------------------------------------
alter table itemcondicaodescontorenegociacao add column percentualplanodesconto numeric(20,2);
alter table itemcondicaodescontorenegociacao add column percentualdescontoprogressivo numeric(20,2);
alter table itemcondicaodescontorenegociacao add column percentualdescontoaluno numeric(20,2);

---- Alessandro 7.0.1 10/09/2018 ----
alter table configuracaogeralsistema add column urlLdap varchar(50);
alter table configuracaogeralsistema add column usuarioLdap varchar(50);
alter table configuracaogeralsistema add column senhaLdap varchar(50);
alter table configuracaogeralsistema add column bloquearAlteracaoUsernameSincronizandoComCPF boolean default false;

-------------- Rodrigo Wind 10/09/2018 15:24 7.0.1 ---------------------------------------
create index idx_centroresultadoorigem_origem_codorigem on centroresultadoorigem(tipoCentroResultadoOrigem, codorigem);
create index idx_centroresultadoorigem_centroreceita on centroresultadoorigem(centroreceita);
create index idx_centroresultadoorigem_categoriadespesa on centroresultadoorigem(categoriadespesa);

------------- Rodrigo Wind 11/09/2018 07:45 7.0.1 ---------------
alter table turmacontrato drop constraint fk_turmacontrato_turma;
alter table turmacontrato add constraint fk_turmacontrato_turma foreign key (turma) references turma(codigo) on update cascade on delete cascade;

------------- Leandro 11/09/2018 09:05 7.0.1 ---------------
update contareceber set tipopessoa = 'FU' where codigo in(
select contareceber.codigo from contareceber 
inner join emprestimo on codorigem::int = emprestimo.codigo
where contareceber.tipoorigem = 'BIB' 
and contareceber.tipopessoa = 'PR' );

----- Renato Borges 12/09/2018 17:39 7.0.1 -----
CREATE TABLE public.afastamentofuncionario(
    codigo serial NOT NULL,
    funcionariocargo integer NOT NULL,
    tipoafastamento character varying(50),
    motivoafastamento character varying(50),
    datainicio date,
    datafinal date,
    arquivo integer,
    PRIMARY KEY (codigo),
    CONSTRAINT funcionariocargo_fkey FOREIGN KEY (funcionariocargo) REFERENCES public.funcionariocargo (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT arquivo_fkey FOREIGN KEY (arquivo) REFERENCES public.arquivo (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)WITH (OIDS = FALSE);
ALTER TABLE public.afastamentofuncionario OWNER to postgres;

alter table historicoafastamento  add column processado boolean null;

------------- Ana Claudia 13/09/2018 09:50 7.0.1 ---------------

UPDATE areaconhecimento SET areaconhecimentoprincipal = null WHERE areaconhecimentoprincipal = 0;

ALTER TABLE public.areaconhecimento ADD CONSTRAINT fk_areaconhecimentoprincipal FOREIGN KEY (areaconhecimentoprincipal)
    REFERENCES public.areaconhecimento (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;

------------- Gilberto 17/09/2018 15:31 7.0.1 ---------------
alter table eventofolhapagamento add column inssFerias boolean null;
alter table eventofolhapagamento add column previdenciaPropriaFerias boolean null;
alter table eventofolhapagamento add column previdenciaObrigatoriaFerias boolean null;
alter table eventofolhapagamento add column incideAssociacaoSindicatoFerias boolean null;


------------- Gilberto 18/09/2018 15:36 7.0.1 ---------------

CREATE TABLE public.sindicatomedia13
(
    codigo serial NOT NULL,
    grupo character varying(30) COLLATE pg_catalog."default",
    eventomedia integer,
    sindicato integer,
    CONSTRAINT sindicatomedia13_pkey PRIMARY KEY (codigo),
    CONSTRAINT sindicatomedia13_eventomedia_fkey FOREIGN KEY (eventomedia)
        REFERENCES public.eventofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT sindicatomedia13_sindicato_fkey FOREIGN KEY (sindicato)
        REFERENCES public.sindicato (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.sindicatomedia13 OWNER to postgres;


CREATE TABLE public.sindicatomediarescisao
(
    codigo serial NOT NULL,
    grupo character varying(30) COLLATE pg_catalog."default",
    eventomedia integer,
    sindicato integer,
    CONSTRAINT sindicatomediarescisao_pkey PRIMARY KEY (codigo),
    CONSTRAINT sindicatomediarescisao_eventomedia_fkey FOREIGN KEY (eventomedia)
        REFERENCES public.eventofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT sindicatomediarescisao_sindicato_fkey FOREIGN KEY (sindicato)
        REFERENCES public.sindicato (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (
    OIDS = FALSE
)
TABLESPACE pg_default;

ALTER TABLE public.sindicatomediarescisao OWNER to postgres;

------------- Ana Claudia 18/09/2018 15:40 7.0.1 ---------------
alter table configuracaoAcademico add column validarDadosEnadeCensoMatricularAluno boolean default false;



-------------- Rodrigo 18/09/2018 16:40 6.25.15 ------------------------
CREATE OR REPLACE FUNCTION sem_acentos(character varying)
  RETURNS character varying AS
$BODY$
SELECT translate($1, 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ''', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION sem_acentos(character varying)
  OWNER TO postgres;
  
------------- Ana Claudia 18/09/2018 16:46 7.0.1 ---------------
alter table curso add column utilizarRecursoAvaTerceiros boolean default false;

------------- Gilberto 19/09/2018 10:27 7.0.1 ---------------

alter table sindicato add column eventoPrimeiraParcela13 integer null;
alter table sindicato add CONSTRAINT sindicato_eventoPrimeiraParcela13_fkey FOREIGN KEY (eventoPrimeiraParcela13)
        REFERENCES public.eventofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;

alter table sindicato add column eventoDescontoPrimeiraParcela13 integer null;
alter table sindicato add CONSTRAINT sindicato_eventoDescontoPrimeiraParcela13_fkey FOREIGN KEY (eventoDescontoPrimeiraParcela13)
        REFERENCES public.eventofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;


----- Hyllner Valadares da Silva 20/09/2018 10:00 7.0.1 -----
ALTER TABLE HISTORICODEPENDENTES DROP CONSTRAINT historicodependentes_funcionariocargo_fkey;
ALTER TABLE HISTORICODEPENDENTES RENAME COLUMN FUNCIONARIOCARGO TO FUNCIONARIO;

ALTER TABLE public.historicodependentes ADD CONSTRAINT historicodependentes_funcionario_fkey FOREIGN KEY (funcionario)
	REFERENCES public.funcionario (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;		

------------- Gilberto 20/09/2018 09:20 7.0.1 ---------------

alter table controlelancamentofolhapagamento add column competenciaFolhaPagamento integer not null;
alter table controlelancamentofolhapagamento add column anoCompetencia integer not null;
alter table controlelancamentofolhapagamento add column mesCompetencia integer not null;
alter table controlelancamentofolhapagamento add CONSTRAINT controlelancamentofolhapagamento_competenciaFolhaPagamento_fkey FOREIGN KEY (competenciaFolhaPagamento)
        REFERENCES public.competenciaFolhaPagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION;

alter table controlelancamentofolhapagamento add column primeiraParcela13 boolean null;
alter table controlelancamentofolhapagamento add column segundaParcela13 boolean null;

------------- Rodrigo 20/09/2018 10:40 7.0.1 ----------------
alter table contacorrente add column realizarNegociacaoContaReceberVencidaAutomaticamente boolean default false;
alter table contacorrente add column permiteEmissaoBoletoVencido  boolean default false;
alter table contacorrente add column numeroDiaVencidoNegociarContaReceberAutomaticamente int default 1;
alter table contacorrente add column numeroDiaAvancarVencimentoContaReceber int default 0;
alter table configuracaofinanceiro  add column qtdeDiasExcluirNegociacaoContaReceberVencida integer default 4;

------------- Edigar 21/09/2018 09:04 7.0.1 ----------------
alter table fechamentoMes add column dataUtilizarVerificarBloqueioContaPagar varchar(2) default 'DC';
alter table fechamentoMes add column dataUtilizarVerificarBloqueioContaReceber varchar(2) default 'DC';

----- Renato Borges 20/09/2018 09:02 7.0.1 -----
CREATE TABLE public.rescisao(
    codigo serial NOT NULL,
    templatelancamentofolhapagamento integer NOT NULL,
    secaofolhapagamento integer,
    competenciafolhapagamento integer NOT NULL,
    periodo integer NOT NULL,
    tipodemissao character varying(50) COLLATE pg_catalog."default",
    motivodemissao character varying(50) COLLATE pg_catalog."default",
    datademissao date,
    CONSTRAINT rescisao_pkey PRIMARY KEY (codigo),
    CONSTRAINT competenciafolhapagamento_fkey FOREIGN KEY (competenciafolhapagamento)
        REFERENCES public.competenciafolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT competenciaperiodofolhapagamento FOREIGN KEY (periodo)
        REFERENCES public.competenciaperiodofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT secaofolhapagamento_fkey FOREIGN KEY (secaofolhapagamento)
        REFERENCES public.secaofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT templatelancamentofolhapagamento_fkey FOREIGN KEY (templatelancamentofolhapagamento)
        REFERENCES public.templatelancamentofolhapagamento (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
WITH (OIDS = FALSE);

------------- Pedro Andrade 24/09/2018 08:15 7.0.1 ----------------
alter table configuracaofinanceiro  add column indicereajustepadraocontasporatraso integer;
alter table configuracaofinanceiro  add column qtddiasaplicarindirereajusteporatrasocontareceber integer;
alter table contareceber  add column valorindicereajusteporatraso numeric(20,2);
alter table contareceber  add column indiceReajustePadraoPorAtraso integer;

ALTER TABLE configuracaofinanceiro ADD CONSTRAINT configuracaofinanceiro_indicereajustepadraocontasporatraso_fkey FOREIGN KEY (indicereajustepadraocontasporatraso)
      REFERENCES indicereajuste (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
	  
ALTER TABLE contareceber ADD CONSTRAINT contareceber_indiceReajustePadraoPorAtraso_fkey FOREIGN KEY (indiceReajustePadraoPorAtraso)
      REFERENCES indicereajuste (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
	  
----- Hyllner Valadares da Silva 24/09/2018 10:17 7.0.1 -----	  
ALTER TABLE RESCISAO ADD COLUMN FUNCIONARIOCARGO INTEGER NOT NULL;
ALTER TABLE public.rescisao ADD CONSTRAINT funcionariocargo_fkey FOREIGN KEY (funcionariocargo)
	REFERENCES funcionariocargo (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
	
------------- Rodrigo 24/09/2018 11:55 7.0.1 -----------------
	alter table pessoa alter column celular type varchar(20);
alter table pessoa alter column telefoneres type varchar(20);
alter table pessoa alter column telefonecomer type varchar(20);
alter table pessoa alter column telefonefiador type varchar(20);
alter table pessoa alter column telefonerecado type varchar(20);
alter table prospects alter column telefonecomercial type varchar(20);
alter table prospects alter column telefoneempresa type varchar(20);
alter table prospects alter column telefonerecado type varchar(20);
alter table prospects alter column telefoneresidencial type varchar(20);
alter table prospects alter column celular type varchar(20);

----- Renato Borges 24/09/2018 10:05 7.0.1 -----
CREATE TABLE public.sindicatomediarescisao(
    codigo serial,
    grupo character varying(30) COLLATE pg_catalog."default",
    eventomedia integer,
    sindicato integer,
    tipoeventomediarescisao character varying(50) COLLATE pg_catalog."default",
    CONSTRAINT sindicatomediarescisao_pkey PRIMARY KEY (codigo),
    CONSTRAINT sindicatomediarescisao_eventomedia_fkey FOREIGN KEY (eventomedia)
        REFERENCES public.eventofolhapagamento (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT sindicatomediarescisao_sindicato_fkey FOREIGN KEY (sindicato)
        REFERENCES public.sindicato (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS = FALSE)
TABLESPACE pg_default;
ALTER TABLE public.sindicatomediarescisao OWNER to postgres;

----- Renato Borges 24/09/2018 17:38 7.0.1 -----
ALTER TABLE public.rescisao DROP CONSTRAINT secaofolhapagamento_fkey;
ALTER TABLE public.rescisao DROP CONSTRAINT competenciaperiodofolhapagamento;

ALTER TABLE public.rescisao DROP COLUMN secaofolhapagamento;
ALTER TABLE public.rescisao DROP COLUMN periodo;
ALTER TABLE public.rescisao DROP COLUMN funcionariocargo;

ALTER TABLE public.rescisao ADD COLUMN situacao character varying(50);

CREATE TABLE public.rescisaoindividual(
    codigo serial,
    rescisao integer,
    funcionariocargo integer,
    CONSTRAINT funcionariocargo_fkey FOREIGN KEY (funcionariocargo)
        REFERENCES public.funcionariocargo (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT rescisao_fkey FOREIGN KEY (rescisao) REFERENCES public.rescisao (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH (OIDS = FALSE);

ALTER TABLE public.rescisaoindividual OWNER to postgres;

----- Hyllner Valadares da Silva 24/09/2018 10:17 7.0.1 -----
ALTER TABLE templatelancamentofolhapagamento add column lancarrescisao boolean null;

------------- Gilberto 24/09/2018 18:24 7.0.1 ---------------
ALTER TABLE templatelancamentofolhapagamento add column lancar13Parcela1 boolean null;
ALTER TABLE templatelancamentofolhapagamento add column lancar13Parcela2 boolean null;

ALTER TABLE controlelancamentofolhapagamento drop column lancamentofolhapagamento;
ALTER TABLE controlelancamentofolhapagamento drop CONSTRAINT lancamentofolhapagamento_fkey;

---------JEAN PIERRE 25/09/2018 10:07 -----------------------------------------------------------
create index CONCURRENTLY  idx_detalhamentovalorconta_codigoorigem                         on public.detalhamentovalorconta (codigoorigem); 
create index CONCURRENTLY  idx_detalhamentovalorconta_codorigemdotipodetalhe               on public.detalhamentovalorconta (codorigemdotipodetalhe);
create index CONCURRENTLY  idx_detalhamentovalorconta_datalimiteaplicacaodesconto          on public.detalhamentovalorconta (datalimiteaplicacaodesconto);

----- Hyllner Valadares da Silva 25/09/2018 10:48 7.0.1 -----
ALTER TABLE rescisaoindividual add CONSTRAINT rescisaoindividual_pkey PRIMARY KEY (codigo);

------------- Gilberto 25/09/2018 15:42 7.0.1 ---------------
ALTER TABLE controlelancamentofolhapagamento add column rescisao boolean null;

--------------- Allan Costa 25/09/2018 15:43 7.0.1 ---------------
alter table tiporequerimento add column uploadarquivoobrigatorio boolean default false;


----- Renato Borges 25/09/2018 15:41 7.0.1 -----
ALTER TABLE public.rescisaoindividual ADD COLUMN historicosituacao integer;
ALTER TABLE public.rescisaoindividual ADD CONSTRAINT historicosituacao_fkey FOREIGN KEY (historicosituacao)
    REFERENCES public.historicosituacao (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

	
------------- Gilberto 25/09/2018 15:42 7.0.1 ---------------

ALTER TABLE public.controlelancamentofolhapagamento
    ADD COLUMN competenciaperiodofolhapagamento integer NOT NULL;
ALTER TABLE public.controlelancamentofolhapagamento
    ADD CONSTRAINT competenciaperiodofolhapagamento_fkey FOREIGN KEY (competenciaperiodofolhapagamento)
    REFERENCES public.competenciaperiodofolhapagamento (codigo) MATCH SIMPLE
    ON UPDATE NO ACTION
    ON DELETE NO ACTION;

	
------------- Gilberto 27/09/2018 18:33 7.0.1 ---------------
ALTER TABLE contracheque drop column lancamentofolhapagamento;

---------- Ana Claudia 29/09/2018 08:37 7.0.1 ------------------
alter table programacaotutoriaonline add column definirPeriodoAulaOnline boolean default false;
alter table programacaotutoriaonline add column dataInicioAula date;
alter table programacaotutoriaonline add column dataTerminoAula date;
alter table programacaotutoriaonline add column ano varchar(4);
alter table programacaotutoriaonline add column semestre varchar(1);

-------------------JEAN PIERRE 29/09/2018 7.0.1-----------------------------------------
create index concurrently idx_registronegativacaocobrancacontareceberitem_contareceber_dataregistro 			  ON registronegativacaocobrancacontareceberitem (contareceber,dataregistro);
create index concurrently idx_registronegativacaocobrancacontareceberitem_matricula 							  ON registronegativacaocobrancacontareceberitem (matricula);
create index concurrently idx_registronegativacaocobrancacontareceberitem_registronegativacaocobrancacontareceber ON registronegativacaocobrancacontareceberitem (registronegativacaocobrancacontareceber);


---------------------JEAN PIERRE 01/10/2018 15:37-----------------------------------------
create index concurrently idx_horarioturmadiaitem_dataultimaalteracao on horarioturmadiaitem (dataultimaalteracao);
CREATE EXTENSION pg_trgm;
CREATE OR REPLACE FUNCTION sem_acentos(character varying)
  RETURNS character varying AS
$BODY$
SELECT translate(trim($1), 'áéíóúàèìòùãõâêîôôäëïöüçÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛÄËÏÖÜÇ''', 'aeiouaeiouaoaeiooaeioucAEIOUAEIOUAOAEIOOAEIOUC')
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION sem_acentos(character varying)
  OWNER TO postgres;

CREATE INDEX idx_pessoa_nome_funcao_sem_acentos ON pessoa  USING gin ( sem_acentos(nome) gin_trgm_ops);
create index concurrently idx_matriculaperiodoturmadisciplina_dataultimaalteracao on matriculaperiodoturmadisciplina (dataultimaalteracao);
create index concurrently idx_updated_updated on matricula (updated);

------------- Rodrigo 02/10/2018 11:00 7.0.1 ---------------------
create index idx_contareceber_nossonumero_func_gin on contareceber using gin(nossoNumero gin_trgm_ops);
drop index concurrently  ch_cr_sit_par_to_dv;
drop index concurrently idx_contareceber_sit_datacomp_unidade;
 

----------- Rodrigo 03/10/2018 07:47 7.0.1 -----------------------

CREATE OR REPLACE function converterDataIndiceReajuste(ano varchar, mes varchar ) 
returns DATE as 
$BODY$
	select (($1 ||'-'||case $2 when 'JANEIRO' THEN '01'
when 'FEVEREIRO' THEN '02'
when 'MARCO' THEN '03'
		 when 'ABRIL' THEN '04'
		 when 'MAIO' THEN '05'
		 when 'JUNHO' THEN '06'
		 when 'JULHO' THEN '07'
		 when 'AGOSTO' THEN '08'
		 when 'SETEMBRO' THEN '09'
		 when 'OUTUBRO' THEN '10'
		 when 'NOVEMBRO' THEN '11' ELSE '12' END)||'-01')::DATE
$BODY$
LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION converterDataIndiceReajuste(varchar, varchar)
  OWNER TO postgres;

  
  CREATE OR REPLACE function calcularIndiceReajusteTabela(valorbase numeric, dataInicial date, datafinal date, indicereajuste int ) 
returns table(indicereajuste int, competencia date, valorbase numeric, percentual numeric, percentualcalculado numeric, valorfinal numeric) as 
$BODY$

WITH RECURSIVE  indice (
indicereajuste, competencia,  valorbase, percentual, percentualcalculado, valorfinal
) as (
(select indicereajuste, converterDataIndiceReajuste(indiceReajustePeriodo.ano, indiceReajustePeriodo.mes) as competencia, 
$1 as valorbase, indiceReajustePeriodo.percentualReajuste as percentual, 
(($1*indiceReajustePeriodo.percentualReajuste)/100)::numeric(20,2) as percentualcalculado,
$1 + (($1*indiceReajustePeriodo.percentualReajuste)/100)::numeric(20,2) as valorfinal
from indiceReajustePeriodo 
where indiceReajustePeriodo.indiceReajuste = $4

and to_char(converterDataIndiceReajuste(indiceReajustePeriodo.ano, indiceReajustePeriodo.mes), 'yyyy-MM') >= to_char($2, 'yyyy-MM')

and to_char(converterDataIndiceReajuste(indiceReajustePeriodo.ano, indiceReajustePeriodo.mes), 'yyyy-MM') <= to_char($3, 'yyyy-MM')
		 
order by competencia limit 1)

union		 

select indice.indiceReajuste, converterDataIndiceReajuste(indiceReajustePeriodo.ano, indiceReajustePeriodo.mes) as competencia, 
indice.valorfinal as valorbase, indiceReajustePeriodo.percentualReajuste as percentual, 
((indice.valorfinal*indiceReajustePeriodo.percentualReajuste)/100)::numeric(20,2) as percentualcalculado,
indice.valorfinal + ((indice.valorfinal*indiceReajustePeriodo.percentualReajuste)/100)::numeric(20,2) as valorfinal
from indiceReajustePeriodo 
inner join indice on indice.competencia < converterDataIndiceReajuste(indiceReajustePeriodo.ano, indiceReajustePeriodo.mes)
and to_char(indice.competencia, 'yyyy-MM') <= to_char(($3-('1 month'::interval)), 'yyyy-MM')
where indiceReajustePeriodo.indiceReajuste = indice.indiceReajuste

and to_char(converterDataIndiceReajuste(indiceReajustePeriodo.ano, indiceReajustePeriodo.mes), 'yyyy-MM') > to_char(indice.competencia, 'yyyy-MM')

and to_char(converterDataIndiceReajuste(indiceReajustePeriodo.ano, indiceReajustePeriodo.mes), 'yyyy-MM') <= to_char((indice.competencia + ('1 month'::interval)), 'yyyy-MM')

) select * from indice 
order by competencia 

$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION calcularIndiceReajusteTabela(numeric, date, date, int)
  OWNER TO postgres;


CREATE OR REPLACE function calcularIndiceReajuste(valorbase numeric, dataInicial date, datafinal date, indicereajuste int ) 
returns NUMERIC(20,2) as 
$BODY$
	select valorfinal from calcularIndiceReajusteTabela($1, $2, $3, $4) as t order by competencia desc limit 1
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION calcularIndiceReajuste(numeric, date, date, int)
  OWNER TO postgres;
  
  
  ---------------------------------------JEAN PIERRE 03/10/2018 15:17-------------------------------------------------------------------
  CREATE INDEX concurrently idx_pessoa_nome_funcao_upper ON pessoa  USING gin (upper(nome) gin_trgm_ops);

  ------------ Leandro 03/10/2018 15:30 7.0.1----------------------------
alter table conteudoplanejamento add column praticasupervisionada text;
alter table registroaula add column praticasupervisionada text;



  ----------------- Rodrigo Wind 04/10/2018 07:45 ----------------------
  DROP FUNCTION periodoauladisciplinaaluno(integer);
CREATE OR REPLACE FUNCTION periodoauladisciplinaaluno(IN historico integer)
  RETURNS TABLE(professor_codigo integer, professor_nome character varying, professores_codigo integer[], disciplina_codigo integer, disciplina_nome character varying, datainicio date, datatermino date, modalidadedisciplina character varying) AS
$BODY$ 
        (select * from  periodoauladisciplinaaluno ($1, true, null, false))
  $BODY$
  LANGUAGE sql VOLATILE
  COST 1
  ROWS 1;
ALTER FUNCTION periodoauladisciplinaaluno(integer)
  OWNER TO postgres;
  
  create index idx_nossonumerocontareceber_nossonumero on nossonumerocontareceber(nossonumero);
  create index idx_contareceber_nossonumeroantigo on contareceber(nossonumeroantigo);
  create index idx_contarecebernossonumero_contareceber on contarecebernossonumero (contareceber);
  
  ----------------- PEDRO ANDRADE  04/10/2018 07:45 ----------------------
  alter table parceiro add column isentarmulta  boolean default false;
alter table parceiro add column isentarreajusteparcelavencida  boolean default false;
alter table parceiro add column financiamentoproprio  boolean default false;
alter table parceiro add column numeroparcelavencidassuspenderfinanciamento  integer;
UPDATE parceiro set isentarmulta = isentarjuromulta; 
ALTER TABLE parceiro RENAME COLUMN isentarjuromulta TO isentarjuro;
alter table planodescontocontareceber add column suspensofinanciamentoproprio  boolean default false;

----------- Rodrigo Wind 04/10/2018 16:12 -----------------------
update gradecurriculargrupooptativadisciplina  set modalidadedisciplina = 'ON_LINE' where modalidadedisciplina = 'ONLINE';


--------------- Rodrigo Wind 04/10/2018 16:36 7.0.1 ------------------------

CREATE OR REPLACE function calcularIndiceReajusteTabela(valorbase numeric, dataInicial date, datafinal date, indicereajuste int ) 
returns table(indicereajuste int, competencia date, valorbase numeric, percentual numeric, percentualcalculado numeric, valorfinal numeric) as 
$BODY$

WITH RECURSIVE  indice (
indicereajuste, competencia,  valorbase, percentual, percentualcalculado, valorfinal
) as (
(select indicereajuste, converterDataIndiceReajuste(indiceReajustePeriodo.ano, indiceReajustePeriodo.mes) as competencia, 
$1 as valorbase, indiceReajustePeriodo.percentualReajuste as percentual, 
(($1*indiceReajustePeriodo.percentualReajuste)/100)::numeric(20,7) as percentualcalculado,
$1 + (($1*indiceReajustePeriodo.percentualReajuste)/100)::numeric(20,7) as valorfinal
from indiceReajustePeriodo 
where indiceReajustePeriodo.indiceReajuste = $4

and to_char(converterDataIndiceReajuste(indiceReajustePeriodo.ano, indiceReajustePeriodo.mes), 'yyyy-MM') >= to_char($2, 'yyyy-MM')

and to_char(converterDataIndiceReajuste(indiceReajustePeriodo.ano, indiceReajustePeriodo.mes), 'yyyy-MM') <= to_char($3, 'yyyy-MM')
		 
order by competencia limit 1)

union		 

select indice.indiceReajuste, converterDataIndiceReajuste(indiceReajustePeriodo.ano, indiceReajustePeriodo.mes) as competencia, 
indice.valorfinal as valorbase, indiceReajustePeriodo.percentualReajuste as percentual, 
((indice.valorfinal*indiceReajustePeriodo.percentualReajuste)/100)::numeric(20,7) as percentualcalculado,
indice.valorfinal + ((indice.valorfinal*indiceReajustePeriodo.percentualReajuste)/100)::numeric(20,7) as valorfinal
from indiceReajustePeriodo 
inner join indice on indice.competencia < converterDataIndiceReajuste(indiceReajustePeriodo.ano, indiceReajustePeriodo.mes)
and to_char(indice.competencia, 'yyyy-MM') <= to_char(($3-('1 month'::interval)), 'yyyy-MM')
where indiceReajustePeriodo.indiceReajuste = indice.indiceReajuste

and to_char(converterDataIndiceReajuste(indiceReajustePeriodo.ano, indiceReajustePeriodo.mes), 'yyyy-MM') > to_char(indice.competencia, 'yyyy-MM')

and to_char(converterDataIndiceReajuste(indiceReajustePeriodo.ano, indiceReajustePeriodo.mes), 'yyyy-MM') <= to_char((indice.competencia + ('1 month'::interval)), 'yyyy-MM')

) select * from indice 
order by competencia 

$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION calcularIndiceReajusteTabela(numeric, date, date, int)
  OWNER TO postgres;


CREATE OR REPLACE function calcularIndiceReajuste(valorbase numeric, dataInicial date, datafinal date, indicereajuste int ) 
returns NUMERIC(20,2) as 
$BODY$
	select valorfinal::numeric(20,2) from calcularIndiceReajusteTabela($1, $2, $3, $4) as t order by competencia desc limit 1
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;
ALTER FUNCTION calcularIndiceReajuste(numeric, date, date, int)
----- Renato Borges 08/10/2018 10:22 7.0.1 -----  
CREATE TABLE public."historicopensao"(
    codigo serial NOT NULL,
    funcionariodependente integer NOT NULL,
    valor numeric(20, 2),
    competenciafolhapagamento integer,
    tipomovimentopensao character varying(50),
    PRIMARY KEY (codigo)
)WITH (OIDS = FALSE);
ALTER TABLE public."historicopensao" OWNER to postgres;  

ALTER TABLE public.historicopensao ADD CONSTRAINT funcionariodependente_fkey FOREIGN KEY (funcionariodependente)
    REFERENCES public.funcionariodependente (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE public.historicopensao ADD CONSTRAINT competenciafolhapagamento_fkey FOREIGN KEY (competenciafolhapagamento)
    REFERENCES public.competenciafolhapagamento (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

----------------- PEDRO ANDRADE  09/10/2018 11:15 ----------------------
 alter table parceiro add column emitirboletoemnomebeneficiado  boolean default false;
 
 ----------------- Rodrigo 09/10/2018 15:07 7.0.1 -----------------
 alter table configuracaoacademico  add column usarFormulaCalculoFrequencia boolean default false;
alter table configuracaoacademico  add column formulaCalculoFrequencia text;
---------------------Jean Pierre 17/10/2018 11:07--------------------------------------------------
ALTER TABLE comunicacaointerna      ALTER COLUMN assunto TYPE varchar (200);
ALTER TABLE documetacaomatriculalog ALTER COLUMN justificativanegacao TYPE varchar (250);
ALTER TABLE dre                     ALTER COLUMN sinal TYPE varchar (2);
ALTER TABLE liberacaofinanceirocancelamentotrancamento ALTER COLUMN motivoliberacaofinanceiro TYPE varchar (2);
ALTER TABLE arquivo                 ALTER COLUMN origem TYPE varchar (4);
ALTER TABLE arquivo                 ALTER COLUMN situacao TYPE varchar (2);
ALTER TABLE arquivo                 ALTER COLUMN extensao TYPE varchar (4);
ALTER TABLE arquivobackup           ALTER COLUMN origem TYPE varchar (4);
ALTER TABLE arquivobackup           ALTER COLUMN situacao TYPE varchar (2);
ALTER TABLE arquivobackup           ALTER COLUMN extensao TYPE varchar (4);
ALTER TABLE registroentrada         ALTER COLUMN descricao TYPE varchar (50);
ALTER TABLE registroentrada         ALTER COLUMN delimitador TYPE varchar (20);
ALTER TABLE censo                   ALTER COLUMN ano TYPE varchar (4);
ALTER TABLE censo                   ALTER COLUMN semestre TYPE varchar (1);
ALTER TABLE documetacaomatricula    ALTER COLUMN justificativanegacao TYPE varchar (250);
ALTER TABLE pessoa                  ALTER COLUMN corraca TYPE varchar (2);
ALTER TABLE pessoa                  ALTER COLUMN deficiencia TYPE varchar (2);
ALTER TABLE matricula               ALTER COLUMN formaingresso TYPE varchar (2);
ALTER TABLE matricula               ALTER COLUMN programareservavaga TYPE varchar (2);
ALTER TABLE matricula               ALTER COLUMN financiamentoestudantil TYPE varchar (2);
ALTER TABLE matricula               ALTER COLUMN apoiosocial TYPE varchar (2);
ALTER TABLE matricula               ALTER COLUMN atividadecomplementar TYPE varchar (2);
ALTER TABLE matricula               ALTER COLUMN anoingresso TYPE varchar (4);
ALTER TABLE matricula               ALTER COLUMN semestreingresso TYPE varchar (2);
ALTER TABLE matricula               ALTER COLUMN anoconclusao TYPE varchar (4);
ALTER TABLE matricula               ALTER COLUMN semestreconclusao TYPE varchar (2);
ALTER TABLE turmaabertura           ALTER COLUMN situacao TYPE varchar (2);
ALTER TABLE turma                   ALTER COLUMN tipochancela TYPE varchar (2);
ALTER TABLE paiz                    ALTER COLUMN siglainep TYPE varchar (3);
ALTER TABLE curso                   ALTER COLUMN titulo TYPE varchar (2);
ALTER TABLE mapalancamentofuturo    ALTER COLUMN tipomapalancamentofuturo TYPE varchar (2);
ALTER TABLE mapa_acentos_html       ALTER COLUMN ch TYPE varchar (1);

----------------- PEDRO ANDRADE  18/10/2018 16:15 ----------------------
alter table contareceber add column liberadodoindicereajusteporatraso  boolean default false;
ALTER TABLE contarecebernegociado DROP CONSTRAINT fk_contarecebernegociado_itemcondicaodescontorenegociacao;
ALTER TABLE contarecebernegociado DROP COLUMN itemcondicaodescontorenegociacao;
ALTER TABLE contarecebernegociado DROP COLUMN valororiginaljuro;
ALTER TABLE contarecebernegociado DROP COLUMN valororiginalmulta;

alter table negociacaocontareceber add column itemcondicaodescontorenegociacao integer;
ALTER TABLE negociacaocontareceber ADD CONSTRAINT fk_negociacaocontareceber_itemcondicaodescontorenegociacao FOREIGN KEY (itemcondicaodescontorenegociacao) REFERENCES itemcondicaodescontorenegociacao (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table negociacaocontareceber add column pessoacomissionada integer;
ALTER TABLE negociacaocontareceber ADD CONSTRAINT fk_negociacaocontareceber_pessoacomissionada FOREIGN KEY (pessoacomissionada) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table negociacaocontareceber add column valortotaljuro numeric(20,2) default 0.0;
alter table negociacaocontareceber add column valorisencaototaljuro numeric(20,2) default 0.0;
alter table negociacaocontareceber add column valorisencaototaljuromaximo numeric(20,2) default 0.0;
alter table negociacaocontareceber add column valortotalmulta numeric(20,2) default 0.0;
alter table negociacaocontareceber add column valorisencaototalmulta numeric(20,2) default 0.0;
alter table negociacaocontareceber add column valorisencaototalmultamaximo numeric(20,2) default 0.0;
alter table negociacaocontareceber add column valortotaldescontoperdido numeric(20,2) default 0.0;
alter table negociacaocontareceber add column valorconcecaodescontoperdido numeric(20,2) default 0.0;
alter table negociacaocontareceber add column valorconcecaodescontoperdidomaximo numeric(20,2) default 0.0;


---------------------jean pierre 21/10/2018 13:34------------------------------
create index idX_controleremessacontareceber_controleremessa on controleremessacontareceber (controleremessa);
create index idX_controleremessacontareceber_nossonumero on controleremessacontareceber (nossonumero);
update controleremessacontareceber set dac = '0' where (dac = '' or null);
ALTER TABLE controleremessacontareceber ALTER COLUMN dac TYPE integer USING (dac::integer);

--------------------- Renato Borges 23/10/2018 14:48------------------------------
ALTER TABLE public.eventofolhapagamento ADD COLUMN incideplanosaudeferias boolean;
---------------------pedro 24/10/2018 10:15------------------------------
ALTER TABLE contarecebernaolocalizadaarquivoretorno ALTER COLUMN valorRecebido TYPE numeric(20,2);
ALTER TABLE contarecebernaolocalizadaarquivoretorno ALTER COLUMN valor TYPE numeric(20,2);


------------------ rODRIGO 24/10/2018 10:15 7.0.2.0 -------------
alter table centroresultadoorigem add column porcentagem_tmp NUMERIC(20,4);
update centroresultadoorigem set porcentagem_tmp = (((porcentagem*1000)::NUMERIC(20,4))/1000)::NUMERIC(20,4); -- ignore
alter table centroresultadoorigem alter column porcentagem type NUMERIC(20,4);
update centroresultadoorigem set porcentagem  = porcentagem_tmp;
alter table centroresultadoorigem drop column porcentagem_tmp;
create index idx_nossonumerocontareceber_matriculaperiodo on public.nossonumerocontareceber (matriculaperiodo);

---------------------pedro 25/10/2018 15:15------------------------------
create index idx_lancamentocontabil_codorigem_tipoorigemlancamentocontabil on lancamentocontabil (codorigem,tipoorigemlancamentocontabil);

----------------- Leandro 26/10/2018 08:50 7.0.1 -----------------------
update controlecobranca set responsavel = 
(select responsavel from controlecobranca where responsavel is not null and responsavel > 0 order by codigo asc limit 1)
where responsavel is null or responsavel = 0;

 ALTER TABLE controlecobranca ADD CONSTRAINT fk_controlecobranca_responsavel FOREIGN KEY (responsavel)
	REFERENCES usuario (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;

-------- Alessandro 26/10/2018 09:00 --------
CREATE INDEX registroexecucaojob_nome ON registroexecucaojob (nome);
CREATE INDEX registroexecucaojob_datainicio ON registroexecucaojob (datainicio);
alter table registroexecucaojob add column total int;
alter table registroexecucaojob add column totalsucesso int;
alter table registroexecucaojob add column totalerro int;


-------------- pedro 26/10/2018 10:35  -----------------
DROP FUNCTION inserirnossonumero(codigoconta integer);

CREATE OR REPLACE FUNCTION inserirnossonumero(codigoconta integer, qtdPermitida integer)
  RETURNS  TABLE(sequencialnossonumero_codigo integer, anoboleto integer ) AS
$BODY$
DECLARE
 resultadoBusca RECORD;
 sequencialnossonumero_codigo integer;
 anoatual integer;
	BEGIN	
	 anoatual := extract(year from now());	 
	 while(anoboleto is null) loop
         select 
         case when t.numero >= t.qtdMaxima then null else anoatual end as anoboleto
             INTO	anoboleto 
         from( select numero, lpad(cast('' as varchar),qtdPermitida,'9')::bigint as qtdMaxima  from sequencialnossonumero where ano = anoatual) as t; 

	 if(anoboleto is null ) then
		anoatual := anoatual+1;
		if((select numero from sequencialnossonumero where ano = anoatual) is null) then
			anoboleto := anoatual;
		end if;		
	 END IF;
	 end loop;
	 
	 if((select numero from sequencialnossonumero where ano = anoboleto) is null or anoboleto is null ) then
		INSERT INTO sequencialnossonumero (numero, ano) values (0, anoboleto);
	 END IF;
	 UPDATE sequencialnossonumero set numero = numero+1 where ano = anoboleto;	 
	 UPDATE contareceber set sequencialnossonumero = (select numero from sequencialnossonumero where ano = anoboleto) where codigoconta = contareceber.codigo;
	 for resultadoBusca in (select sequencialnossonumero from contareceber where codigoconta = contareceber.codigo)	 loop 
	 sequencialnossonumero_codigo := resultadoBusca.sequencialnossonumero;
	 end loop;	
	 return query select sequencialnossonumero_codigo as sequencialnossonumero_codigo, anoboleto as anoboleto;
	END 
	$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER FUNCTION inserirnossonumero(integer, integer)
  OWNER TO postgres;
  
  
  --------------------- Rodrigo 30/10/2018 08:39 ---------------
  create index idx_avalOnlineMatrRespQuestao_avalOnlineMatQuestao on avaliacaoOnlineMatriculaRespostaQuestao(avaliacaoOnlineMatriculaQuestao);
  
  --------------------- Rodrigo 30/10/2018 10:57 7.0.2.0 ---------------
  update requisicaoitem  set cotacao  =  null where cotacao = 0;
alter table requisicaoitem add constraint fk_requisicaoitem_cotacao foreign key (cotacao) references cotacao(codigo);


--------------------- Gilberto Nery 30/10/2018 17:28 7.0.1 ---------------
alter table templatelancamentofolhapagamento add column lancarPensao boolean null; 
--------------------- Gilberto Nery 01/11/2018 10:16 7.0.1 ---------------
alter table eventofolhapagamento add column permiteDuplicarContraCheque boolean null; 
--------------------- Renato Borges 31/10/2018 15:58 7.0.1 ---------------
ALTER TABLE public.funcionariocargo ADD COLUMN salariocomposto boolean;
------------------- Rodrigo Wind 03/11/2018 08:45 7.0.1 -------------------
alter table cursocoordenador  add column tipocoordenadorcurso varchar(20) default 'GERAL';
alter table avaliacaoinstitucional   add column tipocoordenadorcurso varchar(20) default 'GERAL';

create table avaliacaoinstitucionalcurso(
	codigo serial,
	curso int not null,
	avaliacaoinstitucional int not null,

	constraint pk_avaliacaoinstitucionalcurso primary key (codigo),
	constraint fk_avaliacaoinstitucionalcurso_curso foreign key (curso) references curso(codigo),
	constraint fk_avaliacaoinstitucionalcurso_avaliacaoinstitucional foreign key (avaliacaoinstitucional) references avaliacaoinstitucional(codigo),
	constraint unq_avaliacaoinstitucionalcurso_avalinst_curso unique (avaliacaoinstitucional, curso)
);
 create index idx_avaliacaoinstitucionalcurso_curso on avaliacaoinstitucionalcurso(curso);
 create index idx_avaliacaoinstitucionalcurso_avalinst on avaliacaoinstitucionalcurso(avaliacaoinstitucional);
 create index idx_avaliacaoinstitucionalcurso_avalinst_curso on avaliacaoinstitucionalcurso(avaliacaoinstitucional, curso);
 
 
 insert into avaliacaoinstitucionalcurso  (avaliacaoinstitucional, curso) (
	select codigo, curso from avaliacaoinstitucional
	where curso is not null and turma is null
	and not exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where
	avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo 
	and avaliacaoinstitucionalcurso.curso = avaliacaoinstitucional.curso)
);

update avaliacaoinstitucional set curso =  null where turma is null and exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso where
	avaliacaoinstitucionalcurso.avaliacaoinstitucional = avaliacaoinstitucional.codigo 
	and avaliacaoinstitucionalcurso.curso = avaliacaoinstitucional.curso);
	
	alter table avaliacaoinstitucional  add column tipoFiltroProfessor varchar(50) default 'todos';
update avaliacaoinstitucional set tipoFiltroProfessor = 'turma' where turma is not null;
update avaliacaoinstitucional set tipoFiltroProfessor = 'curso' where exists (select avaliacaoinstitucionalcurso.codigo from avaliacaoinstitucionalcurso  
where avaliacaoinstitucional.codigo = avaliacaoinstitucionalcurso.avaliacaoinstitucional );

create index idx_numerodocumentocontareceber_pessoa on numerodocumentocontareceber (pessoa);
create index idx_numerodocumentocontareceber_parceiro on numerodocumentocontareceber (parceiro);

------------------- Cesar 06/11/2018 10:26 7.0.1 -------------
ALTER TABLE itememprestimo ADD COLUMN motivoisencao TEXT;

--------------------- Renato Borges 07/11/2018 08:25 7.0.1 ---------------
ALTER TABLE public.afastamentofuncionario ADD COLUMN quantidadediasafastado integer;
ALTER TABLE public.competenciafolhapagamento DROP COLUMN descricao;	
ALTER TABLE public.competenciafolhapagamento ADD COLUMN quantidadediasuteismeioexpediente integer;

--------------------- Pedro Andrade 07/11/2018 10:05 ---------------
ALTER TABLE horarioturmadiaitem DROP CONSTRAINT horarioturmadiaitem_uniq;
alter table horarioprofessordiaitem  add column usuarioliberacaochoquehorario integer;
ALTER TABLE horarioprofessordiaitem ADD CONSTRAINT fk_horarioprofessordiaitem_usuarioliberacaochoquehorario FOREIGN KEY (usuarioliberacaochoquehorario)
      REFERENCES usuario (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;

CREATE OR REPLACE FUNCTION fn_validarexistenciahorarioprofessordiaitem(integer, integer, integer, integer, integer, integer)
  RETURNS boolean AS
$BODY$
DECLARE 
 quantidadeRegistro integer;
begin
	 if ($6 is null) then
	     select into quantidadeRegistro count(codigo) from horarioprofessordiaitem  where nraula = $2 and horarioprofessordia = $3 and codigo != $1 and usuarioliberacaochoquehorario is null;
	 else
	     select into quantidadeRegistro count (codigo) from horarioprofessordiaitem  where nraula = $2 and horarioprofessordia = $3 and disciplina = $4 and turma = $5  and codigo != $1;	     
	 end if;

	  IF quantidadeRegistro > 0 THEN	        
		RETURN   false;
	  ELSE
		RETURN   true;
	  END IF;   
end 
$BODY$
 LANGUAGE plpgsql VOLATILE COST 100;
ALTER FUNCTION fn_validarexistenciahorarioprofessordiaitem(integer, integer, integer, integer, integer, integer) OWNER TO postgres;
	  
ALTER TABLE horarioprofessordiaitem ADD CONSTRAINT check_horarioprofessordiaitem_usuarioliberacaochoquehorario CHECK (
 fn_validarexistenciahorarioprofessordiaitem(codigo::int, nraula::int, horarioprofessordia::int, disciplina::int, turma::int, usuarioliberacaochoquehorario::int) 
) NOT VALID;
  
  
UPDATE horarioprofessordiaitem set usuarioliberacaochoquehorario = 1 from (
  select nraula as nraula_duplicado, horarioprofessordia as horarioprofessordia_duplicado from horarioprofessordiaitem 
  where horarioprofessordia not in (
	select
		distinct horarioprofessordia
		from horarioprofessordiaitem 
		group by 
		nraula, horarioprofessordia , turma , disciplina
		having count(1)> 1
  )
  group by 
  nraula, horarioprofessordia having count(1)> 1
) as t where t.nraula_duplicado = nraula and t.horarioprofessordia_duplicado = horarioprofessordia and usuarioliberacaochoquehorario is null;

--------------------- Pedro Andrade 08/11/2018 11:15 ---------------
alter table nossonumerocontareceber add column contacorrente integer;
ALTER TABLE nossonumerocontareceber
  ADD CONSTRAINT fk_nossonumerocontareceber_contacorrente FOREIGN KEY (contacorrente)
      REFERENCES contacorrente (codigo) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT;
	  
UPDATE nossonumerocontareceber set contacorrente = (
select contacorrente from contareceber where contareceber.nossonumero = nossonumerocontareceber.nossonumero and contareceber.matriculaperiodo = nossonumerocontareceber.matriculaperiodo
) where nossonumerocontareceber.contacorrente is null;
	  
--------------------- Hyllner Valadares da Silva 09/11/2018 08:45 ---------------	  
ALTER TABLE public.funcionario ADD COLUMN operacaobancaria varchar(05);	  

--------------------- Hyllner Valadares da Silva 19/11/2018 15:5 ---------------	
ALTER TABLE public.contareceber ADD COLUMN contaGeradaAPartirValorMulta_PHL boolean;

------------- Thyago 16:10 7.0.2.0 -----------
alter table formacaoacademica  add column semestredatafim varchar(2);

--------------------- Pedro Andrade 26/11/2018 11:58 ---------------	
alter table pessoa  add column senhacertificadoparadocumento VARCHAR(20);

CREATE TABLE documentoassinadopessoa (
	codigo serial NOT NULL,
	dataSolicitacao timestamp not null,
	dataAssinatura timestamp,
	dataRejeicao timestamp,
	pessoa integer,
	documentoassinado integer not null,
	tipoPessoa VARCHAR(100) not null,
	situacaoDocumentoAssinadoPessoa VARCHAR(100) not null,
	motivoRejeicao VARCHAR(255),
	CONSTRAINT documentoassinadopessoa_pkey PRIMARY KEY (codigo),
	CONSTRAINT fk_documentoassinadopessoa_documentoassinado FOREIGN KEY (documentoassinado) REFERENCES documentoassinado (codigo) MATCH SIMPLE ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT documentoassinadopessoa_pessoa_fkey FOREIGN KEY (pessoa) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
) WITH (
  OIDS=FALSE
);

ALTER TABLE documentoassinado ALTER COLUMN ano TYPE character varying(4);
alter table documentoassinado add column unidadeensino integer;
ALTER TABLE documentoassinado ADD CONSTRAINT fk_documentoassinado_unidadeensino FOREIGN KEY (unidadeensino) REFERENCES unidadeensino (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;

update documentoassinado set unidadeensino = (select unidadeensino from matricula where matricula.matricula = documentoassinado.matricula)  where documentoassinado.unidadeensino is null and documentoassinado.matricula is not null;

--------------------- Leandro 26/11/2018 17:30 ---------------
ALTER TABLE pessoa 
	ADD COLUMN estadoCivilFiador varchar(01),
	ADD COLUMN dataNascimentoFiador date,
	ADD COLUMN profissaoFiador varchar(100),
	ADD COLUMN rgFiador varchar(50),
	ADD COLUMN paisFiador integer;

ALTER TABLE pessoa ADD 
  constraint fk_pessoa_paisFiador foreign key (paisFiador) references paiz(codigo) ;

------------ rodrigo 29/11/2018 16:49 -------------
alter table centroresultadoorigem  alter column porcentagem type numeric(20,8);
------------ Pedro Andrade 29/11/2018 17:49 -------------
alter table layoutintegracao add column terminaInstrucaoComDelimitador boolean default false;
alter table layoutintegracao add column valorprefixo VARCHAR(100);
alter table layoutintegracao add column valorsufixo VARCHAR(100);

-------- Alessandro 30/11/2018 10:00 --------
alter table configuracaogeralsistema add column urlwebservicenfse text;

alter table notafiscalsaida add column naturezaoperacaoenum text;
alter table notafiscalsaida add column isincentivadorcultural boolean;
alter table notafiscalsaida add column codigocnae text;
alter table notafiscalsaida add column numerorps integer;

alter table configuracaonotafiscal add column naturezaoperacaoenum text;
alter table configuracaonotafiscal add column isincentivadorcultural boolean;
alter table configuracaonotafiscal add column codigocnae text;
alter table configuracaonotafiscal add column serie text;

ALTER TABLE notafiscalsaida DROP CONSTRAINT check_numeronota_serie_ja_existente;
DROP FUNCTION fn_validarnumeroserienotajaexistente(codigo_nota integer, numero_nota integer, serie_nota character, unidadeensino_nota integer);
CREATE OR REPLACE FUNCTION fn_validarnumeroserienotajaexistente(
    codigo_nota integer,
    numero_nota bigint,
    serie_nota character,
    unidadeensino_nota integer)
  RETURNS boolean AS
$BODY$
DECLARE
	quantidaderegistros integer;
BEGIN
IF (numero_nota is null or numero_nota = 0 )
then return true ;
END IF;
  select into quantidaderegistros count(notafiscalsaida.codigo)	
  from notafiscalsaida
	inner join unidadeensino on unidadeensino.codigo = notafiscalsaida.unidadeensino
	inner join configuracaonotafiscal on configuracaonotafiscal.codigo = unidadeensino.configuracaonotafiscal
  where notafiscalsaida.numero = numero_nota
  and notafiscalsaida.serie = serie_nota
  and notafiscalsaida.codigo <> codigo_nota
  and configuracaonotafiscal.codigo in (select configuracaonotafiscal from unidadeensino where codigo = unidadeensino_nota);
RETURN quantidaderegistros = 0 ;
END;  
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
ALTER TABLE notafiscalsaida ADD CONSTRAINT check_numeronota_serie_ja_existente CHECK (fn_validarnumeroserienotajaexistente(codigo, numero, serie::bpchar, unidadeensino)) NOT VALID;
alter table notafiscalsaida alter column numero type bigint;


--------------- Rodrigo  30/11/2018 12:57 7.0.1 --------------------
alter table itemcondicaorenegociacao  add column qtdeDiasEntrada int default 0;
alter table itemcondicaorenegociacao  add column gerarParcelas30DiasAposDataEntrada boolean default true;

--------------- Pedro Andrade  30/11/2018 17:24  --------------------
alter table integracaocontabil add column tipogeracaointegracaocontabil VARCHAR(100);
alter table integracaocontabil add column codigointegracaocontabil VARCHAR(100);
alter table integracaocontabil ALTER COLUMN unidadeensino DROP NOT NULL;
update integracaocontabil set tipogeracaointegracaocontabil = 'UNIDADE_ENSINO' where unidadeensino is not null and tipogeracaointegracaocontabil is null;
alter table loteunidadeensinocontabil add column codigointegracaocontabil VARCHAR(100);
ALTER TABLE layoutintegracaotag ALTER COLUMN campo TYPE text;

---------- Rodrigo Wind 03/12/2018 10:46 7.0.1 -------
alter table configuracaogeralsistema alter column localUploadArquivoGED type varchar(255);

---------- Renato Borges Cardoso Wind 03/12/2018 10:46 7.0.1 -------
ALTER TABLE public.eventoemprestimocargofuncionario ADD COLUMN quitado boolean default false;
ALTER TABLE public.eventoemprestimocargofuncionario ADD COLUMN datapagamento timestamp with time zone;

--------------------Cesar Henrique 03/12/2018 ---------------------------------------------------------

ALTER TABLE ProcSeletivo ADD COLUMN tipoProcessoSeletivo boolean;
ALTER TABLE ProcSeletivo ADD COLUMN tipoEnem boolean;
ALTER TABLE ProcSeletivo ADD COLUMN tipoPortadorDiploma boolean;
ALTER TABLE ProcSeletivo ADD COLUMN uploadComprovanteEnem boolean;
ALTER TABLE ProcSeletivo ADD COLUMN uploadComprovantePortadorDiploma boolean;
ALTER TABLE ProcSeletivo ADD COLUMN orientacaoUploadEnem varchar(280);
ALTER TABLE ProcSeletivo ADD COLUMN orientacaoUploadPortadorDiploma varchar(280);
ALTER TABLE inscricao ADD COLUMN formaIngresso varchar(5);
UPDATE inscricao SET  formaingresso = 'PS';
UPDATE ProcSeletivo SET  tipoProcessoSeletivo = 'true' ,tipoEnem = 'false' , tipoPortadorDiploma = 'false';

----------------------------------------------------------------------------------------------------------

---- Alessandro 04/12/2018 ----
ALTER TABLE fluxocaixa DROP CONSTRAINT contacaixa_aberta;
ALTER TABLE fluxocaixa ADD CONSTRAINT contacaixa_aberta CHECK (validar_contacaixa_aberta(situacao::text, contacaixa, codigo)) NOT VALID;


--------------- Pedro Andrade  04/12/2018 11:30  --------------------
create table situacaocomplementarhistorico (
	codigo serial not null,
	nome varchar(255) not null,
	sigla varchar(4) not null,
	situacaohistorico varchar(100) not null,
	constraint situacaocomplementarhistorico_pkey primary key (codigo)
) with (
  oids=false
);


------------- Rodrigo 6.25.14 05/12/2018 15:35 ------
update configuracaoacademico  set reprovadomatriculardisciplinaperiodoletivo  = true where numeroDisciplinaConsiderarReprovadoPeriodoLetivo >0 and reprovadomatriculardisciplinaperiodoletivo = false;

--------------- Pedro Andrade  05/12/2018 11:30  --------------------
ALTER TABLE periodoletivo ADD COLUMN numeroMinimoCreditoAlunoPodeCursar integer;
ALTER TABLE periodoletivo ADD COLUMN numeroMinimoCargaHorariaAlunoPodeCursar integer;

---- Alessandro 06/12/2018 18:00 ----
update configuracaonotafiscal set serie = '1' where serie is null;

--------------- Pedro Andrade  07/12/2018 08:30  --------------------
alter table contacorrente add column tipocontacorrente varchar(100);
update contacorrente set tipocontacorrente = 'CAIXA' where contacaixa = true and tipocontacorrente is null;
update contacorrente set tipocontacorrente = 'CORRENTE' where contacaixa is not true and tipocontacorrente is null;

--------------- Marcos Paulo  07/12/2018 09:48  --------------------
alter table contarecebernaolocalizadaarquivoretorno add column contacorrente integer;

--------------- Pedro Andrade  11/12/2018 10:30  --------------------
alter table planodescontocontareceber add column utilizarAvancoDiaUtil boolean default false;
alter table planodesconto add column utilizarAvancoDiaUtil boolean default false;

--------------- Renato Borges  11/12/2018 16:44  --------------------
ALTER TABLE public.eventofolhapagamento ADD COLUMN categoriadespesa integer;
ALTER TABLE public.eventofolhapagamento ADD CONSTRAINT eventofolhapagamento_categoriadespesa_fkey FOREIGN KEY (categoriadespesa)
    REFERENCES public.categoriadespesa (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
	
	
ALTER TABLE public.configuracaofinanceiro ADD COLUMN categoriadespesa integer;
ALTER TABLE public.configuracaofinanceiro ADD CONSTRAINT configuracaofinanceiro_categoriadespesa_fkey FOREIGN KEY (categoriadespesa)
    REFERENCES public.categoriadespesa (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
	
	
	------------- Rodrigo 12/12/18 08:36 7.0.2.0 ------------------
	update contarecebernaolocalizadaarquivoretorno set contacorrente = t.contacorrente from (
select contarecebernaolocalizadaarquivoretorno.codigo,  controlecobranca.contacorrente 
from contarecebernaolocalizadaarquivoretorno  
inner join contareceberregistroarquivo on contareceberregistroarquivo.nossonumero = contarecebernaolocalizadaarquivoretorno.nossonumero
inner join registroarquivo on contareceberregistroarquivo.registroarquivo = registroarquivo.codigo
inner join controlecobranca on controlecobranca.registroarquivo = registroarquivo.codigo
where contarecebernaolocalizadaarquivoretorno.contacorrente is null
) as t where t.codigo = contarecebernaolocalizadaarquivoretorno.codigo;


--------------- Marcos Paulo  12/12/2018 16:27  --------------------
ALTER TABLE configuracaogeralsistema ADD COLUMN siglaAbonoFalta varchar(2);
ALTER TABLE configuracaogeralsistema ADD COLUMN descricaoAbonoFalta text;

---------- Thyago 6.26.5 31/07/2018 09:40 ---------
alter table layoutpadrao  add column titulorelatorio varchar(30);

---- Alessandro 13/12/2018 18:00 ----
CREATE OR REPLACE FUNCTION elastic.agendar_sync_pg_es(
    m text,
    u text,
    c json)
  RETURNS void AS
$BODY$
	DECLARE
		var_returned_sqlstate text;
		var_column_name text;
		var_constraint_name text;
		var_pg_datatype_name text;
		var_message_text text;
		var_table_name text;
		var_schema_name text;
		var_exception_detail text;
		var_exception_hint text;
		var_exception_context text;
	BEGIN
		insert into elastic.fila (method, uri, content) values (m, u, c);
	EXCEPTION WHEN OTHERS THEN
		get stacked diagnostics
			var_returned_sqlstate = RETURNED_SQLSTATE,
			var_column_name = COLUMN_NAME,
			var_constraint_name = CONSTRAINT_NAME,
			var_pg_datatype_name = PG_DATATYPE_NAME,
			var_message_text = MESSAGE_TEXT,
			var_table_name = TABLE_NAME,
			var_schema_name = SCHEMA_NAME,
			var_exception_detail = PG_EXCEPTION_DETAIL,
			var_exception_hint = PG_EXCEPTION_HINT,
			var_exception_context = PG_EXCEPTION_CONTEXT;
		insert into elastic.fila_exception (origin, returned_sqlstate, column_name, constraint_name, pg_datatype_name, message_text, table_name, schema_name, exception_detail, exception_hint, exception_context) values (current_query(), var_returned_sqlstate, var_column_name, var_constraint_name, var_pg_datatype_name, var_message_text, var_table_name, var_schema_name, var_exception_detail, var_exception_hint, var_exception_context);
	END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
CREATE OR REPLACE FUNCTION elastic.agendar_alunos()
  RETURNS void AS
$BODY$
	DECLARE
		r record;
	BEGIN
		FOR r IN select p.codigo, to_json(p) conteudo from (
			select a.codigo, ar.cpfrequerimento||'/'||ar.nome foto, trim(a.nome) nome, elastic.filtrar_aphanumeric(array_to_string(array_agg(m.matricula),', ')) matriculas, trim(elastic.filtrar_aphanumeric(a.cpf)) cpf, trim(a.email) email,
			coalesce((select array_agg(p2) from (
				select trim(pf.nome) nome, trim(elastic.filtrar_aphanumeric(pf.cpf)) cpf, trim(pf.email) email, f.tipo, f.responsavelfinanceiro from filiacao f
				inner join pessoa pf on pf.codigo = f.pais
				where f.aluno = a.codigo
			) p2),'{}'::record[]) filiacao
			from pessoa a
			left join matricula m on m.aluno = a.codigo
			left join arquivo ar on ar.codigo = a.arquivoimagem
			where a.aluno
			group by a.codigo, ar.cpfrequerimento, ar.nome
		) p
		LOOP
			perform elastic.agendar_sync_pg_es('post', (select elastic.consultar_indice('aluno')||r.codigo), r.conteudo);
		END LOOP;
		return;
	END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
CREATE OR REPLACE FUNCTION elastic.montar_query_aluno(
    text,
    integer)
  RETURNS json AS
$BODY$
	select ('{
	"query":{
		"bool":{
			"should":[
				{"match":{"nome":{"query":"'||(select trim(lower(unaccent($1))))||'","boost":4,"fuzziness":"AUTO"}}},
				{"match":{"matriculas":{"query":"'||(select trim(lower(unaccent($1))))||'","boost":2,"fuzziness":"AUTO"}}},
				{"match":{"cpf":{"query":"'||(select trim(lower(unaccent($1))))||'","boost":2,"fuzziness":"AUTO"}}},
				{"match":{"email":{"query":"'||(select trim(lower(unaccent($1))))||'","boost":2,"fuzziness":"AUTO"}}},
				{"match":{"filiacao.nome":{"query":"'||(select trim(lower(unaccent($1))))||'","boost":1.5,"fuzziness":"AUTO"}}},
				{"match":{"filiacao.cpf":{"query":"'||(select trim(lower(unaccent($1))))||'","boost":1.5,"fuzziness":"AUTO"}}},
				{"match":{"filiacao.email":{"query":"'||(select trim(lower(unaccent($1))))||'","boost":1.5,"fuzziness":"AUTO"}}}]
		}
	},
	"size":"'||$2||'",
	"highlight":{
		"pre_tags":["<b>"],
		"post_tags":["</b>"],
		"fields":{
			"nome":{},
			"matriculas":{},
			"cpf":{},
			"email":{},
			"filiacao.nome":{},
			"filiacao.cpf":{},
			"filiacao.email":{}
		}
	}
}')::json;
$BODY$
  LANGUAGE sql VOLATILE
  COST 100;

  
  ------------- Thyago 16:10 7.0.2.0 -----------
alter table formacaoacademica  add column semestredatafim varchar(2);

---------- Rodrigo 14/12/2018 16:43 7.0.1.0 -------------------
insert into condicaorenegociacaounidadeensino(condicaorenegociacao, unidadeensino, contacorrente) (
select condicaorenegociacao.codigo, condicaorenegociacao.unidadeensino, condicaorenegociacao.contacorrentepadrao from condicaorenegociacao   
where not exists (	
	select codigo from condicaorenegociacaounidadeensino where condicaorenegociacao.codigo = condicaorenegociacaounidadeensino.condicaorenegociacao
)
);

------------- Marcos Paulo 6.25.14 17/12/2018 08:47 ------
update RespostaAvaliacaoInstitucionalDW set resposta = t.nova_resposta from (
     select 
       RespostaAvaliacaoInstitucionalDW.codigo as RespostaAvaliacaoInstitucionalDW,RespostaAvaliacaoInstitucionalDW.resposta,
       substring(RespostaAvaliacaoInstitucionalDW.resposta,1,position(']' in RespostaAvaliacaoInstitucionalDW.resposta)) as nova_resposta
     FROM RespostaAvaliacaoInstitucionalDW
     LEFT JOIN UnidadeEnsino ON UnidadeEnsino.codigo = RespostaAvaliacaoInstitucionalDW.unidadeEnsino
     LEFT JOIN Curso ON Curso.codigo = RespostaAvaliacaoInstitucionalDW.curso
     LEFT JOIN Disciplina ON Disciplina.codigo = RespostaAvaliacaoInstitucionalDW.disciplina
     LEFT JOIN AvaliacaoInstitucional ON AvaliacaoInstitucional.codigo = RespostaAvaliacaoInstitucionalDW.avaliacaoInstitucional
     LEFT JOIN Questionario ON Questionario.codigo = RespostaAvaliacaoInstitucionalDW.questionario
     LEFT JOIN Pergunta ON Pergunta.codigo = RespostaAvaliacaoInstitucionalDW.pergunta

     where pergunta.tiporesposta = 'SE'
     and RespostaAvaliacaoInstitucionalDW.resposta <> substring(RespostaAvaliacaoInstitucionalDW.resposta,1,position(']' in RespostaAvaliacaoInstitucionalDW.resposta)) 
) as t where RespostaAvaliacaoInstitucionalDW.codigo = t.RespostaAvaliacaoInstitucionalDW;


---------- thyago 12/12/2018 15:40 7.0.2.0 -------------
alter table contareceberregistroarquivo  add column contarecebidanegociada boolean default false;

--------------------- Pedro Andrade 07/11/2018 10:05 ---------------
ALTER TABLE nossonumerocontareceber ALTER COLUMN matriculaperiodo DROP NOT NULL;

--------- Renato Borges 28/12/2018 10:48 7.0.1 ------------
ALTER TABLE public.configuracaofinanceiro ADD COLUMN categoriadespesa integer;
ALTER TABLE public.configuracaofinanceiro ADD CONSTRAINT configuracaofinanceiro_categoriadespesa_fkey FOREIGN KEY (categoriadespesa)
    REFERENCES public.categoriadespesa (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
	
ALTER TABLE public.configuracaofinanceiro ADD COLUMN bancopadraoremessa integer;
ALTER TABLE public.configuracaofinanceiro ADD CONSTRAINT configuracaofinanceiro_bancopadraoremessa_fkey FOREIGN KEY (bancopadraoremessa)
    REFERENCES public.banco (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
	
ALTER TABLE public.reciboferiasevento ADD COLUMN valorreferencia numeric(20, 2);
	
ALTER TABLE public.configuracaofinanceiro ADD COLUMN formapagamentopadrao integer;
ALTER TABLE public.configuracaofinanceiro ADD CONSTRAINT configuracaofinanceiro_formapagamentopadrao_fkey FOREIGN KEY (formapagamentopadrao)
    REFERENCES public.formapagamento (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
	
--------------- Marcos Paulo  02/01/2019 11:21  --------------------
update prospects set sexo = '' where sexo = 'NENHUM';

---------- Ana Claudia 02/01/2019 17:18 7.0.1 ------------------
ALTER TABLE configuracaogeralsistema ADD COLUMN permiteReativacaoMatriculaSemRequerimento BOOLEAN;

---------- Renato Borges Cardoso 03/01/2019 11:00 7.0.1 ------------------
ALTER TABLE public.controlelancamentofolhapagamento ADD COLUMN contapagar integer;
ALTER TABLE public.controlelancamentofolhapagamento ADD CONSTRAINT contapagar_fkey FOREIGN KEY (contapagar)
    REFERENCES public.contapagar (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;
	
ALTER TABLE contracheque DROP COLUMN IF EXISTS contapagar;	

----------------- Leandro 04/01/2019 09:15 ---------------------
ALTER TABLE notafiscalentradaimposto ALTER COLUMN porcentagem TYPE numeric(20,8);

----------------- Marcos Paulo 10/01/2019 11:29 ---------------------
CREATE OR REPLACE FUNCTION public.fn_verificarContemParcelaNegociadaRecebida ( v_contareceberorigem integer, v_validarTodasRecebidas boolean )
 RETURNS boolean
 LANGUAGE plpgsql
 STABLE
AS $function$
 DECLARE
  v_contemParcelaPaga  boolean := false;
  listaContaReceber    record;
		
BEGIN
      FOr listaContaReceber IN 
       SELECT 
          contanegociada.codigo , contanegociada.situacao
       FROM contareceber AS contanegociada           
         WHERE contanegociada.tipoorigem = 'NCR'
         AND contanegociada.codorigem = (select contarecebernegociado.negociacaocontareceber::varchar  from contarecebernegociado  where contarecebernegociado.contareceber = v_contareceberorigem )
       order by case contanegociada.situacao when 'RE' then 1 when 'AR' then 2 when 'NE' then 3 else 4 END
      
      LOOP
        if (listaContaReceber.situacao = 'RE'    and v_validarTodasRecebidas = false) then
         return true;
        ELSIF (listaContaReceber.situacao = 'RE' and v_validarTodasRecebidas = TRUE)  then
         v_contemParcelaPaga := true;
        ELSIF (listaContaReceber.situacao = 'AR' and v_validarTodasRecebidas = TRUE)  then
         return false;
        ELSIF (listaContaReceber.situacao = 'NE') then 
         v_contemParcelaPaga := fn_verificarContemParcelaNegociadaRecebida(listaContaReceber.codigo, v_validarTodasRecebidas);
          if (v_contemParcelaPaga = true and v_validarTodasRecebidas = false)  then 
            return true;
          end if;
        end if;  
      END LOOP;
  RETURN v_contemParcelaPaga;
END;
$function$;

 ----------------- Marcos Paulo 10/01/2019 16:17 ---------------------

ALTER TABLE contareceber DROP CONSTRAINT unique_contareceber_nossonumero;
ALTER TABLE contareceber ADD CONSTRAINT unique_contareceber_nossonumero_contacorrente UNIQUE(nossonumero, contacorrente);

ALTER TABLE nossonumerocontareceber DROP CONSTRAINT unique_nossonumerocontareceber_nossonumero;
ALTER TABLE nossonumerocontareceber ADD CONSTRAINT unique_nossonumerocontareceber_nossonumero_contacorrente UNIQUE(nossonumero, contacorrente);

---------- Ana Claudia 11/01/2019 15:11 7.0.1 ------------------
alter table turma  add column digitoTurma character varying(1);

---------- Jean Pierre 14/01/2019 14:42 ------------------------------
alter table transferenciaturma add primary key (codigo);
alter table transferenciaturma add CONSTRAINT fk_transferenciaturma_turmaorigem         FOREIGN KEY (turmaorigem) REFERENCES turma (codigo) MATCH SIMPLE  ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table transferenciaturma add CONSTRAINT fk_transferenciaturma_turmadestino        FOREIGN KEY (turmadestino) REFERENCES turma (codigo) MATCH SIMPLE  ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table transferenciaturma add CONSTRAINT fk_transferenciaturma_matricula           FOREIGN KEY (matricula) REFERENCES matricula (matricula) MATCH SIMPLE  ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table transferenciaturma add CONSTRAINT fk_transferenciaturma_disciplina          FOREIGN KEY (disciplina) REFERENCES disciplina (codigo) MATCH SIMPLE  ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table transferenciaturma add CONSTRAINT fk_transferenciaturma_usuario             FOREIGN KEY (usuario) REFERENCES usuario (codigo) MATCH SIMPLE  ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table transferenciaturma add CONSTRAINT fk_transferenciaturma_turmapraticaorigem  FOREIGN KEY (turmapraticaorigem) REFERENCES turma (codigo) MATCH SIMPLE  ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table transferenciaturma add CONSTRAINT fk_transferenciaturma_turmapraticadestino FOREIGN KEY (turmapraticadestino) REFERENCES turma (codigo) MATCH SIMPLE  ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table transferenciaturma add CONSTRAINT fk_transferenciaturma_turmateoricaorigem  FOREIGN KEY (turmateoricaorigem) REFERENCES turma (codigo) MATCH SIMPLE  ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table transferenciaturma add CONSTRAINT fk_transferenciaturma_turmateoricadestino FOREIGN KEY (turmateoricadestino) REFERENCES turma (codigo) MATCH SIMPLE  ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table solicitacaopgtoservicoacademico add CONSTRAINT fk_solicitacaopgtoservicoacademico_pessoapgtoservico FOREIGN KEY (pessoapgtoservico) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table inscricaoevento add CONSTRAINT fk_inscricaoevento_pessoainscricao FOREIGN KEY (pessoainscricao) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table professorcursoextensao add CONSTRAINT fk_professorcursoextensao_pessoaprofessorcursoextensao FOREIGN KEY (pessoaprofessorcursoextensao) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table pesquisadorconvidado add CONSTRAINT fk_pesquisadorconvidado_pessoa FOREIGN KEY (pessoa) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table aproveitamentodisciplina add CONSTRAINT fk_aproveitamentodisciplina_pessoa FOREIGN KEY (pessoa) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table inscricaocursoextensao add CONSTRAINT fk_inscricaocursoextensao_pessoainscricaocursoextensao FOREIGN KEY (pessoainscricaocursoextensao) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table contabil add CONSTRAINT fk_contabil_pessoa FOREIGN KEY (pessoa) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table fechamentofinanceiroconta add CONSTRAINT fk_fechamentofinanceiroconta_pessoa FOREIGN KEY (pessoa) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table membrocomunidade add CONSTRAINT fk_membrocomunidade_pessoa FOREIGN KEY (pessoa) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table contareceberregistroarquivo add CONSTRAINT fk_contareceberregistroarquivo_pessoa FOREIGN KEY (pessoa) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table autortrabalhosubmetido add CONSTRAINT fk_autortrabalhosubmetido_pessoaautortrabalhosubmetido FOREIGN KEY (pessoaautortrabalhosubmetido) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table questionarioaluno add CONSTRAINT fk_questionarioaluno_questionario  FOREIGN KEY (questionario) REFERENCES questionario (codigo) MATCH SIMPLE  ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table questionarioaluno add CONSTRAINT fk_questionarioaluno_aluno         FOREIGN KEY (aluno) REFERENCES pessoa (codigo) MATCH SIMPLE  ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table portadordiploma add CONSTRAINT fk_portadordiploma_aluno FOREIGN KEY (aluno) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table comunicacaointerna add CONSTRAINT fk_comunicacaointerna_aluno FOREIGN KEY (aluno) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table arquivo add CONSTRAINT fk_arquivo_professor FOREIGN KEY (professor) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table comunicacaointerna add CONSTRAINT fk_comunicacaointerna_professor FOREIGN KEY (professor) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table notificacaoregistroaulanota add CONSTRAINT fk_notificacaoregistroaulanota_professor FOREIGN KEY (professor) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table respostaavaliacaoinstitucionaldw add CONSTRAINT fk_respostaavaliacaoinstitucionaldw_professor FOREIGN KEY (professor) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table publicacaopesquisa add CONSTRAINT fk_publicacaopesquisa_professor FOREIGN KEY (autorprofessor) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
alter table comunicacaointerna add CONSTRAINT fk_comunicacaointerna_coordenador FOREIGN KEY (coordenador) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;

CREATE OR REPLACE FUNCTION fn_removerduplicidadepessoa (v_cpfexcluir text,v_cpfmanter text)
  RETURNS boolean AS
$BODY$
 DECLARE
  listaTabelaReferenciada      record;
  query_update                 text := '';
  codigopessoaexcluir          integer := 0;
  codigopessoamanter           integer := 0;
		
BEGIN

    	BEGIN
	  select 
	      pessoa.codigo  into codigopessoaexcluir
       from pessoa 
       where replace(replace(upper( replace(pessoa.cpf,' ','')  ),'.',''),'-','') = replace(replace(upper( replace(v_cpfexcluir,' ','')  ),'.',''),'-','');

       IF (codigopessoaexcluir IS NULL) THEN
         raise exception 'O CPF informado para exclusão não está cadastrado ! ';
	  END IF;	 
     
       select 
	     pessoa.codigo   into codigopessoamanter
       from pessoa 
       where replace(replace(upper( replace(pessoa.cpf,' ','')  ),'.',''),'-','') = replace(replace(upper( replace(v_cpfmanter,' ','')  ),'.',''),'-','');

       IF (codigopessoamanter IS NULL) THEN
          raise exception 'O CPF informado para manter não está cadastrado ! ';
	  END IF;	 
	END;

   for listaTabelaReferenciada in 
           select   
             att2.attname as colunareferenciada,cl.relname as "parent_table",att.attname as "parent_column",conname,table_references As tabelareferenciada
           from (
             select 
                  unnest(con1.conkey) as "parent", unnest(con1.confkey) as "child", con1.confrelid,  con1.conrelid, con1.conname,cl.relname as table_references
              from  pg_class cl
                inner  join pg_namespace ns    on cl.relnamespace = ns.oid
                inner  join pg_constraint con1 on con1.conrelid   = cl.oid
              where 1=1 
               and ns.nspname = 'public'
               and con1.contype = 'f'
             ) con
            inner  join pg_attribute att      on att.attrelid  = con.confrelid and att.attnum = con.child
            inner join pg_class cl            on cl.oid        = con.confrelid
            inner join pg_attribute att2      on att2.attrelid = con.conrelid and att2.attnum = con.parent
          where cl.relname = 'pessoa' --   and table_references = 'comunicacaointerna'
          loop
         query_update  :=  ' UPDATE  ' ||  listaTabelaReferenciada.tabelareferenciada::text ||  ' SET ' || listaTabelaReferenciada.colunareferenciada::text || ' =  ' || codigopessoamanter::text
            || ' WHERE ' ||  listaTabelaReferenciada.colunareferenciada::text || ' = ' || codigopessoaexcluir::text || ';-- EXECUTADO PELA FUNÇÃO fn_unificarCadastroPessoa' ;
            EXECUTE(query_update);
        end loop;
        
        execute('UPDATE contareceber SET  beneficiario = ' || codigopessoamanter::text || ' WHERE contareceber.tipoorigem = ''BCC'' ' || ' AND contareceber.beneficiario =  ' || codigopessoaexcluir::text || '; -- EXECUTADO PELA FUNÇÃO fn_unificarCadastroPessoa');
        execute('DELETE FROM pessoa WHERE pessoa.codigo = '   || codigopessoaexcluir::text || '; --EXECUTADO PELA FUNÇÃO fn_unificarCadastroPessoa');
  RETURN true;
END;
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;
 
 ---------- Ana Claudia 15/01/2019 15:18 7.0.2.0 ------------------  
  INSERT INTO permissao(
            tipopermissao, tituloapresentacao, 
            permissoes, nomeentidade, codperfilacesso, responsavel)
     (
select distinct 2 as tipopermissao, 'Permite Anexar Arquivo' as tituloApresentacao, '(0)(1)(2)(3)(9)(12)' as permissoes, 
'EntregaDocumentoPermiteAnexarArquivo' as nomeentidade, codperfilacesso, 1
from permissao 
 INNER JOIN perfilacesso ON permissao.codperfilacesso = perfilacesso.codigo
 where NOT EXISTS (SELECT 1 FROM permissao  where permissao.codperfilacesso = perfilacesso.codigo and permissao.nomeentidade = 'EntregaDocumentoPermiteAnexarArquivo'));
 
 --------- Rodrigo Wind 16/01/2019 10:18 7.0.2.0 ------------
 alter table historico  alter column nomedisciplina type varchar(255);
alter table historico  alter column instituicao type varchar(255);

--------- Rodrigo Wind 16/01/2019 11:50 7.0.1.0 ------------

ALTER TABLE contareceber DROP CONSTRAINT unique_contareceber_codorigem_tipoorigem_parcela_pessoa_matricu;

create unique index  unq_contareceber_codorigem_tipoorigem_parcela_pessoa_matricu on contareceber(codorigem, tipoorigem, parcela, pessoa, matriculaaluno) where tipoorigem != 'BCC' ;
create unique index  unq_contareceber_codorigem_tipoorigem_parcela_parceiro_matricu on contareceber(codorigem, tipoorigem, parcela, parceiro, matriculaaluno) 
where tipoorigem = 'BCC' and codigo > <USAR O CODIGO DO SCRIPT ABAIXO> ;

select max(codigo) from (
select max(contareceber.codigo) as codigo, matriculaaluno, array_to_string(array_agg('nosso numero: '||nossonumero||' - '||situacao||' - '||to_char(datavencimento, 'dd/MM/yyyy')), ' ,  ')  as parcelas
from contareceber 
where tipoorigem = 'BCC'
group by tipoorigem, codorigem, matriculaaluno, pessoa, parcela, parceiro having count(codigo) > 1
) as t;

--------- Marcos Paulo 17/01/2019 14:05 7.0.1.0 ------------
alter table transferenciaturnodisciplina add column disciplina integer;

---------- Jean Pierre 17/01/2019 16:25 ------------------------------
drop index idx_pessoa_perfileconomico;
drop index ch_pessoa_nome;
create index idx_pessoa_codigo_funcao_encript on public.pessoa (encript(codigo::varchar));
create index idx_pessoa_cpf_replace on public.pessoa (replace(replace(CPF,'.',''), '-',''));

----------------- Leandro 19/01/2019 11:45 ---------------------
ALTER TABLE planoensino ADD COLUMN professorResponsavel integer;
ALTER TABLE planoensino DROP CONSTRAINT unq_dis_cur_uni_ano_sem;
ALTER TABLE planoensino ADD CONSTRAINT unq_dis_cur_uni_ano_sem_prof UNIQUE(disciplina, curso, unidadeensino, ano, semestre, professorResponsavel);
ALTER TABLE planoensino ADD CONSTRAINT fk_planoensino_professorresponsavel FOREIGN KEY (professorresponsavel) REFERENCES pessoa (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;

---------- Pedro Andrade 21/01/2019 09:40 ------------------------------
ALTER TABLE turmaatualizacaodisciplinalog ALTER COLUMN data TYPE timestamp without time zone;

--------- Marcos Paulo 21/01/2019 16:43 7.0.1.0 ------------
alter table configuracaorecebimentocartaoonline add column permiterecebercontamaterialdidatico boolean;

--------- Renato Borges 22/01/2019 17:24 7.0.1.0 ------------
CREATE TABLE public.layoutrelatorioseidecidirfuncionariocargo(
    codigo serial NOT NULL,
    funcionariocargo integer NOT NULL,
    layoutrelatorioseidecidir integer NOT NULL,
    PRIMARY KEY (codigo),
    CONSTRAINT funcionariocargo_fkey FOREIGN KEY (funcionariocargo) REFERENCES public.funcionariocargo (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT layoutrelatorioseidecidir_fkey FOREIGN KEY (layoutrelatorioseidecidir) REFERENCES public.layoutrelatorioseidecidir (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH ( OIDS = FALSE );
ALTER TABLE public.layoutrelatorioseidecidirfuncionariocargo OWNER to postgres;

CREATE TABLE public.layoutrelatorioseidecidirperfilacesso(
    codigo serial NOT NULL,
    perfilacesso integer NOT NULL,
    layoutrelatorioseidecidir integer NOT NULL,
    PRIMARY KEY (codigo),
    CONSTRAINT perfilacesso_fkey FOREIGN KEY (perfilacesso) REFERENCES public.perfilacesso (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT layoutrelatorioseidecidir_fkey FOREIGN KEY (layoutrelatorioseidecidir) REFERENCES public.layoutrelatorioseidecidir (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
)
WITH ( OIDS = FALSE );
ALTER TABLE public.layoutrelatorioseidecidirperfilacesso OWNER to postgres;
---------- Jean Pierre 22/01/2019 16:25 ------------------------------
create index idx_indicereajusteperiodomatriculaperiodovencimento_matriculaperiodo on indicereajusteperiodomatriculaperiodovencimento (matriculaperiodo);
create index idx_indicereajusteperiodomatriculaperiodovencimento_indicereajusteperiodo on indicereajusteperiodomatriculaperiodovencimento (indicereajusteperiodo);

--------- Marcos Paulo 23/01/2019 15:05 7.0.1.0 ------------
alter table transferenciaturnodisciplina add CONSTRAINT fk_transferenciaturnodisciplina_disciplina FOREIGN KEY (disciplina)
 REFERENCES disciplina (codigo) MATCH SIMPLE  ON UPDATE RESTRICT ON DELETE RESTRICT;
 
--------- Pedro Andrade 23/01/2019 15:08 7.0.1.0 ------------
alter table public.notafiscalentrada add column dataregistro timestamp without time zone;
alter table public.notafiscalentrada add column usuariocadastro integer;
alter table public.notafiscalentrada add constraint notafiscalentrada_usuariocadastro_fkey foreign key (usuariocadastro) references public.usuario (codigo) match simple on update no action on delete no action;
UPDATE notafiscalentrada set dataregistro = dataentrada where dataregistro is null;
UPDATE notafiscalentrada set usuariocadastro = 1 where usuariocadastro is null;

update lancamentocontabil set dataCompensacao = t.dataemissao from (
select dataemissao, lancamentocontabil.codigo as codigo_lc from notafiscalentrada
inner join lancamentocontabil on lancamentocontabil.codorigem::int = notafiscalentrada.codigo and TipoOrigemLancamentoContabil = 'NOTA_FISCAL_ENTRADA' 
and lancamentocontabil.dataCompensacao != notafiscalentrada.dataemissao
) as t where t.codigo_lc = lancamentocontabil.codigo;

update lancamentocontabil set dataregistro = t.dataEntrada from (
select dataEntrada, lancamentocontabil.codigo as codigo_lc from notafiscalentrada
inner join lancamentocontabil on lancamentocontabil.codorigem::int = notafiscalentrada.codigo and TipoOrigemLancamentoContabil = 'NOTA_FISCAL_ENTRADA' 
and lancamentocontabil.dataregistro != notafiscalentrada.dataEntrada
) as t where t.codigo_lc = lancamentocontabil.codigo;

---------- Jean Pierre 28/01/2019 14:2 ------------------------------
create index idx_disciplinasaproveitadas_aproveitamentodisciplina on disciplinasaproveitadas (aproveitamentodisciplina);
create index idx_disciplinasaproveitadas_disciplina on disciplinasaproveitadas (disciplina);

---------- Ana Claudia 28/01/2019 15:02 7.0.2.0 ------------------  
ALTER TABLE contapagar ADD COLUMN codigonotafiscalentrada character varying(250);

---------- Ana Claudia 29/01/2019 14:58 7.0.2.0 ------------------ 
update contapagar set codigonotafiscalentrada  = codOrigem  where tipoorigem  = 'NE' and codigonotafiscalentrada is null;

update contapagar set codigonotafiscalentrada  = t.notafiscal  from (
	select contapagar.codigo, array_to_string(array_agg(notafiscalentrada), ', ') as notafiscal from contapagar 
	inner join notafiscalentradarecebimentocompra  on contapagar.tipoorigem  = ( 'RC')  and  notafiscalentradarecebimentocompra.recebimentocompra::varchar = contapagar.codorigem
	where codigonotafiscalentrada is null group by contapagar.codigo
)as t where t.codigo = contapagar.codigo;

update contapagar set codigonotafiscalentrada  = t.notafiscal  from (
	select contapagar.codigo, array_to_string(array_agg(notafiscalentrada), ', ') as notafiscal 
	from contapagar 
	inner join compra  on contapagar.tipoorigem  = ( 'CO')  and  compra.codigo::varchar = contapagar.codorigem
	inner join recebimentocompra  on  recebimentocompra.compra = compra.codigo
	inner join notafiscalentradarecebimentocompra  on  notafiscalentradarecebimentocompra.recebimentocompra = recebimentocompra.codigo
	where codigonotafiscalentrada is null group by contapagar.codigo
)as t where t.codigo = contapagar.codigo;

---- Alessandro 29/01/2019 17:10 7.0.1 ----
alter table configuracaobiblioteca add column possuiIntegracaoMinhaBiblioteca boolean not null default false;
alter table configuracaobiblioteca add column chaveAutenticacaoMinhaBiblioteca text;
alter table pessoa add column possuiCadastroMinhaBiblioteca boolean;

----------------- Leandro 30/01/2019 09:20 ---------------------
ALTER TABLE centroresultadoorigem ADD CHECK (coalesce(porcentagem,0) > 0 ) not valid;

-------------- Rodrigo Wind 30/01/2019 11:38 7.0.1.0 ------------------


create or replace function somaSemestre(ano varchar, semestre varchar, qtdesemestre bigint) returns varchar as
$BODY$
declare 
database DATE;
begin
	if(semestre = '1')then
		database := cast(ano||'-06'||'-30' as date);
	else
		database := cast(ano||'-12'||'-30' as date);
	end if;
	database := database+((qtdesemestre*6)||' month')::interval;	
	return extract(year from database)||'/'||case when extract(month from database) >= 7 then '2' else '1' end;

END
$BODY$
  LANGUAGE plpgsql VOLATILE
  COST 100;

  ALTER FUNCTION somaSemestre(varchar, varchar, bigint) OWNER TO postgres;

--------- Renato Borges 05/02/2019 11:00 7.0.1.0 ------------
CREATE TABLE public.layoutrelatorioseidecidirarquivo(
    codigo serial NOT NULL,
    arquivo integer NOT NULL,
    layoutrelatorioseidecidir integer NOT NULL,
    layoutrelatorioseidecidirsuperior integer,
    CONSTRAINT layoutrelatorioseidecidirarquivo_pkey PRIMARY KEY (codigo),
    CONSTRAINT arquivo_fkey FOREIGN KEY (arquivo)
        REFERENCES public.arquivo (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT layoutrelatorioseidecidir_fkey FOREIGN KEY (layoutrelatorioseidecidir)
        REFERENCES public.layoutrelatorioseidecidir (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
    CONSTRAINT layoutrelatorioseidecidirsuperior_fkey FOREIGN KEY (layoutrelatorioseidecidirsuperior) REFERENCES public.layoutrelatorioseidecidir (codigo) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION
) WITH (OIDS = FALSE)TABLESPACE pg_default;

ALTER TABLE public.layoutrelatorioseidecidirarquivo OWNER to postgres;

ALTER TABLE public.layoutrelatorioseidecidir ADD COLUMN subrelatorio boolean default false;
ALTER TABLE public.layoutrelatorioseidecidir ADD COLUMN sqlwhere text;
ALTER TABLE public.layoutrelatorioseidecidircampo ADD COLUMN utilizarParametroRelatorio boolean default false;
alter table layoutrelatorioseidecidirarquivo add column sqlwhere text;


----------- Thyago Jayme 24/01/2019 10:45 7.0.2.0 -----------
alter table contareceber add column dataLimitePrimeiraFaixaDescontos timestamp without time zone;
alter table contareceber add column dataLimiteSegundaFaixaDescontos timestamp without time zone;
alter table contareceber add column dataLimiteTerceiraFaixaDescontos timestamp without time zone;
alter table contareceber add column dataLimiteQuartaFaixaDescontos timestamp without time zone;

alter table nossoNumeroContaReceber add column dataLimitePrimeiraFaixaDescontos timestamp without time zone;
alter table nossoNumeroContaReceber add column dataLimiteSegundaFaixaDescontos timestamp without time zone;
alter table nossoNumeroContaReceber add column dataLimiteTerceiraFaixaDescontos timestamp without time zone;
alter table nossoNumeroContaReceber add column dataLimiteQuartaFaixaDescontos timestamp without time zone;

----------------- Marcos Paulo 07/02/2019 09:28 ---------------------
alter table unidadeensino alter column credenciamentoportaria type varchar (200);


--------- Cesar Henrique 07/02/2019 11:25 7.0.1.0 ------------------------------------


alter table configuracaobiblioteca add column tamanhoCodigoBarra integer default '7';
alter table exemplar add column desconsiderarReserva boolean;
create index idx_formapagamentonegociacaorecebimento_formapgnegcartaocredito on public.formapagamentonegociacaorecebimento (formapagamentonegociacaorecebimentocartaocredito);

--------------------------------- JEAN PIERRE 07/02/2019 15:18------------------------------------
 CREATE INDEX idx_extratocontacorrente_origemextratocontacorrente_origem_tipomovf ON public.extratocontacorrente (origemextratocontacorrente,codigoorigem,tipomovimentacaofinanceira);
 CREATE INDEX idx_extratocontacorrente_conciliacaocontacorrentediaextrato         ON public.extratocontacorrente (conciliacaocontacorrentediaextrato);
 CREATE INDEX extratocontacorrente_codigosacado 								  ON public.extratocontacorrente (codigosacado);
 
 -------------- Ana 07/02/2019 18:31 7.0.2.0 ---------------
 alter table UnidadeEnsinoContaCorrente add column usarPorDefaultMovimentacaoFinanceira boolean default false;
 
  -------------- Cesar 08/02/2019 09:39   ---------------
 alter table exemplar add column dataAquisicao date;
 
  -------------- Ana 08/02/2019 11:24 7.0.2.0 ---------------
alter table movimentacaofinanceira  add column unidadeensino int;
alter table movimentacaofinanceira  add constraint fk_movimentacaofinanceira_unidadeensino foreign key (unidadeensino) references unidadeensino(codigo);

update unidadeensinocontacorrente set usarpordefaultmovimentacaofinanceira = true from (
select unidadeensinocontacorrente.contacorrente, unidadeensinocontacorrente.unidadeensino from contacorrente
inner join unidadeensinocontacorrente on unidadeensinocontacorrente.contacorrente = contacorrente.codigo
where tipocontacorrente = 'CAIXA' and (usarpordefaultmovimentacaofinanceira is null or 
usarpordefaultmovimentacaofinanceira = false)
) 
as t where unidadeensinocontacorrente.contacorrente = t.contacorrente 
and unidadeensinocontacorrente.unidadeensino = t.unidadeensino;

update unidadeensinocontacorrente set usarpordefaultmovimentacaofinanceira = true from (
select  unidadeensinocontacorrente.contacorrente, min(unidadeensinocontacorrente.unidadeensino) as unidadeensino
from contacorrente  
inner join unidadeensinocontacorrente on unidadeensinocontacorrente.contacorrente = contacorrente.codigo
where tipocontacorrente != 'CAIXA' and utilizarremessa =  true and unidadeensinocontacorrente.unidadeensino = 27
and not exists (
select uecc.unidadeensino from unidadeensinocontacorrente uecc where uecc.contacorrente = contacorrente.codigo 
and 	usarpordefaultmovimentacaofinanceira

) 
group by unidadeensinocontacorrente.contacorrente
order by unidadeensinocontacorrente.contacorrente
) as t where unidadeensinocontacorrente.contacorrente = t.contacorrente 
and unidadeensinocontacorrente.unidadeensino = t.unidadeensino;

update unidadeensinocontacorrente set usarpordefaultmovimentacaofinanceira = true from (
select  unidadeensinocontacorrente.contacorrente, min(unidadeensinocontacorrente.unidadeensino) as unidadeensino
from contacorrente  
inner join unidadeensinocontacorrente on unidadeensinocontacorrente.contacorrente = contacorrente.codigo
where tipocontacorrente != 'CAIXA' and utilizarremessa =  true
and not exists (
select uecc.unidadeensino from unidadeensinocontacorrente uecc where uecc.contacorrente = contacorrente.codigo 
and 	usarpordefaultmovimentacaofinanceira
) 
group by unidadeensinocontacorrente.contacorrente
order by unidadeensinocontacorrente.contacorrente
) 
as t where unidadeensinocontacorrente.contacorrente = t.contacorrente 
and unidadeensinocontacorrente.unidadeensino = t.unidadeensino;

update movimentacaofinanceira set unidadeensino = t.unidadeensino from (
select movimentacaofinanceira.codigo, unidadeensinocontacorrente.unidadeensino 
from movimentacaofinanceira  
inner join contacorrente on contacorrente.codigo = contacorrentedestino
inner join unidadeensinocontacorrente on unidadeensinocontacorrente.contacorrente = contacorrente.codigo 
and usarpordefaultmovimentacaofinanceira
where movimentacaofinanceira.unidadeensino is null
) as t where t.codigo = movimentacaofinanceira.codigo;

update lancamentocontabil set unidadeensino = movimentacaofinanceira.unidadeensino from movimentacaofinanceira
where lancamentocontabil.tipoOrigemLancamentoContabil = 'MOVIMENTACAO_FINANCEIRA' 
and movimentacaofinanceira.codigo::VARCHAR = lancamentocontabil.codorigem and
movimentacaofinanceira.unidadeensino is not null
and movimentacaofinanceira.unidadeensino != lancamentocontabil.unidadeensino;

update lancamentocontabil set dataCompensacao = t.dataEntrada from (
select dataEntrada, lancamentocontabil.codigo as codigo_lc from notafiscalentrada
inner join lancamentocontabil on lancamentocontabil.codorigem::int = notafiscalentrada.codigo and TipoOrigemLancamentoContabil = 'NOTA_FISCAL_ENTRADA' 
and lancamentocontabil.dataCompensacao != notafiscalentrada.dataEntrada
) as t where t.codigo_lc = lancamentocontabil.codigo;

update lancamentocontabil set dataregistro = t.dataemissao from (
select dataemissao, lancamentocontabil.codigo as codigo_lc from notafiscalentrada
inner join lancamentocontabil on lancamentocontabil.codorigem::int = notafiscalentrada.codigo and TipoOrigemLancamentoContabil = 'NOTA_FISCAL_ENTRADA' 
and lancamentocontabil.dataregistro != notafiscalentrada.dataemissao
) as t where t.codigo_lc = lancamentocontabil.codigo;
 
    -------------- Ana 08/02/2019 11:56 7.0.2.0 ---------------
 alter table LogEstornoMovimentacaoFinanceiraItem alter column valor type numeric(10,2);
 
 
 -------------- rodrigo 08/02/19 18:33 7.0.1.0 -----------------
 ALTER TABLE matricula DROP CONSTRAINT fk_matricula_autorizacaocurso;
ALTER TABLE matricula
  ADD CONSTRAINT fk_matricula_autorizacaocurso FOREIGN KEY (autorizacaocurso)
      REFERENCES autorizacaocurso (codigo) MATCH SIMPLE
      ON UPDATE CASCADE ON DELETE restrict;
	  
 -------------- rodrigo 12/02/19 15:06 7.0.1.15 -----------------
alter table questao alter column justificativa type text;
alter table questao alter column ajuda type text;

-------------- rodrigo 12/02/19 15:06 7.0.2.0 -----------------
update lancamentocontabil set tipoPlanoConta = 'CREDITO' where codigo in (
select lancamentocontabil.codigo
from lancamentocontabil  
inner join movimentacaofinanceira on movimentacaofinanceira.codigo::varchar = lancamentocontabil.codorigem
and lancamentocontabil.tipoOrigemLancamentoContabil = 'MOVIMENTACAO_FINANCEIRA'
inner join configuracaocontabilregra on configuracaocontabilregra.contacorrenteorigem = movimentacaofinanceira.contacorrenteorigem
and configuracaocontabilregra.planoconta = lancamentocontabil.planoconta
and movimentacaofinanceira.contacorrenteorigem = lancamentocontabil.contacorrente
and configuracaocontabilregra.tipoRegraContabil = 'MOVIMENTACAO_FINANCEIRA'
where lancamentocontabil.tipoPlanoConta = 'DEBITO'

);


update lancamentocontabil set tipoPlanoConta = 'DEBITO' where codigo in (
select lancamentocontabil.codigo
from lancamentocontabil  
inner join movimentacaofinanceira on movimentacaofinanceira.codigo::varchar = lancamentocontabil.codorigem
and lancamentocontabil.tipoOrigemLancamentoContabil = 'MOVIMENTACAO_FINANCEIRA'
inner join configuracaocontabilregra on configuracaocontabilregra.contacorrenteorigem = movimentacaofinanceira.contacorrentedestino
and configuracaocontabilregra.planoconta = lancamentocontabil.planoconta
and movimentacaofinanceira.contacorrentedestino = lancamentocontabil.contacorrente
and configuracaocontabilregra.tipoRegraContabil = 'MOVIMENTACAO_FINANCEIRA'
where lancamentocontabil.tipoPlanoConta = 'CREDITO'
);

----------------- Marcos Paulo 13/02/2019 11:44 ---------------------
update historico set historicocursandoporcorrespondenciaapostransferencia = false where historico.codigo in (
select historico.codigo from historico
inner join matricula on matricula.matricula = historico.matricula and historico.matrizcurricular = matricula.gradecurricularatual
where historicocursandoporcorrespondenciaapostransferencia = true
and historico.matriculaperiodoturmadisciplina is null
and transferenciamatrizcurricularmatricula is not null
and exists (
	select his.codigo from historico his where his.matricula = matricula.matricula
	and his.anohistorico = historico.anohistorico
	and his.semestrehistorico = historico.semestrehistorico
	and his.disciplina = historico.disciplina
	and his.transferenciamatrizcurricularmatricula = historico.transferenciamatrizcurricularmatricula
	and his.historicocursandoporcorrespondenciaapostransferencia 
	and his.matrizcurricular != matricula.gradecurricularatual
	limit 1
)); -- chamado: 20689

 ----------------- Ana Claudia 13/02/2019 14:28 7.0.2.0 -----------------
alter table tramite add column unidadeensinopadrao integer;
ALTER TABLE tramite ADD CONSTRAINT fk_tramite_unidadeensinopadrao FOREIGN KEY (unidadeensinopadrao) REFERENCES unidadeensino (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;

----------------- Pedro Andrade 13/02/2019 15:00: 7.0.2.0 -----------------
create table conciliacaocontacorrentediaextratoconjunta (
	codigo serial not null,
	codigoofx integer not null,
	lancamentoofx varchar(255),
	documentoofx varchar(255),
	valorofx numeric(12,2),
	conciliacaocontacorrentediaextrato integer not null,
	constraint conciliacaocontacorrentediaextratoconjunta_pkey primary key (codigo),
	constraint fk_conciliacaocontacorrentediaextratoconjunta_conciliacaocontacorrentediaextrato foreign key (conciliacaocontacorrentediaextrato) references conciliacaocontacorrentediaextrato (codigo) match simple on update cascade on delete cascade
) with (
  oids=false
);

alter table conciliacaocontacorrente  drop column dataInicioArquivo;
alter table conciliacaocontacorrente  drop column dataFinalArquivo;
alter table conciliacaocontacorrente  drop column DataDoArquivo;
alter table conciliacaocontacorrente  drop column AgenciaArquivo;

UPDATE extratocontacorrente set conciliacaocontacorrentediaextrato = null where desconsiderarconciliacaobancaria  = true and conciliacaocontacorrentediaextrato is not null;
alter table conciliacaocontacorrentediaextrato  add column identificadorOfx varchar(40);
UPDATE conciliacaocontacorrentediaextrato set identificadorofx = codigoofx where identificadorofx is null;

alter table conciliacaocontacorrente  add column nomecontacorrente text;

UPDATE conciliacaocontacorrente set nomecontacorrente = t.nome from (
select array_to_string(array_agg(nomeapresentacaosistema), ',') as nome, ccc.codigo as codigo 
from contacorrente cc
inner JOIN agencia ON agencia.codigo = cc.agencia
inner join conciliacaocontacorrente ccc on 
( trim(leading '0' from cc.numero) = ccc.contacorrentearquivo 
or (trim(leading '0' from cc.numero)||cc.digito) = ccc.contacorrentearquivo
or (trim(leading '0' from agencia.numeroagencia)||cc.numero||cc.digito) = ccc.contacorrentearquivo
or ('0'||trim(leading '0' from agencia.numeroagencia)||cc.numero||cc.digito) = ccc.contacorrentearquivo)
group by ccc.codigo
) as t where t.codigo = conciliacaocontacorrente.codigo and conciliacaocontacorrente.nomecontacorrente is null;


UPDATE conciliacaocontacorrentediaextrato set codigoofx = t.novo_codigoofx::text from (
select conciliacaocontacorrentediaextrato.codigo, conciliacaocontacorrentediaextrato.codigoofx, row_number() OVER (PARTITION by conciliacaocontacorrente.codigo) as novo_codigoofx from conciliacaocontacorrente  
inner join conciliacaocontacorrentedia on conciliacaocontacorrentedia.conciliacaocontacorrente = conciliacaocontacorrente.codigo
inner join conciliacaocontacorrentediaextrato on conciliacaocontacorrentediaextrato.conciliacaocontacorrentedia = conciliacaocontacorrentedia.codigo
where conciliacaocontacorrentediaextrato.valorofx !=0.0 and (conciliacaocontacorrentediaextrato.codigoofx is not null or conciliacaocontacorrentediaextrato.codigoofx != '')
) as t where t.codigo = conciliacaocontacorrentediaextrato.codigo;

UPDATE conciliacaocontacorrentediaextrato set codigoofx = '0' where codigoofx is null or codigoofx ='';
ALTER TABLE conciliacaocontacorrentediaextrato ALTER COLUMN codigoofx TYPE integer USING (codigoofx::integer);
UPDATE conciliacaocontacorrentediaextrato set codigoofx = null where codigoofx =0;

----------------- Marcos Paulo 13/02/2019 15:37 7.0.2.0 -----------------
update tiporequerimento set prazoexecucao = t.totalprazo from (
select sum(tiporequerimentodepartamento.prazoexecucao) as totalprazo, tiporequerimentodepartamento.tiporequerimento 
from tiporequerimentodepartamento
inner join tiporequerimento on tiporequerimento.codigo = tiporequerimentodepartamento.tiporequerimento
group by tiporequerimento.codigo, tiporequerimentodepartamento.tiporequerimento
) as t where t.tiporequerimento = tiporequerimento.codigo and t.totalprazo <> tiporequerimento.prazoexecucao;

-------------- Rodrigo 14/02/2019 11:41 7.0.2.0 ----------------
update formapagamentonegociacaorecebimento set datacredito  = t.dataextrato from (
select formapagamentonegociacaorecebimento.codigo, formapagamentonegociacaorecebimento.datacredito, extratocontacorrente.data as dataextrato, negociacaorecebimento.data as datarecebimento 
from negociacaorecebimento  
inner join formapagamentonegociacaorecebimento on negociacaorecebimento.codigo = formapagamentonegociacaorecebimento.negociacaorecebimento
inner join extratocontacorrente on extratocontacorrente.contacorrente = formapagamentonegociacaorecebimento.contacorrente
and extratocontacorrente.origemextratocontacorrente = 'RECEBIMENTO' and negociacaorecebimento.codigo = extratocontacorrente.codigoorigem
where negociacaorecebimento.recebimentoboletoautomatico
and formapagamentonegociacaorecebimento.datacredito::date != extratocontacorrente.data::date
) as t where t.codigo = formapagamentonegociacaorecebimento.codigo; -- chamado: 21080



-------------------JEAN PIERRE 15/02/2019 15:57--------------------------------------------------------------
create index idx_contrachequeevento_contracheque               on public.contrachequeevento (contracheque);
create index idx_contrachequeevento_eventofolhapagamento       on public.contrachequeevento (eventofolhapagamento);
create index idx_contrachequeevento_periodo                    on public.contrachequeevento (periodo);
create index idx_contracheque_funcionariocargo                 on public.contracheque       (funcionariocargo);
create index idx_contracheque_templatelancamentofolhapagamento on public.contracheque       (templatelancamentofolhapagamento);
create index idx_contracheque_competenciafolhapagamento        on public.contracheque       (competenciafolhapagamento);
CREATE INDEX idx_fornecedor_nome_funcao_sem_acento ON fornecedor USING gin (sem_acentos(nome) gin_trgm_ops);
CREATE INDEX idx_parceiro_nome_funcao_sem_acento   ON parceiro USING gin (sem_acentos(nome) gin_trgm_ops);
CREATE INDEX idx_convenio_descricao_funcao_sem_acento   ON convenio USING gin (sem_acentos(descricao) gin_trgm_ops);

----------- Rodrigo Wind 19/02/2019 17:45 ------------
alter table condicaopagamentoplanofinanceirocurso  add column lancarvalorratiadosobrevalorbasecontareceber boolean default false;

----------- Rodrigo Wind 20/02/2019 09:39 ------------
ALTER TABLE exemplar DROP CONSTRAINT check_exemplar_emprestado_vinculo_emprestimo;
alter table exemplar alter column desconsiderarReserva set default false;
UPDATE exemplar SET desconsiderarReserva = FALSE where desconsiderarReserva  is null ;

ALTER TABLE exemplar
  ADD CONSTRAINT check_exemplar_emprestado_vinculo_emprestimo CHECK (fn_validarexemplaremprestadovinculadoemprestimo(codigo, situacaoatual::text) = true) NOT VALID;

---- Alessandro 20/02/2019 ----  
alter table configuracaogeralsistema drop column urlLdap;
alter table configuracaogeralsistema drop column usuarioLdap;
alter table configuracaogeralsistema drop column senhaLdap;
alter table configuracaogeralsistema drop column bloquearAlteracaoUsernameSincronizandoComCPF;
alter table configuracaogeralsistema add column possuiIntegracaoLdap boolean default false;
alter table configuracaogeralsistema add column hostnameLdap text;
alter table configuracaogeralsistema add column portaLdap text;
alter table configuracaogeralsistema add column dnLdap text;
alter table configuracaogeralsistema add column senhaLdap text;
alter table configuracaogeralsistema add column dcLdap text;
alter table configuracaogeralsistema add column grupoPrincipalLdap text;
alter table configuracaogeralsistema add column grupoAlunoLdap text;
alter table configuracaogeralsistema add column grupoCandidatoLdap text;
alter table configuracaogeralsistema add column grupoProfessorLdap text;
alter table configuracaogeralsistema add column grupoCoordenadorLdap text;
alter table configuracaogeralsistema add column grupoFuncionarioLdap text;
alter table usuario add column possuiCadastroLdap boolean;
alter table usuario add column ativoLdap boolean not null default false;
create table registroldap(
	codigo serial primary key,
	data timestamp not null default now(),
	usuario integer not null references usuario (codigo),
	operacao varchar,
	resumo text,
	sucesso boolean not null default false,
	excessao text
);
  
---- Alessandro 20/02/2019 ----
alter table pessoa drop column possuiCadastroMinhaBiblioteca;

---- Rodrigo 20/02/2019 14:35 ----
update configuracaobiblioteca  set tamanhoCodigoBarra = (
select length(codigobarra)
from exemplar  where codigobarra ::numeric(20,0) = (select max(e.codigobarra::numeric(20,0)) from exemplar e ) 
) where tamanhoCodigoBarra =  7 and tamanhoCodigoBarra !=  (
select length(codigobarra)
from exemplar  where codigobarra ::numeric(20,0) = (select max(e.codigobarra::numeric(20,0)) from exemplar e ) 
);

----------- Marcos Paulo 20/02/2019 16:04 ------------  
--update turma set identificadorTurma = replace(identificadorTurma, '\', '/') WHERE identificadorTurma like '%\%' escape '@' ;

--------- Renato Borges 21/02/2019 09:10 7.0.1 ------------
ALTER TABLE public.layoutrelatorioseidecidir ADD COLUMN subrelatoriocrosstab boolean default false;
ALTER TABLE public.layoutrelatorioseidecidircampo ADD COLUMN valorcrosstab character varying(50);

-------------------JEAN PIERRE 21/02/2019 10:56-----------------------------------------------------------------------
create index idx_abonofalta_matricula on public.abonofalta (matricula);
create index idx_abonofalta_pessoa    on public.abonofalta (pessoa);
 create index idx_emprestimo_matricula on public.emprestimo (matricula);
 create index idx_catalogo_editora on public.catalogo (editora);
 create index idx_catalogo_responsavelatualizacao on public.catalogo (responsavelatualizacao);
 create index idx_catalogo_tipocatalogo on public.catalogo (tipocatalogo);
 create index idx_catalogo_assunto_funcao_sem_acentos_gin on public.catalogo using gin (sem_acentos(assunto) gin_trgm_ops);
 create index idx_catalogo_titulo_funcao_sem_acentos_gin  on public.catalogo using gin (sem_acentos(titulo) gin_trgm_ops);
 
create index idx_controlecobranca_responsavel       on public.controlecobranca (responsavel);
create index idx_controlecobranca_registroarquivo   on public.controlecobranca (registroarquivo);
create index idx_controlecobranca_contacorrente     on public.controlecobranca (contacorrente);
create index idx_controlecobranca_dataprocessamento on public.controlecobranca (dataprocessamento);CREATE INDEX idx_convenio_descricao_funcao_sem_acento   ON convenio USING gin (sem_acentos(descricao) gin_trgm_ops);


-------------------Pedro Andrade 25/02/2019 11:00--------------------------------------------------------------
update contareceber set pagocomdcc = false where pagocomdcc is null;
update negociacaorecebimento set pagamentocomdcc = false where pagamentocomdcc is null;

-------------------RODRIGO 26/02/2019 12:03--------------------------------------------------------------
alter table avaliacaoinstitucional alter column nome type varchar(250);

-------------------Pedro Andrade 26/02/2019 16:48--------------------------------------------------------------
insert into permissao (tipopermissao, permissoes, nomeentidade, tituloapresentacao, codperfilacesso)
select  
     1 as tipopermissao,'(0)(1)(2)(3)(9)(12)' as permissoes,'PermitirLancamentoContabilMovimentacaoFinanceiraSomenteDestino' as nomeentidade,
     'Permitir Realizar Movimentação Financeira Somente Com a Conta Destino' as tituloapresentacao, perfilacesso.codigo as codperfilacesso 
from permissao
 INNER JOIN perfilacesso ON permissao.codperfilacesso = perfilacesso.codigo
where permissao.tituloapresentacao = 'Movimentação Financeira'
and permissao.nomeentidade = 'MovimentacaoFinanceira'
AND NOT EXISTS (SELECT 1 FROM permissao  where permissao.codperfilacesso = perfilacesso.codigo and permissao.nomeentidade = 'PermitirLancamentoContabilMovimentacaoFinanceiraSomenteDestino');

---------------------------------JEAN PIERRE 28/02/2019 10:13--------------------------------------------------------------------
alter table contarecebernaolocalizadaarquivoretorno add CONSTRAINT fk_contarecebernaolocalizadaarquivoretorno_contacorrente FOREIGN KEY (contacorrente) REFERENCES contacorrente (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;


--------------------- Pedro Andrade / Thyago 01/03/2019 10:00 ---------------
alter table nossonumerocontareceber add column contacorrente integer;
ALTER TABLE nossonumerocontareceber
  ADD CONSTRAINT fk_nossonumerocontareceber_contacorrente FOREIGN KEY (contacorrente)
      REFERENCES contacorrente (codigo) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT;
      
ALTER TABLE nossonumerocontareceber DROP CONSTRAINT unique_nossonumerocontareceber_nossonumero_contacorrente;
	  
UPDATE nossonumerocontareceber set contacorrente = (
select contacorrente from contareceber where contareceber.nossonumero = nossonumerocontareceber.nossonumero and contareceber.matriculaperiodo = nossonumerocontareceber.matriculaperiodo
) where nossonumerocontareceber.contacorrente is null;

----------------- Leandro 06/03/2019 08:55 ---------------------
ALTER TABLE transferenciaturnodisciplina 
	add column turmaPraticaAntiga integer,
	add column turmaPraticatTransferida integer,
	add column turmaTeoricaAntiga integer,
	add column turmaTeoricaTransferida integer;
	
	
------------- Ana Claudia 06/03/2019 10:50 7.0.1 ---------------
ALTER TABLE prospects ALTER COLUMN cpf TYPE character varying(18);


----------------- Marcos Paulo 06/03/2019 11:17 ---------------------
DROP INDEX unq_contareceber_codorigem_tipoorigem_parcela_pessoa_matricu;

CREATE UNIQUE INDEX unq_contareceber_codorigem_tipoorigem_parcela_pessoa_matricu
  ON contareceber
  USING btree
  (codorigem COLLATE pg_catalog."default", tipoorigem COLLATE pg_catalog."default", parcela COLLATE pg_catalog."default", pessoa, matriculaaluno COLLATE pg_catalog."default")
  WHERE tipoorigem::text <> 'BCC'::text AND tipoorigem::text <> 'OUT';
  
  ------------- Pedro Andrade 12/03/2019 10:05 7.0.2.0 ---------------	
alter table gradecurricular add column nrmesesconclusaomatrizcurricular integer;
update gradecurricular set nrmesesconclusaomatrizcurricular = 0 where nrmesesconclusaomatrizcurricular is null;


-------------- Rodrigo 12/03/2019 20:25 7.0.2.0 --------------
alter table contareceberrecebimento add column valorrecebimento_tmp numeric(20,2);
update contareceberrecebimento set valorrecebimento_tmp = (((valorrecebimento*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2)
where (((valorrecebimento*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2) != valorrecebimento::NUMERIC(20,2) ;  --ignoreaudit
alter table contareceberrecebimento alter column valorrecebimento type NUMERIC(20,2);
update contareceberrecebimento set valorrecebimento = valorrecebimento_tmp where valorrecebimento_tmp is not null and valorrecebimento_tmp != valorrecebimento ; --ignoreaudit

alter table formapagamentonegociacaorecebimento add column valorrecebimento_tmp numeric(20,2);
update formapagamentonegociacaorecebimento set valorrecebimento_tmp = (((valorrecebimento*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2)
where (((valorrecebimento*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2) != valorrecebimento::NUMERIC(20,2) ;  --ignoreaudit
alter table formapagamentonegociacaorecebimento alter column valorrecebimento type NUMERIC(20,2);
update formapagamentonegociacaorecebimento set valorrecebimento = valorrecebimento_tmp where valorrecebimento_tmp is not null and valorrecebimento != valorrecebimento_tmp; --ignoreaudit

-------------- Renato 13/03/2019 08:40 7.0.2.0 --------------
ALTER TABLE public.conteudoplanejamento ALTER COLUMN cargahoraria TYPE numeric (12, 2);
update formapagamentonegociacaorecebimento set valorrecebimento = valorrecebimento_tmp where valorrecebimento_tmp is not null and valorrecebimento != valorrecebimento_tmp; --ignoreaudit

------------- Pedro Andrade 13/03/2019 08:38 7.0.1.0 ---------------	
ALTER TABLE horarioprofessordiaitem DROP CONSTRAINT horarioprofessordiaitem_uniq;

-------------- Renato 15/03/2019 16:51 7.0.1 --------------
alter table planoensino add column turno int;

ALTER TABLE public.planoensino ADD CONSTRAINT fk_planoensino_turno FOREIGN KEY (turno)
REFERENCES public.turno (codigo) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE public.planoensino DROP CONSTRAINT unq_dis_cur_uni_ano_sem_prof;
ALTER TABLE public.planoensino ADD CONSTRAINT unq_dis_cur_uni_ano_sem_prof UNIQUE (disciplina, curso, unidadeensino, ano, semestre, professorresponsavel, turno);	

CREATE OR REPLACE FUNCTION public.consultarPlanoEnsino(
	unidadeensino integer, curso integer, ano varchar(4), semestre varchar(1), disciplina integer, turno integer, professor integer, situacao varchar)
RETURNS TABLE(codigo integer) 
    LANGUAGE 'sql'
    COST 1
    VOLATILE 
    ROWS 1
AS $BODY$
 
     select codigo from (  
select row_number() over(partition by planoensino.unidadeensino, planoensino.curso, planoensino.disciplina order by planoensino.unidadeensino, planoensino.curso, planoensino.disciplina,						 
						 case when planoensino.professorresponsavel is not null then 1 else 2 end, 						 
						 case when planoensino.turno is not null then 1 else 2 end) as ordem,
planoensino.codigo, planoensino.unidadeensino, planoensino.curso, planoensino.turno, planoensino.disciplina, planoensino.professorresponsavel
from planoensino
inner join curso on curso.codigo = planoensino.curso
where 
		 ((curso.periodicidade = 'SE' and (planoensino.ano||planoensino.semestre) <= ($3||$4))
		 or (curso.periodicidade = 'AN' and planoensino.ano <= $3)
		 or curso.periodicidade = 'IN')
and planoensino.unidadeensino = $1
and planoensino.curso = $2
and planoensino.disciplina = $5  
and (planoensino.professorresponsavel = $7  or professorresponsavel is null)
and (planoensino.turno = $6  or turno is null)
and ($8 is null or $8 = '' or situacao = $8)
order by ordem limit 1 
  ) as t
$BODY$;

ALTER FUNCTION public.consultarPlanoEnsino(integer, integer, varchar, varchar, integer, integer, integer, varchar)
    OWNER TO postgres;
	
	
---------------- Carlos 16/03/2019 08:32  7.0.1 -----------------
alter table configuracaoacademico rename OcultarSituacaoDisciplinaMaeCasoReprovadoDisciplinaFilha to ocultarMediaFinalDisciplinaCasoReprovado;

---- Alessandro 18/03/2019 ----
CREATE INDEX idx_contareceber_nrdocumento_func_gin ON contareceber USING gin (nrdocumento COLLATE pg_catalog."default" gin_trgm_ops);

---------------- Pedro Andrade 16/03/2019 08:32  7.0.1 -----------------
alter table contacorrente add column utilizataxacartaodebito boolean default false;


--------------- Carlos 20/03/2019 07:46 7.0.1 ---------------------
alter table tiporequerimento add column bloquearQuantidadeRequerimentoAbertosSimultaneamente boolean default(false);
alter table tiporequerimento add column quantidadeLimiteRequerimentoAbertoSimultaneamente integer;
alter table tiporequerimento add column considerarBloqueioSimultaneoRequerimentoDeferido boolean default(false);
alter table tiporequerimento add column considerarBloqueioSimultaneoRequerimentoIndeferido boolean default(false);


update requerimento set matriculaperiodo = t2.matriculaperiodo 
from (

	select codigo, matricula, data, ano, semestre, tipopessoa, 
	case 
		when t1.matriculaperiodo is not null then t1.matriculaperiodo
		else 
		(
			select matriculaperiodo.codigo from matriculaperiodo 
			where matriculaperiodo.matricula = t1.matricula
			order by (matriculaperiodo.ano || matriculaperiodo.semestre) desc, matriculaperiodo.codigo desc limit 1
		)
	end AS matriculaPeriodo
	from (
		select *, 
		(
			select matriculaperiodo.codigo from matriculaperiodo 
			where matriculaperiodo.matricula = t.matricula
			and case when t.periodicidade in('SE', 'AN') then matriculaperiodo.ano = cast(t.ano as varchar) else false end
			and case when t.periodicidade in('SE') then matriculaperiodo.semestre = t.semestre else true end
			order by matriculaperiodo.codigo desc limit 1
		) AS matriculaPeriodo
		from (

			select requerimento.codigo, requerimento.matricula, requerimento.data, 
			extract(year from requerimento.data) AS ano,
			
			case 
				when extract(month from requerimento.data) > 7 then '2'
				else 
				case 
					when extract(month from requerimento.data) = 7 and extract(day from requerimento.data) <= 20
					then '1' 
					else 
					case when extract(month from requerimento.data) = 7 and extract(day from requerimento.data) > 20
					then '2'
				else 
				case 
					when extract(month from requerimento.data) < 7 then '1'
				end
			end end end
			AS semestre, 
			tipopessoa, curso.periodicidade 
			from requerimento 
				inner join matricula on matricula.matricula = requerimento.matricula
				inner join curso on curso.codigo = matricula.curso
			where 1=1
			and tipopessoa = 'ALUNO'
			and matriculaperiodo is null
		) as t
		order by data

	) as t1

) as t2 where t2.codigo = requerimento.codigo;

--------------- Cesar Henrique 22/03/2019 14:42 7.0.1 ---------------------
alter table AtendimentoInteracaoDepartamento add column mensagemEnviada boolean default(false);

--------------- Marcos Paulo 25/03/2019 11:10 7.0.1 ---------------------
ALTER TABLE matricula DROP CONSTRAINT fk_matricula_renovacaoreconhecimento;

ALTER TABLE matricula
  ADD CONSTRAINT fk_matricula_renovacaoreconhecimento FOREIGN KEY (renovacaoreconhecimento)
      REFERENCES autorizacaocurso (codigo) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

---- Alessandro 27/03/2019 ----
alter table configuracaonotafiscal add column numeronotahomologacao int;
alter table configuracaonotafiscal add column lotehomologacao int;
alter table configuracaonotafiscal add column seriehomologacao text;  
alter table configuracaonotafiscal add column fusohorario int not null default 0;

-------- Renato Borges 31/04/2019 08:15 6.0.0.0 ---------------------
ALTER TABLE itemprestacaocontaorigemcontareceber ADD COLUMN valorinformadomanual boolean;
ALTER TABLE itemprestacaocontacategoriadespesa ADD COLUMN valorinformadomanual boolean;

----------------- Leandro 03/04/2019 10:30 ---------------------
alter table turma add column codigoTurnoApresentarCenso integer default 0;


----------------- RODRIGO 03/04/2019 10:30 ---------------------
CREATE OR REPLACE FUNCTION public.fn_verificarpessoacontarecebermesmapessoamatricula(p_pessoa integer, p_tipopessoa character varying, p_matricula text, p_beneficiario integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE 
   quantidaderegistros integer := 0;
BEGIN
        if ( (p_matricula is null) or (p_tipopessoa not in ('AL','RF','RE','PA') )
        or ( p_tipopessoa = 'PA' AND (p_beneficiario IS null or p_beneficiario = 0) )) then
          return true;
        end if ;

        IF (p_tipopessoa IN ('AL','RF','RE') ) THEN
             SELECT   into quantidaderegistros  count(*)
 
              FROM matricula
             where matricula.matricula = p_matricula
             and matricula.aluno = p_pessoa;
         ELSIF  ( p_tipopessoa = 'PA' AND p_beneficiario IS NOT null AND p_beneficiario > 0 )  THEN 
           SELECT   into quantidaderegistros  count(*)
 
              FROM matricula
             where matricula.matricula = p_matricula
             and matricula.aluno = p_beneficiario;   
         END IF;  

        if (quantidaderegistros > 0 ) then
           return true;
        ELSE  
          return false;  
       end if ;                  
END;     
$function$
;


update contareceber set beneficiario = matricula.aluno from  matricula 
where matricula.matricula = contareceber.matriculaaluno
and matricula.aluno != contareceber.beneficiario
and contareceber.beneficiario = 0
and contareceber.tipopessoa = 'PA'; -- chamado: 21566


------------------ RODRIGO 04/04/2019 10:05 7.0.1.0 -------------
update extratocontacorrente set tiposacado = 'RESPONSAVEL_FINANCEIRO' where tiposacado = 'RF';
update extratocontacorrente set tiposacado = 'ALUNO' where tiposacado = 'AL';
--------------- Cesar Henrique 05/04/2019 10:14 7.0.1 ---------------------
alter table atendimento add column celular varchar(15);

--------------- Renato Borges 04/04/2019 14:56 7.0.1 ---------------------
alter table pessoa add column secaozonaeleitoral varchar(20);

--------------- Cesar Henrique 05/04/2019 14:12 7.0.1 ---------------------
alter table logExclusaoMatricula alter column curso type text;

------------- Pedro Andrade 28/03/2019 16:40 7.0.2.0 ---------------	
ALTER TABLE contacorrente ALTER COLUMN utilizataxacartaodebito SET DEFAULT true;
ALTER TABLE contacorrente ADD COLUMN utilizataxacartaocredito boolean DEFAULT true;
alter table extratocontacorrente  add column formapagamentonegociacaorecebimento integer;


UPDATE extratocontacorrente set formapagamentonegociacaorecebimento = tt.formapagamentonegociacaorecebimento from (
select distinct t.negociacaorecebimento, t.formapagamentonegociacaorecebimento, extratocontacorrente.codigo as extratocontacorrente from (
select 
row_number() over(partition by negociacaorecebimento.codigo, formapagamentonegociacaorecebimento.formapagamento, 
formapagamentonegociacaorecebimento.operadoracartao order by formapagamentonegociacaorecebimento.negociacaorecebimento, 
formapagamentonegociacaorecebimento.formapagamento, formapagamentonegociacaorecebimento.operadoracartao,
substring(formapagamentonegociacaorecebimentocartaocredito.numeroparcela, 1, 1),
formapagamentonegociacaorecebimentocartaocredito.numeroparcela
) as ordemBaixa, 
formapagamentonegociacaorecebimento.codigo as formapagamentonegociacaorecebimento,
formapagamentonegociacaorecebimento.negociacaorecebimento,
formapagamentonegociacaorecebimento.formapagamento,
formapagamentonegociacaorecebimento.operadoracartao,
formapagamentonegociacaorecebimentocartaocredito.datarecebimento,
formapagamentonegociacaorecebimentocartaocredito.numeroparcela,
formapagamentonegociacaorecebimentocartaocredito.valorparcela, 
formapagamentonegociacaorecebimentocartaocredito.codigo, 
substring(formapagamentonegociacaorecebimentocartaocredito.numeroparcela, 1, 1)::INT
from formapagamentonegociacaorecebimento
inner join negociacaorecebimento on negociacaorecebimento.codigo = formapagamentonegociacaorecebimento.negociacaorecebimento
inner join formapagamento on formapagamento.codigo = formapagamentonegociacaorecebimento.formapagamento
inner join formapagamentonegociacaorecebimentocartaocredito on ((formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito is not null and formapagamentonegociacaorecebimentocartaocredito.codigo 
= formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito ) or (
formapagamentonegociacaorecebimento.formapagamentonegociacaorecebimentocartaocredito is null
and formapagamentonegociacaorecebimento.codigo =formapagamentonegociacaorecebimentocartaocredito.formapagamentonegociacaorecebimento
))
where formapagamento.tipo in ('CA') and formapagamentonegociacaorecebimentocartaocredito.situacao = 'RE'
order by formapagamentonegociacaorecebimento.negociacaorecebimento, 
formapagamentonegociacaorecebimento.formapagamento, formapagamentonegociacaorecebimento.operadoracartao,
substring(formapagamentonegociacaorecebimentocartaocredito.numeroparcela, 1, 1),
formapagamentonegociacaorecebimentocartaocredito.numeroparcela
) as t
inner join extratocontacorrente on codigoorigem = t.negociacaorecebimento 
and origemExtratoContaCorrente = 'COMPENSACAO_CARTAO' and t.formapagamento = ExtratoContaCorrente.formapagamento
and t.operadoracartao = extratocontacorrente.operadoracartao
and extratocontacorrente.tipomovimentacaofinanceira = 'ENTRADA'
and extratocontacorrente.codigo = (
select ecc.codigo from extratocontacorrente ecc
where ecc.codigoorigem = t.negociacaorecebimento 
and ecc.origemExtratoContaCorrente = 'COMPENSACAO_CARTAO' and t.formapagamento = ecc.formapagamento
and t.operadoracartao = ecc.operadoracartao
and ecc.tipomovimentacaofinanceira = 'ENTRADA'
order by ecc."data", ecc.codigo
limit 1 offset (t.ordemBaixa - 1)
)) as tt where tt.extratocontacorrente = extratocontacorrente.codigo 
and extratocontacorrente.origemExtratoContaCorrente = 'COMPENSACAO_CARTAO'
and extratocontacorrente.formapagamentonegociacaorecebimento is null;



UPDATE extratocontacorrente set formapagamentonegociacaorecebimento = tt.formapagamentonegociacaorecebimento from (
select 
formapagamentonegociacaorecebimento.codigo as formapagamentonegociacaorecebimento,
extratocontacorrente.codigo as extratocontacorrente
from extratocontacorrente
inner join formapagamento on formapagamento.codigo = extratocontacorrente.formapagamento
inner join formapagamentonegociacaorecebimento on  formapagamentonegociacaorecebimento.negociacaorecebimento = extratocontacorrente.codigoorigem
and formapagamentonegociacaorecebimento.formapagamento = extratocontacorrente.formapagamento
where formapagamento.tipo in ('DI', 'BO', 'DE', 'DC', 'CD') 
and extratocontacorrente.origemExtratoContaCorrente = 'RECEBIMENTO' 
 and extratocontacorrente.formapagamentonegociacaorecebimento is null
) as tt where tt.extratocontacorrente = extratocontacorrente.codigo 
and extratocontacorrente.origemExtratoContaCorrente = 'RECEBIMENTO'
and extratocontacorrente.formapagamentonegociacaorecebimento is null
and extratocontacorrente.formapagamento in (select codigo from formapagamento where tipo  in ('DI', 'BO', 'DE', 'DC', 'CD') );


	
------------ rODRIGO 08/04/2019 16:00 ----------------
create table tratamentoErro
(
	codigo serial not null,
	erro text not null,
	mensagemApresentar text,
	primary key(codigo)
);	

-------------- RODRIGO 09/04/2019 12:19 -----------
alter table exemplar alter column tituloexemplar type text;

-------------- Carlos 10/04/2019 09:21 ------------------
alter table tiporequerimento add column permitirImpressaoHistoricoVisaoAluno boolean default(false);
alter table tiporequerimento add column nivelEducacional varchar(2);
alter table tiporequerimento add column layoutHistoricoApresentar varchar(255);
alter table tiporequerimento add column aprovadoSituacaoHistorico boolean default(false);
alter table tiporequerimento add column reprovadoSituacaoHistorico boolean default(false);
alter table tiporequerimento add column trancadoSituacaoHistorico boolean default(false);
alter table tiporequerimento add column cursandoSituacaoHistorico boolean default(false);
alter table tiporequerimento add column abandonoCursoSituacaoHistorico boolean default(false);
alter table tiporequerimento add column transferidoSituacaoHistorico boolean default(false);
alter table tiporequerimento add column canceladoSituacaoHistorico boolean default(false);
ALTER TABLE tiporequerimento add column assinardigitalmentehistorico boolean default(false);

-------------- Renato Borges Cardoso 10/04/2019 17:09 -----------
ALTER TABLE gabaritoresposta ADD COLUMN anulado boolean DEFAULT false;
ALTER TABLE gabaritoresposta ADD COLUMN historicoanulado text;

---------------Cesar Henrique 10/04/2019 17:15 -------------------

create table reagendamentoCompromisso (
 
 	codigo serial NOT NULL,
	dataModificacaoReagendamento date ,
	dataInicioCompromisso date not null,
	dataReagendamentoCompromisso date not null,
	compromissoAgendaPessoaHorario int not null ,
	agendaPessoaHorario int,
	campanha int ,
	responsavelReagendamento text,
	CONSTRAINT reagendamentoCompromisso_pkey PRIMARY KEY (codigo) 
  );

create index idx_reagendamentocompromisso_datainiciocompromisso on public.reagendamentocompromisso (dataInicioCompromisso);
create index idx_reagendamentocompromisso_agendaPessoaHorario   on public.reagendamentocompromisso (agendaPessoaHorario);

-------------- Renato Borges Cardoso 11/04/2019 17:09 -----------
ALTER TABLE eventofolhapagamento ADD COLUMN incideassociacaosindicatodecimoterceiro boolean DEFAULT false;
ALTER TABLE eventofolhapagamento ADD COLUMN incideprevidenciaobrigatoriadecimoterceiro boolean DEFAULT false;

----------------- Leandro 12/04/2019 11:50 ---------------------
alter table expedicaodiploma alter column titulofuncionarioprincipal type varchar(100);
alter table expedicaodiploma alter column titulofuncionariosecundario type varchar(100);

---------------Cesar Henrique 15/04/2019 10:43 -------------------
UPDATE exemplar SET dataaquisicao = datacompra where datacompra is not null and dataaquisicao is null;

---------------- Thyago 15/04/2019 14:30 ----------------
alter table parceiro add column permiteRemessaBoletoAlunoVinculadoParceiro boolean default true;
alter table parceiro add column permiteEmitirBoletoAlunoVinculadoParceiro boolean default true;

-------------- Renato Borges Cardoso 17/04/2019 08:09 -----------
ALTER TABLE itemprestacaocontaorigemcontareceber ADD COLUMN valormanual NUMERIC(20,2);


----------- Rodrigo 17/01/2019 17:06 ------------

CREATE OR REPLACE FUNCTION public.fn_verificarcontemparcelanegociadatipoorigem(v_negociacaocontareceber integer, v_tipoorigem varchar(3))
 RETURNS boolean
 LANGUAGE plpgsql
 STABLE
AS $function$
 DECLARE
  v_contemParcelaTipoOrigem  boolean := false;
  listaContaReceber    record;
		
BEGIN
      FOr listaContaReceber IN 
       SELECT contareceber.codigo , contareceber.codorigem, contareceber.tipoorigem
       FROM contarecebernegociado
       inner join contareceber on contareceber.codigo = contarecebernegociado.contareceber       
       where contarecebernegociado.negociacaocontareceber = v_negociacaocontareceber
       and contareceber.tipoorigem in ('NCR', v_tipoorigem)
      
      LOOP
        if (listaContaReceber.tipoorigem = v_tipoorigem) then
         	return true;
        ELSIF (listaContaReceber.tipoorigem = 'NCR' and listaContaReceber.codorigem != '')  then 
         	v_contemParcelaTipoOrigem := fn_verificarcontemparcelanegociadatipoorigem(listaContaReceber.codorigem::INT, v_tipoorigem);
         	if(v_contemParcelaTipoOrigem = true) then
         		return v_contemParcelaTipoOrigem;
         	end if;
        end if;  
      END LOOP;
  RETURN v_contemParcelaTipoOrigem;
END;
$function$
;

---------------- Renato Borges 25/04/2019 15:48 -------------------
ALTER TABLE unidadeensino ADD COLUMN codigoiesmantenedora integer;

---- Alessandro 29/04/2019 10:30 ----
alter table unidadeensinocurso alter column codigoitemlistaservico type text;


------- Rodrigo 30/04/19 18:16 --------
create index idx_registronegcontareceber_curso on registronegativacaocobrancacontareceber (curso);
create index idx_registronegcontareceber_turma on registronegativacaocobrancacontareceber (turma);
create index idx_registronegcontareceber_unidadeensino on registronegativacaocobrancacontareceber (unidadeensino);
create index idx_registronegcontareceber_dataGeracao on registronegativacaocobrancacontareceber (dataGeracao);
create index idx_registronegcontareceberitem_aluno on registronegativacaocobrancacontareceberitem (aluno);
create index idx_registronegcontareceberitem_nossonumero on registronegativacaocobrancacontareceberitem (nossonumero);

----------------- Leandro 02/05/2019 14:50 ---------------------
CREATE OR REPLACE FUNCTION fn_validarsituacaodevolucaoitememprestimo()
  RETURNS trigger AS
$BODY$
  BEGIN 
     IF current_query() ~ '--ignorevalidacaosituacao' THEN RETURN new;
     END IF;
     IF ((old.contareceber > 0 AND old.situacao = 'DE' AND NEW.situacao = 'DE') 
	OR (new.contareceber is null AND old.situacao = 'DE' AND NEW.situacao = 'DE')) THEN
           RAISE EXCEPTION 'emprestimoJaEstaDevolvido{(%)}', old.codigo;
     END IF;
     RETURN new;
  END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;

CREATE TRIGGER  tg_validarsituacaodevolucaoitememprestimo BEFORE UPDATE ON itememprestimo
FOR EACH ROW  EXECUTE PROCEDURE fn_validarsituacaodevolucaoitememprestimo();

----------------- Leandro 08/05/2019 09:50 ---------------------
CREATE OR REPLACE FUNCTION fn_validarContaReceberVinculadoAgenteRegistroNegativacao(codigo_registronegativacaocobrancacontareceber integer, 
	codigo_contareceber integer, 
	data_exclusao timestamp without time zone) RETURNS boolean AS $$
DECLARE 
   quantidaderegistros integer;
   codigo_agente integer;
begin
	select into codigo_agente rnccr.agente from registronegativacaocobrancacontareceber rnccr where rnccr.codigo = codigo_registronegativacaocobrancacontareceber;
  	select into quantidaderegistros count(registronegativacaocobrancacontareceberitem.codigo) 
   	from registronegativacaocobrancacontareceber 
		inner join registronegativacaocobrancacontareceberitem on registronegativacaocobrancacontareceber.codigo = registronegativacaocobrancacontareceberitem.registronegativacaocobrancacontareceber
		where registronegativacaocobrancacontareceberitem.contareceber = codigo_contareceber
		and registronegativacaocobrancacontareceber.agente = codigo_agente
		and registronegativacaocobrancacontareceberitem.dataexclusao is null 
		and data_exclusao is null;
    IF coalesce(quantidaderegistros,0) > 0 THEN
       RETURN FALSE; 
    ELSE 
       RETURN TRUE;  
    END IF;                 
END;     
$$ LANGUAGE plpgsql;

ALTER TABLE registronegativacaocobrancacontareceberitem ADD CONSTRAINT check_registronegativacaocobrancacontareceberitem_contareceber_agente CHECK 
	(fn_validarContaReceberVinculadoAgenteRegistroNegativacao(registronegativacaocobrancacontareceber, contareceber, dataexclusao)) not valid;

----------------- Edson 08/05/2019 14:50 ---------------------
ALTER TABLE IntegracaoGinfesAlunoItem ALTER COLUMN cep TYPE varchar(10);
alter table integracaoginfesaluno add column descontocondicional  boolean default true;
alter table integracaoginfesaluno add column descontoincondicional  boolean default true;
alter table IntegracaoGinfesAlunoItem add column complemento  varchar(60);  
alter table IntegracaoGinfesAlunoItem add column codigoAluno integer ;  

---- Alessandro 09/05/2019 ----
CREATE OR REPLACE FUNCTION aulaaluno_gradedisciplinacomposta(
    historico integer,
    trazerdisciplinacomposta boolean,
    professor integer,
    apenasturmaorigem boolean)
  RETURNS SETOF horarioaulaaluno AS
$BODY$
select distinct max(pessoa.codigo) as professor_codigo, 
 array_to_string(array_agg(distinct pessoa.nome), ', ') as professor_nome, 
 array_agg(distinct pessoa.codigo) as professores_codigo,
 disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
 min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino , 'PRESENCIAL' as modalidadeDisciplina 
 from horarioturma  
 inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
 inner join turma on turma.codigo = horarioturma.turma
 inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
 inner join gradedisciplinacomposta on gradedisciplinacomposta.disciplina = horarioturmadiaitem.disciplina
 inner join gradedisciplina on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina
 inner join disciplina on disciplina.codigo = gradedisciplina.disciplina
 inner join historico on historico.codigo = $1 and gradedisciplina.codigo = historico.gradedisciplina
 inner join matricula on matricula.matricula = historico.matricula
 inner join curso on matricula.curso = curso.codigo
 inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo
 inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor
 left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo 
 and matriculaperiodoturmadisciplina.gradedisciplinacomposta = gradedisciplinacomposta.codigo 
 where  $2 = true and  (($3 is not null and pessoa.codigo = $3) or ($3 is null and pessoa.codigo is not null)) and
((curso.periodicidade = 'AN' and horarioturma.anovigente = matriculaperiodo.ano)
   or (curso.periodicidade = 'SE' and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre)
   or (curso.periodicidade = 'IN')
  ) 
and horarioturma.turma in (
	select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
	union all
	select t.codigo from turma t
	inner join turmaagrupada ta on ta.turmaorigem = t.codigo
	and ta.turma in (
		select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
		
	)
 )
 group by disciplina.codigo, disciplina.nome,  matriculaperiodoturmadisciplina.modalidadedisciplina  
$BODY$
  LANGUAGE sql VOLATILE
  COST 1
  ROWS 1;
  CREATE OR REPLACE FUNCTION aulaaluno_grupooptativacomposta(
    historico integer,
    trazerdisciplinacomposta boolean,
    professor integer,
    apenasturmaorigem boolean)
  RETURNS SETOF horarioaulaaluno AS
$BODY$
 select distinct max(pessoa.codigo) as professor_codigo, 
 array_to_string(array_agg(distinct pessoa.nome), ', ') as professor_nome, 
 array_agg(distinct pessoa.codigo) as professores_codigo,
 disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
 min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino , 'PRESENCIAL' as modalidadeDisciplina
 from horarioturma  
 inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
 inner join turma on turma.codigo = horarioturma.turma
 inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
 inner join gradedisciplinacomposta on gradedisciplinacomposta.disciplina = horarioturmadiaitem.disciplina
 inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina
 inner join disciplina on disciplina.codigo = gradecurriculargrupooptativadisciplina.disciplina
 inner join historico on historico.codigo = $1 and gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina
 inner join matricula on matricula.matricula = historico.matricula
 inner join curso on matricula.curso = curso.codigo
 inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo
 inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor
 left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo 
 and matriculaperiodoturmadisciplina.gradedisciplinacomposta = gradedisciplinacomposta.codigo 
 where  $2 = true and  (($3 is not null and pessoa.codigo = $3) or ($3 is null and pessoa.codigo is not null)) and
((curso.periodicidade = 'AN' and horarioturma.anovigente = matriculaperiodo.ano)
   or (curso.periodicidade = 'SE' and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre)
   or (curso.periodicidade = 'IN')
  ) 
and horarioturma.turma in (
	select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
	union all
	select t.codigo from turma t
	inner join turmaagrupada ta on ta.turmaorigem = t.codigo
	and ta.turma in (
		select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
		
	)
 )
 group by disciplina.codigo, disciplina.nome,  matriculaperiodoturmadisciplina.modalidadedisciplina 
$BODY$
  LANGUAGE sql VOLATILE
  COST 1
  ROWS 1;
  CREATE OR REPLACE FUNCTION aulaaluno_disciplinanormal(
    historico integer,
    trazerdisciplinacomposta boolean,
    professor integer,
    apenasturmaorigem boolean)
  RETURNS SETOF horarioaulaaluno AS
$BODY$
select distinct max(pessoa.codigo) as professor_codigo, 
 array_to_string(array_agg(distinct pessoa.nome), ', ') as professor_nome, 
 array_agg(distinct pessoa.codigo) as professores_codigo,
 disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
 min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino , 'PRESENCIAL' as modalidadeDisciplina  
 from horarioturma  
 inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
 inner join turma on turma.codigo = horarioturma.turma
 inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
 inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina
 inner join historico on historico.codigo = $1
 inner join matricula on matricula.matricula = historico.matricula
 inner join curso on matricula.curso = curso.codigo
 inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo
 inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor
 left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina
 where disciplina.codigo = historico.disciplina 
 and (($3 is not null and pessoa.codigo = $3) or ($3 is null and pessoa.codigo is not null))
 and ((curso.periodicidade = 'AN' and horarioturma.anovigente = matriculaperiodo.ano)
   or (curso.periodicidade = 'SE' and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre)
   or (curso.periodicidade = 'IN')
  ) 
and horarioturma.turma in (
	select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
	union all
	select t.codigo from turma t
	inner join turmaagrupada ta on ta.turmaorigem = t.codigo
	and ta.turma in (
		select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
		
	)
 )
 group by disciplina.codigo, disciplina.nome,  modalidadedisciplina
$BODY$
  LANGUAGE sql VOLATILE
  COST 1
  ROWS 1;
CREATE OR REPLACE FUNCTION aulaaluno_disciplinaequivalente(
    historico integer,
    trazerdisciplinacomposta boolean,
    professor integer,
    apenasturmaorigem boolean)
  RETURNS SETOF horarioaulaaluno AS
$BODY$
select distinct max(pessoa.codigo) as professor_codigo, 
 array_to_string(array_agg(distinct pessoa.nome), ', ') as professor_nome, 
 array_agg(distinct pessoa.codigo) as professores_codigo,
 disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
 min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino , 'PRESENCIAL' as modalidadeDisciplina
 from horarioturma  
 inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
 inner join turma on turma.codigo = horarioturma.turma and turma.turmaagrupada
 inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
 inner join disciplinaequivalente  on disciplinaequivalente.disciplina = horarioturmadiaitem.disciplina
 inner join disciplina on disciplina.codigo = disciplinaequivalente.equivalente
 inner join historico on historico.codigo = $1 and disciplina.codigo = historico.disciplina
 inner join matricula on matricula.matricula = historico.matricula
 inner join curso on matricula.curso = curso.codigo
 inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo
 inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor 
 left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina
 where  (($3 is not null and pessoa.codigo = $3) or ($3 is null and pessoa.codigo is not null)) and
((curso.periodicidade = 'AN' and horarioturma.anovigente = matriculaperiodo.ano)
   or (curso.periodicidade = 'SE' and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre)
   or (curso.periodicidade = 'IN')
  ) 
and horarioturma.turma in (
	select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
	union all
	select t.codigo from turma t
	inner join turmaagrupada ta on ta.turmaorigem = t.codigo
	and ta.turma in (
		select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
		
	)
 )
 group by disciplina.codigo, disciplina.nome,  modalidadedisciplina 
$BODY$
  LANGUAGE sql VOLATILE
  COST 1
  ROWS 1;
CREATE OR REPLACE FUNCTION aulaaluno_disciplinaequivale(
    historico integer,
    trazerdisciplinacomposta boolean,
    professor integer,
    apenasturmaorigem boolean)
  RETURNS SETOF horarioaulaaluno AS
$BODY$
select distinct max(pessoa.codigo) as professor_codigo, 
 array_to_string(array_agg(distinct pessoa.nome), ', ') as professor_nome, 
 array_agg(distinct pessoa.codigo) as professores_codigo,
 disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
 min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino , 'PRESENCIAL' as modalidadeDisciplina 
 from horarioturma  
 inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
 inner join turma on turma.codigo = horarioturma.turma and turma.turmaagrupada
 inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
 inner join disciplinaequivalente  on disciplinaequivalente.equivalente = horarioturmadiaitem.disciplina
 inner join disciplina on disciplina.codigo = disciplinaequivalente.disciplina
 inner join historico on historico.codigo = $1 and disciplina.codigo = historico.disciplina
 inner join matricula on matricula.matricula = historico.matricula
 inner join curso on matricula.curso = curso.codigo
 inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo
 inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor
 left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina
 where  (($3 is not null and pessoa.codigo = $3) or ($3 is null and pessoa.codigo is not null)) and ((curso.periodicidade = 'AN' and horarioturma.anovigente = matriculaperiodo.ano)
   or (curso.periodicidade = 'SE' and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre)
   or (curso.periodicidade = 'IN')
  ) 
and horarioturma.turma in (
	select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null 
		and $4 = false
		and matriculaperiodoturmadisciplina.turma is not null then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null 
		and $4 = false
		then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false 
		then matriculaperiodoturmadisciplina.turmateorica else 0 end
	union all
	select t.codigo from turma t
	inner join turmaagrupada ta on ta.turmaorigem = t.codigo
	and ta.turma in (
		select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null 
		and matriculaperiodoturmadisciplina.turma is not null 
		and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
		
	)
 )
 group by disciplina.codigo, disciplina.nome,  modalidadedisciplina  
$BODY$
  LANGUAGE sql VOLATILE
  COST 1
  ROWS 1;
CREATE OR REPLACE FUNCTION periodoauladisciplinaaluno(
    IN historico integer,
    IN trazerdisciplinacomposta boolean)
  RETURNS TABLE(professor_codigo integer, professor_nome character varying, professores_codigo integer[], disciplina_codigo integer, disciplina_nome character varying, datainicio date, datatermino date, modalidadedisciplina character varying) AS
$BODY$ 
(
(select distinct max(pessoa.codigo) as professor_codigo, 
 array_to_string(array_agg(distinct pessoa.nome), ', ') as professor_nome, 
 array_agg(distinct pessoa.codigo) as professores_codigo,
 disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
 min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino , 'PRESENCIAL' as modalidadeDisciplina 
 from horarioturma  
 inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
 inner join turma on turma.codigo = horarioturma.turma
 inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
 inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina
 inner join historico on historico.codigo = $1
 inner join matricula on matricula.matricula = historico.matricula
 inner join curso on matricula.curso = curso.codigo
 inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo
 inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor
 left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina
 where disciplina.codigo = historico.disciplina

 and ((curso.periodicidade = 'AN' and horarioturma.anovigente = matriculaperiodo.ano)
   or (curso.periodicidade = 'SE' and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre)
   or (curso.periodicidade = 'IN')
  ) 
and horarioturma.turma in (
	select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null then matriculaperiodoturmadisciplina.turmateorica else 0 end
	union all
	select t.codigo from turma t
	inner join turmaagrupada ta on ta.turmaorigem = t.codigo
	and ta.turma in (
		select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null then matriculaperiodoturmadisciplina.turmateorica else 0 end
		
	)
 )
 group by disciplina.codigo, disciplina.nome,  modalidadedisciplina  )
union all
(select distinct max(pessoa.codigo) as professor_codigo, 
 array_to_string(array_agg(distinct pessoa.nome), ', ') as professor_nome, 
 array_agg(distinct pessoa.codigo) as professores_codigo,
 disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
 min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino , 'PRESENCIAL' as modalidadeDisciplina 
 from horarioturma  
 inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
 inner join turma on turma.codigo = horarioturma.turma and turma.turmaagrupada
 inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
 inner join disciplinaequivalente  on disciplinaequivalente.disciplina = horarioturmadiaitem.disciplina
 inner join disciplina on disciplina.codigo = disciplinaequivalente.equivalente
 inner join historico on historico.codigo = $1 and disciplina.codigo = historico.disciplina
 inner join matricula on matricula.matricula = historico.matricula
 inner join curso on matricula.curso = curso.codigo
 inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo
 inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor 
 left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina
 where  
((curso.periodicidade = 'AN' and horarioturma.anovigente = matriculaperiodo.ano)
   or (curso.periodicidade = 'SE' and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre)
   or (curso.periodicidade = 'IN')
  ) 
and horarioturma.turma in (
	select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null then matriculaperiodoturmadisciplina.turmateorica else 0 end
	union all
	select t.codigo from turma t
	inner join turmaagrupada ta on ta.turmaorigem = t.codigo
	and ta.turma in (
		select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null then matriculaperiodoturmadisciplina.turmateorica else 0 end
		
	)
 )
 group by disciplina.codigo, disciplina.nome,  modalidadedisciplina  )

 union all
(select distinct max(pessoa.codigo) as professor_codigo, 
 array_to_string(array_agg(distinct pessoa.nome), ', ') as professor_nome, 
 array_agg(distinct pessoa.codigo) as professores_codigo,
 disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
 min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino , 'PRESENCIAL' as modalidadeDisciplina 
 from horarioturma  
 inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
 inner join turma on turma.codigo = horarioturma.turma and turma.turmaagrupada
 inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
 inner join disciplinaequivalente  on disciplinaequivalente.equivalente = horarioturmadiaitem.disciplina
 inner join disciplina on disciplina.codigo = disciplinaequivalente.disciplina
 inner join historico on historico.codigo = $1 and disciplina.codigo = historico.disciplina
 inner join matricula on matricula.matricula = historico.matricula
 inner join curso on matricula.curso = curso.codigo
 inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo
 inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor
 left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina
 where  ((curso.periodicidade = 'AN' and horarioturma.anovigente = matriculaperiodo.ano)
   or (curso.periodicidade = 'SE' and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre)
   or (curso.periodicidade = 'IN')
  ) 
and horarioturma.turma in (
	select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null then matriculaperiodoturmadisciplina.turmateorica else 0 end
	union all
	select t.codigo from turma t
	inner join turmaagrupada ta on ta.turmaorigem = t.codigo
	and ta.turma in (
		select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null then matriculaperiodoturmadisciplina.turmateorica else 0 end
		
	)
 )
 group by disciplina.codigo, disciplina.nome,  modalidadedisciplina  )
union
(select distinct max(pessoa.codigo) as professor_codigo, 
 array_to_string(array_agg(distinct pessoa.nome), ', ') as professor_nome, 
 array_agg(distinct pessoa.codigo) as professores_codigo,
 disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
 min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino , 'PRESENCIAL' as modalidadeDisciplina 
 from horarioturma  
 inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
 inner join turma on turma.codigo = horarioturma.turma
 inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
 inner join gradedisciplinacomposta on gradedisciplinacomposta.disciplina = horarioturmadiaitem.disciplina
 inner join gradedisciplina on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina
 inner join disciplina on disciplina.codigo = gradedisciplina.disciplina
 inner join historico on historico.codigo = $1 and gradedisciplina.codigo = historico.gradedisciplina
 inner join matricula on matricula.matricula = historico.matricula
 inner join curso on matricula.curso = curso.codigo
 inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo
 inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor
 left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo 
 and matriculaperiodoturmadisciplina.gradedisciplinacomposta = gradedisciplinacomposta.codigo 
 where  $2 = true and
((curso.periodicidade = 'AN' and horarioturma.anovigente = matriculaperiodo.ano)
   or (curso.periodicidade = 'SE' and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre)
   or (curso.periodicidade = 'IN')
  ) 
and horarioturma.turma in (
	select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null then matriculaperiodoturmadisciplina.turmateorica else 0 end
	union all
	select t.codigo from turma t
	inner join turmaagrupada ta on ta.turmaorigem = t.codigo
	and ta.turma in (
		select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null then matriculaperiodoturmadisciplina.turmateorica else 0 end
		
	)
 )
 group by disciplina.codigo, disciplina.nome,  matriculaperiodoturmadisciplina.modalidadedisciplina  )
 union
 (select distinct max(pessoa.codigo) as professor_codigo, 
 array_to_string(array_agg(distinct pessoa.nome), ', ') as professor_nome, 
 array_agg(distinct pessoa.codigo) as professores_codigo,
 disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
 min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino , 'PRESENCIAL' as modalidadeDisciplina 
 from horarioturma  
 inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
 inner join turma on turma.codigo = horarioturma.turma
 inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
 inner join gradedisciplinacomposta on gradedisciplinacomposta.disciplina = horarioturmadiaitem.disciplina
 inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina
 inner join disciplina on disciplina.codigo = gradecurriculargrupooptativadisciplina.disciplina
 inner join historico on historico.codigo = $1 and gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina
 inner join matricula on matricula.matricula = historico.matricula
 inner join curso on matricula.curso = curso.codigo
 inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo
 inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor
 left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo 
 and matriculaperiodoturmadisciplina.gradedisciplinacomposta = gradedisciplinacomposta.codigo 
 where  $2 = true and
((curso.periodicidade = 'AN' and horarioturma.anovigente = matriculaperiodo.ano)
   or (curso.periodicidade = 'SE' and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre)
   or (curso.periodicidade = 'IN')
  ) 
and horarioturma.turma in (
	select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null then matriculaperiodoturmadisciplina.turmateorica else 0 end
	union all
	select t.codigo from turma t
	inner join turmaagrupada ta on ta.turmaorigem = t.codigo
	and ta.turma in (
		select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null then matriculaperiodoturmadisciplina.turmateorica else 0 end
		
	)
 )
 group by disciplina.codigo, disciplina.nome,  matriculaperiodoturmadisciplina.modalidadedisciplina  )

 
union 
(select distinct pessoa.codigo as professor_codigo , pessoa.nome as professor_nome, array_agg(pessoa.codigo) as professores_codigo,
disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome,
null::date as datainicio, null::date as datatermino, 'ON_LINE' as modalidadeDisciplina 
from matriculaperiodoturmadisciplina 
inner join turmadisciplina on turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina and turmadisciplina.definicoestutoriaonline = 'DINAMICA' 
inner join disciplina on disciplina.codigo = turmadisciplina.disciplina 
inner join turma on turma.codigo = turmadisciplina.turma
inner join historico on historico.codigo = $1 and matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina 
inner join pessoa on pessoa.codigo = matriculaperiodoturmadisciplina.professor
where disciplina.codigo = historico.disciplina
and (( turma.turmaagrupada = false and matriculaperiodoturmadisciplina.turma = turma.codigo) or (turma.turmaagrupada and matriculaperiodoturmadisciplina.turma in (select t.codigo from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo)))
group by pessoa.codigo , pessoa.nome, disciplina.codigo, disciplina.nome
) 
)
  $BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;

----------- Marcos Paulo 13/05/2019 09:30 ------------
update unidadeensino set configuracoes = (
select codigo from configuracaofinanceiro where configuracoes = (SELECT codigo FROM Configuracoes where padrao)
) 
where configuracoes is null;

----------- Carlos 17/05/2019 09:55 -------------
alter table configuracaoacademiconota add column tipoUsoNota varchar(25) default('NENHUM');

----------- Leandro 22/05/2019 09:00 ------------
CREATE OR REPLACE FUNCTION fn_validarNotaFiscalEntradaFornecedorNumeroSerie(notafiscalentrada_fornecedor integer, 
	notafiscalentrada_numero bigint, 
	notafiscalentrada_serie varchar(100),
	notafiscalentrada_codigo integer) RETURNS boolean AS $$
DECLARE 
   quantidaderegistros integer;
   begin
	select into quantidaderegistros count(codigo) from notafiscalentrada where fornecedor = notafiscalentrada_fornecedor
		and numero = notafiscalentrada_numero
		and lower(serie) = lower(notafiscalentrada_serie)
		and codigo != notafiscalentrada_codigo;
	
    IF coalesce(quantidaderegistros, 0) > 0 THEN
       RETURN FALSE; 
    ELSE 
       RETURN TRUE;  
    END IF;                 
END;     
$$ LANGUAGE plpgsql;
ALTER TABLE notafiscalentrada ADD CONSTRAINT check_notafiscalentrada_fornecedor_numero_serie CHECK 
	(fn_validarNotaFiscalEntradaFornecedorNumeroSerie(fornecedor, numero, serie, codigo)) not valid;
	
	
----------------- RODRIGO 22/05/2019 17:33 7.0.1.2m ----------
alter table periodoletivo add column configuracaoacademico int;
alter table periodoletivo add constraint fk_periodoletivo_configuracaoacademico foreign key (configuracaoacademico) references configuracaoacademico(codigo);

	
	----------- Thyago Jayme 27/05/2019 ------------
alter table contacorrente add column habilitarProtestoBoleto boolean default false;
alter table contacorrente add column qtdDiasProtestoBoleto integer;

----------- Renato Borges 30/05/2019 17:01 ------------
ALTER TABLE unidadeensino ALTER COLUMN credenciamentoPortaria TYPE text;

ALTER TABLE unidadeensino ADD COLUMN cnpjmantenedora varchar(20);
ALTER TABLE unidadeensino ADD COLUMN unidadecertificadora varchar(255);
ALTER TABLE unidadeensino ADD COLUMN cnpjunidadecertificadora varchar(20);
ALTER TABLE unidadeensino ADD COLUMN codigoiesunidadecertificadora integer;


----------- Cesar Henrique 31/05/2019 15:07 ------------

CREATE OR REPLACE FUNCTION public.consultarplanoensino(unidadeensino integer, curso integer, ano character varying, semestre character varying, disciplina integer, turno integer, professor integer, situacao character varying)
 RETURNS TABLE(codigo integer)
 LANGUAGE sql
 COST 1 ROWS 1
AS $function$
 
     select codigo from (  
select row_number() over(partition by planoensino.unidadeensino, planoensino.curso, planoensino.disciplina order by planoensino.unidadeensino, planoensino.curso, planoensino.disciplina,						 
						 case when planoensino.professorresponsavel is not null then 1 else 2 end, 						 
						 case when planoensino.turno is not null then 1 else 2 end, 
		planoensino.ano  desc,
		planoensino.semestre desc) as ordem,
planoensino.codigo, planoensino.unidadeensino, planoensino.curso, planoensino.turno, planoensino.disciplina, planoensino.professorresponsavel
from planoensino
inner join curso on curso.codigo = planoensino.curso
where 
		 ((curso.periodicidade = 'SE' and (planoensino.ano||planoensino.semestre) <= ($3||$4))
		 or (curso.periodicidade = 'AN' and planoensino.ano <= $3)
		 or curso.periodicidade = 'IN')
and planoensino.unidadeensino = $1
and planoensino.curso = $2
and planoensino.disciplina = $5  
and (planoensino.professorresponsavel = $7  or professorresponsavel is null)
and (planoensino.turno = $6  or turno is null)
and ($8 is null or $8 = '' or situacao = $8)
order by ordem limit 1 
  ) as t
$function$
;


----------- Renato Borges 03/06/2019 16:48 7.0.1------------
alter table avaliacaoinstitucional add column avaliardisciplinasreposicao boolean;


----------- Edson Alves 04/06/2019 14:55 7.0.1------------
ALTER TABLE public.planoensino RENAME COLUMN "habilidadecompetencia" TO perfilegresso; 

----------- Marcos Paulo 05/06/2019 7.0.1------------
CREATE OR REPLACE FUNCTION public.periodoauladisciplinaaluno(turma integer, disciplina integer, periodicidade character varying, ano character varying, semestre character varying)
RETURNS TABLE(professor_codigo integer, professor_nome character varying, disciplina_codigo integer, disciplina_nome character varying, datainicio date, datatermino date, sala_codigo integer, sala_nome character varying, local_codigo integer, local_nome character varying)
LANGUAGE sql
AS $function$ 
(
select professor.codigo as professor_codigo, professor.nome as professor_nome, 
t.disciplina_codigo, t.disciplina_nome, t.datainicio , t.datatermino, 
sala.codigo as sala_codigo , sala.sala as sala_nome, local.codigo as local_codigo , local.local as local_nome 
from ( 
select distinct disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino,
max(horarioturmadiaitem.codigo) as horarioturmadiaitem,
turma.codigo as turma_codigo
from horarioturma 
inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
inner join turma on turma.codigo = horarioturma.turma
inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor
inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina
where disciplina.codigo = $2
and (($3 = 'AN' and horarioturma.anovigente = $4)
or ($3 = 'SE' and horarioturma.anovigente = $4 and horarioturma.semestrevigente = $5)
or ($3 = 'IN')
) 
and (horarioturma.turma = $1 or exists (select turmaagrupada.turmaorigem from turmaagrupada where turmaagrupada.turmaorigem = horarioturma.turma and turmaagrupada.turma = $1 ))

group by disciplina.codigo, disciplina.nome, turma.codigo

union 
select distinct disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino,
max(horarioturmadiaitem.codigo) as horarioturmadiaitem,
turma.codigo as turma_codigo
from horarioturma 
inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
inner join turma on turma.codigo = horarioturma.turma
inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor

inner join disciplinaequivalente on disciplinaequivalente.disciplina = horarioturmadiaitem.disciplina
inner join disciplina on disciplina.codigo = disciplinaequivalente.equivalente
where disciplina.codigo = $2 and turma.turmaagrupada
and (($3 = 'AN' and horarioturma.anovigente = $4)
or ($3 = 'SE' and horarioturma.anovigente = $4 and horarioturma.semestrevigente = $5)
or ($3 = 'IN')
) 
and (horarioturma.turma = $1 or exists (select turmaagrupada.turmaorigem from turmaagrupada where turmaagrupada.turmaorigem = horarioturma.turma and turmaagrupada.turma = $1 ))

group by disciplina.codigo, disciplina.nome, turma.codigo

union 
select distinct disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino,
max(horarioturmadiaitem.codigo) as horarioturmadiaitem, 
turma.codigo as turma_codigo
from horarioturma 
inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
inner join turma on turma.codigo = horarioturma.turma
inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
inner join pessoa on pessoa.codigo = horarioturmadiaitem.professor 
inner join disciplinaequivalente on disciplinaequivalente.equivalente = horarioturmadiaitem.disciplina
inner join disciplina on disciplina.codigo = disciplinaequivalente.disciplina
where disciplina.codigo = $2 and turma.turmaagrupada
and (($3 = 'AN' and horarioturma.anovigente = $4)
or ($3 = 'SE' and horarioturma.anovigente = $4 and horarioturma.semestrevigente = $5)
or ($3 = 'IN')
) 
and (horarioturma.turma = $1 or exists (select turmaagrupada.turmaorigem from turmaagrupada where turmaagrupada.turmaorigem = horarioturma.turma and turmaagrupada.turma = $1 ))

group by disciplina.codigo, disciplina.nome, turma.codigo

) as t
inner join horarioturmadiaitem on horarioturmadiaitem.codigo = t.horarioturmadiaitem
left join professortitulardisciplinaturma on professortitulardisciplinaturma.disciplina = t.disciplina_codigo
and professortitulardisciplinaturma.turma = t.turma_codigo 
and (($3 = 'AN' and professortitulardisciplinaturma.ano = $4)
or ($3 = 'SE' and professortitulardisciplinaturma.ano = $4 and professortitulardisciplinaturma.semestre = $5)
or ($3 = 'IN')
) and professortitulardisciplinaturma.titular
inner join pessoa as professor on ((professortitulardisciplinaturma.codigo is not null and professortitulardisciplinaturma.professor = professor.codigo) or (professortitulardisciplinaturma.codigo is null and professor.codigo = horarioturmadiaitem.professor))
left join salalocalaula as sala on sala.codigo = horarioturmadiaitem.sala
left join localaula as local on local.codigo = sala.localaula


order by case when professortitulardisciplinaturma.titular then 0 else 1 end 

)

$function$
;

----------- Edson ALves  05/06/2019 14:22 7.0.1------------

CREATE OR REPLACE VIEW horarioturmaprofessordisciplina AS 
 SELECT DISTINCT horarioturma.codigo as horarioturma,
    horarioturmadiaitem.disciplina,
    horarioturmadiaitem.professor,
    horarioturma.turma
   FROM horarioturma
   inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo
   inner join horarioturmadiaitem on horarioturmadia.codigo = horarioturmadiaitem.horarioturmadia  ;
  
  
  
DROP FUNCTION horarioturmadetalhado(integer, integer, integer, timestamp without time zone);

CREATE OR REPLACE FUNCTION horarioturmadetalhado(
    IN turma integer,
    IN professor integer,
    IN disciplina integer,
    IN data timestamp without time zone)
  RETURNS TABLE(horarioturma integer, turma integer, identificadorturma character varying, data timestamp without time zone, disciplina integer, professor integer, nraula character varying, ocultardataaula boolean) AS
$BODY$ 

	select horarioturma.codigo as horarioturma, turma.codigo as turma, turma.identificadorturma, horarioturmadia.data, 
	disciplina.codigo as disciplina, professor.codigo as professor, horarioturmadiaitem.nraula::VARCHAR as nraula, ocultardataaula  
	from horarioturma     
	inner join turma on turma.codigo = horarioturma.turma   
	inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma  	
	inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo   
	inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina
	inner join pessoa as professor on professor.codigo =  horarioturmadiaitem.professor
	where case when $1 is null then true else turma.codigo = $1 end = true 
	and case when $4 is null then true else horarioturmadia.data = $4 end  = true
	and case when $3 is null then true else disciplina.codigo = $3 end  = true
	and case when $2 is null then true else professor.codigo = $2 end = true 

	$BODY$
  LANGUAGE sql VOLATILE
  COST 100
  ROWS 1000;
ALTER FUNCTION horarioturmadetalhado(integer, integer, integer, timestamp without time zone)
  OWNER TO postgres;
 
 
 DROP FUNCTION horarioturmadetalhado(integer, integer, character varying, character varying, integer, integer, timestamp without time zone, timestamp without time zone);
    
 
  CREATE OR REPLACE FUNCTION public.horarioturmadetalhado(horarioturma integer, turma integer, ano character varying, semestre character varying, professor integer, disciplina integer, datainicio timestamp without time zone, datatermino timestamp without time zone)
 RETURNS TABLE(horarioturma integer, turma integer, identificadorturma character varying, horarioturmadia integer, horarioturmadiaitem integer, data timestamp without time zone, disciplina integer, professor integer, horariodisciplinaprofessor character varying, nraula integer, duracaoaula integer, horarioinicioaula character varying, horariofinalaula character varying, diasemana character varying, turno integer, ocultardataaula boolean, sala integer)
 LANGUAGE sql
AS $function$ 

	select horarioturma.codigo as horarioturma, turma.codigo as turma, turma.identificadorturma, horarioturmadia.codigo as horarioturmadia, horarioturmadiaitem.codigo as horarioturmadiaitem, 
        horarioturmadia.data, horarioturmadiaitem.disciplina, horarioturmadiaitem.professor,
	horarioturmadia.horariodisciplinaprofessor, horarioturmadiaitem.nrAula,
	horarioturmadiaitem.duracaoaula, horarioturmadiaitem.horarioinicio as horarioinicioaula, horarioturmadiaitem.horariotermino as horariofinalaula, 
	('0'::VARCHAR(1)||TO_CHAR(horarioturmadia.data, 'D'))::VARCHAR(2) as diasemana, turma.turno, horarioturmadia.ocultardataaula,
	horarioturmadiaitem.sala
	from   horarioturma   	
	inner join turma on turma.codigo = horarioturma.turma  	
	inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma  
	inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo  
	inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina
    inner join pessoa as professor on professor.codigo = horarioturmadiaitem.professor
	where 
	case when $1 is null then true else horarioturma.codigo = $1 end  = true
	
	and case when $2 is null then true else turma.codigo = $2 end = true 
	
	and case when $3 is null then true else anovigente = $3 end = true 
	
	and case when $4 is null then true else semestrevigente = $4 end = true 	
	
	and case when $7 is null and $8 is null then true else 
	case when $7 is not null and $8 is null then horarioturmadia.data = $7 else 	
	case when $7 is null and $8 is not null then horarioturmadia.data = $8 else 
	horarioturmadia.data >= $7 and horarioturmadia.data <= $8
	end end end = true	
	
	and case when $5 is null then true else professor.codigo = $5 end = true 
	
	and case when $6 is null then true else disciplina.codigo = $6 end  = true 
 
 $function$
;


--------------RODRIGO 05/06/2019 7.0.1.0 -------------
 update convenio set tipofinanciamentoestudantil = 'FI' where descricao ilike ('%FIES%') and tipofinanciamentoestudantil is null;
 update convenio set tipofinanciamentoestudantil = 'PI' where descricao ilike ('%PROUNI%') and tipofinanciamentoestudantil is null and descontoparcela = 100.0;
 update convenio set tipofinanciamentoestudantil = 'PP' where descricao ilike ('%PROUNI%') and tipofinanciamentoestudantil is null and descontoparcela < 100.0;

--------------EDSON 07/06/2019 7.0.1.0 -------------
alter table  gradecurricular ALTER COLUMN sistemaavaliacao TYPE text;

----------------- Marcos Paulo 04/06/2019----------	
alter table configuracaoacademico add column formulaCoeficienteRendimento text;
alter table matricula add column proficienciaLinguaEstrangeira varchar(50);
alter table matricula add column situacaoProficienciaLinguaEstrangeira varchar(50);


-------                  Jean Pierre 11/06/19 09:55 --------
alter table reserva                                 add primary key (codigo);
alter table campanhapublicoalvoprospect             add primary key (codigo);
alter table arquivobackup                           add primary key (codigo);
alter table cartaoresposta                          add primary key (codigo);
alter table contapagarcontroleremessacontapagar     add primary key (codigo);
ALTER TABLE contarecebernaolocalizadaarquivoretorno ADD PRIMARY KEY (codigo);
alter table controleremessadatageracao              add primary key (codigo);
ALTER TABLE followup                                ADD PRIMARY KEY (codigo);
ALTER TABLE documetacaopessoa                       ADD PRIMARY KEY (codigo);
ALTER TABLE fraseinspiracao                         ADD PRIMARY KEY (codigo);
ALTER TABLE historicofollowup                       ADD PRIMARY KEY (codigo);
alter table logcontacorrente                        add primary key (codigo);
alter table logexclusaomatricula                    add primary key (codigo);
alter table logfechamento                           add primary key (codigo);
alter table logimpressaocontrato                    add primary key (codigo);
alter table logtransferenciacatalogoperiodico       add primary key (codigo);
alter table logturma                                add primary key (codigo);
alter table parametrorelatorio                      add primary key (codigo);
alter table observacaocomplementarhistoricoaluno    add primary key (codigo);
alter table notificacaoregistroaulanota             add primary key (codigo);
alter table titulacaoprofessorcurso                 add primary key (codigo);
alter table tipoautoria                             add primary key (codigo);
alter table operacaofuncionalidade                  add primary key (codigo);
alter table preinscricaolog                         add primary key (codigo);
alter table resultadoprocessoseletivoprovaresposta  add primary key (codigo);
select admin.corrigir_sequencias_diferentes('public', true);
----------------- Marcos Paulo 12/06/2019----------	
CREATE OR REPLACE FUNCTION fn_validardataemprestimo()
RETURNS trigger AS
$BODY$
BEGIN 
IF (NEW.data::date < current_date) THEN
RAISE EXCEPTION 'A data do emprestimo é menor que a data de hoje' ;
END IF;
RETURN new;
END;
$BODY$
LANGUAGE plpgsql VOLATILE
COST 100;


CREATE TRIGGER tg_validarsituacaoemprestimo BEFORE insert ON emprestimo 
FOR EACH ROW EXECUTE PROCEDURE fn_validardataemprestimo();

----------------- Gabriel Dias 14/06/2019-------------------------

update controlelivroregistrodiploma set niveleducacional = curso.niveleducacional 
 from curso
where curso.codigo = controlelivroregistrodiploma.curso
  and controlelivroregistrodiploma.niveleducacional is null;
  
---------------- JACKELINE 18/06/2019 08:10 ---------------------
alter table pessoa add tipomidiacaptacao int4 null;
alter table pessoa add constraint fk_pessoa_tipomidiacaptacao foreign key (tipomidiacaptacao) references tipomidiacaptacao(codigo) match simple on update restrict on delete restrict;	

 ---------- Ana Claudia 19/06/2019 14:22 7.0.1------------------
alter table procseletivo add column tipoTransferencia boolean default false;
alter table procseletivo add column obrigarUploadComprovanteTransferencia boolean default false;
alter table procseletivo add column orientacaoTransferencia text; 

--------------- Rodrigo 19/06/2019 15:21 -----------
update catalogo set linguapublicacao =  null where linguapublicacao is not null and not exists (
	select codigo from linguapublicacaocatalogo where linguapublicacaocatalogo.codigo =catalogo.linguapublicacao
);
alter table catalogo add constraint fk_catalogo_linguapublicacao foreign key (linguaPublicacao) references linguapublicacaocatalogo(codigo);

update catalogo set cidadepublicacao =  null where cidadepublicacao is not null and not exists (
	select codigo from cidadepublicacaocatalogo where cidadepublicacaocatalogo.codigo =catalogo.cidadepublicacao
);
alter table catalogo add constraint fk_catalogo_cidadepublicacao foreign key (cidadepublicacao) references cidadepublicacaocatalogo(codigo);

alter table catalogo add constraint fk_catalogo_editora foreign key (editora) references editora(codigo);
alter table catalogo add constraint fk_catalogo_responsavel foreign key (responsavel) references usuario(codigo);
alter table catalogo add constraint fk_catalogo_responsavelAtualizacao foreign key (responsavelAtualizacao) references usuario(codigo);



---------- rodrigo 21/06/2019 7.0.2.0 --------------

alter table registrodetalhe add column valorliquido_tmp NUMERIC(20,2);
alter table registrodetalhe add column valorpago_tmp NUMERIC(20,2);
alter table registrodetalhe add column valorabatimento_tmp NUMERIC(20,2);
alter table registrodetalhe add column valordesconto_tmp NUMERIC(20,2);
alter table registrodetalhe add column valornominaltitulo_tmp NUMERIC(20,2);

update registrodetalhe set valorliquido_tmp = (((valorliquido*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2) where valorliquido::numeric(20,2) != (((valorliquido*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2);
update registrodetalhe set valorpago_tmp = (((valorpago*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2) where valorpago::numeric(20,2) != (((valorpago*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2);
update registrodetalhe set valorabatimento_tmp = (((valorabatimento*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2) where valorabatimento::numeric(20,2) != (((valorabatimento*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2);
update registrodetalhe set valordesconto_tmp = (((valordesconto*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2) where valordesconto::numeric(20,2) != (((valordesconto*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2);
update registrodetalhe set valornominaltitulo_tmp = (((valornominaltitulo*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2) where valornominaltitulo::numeric(20,2) != (((valornominaltitulo*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2);

alter table registrodetalhe alter column valorliquido type NUMERIC(20,2);
alter table registrodetalhe alter column valorpago type  NUMERIC(20,2);
alter table registrodetalhe alter column valorabatimento type  NUMERIC(20,2);
alter table registrodetalhe alter column valordesconto type  NUMERIC(20,2);
alter table registrodetalhe alter column valornominaltitulo type  NUMERIC(20,2);

update registrodetalhe set valorliquido = valorliquido_tmp where valorliquido != valorliquido_tmp and valorliquido_tmp is not null;
update registrodetalhe set valorpago = valorpago_tmp where valorpago != valorpago_tmp and valorpago_tmp is not null;
update registrodetalhe set valorabatimento = valorabatimento_tmp where valorabatimento != valorabatimento_tmp and valorabatimento_tmp is not null;
update registrodetalhe set valordesconto = valordesconto_tmp where valordesconto != valordesconto_tmp and valordesconto_tmp is not null;
update registrodetalhe set valornominaltitulo = valornominaltitulo_tmp where valornominaltitulo != valornominaltitulo_tmp and valornominaltitulo_tmp is not null;

---------------------Gabriel Dias 24/06/2019 ------------------	 
alter table controlelivrofolharecibo  add column situacao varchar(30);
---------------------Gabriel Dias 24/06/2019 ------------------		
alter table controlelivroregistrodiploma  add column niveleducacional varchar(10);




---- Alessandro 24/06/2019 ----
alter table configuracaofinanceiro add column operadora text not null default 'NENHUM';
alter table configuracaofinanceiro add column tokenrede text;
alter table configuracaofinanceiro add column pvrede text;

alter table configuracaofinanceirocartaorecebimento add column qtdeDiasInicialParcelarContaVencida integer default 0;
alter table configuracaofinanceirocartaorecebimento add column qtdeDiasFinalParcelarContaVencida integer;
update configuracaofinanceirocartaorecebimento set qtdeDiasFinalParcelarContaVencida = 10000 where qtdeDiasFinalParcelarContaVencida is null;
alter table configuracaofinanceirocartaorecebimento alter column qtdeDiasFinalParcelarContaVencida set default 0;
alter table configuracaofinanceirocartaorecebimento add column visao text default 'ADMINISTRATIVA';

alter table configuracaorecebimentocartaoonline add column valorminimorecebimentocartaodebito numeric(20,2);
alter table configuracaorecebimentocartaoonline add column permiterecebercontabibliotecadebito boolean;
alter table configuracaorecebimentocartaoonline add column permiterecebercontacontratoreceitadebito boolean;
alter table configuracaorecebimentocartaoonline add column permiterecebercontaoutrosdebito boolean;
alter table configuracaorecebimentocartaoonline add column permiterecebercontadevolucaochequedebito boolean;
alter table configuracaorecebimentocartaoonline add column permiterecebercontainclusaoreposicaodebito boolean;
alter table configuracaorecebimentocartaoonline add column permiterecebercontainscricaoprocessoseletivodebito boolean;
alter table configuracaorecebimentocartaoonline add column permiterecebercontarequerimentodebito boolean;
alter table configuracaorecebimentocartaoonline add column permiterecebercontanegociacaodebito boolean;
alter table configuracaorecebimentocartaoonline add column habilitarrenegociacaoonlinedebito boolean;
alter table configuracaorecebimentocartaoonline add column permiterecebercontamatriculadebito boolean;
alter table configuracaorecebimentocartaoonline add column habilitarrecebimentomatriculaonlinedebito boolean;
alter table configuracaorecebimentocartaoonline add column habilitarrecebimentomatricularenovacaoonlinedebito boolean;
alter table configuracaorecebimentocartaoonline add column permiterecebercontamensalidadedebito boolean;
alter table configuracaorecebimentocartaoonline add column habilitarrecebimentomensalidadematriculaonlinedebito boolean;
alter table configuracaorecebimentocartaoonline add column habilitarrecebimentomensalidaderenovacaoonlinedebito boolean;
alter table configuracaorecebimentocartaoonline add column permitirrecebercontavencidadebito boolean;
alter table configuracaorecebimentocartaoonline add column numeromaximodiasrecebercontavencidadebito integer;
alter table configuracaorecebimentocartaoonline add column permiterecebercontamaterialdidaticodebito boolean;
alter table configuracaorecebimentocartaoonline add column permitirCartao text not null default 'CREDITO';

alter table operadoracartao add column formaPagamentoPadraoRecebimentoOnline integer;
alter table operadoracartao add constraint fk_operadoracartao_formapagamento FOREIGN KEY (formaPagamentoPadraoRecebimentoOnline) REFERENCES formapagamento (codigo);

alter table transacaocartaocredito rename to transacaocartaoonline;
alter table transacaocartaoonline add column tipocartao text;
update transacaocartaoonline set tipocartao = 'CARTAO_CREDITO' where tipocartao is null;


---------- rodrigo 25/06/2019 7.0.2.0 --------------

alter table registrodetalhe add column valorliquido_tmp NUMERIC(20,2);
alter table registrodetalhe add column valorpago_tmp NUMERIC(20,2);
alter table registrodetalhe add column valorabatimento_tmp NUMERIC(20,2);
alter table registrodetalhe add column valordesconto_tmp NUMERIC(20,2);
alter table registrodetalhe add column valornominaltitulo_tmp NUMERIC(20,2);

update registrodetalhe set valorliquido_tmp = (((valorliquido*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2) where valorliquido::numeric(20,2) != (((valorliquido*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2);
update registrodetalhe set valorpago_tmp = (((valorpago*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2) where valorpago::numeric(20,2) != (((valorpago*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2);
update registrodetalhe set valorabatimento_tmp = (((valorabatimento*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2) where valorabatimento::numeric(20,2) != (((valorabatimento*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2);
update registrodetalhe set valordesconto_tmp = (((valordesconto*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2) where valordesconto::numeric(20,2) != (((valordesconto*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2);
update registrodetalhe set valornominaltitulo_tmp = (((valornominaltitulo*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2) where valornominaltitulo::numeric(20,2) != (((valornominaltitulo*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2);

alter table registrodetalhe alter column valorliquido type NUMERIC(20,2);
alter table registrodetalhe alter column valorpago type  NUMERIC(20,2);
alter table registrodetalhe alter column valorabatimento type  NUMERIC(20,2);
alter table registrodetalhe alter column valordesconto type  NUMERIC(20,2);
alter table registrodetalhe alter column valornominaltitulo type  NUMERIC(20,2);

update registrodetalhe set valorliquido = valorliquido_tmp where valorliquido != valorliquido_tmp and valorliquido_tmp is not null;
update registrodetalhe set valorpago = valorpago_tmp where valorpago != valorpago_tmp and valorpago_tmp is not null;
update registrodetalhe set valorabatimento = valorabatimento_tmp where valorabatimento != valorabatimento_tmp and valorabatimento_tmp is not null;
update registrodetalhe set valordesconto = valordesconto_tmp where valordesconto != valordesconto_tmp and valordesconto_tmp is not null;
update registrodetalhe set valornominaltitulo = valornominaltitulo_tmp where valornominaltitulo != valornominaltitulo_tmp and valornominaltitulo_tmp is not null;


alter table contareceberregistroarquivo add column valorrecebido_tmp NUMERIC(20,2);
alter table contareceberregistroarquivo add column valor_tmp NUMERIC(20,2);
update contareceberregistroarquivo set valorrecebido_tmp = (((valorrecebido*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2) where valorrecebido::numeric(20,2) != (((valorrecebido*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2);
update contareceberregistroarquivo set valor_tmp = (((valor*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2) where valor::numeric(20,2) != (((valor*1000)::NUMERIC(20,2))/1000)::NUMERIC(20,2);
alter table contareceberregistroarquivo alter column valorrecebido type NUMERIC(20,2);
alter table contareceberregistroarquivo alter column valor type  NUMERIC(20,2);

update contareceberregistroarquivo set valorrecebido = valorrecebido_tmp where valorrecebido != valorrecebido_tmp and valorrecebido_tmp is not null;
update contareceberregistroarquivo set valor = valor_tmp where valor != valor_tmp and valor_tmp is not null;

----------- Gabriel Dias 26/06/2019 15:54 ------------
alter table controlelivroregistrodiploma alter column curso drop not null;
---- MARCO 03/07/2019 14:14 VERSAO 7.1.2.0
insert into novidadesei (versao, url, data, descricao) values ('Versão 7.1.2.0', 'https://portal.otimize-ti.com.br/news/versao_7/versao7120.html', '2019-07-03', 'Novidades SEI Versão 7.1.2.0');

---- EDSON 03/07/2019 14:40 VERSAO 7.0.1.0
CREATE OR REPLACE FUNCTION public.aulaaluno_disciplinaead(historico integer, trazerdisciplinacomposta boolean, professor integer)
 RETURNS SETOF horarioaulaaluno
 LANGUAGE sql
 COST 1 ROWS 1
AS $function$
    select distinct pessoa.codigo as professor_codigo , pessoa.nome as professor_nome, array_agg(pessoa.codigo) as professores_codigo,
disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome,
null::date as datainicio, null::date as datatermino, 'ON_LINE' as modalidadeDisciplina
from matriculaperiodoturmadisciplina 
inner join turmadisciplina on turmadisciplina.disciplina = matriculaperiodoturmadisciplina.disciplina and turmadisciplina.definicoestutoriaonline = 'DINAMICA'
inner join disciplina on disciplina.codigo = turmadisciplina.disciplina
inner join matricula on matricula.matricula = matriculaperiodoturmadisciplina.matricula
inner join curso on matricula.curso = curso.codigo
inner join turma on turma.codigo = turmadisciplina.turma
inner join historico on historico.codigo = $1 and matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina 
left join professortitulardisciplinaturma on professortitulardisciplinaturma.disciplina = disciplina.codigo
	and professortitulardisciplinaturma.turma = turma.codigo 
	and ((curso.periodicidade = 'AN' and professortitulardisciplinaturma.ano = matriculaperiodoturmadisciplina.ano)
	or (curso.periodicidade = 'SE' and professortitulardisciplinaturma.ano = matriculaperiodoturmadisciplina.ano and professortitulardisciplinaturma.semestre = matriculaperiodoturmadisciplina.semestre)
	or (curso.periodicidade = 'IN')
	) and professortitulardisciplinaturma.titular and ($3 is null or $3 = 0)
inner join pessoa on ((professortitulardisciplinaturma.codigo is not null and professortitulardisciplinaturma.professor = pessoa.codigo) or (professortitulardisciplinaturma.codigo is null and pessoa.codigo = matriculaperiodoturmadisciplina.professor))
where disciplina.codigo = historico.disciplina and (($3 is not null and pessoa.codigo = $3) or ($3 is null and pessoa.codigo is not null)) 
and (( turma.turmaagrupada = false and matriculaperiodoturmadisciplina.turma = turma.codigo) or (turma.turmaagrupada and matriculaperiodoturmadisciplina.turma in (select t.codigo from turmaagrupada inner join turma as t on t.codigo = turmaagrupada.turma where turmaagrupada.turmaorigem = turma.codigo)))
group by pessoa.codigo , pessoa.nome, disciplina.codigo, disciplina.nome
    $function$
;

---- EDSON 03/07/2019 14:40 VERSAO 7.0.1.0

CREATE OR REPLACE FUNCTION public.aulaaluno_disciplinaequivale(historico integer, trazerdisciplinacomposta boolean, professor integer, apenasturmaorigem boolean)
 RETURNS SETOF horarioaulaaluno
 LANGUAGE sql
 COST 1 ROWS 1
AS $function$
select distinct max(pessoa.codigo) as professor_codigo, 
 array_to_string(array_agg(distinct pessoa.nome), ', ') as professor_nome, 
 array_agg(distinct pessoa.codigo) as professores_codigo,
 disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
 min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino , 'PRESENCIAL' as modalidadeDisciplina 
 from horarioturma  
 inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
 inner join turma on turma.codigo = horarioturma.turma and turma.turmaagrupada
 inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
 inner join disciplinaequivalente  on disciplinaequivalente.equivalente = horarioturmadiaitem.disciplina
 inner join disciplina on disciplina.codigo = disciplinaequivalente.disciplina
 inner join historico on historico.codigo = $1 and disciplina.codigo = historico.disciplina
 inner join matricula on matricula.matricula = historico.matricula
 inner join curso on matricula.curso = curso.codigo
 inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo 
 left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina
 left join professortitulardisciplinaturma on professortitulardisciplinaturma.disciplina = horarioturmadiaitem.disciplina
	and professortitulardisciplinaturma.turma = turma.codigo 
	and ((curso.periodicidade = 'AN' and professortitulardisciplinaturma.ano = matriculaperiodo.ano)
	or (curso.periodicidade = 'SE' and professortitulardisciplinaturma.ano = matriculaperiodo.ano and professortitulardisciplinaturma.semestre = matriculaperiodo.semestre)
	or (curso.periodicidade = 'IN')
	) and professortitulardisciplinaturma.titular and ($3 is null or $3 = 0)
inner join pessoa on ((professortitulardisciplinaturma.codigo is not null and professortitulardisciplinaturma.professor = pessoa.codigo) or (professortitulardisciplinaturma.codigo is null and pessoa.codigo = horarioturmadiaitem.professor))
 where  (($3 is not null and pessoa.codigo = $3) or ($3 is null and pessoa.codigo is not null)) and ((curso.periodicidade = 'AN' and horarioturma.anovigente = matriculaperiodo.ano)
   or (curso.periodicidade = 'SE' and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre)
   or (curso.periodicidade = 'IN')
  ) 
and horarioturma.turma in (
	select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null 
		and $4 = false
		and matriculaperiodoturmadisciplina.turma is not null then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null 
		and $4 = false
		then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false 
		then matriculaperiodoturmadisciplina.turmateorica else 0 end
	union all
	select t.codigo from turma t
	inner join turmaagrupada ta on ta.turmaorigem = t.codigo
	and ta.turma in (
		select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null 
		and matriculaperiodoturmadisciplina.turma is not null 
		and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
		
	)
 )
 group by disciplina.codigo, disciplina.nome,  modalidadedisciplina  
$function$
;

---- EDSON 03/07/2019 14:40 VERSAO 7.0.1.0

CREATE OR REPLACE FUNCTION public.aulaaluno_disciplinaequivalente(historico integer, trazerdisciplinacomposta boolean, professor integer, apenasturmaorigem boolean)
 RETURNS SETOF horarioaulaaluno
 LANGUAGE sql
 COST 1 ROWS 1
AS $function$
select distinct max(pessoa.codigo) as professor_codigo, 
 array_to_string(array_agg(distinct pessoa.nome), ', ') as professor_nome, 
 array_agg(distinct pessoa.codigo) as professores_codigo,
 disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
 min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino , 'PRESENCIAL' as modalidadeDisciplina
 from horarioturma  
 inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
 inner join turma on turma.codigo = horarioturma.turma and turma.turmaagrupada
 inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
 inner join disciplinaequivalente  on disciplinaequivalente.disciplina = horarioturmadiaitem.disciplina
 inner join disciplina on disciplina.codigo = disciplinaequivalente.equivalente
 inner join historico on historico.codigo = $1 and disciplina.codigo = historico.disciplina
 inner join matricula on matricula.matricula = historico.matricula
 inner join curso on matricula.curso = curso.codigo
 inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo 
 left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina
 left join professortitulardisciplinaturma on professortitulardisciplinaturma.disciplina = horarioturmadiaitem.disciplina
	and professortitulardisciplinaturma.turma = turma.codigo 
	and ((curso.periodicidade = 'AN' and professortitulardisciplinaturma.ano = matriculaperiodo.ano)
	or (curso.periodicidade = 'SE' and professortitulardisciplinaturma.ano = matriculaperiodo.ano and professortitulardisciplinaturma.semestre = matriculaperiodo.semestre)
	or (curso.periodicidade = 'IN')
	) and professortitulardisciplinaturma.titular and ($3 is null or $3 = 0)
inner join pessoa on ((professortitulardisciplinaturma.codigo is not null and professortitulardisciplinaturma.professor = pessoa.codigo) or (professortitulardisciplinaturma.codigo is null and pessoa.codigo = horarioturmadiaitem.professor))
 where  (($3 is not null and pessoa.codigo = $3) or ($3 is null and pessoa.codigo is not null)) and
((curso.periodicidade = 'AN' and horarioturma.anovigente = matriculaperiodo.ano)
   or (curso.periodicidade = 'SE' and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre)
   or (curso.periodicidade = 'IN')
  ) 
  
and horarioturma.turma in (
	select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
	union all
	select t.codigo from turma t
	inner join turmaagrupada ta on ta.turmaorigem = t.codigo
	and ta.turma in (
		select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
		
	)
 )
 group by disciplina.codigo, disciplina.nome,  modalidadedisciplina 
$function$
;
---- EDSON 03/07/2019 14:40 VERSAO 7.0.1.0

CREATE OR REPLACE FUNCTION public.aulaaluno_disciplinanormal(historico integer, trazerdisciplinacomposta boolean, professor integer, apenasturmaorigem boolean)
 RETURNS SETOF horarioaulaaluno
 LANGUAGE sql
 COST 1 ROWS 1
AS $function$
select distinct max(pessoa.codigo) as professor_codigo, 
 array_to_string(array_agg(distinct pessoa.nome), ', ') as professor_nome, 
 array_agg(distinct pessoa.codigo) as professores_codigo,
 disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
 min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino , 'PRESENCIAL' as modalidadeDisciplina  
 from horarioturma  
 inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
 inner join turma on turma.codigo = horarioturma.turma
 inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
 inner join disciplina on disciplina.codigo = horarioturmadiaitem.disciplina
 inner join historico on historico.codigo = $1
 inner join matricula on matricula.matricula = historico.matricula
 inner join curso on matricula.curso = curso.codigo
 inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo
 left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina
 left join professortitulardisciplinaturma on professortitulardisciplinaturma.disciplina = disciplina.codigo
	and professortitulardisciplinaturma.turma = turma.codigo 
	and ((curso.periodicidade = 'AN' and professortitulardisciplinaturma.ano = matriculaperiodo.ano)
	or (curso.periodicidade = 'SE' and professortitulardisciplinaturma.ano = matriculaperiodo.ano and professortitulardisciplinaturma.semestre = matriculaperiodo.semestre)
	or (curso.periodicidade = 'IN')
	) and professortitulardisciplinaturma.titular and ($3 is null or $3 = 0)
inner join pessoa on ((professortitulardisciplinaturma.codigo is not null and professortitulardisciplinaturma.professor = pessoa.codigo) or (professortitulardisciplinaturma.codigo is null and pessoa.codigo = horarioturmadiaitem.professor))
 where disciplina.codigo = historico.disciplina 
 and (($3 is not null and pessoa.codigo = $3) or ($3 is null and pessoa.codigo is not null))
 and ((curso.periodicidade = 'AN' and horarioturma.anovigente = matriculaperiodo.ano)
   or (curso.periodicidade = 'SE' and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre)
   or (curso.periodicidade = 'IN')
  ) 
and horarioturma.turma in (
	select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
	union all
	select t.codigo from turma t
	inner join turmaagrupada ta on ta.turmaorigem = t.codigo
	and ta.turma in (
		select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
		
	)
 )
 group by disciplina.codigo, disciplina.nome,  modalidadedisciplina
$function$
;
---- EDSON 03/07/2019 14:40 VERSAO 7.0.1.0

CREATE OR REPLACE FUNCTION public.aulaaluno_gradedisciplinacomposta(historico integer, trazerdisciplinacomposta boolean, professor integer, apenasturmaorigem boolean)
 RETURNS SETOF horarioaulaaluno
 LANGUAGE sql
 COST 1 ROWS 1
AS $function$
select distinct max(pessoa.codigo) as professor_codigo, 
 array_to_string(array_agg(distinct pessoa.nome), ', ') as professor_nome, 
 array_agg(distinct pessoa.codigo) as professores_codigo,
 disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
 min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino , 'PRESENCIAL' as modalidadeDisciplina 
 from horarioturma  
 inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
 inner join turma on turma.codigo = horarioturma.turma
 inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
 inner join gradedisciplinacomposta on gradedisciplinacomposta.disciplina = horarioturmadiaitem.disciplina
 inner join gradedisciplina on gradedisciplina.codigo = gradedisciplinacomposta.gradedisciplina
 inner join disciplina on disciplina.codigo = gradedisciplina.disciplina
 inner join historico on historico.codigo = $1 and gradedisciplina.codigo = historico.gradedisciplina
 inner join matricula on matricula.matricula = historico.matricula
 inner join curso on matricula.curso = curso.codigo
 inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo
 left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo 
 and matriculaperiodoturmadisciplina.gradedisciplinacomposta = gradedisciplinacomposta.codigo
 left join professortitulardisciplinaturma on professortitulardisciplinaturma.disciplina = horarioturmadiaitem.disciplina
	and professortitulardisciplinaturma.turma = turma.codigo 
	and ((curso.periodicidade = 'AN' and professortitulardisciplinaturma.ano = matriculaperiodo.ano)
	or (curso.periodicidade = 'SE' and professortitulardisciplinaturma.ano = matriculaperiodo.ano and professortitulardisciplinaturma.semestre = matriculaperiodo.semestre)
	or (curso.periodicidade = 'IN')
	) and professortitulardisciplinaturma.titular and ($3 is null or $3 = 0)
inner join pessoa on ((professortitulardisciplinaturma.codigo is not null and professortitulardisciplinaturma.professor = pessoa.codigo) or (professortitulardisciplinaturma.codigo is null and pessoa.codigo = horarioturmadiaitem.professor)) 
 where  $2 = true and  (($3 is not null and pessoa.codigo = $3) or ($3 is null and pessoa.codigo is not null)) and
((curso.periodicidade = 'AN' and horarioturma.anovigente = matriculaperiodo.ano)
   or (curso.periodicidade = 'SE' and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre)
   or (curso.periodicidade = 'IN')
  ) 
and horarioturma.turma in (
	select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
	union all
	select t.codigo from turma t
	inner join turmaagrupada ta on ta.turmaorigem = t.codigo
	and ta.turma in (
		select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
		
	)
 )
 group by disciplina.codigo, disciplina.nome,  matriculaperiodoturmadisciplina.modalidadedisciplina  
$function$
;


---- EDSON 03/07/2019 14:40 VERSAO 7.0.1.0

CREATE OR REPLACE FUNCTION public.aulaaluno_grupooptativacomposta(historico integer, trazerdisciplinacomposta boolean, professor integer, apenasturmaorigem boolean)
 RETURNS SETOF horarioaulaaluno
 LANGUAGE sql
 COST 1 ROWS 1
AS $function$
 select distinct max(pessoa.codigo) as professor_codigo, 
 array_to_string(array_agg(distinct pessoa.nome), ', ') as professor_nome, 
 array_agg(distinct pessoa.codigo) as professores_codigo,
 disciplina.codigo as disciplina_codigo, disciplina.nome as disciplina_nome ,
 min(horarioturmadiaitem.data) as datainicio, max(horarioturmadiaitem.data) as datatermino , 'PRESENCIAL' as modalidadeDisciplina
 from horarioturma  
 inner join horarioturmadia on horarioturmadia.horarioturma = horarioturma.codigo 
 inner join turma on turma.codigo = horarioturma.turma
 inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo
 inner join gradedisciplinacomposta on gradedisciplinacomposta.disciplina = horarioturmadiaitem.disciplina
 inner join gradecurriculargrupooptativadisciplina on gradecurriculargrupooptativadisciplina.codigo = gradedisciplinacomposta.gradecurriculargrupooptativadisciplina
 inner join disciplina on disciplina.codigo = gradecurriculargrupooptativadisciplina.disciplina
 inner join historico on historico.codigo = $1 and gradecurriculargrupooptativadisciplina.codigo = historico.gradecurriculargrupooptativadisciplina
 inner join matricula on matricula.matricula = historico.matricula
 inner join curso on matricula.curso = curso.codigo
 inner join matriculaperiodo on matriculaperiodo.codigo = historico.matriculaperiodo 
 left join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matriculaperiodo = matriculaperiodo.codigo 
 and matriculaperiodoturmadisciplina.gradedisciplinacomposta = gradedisciplinacomposta.codigo
 left join professortitulardisciplinaturma on professortitulardisciplinaturma.disciplina = horarioturmadiaitem.disciplina
	and professortitulardisciplinaturma.turma = turma.codigo 
	and ((curso.periodicidade = 'AN' and professortitulardisciplinaturma.ano = matriculaperiodo.ano)
	or (curso.periodicidade = 'SE' and professortitulardisciplinaturma.ano = matriculaperiodo.ano and professortitulardisciplinaturma.semestre = matriculaperiodo.semestre)
	or (curso.periodicidade = 'IN')
	) and professortitulardisciplinaturma.titular and ($3 is null or $3 = 0)
 inner join pessoa on ((professortitulardisciplinaturma.codigo is not null and professortitulardisciplinaturma.professor = pessoa.codigo) or (professortitulardisciplinaturma.codigo is null and pessoa.codigo = horarioturmadiaitem.professor))
 where  $2 = true and  (($3 is not null and pessoa.codigo = $3) or ($3 is null and pessoa.codigo is not null)) and
((curso.periodicidade = 'AN' and horarioturma.anovigente = matriculaperiodo.ano)
   or (curso.periodicidade = 'SE' and horarioturma.anovigente = matriculaperiodo.ano and horarioturma.semestrevigente = matriculaperiodo.semestre)
   or (curso.periodicidade = 'IN')
  ) 
and horarioturma.turma in (
	select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
	union all
	select t.codigo from turma t
	inner join turmaagrupada ta on ta.turmaorigem = t.codigo
	and ta.turma in (
		select case when matriculaperiodoturmadisciplina.turmateorica is null 
		and matriculaperiodoturmadisciplina.turmapratica is null and matriculaperiodoturmadisciplina.turma is not null and $4 = false then matriculaperiodoturmadisciplina.turma  
		else case when matriculaperiodo.turma is not null then matriculaperiodo.turma else 0 end end
		union all
		select case when matriculaperiodoturmadisciplina.turmapratica is not null and $4 = false then matriculaperiodoturmadisciplina.turmapratica  else 0 end
		union all
		select case when matriculaperiodoturmadisciplina.turmateorica is not null and $4 = false then matriculaperiodoturmadisciplina.turmateorica else 0 end
		
	)
 )
 group by disciplina.codigo, disciplina.nome,  matriculaperiodoturmadisciplina.modalidadedisciplina 
$function$
;

---------- Renato Borges 21/06/2019 14:20 7.0.1.3M------------------
alter table configuracaoacademico add column quantidadeDiasProfessorPoderaRegistrarAulaAposUltimaAula integer default 0;

------------- Leandro 7.0.1 09/07/2019 16:00 -----------
alter table transferenciaentrada add constraint fk_transferenciaentrada_requerimento foreign key (codigorequerimento) 
	references requerimento (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;

alter table transferenciaentrada add constraint fk_transferenciaentrada_matricula foreign key (matricula) 
	references matricula (matricula) MATCH SIMPLE ON UPDATE cascade ON DELETE cascade;
-- em caso de ocorrência de registros que impossibilitem a criação destas chaves estrangeiras, contatar Leandro.

------------- Rodrigo Wind 7.0.1.0 05/07/2019 15:34 --------
alter table horarioprofessordia alter column horariodisciplinaturma drop not null;
alter table horarioturmadia alter column horariodisciplinaprofessor drop not null;

------------- Rodrigo Wind 7.0.1.0 09/07/2019 11:34 --------
ALTER TABLE public.horarioprofessordiaitem drop CONSTRAINT check_horarioprofessordiaitem_usuarioliberacaochoquehorario;
drop FUNCTION public.fn_validarexistenciahorarioprofessordiaitem(integer, integer, integer, integer, integer, integer);

CREATE OR REPLACE FUNCTION public.fn_validarexistenciahorarioprofessordiaitem(integer, integer, integer, integer, integer, integer, integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE 
 quantidadeRegistro integer;
begin
	 if ($6 is null) then
	     select into quantidadeRegistro count(codigo) from horarioprofessordiaitem  where nraula = $2 and horarioprofessordia = $3 and horarioturmadiaitem = $7 and codigo != $1 and usuarioliberacaochoquehorario is null;
	 else
	     select into quantidadeRegistro count (codigo) from horarioprofessordiaitem  where nraula = $2 and horarioprofessordia = $3 and horarioturmadiaitem = $7 and disciplina = $4 and turma = $5  and codigo != $1;	     
	 end if;

	  IF quantidadeRegistro > 0 THEN	        
		RETURN   false;
	  ELSE
		RETURN   true;
	  END IF;   
end 
$function$
;

------------- Rodrigo 7.0.1 10/07/2019 08:38 -----------
create index idx_professortitulardisciplinaturma_tur_dis_ano_sem on professortitulardisciplinaturma(turma, disciplina, ano, semestre);

------------- Marcos Paulo 7.0.1 10/07/2019 10:58 -----------
update historico set mapaequivalenciadisciplina = t.mapaequivalenciadisciplina from (
select historico.codigo, historico.disciplina, historicoequivalente, mapaequivalenciadisciplinacursada.mapaequivalenciadisciplina, mapaequivalenciadisciplinacursada, mapaequivalenciadisciplinamatrizcurricular 
from historico 
inner join mapaequivalenciadisciplinacursada on mapaequivalenciadisciplinacursada.codigo = historico.mapaequivalenciadisciplinacursada
where historico.mapaequivalenciadisciplina is null
) as t where t.codigo = historico.codigo;

------------- Marcos Paulo 7.0.1 10/07/2019 10:58 -----------
update catalogo set nivelBibliografico = 'MO' where nivelBibliografico is null or nivelBibliografico = '';

------------- Pedro Andrade 7.0.1 11/07/2019 16:48 -----------
Create Or Replace Function fn_proximodiautil (p_data date) Returns date As
$Body$
declare
  diautil date;
  diautillocalizado boolean = false;
Begin
      IF (p_data IS NULL) THEN
         raise exception 'Data não informada corretamente ! ';
	 END IF;	 
      diautil := p_data;
     
      while  diautillocalizado = false loop
         if (EXTRACT(ISODOW FROM diautil) <> 6 and EXTRACT(ISODOW FROM diautil) <> 7 and not exists (select 1 from feriado  where feriado.data::date = diautil and feriado.nacional) ) then 
           diautillocalizado := true;
         else 
          diautil := (diautil + INTERVAL '1 DAYS')::date;
         end if;
      end loop; 
      
     Return diautil;
End;
$Body$
Language 'plpgsql';

------------- Marcos Paulo 16/07/2019 08:43 -----------
alter table prospects alter column orgaoemissor type varchar(50);

------------- Renato Borges 16/07/2019 10:15 -----------
alter table configuracaofinanceirocartaorecebimento add column tipoorigemcontarecebermatricula boolean  default true;
alter table configuracaofinanceirocartaorecebimento add column tipoorigemcontarecebermensalidade boolean default true;
alter table configuracaofinanceirocartaorecebimento add column tipoorigemcontarecebermaterialdidatico boolean default true;
alter table configuracaofinanceirocartaorecebimento add column tipoorigemcontareceberinscricaoprocessoseletivo boolean default true;
alter table configuracaofinanceirocartaorecebimento add column tipoorigemcontareceberrequerimento boolean default true;
alter table configuracaofinanceirocartaorecebimento add column tipoorigemcontareceberbiblioteca boolean default true;
alter table configuracaofinanceirocartaorecebimento add column tipoorigemcontarecebernegociacao boolean default true;
alter table configuracaofinanceirocartaorecebimento add column tipoorigemcontareceberoutros boolean default true;
alter table configuracaofinanceirocartaorecebimento add column tipoorigemcontareceberdevolucaocheque boolean default true;
alter table configuracaofinanceirocartaorecebimento add column tipoorigemcontareceberbolsacusteada boolean default true;
alter table configuracaofinanceirocartaorecebimento add column tipoorigemcontarecebercontratoreceita boolean default true;
alter table configuracaofinanceirocartaorecebimento add column tipoorigemcontareceberinclusaoreposicao boolean default true;

------------- Marcos Paulo 17/07/2019 09:21 -----------
alter table logfuncionario alter column cpf type varchar(18);

------------------ Rodrigo Wind 18/07/2019 14:57 7.1.2.0 ----------------------------
alter table contacorrente add column permiteGerarRemessaOnlineBoletoVencido boolean default false;
	alter table contacorrente add column gerarRemessaBoletoVencidoMatricula boolean default false;
	alter table contacorrente add column gerarRemessaBoletoVencidoParcelas boolean default false;
	alter table contacorrente add column gerarRemessaBoletoVencidoNegociacao boolean default false;
	alter table contacorrente add column gerarRemessaBoletoVencidoOutros boolean default false;	
	alter table contacorrente add column gerarRemessaBoletoVencidoBiblioteca boolean default false;	
	alter table contacorrente add column gerarRemessaBoletoVencidoDevCheque boolean default false;
	alter table contacorrente add column gerarRemessaBoletoVencidoConvenio boolean default false;
	alter table contacorrente add column gerarRemessaBoletoVencidoContratoReceita boolean default false;
	alter table contacorrente add column gerarRemessaBoletoVencidoMateriaDidatico boolean default false;
	alter table contacorrente add column gerarRemessaBoletoVencidoInclusaoReposicao boolean default false;
	alter table contacorrente add column  qtdeDiasVencidoPermitirRemessaOnlineMatricula  int default 0;
	alter table contacorrente add column  qtdeDiasVencidoPermitirRemessaOnlineParcela  int default 0;
	alter table contacorrente add column  qtdeDiasVencidoPermitirRemessaOnlineNegociacao  int default 0;
	alter table contacorrente add column  qtdeDiasVencidoPermitirRemessaOnlineOutros  int default 0;
	alter table contacorrente add column  qtdeDiasVencidoPermitirRemessaOnlineBiblioteca  int default 0;
	alter table contacorrente add column  qtdeDiasVencidoPermitirRemessaOnlineDevCheque  int default 0;
	alter table contacorrente add column  qtdeDiasVencidoPermitirRemessaConvenio  int default 0;
	alter table contacorrente add column  qtdeDiasVencidoPermitirRemessaContratoReceita  int default 0;
	alter table contacorrente add column  qtdeDiasVencidoPermitirRemessaMaterialDidatico  int default 0;
	alter table contacorrente add column  qtdeDiasVencidoPermitirRemessaInclusaoExclusao   int default 0;


---- add by ale

alter table condicaopagamentoplanofinanceirocurso add column valorPrimeiraParcela numeric(20,2);
alter table condicaopagamentoplanofinanceirocurso add column usaValorPrimeiraParcela boolean default false;
alter table condicaopagamentoplanofinanceirocurso add column vencimentoPrimeiraParcelaAntesMaterialDidatico boolean default true;
alter table matriculaperiodovencimento add column usaValorPrimeiraParcela boolean default false;

alter table configuracaofinanceiro add column tipoOrigemMatriculaRotinaInadimplencia boolean default true;
alter table configuracaofinanceiro add column tipoOrigemBibliotecaRotinaInadimplencia boolean default true;
alter table configuracaofinanceiro add column tipoOrigemMensalidadeRotinaInadimplencia boolean default true;
alter table configuracaofinanceiro add column tipoOrigemNegociacaoRotinaInadimplencia boolean default true;
alter table configuracaofinanceiro add column tipoOrigemContratoReceitaRotinaInadimplencia boolean default true;
alter table configuracaofinanceiro add column tipoOrigemOutrosRotinaInadimplencia boolean default true;
alter table configuracaofinanceiro add column tipoOrigemMaterialDidaticoRotinaInadimplencia boolean default true;
alter table configuracaofinanceiro add column tipoOrigemInclusaoReposicaoRotinaInadimplencia boolean default true;
alter table configuracaofinanceiro add column tipoOrigemDevolucaoChequeRotinaInadimplencia boolean default true;

alter table configuracaogeralsistema add column definirPerfilAcessoAlunoNaoAssinouContratoMatricula boolean default false;

ALTER TABLE configuracaogeralsistema ADD COLUMN perfilAcessoAlunoNaoAssinouContratoMatricula integer;
ALTER TABLE configuracaogeralsistema ADD CONSTRAINT fk_perfilAcessoAlunoNaoAssinouContratoMatricula_perfilacesso FOREIGN KEY (perfilAcessoAlunoNaoAssinouContratoMatricula)
    REFERENCES perfilacesso (codigo) ON UPDATE NO ACTION ON DELETE NO ACTION;
    
alter table configuracaogeralsistema add column apresentarMensagemAlertaAlunoNaoAssinouContrato boolean default false;

alter table configuracaogeralsistema add column mensagemAlertaAlunoNaoAssinouContratoMatricula text;

CREATE OR REPLACE FUNCTION public.validaalunoassinoucontratomatricula(codmatriculaperiodo integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
begin 
    IF exists(
    select documetacaomatricula.codigo from documetacaomatricula
	inner join tipodocumento on 
	documetacaomatricula.tipodedocumento=tipodocumento.codigo
	inner join matricula on documetacaomatricula.matricula=matricula.matricula
	inner join matriculaperiodo on matriculaperiodo.matricula=matricula.matricula
	where tipodocumento.contrato is true and
	documetacaomatricula.entregue is false
	and matriculaperiodo.codigo = codMatriculaPeriodo
    )  THEN
       RETURN   TRUE;
    ELSE 
       RETURN false;  
    END IF; 
end;
$function$
;

update catalogo set nivelBibliografico = 'MO' where nivelBibliografico is null or nivelBibliografico = '';

ALTER TABLE configuracaogeralsistema ADD COLUMN percentualBaixaFrequencia integer default 0;

alter table configuracaogeralsistema add column tempoBloqTentativasFalhaLogin integer;
alter table configuracaogeralsistema add column qtdTentativasFalhaLogin integer;

alter table usuario add column datafalhalogin timestamp;
alter table usuario add column qtdFalhaLogin integer default 0; 
alter table usuario add column usuarioBloqPorFalhaLogin boolean default false;

alter table contaReceberNaoLocalizadaArquivoRetorno add column contareceber integer;

alter table controlelivrofolharecibo  add column  responsavelalteracao int;

ALTER TABLE public.fornecedor ADD COLUMN isTemMei Boolean ;
ALTER TABLE public.fornecedor ADD COLUMN nomePessoaFisica varchar(150);
ALTER TABLE public.fornecedor ADD COLUMN cpfFornecedorMei varchar(150);

alter table fornecedor  add column banco integer;
alter table fornecedor  add constraint fk_fornecedor_banco foreign key (banco) references banco(codigo);

alter table fornecedor add column observacao text;

create table pendenciaLiberacaoMatricula (
	codigo serial not null,
	matricula character varying(20) not null,
	dataSolicitacao timestamp without time zone not null,
	usuarioSolicitacao int not null,
	motivoSolicitacao character varying(50) NOT NULL,
	situacao character varying(20) NOT NULL,
	motivoIndeferimento text, 
	dataIndeferimento timestamp without time zone,
	usuarioIndeferimento int,
	dataDeferimento timestamp without time zone,
	usuarioDeferimento int,
	constraint pendenciaLiberacaoMatricula_pkey primary key (codigo),
	constraint fk_pendenciaLiberacaoMatricula_matricula foreign key (matricula) references matricula (matricula) match simple on update cascade on delete cascade,
	constraint fk_pendenciaLiberacaoMatricula_usuarioSolicitacao foreign key (usuarioSolicitacao) references usuario (codigo) match simple on update cascade on delete cascade,
	constraint fk_pendenciaLiberacaoMatricula_usuarioIndeferimento foreign key (usuarioIndeferimento) references usuario (codigo) match simple on update cascade on delete cascade,
	constraint fk_pendenciaLiberacaoMatricula_usuarioDeferimento foreign key (usuarioDeferimento) references usuario (codigo) match simple on update cascade on delete cascade
) with (
  oids=false
);

alter table matricula add column bloqueioPorSolicitacaoLiberacaoMatricula boolean default false;

ALTER TABLE tiporequerimento ADD COLUMN orientacaoatendente text;

update permissao set  nomeEntidade = 'Requerimento' where nomeentidade = 'RequerimentoAluno' and not exists (
	select p.codperfilacesso from permissao p where p.codperfilacesso = permissao.codperfilacesso and nomeentidade = 'Requerimento'
	);

delete from permissao where nomeentidade = 'RequerimentoAluno' and exists (
	select p.codperfilacesso from permissao p where p.codperfilacesso = permissao.codperfilacesso and nomeentidade = 'Requerimento'
	);

ALTER TABLE operadoracartao ADD COLUMN tipofinanciamento character varying(15);
UPDATE operadoracartao SET tipofinanciamento = 'AMBAS' where tipofinanciamento is NULL;


create table RegraEmissao(
	codigo SERIAL not null,
	nivelEducacional varchar(10) not null,
	validarMatrizCurricularIntegralizado BOOLEAN default false,
	validarNotaTCC BOOLEAN default false,
	validarDocumentosEntregues BOOLEAN default false,
	tipoContrato varchar(10),
	notaTCC NUMERIC,
	CONSTRAINT pk_regraEmissao PRIMARY KEY (codigo)
);

create table RegraEmissaoUnidadeEnsino(
	codigo serial not null,
	regraEmissao integer not null,
	unidadeEnsino integer not null,
	CONSTRAINT pk_regraEmissaoUnidadeEnsino PRIMARY KEY (codigo)
);

ALTER TABLE RegraEmissaoUnidadeEnsino
  ADD CONSTRAINT fk_RegraEmissaoUnidadeEnsino_RegraEmissao FOREIGN KEY (regraEmissao)
      REFERENCES regraEmissao (codigo) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT;


ALTER TABLE RegraEmissaoUnidadeEnsino
  ADD CONSTRAINT fk_RegraEmissaoUnidadeEnsino_UnidadeEnsino FOREIGN KEY (unidadeEnsino)
      REFERENCES unidadeEnsino (codigo) MATCH SIMPLE
      ON UPDATE RESTRICT ON DELETE RESTRICT;

ALTER TABLE requerimento ADD COLUMN orientacaoAtendente text;

 alter table materialrequerimento add column disponibilizarParaRequerente boolean default false;

alter table materialrequerimento  add column usuarioDisponibilizouArquivo int;
alter table materialrequerimento  add constraint fk_materialrequerimento_usuario foreign key (usuarioDisponibilizouArquivo) references usuario(codigo);

alter table materialrequerimento add column dataDisponibilizacaoArquivo timestamp without time zone;

alter table materialrequerimento  add column requerimentoHistorico int;
alter table materialrequerimento  add constraint fk_materialrequerimento_requerimentohistorico foreign key (requerimentoHistorico) references requerimentohistorico(codigo);

create table interacaorequerimentohistorico (
	codigo serial not null,
	interacao text not null,
	usuarioInteracao int not null,
	dataInteracao timestamp without time zone not null,
	requerimentohistorico int not null,
	excluido boolean DEFAULT false,
	interacaoRequerimentoHistoricopai integer,
	nivelapresentacao character varying(100),
	constraint interacaorequerimentohistorico_pkey primary key (codigo),
	constraint fk_interacaorequerimentohistorico_usuario foreign key (usuarioInteracao) references usuario (codigo) match simple on update cascade on delete cascade,
	constraint fk_interacaorequerimentohistorico_requerimentohistorico foreign key (requerimentohistorico) references requerimentohistorico (codigo) match simple on update cascade on delete cascade
) with (
  oids=false
);	

alter table tiporequerimentodepartamento add column podeInserirNota boolean default false;
alter table tiporequerimentodepartamento add column notaMaxima NUMERIC;

alter table tiporequerimento add column permitirAlterarDataPrevisaoConclusaoRequerimento boolean default false;
alter table tiporequerimento add column abrirOutroRequerimentoAoDeferirEsteTipoRequerimento boolean default false;
alter table tiporequerimento add column qtdDiasCobrarTaxa integer;
alter table tiporequerimento add column tipoRequerimentoAbrirDeferimento integer; 
	ALTER TABLE tiporequerimento ADD CONSTRAINT tipoRequerimentoAbrirDeferimento_tkey FOREIGN KEY (tipoRequerimentoAbrirDeferimento)
	REFERENCES tiporequerimento (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
	
alter table requerimentoHistorico add column notaTCC NUMERIC;

alter table requerimento add column tipoTrabalhoConclusaoCurso varchar (250);
alter table requerimento add column tituloMonografia varchar (250);
alter table requerimento add column orientadorMonografia varchar (250);
alter table requerimento add column notamonografia NUMERIC;
alter table requerimento add column codigoNovoRequerimento integer;
	ALTER TABLE requerimento ADD CONSTRAINT codigoNovoRequerimento_key FOREIGN KEY (codigoNovoRequerimento)
	REFERENCES requerimento (codigo) MATCH SIMPLE ON UPDATE RESTRICT ON DELETE RESTRICT;
	
alter table interacaoRequerimentoHistorico add column interacaojalida boolean default true; 

ALTER TABLE public.requisicaoitem ADD COLUMN tipoautorizacaorequisicao character varying(50) COLLATE pg_catalog."default" DEFAULT 'NENHUM'::character varying;

update requisicaoitem set tipoautorizacaorequisicao = t.tipoautorizacaorequisicao from (
select  requisicao.tipoautorizacaorequisicao, requisicao.codigo from requisicao 
) as t  where  t.codigo = requisicaoitem.requisicao  and requisicaoitem.tipoautorizacaorequisicao is null;

update requisicaoitem set tipoautorizacaorequisicao = 'NENHUM' where tipoautorizacaorequisicao is null;

ALTER TABLE tiporequerimento ADD COLUMN certificadoImpresso integer;

ALTER TABLE tipoRequerimento ADD COLUMN niveleducacional character varying(2);

alter table tiporequerimento add column apenasParaAlunosComTodasAulasRegistradas boolean default false;

alter table tiporequerimento add column verificarAlunoPossuiPeloMenosUmaDisciplinaAprovada boolean default false;

alter table tiporequerimento add column registrarFormaturaAoRealizarImpressaoCerticadoDigital boolean default false;

create table pendenciaTipoDocumentoTipoRequerimento (
	codigo serial not null,
	tipodocumento int,
	tiporequerimento int,
	constraint pendenciaTipoDocumentoTipoRequerimento_pkey primary key (codigo),
	constraint fk_pendenciaTipoDocumentoTipoRequerimento_tipodocumento foreign key (tipodocumento) references tipodocumento (codigo) match simple on update cascade on delete cascade,
	constraint fk_pendenciaTipoDocumentoTipoRequerimento_tiporequerimento foreign key (tiporequerimento) references tiporequerimento (codigo) match simple on update cascade on delete cascade
) with (
  oids=false
);


ALTER TABLE requerimento ADD COLUMN motivoNaoAceiteCertificado TEXT;

ALTER TABLE requerimento ADD COLUMN formatoCertificadoSelecionado character varying(10);

alter table turma add column considerarturmaavaliacaoinstitucional boolean default true;

alter table UnidadeEnsinoContaCorrente add column utilizarNegociacao boolean default true;

alter table gradecurricular add column quantidadeDisciplinasOptativasMatrizCurricular integer default 0;

