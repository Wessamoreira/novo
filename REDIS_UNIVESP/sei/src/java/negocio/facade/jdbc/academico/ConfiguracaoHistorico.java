package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ConfiguracaoHistoricoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ConfiguracaoHistoricoInterfaceFacade;

@Repository
@Scope("singleton")
public class ConfiguracaoHistorico extends ControleAcesso implements ConfiguracaoHistoricoInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2233121237700641766L;
	private static final String nomeTabela = "configuracaoHistorico";
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoHistoricoVO configuracaoHistoricoVO, UsuarioVO usuarioVO) throws Exception {
		if(!Uteis.isAtributoPreenchido(configuracaoHistoricoVO.getCodigo())) {
			incluir(configuracaoHistoricoVO, nomeTabela , new AtributoPersistencia().add("nivelEducacional", configuracaoHistoricoVO.getNivelEducacional()), usuarioVO);
		}else {
			alterar(configuracaoHistoricoVO, nomeTabela , 
					new AtributoPersistencia().add("nivelEducacional", configuracaoHistoricoVO.getNivelEducacional()),
					new AtributoPersistencia().add("codigo", configuracaoHistoricoVO.getCodigo()), usuarioVO);
		}
		getFacadeFactory().getConfiguracaoObservacaoHistoricoFacade().persistir(configuracaoHistoricoVO, usuarioVO);
		getFacadeFactory().getConfiguracaoLayoutHistoricoFacade().persistir(configuracaoHistoricoVO, usuarioVO);
	}


	@Override
	public List<ConfiguracaoHistoricoVO> consultarConfiguracoesHistorico(UsuarioVO usuarioVO)
			throws Exception {
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet("select * from configuracaoHistorico "), usuarioVO);		
	}

	@Override
	public ConfiguracaoHistoricoVO consultarConfiguracaoHistoricoPorNivelEducacional(
			TipoNivelEducacional tipoNivelEducacional, UsuarioVO usuarioVO) throws Exception {
		if(tipoNivelEducacional != null) {
			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("select * from configuracaoHistorico where niveleducacional = ? ", tipoNivelEducacional.name());
			if(rs.next()) {
				return montarDados(rs, usuarioVO);
			}
		}
		return new ConfiguracaoHistoricoVO();
	}
	
	private List<ConfiguracaoHistoricoVO> montarDadosConsulta(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		List<ConfiguracaoHistoricoVO> configuracaoLayoutHistoricoVOs =  new ArrayList<ConfiguracaoHistoricoVO>(0);
		while(rs.next()) {
			configuracaoLayoutHistoricoVOs.add(montarDados(rs, usuarioVO));
		}
		return configuracaoLayoutHistoricoVOs;
		
	} 
	
	private ConfiguracaoHistoricoVO montarDados(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		ConfiguracaoHistoricoVO configuracaoHistoricoVO =  new ConfiguracaoHistoricoVO();
		configuracaoHistoricoVO.setNovoObj(false);
		configuracaoHistoricoVO.setCodigo(rs.getInt("codigo"));
		configuracaoHistoricoVO.setNivelEducacional(TipoNivelEducacional.valueOf(rs.getString("nivelEducacional")));
		configuracaoHistoricoVO.setConfiguracaoLayoutHistoricoVOs(getFacadeFactory().getConfiguracaoLayoutHistoricoFacade().consultarPorConfiguracaoHistorico(configuracaoHistoricoVO.getCodigo(), usuarioVO));
		configuracaoHistoricoVO.setConfiguracaoObservacaoHistoricoVOs(getFacadeFactory().getConfiguracaoObservacaoHistoricoFacade().consultarPorConfiguracaoHistorico(configuracaoHistoricoVO.getCodigo(), usuarioVO));		
		return configuracaoHistoricoVO;
	}
	
	@Override	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarInclusaoLayoutPadraoHistorico(TipoNivelEducacional tipoNivelEducacional, String caminhoBase) {
		try {
			ConfiguracaoHistoricoVO configuracaoHistoricoVO = consultarConfiguracaoHistoricoPorNivelEducacional(tipoNivelEducacional, null);
			if(!Uteis.isAtributoPreenchido(configuracaoHistoricoVO)) {
				configuracaoHistoricoVO =  new ConfiguracaoHistoricoVO();
				configuracaoHistoricoVO.setNivelEducacional(tipoNivelEducacional);
				persistir(configuracaoHistoricoVO, null);
			}
			getFacadeFactory().getConfiguracaoLayoutHistoricoFacade().realizarInclusaoLayoutPadraoHistorico(configuracaoHistoricoVO, caminhoBase);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
