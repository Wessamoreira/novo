package negocio.comuns.administrativo.enumeradores;

import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;

public enum TagSEIDecidirContaReceberEnum implements PerfilTagSEIDecidirEnum {
	CONTA_RECEBER_CODIGO("contareceber","Código", "contareceber.codigo",TipoCampoEnum.INTEIRO, 20),		
	CONTA_RECEBER_COD_BARRA("contareceber", "Código de Barra", "contareceber.codigobarra",TipoCampoEnum.TEXTO, 40),
	CONTA_RECEBER_DATA_VENCIMENTO("contareceber","Data Vencimento", "contareceber.datavencimento",TipoCampoEnum.DATA,20),
	CONTA_RECEBER_PARCELA("contareceber","Parcela", "contareceber.parcela",TipoCampoEnum.TEXTO, 10),
	CONTA_RECEBER_DATA_COMPETENCIA("contareceber","Data de Competência", "to_char(contareceber.datacompetencia, 'MM/yyyy')",TipoCampoEnum.TEXTO, 15),
	CONTA_RECEBER_LINHA_DIGITAVEL_CODIGO_BARRAS("contareceber", "Linha Digitável Código de Barras", "contareceber.linhaDigitavelCodigoBarras", TipoCampoEnum.TEXTO, 40),
	CONTA_RECEBER_NOSSO_NUMERO("contareceber", "Nosso Número", "contareceber.nossonumero",TipoCampoEnum.TEXTO, 20),
	CONTA_RECEBER_NR_DOCUMENTO("contareceber", "Número Documento", "contareceber.nrdocumento",TipoCampoEnum.TEXTO, 20),	
	CONTA_RECEBER_CENTRO_RECEITA_DESCRICAO("contarecebercentroreceita", "Descrição Centro de Receita", "array_to_string(array(select centroreceita.descricao from centroresultadoorigem inner join centroreceita on centroreceita.codigo = centroresultadoorigem.centroreceita where centroresultadoorigem.tipoCentroResultadoOrigem = 'CONTA_RECEBER' and centroresultadoorigem.codOrigem =  contareceber.codigo::VARCHAR  ), ', ') ",TipoCampoEnum.TEXTO, 30),	
	CONTA_RECEBER_CENTRO_RECEITA_IDENTIFICADOR("contarecebercentroreceita","Identificador Centro Receita", "array_to_string(array(select centroreceita.identificadorCentroReceita from centroresultadoorigem inner join centroreceita on centroreceita.codigo = centroresultadoorigem.centroreceita where centroresultadoorigem.tipoCentroResultadoOrigem = 'CONTA_RECEBER' and centroresultadoorigem.codOrigem =  contareceber.codigo::VARCHAR  ), ', ')",TipoCampoEnum.TEXTO, 20),	
	CONTA_RECEBER_SACADO("contareceber", "Sacado", "(case contareceber.tipoPessoa "+
			" when '"+TipoSacado.FORNECEDOR.getValor()+"' then contareceberfornecedor.nome "+
			" when '"+TipoPessoa.ALUNO.getValor()+"' then contareceberpessoa.nome "+
			" when '"+TipoPessoa.CANDIDATO.getValor()+"' then contareceberpessoa.nome "+
			" when '"+TipoPessoa.FUNCIONARIO.getValor()+"' then contareceberpessoa.nome "+
			" when '"+TipoPessoa.PARCEIRO.getValor()+"' then contareceberparceiro.nome || (case when contareceberpessoa.codigo is not null then ' (ALUNO(A) '||contareceberpessoa.nome||' - '|| contareceber.matriculaaluno ||' )' else '' end) "+			
			" when '"+TipoPessoa.PROFESSOR.getValor()+"' then contareceberpessoa.nome "+
			" when '"+TipoPessoa.REQUERENTE.getValor()+"' then contareceberpessoa.nome "+
			" when '"+TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()+"' then contareceberresponsavelfinanceiro.nome || (case when contareceberpessoa.codigo is not null then ' (ALUNO(A) '||contareceberpessoa.nome||' - '|| contareceber.matriculaaluno ||' )' else '' end) end) "
			,TipoCampoEnum.TEXTO, 40),
	CONTA_RECEBER_CPF_SACADO("contareceber", "CPF Sacado", "(case contareceber.tipoPessoa "+
					" when '"+TipoSacado.FORNECEDOR.getValor()+"' then case when contareceberfornecedor.tipoEmpresa = 'FI' then contareceberfornecedor.cpf else '' end  "+
					" when '"+TipoPessoa.ALUNO.getValor()+"' then contareceberpessoa.cpf "+
					" when '"+TipoPessoa.CANDIDATO.getValor()+"' then contareceberpessoa.cpf "+
					" when '"+TipoPessoa.FUNCIONARIO.getValor()+"' then contareceberpessoa.cpf "+
					" when '"+TipoPessoa.PARCEIRO.getValor()+"' then case when contareceberparceiro.tipoEmpresa = 'FI' then contareceberparceiro.cpf else '' end "+			
					" when '"+TipoPessoa.PROFESSOR.getValor()+"' then contareceberpessoa.cpf "+
					" when '"+TipoPessoa.REQUERENTE.getValor()+"' then contareceberpessoa.cpf "+
					" when '"+TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()+"' then contareceberresponsavelfinanceiro.cpf else '' end) "
					,TipoCampoEnum.TEXTO, 20),					
    CONTA_RECEBER_CNPJ_SACADO("contareceber", "CNPJ Sacado", "(case contareceber.tipoPessoa "+
							" when '"+TipoSacado.FORNECEDOR.getValor()+"' then case when contareceberfornecedor.tipoEmpresa = 'JU' then contareceberfornecedor.cnpj else '' end  "+
							" when '"+TipoPessoa.PARCEIRO.getValor()+"' then case when contareceberparceiro.tipoEmpresa = 'JU' then contareceberparceiro.cnpj else '' end "+			
							" else '' end)"
							,TipoCampoEnum.TEXTO, 20),
	CONTA_RECEBER_IDENTIDADE_SACADO("contareceber", "RG Sacado", "(case contareceber.tipoPessoa "+
			" when '"+TipoSacado.FORNECEDOR.getValor()+"' then ''  "+
			" when '"+TipoPessoa.PARCEIRO.getValor()+"' then '' "+			
			" when '"+TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()+"' then contareceberresponsavelfinanceiro.rg "+
			" else contareceberpessoa.rg end)", TipoCampoEnum.TEXTO, 20),
	CONTA_RECEBER_DATA_NASCIMENTO_SACADO("contareceber", "Data Nasc. Sacado", "(case contareceber.tipoPessoa "+
			" when '"+TipoSacado.FORNECEDOR.getValor()+"' then null  "+
			" when '"+TipoPessoa.PARCEIRO.getValor()+"' then null "+			
			" when '"+TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()+"' then contareceberresponsavelfinanceiro.datanasc "+
			" else contareceberpessoa.datanasc end)", TipoCampoEnum.DATA, 20),
	CONTA_RECEBER_EMAIL_SACADO("contareceber", "E-mail Sacado", "(case contareceber.tipoPessoa "+
			" when '"+TipoSacado.FORNECEDOR.getValor()+"' then contareceberfornecedor.email  "+
			" when '"+TipoPessoa.PARCEIRO.getValor()+"' then contareceberparceiro.email "+			
			" when '"+TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()+"' then contareceberresponsavelfinanceiro.email "+
			" else contareceberpessoa.email end)"
			,TipoCampoEnum.TEXTO, 20),
	CONTA_RECEBER_ENDERECO_SACADO("contareceber", "Endereço Sacado", "(case contareceber.tipoPessoa "+
			" when '"+TipoSacado.FORNECEDOR.getValor()+"' then contareceberfornecedor.endereco  "+
			" when '"+TipoPessoa.PARCEIRO.getValor()+"' then contareceberparceiro.endereco "+	
			" when '"+TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()+"' then contareceberresponsavelfinanceiro.endereco "+
			" else contareceberpessoa.endereco end)"
			,TipoCampoEnum.TEXTO, 50),
	CONTA_RECEBER_BAIRRO_SACADO("contareceber", "Bairro Sacado", "(case contareceber.tipoPessoa "+
			" when '"+TipoSacado.FORNECEDOR.getValor()+"' then contareceberfornecedor.setor  "+
			" when '"+TipoPessoa.PARCEIRO.getValor()+"' then contareceberparceiro.setor "+
			" when '"+TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()+"' then contareceberresponsavelfinanceiro.setor "+
			" else contareceberpessoa.setor end)"					
			,TipoCampoEnum.TEXTO, 50),
	CONTA_RECEBER_CIDADE_SACADO("contareceber", "Cidade Sacado", "(case contareceber.tipoPessoa "+
			" when '"+TipoSacado.FORNECEDOR.getValor()+"' then contarecebercidadefornecedor.nome  "+
			" when '"+TipoPessoa.PARCEIRO.getValor()+"' then contarecebercidadeparceiro.nome "+			
			" when '"+TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()+"' then contarecebercidaderesponsavelfinanceiro.nome "+
			" else contarecebercidadepessoa.nome end)"					
			,TipoCampoEnum.TEXTO, 50),
	CONTA_RECEBER_ESTADO_SACADO("contareceber", "Estado Sacado", "(case contareceber.tipoPessoa "+
		" when '"+TipoSacado.FORNECEDOR.getValor()+"' then contareceberestadofornecedor.sigla  "+
		" when '"+TipoPessoa.PARCEIRO.getValor()+"' then contareceberestadoparceiro.sigla "+			
		" when '"+TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()+"' then contareceberestadoresponsavelfinanceiro.sigla "+			
		" else contareceberestadopessoa.sigla end)"					
		,TipoCampoEnum.TEXTO, 10),
	CONTA_RECEBER_CEP_SACADO("contareceber", "CEP Sacado", "(case contareceber.tipoPessoa "+
				" when '"+TipoSacado.FORNECEDOR.getValor()+"' then contareceberfornecedor.cep  "+
				" when '"+TipoPessoa.PARCEIRO.getValor()+"' then contareceberparceiro.cep "+
				" when '"+TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()+"' then contareceberresponsavelfinanceiro.cep "+
				" else contareceberpessoa.cep end)"					
				,TipoCampoEnum.TEXTO, 20),
     CONTA_RECEBER_TEL_UM_SACADO("contareceber", "Telefone 1 (Resid.) Sacado", "(case contareceber.tipoPessoa " 
				+ " when '" + TipoSacado.FORNECEDOR.getValor() + "' then contareceberfornecedor.telComercial1  " 
		        + " when '" + TipoPessoa.PARCEIRO.getValor() + "' then contareceberparceiro.telComercial1 " 
 				+ " when '" + TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor() + "' then contareceberresponsavelfinanceiro.telefoneres "
		        + " else contareceberpessoa.telefoneres end)", TipoCampoEnum.TEXTO, 20), 
    CONTA_RECEBER_TEL_DOIS_SACADO(
			"contareceber", "Telefone 2 (Recad) Sacado", "(case contareceber.tipoPessoa " 
		  + " when '" + TipoSacado.FORNECEDOR.getValor() + "' then contareceberfornecedor.telComercial2  "
		  + " when '" + TipoPessoa.PARCEIRO.getValor() + "' then contareceberparceiro.telComercial2 " 
          + " when '" + TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor() + "' then contareceberresponsavelfinanceiro.telefonerecado " 
		  + " else contareceberpessoa.telefonerecado end)", TipoCampoEnum.TEXTO, 20),
		  
