package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.enumeradores.TipoControleGrupoOptativaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoDisciplina;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 * Reponsável por manter os dados da entidade PeriodoLetivo. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 */
@XmlRootElement(name = "periodoLetivoVO")
public class PeriodoLetivoVO extends SuperVO {

    private Integer codigo;
    private String descricao;
    /** Atributo responsável por manter os objetos da classe <code>GradeDisciplina</code>. */
    private List<GradeDisciplinaVO> gradeDisciplinaVOs;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Curso </code>.*/
    private Integer gradeCurricular;
    /** Atributo responsável por manter o objeto relacionado da classe <code>PeriodoLetivo </code>.*/
    private Integer periodoLetivo;
    private Boolean primeiro;
    private Integer totalCargaHoraria;
    private Integer totalCreditos;
    private String nomeCertificacao;
    public static final long serialVersionUID = 1L;
    /**
     * Este habilita o controle de disciplinas optativas por grupo de optativas.
     */
    private Boolean controleOptativaGrupo;
    private Integer numeroCreditoOptativa;
    private Integer numeroCargaHorariaOptativa;
    private TipoControleGrupoOptativaEnum tipoControleGrupoOptativa;
    private GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativa;
    private Integer numeroMaximoCreditoAlunoPodeCursar;
    private Integer numeroMaximoCargaHorariaAlunoPodeCursar;
    private Integer numeroMinimoCreditoAlunoPodeCursar;
    private Integer numeroMinimoCargaHorariaAlunoPodeCursar;
    private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
    
    /**
     * Atributo transiente
     */
    private Map<Integer, GradeDisciplinaVO> mapGradeDisciplina;

