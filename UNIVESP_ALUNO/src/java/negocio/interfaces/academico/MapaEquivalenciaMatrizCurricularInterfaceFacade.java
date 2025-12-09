package negocio.interfaces.academico;

import java.util.List;
import java.util.Map;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MapaEquivalenciaMatrizCurricularVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface MapaEquivalenciaMatrizCurricularInterfaceFacade {

	void persistir(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;
	
	List<MapaEquivalenciaMatrizCurricularVO> consultarPorCurso(String curso, int nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioLogado, Integer limite, Integer pagina, String campoConsultaSituacao,  String campoConsultaDisciplina) throws Exception;
	
	List<MapaEquivalenciaMatrizCurricularVO> consultarPorGradeCurricular(String gradeCurricular, int nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioLogado, Integer limite, Integer pagina, String campoConsultaSituacao,  String campoConsultaDisciplina) throws Exception;
        
        public List<MapaEquivalenciaMatrizCurricularVO> consultarPorCodigoGradeCurricular(Integer gradeCurricular, int nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;
	
	MapaEquivalenciaMatrizCurricularVO consultarPorChavePrimaria(Integer codigo, NivelMontarDados nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;
	
	MapaEquivalenciaMatrizCurricularVO consultarPorGradeCurricularSituacaoAtiva(Integer gradeCurricular,  NivelMontarDados nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;
	
	void validarDados(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO) throws ConsistirException;
	
	void realizarAtivacaoMapaEquivalencia(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO,  boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	void realizarInativacaoMapaEquivalencia(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	void adicionarMapaEquivalenciaDisciplina(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO, MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws Exception;
	
	void removerMapaEquivalenciaDisciplina(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO, MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) throws Exception;

	void excluir(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO, boolean validarAcesso, UsuarioVO usuarioLogado) throws Exception;

	List<MapaEquivalenciaMatrizCurricularVO> consultar(String campoConsulta, String valorConsulta, int nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioLogado, Integer limite, Integer pagina, String campoConsultaSituacao, String campoConsultaDisciplina) throws Exception;

	Integer consultarTotalRegistroEncontrado(String campoConsulta, String valorConsulta, String campoConsultaSituacao, String campoConsultaDisciplina) throws Exception;

	List<DisciplinaVO> realizarVerificacaoDisciplinaNaoRealizadoEquivalencia(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO) throws Exception;

	List<MapaEquivalenciaMatrizCurricularVO> consultarPorDescricao(String descricao, int nivelMontarDados, boolean validarAcesso, UsuarioVO usuarioLogado, Integer limite, Integer pagina, String campoConsultaSituacao,  String campoConsultaDisciplina) throws Exception;
	
	public Map<String, Integer> validarSeExisteEquivalenciaParaReposicaoComTrocaAlteracaoGradeCurricular(Integer gradeCurricular, Integer disciplina, Integer novaDisciplina) throws Exception; 

	void clonar(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaVO, UsuarioVO usuarioLogado) throws Exception;

	Boolean realizarVerificacaoDisciplinaEquivalente(Integer matrizCurricular, Integer disciplina) throws Exception;
	
	
}
