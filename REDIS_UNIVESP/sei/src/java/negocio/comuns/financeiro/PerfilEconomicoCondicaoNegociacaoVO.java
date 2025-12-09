package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.facade.jdbc.financeiro.PerfilEconomico;

/**
 * Reponsável por manter os dados da entidade PerfilEconomicoCondicaoNegociacao.
 * Classe do tipo VO - Value Object composta pelos atributos da entidade com
 * visibilidade protegida e os métodos de acesso a estes atributos. Classe
 * utilizada para apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 * @see PerfilEconomico
 */
public class PerfilEconomicoCondicaoNegociacaoVO extends SuperVO {

    private Integer codigo;
    private Integer perfilEconomico;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>CondicaoNegociacao </code>.
     */
    private CondicaoNegociacaoVO condicaoNegociacao;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe
     * <code>PerfilEconomicoCondicaoNegociacao</code>. Cria uma nova instância
     * desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public PerfilEconomicoCondicaoNegociacaoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>PerfilEconomicoCondicaoNegociacaoVO</code>. Todos os tipos de
     * consistência de dados são e devem ser implementadas neste método. São
     * validações típicas: verificação de campos obrigatórios, verificação de
     * valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(PerfilEconomicoCondicaoNegociacaoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if ((obj.getCondicaoNegociacao() == null) || (obj.getCondicaoNegociacao().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CONDIÇÃO NEGOCIAÇÃO (Condição Negociação) deve ser informado.");
        }
    }

    /**
     * Retorna o objeto da classe <code>CondicaoNegociacao</code> relacionado
     * com (<code>PerfilEconomicoCondicaoNegociacao</code>).
     */
    public CondicaoNegociacaoVO getCondicaoNegociacao() {
        if (condicaoNegociacao == null) {
            condicaoNegociacao = new CondicaoNegociacaoVO();
        }
        return (condicaoNegociacao);
    }

    /**
     * Define o objeto da classe <code>CondicaoNegociacao</code> relacionado com
     * (<code>PerfilEconomicoCondicaoNegociacao</code>).
     */
    public void setCondicaoNegociacao(CondicaoNegociacaoVO obj) {
        this.condicaoNegociacao = obj;
    }

    public Integer getPerfilEconomico() {
        if (perfilEconomico == null) {
            perfilEconomico = 0;
        }
        return (perfilEconomico);
    }

    public void setPerfilEconomico(Integer perfilEconomico) {
        this.perfilEconomico = perfilEconomico;
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
