package negocio.interfaces.bancocurriculum;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.OpcaoRespostaVagaQuestaoVO;
import negocio.comuns.bancocurriculum.VagaQuestaoVO;
import negocio.comuns.bancocurriculum.VagasVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface VagaQuestaoInterfaceFacade {
	
	void incluirVagaQuestao(VagasVO vaga, UsuarioVO usuarioVO) throws Exception;
	
	void alterarVagaQuestao(VagasVO vaga, UsuarioVO usuarioVO) throws Exception;
	
	List<VagaQuestaoVO> consultarPorVagas(Integer vaga) throws Exception;
	
	void alterarOrdemOpcaoRespostaVagaQuestao(VagaQuestaoVO vagaQuestaoVO, OpcaoRespostaVagaQuestaoVO opc1, OpcaoRespostaVagaQuestaoVO opc2) throws Exception;

	void adicionarOrdemOpcaoRespostaVagaQuestao(VagaQuestaoVO vagaQuestaoVO, OpcaoRespostaVagaQuestaoVO opc1, Boolean validarDados) throws Exception;

	void removerOrdemOpcaoRespostaVagaQuestao(VagaQuestaoVO vagaQuestaoVO, OpcaoRespostaVagaQuestaoVO opc1)  throws Exception;

	void validarDados(VagaQuestaoVO vagaQuestaoVO) throws ConsistirException;

	VagaQuestaoVO novo() throws Exception;

}
