drop function if exists public.consultarnegociacaorecebimentosoftfin(date, date);

CREATE OR REPLACE FUNCTION public.consultarnegociacaorecebimentosoftfin(inicio date, fim date) 
RETURNS TABLE (
cnpjunidadeensino varchar(18), 
datarecebimento timestamp,
cpfCliente varchar(18), 
valorliquidocontareceber numeric(20,2),
tipoFormaPagamento int,
contacorrente varchar(100),
contacorrentedigito  varchar(2),
codigocontareceber int,
nossonumerocontareceber varchar(25),
dataemissao timestamp,
datavencimento timestamp without time zone,
codorigem varchar(100),
codigocentroresultado int,
descricaocentroresultado varchar(255),
valorcentroresultadoorigem numeric(20,2)
) AS $$
BEGIN
RETURN QUERY SELECT			
				unidadeensino.cnpj as cnpjunidadeensino,
				formapagamentonegociacaorecebimento.datacredito as dataRecebimento,
				pessoa.cpf as cpfCliente,
				crnr.valortotal as valorLiquidoContaReceber,
				case when formapagamento.tipo in ('CA','CD', 'DE') then 63					 
					 when formapagamento.tipo = 'BO' then 402
					 else 0 end  as tipoFormaPagamento,
				contacorrente.numero as contacorrente,
				contacorrente.digito as contacorrentedigito,
				contareceber.codigo as codigocontareceber,
				contareceber.nossonumero as nossonumerocontareceber,
				contareceber.data as dataemissao,
				contareceber.datavencimento as datavencimento,
				centroresultadoorigem.codorigem as codorigem, 
			    centroresultado.codigo  as codigocentroresultado, 
				centroresultado.descricao as descricaocentroresultado, 
				centroresultadoorigem.valor as valorcentroresultadoorigem
			from  negociacaorecebimento nr
			inner join unidadeensino on	unidadeensino.codigo = nr.unidadeensino
			inner join formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.negociacaorecebimento = nr.codigo
			inner join contacorrente on	contacorrente.codigo = formapagamentonegociacaorecebimento.contacorrente
			inner join formapagamento on formapagamento.codigo = formapagamentonegociacaorecebimento.formapagamento
			inner join contarecebernegociacaorecebimento crnr on  crnr.negociacaorecebimento = nr.codigo
			inner join contareceber on contareceber.codigo = crnr.contareceber
			inner join matricula on matricula.matricula = contareceber.matriculaaluno 
			inner join pessoa on pessoa.codigo = matricula.aluno
			inner join centroresultadoorigem on centroresultadoorigem.codorigem = contareceber.codigo::text and centroresultadoorigem.tipoCentroResultadoOrigem = 'CONTA_RECEBER' 
			inner join centroresultado on centroresultado.codigo = centroresultadoorigem.centroresultadoadministrativo
			where formapagamentonegociacaorecebimento.datacredito >= (inicio::timestamp)
				and formapagamentonegociacaorecebimento.datacredito <= (fim || ' 23:59:59')::timestamp
				and nr.tipopessoa = 'AL'
				and unidadeensino.codigo in(1000)
				and contareceber.situacao = 'RE'
				and formapagamento.tipo in ('CA','CD', 'DE', 'BO')
				and not exists ( select codigo from contarecebersoftfin where contarecebersoftfin.numerodocumento = contareceber.codigo )
				order by contareceber.codigo;
END
$$ LANGUAGE plpgsql;
