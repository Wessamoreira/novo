package negocio.interfaces.academico;

import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.CancelamentoVO;
import negocio.comuns.academico.MapaRegistroEvasaoCursoMatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface CancelamentoInterfaceFacade {

	public CancelamentoVO novo() throws Exception;

	public void incluir(CancelamentoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario, Boolean verificarPermissao) throws Exception;

	public void alterar(CancelamentoVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception;

	public void excluir(CancelamentoVO obj, UsuarioVO usuario) throws Exception;

	public CancelamentoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<CancelamentoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

//	public List<CancelamentoVO> consultarPorData(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<CancelamentoVO> consultarPorDescricao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public List<CancelamentoVO> consultarPorSituacao(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public List<CancelamentoVO> consultarPorDataUnidadeEnsino(Date prmIni, Date prmFim, String unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public CancelamentoVO consultarPorCodigoRequerimento(Integer codigoPrm, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public void alterarSituacaoCancelamento(Integer codigo, String situacao, UsuarioVO usuario);

	public List<CancelamentoVO> consultaRapidaPorNomeAluno(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
//	public List<CancelamentoVO> consultaRapidaPorNomeAlunoUltimoCancelamento(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<CancelamentoVO> consultaRapidaPorTipoJustificativa(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

//	public List<CancelamentoVO> consultaRapidaPorTipoJustificativaUltimoCancelamento(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
//	public List<CancelamentoVO> consultaRapidaPorCodigoRequerimento(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<CancelamentoVO> consultaRapidaPorMatricula(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;
	
//	public List<CancelamentoVO> consultaRapidaPorMatriculaUltimoCancelamento(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

//	public List<CancelamentoVO> consultaRapidaPorSituacao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

//	public List<CancelamentoVO> consultaRapidaPorDescricao(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List<CancelamentoVO> consultaRapidaPorData(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

//	public List<CancelamentoVO> consultaRapidaPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

//	public List<CancelamentoVO> consultaRapidaPorTurma(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public void carregarDados(CancelamentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public void carregarDados(CancelamentoVO obj, NivelMontarDados nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public String imprimirDeclaracaoCancelamento(CancelamentoVO cancelamentoVO, TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO, ConfiguracaoGeralSistemaVO config, UsuarioVO usuario) throws Exception;
	
	public void validarDadosAntesImpressao(CancelamentoVO cancelamentoVO, Integer textoPadrao) throws Exception;

	/**
	 * Responsável por executar a persistência dos dados pertinentes a
	 * CancelamentoVO.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param obj
	 * @param configuracaoGeralSistemaVO
	 * @param configuracaoFinanceiroVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void persistir(CancelamentoVO cancelamentoVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	/**
	 * Responsável por executar a validação se a matrícula está apta a realizar
	 * o cancelamento, levando em consideração sua situação, a situação
	 * financeira e e se existe pendência em empréstimo na biblioteca.
	 * 
	 * @author Wellington Rodrigues - 01/04/2015
	 * @param matriculaVO
	 * @param configuracaoGeralSistemaVO
	 * @param usuarioVO
	 * @throws Exception
	 */
	void executarValidacaoExistePendenciaFinanceiraEPreMatriculaAtiva(MatriculaVO matriculaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;
	
	void executarEstorno(CancelamentoVO cancelamentoVO, UsuarioVO usuarioVO) throws Exception;
	public CancelamentoVO consultarPorMatricula(String valorConsulta, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

//	List<CancelamentoVO> consultaRapidaPorRegistroAcademicoUltimoCancelamento(String valorConsulta,	Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void realizarCancelamentoMatriculaAtivaPorOutroCursoMesmoNivelEducacional(MatriculaVO matriculaVO,	ConfiguracaoGeralSistemaVO configuracaoGeralSistema, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO,  UsuarioVO usuarioVO) throws Exception;
	
	public void realizarAlteracaoCodigoRequerimentoCancelamento(final Integer codigoRequerimento) throws Exception;

	void persistirCancelamentoMapaRegistroAbandonoCursoTrancamentoIndividualmente(MapaRegistroEvasaoCursoMatriculaPeriodoVO mrecmp, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	void consultaOtimizada(DataModelo controleConsulta, String  tipoJustificativa, String situacao, Integer unidadeEnsino, boolean controlarAcesso,
			UsuarioVO usuario) throws Exception;
}