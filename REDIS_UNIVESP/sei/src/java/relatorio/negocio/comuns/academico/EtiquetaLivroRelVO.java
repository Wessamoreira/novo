package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.List;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

public class EtiquetaLivroRelVO {

    private String codigoBarra;
    private String nomeLivro;
    private String anoPublicacao;
    private String edicao;
    private String identificador;
    private String tipoClassificacao;
    private String autores;
    private String volume;
    private String exemplar;
    private String cutterPha;
    private String cdu;
    private String mes;
    private String anovolume;
    private String edicaoEspecial;
    private String tituloExemplar;
    private String edicaoExemplar;
    private String anoPublicacaoExemplar;    	
    private String nrPaginasExemplar;    
    private String isbnExemplar;    
    private String issnExemplar;    	
    private String nomeUnidadeEnsino;
    private String siglaUnidadeEnsino;
    private String siglaTipoCatalogo;
    private Integer codigoBiblioteca;
    private String abreviacaoTitulo;
    private String fasciculos;
    
	private List<EtiquetaLivroRelVO> listaEtiquetaLivroColuna1;
    private List<EtiquetaLivroRelVO> listaEtiquetaLivroColuna2;
    private List<EtiquetaLivroRelVO> listaEtiquetaLivroColuna3;

    public EtiquetaLivroRelVO() {
    }

    public JRDataSource getEtiquetaLivroColuna1VOs() {
        return new JRBeanArrayDataSource(getListaEtiquetaLivroColuna1().toArray());
    }

    public JRDataSource getEtiquetaLivroColuna2VOs() {
        return new JRBeanArrayDataSource(getListaEtiquetaLivroColuna2().toArray());
    }

    public JRDataSource getEtiquetaLivroColuna3VOs() {
        return new JRBeanArrayDataSource(getListaEtiquetaLivroColuna3().toArray());
    }

    public List<EtiquetaLivroRelVO> getListaEtiquetaLivroColuna1() {
        if (listaEtiquetaLivroColuna1 == null) {
            listaEtiquetaLivroColuna1 = new ArrayList(0);
        }
        return listaEtiquetaLivroColuna1;
    }

    public void setListaEtiquetaLivroColuna1(List<EtiquetaLivroRelVO> listaEtiquetaLivroColuna1) {
        this.listaEtiquetaLivroColuna1 = listaEtiquetaLivroColuna1;
    }

    public List<EtiquetaLivroRelVO> getListaEtiquetaLivroColuna2() {
        if (listaEtiquetaLivroColuna2 == null) {
            listaEtiquetaLivroColuna2 = new ArrayList(0);
        }
        return listaEtiquetaLivroColuna2;
    }

    public void setListaEtiquetaLivroColuna2(List<EtiquetaLivroRelVO> listaEtiquetaLivroColuna2) {
        this.listaEtiquetaLivroColuna2 = listaEtiquetaLivroColuna2;
    }

    public List<EtiquetaLivroRelVO> getListaEtiquetaLivroColuna3() {
        if (listaEtiquetaLivroColuna3 == null) {
            listaEtiquetaLivroColuna3 = new ArrayList(0);
        }
        return listaEtiquetaLivroColuna3;
    }

    public void setListaEtiquetaLivroColuna3(List<EtiquetaLivroRelVO> listaEtiquetaLivroColuna3) {
        this.listaEtiquetaLivroColuna3 = listaEtiquetaLivroColuna3;
    }

