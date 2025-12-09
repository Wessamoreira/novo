package negocio.comuns.basico;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.enumeradores.TagEtiquetaEnum;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

@XmlRootElement(name = "layoutCampo")
public class LayoutEtiquetaTagVO extends SuperVO {

	@XmlTransient
	private static final long serialVersionUID = -2501620496981624917L;
	@XmlTransient
	private Integer codigo;
	private TagEtiquetaEnum tagEtiqueta;
	private String labelTag;
	private Integer margemDireita;
	private Integer margemTopo;
	private Boolean rotacao;
	private Integer tamanhoFonte;
	private Boolean cor;
	@XmlTransient
	private LayoutEtiquetaVO layoutEtiquetaVO;
	private String valorTextoPreview;
	private Integer larguraCodigoBarra;
	private Integer alturaCodigoBarra;
	private Boolean imprimirNumeroAbaixo;
	private Integer ordem;
	private Boolean apresentarLabelEtiquetaAposTagEtiqueta;
	@XmlTransient
	private String nomeArquivo;
	@XmlTransient
	private String nomeArquivoApresentar;
	@XmlTransient
	private PastaBaseArquivoEnum pastaBaseArquivo;
	/**
	 * Transient
	 */
	@XmlTransient
	private String urlImagem;
	@XmlTransient
	private String nomeArquivoAnt;
	private String corTexto;
	private Boolean fontNegrito;
	private Integer alturaFoto;
	private Integer larguraFoto;
	private String urlImagemApresentar;

	/**
	 * Fim Transient
	 */
	@XmlElement(name = "larguraCodigoBarra")
	public Integer getLarguraCodigoBarra() {
		if (larguraCodigoBarra == null) {
			larguraCodigoBarra = 0;
		}
		return larguraCodigoBarra;
	}

	public void setLarguraCodigoBarra(Integer larguraCodigoBarra) {
		this.larguraCodigoBarra = larguraCodigoBarra;
	}

	@XmlElement(name = "alturaCodigoBarra")
	public Integer getAlturaCodigoBarra() {
		if (alturaCodigoBarra == null) {
			alturaCodigoBarra = 0;
		}
		return alturaCodigoBarra;
	}

	public void setAlturaCodigoBarra(Integer alturaCodigoBarra) {
		this.alturaCodigoBarra = alturaCodigoBarra;
	}

	@XmlTransient
	public Boolean getMostrarItensCodigoBarra() {		
		return getTagEtiqueta().equals(TagEtiquetaEnum.BIB_CODIGO_BARRAS) || getTagEtiqueta().equals(TagEtiquetaEnum.TUR_DIS_PROF_COD_BARRA);
	}
	@XmlElement(name = "valorTextoPreview")
	public String getValorTextoPreview() {
		if (valorTextoPreview == null) {
			valorTextoPreview = "";
		}
		return valorTextoPreview;
	}

	public void setValorTextoPreview(String valorTextoPreview) {
		this.valorTextoPreview = valorTextoPreview;
	}

	@XmlElement(name = "tagEtiqueta")
	public TagEtiquetaEnum getTagEtiqueta() {
		if (tagEtiqueta == null) {
			tagEtiqueta = TagEtiquetaEnum.NOME_PESSOA;
		}
		return tagEtiqueta;
	}

