package negocio.interfaces.arquitetura;

import negocio.comuns.arquitetura.TratamentoErroVO;
import negocio.comuns.arquitetura.UsuarioVO;

public interface TratamentoErroInterfaceFacade {

	TratamentoErroVO inicializarDadosTratamentoErro(String mensagemErro, UsuarioVO usuarioVO);

	void persistirTratamentoErroPorMensagem(String mensagemErro, UsuarioVO usuarioVO);

	void incluir(TratamentoErroVO obj, boolean verificarPermissao, UsuarioVO usuario) throws Exception;

}
