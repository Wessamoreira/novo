package negocio.comuns.pesquisa;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade PublicacaoPesquisa. Classe do tipo
 * VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class PublicacaoPesquisaVO extends SuperVO {

    private Integer codigo;
    private String tituloPublicacao;
    private String eventoRevistaPublicacao;
    private String localPublicacao;
    private Integer anoPublicacao;
    private String semestrePublicacao;
    private String complementoPublicacao;
    private String tipoPesquisador;
    private String tipoPublicacao;
    private String palavraChave;
    private String resumo;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO autorProfessor;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Matricula </code>.
     */
    private MatriculaVO autorAluno;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO orientador;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Curso </code>.
     */
    private CursoVO curso;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ProjetoPesquisa </code>.
     */
    private ProjetoPesquisaVO projetoPesquisa;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>LinhaPesquisa </code>.
     */
    private LinhaPesquisaVO linhaPesquisa;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>PublicacaoPesquisa</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public PublicacaoPesquisaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PublicacaoPesquisaVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PublicacaoPesquisaVO obj) throws ConsistirException {
        if (obj.getTituloPublicacao().equals("")) {
            throw new ConsistirException("O campo TÍTULO PUBLICAÇÃO (Publicação Pesquisa) deve ser informado.");
        }
        if (obj.getTipoPesquisador().equals("")) {
            throw new ConsistirException("O campo TIPO PESQUISADOR (Publicação Pesquisa) deve ser informado.");
        }
        if (obj.getTipoPublicacao().equals("")) {
            throw new ConsistirException("O campo TIPO PUBLICAÇÃO (Publicação Pesquisa) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setTituloPublicacao("");
        setEventoRevistaPublicacao("");
        setLocalPublicacao("");
        setAnoPublicacao(0);
        setSemestrePublicacao("");
        setComplementoPublicacao("");
        setTipoPesquisador("");
        setTipoPublicacao("");
        setPalavraChave("");
        setResumo("");
    }

    /**
     * Retorna o objeto da classe <code>LinhaPesquisa</code> relacionado com (
     * <code>PublicacaoPesquisa</code>).
     */
    public LinhaPesquisaVO getLinhaPesquisa() {
        if (linhaPesquisa == null) {
            linhaPesquisa = new LinhaPesquisaVO();
        }
        return (linhaPesquisa);
    }

    /**
     * Define o objeto da classe <code>LinhaPesquisa</code> relacionado com (
     * <code>PublicacaoPesquisa</code>).
     */
    public void setLinhaPesquisa(LinhaPesquisaVO obj) {
        this.linhaPesquisa = obj;
    }

    /**
     * Retorna o objeto da classe <code>ProjetoPesquisa</code> relacionado com (
     * <code>PublicacaoPesquisa</code>).
     */
    public ProjetoPesquisaVO getProjetoPesquisa() {
        if (projetoPesquisa == null) {
            projetoPesquisa = new ProjetoPesquisaVO();
        }
        return (projetoPesquisa);
    }

    /**
     * Define o objeto da classe <code>ProjetoPesquisa</code> relacionado com (
     * <code>PublicacaoPesquisa</code>).
     */
    public void setProjetoPesquisa(ProjetoPesquisaVO obj) {
        this.projetoPesquisa = obj;
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>PublicacaoPesquisa</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsinso() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>PublicacaoPesquisa</code>).
     */
    public void setUnidadeEnsinso(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (
     * <code>PublicacaoPesquisa</code>).
     */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }

    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (
     * <code>PublicacaoPesquisa</code>).
     */
    public void setCurso(CursoVO obj) {
        this.curso = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PublicacaoPesquisa</code>).
     */
    public PessoaVO getOrientador() {
        if (orientador == null) {
            orientador = new PessoaVO();
        }
        return (orientador);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PublicacaoPesquisa</code>).
     */
    public void setOrientador(PessoaVO obj) {
        this.orientador = obj;
    }

    /**
     * Retorna o objeto da classe <code>Matricula</code> relacionado com (
     * <code>PublicacaoPesquisa</code>).
     */
    public MatriculaVO getAutorAluno() {
        if (autorAluno == null) {
            autorAluno = new MatriculaVO();
        }
        return (autorAluno);
    }

    /**
     * Define o objeto da classe <code>Matricula</code> relacionado com (
     * <code>PublicacaoPesquisa</code>).
     */
    public void setAutorAluno(MatriculaVO obj) {
        this.autorAluno = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PublicacaoPesquisa</code>).
     */
    public PessoaVO getAutorProfessor() {
        if (autorProfessor == null) {
            autorProfessor = new PessoaVO();
        }
        return (autorProfessor);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PublicacaoPesquisa</code>).
     */
    public void setAutorProfessor(PessoaVO obj) {
        this.autorProfessor = obj;
    }

    public String getResumo() {
        return (resumo);
    }

    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

    public String getPalavraChave() {
        return (palavraChave);
    }

    public void setPalavraChave(String palavraChave) {
        this.palavraChave = palavraChave;
    }

    public String getTipoPublicacao() {
        return (tipoPublicacao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoPublicacao_Apresentar() {
        if (tipoPublicacao.equals("LI")) {
            return "Livro";
        }
        if (tipoPublicacao.equals("CL")) {
            return "Capítulo Livro";
        }
        if (tipoPublicacao.equals("AR")) {
            return "Artigo";
        }
        if (tipoPublicacao.equals("MO")) {
            return "Monografia";
        }
        return (tipoPublicacao);
    }

    public void setTipoPublicacao(String tipoPublicacao) {
        this.tipoPublicacao = tipoPublicacao;
    }

    public String getTipoPesquisador() {
        return (tipoPesquisador);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoPesquisador_Apresentar() {
        if (tipoPesquisador.equals("PR")) {
            return "Professor";
        }
        if (tipoPesquisador.equals("AL")) {
            return "Aluno";
        }
        return (tipoPesquisador);
    }

    public void setTipoPesquisador(String tipoPesquisador) {
        this.tipoPesquisador = tipoPesquisador;
    }

    public String getComplementoPublicacao() {
        return (complementoPublicacao);
    }

    public void setComplementoPublicacao(String complementoPublicacao) {
        this.complementoPublicacao = complementoPublicacao;
    }

    public String getSemestrePublicacao() {
        return (semestrePublicacao);
    }

    public void setSemestrePublicacao(String semestrePublicacao) {
        this.semestrePublicacao = semestrePublicacao;
    }

    public Integer getAnoPublicacao() {
        return (anoPublicacao);
    }

    public void setAnoPublicacao(Integer anoPublicacao) {
        this.anoPublicacao = anoPublicacao;
    }

    public String getLocalPublicacao() {
        return (localPublicacao);
    }

    public void setLocalPublicacao(String localPublicacao) {
        this.localPublicacao = localPublicacao;
    }

    public String getEventoRevistaPublicacao() {
        return (eventoRevistaPublicacao);
    }

    public void setEventoRevistaPublicacao(String eventoRevistaPublicacao) {
        this.eventoRevistaPublicacao = eventoRevistaPublicacao;
    }

    public String getTituloPublicacao() {
        return (tituloPublicacao);
    }

    public void setTituloPublicacao(String tituloPublicacao) {
        this.tituloPublicacao = tituloPublicacao;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
