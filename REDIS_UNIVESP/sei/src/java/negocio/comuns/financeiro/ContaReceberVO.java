
package negocio.comuns.financeiro;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.ItemPlanoFinanceiroAlunoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.crm.InteracaoWorkflowVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.faturamento.nfe.NotaFiscalSaidaServicoVO;
import negocio.comuns.financeiro.enumerador.BancoEnum;
import negocio.comuns.financeiro.enumerador.LayoutArquivoIntegracaoFinanceiraEnum;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.sad.ReceitaDWVO;
import negocio.comuns.utilitarias.BancoFactory;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.boleto.BoletoBanco;
import negocio.comuns.utilitarias.boleto.JBoletoBean;
import negocio.comuns.utilitarias.boleto.bancos.BancoBrasil;
import negocio.comuns.utilitarias.boleto.bancos.Banestes;
import negocio.comuns.utilitarias.boleto.bancos.Bradesco;
import negocio.comuns.utilitarias.boleto.bancos.CaixaEconomica;
import negocio.comuns.utilitarias.boleto.bancos.Daycoval;
import negocio.comuns.utilitarias.boleto.bancos.Hsbc;
import negocio.comuns.utilitarias.boleto.bancos.Itau;
import negocio.comuns.utilitarias.boleto.bancos.Safra;
import negocio.comuns.utilitarias.boleto.bancos.Santander;
import negocio.comuns.utilitarias.boleto.bancos.Sicoob;
import negocio.comuns.utilitarias.boleto.bancos.Sicred;
import negocio.comuns.utilitarias.boleto.bancos.Unibanco;
import negocio.comuns.utilitarias.dominios.FaixaDescontoProgressivo;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.basico.Pessoa;
import negocio.facade.jdbc.financeiro.ContaReceber;
import negocio.facade.jdbc.financeiro.IndiceReajuste;
import webservice.DateAdapterMobile;
import webservice.servicos.objetos.enumeradores.MesesAbreviadosAplicativoEnum;

