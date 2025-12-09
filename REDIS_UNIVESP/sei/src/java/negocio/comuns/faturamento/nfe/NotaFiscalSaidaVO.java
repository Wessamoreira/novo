package negocio.comuns.faturamento.nfe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.CondicaoPagamentoVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.faturamento.nfe.enumeradores.SituacaoNotaFiscalSaidaEnum;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import webservice.nfse.WebServicesNFSEEnum;
import webservice.nfse.generic.NaturezaOperacaoEnum;
import webservice.nfse.generic.RegimeEspecialTributacaoEnum;

public class NotaFiscalSaidaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	protected Integer codigo;
	protected Date dataEmissao;
	protected Long numero;
	protected String modelo;
	protected String serie;
	protected String lote;
	protected String xmlEnvio;
	protected String xmlCancelamento;
	protected Boolean transferencia;
	protected Integer volume;
	protected UsuarioVO responsavel;
	protected PessoaVO pessoaVO;
	protected PessoaVO responsavelFinanceiro;
	protected Double valorTotal;
	protected Double valorTotalServico;
	protected CondicaoPagamentoVO condicaoPagamento;
	protected FormaPagamentoVO formaPagamento;
	protected UnidadeEnsinoVO unidadeEnsinoVO;
	protected String identificadorReceita;
	protected CidadeVO cidade;
	protected Double totalQtd;
	protected Double totalQtdServico;
	protected Double totalIpi;
	protected Double totalIpiServico;
	protected Double totalBaseIcms;
	protected Double totalIcms;
	protected Double totalIcmsServico;
	protected Double totalBaseSubTrib;
	protected Double totalSubTrib;
	protected Double totalSubTribServico;
	protected String observacao;
	protected String observacaoContribuinte;
	protected Boolean cancelado;
	protected String motivoCancelamento;
	protected String motivoInutilizacao;
	protected String loteCancelamento;
	protected String recibo;
	protected String situacao;
	protected Date dataStuacao;
	protected String protocolo;
	protected String protocoloCancelamento;
	protected String protocoloInutilizacao;
	private String descricaoCartaCorrecao;
	private Integer seqEventoCartaCorrecao;
	private String protocoloCartaCorrecao;
	private String xmlCartaCorrecao;
	private Integer notaFiscalClone;
	private List<NotaFiscalSaidaServicoVO> notaFiscalSaidaServicoVOs;
	private String matricula;
	private String tipoPessoa;
	private Double totalIssqn;
	private Double totalIss;
	private String sacado;
	private FornecedorVO fornecedorVO;
	private ParceiroVO parceiroVO;
	private FuncionarioVO funcionarioVO;
	private Boolean notaFiscalSaidaSelecionado;
	private Integer codigoSacado;
	private String mensagemRetorno;
	private Double valorTotalPIS;
	private Double valorTotalCOFINS;
	private Double valorTotalINSS;
	private Double valorTotalIRRF;
	private Double valorTotalCSLL;
	private Double valorLiquido;
	
	private Double aliquotaIssqn;
	private Double aliquotaPis;
	private Double aliquotaCofins;
	private Double aliquotaInss;
	private Double aliquotaCsll;
	private Double aliquotaIr;
	
	private Double percentualCargaTributaria;
	private Double valorCargaTributaria;
	private String fonteCargaTributaria;

	// NATUREZA OPERAÇÃO
	private String codigoNaturezaOperacao;
	private String nomeNaturezaOperacao;
	
	private Double totalBaseIssqn;
	private Double totalAliquotaIssqn;
	private Integer numeroRPS;
	
	/**
	 * variavel gruada o retorno completo da nota vinda da sefaz
	 * para que possa assim gerar o ProcNFe
	 * Variavel não deve ser persistida
	 */
	private String retornoCompleto;
	private String xmlNfeProc;
	
	/**
	 * Construtor padrão da classe <code>NotaFiscalSaida</code>. Cria uma nova
	 * instï¿½ncia desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	private String endereco;
	private String setor;
	private String nomeRazaoSocial;
	private String cnpjCpf;
	private String cep;
	private String inscricaoEstadual;
	private String uf;
	private String telefone;
	private String municipio;
	private List<CartaCorrecaoVO> cartaCorrecaoVOs;
	private String informacaoFiliacaoApresentarDanfe;
	private Boolean issRetido;
	
	private NaturezaOperacaoEnum naturezaOperacaoEnum;
	private Boolean isIncentivadorCultural;
	private RegimeEspecialTributacaoEnum regimeEspecialTributacaoEnum;
	private String codigoCNAE;
	
	// Transient
	private Date dataEmissaoRetroativa;
	private String nomesConvenios;
	private String jsonEnvio;
	private String jsonRetornoEnvio;
	private Boolean possuiErroWebservice;
	
	public NotaFiscalSaidaVO() {
		super();
		inicializarDados();
	}
	
    public JRDataSource getNotaFiscalSaidaServicoVOsJRDataSource() {
        JRDataSource jr = new JRBeanArrayDataSource(getNotaFiscalSaidaServicoVOs().toArray());
        return jr;
    }

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>NotaFiscalSaidaVO</code>. Todos os tipos de consistência de dados
	 * sï¿½o e devem ser implementadas neste mï¿½todo. Sï¿½o validaï¿½ï¿½es
	 * tï¿½picas: verificaï¿½ï¿½o de campos obrigatï¿½rios, verificaï¿½ï¿½o de
	 * valores válidos para os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceï¿½ï¿½o descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(NotaFiscalSaidaVO obj, Integer cfopEntregaFuturaEstadual, Integer cfopEntregaFuturaInterestadual) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getDataEmissao() == null) {
			throw new ConsistirException("O campo DATA EMISSÃO (Nota Fiscal Saída) deve ser informado.");
		}
		if ((obj.getResponsavel() == null) || (obj.getResponsavel().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo RESPONSÁVEL (Nota Fiscal Saída) deve ser informado.");
		}
		if ((obj.getUnidadeEnsinoVO() == null) || (obj.getUnidadeEnsinoVO().getCodigo().intValue() == 0)) {
			throw new ConsistirException("O campo Empresa (Nota Fiscal Saída) deve ser informado.");
		}
		if ((obj.getPessoaVO() == null) || (obj.getPessoaVO().getCodigo().intValue() == 0) && !obj.getTransferencia()) {
			throw new ConsistirException("O campo CLIENTE (Nota Fiscal Saída) deve ser informado.");
		}
		if (obj.getModelo().equals("")) {
			throw new ConsistirException("O campo MODELO (Nota Fiscal Saída) deve ser informado.");
		}
		if (obj.getSerie().equals("")) {
			throw new ConsistirException("O campo SÉRIE (Nota Fiscal Saída) deve ser informado.");
		}
		if (!obj.getCancelado()) {
			obj.setMotivoCancelamento("");
		}
		if (obj.getValorTotal().doubleValue() == 0.0) {
			throw new ConsistirException("O VALOR TOTAL da Nota Fiscal Saída não pode ser zerado.");
		}

		// int valorTotal = 0;
		// for (ContaReceberVO conta : obj.getContaReceberVOs()) {
		// if(conta.getValor() == null || conta.getValor() == 0) {
		// throw new
		// ConsistirException("O VALOR da parcela (Condição Pagamento) não pode ser zerado.");
		// }
		// valorTotal += conta.getValor();
		// }
	}

	/**
	 * Operação responsável por realizar o UpperCase dos atributos do tipo
	 * String.
	 */
	public void realizarUpperCaseDados() {
		setModelo(getModelo().toUpperCase());
		setSerie(getSerie().toUpperCase());
		setMotivoCancelamento(motivoCancelamento.toUpperCase());
	}

	public static void validarNotaFiscalAptaParaExcluir(NotaFiscalSaidaVO notaPrincipal, List<NotaFiscalSaidaVO> notasTransferencias) throws Exception {
		if (notaPrincipal.getNumero().longValue() > 0l) {
			throw new Exception("NOTA FISCAL DE SAIDA não pode mais ser excluida.");
		}
		for (NotaFiscalSaidaVO obj : notasTransferencias) {
			if (obj.getNumero().longValue() > 0l) {
				throw new Exception("NOTA FISCAL DE SAIDA não pode mais ser excluida.");
			}
		}

	}

	public void validarCancelamento() throws Exception {
		motivoCancelamento = motivoCancelamento.toUpperCase();
		if (!getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor())) {
			throw new Exception("A nota deve ser primeiro autorizada, para efetuar o cancelamento.");
		}
		if (motivoCancelamento.length() < 15 || motivoCancelamento.length() > 255) {
			throw new Exception("O campo MOTIVO (Nota Fiscal Saída) deve ter entre 15 a 255 caracteres.");
		}
	}

	/**
	 * Operação responsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setDataEmissao(new Date());
		setNumero(0L);
		setModelo("");
		setSerie("");
		setTransferencia(Boolean.FALSE);
		setVolume(0);
		setResponsavel(new UsuarioVO());
		setCondicaoPagamento(new CondicaoPagamentoVO());
		setValorTotal(0.0);
		setValorTotalServico(0.0);
		setObservacaoContribuinte("");
		setCidade(new CidadeVO());
		setIdentificadorReceita("");
		setTotalBaseIcms(0.0);
		setTotalBaseSubTrib(0.0);
		setObservacao("");
		setLote("");
		setXmlEnvio("");
		setCancelado(false);
		setMotivoCancelamento("");
		setLoteCancelamento("");
		setRecibo("");
		setSituacao("AE");
		setDataStuacao(new Date());
		setProtocolo("");
	}

	public Boolean getBotaoExcluir() {
		if (getNumero().longValue() == 0) {
			return true;
		}
		return false;
	}

	public String getChaveAcesso() {
		if (identificadorReceita.startsWith("NFe")) {
			return identificadorReceita.substring(3, identificadorReceita.length());
		}
		return identificadorReceita;
	}

	public Boolean getEditar() {
		if (getCodigo().intValue() != 0) {
			return true;
		}
		return false;
	}

	public String getCssObrigatorio() {
		if (getEditar()) {
			return "camposSomenteLeitura";
		}
		return "camposObrigatorios";
	}

	public String getCss() {
		if (getEditar()) {
			return "camposSomenteLeitura";
		}
		return "campos";
	}

	public String getTransferencia_Apresentar() {
		if (transferencia) {
			return "SIM";
		}
		return "NÃO";
	}

	public String getNumeroReceita() {
		return Uteis.adicionarMascaraCodigoBarraNFE(getChaveAcesso());
	}

	public String getNumeroNota() {
		return numero.toString();
	}

	public void criarIdNFe() throws Exception {
		if (numero.longValue() > 0l) {
			String codigoIBGE = "";// getEmpresa().getCidade().getCodigoIBGE().substring(0,
									// 2);
			String data = UteisData.getAnoMes(dataEmissao);
			String cnpj = "";
			// if(getEmpresa().getMatriz()){
			cnpj = UteisTexto.removerMascara(""); // getEmpresa().getCnpj()
			// }else if(getEmpresaDestino().getMatriz()){
			// cnpj = UteisTexto.removerMascara(getEmpresaDestino().getCnpj());
			// }
			// else{
			// EmpresaPadraoVO obj =
			// getEmpresaPadrao().consultarPorEmpresaMatrizIdentificador(getEmpresa().getIdentificador(),
			// false, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
			// cnpj = UteisTexto.removerMascara(obj.getCnpj());
			// }
			String model = modelo;
			String ser = "";
			if (modelo.equals("1") || modelo.equals("01") || modelo.equalsIgnoreCase("1A")) {
				model = "55";
			}
			if (model.length() < 2) {
				model = Uteis.getMontarCodigoBarra(model, 2);
			}
			if (serie.length() < 3) {
				ser = Uteis.getMontarCodigoBarra(serie, 3);
			}
			String codigoNFe = Uteis.getMontarCodigoBarra(getNumero().toString(), 9);
			String id = codigoIBGE + data + cnpj + model + ser + codigoNFe + codigoNFe;
			int x = id.length();
			int multi = 2;
			int valorTotal = 0;
			while (x > 0) {
				if (multi == 10) {
					multi = 2;
				}
				int valor = 0;
				if (x == 43) {
					valor = Integer.parseInt(id.substring(x - 1, id.length()));
				} else {
					valor = Integer.parseInt(id.substring(x - 1, x));
				}
				valorTotal = valorTotal + (valor * multi);

				multi++;
				x--;
			}
			int resto = valorTotal % 11;
			if (resto > 1) {
				resto = 11 - resto;
			} else {
				resto = 0;
			}
			setIdentificadorReceita("NFe" + id + resto);
		} else {
			setIdentificadorReceita("");
		}
	}

	/**
	 * Retorna o objeto da classe <code>Usuario</code> relacionado com (
	 * <code>NotaFiscalSaida</code>).
	 */
	public UsuarioVO getResponsavel() {
		if (responsavel == null) {
			responsavel = new UsuarioVO();
		}
		return (responsavel);
	}

	/**
	 * Define o objeto da classe <code>Usuario</code> relacionado com (
	 * <code>NotaFiscalSaida</code>).
	 */
	public void setResponsavel(UsuarioVO obj) {
		this.responsavel = obj;
	}

	public Integer getVolume() {
		if (volume == null) {
			volume = 0;
		}
		return (volume);
	}

	public void setVolume(Integer volume) {
		this.volume = volume;
	}

	public Boolean getTransferencia() {
		return (transferencia);
	}

	public Boolean isTransferencia() {
		if (transferencia == null) {
			transferencia = Boolean.FALSE;
		}
		return (transferencia);
	}

	public void setTransferencia(Boolean transferencia) {
		this.transferencia = transferencia;
	}

	public String getSerie() {
		if (serie == null) {
			serie = "";
		}
		return (serie);
	}

	public void setSerie(String serie) {
		this.serie = serie;
	}

	public String getModelo() {
		if (modelo == null) {
			modelo = "";
		}
		return (modelo);
	}

	public void setModelo(String modelo) {
		this.modelo = modelo;
	}

	public Long getNumero() {
		if (numero == null) {
			numero = 0l;
		}
		return (numero);
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public Date getDataEmissao() {
		if (dataEmissao == null) {
			dataEmissao = new Date();
		}
		return (dataEmissao);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataEmissao_Apresentar() {
		return (UteisData.getData(dataEmissao));
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
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

	public CondicaoPagamentoVO getCondicaoPagamento() {
		if (condicaoPagamento == null) {
			condicaoPagamento = new CondicaoPagamentoVO();
		}
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(CondicaoPagamentoVO condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	public Double getValorTotalServico() {
		return valorTotalServico;
	}

	public void setValorTotalServico(Double valorTotalServico) {
		this.valorTotalServico = valorTotalServico;
	}

	public Double getValorTotal() {
		return valorTotal;
	}

	public String getValorTotal_Apresentar() {
		return (Uteis.getDoubleFormatado(valorTotal));
	}

	public void setValorTotal(Double valorTotal) {
		this.valorTotal = valorTotal;
	}

	public CidadeVO getCidade() {
		return cidade;
	}

	public void setCidade(CidadeVO cidade) {
		this.cidade = cidade;
	}

	public Double getTotalIcms() {
		if (totalIcms == null) {
			totalIcms = 0.0;
		}
		return totalIcms;
	}

	public void setTotalIcms(Double totalIcms) {
		this.totalIcms = totalIcms;
	}

	public Double getTotalIcmsServico() {
		if (totalIcmsServico == null) {
			totalIcmsServico = 0.0;
		}
		return totalIcmsServico;
	}

	public void setTotalIcmsServico(Double totalIcmsServico) {
		this.totalIcmsServico = totalIcmsServico;
	}

	public Double getTotalIpi() {
		if (totalIpi == null) {
			totalIpi = 0.0;
		}
		return totalIpi;
	}

	public void setTotalIpi(Double totalIpi) {
		this.totalIpi = totalIpi;
	}

	public Double getTotalIpiServico() {
		if (totalIpiServico == null) {
			totalIpiServico = 0.0;
		}
		return totalIpiServico;
	}

	public void setTotalIpiServico(Double totalIpiServico) {
		this.totalIpiServico = totalIpiServico;
	}

	public Double getTotalSubTrib() {
		if (totalSubTrib == null) {
			totalSubTrib = 0.0;
		}
		return totalSubTrib;
	}

	public void setTotalSubTrib(Double totalSubTrib) {
		this.totalSubTrib = totalSubTrib;
	}

	public Double getTotalQtd() {
		if (totalQtd == null) {
			totalQtd = 0.0;
		}
		return totalQtd;
	}

	public void setTotalQtd(Double totalQtd) {
		this.totalQtd = totalQtd;
	}

	public Double getTotalQtdServico() {
		if (totalQtdServico == null) {
			totalQtdServico = 0.0;
		}
		return totalQtdServico;
	}

	public void setTotalQtdServico(Double totalQtdServico) {
		this.totalQtdServico = totalQtdServico;
	}

	public Double getTotalSubTribServico() {
		if (totalSubTribServico == null) {
			totalSubTribServico = 0.0;
		}
		return totalSubTribServico;
	}

	public void setTotalSubTribServico(Double totalSubTribServico) {
		this.totalSubTribServico = totalSubTribServico;
	}

	public Double getTotalBaseIcms() {
		if (totalBaseIcms == null) {
			totalBaseIcms = 0.0;
		}
		return totalBaseIcms;
	}

	public void setTotalBaseIcms(Double totalBaseIcms) {
		this.totalBaseIcms = totalBaseIcms;
	}

	public Double getTotalBaseSubTrib() {
		if (totalBaseSubTrib == null) {
			totalBaseSubTrib = 0.0;
		}
		return totalBaseSubTrib;
	}

	public void setTotalBaseSubTrib(Double totalBaseSubTrib) {
		this.totalBaseSubTrib = totalBaseSubTrib;
	}

	public String getIdentificadorReceita() {
		if (identificadorReceita == null) {
			return "";
		}
		return identificadorReceita;
	}

	public void setIdentificadorReceita(String identificadorReceita) {
		this.identificadorReceita = identificadorReceita;
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

	public String getLote() {
		if (lote == null) {
			lote = "";
		}
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getXmlEnvio() {
		if (xmlEnvio == null) {
			xmlEnvio = "";
		}
		return xmlEnvio;
	}

	public void setXmlEnvio(String xmlEnvio) {
		this.xmlEnvio = xmlEnvio;
	}

	public Boolean getCancelado() {
		if (cancelado == null) {
			cancelado = false;
		}
		return cancelado;
	}

	public void setCancelado(Boolean cancelado) {
		this.cancelado = cancelado;
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

	public String getLoteCancelamento() {
		if (loteCancelamento == null) {
			loteCancelamento = "";
		}
		return loteCancelamento;
	}

	public void setLoteCancelamento(String loteCancelamento) {
		this.loteCancelamento = loteCancelamento;
	}

	public String getRecibo() {
		if (recibo == null) {
			recibo = "";
		}
		return recibo;
	}

	public void setRecibo(String recibo) {
		this.recibo = recibo;
	}

	public Boolean getDesabilitarEnviar() {
		if (getSituacao().equals(SituacaoNotaFiscalSaidaEnum.ENVIADA.getValor())) {
			return true;
		}
		return false;
	}

	public Boolean getDesaparecerEnviar() {
		if (getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor()) || getSituacao().equals(SituacaoNotaFiscalSaidaEnum.CANCELADA.getValor()) || getSituacao().equals(SituacaoNotaFiscalSaidaEnum.INUTILIZADA.getValor()) || !getEditar()) {
			return false;
		}
		return true;
	}

	public Boolean getDesaparecerInutilizar() {
		if (getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor()) || getSituacao().equals(SituacaoNotaFiscalSaidaEnum.CANCELADA.getValor()) || getSituacao().equals(SituacaoNotaFiscalSaidaEnum.INUTILIZADA.getValor()) || !getEditar()) {
			return false;
		}
		return true;
	}

	public Boolean getDesaparecerCartaCorrecao() {
		if (!getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor()) || !getEditar()) {
			return false;
		}
		return true;
	}

	public Boolean getImprimirDanfe() {
		if (getSituacao().equals(SituacaoNotaFiscalSaidaEnum.AUTORIZADA.getValor())) {
			return true;
		}
		return false;
	}

	public String getSituacao_Apresentar() {
		if (situacao == null) {
			return "";
		}
		return SituacaoNotaFiscalSaidaEnum.getEnumPorValor(situacao).getDescricao();
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = SituacaoNotaFiscalSaidaEnum.AGUARDANDO_ENVIO.getValor();
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getDataStuacao_Apresentar() {
		return UteisData.getDataComHora(dataStuacao);
	}

	public Date getDataStuacao() {
		return dataStuacao;
	}

	public void setDataStuacao(Date dataStuacao) {
		this.dataStuacao = dataStuacao;
	}

	public String getProtocolo_Apresentar() {
		if (protocolo == null) {
			protocolo = "";
		}
		return protocolo + "  " + getDataStuacao_Apresentar();
	}

	public String getProtocolo() {
		if (protocolo == null) {
			protocolo = "";
		}
		return protocolo;
	}

	public void setProtocolo(String protocolo) {
		this.protocolo = protocolo;
	}

	public String getObservacaoContribuinte() {
		if (observacaoContribuinte == null) {
			observacaoContribuinte = "";
		}
		return observacaoContribuinte;
	}

	public void setObservacaoContribuinte(String observacaoContribuinte) {
		this.observacaoContribuinte = observacaoContribuinte;
	}

	public String getXmlCancelamento() {
		if (xmlCancelamento == null) {
			xmlCancelamento = "";
		}
		return xmlCancelamento;
	}

	public void setXmlCancelamento(String xmlCancelamento) {
		this.xmlCancelamento = xmlCancelamento;
	}

	public String getMotivoInutilizacao() {
		if (motivoInutilizacao == null) {
			motivoInutilizacao = "";
		}
		return motivoInutilizacao;
	}

	public void setMotivoInutilizacao(String motivoInutilizacao) {
		this.motivoInutilizacao = motivoInutilizacao;
	}

	public String getProtocoloCancelamento() {
		if (protocoloCancelamento == null) {
			protocoloCancelamento = "";
		}
		return protocoloCancelamento;
	}

	public void setProtocoloCancelamento(String protocoloCancelamento) {
		this.protocoloCancelamento = protocoloCancelamento;
	}

	public String getProtocoloInutilizacao() {
		if (protocoloInutilizacao == null) {
			protocoloInutilizacao = "";
		}
		return protocoloInutilizacao;
	}

	public void setProtocoloInutilizacao(String protocoloInutilizacao) {
		this.protocoloInutilizacao = protocoloInutilizacao;
	}

	public String getDescricaoCartaCorrecao() {
		if (descricaoCartaCorrecao == null) {
			descricaoCartaCorrecao = "";
		}
		return descricaoCartaCorrecao;
	}

	public void setDescricaoCartaCorrecao(String descricaoCartaCorrecao) {
		this.descricaoCartaCorrecao = descricaoCartaCorrecao;
	}

	public Integer getSeqEventoCartaCorrecao() {
		if (seqEventoCartaCorrecao == null) {
			seqEventoCartaCorrecao = 0;
		}
		return seqEventoCartaCorrecao;
	}

	public void setSeqEventoCartaCorrecao(Integer seqEventoCartaCorrecao) {
		this.seqEventoCartaCorrecao = seqEventoCartaCorrecao;
	}

	public String getXmlCartaCorrecao() {
		if (xmlCartaCorrecao == null) {
			xmlCartaCorrecao = "";
		}
		return xmlCartaCorrecao;
	}

	public void setXmlCartaCorrecao(String xmlCartaCorrecao) {
		this.xmlCartaCorrecao = xmlCartaCorrecao;
	}

	public String getProtocoloCartaCorrecao() {
		if (protocoloCartaCorrecao == null) {
			protocoloCartaCorrecao = "";
		}
		return protocoloCartaCorrecao;
	}

	public void setProtocoloCartaCorrecao(String protocoloCartaCorrecao) {
		this.protocoloCartaCorrecao = protocoloCartaCorrecao;
	}

	public FormaPagamentoVO getFormaPagamento() {
		if (formaPagamento == null) {
			formaPagamento = new FormaPagamentoVO();
		}
		return formaPagamento;
	}

	public void setFormaPagamento(FormaPagamentoVO formaPagamento) {
		this.formaPagamento = formaPagamento;
	}

	public Integer getNotaFiscalClone() {
		if (notaFiscalClone == null) {
			notaFiscalClone = 0;
		}
		return notaFiscalClone;
	}

	public void setNotaFiscalClone(Integer notaFiscalClone) {
		this.notaFiscalClone = notaFiscalClone;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public List<NotaFiscalSaidaServicoVO> getNotaFiscalSaidaServicoVOs() {
		if (notaFiscalSaidaServicoVOs == null) {
			notaFiscalSaidaServicoVOs = new ArrayList<NotaFiscalSaidaServicoVO>(0);
		}
		return notaFiscalSaidaServicoVOs;
	}

	public void setNotaFiscalSaidaServicoVOs(List<NotaFiscalSaidaServicoVO> notaFiscalSaidaServicoVOs) {
		this.notaFiscalSaidaServicoVOs = notaFiscalSaidaServicoVOs;
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

	public Double getTotalIssqn() {
		if (totalIssqn == null) {
			totalIssqn = 0.0;
		}
		return totalIssqn;
	}

	public String getTotalIssqn_Apresentar() {
		return (Uteis.getDoubleFormatado(totalIssqn));
	}

	public String getTotalIss_Apresentar() {
		return (Uteis.getDoubleFormatado(totalIss));
	}

	public void setTotalIssqn(Double totalIssqn) {
		this.totalIssqn = totalIssqn;
	}

	public Double getTotalIss() {
		if (totalIss == null) {
			totalIss = 0.0;
		}
		return totalIss;
	}

	public void setTotalIss(Double totalIss) {
		this.totalIss = totalIss;
	}

	public String getSacado() {
		if (sacado == null) {
			sacado = "";
		}
		return sacado;
	}

	public void setSacado(String sacado) {
		this.sacado = sacado;
	}

	public Boolean getNotaFiscalSaidaSelecionado() {
		if (notaFiscalSaidaSelecionado == null) {
			notaFiscalSaidaSelecionado = false;
		}
		return notaFiscalSaidaSelecionado;
	}

	public void setNotaFiscalSaidaSelecionado(Boolean notaFiscalSaidaSelecionado) {
		this.notaFiscalSaidaSelecionado = notaFiscalSaidaSelecionado;
	}

	public String getCodigoNaturezaOperacao() {
		if (codigoNaturezaOperacao == null) {
			codigoNaturezaOperacao = "";
		}
		return codigoNaturezaOperacao;
	}

	public void setCodigoNaturezaOperacao(String codigoNaturezaOperacao) {
		this.codigoNaturezaOperacao = codigoNaturezaOperacao;
	}

	public String getNomeNaturezaOperacao() {
		if (nomeNaturezaOperacao == null) {
			nomeNaturezaOperacao = "";
		}
		return nomeNaturezaOperacao;
	}

	public void setNomeNaturezaOperacao(String nomeNaturezaOperacao) {
		this.nomeNaturezaOperacao = nomeNaturezaOperacao;
	}

	public FornecedorVO getFornecedorVO() {
		if (fornecedorVO == null) {
			fornecedorVO = new FornecedorVO();
		}
		return fornecedorVO;
	}

	public void setFornecedorVO(FornecedorVO fornecedorVO) {
		this.fornecedorVO = fornecedorVO;
	}

	public ParceiroVO getParceiroVO() {
		if (parceiroVO == null) {
			parceiroVO = new ParceiroVO();
		}
		return parceiroVO;
	}

	public void setParceiroVO(ParceiroVO parceiroVO) {
		this.parceiroVO = parceiroVO;
	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public Integer getCodigoSacado() {
		if (codigoSacado == null) {
			codigoSacado = 0;
		}
		return codigoSacado;
	}

	public void setCodigoSacado(Integer codigoSacado) {
		this.codigoSacado = codigoSacado;
	}

	public Double getTotalBaseIssqn() {
		if (totalBaseIssqn == null) {
			totalBaseIssqn = 0.0;
		}
		return totalBaseIssqn;
	}

	public void setTotalBaseIssqn(Double totalBaseIssqn) {
		this.totalBaseIssqn = totalBaseIssqn;
	}

	public String getMensagemRetorno() {
		if (mensagemRetorno == null) {
			mensagemRetorno = "";
		}
		return mensagemRetorno;
	}

	public void setMensagemRetorno(String mensagemRetorno) {
		this.mensagemRetorno = mensagemRetorno;
	}

	public Double getTotalAliquotaIssqn() {
		if (totalAliquotaIssqn == null) {
			totalAliquotaIssqn = 0.0;
		}
		return totalAliquotaIssqn;
	}

	public void setTotalAliquotaIssqn(Double totalAliquotaIssqn) {
		this.totalAliquotaIssqn = totalAliquotaIssqn;
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
	
	public String getDadosPessoaAtiva() {
		TipoPessoa tipoPessoaLocal = TipoPessoa.getEnum(getTipoPessoa());
		if (tipoPessoaLocal != null) {
			switch (tipoPessoaLocal) {
			case ALUNO:
				return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getPessoaVO().getNome();
			case RESPONSAVEL_FINANCEIRO:
				return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getResponsavelFinanceiro().getNome() + " (" + getInformacaoFiliacaoApresentarDanfe() + ")";
			case PARCEIRO:
				if (getPessoaVO().getNome().isEmpty()) {
					return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getParceiroVO().getNome();
				} else {
					return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getParceiroVO().getNome() + " (ALUNO(A) " + getPessoaVO().getNome() + ")";
				}
			case FUNCIONARIO:
				return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getFuncionarioVO().getPessoa().getNome();
				
			case REQUERENTE:
				return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getPessoaVO().getNome();
			}
		}
		return "";
	}
	
	/**
	 * @author Victor Hugo de Paula Costa
	 */
	private String linkAcesso;

	public String getLinkAcesso() {
		if(linkAcesso == null) {
			linkAcesso = "";
		}
		return linkAcesso;
	}

	public void setLinkAcesso(String linkAcesso) {
		this.linkAcesso = linkAcesso;
	}
	
	private WebServicesNFSEEnum webServicesNFSEEnum;

	public WebServicesNFSEEnum getWebServicesNFSEEnum() {
		if(webServicesNFSEEnum == null) {
			webServicesNFSEEnum = WebServicesNFSEEnum.GOIANIA_GO;
		}
		return webServicesNFSEEnum;
	}

	public void setWebServicesNFSEEnum(WebServicesNFSEEnum webServicesNFSEEnum) {
		this.webServicesNFSEEnum = webServicesNFSEEnum;
	}
	
	public Integer getNumeroRPS() {
		if (numeroRPS == null) {
			numeroRPS = 0;
		}
		return numeroRPS;
	}

	public void setNumeroRPS(Integer numeroRPS) {
		this.numeroRPS = numeroRPS;
	}

	public Double getValorTotalPIS() {
		if (valorTotalPIS == null) {
			valorTotalPIS = 0.0;
		}
		return valorTotalPIS;
	}

	public void setValorTotalPIS(Double valorTotalPIS) {
		this.valorTotalPIS = valorTotalPIS;
	}

	public Double getValorTotalCOFINS() {
		if (valorTotalCOFINS == null) {
			valorTotalCOFINS = 0.0;
		}
		return valorTotalCOFINS;
	}

	public void setValorTotalCOFINS(Double valorTotalCOFINS) {
		this.valorTotalCOFINS = valorTotalCOFINS;
	}
	
	public String getRetornoCompleto() {
		if(retornoCompleto == null){
			retornoCompleto = "";
		}
		return retornoCompleto;
	}

	public void setRetornoCompleto(String retornoCompleto) {
		this.retornoCompleto = retornoCompleto;
	}

	public String getXmlNfeProc() {
		if(xmlNfeProc == null ){
			xmlNfeProc = "";
		}
		return xmlNfeProc;
	}

	public void setXmlNfeProc(String xmlNfeProc) {
		this.xmlNfeProc = xmlNfeProc;
	}


	public Double getValorTotalINSS() {
		if(valorTotalINSS == null){
			valorTotalINSS = 0.0;
		}
		return valorTotalINSS;
	}

	public void setValorTotalINSS(Double valorTotalINSS) {
		this.valorTotalINSS = valorTotalINSS;
	}

	public Double getValorTotalIRRF() {
		if(valorTotalIRRF == null){
			valorTotalIRRF = 0.0;
		}
		return valorTotalIRRF;
	}

	public void setValorTotalIRRF(Double valorTotalIRRF) {
		this.valorTotalIRRF = valorTotalIRRF;
	}

	public Double getValorTotalCSLL() {
		if(valorTotalCSLL == null){
			valorTotalCSLL = 0.0;
		}
		return valorTotalCSLL;
	}

	public void setValorTotalCSLL(Double valorTotalCSLL) {
		this.valorTotalCSLL = valorTotalCSLL;
	}

	public Double getValorLiquido() {
		if(valorLiquido == null){
			valorLiquido = 0.0;
		}
		return valorLiquido;
	}

	public void setValorLiquido(Double valorLiquido) {
		this.valorLiquido = valorLiquido;
	}
	
	public Double getAliquotaIssqn() {
		if(aliquotaIssqn == null){
			aliquotaIssqn = 0.0;
		}
		return aliquotaIssqn;
	}

	public void setAliquotaIssqn(Double aliquotaIssqn) {
		this.aliquotaIssqn = aliquotaIssqn;
	}

	public Double getAliquotaPis() {
		if(aliquotaPis == null){
			aliquotaPis = 0.0;
		}
		return aliquotaPis;
	}

	public void setAliquotaPis(Double aliquotaPis) {
		this.aliquotaPis = aliquotaPis;
	}

	public Double getAliquotaCofins() {
		if(aliquotaCofins == null){
			aliquotaCofins = 0.0;
		}
		return aliquotaCofins;
	}

	public void setAliquotaCofins(Double aliquotaCofins) {
		this.aliquotaCofins = aliquotaCofins;
	}

	public Double getAliquotaInss() {
		if(aliquotaInss == null){
			aliquotaInss =0.0;
		}
		return aliquotaInss;
	}

	public void setAliquotaInss(Double aliquotaInss) {
		this.aliquotaInss = aliquotaInss;
	}

	public Double getAliquotaCsll() {
		if(aliquotaCsll == null){
			aliquotaCsll = 0.0;
		}
		return aliquotaCsll;
	}

	public void setAliquotaCsll(Double aliquotaCsll) {
		this.aliquotaCsll = aliquotaCsll;
	}

	public Double getAliquotaIr() {
		if(aliquotaIr == null){
			aliquotaIr = 0.0;
		}
		return aliquotaIr;
	}

	public void setAliquotaIr(Double aliquotaIr) {
		this.aliquotaIr = aliquotaIr;
	}

	public String getEndereco() {
		if(endereco == null){
			endereco = "";
		}
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getSetor() {
		if(setor == null){
			setor = "";
		}
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public String getNomeRazaoSocial() {
		if(nomeRazaoSocial == null){
			nomeRazaoSocial = "";
		}
		return nomeRazaoSocial;
	}

	public void setNomeRazaoSocial(String nomeRazaoSocial) {
		this.nomeRazaoSocial = nomeRazaoSocial;
	}

	public String getCnpjCpf() {
		if(cnpjCpf == null){
			cnpjCpf = "";
		}
		return cnpjCpf;
	}

	public void setCnpjCpf(String cnpjCpf) {
		this.cnpjCpf = cnpjCpf;
	}

	public String getCep() {
		if(cep == null){
			cep = "";
		}
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getInscricaoEstadual() {
		if(inscricaoEstadual == null){
			inscricaoEstadual = "";
		}
		return inscricaoEstadual;
	}

	public void setInscricaoEstadual(String inscricaoEstadual) {
		this.inscricaoEstadual = inscricaoEstadual;
	}

	public String getUf() {
		if(uf == null){
			uf = "";
		}
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getTelefone() {
		if(telefone == null){
			telefone = "";
		}
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getMunicipio() {
		if(municipio == null){
			municipio = "";
		}
		return municipio;
	}

	public void setMunicipio(String municipio) {
		this.municipio = municipio;
	}

	public List<CartaCorrecaoVO> getCartaCorrecaoVOs() {
		if(cartaCorrecaoVOs == null){
			cartaCorrecaoVOs = new ArrayList<CartaCorrecaoVO>(0);
		}
		return cartaCorrecaoVOs;
	}

	public void setCartaCorrecaoVOs(List<CartaCorrecaoVO> cartaCorrecaoVOs) {
		this.cartaCorrecaoVOs = cartaCorrecaoVOs;
	}

	public String getInformacaoFiliacaoApresentarDanfe() {
	    if(informacaoFiliacaoApresentarDanfe == null){
		informacaoFiliacaoApresentarDanfe = "";
	    }
	    return informacaoFiliacaoApresentarDanfe;
	}

	public void setInformacaoFiliacaoApresentarDanfe(String informacaoFiliacaoApresentarDanfe) {
	    this.informacaoFiliacaoApresentarDanfe = informacaoFiliacaoApresentarDanfe;
	}

	public Boolean getIssRetido() {
	    if(issRetido == null){
		issRetido = Boolean.FALSE;
	    }
	    return issRetido;
	}

	public void setIssRetido(Boolean issRetido) {
	    this.issRetido = issRetido;
	}

	@Override
	public String toString() {
		return "NF Saída [" + this.getCodigo() + "]: " + 
                        " Data: " + this.getDataEmissao_Apresentar() + 
                        " Situação: " + this.getSituacao_Apresentar() + 
                        " Modelo: " + this.getModelo() + 
                        " Série: " + this.getSerie() + 
                        " Lote: " + this.getLote() + 
                        " Volume: " + this.getVolume() + 
                        " Valor Total: " + Uteis.getDoubleFormatado(this.getValorTotal()) + 
                        " Valor Total Serviço: " + Uteis.getDoubleFormatado(this.getValorTotalServico()) + 
                        " Responsável: " + this.getResponsavel().getNome() + 
                        " Unidade de Ensino: " + this.getUnidadeEnsinoVO().getNome() + 
                        " Cidade: " + this.getCidade().getNome();
	}

	public NaturezaOperacaoEnum getNaturezaOperacaoEnum() {
		return naturezaOperacaoEnum;
	}

	public void setNaturezaOperacaoEnum(NaturezaOperacaoEnum naturezaOperacaoEnum) {
		this.naturezaOperacaoEnum = naturezaOperacaoEnum;
	}

	public Boolean getIsIncentivadorCultural() {
		if (isIncentivadorCultural == null) {
			isIncentivadorCultural = false;
		}
		return isIncentivadorCultural;
	}

	public void setIsIncentivadorCultural(Boolean isIncentivadorCultural) {
		this.isIncentivadorCultural = isIncentivadorCultural;
	}

	public String getCodigoCNAE() {
		if (codigoCNAE == null) {
			codigoCNAE = "";
		}
		return codigoCNAE;
	}

	public void setCodigoCNAE(String codigoCNAE) {
		this.codigoCNAE = codigoCNAE;
	}

	public Date getDataEmissaoRetroativa() {
		return dataEmissaoRetroativa;
	}

	public void setDataEmissaoRetroativa(Date dataEmissaoRetroativa) {
		this.dataEmissaoRetroativa = dataEmissaoRetroativa;
	}

	public String getNomesConvenios() {
		if (nomesConvenios == null) {
			nomesConvenios = "";
		}
		return nomesConvenios;
	}

	public void setNomesConvenios(String nomesConvenios) {
		this.nomesConvenios = nomesConvenios;
	}
	
	public RegimeEspecialTributacaoEnum getRegimeEspecialTributacaoEnum() {
		return regimeEspecialTributacaoEnum;
	}

	public void setRegimeEspecialTributacaoEnum(RegimeEspecialTributacaoEnum regimeEspecialTributacaoEnum) {
		this.regimeEspecialTributacaoEnum = regimeEspecialTributacaoEnum;
	}

	public Double getPercentualCargaTributaria() {
		if (percentualCargaTributaria == null) {
			percentualCargaTributaria = 0.0;
		}
		return percentualCargaTributaria;
	}

	public void setPercentualCargaTributaria(Double percentualCargaTributaria) {
		this.percentualCargaTributaria = percentualCargaTributaria;
	}

	public Double getValorCargaTributaria() {
		if (valorCargaTributaria == null) {
			valorCargaTributaria = 0.0;
		}
		return valorCargaTributaria;
	}

	public void setValorCargaTributaria(Double valorCargaTributaria) {
		this.valorCargaTributaria = valorCargaTributaria;
	}

	public String getFonteCargaTributaria() {
		if (fonteCargaTributaria == null) {
			fonteCargaTributaria = "";
		}
		return fonteCargaTributaria;
	}

	public void setFonteCargaTributaria(String fonteCargaTributaria) {
		this.fonteCargaTributaria = fonteCargaTributaria;
	}

	public String getJsonEnvio() {
		if(jsonEnvio == null ) {
			jsonEnvio = Constantes.EMPTY;
		}
		return jsonEnvio;
	}

	public void setJsonEnvio(String jsonEnvio) {
		this.jsonEnvio = jsonEnvio;
	}
	public String getJsonRetornoEnvio() {
		if(jsonRetornoEnvio == null) {
			jsonRetornoEnvio =Constantes.EMPTY;
		}
		return jsonRetornoEnvio;
	}

	public void setJsonRetornoEnvio(String jsonRetornoEnvio) {
		this.jsonRetornoEnvio = jsonRetornoEnvio;
	}

	public Boolean getPossuiErroWebservice() {
		if(possuiErroWebservice==null) {
			possuiErroWebservice =Boolean.FALSE;
		}
		return possuiErroWebservice;
	}

	public void setPossuiErroWebservice(Boolean possuiErroWebservice) {
		this.possuiErroWebservice = possuiErroWebservice;
	}
	
}
