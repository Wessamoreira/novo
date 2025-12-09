package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.faces.model.SelectItem;

import org.jfree.data.time.TimeSeriesCollection;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.financeiro.enumerador.FinalidadeDocEnum;
import negocio.comuns.financeiro.enumerador.FinalidadePixEnum;
import negocio.comuns.financeiro.enumerador.FinalidadeTedEnum;
import negocio.comuns.financeiro.enumerador.ModalidadeTransferenciaBancariaEnum;
import negocio.comuns.financeiro.enumerador.TipoContaEnum;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoChavePixEnum;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoContribuinte;
import negocio.comuns.financeiro.enumerador.TipoLancamentoContaPagarEnum;
import negocio.comuns.financeiro.enumerador.TipoServicoContaPagarEnum;
import negocio.comuns.sad.DespesaDWVO;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.comuns.utilitarias.dominios.OrigemContaPagar;
import negocio.comuns.utilitarias.dominios.SituacaoFinanceira;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 * Reponsável por manter os dados da entidade ContaPagar. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class ContaPagarVO extends SuperVO {

	private Integer codigo;
	private Date data;
	private String codOrigem;
	private String tipoOrigem;
	private List<CentroResultadoOrigemVO> listaCentroResultadoOrigemVOs;

	/**
	 * campo utilizado na conta a pagar somente quando a conta a pagar for gerada para restituir um aluno com relação a um valor de convênio que ele tenha pago, e a IE irá devolver este dinheiro para ele.
	 */
	private ConvenioVO convenio;

	private String situacao;
	private Date dataVencimento;
	private Date dataFatoGerador;
	private Double valor;
	private Double valorPago;
	private Double valorPagamento;
	private Double juro;
	private Double multa;
	private Double desconto;
	private String nrDocumento;
	private Long nossoNumero;
	private String codigoBarra;
	private String parcela;
	private String observacao;
	private Integer origemRenegociacaoPagar;
	private FormaPagamentoVO formaPagamentoVO;
	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>Fornecedor </code>.
	 */
	private FornecedorVO fornecedor;
	private FuncionarioVO funcionario;
	private BancoVO banco;
	private PessoaVO pessoa;
	private PessoaVO responsavelFinanceiro;
	private ParceiroVO parceiro;
	private String matricula;
	private String numeroNotaFiscalEntrada;
	private String codigoNotaFiscalEntrada;

	/**
	 * Dados para geracao do arquivo remessa pagar
	 */
	private BancoVO bancoRemessaPagar;
	private TipoServicoContaPagarEnum tipoServicoContaPagar;
	private TipoLancamentoContaPagarEnum tipoLancamentoContaPagar;
	private FinalidadeDocEnum finalidadeDocEnum;
	private FinalidadeTedEnum finalidadeTedEnum;
	private ModalidadeTransferenciaBancariaEnum modalidadeTransferenciaBancariaEnum;
	private TipoContaEnum tipoContaEnum;

	private BancoVO bancoRecebimento;
	private String numeroAgenciaRecebimento;
	private String digitoAgenciaRecebimento;
	private String contaCorrenteRecebimento;
	private String digitoCorrenteRecebimento;

	private String linhaDigitavel1;
	private String linhaDigitavel2;
	private String linhaDigitavel3;
	private String linhaDigitavel4;
	private String linhaDigitavel5;
	private String linhaDigitavel6;
	private String linhaDigitavel7;
	private String linhaDigitavel8;

	private String codigoReceitaTributo;
	private String identificacaoContribuinte;
	private TipoIdentificacaoContribuinte tipoIdentificacaoContribuinte;
	private String numeroReferencia;
	private Double valorReceitaBrutaAcumulada;
	private Double percentualReceitaBrutaAcumulada;

	/**
	 * Atributo responsável por manter o objeto relacionado da classe <code>CentroDespesa </code>.
	 */
	private ContaCorrenteVO contaCorrente;
	private UnidadeEnsinoVO unidadeEnsino;
	private String tipoSacado;
	private List<ContaPagarPagamentoVO> contaPagarPagamentoVOs;
	private List<LancamentoContabilVO> listaLancamentoContabeisDebito;
	private List<LancamentoContabilVO> listaLancamentoContabeisCredito;
	private boolean excluirContaPagar;
	private String descricao;
	private UsuarioVO responsavel;
	private GrupoContaPagarVO grupoContaPagar;
	private TimeSeriesCollection graficoContaPagarLinhaTempo;
	private String listaGraficoContaPagar;
	private String motivoCancelamento;
	private Date dataCancelamento;
	
	/*
	 * Atributo transiente apresentado na tela de consulta de conta a pagar
	 */
	private String pagamentos;
	/*
	 * Atributo transiente apresentado na tela de contrato despesa
	 */
	private Boolean excluir;
	private boolean lancamentoContabil = false;
	public static final long serialVersionUID = 1L;

	private OperadoraCartaoVO operadoraCartao;
	private boolean ajustarValorPorConciliacaoBancaria = false;
	private Double valorAlterar;	
	private boolean selecionado = false;
	private Date dataExecutarOperacaoTemp;//Campo usado na tela de Gestao Contas Pagar
	private Date dataNegociacaoPagamentoTemp;//Campo usado na tela de Gestao Contas Pagar
	private ArquivoVO arquivoVO;
	private FinalidadePixEnum finalidadePixEnum;  
	private String chaveEnderecamentoPix;
	private TipoIdentificacaoChavePixEnum tipoIdentificacaoChavePixEnum;

	public enum enumCampoConsultaContaPagar {
		CODIGO, FAVORECIDO, CATEGORIA_DESPESA, CODIGO_PAGAMENTO, DEPARTAMENTO, TURMA, NUMERO_NOTA_FISCAL_ENTRADA, NR_DOCUMENTO, CNPJ_FORNECEDOR, CPF_FAVORECIDO, CODIGO_NOTA_FISCAL_ENTRADA ,VALOR,FAIXA_VALOR, TIPO_ORIGEM, CODIGO_CONTRATO_DESPESA;
	}

	public ContaPagarVO() {
		super();
	}

	public ContaPagarVO getClone() {
		try {
			ContaPagarVO clone = (ContaPagarVO) super.clone();
			clone.setNovoObj(true);
			clone.setCodigo(0);
			clone.setData(getData());
			clone.setCodOrigem(getCodOrigem());
			clone.setTipoOrigem(getTipoOrigem());
			clone.setConvenio((ConvenioVO) getConvenio().clone());
			clone.setSituacao(getSituacao());
			clone.setDataVencimento(getDataVencimento());
			clone.setDataFatoGerador(getDataFatoGerador());
			clone.setValor(getValor());
			clone.setValorPago(getValorPago());
			clone.setValorPagamento(getValorPagamento());
			clone.setJuro(getJuro());
			clone.setMulta(getMulta());
			clone.setDesconto(getDesconto());
			clone.setNrDocumento(getNrDocumento());
			clone.setNossoNumero(getNossoNumero());
			clone.setCodigoBarra(getCodigoBarra());
			clone.setParcela(getParcela());
			clone.setObservacao(getObservacao());
			clone.setOrigemRenegociacaoPagar(getOrigemRenegociacaoPagar());
			clone.setFormaPagamentoVO((FormaPagamentoVO) getFormaPagamentoVO().clone());
			clone.setFornecedor((FornecedorVO) getFornecedor().clone());
			clone.setFuncionario((FuncionarioVO) getFuncionario().clone());
			clone.setBanco((BancoVO) getBanco().clone());
			clone.setPessoa((PessoaVO) getPessoa().clone());
			clone.setResponsavelFinanceiro((PessoaVO) getResponsavelFinanceiro().clone());
			clone.setParceiro((ParceiroVO) getParceiro().clone());
			clone.setMatricula(getMatricula());
			clone.setNumeroNotaFiscalEntrada(getNumeroNotaFiscalEntrada());
			clone.setCodigoNotaFiscalEntrada(getCodigoNotaFiscalEntrada());
			clone.setBancoRemessaPagar((BancoVO) getBancoRemessaPagar().clone());
			
			if (Uteis.isAtributoPreenchido(getTipoServicoContaPagar())) {
				clone.setTipoServicoContaPagar(getTipoServicoContaPagar());
			}
			if (Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar())) {
				clone.setTipoLancamentoContaPagar(getTipoLancamentoContaPagar());
			}
			if (Uteis.isAtributoPreenchido(getFinalidadeDocEnum())) {
				clone.setFinalidadeDocEnum(getFinalidadeDocEnum());
			}
			if (Uteis.isAtributoPreenchido(getFinalidadeTedEnum())) {
				clone.setFinalidadeTedEnum(getFinalidadeTedEnum());
			}
			if (Uteis.isAtributoPreenchido(getModalidadeTransferenciaBancariaEnum())) {
				clone.setModalidadeTransferenciaBancariaEnum(getModalidadeTransferenciaBancariaEnum());
			}
			if (Uteis.isAtributoPreenchido(getTipoContaEnum())) {
				clone.setTipoContaEnum(getTipoContaEnum());
			}
			clone.setBancoRecebimento((BancoVO) getBancoRecebimento().clone());
			clone.setNumeroAgenciaRecebimento(getNumeroAgenciaRecebimento());
			clone.setDigitoAgenciaRecebimento(getDigitoAgenciaRecebimento());
			clone.setContaCorrenteRecebimento(getContaCorrenteRecebimento());
			clone.setDigitoCorrenteRecebimento(getDigitoCorrenteRecebimento());

			clone.setLinhaDigitavel1(getLinhaDigitavel1());
			clone.setLinhaDigitavel2(getLinhaDigitavel2());
			clone.setLinhaDigitavel3(getLinhaDigitavel3());
			clone.setLinhaDigitavel4(getLinhaDigitavel4());
			clone.setLinhaDigitavel5(getLinhaDigitavel5());
			clone.setLinhaDigitavel6(getLinhaDigitavel6());
			clone.setLinhaDigitavel7(getLinhaDigitavel7());
			clone.setLinhaDigitavel8(getLinhaDigitavel8());

			if (Uteis.isAtributoPreenchido(getCodigoReceitaTributo())) {
				clone.setCodigoReceitaTributo(getCodigoReceitaTributo());
			}
			if (Uteis.isAtributoPreenchido(getIdentificacaoContribuinte())) {
				clone.setIdentificacaoContribuinte(getIdentificacaoContribuinte());
			}
			if (Uteis.isAtributoPreenchido(getTipoIdentificacaoContribuinte())) {
				clone.setTipoIdentificacaoContribuinte(getTipoIdentificacaoContribuinte());
			}
			if (Uteis.isAtributoPreenchido(getNumeroReferencia())) {
				clone.setNumeroReferencia(getNumeroReferencia());
			}
			if (Uteis.isAtributoPreenchido(getValorReceitaBrutaAcumulada())) {
				clone.setValorReceitaBrutaAcumulada(getValorReceitaBrutaAcumulada());
			}
			if (Uteis.isAtributoPreenchido(getPercentualReceitaBrutaAcumulada())) {
				clone.setPercentualReceitaBrutaAcumulada(getPercentualReceitaBrutaAcumulada());
			}
			clone.setContaCorrente((ContaCorrenteVO) getContaCorrente().clone());
			clone.setUnidadeEnsino((UnidadeEnsinoVO) getUnidadeEnsino().clone());
			clone.setTipoSacado(getTipoSacado());
			List<CentroResultadoOrigemVO>novaLista = new ArrayList<>(); 
			for (CentroResultadoOrigemVO croExistente : getListaCentroResultadoOrigemVOs()) {
				CentroResultadoOrigemVO croClone = croExistente.getClone();
				novaLista.add(croClone);
			}
			clone.getListaCentroResultadoOrigemVOs().clear();
			clone.getListaCentroResultadoOrigemVOs().addAll(novaLista);
			return clone;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	public static void validarDados(ContaPagarVO obj) {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getTipoSacado().equals("")) {
			throw new StreamSeiException("O campo TIPO SACADO (Conta à Pagar) deve ser informado.");
		}
		if (obj.getTipoSacado().equals("FO")) {
			if (obj.getFornecedor() == null || obj.getFornecedor().getCodigo().intValue() == 0) {
				throw new StreamSeiException("O campo FORNECEDOR (Conta à Pagar) deve ser informado.");
			}
		} else if (obj.getTipoSacado().equals("FU")) {
			if (obj.getFuncionario() == null || obj.getFuncionario().getCodigo().intValue() == 0) {
				throw new StreamSeiException("O campo FUNCIONARIO (Conta à Pagar) deve ser informado.");
			}
		} else if (obj.getTipoSacado().equals("AL")) {
			if (obj.getPessoa() == null || obj.getPessoa().getCodigo().intValue() == 0) {
				throw new StreamSeiException("O campo ALUNO (Conta à Pagar) deve ser informado.");
			}
		} else if (obj.getTipoSacado().equals("BA")) {
			if (obj.getBanco() == null || obj.getBanco().getCodigo().intValue() == 0) {
				throw new StreamSeiException("O campo BANCO (Conta à Pagar) deve ser informado.");
			}
		} else if (obj.getTipoSacado().equals("RF")) {
			if (obj.getResponsavelFinanceiro() == null || obj.getResponsavelFinanceiro().getCodigo().intValue() == 0) {
				throw new StreamSeiException("O campo RESPONSÁVEL FINANCEIRO (Conta à Pagar) deve ser informado.");
			}
		} else if (obj.getTipoSacado().equals("PA")) {
			if (obj.getParceiro() == null || obj.getParceiro().getCodigo().intValue() == 0) {
				throw new StreamSeiException("O campo PARCEIRO (Conta à Pagar) deve ser informado.");
			}
		} else if (obj.getTipoSacado().equals("OC") && !Uteis.isAtributoPreenchido(obj.getOperadoraCartao())) {
			throw new StreamSeiException("O campo OPERADORA CARTÃO (Conta à Pagar) deve ser informado.");
		}
		obj.limparDadosTipoSacadoDeAcordoComTipoSacado();

		if (obj.getUnidadeEnsino() == null || obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
			throw new StreamSeiException("O campo UNIDADE DE ENSINO (Conta à Pagar) deve ser informado!");
		}
		if (obj.getData() == null) {
			throw new StreamSeiException("O campo DATA (Conta à Pagar) deve ser informado.");
		}
		if (obj.getSituacao().equals("")) {
			throw new StreamSeiException("O campo SITUAÇÃO (Conta à Pagar) deve ser informado.");
		}
		if (obj.getDataVencimento() == null) {
			throw new StreamSeiException("O campo DATA DE VENCIMENTO (Conta à Pagar) deve ser informado.");
		}
		if (obj.getValor().doubleValue() == 0) {
			throw new StreamSeiException("O campo VALOR (Conta à Pagar) deve ser informado.");
		}
		Uteis.checkState(!obj.getTipoOrigemEnum().isAdiantamento() && obj.getListaCentroResultadoOrigemVOs().isEmpty(), "Deve ser informado pelo menos um Centro de Resultado Movimentação.");
		if (obj.getDesconto() >= obj.getValor()) {
			throw new StreamSeiException("O campo DESCONTO não pode ser maior que o campo VALOR.");
		}
		Uteis.checkState(!obj.getTipoOrigemEnum().isAdiantamento() && !obj.getQuitada() && !obj.getPrecoCentroResultadoTotal().equals(obj.getPrevisaoValorPagoSemDescontoPorUsoAdiantamento()), "O valor total do Centro de Resultado Movimetação da Conta Pagar não pode ser diferente do valor lançado.");
		Uteis.checkState(obj.isCanceladoFinanceiro(), "Não é possivel alterar uma Conta Pagar que esteja com a Situação Cancelada. Por favor caso queira realizar a alteração então Reative a Conta Pagar.");
		validarFormatoDaNumeroNotaFiscalEntrada(obj);
		validarFormatoDoCodigoNotaFiscalEntrada(obj);
		if (Uteis.isAtributoPreenchido(obj.getBancoRemessaPagar())) {
			if (!Uteis.isAtributoPreenchido(obj.getTipoServicoContaPagar())) {
				throw new StreamSeiException("O campo Tipo Serviço (Conta à Pagar) deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(obj.getTipoLancamentoContaPagar())) {
				throw new StreamSeiException("O campo Tipo Lançamento (Conta à Pagar) deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(obj.getFormaPagamentoVO())) {
				throw new StreamSeiException("O campo Forma Pagamento (Conta à Pagar) deve ser informado.");
			}
			if (obj.getNrDocumento().equals("")) {
				throw new StreamSeiException("O campo NR. DOCUMENTO (Conta à Pagar) deve ser informado.");
			}
		}
		if (Uteis.isAtributoPreenchido(obj.getTipoLancamentoContaPagar())) {
			if (obj.getTipoLancamentoContaPagar().isCreditoContaCorrente()
					|| obj.getTipoLancamentoContaPagar().isCreditoContaPoupanca()
					|| obj.getTipoLancamentoContaPagar().isTransferencia()
					|| obj.getTipoLancamentoContaPagar().isCaixaAutenticacao()
					|| obj.getTipoLancamentoContaPagar().isOrdemPagamento()) {
				validarDadosSegmentoA(obj);
				validarDadosSegmentoB(obj);
			} else if (obj.getTipoLancamentoContaPagar().isLiquidacaoTituloCarteiraCobrancaSantander()
					|| obj.getTipoLancamentoContaPagar().isLiquidacaoTituloOutroBanco()) {
				validarDadosSegmentoJ(obj);
			} else if (obj.getTipoLancamentoContaPagar().isGpsSemCodigoBarra()) {
				validarDadosSegmentoN1(obj);
			} else if (obj.getTipoLancamentoContaPagar().isDarfNormalSemCodigoBarra()) {
				validarDadosSegmentoN2(obj);
			} else if (obj.getTipoLancamentoContaPagar().isDarfSimplesSemCodigoBarra()) {
				validarDadosSegmentoN3(obj);
			} else if (obj.getTipoLancamentoContaPagar().isGareDrSemCodigoBarra()
					|| obj.getTipoLancamentoContaPagar().isGareIcmsSemCodigoBarra()
					|| obj.getTipoLancamentoContaPagar().isGareItcmdSemCodigoBarra()) {
				validarDadosSegmentoN4(obj);
			} else if (obj.getTipoLancamentoContaPagar().isPagamentoContasTributosComCodigoBarra()) {
				validarDadosSegmentoO(obj);
			}
		}

	}

	private static void validarFormatoDaNumeroNotaFiscalEntrada(ContaPagarVO contaPagar) {
		if (Uteis.isAtributoPreenchido(contaPagar.getNumeroNotaFiscalEntrada())) {
			String[] lista = contaPagar.getNumeroNotaFiscalEntrada().trim().split(",");
			if (lista.length > 0) {
				StringBuilder sb = new StringBuilder();
				for (String numero : lista) {
					converterNumeroNotaFiscalEntradaParaInteiro(numero);
					sb.append(sb.toString().isEmpty() ? numero.trim() : "," + numero.trim());
				}
				contaPagar.setNumeroNotaFiscalEntrada(sb.toString());
			} else {
				converterNumeroNotaFiscalEntradaParaInteiro(contaPagar.getNumeroNotaFiscalEntrada());
			}
		}
	}

	private static void converterNumeroNotaFiscalEntradaParaInteiro(String numeroNotaFiscalEntrada) {
		try {
			Integer.parseInt(numeroNotaFiscalEntrada.trim());
		} catch (NumberFormatException e) {
			throw new StreamSeiException("O campo Nº Nota Fiscal de Entrada não esta no formato correto.");
		}
	}

	public static void validarDadosSegmentoA(ContaPagarVO contaPagar) {
		if(Uteis.isAtributoPreenchido(contaPagar.getModalidadeTransferenciaBancariaEnum()) &&  contaPagar.getModalidadeTransferenciaBancariaEnum().isPix()) {
			if (contaPagar.getTipoLancamentoContaPagar().isPixTransferencia() && contaPagar.getModalidadeTransferenciaBancariaEnum().isPix() && !Uteis.isAtributoPreenchido(contaPagar.getFinalidadeTedEnum())) {
				throw new StreamSeiException("O campo Finalidade Pix Favorecido deve ser informado.");
			}
		}else {  		
			if (!Uteis.isAtributoPreenchido(contaPagar.getBancoRecebimento().getCodigo())) {
				throw new StreamSeiException("O campo Banco recebimento Favorecido deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(contaPagar.getNumeroAgenciaRecebimento())) {
				throw new StreamSeiException("O campo Agência recebimento Favorecido deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(contaPagar.getContaCorrenteRecebimento())) {
				throw new StreamSeiException("O campo Conta corrente recebimento Favorecido deve ser informado.");
			}
			if (!Uteis.isAtributoPreenchido(contaPagar.getFavorecido())) {
				throw new StreamSeiException("O campo Nome Favorecido deve ser informado.");
			}
			if (contaPagar.getTipoLancamentoContaPagar().isTransferencia() && contaPagar.getModalidadeTransferenciaBancariaEnum().isDoc() && !Uteis.isAtributoPreenchido(contaPagar.getFinalidadeDocEnum())) {
				throw new StreamSeiException("O campo Finalidade Doc Favorecido deve ser informado.");
			}
			if (contaPagar.getTipoLancamentoContaPagar().isTransferencia() && contaPagar.getModalidadeTransferenciaBancariaEnum().isTed() && !Uteis.isAtributoPreenchido(contaPagar.getFinalidadeTedEnum())) {
				throw new StreamSeiException("O campo Finalidade Ted Favorecido deve ser informado.");
			}	     	
	    }
	}

	public static void validarDadosSegmentoB(ContaPagarVO contaPagar) {
		if (!Uteis.isAtributoPreenchido(contaPagar.getTipoInscricaoFavorecido())) {
			throw new StreamSeiException("O campo Tipo Inscrição Favorecido deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(contaPagar.getCnpjOuCpfFavorecido())) {
			throw new StreamSeiException("O campo Inscrição Favorecido deve ser informado.");
		}
	}

	public static void validarDadosSegmentoJ(ContaPagarVO contaPagar) {
		if (!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel1())) {
			throw new StreamSeiException("O campo Linha Digitável 1 deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel2())) {
			throw new StreamSeiException("O campo Linha Digitável 2 deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel3())) {
			throw new StreamSeiException("O campo Linha Digitável 3 deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel4())) {
			throw new StreamSeiException("O campo Linha Digitável 4 deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel5())) {
			throw new StreamSeiException("O campo Linha Digitável 5 deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel6())) {
			throw new StreamSeiException("O campo Linha Digitável 6 deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel7())) {
			throw new StreamSeiException("O campo Linha Digitável 7 deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel8())) {
			throw new StreamSeiException("O campo Linha Digitável 8 deve ser informado.");
		}
	}

	public static void validarDadosSegmentoN1(ContaPagarVO contaPagar) {
		if (!Uteis.isAtributoPreenchido(contaPagar.getCodigoReceitaTributo())) {
			throw new StreamSeiException("O campo Código Receita Tributo deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(contaPagar.getTipoIdentificacaoContribuinte())) {
			throw new StreamSeiException("O campo Tipo Identificação Contribuinte deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(contaPagar.getIdentificacaoContribuinte())) {
			throw new StreamSeiException("O campo Identificação Contribuinte  deve ser informado.");
		}
	}

	public static void validarDadosSegmentoN2(ContaPagarVO contaPagar) {
		validarDadosSegmentoN1(contaPagar);
		if (!Uteis.isAtributoPreenchido(contaPagar.getNumeroReferencia())) {
			throw new StreamSeiException("O campo Número de Referencia  deve ser informado.");
		}
	}

	public static void validarDadosSegmentoN3(ContaPagarVO contaPagar) {
		validarDadosSegmentoN1(contaPagar);
		if (!Uteis.isAtributoPreenchido(contaPagar.getValorReceitaBrutaAcumulada())) {
			throw new StreamSeiException("O campo Valor da Receita Bruta Acumulada  deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(contaPagar.getPercentualReceitaBrutaAcumulada())) {
			throw new StreamSeiException("O campo Percentual da Receita Bruta Acumulada  deve ser informado.");
		}
	}

	public static void validarDadosSegmentoN4(ContaPagarVO contaPagar) {
		validarDadosSegmentoN1(contaPagar);
		if (!Uteis.isAtributoPreenchido(contaPagar.getFornecedor().getInscEstadual())) {
			throw new StreamSeiException("O campo Inscrição estadual deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(contaPagar.getFornecedor().getInscMunicipal())) {
			throw new StreamSeiException("O campo Inscrição Municipal deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(contaPagar.getNumeroReferencia())) {
			throw new StreamSeiException("O campo Número Declaração   deve ser informado.");
		}
	}

	public static void validarDadosSegmentoO(ContaPagarVO contaPagar) {
		if (!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel1())) {
			throw new StreamSeiException("O campo Linha Digitável 1 deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel2())) {
			throw new StreamSeiException("O campo Linha Digitável 2 deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel3())) {
			throw new StreamSeiException("O campo Linha Digitável 3 deve ser informado.");
		}
		if (!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel4())) {
			throw new StreamSeiException("O campo Linha Digitável 4 deve ser informado.");
		}
		contaPagar.setLinhaDigitavel5("");
		contaPagar.setLinhaDigitavel6("");
		contaPagar.setLinhaDigitavel7("");
		contaPagar.setLinhaDigitavel8("");
	}

	public boolean validarSeExisteCategoriaDespesaParaCentroResultadoOrigem(CategoriaDespesaVO cat) {
		return getListaCentroResultadoOrigemVOs().stream().anyMatch(p -> p.getCategoriaDespesaVO().getCodigo().equals(cat.getCodigo()));

	}

	public void limparDadosTipoSacadoDeAcordoComTipoSacado() {
		if (getTipoSacado().equals("FO")) {
			getBanco().setCodigo(0);
			getFuncionario().setCodigo(0);
			getPessoa().setCodigo(0);
			getResponsavelFinanceiro().setCodigo(0);
			getParceiro().setCodigo(0);
			getOperadoraCartao().setCodigo(0);
		} else if (getTipoSacado().equals("FU")) {
			getBanco().setCodigo(0);
			getFornecedor().setCodigo(0);
			getResponsavelFinanceiro().setCodigo(0);
			getParceiro().setCodigo(0);
			getOperadoraCartao().setCodigo(0);
		} else if (getTipoSacado().equals("BA")) {
			getFornecedor().setCodigo(0);
			getFuncionario().setCodigo(0);
			getPessoa().setCodigo(0);
			getOperadoraCartao().setCodigo(0);
			getResponsavelFinanceiro().setCodigo(0);
			getParceiro().setCodigo(0);
		} else if (getTipoSacado().equals("AL")) {
			getFornecedor().setCodigo(0);
			getFuncionario().setCodigo(0);
			getBanco().setCodigo(0);
			getResponsavelFinanceiro().setCodigo(0);
			getParceiro().setCodigo(0);
			getOperadoraCartao().setCodigo(0);
		} else if (getTipoSacado().equals("RF")) {
			getFornecedor().setCodigo(0);
			getFuncionario().setCodigo(0);
			getBanco().setCodigo(0);
			getParceiro().setCodigo(0);
		} else if (getTipoSacado().equals("PA")) {
			getFornecedor().setCodigo(0);
			getFuncionario().setCodigo(0);
			getBanco().setCodigo(0);
			getPessoa().setCodigo(0);
			getResponsavelFinanceiro().setCodigo(0);
			getOperadoraCartao().setCodigo(0);
		} else if (getTipoSacado().equals("OC")) {
			getFornecedor().setCodigo(0);
			getFuncionario().setCodigo(0);
			getBanco().setCodigo(0);
			getPessoa().setCodigo(0);
			getResponsavelFinanceiro().setCodigo(0);
			getParceiro().setCodigo(0);
		}

	}

	public Boolean isQuitada() {
		if ((this.getSituacao().equals(SituacaoFinanceira.PAGO.getValor())) || (this.getSituacao().equals(SituacaoFinanceira.NEGOCIADO.getValor()))) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getQuitada() {
		if ((this.getSituacao().equals(SituacaoFinanceira.PAGO.getValor()))
				|| (this.getSituacao().equals(SituacaoFinanceira.NEGOCIADO.getValor()))) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean isPago() {
		if (this.getSituacao().equals(SituacaoFinanceira.PAGO.getValor())) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isCanceladoFinanceiro() {
		return getSituacao().equals(SituacaoFinanceira.CANCELADO_FINANCEIRO.getValor());
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

	public DespesaDWVO getDespesaDWVO(Double valor, String origem) {
		DespesaDWVO obj = new DespesaDWVO(origem);
		obj.setAno(Uteis.getAnoData(new Date()));
		obj.setMes(Uteis.getMesDataAtual());
		obj.setData(new Date());
		obj.getFornecedor().setCodigo(getFornecedor().getCodigo());
		obj.getFuncionario().setCodigo(getFuncionario().getCodigo());
		obj.getBanco().setCodigo(getBanco().getCodigo());
		obj.getUnidadeEnsino().setCodigo(getUnidadeEnsino().getCodigo());
		obj.setTipoSacado(getTipoSacado());
		obj.setValor(valor);
		return obj;
	}
	
	

	public boolean isSelecionado() {
		return selecionado;
	}

	public void setSelecionado(boolean selecionado) {
		this.selecionado = selecionado;
	}

	public Date getDataExecutarOperacaoTemp() {
		return dataExecutarOperacaoTemp;
	}

	public void setDataExecutarOperacaoTemp(Date dataExecutarOperacaoTemp) {
		this.dataExecutarOperacaoTemp = dataExecutarOperacaoTemp;
	}

	public Date getDataNegociacaoPagamentoTemp() {		
		return dataNegociacaoPagamentoTemp;
	}

	public void setDataNegociacaoPagamentoTemp(Date dataNegociacaoPagamentoTemp) {
		this.dataNegociacaoPagamentoTemp = dataNegociacaoPagamentoTemp;
	}

	public boolean isTipoSacadoFuncionario() {
		return getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor());
	}

	public boolean isTipoSacadoAluno() {
		return getTipoSacado().equals(TipoSacado.ALUNO.getValor());
	}
	
	public boolean isTipoSacadoResponsavelFinanceiro() {
		return getTipoSacado().equals(TipoSacado.RESPONSAVEL_FINANCEIRO.getValor());
	}


	public boolean isTipoSacadoParceiro() {
		return getTipoSacado().equals(TipoSacado.PARCEIRO.getValor());
	}

	public boolean isTipoSacadoFornecedor() {
		return getTipoSacado().equals(TipoSacado.FORNECEDOR.getValor());
	}

	public boolean isTipoSacadoBanco() {
		return getTipoSacado().equals(TipoSacado.BANCO.getValor());
	}
	
	public boolean isTipoSacadoOperadoraCartao() {
		return getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor());
	}

	public String getCodOrigem_Apresentar() {
		if ((this.getCodOrigem().equals("")) || (this.getCodOrigem().equals("0"))) {
			return "";
		}
		return "(" + this.getCodOrigem() + ")";
	}

	public Boolean getEditarDados() {
		if ((getSituacao().equals("PA") || getSituacao().equals("NE"))) {
			return true;
		}
		return false;
	}

	public Boolean getPago() {
		if ((getSituacao().equals("PA"))) {
			return true;
		}
		return false;
	}

	public String getFavorecido_Apresentar() {
		if (getTipoSacadoEnum().isAluno()) {
			return "Aluno - " + getPessoa().getNome();
		} else if (getTipoSacadoEnum().isFuncionario()) {
			return "Funcionário - " + getFuncionario().getPessoa().getNome();
		} else if (getTipoSacadoEnum().isResponsavelFinanceiro()) {
			return "Responsável - " + getResponsavelFinanceiro().getNome();
		} else if (getTipoSacadoEnum().isBanco()) {
			return "Banco - " + getBanco().getNome();
		} else if (getTipoSacadoEnum().isOperadoraCartao()) {
			return "Operadora de Cartão - " + getOperadoraCartao().getNome();
		} else if (getTipoSacadoEnum().isFornecedor()) {
			return "Fornecedor - " + getFornecedor().getNome();
		} else if (getTipoSacadoEnum().isParceiro()) {
			return "Parceiro - " + getParceiro().getNome();
		}
		return "";
	}

	public String getFavorecido() {
		if (getTipoSacadoEnum().isAluno()) {
			return getPessoa().getNome();
		} else if (getTipoSacadoEnum().isFuncionario()) {
			return getFuncionario().getPessoa().getNome();
		} else if (getTipoSacadoEnum().isResponsavelFinanceiro()) {
			return getResponsavelFinanceiro().getNome();
		} else if (getTipoSacadoEnum().isBanco()) {
			return getBanco().getNome();
		} else if (getTipoSacadoEnum().isOperadoraCartao()) {
			return getOperadoraCartao().getNome();
		} else if (getTipoSacadoEnum().isFornecedor()) {
			return getFornecedor().getIsTemMei() ? getFornecedor().getNomePessoaFisica() : getFornecedor().getNome();
		} else if (getTipoSacadoEnum().isParceiro()) {
			return getParceiro().getNome();
		} else if (getTipoSacado().equals(TipoSacado.OPERADORA_CARTAO.getValor())) {
			return "Operadora Cartão - " + getOperadoraCartao().getNome();
		}
		return "";
	}

	public String getTipoInscricaoFavorecido() {
		if (getTipoSacadoEnum().isAluno() || getTipoSacadoEnum().isFuncionario() || getTipoSacadoEnum().isResponsavelFinanceiro()) {
			return "1";
		} else if (getTipoSacadoEnum().isBanco() || getTipoSacadoEnum().isOperadoraCartao()) {
			// Até o momento nao tenho informacao para esse campo
			return "";
		} else if (getTipoSacadoEnum().isFornecedor()) {
			return getFornecedor().getCNPJ().isEmpty() ? "1" : "2";
		} else if (getTipoSacadoEnum().isParceiro()) {
			return getParceiro().getCNPJ().isEmpty() ? "1" : "2";
		}
		return "";
	}

	public String getCnpjOuCpfFavorecido() {
		if (getTipoSacadoEnum().isAluno()) {
			return getPessoa().getCPF();
		} else if (getTipoSacadoEnum().isFuncionario()) {
			return getFuncionario().getPessoa().getCPF();
		} else if (getTipoSacadoEnum().isResponsavelFinanceiro()) {
			return getResponsavelFinanceiro().getCPF();
		} else if (getTipoSacadoEnum().isBanco() || getTipoSacadoEnum().isOperadoraCartao()) {
			// Até o momento nao tenho informacao para esse campo
			return "";
		} else if (getTipoSacadoEnum().isFornecedor()) {
			if (getFornecedor().getIsTemMei()) {
				return getFornecedor().getCpfFornecedor();
			} else {
				return getFornecedor().getCNPJ().isEmpty() ? getFornecedor().getCPF() : getFornecedor().getCNPJ();
			}
		} else if (getTipoSacadoEnum().isParceiro()) {
			return getParceiro().getCNPJ().isEmpty() ? getParceiro().getCPF() : getParceiro().getCNPJ();
		}
		return "";
	}

	public String getInscricaoEstadualFavorecido() {
		if (getTipoSacadoEnum().isAluno()) {
			return "";
		} else if (getTipoSacadoEnum().isFuncionario()) {
			return "";
		} else if (getTipoSacadoEnum().isResponsavelFinanceiro()) {
			return "";
		} else if (getTipoSacadoEnum().isBanco() || getTipoSacadoEnum().isOperadoraCartao()) {
			// Até o momento nao tenho informacao para esse campo
			return "";
		} else if (getTipoSacadoEnum().isFornecedor()) {
			return getFornecedor().getInscEstadual();
		} else if (getTipoSacadoEnum().isParceiro()) {
			return getParceiro().getInscEstadual();
		}
		return "";
	}

	public String getInscricaoMunicipalFavorecido() {
		if (getTipoSacadoEnum().isAluno()) {
			return "";
		} else if (getTipoSacadoEnum().isFuncionario()) {
			return "";
		} else if (getTipoSacadoEnum().isResponsavelFinanceiro()) {
			return "";
		} else if (getTipoSacadoEnum().isBanco() || getTipoSacadoEnum().isOperadoraCartao()) {
			// Até o momento nao tenho informacao para esse campo
			return "";
		} else if (getTipoSacadoEnum().isFornecedor()) {
			return getFornecedor().getInscMunicipal();
		} else if (getTipoSacadoEnum().isParceiro()) {
			return getParceiro().getInscMunicipal();
		}
		return "";
	}

	public String getLogadouroFavorecido() {
		if (getTipoSacadoEnum().isAluno()) {
			return getPessoa().getEndereco();
		} else if (getTipoSacadoEnum().isFuncionario()) {
			return getFuncionario().getPessoa().getEndereco();
		} else if (getTipoSacadoEnum().isResponsavelFinanceiro()) {
			return getResponsavelFinanceiro().getEndereco();
		} else if (getTipoSacadoEnum().isBanco() || getTipoSacadoEnum().isOperadoraCartao()) {
			// Até o momento nao tenho informacao para esse campo
			return "";
		} else if (getTipoSacadoEnum().isFornecedor()) {
			return getFornecedor().getEndereco();
		} else if (getTipoSacadoEnum().isParceiro()) {
			return getParceiro().getEndereco();
		}
		return "";
	}

	public String getComplementoFavorecido() {
		if (getTipoSacadoEnum().isAluno()) {
			return getPessoa().getComplemento();
		} else if (getTipoSacadoEnum().isFuncionario()) {
			return getFuncionario().getPessoa().getComplemento();
		} else if (getTipoSacadoEnum().isResponsavelFinanceiro()) {
			return getResponsavelFinanceiro().getComplemento();
		} else if (getTipoSacadoEnum().isBanco() || getTipoSacadoEnum().isOperadoraCartao()) {
			// Até o momento nao tenho informacao para esse campo
			return "";
		} else if (getTipoSacadoEnum().isFornecedor()) {
			return getFornecedor().getComplemento();
		} else if (getTipoSacadoEnum().isParceiro()) {
			return getParceiro().getComplemento();
		}
		return "";
	}

	public String getNumeroEnderecoFavorecido() {
		if (getTipoSacadoEnum().isAluno()) {
			return getPessoa().getNumero();
		} else if (getTipoSacadoEnum().isFuncionario()) {
			return getFuncionario().getPessoa().getNumero();
		} else if (getTipoSacadoEnum().isResponsavelFinanceiro()) {
			return getResponsavelFinanceiro().getNumero();
		} else if (getTipoSacadoEnum().isBanco() || getTipoSacadoEnum().isOperadoraCartao()) {
			// Até o momento nao tenho informacao para esse campo
			return "";
		} else if (getTipoSacadoEnum().isFornecedor()) {
			return getFornecedor().getNumero();
		} else if (getTipoSacadoEnum().isParceiro()) {
			return getParceiro().getNumero();
		}
		return "";
	}

	public String getBairroFavorecido() {
		if (getTipoSacadoEnum().isAluno()) {
			return getPessoa().getSetor();
		} else if (getTipoSacadoEnum().isFuncionario()) {
			return getFuncionario().getPessoa().getSetor();
		} else if (getTipoSacadoEnum().isResponsavelFinanceiro()) {
			return getResponsavelFinanceiro().getSetor();
		} else if (getTipoSacadoEnum().isBanco() || getTipoSacadoEnum().isOperadoraCartao()) {
			// Até o momento nao tenho informacao para esse campo
			return "";
		} else if (getTipoSacadoEnum().isFornecedor()) {
			return getFornecedor().getSetor();
		} else if (getTipoSacadoEnum().isParceiro()) {
			return getParceiro().getSetor();
		}
		return "";
	}

	public String getCidadeFavorecido() {
		if (getTipoSacadoEnum().isAluno()) {
			return getPessoa().getCidade().getNome();
		} else if (getTipoSacadoEnum().isFuncionario()) {
			return getFuncionario().getPessoa().getCidade().getNome();
		} else if (getTipoSacadoEnum().isResponsavelFinanceiro()) {
			return getResponsavelFinanceiro().getCidade().getNome();
		} else if (getTipoSacadoEnum().isBanco() || getTipoSacadoEnum().isOperadoraCartao()) {
			// Até o momento nao tenho informacao para esse campo
			return "";
		} else if (getTipoSacadoEnum().isFornecedor()) {
			return getFornecedor().getCidade().getNome();
		} else if (getTipoSacadoEnum().isParceiro()) {
			return getParceiro().getCidade().getNome();
		}
		return "";
	}

	public String getCepFavorecido() {
		if (getTipoSacadoEnum().isAluno()) {
			return getPessoa().getCEP();
		} else if (getTipoSacadoEnum().isFuncionario()) {
			return getFuncionario().getPessoa().getCEP();
		} else if (getTipoSacadoEnum().isResponsavelFinanceiro()) {
			return getResponsavelFinanceiro().getCEP();
		} else if (getTipoSacadoEnum().isBanco() || getTipoSacadoEnum().isOperadoraCartao()) {
			// Até o momento nao tenho informacao para esse campo
			return "";
		} else if (getTipoSacadoEnum().isFornecedor()) {
			return getFornecedor().getCEP();
		} else if (getTipoSacadoEnum().isParceiro()) {
			return getParceiro().getCEP();
		}
		return "";
	}

	public String getEstadoFavorecido() {
		if (getTipoSacadoEnum().isAluno()) {
			return getPessoa().getCidade().getEstado().getSigla();
		} else if (getTipoSacadoEnum().isFuncionario()) {
			return getFuncionario().getPessoa().getCidade().getEstado().getSigla();
		} else if (getTipoSacadoEnum().isResponsavelFinanceiro()) {
			return getResponsavelFinanceiro().getCidade().getEstado().getSigla();
		} else if (getTipoSacadoEnum().isBanco() || getTipoSacadoEnum().isOperadoraCartao()) {
			// Até o momento nao tenho informacao para esse campo
			return "";
		} else if (getTipoSacadoEnum().isFornecedor()) {
			return getFornecedor().getCidade().getEstado().getSigla();
		} else if (getTipoSacadoEnum().isParceiro()) {
			return getParceiro().getCidade().getEstado().getSigla();
		}
		return "";
	}

	/**
	 * Retorna o objeto da classe <code>Fornecedor</code> relacionado com ( <code>ContaPagar</code>).
	 */
	public FornecedorVO getFornecedor() {
		if (fornecedor == null) {
			fornecedor = new FornecedorVO();
		}
		return (fornecedor);
	}

	/**
	 * Define o objeto da classe <code>Fornecedor</code> relacionado com ( <code>ContaPagar</code>).
	 */
	public void setFornecedor(FornecedorVO obj) {
		this.fornecedor = obj;
	}

	public Integer getOrigemRenegociacaoPagar() {
		if (origemRenegociacaoPagar == null) {
			origemRenegociacaoPagar = 0;
		}
		return (origemRenegociacaoPagar);
	}

	public void setOrigemRenegociacaoPagar(Integer origemRenegociacaoPagar) {
		this.origemRenegociacaoPagar = origemRenegociacaoPagar;
	}

	public String getParcela() {
		if (parcela == null) {
			parcela = "1/1";
		}
		return (parcela);
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public String getNrDocumento() {
		if (nrDocumento == null) {
			nrDocumento = "";
		}
		return (nrDocumento);
	}

	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public Long getNossoNumero() {
		if (nossoNumero == null) {
			nossoNumero = 0L;
		}
		return nossoNumero;
	}

	public void setNossoNumero(Long nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public Double getMulta() {
		if (multa == null) {
			multa = 0.0;
		}
		return (multa);
	}

	public void setMulta(Double multa) {
		this.multa = multa;
	}

	public boolean isExisteJuro() {
		return getJuro() > 0.0;
	}

	public Double getJuro() {
		if (juro == null) {
			juro = 0.0;
		}
		return (juro);
	}

	public void setJuro(Double juro) {
		this.juro = juro;
	}

	public Double getValor() {
		if (valor == null) {
			valor = 0.0;
		}
		return (valor);
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Date getDataVencimento() {
		if (dataVencimento == null) {
			dataVencimento = new Date();
		}
		return (dataVencimento);
	}

	public Long getDataVencimento_Time() {
		return UteisData.getDataSemHora(getDataVencimento()).getTime();
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
	 */
	public String getDataVencimento_Apresentar() {
		return (Uteis.getData(dataVencimento));
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = SituacaoFinanceira.A_PAGAR.getValor();
		}
		return (situacao);
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

	/**
	 * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico. Com base no valor de armazenamento do atributo esta função é capaz de retornar o de apresentação correspondente. Útil para campos como sexo, escolaridade, etc.
	 */
	public String getSituacao_Apresentar() {
		return SituacaoFinanceira.getDescricao(situacao);
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getTipoOrigem() {
		if (tipoOrigem == null) {
			tipoOrigem = OrigemContaPagar.REGISTRO_MANUAL.getValor();
		}
		return (tipoOrigem);
	}

	public OrigemContaPagar getTipoOrigemEnum() {
		if (Uteis.isAtributoPreenchido(getTipoOrigem())) {
			return OrigemContaPagar.getEnum(getTipoOrigem());
		}
		return OrigemContaPagar.REGISTRO_MANUAL;
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

	public void setCodOrigem(String codOrigem) {
		this.codOrigem = codOrigem;
	}

	public Date getData() {
		if (data == null) {
			data = new Date();
		}
		return (data);
	}

	public ContaCorrenteVO getContaCorrente() {
		if (contaCorrente == null) {
			contaCorrente = new ContaCorrenteVO();
		}
		return contaCorrente;
	}

	public void setContaCorrente(ContaCorrenteVO contaCorrente) {
		this.contaCorrente = contaCorrente;
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
	 */
	public String getData_Apresentar() {
		return (Uteis.getData(data));
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Double getPrevisaoValorPago() {
		return Uteis.arrendondarForcando2CadasDecimais(getValor() + getJuro() + getMulta() - getDesconto() - getDescontoPorUsoAdiantamento());
	}
	
	public Double getPrevisaoValorPagoSemDescontoPorUsoAdiantamento() {
		return Uteis.arrendondarForcando2CadasDecimais(getValor() + getJuro() + getMulta() - getDesconto());
	}

	public Double getValorPago() {
		if (valorPago == null) {
			valorPago = 0.0;
		}
		return valorPago;
	}

	public void setValorPago(Double valorPago) {
		this.valorPago = valorPago;
	}

	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	public Boolean getValidarSePossuiOrigem() {
		return getOrigemRenegociacaoPagar() != 0;
	}

	public boolean isExisteDesconto() {
		return getDesconto() > 0.0;
	}

	public Double getDesconto() {
		if (desconto == null) {
			desconto = 0.0;
		}
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public List<ContaPagarPagamentoVO> getContaPagarPagamentoVOs() {
		if (contaPagarPagamentoVOs == null) {
			contaPagarPagamentoVOs = new ArrayList<ContaPagarPagamentoVO>(0);
		}
		return contaPagarPagamentoVOs;
	}

	public void setContaPagarPagamentoVOs(List<ContaPagarPagamentoVO> contaPagarPagamentoVOs) {
		this.contaPagarPagamentoVOs = contaPagarPagamentoVOs;
	}

	public Double getValorPagamento() {
		if (valorPagamento == null) {
			valorPagamento = 0.0;
		}
		return valorPagamento;
	}

	public void setValorPagamento(Double valorPagamento) {
		this.valorPagamento = valorPagamento;
	}

	public FuncionarioVO getFuncionario() {
		if (funcionario == null) {
			funcionario = new FuncionarioVO();
		}
		return funcionario;
	}

	public void setFuncionario(FuncionarioVO funcionario) {
		this.funcionario = funcionario;
	}

	public PessoaVO getPessoa() {
		if (pessoa == null) {
			pessoa = new PessoaVO();
		}
		return pessoa;
	}

	public void setPessoa(PessoaVO pessoa) {
		this.pessoa = pessoa;
	}

	public TipoSacado getTipoSacadoEnum() {
		return TipoSacado.getEnum(getTipoSacado());
	}

	public String getTipoSacado() {
		if (tipoSacado == null) {
			tipoSacado = TipoSacado.FORNECEDOR.getValor();
		}
		return tipoSacado;
	}

	public void setTipoSacado(String tipoSacado) {
		this.tipoSacado = tipoSacado;
	}

	public Integer getCodigoPessoaSacado() {
		TipoSacado tipoPessoaLocal = TipoSacado.getEnum(getTipoSacado());
		if (tipoPessoaLocal != null) {
			switch (tipoPessoaLocal) {
			case ALUNO:
				return getPessoa().getCodigo();
			case RESPONSAVEL_FINANCEIRO:
				return getResponsavelFinanceiro().getCodigo();
			case FUNCIONARIO_PROFESSOR:
				return getFuncionario().getCodigo();
			case PARCEIRO:
				return getParceiro().getCodigo();
			case FORNECEDOR:
				return getFornecedor().getCodigo();
			case BANCO:
				return getBanco().getCodigo();
			case OPERADORA_CARTAO:
				return getOperadoraCartao().getCodigo();
			default:
				return 0; 
			}
		}
		return 0;
	}

	/**
	 * @return the banco
	 */
	public BancoVO getBanco() {
		if (banco == null) {
			banco = new BancoVO();
		}
		return banco;
	}

	/**
	 * @param banco
	 *            the banco to set
	 */
	public void setBanco(BancoVO banco) {
		this.banco = banco;
	}

	public String getTipoOrigem_Apresentar() {
		if (Uteis.isAtributoPreenchido(tipoOrigem)) {
			return OrigemContaPagar.getDescricao(tipoOrigem);
		}
		return OrigemContaPagar.REGISTRO_MANUAL.getValor();
	}

	public Boolean getPermitirInformarAlterarCentroCusto() {
		if (this.getTipoOrigem().equals(OrigemContaPagar.COMPRA.getValor())) {
			// Contas a pagar advindas de uma compra são creditadas
			// automaticamente
			// no centro de custo da unidade (e somente em nível de unidade)
			// isto por que, quando se compra um material, o mesmo só irá para
			// o centro de custo de um departamento / funcionário / turma
			// quando se for realizar um entrega de requisição.
			// Para um compra lançada diretamente no cadastro de compras ou
			// advindas
			// de outras origens, aí sim, poderá existir a
			// necessidade/possibilidade de
			// se alterar o centro de custo da mesma.
			return false;
		}
		return true;
	}

	public Double getValorPrevisaoPagamento() {
		return Uteis.arrendondarForcando2CadasDecimais(this.getValor() + this.getJuro() + this.getMulta() - this.getDesconto() - this.getDescontoPorUsoAdiantamento());
	}
	
	public Double getValorPrevisaoPagamentoNotaFiscal() {
		return Uteis.arrendondarForcando2CadasDecimais(this.getValor() -  this.getDesconto() - this.getDescontoPorUsoAdiantamento());
	}

	/**
	 * @return the excluirContaPagar
	 */
	public boolean isExcluirContaPagar() {
		return excluirContaPagar;
	}

	/**
	 * @param excluirContaPagar
	 *            the excluirContaPagar to set
	 */
	public void setExcluirContaPagar(boolean excluirContaPagar) {
		this.excluirContaPagar = excluirContaPagar;
	}

	/**
	 * @return the descricao
	 */
	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	/**
	 * @param descricao
	 *            the descricao to set
	 */
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return responsavel;
	}

	public void setResponsavel(UsuarioVO responsavel) {
		this.responsavel = responsavel;
	}

	public Date getDataFatoGerador() {
		if (dataFatoGerador == null) {
			dataFatoGerador = new Date();
		}
		return dataFatoGerador;
	}

	public void setDataFatoGerador(Date dataFatoGerador) {
		this.dataFatoGerador = dataFatoGerador;
	}

	public String getDataFatoGerador_Apresentar() {
		return (Uteis.getData(dataFatoGerador));
	}

	public GrupoContaPagarVO getGrupoContaPagar() {
		if (grupoContaPagar == null) {
			grupoContaPagar = new GrupoContaPagarVO();
		}
		return grupoContaPagar;
	}

	public void setGrupoContaPagar(GrupoContaPagarVO grupoContaPagar) {
		this.grupoContaPagar = grupoContaPagar;
	}

	public TimeSeriesCollection getGraficoContaPagarLinhaTempo() {
		return graficoContaPagarLinhaTempo;
	}

	public void setGraficoContaPagarLinhaTempo(TimeSeriesCollection graficoContaPagarLinhaTempo) {
		this.graficoContaPagarLinhaTempo = graficoContaPagarLinhaTempo;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public ParceiroVO getParceiro() {
		if (parceiro == null) {
			parceiro = new ParceiroVO();
		}
		return parceiro;
	}

	public void setParceiro(ParceiroVO parceiro) {
		this.parceiro = parceiro;
	}

	public PessoaVO getResponsavelFinanceiro() {
		if (responsavelFinanceiro == null) {
			responsavelFinanceiro = new PessoaVO();
		}
		return responsavelFinanceiro;
	}

	public void setResponsavelFinanceiro(PessoaVO responsavelFinanceiro) {
		this.responsavelFinanceiro = responsavelFinanceiro;
	}

	public boolean getTipoResponsavelFinanceiro() {
		return getTipoSacado().equals("RF");
	}

	public boolean getTipoAluno() {
		return getTipoSacado().equals("AL");
	}

	public boolean getTipoOperadoraCartao() {
		return getTipoSacado().equals("OC");
	}

	public String getPagamentos() {
		if (pagamentos == null) {
			pagamentos = "";
		}
		return pagamentos;
	}

	public void setPagamentos(String pagamentos) {
		this.pagamentos = pagamentos;
	}

	public String getListaGraficoContaPagar() {
		if (listaGraficoContaPagar == null) {
			listaGraficoContaPagar = "";
		}
		return listaGraficoContaPagar;
	}

	public void setListaGraficoContaPagar(String listaGraficoContaPagar) {
		this.listaGraficoContaPagar = listaGraficoContaPagar;
	}

	public Boolean getExcluir() {
		if (excluir == null) {
			excluir = false;
		}
		return excluir;
	}

	public void setExcluir(Boolean excluir) {
		this.excluir = excluir;
	}

	public OperadoraCartaoVO getOperadoraCartao() {
		if (operadoraCartao == null) {
			operadoraCartao = new OperadoraCartaoVO();
		}
		return operadoraCartao;
	}

	public void setOperadoraCartao(OperadoraCartaoVO operadoraCartao) {
		this.operadoraCartao = operadoraCartao;
	}

	/**
	 * @return the convenio
	 */
	public ConvenioVO getConvenio() {
		if (convenio == null) {
			convenio = new ConvenioVO();
		}
		return convenio;
	}

	/**
	 * @param convenio
	 *            the convenio to set
	 */
	public void setConvenio(ConvenioVO convenio) {
		this.convenio = convenio;
	}

	public Boolean getContaRestituicaoConvenio() {
		if (!this.getConvenio().getCodigo().equals(0)) {
			return true;
		}
		return false;
	}

	public Double getValorAlterar() {
		if (valorAlterar == null) {
			valorAlterar = getValor();
		}
		return valorAlterar;
	}

	public void setValorAlterar(Double valorAlterar) {
		this.valorAlterar = valorAlterar;
	}

	public boolean isAjustarValorPorConciliacaoBancaria() {
		return ajustarValorPorConciliacaoBancaria;
	}

	public void setAjustarValorPorConciliacaoBancaria(boolean ajustarValorPorConciliacaoBancaria) {
		this.ajustarValorPorConciliacaoBancaria = ajustarValorPorConciliacaoBancaria;
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
		return getListaLancamentoContabeisCredito().stream().mapToDouble(LancamentoContabilVO::getValor).sum();
	}

	public Double getTotalLancamentoContabeisDebito() {
		return getListaLancamentoContabeisDebito().stream().mapToDouble(LancamentoContabilVO::getValor).sum();
	}

	public Double getTotalLancamentoContabeisCreditoTipoValorContaPagar() {
		return getListaLancamentoContabeisDebito().stream().filter(p -> p.getTipoValorLancamentoContabilEnum().isContaPagar()).mapToDouble(LancamentoContabilVO::getValor).sum();
	}

	public Double getTotalLancamentoContabeisDebitoTipoValorContaPagar() {
		return getListaLancamentoContabeisDebito().stream().filter(p -> p.getTipoValorLancamentoContabilEnum().isContaPagar()).mapToDouble(LancamentoContabilVO::getValor).sum();
	}

	public String getNumeroNotaFiscalEntrada() {
		numeroNotaFiscalEntrada = Optional.ofNullable(numeroNotaFiscalEntrada).orElse("");
		return numeroNotaFiscalEntrada;
	}

	public void setNumeroNotaFiscalEntrada(String numeroNotaFiscalEntrada) {
		this.numeroNotaFiscalEntrada = numeroNotaFiscalEntrada;
	}

	public BancoVO getBancoRemessaPagar() {
		if (bancoRemessaPagar == null) {
			bancoRemessaPagar = new BancoVO();
		}
		return bancoRemessaPagar;
	}

	public void setBancoRemessaPagar(BancoVO bancoRemessaPagar) {
		this.bancoRemessaPagar = bancoRemessaPagar;
	}

	public TipoServicoContaPagarEnum getTipoServicoContaPagar() {
		return tipoServicoContaPagar;
	}

	public void setTipoServicoContaPagar(TipoServicoContaPagarEnum tipoServicoContaPagar) {
		this.tipoServicoContaPagar = tipoServicoContaPagar;
	}

	public TipoLancamentoContaPagarEnum getTipoLancamentoContaPagar() {
		return tipoLancamentoContaPagar;
	}

	public void setTipoLancamentoContaPagar(TipoLancamentoContaPagarEnum tipoLancamentoContaPagar) {
		this.tipoLancamentoContaPagar = tipoLancamentoContaPagar;
	}

	public FinalidadeDocEnum getFinalidadeDocEnum() {
		return finalidadeDocEnum;
	}

	public void setFinalidadeDocEnum(FinalidadeDocEnum finalidadeDocEnum) {
		this.finalidadeDocEnum = finalidadeDocEnum;
	}

	public FinalidadeTedEnum getFinalidadeTedEnum() {
		return finalidadeTedEnum;
	}

	public void setFinalidadeTedEnum(FinalidadeTedEnum finalidadeTedEnum) {
		this.finalidadeTedEnum = finalidadeTedEnum;
	}

	public String getNumeroAgenciaRecebimento() {
		if (numeroAgenciaRecebimento == null) {
			numeroAgenciaRecebimento = "";
		}
		return numeroAgenciaRecebimento;
	}

	public void setNumeroAgenciaRecebimento(String numeroAgenciaRecebimento) {
		this.numeroAgenciaRecebimento = numeroAgenciaRecebimento;
	}

	public String getDigitoAgenciaRecebimento() {
		if (digitoAgenciaRecebimento == null) {
			digitoAgenciaRecebimento = "";
		}
		return digitoAgenciaRecebimento;
	}

	public void setDigitoAgenciaRecebimento(String digitoAgenciaRecebimento) {
		this.digitoAgenciaRecebimento = digitoAgenciaRecebimento;
	}

	public String getContaCorrenteRecebimento() {
		if (contaCorrenteRecebimento == null) {
			contaCorrenteRecebimento = "";
		}
		return contaCorrenteRecebimento;
	}

	public void setContaCorrenteRecebimento(String contaCorrenteRecebimento) {
		this.contaCorrenteRecebimento = contaCorrenteRecebimento;
	}

	public String getDigitoCorrenteRecebimento() {
		if (digitoCorrenteRecebimento == null) {
			digitoCorrenteRecebimento = "";
		}
		return digitoCorrenteRecebimento;
	}

	public void setDigitoCorrenteRecebimento(String digitoCorrenteRecebimento) {
		this.digitoCorrenteRecebimento = digitoCorrenteRecebimento;
	}

	public ModalidadeTransferenciaBancariaEnum getModalidadeTransferenciaBancariaEnum() {
		return modalidadeTransferenciaBancariaEnum;
	}

	public void setModalidadeTransferenciaBancariaEnum(ModalidadeTransferenciaBancariaEnum modalidadeTransferenciaBancariaEnum) {
		this.modalidadeTransferenciaBancariaEnum = modalidadeTransferenciaBancariaEnum;
	}

	public TipoContaEnum getTipoContaEnum() {
		return tipoContaEnum;
	}

	public void setTipoContaEnum(TipoContaEnum tipoContaEnum) {
		this.tipoContaEnum = tipoContaEnum;
	}

	public String getCodigoReceitaTributo() {
		return codigoReceitaTributo;
	}

	public void setCodigoReceitaTributo(String codigoReceitaTributo) {
		this.codigoReceitaTributo = codigoReceitaTributo;
	}

	public TipoIdentificacaoContribuinte getTipoIdentificacaoContribuinte() {
		return tipoIdentificacaoContribuinte;
	}

	public void setTipoIdentificacaoContribuinte(TipoIdentificacaoContribuinte tipoIdentificacaoContribuinte) {
		this.tipoIdentificacaoContribuinte = tipoIdentificacaoContribuinte;
	}

	public String getIdentificacaoContribuinte() {
		return identificacaoContribuinte;
	}

	public void setIdentificacaoContribuinte(String identificacaoContribuinte) {
		this.identificacaoContribuinte = identificacaoContribuinte;
	}

	public String getNumeroReferencia() {
		return numeroReferencia;
	}

	public void setNumeroReferencia(String numeroReferencia) {
		this.numeroReferencia = numeroReferencia;
	}

	public Double getValorReceitaBrutaAcumulada() {
		return valorReceitaBrutaAcumulada;
	}

	public void setValorReceitaBrutaAcumulada(Double valorReceitaBrutaAcumulada) {
		this.valorReceitaBrutaAcumulada = valorReceitaBrutaAcumulada;
	}

	public Double getPercentualReceitaBrutaAcumulada() {
		return percentualReceitaBrutaAcumulada;
	}

	public void setPercentualReceitaBrutaAcumulada(Double percentualReceitaBrutaAcumulada) {
		this.percentualReceitaBrutaAcumulada = percentualReceitaBrutaAcumulada;
	}

	public BancoVO getBancoRecebimento() {
		if (bancoRecebimento == null) {
			bancoRecebimento = new BancoVO();
		}
		return bancoRecebimento;
	}

	public void setBancoRecebimento(BancoVO bancoRecebimento) {
		this.bancoRecebimento = bancoRecebimento;
	}

	public String getLinhaDigitavel1() {
		if (linhaDigitavel1 == null) {
			linhaDigitavel1 = "";
		}
		return linhaDigitavel1;
	}

	public void setLinhaDigitavel1(String linhaDigitavel1) {
		this.linhaDigitavel1 = linhaDigitavel1;
	}

	public String getLinhaDigitavel2() {
		if (linhaDigitavel2 == null) {
			linhaDigitavel2 = "";
		}
		return linhaDigitavel2;
	}

	public void setLinhaDigitavel2(String linhaDigitavel2) {
		this.linhaDigitavel2 = linhaDigitavel2;
	}

	public String getLinhaDigitavel3() {
		if (linhaDigitavel3 == null) {
			linhaDigitavel3 = "";
		}
		return linhaDigitavel3;
	}

	public void setLinhaDigitavel3(String linhaDigitavel3) {
		this.linhaDigitavel3 = linhaDigitavel3;
	}

	public String getLinhaDigitavel4() {
		if (linhaDigitavel4 == null) {
			linhaDigitavel4 = "";
		}
		return linhaDigitavel4;
	}

	public void setLinhaDigitavel4(String linhaDigitavel4) {
		this.linhaDigitavel4 = linhaDigitavel4;
	}

	public String getLinhaDigitavel5() {
		if (linhaDigitavel5 == null) {
			linhaDigitavel5 = "";
		}
		return linhaDigitavel5;
	}

	public void setLinhaDigitavel5(String linhaDigitavel5) {
		this.linhaDigitavel5 = linhaDigitavel5;
	}

	public String getLinhaDigitavel6() {
		if (linhaDigitavel6 == null) {
			linhaDigitavel6 = "";
		}
		return linhaDigitavel6;
	}

	public void setLinhaDigitavel6(String linhaDigitavel6) {
		this.linhaDigitavel6 = linhaDigitavel6;
	}

	public String getLinhaDigitavel7() {
		if (linhaDigitavel7 == null) {
			linhaDigitavel7 = "";
		}
		return linhaDigitavel7;
	}

	public void setLinhaDigitavel7(String linhaDigitavel7) {
		this.linhaDigitavel7 = linhaDigitavel7;
	}

	public String getLinhaDigitavel8() {
		if (linhaDigitavel8 == null) {
			linhaDigitavel8 = "";
		}
		return linhaDigitavel8;
	}

	public void setLinhaDigitavel8(String linhaDigitavel8) {
		this.linhaDigitavel8 = linhaDigitavel8;
	}

	public String getObservacao() {
		if (observacao == null) {
			observacao = "";
		}
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public FormaPagamentoVO getFormaPagamentoVO() {
		if (formaPagamentoVO == null) {
			formaPagamentoVO = new FormaPagamentoVO();
		}
		return formaPagamentoVO;
	}

	public void setFormaPagamentoVO(FormaPagamentoVO formaPagamentoVO) {
		this.formaPagamentoVO = formaPagamentoVO;
	}

	public List<CentroResultadoOrigemVO> getListaCentroResultadoOrigemVOs() {
		listaCentroResultadoOrigemVOs = Optional.ofNullable(listaCentroResultadoOrigemVOs).orElse(new ArrayList<>());
		return listaCentroResultadoOrigemVOs;
	}

	public void setListaCentroResultadoOrigemVOs(List<CentroResultadoOrigemVO> listaCentroResultadoOrigemVOs) {
		this.listaCentroResultadoOrigemVOs = listaCentroResultadoOrigemVOs;
	}

	public Double getQuantidadeCentroResultadoTotal() {
		return getListaCentroResultadoOrigemVOs().stream().mapToDouble(CentroResultadoOrigemVO::getQuantidade).sum();
	}

	public Double getPorcentagemCentroResultadoTotal() {
		return Uteis.arrendondarForcandoCadasDecimais(getListaCentroResultadoOrigemVOs().stream().map(p -> p.getPorcentagem()).reduce(0D, (a, b) -> Uteis.arrendondarForcandoCadasDecimais(a + b, 8)), 8);
	}

	public Double getPrecoCentroResultadoTotal() {
		return getListaCentroResultadoOrigemVOs().stream().map(p -> p.getValor()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}

	public boolean isApresentarDadosContaBancaria() {
		return (getFormaPagamentoVO().getTipoFormaPagamentoEnum() != null 	&& (getFormaPagamentoVO().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.DEPOSITO)
						|| getFormaPagamentoVO().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.DEBITO_EM_CONTA_CORRENTE)) 	&& !Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()))
				        || (Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()) 	&& (getTipoLancamentoContaPagar().isCreditoContaCorrente()
								|| getTipoLancamentoContaPagar().isCreditoContaPoupanca()
								|| getTipoLancamentoContaPagar().isTransferencia()
								|| getTipoLancamentoContaPagar().isCaixaAutenticacao()
								|| getTipoLancamentoContaPagar().isOrdemPagamento()));
	}

	public boolean isApresentarDadosTituloBancaria() {
		return (getFormaPagamentoVO().getTipoFormaPagamentoEnum() != null 	&& getFormaPagamentoVO().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.BOLETO_BANCARIO) && !Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()))
				|| (Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar())
						&& (getTipoLancamentoContaPagar().isLiquidacaoTituloProprioBanco()
								|| getTipoLancamentoContaPagar().isLiquidacaoTituloOutroBanco()
								|| getTipoLancamentoContaPagar().isLiquidacaoTituloCarteiraCobrancaSicoob()));

	}

	public boolean isApresentarDadosPagamentoContasTributosComCodigoBarra() {
		return Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()) && (getTipoLancamentoContaPagar().isPagamentoContasTributosComCodigoBarra());
					
	}

	public boolean isApresentarModalidadeFinalidadeTed() {
		return Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()) && getTipoLancamentoContaPagar().isTransferencia();
	}
	
	public boolean isApresentarModalidadeFinalidade() {
		return Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()) &&	
				(getTipoLancamentoContaPagar().equals(TipoLancamentoContaPagarEnum.TRANSFERENCIA_OUTRO_BANCO)
						|| getTipoLancamentoContaPagar().equals(TipoLancamentoContaPagarEnum.TED_OUTRA_TITULARIDADE)
						|| getTipoLancamentoContaPagar() == (TipoLancamentoContaPagarEnum.ITAU_TRANSFERENCIA_DOC_D));
	}
	
	public boolean isPermiteSelecionarModalidadeFinalidade() {
		return Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()) 
				&& (getTipoLancamentoContaPagar().equals(TipoLancamentoContaPagarEnum.TRANSFERENCIA_OUTRO_BANCO) 						
						|| getTipoLancamentoContaPagar().equals(TipoLancamentoContaPagarEnum.ITAU_TRANSFERENCIA_DOC_D)
						|| getTipoLancamentoContaPagar().equals(TipoLancamentoContaPagarEnum.PIXTRANSFERÊNCIA));
	}

	public boolean isApresentarTipoConta() {
		return Uteis.isAtributoPreenchido(getBancoRemessaPagar().getCodigo()) &&
				Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()) &&
				(getTipoLancamentoContaPagar() == (TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE)
						|| getTipoLancamentoContaPagar() == TipoLancamentoContaPagarEnum.CREDITO_CONTA_POUPANCA
						|| getTipoLancamentoContaPagar() == TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE_MESMA_TITULARIDADE
						|| getTipoLancamentoContaPagar() == TipoLancamentoContaPagarEnum.TRANSFERENCIA_OUTRO_BANCO
						|| getTipoLancamentoContaPagar() == TipoLancamentoContaPagarEnum.TED_OUTRA_TITULARIDADE
						|| getTipoLancamentoContaPagar() == TipoLancamentoContaPagarEnum.TED_MESMA_TITULARIDADE);
	}

	public boolean isApresentarCodigoBarra() {
		return (getFormaPagamentoVO().getTipoFormaPagamentoEnum() != null
				&& getFormaPagamentoVO().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.BOLETO_BANCARIO)
				&& !Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()))
				|| (Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar())
						&& (getTipoLancamentoContaPagar() == (TipoLancamentoContaPagarEnum.PAGAMENTO_CONTAS_TRIBUTOS_COM_CODIGO_BARRA)
								|| getTipoLancamentoContaPagar() == (TipoLancamentoContaPagarEnum.PAGAMENTO_CONTAS_CONCES_TRIBUTOS_COM_CODIGO_BARRA)
								|| getTipoLancamentoContaPagar() == (TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_OUTRO_BANCO)
								|| getTipoLancamentoContaPagar() == (TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_PROPRIO_BANCO)));
	}

	public boolean isApresentarDadosTributos() {
		return Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()) &&
				(getTipoLancamentoContaPagar().isGpsSemCodigoBarra()
						|| getTipoLancamentoContaPagar().isDarfNormalSemCodigoBarra()
						|| getTipoLancamentoContaPagar().isDarfSimplesSemCodigoBarra()
						|| getTipoLancamentoContaPagar().isGareDrSemCodigoBarra()
						|| getTipoLancamentoContaPagar().isGareIcmsSemCodigoBarra()
						|| getTipoLancamentoContaPagar().isGareItcmdSemCodigoBarra());
	}

	public boolean isHabilitarCampoNumeroNotaFiscal() {
		return Uteis.isAtributoPreenchido(getTipoOrigemEnum()) && (getTipoOrigemEnum().equals(OrigemContaPagar.COMPRA) || getTipoOrigemEnum().equals(OrigemContaPagar.RECEBIMENTO_COMPRA) || getTipoOrigemEnum().equals(OrigemContaPagar.NOTA_FISCAL_ENTRADA));
	}
	
	 
	public boolean isHabilitarEdicaoContaPagarPorNotaFiscal() {
		return !Uteis.isAtributoPreenchido(getCodigo()) 
				|| !(Uteis.isAtributoPreenchido(getCodigo()) 
						&& (getJuro() > 0 || getMulta() > 0)
						&& getListaContaPagarAdiantamentoVO().stream().anyMatch(p-> Uteis.isAtributoPreenchido(p.getCodigo())));
	}

	public boolean equalsCampoSelecaoListaPorNotaFiscalEntrada(ContaPagarVO obj) {
		return Uteis.isAtributoPreenchido(getDataVencimento()) && Uteis.isAtributoPreenchido(obj.getDataVencimento()) && UteisData.getCompararDatas(getDataVencimento(), obj.getDataVencimento());
	}

	public String descricaoCategoriasDespesaVOs;

	public String getDescricaoCategoriasDespesaVOs() {
		if (descricaoCategoriasDespesaVOs == null) {
			descricaoCategoriasDespesaVOs = "";
			int x = 1;
			for (CentroResultadoOrigemVO centroResultadoOrigemVO : getListaCentroResultadoOrigemVOs()) {
				if (!descricaoCategoriasDespesaVOs.contains(centroResultadoOrigemVO.getCategoriaDespesaVO().getDescricao())) {
					if (!descricaoCategoriasDespesaVOs.isEmpty()) {
						if (x == getListaCentroResultadoOrigemVOs().size()) {
							descricaoCategoriasDespesaVOs += " e ";
						} else {
							descricaoCategoriasDespesaVOs += ",";
						}
					}
					descricaoCategoriasDespesaVOs += centroResultadoOrigemVO.getCategoriaDespesaVO().getDescricao();
				}
				x++;
			}

		}
		return descricaoCategoriasDespesaVOs;
	}

	/** 
	 * INICIO DO MERGE EDIGAR 22/05/2018 
	 */
	private Double descontoPorUsoAdiantamento;
	private Double valorUtilizadoAdiantamento;
	private List<ContaPagarAdiantamentoVO> listaContaPagarAdiantamentoVO;
	
	/**
	 * TRANSIENT 
	 * Transiente ira ser utilizado para manter o valor gravado para este campo. este
	 * valor será importante durante calculo, caso o usuario deseje desfazer o uso de um
	 * adiantamento. assim temos esse backup que restabelecer o valor inicial.
	 */
	private Double valorUtilizadoAdiantamentoBackup;
	private List<ContaPagarAdiantamentoVO> listaContaPagarAdiantamentoUtilizado;
	
	public Double getValorUtilizadoAdiantamentoBackup() {
		if (valorUtilizadoAdiantamentoBackup == null) {
			valorUtilizadoAdiantamentoBackup = 0.0;
		}
		return valorUtilizadoAdiantamentoBackup;
	}

	public void setValorUtilizadoAdiantamentoBackup(Double valorUtilizadoAdiantamentoBackup) {
		this.valorUtilizadoAdiantamentoBackup = valorUtilizadoAdiantamentoBackup;
	}
	
	public Double getValorUtilizadoAdiantamentoCorrente() {
		return getValorUtilizadoAdiantamento() - getValorUtilizadoAdiantamentoBackup();
	}

	/**
	 * TRANSIENT
	 * @return
	 */
	private Boolean utilizarAdiantamentoNegociacaoPagamento;
	
	public Boolean getUtilizaAdiantamentoParaAbaterValorConta() {
		if (getDescontoPorUsoAdiantamento().doubleValue() > 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Double getValorUtilizadoAdiantamento() {
		if (valorUtilizadoAdiantamento == null) {
			valorUtilizadoAdiantamento = 0.0;
		}
		return valorUtilizadoAdiantamento;
	}

	public void setValorUtilizadoAdiantamento(Double valorUtilizadoAdiantamento) {
		this.valorUtilizadoAdiantamento = valorUtilizadoAdiantamento;
	}
	
	public Double getDescontoPorUsoAdiantamento() {
		if (descontoPorUsoAdiantamento == null) {
			descontoPorUsoAdiantamento = 0.0;
		}
		return descontoPorUsoAdiantamento;
	}

	public void setDescontoPorUsoAdiantamento(Double descontoPorUsoAdiantamento) {
		this.descontoPorUsoAdiantamento = descontoPorUsoAdiantamento;
	}

	public List<ContaPagarAdiantamentoVO> getListaContaPagarAdiantamentoVO() {
		if (listaContaPagarAdiantamentoVO == null) {
			listaContaPagarAdiantamentoVO = new ArrayList<>();
		}
		return listaContaPagarAdiantamentoVO;
	}

	public void setListaContaPagarAdiantamentoVO(List<ContaPagarAdiantamentoVO> listaContaPagarAdiantamentoVO) {
		this.listaContaPagarAdiantamentoVO = listaContaPagarAdiantamentoVO;
	}

	public List<ContaPagarAdiantamentoVO> getListaContaPagarAdiantamentoUtilizado() {
		listaContaPagarAdiantamentoUtilizado = Optional.ofNullable(listaContaPagarAdiantamentoUtilizado).orElse(new ArrayList<>());
		return listaContaPagarAdiantamentoUtilizado;
	}

	public void setListaContaPagarAdiantamentoUtilizado(List<ContaPagarAdiantamentoVO> listaContaPagarAdiantamentoUtilizado) {
		this.listaContaPagarAdiantamentoUtilizado = listaContaPagarAdiantamentoUtilizado;
	}
	public Double getTotalContaPagarAdiantamentoUtilizado() {
		return getListaContaPagarAdiantamentoUtilizado().stream().mapToDouble(p-> p.getContaPagar().getDescontoPorUsoAdiantamento()).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
	}
	
	

	public Boolean getUtilizarAdiantamentoNegociacaoPagamento() {
		if (utilizarAdiantamentoNegociacaoPagamento == null) {
			utilizarAdiantamentoNegociacaoPagamento = Boolean.TRUE;
		}
		return utilizarAdiantamentoNegociacaoPagamento;
	}

	public void setUtilizarAdiantamentoNegociacaoPagamento(Boolean utilizarAdiantamentoNegociacaoPagamento) {
		this.utilizarAdiantamentoNegociacaoPagamento = utilizarAdiantamentoNegociacaoPagamento;
	}
	
	private Double valorTemp;
	private Double juroTemp;
	private Double multaTemp;
	private Double descontoTemp;

	public Double getJuroTemp() {
		if (juroTemp == null) {
			juroTemp = 0.0;
		}
		return juroTemp;
	}

	public void setJuroTemp(Double juroTemp) {
		this.juroTemp = juroTemp;
	}

	public Double getMultaTemp() {
		if (multaTemp == null) {
			multaTemp = 0.0;
		}
		return multaTemp;
	}

	public void setMultaTemp(Double multaTemp) {
		this.multaTemp = multaTemp;
	}

	public Double getDescontoTemp() {
		if (descontoTemp == null) {
			descontoTemp = 0.0;
		}
		return descontoTemp;
	}

	public void setDescontoTemp(Double descontoTemp) {
		this.descontoTemp = descontoTemp;
	}
	
	public Double getValorFinalTemp() {
		return Uteis.arrendondarForcando2CadasDecimais(getValorTemp()+getJuroTemp()+getMultaTemp()-getDescontoTemp());
	}

	public Double getValorTemp() {
		if (valorTemp == null) {
			valorTemp = 0.0;
		}
		return valorTemp;
	}

	public void setValorTemp(Double valorTemp) {
		this.valorTemp = valorTemp;
	}
	
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

	@Override
	public String toString() {
		return "Conta a Pagar [" + this.getCodigo() + "]: " + 
                        " Data Vcto: " + this.getDataVencimento_Apresentar() + 
                        " Situação: " + this.getSituacao_Apresentar() + 
                        " Favorecido: " + this.getFavorecido() +
                        " Valor: " + Uteis.getDoubleFormatado(this.getValor()) + 
                        " Desconto (R$): " + Uteis.getDoubleFormatado(this.getDesconto()) +
                        " Desconto Adiantamento: " + Uteis.getDoubleFormatado(this.getDescontoPorUsoAdiantamento()) +
                        " Juros Já Calculados (R$): " + Uteis.getDoubleFormatado(this.getJuro()) +
                        " Multa Já Calculados (R$): " + Uteis.getDoubleFormatado(this.getMulta()) +
                        " Valor Pago: " + Uteis.getDoubleFormatado(this.getValorPago()) +
                        " Valor Pagamento: " +  Uteis.getDoubleFormatado(this.getValorPagamento()) +
                        " Tipo Origem: " + this.getTipoOrigem() + 
                        " Cod.Origem: " + this.getCodOrigem() + 
                        " Nosso Número: " + this.getNossoNumero();
	}	
	
	public Boolean getPermitiEditarValorCentroResultadoOrigem() {
		return !getPrecoCentroResultadoTotal().equals(getPrevisaoValorPagoSemDescontoPorUsoAdiantamento()) && getPorcentagemCentroResultadoTotal() >= 100.00;
	}
	
	public Boolean getPermitiEditarPorcentagemCentroResultadoOrigem() {
		return getPorcentagemCentroResultadoTotal() > 100.00 || (getPrecoCentroResultadoTotal().equals(getPrevisaoValorPagoSemDescontoPorUsoAdiantamento()) && getPorcentagemCentroResultadoTotal() < 100.00);
	}
	
	public String getCodigoNotaFiscalEntrada() {
		codigoNotaFiscalEntrada = Optional.ofNullable(codigoNotaFiscalEntrada).orElse("");
		return codigoNotaFiscalEntrada;
	}

	public void setCodigoNotaFiscalEntrada(String codigoNotaFiscalEntrada) {
		this.codigoNotaFiscalEntrada = codigoNotaFiscalEntrada;
	}
	
	private static void validarFormatoDoCodigoNotaFiscalEntrada(ContaPagarVO contaPagar) {
		if (Uteis.isAtributoPreenchido(contaPagar.getCodigoNotaFiscalEntrada())) {
			String[] lista = contaPagar.getCodigoNotaFiscalEntrada().trim().split(",");
			if (lista.length > 0) {
				StringBuilder sb = new StringBuilder();
				for (String numero : lista) {
					converterCodigoNotaFiscalEntradaParaInteiro(numero);
					sb.append(sb.toString().isEmpty() ? numero.trim() : "," + numero.trim());
				}
				contaPagar.setCodigoNotaFiscalEntrada(sb.toString());
			} else {
				converterCodigoNotaFiscalEntradaParaInteiro(contaPagar.getCodigoNotaFiscalEntrada());
			}
		}
	}

	private static void converterCodigoNotaFiscalEntradaParaInteiro(String codigoNotaFiscalEntrada) {
		try {
			Integer.parseInt(codigoNotaFiscalEntrada.trim());
		} catch (NumberFormatException e) {
			throw new StreamSeiException("O campo Nº Nota Fiscal de Entrada não esta no formato correto.");
		}
	}

	public ArquivoVO getArquivoVO() {
		if(arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}

	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}
	
	public boolean getIsPossuiArquivo() {
	       return !getArquivoVO().getNome().equals("");
	}
	
	public FinalidadePixEnum getFinalidadePixEnum() {
		return finalidadePixEnum;
	}

	public void setFinalidadePixEnum(FinalidadePixEnum finalidadePixEnum) {
		this.finalidadePixEnum = finalidadePixEnum;
	}

	public String getChaveEnderecamentoPix() {
		return chaveEnderecamentoPix;
	}

	public void setChaveEnderecamentoPix(String chaveEnderecamentoPix) {
		this.chaveEnderecamentoPix = chaveEnderecamentoPix;
	}

	public TipoIdentificacaoChavePixEnum getTipoIdentificacaoChavePixEnum() {
		if(tipoIdentificacaoChavePixEnum == null ) {
			tipoIdentificacaoChavePixEnum = TipoIdentificacaoChavePixEnum.CPF_CNPJ;
		}
		return tipoIdentificacaoChavePixEnum;
	}

	public void setTipoIdentificacaoChavePixEnum(TipoIdentificacaoChavePixEnum tipoIdentificacaoChavePixEnum) {
		this.tipoIdentificacaoChavePixEnum = tipoIdentificacaoChavePixEnum;
	}

	
	
	
	
	 public void validarSubistituirTipoLancamentoDepreciado () {
		    if (Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()) && getTipoLancamentoContaPagar().isDepreciado()) {						
				for (TipoLancamentoContaPagarEnum tipoLancamentoContaPagarEnum : TipoLancamentoContaPagarEnum.values()) {
					if(!tipoLancamentoContaPagarEnum.isDepreciado() && tipoLancamentoContaPagarEnum.getValor().equals(getTipoLancamentoContaPagar().getValor())) {
						setTipoLancamentoContaPagar(tipoLancamentoContaPagarEnum);
						break;
					}		
			    }				
		   }
	 }
	 
	 
	 public List<SelectItem> getComboTipoIdentificacaoChavePixEnum() {
	
		    List<SelectItem> comboTipoIdentificacaoChavePixEnum = new ArrayList<>();
			Bancos layout = Bancos.getEnum(getBancoRemessaPagar().getNrBanco());
			if (layout != null) {
				for (TipoIdentificacaoChavePixEnum tipoIdentificacaoChavePixEnum : TipoIdentificacaoChavePixEnum.values()) {					
					if (tipoIdentificacaoChavePixEnum.equals(TipoIdentificacaoChavePixEnum.DADOS_BANCARIOS)) {		
						if(!layout.getNumeroBanco().equals("341")) {							
							comboTipoIdentificacaoChavePixEnum.add(new SelectItem(tipoIdentificacaoChavePixEnum, tipoIdentificacaoChavePixEnum.getDescricao()));
						}
					}else {
						comboTipoIdentificacaoChavePixEnum.add(new SelectItem(tipoIdentificacaoChavePixEnum, tipoIdentificacaoChavePixEnum.getDescricao()));	
					}
				}				
			}
		
		return comboTipoIdentificacaoChavePixEnum;
	}
		
		
		
	public static void validarPreenchimentoCorretoCamposLinhaDigitavelDadosSegmentoJ(ContaPagarVO obj) {	 
			
			if( Uteis.isAtributoPreenchido(obj.getLinhaDigitavel1()) &&  obj.getLinhaDigitavel1().length() < 4) {
				throw new StreamSeiException("O campo Linha Digitável 1 deve ter 5 Digitos.");		
			}
			if( Uteis.isAtributoPreenchido(obj.getLinhaDigitavel2()) &&  obj.getLinhaDigitavel2().length() < 5 ) {
				throw new StreamSeiException("O campo Linha Digitável 1 deve ter 5 Digitos.");		
					}
			if( Uteis.isAtributoPreenchido(obj.getLinhaDigitavel3()) &&  obj.getLinhaDigitavel3().length() < 5) {
				throw new StreamSeiException("O campo Linha Digitável 1 deve ter 5 Digitos.");
			}
			if( Uteis.isAtributoPreenchido(obj.getLinhaDigitavel4()) &&  obj.getLinhaDigitavel4().length() < 6) {
				throw new StreamSeiException("O campo Linha Digitável 4 deve ter 6 Digitos.");
			}
			if( Uteis.isAtributoPreenchido(obj.getLinhaDigitavel5()) &&  obj.getLinhaDigitavel5().length() < 5) {
				throw new StreamSeiException("O campo Linha Digitável 1 deve ter 5 Digitos.");
			}
			if( Uteis.isAtributoPreenchido(obj.getLinhaDigitavel6()) &&  obj.getLinhaDigitavel6().length() < 6 ) {
				throw new StreamSeiException("O campo Linha Digitável 1 deve ter 6 Digitos.");
			}
			if( Uteis.isAtributoPreenchido(obj.getLinhaDigitavel7()) &&  obj.getLinhaDigitavel7().length() < 1) {
				throw new StreamSeiException("O campo Linha Digitável 1 deve ter 1 Digitos.");
			}
			if( Uteis.isAtributoPreenchido(obj.getLinhaDigitavel8()) &&  obj.getLinhaDigitavel8().length() > 14) {
				throw new StreamSeiException("O campo Linha Digitável 8 deve ter 14 Digitos.");
			}			
		}

	
		public static void validarPreenchimentoCorretoCamposLinhaDigitavelDadosSegmentoO(ContaPagarVO obj) {		
				
				if(Uteis.isAtributoPreenchido(obj.getLinhaDigitavel1()) &&  obj.getLinhaDigitavel1().length() < 11) {
					throw new StreamSeiException("O campo Linha Digitável 1 deve ter 11 Digitos.");		
				}
				if( Uteis.isAtributoPreenchido(obj.getLinhaDigitavel2()) &&  obj.getLinhaDigitavel2().length() < 11) {
					throw new StreamSeiException("O campo Linha Digitável 2 deve ter 11 Digitos.");	
				}
				if( Uteis.isAtributoPreenchido(obj.getLinhaDigitavel3()) &&  obj.getLinhaDigitavel3().length() < 11) {
					throw new StreamSeiException("O campo Linha Digitável 3 deve ter 11 Digitos.");
				}
				if( Uteis.isAtributoPreenchido(obj.getLinhaDigitavel4()) &&  obj.getLinhaDigitavel4().length() < 11) {
				throw new StreamSeiException("O campo Linha Digitável 4 deve ter 11 Digitos.");
				}	
			}
}
