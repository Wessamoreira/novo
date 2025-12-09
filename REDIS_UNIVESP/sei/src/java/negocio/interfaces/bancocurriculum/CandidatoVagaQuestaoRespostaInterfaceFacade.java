package negocio.interfaces.bancocurriculum;

import java.util.List;

import negocio.comuns.bancocurriculum.CandidatoVagaQuestaoRespostaVO;
import negocio.comuns.bancocurriculum.CandidatoVagaQuestaoVO;

public interface CandidatoVagaQuestaoRespostaInterfaceFacade {
	
	void incluirCandidatoVagaQuestaoResposta(CandidatoVagaQuestaoVO candidatoVagaQuestaoVO) throws Exception;
	
	void alteraCandidatoVagaQuestaoResposta(CandidatoVagaQuestaoVO candidatoVagaQuestaoVO) throws Exception;
	
	List<CandidatoVagaQuestaoRespostaVO> consultarPorCandidatoVagaQuestao(Integer candidatoVagaQuestao, Integer vagaQuestao) throws Exception;
	
	
}
