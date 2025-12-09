/**
 * 
 */
package relatorio.negocio.jdbc.administrativo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.PatrimonioUnidadeVO;
import negocio.comuns.patrimonio.PatrimonioVO;
import negocio.comuns.patrimonio.TipoPatrimonioVO;
import negocio.comuns.patrimonio.enumeradores.SituacaoPatrimonioUnidadeEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.administrativo.PatrimonioRelVO;
import relatorio.negocio.interfaces.administrativo.PatrimonioRelInterfaceFacade;

/**
 * @author Leonardo Riciolle
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class PatrimonioRel extends SuperFacadeJDBC implements PatrimonioRelInterfaceFacade {

	private static final long serialVersionUID = 1L;

	@Override
	public List<PatrimonioRelVO> criarObjetoAnalitico(PatrimonioRelVO patrimonioRelVO, UnidadeEnsinoVO unidadeEnsinoVO, PatrimonioVO patrimonioVO, TipoPatrimonioVO tipoPatrimonioVO, LocalArmazenamentoVO localArmazenamentoVO, UsuarioVO usuarioVO, String ordenarPor) throws Exception {
		List<PatrimonioRelVO> listaPatrimonioRelVO = new ArrayList<PatrimonioRelVO>(0);
		SqlRowSet dadosSQL = executarConsultaParametrizadaAnalitico(patrimonioRelVO, unidadeEnsinoVO, patrimonioVO, tipoPatrimonioVO, localArmazenamentoVO, ordenarPor);
		PatrimonioRelVO patrimonioRelVO2 = montarDados(dadosSQL, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
		if(!patrimonioRelVO2.getPatrimonioVO().getPatrimonioUnidadeVOs().isEmpty()){
			listaPatrimonioRelVO.add(patrimonioRelVO2);
		}
		return listaPatrimonioRelVO;
	}

	@Override
	public void validarDados(PatrimonioRelVO patrimonioRelVO, UnidadeEnsinoVO unidadeEnsinoVO, TipoPatrimonioVO tipoPatrimonioVO, LocalArmazenamentoVO localArmazenamentoVO) throws Exception {
		if (unidadeEnsinoVO.getCodigo() == null || unidadeEnsinoVO.getCodigo().equals(0)) {
			throw new Exception("O campo UNIDADE ENSINO deve ser informado para geração do relatório.");
		}
//		if (tipoPatrimonioVO.getCodigo() == null || tipoPatrimonioVO.getCodigo().equals(0)) {
//			throw new Exception("O campo TIPO PATRIMONIO deve ser informado para geração do relatório.");
//		}
		if (patrimonioRelVO.getIncluirSubLocal()) {
			if (localArmazenamentoVO.getCodigo() == null || localArmazenamentoVO.getCodigo().equals(0)) {
				throw new Exception("O campo LOCAL ARMAZENAMNETO deve ser informado para geração do relatório.");
			}
		}
	}

	private SqlRowSet executarConsultaParametrizadaAnalitico(PatrimonioRelVO patrimonioRelVO, UnidadeEnsinoVO unidadeEnsinoVO, PatrimonioVO patrimonioVO, TipoPatrimonioVO tipoPatrimonioVO, LocalArmazenamentoVO localArmazenamentoVO, String ordenarPor) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT ");
		sqlStr.append(" patrimonio.descricao as \"descricaoPatrimonio\",");
		sqlStr.append(" tipopatrimonio.descricao as \"tipoPatrimonioDescricao\",  ");
		sqlStr.append(" localArmazenamento.localarmazenamento as \"localArmazenamento\",  ");
		sqlStr.append(" patrimoniounidade.situacao as \"situacao\",  ");
		sqlStr.append(" patrimoniounidade.codigo as \"patrimoniounidade.codigo\",  ");
		sqlStr.append(" patrimoniounidade.codigobarra as \"codigobarra\",  ");
		sqlStr.append(" patrimoniounidade.valorrecurso as \"valorrecurso\"  ");
		sqlStr.append(" FROM patrimonio ");
		sqlStr.append(" inner join patrimoniounidade on patrimoniounidade.patrimonio = patrimonio.codigo ");
		sqlStr.append(" inner join localArmazenamento on localArmazenamento.codigo = patrimoniounidade.localArmazenamento ");
		sqlStr.append(" inner join tipopatrimonio on tipopatrimonio.codigo = patrimonio.tipopatrimonio ");
		sqlStr.append(" where ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCodigo())) {
			sqlStr.append(" localArmazenamento.unidadeensino = ").append(unidadeEnsinoVO.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(patrimonioVO.getCodigo())) {
			sqlStr.append(" and patrimonio.codigo = ").append(patrimonioVO.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(tipoPatrimonioVO.getCodigo())) {
			sqlStr.append(" and tipopatrimonio.codigo = ").append(tipoPatrimonioVO.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(localArmazenamentoVO.getCodigo()) && !patrimonioRelVO.getIncluirSubLocal()) {
			sqlStr.append("and localArmazenamento.codigo = ").append(localArmazenamentoVO.getCodigo());
		}
		if (patrimonioRelVO.getIncluirSubLocal()) {
			sqlStr.append(" and localArmazenamento.codigo in ( ");
			sqlStr.append(" WITH RECURSIVE  local (codigo, localarmazenamento, unidadeensino, localarmazenamentosuperior) as ( ");
			sqlStr.append(" select codigo, localarmazenamento, unidadeensino, localarmazenamentosuperior from localarmazenamento ");
			sqlStr.append(" where localarmazenamento.codigo =  ").append(localArmazenamentoVO.getCodigo());
			sqlStr.append(" union ");
			sqlStr.append(" select localarmazenamento.codigo, localarmazenamento.localarmazenamento, localarmazenamento.unidadeensino, localarmazenamento.localarmazenamentosuperior from localarmazenamento ");
			sqlStr.append(" inner join local on localarmazenamento.localarmazenamentosuperior = local.codigo  ");
			sqlStr.append(" ) select codigo from local order by case when local.localarmazenamentosuperior is null then 0 else local.localarmazenamentosuperior end )");
		}
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoPatrimonioUnidade(patrimonioRelVO, "patrimoniounidade"));
		if (ordenarPor.equals("LO")) {
			sqlStr.append(" order by localArmazenamento.localarmazenamento ");
		} else if (ordenarPor.equals("PA")) {
			sqlStr.append(" order by patrimonio.descricao ");
		} else if (ordenarPor.equals("ST")) {
			sqlStr.append(" order by patrimoniounidade.situacao ");
		} else if (ordenarPor.equals("TP")) {
			sqlStr.append(" order by tipopatrimonio.descricao ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado;
	}

	@Override
	public List<PatrimonioRelVO> executarConsultaParametrizadaSintetico(final PatrimonioRelVO patrimonioRelVO, final UnidadeEnsinoVO unidadeEnsinoVO, final PatrimonioVO patrimonioVO, final TipoPatrimonioVO tipoPatrimonioVO, final LocalArmazenamentoVO localArmazenamentoVO, final UsuarioVO usuarioVO, final String ordenarPor) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select patrimonio.descricao as \"descricaoPatrimonio\", tipopatrimonio.descricao as \"tipoPatrimonioDescricao\", ");
		sqlStr.append(" sum(case when patrimoniounidade.situacao = 'DISPONIVEL' then 1 else 0 end) as qtdeEmUso, ");
		sqlStr.append(" sum(case when patrimoniounidade.situacao = 'EMPRESTADO' then 1 else 0 end) as qtdeEmprestado, ");
		sqlStr.append(" sum(case when patrimoniounidade.situacao = 'EM_MANUTENCAO' then 1 else 0 end) as qtdeManut, ");
		sqlStr.append(" sum(case when patrimoniounidade.situacao = 'SEPARADO_PARA_DESCARTE' then 1 else 0 end) as qtdeSepDesc, ");
		sqlStr.append(" sum(case when patrimoniounidade.situacao = 'DESCARTADO' then 1 else 0 end) as qtdeDesc, ");
		sqlStr.append(" patrimoniounidade.valorrecurso as \"valorRecurso\"  ");
		sqlStr.append(" from patrimonio  ");
		sqlStr.append(" inner join patrimoniounidade on patrimoniounidade.patrimonio = patrimonio.codigo  ");
		sqlStr.append(" inner join tipopatrimonio on tipopatrimonio.codigo = patrimonio.tipopatrimonio  ");
		sqlStr.append(" inner join localArmazenamento on localArmazenamento.codigo = patrimoniounidade.localArmazenamento  ");
		sqlStr.append(" where ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCodigo())) {
			sqlStr.append(" localArmazenamento.unidadeensino = ").append(unidadeEnsinoVO.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(patrimonioVO.getCodigo())) {
			sqlStr.append(" and patrimonio.codigo = ").append(patrimonioVO.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(tipoPatrimonioVO.getCodigo())) {
			sqlStr.append(" and tipopatrimonio.codigo = ").append(tipoPatrimonioVO.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(localArmazenamentoVO.getCodigo()) && !patrimonioRelVO.getIncluirSubLocal()) {
			sqlStr.append("and localArmazenamento.codigo = ").append(localArmazenamentoVO.getCodigo());
		}
		if (patrimonioRelVO.getIncluirSubLocal()) {
			sqlStr.append(" and localArmazenamento.codigo in ( ");
			sqlStr.append(" WITH RECURSIVE  local (codigo, localarmazenamento, unidadeensino, localarmazenamentosuperior) as ( ");
			sqlStr.append(" select codigo, localarmazenamento, unidadeensino, localarmazenamentosuperior from localarmazenamento ");
			sqlStr.append(" where localarmazenamento.codigo =  ").append(localArmazenamentoVO.getCodigo());
			sqlStr.append(" union ");
			sqlStr.append(" select localarmazenamento.codigo, localarmazenamento.localarmazenamento, localarmazenamento.unidadeensino, localarmazenamento.localarmazenamentosuperior from localarmazenamento ");
			sqlStr.append(" inner join local on localarmazenamento.localarmazenamentosuperior = local.codigo  ");
			sqlStr.append(" ) select codigo from local order by case when local.localarmazenamentosuperior is null then 0 else local.localarmazenamentosuperior end )");
		}
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoPatrimonioUnidade(patrimonioRelVO, "patrimoniounidade"));
		if (ordenarPor.equals("LO")) {
			sqlStr.append(" group by patrimonio.descricao, tipopatrimonio.descricao,patrimoniounidade.valorrecurso,localArmazenamento.localarmazenamento ");
		} else if (ordenarPor.equals("PA")) {
			sqlStr.append(" group by patrimonio.descricao, tipopatrimonio.descricao,patrimoniounidade.valorrecurso,patrimonio.descricao ");
		} else if (ordenarPor.equals("ST")) {
			sqlStr.append(" group by patrimonio.descricao, tipopatrimonio.descricao,patrimoniounidade.valorrecurso,patrimoniounidade.situacao ");
		} else if (ordenarPor.equals("TP")) {
			sqlStr.append(" group by patrimonio.descricao, tipopatrimonio.descricao,patrimoniounidade.valorrecurso,tipopatrimonio.descricao ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<PatrimonioRelVO> listaPatrimonioRelVO = new ArrayList<PatrimonioRelVO>(0);
		while (tabelaResultado.next()) {
			PatrimonioRelVO obj = new PatrimonioRelVO();
			obj.getPatrimonioVO().setDescricao(tabelaResultado.getString("descricaoPatrimonio"));
			obj.getPatrimonioVO().getTipoPatrimonioVO().setDescricao(tabelaResultado.getString("tipoPatrimonioDescricao"));
			obj.setQtdEmUso(tabelaResultado.getInt("qtdeEmUso"));
			obj.setQtdEmprestado(tabelaResultado.getInt("qtdeEmprestado"));
			obj.setQtdManutencao(tabelaResultado.getInt("qtdeManut"));
			obj.setQtdSeparadoParaDescarte(tabelaResultado.getInt("qtdeSepDesc"));
			obj.setQtdDescartado(tabelaResultado.getInt("qtdeDesc"));
			obj.setValorRecurso(tabelaResultado.getBigDecimal("valorRecurso"));
			listaPatrimonioRelVO.add(obj);
		}
		return listaPatrimonioRelVO;
	}

	@Override
	public List<PatrimonioRelVO> executarConsultaParametrizadaSinteticoLocalArmazenamento(final PatrimonioRelVO patrimonioRelVO, final UnidadeEnsinoVO unidadeEnsinoVO, final PatrimonioVO patrimonioVO, final TipoPatrimonioVO tipoPatrimonioVO, final LocalArmazenamentoVO localArmazenamentoVO, final UsuarioVO usuarioVO, final String ordenarPor) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("select localarmazenamento.localarmazenamento as \"localArmazenamento\",  ");
		sqlStr.append(" sum(case when patrimoniounidade.situacao = 'DISPONIVEL' then 1 else 0 end) as qtdeEmUso, ");
		sqlStr.append(" sum(case when patrimoniounidade.situacao = 'EMPRESTADO' then 1 else 0 end) as qtdeEmprestado, ");
		sqlStr.append(" sum(case when patrimoniounidade.situacao = 'EM_MANUTENCAO' then 1 else 0 end) as qtdeManut, ");
		sqlStr.append(" sum(case when patrimoniounidade.situacao = 'SEPARADO_PARA_DESCARTE' then 1 else 0 end) as qtdeSepDesc, ");
		sqlStr.append(" sum(case when patrimoniounidade.situacao = 'DESCARTADO' then 1 else 0 end) as qtdeDesc, ");
		sqlStr.append(" patrimoniounidade.valorrecurso as \"valorRecurso\"  ");
		sqlStr.append(" from patrimonio  ");
		sqlStr.append(" inner join patrimoniounidade on patrimoniounidade.patrimonio = patrimonio.codigo  ");
		sqlStr.append(" inner join tipopatrimonio on tipopatrimonio.codigo = patrimonio.tipopatrimonio  ");
		sqlStr.append(" inner join localArmazenamento on localArmazenamento.codigo = patrimoniounidade.localArmazenamento  ");
		sqlStr.append(" where ");
		if (Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCodigo())) {
			sqlStr.append(" localArmazenamento.unidadeensino = ").append(unidadeEnsinoVO.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(patrimonioVO.getCodigo())) {
			sqlStr.append(" and patrimonio.codigo = ").append(patrimonioVO.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(tipoPatrimonioVO.getCodigo())) {
			sqlStr.append(" and tipopatrimonio.codigo = ").append(tipoPatrimonioVO.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(localArmazenamentoVO.getCodigo()) && !patrimonioRelVO.getIncluirSubLocal()) {
			sqlStr.append("and localArmazenamento.codigo = ").append(localArmazenamentoVO.getCodigo());
		}
		if (patrimonioRelVO.getIncluirSubLocal()) {
			sqlStr.append(" and localArmazenamento.codigo in ( ");
			sqlStr.append(" WITH RECURSIVE  local (codigo, localarmazenamento, unidadeensino, localarmazenamentosuperior) as ( ");
			sqlStr.append(" select codigo, localarmazenamento, unidadeensino, localarmazenamentosuperior from localarmazenamento ");
			sqlStr.append(" where localarmazenamento.codigo =  ").append(localArmazenamentoVO.getCodigo());
			sqlStr.append(" union ");
			sqlStr.append(" select localarmazenamento.codigo, localarmazenamento.localarmazenamento, localarmazenamento.unidadeensino, localarmazenamento.localarmazenamentosuperior from localarmazenamento ");
			sqlStr.append(" inner join local on localarmazenamento.localarmazenamentosuperior = local.codigo  ");
			sqlStr.append(" ) select codigo from local order by case when local.localarmazenamentosuperior is null then 0 else local.localarmazenamentosuperior end )");
		}
		sqlStr.append(" AND ").append(adicionarFiltroSituacaoPatrimonioUnidade(patrimonioRelVO, "patrimoniounidade"));
		if (ordenarPor.equals("LO")) {
			sqlStr.append(" group by patrimonio.descricao, tipopatrimonio.descricao,patrimoniounidade.valorrecurso,localArmazenamento.localarmazenamento ");
		} else if (ordenarPor.equals("PA")) {
			sqlStr.append(" group by patrimonio.descricao, tipopatrimonio.descricao,patrimoniounidade.valorrecurso,patrimonio.descricao,localarmazenamento.localarmazenamento ");
		} else if (ordenarPor.equals("ST")) {
			sqlStr.append(" group by patrimonio.descricao, tipopatrimonio.descricao,patrimoniounidade.valorrecurso,patrimoniounidade.situacao,localarmazenamento.localarmazenamento ");
		} else if (ordenarPor.equals("TP")) {
			sqlStr.append(" group by patrimonio.descricao, tipopatrimonio.descricao,patrimoniounidade.valorrecurso,tipopatrimonio.descricao,localarmazenamento.localarmazenamento ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List<PatrimonioRelVO> listaPatrimonioRelVO = new ArrayList<PatrimonioRelVO>(0);
		while (tabelaResultado.next()) {
			PatrimonioRelVO obj = new PatrimonioRelVO();
			obj.getLocalArmazenamentoVO().setLocalArmazenamento(tabelaResultado.getString("localArmazenamento"));
			obj.setQtdEmUso(tabelaResultado.getInt("qtdeEmUso"));
			obj.setQtdEmprestado(tabelaResultado.getInt("qtdeEmprestado"));
			obj.setQtdManutencao(tabelaResultado.getInt("qtdeManut"));
			obj.setQtdSeparadoParaDescarte(tabelaResultado.getInt("qtdeSepDesc"));
			obj.setQtdDescartado(tabelaResultado.getInt("qtdeDesc"));
			obj.setValorRecurso(tabelaResultado.getBigDecimal("valorRecurso"));
			listaPatrimonioRelVO.add(obj);
		}
		return listaPatrimonioRelVO;
	}

	private PatrimonioRelVO montarDados(SqlRowSet dadosSQL, Integer nivelMontarDados, UsuarioVO usuarioVO) throws InvalidResultSetAccessException, Exception {
		PatrimonioRelVO obj = new PatrimonioRelVO();
		while (dadosSQL.next()) {
			PatrimonioUnidadeVO patrimonioUnidadeVO = new PatrimonioUnidadeVO();
			patrimonioUnidadeVO.setCodigo(dadosSQL.getInt("patrimoniounidade.codigo"));
			patrimonioUnidadeVO.setCodigoBarra(dadosSQL.getString("codigobarra"));
			patrimonioUnidadeVO.setValorRecurso(dadosSQL.getBigDecimal("valorRecurso"));
			patrimonioUnidadeVO.setSituacaoPatrimonioUnidade(SituacaoPatrimonioUnidadeEnum.valueOf(dadosSQL.getString("situacao")));
			patrimonioUnidadeVO.getPatrimonioVO().setDescricao(dadosSQL.getString("descricaoPatrimonio"));
			patrimonioUnidadeVO.getPatrimonioVO().getTipoPatrimonioVO().setDescricao(dadosSQL.getString("tipoPatrimonioDescricao"));
			patrimonioUnidadeVO.getLocalArmazenamento().setLocalArmazenamento(dadosSQL.getString("localArmazenamento"));
			obj.getPatrimonioVO().getPatrimonioUnidadeVOs().add(patrimonioUnidadeVO);
		}
		return obj;
	}

	public StringBuilder adicionarFiltroSituacaoPatrimonioUnidade(PatrimonioRelVO patrimonioRelVO, String keyEntidade) {
		StringBuilder sqlStr = new StringBuilder("");
//		if (!Uteis.isAtributoPreenchido(patrimonioRelVO)) {
//			return sqlStr;
//		}
		keyEntidade = keyEntidade.trim();
		sqlStr.append(" ").append(keyEntidade).append(".situacao in (");
		if (patrimonioRelVO.getEmUso() || patrimonioRelVO.getEmprestado() || patrimonioRelVO.getManutencao() || patrimonioRelVO.getSeparadoParaDescarte() || patrimonioRelVO.getDescartado()) {
			boolean virgula = false;
			if (patrimonioRelVO.getEmUso()) {
				sqlStr.append(virgula ? "," : "").append("'" + SituacaoPatrimonioUnidadeEnum.DISPONIVEL + "'");
				virgula = true;
			}
			if (patrimonioRelVO.getEmprestado()) {
				sqlStr.append(virgula ? "," : "").append("'" + SituacaoPatrimonioUnidadeEnum.EMPRESTADO + "'");
				virgula = true;
			}
			if (patrimonioRelVO.getManutencao()) {
				sqlStr.append(virgula ? "," : "").append("'" + SituacaoPatrimonioUnidadeEnum.EM_MANUTENCAO + "'");
				virgula = true;
			}
			if (patrimonioRelVO.getSeparadoParaDescarte()) {
				sqlStr.append(virgula ? "," : "").append("'" + SituacaoPatrimonioUnidadeEnum.SEPARADO_PARA_DESCARTE + "'");
				virgula = true;
			}
			if (patrimonioRelVO.getDescartado()) {
				sqlStr.append(virgula ? "," : "").append("'" + SituacaoPatrimonioUnidadeEnum.DESCARTADO + "'");
				virgula = true;
			}
		} else {
			return new StringBuilder(" 1 = 1 ");
		}
		sqlStr.append(") ");
		return sqlStr;
	}

	@Override
	public String designIReportRelatorioAnalitico() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator + "AnaliticoPatrimonioRel.jrxml");
	}

	@Override
	public String designIReportRelatorioSinteticoPatrimoio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator + "SinteticoPatrimonioRel.jrxml");
	}

	@Override
	public String designIReportRelatorioSinteticoPorTipoPatrimoio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator + "SinteticoPorTipoPatrimonioRel.jrxml");
	}

	@Override
	public String designIReportRelatorioSinteticoPorLocalArmazenamento() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator + "SinteticoLocalArmazenamentoRel.jrxml");
	}
	

	@Override
	public String designIReportRelatorioAnaliticoExcel() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator + "AnaliticoPatrimonioExcelRel.jrxml");
	}

	@Override
	public String designIReportRelatorioSinteticoPatrimoioExcel() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator + "SinteticoPatrimonioExcelRel.jrxml");
	}

	@Override
	public String designIReportRelatorioSinteticoPorTipoPatrimoioExcel() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator + "SinteticoPorTipoPatrimonioExcelRel.jrxml");
	}

	@Override
	public String designIReportRelatorioSinteticoPorLocalArmazenamentoExcel() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator + "SinteticoLocalArmazenamentoExcelRel.jrxml");
	}


}
