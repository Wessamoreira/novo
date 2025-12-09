package negocio.comuns.academico;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.enumeradores.NomeTurnoCensoEnum;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.ControleGeracaoParcelaTurmaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import negocio.facade.jdbc.academico.ProcessoMatricula;

/**
 * Reponsável por manter os dados da entidade ProcessoMatriculaCalendario. Classe do tipo VO - Value Object composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a
 * estes atributos. Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see ProcessoMatricula
 */
public class ProcessoMatriculaCalendarioVO extends SuperVO implements Cloneable {

    private Integer processoMatricula;
    private Date dataInicioMatricula;
    private Date dataFinalMatricula;
    private Date dataInicioInclusaoDisciplina;
    private Date dataFinalInclusaoDisciplina;
    private Date dataInicioMatForaPrazo;
    private Date dataFinalMatForaPrazo;
    private Date dataVencimentoMatricula;
    private Date dataCompetenciaMatricula;
    private Date dataCompetenciaMensalidade;
    private Integer mesVencimentoPrimeiraMensalidade;
    private Integer diaVencimentoPrimeiraMensalidade;
    private Integer anoVencimentoPrimeiraMensalidade;
    private Boolean usarDataVencimentoDataMatricula;
    private Integer qtdeDiasAvancarDataVencimentoMatricula;
    private Boolean mesSubsequenteMatricula;
    private Boolean mesDataBaseGeracaoParcelas;
    private Boolean zerarValorDescontoPlanoFinanceiroAluno;
    private Boolean utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual;
    private Boolean utilizaControleGeracaoParcelaTurma;
    private Boolean considerarCompetenciaVctoMatricula;
    private Boolean considerarCompetenciaVctoMensalidade;
    private Boolean utilizarOrdemDescontoConfiguracaoFinanceira;
    private ControleGeracaoParcelaTurmaVO controleGeracaoParcelaTurma;
    private PoliticaDivulgacaoMatriculaOnlineVO politicaDivulgacaoMatriculaOnlineVO;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Curso </code>.
     */
//    private UnidadeEnsinoCursoVO unidadeEnsinoCurso;
    private CursoVO cursoVO;
    private TurnoVO turnoVO;
    private PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivolUnidadeEnsinoCursoVO;
    private DiaSemana diaSemanaAula;
    private NomeTurnoCensoEnum turnoAula;
    // Apenas usado para controle de tela.
    private Boolean possuiMatriculaVinculada;
    private Boolean atualizacaoIndividual;
    private Boolean acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno;
    public static final long serialVersionUID = 1L;
    /*
     * Transient
     */
    private List<SelectItem> listaSelectItemPoliticaDivulgacaoMatriculaVOs;

