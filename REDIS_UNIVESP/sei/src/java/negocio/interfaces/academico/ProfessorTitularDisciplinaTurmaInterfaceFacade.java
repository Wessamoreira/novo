package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import relatorio.negocio.comuns.academico.HistoricoTurmaRelVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface ProfessorTitularDisciplinaTurmaInterfaceFacade {

    public ProfessorTitularDisciplinaTurmaVO novo() throws Exception;

    public void incluir(ProfessorTitularDisciplinaTurmaVO obj, UsuarioVO usuario) throws Exception;

    public void alterar(ProfessorTitularDisciplinaTurmaVO obj, UsuarioVO usuario) throws Exception;

    public void excluir(ProfessorTitularDisciplinaTurmaVO obj, UsuarioVO usuario) throws Exception;

    public ProfessorTitularDisciplinaTurmaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorIdentificadorTurmaProfessor(String valorConsulta, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigo(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorData(Date prmIni, Date prmFim, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeDisciplina(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoDisciplina(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCargaHoraria(Integer valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorConteudo(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoTurma(Integer valorConsulta, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorIdentificadorTurma(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeProfessor(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeCurso(String valorConsulta, String semestre, String ano, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeCursoProfessor(String valorConsulta, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorIdentificadorTurmaProfessorDisciplina(String valorConsulta, String semestre, String ano, Integer professor, Integer disciplina, Integer unidadeEnsino,
            boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorNomeDisciplinaProfessor(String valorConsulta, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorDataProfessor(Date prmIni, Date prmFim, String semestre, String ano, Integer professor, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void setIdEntidade(String aIdEntidade);

    public List<PessoaVO> montarListaProfessorRegistroAula(HorarioTurmaVO obj, ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO, UsuarioVO usuario);

    public List montarProfessoresTitularDisciplinaTurma(ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List consultarPorCodigoDisciplinaCodigoTurma(Integer valorConsulta, Integer turma, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void validarConsultaDoUsuario(UsuarioVO usuario) throws Exception;

    public ProfessorTitularDisciplinaTurmaVO consultarUltimoRegistroAulaPorMatricula(String matricula, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;

    public void incluirProfessorTitularDisciplinaTurma(TurmaVO turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataAula, UsuarioVO usuario) throws Exception;

    public void alterarProfessorTitularDisciplinaTurma(TurmaVO turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataAula, UsuarioVO usuario) throws Exception;

    public void alterarListaProfessoresTitularDisciplinaTurma(List<ProfessorTitularDisciplinaTurmaVO> professoresTitularDisciplinaTurma, String tipoDefinicaoProfessor, UsuarioVO usuario) throws Exception;

    public ProfessorTitularDisciplinaTurmaVO montarProfessoresTitularDisciplinaTurmaTitular(ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public ProfessorTitularDisciplinaTurmaVO consultarPorProfessorDisciplinaTurmaAnoSemestre(Integer codProfessor, Integer codDisciplina, Integer codTurma, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void incluirProfessorTitularDisciplinaTurma(PessoaVO professorVO, DisciplinaVO disciplinaVO, TurmaVO turmaVO, UsuarioVO usuario) throws Exception;

    public ProfessorTitularDisciplinaTurmaVO consultarPorDisciplinaTurmaAnoSemestre(Integer codDisciplina, Integer codTurma, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<ProfessorTitularDisciplinaTurmaVO> montarProfessoresComProgramacaoAulaDisciplinaTurma(Integer codTurma, Integer codCurso, Integer codDisciplina, String ano, String semestre, int nivelMontarDados, UsuarioVO usuario) throws Exception;

    public void incluirProfessorTitularDisciplinaTurma(PessoaVO professorVO, DisciplinaVO disciplinaVO, TurmaVO turmaVO, String semestre, String ano, UsuarioVO usuario) throws Exception;

    public ProfessorTitularDisciplinaTurmaVO montarProfessoresTitularDisciplinaTurmaAgrupadaTitular(ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO, Boolean liberarRegistroAulaEntrePeriodo, Boolean retornarExcessao, int nivelMontarDados, String matricula, UsuarioVO usuario) throws Exception;

    public void excluirComBaseNaProgramacaoAula(Integer turma, Integer professor, Integer disciplina, String semestre, String ano, UsuarioVO usuario) throws Exception;

    public void excluirComBaseNaProgramacaoAulaSemDisciplina(Integer turma, Integer professor, String semestre, String ano, UsuarioVO usuario) throws Exception;

    public void excluirTodosRegistrosTurmaComBaseNaProgramacaoAula(Integer turma, String semestre, String ano, UsuarioVO usuario) throws Exception;
    
    public void alterarDisciplinaEquivalenteTurmaAgrupada(final Integer disciplinaEquivalenteTurmaAgrupada, final Integer turma, final Integer professor, final Integer disciplina, final String ano, final String semestre, UsuarioVO usuario) throws Exception;
    
    public ProfessorTitularDisciplinaTurmaVO executarObterDadosProfessorTitularDisciplinaTurma(List<ProfessorTitularDisciplinaTurmaVO> listaProfessores) throws Exception;

	List<ProfessorTitularDisciplinaTurmaVO> consultarProfessoresDisciplinaTurma(TurmaVO turmaVO, Integer disciplina, String ano, String semestre, Boolean retornarExcessao, Boolean liberarRegistroAulaEntrePeriodo, String matricula, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void alterarPessoaProfessorTitularDisciplinaTurmaUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova) throws Exception;
	
	void alterarProfessorTitularDisciplinaTurmaPorAlteracaoGradeCurricularCursoIntegral(Integer turma, Integer disciplina, String ano, String semestre, Integer novaDisciplina, UsuarioVO usuario) throws Exception;

	void excluirPorCodigoDisciplinaTurma(Integer turma, Integer disciplina, String ano, String semestre, UsuarioVO usuario) throws Exception;
	
	public String consultarProfessorTitularTurma(HistoricoTurmaRelVO historicoTurmaRelVO) throws Exception;
	
}
