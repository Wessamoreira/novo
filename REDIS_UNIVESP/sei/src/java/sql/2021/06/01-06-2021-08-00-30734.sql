create table ImportarCandidatoInscricaoProcessoSeletivo
(
	codigo serial not null,
	arquivo integer not null,
	created timestamp not null,
    codigoCreated Integer not null,
    nomeCreated varchar(255) not null,
    primary key(codigo),
    CONSTRAINT fk_ImportarCandidatoInscricaoProcessoSeletivo_arquivo FOREIGN KEY (arquivo) REFERENCES arquivo (codigo) match simple on update cascade on delete cascade
);

CREATE OR REPLACE FUNCTION public.fn_isnumeric_so_numero(v_texto_recebido text)
 RETURNS boolean
 LANGUAGE plpgsql
 IMMUTABLE
AS $function$
BEGIN
	/*Essa função é usada para validar se o texto passado contém só numero,ele não considera número negativo/positivo OU número decimal .Exemplo -5 ou +2 */
  IF ( (COALESCE(RTRIM(v_texto_recebido),'') = '') OR (v_texto_recebido ILIKE '%.%') ) THEN
    RETURN FALSE;
  ELSE
   RETURN (SELECT v_texto_recebido ~'^([0-9]+\.?[0-9]*|\.[0-9]+)$');
  END IF;
END;
$function$;

update matricula set classificacaoingresso = null where fn_isnumeric_so_numero(trim(classificacaoingresso)) is false and classificacaoingresso is not null;
alter table matricula alter column classificacaoingresso type INT using trim(classificacaoingresso)::integer;

update inscricao set classificacao = null where fn_isnumeric_so_numero(trim(classificacao)) is false and classificacao is not null;
alter table inscricao alter column classificacao type INT using trim(classificacao)::integer;

alter table inscricao add column sobreBolsasEAuxilios Boolean default (false);
alter table inscricao add column autodeclaracaoPretoPardoOuIndigena Boolean default (false);
alter table inscricao add column inscricaoProvenienteImportacao Boolean default (false);


alter table arquivo alter column extensao type varchar(8);
alter table arquivo alter column origem type varchar(50);

