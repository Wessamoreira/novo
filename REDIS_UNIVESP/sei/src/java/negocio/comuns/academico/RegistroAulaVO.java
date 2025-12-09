package negocio.comuns.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import controle.academico.RegistroAulaNotaControle;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
/**
 * Reponsável por manter os dados da entidade RegistroAula. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import webservice.DateAdapterMobile;

@XmlRootElement(name = "registroAula")
public class RegistroAulaVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private Date dataOriginal;
    private Integer cargaHoraria;
    private Integer qtdFaltas;
    private String cargaHorariaStr;
    private String conteudo;
    private Date dataRegistroAula;
    private String tipoAula;
    private String diaSemana;
    private String semestre;
    private String ano;
    private Integer nrAula;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>FrequenciaAula</code>.
     */
    private List<FrequenciaAulaVO> frequenciaAulaVOs;
    private TurmaVO turma;
    private DisciplinaVO disciplina;
    private GradeDisciplinaVO gradeDisciplinaVO;
    private HorarioTurmaVO horarioTurma;
    private PessoaVO professor;
    private String horario;
    private RegistroAulaConteudoVO registroAulaConteudoVO;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private UsuarioVO responsavelRegistroAula;
    private List<RegistroAulaVO> listaVerso1;
    private List<RegistroAulaVO> listaVerso2;
    //Atributo para controle tela
    private String situacaoAbonoFalta;
    private Boolean atividadeComplementar;
    
	/**
	 * Utilizado no {@link RegistroAulaNotaControle} para replicar o conteúdo
	 * inserido em um dia para todos os outros. Dado não persistido
	 */
	private Boolean replicarConteudoTodosDias;
	private Integer turmaOrigem;

	private String praticaSupervisionada;
	
	private Integer qtdAulasMinistradas;
	
	private Boolean isAulaNaoRegistrada;
	
	//TRANSIENT
	private String nivelEducacional;
	
	public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>RegistroAula</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public RegistroAulaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>RegistroAulaVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(RegistroAulaVO obj, Boolean permiteRegistrarAulaFutura) throws ConsistirException {
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Registrar Realização Aula) deve ser informado.");
        }
        if (!permiteRegistrarAulaFutura) {
        	if (Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(obj.getData()).compareTo(Uteis.getDataComHoraSetadaParaPrimeiroMinutoDia(new Date())) >= 1) {
        		throw new ConsistirException("Não é possível registrar aula para uma DATA FUTURA.");
        	}
        }
        if ((obj.getTurma() == null) || (obj.getTurma().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TURMA (Registrar Realização Aula) deve ser informado.");
        }
        if ((obj.getDisciplina() == null) || (obj.getDisciplina().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo DISCIPLINA (Registrar Realização Aula) deve ser informado.");
        }
        if ((obj.getProfessor() == null) || (obj.getProfessor().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo PROFESSOR (Registrar Realização Aula) deve ser informado.");
        }
        if (obj.getCargaHoraria().intValue() == 0) {
            throw new ConsistirException("O campo CARGA HORÁRIA (Registrar Realização Aula) deve ser informado.");
        }
        if (obj.getConteudo().trim().equals("")) {
            throw new ConsistirException("O campo CONTEÚDO ABORDADO (Registrar Realização Aula) deve ser informado.");
        }
        if ((obj.getResponsavelRegistroAula() == null)
                || (obj.getResponsavelRegistroAula().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL (Registrar Realização Aula) deve ser informado.");
        }
        if (obj.getDataRegistroAula() == null) {
            throw new ConsistirException("O campo DATA REGISTRO AULA (Registrar Realização Aula) deve ser informado.");
        }
        if (obj.getFrequenciaAulaVOs().isEmpty()) {
            throw new ConsistirException("É necessário montar a lista de alunos para esta aula");
        }
        obj.montarDiaSemanaAula();
    }

    public static void validarDadosRegistroAulaTurma(RegistroAulaVO obj) throws ConsistirException {
        if ((obj.getTurma() == null) || (obj.getTurma().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TURMA (Registrar Realização Aula) deve ser informado.");
        }
        if ((obj.getDisciplina() == null) || (obj.getDisciplina().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo DISCIPLINA (Registrar Realização Aula) deve ser informado.");
        }

        if (obj.getTurma().getSemestral()) {
            if (obj.getSemestre().equals("")) {
                throw new ConsistirException("O campo SEMESTRE (Registrar Realização Aula) deve ser informado");
            }
            if (obj.getAno().equals("")) {
                throw new ConsistirException("O campo ANO (Registrar Realização Aula) deve ser informado");
            }
        } else if (obj.getTurma().getAnual()) {
            if (obj.getAno().equals("")) {
                throw new ConsistirException("O campo ANO (Registrar Realização Aula) deve ser informado");
            }
            obj.setSemestre("");
        } else if (obj.getTurma().getIntegral()) {
            obj.setAno("");
            obj.setSemestre("");
        }
        obj.montarDiaSemanaAula();
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setData(new Date());
        setCargaHoraria(0);
        setConteudo("");
        setDataRegistroAula(new Date());
        setTipoAula("P");
        setDiaSemana("");
        setSemestre("");
        setAno(Uteis.getAnoDataAtual4Digitos());
        montarDiaSemanaAula();
    }

    @XmlElement(name = "diaSemana")
    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    public String getDiaSemana_Apresentar() {
        if (diaSemana.equals("01")) {
            return "Domingo";
        }
        if (diaSemana.equals("02")) {
            return "Segunda";
        }
        if (diaSemana.equals("03")) {
            return "Terça";
        }
        if (diaSemana.equals("04")) {
            return "Quarta";
        }
        if (diaSemana.equals("05")) {
            return "Quinta";
        }
        if (diaSemana.equals("06")) {
            return "Sexta";
        }
        if (diaSemana.equals("07")) {
            return "Sábado";
        }
        return (diaSemana);
    }

    public boolean getPermiteAlterarCargaHoraria() {
        if (getTipoAula().equals("P")) {
            return false;
        }
        return true;
    }

    @XmlElement(name = "ano")
    public String getAno() {
    	if (ano == null) {
    		ano = "";
    	}
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }
    
    public String getAnoSemestre() {
    	return getAno() +" / "+ getSemestre();
    }

    @XmlElement(name = "semestre")
    public String getSemestre() {
    	if (semestre == null) {
    		semestre = "";
    	}
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    @XmlElement(name = "professor")
    public PessoaVO getProfessor() {
        if (professor == null) {
            professor = new PessoaVO();
        }
        return professor;
    }

    public void setProfessor(PessoaVO professor) {
        this.professor = professor;
    }

    public HorarioTurmaVO getHorarioTurma() {
        if (horarioTurma == null) {
            horarioTurma = new HorarioTurmaVO();
        }
        return horarioTurma;
    }

    public void setHorarioTurma(HorarioTurmaVO horarioTurma) {
        this.horarioTurma = horarioTurma;
    }

    @XmlElement(name = "turma")
    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    @XmlElement(name = "disciplina")
    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>RegistroAula</code>).
     */
    public UsuarioVO getResponsavelRegistroAula() {
        if (responsavelRegistroAula == null) {
            responsavelRegistroAula = new UsuarioVO();
        }
        return (responsavelRegistroAula);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>RegistroAula</code>).
     */
    public void setResponsavelRegistroAula(UsuarioVO obj) {
        this.responsavelRegistroAula = obj;
    }

    /**
     * /** Retorna Atributo responsável por manter os objetos da classe
     * <code>FrequenciaAula</code>.
     */
    @XmlElement(name = "frequenciaAulaVOs")
    public List<FrequenciaAulaVO> getFrequenciaAulaVOs() {
        if (frequenciaAulaVOs == null) {
            frequenciaAulaVOs = new ArrayList<FrequenciaAulaVO>(0);
        }
        return (frequenciaAulaVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>FrequenciaAula</code>.
     */
    public void setFrequenciaAulaVOs(List<FrequenciaAulaVO> frequenciaAulaVOs) {
        this.frequenciaAulaVOs = frequenciaAulaVOs;
    }

    @XmlElement(name = "dataRegistroAula") 
    @XmlJavaTypeAdapter(DateAdapterMobile.class)
    public Date getDataRegistroAula() {
        return (dataRegistroAula);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataRegistroAula_Apresentar() {
        return (Uteis.getData(dataRegistroAula));
    }

    public void setDataRegistroAula(Date dataRegistroAula) {
        this.dataRegistroAula = dataRegistroAula;
    }

    @XmlElement(name = "conteudo")
    public String getConteudo() {
        return (conteudo);
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    @XmlElement(name = "cargaHoraria")
    public Integer getCargaHoraria() {
        return (cargaHoraria);
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    @XmlElement(name = "data")
    @XmlJavaTypeAdapter(DateAdapterMobile.class)
    public Date getData() {
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return (Uteis.getData(data));
    }

    public void setData(Date data) {
        this.data = data;
    }

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @XmlElement(name = "tipoAula")
    public String getTipoAula() {
        return tipoAula;
    }

    public void setTipoAula(String tipoAula) {
        this.tipoAula = tipoAula;
    }

    public JRDataSource getListaFrequencia() {
        Ordenacao.ordenarLista(frequenciaAulaVOs, "ordenacao");
        JRDataSource jr = new JRBeanCollectionDataSource(getFrequenciaAulaVOs());
        return jr;
    }

    public void montarDiaSemanaAula() {
        
            int x = Uteis.getDiaSemana(this.getData());
            this.setDiaSemana("0" + x);
        
    }
    @XmlElement(name = "horario")
    public String getHorario() {
        if (horario == null) {
            horario = "";
        }
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getCargaHorariaStr() {
        if (cargaHorariaStr == null) {
            cargaHorariaStr = "";
        }
        return cargaHorariaStr;
    }

    public void setCargaHorariaStr(String cargaHorariaStr) {
        this.cargaHorariaStr = cargaHorariaStr;
    }
    
    public String getSomenteHorarioAula_Apresentar() {
        String horarioCompleto = getHorario();
        if (horarioCompleto.indexOf("(") != -1) {
            String hor = horarioCompleto.substring(horarioCompleto.indexOf("(") + 1, horarioCompleto.indexOf(")"));
            return hor;
        }
        return getHorario();
    }
    
    public String getHorarioNrAula_Apresentar() {
        String horarioCompleto = getHorario();
        if (horarioCompleto.indexOf("(") != -1) {
            String nrAula = horarioCompleto.substring(0, horarioCompleto.indexOf("(") - 1);
            return nrAula;
        }
        return getHorario();
    }

    public String getHorario_Apresentar() {
        if(getHorario().equals("")){
            return "";
        }
        return getHorario().substring(0, getHorario().indexOf("("));
    }

    public long getOrdenacao() {
        return Long.valueOf(getNrAula()+""+getData().getTime());
    }

    public RegistroAulaConteudoVO getRegistroAulaConteudoVO() {
        if (registroAulaConteudoVO == null) {
            registroAulaConteudoVO = new RegistroAulaConteudoVO();
        }
        return registroAulaConteudoVO;
    }

    public void setRegistroAulaConteudoVO(RegistroAulaConteudoVO registroAulaConteudoVO) {
        this.registroAulaConteudoVO = registroAulaConteudoVO;
    }

    public List<RegistroAulaVO> getListaVerso1() {
        if (listaVerso1 == null) {
            listaVerso1 = new ArrayList<RegistroAulaVO>();
        }
        return listaVerso1;
    }

    public void setListaVerso1(List<RegistroAulaVO> listaVerso1) {
        this.listaVerso1 = listaVerso1;
    }

    public List<RegistroAulaVO> getListaVerso2() {
        if (listaVerso2 == null) {
            listaVerso2 = new ArrayList<RegistroAulaVO>();
        }
        return listaVerso2;
    }

    public void setListaVerso2(List<RegistroAulaVO> listaVerso2) {

        this.listaVerso2 = listaVerso2;
    }

    public JRDataSource getListaConteudoVerso1() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaVerso1().toArray());
        return jr;
    }

    public JRDataSource getListaConteudoVerso2() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaVerso2().toArray());
        return jr;
    }

    /**
     * @return the qtdFaltas
     */
    public Integer getQtdFaltas() {
        if (qtdFaltas == null) {
            qtdFaltas = 0;
        }
        return qtdFaltas;
    }

    /**
     * @param qtdFaltas the qtdFaltas to set
     */
    public void setQtdFaltas(Integer qtdFaltas) {
        this.qtdFaltas = qtdFaltas;
    }

    public String getSituacaoAbonoFalta() {
        if (situacaoAbonoFalta ==  null) {
            situacaoAbonoFalta = "";
        }
        return situacaoAbonoFalta;
    }

    public void setSituacaoAbonoFalta(String situacaoAbonoFalta) {
        this.situacaoAbonoFalta = situacaoAbonoFalta;
    }

    public Boolean getAtividadeComplementar() {
        if (atividadeComplementar == null) {
            atividadeComplementar = Boolean.FALSE;
        }
        return atividadeComplementar;
    }

    public void setAtividadeComplementar(Boolean atividadeComplementar) {
        this.atividadeComplementar = atividadeComplementar;
    }

	public GradeDisciplinaVO getGradeDisciplinaVO() {
		if (gradeDisciplinaVO == null) {
			gradeDisciplinaVO = new  GradeDisciplinaVO();
		}
		return gradeDisciplinaVO;
	}

	public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
		this.gradeDisciplinaVO = gradeDisciplinaVO;
	}

	public Boolean getReplicarConteudoTodosDias() {
		if (replicarConteudoTodosDias == null) {
			replicarConteudoTodosDias = false;
		}
		return replicarConteudoTodosDias;
	}

	public void setReplicarConteudoTodosDias(Boolean replicarConteudoTodosDias) {
		this.replicarConteudoTodosDias = replicarConteudoTodosDias;
	}

	@XmlElement(name = "nrAula")
	public Integer getNrAula() {
		if (nrAula == null) {
			nrAula = 0;
		}
		return nrAula;
	}

	public void setNrAula(Integer nrAula) {
		this.nrAula = nrAula;
	}
	
    public Integer getTurmaOrigem() {
    	if(turmaOrigem == null){
    		turmaOrigem = 0;
    	}
		return turmaOrigem;
	}

	public void setTurmaOrigem(Integer turmaOrigem) {
		this.turmaOrigem = turmaOrigem;
	}

	public String getAnoSemestreApresentar() {
		return getAno() + (getSemestre().trim().isEmpty() ? "" : "/" + getSemestre());
	}

	public Date getDataOriginal() {
		if (dataOriginal == null) {
			dataOriginal = getData();
		}
		return dataOriginal;
	}

	public void setDataOriginal(Date dataOriginal) {
		this.dataOriginal = dataOriginal;
	}

	@XmlElement(name = "praticaSupervisionada")
	public String getPraticaSupervisionada() {
		if (praticaSupervisionada == null) {
			praticaSupervisionada = "";
		}
		return praticaSupervisionada;
	}

	public void setPraticaSupervisionada(String praticaSupervisionada) {
		this.praticaSupervisionada = praticaSupervisionada;
	}

	public Integer getQtdAulasMinistradas() {
		return qtdAulasMinistradas;
	}

	public void setQtdAulasMinistradas(Integer qtdAulasMinistradas) {
		this.qtdAulasMinistradas = qtdAulasMinistradas;
	}

	public Boolean getIsAulaNaoRegistrada() {
		if(isAulaNaoRegistrada == null ) {
			isAulaNaoRegistrada = Boolean.FALSE;
		}
		return isAulaNaoRegistrada;
	}

	public void setIsAulaNaoRegistrada(Boolean isAulaNaoRegistrada) {
		this.isAulaNaoRegistrada = isAulaNaoRegistrada;
	}
	
	public String getNivelEducacional() {
		if (nivelEducacional == null) {
			nivelEducacional = "";
		}
		return nivelEducacional;
	}

	public void setNivelEducacional(String nivelEducacional) {
		this.nivelEducacional = nivelEducacional;
	}	
			
}
