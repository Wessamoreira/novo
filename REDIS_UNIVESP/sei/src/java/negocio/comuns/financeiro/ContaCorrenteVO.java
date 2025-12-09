package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.SerializationUtils;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.enumerador.AmbienteContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoAutenticacaoRegistroRemessaOnlineEnum;
import negocio.comuns.financeiro.enumerador.TipoContaCorrenteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UteisWebServiceUrl;
import negocio.comuns.utilitarias.dominios.Bancos;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;

/**
 * Reponsável por manter os dados da entidade ContaCorrente. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
@XmlRootElement(name = "contaCorrenteVO")
public class ContaCorrenteVO extends FiltroRelatorioFinanceiroVO {

    private Integer codigo;
    private String numero;
    private Date dataAbertura;
    private Double saldo;
    private Double taxaBoleto;
    private Boolean contaCaixa;
    private TipoContaCorrenteEnum tipoContaCorrenteEnum;
    private Boolean bloquearEmissaoBoleto;
    private String situacao;
    private String variacaoCarteira;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Agencia </code>.
     */
    private AgenciaVO agencia;
//    /**
//     * Atributo responsável por manter o objeto relacionado da classe <code>UnidadeEnsino </code>.
//     */
//    private UnidadeEnsinoVO unidadeEnsino;
    private String digito;
    private String carteira;
    private String convenio;
    private FuncionarioVO funcionarioResponsavel;
    private String codigoCedente;
    private String digitoCodigoCedente;
    private List<UnidadeEnsinoContaCorrenteVO> unidadeEnsinoContaCorrenteVOs;
    private Boolean requerConfirmacaoMovimentacaoFinanceira;
    //Atributos da carteira registrada
    private String mensagemCarteiraRegistrada;
    private Boolean carteiraRegistradaEmissaoBoletoBanco;
	private Boolean carteiraRegistrada;
    private Boolean utilizarRenegociacao;
    private Boolean utilizaDadosInformadosCCparaGeracaoBoleto;
    //Atributos para impressão de boletos
	protected String nome;
	protected String razaoSocial;
	protected String mantenedora;
	protected String endereco;
	protected String completo;
	protected String setor;
	protected String numeroEnd;
	protected String complemento;
	protected CidadeVO cidade;
	protected String CEP;
	protected String CNPJ;
	protected String inscEstadual;
	protected String telComercial1;
	protected String telComercial2;
	protected String telComercial3;
	protected String email;
	protected String site;
	protected String fax;    
	private ControleRemessaMXVO controleRemessaMXVO;
	   	
	protected String codigoTransmissaoRemessa;
	protected String codigoEstacaoRemessa;	
	protected String cnab;    
	protected Boolean utilizaCobrancaPartilhada;    
	protected String codigoReceptor1;    
	protected String codigoReceptor2;    
	protected String codigoReceptor3;    
	protected String codigoReceptor4;    
	protected Double percReceptor1;    
	protected Double percReceptor2;    
	protected Double percReceptor3;    
	protected Double percReceptor4;    
	protected Boolean permiteEnviarBoletoVencido;    
	protected Integer qtdDiasBoletoVencido;
	protected Integer qtdDiasBoletoAVencer;
	public Integer qtdDiasFiltroRemessa;	
	private List<ChequeVO> cheques;
	private String nomeApresentacaoSistema;
	private Boolean gerarRemessaSemDescontoAbatido;
	private Boolean controlarBloqueioEmissaoBoleto;	
	private Boolean bloquearEmissaoBoletoFimDeSemana;	
	private Boolean bloquearEmissaoBoletoFeriado;	
	private String bloquearEmissaoBoletoHoraIni;	
	private String bloquearEmissaoBoletoHoraFim;	
	private String mensagemBloqueioEmissaoBoleto;	
	/*
	 * Campo responsável por controlar se o ao realizar o processamento de arquivo de retorno, o banco já deposita o valor abatendo a taxa bancaria
	 * por boleto, ou se é gerado um saída após o registro de entra na conta corrente.
	 * */
	private Boolean utilizaAbatimentoNoRepasseRemessaBanco;
	private boolean utilizaTaxaCartaoDebito=true;
	private boolean utilizaTaxaCartaoCredito=true;
	private Boolean gerarContaPagarTaxaBancaria;
	private FormaPagamentoVO formaPgtoTaxaBancaria;
	private CategoriaDespesaVO categoriaDespesaTaxaBancaria;
	private Integer qtdDiasBaixaAutTitulo;
	private Boolean remessaBoletoEmitido;

	private Boolean habilitarRegistroRemessaOnline;
	private TipoAutenticacaoRegistroRemessaOnlineEnum tipoAutenticacaoRegistroRemessaOnlineEnum;
	private AmbienteContaCorrenteEnum ambienteContaCorrenteEnum;
	private String tokenIdRegistroRemessaOnline;
	private String tokenKeyRegistroRemessaOnline;
	private String tokenClienteRegistroRemessaOnline; 
	
	private Boolean habilitarRegistroPix;
	private AmbienteContaCorrenteEnum ambienteContaCorrentePix;
	private String tokenIdRegistroPix;
	private String tokenKeyRegistroPix;
	private String chavePix;
	private String chaveAplicacaoDesenvolvedorPix;
	private FormaPagamentoVO formaRecebimentoPadraoPix;
	
	private String senhaCertificado;
	private String caminhoCertificado;
	private ArquivoVO arquivoCertificadoVO;
	private Date dataVencimentoCertificado;
	private String senhaUnidadeCertificadora;
	private String caminhoUnidadeCertificadora;
	
	private ArquivoVO arquivoUnidadeCertificadoraVO;
	private Boolean gerarRemessaMatriculaAut;
	private Boolean gerarRemessaParcelasAut;
	private Boolean gerarRemessaNegociacaoAut;
	private Boolean gerarRemessaOutrosAut;
	private Boolean gerarRemessaRequerimentoAut;
	private Boolean gerarRemessaBibliotecaAut;
	private Boolean gerarRemessaInscProcSeletivoAut;
	private Boolean gerarRemessaDevChequeAut;
	private Boolean gerarRemessaConvenioAut;
	private Boolean gerarRemessaContratoReceitaAut;
	private Boolean gerarRemessaMateriaDidaticoAut;
	private Boolean gerarRemessaInclusaoReposicaoAut;
	private Boolean permitirEmissaoBoletoRemessaOnlineRejeita;
	private String codigoComunicacaoRemessaCP;
	private Boolean realizarNegociacaoContaReceberVencidaAutomaticamente;
	private Boolean permiteEmissaoBoletoVencido;
	private Integer numeroDiaVencidoNegociarContaReceberAutomaticamente;
	private Integer numeroDiaAvancarVencimentoContaReceber;

	private Boolean habilitarProtestoBoleto;
	private Integer qtdDiasProtestoBoleto;
	private String especieTituloBoleto;
	private Boolean permiteGerarRemessaOnlineBoletoVencido;
	private Boolean gerarRemessaBoletoVencidoMatricula;
	private Boolean gerarRemessaBoletoVencidoParcelas;
	private Boolean gerarRemessaBoletoVencidoNegociacao;
	private Boolean gerarRemessaBoletoVencidoOutros;	
	private Boolean gerarRemessaBoletoVencidoBiblioteca;	
	private Boolean gerarRemessaBoletoVencidoDevCheque;
	private Boolean gerarRemessaBoletoVencidoConvenio;
	private Boolean gerarRemessaBoletoVencidoContratoReceita;
	private Boolean gerarRemessaBoletoVencidoMateriaDidatico;
	private Boolean gerarRemessaBoletoVencidoInclusaoReposicao;
	private Integer qtdeDiasVencidoPermitirRemessaOnlineMatricula;
	private Integer qtdeDiasVencidoPermitirRemessaOnlineParcela;
	private Integer qtdeDiasVencidoPermitirRemessaOnlineNegociacao;
	private Integer qtdeDiasVencidoPermitirRemessaOnlineOutros;
	private Integer qtdeDiasVencidoPermitirRemessaOnlineBiblioteca;
	private Integer qtdeDiasVencidoPermitirRemessaOnlineDevCheque;
	private Integer qtdeDiasVencidoPermitirRemessaConvenio;
	private Integer qtdeDiasVencidoPermitirRemessaContratoReceita;
	private Integer qtdeDiasVencidoPermitirRemessaMaterialDidatico;
	private Integer qtdeDiasVencidoPermitirRemessaInclusaoExclusao;
	private String inicialGeracaoNossoNumero;
	private Boolean bloquearEmitirBoletoSemRegistroRemessa;
	private String chaveAutenticacaoClienteRegistroRemessaOnline;
	private String chaveTransacaoClienteRegistroRemessaOnline;
	private Date   dataExpiracaoChaveTransacaoClienteRegistroRemessaOnline;
	private Boolean permiteRecebimentoBoletoVencidoRemessaOnline;
	private Integer numeroDiasLimiteRecebimentoBoletoVencidoRemessaOnline;

	public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ContaCorrente</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public ContaCorrenteVO() {
        super();
    }
    
	public ContaCorrenteVO getClonePersistido() {
		ContaCorrenteVO obj = (ContaCorrenteVO) SerializationUtils.clone(this);
		obj.setNovoObj(false);
		return obj;
	}
    
	public ContaCorrenteVO getClone() {
		ContaCorrenteVO cc = (ContaCorrenteVO) SerializationUtils.clone(this);
		cc.setNovoObj(true);
		cc.setCodigo(0);
		return cc;
	}

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>ContaCorrenteVO</code>. Todos os tipos de consistência de dados são e devem ser implementadas neste método. São validações
     * típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
     */
    public static void validarDados(ContaCorrenteVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getUnidadeEnsinoContaCorrenteVOs().isEmpty()) {
            throw new ConsistirException("Por favor informe ao menos UMA unidade de ensino. (Unidade de Ensino)");
        }
        if (obj.getUnidadeEnsinoContaCorrenteVOs().stream().noneMatch(ueContaCorrente -> ueContaCorrente.getUsarPorDefaultMovimentacaoFinanceira() == Boolean.TRUE)) {
            throw new ConsistirException("Por favor escolha uma unidade de ensino para usar por default na movimentação financeira. (Unidade de Ensino)");
        }
        if (obj.getNumero().equals("")) {
            throw new ConsistirException("O campo NÚMERO (Informações Pessoais) deve ser informado.");
        }
        if (obj.getDataAbertura() == null) {
            throw new ConsistirException("O campo DATA ABERTURA (Conta Corrente) deve ser informado.");
        }
        if (!obj.getContaCaixa()) {
            if ((obj.getAgencia() == null) || (obj.getAgencia().getCodigo().intValue() == 0)) {
                throw new ConsistirException("O campo AGÊNCIA (Conta Corrente) deve ser informado.");
            }
            if (obj.getCarteira().equals("")) {
                throw new ConsistirException("O campo CARTEIRA (Conta Corrente) deve ser informado.");
            }
            if (obj.getConvenio().equals("")) {
                throw new ConsistirException("O campo CONVÊNIO (Conta Corrente) deve ser informado.");
            }
            if (obj.getGerarContaPagarTaxaBancaria().booleanValue()) {
            	if (obj.getFormaPgtoTaxaBancaria().getCodigo().intValue() == 0) {
            		throw new ConsistirException("Como o campo GERAR CONTA PAGAR TAXAS BANCÁRIAS está selecionado o campo FORMA PAGAMENTO PADRÃO CONTA PAGAR TAXA BANCÁRIA passa ser obrigatório!");
            	}
            	if (obj.getCategoriaDespesaTaxaBancaria().getCodigo().intValue() == 0) {
            		throw new ConsistirException("Como o campo GERAR CONTA PAGAR TAXAS BANCÁRIAS está selecionado o campo CATEGORIA DESPESA PADRÃO CONTA PAGAR TAXA BANCÁRIA passa ser obrigatório!");
            	}
            }        
        } else {
            if (obj.getUnidadeEnsinoContaCorrenteVOs().size() > 1) {
                throw new ConsistirException("Uma CONTA CAIXA não pode ter mais de uma unidade de ensino.");
            }
        }
        if ( ((!obj.getNumero().trim().isEmpty() && !obj.getNumero().matches("[0-9]+")) || (!obj.getDigito().trim().isEmpty() && (!obj.getDigito().matches("[0-9]+") && (!obj.getDigito().toLowerCase().equals("x"))))) && !obj.getContaCaixa()) {
        	throw new ConsistirException("O campo CONTA CORRENTE não deve possuir letras quando não for conta caixa.");
        }
        if(!obj.getContaCaixa() && obj.getRealizarNegociacaoContaReceberVencidaAutomaticamente() && obj.getNumeroDiaVencidoNegociarContaReceberAutomaticamente() <= 0 ) {
        	throw new ConsistirException("O campo "+UteisJSF.internacionalizar("prt_ContaCorrente_numeroDiaVencidoNegociarContaReceberAutomaticamente").toUpperCase()+" deve ser maior que 0.");
        }
        if(!obj.getContaCaixa() && obj.getRealizarNegociacaoContaReceberVencidaAutomaticamente()) {
        	obj.setTipoOrigemInscricaoProcessoSeletivo(false);
        	obj.setTipoOrigemRequerimento(false);
        	if(!obj.getTipoOrigemBiblioteca() && !obj.getTipoOrigemBolsaCusteadaConvenio() && !obj.getTipoOrigemContratoReceita() && !obj.getTipoOrigemDevolucaoCheque()
        			&& !obj.getTipoOrigemInclusaoReposicao() && !obj.getTipoOrigemMaterialDidatico() && !obj.getTipoOrigemMatricula() && !obj.getTipoOrigemMensalidade()
        			&& !obj.getTipoOrigemNegociacao() && !obj.getTipoOrigemOutros()) {
        		throw new ConsistirException("Pelo menos uma das ORIGENS DE CONTA A RECEBER deve ser marcado.");
        	}
        }
        if(!obj.getContaCaixa()) {
        	if(obj.getCarteiraRegistrada() && obj.getPermiteEmissaoBoletoVencido()) {
        		obj.setCarteiraRegistradaEmissaoBoletoBanco(true);
        	}
        }
        
        Uteis.checkState(obj.getHabilitarRegistroRemessaOnline() && !obj.getAgencia().getBanco().getNrBanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco())
        		&& obj.getTipoAutenticacaoRegistroRemessaOnlineEnum().isToken()
        		&& !Uteis.isAtributoPreenchido(obj.getTokenIdRegistroRemessaOnline()), "O campo Token ID (Conta Corrente) deve ser informado.");
        
        Uteis.checkState(obj.getHabilitarRegistroRemessaOnline() && !obj.getAgencia().getBanco().getNrBanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco())
        		&& obj.getTipoAutenticacaoRegistroRemessaOnlineEnum().isToken()
        		&& !Uteis.isAtributoPreenchido(obj.getTokenKeyRegistroRemessaOnline()), "O campo Token Key (Conta Corrente) deve ser informado.");
       
        Uteis.checkState(obj.getHabilitarRegistroRemessaOnline() && !obj.getAgencia().getBanco().getNrBanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco())
        		&& obj.getTipoAutenticacaoRegistroRemessaOnlineEnum().isToken()
        		&& !Uteis.isAtributoPreenchido(obj.getTokenClienteRegistroRemessaOnline()), "O campo Token Id Cliente (Conta Corrente) deve ser informado.");
        
        Uteis.checkState(obj.getHabilitarRegistroRemessaOnline() && !obj.getAgencia().getBanco().getNrBanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco())
        		&& obj.getTipoAutenticacaoRegistroRemessaOnlineEnum().isChaveAutenticacao()
        		&& !Uteis.isAtributoPreenchido(obj.getChaveAutenticacaoClienteRegistroRemessaOnline()), "O campo Chave Autenticacão Cliente  (Conta Corrente) deve ser informado.");
        
        validarDadosContaCorrentePix(obj);
