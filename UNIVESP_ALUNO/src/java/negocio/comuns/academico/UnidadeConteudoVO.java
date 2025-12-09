package negocio.comuns.academico;

import negocio.comuns.academico.enumeradores.TipoIntegradorConteudoEnum;
import negocio.comuns.arquitetura.SuperInterfaceVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.ead.enumeradores.OrigemBackgroundConteudoEnum;
import negocio.comuns.ead.enumeradores.TamanhoImagemBackgroundConteudoEnum;
import negocio.comuns.utilitarias.Uteis;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class UnidadeConteudoVO extends SuperVO implements SuperInterfaceVO  {

   
    private static final long serialVersionUID = 1390255460495740132L;
    private Integer codigo;
    private ConteudoVO conteudo;
    private Integer ordem;
    private String titulo;
    /*
     * Transiente
     */
    private Integer tempo;
    private Double ponto;
    private Integer paginas;
    
    private List<ConteudoUnidadePaginaVO> conteudoUnidadePaginaVOs;
    
    private String caminhoBaseBackground;
    private String nomeImagemBackground;
    private String corBackground;
    private TamanhoImagemBackgroundConteudoEnum tamanhoImagemBackgroundConteudo;
    private OrigemBackgroundConteudoEnum origemBackgroundConteudo;
    private TemaAssuntoVO temaAssuntoVO;
    private TipoIntegradorConteudoEnum tipoIntegradorEnum;
    private String idUnidadeIntegrada;
	/**
	 * Transient verificar se existe AnotacaoDisciplina
	 */
	private Boolean existeAnotacaoDisciplina;
	/**
	 * Fim Transient
	 */
	
	public UnidadeConteudoVO clone() throws CloneNotSupportedException {
		UnidadeConteudoVO clone = (UnidadeConteudoVO) super.clone();
		clone.setNovoObj(true);
        clone.setCodigo(0);
        clone.setConteudo(new ConteudoVO());
        clone.setConteudoUnidadePaginaVOs(new ArrayList<ConteudoUnidadePaginaVO>());
        for (ConteudoUnidadePaginaVO conteudoUnidadePaginaVO : this.getConteudoUnidadePaginaVOs()) {
			ConteudoUnidadePaginaVO cloneConteudoUnidadePaginaVO = conteudoUnidadePaginaVO.clone();
			clone.getConteudoUnidadePaginaVOs().add(cloneConteudoUnidadePaginaVO);
		}
		return clone;
	}
    
    public ConteudoVO getConteudo() {
        if(conteudo == null){
            conteudo = new ConteudoVO();
        }
        return conteudo;
    }
    
    public void setConteudo(ConteudoVO conteudo) {
        this.conteudo = conteudo;
    }
    
    public Integer getOrdem() {
        if(ordem == null){
            ordem = 0;
        }
        return ordem;
    }
    
    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }
    
    public String getTitulo() {
        if(titulo == null){
            titulo = "";
        }
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
      
    
    public Integer getTempo() {
        if(tempo == null){
            tempo = 0;
        }
        return tempo;
    }
    
    public void setTempo(Integer tempo) {
        this.tempo = tempo;
    }
    
    public Double getPonto() {
        if(ponto == null){
            ponto = 0.0;
        }
        return ponto;
    }
    
    public void setPonto(Double ponto) {
        this.ponto = ponto;
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

    
    public List<ConteudoUnidadePaginaVO> getConteudoUnidadePaginaVOs() {
        if(conteudoUnidadePaginaVOs == null){
            conteudoUnidadePaginaVOs = new ArrayList<ConteudoUnidadePaginaVO>(0);
        }
        return conteudoUnidadePaginaVOs;
    }

    
    public void setConteudoUnidadePaginaVOs(List<ConteudoUnidadePaginaVO> conteudoUnidadePaginaVOs) {
        this.conteudoUnidadePaginaVOs = conteudoUnidadePaginaVOs;
    }

    
    public Integer getPaginas() {
    	if(paginas == null){
    		paginas = 0;
    	}
        return paginas;
    }

    
    public void setPaginas(Integer paginas) {
        this.paginas = paginas;
    }
    
    
    public String getCaminhoBaseBackground() {
		if(caminhoBaseBackground == null){
			caminhoBaseBackground = "";
		}
		return caminhoBaseBackground;
	}

	public void setCaminhoBaseBackground(String caminhoBaseBackground) {
		this.caminhoBaseBackground = caminhoBaseBackground;
	}

	public String getNomeImagemBackground() {
		if(nomeImagemBackground == null){
			nomeImagemBackground = "";
		}
		return nomeImagemBackground;
	}

	public void setNomeImagemBackground(String nomeImagemBackground) {
		this.nomeImagemBackground = nomeImagemBackground;
	}

	public String getCorBackground() {
		if(corBackground == null){
			corBackground = "#FFFFFF";
		}
		return corBackground;
	}

	public void setCorBackground(String corBackground) {
		this.corBackground = corBackground;
	}

	public TamanhoImagemBackgroundConteudoEnum getTamanhoImagemBackgroundConteudo() {
		if(tamanhoImagemBackgroundConteudo == null){
			tamanhoImagemBackgroundConteudo = TamanhoImagemBackgroundConteudoEnum.CEM_PORCENTO;
		}
		return tamanhoImagemBackgroundConteudo;
	}

	public void setTamanhoImagemBackgroundConteudo(TamanhoImagemBackgroundConteudoEnum tamanhoImagemBackgroundConteudoEnum) {
		this.tamanhoImagemBackgroundConteudo = tamanhoImagemBackgroundConteudoEnum;
	}

	public OrigemBackgroundConteudoEnum getOrigemBackgroundConteudo() {
		if(origemBackgroundConteudo == null){
			origemBackgroundConteudo = OrigemBackgroundConteudoEnum.SEM_BACKGROUND;
		}
		return origemBackgroundConteudo;
	}

	public void setOrigemBackgroundConteudo(OrigemBackgroundConteudoEnum origemBackgroundConteudo) {
		this.origemBackgroundConteudo = origemBackgroundConteudo;
	}

	
	public Boolean getExisteImagemBackground(){
		return !getOrigemBackgroundConteudo().equals(OrigemBackgroundConteudoEnum.SEM_BACKGROUND) && (!getCaminhoBaseBackground().trim().isEmpty() || !getCorBackground().trim().isEmpty());
	}

	public Boolean getExisteAnotacaoDisciplina() {
		if (existeAnotacaoDisciplina == null) {
			existeAnotacaoDisciplina = false;
		}
		return existeAnotacaoDisciplina;
	}

	public void setExisteAnotacaoDisciplina(Boolean existeAnotacaoDisciplina) {
		this.existeAnotacaoDisciplina = existeAnotacaoDisciplina;
	}

	public TemaAssuntoVO getTemaAssuntoVO() {
		if(temaAssuntoVO == null) {
			temaAssuntoVO = new TemaAssuntoVO();
		}
		return temaAssuntoVO;
	}

	public void setTemaAssuntoVO(TemaAssuntoVO temaAssuntoVO) {
		this.temaAssuntoVO = temaAssuntoVO;
	}
	
	private static String CONTEUDO_PARCIALMENTE_VISUALIZADO = "fas fa-check text-warning";
	private static String CONTEUDO_TOTALMENTE_VISUALIZADO = "fas fa-check text-success";
	private static String CONTEUDO_NAO_VISUALIZADO = "fas fa-check text-gray";
	
	public String getCssConteudoVisualizado() {
		return getConteudoUnidadePaginaVOs().stream().anyMatch(t -> t.getPaginaJaVisualizada()) && getConteudoUnidadePaginaVOs().stream().anyMatch(t -> !t.getPaginaJaVisualizada()) ? CONTEUDO_PARCIALMENTE_VISUALIZADO : 
			!getConteudoUnidadePaginaVOs().stream().anyMatch(t -> !t.getPaginaJaVisualizada()) ? CONTEUDO_TOTALMENTE_VISUALIZADO : CONTEUDO_NAO_VISUALIZADO;
	}
	
	public TipoIntegradorConteudoEnum getTipoIntegradorEnum() {
		return tipoIntegradorEnum;
	}
	
	public void setTipoIntegradorEnum(TipoIntegradorConteudoEnum tipoIntegradorEnum) {
		this.tipoIntegradorEnum = tipoIntegradorEnum;
	}
	
	public String getIdUnidadeIntegrada() {
		return idUnidadeIntegrada;
	}
	
	public void setIdUnidadeIntegrada(String idUnidadeIntegrada) {
		this.idUnidadeIntegrada = idUnidadeIntegrada;
	}
	
	public boolean isIntegradorConteudoAureaUtilizado() {
		return Objects.nonNull(getTipoIntegradorEnum()) && getTipoIntegradorEnum().isSistemaAurea() && Uteis.isAtributoPreenchido(getIdUnidadeIntegrada());
	}
}
