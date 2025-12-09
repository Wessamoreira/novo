update centroresultadoorigem set valor = tt.valorrecebido from (
	select * from (
		select contareceber.codigo::text, contareceber.valorrecebido , sum(centroresultadoorigem.valor) as valorResultado from contareceber
		inner join centroresultadoorigem on  centroresultadoorigem.tipocentroresultadoorigem = 'CONTA_RECEBER' and centroresultadoorigem.codorigem = contareceber.codigo::text
		where  1=1
		and contareceber.situacao = 'RE'
		group by contareceber.codigo, contareceber.valorrecebido
		having count(centroresultadoorigem.codigo) = 1
	) as t where t.valorrecebido != t.valorResultado
) as tt where centroresultadoorigem.tipocentroresultadoorigem = 'CONTA_RECEBER'
and centroresultadoorigem.codorigem = tt.codigo;