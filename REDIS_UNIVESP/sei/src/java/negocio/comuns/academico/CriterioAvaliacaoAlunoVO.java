package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.academico.enumeradores.OrigemCriterioAvaliacaoIndicadorEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;

public class CriterioAvaliacaoAlunoVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6372744957107078801L;

	private Integer codigo;
	private MatriculaPeriodoVO matriculaPeriodo;
	private String ano;
	private String semestre;
	private OrigemCriterioAvaliacaoIndicadorEnum origemCriterioAvaliacaoIndicador;
	private CriterioAvaliacaoVO criterioAvaliacao;
	private CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivo;	
	private CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplina;
	private CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicador;
	private CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicador;
	private CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceito1Bimestre;
	private CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceito2Bimestre;
	private CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceito3Bimestre;
	private CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceito4Bimestre;
	private Double nota1Bimestre;
	private Double nota2Bimestre;
	private Double nota3Bimestre;
	private Double nota4Bimestre;
	private Date dataCadastro;
	private UsuarioVO usuarioCadastro;

	private Double nota;
	
	// @Transient
	private String selectItemNota1;
	private String selectItemNota2;
	private String selectItemNota3;
	private String selectItemNota4;
	

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public MatriculaPeriodoVO getMatriculaPeriodo() {
		if (matriculaPeriodo == null) {
			matriculaPeriodo = new MatriculaPeriodoVO();
		}
		return matriculaPeriodo;
	}

	public void setMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodo) {
		this.matriculaPeriodo = matriculaPeriodo;
	}

	

	public CriterioAvaliacaoIndicadorVO getCriterioAvaliacaoIndicador() {
		if (criterioAvaliacaoIndicador == null) {
			criterioAvaliacaoIndicador = new CriterioAvaliacaoIndicadorVO();
		}
		return criterioAvaliacaoIndicador;
	}

	public void setCriterioAvaliacaoIndicador(CriterioAvaliacaoIndicadorVO criterioAvaliacaoIndicador) {
		this.criterioAvaliacaoIndicador = criterioAvaliacaoIndicador;
	}

	public Double getNota() {
		if (nota == null) {
			nota = 0.0;
		}
		return nota;
	}

	public void setNota(Double nota) {
		this.nota = nota;
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

	public UsuarioVO getUsuarioCadastro() {
		if (usuarioCadastro == null) {
			usuarioCadastro = new UsuarioVO();
		}
		return usuarioCadastro;
	}

	

	

	public CriterioAvaliacaoNotaConceitoVO getCriterioAvaliacaoNotaConceito1Bimestre() {
		if (criterioAvaliacaoNotaConceito1Bimestre == null) {
			criterioAvaliacaoNotaConceito1Bimestre = new CriterioAvaliacaoNotaConceitoVO();
		}
		return criterioAvaliacaoNotaConceito1Bimestre;
	}

	public void setCriterioAvaliacaoNotaConceito1Bimestre(CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceito1Bimestre) {
		this.criterioAvaliacaoNotaConceito1Bimestre = criterioAvaliacaoNotaConceito1Bimestre;
	}

	public CriterioAvaliacaoNotaConceitoVO getCriterioAvaliacaoNotaConceito2Bimestre() {
		if (criterioAvaliacaoNotaConceito2Bimestre == null) {
			criterioAvaliacaoNotaConceito2Bimestre = new CriterioAvaliacaoNotaConceitoVO();
		}
		return criterioAvaliacaoNotaConceito2Bimestre;
	}

	public void setCriterioAvaliacaoNotaConceito2Bimestre(CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceito2Bimestre) {
		this.criterioAvaliacaoNotaConceito2Bimestre = criterioAvaliacaoNotaConceito2Bimestre;
	}

	public CriterioAvaliacaoNotaConceitoVO getCriterioAvaliacaoNotaConceito3Bimestre() {
		if (criterioAvaliacaoNotaConceito3Bimestre == null) {
			criterioAvaliacaoNotaConceito3Bimestre = new CriterioAvaliacaoNotaConceitoVO();
		}
		return criterioAvaliacaoNotaConceito3Bimestre;
	}

	public void setCriterioAvaliacaoNotaConceito3Bimestre(CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceito3Bimestre) {
		this.criterioAvaliacaoNotaConceito3Bimestre = criterioAvaliacaoNotaConceito3Bimestre;
	}

	public CriterioAvaliacaoNotaConceitoVO getCriterioAvaliacaoNotaConceito4Bimestre() {
		if (criterioAvaliacaoNotaConceito4Bimestre == null) {
			criterioAvaliacaoNotaConceito4Bimestre = new CriterioAvaliacaoNotaConceitoVO();
		}
		return criterioAvaliacaoNotaConceito4Bimestre;
	}

	public void setCriterioAvaliacaoNotaConceito4Bimestre(CriterioAvaliacaoNotaConceitoVO criterioAvaliacaoNotaConceito4Bimestre) {
		this.criterioAvaliacaoNotaConceito4Bimestre = criterioAvaliacaoNotaConceito4Bimestre;
	}

	public Double getNota1Bimestre() {
		if (nota1Bimestre == null) {
			nota1Bimestre = 0.0;
		}
		return nota1Bimestre;
	}

	public void setNota1Bimestre(Double nota1Bimestre) {
		this.nota1Bimestre = nota1Bimestre;
	}

	public Double getNota2Bimestre() {
		if (nota2Bimestre == null) {
			nota2Bimestre = 0.0;
		}
		return nota2Bimestre;
	}

	public void setNota2Bimestre(Double nota2Bimestre) {
		this.nota2Bimestre = nota2Bimestre;
	}

	public Double getNota3Bimestre() {
		if (nota3Bimestre == null) {
			nota3Bimestre = 0.0;
		}
		return nota3Bimestre;
	}

	public void setNota3Bimestre(Double nota3Bimestre) {
		this.nota3Bimestre = nota3Bimestre;
	}

	public Double getNota4Bimestre() {
		if (nota4Bimestre == null) {
			nota4Bimestre = 0.0;
		}
		return nota4Bimestre;
	}

	public void setNota4Bimestre(Double nota4Bimestre) {
		this.nota4Bimestre = nota4Bimestre;
	}

	public void setUsuarioCadastro(UsuarioVO usuarioCadastro) {
		this.usuarioCadastro = usuarioCadastro;
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

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public CriterioAvaliacaoVO getCriterioAvaliacao() {
		if (criterioAvaliacao == null) {
			criterioAvaliacao = new CriterioAvaliacaoVO();
		}
		return criterioAvaliacao;
	}

	public void setCriterioAvaliacao(CriterioAvaliacaoVO criterioAvaliacao) {
		this.criterioAvaliacao = criterioAvaliacao;
	}

	public CriterioAvaliacaoPeriodoLetivoVO getCriterioAvaliacaoPeriodoLetivo() {
		if (criterioAvaliacaoPeriodoLetivo == null) {
			criterioAvaliacaoPeriodoLetivo = new CriterioAvaliacaoPeriodoLetivoVO();
		}
		return criterioAvaliacaoPeriodoLetivo;
	}

	public void setCriterioAvaliacaoPeriodoLetivo(CriterioAvaliacaoPeriodoLetivoVO criterioAvaliacaoPeriodoLetivo) {
		this.criterioAvaliacaoPeriodoLetivo = criterioAvaliacaoPeriodoLetivo;
	}

	

	public CriterioAvaliacaoDisciplinaVO getCriterioAvaliacaoDisciplina() {
		if (criterioAvaliacaoDisciplina == null) {
			criterioAvaliacaoDisciplina = new CriterioAvaliacaoDisciplinaVO();
		}
		return criterioAvaliacaoDisciplina;
	}

	public void setCriterioAvaliacaoDisciplina(CriterioAvaliacaoDisciplinaVO criterioAvaliacaoDisciplina) {
		this.criterioAvaliacaoDisciplina = criterioAvaliacaoDisciplina;
	}

	public CriterioAvaliacaoDisciplinaEixoIndicadorVO getCriterioAvaliacaoDisciplinaEixoIndicador() {
		if (criterioAvaliacaoDisciplinaEixoIndicador == null) {
			criterioAvaliacaoDisciplinaEixoIndicador = new CriterioAvaliacaoDisciplinaEixoIndicadorVO();
		}
		return criterioAvaliacaoDisciplinaEixoIndicador;
	}

	public void setCriterioAvaliacaoDisciplinaEixoIndicador(CriterioAvaliacaoDisciplinaEixoIndicadorVO criterioAvaliacaoDisciplinaEixoIndicador) {
		this.criterioAvaliacaoDisciplinaEixoIndicador = criterioAvaliacaoDisciplinaEixoIndicador;
	}

	public OrigemCriterioAvaliacaoIndicadorEnum getOrigemCriterioAvaliacaoIndicador() {
		if (origemCriterioAvaliacaoIndicador == null) {
			origemCriterioAvaliacaoIndicador = OrigemCriterioAvaliacaoIndicadorEnum.DISCIPLINA;
		}
		return origemCriterioAvaliacaoIndicador;
	}

	public void setOrigemCriterioAvaliacaoIndicador(OrigemCriterioAvaliacaoIndicadorEnum origemCriterioAvaliacaoIndicador) {
		this.origemCriterioAvaliacaoIndicador = origemCriterioAvaliacaoIndicador;
	}

	public String getSelectItemNota1() {
		if (selectItemNota1 == null) {
			selectItemNota1 = "";
		}
		return selectItemNota1;
	}

	public void setSelectItemNota1(String selectItemNota1) {
		this.selectItemNota1 = selectItemNota1;
	}

	public String getSelectItemNota2() {
		if (selectItemNota2 == null) {
			selectItemNota2 = "";
		}
		return selectItemNota2;
	}

	public void setSelectItemNota2(String selectItemNota2) {
		this.selectItemNota2 = selectItemNota2;
	}

	public String getSelectItemNota3() {
		if (selectItemNota3 == null) {
			selectItemNota3 = "";
		}
		return selectItemNota3;
	}

	public void setSelectItemNota3(String selectItemNota3) {
		this.selectItemNota3 = selectItemNota3;
	}

	public String getSelectItemNota4() {
		if (selectItemNota4 == null) {
			selectItemNota4 = "";
		}
		return selectItemNota4;
	}

	public void setSelectItemNota4(String selectItemNota4) {
		this.selectItemNota4 = selectItemNota4;
	}
	
	
	
	
	
	
}
