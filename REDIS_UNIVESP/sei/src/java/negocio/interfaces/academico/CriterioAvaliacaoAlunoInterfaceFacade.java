package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.CriterioAvaliacaoAlunoVO;
import negocio.comuns.academico.CriterioAvaliacaoPeriodoLetivoVO;
import negocio.comuns.academico.CriterioAvaliacaoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public interface CriterioAvaliacaoAlunoInterfaceFacade {

	void persistir(CriterioAvaliacaoAlunoVO criterioAvaliacaoAlunoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	
	void incluirCriterioAvaliacaoAlunoVO(CriterioAvaliacaoVO criterioAvaliacaoVO, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;
	


	List<CriterioAvaliacaoVO> consultarCriterioAvaliacaoAlunoResponder(String consultarPor, Integer matriculaPeriodo, MatriculaVO matricula, Integer disciplina, String situacao, TurmaVO turma, String ano, String semestre, Integer unidadeEnsino, Integer gradeCurricular, boolean ordenarVisaoPais, boolean visaoProfessor, NivelMontarDados nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<CriterioAvaliacaoVO> consultarCriterioAvaliacaoAlunoNaoRespondido(String consultarPor, Integer matriculaPeriodo,String matricula, Integer disciplina, Integer turma, String ano, String semestre, Integer unidadeEnsino, Integer gradeCurricular,  boolean ordenarVisaoPais, boolean visaoProfessor, NivelMontarDados nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	List<CriterioAvaliacaoVO> consultarCriterioAvaliacaoAlunoRespondido(String consultarPor, Integer matriculaPeriodo, String matricula, Integer disciplina, Integer turma, String ano, String semestre, Integer unidadeEnsino, Integer gradeCurricular, boolean ordenarVisaoPais, boolean visaoProfessor, NivelMontarDados nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void excluirCriterioAvaliacaoAlunoVO(CriterioAvaliacaoVO criterioAvaliacaoVO, List<CriterioAvaliacaoPeriodoLetivoVO> criterioAvaliacaoPeriodoLetivoVOs, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	void selecionarNotaConceito(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO, Integer criterioAvaliacaoDisciplina, Integer criterioAvaliacaoEixoIndicador, Integer criterioAvaliacaoIndicador, String origem, Integer nota, Integer criterioAvaliacaoNotaConceito) throws Exception;

	void realizacaoCriacaoOpcaoNotaConceito(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception;

}
