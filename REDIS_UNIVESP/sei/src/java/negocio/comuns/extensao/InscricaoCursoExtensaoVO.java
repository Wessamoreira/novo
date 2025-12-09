package negocio.comuns.extensao;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade InscricaoCursoExtensao. Classe do
 * tipo VO - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class InscricaoCursoExtensaoVO extends SuperVO {

    private Integer nrInscricao;
    private Integer cursoExtensao;
    private Date data;
    private String hora;
    private Double valorTotal;
    private String tipoInscricao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO pessoaInscricaoCursoExtensao;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>InscricaoCursoExtensao</code>. Cria uma
     * nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public InscricaoCursoExtensaoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>InscricaoCursoExtensaoVO</code>. Todos os tipos de consistência de
     * dados são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(InscricaoCursoExtensaoVO obj) throws ConsistirException {
        if (obj.getCursoExtensao().intValue() == 0) {
            throw new ConsistirException("O campo CURSO EXTENSÃO (Inscrição Curso Extensão) deve ser informado.");
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Inscrição Curso Extensão) deve ser informado.");
        }
        if (obj.getHora().equals("")) {
            throw new ConsistirException("O campo HORA (Inscrição Curso Extensão) deve ser informado.");
        }
        if (obj.getValorTotal().intValue() == 0) {
            throw new ConsistirException("O campo VALOR TOTAL (Inscrição Curso Extensão) deve ser informado.");
        }
        if (obj.getTipoInscricao().equals("")) {
            throw new ConsistirException("O campo TIPO INSCRIÇÃO (Inscrição Curso Extensão) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setNrInscricao(0);
        setCursoExtensao(0);
        setData(new Date());
        setHora("");
        setValorTotal(0.0);
        setTipoInscricao("");
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>InscricaoCursoExtensao</code>).
     */
    public PessoaVO getPessoaInscricaoCursoExtensao() {
        if (pessoaInscricaoCursoExtensao == null) {
            pessoaInscricaoCursoExtensao = new PessoaVO();
        }
        return (pessoaInscricaoCursoExtensao);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>InscricaoCursoExtensao</code>).
     */
    public void setPessoaInscricaoCursoExtensao(PessoaVO obj) {
        this.pessoaInscricaoCursoExtensao = obj;
    }

    public String getTipoInscricao() {
        return (tipoInscricao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoInscricao_Apresentar() {
        if (tipoInscricao.equals("PR")) {
            return "Professor";
        }
        if (tipoInscricao.equals("AL")) {
            return "Aluno";
        }
        if (tipoInscricao.equals("MC")) {
            return "Membro Comunidade";
        }
        if (tipoInscricao.equals("FU")) {
            return "Funcionário";
        }
        return (tipoInscricao);
    }

    public void setTipoInscricao(String tipoInscricao) {
        this.tipoInscricao = tipoInscricao;
    }

    public Double getValorTotal() {
        return (valorTotal);
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getHora() {
        return (hora);
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

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

    public Integer getCursoExtensao() {
        return (cursoExtensao);
    }

    public void setCursoExtensao(Integer cursoExtensao) {
        this.cursoExtensao = cursoExtensao;
    }

    public Integer getNrInscricao() {
        return (nrInscricao);
    }

    public void setNrInscricao(Integer nrInscricao) {
        this.nrInscricao = nrInscricao;
    }
}
