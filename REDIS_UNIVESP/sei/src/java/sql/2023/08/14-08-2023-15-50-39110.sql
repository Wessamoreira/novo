CREATE OR REPLACE FUNCTION public.somaano(ano character varying, qtdeano bigint)
 RETURNS character varying
 LANGUAGE plpgsql
AS $function$
declare 
database DATE;
begin
	database := cast(ano||'-01'||'-01' as date);
	database := database+(qtdeano||' year')::interval;	
	return extract(year from database);
END
$function$;