package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeDisciplinaCompostaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;


public interface GradeCurricularGrupoOptativaDisciplinaInterfaceFacade {
	
	void incluirGradeCurricularGrupoOptativaDisciplinaVOs(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception;
	
	void alterarGradeCurricularGrupoOptativaDisciplinaVOs(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO, String situacaoGradeCurricular, UsuarioVO usuario) throws Exception;
	
	void excluirGradeCurricularGrupoOptativaDisciplinaVOs(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativaVO, UsuarioVO usuario) throws Exception;
	
	List<GradeCurricularGrupoOptativaDisciplinaVO> consultarPorGradeCurricularGrupoOptativa(Integer gradeCurricularGrupoOptativa, UsuarioVO usuarioVO) throws Exception;

	void validarDados(GradeCurricularGrupoOptativaDisciplinaVO obj) throws ConsistirException;	
	
	public void adicionarGradeDisciplinaCompostaVOs(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO, String situacaoGradeCurricular) throws Exception;
	
	public void removerGradeDisciplinaCompostaVOs(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO) throws Exception;
        
	GradeCurricularGrupoOptativaDisciplinaVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception;
     
	GradeCurricularGrupoOptativaDisciplinaVO consultarPorDisciplinaMatrizCurricularPeriodoLetivo(Integer codigoDisciplina, Integer matrizCurricular, UsuarioVO usuario) throws Exception;
	      
	Integer consultarPrimeiroPeriodoLetivoComDisciplinaGrupoOptativaMatrizCurricular(Integer codigoDisciplina, Integer matrizCurricular, Integer codigoPeriodoLetivoPrivilegiar, UsuarioVO usuario) throws Exception;

	void validarDadosFormulaCalculoComposicao(GradeCurricularGrupoOptativaDisciplinaVO obj) throws ConsistirException;
			
	void excluirObjDisciplinaPreRequisitoVOs(Integer preRequisito, GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, UsuarioVO usuario) throws Exception;
	
	public List<GradeCurricularGrupoOptativaDisciplinaVO> consultarPorMatrizCurricularGrupoOptativaComposta(Integer matrizCurricular, UsuarioVO usuario) throws Exception;

}
