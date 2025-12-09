package negocio.comuns.academico;

import java.util.Date;

import org.apache.commons.lang.StringUtils;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;


public class ForumInteracaoVO extends SuperVO {

    /**
     * 
     */
    private static final long serialVersionUID = -7811769502369116822L;
    private Integer codigo;
    private String interacao;
    private Date dataInteracao;
    private UsuarioVO usuarioInteracao;
    private Boolean excluido;
    private Integer forum;
    private ForumInteracaoVO forumInteracaoPai;
	private String nivelApresentacao;
    /**
     * Atributos Transientes
     */
	private String cssQtdRegistroForum;
	private String cssApresentacao;
    private Boolean jaGostado;
    private Integer qtdeGostado;
    
    
    public Integer getQtdeGostado() {
        if(qtdeGostado == null){
            qtdeGostado = 0;
        }
        return qtdeGostado;
    }

    
    public void setQtdeGostado(Integer qtdeGostado) {
        this.qtdeGostado = qtdeGostado;
    }

    public Integer getCodigo() {
        if(codigo == null){
            codigo = 0;
        }
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public String getInteracao() {
        if(interacao == null){
            interacao = "";
        }
        return interacao;
    }
    
    public void setInteracao(String interacao) {
        this.interacao = interacao;
    }
    
    public Date getDataInteracao() {
        if(dataInteracao == null){
            dataInteracao = new Date();
        }
        return dataInteracao;
    }
    
    public void setDataInteracao(Date dataInteracao) {
        this.dataInteracao = dataInteracao;
    }
    
    public UsuarioVO getUsuarioInteracao() {
        if(usuarioInteracao == null){
            usuarioInteracao = new UsuarioVO();
        }
        return usuarioInteracao;
    }
    
    public void setUsuarioInteracao(UsuarioVO usuarioInteracao) {
        this.usuarioInteracao = usuarioInteracao;
    }
    
    public Boolean getExcluido() {
        if(excluido == null){
            excluido = false;
        }
        return excluido;
    }
    
    public void setExcluido(Boolean excluido) {
        this.excluido = excluido;
    }
    
    public Integer getForum() {
        if(forum == null){
            forum = 0;
        }
        return forum;
    }
    
    public void setForum(Integer forum) {
        this.forum = forum;
    }

    
    public Boolean getJaGostado() {
        if(jaGostado == null){
            jaGostado = false;
        }
        return jaGostado;
    }

    
    public void setJaGostado(Boolean jaGostado) {
        this.jaGostado = jaGostado;
    }
    
    public ForumInteracaoVO getForumInteracaoPai() {
		if (forumInteracaoPai == null) {
			forumInteracaoPai = new ForumInteracaoVO();
		}
		return forumInteracaoPai;
	}

	public void setForumInteracaoPai(ForumInteracaoVO forumInteracaoPai) {
		this.forumInteracaoPai = forumInteracaoPai;
	}

	public String getNivelApresentacao() {
		if (nivelApresentacao == null) {
			nivelApresentacao = "";
		}
		return nivelApresentacao;
	}

	public void setNivelApresentacao(String nivelApresentacao) {
		this.nivelApresentacao = nivelApresentacao;
	}
	
	public String getCssQtdRegistroForum() {
		if(cssQtdRegistroForum==null){
			cssQtdRegistroForum	="";
		}
		return cssQtdRegistroForum;
	}

	public void setCssQtdRegistroForum(String cssQtdRegistroForum) {
		this.cssQtdRegistroForum = cssQtdRegistroForum;
	}

	public String getCssApresentacao() {
		int qtd = StringUtils.countMatches(getNivelApresentacao(), ".");
		if(qtd == 0) {
			return "margin-left: 5px !important";
		}
		return "margin-left: "+(qtd * 35) +"px !important;width: calc(100% - "+(qtd * 35)+"px) !important;min-width(300px)";
	}

	public void setCssApresentacao(String cssApresentacao) {
		this.cssApresentacao = cssApresentacao;
	}
	
    
    
    

}
