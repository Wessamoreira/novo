package negocio.interfaces.bancocurriculum;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.bancocurriculum.OpcaoRespostaVagaQuestaoVO;
import negocio.comuns.bancocurriculum.VagaQuestaoVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface OpcaoRespostaVagaQuestaoInterfaceFacade {

	void incluirOpcaoRespostaVagaQuestao(VagaQuestaoVO vagaQuestaoVO, UsuarioVO usuarioVO) throws Exception;

	void alteraOpcaoRespostaVagaQuestao(VagaQuestaoVO vagaQuestaoVO, UsuarioVO usuarioVO) throws Exception;

	List<OpcaoRespostaVagaQuestaoVO> consultarPorVagaQuestao(Integer vagaQuestao) throws Exception;

	void validarDados(OpcaoRespostaVagaQuestaoVO opcaoRespostaVagaQuestaoVO) throws ConsistirException;

}
