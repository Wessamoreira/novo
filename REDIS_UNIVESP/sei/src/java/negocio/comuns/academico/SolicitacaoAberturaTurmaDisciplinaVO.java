package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.FeriadoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;

public class SolicitacaoAberturaTurmaDisciplinaVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2795233219412605065L;
	private Integer codigo;
	private Integer numeroModulo;
	private Date dataInicio;
	private Date dataTermino;
	private SolicitacaoAberturaTurmaVO solicitacaoAberturaTurma;
	private Boolean revisar;
	private String motivoRevisao;
	private PessoaVO professor;
	private DisciplinaVO disciplina;
	
	
	/**Transient**/
	private FeriadoVO feriado;
	private Boolean mesAnterior = false;
	
	
		
	public SolicitacaoAberturaTurmaDisciplinaVO() {
		super();
	}

	public SolicitacaoAberturaTurmaDisciplinaVO(Integer numeroModulo, Date dataInicio, Boolean mesAnterior) {
		super();
		this.numeroModulo = numeroModulo;
		this.dataInicio = dataInicio;
		this.mesAnterior = mesAnterior;
	}
	
	
	
	public FeriadoVO getFeriado() {
		if(feriado == null){
			feriado = new FeriadoVO();
		}
		return feriado;
	}

	public void setFeriado(FeriadoVO feriadoVO) {
		this.feriado = feriadoVO;
	}

	public Boolean getIsFeriado(){
		return !getFeriado().getCodigo().equals(0);
	}
	public Boolean getIsAulaProgramada(){
		return !getNumeroModulo().equals(0);
	}
	
	public String getDia(){
		return Uteis.getData(getDataInicio(), "dd");
	}

	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}
	
	public String getNomeModulo(){
		return getNumeroModulo()+"º Módulo";
	}
	
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	public Integer getNumeroModulo() {
		if(numeroModulo == null){
			numeroModulo = 0;
		}
		return numeroModulo;
	}
	public void setNumeroModulo(Integer numeroModulo) {
		this.numeroModulo = numeroModulo;
	}
	public Date getDataInicio() {
		return dataInicio;
	}
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}
	public Date getDataTermino() {
		return dataTermino;
	}
	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}
	public SolicitacaoAberturaTurmaVO getSolicitacaoAberturaTurma() {
		if(solicitacaoAberturaTurma == null){
			solicitacaoAberturaTurma = new SolicitacaoAberturaTurmaVO();
		}
		return solicitacaoAberturaTurma;
	}
	public void setSolicitacaoAberturaTurma(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurma) {
		this.solicitacaoAberturaTurma = solicitacaoAberturaTurma;
	}

	public Boolean getRevisar() {
		if(revisar == null){
			revisar = false;
		}
		return revisar;
	}

	public void setRevisar(Boolean revisar) {
		this.revisar = revisar;
	}

	public String getMotivoRevisao() {
		if(motivoRevisao == null){
			motivoRevisao = "";
		}
		return motivoRevisao;
	}

	public void setMotivoRevisao(String motivoRevisao) {
		this.motivoRevisao = motivoRevisao;
	}

	public PessoaVO getProfessor() {
		if(professor == null){
			professor = new PessoaVO();
		}
		return professor;
	}

	public void setProfessor(PessoaVO professor) {
		this.professor = professor;
	}

	public DisciplinaVO getDisciplina() {
		if(disciplina == null){
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
	}

	public Boolean getMesAnterior() {
		if(mesAnterior == null){
			mesAnterior = false;
		}
		return mesAnterior;
	}

	public void setMesAnterior(Boolean mesAnterior) {
		this.mesAnterior = mesAnterior;
	}
	
	
	

}
