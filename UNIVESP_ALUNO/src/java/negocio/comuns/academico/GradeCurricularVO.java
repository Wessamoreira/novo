package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.academico.enumeradores.RegraContagemPeriodoLetivoEnum;
import negocio.comuns.academico.enumeradores.TipoControleComposicaoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.academico.Curso;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 * Reponsável por manter os dados da entidade GradeCurricular. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Curso
 */
@XmlRootElement(name = "gradeCurricularVO")
public class GradeCurricularVO extends SuperVO {

    private Integer codigo;
    private String nome;
    private Date dataCadastro;
    private String situacao;
    private Date dataFinalVigencia;
    private Date dataAtivacao;
    private Date dataDesativacao;
    private UsuarioVO responsavelAtivacao;
    private UsuarioVO responsavelDesativacao;
    private Integer curso;
    private List<PeriodoLetivoVO> periodoLetivosVOs;
    private Integer totalCargaHoraria;
    private Integer totalCreditos;
    private Integer cargaHoraria;
    private Integer creditos;
    private Integer totalCargaHorariaAtividadeComplementar;
    private Double percentualPermitirIniciarEstagio;
    private Integer totalCargaHorariaEstagio;
    private List<GradeCurricularTipoAtividadeComplementarVO> listaGradeCurricularTipoAtividadeComplementarVOs;
    private Double percentualPermitirIniciarTcc;
    public static final long serialVersionUID = 1L;
    private Integer totalDiaLetivoAno;
    private Integer totalSemanaLetivaAno;
    private List<GradeCurricularGrupoOptativaVO> gradeCurricularGrupoOptativaVOs;
    private String observacaoHistorico;
    private String competenciaProfissional;
    private String sistemaAvaliacao;
    private String resolucao;
    private Integer nrMesesConclusaoMatrizCurricular;
    private Integer quantidadeDisciplinasOptativasMatrizCurricular;
    private List<GradeCurricularEstagioVO> listaGradeCurricularEstagioVO;
    private DisciplinaVO disciplinaPadraoTcc;
    private Integer qtdeAnoSemestreParaIntegralizacaoCurso;
    private RegraContagemPeriodoLetivoEnum regraContagemPeriodoLetivoEnum;
    private boolean considerarPeriodoTrancadoParaJubilamento = false;

    /**
     * Utilizado apenas para a geracao do relatorio DisciplinasGradeRel Layout 1
     */
    private String anoSemestreDataAtivacao;
    private String anoSemestreDataFinalVigencia;
    private CursoVO cursoVO;
    private String habilitacao;
    
 // Transient, não persiste no banco, apenas para exibição na Matriz Curricular
    private Integer totalCargaHorariaDisciplinasObrigatorias;
    private GradeCurricularVO gradeCurricularOriginalVO;

    private String prazojubilamento;
    private Boolean existeGradeCurricularEstagio;
    
