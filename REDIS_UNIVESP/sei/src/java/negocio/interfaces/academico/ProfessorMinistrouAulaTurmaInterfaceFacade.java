package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.ProfessorMinistrouAulaTurmaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface ProfessorMinistrouAulaTurmaInterfaceFacade {

	public ProfessorMinistrouAulaTurmaVO novo() throws Exception;

	public void incluir(ProfessorMinistrouAulaTurmaVO obj, UsuarioVO usuario) throws Exception;

	public void alterar(ProfessorMinistrouAulaTurmaVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(ProfessorMinistrouAulaTurmaVO obj,UsuarioVO usuario) throws Exception;

	public ProfessorMinistrouAulaTurmaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

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

	public List<PessoaVO> montarListaProfessorRegistroAula(HorarioTurmaVO obj, ProfessorMinistrouAulaTurmaVO professorMinistrouAulaTurmaVO,UsuarioVO usuario);

	public List montarProfessoresMinistrouAulaTurma(ProfessorMinistrouAulaTurmaVO professorMinistrouAulaTurmaVO, int nivelMontarDados,UsuarioVO usuario) throws Exception;

	public List consultarPorCodigoDisciplinaCodigoTurma(Integer valorConsulta, Integer turma, String semestre, String ano, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void validarConsultaDoUsuario(UsuarioVO usuario) throws Exception;

	public Integer consultarSomaCargaHorarioDisciplina(Integer codigoTurma, String semestre, String ano, Integer codigoDisciplina, boolean b, UsuarioVO usuario) throws Exception;

	public ProfessorMinistrouAulaTurmaVO consultarUltimoRegistroAulaPorMatricula(String matricula, boolean b, int nivelmontardadosDadosbasicos, UsuarioVO usuario) throws Exception;

        public void incluirProfessorMinistrouAulaTurma(TurmaVO turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataAula, UsuarioVO usuario) throws Exception;

        public void alterarProfessorMinistrouAulaTurma(TurmaVO turma, Integer disciplina, Integer professor, String ano, String semestre, Date dataAula, UsuarioVO usuario) throws Exception;

        public void alterarListaProfessoresMinistrouAulaTurma(List professoresMinistrouAulaTurma) throws Exception;

        public ProfessorMinistrouAulaTurmaVO montarProfessoresMinistrouAulaTurmaTitular(ProfessorMinistrouAulaTurmaVO professorMinistrouAulaTurmaVO, int nivelMontarDados,UsuarioVO usuario) throws Exception;
}