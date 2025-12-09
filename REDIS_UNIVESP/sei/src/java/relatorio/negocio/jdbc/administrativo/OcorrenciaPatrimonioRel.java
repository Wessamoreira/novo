/**
 * 
 */
package relatorio.negocio.jdbc.administrativo;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.PatrimonioVO;
import negocio.comuns.patrimonio.TipoPatrimonioVO;
import negocio.comuns.patrimonio.enumeradores.SituacaoOcorrenciaPatrimonioEnum;
import negocio.comuns.patrimonio.enumeradores.TipoOcorrenciaPatrimonioEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.administrativo.OcorrenciaPatrimonioRelVO;
import relatorio.negocio.interfaces.administrativo.OcorrenciaPatrimonioRelInterfaceFacede;

/**
 * @author Leonardo Riciolle
 *
 */
@Repository
@Scope("singleton")
@Lazy
public class OcorrenciaPatrimonioRel extends SuperFacadeJDBC implements OcorrenciaPatrimonioRelInterfaceFacede {

	private static final long serialVersionUID = 1L;

	@Override
	public void validarDados(OcorrenciaPatrimonioRelVO ocorrenciaPatrimonioRelVO, UnidadeEnsinoVO unidadeEnsinoVO, TipoPatrimonioVO tipoPatrimonioVO, LocalArmazenamentoVO localArmazenamentoVO) throws Exception {
		if (unidadeEnsinoVO.getCodigo() == null || unidadeEnsinoVO.getCodigo().equals(0)) {
			throw new Exception("O campo UNIDADE ENSINO deve ser informado para geração do relatório.");
		}
//		if (tipoPatrimonioVO.getCodigo() == null || tipoPatrimonioVO.getCodigo().equals(0)) {
//			throw new Exception("O campo TIPO PATRIMONIO deve ser informado para geração do relatório.");
//		}
		if (ocorrenciaPatrimonioRelVO.getIncluirSubLocal()) {
			if (localArmazenamentoVO.getCodigo() == null || localArmazenamentoVO.getCodigo().equals(0)) {
				throw new Exception("O campo LOCAL ARMAZENAMNETO deve ser informado para geração do relatório.");
			}
		}
	}

	@Override
	public List<OcorrenciaPatrimonioRelVO> criarObjeto(OcorrenciaPatrimonioRelVO ocorrenciaPatrimonioRelVO, UnidadeEnsinoVO unidadeEnsinoVO, PatrimonioVO patrimonioVO, TipoPatrimonioVO tipoPatrimonioVO, LocalArmazenamentoVO localArmazenamentoVO, UsuarioVO usuarioVO, Date dataInicio, Date dataFim, String ordenarPor) throws Exception {
		List<OcorrenciaPatrimonioRelVO> listaOcorrenciaPatrimonioRelVO = new ArrayList<OcorrenciaPatrimonioRelVO>(0);
		SqlRowSet dadosSQL = executarConsulta(ocorrenciaPatrimonioRelVO, unidadeEnsinoVO, patrimonioVO, tipoPatrimonioVO, localArmazenamentoVO, dataInicio, dataFim, ordenarPor);
		while (dadosSQL.next()) {
			listaOcorrenciaPatrimonioRelVO.add(montarDados(dadosSQL, Uteis.NIVELMONTARDADOS_TODOS, usuarioVO));
		}
		return listaOcorrenciaPatrimonioRelVO;
	}

