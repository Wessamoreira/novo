
CREATE TABLE public.transmissaonossonumero (
	numero int4 NULL,
	ano int4 NULL
);  

INSERT INTO transmissaonossonumero (numero, ano) values (0, extract(year from now())); 
   
 ALTER TABLE IF EXISTS contapagarcontroleremessacontapagar ADD COLUMN IF NOT EXISTS transmissaonossonumero varchar(50);


CREATE OR REPLACE FUNCTION public.inserircodigotransmissaonossonumero(qtdpermitida integer)
 RETURNS TABLE(transmissaonossonumero_codigo integer, anoremessa integer)
 LANGUAGE plpgsql
AS $function$
DECLARE
 resultadoBusca RECORD;
 transmissaonossonumero_codigo integer;
 anoatual integer;
	BEGIN	
	 anoatual := extract(year from now());	 
	 while(anoremessa is null) loop
         select 
         case when t.numero >= t.qtdMaxima then null else anoatual end as anoremessa
             INTO	anoremessa 
         from( select numero, lpad(cast('' as varchar),qtdPermitida,'9')::bigint as qtdMaxima  from transmissaonossonumero where ano = anoatual) as t; 

	 if(anoremessa is null ) then
		anoatual := anoatual+1;
		if((select numero from transmissaonossonumero where ano = anoatual) is null) then
			anoremessa := anoatual;
		end if;		
	 END IF;
	 end loop;
	 
	 if((select numero from transmissaonossonumero where ano = anoremessa) is null or anoremessa is null ) then
		INSERT INTO transmissaonossonumero (numero, ano) values (0, anoremessa);
	 END IF;
	 UPDATE transmissaonossonumero set numero = numero+1 where ano = anoremessa;
	 
	 transmissaonossonumero_codigo = (select numero from transmissaonossonumero where ano = anoremessa);
	
	 return query select transmissaonossonumero_codigo as transmissaonossonumero_codigo, anoremessa as anoremessa;
	END 
	$function$ 
	;
  

 