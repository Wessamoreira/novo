package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
/**
 * Reponsável por manter os dados da entidade MatriculaPeriodoTurmaDisciplina.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class InclusaoDisciplinasHistoricoForaPrazoVO extends SuperVO {

	private Integer codigo;
	private TurmaVO turma;
	private DisciplinaVO disciplina;
	private GradeDisciplinaVO gradeDisciplinaVO;
	private String semestre;
	private String ano;
	private InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO;
	private MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursada;
	private Boolean disciplinaForaGrade;
	private PeriodoLetivoVO periodoLetivoDisciplinaIncluidaVO;
	private GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO;
    private UsuarioVO usuarioLibercaoChoqueHorario;
    private Date dataUsuarioLibercaoChoqueHorario;	
	
	
	public static final long serialVersionUID = 1L;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return (turma);
	}

	public void setTurma(TurmaVO obj) {
		this.turma = obj;
	}

	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
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

	public InclusaoHistoricoForaPrazoVO getInclusaoHistoricoForaPrazoVO() {
		if (inclusaoHistoricoForaPrazoVO == null) {
			inclusaoHistoricoForaPrazoVO = new InclusaoHistoricoForaPrazoVO();
		}
		return inclusaoHistoricoForaPrazoVO;
	}

	public void setInclusaoHistoricoForaPrazoVO(InclusaoHistoricoForaPrazoVO inclusaoHistoricoForaPrazoVO) {
		this.inclusaoHistoricoForaPrazoVO = inclusaoHistoricoForaPrazoVO;
	}

	public GradeDisciplinaVO getGradeDisciplinaVO() {
		if (gradeDisciplinaVO == null) {
			gradeDisciplinaVO = new GradeDisciplinaVO();
		}
		return gradeDisciplinaVO;
	}

	public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
		this.gradeDisciplinaVO = gradeDisciplinaVO;
	}

	public MapaEquivalenciaDisciplinaCursadaVO getMapaEquivalenciaDisciplinaCursada() {
		if (mapaEquivalenciaDisciplinaCursada == null) {
			mapaEquivalenciaDisciplinaCursada = new MapaEquivalenciaDisciplinaCursadaVO();
		}
		return mapaEquivalenciaDisciplinaCursada;
	}

	public void setMapaEquivalenciaDisciplinaCursada(MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursada) {
		this.mapaEquivalenciaDisciplinaCursada = mapaEquivalenciaDisciplinaCursada;
	}

	public Boolean getDisciplinaForaGrade() {
		if (disciplinaForaGrade == null) {
			disciplinaForaGrade = Boolean.FALSE;
		}
		return disciplinaForaGrade;
	}

	public void setDisciplinaForaGrade(Boolean disciplinaForaGrade) {
		this.disciplinaForaGrade = disciplinaForaGrade;
	}

	public PeriodoLetivoVO getPeriodoLetivoDisciplinaIncluidaVO() {
		if (periodoLetivoDisciplinaIncluidaVO == null) {
			periodoLetivoDisciplinaIncluidaVO = new PeriodoLetivoVO();
		}
		return periodoLetivoDisciplinaIncluidaVO;
	}

	public void setPeriodoLetivoDisciplinaIncluidaVO(PeriodoLetivoVO periodoLetivoDisciplinaIncluidaVO) {
		this.periodoLetivoDisciplinaIncluidaVO = periodoLetivoDisciplinaIncluidaVO;
	}

	public GradeCurricularGrupoOptativaDisciplinaVO getGradeCurricularGrupoOptativaDisciplinaVO() {
		if (gradeCurricularGrupoOptativaDisciplinaVO == null) {
			gradeCurricularGrupoOptativaDisciplinaVO = new GradeCurricularGrupoOptativaDisciplinaVO();
		}
		return gradeCurricularGrupoOptativaDisciplinaVO;
	}

	public void setGradeCurricularGrupoOptativaDisciplinaVO(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO) {
		this.gradeCurricularGrupoOptativaDisciplinaVO = gradeCurricularGrupoOptativaDisciplinaVO;
	}

	public UsuarioVO getUsuarioLibercaoChoqueHorario() {
		return usuarioLibercaoChoqueHorario;
	}

	public void setUsuarioLibercaoChoqueHorario(UsuarioVO usuarioLibercaoChoqueHorario) {
		this.usuarioLibercaoChoqueHorario = usuarioLibercaoChoqueHorario;
	}

	public Date getDataUsuarioLibercaoChoqueHorario() {
		return dataUsuarioLibercaoChoqueHorario;
	}

	public void setDataUsuarioLibercaoChoqueHorario(Date dataUsuarioLibercaoChoqueHorario) {
		this.dataUsuarioLibercaoChoqueHorario = dataUsuarioLibercaoChoqueHorario;
	}
}
