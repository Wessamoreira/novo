package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.DisciplinasProcSeletivoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe
 * Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de
 * padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de sua classe Façade (responsável por persistir
 * os dados das classes VO).
 */
public interface DisciplinasProcSeletivoInterfaceFacade {

	DisciplinasProcSeletivoVO novo(UsuarioVO usuario) throws Exception;

	void incluir(DisciplinasProcSeletivoVO obj, UsuarioVO usuario) throws Exception;

	void alterar(DisciplinasProcSeletivoVO obj, UsuarioVO usuario) throws Exception;

	void excluir(DisciplinasProcSeletivoVO obj, UsuarioVO usuario) throws Exception;

	DisciplinasProcSeletivoVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception;

	List<DisciplinasProcSeletivoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<DisciplinasProcSeletivoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<DisciplinasProcSeletivoVO> consultarPorTipoDisciplina(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<DisciplinasProcSeletivoVO> consultarPorDisciplinasIdioma(boolean tipoDisciplinasLinguaEstrangeira, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void setIdEntidade(String aIdEntidade);

	/**
	 * @author Wellington - 17 de dez de 2015
	 * @param grupoDisciplinaProcSeletivo
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<DisciplinasProcSeletivoVO> consultarPorGrupoDisciplinaProcSeletivo(Integer grupoDisciplinaProcSeletivo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	List<DisciplinasProcSeletivoVO> consultarPorProcSeletivo(Integer procSeletivo, boolean controlarAcesso,
			UsuarioVO usuario) throws Exception;
}