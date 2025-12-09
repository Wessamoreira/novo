package negocio.interfaces.academico;

import java.util.List;
import java.util.Map;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaMatrizCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MapaEquivalenciaMatrizCurricularVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface MapaEquivalenciaDisciplinaInterfaceFacade {

	void incluirMapaEquivalenciaDisciplinaVOs(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO, UsuarioVO usuario) throws Exception;
	void alterarMapaEquivalenciaDisciplinaVOs(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO) throws Exception;
	void excluirMapaEquivalenciaDisciplinaVOs(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO, List<MapaEquivalenciaDisciplinaVO> mapaEquivalenciaDisciplinaVOs) throws Exception;
	
	void validarDados(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws ConsistirException;
	
	MapaEquivalenciaDisciplinaVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados) throws Exception;
	
	void adicionarMapaEquivalenciaDisciplinaMatrizCurricularVOs(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO, MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO, String tipoOrigemDisciplina, Integer codigoOrigemDisciplina) throws Exception;
	void removerMapaEquivalenciaDisciplinaMatrizCurricularVOs(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO) throws Exception;
	
	void adicionarMapaEquivalenciaDisciplinaCursadaVOs(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO, MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, MapaEquivalenciaDisciplinaCursadaVO  mapaEquivalenciaDisciplinaCursadaVO) throws Exception;
	void removerMapaEquivalenciaDisciplinaCursadaVOs(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO) throws Exception;
	
	void gerarNovoMapaEquivalenciaMatrizCurricularPorAtualizacaoGradeCurricularCursoIntegral(Map<String, Integer> map, CursoVO curso, GradeCurricularVO gradeCurricular, TurmaDisciplinaVO td, TurmaDisciplinaVO tdCorrespodente , UsuarioVO usuarioVO) throws Exception;
	
	List<MapaEquivalenciaDisciplinaVO> consultarPorMapaEquivalenciaMatrizCurricular(Integer mapaEquivalenciaMatrizCurricular, NivelMontarDados nivelMontarDados) throws Exception;
        
        public List<MapaEquivalenciaDisciplinaVO> consultarPorMapaEquivalenciaMatrizCurricularDisciplinaMatriz(
                Integer codigoMatrizCurricular, Integer codigoDisplinaMatriz, NivelMontarDados nivelMontarDados, boolean apenasEquivalenciaUmPraUm) throws Exception;
        
        public List<MapaEquivalenciaDisciplinaVO> consultarPorMapaEquivalenciaMatrizCurricularDisciplinaMatrizConsiderandoSituacao(
                Integer codigoMatrizCurricular, Integer codigoDisplinaMatriz, NivelMontarDados nivelMontarDados, boolean apenasEquivalenciaUmPraUm ,boolean apenasSituacaoAtiva) throws Exception;
        
        /**
         * Carrega os históricos das disciplinas envolvidas em um mapa de equivalencia, tando das disciplinas a serm
         * cursadas do mapa, quando das disciplinas da matriz curricular que serão aprovadas por meio do mapa.
         */
        public void carregarHistoricosMapaEquivalenciaParaAvaliacaoEResolucao(String matricula, Integer matrizCurricular,
            MapaEquivalenciaDisciplinaVO mapaEquivalencia, Integer numeroAgrupamentoEquivalenciaDisciplina, Integer historicoIgnorar,  UsuarioVO usuario) throws Exception;
        
        public MapaEquivalenciaDisciplinaVO consultarPorMapaEquivalenciaCursada(Integer codigoCursada, NivelMontarDados nivelMontarDados) throws Exception;
        
        public Integer consultarCodigoPorMapaEquivalenciaCursada(Integer codigoCursada, NivelMontarDados nivelMontarDados) throws Exception;
        
        public List<MapaEquivalenciaDisciplinaVO> consultarPorMapaEquivalenciaMatrizCurricularDisciplinaCursada(
                Integer codigoMatrizCurricular, Integer codigoDisplinaCursar, Integer cargaHoraria, NivelMontarDados nivelMontarDados) throws Exception;
		void adicionarMapaEquivalenciaDisciplinaMatrizCurricularVOs(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO, MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO) throws Exception;
        
        public void incluir(final MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws Exception;
		void realizarAtivacaoMapaEquivalenciaDisciplina(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, UsuarioVO usuarioVO) throws Exception;
		void realizarInativacaoMapaEquivalenciaDisciplina(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO, UsuarioVO usuarioVO) throws Exception;
                
        public List<MapaEquivalenciaDisciplinaVO> consultarPorMapaEquivalenciaMatrizCurricularDisciplinaCursada(
                Integer codigoMatrizCurricular, Integer codigoDisplinaCursar, Integer cargaHoraria, Integer cogigoMapaEquivalenciaMatrizCurricula,NivelMontarDados nivelMontarDados) throws Exception;

        public List<MapaEquivalenciaDisciplinaVO> consultarPorMapaEquivalenciaMatrizCurricularDisciplinaMatriz(Integer codigoMatrizCurricular, Integer codigoDisplinaMatriz, Integer codigoMapaEquivalencia, NivelMontarDados nivelMontarDados) throws Exception;        

}
