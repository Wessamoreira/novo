package negocio.comuns.basico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.arquitetura.enumeradores.TipoNovidadeEnum;
import negocio.comuns.basico.enumeradores.TipoArtefatoAjudaEnum;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade ArtefatoAjudaVO. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * @author Paulo Taucci
 * @see SuperVO
 */
public class ArtefatoAjudaVO extends SuperVO {

    private Integer codigo;
    private String titulo;
    private String descricao;
    private String textoInformativo;
    private TipoArtefatoAjudaEnum tipoArtefato;
    private String palavrasChave;
    private String link;
    private String modulo;
    private String recurso;
    private String submodulo;
    private String tela;
    private String versao;
    private Boolean artefatoDesatualizado;
    private Boolean destaque;
    private UsuarioVO responsavelAssinalarDesatualizado;
    private Date dataAssinaladoDesatualizado;
    private Date dataVersao;
    private UsuarioVO responsavelCadastro;
    @ExcluirJsonAnnotation
    @JsonIgnore
    @Expose(deserialize = false, serialize = false)
    private List<ArtefatoAjudaVO> artefatoAjudaVOs;
    private Boolean novidade;
    private Date dataLimiteDisponibilidade;
    private Date dataInicioDisponibilidade;
    private TipoNovidadeEnum tipoNovidade;
    
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ArtefatoAjudaVO</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public ArtefatoAjudaVO() {
        super();
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

    public String getTitulo() {
        if (titulo == null) {
            titulo = "";
        }
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return descricao;
    }

    public String getDescricao_Apresentar() {
        return Uteis.getObterParteStringMantendoPalavraFinalInteira(getDescricao(), 64, true);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public TipoArtefatoAjudaEnum getTipoArtefato() {
    	if(tipoArtefato == null) {
    		tipoArtefato =  TipoArtefatoAjudaEnum.PDF;
    	}
        return tipoArtefato;
    }

    public void setTipoArtefato(TipoArtefatoAjudaEnum tipoArtefato) {
        this.tipoArtefato = tipoArtefato;
    }

    public String getPalavrasChave() {
        if (palavrasChave == null) {
            palavrasChave = "";
        }
        return palavrasChave;
    }

    public void setPalavrasChave(String palavrasChave) {
        this.palavrasChave = palavrasChave;
    }

    public String getLink() {
        if (link == null) {
            link = "";
        }
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Boolean getArtefatoDesatualizado() {
        if (artefatoDesatualizado == null) {
            artefatoDesatualizado = Boolean.FALSE;
        }
        return artefatoDesatualizado;
    }

    public void setArtefatoDesatualizado(Boolean artefatoDesatualizado) {
        this.artefatoDesatualizado = artefatoDesatualizado;
    }

    public UsuarioVO getResponsavelAssinalarDesatualizado() {
        if (responsavelAssinalarDesatualizado == null) {
            responsavelAssinalarDesatualizado = new UsuarioVO();
        }
        return responsavelAssinalarDesatualizado;
    }

    public void setResponsavelAssinalarDesatualizado(UsuarioVO responsavelAssinalarDesatualizado) {
        this.responsavelAssinalarDesatualizado = responsavelAssinalarDesatualizado;
    }

    public Date getDataAssinaladoDesatualizado() {
        if (dataAssinaladoDesatualizado == null) {
            dataAssinaladoDesatualizado = new Date();
        }
        return dataAssinaladoDesatualizado;
    }

    public void setDataAssinaladoDesatualizado(Date dataAssinaladoDesatualizado) {
        this.dataAssinaladoDesatualizado = dataAssinaladoDesatualizado;
    }

    public UsuarioVO getResponsavelCadastro() {
        if (responsavelCadastro == null) {
            responsavelCadastro = new UsuarioVO();
        }
        return responsavelCadastro;
    }

    public void setResponsavelCadastro(UsuarioVO responsavelCadastro) {
        this.responsavelCadastro = responsavelCadastro;
    }

    /**
     * @return the modulo
     */
    public String getModulo() {
        if (modulo == null) {
            modulo = "";
        }
        return modulo;
    }

    /**
     * @param modulo the modulo to set
     */
    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

	public String getSubmodulo() {
		if(submodulo == null) {
			submodulo =  "";
		}
		return submodulo;
	}

	public void setSubmodulo(String submodulo) {
		this.submodulo = submodulo;
	}

	public String getTela() {
		if(tela == null) {
			tela =  "";
		}
		return tela;
	}

	public void setTela(String tela) {
		this.tela = tela;
	}

	public String getVersao() {
		if(versao == null) {
			versao = "";
		}
		return versao;
	}

	public void setVersao(String versao) {
		this.versao = versao;
	}


	 public String linkApresentar;
	    
	    
	    public String getLinkApresentar() {
	    	if(linkApresentar == null) {
	    		StringBuilder texto = new StringBuilder();
	    		String urlvideo = getLink().trim();
	    	
	    		if (urlvideo.endsWith(".pdf") || getTipoArtefato().equals(TipoArtefatoAjudaEnum.PDF) || urlvideo.contains("<iframe") || urlvideo.contains("<object") || urlvideo.contains("<OBJECT")) {
	    			if(urlvideo.contains("width")) {
	    				String urlWidth = urlvideo.substring(0, urlvideo.indexOf("width"));
	    				String urlWidth2 = urlvideo.substring(urlvideo.indexOf("width")+7, urlvideo.length());
	    				urlWidth2 = urlWidth2.substring(urlWidth2.indexOf("\"")+1, urlWidth2.length());
	    				urlvideo = urlWidth+"width=\"100%\""+urlWidth2;
	    				urlWidth = urlvideo.substring(0, urlvideo.indexOf("height"));
	    				urlWidth2 = urlvideo.substring(urlvideo.indexOf("height")+8, urlvideo.length());
	    				urlWidth2 = urlWidth2.substring(urlWidth2.indexOf("\"")+1, urlWidth2.length());
	    				urlvideo = urlWidth+"height=\"100%\""+urlWidth2;
	    				
	    			}
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
			return linkApresentar;
		}

		public void setLinkApresentar(String linkApresentar) {
			this.linkApresentar = linkApresentar;
		}

		public List<ArtefatoAjudaVO> getArtefatoAjudaVOs() {
			if(artefatoAjudaVOs == null) {
				artefatoAjudaVOs =  new ArrayList<ArtefatoAjudaVO>(0);
			}
			return artefatoAjudaVOs;
		}

		public void setArtefatoAjudaVOs(List<ArtefatoAjudaVO> artefatoAjudaVOs) {
			this.artefatoAjudaVOs = artefatoAjudaVOs;
		}

		public String getRecurso() {
			if(recurso == null) {
				recurso =  "";
			}
			return recurso;
		}

		public void setRecurso(String recurso) {
			this.recurso = recurso;
		}

		public Boolean getNovidade() {
			if(novidade == null) {
				novidade = false;
			}
			return novidade;
		}

		public void setNovidade(Boolean novidade) {
			this.novidade = novidade;
		}

		public Date getDataVersao() {
			if(dataVersao == null) {
				dataVersao =  new Date();
			}
			return dataVersao;
		}

		public void setDataVersao(Date dataVersao) {
			this.dataVersao = dataVersao;
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

		public Boolean getDestaque() {
			if(destaque == null) {
				destaque =  false;
			}
			return destaque;
		}

		public void setDestaque(Boolean destaque) {
			this.destaque = destaque;
		}

		public Date getDataInicioDisponibilidade() {
			return dataInicioDisponibilidade;
		}

		public void setDataInicioDisponibilidade(Date dataInicioDisponibilidade) {
			this.dataInicioDisponibilidade = dataInicioDisponibilidade;
		}

		public String getTextoInformativo() {
			if(textoInformativo == null) {
				textoInformativo= "";
			}
			return textoInformativo;
		}

		public void setTextoInformativo(String textoInformativo) {
			this.textoInformativo = textoInformativo;
		}

		
}