//        if (!obj.getContaCaixa()) {
//        	//Validar dados Configuração Banco Carteira Sem registro
//        	if (!obj.getCarteiraRegistrada()) {
//        		//Banco do Brasil
//        		if (obj.getAgencia().getBanco().getNrBanco().equals(Bancos.BANCO_DO_BRASIL.getNumeroBanco())) {
////        			if (obj.getNumero().equals("") || obj.getNumero().trim().length() != 5) {
////        				throw new ConsistirException("Para a geração correta do boleto bancário(Banco do Brasil), é preciso que informe a (CONTA CORRENTE/CAIXA) com 5 dígitos.");
////        			}
////        			if (obj.getDigito().equals("") || obj.getDigito().trim().length() != 1) {
////        				throw new ConsistirException("Para a geração correta do boleto bancário(Banco do Brasil), é preciso que informe o (DÍGITO DA CONTA CORRENTE) com 1 dígito.");
////        			}
//        			if (obj.getCarteira().equals("") || obj.getCarteira().trim().length() != 2) {
//        				throw new ConsistirException("Para a geração correta do boleto bancário(Banco do Brasil), é preciso que informe a (CARTEIRA) com 2 dígitos.");
//        			}
//        			if (obj.getConvenio().equals("") || obj.getConvenio().trim().length() != 6) {
//        				throw new ConsistirException("Para a geração correta do boleto bancário(Banco do Brasil), é preciso que informe o (CONVÊNIO) com 6 dígitos.");
//        			}
//        		}
//        		if (obj.getAgencia().getBanco().getNrBanco().equals(Bancos.ITAU.getNumeroBanco())) {
////        			if (obj.getNumero().equals("") || obj.getNumero().trim().length() != 5) {
////        				throw new ConsistirException("Para a geração correta do boleto bancário(Itaú), é preciso que informe a (CONTA CORRENTE/CAIXA) com 5 dígitos.");
////        			}
////        			if (obj.getDigito().equals("") || obj.getDigito().trim().length() != 1) {
////        				throw new ConsistirException("Para a geração correta do boleto bancário(Itaú), é preciso que informe o (DÍGITO DA CONTA CORRENTE) com 1 dígito.");
////        			}
//        			if (obj.getCarteira().equals("") || obj.getCarteira().trim().length() != 3) {
//        				throw new ConsistirException("Para a geração correta do boleto bancário(Itaú), é preciso que informe a (CARTEIRA) com 3 dígitos.");
//        			}
//        			if (obj.getConvenio().equals("") || obj.getConvenio().trim().length() != 9) {
//        				throw new ConsistirException("Para a geração correta do boleto bancário(Itaú), é preciso que informe o (CONVÊNIO) com 9 dígitos.");
//        			}
//        		}
//        		if (obj.getAgencia().getBanco().getNrBanco().equals(Bancos.BRADESCO.getNumeroBanco())) {
////        			if (obj.getNumero().equals("") || obj.getNumero().trim().length() != 7) {
////        				throw new ConsistirException("Para a geração correta do boleto bancário(Bradesco), é preciso que informe a (CONTA CORRENTE/CAIXA) com 7 dígitos.");
////        			}
////        			if (obj.getDigito().equals("") || obj.getDigito().trim().length() != 1) {
////        				throw new ConsistirException("Para a geração correta do boleto bancário(Bradesco), é preciso que informe o (DÍGITO DA CONTA CORRENTE) com 1 dígito.");
////        			}
//        			if (obj.getCarteira().equals("") || obj.getCarteira().trim().length() != 2) {
//        				throw new ConsistirException("Para a geração correta do boleto bancário(Bradesco), é preciso que informe a (CARTEIRA) com 2 dígitos.");
//        			}
//        			if (obj.getConvenio().equals("") || obj.getConvenio().trim().length() != 7) {
//        				throw new ConsistirException("Para a geração correta do boleto bancário(Bradesco), é preciso que informe o (CONVÊNIO) com 7 dígitos.");
//        			}
//        		}
//        		if (obj.getAgencia().getBanco().getNrBanco().equals(Bancos.HSBC.getNumeroBanco())) {
////        			if (obj.getNumero().equals("") || obj.getNumero().trim().length() != 5) {
////        				throw new ConsistirException("Para a geração correta do boleto bancário(HSBC), é preciso que informe a (CONTA CORRENTE/CAIXA) com 5 dígitos.");
////        			}
////        			if (obj.getDigito().equals("") || obj.getDigito().trim().length() != 2) {
////        				throw new ConsistirException("Para a geração correta do boleto bancário(HSBC), é preciso que informe o (DÍGITO DA CONTA CORRENTE) com 2 dígitos.");
////        			}
//        			if (obj.getCarteira().equals("") || obj.getCarteira().trim().length() != 1) {
//        				throw new ConsistirException("Para a geração correta do boleto bancário(HSBC), é preciso que informe a (CARTEIRA) com 1 dígito.");
//        			}
//        			if (obj.getConvenio().equals("") || obj.getConvenio().trim().length() != 7) {
//        				throw new ConsistirException("Para a geração correta do boleto bancário(HSBC), é preciso que informe o (CONVÊNIO) com 7 dígitos.");
//        			}
//        		}
//        		if (obj.getAgencia().getBanco().getNrBanco().equals(Bancos.SANTANDER.getNumeroBanco())) {
////        			if (obj.getNumero().equals("") || obj.getNumero().trim().length() != 8) {
////        				throw new ConsistirException("Para a geração correta do boleto bancário(Santander), é preciso que informe a (CONTA CORRENTE/CAIXA) com 8 dígitos.");
////        			}
////        			if (obj.getDigito().equals("") || obj.getDigito().trim().length() != 1) {
////        				throw new ConsistirException("Para a geração correta do boleto bancário(Santander), é preciso que informe o (DÍGITO DA CONTA CORRENTE) com 1 dígito.");
////        			}
//        			if (obj.getCarteira().equals("") || obj.getCarteira().trim().length() != 3) {
//        				throw new ConsistirException("Para a geração correta do boleto bancário(Santander), é preciso que informe a (CARTEIRA) com 3 dígitos.");
//        			}
//        			if (obj.getConvenio().equals("") || obj.getConvenio().trim().length() != 7) {
//        				throw new ConsistirException("Para a geração correta do boleto bancário(Santander), é preciso que informe o (CONVÊNIO) com 7 dígitos.");
//        			}
//        		}
//        		if (obj.getAgencia().getBanco().getNrBanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco())) {
////        			if (obj.getNumero().equals("") || obj.getNumero().trim().length() != 3) {
////        				throw new ConsistirException("Para a geração correta do boleto bancário(Caixa Econômica), é preciso que informe a (CONTA CORRENTE/CAIXA) com 3 dígitos.");
////        			}
////        			if (obj.getDigito().equals("") || obj.getDigito().trim().length() != 1) {
////        				throw new ConsistirException("Para a geração correta do boleto bancário(Caixa Econômica), é preciso que informe o (DÍGITO DA CONTA CORRENTE) com 1 dígito.");
////        			}
//        			if (obj.getCarteira().equals("") || obj.getCarteira().trim().length() != 2) {
//        				throw new ConsistirException("Para a geração correta do boleto bancário(Caixa Econômica), é preciso que informe a (CARTEIRA) com 2 dígitos.");
//        			}
//        			if (obj.getConvenio().equals("") || obj.getConvenio().trim().length() != 6) {
//        				throw new ConsistirException("Para a geração correta do boleto bancário(Caixa Econômica), é preciso que informe o (CONVÊNIO) com 6 dígitos.");
//        			}
//        			if (obj.getCodigoCedente().equals("") || obj.getCodigoCedente().trim().length() != 6) {
//        				throw new ConsistirException("Para a geração correta do boleto bancário(Caixa Econômica), é preciso que informe o (CÓDIGO CEDENTE) com 6 dígitos.");
//        			}
//        		}
//        	}
//        }
        if(!obj.getHabilitarRegistroRemessaOnline()) {
        	obj.setGerarRemessaMatriculaAut(false);
        	obj.setGerarRemessaParcelasAut(false);
        	obj.setGerarRemessaNegociacaoAut(false);
        	obj.setGerarRemessaContratoReceitaAut(false);
        	obj.setGerarRemessaMateriaDidaticoAut(false);
        	obj.setGerarRemessaBibliotecaAut(false);
        	obj.setGerarRemessaOutrosAut(false);
        	obj.setGerarRemessaRequerimentoAut(false);
        	obj.setGerarRemessaConvenioAut(false);
        	obj.setGerarRemessaDevChequeAut(false);
        	obj.setGerarRemessaInclusaoReposicaoAut(false);
        	obj.setGerarRemessaInscProcSeletivoAut(false);
        }
        if(!obj.getGerarRemessaMatriculaAut() || !obj.getPermiteGerarRemessaOnlineBoletoVencido()) {
        	obj.setGerarRemessaBoletoVencidoMatricula(false);
        	obj.setQtdeDiasVencidoPermitirRemessaOnlineMatricula(0);
        }
        if(!obj.getGerarRemessaNegociacaoAut() || !obj.getPermiteGerarRemessaOnlineBoletoVencido()) {
        	obj.setGerarRemessaBoletoVencidoNegociacao(false);
        	obj.setQtdeDiasVencidoPermitirRemessaOnlineNegociacao(0);
        }
        if(!obj.getGerarRemessaParcelasAut() || !obj.getPermiteGerarRemessaOnlineBoletoVencido()) {
        	obj.setGerarRemessaBoletoVencidoParcelas(false);
        	obj.setQtdeDiasVencidoPermitirRemessaOnlineParcela(0);
        }
        if(!obj.getGerarRemessaContratoReceitaAut() || !obj.getPermiteGerarRemessaOnlineBoletoVencido()) {
        	obj.setGerarRemessaBoletoVencidoContratoReceita(false);
        	obj.setQtdeDiasVencidoPermitirRemessaContratoReceita(0);
        }
        if(!obj.getGerarRemessaConvenioAut() || !obj.getPermiteGerarRemessaOnlineBoletoVencido()) {
        	obj.setGerarRemessaBoletoVencidoConvenio(false);
        	obj.setQtdeDiasVencidoPermitirRemessaConvenio(0);
        }
        if(!obj.getGerarRemessaInclusaoReposicaoAut() || !obj.getPermiteGerarRemessaOnlineBoletoVencido()) {
        	obj.setGerarRemessaBoletoVencidoInclusaoReposicao(false);
        	obj.setQtdeDiasVencidoPermitirRemessaInclusaoExclusao(0);
        }
        if(!obj.getGerarRemessaBibliotecaAut() || !obj.getPermiteGerarRemessaOnlineBoletoVencido()) {
        	obj.setGerarRemessaBoletoVencidoBiblioteca(false);
        	obj.setQtdeDiasVencidoPermitirRemessaOnlineBiblioteca(0);
        }
        if(!obj.getGerarRemessaOutrosAut() || !obj.getPermiteGerarRemessaOnlineBoletoVencido()) {
        	obj.setGerarRemessaBoletoVencidoOutros(false);
        	obj.setQtdeDiasVencidoPermitirRemessaOnlineOutros(0);
        }
        if(!obj.getGerarRemessaDevChequeAut() || !obj.getPermiteGerarRemessaOnlineBoletoVencido()) {
        	obj.setGerarRemessaBoletoVencidoDevCheque(false);
        	obj.setQtdeDiasVencidoPermitirRemessaOnlineDevCheque(0);
        }
        if(!obj.getGerarRemessaMateriaDidaticoAut() || !obj.getPermiteGerarRemessaOnlineBoletoVencido()) {
        	obj.setGerarRemessaBoletoVencidoMateriaDidatico(false);
        	obj.setQtdeDiasVencidoPermitirRemessaMaterialDidatico(0);
        }
    }

    public static void validarDadosContaCorrentePix(ContaCorrenteVO obj) {
		Uteis.checkState(obj.getHabilitarRegistroPix() && !Uteis.isAtributoPreenchido(obj.getAgencia().getBanco().isIntegracaoPix()), "O banco informado para a conta corrente em questão não possui integração com Pix. Por favor verificar o Cadastro do Banco.");
		Uteis.checkState(obj.getHabilitarRegistroPix() && !Uteis.isAtributoPreenchido(obj.getAgencia().getBanco().getUrlApiPixAutenticacao()), "O campo Url Api Pix Autenticação no cadastro do Banco escolhido para essa conta corrente deve ser informado.");
		Uteis.checkState(obj.getHabilitarRegistroPix() && !Uteis.isAtributoPreenchido(obj.getAgencia().getBanco().getUrlApiPixProducao()), "O campo Url Api Pix Produção no cadastro do Banco escolhido para essa conta corrente deve ser informado.");
		Uteis.checkState(obj.getHabilitarRegistroPix() && !Uteis.isAtributoPreenchido(obj.getAgencia().getBanco().getUrlApiPixHomologacao()), "O campo Url Api Pix Homologação no cadastro do Banco escolhido para essa conta corrente deve ser informado.");
		Uteis.checkState(obj.getHabilitarRegistroPix() && !Uteis.isAtributoPreenchido(obj.getTokenIdRegistroPix()), "O campo Token Id Pix  (Conta Corrente) deve ser informado.");
        Uteis.checkState(obj.getHabilitarRegistroPix() && !Uteis.isAtributoPreenchido(obj.getTokenKeyRegistroPix()), "O campo Token Secret Pix  (Conta Corrente) deve ser informado.");
        Uteis.checkState(obj.getHabilitarRegistroPix() && obj.isApresentarChaveAplicacaoDesenvolvedorPix() && !Uteis.isAtributoPreenchido(obj.getChaveAplicacaoDesenvolvedorPix()), "O campo Chave de Aplicação do Desenvolvedor Pix  (Conta Corrente) deve ser informado.");
        Uteis.checkState(obj.getHabilitarRegistroPix() && !Uteis.isAtributoPreenchido(obj.getChavePix()), "O campo Chave Pix  (Conta Corrente) deve ser informado.");
        Uteis.checkState(obj.getHabilitarRegistroPix() && !Uteis.isAtributoPreenchido(obj.getFormaRecebimentoPadraoPix()), "O campo Forma Recebimento Padrão Pix  (Conta Corrente) deve ser informado.");
	}

    public String getBancoAgenciaContaCorrente() {
    	if(Uteis.isAtributoPreenchido(this.getNomeApresentacaoSistema())){
    		return this.getNomeApresentacaoSistema();
    	}
        String nome = "";
        if (getContaCaixa()) {
            nome = getNumero() + "-" + getDigito();
        } else {
            String str = getAgencia().getBanco().getNome();
            if (str != null && !str.equals("")) {
                nome = "Banco: " + str;
            }
            str = getAgencia().getNumeroAgencia() + "-" + getAgencia().getDigito();
            if (str != null && !str.equals("-")) {
                nome += " AG: " + str;
            }
            str = getNumero();
            if (str != null && !str.equals("")) {
                nome += " CC: " + str;
            }
            str = getCarteira();
            if (str != null && !str.equals("")) {
            	nome += " Carteira: " + str;
            }
        }
        return nome;
    }

    public String getDigito() {
        if (digito == null) {
            digito = "";
        }
        return digito;
    }

    public void setDigito(String digito) {
        this.digito = digito;
    }