	public void setTagEtiqueta(TagEtiquetaEnum tagEtiqueta) {
		this.tagEtiqueta = tagEtiqueta;

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
	public LayoutEtiquetaVO getLayoutEtiquetaVO() {
		if (layoutEtiquetaVO == null) {
			layoutEtiquetaVO = new LayoutEtiquetaVO();
		}
		return layoutEtiquetaVO;
	}

	public void setLayoutEtiquetaVO(LayoutEtiquetaVO layoutEtiquetaVO) {
		this.layoutEtiquetaVO = layoutEtiquetaVO;
	}
	@XmlElement(name = "labelTag")
	public String getLabelTag() {
		if (labelTag == null) {
			labelTag = "";
		}
		return labelTag;
	}

	public void setLabelTag(String labelTag) {
		this.labelTag = labelTag;
	}

	@XmlElement(name = "margemDireita")
	public Integer getMargemDireita() {
		if (margemDireita == null) {
			margemDireita = 5;
		}
		return margemDireita;
	}

	public void setMargemDireita(Integer margemDireita) {
		this.margemDireita = margemDireita;
	}

	@XmlElement(name = "margemTopo")
	public Integer getMargemTopo() {
		if (margemTopo == null) {
			margemTopo = 5;
		}
		return margemTopo;
	}

	public void setMargemTopo(Integer margemTopo) {
		this.margemTopo = margemTopo;
	}

	@XmlElement(name = "rotacao")
	public Boolean getRotacao() {
		if (rotacao == null) {
			rotacao = false;
		}

		return rotacao;
	}

	public void setRotacao(Boolean rotacao) {
		this.rotacao = rotacao;
	}

	@XmlElement(name = "tamanhoFonte")
	public Integer getTamanhoFonte() {
		if (tamanhoFonte == null) {
			tamanhoFonte = 12;
		}
		return tamanhoFonte;
	}

	public void setTamanhoFonte(Integer tamanhoFonte) {

		this.tamanhoFonte = tamanhoFonte;
	}

	@XmlElement(name = "cor")
	public Boolean getCor() {
		if (cor == null) {
			cor = false;
		}
		return cor;
	}

	public void setCor(Boolean cor) {
		this.cor = cor;
	}

	@XmlElement(name = "imprimirNumeroAbaixo")
	public Boolean getImprimirNumeroAbaixo() {
		if (imprimirNumeroAbaixo == null) {
			imprimirNumeroAbaixo = false;
		}
		return imprimirNumeroAbaixo;
	}

	public void setImprimirNumeroAbaixo(Boolean imprimirNumeroAbaixo) {
		this.imprimirNumeroAbaixo = imprimirNumeroAbaixo;
	}

	@XmlElement(name = "ordem")
	public Integer getOrdem() {
		if (ordem == null) {
			ordem = 1;
		}
		return ordem;
	}

	public void setOrdem(Integer ordem) {
		this.ordem = ordem;
	}

	@XmlElement(name = "apresentarLabelEtiquetaAposTagEtiqueta")
	public Boolean getApresentarLabelEtiquetaAposTagEtiqueta() {
		if (apresentarLabelEtiquetaAposTagEtiqueta == null) {
			apresentarLabelEtiquetaAposTagEtiqueta = Boolean.FALSE;
		}
		return apresentarLabelEtiquetaAposTagEtiqueta;
	}

	public void setApresentarLabelEtiquetaAposTagEtiqueta(Boolean apresentarLabelEtiquetaAposTagEtiqueta) {
		this.apresentarLabelEtiquetaAposTagEtiqueta = apresentarLabelEtiquetaAposTagEtiqueta;
	}

	@XmlTransient
	public String getNomeArquivo() {
		if (nomeArquivo == null) {
			nomeArquivo = "";
		}
		return nomeArquivo;
	}

	
	public void setNomeArquivo(String nomeArquivo) {
		this.nomeArquivo = nomeArquivo;
	}

	@XmlTransient
	public String getNomeArquivoApresentar() {
		if (nomeArquivoApresentar == null) {
			nomeArquivoApresentar = "";
		}
		return nomeArquivoApresentar;
	}

	public void setNomeArquivoApresentar(String nomeArquivoApresentar) {
		this.nomeArquivoApresentar = nomeArquivoApresentar;
	}

	@XmlTransient
	public PastaBaseArquivoEnum getPastaBaseArquivo() {
		if (pastaBaseArquivo == null) {
			pastaBaseArquivo = PastaBaseArquivoEnum.IMAGEM_FUNDO_CARTEIRA_ESTUDANTIL;
		}
		return pastaBaseArquivo;
	}

	public void setPastaBaseArquivo(PastaBaseArquivoEnum pastaBaseArquivo) {
		this.pastaBaseArquivo = pastaBaseArquivo;
	}

	@XmlTransient
	public String getUrlImagem() {
		if (urlImagem == null) {
			urlImagem = "";
		}
		return urlImagem;
	}

	public void setUrlImagem(String urlImagem) {
		this.urlImagem = urlImagem;
	}

	@XmlTransient
	public String getNomeArquivoAnt() {
		if (nomeArquivoAnt == null) {
			nomeArquivoAnt = "";
		}
		return nomeArquivoAnt;
	}

	public void setNomeArquivoAnt(String nomeArquivoAnt) {
		this.nomeArquivoAnt = nomeArquivoAnt;
	}
	
	@XmlTransient
	public boolean getIsApresentarCampoImagemFundo() {
		return getTagEtiqueta().equals(TagEtiquetaEnum.IMAGEM_FUNDO);
	}
	

	@XmlElement(name = "corTexto")
	public String getCorTexto() {
		if (corTexto == null) {
			corTexto = "#000000";
		}
		return corTexto;
	}

	public void setCorTexto(String corTexto) {
		this.corTexto = corTexto;
	}

	@XmlElement(name = "fontNegrito")
	public Boolean getFontNegrito() {
		if(fontNegrito == null){
			fontNegrito = false;
		}
		return fontNegrito;
	}

	public void setFontNegrito(Boolean fontNegrito) {
		this.fontNegrito = fontNegrito;
	}

	@XmlElement(name = "alturaFoto")
	public Integer getAlturaFoto() {
		if(alturaFoto == null){
			alturaFoto = 0;
		}
		return alturaFoto;
	}

	public void setAlturaFoto(Integer alturaFoto) {
		this.alturaFoto = alturaFoto;
	}

	@XmlElement(name = "larguraFoto")
	public Integer getLarguraFoto() {
		if(larguraFoto == null){
			larguraFoto = 0;
		}
		return larguraFoto;
	}

	public void setLarguraFoto(Integer larguraFoto) {
		this.larguraFoto = larguraFoto;
	}

	public String getUrlImagemApresentar() {
		if (urlImagemApresentar == null) {
			urlImagemApresentar = "";
		}
		return urlImagemApresentar;
	}

	public void setUrlImagemApresentar(String urlImagemApresentar) {
		this.urlImagemApresentar = urlImagemApresentar;
	}
}
