package relatorio.negocio.jdbc.financeiro;

import java.io.File;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.negocio.comuns.financeiro.AdiantamentoRelVO;
import relatorio.negocio.interfaces.financeiro.AdiantamentoRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings({"serial","deprecation"})
@Scope("singleton")
@Repository
@Lazy
public class AdiantamentoRel extends SuperRelatorio implements AdiantamentoRelInterfaceFacade {

	private static String idEntidade;

	public AdiantamentoRel() {
		setIdEntidade("AdiantamentoRel");
	}

	@Override
	public List<AdiantamentoRelVO> criarObjeto(List<UnidadeEnsinoVO> listaUnidadeEnsino, Date dataInicio, Date dataFim, String tipoSacado, String situacaoContaPagar, String situacaoAdiantamento, UsuarioVO usuario) throws Exception {		
		StringBuilder sqlStr = new StringBuilder("");
		sqlStr.append("SELECT contapagarutilizada.codigo as codigoContaPagarUtilizada, ");
		sqlStr.append("contapagarutilizada.tiposacado, ");
		sqlStr.append("fornecedor.nome as fornecedor, "); 
		sqlStr.append("parceiro.nome as parceiro, "); 
		sqlStr.append("pessoafuncionario.nome as funcionario, "); 
		sqlStr.append("unidadeensino.nome as unidadeEnsino, ");
		sqlStr.append("contapagarutilizada.datavencimento as dataLancamento, ");
		sqlStr.append("negociacaopagamento.data as dataPagamento, ");
		sqlStr.append("contapagarutilizada.situacao as situacaoContaPagarUtilizada,  ");
		sqlStr.append("contapagar.codigo as codigoContaPagar, ");
		sqlStr.append("contapagar.nrdocumento, ");
		sqlStr.append("contapagar.numeronotafiscalentrada, ");
		sqlStr.append("contapagar.valor as valorDocumento, ");
		sqlStr.append("contapagaradiantamento.valorUtilizado, ");
		sqlStr.append("contapagarutilizada.valor as valorTotal, ");
		sqlStr.append("contapagarutilizada.valorutilizadoadiantamento ");		
		sqlStr.append("FROM contapagar as contapagarutilizada ");
		sqlStr.append("LEFT JOIN contapagaradiantamento on contapagarutilizada.codigo=contapagaradiantamento.contapagarutilizada  ");
		sqlStr.append("LEFT JOIN contapagar on contapagaradiantamento.contapagar=contapagar.codigo  ");
		sqlStr.append("INNER JOIN unidadeensino on contapagarutilizada.unidadeensino=unidadeensino.codigo  ");
		sqlStr.append("LEFT JOIN contapagarnegociacaopagamento on contapagarutilizada.codigo=contapagarnegociacaopagamento.contapagar ");
		sqlStr.append("LEFT JOIN negociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar=negociacaopagamento.codigo ");
		sqlStr.append("LEFT JOIN fornecedor on fornecedor.codigo = contapagarutilizada.fornecedor ");
		sqlStr.append("LEFT JOIN funcionario on funcionario.codigo = contapagarutilizada.funcionario  ");
		sqlStr.append("LEFT JOIN parceiro on parceiro.codigo = contapagarutilizada.parceiro  ");
		sqlStr.append("LEFT JOIN pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
		sqlStr.append("WHERE contapagarutilizada.tipoorigem = 'AD' ");

		if (!listaUnidadeEnsino.isEmpty()) {
			boolean virgula = false;
			sqlStr.append("AND unidadeensino.codigo IN(");
			for (UnidadeEnsinoVO unidadeEnsinoVO : listaUnidadeEnsino) {
				if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
					if (!virgula) {
						sqlStr.append(unidadeEnsinoVO.getCodigo());
					} else {
						sqlStr.append(", ").append(unidadeEnsinoVO.getCodigo());
					}
					virgula = true;
				}
			}
			sqlStr.append(") ");
		}

		if (dataInicio != null) {
			sqlStr.append("AND contapagarutilizada.datavencimento BETWEEN '").append(Uteis.getDataBD0000(dataInicio)).append("' AND '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		} else {
			sqlStr.append("AND contapagarutilizada.datavencimento < '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		}
		
		if(tipoSacado.equals("todos")) {
			sqlStr.append("AND contapagarutilizada.tiposacado in ('FO', 'FU', 'PA') ");
		}
		else {
			sqlStr.append("AND contapagarutilizada.tiposacado = '").append(tipoSacado).append("' ");
		}
		
		if(situacaoContaPagar.equals("todas")) {
			sqlStr.append("AND contapagarutilizada.situacao in ('AP', 'PA', 'NE') ");
		}
		else {
			sqlStr.append("AND contapagarutilizada.situacao = '").append(situacaoContaPagar).append("' ");
		}
		
		if(situacaoAdiantamento.equals("NU")) {
			sqlStr.append("AND contapagarutilizada.valorutilizadoadiantamento = 0 ");
		}
		else if(situacaoAdiantamento.equals("PU")) {
			sqlStr.append("AND contapagarutilizada.valorutilizadoadiantamento > 0  AND contapagarutilizada.valorutilizadoadiantamento < contapagarutilizada.valor ");
		}
		else if(situacaoAdiantamento.equals("UT")) {
			sqlStr.append("AND contapagarutilizada.valorutilizadoadiantamento = contapagarutilizada.valor ");
		}

		sqlStr.append("ORDER BY contapagarutilizada.codigo");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return montarDadosConsulta(tabelaResultado);

	}

	@Override
	public List<AdiantamentoRelVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
		List<AdiantamentoRelVO> vetResultado = new ArrayList<AdiantamentoRelVO>(0);
		while (tabelaResultado.next()) {
			AdiantamentoRelVO adiantamentoRelVO = montarDados(tabelaResultado);
			vetResultado.add(adiantamentoRelVO);
		}
		return vetResultado;
	}

	@Override
	public AdiantamentoRelVO montarDados(SqlRowSet dadosSQL) throws Exception {
		AdiantamentoRelVO obj = new AdiantamentoRelVO();
		obj.setCodigoContaPagarUtilizada(dadosSQL.getInt("codigoContaPagarUtilizada"));
		obj.setCodigoContaPagar(dadosSQL.getInt("codigoContaPagar"));
		obj.setTipoSacado(dadosSQL.getString("tiposacado"));
		if(dadosSQL.getString("tiposacado").equals("FO")) {
			obj.setNomeSacado(dadosSQL.getString("fornecedor"));
		}
		else if(dadosSQL.getString("tiposacado").equals("FU")) {
				obj.setNomeSacado(dadosSQL.getString("funcionario"));
		}
		else if(dadosSQL.getString("tiposacado").equals("PA")) {
			obj.setNomeSacado(dadosSQL.getString("parceiro"));
		}		
		obj.setUnidadeEnsino(dadosSQL.getString("unidadeEnsino"));
		obj.setDataLancamento(dadosSQL.getDate("dataLancamento"));
		obj.setDataPagamento(dadosSQL.getDate("dataPagamento"));
		obj.setSituacaoContaPagar(dadosSQL.getString("situacaoContaPagarUtilizada"));
		if(dadosSQL.getDouble("valorutilizadoadiantamento") == 0) {
			obj.setSituacaoAdiantamento("Não Utilizado");
		}
		else if(dadosSQL.getDouble("valorutilizadoadiantamento") > 0 && dadosSQL.getDouble("valorutilizadoadiantamento") < dadosSQL.getDouble("valorTotal")) {
			obj.setSituacaoAdiantamento("Parcialmente Utilizado");
		}
		else if(dadosSQL.getDouble("valorutilizadoadiantamento") == dadosSQL.getDouble("valorTotal")) {
			obj.setSituacaoAdiantamento("Utilizado");
		}
		obj.setNumeroDocumento(dadosSQL.getString("nrdocumento"));
		obj.setNumeroNotaFiscalEntrada(dadosSQL.getString("numeronotafiscalentrada"));
		obj.setValorDocumento(dadosSQL.getDouble("valorDocumento"));
		obj.setValorUtilizado(dadosSQL.getDouble("valorutilizado"));
		obj.setValorTotal(dadosSQL.getDouble("valorTotal"));
		obj.setValorTotalUtilizado(dadosSQL.getDouble("valorutilizadoadiantamento"));
		obj.setValorSaldo(dadosSQL.getDouble("valorTotal") - dadosSQL.getDouble("valorutilizadoadiantamento"));

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
	public String designIReportRelatorioExcel() {
			return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator + getIdEntidade() + ".jrxml");
	}


	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
	}

	@Override
	public void validarDados(Date dataInicio, Date dataFim) throws Exception {

		if(dataInicio == null) {
			throw new Exception("A Data Inicial deve ser informada para a geração do relatório.");
		}
		else if (dataFim == null) {
			throw new Exception("A Data Final deve ser informada para a geração do relatório.");
		}else { 

			ZoneId defaultZoneId = ZoneId.systemDefault();				

			LocalDate ldDataInicio = dataInicio.toInstant().atZone(defaultZoneId).toLocalDate();
			LocalDate ldDataFim = dataFim.toInstant().atZone(defaultZoneId).toLocalDate();

			Period periodo = Period.between(ldDataInicio, ldDataFim);

			if((periodo.getYears() > 1) || (periodo.getYears() == 1 && (periodo.getMonths() > 0 || periodo.getDays() > 0))) {
				throw new Exception("O intervalo deve ser menor que um ano para a geração do relatório.");
			}

			if (ldDataInicio.isAfter(ldDataFim)) {
				throw new Exception("A Data Inicial não pode ser maior que a data Final.");
			}
		}
	}

	public static void setIdEntidade(String idEntidade) {
		AdiantamentoRel.idEntidade = idEntidade;
	}

	public static String getIdEntidade() {
		return idEntidade;
	}

}