    /**
     * Construtor padrão da classe <code>PeriodoLetivo</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public PeriodoLetivoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>PeriodoLetivoVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     *                               o atributo e o erro ocorrido.
     */
    public static void validarDados(PeriodoLetivoVO obj) throws ConsistirException {
//        if ((obj.getCurso().intValue() == 0)) { 
//            throw new ConsistirException("O campo CURSO (Grade Curricular) deve ser informado.");
//        }
        if ((obj.getPeriodoLetivo().intValue() == 0)) {
            throw new ConsistirException("O campo PERÍODO LETIVO (Grade Curricular) deve ser informado.");
        }
        if (obj.getDescricao().trim().isEmpty()) {
            throw new ConsistirException("O campo DESCRIÇÃO (Período Letivo "+obj.getPeriodoLetivo()+") deve ser informado.");
        }
        if(!obj.getControleOptativaGrupo()){
        	obj.setNumeroCargaHorariaOptativa(0);
        	obj.setNumeroCreditoOptativa(0);
        }
        Uteis.checkState(Uteis.isAtributoPreenchido(obj.getNumeroMinimoCreditoAlunoPodeCursar()) && Uteis.isAtributoPreenchido(obj.getNumeroMaximoCreditoAlunoPodeCursar()) && obj.getNumeroMinimoCreditoAlunoPodeCursar() > obj.getNumeroMaximoCreditoAlunoPodeCursar(), "O campo Nr Min. Crédito Aluno não pode ser Maior que o campo Nr Máx. Crédito Aluno.");
        Uteis.checkState(Uteis.isAtributoPreenchido(obj.getNumeroMinimoCargaHorariaAlunoPodeCursar()) && Uteis.isAtributoPreenchido(obj.getNumeroMaximoCargaHorariaAlunoPodeCursar()) && obj.getNumeroMinimoCargaHorariaAlunoPodeCursar() > obj.getNumeroMaximoCargaHorariaAlunoPodeCursar(), "O campo Nr Min. C.H Aluno não pode ser Maior que o campo Nr Máx. C.H Aluno.");
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setDescricao("");
        setGradeCurricular(0);
        setPeriodoLetivo(0);
        setPrimeiro(Boolean.FALSE);
        setTotalCargaHoraria(0);
        setTotalCreditos(0);
        setNomeCertificacao("");
    }

//    public void verificarDisciplinaJaExistePeriodosAnteriores(PeriodoLetivoVO gradeCurricularVO ,GradeDisciplinaVO gradeDisciplinaVO) throws Exception {
//        PeriodoLetivoVO gradeCurricular = PeriodoLetivo.consultarGradeCurriccularPorDisciplinaPorCurso(gradeDisciplinaVO.getDisciplina().getCodigo(), gradeCurricularVO.getCurso().getCodigo());
//        if (gradeCurricular != null ){
//            PeriodoLetivoVO periodoLetivo = new PeriodoLetivoVO();
//            periodoLetivo = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(gradeCurricular.getPeriodoLetivo().getCodigo());
//            throw new ConsistirException("Essa disciplina já está adicionada no " + periodoLetivo.getSigla() + ".");
//        }
//    }
// Comentei AKI
//    public void verificarDisciplinaPossuiPreRequisitoMesmoPeriodo(PeriodoLetivoVO gradeCurricularVO ,GradeDisciplinaVO gradeDisciplinaVO) throws Exception {
//        List preRequisitos = (List)DisciplinaPreRequisito.consultarDisciplinaPreRequisitos(gradeDisciplinaVO.getDisciplina().getCodigo(), false);
//        Iterator i = preRequisitos.iterator();
//        while (i.hasNext()) {
//            DisciplinaPreRequisitoVO objExistente = (DisciplinaPreRequisitoVO)i.next();
//            Iterator j = getGradeDisciplinaVOs().iterator();
//            while (j.hasNext()) {
//                GradeDisciplinaVO obj = (GradeDisciplinaVO)j.next();
//                if (objExistente.getDisciplina().getCodigo().intValue() == obj.getDisciplina().getCodigo().intValue()) {
//                    throw new ConsistirException("A disciplina pré-requisito (" + objExistente.getDisciplina().getNome() + ") não pode estar adicionada no mesmo período da disciplina (" + gradeDisciplinaVO.getDisciplina().getNome() + "). Ela deve estar adicionada em períodos anteriores.");
//                }
//            }
//        }
//        Iterator y = preRequisitos.iterator();
//        while (y.hasNext()) {
//            DisciplinaPreRequisitoVO objExistente = (DisciplinaPreRequisitoVO)y.next();
//            //verificarSeExistePreRequisitosPeriodosAnteriores(objExistente.getPreRequisito(), gradeCurricularVO.getPeriodoLetivo().getPeriodoLetivoAnterior().getCodigo(), gradeCurricularVO.getCurso());
//        }
//    }
// Ate AKI
//    public void verificarSeExistePreRequisitosPeriodosAnteriores(DisciplinaVO disciplina ,Integer periodoLetivoAnterior, CursoVO cursoVO) throws Exception   {    
//        GradeDisciplinaVO gradeDisciplinaVO = getFacadeFactory().getGradeDisciplinaFacade().consultarPorNomeDisciplina(disciplina.getNome(), cursoVO.getCodigo());
//        PeriodoLetivoVO periodoLetivoVO = new PeriodoLetivoVO();
//        if (gradeDisciplinaVO == null) {
//            if (periodoLetivoAnterior.intValue() != 0) {
//                periodoLetivoVO = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(periodoLetivoAnterior);
//            } 
//            if (periodoLetivoVO.getPeriodoLetivoAnterior().getCodigo().intValue() != 0) {
//                verificarSeExistePreRequisitosPeriodosAnteriores(disciplina, periodoLetivoVO.getPeriodoLetivoAnterior().getCodigo(),curso);
//            } else {
//                throw new ConsistirException("Essa disciplina possui pré-requisitos que não estão adicionadas nos períodos anteriores(" + disciplina.getNome() + ").");                
//            }
//        }
//    }
    /**
     * Operação responsável por adicionar um novo objeto da classe <code>GradeDisciplinaVO</code>
     * ao List <code>gradeDisciplinaVOs</code>. Utiliza o atributo padrão de consulta 
     * da classe <code>GradeDisciplina</code> - getDisciplina().getCodigo() - como identificador (key) do objeto no List.
     * @param obj    Objeto da classe <code>GradeDisciplinaVO</code> que será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjGradeDisciplinaVOs(GradeDisciplinaVO obj, String regime) throws Exception {
        GradeDisciplinaVO.validarDados(obj, regime, "");
        int index = 0;
        Iterator<GradeDisciplinaVO> i = getGradeDisciplinaVOs().iterator();
        while (i.hasNext()) {
            GradeDisciplinaVO objExistente = (GradeDisciplinaVO) i.next();
            if (objExistente.getDisciplina().getCodigo().equals(obj.getDisciplina().getCodigo())) {
                getGradeDisciplinaVOs().set(index, obj);
                return;
            }
            index++;
        }
        totalCargaHoraria = totalCargaHoraria + obj.getCargaHoraria();
        totalCreditos = totalCreditos + obj.getNrCreditos();
        getGradeDisciplinaVOs().add(obj);
    }

    /**
     * Operação responsável por consultar um objeto da classe <code>GradeDisciplinaVO</code>
     * no List <code>gradeDisciplinaVOs</code>. Utiliza o atributo padrão de consulta 
     * da classe <code>GradeDisciplina</code> - getDisciplina().getCodigo() - como identificador (key) do objeto no List.
     * @param disciplina  Parâmetro para localizar o objeto do List.
     */
    public GradeDisciplinaVO consultarObjGradeDisciplinaVO(Integer disciplina) throws Exception {
        Iterator<GradeDisciplinaVO> i = getGradeDisciplinaVOs().iterator();
        while (i.hasNext()) {
            GradeDisciplinaVO objExistente = (GradeDisciplinaVO) i.next();
            if (objExistente.getDisciplina().getCodigo().equals(disciplina)) {
                return objExistente;
            }
        }
        return null;
    }

