package negocio.comuns.administrativo;

/**
 * @author Leonardo Riciolle 19/11/2014
 */

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlTransient;

import negocio.comuns.administrativo.enumeradores.RelatorioSEIDecidirTipoTotalizadorEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.interfaces.administrativo.PerfilTagSEIDecidirEnum;
import relatorio.negocio.comuns.arquitetura.enumeradores.CrossTabEnum;

@XmlRootElement(name = "layoutCampo")
public class LayoutRelatorioSeiDecidirCampoVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	@XmlTransient
	private LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO;
	@XmlTransient
	private Integer codigo;
	private String titulo;
	private String campo;
	private Integer tamanhoColuna;
	private Integer qtdCasasDecimais;
	private RelatorioSEIDecidirTipoTotalizadorEnum tipoTotalizador;
	private String textoTotalizador;
	private Integer ordemApresentacao;
	private Boolean utilizarGroupBy;
	private Boolean utilizarOrderBy;
	private Boolean ordenarDescrecente;
	private Boolean utilizarParametroRelatorio;
	private CrossTabEnum valorCrosstab;
	/**
	 * Variavel Transient
	 * 
	 * @return
	 */	
	private TipoCampoEnum tipoCampo;
	@XmlTransient
	private List<PerfilTagSEIDecidirEnum> tagUtilizadaEnum;
	@XmlTransient
	private String campoTagSubstituida;
	@XmlTransient
	private Double somaColuna;
	@XmlTransient
	private Integer qtdeColuna;
	
	@XmlElement(name = "titulo")
	public String getTitulo() {
		if (titulo == null) {
			titulo = "";
		}
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	@XmlElement(name = "campo")
	public String getCampo() {
		if (campo == null) {
			campo = "";
		}
		return campo;
	}

	public void setCampo(String campo) {
		this.campo = campo;
	}

	@XmlElement(name = "tamanhoColuna")
	public Integer getTamanhoColuna() {
		if (tamanhoColuna == null) {
			tamanhoColuna = 0;
		}
		return tamanhoColuna;
	}

	public void setTamanhoColuna(Integer tamanhoColuna) {
		this.tamanhoColuna = tamanhoColuna;
	}

	@XmlElement(name = "tipoCampo")
	public TipoCampoEnum getTipoCampo() {
		if (tipoCampo == null) {
			tipoCampo = TipoCampoEnum.TEXTO;
		}
		return tipoCampo;
	}

	public void setTipoCampo(TipoCampoEnum tipoCampo) {
		this.tipoCampo = tipoCampo;
	}
	
	@XmlElement(name = "qtdCasasDecimais")
	public Integer getQtdCasasDecimais() {
		if (qtdCasasDecimais == null) {
			qtdCasasDecimais = 2;
		}
		return qtdCasasDecimais;
	}

	public void setQtdCasasDecimais(Integer qtdCasasDecimais) {
		this.qtdCasasDecimais = qtdCasasDecimais;
	}

	@XmlElement(name = "tipoTotalizador")
	public RelatorioSEIDecidirTipoTotalizadorEnum getTipoTotalizador() {
		if (tipoTotalizador == null) {
			tipoTotalizador = RelatorioSEIDecidirTipoTotalizadorEnum.NENHUM;
		}
		return tipoTotalizador;
	}

	public void setTipoTotalizador(RelatorioSEIDecidirTipoTotalizadorEnum tipoTotalizador) {
		this.tipoTotalizador = tipoTotalizador;
	}

	@XmlElement(name = "textoTotalizador")
	public String getTextoTotalizador() {
		if (textoTotalizador == null) {
			textoTotalizador = "";
		}
		return textoTotalizador;
	}

	public void setTextoTotalizador(String textoTotalizador) {
		this.textoTotalizador = textoTotalizador;
	}
	@XmlTransient
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@XmlTransient
	public LayoutRelatorioSEIDecidirVO getLayoutRelatorioSEIDecidirVO() {
		if (layoutRelatorioSEIDecidirVO == null) {
			layoutRelatorioSEIDecidirVO = new LayoutRelatorioSEIDecidirVO();
		}
		return layoutRelatorioSEIDecidirVO;
	}

	public void setLayoutRelatorioSEIDecidirVO(LayoutRelatorioSEIDecidirVO layoutRelatorioSEIDecidirVO) {
		this.layoutRelatorioSEIDecidirVO = layoutRelatorioSEIDecidirVO;
	}

	
	@XmlElement(name = "ordemApresentacao")
	public Integer getOrdemApresentacao() {
		if (ordemApresentacao == null) {
			ordemApresentacao = 0;
		}
		return ordemApresentacao;
	}

	public void setOrdemApresentacao(Integer ordemApresentacao) {
		this.ordemApresentacao = ordemApresentacao;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((getOrdemApresentacao() == null) ? 0 : getOrdemApresentacao().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LayoutRelatorioSeiDecidirCampoVO other = (LayoutRelatorioSeiDecidirCampoVO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (getOrdemApresentacao() == null) {
			if (other.getOrdemApresentacao() != null)
				return false;
		} else if (!getOrdemApresentacao().equals(other.getOrdemApresentacao()))
			return false;
		return true;		
	}
	
	@XmlTransient
	public List<PerfilTagSEIDecidirEnum> getTagUtilizadaEnum() {
		if (tagUtilizadaEnum == null) {
			tagUtilizadaEnum = new ArrayList<PerfilTagSEIDecidirEnum>(0);
		}
		return tagUtilizadaEnum;
	}

	public void setTagUtilizadaEnum(List<PerfilTagSEIDecidirEnum> tagUtilizadaEnum) {
		this.tagUtilizadaEnum = tagUtilizadaEnum;
	}
	@XmlTransient
	public String getCampoTagSubstituida() {
		if (campoTagSubstituida == null) {
			campoTagSubstituida = getCampo();
		}
		return campoTagSubstituida;
	}

	public void setCampoTagSubstituida(String campoTagSubstituida) {
		this.campoTagSubstituida = campoTagSubstituida;
	}
	@XmlTransient
	public String getAlias(){
		return "campo"+getOrdemApresentacao();
	}

	@XmlElement(name = "utilizarGroupBy")
	public Boolean getUtilizarGroupBy() {
		if (utilizarGroupBy == null) {
			utilizarGroupBy = true;
		}
		return utilizarGroupBy;
	}

	public void setUtilizarGroupBy(Boolean utilizarGroupBy) {
		this.utilizarGroupBy = utilizarGroupBy;
	}

	@XmlElement(name = "utilizarOrderBy")
	public Boolean getUtilizarOrderBy() {
		if (utilizarOrderBy == null) {
			utilizarOrderBy = true;
		}
		return utilizarOrderBy;
	}

	public void setUtilizarOrderBy(Boolean utilizarOrderBy) {
		this.utilizarOrderBy = utilizarOrderBy;
	}
	@XmlTransient
	public Double getSomaColuna() {
		if (somaColuna == null) {
			somaColuna = 0.0;
		}
		return somaColuna;
	}

	public void setSomaColuna(Double somaColuna) {
		this.somaColuna = somaColuna;
	}

	@XmlTransient
	public Integer getQtdeColuna() {
		if (qtdeColuna == null) {
			qtdeColuna = 0;
		}
		return qtdeColuna;
	}

	public void setQtdeColuna(Integer qtdeColuna) {
		this.qtdeColuna = qtdeColuna;
	}
	
	@XmlTransient
	public boolean getIsApresentarQtdCasasDecimais() {
		return getTipoCampo().equals(TipoCampoEnum.DOUBLE);
	}

	@XmlTransient
	public boolean getIsApresentarTextoTotalizador() {
		return getTipoTotalizador().equals(RelatorioSEIDecidirTipoTotalizadorEnum.TEXTO);
	}

	@XmlElement(name = "utilizarParametroRelatorio")
	public Boolean getUtilizarParametroRelatorio() {
		if (utilizarParametroRelatorio == null) {
			utilizarParametroRelatorio = Boolean.FALSE;
		}
		return utilizarParametroRelatorio;
	}

	public void setUtilizarParametroRelatorio(Boolean utilizarParametroRelatorio) {
		this.utilizarParametroRelatorio = utilizarParametroRelatorio;
	}

	public CrossTabEnum getValorCrosstab() {
		return valorCrosstab;
	}

	public void setValorCrosstab(CrossTabEnum valorCrosstab) {
		this.valorCrosstab = valorCrosstab;
	}

	@XmlElement(name = "ordenarDescrecente")
	public Boolean getOrdenarDescrecente() {
		if(ordenarDescrecente == null) {
			ordenarDescrecente =  false;
}
		return ordenarDescrecente;
	}

	public void setOrdenarDescrecente(Boolean ordenarDescrecente) {
		this.ordenarDescrecente = ordenarDescrecente;
	}
	
	
}
