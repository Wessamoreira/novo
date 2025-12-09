/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.financeiro;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ModeloBoletoVO;
import negocio.comuns.financeiro.OrdemDescontoVO;
import negocio.comuns.financeiro.PlanoFinanceiroAlunoDescricaoDescontosVO;
import negocio.comuns.financeiro.ProcessamentoIntegracaoFinanceiraDetalheVO;
import negocio.comuns.financeiro.enumerador.BancoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import relatorio.negocio.comuns.financeiro.enumeradores.TipoInstrucaoBoletoEnum;

/**
 * 
 * @author otimize-ti
 */
public class BoletoBancarioRelVO {

	protected Integer contareceber_codigo;
	protected String contareceber_codigobarra;
	protected String contareceber_matriculaaluno;
	protected Date contareceber_data;
	protected ProcessamentoIntegracaoFinanceiraDetalheVO processoIntegracaoFinanceiroDetalhe;
	protected String contareceber_tipoorigem;
	protected String contareceber_situacao;
	protected String contareceber_descricaopagamento;
	protected Date contareceber_datavencimento;
	protected Date contareceber_dataoriginalvencimento;
	protected Date contareceber_datavencimentodiautil;
	protected double contareceber_valor;
	private double contareceber_valorCusteadoContaReceber;
	private double contareceber_valorBaseContaReceber;
	private Double contareceber_valorDescontoRateio;
	private String contareceber_descricaoValorCusteadoContaReceber;
	private String contareceber_tipodesconto;
	protected double contareceber_valordesconto;
	protected double contareceber_juro;
	protected double contareceber_juroporcentagem;
	protected double contareceber_multa;
	protected double contareceber_multaporcentagem;
	protected double contareceber_acrescimo;
	protected double contareceber_valorIndiceReajustePorAtraso;
	protected String contareceber_nrdocumento;
	private String contareceber_nossonumero;
	protected String contareceber_parcela;
	protected Integer contareceber_origemnegociacaoreceber;
	protected Integer contacorrente_agencia;
	protected String contacorrente_carteira;
	protected Integer contareceber_contacorrente;
	protected double contareceber_descontoinstituica;
	protected double contareceber_descontoconvenio;
	protected Boolean contareceber_usadescontocompostoplanodesconto;
	protected Boolean contacorrente_utilizaDadosInformadosCCparaGeracaoBoleto;
	protected String agencia_numeroagencia;
	protected String contacorrente_numero;
	protected String contacorrente_convenio;
	protected Boolean contacorrente_habilitarRegistroRemessaOnline;
	protected String contacorrente_codigocedente;
	protected String contacorrente_digitocodigocedente;
	protected String contareceber_tipoDescontoAluno;
	protected Double contareceber_percDescontoAluno;
	protected Double contareceber_valorDescontoAluno;
	protected String pessoa_nome;
	protected String pessoa_cpf;
	protected String pessoa_setor;
	protected String pessoa_endereco;
	protected String pessoa_complemento;
	protected String pessoa_numero;
	protected String pessoa_cep;
	protected Integer pessoa_cidade;
	protected String cidade_nome;
	protected Integer agencia_banco;
	protected String banco_nome;
	protected String banco_nrbanco;
	protected String banco_digito;
	protected Integer cidade_estado;
	protected String estado_nome;
	protected Boolean ocultarCodBarraLinhaDigitavel;
	protected String modeloboleto_observacoesgerais1;
	protected String modeloboleto_observacoesgerais2;
	private Boolean modeloboleto_utilizarDescricaoDescontoPersonalizado;
	private String modeloboleto_textoTopo;
	private String modeloboleto_instrucao1;
	private String modeloboleto_instrucao2;
	private String modeloboleto_instrucao3;
	private String modeloboleto_instrucao4;
	private String modeloboleto_instrucao5;
	private String modeloboleto_instrucao6;
	private String modeloboleto_textoRodape;
	private String modeloboleto_textoTopoInferior;
	private String modeloboleto_instrucao1Inferior;
	private String modeloboleto_instrucao2Inferior;
	private String modeloboleto_instrucao3Inferior;
	private String modeloboleto_instrucao4Inferior;
	private String modeloboleto_instrucao5Inferior;
	private String modeloboleto_instrucao6Inferior;
	private String modeloboleto_textoRodapeInferior;

	// protected byte[] modeloboleto_imagem;
	protected String modeloboleto_localpagamento;
	private Boolean modeloboleto_apenasDescontoInstrucaoBoleto;
	protected String contareceber_linhadigitavelcodigobarras;
	protected Integer contareceber_descontoprogressivo;
	protected Integer descontoprogressivo_codigo;
	protected Integer descontoprogressivo_dialimite1;
	protected Integer descontoprogressivo_dialimite2;
	protected Integer descontoprogressivo_dialimite3;
	protected Integer descontoprogressivo_dialimite4;
	protected Boolean descontoprogressivo_utilizarDiaFixo;
	private Boolean descontoprogressivo_utilizarDiaUtil;
	private Integer diasVariacaoDataVencimento;
	protected double descontoprogressivo_percdescontolimite1;
	protected double descontoprogressivo_percdescontolimite2;
	protected double descontoprogressivo_percdescontolimite3;
	protected double descontoprogressivo_percdescontolimite4;
	protected double descontoprogressivo_valordescontolimite1;
	protected double descontoprogressivo_valordescontolimite2;
	protected double descontoprogressivo_valordescontolimite3;
	protected double descontoprogressivo_valordescontolimite4;
	protected double total1;
	protected double total2;
	protected double total3;
	protected double total4;
	protected double valorDescProg1;
	protected double valorDescProg2;
	protected double valorDescProg3;
	protected double valorDescProg4;
	protected double descontoTotal1;
	protected double descontoTotal2;
	protected double descontoTotal3;
	protected double descontoTotal4;
	protected Date dataDescProg1;
	protected Date dataDescProg2;
	protected Date dataDescProg3;
	protected Date dataDescProg4;
	protected String pagamento1Parte1;
	protected String pagamento1Parte2;
	protected String pagamento1Parte3;
	protected String pagamento1Parte4;
	protected String pagamento2Parte1;
	protected String pagamento2Parte2;
	protected String pagamento2Parte3;
	protected String pagamento2Parte4;
	protected String pagamento3Parte1;
	protected String pagamento3Parte2;
	protected String pagamento3Parte3;
	protected String pagamento3Parte4;
	protected String pagamento4Parte1;
	protected String pagamento4Parte2;
	protected String pagamento4Parte3;
	protected String pagamento4Parte4;
	private String pagamento5Parte1;
	private String pagamento5Parte2;
	private String pagamento5Parte3;
	private String pagamento5Parte4;
	private String pagamento6Parte1;
	private String pagamento6Parte2;
	private String pagamento6Parte3;
	private String pagamento6Parte4;
	protected String pagamentoSemDescProg;
	protected String nossonumero;
	private InputStream imagemBoleto;
	private String contareceber_mantenedora;
	private String contareceber_razaoSocialMantenedora;
	private String contareceber_cnpjMantenedora;
	private String tipoBoletoContaReceber;
	private String parceiro_razaosocial;
	private String turmaBase;
	private String nomeCurso;
	private String moeda;
	private List<OrdemDescontoVO> ordemDescontos;
	private String especieDoc;
	private String enderecoParceiro;
	private String setorParceiro;
	private String cepParceiro;
	private String cnpjParceiro;
	private String telParceiro;
	private String cidadeParceiro;
	private String estadoParceiro;
	private Boolean descontoValidoatedataparcela;
	private String contareceber_foneMantenedora;
	private String telefoneUnidadeEnsino;
	private String enderecoUnidadeEnsino;
	private String cidadeUnidadeEnsino;
	private String estadoUnidadeEnsino;
	private Integer codigoCidadeUnidadeEnsino;
	private String cepUnidadeEnsino;
	private String complementoUnidadeEnsino;
	private String setorUnidadeEnsino;
	private String numeroUnidadeEnsino;
	private String turno;
	private String situacaoMatricula;
	private String ano;
	private String semestre;
	private String responsavelFinanceiro;
	private String digitoVerificadorNossoNumero;
	private Integer modeloBoletoMatricula_codigo;
	private Integer modeloBoletoMensalidade_codigo;
	private Integer modeloBoletoMaterialDidatico_codigo;
	private Integer modeloBoletoRequerimento_codigo;
	private Integer modeloBoletoProcessoSeletivo_codigo;
	private Integer modeloBoletoRenegociacao_codigo;
	private Integer modeloBoletoOutros_codigo;
	private Integer modeloBoletoBiblioteca_codigo;
	private Boolean utilizarDadosMatrizBoleto;
	private Boolean contaCorrente_carteiraRegistrada;
	private Boolean condicao_aplicarCalculoComBaseDescontosCalculados;
	private List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaPlanoFinanceiroAlunoDescricaoDescontos;
	private HashMap<String, PlanoFinanceiroAlunoDescricaoDescontosVO> mapPlanoFinanceiroAlunoDescricaoDescontosVOs;
	private String contaReceber_tipoPessoa;
	private String contaReceber_diretorioImgLinhaDigitavel;
	private String totaisInstrucoesSuperior;
	private String totaisInstrucoesInferior;
	private BigDecimal contaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa;
	private BigDecimal contaReceber_valorIndiceReajuste;
	private Date dataVencimentoRemessaOnline;
	private Double valorAcrescimoRemessaOnline;
	private ContaCorrenteVO contaCorrenteVO;
	private ContaReceberVO contaReceberVO;
	private String contaCorrenteCnab;
	private Boolean parceiroIsentarMulta;
	private Boolean parceiroIsentarJuro;

	public void calcularDatasDescontoProgressivo(BoletoBancarioRelVO obj) throws Exception {
		obj.setDataDescProg1(Uteis.obterDataFuturaMesContabilConsiderandoDiaInicial(obj.getContareceber_datavencimento(), -(obj.getDescontoprogressivo_dialimite1() - 1)));
		if (obj.getDescontoprogressivo_dialimite2() != null) {
			obj.setDataDescProg2(Uteis.obterDataFuturaMesContabilConsiderandoDiaInicial(obj.getContareceber_datavencimento(), -(obj.getDescontoprogressivo_dialimite2() - 1)));
		}
		if (obj.getDescontoprogressivo_dialimite3() != null) {
			obj.setDataDescProg3(Uteis.obterDataFuturaMesContabilConsiderandoDiaInicial(obj.getContareceber_datavencimento(), -(obj.getDescontoprogressivo_dialimite3() - 1)));
		}
		if (obj.getDescontoprogressivo_dialimite4() != null) {
			obj.setDataDescProg4(Uteis.obterDataFuturaMesContabilConsiderandoDiaInicial(obj.getContareceber_datavencimento(), -(obj.getDescontoprogressivo_dialimite4() - 1)));
		}
	}

	public void calcularValorDescontoProgressivo(BoletoBancarioRelVO obj) {
		obj.setValorDescProg1((obj.getContareceber_valor() * obj.getDescontoprogressivo_percdescontolimite1()) / 100.0);
		if (obj.getDescontoprogressivo_percdescontolimite2() != 0) {
			obj.setValorDescProg2((obj.getContareceber_valor() * obj.getDescontoprogressivo_percdescontolimite2()) / 100.0);
		}
		if (obj.getDescontoprogressivo_percdescontolimite3() != 0) {
			obj.setValorDescProg3((obj.getContareceber_valor() * obj.getDescontoprogressivo_percdescontolimite3()) / 100.0);
		}
		if (obj.getDescontoprogressivo_percdescontolimite4() != 0) {
			obj.setValorDescProg4((obj.getContareceber_valor() * obj.getDescontoprogressivo_percdescontolimite4()) / 100.0);
		}
		calcularTotalDescontos(obj);
	}

	public void calcularValorFinalDocumento(BoletoBancarioRelVO obj) {
		obj.setTotal1(obj.getContareceber_valor() - obj.getDescontoTotal1());
		if (obj.getDescontoTotal2() != 0) {
			obj.setTotal2(obj.getContareceber_valor() - obj.getDescontoTotal2());
		}
		if (obj.getDescontoTotal3() != 0) {
			obj.setTotal3(obj.getContareceber_valor() - obj.getDescontoTotal3());
		}
		if (obj.getDescontoTotal4() != 0) {
			obj.setTotal4(obj.getContareceber_valor() - obj.getDescontoTotal4());
		}
	}

	public void calcularTotalDescontos(BoletoBancarioRelVO obj) {
		obj.setDescontoTotal1(obj.getValorDescProg1() + obj.getContareceber_valordesconto() + obj.getContareceber_descontoinstituica() + obj.getContareceber_descontoconvenio());
		if (obj.getValorDescProg2() != 0) {
			obj.setDescontoTotal2(obj.getValorDescProg2() + obj.getContareceber_valordesconto() + obj.getContareceber_descontoinstituica() + obj.getContareceber_descontoconvenio());
		}
		if (obj.getValorDescProg3() != 0) {
			obj.setDescontoTotal3(obj.getValorDescProg3() + obj.getContareceber_valordesconto() + obj.getContareceber_descontoinstituica() + obj.getContareceber_descontoconvenio());
		}
		if (obj.getValorDescProg4() != 0) {
			obj.setDescontoTotal4(obj.getValorDescProg4() + obj.getContareceber_valordesconto() + obj.getContareceber_descontoinstituica() + obj.getContareceber_descontoconvenio());
		}
		calcularValorFinalDocumento(obj);
	}

