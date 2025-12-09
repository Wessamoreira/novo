package negocio.facade.jdbc.administrativo;

import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoSeiBlackboardDominioVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.ConfiguracaoSeiBlackboardDominioInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class ConfiguracaoSeiBlackboardDominio extends ControleAcesso implements ConfiguracaoSeiBlackboardDominioInterfaceFacade{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6332719918276312663L;
	private static String idEntidade = "ConfiguracaoSeiBlackboard";
	
	public static String getIdEntidade() {
		return ConfiguracaoSeiBlackboardDominio.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		ConfiguracaoSeiBlackboardDominio.idEntidade = idEntidade;
	}
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void validarDados(ConfiguracaoSeiBlackboardDominioVO obj) {
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getTipoUsuarioBlackboard()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiBlackboard_tipoUsuarioBlackboard"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getPermissaoSistema()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiBlackboard_permissaoSistema"));
		Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getPermissaoInstitucional()), UteisJSF.internacionalizar("msg_ConfiguracaoSeiBlackboard_permissaoInstitucional"));
		Uteis.checkState(obj.getPermissaoSistema().split(";").length != obj.getRoleIdSistema().split(";").length, "A quantidade de permissão do sistema não é igual a quantidade de regras do sistema.");
		Uteis.checkState(obj.getPermissaoInstitucional().split(";").length != obj.getRoleIdInstitucional().split(";").length, "A quantidade de permissão institucional não é igual a quantidade de regras institucional.");
	}	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void incluir(final ConfiguracaoSeiBlackboardDominioVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			ConfiguracaoSeiGsuite.incluir(getIdEntidade(), verificarAcesso, usuario);
			incluir(obj, "ConfiguracaoSeiBlackboardDominio", new AtributoPersistencia()
					.add("configuracaoSeiBlackboard", obj.getConfiguracaoSeiBlackboardVO())
					.add("tipoUsuarioBlackboard", obj.getTipoUsuarioBlackboard())						
					.add("permissaoSistema", obj.getPermissaoSistema())
					.add("roleIdSistema", obj.getRoleIdSistema()) 
					.add("permissaoInstitucional", obj.getPermissaoInstitucional())
					.add("roleIdInstitucional", obj.getRoleIdInstitucional()),
					usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			obj.setNovoObj(Boolean.TRUE);
			obj.setCodigo(0);
			throw new StreamSeiException(e);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void alterar(final ConfiguracaoSeiBlackboardDominioVO obj, boolean verificarAcesso, UsuarioVO usuario) {
		try {
			ConfiguracaoSeiGsuite.alterar(getIdEntidade(), verificarAcesso, usuario);
			alterar(obj, "ConfiguracaoSeiBlackboardDominio", new AtributoPersistencia()
					.add("configuracaoSeiBlackboard", obj.getConfiguracaoSeiBlackboardVO())
					.add("tipoUsuarioBlackboard", obj.getTipoUsuarioBlackboard())			
					.add("permissaoSistema", obj.getPermissaoSistema())
					.add("roleIdSistema", obj.getRoleIdSistema()) 
					.add("permissaoInstitucional", obj.getPermissaoInstitucional())
					.add("roleIdInstitucional", obj.getRoleIdInstitucional()), 
					new AtributoPersistencia()
					.add("codigo", obj.getCodigo()), usuario);
			obj.setNovoObj(Boolean.FALSE);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void persistir(List<ConfiguracaoSeiBlackboardDominioVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) {
		try {
			for (ConfiguracaoSeiBlackboardDominioVO obj : lista) {
				validarDados(obj);
				if (!Uteis.isAtributoPreenchido(obj)) {
					incluir(obj, verificarAcesso, usuarioVO);
				} else {
					alterar(obj, verificarAcesso, usuarioVO);
				}
			}	
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
	

}
