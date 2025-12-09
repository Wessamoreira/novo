package relatorio.negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.TurmaVO;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;

public class HistoricoTurmaRelVO {
     private TurmaVO turmaVO;
     private DisciplinaVO disciplinaVO;
     private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
     private Integer cargaHorariaDisciplina;
     private String nomeProfessor;
     private String ano;
     private String semestre;     
     private Integer ordemLinhaAtual;
     private Integer totalAlunos;
     private List<CrosstabVO> crosstabVOs;
     private Date dataPrimeiraAula;
     private Date dataUltimaAula;
     
	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}
	
	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}
	
	public DisciplinaVO getDisciplinaVO() {		
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}
	
	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}
	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if (configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}
	
	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
	}
	
	public Integer getOrdemLinhaAtual() {
		if (ordemLinhaAtual == null) {
			ordemLinhaAtual = 0;
		}
		return ordemLinhaAtual;
	}
	
	public void setOrdemLinhaAtual(Integer ordemLinhaAtual) {
		this.ordemLinhaAtual = ordemLinhaAtual;
	}
	
	public List<CrosstabVO> getCrosstabVOs() {
		if (crosstabVOs == null) {
			crosstabVOs = new ArrayList<CrosstabVO>(0);
		}
		return crosstabVOs;
	}
	
	public void setCrosstabVOs(List<CrosstabVO> crosstabVOs) {
		this.crosstabVOs = crosstabVOs;
	}

	public Integer getCargaHorariaDisciplina() {
		if (cargaHorariaDisciplina == null) {
			cargaHorariaDisciplina = 0;
		}
		return cargaHorariaDisciplina;
	}

	public void setCargaHorariaDisciplina(Integer cargaHorariaDisciplina) {
		this.cargaHorariaDisciplina = cargaHorariaDisciplina;
	}

	public String getNomeProfessor() {
		if (nomeProfessor == null) {
			nomeProfessor = "";
		}
		return nomeProfessor;
	}

	public void setNomeProfessor(String nomeProfessor) {
		this.nomeProfessor = nomeProfessor;
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

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public Integer getTotalAlunos() {
		if (totalAlunos == null) {
			totalAlunos = 0;
		}
		return totalAlunos;
	}

	public void setTotalAlunos(Integer totalAlunos) {
		this.totalAlunos = totalAlunos;
	}

	public Date getDataPrimeiraAula() {		
		return dataPrimeiraAula;
	}

	public void setDataPrimeiraAula(Date dataPrimeiraAula) {
		this.dataPrimeiraAula = dataPrimeiraAula;
	}

	public Date getDataUltimaAula() {
		
		return dataUltimaAula;
	}

	public void setDataUltimaAula(Date dataUltimaAula) {
		this.dataUltimaAula = dataUltimaAula;
	}
	
	
     
     
     
}
