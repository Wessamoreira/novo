package negocio.interfaces.recursoshumanos;

import java.util.List;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.AtividadeExtraClasseProfessorCursoVO;
import negocio.comuns.recursoshumanos.AtividadeExtraClasseProfessorVO;
import negocio.facade.jdbc.recursoshumanos.SuperFacadeInterface;

public interface AtividadeExtraClasseProfessorCursoInterfaceFacade <T extends SuperVO> extends SuperFacadeInterface<T> {

	public void persistirTodos(List<AtividadeExtraClasseProfessorCursoVO> listaAtividadeExtraClasseProfessorCurso, boolean validarDados, UsuarioVO usuario,
			AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO) throws Exception;

	public void adicionarAtividadeExtraClasseCurso(
			List<AtividadeExtraClasseProfessorVO> listaAtividadeExtraClasseProfessor,
			AtividadeExtraClasseProfessorCursoVO atividadeExtraClasseProfessorCursoVO,
			AtividadeExtraClasseProfessorVO atividadeExtraClasseCursoVO) throws Exception;

	public void adicionarAtividadeExtraClasseCursoReplicarTodosMeses(
			List<AtividadeExtraClasseProfessorVO> listaAtividadeExtraClasseProfessor,
			AtividadeExtraClasseProfessorCursoVO atividadeExtraClasseProfessorCursoVO, AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorSelecionadoVO) throws Exception;

	public void alteraHorasPrevistaCurso(List<AtividadeExtraClasseProfessorVO> listaAtividadeExtraClasseProfessor,
			AtividadeExtraClasseProfessorCursoVO atividadeExtraClasseProfessorCursoSelecionadoVO) throws Exception;

	public void alteraHorasPrevistaCursoTodosMeses(List<AtividadeExtraClasseProfessorVO> listaAtividadeExtraClasseProfessor,
			AtividadeExtraClasseProfessorCursoVO atividadeExtraClasseProfessorCursoSelecionadoVO, FuncionarioCargoVO funcionarioCargo, UsuarioVO usuarioVO) throws Exception;

	public List<AtividadeExtraClasseProfessorCursoVO> consultarPorAtividadeExtraClasse(AtividadeExtraClasseProfessorVO obj, UsuarioVO usuarioLogado) throws Exception;

	public void removerTodosMesesAtividadeExtraClasseCurso(AtividadeExtraClasseProfessorCursoVO atividadeExtraClasseProfessorCursoVO, 
			List<AtividadeExtraClasseProfessorCursoVO> listaAtividadeExtraClasseProfessorCurso,  List<AtividadeExtraClasseProfessorVO> listaAtividadeExtraClasseProfessor) throws Exception;

	public void excluirAtividadeExtraClasseProfessorCurso(
			AtividadeExtraClasseProfessorVO atividadeExtraClasseProfessorVO, UsuarioVO usuario) throws Exception;

	void excluirPorAtividadeExtraClasse(AtividadeExtraClasseProfessorVO obj, UsuarioVO usuarioVO) throws Exception;
}
