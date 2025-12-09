package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PossuiEndereco;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.financeiro.Parceiro;

/**
 * Reponsável por manter os dados da entidade ContatoParceiro. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 *
 * @see SuperVO
 * @see Parceiro
 */
public class ContatoParceiroVO extends SuperVO implements PossuiEndereco {

    private Integer codigo;
    private String nome;
    private String cargo;
    private String telefone;
    private String ramal;
    private String celular;
    private String email;
    private ParceiroVO parceiro;
    private Date dataNascimento;

    // INDICA QUE A PESSOA É RESPONSÁVEL LEGAL DA EMPRESA - ASSINA POR CONTRATOS VINCULADOS AO PARCEIRO
    private Boolean responsavelLegal;
    
    // DADOS PARA EMISSAO DE CONTRATO QUANDO É RESPONSAVEL LEGAL
    private String cpf;
    private String rg;
    private String endereco;
    private String setor;
    private String numero;
    private String complemento;
    private CidadeVO cidade;
    private String cep;    
    	
    public static final long serialVersionUID = 1L;

    // atributo transiente - nao persistido.
    private String msgCpfInvalido;
	
    /**
     * Construtor padrão da classe <code>ContatoParceiro</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ContatoParceiroVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ContatoParceiroVO</code>. Todos os tipos de consistência de dados
     * são e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ContatoParceiroVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getNome().equals("")) {
            throw new ConsistirException("O campo NOME (Contato Parceiro) deve ser informado.");
        }
//        if (Uteis.getDataBD0000(obj.getDataNascimento()).equals(Uteis.getDataBD0000(new Date()))) {
//            throw new ConsistirException("O campo Data Nascimento (Contato Parceiro) deve ser diferente da data atual.");
//        }
//        if (obj.getCargo().equals("")) {
//            throw new ConsistirException("O campo CARGO (Contato Parceiro) deve ser informado.");
//        }
//        if (obj.getTelefone().equals("")) {
//            throw new ConsistirException("O campo TELEFONE (Contato Parceiro) deve ser informado.");
//        }
//        if (obj.getEmail().equals("")) {
//            throw new ConsistirException("O campo EMAIL (Contato Parceiro) deve ser informado.");
//        }
    }

    public ParceiroVO getParceiro() {
        if (parceiro == null) {
            parceiro = new ParceiroVO();
        }
        return (parceiro);
    }

    public void setParceiro(ParceiroVO parceiro) {
        this.parceiro = parceiro;
    }

    public String getEmail() {
        if (email == null) {
            email = "";
        }
        return (email);
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCelular() {
        if (celular == null) {
            celular = "";
        }
        return (celular);
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getRamal() {
        if (ramal == null) {
            ramal = "";
        }
        return (ramal);
    }

    public void setRamal(String ramal) {
        this.ramal = ramal;
    }

    public String getTelefone() {
        if (telefone == null) {
            telefone = "";
        }
        return (telefone);
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCargo() {
        if (cargo == null) {
            cargo = "";
        }
        return (cargo);
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
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

    public Date getDataNascimento() {
        if (dataNascimento == null) {
            dataNascimento = new Date();
        }
        return dataNascimento;
    }

    public String getDataNascimento_Apresentar() {
        return Uteis.getData(getDataNascimento());
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
	

    /**
     * @return the responsavelLegal
     */
    public Boolean getResponsavelLegal() {
        if (responsavelLegal == null) {
            responsavelLegal = Boolean.FALSE;
        }         
        return responsavelLegal;
    }

    /**
     * @param responsavelLegal the responsavelLegal to set
     */
    public void setResponsavelLegal(Boolean responsavelLegal) {
        this.responsavelLegal = responsavelLegal;
    }

    /**
     * @return the cpf
     */
    public String getCpf() {
        if (cpf == null) {
            cpf = "";
        }          
        return cpf;
    }

    /**
     * @param cpf the cpf to set
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * @return the rg
     */
    public String getRg() {
        if (rg == null) {
            rg = "";
        }        
        return rg;
    }

    /**
     * @param rg the rg to set
     */
    public void setRg(String rg) {
        this.rg = rg;
    }

    /**
     * @return the endereco
     */
    public String getEndereco() {
        if (endereco == null) {
            endereco = "";
        }         
        return endereco;
    }

    /**
     * @param endereco the endereco to set
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    /**
     * @return the setor
     */
    public String getSetor() {
        if (setor == null) {
            setor = "";
        }        
        return setor;
    }

    /**
     * @param setor the setor to set
     */
    public void setSetor(String setor) {
        this.setor = setor;
    }

    /**
     * @return the numero
     */
    public String getNumero() {
        if (numero == null) {
            numero = "";
        }        
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return the complemento
     */
    public String getComplemento() {
        if (complemento == null) {
            complemento = "";
        }
        return complemento;
    }

    /**
     * @param complemento the complemento to set
     */
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    /**
     * @return the cidade
     */
    public CidadeVO getCidade() {
        if (cidade == null) {
            cidade = new CidadeVO();
        }
        return cidade;
    }

    /**
     * @param cidade the cidade to set
     */
    public void setCidade(CidadeVO cidade) {
        this.cidade = cidade;
    }

    /**
     * @return the cep
     */
    public String getCEP() {
        if (cep == null) {
            cep = "";
        }
        return cep;
    }

    /**
     * @param cep the cep to set
     */
    public void setCEP(String cep) {
        this.cep = cep;
    }
    
/**
     * @return the cep
     */
    public String getCep() {
        if (cep == null) {
            cep = "";
        }
        return cep;
    }

    /**
     * @param cep the cep to set
     */
    public void setCep(String cep) {
        this.cep = cep;
    }

    /**
     * @return the msgCpfInvalido
     */
    public String getMsgCpfInvalido() {
        if (msgCpfInvalido == null) {
            msgCpfInvalido = "";
        }
        return msgCpfInvalido;
    }

    /**
     * @param msgCpfInvalido the msgCpfInvalido to set
     */
    public void setMsgCpfInvalido(String msgCpfInvalido) {
        this.msgCpfInvalido = msgCpfInvalido;
    }
	
}
