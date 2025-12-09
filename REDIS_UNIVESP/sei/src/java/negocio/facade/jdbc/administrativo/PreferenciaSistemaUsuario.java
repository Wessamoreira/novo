package negocio.facade.jdbc.administrativo;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.administrativo.ConfiguracaoAparenciaSistemaVO;
import negocio.comuns.administrativo.PreferenciaSistemaUsuarioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.PreferenciaSistemaUsuarioInterfaceFacade;

@Repository
@Lazy
@Scope("singleton")
public class PreferenciaSistemaUsuario extends ControleAcesso implements PreferenciaSistemaUsuarioInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6688829638045484905L;

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(PreferenciaSistemaUsuarioVO preferenciaSistemaUsuarioVO) throws Exception {
		if(!Uteis.isAtributoPreenchido(preferenciaSistemaUsuarioVO.getCodigo())) {
			incluir(preferenciaSistemaUsuarioVO);
		}else {
			alterar(preferenciaSistemaUsuarioVO);
		}
		preferenciaSistemaUsuarioVO.setCssFonte(null);
		if(Uteis.isAtributoPreenchido(preferenciaSistemaUsuarioVO.getConfiguracaoAparenciaSistemaVO())) {
			preferenciaSistemaUsuarioVO.setConfiguracaoAparenciaSistemaVO(getFacadeFactory().getConfiguracaoAparenciaSistemaFacade().consultarPorChavePrimaria(preferenciaSistemaUsuarioVO.getConfiguracaoAparenciaSistemaVO().getCodigo(), false, preferenciaSistemaUsuarioVO.getUsuarioVO()));
		}else {
			preferenciaSistemaUsuarioVO.setConfiguracaoAparenciaSistemaVO(new ConfiguracaoAparenciaSistemaVO());
		}
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(PreferenciaSistemaUsuarioVO preferenciaSistemaUsuarioVO) throws Exception {
		incluir(preferenciaSistemaUsuarioVO, "preferenciaSistemaUsuario", new  AtributoPersistencia()
				.add("tamanhoFonte", preferenciaSistemaUsuarioVO.getTamanhoFonte())
				.add("usuario", preferenciaSistemaUsuarioVO.getUsuarioVO())
				.add("configuracaoAparenciaSistema", preferenciaSistemaUsuarioVO.getConfiguracaoAparenciaSistemaVO()), preferenciaSistemaUsuarioVO.getUsuarioVO());
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(PreferenciaSistemaUsuarioVO preferenciaSistemaUsuarioVO) throws Exception {
		alterar(preferenciaSistemaUsuarioVO, "preferenciaSistemaUsuario", new  AtributoPersistencia()
				.add("tamanhoFonte", preferenciaSistemaUsuarioVO.getTamanhoFonte())				
				.add("configuracaoAparenciaSistema", preferenciaSistemaUsuarioVO.getConfiguracaoAparenciaSistemaVO()),
				 new AtributoPersistencia().add("usuario", preferenciaSistemaUsuarioVO.getUsuarioVO()), preferenciaSistemaUsuarioVO.getUsuarioVO());
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void excluir(PreferenciaSistemaUsuarioVO preferenciaSistemaUsuarioVO) throws Exception {
		getConexao().getJdbcTemplate().update("delete from preferenciaSistemaUsuario where usuario = ?  ", preferenciaSistemaUsuarioVO.getUsuarioVO().getCodigo());

	}

	@Override
	public PreferenciaSistemaUsuarioVO consultarPorUsuario(UsuarioVO usuarioVO) throws Exception {		
		SqlRowSet rs =  getConexao().getJdbcTemplate().queryForRowSet("select * from  preferenciaSistemaUsuario where usuario = ? ", usuarioVO.getCodigo());
		PreferenciaSistemaUsuarioVO preferenciaSistemaUsuarioVO =  new PreferenciaSistemaUsuarioVO();
		if(rs.next()) {
			preferenciaSistemaUsuarioVO.setCodigo(rs.getInt("codigo"));
			preferenciaSistemaUsuarioVO.setTamanhoFonte(rs.getInt("tamanhoFonte"));
			preferenciaSistemaUsuarioVO.getConfiguracaoAparenciaSistemaVO().setCodigo(rs.getInt("configuracaoAparenciaSistema"));
			preferenciaSistemaUsuarioVO.getUsuarioVO().setCodigo(rs.getInt("usuario"));
			preferenciaSistemaUsuarioVO.setNovoObj(false);
			if(Uteis.isAtributoPreenchido(preferenciaSistemaUsuarioVO.getConfiguracaoAparenciaSistemaVO())) {
				preferenciaSistemaUsuarioVO.setConfiguracaoAparenciaSistemaVO(getFacadeFactory().getConfiguracaoAparenciaSistemaFacade().consultarPorChavePrimaria(preferenciaSistemaUsuarioVO.getConfiguracaoAparenciaSistemaVO().getCodigo(), false, usuarioVO));
			}
		}
		return preferenciaSistemaUsuarioVO;
	}

}
