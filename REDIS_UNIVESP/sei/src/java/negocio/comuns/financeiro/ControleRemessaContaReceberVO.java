package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaContaReceberEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
/**
 *
 * @author Carlos
 */
public class ControleRemessaContaReceberVO extends SuperVO {
	private UnidadeEnsinoVO unidadeEnsino;
    private Integer numeroInscricaoEmpresa;
    private String cnpj;
    private String agencia;
    private String digitoAgencia;	
    private String contaCorrente;
    private String digitoContaCorrente;	
    //DAC significa digito de auto conferência da conta
    private Integer dac;
    private String nossoNumero;
    private String carteira;
    private String codigocarteira;
    private String nrDocumento;
    private Date dataVencimento;
    private Date dataEmissao;
    private Double valor;
    private Double acrescimo;
    private Double valorDescontoAluno;
    private Double valorDescontoRateio;
    private Double valorBase;
    private String especieTitulo;
    private Double juro;
    private Date dataLimiteConcessaoDesconto;
    private Double valorDescontoDataLimite;
    private Date dataLimiteConcessaoDesconto2;
    private Double valorDescontoDataLimite2;
    private Date dataLimiteConcessaoDesconto3;
    private Double valorDescontoDataLimite3;
    private Double valorDesconto;
    //01=CPF  -  02=CNPJ    - codigoInscricao
    private Integer codigoInscricao;
    //CPF OU CNPJ
    private String numeroInscricao;
    private String nomeSacado;
    private String emailSacado;	
    private String logradouro;
    private String complementoLogradouro;
    private String nrLogradouro;
    private String bairro;
    private String cep;
    private String cidade;
    private String estado;
    private String avalista;
    private Integer contaReceber;
    private String tipoOrigem;
    private String tipoDesconto;
    private Boolean apresentarArquivoRemessa;
    private List<OrdemDescontoVO> listaOrdemDescontoVOs;
    private List<PlanoDescontoContaReceberVO> planoDescontoContaReceberVOs;
    private Boolean usaDescontoCompostoPlanoDesconto;
    private Integer descontoprogressivo_codigo;
    private Integer descontoprogressivo_dialimite1;
    private Integer descontoprogressivo_dialimite2;
    private Integer descontoprogressivo_dialimite3;
    private Integer descontoprogressivo_dialimite4;
    private double descontoprogressivo_percdescontolimite1;
    private double descontoprogressivo_percdescontolimite2;
    private double descontoprogressivo_percdescontolimite3;
    private double descontoprogressivo_percdescontolimite4;
    private double descontoprogressivo_valordescontolimite1;
    private double descontoprogressivo_valordescontolimite2;
    private double descontoprogressivo_valordescontolimite3;
    private double descontoprogressivo_valordescontolimite4;
    private Boolean descontoprogressivo_utilizaDiaUtil;
    private Boolean descontoprogressivo_utilizaDiaFixo;
    private Boolean contareceber_contaEditadaManualmente;
    private Integer contareceber_diaLimite1;
	private Integer contareceber_diaLimite2;
	private Integer contareceber_diaLimite3;
	private Integer contareceber_diaLimite4;
	private Boolean contareceber_utilizarDescontoProgressivoManual;
    private Integer codigo;
	private String codMovRemessa;
	private Boolean utilizaCobrancaPartilhada;
	private String codigoReceptor1;
	private String codigoReceptor2;
	private String codigoReceptor3;
	private String codigoReceptor4;
	private Boolean descontoValidoAteDataParcela;
	private SituacaoControleRemessaContaReceberEnum situacaoControleRemessaContaReceber;
	private UsuarioVO usuarioEstorno;
	private Date dataEstorno;
	private String motivoEstorno;	
	private String motivoErro;	
	private Boolean contaRemetidaComAlteracao;		
	private Boolean mensagemEnviada;			
	private Boolean contaRemetidaComAlteracao_valorBase;
	private Boolean contaRemetidaComAlteracao_valorComAbatimentoAdicionadoOuModificado;
	private Boolean contaRemetidaComAlteracao_valorComAbatimentoRemovido;
	private Boolean contaRemetidaComAlteracao_valorDescProgressivo;
	private Boolean contaRemetidaComAlteracao_dataDescProgressivo;
	private Boolean contaRemetidaComAlteracao_dataVencimento;
	private Integer posicaoNoArquivoRemessa;
	private Boolean parcelaDeveReceberReajustePreco;
	private Boolean baixarConta;
	private Double valorAbatimento;
	private String situacaoConta;
	private String codigoMotivoEstorno;
	private String telefoneSacado;
	private Boolean descontoDataLimite2Aplicado;
	private Boolean descontoDataLimite3Aplicado;
	private Boolean parceiroPermiteIsentarMulta;
	private Double juroPorcentagem;
	private Double multaPorcentagem;

	
    @Override
    public ControleRemessaContaReceberVO clone() throws CloneNotSupportedException {        
        return (ControleRemessaContaReceberVO)super.clone();
    }
	
