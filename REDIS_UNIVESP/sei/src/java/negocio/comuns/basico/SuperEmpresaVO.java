package negocio.comuns.basico;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.gson.annotations.Expose;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.utilitarias.ConsistirException;

/**
 * Reponsável por manter os dados da entidade SuperEmpresa. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlAccessorType(XmlAccessType.NONE)
public class SuperEmpresaVO extends SuperVO {

	protected Integer codigo;
	protected String nome;
	protected String razaoSocial;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String endereco;
	// Este atributo armazena todos os campos de formação de endereco completo
	// (endereco, numero, setor).
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String enderecoCompleto;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String setor;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String numero;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String complemento;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected CidadeVO cidade;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String CEP;
	protected String CNPJ;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String inscEstadual;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String inscMunicipal;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String telComercial1;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String telComercial2;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String telComercial3;
	// Este atributo armazena todos os telefones de entidades como: UnidadeEnsino, Pessoa...
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String telefones;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String email;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String site;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String fax;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String situacao;
	@ExcluirJsonAnnotation @JsonIgnore
	@Expose(deserialize = false, serialize = false)	
	protected String caixaPostal;
	public static final long serialVersionUID = 1L;

	/**
	 * Construtor padrão da classe <code>SuperEmpresa</code>. Cria uma nova
	 * instância desta entidade, inicializando automaticamente seus atributos
	 * (Classe VO).
	 */
	public SuperEmpresaVO() {
		super();
	}

	/**
	 * Operação responsável por validar os dados de um objeto da classe
	 * <code>SuperEmpresaVO</code>. Todos os tipos de consistência de dados são
	 * e devem ser implementadas neste método. São validações típicas:
	 * verificação de campos obrigatórios, verificação de valores válidos para
	 * os atributos.
	 * 
	 * @exception ConsistirExecption
	 *                Se uma inconsistência for encontrada aumaticamente é
	 *                gerada uma exceção descrevendo o atributo e o erro
	 *                ocorrido.
	 */
	public static void validarDados(SuperEmpresaVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getNome().equals("")) {
			throw new ConsistirException("O campo NOME (Super Empresa) deve ser informado.");
		}
		if (obj.getRazaoSocial().equals("")) {
			throw new ConsistirException("O campo RAZÃO SOCIAL (Super Empresa) deve ser informado.");
		}
		if (obj.getEndereco().equals("")) {
			throw new ConsistirException("O campo ENDEREÇO (Super Empresa) deve ser informado.");
		}
		if (obj.getCidade().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo CIDADE (Super Empresa) deve ser informado.");
		}
		if (obj.getCNPJ().equals("")) {
			throw new ConsistirException("O campo CNPJ (Super Empresa) deve ser informado.");
		}
	}

	public String getFax() {
		if (fax == null) {
			fax = "";
		}
		return (fax);
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getSite() {
		if (site == null) {
			site = "";
		}
		return (site);
	}

	public void setSite(String site) {
		this.site = site;
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

	public String getTelComercial3() {
		if (telComercial3 == null) {
			telComercial3 = "";
		}
		return (telComercial3);
	}

	public void setTelComercial3(String telComercial3) {
		this.telComercial3 = telComercial3;
	}

	public String getTelComercial2() {
		if (telComercial2 == null) {
			telComercial2 = "";
		}
		return (telComercial2);
	}

	public void setTelComercial2(String telComercial2) {
		this.telComercial2 = telComercial2;
	}

	public String getTelComercial1() {
		if (telComercial1 == null) {
			telComercial1 = "";
		}
		return (telComercial1);
	}

	public String getTelefones() {

		if (telefones == null) {
			telefones = "";
		}

		return telefones;
	}

	public void setTelefones(String telefones) {
		this.telefones = telefones;
	}

	public void setTelComercial1(String telComercial1) {
		this.telComercial1 = telComercial1;
	}

	public String getInscEstadual() {
		if (inscEstadual == null) {
			inscEstadual = "";
		}
		return (inscEstadual);
	}

	public void setInscEstadual(String inscEstadual) {
		this.inscEstadual = inscEstadual;
	}
	@XmlElement(name = "cnpjUnidadeEnsino")
	public String getCNPJ() {
		if (CNPJ == null) {
			CNPJ = "";
		}
		return (CNPJ);
	}

	public void setCNPJ(String CNPJ) {
		this.CNPJ = CNPJ;
	}

	public String getCEP() {
		if (CEP == null) {
			CEP = "";
		}
		return (CEP);
	}

	public void setCEP(String CEP) {
		this.CEP = CEP;
	}

	public CidadeVO getCidade() {
		if (cidade == null) {
			cidade = new CidadeVO();
		}
		return (cidade);
	}

	public void setCidade(CidadeVO cidade) {
		this.cidade = cidade;
	}

	public String getComplemento() {
		if (complemento == null) {
			complemento = "";
		}
		return (complemento);
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getNumero() {
		if (numero == null) {
			numero = "";
		}
		return (numero);
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getSetor() {
		if (setor == null) {
			setor = "";
		}
		return (setor);
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}

	public String getEndereco() {
		if (endereco == null) {
			endereco = "";
		}
		return (endereco);
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getEnderecoCompleto() {

		if (enderecoCompleto == null) {
			enderecoCompleto = "";
		}

		return enderecoCompleto;
	}

	public void setEnderecoCompleto(String enderecoCompleto) {
		this.enderecoCompleto = enderecoCompleto;
	}

	public String getRazaoSocial() {
		if (razaoSocial == null) {
			razaoSocial = "";
		}
		return (razaoSocial);
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	@XmlElement(name = "nome")
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return (nome);
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public boolean getAtivo() {
		if (getSituacao().equals("AT")) {
			return true;
		} else {
			return false;
		}
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "AT";
		}
		return (situacao);
	}

	public String getSituacao_Apresentar() {
		if (getSituacao().equals("AT")) {
			return "Ativo";
		} else {
			return "Inativo";
		}

	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return (codigo);
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getInscMunicipal() {
		if (inscMunicipal == null) {
			inscMunicipal = "";
		}
		return inscMunicipal;
	}

	public void setInscMunicipal(String inscMunicipal) {
		this.inscMunicipal = inscMunicipal;
	}

	public String getCaixaPostal() {
		if (caixaPostal == null) {
			caixaPostal = "";
		}
		return caixaPostal;
	}

	public void setCaixaPostal(String caixaPostal) {
		this.caixaPostal = caixaPostal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SuperEmpresaVO other = (SuperEmpresaVO) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		return true;
	}
	
	
}
