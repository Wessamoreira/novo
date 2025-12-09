package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.AtividadeComplementarMatriculaVO;
import negocio.comuns.secretaria.AtividadeComplementarVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 *
 * @author Carlos
 */
public interface AtividadeComplementarInterfaceFacade {
    //public void realizarAlteracaoHoraComplementarAluno(AtividadeComplementarVO obj, List<AtividadeComplementarMatriculaVO> listaAtividadeComplementarMatriculaVOs, Integer horaComplementar, String ano, String semestre, TurmaVO turmaVO, DisciplinaVO disciplinaVO, Date dataAtividade, UsuarioVO usuarioVO) throws Exception;
    public void persistir(AtividadeComplementarVO obj, Boolean permiteLancamentoAtividadeComplementarFutura, UsuarioVO usuarioVO) throws Exception;
    public void preencherTodosListaAluno(List<AtividadeComplementarMatriculaVO> listaAtividadeComplementarMatriculaVOs , Boolean marcarTodos);
    public void desmarcarTodosListaAluno(List<AtividadeComplementarMatriculaVO> listaAtividadeComplementarMatriculaVOs);
    public List<AtividadeComplementarVO> consultar(String campoConsulta, String valorConsulta, String ano, String semestre, int limit, int offset, UsuarioVO usuarioVO) throws Exception;
    public void carregarDados(AtividadeComplementarVO obj, UsuarioVO usuario) throws Exception;
    public void carregarDados(AtividadeComplementarVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;
    public void incluir(AtividadeComplementarVO obj, UsuarioVO usuario) throws Exception;
    public void alterar(AtividadeComplementarVO obj, UsuarioVO usuario) throws Exception;
    public void excluir(AtividadeComplementarVO obj, UsuarioVO usuario) throws Exception;
    public List<AtividadeComplementarVO> consultaRapidaPorProfessor(Integer professor, TurmaVO turma, Integer disciplina, String ano, String semestre, int limit, int offset, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	Integer consultaTotalPorProfessor(Integer professor, TurmaVO turma, Integer disciplina, String ano, String semestre) throws Exception;
	Integer consultarTotalRegistro(String campoConsulta, String valorConsulta, String ano, String semestre) throws Exception;
}