	private SqlRowSet executarConsulta(OcorrenciaPatrimonioRelVO ocorrenciaPatrimonioRelVO, UnidadeEnsinoVO unidadeEnsinoVO, PatrimonioVO patrimonioVO, TipoPatrimonioVO tipoPatrimonioVO, LocalArmazenamentoVO localArmazenamentoVO, Date dataInicio, Date dataFim, String ordenarPor) throws Exception {
		StringBuilder sqlStr = new StringBuilder();
		sqlStr.append("SELECT ocorrenciapatrimonio.dataocorrencia as \"dataOcorrencia\",");
		sqlStr.append(" ocorrenciapatrimonio.dataprevisaodevolucao as \"dataPrevisaoDevolucao\", ");
		sqlStr.append(" ocorrenciapatrimonio.tipoocorrenciapatrimonio as \"tipoocorrenciapatrimonio\", ");
		sqlStr.append(" ocorrenciapatrimonio.solicitanteemprestimo as \"solicitanteEmprestimo\", ");
		sqlStr.append(" patrimonio.descricao as \"descricaoPatrimonio\", ");
		sqlStr.append(" patrimoniounidade.codigobarra as \"codigobarra\", ");
		sqlStr.append(" ocorrenciapatrimonio.situacao as \"situacao\", ");
		sqlStr.append(" tipopatrimonio.descricao as \"tipoPatrimonioDescricao\", ");
		sqlStr.append(" ocorrenciapatrimonio.localarmazenamentoorigem as \"localArmazenamentoOrigem\", ");
		sqlStr.append(" ocorrenciapatrimonio.localarmazenamentodestino as \"localArmazenamentoDestino\", ");
		sqlStr.append(" patrimoniounidade.localarmazenamento as \"localArmazenamento\" ");
		sqlStr.append(" from ocorrenciapatrimonio ");
		sqlStr.append(" inner join patrimoniounidade on patrimoniounidade.codigo = ocorrenciapatrimonio.patrimoniounidade");
		sqlStr.append(" inner join patrimonio on patrimonio.codigo = patrimoniounidade.patrimonio ");
		sqlStr.append(" inner join localArmazenamento on localArmazenamento.codigo = patrimoniounidade.localArmazenamento");
		sqlStr.append(" inner join tipopatrimonio on tipopatrimonio.codigo = patrimonio.tipopatrimonio");
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
		if (Uteis.isAtributoPreenchido(localArmazenamentoVO.getCodigo())  && !ocorrenciaPatrimonioRelVO.getIncluirSubLocal()) {
			sqlStr.append("and localArmazenamento.codigo = ").append(localArmazenamentoVO.getCodigo());
		}
		sqlStr.append(" AND ocorrenciapatrimonio.dataocorrencia >= '").append(Uteis.getDataBD0000(dataInicio)).append("' ");
		sqlStr.append(" AND ocorrenciapatrimonio.dataocorrencia <= '").append(Uteis.getDataBD2359(dataFim)).append("' ");
		if (ocorrenciaPatrimonioRelVO.getIncluirSubLocal()) {
			sqlStr.append(" and localArmazenamento.codigo in ( ");
			sqlStr.append(" WITH RECURSIVE  local (codigo, localarmazenamento, unidadeensino, localarmazenamentosuperior) as ( ");
			sqlStr.append(" select codigo, localarmazenamento, unidadeensino, localarmazenamentosuperior from localarmazenamento ");
			sqlStr.append(" where localarmazenamento.codigo =  ").append(localArmazenamentoVO.getCodigo());
			sqlStr.append(" union ");
			sqlStr.append(" select localarmazenamento.codigo, localarmazenamento.localarmazenamento, localarmazenamento.unidadeensino, localarmazenamento.localarmazenamentosuperior from localarmazenamento ");
			sqlStr.append(" inner join local on localarmazenamento.localarmazenamentosuperior = local.codigo  ");
			sqlStr.append(" ) select codigo from local order by case when local.localarmazenamentosuperior is null then 0 else local.localarmazenamentosuperior end )");
		}
		sqlStr.append(" AND ").append(adicionarFiltroTipoOcorrenciaPatrimonio(ocorrenciaPatrimonioRelVO, "ocorrenciaPatrimonio"));
		if (ordenarPor.equals("LO")) {
			sqlStr.append(" order by  ocorrenciapatrimonio.tipoocorrenciapatrimonio , localArmazenamento.localarmazenamento ");
		} else if (ordenarPor.equals("PA")) {
			sqlStr.append(" order by  ocorrenciapatrimonio.tipoocorrenciapatrimonio ,patrimonio.descricao ");
		} else if (ordenarPor.equals("TP")) {
			sqlStr.append(" order by  ocorrenciapatrimonio.tipoocorrenciapatrimonio , tipopatrimonio.descricao ");
		}
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return tabelaResultado;
	}