    public Integer getTotalCargaHoraria() {
        return totalCargaHoraria;
    }

    public void setTotalCargaHoraria(Integer totalCargaHoraria) {
        this.totalCargaHoraria = totalCargaHoraria;
    }

    public Integer getGradeCurricular() {
        return gradeCurricular;
    }

    public void setGradeCurricular(Integer gradeCurricular) {
        this.gradeCurricular = gradeCurricular;
    }

    public Integer getPeriodoLetivo() {
        if (periodoLetivo == null) {
            periodoLetivo = 0;
        }
        return periodoLetivo;
    }

    public void setPeriodoLetivo(Integer periodoLetivo) {
        this.periodoLetivo = periodoLetivo;
    }

    public Boolean getPrimeiro() {
        if (getPeriodoLetivo().intValue() == 1) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public void setPrimeiro(Boolean primeiro) {
        this.primeiro = primeiro;
    }

    public JRDataSource getGradeDisciplinaVOsJRDataSource() {
        return new JRBeanArrayDataSource(gradeDisciplinaVOs.toArray());
    }

    /** Retorna Atributo responsável por manter os objetos da classe <code>GradeDisciplina</code>. */
    public List<GradeDisciplinaVO> getGradeDisciplinaVOs() {
        if (gradeDisciplinaVOs == null) {
            gradeDisciplinaVOs = new ArrayList<GradeDisciplinaVO>(0);
        }
        return (gradeDisciplinaVOs);
    }

    /** Define Atributo responsável por manter os objetos da classe <code>GradeDisciplina</code>. */
    public void setGradeDisciplinaVOs(List<GradeDisciplinaVO> gradeDisciplinaVOs) {
        this.gradeDisciplinaVOs = gradeDisciplinaVOs;
    }

    public String getDescricao() {
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getTotalCreditos() {
        if (totalCreditos == null) {
            totalCreditos = 0;
        }
        return totalCreditos;
    }

    public void setTotalCreditos(Integer totalCreditos) {
        this.totalCreditos = totalCreditos;
    }

    public void atualizarTotalCredito() throws Exception {
        this.setTotalCreditos(0);
        for (GradeDisciplinaVO gradeDesc : this.getGradeDisciplinaVOs()) {
            this.setTotalCreditos(this.getTotalCreditos() + gradeDesc.getNrCreditos());
        }
        if (this.getControleOptativaGrupo()) {
            this.setTotalCreditos(this.getTotalCreditos() + this.getNumeroCreditoOptativa());
        }
    }

    public void atualizarTotalCargaHoraria() throws Exception {
        this.setTotalCargaHoraria(0);
        for (GradeDisciplinaVO gradeDesc : this.getGradeDisciplinaVOs()) {
            this.setTotalCargaHoraria(this.getTotalCargaHoraria() + gradeDesc.getCargaHoraria());
        }
        if (this.getControleOptativaGrupo()) {
            this.setTotalCargaHoraria(this.getTotalCargaHoraria() + this.getNumeroCargaHorariaOptativa());
        }
    }

    public void setNomeCertificacao(String nomeCertificacao) {
        this.nomeCertificacao = nomeCertificacao;
    }

    public String getNomeCertificacao() {
        return nomeCertificacao;
    }

    public String getControleOptativaGrupo_Apresentar() {
        if (getControleOptativaGrupo()) {
            return "Sim";
        }
        return "Não";
    }

    public Boolean getControleOptativaGrupo() {
        if (controleOptativaGrupo == null) {
            controleOptativaGrupo = false;
        }
        return controleOptativaGrupo;
    }

    public void setControleOptativaGrupo(Boolean controleOptativaGrupo) {
        this.controleOptativaGrupo = controleOptativaGrupo;
    }

    public Integer getNumeroCreditoOptativa() {
        if (numeroCreditoOptativa == null) {
            numeroCreditoOptativa = 0;
        }
        return numeroCreditoOptativa;
    }

    public void setNumeroCreditoOptativa(Integer numeroCreditoOptativa) {
        this.numeroCreditoOptativa = numeroCreditoOptativa;
    }

    public Integer getNumeroCargaHorariaOptativa() {
        if (numeroCargaHorariaOptativa == null) {
            numeroCargaHorariaOptativa = 0;
        }
        return numeroCargaHorariaOptativa;
    }

    public void setNumeroCargaHorariaOptativa(Integer numeroCargaHorariaOptativa) {
        this.numeroCargaHorariaOptativa = numeroCargaHorariaOptativa;
    }

    public String getTipoControleGrupoOptativa_Apresentar() {
        return getTipoControleGrupoOptativa().getValorApresentar();
    }

    public boolean getTipoControleGrupoOptativaPorCredito() {
        if (getTipoControleGrupoOptativa().equals(TipoControleGrupoOptativaEnum.CREDITO)) {
            return true;
        }
        return false;
    }

    public boolean getTipoControleGrupoOptativaPorCargaHoraria() {
        if (getTipoControleGrupoOptativa().equals(TipoControleGrupoOptativaEnum.CARGA_HORARIA)) {
            return true;
        }
        return false;
    }

    public TipoControleGrupoOptativaEnum getTipoControleGrupoOptativa() {
        if (tipoControleGrupoOptativa == null) {
            tipoControleGrupoOptativa = TipoControleGrupoOptativaEnum.CREDITO;
        }
        return tipoControleGrupoOptativa;
    }

    public void setTipoControleGrupoOptativa(TipoControleGrupoOptativaEnum tipoControleGrupoOptativa) {
        this.tipoControleGrupoOptativa = tipoControleGrupoOptativa;
    }

    public GradeCurricularGrupoOptativaVO getGradeCurricularGrupoOptativa() {
        if (gradeCurricularGrupoOptativa == null) {
            gradeCurricularGrupoOptativa = new GradeCurricularGrupoOptativaVO();
        }
        return gradeCurricularGrupoOptativa;
    }

    public void setGradeCurricularGrupoOptativa(GradeCurricularGrupoOptativaVO gradeCurricularGrupoOptativa) {
        this.gradeCurricularGrupoOptativa = gradeCurricularGrupoOptativa;
    }
    
    

    public Integer getNumeroMinimoCreditoAlunoPodeCursar() {
		if (numeroMinimoCreditoAlunoPodeCursar == null) {
			numeroMinimoCreditoAlunoPodeCursar = 0;
		}
		return numeroMinimoCreditoAlunoPodeCursar;
	}

	public void setNumeroMinimoCreditoAlunoPodeCursar(Integer numeroMinimoCreditoAlunoPodeCursar) {
		this.numeroMinimoCreditoAlunoPodeCursar = numeroMinimoCreditoAlunoPodeCursar;
	}

	public Integer getNumeroMinimoCargaHorariaAlunoPodeCursar() {
		if (numeroMinimoCargaHorariaAlunoPodeCursar == null) {
			numeroMinimoCargaHorariaAlunoPodeCursar = 0;
		}
		return numeroMinimoCargaHorariaAlunoPodeCursar;
	}

	public void setNumeroMinimoCargaHorariaAlunoPodeCursar(Integer numeroMinimoCargaHorariaAlunoPodeCursar) {
		this.numeroMinimoCargaHorariaAlunoPodeCursar = numeroMinimoCargaHorariaAlunoPodeCursar;
	}

	/**
     * @return the numeroMaximoCreditoAlunoPodeCursar
     */
    public Integer getNumeroMaximoCreditoAlunoPodeCursar() {
        if (numeroMaximoCreditoAlunoPodeCursar == null) {
            numeroMaximoCreditoAlunoPodeCursar = 0;
        }
        return numeroMaximoCreditoAlunoPodeCursar;
    }

    /**
     * @param numeroMaximoCreditoAlunoPodeCursar the numeroMaximoCreditoAlunoPodeCursar to set
     */
    public void setNumeroMaximoCreditoAlunoPodeCursar(Integer numeroMaximoCreditoAlunoPodeCursar) {
        this.numeroMaximoCreditoAlunoPodeCursar = numeroMaximoCreditoAlunoPodeCursar;
    }

    /**
     * @return the numeroMaximoCargaHorariaAlunoPodeCursar
     */
    public Integer getNumeroMaximoCargaHorariaAlunoPodeCursar() {
        if (numeroMaximoCargaHorariaAlunoPodeCursar == null) {
            numeroMaximoCargaHorariaAlunoPodeCursar = 0;
        }
        return numeroMaximoCargaHorariaAlunoPodeCursar;
    }

    /**
     * @param numeroMaximoCargaHorariaAlunoPodeCursar the numeroMaximoCargaHorariaAlunoPodeCursar to set
     */
    public void setNumeroMaximoCargaHorariaAlunoPodeCursar(Integer numeroMaximoCargaHorariaAlunoPodeCursar) {
        this.numeroMaximoCargaHorariaAlunoPodeCursar = numeroMaximoCargaHorariaAlunoPodeCursar;
    }

    /**
     * Copia do objeto de periodoLetivoVO, somente com os dados principais
     * do mesmo. Utilizado para combobox.
     */
    public PeriodoLetivoVO getNovaInstanciaPeriodoLetivo() {
        PeriodoLetivoVO novaInstancia = new PeriodoLetivoVO();
        novaInstancia.setCodigo(this.getCodigo());
        novaInstancia.setDescricao(this.getDescricao());
        novaInstancia.setPeriodoLetivo(this.getPeriodoLetivo());
        return novaInstancia;
    }

    public Integer getTotalCargaHorariaPratica() {
        Integer totalCargaHorariaPratica = 0;
        for (GradeDisciplinaVO gradeDisciplinaVO : getGradeDisciplinaVOs()) {
            totalCargaHorariaPratica += gradeDisciplinaVO.getCargaHorariaPratica();
        }
        return totalCargaHorariaPratica;
    }

    public Integer getTotalCargaHorariaTeorica() {
        Integer totalCargaHorariaTeorica = 0;
        for (GradeDisciplinaVO gradeDisciplinaVO : getGradeDisciplinaVOs()) {
            totalCargaHorariaTeorica += gradeDisciplinaVO.getCargaHorariaTeorica();
        }
        return totalCargaHorariaTeorica;
    }

	public Integer getTotalCargaHorariaPraticaObrigatorio() {
		Integer totalCargaHorariaPratica = 0;
		for (GradeDisciplinaVO gradeDisciplinaVO : getGradeDisciplinaVOs()) {
			if (gradeDisciplinaVO.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OBRIGATORIA.getValor()) || gradeDisciplinaVO.getTipoDisciplina().equals(TipoDisciplina.OBRIGATORIA.getValor())) {
				totalCargaHorariaPratica += gradeDisciplinaVO.getCargaHorariaPratica();
			}
		}
		return totalCargaHorariaPratica;
	}

	public Integer getTotalCargaHorariaTeoricaObrigatorio() {
		Integer totalCargaHorariaTeorica = 0;
		for (GradeDisciplinaVO gradeDisciplinaVO : getGradeDisciplinaVOs()) {
			if (gradeDisciplinaVO.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OBRIGATORIA.getValor()) || gradeDisciplinaVO.getTipoDisciplina().equals(TipoDisciplina.OBRIGATORIA.getValor())) {
				totalCargaHorariaTeorica += gradeDisciplinaVO.getCargaHorariaTeorica();
			}
		}
		return totalCargaHorariaTeorica;
	}

