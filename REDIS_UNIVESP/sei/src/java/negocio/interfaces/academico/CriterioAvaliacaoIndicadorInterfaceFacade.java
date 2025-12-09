package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CriterioAvaliacaoDisciplinaEixoIndicadorVO;
import negocio.comuns.academico.CriterioAvaliacaoIndicadorVO;
import negocio.comuns.academico.CriterioAvaliacaoPeriodoLetivoVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface CriterioAvaliacaoIndicadorInterfaceFacade {
	
	void incluirCriterioAvaliacaoIndicadorPorEixoIndicadorVO(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO) throws Exception;
	
	void alterarCriterioAvaliacaoIndicadorPorEixoIndicadorVO(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO) throws Exception;
	
	void excluirCriterioAvaliacaoIndicadorPorEixoIndicadorVO(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicadorVO) throws Exception;
	
	void incluirCriterioAvaliacaoIndicadorPorPeriodoLetivoVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception;
	
	void alterarCriterioAvaliacaoIndicadorPorPeriodoLetivoVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception;
	
	void excluirCriterioAvaliacaoIndicadorPorPeriodoLetivoVO(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO) throws Exception;
	
	void validarDados(CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicadorVO ) throws ConsistirException;
	
	List<CriterioAvaliacaoIndicadorVO> consultarPorCriterioAvalicaoDisciplinaPorEixoIndicador(Integer criterioAvaliacaoDisciplinaEixoIndicador) throws Exception;
	
	List<CriterioAvaliacaoIndicadorVO> consultarPorCriterioAvalicaoDisciplinaPorPeriodoLetivo(Integer criterioAvaliacaoPeriodoLetivo) throws Exception;
	
	

}
