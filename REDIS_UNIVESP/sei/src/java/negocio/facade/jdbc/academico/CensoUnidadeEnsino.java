package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.CensoUnidadeEnsinoVO;
import negocio.comuns.academico.CensoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CensoUnidadeEnsinoInterfaceFacade;

@Repository
@Scope("singleton")
public class CensoUnidadeEnsino extends ControleAcesso implements CensoUnidadeEnsinoInterfaceFacade {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6583698883111955862L;
	private static final String  TABLE_NAME = "censoUnidadeEnsino";

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(CensoVO censoVO, UsuarioVO usuarioVO) throws Exception {
		for(CensoUnidadeEnsinoVO censoUnidadeEnsinoVO: censoVO.getCensoUnidadeEnsinoVOs()) {
			if(censoUnidadeEnsinoVO.getIsSelecionado()) {
				if(!Uteis.isAtributoPreenchido(censoUnidadeEnsinoVO.getCodigo())) {
					censoUnidadeEnsinoVO.setCensoVO(censoVO);
					incluir(censoUnidadeEnsinoVO, usuarioVO);
				}				
			}else if(Uteis.isAtributoPreenchido(censoVO.getCodigo())) {
				excluir(censoUnidadeEnsinoVO, usuarioVO);				
			}			
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluir(CensoUnidadeEnsinoVO censoUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception {
		incluir(censoUnidadeEnsinoVO, TABLE_NAME, 
				new AtributoPersistencia().add("censo", censoUnidadeEnsinoVO.getCensoVO())
				.add("unidadeEnsino", censoUnidadeEnsinoVO.getUnidadeEnsinoVO()), usuarioVO);
		
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(CensoUnidadeEnsinoVO censoUnidadeEnsinoVO, UsuarioVO usuarioVO) throws Exception {
		getConexao().getJdbcTemplate().update("delete from censoUnidadeEnsino where censo = ? and unidadeEnsino = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), censoUnidadeEnsinoVO.getCensoVO().getCodigo(), censoUnidadeEnsinoVO.getUnidadeEnsinoVO().getCodigo());
		
	}

	@Override
	public List<CensoUnidadeEnsinoVO> consultarPorCenso(Integer censo) throws Exception {
		StringBuilder sb = new StringBuilder(getSqlPadrao());
		sb.append(" where censoUnidadeEnsino.censo = ? order by unidadeEnsino.nome ");
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), censo);		
		return montarDadosConsulta(rs);
	}
	
	private List<CensoUnidadeEnsinoVO> montarDadosConsulta(SqlRowSet rs) throws Exception{
		List<CensoUnidadeEnsinoVO> censoUnidadeEnsinoVOs =  new ArrayList<CensoUnidadeEnsinoVO>(0);
		while(rs.next()) {
			censoUnidadeEnsinoVOs.add(montarDados(rs));
		}
		return censoUnidadeEnsinoVOs;		
	}
	
	private CensoUnidadeEnsinoVO montarDados(SqlRowSet rs) throws Exception{
		CensoUnidadeEnsinoVO censoUnidadeEnsinoVO =  new CensoUnidadeEnsinoVO();
		censoUnidadeEnsinoVO.setNovoObj(rs.getInt("codigo") > 0);
		censoUnidadeEnsinoVO.setSelecionado(rs.getInt("codigo") > 0);
		censoUnidadeEnsinoVO.setCodigo(rs.getInt("codigo"));
		censoUnidadeEnsinoVO.getUnidadeEnsinoVO().setCodigo(rs.getInt("unidadeEnsino.codigo"));
		censoUnidadeEnsinoVO.getUnidadeEnsinoVO().setNome(rs.getString("unidadeEnsino.nome"));
		censoUnidadeEnsinoVO.getCensoVO().setCodigo(rs.getInt("censo"));
		return censoUnidadeEnsinoVO;		
	}
	
	private StringBuilder getSqlPadrao() {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select censoUnidadeEnsino.codigo, unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\", ");
		sb.append(" censoUnidadeEnsino.censo ");
		sb.append(" from censoUnidadeEnsino ");
		sb.append(" inner join unidadeEnsino on unidadeEnsino.codigo = censoUnidadeEnsino.unidadeEnsino ");
		return sb;
	}
		

	@Override
	public void carregarUnidadeEnsinoNaoSelecionado(CensoVO censoVO, Integer unidadeEnsinoLogado) throws Exception {
		StringBuilder sb = new StringBuilder("");
		sb.append(" select 0 as codigo, 0 as censo, unidadeEnsino.codigo as \"unidadeEnsino.codigo\", unidadeEnsino.nome as \"unidadeEnsino.nome\"  ");		
		sb.append(" from unidadeEnsino ");
		sb.append(" where not exists (select codigo from censoUnidadeEnsino where unidadeEnsino.codigo = censoUnidadeEnsino.unidadeEnsino ");		
		sb.append(" and censoUnidadeEnsino.censo = ? ) order by unidadeEnsino.nome  ");
		if(Uteis.isAtributoPreenchido(unidadeEnsinoLogado)) {
			sb.append(" and unidadeEnsino.codigo =  ").append(unidadeEnsinoLogado);
		}
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), censoVO.getCodigo());
		censoVO.getCensoUnidadeEnsinoVOs().addAll(montarDadosConsulta(rs));
		
	}
}
