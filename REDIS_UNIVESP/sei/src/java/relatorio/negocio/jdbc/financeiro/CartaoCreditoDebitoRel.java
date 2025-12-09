package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import relatorio.negocio.comuns.financeiro.CartaoCreditoDebitoRelVO;
import relatorio.negocio.interfaces.financeiro.CartaoCreditoDebitoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@Repository
@Scope("singleton")
@Lazy
public class CartaoCreditoDebitoRel extends SuperRelatorio implements CartaoCreditoDebitoRelInterfaceFacade {

	private static String idEntidade;

	public CartaoCreditoDebitoRel() {
		setIdEntidade("CartaoCreditoDebitoRel");
	}

	public List<CartaoCreditoDebitoRelVO> gerarListaSintetico(List<UnidadeEnsinoVO> listaUnidadeEnsino, List<OperadoraCartaoVO> listaOperadoraCartao, List<CursoVO> listaCurso, Date dataInicio, Date dataFim, String filtrarPor, String situacaoBaixa, MatriculaVO matricula, PessoaVO responsavelFinanceiro, Integer fornecedor, Integer candidato, Integer parceiro, String tipoSacado) throws Exception {
		StringBuffer sqlStr = new StringBuffer(" select t.nome as \"operadoraCartao.nome\", t.codigo as \"operadoraCartao.codigo\", t.datavencimento as \"formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento\",  ");
		sqlStr.append(" sum(t.valorCompensado) as valorCompensado, sum(t.valorCompensar) as valorCompensar, sum(t.valorDebito) as valorDebito, ");
		sqlStr.append(" t.unidadeensino, t.nomeUnidadeEnsino as \"unidadeensino.nome\" from ( ");
		sqlStr.append(" SELECT operadoraCartao.nome, operadoraCartao.codigo , operadoraCartao.tipo,operadoraCartao.operadoracartaocredito as \"operadoraCartao.operadoracartaocredito\",  ");
		sqlStr.append(" case when operadoraCartao.tipo = 'CARTAO_DEBITO' then formaPagamentoNegociacaoRecebimento.datacredito else formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento end as datavencimento, formaPgtoNegociacaoRecebimentoCartaoCredito.situacao as \"formaPgtoNegociacaoRecebimentoCartaoCredito.situacao\",  ");
		sqlStr.append(" case when formaPgtoNegociacaoRecebimentoCartaoCredito.situacao = 'RE' and operadoraCartao.tipo = 'CARTAO_CREDITO' then formaPgtoNegociacaoRecebimentoCartaoCredito.valorParcela else 0 end as valorCompensado, ");
		sqlStr.append(" case when formaPgtoNegociacaoRecebimentoCartaoCredito.situacao = 'AR' and operadoraCartao.tipo = 'CARTAO_CREDITO' then formaPgtoNegociacaoRecebimentoCartaoCredito.valorParcela else 0 end as valorCompensar, ");
		sqlStr.append(" case when operadoraCartao.tipo = 'CARTAO_DEBITO' then formaPagamentoNegociacaoRecebimento.valorRecebimento else 0 end as valorDebito, ");
		sqlStr.append(" negociacaoRecebimento.unidadeensino, unidadeensino.nome as nomeUnidadeEnsino ");
		sqlStr.append(" FROM formaPagamentoNegociacaoRecebimento ");
		/**
		 * Este join deve ser matido para atender o modelo antigo do
		 * relacionamento onde a forma de pagamento negociacao recebimento tinha
		 * uma lista de FormaPagamentoNegociacaoRecebimentoCartaoCredito hoje
		 * este relacionamento foi alterado 1 para 1
		 */
		sqlStr.append(" LEFT JOIN formaPagamentoNegociacaoRecebimentoCartaoCredito as formaPgtoNegociacaoRecebimentoCartaoCredito ON (formaPagamentoNegociacaoRecebimento.formaPagamentoNegociacaoRecebimentoCartaoCredito = formaPgtoNegociacaoRecebimentoCartaoCredito.codigo ");
		sqlStr.append(" or formaPagamentoNegociacaoRecebimento.codigo = formaPgtoNegociacaoRecebimentoCartaoCredito.formaPagamentoNegociacaoRecebimento) ");
		sqlStr.append(" LEFT JOIN categoriaDespesa ON categoriaDespesa.codigo = formaPagamentoNegociacaoRecebimento.categoriaDespesa ");
		sqlStr.append(" INNER JOIN contaCorrente ON contaCorrente.codigo = formaPagamentoNegociacaoRecebimento.contaCorrente ");
		sqlStr.append(" INNER JOIN operadoraCartao ON operadoraCartao.codigo = formaPagamentoNegociacaoRecebimento.operadoraCartao ");
		sqlStr.append(" INNER JOIN negociacaoRecebimento ON negociacaoRecebimento.codigo = formaPagamentoNegociacaoRecebimento.negociacaoRecebimento ");
		sqlStr.append(" LEFT JOIN unidadeEnsino ON negociacaorecebimento.unidadeEnsino = unidadeEnsino.codigo ");
		String filtro = "WHERE";
		if (!situacaoBaixa.equals("")) {
			String sit = "";
			if (situacaoBaixa.equals("CO")) {
				sit = "RE";
			} else {
				sit = "AR";
			}
			sqlStr.append(filtro).append(" case when operadoraCartao.tipo = 'CARTAO_CREDITO' then formaPgtoNegociacaoRecebimentoCartaoCredito.situacao = '").append(sit).append("' else formaPgtoNegociacaoRecebimentoCartaoCredito.situacao is null end ");
			filtro = "AND";
		}
		if (!filtrarPor.equals("")) {
			if (filtrarPor.equals("DR")) {
				if (dataInicio != null) {
					sqlStr.append(filtro).append(" negociacaorecebimento.data >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataInicio, 0, 0, 0))).append("' ");
					filtro = "AND";
				}
				if (dataFim != null) {
					sqlStr.append(filtro).append(" negociacaorecebimento.data <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraFinalDia(dataFim))).append("' ");
					filtro = "AND";
				}
			} else {
				if (dataInicio != null) {
					sqlStr.append(filtro).append(" case when operadoraCartao.tipo = 'CARTAO_CREDITO' then  formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataInicio, 0, 0, 0))).append("' else formaPagamentoNegociacaoRecebimento.datacredito >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataInicio, 0, 0, 0))).append("' end ");
					filtro = "AND";
				}
				if (dataFim != null) {
					sqlStr.append(filtro).append(" case when operadoraCartao.tipo = 'CARTAO_CREDITO' then  formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraFinalDia(dataFim))).append("' else formaPagamentoNegociacaoRecebimento.datacredito <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraFinalDia(dataFim))).append("' end ");
					filtro = "AND";
				}
			}
		}
		if (!listaUnidadeEnsino.isEmpty()) {
			sqlStr.append(filtro).append(" negociacaoRecebimento.unidadeEnsino in (");
			for (UnidadeEnsinoVO ue : listaUnidadeEnsino) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) ");
		}		
		if (!listaOperadoraCartao.isEmpty()) {
			sqlStr.append(filtro).append(" operadoracartao.codigo in (");
			for (OperadoraCartaoVO ue : listaOperadoraCartao) {
				if (ue.getFiltrarOperadoraCartaoVO()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) ");
		}		
		if (!listaCurso.isEmpty()) {
			sqlStr.append(filtro).append(" exists (");
			sqlStr.append(" select distinct ng.codigo from negociacaorecebimento ng ");
			sqlStr.append(" LEFT JOIN contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = ng.codigo ");
			sqlStr.append(" LEFT JOIN contareceber on contareceber.codigo = contarecebernegociacaorecebimento.contareceber ");
			sqlStr.append(" LEFT JOIN matricula on matricula.aluno = contareceber.pessoa ");
			sqlStr.append(" where matricula.curso in ( ");
			for (CursoVO ue : listaCurso) {
				if (ue.getFiltrarCursoVO()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) and ng.codigo = negociacaorecebimento.codigo ) ");
		}		
		sqlStr.append(" order by formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento ");
		sqlStr.append(" ) as t group by t.nome, t.codigo, t.dataVencimento, t.unidadeEnsino, t.nomeUnidadeEnsino order by t.nomeUnidadeEnsino, t.datavencimento ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsultaSintetico(tabelaResultado);
	}

	public List<CartaoCreditoDebitoRelVO> gerarListaAnalitico(List<UnidadeEnsinoVO> listaUnidadeEnsino, List<OperadoraCartaoVO> listaOperadoraCartao, List<CursoVO> listaCurso, Date dataInicio, Date dataFim, String filtrarPor, String situacaoBaixa, MatriculaVO matricula, PessoaVO responsavelFinanceiro, Integer fornecedor, Integer candidato, Integer parceiro, String tipoSacado) throws Exception {
		StringBuffer sqlStr = new StringBuffer("SELECT operadoraCartao.nome as \"operadoraCartao.nome\", operadoraCartao.codigo as \"operadoraCartao.codigo\", operadoraCartao.tipo as \"operadoraCartao.tipo\",matricula.matricula as \"matricula.matricula\", ");
		sqlStr.append(" operadoraCartao.operadoracartaocredito as \"operadoraCartao.operadoracartaocredito\", negociacaorecebimento.tipoPessoa as \"negociacaorecebimento.tipoPessoa\", ");
		sqlStr.append(" case when negociacaorecebimento.tipoPessoa = 'PA' then parceiro.nome else case when negociacaorecebimento.tipoPessoa = 'FO' then fornecedor.nome else pessoa.nome end end as \"pessoa.nome\", ");
		sqlStr.append(" formaPagamentoNegociacaoRecebimento.taxaOperadora as \"formaPagamentoNegociacaoRecebimento.taxaOperadora\", formaPagamentoNegociacaoRecebimento.taxaAntecipacao as \"formaPagamentoNegociacaoRecebimento.taxaAntecipacao\", ");
		sqlStr.append(" formaPgtoNegociacaoRecebimentoCartaoCredito.codigo as \"formaPgtoNegociacaoRecebimentoCartaoCredito.codigo\", ");
		sqlStr.append(" formaPgtoNegociacaoRecebimentoCartaoCredito.numeroParcela as \"formaPgtoNegociacaoRecebimentoCartaoCredito.numeroParcela\", ");
		sqlStr.append(" substring(formaPgtoNegociacaoRecebimentoCartaoCredito.numerocartao from (char_length(formaPgtoNegociacaoRecebimentoCartaoCredito.numerocartao)-3)) as \"formaPgtoNegociacaoRecebimentoCartaoCredito.numerocartao\", ");
		sqlStr.append(" case when operadoraCartao.tipo = 'CARTAO_DEBITO' then formapagamentonegociacaorecebimento.valorrecebimento else formaPgtoNegociacaoRecebimentoCartaoCredito.valorParcela end as \"formaPgtoNegociacaoRecebimentoCartaoCredito.valorParcela\", ");
		sqlStr.append(" case when operadoraCartao.tipo = 'CARTAO_DEBITO' then formapagamentonegociacaorecebimento.datacredito else formaPgtoNegociacaoRecebimentoCartaoCredito.dataEmissao end as \"formaPgtoNegociacaoRecebimentoCartaoCredito.dataEmissao\", ");
		sqlStr.append(" case when operadoraCartao.tipo = 'CARTAO_DEBITO' then formapagamentonegociacaorecebimento.datacredito else formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento end as \"formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento\", ");
		sqlStr.append(" case when operadoraCartao.tipo = 'CARTAO_DEBITO' then negociacaoRecebimento.data  else formaPgtoNegociacaoRecebimentoCartaoCredito.dataRecebimento end as \"formaPgtoNegociacaoRecebimentoCartaoCredito.dataRecebimento\", ");
		sqlStr.append(" case when operadoraCartao.tipo = 'CARTAO_DEBITO' then 'RE' else formaPgtoNegociacaoRecebimentoCartaoCredito.situacao end as \"formaPgtoNegociacaoRecebimentoCartaoCredito.situacao\", ");
		sqlStr.append(" formaPagamentoNegociacaoRecebimento.codigo as \"formaPagamentoNegociacaoRecebimento.codigo\", ");
		sqlStr.append(" usuario.codigo as \"usuario.codigo\", usuario.nome as \"usuario.nome\", ");
		sqlStr.append(" bancoCC.codigo as \"bancoCC.codigo\", bancoCC.nrBanco as \"bancoCC.nrBanco\", bancoCC.nome as \"bancoCC.nome\", ");
		sqlStr.append(" contaCorrente.codigo as \"contaCorrente.codigo\", ");
		sqlStr.append(" bancoCCOC.codigo as \"bancoCCOC.codigo\", bancoCCOC.nrBanco as \"bancoCCOC.nrBanco\", bancoCCOC.nome as \"bancoCCOC.nome\", ");
		sqlStr.append(" contaCorrenteOperadoraCartao.codigo as \"contaCorrenteOperadoraCartao.codigo\", ");
		sqlStr.append(" categoriaDespesa.codigo as \"categoriaDespesa.codigo\", ");
		sqlStr.append(" negociacaoRecebimento.unidadeensino, unidadeensino.nome as \"unidadeensino.nome\", formaPgtoNegociacaoRecebimentoCartaoCredito.numerorecibotransacao ");
		sqlStr.append(" FROM formaPagamentoNegociacaoRecebimento ");
		/**
		 * Este join deve ser matido para atender o modelo antigo do
		 * relacionamento onde a forma de pagamento negociacao recebimento tinha
		 * uma lista de FormaPagamentoNegociacaoRecebimentoCartaoCredito hoje
		 * este relacionamento foi alterado 1 para 1
		 */
		sqlStr.append(" LEFT JOIN formaPagamentoNegociacaoRecebimentoCartaoCredito as formaPgtoNegociacaoRecebimentoCartaoCredito ON (formaPagamentoNegociacaoRecebimento.formaPagamentoNegociacaoRecebimentoCartaoCredito = formaPgtoNegociacaoRecebimentoCartaoCredito.codigo    ");
		sqlStr.append(" or formaPagamentoNegociacaoRecebimento.codigo = formaPgtoNegociacaoRecebimentoCartaoCredito.formaPagamentoNegociacaoRecebimento)   ");
		sqlStr.append(" LEFT JOIN categoriaDespesa ON categoriaDespesa.codigo = formaPagamentoNegociacaoRecebimento.categoriaDespesa ");
		sqlStr.append(" INNER JOIN contaCorrente ON contaCorrente.codigo = formaPagamentoNegociacaoRecebimento.contaCorrente ");
		sqlStr.append(" INNER JOIN operadoraCartao ON operadoraCartao.codigo = formaPagamentoNegociacaoRecebimento.operadoraCartao ");
		sqlStr.append(" INNER JOIN negociacaoRecebimento ON negociacaoRecebimento.codigo = formaPagamentoNegociacaoRecebimento.negociacaoRecebimento ");
		sqlStr.append(" LEFT JOIN agencia as agenciaCC ON agenciaCC.codigo = contaCorrente.agencia ");
		sqlStr.append(" LEFT JOIN banco as bancoCC ON bancoCC.codigo = agenciaCC.banco ");
		sqlStr.append(" LEFT JOIN contaCorrente as contaCorrenteOperadoraCartao ON contaCorrenteOperadoraCartao.codigo = formaPagamentoNegociacaoRecebimento.contaCorrenteOperadoraCartao ");
		sqlStr.append(" LEFT JOIN agencia as agenciaCCOC ON agenciaCCOC.codigo = contaCorrenteOperadoraCartao.agencia ");
		sqlStr.append(" LEFT JOIN banco as bancoCCOC ON bancoCCOC.codigo = agenciaCCOC.banco ");
		sqlStr.append(" LEFT JOIN matricula ON matricula.matricula = negociacaoRecebimento.matricula ");
		sqlStr.append(" LEFT JOIN pessoa ON pessoa.codigo = negociacaoRecebimento.pessoa ");
		sqlStr.append(" LEFT JOIN usuario ON usuario.codigo = formaPgtoNegociacaoRecebimentoCartaoCredito.responsavelPelaBaixa ");
		sqlStr.append(" LEFT JOIN parceiro ON negociacaorecebimento.parceiro = parceiro.codigo ");
		sqlStr.append(" LEFT JOIN fornecedor ON negociacaorecebimento.fornecedor = fornecedor.codigo ");
		sqlStr.append(" LEFT JOIN unidadeEnsino ON negociacaorecebimento.unidadeEnsino = unidadeEnsino.codigo ");
		String filtro = "WHERE";
		if (!situacaoBaixa.equals("")) {
			String sit = "";
			if (situacaoBaixa.equals("CO")) {
				sit = "RE";
			} else {
				sit = "AR";
			}
			sqlStr.append(filtro).append(" case when operadoraCartao.tipo = 'CARTAO_CREDITO' then formaPgtoNegociacaoRecebimentoCartaoCredito.situacao = '").append(sit).append("' else formaPgtoNegociacaoRecebimentoCartaoCredito.situacao is null end ");
			filtro = "AND";
		}
		if (!filtrarPor.equals("")) {
			if (filtrarPor.equals("DR")) {
				if (dataInicio != null) {
					sqlStr.append(filtro).append(" negociacaorecebimento.data >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataInicio, 0, 0, 0))).append("' ");
					filtro = "AND";
				}
				if (dataFim != null) {
					sqlStr.append(filtro).append(" negociacaorecebimento.data <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraFinalDia(dataFim))).append("' ");
					filtro = "AND";
				}
			} else {
				if (dataInicio != null) {
					sqlStr.append(filtro).append(" case when operadoraCartao.tipo = 'CARTAO_CREDITO' then  formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataInicio, 0, 0, 0))).append("' else formaPagamentoNegociacaoRecebimento.datacredito >= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateTime(dataInicio, 0, 0, 0))).append("' end ");
					filtro = "AND";
				}
				if (dataFim != null) {
					sqlStr.append(filtro).append(" case when operadoraCartao.tipo = 'CARTAO_CREDITO' then  formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraFinalDia(dataFim))).append("' else formaPagamentoNegociacaoRecebimento.datacredito <= '").append(Uteis.getDataJDBCTimestamp(Uteis.getDateHoraFinalDia(dataFim))).append("' end ");
					filtro = "AND";
				}
			}
		}
		if (!listaUnidadeEnsino.isEmpty()) {
			sqlStr.append(filtro).append(" negociacaoRecebimento.unidadeEnsino in (");
			for (UnidadeEnsinoVO ue : listaUnidadeEnsino) {
				if (ue.getFiltrarUnidadeEnsino()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) ");
		}		
		if (!listaOperadoraCartao.isEmpty()) {
			sqlStr.append(filtro).append(" operadoracartao.codigo in (");
			for (OperadoraCartaoVO ue : listaOperadoraCartao) {
				if (ue.getFiltrarOperadoraCartaoVO()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) ");
		}		
		if (!listaCurso.isEmpty()) {
			sqlStr.append(filtro).append(" exists (");
			sqlStr.append(" select distinct ng.codigo from negociacaorecebimento ng ");
			sqlStr.append(" LEFT JOIN contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = ng.codigo ");
			sqlStr.append(" LEFT JOIN contareceber on contareceber.codigo = contarecebernegociacaorecebimento.contareceber ");
			sqlStr.append(" LEFT JOIN matricula on matricula.aluno = contareceber.pessoa ");
			sqlStr.append(" where matricula.curso in ( ");
			for (CursoVO ue : listaCurso) {
				if (ue.getFiltrarCursoVO()) {
					sqlStr.append(ue.getCodigo() + ", ");
				}
			}
			sqlStr.append("0) and ng.codigo = negociacaorecebimento.codigo ) ");
		}		
		sqlStr.append(" order by unidadeensino.nome, formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento, \"pessoa.nome\", \"formaPgtoNegociacaoRecebimentoCartaoCredito.numeroParcela\" ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado);
	}

	public List<CartaoCreditoDebitoRelVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List<CartaoCreditoDebitoRelVO> vetResultado = new ArrayList<CartaoCreditoDebitoRelVO>(0);
		while (tabelaResultado.next()) {
			CartaoCreditoDebitoRelVO CartaoCreditoDebitoRelVO = montarDados(tabelaResultado);
			vetResultado.add(CartaoCreditoDebitoRelVO);
		}
		return vetResultado;
	}
	
	public List<CartaoCreditoDebitoRelVO> montarDadosConsultaSintetico(SqlRowSet tabelaResultado) throws Exception {
		List<CartaoCreditoDebitoRelVO> vetResultado = new ArrayList<CartaoCreditoDebitoRelVO>(0);
		while (tabelaResultado.next()) {
			CartaoCreditoDebitoRelVO CartaoCreditoDebitoRelVO = montarDadosSintetico(tabelaResultado);
			vetResultado.add(CartaoCreditoDebitoRelVO);
		}
		return vetResultado;
	}

	public CartaoCreditoDebitoRelVO montarDados(SqlRowSet dadosSQL) throws Exception {
		CartaoCreditoDebitoRelVO obj = new CartaoCreditoDebitoRelVO();
		obj.setUnidadeEnsino(dadosSQL.getString("unidadeensino.nome"));
		obj.setValor(dadosSQL.getDouble("formaPgtoNegociacaoRecebimentoCartaoCredito.valorParcela"));
		obj.setParcela(dadosSQL.getString("formaPgtoNegociacaoRecebimentoCartaoCredito.numeroParcela"));
		obj.setSituacao(dadosSQL.getString("formaPgtoNegociacaoRecebimentoCartaoCredito.situacao"));
		obj.setNumeroCartaoCredito(dadosSQL.getString("formaPgtoNegociacaoRecebimentoCartaoCredito.numerocartao"));
		obj.setDataPrevRecebimento(dadosSQL.getDate("formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento"));
		obj.setDataRecebimento(dadosSQL.getDate("formaPgtoNegociacaoRecebimentoCartaoCredito.dataRecebimento"));
		obj.setBandeira(dadosSQL.getString("operadoraCartao.nome"));
		obj.setTipoCartao(dadosSQL.getString("operadoraCartao.tipo"));
		obj.setSacado(dadosSQL.getString("pessoa.nome"));
		obj.setNumeroReciboTransacao(dadosSQL.getString("numerorecibotransacao"));
		//obj.setOperador(dadosSQL.getString("operadoraCartao.tipo"));
		return obj;
	}

	public CartaoCreditoDebitoRelVO montarDadosSintetico(SqlRowSet dadosSQL) throws Exception {
		CartaoCreditoDebitoRelVO obj = new CartaoCreditoDebitoRelVO();
		obj.setUnidadeEnsino(dadosSQL.getString("unidadeensino.nome"));
		obj.setCredCompensado(dadosSQL.getDouble("valorCompensado"));
		obj.setCredCompensar(dadosSQL.getDouble("valorCompensar"));
		obj.setDebito(dadosSQL.getDouble("valorDebito"));
		obj.setDataPrevRecebimento(dadosSQL.getDate("formaPgtoNegociacaoRecebimentoCartaoCredito.dataVencimento"));
		obj.setBandeira(dadosSQL.getString("operadoraCartao.nome"));
		return obj;
	}

	@Override
	public String caminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro");
	}

	@Override
	public String designIReportRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
	}

	@Override
	public void validarDados(List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataInicio, Date dataFim, String tipoRelatorio) throws Exception {
		if (listaUnidadeEnsino.isEmpty()) {
			throw new Exception(UteisJSF.internacionalizar("msg_CartaoCreditoDebito_unidadeEnsino"));
		}
		boolean existeUnidade = false;
		for(UnidadeEnsinoVO unidadeEnsino: listaUnidadeEnsino){
			if(unidadeEnsino.getFiltrarUnidadeEnsino()){
				existeUnidade = true;
				break;
			}
		}
		if(!existeUnidade){
			throw new Exception(UteisJSF.internacionalizar("msg_CartaoCreditoDebito_unidadeEnsino"));
		}
//		if (dataFim == null) {
//			throw new Exception("A Data Final deve ser informada para a geração do relatório.");
//		} else if (dataFim.after(new Date())) {
//			throw new Exception("A Data Final não pode ser maior que a data atual.");
//		}
	}

	public static String getDesignIReportRelatorioSintetico() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + "Sintetico" + ".jrxml");
	}

	public static String getDesignIReportRelatorioAnalitico() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + "Analitico" + ".jrxml");
	}
	
	public static void setIdEntidade(String idEntidade) {
		CartaoCreditoDebitoRel.idEntidade = idEntidade;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}
	
}
