package negocio.comuns.arquitetura;

import java.util.Date;

import negocio.comuns.arquitetura.enumeradores.TipoNovidadeEnum;
import negocio.comuns.basico.enumeradores.TipoArtefatoAjudaEnum;



public class NovidadeSeiVO extends SuperVO {

    /**
     * 
     */
    private static final long serialVersionUID = -4413343543333219361L;
    
    private Integer codigo;
    private String versao;
    private String url;
    private String textoInformativo;
    private Date data;
    private Date dataLimiteDisponibilidade;
    private Date dataInicioDisponibilidade;
    private Boolean destaque;
    private TipoNovidadeEnum tipoNovidade;
    private String descricao;
    private String palavrasChaves;
    private Boolean visualizado;
    private TipoArtefatoAjudaEnum tipoArtefato;
    
    
    public NovidadeSeiVO() {
        super();
        // TODO Auto-generated constructor stub
    }

    public NovidadeSeiVO(Integer codigo, String versao, String url, Date data, String descricao, Boolean visualizado) {
        super();
        this.codigo = codigo;
        this.versao = versao;
        this.url = url;
        this.data = data;
        this.descricao = descricao;
        this.visualizado = visualizado;
    }

    public Integer getCodigo() {
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public String getVersao() {
        return versao;
    }
    
    public void setVersao(String versao) {
        this.versao = versao;
    }
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public Date getData() {
        return data;
    }
    
    public void setData(Date data) {
        this.data = data;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    
    public Boolean getVisualizado() {
        if(visualizado == null){
            visualizado = false;
        }
        return visualizado;
    }

    
    public void setVisualizado(Boolean visualizado) {
        this.visualizado = visualizado;
    }
    
    public String linkApresentar;
    
    
    public String getLinkApresentar() {
    	if(linkApresentar == null) {
    		StringBuilder texto = new StringBuilder();
    		String urlvideo = getUrl().trim();
    	
    		if (urlvideo.endsWith(".pdf") || (getTipoArtefato() != null && getTipoArtefato().equals(TipoArtefatoAjudaEnum.PDF)) || urlvideo.contains("<iframe") || urlvideo.contains("<object") || urlvideo.contains("<OBJECT")) {					
    			texto.append(urlvideo);
    		} else if (urlvideo.contains("youtube") && urlvideo.indexOf("v=") > 0) {
    			if (urlvideo.contains("&")) {
    				urlvideo = urlvideo.substring(urlvideo.indexOf("v=") + 2, urlvideo.indexOf("&"));
    			} else {
    				urlvideo = urlvideo.substring(urlvideo.indexOf("v=") + 2);
    			}
    			texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn\" style=\"z-index:1;height:300px\"  height=\"300px\"  width=\"100%\" src=\"https://www.youtube.com/embed/").append(urlvideo).append("?wmode=transparent\" frameborder=\"0\" ></iframe>");
    		} else if (urlvideo.contains("youtu.be")) {
    			urlvideo = urlvideo.substring(urlvideo.lastIndexOf("/") + 1);
    			texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn\" style=\"z-index:1;height:300px\" height=\"300px\"  width=\"100%\" src=\"https://www.youtube.com/embed/").append(urlvideo).append("?wmode=transparent\" frameborder=\"0\" ></iframe>");
    		} else if (!urlvideo.contains("http") && !urlvideo.contains("youtu") && !urlvideo.contains("www")) {
    			texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn\" style=\"z-index:1;height:300px\" height=\"300px\"  width=\"100%\" src=\"https://www.youtube.com/embed/").append(urlvideo).append("?wmode=transparent\" frameborder=\"0\" ></iframe>");
    		} else if (urlvideo.contains("?")) {
    			texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn\" style=\"z-index:1;height:300px\" height=\"300px\"  width=\"100%\" src=\"").append(urlvideo).append("\" frameborder=\"0\" ></iframe>");
    		} else {
    			texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn\" style=\"z-index:1;height:300px\" height=\"300px\"  width=\"100%\" src=\"").append(urlvideo).append("?wmode=transparent\" frameborder=\"0\" ></iframe>");
    		}
    		linkApresentar = texto.toString();
    	}
		return linkApresentar.replace("style=\"min-height:500px\"", "style=\"min-height:300px; max-height:300px\"");
	}
    
    public String linkApresentarDestaque;
    public String getLinkApresentarDestaque() {
    	if(linkApresentarDestaque == null) {
    		StringBuilder texto = new StringBuilder();
    		String urlvideo = getUrl().trim();
    	
    		if (urlvideo.endsWith(".pdf") || (getTipoArtefato() != null && getTipoArtefato().equals(TipoArtefatoAjudaEnum.PDF)) || urlvideo.contains("<iframe") || urlvideo.contains("<object") || urlvideo.contains("<OBJECT")) {					
    			texto.append(urlvideo);
    		} else if (urlvideo.contains("youtube") && urlvideo.indexOf("v=") > 0) {
    			if (urlvideo.contains("&")) {
    				urlvideo = urlvideo.substring(urlvideo.indexOf("v=") + 2, urlvideo.indexOf("&"));
    			} else {
    				urlvideo = urlvideo.substring(urlvideo.indexOf("v=") + 2);
    			}
    			texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn\" style=\"z-index:1;height:300px\"  height=\"300px\"  width=\"100%\" src=\"https://www.youtube.com/embed/").append(urlvideo).append("?wmode=transparent\" frameborder=\"0\" ></iframe>");
    		} else if (urlvideo.contains("youtu.be")) {
    			urlvideo = urlvideo.substring(urlvideo.lastIndexOf("/") + 1);
    			texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn\" style=\"z-index:1;height:300px\" height=\"300px\"  width=\"100%\" src=\"https://www.youtube.com/embed/").append(urlvideo).append("?wmode=transparent\" frameborder=\"0\" ></iframe>");
    		} else if (!urlvideo.contains("http") && !urlvideo.contains("youtu") && !urlvideo.contains("www")) {
    			texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn\" style=\"z-index:1;height:300px\" height=\"300px\"  width=\"100%\" src=\"https://www.youtube.com/embed/").append(urlvideo).append("?wmode=transparent\" frameborder=\"0\" ></iframe>");
    		} else if (urlvideo.contains("?")) {
    			texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn\" style=\"z-index:1;height:300px\" height=\"300px\"  width=\"100%\" src=\"").append(urlvideo).append("\" frameborder=\"0\" ></iframe>");
    		} else {
    			texto.append("<iframe wmode=\"transparent\"  class=\"col-md-12 pn\" style=\"z-index:1;height:300px\" height=\"300px\"  width=\"100%\" src=\"").append(urlvideo).append("?wmode=transparent\" frameborder=\"0\" ></iframe>");
    		}
    		linkApresentarDestaque = texto.toString();
    		if(linkApresentarDestaque.contains("class=\"")) {
    			String style  = linkApresentarDestaque.substring(linkApresentarDestaque.indexOf("class=\""), linkApresentarDestaque.length());
    			style = style.substring(0, style.indexOf("\""));    			
    			
    			linkApresentarDestaque = linkApresentarDestaque.replace(style, style+" destaqueNovidade \" ");
    		}else {
    			if(linkApresentarDestaque.contains("/>")) {
    				linkApresentarDestaque = linkApresentarDestaque.replace("/>", " class=\"destaqueNovidade\"/>");
    			}else if(linkApresentarDestaque.contains(">")) {
    				linkApresentarDestaque =  linkApresentarDestaque.replaceFirst(">", " class=\"destaqueNovidade\" >");
    			}
    		}
    	}
		return linkApresentarDestaque;
	}


	public void setLinkApresentar(String linkApresentar) {
		this.linkApresentar = linkApresentar;
	}
	

    public TipoArtefatoAjudaEnum getTipoArtefato() {
        return tipoArtefato;
    }

    public void setTipoArtefato(TipoArtefatoAjudaEnum tipoArtefato) {
        this.tipoArtefato = tipoArtefato;
    }

	public String getPalavrasChaves() {
		if(palavrasChaves == null) {
			palavrasChaves = "";
		}
		return palavrasChaves;
	}

	public void setPalavrasChaves(String palavrasChaves) {
		this.palavrasChaves = palavrasChaves;
	}

	public Date getDataLimiteDisponibilidade() {
		return dataLimiteDisponibilidade;
	}

	public void setDataLimiteDisponibilidade(Date dataLimiteDisponibilidade) {
		this.dataLimiteDisponibilidade = dataLimiteDisponibilidade;
	}

	public TipoNovidadeEnum getTipoNovidade() {
		if(tipoNovidade == null) {
			tipoNovidade =  TipoNovidadeEnum.NEWS;
		}
		return tipoNovidade;
	}

	public void setTipoNovidade(TipoNovidadeEnum tipoNovidade) {
		this.tipoNovidade = tipoNovidade;
	}

	public Date getDataInicioDisponibilidade() {
		return dataInicioDisponibilidade;
	}

	public void setDataInicioDisponibilidade(Date dataInicioDisponibilidade) {
		this.dataInicioDisponibilidade = dataInicioDisponibilidade;
	}

	public Boolean getDestaque() {
		if(destaque == null) {
			destaque = false;
		}
		return destaque;
	}

	public void setDestaque(Boolean destaque) {
		this.destaque = destaque;
	}

	public String getTextoInformativo() {
		if(textoInformativo == null) {
			textoInformativo =  "";
		}
		return textoInformativo;
	}

	public void setTextoInformativo(String textoInformativo) {
		this.textoInformativo = textoInformativo;
	}
    
    

}