	public StringBuilder adicionarFiltroTipoOcorrenciaPatrimonio(OcorrenciaPatrimonioRelVO ocorrenciaPatrimonioRelVO, String keyEntidade) {
		StringBuilder sqlStr = new StringBuilder("");
		if (ocorrenciaPatrimonioRelVO == null) {
			return new StringBuilder(" 1 = 1 ");	
		}
		keyEntidade = keyEntidade.trim();
		sqlStr.append(" ").append(keyEntidade).append(".tipoocorrenciapatrimonio in (");
		if (ocorrenciaPatrimonioRelVO.getTrocaLocal() || ocorrenciaPatrimonioRelVO.getEmprestimo() || ocorrenciaPatrimonioRelVO.getManutencao() || ocorrenciaPatrimonioRelVO.getSeparadoParaDescarte() || ocorrenciaPatrimonioRelVO.getDescartado()) {
			boolean virgula = false;
			if (ocorrenciaPatrimonioRelVO.getTrocaLocal()) {
				sqlStr.append(virgula ? "," : "").append("'" + TipoOcorrenciaPatrimonioEnum.TROCA_LOCAL + "'");
				virgula = true;
			}
			if (ocorrenciaPatrimonioRelVO.getEmprestimo()) {
				sqlStr.append(virgula ? "," : "").append("'" + TipoOcorrenciaPatrimonioEnum.EMPRESTIMO + "'");
				virgula = true;
			}
			if (ocorrenciaPatrimonioRelVO.getManutencao()) {
				sqlStr.append(virgula ? "," : "").append("'" + TipoOcorrenciaPatrimonioEnum.MANUTENCAO + "'");
				virgula = true;
			}
			if (ocorrenciaPatrimonioRelVO.getSeparadoParaDescarte()) {
				sqlStr.append(virgula ? "," : "").append("'" + TipoOcorrenciaPatrimonioEnum.SEPARAR_DESCARTE + "'");
				virgula = true;
			}
			if (ocorrenciaPatrimonioRelVO.getDescartado()) {
				sqlStr.append(virgula ? "," : "").append("'" + TipoOcorrenciaPatrimonioEnum.DESCARTE + "'");
				virgula = true;
			}
		} else {
			return new StringBuilder(" 1 = 1 ");	
		}
		sqlStr.append(") ");
		return sqlStr;
	}

	private OcorrenciaPatrimonioRelVO montarDados(SqlRowSet dadosSQL, Integer nivelMontarDados, UsuarioVO usuarioVO) throws InvalidResultSetAccessException, Exception {
		OcorrenciaPatrimonioRelVO obj = new OcorrenciaPatrimonioRelVO();
		obj.getOcorrenciaPatrimonioVO().setDataOcorrencia(dadosSQL.getDate("dataOcorrencia"));
		obj.getOcorrenciaPatrimonioVO().setDataPrevisaoDevolucao(dadosSQL.getDate("dataPrevisaoDevolucao"));
		obj.getOcorrenciaPatrimonioVO().setTipoOcorrenciaPatrimonio(TipoOcorrenciaPatrimonioEnum.valueOf(dadosSQL.getString("tipoocorrenciapatrimonio")));
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("solicitanteEmprestimo"))) {
			obj.getOcorrenciaPatrimonioVO().setSolicitanteEmprestimo(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorChavePrimaria(dadosSQL.getInt("solicitanteEmprestimo"), false, usuarioVO));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("localarmazenamento"))) {
			obj.getOcorrenciaPatrimonioVO().getPatrimonioUnidade().setLocalArmazenamento(getFacadeFactory().getLocalArmazenamentoFacade().consultarPorCodigo(dadosSQL.getInt("localarmazenamento"), false, usuarioVO));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("localArmazenamentoOrigem"))) {
			obj.getOcorrenciaPatrimonioVO().setLocalArmazenamentoOrigem(getFacadeFactory().getLocalArmazenamentoFacade().consultarPorCodigo(dadosSQL.getInt("localArmazenamentoOrigem"), false, usuarioVO));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("localArmazenamentoDestino"))) {
			obj.getOcorrenciaPatrimonioVO().setLocalArmazenamentoDestino(getFacadeFactory().getLocalArmazenamentoFacade().consultarPorCodigo(dadosSQL.getInt("localArmazenamentoDestino"), false, usuarioVO));
		}
		obj.getOcorrenciaPatrimonioVO().setSituacao(SituacaoOcorrenciaPatrimonioEnum.valueOf(dadosSQL.getString("situacao")));
		obj.getOcorrenciaPatrimonioVO().getPatrimonioUnidade().getPatrimonioVO().setDescricao(dadosSQL.getString("descricaoPatrimonio"));
		obj.getOcorrenciaPatrimonioVO().getPatrimonioUnidade().setCodigoBarra(dadosSQL.getString("codigobarra"));
		obj.getOcorrenciaPatrimonioVO().getPatrimonioUnidade().getPatrimonioVO().getTipoPatrimonioVO().setDescricao(dadosSQL.getString("tipoPatrimonioDescricao"));
		return obj;
	}

	@Override
	public String designIReportRelatorioOcorrenciaPatrimonioRel() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator + "OcorrenciaPatrimonioRel.jrxml");
	}
}