    public String getCodigoBarra() {
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public String getNomeLivro() {
        return nomeLivro;
    }

    public void setNomeLivro(String nomeLivro) {
        this.nomeLivro = nomeLivro;
    }

    public String getAnoPublicacao() {
        if (anoPublicacao == null) {
            anoPublicacao = "";
        }
        return (anoPublicacao);
    }

    public void setAnoPublicacao(String anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public String getEdicao() {
        if (edicao == null) {
            edicao = "";
        }
        return edicao;
    }

    public void setEdicao(String edicao) {
        this.edicao = edicao;
    }

    public String getIdentificador() {
        if (identificador == null) {
            identificador = "";
        }
        return identificador;
    }

    public void setIdentificador(String identificador) {
        this.identificador = identificador;
    }

    public String getAutores() {
        if (autores == null) {
            autores = "";
        }
        return autores;
    }

    public void setAutores(String autores) {
        this.autores = autores;
    }

    public String getVolume() {
        if (volume == null) {
            volume = "";
        }
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getExemplar() {
        if (exemplar == null) {
            exemplar = "";
        }
        return exemplar;
    }

    public void setExemplar(String exemplar) {
        this.exemplar = exemplar;
    }

    public String getTipoClassificacao() {
        if (tipoClassificacao == null) {
            tipoClassificacao = "";
        }
        return tipoClassificacao;
    }

    public void setTipoClassificacao(String tipoClassificacao) {
        this.tipoClassificacao = tipoClassificacao;
    }

    
    public String getCutterPha() {
        if(cutterPha == null){
            cutterPha = "";
        }
        return cutterPha;
    }

    
    public void setCutterPha(String cutterPha) {
        this.cutterPha = cutterPha;
    }

	public String getCdu() {
		if(cdu == null){
			cdu = "";
		}
		return cdu;
	}

	public void setCdu(String cdu) {
		this.cdu = cdu;
	}

	public String getMes() {
		if(mes == null){
			mes = "";
		}
		return mes;
	}

	public void setMes(String mes) {
		this.mes = mes;
	}

	public String getAnovolume() {
		if(anovolume== null){
			anovolume = "";
		}
		return anovolume;
	}

	public void setAnovolume(String anovolume) {
		this.anovolume = anovolume;
	}

	public String getEdicaoEspecial() {
		if(edicaoEspecial == null){
			edicaoEspecial = "";
		}
		return edicaoEspecial;
	}

	public void setEdicaoEspecial(String edicaoEspecial) {
		this.edicaoEspecial = edicaoEspecial;
	}

	public String getTituloExemplar() {
		if(tituloExemplar == null){
			tituloExemplar = "";
		}
		return tituloExemplar;
	}

	public void setTituloExemplar(String tituloExemplar) {
		this.tituloExemplar = tituloExemplar;
	}

	public String getEdicaoExemplar() {
		if(edicaoExemplar == null) {
			edicaoExemplar = "";
		}
		return edicaoExemplar;
	}

	public void setEdicaoExemplar(String edicaoExemplar) {
		this.edicaoExemplar = edicaoExemplar;
	}

	public String getNomeUnidadeEnsino() {
		if (nomeUnidadeEnsino == null) {
			nomeUnidadeEnsino = "";
		}
		return nomeUnidadeEnsino;
	}

	public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
		this.nomeUnidadeEnsino = nomeUnidadeEnsino;
	}

	public String getSiglaUnidadeEnsino() {
		if (siglaUnidadeEnsino == null) {
			siglaUnidadeEnsino = "";
		}
		return siglaUnidadeEnsino;
	}

	public void setSiglaUnidadeEnsino(String siglaUnidadeEnsino) {
		this.siglaUnidadeEnsino = siglaUnidadeEnsino;
	}

	public String getSiglaTipoCatalogo() {
		if (siglaTipoCatalogo == null) {
			siglaTipoCatalogo = "";
		}
		return siglaTipoCatalogo;
	}

	public void setSiglaTipoCatalogo(String siglaTipoCatalogo) {
		this.siglaTipoCatalogo = siglaTipoCatalogo;
	}

	public Integer getCodigoBiblioteca() {
		if (codigoBiblioteca == null) {
			codigoBiblioteca = 0;
		}
		return codigoBiblioteca;
	}

	public void setCodigoBiblioteca(Integer codigoBiblioteca) {
		this.codigoBiblioteca = codigoBiblioteca;
	}

	public String getAbreviacaoTitulo() {
		if (abreviacaoTitulo == null) {
			abreviacaoTitulo = "";
		}
		return abreviacaoTitulo;
	}

	public void setAbreviacaoTitulo(String abreviacaoTitulo) {
		this.abreviacaoTitulo = abreviacaoTitulo;
	}

    public String getAnoPublicacaoExemplar() {
        if (anoPublicacaoExemplar == null) {
            anoPublicacaoExemplar = "";
        }
        return (anoPublicacaoExemplar);
    }

    public void setAnoPublicacaoExemplar(String anoPublicacaoExemplar) {
        this.anoPublicacaoExemplar = anoPublicacaoExemplar;
    }

	public String getNrPaginasExemplar() {
		if (nrPaginasExemplar == null) {
			nrPaginasExemplar = "";
		}
		return nrPaginasExemplar;
	}

	public void setNrPaginasExemplar(String nrPaginasExemplar) {
		this.nrPaginasExemplar = nrPaginasExemplar;
	}

	public String getIsbnExemplar() {
		if (isbnExemplar == null) {
			isbnExemplar = "";
		}
		return isbnExemplar;
	}

	public void setIsbnExemplar(String isbnExemplar) {
		this.isbnExemplar = isbnExemplar;
	}

	public String getIssnExemplar() {
		if (issnExemplar == null) {
			issnExemplar = "";
		}
		return issnExemplar;
	}

	public void setIssnExemplar(String issnExemplar) {
		this.issnExemplar = issnExemplar;
	}
	
	public String getFasciculos() {
		if (fasciculos == null) {
			fasciculos = "";
		}
		return fasciculos;
	}

	public void setFasciculos(String fasciculos) {
		this.fasciculos = fasciculos;
	}
}
