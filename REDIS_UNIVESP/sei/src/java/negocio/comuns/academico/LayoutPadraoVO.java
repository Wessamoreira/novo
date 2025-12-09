package negocio.comuns.academico;

import java.util.HashMap;
import java.util.Map;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Constantes;

public class LayoutPadraoVO extends SuperVO {

	private Integer codigo;
    private String entidade;
    private String campo;
    private String valor;
    private Integer assinaturaFunc1;
    private Integer assinaturaFunc2;
    private Integer assinaturaFunc3;
    private Integer assinaturaFunc4;
    private Integer assinaturaFunc5;
    private Boolean apresentarTopoRelatorio;
    private String tituloAssinaturaFunc1;
    private String tituloAssinaturaFunc2;
    private String tituloAssinaturaFunc3;
    private String tituloAssinaturaFunc4;
    private String tituloAssinaturaFunc5;
    private String observacaoComplementarIntegralizado;
    private String textoCertidaoEstudo;
    private String agrupador;
    private String tituloRelatorio;
    private Map<String, String> mapCampoValores;
    private String nomeCargo1Apresentar;
    private String nomeCargo2Apresentar;
    private String nomeCargo3Apresentar;
    private String nomeCargo4Apresentar;
    private String nomeCargo5Apresentar;
    private Integer usuario;
	
	public static final long serialVersionUID = 1L;

    public LayoutPadraoVO() {
    }

    public String getEntidade() {
        if (entidade == null) {
            entidade = "";
        }
        return entidade;
    }

    public void setEntidade(String entidade) {
        this.entidade = entidade;
    }

    public String getCampo() {
        if (campo == null) {
            campo = "";
        }
        return campo;
    }

    public void setCampo(String campo) {
        this.campo = campo;
    }

