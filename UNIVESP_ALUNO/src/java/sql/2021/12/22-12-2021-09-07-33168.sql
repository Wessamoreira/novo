CREATE OR REPLACE FUNCTION public.fn_validaprospectvinculadopessoacorreta(pessoa integer, cpf character varying, emailprincipal character varying, emailsecundario character varying)
 RETURNS boolean
 LANGUAGE plpgsql
AS $function$
declare
begin 
  	if (($1::int is null) or ($1::int = 0::int)) or exists (select pessoa.codigo
	  from pessoa  where codigo = $1 
	  and (($2 is not null and replace(replace(replace(trim($2), '.', ''), '-', ''), ' ', '')  != '' and replace(replace(replace(trim($2), '.', ''), '-', ''), ' ', '')  = replace(replace(replace(trim(pessoa.cpf), '.', ''), '-', ''), ' ', '') )
	  or (($2 is null or replace(replace(replace(trim($2), '.', ''), '-', ''), ' ', '') = '') and $3 is not null and trim($3) != '' and pessoa.email != '' and lower(pessoa.email) in (select regexp_split_to_table(replace((lower($3)), ' ', ''), ';')))
	  or (($2 is null or replace(replace(replace(trim($2), '.', ''), '-', ''), ' ', '') = '') and $4 is not null and trim($4) != '' and pessoa.email != '' and lower(pessoa.email) in (select regexp_split_to_table(replace((lower($4)), ' ', ''), ';')))
	  )) THEN
    	RETURN true;
    else 
	    RETURN false;    
	end if;
END;     
$function$
;