package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MapaEquivalenciaDisciplinaMatrizCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface MapaEquivalenciaDisciplinaMatrizCurricularInterfaceFacade {

	void incluirMapaEquivalenciaDisciplinaMatrizCurricularVOs(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws Exception;
	void alterarMapaEquivalenciaDisciplinaMatrizCurricularVOs(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws Exception;
	void excluirMapaEquivalenciaDisciplinaMatrizCurricularVOs(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, List<MapaEquivalenciaDisciplinaMatrizCurricularVO> mapaEquivalenciaDisciplinaMatrizCurricularVOs) throws Exception;
	
	void validarDados(MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO) throws ConsistirException;
	
	MapaEquivalenciaDisciplinaMatrizCurricularVO consultarPorChavePrimaria(Integer codigo) throws Exception;
	
	List<MapaEquivalenciaDisciplinaMatrizCurricularVO> consultarPorMapaEquivalenciaDisciplina(Integer mapaEquivalenciaDisciplina) throws Exception;
	
}
