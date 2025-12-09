package negocio.interfaces.processosel;

import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.processosel.DisciplinasGrupoDisciplinaProcSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe
 * Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de
 * padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de sua classe Façade (responsável por persistir
 * os dados das classes VO).
 */
public interface DisciplinasGrupoDisciplinaProcSeletivoInterfaceFacade {

	public DisciplinasGrupoDisciplinaProcSeletivoVO novo() throws Exception;

	public void incluir(final DisciplinasGrupoDisciplinaProcSeletivoVO obj) throws Exception;

	public void alterar(DisciplinasGrupoDisciplinaProcSeletivoVO obj) throws Exception;

	public void excluir(DisciplinasGrupoDisciplinaProcSeletivoVO obj) throws Exception;

	public List<DisciplinasGrupoDisciplinaProcSeletivoVO> consultarPorNomeDisciplinasGrupoDisciplinaProcSeletivo(String valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public DisciplinasGrupoDisciplinaProcSeletivoVO consultarPorChavePrimaria(Integer disciplinasProcSeletivoPrm, Integer procSeletivoPrm, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public void incluirDisciplinasGrupoDisciplinaProcSeletivos(Integer grupoDisciplinaProcSeletivoPrm, List<DisciplinasGrupoDisciplinaProcSeletivoVO> objetos) throws Exception;

	public void alterarDisciplinasGrupoDisciplinaProcSeletivos(Integer grupoDisciplinaProcSeletivo, List<DisciplinasGrupoDisciplinaProcSeletivoVO> objetos) throws Exception;

	/**
	 * @author Wellington - 2 de dez de 2015
	 * @param grupoDisciplinaProcSeletivo
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<DisciplinasGrupoDisciplinaProcSeletivoVO> consultarDisciplinasGrupoDisciplinaProcSeletivos(Integer grupoDisciplinaProcSeletivo, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington - 21 de dez de 2015
	 * @param obj
	 * @throws ConsistirException
	 */
	void validarDados(DisciplinasGrupoDisciplinaProcSeletivoVO obj) throws ConsistirException;

	/** 
	 * @author Wellington - 6 de jan de 2016 
	 * @param valorConsulta
	 * @param controlarAcesso
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	List<DisciplinasGrupoDisciplinaProcSeletivoVO> consultarPorInscricaoCandidato(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
}