//    /**
//     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com ( <code>ContaCorrente</code>).
//     */
//    public UnidadeEnsinoVO getUnidadeEnsino() {
//        if (unidadeEnsino == null) {
//            unidadeEnsino = new UnidadeEnsinoVO();
//        }
//        return (unidadeEnsino);
//    }
//
//    /**
//     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com ( <code>ContaCorrente</code>).
//     */
//    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
//        this.unidadeEnsino = obj;
//    }
    /**
     * Retorna o objeto da classe <code>Agencia</code> relacionado com ( <code>ContaCorrente</code>).
     */
    @XmlElement(name = "agenciaVO")
    public AgenciaVO getAgencia() {
        if (agencia == null) {
            agencia = new AgenciaVO();
        }
        return (agencia);
    }

    /**
     * Define o objeto da classe <code>Agencia</code> relacionado com ( <code>ContaCorrente</code>).
     */
    public void setAgencia(AgenciaVO obj) {
        this.agencia = obj;
    }

    public Boolean getContaCaixa() {
        /*if (contaCaixa == null) {
            contaCaixa = Boolean.FALSE;
        }
        return (contaCaixa);*/
    	return getTipoContaCorrenteEnum().isCaixa();
    }

    // public Boolean isContaCaixa() {
    // return (contaCaixa);
    // }
    public void setContaCaixa(Boolean contaCaixa) {
        this.contaCaixa = contaCaixa;
    }

    public Double getSaldo() {
        if (saldo == null) {
            saldo = 0.0;
        }
        return (saldo);
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Date getDataAbertura() {
        if (dataAbertura == null) {
            dataAbertura = new Date();
        }
        return (dataAbertura);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
     */
    public String getDataAbertura_Apresentar() {
        return (Uteis.getData(getDataAbertura()));
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public String getNumero() {
        if (numero == null) {
            numero = "";
        }
        return (numero);
    }

    public void setNumero(String numero) {
        this.numero = numero;
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

    public String getDescricaoCompletaConta() {
        try {
        	if(Uteis.isAtributoPreenchido(getNomeApresentacaoSistema()) && !getNomeApresentacaoSistema().equals("-")){
        		return getNomeApresentacaoSistema();
        	} 
             if (this.getContaCaixa()) {
            	 if(this.getUnidadeEnsinoContaCorrenteVOs().isEmpty()) {
            		 return "Caixa: " + this.getNumero() + "-" + this.getDigito();
            	 }
                  return "Caixa: " + this.getNumero() + "-" + this.getDigito() + " - " + this.getUnidadeEnsinoContaCorrenteVOs().get(0).getUnidadeEnsino().getNome();
              } else {
                  return this.getAgencia().getBanco().getNome() + " - " + this.getAgencia().getNumeroAgencia() + " Conta: " + this.getNumero() + "-" + this.getDigito() + " Carteira: " + this.getCarteira();
              }	
        } catch (Exception e) {
            return this.getNumero() + " - " + this.getDigito();
        }
    }

    public String getTipoConta() {
        return (getTipoContaCorrenteEnum().isCaixa() ? "Ct Caixa: " : getTipoContaCorrenteEnum().isCorrente() ? "Ct Corrente: " : "Ct Aplicação: " );
    }

    public String getDescricaoParaComboBox() {
    	if(Uteis.isAtributoPreenchido(getNomeApresentacaoSistema()) && !getNomeApresentacaoSistema().equals("-")){
    		return getNomeApresentacaoSistema();
    	}    
    	if (!getUnidadeEnsinoContaCorrenteVOs().isEmpty()) {
    		if (!getUnidadeEnsinoContaCorrenteVOs().get(0).getUnidadeEnsino().getNome().equals("")) {
    			return getTipoConta() + getNumero().toString() + "-" + getDigito() + " - " + getCarteira()  + " - " + getUnidadeEnsinoContaCorrenteVOs().get(0).getUnidadeEnsino().getNome();
    		}
    	}
    	return getTipoConta() + getNumero().toString() + "-" + getDigito();
    }

    public String getDescricaoContaCaixa() {
        if (this.getContaCaixa()) {
            return "Caixa";
        } else {
            return "";
        }
    }

    public String getCarteira() {
        if (carteira == null) {
            carteira = "";
        }
        return carteira;
    }

    public void setCarteira(String carteira) {
        this.carteira = carteira;
    }

    /**
     * @return the convenio
     */
    @XmlElement(name = "convenio")
    public String getConvenio() {
        if (convenio == null) {
            convenio = "";
        }
        return convenio;
    }

    /**
     * @param convenio
     *            the convenio to set
     */
    public void setConvenio(String convenio) {
        this.convenio = convenio;
    }

    public FuncionarioVO getFuncionarioResponsavel() {
        if (funcionarioResponsavel == null) {
            funcionarioResponsavel = new FuncionarioVO();
        }
        return funcionarioResponsavel;
    }

    public void setFuncionarioResponsavel(FuncionarioVO funcionarioResponsavel) {
        this.funcionarioResponsavel = funcionarioResponsavel;
    }

    public void setCodigoCedente(String codigoCedente) {
        this.codigoCedente = codigoCedente;
    }

    @XmlElement(name = "codigoCedente")
    public String getCodigoCedente() {
        if (codigoCedente == null) {
            codigoCedente = "";
        }
        return codigoCedente;
    }

    public void setUnidadeEnsinoContaCorrenteVOs(List<UnidadeEnsinoContaCorrenteVO> unidadeEnsinoContaCorrenteVOs) {
        this.unidadeEnsinoContaCorrenteVOs = unidadeEnsinoContaCorrenteVOs;
    }

    public List<UnidadeEnsinoContaCorrenteVO> getUnidadeEnsinoContaCorrenteVOs() {
        if (unidadeEnsinoContaCorrenteVOs == null) {
            unidadeEnsinoContaCorrenteVOs = new ArrayList<UnidadeEnsinoContaCorrenteVO>(0);
        }
        return unidadeEnsinoContaCorrenteVOs;
    }

    public String getNumeroDigito() {
        return getNumero() + " - " + getDigito();
    }
    
    public String getNumeroDigitoSemEspaco() {
        return getNumero() + "" + getDigito();
    }

    public Boolean getRequerConfirmacaoMovimentacaoFinanceira() {
        if (requerConfirmacaoMovimentacaoFinanceira == null) {
            requerConfirmacaoMovimentacaoFinanceira = false;
        }
        return requerConfirmacaoMovimentacaoFinanceira;
    }

    public void setRequerConfirmacaoMovimentacaoFinanceira(Boolean requerConfirmacaoMovimentacaoFinanceira) {
        this.requerConfirmacaoMovimentacaoFinanceira = requerConfirmacaoMovimentacaoFinanceira;
    }

    @XmlElement(name = "mensagemCarteiraRegistrada")
    public String getMensagemCarteiraRegistrada() {
        if (mensagemCarteiraRegistrada == null) {
            mensagemCarteiraRegistrada = 
                "<p>Prezado Usu&aacute;rio,<br /><br /> link: <a href=\" \" target=\"_blank\"></a> <br /><br />Departamento de TI</p>";
        }
        return mensagemCarteiraRegistrada;
    }

    public void setMensagemCarteiraRegistrada(String mensagemCarteiraRegistrada) {
        this.mensagemCarteiraRegistrada = mensagemCarteiraRegistrada;
    }

    public Boolean getCarteiraRegistrada() {
        if (carteiraRegistrada == null) {
            carteiraRegistrada = Boolean.FALSE;
        }
        return carteiraRegistrada;
    }

    public void setCarteiraRegistrada(Boolean carteiraRegistrada) {
        this.carteiraRegistrada = carteiraRegistrada;
    }

	public Boolean getUtilizarRenegociacao() {
		if (utilizarRenegociacao == null) {
			utilizarRenegociacao = Boolean.TRUE;
		}
		return utilizarRenegociacao;
	}

	public void setUtilizarRenegociacao(Boolean utilizarRenegociacao) {
		this.utilizarRenegociacao = utilizarRenegociacao;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "AT";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

    
    public Double getTaxaBoleto() {
        if(taxaBoleto == null){
            taxaBoleto = 0.0;
        }
        return taxaBoleto;
    }

    
    public void setTaxaBoleto(Double taxaBoleto) {
        this.taxaBoleto = taxaBoleto;
    }

	public Boolean getUtilizaDadosInformadosCCparaGeracaoBoleto() {
		if (utilizaDadosInformadosCCparaGeracaoBoleto == null) {
			utilizaDadosInformadosCCparaGeracaoBoleto = Boolean.FALSE;
		}
		return utilizaDadosInformadosCCparaGeracaoBoleto;
	}

	public void setUtilizaDadosInformadosCCparaGeracaoBoleto(Boolean utilizaDadosInformadosCCparaGeracaoBoleto) {
		this.utilizaDadosInformadosCCparaGeracaoBoleto = utilizaDadosInformadosCCparaGeracaoBoleto;
	}
	

	public String getFax() {
		if (fax == null) {
			fax = "";
		}
		return (fax);
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getSite() {
		if (site == null) {
			site = "";
		}
		return (site);
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return (email);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelComercial3() {
		if (telComercial3 == null) {
			telComercial3 = "";
		}
		return (telComercial3);
	}

	public void setTelComercial3(String telComercial3) {
		this.telComercial3 = telComercial3;
	}

	public String getTelComercial2() {
		if (telComercial2 == null) {
			telComercial2 = "";
		}
		return (telComercial2);
	}

	public void setTelComercial2(String telComercial2) {
		this.telComercial2 = telComercial2;
	}

	public String getTelComercial1() {
		if (telComercial1 == null) {
			telComercial1 = "";
		}
		return (telComercial1);
	}

	public void setTelComercial1(String telComercial1) {
		this.telComercial1 = telComercial1;
	}

	public String getInscEstadual() {
		if (inscEstadual == null) {
			inscEstadual = "";
		}
		return (inscEstadual);
	}

	public void setInscEstadual(String inscEstadual) {
		this.inscEstadual = inscEstadual;
	}

	public String getCNPJ() {
		if (CNPJ == null) {
			CNPJ = "";
		}
		return (CNPJ);
	}

	public void setCNPJ(String CNPJ) {
		this.CNPJ = CNPJ;
	}

	public String getCEP() {
		if (CEP == null) {
			CEP = "";
		}
		return (CEP);
	}

	public void setCEP(String CEP) {
		this.CEP = CEP;
	}

	public CidadeVO getCidade() {
		if (cidade == null) {
			cidade = new CidadeVO();
		}
		return (cidade);
	}

	public void setCidade(CidadeVO cidade) {
		this.cidade = cidade;
	}

	public String getComplemento() {
		if (complemento == null) {
			complemento = "";
		}
		return (complemento);
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getNumeroEnd() {
		if (numeroEnd == null) {
			numeroEnd = "";
		}
		return (numeroEnd);
	}

	public void setNumeroEnd(String numeroEnd) {
		this.numeroEnd = numeroEnd;
	}

	public String getSetor() {
		if (setor == null) {
			setor = "";
		}
		return (setor);
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public String getEndereco() {
		if (endereco == null) {
			endereco = "";
		}
		return (endereco);
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getcompleto() {
		if (completo == null) {
			completo = "";
		}
		return completo;
	}

	public void setCompleto(String completo) {
		this.completo = completo;
	}

	public String getRazaoSocial() {
		if (razaoSocial == null) {
			razaoSocial = "";
		}
		return (razaoSocial);
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return (nome);
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getMantenedora() {
		if (mantenedora == null) {
			mantenedora = "";
		}	
		return mantenedora;
	}

	public void setMantenedora(String mantenedora) {
		this.mantenedora = mantenedora;
	}

	public Boolean getBloquearEmissaoBoleto() {
		if (bloquearEmissaoBoleto == null) {
			bloquearEmissaoBoleto = Boolean.FALSE;
		}
		return bloquearEmissaoBoleto;
	}

	public void setBloquearEmissaoBoleto(Boolean bloquearEmissaoBoleto) {
		this.bloquearEmissaoBoleto = bloquearEmissaoBoleto;
	}

	public String getDigitoCodigoCedente() {
		if (digitoCodigoCedente == null) {
			digitoCodigoCedente = "";
		}
		return digitoCodigoCedente;
	}

	public void setDigitoCodigoCedente(String digitoCodigoCedente) {
		this.digitoCodigoCedente = digitoCodigoCedente;
	}

	public ControleRemessaMXVO getControleRemessaMXVO() {
		if (controleRemessaMXVO == null) {
			controleRemessaMXVO = new ControleRemessaMXVO();
		}
		return controleRemessaMXVO;
	}

	public void setControleRemessaMXVO(ControleRemessaMXVO controleRemessaMXVO) {
		this.controleRemessaMXVO = controleRemessaMXVO;
	}
	
	public String getVariacaoCarteira() {
		return variacaoCarteira;
	}

	public void setVariacaoCarteira(String variacaoCarteira) {
		this.variacaoCarteira = variacaoCarteira;
	}

	public String getCodigoTransmissaoRemessa() {
		if (codigoTransmissaoRemessa == null) {
			codigoTransmissaoRemessa = "";
		}
		return codigoTransmissaoRemessa;
	}

	public void setCodigoTransmissaoRemessa(String codigoTransmissaoRemessa) {
		this.codigoTransmissaoRemessa = codigoTransmissaoRemessa;
	}

	public String getCnab() {
		if (cnab == null || cnab.trim().isEmpty()) {
			cnab = "CNAB240";
		}
		return cnab;
	}

	public void setCnab(String cnab) {
		this.cnab = cnab;
	}
	

	public Boolean getUtilizaCobrancaPartilhada() {
		if (utilizaCobrancaPartilhada == null) {
			utilizaCobrancaPartilhada = Boolean.FALSE;
		}
		return utilizaCobrancaPartilhada;
	}

	public void setUtilizaCobrancaPartilhada(Boolean utilizaCobrancaPartilhada) {
		this.utilizaCobrancaPartilhada = utilizaCobrancaPartilhada;
	}

	public String getCodigoReceptor1() {
		if (codigoReceptor1 == null) {
			codigoReceptor1 = "";
		}
		return codigoReceptor1;
	}

	public void setCodigoReceptor1(String codigoReceptor1) {
		this.codigoReceptor1 = codigoReceptor1;
	}

	public String getCodigoReceptor2() {
		if (codigoReceptor2 == null) {
			codigoReceptor2 = "";
		}
		return codigoReceptor2;
	}

	public void setCodigoReceptor2(String codigoReceptor2) {
		this.codigoReceptor2 = codigoReceptor2;
	}

	public String getCodigoReceptor3() {
		if (codigoReceptor3 == null) {
			codigoReceptor3 = "";
		}
		return codigoReceptor3;
	}

	public void setCodigoReceptor3(String codigoReceptor3) {
		this.codigoReceptor3 = codigoReceptor3;
	}

	public String getCodigoReceptor4() {
		if (codigoReceptor4 == null) {
			codigoReceptor4 = "";
		}
		return codigoReceptor4;
	}

	public void setCodigoReceptor4(String codigoReceptor4) {
		this.codigoReceptor4 = codigoReceptor4;
	}

	public Double getPercReceptor1() {
		if (percReceptor1 == null) {
			percReceptor1 = new Double(0);
		}
		return percReceptor1;
	}

	public void setPercReceptor1(Double percReceptor1) {
		this.percReceptor1 = percReceptor1;
	}

	public Double getPercReceptor2() {
		if (percReceptor2 == null) {
			percReceptor2 = new Double(0);
		}
		return percReceptor2;
	}

	public void setPercReceptor2(Double percReceptor2) {
		this.percReceptor2 = percReceptor2;
	}

	public Double getPercReceptor3() {
		if (percReceptor3 == null) {
			percReceptor3 = new Double(0);
		}
		return percReceptor3;
	}

	public void setPercReceptor3(Double percReceptor3) {
		this.percReceptor3 = percReceptor3;
	}

	public Double getPercReceptor4() {
		if (percReceptor4 == null) {
			percReceptor4 = new Double(0);
		}
		return percReceptor4;
	}

	public void setPercReceptor4(Double percReceptor4) {
		this.percReceptor4 = percReceptor4;
	}
	
	public Boolean getPermiteEnviarBoletoVencido() {
		if (permiteEnviarBoletoVencido == null) {
			permiteEnviarBoletoVencido = Boolean.FALSE;
		}
		return permiteEnviarBoletoVencido;
	}

	public void setPermiteEnviarBoletoVencido(Boolean permiteEnviarBoletoVencido) {
		this.permiteEnviarBoletoVencido = permiteEnviarBoletoVencido;
	}

	public Integer getQtdDiasBoletoVencido() {
		if (qtdDiasBoletoVencido == null) {
			qtdDiasBoletoVencido = 0;
		}
		return qtdDiasBoletoVencido;
	}

	public void setQtdDiasBoletoVencido(Integer qtdDiasBoletoVencido) {
		this.qtdDiasBoletoVencido = qtdDiasBoletoVencido;
	}

	public Integer getQtdDiasBoletoAVencer() {
		if (qtdDiasBoletoAVencer == null) {
			qtdDiasBoletoAVencer = 0;
		}
		return qtdDiasBoletoAVencer;
	}

	public void setQtdDiasBoletoAVencer(Integer qtdDiasBoletoAVencer) {
		this.qtdDiasBoletoAVencer = qtdDiasBoletoAVencer;
	}
	
	public List<ChequeVO> getCheques() {
		 if (cheques == null) {
			 cheques = new ArrayList<ChequeVO>(0);
	        }
		return cheques;
	}

	public void setCheques(List<ChequeVO> cheques) {
		this.cheques = cheques;
	}
	
	public Integer getQtdDiasFiltroRemessa() {
		if (qtdDiasFiltroRemessa == null) {
			qtdDiasFiltroRemessa = 30;
		}
		return qtdDiasFiltroRemessa;
	}

	public void setQtdDiasFiltroRemessa(Integer qtdDiasFiltroRemessa) {
		this.qtdDiasFiltroRemessa = qtdDiasFiltroRemessa;
	}

    public Boolean getCarteiraRegistradaEmissaoBoletoBanco() {
		if (carteiraRegistradaEmissaoBoletoBanco == null) {
			carteiraRegistradaEmissaoBoletoBanco = Boolean.FALSE;
		}
		return carteiraRegistradaEmissaoBoletoBanco;
	}

	public void setCarteiraRegistradaEmissaoBoletoBanco(Boolean carteiraRegistradaEmissaoBoletoBanco) {
		this.carteiraRegistradaEmissaoBoletoBanco = carteiraRegistradaEmissaoBoletoBanco;
	}
	
	public String getNomeApresentacaoSistema() {
		if (nomeApresentacaoSistema == null) {
			nomeApresentacaoSistema = "";
		}
		if (nomeApresentacaoSistema.equals("") && !getNumero().trim().isEmpty()) { 
			return getNumero()  + (getDigito().trim().isEmpty() ? "" : "-" + getDigito());
		}
		return nomeApresentacaoSistema;
	}

	public void setNomeApresentacaoSistema(String nomeApresentacaoSistema) {
		this.nomeApresentacaoSistema = nomeApresentacaoSistema;
	}
	
	public String getNomeBancoAgenciaContaApresentar(){
		return "Banco:" + getAgencia().getBanco().getNome() + " Ag:" + getAgencia().getNumeroAgencia() + "-" + getAgencia().getDigito() + " CC:" + getNumero() + "-" + getDigito() + " Carteira: " + getCarteira();
	}

	public Boolean getGerarRemessaSemDescontoAbatido() {
		if (gerarRemessaSemDescontoAbatido == null) {
			gerarRemessaSemDescontoAbatido = Boolean.TRUE;
		}
		return gerarRemessaSemDescontoAbatido;
	}

	public void setGerarRemessaSemDescontoAbatido(Boolean gerarRemessaSemDescontoAbatido) {
		this.gerarRemessaSemDescontoAbatido = gerarRemessaSemDescontoAbatido;
	}

	public Boolean getControlarBloqueioEmissaoBoleto() {
		if (controlarBloqueioEmissaoBoleto == null) {
			controlarBloqueioEmissaoBoleto = Boolean.FALSE;
		}
		return controlarBloqueioEmissaoBoleto;
	}

	public void setControlarBloqueioEmissaoBoleto(Boolean controlarBloqueioEmissaoBoleto) {
		this.controlarBloqueioEmissaoBoleto = controlarBloqueioEmissaoBoleto;
	}

	public Boolean getBloquearEmissaoBoletoFimDeSemana() {
		if (bloquearEmissaoBoletoFimDeSemana == null) {
			bloquearEmissaoBoletoFimDeSemana = Boolean.FALSE;
		}
		return bloquearEmissaoBoletoFimDeSemana;
	}

	public void setBloquearEmissaoBoletoFimDeSemana(Boolean bloquearEmissaoBoletoFimDeSemana) {
		this.bloquearEmissaoBoletoFimDeSemana = bloquearEmissaoBoletoFimDeSemana;
	}

	public Boolean getBloquearEmissaoBoletoFeriado() {
		if (bloquearEmissaoBoletoFeriado == null) {
			bloquearEmissaoBoletoFeriado = Boolean.FALSE;
		}
		return bloquearEmissaoBoletoFeriado;
	}

	public void setBloquearEmissaoBoletoFeriado(Boolean bloquearEmissaoBoletoFeriado) {
		this.bloquearEmissaoBoletoFeriado = bloquearEmissaoBoletoFeriado;
	}

	public String getBloquearEmissaoBoletoHoraIni() {
		if (bloquearEmissaoBoletoHoraIni == null) {
			bloquearEmissaoBoletoHoraIni = "";
		}
		return bloquearEmissaoBoletoHoraIni;
	}

	public void setBloquearEmissaoBoletoHoraIni(String bloquearEmissaoBoletoHoraIni) {
		this.bloquearEmissaoBoletoHoraIni = bloquearEmissaoBoletoHoraIni;
	}

	public String getBloquearEmissaoBoletoHoraFim() {
		if (bloquearEmissaoBoletoHoraFim == null) {
			bloquearEmissaoBoletoHoraFim = "";
		}
		return bloquearEmissaoBoletoHoraFim;
	}

	public void setBloquearEmissaoBoletoHoraFim(String bloquearEmissaoBoletoHoraFim) {
		this.bloquearEmissaoBoletoHoraFim = bloquearEmissaoBoletoHoraFim;
	}

	public String getMensagemBloqueioEmissaoBoleto() {
		if (mensagemBloqueioEmissaoBoleto == null) {
			mensagemBloqueioEmissaoBoleto = "";
		}
		return mensagemBloqueioEmissaoBoleto;
	}

	public void setMensagemBloqueioEmissaoBoleto(String mensagemBloqueioEmissaoBoleto) {
		this.mensagemBloqueioEmissaoBoleto = mensagemBloqueioEmissaoBoleto;
	}

	public Boolean getUtilizaAbatimentoNoRepasseRemessaBanco() {
		if (utilizaAbatimentoNoRepasseRemessaBanco == null) {
			utilizaAbatimentoNoRepasseRemessaBanco = Boolean.FALSE;
		}
		return utilizaAbatimentoNoRepasseRemessaBanco;
	}

	public void setUtilizaAbatimentoNoRepasseRemessaBanco(Boolean utilizaAbatimentoNoRepasseRemessaBanco) {
		this.utilizaAbatimentoNoRepasseRemessaBanco = utilizaAbatimentoNoRepasseRemessaBanco;
	}

	public Boolean getGerarContaPagarTaxaBancaria() {
		if (gerarContaPagarTaxaBancaria == null) {
			gerarContaPagarTaxaBancaria = Boolean.TRUE;
		}
		return gerarContaPagarTaxaBancaria;
	}

	public void setGerarContaPagarTaxaBancaria(Boolean gerarContaPagarTaxaBancaria) {
		this.gerarContaPagarTaxaBancaria = gerarContaPagarTaxaBancaria;
	}

	public FormaPagamentoVO getFormaPgtoTaxaBancaria() {
		if (formaPgtoTaxaBancaria == null) {
			formaPgtoTaxaBancaria = new FormaPagamentoVO();
		}
		return formaPgtoTaxaBancaria;
	}

	public void setFormaPgtoTaxaBancaria(FormaPagamentoVO formaPgtoTaxaBancaria) {
		this.formaPgtoTaxaBancaria = formaPgtoTaxaBancaria;
	}

	public CategoriaDespesaVO getCategoriaDespesaTaxaBancaria() {
		if (categoriaDespesaTaxaBancaria == null) {
			categoriaDespesaTaxaBancaria = new CategoriaDespesaVO();
		}
		return categoriaDespesaTaxaBancaria;
	}

	public void setCategoriaDespesaTaxaBancaria(CategoriaDespesaVO categoriaDespesaTaxaBancaria) {
		this.categoriaDespesaTaxaBancaria = categoriaDespesaTaxaBancaria;
	}

	public Integer getQtdDiasBaixaAutTitulo() {
		if (qtdDiasBaixaAutTitulo == null) {
			qtdDiasBaixaAutTitulo = 120;
		}
		return qtdDiasBaixaAutTitulo;
	}

	public void setQtdDiasBaixaAutTitulo(Integer qtdDiasBaixaAutTitulo) {
		this.qtdDiasBaixaAutTitulo = qtdDiasBaixaAutTitulo;
	}

	public Boolean getRemessaBoletoEmitido() {
		if (remessaBoletoEmitido == null) {
			remessaBoletoEmitido = Boolean.FALSE;
		}
		return remessaBoletoEmitido;
	}

	public void setRemessaBoletoEmitido(Boolean remessaBoletoEmitido) {
		this.remessaBoletoEmitido = remessaBoletoEmitido;
	}

	public Boolean getHabilitarRegistroRemessaOnline() {
		if (habilitarRegistroRemessaOnline == null) {
			habilitarRegistroRemessaOnline = Boolean.FALSE;
		}
		return habilitarRegistroRemessaOnline;
	}

	public void setHabilitarRegistroRemessaOnline(Boolean habilitarRegistroRemessaOnline) {
		this.habilitarRegistroRemessaOnline = habilitarRegistroRemessaOnline;
	}
	

	public TipoAutenticacaoRegistroRemessaOnlineEnum getTipoAutenticacaoRegistroRemessaOnlineEnum() {
		if (tipoAutenticacaoRegistroRemessaOnlineEnum == null) {
			tipoAutenticacaoRegistroRemessaOnlineEnum = TipoAutenticacaoRegistroRemessaOnlineEnum.TOKEN;
		}
		return tipoAutenticacaoRegistroRemessaOnlineEnum;
	}

	public void setTipoAutenticacaoRegistroRemessaOnlineEnum(TipoAutenticacaoRegistroRemessaOnlineEnum tipoAutenticacaoRegistroRemessaOnlineEnum) {
		this.tipoAutenticacaoRegistroRemessaOnlineEnum = tipoAutenticacaoRegistroRemessaOnlineEnum;
	}

	public AmbienteContaCorrenteEnum getAmbienteContaCorrenteEnum() {
		if (ambienteContaCorrenteEnum == null) {
			ambienteContaCorrenteEnum = AmbienteContaCorrenteEnum.HOMOLOGACAO;
		}
		return ambienteContaCorrenteEnum;
	}

	public void setAmbienteContaCorrenteEnum(AmbienteContaCorrenteEnum ambienteContaCorrenteEnum) {
		this.ambienteContaCorrenteEnum = ambienteContaCorrenteEnum;
	}

	public String getTokenIdRegistroRemessaOnline() {
		if (tokenIdRegistroRemessaOnline == null) {
			tokenIdRegistroRemessaOnline = "";
		}
		return tokenIdRegistroRemessaOnline;
	}

	public void setTokenIdRegistroRemessaOnline(String tokenIdRegistroRemessaOnline) {
		this.tokenIdRegistroRemessaOnline = tokenIdRegistroRemessaOnline;
	}

	public String getTokenKeyRegistroRemessaOnline() {
		if (tokenKeyRegistroRemessaOnline == null) {
			tokenKeyRegistroRemessaOnline = "";
		}
		return tokenKeyRegistroRemessaOnline;
	}

	public void setTokenKeyRegistroRemessaOnline(String tokenKeyRegistroRemessaOnline) {
		this.tokenKeyRegistroRemessaOnline = tokenKeyRegistroRemessaOnline;
	}

	public String getTokenClienteRegistroRemessaOnline() {
		if (tokenClienteRegistroRemessaOnline == null) {
			tokenClienteRegistroRemessaOnline = "";
		}
		return tokenClienteRegistroRemessaOnline;
	}

	public void setTokenClienteRegistroRemessaOnline(String tokenClienteRegistroRemessaOnline) {
		this.tokenClienteRegistroRemessaOnline = tokenClienteRegistroRemessaOnline;
	}
	
	
	public String getUrlPix() {
		return getAmbienteContaCorrentePix().isHomologacao() ? getAgencia().getBanco().getUrlApiPixHomologacao() : getAgencia().getBanco().getUrlApiPixProducao();
	}
	
	public String getUrlRegistroWebhook() {
		if (getAgencia().getBanco().isBancoItau()) {
			return getUrlPix() + UteisWebServiceUrl.URL_PIX_RECEBIMENTOS_WEBHOOK + getChavePix();				
		}
		throw new StreamSeiException("Não foi definido a URL para o registro de webhoo do Pix");
	}
	
	public String getUrlConsultaWebhook() {
		if (getAgencia().getBanco().isBancoItau()) {
			return getUrlPix() + UteisWebServiceUrl.URL_PIX_RECEBIMENTOS_WEBHOOK + getChavePix();				
		}
		throw new StreamSeiException("Não foi definido a URL para o registro de webhoo do Pix");
	}
	public String getUrlCancelamentoWebhook() {
		if (getAgencia().getBanco().isBancoItau()) {
			return getUrlPix() + UteisWebServiceUrl.URL_PIX_RECEBIMENTOS_WEBHOOK + getChavePix();				
		}
		throw new StreamSeiException("Não foi definido a URL para o registro de webhoo do Pix");
	}
	
	public Boolean getHabilitarRegistroPix() {
		if(habilitarRegistroPix == null) {
			habilitarRegistroPix = false;
		}
		return habilitarRegistroPix;
	}

	public void setHabilitarRegistroPix(Boolean habilitarRegistroPix) {
		this.habilitarRegistroPix = habilitarRegistroPix;
	}

	public AmbienteContaCorrenteEnum getAmbienteContaCorrentePix() {
		if(ambienteContaCorrentePix == null) {
			ambienteContaCorrentePix = AmbienteContaCorrenteEnum.HOMOLOGACAO;
		}
		return ambienteContaCorrentePix;
	}

	public void setAmbienteContaCorrentePix(AmbienteContaCorrenteEnum ambienteContaCorrentePix) {
		this.ambienteContaCorrentePix = ambienteContaCorrentePix;
	}

	public String getTokenIdRegistroPix() {
		if(tokenIdRegistroPix == null) {
			tokenIdRegistroPix = "";
		}
		return tokenIdRegistroPix;
	}

	public void setTokenIdRegistroPix(String tokenIdRegistroPix) {
		this.tokenIdRegistroPix = tokenIdRegistroPix;
	}

	public String getTokenKeyRegistroPix() {
		if(tokenKeyRegistroPix == null) {
			tokenKeyRegistroPix = "";
		}
		return tokenKeyRegistroPix;
	}

	public void setTokenKeyRegistroPix(String tokenKeyRegistroPix) {
		this.tokenKeyRegistroPix = tokenKeyRegistroPix;
	}

	public String getChavePix() {
		if(chavePix == null) {
			chavePix = "";
		}
		return chavePix;
	}

	public void setChavePix(String chavePix) {
		this.chavePix = chavePix;
	}
	

	public String getChaveAplicacaoDesenvolvedorPix() {
		if (chaveAplicacaoDesenvolvedorPix == null) {
			chaveAplicacaoDesenvolvedorPix = "";
		}
		return chaveAplicacaoDesenvolvedorPix;
	}

	public void setChaveAplicacaoDesenvolvedorPix(String chaveAplicacaoDesenvolvedorPix) {
		this.chaveAplicacaoDesenvolvedorPix = chaveAplicacaoDesenvolvedorPix;
	}
	
	public boolean isApresentarChaveAplicacaoDesenvolvedorPix() {
		return getAgencia().getBanco().isBancoBrasil();
	}

	public FormaPagamentoVO getFormaRecebimentoPadraoPix() {
		if(formaRecebimentoPadraoPix == null) {
			formaRecebimentoPadraoPix = new FormaPagamentoVO();
		}
		return formaRecebimentoPadraoPix;
	}

	public void setFormaRecebimentoPadraoPix(FormaPagamentoVO formaRecebimentoPadraoPix) {
		this.formaRecebimentoPadraoPix = formaRecebimentoPadraoPix;
	}

	public String getSenhaCertificado() {
		if (senhaCertificado == null) {
			senhaCertificado = "";
		}
		return senhaCertificado;
	}

	public void setSenhaCertificado(String senhaCertificado) {
		this.senhaCertificado = senhaCertificado;
	}

	public String getCaminhoCertificado() {
		if (caminhoCertificado == null) {
			caminhoCertificado = "";
		}
		return caminhoCertificado;
	}

	public void setCaminhoCertificado(String caminhoCertificado) {
		this.caminhoCertificado = caminhoCertificado;
	}

	public ArquivoVO getArquivoCertificadoVO() {
		if (arquivoCertificadoVO == null) {
			arquivoCertificadoVO = new ArquivoVO();
		}
		return arquivoCertificadoVO;
	}

	public void setArquivoCertificadoVO(ArquivoVO arquivoCertificadoVO) {
		this.arquivoCertificadoVO = arquivoCertificadoVO;
	}

	public Date getDataVencimentoCertificado() {
		if (dataVencimentoCertificado == null) {
			dataVencimentoCertificado = new Date();
		}
		return dataVencimentoCertificado;
	}

	public void setDataVencimentoCertificado(Date dataVencimentoCertificado) {
		this.dataVencimentoCertificado = dataVencimentoCertificado;
	}

	public String getSenhaUnidadeCertificadora() {
		if (senhaUnidadeCertificadora == null) {
			senhaUnidadeCertificadora = "";
		}
		return senhaUnidadeCertificadora;
	}

	public void setSenhaUnidadeCertificadora(String senhaUnidadeCertificadora) {
		this.senhaUnidadeCertificadora = senhaUnidadeCertificadora;
	}

	public String getCaminhoUnidadeCertificadora() {
		if (caminhoUnidadeCertificadora == null) {
			caminhoUnidadeCertificadora = "";
		}
		return caminhoUnidadeCertificadora;
	}

	public void setCaminhoUnidadeCertificadora(String caminhoUnidadeCertificadora) {
		this.caminhoUnidadeCertificadora = caminhoUnidadeCertificadora;
	}

	public ArquivoVO getArquivoUnidadeCertificadoraVO() {
		if (arquivoUnidadeCertificadoraVO == null) {
			arquivoUnidadeCertificadoraVO = new ArquivoVO();
		}
		return arquivoUnidadeCertificadoraVO;
	}

	public void setArquivoUnidadeCertificadoraVO(ArquivoVO arquivoUnidadeCertificadoraVO) {
		this.arquivoUnidadeCertificadoraVO = arquivoUnidadeCertificadoraVO;
	}

	public Boolean getGerarRemessaMatriculaAut() {
		if (gerarRemessaMatriculaAut == null) {
			gerarRemessaMatriculaAut = Boolean.TRUE;
		}
		return gerarRemessaMatriculaAut;
	}

	public void setGerarRemessaMatriculaAut(Boolean gerarRemessaMatriculaAut) {
		this.gerarRemessaMatriculaAut = gerarRemessaMatriculaAut;
	}

	public Boolean getGerarRemessaParcelasAut() {
		if (gerarRemessaParcelasAut == null) {
			gerarRemessaParcelasAut = Boolean.TRUE;
		}
		return gerarRemessaParcelasAut;
	}

	public void setGerarRemessaParcelasAut(Boolean gerarRemessaParcelasAut) {
		this.gerarRemessaParcelasAut = gerarRemessaParcelasAut;
	}

	public Boolean getGerarRemessaNegociacaoAut() {
		if (gerarRemessaNegociacaoAut == null) {
			gerarRemessaNegociacaoAut = Boolean.TRUE;
		}
		return gerarRemessaNegociacaoAut;
	}

	public void setGerarRemessaNegociacaoAut(Boolean gerarRemessaNegociacaoAut) {
		this.gerarRemessaNegociacaoAut = gerarRemessaNegociacaoAut;
	}

	public Boolean getPermitirEmissaoBoletoRemessaOnlineRejeita() {
		if (permitirEmissaoBoletoRemessaOnlineRejeita == null) {
			permitirEmissaoBoletoRemessaOnlineRejeita = Boolean.FALSE;
		}
		return permitirEmissaoBoletoRemessaOnlineRejeita;
	}

	public void setPermitirEmissaoBoletoRemessaOnlineRejeita(Boolean permitirEmissaoBoletoRemessaOnlineRejeita) {
		this.permitirEmissaoBoletoRemessaOnlineRejeita = permitirEmissaoBoletoRemessaOnlineRejeita;
	}

	public Boolean getGerarRemessaOutrosAut() {
		if (gerarRemessaOutrosAut == null) {
			gerarRemessaOutrosAut = Boolean.TRUE;
		}
		return gerarRemessaOutrosAut;
	}

	public void setGerarRemessaOutrosAut(Boolean gerarRemessaOutrosAut) {
		this.gerarRemessaOutrosAut = gerarRemessaOutrosAut;
	}

	public Boolean getGerarRemessaRequerimentoAut() {
		if (gerarRemessaRequerimentoAut == null) {
			gerarRemessaRequerimentoAut = Boolean.TRUE;
		}
		return gerarRemessaRequerimentoAut;
	}

	public void setGerarRemessaRequerimentoAut(Boolean gerarRemessaRequerimentoAut) {
		this.gerarRemessaRequerimentoAut = gerarRemessaRequerimentoAut;
	}

	public Boolean getGerarRemessaBibliotecaAut() {
		if (gerarRemessaBibliotecaAut == null) {
			gerarRemessaBibliotecaAut = Boolean.TRUE;
		}
		return gerarRemessaBibliotecaAut;
	}

	public void setGerarRemessaBibliotecaAut(Boolean gerarRemessaBibliotecaAut) {
		this.gerarRemessaBibliotecaAut = gerarRemessaBibliotecaAut;
	}

	public Boolean getGerarRemessaInscProcSeletivoAut() {
		if (gerarRemessaInscProcSeletivoAut == null) {
			gerarRemessaInscProcSeletivoAut = Boolean.TRUE;
		}
		return gerarRemessaInscProcSeletivoAut;
	}

	public void setGerarRemessaInscProcSeletivoAut(Boolean gerarRemessaInscProcSeletivoAut) {
		this.gerarRemessaInscProcSeletivoAut = gerarRemessaInscProcSeletivoAut;
	}

	public Boolean getGerarRemessaDevChequeAut() {
		if (gerarRemessaDevChequeAut == null) {
			gerarRemessaDevChequeAut = Boolean.TRUE;
		}
		return gerarRemessaDevChequeAut;
	}

	public void setGerarRemessaDevChequeAut(Boolean gerarRemessaDevChequeAut) {
		this.gerarRemessaDevChequeAut = gerarRemessaDevChequeAut;
	}

	public Boolean getGerarRemessaConvenioAut() {
		if (gerarRemessaConvenioAut == null) {
			gerarRemessaConvenioAut = Boolean.TRUE;
		}
		return gerarRemessaConvenioAut;
	}

	public void setGerarRemessaConvenioAut(Boolean gerarRemessaConvenioAut) {
		this.gerarRemessaConvenioAut = gerarRemessaConvenioAut;
	}

	public Boolean getGerarRemessaContratoReceitaAut() {
		if (gerarRemessaContratoReceitaAut == null) {
			gerarRemessaContratoReceitaAut = Boolean.TRUE;
		}
		return gerarRemessaContratoReceitaAut;
	}

	public void setGerarRemessaContratoReceitaAut(Boolean gerarRemessaContratoReceitaAut) {
		this.gerarRemessaContratoReceitaAut = gerarRemessaContratoReceitaAut;
	}

	public Boolean getGerarRemessaMateriaDidaticoAut() {
		if (gerarRemessaMateriaDidaticoAut == null) {
			gerarRemessaMateriaDidaticoAut = Boolean.TRUE;
		}
		return gerarRemessaMateriaDidaticoAut;
	}

	public void setGerarRemessaMateriaDidaticoAut(Boolean gerarRemessaMateriaDidaticoAut) {
		this.gerarRemessaMateriaDidaticoAut = gerarRemessaMateriaDidaticoAut;
	}

	public Boolean getGerarRemessaInclusaoReposicaoAut() {
		if (gerarRemessaInclusaoReposicaoAut == null) {
			gerarRemessaInclusaoReposicaoAut = Boolean.TRUE;
		}
		return gerarRemessaInclusaoReposicaoAut;
	}

	public void setGerarRemessaInclusaoReposicaoAut(Boolean gerarRemessaInclusaoReposicaoAut) {
		this.gerarRemessaInclusaoReposicaoAut = gerarRemessaInclusaoReposicaoAut;
	}

	public String getCodigoComunicacaoRemessaCP() {
		if (codigoComunicacaoRemessaCP == null) {
			codigoComunicacaoRemessaCP = "";
		}
		return codigoComunicacaoRemessaCP;
	}

	public void setCodigoComunicacaoRemessaCP(String codigoComunicacaoRemessaCP) {
		this.codigoComunicacaoRemessaCP = codigoComunicacaoRemessaCP;
	}


	public String getCodigoEstacaoRemessa() {
		if (codigoEstacaoRemessa == null) {
			codigoEstacaoRemessa = "";
		}
		return codigoEstacaoRemessa;
	}

	public void setCodigoEstacaoRemessa(String codigoEstacaoRemessa) {
		this.codigoEstacaoRemessa = codigoEstacaoRemessa;
	}

	/**
	 * atributo transiente utilizado para marcar contas caixas para emissao de relatorios e outros
	 */
	private Boolean filtrarConta;
	
	public Boolean getFiltrarConta() {
		if (filtrarConta == null) {
			filtrarConta = Boolean.FALSE;
		}
		return filtrarConta;
	}

	public void setFiltrarConta(Boolean filtrarConta) {
		this.filtrarConta = filtrarConta;
	}

	public Boolean getRealizarNegociacaoContaReceberVencidaAutomaticamente() {
		if(realizarNegociacaoContaReceberVencidaAutomaticamente == null){
			realizarNegociacaoContaReceberVencidaAutomaticamente = false;
		}
		return realizarNegociacaoContaReceberVencidaAutomaticamente;
	}

	public void setRealizarNegociacaoContaReceberVencidaAutomaticamente(
			Boolean realizarNegociacaoContaReceberVencidaAutomaticamente) {
		this.realizarNegociacaoContaReceberVencidaAutomaticamente = realizarNegociacaoContaReceberVencidaAutomaticamente;
	}

	public Integer getNumeroDiaVencidoNegociarContaReceberAutomaticamente() {
		if(numeroDiaVencidoNegociarContaReceberAutomaticamente == null){
			numeroDiaVencidoNegociarContaReceberAutomaticamente = 0;
		}
		return numeroDiaVencidoNegociarContaReceberAutomaticamente;
	}

	public void setNumeroDiaVencidoNegociarContaReceberAutomaticamente(
			Integer numeroDiaVencidoNegociarContaReceberAutomaticamente) {
		this.numeroDiaVencidoNegociarContaReceberAutomaticamente = numeroDiaVencidoNegociarContaReceberAutomaticamente;
	}

	public Integer getNumeroDiaAvancarVencimentoContaReceber() {
		if(numeroDiaAvancarVencimentoContaReceber == null){
			numeroDiaAvancarVencimentoContaReceber = 0;
		}
		return numeroDiaAvancarVencimentoContaReceber;
	}

	public void setNumeroDiaAvancarVencimentoContaReceber(Integer numeroDiaAvancarVencimentoContaReceber) {
		this.numeroDiaAvancarVencimentoContaReceber = numeroDiaAvancarVencimentoContaReceber;
	}

	public Boolean getPermiteEmissaoBoletoVencido() {
		if(permiteEmissaoBoletoVencido == null){
			permiteEmissaoBoletoVencido = true;
		}
		return permiteEmissaoBoletoVencido;
	}

	public void setPermiteEmissaoBoletoVencido(Boolean permiteEmissaoBoletoVencido) {
		this.permiteEmissaoBoletoVencido = permiteEmissaoBoletoVencido;
	}

	public TipoContaCorrenteEnum getTipoContaCorrenteEnum() {
		if(tipoContaCorrenteEnum == null){
			tipoContaCorrenteEnum = TipoContaCorrenteEnum.CORRENTE;
		}
		return tipoContaCorrenteEnum;
	}

	public void setTipoContaCorrenteEnum(TipoContaCorrenteEnum tipoContaCorrenteEnum) {
		this.tipoContaCorrenteEnum = tipoContaCorrenteEnum;
	}

	public boolean isUtilizaTaxaCartaoDebito() {		
		return utilizaTaxaCartaoDebito;
	}

	public void setUtilizaTaxaCartaoDebito(boolean utilizaTaxaCartaoDebito) {
		this.utilizaTaxaCartaoDebito = utilizaTaxaCartaoDebito;
	}

	public boolean isUtilizaTaxaCartaoCredito() {
		return utilizaTaxaCartaoCredito;
	}

	public void setUtilizaTaxaCartaoCredito(boolean utilizaTaxaCartaoCredito) {
		this.utilizaTaxaCartaoCredito = utilizaTaxaCartaoCredito;
	}
	
	public Boolean getHabilitarProtestoBoleto() {
		if (habilitarProtestoBoleto == null) {
			habilitarProtestoBoleto = Boolean.FALSE;
		}
		return habilitarProtestoBoleto;
	}

	public void setHabilitarProtestoBoleto(Boolean habilitarProtestoBoleto) {
		this.habilitarProtestoBoleto = habilitarProtestoBoleto;
	}

	public Integer getQtdDiasProtestoBoleto() {
		if (qtdDiasProtestoBoleto == null) {
			qtdDiasProtestoBoleto = 0;
		} else {
			if (qtdDiasProtestoBoleto.toString().length() <= 1) {
				
			}
		}
		return qtdDiasProtestoBoleto;
	}

	public String getQtdDiasProtestoBoleto_Str() {
		if (getQtdDiasProtestoBoleto().toString().length() <= 1) {
			return "0" + getQtdDiasProtestoBoleto();
		}
		return getQtdDiasProtestoBoleto().toString();
	}

	public void setQtdDiasProtestoBoleto(Integer qtdDiasProtestoBoleto) {
		this.qtdDiasProtestoBoleto = qtdDiasProtestoBoleto;
	}
	
	public String getEspecieTituloBoleto() {
		if (especieTituloBoleto == null) {
			especieTituloBoleto = "";
		}
		return especieTituloBoleto;
	}

	public void setEspecieTituloBoleto(String especieTituloBoleto) {
		this.especieTituloBoleto = especieTituloBoleto;
	}

	public Boolean getPermiteGerarRemessaOnlineBoletoVencido() {
		if(permiteGerarRemessaOnlineBoletoVencido == null) {
			permiteGerarRemessaOnlineBoletoVencido = false;
		}
		return permiteGerarRemessaOnlineBoletoVencido;
	}

	public void setPermiteGerarRemessaOnlineBoletoVencido(Boolean permiteGerarRemessaOnlineBoletoVencido) {	
		this.permiteGerarRemessaOnlineBoletoVencido = permiteGerarRemessaOnlineBoletoVencido;
	}
	
	public Boolean getGerarRemessaBoletoVencidoMatricula() {
		if(gerarRemessaBoletoVencidoMatricula == null) {
			gerarRemessaBoletoVencidoMatricula = false;
		}
		return gerarRemessaBoletoVencidoMatricula;
	}

	public void setGerarRemessaBoletoVencidoMatricula(Boolean gerarRemessaBoletoVencidoMatricula) {
		this.gerarRemessaBoletoVencidoMatricula = gerarRemessaBoletoVencidoMatricula;
	}

	public Boolean getGerarRemessaBoletoVencidoParcelas() {
		if(gerarRemessaBoletoVencidoParcelas == null) {
			gerarRemessaBoletoVencidoParcelas = false;
		}
		return gerarRemessaBoletoVencidoParcelas;
	}

	public void setGerarRemessaBoletoVencidoParcelas(Boolean gerarRemessaBoletoVencidoParcelas) {
		this.gerarRemessaBoletoVencidoParcelas = gerarRemessaBoletoVencidoParcelas;
	}

	public Boolean getGerarRemessaBoletoVencidoNegociacao() {
		if(gerarRemessaBoletoVencidoNegociacao == null) {
			gerarRemessaBoletoVencidoNegociacao = false;
		}
		return gerarRemessaBoletoVencidoNegociacao;
	}

	public void setGerarRemessaBoletoVencidoNegociacao(Boolean gerarRemessaBoletoVencidoNegociacao) {
		this.gerarRemessaBoletoVencidoNegociacao = gerarRemessaBoletoVencidoNegociacao;
	}

	public Boolean getGerarRemessaBoletoVencidoOutros() {
		if(gerarRemessaBoletoVencidoOutros == null) {
			gerarRemessaBoletoVencidoOutros = false;
		}
		return gerarRemessaBoletoVencidoOutros;
	}

	public void setGerarRemessaBoletoVencidoOutros(Boolean gerarRemessaBoletoVencidoOutros) {
		this.gerarRemessaBoletoVencidoOutros = gerarRemessaBoletoVencidoOutros;
	}

	public Boolean getGerarRemessaBoletoVencidoBiblioteca() {
		if(gerarRemessaBoletoVencidoBiblioteca == null) {
			gerarRemessaBoletoVencidoBiblioteca = false;
		}
		return gerarRemessaBoletoVencidoBiblioteca;
	}

	public void setGerarRemessaBoletoVencidoBiblioteca(Boolean gerarRemessaBoletoVencidoBiblioteca) {
		this.gerarRemessaBoletoVencidoBiblioteca = gerarRemessaBoletoVencidoBiblioteca;
	}

	public Boolean getGerarRemessaBoletoVencidoDevCheque() {
		if(gerarRemessaBoletoVencidoDevCheque == null) {
			gerarRemessaBoletoVencidoDevCheque = false;
		}
		return gerarRemessaBoletoVencidoDevCheque;
	}

	public void setGerarRemessaBoletoVencidoDevCheque(Boolean gerarRemessaBoletoVencidoDevCheque) {
		this.gerarRemessaBoletoVencidoDevCheque = gerarRemessaBoletoVencidoDevCheque;
	}

	public Boolean getGerarRemessaBoletoVencidoConvenio() {
		if(gerarRemessaBoletoVencidoConvenio == null) {
			gerarRemessaBoletoVencidoConvenio = false;
		}
		return gerarRemessaBoletoVencidoConvenio;
	}

	public void setGerarRemessaBoletoVencidoConvenio(Boolean gerarRemessaBoletoVencidoConvenio) {
		this.gerarRemessaBoletoVencidoConvenio = gerarRemessaBoletoVencidoConvenio;
	}

	public Boolean getGerarRemessaBoletoVencidoContratoReceita() {
		if(gerarRemessaBoletoVencidoContratoReceita == null) {
			gerarRemessaBoletoVencidoContratoReceita = false;
		}
		return gerarRemessaBoletoVencidoContratoReceita;
	}

	public void setGerarRemessaBoletoVencidoContratoReceita(Boolean gerarRemessaBoletoVencidoContratoReceita) {
		this.gerarRemessaBoletoVencidoContratoReceita = gerarRemessaBoletoVencidoContratoReceita;
	}

	public Boolean getGerarRemessaBoletoVencidoMateriaDidatico() {
		if(gerarRemessaBoletoVencidoMateriaDidatico == null) {
			gerarRemessaBoletoVencidoMateriaDidatico = false;
		}
		return gerarRemessaBoletoVencidoMateriaDidatico;
	}

	public void setGerarRemessaBoletoVencidoMateriaDidatico(Boolean gerarRemessaBoletoVencidoMateriaDidatico) {
		this.gerarRemessaBoletoVencidoMateriaDidatico = gerarRemessaBoletoVencidoMateriaDidatico;
	}

	public Boolean getGerarRemessaBoletoVencidoInclusaoReposicao() {
		if(gerarRemessaBoletoVencidoInclusaoReposicao == null) {
			gerarRemessaBoletoVencidoInclusaoReposicao = false;
		}
		return gerarRemessaBoletoVencidoInclusaoReposicao;
	}

	public void setGerarRemessaBoletoVencidoInclusaoReposicao(Boolean gerarRemessaBoletoVencidoInclusaoReposicao) {
		this.gerarRemessaBoletoVencidoInclusaoReposicao = gerarRemessaBoletoVencidoInclusaoReposicao;
	}

	public Integer getQtdeDiasVencidoPermitirRemessaOnlineMatricula() {
		if(qtdeDiasVencidoPermitirRemessaOnlineMatricula == null) {
			qtdeDiasVencidoPermitirRemessaOnlineMatricula = 0;
		}
		return qtdeDiasVencidoPermitirRemessaOnlineMatricula;
	}

	public void setQtdeDiasVencidoPermitirRemessaOnlineMatricula(Integer qtdeDiasVencidoPermitirRemessaOnlineMatricula) {
		this.qtdeDiasVencidoPermitirRemessaOnlineMatricula = qtdeDiasVencidoPermitirRemessaOnlineMatricula;
	}

	public Integer getQtdeDiasVencidoPermitirRemessaOnlineParcela() {
		if(qtdeDiasVencidoPermitirRemessaOnlineParcela == null) {
			qtdeDiasVencidoPermitirRemessaOnlineParcela = 0;
		}
		return qtdeDiasVencidoPermitirRemessaOnlineParcela;
	}

	public void setQtdeDiasVencidoPermitirRemessaOnlineParcela(Integer qtdeDiasVencidoPermitirRemessaOnlineParcela) {
		this.qtdeDiasVencidoPermitirRemessaOnlineParcela = qtdeDiasVencidoPermitirRemessaOnlineParcela;
	}

	public Integer getQtdeDiasVencidoPermitirRemessaOnlineNegociacao() {
		if(qtdeDiasVencidoPermitirRemessaOnlineNegociacao == null) {
			qtdeDiasVencidoPermitirRemessaOnlineNegociacao = 0;
		}
		return qtdeDiasVencidoPermitirRemessaOnlineNegociacao;
	}

	public void setQtdeDiasVencidoPermitirRemessaOnlineNegociacao(Integer qtdeDiasVencidoPermitirRemessaOnlineNegociacao) {
		this.qtdeDiasVencidoPermitirRemessaOnlineNegociacao = qtdeDiasVencidoPermitirRemessaOnlineNegociacao;
	}

	public Integer getQtdeDiasVencidoPermitirRemessaOnlineOutros() {
		if(qtdeDiasVencidoPermitirRemessaOnlineOutros == null) {
			qtdeDiasVencidoPermitirRemessaOnlineOutros = 0;
		}
		return qtdeDiasVencidoPermitirRemessaOnlineOutros;
	}

	public void setQtdeDiasVencidoPermitirRemessaOnlineOutros(Integer qtdeDiasVencidoPermitirRemessaOnlineOutros) {
		this.qtdeDiasVencidoPermitirRemessaOnlineOutros = qtdeDiasVencidoPermitirRemessaOnlineOutros;
	}

	public Integer getQtdeDiasVencidoPermitirRemessaOnlineBiblioteca() {
		if(qtdeDiasVencidoPermitirRemessaOnlineBiblioteca == null) {
			qtdeDiasVencidoPermitirRemessaOnlineBiblioteca = 0;
		}
		return qtdeDiasVencidoPermitirRemessaOnlineBiblioteca;
	}

	public void setQtdeDiasVencidoPermitirRemessaOnlineBiblioteca(Integer qtdeDiasVencidoPermitirRemessaOnlineBiblioteca) {
		this.qtdeDiasVencidoPermitirRemessaOnlineBiblioteca = qtdeDiasVencidoPermitirRemessaOnlineBiblioteca;
	}

	public Integer getQtdeDiasVencidoPermitirRemessaOnlineDevCheque() {
		if(qtdeDiasVencidoPermitirRemessaOnlineDevCheque == null) {
			qtdeDiasVencidoPermitirRemessaOnlineDevCheque = 0;
		}
		return qtdeDiasVencidoPermitirRemessaOnlineDevCheque;
	}

	public void setQtdeDiasVencidoPermitirRemessaOnlineDevCheque(Integer qtdeDiasVencidoPermitirRemessaOnlineDevCheque) {
		this.qtdeDiasVencidoPermitirRemessaOnlineDevCheque = qtdeDiasVencidoPermitirRemessaOnlineDevCheque;
	}

	public Integer getQtdeDiasVencidoPermitirRemessaConvenio() {
		if(qtdeDiasVencidoPermitirRemessaConvenio == null) {
			qtdeDiasVencidoPermitirRemessaConvenio = 0;
		}
		return qtdeDiasVencidoPermitirRemessaConvenio;
	}

	public void setQtdeDiasVencidoPermitirRemessaConvenio(Integer qtdeDiasVencidoPermitirRemessaConvenio) {
		this.qtdeDiasVencidoPermitirRemessaConvenio = qtdeDiasVencidoPermitirRemessaConvenio;
	}

	public Integer getQtdeDiasVencidoPermitirRemessaContratoReceita() {
		if(qtdeDiasVencidoPermitirRemessaContratoReceita == null) {
			qtdeDiasVencidoPermitirRemessaContratoReceita = 0;
		}
		return qtdeDiasVencidoPermitirRemessaContratoReceita;
	}

	public void setQtdeDiasVencidoPermitirRemessaContratoReceita(Integer qtdeDiasVencidoPermitirRemessaContratoReceita) {
		this.qtdeDiasVencidoPermitirRemessaContratoReceita = qtdeDiasVencidoPermitirRemessaContratoReceita;
	}

	public Integer getQtdeDiasVencidoPermitirRemessaMaterialDidatico() {
		if(qtdeDiasVencidoPermitirRemessaMaterialDidatico == null) {
			qtdeDiasVencidoPermitirRemessaMaterialDidatico = 0;
		}
		return qtdeDiasVencidoPermitirRemessaMaterialDidatico;
	}

	public void setQtdeDiasVencidoPermitirRemessaMaterialDidatico(Integer qtdeDiasVencidoPermitirRemessaMaterialDidatico) {
		this.qtdeDiasVencidoPermitirRemessaMaterialDidatico = qtdeDiasVencidoPermitirRemessaMaterialDidatico;
	}

	public Integer getQtdeDiasVencidoPermitirRemessaInclusaoExclusao() {
		if(qtdeDiasVencidoPermitirRemessaInclusaoExclusao == null) {
			qtdeDiasVencidoPermitirRemessaInclusaoExclusao = 0;
		}
		return qtdeDiasVencidoPermitirRemessaInclusaoExclusao;
	}

	public void setQtdeDiasVencidoPermitirRemessaInclusaoExclusao(Integer qtdeDiasVencidoPermitirRemessaInclusaoExclusao) {
		this.qtdeDiasVencidoPermitirRemessaInclusaoExclusao = qtdeDiasVencidoPermitirRemessaInclusaoExclusao;
	}

	public String getInicialGeracaoNossoNumero() {
		if (inicialGeracaoNossoNumero == null) {
			inicialGeracaoNossoNumero = "";
		}
		return inicialGeracaoNossoNumero;
	}

	public void setInicialGeracaoNossoNumero(String inicialGeracaoNossoNumero) {
		this.inicialGeracaoNossoNumero = inicialGeracaoNossoNumero;
	}


	public Boolean getApresentarDefinicaoInicialGeracaoNossoNumero() {
		return getAgencia().getBanco().getNrBanco().equals(Bancos.SICOOB.getNumeroBanco()) || getAgencia().getBanco().getNrBanco().equals(Bancos.SANTANDER.getNumeroBanco())
				|| getAgencia().getBanco().getNrBanco().equals(Bancos.SICRED.getNumeroBanco());
	}

	public Boolean getBloquearEmitirBoletoSemRegistroRemessa() {
		if (bloquearEmitirBoletoSemRegistroRemessa == null) {
			bloquearEmitirBoletoSemRegistroRemessa = false;
		}
		return bloquearEmitirBoletoSemRegistroRemessa;
	}

	public void setBloquearEmitirBoletoSemRegistroRemessa(Boolean bloquearEmitirBoletoSemRegistroRemessa) {
		this.bloquearEmitirBoletoSemRegistroRemessa = bloquearEmitirBoletoSemRegistroRemessa;
	}

	public String getChaveAutenticacaoClienteRegistroRemessaOnline() {
		if(chaveAutenticacaoClienteRegistroRemessaOnline ==null) {
			chaveAutenticacaoClienteRegistroRemessaOnline = "";
		}
		return chaveAutenticacaoClienteRegistroRemessaOnline;
	}

	public void setChaveAutenticacaoClienteRegistroRemessaOnline(
			String chaveAutenticacaoClienteRegistroRemessaOnline) {
		this.chaveAutenticacaoClienteRegistroRemessaOnline = chaveAutenticacaoClienteRegistroRemessaOnline;
	}

	public String getChaveTransacaoClienteRegistroRemessaOnline() {
		if(chaveTransacaoClienteRegistroRemessaOnline == null) {
			chaveTransacaoClienteRegistroRemessaOnline = "";
		}
		return chaveTransacaoClienteRegistroRemessaOnline;
	}

	public void setChaveTransacaoClienteRegistroRemessaOnline(String chaveTransacaoClienteRegistroRemessaOnline) {
		this.chaveTransacaoClienteRegistroRemessaOnline = chaveTransacaoClienteRegistroRemessaOnline;
	}

	public Date getDataExpiracaoChaveTransacaoClienteRegistroRemessaOnline() {
		if(dataExpiracaoChaveTransacaoClienteRegistroRemessaOnline ==null ) {
			dataExpiracaoChaveTransacaoClienteRegistroRemessaOnline = new Date();
		}
		return dataExpiracaoChaveTransacaoClienteRegistroRemessaOnline;
	}

	public void setDataExpiracaoChaveTransacaoClienteRegistroRemessaOnline(
			Date dataExpiracaoChaveTransacaoClienteRegistroRemessaOnline) {
		this.dataExpiracaoChaveTransacaoClienteRegistroRemessaOnline = dataExpiracaoChaveTransacaoClienteRegistroRemessaOnline;
	}

	public Boolean getPermiteRecebimentoBoletoVencidoRemessaOnline() {
		if(permiteRecebimentoBoletoVencidoRemessaOnline == null ) {
			permiteRecebimentoBoletoVencidoRemessaOnline = Boolean.FALSE;
		}
		return permiteRecebimentoBoletoVencidoRemessaOnline;
	}

	public void setPermiteRecebimentoBoletoVencidoRemessaOnline(Boolean permiteRecebimentoBoletoVencidoRemessaOnline) {
		this.permiteRecebimentoBoletoVencidoRemessaOnline = permiteRecebimentoBoletoVencidoRemessaOnline;
	}

	public Integer getNumeroDiasLimiteRecebimentoBoletoVencidoRemessaOnline() {
		if(numeroDiasLimiteRecebimentoBoletoVencidoRemessaOnline == null ) {
			numeroDiasLimiteRecebimentoBoletoVencidoRemessaOnline = 0;
		}
		return numeroDiasLimiteRecebimentoBoletoVencidoRemessaOnline;
	}

	public void setNumeroDiasLimiteRecebimentoBoletoVencidoRemessaOnline(
			Integer numeroDiasLimiteRecebimentoBoletoVencidoRemessaOnline) {
		this.numeroDiasLimiteRecebimentoBoletoVencidoRemessaOnline = numeroDiasLimiteRecebimentoBoletoVencidoRemessaOnline;
	}
	
	
	
}
