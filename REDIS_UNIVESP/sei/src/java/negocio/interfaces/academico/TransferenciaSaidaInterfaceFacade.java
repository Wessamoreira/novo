package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TransferenciaSaidaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface TransferenciaSaidaInterfaceFacade {

	public TransferenciaSaidaVO novo() throws Exception;

	public void incluir(TransferenciaSaidaVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

	public void alterar(TransferenciaSaidaVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception;

	public void excluir(TransferenciaSaidaVO obj, UsuarioVO usuarioVO) throws Exception;

	public TransferenciaSaidaVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaSaidaVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaSaidaVO> consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaSaidaVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaSaidaVO> consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaSaidaVO> consultarPorCodigoRequerimento(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaSaidaVO> consultarPorInstituicaoDestino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaSaidaVO> consultarPorTipoJustificativa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<TransferenciaSaidaVO> consultarPorNomePessoa(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List<TransferenciaSaidaVO> consultarPorDataUnidadeEnsino(Date datainicio, Date dataFim, String unidadeEnsino, boolean b, int nivelmontardadosDadosconsulta, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	List<TransferenciaSaidaVO> consultarPorAluno(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	/**
	 * Responsável por executar a validação se a matrícula está apta a realizar
	 * a transferência de saída, levando em consideração sua situação, a situação
	 * financeira e e se existe pendência em empréstimo na biblioteca.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param matriculaVO
	 * @param configuracaoGeralSistemaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void executarValidacaoExistePendenciaFinanceiraEPreMatriculaAtiva(MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

	/**
	 * @author Wellington Rodrigues - 31/03/2015
	 * @param obj
	 * @throws ConsistirException
	 */
	void validarTransferenciaSaida(TransferenciaSaidaVO obj) throws ConsistirException;

	/**
	 * Responsável por executar a persistência dos dados pertinentes a
	 * TransferenciaSaidaVO.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param obj
	 * @param configuracaoGeralSistemaVO
	 * @param configuracaoFinanceiroVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void persistir(TransferenciaSaidaVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington Rodrigues - 31/03/2015
	 * @param transferenciaSaidaVO
	 * @param textoPadraoDeclaracao
	 * @param usuario
	 * @return
	 * @throws Exception
	 */
	public String imprimirDeclaracaoTransferenciaSaida(TransferenciaSaidaVO transferenciaSaidaVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO, ConfiguracaoGeralSistemaVO config,  UsuarioVO usuario) throws Exception;
	
	public void validarDadosAntesImpressao(TransferenciaSaidaVO obj, Integer textoPadrao) throws Exception;
	
	TransferenciaSaidaVO consultarPorMatricula(String matricula, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void realizarEstornoTransferenciaSaida(TransferenciaSaidaVO transferenciaSaidaVO,
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;
}