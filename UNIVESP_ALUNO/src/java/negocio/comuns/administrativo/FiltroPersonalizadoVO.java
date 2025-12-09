package negocio.comuns.administrativo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.faces. model.SelectItem;

import negocio.comuns.administrativo.enumeradores.OrigemFiltroPersonalizadoEnum;
import negocio.comuns.administrativo.enumeradores.TagSubstituirValorDigitadoFiltroPersonalizadoEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoFiltroEnum;
import negocio.comuns.arquitetura.SuperVO;

public class FiltroPersonalizadoVO extends SuperVO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String tituloCampo;
	private String campoQuery;
	private String campoQueryListaCombobox;
	private TipoCampoFiltroEnum tipoCampoFiltro;
	private OrigemFiltroPersonalizadoEnum origem;
	private Integer codigoOrigem;
	private Integer ordem;
	private Integer coluna;
	private List<FiltroPersonalizadoOpcaoVO> listaFiltroPersonalizadoOpcaoVOs;
	private Boolean obrigatorio;
	
	//Transient
	private TagSubstituirValorDigitadoFiltroPersonalizadoEnum tagSubstituirValorDigitadoFiltroPersonalizado;
	private List<SelectItem> listaSelectItemComboboxCustomizavelVOs;
	private List<SelectItem> listaSelectItemComboboxVOs;
	private Boolean editando;
	private String valorCampoTexto;
	private Integer valorCampoInteiro;
	private Date valorCampoDataInicio;
	private Date valorCampoDataFim;
	private Boolean valorCampoBoolean;
	private Integer valorCampoFiltroCustomizavel;
	private String valorCampoCombobox;
	private Boolean comboboxManual;
	
	public FiltroPersonalizadoVO() {
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
	public String getValorCampoTexto() {
		if (valorCampoTexto == null) {
			valorCampoTexto = "";
		}
		return valorCampoTexto;
	}
	public void setValorCampoTexto(String valorCampoTexto) {
		this.valorCampoTexto = valorCampoTexto;
	}
	public Integer getValorCampoInteiro() {
		if (valorCampoInteiro == null) {
			valorCampoInteiro = 0;
		}
		return valorCampoInteiro;
	}
	public void setValorCampoInteiro(Integer valorCampoInteiro) {
		this.valorCampoInteiro = valorCampoInteiro;
	}
	public Date getValorCampoDataInicio() {
		return valorCampoDataInicio;
	}
	public void setValorCampoDataInicio(Date valorCampoDataInicio) {
		this.valorCampoDataInicio = valorCampoDataInicio;
	}
	public Date getValorCampoDataFim() {
		return valorCampoDataFim;
	}
	public void setValorCampoDataFim(Date valorCampoDataFim) {
		this.valorCampoDataFim = valorCampoDataFim;
	}
	public Boolean getValorCampoBoolean() {
		if (valorCampoBoolean == null) {
			valorCampoBoolean = false;
		}
		return valorCampoBoolean;
	}
	public void setValorCampoBoolean(Boolean valorCampoBoolean) {
		this.valorCampoBoolean = valorCampoBoolean;
	}
	public String getCampoQuery() {
		if (campoQuery == null) {
			campoQuery = "";
		}
		return campoQuery;
	}
	public void setCampoQuery(String campoQuery) {
		this.campoQuery = campoQuery;
	}
	public TipoCampoFiltroEnum getTipoCampoFiltro() {
		if (tipoCampoFiltro == null) {
			tipoCampoFiltro = TipoCampoFiltroEnum.TEXTUAL;
		}
		return tipoCampoFiltro;
	}
	public void setTipoCampoFiltro(TipoCampoFiltroEnum tipoCampoFiltro) {
		this.tipoCampoFiltro = tipoCampoFiltro;
	}
	public List<FiltroPersonalizadoOpcaoVO> getListaFiltroPersonalizadoOpcaoVOs() {
		if (listaFiltroPersonalizadoOpcaoVOs == null) {
			listaFiltroPersonalizadoOpcaoVOs = new ArrayList<FiltroPersonalizadoOpcaoVO>(0);
		}
		return listaFiltroPersonalizadoOpcaoVOs;
	}
	public void setListaFiltroPersonalizadoOpcaoVOs(List<FiltroPersonalizadoOpcaoVO> listaFiltroPersonalizadoOpcaoVOs) {
		this.listaFiltroPersonalizadoOpcaoVOs = listaFiltroPersonalizadoOpcaoVOs;
	}

	public OrigemFiltroPersonalizadoEnum getOrigem() {
		if (origem == null) {
			origem = OrigemFiltroPersonalizadoEnum.SEI_DECIDIR;
		}
		return origem;
	}

	public void setOrigem(OrigemFiltroPersonalizadoEnum origem) {
		this.origem = origem;
	}

	public Integer getCodigoOrigem() {
		if (codigoOrigem == null) {
			codigoOrigem = 0;
		}
		return codigoOrigem;
	}

	public void setCodigoOrigem(Integer codigoOrigem) {
		this.codigoOrigem = codigoOrigem;
	}

	public String getTituloCampo() {
		if (tituloCampo == null) {
			tituloCampo = "";
		}
		return tituloCampo;
	}

	public void setTituloCampo(String tituloCampo) {
		this.tituloCampo = tituloCampo;
	}

	public TagSubstituirValorDigitadoFiltroPersonalizadoEnum getTagSubstituirValorDigitadoFiltroPersonalizado() {
		if (tagSubstituirValorDigitadoFiltroPersonalizado == null) {
			tagSubstituirValorDigitadoFiltroPersonalizado = TagSubstituirValorDigitadoFiltroPersonalizadoEnum.TAG_VALOR_CAMPO;
		}
		return tagSubstituirValorDigitadoFiltroPersonalizado;
	}

	public void setTagSubstituirValorDigitadoFiltroPersonalizado(
			TagSubstituirValorDigitadoFiltroPersonalizadoEnum tagSubstituirValorDigitadoFiltroPersonalizado) {
		this.tagSubstituirValorDigitadoFiltroPersonalizado = tagSubstituirValorDigitadoFiltroPersonalizado;
	}
	
	public Boolean getTipoCampoMultiplaEscolha() {
        if (getTipoCampoFiltro().equals(TipoCampoFiltroEnum.MULTIPLA_ESCOLHA)) {
            return true;
        }
        return false;
    }
	
	public Boolean getTipoCampoSimplesEscolha() {
        if (getTipoCampoFiltro().equals(TipoCampoFiltroEnum.SIMPLES_ESCOLHA)) {	
            return true;
        }
        return false;
    }

	public String getCssFiltroOpcoes() {
    	if(getListaFiltroPersonalizadoOpcaoVOs().size() <= 2) {
    		return "col-md-6";
    	}else if(getListaFiltroPersonalizadoOpcaoVOs().size() == 3){
    		return "col-md-4";
    	} 
    	return "col-md-3";
    }
	
	public Integer getNumeroOpcoes(){
		return getListaFiltroPersonalizadoOpcaoVOs().size();
	}

	public String getTagSubstituirCampo() {
		return TagSubstituirValorDigitadoFiltroPersonalizadoEnum.TAG_VALOR_CAMPO.name();
	}
	
	public String getTagSubstituirCampoDataInicio() {
		return TagSubstituirValorDigitadoFiltroPersonalizadoEnum.TAG_DATA_INICIO.name();
	}
	
	public String getTagSubstituirCampoDataFim() {
		return TagSubstituirValorDigitadoFiltroPersonalizadoEnum.TAG_DATA_FIM.name();
	}
	
	public List<SelectItem> getListaSelectItemComboboxCustomizavelVOs() {
		if (listaSelectItemComboboxCustomizavelVOs == null) {
			listaSelectItemComboboxCustomizavelVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemComboboxCustomizavelVOs;
	}

	public void setListaSelectItemComboboxCustomizavelVOs(List<SelectItem> listaSelectItemComboboxCustomizavelVOs) {
		this.listaSelectItemComboboxCustomizavelVOs = listaSelectItemComboboxCustomizavelVOs;
	}

	public Boolean getEditando() {
		if (editando == null) {
			editando = false;
		}
		return editando;
	}

	public void setEditando(Boolean editando) {
		this.editando = editando;
	}

	public Integer getValorCampoFiltroCustomizavel() {
		if (valorCampoFiltroCustomizavel == null) {
			valorCampoFiltroCustomizavel = 0;
		}
		return valorCampoFiltroCustomizavel;
	}

	public void setValorCampoFiltroCustomizavel(Integer valorCampoFiltroCustomizavel) {
		this.valorCampoFiltroCustomizavel = valorCampoFiltroCustomizavel;
	}

	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 0;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}
	
	public String getColunaCSS() {
		return "col-md-" + getColuna().toString();
	}

	public Integer getColuna() {
		if (coluna == null) {
			coluna = 12;
		}
		return coluna;
	}

	public void setColuna(Integer coluna) {
		this.coluna = coluna;
	}

	public String getCampoQueryListaCombobox() {
		if (campoQueryListaCombobox == null) {
			campoQueryListaCombobox = "";
		}
		return campoQueryListaCombobox;
	}

	public void setCampoQueryListaCombobox(String campoQueryListaCombobox) {
		this.campoQueryListaCombobox = campoQueryListaCombobox;
	}

	public String getValorCampoCombobox() {
		if (valorCampoCombobox == null) {
			valorCampoCombobox = "";
		}
		return valorCampoCombobox;
	}

	public void setValorCampoCombobox(String valorCampoCombobox) {
		this.valorCampoCombobox = valorCampoCombobox;
	}

	public Boolean getComboboxManual() {
		if (comboboxManual == null) {
			comboboxManual = false;
		}
		return comboboxManual;
	}

	public void setComboboxManual(Boolean comboboxManual) {
		this.comboboxManual = comboboxManual;
	}

	public Boolean getApresentarFiltroOpcao() {
		return getTipoCampoFiltro().equals(TipoCampoFiltroEnum.MULTIPLA_ESCOLHA) 
				|| getTipoCampoFiltro().equals(TipoCampoFiltroEnum.SIMPLES_ESCOLHA) 
				|| getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX_CUSTOMIZAVEL) 
				|| (getTipoCampoFiltro().equals(TipoCampoFiltroEnum.COMBOBOX) && getComboboxManual());
	}

	public List<SelectItem> getListaSelectItemComboboxVOs() {
		if (listaSelectItemComboboxVOs == null) {
			listaSelectItemComboboxVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemComboboxVOs;
	}

	public void setListaSelectItemComboboxVOs(List<SelectItem> listaSelectItemComboboxVOs) {
		this.listaSelectItemComboboxVOs = listaSelectItemComboboxVOs;
	}

	public Boolean getObrigatorio() {
		if (obrigatorio == null) {
			obrigatorio = false;
		}
		return obrigatorio;
	}

	public void setObrigatorio(Boolean obrigatorio) {
		this.obrigatorio = obrigatorio;
	}
	
	public String getStyleClass() {
		if (getObrigatorio()) {
			return "form-control camposObrigatorios";
		}
		return "form-control campos";
	}
	
}
