package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade CrRLog. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class ContaReceberHistoricoVO extends SuperVO {

    private Integer codigo;
    private Integer contaReceber;
    private String nossoNumero;
    private Double valorRecebimento;
    private Integer negociacaoRecebimento;
    private String nomeArquivo;
    private Date data;
    private UsuarioVO responsavel;
    private String motivo;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>CrRLog</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public ContaReceberHistoricoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>CrRLogVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ContaReceberHistoricoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
    }

    public Boolean getApresentarMotivo() {
        if (getMotivo().equals("")) {
            return false;
        }
        return true;
    }

    public Integer getContaReceber() {
        if (contaReceber == null) {
            contaReceber = 0;
        }
        return contaReceber;
    }

    public void setContaReceber(Integer contaReceber) {
        this.contaReceber = contaReceber;
    }

    public String getData_Apresentar() {
        return Uteis.getDataComHora(data);
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getMotivo() {
        if (motivo == null) {
            motivo = "";
        }
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Integer getNegociacaoRecebimento() {
        if (negociacaoRecebimento == null) {
            negociacaoRecebimento = 0;
        }
        return negociacaoRecebimento;
    }

    public void setNegociacaoRecebimento(Integer negociacaoRecebimento) {
        this.negociacaoRecebimento = negociacaoRecebimento;
    }

    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return responsavel;
    }

    public void setResponsavel(UsuarioVO responsavel) {
        this.responsavel = responsavel;
    }

    public Double getValorRecebimento() {
        if (valorRecebimento == null) {
            valorRecebimento = 0.0;
        }
        return valorRecebimento;
    }

    public void setValorRecebimento(Double valorRecebimento) {
        this.valorRecebimento = valorRecebimento;
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

    /**
     * @return the nossoNumero
     */
    public String getNossoNumero() {
        if (nossoNumero == null) {
            nossoNumero = "";
        }
        return nossoNumero;
    }

    /**
     * @param nossoNumero the nossoNumero to set
     */
    public void setNossoNumero(String nossoNumero) {
        this.nossoNumero = nossoNumero;
    }

    /**
     * @return the nomeArquivo
     */
    public String getNomeArquivo() {
        if (nomeArquivo == null) {
            nomeArquivo = "";
        }
        return nomeArquivo;
    }

    /**
     * @param nomeArquivo the nomeArquivo to set
     */
    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }
}
