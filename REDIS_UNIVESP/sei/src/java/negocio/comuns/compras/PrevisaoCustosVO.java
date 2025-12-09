package negocio.comuns.compras;

import java.util.Date;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.eventos.EventoVO;
import negocio.comuns.extensao.CursoExtensaoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade PrevisaoCustos. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class PrevisaoCustosVO extends SuperVO {

    private Integer codigo;
    private String descricao;
    private Date data;
    private Double valorEstimado;
    private Double valorGasto;
    private String tipoDestinacaoCusto;
    private Boolean pagamentoServico;
    private Integer cargaHoraria;
    private Double valorPagamentoHora;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ClassificaoCustos </code>.
     */
    private ClassificaoCustosVO classificaoCustos;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO responsavelRequisicao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO autorizacaoCustos;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Curso </code>.
     */
    private CursoVO curso;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Evento </code>.
     */
    private EventoVO evento;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>CursoExtensao </code>.
     */
    private CursoExtensaoVO cursoExtensao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>PrevisaoCustos</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public PrevisaoCustosVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PrevisaoCustosVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PrevisaoCustosVO obj) throws ConsistirException {
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO (Previsão Custos) deve ser informado.");
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Previsão Custos) deve ser informado.");
        }
        if ((obj.getClassificaoCustos() == null) || (obj.getClassificaoCustos().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CLASSIFIÇÃO CUSTOS (Previsão Custos) deve ser informado.");
        }
        if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo UNIDADE ENSINO (Previsão Custos) deve ser informado.");
        }
        if (obj.getValorEstimado().intValue() == 0) {
            throw new ConsistirException("O campo VALOR ESTIMADO (Previsão Custos) deve ser informado.");
        }
        if (obj.getValorGasto().intValue() == 0) {
            throw new ConsistirException("O campo VALOR GASTO (Previsão Custos) deve ser informado.");
        }
        if (obj.getTipoDestinacaoCusto().equals("")) {
            throw new ConsistirException("O campo TIPO DESTINAÇÃO CUSTO (Previsão Custos) deve ser informado.");
        }
        if (obj.getValorPagamentoHora().intValue() == 0) {
            throw new ConsistirException("O campo VALOR PAGAMENTO POR HORA (Previsão Custos) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setDescricao("");
        setData(new Date());
        setValorEstimado(0.0);
        setValorGasto(0.0);
        setTipoDestinacaoCusto("");
        setPagamentoServico(Boolean.FALSE);
        setCargaHoraria(0);
        setValorPagamentoHora(0.0);
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>PrevisaoCustos</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>PrevisaoCustos</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    /**
     * Retorna o objeto da classe <code>CursoExtensao</code> relacionado com (
     * <code>PrevisaoCustos</code>).
     */
    public CursoExtensaoVO getCursoExtensao() {
        if (cursoExtensao == null) {
            cursoExtensao = new CursoExtensaoVO();
        }
        return (cursoExtensao);
    }

    /**
     * Define o objeto da classe <code>CursoExtensao</code> relacionado com (
     * <code>PrevisaoCustos</code>).
     */
    public void setCursoExtensao(CursoExtensaoVO obj) {
        this.cursoExtensao = obj;
    }

    /**
     * Retorna o objeto da classe <code>Evento</code> relacionado com (
     * <code>PrevisaoCustos</code>).
     */
    public EventoVO getEvento() {
        if (evento == null) {
            evento = new EventoVO();
        }
        return (evento);
    }

    /**
     * Define o objeto da classe <code>Evento</code> relacionado com (
     * <code>PrevisaoCustos</code>).
     */
    public void setEvento(EventoVO obj) {
        this.evento = obj;
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (
     * <code>PrevisaoCustos</code>).
     */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }

    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (
     * <code>PrevisaoCustos</code>).
     */
    public void setCurso(CursoVO obj) {
        this.curso = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PrevisaoCustos</code>).
     */
    public PessoaVO getAutorizacaoCustos() {
        if (autorizacaoCustos == null) {
            autorizacaoCustos = new PessoaVO();
        }
        return (autorizacaoCustos);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PrevisaoCustos</code>).
     */
    public void setAutorizacaoCustos(PessoaVO obj) {
        this.autorizacaoCustos = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PrevisaoCustos</code>).
     */
    public PessoaVO getResponsavelRequisicao() {
        if (responsavelRequisicao == null) {
            responsavelRequisicao = new PessoaVO();
        }
        return (responsavelRequisicao);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>PrevisaoCustos</code>).
     */
    public void setResponsavelRequisicao(PessoaVO obj) {
        this.responsavelRequisicao = obj;
    }

    /**
     * Retorna o objeto da classe <code>ClassificaoCustos</code> relacionado com
     * (<code>PrevisaoCustos</code>).
     */
    public ClassificaoCustosVO getClassificaoCustos() {
        if (classificaoCustos == null) {
            classificaoCustos = new ClassificaoCustosVO();
        }
        return (classificaoCustos);
    }

    /**
     * Define o objeto da classe <code>ClassificaoCustos</code> relacionado com
     * (<code>PrevisaoCustos</code>).
     */
    public void setClassificaoCustos(ClassificaoCustosVO obj) {
        this.classificaoCustos = obj;
    }

    public Double getValorPagamentoHora() {
        return (valorPagamentoHora);
    }

    public void setValorPagamentoHora(Double valorPagamentoHora) {
        this.valorPagamentoHora = valorPagamentoHora;
    }

    public Integer getCargaHoraria() {
        return (cargaHoraria);
    }

    public void setCargaHoraria(Integer cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public Boolean getPagamentoServico() {
        return (pagamentoServico);
    }

    public Boolean isPagamentoServico() {
        return (pagamentoServico);
    }

    public void setPagamentoServico(Boolean pagamentoServico) {
        this.pagamentoServico = pagamentoServico;
    }

    public String getTipoDestinacaoCusto() {
        return (tipoDestinacaoCusto);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoDestinacaoCusto_Apresentar() {
        if (tipoDestinacaoCusto.equals("CU")) {
            return "Curso";
        }
        if (tipoDestinacaoCusto.equals("EV")) {
            return "Evento";
        }
        if (tipoDestinacaoCusto.equals("CE")) {
            return "Curso de Extensão";
        }
        return (tipoDestinacaoCusto);
    }

    public void setTipoDestinacaoCusto(String tipoDestinacaoCusto) {
        this.tipoDestinacaoCusto = tipoDestinacaoCusto;
    }

    public Double getValorGasto() {
        return (valorGasto);
    }

    public void setValorGasto(Double valorGasto) {
        this.valorGasto = valorGasto;
    }

    public Double getValorEstimado() {
        return (valorEstimado);
    }

    public void setValorEstimado(Double valorEstimado) {
        this.valorEstimado = valorEstimado;
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

    public String getDescricao() {
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
