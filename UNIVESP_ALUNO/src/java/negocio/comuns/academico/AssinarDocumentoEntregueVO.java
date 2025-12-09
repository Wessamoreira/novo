package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;

public class AssinarDocumentoEntregueVO extends SuperVO {

	private static final long serialVersionUID = 1L;

	private String origemArquivo;
	private String servidorArquivo;
	private Integer qtdProcessarLote;
	private Date dataInicioEntrega;
	private Date dataFimEntrega;
	private Integer codigoUnidadeEnsino;
	private Boolean considerarDocumentosProcessadorComErro;
	private MatriculaVO matriculaVO;

	public String getOrigemArquivo() {
		return origemArquivo;
	}

	public void setOrigemArquivo(String origemArquivo) {
		this.origemArquivo = origemArquivo;
	}

	public String getServidorArquivo() {
		if (servidorArquivo == null) {
			servidorArquivo = "";
		}
		return servidorArquivo;
	}

	public void setServidorArquivo(String servidorArquivo) {
		this.servidorArquivo = servidorArquivo;
	}

	public Integer getQtdProcessarLote() {
		if (qtdProcessarLote == null) {
			qtdProcessarLote = 10;
		}
		return qtdProcessarLote;
	}

	public void setQtdProcessarLote(Integer qtdProcessarLote) {
		this.qtdProcessarLote = qtdProcessarLote;
	}

	public Date getDataInicioEntrega() {
		return dataInicioEntrega;
	}

	public void setDataInicioEntrega(Date dataInicioEntrega) {
		this.dataInicioEntrega = dataInicioEntrega;
	}

	public Date getDataFimEntrega() {
		return dataFimEntrega;
	}

	public void setDataFimEntrega(Date dataFimEntrega) {
		this.dataFimEntrega = dataFimEntrega;
	}

	public Integer getCodigoUnidadeEnsino() {
		if (codigoUnidadeEnsino == null) {
			codigoUnidadeEnsino = 0;
		}
		return codigoUnidadeEnsino;
	}

	public void setCodigoUnidadeEnsino(Integer codigoUnidadeEnsino) {
		this.codigoUnidadeEnsino = codigoUnidadeEnsino;
	}

	public Boolean getConsiderarDocumentosProcessadorComErro() {
		if (considerarDocumentosProcessadorComErro == null) {
			considerarDocumentosProcessadorComErro = false;
		}
		return considerarDocumentosProcessadorComErro;
	}

	public void setConsiderarDocumentosProcessadorComErro(Boolean considerarDocumentosProcessadorComErro) {
		this.considerarDocumentosProcessadorComErro = considerarDocumentosProcessadorComErro;
	}

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}
}
