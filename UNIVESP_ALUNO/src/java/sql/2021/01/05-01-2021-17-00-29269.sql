CREATE OR REPLACE FUNCTION fn_validartiporequerimentoabrirdeferimento(codigo_tiporequerimento integer, codigo_tiporequerimentodeferimento integer) RETURNS boolean AS $$
DECLARE 
   nao_existe_tiporequerimento boolean;
begin
	select into nao_existe_tiporequerimento nao_existe from (with recursive arvore_tiporequerimentoabrirdeferimento as (select tiporequerimento.codigo,	tiporequerimento.tiporequerimentoabrirdeferimento
	from tiporequerimento where	tiporequerimento.codigo = codigo_tiporequerimento union all select tiporequerimento.codigo, tiporequerimento.tiporequerimentoabrirdeferimento
	from tiporequerimento inner join arvore_tiporequerimentoabrirdeferimento on	arvore_tiporequerimentoabrirdeferimento.codigo = tiporequerimento.tiporequerimentoabrirdeferimento)
	select case when codigo is not null then false else true end nao_existe from arvore_tiporequerimentoabrirdeferimento where codigo = codigo_tiporequerimentodeferimento) as t;
    RETURN nao_existe_tiporequerimento;                
END;     
$$ LANGUAGE plpgsql;
alter table	tiporequerimento add constraint check_tiporequerimento_tiporequerimentoabrirdeferimento check 
	(fn_validartiporequerimentoabrirdeferimento(codigo, tiporequerimentoabrirdeferimento)) not valid;