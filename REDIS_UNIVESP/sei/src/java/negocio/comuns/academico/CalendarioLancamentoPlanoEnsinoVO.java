package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SemestreEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;

public class CalendarioLancamentoPlanoEnsinoVO extends SuperVO {
	
	private static final long serialVersionUID = 1912631754862048668L;

	private Integer codigo;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private String ano;
	private SemestreEnum semestre;
	private CursoVO cursoVO;
	private TurnoVO turnoVO;
	private DisciplinaVO disciplinaVO;
	private PeriodicidadeEnum periodicidade;
	private PessoaVO professor;
	private Date dataInicio;
	private Date dataFim;
	private String calendarioPor;
	
	public enum EnumCampoConsultaCalendarioLancamentoPlanoEnsino {
		CODIGO;
	}

	public enum EnumCampoCalendarioPor {
		UNIDADE_ENSINO, PROFESSOR, CURSO;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public SemestreEnum getSemestre() {
		return semestre;
	}

	public String getSemestreApresentar() {
		if (Uteis.isAtributoPreenchido(getSemestre())) {
			return getSemestre().getDescricao();
		}
		return "";
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public TurnoVO getTurnoVO() {
		if (turnoVO == null) {
			turnoVO = new TurnoVO();
		}
		return turnoVO;
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public PeriodicidadeEnum getPeriodicidade() {
		if (periodicidade == null) {
			periodicidade = PeriodicidadeEnum.SEMESTRAL;
		}
		return periodicidade;
	}

	public PessoaVO getProfessor() {
		if (professor == null) {
			professor = new PessoaVO();
		}
		return professor;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
			//dataInicio = Uteis.getDataInicioSemestreAno(UteisData.getAnoData(new Date()), Integer.parseInt(getSemestre().getValor()));
		}
		return dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = new Date();
			//dataFim = Uteis.getDataFimSemestreAno(UteisData.getAnoData(new Date()), Integer.parseInt(getSemestre().getValor()));
		}
		return dataFim;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public void setSemestre(SemestreEnum semestre) {
		this.semestre = semestre;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public void setTurnoVO(TurnoVO turnoVO) {
		this.turnoVO = turnoVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public void setPeriodicidade(PeriodicidadeEnum periodicidade) {
		this.periodicidade = periodicidade;
	}

	public void setProfessor(PessoaVO professor) {
		this.professor = professor;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public String getCalendarioPor() {
		if (calendarioPor == null) {
			calendarioPor = "";
		}
		return calendarioPor;
	}

	public void setCalendarioPor(String calendarioPor) {
		this.calendarioPor = calendarioPor;
	}

	public String getPeriodicidadeApresentar() {
        if (periodicidade.equals(PeriodicidadeEnum.ANUAL)) {
            return "Anual";
        }
        if (periodicidade.equals(PeriodicidadeEnum.SEMESTRAL)) {
            return "Semestral";
        }
        return periodicidade.getValor();
	}
}
