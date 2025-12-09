package negocio.interfaces.academico;

import java.util.List;

import negocio.comuns.academico.VagaTurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em especial com a classe
 * Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais. Além de
 * padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio de sua classe Façade (responsável por persistir
 * os dados das classes VO).
 */
public interface VagaTurmaInterfaceFacade {

	void excluir(VagaTurmaVO obj, UsuarioVO usuarioVO) throws Exception;

	VagaTurmaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	
	public VagaTurmaVO consultaRapidaPorTurma(Integer turma, UsuarioVO usuario) throws Exception;
	
	List<VagaTurmaVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<VagaTurmaVO> consultaRapidaPorIdentificadorTurma(String identificador, Integer unidade, String ano, String semestre, String situacaoTurma, String situacaoTipoTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	List<VagaTurmaVO> consultaRapidaPorCodigoTurma(String valorConsulta, Integer unidade, String ano, String semestre, String situacaoTurma, String situacaoTipoTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * @author Rodrigo Wind - 20/11/2015
	 * @param turma
	 * @param ano
	 * @param semestre
	 * @param codigo
	 * @return
	 * @throws Exception
	 */
	VagaTurmaVO consultaRapidaUnicidadeTurmaPorIdentificador(Integer turma, String ano, String semestre, Integer codigo) throws Exception;

	/**
	 * @author Wellington - 5 de fev de 2016
	 * @param vagaTurmaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void persistir(VagaTurmaVO vagaTurmaVO, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington - 5 de fev de 2016
	 * @param valorConsulta
	 * @param unidade
	 * @param ano
	 * @param semestre
	 * @param situacaoTurma
	 * @param situacaoTipoTurma
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<VagaTurmaVO> consultaRapidaPorCodigoDisciplina(String valorConsulta, Integer unidade, String ano, String semestre, String situacaoTurma, String situacaoTipoTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington - 5 de fev de 2016
	 * @param valorConsulta
	 * @param unidade
	 * @param ano
	 * @param semestre
	 * @param situacaoTurma
	 * @param situacaoTipoTurma
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	List<VagaTurmaVO> consultaRapidaPorNomeDisciplina(String valorConsulta, Integer unidade, String ano, String semestre, String situacaoTurma, String situacaoTipoTurma, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
	
	public void persistirVagaTurmaBase(VagaTurmaVO vagaTurmaVO, UsuarioVO usuarioVO) throws Exception;

}
