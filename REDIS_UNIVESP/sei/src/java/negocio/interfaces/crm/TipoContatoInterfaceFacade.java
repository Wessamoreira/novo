package negocio.interfaces.crm;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.crm.TipoContatoVO;

public interface TipoContatoInterfaceFacade {
	
	void persistir(TipoContatoVO tipoContatoVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	void ativar(TipoContatoVO tipoContatoVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	void inativar(TipoContatoVO tipoContatoVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	TipoContatoVO consultarPorChavePrimaria(Integer codigo) throws Exception;
	
	List<TipoContatoVO> consultar(String descricao, StatusAtivoInativoEnum situacao, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception;

}
