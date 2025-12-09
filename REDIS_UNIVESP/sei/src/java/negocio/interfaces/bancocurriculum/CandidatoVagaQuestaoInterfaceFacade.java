package negocio.interfaces.bancocurriculum;

import java.util.List;

import negocio.comuns.bancocurriculum.CandidatoVagaQuestaoRespostaVO;
import negocio.comuns.bancocurriculum.CandidatoVagaQuestaoVO;
import negocio.comuns.bancocurriculum.CandidatosVagasVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface CandidatoVagaQuestaoInterfaceFacade {
	
	void incluirCandidatoVagaQuestao(CandidatosVagasVO candidatosVagasVO) throws Exception;
	
	void alteraCandidatoVagaQuestao(CandidatosVagasVO candidatosVagasVO) throws Exception;
	
	List<CandidatoVagaQuestaoVO> consultarPorCandidatoVaga(Integer candidatoVaga, Integer vaga) throws Exception;
	
	void validarDados(CandidatosVagasVO candidatosVagasVO) throws ConsistirException;

	void realizarVerificacaoQuestaoUnicaEscolha(CandidatosVagasVO candidatosVagasVO, CandidatoVagaQuestaoRespostaVO candidatoVagaQuestaoRespostaVO);

}
