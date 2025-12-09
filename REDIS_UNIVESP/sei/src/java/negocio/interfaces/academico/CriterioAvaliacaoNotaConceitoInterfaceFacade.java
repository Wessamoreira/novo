package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CriterioAvaliacaoNotaConceitoVO;
import negocio.comuns.academico.CriterioAvaliacaoPeriodoLetivoVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface CriterioAvaliacaoNotaConceitoInterfaceFacade {

	void incluirCriterioAvaliacaoNotaConceito(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception;
	
	void alterarCriterioAvaliacaoNotaConceito(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception;
	
	void excluirCriterioAvaliacaoNotaConceito(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception;
	
	List<CriterioAvaliacaoNotaConceitoVO> consultarPorCriterioAvaliacaoPeriodoLetivo(Integer criterioAvaliacaoPeriodoLetivo) throws Exception;
	
	void validarDados(CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceitoVO) throws ConsistirException;
	
}