	CONTA_RECEBER_TEL_TRES_SACADO(
					"contareceber", "Telefone 2 (Celular) Sacado", "(case contareceber.tipoPessoa " 
				  + " when '" + TipoSacado.FORNECEDOR.getValor() + "' then contareceberfornecedor.telComercial3  "
				  + " when '" + TipoPessoa.PARCEIRO.getValor() + "' then contareceberparceiro.telComercial2 " 
		          + " when '" + TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor() + "' then contareceberresponsavelfinanceiro.celular " 
				  + " else contareceberpessoa.celular end)", TipoCampoEnum.TEXTO, 20),
	CONTA_RECEBER_SITUACAO_SIGLA("contareceber", "Situação - Sigla", "(case when contareceber.situacao = 'RE' and CONTA_RECEBER_DATA_BASE != null "
			              + "and negociacaoRecebimento.data > CONTA_RECEBER_DATA_BASE then 'AR' else contareceber.situacao end)", TipoCampoEnum.TEXTO, 10),
	CONTA_RECEBER_SITUACAO_DESCRICAO("contareceber", "Situação - Descrição", "(case (case when contareceber.situacao = 'RE' and CONTA_RECEBER_DATA_BASE != null "
			              + "and negociacaoRecebimento.data > CONTA_RECEBER_DATA_BASE then 'AR' else contareceber.situacao end) "+
			              " when '"+SituacaoContaReceber.A_RECEBER.getValor()+"' then '"+SituacaoContaReceber.A_RECEBER.getDescricao()+"'"+
			  			" when '"+SituacaoContaReceber.NEGOCIADO.getValor()+"' then '"+SituacaoContaReceber.NEGOCIADO.getDescricao()+"'"+
			  			" when '"+SituacaoContaReceber.RECEBIDO.getValor()+"' then '"+SituacaoContaReceber.RECEBIDO.getDescricao()+"'"+
			  			" when '"+SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor()+"' then '"+SituacaoContaReceber.CANCELADO_FINANCEIRO.getDescricao()+"'"+
			  			" when '"+SituacaoContaReceber.REMOVER.getValor()+"' then '"+SituacaoContaReceber.REMOVER.getDescricao()+"' end) ",
			              TipoCampoEnum.TEXTO, 20),
	CONTA_RECEBER_TIPO_ORIGEM_SIGLA("contareceber", "Tipo Origem - Sigla",  "contareceber.tipoOrigem",TipoCampoEnum.TEXTO, 10),	
	CONTA_RECEBER_TIPO_ORIGEM_DESCRICAO("contareceber", "Tipo Origem - Descrição",  "(case contareceber.tipoOrigem "+
			" when '"+TipoOrigemContaReceber.BIBLIOTECA.getValor()+"' then '"+TipoOrigemContaReceber.BIBLIOTECA.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO.getValor()+"' then '"+TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.MATRICULA.getValor()+"' then '"+TipoOrigemContaReceber.MATRICULA.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.MATERIAL_DIDATICO.getValor()+"' then '"+TipoOrigemContaReceber.MATERIAL_DIDATICO.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.REQUERIMENTO.getValor()+"' then '"+TipoOrigemContaReceber.REQUERIMENTO.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.OUTROS.getValor()+"' then '"+TipoOrigemContaReceber.OUTROS.getDescricao()+"'"+			
			" when '"+TipoOrigemContaReceber.MENSALIDADE.getValor()+"' then '"+TipoOrigemContaReceber.MENSALIDADE.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getValor()+"' then '"+TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.NEGOCIACAO.getValor()+"' then '"+TipoOrigemContaReceber.NEGOCIACAO.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.BOLSA_CUSTEADA_CONVENIO.getValor()+"' then '"+TipoOrigemContaReceber.BOLSA_CUSTEADA_CONVENIO.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.CONTRATO_RECEITA.getValor()+"' then '"+TipoOrigemContaReceber.CONTRATO_RECEITA.getDescricao()+"'"+
			" when '"+TipoOrigemContaReceber.INCLUSAOREPOSICAO.getValor()+"' then '"+TipoOrigemContaReceber.INCLUSAOREPOSICAO.getDescricao()+"' end) ",
			TipoCampoEnum.TEXTO, 20),
	CONTA_RECEBER_TIPO_PESSOA_SIGLA("contareceber", "Tipo de Pessoa - Sigla",  "contareceber.tipoPessoa",TipoCampoEnum.TEXTO, 10),
			              
