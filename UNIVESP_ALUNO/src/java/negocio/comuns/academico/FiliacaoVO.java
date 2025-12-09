package negocio.comuns.academico;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.basico.Pessoa;

/**
 * Reponsável por manter os dados da entidade Filiacao. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 * @see Pessoa
 */
@XmlRootElement(name ="filiacao")
public class FiliacaoVO extends SuperVO {

   	
	private Integer codigo;
    // private String nome;
    private String tipo;
    // private String CPF;
    // private String RG;
    // private String orgaoEmissor;
    // private CidadeVO cidade;
    // private String setor;
    // private String endereco;
    // private String cep;
    // private String telefoneComer;
    // private String telefoneRes;
    // private String telefoneRecado;
    // private String celular;
    // private String email;
    private Boolean responsavelFinanceiro;
    private Integer aluno;
    private PessoaVO pais;
    private Boolean responsavelLegal;
    private static final long serialVersionUID = 8552955975646927123L;

    /**
     * Construtor padrão da classe <code>Filiacao</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public FiliacaoVO() {
        super();
        inicializarDados();
    }
    
    public FiliacaoVO getClone() {
		try {
			FiliacaoVO obj = (FiliacaoVO) super.clone();
			obj.setPais((PessoaVO) getPais().getClone());
			return obj;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>FiliacaoVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(FiliacaoVO obj, Boolean validaCpf) throws ConsistirException {
        if (obj.getPais().getCPF().equals("")) {
            throw new ConsistirException("O campo CPF (Filiação)  deve ser informado.");
        } else {
        	if (Uteis.isAtributoPreenchido(validaCpf) && validaCpf && !obj.getCPF().contains("T") && obj.getCPF().length() < 14) {
        		obj.setCPF(obj.getCPF() + "T");
        	}
            if (Uteis.isAtributoPreenchido(validaCpf) && validaCpf && !obj.getCPF().contains("T")) {
                if (!Uteis.verificaCPF(obj.getCPF())) {
                    throw new ConsistirException("O CPF não é VÁLIDO (Filiação).");
                }
            }
        }        
        if (obj.getNome().trim().isEmpty()) {
            throw new ConsistirException("O campo NOME SOCIAL (Filiação) deve ser informado.");
        }
        if (obj.getPais().getNomeBatismo().trim().isEmpty()) {
        	obj.getPais().setNomeBatismo(obj.getNome());
            //throw new ConsistirException("O campo NOME BATISMO (Filiação) deve ser informado.");
        }
        if (obj.getTipo() == null || obj.getTipo().equals("")) {
            throw new ConsistirException("O campo TIPO (Filiação) deve ser informado.");
        }
		if (!obj.getPais().getCEP().trim().isEmpty() && Uteis.adicionarMascaraCEPConformeTamanhoCampo(obj.getPais().getCEP()).length() != 10) {
			if(obj.getTipo().equals("MA")) {
				throw new ConsistirException("O campo CEP (Filiação) da " + obj.getTipo_Apresentar() + " está com formato inválido. Exemplo: 99.999-999.");
			}
			throw new ConsistirException("O campo CEP (Filiação) do " + obj.getTipo_Apresentar() + " está com formato inválido. Exemplo: 99.999-999.");
		}
		
		if (!obj.getTelefoneComer().isEmpty() && Uteis.removeCaractersEspeciais2(obj.getTelefoneComer()).length() < 8) {
        	throw new ConsistirException("O campo TEL.COMERCIAL (Dados Filiação) deve conter 8 ou mais números informados.");
        }
        if (!obj.getTelefoneRes().isEmpty() && Uteis.removeCaractersEspeciais2(obj.getTelefoneRes()).length() < 8) {
        	throw new ConsistirException("O campo TEL.RESIDENCIAL (Dados Filiação) deve conter 8 ou mais números informados.");
        }
        if (!obj.getTelefoneRecado().isEmpty() && Uteis.removeCaractersEspeciais2(obj.getTelefoneRecado()).length() < 8) {
        	throw new ConsistirException("O campo TEL.RECADO (Dados Filiação) deve conter 8 ou mais números informados.");
        }
        if (!obj.getCelular().isEmpty() && Uteis.removeCaractersEspeciais2(obj.getCelular()).length() < 8) {
        	throw new ConsistirException("O campo CELULAR (Dados Filiação) deve conter 8 ou mais números informados.");
        }
        if (Uteis.isAtributoPreenchido(obj.getPais()) && (obj.getPais().getCodigo().equals(obj.getAluno()))) {
        	throw new ConsistirException("A pessoa informada na filiação não pode ser igual ao aluno.");
        }
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        // setNome("");
        setTipo("");
        // setCPF("");
        // setRG("");
        // setTelefoneComer("");
        // setTelefoneRes("");
        // setTelefoneRecado("");
        // setCelular("");
        // setEmail("");
    }

    @XmlElement(name ="aluno")
    public Integer getAluno() {
        return (aluno);
    }

    public void setAluno(Integer aluno) {
        this.aluno = aluno;
    }

    public String getEmail() {
        return (getPais().getEmail());

    }

    public void setEmail(String email) {
        this.getPais().setEmail(email);
    }

    public String getCelular() {

        return (getPais().getCelular());
    }

    public void setCelular(String celular) {
        this.getPais().setCelular(celular);
    }

    public String getTelefoneRecado() {

        return (getPais().getTelefoneRecado());
    }

    public void setTelefoneRecado(String telefoneRecado) {
        this.getPais().setTelefoneRecado(telefoneRecado);
    }

    public String getTelefoneRes() {
        return (getPais().getTelefoneRes());
    }

    public void setTelefoneRes(String telefoneRes) {
        this.getPais().setTelefoneRes(telefoneRes);
    }

    public String getTelefoneComer() {
        return (getPais().getTelefoneComer());
    }

    public void setTelefoneComer(String telefoneComer) {
        this.getPais().setTelefoneComer(telefoneComer);
    }

    public String getRG() {
        return (getPais().getRG());
    }

    public void setRG(String RG) {
        this.getPais().setRG(RG);
    }

    public String getCPF() {
        return (getPais().getCPF());
    }

    public void setCPF(String CPF) {
        this.getPais().setCPF(CPF);
    }

    @XmlElement(name ="tipo")
    public String getTipo() {
        return (tipo);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
	public String getTipo_Apresentar() {
        if (tipo.equals("MA")) {
            return "MÃE";
        }
        if (tipo.equals("FI")) {
            return "FILHO";
        }
        if (tipo.equals("PA")) {
            return "PAI";
        }
        if (tipo.equals("RL")) {
            return "RESPONSÁVEL LEGAL";
        }
        return (tipo);
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return (getPais().getNome());
    }

    public void setNome(String nome) {
        this.getPais().setNome(nome);
    }

    @XmlElement(name ="codigo")
    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    @XmlElement(name ="responsavelFinanceiro")
    public Boolean getResponsavelFinanceiro() {
        if (responsavelFinanceiro == null) {
            responsavelFinanceiro = Boolean.FALSE;
        }
        return responsavelFinanceiro;
    }

    public void setResponsavelFinanceiro(Boolean responsavelFinanceiro) {
        this.responsavelFinanceiro = responsavelFinanceiro;
    }

    public String getOrgaoEmissor() {
        return (getPais().getOrgaoEmissor());
    }

    public void setOrgaoEmissor(String orgaoEmissor) {
        this.getPais().setOrgaoEmissor(orgaoEmissor);
    }

    public CidadeVO getCidade() {
        return (getPais().getCidade());

    }

    public void setCidade(CidadeVO cidade) {
        this.getPais().setCidade(cidade);
    }

    public String getSetor() {
        return (getPais().getSetor());
    }

    public void setSetor(String setor) {
        this.getPais().setSetor(setor);
    }

    public String getEndereco() {
        return (getPais().getEndereco());

    }

    public void setEndereco(String endereco) {
        this.getPais().setEndereco(endereco);
    }

    public String getCep() {
        return (getPais().getCEP());
    }

    public void setCep(String cep) {
        this.getPais().setCEP(cep);
    }

    @XmlElement(name ="pais")
    public PessoaVO getPais() {
        if (pais == null) {
            pais = new PessoaVO();
        }
        return pais;
    }

    public void setPais(PessoaVO pais) {
        this.pais = pais;
    }

    @XmlElement(name ="responsavelLegal")
    public Boolean getResponsavelLegal() {
        if (responsavelLegal == null) {
            responsavelLegal = Boolean.FALSE;
        }
        return responsavelLegal;
    }

    public void setResponsavelLegal(Boolean responsavelLegal) {
        this.responsavelLegal = responsavelLegal;
    }
}