    public String getValor() {
        if (valor == null) {
            valor = "";
        }
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

	public Integer getAssinaturaFunc1() {
		if (assinaturaFunc1 == null) {
			assinaturaFunc1 = 0;
        }
		return assinaturaFunc1;
	}

	public void setAssinaturaFunc1(Integer assinaturaFunc1) {
		this.assinaturaFunc1 = assinaturaFunc1;
	}

	public Integer getAssinaturaFunc2() {
		if (assinaturaFunc2 == null) {
			assinaturaFunc2 = 0;
        }
		return assinaturaFunc2;
	}

	public void setAssinaturaFunc2(Integer assinaturaFunc2) {
		this.assinaturaFunc2 = assinaturaFunc2;
	}
	
	public Integer getAssinaturaFunc3() {
		if (assinaturaFunc3 == null) {
			assinaturaFunc3 = 0;
        }
		return assinaturaFunc3;
	}

	public void setAssinaturaFunc3(Integer assinaturaFunc3) {
		this.assinaturaFunc3 = assinaturaFunc3;
	}

	public Boolean getApresentarTopoRelatorio() {
		if (apresentarTopoRelatorio == null) {
			apresentarTopoRelatorio = Boolean.FALSE;
		}
		return apresentarTopoRelatorio;
	}

	public void setApresentarTopoRelatorio(Boolean apresentarTopoRelatorio) {
		this.apresentarTopoRelatorio = apresentarTopoRelatorio;
	}
	
    public String getTituloAssinaturaFunc1() {
    	if (tituloAssinaturaFunc1 == null) {
    		tituloAssinaturaFunc1 = "";
    	}
		return tituloAssinaturaFunc1;
	}

	public void setTituloAssinaturaFunc1(String tituloAssinaturaFunc1) {
		this.tituloAssinaturaFunc1 = tituloAssinaturaFunc1;
	}

	public String getTituloAssinaturaFunc2() {
    	if (tituloAssinaturaFunc2 == null) {
    		tituloAssinaturaFunc2 = "";
    	}
		return tituloAssinaturaFunc2;
	}

	public void setTituloAssinaturaFunc2(String tituloAssinaturaFunc2) {
		this.tituloAssinaturaFunc2 = tituloAssinaturaFunc2;
	}
	
	public String getTituloAssinaturaFunc3() {
    	if (tituloAssinaturaFunc3 == null) {
    		tituloAssinaturaFunc3 = "";
    	}
		return tituloAssinaturaFunc3;
	}

	public void setTituloAssinaturaFunc3(String tituloAssinaturaFunc3) {
		this.tituloAssinaturaFunc3 = tituloAssinaturaFunc3;
	}

	public String getObservacaoComplementarIntegralizado() {
		if (observacaoComplementarIntegralizado == null) {
			observacaoComplementarIntegralizado = "";
		}
		return observacaoComplementarIntegralizado;
	}

	public void setObservacaoComplementarIntegralizado(String observacaoComplementarIntegralizado) {
		this.observacaoComplementarIntegralizado = observacaoComplementarIntegralizado;
	}
	
	public String getTextoCertidaoEstudo() {
		if (textoCertidaoEstudo == null) {
			textoCertidaoEstudo = "";
		}
		return textoCertidaoEstudo;
	}

	public void setTextoCertidaoEstudo(String textoCertidaoEstudo) {
		this.textoCertidaoEstudo = textoCertidaoEstudo;
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
	

	public String getAgrupador() {
		if(agrupador == null){
			agrupador = "";
		}
		return agrupador;
	}

	public void setAgrupador(String agrupador) {
		this.agrupador = agrupador;
	}

	public Map<String, String> getMapCampoValores() {
		if(mapCampoValores == null){
			mapCampoValores = new HashMap<String,String>(0);
		}
		return mapCampoValores;
	}

	public void setMapCampoValores(Map<String, String> mapCampoValores) {
		this.mapCampoValores = mapCampoValores;
	}

	public String getNomeCargo1Apresentar() {
		if (nomeCargo1Apresentar == null) {
			nomeCargo1Apresentar = "";
		}
		return nomeCargo1Apresentar;
	}

	public void setNomeCargo1Apresentar(String nomeCargo1Apresentar) {
		this.nomeCargo1Apresentar = nomeCargo1Apresentar;
	}

	public String getNomeCargo2Apresentar() {
		if (nomeCargo2Apresentar == null) {
			nomeCargo2Apresentar = "";
		}
		return nomeCargo2Apresentar;
	}

	public void setNomeCargo2Apresentar(String nomeCargo2Apresentar) {
		this.nomeCargo2Apresentar = nomeCargo2Apresentar;
	}

	public String getTituloRelatorio() {
		if (tituloRelatorio == null) {
			tituloRelatorio = "";
		}
		return tituloRelatorio;
	}

	public void setTituloRelatorio(String tituloRelatorio) {
		this.tituloRelatorio = tituloRelatorio;
	}

	public Integer getAssinaturaFunc4() {
		if (assinaturaFunc4 == null) {
			assinaturaFunc4 = 0;
		}
		return assinaturaFunc4;
	}

	public void setAssinaturaFunc4(Integer assinaturaFunc4) {
		this.assinaturaFunc4 = assinaturaFunc4;
	}

	public Integer getAssinaturaFunc5() {
		if (assinaturaFunc5 == null) {
			assinaturaFunc5 = 0;
		}
		return assinaturaFunc5;
	}

	public void setAssinaturaFunc5(Integer assinaturaFunc5) {
		this.assinaturaFunc5 = assinaturaFunc5;
	}

	public String getTituloAssinaturaFunc4() {
		if (tituloAssinaturaFunc4 == null) {
			tituloAssinaturaFunc4 = Constantes.EMPTY;
		}
		return tituloAssinaturaFunc4;
	}

	public void setTituloAssinaturaFunc4(String tituloAssinaturaFunc4) {
		this.tituloAssinaturaFunc4 = tituloAssinaturaFunc4;
	}

	public String getTituloAssinaturaFunc5() {
		if (tituloAssinaturaFunc5 == null) {
			tituloAssinaturaFunc5 = Constantes.EMPTY;
		}
		return tituloAssinaturaFunc5;
	}

	public void setTituloAssinaturaFunc5(String tituloAssinaturaFunc5) {
		this.tituloAssinaturaFunc5 = tituloAssinaturaFunc5;
	}

	public String getNomeCargo3Apresentar() {
		if (nomeCargo3Apresentar == null) {
			nomeCargo3Apresentar = Constantes.EMPTY;
		}
		return nomeCargo3Apresentar;
	}

	public void setNomeCargo3Apresentar(String nomeCargo3Apresentar) {
		this.nomeCargo3Apresentar = nomeCargo3Apresentar;
	}

	public String getNomeCargo4Apresentar() {
		if (nomeCargo4Apresentar == null) {
			nomeCargo4Apresentar = Constantes.EMPTY;
		}
		return nomeCargo4Apresentar;
	}

	public void setNomeCargo4Apresentar(String nomeCargo4Apresentar) {
		this.nomeCargo4Apresentar = nomeCargo4Apresentar;
	}

	public String getNomeCargo5Apresentar() {
		if (nomeCargo5Apresentar == null) {
			nomeCargo5Apresentar = Constantes.EMPTY;
		}
		return nomeCargo5Apresentar;
	}

	public void setNomeCargo5Apresentar(String nomeCargo5Apresentar) {
		this.nomeCargo5Apresentar = nomeCargo5Apresentar;
	}

	public Integer getUsuario() {
		if (usuario == null) {
			usuario = 0;
		}
		return usuario;
	}

	public void setUsuario(Integer usuario) {
		this.usuario = usuario;
	}
}