	CONTA_RECEBER_TIPO_PESSOA_DESCRICAO("contareceber", "Tipo de Pessoa - Descrição",  "(case contareceber.tipoPessoa "+
			" when '"+TipoSacado.FORNECEDOR.getValor()+"' then '"+TipoPessoa.FORNECEDOR.getDescricao()+"'"+
			" when '"+TipoPessoa.ALUNO.getValor()+"' then '"+TipoPessoa.ALUNO.getDescricao()+"'"+
			" when '"+TipoPessoa.CANDIDATO.getValor()+"' then '"+TipoPessoa.CANDIDATO.getDescricao()+"'"+
			" when '"+TipoPessoa.FUNCIONARIO.getValor()+"' then '"+TipoPessoa.FUNCIONARIO.getDescricao()+"'"+
			" when '"+TipoPessoa.PARCEIRO.getValor()+"' then '"+TipoPessoa.PARCEIRO.getDescricao()+"'"+			
			" when '"+TipoPessoa.PROFESSOR.getValor()+"' then '"+TipoPessoa.PROFESSOR.getDescricao()+"'"+
			" when '"+TipoPessoa.REQUERENTE.getValor()+"' then '"+TipoPessoa.REQUERENTE.getDescricao()+"'"+
			" when '"+TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor()+"' then '"+TipoPessoa.RESPONSAVEL_FINANCEIRO.getDescricao()+"' end) ",
			TipoCampoEnum.TEXTO, 20),	
	CONTA_RECEBER_VALOR_RECEBER("contareceber", "Valor", "contareceber.valor",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_VALOR_BASE_RECEBER("contareceber", "Valor Base", "contareceber.valorBaseContaReceber",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_VALOR_CUSTEADO_RECEBER("contareceber","Valor Custeado Convênio", "contareceber.valorCusteadoContaReceber",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_VALOR_CALCULADO("contareceber", "Valor Receber Calculado", "contareceber.valorReceberCalculado",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_VALOR_RECEBIDO("contareceber", "Valor Recebido", "(case when contareceber.situacao = 'RE' and CONTA_RECEBER_DATA_BASE != null "
			+ "and negociacaoRecebimento.data > CONTA_RECEBER_DATA_BASE then 0.0 else contareceber.valorRecebido end) ",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_VALOR_JURO("contareceber", "Juro", "(case when contareceber.situacao = 'RE' then contareceber.juro else contareceber.valorjurocalculado end)",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_VALOR_MULTA("contareceber", "Multa", "(case when contareceber.situacao = 'RE' then contareceber.multa else contareceber.valormultacalculado end)",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_VALOR_ACRESCIMO("contareceber", "Acréscimo", "contareceber.acrescimo",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_VALOR_INDICE_REAJUSTE_POR_ATRASO("contareceber", "Valor Índice Reajuste Por Atraso", "contareceber.valorIndiceReajustePorAtraso",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_VALOR_DESCONTO_ALUNO("contareceber", "Desconto do Aluno", "contareceber.valorDescontoAlunoJaCalculado",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_VALOR_DESCONTO_RECEBIMENTO("contareceber", "Desconto no Recebimento", "contareceber.valorCalculadoDescontoLancadoRecebimento",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_VALOR_DESCONTO_PROGRESSIVO("contareceber", "Desconto Progressivo", "contareceber.valorDescontoProgressivo",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_VALOR_DESCONTO_CONVENIO("contareceber","Desconto do Convênio", "contareceber.descontoconvenio",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_VALOR_DESCONTO_INSTITUICAO("contareceber","Desconto da Instituição", "contareceber.descontoinstituicao",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_VALOR_TOTAL_DESCONTO_RECEBIDO("contareceber","Total Desconto Recebido", "(case when contareceber.situacao = 'RE' then contareceber.valorDescontoRecebido else contareceber.valordescontocalculado end)",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_VALOR_DESCONTO_RATEIO("contareceber","Desconto Rateio", "contareceber.valordescontorateio",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_DATA_BASE("contareceber", "Data Base Faturamento", "CONTA_RECEBER_DATA_BASE", TipoCampoEnum.DATA, 20),
	CONTA_RECEBER_VALOR_BOLSA_FILANTROPIA("contareceber", "Valor do Desconto de Bolsa Filantrópica", "(select sum(planodescontocontareceber.valorutilizadorecebimento) from planodescontocontareceber inner join planodesconto on planodesconto.codigo = planodescontocontareceber.planodesconto where contareceber.codigo = planodescontocontareceber.contareceber and planodesconto.bolsaFilantropia)",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_VALOR_BOLSA_NAO_FILANTROPICA("contareceber", "Valor do Desconto de Bolsa Não Filantrópica", "(select sum(planodescontocontareceber.valorutilizadorecebimento) from planodescontocontareceber inner join planodesconto on planodesconto.codigo = planodescontocontareceber.planodesconto where contareceber.codigo = planodescontocontareceber.contareceber and planodesconto.bolsaFilantropia = false)",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_POSSUI_BOLSA_FILANTROPICA("contareceber", "Conta a Receber Possui Bolsa Filantrópica", "(case when (select sum(planodescontocontareceber.valorutilizadorecebimento) from planodescontocontareceber inner join planodesconto on planodesconto.codigo = planodescontocontareceber.planodesconto where contareceber.codigo = planodescontocontareceber.contareceber and planodesconto.bolsaFilantropia limit 1) > 0 then true else false end)",TipoCampoEnum.BOOLEAN, 20),
	CONTA_RECEBER_FORMAS_DE_RECEBIMENTO("contareceberrecebimento", "Formas de Recebimento - Lista", "(array_to_string(array(select distinct formapagamento.nome from contareceberrecebimento inner join formapagamento on formapagamento.codigo = contareceberrecebimento.formapagamento where contareceberrecebimento.contareceber = contareceber.codigo and contareceberrecebimento.formapagamentonegociacaorecebimento is not null and contareceberrecebimento.tiporecebimento = 'CR'), ', '))",TipoCampoEnum.TEXTO, 20),
	CONTA_RECEBER_NOME_FORMA_RECEBIMENTO("formapagamento", "Forma Recebimento - Individual", "formapagamento.nome",TipoCampoEnum.TEXTO, 20),
	CONTA_RECEBER_DATA_COMPENSACAO_FORMA_RECEBIMENTO("contareceberrecebimento", "Data Compensação Forma Recebimento", "(case when formapagamento.tipo = 'CH' then case when cheque.pago then cheque.databaixa else cheque.dataPrevisao end "
			+"else case when formapagamento.tipo =  'CA' and formapagamentonegociacaorecebimentocartaocredito.codigo is not null then case when formapagamentonegociacaorecebimentocartaocredito.situacao = 'RE' and formapagamentonegociacaorecebimentocartaocredito.datarecebimento is not null then formapagamentonegociacaorecebimentocartaocredito.datarecebimento else formapagamentonegociacaorecebimentocartaocredito.datavencimento	end "
			+"else case when formapagamento.tipo =  'CD' then (case when formapagamentonegociacaorecebimento.datacredito is null then negociacaorecebimento.data else formapagamentonegociacaorecebimento.datacredito end ) "
            +"else case when formapagamento.tipo =  'BO' then (case when formapagamentonegociacaorecebimento.datacredito is null then negociacaorecebimento.data else formapagamentonegociacaorecebimento.datacredito end ) else (case when formapagamentonegociacaorecebimento.datacredito is null then negociacaorecebimento.data else formapagamentonegociacaorecebimento.datacredito end ) end end end end)", TipoCampoEnum.DATA, 20),
	CONTA_RECEBER_VALOR_UTILIZADO_FORMA_RECEBIMENTO("contareceberrecebimento", "Valor Utilizado Forma Recebimento", "contareceberrecebimento.valorrecebimento",TipoCampoEnum.DOUBLE, 20),
	CONTA_RECEBER_PLANO_DESCONTO("planodescontocontareceber", "Descrição Desconto da Instituição", "(array_to_string(array(select distinct planodesconto.nome from planodescontocontareceber inner join planodesconto on planodesconto.codigo = planodescontocontareceber.planodesconto where planodescontocontareceber.contareceber = contareceber.codigo), ', '))",TipoCampoEnum.TEXTO, 20),
	CONTA_RECEBER_CONVENIO("planodescontocontareceber", "Descrição Convênio", "(array_to_string(array(select distinct convenio.descricao from planodescontocontareceber inner join convenio on convenio.codigo = planodescontocontareceber.convenio where planodescontocontareceber.contareceber = contareceber.codigo), ', '))",TipoCampoEnum.TEXTO, 20),
	CONTA_RECEBER_DATA_NEGOCIACAO("contareceber", "Data Negociação", "(case when contareceber.situacao = '"+SituacaoContaReceber.NEGOCIADO.getValor()+" "
	+" then (select negociacaocontareceber.data from contarecebernegociado inner join negociacaocontareceber on negociacaocontareceber.codigo = contarecebernegociado.negociacaocontareceber "
	+" where contareceber.codigo = contarecebernegociado.contareceber order by negociacaocontareceber.data desc limit 1 )"
	+" else case when contareceber.tipoorigem = 'NCR' then (select negociacaocontareceber.data from negociacaocontareceber where negociacaocontareceber.codigo::VARCHAR = contareceber.codorigem) else null end)",TipoCampoEnum.DATA, 20),	
	CONTA_RECEBER_CONTA_CORRENTE("contacorrente", "Conta Corrente", "(select case when cc.contacaixa then cc.numero else 'Banco: '||ba.nome||' AG: '||case when ag.numero is not null then ag.numero else '' end||'-'||ag.digito||' CC: '||cc.numero end "
			 +" from contacorrente as cc left join agencia as ag on ag.codigo = cc.agencia left join banco as ba on ag.banco = ba.codigo"
			 +" where cc.codigo = contareceber.contacorrente  )", TipoCampoEnum.TEXTO, 30),	
	CONTA_RECEBER_CHEQUE_DEVOLVIDO_NUMERO("cheque", "Nº Cheque Devolvido", "chequeDevolvido.numero", TipoCampoEnum.TEXTO, 20),
	CONTA_RECEBER_CHEQUE_DEVOLVIDO_BANCO("cheque", "Banco Cheque Devolvido", "chequeDevolvido.banco",TipoCampoEnum.TEXTO, 20),
	CONTA_RECEBER_CHEQUE_DEVOLVIDO_AGENCIA("cheque", "Agência Cheque Devolvido", "chequeDevolvido.agencia",TipoCampoEnum.TEXTO, 20),
	CONTA_RECEBER_CHEQUE_DEVOLVIDO_CONTA_CORRENTE("cheque", "Conta Corrente Chequ Devolvido", "chequeDevolvido.contaCorrente",TipoCampoEnum.TEXTO, 20),
	CONTA_RECEBER_CHEQUE_DEVOLVIDO_CPF_CNPJ("cheque", "CPF/CNPJ Cheque Devolvido", "(case when chequeDevolvido.emitentepessoajuridica is null or chequeDevolvido.emitentepessoajuridica = false then chequeDevolvido.cnpj else chequeDevolvido.cnpj end)",TipoCampoEnum.TEXTO, 20),
	CONTA_RECEBER_CHEQUE_DEVOLVIDO_EMITENTE("cheque", "Emitente Cheque Devolvido", "chequeDevolvido.sacado",TipoCampoEnum.TEXTO, 20),
 ;
	private TagSEIDecidirContaReceberEnum(String entidade, String atributo, String campo, TipoCampoEnum tipoCampo, Integer tamanhoCampo) {
		this.tamanhoCampo = tamanhoCampo;
		this.entidade = entidade;
		this.campo = campo;
		this.atributo = atributo;
		this.tipoCampo = tipoCampo;
	}

	private String entidade;
	private String atributo;
	private String campo;
	private Integer tamanhoCampo;
	private TipoCampoEnum tipoCampo;
	
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

	@Override
	public void setAtributo(String atributo) {
		this.atributo = atributo;
		
	}

	@Override
	public String getAtributo() {
		return atributo;
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
