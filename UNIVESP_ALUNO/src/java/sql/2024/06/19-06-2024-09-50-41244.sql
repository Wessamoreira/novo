 CREATE OR REPLACE FUNCTION public.sem_acentos(character varying)
 RETURNS character varying
 LANGUAGE sql
AS $function$
SELECT replace(unaccent(trim($1)), '''', '')
$function$
;