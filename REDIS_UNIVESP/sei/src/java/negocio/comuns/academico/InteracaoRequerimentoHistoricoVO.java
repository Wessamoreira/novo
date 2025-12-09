package negocio.comuns.academico;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import com.google.gson.annotations.Expose;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoHistoricoVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade InteracaoRequerimentoHistoricoVO. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "interacaoRequerimentoHistoricoVO")
public class InteracaoRequerimentoHistoricoVO extends SuperVO {

    private Integer codigo;
    private String interacao;
    private UsuarioVO usuarioInteracao;
    private Date dataInteracao;
    @Expose(serialize = false, deserialize = false)
    private RequerimentoHistoricoVO requerimentoHistorico;
    private Boolean excluido;
    private InteracaoRequerimentoHistoricoVO interacaoRequerimentoHistoricoPai;
	private String nivelApresentacao;
	
	//TRANSIENT
	private Integer codigoInteracaoRequerimentoHistoricoPai;
	
    /**
     * Atributos Transientes
     */
	private String cssQtdRegistroRequerimentoHistorico;
	private String cssApresentacao;
    
    public static final long serialVersionUID = 1L;

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @XmlElement(name = "interacao")
    public String getInteracao() {
        if (interacao == null) {
        	interacao = "";
        }
        return interacao;
    }

    public void setInteracao(String interacao) {
        this.interacao = interacao;
    }

    @XmlElement(name = "usuarioInteracao")
	public UsuarioVO getUsuarioInteracao() {
		if(usuarioInteracao == null) {
			usuarioInteracao = new UsuarioVO();
		}
		return usuarioInteracao;
	}

	public void setUsuarioInteracao(UsuarioVO usuarioInteracao) {
		this.usuarioInteracao = usuarioInteracao;
	}

	@XmlElement(name = "dataInteracao")
	public Date getDataInteracao() {
		return dataInteracao;
	}

	public void setDataInteracao(Date dataInteracao) {
		this.dataInteracao = dataInteracao;
	}

	public RequerimentoHistoricoVO getRequerimentoHistorico() {
		if(requerimentoHistorico == null) {
			requerimentoHistorico = new RequerimentoHistoricoVO();
		}
		return requerimentoHistorico;
	}

	public void setRequerimentoHistorico(RequerimentoHistoricoVO requerimentoHistorico) {
		this.requerimentoHistorico = requerimentoHistorico;
	}
	
	@XmlElement(name = "dataInteracaoApresentar")
	public String getDataInteracao_Apresentar() {
		return Uteis.getData(dataInteracao) + " " + Uteis.getHoraMinutoComMascara(dataInteracao);
	}

	public Boolean getExcluido() {
		if(excluido == null) {
			excluido = Boolean.FALSE;
		}
		return excluido;
	}

	public void setExcluido(Boolean excluido) {
		this.excluido = excluido;
	}

	public InteracaoRequerimentoHistoricoVO getInteracaoRequerimentoHistoricoPai() {
		if(interacaoRequerimentoHistoricoPai == null) {
			interacaoRequerimentoHistoricoPai = new InteracaoRequerimentoHistoricoVO();
		}
		return interacaoRequerimentoHistoricoPai;
	}

	public void setInteracaoRequerimentoHistoricoPai(InteracaoRequerimentoHistoricoVO interacaoRequerimentoHistoricoPai) {
		this.interacaoRequerimentoHistoricoPai = interacaoRequerimentoHistoricoPai;
	}

	public String getNivelApresentacao() {
		if(nivelApresentacao == null) {
			nivelApresentacao = "";
		}
		return nivelApresentacao;
	}

	public void setNivelApresentacao(String nivelApresentacao) {
		this.nivelApresentacao = nivelApresentacao;
	}
	
	public String getCssQtdRegistroRequerimentoHistorico() {
		if(cssQtdRegistroRequerimentoHistorico==null){
			cssQtdRegistroRequerimentoHistorico	="";
		}
		return cssQtdRegistroRequerimentoHistorico;
	}

	public void setCssQtdRegistroRequerimentoHistorico(String cssQtdRegistroRequerimentoHistorico) {
		this.cssQtdRegistroRequerimentoHistorico = cssQtdRegistroRequerimentoHistorico;
	}

	public String getCssApresentacao() {
		int qtd = StringUtils.countMatches(getNivelApresentacao(), ".");
		return "margin-left: "+(qtd * 35) +"px;";
	}

	public void setCssApresentacao(String cssApresentacao) {
		this.cssApresentacao = cssApresentacao;
	}

	@XmlElement(name = "codigoInteracaoRequerimentoHistoricoPai")
	public Integer getCodigoInteracaoRequerimentoHistoricoPai() {
		if (codigoInteracaoRequerimentoHistoricoPai == null) {
			codigoInteracaoRequerimentoHistoricoPai = 0;
		}
		return codigoInteracaoRequerimentoHistoricoPai;
	}

	public void setCodigoInteracaoRequerimentoHistoricoPai(Integer codigoInteracaoRequerimentoHistoricoPai) {
		this.codigoInteracaoRequerimentoHistoricoPai = codigoInteracaoRequerimentoHistoricoPai;
	}
	
	
    
}
