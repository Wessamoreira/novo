package negocio.comuns.academico;

import java.io.Serializable;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.Uteis;

public class CidTipoRequerimentoVO extends SuperVO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String descricaoCid;
	private String codCid;
	private Integer tipoRequerimento;
	private Integer codigo;
	private  Boolean selecionado;
	private ArquivoVO arquivoVO;
	private  Boolean importacaoExcel;
	private String nomeCid_Apresentar;
	
	
	public String getDescricaoCid() {
		if (descricaoCid == null) {
			descricaoCid = "";
		}
		return descricaoCid;
	}
	
	
	public void setDescricaoCid(String descricaoCid) {
		this.descricaoCid = descricaoCid;
	}
	
	
	public String getCodCid() {
		if (codCid == null) {
			codCid = "";
		}
		return codCid;
	}
	
	
	public void setCodCid(String codCid) {
		this.codCid = codCid;
	}
	
	
	public Integer getTipoRequerimento() {
		if(tipoRequerimento == null) {
			tipoRequerimento = 0;
		}
		return tipoRequerimento;
	}
	
	
	public void setTipoRequerimento(Integer tipoRequerimento) {
		this.tipoRequerimento = tipoRequerimento;
	}
	
	
	public Integer getCodigo() {
		if(codigo == null) {
			codigo = 0;
		}
		return codigo;
	}
	
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	public Boolean getSelecionado() {
		if(selecionado == null) {
			selecionado =  false;
		}
		return selecionado;
		}
	
	
	public void setSelecionado(Boolean selecionado) {
		this.selecionado = selecionado;
	}


	public ArquivoVO getArquivoVO() {
		if(arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}


	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}


	public Boolean getImportacaoExcel() {
		if(selecionado == null) {
			selecionado =  false;
		}
		return importacaoExcel;
	}


	public void setImportacaoExcel(Boolean importacaoExcel) {
		this.importacaoExcel = importacaoExcel;
	}


	public String getNomeCid_Apresentar() {
		return getCodCid()+" - "+getDescricaoCid();
	}
	
}
