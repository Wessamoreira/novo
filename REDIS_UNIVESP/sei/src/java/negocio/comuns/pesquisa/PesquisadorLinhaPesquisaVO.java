package negocio.comuns.pesquisa;

import java.util.Date;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade PesquisadorLinhaPesquisa. Classe
 * do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class PesquisadorLinhaPesquisaVO extends SuperVO {

    private Integer codigo;
    private String tipoPesquisador;
    private Date dataFiliacao;
    private String situacao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO pesquisadorProfessor;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Matricula </code>.
     */
    private MatriculaVO pesquisadorAluno;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>PesquisadorConvidado </code>.
     */
    private PesquisadorConvidadoVO pesquisadorConvidado;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>LinhaPesquisa </code>.
     */
    private LinhaPesquisaVO linhaPesquisa;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>PesquisadorLinhaPesquisa</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public PesquisadorLinhaPesquisaVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PesquisadorLinhaPesquisaVO</code>. Todos os tipos de consistência
     * de dados são e devem ser implementadas neste método. São validações
     * típicas: verificação de campos obrigatórios, verificação de valores
     * válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PesquisadorLinhaPesquisaVO obj) throws ConsistirException {
        if (obj.getTipoPesquisador().equals("")) {
            throw new ConsistirException("O campo TIPO PESQUISADOR (Pesquisador Linha de Pesquisa) deve ser informado.");
        }
        if (obj.getDataFiliacao() == null) {
            throw new ConsistirException("O campo DATA FILIAÇÃO (Pesquisador Linha de Pesquisa) deve ser informado.");
        }
        if (obj.getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Pesquisador Linha de Pesquisa) deve ser informado.");
        }
        if ((obj.getLinhaPesquisa() == null) || (obj.getLinhaPesquisa().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo LINHA DE PESQUISA (Pesquisador Linha de Pesquisa) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setTipoPesquisador("");
        setDataFiliacao(new Date());
        setSituacao("");
    }

    /**
     * Retorna o objeto da classe <code>LinhaPesquisa</code> relacionado com (
     * <code>PesquisadorLinhaPesquisa</code>).
     */
    public LinhaPesquisaVO getLinhaPesquisa() {
        if (linhaPesquisa == null) {
            linhaPesquisa = new LinhaPesquisaVO();
        }
        return (linhaPesquisa);
    }

    /**
     * Define o objeto da classe <code>LinhaPesquisa</code> relacionado com (
     * <code>PesquisadorLinhaPesquisa</code>).
     */
    public void setLinhaPesquisa(LinhaPesquisaVO obj) {
        this.linhaPesquisa = obj;
    }

    /**
     * Retorna o objeto da classe <code>PesquisadorConvidado</code> relacionado
     * com (<code>PesquisadorLinhaPesquisa</code>).
     */
    public PesquisadorConvidadoVO getPesquisadorConvidado() {
        if (pesquisadorConvidado == null) {
            pesquisadorConvidado = new PesquisadorConvidadoVO();
        }
        return (pesquisadorConvidado);
    }

    /**
     * Define o objeto da classe <code>PesquisadorConvidado</code> relacionado
     * com (<code>PesquisadorLinhaPesquisa</code>).
     */
    public void setPesquisadorConvidado(PesquisadorConvidadoVO obj) {
        this.pesquisadorConvidado = obj;
    }

    /**
     * Retorna o objeto da classe <code>Matricula</code> relacionado com (
     * <code>PesquisadorLinhaPesquisa</code>).
     */
    public MatriculaVO getPesquisadorAluno() {
        if (pesquisadorAluno == null) {
            pesquisadorAluno = new MatriculaVO();
        }
        return (pesquisadorAluno);
    }

    /**
     * Define o objeto da classe <code>Matricula</code> relacionado com (
     * <code>PesquisadorLinhaPesquisa</code>).
     */
    public void setPesquisadorAluno(MatriculaVO obj) {
        this.pesquisadorAluno = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PesquisadorLinhaPesquisa</code>).
     */
    public PessoaVO getPesquisadorProfessor() {
        if (pesquisadorProfessor == null) {
            pesquisadorProfessor = new PessoaVO();
        }
        return (pesquisadorProfessor);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PesquisadorLinhaPesquisa</code>).
     */
    public void setPesquisadorProfessor(PessoaVO obj) {
        this.pesquisadorProfessor = obj;
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
        if (situacao.equals("AT")) {
            return "Ativo";
        }
        if (situacao.equals("IN")) {
            return "Inativo";
        }
        return (situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Date getDataFiliacao() {
        return (dataFiliacao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFiliacao_Apresentar() {
        return (Uteis.getData(dataFiliacao));
    }

    public void setDataFiliacao(Date dataFiliacao) {
        this.dataFiliacao = dataFiliacao;
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
        if (tipoPesquisador.equals("Al")) {
            return "Aluno";
        }
        if (tipoPesquisador.equals("CO")) {
            return "Convidado";
        }
        return (tipoPesquisador);
    }

    public void setTipoPesquisador(String tipoPesquisador) {
        this.tipoPesquisador = tipoPesquisador;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
