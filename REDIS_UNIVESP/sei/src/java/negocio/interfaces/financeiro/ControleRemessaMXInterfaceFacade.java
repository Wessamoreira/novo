package negocio.interfaces.financeiro;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ControleRemessaMXVO;

public interface ControleRemessaMXInterfaceFacade {

	public void incluir(final ControleRemessaMXVO obj) throws Exception;
	public Integer consultarIncrementalPorContaCorrente(Integer contaCorrente, UsuarioVO usuarioVO) throws Exception;
	public void alterarIncrementalPorContaCorrente(final Integer contaCorrente, final Integer incremental, final Integer incrementalCP, UsuarioVO usuario) throws Exception;
	public void incluirControleRemessaMXContaCorrente(Integer contaCorrente, ControleRemessaMXVO obj, UsuarioVO usuarioVO) throws Exception;
	public void alterarControleRemessaMXContaCorrente(Integer contaCorrente, ControleRemessaMXVO obj, UsuarioVO usuarioVO) throws Exception;
	public ControleRemessaMXVO consultarPorContaCorrente(Integer contaCorrente, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;
	public Integer consultarIncrementalCPPorContaCorrente(Integer contaCorrente, UsuarioVO usuarioVO) throws Exception;
	
}
