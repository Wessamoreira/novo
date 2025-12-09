CREATE OR REPLACE FUNCTION public.fn_matricula_validaralunocursogradecurricular() RETURNS TRIGGER LANGUAGE plpgsql
AS $function$ 
DECLARE
	v_retorno INTEGER := 0;
BEGIN
	SELECT INTO v_retorno COUNT(DISTINCT matricula) FROM (
	SELECT
		matricula.matricula
	FROM
		matricula
	INNER JOIN curso ON
		curso.codigo = matricula.curso
	WHERE
		matricula.aluno = NEW.aluno
		AND curso.codigo = NEW.curso
		AND gradecurricularatual = NEW.gradecurricularatual
		AND matricula.matricula != NEW.matricula
		AND curso.niveleducacional = 'PO'
		AND matricula.situacao IN ('AT', 'AC')
		AND COALESCE(matricula.tipomatricula, 'NO') != 'EX'
		AND COALESCE(NEW.tipomatricula, 'NO') = 'EX'
	UNION ALL 
	SELECT
		matricula.matricula
	FROM
		matricula
	INNER JOIN curso ON
		curso.codigo = matricula.curso
	WHERE
		matricula.aluno = NEW.aluno
		AND curso.codigo = NEW.curso
		AND matricula.matricula != NEW.matricula
		AND COALESCE(matricula.tipomatricula, 'NO') = 'NO'
		AND COALESCE(NEW.tipomatricula, 'NO') = 'NO'
		AND matricula.situacao IN ('AT', 'AC')) AS t;
    IF (v_retorno > 0 ) THEN 
        RAISE EXCEPTION 'ERRO';
    END IF;
   RETURN NEW;
END;     
$function$;

CREATE TRIGGER tg_matricula_alunocursogradecurricular BEFORE INSERT ON matricula FOR EACH ROW EXECUTE FUNCTION fn_matricula_validaralunocursogradecurricular();