	public Integer getTotalCargaHorariaPraticaOptativa() {
		Integer totalCargaHorariaPratica = 0;
		for (GradeDisciplinaVO gradeDisciplinaVO : getGradeDisciplinaVOs()) {
			if (gradeDisciplinaVO.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OPTATIVA.getValor()) || gradeDisciplinaVO.getTipoDisciplina().equals(TipoDisciplina.OPTATIVA.getValor())) {
				totalCargaHorariaPratica += gradeDisciplinaVO.getCargaHorariaPratica();
			}
		}
		return totalCargaHorariaPratica;
	}

	public Integer getTotalCargaHorariaTeoricaOptativa() {
		Integer totalCargaHorariaTeorica = 0;
		for (GradeDisciplinaVO gradeDisciplinaVO : getGradeDisciplinaVOs()) {
			if (gradeDisciplinaVO.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OPTATIVA.getValor()) || gradeDisciplinaVO.getTipoDisciplina().equals(TipoDisciplina.OPTATIVA.getValor())) {
				totalCargaHorariaTeorica += gradeDisciplinaVO.getCargaHorariaTeorica();
			}
		}
		return totalCargaHorariaTeorica;
	}

    public Integer getTotalCredito() {
        Integer total = 0;
        for (GradeDisciplinaVO gradeDisciplinaVO : getGradeDisciplinaVOs()) {
            total += gradeDisciplinaVO.getNrCreditos();
        }
        return total;
    }
    
    public Integer getTotalCreditoObrigatorio() {
    	Integer total = 0;
    	for (GradeDisciplinaVO gradeDisciplinaVO : getGradeDisciplinaVOs()) {
    		if (gradeDisciplinaVO.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OBRIGATORIA.getValor()) || gradeDisciplinaVO.getTipoDisciplina().equals(TipoDisciplina.OBRIGATORIA.getValor())) {
    			total += gradeDisciplinaVO.getNrCreditos();
    		}
    	}
    	return total;
    }
    
    public Integer getTotalCreditoOptativa() {
    	Integer total = 0;
    	for (GradeDisciplinaVO gradeDisciplinaVO : getGradeDisciplinaVOs()) {
    		if (gradeDisciplinaVO.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OPTATIVA.getValor()) || gradeDisciplinaVO.getTipoDisciplina().equals(TipoDisciplina.OPTATIVA.getValor())) {
    			total += gradeDisciplinaVO.getNrCreditos();
    		}
    	}
    	return total;
    }

	public Double getTotalCreditoFinanceiro() {
		Double total = 0.0;
		for (GradeDisciplinaVO gradeDisciplinaVO : getGradeDisciplinaVOs()) {
			total += gradeDisciplinaVO.getNrCreditoFinanceiro();
		}
		return total;
	}

	public Double getTotalCreditoFinanceiroObrigatorio() {
		Double total = 0.0;
		for (GradeDisciplinaVO gradeDisciplinaVO : getGradeDisciplinaVOs()) {
			if (gradeDisciplinaVO.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OBRIGATORIA.getValor()) || gradeDisciplinaVO.getTipoDisciplina().equals(TipoDisciplina.OBRIGATORIA.getValor())) {
				total += gradeDisciplinaVO.getNrCreditoFinanceiro();
			}
		}
		return total;
	}

	public Double getTotalCreditoFinanceiroOptativa() {
		Double total = 0.0;
		for (GradeDisciplinaVO gradeDisciplinaVO : getGradeDisciplinaVOs()) {
			if (gradeDisciplinaVO.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OPTATIVA.getValor()) || gradeDisciplinaVO.getTipoDisciplina().equals(TipoDisciplina.OPTATIVA.getValor())) {
				total += gradeDisciplinaVO.getNrCreditoFinanceiro();
			}
		}
		return total;
	}

    /**
     * Método responsável por percorrer todas as gradeDisciplinas deste periodo
     * e obter GradeDisciplina corresponde a informada como parametro
     * Utilizado: TransferenciaMatrizCurricular
     * @param gradeDisciplinaVO
     * @return 
     */
    public GradeDisciplinaVO obterGradeDisciplinaCorrespondente(Integer codigoDisciplina, Integer cargaHoraria) {
        for (GradeDisciplinaVO gradeDisciplinaVO : this.getGradeDisciplinaVOs()) {
            if ((gradeDisciplinaVO.getDisciplina().getCodigo().equals(codigoDisciplina))
                    && (gradeDisciplinaVO.getCargaHoraria().equals(cargaHoraria))) {
                // se trata-se da mesma disciplina (mesmo codigo) e a mesma carga horaria, entao encontramos a 
                // a GradeDisciplina correspondente
                return gradeDisciplinaVO;
            }
        }
        return null;
    }
    
    public GradeDisciplinaVO obterGradeDisciplinaCorrespondentePorCodigo(Integer codigoGradeDisciplina) {
        for (GradeDisciplinaVO gradeDisciplinaVO : this.getGradeDisciplinaVOs()) {
            if ((gradeDisciplinaVO.getCodigo().equals(codigoGradeDisciplina))) {
                return gradeDisciplinaVO;
            }
        }
        return null;
    }
    
    /**
     * Método responsável por percorrer todas as GradeCurricularGrupoOptativaDisciplinaVO deste periodo
     * e obter GradeCurricularGrupoOptativaDisciplinaVO corresponde a informada como parametro
     * Utilizado: TransferenciaMatrizCurricular
     * @param gradeDisciplinaVO
     * @return 
     */
    public GradeCurricularGrupoOptativaDisciplinaVO obterGradeCurricularGrupoOptativaCorrespondente(Integer codigoDisciplina, Integer cargaHoraria) {
        for (GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO : this.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()) {
            if ((gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo().equals(codigoDisciplina))
                    && (gradeCurricularGrupoOptativaDisciplinaVO.getCargaHoraria().equals(cargaHoraria))) {
                // se trata-se da mesma disciplina (mesmo codigo) e a mesma carga horaria, entao encontramos a 
                // a GradeCurricularGrupoOptativaVO correspondente
                return gradeCurricularGrupoOptativaDisciplinaVO;
            }
        }
        return null;
    }

	@Override
	public String toString() {
		return "PeriodoLetivoVO [codigo=" + codigo + ", descricao=" + descricao + ", gradeDisciplinaVOs=" + gradeDisciplinaVOs + ", gradeCurricular=" + gradeCurricular + ", periodoLetivo=" + periodoLetivo + ", primeiro=" + primeiro + ", totalCargaHoraria=" + totalCargaHoraria + ", totalCreditos=" + totalCreditos + ", nomeCertificacao=" + nomeCertificacao + ", controleOptativaGrupo=" + controleOptativaGrupo + ", numeroCreditoOptativa=" + numeroCreditoOptativa + ", numeroCargaHorariaOptativa=" + numeroCargaHorariaOptativa + ", tipoControleGrupoOptativa=" + tipoControleGrupoOptativa + ", gradeCurricularGrupoOptativa=" + gradeCurricularGrupoOptativa + ", numeroMaximoCreditoAlunoPodeCursar=" + numeroMaximoCreditoAlunoPodeCursar + ", numeroMaximoCargaHorariaAlunoPodeCursar=" + numeroMaximoCargaHorariaAlunoPodeCursar + "]";
	}
	
	public Integer getTamanhoLista() {
		return getGradeDisciplinaVOs().size();
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if(configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO =  new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}

	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
	}

	public List<GradeDisciplinaVO> obterInclusaoGradeDisciplina(PeriodoLetivoVO periodoLetivoAntigoVO) {
		List<GradeDisciplinaVO> listaInclusaoGradeDisciplinaVOs = null;
		
		for (GradeDisciplinaVO gradeDisciplinaVO : this.getGradeDisciplinaVOs()) {
			if(periodoLetivoAntigoVO.getGradeDisciplinaVOs().stream().noneMatch(p-> p.getDisciplina().getCodigo().equals(gradeDisciplinaVO.getDisciplina().getCodigo()))) {
				if (listaInclusaoGradeDisciplinaVOs == null) {
					listaInclusaoGradeDisciplinaVOs = new ArrayList<GradeDisciplinaVO>(0);
				}
				listaInclusaoGradeDisciplinaVOs.add(gradeDisciplinaVO);
			}
		}
		return listaInclusaoGradeDisciplinaVOs;
	}
	
	public String obterLogInclusaoGradeDisciplina(List<GradeDisciplinaVO> listaGradeDisciplinaAdicionadaVOs) {
		String alteracao = "";
		for (GradeDisciplinaVO gradeDisciplinaVO : listaGradeDisciplinaAdicionadaVOs) {
			alteracao += gradeDisciplinaVO.getDescricaoCodigoNomeDisciplina();
		}
		return alteracao;
	}
	
	public Map<Integer, GradeDisciplinaVO> getMapGradeDisciplina() {
		if (mapGradeDisciplina == null) {
			mapGradeDisciplina = new HashMap<>(0);
		}
		return mapGradeDisciplina;
	}
	
	public void setMapGradeDisciplina(Map<Integer, GradeDisciplinaVO> mapGradeDisciplina) {
		this.mapGradeDisciplina = mapGradeDisciplina;
	}
	
	public Integer getChDisciplinaObrigatoria(Boolean calcularEstagio) {
		Integer total = 0;
    	if (Uteis.isAtributoPreenchido(getGradeDisciplinaVOs())) {
    		Optional<Integer> carga = getGradeDisciplinaVOs().stream().filter(g -> (g.getTipoDisciplina().equals(TipoDisciplina.OBRIGATORIA.getValor()) || g.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OBRIGATORIA.getValor()))).map(g -> g.getCargaHoraria()).reduce(Integer::sum);
			total = carga.isPresent() ? carga.get() : 0;
			if (!calcularEstagio) {
				Optional<Integer> estagio = getGradeDisciplinaVOs().stream().filter(g -> (g.getTipoDisciplina().equals(TipoDisciplina.OBRIGATORIA.getValor()) || g.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OBRIGATORIA.getValor())) && g.getDisciplinaEstagio()).map(g -> g.getCargaHoraria()).reduce(Integer::sum);
				if (estagio.isPresent()) {
					total = total - estagio.get();
				}
			}
		}
    	return total;
    }
    
    public Integer getChDisciplinaObrigatoriaPratica(Boolean calcularEstagio) {
    	Integer total = 0;
    	if (Uteis.isAtributoPreenchido(getGradeDisciplinaVOs())) {
    		Optional<Integer> carga = getGradeDisciplinaVOs().stream().filter(g -> (g.getTipoDisciplina().equals(TipoDisciplina.OBRIGATORIA.getValor()) || g.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OBRIGATORIA.getValor())) && Uteis.isAtributoPreenchido(g.getCargaHorariaPratica())).map(g -> g.getCargaHorariaPratica()).reduce(Integer::sum);
    		total = carga.isPresent() ? carga.get() : 0;
    		if (!calcularEstagio) {
    			Optional<Integer> estagio = getGradeDisciplinaVOs().stream().filter(g -> (g.getTipoDisciplina().equals(TipoDisciplina.OBRIGATORIA.getValor()) || g.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OBRIGATORIA.getValor())) && Uteis.isAtributoPreenchido(g.getCargaHorariaPratica()) && g.getDisciplinaEstagio()).map(g -> g.getCargaHorariaPratica()).reduce(Integer::sum);
    			if (estagio.isPresent()) {
    				total = total - estagio.get();
    			}
    		}
    	}
    	return total;
    }
    
    public Integer getChDisciplinaObrigatoriaTeorica(Boolean calcularEstagio) {
    	Integer total = 0;
    	if (Uteis.isAtributoPreenchido(getGradeDisciplinaVOs())) {
    		Optional<Integer> carga = getGradeDisciplinaVOs().stream().filter(g -> (g.getTipoDisciplina().equals(TipoDisciplina.OBRIGATORIA.getValor()) || g.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OBRIGATORIA.getValor())) && Uteis.isAtributoPreenchido(g.getCargaHorariaTeorica())).map(g -> g.getCargaHorariaTeorica()).reduce(Integer::sum);
    		total = carga.isPresent() ? carga.get() : 0;
    		if (!calcularEstagio) {
    			Optional<Integer> estagio = getGradeDisciplinaVOs().stream().filter(g -> (g.getTipoDisciplina().equals(TipoDisciplina.OBRIGATORIA.getValor()) || g.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OBRIGATORIA.getValor())) && Uteis.isAtributoPreenchido(g.getCargaHorariaTeorica()) && g.getDisciplinaEstagio()).map(g -> g.getCargaHorariaTeorica()).reduce(Integer::sum);
    			if (estagio.isPresent()) {
    				total = total - estagio.get();
    			}
    		}
    	}
    	return total;
    }
    
    public Integer getChDisciplinaEstagio() {
    	Integer total = 0;
    	if (Uteis.isAtributoPreenchido(getGradeDisciplinaVOs())) {
    		Optional<Integer> carga = getGradeDisciplinaVOs().stream().filter(g -> g.getDisciplinaEstagio()).map(g -> g.getCargaHoraria()).reduce(Integer::sum);
    		total = carga.isPresent() ? carga.get() : 0;
    	}
    	return total;
    }
    
    public Integer getChDisciplinaOptativa(Boolean calcularEstagio) {
		Integer total = 0;
    	if (Uteis.isAtributoPreenchido(getGradeDisciplinaVOs())) {
    		Optional<Integer> carga = getGradeDisciplinaVOs().stream().filter(g -> (g.getTipoDisciplina().equals(TipoDisciplina.OPTATIVA.getValor()) || g.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OPTATIVA.getValor()))).map(g -> g.getCargaHoraria()).reduce(Integer::sum);
			total = carga.isPresent() ? carga.get() : 0;
			if (!calcularEstagio) {
				Optional<Integer> estagio = getGradeDisciplinaVOs().stream().filter(g -> (g.getTipoDisciplina().equals(TipoDisciplina.OPTATIVA.getValor()) || g.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OPTATIVA.getValor())) && g.getDisciplinaEstagio()).map(g -> g.getCargaHoraria()).reduce(Integer::sum);
				if (estagio.isPresent()) {
					total = total - estagio.get();
				}
			}
		}
    	return total;
    }
    
    public Integer getChDisciplinaOptativaPratica(Boolean calcularEstagio) {
    	Integer total = 0;
    	if (Uteis.isAtributoPreenchido(getGradeDisciplinaVOs())) {
    		Optional<Integer> carga = getGradeDisciplinaVOs().stream().filter(g -> (g.getTipoDisciplina().equals(TipoDisciplina.OPTATIVA.getValor()) || g.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OPTATIVA.getValor()))).map(g -> g.getCargaHorariaPratica()).reduce(Integer::sum);
    		total = carga.isPresent() ? carga.get() : 0;
    		if (!calcularEstagio) {
    			Optional<Integer> estagio = getGradeDisciplinaVOs().stream().filter(g -> (g.getTipoDisciplina().equals(TipoDisciplina.OPTATIVA.getValor()) || g.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OPTATIVA.getValor())) && g.getDisciplinaEstagio()).map(g -> g.getCargaHorariaPratica()).reduce(Integer::sum);
    			if (estagio.isPresent()) {
    				total = total - estagio.get();
    			}
    		}
    	}
    	return total;
    }
    
    public Integer getChDisciplinaOptativaTeorica(Boolean calcularEstagio) {
    	Integer total = 0;
    	if (Uteis.isAtributoPreenchido(getGradeDisciplinaVOs())) {
    		Optional<Integer> carga = getGradeDisciplinaVOs().stream().filter(g -> (g.getTipoDisciplina().equals(TipoDisciplina.OPTATIVA.getValor()) || g.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OPTATIVA.getValor()))).map(g -> g.getCargaHorariaTeorica()).reduce(Integer::sum);
    		total = carga.isPresent() ? carga.get() : 0;
    		if (!calcularEstagio) {
    			Optional<Integer> estagio = getGradeDisciplinaVOs().stream().filter(g -> (g.getTipoDisciplina().equals(TipoDisciplina.OPTATIVA.getValor()) || g.getTipoDisciplina().equals(TipoDisciplina.LABORATORIAL_OPTATIVA.getValor())) && g.getDisciplinaEstagio()).map(g -> g.getCargaHorariaTeorica()).reduce(Integer::sum);
    			if (estagio.isPresent()) {
    				total = total - estagio.get();
    			}
    		}
    	}
    	return total;
    }
}