    public Integer getNumeroInscricaoEmpresa() {
        if (numeroInscricaoEmpresa == null) {
            numeroInscricaoEmpresa = 0;
        }
        return numeroInscricaoEmpresa;
    }

    public void setNumeroInscricaoEmpresa(Integer numeroInscricaoEmpresa) {
        this.numeroInscricaoEmpresa = numeroInscricaoEmpresa;
    }

    public String getCnpj() {
        if (cnpj == null) {
            cnpj = "";
        }
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getAgencia() {
        if (agencia == null) {
            agencia = "";
        }
        return agencia;
    }

    public void setAgencia(String agencia) {
        this.agencia = agencia;
    }

    public String getContaCorrente() {
        if (contaCorrente == null) {
            contaCorrente = "";
        }
        return contaCorrente;
    }

    public void setContaCorrente(String contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    public Integer getDac() {
        if (dac == null) {
            dac = 0;
        }
        return dac;
    }

    public void setDac(Integer dac) {
        this.dac = dac;
    }

    public String getNossoNumero() {
        if (nossoNumero == null) {
            nossoNumero = "";
        }
        return nossoNumero;
    }

    public void setNossoNumero(String nossoNumero) {
        this.nossoNumero = nossoNumero;
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

    public String getNrDocumento() {
        if (nrDocumento == null) {
            nrDocumento = "";
        }
        return nrDocumento;
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public String getDataVencimento_Apresentar() {
        return Uteis.getData(getDataVencimento());
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    public Double getValor() {
        if (valor == null) {
            valor = 0.0;
        }
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public String getEspecieTitulo() {
        if (especieTitulo == null) {
            especieTitulo = "";
        }
        return especieTitulo;
    }

    public void setEspecieTitulo(String especieTitulo) {
        this.especieTitulo = especieTitulo;
    }

    public Double getJuro() {
        if (juro == null) {
            juro = 0.0;
        }
        return juro;
    }

    public void setJuro(Double juro) {
        this.juro = juro;
    }

    public Double getValorDesconto() {
        if (valorDesconto == null) {
            valorDesconto = 0.0;
        }
        return valorDesconto;
    }

    public void setValorDesconto(Double valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public Integer getCodigoInscricao() {
        if (codigoInscricao == null) {
            codigoInscricao = 0;
        }
        return codigoInscricao;
    }

    public void setCodigoInscricao(Integer codigoInscricao) {
        this.codigoInscricao = codigoInscricao;
    }

    public String getNumeroInscricao() {
        if (numeroInscricao == null) {
            numeroInscricao = "";
        }
        return numeroInscricao;
    }

    public void setNumeroInscricao(String numeroInscricao) {
        this.numeroInscricao = numeroInscricao;
    }

    public String getNomeSacado() {
        if (nomeSacado == null) {
            nomeSacado = "";
        }
        return nomeSacado;
    }

    public void setNomeSacado(String nomeSacado) {
        this.nomeSacado = nomeSacado;
    }

    public String getLogradouro() {
        if (logradouro == null) {
            logradouro = "";
        }
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getBairro() {
        if (bairro == null) {
            bairro = "";
        }
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        if (cep == null) {
            cep = "";
        }
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getCidade() {
        if (cidade == null) {
            cidade = "";
        }
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        if (estado == null) {
            estado = "";
        }
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getAvalista() {
        if (avalista == null) {
            avalista = "";
        }
        return avalista;
    }

    public void setAvalista(String avalista) {
        this.avalista = avalista;
    }

    public String getCodigocarteira() {
        if (codigocarteira == null) {
            codigocarteira = "";
        }
        return codigocarteira;
    }

    public void setCodigocarteira(String codigocarteira) {
        this.codigocarteira = codigocarteira;
    }

    public Integer getContaReceber() {
        if (contaReceber == null) {
            contaReceber = 0;
        }
        return contaReceber;
    }

    public void setContaReceber(Integer contaReceber) {
        this.contaReceber = contaReceber;
    }

    public String getTipoOrigem() {
        if (tipoOrigem == null) {
            tipoOrigem = "";
        }
        return tipoOrigem;
    }

    public void setTipoOrigem(String tipoOrigem) {
        this.tipoOrigem = tipoOrigem;
    }

    public String getTipoDesconto() {
        if (tipoDesconto == null) {
            tipoDesconto = "";
        }
        return tipoDesconto;
    }

    public void setTipoDesconto(String tipoDesconto) {
        this.tipoDesconto = tipoDesconto;
    }

    public List<OrdemDescontoVO> getListaOrdemDescontoVOs() {
        if (listaOrdemDescontoVOs == null) {
            listaOrdemDescontoVOs = new ArrayList(0);
        }
        return listaOrdemDescontoVOs;
    }

    public void setListaOrdemDescontoVOs(List<OrdemDescontoVO> listaOrdemDescontoVOs) {
        this.listaOrdemDescontoVOs = listaOrdemDescontoVOs;
    }

    public List<PlanoDescontoContaReceberVO> getPlanoDescontoContaReceberVOs() {
        if (planoDescontoContaReceberVOs == null) {
            planoDescontoContaReceberVOs = new ArrayList(0);
        }
        return planoDescontoContaReceberVOs;
    }

    public void setPlanoDescontoContaReceberVOs(List<PlanoDescontoContaReceberVO> planoDescontoContaReceberVOs) {
        this.planoDescontoContaReceberVOs = planoDescontoContaReceberVOs;
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

    public Integer getDescontoprogressivo_codigo() {
    	if (descontoprogressivo_codigo == null) {
    		descontoprogressivo_codigo = 0;
    	}
		return descontoprogressivo_codigo;
    }

    public void setDescontoprogressivo_codigo(Integer descontoprogressivo_codigo) {
        this.descontoprogressivo_codigo = descontoprogressivo_codigo;
    }

    public Integer getDescontoprogressivo_dialimite1() {
    	if (descontoprogressivo_dialimite1 == null) {
    		descontoprogressivo_dialimite1 = 0;
    	}
        return descontoprogressivo_dialimite1;
    }

    public void setDescontoprogressivo_dialimite1(Integer descontoprogressivo_dialimite1) {
        this.descontoprogressivo_dialimite1 = descontoprogressivo_dialimite1;
    }

    public Integer getDescontoprogressivo_dialimite2() {
    	if (descontoprogressivo_dialimite2 == null) {
    		descontoprogressivo_dialimite2 = 0;
    	}
    	return descontoprogressivo_dialimite2;
    }

    public void setDescontoprogressivo_dialimite2(Integer descontoprogressivo_dialimite2) {
        this.descontoprogressivo_dialimite2 = descontoprogressivo_dialimite2;
    }

    public Integer getDescontoprogressivo_dialimite3() {
    	if (descontoprogressivo_dialimite3 == null) {
    		descontoprogressivo_dialimite3 = 0;
    	}    	
		return descontoprogressivo_dialimite3;
    }

    public void setDescontoprogressivo_dialimite3(Integer descontoprogressivo_dialimite3) {
        this.descontoprogressivo_dialimite3 = descontoprogressivo_dialimite3;
    }

    public Integer getDescontoprogressivo_dialimite4() {
    	if (descontoprogressivo_dialimite4 == null) {
    		descontoprogressivo_dialimite4 = 0;
    	}    	
        return descontoprogressivo_dialimite4;
    }

    public void setDescontoprogressivo_dialimite4(Integer descontoprogressivo_dialimite4) {
        this.descontoprogressivo_dialimite4 = descontoprogressivo_dialimite4;
    }

    public double getDescontoprogressivo_percdescontolimite1() {
        return descontoprogressivo_percdescontolimite1;
    }

    public void setDescontoprogressivo_percdescontolimite1(double descontoprogressivo_percdescontolimite1) {
        this.descontoprogressivo_percdescontolimite1 = descontoprogressivo_percdescontolimite1;
    }

    public double getDescontoprogressivo_percdescontolimite2() {
        return descontoprogressivo_percdescontolimite2;
    }

    public void setDescontoprogressivo_percdescontolimite2(double descontoprogressivo_percdescontolimite2) {
        this.descontoprogressivo_percdescontolimite2 = descontoprogressivo_percdescontolimite2;
    }

    public double getDescontoprogressivo_percdescontolimite3() {
        return descontoprogressivo_percdescontolimite3;
    }

    public void setDescontoprogressivo_percdescontolimite3(double descontoprogressivo_percdescontolimite3) {
        this.descontoprogressivo_percdescontolimite3 = descontoprogressivo_percdescontolimite3;
    }

    public double getDescontoprogressivo_percdescontolimite4() {
        return descontoprogressivo_percdescontolimite4;
    }

    public void setDescontoprogressivo_percdescontolimite4(double descontoprogressivo_percdescontolimite4) {
        this.descontoprogressivo_percdescontolimite4 = descontoprogressivo_percdescontolimite4;
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

    public Date getDataLimiteConcessaoDesconto() {
        return dataLimiteConcessaoDesconto;
    }
    
    public String getDataLimiteConcessaoDesconto_Apresentar() {
        return Uteis.getData(getDataLimiteConcessaoDesconto());
    }

    public void setDataLimiteConcessaoDesconto(Date dataLimiteConcessaoDesconto) {
        this.dataLimiteConcessaoDesconto = dataLimiteConcessaoDesconto;
    }

    public Double getValorBase() {
        if (valorBase == null) {
            valorBase = 0.0;
        }
        return valorBase;
    }

    public void setValorBase(Double valorBase) {
        this.valorBase = valorBase;
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

    public Double getValorDescontoAluno() {
        if (valorDescontoAluno == null) {
            valorDescontoAluno = 0.0;
        }
        return valorDescontoAluno;
    }

    public void setValorDescontoAluno(Double valorDescontoAluno) {
        this.valorDescontoAluno = valorDescontoAluno;
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

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

	public String getDigitoContaCorrente() {
		if (digitoContaCorrente == null) {
			digitoContaCorrente = "";
		}
		return digitoContaCorrente;
	}

	public void setDigitoContaCorrente(String digitoContaCorrente) {
		this.digitoContaCorrente = digitoContaCorrente;
	}

	public String getDigitoAgencia() {
		if (digitoAgencia == null) {
			digitoAgencia = "";
		}
		return digitoAgencia;
	}

	public void setDigitoAgencia(String digitoAgencia) {
		this.digitoAgencia = digitoAgencia;
	}

	/**
	 * @return the unidadeEnsino
	 */
	public UnidadeEnsinoVO getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = new UnidadeEnsinoVO();
		}
		return unidadeEnsino;
	}

	/**
	 * @param unidadeEnsino the unidadeEnsino to set
	 */
	public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}
	
	public String getCodMovRemessa() {
		if (codMovRemessa == null) {
			codMovRemessa = "01";
		}
		return codMovRemessa;
	}

	public void setCodMovRemessa(String codMovRemessa) {
		this.codMovRemessa = codMovRemessa;
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

	public Boolean getDescontoValidoAteDataParcela() {
		if (descontoValidoAteDataParcela == null) {
			descontoValidoAteDataParcela = Boolean.FALSE;
		}
		return descontoValidoAteDataParcela;
	}

	public void setDescontoValidoAteDataParcela(Boolean descontoValidoAteDataParcela) {
		this.descontoValidoAteDataParcela = descontoValidoAteDataParcela;
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

	public Double getValorDescontoRateio() {
		if(valorDescontoRateio == null){
			valorDescontoRateio = 0.0;
		}
		return valorDescontoRateio;
	}

	public void setValorDescontoRateio(Double valorDescontoRateio) {
		this.valorDescontoRateio = valorDescontoRateio;
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
	
	public String getEmailSacado() {
		if (emailSacado == null) {
			emailSacado = "";
		}
		return emailSacado;
	}

	public void setEmailSacado(String emailSacado) {
		this.emailSacado = emailSacado;
	}

	public Boolean getMensagemEnviada() {
		if (mensagemEnviada == null) {
			mensagemEnviada = Boolean.FALSE;
		}
		return mensagemEnviada;
	}

	public void setMensagemEnviada(Boolean mensagemEnviada) {
		this.mensagemEnviada = mensagemEnviada;
	}
	

	public Double getAcrescimo() {
		if(acrescimo == null){
			acrescimo = 0.0;
		}
		return acrescimo;
	}

	public void setAcrescimo(Double acrescimo) {
		this.acrescimo = acrescimo;
	}

	public Double valorComAcrescimo;
	
	public Double getValorComAcrescimo(){
		if(valorComAcrescimo == null){
			valorComAcrescimo = Uteis.arrendondarForcando2CadasDecimais(getValor()+getAcrescimo());
		}
		return valorComAcrescimo;
	}

	public void setValorComAcrescimo(Double valorComAcrescimo) {
		this.valorComAcrescimo = valorComAcrescimo;
	}
	
	private Double valorBaseComAcrescimo;
	
	public Double getValorBaseComAcrescimo(){
		if(valorBaseComAcrescimo == null){
			valorBaseComAcrescimo = Uteis.arrendondarForcando2CadasDecimais(getValorBase()+getAcrescimo());
		}
		return valorBaseComAcrescimo;
	}
		

	public void setValorBaseComAcrescimo(Double valorBaseComAcrescimo) {
		this.valorBaseComAcrescimo = valorBaseComAcrescimo;
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
	
	public Integer getPosicaoNoArquivoRemessa() {
		if (posicaoNoArquivoRemessa == null) {
			posicaoNoArquivoRemessa = 0;
		}
		return posicaoNoArquivoRemessa;
	}

	public void setPosicaoNoArquivoRemessa(Integer posicaoNoArquivoRemessa) {
		this.posicaoNoArquivoRemessa = posicaoNoArquivoRemessa;
	}

	public Date getDataLimiteConcessaoDesconto2() {
		return dataLimiteConcessaoDesconto2;
	}

	public void setDataLimiteConcessaoDesconto2(Date dataLimiteConcessaoDesconto2) {
		this.dataLimiteConcessaoDesconto2 = dataLimiteConcessaoDesconto2;
	}

	public Double getValorDescontoDataLimite2() {
		if (valorDescontoDataLimite2 == null) {
            valorDescontoDataLimite2 = 0.0;
        }		
		return valorDescontoDataLimite2;
	}

	public void setValorDescontoDataLimite2(Double valorDescontoDataLimite2) {
		this.valorDescontoDataLimite2 = valorDescontoDataLimite2;
	}

	public Date getDataLimiteConcessaoDesconto3() {
		return dataLimiteConcessaoDesconto3;
	}

	public void setDataLimiteConcessaoDesconto3(Date dataLimiteConcessaoDesconto3) {
		this.dataLimiteConcessaoDesconto3 = dataLimiteConcessaoDesconto3;
	}

	public Double getValorDescontoDataLimite3() {
		if (valorDescontoDataLimite3 == null) {
            valorDescontoDataLimite3 = 0.0;
        }
		return valorDescontoDataLimite3;
	}

	public void setValorDescontoDataLimite3(Double valorDescontoDataLimite3) {
		this.valorDescontoDataLimite3 = valorDescontoDataLimite3;
	}

    public String getDataLimiteConcessaoDesconto2_Apresentar() {
        return Uteis.getData(getDataLimiteConcessaoDesconto2());
    }

    public String getDataLimiteConcessaoDesconto3_Apresentar() {
        return Uteis.getData(getDataLimiteConcessaoDesconto3());
    }

	public Date getDataEmissao() {
		return dataEmissao;
	}

	public void setDataEmissao(Date dataEmissao) {
		this.dataEmissao = dataEmissao;
	}

	public Boolean getParcelaDeveReceberReajustePreco() {
		if (parcelaDeveReceberReajustePreco == null) {
			parcelaDeveReceberReajustePreco = false;
		}
		return parcelaDeveReceberReajustePreco;
	}

	public void setParcelaDeveReceberReajustePreco(Boolean parcelaDeveReceberReajustePreco) {
		this.parcelaDeveReceberReajustePreco = parcelaDeveReceberReajustePreco;
	}

	public Double getValorAbatimento() {
		if (valorAbatimento == null) {
			valorAbatimento = 0.0;
		}
		return valorAbatimento;
	}

	public void setValorAbatimento(Double valorAbatimento) {
		this.valorAbatimento = valorAbatimento;
	}

	public String getComplementoLogradouro() {
		if (complementoLogradouro == null) {
			complementoLogradouro = "";
		}
		return complementoLogradouro;
	}

	public void setComplementoLogradouro(String complementoLogradouro) {
		this.complementoLogradouro = complementoLogradouro;
	}

	public String getNrLogradouro() {
		if (nrLogradouro == null) {
			nrLogradouro = "";
		}
		return nrLogradouro;
	}

	public void setNrLogradouro(String nrLogradouro) {
		this.nrLogradouro = nrLogradouro;
	}

	public Boolean getBaixarConta() {
		if (baixarConta == null) {
			baixarConta = Boolean.FALSE;
		}
		return baixarConta;
	}

	public void setBaixarConta(Boolean baixarConta) {
		this.baixarConta = baixarConta;
	}

	public Boolean getDescontoprogressivo_utilizaDiaUtil() {
		if(descontoprogressivo_utilizaDiaUtil == null) {
			descontoprogressivo_utilizaDiaUtil = false;
		}
		return descontoprogressivo_utilizaDiaUtil;
	}

	public void setDescontoprogressivo_utilizaDiaUtil(Boolean descontoprogressivo_utilizaDiaUtil) {
		this.descontoprogressivo_utilizaDiaUtil = descontoprogressivo_utilizaDiaUtil;
	}

	public Boolean getDescontoprogressivo_utilizaDiaFixo() {
		if(descontoprogressivo_utilizaDiaFixo == null) {
			descontoprogressivo_utilizaDiaFixo = false;
		}
		return descontoprogressivo_utilizaDiaFixo;
	}

	public void setDescontoprogressivo_utilizaDiaFixo(Boolean descontoprogressivo_utilizaDiaFixo) {
		this.descontoprogressivo_utilizaDiaFixo = descontoprogressivo_utilizaDiaFixo;
	}

	public Boolean getContareceber_contaEditadaManualmente() {
		if(contareceber_contaEditadaManualmente == null) {
			contareceber_contaEditadaManualmente = false;
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

	public String getSituacaoConta() {
		if(situacaoConta == null) {
			situacaoConta = "AR";
		}
		return situacaoConta;
	}

	public void setSituacaoConta(String situacaoConta) {
		this.situacaoConta = situacaoConta;
	}
	
	
	private String situacaoConta_Apresentar;
	public String getSituacaoConta_Apresentar() {
		if(situacaoConta_Apresentar == null) {
			SituacaoContaReceber situacaoContaReceber = SituacaoContaReceber.getEnum(getSituacaoConta());
			if(situacaoContaReceber != null) {
				situacaoConta_Apresentar = situacaoContaReceber.getDescricao();
			}else {
				situacaoConta_Apresentar = "A Receber";
			}
		}
		return situacaoConta_Apresentar;
	}

	public String getCodigoMotivoEstorno() {
		if (codigoMotivoEstorno == null) {
			codigoMotivoEstorno = "";
		}
		return codigoMotivoEstorno;
	}

	public void setCodigoMotivoEstorno(String codigoMotivoEstorno) {
		this.codigoMotivoEstorno = codigoMotivoEstorno;
	}

	public String getTelefoneSacado() {
		if(telefoneSacado == null ) {
			telefoneSacado = "";		
		}
		return telefoneSacado;
	}

	public void setTelefoneSacado(String telefoneSacado) {
		this.telefoneSacado = telefoneSacado;
	}

	public Boolean getDescontoDataLimite2Aplicado() {
		if (descontoDataLimite2Aplicado == null) {
			descontoDataLimite2Aplicado = false;
		}
		return descontoDataLimite2Aplicado;
	}

	public void setDescontoDataLimite2Aplicado(Boolean descontoDataLimite2Aplicado) {
		this.descontoDataLimite2Aplicado = descontoDataLimite2Aplicado;
	}

	public Boolean getDescontoDataLimite3Aplicado() {
		if (descontoDataLimite3Aplicado == null) {
			descontoDataLimite3Aplicado = false;
		}
		return descontoDataLimite3Aplicado;
	}

	public void setDescontoDataLimite3Aplicado(Boolean descontoDataLimite3Aplicado) {
		this.descontoDataLimite3Aplicado = descontoDataLimite3Aplicado;
	}

	public Boolean getParceiroPermiteIsentarMulta() {
		if (parceiroPermiteIsentarMulta == null) {
			parceiroPermiteIsentarMulta = false;
		}
		return parceiroPermiteIsentarMulta;
	}

	public void setParceiroPermiteIsentarMulta(Boolean parceiroPermiteIsentarMulta) {
		this.parceiroPermiteIsentarMulta = parceiroPermiteIsentarMulta;
	}

	public Double getJuroPorcentagem() {
		if (juroPorcentagem == null) {
			juroPorcentagem = 0.0;
		}
		return juroPorcentagem;
	}

	public void setJuroPorcentagem(Double juroPorcentagem) {
		this.juroPorcentagem = juroPorcentagem;
	}

	public Double getMultaPorcentagem() {
		if (multaPorcentagem == null) {
			multaPorcentagem = 0.0;
		}
		return multaPorcentagem;
	}

	public void setMultaPorcentagem(Double multaPorcentagem) {
		this.multaPorcentagem = multaPorcentagem;
	}
	
	
	

}