    /**
     * Construtor padrão da classe <code>GradeCurricular</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public GradeCurricularVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>GradeCurricularVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     * 
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDadosAtivarGrade(GradeCurricularVO grade) throws ConsistirException {
        // if (regime.equals("SE")) {
        // if (!cargaHoraria.equals(cargaHorariaAdicionada)) {
        // throw new ConsistirException("A carga horária da Grade Curricular ("
        // + cargaHorariaAdicionada
        // + ") está incompatível com a carga horária do Curso (" +
        // String.valueOf(cargaHoraria) + ")");
        // }
        // }
        //
        // if (regime.equals("CR")) {
        // if (!nrCreditos.equals(nrCreditosAdicionados)) {
        // throw new
        // ConsistirException("O Número de crédito da Grade Curricular (" +
        // nrCreditosAdicionados
        // + ") está incompatível com o número de crédito do Curso (" +
        // String.valueOf(nrCreditos) + ")");
        // }
        // }
        if (grade.getListaGradeCurricularTipoAtividadeComplementarVOs().isEmpty() && grade.getTotalCargaHorariaAtividadeComplementar() > 0) {
            throw new ConsistirException("Deve ser informado os tipos de ATIVIDADES COMPLEMENTARES para compor as horas de atividades cadastradas.");
        }
        if (grade.getPeriodoLetivosVOs().isEmpty()) {
        	throw new ConsistirException("A Matriz Curricular deve possuir pelo menos um PERÍODO LETIVO.");
        }
        Map<Integer, DisciplinaVO> mapDisciplina = new HashMap<Integer, DisciplinaVO>(0);
        for (PeriodoLetivoVO obj : grade.getPeriodoLetivosVOs()) {        	
            if (obj.getGradeDisciplinaVOs().isEmpty()) {
                throw new ConsistirException("Deve ser informado pelo menos uma DISCIPLINA no PERÍODO LETIVO - " + obj.getDescricao() + ".");
            }
            for(GradeDisciplinaVO gradeDisciplinaVO: obj.getGradeDisciplinaVOs()){
            	gradeDisciplinaVO.setPeriodoLetivoVO(obj);
            	GradeDisciplinaVO.validarDados(gradeDisciplinaVO, "CH", grade.getSituacao());
            	if(mapDisciplina.containsKey(gradeDisciplinaVO.getDisciplina().getCodigo())){
            		throw new ConsistirException("A DISCIPLINA "+gradeDisciplinaVO.getDisciplina().getCodigo()+" - "+gradeDisciplinaVO.getDisciplina().getNome()+" do PERÍODO LETIVO "+obj.getDescricao()+" foi adicionada mais de uma vez na matriz curricular.");
            	}else{
            		mapDisciplina.put(gradeDisciplinaVO.getDisciplina().getCodigo(), gradeDisciplinaVO.getDisciplina());
            	}
            	if (gradeDisciplinaVO.getDisciplinaComposta()) {
	            	int totalCargaHorariaComposta = 0;
	    			int totalCreditosComposta = 0;
	            	for(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO: gradeDisciplinaVO.getGradeDisciplinaCompostaVOs()){
	            		totalCargaHorariaComposta += gradeDisciplinaCompostaVO.getCargaHoraria();
	    				totalCreditosComposta += gradeDisciplinaCompostaVO.getNrCreditos();
	            		if(mapDisciplina.containsKey(gradeDisciplinaCompostaVO.getDisciplina().getCodigo())){
	                		throw new ConsistirException("A DISCIPLINA COMPOSTA "+gradeDisciplinaCompostaVO.getDisciplina().getCodigo()+" - "+gradeDisciplinaCompostaVO.getDisciplina().getNome()+" da DISCIPLINA "+gradeDisciplinaVO.getDisciplina().getCodigo()+" - "+gradeDisciplinaVO.getDisciplina().getNome()+"  do PERÍODO LETIVO "+obj.getDescricao()+" foi adicionada mais de uma vez na matriz curricular.");
	                	}else{
	                		mapDisciplina.put(gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), gradeDisciplinaCompostaVO.getDisciplina());
	                	}
	            	}
	            	if (!gradeDisciplinaVO.getGradeDisciplinaCompostaVOs().isEmpty() && gradeDisciplinaVO.getTipoControleComposicao().equals(TipoControleComposicaoEnum.ESTUDAR_TODAS_COMPOSTAS)) {
	            		if (totalCargaHorariaComposta > gradeDisciplinaVO.getCargaHoraria()) {
	            			throw new ConsistirException("A DISCIPLINA "+gradeDisciplinaVO.getDisciplina().getCodigo()+" - "+gradeDisciplinaVO.getDisciplina().getNome()+" do PERÍODO LETIVO "+obj.getDescricao()+" é uma disciplina composta e a carga horária total da composição deve ser inferior ou igual a carga horária da disciplina principal.");
	        			}
	            		if (totalCreditosComposta > gradeDisciplinaVO.getNrCreditos()) {
	            			throw new ConsistirException("A DISCIPLINA "+gradeDisciplinaVO.getDisciplina().getCodigo()+" - "+gradeDisciplinaVO.getDisciplina().getNome()+" do PERÍODO LETIVO "+obj.getDescricao()+" é uma disciplina composta e o total de créditos da composição deve ser inferior ou igual aos créditos da disciplina principal.");
	        			}
	            	}
            	}            	            	            
            }
//			if (obj.getControleOptativaGrupo()) {
//				if (obj.getNumeroCargaHorariaOptativa().equals(0)) {
//					throw new ConsistirException("O campo CARGA HORÁRIA do Grupo Optativa no PERÍODO LETIVO " + obj.getDescricao() + " deve ser informado.");
//				}
//				if (obj.getNumeroCreditoOptativa().equals(0)) {
//					throw new ConsistirException("O campo NÚMERO CRÉDITO do Grupo Optativa no PERÍODO LETIVO " + obj.getDescricao() + " deve ser informado.");
//				}
//			}
        }
        
        if (!grade.getListaGradeCurricularTipoAtividadeComplementarVOs().isEmpty() && grade.getTotalCargaHorariaAtividadeComplementar() <= 0) {
            throw new ConsistirException("O campo TOTAL DE HORAS DE ATIVIDADES COMPLEMENTARES deve ser informado.");
        }
        for (GradeCurricularGrupoOptativaVO obj : grade.getGradeCurricularGrupoOptativaVOs()) {
            if (obj.getGradeCurricularGrupoOptativaDisciplinaVOs().isEmpty()) {
                throw new ConsistirException("Deve ser informado pelo menos uma DISCIPLINA no GRUPO DE OPTATIVAS - " + obj.getDescricao() + ".");
            }
            for(GradeCurricularGrupoOptativaDisciplinaVO gradeDisciplinaCurricularGrupoOptativaDisciplinaVO: obj.getGradeCurricularGrupoOptativaDisciplinaVOs()){
            	if(mapDisciplina.containsKey(gradeDisciplinaCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo())){
            		throw new ConsistirException("A DISCIPLINA "+gradeDisciplinaCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo()+" - "+gradeDisciplinaCurricularGrupoOptativaDisciplinaVO.getDisciplina().getNome()+" do GRUPO DE OPTATIVA "+obj.getDescricao()+" foi adicionada mais de uma vez na matriz curricular.");
            	}else{
            		mapDisciplina.put(gradeDisciplinaCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo(), gradeDisciplinaCurricularGrupoOptativaDisciplinaVO.getDisciplina());
            	}
            	for(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO: gradeDisciplinaCurricularGrupoOptativaDisciplinaVO.getGradeDisciplinaCompostaVOs()){
            		if(mapDisciplina.containsKey(gradeDisciplinaCompostaVO.getDisciplina().getCodigo())){
                		throw new ConsistirException("A DISCIPLINA COMPOSTA "+gradeDisciplinaCompostaVO.getDisciplina().getCodigo()+" - "+gradeDisciplinaCompostaVO.getDisciplina().getNome()+" da DISCIPLINA "+gradeDisciplinaCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo()+" - "+gradeDisciplinaCurricularGrupoOptativaDisciplinaVO.getDisciplina().getNome()+"  do GRUPO OPTATIVA "+obj.getDescricao()+" foi adicionada mais de uma vez na matriz curricular.");
                	}else{
                		mapDisciplina.put(gradeDisciplinaCompostaVO.getDisciplina().getCodigo(), gradeDisciplinaCompostaVO.getDisciplina());
                	}
            	}
            }
        }
    }

    public String validarTotalCargaHorariaCredito(Integer cargaHorariaCurso, Integer creditoCurso, String regime) {
        Iterator<PeriodoLetivoVO> i = getPeriodoLetivosVOs().iterator();
        int cargaHoraria = 0;
        int creditos = 0;
        int creditosOptativa = 0;
        int creditosGrupoOptativa = 0;
        int cargaHorariaDisciplinaOptativa = 0; 
        int cargaHorariaDisciplinaGrupoOptativa = 0;
        int totalCargaHorariaDisciplinasObrigatorias = 0;
        while (i.hasNext()) {
            PeriodoLetivoVO periodo = (PeriodoLetivoVO) i.next();
            cargaHoraria = cargaHoraria + periodo.getTotalCargaHorariaPraticaObrigatorio() + periodo.getTotalCargaHorariaTeoricaObrigatorio();
            if(periodo.getControleOptativaGrupo()) {
            	cargaHorariaDisciplinaGrupoOptativa += periodo.getNumeroCargaHorariaOptativa();
            	creditosGrupoOptativa += periodo.getNumeroCreditoOptativa();
            }else {
            	cargaHorariaDisciplinaOptativa += periodo.getTotalCargaHorariaPraticaOptativa() + periodo.getTotalCargaHorariaTeoricaOptativa();
            	creditosOptativa += periodo.getTotalCreditoOptativa();
            }
            creditos = creditos + periodo.getTotalCreditoObrigatorio();
            for(GradeDisciplinaVO gradeDisciplinaVO: periodo.getGradeDisciplinaVOs()) {
				if(!gradeDisciplinaVO.getIsDisciplinaOptativa()) {
					totalCargaHorariaDisciplinasObrigatorias += gradeDisciplinaVO.getCargaHoraria();			
				}
			}
        }
        setTotalCargaHorariaDisciplinasObrigatorias(totalCargaHorariaDisciplinasObrigatorias);
        setTotalCargaHoraria(cargaHoraria + getTotalCargaHorariaAtividadeComplementar() + getTotalCargaHorariaEstagio() + cargaHorariaDisciplinaGrupoOptativa);
        setTotalCreditos(creditos+creditosGrupoOptativa);
        if (regime.equals("SE")) {
            if (getTotalCargaHoraria() > cargaHorariaCurso && (getTotalCargaHoraria() > cargaHorariaCurso+cargaHorariaDisciplinaOptativa)) {
                return ("A carga horária da Grade Curricular (" + String.valueOf(getTotalCargaHoraria()) + "H) ultrapassou a carga do Curso (" + String.valueOf(cargaHorariaCurso) + "H)");
            } else if (getTotalCargaHoraria() < cargaHorariaCurso) {
                return ("Faltam (" + String.valueOf(cargaHorariaCurso - getTotalCargaHoraria()) + "H) horas para completar a carga horária do Curso (" + String.valueOf(cargaHorariaCurso) + "H)");
            }
        } else if (regime.equals("CR")) {
            if (getTotalCreditos() > creditoCurso+creditosGrupoOptativa && getTotalCreditos() > (creditoCurso + creditosGrupoOptativa + creditosOptativa)) {
                return ("Os créditos da Grade Curricular (" + String.valueOf(getTotalCreditos()) + ") ultrapassaram os créditos do Curso (" + String.valueOf(creditoCurso) + ")");
            } else if (getTotalCreditos() < creditoCurso) {
                return ("Faltam (" + String.valueOf(creditoCurso - getTotalCreditos()) + ") créditos para completar os créditos do Curso (" + String.valueOf(creditoCurso) + ")");
            }
        }
        return "";
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNome("");
        setDataCadastro(new Date());
        setSituacao("CO");
        setTotalCargaHoraria(0);
        setTotalCreditos(0);
        setCargaHoraria(0);
        setCreditos(0);
    }

    public void adicionarObjPeriodoLetivoVOs(PeriodoLetivoVO obj) throws Exception {
        PeriodoLetivoVO.validarDados(obj);
        int index = 0;
        Iterator<PeriodoLetivoVO> i = getPeriodoLetivosVOs().iterator();
        while (i.hasNext()) {
            PeriodoLetivoVO objExistente = (PeriodoLetivoVO) i.next();
            if (objExistente.getPeriodoLetivo().equals(obj.getPeriodoLetivo())) {
                getPeriodoLetivosVOs().set(index, obj);
                return;
            }
            index++;
        }
        getPeriodoLetivosVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>GradeDisciplinaVO</code> no List <code>gradeDisciplinaVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>GradeDisciplina</code> - getDisciplina().getCodigo() - como
     * identificador (key) do objeto no List.
     * 
     * @param disciplina
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjPeriodoLetivoVOs(PeriodoLetivoVO periodoLetivo) throws Exception {
        int index = 0;
        Iterator<PeriodoLetivoVO> i = getPeriodoLetivosVOs().iterator();
        while (i.hasNext()) {
            PeriodoLetivoVO objExistente = (PeriodoLetivoVO) i.next();
            if (objExistente.getPeriodoLetivo().equals(periodoLetivo.getPeriodoLetivo())) {
                getPeriodoLetivosVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>GradeDisciplinaVO</code> no List <code>gradeDisciplinaVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>GradeDisciplina</code> - getDisciplina().getCodigo() - como
     * identificador (key) do objeto no List.
     * 
     * @param disciplina
     *            Parâmetro para localizar o objeto do List.
     */
    public PeriodoLetivoVO consultarObjPeriodoLetivoVO(Integer periodoLetivo) throws Exception {
        Iterator<PeriodoLetivoVO> i = getPeriodoLetivosVOs().iterator();
        while (i.hasNext()) {
            PeriodoLetivoVO objExistente = (PeriodoLetivoVO) i.next();
            if (objExistente.getPeriodoLetivo().equals(periodoLetivo)) {
                return objExistente;
            }
        }
        return null;
    }
    