    /**
     * Construtor padrão da classe <code>ProcessoMatriculaCalendario</code>. Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public ProcessoMatriculaCalendarioVO() {
        super();
    }

    public ProcessoMatriculaCalendarioVO getClone() throws Exception {
        return (ProcessoMatriculaCalendarioVO) super.clone();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>ProcessoMatriculaCalendarioVO</code>. Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o erro ocorrido.
     */
    public static void validarDados(ProcessoMatriculaVO processoMatriculaVO, ProcessoMatriculaCalendarioVO obj) throws ConsistirException {
        if ((obj.getCursoVO() == null) || (obj.getCursoVO().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CURSO (Calendário Processo de Matricula Curso) deve ser informado.");
        }
        if ((obj.getTurnoVO() == null) || (obj.getTurnoVO().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TURNO (Calendário Processo de Matricula Curso) deve ser informado.");
        }
        String cursoTurno = "(Curso: "+obj.getCursoVO().getNome()+" e Turno: "+obj.getTurnoVO().getNome()+")";
        obj.getCursoVO().setDescricao(cursoTurno);
        // Todas as validações referente a Geração de Parcelas para Turma não precisam ser realizadas, caso
        // esteja-se utlizando um ControleGeracaoParcelaTurma já cadastrado. Pois este é validado no ato do seu cadastro
        if (!obj.getUtilizaControleGeracaoParcelaTurma()) {

            // A data de vencimento da matrícula somente é validado se o campo que indica se o vencimento será o próprio
            // dia da matrícula estiver desmarcado.
            if (!obj.getUsarDataVencimentoDataMatricula()) {
                if (obj.getDataVencimentoMatricula() == null) {
                    throw new ConsistirException("O campo DATA VENCIMENTO MATRÍCULA "+cursoTurno+" deve ser informado "+cursoTurno+".");
                }
                if(Uteis.getAnoData(obj.getDataVencimentoMatricula()) < 1997){
                	throw new ConsistirException("O campo ANO  da DATA DE VENCIMENTO 1ª MENSALIDADE "+cursoTurno+" deve ser maior que 1997.");
                }
            }


            // O mês e ano da primeira mensalidade somente será validado se o campo que indica se a primeira parcela da matrícula
            // será jogada para o mês subsequente da matrícula.
            if ((!obj.getMesSubsequenteMatricula()) && (!obj.getMesDataBaseGeracaoParcelas())) {
                if (obj.getMesVencimentoPrimeiraMensalidade() == null || obj.getMesVencimentoPrimeiraMensalidade().intValue() == 0) {
                    throw new ConsistirException("O campo MÊS da DATA DE VENCIMENTO 1ª MENSALIDADE  "+cursoTurno+" deve ser informado .");
                }
                if(Integer.valueOf(obj.getMesVencimentoPrimeiraMensalidade()) < 1 || Integer.valueOf(obj.getMesVencimentoPrimeiraMensalidade()) > 12){
                	throw new ConsistirException("O campo MÊS  da DATA DE VENCIMENTO 1ª MENSALIDADE "+cursoTurno+" deve ser entre 01 e 12.");
                }
                if (obj.getAnoVencimentoPrimeiraMensalidade() == null || obj.getMesVencimentoPrimeiraMensalidade().intValue() == 0) {
                    throw new ConsistirException("O campo ANO da DATA DE VENCIMENTO DA 1ª MENSALIDADE "+cursoTurno+" deve ser informado.");
                }
                if(Integer.valueOf(obj.getAnoVencimentoPrimeiraMensalidade()) < 1997){
                	throw new ConsistirException("O campo ANO  da DATA DE VENCIMENTO 1ª MENSALIDADE "+cursoTurno+" deve ser maior que 1997.");
                }
            }

            if (!obj.getMesDataBaseGeracaoParcelas()) {
                if (obj.getDiaVencimentoPrimeiraMensalidade() == null) {
                    throw new ConsistirException("O campo DIA DE VENCIMENTO PRIMEIRA MENSALIDADE "+cursoTurno+" deve ser informado.");
                }
                if(Integer.valueOf(obj.getDiaVencimentoPrimeiraMensalidade())<1 || Integer.valueOf(obj.getDiaVencimentoPrimeiraMensalidade())>31){
               	 throw new ConsistirException("O campo DIA DE VENCIMENTO PRIMEIRA MENSALIDADE "+cursoTurno+" deve ser informado entre 01 e 31.");
               }
            }
            if ((!obj.getMesSubsequenteMatricula()) && !obj.getMesDataBaseGeracaoParcelas()) {
            	try {
    				Date dataBase = Uteis.getData(obj.getDiaVencimentoPrimeiraMensalidade()+"/"+obj.getMesVencimentoPrimeiraMensalidade().toString()+"/"+obj.getAnoVencimentoPrimeiraMensalidade(), "dd/MM/yyyy");
    				if(dataBase.compareTo(new GregorianCalendar(1997, Calendar.OCTOBER, 7, 0, 0).getTime()) < 0){
    					throw new ConsistirException("O campo DATA DE VENCIMENTO PRIMEIRA MENSALIDADE "+cursoTurno+" deve ser maior que 07/10/1997.");
    				}
    			} catch (ParseException e) {				
    				throw new ConsistirException("O campo DATA DE VENCIMENTO PRIMEIRA MENSALIDADE "+cursoTurno+" é inválido.");
    			}
            }
        } else {
            if (obj.getControleGeracaoParcelaTurma().getCodigo().equals(0)) {
                throw new ConsistirException("É necessário informar o Controle de Geração de Parcelas Turma "+cursoTurno+".");
            }
        }

        // Validação Data Matrícula
        if (!processoMatriculaVO.getNivelProcessoMatricula().equals("PO") && !processoMatriculaVO.getNivelProcessoMatricula().equals("EX")) {
        	if ((obj.getDataInicioMatricula() == null)) {
                throw new ConsistirException("No curso " + obj.getCursoVO().getDescricao()
                        + " o  campo DATA INÍCIO MATRÍCULA (Calendário Processo de Matricula Curso) deve ser informado.");
            }
            if ((obj.getDataFinalMatricula() == null)) {
                throw new ConsistirException("No curso " + obj.getCursoVO().getDescricao()
                        + " o  campo DATA FINAL MATRÍCULA (Calendário Processo de Matricula Curso) deve ser informado.");
            }
        	if (Uteis.isAtributoPreenchido(obj.getDataInicioMatForaPrazo()) && !Uteis.isAtributoPreenchido(obj.getDataFinalMatForaPrazo())) {
        		throw new ConsistirException("No curso " + obj.getCursoVO().getDescricao()
        				+ " o campo DATA FINAL MATRÍCULA FORA PRAZO deve ser informado pois foi preenchido o campo DATA INÍCIO MATRÍCULA FORA PRAZO.");
        	}
        	if (Uteis.isAtributoPreenchido(obj.getDataFinalMatForaPrazo()) && !Uteis.isAtributoPreenchido(obj.getDataInicioMatForaPrazo())) {
        		throw new ConsistirException("No curso " + obj.getCursoVO().getDescricao()
        				+ " o campo DATA INÍCIO MATRÍCULA FORA PRAZO deve ser informado pois foi preenchido o campo DATA FINAL MATRÍCULA FORA PRAZO.");
        	}
        	if(Uteis.isAtributoPreenchido(processoMatriculaVO.getDataInicio()) &&Uteis.getAnoData(processoMatriculaVO.getDataInicio()) < 999){
        		 throw new ConsistirException("No curso " + obj.getCursoVO().getDescricao()
                         + " o campo DATA INÍCIO deve possuir o ano com 4 dígitos.");
            }
        	if(Uteis.isAtributoPreenchido(obj.getDataInicioMatricula()) && Uteis.getAnoData(obj.getDataInicioMatricula()) < 999){
       		 throw new ConsistirException("No curso " + obj.getCursoVO().getDescricao()
                        + " o campo DATA INÍCIO MATRÍCULA deve possuir o ano com 4 dígitos.");
           }
        	if(Uteis.isAtributoPreenchido(obj.getDataInicioInclusaoDisciplina()) && Uteis.getAnoData(obj.getDataInicioInclusaoDisciplina()) < 999){
          		 throw new ConsistirException("No curso " + obj.getCursoVO().getDescricao()
                           + " o campo DATA INÍCIO INCLUSÃO DISCIPLINA deve possuir o ano com 4 dígitos.");
             }
        	if(Uteis.isAtributoPreenchido(obj.getDataInicioMatForaPrazo()) && Uteis.getAnoData(obj.getDataInicioMatForaPrazo()) < 999){
         		 throw new ConsistirException("No curso " + obj.getCursoVO().getDescricao()
                          + " o campo DATA INÍCIO MATRÍCULA FORA DO PRAZO deve possuir o ano com 4 dígitos.");
            }
            if (Uteis.isAtributoPreenchido(obj.getDataInicioMatricula()) && Uteis.isAtributoPreenchido(obj.getDataFinalMatricula())
            		&& obj.getDataInicioMatricula().compareTo(obj.getDataFinalMatricula()) > 0) {
                throw new ConsistirException("No curso " + obj.getCursoVO().getDescricao()
                        + " o campo DATA INÍCIO MATRÍCULA não pode ser menor do que o campo DATA FINAL MATRÍCULA.");
            }
            if (Uteis.isAtributoPreenchido(obj.getDataInicioMatricula()) && Uteis.isAtributoPreenchido(processoMatriculaVO.getDataInicio()) 
            		&& obj.getDataInicioMatricula().compareTo(processoMatriculaVO.getDataInicio()) < 0) {
                throw new ConsistirException("No curso " + obj.getCursoVO().getDescricao()
                        + " o campo DATA INÍCIO MATRÍCULA do curso deve ser maior igual a DATA INÍCIO MATRÍCULA da Instituição.");
            }
            if (obj.getDataFinalMatricula().compareTo(processoMatriculaVO.getDataFinal()) > 0) {
                throw new ConsistirException("No curso "
                        + obj.getCursoVO().getDescricao()
                        + " o campo DATA FINAL MATRÍCULA do curso (Calendário Processo de Matricula Curso) não pode ser MAIOR que a DATA FINAL MATRÍCULA do Processo de Matrícula da Instituição (Dados Básicos).");
            }
            if ((obj.getDataInicioInclusaoDisciplina() == null)) {
                throw new ConsistirException("No curso " + obj.getCursoVO().getDescricao()
                        + " o  campo DATA INÍCIO INCLUSÃO DISCIPLINA (Calendário Processo de Matricula Curso) deve ser informado.");
            }
            if ((obj.getDataFinalInclusaoDisciplina() == null)) {
                throw new ConsistirException("No curso " + obj.getCursoVO().getDescricao()
                        + " o  campo DATA FINAL INCLUSÃO DISCIPLINA (Calendário Processo de Matricula Curso) deve ser informado.");
            }
            if (obj.getDataFinalInclusaoDisciplina().before(obj.getDataInicioInclusaoDisciplina())) {
                throw new ConsistirException("No curso "
                        + obj.getCursoVO().getDescricao()
                        + " o  campo DATA FINAL INCLUSÃO DISCIPLINA (Calendário Processo de Matricula Curso) deve ser maior que DATA INÍCIO INCLUSÃO DISCIPLINA (Calendário Processo de Matricula Curso).");
            }
            if (obj.getDataInicioInclusaoDisciplina().compareTo(processoMatriculaVO.getDataInicio()) < 0) {
                throw new ConsistirException("No curso " + obj.getCursoVO().getDescricao()
                        + " o campo DATA INÍCIO PERÍODO INCL. DISCIPLINAS do Curso deve ser maior ou ingual ao campo DATA INÍCIO MATRÍCULA da Instituição.");
            }
            if ((obj.getDataInicioMatForaPrazo() != null) && (obj.getDataFinalMatForaPrazo() != null)) {
                if (obj.getDataInicioMatForaPrazo().compareTo(obj.getDataFinalMatForaPrazo()) > 0) {
                    throw new ConsistirException("No curso "
                            + obj.getCursoVO().getDescricao()
                            + " o campo DATA INÍCIO MATRÍCULA FORA DO PRAZO (Calendário Processo de Matricula Curso) deve ser menor do que o campo DATA FINAL MATRÍCULA FORA DO PRAZO (Calendário Processo de Matricula Curso).");
                }
                if (obj.getDataInicioMatForaPrazo().compareTo(obj.getDataInicioMatricula()) < 0) {
                    throw new ConsistirException("No curso "
                            + obj.getCursoVO().getDescricao()
                            + " o campo DATA INÍCIO MATRÍCULA FORA DO PRAZO (Calendário Processo de Matricula Curso) não pode ser menor do que o campo DATA INÍCIO  PERÍODO MATRÍCULA (Calendário Processo de Matricula Curso).");
                }
            }
            if ((processoMatriculaVO.getDataInicio().compareTo(obj.getDataInicioMatricula()) > 0) || (processoMatriculaVO.getDataFinal().compareTo(obj.getDataInicioMatricula()) < 0)) {
                throw new ConsistirException("No curso " + obj.getCursoVO().getDescricao()
                        + " o  campo DATA INÍCIO MATRÍCULA (Calendário Processo de Matricula Curso) deve estar dentro do período de matrícula do PROCESSO SELETIVO.");
            }
            if ((processoMatriculaVO.getDataInicio().compareTo(obj.getDataFinalMatricula()) > 0) || (processoMatriculaVO.getDataFinal().compareTo(obj.getDataFinalMatricula()) < 0)) {
                throw new ConsistirException("No curso " + obj.getCursoVO().getDescricao()
                        + " o  campo DATA FINAL MATRÍCULA (Calendário Processo de Matricula Curso) deve estar dentro do período de matrícula do PROCESSO SELETIVO.");
            }
            if ((processoMatriculaVO.getDataInicio().compareTo(obj.getDataFinalMatricula()) > 0) || (processoMatriculaVO.getDataFinal().compareTo(obj.getDataFinalMatricula()) < 0)) {
                throw new ConsistirException("No curso " + obj.getCursoVO().getDescricao()
                        + " o  campo DATA FINAL MATRICULA (Calendário Processo de Matricula Curso) deve estar dentro do período de matrícula do PROCESSO SELETIVO.");
            }
            
        }

    }

    // Metodo validarPlanoFinanceiro que validaPeriodoLetivo ????
    public void validarCursoExistePlanoFinanceiro(UnidadeEnsinoCursoVO obj) throws Exception {
        if (obj.getPlanoFinanceiroCurso().getCodigo().intValue() != 0) {
            getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().setTipoPeriodoLetivo(obj.getCurso().getPeriodicidade());
        } else {
            throw new Exception("Não existe um PLANO FINANCEIRO para o curso" + obj.getCurso().getDescricao() + ".");
        }
    }

    public Boolean verificarProcessoMatriculaEstaAFrenteDataAtual(Date dataVerificar) throws Exception {
        dataVerificar = Uteis.getDateSemHora(dataVerificar);
        if (dataVerificar.compareTo(this.getDataInicioMatricula()) < 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean verificarDataEstaDentroPeriodoMatriculaValido(Date dataVerificar) throws Exception {
        Date dataSemHora = Uteis.getDateSemHora(dataVerificar);
        if (getDataInicioMatricula() == null && getDataFinalMatricula() == null) {
            return true;
        }
        if ((dataSemHora.compareTo(this.getDataInicioMatricula()) >= 0 || Uteis.getData(dataSemHora).equals(Uteis.getData(getDataInicioMatricula()))) 
        		&& (dataSemHora.compareTo(this.getDataFinalMatricula()) <= 0 ||  Uteis.getData(dataSemHora).equals(Uteis.getData(getDataFinalMatricula())))) {
            return true;
        } else {
            return false;
        }
    }

    public String getDescricaoPeriodoInclusaoExclusaoDisciplinas() throws ConsistirException {
        return this.getDataInicioInclusaoDisciplina_Apresentar() + " até " + this.getDataFinalInclusaoDisciplina_Apresentar();
    }

    public Boolean verificarDataEstaDentroPeriodoInclusaoExclusaoValido(Date dataVerificar) throws Exception {
        dataVerificar = Uteis.getDateSemHora(dataVerificar);
        if (this.getDataInicioInclusaoDisciplina() == null && this.getDataFinalInclusaoDisciplina() == null && getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getTipoPeriodoLetivo().equals("IN")) {
            return true;
        }
        if (Uteis.isAtributoPreenchido(this.getDataInicioInclusaoDisciplina()) && (dataVerificar.compareTo(this.getDataInicioInclusaoDisciplina()) >= 0) && (dataVerificar.compareTo(this.getDataFinalInclusaoDisciplina()) <= 0)) {
            return true;
        } else {
            return false;
        }
    }

    public void validarCursoExisteGradeCurricularAtiva(GradeCurricularVO gradeCurricularCurso) throws Exception {
        if (gradeCurricularCurso.getCodigo().intValue() == 0) {
            throw new Exception("Não existe uma GRADE CURRICULAR ATIVA para este curso.");
        }
    }

    public Integer getAnoVencimentoPrimeiraMensalidade() {
        return anoVencimentoPrimeiraMensalidade;
    }

    public void setAnoVencimentoPrimeiraMensalidade(Integer anoVencimentoPrimeiraMensalidade) {
        this.anoVencimentoPrimeiraMensalidade = anoVencimentoPrimeiraMensalidade;
    }

    public Integer getDiaVencimentoPrimeiraMensalidade() {
        return diaVencimentoPrimeiraMensalidade;
    }

    public void setDiaVencimentoPrimeiraMensalidade(Integer diaVencimentoPrimeiraMensalidade) {
        this.diaVencimentoPrimeiraMensalidade = diaVencimentoPrimeiraMensalidade;
    }

//    public UnidadeEnsinoCursoVO getUnidadeEnsinoCurso() {
//        if (unidadeEnsinoCurso == null) {
//            unidadeEnsinoCurso = new UnidadeEnsinoCursoVO();
//        }
//        return unidadeEnsinoCurso;
//    }
//
//    public void setUnidadeEnsinoCurso(UnidadeEnsinoCursoVO unidadeEnsinoCurso) {
//        this.unidadeEnsinoCurso = unidadeEnsinoCurso;
//    }

    public Date getDataVencimentoMatricula() {
        return dataVencimentoMatricula;
    }

    public void setDataVencimentoMatricula(Date dataVencimentoMatricula) {
        this.dataVencimentoMatricula = dataVencimentoMatricula;
    }

    public Integer getMesVencimentoPrimeiraMensalidade() {
        return mesVencimentoPrimeiraMensalidade;
    }

    public void setMesVencimentoPrimeiraMensalidade(Integer mesVencimentoPrimeiraMensalidade) {
        this.mesVencimentoPrimeiraMensalidade = mesVencimentoPrimeiraMensalidade;
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com ( <code>ProcessoMatriculaCalendario</code>).
     */
    public Date getDataFinalMatForaPrazo() {
        return (dataFinalMatForaPrazo);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
     */
    public String getDataFinalMatForaPrazo_Apresentar() {
        return (Uteis.getData(dataFinalMatForaPrazo));
    }

    public void setDataFinalMatForaPrazo(Date dataFinalMatForaPrazo) {
        this.dataFinalMatForaPrazo = dataFinalMatForaPrazo;
    }

    public Date getDataInicioMatForaPrazo() {
        return (dataInicioMatForaPrazo);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
     */
    public String getDataInicioMatForaPrazo_Apresentar() {
        return (Uteis.getData(dataInicioMatForaPrazo));
    }

    public void setDataInicioMatForaPrazo(Date dataInicioMatForaPrazo) {
        this.dataInicioMatForaPrazo = dataInicioMatForaPrazo;
    }

    public Date getDataFinalInclusaoDisciplina() {
        return (dataFinalInclusaoDisciplina);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
     */
    public String getDataFinalInclusaoDisciplina_Apresentar() {
        return (Uteis.getData(dataFinalInclusaoDisciplina));
    }

    public void setDataFinalInclusaoDisciplina(Date dataFinalInclusaoDisciplina) {
        this.dataFinalInclusaoDisciplina = dataFinalInclusaoDisciplina;
    }

    public Date getDataInicioInclusaoDisciplina() {
        return (dataInicioInclusaoDisciplina);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
     */
    public String getDataInicioInclusaoDisciplina_Apresentar() {
        return (Uteis.getData(dataInicioInclusaoDisciplina));
    }

    public void setDataInicioInclusaoDisciplina(Date dataInicioInclusaoDisciplina) {
        this.dataInicioInclusaoDisciplina = dataInicioInclusaoDisciplina;
    }

    public Date getDataFinalMatricula() {
        return (dataFinalMatricula);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
     */
    public String getDataFinalMatricula_Apresentar() {
        return (Uteis.getData(dataFinalMatricula));
    }

    public void setDataFinalMatricula(Date dataFinalMatricula) {
        this.dataFinalMatricula = dataFinalMatricula;
    }

    public Date getDataInicioMatricula() {
        return (dataInicioMatricula);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
     */
    public String getDataInicioMatricula_Apresentar() {
        return (Uteis.getData(dataInicioMatricula));
    }

    public void setDataInicioMatricula(Date dataInicioMatricula) {
        this.dataInicioMatricula = dataInicioMatricula;
    }

    public Integer getProcessoMatricula() {
        return (processoMatricula);
    }

    public void setProcessoMatricula(Integer processoMatricula) {
        this.processoMatricula = processoMatricula;
    }

    public PeriodoLetivoAtivoUnidadeEnsinoCursoVO getPeriodoLetivoAtivolUnidadeEnsinoCursoVO() {
        if (periodoLetivoAtivolUnidadeEnsinoCursoVO == null) {
            periodoLetivoAtivolUnidadeEnsinoCursoVO = new PeriodoLetivoAtivoUnidadeEnsinoCursoVO();
        }
        return periodoLetivoAtivolUnidadeEnsinoCursoVO;
    }

    public void setPeriodoLetivoAtivolUnidadeEnsinoCursoVO(PeriodoLetivoAtivoUnidadeEnsinoCursoVO periodoLetivoAtivolUnidadeEnsinoCursoVO) {
        this.periodoLetivoAtivolUnidadeEnsinoCursoVO = periodoLetivoAtivolUnidadeEnsinoCursoVO;
    }

    /**
     * @return the usarDataVencimentoDataMatricula
     */
    public Boolean getUsarDataVencimentoDataMatricula() {
        if (usarDataVencimentoDataMatricula == null) {
            usarDataVencimentoDataMatricula = Boolean.FALSE;
        }
        return usarDataVencimentoDataMatricula;
    }

    /**
     * @param usarDataVencimentoDataMatricula
     *            the usarDataVencimentoDataMatricula to set
     */
    public void setUsarDataVencimentoDataMatricula(Boolean usarDataVencimentoDataMatricula) {
        this.usarDataVencimentoDataMatricula = usarDataVencimentoDataMatricula;
    }

    /**
     * @return the mesSubsequenteMatricula
     */
    public Boolean getMesSubsequenteMatricula() {
        if (mesSubsequenteMatricula == null) {
            mesSubsequenteMatricula = Boolean.FALSE;
        }
        return mesSubsequenteMatricula;
    }

    /**
     * @param mesSubsequenteMatricula
     *            the mesSubsequenteMatricula to set
     */
    public void setMesSubsequenteMatricula(Boolean mesSubsequenteMatricula) {
        this.mesSubsequenteMatricula = mesSubsequenteMatricula;
    }

    public ProcessoMatriculaCalendarioVO clone() throws CloneNotSupportedException {
        ProcessoMatriculaCalendarioVO obj = (ProcessoMatriculaCalendarioVO) super.clone();
        obj.setCursoVO(new CursoVO());
        obj.setTurnoVO(new TurnoVO());
        obj.setPeriodoLetivoAtivolUnidadeEnsinoCursoVO(obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().clone());
        return obj;
    }

    public Date getDataVencimentoPrimeiraMensalidade() throws ParseException {
        String dataVencimentoPrimeiraMensalidade = getDiaVencimentoPrimeiraMensalidade() + "/" + getMesVencimentoPrimeiraMensalidade() + "/" + getAnoVencimentoPrimeiraMensalidade();
        SimpleDateFormat formatador = new SimpleDateFormat("dd/MM/yyyy");
        return formatador.parse(dataVencimentoPrimeiraMensalidade);
    }

    /**
     * @return the mesDataBaseGeracaoParcelas
     */
    public Boolean getMesDataBaseGeracaoParcelas() {
        if (mesDataBaseGeracaoParcelas == null) {
            mesDataBaseGeracaoParcelas = Boolean.FALSE;
        }
        return mesDataBaseGeracaoParcelas;
    }

    /**
     * @param mesDataBaseGeracaoParcelas the mesDataBaseGeracaoParcelas to set
     */
    public void setMesDataBaseGeracaoParcelas(Boolean mesDataBaseGeracaoParcelas) {
        this.mesDataBaseGeracaoParcelas = mesDataBaseGeracaoParcelas;
    }

    public Boolean getZerarValorDescontoPlanoFinanceiroAluno() {
        if (zerarValorDescontoPlanoFinanceiroAluno == null) {
            zerarValorDescontoPlanoFinanceiroAluno = Boolean.FALSE;
        }
        return zerarValorDescontoPlanoFinanceiroAluno;
    }

    public void setZerarValorDescontoPlanoFinanceiroAluno(
            Boolean zerarValorDescontoPlanoFinanceiroAluno) {
        this.zerarValorDescontoPlanoFinanceiroAluno = zerarValorDescontoPlanoFinanceiroAluno;
    }

    public Boolean getUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual() {
        if (utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual == null) {
            utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual = Boolean.FALSE;
        }
        return utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual;
    }

    public void setUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual(
            Boolean utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual) {
        this.utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual = utilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual;
    }

    public Integer getQtdeDiasAvancarDataVencimentoMatricula() {
        if (qtdeDiasAvancarDataVencimentoMatricula == null) {
            qtdeDiasAvancarDataVencimentoMatricula = 0;
        }
        return qtdeDiasAvancarDataVencimentoMatricula;
    }

    public void setQtdeDiasAvancarDataVencimentoMatricula(Integer qtdeDiasAvancarDataVencimentoMatricula) {
        this.qtdeDiasAvancarDataVencimentoMatricula = qtdeDiasAvancarDataVencimentoMatricula;
    }

    /**
     * @return the utilizaControleGeracaoParcelaTurma
     */
    public Boolean getUtilizaControleGeracaoParcelaTurma() {
        if (utilizaControleGeracaoParcelaTurma == null) {
            utilizaControleGeracaoParcelaTurma = Boolean.TRUE;
        }
        return utilizaControleGeracaoParcelaTurma;
    }

    /**
     * @param utilizaControleGeracaoParcelaTurma the utilizaControleGeracaoParcelaTurma to set
     */
    public void setUtilizaControleGeracaoParcelaTurma(Boolean utilizaControleGeracaoParcelaTurma) {
        this.utilizaControleGeracaoParcelaTurma = utilizaControleGeracaoParcelaTurma;
    }

    /**
     * @return the controleGeracaoParcelaTurma
     */
    public ControleGeracaoParcelaTurmaVO getControleGeracaoParcelaTurma() {
        if (controleGeracaoParcelaTurma == null) {
            controleGeracaoParcelaTurma = new ControleGeracaoParcelaTurmaVO();
        }
        return controleGeracaoParcelaTurma;
    }

    /**
     * @param controleGeracaoParcelaTurma the controleGeracaoParcelaTurma to set
     */
    public void setControleGeracaoParcelaTurma(ControleGeracaoParcelaTurmaVO controleGeracaoParcelaTurma) {
        this.controleGeracaoParcelaTurma = controleGeracaoParcelaTurma;
    }
    
    public void inicializarDadosComBaseControleGeracaoParcelaTurmaVO(ControleGeracaoParcelaTurmaVO controle) {
        this.setMesVencimentoPrimeiraMensalidade(controle.getMesVencimentoPrimeiraMensalidade());
        this.setDiaVencimentoPrimeiraMensalidade(controle.getDiaVencimentoPrimeiraMensalidade());
        this.setAnoVencimentoPrimeiraMensalidade(controle.getAnoVencimentoPrimeiraMensalidade());
        this.setQtdeDiasAvancarDataVencimentoMatricula(controle.getQtdeDiasAvancarDataVencimentoMatricula());
        this.setDataVencimentoMatricula(controle.getDataVencimentoMatricula());
        this.setUsarDataVencimentoDataMatricula(controle.getUsarDataVencimentoDataMatricula());
        this.setMesSubsequenteMatricula(controle.getMesSubsequenteMatricula());
        this.setMesDataBaseGeracaoParcelas(controle.getMesDataBaseGeracaoParcelas());
        this.setZerarValorDescontoPlanoFinanceiroAluno(controle.getZerarValorDescontoPlanoFinanceiroAluno());
        this.setUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual(controle.getUtilizarDescInstituicaoPlanoFinanceiroCursoConfiguracaoAtual());
        this.setAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno(controle.getAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno());
        this.setConsiderarCompetenciaVctoMatricula(controle.getConsiderarCompetenciaVctoMatricula());
        if (getConsiderarCompetenciaVctoMatricula()) {
            this.setDataCompetenciaMatricula(this.getDataVencimentoMatricula());
        }
        this.setConsiderarCompetenciaVctoMensalidade(controle.getConsiderarCompetenciaVctoMensalidade());
        this.setDataCompetenciaMensalidade(controle.getDataCompetenciaMensalidade());
        this.setUtilizarOrdemDescontoConfiguracaoFinanceira(controle.getUtilizarOrdemDescontoConfiguracaoFinanceira());
    }

    /**
     * @return the possuiMatriculaVinculada
     */
    public Boolean getPossuiMatriculaVinculada() {
        if (possuiMatriculaVinculada == null) {
            possuiMatriculaVinculada = Boolean.TRUE;
        }
        return possuiMatriculaVinculada;
    }

    /**
     * @param possuiMatriculaVinculada the possuiMatriculaVinculada to set
     */
    public void setPossuiMatriculaVinculada(Boolean possuiMatriculaVinculada) {
        this.possuiMatriculaVinculada = possuiMatriculaVinculada;
    }

	public Date getDataCompetenciaMatricula() {
		if (dataCompetenciaMatricula == null) {
			dataCompetenciaMatricula = new Date();
		}
		return dataCompetenciaMatricula;
	}

	public void setDataCompetenciaMatricula(Date dataCompetenciaMatricula) {
		this.dataCompetenciaMatricula = dataCompetenciaMatricula;
	}

	public Date getDataCompetenciaMensalidade() {
		if (dataCompetenciaMensalidade == null) {
			dataCompetenciaMensalidade = new Date();
		}
		return dataCompetenciaMensalidade;
	}

	public void setDataCompetenciaMensalidade(Date dataCompetenciaMensalidade) {
		this.dataCompetenciaMensalidade = dataCompetenciaMensalidade;
	}

	public Boolean getConsiderarCompetenciaVctoMatricula() {
		if (considerarCompetenciaVctoMatricula == null) {
			considerarCompetenciaVctoMatricula = Boolean.TRUE;
		}
		return considerarCompetenciaVctoMatricula;
	}

	public void setConsiderarCompetenciaVctoMatricula(Boolean considerarCompetenciaVctoMatricula) {
		this.considerarCompetenciaVctoMatricula = considerarCompetenciaVctoMatricula;
	}

	public Boolean getConsiderarCompetenciaVctoMensalidade() {
		if (considerarCompetenciaVctoMensalidade == null) {
			considerarCompetenciaVctoMensalidade = Boolean.TRUE;
		}
		return considerarCompetenciaVctoMensalidade;
	}

	public void setConsiderarCompetenciaVctoMensalidade(Boolean considerarCompetenciaVctoMensalidade) {
		this.considerarCompetenciaVctoMensalidade = considerarCompetenciaVctoMensalidade;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((processoMatricula == null) ? 0 : processoMatricula.hashCode());
		result = prime * result + ((cursoVO == null) ? 0 : cursoVO.getCodigo().hashCode());
		result = prime * result + ((turnoVO == null) ? 0 : turnoVO.getCodigo().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProcessoMatriculaCalendarioVO other = (ProcessoMatriculaCalendarioVO) obj;
		if (processoMatricula == null) {
			if (other.processoMatricula != null)
				return false;
		} else if (!processoMatricula.equals(other.processoMatricula))
			return false;
		if (cursoVO == null) {
			if (other.cursoVO != null)
				return false;
		} else if (!cursoVO.getCodigo().equals(other.cursoVO.getCodigo()))
			return false;
		if (turnoVO == null) {
			if (other.turnoVO != null)
				return false;
		} else if (!turnoVO.getCodigo().equals(other.turnoVO.getCodigo()))
			return false;
		return true;
	}

	public Boolean getUtilizarOrdemDescontoConfiguracaoFinanceira() {
		if (utilizarOrdemDescontoConfiguracaoFinanceira == null) {
			utilizarOrdemDescontoConfiguracaoFinanceira = false;
		}
		return utilizarOrdemDescontoConfiguracaoFinanceira;
	}

	public void setUtilizarOrdemDescontoConfiguracaoFinanceira(Boolean utilizarOrdemDescontoConfiguracaoFinanceira) {
		this.utilizarOrdemDescontoConfiguracaoFinanceira = utilizarOrdemDescontoConfiguracaoFinanceira;
	}

	public PoliticaDivulgacaoMatriculaOnlineVO getPoliticaDivulgacaoMatriculaOnlineVO() {
		if(politicaDivulgacaoMatriculaOnlineVO == null) {
			politicaDivulgacaoMatriculaOnlineVO = new PoliticaDivulgacaoMatriculaOnlineVO();
		}
		return politicaDivulgacaoMatriculaOnlineVO;
	}

	public void setPoliticaDivulgacaoMatriculaOnlineVO(PoliticaDivulgacaoMatriculaOnlineVO politicaDivulgacaoMatriculaOnlineVO) {
		this.politicaDivulgacaoMatriculaOnlineVO = politicaDivulgacaoMatriculaOnlineVO;
	}

	public List<SelectItem> getListaSelectItemPoliticaDivulgacaoMatriculaVOs() {
		if(listaSelectItemPoliticaDivulgacaoMatriculaVOs == null) {
			listaSelectItemPoliticaDivulgacaoMatriculaVOs = new ArrayList<SelectItem>();
		}
		return listaSelectItemPoliticaDivulgacaoMatriculaVOs;
	}

	public void setListaSelectItemPoliticaDivulgacaoMatriculaVOs(List<SelectItem> listaSelectItemPoliticaDivulgacaoMatriculaVOs) {
		this.listaSelectItemPoliticaDivulgacaoMatriculaVOs = listaSelectItemPoliticaDivulgacaoMatriculaVOs;
	}
	
	public void montarListaSelectItemPoliticaDivulgacaoMatriculaVOs(List<PoliticaDivulgacaoMatriculaOnlineVO> politicaDivulgacaoMatriculaOnlineVOs) {
		try {
			setListaSelectItemPoliticaDivulgacaoMatriculaVOs(UtilSelectItem.getListaSelectItem(politicaDivulgacaoMatriculaOnlineVOs, "codigo", "nome", true));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Boolean getAtualizacaoIndividual() {
		if (atualizacaoIndividual == null) {
			atualizacaoIndividual = Boolean.FALSE;
		}
		return atualizacaoIndividual;
	}

	public void setAtualizacaoIndividual(Boolean atualizacaoIndividual) {
		this.atualizacaoIndividual = atualizacaoIndividual;
	}
	
	public Boolean getAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno() {
		if (acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno == null) {
			acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno = Boolean.FALSE;
		}
		return acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno;
	}

	public void setAcrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno(Boolean acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno) {
		this.acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno = acrescentarDescInstPlanoFinanCursoRenovAlemExistPlanoFinanAluno;
	}
	
	public static void validarPeriodicidadeCursoTipoPeriodoLetivo(ProcessoMatriculaVO processoMatriculaVO, ProcessoMatriculaCalendarioVO obj) throws ConsistirException {
		if (!obj.getCursoVO().getPeriodicidade().equals(obj.getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getTipoPeriodoLetivo())) {
			throw new ConsistirException("O Tipo Período Letivo (Calendário Processo de Matricula Curso) é diferente da Periodicidade do (CURSO) Selecionado.");
		}
	}
	
	public Boolean verificarDataEstaDentroPeriodoMatriculaForaPrazoValido(Date dataVerificar) throws Exception {
		Date dataSemHora = Uteis.getDateSemHora(dataVerificar);
		if ((getDataInicioMatForaPrazo() == null && getDataFinalMatForaPrazo() != null) || (getDataInicioMatForaPrazo() != null && getDataFinalMatForaPrazo() == null)) {
			throw new ConsistirException("Período de matrícula fora do prazo está cadastrado INCORRETAMENTE. O período deve ser preenchido integralmente ou não informado.");
		}
		return getDataInicioMatForaPrazo() == null && getDataFinalMatForaPrazo() == null ? 
				false : dataSemHora.compareTo(this.getDataInicioMatForaPrazo()) >= 0 && dataSemHora.compareTo(this.getDataFinalMatForaPrazo()) <= 0;
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
		return getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getAnual();
	}
	
	public Boolean getSemestral() {
		return getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getSemestral();
	}
	
	public Boolean getIntegral() {
		return getPeriodoLetivoAtivolUnidadeEnsinoCursoVO().getIntegral();
	}

	public DiaSemana getDiaSemanaAula() {
		if(diaSemanaAula == null) {
			diaSemanaAula = DiaSemana.NENHUM; 
		}
		return diaSemanaAula;
	}

	public void setDiaSemanaAula(DiaSemana diaSemanaAula) {
		this.diaSemanaAula = diaSemanaAula;
	}

	public NomeTurnoCensoEnum getTurnoAula() {
		if(turnoAula == null) {
			turnoAula = NomeTurnoCensoEnum.NENHUM; 
		}
		return turnoAula;
	}

	public void setTurnoAula(NomeTurnoCensoEnum turnoAula) {
		this.turnoAula = turnoAula;
	}
	
	
}
