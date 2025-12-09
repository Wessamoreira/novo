package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

/**
 * Reponsável por manter os dados da entidade
 * PeriodoLetivoAtivoUnidadeEnsinoCurso. Classe do tipo VO - Value Object
 * composta pelos atributos da entidade com visibilidade protegida e os métodos
 * de acesso a estes atributos. Classe utilizada para apresentar e manter em
 * memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class PeriodoLetivoAtivoUnidadeEnsinoCursoVO extends SuperVO implements Cloneable {

	private Integer codigo;
	private String situacao;
    private CursoVO cursoVO;
    private TurnoVO turnoVO;
	private String semestreReferenciaPeriodoLetivo;
	private String anoReferenciaPeriodoLetivo;
	private String tipoPeriodoLetivo;
	private Date dataInicioPeriodoLetivo;
	private Date dataFimPeriodoLetivo;
	private Date dataAbertura;
	private UsuarioVO reponsavelAbertura;
	private Date dataFechamento;
	private UsuarioVO responsavelFechamento;
	private TurmaVO turma;

	private Date dataInicioPeriodoLetivoPrimeiroBimestre;
	private Date dataFimPeriodoLetivoPrimeiroBimestre;
	private Date dataInicioPeriodoLetivoSegundoBimestre;
	private Date dataFimPeriodoLetivoSegundoBimestre;
	private Date dataInicioPeriodoLetivoTerceiroBimestre;
	private Date dataFimPeriodoLetivoTerceiroBimestre;
	private Date dataInicioPeriodoLetivoQuartoBimestre;
	private Date dataFimPeriodoLetivoQuartoBimestre;
	private Integer qtdeDiaLetivoPrimeiroBimestre;
	private Integer qtdeSemanaLetivaPrimeiroBimestre;
	private Integer qtdeDiaLetivoSegundoBimestre;
	private Integer qtdeSemanaLetivaSegundoBimestre;
	private Integer qtdeDiaLetivoTerceiroBimestre;
	private Integer qtdeSemanaLetivaTerceiroBimestre;
	private Integer qtdeDiaLetivoQuartoBimestre;
	private Integer qtdeSemanaLetivaQuartoBimestre;
	private Integer totalDiaLetivoAno;
	private Integer totalSemanaLetivaAno;
	private UnidadeEnsinoCursoVO unidadeEnsinoCurso;
	/**
	 * Transient
	 */
	private Boolean selecionarPeriodoLetivoAtivoUnidadeEnsinoCursoFinalizar;
	private ProcessoMatriculaVO processoMatriculaVO;
	private Integer qtdeAlunosAtivos;
	/**
	 * Fim Transient
	 */

	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe
	 * <code>PeriodoLetivoAtivoUnidadeEnsinoCurso</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO() {
		super();
		inicializarDados();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>PeriodoLetivoAtivoUnidadeEnsinoCursoVO</code>. Todos os tipos de
	 * consistência de dados são e devem ser implementadas neste método. São
	 * validações típicas: verificação de campos obrigatórios, verificação de
	 * valores válidos para os atributos.
	 *
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj, String nivelProcessoMatricula) throws ConsistirException {
		String cursoTurno = "(Curso: "+(obj.getCursoVO().getNome())+" e Turno: "+obj.getTurnoVO().getNome()+")";
		if (!nivelProcessoMatricula.equals("PO") && !nivelProcessoMatricula.equals("EX")) {
			if (!obj.isValidarDados().booleanValue()) {
				return;
			}
			if (obj.getSituacao().equals("") || obj.getSituacao() == null) {
				throw new ConsistirException("O campo SITUAÇÃO "+cursoTurno+" deve ser informado.");
			}
			if (obj.getCursoVO() == null || obj.getCursoVO().getCodigo().intValue() == 0 ) {
				throw new ConsistirException("O campo CURSO "+cursoTurno+" deve ser informado.");
			}
			if (obj.getTurnoVO() == null || obj.getTurnoVO().getCodigo().intValue() == 0 ) {
				throw new ConsistirException("O campo TURNO "+cursoTurno+" deve ser informado.");
			}
			if (obj.getTipoPeriodoLetivo().equals("")) {
				throw new ConsistirException("O campo TIPO PERIODO LETIVO "+cursoTurno+" deve ser informado.");
			}
			if (obj.getTipoPeriodoLetivo().equals("SE")) {
				if (obj.getSemestreReferenciaPeriodoLetivo().equals("") || obj.getSemestreReferenciaPeriodoLetivo() == null) {
					throw new ConsistirException("O campo SEMESTRE PERIODO LETIVO "+cursoTurno+" deve ser informado.");
				}
				if (obj.getAnoReferenciaPeriodoLetivo().equals("") || obj.getAnoReferenciaPeriodoLetivo() == null) {
					throw new ConsistirException("O campo ANO PERIODO LETIVO "+cursoTurno+" deve ser informado.");
				} else if (obj.getAnoReferenciaPeriodoLetivo().length() < 4) {
					throw new ConsistirException("O campo ANO PERIODO LETIVO "+cursoTurno+" deve ter 4 dígitos.");
				}
			} else {
				obj.setSemestreReferenciaPeriodoLetivo("");
			}
			if (obj.getTipoPeriodoLetivo().equals("AN")) {
				if (obj.getAnoReferenciaPeriodoLetivo().equals("") || obj.getAnoReferenciaPeriodoLetivo() == null) {
					throw new ConsistirException("O campo ANO REFERÊNCIA PERIODO LETIVO "+cursoTurno+" deve ser informado.");
				} else if (obj.getAnoReferenciaPeriodoLetivo().length() < 4) {
					throw new ConsistirException("O campo ANO PERIODO LETIVO "+cursoTurno+" deve ter 4 dígitos.");
				}
			}
			if (obj.getTipoPeriodoLetivo().equals("IN")) {
				obj.setAnoReferenciaPeriodoLetivo("");			
			}
			if (obj.getDataInicioPeriodoLetivo() == null) {
				throw new ConsistirException("O campo DATA INICIO PERIODO LETIVO "+cursoTurno+" deve ser informado.");
			}
			if (obj.getDataFimPeriodoLetivo() == null) {
				throw new ConsistirException("O campo DATA FIM PERIODO LETIVO "+cursoTurno+" deve ser informado.");
			}
			if (obj.getDataFimPeriodoLetivo().before(obj.getDataInicioPeriodoLetivo())) {
				throw new ConsistirException("O campo DATA FIM PERIODO LETIVO deve ser posterior à DATA INICIO PERIODO LETIVO "+cursoTurno+".");
			}
			if (obj.getDataAbertura() == null) {
				throw new ConsistirException("O campo DATA DA ABERTURA "+cursoTurno+" deve ser informado.");
			}
		}
		if (nivelProcessoMatricula.equals("ME") || nivelProcessoMatricula.equals("BA") || nivelProcessoMatricula.equals("IN")) {
			if (obj.getDataInicioPeriodoLetivoPrimeiroBimestre() == null || obj.getDataFimPeriodoLetivoPrimeiroBimestre() == null) {
				throw new ConsistirException("O campo Período Letivo 1º Bimestre " + cursoTurno + " deve ser informado INTEGRALMENTE.");
			}
			if (obj.getDataInicioPeriodoLetivoSegundoBimestre() == null || obj.getDataFimPeriodoLetivoSegundoBimestre() == null) {
				throw new ConsistirException("O campo Período Letivo 2º Bimestre " + cursoTurno + " deve ser informado INTEGRALMENTE.");
			}
			if (obj.getDataInicioPeriodoLetivoTerceiroBimestre() == null || obj.getDataFimPeriodoLetivoTerceiroBimestre() == null) {
				throw new ConsistirException("O campo Período Letivo 3º Bimestre " + cursoTurno + " deve ser informado INTEGRALMENTE.");
			}
			if (obj.getDataInicioPeriodoLetivoQuartoBimestre() == null || obj.getDataFimPeriodoLetivoQuartoBimestre() == null) {
				throw new ConsistirException("O campo Período Letivo 4º Bimestre " + cursoTurno + " deve ser informado INTEGRALMENTE.");
			}
		}
	}

	public PeriodoLetivoAtivoUnidadeEnsinoCursoVO clone() throws CloneNotSupportedException {
		PeriodoLetivoAtivoUnidadeEnsinoCursoVO obj = (PeriodoLetivoAtivoUnidadeEnsinoCursoVO) super.clone();
		return obj;
	}

	/**
	 * Operação reponsável por inicializar os atributos da classe.
	 */
	public void inicializarDados() {
		setCodigo(0);
		setSituacao("AT");
		setSemestreReferenciaPeriodoLetivo("");
		setAnoReferenciaPeriodoLetivo("");
		setTipoPeriodoLetivo("");
		setDataAbertura(new Date());
		setDataFechamento(new Date());
	}

	public UsuarioVO getResponsavelFechamento() {
		if (responsavelFechamento == null) {
			responsavelFechamento = new UsuarioVO();
		}
		return (responsavelFechamento);
	}

	public void setResponsavelFechamento(UsuarioVO responsavelFechamento) {
		this.responsavelFechamento = responsavelFechamento;
	}

	public Date getDataFechamento() {
		return (dataFechamento);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataFechamento_Apresentar() {
		return (Uteis.getData(dataFechamento));
	}

	public void setDataFechamento(Date dataFechamento) {
		this.dataFechamento = dataFechamento;
	}

	public UsuarioVO getReponsavelAbertura() {
		if (reponsavelAbertura == null) {
			reponsavelAbertura = new UsuarioVO();
		}
		return (reponsavelAbertura);
	}

	public void setReponsavelAbertura(UsuarioVO reponsavelAbertura) {
		this.reponsavelAbertura = reponsavelAbertura;
	}

	public Date getDataAbertura() {
		return (dataAbertura);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataAbertura_Apresentar() {
		return (Uteis.getData(dataAbertura));
	}

	public void setDataAbertura(Date dataAbertura) {
		this.dataAbertura = dataAbertura;
	}

	public Date getDataFimPeriodoLetivo() {
		return (dataFimPeriodoLetivo);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataFimPeriodoLetivo_Apresentar() {
		return (Uteis.getData(dataFimPeriodoLetivo));
	}

	public void setDataFimPeriodoLetivo(Date dataFimPeriodoLetivo) {
		this.dataFimPeriodoLetivo = dataFimPeriodoLetivo;
	}

	public Date getDataInicioPeriodoLetivo() {
		return (dataInicioPeriodoLetivo);
	}

	/**
	 * Operação responsável por retornar um atributo do tipo data no formato
	 * padrão dd/mm/aaaa.
	 */
	public String getDataInicioPeriodoLetivo_Apresentar() {
		return (Uteis.getData(dataInicioPeriodoLetivo));
	}

	public void setDataInicioPeriodoLetivo(Date dataInicioPeriodoLetivo) {
		this.dataInicioPeriodoLetivo = dataInicioPeriodoLetivo;
	}

	public String getTipoPeriodoLetivo_Apresentar() {
		if (tipoPeriodoLetivo.equals("SE")) {
			return "Semestral";
		}
		if (tipoPeriodoLetivo.equals("AN")) {
			return "Anual";
		}
		if (tipoPeriodoLetivo.equals("IN")) {
			return "Integral";
		}
		return (tipoPeriodoLetivo);
	}

	public String getTipoPeriodoLetivo() {
		if (tipoPeriodoLetivo == null) {
			tipoPeriodoLetivo = "";
		}
		return (tipoPeriodoLetivo);
	}

	public void setTipoPeriodoLetivo(String tipoPeriodoLetivo) {
		this.tipoPeriodoLetivo = tipoPeriodoLetivo;
	}

	public String getAnoReferenciaPeriodoLetivo() {
		return (anoReferenciaPeriodoLetivo);
	}

	public void setAnoReferenciaPeriodoLetivo(String anoReferenciaPeriodoLetivo) {
		this.anoReferenciaPeriodoLetivo = anoReferenciaPeriodoLetivo;
	}

	public String getSemestreAno() {
		if (tipoPeriodoLetivo.equals("SE")) {
			return (semestreReferenciaPeriodoLetivo + "/" + anoReferenciaPeriodoLetivo);
		} else if (tipoPeriodoLetivo.equals("IN")) {
			return "";
		} else {
			return anoReferenciaPeriodoLetivo;
		}
	}

	public String getSemestreReferenciaPeriodoLetivo() {
		return (semestreReferenciaPeriodoLetivo);
	}

	public void setSemestreReferenciaPeriodoLetivo(String semestreReferenciaPeriodoLetivo) {
		this.semestreReferenciaPeriodoLetivo = semestreReferenciaPeriodoLetivo;
	}


	public String getSituacao_Apresentar() {
		if (situacao.equals("AT")) {
			return "Ativo";
		}
		if (situacao.equals("FI")) {
			return "Fechado";
		}
		return (situacao);
	}

	public String getSituacao() {
		return (situacao);
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public Integer getCodigo() {
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public void setTurma(TurmaVO turma) {
		this.turma = turma;
	}

	public TurmaVO getTurma() {
		if (turma == null) {
			turma = new TurmaVO();
		}
		return turma;
	}

	public Boolean getDataFimPeriodoLetivoAposADataAtual() {
		if (getDataFimPeriodoLetivo() != null) {
			return getDataFimPeriodoLetivo().after(new Date());
		}
		return true;
	}

	public Date getDataInicioPeriodoLetivoPrimeiroBimestre() {
		return dataInicioPeriodoLetivoPrimeiroBimestre;
	}

	public void setDataInicioPeriodoLetivoPrimeiroBimestre(Date dataInicioPeriodoLetivoPrimeiroBimestre) {
		this.dataInicioPeriodoLetivoPrimeiroBimestre = dataInicioPeriodoLetivoPrimeiroBimestre;
	}

	public Date getDataFimPeriodoLetivoPrimeiroBimestre() {
		return dataFimPeriodoLetivoPrimeiroBimestre;
	}

	public void setDataFimPeriodoLetivoPrimeiroBimestre(Date dataFimPeriodoLetivoPrimeiroBimestre) {
		this.dataFimPeriodoLetivoPrimeiroBimestre = dataFimPeriodoLetivoPrimeiroBimestre;
	}

	public Date getDataInicioPeriodoLetivoSegundoBimestre() {
		return dataInicioPeriodoLetivoSegundoBimestre;
	}

	public void setDataInicioPeriodoLetivoSegundoBimestre(Date dataInicioPeriodoLetivoSegundoBimestre) {
		this.dataInicioPeriodoLetivoSegundoBimestre = dataInicioPeriodoLetivoSegundoBimestre;
	}

	public Date getDataFimPeriodoLetivoSegundoBimestre() {
		return dataFimPeriodoLetivoSegundoBimestre;
	}

	public void setDataFimPeriodoLetivoSegundoBimestre(Date dataFimPeriodoLetivoSegundoBimestre) {
		this.dataFimPeriodoLetivoSegundoBimestre = dataFimPeriodoLetivoSegundoBimestre;
	}

	public Date getDataInicioPeriodoLetivoTerceiroBimestre() {
		return dataInicioPeriodoLetivoTerceiroBimestre;
	}

	public void setDataInicioPeriodoLetivoTerceiroBimestre(Date dataInicioPeriodoLetivoTerceiroBimestre) {
		this.dataInicioPeriodoLetivoTerceiroBimestre = dataInicioPeriodoLetivoTerceiroBimestre;
	}

	public Date getDataFimPeriodoLetivoTerceiroBimestre() {
		return dataFimPeriodoLetivoTerceiroBimestre;
	}

	public void setDataFimPeriodoLetivoTerceiroBimestre(Date dataFimPeriodoLetivoTerceiroBimestre) {
		this.dataFimPeriodoLetivoTerceiroBimestre = dataFimPeriodoLetivoTerceiroBimestre;
	}

	public Date getDataInicioPeriodoLetivoQuartoBimestre() {
		return dataInicioPeriodoLetivoQuartoBimestre;
	}

	public void setDataInicioPeriodoLetivoQuartoBimestre(Date dataInicioPeriodoLetivoQuartoBimestre) {
		this.dataInicioPeriodoLetivoQuartoBimestre = dataInicioPeriodoLetivoQuartoBimestre;
	}

	public Date getDataFimPeriodoLetivoQuartoBimestre() {
		return dataFimPeriodoLetivoQuartoBimestre;
	}

	public void setDataFimPeriodoLetivoQuartoBimestre(Date dataFimPeriodoLetivoQuartoBimestre) {
		this.dataFimPeriodoLetivoQuartoBimestre = dataFimPeriodoLetivoQuartoBimestre;
	}

	public Integer getQtdeDiaLetivoPrimeiroBimestre() {
		if (qtdeDiaLetivoPrimeiroBimestre == null) {
			qtdeDiaLetivoPrimeiroBimestre = 0;
		}
		return qtdeDiaLetivoPrimeiroBimestre;
	}

	public void setQtdeDiaLetivoPrimeiroBimestre(Integer qtdeDiaLetivoPrimeiroBimestre) {
		this.qtdeDiaLetivoPrimeiroBimestre = qtdeDiaLetivoPrimeiroBimestre;
	}

	public Integer getQtdeSemanaLetivaPrimeiroBimestre() {
		if (qtdeSemanaLetivaPrimeiroBimestre == null) {
			qtdeSemanaLetivaPrimeiroBimestre = 0;
		}
		return qtdeSemanaLetivaPrimeiroBimestre;
	}

	public void setQtdeSemanaLetivaPrimeiroBimestre(Integer qtdeSemanaLetivaPrimeiroBimestre) {
		this.qtdeSemanaLetivaPrimeiroBimestre = qtdeSemanaLetivaPrimeiroBimestre;
	}

	public Integer getQtdeDiaLetivoSegundoBimestre() {
		if (qtdeDiaLetivoSegundoBimestre == null) {
			qtdeDiaLetivoSegundoBimestre = 0;
		}
		return qtdeDiaLetivoSegundoBimestre;
	}

	public void setQtdeDiaLetivoSegundoBimestre(Integer qtdeDiaLetivoSegundoBimestre) {
		this.qtdeDiaLetivoSegundoBimestre = qtdeDiaLetivoSegundoBimestre;
	}

	public Integer getQtdeSemanaLetivaSegundoBimestre() {
		if (qtdeSemanaLetivaSegundoBimestre == null) {
			qtdeSemanaLetivaSegundoBimestre = 0;
		}
		return qtdeSemanaLetivaSegundoBimestre;
	}

	public void setQtdeSemanaLetivaSegundoBimestre(Integer qtdeSemanaLetivaSegundoBimestre) {
		this.qtdeSemanaLetivaSegundoBimestre = qtdeSemanaLetivaSegundoBimestre;
	}

	public Integer getQtdeDiaLetivoTerceiroBimestre() {
		if (qtdeDiaLetivoTerceiroBimestre == null) {
			qtdeDiaLetivoTerceiroBimestre = 0;
		}
		return qtdeDiaLetivoTerceiroBimestre;
	}

	public void setQtdeDiaLetivoTerceiroBimestre(Integer qtdeDiaLetivoTerceiroBimestre) {
		this.qtdeDiaLetivoTerceiroBimestre = qtdeDiaLetivoTerceiroBimestre;
	}

	public Integer getQtdeSemanaLetivaTerceiroBimestre() {
		if (qtdeSemanaLetivaTerceiroBimestre == null) {
			qtdeSemanaLetivaTerceiroBimestre = 0;
		}
		return qtdeSemanaLetivaTerceiroBimestre;
	}

	public void setQtdeSemanaLetivaTerceiroBimestre(Integer qtdeSemanaLetivaTerceiroBimestre) {
		this.qtdeSemanaLetivaTerceiroBimestre = qtdeSemanaLetivaTerceiroBimestre;
	}

	public Integer getQtdeDiaLetivoQuartoBimestre() {
		if (qtdeDiaLetivoQuartoBimestre == null) {
			qtdeDiaLetivoQuartoBimestre = 0;
		}
		return qtdeDiaLetivoQuartoBimestre;
	}

	public void setQtdeDiaLetivoQuartoBimestre(Integer qtdeDiaLetivoQuartoBimestre) {
		this.qtdeDiaLetivoQuartoBimestre = qtdeDiaLetivoQuartoBimestre;
	}

	public Integer getQtdeSemanaLetivaQuartoBimestre() {
		if (qtdeSemanaLetivaQuartoBimestre == null) {
			qtdeSemanaLetivaQuartoBimestre = 0;
		}
		return qtdeSemanaLetivaQuartoBimestre;
	}

	public void setQtdeSemanaLetivaQuartoBimestre(Integer qtdeSemanaLetivaQuartoBimestre) {
		this.qtdeSemanaLetivaQuartoBimestre = qtdeSemanaLetivaQuartoBimestre;
	}

	public Integer getTotalDiaLetivoAno() {
		if (totalDiaLetivoAno == null) {
			totalDiaLetivoAno = 0;
		}
		return totalDiaLetivoAno;
	}

	public void setTotalDiaLetivoAno(Integer totalDiaLetivoAno) {
		this.totalDiaLetivoAno = totalDiaLetivoAno;
	}

	public Integer getTotalSemanaLetivaAno() {
		if (totalSemanaLetivaAno == null) {
			totalSemanaLetivaAno = 0;
		}
		return totalSemanaLetivaAno;
	}

	public void setTotalSemanaLetivaAno(Integer totalSemanaLetivaAno) {
		this.totalSemanaLetivaAno = totalSemanaLetivaAno;
	}

	public Boolean getSelecionarPeriodoLetivoAtivoUnidadeEnsinoCursoFinalizar() {
		if (selecionarPeriodoLetivoAtivoUnidadeEnsinoCursoFinalizar == null) {
			selecionarPeriodoLetivoAtivoUnidadeEnsinoCursoFinalizar = false;
		}
		return selecionarPeriodoLetivoAtivoUnidadeEnsinoCursoFinalizar;
	}

	public void setSelecionarPeriodoLetivoAtivoUnidadeEnsinoCursoFinalizar(Boolean selecionarPeriodoLetivoAtivoUnidadeEnsinoCursoFinalizar) {
		this.selecionarPeriodoLetivoAtivoUnidadeEnsinoCursoFinalizar = selecionarPeriodoLetivoAtivoUnidadeEnsinoCursoFinalizar;
	}

	public boolean getIsPermitirSelecionarPeriodoLetivoAtivoUnidadeEnsinoCursoFinalizar() {
		if (getTurma().getIntegral()) {
			return true;
		} else {
			return Uteis.isAtributoPreenchido(getDataFimPeriodoLetivo()) && Uteis.getDataJDBC(getDataFimPeriodoLetivo()).before(Uteis.getDataJDBC(new Date()));
		}
	}

	public ProcessoMatriculaVO getProcessoMatriculaVO() {
		if (processoMatriculaVO == null) {
			processoMatriculaVO = new ProcessoMatriculaVO();
		}
		return processoMatriculaVO;
	}

	public void setProcessoMatriculaVO(ProcessoMatriculaVO processoMatriculaVO) {
		this.processoMatriculaVO = processoMatriculaVO;
	}

	public Integer getQtdeAlunosAtivos() {
		if (qtdeAlunosAtivos == null) {
			qtdeAlunosAtivos = 0;
		}
		return qtdeAlunosAtivos;
	}

	public void setQtdeAlunosAtivos(Integer qtdeAlunosAtivos) {
		this.qtdeAlunosAtivos = qtdeAlunosAtivos;
	}
	
	/**
	 * Atributo utilizado no fechamento do período letivo para validar apresentação da data início e data fim do período letivo,
	 * 
	 * @return
	 */
	public boolean getIsApresentarDataInicioDataFimFechamentoPeriodoLetivo() {
		return !getProcessoMatriculaVO().getNivelProcessoMatricula().equals(TipoNivelEducacional.POS_GRADUACAO.getValor()) && !getProcessoMatriculaVO().getNivelProcessoMatricula().equals(TipoNivelEducacional.EXTENSAO.getValor());
	}

	public boolean isReabrirPeriodoLetivo() {
		return this.situacao.equals("FI");
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
	
	public Boolean getAnual() {
		return getTipoPeriodoLetivo().equals(PeriodicidadeEnum.ANUAL.getValor());
	}
	
	public Boolean getSemestral() {
		return getTipoPeriodoLetivo().equals(PeriodicidadeEnum.SEMESTRAL.getValor());
	}
	
	public Boolean getIntegral() {
		return getTipoPeriodoLetivo().equals(PeriodicidadeEnum.INTEGRAL.getValor());
	}
	
	 public UnidadeEnsinoCursoVO getUnidadeEnsinoCurso() {
	        if (unidadeEnsinoCurso == null) {
	            unidadeEnsinoCurso = new UnidadeEnsinoCursoVO();
	        }
	        return unidadeEnsinoCurso;
	    }

	    public void setUnidadeEnsinoCurso(UnidadeEnsinoCursoVO unidadeEnsinoCurso) {
	        this.unidadeEnsinoCurso = unidadeEnsinoCurso;
	    }
}