	/**
	 * @param obj
	 * @throws Exception
	 * @deprecated 27-09-2010
	 */
	public void gerarIntrucoesPagamento(BoletoBancarioRelVO obj) throws Exception {
		calcularDatasDescontoProgressivo(obj);
		calcularValorDescontoProgressivo(obj);
		obj.setPagamento1Parte1("Até " + Uteis.getData(obj.getDataDescProg1()));
		obj.setPagamento1Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(obj.getDescontoTotal1()));
		obj.setPagamento1Parte3(" (Descontos: Prog. " + Uteis.arrendondarForcando2CadasDecimais(obj.getDescontoprogressivo_percdescontolimite1()) + "% = " + Uteis.formatarDoubleParaMoeda(obj.getValorDescProg1()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(obj.getContareceber_valordesconto()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(obj.getContareceber_descontoinstituica()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(obj.getContareceber_descontoconvenio()) + ")");
		obj.setPagamento1Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(obj.getTotal1()));
		if (obj.getDescontoprogressivo_dialimite2() != 0) {
			obj.setPagamento2Parte1("Até " + Uteis.getData(obj.getDataDescProg2()));
			obj.setPagamento2Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(obj.getDescontoTotal2()));
			obj.setPagamento2Parte3(" (Descontos: Prog. " + Uteis.arrendondarForcando2CadasDecimais(obj.getDescontoprogressivo_percdescontolimite2()) + "% = " + Uteis.formatarDoubleParaMoeda(obj.getValorDescProg2()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(obj.getContareceber_valordesconto()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(obj.getContareceber_descontoinstituica()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(obj.getContareceber_descontoconvenio()) + ")");
			obj.setPagamento2Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(obj.getTotal2()));
		}
		if (obj.getDescontoprogressivo_dialimite3() != 0) {
			obj.setPagamento3Parte1("Até " + Uteis.getData(obj.getDataDescProg3()));
			obj.setPagamento3Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(obj.getDescontoTotal3()));
			obj.setPagamento3Parte3(" (Descontos: Prog. " + Uteis.arrendondarForcando2CadasDecimais(obj.getDescontoprogressivo_percdescontolimite3()) + "% = " + Uteis.formatarDoubleParaMoeda(obj.getValorDescProg3()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(obj.getContareceber_valordesconto()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(obj.getContareceber_descontoinstituica()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(obj.getContareceber_descontoconvenio()) + ")");
			obj.setPagamento3Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(obj.getTotal3()));
		}
		if (obj.getDescontoprogressivo_dialimite4() != 0) {
			obj.setPagamento4Parte1("Até " + Uteis.getData(obj.getDataDescProg4()));
			obj.setPagamento4Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(obj.getDescontoTotal4()));
			obj.setPagamento4Parte3(" (Descontos: Prog. " + Uteis.arrendondarForcando2CadasDecimais(obj.getDescontoprogressivo_percdescontolimite4()) + "% = " + Uteis.formatarDoubleParaMoeda(obj.getValorDescProg4()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(obj.getContareceber_valordesconto()) + "    Inst = " + Uteis.formatarDoubleParaMoeda(obj.getContareceber_descontoinstituica()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(obj.getContareceber_descontoconvenio()) + ")");
			obj.setPagamento4Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(obj.getTotal4()));
		}
	}

	private void preencherInstrucoesBoletoPorDescricaoPersonalizado(BoletoBancarioRelVO obj, PlanoFinanceiroAlunoDescricaoDescontosVO p, Double descontoAluno, Boolean matricula, int i, TipoInstrucaoBoletoEnum tipoInstrucaoBoletoEnum) {
		String dataLimiteAplicacaoDesconto_Apresentar = null; 
		String valorTotalDesconto = null;
		if(tipoInstrucaoBoletoEnum.isSemDescontoProgressivoComValidadeSemValoresDiferentes()){
			valorTotalDesconto = Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorCusteadoContaReceber() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue());
			dataLimiteAplicacaoDesconto_Apresentar = "Até" + Uteis.getData(obj.getContareceber_datavencimento()) + "";
		}else if(tipoInstrucaoBoletoEnum.isSemDescontoProgressivoSemValidadeComValoresDiferentes()){
			valorTotalDesconto = Uteis.formatarDoubleParaMoeda(p.getValorDescontoProgressivo() + descontoAluno + p.getValorDescontoInstituicao() + p.getValorDescontoConvenio() + p.getValorDescontoRateio());
			dataLimiteAplicacaoDesconto_Apresentar = "";
		}else if(tipoInstrucaoBoletoEnum.isSemDescontoProgressivoSemValidadeSemValoresDiferentes() || tipoInstrucaoBoletoEnum.isPlanoDescontoSemValoresDiferente()){
			valorTotalDesconto = Uteis.formatarDoubleParaMoeda(p.getValorDescontoProgressivo() + descontoAluno + p.getValorDescontoInstituicao() + p.getValorDescontoConvenio() + p.getValorDescontoRateio());
			dataLimiteAplicacaoDesconto_Apresentar = "Até" + Uteis.getData(obj.getContareceber_datavencimento()) + "";
		}else{
			valorTotalDesconto = Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorCusteadoContaReceber() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue());
			dataLimiteAplicacaoDesconto_Apresentar = p.getDataLimiteAplicacaoDesconto_Apresentar();
		}
		String percDescontoProgressivo = Uteis.formatarDecimal(p.getPercentualDescontoProgressivo(), "#0.0000");
		String valorDescontoProgressivo = Uteis.formatarDoubleParaMoeda(p.getValorDescontoProgressivo());
		String valorDescontoAluno = Uteis.formatarDoubleParaMoeda(descontoAluno);
		String valorDescontoInstituicao = Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao());
		String valorDescontoConvenio = Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio());
		String valorDescontoRateio = Uteis.formatarDoubleParaMoeda(p.getValorDescontoRateio());
		String valorTotal = Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo()));
		if (i == 0) {
			obj.setModeloboleto_instrucao1(new ModeloBoletoVO().substituirTags(obj.getModeloboleto_instrucao1(), dataLimiteAplicacaoDesconto_Apresentar, valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, obj.getCodigoAdministrativo(), valorDescontoRateio));
			obj.setModeloboleto_instrucao1Inferior(new ModeloBoletoVO().substituirTags(obj.getModeloboleto_instrucao1Inferior(), dataLimiteAplicacaoDesconto_Apresentar, valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, obj.getCodigoAdministrativo(), valorDescontoRateio));
		} else if (i == 1) {
			obj.setModeloboleto_instrucao2(new ModeloBoletoVO().substituirTags(obj.getModeloboleto_instrucao2(), dataLimiteAplicacaoDesconto_Apresentar, valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, obj.getCodigoAdministrativo(), valorDescontoRateio));
			obj.setModeloboleto_instrucao2Inferior(new ModeloBoletoVO().substituirTags(obj.getModeloboleto_instrucao2Inferior(), dataLimiteAplicacaoDesconto_Apresentar, valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, obj.getCodigoAdministrativo(), valorDescontoRateio));
		} else if (i == 2) {
			obj.setModeloboleto_instrucao3(new ModeloBoletoVO().substituirTags(obj.getModeloboleto_instrucao3(), dataLimiteAplicacaoDesconto_Apresentar, valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, obj.getCodigoAdministrativo(), valorDescontoRateio));
			obj.setModeloboleto_instrucao3Inferior(new ModeloBoletoVO().substituirTags(obj.getModeloboleto_instrucao3Inferior(), dataLimiteAplicacaoDesconto_Apresentar, valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, obj.getCodigoAdministrativo(), valorDescontoRateio));
		} else if (i == 3) {
			obj.setModeloboleto_instrucao4(new ModeloBoletoVO().substituirTags(obj.getModeloboleto_instrucao4(), dataLimiteAplicacaoDesconto_Apresentar, valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, obj.getCodigoAdministrativo(), valorDescontoRateio));
			obj.setModeloboleto_instrucao4Inferior(new ModeloBoletoVO().substituirTags(obj.getModeloboleto_instrucao4Inferior(), dataLimiteAplicacaoDesconto_Apresentar, valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, obj.getCodigoAdministrativo(), valorDescontoRateio));
		} else if (i == 4) {
			obj.setModeloboleto_instrucao5(new ModeloBoletoVO().substituirTags(obj.getModeloboleto_instrucao5(), dataLimiteAplicacaoDesconto_Apresentar, valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, obj.getCodigoAdministrativo(), valorDescontoRateio));
			obj.setModeloboleto_instrucao5Inferior(new ModeloBoletoVO().substituirTags(obj.getModeloboleto_instrucao5Inferior(), dataLimiteAplicacaoDesconto_Apresentar, valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, obj.getCodigoAdministrativo(), valorDescontoRateio));
		} else if (i == 5) {
			obj.setModeloboleto_instrucao6(new ModeloBoletoVO().substituirTags(obj.getModeloboleto_instrucao6(), dataLimiteAplicacaoDesconto_Apresentar, valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, obj.getCodigoAdministrativo(), valorDescontoRateio));
			obj.setModeloboleto_instrucao6Inferior(new ModeloBoletoVO().substituirTags(obj.getModeloboleto_instrucao6Inferior(), dataLimiteAplicacaoDesconto_Apresentar, valorTotalDesconto, percDescontoProgressivo, valorDescontoProgressivo, valorDescontoAluno, valorDescontoInstituicao, valorDescontoConvenio, valorTotal, obj.getCodigoAdministrativo(), valorDescontoRateio));
		}
	}

	private void preencherInstrucoesBoletoSemPersonalidado(BoletoBancarioRelVO obj, Boolean matricula, PlanoFinanceiroAlunoDescricaoDescontosVO p, Double descontoAluno,  int i, TipoInstrucaoBoletoEnum tipoInstrucaoBoletoEnum) {
		Double valorDesc = p.getValorDescontoProgressivo() + descontoAluno + p.getValorDescontoInstituicao() + p.getValorDescontoConvenio() + p.getValorDescontoRateio();
		if (i == 0) {
			obj.setPagamento1Parte1(p.getDataLimiteAplicacaoDesconto_Apresentar());
			if (tipoInstrucaoBoletoEnum.isPlanoDescontoComValoresDiferente() || tipoInstrucaoBoletoEnum.isSemDescontoProgressivoSemValidadeComValoresDiferentes()) {
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					if (Uteis.isAtributoPreenchido(valorDesc)) {
						obj.setPagamento1Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
					} else {
						obj.setPagamento1Parte1("");
					}
				} else {
					obj.setPagamento1Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo())));
				}
			} else if (tipoInstrucaoBoletoEnum.isDesconto()) {
				obj.setPagamento1Parte3(" (Descontos: Prog. " + Uteis.formatarDecimal(p.getPercentualDescontoProgressivo(), "#0.0000") + "% = " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + " RAT= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoRateio()) + ")");
				obj.setPagamento1Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorCusteadoContaReceber() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue()));
				obj.setPagamento1Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo())));
			} else if (tipoInstrucaoBoletoEnum.isSemDescontoProgressivoComValidadeComValoresDiferentes()) {
				obj.setPagamento1Parte3(" (Descontos: Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + "  RAT= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoRateio()) + ")");
				obj.setPagamento1Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorCusteadoContaReceber() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue()));
				obj.setPagamento1Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo())));
			} else if (tipoInstrucaoBoletoEnum.isSemDescontoProgressivoComValidadeSemValoresDiferentes()) {
				obj.setPagamento1Parte1("Até " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				obj.setPagamento1Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorCusteadoContaReceber() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue()));
				obj.setPagamento1Parte3(" (Descontos: Aluno = " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + "  RAT= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoRateio()) + ")");
				obj.setPagamento1Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo() )));
			} else if (tipoInstrucaoBoletoEnum.isSemDescontoProgressivoSemValidadeSemValoresDiferentes() || tipoInstrucaoBoletoEnum.isPlanoDescontoSemValoresDiferente()) {
				obj.setPagamento1Parte1("Até " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					if (Uteis.isAtributoPreenchido(valorDesc)) {
						obj.setPagamento1Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
					} else {
						obj.setPagamento1Parte1("");
					}
				} else {
					obj.setPagamento1Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula)+getContareceber_totalAcrescimo() )));
				}
			}
		} else if (i == 1) {
			obj.setPagamento2Parte1(p.getDataLimiteAplicacaoDesconto_Apresentar());
			if (tipoInstrucaoBoletoEnum.isPlanoDescontoComValoresDiferente() || tipoInstrucaoBoletoEnum.isSemDescontoProgressivoSemValidadeComValoresDiferentes()) {
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					if (Uteis.isAtributoPreenchido(valorDesc)) {
						obj.setPagamento2Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
					} else {
						obj.setPagamento2Parte1("");
					}
				} else {
					obj.setPagamento2Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo())));
				}
			} else if (tipoInstrucaoBoletoEnum.isDesconto()) {
				obj.setPagamento2Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorCusteadoContaReceber() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue()));
				obj.setPagamento2Parte3(" (Descontos: Prog. " + Uteis.formatarDecimal(p.getPercentualDescontoProgressivo(), "#0.0000") + "% = " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + " RAT= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoRateio()) + ")");
				obj.setPagamento2Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo())));
			} else if (tipoInstrucaoBoletoEnum.isSemDescontoProgressivoComValidadeComValoresDiferentes()) {
				obj.setPagamento2Parte2(" Desconto = " +  Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorCusteadoContaReceber() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue()));
				obj.setPagamento2Parte3(" (Descontos: Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + "  RAT= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoRateio()) + ")");
				obj.setPagamento2Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo())));
			} 
		} else if (i == 2) {
			obj.setPagamento3Parte1(p.getDataLimiteAplicacaoDesconto_Apresentar());
			if (tipoInstrucaoBoletoEnum.isPlanoDescontoComValoresDiferente() || tipoInstrucaoBoletoEnum.isSemDescontoProgressivoSemValidadeComValoresDiferentes()) {
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					if (Uteis.isAtributoPreenchido(valorDesc)) {
						obj.setPagamento3Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
					} else {
						obj.setPagamento3Parte1("");
					}
				} else {
					obj.setPagamento3Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo())));
				}
			} else if (tipoInstrucaoBoletoEnum.isDesconto()) {
				obj.setPagamento3Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorCusteadoContaReceber() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue()));
				obj.setPagamento3Parte3(" (Descontos: Prog. " + Uteis.formatarDecimal(p.getPercentualDescontoProgressivo(), "#0.0000") + "% = " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + " RAT= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoRateio()) + ")");
				obj.setPagamento3Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo() )));
			} else if (tipoInstrucaoBoletoEnum.isSemDescontoProgressivoComValidadeComValoresDiferentes()) {
				obj.setPagamento3Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorCusteadoContaReceber() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue()));
				obj.setPagamento3Parte3(" (Descontos: Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + "  RAT= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoRateio()) + ")");
				obj.setPagamento3Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo() )));
			} 
		} else if (i == 3) {
			obj.setPagamento4Parte1(p.getDataLimiteAplicacaoDesconto_Apresentar());
			if (tipoInstrucaoBoletoEnum.isPlanoDescontoComValoresDiferente() || tipoInstrucaoBoletoEnum.isSemDescontoProgressivoSemValidadeComValoresDiferentes()) {
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					if (Uteis.isAtributoPreenchido(valorDesc)) {
						obj.setPagamento4Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
					} else {
						obj.setPagamento4Parte1("");
					}
				} else {
					obj.setPagamento4Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo())));
				}
			} else if (tipoInstrucaoBoletoEnum.isDesconto()) {
				obj.setPagamento4Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorCusteadoContaReceber() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue()));
				obj.setPagamento4Parte3(" (Descontos: Prog. " + Uteis.formatarDecimal(p.getPercentualDescontoProgressivo(), "#0.0000") + "% = " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + " RAT= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoRateio()) + ")");
				obj.setPagamento4Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo())));
			} else if (tipoInstrucaoBoletoEnum.isSemDescontoProgressivoComValidadeComValoresDiferentes()) {
				obj.setPagamento4Parte2(" Desconto = " +  Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorCusteadoContaReceber() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue()));
				obj.setPagamento4Parte3(" (Descontos: Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + "  RAT= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoRateio()) + ")");
				obj.setPagamento4Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo())));
			} 
		} else if (i == 4) {
			obj.setPagamento5Parte1(p.getDataLimiteAplicacaoDesconto_Apresentar());
			if (tipoInstrucaoBoletoEnum.isPlanoDescontoComValoresDiferente() || tipoInstrucaoBoletoEnum.isSemDescontoProgressivoSemValidadeComValoresDiferentes()) {
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					if (Uteis.isAtributoPreenchido(valorDesc)) {
						obj.setPagamento5Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
					} else {
						obj.setPagamento5Parte1("");
					}
				} else {
					obj.setPagamento5Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo())));
				}
			} else if (tipoInstrucaoBoletoEnum.isDesconto()) {
				obj.setPagamento5Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorCusteadoContaReceber() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue()));
				obj.setPagamento5Parte3(" (Descontos: Prog. " + Uteis.formatarDecimal(p.getPercentualDescontoProgressivo(), "#0.0000") + "% = " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + " RAT= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoRateio()) + ")");
				obj.setPagamento5Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo())));
			} else if (tipoInstrucaoBoletoEnum.isSemDescontoProgressivoComValidadeComValoresDiferentes()) {
				obj.setPagamento5Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorCusteadoContaReceber() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue()));
				obj.setPagamento5Parte3(" (Descontos: Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + "  RAT= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoRateio()) + ")");
				obj.setPagamento5Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo())));
			} 
		} else if (i == 5) {
			obj.setPagamento6Parte1(p.getDataLimiteAplicacaoDesconto_Apresentar());
			if (tipoInstrucaoBoletoEnum.isPlanoDescontoComValoresDiferente() || tipoInstrucaoBoletoEnum.isSemDescontoProgressivoSemValidadeComValoresDiferentes()) {
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					if (Uteis.isAtributoPreenchido(valorDesc)) {
						obj.setPagamento6Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
					} else {
						obj.setPagamento6Parte1("");
					}
				} else {
					obj.setPagamento6Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo())));
				}
			} else if (tipoInstrucaoBoletoEnum.isDesconto()) {
				obj.setPagamento6Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorCusteadoContaReceber() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue()));
				obj.setPagamento6Parte3(" (Descontos: Prog. " + Uteis.formatarDecimal(p.getPercentualDescontoProgressivo(), "#0.0000") + "% = " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + " RAT= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoRateio()) + ")");
				obj.setPagamento6Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo())));
			} else if (tipoInstrucaoBoletoEnum.isSemDescontoProgressivoComValidadeComValoresDiferentes()) {
				obj.setPagamento6Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorCusteadoContaReceber() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue()));
				obj.setPagamento6Parte3(" (Descontos: Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + "  RAT= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoRateio()) + ")");
				obj.setPagamento6Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo())));
			} 
		}
	}

	public void limparInstrucoesNaoUtilizadas(BoletoBancarioRelVO obj, List listaPlanoFinanceiroAlunoDescricaoDescontos) {
		boolean limpar = true;
		for (int i = 6; i > 0; i--) {
			if (i <= listaPlanoFinanceiroAlunoDescricaoDescontos.size()) {
				PlanoFinanceiroAlunoDescricaoDescontosVO desconto = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(i - 1);
				limpar = desconto.getValorBase() - desconto.getValorParaPagamentoDentroDataLimiteDesconto(obj.getContareceber_tipoorigem().equals("MAT")) == 0 || desconto.getApresentarDescricaoBoleto();
			}
			if (i == 6 && limpar) {
				obj.setModeloboleto_instrucao6("");
				obj.setModeloboleto_instrucao6Inferior("");
			} else if (i == 5 && limpar) {
				obj.setModeloboleto_instrucao5("");
				obj.setModeloboleto_instrucao5Inferior("");
			} else if (i == 4 && limpar) {
				obj.setModeloboleto_instrucao4("");
				obj.setModeloboleto_instrucao4Inferior("");
			} else if (i == 3 && limpar) {
				obj.setModeloboleto_instrucao3("");
				obj.setModeloboleto_instrucao3Inferior("");
			} else if (i == 2 && limpar) {
				obj.setModeloboleto_instrucao2("");
				obj.setModeloboleto_instrucao2Inferior("");
			} else if (i == 1 && limpar) {
				obj.setModeloboleto_instrucao1("");
				obj.setModeloboleto_instrucao1Inferior("");
			}
		}
	}
	
	private void validarUtilizacaoDescontoPersonalizado(BoletoBancarioRelVO obj, Boolean matricula, int i, PlanoFinanceiroAlunoDescricaoDescontosVO p, Double descontoAluno, TipoInstrucaoBoletoEnum tipoInstrucaoBoletoEnum) {
		if (!obj.getModeloboleto_utilizarDescricaoDescontoPersonalizado()) {
			preencherInstrucoesBoletoSemPersonalidado(obj, matricula, p, descontoAluno, i, tipoInstrucaoBoletoEnum);
		} else {
			preencherInstrucoesBoletoPorDescricaoPersonalizado(obj, p, descontoAluno, matricula, i, tipoInstrucaoBoletoEnum);
		}
	}
	
	private void validarTextoRodapeInstrucao(BoletoBancarioRelVO obj, ConfiguracaoFinanceiroVO conf, Double valorUtilizadoParaRealizarCalculoJuroOuMulta) {
		Optional<BancoEnum> optionalBancoEnum = Optional.ofNullable(BancoEnum.getEnum(getContaCorrenteCnab(), getBanco_nrbanco()));
		boolean permiteCalcularMultaSobreValorCheio = optionalBancoEnum.isPresent() && optionalBancoEnum.map(BancoEnum::getPermiteInformarValorMultaParaRemessa).orElse(Boolean.FALSE);
		calcularMultaJuroBoletoBancarioRelVO(obj, conf, valorUtilizadoParaRealizarCalculoJuroOuMulta, permiteCalcularMultaSobreValorCheio);

		if (conf.getCobrarJuroMultaSobreValorCheioConta()) {
			obj.setModeloboleto_textoRodape(Uteis.substituirTagCasoExista(obj.getModeloboleto_textoRodape(), obj.getContareceber_valor(), obj.getContareceber_multa(), obj.getContareceber_juro()));
			obj.setModeloboleto_textoRodapeInferior(Uteis.substituirTagCasoExista(obj.getModeloboleto_textoRodapeInferior(), obj.getContareceber_valor(), obj.getContareceber_multa(), obj.getContareceber_juro()));
			obj.setContareceber_descricaopagamento(Uteis.substituirTagCasoExista(obj.getContareceber_descricaopagamento(), obj.getContareceber_valor(), obj.getContareceber_multa(), obj.getContareceber_juro()));
		} else {
			obj.setModeloboleto_textoRodape(Uteis.substituirTagCasoExista(obj.getModeloboleto_textoRodape(), valorUtilizadoParaRealizarCalculoJuroOuMulta, obj.getContareceber_multa(), obj.getContareceber_juro()));
			obj.setModeloboleto_textoRodapeInferior(Uteis.substituirTagCasoExista(obj.getModeloboleto_textoRodapeInferior(), valorUtilizadoParaRealizarCalculoJuroOuMulta, obj.getContareceber_multa(), obj.getContareceber_juro()));
			obj.setContareceber_descricaopagamento(Uteis.substituirTagCasoExista(obj.getContareceber_descricaopagamento(), valorUtilizadoParaRealizarCalculoJuroOuMulta, obj.getContareceber_multa(), obj.getContareceber_juro()));
		}
	}

	public void gerarIntrucoesDescontos(BoletoBancarioRelVO obj, List listaPlanoFinanceiroAlunoDescricaoDescontos, List<PlanoDescontoVO> listaPlanoDesconto, ConfiguracaoFinanceiroVO conf) throws Exception {
		Boolean matricula = obj.getContareceber_tipoorigem().equals("MAT"); // aqui...
		for (Iterator<PlanoFinanceiroAlunoDescricaoDescontosVO> iterator = ((List<PlanoFinanceiroAlunoDescricaoDescontosVO>) listaPlanoFinanceiroAlunoDescricaoDescontos).iterator(); iterator.hasNext();) {
			PlanoFinanceiroAlunoDescricaoDescontosVO objdesc = iterator.next();
			if (objdesc.executarCalculoValorTotalDesconto().equals(0.0) && !objdesc.getReferentePlanoFinanceiroAposVcto()) {
				iterator.remove();
			}
		}
		Double valorPagarParaJuroMulta = 0.0;
		for (int i = 0; i < listaPlanoFinanceiroAlunoDescricaoDescontos.size(); i++) {
			PlanoFinanceiroAlunoDescricaoDescontosVO p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(i);

			if (p.getDiaNrAntesVencimento().equals(0) && i == listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 2 && ((PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 1)).getReferentePlanoFinanceiroAposVcto()) {
				p.setDiaNrAntesVencimento(1);
			}
			if (p.getDiaNrAntesVencimento().equals(0) && p.getValorDescontoAluno() > 0 && obj.getDescontoValidoatedataparcela()) {
				if (p.getReferentePlanoFinanceiroAposVcto()) {
					p.setValorDescontoAluno(0.0);
				} else {
					p.setDiaNrAntesVencimento(1);
				}
			}
			if (i == 0) {
				validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.DESCONTO);
			} else if ((i == 1 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) || i == 1 && p.getApresentarDescricaoBoleto()) {
				validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.DESCONTO);
			} else if ((i == 2 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) || i == 2 && p.getApresentarDescricaoBoleto()) {
				validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.DESCONTO);
			} else if ((i == 3 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) || i == 3 && p.getApresentarDescricaoBoleto()) {
				validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.DESCONTO);
			} else if ((i == 4 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) || i == 4 && p.getApresentarDescricaoBoleto()) {
				validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.DESCONTO);
			} else if ((i == 5 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) || i == 5 && p.getApresentarDescricaoBoleto()) {
				validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.DESCONTO);
			}
			valorPagarParaJuroMulta = Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula)+getContareceber_totalAcrescimo());
		}
		if(listaPlanoFinanceiroAlunoDescricaoDescontos.isEmpty() && valorPagarParaJuroMulta == 0.0){
			valorPagarParaJuroMulta = Uteis.arrendondarForcando2CadasDecimais(getContareceber_valorBaseContaReceber()+getContareceber_totalAcrescimo());	
		}
		validarTextoRodapeInstrucao(obj, conf, valorPagarParaJuroMulta);
		limparInstrucoesNaoUtilizadas(obj, listaPlanoFinanceiroAlunoDescricaoDescontos);
	}	

	public void gerarInstrucaoPlanoDesconto(BoletoBancarioRelVO obj, List listaPlanoFinanceiroAlunoDescricaoDescontos, List<PlanoDescontoVO> listaPlanoDesconto, Boolean eMatricula, ConfiguracaoFinanceiroVO conf) throws Exception {

		Boolean matricula = obj.getContareceber_tipoorigem().equals("MAT");
		PlanoFinanceiroAlunoDescricaoDescontosVO p = null;
		String valor = null;
		Boolean valoresDiferentes = false;
		Boolean descontoPlanoDescontoValidoAteDataVencimento = false;

		for (PlanoDescontoVO planoDescontoVO : listaPlanoDesconto) {
			if (planoDescontoVO.getDescontoValidoAteDataVencimento()) {
				descontoPlanoDescontoValidoAteDataVencimento = Boolean.TRUE;
				break;
			}
		}
		Double valorPagarParaJuroMulta = 0.0;
		if (listaPlanoFinanceiroAlunoDescricaoDescontos != null) {
			for (int i = 0; i < listaPlanoFinanceiroAlunoDescricaoDescontos.size(); i++) {
				p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(i);
				if (valor == null) {
					valor = Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue());
				} else {
					if (!valor.equals(Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue()))) {
						valoresDiferentes = true;
						i = listaPlanoFinanceiroAlunoDescricaoDescontos.size();
					}
				}
			}
			if (valoresDiferentes) {
				for (int i = 0; i < listaPlanoFinanceiroAlunoDescricaoDescontos.size(); i++) {
					p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(i);

					if (p.getDiaNrAntesVencimento().equals(0) && i == listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 1 && descontoPlanoDescontoValidoAteDataVencimento) {
						p.setDiaNrAntesVencimento(1);
					}
					/**
					 * Adicionada regra caso no planoFinanceiroAluno esteja marcado como descontoValidoAteDataParcela zere o valor do desconto do aluno, devido a existir uma insconsistência no ato de realizar os cálculos financeiros da ContaReceber e o mesmo não poder ser solucionado através da passagem deste parâmetro do PlanoFinanceiroAluno.
					 */
					if (p.getDiaNrAntesVencimento().equals(0) && p.getValorDescontoAluno() > 0 && obj.getDescontoValidoatedataparcela()) {
						if (p.getReferentePlanoFinanceiroAposVcto()) {
							p.setValorDescontoAluno(0.0);
						} else {
							p.setDiaNrAntesVencimento(1);
						}
					}					
					if (i == 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.PLANO_DESCONTO_COM_VALORES_DIFERENTE);
					} else if (i == 1 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.PLANO_DESCONTO_COM_VALORES_DIFERENTE);
					} else if (i == 2 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.PLANO_DESCONTO_COM_VALORES_DIFERENTE);
					} else if (i == 3 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.PLANO_DESCONTO_COM_VALORES_DIFERENTE);
					} else if (i == 4 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.PLANO_DESCONTO_COM_VALORES_DIFERENTE);
					} else if (i == 5 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.PLANO_DESCONTO_COM_VALORES_DIFERENTE);
					}
					valorPagarParaJuroMulta = Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula)+getContareceber_totalAcrescimo());
				}
			} else {
				if ((descontoPlanoDescontoValidoAteDataVencimento || obj.getDescontoValidoatedataparcela()) && !listaPlanoFinanceiroAlunoDescricaoDescontos.isEmpty()) {
					p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(0);
					validarUtilizacaoDescontoPersonalizado(obj, matricula, 0, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.PLANO_DESCONTO_SEM_VALORES_DIFERENTE);
				} else if (!listaPlanoFinanceiroAlunoDescricaoDescontos.isEmpty()) {
					p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(0);
					validarUtilizacaoDescontoPersonalizado(obj, matricula, 0, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.PLANO_DESCONTO_SEM_VALORES_DIFERENTE);
					obj.setPagamento1Parte1("");
				}
				valorPagarParaJuroMulta = Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula)+getContareceber_totalAcrescimo());
				
			}
			validarTextoRodapeInstrucao(obj, conf, valorPagarParaJuroMulta);
			limparInstrucoesNaoUtilizadas(obj, listaPlanoFinanceiroAlunoDescricaoDescontos);
		}
	}
	
	
	public void gerarIntrucoesDescontosSemDescontoProgressivo(BoletoBancarioRelVO obj, List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaPlanoFinanceiroAlunoDescricaoDescontos, List<PlanoDescontoVO> listaPlanoDesconto, Boolean gerarBoletoComDescontoSemValidade, ConfiguracaoFinanceiroVO conf) throws Exception {
		Boolean matricula = obj.getContareceber_tipoorigem().equals("MAT");
		PlanoFinanceiroAlunoDescricaoDescontosVO p = null;
		String valor = null;
		Boolean valoresDiferentes = false;
		Boolean descontoPlanoDescontoValidoAteDataVencimento = false;

		for (PlanoDescontoVO planoDescontoVO : listaPlanoDesconto) {
			if (planoDescontoVO.getDescontoValidoAteDataVencimento()) {
				descontoPlanoDescontoValidoAteDataVencimento = Boolean.TRUE;
				break;
			}
		}

		if (!descontoPlanoDescontoValidoAteDataVencimento) {
			if (!listaPlanoFinanceiroAlunoDescricaoDescontos.isEmpty() && (!listaPlanoFinanceiroAlunoDescricaoDescontos.get(0).getReferentePlanoFinanceiroAposVcto() 
					&& (!listaPlanoFinanceiroAlunoDescricaoDescontos.get(0).getReferentePlanoFinanceiroAteVencimento() 
					&& listaPlanoFinanceiroAlunoDescricaoDescontos.get(0).getDiaNrAntesVencimento() != null 
					&& listaPlanoFinanceiroAlunoDescricaoDescontos.get(0).getDiaNrAntesVencimento() > 0))) {
				descontoPlanoDescontoValidoAteDataVencimento = listaPlanoFinanceiroAlunoDescricaoDescontos.get(0).getDataLimiteAplicacaoDesconto().equals(obj.getContareceber_datavencimento());
			}
		}

		for (int i = 0; i < listaPlanoFinanceiroAlunoDescricaoDescontos.size(); i++) {
			p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(i);
			if (valor == null) {
				valor = Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue());
			} else {
				if (!valor.equals(Uteis.formatarDoubleParaMoeda(p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue()))) {
					valoresDiferentes = true;
					i = listaPlanoFinanceiroAlunoDescricaoDescontos.size();
				}
			}
		}
		for (Iterator<PlanoFinanceiroAlunoDescricaoDescontosVO> iterator = ((List<PlanoFinanceiroAlunoDescricaoDescontosVO>) listaPlanoFinanceiroAlunoDescricaoDescontos).iterator(); iterator.hasNext();) {
			PlanoFinanceiroAlunoDescricaoDescontosVO objdesc = iterator.next();
			if (objdesc.executarCalculoValorTotalDesconto().equals(0.0) && !objdesc.getReferentePlanoFinanceiroAposVcto()) {
				iterator.remove();
			}
		}
		Double valorPagarParaJuroMulta = 0.0;
		if (!gerarBoletoComDescontoSemValidade) {
			if (valoresDiferentes) {
				for (int i = 0; i < listaPlanoFinanceiroAlunoDescricaoDescontos.size(); i++) {
					p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(i);
					if (p.getDiaNrAntesVencimento().equals(0) && i == listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 2 && ((PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 1)).getReferentePlanoFinanceiroAposVcto()) {
						p.setDiaNrAntesVencimento(1);
					}
					/**
					 * Adicionada regra caso no planoFinanceiroAluno esteja marcado como descontoValidoAteDataParcela zere o valor do desconto do aluno, devido a existir uma insconsistência no ato de realizar os cálculos financeiros da ContaReceber e o mesmo não poder ser solucionado através da passagem deste parâmetro do PlanoFinanceiroAluno.
					 */
					if (p.getDiaNrAntesVencimento().equals(0) && p.getValorDescontoAluno() > 0 && obj.getDescontoValidoatedataparcela()) {
						if (p.getReferentePlanoFinanceiroAposVcto()) {
							p.setValorDescontoAluno(0.0);
						} else {
							p.setDiaNrAntesVencimento(1);
						}
					}					
					if (i == 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_COM_VALIDADE_COM_VALORES_DIFERENTE);
					} else if (i == 1 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_COM_VALIDADE_COM_VALORES_DIFERENTE);
					} else if (i == 2 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_COM_VALIDADE_COM_VALORES_DIFERENTE);
					} else if (i == 3 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_COM_VALIDADE_COM_VALORES_DIFERENTE);
					} else if (i == 4 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_COM_VALIDADE_COM_VALORES_DIFERENTE);
					} else if (i == 5 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p,p.getValorDescontoAluno(),  TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_COM_VALIDADE_COM_VALORES_DIFERENTE);
					}
					valorPagarParaJuroMulta = Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo());
				}
			} else {
				if (!listaPlanoFinanceiroAlunoDescricaoDescontos.isEmpty() && (descontoPlanoDescontoValidoAteDataVencimento || obj.getDescontoValidoatedataparcela() || ((PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(0)).getReferentePlanoFinanceiroAteVencimento())) {
					p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(0);
					validarUtilizacaoDescontoPersonalizado(obj, matricula, 0, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_COM_VALIDADE_SEM_VALORES_DIFERENTE);					
				} else if (!listaPlanoFinanceiroAlunoDescricaoDescontos.isEmpty()) {
					p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(0);
					validarUtilizacaoDescontoPersonalizado(obj, matricula, 0, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_COM_VALIDADE_SEM_VALORES_DIFERENTE);
					obj.setPagamento1Parte1("");
				}
				valorPagarParaJuroMulta = Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo());
			}
		} else {
			if (valoresDiferentes) {
				for (int i = 0; i < listaPlanoFinanceiroAlunoDescricaoDescontos.size(); i++) {
					p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(i);					
					if (p.getDiaNrAntesVencimento().equals(0) && i == listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 2 && ((PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(listaPlanoFinanceiroAlunoDescricaoDescontos.size() - 1)).getReferentePlanoFinanceiroAposVcto()) {
						p.setDiaNrAntesVencimento(1);
					}
					double descontoAluno = p.getValorDescontoAluno();
					/**
					 * Adicionada regra caso no planoFinanceiroAluno esteja marcado como descontoValidoAteDataParcela zere o valor do desconto do aluno, devido a existir uma insconsistência no ato de realizar os cálculos financeiros da ContaReceber e o mesmo não poder ser solucionado através da passagem deste parâmetro do PlanoFinanceiroAluno.
					 */
					if (p.getDiaNrAntesVencimento().equals(0) && p.getValorDescontoAluno() > 0 && obj.getDescontoValidoatedataparcela()) {
						if (p.getReferentePlanoFinanceiroAposVcto()) {
							descontoAluno = 0.0;
						} else {
							p.setDiaNrAntesVencimento(1);
						}
					}

					if (i == 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, descontoAluno, TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_SEM_VALIDADE_COM_VALORES_DIFERENTE);
					} else if (i == 1 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, descontoAluno, TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_SEM_VALIDADE_COM_VALORES_DIFERENTE);						
					} else if (i == 2 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, descontoAluno, TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_SEM_VALIDADE_COM_VALORES_DIFERENTE);
					} else if (i == 3 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, descontoAluno, TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_SEM_VALIDADE_COM_VALORES_DIFERENTE);
					} else if (i == 4 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, descontoAluno, TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_SEM_VALIDADE_COM_VALORES_DIFERENTE);
					} else if (i == 5 && p.getValorBase().doubleValue() - p.getValorParaPagamentoDentroDataLimiteDesconto(matricula).doubleValue() > 0) {
						validarUtilizacaoDescontoPersonalizado(obj, matricula, i, p, descontoAluno, TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_SEM_VALIDADE_COM_VALORES_DIFERENTE);
					}
					valorPagarParaJuroMulta = Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo());
				}
			} else if (!listaPlanoFinanceiroAlunoDescricaoDescontos.isEmpty()) {
				if (!descontoPlanoDescontoValidoAteDataVencimento || obj.getDescontoValidoatedataparcela()) {
					p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(0);
					validarUtilizacaoDescontoPersonalizado(obj, matricula, 0, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_SEM_VALIDADE_SEM_VALORES_DIFERENTE);
					obj.setPagamento1Parte1("");
				} else {
					p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(0);
					validarUtilizacaoDescontoPersonalizado(obj, matricula, 0, p, p.getValorDescontoAluno(), TipoInstrucaoBoletoEnum.SEM_DESCONTO_PROGRESSIVO_SEM_VALIDADE_SEM_VALORES_DIFERENTE);
				}
				valorPagarParaJuroMulta = Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(matricula) + getContareceber_totalAcrescimo());
			}
		}
		validarTextoRodapeInstrucao(obj, conf, valorPagarParaJuroMulta);

		limparInstrucoesNaoUtilizadas(obj, listaPlanoFinanceiroAlunoDescricaoDescontos);
	}

	

	public void gerarInstrucaoDescontoProgressivoDiaLimite1(BoletoBancarioRelVO obj, DescontoProgressivoVO descontoProgressivoVO, List listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade, List listaPlanoFinanceiroAlunoDescricaoDescontos, Boolean eMatricula) throws Exception {
		if (!descontoProgressivoVO.getDiaLimite1().equals(0)) {
			Double valorDescontoOriginal = 0.0;
			if (listaPlanoFinanceiroAlunoDescricaoDescontos != null) {
				PlanoFinanceiroAlunoDescricaoDescontosVO planoOriginal = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(0);
				valorDescontoOriginal = planoOriginal.getValorDescontoProgressivo();
			}
			PlanoFinanceiroAlunoDescricaoDescontosVO p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade.get(0);
			if (obj.getPagamento1Parte1().equals("") && obj.getPagamento1Parte2().equals("")) {
				obj.setPagamento1Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento1Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite1() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento1Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento1Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento2Parte1().equals("") && obj.getPagamento2Parte2().equals("")) {
				obj.setPagamento2Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento2Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite1() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento2Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento2Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento3Parte1().equals("") && obj.getPagamento3Parte2().equals("")) {
				obj.setPagamento3Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento3Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite1() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento3Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento3Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento4Parte1().equals("") && obj.getPagamento4Parte2().equals("")) {
				obj.setPagamento4Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento4Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite1() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento4Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento4Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento5Parte1().equals("") && obj.getPagamento5Parte2().equals("")) {
				obj.setPagamento5Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento5Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite1() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento5Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento5Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento6Parte1().equals("") && obj.getPagamento6Parte2().equals("")) {
				obj.setPagamento6Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento6Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite1() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento6Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento6Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			}
		}
		return;
	}

	public void gerarInstrucaoDescontoProgressivoDiaLimite2(BoletoBancarioRelVO obj, DescontoProgressivoVO descontoProgressivoVO, List listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade, List listaPlanoFinanceiroAlunoDescricaoDescontos, Boolean eMatricula) throws Exception {
		if (!descontoProgressivoVO.getDiaLimite2().equals(0)) {
			Double valorDescontoOriginal = 0.0;
			if (listaPlanoFinanceiroAlunoDescricaoDescontos != null) {
				PlanoFinanceiroAlunoDescricaoDescontosVO planoOriginal = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(0);
				valorDescontoOriginal = planoOriginal.getValorDescontoProgressivo();
			}
			PlanoFinanceiroAlunoDescricaoDescontosVO p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade.get(0);
			if (obj.getPagamento1Parte1().equals("") && obj.getPagamento1Parte2().equals("")) {
				obj.setPagamento1Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento1Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite2() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento1Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento1Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento2Parte1().equals("") && obj.getPagamento2Parte2().equals("")) {
				obj.setPagamento2Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento2Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite2() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento2Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento2Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento3Parte1().equals("") && obj.getPagamento3Parte2().equals("")) {
				obj.setPagamento3Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento3Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite2() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento3Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento3Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento4Parte1().equals("") && obj.getPagamento4Parte2().equals("")) {
				obj.setPagamento4Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento4Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite2() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento4Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento4Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento5Parte1().equals("") && obj.getPagamento5Parte2().equals("")) {
				obj.setPagamento5Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento5Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite2() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento5Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento5Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento6Parte1().equals("") && obj.getPagamento6Parte2().equals("")) {
				obj.setPagamento6Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento6Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite2() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento6Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento6Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			}
		}
		return;
	}

	public void gerarInstrucaoDescontoProgressivoDiaLimite3(BoletoBancarioRelVO obj, DescontoProgressivoVO descontoProgressivoVO, List listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade, List listaPlanoFinanceiroAlunoDescricaoDescontos, Boolean eMatricula) throws Exception {
		if (!descontoProgressivoVO.getDiaLimite3().equals(0)) {
			Double valorDescontoOriginal = 0.0;
			if (listaPlanoFinanceiroAlunoDescricaoDescontos != null) {
				PlanoFinanceiroAlunoDescricaoDescontosVO planoOriginal = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(0);
				valorDescontoOriginal = planoOriginal.getValorDescontoProgressivo();
			}
			PlanoFinanceiroAlunoDescricaoDescontosVO p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade.get(0);
			if (obj.getPagamento1Parte1().equals("") && obj.getPagamento1Parte2().equals("")) {
				obj.setPagamento1Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento1Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite3() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento1Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento1Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento2Parte1().equals("") && obj.getPagamento2Parte2().equals("")) {
				obj.setPagamento2Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento2Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite3() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento2Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento2Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento3Parte1().equals("") && obj.getPagamento3Parte2().equals("")) {
				obj.setPagamento3Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento3Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite3() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento3Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento3Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento4Parte1().equals("") && obj.getPagamento4Parte2().equals("")) {
				obj.setPagamento4Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento4Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite3() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento4Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento4Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento5Parte1().equals("") && obj.getPagamento5Parte2().equals("")) {
				obj.setPagamento5Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento5Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite3() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento5Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento5Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento6Parte1().equals("") && obj.getPagamento6Parte2().equals("")) {
				obj.setPagamento6Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento6Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite3() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento6Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento6Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			}
		}
		return;
	}

	public void gerarInstrucaoDescontoProgressivoDiaLimite4(BoletoBancarioRelVO obj, DescontoProgressivoVO descontoProgressivoVO, List listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade, List listaPlanoFinanceiroAlunoDescricaoDescontos, Boolean eMatricula) throws Exception {
		if (!descontoProgressivoVO.getDiaLimite4().equals(0)) {
			Double valorDescontoOriginal = 0.0;
			if (listaPlanoFinanceiroAlunoDescricaoDescontos != null) {
				PlanoFinanceiroAlunoDescricaoDescontosVO planoOriginal = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(0);
				valorDescontoOriginal = planoOriginal.getValorDescontoProgressivo();
			}
			PlanoFinanceiroAlunoDescricaoDescontosVO p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontosSemValidade.get(0);
			if (obj.getPagamento1Parte1().equals("") && obj.getPagamento1Parte2().equals("")) {
				obj.setPagamento1Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento1Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite4() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento1Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento1Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento2Parte1().equals("") && obj.getPagamento2Parte2().equals("")) {
				obj.setPagamento2Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento2Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite4() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento2Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento2Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento3Parte1().equals("") && obj.getPagamento3Parte2().equals("")) {
				obj.setPagamento3Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento3Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite4() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento3Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento3Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento4Parte1().equals("") && obj.getPagamento4Parte2().equals("")) {
				obj.setPagamento4Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento4Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite4() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento4Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento4Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento5Parte1().equals("") && obj.getPagamento5Parte2().equals("")) {
				obj.setPagamento5Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento5Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite4() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento5Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento5Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			} else if (obj.getPagamento6Parte1().equals("") && obj.getPagamento6Parte2().equals("")) {
				obj.setPagamento6Parte1("Após " + Uteis.getData(obj.getContareceber_datavencimento()) + "");
				if (obj.getModeloboleto_apenasDescontoInstrucaoBoleto()) {
					obj.setPagamento6Parte1("Até " + Uteis.getData(Uteis.getDataPassada(obj.getContareceber_datavencimento(), descontoProgressivoVO.getDiaLimite4() - 1)) + "");
					Double valorDesc = Uteis.arrendondarForcando2CadasDecimais(valorDescontoOriginal - p.getValorDescontoProgressivo());
					obj.setPagamento6Parte2(" Desconto de = " + Uteis.formatarDoubleParaMoeda(valorDesc));
				} else {
					obj.setPagamento6Parte2(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDesconto(eMatricula) + getContareceber_totalAcrescimo())));
				}
			}
		}
		return;
	}

	

	public PlanoFinanceiroAlunoDescricaoDescontosVO inicializarDadosPlanoDescricaoDescontos(PlanoFinanceiroAlunoDescricaoDescontosVO plano, Boolean matricula, Double valorBase) {
		PlanoFinanceiroAlunoDescricaoDescontosVO planoFinanceiroAlunoDescricaoDescontosVO = new PlanoFinanceiroAlunoDescricaoDescontosVO();
		planoFinanceiroAlunoDescricaoDescontosVO.setDataLimiteAplicacaoDesconto(plano.getDataLimiteAplicacaoDesconto());
		planoFinanceiroAlunoDescricaoDescontosVO.setValorBase(valorBase);
		planoFinanceiroAlunoDescricaoDescontosVO.setValorParaPagamentoDentroDataLimiteDesconto(plano.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase));
		planoFinanceiroAlunoDescricaoDescontosVO.setPercentualDescontoProgressivo(plano.getPercentualDescontoProgressivo());
		planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoProgressivo(plano.getValorDescontoProgressivo());
		planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoAluno(plano.getValorDescontoAluno());
		planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoInstituicao(plano.getValorDescontoInstituicao());
		planoFinanceiroAlunoDescricaoDescontosVO.setValorDescontoConvenio(plano.getValorDescontoConvenio());
		planoFinanceiroAlunoDescricaoDescontosVO.setCodigoContaReceber(plano.getCodigoContaReceber());
		return planoFinanceiroAlunoDescricaoDescontosVO;
	}

	public void realizarSomaPlanoDescricaoDescontos(PlanoFinanceiroAlunoDescricaoDescontosVO planoMap, PlanoFinanceiroAlunoDescricaoDescontosVO plano, Boolean matricula, Double valorBase) {
		planoMap.setValorBase(valorBase);
		planoMap.setPercentualDescontoProgressivo(planoMap.getPercentualDescontoProgressivo() + plano.getPercentualDescontoProgressivo());
		planoMap.setValorDescontoProgressivo(planoMap.getValorDescontoProgressivo() + plano.getValorDescontoProgressivo());
		planoMap.setValorDescontoAluno(planoMap.getValorDescontoAluno() + plano.getValorDescontoAluno());
		planoMap.setValorDescontoInstituicao(planoMap.getValorDescontoInstituicao() + plano.getValorDescontoInstituicao());
		planoMap.setValorDescontoConvenio(planoMap.getValorDescontoConvenio() + plano.getValorDescontoConvenio());
		planoMap.setValorParaPagamentoDentroDataLimiteDesconto(Uteis.arrendondarForcando2CadasDecimais(planoMap.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase) + getContareceber_totalAcrescimo()));
	}

	public void gerarIntrucoesDescontosContaAgrupada(List listaPlanoFinanceiroAlunoDescricaoDescontos, List<PlanoDescontoVO> listaPlanoDesconto, Double valorBase) throws Exception {
		Boolean matricula = getContareceber_tipoorigem().equals("MAT");
		for (int i = 0; i < listaPlanoFinanceiroAlunoDescricaoDescontos.size(); i++) {
			PlanoFinanceiroAlunoDescricaoDescontosVO p = (PlanoFinanceiroAlunoDescricaoDescontosVO) listaPlanoFinanceiroAlunoDescricaoDescontos.get(i);
			if ((valorBase.doubleValue() - p.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase).doubleValue() + getContareceber_totalAcrescimo() > 0) || p.getApresentarDescricaoBoleto()) {
				if (getPagamento1Parte1().equals("")) {
					setPagamento1Parte1(p.getDataLimiteAplicacaoDesconto_Apresentar());
					setPagamento1Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(valorBase.doubleValue() - p.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase).doubleValue()));
					setPagamento1Parte3(" (Descontos: Prog. " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + ")");
					setPagamento1Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase) + getContareceber_totalAcrescimo())));
					getMapPlanoFinanceiroAlunoDescricaoDescontosVOs().put(p.getDataLimiteAplicacaoDesconto_Apresentar(), inicializarDadosPlanoDescricaoDescontos(p, matricula, valorBase));
					continue;
				} else {
					PlanoFinanceiroAlunoDescricaoDescontosVO planoMap = getMapPlanoFinanceiroAlunoDescricaoDescontosVOs().get(getPagamento1Parte1());
					if ((planoMap.getDataLimiteAplicacaoDesconto_Apresentar().equals(p.getDataLimiteAplicacaoDesconto_Apresentar()) || planoMap.getDataLimiteAplicacaoDesconto().before(p.getDataLimiteAplicacaoDesconto())) && !planoMap.getCodigoContaReceber().equals(p.getCodigoContaReceber())) {
						realizarSomaPlanoDescricaoDescontos(planoMap, p, matricula, valorBase);
						setPagamento1Parte1(planoMap.getDataLimiteAplicacaoDesconto_Apresentar());
						setPagamento1Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda((planoMap.getValorBase().doubleValue()) - planoMap.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase).doubleValue()));
						setPagamento1Parte3(" (Descontos: Prog. " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoConvenio()) + ")");
						setPagamento1Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(planoMap.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase) + getContareceber_totalAcrescimo())));
						if (planoMap.getDataLimiteAplicacaoDesconto_Apresentar().equals(p.getDataLimiteAplicacaoDesconto_Apresentar())) {
							continue;
						}
					}
				}
				if (getPagamento2Parte1().equals("")) {
					setPagamento2Parte1(p.getDataLimiteAplicacaoDesconto_Apresentar());
					setPagamento2Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(valorBase.doubleValue() - p.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase).doubleValue()));
					setPagamento2Parte3(" (Descontos: Prog. " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + ")");
					setPagamento2Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase) + getContareceber_totalAcrescimo())));
					getMapPlanoFinanceiroAlunoDescricaoDescontosVOs().put(p.getDataLimiteAplicacaoDesconto_Apresentar(), inicializarDadosPlanoDescricaoDescontos(p, matricula, valorBase));
					continue;
				} else {
					PlanoFinanceiroAlunoDescricaoDescontosVO planoMap = getMapPlanoFinanceiroAlunoDescricaoDescontosVOs().get(getPagamento2Parte1());
					if ((planoMap.getDataLimiteAplicacaoDesconto_Apresentar().equals(p.getDataLimiteAplicacaoDesconto_Apresentar()) || planoMap.getDataLimiteAplicacaoDesconto().before(p.getDataLimiteAplicacaoDesconto())) && !planoMap.getCodigoContaReceber().equals(p.getCodigoContaReceber())) {
						realizarSomaPlanoDescricaoDescontos(planoMap, p, matricula, valorBase);
						setPagamento2Parte1(planoMap.getDataLimiteAplicacaoDesconto_Apresentar());
						setPagamento2Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda((planoMap.getValorBase().doubleValue()) - planoMap.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase).doubleValue()));
						setPagamento2Parte3(" (Descontos: Prog. " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoConvenio()) + ")");
						setPagamento2Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(planoMap.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase) + getContareceber_totalAcrescimo())));
						if (planoMap.getDataLimiteAplicacaoDesconto_Apresentar().equals(p.getDataLimiteAplicacaoDesconto_Apresentar())) {
							continue;
						}
					}
				}
				if (getPagamento3Parte1().equals("")) {
					setPagamento3Parte1(p.getDataLimiteAplicacaoDesconto_Apresentar());
					setPagamento3Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(valorBase.doubleValue() - p.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase).doubleValue()));
					setPagamento3Parte3(" (Descontos: Prog. " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + ")");
					setPagamento3Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase) + getContareceber_totalAcrescimo())));
					getMapPlanoFinanceiroAlunoDescricaoDescontosVOs().put(p.getDataLimiteAplicacaoDesconto_Apresentar(), inicializarDadosPlanoDescricaoDescontos(p, matricula, valorBase));
					continue;
				} else {
					PlanoFinanceiroAlunoDescricaoDescontosVO planoMap = getMapPlanoFinanceiroAlunoDescricaoDescontosVOs().get(getPagamento3Parte1());
					if ((planoMap.getDataLimiteAplicacaoDesconto_Apresentar().equals(p.getDataLimiteAplicacaoDesconto_Apresentar()) || planoMap.getDataLimiteAplicacaoDesconto().before(p.getDataLimiteAplicacaoDesconto())) && !planoMap.getCodigoContaReceber().equals(p.getCodigoContaReceber())) {
						realizarSomaPlanoDescricaoDescontos(planoMap, p, matricula, valorBase);
						setPagamento3Parte1(planoMap.getDataLimiteAplicacaoDesconto_Apresentar());
						setPagamento3Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda((planoMap.getValorBase().doubleValue()) - planoMap.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase).doubleValue()));
						setPagamento3Parte3(" (Descontos: Prog. " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoConvenio()) + ")");
						setPagamento3Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(planoMap.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase) + getContareceber_totalAcrescimo())));
						if (planoMap.getDataLimiteAplicacaoDesconto_Apresentar().equals(p.getDataLimiteAplicacaoDesconto_Apresentar())) {
							continue;
						}
					}
				}
				if (getPagamento4Parte1().equals("")) {
					setPagamento4Parte1(p.getDataLimiteAplicacaoDesconto_Apresentar());
					setPagamento4Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(valorBase.doubleValue() - p.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase).doubleValue()));
					setPagamento4Parte3(" (Descontos: Prog. " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + ")");
					setPagamento4Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase) + getContareceber_totalAcrescimo())));
					getMapPlanoFinanceiroAlunoDescricaoDescontosVOs().put(p.getDataLimiteAplicacaoDesconto_Apresentar(), inicializarDadosPlanoDescricaoDescontos(p, matricula, valorBase));
					continue;
				} else {
					PlanoFinanceiroAlunoDescricaoDescontosVO planoMap = getMapPlanoFinanceiroAlunoDescricaoDescontosVOs().get(getPagamento4Parte1());
					if ((planoMap.getDataLimiteAplicacaoDesconto_Apresentar().equals(p.getDataLimiteAplicacaoDesconto_Apresentar()) || planoMap.getDataLimiteAplicacaoDesconto().before(p.getDataLimiteAplicacaoDesconto())) && !planoMap.getCodigoContaReceber().equals(p.getCodigoContaReceber())) {
						realizarSomaPlanoDescricaoDescontos(planoMap, p, matricula, valorBase);
						setPagamento4Parte1(planoMap.getDataLimiteAplicacaoDesconto_Apresentar());
						setPagamento4Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda((planoMap.getValorBase().doubleValue()) - planoMap.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase).doubleValue()));
						setPagamento4Parte3(" (Descontos: Prog. " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoConvenio()) + ")");
						setPagamento4Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(planoMap.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase) + getContareceber_totalAcrescimo())));
						if (planoMap.getDataLimiteAplicacaoDesconto_Apresentar().equals(p.getDataLimiteAplicacaoDesconto_Apresentar())) {
							continue;
						}
					}
				}
				if (getPagamento5Parte1().equals("")) {
					setPagamento5Parte1(p.getDataLimiteAplicacaoDesconto_Apresentar());
					setPagamento5Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(valorBase.doubleValue() - p.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase).doubleValue()));
					setPagamento5Parte3(" (Descontos: Prog. " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + ")");
					setPagamento5Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase) + getContareceber_totalAcrescimo())));
					getMapPlanoFinanceiroAlunoDescricaoDescontosVOs().put(p.getDataLimiteAplicacaoDesconto_Apresentar(), inicializarDadosPlanoDescricaoDescontos(p, matricula, valorBase));
					continue;
				} else {
					PlanoFinanceiroAlunoDescricaoDescontosVO planoMap = getMapPlanoFinanceiroAlunoDescricaoDescontosVOs().get(getPagamento5Parte1());
					if ((planoMap.getDataLimiteAplicacaoDesconto_Apresentar().equals(p.getDataLimiteAplicacaoDesconto_Apresentar()) || planoMap.getDataLimiteAplicacaoDesconto().before(p.getDataLimiteAplicacaoDesconto())) && !planoMap.getCodigoContaReceber().equals(p.getCodigoContaReceber())) {
						realizarSomaPlanoDescricaoDescontos(planoMap, p, matricula, valorBase);
						setPagamento5Parte1(planoMap.getDataLimiteAplicacaoDesconto_Apresentar());
						setPagamento5Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda((planoMap.getValorBase().doubleValue()) - planoMap.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase).doubleValue()));
						setPagamento5Parte3(" (Descontos: Prog. " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoConvenio()) + ")");
						setPagamento5Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(planoMap.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase) + getContareceber_totalAcrescimo())));
						if (planoMap.getDataLimiteAplicacaoDesconto_Apresentar().equals(p.getDataLimiteAplicacaoDesconto_Apresentar())) {
							continue;
						}
					}
				}
				if (getPagamento6Parte1().equals("")) {
					setPagamento6Parte1(p.getDataLimiteAplicacaoDesconto_Apresentar());
					setPagamento6Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda(valorBase.doubleValue() - p.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase).doubleValue()));
					setPagamento6Parte3(" (Descontos: Prog. " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(p.getValorDescontoConvenio()) + ")");
					setPagamento6Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(p.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase) + getContareceber_totalAcrescimo())));
					getMapPlanoFinanceiroAlunoDescricaoDescontosVOs().put(p.getDataLimiteAplicacaoDesconto_Apresentar(), inicializarDadosPlanoDescricaoDescontos(p, matricula, valorBase));
					continue;
				} else {
					PlanoFinanceiroAlunoDescricaoDescontosVO planoMap = getMapPlanoFinanceiroAlunoDescricaoDescontosVOs().get(getPagamento6Parte1());
					if ((planoMap.getDataLimiteAplicacaoDesconto_Apresentar().equals(p.getDataLimiteAplicacaoDesconto_Apresentar()) || planoMap.getDataLimiteAplicacaoDesconto().before(p.getDataLimiteAplicacaoDesconto())) && !planoMap.getCodigoContaReceber().equals(p.getCodigoContaReceber())) {
						realizarSomaPlanoDescricaoDescontos(planoMap, p, matricula, valorBase);
						setPagamento6Parte1(planoMap.getDataLimiteAplicacaoDesconto_Apresentar());
						setPagamento6Parte2(" Desconto = " + Uteis.formatarDoubleParaMoeda((planoMap.getValorBase().doubleValue()) - planoMap.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase).doubleValue()));
						setPagamento6Parte3(" (Descontos: Prog. " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoProgressivo()) + "   Aluno= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoAluno()) + "    Inst= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoInstituicao()) + "   Conv= " + Uteis.formatarDoubleParaMoeda(planoMap.getValorDescontoConvenio()) + ")");
						setPagamento6Parte4(" Valor a Pagar = " + Uteis.formatarDoubleParaMoeda(Uteis.arrendondarForcando2CadasDecimais(planoMap.getValorParaPagamentoDentroDataLimiteDescontoContaAgrupada(matricula, valorBase) + getContareceber_totalAcrescimo())));
						if (planoMap.getDataLimiteAplicacaoDesconto_Apresentar().equals(p.getDataLimiteAplicacaoDesconto_Apresentar())) {
							continue;
						}
					}
				}
			}
		}
	}

	public Integer getContareceber_codigo() {
		if (contareceber_codigo == null) {
			contareceber_codigo = 0;
		}
		return contareceber_codigo;
	}

	public void setContareceber_codigo(Integer contareceber_codigo) {
		this.contareceber_codigo = contareceber_codigo;
	}

	/**
	 * @return the contareceber_codigobarra
	 */
	public String getContareceber_codigobarra() {
		if (contareceber_codigobarra == null) {
			contareceber_codigobarra = "";
		}
		return contareceber_codigobarra;
	}

	/**
	 * @param contareceber_codigobarra
	 *            the contareceber_codigobarra to set
	 */
	public void setContareceber_codigobarra(String contareceber_codigobarra) {
		this.contareceber_codigobarra = contareceber_codigobarra;
	}

	/**
	 * @return the contareceber_data
	 */
	public Date getContareceber_data() {
		return contareceber_data;
	}

	/**
	 * @param contareceber_data
	 *            the contareceber_data to set
	 */
	public void setContareceber_data(Date contareceber_data) {
		this.contareceber_data = contareceber_data;
	}

	/**
	 * @return the contareceber_tipoorigem
	 */
	public String getContareceber_tipoorigem() {
		if (contareceber_tipoorigem == null) {
			contareceber_tipoorigem = "";
		}
		return contareceber_tipoorigem;
	}

	/**
	 * @param contareceber_tipoorigem
	 *            the contareceber_tipoorigem to set
	 */
	public void setContareceber_tipoorigem(String contareceber_tipoorigem) {
		this.contareceber_tipoorigem = contareceber_tipoorigem;
	}

	/**
	 * @return the contareceber_descricaopagamento
	 */
	public String getContareceber_descricaopagamento() {
		if (contareceber_descricaopagamento == null) {
			contareceber_descricaopagamento = "";
		}
		return contareceber_descricaopagamento;
	}

	/**
	 * @param contareceber_descricaopagamento
	 *            the contareceber_descricaopagamento to set
	 */
	public void setContareceber_descricaopagamento(String contareceber_descricaopagamento) {
		this.contareceber_descricaopagamento = contareceber_descricaopagamento;
	}

	/**
	 * @return the contareceber_datavencimento
	 */
	public Date getContareceber_datavencimento() {
		return contareceber_datavencimento;
	}

	/**
	 * @param contareceber_datavencimento
	 *            the contareceber_datavencimento to set
	 */
	public void setContareceber_datavencimento(Date contareceber_datavencimento) {
		this.contareceber_datavencimento = contareceber_datavencimento;
	}

	/**
	 * @return the contareceber_valor
	 */
	public double getContareceber_valor() {
		return contareceber_valor;
	}

	/**
	 * @param contareceber_valor
	 *            the contareceber_valor to set
	 */
	public void setContareceber_valor(double contareceber_valor) {
		this.contareceber_valor = contareceber_valor;
	}

	/**
	 * @return the contareceber_valordesconto
	 */
	public double getContareceber_valordesconto() {
		return contareceber_valordesconto;
	}

	/**
	 * @param contareceber_valordesconto
	 *            the contareceber_valordesconto to set
	 */
	public void setContareceber_valordesconto(double contareceber_valordesconto) {
		this.contareceber_valordesconto = contareceber_valordesconto;
	}

	/**
	 * @return the contareceber_juro
	 */
	public double getContareceber_juro() {
		return contareceber_juro;
	}

	/**
	 * @param contareceber_juro
	 *            the contareceber_juro to set
	 */
	public void setContareceber_juro(double contareceber_juro) {
		this.contareceber_juro = contareceber_juro;
	}

	/**
	 * @return the contareceber_juroporcentagem
	 */
	public double getContareceber_juroporcentagem() {
		return contareceber_juroporcentagem;
	}

	/**
	 * @param contareceber_juroporcentagem
	 *            the contareceber_juroporcentagem to set
	 */
	public void setContareceber_juroporcentagem(double contareceber_juroporcentagem) {
		this.contareceber_juroporcentagem = contareceber_juroporcentagem;
	}

	/**
	 * @return the contareceber_multa
	 */
	public double getContareceber_multa() {
		return contareceber_multa;
	}

	/**
	 * @param contareceber_multa
	 *            the contareceber_multa to set
	 */
	public void setContareceber_multa(double contareceber_multa) {
		this.contareceber_multa = contareceber_multa;
	}

	/**
	 * @return the contareceber_multaporcentagem
	 */
	public double getContareceber_multaporcentagem() {
		return contareceber_multaporcentagem;
	}

	/**
	 * @param contareceber_multaporcentagem
	 *            the contareceber_multaporcentagem to set
	 */
	public void setContareceber_multaporcentagem(double contareceber_multaporcentagem) {
		this.contareceber_multaporcentagem = contareceber_multaporcentagem;
	}

	/**
	 * @return the contareceber_nrdocumento
	 */
	public String getContareceber_nrdocumento() {
		if (contareceber_nrdocumento == null) {
			contareceber_nrdocumento = "";
		}
		return contareceber_nrdocumento;
	}

	/**
	 * @param contareceber_nrdocumento
	 *            the contareceber_nrdocumento to set
	 */
	public void setContareceber_nrdocumento(String contareceber_nrdocumento) {
		this.contareceber_nrdocumento = contareceber_nrdocumento;
	}

	/**
	 * @return the contareceber_parcela
	 */
	public String getContareceber_parcela() {
		if (contareceber_parcela == null) {
			contareceber_parcela = "";
		}
		return contareceber_parcela;
	}

	/**
	 * @param contareceber_parcela
	 *            the contareceber_parcela to set
	 */
	public void setContareceber_parcela(String contareceber_parcela) {
		this.contareceber_parcela = contareceber_parcela;
	}
	
	public void  preencherParcelaPorConfiguracaoFinanceira_Apresentar(ConfiguracaoFinanceiroVO configFinanceiro) {
		if (Uteis.isAtributoPreenchido(getContareceber_tipoorigem())
				&& getContareceber_tipoorigem().equals(TipoOrigemContaReceber.MATRICULA.valor) 
				&& Uteis.isAtributoPreenchido(configFinanceiro.getNomeParcelaMatriculaApresentarAluno())) {
			setContareceber_parcela(configFinanceiro.getNomeParcelaMatriculaApresentarAluno());
		}else if (Uteis.isAtributoPreenchido(getContareceber_tipoorigem())
				&& getContareceber_tipoorigem().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO.valor) 
				&& Uteis.isAtributoPreenchido(configFinanceiro.getNomeParcelaMaterialDidaticoApresentarAluno())) {
			setContareceber_parcela(configFinanceiro.getNomeParcelaMaterialDidaticoApresentarAluno());
		} 
	}

	/**
	 * @return the contareceber_origemnegociacaoreceber
	 */
	public Integer getContareceber_origemnegociacaoreceber() {
		return contareceber_origemnegociacaoreceber;
	}

	/**
	 * @param contareceber_origemnegociacaoreceber
	 *            the contareceber_origemnegociacaoreceber to set
	 */
	public void setContareceber_origemnegociacaoreceber(Integer contareceber_origemnegociacaoreceber) {
		this.contareceber_origemnegociacaoreceber = contareceber_origemnegociacaoreceber;
	}

	/**
	 * @return the contacorrente_agencia
	 */
	public Integer getContacorrente_agencia() {
		return contacorrente_agencia;
	}

	/**
	 * @param contacorrente_agencia
	 *            the contacorrente_agencia to set
	 */
	public void setContacorrente_agencia(Integer contacorrente_agencia) {
		this.contacorrente_agencia = contacorrente_agencia;
	}

	/**
	 * @return the contareceber_contacorrente
	 */
	public Integer getContareceber_contacorrente() {
		return contareceber_contacorrente;
	}

	/**
	 * @param contareceber_contacorrente
	 *            the contareceber_contacorrente to set
	 */
	public void setContareceber_contacorrente(Integer contareceber_contacorrente) {
		this.contareceber_contacorrente = contareceber_contacorrente;
	}

	/**
	 * @return the contareceber_descontoinstituica
	 */
	public double getContareceber_descontoinstituica() {
		return contareceber_descontoinstituica;
	}

	/**
	 * @param contareceber_descontoinstituica
	 *            the contareceber_descontoinstituica to set
	 */
	public void setContareceber_descontoinstituica(double contareceber_descontoinstituica) {
		this.contareceber_descontoinstituica = contareceber_descontoinstituica;
	}

	/**
	 * @return the contareceber_descontoconvenio
	 */
	public double getContareceber_descontoconvenio() {
		return contareceber_descontoconvenio;
	}

	/**
	 * @param contareceber_descontoconvenio
	 *            the contareceber_descontoconvenio to set
	 */
	public void setContareceber_descontoconvenio(double contareceber_descontoconvenio) {
		this.contareceber_descontoconvenio = contareceber_descontoconvenio;
	}

	/**
	 * @return the agencia_numeroagencia
	 */
	public String getAgencia_numeroagencia() {
		if (agencia_numeroagencia == null) {
			agencia_numeroagencia = "";
		}
		return agencia_numeroagencia;
	}

	/**
	 * @param agencia_numeroagencia
	 *            the agencia_numeroagencia to set
	 */
	public void setAgencia_numeroagencia(String agencia_numeroagencia) {
		this.agencia_numeroagencia = agencia_numeroagencia;
	}

	/**
	 * @return the contacorrente_numero
	 */
	public String getContacorrente_numero() {
		if (contacorrente_numero == null) {
			contacorrente_numero = "";
		}
		return contacorrente_numero;
	}

	/**
	 * @param contacorrente_numero
	 *            the contacorrente_numero to set
	 */
	public void setContacorrente_numero(String contacorrente_numero) {
		this.contacorrente_numero = contacorrente_numero;
	}

	/**
	 * @return the pessoa_cpf
	 */
	public String getPessoa_cpf() {
		if (pessoa_cpf == null) {
			pessoa_cpf = "";
		}
		return pessoa_cpf;
	}

	/**
	 * @param pessoa_cpf
	 *            the pessoa_cpf to set
	 */
	public void setPessoa_cpf(String pessoa_cpf) {
		this.pessoa_cpf = pessoa_cpf;
	}

	/**
	 * @return the pessoa_setor
	 */
	public String getPessoa_setor() {
		if (pessoa_setor == null) {
			pessoa_setor = "";
		}
		return pessoa_setor;
	}

	/**
	 * @param pessoa_setor
	 *            the pessoa_setor to set
	 */
	public void setPessoa_setor(String pessoa_setor) {
		this.pessoa_setor = pessoa_setor;
	}

	/**
	 * @return the pessoa_endereco
	 */
	public String getPessoa_endereco() {
		if (pessoa_endereco == null) {
			pessoa_endereco = "";
		}
		return pessoa_endereco;
	}

	/**
	 * @param pessoa_endereco
	 *            the pessoa_endereco to set
	 */
	public void setPessoa_endereco(String pessoa_endereco) {
		this.pessoa_endereco = pessoa_endereco;
	}

	/**
	 * @return the pessoa_cep
	 */
	public String getPessoa_cep() {
		if (pessoa_cep == null) {
			pessoa_cep = "";
		}
		return pessoa_cep;
	}

	/**
	 * @param pessoa_cep
	 *            the pessoa_cep to set
	 */
	public void setPessoa_cep(String pessoa_cep) {
		this.pessoa_cep = pessoa_cep;
	}

	/**
	 * @return the pessoa_cidade
	 */
	public Integer getPessoa_cidade() {
		return pessoa_cidade;
	}

	/**
	 * @param pessoa_cidade
	 *            the pessoa_cidade to set
	 */
	public void setPessoa_cidade(Integer pessoa_cidade) {
		this.pessoa_cidade = pessoa_cidade;
	}

	/**
	 * @return the cidade_nome
	 */
	public String getCidade_nome() {
		if (cidade_nome == null) {
			cidade_nome = "";
		}
		return cidade_nome;
	}

	/**
	 * @param cidade_nome
	 *            the cidade_nome to set
	 */
	public void setCidade_nome(String cidade_nome) {
		this.cidade_nome = cidade_nome;
	}

	/**
	 * @return the agencia_banco
	 */
	public Integer getAgencia_banco() {
		return agencia_banco;
	}

	/**
	 * @param agencia_banco
	 *            the agencia_banco to set
	 */
	public void setAgencia_banco(Integer agencia_banco) {
		this.agencia_banco = agencia_banco;
	}

	/**
	 * @return the banco_nome
	 */
	public String getBanco_nome() {
		return banco_nome;
	}

	/**
	 * @param banco_nome
	 *            the banco_nome to set
	 */
	public void setBanco_nome(String banco_nome) {
		this.banco_nome = banco_nome;
	}

	/**
	 * @return the cidade_estado
	 */
	public Integer getCidade_estado() {
		return cidade_estado;
	}

	/**
	 * @param cidade_estado
	 *            the cidade_estado to set
	 */
	public void setCidade_estado(Integer cidade_estado) {
		this.cidade_estado = cidade_estado;
	}

	/**
	 * @return the modeloboleto_observacoesgerais1
	 */
	public String getModeloboleto_observacoesgerais1() {
		if (modeloboleto_observacoesgerais1 == null) {
			modeloboleto_observacoesgerais1 = "";
		}
		return modeloboleto_observacoesgerais1;
	}

	/**
	 * @param modeloboleto_observacoesgerais1
	 *            the modeloboleto_observacoesgerais1 to set
	 */
	public void setModeloboleto_observacoesgerais1(String modeloboleto_observacoesgerais1) {
		this.modeloboleto_observacoesgerais1 = modeloboleto_observacoesgerais1;
	}

	/**
	 * @return the modeloboleto_observacoesgerais2
	 */
	public String getModeloboleto_observacoesgerais2() {
		if (modeloboleto_observacoesgerais2 == null) {
			modeloboleto_observacoesgerais2 = "";
		}
		return modeloboleto_observacoesgerais2;
	}

	/**
	 * @param modeloboleto_observacoesgerais2
	 *            the modeloboleto_observacoesgerais2 to set
	 */
	public void setModeloboleto_observacoesgerais2(String modeloboleto_observacoesgerais2) {
		this.modeloboleto_observacoesgerais2 = modeloboleto_observacoesgerais2;
	}

	/**
	 * @return the modeloboleto_imagem
	 */
	// public byte[] getModeloboleto_imagem() {
	// return modeloboleto_imagem;
	// }
	//
	// /**
	// * @param modeloboleto_imagem the modeloboleto_imagem to set
	// */
	// public void setModeloboleto_imagem(byte[] modeloboleto_imagem) {
	// this.modeloboleto_imagem = modeloboleto_imagem;
	// }
	/**
	 * @return the modeloboleto_localpagamento
	 */
	public String getModeloboleto_localpagamento() {
		if (modeloboleto_localpagamento == null) {
			modeloboleto_localpagamento = "";
		}
		return modeloboleto_localpagamento;
	}

	/**
	 * @param modeloboleto_localpagamento
	 *            the modeloboleto_localpagamento to set
	 */
	public void setModeloboleto_localpagamento(String modeloboleto_localpagamento) {
		this.modeloboleto_localpagamento = modeloboleto_localpagamento;
	}

	/**
	 * @return the contareceber_linhadigitavelcodigobarras
	 */
	public String getContareceber_linhadigitavelcodigobarras() {
		if (contareceber_linhadigitavelcodigobarras == null) {
			contareceber_linhadigitavelcodigobarras = "";
		}
		return contareceber_linhadigitavelcodigobarras;
	}

	/**
	 * @param contareceber_linhadigitavelcodigobarras
	 *            the contareceber_linhadigitavelcodigobarras to set
	 */
	public void setContareceber_linhadigitavelcodigobarras(String contareceber_linhadigitavelcodigobarras) {
		this.contareceber_linhadigitavelcodigobarras = contareceber_linhadigitavelcodigobarras;
	}

	public Integer getDescontoprogressivo_codigo() {
		return descontoprogressivo_codigo;
	}

	public void setDescontoprogressivo_codigo(Integer descontoprogressivo_codigo) {
		this.descontoprogressivo_codigo = descontoprogressivo_codigo;
	}

	/**
	 * @return the descontoprogressivo_dialimite1
	 */
	public Integer getDescontoprogressivo_dialimite1() {
		return descontoprogressivo_dialimite1;
	}

	/**
	 * @param descontoprogressivo_dialimite1
	 *            the descontoprogressivo_dialimite1 to set
	 */
	public void setDescontoprogressivo_dialimite1(Integer descontoprogressivo_dialimite1) {
		this.descontoprogressivo_dialimite1 = descontoprogressivo_dialimite1;
	}

	/**
	 * @return the descontoprogressivo_dialimite2
	 */
	public Integer getDescontoprogressivo_dialimite2() {
		return descontoprogressivo_dialimite2;
	}

	/**
	 * @param descontoprogressivo_dialimite2
	 *            the descontoprogressivo_dialimite2 to set
	 */
	public void setDescontoprogressivo_dialimite2(Integer descontoprogressivo_dialimite2) {
		this.descontoprogressivo_dialimite2 = descontoprogressivo_dialimite2;
	}

	/**
	 * @return the descontoprogressivo_dialimite3
	 */
	public Integer getDescontoprogressivo_dialimite3() {
		return descontoprogressivo_dialimite3;
	}

	/**
	 * @param descontoprogressivo_dialimite3
	 *            the descontoprogressivo_dialimite3 to set
	 */
	public void setDescontoprogressivo_dialimite3(Integer descontoprogressivo_dialimite3) {
		this.descontoprogressivo_dialimite3 = descontoprogressivo_dialimite3;
	}

	/**
	 * @return the descontoprogressivo_dialimite4
	 */
	public Integer getDescontoprogressivo_dialimite4() {
		return descontoprogressivo_dialimite4;
	}

	/**
	 * @param descontoprogressivo_dialimite4
	 *            the descontoprogressivo_dialimite4 to set
	 */
	public void setDescontoprogressivo_dialimite4(Integer descontoprogressivo_dialimite4) {
		this.descontoprogressivo_dialimite4 = descontoprogressivo_dialimite4;
	}

	/**
	 * @return the descontoprogressivo_percdescontolimite1
	 */
	public double getDescontoprogressivo_percdescontolimite1() {
		return descontoprogressivo_percdescontolimite1;
	}

	/**
	 * @param descontoprogressivo_percdescontolimite1
	 *            the descontoprogressivo_percdescontolimite1 to set
	 */
	public void setDescontoprogressivo_percdescontolimite1(double descontoprogressivo_percdescontolimite1) {
		this.descontoprogressivo_percdescontolimite1 = descontoprogressivo_percdescontolimite1;
	}

	/**
	 * @return the descontoprogressivo_percdescontolimite2
	 */
	public double getDescontoprogressivo_percdescontolimite2() {
		return descontoprogressivo_percdescontolimite2;
	}

	/**
	 * @param descontoprogressivo_percdescontolimite2
	 *            the descontoprogressivo_percdescontolimite2 to set
	 */
	public void setDescontoprogressivo_percdescontolimite2(double descontoprogressivo_percdescontolimite2) {
		this.descontoprogressivo_percdescontolimite2 = descontoprogressivo_percdescontolimite2;
	}

	/**
	 * @return the descontoprogressivo_percdescontolimite3
	 */
	public double getDescontoprogressivo_percdescontolimite3() {
		return descontoprogressivo_percdescontolimite3;
	}

	/**
	 * @param descontoprogressivo_percdescontolimite3
	 *            the descontoprogressivo_percdescontolimite3 to set
	 */
	public void setDescontoprogressivo_percdescontolimite3(double descontoprogressivo_percdescontolimite3) {
		this.descontoprogressivo_percdescontolimite3 = descontoprogressivo_percdescontolimite3;
	}

	/**
	 * @return the descontoprogressivo_percdescontolimite4
	 */
	public double getDescontoprogressivo_percdescontolimite4() {
		return descontoprogressivo_percdescontolimite4;
	}

	/**
	 * @param descontoprogressivo_percdescontolimite4
	 *            the descontoprogressivo_percdescontolimite4 to set
	 */
	public void setDescontoprogressivo_percdescontolimite4(double descontoprogressivo_percdescontolimite4) {
		this.descontoprogressivo_percdescontolimite4 = descontoprogressivo_percdescontolimite4;
	}

	/**
	 * @return the total
	 */
	public double getTotal1() {
		return total1;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal1(double total1) {
		this.total1 = total1;
	}

	/**
	 * @return the valorDescProg
	 */
	public double getValorDescProg1() {
		return valorDescProg1;
	}

	/**
	 * @param valorDescProg
	 *            the valorDescProg to set
	 */
	public void setValorDescProg1(double valorDescProg1) {
		this.valorDescProg1 = valorDescProg1;
	}

	/**
	 * @return the dataDescProg1
	 */
	public Date getDataDescProg1() {
		return dataDescProg1;
	}

	/**
	 * @param dataDescProg1
	 *            the dataDescProg1 to set
	 */
	public void setDataDescProg1(Date dataDescProg1) {
		this.dataDescProg1 = dataDescProg1;
	}

	/**
	 * @return the dataDescProg2
	 */
	public Date getDataDescProg2() {
		return dataDescProg2;
	}

	/**
	 * @param dataDescProg2
	 *            the dataDescProg2 to set
	 */
	public void setDataDescProg2(Date dataDescProg2) {
		this.dataDescProg2 = dataDescProg2;
	}

	/**
	 * @return the dataDescProg3
	 */
	public Date getDataDescProg3() {
		return dataDescProg3;
	}

	/**
	 * @param dataDescProg3
	 *            the dataDescProg3 to set
	 */
	public void setDataDescProg3(Date dataDescProg3) {
		this.dataDescProg3 = dataDescProg3;
	}

	/**
	 * @return the dataDescProg4
	 */
	public Date getDataDescProg4() {
		return dataDescProg4;
	}

	/**
	 * @param dataDescProg4
	 *            the dataDescProg4 to set
	 */
	public void setDataDescProg4(Date dataDescProg4) {
		this.dataDescProg4 = dataDescProg4;
	}

	/**
	 * @return the total2
	 */
	public double getTotal2() {
		return total2;
	}

	/**
	 * @param total2
	 *            the total2 to set
	 */
	public void setTotal2(double total2) {
		this.total2 = total2;
	}

	/**
	 * @return the total3
	 */
	public double getTotal3() {
		return total3;
	}

	/**
	 * @param total3
	 *            the total3 to set
	 */
	public void setTotal3(double total3) {
		this.total3 = total3;
	}

	/**
	 * @return the total4
	 */
	public double getTotal4() {
		return total4;
	}

	/**
	 * @param total4
	 *            the total4 to set
	 */
	public void setTotal4(double total4) {
		this.total4 = total4;
	}

	/**
	 * @return the valorDescProg2
	 */
	public double getValorDescProg2() {
		return valorDescProg2;
	}

	/**
	 * @param valorDescProg2
	 *            the valorDescProg2 to set
	 */
	public void setValorDescProg2(double valorDescProg2) {
		this.valorDescProg2 = valorDescProg2;
	}

	/**
	 * @return the valorDescProg3
	 */
	public double getValorDescProg3() {
		return valorDescProg3;
	}

	/**
	 * @param valorDescProg3
	 *            the valorDescProg3 to set
	 */
	public void setValorDescProg3(double valorDescProg3) {
		this.valorDescProg3 = valorDescProg3;
	}

	/**
	 * @return the valorDescProg4
	 */
	public double getValorDescProg4() {
		return valorDescProg4;
	}

	/**
	 * @param valorDescProg4
	 *            the valorDescProg4 to set
	 */
	public void setValorDescProg4(double valorDescProg4) {
		this.valorDescProg4 = valorDescProg4;
	}

	/**
	 * @return the contareceber_descontoprogressivo
	 */
	public Integer getContareceber_descontoprogressivo() {
		return contareceber_descontoprogressivo;
	}

	/**
	 * @param contareceber_descontoprogressivo
	 *            the contareceber_descontoprogressivo to set
	 */
	public void setContareceber_descontoprogressivo(Integer contareceber_descontoprogressivo) {
		this.contareceber_descontoprogressivo = contareceber_descontoprogressivo;
	}

	/**
	 * @return the descontoTotal1
	 */
	public double getDescontoTotal1() {
		return descontoTotal1;
	}

	/**
	 * @param descontoTotal1
	 *            the descontoTotal1 to set
	 */
	public void setDescontoTotal1(double descontoTotal1) {
		this.descontoTotal1 = descontoTotal1;
	}

	/**
	 * @return the descontoTotal2
	 */
	public double getDescontoTotal2() {
		return descontoTotal2;
	}

	/**
	 * @param descontoTotal2
	 *            the descontoTotal2 to set
	 */
	public void setDescontoTotal2(double descontoTotal2) {
		this.descontoTotal2 = descontoTotal2;
	}

	/**
	 * @return the descontoTotal3
	 */
	public double getDescontoTotal3() {
		return descontoTotal3;
	}

	/**
	 * @param descontoTotal3
	 *            the descontoTotal3 to set
	 */
	public void setDescontoTotal3(double descontoTotal3) {
		this.descontoTotal3 = descontoTotal3;
	}

	/**
	 * @return the descontoTotal4
	 */
	public double getDescontoTotal4() {
		return descontoTotal4;
	}

	/**
	 * @param descontoTotal4
	 *            the descontoTotal4 to set
	 */
	public void setDescontoTotal4(double descontoTotal4) {
		this.descontoTotal4 = descontoTotal4;
	}

	/**
	 * @return the pessoa_nome
	 */
	public String getPessoa_nome() {
		return pessoa_nome;
	}

	/**
	 * @param pessoa_nome
	 *            the pessoa_nome to set
	 */
	public void setPessoa_nome(String pessoa_nome) {
		this.pessoa_nome = pessoa_nome;
	}

	/**
	 * @return the nossonumero
	 */
	public String getNossonumero() {
		if (nossonumero == null) {
			nossonumero = "";
		}
		return nossonumero;
	}

	/**
	 * @param nossonumero
	 *            the nossonumero to set
	 */
	public void setNossonumero(String nossonumero) {
		this.nossonumero = nossonumero;
	}

	/**
	 * @return the estado_nome
	 */
	public String getEstado_nome() {
		if (estado_nome == null) {
			estado_nome = "";
		}
		return estado_nome;
	}

	/**
	 * @param estado_nome
	 *            the estado_nome to set
	 */
	public void setEstado_nome(String estado_nome) {
		this.estado_nome = estado_nome;
	}

	/**
	 * @return the pagamento1Parte1
	 */
	public String getPagamento1Parte1() {
		if (pagamento1Parte1 == null) {
			pagamento1Parte1 = "";
		}
		return pagamento1Parte1;
	}

	/**
	 * @param pagamento1Parte1
	 *            the pagamento1Parte1 to set
	 */
	public void setPagamento1Parte1(String pagamento1Parte1) {
		this.pagamento1Parte1 = pagamento1Parte1;
	}

	/**
	 * @return the pagamento1Parte2
	 */
	public String getPagamento1Parte2() {
		if (pagamento1Parte2 == null) {
			pagamento1Parte2 = "";
		}
		return pagamento1Parte2;
	}

	/**
	 * @param pagamento1Parte2
	 *            the pagamento1Parte2 to set
	 */
	public void setPagamento1Parte2(String pagamento1Parte2) {
		this.pagamento1Parte2 = pagamento1Parte2;
	}

	/**
	 * @return the pagamento1Parte3
	 */
	public String getPagamento1Parte3() {
		if (pagamento1Parte3 == null) {
			pagamento1Parte3 = "";
		}
		return pagamento1Parte3;
	}

	/**
	 * @param pagamento1Parte3
	 *            the pagamento1Parte3 to set
	 */
	public void setPagamento1Parte3(String pagamento1Parte3) {
		this.pagamento1Parte3 = pagamento1Parte3;
	}

	/**
	 * @return the pagamento1Parte4
	 */
	public String getPagamento1Parte4() {
		if (pagamento1Parte4 == null) {
			pagamento1Parte4 = "";
		}
		return pagamento1Parte4;
	}

	/**
	 * @param pagamento1Parte4
	 *            the pagamento1Parte4 to set
	 */
	public void setPagamento1Parte4(String pagamento1Parte4) {
		this.pagamento1Parte4 = pagamento1Parte4;
	}

	/**
	 * @return the pagamento2Parte1
	 */
	public String getPagamento2Parte1() {
		if (pagamento2Parte1 == null) {
			pagamento2Parte1 = "";
		}
		return pagamento2Parte1;
	}

	/**
	 * @param pagamento2Parte1
	 *            the pagamento2Parte1 to set
	 */
	public void setPagamento2Parte1(String pagamento2Parte1) {
		this.pagamento2Parte1 = pagamento2Parte1;
	}

	/**
	 * @return the pagamento2Parte2
	 */
	public String getPagamento2Parte2() {
		if (pagamento2Parte2 == null) {
			pagamento2Parte2 = "";
		}
		return pagamento2Parte2;
	}

	/**
	 * @param pagamento2Parte2
	 *            the pagamento2Parte2 to set
	 */
	public void setPagamento2Parte2(String pagamento2Parte2) {
		this.pagamento2Parte2 = pagamento2Parte2;
	}

	/**
	 * @return the pagamento2Parte3
	 */
	public String getPagamento2Parte3() {
		if (pagamento2Parte3 == null) {
			pagamento2Parte3 = "";
		}
		return pagamento2Parte3;
	}

	/**
	 * @param pagamento2Parte3
	 *            the pagamento2Parte3 to set
	 */
	public void setPagamento2Parte3(String pagamento2Parte3) {
		this.pagamento2Parte3 = pagamento2Parte3;
	}

	/**
	 * @return the pagamento2Parte4
	 */
	public String getPagamento2Parte4() {
		if (pagamento2Parte4 == null) {
			pagamento2Parte4 = "";
		}
		return pagamento2Parte4;
	}

	/**
	 * @param pagamento2Parte4
	 *            the pagamento2Parte4 to set
	 */
	public void setPagamento2Parte4(String pagamento2Parte4) {
		this.pagamento2Parte4 = pagamento2Parte4;
	}

	/**
	 * @return the pagamento3Parte1
	 */
	public String getPagamento3Parte1() {
		if (pagamento3Parte1 == null) {
			pagamento3Parte1 = "";
		}
		return pagamento3Parte1;
	}

	/**
	 * @param pagamento3Parte1
	 *            the pagamento3Parte1 to set
	 */
	public void setPagamento3Parte1(String pagamento3Parte1) {
		this.pagamento3Parte1 = pagamento3Parte1;
	}

	/**
	 * @return the pagamento3Parte2
	 */
	public String getPagamento3Parte2() {
		if (pagamento3Parte2 == null) {
			pagamento3Parte2 = "";
		}
		return pagamento3Parte2;
	}

	/**
	 * @param pagamento3Parte2
	 *            the pagamento3Parte2 to set
	 */
	public void setPagamento3Parte2(String pagamento3Parte2) {
		this.pagamento3Parte2 = pagamento3Parte2;
	}

	/**
	 * @return the pagamento3Parte3
	 */
	public String getPagamento3Parte3() {
		if (pagamento3Parte3 == null) {
			pagamento3Parte3 = "";
		}
		return pagamento3Parte3;
	}

	/**
	 * @param pagamento3Parte3
	 *            the pagamento3Parte3 to set
	 */
	public void setPagamento3Parte3(String pagamento3Parte3) {
		this.pagamento3Parte3 = pagamento3Parte3;
	}

	/**
	 * @return the pagamento3Parte4
	 */
	public String getPagamento3Parte4() {
		if (pagamento3Parte4 == null) {
			pagamento3Parte4 = "";
		}
		return pagamento3Parte4;
	}

	/**
	 * @param pagamento3Parte4
	 *            the pagamento3Parte4 to set
	 */
	public void setPagamento3Parte4(String pagamento3Parte4) {
		this.pagamento3Parte4 = pagamento3Parte4;
	}

	/**
	 * @return the pagamento4Parte1
	 */
	public String getPagamento4Parte1() {
		if (pagamento4Parte1 == null) {
			pagamento4Parte1 = "";
		}
		return pagamento4Parte1;
	}

	/**
	 * @param pagamento4Parte1
	 *            the pagamento4Parte1 to set
	 */
	public void setPagamento4Parte1(String pagamento4Parte1) {
		this.pagamento4Parte1 = pagamento4Parte1;
	}

	/**
	 * @return the pagamento4Parte2
	 */
	public String getPagamento4Parte2() {
		if (pagamento4Parte2 == null) {
			pagamento4Parte2 = "";
		}
		return pagamento4Parte2;
	}

	/**
	 * @param pagamento4Parte2
	 *            the pagamento4Parte2 to set
	 */
	public void setPagamento4Parte2(String pagamento4Parte2) {
		this.pagamento4Parte2 = pagamento4Parte2;
	}

	/**
	 * @return the pagamento4Parte3
	 */
	public String getPagamento4Parte3() {
		if (pagamento4Parte3 == null) {
			pagamento4Parte3 = "";
		}
		return pagamento4Parte3;
	}

	/**
	 * @param pagamento4Parte3
	 *            the pagamento4Parte3 to set
	 */
	public void setPagamento4Parte3(String pagamento4Parte3) {
		this.pagamento4Parte3 = pagamento4Parte3;
	}

	/**
	 * @return the pagamento4Parte4
	 */
	public String getPagamento4Parte4() {
		if (pagamento4Parte4 == null) {
			pagamento4Parte4 = "";
		}
		return pagamento4Parte4;
	}

	/**
	 * @param pagamento4Parte4
	 *            the pagamento4Parte4 to set
	 */
	public void setPagamento4Parte4(String pagamento4Parte4) {
		this.pagamento4Parte4 = pagamento4Parte4;
	}

	/**
	 * @return the pagamentoSemDescProg
	 */
	public String getPagamentoSemDescProg() {
		if (pagamentoSemDescProg == null) {
			pagamentoSemDescProg = "";
		}
		return pagamentoSemDescProg;
	}

	/**
	 * @param pagamentoSemDescProg
	 *            the pagamentoSemDescProg to set
	 */
	public void setPagamentoSemDescProg(String pagamentoSemDescProg) {
		this.pagamentoSemDescProg = pagamentoSemDescProg;
	}

	public void setImagemBoleto(InputStream imagemBoleto) {
		this.imagemBoleto = imagemBoleto;
	}

	public InputStream getImagemBoleto() {
		return imagemBoleto;
	}

	public void setContareceber_mantenedora(String contareceber_mantenedora) {
		this.contareceber_mantenedora = contareceber_mantenedora;
	}

	public String getContareceber_mantenedora() {
		if (contareceber_mantenedora == null) {
			contareceber_mantenedora = "";
		}
		return contareceber_mantenedora;
	}

	public void setContareceber_razaoSocialMantenedora(String contareceber_razaoSocialMantenedora) {
		this.contareceber_razaoSocialMantenedora = contareceber_razaoSocialMantenedora;
	}

	public String getContareceber_razaoSocialMantenedora() {
		if (contareceber_razaoSocialMantenedora == null) {
			contareceber_razaoSocialMantenedora = "";
		}
		return contareceber_razaoSocialMantenedora;
	}

	public void setTipoBoletoContaReceber(String tipoBoletoContaReceber) {
		this.tipoBoletoContaReceber = tipoBoletoContaReceber;
	}

	public String getTipoBoletoContaReceber() {
		return tipoBoletoContaReceber;
	}

	/**
	 * @return the contareceber_nossonumero
	 */
	public String getContareceber_nossonumero() {
		if (contareceber_nossonumero == null) {
			contareceber_nossonumero = "";
		}
		return contareceber_nossonumero;
	}

	/**
	 * @param contareceber_nossonumero
	 *            the contareceber_nossonumero to set
	 */
	public void setContareceber_nossonumero(String contareceber_nossonumero) {
		this.contareceber_nossonumero = contareceber_nossonumero;
	}

	/**
	 * @return the pagamento5Parte1
	 */
	public String getPagamento5Parte1() {
		if (pagamento5Parte1 == null) {
			pagamento5Parte1 = "";
		}
		return pagamento5Parte1;
	}

	/**
	 * @param pagamento5Parte1
	 *            the pagamento5Parte1 to set
	 */
	public void setPagamento5Parte1(String pagamento5Parte1) {
		this.pagamento5Parte1 = pagamento5Parte1;
	}

	/**
	 * @return the pagamento5Parte2
	 */
	public String getPagamento5Parte2() {
		if (pagamento5Parte2 == null) {
			pagamento5Parte2 = "";
		}
		return pagamento5Parte2;
	}

	/**
	 * @param pagamento5Parte2
	 *            the pagamento5Parte2 to set
	 */
	public void setPagamento5Parte2(String pagamento5Parte2) {
		this.pagamento5Parte2 = pagamento5Parte2;
	}

	/**
	 * @return the pagamento5Parte3
	 */
	public String getPagamento5Parte3() {
		if (pagamento5Parte3 == null) {
			pagamento5Parte3 = "";
		}
		return pagamento5Parte3;
	}

	/**
	 * @param pagamento5Parte3
	 *            the pagamento5Parte3 to set
	 */
	public void setPagamento5Parte3(String pagamento5Parte3) {
		this.pagamento5Parte3 = pagamento5Parte3;
	}

	/**
	 * @return the pagamento5Parte4
	 */
	public String getPagamento5Parte4() {
		if (pagamento5Parte4 == null) {
			pagamento5Parte4 = "";
		}
		return pagamento5Parte4;
	}

	/**
	 * @param pagamento5Parte4
	 *            the pagamento5Parte4 to set
	 */
	public void setPagamento5Parte4(String pagamento5Parte4) {
		this.pagamento5Parte4 = pagamento5Parte4;
	}

	/**
	 * @return the pagamento6Parte1
	 */
	public String getPagamento6Parte1() {
		if (pagamento6Parte1 == null) {
			pagamento6Parte1 = "";
		}
		return pagamento6Parte1;
	}

	/**
	 * @param pagamento6Parte1
	 *            the pagamento6Parte1 to set
	 */
	public void setPagamento6Parte1(String pagamento6Parte1) {
		this.pagamento6Parte1 = pagamento6Parte1;
	}

	/**
	 * @return the pagamento6Parte2
	 */
	public String getPagamento6Parte2() {
		if (pagamento6Parte2 == null) {
			pagamento6Parte2 = "";
		}
		return pagamento6Parte2;
	}

	/**
	 * @param pagamento6Parte2
	 *            the pagamento6Parte2 to set
	 */
	public void setPagamento6Parte2(String pagamento6Parte2) {
		this.pagamento6Parte2 = pagamento6Parte2;
	}

	/**
	 * @return the pagamento6Parte3
	 */
	public String getPagamento6Parte3() {
		if (pagamento6Parte3 == null) {
			pagamento6Parte3 = "";
		}
		return pagamento6Parte3;
	}

	/**
	 * @param pagamento6Parte3
	 *            the pagamento6Parte3 to set
	 */
	public void setPagamento6Parte3(String pagamento6Parte3) {
		this.pagamento6Parte3 = pagamento6Parte3;
	}

	/**
	 * @return the pagamento6Parte4
	 */
	public String getPagamento6Parte4() {
		if (pagamento6Parte4 == null) {
			pagamento6Parte4 = "";
		}
		return pagamento6Parte4;
	}

	/**
	 * @param pagamento6Parte4
	 *            the pagamento6Parte4 to set
	 */
	public void setPagamento6Parte4(String pagamento6Parte4) {
		this.pagamento6Parte4 = pagamento6Parte4;
	}

	/**
	 * @return the ordemDescontos
	 */
	public List<OrdemDescontoVO> getOrdemDescontos() {
		if (ordemDescontos == null) {
			ordemDescontos = new ArrayList<OrdemDescontoVO>(0);
		}
		return ordemDescontos;
	}

	/**
	 * @param ordemDescontos
	 *            the ordemDescontos to set
	 */
	public void setOrdemDescontos(List<OrdemDescontoVO> ordemDescontos) {
		this.ordemDescontos = ordemDescontos;
	}

	/**
	 * @return the contareceber_tipodesconto
	 */
	public String getContareceber_tipodesconto() {
		if (contareceber_tipodesconto == null) {
			contareceber_tipodesconto = "";
		}
		return contareceber_tipodesconto;
	}

	/**
	 * @param contareceber_tipodesconto
	 *            the contareceber_tipodesconto to set
	 */
	public void setContareceber_tipodesconto(String contareceber_tipodesconto) {
		this.contareceber_tipodesconto = contareceber_tipodesconto;
	}

	public String getParceiro_razaosocial() {
		if (parceiro_razaosocial == null) {
			parceiro_razaosocial = "";
		}
		return parceiro_razaosocial;
	}

	public void setParceiro_razaosocial(String parceiroRazaosocial) {
		parceiro_razaosocial = parceiroRazaosocial;
	}

	/**
	 * @return the turmaBase
	 */
	public String getTurmaBase() {
		if (turmaBase == null) {
			turmaBase = "";
		}
		return turmaBase;
	}

	/**
	 * @param turmaBase
	 *            the turmaBase to set
	 */
	public void setTurmaBase(String turmaBase) {
		this.turmaBase = turmaBase;
	}

	/**
	 * @return the nomeCurso
	 */
	public String getNomeCurso() {
		if (nomeCurso == null) {
			nomeCurso = "";
		}
		return nomeCurso;
	}

	/**
	 * @param nomeCurso
	 *            the nomeCurso to set
	 */
	public void setNomeCurso(String nomeCurso) {
		this.nomeCurso = nomeCurso;
	}

	public String getContacorrente_carteira() {
		if (contacorrente_carteira == null) {
			contacorrente_carteira = "";
		}
		return contacorrente_carteira;
	}

	public void setContacorrente_carteira(String contacorrente_carteira) {
		this.contacorrente_carteira = contacorrente_carteira;
	}

	public String getContareceber_cnpjMantenedora() {
		if (contareceber_cnpjMantenedora == null) {
			contareceber_cnpjMantenedora = "";
		}
		return contareceber_cnpjMantenedora;
	}

	public void setContareceber_cnpjMantenedora(String contareceber_cnpjMantenedora) {
		this.contareceber_cnpjMantenedora = contareceber_cnpjMantenedora;
	}

	public String getBanco_nrbanco() {
		if (banco_nrbanco == null) {
			banco_nrbanco = "";
		}
		return banco_nrbanco;
	}

	public void setBanco_nrbanco(String banco_nrbanco) {
		this.banco_nrbanco = banco_nrbanco;
	}

	public String getContareceber_matriculaaluno() {
		if (contareceber_matriculaaluno == null) {
			contareceber_matriculaaluno = "";
		}
		return contareceber_matriculaaluno;
	}

	public void setContareceber_matriculaaluno(String contareceber_matriculaaluno) {
		this.contareceber_matriculaaluno = contareceber_matriculaaluno;
	}

	public void setMoeda(String moeda) {
		this.moeda = moeda;
	}

	public String getMoeda() {
		if (moeda == null) {
			moeda = "";
		}
		return moeda;
	}

	public String getEspecieDoc() {
		if (especieDoc == null) {
			especieDoc = "";
		}
		return especieDoc;
	}

	public void setEspecieDoc(String especieDoc) {
		this.especieDoc = especieDoc;
	}

	public String getEnderecoParceiro() {
		if (enderecoParceiro == null) {
			enderecoParceiro = "";
		}
		return enderecoParceiro;
	}

	public void setEnderecoParceiro(String enderecoParceiro) {
		this.enderecoParceiro = enderecoParceiro;
	}

	public String getSetorParceiro() {
		if (setorParceiro == null) {
			setorParceiro = "";
		}
		return setorParceiro;
	}

	public void setSetorParceiro(String setorParceiro) {
		this.setorParceiro = setorParceiro;
	}

	public String getCepParceiro() {
		if (cepParceiro == null) {
			cepParceiro = "";
		}
		return cepParceiro;
	}

	public void setCepParceiro(String cepParceiro) {
		this.cepParceiro = cepParceiro;
	}

	public void setCnpjParceiro(String cnpjParceiro) {
		this.cnpjParceiro = cnpjParceiro;
	}

	public String getCnpjParceiro() {
		if (cnpjParceiro == null) {
			cnpjParceiro = "";
		}
		return cnpjParceiro;
	}

	public void setTelParceiro(String telParceiro) {
		this.telParceiro = telParceiro;
	}

	public String getTelParceiro() {
		if (telParceiro == null) {
			telParceiro = "";
		}
		return telParceiro;
	}

	public String getCidadeParceiro() {
		if (cidadeParceiro == null) {
			cidadeParceiro = "";
		}
		return cidadeParceiro;
	}

	public void setCidadeParceiro(String cidadeParceiro) {
		this.cidadeParceiro = cidadeParceiro;
	}

	public String getEstadoParceiro() {
		if (estadoParceiro == null) {
			estadoParceiro = "";
		}
		return estadoParceiro;
	}

	public void setEstadoParceiro(String estadoParceiro) {
		this.estadoParceiro = estadoParceiro;
	}

	public Boolean getContareceber_usadescontocompostoplanodesconto() {
		if (contareceber_usadescontocompostoplanodesconto == null) {
			contareceber_usadescontocompostoplanodesconto = Boolean.FALSE;
		}
		return contareceber_usadescontocompostoplanodesconto;
	}

	public void setContareceber_usadescontocompostoplanodesconto(Boolean contareceber_usadescontocompostoplanodesconto) {
		this.contareceber_usadescontocompostoplanodesconto = contareceber_usadescontocompostoplanodesconto;
	}

	public Boolean getDescontoValidoatedataparcela() {
		if (descontoValidoatedataparcela == null) {
			descontoValidoatedataparcela = Boolean.FALSE;
		}
		return descontoValidoatedataparcela;
	}

	public void setDescontoValidoatedataparcela(Boolean descontoValidoatedataparcela) {
		this.descontoValidoatedataparcela = descontoValidoatedataparcela;
	}

	public String getContareceber_foneMantenedora() {
		return contareceber_foneMantenedora;
	}

	public void setContareceber_foneMantenedora(String contareceber_foneMantenedora) {
		this.contareceber_foneMantenedora = contareceber_foneMantenedora;
	}

	public String getCidadeUnidadeEnsino() {
		if (cidadeUnidadeEnsino == null) {
			cidadeUnidadeEnsino = "";
		}
		return cidadeUnidadeEnsino;
	}

	public void setCidadeUnidadeEnsino(String cidadeUnidadeEnsino) {
		this.cidadeUnidadeEnsino = cidadeUnidadeEnsino;
	}

	public String getEstadoUnidadeEnsino() {
		if (estadoUnidadeEnsino == null) {
			estadoUnidadeEnsino = "";
		}
		return estadoUnidadeEnsino;
	}

	public void setEstadoUnidadeEnsino(String estadoUnidadeEnsino) {
		this.estadoUnidadeEnsino = estadoUnidadeEnsino;
	}

	public String getCepUnidadeEnsino() {
		if (cepUnidadeEnsino == null) {
			cepUnidadeEnsino = "";
		}
		return cepUnidadeEnsino;
	}

	public void setCepUnidadeEnsino(String cepUnidadeEnsino) {
		this.cepUnidadeEnsino = cepUnidadeEnsino;
	}

	public String getComplementoUnidadeEnsino() {
		if (complementoUnidadeEnsino == null) {
			complementoUnidadeEnsino = "";
		}
		return complementoUnidadeEnsino;
	}

	public void setComplementoUnidadeEnsino(String complementoUnidadeEnsino) {
		this.complementoUnidadeEnsino = complementoUnidadeEnsino;
	}

	public void setSetorUnidadeEnsino(String setorUnidadeEnsino) {
		this.setorUnidadeEnsino = setorUnidadeEnsino;
	}

	public String getEnderecoUnidadeEnsino() {
		return enderecoUnidadeEnsino;
	}

	public void setEnderecoUnidadeEnsino(String enderecoUnidadeEnsino) {
		if (enderecoUnidadeEnsino == null) {
			enderecoUnidadeEnsino = "";
		}
		this.enderecoUnidadeEnsino = enderecoUnidadeEnsino;
	}

	public String getSetorUnidadeEnsino() {
		return setorUnidadeEnsino;
	}

	public String getNumeroUnidadeEnsino() {
		if (numeroUnidadeEnsino == null) {
			numeroUnidadeEnsino = "";
		}
		return numeroUnidadeEnsino;
	}

	public void setNumeroUnidadeEnsino(String numeroUnidadeEnsino) {
		this.numeroUnidadeEnsino = numeroUnidadeEnsino;
	}

	public String getTurno() {
		return turno;
	}

	public void setTurno(String turno) {
		this.turno = turno;
	}

	public String getSituacaoMatricula() {
		return situacaoMatricula;
	}

	public void setSituacaoMatricula(String situacaoMatricula) {
		this.situacaoMatricula = situacaoMatricula;
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getResponsavelFinanceiro() {
		if (responsavelFinanceiro == null) {
			responsavelFinanceiro = "";
		}
		return responsavelFinanceiro;
	}

	public void setResponsavelFinanceiro(String responsavelFinanceiro) {
		this.responsavelFinanceiro = responsavelFinanceiro;
	}

	public Integer getDiasVariacaoDataVencimento() {
		if (diasVariacaoDataVencimento == null) {
			diasVariacaoDataVencimento = 0;
		}
		return diasVariacaoDataVencimento;
	}

	public void setDiasVariacaoDataVencimento(Integer diasVariacaoDataVencimento) {
		this.diasVariacaoDataVencimento = diasVariacaoDataVencimento;
	}

	public double getDescontoprogressivo_valordescontolimite1() {
		return descontoprogressivo_valordescontolimite1;
	}

	public void setDescontoprogressivo_valordescontolimite1(double descontoprogressivo_valordescontolimite1) {
		this.descontoprogressivo_valordescontolimite1 = descontoprogressivo_valordescontolimite1;
	}

	public double getDescontoprogressivo_valordescontolimite2() {
		return descontoprogressivo_valordescontolimite2;
	}

	public void setDescontoprogressivo_valordescontolimite2(double descontoprogressivo_valordescontolimite2) {
		this.descontoprogressivo_valordescontolimite2 = descontoprogressivo_valordescontolimite2;
	}

	public double getDescontoprogressivo_valordescontolimite3() {
		return descontoprogressivo_valordescontolimite3;
	}

	public void setDescontoprogressivo_valordescontolimite3(double descontoprogressivo_valordescontolimite3) {
		this.descontoprogressivo_valordescontolimite3 = descontoprogressivo_valordescontolimite3;
	}

	public double getDescontoprogressivo_valordescontolimite4() {
		return descontoprogressivo_valordescontolimite4;
	}

	public void setDescontoprogressivo_valordescontolimite4(double descontoprogressivo_valordescontolimite4) {
		this.descontoprogressivo_valordescontolimite4 = descontoprogressivo_valordescontolimite4;
	}

	public String getDigitoVerificadorNossoNumero() {
		if (digitoVerificadorNossoNumero == null) {
			digitoVerificadorNossoNumero = "";
		}
		return digitoVerificadorNossoNumero;
	}

	public void setDigitoVerificadorNossoNumero(String digitoVerificadorNossoNumero) {
		this.digitoVerificadorNossoNumero = digitoVerificadorNossoNumero;
	}

	public String getContacorrente_codigocedente() {
		if (contacorrente_codigocedente == null) {
			contacorrente_codigocedente = "";
		}
		return contacorrente_codigocedente;
	}

	public void setContacorrente_codigocedente(String contacorrente_codigocedente) {
		this.contacorrente_codigocedente = contacorrente_codigocedente;
	}

	public String getContacorrente_convenio() {
		if (contacorrente_convenio == null) {
			contacorrente_convenio = "";
		}
		return contacorrente_convenio;
	}

	public void setContacorrente_convenio(String contacorrente_convenio) {
		this.contacorrente_convenio = contacorrente_convenio;
	}

	/**
	 * @return the modeloBoletoMatricula_nome
	 */
	public Integer getModeloBoletoMatricula_codigo() {
		if (modeloBoletoMatricula_codigo == null) {
			modeloBoletoMatricula_codigo = 0;
		}
		return modeloBoletoMatricula_codigo;
	}

	/**
	 * @param modeloBoletoMatricula_nome
	 *            the modeloBoletoMatricula_nome to set
	 */
	public void setModeloBoletoMatricula_codigo(Integer modeloBoletoMatricula_codigo) {
		this.modeloBoletoMatricula_codigo = modeloBoletoMatricula_codigo;
	}

	/**
	 * @return the modeloBoletoMensalidade_nome
	 */
	public Integer getModeloBoletoMensalidade_codigo() {
		if (modeloBoletoMensalidade_codigo == null) {
			modeloBoletoMensalidade_codigo = 0;
		}
		return modeloBoletoMensalidade_codigo;
	}

	/**
	 * @param modeloBoletoMensalidade_nome
	 *            the modeloBoletoMensalidade_nome to set
	 */
	public void setModeloBoletoMensalidade_codigo(Integer modeloBoletoMensalidade_codigo) {
		this.modeloBoletoMensalidade_codigo = modeloBoletoMensalidade_codigo;
	}
	
	

	public Integer getModeloBoletoMaterialDidatico_codigo() {
		if (modeloBoletoMaterialDidatico_codigo == null) {
			modeloBoletoMaterialDidatico_codigo = 0;
		}
		return modeloBoletoMaterialDidatico_codigo;
	}

	public void setModeloBoletoMaterialDidatico_codigo(Integer modeloBoletoMaterialDidatico_codigo) {
		this.modeloBoletoMaterialDidatico_codigo = modeloBoletoMaterialDidatico_codigo;
	}

	/**
	 * @return the modeloBoletoRequerimento_nome
	 */
	public Integer getModeloBoletoRequerimento_codigo() {
		if (modeloBoletoRequerimento_codigo == null) {
			modeloBoletoRequerimento_codigo = 0;
		}
		return modeloBoletoRequerimento_codigo;
	}

	/**
	 * @param modeloBoletoRequerimento_nome
	 *            the modeloBoletoRequerimento_nome to set
	 */
	public void setModeloBoletoRequerimento_codigo(Integer modeloBoletoRequerimento_codigo) {
		this.modeloBoletoRequerimento_codigo = modeloBoletoRequerimento_codigo;
	}

	/**
	 * @return the modeloBoletoProcessoSeletivo_nome
	 */
	public Integer getModeloBoletoProcessoSeletivo_codigo() {
		if (modeloBoletoProcessoSeletivo_codigo == null) {
			modeloBoletoProcessoSeletivo_codigo = 0;
		}
		return modeloBoletoProcessoSeletivo_codigo;
	}

	/**
	 * @param modeloBoletoProcessoSeletivo_nome
	 *            the modeloBoletoProcessoSeletivo_nome to set
	 */
	public void setModeloBoletoProcessoSeletivo_codigo(Integer modeloBoletoProcessoSeletivo_codigo) {
		this.modeloBoletoProcessoSeletivo_codigo = modeloBoletoProcessoSeletivo_codigo;
	}

	/**
	 * @return the modeloBoletoOutros_nome
	 */
	public Integer getModeloBoletoOutros_codigo() {
		if (modeloBoletoOutros_codigo == null) {
			modeloBoletoOutros_codigo = 0;
		}
		return modeloBoletoOutros_codigo;
	}

	/**
	 * @param modeloBoletoOutros_nome
	 *            the modeloBoletoOutros_nome to set
	 */
	public void setModeloBoletoOutros_codigo(Integer modeloBoletoOutros_codigo) {
		this.modeloBoletoOutros_codigo = modeloBoletoOutros_codigo;
	}

	/**
	 * @return the utilizarDadosMatrizBoleto
	 */
	public Boolean getUtilizarDadosMatrizBoleto() {
		if (utilizarDadosMatrizBoleto == null) {
			utilizarDadosMatrizBoleto = Boolean.FALSE;
		}
		return utilizarDadosMatrizBoleto;
	}

	/**
	 * @param utilizarDadosMatrizBoleto
	 *            the utilizarDadosMatrizBoleto to set
	 */
	public void setUtilizarDadosMatrizBoleto(Boolean utilizarDadosMatrizBoleto) {
		this.utilizarDadosMatrizBoleto = utilizarDadosMatrizBoleto;
	}

	/**
	 * @return the modeloboleto_apenasDescontoInstrucaoBoleto
	 */
	public Boolean getModeloboleto_apenasDescontoInstrucaoBoleto() {
		if (modeloboleto_apenasDescontoInstrucaoBoleto == null) {
			modeloboleto_apenasDescontoInstrucaoBoleto = Boolean.FALSE;
		}
		return modeloboleto_apenasDescontoInstrucaoBoleto;
	}

	/**
	 * @param modeloboleto_apenasDescontoInstrucaoBoleto
	 *            the modeloboleto_apenasDescontoInstrucaoBoleto to set
	 */
	public void setModeloboleto_apenasDescontoInstrucaoBoleto(Boolean modeloboleto_apenasDescontoInstrucaoBoleto) {
		this.modeloboleto_apenasDescontoInstrucaoBoleto = modeloboleto_apenasDescontoInstrucaoBoleto;
	}

	public Boolean getDescontoprogressivo_utilizarDiaFixo() {
		if (descontoprogressivo_utilizarDiaFixo == null) {
			descontoprogressivo_utilizarDiaFixo = false;
		}
		return descontoprogressivo_utilizarDiaFixo;
	}

	public void setDescontoprogressivo_utilizarDiaFixo(Boolean descontoprogressivo_utilizarDiaFixo) {
		this.descontoprogressivo_utilizarDiaFixo = descontoprogressivo_utilizarDiaFixo;
	}

	public String getBanco_digito() {
		if (banco_digito == null) {
			banco_digito = "";
		}
		return banco_digito;
	}

	public void setBanco_digito(String banco_digito) {
		this.banco_digito = banco_digito;
	}

	public Integer getModeloBoletoRenegociacao_codigo() {
		if (modeloBoletoRenegociacao_codigo == null) {
			modeloBoletoRenegociacao_codigo = 0;
		}
		return modeloBoletoRenegociacao_codigo;
	}

	public void setModeloBoletoRenegociacao_codigo(Integer modeloBoletoRenegociacao_codigo) {
		this.modeloBoletoRenegociacao_codigo = modeloBoletoRenegociacao_codigo;
	}

	/**
	 * @return the descontoprogressivo_utilizarDiaUtil
	 */
	public Boolean getDescontoprogressivo_utilizarDiaUtil() {
		if (descontoprogressivo_utilizarDiaUtil == null) {
			descontoprogressivo_utilizarDiaUtil = false;
		}
		return descontoprogressivo_utilizarDiaUtil;
	}

	/**
	 * @param descontoprogressivo_utilizarDiaUtil
	 *            the descontoprogressivo_utilizarDiaUtil to set
	 */
	public void setDescontoprogressivo_utilizarDiaUtil(Boolean descontoprogressivo_utilizarDiaUtil) {
		this.descontoprogressivo_utilizarDiaUtil = descontoprogressivo_utilizarDiaUtil;
	}

	public String getContareceber_tipoDescontoAluno() {
		if (contareceber_tipoDescontoAluno == null) {
			contareceber_tipoDescontoAluno = "";
		}
		return contareceber_tipoDescontoAluno;
	}

	public void setContareceber_tipoDescontoAluno(String contareceber_tipoDescontoAluno) {
		this.contareceber_tipoDescontoAluno = contareceber_tipoDescontoAluno;
	}

	public Double getContareceber_percDescontoAluno() {
		if (contareceber_percDescontoAluno == null) {
			contareceber_percDescontoAluno = 0.0;
		}
		return contareceber_percDescontoAluno;
	}

	public void setContareceber_percDescontoAluno(Double contareceber_percDescontoAluno) {
		this.contareceber_percDescontoAluno = contareceber_percDescontoAluno;
	}

	public Double getContareceber_valorDescontoAluno() {
		if (contareceber_valorDescontoAluno == null) {
			contareceber_valorDescontoAluno = 0.0;
		}
		return contareceber_valorDescontoAluno;
	}

	public void setContareceber_valorDescontoAluno(Double contareceber_valorDescontoAluno) {
		this.contareceber_valorDescontoAluno = contareceber_valorDescontoAluno;
	}

	public Boolean getContacorrente_utilizaDadosInformadosCCparaGeracaoBoleto() {
		if (contacorrente_utilizaDadosInformadosCCparaGeracaoBoleto == null) {
			contacorrente_utilizaDadosInformadosCCparaGeracaoBoleto = Boolean.FALSE;
		}
		return contacorrente_utilizaDadosInformadosCCparaGeracaoBoleto;
	}

	public void setContacorrente_utilizaDadosInformadosCCparaGeracaoBoleto(Boolean contacorrente_utilizaDadosInformadosCCparaGeracaoBoleto) {
		this.contacorrente_utilizaDadosInformadosCCparaGeracaoBoleto = contacorrente_utilizaDadosInformadosCCparaGeracaoBoleto;
	}

	public Boolean getContaCorrente_carteiraRegistrada() {
		if (contaCorrente_carteiraRegistrada == null) {
			contaCorrente_carteiraRegistrada = Boolean.FALSE;
		}
		return contaCorrente_carteiraRegistrada;
	}

	public void setContaCorrente_carteiraRegistrada(Boolean contaCorrente_carteiraRegistrada) {
		this.contaCorrente_carteiraRegistrada = contaCorrente_carteiraRegistrada;
	}

	public Boolean getCondicao_aplicarCalculoComBaseDescontosCalculados() {
		if (condicao_aplicarCalculoComBaseDescontosCalculados == null) {
			condicao_aplicarCalculoComBaseDescontosCalculados = Boolean.FALSE;
		}
		return condicao_aplicarCalculoComBaseDescontosCalculados;
	}

	public void setCondicao_aplicarCalculoComBaseDescontosCalculados(Boolean condicao_aplicarCalculoComBaseDescontosCalculados) {
		this.condicao_aplicarCalculoComBaseDescontosCalculados = condicao_aplicarCalculoComBaseDescontosCalculados;
	}

	public HashMap<String, PlanoFinanceiroAlunoDescricaoDescontosVO> getMapPlanoFinanceiroAlunoDescricaoDescontosVOs() {
		if (mapPlanoFinanceiroAlunoDescricaoDescontosVOs == null) {
			mapPlanoFinanceiroAlunoDescricaoDescontosVOs = new HashMap<String, PlanoFinanceiroAlunoDescricaoDescontosVO>(0);
		}
		return mapPlanoFinanceiroAlunoDescricaoDescontosVOs;
	}

	public void setMapPlanoFinanceiroAlunoDescricaoDescontosVOs(HashMap<String, PlanoFinanceiroAlunoDescricaoDescontosVO> mapPlanoFinanceiroAlunoDescricaoDescontosVOs) {
		this.mapPlanoFinanceiroAlunoDescricaoDescontosVOs = mapPlanoFinanceiroAlunoDescricaoDescontosVOs;
	}

	public List<PlanoFinanceiroAlunoDescricaoDescontosVO> getListaPlanoFinanceiroAlunoDescricaoDescontos() {
		if (listaPlanoFinanceiroAlunoDescricaoDescontos == null) {
			listaPlanoFinanceiroAlunoDescricaoDescontos = new ArrayList<PlanoFinanceiroAlunoDescricaoDescontosVO>(0);
		}
		return listaPlanoFinanceiroAlunoDescricaoDescontos;
	}

	public void setListaPlanoFinanceiroAlunoDescricaoDescontos(List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaPlanoFinanceiroAlunoDescricaoDescontos) {
		this.listaPlanoFinanceiroAlunoDescricaoDescontos = listaPlanoFinanceiroAlunoDescricaoDescontos;
	}

	/**
	 * @return the contareceber_valorCusteadoContaReceber
	 */
	public double getContareceber_valorCusteadoContaReceber() {
		return contareceber_valorCusteadoContaReceber;
	}

	/**
	 * @param contareceber_valorCusteadoContaReceber
	 *            the contareceber_valorCusteadoContaReceber to set
	 */
	public void setContareceber_valorCusteadoContaReceber(double contareceber_valorCusteadoContaReceber) {
		this.contareceber_valorCusteadoContaReceber = contareceber_valorCusteadoContaReceber;
	}

	/**
	 * @return the contareceber_descricaoValorCusteadoContaReceber
	 */
	public String getContareceber_descricaoValorCusteadoContaReceber() {
		return contareceber_descricaoValorCusteadoContaReceber;
	}

	/**
	 * @param contareceber_descricaoValorCusteadoContaReceber
	 *            the contareceber_descricaoValorCusteadoContaReceber to set
	 */
	public void setContareceber_descricaoValorCusteadoContaReceber(String contareceber_descricaoValorCusteadoContaReceber) {
		this.contareceber_descricaoValorCusteadoContaReceber = contareceber_descricaoValorCusteadoContaReceber;
	}

	/**
	 * @return the contareceber_valorBaseContaReceber
	 */
	public double getContareceber_valorBaseContaReceber() {
		return contareceber_valorBaseContaReceber;
	}

	/**
	 * @param contareceber_valorBaseContaReceber
	 *            the contareceber_valorBaseContaReceber to set
	 */
	public void setContareceber_valorBaseContaReceber(double contareceber_valorBaseContaReceber) {
		this.contareceber_valorBaseContaReceber = contareceber_valorBaseContaReceber;
	}

	public String getPessoa_complemento() {
		if (pessoa_complemento == null) {
			pessoa_complemento = "";
		}
		return pessoa_complemento;
	}

	public void setPessoa_complemento(String pessoa_complemento) {
		this.pessoa_complemento = pessoa_complemento;
	}

	public String getPessoa_numero() {
		if (pessoa_numero == null) {
			pessoa_numero = "";
		}
		return pessoa_numero;
	}

	public void setPessoa_numero(String pessoa_numero) {
		this.pessoa_numero = pessoa_numero;
	}

	public String getContaReceber_tipoPessoa() {
		if (contaReceber_tipoPessoa == null) {
			contaReceber_tipoPessoa = "";
		}
		return contaReceber_tipoPessoa;
	}

	public void setContaReceber_tipoPessoa(String contaReceber_tipoPessoa) {
		this.contaReceber_tipoPessoa = contaReceber_tipoPessoa;
	}

	public String getContacorrente_digitocodigocedente() {
		if (contacorrente_digitocodigocedente == null) {
			contacorrente_digitocodigocedente = "";
		}
		return contacorrente_digitocodigocedente;
	}

	public void setContacorrente_digitocodigocedente(String contacorrente_digitocodigocedente) {
		this.contacorrente_digitocodigocedente = contacorrente_digitocodigocedente;
	}

	public Boolean getModeloboleto_utilizarDescricaoDescontoPersonalizado() {
		if (modeloboleto_utilizarDescricaoDescontoPersonalizado == null) {
			modeloboleto_utilizarDescricaoDescontoPersonalizado = Boolean.FALSE;
		}
		return modeloboleto_utilizarDescricaoDescontoPersonalizado;
	}

	public void setModeloboleto_utilizarDescricaoDescontoPersonalizado(Boolean modeloboleto_utilizarDescricaoDescontoPersonalizado) {
		this.modeloboleto_utilizarDescricaoDescontoPersonalizado = modeloboleto_utilizarDescricaoDescontoPersonalizado;
	}

	public String getModeloboleto_textoTopo() {
		if (modeloboleto_textoTopo == null) {
			modeloboleto_textoTopo = "";
		}
		return modeloboleto_textoTopo;
	}

	public void setModeloboleto_textoTopo(String modeloboleto_textoTopo) {
		this.modeloboleto_textoTopo = modeloboleto_textoTopo;
	}

	public String getModeloboleto_instrucao1() {
		if (modeloboleto_instrucao1 == null) {
			modeloboleto_instrucao1 = "";
		}
		return modeloboleto_instrucao1;
	}

	public void setModeloboleto_instrucao1(String modeloboleto_instrucao1) {
		this.modeloboleto_instrucao1 = modeloboleto_instrucao1;
	}

	public String getModeloboleto_instrucao2() {
		if (modeloboleto_instrucao2 == null) {
			modeloboleto_instrucao2 = "";
		}
		return modeloboleto_instrucao2;
	}

	public void setModeloboleto_instrucao2(String modeloboleto_instrucao2) {
		this.modeloboleto_instrucao2 = modeloboleto_instrucao2;
	}

	public String getModeloboleto_instrucao3() {
		if (modeloboleto_instrucao3 == null) {
			modeloboleto_instrucao3 = "";
		}
		return modeloboleto_instrucao3;
	}

	public void setModeloboleto_instrucao3(String modeloboleto_instrucao3) {
		this.modeloboleto_instrucao3 = modeloboleto_instrucao3;
	}

	public String getModeloboleto_instrucao4() {
		if (modeloboleto_instrucao4 == null) {
			modeloboleto_instrucao4 = "";
		}
		return modeloboleto_instrucao4;
	}

	public void setModeloboleto_instrucao4(String modeloboleto_instrucao4) {
		this.modeloboleto_instrucao4 = modeloboleto_instrucao4;
	}

	public String getModeloboleto_instrucao5() {
		if (modeloboleto_instrucao5 == null) {
			modeloboleto_instrucao5 = "";
		}
		return modeloboleto_instrucao5;
	}

	public void setModeloboleto_instrucao5(String modeloboleto_instrucao5) {
		this.modeloboleto_instrucao5 = modeloboleto_instrucao5;
	}

	public String getModeloboleto_instrucao6() {
		if (modeloboleto_instrucao6 == null) {
			modeloboleto_instrucao6 = "";
		}
		return modeloboleto_instrucao6;
	}

	public void setModeloboleto_instrucao6(String modeloboleto_instrucao6) {
		this.modeloboleto_instrucao6 = modeloboleto_instrucao6;
	}

	public String getModeloboleto_textoRodape() {
		if (modeloboleto_textoRodape == null) {
			modeloboleto_textoRodape = "";
		}
		return modeloboleto_textoRodape;
	}

	public void setModeloboleto_textoRodape(String modeloboleto_textoRodape) {
		this.modeloboleto_textoRodape = modeloboleto_textoRodape;
	}

	public String getModeloboleto_textoTopoInferior() {
		if (modeloboleto_textoTopoInferior == null) {
			modeloboleto_textoTopoInferior = "";
		}
		return modeloboleto_textoTopoInferior;
	}

	public void setModeloboleto_textoTopoInferior(String modeloboleto_textoTopoInferior) {
		this.modeloboleto_textoTopoInferior = modeloboleto_textoTopoInferior;
	}

	public String getModeloboleto_instrucao1Inferior() {
		if (modeloboleto_instrucao1Inferior == null) {
			modeloboleto_instrucao1Inferior = "";
		}
		return modeloboleto_instrucao1Inferior;
	}

	public void setModeloboleto_instrucao1Inferior(String modeloboleto_instrucao1Inferior) {
		this.modeloboleto_instrucao1Inferior = modeloboleto_instrucao1Inferior;
	}

	public String getModeloboleto_instrucao2Inferior() {
		if (modeloboleto_instrucao2Inferior == null) {
			modeloboleto_instrucao2Inferior = "";
		}
		return modeloboleto_instrucao2Inferior;
	}

	public void setModeloboleto_instrucao2Inferior(String modeloboleto_instrucao2Inferior) {
		this.modeloboleto_instrucao2Inferior = modeloboleto_instrucao2Inferior;
	}

	public String getModeloboleto_instrucao3Inferior() {
		if (modeloboleto_instrucao3Inferior == null) {
			modeloboleto_instrucao3Inferior = "";
		}
		return modeloboleto_instrucao3Inferior;
	}

	public void setModeloboleto_instrucao3Inferior(String modeloboleto_instrucao3Inferior) {
		this.modeloboleto_instrucao3Inferior = modeloboleto_instrucao3Inferior;
	}

	public String getModeloboleto_instrucao4Inferior() {
		if (modeloboleto_instrucao4Inferior == null) {
			modeloboleto_instrucao4Inferior = "";
		}
		return modeloboleto_instrucao4Inferior;
	}

	public void setModeloboleto_instrucao4Inferior(String modeloboleto_instrucao4Inferior) {
		this.modeloboleto_instrucao4Inferior = modeloboleto_instrucao4Inferior;
	}

	public String getModeloboleto_instrucao5Inferior() {
		if (modeloboleto_instrucao5Inferior == null) {
			modeloboleto_instrucao5Inferior = "";
		}
		return modeloboleto_instrucao5Inferior;
	}

	public void setModeloboleto_instrucao5Inferior(String modeloboleto_instrucao5Inferior) {
		this.modeloboleto_instrucao5Inferior = modeloboleto_instrucao5Inferior;
	}

	public String getModeloboleto_instrucao6Inferior() {
		if (modeloboleto_instrucao6Inferior == null) {
			modeloboleto_instrucao6Inferior = "";
		}
		return modeloboleto_instrucao6Inferior;
	}

	public void setModeloboleto_instrucao6Inferior(String modeloboleto_instrucao6Inferior) {
		this.modeloboleto_instrucao6Inferior = modeloboleto_instrucao6Inferior;
	}

	public String getModeloboleto_textoRodapeInferior() {
		if (modeloboleto_textoRodapeInferior == null) {
			modeloboleto_textoRodapeInferior = "";
		}
		return modeloboleto_textoRodapeInferior;
	}

	public void setModeloboleto_textoRodapeInferior(String modeloboleto_textoRodapeInferior) {
		this.modeloboleto_textoRodapeInferior = modeloboleto_textoRodapeInferior;
	}

	/**
	 * @return the processoIntegracaoFinanceiroDetalhe
	 */
	public ProcessamentoIntegracaoFinanceiraDetalheVO getProcessoIntegracaoFinanceiroDetalhe() {
		if (processoIntegracaoFinanceiroDetalhe == null) {
			processoIntegracaoFinanceiroDetalhe = new ProcessamentoIntegracaoFinanceiraDetalheVO();
		}
		return processoIntegracaoFinanceiroDetalhe;
	}

	/**
	 * @param processoIntegracaoFinanceiroDetalhe
	 *            the processoIntegracaoFinanceiroDetalhe to set
	 */
	public void setProcessoIntegracaoFinanceiroDetalhe(ProcessamentoIntegracaoFinanceiraDetalheVO processoIntegracaoFinanceiroDetalhe) {
		this.processoIntegracaoFinanceiroDetalhe = processoIntegracaoFinanceiroDetalhe;
	}

	public boolean getIsUtilizaFinanceiroIntegrado() {
		return Uteis.isAtributoPreenchido(getProcessoIntegracaoFinanceiroDetalhe().getCodigo());
	}

	public Boolean getUtilizaFinanceiroIntegrado() {
		return Uteis.isAtributoPreenchido(getProcessoIntegracaoFinanceiroDetalhe().getCodigo());
	}

	public Boolean getOcultarCodBarraLinhaDigitavel() {
		if (ocultarCodBarraLinhaDigitavel == null) {
			ocultarCodBarraLinhaDigitavel = Boolean.FALSE;
		}
		return ocultarCodBarraLinhaDigitavel;
	}

	public void setOcultarCodBarraLinhaDigitavel(Boolean ocultarCodBarraLinhaDigitavel) {
		this.ocultarCodBarraLinhaDigitavel = ocultarCodBarraLinhaDigitavel;
	}

	public String getTelefoneUnidadeEnsino() {
		if (telefoneUnidadeEnsino == null) {
			telefoneUnidadeEnsino = "";
		}
		return telefoneUnidadeEnsino;
	}

	public void setTelefoneUnidadeEnsino(String telefoneUnidadeEnsino) {
		this.telefoneUnidadeEnsino = telefoneUnidadeEnsino;
	}

	public String getContaReceber_diretorioImgLinhaDigitavel() {
		if (contaReceber_diretorioImgLinhaDigitavel == null) {
			contaReceber_diretorioImgLinhaDigitavel = "";
		}
		return contaReceber_diretorioImgLinhaDigitavel;
	}

	public void setContaReceber_diretorioImgLinhaDigitavel(String contaReceber_diretorioImgLinhaDigitavel) {
		this.contaReceber_diretorioImgLinhaDigitavel = contaReceber_diretorioImgLinhaDigitavel;
	}

	public Integer getModeloBoletoBiblioteca_codigo() {
		if (modeloBoletoBiblioteca_codigo == null) {
			modeloBoletoBiblioteca_codigo = 0;
		}
		return modeloBoletoBiblioteca_codigo;
	}

	public void setModeloBoletoBiblioteca_codigo(Integer modeloBoletoBiblioteca_codigo) {
		this.modeloBoletoBiblioteca_codigo = modeloBoletoBiblioteca_codigo;
	}

	public String codigoAdministrativo;

	public String getCodigoAdministrativo() {
		if (codigoAdministrativo == null) {
			codigoAdministrativo = Uteis.isAtributoPreenchido(getProcessoIntegracaoFinanceiroDetalhe().getCodigo()) ? getProcessoIntegracaoFinanceiroDetalhe().getCodigoPessoaFinanceiro() : getContareceber_nossonumero();
		}
		return codigoAdministrativo;
	}

	public String dataMaximoRecebimento;

	public String getDataMaximoRecebimento() {
		if (dataMaximoRecebimento == null) {
			dataMaximoRecebimento = Uteis.isAtributoPreenchido(getProcessoIntegracaoFinanceiroDetalhe().getCodigo()) ? Uteis.getData(getProcessoIntegracaoFinanceiroDetalhe().getDataMaximaPagamento()) : "";
		}
		return dataMaximoRecebimento;
	}

	public Integer getCodigoCidadeUnidadeEnsino() {
		if (codigoCidadeUnidadeEnsino == null) {
			codigoCidadeUnidadeEnsino = 0;
		}
		return codigoCidadeUnidadeEnsino;
	}

	public void setCodigoCidadeUnidadeEnsino(Integer codigoCidadeUnidadeEnsino) {
		this.codigoCidadeUnidadeEnsino = codigoCidadeUnidadeEnsino;
	}

	public Date getContareceber_dataoriginalvencimento() {
		if (contareceber_dataoriginalvencimento == null) {
			contareceber_dataoriginalvencimento = contareceber_datavencimento;
		}
		return contareceber_dataoriginalvencimento;
	}

	public void setContareceber_dataoriginalvencimento(Date contareceber_dataoriginalvencimento) {
		this.contareceber_dataoriginalvencimento = contareceber_dataoriginalvencimento;
	}

	public Date getContareceber_datavencimentodiautil() {
		if (contareceber_datavencimentodiautil == null) {
			contareceber_datavencimentodiautil = contareceber_datavencimento;
		}
		return contareceber_datavencimentodiautil;
	}

	public void setContareceber_datavencimentodiautil(Date contareceber_datavencimentodiautil) {
		this.contareceber_datavencimentodiautil = contareceber_datavencimentodiautil;
	}

	public Double getContareceber_valorDescontoRateio() {
		if (contareceber_valorDescontoRateio == null) {
			contareceber_valorDescontoRateio = 0.0;
		}
		return contareceber_valorDescontoRateio;
	}

	public void setContareceber_valorDescontoRateio(Double contareceber_valorDescontoRateio) {
		this.contareceber_valorDescontoRateio = contareceber_valorDescontoRateio;
	}

	public double getContareceber_acrescimo() {
		return contareceber_acrescimo;
	}

	public void setContareceber_acrescimo(double contareceber_acrescimo) {
		this.contareceber_acrescimo = contareceber_acrescimo;
	}
	
	
	
	public double getContareceber_valorIndiceReajustePorAtraso() {
		return contareceber_valorIndiceReajustePorAtraso;
	}

	public void setContareceber_valorIndiceReajustePorAtraso(double contareceber_valorIndiceReajustePorAtraso) {
		this.contareceber_valorIndiceReajustePorAtraso = contareceber_valorIndiceReajustePorAtraso;
	}

	public String getTotaisInstrucoesSuperior(){
		StringBuilder sb = new StringBuilder("");
		sb.append("<html>");
		sb.append("<body>");
		sb.append(Uteis.removeCabecarioHTML(getModeloboleto_instrucao1()));
		sb.append(Uteis.removeCabecarioHTML(getModeloboleto_instrucao2()));
		sb.append(Uteis.removeCabecarioHTML(getModeloboleto_instrucao3()));
		sb.append(Uteis.removeCabecarioHTML(getModeloboleto_instrucao4()));
		sb.append(Uteis.removeCabecarioHTML(getModeloboleto_instrucao5()));
		sb.append(Uteis.removeCabecarioHTML(getModeloboleto_instrucao6()));
		sb.append(Uteis.removeCabecarioHTML(getModeloboleto_textoRodape()));
		sb.append("</body>");
		sb.append("/<html>");
		return sb.toString();
	}
	
	public String getTotaisInstrucoesInferior(){
		StringBuilder sb = new StringBuilder("");
		sb.append("<html>");
		sb.append("<body>");
		sb.append(Uteis.removeCabecarioHTML(getModeloboleto_instrucao1Inferior()));
		sb.append(Uteis.removeCabecarioHTML(getModeloboleto_instrucao2Inferior()));
		sb.append(Uteis.removeCabecarioHTML(getModeloboleto_instrucao3Inferior()));
		sb.append(Uteis.removeCabecarioHTML(getModeloboleto_instrucao4Inferior()));
		sb.append(Uteis.removeCabecarioHTML(getModeloboleto_instrucao5Inferior()));
		sb.append(Uteis.removeCabecarioHTML(getModeloboleto_instrucao6Inferior()));
		sb.append(Uteis.removeCabecarioHTML(getModeloboleto_textoRodapeInferior()));
		sb.append("</body>");
		sb.append("/<html>");
		return sb.toString();
	}

	public void setTotaisInstrucoesSuperior(String totaisInstrucoesSuperior) {
		this.totaisInstrucoesSuperior = totaisInstrucoesSuperior;
	}

	public void setTotaisInstrucoesInferior(String totaisInstrucoesInferior) {
		this.totaisInstrucoesInferior = totaisInstrucoesInferior;
	}

	public BigDecimal getContaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa() {
		if (contaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa == null) {
			contaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa = BigDecimal.ZERO;
		}
		return contaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa;
	}

	public void setContaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa(BigDecimal contaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa) {
		this.contaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa = contaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa;
	}

	public BigDecimal getContaReceber_valorIndiceReajuste() {
		if (contaReceber_valorIndiceReajuste == null) {
			contaReceber_valorIndiceReajuste = BigDecimal.ZERO;
		}
		return contaReceber_valorIndiceReajuste;
	}

	public void setContaReceber_valorIndiceReajuste(BigDecimal contaReceber_valorIndiceReajuste) {
		this.contaReceber_valorIndiceReajuste = contaReceber_valorIndiceReajuste;
	}
	
	/**
	 * INICIO MERGE EDIGAR - 05-04-18 
	 */
	private Boolean contareceber_contaEditadaManualmente;
	private Integer contareceber_diaLimite1;
	private Integer contareceber_diaLimite2;
	private Integer contareceber_diaLimite3;
	private Integer contareceber_diaLimite4;
	private Boolean contareceber_utilizarDescontoProgressivoManual;

	public Boolean getContareceber_contaEditadaManualmente() {
		if (contareceber_contaEditadaManualmente == null) {
			contareceber_contaEditadaManualmente = Boolean.FALSE;
		}
		return contareceber_contaEditadaManualmente;
	}

	public void setContareceber_contaEditadaManualmente(Boolean contareceber_contaEditadaManualmente) {
		this.contareceber_contaEditadaManualmente = contareceber_contaEditadaManualmente;
	}

	public Integer getContareceber_diaLimite1() {
		return contareceber_diaLimite1;
	}

	public void setContareceber_diaLimite1(Integer contareceber_diaLimite1) {
		this.contareceber_diaLimite1 = contareceber_diaLimite1;
	}

	public Integer getContareceber_diaLimite2() {
		return contareceber_diaLimite2;
	}

	public void setContareceber_diaLimite2(Integer contareceber_diaLimite2) {
		this.contareceber_diaLimite2 = contareceber_diaLimite2;
	}

	public Integer getContareceber_diaLimite3() {
		return contareceber_diaLimite3;
	}

	public void setContareceber_diaLimite3(Integer contareceber_diaLimite3) {
		this.contareceber_diaLimite3 = contareceber_diaLimite3;
	}

	public Integer getContareceber_diaLimite4() {
		return contareceber_diaLimite4;
	}

	public void setContareceber_diaLimite4(Integer contareceber_diaLimite4) {
		this.contareceber_diaLimite4 = contareceber_diaLimite4;
	}

	public Boolean getContareceber_utilizarDescontoProgressivoManual() {
		if (contareceber_utilizarDescontoProgressivoManual == null) {
			contareceber_utilizarDescontoProgressivoManual = Boolean.FALSE;
		}
		return contareceber_utilizarDescontoProgressivoManual;
	}

	public void setContareceber_utilizarDescontoProgressivoManual(Boolean contareceber_utilizarDescontoProgressivoManual) {
		this.contareceber_utilizarDescontoProgressivoManual = contareceber_utilizarDescontoProgressivoManual;
	}

	public String getContareceber_situacao() {
		if (contareceber_situacao == null) {
			contareceber_situacao = "";
		}
		return contareceber_situacao;
	}

	public void setContareceber_situacao(String contareceber_situacao) {
		this.contareceber_situacao = contareceber_situacao;
	}

	public Boolean getContacorrente_habilitarRegistroRemessaOnline() {
		if (contacorrente_habilitarRegistroRemessaOnline == null) {
			contacorrente_habilitarRegistroRemessaOnline = Boolean.FALSE;
		}
		return contacorrente_habilitarRegistroRemessaOnline;
	}

	public void setContacorrente_habilitarRegistroRemessaOnline(Boolean contacorrente_habilitarRegistroRemessaOnline) {
		this.contacorrente_habilitarRegistroRemessaOnline = contacorrente_habilitarRegistroRemessaOnline;
	}	
	
	public String getObterLogoBanco(String caminhoPastaWeb){
		String bancoLogo = "";
			if (getBanco_nrbanco().equals("341")) {
				bancoLogo = caminhoPastaWeb + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoBancoItau.png";
			} else if (getBanco_nrbanco().equals("001")) {
				bancoLogo = caminhoPastaWeb + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoBancoBrasil.png";
			} else if (getBanco_nrbanco().equals("104")) {
				bancoLogo = caminhoPastaWeb + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoBancoCaixa.png";
			} else if (getBanco_nrbanco().equals("104-S")) {
				bancoLogo = caminhoPastaWeb + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoBancoCaixa.png";
				setBanco_nrbanco("104");
			} else if (getBanco_nrbanco().equals("104-N15")) {
				bancoLogo = caminhoPastaWeb + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoBancoCaixa.png";
			} else if (getBanco_nrbanco().equals("756")) {
				bancoLogo = caminhoPastaWeb + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoBancoSicoob.png";
			} else if (getBanco_nrbanco().equals("748")) {
            	bancoLogo = caminhoPastaWeb + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoBancoSicred.png";
            } else if (getBanco_nrbanco().equals("033")) {
            	bancoLogo = caminhoPastaWeb + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoBancoSantander.png";
            }else if (getBanco_nrbanco().equals("021")) {
            	bancoLogo = caminhoPastaWeb + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoBancoBanestes.png";
            }
		return bancoLogo;
	}
	
	public Boolean getTipoOrigemContaReceberAptaGerarRemessaOnline() {
		return ((getContaCorrenteVO().getGerarRemessaMatriculaAut() && getContareceber_tipoorigem().equals(TipoOrigemContaReceber.MATRICULA.valor)) 
				|| (getContaCorrenteVO().getGerarRemessaMateriaDidaticoAut() && getContareceber_tipoorigem().equals(TipoOrigemContaReceber.MATERIAL_DIDATICO.valor))
				|| (getContaCorrenteVO().getGerarRemessaParcelasAut() && getContareceber_tipoorigem().equals(TipoOrigemContaReceber.MENSALIDADE.valor))								
				|| (getContaCorrenteVO().getGerarRemessaOutrosAut() && getContareceber_tipoorigem().equals(TipoOrigemContaReceber.OUTROS.valor)) 
				|| (getContaCorrenteVO().getGerarRemessaRequerimentoAut() && getContareceber_tipoorigem().equals(TipoOrigemContaReceber.REQUERIMENTO.valor)) 
				|| (getContaCorrenteVO().getGerarRemessaBibliotecaAut() && getContareceber_tipoorigem().equals(TipoOrigemContaReceber.BIBLIOTECA.valor)) 
				|| (getContaCorrenteVO().getGerarRemessaInscProcSeletivoAut() && getContareceber_tipoorigem().equals(TipoOrigemContaReceber.INSCRICAO_PROCESSO_SELETIVO.valor)) 
				|| (getContaCorrenteVO().getGerarRemessaDevChequeAut() && getContareceber_tipoorigem().equals(TipoOrigemContaReceber.DEVOLUCAO_CHEQUE.valor)) 
				|| (getContaCorrenteVO().getGerarRemessaConvenioAut() && getContareceber_tipoorigem().equals(TipoOrigemContaReceber.BOLSA_CUSTEADA_CONVENIO.valor))  
				|| (getContaCorrenteVO().getGerarRemessaInclusaoReposicaoAut() && getContareceber_tipoorigem().equals(TipoOrigemContaReceber.INCLUSAOREPOSICAO.valor)) 
				|| (getContaCorrenteVO().getGerarRemessaContratoReceitaAut() && getContareceber_tipoorigem().equals(TipoOrigemContaReceber.CONTRATO_RECEITA.valor))								
				|| (getContaCorrenteVO().getGerarRemessaNegociacaoAut() && getContareceber_tipoorigem().equals(TipoOrigemContaReceber.NEGOCIACAO.valor)));
		
	}
	
	public double getContareceber_totalAcrescimo() {
		return Uteis.arrendondarForcando2CadasDecimais(getContareceber_acrescimo()+getContareceber_valorIndiceReajustePorAtraso()+getContaReceber_valorReajusteDiferencaParcelaRecebidaOuEnviadaRemessa().doubleValue());
	}

	public Date getDataVencimentoRemessaOnline() {		
		return dataVencimentoRemessaOnline;
	}

	public void setDataVencimentoRemessaOnline(Date dataVencimentoRemessaOnline) {
		this.dataVencimentoRemessaOnline = dataVencimentoRemessaOnline;
	}

	public Double getValorAcrescimoRemessaOnline() {
		return valorAcrescimoRemessaOnline;
	}

	public void setValorAcrescimoRemessaOnline(Double valorAcrescimoRemessaOnline) {
		this.valorAcrescimoRemessaOnline = valorAcrescimoRemessaOnline;
	}

	public void setDataMaximoRecebimento(String dataMaximoRecebimento) {
		this.dataMaximoRecebimento = dataMaximoRecebimento;
	}

	public ContaCorrenteVO getContaCorrenteVO() {
		if(contaCorrenteVO == null) {
			contaCorrenteVO =  new ContaCorrenteVO();
		}
		return contaCorrenteVO;
	}

	public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
		this.contaCorrenteVO = contaCorrenteVO;
	}

	public ContaReceberVO getContaReceberVO() {
		if(contaReceberVO == null) {
			contaReceberVO =  new ContaReceberVO();
		}
		return contaReceberVO;
	}

	public void setContaReceberVO(ContaReceberVO contaReceberVO) {
		this.contaReceberVO = contaReceberVO;
	}
	
	public String getContaCorrenteCnab() {
		if (contaCorrenteCnab == null) {
			contaCorrenteCnab = "";
		}
		return contaCorrenteCnab;
	}

	public void setContaCorrenteCnab(String contaCorrenteCnab) {
		this.contaCorrenteCnab = contaCorrenteCnab;
	}
	
	public Boolean getParceiroIsentarMulta() {
		if (parceiroIsentarMulta == null) {
			parceiroIsentarMulta = Boolean.FALSE;
		}
		return parceiroIsentarMulta;
	}

	public void setParceiroIsentarMulta(Boolean parceiroIsentarMulta) {
		this.parceiroIsentarMulta = parceiroIsentarMulta;
	}

	public Boolean getParceiroIsentarJuro() {
		if (parceiroIsentarJuro == null) {
			parceiroIsentarJuro = Boolean.FALSE;
		}
		return parceiroIsentarJuro;
	}

	public void setParceiroIsentarJuro(Boolean parceiroIsentarJuro) {
		this.parceiroIsentarJuro = parceiroIsentarJuro;
	}

	private void calcularMultaJuroBoletoBancarioRelVO(BoletoBancarioRelVO obj, ConfiguracaoFinanceiroVO conf, double valorUtilizadoParaRealizarCalculoJuroOuMulta, boolean permiteCalcularMultaSobreValorCheio) {
		calcularMultaBoletoBancarioRelVO(obj, conf, valorUtilizadoParaRealizarCalculoJuroOuMulta, permiteCalcularMultaSobreValorCheio);
		calcularJuroBoletoBancarioRelVO(obj, conf, valorUtilizadoParaRealizarCalculoJuroOuMulta);
	}
	
	private void calcularMultaBoletoBancarioRelVO(BoletoBancarioRelVO obj, ConfiguracaoFinanceiroVO conf, double valorUtilizadoParaRealizarCalculoJuroOuMulta, boolean permiteCalcularMultaSobreValorCheio) {
		if (Uteis.isAtributoPreenchido(obj.getContareceber_multa())) {
			return;
		}
		if (obj.getContaReceber_tipoPessoa().equals("PA") && obj.getParceiroIsentarMulta()) {
			obj.setContareceber_multa(0.0);
		} else {
			double porcentagemMulta = Uteis.isAtributoPreenchido(obj.getContareceber_multaporcentagem()) ? obj.getContareceber_multaporcentagem() : conf.getPercentualMultaPadrao();
			double multa = 0.0;
			if (conf.getCobrarJuroMultaSobreValorCheioConta() && permiteCalcularMultaSobreValorCheio) {
				multa = Uteis.trunc(new BigDecimal(obj.getContareceber_valor() * porcentagemMulta / 100), 2).doubleValue();
			} else {
				multa = Uteis.trunc(new BigDecimal(valorUtilizadoParaRealizarCalculoJuroOuMulta * porcentagemMulta / 100), 2).doubleValue();
			}
			obj.setContareceber_multa(multa);
		}
	}
	
	private void calcularJuroBoletoBancarioRelVO(BoletoBancarioRelVO obj, ConfiguracaoFinanceiroVO conf, double valorUtilizadoParaRealizarCalculoJuroOuMulta) {
		if (obj.getContaReceber_tipoPessoa().equals("PA") && obj.getParceiroIsentarJuro()) {
			obj.setContareceber_juro(0.0);
		} else {
			double juro = 0.0;
			double valorComJuro = 0.0;
			double porcentagemJuro = Uteis.isAtributoPreenchido(obj.getContareceber_juroporcentagem()) ? obj.getContareceber_juroporcentagem() : conf.getPercentualJuroPadrao();
			if (conf.getCobrarJuroMultaSobreValorCheioConta()) {
				valorComJuro = obj.getContareceber_valor() * porcentagemJuro / 100 / 30;
			} else {
				valorComJuro = valorUtilizadoParaRealizarCalculoJuroOuMulta * porcentagemJuro / 100 / 30;
			}
			juro = Uteis.trunc(new BigDecimal(valorComJuro), 2).doubleValue();
			obj.setContareceber_juro(juro);
		}
	}
}
