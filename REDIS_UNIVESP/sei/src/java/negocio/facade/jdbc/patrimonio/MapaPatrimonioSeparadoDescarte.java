/**
 * 
 */
package negocio.facade.jdbc.patrimonio;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.OcorrenciaPatrimonioVO;
import negocio.comuns.patrimonio.PatrimonioUnidadeVO;
import negocio.comuns.patrimonio.TipoPatrimonioVO;
import negocio.comuns.patrimonio.enumeradores.SituacaoPatrimonioUnidadeEnum;
import negocio.comuns.patrimonio.enumeradores.TipoOcorrenciaPatrimonioEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.patrimonio.MapaPatrimonioSeparadoDescarteInterfaceFacade;

/**
 * @author Rodrigo Wind
 *
 */
@Repository
@Lazy
public class MapaPatrimonioSeparadoDescarte extends ControleAcesso implements MapaPatrimonioSeparadoDescarteInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 826589276215731116L;
	private static String idEntidade; 

	/* (non-Javadoc)
	 * @see negocio.interfaces.patrimonio.MapaPatrimonioSeparadoDescarteInterfaceFacade#consultarPatrimonioUnidadeVOs(java.lang.Integer, negocio.comuns.patrimonio.LocalArmazenamentoVO, negocio.comuns.patrimonio.TipoPatrimonioVO, java.lang.String, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	public List<PatrimonioUnidadeVO> consultarPatrimonioUnidadeVOs(Integer unidadeEnsino, LocalArmazenamentoVO localArmazenamentoVO, Boolean incluirSubLocal, TipoPatrimonioVO tipoPatrimonioVO, String codigoBarra, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		PatrimonioUnidade.consultar(getIdEntidade(), validarAcesso, usuarioVO);
		StringBuilder sqlStr = getSqlConsultaCompleta();
		sqlStr.append(" WHERE patrimoniounidade.situacao = '").append(SituacaoPatrimonioUnidadeEnum.SEPARADO_PARA_DESCARTE.name()).append("' ");
		if (Uteis.isAtributoPreenchido(codigoBarra)) {
			if(!codigoBarra.matches("\\d*")){
				throw new Exception(UteisJSF.internacionalizar("msg_PatrimonioUnidade_informeApenasValoresNumericos"));
			}
			sqlStr.append(" and  codigoBarra::NUMERIC(20,0) = '").append(codigoBarra).append("'::NUMERIC(20,0) ");
		}
		if (Uteis.isAtributoPreenchido(tipoPatrimonioVO)) {
			sqlStr.append(" and patrimonio.tipoPatrimonio  = ").append(tipoPatrimonioVO.getCodigo());
		}
		if (Uteis.isAtributoPreenchido(localArmazenamentoVO)) {
			if(incluirSubLocal){				
					sqlStr.append(" and localArmazenamento.codigo in ( ");
					sqlStr.append(" WITH RECURSIVE  local (codigo, localarmazenamento, unidadeensino, localarmazenamentosuperior) as ( ");
					sqlStr.append(" select codigo, localarmazenamento, unidadeensino, localarmazenamentosuperior from localarmazenamento ");
					sqlStr.append(" where localarmazenamento.codigo =  ").append(localArmazenamentoVO.getCodigo());
					sqlStr.append(" union ");
					sqlStr.append(" select localarmazenamento.codigo, localarmazenamento.localarmazenamento, localarmazenamento.unidadeensino, localarmazenamento.localarmazenamentosuperior from localarmazenamento ");
					sqlStr.append(" inner join local on localarmazenamento.localarmazenamentosuperior = local.codigo  ");
					sqlStr.append(" ) select codigo from local order by case when local.localarmazenamentosuperior is null then 0 else local.localarmazenamentosuperior end )");				
			}else{
				sqlStr.append(" and localArmazenamento.codigo = ").append(localArmazenamentoVO.getCodigo());
			}
		}		
		if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
			sqlStr.append(" and unidadeensino.codigo =   ").append(unidadeEnsino);			
		} 	
		
		sqlStr.append(" order by patrimonio.descricao, localArmazenamento.localArmazenamento, codigoBarra ");
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString()), NivelMontarDados.TODOS, usuarioVO);
	}
	
	public StringBuilder getSqlConsultaCompleta() {
		StringBuilder sql = new StringBuilder("select patrimoniounidade.*, localarmazenamento.localarmazenamento as \"localarmazenamento.localarmazenamento\", ");
		sql.append(" unidadeensino.codigo as \"unidadeensino.codigo\", unidadeensino.nome as \"unidadeensino.nome\", patrimonio.descricao  as \"patrimonio.descricao\", tipopatrimonio.descricao as \"tipopatrimonio.descricao\" ");
		sql.append(" from patrimoniounidade ");
		sql.append(" left join localarmazenamento on localarmazenamento.codigo = patrimoniounidade.localarmazenamento");
		sql.append(" left join unidadeensino on localarmazenamento.unidadeensino = unidadeensino.codigo ");
		sql.append(" left join patrimonio on patrimoniounidade.patrimonio = patrimonio.codigo ");
		sql.append(" left join tipopatrimonio on patrimonio.tipopatrimonio = tipopatrimonio.codigo ");
		return sql;
	}
	
	public List<PatrimonioUnidadeVO> montarDadosConsulta(SqlRowSet tabelaResultado, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		List<PatrimonioUnidadeVO> patrimonioUnidadeVOs = new ArrayList<PatrimonioUnidadeVO>(0);
		while (tabelaResultado.next()) {
			patrimonioUnidadeVOs.add(montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
		}
		return patrimonioUnidadeVOs;
	}

	public PatrimonioUnidadeVO montarDados(SqlRowSet tabelaResultado, NivelMontarDados nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
		PatrimonioUnidadeVO obj = new PatrimonioUnidadeVO();
		obj.setNovoObj(false);
		obj.setCodigo(tabelaResultado.getInt("codigo"));
		obj.getPatrimonioVO().setCodigo(tabelaResultado.getInt("patrimonio"));		
		obj.getPatrimonioVO().setDescricao(tabelaResultado.getString("patrimonio.descricao"));
		obj.getPatrimonioVO().getTipoPatrimonioVO().setDescricao(tabelaResultado.getString("tipopatrimonio.descricao"));
		obj.setCodigoBarra(tabelaResultado.getString("codigobarra"));
		obj.setNumeroDeSerie(tabelaResultado.getString("numeroserie"));

		if (Uteis.isAtributoPreenchido(tabelaResultado.getString("situacao"))) {
			obj.setSituacaoPatrimonioUnidade(SituacaoPatrimonioUnidadeEnum.valueOf(tabelaResultado.getString("situacao")));
		}
		obj.setValorRecurso(tabelaResultado.getBigDecimal("valorrecurso"));
		obj.setUnidadeLocado(tabelaResultado.getBoolean("unidadelocado"));
		obj.setPermiteReserva(tabelaResultado.getBoolean("permitereserva"));
		obj.getLocalArmazenamento().setCodigo(tabelaResultado.getInt("localarmazenamento"));
		obj.getLocalArmazenamento().setLocalArmazenamento(tabelaResultado.getString("localarmazenamento.localarmazenamento"));
		obj.getLocalArmazenamento().getUnidadeEnsinoVO().setCodigo(tabelaResultado.getInt("unidadeensino.codigo"));
		obj.getLocalArmazenamento().getUnidadeEnsinoVO().setNome(tabelaResultado.getString("unidadeensino.nome"));		

		return obj;
	}

	/* (non-Javadoc)
	 * @see negocio.interfaces.patrimonio.MapaPatrimonioSeparadoDescarteInterfaceFacade#realizarSeparacaoPatrimonioUnidadeSelecionado(java.util.List, negocio.comuns.patrimonio.PatrimonioUnidadeVO)
	 */
	@Override
	public List<PatrimonioUnidadeVO> realizarSeparacaoPatrimonioUnidadeSelecionado(List<PatrimonioUnidadeVO> patrimonioUnidadeVOs) throws Exception  {
		List<PatrimonioUnidadeVO> patrimonioUnidadeSelecionadoVOs = new ArrayList<PatrimonioUnidadeVO>(0);
		for(PatrimonioUnidadeVO obj: patrimonioUnidadeVOs){
			if(obj.getSelecionado()){
				patrimonioUnidadeSelecionadoVOs.add(obj);
			}
		}
		validarDados(patrimonioUnidadeSelecionadoVOs);
		return patrimonioUnidadeSelecionadoVOs;
	}

	/* (non-Javadoc)
	 * @see negocio.interfaces.patrimonio.MapaPatrimonioSeparadoDescarteInterfaceFacade#persistir(java.util.List, java.lang.String, boolean, negocio.comuns.arquitetura.UsuarioVO)
	 */
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<PatrimonioUnidadeVO> patrimonioUnidadeVOs, String observacao, LocalArmazenamentoVO localArmazenamentoDestinoVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
		MapaPatrimonioSeparadoDescarte.incluir(getIdEntidade(), validarAcesso, usuarioVO);		
		validarDados(patrimonioUnidadeVOs);
		OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO = new OcorrenciaPatrimonioVO();
		ocorrenciaPatrimonioVO.setObservacao(observacao);
		ocorrenciaPatrimonioVO.getResponsavelOcorrencia().setCodigo(usuarioVO.getCodigo());
		ocorrenciaPatrimonioVO.getResponsavelOcorrencia().setNome(usuarioVO.getNome());
		ocorrenciaPatrimonioVO.setTipoOcorrenciaPatrimonio(TipoOcorrenciaPatrimonioEnum.DESCARTE);
		ocorrenciaPatrimonioVO.setDataOcorrencia(new Date());		
		for(PatrimonioUnidadeVO patrimonioUnidadeVO: patrimonioUnidadeVOs){
			getFacadeFactory().getOcorrenciaPatrimonioFacade().inicializarDadosPatrimonioUnidade(ocorrenciaPatrimonioVO, patrimonioUnidadeVO, usuarioVO);
			if(Uteis.isAtributoPreenchido(localArmazenamentoDestinoVO)){
				ocorrenciaPatrimonioVO.getLocalArmazenamentoDestino().setCodigo(localArmazenamentoDestinoVO.getCodigo());
				ocorrenciaPatrimonioVO.getLocalArmazenamentoDestino().setLocalArmazenamento(localArmazenamentoDestinoVO.getLocalArmazenamento());
			}
			getFacadeFactory().getOcorrenciaPatrimonioFacade().persistir(ocorrenciaPatrimonioVO, false,false, false, usuarioVO);
			ocorrenciaPatrimonioVO.setNovoObj(true);
			ocorrenciaPatrimonioVO.setCodigo(0);			
		}
	}
	
	public void validarDados(List<PatrimonioUnidadeVO> patrimonioUnidadeVOs) throws Exception {
		if(patrimonioUnidadeVOs.isEmpty()){
			throw new Exception(UteisJSF.internacionalizar("msg_MapaPatrimonioSeparadoDescarte_selecionePatrimonioParaDescate"));
		}
	}

	/* (non-Javadoc)
	 * @see negocio.interfaces.patrimonio.MapaPatrimonioSeparadoDescarteInterfaceFacade#realizarMarcarDesmarcarTodosPatrimonioUnidade(java.util.List, boolean)
	 */
	@Override
	public void realizarMarcarDesmarcarTodosPatrimonioUnidade(List<PatrimonioUnidadeVO> patrimonioUnidadeVOs, boolean selecionar) {
		for(PatrimonioUnidadeVO obj: patrimonioUnidadeVOs){
			obj.setSelecionado(selecionar);
		}

	}

	/**
	 * @return the idEntidade
	 */
	public static String getIdEntidade() {
		if (idEntidade == null) {
			idEntidade = "MapaPatrimonioSeparadoDescarte";
		}
		return idEntidade;
	}

	/**
	 * @param idEntidade the idEntidade to set
	 */
	public static void setIdEntidade(String idEntidade) {
		MapaPatrimonioSeparadoDescarte.idEntidade = idEntidade;
	}
	
	
}
