package negocio.comuns.financeiro;

import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.financeiro.LiberacaoFinanceiroCancelamentoTrancamento;

/**
 * Reponsável por manter os dados da entidade
 * HistoricoLiberacaoFinanceiroCancelamentoTrancamento. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 * @see LiberacaoFinanceiroCancelamentoTrancamento
 */
public class HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO extends SuperVO {

    private Integer codigo;
    private LiberacaoFinanceiroCancelamentoTrancamentoVO liberacaoFinanceiroCancelamentoTrancamento;
    private String descricao;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe
     * <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamento</code>. Cria
     * uma nova instância desta entidade, inicializando automaticamente seus
     * atributos (Classe VO).
     */
    public HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO() {
        super();
    }

    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da
     * classe <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code>
     * .
     */
    public static void validarUnicidade(List<HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO> lista, HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO obj) throws ConsistirException {
        for (HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO repetido : lista) {
        }
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO</code>. Todos
     * os tipos de consistência de dados são e devem ser implementadas neste
     * método. São validações típicas: verificação de campos obrigatórios,
     * verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(HistoricoLiberacaoFinanceiroCancelamentoTrancamentoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        // ConsistirException consistir = new ConsistirException();
        if (obj.getDescricao().equals("")) {
            // consistir.adicionarListaMensagemErro(getMensagemInternalizacao("msg_HistoricoLiberacaoFinanceiroCancelamentoTrancamento_descricao"));
        }
        // if (consistir.existeErroListaMensagemErro()) {
        // throw consistir;
        // }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        setDescricao(getDescricao().toUpperCase());
    }

    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LiberacaoFinanceiroCancelamentoTrancamentoVO getLiberacaoFinanceiroCancelamentoTrancamento() {
        if (liberacaoFinanceiroCancelamentoTrancamento == null) {
            liberacaoFinanceiroCancelamentoTrancamento = new LiberacaoFinanceiroCancelamentoTrancamentoVO();
        }
        return (liberacaoFinanceiroCancelamentoTrancamento);
    }

    public void setLiberacaoFinanceiroCancelamentoTrancamento(LiberacaoFinanceiroCancelamentoTrancamentoVO liberacaoFinanceiroCancelamentoTrancamento) {
        this.liberacaoFinanceiroCancelamentoTrancamento = liberacaoFinanceiroCancelamentoTrancamento;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
}
