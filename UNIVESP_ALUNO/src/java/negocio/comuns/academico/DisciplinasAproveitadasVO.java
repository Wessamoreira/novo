package negocio.comuns.academico;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;

/**
 * Reponsável por manter os dados da entidade DisciplinasAproveitadas. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see
 */
@XmlAccessorType(XmlAccessType.NONE)
public class DisciplinasAproveitadasVO extends SuperVO {

    private Integer codigo;
    private String tipo;
    private DisciplinaVO disciplina;
    /**
     * Nome da disciplina cursada pelo aluno que será utilizada para aproveitar
     * outra disciplina da matriz.
     */
    private String nomeDisciplinaCursada;
    // TRANSIENTE
    private GradeDisciplinaVO gradeDisciplinaVO;
    // TRANSIENTE
    private GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplina;
    // TRANSIENTE para controle de Disciplinas Compostas
    private Boolean disciplinaComposta;
    /**
     * TRANSIENT e utilizadas para manter os aproveitamentos das disciplinas que fazem parte de uma
     * composicao. Quando faco um aproveitamento entre matriculas do aluno e a composicao é a mesma
     * na gradeOrigem e gradeDestino do aproveitamento, entao tambem levamos os historicos das disciplinas
     * que fazer parte da composicao. Isto é importante para alguns clientes que precisam emitir o boletim
     * com novas e frequencias das disciplinas que fazem parte da composicao;
     */
    private List<DisciplinasAproveitadasVO> disciplinasAproveitadasFazemParteComposicao;
    /**
     * TRANSIENT
     * quando o objeto DisciplinaAproveitaVO for referente a uma discplina que faz parte de uma composicao
     * iremos registrar neste atributo sobre qual disciplina da composicao tal objeto se refere. Isto
     * será importante na hora de gravar o aproveitamento e gerar o historico para este tipo de disciplina.
     */
    private GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO; 
    private Double nota;
    private Boolean utilizaNotaConceito;
    private String mediaFinalConceito;
    private Double frequencia;
    private Integer cargaHoraria;
    private Integer cargaHorariaCursada;
    private Integer aproveitamentoDisciplina;
    /**
     * TRANSIENT, UTILIZADO PARA PERSISTENCIA DA DISCIPLINAAPROVEITADASVO
     */
    private AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO;
    /**
     * TRANSIENT, UTILIZADO PARA PERSISTENCIA DA DISCIPLINAAPROVEITADASVO
     * TRATA-SE DO HISTÓRICO ATUAL DA DISCIPLINA. POIS UMA DISCIPLINA
     * (SEJA GRADEDISCIPLINA, GRUPO OPTATIVA E MAPA EQUIVALENCIA CURSADA)
     * PODE TER MAIS DE UM HISTÓRICO, MAS SEMPRE HAVERÁ UM QUE REPRESENTA
     * O ATUAL HISTÓRICO DA DISCIPLINA PARA O ALUNO.
     */
    private HistoricoVO historicoAtual;
    
    private String instituicao;
    private CidadeVO cidade;
    private String ano;
    private String semestre;
    private Boolean disciplinaForaGrade;
    private MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursada;
    private Integer periodoLetivoOrigemDisciplina;
    private boolean criarNovoHistorico = false;
    private boolean excluirHistoricoDisciplinaCursada = false;
    private PeriodoLetivoVO periodoletivoGrupoOptativaVO;
    public static final long serialVersionUID = 1L;
    private Boolean aproveitamentoPorIsencao;
    private Boolean realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento;
    // private DisciplinaVO disciplinaVO;
    // private GradeDisciplinaVO gradeDisciplinaVO;
    // private AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO;
    private Integer qtdeCreditoConcedido;
    
    // atributos utilizados quando o aproveitamento é proveniente de um
    // aproveitamento entre matriculas diferentes do aluno (matriculas que existem
    // dento do SEI) o aluno fez um curso e irá fazer um segundo curso, sendo que ambos
    // os cursos estao registrados dentro do SEI.    
    private String descricaoComplementacaoCH;
    // private String ano;
    // private String semestre;
    // private DisciplinaVO disciplinaVO;
    // private GradeDisciplinaVO gradeDisciplinaVO;
    // private AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO;
    private Integer qtdeCargaHorariaConcedido;
    private SituacaoHistorico situacaoHistorico;
    
