package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirFechamentoMesReceitaEnum implements PerfilTagSEIDecidirEnum {
	FECHAMENTO_MES_RECEITA_TIPO_ORIGEM_SIGLA("fechamentoFinanceiroConta", "Tipo Origem - Sigla",  "fechamentoFinanceiroConta.tipoOrigemContaReceber",TipoCampoEnum.TEXTO, 10),
	FECHAMENTO_MES_RECEITA_TIPO_ORIGEM_DESCRICAO("fechamentoFinanceiroConta", "Tipo Origem - Descrição",  "(case fechamentoFinanceiroConta.tipoOrigemContaReceber "+
			" when '"+TipoOrigemContaReceber.BIBLIOTECA+"' then '"+TipoOrigemContaReceber.BIBLIOTECA.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO+"' then '"+TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.MATRICULA+"' then '"+TipoOrigemContaReceber.MATRICULA.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.REQUERIMENTO+"' then '"+TipoOrigemContaReceber.REQUERIMENTO.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.OUTROS+"' then '"+TipoOrigemContaReceber.OUTROS.getDescricao()+"'"+			
			" when '"+TipoOrigemContaReceber.MENSALIDADE+"' then '"+TipoOrigemContaReceber.MENSALIDADE.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.DEVOLUCAO_CHEQUE+"' then '"+TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.NEGOCIACAO+"' then '"+TipoOrigemContaReceber.NEGOCIACAO.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.BOLSA_CUSTEADA_CONVENIO+"' then '"+TipoOrigemContaReceber.BOLSA_CUSTEADA_CONVENIO.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.CONTRATO_RECEITA+"' then '"+TipoOrigemContaReceber.CONTRATO_RECEITA.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.INCLUSAOREPOSICAO+"' then '"+TipoOrigemContaReceber.INCLUSAOREPOSICAO.getDescricao()+"' end) ",
			TipoCampoEnum.TEXTO, 20),
	FECHAMENTO_MES_RECEITA_NOSSO_NUMERO("fechamentoFinanceiroConta", "Nosso Número", "fechamentoFinanceiroConta.nossonumero",TipoCampoEnum.TEXTO, 20),
	FECHAMENTO_MES_RECEITA_PARCELA("fechamentoFinanceiroConta","Parcela", "fechamentoFinanceiroConta.parcela",TipoCampoEnum.TEXTO, 10),
	FECHAMENTO_MES_RECEITA_SITUACAO_SIGLA("fechamentoFinanceiroConta", "Situação - Sigla", "fechamentoFinanceiroConta.situacaoContaReceber" , TipoCampoEnum.TEXTO, 10),
	FECHAMENTO_MES_RECEITA_SITUACAO_DESCRICAO("fechamentoFinanceiroConta", "Situação - Descrição", "(case "+
	            " when '"+SituacaoContaReceber.A_RECEBER.getValor()+"' then '"+SituacaoContaReceber.A_RECEBER.getDescricao()+"'"+
				" when '"+SituacaoContaReceber.NEGOCIADO.getValor()+"' then '"+SituacaoContaReceber.NEGOCIADO.getDescricao()+"'"+
				" when '"+SituacaoContaReceber.RECEBIDO.getValor()+"' then '"+SituacaoContaReceber.RECEBIDO.getDescricao()+"'"+
				" when '"+SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor()+"' then '"+SituacaoContaReceber.CANCELADO_FINANCEIRO.getDescricao()+"'"+
				" when '"+SituacaoContaReceber.REMOVER.getValor()+"' then '"+SituacaoContaReceber.REMOVER.getDescricao()+"' end) ",
	            TipoCampoEnum.TEXTO, 20),
	FECHAMENTO_MES_RECEITA_PESSOA_SIGLA("fechamentoFinanceiroConta", "Tipo de Pessoa - Sigla",  "fechamentoFinanceiroConta.tipoPessoa",TipoCampoEnum.TEXTO, 10),
    
	FECHAMENTO_MES_RECEITA_PESSOA_DESCRICAO("fechamentoFinanceiroConta", "Tipo de Pessoa - Descrição",  "(case fechamentoFinanceiroConta.tipoPessoa "+
			" when '"+TipoSacado.FORNECEDOR+"' then '"+TipoPessoa.FORNECEDOR.getDescricao()+"'"+
			" when '"+TipoPessoa.ALUNO+"' then '"+TipoPessoa.ALUNO.getDescricao()+"'"+
			" when '"+TipoPessoa.CANDIDATO+"' then '"+TipoPessoa.CANDIDATO.getDescricao()+"'"+
			" when '"+TipoPessoa.FUNCIONARIO+"' then '"+TipoPessoa.FUNCIONARIO.getDescricao()+"'"+
			" when '"+TipoPessoa.PARCEIRO+"' then '"+TipoPessoa.PARCEIRO.getDescricao()+"'"+			
			" when '"+TipoPessoa.PROFESSOR+"' then '"+TipoPessoa.PROFESSOR.getDescricao()+"'"+
			" when '"+TipoPessoa.REQUERENTE+"' then '"+TipoPessoa.REQUERENTE.getDescricao()+"'"+
			" when '"+TipoPessoa.RESPONSAVEL_FINANCEIRO+"' then '"+TipoPessoa.RESPONSAVEL_FINANCEIRO.getDescricao()+"' end) ",
			TipoCampoEnum.TEXTO, 20),
	FECHAMENTO_MES_RECEITA_DATA_VENCIMENTO("fechamentoFinanceiroConta","Data Vencimento", "fechamentoFinanceiroConta.datavencimento",TipoCampoEnum.DATA,20),
	FECHAMENTO_MES_RECEITA_DATA_COMPETENCIA("fechamentoFinanceiroConta","Data Competência", "to_char(fechamentoFinanceiroConta.datacompetencia, 'MM/yyyy')",TipoCampoEnum.DATA, 15),
	FECHAMENTO_MES_RECEITA_DATA_RECEBIMENTO("fechamentoFinanceiroConta","Data Recebimento", "fechamentoFinanceiroConta.dataRecebimento",TipoCampoEnum.DATA,20),
	FECHAMENTO_MES_RECEITA_DATA_CANCELAMENTO("fechamentoFinanceiroConta","Data Cancelamento", "fechamentoFinanceiroConta.dataCancelamento",TipoCampoEnum.DATA,20),
	FECHAMENTO_MES_RECEITA_DATA_NEGOCIACAO("fechamentoFinanceiroConta","Data Negociacao", "fechamentoFinanceiroConta.dataNegociacao",TipoCampoEnum.DATA,20),
	
	FECHAMENTO_MES_RECEITA_SACADO("fechamentoFinanceiroConta", "Sacado", "fechamentoFinanceiroConta.nomeSacado", TipoCampoEnum.TEXTO, 40),
	FECHAMENTO_MES_RECEITA_CPF_CNPJ_SACADO("fechamentoFinanceiroConta", "CPF/CNPJ Sacado", "fechamentoFinanceiroConta.cpfCnpjSacado", TipoCampoEnum.TEXTO, 20),
	FECHAMENTO_MES_RECEITA_MATRICULA("fechamentoFinanceiroConta", "Matrícula", "fechamentoFinanceiroConta.matricula", TipoCampoEnum.TEXTO, 20),
	FECHAMENTO_MES_RECEITA_VALOR_RECEBER("fechamentoFinanceiroConta", "Valor", "fechamentoFinanceiroConta.valor",TipoCampoEnum.DOUBLE, 20),
	FECHAMENTO_MES_RECEITA_CONTA_CORRENTE("contacorrente", "Conta Corrente", "array_to_string(array(select case when contacorrente.nomeapresentacaosistema != '' then contacorrente.nomeapresentacaosistema else  banco.nome||'CC '||contacorrente.numero end from fechamentofinanceiroformapagamento "
			 +" inner join contacorrente on contacorrente.codigo = fechamentofinanceiroformapagamento.contacorrente "
			 +" left join agencia on contacorrente.agencia = agencia.codigo "
			 +" left join banco on agencia.banco = banco.codigo "
			 +" where fechamentofinanceiroformapagamento.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			 +" order by fechamentofinanceiroformapagamento.codigo "
			 +" ), ', ') ", TipoCampoEnum.TEXTO, 30),
	FECHAMENTO_MES_RECEITA_FORMAS_DE_RECEBIMENTO("formapagamento", "Formas de Recebimento/Pagamento - Lista", "array_to_string(array(select formapagamento.nome from fechamentofinanceiroformapagamento "
			+ " inner join formapagamento on formapagamento.codigo = fechamentofinanceiroformapagamento.formapagamento "
			+ " where fechamentofinanceiroformapagamento.fechamentofinanceiroconta =fechamentofinanceiroconta.codigo "
			+ " order by fechamentofinanceiroformapagamento.codigo "
			+ "), ', ') "
			+ "",TipoCampoEnum.TEXTO, 20),
	
	FECHAMENTO_MES_RECEITA_VALOR_CURSO("fechamentofinanceirodetalhamentovalor", "Valor Curso", "sum((select fechamentofinanceirodetalhamentovalor.valor from fechamentofinanceirodetalhamentovalor where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo and tipoCentroResultadoOrigemDetalhe = 'VALOR_BASE') ) ",TipoCampoEnum.DOUBLE, 20),
	
	
	FECHAMENTO_MES_RECEITA_VALOR_NORMAL("fechamentofinanceirodetalhamentovalor", "Valor Normal", "sum((select sum(case when tipoCentroResultadoOrigemDetalhe = 'VALOR_BASE' then fechamentofinanceirodetalhamentovalor.valor else 0.0 end) - "
			+ " sum(case when ( tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CONVENIO' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CUSTEADO_CONTA_RECEBER' "
			+ " or (( tipoCentroResultadoOrigemDetalhe = 'DESCONTO_MANUAL' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_INSTITUICAO') and dataLimiteAplicacaoDesconto is null) ) "
			+ " then fechamentofinanceirodetalhamentovalor.valor else 0.0 end) from fechamentofinanceirodetalhamentovalor "
			+ " left join convenio on 	convenio.codigo = fechamentofinanceirodetalhamentovalor.codOrigemDoTipoDetalhe and fechamentofinanceirodetalhamentovalor.tipoCentroResultadoOrigemDetalhe in ('DESCONTO_CUSTEADO_CONTA_RECEBER', 'DESCONTO_CONVENIO') "
			+ " left join parceiro on convenio.parceiro = parceiro.codigo "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ "))  ",TipoCampoEnum.DOUBLE, 20),
	
	FECHAMENTO_MES_RECEITA_VALOR_CONFIGURADO_DESCONTO_FIES("fechamentofinanceirodetalhamentovalor", "Valor Configurado FIES %/R\\$", "max((select max(case when (tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CONVENIO' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CUSTEADO_CONTA_RECEBER') "
			+ " and parceiro.nome ilike ('%FIES%') then case when fechamentofinanceirodetalhamentovalor.tipovalor = 'VALOR' then 'R\\$ ' else '' end "
			+ " || trim(to_char(fechamentofinanceirodetalhamentovalor.valortipovalor, '999G990D99')) || case when fechamentofinanceirodetalhamentovalor.tipovalor = 'VALOR' then '' else '%' end else '-' end) "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " inner join convenio on 	convenio.codigo = fechamentofinanceirodetalhamentovalor.codOrigemDoTipoDetalhe and fechamentofinanceirodetalhamentovalor.tipoCentroResultadoOrigemDetalhe in ('DESCONTO_CUSTEADO_CONTA_RECEBER', 'DESCONTO_CONVENIO') "
			+ " inner join parceiro on convenio.parceiro = parceiro.codigo "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ ")) ",TipoCampoEnum.TEXTO, 20),
	
	
	FECHAMENTO_MES_RECEITA_VALOR_FIES("fechamentofinanceirodetalhamentovalor", "Valor FIES", "sum((select sum(case when (tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CONVENIO' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CUSTEADO_CONTA_RECEBER' ) "
			+ " and parceiro.nome ilike ('%FIES%') then fechamentofinanceirodetalhamentovalor.valor else 0.0 end) "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " inner join convenio on 	convenio.codigo = fechamentofinanceirodetalhamentovalor.codOrigemDoTipoDetalhe "
			+ " and fechamentofinanceirodetalhamentovalor.tipoCentroResultadoOrigemDetalhe in ('DESCONTO_CUSTEADO_CONTA_RECEBER', 'DESCONTO_CONVENIO') "
			+ " inner join parceiro on convenio.parceiro = parceiro.codigo "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ ")) ",TipoCampoEnum.DOUBLE, 20),
	
	
	FECHAMENTO_MES_RECEITA_VALOR_CONFIGURADO_DESCONTO_OVG("fechamentofinanceirodetalhamentovalor", "Valor Configurado OVG %/R\\$", "max((select max(case when (tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CONVENIO' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CUSTEADO_CONTA_RECEBER' ) "
			+ " and parceiro.nome ilike ('%OVG%') then case when fechamentofinanceirodetalhamentovalor.tipovalor = 'VALOR' then 'R\\$ ' else '' end "
			+ " || trim(to_char(fechamentofinanceirodetalhamentovalor.valortipovalor, '999G990D99')) "
			+ " || case when fechamentofinanceirodetalhamentovalor.tipovalor = 'VALOR' then '' else '%' end else '-' end) "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " inner join convenio on 	convenio.codigo = fechamentofinanceirodetalhamentovalor.codOrigemDoTipoDetalhe "
			+ " and fechamentofinanceirodetalhamentovalor.tipoCentroResultadoOrigemDetalhe in ('DESCONTO_CUSTEADO_CONTA_RECEBER', 'DESCONTO_CONVENIO') "
			+ " inner join parceiro on convenio.parceiro = parceiro.codigo "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ "))  ",TipoCampoEnum.TEXTO, 20),
	
	
	FECHAMENTO_MES_RECEITA_VALOR_OVG("fechamentofinanceirodetalhamentovalor", "Valor OVG", "sum((select sum(case when (tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CONVENIO' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CUSTEADO_CONTA_RECEBER' ) "
			+ " and parceiro.nome ilike ('%OVG%') then fechamentofinanceirodetalhamentovalor.valor else 0.0 end) "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " inner join convenio on 	convenio.codigo = fechamentofinanceirodetalhamentovalor.codOrigemDoTipoDetalhe "
			+ " and fechamentofinanceirodetalhamentovalor.tipoCentroResultadoOrigemDetalhe in ('DESCONTO_CUSTEADO_CONTA_RECEBER', 'DESCONTO_CONVENIO') "
			+ " inner join parceiro on convenio.parceiro = parceiro.codigo "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ "))  ",TipoCampoEnum.DOUBLE, 20),
	
	
	FECHAMENTO_MES_RECEITA_VALOR_CONFIGURADO_DESCONTO_PRA_VALER("fechamentofinanceirodetalhamentovalor", "Valor Configurado Pra Valer %/R\\$", "max((select max(case when (tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CONVENIO' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CUSTEADO_CONTA_RECEBER' ) "
			+ " and parceiro.nome ilike ('%PRA VALER%') then case when fechamentofinanceirodetalhamentovalor.tipovalor = 'VALOR' then 'R\\$ ' else '' end  "
			+ " || trim(to_char(fechamentofinanceirodetalhamentovalor.valortipovalor, '999G990D99')) "
			+ " || case when fechamentofinanceirodetalhamentovalor.tipovalor = 'VALOR' then '' else '%' end else '-' end) "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " inner join convenio on 	convenio.codigo = fechamentofinanceirodetalhamentovalor.codOrigemDoTipoDetalhe and fechamentofinanceirodetalhamentovalor.tipoCentroResultadoOrigemDetalhe in ('DESCONTO_CUSTEADO_CONTA_RECEBER', 'DESCONTO_CONVENIO') "
			+ " inner join parceiro on convenio.parceiro = parceiro.codigo "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ "))  ",TipoCampoEnum.TEXTO, 20),
	
	
	FECHAMENTO_MES_RECEITA_VALOR_PRA_VALER("fechamentofinanceirodetalhamentovalor", "Valor Pra Valer", "sum((select sum(case when (tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CONVENIO' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CUSTEADO_CONTA_RECEBER' ) "
			+ " and parceiro.nome ilike ('%PRA VALER%') then fechamentofinanceirodetalhamentovalor.valor else 0.0 end) "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " inner join convenio on 	convenio.codigo = fechamentofinanceirodetalhamentovalor.codOrigemDoTipoDetalhe and fechamentofinanceirodetalhamentovalor.tipoCentroResultadoOrigemDetalhe in ('DESCONTO_CUSTEADO_CONTA_RECEBER', 'DESCONTO_CONVENIO') "
			+ " inner join parceiro on convenio.parceiro = parceiro.codigo "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ ")) ",TipoCampoEnum.DOUBLE, 20),
	
	
	FECHAMENTO_MES_RECEITA_VALOR_CONFIGURADO_DESCONTO_BOLSA_FUNCIONAL("fechamentofinanceirodetalhamentovalor", "Valor Configurado Bolsa Funcional %/R\\$", "max((select max(case when (tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CONVENIO' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CUSTEADO_CONTA_RECEBER' ) "
			+ " and parceiro.nome ilike ('%BOLSA FUNCIONAL%') then case when fechamentofinanceirodetalhamentovalor.tipovalor = 'VALOR' then 'R\\$ ' else '' end "
			+ " || trim(to_char(fechamentofinanceirodetalhamentovalor.valortipovalor, '999G990D99')) "
			+ " || case when fechamentofinanceirodetalhamentovalor.tipovalor = 'VALOR' then '' else '%' end else '-' end)  "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " inner join convenio on 	convenio.codigo = fechamentofinanceirodetalhamentovalor.codOrigemDoTipoDetalhe and fechamentofinanceirodetalhamentovalor.tipoCentroResultadoOrigemDetalhe in ('DESCONTO_CUSTEADO_CONTA_RECEBER', 'DESCONTO_CONVENIO') "
			+ " inner join parceiro on convenio.parceiro = parceiro.codigo "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ ")) ",TipoCampoEnum.TEXTO, 20),
	
	
	FECHAMENTO_MES_RECEITA_VALOR_BOLSA_FUNCIONAL("fechamentofinanceirodetalhamentovalor", "Valor Bolsa Funcional", "sum((select sum(case when (tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CONVENIO' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CUSTEADO_CONTA_RECEBER' ) "
			+ " and parceiro.nome ilike ('%BOLSA FUNCIONAL%') then fechamentofinanceirodetalhamentovalor.valor else 0.0 end) "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " inner join convenio on 	convenio.codigo = fechamentofinanceirodetalhamentovalor.codOrigemDoTipoDetalhe and fechamentofinanceirodetalhamentovalor.tipoCentroResultadoOrigemDetalhe in ('DESCONTO_CUSTEADO_CONTA_RECEBER', 'DESCONTO_CONVENIO') "
			+ " inner join parceiro on convenio.parceiro = parceiro.codigo "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ ")) ",TipoCampoEnum.DOUBLE, 20),
	
	
	FECHAMENTO_MES_RECEITA_VALOR_CONFIGURADO_DESCONTO_PROUNI("fechamentofinanceirodetalhamentovalor", "Valor Configurado Prouni %/R\\$", "max((select max(case when (tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CONVENIO' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CUSTEADO_CONTA_RECEBER' ) "
			+ " and parceiro.nome ilike ('%PROUNI%') then case when fechamentofinanceirodetalhamentovalor.tipovalor = 'VALOR' then 'R\\$ ' else '' end "
			+ " || trim(to_char(fechamentofinanceirodetalhamentovalor.valortipovalor, '999G990D99')) || case when fechamentofinanceirodetalhamentovalor.tipovalor = 'VALOR' then '' else '%' end else '-' end) "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " inner join convenio on 	convenio.codigo = fechamentofinanceirodetalhamentovalor.codOrigemDoTipoDetalhe and fechamentofinanceirodetalhamentovalor.tipoCentroResultadoOrigemDetalhe in ('DESCONTO_CUSTEADO_CONTA_RECEBER', 'DESCONTO_CONVENIO') "
			+ " inner join parceiro on convenio.parceiro = parceiro.codigo "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ ")) ",TipoCampoEnum.TEXTO, 20),
	
	
	FECHAMENTO_MES_RECEITA_VALOR_PROUNI("fechamentofinanceirodetalhamentovalor", "Valor Prouni", "sum((select sum(case when (tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CONVENIO' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CUSTEADO_CONTA_RECEBER' ) "
			+ " and parceiro.nome ilike ('%PROUNI%') then fechamentofinanceirodetalhamentovalor.valor else 0.0 end)  "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " inner join convenio on 	convenio.codigo = fechamentofinanceirodetalhamentovalor.codOrigemDoTipoDetalhe and fechamentofinanceirodetalhamentovalor.tipoCentroResultadoOrigemDetalhe in ('DESCONTO_CUSTEADO_CONTA_RECEBER', 'DESCONTO_CONVENIO') "
			+ " inner join parceiro on convenio.parceiro = parceiro.codigo "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ ")) ",TipoCampoEnum.DOUBLE, 20),
	
	
	FECHAMENTO_MES_RECEITA_VALOR_CONFIGURADO_CONVENIO("fechamentofinanceirodetalhamentovalor", "Valor Configurado Convênio %/R\\$", "array_to_string(array_agg(distinct (select unnest(parceiro.parceiro) from (select array_agg(distinct  "
			+ " case when (tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CONVENIO' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CUSTEADO_CONTA_RECEBER' ) "
			+ " and parceiro.nome not ilike ('%PROUNI%') and parceiro.nome not ilike ('%OVG%') and parceiro.nome not ilike ('%FIES%') and parceiro.nome not ilike ('%Bolsa Funcional%') "
			+ " and parceiro.nome not ilike ('%Pra Valer%') then case when fechamentofinanceirodetalhamentovalor.tipovalor = 'VALOR' then 'R\\$ ' else '' end "
			+ " || trim(to_char(fechamentofinanceirodetalhamentovalor.valortipovalor, '999G990D99')) "
			+ " || case when fechamentofinanceirodetalhamentovalor.tipovalor = 'VALOR' then '' else '%' end end ) as parceiro "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " inner join convenio on 	convenio.codigo = fechamentofinanceirodetalhamentovalor.codOrigemDoTipoDetalhe and fechamentofinanceirodetalhamentovalor.tipoCentroResultadoOrigemDetalhe in ('DESCONTO_CUSTEADO_CONTA_RECEBER', 'DESCONTO_CONVENIO') "
			+ " inner join parceiro on convenio.parceiro = parceiro.codigo "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ ") as parceiro limit 1 )), ', ') ",TipoCampoEnum.TEXTO, 20),
	
	
	FECHAMENTO_MES_RECEITA_NOME_CONVENIO("fechamentofinanceirodetalhamentovalor", "Nome Convênio", "array_to_string(array_agg(distinct (select unnest(parceiro.parceiro) from (select array_agg(distinct  "
			+ " case when (tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CONVENIO' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CUSTEADO_CONTA_RECEBER' ) and parceiro.nome not ilike ('%PROUNI%') "
			+ " and parceiro.nome not ilike ('%OVG%') and parceiro.nome not ilike ('%FIES%') and parceiro.nome not ilike ('%Bolsa Funcional%') "
			+ " and parceiro.nome not ilike ('%Pra Valer%') then parceiro.nome end ) as parceiro "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " inner join convenio on 	convenio.codigo = fechamentofinanceirodetalhamentovalor.codOrigemDoTipoDetalhe "
			+ " and fechamentofinanceirodetalhamentovalor.tipoCentroResultadoOrigemDetalhe in ('DESCONTO_CUSTEADO_CONTA_RECEBER', 'DESCONTO_CONVENIO') "
			+ " inner join parceiro on convenio.parceiro = parceiro.codigo "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ ") as parceiro  limit 1 )), ', ') ",TipoCampoEnum.TEXTO, 70),
	
	
	FECHAMENTO_MES_RECEITA_VALOR_CONVENIO("fechamentofinanceirodetalhamentovalor", "Valor Convênio", "sum((select sum(case when (tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CONVENIO' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_CUSTEADO_CONTA_RECEBER') "
			+ " and parceiro.nome not ilike ('%PROUNI%') and parceiro.nome not ilike ('%OVG%') and parceiro.nome not ilike ('%FIES%') and parceiro.nome not ilike ('%Bolsa Funcional%') "
			+ " and parceiro.nome not ilike ('%Pra Valer%') then fechamentofinanceirodetalhamentovalor.valor else 0.0 end) "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " inner join convenio on 	convenio.codigo = fechamentofinanceirodetalhamentovalor.codOrigemDoTipoDetalhe and fechamentofinanceirodetalhamentovalor.tipoCentroResultadoOrigemDetalhe in ('DESCONTO_CUSTEADO_CONTA_RECEBER', 'DESCONTO_CONVENIO') "
			+ " inner join parceiro on convenio.parceiro = parceiro.codigo "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ "))  ",TipoCampoEnum.DOUBLE, 20),
	
	
	
	FECHAMENTO_MES_RECEITA_VALOR_DESC_INSTITUCIONAL_INCONDICIONAL("fechamentofinanceirodetalhamentovalor", "Valor Desc.Institucional Incondicional", "sum((select 	sum(case when (tipoCentroResultadoOrigemDetalhe = 'DESCONTO_MANUAL' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_INSTITUICAO' ) "
			+ " and dataLimiteAplicacaoDesconto is null then fechamentofinanceirodetalhamentovalor.valor else 0.0 end) "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ "))  ",TipoCampoEnum.DOUBLE, 20),
	
	FECHAMENTO_MES_RECEITA_VALOR_DESC_INSTITUCIONAL_CONDICIONAL("fechamentofinanceirodetalhamentovalor", "Valor Desc.Institucional Condicional", "sum((select sum(case when (tipoCentroResultadoOrigemDetalhe = 'DESCONTO_MANUAL' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_INSTITUICAO') "
			+ " and dataLimiteAplicacaoDesconto is not null and fechamentofinanceirodetalhamentovalor.utilizado then fechamentofinanceirodetalhamentovalor.valor else 0.0 end)  "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ ")) ",TipoCampoEnum.DOUBLE, 20),
	
	FECHAMENTO_MES_RECEITA_DESC_INST_CONDICIONAL_NAO_UTILIZADO("fechamentofinanceirodetalhamentovalor", "Valor Desc.Institucional Condicional Não Utilizado", "sum((select sum(case when (tipoCentroResultadoOrigemDetalhe = 'DESCONTO_MANUAL' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_INSTITUICAO') and dataLimiteAplicacaoDesconto is not null "
			+ " and fechamentofinanceirodetalhamentovalor.utilizado = false then fechamentofinanceirodetalhamentovalor.valor else 0.0 end)  "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ "))  ",TipoCampoEnum.DOUBLE, 20),
	
	
	FECHAMENTO_MES_RECEITA_VALOR_CONFIGURADO_DESCONTO_PROGRESSIVO("fechamentofinanceirodetalhamentovalor", "Valor Configurado Desc.Progressivo", "max((select max(case when tipoCentroResultadoOrigemDetalhe = 'DESCONTO_PROGRESSIVO' and fechamentofinanceirodetalhamentovalor.utilizado then "
			+ " case when fechamentofinanceirodetalhamentovalor.tipovalor = 'VALOR' then 'R\\$ ' else '' end || trim(to_char(fechamentofinanceirodetalhamentovalor.valortipovalor, '999G990D99'))  "
			+ " || case when fechamentofinanceirodetalhamentovalor.tipovalor = 'VALOR' then '' else '%' end else '' end) "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ ")) ",TipoCampoEnum.TEXTO, 20),
	
	
	FECHAMENTO_MES_RECEITA_VALOR_DESCONTO_PROGRESSIVO("fechamentofinanceirodetalhamentovalor", "Valor Desc.Progressivo", "sum((select sum(case when tipoCentroResultadoOrigemDetalhe = 'DESCONTO_PROGRESSIVO' "
			+ " and fechamentofinanceirodetalhamentovalor.utilizado then fechamentofinanceirodetalhamentovalor.valor else 0.0 end)  "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ ")) ",TipoCampoEnum.DOUBLE, 20),
	
	FECHAMENTO_MES_RECEITA_VALOR_OUTRAS_DEDUCOES("fechamentofinanceirodetalhamentovalor", "Valor Outras Deduções", "sum((  "
			+ " select sum(case when tipoCentroResultadoOrigemDetalhe = 'DESCONTO_RECEBIMENTO' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_ALUNO' or tipoCentroResultadoOrigemDetalhe = 'DESCONTO_RATEIO' "
			+ " then fechamentofinanceirodetalhamentovalor.valor else 0.0 end) from fechamentofinanceirodetalhamentovalor "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ "))  ",TipoCampoEnum.DOUBLE, 20),
	
	FECHAMENTO_MES_RECEITA_VALOR_JURO_MULTA_ACRESCIMO("fechamentofinanceirodetalhamentovalor", "Valor Juro, Multa e Acréscimo", "sum((select sum(case when (tipoCentroResultadoOrigemDetalhe = 'JURO' or tipoCentroResultadoOrigemDetalhe = 'MULTA' or tipoCentroResultadoOrigemDetalhe = 'ACRESCIMO' "
			+ " or tipoCentroResultadoOrigemDetalhe = 'REAJUSTE_PRECO') then fechamentofinanceirodetalhamentovalor.valor else 0.0 end) "
			+ " from fechamentofinanceirodetalhamentovalor "
			+ " where fechamentofinanceirodetalhamentovalor.fechamentofinanceiroconta = fechamentofinanceiroconta.codigo "
			+ ")) ",TipoCampoEnum.DOUBLE, 20),
	
	FECHAMENTO_MES_RECEITA_VALOR_PAGO("fechamentofinanceiroconta", "Valor Pago", "fechamentofinanceiroconta.valor ",TipoCampoEnum.DOUBLE, 20),
	
	;
	
	private TagSEIDecidirFechamentoMesReceitaEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
		this.tamanhoCampo = tamanhoCampo;
		this.entidade = entidade;
		this.campo = campo;
		this.atributo = atributo;
		this.tipoCampo = tipoCampo;
	}

	private String entidade;
	private String campo;
	private String atributo;
	private TipoCampoEnum tipoCampo;
	private Integer tamanhoCampo;
	public String getEntidade() {
		return entidade;
	}

	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}

	public String getCampo() {	
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	public TipoCampoEnum getTipoCampo() {		
		return tipoCampo;
	}

	public void setTipoCampo(TipoCampoEnum tipoCampo) {
		this.tipoCampo = tipoCampo;
	}

	@Override
	public String getTag() {	
		return this.name();
	}

	public String getAtributo() {
		if (atributo == null) {
			atributo = "";
		}
		return atributo;
	}

	public void setAtributo(String atributo) {
		this.atributo = atributo;
	}
	
	/* (non-Javadoc)
	 * @see negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum#getTamanhoCampo()
	 */
	@Override
	public Integer getTamanhoCampo() {

		return tamanhoCampo;
	}

	/* (non-Javadoc)
	 * @see negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum#setTamanhoCampo(java.lang.Integer)
	 */
	@Override
	public void setTamanhoCampo(Integer tamanhoCampo) {
		this.tamanhoCampo = tamanhoCampo;		
	}
	
}
