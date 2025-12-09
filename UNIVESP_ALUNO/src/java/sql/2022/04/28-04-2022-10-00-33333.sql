alter table tiporequerimento add column registrarTrancamentoProximoSemestre boolean default false;

CREATE OR REPLACE FUNCTION public.fn_validaruematriculaigualueprocessomatricula(codigo_processomatricula integer, codigo_unidadeensinocurso integer)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE 
       quantidadeRegistro integer;
begin
	if(codigo_processomatricula is null or codigo_processomatricula = 0 ) then
	return true;
	end if;
 --Efetua a consulta para verificar se a unidade de ensino presente no processoMatricula é igual a unidade de ensino da matrícula.
SELECT INTO quantidadeRegistro count(processomatricula.codigo) AS quantidadeRegistro
FROM unidadeensinocurso  
INNER JOIN processomatricula ON processomatricula.codigo = codigo_processoMatricula
inner join processomatriculaunidadeensino on processomatriculaunidadeensino.processomatricula = processomatricula.codigo
and unidadeensinocurso.unidadeensino = processomatriculaunidadeensino.unidadeensino
WHERE unidadeensinocurso.codigo         = codigo_unidadeensinocurso ;
--Verifica se há registro retornado da consulta.
      IF quantidadeRegistro > 0 THEN
            RETURN   TRUE;
      ELSE
            RETURN   FALSE;
      END IF;                                                                    
END;
$function$
;