    // atributos utilizados quando o aproveitamento é proveniente de um
    // aproveitamento entre matriculas diferentes do aluno (matriculas que existem
    // dento do SEI) o aluno fez um curso e irá fazer um segundo curso, sendo que ambos
    // os cursos estao registrados dentro do SEI.
    private MatriculaVO matriculaOrigemAproveitamentoEntreMatriculas;
    private String matriculaComNomeCursoMatriculaOrigemAproveitamento; // transient - utilizado somente para apresentar o nome do curso ao usuario em algumas telas
    private UsuarioVO responsavelAproveitamentoEntreMatriculas;
    private Date dataAproveitamentoEntreMatriculas;
    private String observacaoAproveitamentoEntreMatriculas;
    private AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO; // TRANSIENT
    private ConfiguracaoAcademicoVO configuracaoAcademicoVO; // TRANSIENT - UTILIZADO SOMENTE ATE A INCLUSAO DO HISTORICO
    private ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO; // TRANSIENT - UTILIZADO SOMENTE ATE A INCLUSAO DO HISTORICO 
    private Boolean apresentarAprovadoHistorico;
    private String nomeProfessor;
	private String titulacaoProfessor;
	private String sexoProfessor;
	private String situacaoDisciplinaMaeComposicao;
	private Date dataInicioAula;
	private Date dataFimAula;
	

    // private String descricaoComplementacaoCH;
    // private String ano;
    // private String semestre;
    /**
     * Construtor padrão da classe <code>DisciplinasAproveitadas</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public DisciplinasAproveitadasVO() {
        super();
        inicializarDados();
    }
    
    public static void validarDados(DisciplinasAproveitadasVO obj, String periodicidadeCurso) throws ConsistirException {
        validarDados(obj, periodicidadeCurso, 0);
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>DisciplinasAproveitadasVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     * 
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(DisciplinasAproveitadasVO obj, String periodicidadeCurso, Integer percMinimoCHDisciplinaAproveitada) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getDisciplina().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo DISCIPLINA (DisciplinasAproveitadas) deve ser informado.");
        }
        if (obj.getTipo().equals("AP")) {
            if (!obj.getAproveitamentoPorIsencao()) {
                if (obj.getUtilizaNotaConceito() && obj.getMediaFinalConceito() == "") {
                    throw new ConsistirException("O campo NOTA (DisciplinasAproveitadas) deve ser informado.");
                }
                if (obj.getNota().doubleValue() == 0 && !obj.getUtilizaNotaConceito()) {
                    throw new ConsistirException("O campo NOTA (DisciplinasAproveitadas) da Disciplina " + obj.getDisciplina().getNome().toUpperCase() + " deve ser informado.");
                }
                if ((obj.getFrequencia() == null) || (obj.getFrequencia().doubleValue() == 0)) {
                    throw new ConsistirException("O campo FREQUÊNCIA (DisciplinasAproveitadas) da Disciplina " + obj.getDisciplina().getNome().toUpperCase() + " deve ser informado.");
                }
            }
            
            if ((percMinimoCHDisciplinaAproveitada != null) && (percMinimoCHDisciplinaAproveitada.compareTo(0) > 0)) {
                Double percenutalCargaHorariaAproveitamento = ((obj.getCargaHorariaCursada().doubleValue() * 100) / obj.getCargaHoraria().doubleValue());
                if (percenutalCargaHorariaAproveitamento.compareTo(percMinimoCHDisciplinaAproveitada.doubleValue()) < 0) {
                    throw new ConsistirException("A carga horária da Disciplina que está sendo Aproveitada deve possuir pelo menos " + percMinimoCHDisciplinaAproveitada + "% da carga horária da disciplina da Matriz Curricular do Aluno - Disciplina: " + obj.getDisciplina().getNome().toUpperCase() + " - CH: " + obj.getCargaHoraria());
                }
            }
        }

        if ((periodicidadeCurso.equals("AN") || periodicidadeCurso.equals("SE"))) {
            if (obj.getAno().trim().equals("")) {
                throw new ConsistirException("O campo ANO  da Disciplina " + obj.getDisciplina().getNome().toUpperCase() + " deve ser informado.");
            }
            if (obj.getAno().trim().length() != 4) {
                throw new ConsistirException("O campo ANO da Disciplina " + obj.getDisciplina().getNome().toUpperCase() + " deve possuir 4 dígitos.");
            }
        }
        if ((!periodicidadeCurso.equals("AN") && !periodicidadeCurso.equals("SE"))) {
            obj.setAno("");
        }

        if ((periodicidadeCurso.equals("SE")) && obj.getSemestre().trim().equals("")) {
            throw new ConsistirException("O campo SEMESTRE da Disciplina " + obj.getDisciplina().getNome().toUpperCase() + " deve ser informado.");
        }
        if ((!periodicidadeCurso.equals("SE"))) {
            obj.setSemestre("");
        }
        // if (obj.getCargaHoraria().doubleValue() == 0) {
        // throw new
        // ConsistirException("O campo CARGA HORÁARIA (DisciplinasAproveitadas) deve ser informado.");
        // }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNota(0.0);
        setFrequencia(null);
        setAno(Uteis.getAnoDataAtual());
        setSemestre(Uteis.getSemestreAtual());
    }

    @XmlElement(name = "frequencia")
    public Double getFrequencia() {
        return (frequencia);
    }

    public void setFrequencia(Double frequencia) {
        this.frequencia = frequencia;
    }

    @XmlElement(name = "nota")
    public Double getNota() {
        return (nota);
    }

    public void setNota(Double nota) {
        this.nota = nota;
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

    @XmlTransient
    @ExcluirJsonAnnotation
    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @XmlTransient
    public String getCampoOrdenacao() {
        return getDisciplina().getNome();
    }

    /**
     * @return the aproveitamentoDisciplina
     */
    @XmlTransient
    public Integer getAproveitamentoDisciplina() {
        return aproveitamentoDisciplina;
    }

