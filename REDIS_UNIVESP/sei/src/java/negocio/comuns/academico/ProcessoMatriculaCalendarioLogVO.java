package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.SuperVO;

public class ProcessoMatriculaCalendarioLogVO extends SuperVO {

	private Integer processoMatricula;
    private Date dataInicioMatricula;
    private Date dataFinalMatricula;
    private Date dataInicioInclusaoDisciplina;
    private Date dataFinalInclusaoDisciplina;
    private Date dataInicioMatForaPrazo;
    private Date dataFinalMatForaPrazo;
    private Date dataVencimentoMatricula;
    private Integer mesVencimentoPrimeiraMensalidade;
    private Integer diaVencimentoPrimeiraMensalidade;
    private Integer anoVencimentoPrimeiraMensalidade;
    private Boolean usarDataVencimentoDataMatricula;
    private Boolean mesSubsequenteMatricula;
    private Boolean mesDataBaseGeracaoParcelas;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Curso </code>.
     */
    private CursoVO cursoVO;
    private TurnoVO turnoVO;
    private PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivolUnidadeEnsinoCursoVO;
    private String operacao;
    private Integer responsavel;
    
	public Integer getProcessoMatricula() {
		if (processoMatricula == null) {
			processoMatricula = 0;
		}
		return processoMatricula;
	}
	public void setProcessoMatricula(Integer processoMatricula) {
		this.processoMatricula = processoMatricula;
	}
	public Date getDataInicioMatricula() {
		return dataInicioMatricula;
	}
	public void setDataInicioMatricula(Date dataInicioMatricula) {
		this.dataInicioMatricula = dataInicioMatricula;
	}
	public Date getDataFinalMatricula() {
		return dataFinalMatricula;
	}
	public void setDataFinalMatricula(Date dataFinalMatricula) {
		this.dataFinalMatricula = dataFinalMatricula;
	}
	public Date getDataInicioInclusaoDisciplina() {
		return dataInicioInclusaoDisciplina;
	}
	public void setDataInicioInclusaoDisciplina(Date dataInicioInclusaoDisciplina) {
		this.dataInicioInclusaoDisciplina = dataInicioInclusaoDisciplina;
	}
	public Date getDataFinalInclusaoDisciplina() {
		return dataFinalInclusaoDisciplina;
	}
	public void setDataFinalInclusaoDisciplina(Date dataFinalInclusaoDisciplina) {
		this.dataFinalInclusaoDisciplina = dataFinalInclusaoDisciplina;
	}
	public Date getDataInicioMatForaPrazo() {
		return dataInicioMatForaPrazo;
	}
	public void setDataInicioMatForaPrazo(Date dataInicioMatForaPrazo) {
		this.dataInicioMatForaPrazo = dataInicioMatForaPrazo;
	}
	public Date getDataFinalMatForaPrazo() {
		return dataFinalMatForaPrazo;
	}
	public void setDataFinalMatForaPrazo(Date dataFinalMatForaPrazo) {
		this.dataFinalMatForaPrazo = dataFinalMatForaPrazo;
	}
	public Date getDataVencimentoMatricula() {
		return dataVencimentoMatricula;
	}
	public void setDataVencimentoMatricula(Date dataVencimentoMatricula) {
		this.dataVencimentoMatricula = dataVencimentoMatricula;
	}
	public Integer getMesVencimentoPrimeiraMensalidade() {
		return mesVencimentoPrimeiraMensalidade;
	}
	public void setMesVencimentoPrimeiraMensalidade(
			Integer mesVencimentoPrimeiraMensalidade) {
		this.mesVencimentoPrimeiraMensalidade = mesVencimentoPrimeiraMensalidade;
	}
	public Integer getDiaVencimentoPrimeiraMensalidade() {
		return diaVencimentoPrimeiraMensalidade;
	}
	public void setDiaVencimentoPrimeiraMensalidade(
			Integer diaVencimentoPrimeiraMensalidade) {
		this.diaVencimentoPrimeiraMensalidade = diaVencimentoPrimeiraMensalidade;
	}
	public Integer getAnoVencimentoPrimeiraMensalidade() {
		return anoVencimentoPrimeiraMensalidade;
	}
	public void setAnoVencimentoPrimeiraMensalidade(
			Integer anoVencimentoPrimeiraMensalidade) {
		this.anoVencimentoPrimeiraMensalidade = anoVencimentoPrimeiraMensalidade;
	}
	public Boolean getUsarDataVencimentoDataMatricula() {
		if (usarDataVencimentoDataMatricula == null) {
			usarDataVencimentoDataMatricula = Boolean.FALSE;
		}
		return usarDataVencimentoDataMatricula;
	}
	public void setUsarDataVencimentoDataMatricula(
			Boolean usarDataVencimentoDataMatricula) {
		this.usarDataVencimentoDataMatricula = usarDataVencimentoDataMatricula;
	}
	public Boolean getMesSubsequenteMatricula() {
		if (mesSubsequenteMatricula == null) {
            mesSubsequenteMatricula = Boolean.FALSE;
        }
		return mesSubsequenteMatricula;
	}
	public void setMesSubsequenteMatricula(Boolean mesSubsequenteMatricula) {
		this.mesSubsequenteMatricula = mesSubsequenteMatricula;
	}
	public Boolean getMesDataBaseGeracaoParcelas() {
		if (mesDataBaseGeracaoParcelas == null) {
            mesDataBaseGeracaoParcelas = Boolean.FALSE;
        }
		return mesDataBaseGeracaoParcelas;
	}
	public void setMesDataBaseGeracaoParcelas(Boolean mesDataBaseGeracaoParcelas) {
		this.mesDataBaseGeracaoParcelas = mesDataBaseGeracaoParcelas;
	}
	
	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO getPeriodoLetivoAtivolUnidadeEnsinoCursoVO() {
		if (periodoLetivoAtivolUnidadeEnsinoCursoVO == null) {
            periodoLetivoAtivolUnidadeEnsinoCursoVO = new PeriodoLetivoAtivoUnidadeEnsinoCursoVO();
        }
		return periodoLetivoAtivolUnidadeEnsinoCursoVO;
	}
	public void setPeriodoLetivoAtivolUnidadeEnsinoCursoVO(
			PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivolUnidadeEnsinoCursoVO) {
		this.periodoLetivoAtivolUnidadeEnsinoCursoVO = periodoLetivoAtivolUnidadeEnsinoCursoVO;
	}
	public String getOperacao() {
		if (operacao == null) {
			operacao = "";
		}
		return operacao;
	}
	public void setOperacao(String operacao) {
		this.operacao = operacao;
	}
	public Integer getResponsavel() {
		if (responsavel == null) {
			responsavel = 0;
		}
		return responsavel;
	}
	public void setResponsavel(Integer responsavel) {
		this.responsavel = responsavel;
	}
	public CursoVO getCursoVO() {
		if(cursoVO == null) {
			cursoVO =  new CursoVO();
		}
		return cursoVO;
	}
	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}
	public TurnoVO getTurnoVO() {
		if(turnoVO == null) {
			turnoVO =  new TurnoVO();
		}
		return turnoVO;
	}
	public void setTurnoVO(TurnoVO turnoVO) {
		this.turnoVO = turnoVO;
	}

	
}
