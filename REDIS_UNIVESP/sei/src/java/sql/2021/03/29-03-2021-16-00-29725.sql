create or replace
function fn_validarsituacaocontareceber() returns trigger language plpgsql as $function$ declare v_retorno boolean := false;
begin
if (new.situacao in ('CF', 'RM', 'RC', 'CP', 'EP', 'EX')) then 
	v_retorno := true;
end if ;

if (new.situacao = 'RE' and old.situacao = 'RE') then 
	v_retorno := true;
end if;
if (new.situacao = 'RE' and old.situacao = 'AR' and exists (
	select
		negociacaorecebimento.codigo
	from
		contarecebernegociacaorecebimento
	inner join negociacaorecebimento on
		negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento
	where
		contarecebernegociacaorecebimento.contareceber = new.codigo)
	and not exists (
	select
		negociacaocontareceber.codigo
	from
		contarecebernegociado
	inner join negociacaocontareceber on
		negociacaocontareceber.codigo = contarecebernegociado.negociacaocontareceber
	where
		contarecebernegociado.contareceber = new.codigo) ) then 
	v_retorno := true;
end if;

if (new.situacao = 'AR' and not exists (
	select
		negociacaorecebimento.codigo
	from
		contarecebernegociacaorecebimento
	inner join negociacaorecebimento on
		negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento
	where
		contarecebernegociacaorecebimento.contareceber = new.codigo)
	and not exists (
	select
		negociacaocontareceber.codigo
	from
		contarecebernegociado
	inner join negociacaocontareceber on
		negociacaocontareceber.codigo = contarecebernegociado.negociacaocontareceber
	where
		contarecebernegociado.contareceber = new.codigo) ) then 
	v_retorno := true;
end if;

if (new.situacao = 'NE' and exists (
	select
		negociacaocontareceber.codigo
	from
		contarecebernegociado
	inner join negociacaocontareceber on
		negociacaocontareceber.codigo = contarecebernegociado.negociacaocontareceber
	where
		contarecebernegociado.contareceber = new.codigo)
	and not exists (
	select
		negociacaorecebimento.codigo
	from
		contarecebernegociacaorecebimento
	inner join negociacaorecebimento on
		negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento
	where
		contarecebernegociacaorecebimento.contareceber = new.codigo) ) then 
	v_retorno := true;
end if;

if (v_retorno is false) then 
	raise exception 'ERRO';
end if;

return new;
end;
$function$;

create trigger tg_validar_situacaocontareceber before update on	contareceber for each row execute function fn_validarsituacaocontareceber();