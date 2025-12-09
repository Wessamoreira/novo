package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CriterioAvaliacaoDisciplinaEixoIndicadorVO;
import negocio.comuns.academico.CriterioAvaliacaoDisciplinaVO;
import negocio.comuns.academico.CriterioAvaliacaoPeriodoLetivoVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface CriterioAvaliacaoDisciplinaInterfaceFacade {

	void incluirCriterioAvaliacaoDisciplinaVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception;
	
	void alterarCriterioAvaliacaoDisciplinaVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception;
	
	void excluirCriterioAvaliacaoDisciplinaVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception;
	
	void validarDados(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO) throws ConsistirException;
	
	void adicionarCriterioAvaliacaoDisciplinaEixoIndicadorVO(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO, CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO) throws Exception;
	
	void removerCriterioAvaliacaoDisciplinaEixoIndicadorVO(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO, CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO) throws Exception;
		
	
	List<CriterioAvaliacaoDisciplinaVO> consultarPorCriterioAvaliacaoPeriodoLetivo(Integer criterioAvaliacaoPeriodoLetivo,Integer disciplina, int nivelMontarDados) throws Exception;

	void alterarOrdemCriterioAvaliacaoDisciplinaEixoIndicadorVO(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO, CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO, boolean subir) throws Exception;

	void realizarCalculoNotaCriterioAvaliacaoDisciplina(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplinaVO) throws Exception;
	
}
