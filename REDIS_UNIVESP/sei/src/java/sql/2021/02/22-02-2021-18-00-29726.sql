DROP FUNCTION  inserircodigotransmissaonossonumero(qtdpermitida integer);


CREATE OR REPLACE FUNCTION public.inserircodigotransmissaonossonumero(qtdpermitida integer)
 RETURNS TABLE(transmissaonossonumero_codigo integer, anoremessa integer)
 LANGUAGE plpgsql
AS $function$
DECLARE
resultadoBusca RECORD;
transmissaonossonumero_codigo integer;
anoatual integer;
  anoauxiliar integer;
  codtransmissaoanoauxiliar integer;
BEGIN
anoatual := extract(year from now());
while(anoremessa is null) loop
select
case when t.numero >= t.qtdMaxima then null else anoatual end as anoremessa
INTO anoremessa
from( select numero, lpad(cast('' as varchar),qtdPermitida,'9')::bigint as qtdMaxima from transmissaonossonumero where ano = anoatual) as t;

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

if exists(select 1 from contapagarcontroleremessacontapagar cpcrp
where cpcrp.transmissaonossonumero = (transmissaonossonumero_codigo::text || substring((anoremessa::text) from 3 for 4)) and contapagar is not null) then
  select  funcao.transmissaonossonumero_codigo, funcao.anoremessa into  codtransmissaoanoauxiliar , anoauxiliar   from  public.inserircodigotransmissaonossonumero(qtdpermitida) as funcao;
  transmissaonossonumero_codigo := codtransmissaoanoauxiliar;
  anoremessa := anoauxiliar;
  codtransmissaoanoauxiliar := null;
  anoauxiliar := null;
 
end if;
return query select transmissaonossonumero_codigo as transmissaonossonumero_codigo, anoremessa as anoremessa;
END
$function$
;