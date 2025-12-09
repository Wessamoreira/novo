package negocio.comuns.financeiro;

/**
 * Reponsï¿½vel por manter os dados da entidade Agencia. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os mï¿½todos de acesso a estes atributos. Classe utilizada para apresentar
 * e manter em memï¿½ria os dados desta entidade.
 * 
 * @see SuperVO
 */
import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;

import negocio.comuns.basico.PossuiEndereco;
import negocio.comuns.basico.SuperEmpresaVO;
import negocio.comuns.utilitarias.ConsistirException;

public class AgenciaVO extends SuperEmpresaVO implements PossuiEndereco, Serializable {

    private String numeroAgencia;
    private String digito;
    private String gerente;
    /**
     * Atributo responsï¿½vel por manter o objeto relacionado da classe
     * <code>Banco </code>.
     */
    private BancoVO banco;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrï¿½o da classe <code>Agencia</code>. Cria uma nova
     * instï¿½ncia desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public AgenciaVO() {
        super();
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por validar os dados de um objeto da classe
     * <code>AgenciaVO</code>. Todos os tipos de consistï¿½ncia de dados sï¿½o e
     * devem ser implementadas neste mï¿½todo. Sï¿½o validaï¿½ï¿½es tï¿½picas:
     * verificaï¿½ï¿½o de campos obrigatï¿½rios, verificaï¿½ï¿½o de valores
     * vï¿½lidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistï¿½ncia for encontrada aumaticamente ï¿½
     *                gerada uma exceï¿½ï¿½o descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(AgenciaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Agência, Dados Pessoais) deve ser informado.");
        }
        if (obj.getNumeroAgencia().equals("")) {
            throw new ConsistirException("O campo NÚMERO AGÊNCIA (Agência, Dados Pessoais) deve ser informado.");
        }
        if ((obj.getBanco() == null) || (obj.getBanco().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo BANCO (Agência, Dados Pessoais) deve ser informado.");
        }
//        if ((obj.getCidade() == null) || (obj.getCidade().getCodigo().intValue() == 0)) {
//            throw new ConsistirException("O campo CIDADE (Agência) deve ser informado.");
//        }
    }

    @XmlElement(name = "digito")
    public String getDigito() {
        if (digito == null) {
            digito = "";
        }
        return digito;
    }

    public void setDigito(String digito) {
        this.digito = digito;
    }

    /**
     * Retorna o objeto da classe <code>Banco</code> relacionado com (
     * <code>Agencia</code>).
     */
    public BancoVO getBanco() {
        if (banco == null) {
            banco = new BancoVO();
        }
        return (banco);
    }

    /**
     * Define o objeto da classe <code>Banco</code> relacionado com (
     * <code>Agencia</code>).
     */
    public void setBanco(BancoVO obj) {
        this.banco = obj;
    }

    public String getGerente() {
        if (gerente == null) {
            gerente = "";
        }
        return (gerente);
    }

    public void setGerente(String gerente) {
        this.gerente = gerente;
    }
    @XmlElement(name = "numeroAgencia")
    public String getNumeroAgencia() {
        if (numeroAgencia == null) {
            numeroAgencia = "";
        }
        return (numeroAgencia);
    }

    public void setNumeroAgencia(String numeroAgencia) {
        this.numeroAgencia = numeroAgencia;
    }
}
