package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.LiberacaoFinanceiroCancelamentoTrancamentoVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a
 * camada de controle e camada de negócio (em especial com a classe Façade). Com
 * a utilização desta interface é possível substituir tecnologias de uma camada
 * da aplicação com mínimo de impacto nas demais. Além de padronizar as
 * funcionalidades que devem ser disponibilizadas pela camada de negócio, por
 * intermédio de sua classe Façade (responsável por persistir os dados das
 * classes VO).
 */
public interface LiberacaoFinanceiroCancelamentoTrancamentoInterfaceFacade {

	public LiberacaoFinanceiroCancelamentoTrancamentoVO novo() throws Exception;

	public void incluir(LiberacaoFinanceiroCancelamentoTrancamentoVO obj, List listaPendencias, List listaContaReceber, List listaContaReceberPersistirBanco, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public void alterar(LiberacaoFinanceiroCancelamentoTrancamentoVO obj, UsuarioVO usuario) throws Exception;

	public void excluir(LiberacaoFinanceiroCancelamentoTrancamentoVO obj, UsuarioVO usuario) throws Exception;

	public LiberacaoFinanceiroCancelamentoTrancamentoVO consultarPorChavePrimaria(Integer codigo, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuarioVO) throws Exception;

	public List consultarPorMatriculaMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	public List consultarPorNomeUnidadeEnsino(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	public List consultarPorDataCadastro(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	public List consultarPorNomeUsuario(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	public void excluirObjListaContaReceberVOs(Integer codigo, List listaContaReceber, UsuarioVO usuario) throws Exception;

	public List<LiberacaoFinanceiroCancelamentoTrancamentoVO> consultaRapidaPorMatricula(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	public List<LiberacaoFinanceiroCancelamentoTrancamentoVO> consultaRapidaPorNomeAluno(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	public List<LiberacaoFinanceiroCancelamentoTrancamentoVO> consultaRapidaPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	public List<LiberacaoFinanceiroCancelamentoTrancamentoVO> consultaRapidaPorDataCadastro(Date prmIni, Date prmFim, Integer unidadeEnsino, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	public List<LiberacaoFinanceiroCancelamentoTrancamentoVO> consultaRapidaPorResponsavel(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	public void excluirComBaseNaMatricula(String matricula, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

	public void realizarEstorno(LiberacaoFinanceiroCancelamentoTrancamentoVO obj, List<ContaReceberVO> contaReceberCancela, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception;

	public void alterarSituacao(final LiberacaoFinanceiroCancelamentoTrancamentoVO obj, UsuarioVO usuario) throws Exception;

	void validarDadosPendenciaEmprestimoBiblioteca(MatriculaVO matriculaVO, boolean realizandoTrancamento, boolean realizandoAbandono, boolean realizandoCancelamento, boolean realizandoTransferenciaSaida, boolean realizandoTransferenciaInterna, boolean realizandoRenovacao, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception;

}