package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;

/**
 * @author Leonardo Riciolle 
 * 			Responsavel por registrar a falta conforme as informações apresentadas
 */

public class RegistroFaltaVO extends SuperVO {

	private MatriculaVO matriculaVO;
	private String semestre;
	private String motivoFalta;
	private String bimestre;
	private String ano;
	private Integer codigo;
	private Date dataFalta;
	private Date dataCadastro;
	private Integer responsavelcadastro;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
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

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public Date getDataFalta() {
		if (dataFalta == null) {
			dataFalta = new Date();
		}
		return dataFalta;
	}

	public void setDataFalta(Date dataFalta) {
		this.dataFalta = dataFalta;
	}

	public String getMotivoFalta() {
		if (motivoFalta == null) {
			motivoFalta = "";
		}
		return motivoFalta;
	}

	public void setMotivoFalta(String motivoFalta) {
		this.motivoFalta = motivoFalta;
	}

	public String getBimestre() {
		if (bimestre == null) {
			bimestre = "";
		}
		return bimestre;
	}

	public void setBimestre(String bimestre) {
		this.bimestre = bimestre;
	}

	public Date getDataCadastro() {
		if (dataCadastro == null) {
			dataCadastro = new Date();
		}
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Integer getResponsavelcadastro() {
		if (responsavelcadastro == null) {
			responsavelcadastro = 0;
		}
		return responsavelcadastro;
	}

	public void setResponsavelcadastro(Integer responsavelcadastro) {
		this.responsavelcadastro = responsavelcadastro;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

}
