package negocio.interfaces.basico;

import negocio.comuns.basico.ScriptExecutadoVO;

public interface ScriptExecutadoInterfaceFacade {

	void executarScripts() throws Exception;
	void alterarScriptNaoExecutadoParaExecutadoComSucesso(Integer codigo) throws Exception;
	void persistir(ScriptExecutadoVO obj) throws Exception;
}