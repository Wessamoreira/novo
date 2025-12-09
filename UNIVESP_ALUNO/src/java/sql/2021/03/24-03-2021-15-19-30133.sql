 CREATE OR REPLACE FUNCTION is_so_numero(v_texto_recebido TEXT) RETURNS boolean
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
 
CREATE OR REPLACE FUNCTION  formatartelefone(telefone character varying, size1 integer, pattner character varying, size2 integer, pattner2 character varying)
 RETURNS character varying
 LANGUAGE plpgsql
AS $function$
declare
  fone character varying;
begin 	
 
	fone := trim(replace(replace(replace(replace(replace(trim(sem_acentos(sem_caracteres_especiais(replace($1,'_','')))), '(', ''), ')', ''), '+55', ''), '-', ''), ' ', ''));
	
   IF(is_so_numero(fone) = false) THEN
	  RETURN '';
	END IF;
	IF(length(fone) > $4 ) THEN
	  fone:= substring(fone, length(fone) - ($4 -1), length(fone));
	END IF;
	IF(length(fone) = $2 ) THEN
	   RETURN trim(to_char(fone::numeric(20,0), $3));
	END IF;
	IF(length(fone) = $4 ) THEN
	   RETURN trim(to_char(fone::numeric(20,0), $5));
	END IF;	
	RETURN '';

  /*Se houver uma EXCEPTION acima será retornado vazio.REGRA SOLICITADA PELO RODRIGO DIA 24/03/2021*/
  EXCEPTION
    WHEN OTHERS THEN
        RETURN '';
END;
$function$;