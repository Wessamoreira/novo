/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.avaliacaoInst;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Rodrigo
 */
public class RespostaRelVO {

    protected String resposta;
    protected String nomeResposta;
    protected String nomeRespostaAdicional;
    protected String siglaResposta;
    protected Integer quantidadePessoa;
    protected Integer totalPessoas;
    protected Integer ordem;
    protected Integer agruparResposta;
	protected String listaRespostaAgrupadas;
	private Boolean apresentarRespota;
	private List<RespostaRelVO> listaRespostasAdicionais;
	private JRDataSource respostasAdicionaisJR;
	private Boolean apresentarRespostaAdicionais;
	private Boolean apresentarDescricaoResposta;
	private Boolean apresentarFiltroRespostaAdicionais;

    public RespostaRelVO() {
        incializarDados();
    }

    public void incializarDados() {
        setResposta("");
        setQuantidadePessoa(0);
        setNomeResposta("");
        setSiglaResposta("");
    }

    public Integer getQuantidadePessoa() {
        return quantidadePessoa;
    }

    public void setQuantidadePessoa(Integer quantidadePessoa) {
        this.quantidadePessoa = quantidadePessoa;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public String getNomeResposta() {
        return nomeResposta;
    }

    public void setNomeResposta(String nomeResposta) {
        this.nomeResposta = nomeResposta;
    }

    public String getSiglaResposta() {
        return siglaResposta;
    }

    public void setSiglaResposta(String siglaResposta) {
        this.siglaResposta = siglaResposta;
    }

    public Double getPercentual() {
        try {
            Double percentual = Uteis.truncar(((getQuantidadePessoa().doubleValue() * 100) / getTotalPessoas().doubleValue()),2);
            if(percentual == null || percentual == Double.NaN || percentual <0.0 && getQuantidadePessoa().intValue() == 0){
                return 0.0;
            }
            return percentual;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public Integer getTotalPessoas() {
        if (totalPessoas == null) {
            totalPessoas = 0;
        }
        return totalPessoas;
    }

    public void setTotalPessoas(Integer totalPessoas) {
        this.totalPessoas = totalPessoas;
    }

	public Boolean getApresentarRespota() {
		if (apresentarRespota == null) {
			apresentarRespota = Boolean.TRUE;
		}
		return apresentarRespota;
	}

	public void setApresentarRespota(Boolean apresentarRespota) {
		this.apresentarRespota = apresentarRespota;
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
	
	public Integer getAgruparResposta() {
		return agruparResposta;
	}

	public void setAgruparResposta(Integer agruparResposta) {
		this.agruparResposta = agruparResposta;
	}

	public String getNomeRespostaAdicional() {
		if (nomeRespostaAdicional == null) {
			nomeRespostaAdicional = "";
		}
		return nomeRespostaAdicional;
	}

	public void setNomeRespostaAdicional(String nomeRespostaAdicional) {
		this.nomeRespostaAdicional = nomeRespostaAdicional;
	}
	
	public String getListaRespostaAgrupadas() {
		if (listaRespostaAgrupadas == null) {
			listaRespostaAgrupadas = "";
		}
		return listaRespostaAgrupadas;
	}

	public void setListaRespostaAgrupadas(String listaRespostaAgrupadas) {
		this.listaRespostaAgrupadas = listaRespostaAgrupadas;
	}

	public List<RespostaRelVO> getListaRespostasAdicionais() {
		if (listaRespostasAdicionais == null) {
			listaRespostasAdicionais = new ArrayList<RespostaRelVO>();
		}
		return listaRespostasAdicionais;
	}

	public void setListaRespostasAdicionais(List<RespostaRelVO> listaRespostasAdicionais) {
		this.listaRespostasAdicionais = listaRespostasAdicionais;
	}

	public JRDataSource getRespostasAdicionaisJR() {
		if (respostasAdicionaisJR == null) {
			respostasAdicionaisJR = new JRBeanArrayDataSource(getListaRespostasAdicionais().toArray());
		}
		return respostasAdicionaisJR;
	}
	
	public void setRespostasAdicionaisJR(JRDataSource respostasAdicionaisJR) {
		this.respostasAdicionaisJR = respostasAdicionaisJR;
	}
	
	public Boolean getApresentarRespostaAdicionais() {
		return Uteis.isAtributoPreenchido(getListaRespostasAdicionais()) && !getListaRespostasAdicionais().isEmpty();
	}
	
	public void setApresentarRespostaAdicionais(Boolean apresentarRespostaAdicionais) {
		this.apresentarRespostaAdicionais = apresentarRespostaAdicionais;
	}
	
	public Boolean getApresentarDescricaoResposta() {
		if (apresentarDescricaoResposta == null) {
			apresentarDescricaoResposta = true;
		}
		return apresentarDescricaoResposta;
	}
	
	public void setApresentarDescricaoResposta(Boolean apresentarDescricaoResposta) {
		this.apresentarDescricaoResposta = apresentarDescricaoResposta;
	}
	
	public Boolean getApresentarFiltroRespostaAdicionais() {
		if (apresentarFiltroRespostaAdicionais == null) {
			apresentarFiltroRespostaAdicionais = true;
		}
		return apresentarFiltroRespostaAdicionais;
	}
	
	public void setApresentarFiltroRespostaAdicionais(Boolean apresentarFiltroRespostaAdicionais) {
		this.apresentarFiltroRespostaAdicionais = apresentarFiltroRespostaAdicionais;
	}
	
}
