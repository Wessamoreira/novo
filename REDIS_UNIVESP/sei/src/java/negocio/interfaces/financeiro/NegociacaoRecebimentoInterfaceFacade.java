package negocio.interfaces.financeiro;

import java.util.Date;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import appletImpressaoMatricial.LinhaImpressao;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.ImpressoraVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ConfiguracaoRecebimentoCartaoOnlineVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import webservice.servicos.BandeiraRSVO;
import webservice.servicos.NegociacaoRecebimentoRSVO;

/**
 * Interface reponsável por criar uma estrutura padrão de comunidação entre a camada de controle e camada de negócio (em
 * especial com a classe Façade). Com a utilização desta interface é possível substituir tecnologias de uma camada da
 * aplicação com mínimo de impacto nas demais. Além de padronizar as funcionalidades que devem ser disponibilizadas pela
 * camada de negócio, por intermédio de sua classe Façade (responsável por persistir os dados das classes VO).
 */
public interface NegociacaoRecebimentoInterfaceFacade {

	public NegociacaoRecebimentoVO novo() throws Exception;

	public void excluir(NegociacaoRecebimentoVO obj,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	public NegociacaoRecebimentoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;
	
	/**
	 * 
	 * @param codigo
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @return
	 * @throws Exception
	 * @since 27/08/2010
	 * @author Vinícius Bueno
	 */
	SqlRowSet consultaRapidaPorChavePrimariaDadosBasicos(Integer codigo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	/**
	 * 
	 * @param codigo
	 * @param controlarAcesso
	 * @return
	 * @throws Exception
	 * @since 27/08/2010
	 * @author Vinícius Bueno
	 */
	SqlRowSet consultaRapidaPorChavePrimariaDadosCompletos(Integer codigo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	public List consultarPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	/**
	 * 
	 * @param valorConsulta
	 * @param dataIni
	 * @param dataFim
	 * @param unidadeEnsino
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @return
	 * @throws Exception
	 * @since 27/08/2010
	 * @author Vinícius Bueno
	 */
	List<NegociacaoRecebimentoVO> consultaRapidaPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorResponsavel(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	/**
	 * 
	 * @param valorConsulta
	 * @param dataIni
	 * @param dataFim
	 * @param unidadeEnsino
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @return
	 * @throws Exception
	 * @since 27/08/2010
	 * @author Vinícius Bueno
	 */
	List<NegociacaoRecebimentoVO> consultaRapidaPorResponsavel(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorContaCorrenteCaixa(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	/**
	 * 
	 * @param valorConsulta
	 * @param dataIni
	 * @param dataFim
	 * @param unidadeEnsino
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @return
	 * @throws Exception
	 * @since 27/08/2010
	 * @author Vinícius Bueno
	 */
	List<NegociacaoRecebimentoVO> consultaRapidaPorContaCorrenteCaixa(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorPessoa(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

	/**
	 * 
	 * @param valorConsulta
	 * @param dataIni
	 * @param dataFim
	 * @param unidadeEnsino
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @return
	 * @throws Exception
	 * @since 27/08/2010
	 * @author Vinícius Bueno
	 */
	List<NegociacaoRecebimentoVO> consultaRapidaPorPessoa(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorMatricula(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;

         /*Responsável por criar a String de autenticação mecânica composta pelos seguintes campos
     * 1- Numero do Documento
     * 2- Data pagamento (10/10/10)
     * 3- Ano e Semestre (2010/2)
     * 4- Numero da Parcela
     * 5- Primeiro Nome da Pessoa
     * 6- Valor Recebido
     * 7- Usuário Responsavel pela autenticação
     */
    public List<LinhaImpressao> executarAutenticacaoRecebimento(NegociacaoRecebimentoVO negociacaoRecebimentoVO, ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO, UsuarioVO usuario,ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception;

	/**
	 * 
	 * @param valorConsulta
	 * @param dataIni
	 * @param dataFim
	 * @param unidadeEnsino
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @return
	 * @throws Exception
	 * @since 27/08/2010
	 * @author Vinícius Bueno
	 */
	public List<NegociacaoRecebimentoVO> consultaRapidaPorMatricula(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public List consultarPorCpf(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;
	
	/**
	 * 
	 * @param valorConsulta
	 * @param dataIni
	 * @param dataFim
	 * @param unidadeEnsino
	 * @param controlarAcesso
	 * @param nivelMontarDados
	 * @return
	 * @throws Exception
	 * @since 27/08/2010
	 * @author Vinícius Bueno
	 */
	List<NegociacaoRecebimentoVO> consultaRapidaPorCpf(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;

	public void setIdEntidade(String aIdEntidade);

	/**
	 * 
	 * @param obj
	 * @param nivelMontarDados
	 * @throws Exception
	 * @since 27/08/2010
	 * @author Vinícius Bueno
	 * @return
	 */
	NegociacaoRecebimentoVO carregarDados(NegociacaoRecebimentoVO obj, NivelMontarDados nivelMontarDados, UsuarioVO usuario) throws Exception;

    public List<NegociacaoRecebimentoVO> consultaRapidaPorNossoNumero(String valorConsulta, Integer unidadeEnsino, String tipoOrigem, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception;
    
    public Boolean verificarSeChequeFoiMovimentadoParaOutraContaCaixa(Integer codigoNegociacao) throws Exception;

    public String consultarDescricaoPorCodigo(Integer codigoPrm) throws Exception;
    
    public Integer consultarCodigoUnidadeEnsinoPelaFormaPagamentoNegociacaoRecebimentoCartaoCredito(
			Integer codigoFormaPagamentoNegociacaoRecebimentoCartaoCredito) throws Exception;
    
    public Integer consultarNumeroDeRecebimentosParaUmaMatricula(String matricula) throws Exception;
    
    public void alterarTipoRecebimentoBoletoAutomatico(final Boolean recebimentoBoletoAutomatico, final Integer codigo, UsuarioVO usuario) throws Exception;

    public void calcularValorTrocoDeAcordoFormaPagamento(NegociacaoRecebimentoVO obj) throws Exception;

    public List<NegociacaoRecebimentoVO> consultaRapidaPorNossoNumeroContaReceber(String nossoNumero, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

    public List<NegociacaoRecebimentoVO> consultaRapidaMatriculaNegativadaSerasaQueRealizouPagamento(Date dataPagamento, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;
    
    //public String consultarMatriculaPeloChequeFormaPagamentoNegociacaoRecebimento(Integer cheque) throws Exception;
    
    public void incluirLogAlteracaoContaCaixaEstorno(final String matricula, final Integer contaCaixaPadrao, final Integer contaCaixaEstorno, UsuarioVO usuarioVO) throws Exception;

    public NegociacaoRecebimentoVO consultarMatriculaPeloChequeFormaPagamentoNegociacaoRecebimento(Integer cheque) throws Exception;

	String realizarGeracaoTextoComprovante(NegociacaoRecebimentoVO negociacaoRecebimentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO,  ImpressoraVO impressoraVO, UsuarioVO usuario)  throws Exception;
        
    public NegociacaoRecebimentoVO consultaRapidaPorNossoNumeroUnicaContaReceber(String nossoNumero, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiro, UsuarioVO usuario) throws Exception;
    
    public void alterarPessoaTipoPessoaContaReceberNegociacaoRecebimento(ContaReceberVO contaReceberVO, UsuarioVO usuario) throws Exception;

	/**
	 * @author Wellington Rodrigues - 21/05/2015
	 * @param pessoaAntigo
	 * @param pessoaNova
	 * @throws Exception
	 */
	void alterarPessoaNegociacaoRecebimentoUnificacaoFuncionario(Integer pessoaAntigo, Integer pessoaNova) throws Exception;

	void alterar(NegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	void incluir(NegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	void realizarRecebimentoCartaoCreditoMatriculaRenovacaoOnline(NegociacaoRecebimentoVO negociacaoRecebimentoVO, String matricula, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	void realizarRecebimentoCartaoCreditoIncricao(NegociacaoRecebimentoVO negociacaoRecebimentoVO, Integer codigoPessoa, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;
	/** 
	 * @author Victor Hugo de Paula Costa - 26 de abr de 2016 
	 * @param controleConsulta
	 * @param valorConsultaUnidadeEnsino
	 * @param tipoOrigem
	 * @param nivelMontarDados
	 * @param verificarAcesso
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	List<NegociacaoRecebimentoVO> consultar(ControleConsulta controleConsulta, int valorConsultaUnidadeEnsino, String tipoOrigem, int nivelMontarDados, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 27 de abr de 2016 
	 * @param obj
	 * @param configuracaoFinanceiroVO
	 * @param usuario
	 * @return 
	 * @throws Exception 
	 */
	NegociacaoRecebimentoVO carregarDados(NegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 25 de mai de 2016 
	 * @param codigoInscricaoProcessoSeletivo
	 * @param configuracaoFinanceiro
	 * @param controleAcesso
	 * @param nivelMontarDados
	 * @param usuarioVO
	 * @return
	 * @throws Exception 
	 */
	NegociacaoRecebimentoVO consultarRecebimentoProcessoSeletivoAluno(Integer codigoInscricaoProcessoSeletivo, ConfiguracaoFinanceiroVO configuracaoFinanceiro, boolean controleAcesso, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception;

	/** 
	 * @author Victor Hugo de Paula Costa - 30 de mai de 2016 
	 * @param obj
	 * @param configuracaoFinanceiro
	 * @param verificarAcesso
	 * @param usuario
	 * @throws Exception 
	 */
	void incluirRecebimentoBaixaDCC(NegociacaoRecebimentoVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiro, boolean verificarAcesso, UsuarioVO usuario) throws Exception;

	
	public void distribuirRecebimentoContaReceber(NegociacaoRecebimentoVO obj, UsuarioVO usuario) throws Exception;
	
	public void distribuirContaReceberNegociacao(NegociacaoRecebimentoVO obj, FormaPagamentoNegociacaoRecebimentoVO rcbmnt, Double valorRecebimento, List<LancamentoContabilVO> listaLancamentoContabilVOs) throws Exception;
	
	public void persistirLancamentoContabilPorNegociacaoRecebimento(NegociacaoRecebimentoVO obj, UsuarioVO usuario, List<LancamentoContabilVO> listaLancamentoContabilVOs) throws Exception; 
	/** 
	 * @author Victor Hugo de Paula Costa - 31 de mai de 2016 
	 * @param contaReceberNegociacaoRecebimentoVO
	 * @param formaPagamentoNegociacaoRecebimentoVOs
	 * @param unidadeEnsinoVO
	 * @param tipoPessoa
	 * @param pessoaVO
	 * @param parceiroVO
	 * @param fornecedorVO
	 * @throws Exception 
	 */
	void realizarRecebimentoContaProcessada(ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO, List<FormaPagamentoNegociacaoRecebimentoVO> formaPagamentoNegociacaoRecebimentoVOs, UnidadeEnsinoVO unidadeEnsinoVO, String tipoPessoa, PessoaVO pessoaVO, ParceiroVO parceiroVO, FornecedorVO fornecedorVO) throws Exception;

	NegociacaoRecebimentoVO criarNegociacaoRecebimentoVOPorBaixaAutomatica(ContaReceberVO contaReceberVO,
			ContaCorrenteVO contaCorrente, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO)
			throws Exception;
	
	public boolean validarNegociacaoRecebimentoExistente(Integer codigo, boolean controlarAcesso, UsuarioVO usuario) throws Exception;

	void realizarGeracaoFormaPagamentoNegociacaoRecebimentoCartaoCreditoFinanciamentoInstituicao(
			FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO,
			NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuarioVO) throws Exception;
	
	public List<FormaPagamentoNegociacaoRecebimentoVO> preencherFormaPagamentoNegociacaoRecebimentoCartaoCredito(FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, NegociacaoRecebimentoVO negociacaoRecebimentoVO, Double valorRecebimento, UsuarioVO usuarioVO) throws Exception;
	
	Date consultaDataRecebimentoPorContaReceberUnica(Integer contaReceber, UsuarioVO usuario) throws Exception;
	
	public void validarDadosRecebimentoCartaoCreditoPorFormaPagamentoNegociacaoRecebimento(NegociacaoRecebimentoVO negociacaoRecebimentoVO, FormaPagamentoNegociacaoRecebimentoVO obj, UsuarioVO usuarioVO) throws Exception;
	
	public List<BandeiraRSVO> consultarBandeirasDisponiveisPagamentoOnline(ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, UnidadeEnsinoVO unidadeEnsinoVO, Double valorAReceber);
	
	public void alterarUnidadeEnsino (TurmaVO turmaVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception;
}