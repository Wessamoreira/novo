package negocio.comuns.extensao;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade CursoExtensao. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class CursoExtensaoVO extends SuperVO {

    private Integer codigo;
    private String nome;
    private Integer cargaHoraria;
    private Date dataInicial;
    private Date dataFinal;
    private String ementa;
    private String conteudoProgramatico;
    private String situacao;
    private String situacaoFinanceira;
    private String local;
    private String horario;
    private Date dataInicialInscricao;
    private Date dataFinalInscricao;
    private Double valorComunidade;
    private Double valorAluno;
    private Double valorFuncionario;
    private Double valorProfessor;
    private Double valorInscricaoComunidade;
    private Double valorInscricaoAluno;
    private Double valorInscricaoFuncionario;
    private Double valorInscricaoProfessor;
    private Boolean cobrarInscricao;
    private Boolean inscricaoPelaInternet;
    private Boolean confirmacaoPresencial;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>ProfessorCursoExtensao</code>.
     */
    private List professorCursoExtensaoVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ClassificaoCursoExtensao </code>.
     */
    private ClassificaoCursoExtensaoVO classificacaoCursoExtensao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>CursoExtensao</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public CursoExtensaoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>CursoExtensaoVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(CursoExtensaoVO obj) throws ConsistirException {
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Curso de Extensão) deve ser informado.");
        }
        if (obj.getCargaHoraria().intValue() == 0) {
            throw new ConsistirException("O campo CARGA HORÁRIA (Curso de Extensão) deve ser informado.");
        }
        if (obj.getDataInicial() == null) {
            throw new ConsistirException("O campo DATA INICIAL (Curso de Extensão) deve ser informado.");
        }
        if (obj.getDataFinal() == null) {
            throw new ConsistirException("O campo DATA FINAL (Curso de Extensão) deve ser informado.");
        }
        if (obj.getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Curso de Extensão) deve ser informado.");
        }
        if (obj.getSituacaoFinanceira().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO FINANCEIRA (Curso de Extensão) deve ser informado.");
        }
        if ((obj.getClassificacaoCursoExtensao() == null)
                || (obj.getClassificacaoCursoExtensao().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CLASSIFICAÇÃO CURSO EXTENSÃO (Curso de Extensão) deve ser informado.");
        }
        if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo UNIDADE ENSINO (Curso de Extensão) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNome("");
        setCargaHoraria(0);
        setDataInicial(new Date());
        setDataFinal(new Date());
        setEmenta("");
        setConteudoProgramatico("");
        setSituacao("");
        setSituacaoFinanceira("");
        setLocal("");
        setHorario("");
        setDataInicialInscricao(new Date());
        setDataFinalInscricao(new Date());
        setValorComunidade(0.0);
        setValorAluno(0.0);
        setValorFuncionario(0.0);
        setValorProfessor(0.0);
        setValorInscricaoComunidade(0.0);
        setValorInscricaoAluno(0.0);
        setValorInscricaoFuncionario(0.0);
        setValorInscricaoProfessor(0.0);
        setCobrarInscricao(Boolean.FALSE);
        setInscricaoPelaInternet(Boolean.FALSE);
        setConfirmacaoPresencial(Boolean.FALSE);
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>ProfessorCursoExtensaoVO</code> ao List
     * <code>professorCursoExtensaoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ProfessorCursoExtensao</code> -
     * getPessoaProfessorCursoExtensao().getCodigo() - como identificador (key)
     * do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>ProfessorCursoExtensaoVO</code> que
     *            será adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjProfessorCursoExtensaoVOs(ProfessorCursoExtensaoVO obj) throws Exception {
        ProfessorCursoExtensaoVO.validarDados(obj);
        int index = 0;
        Iterator i = getProfessorCursoExtensaoVOs().iterator();
        while (i.hasNext()) {
            ProfessorCursoExtensaoVO objExistente = (ProfessorCursoExtensaoVO) i.next();
            if (objExistente.getPessoaProfessorCursoExtensao().getCodigo().equals(obj.getPessoaProfessorCursoExtensao().getCodigo())) {
                getProfessorCursoExtensaoVOs().set(index, obj);
                return;
            }
            index++;
        }
        getProfessorCursoExtensaoVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>ProfessorCursoExtensaoVO</code> no List
     * <code>professorCursoExtensaoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ProfessorCursoExtensao</code> -
     * getPessoaProfessorCursoExtensao().getCodigo() - como identificador (key)
     * do objeto no List.
     *
     * @param pessoaProfessorCursoExtensao
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjProfessorCursoExtensaoVOs(Integer pessoaProfessorCursoExtensao) throws Exception {
        int index = 0;
        Iterator i = getProfessorCursoExtensaoVOs().iterator();
        while (i.hasNext()) {
            ProfessorCursoExtensaoVO objExistente = (ProfessorCursoExtensaoVO) i.next();
            if (objExistente.getPessoaProfessorCursoExtensao().getCodigo().equals(pessoaProfessorCursoExtensao)) {
                getProfessorCursoExtensaoVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>ProfessorCursoExtensaoVO</code> no List
     * <code>professorCursoExtensaoVOs</code>. Utiliza o atributo padrão de
     * consulta da classe <code>ProfessorCursoExtensao</code> -
     * getPessoaProfessorCursoExtensao().getCodigo() - como identificador (key)
     * do objeto no List.
     *
     * @param pessoaProfessorCursoExtensao
     *            Parâmetro para localizar o objeto do List.
     */
    public ProfessorCursoExtensaoVO consultarObjProfessorCursoExtensaoVO(Integer pessoaProfessorCursoExtensao) throws Exception {
        Iterator i = getProfessorCursoExtensaoVOs().iterator();
        while (i.hasNext()) {
            ProfessorCursoExtensaoVO objExistente = (ProfessorCursoExtensaoVO) i.next();
            if (objExistente.getPessoaProfessorCursoExtensao().getCodigo().equals(pessoaProfessorCursoExtensao)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>CursoExtensao</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>CursoExtensao</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    /**
     * Retorna o objeto da classe <code>ClassificaoCursoExtensao</code>
     * relacionado com (<code>CursoExtensao</code>).
     */
    public ClassificaoCursoExtensaoVO getClassificacaoCursoExtensao() {
        if (classificacaoCursoExtensao == null) {
            classificacaoCursoExtensao = new ClassificaoCursoExtensaoVO();
        }
        return (classificacaoCursoExtensao);
    }

    /**
     * Define o objeto da classe <code>ClassificaoCursoExtensao</code>
     * relacionado com (<code>CursoExtensao</code>).
     */
    public void setClassificacaoCursoExtensao(ClassificaoCursoExtensaoVO obj) {
        this.classificacaoCursoExtensao = obj;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ProfessorCursoExtensao</code>.
     */
    public List getProfessorCursoExtensaoVOs() {
        if (professorCursoExtensaoVOs == null) {
            professorCursoExtensaoVOs = new ArrayList();
        }
        return (professorCursoExtensaoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>ProfessorCursoExtensao</code>.
     */
    public void setProfessorCursoExtensaoVOs(List professorCursoExtensaoVOs) {
        this.professorCursoExtensaoVOs = professorCursoExtensaoVOs;
    }

    public Boolean getConfirmacaoPresencial() {
        return (confirmacaoPresencial);
    }

    public Boolean isConfirmacaoPresencial() {
        return (confirmacaoPresencial);
    }

    public void setConfirmacaoPresencial(Boolean confirmacaoPresencial) {
        this.confirmacaoPresencial = confirmacaoPresencial;
    }

    public Boolean getInscricaoPelaInternet() {
        return (inscricaoPelaInternet);
    }

    public Boolean isInscricaoPelaInternet() {
        return (inscricaoPelaInternet);
    }

    public void setInscricaoPelaInternet(Boolean inscricaoPelaInternet) {
        this.inscricaoPelaInternet = inscricaoPelaInternet;
    }

    public Boolean getCobrarInscricao() {
        return (cobrarInscricao);
    }

    public Boolean isCobrarInscricao() {
        return (cobrarInscricao);
    }

    public void setCobrarInscricao(Boolean cobrarInscricao) {
        this.cobrarInscricao = cobrarInscricao;
    }

    public Double getValorInscricaoProfessor() {
        return (valorInscricaoProfessor);
    }

    public void setValorInscricaoProfessor(Double valorInscricaoProfessor) {
        this.valorInscricaoProfessor = valorInscricaoProfessor;
    }

    public Double getValorInscricaoFuncionario() {
        return (valorInscricaoFuncionario);
    }

    public void setValorInscricaoFuncionario(Double valorInscricaoFuncionario) {
        this.valorInscricaoFuncionario = valorInscricaoFuncionario;
    }

    public Double getValorInscricaoAluno() {
        return (valorInscricaoAluno);
    }

    public void setValorInscricaoAluno(Double valorInscricaoAluno) {
        this.valorInscricaoAluno = valorInscricaoAluno;
    }

    public Double getValorInscricaoComunidade() {
        return (valorInscricaoComunidade);
    }

    public void setValorInscricaoComunidade(Double valorInscricaoComunidade) {
        this.valorInscricaoComunidade = valorInscricaoComunidade;
    }

    public Double getValorProfessor() {
        return (valorProfessor);
    }

    public void setValorProfessor(Double valorProfessor) {
        this.valorProfessor = valorProfessor;
    }

    public Double getValorFuncionario() {
        return (valorFuncionario);
    }

    public void setValorFuncionario(Double valorFuncionario) {
        this.valorFuncionario = valorFuncionario;
    }

    public Double getValorAluno() {
        return (valorAluno);
    }

    public void setValorAluno(Double valorAluno) {
        this.valorAluno = valorAluno;
    }

    public Double getValorComunidade() {
        return (valorComunidade);
    }

    public void setValorComunidade(Double valorComunidade) {
        this.valorComunidade = valorComunidade;
    }

    public Date getDataFinalInscricao() {
        return (dataFinalInscricao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFinalInscricao_Apresentar() {
        return (Uteis.getData(dataFinalInscricao));
    }

    public void setDataFinalInscricao(Date dataFinalInscricao) {
        this.dataFinalInscricao = dataFinalInscricao;
    }

    public Date getDataInicialInscricao() {
        return (dataInicialInscricao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataInicialInscricao_Apresentar() {
        return (Uteis.getData(dataInicialInscricao));
    }

    public void setDataInicialInscricao(Date dataInicialInscricao) {
        this.dataInicialInscricao = dataInicialInscricao;
    }

    public String getHorario() {
        return (horario);
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getLocal() {
        return (local);
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getSituacaoFinanceira() {
        return (situacaoFinanceira);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getSituacaoFinanceira_Apresentar() {
        if (situacaoFinanceira.equals("MA")) {
            return "Matrícula em Aberto";
        }
        if (situacaoFinanceira.equals("IA")) {
            return "Inscrição em Aberto";
        }
        if (situacaoFinanceira.equals("MQ")) {
            return "Matrícula Quitada";
        }
        if (situacaoFinanceira.equals("IQ")) {
            return "Inscrição Quitada";
        }
        return (situacaoFinanceira);
    }

    public void setSituacaoFinanceira(String situacaoFinanceira) {
        this.situacaoFinanceira = situacaoFinanceira;
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
        if (situacao.equals("MA")) {
            return "Matrícula";
        }
        if (situacao.equals("IN")) {
            return "Inscrito";
        }
        return (situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getConteudoProgramatico() {
        return (conteudoProgramatico);
    }

    public void setConteudoProgramatico(String conteudoProgramatico) {
        this.conteudoProgramatico = conteudoProgramatico;
    }

    public String getEmenta() {
        return (ementa);
    }

    public void setEmenta(String ementa) {
        this.ementa = ementa;
    }

    public Date getDataFinal() {
        return (dataFinal);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFinal_Apresentar() {
        return (Uteis.getData(dataFinal));
    }

    public void setDataFinal(Date dataFinal) {
        this.dataFinal = dataFinal;
    }

    public Date getDataInicial() {
        return (dataInicial);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataInicial_Apresentar() {
        return (Uteis.getData(dataInicial));
    }

    public void setDataInicial(Date dataInicial) {
        this.dataInicial = dataInicial;
    }

    public Integer getCargaHoraria() {
        return (cargaHoraria);
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public String getNome() {
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