/**
 * Reponsável por manter os dados da entidade ContaReceber. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "contaReceber")
public class ContaReceberVO extends SuperVO {
	
	private Integer codigo;
	private Date data;
	private String codOrigem;
	private String tipoOrigem;
	private String situacao;
	private String descricaoPagamento;
	private Date dataVencimento;
	private Date dataCompetencia;
        private Boolean possuiPendenciasFinanceirasExternas;   
        private Boolean possuiFiesIntegracaoFinanceiras;   
        private Boolean bloquearPagamentoVisaoAluno; // atributo transient
    /*
     * Esse campo foi criado para solucionar uma situação do boleto gerado para o SICOOB, onde o mesmo é gerado com no máximo 8 caracteres sendo um deles o digito verificador.
     * Para isso, foi criado um campo sequencial na matriculaperiodo permitindo utilizar esse codigo ao inves do codigo da matricula periodo.
     * Como a matricula periodo ao realizar a importação muitas das vezes vem com o seguencial do codigo muito alto, estava inviabilizando a utilização do mesmo no nossonumero.
     * Criado por Thyago - 17/03/2015 - para atender a demanda da faculdade FACER.
     */

	private Integer sequencialcodmatperalternativoboleto;
        /*
         * Valor da conta a receber utilizado como base para o recebimento da conta.
         * Via de regra será o valor da original da conta a receber. O mesmo pode
         * ser diferente do valor original da conta a receber somente quando o aluno
         * tiver um convênio que esteja configurado para abater o valor custeado
         * da conta a receber do valor da conta. Ver explicação detalhada em 
         * valorCusteadoContaReceber e valorBaseContaReceber
         */
	private Double valor;
        /*
         * Pode estar armazenado um valor de desconto ou um percentual de desconto.
         * O que vai determinar o significado deste dado é o atributo tipo do Desconto.
         * PODE SER PERCENTUAL OU VALOR
         */
	private Double valorDesconto;
	private Date dataRecebimento;
        
	/**
	 * É calculado com base nos campos tipoDesconto e valorDesconto que já estão disponiveis na conta a
	 * receber e tambem ja sao persistidos. Estes descontos do aluno podem ser lançados no ato da 
         * matrícula do aluno ou mesmo no momento da criação de uma conta a receber. O método "espirita"
         * ContaReceber.montarListaDescontosAplicaveisContaReceber é o responsável por calcular e atualizar
         * este atributo.
	 */
	private Double valorDescontoAlunoJaCalculado;
        
        /**
	 * É calculado com base em todos os descontos de instituição (planos de desconto) que o aluno
         * possui. O mesmo é calculado considerando a ordem de aplicação de descontos, parametrização
         * sobre a base de cálculo do mesmo (se sobre valor cheio ou não). O método "espirita"
         * ContaReceber.montarListaDescontosAplicaveisContaReceber é o responsável por calcular e atualizar
         * este atributo.
	 */
	private Double valorDescontoInstituicao;
        
        /**
	 * É calculado com base em todos os descontos de convênio (cadastro de convênio de parceiros) que o aluno
         * possui. É importante notar que um convênio no SEI pode gerar um desconto na parcela ou pode gerar 
         * um abatimento no valor original da mesma. Este campo valorDescontoConvenio será utilizado para armazenar
         * o valor de desconto para esta segunda situação - ou seja, para quando o convênio estiver cadastrado
         * para gerar um desconto no valor da parcela - ver valorCusteadoConvenio para mais detalhes. O método "espirita"
         * ContaReceber.montarListaDescontosAplicaveisContaReceber é o responsável por calcular e atualizar
         * este atributo.
	 */
	private Double valorDescontoConvenio;
        
        /** 
         * Nesta variável irá ficar armazenado o valor da parcela que foi abatido
         * do valor total em decorrência do convênio. Isto ocorrerá quando o convênio
         * for configurado para abater do valor da parcela a parte que a conveniada
         * irá pagar. O método "espirita"
         * ContaReceber.montarListaDescontosAplicaveisContaReceber é o responsável por calcular e atualizar
         * este atributo.
         */
        private Double valorCusteadoContaReceber;
        
        /**
         * Nesta variável ficará armazenado o valor da conta a receber base, utilizado
         * para aplicar todos os descontos, juros e outros valores variáveis. Este valor
         * base é importante para reconstruir o calculo de descontos progressivos e outros
         * que sofrem variações com o passar do tempo. Isto por que o campo valor - da conta
         * a receber pode sofrer variações - como a redução de um valor que é para ser custeado
         * por algum convênio do aluno. O método "espirita"
         * ContaReceber.montarListaDescontosAplicaveisContaReceber é o responsável por calcular e atualizar
         * este atributo.
         */
        private Double valorBaseContaReceber;
        
        /**
         * Armazena, quando a conta a receber for recebida (ou seja, será preenchido somente
         * para as contas a receber com situação recebida) a soma de todos os descontos efetivados
         * para o aluno - desconto do convênio, desconto da instituição, desconto do aluno,
         * desconto dado no recebimento e desconto progressivo.
         */
	private Double valorDescontoRecebido;
        
        /**
	 * É calculado com base no desconto progressivo a ser aplicado na conta a receber no momento.
         * O desconto progressivo pode ter diveras faixas de descontos, logo este atributo 
         * é atualizado diariamente (pelo método espírita - via Job) quando a conta esta a receber
         * e fixado com o desconto progressivo efetivado quando a conta é recebido (não sendo mais alterado).
         * O mesmo é calculado considerando a ordem de aplicação de descontos, parametrização
         * sobre a base de cálculo do mesmo (se sobre valor cheio ou não). O método "espirita"
         * ContaReceber.montarListaDescontosAplicaveisContaReceber é o responsável por calcular e atualizar
         * este atributo.
	 */
	private Double valorDescontoProgressivo;
        
        /**
         * Valor real recebido da conta a receber. Este valor é preenchido somente quando a conta
         * for recebida.
         */
	private Double valorRecebido;
        
        /**
         * Valor calculado pelo método "espirita" ContaReceber.montarListaDescontosAplicaveisContaReceber
         * que contém o valor da conta a receber atualizado (com juros, multas, descontos válidos, etc)
         * para a data atual. A Job que é processada todos os dias atualiza este campo, assim como 
         * o método esperíta também faz isto, sempre que uma conta a receber nova é gerada e/ou alterada.
         * Este campo é utilizado no painel gestor e nos relatórios para mostrar o valor da conta 
         * a receber atualizado para o dia atual.
         */
        private Double valorReceberCalculado;
        
        /**
         * Este atributo é utilizado pelo SEI para registrar o dia em que a Job que atualiza os 
         * valores dos descontos (desconto do convênio, institucional, progressivo, ...) e juros e multas
         * foi executada para uma determinada conta a receber. A idéia é que esta Job que atualiza estes valores
         * seja executada todo dia depois da meia-noite (às 01h da manhã). Sempre que a mesma atualiza uma
         * conta a receber, ela registra a data desta atualização (com relação aos valores já calculados de 
         * descontos e juros, como citado acima). Assim, quando alguém acessar o painel gestor, como a conta
         * já estara com os valores dos descontos calculados e corretos, teremos os dados precisos e alinhados
         * com os relatórios do SEI. Adicionalmente, o método espírita ContaReceber.montarListaDescontosAplicaveisContaReceber
         * será modificado nos relatórios do SEI de forma que passe a buscar os valores já atualizados na conta areceber
         * e somente recalcule os valores quando a dataProcessamentoValorReceber esteja desatualizada com a data de hoje.
         */
        private Date dataProcessamentoValorReceber;
        
	private Double valorDescontoInclusaoExclusao;
	private Double valorNegociacao;
	private Double juro;
	private Double juroPorcentagem;
	private Double multa;
	private Double acrescimo;
	private Double multaPorcentagem;
	private Double valorJuroCalculado;
	private Double valorMultaCalculado;
        /**
         * Armazena o valor final da conta a receber já calculado considerando a aplicação
         * de todos os descontos cabíveis, bem como acrescimo, juros e multas.
         */
	private Double valorFinalCalculado;
	private String nrDocumento;
	private String parcela;
	private String tipoPessoa;
	private Integer origemNegociacaoReceber;
	private Integer contaCorrente;
	private String nossoNumero;
	private String identificacaoTituloBanco;	
	/*
	 * Irá carregar o nosso numero gerado pelos bancos para carteira
	 * registradas, onde o banco é responsável pela geração.
	 */
	private String nossoNumeroBanco;
	private UnidadeEnsinoVO unidadeEnsino;
	private UnidadeEnsinoVO unidadeEnsinoFinanceira;
	private ConvenioVO convenio;
	private String linhaDigitavelCodigoBarras;
	private PessoaVO beneficiario;
	private ParceiroVO parceiroVO;
	// Atributo usado para controle na impressão do boleto na visão do aluno
	private ContaCorrenteVO contaCorrenteVO;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>CentroReceita </code>.
	 */
	private CentroReceitaVO centroReceita;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Matricula </code>.
	 */
	private MatriculaVO matriculaAluno;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe
	 * <code>Pessoa </code>.
	 */
	private Integer matriculaPeriodo;
	private FuncionarioVO funcionario;
	private PessoaVO candidato;
	private PessoaVO responsavelFinanceiro;
	private PessoaVO pessoa;
	private NegociacaoContaReceberVO renegociacaoContaReceberOrigem;
	private String codigoBarra;
	private DescontoProgressivoVO descontoProgressivo;
	private String descricaoDescontoProgressivo;
	private String tipoDesconto;
	private List<ContaReceberHistoricoVO> contaReceberHistoricoVOs;
	private List<ContaReceberRecebimentoVO> contaReceberRecebimentoVOs;
	private List<PlanoDescontoContaReceberVO> planoDescontoContaReceberVOs;
	private List<LancamentoContabilVO> listaLancamentoContabeisDebito;
	private List<LancamentoContabilVO> listaLancamentoContabeisCredito;
	private boolean lancamentoContabil = false;
	private String tipoBoleto;
	private boolean gerarNegociacaoRecebimento = true;
        
	private boolean isentar = false;
	private boolean remover = false;
        private boolean cancelar = false;
	private boolean selecionado = false;
	// Campo para geração de relatório de imposto de renda
	private String periodoConta;
	/**
	 * Atributo utilizado para indicar se a conta a receber trata-se de um
	 * título gerado em outro sistema. Este é controle é necessário pois os
	 * boletos referentes a estes títulos podem já terem sido entregues aos
	 * alunos. Portanto, esta conta a receber não pode ser apagada e gerada
	 * novamente.
	 */
	private Boolean tituloImportadoSistemaLegado;
	/**
	 * Atributo responsável por indicar se essa conta foi recebida através de um
	 * boleto bancário ou não.
	 */
	private Boolean recebimentoBancario;
	/**
	 * TRANSIENT - NAO PERSISTIDO E UTILIZADO PARA INCLUIR/ALTERAR UMA
	 * CONTARECECEBER DE FORMA MAIS RAPIDA, SEM TER QUE MONTAR ESTE OBJETO TODAS
	 * AS VEZES.
	 */
	private ConfiguracaoFinanceiroVO configuracaoFinanceiro;
	private Integer ordemDescontoAluno;
	private Boolean ordemDescontoAlunoValorCheio;
	private Integer ordemPlanoDesconto;
	private Boolean ordemPlanoDescontoValorCheio;
	private Integer ordemConvenio;
	private Boolean ordemConvenioValorCheio;
	private Integer ordemDescontoProgressivo;
	private Boolean ordemDescontoProgressivoValorCheio;
	private List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosAplicavesContaReceber;
	/**
	 * TRANSIENT - Criado para mostrar informações para o usuário na tela de
	 * controle de cobrança.
	 */
	private String observacao;
	private String justificativaDesconto;
	/**
	 * Atributos usados na tela de recebimento
	 */
	private Double valorCalculadoDescontoLancadoRecebimento;
	private String tipoDescontoLancadoRecebimento;
	private Double valorDescontoLancadoRecebimento;
	private Double valorAnteriorDescontoLancadoControle;
	private String controleErroDescontoRecebimento;
	/**
	 * Flag que controle se precisa calcular o desconto na tela de recebimento.
	 */
	private Boolean precisaCalcularDesconto;
	/**
	 * Flag que controla se o boleto da conta receber já foi impresso.
	 */
	private Boolean impressaoBoletoRealizada;
	private FaixaDescontoProgressivo descontoProgressivoUtilizado;
        
        /**
         * Os atributos abaixo visam armazenar o valor da conta a receber considerando 
         * as possíveis faixas de descontos que a mesma possui, no momento de sua criação.
         * Ou seja, registra as faixas de descontos originais da conta a receber considerando
         * os descontos progressivos e planos de descontos (com data limite de aplicação)
         * existentes para a mesma. Mesmo com o passar do tempo estes valores não são modificados,
         * permitindo assim gerar informações estratégias, como quando era o valor inicial
         * a ser recebido da conta (na primeira faixa) e quanto de fato foi recebido. Haja vista,
         * que com o passar do tempo o valor a ser pago pelo aluno pode ir crescendo em função do
         * mesmo ir perdendo os descontos progressivos e os planos de descontos.
         */
	private Double valorDescontoCalculadoPrimeiraFaixaDescontos;
	private Date dataLimitePrimeiraFaixaDescontos;
	private Double valorDescontoCalculadoSegundaFaixaDescontos;
	private Date dataLimiteSegundaFaixaDescontos;
	private Double valorDescontoCalculadoTerceiraFaixaDescontos;
	private Date dataLimiteTerceiraFaixaDescontos;
	private Double valorDescontoCalculadoQuartaFaixaDescontos;
	private Date dataLimiteQuartaFaixaDescontos;
        
        
	private Double taxaBoleto;
	private Date dataArquivoRemessa;
	private Boolean usaDescontoCompostoPlanoDesconto;
	/**
	 * Usado para processar arquivo de retorno.
	 */
	private RegistroArquivoVO registroArquivoVO;
	private Double valorCodigoBarraDescSemValidade;
	private Boolean gerarBoletoComDescontoSemValidade;
	private Boolean realizandoRecebimento;
	private Date dataVencimentoDiaUtil;
	private TurmaVO turma;
	private Boolean duplicidadeTratada;
	private FornecedorVO fornecedor;

	
	//Atributo criado para controle de arquivo de retorno
	private Integer contaReceberAgrupada;
	/**
	 * Atributo utilizado para controle ao realizar a atualização de vencimento
	 */
	private Boolean atualizandoVencimento;

	public static final long serialVersionUID = 1L;
	private Boolean telaEdicaoContaReceber;
	private Double valorDescontoCalculado;
	private Boolean notaFiscalEmitida;
	private UsuarioVO usuarioDesbloqueouDescontoRecebimento;
	private Date dataUsuarioDesbloqueouDescontoRecebimento;
	private ProcessamentoIntegracaoFinanceiraDetalheVO processamentoIntegracaoFinanceiraDetalheVO;
	/**
	 * Atributo utilizado na tela de atualização de vencimento
	 * Atributo não persistido
	 * @Autor Carlos
	 */
	private Date dataVencimentoAtualizar; 
	/**
	 * Atributo transiente criado para controlar a origem do recebimento, pois quando um aluno com as 
	 * parcelas já geradas era incluido o desconto de 100% ao repassar na matricula estava executando 
	 * a rotina de geração de parcelas duas vezes gerando assim inconsistencia na rotina do financeiro, 
	 * portanto este campo é marcado como true apenas ao repassar a matricula adicionando novos descontos
	 * 
	 */
	public Boolean realizandoRecebimentoAutomaticoMatricula;
	private Date dataOriginalVencimento;
	private String motivoCancelamento;
	private Date dataCancelamento;
	private UsuarioVO responsavelCancelamentoVO;
	// private String turma;
	private NotaFiscalSaidaServicoVO notaFiscalSaidaServicoVO;
	/**
	 * Desconto usando quando é repassado pela matrícula alterando o plano financeiro do aluno e esta alteração
	 * tenha gerado um valor maior pago pelo aluno, onde se na condição do plano financeiro do curso o campo
	 * abaterValorRateiroComoDescontoRateio esteja marcado o sistema irá aplicar este valor como um desconto
	 * ao invés de abater sobre o valor base da conta a receber.
	 */
	private Double valorDescontoRateio;
	private String nomeDasFormaDePagamentosResumido;
	private String nomeDasFormaDePagamentosCompleto;
	/*
	 * Campo criado para controlar ao repassar pela matricula, se a conta a receber sofreu alteração após ser remetida ( Enviada no arquivo de remessa ) para o banco.
	 * Caso tenha sofrido alteração (No valor, vencimento ou descontros) essa campo recebe o valor true;
	 * */
	private Boolean contaRemetidaComAlteracao;
	private Boolean contaRemetidaComAlteracao_valorBase;
	private Boolean contaRemetidaComAlteracao_valorComAbatimentoAdicionadoOuModificado;
	private Boolean contaRemetidaComAlteracao_valorComAbatimentoRemovido;
	private Boolean contaRemetidaComAlteracao_valorDescProgressivo;
	private Boolean contaRemetidaComAlteracao_dataDescProgressivo;
	private Boolean contaRemetidaComAlteracao_dataVencimento;
	
	private Double valorDescontoDataLimite;
	private Double valorDescontoSemValidade;
	private Date dataLimiteConcessaoDesconto;
	private Boolean verificaRegraBloqueioEmissaoBoleto;
	private Boolean emissaoBloqueada;
	private Boolean nossoNumeroAlterado;
	// Campo usado para ser preenchido pelo dado da tabela nossonumerocontarecer
	private String nrBanco;	
	//private RegistroNegativacaoCobrancaContaReceberVO registroNegativacaoCobrancaContaReceber;
	private String situacaoRegistroNegativacaoCobrancaContaReceber;
	private List<RegistroNegativacaoCobrancaContaReceberItemVO> historicoNegativacoes;
	
	/**
	 * atributo gravado em banco
	 */
	private BigDecimal valorIndiceReajuste;
	private BigDecimal valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa;
	private Boolean indiceReajusteCalculado;
	private List<CentroResultadoOrigemVO> listaCentroResultadoOrigem;
	
	private List<DetalhamentoValorContaVO> detalhamentoValorContaVOs;
	private IndiceReajusteVO indiceReajustePadraoPorAtraso;
	private BigDecimal valorIndiceReajustePorAtraso;
	private boolean liberadoDoIndiceReajustePorAtraso = false;
	private Boolean estornandoRecebimento;
	private Boolean cr_recebidooutronossonumero;
	private String nossoNumeroOriginouBaixa;
	private Date dataCredito;
	private DepartamentoVO departamentoVO;
	private Boolean bloquearPagamentoCartaoPorOutrosDebitos;

	private Boolean possuiRemessa;
	private Boolean considerarDescontoSemValidadeCalculoIndiceReajuste;
	private Boolean realizandoBaixaAutomatica;
	private Date novaDataVencimento;
	private AgenteNegativacaoCobrancaContaReceberVO agenteNegativacaoCobrancaContaReceberVO;

	/**
	 * Construtor padrão da classe <code>ContaReceber</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public ContaReceberVO() {
		super();
	}

	public Boolean getIsPermiteAutenticarDocumento() {
		if (getSituacao().equals(SituacaoContaReceber.RECEBIDO.getValor())) {
			return true;
		}
		return false;
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>ContaReceberVO</code>. Todos os tipos de consistência de dados são
	 * e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public static void validarDados(ContaReceberVO obj) throws ConsistirException, Exception {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getData() == null) {
			throw new ConsistirException("O campo DATA (Conta a Receber) deve ser informado.");
		}
		if (obj.getTipoPessoa().equals("")) {
			throw new ConsistirException("O campo TIPO PESSOA (Conta a Receber) deve ser informado.");
		}
	
		if (obj.getTipoPessoa().equals("AL") && !obj.getMatriculaAluno().getMatricula().equals("") && !obj.getTipoOrigem().equals("BCC")) {
			if (obj.getPessoa().getCodigo() == 0) {
				obj.setPessoa(obj.getMatriculaAluno().getAluno());
			}

		} else if (obj.getTipoPessoa().equals("AL") && obj.getMatriculaAluno().getMatricula().equals("")) {
			if (new Pessoa().verificarPessoaRequisitante(obj.getPessoa().getCodigo()) == null || !new Pessoa().verificarPessoaRequisitante(obj.getPessoa().getCodigo())) {
				throw new ConsistirException("O campo ALUNO (Conta a Receber) deve ser informado.");
			}
		}
		if (obj.getTipoPessoa().equals("FO") && obj.getFornecedor().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo FORNECEDOR (Conta a Receber) deve ser informado.");
		}
		if (obj.getTipoPessoa().equals("FU") && obj.getFuncionario().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo FUNCIONÁRIO (Conta a Receber) deve ser informado.");
		} else if (obj.getTipoPessoa().equals("FU")) {
			obj.setPessoa(obj.getFuncionario().getPessoa());
		}
		if (obj.getTipoPessoa().equals("CA") && obj.getCandidato().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo CANDIDATO (Conta a Receber) deve ser informado.");
		} else if (obj.getTipoPessoa().equals("CA")) {
			obj.setPessoa(obj.getCandidato());
		}
		if (obj.getTipoPessoa().equals("RE") && obj.getPessoa().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo REQUERENTE (Conta a Receber) deve ser informado.");
		} else if (obj.getTipoPessoa().equals("RE")) {
			obj.setPessoa(obj.getPessoa());
		}
		if (obj.getTipoPessoa().equals("RF") && obj.getResponsavelFinanceiro().getCodigo() == 0) {
			throw new ConsistirException("O campo RESPONSÁVEL FINANCEIRO (Conta a Receber) deve ser informado.");
		}
		if (obj.getTipoPessoa().equals("PA") && obj.getParceiroVO().getCodigo() == 0) {
			throw new ConsistirException("O campo PARCEIRO (Conta a Receber) deve ser informado.");
		}
		
		if (obj.getUnidadeEnsino().getCodigo() == 0) {
			throw new ConsistirException("O campo UNIDADE ENSINO (Conta a Receber) deve ser informado.");
		}
		
		if (!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoFinanceira())) {
			if(!obj.getTipoOrigem().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO.getValor())
					&& !obj.getTipoOrigem().equals(TipoOrigemContaReceber.MENSALIDADE.getValor()) 
					&& !obj.getTipoOrigem().equals(TipoOrigemContaReceber.MATRICULA.getValor())){
				obj.setUnidadeEnsinoFinanceira(obj.getUnidadeEnsino());
			}else{
				throw new ConsistirException("O campo UNIDADE ENSINO FINANCEIRA (Conta a Receber) deve ser informado.");	
			}
		}else if(Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoFinanceira())
 					&& !obj.getUnidadeEnsino().getCodigo().equals(obj.getUnidadeEnsinoFinanceira().getCodigo())
					&& !obj.getTipoOrigem().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO.getValor())
					&& !obj.getTipoOrigem().equals(TipoOrigemContaReceber.MENSALIDADE.getValor()) 
					&& !obj.getTipoOrigem().equals(TipoOrigemContaReceber.NEGOCIACAO.getValor()) 
					&& !obj.getTipoOrigem().equals(TipoOrigemContaReceber.MATRICULA.getValor())){
			obj.setUnidadeEnsinoFinanceira(obj.getUnidadeEnsino());
		}
		
		if (obj.getSituacao().equals("")) {
			throw new ConsistirException("O campo SITUAÇÃO (Conta a Receber) deve ser informado.");
		}
		if (obj.getParcela().equals("")) {
			throw new ConsistirException("O campo PARCELA (Conta a Receber) deve ser informado.");
		}
		if (obj.getDataVencimento() == null) {
			throw new ConsistirException("O campo DATA DE VENCIMENTO (Conta a Receber) deve ser informado.");
		}		

		if (obj.getTipoDesconto().equals("")) {
			obj.setTipoDesconto("PO");
		}

		if ((obj.getTipoOrigem().equals("OUT")) && obj.getValor().equals(0.0)) {
			throw new ConsistirException(UteisJSF.internacionalizar("msg_contaReceberValorManual"));
		}
		
		if ((obj.getContaCorrente() == null) || (obj.getContaCorrente().intValue() == 0)) {
			throw new ConsistirException("O campo CONTA CORRENTE (Conta a Receber) deve ser informado. Ela será utilizada na geração do boleto bancário");
		}
		
		if ((obj.getCentroReceita() == null) || (obj.getCentroReceita().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo CENTRO DE RECEITA (Conta a Receber) deve ser informado.");
		}
		
		if((obj.getTipoPessoa().equals("AL") || obj.getTipoPessoa().equals("RF")) &&  !obj.getTipoOrigem().equals("BCC") && !obj.getMatriculaAluno().getMatricula().equals("") && !obj.getPessoa().getCodigo().equals(obj.getMatriculaAluno().getAluno().getCodigo())) {
			throw new ConsistirException("A pessoa da Conta A Receber é diferente da pessoa matriculada (Nosso Número = " + obj.getNossoNumero() + ").");
		}
		
		if(obj.isContaReceberReferenteMaterialDidatico() &&  obj.getMatriculaAluno().getMatricula().isEmpty()) {
			throw new ConsistirException("Deve ser informada o Número Matrícula do aluno.");
		}
		//Adicionado a validacao para a conta receber qnd a mesma esta com a situacao a receber e é dado um desconto maior que o valor pago e nesse momento ao clicar no botao regegar boleto esta sendo possivel gravar a conta ficando com a situacao recebida.
		Uteis.checkState(!obj.getEstornandoRecebimento() && obj.getSituacaoAReceber() && !Uteis.isAtributoPreenchido(obj.getValorReceberCalculado()), "Não é possível alterar a conta a receber, pois o valor da conta esta zerado. Realize o recebimento para baixa do boleto!");

	}

	public boolean isHabilitarBotaoImprimir() {
		try {
			return SituacaoContaReceber.A_RECEBER.getValor().equals(getSituacao()) && !getContaCorrenteVO().getCarteira().equals("112");
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isHabilitarBotaoRecebimento() {
		try {
			return SituacaoContaReceber.A_RECEBER.getValor().equals(getSituacao()) && !getContaCorrenteVO().getCarteira().equals("112") && (getContaReceberAgrupada().intValue() == 0) && this.getCodigo().intValue() > 0 && !getPossuiRegistroNegativacao();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isHabilitarBotaoRecebimentoAgrupado() {
		try {
			return SituacaoContaReceber.A_RECEBER.getValor().equals(getSituacao()) && !getContaCorrenteVO().getCarteira().equals("112") && (getContaReceberAgrupada().intValue() > 0);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isAgrupado() {
		try {
			return getContaReceberAgrupada().intValue() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean isHabilitarBotaoRecebimentoVisaoAluno() {
		try {
			if (Uteis.nrDiasEntreDatas(getDataVencimento(), new Date()) >= 0 && getSituacaoAReceber()) {    
				return true;
			} else {
				return false;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean getHabilitarBotaoLinkBanco() {
		boolean habilitar = true;
		if (!SituacaoContaReceber.A_RECEBER.getValor().equals(getSituacao())) {
			habilitar = false;
		}
		if (!getContaCorrenteVO().getCarteiraRegistrada() || (getContaCorrenteVO().getCarteiraRegistrada() && getContaCorrenteVO().getPermiteEmissaoBoletoVencido())) {
			habilitar = false;
		}
		if (getContaCorrenteVO().getControlarBloqueioEmissaoBoleto()) {
			habilitar = false;
		}
		if (getContaCorrenteVO().getCarteiraRegistrada() && !getContaCorrenteVO().getCarteiraRegistradaEmissaoBoletoBanco() && !getContaCorrenteVO().getPermiteEmissaoBoletoVencido()) {
			if (getDataVencimento().after(new Date()) || Uteis.getData(getDataVencimento()).equals(Uteis.getData(new Date()))) {
				habilitar = false;
			} else {
				habilitar = true;
			}
			if (getDataVencimento().before(new Date()) && !getContaCorrenteVO().getBloquearEmissaoBoletoFimDeSemana().booleanValue() && !Uteis.isDiaDaSemana(getDataVencimento())) {
				habilitar = false;
			}
			try {
				Date novaData = getDataVencimento();
				novaData = Uteis.getDateSemHora(novaData);
				int diaSemana = getDataVencimento().getDay();
				if (diaSemana == 0) {
					novaData = Uteis.obterDataAvancada(novaData, 1);
				}
				if (diaSemana == 6) {
					novaData = Uteis.obterDataAvancada(novaData, 2);
				}
				if (Uteis.getDateSemHora(new Date()).compareTo(Uteis.getDateSemHora(novaData)) == 0) {
					habilitar = false;
				}
			} catch (Exception e) {
				habilitar = true;
			}		
		}
//		return SituacaoContaReceber.A_RECEBER.getValor().equals(getSituacao()) && getContaCorrenteVO().getCarteiraRegistrada() && getDataVencimento().before(new Date());
		return habilitar;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void criarBoleto(ContaCorrenteVO contaCorrenteVO) throws Exception {
		try {
			JBoletoBean jBoletoBean = new JBoletoBean();
			jBoletoBean.setDataDocumento(Uteis.getDataAno4Digitos(getData()));
			jBoletoBean.setDataProcessamento(Uteis.getDataAno4Digitos(new Date()));
			if(Uteis.isAtributoPreenchido(getProcessamentoIntegracaoFinanceiraDetalheVO().getCodigo())){
				jBoletoBean.setDataVencimento(Uteis.getDataAno4Digitos(getProcessamentoIntegracaoFinanceiraDetalheVO().getDataMaximaPagamento()));
			}else{
				jBoletoBean.setDataVencimento(Uteis.getDataAno4Digitos(getDataVencimento()));
			}
			jBoletoBean.setAgencia(contaCorrenteVO.getAgencia().getNumeroAgencia());
			jBoletoBean.setDvContaCorrente(contaCorrenteVO.getDigito());
			jBoletoBean.setContaCorrente(contaCorrenteVO.getNumero());
			jBoletoBean.setCarteira(contaCorrenteVO.getCarteira());
			jBoletoBean.setNumConvenio(contaCorrenteVO.getConvenio());
			jBoletoBean.setCedente(contaCorrenteVO.getCodigoCedente());
			
			if(Uteis.isAtributoPreenchido(getProcessamentoIntegracaoFinanceiraDetalheVO().getCodigo())){
				if(getProcessamentoIntegracaoFinanceiraDetalheVO().getTipoLayoutArquivo().equals(LayoutArquivoIntegracaoFinanceiraEnum.LAYOUT_DESC_GERAL) || getProcessamentoIntegracaoFinanceiraDetalheVO().getTipoLayoutArquivo().equals(LayoutArquivoIntegracaoFinanceiraEnum.LAYOUT_DESC_GERAL_2)){
					jBoletoBean.setValorBoleto(String.valueOf(getValor()+getJuro()+getMulta()+getAcrescimo()+getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue()+ getValorIndiceReajustePorAtraso().doubleValue()-getValorDescontoAlunoJaCalculado()));
				}else if(getProcessamentoIntegracaoFinanceiraDetalheVO().getTipoLayoutArquivo().equals(LayoutArquivoIntegracaoFinanceiraEnum.LAYOUT_DESC_DETALHADO)){
					jBoletoBean.setValorBoleto(String.valueOf(getValor()+ getAcrescimo() + getValorIndiceReajustePorAtraso().doubleValue() + getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue()));
				}
			}else if (getGerarBoletoComDescontoSemValidade()) {
				jBoletoBean.setValorBoleto(String.valueOf(getValorCodigoBarraDescSemValidade() + getAcrescimo() + getValorIndiceReajustePorAtraso().doubleValue() + getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue()));
			} else {
				jBoletoBean.setValorBoleto(String.valueOf(getValor() + getAcrescimo() + getValorIndiceReajustePorAtraso().doubleValue() + getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue()));
			}

			BoletoBanco banco = BancoFactory.getBoletoInstancia(contaCorrenteVO.getAgencia().getBanco().getNrBanco(), jBoletoBean);
			if (banco instanceof BancoBrasil) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 10);
			} else if (banco instanceof Unibanco) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 14);
			} else if (banco instanceof Hsbc) {
				jBoletoBean.setCodCliente(contaCorrenteVO.getCodigoCedente());
				// Não devem ser calculados os dígitos verificadores e o tipo identificador na linha
				// digitável e no código de brarras.
				// Qualquer dúvida acessar o documento no link na página 7
				// http://www.hsbc.com.br/1/2/br/para-sua-empresa/empresas/gestao-financeira/recebimentos/cobranca/cobranca-nao-registrada/layout-tecnico
				/** Eu Rodrigo comentei o codigo abaixo, pois ao realizar a substrig para 13 estava gerando o codigo de barra duplicado na base de dados
				 * jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero().substring(0, 13))); 
				 */
				//
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 13);
			} else if (banco instanceof Santander) {
				jBoletoBean.setIOS("0");
				jBoletoBean.setCodCliente(contaCorrenteVO.getCodigoCedente());
				if (getNossoNumero().length() > 8) {
					jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), getNossoNumero().length());
				} else {
					jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 8);
				}				
			} else if (banco instanceof Bradesco) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 11);
			} else if (banco instanceof Daycoval) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 11);
			} else if (banco instanceof Itau) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 8);
			}else if (banco instanceof Banestes) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 10);
			} else if (banco instanceof Sicred) {
				jBoletoBean.setDvAgencia(contaCorrenteVO.getAgencia().getDigito());
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 9);
			} else if (banco instanceof Safra) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 9);
			} else if (banco instanceof Sicoob) {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 8);
			} else if (banco instanceof CaixaEconomica) {
				jBoletoBean.setCedente(String.valueOf(contaCorrenteVO.getCodigoCedente().toString()));
				if (contaCorrenteVO.getAgencia().getBanco().getModeloGeracaoBoleto().equals("SICOB")) {
					banco = BancoFactory.getBoletoInstancia(contaCorrenteVO.getAgencia().getBanco().getNrBanco() + "-S", jBoletoBean);
					jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 8);
				} else if (contaCorrenteVO.getAgencia().getBanco().getModeloGeracaoBoleto().equals("SICOB15")) {
					banco = BancoFactory.getBoletoInstancia(contaCorrenteVO.getAgencia().getBanco().getNrBanco() + "-N15", jBoletoBean);
					jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 14);
				} else {
					jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 15);
				}
			} else {
				jBoletoBean.setNossoNumero(String.valueOf(getNossoNumero()), 14);
			}			
			if (banco != null) {
				setCodigoBarra(banco.getCodigoBarras());
				setLinhaDigitavelCodigoBarras(banco.getLinhaDigitavel());
			}
		} catch (Exception e) {
			////System.out.println(e.getMessage());
			throw new Exception("Ocorreu um erro na geração do nosso número das contas a receber. Provavelmente o nr de carteira e/ou convênio estão errados!");
			// throw e;
		}
	}

	public ReceitaDWVO getReceitaDWVO(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo, InscricaoVO inscricao) {
		ReceitaDWVO obj = new ReceitaDWVO();
		obj.setAno(Integer.parseInt(Uteis.getAnoDataAtual4Digitos()));
		obj.setMes(Uteis.getMesDataAtual());
		obj.getUnidadeEnsino().setCodigo(getUnidadeEnsino().getCodigo());
		obj.setValor(getValorRecebido());
		obj.getCentroReceita().setCodigo(getCentroReceita().getCodigo());
		obj.setData(new Date());
		if (matricula != null) {
			obj.getCurso().setCodigo(matricula.getCurso().getCodigo());
			obj.setNivelEducacional(matricula.getCurso().getNivelEducacional());
			obj.getTurno().setCodigo(matricula.getTurno().getCodigo());
			obj.getAreaConhecimento().setCodigo(matricula.getCurso().getAreaConhecimento().getCodigo());
		}
		if (matriculaPeriodo != null) {
			obj.getProcessoMatricula().setCodigo(matriculaPeriodo.getProcessoMatricula());
		}
		if (inscricao != null) {
			obj.getProcSeletivo().setCodigo(inscricao.getProcSeletivo().getCodigo());
		}
		return obj;
	}

	public NegociacaoRecebimentoVO gerarNegociacaoRecebimentoVOPreenchido(UsuarioVO usuarioVO, PessoaVO pessoaVO, PessoaVO responsavelFinanceiro, FuncionarioVO funcionarioVO, ParceiroVO parceiroVO, ConfiguracaoFinanceiroVO conf) throws Exception {
		NegociacaoRecebimentoVO negociacaoRecebimentoVO = new NegociacaoRecebimentoVO();
		ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO = new ContaReceberNegociacaoRecebimentoVO();
		negociacaoRecebimentoVO.setResponsavel((UsuarioVO)usuarioVO.clone());
		negociacaoRecebimentoVO.setUnidadeEnsino((UnidadeEnsinoVO)getUnidadeEnsinoFinanceira().clone());
		negociacaoRecebimentoVO.setTipoPessoa(getTipoPessoa());
		if (getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor())) {
			negociacaoRecebimentoVO.setFornecedor(getFornecedor());
			negociacaoRecebimentoVO.setPessoa(null);
		} else if (getTipoPessoa().equals(TipoPessoa.PARCEIRO.getValor())) {
			negociacaoRecebimentoVO.setParceiroVO(parceiroVO);
			negociacaoRecebimentoVO.setPessoa(null);
		} else if (getTipoPessoa().equals(TipoPessoa.ALUNO.getValor())) {
			negociacaoRecebimentoVO.setMatricula(getMatriculaAluno().getMatricula());
			negociacaoRecebimentoVO.setPessoa(pessoaVO);
		} else if (getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor())) {
			negociacaoRecebimentoVO.setMatricula(getMatriculaAluno().getMatricula());
			negociacaoRecebimentoVO.setPessoa(responsavelFinanceiro);
		} else if (getTipoPessoa().equals(TipoPessoa.FUNCIONARIO.getValor())) {
//			negociacaoRecebimentoVO.setMatricula(funcionarioVO.getMatricula());
			negociacaoRecebimentoVO.setPessoa(funcionarioVO.getPessoa());
		} else if (getTipoPessoa().equals(TipoPessoa.REQUERENTE.getValor())) {			
			negociacaoRecebimentoVO.setPessoa(pessoaVO);
		} else if (getTipoPessoa().equals(TipoPessoa.CANDIDATO.getValor())) {			
			negociacaoRecebimentoVO.setPessoa(pessoaVO);
		}
		
		Date dataVencimentoOriginal = null;
		//Caso essa opção esteja marcada deve atualizar a data de vencimento para o próximo dia útil para realizar os cálculos 
		//com a data de vencimento com o dia útil. Posteriormente entrar no método "Espírita" é voltada a data de vencimento original.
		if (conf.getVencimentoParcelaDiaUtil()) {
			dataVencimentoOriginal = this.getDataVencimento();
			this.setDataVencimento(this.getDataVencimentoDiaUtil());
		}
		contaReceberNegociacaoRecebimentoVO.setValorTotal(getCalcularValorFinal(conf, usuarioVO));
		if (!this.getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().equals(BigDecimal.ZERO)) {
			this.setValor(this.getValor());
			this.setValorBaseContaReceber(this.getValorBaseContaReceber() + this.getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue());
		}
		contaReceberNegociacaoRecebimentoVO.setContaReceber(this);
		negociacaoRecebimentoVO.adicionarObjContaReceberNegociacaoRecebimentoVOs(contaReceberNegociacaoRecebimentoVO);
		negociacaoRecebimentoVO.calcularTotal(conf, usuarioVO);
		//Após calculado os descontos com a data de vencimento do dia útil eu volto a data de vencimento original
		if (conf.getVencimentoParcelaDiaUtil()) {
			this.setDataVencimento(dataVencimentoOriginal);
			atualizarDataVencimentoAposSerSubstituidaPorDataUtil(negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs(), dataVencimentoOriginal);
		}
		return negociacaoRecebimentoVO;
	}
	
	public void atualizarDataVencimentoAposSerSubstituidaPorDataUtil(List<ContaReceberNegociacaoRecebimentoVO> listaContaReceberNegociacaoRecebimentoVOs, Date dataVencimentoOriginal) {
		for (ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO : listaContaReceberNegociacaoRecebimentoVOs) {
			if (contaReceberNegociacaoRecebimentoVO.getContaReceber().getCodigo().equals(this.getCodigo())) {
				contaReceberNegociacaoRecebimentoVO.getContaReceber().setDataVencimento(dataVencimentoOriginal);
				break;
			}
		}
	}

	public ReceitaDWVO getReceitaDWEstornoVO(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo, InscricaoVO inscricao) {
		ReceitaDWVO obj = new ReceitaDWVO();
		obj.setAno(Integer.parseInt(Uteis.getAnoDataAtual4Digitos()));
		obj.setMes(Uteis.getMesDataAtual());
		obj.getCentroReceita().setCodigo(getCentroReceita().getCodigo());
		obj.getCurso().setCodigo(matricula.getCurso().getCodigo());
		obj.setNivelEducacional(matricula.getCurso().getNivelEducacional());
		obj.getProcessoMatricula().setCodigo(matriculaPeriodo.getProcessoMatricula());
		obj.getProcSeletivo().setCodigo(inscricao.getProcSeletivo().getCodigo());
		obj.getTurno().setCodigo(matricula.getTurno().getCodigo());
		obj.getUnidadeEnsino().setCodigo(getUnidadeEnsino().getCodigo());
		obj.setValor(-1 * getValorRecebido());
		obj.getAreaConhecimento().setCodigo(matricula.getCurso().getAreaConhecimento().getCodigo());
		obj.setData(new Date());
		return obj;
	}

	public Boolean getTipoAluno() {
		if (getTipoPessoa().equals("AL")) {
			return Boolean.TRUE;
		}
		return (Boolean.FALSE);
	}

	public Boolean getTipoResponsavelFinanceiro() {
		return getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor());
	}

	public Boolean getTipoFornecedor() {
		return getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor());
	}

	public Boolean getTipoRequerente() {
		if (getTipoPessoa().equals("RE")) {
			return Boolean.TRUE;
		}
		return (Boolean.FALSE);
	}

	public Boolean getTipoFuncionario() {
		if (getTipoPessoa().equals("FU")) {
			return (Boolean.TRUE);
		}
		return (Boolean.FALSE);
	}

	public Boolean getTipoParceiro() {
		if (getTipoPessoa().equals("PA")) {
			return (Boolean.TRUE);
		}
		return (Boolean.FALSE);
	}

	public Boolean getTipoCandidato() {
		if (getTipoPessoa().equals("CA")) {
			return (Boolean.TRUE);
		}
		return (Boolean.FALSE);
	}

	public Boolean getEdicao() {
		if (getSituacao().equals("AR")) {
			return false;
		}
		return true;
	}

	public Boolean getPossuiOrigem() {
		return  Uteis.isAtributoPreenchido(getCodigo()) && Uteis.isAtributoPreenchido(getTipoOrigem());
	}

	public PessoaVO getCandidato() {
		if (candidato == null) {
			candidato = new PessoaVO();
		}
		return candidato;
	}

	public void setCandidato(PessoaVO candidato) {
		this.candidato = candidato;
	}

	public DescontoProgressivoVO getDescontoProgressivo() {
		if (descontoProgressivo == null) {
			descontoProgressivo = new DescontoProgressivoVO();
		}
		return descontoProgressivo;
	}

	public void setDescontoProgressivo(DescontoProgressivoVO descontoProgressivo) {
		this.descontoProgressivo = descontoProgressivo;
	}

	public Boolean getExisteDescontoProgressivo() {
		if (getDescontoProgressivo().getCodigo().intValue() != 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public String getDescricaoDescontoProgressivo1() {
		String descricao = "";
		if (getDescontoProgressivo().getCodigo().intValue() != 0) {
			descricao = descricao + "Desconto de " + Uteis.getDoubleFormatado(Uteis.arredondar(getDescontoProgressivo().getPercDescontoLimite1(), 2, 0)) + "% até " + getDescontoProgressivo().getDiaLimite1() + " dias antes do vencimento da fatura\n";
			descricao += getDescricaoDescontoProgressivo2();
			descricao += getDescricaoDescontoProgressivo3();
			descricao += getDescricaoDescontoProgressivo4();
		}
		return descricao;
	}

	public String getDescricaoDescontoProgressivo2() {
		String descricao = "";
		if ((getDescontoProgressivo().getCodigo().intValue() != 0) && (!getDescontoProgressivo().getPercDescontoLimite2().equals(0.0))) {
			descricao = descricao + "Desconto de " + Uteis.getDoubleFormatado(Uteis.arredondar(getDescontoProgressivo().getPercDescontoLimite2(), 2, 0)) + "% até " + getDescontoProgressivo().getDiaLimite2() + " dias antes do vencimento da fatura\n";
		}
		return descricao;
	}

	public String getDescricaoDescontoProgressivo3() {
		String descricao = "";
		if ((getDescontoProgressivo().getCodigo().intValue() != 0) && (!getDescontoProgressivo().getPercDescontoLimite3().equals(0.0))) {

			descricao = descricao + "Desconto de " + Uteis.getDoubleFormatado(Uteis.arredondar(getDescontoProgressivo().getPercDescontoLimite3(), 2, 0)) + "% até " + getDescontoProgressivo().getDiaLimite3() + " dias antes do vencimento da fatura\n";
		}
		return descricao;
	}

	public String getDescricaoDescontoProgressivo4() {
		String descricao = "";
		if ((getDescontoProgressivo().getCodigo().intValue() != 0) && (!getDescontoProgressivo().getPercDescontoLimite4().equals(0.0))) {
			descricao = descricao + "Desconto de " + Uteis.getDoubleFormatado(Uteis.arredondar(getDescontoProgressivo().getPercDescontoLimite4(), 2, 0)) + "% até " + getDescontoProgressivo().getDiaLimite4() + " dias antes do vencimento da fatura\n";
		}
		return descricao;
	}

	public Long getNrDiasAtraso() {
		Long dias = Uteis.nrDiasEntreDatas(new Date(), getDataVencimento());
		if (dias > 0) {
			return dias;
		}
		return 0l;
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>PlanoDescontoContaReceberVO</code> ao List
	 * <code>PlanoDescontoContaReceberVOs</code>. Utiliza o atributo padrão de
	 * consulta da classe <code>PlanoDescontoContaReceber</code> - getRtRLog() -
	 * como identificador (key) do objeto no List.
	 * 
	 * @param obj
	 *            Objeto da classe <code>PlanoDescontoContaReceberVO</code> que
	 *            será adiocionado ao Hashtable correspondente.
	 */
	public void adicionarObjPlanoDescontoContaReceberVOs(PlanoDescontoContaReceberVO obj) throws Exception {
		PlanoDescontoContaReceberVO.validarDados(obj);
		getPlanoDescontoContaReceberVOs().add(obj);
		Ordenacao.ordenarLista(getPlanoDescontoContaReceberVOs(), "ordenacao");
	}
	
	public void alterarObjPlanoDescontoContaReceberVOs(PlanoDescontoContaReceberVO obj, Integer indice) throws Exception {
		PlanoDescontoContaReceberVO.validarDados(obj);
		getPlanoDescontoContaReceberVOs().set(indice, obj);
		Ordenacao.ordenarLista(getPlanoDescontoContaReceberVOs(), "ordenacao");
	}

	public void adicionarListaPlanoDescontoContaReceberVOComBaseItensPlanoDescontoMatricula(PlanoFinanceiroAlunoVO planoFinanceiroAluno,MatriculaPeriodoVO matriculaPeriodoVO,  MatriculaPeriodoVencimentoVO parcelaMatricula) throws Exception {
		if(parcelaMatricula.getTipoOrigemMatriculaPeriodoVencimento().isMaterialDidatico() && (!matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isAplicarDescontosParcelasNoMaterialDidatico() )){
			return;
		}
		Double valorInstituicaoTMP = getValorDescontoInstituicao();
		Double valorConvenioTMP = getValorDescontoConvenio();
		for (ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoVO : planoFinanceiroAluno.getItemPlanoFinanceiroAlunoVOs()) {
			PlanoDescontoContaReceberVO planoContaReceberVO = new PlanoDescontoContaReceberVO();
			planoContaReceberVO.setTipoItemPlanoFinanceiro(itemPlanoFinanceiroAlunoVO.getTipoItemPlanoFinanceiro());
			if (itemPlanoFinanceiroAlunoVO.getTipoPlanoFinanceiroDescontoInstitucional() && 
					(!parcelaMatricula.getTipoOrigemMatriculaPeriodoVencimento().isMaterialDidatico() || 
					(parcelaMatricula.getTipoOrigemMatriculaPeriodoVencimento().isMaterialDidatico() && matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isAplicarDescontoMaterialDidaticoDescontoInstitucional()))) {
				String parcelaBase = getParcela().contains("/")? getParcela().substring(0, getParcela().indexOf("/")): getParcela();
				if(itemPlanoFinanceiroAlunoVO.getPlanoDesconto().getAplicarDescontoApartirParcela() <= 1 
				|| !Uteis.getIsValorNumerico(parcelaBase)
				|| (itemPlanoFinanceiroAlunoVO.getPlanoDesconto().getAplicarDescontoApartirParcela() > 1 						
					&& Integer.valueOf(parcelaBase) >= (itemPlanoFinanceiroAlunoVO.getPlanoDesconto().getAplicarDescontoApartirParcela()))){
					planoContaReceberVO.setPlanoDescontoVO(itemPlanoFinanceiroAlunoVO.getPlanoDesconto());
					if(valorInstituicaoTMP > 0){
						planoContaReceberVO.setUtilizado(true);
						planoContaReceberVO.setValorUtilizadoRecebimento(valorInstituicaoTMP);
						valorInstituicaoTMP = 0.0;
					}
					this.adicionarObjPlanoDescontoContaReceberVOs(planoContaReceberVO);
				}
			}else if (itemPlanoFinanceiroAlunoVO.getTipoPlanoFinanceiroConvenio() && 
					(!parcelaMatricula.getTipoOrigemMatriculaPeriodoVencimento().isMaterialDidatico() || 
					(parcelaMatricula.getTipoOrigemMatriculaPeriodoVencimento().isMaterialDidatico() 
							&& matriculaPeriodoVO.getCondicaoPagamentoPlanoFinanceiroCurso().isAplicarDescontoMaterialDidaticoDescontoConvenio()
							&& itemPlanoFinanceiroAlunoVO.getConvenio().getParceiro().isCusteaParcelasMaterialDidatico()))) {
				planoContaReceberVO.setConvenio(itemPlanoFinanceiroAlunoVO.getConvenio());
					if(valorConvenioTMP > 0){
					planoContaReceberVO.setUtilizado(true);
					planoContaReceberVO.setValorUtilizadoRecebimento(valorConvenioTMP);
					valorConvenioTMP = 0.0;
				}
				this.adicionarObjPlanoDescontoContaReceberVOs(planoContaReceberVO);
			}
		}
	}

	@SuppressWarnings("element-type-mismatch")
	public void excluirObjPlanoDescontoContaReceberVOs(Integer planoDesconto) throws Exception {
		int index = 0;
		Iterator i = getPlanoDescontoContaReceberVOs().iterator();
		while (i.hasNext()) {
			PlanoDescontoContaReceberVO objExistente = (PlanoDescontoContaReceberVO) i.next();
			if (objExistente.getPlanoDescontoVO().getCodigo().intValue() == planoDesconto) {
				getPlanoDescontoContaReceberVOs().remove(index);
				return;
			}
			index++;
		}
	}

	/**
	 * Operação responsável por adicionar um novo objeto da classe
	 * <code>CrRRtRLogVO</code> ao List <code>crRRtRLogVOs</code>. Utiliza o
	 * atributo padrão de consulta da classe <code>CrRRtRLog</code> -
	 * getRtRLog() - como identificador (key) do objeto no List.
	 * 
	 * @param obj
	 *            Objeto da classe <code>CrRRtRLogVO</code> que será adiocionado
	 *            ao Hashtable correspondente.
	 */
	public void adicionarObjContaReceberRecebimentoVOs(ContaReceberRecebimentoVO obj) throws Exception {
		ContaReceberRecebimentoVO.validarDados(obj);
		int index = 0;
		for (ContaReceberRecebimentoVO objExistente : getContaReceberRecebimentoVOs()) {
			if (objExistente.getFormaPagamentoNegociacaoRecebimento().equals(obj.getFormaPagamentoNegociacaoRecebimento())) {
				getContaReceberRecebimentoVOs().set(index, obj);
				return;
			}
			index++;
		}
		getContaReceberRecebimentoVOs().add(obj);
	}

	@XmlElement(name = "valorFinalCalculado")
	public Double getValorFinalCalculado() {
		if (valorFinalCalculado == null) {
			valorFinalCalculado = 0.0;
		}
		if (valorFinalCalculado < 0) {
			valorFinalCalculado = 0.0;
		}
		return valorFinalCalculado;
	}

	public void setValorFinalCalculado(Double valorFinalCalculado) {
		this.valorFinalCalculado = valorFinalCalculado;
	}

	public Double getCalcularValorFinal(ConfiguracaoFinanceiroVO conf, UsuarioVO usuario) throws Exception {
		return getCalcularValorFinal(null, conf, usuario);
	}

	public Double getCalcularValorFinal(Date dataOcorrencia, ConfiguracaoFinanceiroVO conf, UsuarioVO usuario) throws Exception {
		return getCalcularValorFinal(dataOcorrencia, conf, false, usuario);
	}
        
	public void validarValoresAcrescimoDescontosERecebidoContaReceber() throws Exception {
            double totalDescontos = this.getValorTotalDescontoContaReceber();
            double totalAcrescimos = this.getTotalAcrescimoContaReceber();
            double valorRecebidoProjetado = Uteis.arrendondarForcando2CadasDecimais(this.valor + totalAcrescimos - totalDescontos); 
            double valorDiferenca = Uteis.arrendondarForcando2CadasDecimais(valorRecebidoProjetado - this.getValorRecebido());
            if (valorDiferenca != 0.0) {
                if (valorDiferenca > 0) {
                    if (valorDiferenca >= 0.01) {
                        return;
                    }
                } else {
                    if (valorDiferenca <= -0.01) {
                        return;
                    }
                }
                throw new Exception("Conta a receber com valores dos descontos - acrescimos - valor recebido - inválidos");
            }
        }

	public Double getCalcularValorFinal(Date dataOcorrencia, ConfiguracaoFinanceiroVO conf, boolean porcentagemMultaAlterada, UsuarioVO usuario) throws Exception {
		return getCalcularValorFinal(dataOcorrencia, conf, false, new Date(), usuario);
	}
	
	public void inicializarCodigosSimuladosPlanosDescontoManual() {
		for (PlanoDescontoContaReceberVO plano : this.getPlanoDescontoContaReceberVOs()) {
			if ((plano.getIsPlanoDescontoManual()) && (plano.getPlanoDescontoVO().getCodigo().equals(0))) {
				Integer codigoSimuladoPlanoDesconto = this.obterProximoCodigoSimuladoDescontoManualRegistradosConta();
				// é importante definir esse codigo simulado para o desconto manual, pois o método espírita depois de calcular
				// os descontos a serem aplicados, volta nestes mesmos planos para registrar o valor calculado de desconto
				// para cada objeto do PlanoDescontoContaReceberVO (valorUtilizadoRecebimento). Assim, precisamos distinguir
				// cada desconto manual um do outro, por meio desse codigo simulado. Assim o metodo irá trabalhar corretamente na
				// atualizacao do valor no PlanoDescontoContaReceber.
				plano.getPlanoDescontoVO().setCodigo(codigoSimuladoPlanoDesconto);
			}
		}
	}
        
	public Double getCalcularValorFinal(Date dataOcorrencia, ConfiguracaoFinanceiroVO conf, boolean porcentagemMultaAlterada, Date dataCalcular, UsuarioVO usuarioVO) throws Exception {	
		if (dataCalcular == null) {
			dataCalcular = new Date();
		}
		
		inicializarCodigosSimuladosPlanosDescontoManual();
		
		if (getSituacao().equals("RE")) {
			return this.getValorRecebidoTituloQuitado();
		}
		if(Uteis.isAtributoPreenchido(getProcessamentoIntegracaoFinanceiraDetalheVO().getCodigo())){
			Double valorFinal = getValor()+getJuro()+getMulta()+getAcrescimo() + getValorIndiceReajustePorAtraso().doubleValue() - getValorDescontoAlunoJaCalculado();
			setValorRecebido(Uteis.arredondar(valorFinal, 2, 0));
			return valorFinal;
		}
		setValorRecebido(0.0);
		if (!this.getTelaEdicaoContaReceber() && !this.getRealizandoRecebimento() && !getParcela().contains("EX")) {
			//Foi verificado que ao realizar o estorno de uma conta receber e sendo zerado o campo valordesconto e ao repassar no calculo da contareceber
			//esse valor estaria recebendo o desconto salvo no plano financeiro do aluno porem se o mesmo foi alterado depois que essa conta foi recebida				
			//perdendo assim o desconto dado anterior entao foi criado a nova verificação para apenas usar o desconto do plano do aluno caso e
			if (!Uteis.isAtributoPreenchido(this.getValorDesconto()) && this.getTipoOrigem().equals(TipoOrigemContaReceber.MENSALIDADE.getValor())) {
				if (this.getMatriculaAluno().getPlanoFinanceiroAluno().getTipoDescontoParcela().equals("PO")) {
					this.setTipoDesconto("PO");
					this.setValorDesconto(this.getMatriculaAluno().getPlanoFinanceiroAluno().getPercDescontoParcela());
				} else {
					this.setTipoDesconto("VA");
					this.setValorDesconto(this.getMatriculaAluno().getPlanoFinanceiroAluno().getValorDescontoParcela());
				}
			} else if (!Uteis.isAtributoPreenchido(this.getValorDesconto()) && this.getTipoOrigem().equals(TipoOrigemContaReceber.MATRICULA.getValor())) {
				if (this.getMatriculaAluno().getPlanoFinanceiroAluno().getTipoDescontoMatricula().equals("PO")) {
					this.setTipoDesconto("PO");
					this.setValorDesconto(this.getMatriculaAluno().getPlanoFinanceiroAluno().getPercDescontoMatricula());
				} else {
					this.setTipoDesconto("VA");
					this.setValorDesconto(this.getMatriculaAluno().getPlanoFinanceiroAluno().getValorDescontoMatricula());
				}
			}
			
			
		}
		if ((getTipoOrigem().equals("MAT") || getTipoOrigem().equals("MEN") || getTipoOrigem().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO.getValor()))  && this.getMatriculaAluno().getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela() && this.getRealizandoRecebimento() ) {
			if (Uteis.getDateSemHora(this.getDataVencimento()).before(Uteis.getDateSemHora(dataCalcular))) {				
				setValorDescontoAlunoJaCalculado(0.0);
			}
		}
		ContaReceber.montarListaDescontosAplicaveisContaReceber(this, dataCalcular, conf.getUsaDescontoCompostoPlanoDesconto(), conf, null, null);
		/**
         * Adicionada regra caso no planoFinanceiroAluno esteja marcado
         * como descontoValidoAteDataParcela zere o valor do desconto do
         * aluno, devido a existir uma insconsistência no ato de
         * realizar os cálculos financeiros da ContaReceber e o mesmo
         * não poder ser solucionado através da passagem deste parâmetro
         * do PlanoFinanceiroAluno.
         */
		setDataLimiteAplicacaoDesconto(getDataVencimento());
		for (Iterator<PlanoFinanceiroAlunoDescricaoDescontosVO> iterator = getListaDescontosAplicavesContaReceber().iterator(); iterator.hasNext();) {
			PlanoFinanceiroAlunoDescricaoDescontosVO obj = iterator.next();
			if(obj.getDataInicioAplicacaoDesconto().compareTo(Uteis.getDateSemHora(new Date())) > 0 && obj.getDataInicioAplicacaoDesconto().compareTo(Uteis.getDateSemHora(getDataVencimento())) <= 0) {
				setDataLimiteAplicacaoDesconto(obj.getDataLimiteAplicacaoDesconto());
			}
			if (obj.executarCalculoValorTotalDesconto().equals(0.0) && obj.getDiaNrAntesVencimento() > 0) {
				iterator.remove();
			}
			if((getTipoOrigem().equals("MAT") || getTipoOrigem().equals("MEN") || getTipoOrigem().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO.getValor()))  && obj.getValorDescontoAluno() > 0 
					&& this.getMatriculaAluno().getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela() 
					&& obj.getDiaNrAntesVencimento() <= 0 && Uteis.getDateSemHora(this.getDataVencimento()).before(Uteis.getDateSemHora(dataCalcular))){				
				setValorDescontoAlunoJaCalculado(0.0);
			}
		}
		Double valorFinal = getValorFinalCalculadoSemJurosEMulta();
		if(valorFinal < 0){
			valorFinal  = 0.0;
		}
		Long diasAtraso = 0L;
		Date dataVencimentoParcela= null;
		if (conf.getVencimentoParcelaDiaUtil()) {		
			// rotina acrescentado aqui pois o calculo pra data de vencimento dia util so estava sendo validada qndo o mesmo passava por o metodo de montar dados completo deixando assim erro no sistema.
			dataVencimentoParcela = ContaReceber.obterDataVerificandoDiaUtilStatic(getDataVencimento(), getUnidadeEnsino().getCidade().getCodigo(), null);
			if (dataOcorrencia == null) {
				diasAtraso = Uteis.nrDiasEntreDatas(Uteis.getDataMinutosHoraDia(dataCalcular), Uteis.getDataMinutosHoraDia(dataVencimentoParcela));
			} else {
				diasAtraso = Uteis.nrDiasEntreDatas(Uteis.getDataMinutosHoraDia(dataOcorrencia), Uteis.getDataMinutosHoraDia(dataVencimentoParcela));
			}
		} else {
			dataVencimentoParcela = getDataVencimento();
			if (dataOcorrencia == null) {
				diasAtraso = Uteis.nrDiasEntreDatas(Uteis.getDataMinutosHoraDia(dataCalcular), Uteis.getDataMinutosHoraDia(dataVencimentoParcela));
			} else {
				diasAtraso = Uteis.nrDiasEntreDatas(Uteis.getDataMinutosHoraDia(dataOcorrencia), Uteis.getDataMinutosHoraDia(dataVencimentoParcela));
			}
		}
		validarAplicacaoIndiceReajustePorAtraso(dataCalcular, dataVencimentoParcela, valorFinal, diasAtraso, conf);
		setJuro(0.0);
		setValorJuroCalculado(0.0);
		setMulta(0.0);
		setValorMultaCalculado(0.0);
		if (diasAtraso <= 0) {
			setJuroPorcentagem(0.0);
			setMultaPorcentagem(0.0);
		}else if (diasAtraso > 0) {
			/**
			 * @author Wendel Rodrigues
			 * @since 25 de março de 2015
			 * @version 5.0.3.2
			 * 
			 * Adicionada regra caso na Configuração Financeira esteja marcado Cobrar Juro e Multa Sobre o Valor Cheio da Conta, 
			 * o sistema deverá na hora de calcular o juro e multa considerar o valor cheio da conta e não o valor com os descontos aplicados.
			 */
			Double valorBase = valorFinal;
			if (conf.getCobrarJuroMultaSobreValorCheioConta()) {
				valorBase = getValor()+ getAcrescimo();
			}
			BancoEnum bancoEnum = BancoEnum.getEnum(getContaCorrenteVO().getCnab(), getContaCorrenteVO().getAgencia().getBanco().getNrBanco());
			if (Uteis.isAtributoPreenchido(bancoEnum) && bancoEnum.getPermiteInformarValorMultaParaRemessa()) {
				validarCalculoDeCobrancaMulta(conf, porcentagemMultaAlterada, valorBase);
			} else {
				validarCalculoDeCobrancaMulta(conf, porcentagemMultaAlterada, valorFinal);
			}
			validarCalculoDeCobrancaJuro(conf, diasAtraso, valorBase, getContaCorrenteVO());
			setValorMultaCalculado(getMulta());
			setValorJuroCalculado(getJuro());
			valorFinal = Uteis.arrendondarForcando2CadasDecimais(Uteis.arrendondarForcando2CadasDecimais(valorFinal) + Uteis.arrendondarForcando2CadasDecimais(getJuro()) + Uteis.arrendondarForcando2CadasDecimais(getMulta()));
		}
		if (getPrecisaCalcularDesconto()) {
			valorFinal = executarCalculoValorDescontoRecebido(valorFinal);
		}
		valorFinal = valorFinal + getAcrescimo() + getValorIndiceReajustePorAtraso().doubleValue() + getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue();
        setValorReceberCalculado(Uteis.arredondar(valorFinal, 2, 0));        
		if (!getValorDescontoLancadoRecebimento().equals(0.0)) {
			if (getTipoDescontoLancadoRecebimento().equals("PO")) {
				setValorCalculadoDescontoLancadoRecebimento(Uteis.arredondar(((valorFinal * getValorDescontoLancadoRecebimento()) / 100), 2, 0));
				if (getValorCalculadoDescontoLancadoRecebimento() > Uteis.arredondar(valorFinal, 2, 0)) {
					setValorDescontoLancadoRecebimento(null);
					setValorCalculadoDescontoLancadoRecebimento(null);
					setControleErroDescontoRecebimento("PO");
				} else {
					valorFinal = Uteis.arredondar(valorFinal, 2, 0) - getValorCalculadoDescontoLancadoRecebimento();
					setControleErroDescontoRecebimento("");
				}
			} else {
				setValorCalculadoDescontoLancadoRecebimento(getValorDescontoLancadoRecebimento());
				if (getValorCalculadoDescontoLancadoRecebimento() > Uteis.arredondar(valorFinal, 2, 0)) {
					setValorDescontoLancadoRecebimento(null);
					setValorCalculadoDescontoLancadoRecebimento(null);
					setControleErroDescontoRecebimento("VA");
				} else {
					valorFinal = Uteis.arredondar(valorFinal, 2, 0) - getValorCalculadoDescontoLancadoRecebimento();
					setControleErroDescontoRecebimento("");
				}
			}
		} else {
			setValorCalculadoDescontoLancadoRecebimento(null);
			setControleErroDescontoRecebimento("");
		}
		setValorRecebido(Uteis.arredondar(valorFinal, 2, 0));
		if (this.getValorRecebido() > 0.0) {
			setValorDescontoRecebido(Uteis.arredondar((this.getValorDescontoConvenio() + this.getValorDescontoInstituicao() + this.getValorDescontoProgressivo() + this.getValorDescontoAlunoJaCalculado() + this.getValorCalculadoDescontoLancadoRecebimento()), 2, 0));
		} else if(getValorRecebido() < 0) {
			setValorRecebido(0.0);
			setValorDescontoRecebido(getValor() + getJuro() + getMulta() + getAcrescimo());
		}else{
			setValorDescontoRecebido(0.0);
		}
		return getValorRecebido();
	}

	private void validarCalculoDeCobrancaJuro(ConfiguracaoFinanceiroVO conf, Long diasAtraso, Double valorBase, ContaCorrenteVO contaCorrenteVO) {
		// Calcula Juro
		if(getTipoParceiro() && getParceiroVO().isIsentarJuro()){
			setJuro(0.0);
			setValorJuroCalculado(0.0);
			setJuroPorcentagem(0.0);
		}else {
			if (conf.getTipoCalculoJuro().equals("CO")) {
				if (getJuroPorcentagem().doubleValue() == 0) {
					setJuroPorcentagem(conf.getPercentualJuroPadrao());
				}
				double atraso = (diasAtraso.doubleValue() / 30);
				double valorComJuro = (valorBase * Math.pow(((getJuroPorcentagem() / 100) + 1), Uteis.arredondar(atraso, 7, 0))) - valor;
				setJuro(Uteis.arredondar(valorComJuro, 7, 0));
			} else {
				if (getJuroPorcentagem().doubleValue() == 0) {					
					setJuroPorcentagem(conf.getPercentualJuroPadrao());
				}				
				double valorComJuro = (valorBase * (getJuroPorcentagem() / 100) * 1);
				int casasDecimaisJuro;
				if ((contaCorrenteVO.getAgencia().getBanco().getNrBanco().equals("341") && contaCorrenteVO.getCnab().equals("CNAB400")) 
					|| (contaCorrenteVO.getAgencia().getBanco().getNrBanco().equals("756") && contaCorrenteVO.getCnab().equals("CNAB240"))
					|| (contaCorrenteVO.getAgencia().getBanco().getNrBanco().equals("237") && contaCorrenteVO.getCnab().equals("CNAB400"))
					|| (contaCorrenteVO.getAgencia().getBanco().getNrBanco().equals("001") && contaCorrenteVO.getCnab().equals("CNAB240"))
					|| (contaCorrenteVO.getAgencia().getBanco().getNrBanco().equals("001") && contaCorrenteVO.getCnab().equals("CNAB400"))
					|| (contaCorrenteVO.getAgencia().getBanco().getNrBanco().equals("021") && contaCorrenteVO.getCnab().equals("CNAB400"))
					|| (contaCorrenteVO.getAgencia().getBanco().getNrBanco().equals("707") && contaCorrenteVO.getCnab().equals("CNAB400"))
					|| (contaCorrenteVO.getAgencia().getBanco().getNrBanco().equals("033") && contaCorrenteVO.getCnab().equals("CNAB400"))
					|| (contaCorrenteVO.getAgencia().getBanco().getNrBanco().equals("422") && contaCorrenteVO.getCnab().equals("CNAB400"))
				) {
					casasDecimaisJuro = 2;
				} else {
					casasDecimaisJuro = 7;
				}
				setJuro(Uteis.arredondar((Uteis.arredondarAbaixo((valorComJuro / 30), casasDecimaisJuro, 0) * diasAtraso), 7, 0));
			}
		}
	}

	private void validarCalculoDeCobrancaMulta(ConfiguracaoFinanceiroVO conf, boolean porcentagemMultaAlterada, Double valorBase) {
		// Calcula Multa
		if(getTipoParceiro() && getParceiroVO().isIsentarMulta()){
			setMulta(0.0);
			setValorMultaCalculado(0.0);
			setMultaPorcentagem(0.0);
		}else{
			if (getMultaPorcentagem().doubleValue() == 0 && !porcentagemMultaAlterada) {
				setMultaPorcentagem(conf.getPercentualMultaPadrao());
			}
			double valorComMulta = (valorBase * (getMultaPorcentagem()) / 100);
			setMulta(Uteis.trunc(new BigDecimal(valorComMulta), 2).doubleValue());
		}
	}
	
	public void validarAplicacaoIndiceReajustePorAtraso(Date dataCalcular, Date dataVencimentoParcela, Double valorFinal,  Long diasAtraso , ConfiguracaoFinanceiroVO conf) throws Exception{
		if(conf.isAplicarIndireReajustePorAtrasoContaReceber() 
				&& diasAtraso >= conf.getQtdDiasAplicarIndireReajustePorAtrasoContaReceber()
				&& !isLiberadoDoIndiceReajustePorAtraso()
				&& ((getTipoParceiro() && !getParceiroVO().isIsentarReajusteParcelaVencida()) || getTipoOrigem().equals("OUT") || getTipoOrigem().equals("MEN") || (getTipoOrigem().equals("NCR") && !ContaReceber.consultarSeExisteContaReceberDifenteDoTipoOrigemMensalidademParaAplicarIndiceReajuste(getCodOrigem())))){
			dataVencimentoParcela = UteisData.obterDataFuturaUsandoCalendar(dataVencimentoParcela, conf.getQtdDiasAplicarIndireReajustePorAtrasoContaReceber());
			IndiceReajuste.aplicacaoIndiceReajusteContaReceberPorAtraso(conf.getIndiceReajustePadraoContasPorAtrasoVO().getCodigo(), valorFinal, dataCalcular, dataVencimentoParcela, this);
		}else if((conf.isAplicarIndireReajustePorAtrasoContaReceber() && diasAtraso < conf.getQtdDiasAplicarIndireReajustePorAtrasoContaReceber()) && getTipoParceiro() && getParceiroVO().isIsentarReajusteParcelaVencida()){
			setValorIndiceReajustePorAtraso(BigDecimal.ZERO);
			setIndiceReajustePadraoPorAtraso(new IndiceReajusteVO());
		}
	}

	public double executarCalculoValorDescontoRecebido(double valorFinal) throws ConsistirException {
		Double somaDescontos = Uteis.arredondar((this.getValorDescontoConvenio() + this.getValorDescontoInstituicao() + this.getValorDescontoProgressivo() + this.getValorDescontoAlunoJaCalculado() + getValorDescontoRateio()), 2, 0);
		if (getTipoDesconto().equals("PO")) {
			if (getValorDesconto() > 100) {
				throw new ConsistirException("O valor do desconto não pode ser maior que 100%.");
			}
			// setValorDescontoRecebido(Uteis.arredondar((getValor() -
			// valorFinal), 2, 0));
			// setValorDescontoRecebido(valorFinal *
			// (getValorDescontoAlunoJaCalculado() / 100));
			// valorFinal = valorFinal - (valorFinal * (getValorDesconto() /
			// 100));
			// valorFinal = valorFinal - (getValorDescontoAlunoJaCalculado());
		} else if (getTipoDesconto().equals("VA")) {
			// setValorDescontoRecebido(getValorDesconto());
			// setValorDescontoRecebido(Uteis.arredondar((getValor() -
			// valorFinal), 2, 0));
			// valorFinal = valorFinal - getValorDescontoRecebido();
		}
		// TODO Alberto Correção ordem dos descontos
		if (getValorDescontoRecebido() == null || getValorDescontoRecebido().equals(0.0) || getValorDescontoRecebido() < somaDescontos) {
			// setValorDescontoRecebido(somaDescontos);
		}
		// TODO Alberto Correção ordem dos descontos
		return valorFinal;
	}

	/*@SuppressWarnings("SillyAssignment")
	public void calcularDescontoInstituicao(Date dataOcorrencia) {
		Long diasAntecipados = 0L;
		if (dataOcorrencia == null) {
			try {
				diasAntecipados = Uteis.nrDiasEntreDatas(getDataVencimento(), Uteis.getDateSemHora(new Date()));
			} catch (Exception e) {
				diasAntecipados = Uteis.nrDiasEntreDatas(getDataVencimento(), new Date());
			}
		} else {
			diasAntecipados = Uteis.nrDiasEntreDatas(getDataVencimento(), dataOcorrencia);
		}
		@SuppressWarnings("LocalVariableHidesMemberVariable")
		Double valorDesconto = 0.0;
		// Realiza a iteração da lista de plano desconto e totaliza o valor na
		// variavel descontoInstituicao
		if (!getPlanoDescontoContaReceberVOs().isEmpty()) {
			for (PlanoDescontoContaReceberVO plano : getPlanoDescontoContaReceberVOs()) {
				// Verifica se a conta receber é parcela ou matricula
				if (this.getTipoOrigem().equals("MAT")) {
					// Verifica se Desconto será Aplicado, devido a quantidade
					// de dias.
					if (diasAntecipados >= plano.getPlanoDescontoVO().getDiasValidadeVencimento().intValue()) {
						plano.setUtilizado(Boolean.TRUE);
						// Verificar se o tipo do desconto é % ou Valor
						if (plano.getPlanoDescontoVO().getTipoDescontoMatricula().equals("PO")) {
							if (plano.getPlanoDescontoVO().getPercDescontoMatricula().doubleValue() == 0.0) {
								valorDesconto = valorDesconto;
							} else {
								double valorLocal = (this.getValor().doubleValue() * plano.getPlanoDescontoVO().getPercDescontoMatricula().doubleValue()) / 100;
								valorDesconto = valorDesconto + valorLocal;
							}
						} else {
							valorDesconto = valorDesconto + plano.getPlanoDescontoVO().getPercDescontoMatricula().doubleValue();
						}
					} else {
						plano.setUtilizado(Boolean.FALSE);
						valorDesconto = 0.0;
					}
				} else {
					// Verifica se Desconto será Aplicado, devido a quantidade
					// de dias.
					if (diasAntecipados >= plano.getPlanoDescontoVO().getDiasValidadeVencimento().intValue()) {
						plano.setUtilizado(Boolean.TRUE);
						// Verificar se o tipo do desconto é % ou Valor
						if (plano.getPlanoDescontoVO().getTipoDescontoParcela().equals("PO")) {
							if (plano.getPlanoDescontoVO().getPercDescontoMatricula().doubleValue() == 0.0) {
								valorDesconto = valorDesconto;
							} else {
								double valorLocal = (this.getValor().doubleValue() * plano.getPlanoDescontoVO().getPercDescontoParcela().doubleValue()) / 100;
								valorDesconto = valorDesconto.doubleValue() + valorLocal;
							}
						} else {
							valorDesconto = valorDesconto.doubleValue() + plano.getPlanoDescontoVO().getPercDescontoParcela().doubleValue();
						}
					} else {
						plano.setUtilizado(Boolean.FALSE);
						valorDesconto = 0.0;
					}
				}
			}
			setValorDescontoInstituicao(valorDesconto);
		}
	}*/

	public void verificarUtilizacaoDesconto(Date dataOcorrencia) {
		Long diasAntecipados = 0L;
		if (dataOcorrencia == null) {
			diasAntecipados = Uteis.nrDiasEntreDatas(getDataVencimento(), new Date());
		} else {
			diasAntecipados = Uteis.nrDiasEntreDatas(getDataVencimento(), dataOcorrencia);
		}
		// Realiza a iteração da lista de plano desconto e totaliza o valor na
		// variavel descontoInstituicao
		if (!getPlanoDescontoContaReceberVOs().isEmpty()) {
			for (PlanoDescontoContaReceberVO plano : getPlanoDescontoContaReceberVOs()) {
				// Verifica se a conta receber é parcela ou matricula
				if (this.getTipoOrigem().equals("MAT")) {
					// Verifica se Desconto será Aplicado, devido a quantidade
					// de dias.
					if (diasAntecipados >= plano.getPlanoDescontoVO().getDiasValidadeVencimento().intValue()) {
						plano.setUtilizado(Boolean.TRUE);
					} else {
						plano.setUtilizado(Boolean.FALSE);
					}
				} else {
					// Verifica se Desconto será Aplicado, devido a quantidade
					// de dias.
					if (diasAntecipados >= plano.getPlanoDescontoVO().getDiasValidadeVencimento().intValue()) {
						plano.setUtilizado(Boolean.TRUE);
					} else {
						plano.setUtilizado(Boolean.FALSE);
					}
				}
			}
		}
	}

	public String getTipoDesc() {
		return TipoDescontoAluno.getSimbolo(getTipoDesconto());
	}

	public String getTipoDescLancado() {
		return TipoDescontoAluno.getSimbolo(getTipoDescontoLancadoRecebimento());
	}

	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO(ConfiguracaoFinanceiroVO conf) {
		try {
			return conf;
		} catch (Exception e) {
			return new ConfiguracaoFinanceiroVO();
		}
	}

	/**
	 * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
	 * <code>ContaReceber</code>).
	 */
	@XmlElement(name = "funcionario")
	public FuncionarioVO getFuncionario() {
		if (funcionario == null) {
			funcionario = new FuncionarioVO();
		}
		return (funcionario);
	}

	/**
	 * Define o objeto da classe <code>Pessoa</code> relacionado com (
	 * <code>ContaReceber</code>).
	 */
	public void setFuncionario(FuncionarioVO obj) {
		this.funcionario = obj;
	}

	@XmlElement(name = "tipoPessoa")
	public String getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = "";
		}
		return tipoPessoa;
	}

	public String getTipoPessoa_apresentar() {
		return TipoPessoa.getDescricao(tipoPessoa);
	}

	public void setTipoPessoa(String tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	/**
	 * Retorna o objeto da classe <code>Matricula</code> relacionado com (
	 * <code>ContaReceber</code>).
	 */
	@XmlElement(name = "matriculaAluno")
	public MatriculaVO getMatriculaAluno() {
		if (matriculaAluno == null) {
			matriculaAluno = new MatriculaVO();
		}
		return (matriculaAluno);
	}

	/**
	 * Define o objeto da classe <code>Matricula</code> relacionado com (
	 * <code>ContaReceber</code>).
	 */
	public void setMatriculaAluno(MatriculaVO obj) {
		this.matriculaAluno = obj;
	}
	
	
	public boolean isApresentarCampoMatriculaAluno(){
		return getTipoAluno() ||  (getTipoResponsavelFinanceiro() && Uteis.isAtributoPreenchido(getResponsavelFinanceiro())) || (getTipoParceiro() && Uteis.isAtributoPreenchido(getParceiroVO()));
	}

	/**
	 * Retorna o objeto da classe <code>CentroReceita</code> relacionado com (
	 * <code>ContaReceber</code>).
	 */
	@XmlElement(name = "centroReceita")
	public CentroReceitaVO getCentroReceita() {
		if (centroReceita == null) {
			centroReceita = new CentroReceitaVO();
		}
		return (centroReceita);
	}

	/**
	 * Define o objeto da classe <code>CentroReceita</code> relacionado com (
	 * <code>ContaReceber</code>).
	 */
	public void setCentroReceita(CentroReceitaVO obj) {
		this.centroReceita = obj;
	}

	public String getDadosPessoaAtiva() {
            try {
		TipoPessoa tipoPessoaLocal = TipoPessoa.getEnum(getTipoPessoa());
		if (tipoPessoaLocal != null) {
			switch (tipoPessoaLocal) {
			case ALUNO:
				if (getMatriculaAluno().getMatricula() != null && !getMatriculaAluno().getMatricula().equals("")) {
					if(Uteis.isAtributoPreenchido(getMatriculaAluno().getAluno().getNome())) {
						return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getMatriculaAluno().getAluno().getNome() + " - " + getMatriculaAluno().getMatricula();
					}
					return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getPessoa().getNome() + " - " + getMatriculaAluno().getMatricula();
				}
				return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getPessoa().getNome();
			case RESPONSAVEL_FINANCEIRO:
				if (getMatriculaAluno().getMatricula() != null && !getMatriculaAluno().getMatricula().equals("") && Uteis.isAtributoPreenchido(getMatriculaAluno().getAluno().getNome())) {
					return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getResponsavelFinanceiro().getNome() + " (ALUNO(A) " + getMatriculaAluno().getAluno().getNome() + " - " + getMatriculaAluno().getMatricula() + ")";
				}
				if (getMatriculaAluno().getMatricula() != null && !getMatriculaAluno().getMatricula().equals("") && !Uteis.isAtributoPreenchido(getMatriculaAluno().getAluno().getNome())) {
					return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getResponsavelFinanceiro().getNome() + " (ALUNO(A) " + getPessoa().getNome() + " - " + getMatriculaAluno().getMatricula() + ")";
				}
				return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getResponsavelFinanceiro().getNome();
			case CANDIDATO:
				if (!getCandidato().getNome().equals("")) {
					return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getCandidato().getNome() + " - " + getCandidato().getCPF();
				} else if (getPessoa().getCodigo() != 0) {
					setCandidato(getPessoa());
					return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getCandidato().getNome() + " - " + getCandidato().getCPF();
				}
			case FUNCIONARIO:
				return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getFuncionario().getPessoa().getNome() + " - " + getFuncionario().getMatricula();
			case REQUERENTE:
				return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getPessoa().getNome() + " - " + getPessoa().getCPF();
			case PARCEIRO: {
				StringBuilder sacado = new StringBuilder("");
				
				if ((getConvenio() != null) && (!getConvenio().getCodigo().equals(0))) {
					sacado = new StringBuilder("PARCEIRO - " + getConvenio().getParceiro().getNome() + " - " + getConvenio().getParceiro().getCNPJ() + " " + getConvenio().getParceiro().getCPF());
					
				} else {
					sacado = new StringBuilder("PARCEIRO - " + this.getParceiroVO().getNome() + " - " + this.getParceiroVO().getCNPJ() + " " + this.getParceiroVO().getCPF());					
				}
				if(!getMatriculaAluno().getMatricula().trim().isEmpty()){
					if(Uteis.isAtributoPreenchido(getMatriculaAluno().getAluno().getNome())) {
						sacado.append(" (Aluno(a): ").append(getMatriculaAluno().getAluno().getNome()).append(" - ").append(getMatriculaAluno().getMatricula()).append(") ");
					}else {
						sacado.append(" (Aluno(a): ").append(getPessoa().getNome()).append(" - ").append(getMatriculaAluno().getMatricula()).append(") ");
					}
				}
				return sacado.toString();
			}
			case FORNECEDOR: {
				if (getFornecedor().getTipoEmpresa().equals("FI")) {
					return "FORNECEDOR - " + this.getFornecedor().getNome() + " - " + this.getFornecedor().getCPF();
				} else {
					return "FORNECEDOR - " + this.getFornecedor().getNome() + " - " + this.getFornecedor().getCNPJ();
				}

			}
			}
		} else {
                    if (getMatriculaAluno().getMatricula() != null && !getMatriculaAluno().getMatricula().equals("")) {
                            return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getMatriculaAluno().getAluno().getNome() + " - " + getMatriculaAluno().getMatricula();
                    }
                    if(Uteis.isAtributoPreenchido(getTipoPessoa())) {
                    	return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getPessoa().getNome();
                    }
                    return  getPessoa().getNome();
                }
		return getTipoPessoa();
            } catch (Exception e) {
                    if (getMatriculaAluno().getMatricula() != null && !getMatriculaAluno().getMatricula().equals("")) {
                            return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getMatriculaAluno().getAluno().getNome() + " - " + getMatriculaAluno().getMatricula();
                    }
                    return getPessoa().getNome();
            }
	}
	

	public String getNomePessoa() {
		TipoPessoa tipoPessoaLocal = TipoPessoa.getEnum(getTipoPessoa());
		if (tipoPessoaLocal != null) {
			switch (tipoPessoaLocal) {
			case ALUNO:
				return getPessoa().getNome();
			case RESPONSAVEL_FINANCEIRO:
				return getResponsavelFinanceiro().getNome();
			case CANDIDATO:
				return getCandidato().getNome();
			case FUNCIONARIO:
				return getFuncionario().getPessoa().getNome();
			case REQUERENTE:
				return getPessoa().getNome();
			case PARCEIRO:
				return getParceiroVO().getNome();
			case FORNECEDOR:
				return getFornecedor().getNome();
			}
		}
		return "";
	}
	
	
	public Integer getCodigoPessoaSacado() {
		TipoPessoa tipoPessoaLocal = TipoPessoa.getEnum(getTipoPessoa());
		if (tipoPessoaLocal != null) {
			switch (tipoPessoaLocal) {
			case ALUNO:
				return getPessoa().getCodigo();
			case RESPONSAVEL_FINANCEIRO:
				return getResponsavelFinanceiro().getCodigo();
			case CANDIDATO:
				return getCandidato().getCodigo();
			case FUNCIONARIO:
				return getFuncionario().getCodigo();
			case REQUERENTE:
				return getPessoa().getCodigo();
			case PARCEIRO:
				return getParceiroVO().getCodigo();
			case FORNECEDOR:
				return getFornecedor().getCodigo();
			}
		}
		return 0;
	}

	public Integer getOrigemNegociacaoReceber() {
		if (origemNegociacaoReceber == null) {
			origemNegociacaoReceber = 0;
		}
		return (origemNegociacaoReceber);
	}

	public void setOrigemNegociacaoReceber(Integer origemNegociacaoReceber) {
		this.origemNegociacaoReceber = origemNegociacaoReceber;
	}

	@XmlElement(name = "pacela")
	public String getParcela() {
		if (parcela == null) {
			parcela = "1/1";
		}
		return (parcela);
	}
	
	public String getParcelaPorConfiguracaoFinanceira_Apresentar() {
		if (Uteis.isAtributoPreenchido(getTipoOrigem()) 
				&& getTipoOrigem().equals(TipoOrigemContaReceber.MATRICULA.valor) 
				&& Uteis.isAtributoPreenchido(getConfiguracaoFinanceiro().getSiglaParcelaMatriculaApresentarAluno())) {
		    return getConfiguracaoFinanceiro().getSiglaParcelaMatriculaApresentarAluno();
		}
		return getParcela();
	}

	public String getNumeroParcela() {
		if (getParcela().contains("M")) {
			return getParcela();
		}
		if (getParcela().contains("E")) {
			return getParcela();
		}
		int pos = getParcela().indexOf('/');
		String numeroparcela = getParcela().substring(0, pos + 1);
		if (numeroparcela.equals("")) {
			return getParcela();
		}
		return (numeroparcela);
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}
	@XmlElement(name = "nrDocumento")
	public String getNrDocumento() {
		if (nrDocumento == null) {
			nrDocumento = "";
		}
		return (nrDocumento);
	}

	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public Double getMultaPorcentagem() {
		if (multaPorcentagem == null) {
			multaPorcentagem = 0.0;
		}
		return (multaPorcentagem);
	}

	public void setMultaPorcentagem(Double multaPorcentagem) {
		this.multaPorcentagem = multaPorcentagem;
	}

	@XmlElement(name = "multa")
	public Double getMulta() {
		if (multa == null) {
			multa = 0.0;
		}
		return (multa);
	}

	public void setMulta(Double multa) {
		this.multa = multa;
	}

	public Double getValorMultaCalculado() {
		valorMultaCalculado = Optional.ofNullable(valorMultaCalculado).orElse(0.0);
		return valorMultaCalculado;
	}

	public void setValorMultaCalculado(Double valorMultaCalculado) {
		this.valorMultaCalculado = valorMultaCalculado;
	}

	public String getCodigoBarra() {
		if (codigoBarra == null) {
			codigoBarra = "";
		}
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}

	public Double getJuroPorcentagem() {
		if (juroPorcentagem == null) {
			juroPorcentagem = 0.0;
		}
		return (juroPorcentagem);
	}

	public void setJuroPorcentagem(Double juroPorcentagem) {
		this.juroPorcentagem = juroPorcentagem;
	}

	@XmlElement(name = "juro")
	public Double getJuro() {
		if (juro == null) {
			juro = 0.0;
		}
		return (juro);
	}

	public void setJuro(Double juro) {
		this.juro = juro;
	}
	

	public Double getValorJuroCalculado() {
		valorJuroCalculado = Optional.ofNullable(valorJuroCalculado).orElse(0.0);
		return valorJuroCalculado;
	}

	public void setValorJuroCalculado(Double valorJuroCalculado) {
		this.valorJuroCalculado = valorJuroCalculado;
	}

	// public Double getValorFinal() {
	// return (valorFinal);
	// }
	//
	// public void setValorFinal(Double valorFinal) {
	// this.valorFinal = valorFinal;
	// }
	public Double getValorDesconto() {
		if (valorDesconto == null) {
			valorDesconto = 0.0;
		}
		return (valorDesconto);
	}

	public void setValorDesconto(Double valorDesconto) {
		this.valorDesconto = Uteis.arrendondarForcando2CadasDecimais(valorDesconto);
	}

	@XmlElement(name = "valor")
	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return (valor);
	}

	public Double getValor_Apresentar() {
		return (Uteis.arredondarDecimal(valor, 2));
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
	@XmlElement(name = "dataVencimento")
	@XmlJavaTypeAdapter(DateAdapterMobile.class)
	public Date getDataVencimento() {
		if (dataVencimento == null) {
			dataVencimento = new Date();
		}
		return (dataVencimento);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	@XmlElement(name = "dataVencimentoApresentar")
	public String getDataVencimento_Apresentar() {
		if (dataVencimento == null) {
			return "";
		}
		return (Uteis.getData(dataVencimento));
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataCredito() {
		if (dataCredito == null) {
			dataCredito = new Date();
		}
		return (dataCredito);
	}
	
	public String getDataCredito_Apresentar() {
		if (dataCredito == null) {
			return "";
		}
		return (Uteis.getData(dataCredito));
	}
	
	public void setDataCredito(Date dataCredito) {
		this.dataCredito = dataCredito;
	}
	@XmlElement(name = "descricaoPagamento")
	public String getDescricaoPagamento() {
		if (descricaoPagamento == null) {
			descricaoPagamento = "";
		}
		return (descricaoPagamento);
	}

	public void setDescricaoPagamento(String descricaoPagamento) {
		this.descricaoPagamento = descricaoPagamento;
	}
	@XmlElement(name = "situacao")
	public String getSituacao() {
		if (situacao == null) {
			situacao = "AR";
		}
		return (situacao);
	}
	@XmlElement(name = "situacaoApresentar")
	public String getSituacao_Apresentar() {
		return SituacaoContaReceber.getDescricao(situacao);
	}

	public boolean getSituacao_ApresentarIsentos() {
		if (getSituacao().equals("RE") || getSituacao().equals("RM") || getSituacao().equals("NE")) {
			return false;
		}
		return true;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}
	@XmlElement(name = "tipoOrigem")
	public String getTipoOrigem() {
		if (tipoOrigem == null) {
			tipoOrigem = "";
		}
		return (tipoOrigem);
	}
	@XmlElement(name = "tipoOrigemApresentar")
	public String getTipoOrigem_apresentar() {
		return TipoOrigemContaReceber.getDescricao(getTipoOrigem());
	}
	
	public String getTipoOrigemPorConfiguracaoFinanceira_Apresentar() {
		TipoOrigemContaReceber obj = TipoOrigemContaReceber.getEnum(getTipoOrigem());
		if (obj != null 
				&& obj.isMatricula() 
				&& Uteis.isAtributoPreenchido(getConfiguracaoFinanceiro().getNomeParcelaMatriculaApresentarAluno())) {
		    return getConfiguracaoFinanceiro().getNomeParcelaMatriculaApresentarAluno();
		}else  if (obj != null 
				&& obj.isMaterialDidatico() 
				&& Uteis.isAtributoPreenchido(getConfiguracaoFinanceiro().getNomeParcelaMaterialDidaticoApresentarAluno())) {
			return getConfiguracaoFinanceiro().getNomeParcelaMaterialDidaticoApresentarAluno();
		}else  if (obj != null) {
			return obj.getDescricao();
		}
		return getTipoOrigem();
	}
	
	public TipoOrigemContaReceber getTipoOrigemContaReceber() {		
		return TipoOrigemContaReceber.getEnum(getTipoOrigem());
	}

	public void setTipoOrigem(String tipoOrigem) {
		this.tipoOrigem = tipoOrigem;
	}

	public String getCodOrigem() {
		if (codOrigem == null) {
			codOrigem = "";
		}
		return (codOrigem);
	}
	@XmlElement(name = "codOrigem")
	public void setCodOrigem(String codOrigem) {
		this.codOrigem = codOrigem;
	}

	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return (data);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getData_Apresentar() {
		if (data == null) {
			return "";
		}
		return (Uteis.getData(data));
	}

	public void setData(Date data) {
		this.data = data;
	}
	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@XmlElement(name = "pessoa")
	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return pessoa;
	}

	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
	}

	@XmlElement(name = "contaCorrente")
	public Integer getContaCorrente() {
		if (contaCorrente == null) {
			contaCorrente = 0;
		}
		return contaCorrente;
	}

	public void setContaCorrente(Integer contaCorrente) {
		this.contaCorrente = contaCorrente;
	}
        
        

        /**
         * Método depreciado em função de somar os descontos utilizando o atributo valorDesconto.
         * Na verdade este atributo pode conter um percentual de desconto e não somente o valor calculado
         * do desconto. Logo, para se calcular o desconto total é necessário considerar o valor desconto
         * do aluno já calculado.
         * 21-01-2014 ver getValorTotalDescontoContaReceber
         * @deprecated 21-01-2014
         */
	public Double getValorTotalDesconto() {
            Double totalDesconto = this.getValorDesconto() + this.getValorDescontoConvenio() + this.getValorDescontoInstituicao() + this.getValorDescontoProgressivo();
            return Uteis.arrendondarForcando2CadasDecimais(totalDesconto);
	}
        
        /**
         * Este é o método oficial para calcular o valor total dos descontos de uma conta a receber.
         * O mesmo considera que o método espírita (ContaReceber.montarListaDescontosAplicaveisContaReceber)
         * já foi chamado para atualizar todos os atributos que armazenam os valores dos descontos de uma conta
         * a receber (desconto progressivo, desconto convenio, desconto instituicao, desconto do alnuo, desconto 
         * do recebimento). Quando a conta a receber já estiver recebida, o método também retorna a soma de todos
         * estes descontos, para título de informação e relatórios. 
         */
        public Double getValorTotalDescontoContaReceber() {
        	Double totalDesconto = this.getValorDescontoAlunoJaCalculado() + 
                    this.getValorDescontoConvenio() + 
                    this.getValorDescontoInstituicao() + 
                    this.getValorDescontoProgressivo() + 
                    this.getValorCalculadoDescontoLancadoRecebimento() + getValorDescontoRateio();
            return Uteis.arrendondarForcando2CadasDecimais(totalDesconto);
        }

	public Double getValorRecebidoTituloQuitado() {
		if (this.valor == null) {
			this.valor = 0.0;
		}
		if (this.valorDesconto == null) {
			this.valorDesconto = 0.0;
		}
		if (this.valorDescontoInstituicao == null) {
			this.valorDescontoInstituicao = 0.0;
		}
		if (this.valorDescontoConvenio == null) {
			this.valorDescontoConvenio = 0.0;
		}
		if (this.valorDescontoProgressivo == null) {
			this.valorDescontoProgressivo = 0.0;
		}
		Double valorFinal = this.valor - this.valorDesconto - this.valorDescontoInstituicao - this.valorDescontoConvenio - this.valorDescontoProgressivo - getValorDescontoRateio();
		return Uteis.arrendondarForcando2CadasDecimais(valorFinal);
	}

	/**
	 * Este método retorna o valor do título excluindo o valor do desconto
	 * progressivo. O mesmo é utilizado na matrícula do aluno para determinar
	 * quanto um aluno que está alterando seu plano financeiro, tem de fato em
	 * desconto.
	 * 
	 * @return
	 */
	public Double getValorRecebidoSemDescontoProgressivo() {
		Double valorFinal = this.valor - this.valorDesconto - this.valorDescontoInstituicao - this.valorDescontoConvenio - getValorDescontoRateio();
		if (valorFinal < 0.0) {
			valorFinal = 0.0;
		}
		return Uteis.arrendondarForcando2CadasDecimais(valorFinal);
	}

	public Double getValorRecebido() {
		if (valorRecebido == null) {
			valorRecebido = 0.0;
		}
		if (valorRecebido < 0.0) {
			valorRecebido = 0.0;
		}
		return valorRecebido;
	}

	public void setValorRecebido(Double valorRecebido) {
		this.valorRecebido = valorRecebido;
	}

	public void calculaJuroMulta() {
		setJuro(new Double(getValor().doubleValue() * getJuroPorcentagem().doubleValue() / 100));
		setMulta(new Double(getValor().doubleValue() * getMultaPorcentagem().doubleValue() / 100));
	}

	public String getTipoDesconto() {
		if (tipoDesconto == null) {
			tipoDesconto = "PO";
		}
		return tipoDesconto;
	}

	public String getTipoDesconto_Apresentar() {
		return TipoDescontoAluno.getDescricao(getTipoDesconto());
	}

	public void setTipoDesconto(String tipoDesconto) {
		this.tipoDesconto = tipoDesconto;
	}

	public String getDescricaoDescontoProgressivo() {
		if (descricaoDescontoProgressivo == null) {
			descricaoDescontoProgressivo = "";
		}
		return descricaoDescontoProgressivo;
	}

	public void setDescricaoDescontoProgressivo(String descricaoDescontoProgressivo) {
		this.descricaoDescontoProgressivo = descricaoDescontoProgressivo;
	}

	@XmlElement(name = "unidadeEnsino")
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	
	

	@XmlElement(name = "unidadeEnsinoFinanceira")
	public UnidadeEnsinoVO getUnidadeEnsinoFinanceira() {
		if (unidadeEnsinoFinanceira == null) {
			unidadeEnsinoFinanceira = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoFinanceira;
	}

	public void setUnidadeEnsinoFinanceira(UnidadeEnsinoVO unidadeEnsinoFinanceira) {
		this.unidadeEnsinoFinanceira = unidadeEnsinoFinanceira;
	}

	public List<ContaReceberRecebimentoVO> getContaReceberRecebimentoVOs() {
		if (contaReceberRecebimentoVOs == null) {
			contaReceberRecebimentoVOs = new ArrayList<ContaReceberRecebimentoVO>(0);
		}
		return contaReceberRecebimentoVOs;
	}

	public void setContaReceberRecebimentoVOs(List<ContaReceberRecebimentoVO> contaReceberRecebimentoVOs) {
		this.contaReceberRecebimentoVOs = contaReceberRecebimentoVOs;
	}

	public Double getValorNegociacao() {
		if (valorNegociacao == null) {
			valorNegociacao = 0.0;
		}
		return valorNegociacao;
	}

	public void setValorNegociacao(Double valorNegociacao) {
		this.valorNegociacao = valorNegociacao;
	}

	public Double getValorDescontoRecebido() {
		if (valorDescontoRecebido == null) {
			valorDescontoRecebido = 0.0;
		}
		return valorDescontoRecebido;
	}

	public void setValorDescontoRecebido(Double valorDescontoRecebido) {
		this.valorDescontoRecebido = valorDescontoRecebido;
	}

	public String getTipoBoleto() {
		if (tipoBoleto == null) {
			tipoBoleto = "";
		}
		return tipoBoleto;
	}

	public void setTipoBoleto(String tipoBoleto) {
		this.tipoBoleto = tipoBoleto;
	}

	public ConvenioVO getConvenio() {
		if (convenio == null) {
			convenio = new ConvenioVO();
		}
		return convenio;
	}

	public void setConvenio(ConvenioVO convenio) {
		this.convenio = convenio;
	}

	public Double getValorDescontoConvenio() {
		if (valorDescontoConvenio == null) {
			valorDescontoConvenio = 0.0;
		}
		return valorDescontoConvenio;
	}

	public void setValorDescontoConvenio(Double valorDescontoConvenio) {
		this.valorDescontoConvenio = valorDescontoConvenio;
	}

	public Double getValorDescontoInstituicao() {
		if (valorDescontoInstituicao == null) {
			valorDescontoInstituicao = 0.0;
		}
		return valorDescontoInstituicao;
	}

	public void setValorDescontoInstituicao(Double valorDescontoInstituicao) {
		this.valorDescontoInstituicao = valorDescontoInstituicao;
	}
	
	@XmlElement(name = "linhaDigitavel")
	public String getLinhaDigitavelCodigoBarras() {
		if (linhaDigitavelCodigoBarras == null) {
			linhaDigitavelCodigoBarras = "";
		}
		return linhaDigitavelCodigoBarras;
	}

	public void setLinhaDigitavelCodigoBarras(String linhaDigitavelCodigoBarras) {
		this.linhaDigitavelCodigoBarras = linhaDigitavelCodigoBarras;
	}

	/**
	 * @return the beneficiario
	 */
	public PessoaVO getBeneficiario() {
		if (beneficiario == null) {
			beneficiario = new PessoaVO();
		}
		return beneficiario;
	}

	/**
	 * @param beneficiario
	 *            the beneficiario to set
	 */
	public void setBeneficiario(PessoaVO beneficiario) {
		this.beneficiario = beneficiario;
	}

	@XmlElement(name = "nossoNumero")
	public String getNossoNumero() {
		if (nossoNumero == null) {
			nossoNumero = "";
		}
		return nossoNumero;
	}

	public void setNossoNumero(String nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public boolean isGerarNegociacaoRecebimento() {
		return gerarNegociacaoRecebimento;
	}

	public void setGerarNegociacaoRecebimento(boolean gerarNegociacaoRecebimento) {
		this.gerarNegociacaoRecebimento = gerarNegociacaoRecebimento;
	}

	public boolean getIsentar() {
		return isentar;
	}

	public void setIsentar(boolean isentar) {
		this.isentar = isentar;
	}

	public boolean getRemover() {
		return remover;
	}

	public void setRemover(boolean remover) {
		this.remover = remover;
	}

	public Boolean getSelecionado() {
		return selecionado;
	}

	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}

	@XmlElement(name = "parceiroVO")
	public ParceiroVO getParceiroVO() {
		if (parceiroVO == null) {
			parceiroVO = new ParceiroVO();
		}
		return parceiroVO;
	}

	public void setParceiroVO(ParceiroVO parceiroVO) {
		this.parceiroVO = parceiroVO;
	}

	public Boolean getSituacaoEQuitada() {
		return SituacaoContaReceber.RECEBIDO.getValor().equals(getSituacao()) || SituacaoContaReceber.NEGOCIADO.getValor().equals(getSituacao());
	}
        
        public Boolean getSituacaoAReceber() {
		if (this.getSituacao().equals("AR")) {
			return true;
		}
		return false;
	}
        
        public Boolean getSituacaoNegociado() {
    		if (this.getSituacao().equals("NE")) {
    			return true;
    		}
    		return false;
    	}

	public Boolean getContaReceberReferenteMatricula() {
		if (this.getTipoOrigem().equals("MAT")) {
			return true;
		}
		return false;
	}

	/**
	 * @return the matriculaPeriodo
	 */
	public Integer getMatriculaPeriodo() {
		if (matriculaPeriodo == null) {
			matriculaPeriodo = 0;
		}
		return matriculaPeriodo;
	}

	/**
	 * @param matriculaPeriodo
	 *            the matriculaPeriodo to set
	 */
	public void setMatriculaPeriodo(Integer matriculaPeriodo) {
		this.matriculaPeriodo = matriculaPeriodo;
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void atualizarSituacaoContaReceberDeAcordoComDescontos(ConfiguracaoFinanceiroVO conf, Date dataMatriculaPeriodo, UsuarioVO usuario) throws Exception {
        Double valorFinalContaReceber = this.getCalcularValorFinal(null, conf, false, dataMatriculaPeriodo, usuario);
		if (valorFinalContaReceber <= 0.0) {
			if (!getEstornandoRecebimento()) {
				this.setSituacao("RE");				
			}
            this.setValorRecebido(0.0);
		} else {
			if (this.getSituacaoEQuitada()) {
				this.getCalcularValorFinal(conf, usuario);
			} else {
				this.setValorRecebido(0.0);
				this.setValorDescontoRecebido(0.0);
			}
		}
	}

	/**
	 * @return the valorDescontoProgressivo
	 */
	public Double getValorDescontoProgressivo() {
		if (valorDescontoProgressivo == null) {
			valorDescontoProgressivo = 0.0;
		}
		return valorDescontoProgressivo;
	}

	/**
	 * @param valorDescontoProgressivo
	 *            the valorDescontoProgressivo to set
	 */
	public void setValorDescontoProgressivo(Double valorDescontoProgressivo) {
		this.valorDescontoProgressivo = Uteis.arrendondarForcando2CadasDecimais(valorDescontoProgressivo);
	}

	/**
	 * @return the tituloImportadoSistemaLegado
	 */
	public Boolean getTituloImportadoSistemaLegado() {
		if (tituloImportadoSistemaLegado == null) {
			tituloImportadoSistemaLegado = false;
		}
		return tituloImportadoSistemaLegado;
	}

	/**
	 * @param tituloImportadoSistemaLegado
	 *            the tituloImportadoSistemaLegado to set
	 */
	public void setTituloImportadoSistemaLegado(Boolean tituloImportadoSistemaLegado) {
		this.tituloImportadoSistemaLegado = tituloImportadoSistemaLegado;
	}

	public String obterNumeroParcela() {
		try {
			if (this.getTipoOrigem().equals("MAT")) {
				return "00";
			}
			String numero = null;
			if (this.getTipoOrigem().equals("MEN")) {
				if (this.getParcela().contains("EX")) {
					numero = "8" + getParcela().replaceAll("\\D", "");
					if (numero.length() == 1) {
						numero = "80";
					}
				} else if (this.getParcela().indexOf("/") < 0) {
					numero = getParcela().replaceAll("\\D", "");
				} else {
					numero = this.getParcela().substring(0, this.getParcela().indexOf("/"));
				}
				if (numero.length() == 1) {
					numero = "0" + numero; // forcar a geracao com dois digitos
											// (se tiver 1, vai retornar 01)
				}
				return numero;
			}
			return "";
		} catch (Exception e) {
			return "";
		}
	}

	@Override
	public String toString() {
		return "Conta a Receber [" + this.getCodigo() + "]: " + 
                        " Data Vcto: " + this.getDataVencimento_Apresentar() + 
                        " Situação: " + this.getSituacao_Apresentar() + 
                        " Valor: " + Uteis.getDoubleFormatado(this.getValor()) + 
                        " Desconto Aluno Já Calculado (R$): " + Uteis.getDoubleFormatado(this.getValorDescontoAlunoJaCalculado()) +
                        " Desconto Progressivo: " + Uteis.getDoubleFormatado(this.getValorDescontoProgressivo()) +
                        " Desconto Convênio: " + Uteis.getDoubleFormatado(this.getValorDescontoConvenio()) +
                        " Desconto Institucional: " + Uteis.getDoubleFormatado(this.getValorDescontoInstituicao()) +
                        " Desconto Recebimento: " + Uteis.getDoubleFormatado(this.getValorCalculadoDescontoLancadoRecebimento()) +
                        " Desconto Rateio: " + Uteis.getDoubleFormatado(this.getValorDescontoRateio()) +
                        " Total Descontos: " + Uteis.getDoubleFormatado(this.getValorTotalDescontoContaReceber()) +
                        " Juros Já Calculados (R$): " + Uteis.getDoubleFormatado(this.getJuro()) +
                        " Multa Já Calculados (R$): " + Uteis.getDoubleFormatado(this.getMulta()) +
                        " Acrecimo (R$): " + Uteis.getDoubleFormatado(this.getAcrescimo()) +
                        " Total Acrescimto: " + Uteis.getDoubleFormatado(this.getTotalAcrescimoContaReceber()) +
                        " Valor para Recebimento: " +  Uteis.getDoubleFormatado(this.getValorRecebido()) +
                        " Tipo Origem: " + this.getTipoOrigem() + 
                        " Cod.Origem: " + this.getCodOrigem() + 
                        " Nosso Número: " + this.getNossoNumero();
	}

	public void setRecebimentoBancario(Boolean recebimentoBancario) {
		this.recebimentoBancario = recebimentoBancario;
	}

	public Boolean getRecebimentoBancario() {
		if (recebimentoBancario == null) {
			recebimentoBancario = false;
		}
		return recebimentoBancario;
	}

	public String getRecebimentoBancarioTexto() {

		if (getRecebimentoBancario() == null) {
			return "Não Informado";
		} else if (getRecebimentoBancario()) {
			return "Sim";
		} else if (!getRecebimentoBancario()) {
			return "Não";
		} else {
			return "";
		}
	}

	/**
	 * @return the planoDescontoContaReceberVOs
	 */
	public boolean getApresentarBotaoDetalhamentoPlanoDescontoContaReceberVOs() {
		return getListaPlanoDescontoInstitucionalVOs().isEmpty();
	}
	
	public boolean getApresentarConvenioVOs() {
		return getListaConvenioVOs().isEmpty();
	}

	public boolean getApresentarContaReceberHistorico() {
		if (this.getContaReceberHistoricoVOs().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	public boolean getApresentarBotaoPix() {
		return Uteis.isAtributoPreenchido(getCodigo()) &&  getSituacaoAReceber() && getContaCorrenteVO().getHabilitarRegistroPix(); 
	}

	public List<PlanoDescontoContaReceberVO> getPlanoDescontoContaReceberVOs() {
		if (planoDescontoContaReceberVOs == null) {
			planoDescontoContaReceberVOs = new ArrayList<PlanoDescontoContaReceberVO>(0);
		}
		return planoDescontoContaReceberVOs;
	}

	/**
	 * @param planoDescontoContaReceberVOs
	 *            the planoDescontoContaReceberVOs to set
	 */
	public void setPlanoDescontoContaReceberVOs(List<PlanoDescontoContaReceberVO> planoDescontoContaReceberVOs) {
		this.planoDescontoContaReceberVOs = planoDescontoContaReceberVOs;
	}

	/**
	 * @return the configuracaoFinanceiro
	 */
	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiro() {
		if (configuracaoFinanceiro == null) {
			configuracaoFinanceiro = new ConfiguracaoFinanceiroVO();
		}
		return configuracaoFinanceiro;
	}

	/**
	 * @param configuracaoFinanceiro
	 *            the configuracaoFinanceiro to set
	 */
	public void setConfiguracaoFinanceiro(ConfiguracaoFinanceiroVO configuracaoFinanceiro) {
		this.configuracaoFinanceiro = configuracaoFinanceiro;
	}
        
        public void inicializarOrdemAplicacaoDescontoConformeConfiguracaoFinanceiro(ConfiguracaoFinanceiroVO conf) {
            if ((this.getOrdemDescontoProgressivo() == null) &&
                (this.getOrdemConvenio() == null) &&
                (this.getOrdemDescontoAluno() == null) &&
                (this.getOrdemPlanoDesconto() == null)) {
                // se as ordens estão todas com valor nulo temos que inicializar a mesma com os valores
                // padroes da configuracao
                this.setOrdemDescontoProgressivo(conf.getOrdemDescontoProgressivo());
                this.setOrdemConvenio(conf.getOrdemConvenio());
                this.setOrdemDescontoAluno(conf.getOrdemDescontoAluno());
                this.setOrdemPlanoDesconto(conf.getOrdemPlanoDesconto());
            }
            if ((this.getOrdemDescontoProgressivo().intValue() == 0) &&
                (this.getOrdemConvenio().intValue() == 0) &&
                (this.getOrdemDescontoAluno().intValue() == 0) &&
                (this.getOrdemPlanoDesconto().intValue() == 0)) {
                // se as ordens estão todas com valor 0 temos que inicializar a mesma com os valores
                // padroes da configuracao
                this.setOrdemDescontoProgressivo(conf.getOrdemDescontoProgressivo());
                this.setOrdemConvenio(conf.getOrdemConvenio());
                this.setOrdemDescontoAluno(conf.getOrdemDescontoAluno());
                this.setOrdemPlanoDesconto(conf.getOrdemPlanoDesconto());
            }
        }

	public List<OrdemDescontoVO> obterOrdemAplicacaoDescontosPadraoAtual() {
		if ((this.getOrdemDescontoAluno() == null) || (this.getOrdemDescontoAluno() >= 4)) {
			this.setOrdemDescontoAluno(0);
		}
		if ((this.getOrdemPlanoDesconto() == null) || (this.getOrdemPlanoDesconto() >= 4)) {
			this.setOrdemPlanoDesconto(1);
		}
		if ((this.getOrdemConvenio() == null) || (this.getOrdemConvenio() >= 4)) {
			this.setOrdemConvenio(2);
		}
		if ((this.getOrdemDescontoProgressivo() == null) || (this.getOrdemDescontoProgressivo() >= 4)) {
			this.setOrdemConvenio(3);
		}
		List<OrdemDescontoVO> obj = new ArrayList<OrdemDescontoVO>(0);
		obj.add(this.obterOrdemDescontoVODescontoAluno());
		obj.add(this.obterOrdemDescontoVOPlanoDesconto());
		obj.add(this.obterOrdemDescontoVOConvenio());
		obj.add(this.obterOrdemDescontoProgressivoVO());
		Ordenacao.ordenarLista(obj, "posicaoAtual");
		return obj;
	}

	public OrdemDescontoVO obterOrdemDescontoProgressivoVO() {
		OrdemDescontoVO ordem = new OrdemDescontoVO();
		ordem.setDescricaoDesconto("Desc.Progressivo");
		ordem.setValorCheio(this.getOrdemDescontoProgressivoValorCheio());
		ordem.setPosicaoAtual(this.getOrdemDescontoProgressivo());
		return ordem;
	}

	public OrdemDescontoVO obterOrdemDescontoVOConvenio() {
		OrdemDescontoVO ordem = new OrdemDescontoVO();
		ordem.setDescricaoDesconto("Convênio");
		ordem.setValorCheio(this.getOrdemConvenioValorCheio());
		ordem.setPosicaoAtual(this.getOrdemConvenio());
		return ordem;
	}

	public OrdemDescontoVO obterOrdemDescontoVODescontoAluno() {
		OrdemDescontoVO ordem = new OrdemDescontoVO();
		ordem.setDescricaoDesconto("Desconto Aluno");
		ordem.setValorCheio(this.getOrdemDescontoAlunoValorCheio());
		ordem.setPosicaoAtual(this.getOrdemDescontoAluno());
		return ordem;
	}

	public OrdemDescontoVO obterOrdemDescontoVOPlanoDesconto() {
		OrdemDescontoVO ordem = new OrdemDescontoVO();
		ordem.setDescricaoDesconto("Plano Desconto");
		ordem.setValorCheio(this.getOrdemPlanoDescontoValorCheio());
		ordem.setPosicaoAtual(this.getOrdemPlanoDesconto());
		return ordem;
	}

	/**
	 * @return the ordemDescontoAluno
	 */
	public Integer getOrdemDescontoAluno() {
		if (ordemDescontoAluno == null) {
			ordemDescontoAluno = 0;
		}
		return ordemDescontoAluno;
	}

	/**
	 * @param ordemDescontoAluno
	 *            the ordemDescontoAluno to set
	 */
	public void setOrdemDescontoAluno(Integer ordemDescontoAluno) {
		this.ordemDescontoAluno = ordemDescontoAluno;
	}

	/**
	 * @return the ordemDescontoAlunoValorCheio
	 */
	public Boolean getOrdemDescontoAlunoValorCheio() {
		if (ordemDescontoAlunoValorCheio == null) {
			ordemDescontoAlunoValorCheio = false;
		}
		return ordemDescontoAlunoValorCheio;
	}

	/**
	 * @param ordemDescontoAlunoValorCheio
	 *            the ordemDescontoAlunoValorCheio to set
	 */
	public void setOrdemDescontoAlunoValorCheio(Boolean ordemDescontoAlunoValorCheio) {
		this.ordemDescontoAlunoValorCheio = ordemDescontoAlunoValorCheio;
	}

	/**
	 * @return the ordemPlanoDesconto
	 */
	public Integer getOrdemPlanoDesconto() {
		if (ordemPlanoDesconto == null) {
			ordemPlanoDesconto = 0;
		}
		return ordemPlanoDesconto;
	}

	/**
	 * @param ordemPlanoDesconto
	 *            the ordemPlanoDesconto to set
	 */
	public void setOrdemPlanoDesconto(Integer ordemPlanoDesconto) {
		this.ordemPlanoDesconto = ordemPlanoDesconto;
	}

	/**
	 * @return the ordemPlanoDescontoValorCheio
	 */
	public Boolean getOrdemPlanoDescontoValorCheio() {
		if (ordemPlanoDescontoValorCheio == null) {
			ordemPlanoDescontoValorCheio = false;
		}
		return ordemPlanoDescontoValorCheio;
	}

	/**
	 * @param ordemPlanoDescontoValorCheio
	 *            the ordemPlanoDescontoValorCheio to set
	 */
	public void setOrdemPlanoDescontoValorCheio(Boolean ordemPlanoDescontoValorCheio) {
		this.ordemPlanoDescontoValorCheio = ordemPlanoDescontoValorCheio;
	}

	/**
	 * @return the ordemConvenio
	 */
	public Integer getOrdemConvenio() {
		if (ordemConvenio == null) {
			ordemConvenio = 0;
		}
		return ordemConvenio;
	}

	/**
	 * @param ordemConvenio
	 *            the ordemConvenio to set
	 */
	public void setOrdemConvenio(Integer ordemConvenio) {
		this.ordemConvenio = ordemConvenio;
	}

	/**
	 * @return the ordemConvenioValorCheio
	 */
	public Boolean getOrdemConvenioValorCheio() {
		if (ordemConvenioValorCheio == null) {
			ordemConvenioValorCheio = false;
		}
		return ordemConvenioValorCheio;
	}

	/**
	 * @param ordemConvenioValorCheio
	 *            the ordemConvenioValorCheio to set
	 */
	public void setOrdemConvenioValorCheio(Boolean ordemConvenioValorCheio) {
		this.ordemConvenioValorCheio = ordemConvenioValorCheio;
	}

	/**
	 * @return the ordemDescontoProgressivo
	 */
	public Integer getOrdemDescontoProgressivo() {
		if (ordemDescontoProgressivo == null) {
			ordemDescontoProgressivo = 0;
		}
		return ordemDescontoProgressivo;
	}

	/**
	 * @param ordemDescontoProgressivo
	 *            the ordemDescontoProgressivo to set
	 */
	public void setOrdemDescontoProgressivo(Integer ordemDescontoProgressivo) {
		this.ordemDescontoProgressivo = ordemDescontoProgressivo;
	}

	/**
	 * @return the ordemDescontoProgressivoValorCheio
	 */
	public Boolean getOrdemDescontoProgressivoValorCheio() {
		if (ordemDescontoProgressivoValorCheio == null) {
			ordemDescontoProgressivoValorCheio = Boolean.FALSE;
		}
		return ordemDescontoProgressivoValorCheio;
	}

	/**
	 * @param ordemDescontoProgressivoValorCheio
	 *            the ordemDescontoProgressivoValorCheio to set
	 */
	public void setOrdemDescontoProgressivoValorCheio(Boolean ordemDescontoProgressivoValorCheio) {
		this.ordemDescontoProgressivoValorCheio = ordemDescontoProgressivoValorCheio;
	}

	public List<PlanoDescontoVO> getListaPlanoDescontoInstitucionalVOs() {
		List<PlanoDescontoVO> listaRetornar = new ArrayList<PlanoDescontoVO>(0);
		for (PlanoDescontoContaReceberVO plano : getPlanoDescontoContaReceberVOs()) {
			if ((plano.getIsPlanoDescontoInstitucional()) ||
			    (plano.getIsPlanoDescontoManual())) {
				listaRetornar.add(plano.getPlanoDescontoVO());
			}
		}
		Ordenacao.ordenarLista(listaRetornar, "ordenacao");
		return listaRetornar;
	}

	public List<ConvenioVO> getListaConvenioVOs() {
		List<ConvenioVO> listaRetornar = new ArrayList<ConvenioVO>(0);
		for (PlanoDescontoContaReceberVO plano : planoDescontoContaReceberVOs) {
			if (plano.getIsConvenio()) {
				listaRetornar.add(plano.getConvenio());
			}
		}
		Ordenacao.ordenarLista(listaRetornar, "ordenacao");
		return listaRetornar;
	}

	/**
	 * @return the listaDescontosAplicavesContaReceber
	 */
	public List<PlanoFinanceiroAlunoDescricaoDescontosVO> getListaDescontosAplicavesContaReceber() {
		if (listaDescontosAplicavesContaReceber == null) {
			listaDescontosAplicavesContaReceber = new ArrayList<PlanoFinanceiroAlunoDescricaoDescontosVO>(0);
		}
		return listaDescontosAplicavesContaReceber;
	}	

	/**
	 * @param listaDescontosAplicavesContaReceber
	 *            the listaDescontosAplicavesContaReceber to set
	 */
	public void setListaDescontosAplicavesContaReceber(List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosAplicavesContaReceber) {
		this.listaDescontosAplicavesContaReceber = listaDescontosAplicavesContaReceber;
	}	
	

	public Boolean getIsContaReceberReferenteMatricula() {
		if (this.getTipoOrigem().equals("MAT") || this.getTipoOrigem().equals("Matrícula")) {
			return true;
		}
		return false;
	}

	public Boolean getIsContaReceberReferenteMensalidade() {
		if (this.getTipoOrigem().equals("MEN")) {
			return true;
		}
		return false;
	}
	
	public boolean isContaReceberReferenteMaterialDidatico() {
		return Uteis.isAtributoPreenchido(getTipoOrigem()) && getTipoOrigem().equals("MDI");
	}

	public String getListaDescontosAplicavesContaReceberFormatoString() {
		StringBuilder str = new StringBuilder();
		String quebrarLinha = "\n";
		for (PlanoFinanceiroAlunoDescricaoDescontosVO plano : getListaDescontosAplicavesContaReceber()) {
			plano.setAcrescimo(getAcrescimo());
			str.append(plano.getDescricaoDetalhadaComDataLimitesEDescontosValidos()).append(quebrarLinha);
		}
		return str.toString();
	}

	public String getDescontosParaDataAtualFormatoString() {
		String desconto = "0,00";
		if (getSituacao().equals("AR")) {
			desconto = Uteis.getDoubleFormatado(getValorDescontoCalculado());
//			for (PlanoFinanceiroAlunoDescricaoDescontosVO plano : getListaDescontosAplicavesContaReceber()) {
//				desconto = Uteis.getDoubleFormatado(plano.executarCalculoValorTotalDesconto());
//				if (new Date().before(plano.getDataLimiteAplicacaoDesconto()) || new Date().equals(plano.getDataLimiteAplicacaoDesconto())) {
//					break;
//				}
//			}
		} else if (getSituacao().equals("RE")) {
			return Uteis.getDoubleFormatado(getValor() - getValorRecebido());
		}
		return desconto;
	}
	
	@XmlElement(name = "valorAPagar")
	public String getValorTotalAPagarNaDataAtualFormatoString() {
		String valorTotalAPagar = "0,00";
		if(getValorTotalAPagarNaDataAtual() > 0.0){
			valorTotalAPagar = Uteis.getDoubleFormatado(getValorTotalAPagarNaDataAtual());
		}
		return valorTotalAPagar;
	}
	
	
	@XmlElement(name = "valorTotalAPagarNaDataAtual")
	public Double getValorTotalAPagarNaDataAtual() {
		if(Uteis.isAtributoPreenchido(getProcessamentoIntegracaoFinanceiraDetalheVO())) {
			return getValor()+getJuro()+getMulta()+getAcrescimo()+getValorIndiceReajustePorAtraso().doubleValue()-getValorDescontoAlunoJaCalculado();
		}
		if (getSituacao().equals("AR")) {
//			valorTotalAPagar = Uteis.getDoubleFormatado(getValor() - getValorDescontoCalculado());
			return getValorReceberCalculado();
		} else if (getSituacao().equals("RE")) {
			return  getValorRecebido();
		}
//		for (PlanoFinanceiroAlunoDescricaoDescontosVO plano : getListaDescontosAplicavesContaReceber()) {
//		valorTotalAPagar = Uteis.getDoubleFormatado(plano.getValorParaPagamentoDentroDataLimiteDesconto(getTipoOrigem().equals("MAT")));
//		if (new Date().before(plano.getDataLimiteAplicacaoDesconto()) || new Date().equals(plano.getDataLimiteAplicacaoDesconto())) {
//			break;
//		}
		return 0.0;
//	}
	
	}

	public void inicializarDescontosConsiderandoDataEspecificaParaQuitacao(Date dataReferenciaQuitacao) throws Exception {
		PlanoFinanceiroAlunoDescricaoDescontosVO planoAplicavel = null;		
		for (PlanoFinanceiroAlunoDescricaoDescontosVO plano : getListaDescontosAplicavesContaReceber()) {
			if (plano.getIsAplicavelDataParaQuitacao(this.getDataVencimento(), dataReferenciaQuitacao)) {
				planoAplicavel = plano;
				break;
			}
		}
		if (planoAplicavel == null) {
			this.setValorDescontoAlunoJaCalculado(0.0);
			this.setValorDescontoInstituicao(0.0);
			this.setValorDescontoProgressivo(0.0);
			for (PlanoDescontoContaReceberVO planoContaReceber : this.getPlanoDescontoContaReceberVOs()) {
				 if ((planoContaReceber.getIsPlanoDescontoInstitucional() 
						 && !planoContaReceber.getPlanoDescontoVO().getUtilizarDescontoSemLimiteValidade() 
						 && (planoContaReceber.getPlanoDescontoVO().getDescontoValidoAteDataVencimento() 
								 || planoContaReceber.getPlanoDescontoVO().getDiasValidadeVencimento() > 0))) {
					 planoContaReceber.setValorUtilizadoRecebimento(0.0);
				 }
			}
			//Como Existem Desconto que nunca se perde entao considero o melhor caso.
			if(!getListaDescontosAplicavesContaReceber().isEmpty()){
				planoAplicavel =  getListaDescontosAplicavesContaReceber().get(0);
				this.setValorDescontoConvenio(planoAplicavel.getValorDescontoConvenio());
				this.setValorCusteadoContaReceber(planoAplicavel.getValorCusteadoContaReceber());
//				this.setValorDescontoInstituicao(planoAplicavel.getValorDescontoInstituicao());
				for (PlanoDescontoContaReceberVO planoContaReceber : this.getPlanoDescontoContaReceberVOs()) {
					 if(planoContaReceber.getIsConvenio() ){
						 planoContaReceber.setValorUtilizadoRecebimento(planoAplicavel.getListaDescontosConvenio().get(planoContaReceber.getConvenio().getCodigo()));
					 }
				}
			}
			// Se continua nulo nao ha o que fazer, podemos sair sem nenhuma inicializacao...
			return;
		}
                
         // Abaixo iremos inicializar os objetos da tabela planoDescontoContaReceber com base no plano
         // aplicavel selecionado. Somente nesta tabela é possível registrar o valor específico que 
         // planoDesconvoVO e ConvenioVO gerou de abatimento na parcela.
         for (PlanoDescontoContaReceberVO planoContaReceber : this.getPlanoDescontoContaReceberVOs()) {
        	 if (planoContaReceber.getIsConvenio()) {        		 
        		 planoContaReceber.setValorUtilizadoRecebimento(planoAplicavel.getListaDescontosConvenio().get(planoContaReceber.getConvenio().getCodigo()));
        	 }
        	 if (planoContaReceber.getIsPlanoDescontoInstitucional()) {
                 planoContaReceber.setValorUtilizadoRecebimento(planoAplicavel.getListaDescontosPlanoDesconto().get(planoContaReceber.getPlanoDescontoVO().getCodigo()));
             }
             if (planoContaReceber.getIsPlanoDescontoManual()) {
                 planoContaReceber.setValorUtilizadoRecebimento(planoAplicavel.getListaDescontosPlanoDesconto().get(planoContaReceber.getPlanoDescontoVO().getCodigo()));
             }
         }      
		// Alterado por Carlos 08-04/2011
		// Se a parcela for do tipo (EX) ou seja Extra, não poderá nunca ser
		// aplicado o desconto para ela.
		if (!getParcela().contains("EX")) {
			this.setValorDescontoAlunoJaCalculado(planoAplicavel.getValorDescontoAluno());
			this.setValorDescontoConvenio(planoAplicavel.getValorDescontoConvenio());
			this.setValorCusteadoContaReceber(planoAplicavel.getValorCusteadoContaReceber());
			this.setValorDescontoInstituicao(planoAplicavel.getValorDescontoInstituicao());
			this.setValorDescontoProgressivo(planoAplicavel.getValorDescontoProgressivo());
		} else {
			if (planoAplicavel.getDescontoCemPorCento()) {
				this.setSituacao("RE");
                this.setValorCusteadoContaReceber(planoAplicavel.getValorCusteadoContaReceber());
				this.setValorDescontoAlunoJaCalculado(planoAplicavel.getValorDescontoAluno());
				this.setValorDescontoConvenio(planoAplicavel.getValorDescontoConvenio());
				this.setValorDescontoInstituicao(planoAplicavel.getValorDescontoInstituicao());
				this.setValorDescontoProgressivo(planoAplicavel.getValorDescontoProgressivo());
				//this.setValorDesconto(planoAplicavel.getValorDescontoAluno() + planoAplicavel.getValorDescontoConvenio() + planoAplicavel.getValorDescontoInstituicao() + planoAplicavel.getValorDescontoProgressivo());
			} else {
				this.setValorDescontoAlunoJaCalculado(0.0);
				this.setValorDescontoConvenio(0.0);
				this.setValorDescontoInstituicao(0.0);
				this.setValorDescontoProgressivo(0.0);
			}
		}

	}

	public Double getValorFinalCalculadoSemJurosEMulta() {
		Double valorFinalCalculadoSemJurosEMulta = this.getValor() - this.getValorDescontoConvenio() - this.getValorDescontoInstituicao() - this.getValorDescontoProgressivo() - this.getValorDescontoAlunoJaCalculado() - this.getValorDescontoRateio();
		if ((getValorDescontoRecebido() == null || getValorDescontoRecebido().equals(0.0)) && getSituacao().equals("RE")) {
			setValorDescontoRecebido(Uteis.arredondar((this.getValorDescontoConvenio() + this.getValorDescontoInstituicao() + this.getValorDescontoProgressivo() + this.getValorDescontoAlunoJaCalculado() + this.getValorCalculadoDescontoLancadoRecebimento() + this.getValorDescontoRateio()), 2, 0));
		}
		if (valorFinalCalculadoSemJurosEMulta >= this.getValor()) {
			setPrecisaCalcularDesconto(true);
		}

		return Uteis.arrendondarForcando2CadasDecimais(valorFinalCalculadoSemJurosEMulta);
	}

	/**
	 * @return the valorDescontoAlunoJaCalculado
	 */
	public Double getValorDescontoAlunoJaCalculado() {
		if (valorDescontoAlunoJaCalculado == null) {
			valorDescontoAlunoJaCalculado = 0.0;
		}
		return valorDescontoAlunoJaCalculado;
	}

	/**
	 * @param valorDescontoAlunoJaCalculado
	 *            the valorDescontoAlunoJaCalculado to set
	 */
	public void setValorDescontoAlunoJaCalculado(Double valorDescontoAlunoJaCalculado) {
		this.valorDescontoAlunoJaCalculado = valorDescontoAlunoJaCalculado;
	}

	public Boolean getIsTipoDescontoPorcentagem() {
		if (this.getTipoDesconto().equals("PO")) {
			return true;
		}
		return false;
	}

	public String getValorRecebidoDeAcordoComSituacao() {
		if (getSituacaoEQuitada()) {
			return Uteis.getDoubleFormatado(getValorRecebido());
		}
		return "0,00";
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	public void setJustificativaDesconto(String justificativaDesconto) {
		this.justificativaDesconto = justificativaDesconto;
	}

	public String getJustificativaDesconto() {
		if (justificativaDesconto == null) {
			justificativaDesconto = "";
		}
		return justificativaDesconto;
	}

	public void setPrecisaCalcularDesconto(Boolean precisaCalcularDesconto) {
		this.precisaCalcularDesconto = precisaCalcularDesconto;
	}

	public Boolean getPrecisaCalcularDesconto() {
		if (precisaCalcularDesconto == null) {
			precisaCalcularDesconto = false;
		}
		return precisaCalcularDesconto;
	}
	
	public boolean isExisteValorDescontoLancadoRecebimento() {
		return getValorDescontoLancadoRecebimento() > 0.0;
	}
	

	public Double getValorDescontoLancadoRecebimento() {
		if (valorDescontoLancadoRecebimento == null) {
			valorDescontoLancadoRecebimento = 0.0;
		}
		return valorDescontoLancadoRecebimento;
	}

	public void setValorDescontoLancadoRecebimento(Double valorDescontoLancadoRecebimento) {
		this.valorDescontoLancadoRecebimento = valorDescontoLancadoRecebimento;
	}

	public String getTipoDescontoLancadoRecebimento() {
		if (tipoDescontoLancadoRecebimento == null) {
			tipoDescontoLancadoRecebimento = "PO";
		}
		return tipoDescontoLancadoRecebimento;
	}

	public void setTipoDescontoLancadoRecebimento(String tipoDescontoLancadoRecebimento) {
		this.tipoDescontoLancadoRecebimento = tipoDescontoLancadoRecebimento;
	}

	public Double getValorCalculadoDescontoLancadoRecebimento() {
		if (valorCalculadoDescontoLancadoRecebimento == null) {
			valorCalculadoDescontoLancadoRecebimento = 0.0;
		}
		return valorCalculadoDescontoLancadoRecebimento;
	}

	public void setValorCalculadoDescontoLancadoRecebimento(Double valorCalculadoDescontoLancadoRecebimento) {
		this.valorCalculadoDescontoLancadoRecebimento = valorCalculadoDescontoLancadoRecebimento;
	}

	public Double getValorAnteriorDescontoLancadoControle() {
		if (valorAnteriorDescontoLancadoControle == null) {
			valorAnteriorDescontoLancadoControle = 0.0;
		}
		return valorAnteriorDescontoLancadoControle;
	}

	public void setValorAnteriorDescontoLancadoControle(Double valorAnteriorDescontoLancadoControle) {
		this.valorAnteriorDescontoLancadoControle = valorAnteriorDescontoLancadoControle;
	}

	public String getControleErroDescontoRecebimento() {
		if (controleErroDescontoRecebimento == null) {
			controleErroDescontoRecebimento = "";
		}
		return controleErroDescontoRecebimento;
	}

	public void setControleErroDescontoRecebimento(String controleErroDescontoRecebimento) {
		this.controleErroDescontoRecebimento = controleErroDescontoRecebimento;
	}

	/**
	 * @return the impressaoBoletoRealizada
	 */
	public Boolean getImpressaoBoletoRealizada() {
		if (impressaoBoletoRealizada == null) {
			impressaoBoletoRealizada = Boolean.FALSE;
		}
		return impressaoBoletoRealizada;
	}

	/**
	 * @param impressaoBoletoRealizada
	 *            the impressaoBoletoRealizada to set
	 */
	public void setImpressaoBoletoRealizada(Boolean impressaoBoletoRealizada) {
		this.impressaoBoletoRealizada = impressaoBoletoRealizada;
	}

	public FaixaDescontoProgressivo getDescontoProgressivoUtilizado() {
		return descontoProgressivoUtilizado;
	}

	public void setDescontoProgressivoUtilizado(FaixaDescontoProgressivo descontoProgressivoUtilizado) {
		this.descontoProgressivoUtilizado = descontoProgressivoUtilizado;
	}

	public Double getValorDescontoCalculadoPrimeiraFaixaDescontos() {
		if (valorDescontoCalculadoPrimeiraFaixaDescontos == null) {
			valorDescontoCalculadoPrimeiraFaixaDescontos = 0.0;
		}
		if (valorDescontoCalculadoPrimeiraFaixaDescontos < 0) {
			valorDescontoCalculadoPrimeiraFaixaDescontos = 0.0;
		}
		return valorDescontoCalculadoPrimeiraFaixaDescontos;
	}

	public void setValorDescontoCalculadoPrimeiraFaixaDescontos(Double valorDescontoCalculadoPrimeiraFaixaDescontos) {
		this.valorDescontoCalculadoPrimeiraFaixaDescontos = valorDescontoCalculadoPrimeiraFaixaDescontos;
	}

	public Double getValorDescontoCalculadoSegundaFaixaDescontos() {
		if (valorDescontoCalculadoSegundaFaixaDescontos == null) {
			valorDescontoCalculadoSegundaFaixaDescontos = 0.0;
		}
		if (valorDescontoCalculadoSegundaFaixaDescontos < 0) {
			valorDescontoCalculadoSegundaFaixaDescontos = 0.0;
		}
		return valorDescontoCalculadoSegundaFaixaDescontos;
	}

	public void setValorDescontoCalculadoSegundaFaixaDescontos(Double valorDescontoCalculadoSegundaFaixaDescontos) {
		this.valorDescontoCalculadoSegundaFaixaDescontos = valorDescontoCalculadoSegundaFaixaDescontos;
	}

	public Double getValorDescontoCalculadoTerceiraFaixaDescontos() {
		if (valorDescontoCalculadoTerceiraFaixaDescontos == null) {
			valorDescontoCalculadoTerceiraFaixaDescontos = 0.0;
		}
		if (valorDescontoCalculadoTerceiraFaixaDescontos < 0) {
			valorDescontoCalculadoTerceiraFaixaDescontos = 0.0;
		}
		return valorDescontoCalculadoTerceiraFaixaDescontos;
	}

	public void setValorDescontoCalculadoTerceiraFaixaDescontos(Double valorDescontoCalculadoTerceiraFaixaDescontos) {
		this.valorDescontoCalculadoTerceiraFaixaDescontos = valorDescontoCalculadoTerceiraFaixaDescontos;
	}

	public Double getValorDescontoCalculadoQuartaFaixaDescontos() {
		if (valorDescontoCalculadoQuartaFaixaDescontos == null) {
			valorDescontoCalculadoQuartaFaixaDescontos = 0.0;
		}
		if (valorDescontoCalculadoQuartaFaixaDescontos < 0) {
			valorDescontoCalculadoQuartaFaixaDescontos = 0.0;
		}
		return valorDescontoCalculadoQuartaFaixaDescontos;
	}

	public void setValorDescontoCalculadoQuartaFaixaDescontos(Double valorDescontoCalculadoQuartaFaixaDescontos) {
		this.valorDescontoCalculadoQuartaFaixaDescontos = valorDescontoCalculadoQuartaFaixaDescontos;
	}

	public void setDataArquivoRemessa(Date dataArquivoRemessa) {
		this.dataArquivoRemessa = dataArquivoRemessa;
	}

	public Date getDataArquivoRemessa() {
		return dataArquivoRemessa;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final ContaReceberVO other = (ContaReceberVO) obj;
		if (this.codigo != other.codigo && (this.codigo == null || !this.codigo.equals(other.codigo))) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 89 * hash + (this.codigo != null ? this.codigo.hashCode() : 0);
		return hash;
	}
	
	public boolean isExisteAcrescimo(){
		return getAcrescimo() > 0.0;
	}
	

	/**
	 * @return the acrescimo
	 */
	@XmlElement(name = "acrescimo")
	public Double getAcrescimo() {
		if (acrescimo == null) {
			acrescimo = 0.0;
		}
		return acrescimo;
	}

	/**
	 * @param acrescimo
	 *            the acrescimo to set
	 */
	public void setAcrescimo(Double acrescimo) {
		this.acrescimo = acrescimo;
	}

	public Boolean getUsaDescontoCompostoPlanoDesconto() {
		if (usaDescontoCompostoPlanoDesconto == null) {
			usaDescontoCompostoPlanoDesconto = Boolean.FALSE;
		}
		return usaDescontoCompostoPlanoDesconto;
	}

	public void setUsaDescontoCompostoPlanoDesconto(Boolean usaDescontoCompostoPlanoDesconto) {
		this.usaDescontoCompostoPlanoDesconto = usaDescontoCompostoPlanoDesconto;
	}

	public RegistroArquivoVO getRegistroArquivoVO() {
		if (registroArquivoVO == null) {
			registroArquivoVO = new RegistroArquivoVO();
		}
		return registroArquivoVO;
	}

	public void setRegistroArquivoVO(RegistroArquivoVO registroArquivoVO) {
		this.registroArquivoVO = registroArquivoVO;
	}

	public Double getValorCodigoBarraDescSemValidade() {
		if (valorCodigoBarraDescSemValidade == null) {
			valorCodigoBarraDescSemValidade = 0.0;
		}
		return valorCodigoBarraDescSemValidade;
	}

	public void setValorCodigoBarraDescSemValidade(Double valorCodigoBarraDescSemValidade) {
		this.valorCodigoBarraDescSemValidade = valorCodigoBarraDescSemValidade;
	}

	public Boolean getGerarBoletoComDescontoSemValidade() {
		if (gerarBoletoComDescontoSemValidade == null) {
			gerarBoletoComDescontoSemValidade = Boolean.FALSE;
		}
		return gerarBoletoComDescontoSemValidade;
	}

	public void setGerarBoletoComDescontoSemValidade(Boolean gerarBoletoComDescontoSemValidade) {
		this.gerarBoletoComDescontoSemValidade = gerarBoletoComDescontoSemValidade;
	}

	public NegociacaoContaReceberVO getRenegociacaoContaReceberOrigem() {
		if (renegociacaoContaReceberOrigem == null) {
			renegociacaoContaReceberOrigem = new NegociacaoContaReceberVO();
		}
		return renegociacaoContaReceberOrigem;
	}

	public void setRenegociacaoContaReceberOrigem(NegociacaoContaReceberVO renegociacaoContaReceberOrigem) {
		this.renegociacaoContaReceberOrigem = renegociacaoContaReceberOrigem;
	}

	public Boolean getRealizandoRecebimento() {
		if (realizandoRecebimento == null) {
			realizandoRecebimento = Boolean.FALSE;
		}
		return realizandoRecebimento;
	}

	public void setRealizandoRecebimento(Boolean realizandoRecebimento) {
		this.realizandoRecebimento = realizandoRecebimento;
	}

	@XmlElement(name = "contaCorrenteVO")
	public ContaCorrenteVO getContaCorrenteVO() {
		if (contaCorrenteVO == null) {
			contaCorrenteVO = new ContaCorrenteVO();
		}
		return contaCorrenteVO;
	}

	public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
		this.contaCorrenteVO = contaCorrenteVO;
	}

	/**
	 * @return the nossoNumeroBanco
	 */
	public String getNossoNumeroBanco() {
		if (nossoNumeroBanco == null) {
			nossoNumeroBanco = "";
		}
		return nossoNumeroBanco;
	}

	/**
	 * @param nossoNumeroBanco
	 *            the nossoNumeroBanco to set
	 */
	public void setNossoNumeroBanco(String nossoNumeroBanco) {
		this.nossoNumeroBanco = nossoNumeroBanco;
	}

	public Double getValorDescontoInclusaoExclusao() {
		if (valorDescontoInclusaoExclusao == null) {
			valorDescontoInclusaoExclusao = 0.0;
		}
		return valorDescontoInclusaoExclusao;
	}

	public void setValorDescontoInclusaoExclusao(Double valorDescontoInclusaoExclusao) {
		this.valorDescontoInclusaoExclusao = valorDescontoInclusaoExclusao;
	}

	@XmlElement(name = "responsavelFinanceiro")
	public PessoaVO getResponsavelFinanceiro() {
		if (responsavelFinanceiro == null) {
			responsavelFinanceiro = new PessoaVO();
		}
		return responsavelFinanceiro;
	}

	public void setResponsavelFinanceiro(PessoaVO responsavelFinanceiro) {
		this.responsavelFinanceiro = responsavelFinanceiro;
	}

	/**
	 * @return the contaReceberHistoricoVOs
	 */
	public List<ContaReceberHistoricoVO> getContaReceberHistoricoVOs() {
		if (contaReceberHistoricoVOs == null) {
			contaReceberHistoricoVOs = new ArrayList<ContaReceberHistoricoVO>();
		}
		return contaReceberHistoricoVOs;
	}

	/**
	 * @param contaReceberHistoricoVOs
	 *            the contaReceberHistoricoVOs to set
	 */
	public void setContaReceberHistoricoVOs(List<ContaReceberHistoricoVO> contaReceberHistoricoVOs) {
		this.contaReceberHistoricoVOs = contaReceberHistoricoVOs;
	}

	public String getPeriodoConta() {
		if (periodoConta == null) {
			periodoConta = "";
		}
		return periodoConta;
	}

	public void setPeriodoConta(String periodoConta) {
		this.periodoConta = periodoConta;
	}

	public Date getDataVencimentoDiaUtil() {
		if (dataVencimentoDiaUtil == null) {
			dataVencimentoDiaUtil = dataVencimento;
		}
		return dataVencimentoDiaUtil;
	}

	public void setDataVencimentoDiaUtil(Date dataVencimentoDiaUtil) {
		this.dataVencimentoDiaUtil = dataVencimentoDiaUtil;
	}

	public Double getTaxaBoleto() {
		if (taxaBoleto == null) {
			taxaBoleto = 0.0;
		}
		return taxaBoleto;
	}

	public void setTaxaBoleto(Double taxaBoleto) {
		this.taxaBoleto = taxaBoleto;
	}

	public Boolean getIsPermiteEnviarEmailCobranca() {
		return (getTipoAluno() || getTipoResponsavelFinanceiro()) && getSituacao().equals("AR") && getDataVencimento().compareTo(new Date()) < 0;
	}

	@XmlElement(name = "turma")
	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	public Boolean getIsPermiteInformarTurma() {
		return getTipoParceiro() || getTipoAluno();
	}

	/**
	 * @return the duplicidadeTratada
	 */
	public Boolean getDuplicidadeTratada() {
		if (duplicidadeTratada == null) {
			duplicidadeTratada = Boolean.FALSE;
		}
		return duplicidadeTratada;
	}

	/**
	 * @param duplicidadeTratada
	 *            the duplicidadeTratada to set
	 */
	public void setDuplicidadeTratada(Boolean duplicidadeTratada) {
		this.duplicidadeTratada = duplicidadeTratada;
	}

	public FornecedorVO getFornecedor() {
		if (fornecedor == null) {
			fornecedor = new FornecedorVO();
		}
		return fornecedor;
	}

	public void setFornecedor(FornecedorVO fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Double getValorFinal() {
		return getValor() + getJuro() + getMulta();
	}

	public Date getDataRecebimento() {
		return dataRecebimento;
	}

	public String getDataRecebimento_Apresentar() {
		if (dataRecebimento == null) {
			return "";
		}
		return (Uteis.getData(dataRecebimento));
	}

	public void setDataRecebimento(Date dataRecebimento) {
		this.dataRecebimento = dataRecebimento;
	}

	public Integer getContaReceberAgrupada() {
		if (contaReceberAgrupada == null) {
			contaReceberAgrupada = 0;
		}
		return contaReceberAgrupada;
	}

	public void setContaReceberAgrupada(Integer contaReceberAgrupada) {
		this.contaReceberAgrupada = contaReceberAgrupada;
	}
	
	public boolean isValorCusteadoContaReceberInformado() {
		return Uteis.isAtributoPreenchido(getValorCusteadoContaReceber());
	}


    /**
     * @return the valorCusteadoContaReceber
     */
    public Double getValorCusteadoContaReceber() {
        if (valorCusteadoContaReceber == null) {
            valorCusteadoContaReceber = 0.0;
        }
        return valorCusteadoContaReceber;
    }

    /**
     * @param valorCusteadoContaReceber the valorCusteadoContaReceber to set
     */
    public void setValorCusteadoContaReceber(Double valorCusteadoContaReceber) {
        this.valorCusteadoContaReceber = valorCusteadoContaReceber;
    }
    
    private Double valorParcelaComCusteioConvenio;
    
    public Double getValorParcelaComCusteioConvenio() {
    	if(valorParcelaComCusteioConvenio == null){
    		valorParcelaComCusteioConvenio = 0.0;
    	}
    	return valorParcelaComCusteioConvenio;
        /*Double valorParcelaComCusteioConvenio = Uteis.arrendondarForcando2CadasDecimais(this.getValor() + this.getValorCusteadoContaReceber());
        return valorParcelaComCusteioConvenio;*/
    }
    
    public void setValorParcelaComCusteioConvenio(Double valorParcelaComCusteioConvenio) {
        this.valorParcelaComCusteioConvenio = valorParcelaComCusteioConvenio;
    }

    /**
     * @return the valorBaseContaReceber
     */
    public Double getValorBaseContaReceber() {
    	if (valorBaseContaReceber == null) {
    		valorBaseContaReceber = 0.0;
    	}
        return valorBaseContaReceber;
    }

    /**
     * @param valorBaseContaReceber the valorBaseContaReceber to set
     */
    public void setValorBaseContaReceber(Double valorBaseContaReceber) {
        this.valorBaseContaReceber = valorBaseContaReceber;
    }

    /**
     * @return the dataProcessamentoValorReceber
     */
    public Date getDataProcessamentoValorReceber() {
        return dataProcessamentoValorReceber;
    }

    /**
     * @param dataProcessamentoValorReceber the dataProcessamentoValorReceber to set
     */
    public void setDataProcessamentoValorReceber(Date dataProcessamentoValorReceber) {
        this.dataProcessamentoValorReceber = dataProcessamentoValorReceber;
    }
    
    public Double getTotalAcrescimoContaReceber() {
    	return Uteis.arrendondarForcando2CadasDecimais(getJuro() + getMulta() + getAcrescimo());
    }

    /**
     * @return the valorReceberCalculado
     */
    public Double getValorReceberCalculado() {
        if (valorReceberCalculado == null) {
            valorReceberCalculado = 0.0;
        }
        return valorReceberCalculado;
    }

    /**
     * @param valorReceberCalculado the valorReceberCalculado to set
     */
    public void setValorReceberCalculado(Double valorReceberCalculado) {
        this.valorReceberCalculado = valorReceberCalculado;
    }

	public Boolean getAtualizandoVencimento() {
		if (atualizandoVencimento == null) {
			atualizandoVencimento= Boolean.FALSE;
		}
		return atualizandoVencimento;
	}

	public void setAtualizandoVencimento(Boolean atualizandoVencimento) {
		this.atualizandoVencimento = atualizandoVencimento;
	}

	public Date getDataCompetencia() {
		if (dataCompetencia == null) {
			dataCompetencia = new Date();
		}
		return dataCompetencia;
	}

	public void setDataCompetencia(Date dataCompetencia) {
		this.dataCompetencia = dataCompetencia;
	}
	
	public Boolean getTelaEdicaoContaReceber() {
		if (telaEdicaoContaReceber == null) {
			telaEdicaoContaReceber = false;
		}
		return telaEdicaoContaReceber;
	}

	public void setTelaEdicaoContaReceber(Boolean telaEdicaoContaReceber) {
		this.telaEdicaoContaReceber = telaEdicaoContaReceber;
	}
        
        public Boolean getPermiteReativarContaReceberCancelada() {
            if (this.getSituacao().equals(SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor())) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        
        public Boolean getPermiteCancelar() {
            if (this.getSituacaoAReceber() && !this.getTipoOrigem().equals(TipoOrigemContaReceber.REQUERIMENTO.getValor())) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }

    /**
     * @return the cancelar
     */
    public boolean isCancelar() {
        return cancelar;
    }

    public boolean getCancelar() {
        return cancelar;
    }

    /**
     * @param cancelar the cancelar to set
     */
    public void setCancelar(boolean cancelar) {
        this.cancelar = cancelar;
    }

    @XmlElement(name = "valorDescontoCalculado")
	public Double getValorDescontoCalculado() {
		if (valorDescontoCalculado == null) {
			valorDescontoCalculado = 0.0;
		}
		return valorDescontoCalculado;
	}

	public void setValorDescontoCalculado(Double valorDescontoCalculado) {
		this.valorDescontoCalculado = valorDescontoCalculado;
	}

	public Boolean getNotaFiscalEmitida() {
		if (notaFiscalEmitida == null) {
			notaFiscalEmitida = Boolean.FALSE;
		}
		return notaFiscalEmitida;
	}

	public void setNotaFiscalEmitida(Boolean notaFiscalEmitida) {
		this.notaFiscalEmitida = notaFiscalEmitida;
	}
	
	public String getNotaFiscalEmitida_Apresentar() {
		if (getNotaFiscalEmitida()) {
			return "Sim";
		}	
		return "Não";
	}

	public Integer getSequencialcodmatperalternativoboleto() {
		if (sequencialcodmatperalternativoboleto == null) {
			sequencialcodmatperalternativoboleto = 0;
		}
		return sequencialcodmatperalternativoboleto;
	}

	public void setSequencialcodmatperalternativoboleto(Integer sequencialcodmatperalternativoboleto) {
		this.sequencialcodmatperalternativoboleto = sequencialcodmatperalternativoboleto;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa
	 */
	//Transient
	private Boolean permiteRecebimentoCartaoCreditoOnline;

	@XmlElement(name = "permiteRecebimentoCartaoCreditoOnline")
	public Boolean getPermiteRecebimentoCartaoCreditoOnline() {
		if(permiteRecebimentoCartaoCreditoOnline == null) {
			permiteRecebimentoCartaoCreditoOnline = false;
		}
		return permiteRecebimentoCartaoCreditoOnline;
	}

	public void setPermiteRecebimentoCartaoCreditoOnline(Boolean permiteRecebimentoCartaoCreditoOnline) {
		this.permiteRecebimentoCartaoCreditoOnline = permiteRecebimentoCartaoCreditoOnline;
	}
	

	public UsuarioVO getUsuarioDesbloqueouDescontoRecebimento() {
		return usuarioDesbloqueouDescontoRecebimento;
	}

	public void setUsuarioDesbloqueouDescontoRecebimento(UsuarioVO usuarioDesbloqueouDescontoRecebimento) {
		this.usuarioDesbloqueouDescontoRecebimento = usuarioDesbloqueouDescontoRecebimento;
	}

	public Date getDataUsuarioDesbloqueouDescontoRecebimento() {
		return dataUsuarioDesbloqueouDescontoRecebimento;
	}

	public void setDataUsuarioDesbloqueouDescontoRecebimento(Date dataUsuarioDesbloqueouDescontoRecebimento) {
		this.dataUsuarioDesbloqueouDescontoRecebimento = dataUsuarioDesbloqueouDescontoRecebimento;
	}

	/**
	 * @return the processamentoIntegracaoFinanceiraDetalheVO
	 */
	public ProcessamentoIntegracaoFinanceiraDetalheVO getProcessamentoIntegracaoFinanceiraDetalheVO() {
		if (processamentoIntegracaoFinanceiraDetalheVO == null) {
			processamentoIntegracaoFinanceiraDetalheVO = new ProcessamentoIntegracaoFinanceiraDetalheVO();
		}
		return processamentoIntegracaoFinanceiraDetalheVO;
	}

	/**
	 * @param processamentoIntegracaoFinanceiraDetalheVO the processamentoIntegracaoFinanceiraDetalheVO to set
	 */
	public void setProcessamentoIntegracaoFinanceiraDetalheVO(ProcessamentoIntegracaoFinanceiraDetalheVO processamentoIntegracaoFinanceiraDetalheVO) {
		this.processamentoIntegracaoFinanceiraDetalheVO = processamentoIntegracaoFinanceiraDetalheVO;
	}

	public Boolean getPossuiPendenciasFinanceirasExternas() {
		if(possuiPendenciasFinanceirasExternas == null) {
			possuiPendenciasFinanceirasExternas = false;
		}
		return possuiPendenciasFinanceirasExternas;
	}

	public void setPossuiPendenciasFinanceirasExternas(Boolean possuiPendenciasFinanceirasExternas) {
		this.possuiPendenciasFinanceirasExternas = possuiPendenciasFinanceirasExternas;
	}

    /**
     * @return the bloquearPagamentoVisaoAluno
     */
	@XmlElement(name = "bloquearPagamentoVisaoAluno")
    public Boolean getBloquearPagamentoVisaoAluno() {
        if (bloquearPagamentoVisaoAluno == null) { 
            bloquearPagamentoVisaoAluno = Boolean.FALSE;
        }
        return bloquearPagamentoVisaoAluno;
    }

    /**
     * @param bloquearPagamentoVisaoAluno the bloquearPagamentoVisaoAluno to set
     */
    public void setBloquearPagamentoVisaoAluno(Boolean bloquearPagamentoVisaoAluno) {
        this.bloquearPagamentoVisaoAluno = bloquearPagamentoVisaoAluno;
    }

	public Date getDataVencimentoAtualizar() {
		if (dataVencimentoAtualizar == null) {
			dataVencimentoAtualizar = getDataVencimento();
		}
		return dataVencimentoAtualizar;
	}

	public void setDataVencimentoAtualizar(Date dataVencimentoAtualizar) {
		this.dataVencimentoAtualizar = dataVencimentoAtualizar;
	}

	public Boolean getRealizandoRecebimentoAutomaticoMatricula() {
		if (realizandoRecebimentoAutomaticoMatricula == null) {
			realizandoRecebimentoAutomaticoMatricula = false;
		}
		return realizandoRecebimentoAutomaticoMatricula;
	}

	public void setRealizandoRecebimentoAutomaticoMatricula(Boolean realizandoRecebimentoAutomaticoMatricula) {
		this.realizandoRecebimentoAutomaticoMatricula = realizandoRecebimentoAutomaticoMatricula;
	}
        
    /**
     * @author Victor Hugo de Paula Costa 21/03/2016 07:53
     */
    private Boolean selecionarContaAReceber;

	public Boolean getSelecionarContaAReceber() {
		if(selecionarContaAReceber == null) {
			selecionarContaAReceber = Boolean.FALSE;
		}
		return selecionarContaAReceber;
	}

	public void setSelecionarContaAReceber(Boolean selecionarContaAReceber) {
		this.selecionarContaAReceber = selecionarContaAReceber;
	}   
	
	
	private Date dataLimiteAplicacaoDesconto;

	public Date getDataLimiteAplicacaoDesconto() {
		if(dataLimiteAplicacaoDesconto == null) {
			dataLimiteAplicacaoDesconto = new Date();
		}
		return dataLimiteAplicacaoDesconto;
	}

	public void setDataLimiteAplicacaoDesconto(Date dataLimiteAplicacaoDesconto) {
		this.dataLimiteAplicacaoDesconto = dataLimiteAplicacaoDesconto;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa 29/04/2016 5.0.5.0
	 */
	private Boolean pagoComDCC;

	public Boolean getPagoComDCC() {
		if (pagoComDCC == null) {
			pagoComDCC = false;
		}
		return pagoComDCC;
	}

	public void setPagoComDCC(Boolean pagoComDCC) {
		this.pagoComDCC = pagoComDCC;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa 02/05/2016 5.0.5.0
	 */
	private List<ContaReceberNegociacaoRecebimentoVO> contaReceberNegociacaoRecebimentoVOs;

	public List<ContaReceberNegociacaoRecebimentoVO> getContaReceberNegociacaoRecebimentoVOs() {
		if (contaReceberNegociacaoRecebimentoVOs == null) {
			contaReceberNegociacaoRecebimentoVOs = new ArrayList<ContaReceberNegociacaoRecebimentoVO>();
		}
		return contaReceberNegociacaoRecebimentoVOs;
	}

	public void setContaReceberNegociacaoRecebimentoVOs(List<ContaReceberNegociacaoRecebimentoVO> contaReceberNegociacaoRecebimentoVOs) {
		this.contaReceberNegociacaoRecebimentoVOs = contaReceberNegociacaoRecebimentoVOs;
	}
	

	public Date getDataOriginalVencimento() {
		if(dataOriginalVencimento == null){
			dataOriginalVencimento = getDataVencimento();
		}
		return dataOriginalVencimento;
	}

	public void setDataOriginalVencimento(Date dataOriginalVencimento) {
		this.dataOriginalVencimento = dataOriginalVencimento;
	}

	public String getMotivoCancelamento() {
		if (motivoCancelamento == null) {
			motivoCancelamento = "";
		}
		return motivoCancelamento;
	}

	public void setMotivoCancelamento(String motivoCancelamento) {
		this.motivoCancelamento = motivoCancelamento;
	}

	public Date getDataCancelamento() {
		if (dataCancelamento == null) {
			dataCancelamento = new Date();
		}
		return dataCancelamento;
	}

	public void setDataCancelamento(Date dataCancelamento) {
		this.dataCancelamento = dataCancelamento;
	}

	public UsuarioVO getResponsavelCancelamentoVO() {
		if (responsavelCancelamentoVO == null) {
			responsavelCancelamentoVO = new UsuarioVO();
		}
		return responsavelCancelamentoVO;
	}

	public void setResponsavelCancelamentoVO(UsuarioVO responsavelCancelamentoVO) {
		this.responsavelCancelamentoVO = responsavelCancelamentoVO;
	}

	public NotaFiscalSaidaServicoVO getNotaFiscalSaidaServicoVO() {
		if(notaFiscalSaidaServicoVO == null){
			notaFiscalSaidaServicoVO = new NotaFiscalSaidaServicoVO();
		}
		return notaFiscalSaidaServicoVO;
	}

	public void setNotaFiscalSaidaServicoVO(NotaFiscalSaidaServicoVO notaFiscalSaidaServicoVO) {
		this.notaFiscalSaidaServicoVO = notaFiscalSaidaServicoVO;
	}
	
	// Transient
	private Integer diaMesAplicativo;
	private String mesAbreviadoAplicativo;
	private Boolean contaVencida;
	private Boolean permiteImprimirBoleto;
	private Boolean permiteImprimirBoletoLinkBanco;

	@XmlElement(name = "diaMesAplicativo")
	public Integer getDiaMesAplicativo() {
		if (diaMesAplicativo == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getDataVencimento());
			diaMesAplicativo = calendar.get(Calendar.DAY_OF_MONTH);
		}
		return diaMesAplicativo;
	}

	public void setDiaMesAplicativo(Integer diaMesAplicativo) {
		this.diaMesAplicativo = diaMesAplicativo;
	}

	@XmlElement(name = "mesAbreviadoAplicativo")
	public String getMesAbreviadoAplicativo() {
		if (mesAbreviadoAplicativo == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getDataVencimento());
			mesAbreviadoAplicativo = MesesAbreviadosAplicativoEnum.getEnumValor(calendar.get(Calendar.MONTH) + 1).getValor();
		}
		return mesAbreviadoAplicativo;
	}

	public void setMesAbreviadoAplicativo(String mesAbreviadoAplicativo) {
		this.mesAbreviadoAplicativo = mesAbreviadoAplicativo;
	}

	@XmlElement(name = "contaVencida")
	public Boolean getContaVencida() {
		if (contaVencida == null) {
			contaVencida = false;
		}
		return contaVencida;
	}

	public void setContaVencida(Boolean contaVencida) {
		this.contaVencida = contaVencida;
	}

	@XmlElement(name = "permiteImprimirBoleto")
	public Boolean getPermiteImprimirBoleto() {
		if (permiteImprimirBoleto == null) {
			permiteImprimirBoleto = false;
		}
		return permiteImprimirBoleto;
	}

	public void setPermiteImprimirBoleto(Boolean permiteImprimirBoleto) {
		this.permiteImprimirBoleto = permiteImprimirBoleto;
	}

	@XmlElement(name = "permiteImprimirBoletoLinkBanco")
	public Boolean getPermiteImprimirBoletoLinkBanco() {
		if (permiteImprimirBoletoLinkBanco == null) {
			permiteImprimirBoletoLinkBanco = getHabilitarBotaoLinkBanco();
		}
		return permiteImprimirBoletoLinkBanco;
	}

	public void setPermiteImprimirBoletoLinkBanco(Boolean permiteImprimirBoletoLinkBanco) {
		this.permiteImprimirBoletoLinkBanco = permiteImprimirBoletoLinkBanco;
	}


	public Double getValorDescontoRateio() {
		if(valorDescontoRateio == null){
			valorDescontoRateio = 0.0;
		}
		return valorDescontoRateio;
	}

	public void setValorDescontoRateio(Double valorDescontoRateio) {
		this.valorDescontoRateio = valorDescontoRateio;
	}
	

	public String getIdentificacaoTituloBanco() {
		if (identificacaoTituloBanco == null) {
			identificacaoTituloBanco = "";
		}
		return identificacaoTituloBanco;
	}

	public void setIdentificacaoTituloBanco(String identificacaoTituloBanco) {
		this.identificacaoTituloBanco = identificacaoTituloBanco;
	}
	
	public boolean getApresentarIdentificaoTituloBanco() {
		if (getIdentificacaoTituloBanco().trim().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}
	
	public String cssTimeLineFichaAluno;
	public String getCssTimeLineFichaAluno() {
		if(cssTimeLineFichaAluno == null) {
		Date diaAtual = new Date();
		if (((Uteis.getDataJDBC(getDataVencimento()).compareTo(Uteis.getDataJDBC(diaAtual)) >= 0) || Uteis.getData(getDataVencimento()).equals(Uteis.getData(diaAtual))) && getSituacao().equals(SituacaoContaReceber.A_RECEBER.getValor())) {
			cssTimeLineFichaAluno =  "timelineContaReceber-badge-aVencer";
		} else if (Uteis.getDataJDBC(getDataVencimento()).compareTo(Uteis.getDataJDBC(diaAtual)) < 0 && getSituacao().equals(SituacaoContaReceber.A_RECEBER.getValor())) {
			cssTimeLineFichaAluno =  "timelineContaReceber-badge-vencido";
		} else if (getSituacao().equals(SituacaoContaReceber.RECEBIDO.getValor())) {
			cssTimeLineFichaAluno =  "timelineContaReceber-badge-recebido";
		} else if (getSituacao().equals(SituacaoContaReceber.NEGOCIADO.getValor())) {
			cssTimeLineFichaAluno =  "timelineContaReceber-badge-negociado";
		} else if (getSituacao().equals(SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor())) {
			cssTimeLineFichaAluno =  "timelineContaReceber-badge-cancelado";
		}else {
			cssTimeLineFichaAluno =  "timelineContaReceber-badge";
		}
		
	}
		return cssTimeLineFichaAluno;
	}
	
	public String cssTimeLineFichaAlunoModal;
	public String getCssTimeLineFichaAlunoModal() {
		if(cssTimeLineFichaAlunoModal == null) {
		Date diaAtual = new Date();
		if (((Uteis.getDataJDBC(getDataVencimento()).compareTo(Uteis.getDataJDBC(new Date())) >= 0) || Uteis.getData(getDataVencimento()).equals(Uteis.getData(diaAtual))) && getSituacao().equals(SituacaoContaReceber.A_RECEBER.getValor())) {
			cssTimeLineFichaAlunoModal =  "timelineContaReceberModal-badge-aVencer";
		} else if (Uteis.getDataJDBC(getDataVencimento()).compareTo(Uteis.getDataJDBC(new Date())) < 0 && getSituacao().equals(SituacaoContaReceber.A_RECEBER.getValor())) {
			cssTimeLineFichaAlunoModal =  "timelineContaReceberModal-badge-vencido";
		} else if (getSituacao().equals(SituacaoContaReceber.RECEBIDO.getValor())) {
			cssTimeLineFichaAlunoModal =  "timelineContaReceberModal-badge-recebido";
		} else if (getSituacao().equals(SituacaoContaReceber.NEGOCIADO.getValor())) {
			cssTimeLineFichaAlunoModal =  "timelineContaReceberModal-badge-negociado";
		} else if (getSituacao().equals(SituacaoContaReceber.CANCELADO_FINANCEIRO.getValor())) {
			cssTimeLineFichaAlunoModal =  "timelineContaReceberModal-badge-cancelado";
		}else {
			cssTimeLineFichaAlunoModal =  "timelineContaReceberModal-badge";
		}
	}
	
		return cssTimeLineFichaAlunoModal;
	}
	
	public String getMesAbreviadoAnoDataVencimento() {
		if (mesAbreviadoAplicativo == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getDataVencimento());
			mesAbreviadoAplicativo = MesesAbreviadosAplicativoEnum.getEnumValor(calendar.get(Calendar.MONTH) + 1).getValor();
		}
		return mesAbreviadoAplicativo + "/" + Uteis.getAno2Digitos(getDataVencimento());
	}
	
	public String getMesAbreviadoAno4DigitosDataVencimento() {
		if (mesAbreviadoAplicativo == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(getDataVencimento());
			mesAbreviadoAplicativo = MesesAbreviadosAplicativoEnum.getEnumValor(calendar.get(Calendar.MONTH) + 1).getValor();
		}
		return mesAbreviadoAplicativo + "/" + Uteis.getAno(getDataVencimento());
	}
	
	public String getNomeDasFormaDePagamentosCompleto() {
		if(nomeDasFormaDePagamentosCompleto == null){
			nomeDasFormaDePagamentosCompleto = "";
		}
		if(!getContaReceberRecebimentoVOs().isEmpty()){
			nomeDasFormaDePagamentosCompleto = "";
			for (ContaReceberRecebimentoVO cont : getContaReceberRecebimentoVOs()) {
				if(cont.getFormaPagamento().isCheque()){
					nomeDasFormaDePagamentosCompleto += "<p>"+cont.getFormaPagamento().getNome() + " - Nº:"+ cont.getFormaPagamentoNegociacaoRecebimentoVO().getCheque().getNumero() +  " - Data Prevista: " + cont.getFormaPagamentoNegociacaoRecebimentoVO().getCheque().getDataPrevisao_Apresentar() + " - Situação: " + cont.getFormaPagamentoNegociacaoRecebimentoVO().getCheque().getSituacaoChequeApresentarAluno() + "</p></br>" ;
				}else{
					nomeDasFormaDePagamentosCompleto += "<p>"+cont.getFormaPagamento().getNome() + "</p></br>";
				}
			}
			nomeDasFormaDePagamentosCompleto = nomeDasFormaDePagamentosCompleto.substring(0, nomeDasFormaDePagamentosCompleto.length() -5);			
		}
		return nomeDasFormaDePagamentosCompleto;
	}

	public void setNomeDasFormaDePagamentosCompleto(String nomeDasFormaDePagamentosCompleto) {
		this.nomeDasFormaDePagamentosCompleto = nomeDasFormaDePagamentosCompleto;
	}

	public String getNomeDasFormaDePagamentosResumido() {
		if(nomeDasFormaDePagamentosResumido == null){
			if(!getNomeDasFormaDePagamentosCompleto().isEmpty() && getNomeDasFormaDePagamentosCompleto().length() > 35){
				nomeDasFormaDePagamentosResumido =  getNomeDasFormaDePagamentosCompleto().substring(0, 35) + " ...</p>";
			}else if(!getNomeDasFormaDePagamentosCompleto().isEmpty()){
				nomeDasFormaDePagamentosResumido =  getNomeDasFormaDePagamentosCompleto();
			}else{
				nomeDasFormaDePagamentosResumido = "";
			}
		}
		return nomeDasFormaDePagamentosResumido;
	}

	public void setNomeDasFormaDePagamentosResumido(String nomeDasFormaDePagamentosResumido) {
		this.nomeDasFormaDePagamentosResumido = nomeDasFormaDePagamentosResumido;
	}


	public Boolean getContaRemetidaComAlteracao() {
		if (contaRemetidaComAlteracao == null) {
			contaRemetidaComAlteracao = Boolean.FALSE;
		}
		return contaRemetidaComAlteracao;
	}

	public void setContaRemetidaComAlteracao(Boolean contaRemetidaComAlteracao) {
		this.contaRemetidaComAlteracao = contaRemetidaComAlteracao;
	}

	public Double getValorDescontoDataLimite() {
		if (valorDescontoDataLimite == null) {
			valorDescontoDataLimite = 0.0;
		}
		return valorDescontoDataLimite;
	}

	public void setValorDescontoDataLimite(Double valorDescontoDataLimite) {
		this.valorDescontoDataLimite = valorDescontoDataLimite;
	}

	public Date getDataLimiteConcessaoDesconto() {
		return dataLimiteConcessaoDesconto;
	}

	public void setDataLimiteConcessaoDesconto(Date dataLimiteConcessaoDesconto) {
		this.dataLimiteConcessaoDesconto = dataLimiteConcessaoDesconto;
	}

	public Double getValorDescontoSemValidade() {
		if (valorDescontoSemValidade == null) {
			valorDescontoSemValidade = 0.0;
		}
		return valorDescontoSemValidade;
	}

	public void setValorDescontoSemValidade(Double valorDescontoSemValidade) {
		this.valorDescontoSemValidade = valorDescontoSemValidade;
	}
	
	
	public String descricaoDescontos;
	public String getDescricaoDescontos(){
		if(descricaoDescontos == null){
			if(getSituacaoAReceber()){				
				StringBuilder str = new StringBuilder();				
				for (PlanoFinanceiroAlunoDescricaoDescontosVO plano : getListaDescontosAplicavesContaReceber()) {
					plano.setAcrescimo(getAcrescimo());
					str.append("<div>").append(plano.getDescricaoDetalhadaComDataLimitesEDescontosValidos()).append("<div>");
				}
				descricaoDescontos = str.toString();
			}else{
				StringBuilder desc = new StringBuilder();
				if(getValorDescontoAlunoJaCalculado()>0.0){
					desc.append("Aluno: ").append(Uteis.getDoubleFormatado(getValorDescontoAlunoJaCalculado()));
				}			
				if(getValorDescontoProgressivo()>0.0){
					if(desc.length() > 0){
						desc.append(", ");
					}
					desc.append("Prog: ").append(Uteis.getDoubleFormatado(getValorDescontoProgressivo()));
				}
				if(getValorDescontoInstituicao()>0.0){
					if(desc.length() > 0){
						desc.append(", ");
					}
					desc.append("Inst: ").append(Uteis.getDoubleFormatado(getValorDescontoInstituicao()));
				}
				if(getValorDescontoConvenio()>0.0){
					if(desc.length() > 0){
						desc.append(", ");
					}
					desc.append("Conv: ").append(Uteis.getDoubleFormatado(getValorDescontoConvenio()));
				}
				if(getValorDescontoRateio()>0.0){
					if(desc.length() > 0){
						desc.append(", ");
					}
					desc.append("Rat: ").append(Uteis.getDoubleFormatado(getValorDescontoRateio()));
				}
				if(getValorCalculadoDescontoLancadoRecebimento()>0.0){
					if(desc.length() > 0){
						desc.append(", ");
					}
					desc.append("Rec: ").append(Uteis.getDoubleFormatado(getValorCalculadoDescontoLancadoRecebimento()));
				}
				if(desc.length() > 0){
					descricaoDescontos = desc.toString()+" = "+Uteis.getDoubleFormatado(getValorTotalDescontoContaReceber());
				}else{
					descricaoDescontos = "";
				}				
			}
		}
		return descricaoDescontos;
	}
	
	public String descricaoAcrescimos;
	public String getDescricaoAcrescimos() {
		if(descricaoAcrescimos == null){
			StringBuilder acr = new StringBuilder();
			if(getJuro() > 0){
				acr.append("Juro: ").append(Uteis.getDoubleFormatado(Uteis.arrendondarForcando2CadasDecimais(getJuro())));
			}
			if(getMulta() > 0){
				if(acr.length() > 0){
					acr.append(", ");
				}
				acr.append("Multa: ").append(Uteis.getDoubleFormatado(Uteis.arrendondarForcando2CadasDecimais(getMulta())));
			}
			if(getAcrescimo() > 0 || getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue() > 0.0){
				if(acr.length() > 0){
					acr.append(", ");
				}
				acr.append("Acréscimo: ").append(Uteis.getDoubleFormatado(Uteis.arrendondarForcando2CadasDecimais(getAcrescimo() + getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue())));
			}
			if(acr.length() > 0){
				descricaoAcrescimos = ""+acr.toString()+" = "+Uteis.getDoubleFormatado(Uteis.arrendondarForcando2CadasDecimais(getJuro()+getMulta()+getAcrescimo()+getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue()));
			}else{
				descricaoAcrescimos = "";
			}				 
		}
		return descricaoAcrescimos;
	}

	public void setDescricaoAcrescimos(String descricaoAcrescimos) {
		this.descricaoAcrescimos = descricaoAcrescimos;
	}


	public Boolean getContaRemetidaComAlteracao_valorBase() {
		if (contaRemetidaComAlteracao_valorBase == null) {
			contaRemetidaComAlteracao_valorBase = Boolean.FALSE;
		}
		return contaRemetidaComAlteracao_valorBase;
	}

	public void setContaRemetidaComAlteracao_valorBase(Boolean contaRemetidaComAlteracao_valorBase) {
		this.contaRemetidaComAlteracao_valorBase = contaRemetidaComAlteracao_valorBase;
	}

	public Boolean getContaRemetidaComAlteracao_valorComAbatimentoAdicionadoOuModificado() {
		if (contaRemetidaComAlteracao_valorComAbatimentoAdicionadoOuModificado == null) {
			contaRemetidaComAlteracao_valorComAbatimentoAdicionadoOuModificado = Boolean.FALSE;
		}
		return contaRemetidaComAlteracao_valorComAbatimentoAdicionadoOuModificado;
	}

	public void setContaRemetidaComAlteracao_valorComAbatimentoAdicionadoOuModificado(Boolean contaRemetidaComAlteracao_valorComAbatimentoAdicionadoOuModificado) {
		this.contaRemetidaComAlteracao_valorComAbatimentoAdicionadoOuModificado = contaRemetidaComAlteracao_valorComAbatimentoAdicionadoOuModificado;
	}

	public Boolean getContaRemetidaComAlteracao_valorComAbatimentoRemovido() {
		if (contaRemetidaComAlteracao_valorComAbatimentoRemovido == null) {
			contaRemetidaComAlteracao_valorComAbatimentoRemovido = Boolean.FALSE;
		}
		return contaRemetidaComAlteracao_valorComAbatimentoRemovido;
	}

	public void setContaRemetidaComAlteracao_valorComAbatimentoRemovido(Boolean contaRemetidaComAlteracao_valorComAbatimentoRemovido) {
		this.contaRemetidaComAlteracao_valorComAbatimentoRemovido = contaRemetidaComAlteracao_valorComAbatimentoRemovido;
	}

	public Boolean getContaRemetidaComAlteracao_valorDescProgressivo() {
		if (contaRemetidaComAlteracao_valorDescProgressivo == null) {
			contaRemetidaComAlteracao_valorDescProgressivo = Boolean.FALSE;
		}
		return contaRemetidaComAlteracao_valorDescProgressivo;
	}

	public void setContaRemetidaComAlteracao_valorDescProgressivo(Boolean contaRemetidaComAlteracao_valorDescProgressivo) {
		this.contaRemetidaComAlteracao_valorDescProgressivo = contaRemetidaComAlteracao_valorDescProgressivo;
	}

	public Boolean getContaRemetidaComAlteracao_dataDescProgressivo() {
		if (contaRemetidaComAlteracao_dataDescProgressivo == null) {
			contaRemetidaComAlteracao_dataDescProgressivo = Boolean.FALSE;
		}
		return contaRemetidaComAlteracao_dataDescProgressivo;
	}

	public void setContaRemetidaComAlteracao_dataDescProgressivo(Boolean contaRemetidaComAlteracao_dataDescProgressivo) {
		this.contaRemetidaComAlteracao_dataDescProgressivo = contaRemetidaComAlteracao_dataDescProgressivo;
	}

	public Boolean getContaRemetidaComAlteracao_dataVencimento() {
		if (contaRemetidaComAlteracao_dataVencimento == null) {
			contaRemetidaComAlteracao_dataVencimento = Boolean.FALSE;
		}
		return contaRemetidaComAlteracao_dataVencimento;
	}

	public void setContaRemetidaComAlteracao_dataVencimento(Boolean contaRemetidaComAlteracao_dataVencimento) {
		this.contaRemetidaComAlteracao_dataVencimento = contaRemetidaComAlteracao_dataVencimento;
	}

	public Boolean getVerificaRegraBloqueioEmissaoBoleto() {
		if (verificaRegraBloqueioEmissaoBoleto == null) {
			verificaRegraBloqueioEmissaoBoleto = Boolean.FALSE;
		}
		return verificaRegraBloqueioEmissaoBoleto;
	}

	public void setVerificaRegraBloqueioEmissaoBoleto(Boolean verificaRegraBloqueioEmissaoBoleto) {
		this.verificaRegraBloqueioEmissaoBoleto = verificaRegraBloqueioEmissaoBoleto;
	}

	public Boolean getEmissaoBloqueada() {
		if (emissaoBloqueada == null) {
			emissaoBloqueada = Boolean.FALSE;
		}
		return emissaoBloqueada;
	}

	public void setEmissaoBloqueada(Boolean emissaoBloqueada) {
		this.emissaoBloqueada = emissaoBloqueada;
	}
	
	public boolean isLancamentoContabil() {
		return lancamentoContabil;
	}

	public void setLancamentoContabil(boolean lancamentoContabil) {
		this.lancamentoContabil = lancamentoContabil;
	}

	public List<LancamentoContabilVO> getListaLancamentoContabeisDebito() {
		if (listaLancamentoContabeisDebito == null) {
			listaLancamentoContabeisDebito = new ArrayList<>();
		}
		return listaLancamentoContabeisDebito;
	}

	public void setListaLancamentoContabeisDebito(List<LancamentoContabilVO> listaLancamentoContabeisDebito) {
		this.listaLancamentoContabeisDebito = listaLancamentoContabeisDebito;
	}

	public List<LancamentoContabilVO> getListaLancamentoContabeisCredito() {
		if (listaLancamentoContabeisCredito == null) {
			listaLancamentoContabeisCredito = new ArrayList<>();
		}
		return listaLancamentoContabeisCredito;
	}

	public void setListaLancamentoContabeisCredito(List<LancamentoContabilVO> listaLancamentoContabeisCredito) {
		this.listaLancamentoContabeisCredito = listaLancamentoContabeisCredito;
	}
	
	public Double getTotalLancamentoContabeisCredito() {
		Double valor = 0.0;
		for (LancamentoContabilVO lc : getListaLancamentoContabeisCredito()) {
			valor = valor + lc.getValor();
		}
		return valor;
	}
	
	public Double getTotalLancamentoContabeisDebito() {
		Double valor = 0.0;
		for (LancamentoContabilVO lc : getListaLancamentoContabeisDebito()) {
			valor = valor + lc.getValor();
		}
		return valor;
	}
	
	public Double getTotalLancamentoContabeisCreditoTipoValorContaReceber() {
		Double valor = 0.0;
		for (LancamentoContabilVO lc : getListaLancamentoContabeisCredito()) {
			if(lc.getTipoValorLancamentoContabilEnum().isContaReceber()){
				valor = valor + lc.getValor();	
			}
		}
		return valor;
	}
	
	public Double getTotalLancamentoContabeisDebitoTipoValorContaReceber() {
		Double valor = 0.0;
		for (LancamentoContabilVO lc : getListaLancamentoContabeisDebito()) {
			if(lc.getTipoValorLancamentoContabilEnum().isContaReceber()){
				valor = valor + lc.getValor();	
			}
		}
		return valor;
	}

	public Boolean getNossoNumeroAlterado() {
		if (nossoNumeroAlterado == null) {
			nossoNumeroAlterado = Boolean.FALSE;
		}
		return nossoNumeroAlterado;
	}

	public void setNossoNumeroAlterado(Boolean nossoNumeroAlterado) {
		this.nossoNumeroAlterado = nossoNumeroAlterado;
	}

	public BigDecimal getValorIndiceReajuste() {
		if (valorIndiceReajuste == null) {
			valorIndiceReajuste = BigDecimal.ZERO;
		}
		return valorIndiceReajuste;
	}

	public void setValorIndiceReajuste(BigDecimal valorIndiceReajuste) {
		this.valorIndiceReajuste = valorIndiceReajuste;
	}

	@XmlElement(name = "valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa")
	public BigDecimal getValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa() {
		if (valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa == null) {
			valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa = BigDecimal.ZERO;
		}
		return valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa;
	}

	public void setValorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa(BigDecimal valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa) {
		this.valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa = valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa;
	}
	
	public boolean isAplicadoIndireReajustePorAtraso(){
		return Uteis.isAtributoPreenchido(getIndiceReajustePadraoPorAtraso());
	}
	
	public boolean isAplicadoIndireReajustePorAtrasoComoAcrescimo(){
		return getValorIndiceReajustePorAtraso().doubleValue() > 0.0;
	}
	
	public IndiceReajusteVO getIndiceReajustePadraoPorAtraso() {
		if (indiceReajustePadraoPorAtraso == null) {
			indiceReajustePadraoPorAtraso = new IndiceReajusteVO();
		}
		return indiceReajustePadraoPorAtraso;
	}

	public void setIndiceReajustePadraoPorAtraso(IndiceReajusteVO indiceReajustePadraoPorAtraso) {
		this.indiceReajustePadraoPorAtraso = indiceReajustePadraoPorAtraso;
	}

	@XmlElement(name = "valorIndiceReajustePorAtraso")
	public BigDecimal getValorIndiceReajustePorAtraso() {
		if (valorIndiceReajustePorAtraso == null) {
			valorIndiceReajustePorAtraso = BigDecimal.ZERO;
		}
		return valorIndiceReajustePorAtraso;
	}

	public void setValorIndiceReajustePorAtraso(BigDecimal valorIndiceReajustePorAtraso) {
		this.valorIndiceReajustePorAtraso = valorIndiceReajustePorAtraso;
	}

	public boolean isLiberadoDoIndiceReajustePorAtraso() {
		return liberadoDoIndiceReajustePorAtraso;
	}

	public void setLiberadoDoIndiceReajustePorAtraso(boolean liberadoDoIndiceReajustePorAtraso) {
		this.liberadoDoIndiceReajustePorAtraso = liberadoDoIndiceReajustePorAtraso;
	}

	private List<PlanoDescontoContaReceberVO> planoDescontoInstitucionalContaReceber;
	private List<PlanoDescontoContaReceberVO> planoDescontoConvenioContaReceber;
	private List<PlanoDescontoContaReceberVO> planoDescontoConvenioCusteadoContaReceber;
	
	public List<PlanoDescontoContaReceberVO> getPlanoDescontoInstitucionalContaReceber() {
		if (planoDescontoInstitucionalContaReceber == null) {
			planoDescontoInstitucionalContaReceber = new ArrayList<PlanoDescontoContaReceberVO>(0);
			for(PlanoDescontoContaReceberVO planoDescontoContaReceberVO: getPlanoDescontoContaReceberVOs()) {
				if ((planoDescontoContaReceberVO.getIsPlanoDescontoInstitucional()) ||
				    (planoDescontoContaReceberVO.getIsPlanoDescontoManual())) {
					planoDescontoInstitucionalContaReceber.add(planoDescontoContaReceberVO);
				}
			}
			Ordenacao.ordenarLista(planoDescontoInstitucionalContaReceber, "ordenacao");
		}
		return planoDescontoInstitucionalContaReceber;
	}

	public void setPlanoDescontoInstitucionalContaReceber(List<PlanoDescontoContaReceberVO> planoDescontoInstitucionalContaReceber) {
		this.planoDescontoInstitucionalContaReceber = planoDescontoInstitucionalContaReceber;
	}
		

	public List<PlanoDescontoContaReceberVO> getPlanoDescontoConvenioContaReceber() {
		if (planoDescontoConvenioContaReceber == null) {
			planoDescontoConvenioContaReceber = new ArrayList<PlanoDescontoContaReceberVO>(0);
			for(PlanoDescontoContaReceberVO planoDescontoContaReceberVO: getPlanoDescontoContaReceberVOs()) {
				if(planoDescontoContaReceberVO.getIsConvenio() && 
						((!planoDescontoContaReceberVO.getConvenio().getAbaterDescontoNoValorMatricula() && getTipoOrigem().equals(TipoOrigemContaReceber.MATRICULA.getValor()))
								|| (!planoDescontoContaReceberVO.getConvenio().getAbaterDescontoNoValorParcela() && !getTipoOrigem().equals(TipoOrigemContaReceber.MATRICULA.getValor())))) {
					planoDescontoConvenioContaReceber.add(planoDescontoContaReceberVO);
				}
			}
			Ordenacao.ordenarLista(planoDescontoConvenioContaReceber, "ordenacao");
		}
		return planoDescontoConvenioContaReceber;
	}

	public void setPlanoDescontoConvenioContaReceber(List<PlanoDescontoContaReceberVO> planoDescontoConvenioContaReceber) {
		this.planoDescontoConvenioContaReceber = planoDescontoConvenioContaReceber;
	}

	public List<PlanoDescontoContaReceberVO> getPlanoDescontoConvenioCusteadoContaReceber() {
		if (planoDescontoConvenioCusteadoContaReceber == null) {
			planoDescontoConvenioCusteadoContaReceber = new ArrayList<PlanoDescontoContaReceberVO>(0);
			for(PlanoDescontoContaReceberVO planoDescontoContaReceberVO: getPlanoDescontoContaReceberVOs()) {
				if(planoDescontoContaReceberVO.getIsConvenio() && 
						((planoDescontoContaReceberVO.getConvenio().getAbaterDescontoNoValorMatricula() && getTipoOrigem().equals(TipoOrigemContaReceber.MATRICULA.getValor()))
								|| (planoDescontoContaReceberVO.getConvenio().getAbaterDescontoNoValorParcela() && !getTipoOrigem().equals(TipoOrigemContaReceber.MATRICULA.getValor())))) {
					planoDescontoConvenioCusteadoContaReceber.add(planoDescontoContaReceberVO);
				}
			}
			Ordenacao.ordenarLista(planoDescontoConvenioCusteadoContaReceber, "ordenacao");
		}
		return planoDescontoConvenioCusteadoContaReceber;
	}

	public void setPlanoDescontoConvenioCusteadoContaReceber(List<PlanoDescontoContaReceberVO> planoDescontoConvenioCusteadoContaReceber) {
		this.planoDescontoConvenioCusteadoContaReceber = planoDescontoConvenioCusteadoContaReceber;
	}
	
	public Double getValorTotalAcrescimos() {
		return Uteis.arrendondarForcando2CadasDecimais(getJuro()+getMulta()+getAcrescimo());
	}

//	public RegistroNegativacaoCobrancaContaReceberVO getRegistroNegativacaoCobrancaContaReceber() {
//		if (registroNegativacaoCobrancaContaReceber == null) {
//			registroNegativacaoCobrancaContaReceber = new RegistroNegativacaoCobrancaContaReceberVO();
//		}
//		return registroNegativacaoCobrancaContaReceber;
//	}
//
//	public void setRegistroNegativacaoCobrancaContaReceber(RegistroNegativacaoCobrancaContaReceberVO registroNegativacaoCobrancaContaReceber) {
//		this.registroNegativacaoCobrancaContaReceber = registroNegativacaoCobrancaContaReceber;
//	}

	public String getSituacaoRegistroNegativacaoCobrancaContaReceber() {
		if (situacaoRegistroNegativacaoCobrancaContaReceber == null) {
			situacaoRegistroNegativacaoCobrancaContaReceber = "";
		}
		return situacaoRegistroNegativacaoCobrancaContaReceber;
	}

	public void setSituacaoRegistroNegativacaoCobrancaContaReceber(String situacaoRegistroNegativacaoCobrancaContaReceber) {
		this.situacaoRegistroNegativacaoCobrancaContaReceber = situacaoRegistroNegativacaoCobrancaContaReceber;
	}
	
//	public boolean getPossuiRegistroCobranca() {
//		return getRegistroNegativacaoCobrancaContaReceber().getCodigo().intValue() > 0;
//	}
	
	public boolean getPossuiRegistroNegativacao() {
		for(RegistroNegativacaoCobrancaContaReceberItemVO registroNegativacaoCobrancaContaReceberItemVO: getHistoricoNegativacoes()) {
			if(registroNegativacaoCobrancaContaReceberItemVO.getDataExclusao() == null) {
				return true;
			}
		}
		return false;
	}

	public List<RegistroNegativacaoCobrancaContaReceberItemVO> getHistoricoNegativacoes() {
		if (historicoNegativacoes == null) {
			historicoNegativacoes = new ArrayList<RegistroNegativacaoCobrancaContaReceberItemVO>();
		}
		return historicoNegativacoes;
	}

	public void setHistoricoNegativacoes(List<RegistroNegativacaoCobrancaContaReceberItemVO> historicoNegativacoes) {
		this.historicoNegativacoes = historicoNegativacoes;
	}

	public Boolean getIndiceReajusteCalculado() {
		if (indiceReajusteCalculado == null) {
			indiceReajusteCalculado = Boolean.FALSE;
		}
		return indiceReajusteCalculado;
	}

	public void setIndiceReajusteCalculado(Boolean indiceReajusteCalculado) {
		this.indiceReajusteCalculado = indiceReajusteCalculado;
	}
	
	/**
	 * INICIO MERGE EDIGAR - 21/03/2018
	 * Campo criado para controlar se uma conta foi editada manualmente, alterando-se assim, seus dados
	 * como descontos, planos de descontos, convenios, desconto progressivo, datas e ate mesmo valor base.
	 * Uma vez colocado sobre esta situação. A conta a receber não será regerada automaticamente pelo SEI.
	 * A não ser que o usuário delibere para que isto ocorra.
	 * Ao utilizar este recurso não estamos fazendo uso de uma renegociação, mas sim, editando a conta a receber
	 * em si. Ou seja, é um processo diferente ao alterar vcto e valor.
	 */
	private Boolean contaEditadaManualmente;
	private Integer diaLimite1;
	private Integer diaLimite2;
	private Integer diaLimite3;
	private Integer diaLimite4;
	private Boolean utilizarDescontoProgressivoManual;	
	private List<InteracaoWorkflowVO> listaFollowUpConta;
	
	/**
	 * transient usado para manter o prospect do aluno, para persistir observacoes da conta a receber.
	 * @return
	 */
	private ProspectsVO prospectVO;

	public String getNrBanco() {
		if (nrBanco == null) {
			nrBanco = "";
		}
		return nrBanco;
	}

	public void setNrBanco(String nrBanco) {
		this.nrBanco = nrBanco;
	}

	public ProspectsVO getProspectVO() {
		if (prospectVO == null) {
			prospectVO = new ProspectsVO();
		}
		return prospectVO;
	}

	public void setProspectVO(ProspectsVO prospectVO) {
		this.prospectVO = prospectVO;
	}

	public List<InteracaoWorkflowVO> getListaFollowUpConta() {
		if (listaFollowUpConta == null) {
			listaFollowUpConta = new ArrayList<InteracaoWorkflowVO>();
		}
		return listaFollowUpConta;
	}

	public void setListaFollowUpConta(List<InteracaoWorkflowVO> listaFollowUpConta) {
		this.listaFollowUpConta = listaFollowUpConta;
	}

	public Boolean getUtilizarDescontoProgressivoManual() {
		if (utilizarDescontoProgressivoManual == null) {
			utilizarDescontoProgressivoManual = Boolean.FALSE;
		}
		return utilizarDescontoProgressivoManual;
	}

	public void setUtilizarDescontoProgressivoManual(Boolean utilizarDescontoProgressivoManual) {
		this.utilizarDescontoProgressivoManual = utilizarDescontoProgressivoManual;
	}
	
	public Boolean getContaEditadaManualmente() {
		if (contaEditadaManualmente == null) {
			contaEditadaManualmente = Boolean.FALSE;
		}
		return contaEditadaManualmente;
	}

	public void setContaEditadaManualmente(Boolean contaEditadaManualmente) {
		this.contaEditadaManualmente = contaEditadaManualmente;
	}
	
	public Boolean getContaReferenteParcelaConvenio() {
		if (this.getTipoOrigem().equals("BCC")) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Integer getDiaLimite1() {
		return diaLimite1;
	}

	public void setDiaLimite1(Integer diaLimite1) {
		this.diaLimite1 = diaLimite1;
	}

	public Integer getDiaLimite2() {
		return diaLimite2;
	}

	public void setDiaLimite2(Integer diaLimite2) {
		this.diaLimite2 = diaLimite2;
	}

	public Integer getDiaLimite3() {
		return diaLimite3;
	}

	public void setDiaLimite3(Integer diaLimite3) {
		this.diaLimite3 = diaLimite3;
	}

	public Integer getDiaLimite4() {
		return diaLimite4;
	}

	public void setDiaLimite4(Integer diaLimite4) {
		this.diaLimite4 = diaLimite4;
	}
	
    public Integer obterPosicaoOrdemListaDesconto(List<OrdemDescontoVO> listaOrdemDesconto, String tipoDesconto) {
        for (OrdemDescontoVO ordemDesconto : listaOrdemDesconto) {
            if (ordemDesconto.getDescricaoDesconto().equals(tipoDesconto)) {
                return ordemDesconto.getPosicaoAtual();
            }
        }
        return 0;
    }

    public void aplicarOrdemDescontoDefinidaUsuarioContaReceber(List<OrdemDescontoVO> listaOrdemDesconto) {
        this.setOrdemConvenio(obterPosicaoOrdemListaDesconto(listaOrdemDesconto, "Convênio"));
        this.setOrdemDescontoAluno(obterPosicaoOrdemListaDesconto(listaOrdemDesconto, "Desconto Aluno"));
        this.setOrdemPlanoDesconto(obterPosicaoOrdemListaDesconto(listaOrdemDesconto, "Plano Desconto"));
        this.setOrdemDescontoProgressivo(obterPosicaoOrdemListaDesconto(listaOrdemDesconto, "Desc.Progressivo"));
    }

    public void alterarOrdemAplicacaoDescontosSubindoItem(List<OrdemDescontoVO> listaOrdemDesconto, OrdemDescontoVO itemSubir) {
        if (itemSubir.getPosicaoAtual().equals(0)) {
            // nao tem mais como subir
        }
        if (itemSubir.getPosicaoAtual().equals(1)) {
            // entao ele vai para a posicao 0, e o que está na posicao 0, vai
            // para 1)
            OrdemDescontoVO itemAcima = listaOrdemDesconto.get(0);
            itemAcima.setPosicaoAtual(1);
            itemSubir.setPosicaoAtual(0);
        }
        if (itemSubir.getPosicaoAtual().equals(2)) {
            // entao ele vai para a posicao 1, e o que está na posicao 2, vai
            // para 1)
            OrdemDescontoVO itemAcima = listaOrdemDesconto.get(1);
            itemAcima.setPosicaoAtual(2);
            itemSubir.setPosicaoAtual(1);
        }
        if (itemSubir.getPosicaoAtual().equals(3)) {
            // entao ele vai para a posicao 2, e o que está na posicao 2, vai
            // para 3)
            OrdemDescontoVO itemAcima = listaOrdemDesconto.get(2);
            itemAcima.setPosicaoAtual(3);
            itemSubir.setPosicaoAtual(2);
        }
        Ordenacao.ordenarLista(listaOrdemDesconto, "posicaoAtual");
        aplicarOrdemDescontoDefinidaUsuarioContaReceber(listaOrdemDesconto);
    }
    
    public void alterarOrdemAplicacaoDescontosDescentoItem(List<OrdemDescontoVO> listaOrdemDesconto, OrdemDescontoVO itemDescer) {
        if (itemDescer.getPosicaoAtual().equals(3)) {
        }
        if (itemDescer.getPosicaoAtual().equals(2)) {
            // entao ele vai para a posicao 1, e o que está na posicao 1, vai
            // para 2)
            OrdemDescontoVO itemAbaixo = listaOrdemDesconto.get(3);
            itemAbaixo.setPosicaoAtual(2);
            itemDescer.setPosicaoAtual(3);
        }
        if (itemDescer.getPosicaoAtual().equals(1)) {
            // entao ele vai para a posicao 0, e o que está na posicao 0, vai
            // para 1)
            OrdemDescontoVO itemAbaixo = listaOrdemDesconto.get(2);
            itemAbaixo.setPosicaoAtual(1);
            itemDescer.setPosicaoAtual(2);
        }
        if (itemDescer.getPosicaoAtual().equals(0)) {
            // entao ele vai para a posicao 1, e o que está na posicao 2, vai
            // para 1)
            OrdemDescontoVO itemAbaixo = listaOrdemDesconto.get(1);
            itemAbaixo.setPosicaoAtual(0);
            itemDescer.setPosicaoAtual(1);
        }
        Ordenacao.ordenarLista(listaOrdemDesconto, "posicaoAtual");
        aplicarOrdemDescontoDefinidaUsuarioContaReceber(listaOrdemDesconto);
    }
    
    public String getIdentificadorOrigem() {
    	if (this.getTipoAluno()) { 
			return this.getParcela();
		}
    	if (this.getContaReferenteParcelaConvenio()) {
    		return this.getParcela() + "_" + this.getConvenio().getCodigo();
    	}
    	return this.getParcela(); 
    }
    
	public Integer getCodigoEntidadeOrigem() {
		if (this.getTipoAluno()) { 
			return this.getMatriculaPeriodo();
		}
    	if (this.getContaReferenteParcelaConvenio()) {
			return this.getMatriculaPeriodo();
    	}
		if (this.getContaReferenteParcelaConvenio()) {
    		
    	}
		return this.getCodigo();
	}

	public boolean isContaQuitadaPossuiPlanoDescontoComValidade() {
		if(getSituacaoEQuitada()  && getValorRecebido().equals(0.0) && getValorDescontoInstituicao() > 0) {
			return getPlanoDescontoInstitucionalContaReceber().stream().anyMatch(p-> !p.getPlanoDescontoVO().getUtilizarDescontoSemLimiteValidade());
		}
		return false;
	}
	
	public Boolean getPossuiFiesIntegracaoFinanceiras() {
		if (possuiFiesIntegracaoFinanceiras == null) {
			possuiFiesIntegracaoFinanceiras = Boolean.FALSE;
		}
		return possuiFiesIntegracaoFinanceiras;
	}

	public void setPossuiFiesIntegracaoFinanceiras(Boolean possuiFiesIntegracaoFinanceiras) {
		this.possuiFiesIntegracaoFinanceiras = possuiFiesIntegracaoFinanceiras;
	}
	
	public Boolean verificarExistePlanoDescontoMesmoNome(String nomeVerificar, Integer indice) {		
		int x = 0;
		for (PlanoDescontoContaReceberVO plano : getPlanoDescontoContaReceberVOs()) {
			if (plano.getNome().equals(nomeVerificar) && x != indice) {
				return true;
			}
			x++;
		}
		return false;
	}
	
	public Integer obterProximoCodigoSimuladoDescontoManualRegistradosConta() {
		Integer menorCodigoRegistrado = 0;
		for (PlanoDescontoContaReceberVO plano : getPlanoDescontoContaReceberVOs()) {
			if ((plano.getIsPlanoDescontoManual()) &&
			    (!plano.getPlanoDescontoVO().getCodigo().equals(0))) {
				if (plano.getPlanoDescontoVO().getCodigo() < menorCodigoRegistrado) {
					menorCodigoRegistrado = plano.getPlanoDescontoVO().getCodigo();
				}
			}
		}
		// como obtemos o menor codigo (por se tratar de codigo negativos, para nao correr risco 
		// de coincidir com o codigo de planoDesconto que realmente existe na base de dados.
		menorCodigoRegistrado = menorCodigoRegistrado - 1;
		return menorCodigoRegistrado;
	}
	
	public Integer obterNrDescontosManuaisRegistradosConta() {
		int contador = 0;
		for (PlanoDescontoContaReceberVO plano : getPlanoDescontoContaReceberVOs()) {
			if (plano.getIsPlanoDescontoManual()) {
				contador++;
			}
		}
		return contador;
	}
	
	public Integer obterProximoNrPrioridadeDescontosManuaisRegistradosConta() {
		int maior = 0;
		for (PlanoDescontoContaReceberVO plano : getPlanoDescontoContaReceberVOs()) {
			if (plano.getIsPlanoDescontoManual()) {
				if (plano.getOrdemPrioridadeParaCalculo() > maior) {
					maior = plano.getOrdemPrioridadeParaCalculo();
				}
			}
		}
		return maior + 1;
	}
	
	/*
	 * Atributo transiente utilizado para manter a a data de vencimento original da conta 
	 * a receber, antes de qualquer relatorio.
	 */
	private Date dataVencimentoAntesAlteracao;
	public Date getDataVencimentoAntesAlteracao() {
		return dataVencimentoAntesAlteracao;
	}
	public void setDataVencimentoAntesAlteracao(Date dataVencimentoAntesAlteracao) {
		this.dataVencimentoAntesAlteracao = dataVencimentoAntesAlteracao;
	}
	private Date dataCompetenciaAntesAlteracao;
	public Date getDataCompetenciaAntesAlteracao() {
		return dataCompetenciaAntesAlteracao;
	}
	public void setDataCompetenciaAntesAlteracao(Date dataCompetenciaAntesAlteracao) {
		this.dataCompetenciaAntesAlteracao = dataCompetenciaAntesAlteracao;
	}	
	
	public Boolean getDescontoAlunoValidoAteVencimento() {
		return ((getTipoOrigem().equals("MAT") || getTipoOrigem().equals("MEN") || getTipoOrigem().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO.getValor()))   
				&& this.getMatriculaAluno().getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela());
	}
	
	public void atualizarSituacaoValorCheioOrdemDesconto(OrdemDescontoVO ordemClicou) {
        if (ordemClicou.isDescontoAluno()) {
            this.setOrdemDescontoAlunoValorCheio(ordemClicou.getValorCheio());
        }
        if (ordemClicou.isPlanoDesconto()) {
            this.setOrdemPlanoDescontoValorCheio(ordemClicou.getValorCheio());
        }
        if (ordemClicou.isConvenio()) {
            this.setOrdemConvenioValorCheio(ordemClicou.getValorCheio());
        }
        if (ordemClicou.isDescontoProgressivo()) {
            this.setOrdemDescontoProgressivoValorCheio(ordemClicou.getValorCheio());
        }
    }
	
	public String getDescricaoBaseCalculoDescontoProgressivo() {
		if(getOrdemDescontoProgressivoValorCheio()) {
			return "Aplicar Sobre Valor Cheio";
		}
		if(getMatriculaAluno().getPlanoFinanceiroAluno().getCondicaoPagamentoPlanoFinanceiroCursoVO().getAplicarCalculoComBaseDescontosCalculados()) {
			return "Aplicar Sobre Valor Liquido Deduzidos Outros Descontos na Ordem de Aplicação "+(getOrdemDescontoProgressivo()+1);
		}
		return "Aplicar Sobre Valor Liquido na Ordem de Aplicação "+(getOrdemDescontoProgressivo()+1);		
	}
	
	public String getDescricaoBaseCalculoDescontoAluno() {
		if(getOrdemDescontoAlunoValorCheio()) {
			return "Aplicar Sobre Valor Cheio";
		}
		if(getMatriculaAluno().getPlanoFinanceiroAluno().getCondicaoPagamentoPlanoFinanceiroCursoVO().getAplicarCalculoComBaseDescontosCalculados()) {
			return "Aplicar Sobre Valor Liquido Deduzidos Outros Descontos na Ordem de Aplicação "+(getOrdemDescontoAluno()+1);
		}
		return "Aplicar Sobre Valor Liquido na Ordem de Aplicação "+(getOrdemDescontoAluno()+1);		
	}
	
	public String getDescricaoValidadeDescontoAluno() {		
		if(getMatriculaAluno().getPlanoFinanceiroAluno().getDescontoValidoAteDataParcela() && (getTipoOrigem().equals("MAT") || getTipoOrigem().equals("MEN") || getTipoOrigem().equals("MDI"))) {
			return "Desconto Válido até Vencimento";
		}
		return "Desconto Sem Validade";		
	}

	public List<CentroResultadoOrigemVO> getListaCentroResultadoOrigem() {
		listaCentroResultadoOrigem = Optional.ofNullable(listaCentroResultadoOrigem).orElse(new ArrayList<>());
		return listaCentroResultadoOrigem;
	}

	public void setListaCentroResultadoOrigem(List<CentroResultadoOrigemVO> listaCentroResultadoOrigem) {
		this.listaCentroResultadoOrigem = listaCentroResultadoOrigem;
	}
	
	public Double getQuantidadeCentroResultadoTotal() {
		return getListaCentroResultadoOrigem().stream().mapToDouble(CentroResultadoOrigemVO::getQuantidade).sum();
	}

	public Double getPorcentagemCentroResultadoTotal() {
		return getListaCentroResultadoOrigem().stream().map(p -> p.getPorcentagem()).reduce(0D, (a, b) -> Uteis.arrendondarForcandoCadasDecimais(a + b, 8));
	}

	public Double getPrecoCentroResultadoTotal() {
		return getListaCentroResultadoOrigem().stream().map(p -> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public List<DetalhamentoValorContaVO> getDetalhamentoValorContaVOs() {
		if(detalhamentoValorContaVOs == null){
			detalhamentoValorContaVOs = new ArrayList<DetalhamentoValorContaVO>(0);
		}
		return detalhamentoValorContaVOs;
	}

	public void setDetalhamentoValorContaVOs(List<DetalhamentoValorContaVO> detalhamentoValorContaVOs) {
		this.detalhamentoValorContaVOs = detalhamentoValorContaVOs;
	}
	
	public Double getTotalCentroResultadoOrigemDetalheVO() {
		Double totalCentroResultadoOrigemDetalhe = 0.0;
		for (DetalhamentoValorContaVO p : getDetalhamentoValorContaVOs()) {
			if (p.isTipoDetalheSoma() || (p.isTipoDetalheSubtrair() && p.getValor() < 0.0)) {
				totalCentroResultadoOrigemDetalhe = totalCentroResultadoOrigemDetalhe + p.getValor();
			} else if (p.isTipoDetalheSubtrair() && p.getValor() >= 0.0) {
				totalCentroResultadoOrigemDetalhe = totalCentroResultadoOrigemDetalhe - p.getValor();
			}
		}
		return Uteis.arrendondarForcando2CadasDecimais(totalCentroResultadoOrigemDetalhe);
	}

	public Boolean getContaAptaGerarNegociacaoAutomatica() {
		// caso alterem esta regra alterar tambem na consulta realizarValidacaoContaReceberAptaRealizarNegociacaoAutomaticamente em ContaReceber
		return getSituacaoAReceber() 
				&& !getTipoOrigem().equals(TipoOrigemContaReceber.REQUERIMENTO.getValor())
				&& !getTipoOrigem().equals(TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO.getValor()) 
				&& ((getTipoOrigem().equals(TipoOrigemContaReceber.BIBLIOTECA.getValor()) && getContaCorrenteVO().getTipoOrigemBiblioteca())
					|| (getTipoOrigem().equals(TipoOrigemContaReceber.BOLSA_CUSTEADA_CONVENIO.getValor()) && getContaCorrenteVO().getTipoOrigemBolsaCusteadaConvenio())
					|| (getTipoOrigem().equals(TipoOrigemContaReceber.CONTRATO_RECEITA.getValor()) && getContaCorrenteVO().getTipoOrigemContratoReceita())
					|| (getTipoOrigem().equals(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.getValor()) && getContaCorrenteVO().getTipoOrigemDevolucaoCheque())
					|| (getTipoOrigem().equals(TipoOrigemContaReceber.INCLUSAOREPOSICAO.getValor()) && getContaCorrenteVO().getTipoOrigemInclusaoReposicao())
					|| (getTipoOrigem().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO.getValor()) && getContaCorrenteVO().getTipoOrigemMaterialDidatico())
					|| (getTipoOrigem().equals(TipoOrigemContaReceber.MATRICULA.getValor()) && getContaCorrenteVO().getTipoOrigemMatricula())
					|| (getTipoOrigem().equals(TipoOrigemContaReceber.MENSALIDADE.getValor()) && getContaCorrenteVO().getTipoOrigemMensalidade())
					|| (getTipoOrigem().equals(TipoOrigemContaReceber.NEGOCIACAO.getValor()) && getContaCorrenteVO().getTipoOrigemNegociacao())
					|| (getTipoOrigem().equals(TipoOrigemContaReceber.OUTROS.getValor()) && getContaCorrenteVO().getTipoOrigemOutros())					
						)
				&& Uteis.nrDiasEntreDatas(getDataVencimento(), new Date()) < 0 
				&& !getContaCorrenteVO().getContaCaixa() && getContaCorrenteVO().getRealizarNegociacaoContaReceberVencidaAutomaticamente()
				&& Uteis.nrDiasEntreDatas(new Date(), getDataVencimento()) >= getContaCorrenteVO().getNumeroDiaVencidoNegociarContaReceberAutomaticamente();
	} 
	
	public Boolean getTipoOrigemContaReceberAptaGerarRemessaOnline() {
		return ((getContaCorrenteVO().getGerarRemessaMatriculaAut() && getTipoOrigem().equals(TipoOrigemContaReceber.MATRICULA.valor)) 
				|| (getContaCorrenteVO().getGerarRemessaMateriaDidaticoAut() && getTipoOrigem().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO.valor))
				|| (getContaCorrenteVO().getGerarRemessaParcelasAut() && getTipoOrigem().equals(TipoOrigemContaReceber.MENSALIDADE.valor))								
				|| (getContaCorrenteVO().getGerarRemessaOutrosAut() && getTipoOrigem().equals(TipoOrigemContaReceber.OUTROS.valor)) 
				|| (getContaCorrenteVO().getGerarRemessaRequerimentoAut() && getTipoOrigem().equals(TipoOrigemContaReceber.REQUERIMENTO.valor)) 
				|| (getContaCorrenteVO().getGerarRemessaBibliotecaAut() && getTipoOrigem().equals(TipoOrigemContaReceber.BIBLIOTECA.valor)) 
				|| (getContaCorrenteVO().getGerarRemessaInscProcSeletivoAut() && getTipoOrigem().equals(TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO.valor)) 
				|| (getContaCorrenteVO().getGerarRemessaDevChequeAut() && getTipoOrigem().equals(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.valor)) 
				|| (getContaCorrenteVO().getGerarRemessaConvenioAut() && getTipoOrigem().equals(TipoOrigemContaReceber.BOLSA_CUSTEADA_CONVENIO.valor))  
				|| (getContaCorrenteVO().getGerarRemessaInclusaoReposicaoAut() && getTipoOrigem().equals(TipoOrigemContaReceber.INCLUSAOREPOSICAO.valor)) 
				|| (getContaCorrenteVO().getGerarRemessaContratoReceitaAut() && getTipoOrigem().equals(TipoOrigemContaReceber.CONTRATO_RECEITA.valor))								
				|| (getContaCorrenteVO().getGerarRemessaNegociacaoAut() && getTipoOrigem().equals(TipoOrigemContaReceber.NEGOCIACAO.valor)));
		
	}

	/**
	 * @return the estornandoRecebimento
	 */
	public Boolean getEstornandoRecebimento() {
		if (estornandoRecebimento == null) {
			estornandoRecebimento = false;
		}
		return estornandoRecebimento;
	}

	/**
	 * @param estornandoRecebimento the estornandoRecebimento to set
	 */
	public void setEstornandoRecebimento(Boolean estornandoRecebimento) {
		this.estornandoRecebimento = estornandoRecebimento;
	}

	public Date getDataLimitePrimeiraFaixaDescontos() {
		return dataLimitePrimeiraFaixaDescontos;
	}

	public void setDataLimitePrimeiraFaixaDescontos(Date dataLimitePrimeiraFaixaDescontos) {
		this.dataLimitePrimeiraFaixaDescontos = dataLimitePrimeiraFaixaDescontos;
	}

	public Date getDataLimiteSegundaFaixaDescontos() {
		return dataLimiteSegundaFaixaDescontos;
	}

	public void setDataLimiteSegundaFaixaDescontos(Date dataLimiteSegundaFaixaDescontos) {
		this.dataLimiteSegundaFaixaDescontos = dataLimiteSegundaFaixaDescontos;
	}

	public Date getDataLimiteTerceiraFaixaDescontos() {
		return dataLimiteTerceiraFaixaDescontos;
	}

	public void setDataLimiteTerceiraFaixaDescontos(Date dataLimiteTerceiraFaixaDescontos) {
		this.dataLimiteTerceiraFaixaDescontos = dataLimiteTerceiraFaixaDescontos;
	}

	public Date getDataLimiteQuartaFaixaDescontos() {
		return dataLimiteQuartaFaixaDescontos;
	}

	public void setDataLimiteQuartaFaixaDescontos(Date dataLimiteQuartaFaixaDescontos) {
		this.dataLimiteQuartaFaixaDescontos = dataLimiteQuartaFaixaDescontos;
	}

	public Boolean getCr_recebidooutronossonumero() {
		if (cr_recebidooutronossonumero == null) {
			cr_recebidooutronossonumero = Boolean.FALSE;
		}
		return cr_recebidooutronossonumero;
	}

	public void setCr_recebidooutronossonumero(Boolean cr_recebidooutronossonumero) {
		this.cr_recebidooutronossonumero = cr_recebidooutronossonumero;
	}

	public String getNossoNumeroOriginouBaixa() {
		if (nossoNumeroOriginouBaixa == null) {
			nossoNumeroOriginouBaixa = "";
		}
		return nossoNumeroOriginouBaixa;
	}

	public void setNossoNumeroOriginouBaixa(String nossoNumeroOriginouBaixa) {
		this.nossoNumeroOriginouBaixa = nossoNumeroOriginouBaixa;
	}

	public DepartamentoVO getDepartamentoVO() {
		if (departamentoVO == null) {
			departamentoVO = new DepartamentoVO();
		}
		return departamentoVO;
	}

	public void setDepartamentoVO(DepartamentoVO departamentoVO) {
		this.departamentoVO = departamentoVO;
	}

	public Boolean getBloquearPagamentoCartaoPorOutrosDebitos() {
		if(bloquearPagamentoCartaoPorOutrosDebitos == null) {
			bloquearPagamentoCartaoPorOutrosDebitos =  false;
		}
		return bloquearPagamentoCartaoPorOutrosDebitos;
	}

	public void setBloquearPagamentoCartaoPorOutrosDebitos(Boolean bloquearPagamentoCartaoPorOutrosDebitos) {
		this.bloquearPagamentoCartaoPorOutrosDebitos = bloquearPagamentoCartaoPorOutrosDebitos;
	}
	
	public Boolean getPossuiRemessa() {
		if(possuiRemessa == null) {
			possuiRemessa = Boolean.FALSE;
		}
		return possuiRemessa;
	}

	public void setPossuiRemessa(Boolean possuiRemessa) {
		this.possuiRemessa = possuiRemessa;
	}
	
	 public Double getValorTotalDescontoContaReceberContrato() {
     	Double totalDesconto = this.getValorDescontoAlunoJaCalculado() + 
                 this.getValorDescontoConvenio() + 
                 this.getValorDescontoInstituicao() + 
                 this.getValorDescontoProgressivo() + 
                 this.getValorDescontoRateio();
         return Uteis.arrendondarForcando2CadasDecimais(totalDesconto);
     }

	public Boolean getConsiderarDescontoSemValidadeCalculoIndiceReajuste() {
		if(considerarDescontoSemValidadeCalculoIndiceReajuste == null) {
			considerarDescontoSemValidadeCalculoIndiceReajuste = false;
		}
		return considerarDescontoSemValidadeCalculoIndiceReajuste;
	}

	public void setConsiderarDescontoSemValidadeCalculoIndiceReajuste(Boolean considerarDescontoSemValidadeCalculoIndiceReajuste) {
		this.considerarDescontoSemValidadeCalculoIndiceReajuste = considerarDescontoSemValidadeCalculoIndiceReajuste;
	}

	public Boolean getRealizandoBaixaAutomatica() {
		if (realizandoBaixaAutomatica == null) {
			realizandoBaixaAutomatica = false;
		}
		return realizandoBaixaAutomatica;
	}

	public void setRealizandoBaixaAutomatica(Boolean realizandoBaixaAutomatica) {
		this.realizandoBaixaAutomatica = realizandoBaixaAutomatica;
	}

	public Date getNovaDataVencimento() {
		if(novaDataVencimento == null) {
			novaDataVencimento = new Date();
		}
		return novaDataVencimento;
	}

	public void setNovaDataVencimento(Date novaDataVencimento) {
		this.novaDataVencimento = novaDataVencimento;
	}
	
	public String getValorApresentarDescontoProgressivo1() {
		String descricao = "";
		if (Uteis.isAtributoPreenchido(getDescontoProgressivo().getCodigo())) {
			descricao += getDescontoProgressivo().getPercDescontoLimite1() > 0.0 ? 
				Uteis.getDoubleFormatado(Uteis.arredondar(getDescontoProgressivo().getPercDescontoLimite1(), 2, 0)) + "%" :
				"R$ " + Uteis.getDoubleFormatado(Uteis.arredondar(getDescontoProgressivo().getValorDescontoLimite1(), 2, 0));
		}
		return descricao;
	}

	public String getValorApresentarDescontoProgressivo2() {
		String descricao = "";
		if (Uteis.isAtributoPreenchido(getDescontoProgressivo().getCodigo())) {
			descricao += getDescontoProgressivo().getPercDescontoLimite2() > 0.0 ? 
				Uteis.getDoubleFormatado(Uteis.arredondar(getDescontoProgressivo().getPercDescontoLimite2(), 2, 0)) + "%" :
				"R$ " + Uteis.getDoubleFormatado(Uteis.arredondar(getDescontoProgressivo().getValorDescontoLimite2(), 2, 0));
		}
		return descricao;
	}
	
	public Double getValorPagarCalculadoPrimeiraFaixaDescontos() {
		double valor = 0.0;
		if (Uteis.isAtributoPreenchido(getListaDescontosAplicavesContaReceber())) {
			valor = getListaDescontosAplicavesContaReceber().get(0).executarCalculoValorParaPagamentoDentroDataLimiteDesconto();
		}
		return valor;
	}

	public Double getValorPagarCalculadoSegundaFaixaDescontos() {
		double valor = 0.0;
		if (Uteis.isAtributoPreenchido(getListaDescontosAplicavesContaReceber())
				&& getListaDescontosAplicavesContaReceber().size() > 1) {
			valor = getListaDescontosAplicavesContaReceber().get(1).executarCalculoValorParaPagamentoDentroDataLimiteDesconto();
		}
		return valor;
	}

	public Double getValorPagarCalculadoTerceiraFaixaDescontos() {
		double valor = 0.0;
		if (Uteis.isAtributoPreenchido(getListaDescontosAplicavesContaReceber())
				&& getListaDescontosAplicavesContaReceber().size() > 2) {
			valor = getListaDescontosAplicavesContaReceber().get(2).executarCalculoValorParaPagamentoDentroDataLimiteDesconto();
		}
		return valor;
	}

	public Double getValorPagarCalculadoQuartaFaixaDescontos() {
		double valor = 0.0;
		if (Uteis.isAtributoPreenchido(getListaDescontosAplicavesContaReceber())
				&& getListaDescontosAplicavesContaReceber().size() > 3) {
			valor = getListaDescontosAplicavesContaReceber().get(3).executarCalculoValorParaPagamentoDentroDataLimiteDesconto();
		}
		return valor;
	}
	
	public AgenteNegativacaoCobrancaContaReceberVO getAgenteNegativacaoCobrancaContaReceberVO() {
		if(agenteNegativacaoCobrancaContaReceberVO == null) {
			agenteNegativacaoCobrancaContaReceberVO = new AgenteNegativacaoCobrancaContaReceberVO();
		}
		return agenteNegativacaoCobrancaContaReceberVO;
	}

	public void setAgenteNegativacaoCobrancaContaReceberVO(
			AgenteNegativacaoCobrancaContaReceberVO agenteNegativacaoCobrancaContaReceberVO) {
		this.agenteNegativacaoCobrancaContaReceberVO = agenteNegativacaoCobrancaContaReceberVO;
	}

	private String agenteNegativacao;
	private String agenteCobranca;

	public String getAgenteNegativacao() {
		if(agenteNegativacao == null) {
			agenteNegativacao = ""; 
		}
		return agenteNegativacao;
	}

	public void setAgenteNegativacao(String agenteNegativacao) {
		this.agenteNegativacao = agenteNegativacao;
	}

	public String getAgenteCobranca() {
		if(agenteCobranca == null){
			agenteCobranca = "";
		}
		return agenteCobranca;
	}

	public void setAgenteCobranca(String agenteCobranca) {
		this.agenteCobranca = agenteCobranca;
	}
}
