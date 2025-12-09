CREATE OR REPLACE FUNCTION public.fn_pessoa_validarUnicidadeCPF(codigo_pessoa integer, cpf_pessoa character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
DECLARE 
   is_cpf_unico boolean;
BEGIN
	SELECT INTO is_cpf_unico CASE WHEN pe.codigo IS NULL THEN TRUE ELSE FALSE END AS existe_registro FROM pessoa pe 
	WHERE pe.codigo != COALESCE(codigo_pessoa, 0)
	AND pe.cpf IS NOT NULL
	AND cpf_pessoa IS NOT NULL
	AND trim(REPLACE(REPLACE(REPLACE(pe.cpf, '.', ''), '-', ''), ' ', '')) = trim(REPLACE(REPLACE(REPLACE(cpf_pessoa, '.', ''), '-', ''), ' ', ''));
    RETURN is_cpf_unico;
END;
$function$;

SELECT create_constraint('ALTER TABLE pessoa ADD CONSTRAINT pessoa_check_validarUnicidadeCPF CHECK (fn_pessoa_validarUnicidadeCPF(codigo, cpf)) NOT VALID;');