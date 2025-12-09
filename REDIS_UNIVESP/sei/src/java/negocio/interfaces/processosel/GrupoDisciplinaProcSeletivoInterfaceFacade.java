package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.DisciplinasGrupoDisciplinaProcSeletivoVO;
import negocio.comuns.processosel.GrupoDisciplinaProcSeletivoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe
 * Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de
 * padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de sua classe Façade (responsável por persistir
 * os dados das classes VO).
 */
public interface GrupoDisciplinaProcSeletivoInterfaceFacade {

	public GrupoDisciplinaProcSeletivoVO novo() throws Exception;

	public void incluir(GrupoDisciplinaProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void alterar(GrupoDisciplinaProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception;

	public void excluir(GrupoDisciplinaProcSeletivoVO obj, UsuarioVO usuarioVO) throws Exception;

	public GrupoDisciplinaProcSeletivoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<GrupoDisciplinaProcSeletivoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List<GrupoDisciplinaProcSeletivoVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public GrupoDisciplinaProcSeletivoVO consultarCodigo(Integer intValue, boolean b, int nivelmontardadosTodos, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington - 21 de dez de 2015
	 * @param obj
	 * @param disciplinasGrupoDisciplinaProcSeletivoVOs
	 * @throws Exception
	 */
	void adicionarObjDisciplinasGrupoDisciplinaProcSeletivoVOs(DisciplinasGrupoDisciplinaProcSeletivoVO obj, List<DisciplinasGrupoDisciplinaProcSeletivoVO> disciplinasGrupoDisciplinaProcSeletivoVOs) throws Exception;

	/**
	 * @author Wellington - 21 de dez de 2015
	 * @param disciplinasProcSeletivo
	 * @param disciplinasGrupoDisciplinaProcSeletivoVOs
	 * @throws Exception
	 */
	void excluirObjDisciplinasGrupoDisciplinaProcSeletivoVOs(Integer disciplinasProcSeletivo, List<DisciplinasGrupoDisciplinaProcSeletivoVO> disciplinasGrupoDisciplinaProcSeletivoVOs) throws Exception;

	GrupoDisciplinaProcSeletivoVO consultarGrupoDisciplinaPorProcessoSeletivoEUnidadeEnsinoCurso(Integer procSeletivo,
			Integer unidadeEnsinoCurso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception;
	
	public List<GrupoDisciplinaProcSeletivoVO> consultarPorDescricaoSituacaoAtivo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

}