package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.ConfiguracaoAtaResultadosFinaisVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.ConfiguracaoAtaResultadosFinaisInterfaceFacade;

@Repository
@Scope("singleton")
public class ConfiguracaoAtaResultadosFinais extends ControleAcesso implements ConfiguracaoAtaResultadosFinaisInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2233121237700641766L;
	private static final String nomeTabela = "configuracaoAtaResultadosFinais";
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO, UsuarioVO usuarioVO) throws Exception {
		if(!Uteis.isAtributoPreenchido(configuracaoAtaResultadosFinaisVO.getCodigo())) {
			incluir(configuracaoAtaResultadosFinaisVO, nomeTabela , new AtributoPersistencia(), usuarioVO);
		}else {
			alterar(configuracaoAtaResultadosFinaisVO, nomeTabela , new AtributoPersistencia(), new AtributoPersistencia().add("codigo", configuracaoAtaResultadosFinaisVO.getCodigo()), usuarioVO);
		}		
		
		getFacadeFactory().getConfiguracaoLayoutAtaResultadosFinaisFacade().persistir(configuracaoAtaResultadosFinaisVO, usuarioVO);
	}


	@Override
	public List<ConfiguracaoAtaResultadosFinaisVO> consultarConfiguracoesAtaResultadosFinais(UsuarioVO usuarioVO)
			throws Exception {
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet("select * from configuracaoAtaResultadosFinais "), usuarioVO);		
	}
	
	@Override
	public ConfiguracaoAtaResultadosFinaisVO consultarConfiguracaoAtaResultadosFinais(UsuarioVO usuarioVO) throws Exception {

			SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet("select * from configuracaoAtaResultadosFinais");
			if(rs.next()) {
				return montarDados(rs, usuarioVO);
			}
		
		return new ConfiguracaoAtaResultadosFinaisVO();
	}
	


	
	private List<ConfiguracaoAtaResultadosFinaisVO> montarDadosConsulta(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		List<ConfiguracaoAtaResultadosFinaisVO> configuracaoAtaResultadosFinaisVOs =  new ArrayList<ConfiguracaoAtaResultadosFinaisVO>(0);
		while(rs.next()) {
			configuracaoAtaResultadosFinaisVOs.add(montarDados(rs, usuarioVO));
		}
		return configuracaoAtaResultadosFinaisVOs;
		
	} 
	
	private ConfiguracaoAtaResultadosFinaisVO montarDados(SqlRowSet rs, UsuarioVO usuarioVO) throws Exception{
		ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO =  new ConfiguracaoAtaResultadosFinaisVO();
		configuracaoAtaResultadosFinaisVO.setNovoObj(false);
		configuracaoAtaResultadosFinaisVO.setCodigo(rs.getInt("codigo"));
		configuracaoAtaResultadosFinaisVO.setConfiguracaoLayoutAtaResultadosFinaisVOs(getFacadeFactory().getConfiguracaoLayoutAtaResultadosFinaisFacade().consultarPorConfiguracaoAtaResultadosFinais(configuracaoAtaResultadosFinaisVO.getCodigo(), usuarioVO));
		return configuracaoAtaResultadosFinaisVO;
	}
	
	@Override	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void realizarInclusaoLayoutPadraoAtaResultadosFinais(String caminhoBase) {
		try {
			ConfiguracaoAtaResultadosFinaisVO configuracaoAtaResultadosFinaisVO = consultarConfiguracaoAtaResultadosFinais(null);
			if(!Uteis.isAtributoPreenchido(configuracaoAtaResultadosFinaisVO)) {
				configuracaoAtaResultadosFinaisVO =  new ConfiguracaoAtaResultadosFinaisVO();

				persistir(configuracaoAtaResultadosFinaisVO, null);
			}
			getFacadeFactory().getConfiguracaoLayoutAtaResultadosFinaisFacade().realizarInclusaoLayoutPadraoAtaResultadosFinais(configuracaoAtaResultadosFinaisVO, caminhoBase);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
