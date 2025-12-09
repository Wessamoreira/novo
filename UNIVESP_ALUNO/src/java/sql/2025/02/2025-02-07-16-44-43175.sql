CREATE OR REPLACE FUNCTION public.fn_validarmatriculaperiododuplicada()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$
DECLARE v_retorno boolean := FALSE;
BEGIN
IF (new.situacaomatriculaperiodo IN ('PR', 'AT') 
	AND NOT EXISTS (
		SELECT matriculaperiodo.codigo 
		FROM matriculaperiodo 
		WHERE matriculaperiodo.matricula = NEW.matricula 
		AND matriculaperiodo.codigo != NEW.codigo 
		AND matriculaperiodo.ano = NEW.ano 
		AND matriculaperiodo.semestre = NEW.semestre
		AND matriculaperiodo.situacaomatriculaperiodo NOT IN ('PC', 'TR', 'AC', 'CA'))
) THEN
	v_retorno := TRUE;
END IF;
IF (new.situacaomatriculaperiodo NOT IN ('PR', 'AT') OR (COALESCE(NEW.ano, '') = '' AND COALESCE(NEW.semestre, '') = '')
) THEN 
	v_retorno := TRUE;
END IF;
IF (v_retorno IS FALSE AND COALESCE(NEW.ano, '') != '' AND COALESCE(NEW.semestre, '') != '') THEN 
	RAISE EXCEPTION 'INICIO_MSG_ERRO Já existe cadastrado o seguinte período (%) para a matrícula (%)FIM_MSG_ERRO', NEW.ano || '/' || NEW.semestre, NEW.matricula;
END IF;
IF (v_retorno IS FALSE AND COALESCE(NEW.ano, '') != '' AND COALESCE(NEW.semestre, '') = '') THEN
	RAISE EXCEPTION 'INICIO_MSG_ERRO Já existe cadastrado o seguinte período (%) para a matrícula (%)FIM_MSG_ERRO', NEW.ano, NEW.matricula;
END IF;
RETURN NEW; END; $function$
;

CREATE TRIGGER tg_validar_matriculaperiododuplicada BEFORE
INSERT OR UPDATE ON public.matriculaperiodo FOR EACH ROW EXECUTE FUNCTION fn_validarmatriculaperiododuplicada();