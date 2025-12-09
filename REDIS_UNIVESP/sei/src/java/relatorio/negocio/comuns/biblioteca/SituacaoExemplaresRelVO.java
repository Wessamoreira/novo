package relatorio.negocio.comuns.biblioteca;

import java.io.Serializable;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.biblioteca.ExemplarVO;

public class SituacaoExemplaresRelVO extends SuperVO implements Serializable {

    private String secao;
    private String nivelBibliografico;
    private String catalogo;
    private String colunaExemplar;
    private String situacao;
    private UnidadeEnsinoVO unidadeEnsino;
    private ExemplarVO exemplar;
    private Integer tipo;

    public String getSecao() {
        if (secao == null) {
            secao = "";
        }
        return secao;
    }

    public void setSecao(String secao) {
        this.secao = secao;
    }

    public String getNivelBibliografico() {
        if (nivelBibliografico == null) {
            nivelBibliografico = "";
        }
        return nivelBibliografico;
    }

    public void setNivelBibliografico(String nivelBibliografico) {
        this.nivelBibliografico = nivelBibliografico;
    }

    public String getCatalogo() {
        if (catalogo == null) {
            catalogo = "";
        }
        return catalogo;
    }

    public void setCatalogo(String catalogo) {
        this.catalogo = catalogo;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public ExemplarVO getExemplar() {
        if (exemplar == null) {
            exemplar = new ExemplarVO();
        }
        return exemplar;
    }

    public void setExemplar(ExemplarVO exemplar) {
        this.exemplar = exemplar;
    }

    public String getColunaExemplar() {
        if (colunaExemplar == null) {
            colunaExemplar = "";
        }
        return colunaExemplar;
    }

    public void setColunaExemplar(String colunaExemplar) {
        this.colunaExemplar = colunaExemplar;
    }

	public Integer getTipo() {
		if(tipo == null){
			tipo = 0;
		}
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
}
