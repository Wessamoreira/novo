package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface MapaEquivalenciaDisciplinaCursadaInterfaceFacade {

	void incluirMapaEquivalenciaDisciplinaCursadaVOs(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws Exception;
	void alterarMapaEquivalenciaDisciplinaCursadaVOs(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws Exception;
	void excluirMapaEquivalenciaDisciplinaCursadaVOs(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, List<MapaEquivalenciaDisciplinaCursadaVO> mapaEquivalenciaDisciplinaCursadaVOs) throws Exception;
	
	void validarDados(MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO) throws ConsistirException;
	
	MapaEquivalenciaDisciplinaCursadaVO consultarPorChavePrimaria(Integer codigo) throws Exception;
	List<MapaEquivalenciaDisciplinaCursadaVO> consultarPorMapaEquivalenciaDisciplina(Integer mapaEquivalenciaDisciplina) throws Exception;
	public MapaEquivalenciaDisciplinaCursadaVO consultarPorMapaEquivalenciaDisciplinaEDisciplina(Integer mapaEquivalenciaDisciplina, Integer disciplina) throws Exception;
	
}
