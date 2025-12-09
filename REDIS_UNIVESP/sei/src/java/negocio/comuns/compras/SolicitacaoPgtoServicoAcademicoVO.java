package negocio.comuns.compras;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade SolicitacaoPgtoServicoAcademico.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class SolicitacaoPgtoServicoAcademicoVO extends SuperVO {

    private Integer codigo;
    private Date date;
    private String descricao;
    private Integer quantidadeHoras;
    private Double valorHora;
    private Double valorTotal;
    private Date dataAutorizacao;
    private String parecerResponsavel;
    private String situacao;
    private String tipoDestinatarioPagamento;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>CentroCusto </code>.
     */
    private CategoriaDespesaVO centroDespesa;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO responsavelAutorizacao;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>PrevisaoCustos </code>.
     */
    private PrevisaoCustosVO previsaoCustosCurso;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Pessoa </code>.
     */
    private PessoaVO pessoaPgtoServico;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>SolicitacaoPgtoServicoAcademico</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente
     * seus atributos (Classe VO).
     */
    public SolicitacaoPgtoServicoAcademicoVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>SolicitacaoPgtoServicoAcademicoVO</code>. Todos os tipos de
     * consistência de dados são e devem ser implementadas neste método. São
     * validações típicas: verificação de campos obrigatórios, verificação de
     * valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(SolicitacaoPgtoServicoAcademicoVO obj) throws ConsistirException {
        if (obj.getDate() == null) {
            throw new ConsistirException("O campo DATE (Solicitação Pagamento Serviço Acadêmico) deve ser informado.");
        }
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO (Solicitação Pagamento Serviço Acadêmico) deve ser informado.");
        }
        if (obj.getQuantidadeHoras().intValue() == 0) {
            throw new ConsistirException("O campo QUANTIDADE HORAS (Solicitação Pagamento Serviço Acadêmico) deve ser informado.");
        }
        if (obj.getValorHora().intValue() == 0) {
            throw new ConsistirException("O campo VALOR HORA (Solicitação Pagamento Serviço Acadêmico) deve ser informado.");
        }
        if (obj.getValorTotal().intValue() == 0) {
            throw new ConsistirException("O campo VALOR TOTAL (Solicitação Pagamento Serviço Acadêmico) deve ser informado.");
        }
        if ((obj.getCentroDespesa() == null) || (obj.getCentroDespesa().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CENTRO DESPEA (Solicitação Pagamento Serviço Acadêmico) deve ser informado.");
        }
        if (obj.getSituacao().equals("")) {
            throw new ConsistirException("O campo SITUAÇÃO (Solicitação Pagamento Serviço Acadêmico) deve ser informado.");
        }
        if ((obj.getPrevisaoCustosCurso() == null) || (obj.getPrevisaoCustosCurso().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo PREVISÃO CUSTOS CURSO (Solicitação Pagamento Serviço Acadêmico) deve ser informado.");
        }
        if (obj.getTipoDestinatarioPagamento().equals("")) {
            throw new ConsistirException("O campo TIPO DESTINATÁRIO PAGAMENTO (Solicitação Pagamento Serviço Acadêmico) deve ser informado.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setDate(new Date());
        setDescricao("");
        setQuantidadeHoras(0);
        setValorHora(0.0);
        setValorTotal(0.0);
        setDataAutorizacao(new Date());
        setParecerResponsavel("");
        setSituacao("");
        setTipoDestinatarioPagamento("");
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>SolicitacaoPgtoServicoAcademico</code>).
     */
    public PessoaVO getPessoaPgtoServico() {
        if (pessoaPgtoServico == null) {
            pessoaPgtoServico = new PessoaVO();
        }
        return (pessoaPgtoServico);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>SolicitacaoPgtoServicoAcademico</code>).
     */
    public void setPessoaPgtoServico(PessoaVO obj) {
        this.pessoaPgtoServico = obj;
    }

    /**
     * Retorna o objeto da classe <code>PrevisaoCustos</code> relacionado com (
     * <code>SolicitacaoPgtoServicoAcademico</code>).
     */
    public PrevisaoCustosVO getPrevisaoCustosCurso() {
        if (previsaoCustosCurso == null) {
            previsaoCustosCurso = new PrevisaoCustosVO();
        }
        return (previsaoCustosCurso);
    }

    /**
     * Define o objeto da classe <code>PrevisaoCustos</code> relacionado com (
     * <code>SolicitacaoPgtoServicoAcademico</code>).
     */
    public void setPrevisaoCustosCurso(PrevisaoCustosVO obj) {
        this.previsaoCustosCurso = obj;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>SolicitacaoPgtoServicoAcademico</code>).
     */
    public PessoaVO getResponsavelAutorizacao() {
        if (responsavelAutorizacao == null) {
            responsavelAutorizacao = new PessoaVO();
        }
        return (responsavelAutorizacao);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com (
     * <code>SolicitacaoPgtoServicoAcademico</code>).
     */
    public void setResponsavelAutorizacao(PessoaVO obj) {
        this.responsavelAutorizacao = obj;
    }

    /**
     * Retorna o objeto da classe <code>CentroCusto</code> relacionado com (
     * <code>SolicitacaoPgtoServicoAcademico</code>).
     */
    public CategoriaDespesaVO getCentroDespesa() {
        if (centroDespesa == null) {
            centroDespesa = new CategoriaDespesaVO();
        }
        return (centroDespesa);
    }

    /**
     * Define o objeto da classe <code>CentroCusto</code> relacionado com (
     * <code>SolicitacaoPgtoServicoAcademico</code>).
     */
    public void setCentroDespesa(CategoriaDespesaVO obj) {
        this.centroDespesa = obj;
    }

    public String getTipoDestinatarioPagamento() {
        return (tipoDestinatarioPagamento);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getTipoDestinatarioPagamento_Apresentar() {
        if (tipoDestinatarioPagamento.equals("PR")) {
            return "Professor";
        }
        if (tipoDestinatarioPagamento.equals("AL")) {
            return "Aluno";
        }
        if (tipoDestinatarioPagamento.equals("MC")) {
            return "Menbro Comunidade";
        }
        if (tipoDestinatarioPagamento.equals("FU")) {
            return "Funcionário";
        }
        return (tipoDestinatarioPagamento);
    }

    public void setTipoDestinatarioPagamento(String tipoDestinatarioPagamento) {
        this.tipoDestinatarioPagamento = tipoDestinatarioPagamento;
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
            return "Deferido";
        }
        if (situacao.equals("IN")) {
            return "Indeferido";
        }
        if (situacao.equals("AG")) {
            return "Aguardando Deferimento";
        }
        return (situacao);
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getParecerResponsavel() {
        return (parecerResponsavel);
    }

    public void setParecerResponsavel(String parecerResponsavel) {
        this.parecerResponsavel = parecerResponsavel;
    }

    public Date getDataAutorizacao() {
        return (dataAutorizacao);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataAutorizacao_Apresentar() {
        return (Uteis.getData(dataAutorizacao));
    }

    public void setDataAutorizacao(Date dataAutorizacao) {
        this.dataAutorizacao = dataAutorizacao;
    }

    public Double getValorTotal() {
        return (valorTotal);
    }

    public void setValorTotal(Double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public Double getValorHora() {
        return (valorHora);
    }

    public void setValorHora(Double valorHora) {
        this.valorHora = valorHora;
    }

    public Integer getQuantidadeHoras() {
        return (quantidadeHoras);
    }

    public void setQuantidadeHoras(Integer quantidadeHoras) {
        this.quantidadeHoras = quantidadeHoras;
    }

    public String getDescricao() {
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Date getDate() {
        return (date);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDate_Apresentar() {
        return (Uteis.getData(date));
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
