package negocio.interfaces.contabil;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.contabil.LayoutIntegracaoTagVO;
import negocio.comuns.contabil.LayoutIntegracaoVO;

public interface LayoutIntegracaoTagInterfaceFacade {

	void persistir(List<LayoutIntegracaoTagVO> lista, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	void persistir(LayoutIntegracaoTagVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluir(LayoutIntegracaoTagVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	List<LayoutIntegracaoTagVO> consultaRapidaPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<LayoutIntegracaoTagVO> consultaRapidaPorTag(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<LayoutIntegracaoTagVO> consultaRapidaPorLayoutIntegracaoVO(LayoutIntegracaoVO obj, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	

}
