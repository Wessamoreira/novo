ALTER TABLE IF EXISTS programacaoformaturaaluno DROP CONSTRAINT IF EXISTS unq_programacaoformaturaaluno_matricula;

CREATE OR REPLACE FUNCTION public.fn_programacaoFormaturaAluno_validarUnicidadeMatricula(codigo_programacao_formatura int, matricula_matricula character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE 
   is_programacao_formatura_existe boolean;
BEGIN
	SELECT
	INTO
	is_programacao_formatura_existe
	CASE
		WHEN programacaoformaturaaluno.codigo IS NOT NULL THEN TRUE ELSE FALSE
	END existe_registro
FROM
	programacaoformaturaaluno
WHERE
	programacaoformaturaaluno.matricula = matricula_matricula
	AND programacaoformaturaaluno.codigo <> coalesce(codigo_programacao_formatura, 0)
	AND programacaoformaturaaluno.colougrau IN ('NI','SI')
LIMIT 1;
RETURN NOT is_programacao_formatura_existe;
END;
$function$;

SELECT create_constraint('ALTER TABLE programacaoFormaturaAluno ADD CONSTRAINT fn_programacaoFormaturaAluno_validarUnicidadeMatricula CHECK (fn_programacaoFormaturaAluno_validarUnicidadeMatricula(codigo, matricula)) NOT VALID;');