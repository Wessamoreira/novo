CREATE OR REPLACE FUNCTION public.fn_validarsituacaorequisicao()
 RETURNS trigger
 LANGUAGE plpgsql
AS $function$ declare v_retorno boolean := false;
begin
if (new.situacaoentrega IN ('FI') and (select coalesce(sum(ri.quantidadeentregue), 0) = coalesce(sum(ri.quantidadeautorizada), 0) from requisicaoitem ri 
	where ri.requisicao = new.codigo)) then
	v_retorno := true;
end if;
if (new.situacaoentrega IN ('PA') and (select coalesce(sum(ri.quantidadeentregue), 0) < coalesce(sum(ri.quantidadeautorizada), 0) and
	coalesce(sum(ri.quantidadeentregue), 0) > 0 from requisicaoitem ri 
	where ri.requisicao = new.codigo)) then
	v_retorno := true;
end if;
if (new.situacaoentrega IN ('PE') and (select coalesce(sum(ri.quantidadeentregue), 0) = 0 from requisicaoitem ri where ri.requisicao = new.codigo)) then
	v_retorno := true;
end if;
if (v_retorno is false) then 
	raise exception 'ERRO';
end if;
return new;
end;
$function$
;