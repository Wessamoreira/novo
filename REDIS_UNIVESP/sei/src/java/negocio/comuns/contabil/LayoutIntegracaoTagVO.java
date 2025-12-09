package negocio.comuns.contabil;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.contabil.enumeradores.TipoCampoTagEnum;
import negocio.comuns.contabil.enumeradores.TipoTagEnum;
import negocio.comuns.utilitarias.Uteis;

/**
 * 
 * @author PedroOtimize
 *
 */
public class LayoutIntegracaoTagVO extends SuperVO implements Comparable<LayoutIntegracaoTagVO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -752030693274868022L;
	private Integer codigo;
	private LayoutIntegracaoVO layoutIntegracaoVO;
	private LayoutIntegracaoTagVO tagMae;
	private String tag;
	private TipoTagEnum tipoTagEnum;
	private String campo;
	private String mascara;
	private String removerMascara;
	private String separadorMonetaria;
	private boolean suprimirZeroEsquerda = false;
	private TipoCampoTagEnum tipoCampoTagEnum;
	private Integer tamanhoTag;
	private String nivel;
	private List<? extends SuperVO> listaGenerica;

	public LayoutIntegracaoVO getLayoutIntegracaoVO() {
		if (layoutIntegracaoVO == null) {
			layoutIntegracaoVO = new LayoutIntegracaoVO();
		}
		return layoutIntegracaoVO;
	}

	public void setLayoutIntegracaoVO(LayoutIntegracaoVO layoutIntegracaoContabilVO) {
		this.layoutIntegracaoVO = layoutIntegracaoContabilVO;
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

	public LayoutIntegracaoTagVO getTagMae() {
		if (tagMae == null) {
			tagMae = new LayoutIntegracaoTagVO();
		}
		return tagMae;
	}

	public void setTagMae(LayoutIntegracaoTagVO tagMae) {
		this.tagMae = tagMae;
	}

	public String getTag() {
		if (tag == null) {
			tag = "";
		}
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTipoTagEnum_Apresentar() {
		if (Uteis.isAtributoPreenchido(getTipoTagEnum())) {
			return getTipoTagEnum().getDescricao();
		}
		return "";
	}

	public TipoTagEnum getTipoTagEnum() {
		return tipoTagEnum;
	}

	public void setTipoTagEnum(TipoTagEnum tipoValorTagEnum) {
		this.tipoTagEnum = tipoValorTagEnum;
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

	public String getTipoCampoTagEnum_Apresentar() {
		if (Uteis.isAtributoPreenchido(getTipoCampoTagEnum())) {
			return getTipoCampoTagEnum().getDescricao();
		}
		return "";
	}

	public TipoCampoTagEnum getTipoCampoTagEnum() {
		return tipoCampoTagEnum;
	}

	public void setTipoCampoTagEnum(TipoCampoTagEnum tipoCampoTagEnum) {
		this.tipoCampoTagEnum = tipoCampoTagEnum;
	}

	public Integer getTamanhoTag() {
		if (tamanhoTag == null) {
			tamanhoTag = 0;
		}
		return tamanhoTag;
	}

	public void setTamanhoTag(Integer tamanhoTag) {
		this.tamanhoTag = tamanhoTag;
	}

	public String getNivel() {
		if (nivel == null) {
			nivel = "";
		}
		return nivel;
	}

	public void setNivel(String nivel) {
		this.nivel = nivel;
	}

	public String getMascara() {
		if (mascara == null) {
			mascara = "";
		}
		return mascara;
	}

	public void setMascara(String mascara) {
		this.mascara = mascara;
	}
	
	public String getRemoverMascara() {
		if (removerMascara == null) {
			removerMascara = "";
		}
		return removerMascara;
	}

	public void setRemoverMascara(String removerMascara) {
		this.removerMascara = removerMascara;
	}

	public String getSeparadorMonetaria() {
		if (separadorMonetaria == null) {
			separadorMonetaria = "";
		}
		return separadorMonetaria;
	}

	public void setSeparadorMonetaria(String separadorMonetaria) {
		this.separadorMonetaria = separadorMonetaria;
	}
	
	public boolean isSuprimirZeroEsquerda() {
		return suprimirZeroEsquerda;
	}

	public void setSuprimirZeroEsquerda(boolean suprimirZeroEsquerda) {
		this.suprimirZeroEsquerda = suprimirZeroEsquerda;
	}

	public List<? extends SuperVO> getListaGenerica() {
		if (listaGenerica == null) {
			listaGenerica = new ArrayList<>();
		}
		return listaGenerica;
	}

	public void setListaGenerica(List<? extends SuperVO> listaGenerica) {
		this.listaGenerica = listaGenerica;
	}

	public Integer getNumeroNivel() {
		return StringUtils.countMatches(getNivel(), ".");
	}

	public boolean isApresentaTagMae() {
		return Uteis.isAtributoPreenchido(getTipoTagEnum()) && (getTipoTagEnum().isCampo() || getTipoTagEnum().isFixo() || getTipoTagEnum().isTag() || getTipoTagEnum().isTagList() || getTipoTagEnum().isTagFormula());
	}

	public boolean equalsCampoSelecaoLista(LayoutIntegracaoTagVO obj) {
		if (getNivel().equals(obj.getNivel())) {
			return true;
		}
		return false;
	}
	
	

	@Override
	public int compareTo(LayoutIntegracaoTagVO obj) {
		// Define os arrays de elementos para comparação
		String[] comp1 = getNivel().split("\\.");
		String[] comp2 = obj.getNivel().split("\\.");
		// Identificamos o menor comprimento dos arrays
		Integer tamanho = (comp1.length + comp2.length - Math.abs(comp1.length - comp2.length)) / 2;
		for (int i = 0; i < tamanho; i++) {
			Integer a1 = Integer.parseInt(comp1[i]);
			Integer a2 = Integer.parseInt(comp2[i]);
			if (a1 < a2) {
				return -1;
			} else if (a1 > a2) {
				return +1;
			}
		}
		if (comp1.length - comp2.length < 0) {
			return -1;
		} else {
			return 1;
		}
	}

}
