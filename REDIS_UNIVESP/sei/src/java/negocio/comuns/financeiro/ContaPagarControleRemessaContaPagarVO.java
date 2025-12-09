package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.enumerador.FinalidadeDocEnum;
import negocio.comuns.financeiro.enumerador.FinalidadePixEnum;
import negocio.comuns.financeiro.enumerador.FinalidadeTedEnum;
import negocio.comuns.financeiro.enumerador.ModalidadeTransferenciaBancariaEnum;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.TipoContaEnum;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoChavePixEnum;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoContribuinte;
import negocio.comuns.financeiro.enumerador.TipoLancamentoContaPagarEnum;
import negocio.comuns.financeiro.enumerador.TipoServicoContaPagarEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.comuns.utilitarias.dominios.TipoSacado;

/**
 *
 * @author Carlos
 */
public class ContaPagarControleRemessaContaPagarVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6659353831211087178L;
	private Integer controleRemessaContaPagar;
	private Integer codigo;
	private SituacaoControleRemessaContaReceberEnum situacaoControleRemessaContaReceber;
	private UsuarioVO usuarioEstorno;
	private Date dataEstorno;
	private String motivoEstorno;
	private String motivoErro;
	private Boolean contaRemetidaComAlteracao;
	private Boolean apresentarArquivoRemessa;
	
	private ContaPagarVO contaPagar;
	private String tipoSacado;
	private Date dataVencimento;
	private Date dataFatoGerador;
	private Double valor;
	private Double juro;
	private Double multa;
	private Double desconto;
	private String nrDocumento;
	private Long nossoNumero;
	private String codigoBarra;
	private String tipoInscricaoFavorecido;
	private String cnpjOuCpfFavorecido;
	private String inscricaoEstadualFavorecido;
	private String inscricaoMunicipalFavorecido;
	private String nomeFavorecido;
	private String emailFavorecido;
	private String complementoFavorecido;
	private String numeroEnderecoFavorecido;
	private String logradouroFavorecido;
	private String bairroFavorecido;
	private String cepFavorecido;
	private String cidadeFavorecido;
	private String estadoFavorecido;
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
    private String codigoAgrupamentoContasPagar;
    private String observacaoAgrupamento;    
    private Integer qtdRegistrosAgrupado;
    private Double vlrTotalRegistrosAgrupado;
    private String vlrTotalRegistrosAgrupadoApresentar;
    private String codigoTransmissaoRemessanossonumero;    
    private String chaveEnderecamentoPix;
	private String identificacaoQRCODE;	
	private FinalidadePixEnum finalidadePixEnum; 
	private TipoIdentificacaoChavePixEnum tipoIdentificacaoChavePixEnum;
	

	@Override
	public ControleRemessaContaReceberVO clone() throws CloneNotSupportedException {
		return (ControleRemessaContaReceberVO) super.clone();
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public SituacaoControleRemessaContaReceberEnum getSituacaoControleRemessaContaReceber() {
		if (situacaoControleRemessaContaReceber == null) {
			situacaoControleRemessaContaReceber = SituacaoControleRemessaContaReceberEnum.AGUARDANDO_PROCESSAMENTO;
		}
		return situacaoControleRemessaContaReceber;
	}

	public void setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum situacaoControleRemessaContaReceber) {
		this.situacaoControleRemessaContaReceber = situacaoControleRemessaContaReceber;
	}

	public UsuarioVO getUsuarioEstorno() {
		if (usuarioEstorno == null) {
			usuarioEstorno = new UsuarioVO();
		}
		return usuarioEstorno;
	}

	public void setUsuarioEstorno(UsuarioVO usuarioEstorno) {
		this.usuarioEstorno = usuarioEstorno;
	}

	public Date getDataEstorno() {
		return dataEstorno;
	}

	public void setDataEstorno(Date dataEstorno) {
		this.dataEstorno = dataEstorno;
	}

	public String getMotivoEstorno() {
		if (motivoEstorno == null) {
			motivoEstorno = "";
		}
		return motivoEstorno;
	}

	public void setMotivoEstorno(String motivoEstorno) {
		this.motivoEstorno = motivoEstorno;
	}

	public Boolean getApresentarBotaoEstorno() {
		if (getSituacaoControleRemessaContaReceber().getValor().equals(SituacaoControleRemessaContaReceberEnum.ESTORNADO.getValor())) {
			return false;
		} else {
			return true;
		}
	}

	public String getMotivoErro() {
		if (motivoErro == null) {
			motivoErro = "";
		}
		return motivoErro;
	}

	public void setMotivoErro(String motivoErro) {
		this.motivoErro = motivoErro;
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

	public ContaPagarVO getContaPagar() {
		if (contaPagar == null) {
			contaPagar = new ContaPagarVO();
		}
		return contaPagar;
	}

	public void setContaPagar(ContaPagarVO contaPagar) {
		this.contaPagar = contaPagar;
	}

	public Integer getControleRemessaContaPagar() {
		if (controleRemessaContaPagar == null) {
			controleRemessaContaPagar = 0;
		}
		return controleRemessaContaPagar;
	}

	public void setControleRemessaContaPagar(Integer controleRemessaContaPagar) {
		this.controleRemessaContaPagar = controleRemessaContaPagar;
	}

	public Boolean getApresentarArquivoRemessa() {
		if (apresentarArquivoRemessa == null) {
			apresentarArquivoRemessa = Boolean.TRUE;
		}
		return apresentarArquivoRemessa;
	}

	public void setApresentarArquivoRemessa(Boolean apresentarArquivoRemessa) {
		this.apresentarArquivoRemessa = apresentarArquivoRemessa;
	}
	
	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataVencimento_Apresentar() {
		return (Uteis.getData(dataVencimento));
	}

	public Date getDataVencimento() {
		return dataVencimento;
	}

	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}

	public Date getDataFatoGerador() {
		return dataFatoGerador;
	}

	public void setDataFatoGerador(Date dataFatoGerador) {
		this.dataFatoGerador = dataFatoGerador;
	}

	public Double getValor() {
		if(valor == null){
			valor = 0.0; 
		}
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}

	public Double getJuro() {
		if(juro == null ) {
			juro = 0.0;
		}
		return juro;
	}

	public void setJuro(Double juro) {
		this.juro = juro;
	}

	public Double getMulta() {
		if(multa == null) {
			multa =0.0;
		}
		return multa;
	}

	public void setMulta(Double multa) {
		this.multa = multa;
	}

	public Double getDesconto() {
		if(desconto == null ) {
			desconto =  0.0;
		}
		return desconto;
	}

	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}

	public String getNrDocumento() {
		if(nrDocumento == null ) {
			nrDocumento =""; 
		}
		return nrDocumento;
	}

	public void setNrDocumento(String nrDocumento) {
		this.nrDocumento = nrDocumento;
	}

	public String getCodigoBarra() {
		if(codigoBarra == null) {
			codigoBarra =""; 
		}
		return codigoBarra;
	}

	public void setCodigoBarra(String codigoBarra) {
		this.codigoBarra = codigoBarra;
	}	

	public BancoVO getBancoRemessaPagar() {
		if(bancoRemessaPagar == null){
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

	public ModalidadeTransferenciaBancariaEnum getModalidadeTransferenciaBancariaEnum() {
		return modalidadeTransferenciaBancariaEnum;
	}

	public void setModalidadeTransferenciaBancariaEnum(ModalidadeTransferenciaBancariaEnum modalidadeTransferenciaBancariaEnum) {
		this.modalidadeTransferenciaBancariaEnum = modalidadeTransferenciaBancariaEnum;
	}

	public TipoContaEnum getTipoContaEnum() {
		if (tipoContaEnum == null) {
			tipoContaEnum = TipoContaEnum.CREDITO_EM_CONTA_CORRENTE;
		}
		return tipoContaEnum;
	}
	
	public void setTipoContaEnum(TipoContaEnum tipoContaEnum) {
		this.tipoContaEnum = tipoContaEnum;
	}
	
	public String getNumeroAgenciaRecebimento() {
		return numeroAgenciaRecebimento;
	}

	public void setNumeroAgenciaRecebimento(String numeroAgenciaRecebimento) {
		this.numeroAgenciaRecebimento = numeroAgenciaRecebimento;
	}

	public String getDigitoAgenciaRecebimento() {
		return digitoAgenciaRecebimento;
	}

	public void setDigitoAgenciaRecebimento(String digitoAgenciaRecebimento) {
		this.digitoAgenciaRecebimento = digitoAgenciaRecebimento;
	}

	public String getContaCorrenteRecebimento() {
		return contaCorrenteRecebimento;
	}

	public void setContaCorrenteRecebimento(String contaCorrenteRecebimento) {
		this.contaCorrenteRecebimento = contaCorrenteRecebimento;
	}

	public String getDigitoCorrenteRecebimento() {
		return digitoCorrenteRecebimento;
	}

	public void setDigitoCorrenteRecebimento(String digitoCorrenteRecebimento) {
		this.digitoCorrenteRecebimento = digitoCorrenteRecebimento;
	}

	public String getCodigoReceitaTributo() {
		return codigoReceitaTributo;
	}

	public void setCodigoReceitaTributo(String codigoReceitaTributo) {
		this.codigoReceitaTributo = codigoReceitaTributo;
	}

	public String getIdentificacaoContribuinte() {
		return identificacaoContribuinte;
	}

	public void setIdentificacaoContribuinte(String identificacaoContribuinte) {
		this.identificacaoContribuinte = identificacaoContribuinte;
	}

	public TipoIdentificacaoContribuinte getTipoIdentificacaoContribuinte() {
		return tipoIdentificacaoContribuinte;
	}

	public void setTipoIdentificacaoContribuinte(TipoIdentificacaoContribuinte tipoIdentificacaoContribuinte) {
		this.tipoIdentificacaoContribuinte = tipoIdentificacaoContribuinte;
	}

	public String getNumeroReferencia() {
		if(numeroReferencia == null) {
			numeroReferencia ="";
		}
		return numeroReferencia;
	}

	public void setNumeroReferencia(String numeroReferencia) {
		this.numeroReferencia = numeroReferencia;
	}

	public Double getValorReceitaBrutaAcumulada() {
		if(valorReceitaBrutaAcumulada == null ) {
			valorReceitaBrutaAcumulada = 0.0;
		}
		return valorReceitaBrutaAcumulada;
	}

	public void setValorReceitaBrutaAcumulada(Double valorReceitaBrutaAcumulada) {
		this.valorReceitaBrutaAcumulada = valorReceitaBrutaAcumulada;
	}

	public Double getPercentualReceitaBrutaAcumulada() {
		if(percentualReceitaBrutaAcumulada == null) {
			percentualReceitaBrutaAcumulada =0.0;
		}
		return percentualReceitaBrutaAcumulada;
	}

	public void setPercentualReceitaBrutaAcumulada(Double percentualReceitaBrutaAcumulada) {
		this.percentualReceitaBrutaAcumulada = percentualReceitaBrutaAcumulada;
	}

	public Long getNossoNumero() {
		if(nossoNumero == null ) {
			nossoNumero = 0L;
		}
		return nossoNumero;
	}

	public void setNossoNumero(Long nossoNumero) {
		this.nossoNumero = nossoNumero;
	}

	public String getTipoSacado() {
		if(tipoSacado == null){
			tipoSacado = "";
		}
		return tipoSacado;
	}

	public void setTipoSacado(String tipoSacado) {
		this.tipoSacado = tipoSacado;
	}

	public String getTipoInscricaoFavorecido() {
		if(tipoInscricaoFavorecido ==null) {
			tipoInscricaoFavorecido ="";
		}
		return tipoInscricaoFavorecido;
	}

	public void setTipoInscricaoFavorecido(String tipoInscricaoFavorecido) {
		this.tipoInscricaoFavorecido = tipoInscricaoFavorecido;
	}

	public String getCnpjOuCpfFavorecido() {
		if(cnpjOuCpfFavorecido == null ) {
			cnpjOuCpfFavorecido ="";
		}
		return cnpjOuCpfFavorecido;
	}

	public void setCnpjOuCpfFavorecido(String cnpjOuCpfFavorecido) {
		this.cnpjOuCpfFavorecido = cnpjOuCpfFavorecido;
	}

	public String getInscricaoEstadualFavorecido() {
		if(inscricaoEstadualFavorecido == null ) {
			inscricaoEstadualFavorecido ="";
		}
		return inscricaoEstadualFavorecido;
	}

	public void setInscricaoEstadualFavorecido(String inscricaoEstadualFavorecido) {
		this.inscricaoEstadualFavorecido = inscricaoEstadualFavorecido;
	}

	public String getInscricaoMunicipalFavorecido() {
		if(inscricaoMunicipalFavorecido == null ) {
			inscricaoMunicipalFavorecido ="";
		}
		return inscricaoMunicipalFavorecido;
	}

	public void setInscricaoMunicipalFavorecido(String inscricaoMunicipalFavorecido) {
		this.inscricaoMunicipalFavorecido = inscricaoMunicipalFavorecido;
	}

	public String getNomeFavorecido() {
		if(nomeFavorecido == null ) {
			nomeFavorecido ="";
		}
		return nomeFavorecido;
	}

	public void setNomeFavorecido(String nomeFavorecido) {
		this.nomeFavorecido = nomeFavorecido;
	}

	public String getEmailFavorecido() {
		if(emailFavorecido == null ) {
			emailFavorecido ="" ;
		}
		return emailFavorecido;
	}

	public void setEmailFavorecido(String emailFavorecido) {
		this.emailFavorecido = emailFavorecido;
	}

	public String getComplementoFavorecido() {
		if(complementoFavorecido == null) {
			complementoFavorecido =""; 
		}
		return complementoFavorecido;
	}

	public void setComplementoFavorecido(String complementoFavorecido) {
		this.complementoFavorecido = complementoFavorecido;
	}

	public String getNumeroEnderecoFavorecido() {
		if(numeroEnderecoFavorecido ==null) {
			numeroEnderecoFavorecido ="";
		}
		return numeroEnderecoFavorecido;
	}

	public void setNumeroEnderecoFavorecido(String numeroEnderecoFavorecido) {
		this.numeroEnderecoFavorecido = numeroEnderecoFavorecido;
	}

	public String getLogradouroFavorecido() {
		if(logradouroFavorecido ==null ) {
			logradouroFavorecido ="";
		}
		return logradouroFavorecido;
	}

	public void setLogradouroFavorecido(String logradouroFavorecido) {
		this.logradouroFavorecido = logradouroFavorecido;
	}

	public String getBairroFavorecido() {
		if(bairroFavorecido == null ) {
			bairroFavorecido ="";
		}
		return bairroFavorecido;
	}

	public void setBairroFavorecido(String bairroFavorecido) {
		this.bairroFavorecido = bairroFavorecido;
	}

	public String getCepFavorecido() {
		if(cepFavorecido == null) {
			cepFavorecido =""; 
		}
		return cepFavorecido;
	}

	public void setCepFavorecido(String cepFavorecido) {
		this.cepFavorecido = cepFavorecido;
	}

	public String getCidadeFavorecido() {
		if(cidadeFavorecido == null ) {
			cidadeFavorecido ="";
		}
		return cidadeFavorecido;
	}

	public void setCidadeFavorecido(String cidadeFavorecido) {
		this.cidadeFavorecido = cidadeFavorecido;
	}

	public String getEstadoFavorecido() {
		if(estadoFavorecido == null ) {
			estadoFavorecido ="";
		}
		return estadoFavorecido;
	}

	public void setEstadoFavorecido(String estadoFavorecido) {
		this.estadoFavorecido = estadoFavorecido;
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

	public boolean isTipoSacadoFuncionario() {
		return getTipoSacado().equals(TipoSacado.FUNCIONARIO_PROFESSOR.getValor());
	}

	public boolean isTipoSacadoAluno() {
		return getTipoSacado().equals(TipoSacado.ALUNO.getValor());
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
	
	public boolean isApresentarDadosContaBancaria() {
		return (getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum() != null && (getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.DEBITO_EM_CONTA_CORRENTE)
				|| (getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.DEPOSITO) && !Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()))))
				|| (Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()) && (getTipoLancamentoContaPagar().isCreditoContaCorrente() 
				|| getTipoLancamentoContaPagar().isCreditoContaPoupanca() 
				|| getTipoLancamentoContaPagar().isTransferencia()));
	}
	
	public boolean isApresentarModalidadeFinalidadeTed() {
		return Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()) &&	getTipoLancamentoContaPagar().isTransferencia();
	}
	
	
	
	
	public boolean isApresentarTipoConta() {
		return Uteis.isAtributoPreenchido(getBancoRemessaPagar().getCodigo()) &&
				Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()) &&
				(getTipoLancamentoContaPagar() == TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE 
				|| getTipoLancamentoContaPagar() == TipoLancamentoContaPagarEnum.CREDITO_CONTA_POUPANCA
				|| getTipoLancamentoContaPagar() == TipoLancamentoContaPagarEnum.CREDITO_CONTA_CORRENTE_MESMA_TITULARIDADE
				|| getTipoLancamentoContaPagar() ==  TipoLancamentoContaPagarEnum.TRANSFERENCIA_OUTRO_BANCO
				|| getTipoLancamentoContaPagar() ==  TipoLancamentoContaPagarEnum.TED_OUTRA_TITULARIDADE		
		        || getTipoLancamentoContaPagar() == TipoLancamentoContaPagarEnum.TED_MESMA_TITULARIDADE);
	}

	public boolean isApresentarDadosTituloBancaria() {
		return (getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum() != null && getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.BOLETO_BANCARIO) && !Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar())) ||				
				(Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()) &&
				(getTipoLancamentoContaPagar().isLiquidacaoTituloCarteiraCobrancaSantander()
						|| getTipoLancamentoContaPagar().isLiquidacaoTituloCarteiraCobrancaSicoob()
						|| getTipoLancamentoContaPagar().isLiquidacaoTituloOutroBanco()));
	}
	
	public boolean isApresentarCodigoBarra() {
		return (getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum() != null && getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.BOLETO_BANCARIO) && !Uteis.isAtributoPreenchido(getContaPagar().getTipoLancamentoContaPagar())) ||
				(Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()) && 	
				(getTipoLancamentoContaPagar() == (TipoLancamentoContaPagarEnum.PAGAMENTO_CONTAS_TRIBUTOS_COM_CODIGO_BARRA) 				
				 || getTipoLancamentoContaPagar() == (TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_OUTRO_BANCO)				
				 || getTipoLancamentoContaPagar() == (TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_PROPRIO_BANCO)
				 || getTipoLancamentoContaPagar() == (TipoLancamentoContaPagarEnum.PAGAMENTO_CONTAS_CONCES_TRIBUTOS_COM_CODIGO_BARRA)));
	}
	
	public boolean isApresentarDadosPagamentoContasTributosComCodigoBarra() {
		return Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()) && (getTipoLancamentoContaPagar().isPagamentoContasTributosComCodigoBarra());
	}
	
	public boolean isApresentarDadosTributos(){
		if(Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()) &&
	        (getTipoLancamentoContaPagar().isGpsSemCodigoBarra()
	        || getTipoLancamentoContaPagar().isDarfNormalSemCodigoBarra()
 			|| getTipoLancamentoContaPagar().isDarfSimplesSemCodigoBarra()
 			|| getTipoLancamentoContaPagar().isGareDrSemCodigoBarra()
 			|| getTipoLancamentoContaPagar().isGareIcmsSemCodigoBarra()
 			|| getTipoLancamentoContaPagar().isGareItcmdSemCodigoBarra())){	
			return true;
		}
		return false;
	}

	public boolean isPermiteSelecionarModalidadeFinalidade() {
		return Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()) 
				&& (getTipoLancamentoContaPagar().equals(TipoLancamentoContaPagarEnum.TRANSFERENCIA_OUTRO_BANCO) 					
						|| getTipoLancamentoContaPagar().equals(TipoLancamentoContaPagarEnum.ITAU_TRANSFERENCIA_DOC_D)
						|| getTipoLancamentoContaPagar().equals(TipoLancamentoContaPagarEnum.PIXTRANSFERÊNCIA));
	}
	public String getCodigoAgrupamentoContasPagar() {
		if(codigoAgrupamentoContasPagar == null ) {
			codigoAgrupamentoContasPagar = "" ;
		}
		return codigoAgrupamentoContasPagar;
	}

	public void setCodigoAgrupamentoContasPagar(String codigoAgrupamentoContasPagar) {
		this.codigoAgrupamentoContasPagar = codigoAgrupamentoContasPagar;
	}

		
	public String getObservacaoAgrupamento() {
		if(observacaoAgrupamento == null ) {
			observacaoAgrupamento = "";
		}
		return observacaoAgrupamento;
	}

	public void setObservacaoAgrupamento(String observacaoAgrupamento) {
		this.observacaoAgrupamento = observacaoAgrupamento;
	}

	
	public boolean isTipoLancamentoBancoSicoob() {
		return Uteis.isAtributoPreenchido(getTipoLancamentoContaPagar()) 
				&& this.getBancoRemessaPagar().getNrBanco().equals("756")
				&& (getTipoLancamentoContaPagar() == TipoLancamentoContaPagarEnum.TED_OUTRA_TITULARIDADE	|| getTipoLancamentoContaPagar() == TipoLancamentoContaPagarEnum.TRANSFERENCIA_OUTRO_BANCO);
	}
	

	public Integer getQtdRegistrosAgrupado() {
		if (qtdRegistrosAgrupado == null) {
			qtdRegistrosAgrupado = 0;
		}
		return qtdRegistrosAgrupado;
	}
	
	public void setQtdRegistrosAgrupados(Integer qtdRegistrosAgrupado) {
		this.qtdRegistrosAgrupado = qtdRegistrosAgrupado;
	}
	
	public Double getVlrTotalRegistrosAgrupado() {   
		if (vlrTotalRegistrosAgrupado == null) {
			vlrTotalRegistrosAgrupado = 0.0;
		}
		return vlrTotalRegistrosAgrupado; 
	}
	
	public void setVlrTotalRegistrosAgrupado(Double vlrTotalRegistrosAgrupado) {
		this.vlrTotalRegistrosAgrupado = vlrTotalRegistrosAgrupado;
	}

	public String getVlrTotalRegistrosAgrupadoApresentar() {
		if (vlrTotalRegistrosAgrupadoApresentar == null) {
			vlrTotalRegistrosAgrupadoApresentar = "";
		}
		return vlrTotalRegistrosAgrupadoApresentar;
	}

	public void preencherVlrTotalRegistrosAgrupadoApresentar() {
		this.vlrTotalRegistrosAgrupadoApresentar = Uteis.getDoubleFormatado(getVlrTotalRegistrosAgrupado());
	}
	
	
	public Double getPrevisaoValorPagoDescontosMultas() {
		return Uteis.arrendondarForcando2CadasDecimais(getValor() + getJuro() + getMulta() - getDesconto() );
	}


	public String getCodigoTransmissaoRemessanossonumero() {
		if(codigoTransmissaoRemessanossonumero == null ) {
			codigoTransmissaoRemessanossonumero ="";
		}
		return codigoTransmissaoRemessanossonumero;
	}

	public void setCodigoTransmissaoRemessanossonumero(String codigoTransmissaoRemessanossonumero) {
		this.codigoTransmissaoRemessanossonumero = codigoTransmissaoRemessanossonumero;
	}
	
	public String getChaveEnderecamentoPix() {
		if(chaveEnderecamentoPix == null ) {
			chaveEnderecamentoPix = "" ;
		}
		return chaveEnderecamentoPix;
	}
	
	public void setChaveEnderecamentoPix(String chaveEnderecamentoPix) {
		this.chaveEnderecamentoPix = chaveEnderecamentoPix ;
	}



	public String getIdentificacaoQRCODE() {
		if(identificacaoQRCODE == null ) {
			identificacaoQRCODE ="" ;	
		}
		return identificacaoQRCODE;
	}
	public void setIdentificacaoQRCODE(String identificacaoQRCODE) {
		this.identificacaoQRCODE = identificacaoQRCODE ;
	}

	public FinalidadePixEnum getFinalidadePixEnum() {
		return finalidadePixEnum;
	}

	public void setFinalidadePixEnum(FinalidadePixEnum finalidadePixEnum) {
		this.finalidadePixEnum = finalidadePixEnum;
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
}
