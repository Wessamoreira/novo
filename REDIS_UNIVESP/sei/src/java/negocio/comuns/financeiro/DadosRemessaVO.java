package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;
/**
 *
 * @author Carlos
 */
public class DadosRemessaVO extends SuperVO{
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
    private Double valor;
    private Double valorDescontoAluno;
    private Double valorBase;
    private String especieTitulo;
    private Double juro;
    private Date dataLimiteConcessaoDesconto;
    private Double valorDescontoDataLimite;
    private Double valorDesconto;
    //01=CPF  -  02=CNPJ    - codigoInscricao
    private Integer codigoInscricao;
    //CPF OU CNPJ
    private String numeroInscricao;
    private String nomeSacado;
    private String logradouro;
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
    private Integer codigo;
    private String codMovRemessa;
	private Boolean utilizaCobrancaPartilhada;
	private String codigoReceptor1;
	private String codigoReceptor2;
	private String codigoReceptor3;
	private String codigoReceptor4;
	private Boolean descontoValidoAteDataParcela;
	
  
    @Override
    public DadosRemessaVO clone() throws CloneNotSupportedException {        
        return (DadosRemessaVO)super.clone();
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
        return descontoprogressivo_codigo;
    }

    public void setDescontoprogressivo_codigo(Integer descontoprogressivo_codigo) {
        this.descontoprogressivo_codigo = descontoprogressivo_codigo;
    }

    public Integer getDescontoprogressivo_dialimite1() {
        return descontoprogressivo_dialimite1;
    }

    public void setDescontoprogressivo_dialimite1(Integer descontoprogressivo_dialimite1) {
        this.descontoprogressivo_dialimite1 = descontoprogressivo_dialimite1;
    }

    public Integer getDescontoprogressivo_dialimite2() {
        return descontoprogressivo_dialimite2;
    }

    public void setDescontoprogressivo_dialimite2(Integer descontoprogressivo_dialimite2) {
        this.descontoprogressivo_dialimite2 = descontoprogressivo_dialimite2;
    }

    public Integer getDescontoprogressivo_dialimite3() {
        return descontoprogressivo_dialimite3;
    }

    public void setDescontoprogressivo_dialimite3(Integer descontoprogressivo_dialimite3) {
        this.descontoprogressivo_dialimite3 = descontoprogressivo_dialimite3;
    }

    public Integer getDescontoprogressivo_dialimite4() {
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
}