    public GradeDisciplinaVO consultarObjGradeDisciplinaVOPorCodigoDisciplina(Integer codigoDisciplina) throws Exception {
        Iterator<PeriodoLetivoVO> i = getPeriodoLetivosVOs().iterator();
        while (i.hasNext()) {
            PeriodoLetivoVO objExistente = (PeriodoLetivoVO) i.next();
            for (GradeDisciplinaVO grade : objExistente.getGradeDisciplinaVOs()) {
                if (grade.getDisciplina().getCodigo().equals(codigoDisciplina)) {
                    return grade;
                }
            }
        }
        return null;
    }    
    
    public Integer obterNumeroProximoPeriodo(Integer atual) throws Exception {
        int proximo = atual.intValue() + 1;
        int maior = obterNumeroMaiorPeriodoLetivo();
        if (proximo > maior) {
            proximo = maior;
        }
        return proximo;
    }
    
    public Integer obterNumeroMaiorPeriodoLetivo() throws Exception {
        int maior = 0;
        Iterator<PeriodoLetivoVO> i = getPeriodoLetivosVOs().iterator();
        while (i.hasNext()) {
            PeriodoLetivoVO objExistente = (PeriodoLetivoVO) i.next();
            if (objExistente.getPeriodoLetivo().intValue() > maior) {
                maior = objExistente.getPeriodoLetivo().intValue();
            }
        }
        return maior;
    }
    
    public PeriodoLetivoVO obterPeriodoLetivoPorPeriodoLetivo(Integer periodoLetivo) throws Exception {
    	Iterator<PeriodoLetivoVO> i = getPeriodoLetivosVOs().iterator();
    	while (i.hasNext()) {
    		PeriodoLetivoVO objExistente = (PeriodoLetivoVO) i.next();
    		if (objExistente.getPeriodoLetivo().intValue()  == periodoLetivo) {
    			return objExistente;
    		}
    	}
    	return new PeriodoLetivoVO();
    }
    
    public PeriodoLetivoVO obterPeriodoLetivoPorCodigoPeriodoLetivo(Integer codigoPeriodoLetivo) throws Exception {
    	Iterator<PeriodoLetivoVO> i = getPeriodoLetivosVOs().iterator();
    	while (i.hasNext()) {
    		PeriodoLetivoVO objExistente = (PeriodoLetivoVO) i.next();
    		if (objExistente.getCodigo().equals(codigoPeriodoLetivo)) {
    			return objExistente;
    		}
    	}
    	return new PeriodoLetivoVO();
    }
    
    public PeriodoLetivoVO consultarObjPeriodoLetivoVOPorNumeroPeriodoLetivo(Integer periodoLetivo) throws Exception {
        Iterator<PeriodoLetivoVO> i = getPeriodoLetivosVOs().iterator();
        while (i.hasNext()) {
            PeriodoLetivoVO objExistente = (PeriodoLetivoVO) i.next();
            if (objExistente.getPeriodoLetivo().equals(periodoLetivo)) {
                return objExistente;
            }
        }
        return null;
    }

    public PeriodoLetivoVO consultarObjPeriodoLetivoVOPorCodigo(Integer codigo) throws Exception {
        Iterator<PeriodoLetivoVO> i = getPeriodoLetivosVOs().iterator();
        while (i.hasNext()) {
            PeriodoLetivoVO objExistente = (PeriodoLetivoVO) i.next();
            if (objExistente.getCodigo().equals(codigo)) {
                return objExistente;
            }
        }
        return null;
    }

    public Integer getCurso() {
        return (curso);
    }

    public void setCurso(Integer curso) {
        this.curso = curso;
    }

    public Date getDataFinalVigencia() {
        return (dataFinalVigencia);
    }

    public Integer getTotalCargaHoraria() {
        if (totalCargaHoraria == null) {
            totalCargaHoraria = 0;
        }
        return totalCargaHoraria;
    }

