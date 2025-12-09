package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.DisciplinaPreRequisitoVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.enumeradores.FormulaCalculoNotaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;

public interface GradeDisciplinaCompostaInterfaceFacade {

	void persistir(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO, String situacaoGradeCurricular, FormulaCalculoNotaEnum formulaCalculoNota, UsuarioVO usuario) throws Exception;
	
	void validarDados(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO, String situacaoGradeCurricular, FormulaCalculoNotaEnum formulaCalculoNota) throws ConsistirException;
	
	void incluirGradeDisciplinaCompostaVOsPorGradeDisciplina(GradeDisciplinaVO gradeDisciplinaVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception;
	
	void incluirGradeDisciplinaCompostaVOsPorGrupoOptativaDisciplina(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception;
	
	void alterarGradeDisciplinaCompostaVOsPorGradeDisciplina(GradeDisciplinaVO gradeDisciplinaVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception;
	
	void alterarGradeDisciplinaCompostaVOsPorGrupoOptativaDisciplina(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception;
	
	void excluirGradeDisciplinaCompostaVOsPorGradeDisciplina(GradeDisciplinaVO gradeDisciplinaVO, UsuarioVO usuario) throws Exception;
	
	void excluirGradeDisciplinaCompostaVOsPorGrupoOptativaDisciplina(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, UsuarioVO usuario) throws Exception;
	
	List<GradeDisciplinaCompostaVO> consultarPorGradeDisciplina(Integer gradeDisciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	List<GradeDisciplinaCompostaVO> consultarPorGrupoOptativaDisciplina(Integer grupoOptativaDisciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	GradeDisciplinaCompostaVO consultarPorChavePrimaria(Integer gradeDisciplinaComposta, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	void executarCalcularCargaHorariaTeoricaDisciplinaComposta(Integer cargaHorariaDisciplinaPrincipal, List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs, GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO) throws Exception;

	void alterarOrdemGradeDisciplinaComposta(List<GradeDisciplinaCompostaVO> gradeDisciplinaCompostaVOs, GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO, boolean subir) throws Exception;

	Boolean realizaVerificacaoDisciplinaEComposta(Integer turma, Integer disciplina) throws Exception;

	List<GradeDisciplinaCompostaVO> consultarPorTurmaDisciplina(Integer turma, Integer disciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Wellington - 18 de jan de 2016 
	 * @param disciplinaPreRequisitoVOs
	 * @param obj
	 * @throws Exception 
	 */
	void adicionarDisciplinaPreRequisitoVOs(List<DisciplinaPreRequisitoVO> disciplinaPreRequisitoVOs, DisciplinaPreRequisitoVO obj) throws Exception;

	/** 
	 * @author Wellington - 18 de jan de 2016 
	 * @param disciplinaPreRequisitoVOs
	 * @param obj
	 * @throws Exception 
	 */
	void removerDisciplinaPreRequisitoVOs(List<DisciplinaPreRequisitoVO> disciplinaPreRequisitoVOs, DisciplinaPreRequisitoVO obj, UsuarioVO usuario) throws Exception;

	public List<GradeDisciplinaCompostaVO> consultarPorGradeCurricular(Integer gradeCurricular, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public Boolean consultarPorCodigoDisciplinaECargaHorariaDisciplinaFazParteComposicao(Integer codigoDisciplina, Integer gradeCurricular, UsuarioVO usuario) throws Exception;

	Boolean consultarGradeControlaRecuperacaoPorGradeDisciplinaComposta(Integer gradeDisciplinaComposta)
			throws Exception;

	GradeDisciplinaCompostaVO consultarPorCodigoDisciplinaEMatriz(Integer codigoDisciplina, Integer gradeCurricular,
			UsuarioVO usuario) throws Exception;
	
}