    /**
     * @param aproveitamentoDisciplina
     *            the aproveitamentoDisciplina to set
     */
    public void setAproveitamentoDisciplina(Integer aproveitamentoDisciplina) {
        this.aproveitamentoDisciplina = aproveitamentoDisciplina;
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

    @XmlTransient
    public boolean getCriarNovoHistorico() {
        return criarNovoHistorico;
    }

    public void setCriarNovoHistorico(boolean criarNovoHistorico) {
        this.criarNovoHistorico = criarNovoHistorico;
    }

    @XmlElement(name = "disciplinaForaGrade")
    public Boolean getDisciplinaForaGrade() {
        if (disciplinaForaGrade == null) {
            disciplinaForaGrade = Boolean.FALSE;
        }
        return disciplinaForaGrade;
    }

    public void setDisciplinaForaGrade(Boolean disciplinaForaGrade) {
        this.disciplinaForaGrade = disciplinaForaGrade;
    }

    @XmlTransient
    public Integer getPeriodoLetivoOrigemDisciplina() {
        if (periodoLetivoOrigemDisciplina == null) {
            periodoLetivoOrigemDisciplina = 0;
        }
        return periodoLetivoOrigemDisciplina;
    }

    public void setPeriodoLetivoOrigemDisciplina(Integer periodoLetivoOrigemDisciplina) {
        this.periodoLetivoOrigemDisciplina = periodoLetivoOrigemDisciplina;
    }

    @XmlElement(name = "instituicao")
    public String getInstituicao() {
        if (instituicao == null) {
            instituicao = "";
        }
        return instituicao;
    }

    public void setInstituicao(String instituicao) {
        this.instituicao = instituicao;
    }

    @XmlElement(name = "mediaFinalConceito")
    public String getMediaFinalConceito() {
        if (mediaFinalConceito == null) {
            mediaFinalConceito = "";
        }
        return mediaFinalConceito;
    }

    public void setMediaFinalConceito(String mediaFinalConceito) {
        this.mediaFinalConceito = mediaFinalConceito;
    }

    @XmlElement(name = "utilizaNotaConceito")
    public Boolean getUtilizaNotaConceito() {
        if (utilizaNotaConceito == null) {
            utilizaNotaConceito = false;
        }
        return utilizaNotaConceito;
    }

    public void setUtilizaNotaConceito(Boolean utilizaNotaConceito) {
        this.utilizaNotaConceito = utilizaNotaConceito;
    }

    @XmlElement(name = "cidade")
    public CidadeVO getCidade() {
        if (cidade == null) {
            cidade = new CidadeVO();
        }
        return cidade;
    }

    public void setCidade(CidadeVO cidade) {
        this.cidade = cidade;
    }

    @XmlElement(name = "cargaHoraria")
    public Integer getCargaHoraria() {
        if (cargaHoraria == null) {
            cargaHoraria = 0;
        }
        return cargaHoraria;
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    @XmlTransient
    @ExcluirJsonAnnotation
    public boolean isExcluirHistoricoDisciplinaCursada() {
        return excluirHistoricoDisciplinaCursada;
    }

    public void setExcluirHistoricoDisciplinaCursada(boolean excluirHistoricoDisciplinaCursada) {
        this.excluirHistoricoDisciplinaCursada = excluirHistoricoDisciplinaCursada;
    }

    @XmlElement(name = "cargaHorariaCursada")
    public Integer getCargaHorariaCursada() {
        if (cargaHorariaCursada == null) {
            cargaHorariaCursada = 0;
        }
        return cargaHorariaCursada;
    }

    public void setCargaHorariaCursada(Integer cargaHorariaCursada) {
        this.cargaHorariaCursada = cargaHorariaCursada;
    }

    @XmlTransient
    @ExcluirJsonAnnotation
    public GradeDisciplinaVO getGradeDisciplinaVO() {
        if (gradeDisciplinaVO == null) {
            gradeDisciplinaVO = new GradeDisciplinaVO();
        }
        return gradeDisciplinaVO;
    }

    public void setGradeDisciplinaVO(GradeDisciplinaVO gradeDisciplinaVO) {
        this.gradeDisciplinaVO = gradeDisciplinaVO;
    }

    @XmlTransient
    @ExcluirJsonAnnotation
    public Boolean getAproveitamentoPorIsencao() {
        if (aproveitamentoPorIsencao == null) {
            aproveitamentoPorIsencao = Boolean.FALSE;
        }
        return aproveitamentoPorIsencao;
    }

    public void setAproveitamentoPorIsencao(Boolean aproveitamentoPorIsencao) {
        this.aproveitamentoPorIsencao = aproveitamentoPorIsencao;
    }

    @XmlElement(name = "tipo")
    public String getTipo() {
        if (tipo == null) {
            tipo = "AP";
        }
        return tipo;
    }

    @XmlElement(name = "tipo_Apresentar")
    public String getTipo_Apresentar() {
        if (tipo == null) {
            tipo = "AP";
        }
        if (tipo.equals("AP")) {
            return "Aproveitamento de Disciplina";
        }
        if (tipo.equals("CO")) {
            return "Concessão de Crédito";
        }
        if (tipo.equals("CH")) {
            return "Concessão de Carga Horárias";
        }
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @XmlTransient
    @ExcluirJsonAnnotation
    public Integer getQtdeCargaHorariaConcedido() {
        if (qtdeCargaHorariaConcedido == null) {
            qtdeCargaHorariaConcedido = 0;
        }
        return qtdeCargaHorariaConcedido;
    }

    public void setQtdeCargaHorariaConcedido(Integer qtdeCargaHorariaConcedido) {
        this.qtdeCargaHorariaConcedido = qtdeCargaHorariaConcedido;
    }

    @XmlTransient
    @ExcluirJsonAnnotation
    public String getDescricaoComplementacaoCH() {
        if (descricaoComplementacaoCH == null) {
            descricaoComplementacaoCH = "";
        }
        return descricaoComplementacaoCH;
    }

    public void setDescricaoComplementacaoCH(String descricaoComplementacaoCH) {
        this.descricaoComplementacaoCH = descricaoComplementacaoCH;
    }

    @XmlTransient
    @ExcluirJsonAnnotation
    public Integer getQtdeCreditoConcedido() {
        if (qtdeCreditoConcedido == null) {
            qtdeCreditoConcedido = 0;
        }
        return qtdeCreditoConcedido;
    }

    public void setQtdeCreditoConcedido(Integer qtdeCreditoConcedido) {
        this.qtdeCreditoConcedido = qtdeCreditoConcedido;
    }

    @XmlTransient
    @ExcluirJsonAnnotation
    public PeriodoLetivoVO getPeriodoletivoGrupoOptativaVO() {
        if (periodoletivoGrupoOptativaVO == null) {
            periodoletivoGrupoOptativaVO = new PeriodoLetivoVO();
        }
        return periodoletivoGrupoOptativaVO;
    }

    public void setPeriodoletivoGrupoOptativaVO(PeriodoLetivoVO periodoLetivoVO) {
        periodoletivoGrupoOptativaVO = periodoLetivoVO;
    }

    /**
     * @return the mapaEquivalenciaDisciplinaCursada
     */
    @XmlTransient
    @ExcluirJsonAnnotation
    public MapaEquivalenciaDisciplinaCursadaVO getMapaEquivalenciaDisciplinaCursada() {
        if (mapaEquivalenciaDisciplinaCursada == null) {
            mapaEquivalenciaDisciplinaCursada = new MapaEquivalenciaDisciplinaCursadaVO();
        }
        return mapaEquivalenciaDisciplinaCursada;
    }

    /**
     * @param mapaEquivalenciaDisciplinaCursada the mapaEquivalenciaDisciplinaCursada to set
     */
    public void setMapaEquivalenciaDisciplinaCursada(MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursada) {
        this.mapaEquivalenciaDisciplinaCursada = mapaEquivalenciaDisciplinaCursada;
    }

    @XmlTransient
    @ExcluirJsonAnnotation
    public GradeCurricularGrupoOptativaDisciplinaVO getGradeCurricularGrupoOptativaDisciplina() {
        if (gradeCurricularGrupoOptativaDisciplina == null) {
            gradeCurricularGrupoOptativaDisciplina = new GradeCurricularGrupoOptativaDisciplinaVO();
        }
        return gradeCurricularGrupoOptativaDisciplina;
    }

    /**
     * @param gradeCurricularGrupoOptativaDisciplina the gradeCurricularGrupoOptativaDisciplina to set
     */
    public void setGradeCurricularGrupoOptativaDisciplina(GradeCurricularGrupoOptativaDisciplinaVO gradeCurricularGrupoOptativaDisciplina) {
        this.gradeCurricularGrupoOptativaDisciplina = gradeCurricularGrupoOptativaDisciplina;
    }

    /**
     * @return the aproveitamentoDisciplinaVO
     */
    @XmlTransient
    @ExcluirJsonAnnotation
    public AproveitamentoDisciplinaVO getAproveitamentoDisciplinaVO() {
        if (aproveitamentoDisciplinaVO == null) {
            aproveitamentoDisciplinaVO = new AproveitamentoDisciplinaVO();
        }
        return aproveitamentoDisciplinaVO;
    }

    /**
     * @param aproveitamentoDisciplinaVO the aproveitamentoDisciplinaVO to set
     */
    public void setAproveitamentoDisciplinaVO(AproveitamentoDisciplinaVO aproveitamentoDisciplinaVO) {
        this.aproveitamentoDisciplinaVO = aproveitamentoDisciplinaVO;
    }

    /**
     * @return the nomeDisciplinaCursada
     */
    @XmlElement(name = "nomeDisciplinaCursada")
    public String getNomeDisciplinaCursada() {
        if (nomeDisciplinaCursada == null) {
            nomeDisciplinaCursada = "";
        }
        return nomeDisciplinaCursada;
    }

    /**
     * @param nomeDisciplinaCursada the nomeDisciplinaCursada to set
     */
    public void setNomeDisciplinaCursada(String nomeDisciplinaCursada) {
        this.nomeDisciplinaCursada = nomeDisciplinaCursada;
    }

    /**
     * @return the historicoAtual
     */
    @ExcluirJsonAnnotation
    @Transient
    public HistoricoVO getHistoricoAtual() {
        if (historicoAtual == null) {
            historicoAtual = new HistoricoVO();
        }
        return historicoAtual;
    }

    /**
     * @param historicoAtual the historicoAtual to set
     */
    public void setHistoricoAtual(HistoricoVO historicoAtual) {
        this.historicoAtual = historicoAtual;
    }
    
    public static void validarAnoSemestreAproveitamentoDisciplina(DisciplinasAproveitadasVO disciplinasAproveitadasVO, HistoricoVO historicoAtualAluno) throws Exception {
        if (historicoAtualAluno.getReprovado() || historicoAtualAluno.getTrancado()) {
            // SE ENTRAR AQUI É POR QUE JÁ EXISTE UM HISTÓRICO NO PASSADO REGISTRADO PARA ESTE ALUNO.
            // SEJA DE REPROVAÇÃO OU DE UM TRANCAMENTO REALIZADO. PORTANTO, UM APROVEITAMENTO NÃO PODE
            // SER REGISTRARO PARA ANO/SEMESTRE ANTERIOR A ESTE HISTÓRICO JÁ EXISTENTE. POIS, QUEBRARIA
            // A ORDEM CRONOLÓGICA DE REGISTROS DE HISTÓRICOS.
            String anoSemestreHistorico = historicoAtualAluno.getAnoHistorico() + "/" + historicoAtualAluno.getSemestreHistorico();
            String anoSemestreAproveitamento = disciplinasAproveitadasVO.getAno() + "/" + disciplinasAproveitadasVO.getSemestre();
            if (anoSemestreAproveitamento.compareTo(anoSemestreHistorico) <= 0) {
                // ou seja o ano e semestre do aproveitamento precisa ser maior que o anoSemestre do Historico.
                // Caso contrário temos que gerar um exception impedindo o registro do histórico.
                throw new Exception("Este aluno já possui um HISTÓRICO com situação final registrado em Ano/Semestre Anterior (No caso, histórico já registrado em: " + anoSemestreHistorico + " - Situação: " + historicoAtualAluno.getSituacao_Apresentar()+ "). Logo o Aproveitamento só poderá ser registrado em Ano/Semestre posterior, para que a Ordem Cronológica de Históricos seja respeitada.");
            }
        }
    }

    @ExcluirJsonAnnotation
    @Transient
	public SituacaoHistorico getSituacaoHistorico() {
		if(situacaoHistorico == null){
			if(getTipo().equals("AP")){
				situacaoHistorico = SituacaoHistorico.APROVADO_APROVEITAMENTO;				
				if(getApresentarAprovadoHistorico()){
					situacaoHistorico = SituacaoHistorico.APROVADO;
				}
			}else if(getTipo().equals("CO")){
				situacaoHistorico = SituacaoHistorico.CONCESSAO_CREDITO;
			}else{
				situacaoHistorico = SituacaoHistorico.CONCESSAO_CREDITO;				
			}
		}
		return situacaoHistorico;
	}
	
    @Transient
    @ExcluirJsonAnnotation
	public String getSituacaoHistorico_Apresentar() {
		return SituacaoHistorico.getDescricao(situacaoHistorico.getValor());
	}

	public void setSituacaoHistorico(SituacaoHistorico situacaoHistorico) {
		this.situacaoHistorico = situacaoHistorico;
	}

	@Transient
	@ExcluirJsonAnnotation
	public MatriculaVO getMatriculaOrigemAproveitamentoEntreMatriculas() {
		if (matriculaOrigemAproveitamentoEntreMatriculas == null) {
			matriculaOrigemAproveitamentoEntreMatriculas = new MatriculaVO();
		}
		return matriculaOrigemAproveitamentoEntreMatriculas;
	}

	public void setMatriculaOrigemAproveitamentoEntreMatriculas(MatriculaVO matriculaOrigemAproveitamentoEntreMatriculas) {
		this.matriculaOrigemAproveitamentoEntreMatriculas = matriculaOrigemAproveitamentoEntreMatriculas;
	}

	@Transient
	@ExcluirJsonAnnotation
	public UsuarioVO getResponsavelAproveitamentoEntreMatriculas() {
		if (responsavelAproveitamentoEntreMatriculas == null) {
			responsavelAproveitamentoEntreMatriculas = new UsuarioVO();
		}
		return responsavelAproveitamentoEntreMatriculas;
	}

	public void setResponsavelAproveitamentoEntreMatriculas(UsuarioVO responsavelAproveitamentoEntreMatriculas) {
		this.responsavelAproveitamentoEntreMatriculas = responsavelAproveitamentoEntreMatriculas;
	}

	@Transient
	@ExcluirJsonAnnotation
	public Date getDataAproveitamentoEntreMatriculas() {
		if (dataAproveitamentoEntreMatriculas == null) {
			dataAproveitamentoEntreMatriculas = new Date();
		}
		return dataAproveitamentoEntreMatriculas;
	}

	public void setDataAproveitamentoEntreMatriculas(Date dataAproveitamentoEntreMatriculas) {
		this.dataAproveitamentoEntreMatriculas = dataAproveitamentoEntreMatriculas;
	}

	@XmlTransient
	@ExcluirJsonAnnotation
	public String getObservacaoAproveitamentoEntreMatriculas() {
		if (observacaoAproveitamentoEntreMatriculas == null) {
			observacaoAproveitamentoEntreMatriculas = "";
		}
		return observacaoAproveitamentoEntreMatriculas;
	}

	public void setObservacaoAproveitamentoEntreMatriculas(String observacaoAproveitamentoEntreMatriculas) {
		this.observacaoAproveitamentoEntreMatriculas = observacaoAproveitamentoEntreMatriculas;
	}

	@XmlTransient
	@ExcluirJsonAnnotation
	public AproveitamentoDisciplinasEntreMatriculasVO getAproveitamentoDisciplinasEntreMatriculasVO() {
		if (aproveitamentoDisciplinasEntreMatriculasVO == null) {
			aproveitamentoDisciplinasEntreMatriculasVO = new AproveitamentoDisciplinasEntreMatriculasVO();
		}
		return aproveitamentoDisciplinasEntreMatriculasVO;
	}

	public void setAproveitamentoDisciplinasEntreMatriculasVO(AproveitamentoDisciplinasEntreMatriculasVO aproveitamentoDisciplinasEntreMatriculasVO) {
		this.aproveitamentoDisciplinasEntreMatriculasVO = aproveitamentoDisciplinasEntreMatriculasVO;
	}

	@XmlTransient
	@ExcluirJsonAnnotation
	public ConfiguracaoAcademicoNotaConceitoVO getConfiguracaoAcademicoNotaConceitoVO() {
		if (configuracaoAcademicoNotaConceitoVO == null) {
			configuracaoAcademicoNotaConceitoVO = new ConfiguracaoAcademicoNotaConceitoVO();
		}
		return configuracaoAcademicoNotaConceitoVO;
	}

	public void setConfiguracaoAcademicoNotaConceitoVO(ConfiguracaoAcademicoNotaConceitoVO configuracaoAcademicoNotaConceitoVO) {
		this.configuracaoAcademicoNotaConceitoVO = configuracaoAcademicoNotaConceitoVO;
	}

	@XmlTransient
	@ExcluirJsonAnnotation
	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if (configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}

	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
	}

	@XmlTransient
	@ExcluirJsonAnnotation
	public Boolean getRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento() {
		if (realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento == null) {
			realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento = Boolean.TRUE;
		}
		return realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento;
	}

	public void setRealizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento(Boolean realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento) {
		this.realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento = realizarTransferenciaDisclinaAprovadaComoAprovadaAproveitamento;
	}

	@ExcluirJsonAnnotation
	@XmlTransient
	public String getMatriculaComNomeCursoMatriculaOrigemAproveitamento() {
		if (matriculaComNomeCursoMatriculaOrigemAproveitamento == null) {
			matriculaComNomeCursoMatriculaOrigemAproveitamento = "";
		}
		return matriculaComNomeCursoMatriculaOrigemAproveitamento;
	}

	public void setMatriculaComNomeCursoMatriculaOrigemAproveitamento(String matriculaComNomeCursoMatriculaOrigemAproveitamento) {
		this.matriculaComNomeCursoMatriculaOrigemAproveitamento = matriculaComNomeCursoMatriculaOrigemAproveitamento;
	}

	@ExcluirJsonAnnotation
	@XmlTransient
	public boolean getReferenteAproveitamentoOutraDisciplina() {
		if (!this.getMatriculaOrigemAproveitamentoEntreMatriculas().getMatricula().equals("")) {
			return true;
		}
		return false;
	}

	@ExcluirJsonAnnotation
	@XmlTransient
	public Boolean getDisciplinaComposta() {
		if (disciplinaComposta == null) {
			disciplinaComposta = Boolean.FALSE;
		}
		return disciplinaComposta;
	}

	public void setDisciplinaComposta(Boolean disciplinaComposta) {
		this.disciplinaComposta = disciplinaComposta;
	}

	@ExcluirJsonAnnotation
	@XmlTransient	
	public List<DisciplinasAproveitadasVO> getDisciplinasAproveitadasFazemParteComposicao() {
		if (disciplinasAproveitadasFazemParteComposicao == null) {
			disciplinasAproveitadasFazemParteComposicao = new ArrayList<DisciplinasAproveitadasVO>(0);
		}
		return disciplinasAproveitadasFazemParteComposicao;
	}

	public void setDisciplinasAproveitadasFazemParteComposicao(List<DisciplinasAproveitadasVO> disciplinasAproveitadasFazemParteComposicao) {
		this.disciplinasAproveitadasFazemParteComposicao = disciplinasAproveitadasFazemParteComposicao;
	}

	@ExcluirJsonAnnotation
	@XmlTransient
	public GradeDisciplinaCompostaVO getGradeDisciplinaCompostaVO() {
		if (gradeDisciplinaCompostaVO == null) {
			gradeDisciplinaCompostaVO = new GradeDisciplinaCompostaVO();
		}
		return gradeDisciplinaCompostaVO;
	}

	public void setGradeDisciplinaCompostaVO(GradeDisciplinaCompostaVO gradeDisciplinaCompostaVO) {
		this.gradeDisciplinaCompostaVO = gradeDisciplinaCompostaVO;
	}

	@ExcluirJsonAnnotation
	@XmlTransient
	public Boolean getApresentarAprovadoHistorico() {
		if(apresentarAprovadoHistorico == null){
			apresentarAprovadoHistorico = false;
		}
		return apresentarAprovadoHistorico;
	}

	public void setApresentarAprovadoHistorico(Boolean apresentarAprovadoHistorico) {
		this.apresentarAprovadoHistorico = apresentarAprovadoHistorico;
	}
	
	public void definirTipoAproveitamentoDisciplina(){
		setApresentarAprovadoHistorico(getSituacaoHistorico().equals(SituacaoHistorico.APROVADO));			
		if(getSituacaoHistorico().equals(SituacaoHistorico.APROVADO) 
				|| getSituacaoHistorico().equals(SituacaoHistorico.APROVADO_APROVEITAMENTO)  
				|| getSituacaoHistorico().equals(SituacaoHistorico.APROVEITAMENTO_BANCA)){
			setTipo("AP");
		}else if(getSituacaoHistorico().equals(SituacaoHistorico.CONCESSAO_CREDITO)){
			setTipo("CO");
		}else if(getSituacaoHistorico().equals(SituacaoHistorico.CONCESSAO_CARGA_HORARIA)){
			setTipo("CH");
		}
	}

	@XmlElement(name = "nomeProfessor")
	public String getNomeProfessor() {
		if (nomeProfessor == null) {
			nomeProfessor = "";
		}
		return nomeProfessor;
	}

	public void setNomeProfessor(String nomeProfessor) {
		this.nomeProfessor = nomeProfessor;
	}

	@XmlElement(name = "titulacaoProfessor")
	public String getTitulacaoProfessor() {
		if (titulacaoProfessor == null) {
			titulacaoProfessor = "";
		}
		return titulacaoProfessor;
	}

	public void setTitulacaoProfessor(String titulacaoProfessor) {
		this.titulacaoProfessor = titulacaoProfessor;
	}
	
	@XmlElement(name = "sexoProfessor")
	public String getSexoProfessor() {
		if (sexoProfessor == null) {
			sexoProfessor = "";
		}
		return sexoProfessor;
	}

	public void setSexoProfessor(String sexoProfessor) {
		this.sexoProfessor = sexoProfessor;
	}

	@ExcluirJsonAnnotation
	@XmlTransient
	public String getSituacaoDisciplinaMaeComposicao() {
		if (situacaoDisciplinaMaeComposicao == null) {
			situacaoDisciplinaMaeComposicao = "";
		}
		return situacaoDisciplinaMaeComposicao;
	}

	public void setSituacaoDisciplinaMaeComposicao(String situacaoDisciplinaMaeComposicao) {
		this.situacaoDisciplinaMaeComposicao = situacaoDisciplinaMaeComposicao;
	}
	
	public static void validarAnoSemestreAproveitamentoDisciplinaMaeComposicao(DisciplinasAproveitadasVO disciplinasAproveitadasVO, HistoricoVO historicoAtualAluno) throws Exception {
        if (historicoAtualAluno.getReprovado() || historicoAtualAluno.getTrancado()) {
            String anoSemestreHistorico = historicoAtualAluno.getAnoHistorico() + "/" + historicoAtualAluno.getSemestreHistorico();
            String anoSemestreAproveitamento = disciplinasAproveitadasVO.getAno() + "/" + disciplinasAproveitadasVO.getSemestre();
            if (anoSemestreAproveitamento.compareTo(anoSemestreHistorico) < 0) {
                throw new Exception("Este aluno já possui um HISTÓRICO com situação final registrado em Ano/Semestre Anterior (No caso, histórico já registrado em: " + anoSemestreHistorico + " - Situação: " + historicoAtualAluno.getSituacao_Apresentar()+ "). Logo o Aproveitamento só poderá ser registrado em Ano/Semestre posterior, para que a Ordem Cronológica de Históricos seja respeitada.");
            }
        }
    }

	public Date getDataInicioAula() {
		return dataInicioAula;
	}

	public void setDataInicioAula(Date dataInicioAula) {
		this.dataInicioAula = dataInicioAula;
	}

	public Date getDataFimAula() {
		return dataFimAula;
	}

	public void setDataFimAula(Date dataFimAula) {
		this.dataFimAula = dataFimAula;
	}
	
	
}
