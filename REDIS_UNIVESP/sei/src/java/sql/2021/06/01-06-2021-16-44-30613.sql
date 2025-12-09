CREATE OR REPLACE FUNCTION public.fn_validarunidadeensinocontareceber(p_unidadeensino integer, p_codorigem varchar, p_tipoorigem varchar)
 RETURNS BOOLEAN
LANGUAGE plpgsql
AS $function$
DECLARE
 v_unidadeensinonegociacaocontareceber INTEGER := 0;
begin
	IF (p_tipoorigem <> 'NCR') THEN 
		RETURN TRUE;
	END IF;
	select into v_unidadeensinonegociacaocontareceber ncr.unidadeensino from negociacaocontareceber ncr where ncr.codigo::varchar = p_codorigem LIMIT 1;
    IF (p_unidadeensino != v_unidadeensinonegociacaocontareceber) THEN 
        RETURN FALSE;
    END IF;   
    RETURN TRUE; 
END;     
$function$;
SELECT create_constraint('ALTER TABLE public.contareceber ADD CONSTRAINT check_validar_unidadeensino_conta_receber CHECK (fn_validarunidadeensinocontareceber(unidadeensino, codorigem, tipoorigem)) NOT VALID;');