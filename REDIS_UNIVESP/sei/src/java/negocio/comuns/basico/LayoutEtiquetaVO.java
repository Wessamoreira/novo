package negocio.comuns.basico;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

/**
 * @author José Carlos
 */
@XmlRootElement(name = "layout")
public class LayoutEtiquetaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	@XmlTransient
	private Integer codigo;
	private String descricao;
	private Integer larguraFolhaImpressao;
	private Integer alturaFolhaImpressao;
	private Integer larguraEtiqueta;
	private Integer alturaEtiqueta;
	private ModuloLayoutEtiquetaEnum moduloLayoutEtiqueta;

	private List<LayoutEtiquetaTagVO> layoutEtiquetaTagVO;
	private Integer margemSuperiorEtiquetaFolha;
	private Integer margemEsquerdaEtiquetaFolha;
	private Integer margemEntreEtiquetaHorizontal;
	private Integer margemEntreEtiquetaVertical;
	private Integer numeroColunasEtiqueta;
	private Integer numeroLinhasEtiqueta;
	private String urlImagemApresentar;

	/**
	 * Transient
	 */
	@XmlTransient
	private String nomeArquivo;
	@XmlTransient
	private String nomeArquivoApresentar;
	@XmlTransient
	private PastaBaseArquivoEnum pastaBaseArquivo;
	@XmlTransient
	private String urlImagem;
	@XmlTransient
	private String nomeArquivoAnt;
	@XmlTransient
    private Integer codigoExpedicaoDiploma;
	/**
	 * Fim Transient
	 */
	@XmlElement(name = "margemSuperiorEtiquetaFolha")
	public Integer getMargemSuperiorEtiquetaFolha() {
		if (margemSuperiorEtiquetaFolha == null) {
			margemSuperiorEtiquetaFolha = 0;
		}
		return margemSuperiorEtiquetaFolha;
	}

	public void setMargemSuperiorEtiquetaFolha(Integer margemSuperiorEtiquetaFolha) {
		this.margemSuperiorEtiquetaFolha = margemSuperiorEtiquetaFolha;
	}

	@XmlElement(name = "margemEsquerdaEtiquetaFolha")
	public Integer getMargemEsquerdaEtiquetaFolha() {
		if (margemEsquerdaEtiquetaFolha == null) {
			margemEsquerdaEtiquetaFolha = 0;
		}
		return margemEsquerdaEtiquetaFolha;
	}

	public void setMargemEsquerdaEtiquetaFolha(Integer margemEsquerdaEtiquetaFolha) {
		this.margemEsquerdaEtiquetaFolha = margemEsquerdaEtiquetaFolha;
	}

	@XmlElement(name = "margemEntreEtiquetaHorizontal")
	public Integer getMargemEntreEtiquetaHorizontal() {
		if (margemEntreEtiquetaHorizontal == null) {
			margemEntreEtiquetaHorizontal = 0;
		}
		return margemEntreEtiquetaHorizontal;
	}

	public void setMargemEntreEtiquetaHorizontal(Integer margemEntreEtiquetaHorizontal) {
		this.margemEntreEtiquetaHorizontal = margemEntreEtiquetaHorizontal;
	}

	@XmlElement(name = "margemEntreEtiquetaVertical")
	public Integer getMargemEntreEtiquetaVertical() {
		if (margemEntreEtiquetaVertical == null) {
			margemEntreEtiquetaVertical = 0;
		}
		return margemEntreEtiquetaVertical;
	}

	public void setMargemEntreEtiquetaVertical(Integer margemEntreEtiquetaVertical) {
		this.margemEntreEtiquetaVertical = margemEntreEtiquetaVertical;
	}

	@XmlElement(name = "numeroColunasEtiqueta")
	public Integer getNumeroColunasEtiqueta() {
		if (numeroColunasEtiqueta == null) {
			numeroColunasEtiqueta = 0;
		}
		return numeroColunasEtiqueta;
	}

	public void setNumeroColunasEtiqueta(Integer numeroColunaEtiqueta) {
		this.numeroColunasEtiqueta = numeroColunaEtiqueta;
	}

	@XmlElement(name = "numeroLinhasEtiqueta")
	public Integer getNumeroLinhasEtiqueta() {
		if (numeroLinhasEtiqueta == null) {
			numeroLinhasEtiqueta = 0;
		}
		return numeroLinhasEtiqueta;
	}

	public void setNumeroLinhasEtiqueta(Integer numeroLinhaEtiqueta) {
		this.numeroLinhasEtiqueta = numeroLinhaEtiqueta;
	}

	@XmlElement(name = "layoutCampo")
	public List<LayoutEtiquetaTagVO> getLayoutEtiquetaTagVO() {
		return layoutEtiquetaTagVO;
	}
	
	@XmlTransient
	public List<LayoutEtiquetaTagVO> getLayoutEtiquetaTagVOs() {
		if (layoutEtiquetaTagVO == null) {
			layoutEtiquetaTagVO = new ArrayList<LayoutEtiquetaTagVO>();
		}
		return layoutEtiquetaTagVO;
	}

	public void setLayoutEtiquetaTagVO(List<LayoutEtiquetaTagVO> layoutEtiquetaTagVO) {
		this.layoutEtiquetaTagVO = layoutEtiquetaTagVO;
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
	
	@XmlElement(name = "descricao")
	public String getDescricao() {
		if (descricao == null) {
			descricao = "";
		}
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@XmlElement(name = "larguraFolhaImpressao")
	public Integer getLarguraFolhaImpressao() {
		if (larguraFolhaImpressao == null) {
			larguraFolhaImpressao = 0;
		}
		return larguraFolhaImpressao;
	}

	public void setLarguraFolhaImpressao(Integer larguraFolhaImpressao) {
		this.larguraFolhaImpressao = larguraFolhaImpressao;
	}

	@XmlElement(name = "alturaFolhaImpressao")
	public Integer getAlturaFolhaImpressao() {
		if (alturaFolhaImpressao == null) {
			alturaFolhaImpressao = 0;
		}
		return alturaFolhaImpressao;
	}

	public void setAlturaFolhaImpressao(Integer alturaFolhaImpressao) {
		this.alturaFolhaImpressao = alturaFolhaImpressao;
	}

	@XmlElement(name = "larguraEtiqueta")
	public Integer getLarguraEtiqueta() {
		if (larguraEtiqueta == null) {
			larguraEtiqueta = 0;
		}
		return larguraEtiqueta;
	}

	public void setLarguraEtiqueta(Integer larguraEtiqueta) {
		this.larguraEtiqueta = larguraEtiqueta;
	}

	@XmlElement(name = "alturaEtiqueta")
	public Integer getAlturaEtiqueta() {
		if (alturaEtiqueta == null) {
			alturaEtiqueta = 0;
		}
		return alturaEtiqueta;
	}

	public void setAlturaEtiqueta(Integer alturaEtiqueta) {
		this.alturaEtiqueta = alturaEtiqueta;
	}

	@XmlElement(name = "moduloLayoutEtiqueta")
	public ModuloLayoutEtiquetaEnum getModuloLayoutEtiqueta() {
		if (moduloLayoutEtiqueta == null) {
			moduloLayoutEtiqueta = ModuloLayoutEtiquetaEnum.INSCRICAO_SELETIVO;
		}
		return moduloLayoutEtiqueta;
	}

	public void setModuloLayoutEtiqueta(ModuloLayoutEtiquetaEnum moduloLayoutEtiqueta) {
		this.moduloLayoutEtiqueta = moduloLayoutEtiqueta;
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
	
    /**
     * @return the codigoExpedicaoDiploma
     */
	@XmlTransient
    public Integer getCodigoExpedicaoDiploma() {
        if (codigoExpedicaoDiploma == null) { 
            codigoExpedicaoDiploma = 0;
        }
        return codigoExpedicaoDiploma;
    }

    /**
     * @param codigoExpedicaoDiploma the codigoExpedicaoDiploma to set
     */
    public void setCodigoExpedicaoDiploma(Integer codigoExpedicaoDiploma) {
        this.codigoExpedicaoDiploma = codigoExpedicaoDiploma;
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
