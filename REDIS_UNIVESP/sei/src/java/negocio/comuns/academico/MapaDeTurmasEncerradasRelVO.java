package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.utilitarias.dominios.SituacaoVinculoMatricula;

public class MapaDeTurmasEncerradasRelVO {
	
	

	private Integer quantidadeDeAlunos;
	private Integer quantidadeDeAlunosFormados;
	private Integer quantidadeDeAlunosSeremFormados;
	private Date dataUltimaAula;
	private String tipoConsulta;
	
	private TurmaVO turmaVO;
	private MatriculaVO matriculaVO;
	
	
	public TurmaVO getTurmaVO() {
		if (turmaVO==null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}
	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}
	public Integer getQuantidadeDeAlunos() {
		if (quantidadeDeAlunos==null) {
			quantidadeDeAlunos = 0;
		}
		return quantidadeDeAlunos;
	}
	public void setQuantidadeDeAlunos(Integer quantidadeDeAlunos) {
		this.quantidadeDeAlunos = quantidadeDeAlunos;
	}
	public Integer getQuantidadeDeAlunosFormados() {
		if (quantidadeDeAlunosFormados==null) {
			quantidadeDeAlunosFormados = 0;
		}
		return quantidadeDeAlunosFormados;
	}
	public void setQuantidadeDeAlunosFormados(Integer quantidadeDeAlunosFormados) {
		this.quantidadeDeAlunosFormados = quantidadeDeAlunosFormados;
	}
	public Integer getQuantidadeDeAlunosSeremFormados() {
		if (quantidadeDeAlunosSeremFormados==null) {
			quantidadeDeAlunosSeremFormados = 0;
		}
		return quantidadeDeAlunosSeremFormados;
	}
	public void setQuantidadeDeAlunosSeremFormados(
			Integer quantidadeDeAlunosSeremFormados) {
		this.quantidadeDeAlunosSeremFormados = quantidadeDeAlunosSeremFormados;
	}
	public Date getDataUltimaAula() {
		if (dataUltimaAula==null) {
			dataUltimaAula = new Date();
		}
		return dataUltimaAula;
	}
	public void setDataUltimaAula(Date dataUltimaAula) {
		this.dataUltimaAula = dataUltimaAula;
	}
	public MatriculaVO getMatriculaVO() {
		if (matriculaVO==null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}
	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}
	
	
	public String getTipoConsulta() {
		if (tipoConsulta==null) {
			tipoConsulta = "";
		}
		return tipoConsulta;
	}
	
	public void setTipoConsulta(String tipoConsulta) {
		this.tipoConsulta = tipoConsulta;
	}
	
	public StringBuffer getSituacao(){
		if (getTipoConsulta().equals("quantidadeDeAlunos")) {
			return new StringBuffer(SituacaoVinculoMatricula.FORMADO.getValor()).append("','").append(SituacaoVinculoMatricula.ATIVA.getValor());
		}
		if (getTipoConsulta().equals("quantidadeDeAlunosFormados")) {
			return new StringBuffer(SituacaoVinculoMatricula.FORMADO.getValor());
		}else  return new StringBuffer(SituacaoVinculoMatricula.ATIVA.getValor());
		
	}
	

}
