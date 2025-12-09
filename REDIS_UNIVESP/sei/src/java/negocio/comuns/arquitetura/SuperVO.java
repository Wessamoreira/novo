package negocio.comuns.arquitetura;


import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.gson.annotations.Expose;

import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.contabil.FechamentoMesVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@XmlAccessorType(XmlAccessType.NONE)
public class SuperVO implements Cloneable, Serializable {
	@ExcluirJsonAnnotation
    /**
	 * 
	 */
	private static final long serialVersionUID = -5223678489379734656L;
	@ExcluirJsonAnnotation
	protected Boolean novoObj;
   /* @ExcluirJsonAnnotation
    protected Boolean selecionado;*/
    @ExcluirJsonAnnotation
    protected Boolean maximixado;    
    @Expose
    @ExcluirJsonAnnotation
    protected Boolean validarDados;
    @ExcluirJsonAnnotation
    protected NivelMontarDados nivelMontarDados;
    /** Atributo responsável por manter o estado cosistente do objeto */
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date created;
    private Integer codigoCreated;
    private String nomeCreated;
    private Date updated;
    private Integer codigoUpdated;
    private String nomeUpdated;
    @ExcluirJsonAnnotation
    private Boolean controlarConcorrencia;
    @ExcluirJsonAnnotation
    private boolean edicaoManual = false;
    @ExcluirJsonAnnotation
	private Boolean verificouBloqueioPorFechamentoMes;
    @ExcluirJsonAnnotation
	private Boolean bloqueioPorFechamentoMesLiberado;
    @ExcluirJsonAnnotation
	private FechamentoMesVO fechamentoMesVOBloqueio;
    @ExcluirJsonAnnotation
	private String descricaoBloqueio;
    @ExcluirJsonAnnotation
    private boolean minimizar = false;
	public String getDescricaoBloqueio() {
		if (descricaoBloqueio == null) {
			descricaoBloqueio = "";
		}
		return descricaoBloqueio;
	}
	public void setDescricaoBloqueio(String descricaoBloqueio) {
		this.descricaoBloqueio = descricaoBloqueio;
	}
	public FechamentoMesVO getFechamentoMesVOBloqueio() {
		if (fechamentoMesVOBloqueio == null) {
			fechamentoMesVOBloqueio = new FechamentoMesVO();
		}
		return fechamentoMesVOBloqueio;
	}
	public void setFechamentoMesVOBloqueio(FechamentoMesVO fechamentoMesVOBloqueio) {
		this.fechamentoMesVOBloqueio = fechamentoMesVOBloqueio;
	}
	public Boolean getApresentarBotaoLiberarBloqueioFechamentoMes() { 
		if ((getVerificouBloqueioPorFechamentoMes()) &&
		    (!getBloqueioPorFechamentoMesLiberado())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	public Boolean getBloqueioPorFechamentoMesLiberado() {
		if (bloqueioPorFechamentoMesLiberado == null) {
			bloqueioPorFechamentoMesLiberado = Boolean.FALSE;
		}
		return bloqueioPorFechamentoMesLiberado;
	}
	public void setBloqueioPorFechamentoMesLiberado(Boolean bloqueioPorFechamentoMesLiberado) {
		this.bloqueioPorFechamentoMesLiberado = bloqueioPorFechamentoMesLiberado;
	}	
	public Boolean getVerificouBloqueioPorFechamentoMes() {
		if (verificouBloqueioPorFechamentoMes == null) {
			verificouBloqueioPorFechamentoMes = Boolean.FALSE;
		}
		return verificouBloqueioPorFechamentoMes;
	}
	public void setVerificouBloqueioPorFechamentoMes(Boolean verificouBloqueioPorFechamentoMes) {
		this.verificouBloqueioPorFechamentoMes = verificouBloqueioPorFechamentoMes;
	}
	public void reiniciarControleBloqueioCompetencia() {
		descricaoBloqueio = "";
		fechamentoMesVOBloqueio = null;
		bloqueioPorFechamentoMesLiberado = Boolean.FALSE;
		verificouBloqueioPorFechamentoMes = Boolean.FALSE;
		dataBloqueioVerificada = null;   
	}
	public void forcarControleBloqueioCompetencia(String descricao, FechamentoMesVO fechamento, Date dataVerificar) {
		descricaoBloqueio = descricao;
		fechamentoMesVOBloqueio = fechamento;
		bloqueioPorFechamentoMesLiberado = Boolean.FALSE;
		verificouBloqueioPorFechamentoMes = Boolean.TRUE;
		dataBloqueioVerificada = dataVerificar;   
	}
	public void liberarVerificacaoBloqueioFechamentoMes() {
		verificouBloqueioPorFechamentoMes = Boolean.TRUE;
		bloqueioPorFechamentoMesLiberado = Boolean.TRUE;
	}
	public void verificarEReplicarLiberacaoBloqueioEntidadePrincipal(SuperVO entidadePrincipal) {
		if ((entidadePrincipal.getVerificouBloqueioPorFechamentoMes()) &&
		    (entidadePrincipal.getBloqueioPorFechamentoMesLiberado())) {
			this.liberarVerificacaoBloqueioFechamentoMes();
		}
	}
	public Boolean verificarBloqueioCompetenciaFoiLiberado() {
		if ((this.getVerificouBloqueioPorFechamentoMes()) &&
			(this.getBloqueioPorFechamentoMesLiberado())) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

    /** Creates a new instance of SuperDAO */
    public SuperVO() {
        setNovoObj(Boolean.TRUE);
        setValidarDados(Boolean.TRUE);
        setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
    }

    @Override
    public Object clone() throws CloneNotSupportedException {        
        return super.clone();
    }

    /**
     * Retorna um status indicando se o objeto é novo (ainda não foi gravado no BD) ou não.
     * @return boolean True indica que o objeto é novo e portanto ainda não foi gravado no BD.
     *                 False indica que o objeto já existe no BD e deve ser alterado.
     */
    public Boolean getNovoObj() {
        return novoObj;
    }

    public Boolean isNovoObj() {
        return novoObj;
    }

    /**
     * Define um status indicando se o objeto é novo (ainda não foi gravado no BD) ou não.
     * @param novoObj True indica que o objeto é novo e portanto ainda não foi gravado no BD.
     *                False indica que o objeto já existe no BD e deve ser alterado.
     */
    public void setNovoObj(Boolean novoObj) {
        this.novoObj = novoObj;
    }

    /**
     * @deprecated em 31/01/2008 - Utializar rotina em Uteis
     */
    public static String removerMascara(String campo) {
        return Uteis.removerMascara(campo);
    }

    /**
     * @deprecated em 31/01/2008 - Utializar rotina em Uteis
     */
    public static String aplicarMascara(String dado, String mascara) {
        return Uteis.aplicarMascara(dado, mascara);
    }

    /**
     * Realiza a validação de um número de CPF.
     * @param retorno True indica que o CPF é valido.
     *                False indica que o CPF é inválido.
     */
    public static boolean verificaCPF(String cpf) {
        return true;
        /*
         * Uteis.verificarCPF(cpf);*/
    }

    /**
     * Realiza a validação de um número de CNPJ.
     * @param retorno True indica que o CNPJ é valido.
     *                False indica que o CNPJ é inválido.
     */
    public static boolean validaCNPJ(String cnpj) {
        return true;

        /*boolean ret = false;
        cnpj = removerMascara(cnpj);
        String base = "00000000000000";
        if (cnpj.length() <= 14) {
        if (cnpj.length() < 14) {
        cnpj = base.substring(0, 14 - cnpj.length()) + cnpj;
        }
        int soma = 0;
        int dig = 0;
        String cnpj_calc = cnpj.substring(0, 12);
        char[] chr_cnpj = cnpj.toCharArray();
        // Primeira parte
        for (int i = 0; i < 4; i++) {
        if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
        soma += (chr_cnpj[i] - 48) * (6 - (i + 1));
        }
        }
        for (int i = 0; i < 8; i++) {
        if (chr_cnpj[i + 4] - 48 >= 0 && chr_cnpj[i + 4] - 48 <= 9) {
        soma += (chr_cnpj[i + 4] - 48) * (10 - (i + 1));
        }
        }
        dig = 11 - (soma % 11);
        cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);
        // Segunda parte
        soma = 0;
        for (int i = 0; i < 5; i++) {
        if (chr_cnpj[i] - 48 >= 0 && chr_cnpj[i] - 48 <= 9) {
        soma += (chr_cnpj[i] - 48) * (7 - (i + 1));
        }
        }
        for (int i = 0; i < 8; i++) {
        if (chr_cnpj[i + 5] - 48 >= 0 && chr_cnpj[i + 5] - 48 <= 9) {
        soma += (chr_cnpj[i + 5] - 48) * (10 - (i + 1));
        }
        }
        dig = 11 - (soma % 11);
        cnpj_calc += (dig == 10 || dig == 11) ? "0" : Integer.toString(dig);
        ret = cnpj.equals(cnpj_calc);

        }
        return ret;*/
    }

    public Boolean isValidarDados() {
        return validarDados;
    }

    public void setValidarDados(Boolean validarDados) {
        this.validarDados = validarDados;
    }

//    protected UnidadeEnsinoVO getUnidadeEnsinoLogado() throws Exception {
//        LoginControle loginControle = (LoginControle) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("LoginControle");
//        UnidadeEnsinoVO unidadeEnsinoVO = loginControle.getUnidadeEnsino();
//        return unidadeEnsinoVO;
//    }
    /**
     * @return the nivelMontarDados
     */
    public NivelMontarDados getNivelMontarDados() {
        return nivelMontarDados;
    }

    /**
     * @param nivelMontarDados the nivelMontarDados to set
     */
    public void setNivelMontarDados(NivelMontarDados nivelMontarDados) {
        this.nivelMontarDados = nivelMontarDados;
    }

    public boolean getIsNivelMontarDadosBasico() {
        if (this.getNivelMontarDados().equals(NivelMontarDados.BASICO)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getIsDadosBasicosDevemSerCarregados(NivelMontarDados nivelDesejadoObjeto) {
        if ((nivelDesejadoObjeto.equals(NivelMontarDados.BASICO)) && (this.getIsNivelMontarDadosNaoInicializado())) {
            return true;
        }
        return false;
    }

    public boolean getIsDadosCompletosDevemSerCarregados(NivelMontarDados nivelDesejadoObjeto) {
        return getIsDadosDevemSerCarregados(nivelDesejadoObjeto);
    }

    public boolean getIsDadosDevemSerCarregados(NivelMontarDados nivelDesejadoObjeto) {
        NivelMontarDados nivelAtualMontarDadosObjeto = this.getNivelMontarDados();
        if ((nivelAtualMontarDadosObjeto.equals(NivelMontarDados.NAO_INICIALIZADO))
                || (nivelDesejadoObjeto.equals(NivelMontarDados.FORCAR_RECARGATODOSOSDADOS))) {
            // se o objeto não está inicializado então devemos montar os dados
            // se foi dado o parâmetro forcar recarga, entao os dados tambem devem
            // ser remontados.
            return true;
        }
        if (nivelDesejadoObjeto.equals(nivelAtualMontarDadosObjeto)) {
            // se o nivel desejado é igual ao nivel atual do objeto nao
            // ha o que fazer... ou seja, os dados não precisam ser montados
            // novamente.
            return false;
        }
        if (nivelAtualMontarDadosObjeto.compararOutroEnum(nivelDesejadoObjeto) > 0) {
            // se o nivel que esta no objeto é maior que o desejado, por exemplo:
            // nivelDesejado: Basico e nivelObjeto: Todos, então não há o que fazer
            return false;
        }
        return true;
    }

    public boolean getIsNivelMontarDadosTodos() {
        if (this.getNivelMontarDados().equals(NivelMontarDados.TODOS)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getIsNivelMontarDadosNaoInicializado() {
        if (this.getNivelMontarDados().equals(NivelMontarDados.NAO_INICIALIZADO)) {
            return true;
        } else {
            return false;
        }
    }
    

    public Date getCreated() {
		if (created == null) {
			created = new Date();
		}
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public String getCreated_apresentar() {
		return Uteis.isAtributoPreenchido(getCreated()) ? Uteis.getDataComHora(getCreated()) : Constantes.EMPTY;
	}
	
	public Integer getCodigoCreated() {
		if (codigoCreated == null) {
			codigoCreated = 0;
		}
		return codigoCreated;
	}
	public void setCodigoCreated(Integer codigoCreated) {
		this.codigoCreated = codigoCreated;
	}
	public String getNomeCreated() {
		if (nomeCreated == null) {
			nomeCreated = "";
		}
		return nomeCreated;
	}
	public void setNomeCreated(String nomeCreated) {
		this.nomeCreated = nomeCreated;
	}
	public Integer getCodigoUpdated() {
		if (codigoUpdated == null) {
			codigoUpdated = 0;
		}
		return codigoUpdated;
	}
	public void setCodigoUpdated(Integer codigoUpdated) {
		this.codigoUpdated = codigoUpdated;
	}
	public String getNomeUpdated() {
		if (nomeUpdated == null) {
			nomeUpdated = "";
		}
		return nomeUpdated;
	}
	public void setNomeUpdated(String nomeUpdated) {
		this.nomeUpdated = nomeUpdated;
	}
	public void setUpdated(Date updated) {
        this.updated = updated;
        //setChanged();
    }

    public void setChanged() {
        //super.setChanged();
    }

    public Date getUpdated() {
        if (updated == null) {
            updated = new Date();
        }
        return updated;
    }

    public void setControlarConcorrencia(Boolean controlarConcorrencia) {
        this.controlarConcorrencia = controlarConcorrencia;
    }

    public Boolean getControlarConcorrencia() {
        if (controlarConcorrencia == null) {
            controlarConcorrencia = Boolean.FALSE;
        }
        return controlarConcorrencia;
    }
    
    @ExcluirJsonAnnotation
    private ConsistirException mensagemAvisoUsuario;

	/**
	 * @return the mensagemAvisoUsuario
	 */
	public ConsistirException getMensagemAvisoUsuario() {
		if (mensagemAvisoUsuario == null) {
			mensagemAvisoUsuario = new ConsistirException("");
		}
		return mensagemAvisoUsuario;
	}

	/**
	 * @param mensagemAvisoUsuario the mensagemAvisoUsuario to set
	 */
	public void setMensagemAvisoUsuario(ConsistirException mensagemAvisoUsuario) {
		this.mensagemAvisoUsuario = mensagemAvisoUsuario;
	}

	public boolean isEdicaoManual() {
		return edicaoManual;
	}

	public void setEdicaoManual(boolean edicaoManual) {
		this.edicaoManual = edicaoManual;
	}
	
	/*
	 * atributo transient
	 */
	@ExcluirJsonAnnotation
	protected Date dataBloqueioVerificada;
	
	public Date getDataBloqueioVerificada() {
		return dataBloqueioVerificada;
	}
	public void setDataBloqueioVerificada(Date dataBloqueioVerificada) {
		this.dataBloqueioVerificada = dataBloqueioVerificada;
	}
	/*public Boolean getSelecionado() {
		if(selecionado == null) {
			selecionado =  false;
		}
		return selecionado;
	}
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}*/
	public Boolean getMaximixado() {
		if(maximixado == null) {
			maximixado =  true;
		}
		return maximixado;
	}
	public void setMaximixado(Boolean maximixado) {
		this.maximixado = maximixado;
	}	

	public boolean isMinimizar() {
		return minimizar;
	}
	
	public void setMinimizar(boolean minimizar) {
		this.minimizar = minimizar;
	}
}
