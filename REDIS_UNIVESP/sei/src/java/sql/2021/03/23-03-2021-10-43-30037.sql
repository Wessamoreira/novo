CREATE OR REPLACE FUNCTION fn_isnumeric_so_numero (v_texto_recebido TEXT) RETURNS boolean
IMMUTABLE
language plpgsql
AS $$
BEGIN
	/*Essa função é usada para validar se o texto passado contém só numero,ele não considera número negativo/positivo OU número decimal .Exemplo -5 ou +2 */
  IF ( (COALESCE(RTRIM(v_texto_recebido),'') = '') OR (v_texto_recebido ILIKE '%.%') ) THEN
    RETURN FALSE;
  ELSE
   RETURN (SELECT v_texto_recebido ~'^([0-9]+\.?[0-9]*|\.[0-9]+)$');
  END IF;
END;
$$;


CREATE OR REPLACE FUNCTION formatartelefone(telefone character varying, size1 integer, pattner character varying, size2 integer, pattner2 character varying)
 RETURNS character varying
 LANGUAGE plpgsql
AS $function$
declare
  fone character varying;
begin 	
 
	fone :=  trim(replace(replace(replace(replace(replace(trim(sem_acentos(sem_caracteres_especiais(replace($1,'_','')))), '(', ''), ')', ''), '+55', ''), '-', ''), ' ', ''));
	
   if(isnumeric(fone) = false) then
	  return '';
	end if;
	if(length(fone) > $4 ) then
	  fone:= substring(fone, length(fone) - ($4 -1), length(fone));
	end if;
	if(length(fone) = $2 ) then
	   return trim(to_char(fone::numeric(20,0), $3));
	end if;
	if(length(fone) = $4 ) then
	   return trim(to_char(fone::numeric(20,0), $5));
	end if;	
	return '';
end;
$function$;

 CREATE OR REPLACE FUNCTION fn_remover_mascara_telefone (p_telefone text) RETURNS text
 LANGUAGE plpgsql
AS $function$
declare 
 v_retorno text := '';
BEGIN 	
   v_retorno :=  REPLACE(REPLACE(replace(trim(COALESCE(p_telefone,'')),'-',''),')',''),'(','') ;
 RETURN v_retorno ; 
end;
$function$; 