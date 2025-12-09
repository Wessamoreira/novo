package negocio.interfaces.financeiro;
import java.util.Date;
import java.util.List;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberNegociadoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.NegociacaoContaReceberPlanoDescontoVO;
import negocio.comuns.financeiro.NegociacaoContaReceberVO;
import negocio.comuns.financeiro.enumerador.TipoAcrescimoEnum;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle
 * e camada de negócio (em especial com a classe Façade). Com a utilização desta interface 
 * é possível substituir tecnologias de uma camada da aplicação com mínimo de impacto nas demais.
 * Além de padronizar as funcionalidades que devem ser disponibilizadas pela camada de negócio, por intermédio
 * de sua classe Façade (responsável por persistir os dados das classes VO).
*/
public interface NegociacaoContaReceberInterfaceFacade {
	

    public NegociacaoContaReceberVO novo() throws Exception;
    public void incluir(NegociacaoContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, Boolean verificarPermissao, UsuarioVO usuario) throws Exception;
    public void alterar(NegociacaoContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;
    public void excluir(NegociacaoContaReceberVO obj, List<ContaReceberVO> contaReceberVOs,ConfiguracaoFinanceiroVO configuracaoFinanceiro,Boolean verificarPermissao, UsuarioVO usuario) throws Exception;
    public NegociacaoContaReceberVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO ,UsuarioVO usuario) throws Exception;
    public void setIdEntidade(String aIdEntidade);
    public void verificarPermissaoExcluirNegociacao(NegociacaoContaReceberVO obj, List<ContaReceberVO> contaReceberVOs, UsuarioVO usuario) throws Exception;
    public Integer consultarNumeroDeRecebimentosParaUmaMatricula(String matricula) throws Exception;
    public Boolean validarPermissaoNegociacarParcelaNegociadaNaoCumprida(ContaReceberVO obj, UsuarioVO usuarioVO) throws Exception;
    public List<NegociacaoContaReceberVO> consultarPorMatriculaMatricula(String valorConsulta, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;
    List<NegociacaoContaReceberVO> consultarPorCodigoResponsavelFinanceiro(Integer codigoResponsavelFinanceiro, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;
    public Double calcularValorTotalConfirmacaoNegociacao(NegociacaoContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception ;
    public void validarDadosCalculoValorTotalConfirmacaoNegociacao(NegociacaoContaReceberVO obj) throws Exception;
    void realizarInicializacaoDadosRenegociacaoContaReceber(NegociacaoContaReceberVO negociacaoContaReceberVO, MatriculaVO matriculaVO, TipoPessoa tipoPessoa, UsuarioVO usuarioLogado) throws Exception;
    void realizarInicializacaoDadosOpcaoRenegociacao(NegociacaoContaReceberVO negociacaoContaReceberVO, List<ContaReceberVO> contaReceberVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean visaoAdministrativa, UsuarioVO usuarioLogado) throws Exception;
    void incluirRenegociacaoAluno(NegociacaoContaReceberVO negociacaoContaReceberVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioLogado) throws Exception;
    ContaReceberVO incluirRenegociacaoApartirContaReceber(ContaReceberVO contaReceberSimulacao, ContaReceberVO contaReceber, Date dataVencimento, Boolean validarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Boolean removerDescontoVencido, UsuarioVO usuarioLogado) throws Exception;    
	Boolean realizarValidacaoAtivacaoMatriculaPeriodoParcelaRenegociada(Integer contaReceber, Integer matriculaPeriodo, Boolean utilizaMatricula) throws Exception;
	Boolean verificarPermissaoExcluirNegociacaoThread(NegociacaoContaReceberVO obj, List<ContaReceberVO> contaReceberVOs) throws Exception;
	void excluirNegociacaoRecebimentoVencida(RegistroExecucaoJobVO registroExecucaoJobVO) throws Exception;
	Boolean realizarValidacaoEstornoAtivacaoMatriculaPeriodoParcelaRenegociada(Integer contaReceber, Integer matriculaPeriodo, Boolean utilizaMatricula) throws Exception;
	Double realizarCalculoValorBaseParcela(NegociacaoContaReceberVO negociacaoContaReceberVO, TipoAcrescimoEnum tipoAcrescimoEnum, Double acrescimoPorParcela); 
	void gerarParcelas(NegociacaoContaReceberVO obj, TipoAcrescimoEnum tipoAcrescimo, Double acrescimoPorParcela, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO)throws Exception;
	void realizarCalculoValorTodasContaReceberAdicionar(NegociacaoContaReceberVO negociacaoContaReceberVO, Boolean desconsiderarDescontoProgressivo, Boolean desconsiderarDescontoAluno, Boolean desconsiderarDescontoInstituicaoComValidade, Boolean desconsiderarDescontoInstituicaoSemValidade) throws Exception;
	void realizarCalculoTotal(NegociacaoContaReceberVO negociacaoContaReceberVO);
	void adicionarPlanoDesconto(NegociacaoContaReceberVO negociacaoContaReceberVO, Integer planoDesconto,UsuarioVO usuario) throws Exception;
	void removerPlanoDesconto(NegociacaoContaReceberVO negociacaoContaReceberVO, NegociacaoContaReceberPlanoDescontoVO negociacaoContaReceberPlanoDescontoVO) throws Exception;
	void validarExistenciaItemCondicaoDescontoRenegociacao(NegociacaoContaReceberVO negociacaoContaReceberVO, ConfiguracaoFinanceiroVO conf, UsuarioVO usuario) throws Exception;
	void realizarInicializacaoDadosOpcaoRenegociacaoBaseadoCondicaoRenegociacao(NegociacaoContaReceberVO negociacaoContaReceberVO, List<ContaReceberVO> listaContaReceberVOs, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, Boolean visaoAdministrativa, UsuarioVO usuarioVO) throws Exception;
	Date consultaDataNegociacaoContaReceberPorContaReceberUnica(Integer contaReceber, UsuarioVO usuario) throws Exception;
	void consultar(DataModelo dataModelo, String responsavelCadastro, String comissionado, boolean validarAcesso,
			int nivelMontarDados, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception;
	public Boolean permiteReceberCartaoCreditoVisaoAluno(String codOrigem,UsuarioVO usuario) throws Exception;
	public void alterarPermitirPagamentoCartaoCredito(NegociacaoContaReceberVO negociacaoContaReceberVO, UsuarioVO usuarioLogado) throws Exception;
	public void validarValorEntrada(NegociacaoContaReceberVO negociacaoContaReceberVO) throws ConsistirException;
//	public void montarDadosDescontos(NegociacaoContaReceberVO negociacaoContaReceberVO,
//			List<ContaReceberVO> contaReceberVOs);
	void realizarAtualizacaoOpcoesPagamentoConformeItemCondicaoNegociacao(NegociacaoContaReceberVO negociacaoContaReceberVO) throws Exception;
	void persistirAgenteNegativacao(NegociacaoContaReceberVO negociacaoContaReceberVO, UsuarioVO usuarioVO);
	void adicionarContaReceberNegociado(NegociacaoContaReceberVO negociacaoContaReceberVO,
			ContaReceberNegociadoVO contaReceberNegociadoVO, Boolean desconsiderarDescontoProgressivo,
			Boolean desconsiderarDescontoAluno, Boolean desconsiderarDescontoInstituicaoComValidade,
			Boolean desconsiderarDescontoInstituicaoSemValidade, ConfiguracaoFinanceiroVO conf, UsuarioVO usuarioVO)
			throws Exception;
}