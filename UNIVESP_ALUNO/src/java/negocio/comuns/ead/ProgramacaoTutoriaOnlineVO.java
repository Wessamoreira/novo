package negocio.comuns.ead;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.ead.enumeradores.RegraDistribuicaoTutoriaEnum;
import negocio.comuns.ead.enumeradores.SituacaoProgramacaoTutoriaOnlineEnum;
import negocio.comuns.ead.enumeradores.TipoNivelProgramacaoTutoriaEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

/**
 * @author Victor Hugo 11/11/2014
 */
public class ProgramacaoTutoriaOnlineVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private String nivelEducacional;
	private CursoVO cursoVO;
	private TurmaVO turmaVO;
	private DisciplinaVO disciplinaVO;
	private Integer qtdeAlunos;
	private Date dataAlteracao;
	@JsonManagedReference
	private List<ProgramacaoTutoriaOnlineProfessorVO> programacaoTutoriaOnlineProfessorVOs;
	private RegraDistribuicaoTutoriaEnum regraDistribuicaoTutoria;
	private SituacaoProgramacaoTutoriaOnlineEnum situacaoProgramacaoTutoriaOnline;
	private TipoNivelProgramacaoTutoriaEnum tipoNivelProgramacaoTutoria;
	private Integer ordemAtual;
	private Boolean definirPeriodoAulaOnline;
	private Date dataInicioAula;
	private Date dataTerminoAula;
	private String ano;
	private String semestre;
	private boolean executarClassroomAutomatico = false;
	private boolean executarSalaAulaBlackboardAutomatico = false;
	@ExcluirJsonAnnotation
	private List<SalaAulaBlackboardVO> listaSalaAulaBlackboardVO;
	
	 /**
     * Atributo transient, utilizado somente para saber se o atributo dataInicioAula foi alterada
     */
	private Boolean dataInicioAulaAlterada = false;
	 /**
     * Atributo transient, utilizado somente para saber se o atributo dataTerminoAula foi alterada
     */
	private Boolean dataTerminoAulaAlterada = false;
	 
	
	
	// Getters and Setters
	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}
	
	public String nivelEducacional_apresentar;
	public String getNivelEducacional_apresentar() {
		if(nivelEducacional_apresentar == null) {
		if (Uteis.isAtributoPreenchido(nivelEducacional)) {
			nivelEducacional_apresentar = TipoNivelEducacional.getDescricao(nivelEducacional); 
		}else {
			nivelEducacional_apresentar ="";
		}
		}
		return nivelEducacional_apresentar;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

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

	public Integer getQtdeAlunos() {
		if (qtdeAlunos == null) {
			qtdeAlunos = 0;
		}
		return qtdeAlunos;
	}

	public void setQtdeAlunos(Integer qtdeAlunos) {
		this.qtdeAlunos = qtdeAlunos;
	}

	public Date getDataAlteracao() {
		if (dataAlteracao == null) {
			dataAlteracao = new Date();
		}
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public List<ProgramacaoTutoriaOnlineProfessorVO> getProgramacaoTutoriaOnlineProfessorVOs() {
		if (programacaoTutoriaOnlineProfessorVOs == null) {
			programacaoTutoriaOnlineProfessorVOs = new ArrayList<ProgramacaoTutoriaOnlineProfessorVO>();
		}
		return programacaoTutoriaOnlineProfessorVOs;
	}

	public void setProgramacaoTutoriaOnlineProfessorVOs(List<ProgramacaoTutoriaOnlineProfessorVO> programacaoTutoriaOnlineProfessorVOs) {
		this.programacaoTutoriaOnlineProfessorVOs = programacaoTutoriaOnlineProfessorVOs;
	}

	public RegraDistribuicaoTutoriaEnum getRegraDistribuicaoTutoria() {
		if (regraDistribuicaoTutoria == null) {
			regraDistribuicaoTutoria = RegraDistribuicaoTutoriaEnum.SEQUENCIADA;
		}
		return regraDistribuicaoTutoria;
	}

	public void setRegraDistribuicaoTutoria(RegraDistribuicaoTutoriaEnum regraDistribuicaoTutoria) {
		this.regraDistribuicaoTutoria = regraDistribuicaoTutoria;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public SituacaoProgramacaoTutoriaOnlineEnum getSituacaoProgramacaoTutoriaOnline() {
		if (situacaoProgramacaoTutoriaOnline == null) {
			situacaoProgramacaoTutoriaOnline = SituacaoProgramacaoTutoriaOnlineEnum.INATIVO;
		}
		return situacaoProgramacaoTutoriaOnline;
	}

	public void setSituacaoProgramacaoTutoriaOnline(SituacaoProgramacaoTutoriaOnlineEnum situacaoProgramacaoTutoriaOnline) {
		this.situacaoProgramacaoTutoriaOnline = situacaoProgramacaoTutoriaOnline;
	}

	public TipoNivelProgramacaoTutoriaEnum getTipoNivelProgramacaoTutoria() {
		if (tipoNivelProgramacaoTutoria == null) {
			tipoNivelProgramacaoTutoria = TipoNivelProgramacaoTutoriaEnum.DISCIPLINA;
		}
		return tipoNivelProgramacaoTutoria;
	}

	public void setTipoNivelProgramacaoTutoria(TipoNivelProgramacaoTutoriaEnum tipoNivelProgramacaoTutoria) {
		this.tipoNivelProgramacaoTutoria = tipoNivelProgramacaoTutoria;
	}
	
	public String getTipoNIvelProgramacaoTutoria_Apresentar() {
		return getTipoNivelProgramacaoTutoria().getValorApresentar();
	}

	public Integer getOrdemAtual() {
		if (ordemAtual == null) {
			ordemAtual = 0;
		}
		return ordemAtual;
	}

	public void setOrdemAtual(Integer ordemAtual) {
		this.ordemAtual = ordemAtual;
	}

	public Boolean getDefinirPeriodoAulaOnline() {
		if(definirPeriodoAulaOnline == null) {
			definirPeriodoAulaOnline = false;
		}
		return definirPeriodoAulaOnline;
	}

	public void setDefinirPeriodoAulaOnline(Boolean definirPeriodoAulaOnline) {
		this.definirPeriodoAulaOnline = definirPeriodoAulaOnline;
	}

	public Date getDataInicioAula() {
		return dataInicioAula;
	}

	public void setDataInicioAula(Date dataInicioAula) {
		this.dataInicioAula = dataInicioAula;
		if(getCodigo() != null || getCodigo() > 0 ) {
			setDataInicioAulaAlterada(true);
		}
	}

	public Date getDataTerminoAula() {
		return dataTerminoAula;
	}

	public void setDataTerminoAula(Date dataTerminoAula) {
		this.dataTerminoAula = dataTerminoAula;
		if(getCodigo() != null || getCodigo() > 0 ) {
			setDataTerminoAulaAlterada(true);
		}
	}

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public Boolean getDataInicioAulaAlterada() {
		return dataInicioAulaAlterada;
	}

	public void setDataInicioAulaAlterada(Boolean dataInicioAulaAlterada) {
		this.dataInicioAulaAlterada = dataInicioAulaAlterada;
	}

	public Boolean getDataTerminoAulaAlterada() {
		return dataTerminoAulaAlterada;
	}

	public void setDataTerminoAulaAlterada(Boolean dataTerminoAulaAlterada) {
		this.dataTerminoAulaAlterada = dataTerminoAulaAlterada;
	}

	public boolean isExecutarClassroomAutomatico() {
		return executarClassroomAutomatico;
	}

	public void setExecutarClassroomAutomatico(boolean executarClassroomAutomatico) {
		this.executarClassroomAutomatico = executarClassroomAutomatico;
	}

	public boolean isExecutarSalaAulaBlackboardAutomatico() {
		return executarSalaAulaBlackboardAutomatico;
	}

	public void setExecutarSalaAulaBlackboardAutomatico(boolean executarSalaAulaBlackboardAutomatico) {
		this.executarSalaAulaBlackboardAutomatico = executarSalaAulaBlackboardAutomatico;
	}

	public List<SalaAulaBlackboardVO> getListaSalaAulaBlackboardVO() {
		if(listaSalaAulaBlackboardVO == null) {
			listaSalaAulaBlackboardVO =  new ArrayList<SalaAulaBlackboardVO>();
		}
		return listaSalaAulaBlackboardVO;
	}

	public void setListaSalaAulaBlackboardVO(List<SalaAulaBlackboardVO> listaSalaAulaBlackboardVO) {
		this.listaSalaAulaBlackboardVO = listaSalaAulaBlackboardVO;
	}
	
	
	
	
	
	
}