    public void setTotalCargaHoraria(Integer totalCargaHoraria) {
        this.totalCargaHoraria = totalCargaHoraria;
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFinalVigencia_Apresentar() {
        return (Uteis.getData(dataFinalVigencia));
    }

    public void setDataFinalVigencia(Date dataFinalVigencia) {
        this.dataFinalVigencia = dataFinalVigencia;
    }

    public String getSituacao() {
        return (situacao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getSituacao_Apresentar() {
        if (situacao.equals("DE")) {
            return "Defasada";
        }
        if (situacao.equals("IN")) {
            return "Inativa";
        }
        if (situacao.equals("CO")) {
            return "Construção";
        }
        if (situacao.equals("AT")) {
            return "Ativa";
        }
        return (situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Date getDataCadastro() {
        return (dataCadastro);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataCadastro_Apresentar() {
        return (Uteis.getData(dataCadastro));
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getNome() {
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public PeriodoLetivoVO getUltimoPeriodoLetivoGrade() {
        if ((periodoLetivosVOs == null) || (periodoLetivosVOs.size() == 0)) {
            return new PeriodoLetivoVO();
        }
        Ordenacao.ordenarLista(periodoLetivosVOs, "periodoLetivo");
        return (PeriodoLetivoVO) periodoLetivosVOs.get(periodoLetivosVOs.size() - 1);
    }

    public PeriodoLetivoVO getPrimeiroPeriodoLetivoGrade() {
        if ((periodoLetivosVOs == null) || (periodoLetivosVOs.size() == 0)) {
            return new PeriodoLetivoVO();
        }
        Ordenacao.ordenarLista(periodoLetivosVOs, "periodoLetivo");
        return (PeriodoLetivoVO) periodoLetivosVOs.get(0);
    }

    public JRDataSource getPeriodoLetivosVOsJRDataSource() {
        return new JRBeanArrayDataSource(periodoLetivosVOs.toArray());
    }

    public List<PeriodoLetivoVO> getPeriodoLetivosVOs() {
        if (periodoLetivosVOs == null) {
            periodoLetivosVOs = new ArrayList<PeriodoLetivoVO>(0);
        }
        return periodoLetivosVOs;
    }

    public void setPeriodoLetivosVOs(List<PeriodoLetivoVO> periodoLetivosVOs) {
        this.periodoLetivosVOs = periodoLetivosVOs;
    }

    public Date getDataAtivacao() {
        return dataAtivacao;
    }

    public void setDataAtivacao(Date dataAtivacao) {
        this.dataAtivacao = dataAtivacao;
    }

    public Date getDataDesativacao() {
        return dataDesativacao;
    }

    public void setDataDesativacao(Date dataDesativacao) {
        this.dataDesativacao = dataDesativacao;
    }

    public UsuarioVO getResponsavelAtivacao() {
        if (responsavelAtivacao == null) {
            responsavelAtivacao = new UsuarioVO();
        }
        return responsavelAtivacao;
    }

    public void setResponsavelAtivacao(UsuarioVO responsavelAtivacao) {
        this.responsavelAtivacao = responsavelAtivacao;
    }

    public UsuarioVO getResponsavelDesativacao() {
        if (responsavelDesativacao == null) {
            responsavelDesativacao = new UsuarioVO();
        }
        return responsavelDesativacao;
    }

    public void setResponsavelDesativacao(UsuarioVO responsavelDesativacao) {
        this.responsavelDesativacao = responsavelDesativacao;
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

    public Integer getCargaHoraria() {
    	if (cargaHoraria == null) {
    		cargaHoraria = 0;
    	}
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Integer getCreditos() {
    	if(creditos == null) {
    		creditos = 0;
    	}
    	
        return creditos;
    }

    public void setCreditos(Integer creditos) {
        this.creditos = creditos;
    }

    public List<GradeCurricularTipoAtividadeComplementarVO> getListaGradeCurricularTipoAtividadeComplementarVOs() {
        if (listaGradeCurricularTipoAtividadeComplementarVOs == null) {
            listaGradeCurricularTipoAtividadeComplementarVOs = new ArrayList<GradeCurricularTipoAtividadeComplementarVO>(0);
        }
        return listaGradeCurricularTipoAtividadeComplementarVOs;
    }

    public void setListaGradeCurricularTipoAtividadeComplementarVOs(List<GradeCurricularTipoAtividadeComplementarVO> listaGradeCurricularTipoAtividadeComplementarVOs) {
        this.listaGradeCurricularTipoAtividadeComplementarVOs = listaGradeCurricularTipoAtividadeComplementarVOs;
    }

    public Integer getTotalCargaHorariaAtividadeComplementar() {
        if (totalCargaHorariaAtividadeComplementar == null) {
            this.totalCargaHorariaAtividadeComplementar = 0;
        }
//		if(this.getListaGradeCurricularTipoAtividadeComplementarVOs().size() > 0){
//			for(GradeCurricularTipoAtividadeComplementarVO obj : this.getListaGradeCurricularTipoAtividadeComplementarVOs()){
//				this.totalCargaHorariaAtividadeComplementar = this.totalCargaHorariaAtividadeComplementar + obj.getCargaHoraria();
//			}
//		}
        return this.totalCargaHorariaAtividadeComplementar;
    }

    public void setTotalCargaHorariaAtividadeComplementar(Integer totalCargaHorariaAtividadeComplementar) {
        this.totalCargaHorariaAtividadeComplementar = totalCargaHorariaAtividadeComplementar;
    }

    public String toString() {
        return "GradeCurricular - Código: " + this.getCodigo() + " Nome: " + this.getNome();
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

    public List<GradeCurricularGrupoOptativaVO> getGradeCurricularGrupoOptativaVOs() {
        if (gradeCurricularGrupoOptativaVOs == null) {
            gradeCurricularGrupoOptativaVOs = new ArrayList<GradeCurricularGrupoOptativaVO>(0);
        }
        return gradeCurricularGrupoOptativaVOs;
    }

    public void setGradeCurricularGrupoOptativaVOs(List<GradeCurricularGrupoOptativaVO> gradeCurricularGrupoOptativaVOs) {
        this.gradeCurricularGrupoOptativaVOs = gradeCurricularGrupoOptativaVOs;
    }

    public List<GradeCurricularEstagioVO> getListaGradeCurricularEstagioVO() {
		if (listaGradeCurricularEstagioVO == null) {
			listaGradeCurricularEstagioVO = new ArrayList<>();
		}
		return listaGradeCurricularEstagioVO;
	}

	public void setListaGradeCurricularEstagioVO(List<GradeCurricularEstagioVO> listaGradeCurricularEstagioVO) {
		this.listaGradeCurricularEstagioVO = listaGradeCurricularEstagioVO;
	}

	public DisciplinaVO getDisciplinaPadraoTcc() {
		if (disciplinaPadraoTcc == null) {
			disciplinaPadraoTcc = new DisciplinaVO();
		}
		return disciplinaPadraoTcc;
	}

	public void setDisciplinaPadraoTcc(DisciplinaVO disciplinaPadraoTcc) {
		this.disciplinaPadraoTcc = disciplinaPadraoTcc;
	}

	public Integer getQtdeAnoSemestreParaIntegralizacaoCurso() {
		if (qtdeAnoSemestreParaIntegralizacaoCurso == null) {
			qtdeAnoSemestreParaIntegralizacaoCurso = 0;
		}
		return qtdeAnoSemestreParaIntegralizacaoCurso;
	}

	public void setQtdeAnoSemestreParaIntegralizacaoCurso(Integer qtdeAnoSemestreParaIntegralizacaoCurso) {
		this.qtdeAnoSemestreParaIntegralizacaoCurso = qtdeAnoSemestreParaIntegralizacaoCurso;
	}

	public RegraContagemPeriodoLetivoEnum getRegraContagemPeriodoLetivoEnum() {
		if (regraContagemPeriodoLetivoEnum == null) {
			regraContagemPeriodoLetivoEnum = RegraContagemPeriodoLetivoEnum.ULTIMO_PERIODO;
		}
		return regraContagemPeriodoLetivoEnum;
	}

	public void setRegraContagemPeriodoLetivoEnum(RegraContagemPeriodoLetivoEnum regraContagemPeriodoLetivoEnum) {
		this.regraContagemPeriodoLetivoEnum = regraContagemPeriodoLetivoEnum;
	}

	public boolean isConsiderarPeriodoTrancadoParaJubilamento() {		
		return considerarPeriodoTrancadoParaJubilamento;
	}

	public void setConsiderarPeriodoTrancadoParaJubilamento(boolean considerarPeriodoTrancadoParaJubilamento) {
		this.considerarPeriodoTrancadoParaJubilamento = considerarPeriodoTrancadoParaJubilamento;
	}

	public Integer getTotalCargaHorariaEstagio() {
        if (totalCargaHorariaEstagio == null) {
            totalCargaHorariaEstagio = 0;
        }
        return totalCargaHorariaEstagio;
    }

    public void setTotalCargaHorariaEstagio(Integer totalCargaHorariaEstagio) {
        this.totalCargaHorariaEstagio = totalCargaHorariaEstagio;
    }
    
    public Double getPercentualPermitirIniciarEstagio() {
		if (percentualPermitirIniciarEstagio == null) {
			percentualPermitirIniciarEstagio = 50.0;
		}
		return percentualPermitirIniciarEstagio;
	}

	public void setPercentualPermitirIniciarEstagio(Double percentualPermitirIniciarEstagio) {
		this.percentualPermitirIniciarEstagio = percentualPermitirIniciarEstagio;
	}

    public Double getPercentualPermitirIniciarTcc() {
		if (percentualPermitirIniciarTcc == null) {
			percentualPermitirIniciarTcc = 62.5;
		}
		return percentualPermitirIniciarTcc;
	}

	public void setPercentualPermitirIniciarTcc(Double percentualPermitirIniciarTcc) {
		this.percentualPermitirIniciarTcc = percentualPermitirIniciarTcc;
	}

	public String getAnoSemestreDataAtivacao() {
        if (anoSemestreDataAtivacao == null) {
            anoSemestreDataAtivacao = "";
        }
        return anoSemestreDataAtivacao;
    }

    public void setAnoSemestreDataAtivacao(String anoSemestreDataAtivacao) {
        this.anoSemestreDataAtivacao = anoSemestreDataAtivacao;
    }

    public String getAnoSemestreDataFinalVigencia() {
        if (anoSemestreDataFinalVigencia == null) {
            anoSemestreDataFinalVigencia = "";
        }
        return anoSemestreDataFinalVigencia;
    }

    public void setAnoSemestreDataFinalVigencia(String anoSemestreDataFinalVigencia) {
        this.anoSemestreDataFinalVigencia = anoSemestreDataFinalVigencia;
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

    public void atualizarTotalCargaHorariaETotalCreditoMatrizCurricular(boolean atualizarCHECreditosPeriodoLetivo) throws Exception {
        Integer totalCH = 0;
        Integer totalCr = 0;
        Integer totalCHObrigatorio = 0;
        for (PeriodoLetivoVO periodo : this.getPeriodoLetivosVOs()) {
            if (atualizarCHECreditosPeriodoLetivo) {
                periodo.atualizarTotalCargaHoraria();
                periodo.atualizarTotalCredito();
            }
            totalCHObrigatorio += periodo.getTotalCargaHorariaPraticaObrigatorio()+periodo.getTotalCargaHorariaTeoricaObrigatorio();
            totalCH += periodo.getTotalCargaHoraria();
            totalCr += periodo.getTotalCreditos();
        }
        this.setTotalCargaHoraria(totalCH);
        this.setTotalCreditos(totalCr);
        this.setTotalCargaHorariaDisciplinasObrigatorias(totalCHObrigatorio);         
    }

	public String getObservacaoHistorico() {
		if (observacaoHistorico == null) {
			observacaoHistorico = "";
		}
		return observacaoHistorico;
	}

	public void setObservacaoHistorico(String observacaoHistorico) {
		this.observacaoHistorico = observacaoHistorico;
	}

	public String getSistemaAvaliacao() {
		if (sistemaAvaliacao == null) {
			sistemaAvaliacao = "";
		}
		return sistemaAvaliacao;
	}

	public void setSistemaAvaliacao(String sistemaAvaliacao) {
		this.sistemaAvaliacao = sistemaAvaliacao;
	}

	public String getCompetenciaProfissional() {
		if (competenciaProfissional == null) {
			competenciaProfissional = "";
		}
		return competenciaProfissional;
	}

	public void setCompetenciaProfissional(String competenciaProfissional) {
		this.competenciaProfissional = competenciaProfissional;
	}
	
	
	public Integer getTotalCargaHorariaPratica() {
		Integer totalCargaHorariaPratica = 0;
		for (PeriodoLetivoVO periodoLetivoVO : getPeriodoLetivosVOs()) {
			totalCargaHorariaPratica += periodoLetivoVO.getTotalCargaHorariaPratica();
		}
		return totalCargaHorariaPratica;
	}

	public Integer getTotalCargaHorariaTeorica() {
		Integer total = 0;
		for (PeriodoLetivoVO periodoLetivoVO : getPeriodoLetivosVOs()) {
			total += periodoLetivoVO.getTotalCargaHorariaTeorica();
		}
		return total;
	}

	public Integer getTotalCargaHorariaPraticaObrigatorio() {
		Integer totalCargaHorariaPratica = 0;
		for (PeriodoLetivoVO periodoLetivoVO : getPeriodoLetivosVOs()) {
			totalCargaHorariaPratica += periodoLetivoVO.getTotalCargaHorariaPraticaObrigatorio();
		}
		return totalCargaHorariaPratica;
	}

	public Integer getTotalCargaHorariaTeoricaObrigatorio() {
		Integer total = 0;
		for (PeriodoLetivoVO periodoLetivoVO : getPeriodoLetivosVOs()) {
			total += periodoLetivoVO.getTotalCargaHorariaTeoricaObrigatorio();
		}
		return total;
	}

	public Integer getTotalCargaHorariaOptativa() {
		Integer total = 0;
		for (PeriodoLetivoVO periodoLetivoVO : getPeriodoLetivosVOs()) {
			total += periodoLetivoVO.getNumeroCargaHorariaOptativa() + periodoLetivoVO.getTotalCargaHorariaPraticaOptativa() + periodoLetivoVO.getTotalCargaHorariaTeoricaOptativa();
		}
		return total;
	}

	public Integer getTotalCreditoOptativa() {
		Integer total = 0;
		for (PeriodoLetivoVO periodoLetivoVO : getPeriodoLetivosVOs()) {
			total += periodoLetivoVO.getNumeroCreditoOptativa() + periodoLetivoVO.getTotalCreditoOptativa();
		}
		return total;
	}

	public Integer getTotalCreditoObrigatorio() {
		Integer total = 0;
		for (PeriodoLetivoVO periodoLetivoVO : getPeriodoLetivosVOs()) {
			total += periodoLetivoVO.getNumeroCreditoOptativa() + periodoLetivoVO.getTotalCreditoObrigatorio();
		}
		return total;
	}

	public Integer getTotalCredito() {
		Integer total = 0;
		for (PeriodoLetivoVO periodoLetivoVO : getPeriodoLetivosVOs()) {
			total += periodoLetivoVO.getTotalCredito();
		}
		return total;
	}

	public Double getTotalCreditoFinanceiro() {
		Double total = 0.0;
		for (PeriodoLetivoVO periodoLetivoVO : getPeriodoLetivosVOs()) {
			total += periodoLetivoVO.getTotalCreditoFinanceiro();
		}
		return total;
	}

	public Double getTotalCreditoFinanceiroObrigatorio() {
		Double total = 0.0;
		for (PeriodoLetivoVO periodoLetivoVO : getPeriodoLetivosVOs()) {
			total += periodoLetivoVO.getTotalCreditoFinanceiroObrigatorio();
		}
		return total;
	}

	public Double getTotalCreditoFinanceiroOptativa() {
		Double total = 0.0;
		for (PeriodoLetivoVO periodoLetivoVO : getPeriodoLetivosVOs()) {
			total += periodoLetivoVO.getTotalCreditoFinanceiroOptativa();
		}
		return total;
	}
    
    public PeriodoLetivoVO getPeriodoLetivoPorNumero(Integer numeroPeriodo) {
        for (PeriodoLetivoVO periodoLetivosVO : this.getPeriodoLetivosVOs()) {
            if (periodoLetivosVO.getPeriodoLetivo().equals(numeroPeriodo)) {
                return periodoLetivosVO;
            }
        }
        return null;
    }
    
    /**
     * Método responsável por percorrer todas as gradeDisciplinas desta matriz curricular
     * e obter GradeDisciplina corresponde a disciplina e carga horaria fornecida como parametro
     * Utilizado: TransferenciaMatrizCurricular
     * @param gradeDisciplinaVO
     * @return 
     */
    public GradeDisciplinaVO obterGradeDisciplinaCorrespondente(Integer codigoDisciplina, Integer cargaHoraria) {
        GradeDisciplinaVO gradeDisciplinaCorrspondenteVO = null;
        // Primeiro, busca-se no mesmo período da gradeDisciplina fornecida como parametro
        for (PeriodoLetivoVO periodoLetivosVO : this.getPeriodoLetivosVOs()) {
            gradeDisciplinaCorrspondenteVO = periodoLetivosVO.obterGradeDisciplinaCorrespondente(codigoDisciplina, cargaHoraria);
            if (gradeDisciplinaCorrspondenteVO != null) {
                return gradeDisciplinaCorrspondenteVO;
            }
        }
        return null;
    }

    /**
     * Método responsável por percorrer todas as gradeDisciplinas desta matriz curricular
     * e obter GradeDisciplina corresponde a informada como parametro
     * Utilizado: TransferenciaMatrizCurricular
     * @param gradeDisciplinaVO
     * @return 
     */
    public GradeDisciplinaVO obterGradeDisciplinaCorrespondente(GradeDisciplinaVO gradeDisciplinaBuscarVO) {
        GradeDisciplinaVO gradeDisciplinaCorrspondenteVO = null;
        // Primeiro, busca-se no mesmo período da gradeDisciplina fornecida como parametro
        Integer nrPeriodoLetivoOrigem = gradeDisciplinaBuscarVO.getPeriodoLetivoVO().getPeriodoLetivo();
        PeriodoLetivoVO periodoLetivoVOOrigem = this.getPeriodoLetivoPorNumero(nrPeriodoLetivoOrigem);
        if (periodoLetivoVOOrigem != null) {
            // procurar no periodo origem, pois é maior a chance de encontrar o mesmo
            gradeDisciplinaCorrspondenteVO = periodoLetivoVOOrigem.obterGradeDisciplinaCorrespondente(gradeDisciplinaBuscarVO.getDisciplina().getCodigo(), gradeDisciplinaBuscarVO.getCargaHoraria());
            if (gradeDisciplinaCorrspondenteVO != null) {
                return gradeDisciplinaCorrspondenteVO;
            }
        }
        // se nao encontrou no periodo letivo acima, entao temos que busar nos demais periodos
        for (PeriodoLetivoVO periodoLetivosVO : this.getPeriodoLetivosVOs()) {
            gradeDisciplinaCorrspondenteVO = periodoLetivosVO.obterGradeDisciplinaCorrespondente(gradeDisciplinaBuscarVO.getDisciplina().getCodigo(), gradeDisciplinaBuscarVO.getCargaHoraria());
            if (gradeDisciplinaCorrspondenteVO != null) {
                return gradeDisciplinaCorrspondenteVO;
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
    public GradeCurricularGrupoOptativaDisciplinaVO obterGradeCurricularGrupoOptativaCorrespondente(GradeDisciplinaVO gradeDisciplinaBuscarVO) {
        GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO = null;
        // Primeiro, busca-se no mesmo período da gradeDisciplina fornecida como parametro
        Integer nrPeriodoLetivoOrigem = gradeDisciplinaBuscarVO.getPeriodoLetivoVO().getPeriodoLetivo();
        PeriodoLetivoVO periodoLetivoVOOrigem = this.getPeriodoLetivoPorNumero(nrPeriodoLetivoOrigem);
        if (periodoLetivoVOOrigem != null) {
            // procurar no periodo origem, pois é maior a chance de encontrar o mesmo
            gradeCurricularGrupoOptativaDisciplinaVO = periodoLetivoVOOrigem.obterGradeCurricularGrupoOptativaCorrespondente(gradeDisciplinaBuscarVO.getDisciplina().getCodigo(), gradeDisciplinaBuscarVO.getCargaHoraria());
            if (gradeCurricularGrupoOptativaDisciplinaVO != null) {
                gradeCurricularGrupoOptativaDisciplinaVO.setPeriodoLetivoDisciplinaReferenciada(periodoLetivoVOOrigem);
                return gradeCurricularGrupoOptativaDisciplinaVO;
            }
        }
        // se nao encontrou no periodo letivo acima, entao temos que busar nos demais periodos
        for (PeriodoLetivoVO periodoLetivosVO : this.getPeriodoLetivosVOs()) {
            gradeCurricularGrupoOptativaDisciplinaVO = periodoLetivosVO.obterGradeCurricularGrupoOptativaCorrespondente(gradeDisciplinaBuscarVO.getDisciplina().getCodigo(), gradeDisciplinaBuscarVO.getCargaHoraria());
            if (gradeCurricularGrupoOptativaDisciplinaVO != null) {
                gradeCurricularGrupoOptativaDisciplinaVO.setPeriodoLetivoDisciplinaReferenciada(periodoLetivosVO);
                return gradeCurricularGrupoOptativaDisciplinaVO;
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
    public GradeCurricularGrupoOptativaDisciplinaVO obterGradeCurricularGrupoOptativaCorrespondente(
            GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVOBuscar, Integer nrPeriodoLetivoPriorizar) {
        GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO = null;
        // Primeiro, busca-se no mesmo período da gradeDisciplina fornecida como parametro
        PeriodoLetivoVO periodoLetivoVOOrigem = this.getPeriodoLetivoPorNumero(nrPeriodoLetivoPriorizar);
        if (periodoLetivoVOOrigem != null) {
            // procurar no periodo origem, pois é maior a chance de encontrar o mesmo
            gradeCurricularGrupoOptativaDisciplinaVO = periodoLetivoVOOrigem.obterGradeCurricularGrupoOptativaCorrespondente(gradeCurricularGrupoOptativaDisciplinaVOBuscar.getDisciplina().getCodigo(), gradeCurricularGrupoOptativaDisciplinaVOBuscar.getCargaHoraria());
            if (gradeCurricularGrupoOptativaDisciplinaVO != null) {
                return gradeCurricularGrupoOptativaDisciplinaVO;
            }
        }
        // se nao encontrou no periodo letivo acima, entao temos que busar nos demais periodos
        for (PeriodoLetivoVO periodoLetivosVO : this.getPeriodoLetivosVOs()) {
            gradeCurricularGrupoOptativaDisciplinaVO = periodoLetivosVO.obterGradeCurricularGrupoOptativaCorrespondente(gradeCurricularGrupoOptativaDisciplinaVOBuscar.getDisciplina().getCodigo(), gradeCurricularGrupoOptativaDisciplinaVOBuscar.getCargaHoraria());
            if (gradeCurricularGrupoOptativaDisciplinaVO != null) {
                return gradeCurricularGrupoOptativaDisciplinaVO;
            }
        }
        return null;
    }

    public GradeDisciplinaVO obterGradeDisciplinaCorrespondente(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO, Integer periodoLetivoPriorizar) {
        GradeDisciplinaVO gradeDisciplinaCorrspondenteVO = null;
        // Primeiro, busca-se no mesmo período da gradeDisciplina fornecida como parametro
        PeriodoLetivoVO periodoLetivoVOOrigem = this.getPeriodoLetivoPorNumero(periodoLetivoPriorizar);
        if (periodoLetivoVOOrigem != null) {
            // procurar no periodo origem, pois é maior a chance de encontrar o mesmo
            gradeDisciplinaCorrspondenteVO = periodoLetivoVOOrigem.obterGradeDisciplinaCorrespondente(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo(), gradeCurricularGrupoOptativaDisciplinaVO.getCargaHoraria());
            if (gradeDisciplinaCorrspondenteVO != null) {
                return gradeDisciplinaCorrspondenteVO;
            }
        }
        // se nao encontrou no periodo letivo acima, entao temos que busar nos demais periodos
        for (PeriodoLetivoVO periodoLetivosVO : this.getPeriodoLetivosVOs()) {
            gradeDisciplinaCorrspondenteVO = periodoLetivosVO.obterGradeDisciplinaCorrespondente(gradeCurricularGrupoOptativaDisciplinaVO.getDisciplina().getCodigo(), gradeCurricularGrupoOptativaDisciplinaVO.getCargaHoraria());
            if (gradeDisciplinaCorrspondenteVO != null) {
                return gradeDisciplinaCorrspondenteVO;
            }
        }
        return null;
    }
    
/**
     * Método responsável por percorrer todas as GradeCurricularGrupoOptativaDisciplinaVO para mapear
     * uma disciplina optativa correspondente ao código da disciplina e carga horária fornecida como parametro
     * Utilizado: TransferenciaMatrizCurricular
     * @param gradeDisciplinaVO
     * @return 
     */
    public GradeCurricularGrupoOptativaDisciplinaVO obterGradeCurricularGrupoOptativaCorrespondente(
            Integer codigoDisciplina, Integer cargaHoraria) {
        GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplinaVO = null;
        // se nao encontrou no periodo letivo acima, entao temos que busar nos demais periodos
        for (PeriodoLetivoVO periodoLetivosVO : this.getPeriodoLetivosVOs()) {
            gradeCurricularGrupoOptativaDisciplinaVO = periodoLetivosVO.obterGradeCurricularGrupoOptativaCorrespondente(codigoDisciplina, cargaHoraria);
            if (gradeCurricularGrupoOptativaDisciplinaVO != null) {
                // setando o periodo letivo no qual a disciplia foi encontrada. Assim podemos determinar em qual periodo letivo a disciplina grupo optativa foi encontrada
                gradeCurricularGrupoOptativaDisciplinaVO.setPeriodoLetivoDisciplinaReferenciada(periodoLetivosVO);
                return gradeCurricularGrupoOptativaDisciplinaVO;
            }
        }
        return null;
    }    
    
    public String getResolucao() {
		if (resolucao == null) {
			resolucao = "";
		}
		return resolucao;
	}
	
	public void setResolucao(String resolucao) {
		this.resolucao = resolucao;
	}
	
	
	
	public Integer getNrMesesConclusaoMatrizCurricular() {
		if (nrMesesConclusaoMatrizCurricular == null) {
			nrMesesConclusaoMatrizCurricular = 0;
		}
		return nrMesesConclusaoMatrizCurricular;
	}

	public void setNrMesesConclusaoMatrizCurricular(Integer nrMesesConclusaoMatrizCurricular) {
		this.nrMesesConclusaoMatrizCurricular = nrMesesConclusaoMatrizCurricular;
	}

	public Integer getTotalCargaHorariaDisciplinasObrigatorias() {
		if (totalCargaHorariaDisciplinasObrigatorias == null) {
			totalCargaHorariaDisciplinasObrigatorias = 0;
		}
		return totalCargaHorariaDisciplinasObrigatorias;
	}

	public void setTotalCargaHorariaDisciplinasObrigatorias(
			Integer totalCargaHorariaDisciplinasObrigatorias) {
		this.totalCargaHorariaDisciplinasObrigatorias = totalCargaHorariaDisciplinasObrigatorias;
	}

	public Integer getTotalCargaHorariaOptativaExigida() {
		Integer total = getCargaHoraria()-getTotalCargaHorariaEstagio()-getTotalCargaHorariaAtividadeComplementar()-getTotalCargaHorariaDisciplinasObrigatorias();
		if(total < 0) {
			return 0;
		}
		return total;
	}

	public Integer getQuantidadeDisciplinasOptativasMatrizCurricular() {
		if(quantidadeDisciplinasOptativasMatrizCurricular == null) {
			quantidadeDisciplinasOptativasMatrizCurricular = 0;
		}
		return quantidadeDisciplinasOptativasMatrizCurricular;
	}

	public void setQuantidadeDisciplinasOptativasMatrizCurricular(Integer quantidadeDisciplinasOptativasMatrizCurricular) {
		this.quantidadeDisciplinasOptativasMatrizCurricular = quantidadeDisciplinasOptativasMatrizCurricular;
	}
	

	public String getHabilitacao() {
		if (habilitacao == null) {
			habilitacao = "";
		}
		return habilitacao;
	}

	public void setHabilitacao(String habilitacao) {
		this.habilitacao = habilitacao;
	}
	public Integer getTotalCreditoOptativaExigida() {
		Integer total = getCreditos() - getTotalCreditoDisciplinasObrigatorias();
		if(total < 0) {
			return 0;
		}
		return total;
	}
	
	public Integer getTotalCreditoDisciplinasObrigatorias() {
		Integer	totalCreditoDisciplinasObrigatorias = 0;
			for (PeriodoLetivoVO periodoLetivoVO : getPeriodoLetivosVOs()) {
				for(GradeDisciplinaVO gradeDisciplinaVO: periodoLetivoVO.getGradeDisciplinaVOs()) {
					if(!gradeDisciplinaVO.getIsDisciplinaOptativa()) {
						totalCreditoDisciplinasObrigatorias += gradeDisciplinaVO.getNrCreditos();			
					}
				}
		}
		return totalCreditoDisciplinasObrigatorias;
	}
	
	public GradeCurricularVO getGradeCurricularOriginalVO() {
		if (gradeCurricularOriginalVO == null) {
			gradeCurricularOriginalVO = new GradeCurricularVO();
		}
		return gradeCurricularOriginalVO;
	}

	public void setGradeCurricularOriginalVO(GradeCurricularVO gradeCurricularOriginalVO) {
		this.gradeCurricularOriginalVO = gradeCurricularOriginalVO;
	}
	
	public void substituirGradeDisciplina(GradeDisciplinaVO gradeDisciplinaPeriodoVO) {
		for (PeriodoLetivoVO periodoLetivoVO : this.getPeriodoLetivosVOs()) {
			
			if (periodoLetivoVO.getCodigo().equals(gradeDisciplinaPeriodoVO.getPeriodoLetivoVO().getCodigo())) {
				int index = 0;
				for (GradeDisciplinaVO gradeDisciplinaVO : periodoLetivoVO.getGradeDisciplinaVOs()) {
					if (gradeDisciplinaVO.getCodigo().equals(gradeDisciplinaPeriodoVO.getCodigo())) {
						periodoLetivoVO.getGradeDisciplinaVOs().set(index, gradeDisciplinaPeriodoVO);
						break;
					}
					index++;
				}
			}
		}
	}
	
	public Integer getChDisciplinaObrigatoria() {
		Boolean calcularEstagio = Uteis.isAtributoPreenchido(getTotalCargaHorariaEstagio());
		Integer cargaHorariaObrigatoria = 0;
    	if (Uteis.isAtributoPreenchido(getPeriodoLetivosVOs())) {
    		Optional<Integer> cargaHoraria = getPeriodoLetivosVOs().stream().map(p -> p.getChDisciplinaObrigatoria(calcularEstagio)).reduce(Integer::sum);
    		cargaHorariaObrigatoria = cargaHoraria.isPresent() ? cargaHoraria.get() : 0;
		}
    	return (cargaHorariaObrigatoria);
    }
    
    public Integer getChDisciplinaObrigatoriaPratica() {
    	Boolean calcularEstagio = Uteis.isAtributoPreenchido(getTotalCargaHorariaEstagio());
    	Integer cargaHorariaObrigatoriaPratica = 0;
    	if (Uteis.isAtributoPreenchido(getPeriodoLetivosVOs())) {
    		Optional<Integer> cargaHoraria = getPeriodoLetivosVOs().stream().map(p -> p.getChDisciplinaObrigatoriaPratica(calcularEstagio)).reduce(Integer::sum);
    		cargaHorariaObrigatoriaPratica = cargaHoraria.isPresent() ? cargaHoraria.get() : 0;
    	}
    	return (cargaHorariaObrigatoriaPratica);
    }
    
    public Integer getChDisciplinaObrigatoriaTeorica() {
    	Boolean calcularEstagio = Uteis.isAtributoPreenchido(getTotalCargaHorariaEstagio());
    	Integer cargaHorariaObrigatoriaTeorica = 0;
    	if (Uteis.isAtributoPreenchido(getPeriodoLetivosVOs())) {
    		Optional<Integer> cargaHoraria = getPeriodoLetivosVOs().stream().map(p -> p.getChDisciplinaObrigatoriaTeorica(calcularEstagio)).reduce(Integer::sum);
    		cargaHorariaObrigatoriaTeorica = cargaHoraria.isPresent() ? cargaHoraria.get() : 0;
    	}
    	return (cargaHorariaObrigatoriaTeorica);
    }
    
    public Integer getChDisciplinaEstagio() {
    	if (Uteis.isAtributoPreenchido(getTotalCargaHorariaEstagio())) {
    		return getTotalCargaHorariaEstagio();
    	} else {
    		Integer cargaHorariaObrigatoriaEstagio = 0;
    		if (Uteis.isAtributoPreenchido(getPeriodoLetivosVOs())) {
    			Optional<Integer> cargaHoraria = getPeriodoLetivosVOs().stream().map(p -> p.getChDisciplinaEstagio()).reduce(Integer::sum);
    			cargaHorariaObrigatoriaEstagio = cargaHoraria.isPresent() ? cargaHoraria.get() : 0;
    		}
    		return (cargaHorariaObrigatoriaEstagio);
    	}
    }
    
    public Integer getChDisciplinaOptativa() {
		Boolean calcularEstagio = Uteis.isAtributoPreenchido(getTotalCargaHorariaEstagio());
		Integer cargaHorariaOptativa = 0;
    	if (Uteis.isAtributoPreenchido(getPeriodoLetivosVOs())) {
    		Optional<Integer> cargaHorariaGrupoOptativaDisciplina = getPeriodoLetivosVOs().stream().map(p -> p.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()).flatMap(Collection::stream).map(GradeCurricularGrupoOptativaDisciplinaVO::getCargaHoraria).reduce(Integer::sum);
    		Optional<Integer> cargaHoraria = getPeriodoLetivosVOs().stream().map(p -> p.getChDisciplinaOptativa(calcularEstagio)).reduce(Integer::sum);
    		cargaHorariaOptativa = (cargaHoraria.isPresent() ? cargaHoraria.get() : 0) + (cargaHorariaGrupoOptativaDisciplina.isPresent() ? cargaHorariaGrupoOptativaDisciplina.get() : 0);
		}
    	return (cargaHorariaOptativa);
    }
    
    public Integer getChDisciplinaOptativaPratica() {
    	Boolean calcularEstagio = Uteis.isAtributoPreenchido(getTotalCargaHorariaEstagio());
    	Integer cargaHorariaOptativaPratica = 0;
    	if (Uteis.isAtributoPreenchido(getPeriodoLetivosVOs())) {
    		Optional<Integer> cargaHorariaGrupoOptativaDisciplina = getPeriodoLetivosVOs().stream().map(p -> p.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()).flatMap(Collection::stream).map(GradeCurricularGrupoOptativaDisciplinaVO::getCargaHorariaPratica).reduce(Integer::sum);
    		Optional<Integer> cargaHoraria = getPeriodoLetivosVOs().stream().map(p -> p.getChDisciplinaOptativaPratica(calcularEstagio)).reduce(Integer::sum);
    		cargaHorariaOptativaPratica = (cargaHoraria.isPresent() ? cargaHoraria.get() : 0) + (cargaHorariaGrupoOptativaDisciplina.isPresent() ? cargaHorariaGrupoOptativaDisciplina.get() : 0);
    	}
    	return (cargaHorariaOptativaPratica);
    }
    
    public Integer getChDisciplinaOptativaTeorica() {
    	Boolean calcularEstagio = Uteis.isAtributoPreenchido(getTotalCargaHorariaEstagio());
    	Integer cargaHorariaOptativaTeorica = 0;
    	if (Uteis.isAtributoPreenchido(getPeriodoLetivosVOs())) {
    		Optional<Integer> cargaHorariaGrupoOptativaDisciplina = getPeriodoLetivosVOs().stream().map(p -> p.getGradeCurricularGrupoOptativa().getGradeCurricularGrupoOptativaDisciplinaVOs()).flatMap(Collection::stream).map(GradeCurricularGrupoOptativaDisciplinaVO::getCargaHorariaTeorica).reduce(Integer::sum);
    		Optional<Integer> cargaHoraria = getPeriodoLetivosVOs().stream().map(p -> p.getChDisciplinaOptativaTeorica(calcularEstagio)).reduce(Integer::sum);
    		cargaHorariaOptativaTeorica = (cargaHoraria.isPresent() ? cargaHoraria.get() : 0) + (cargaHorariaGrupoOptativaDisciplina.isPresent() ? cargaHorariaGrupoOptativaDisciplina.get() : 0);
    	}
    	return (cargaHorariaOptativaTeorica);
    }
    
    public Integer getChDisciplinaTotal() {
    	Integer totalCargaHorariaDisciplina = 0;
    	if (Uteis.isAtributoPreenchido(getPeriodoLetivosVOs())) {
    		Optional<Integer> cargaHoraria = getPeriodoLetivosVOs().stream().map(PeriodoLetivoVO::getGradeDisciplinaVOs).flatMap(Collection::stream).map(GradeDisciplinaVO::getCargaHoraria).reduce(Integer::sum);
    		totalCargaHorariaDisciplina = cargaHoraria.isPresent() ? cargaHoraria.get() : 0;
    	}
		return totalCargaHorariaDisciplina;
    }
    
    public Integer getChDisciplinaPraticaTotal() {
    	Integer totalCargaHorariaDisciplinaPratica = 0;
    	if (Uteis.isAtributoPreenchido(getPeriodoLetivosVOs())) {
    		Optional<Integer> cargaHoraria = getPeriodoLetivosVOs().stream().map(PeriodoLetivoVO::getGradeDisciplinaVOs).flatMap(Collection::stream).map(GradeDisciplinaVO::getCargaHorariaPratica).reduce(Integer::sum);
    		totalCargaHorariaDisciplinaPratica = cargaHoraria.isPresent() ? cargaHoraria.get() : 0;
    	}
    	return totalCargaHorariaDisciplinaPratica;
    }
    
    public Integer getChDisciplinaTeoricaTotal() {
    	Integer totalCargaHorariaDisciplinaTeorica = 0;
    	if (Uteis.isAtributoPreenchido(getPeriodoLetivosVOs())) {
    		Optional<Integer> cargaHoraria = getPeriodoLetivosVOs().stream().map(PeriodoLetivoVO::getGradeDisciplinaVOs).flatMap(Collection::stream).map(GradeDisciplinaVO::getCargaHorariaTeorica).reduce(Integer::sum);
    		totalCargaHorariaDisciplinaTeorica = cargaHoraria.isPresent() ? cargaHoraria.get() : 0;
    	}
    	return totalCargaHorariaDisciplinaTeorica;
    }
    
    public Integer getTotalCargaHorariaTeoricaOptativa() {
		Integer total = 0;
		for (PeriodoLetivoVO periodoLetivoVO : getPeriodoLetivosVOs()) {
			total += periodoLetivoVO.getTotalCargaHorariaTeoricaOptativa();
		}
		return total;
	}
    
    public Integer getTotalCargaHorariaPraticaOptativa() {
		Integer total = 0;
		for (PeriodoLetivoVO periodoLetivoVO : getPeriodoLetivosVOs()) {
			total += periodoLetivoVO.getTotalCargaHorariaPraticaOptativa();
		}
		return total;
	}

    public String getPrazojubilamento() {
        if(prazojubilamento == null){
            prazojubilamento = "";
        }
        return prazojubilamento;
    }

    public void setPrazojubilamento(String prazojubilamento) {
        this.prazojubilamento = prazojubilamento;
    }

	public Boolean getExisteGradeCurricularEstagio() {
		if (existeGradeCurricularEstagio == null) {
			existeGradeCurricularEstagio = false;
		}
		return existeGradeCurricularEstagio;
	}

	public void setExisteGradeCurricularEstagio(Boolean existeGradeCurricularEstagio) {
		this.existeGradeCurricularEstagio = existeGradeCurricularEstagio;
	}
	
	private String corDoTextoJubilamento;
	
	public String getCorDoTextoJubilamento() {
		if(corDoTextoJubilamento == null){
			corDoTextoJubilamento = "texto-green";
		}
		return corDoTextoJubilamento;
	}

	public void setCorDoTextoJubilamento(String corDoTextoJubilamento) {
		this.corDoTextoJubilamento = corDoTextoJubilamento;
	}

}
