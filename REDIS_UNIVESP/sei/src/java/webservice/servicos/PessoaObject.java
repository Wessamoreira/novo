package webservice.servicos;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.basico.PessoaVO;

@XmlRootElement(name = "pessoa")
public class PessoaObject {

	private Integer codigo;
	private String matricula;
	private String nome;
	private Boolean liberar;
	private String email;
	private String telefoneResidencial;
	private String celular;
	private String cpf;
	private String rg;
	private String cep;	
	private String complemento;	
	private String endereco;
	private String numero;
	private String setor;
	private CidadeObject cidade;
	private String erro;
	private Integer codigoCurso;

	public PessoaObject() {
	}

	@XmlElement(name = "matricula")
	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	@XmlElement(name = "nome")
	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@XmlElement(name = "liberar")
	public Boolean getLiberar() {
		if (liberar == null) {
			liberar = Boolean.FALSE;
		}
		return liberar;
	}

	public void setLiberar(Boolean liberar) {
		this.liberar = liberar;
	}

	/**
	 * @return the codigo
	 */
	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	/**
	 * @param codigo
	 *            the codigo to set
	 */
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	@XmlElement(name = "email")
	public String getEmail() {
		if(email == null) {
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@XmlElement(name = "telefoneResidencial")
	public String getTelefoneResidencial() {
		if(telefoneResidencial == null) {
			telefoneResidencial = "";
		}
		return telefoneResidencial;
	}

	public void setTelefoneResidencial(String telefoneResidencial) {
		this.telefoneResidencial = telefoneResidencial;
	}

	@XmlElement(name = "celular")
	public String getCelular() {
		if(celular == null) {
			celular = "";
		}
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	@XmlElement(name = "cpf")
	public String getCpf() {
		if(cpf == null) {
			cpf = "";
		}
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	@XmlElement(name = "rg")
	public String getRg() {
		if(rg == null) {
			rg = "";
		}
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	@XmlElement(name = "endereco")
	public String getEndereco() {
		if(endereco == null) {
			endereco = "";
		}
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	@XmlElement(name = "numero")
	public String getNumero() {
		if(numero == null) {
			numero = "";
		}
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getErro() {
		if(erro == null) {
			erro = "";
		}
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}
	
	public Integer getCodigoCurso() {
		if (codigoCurso == null) {
			codigoCurso = 0;
		}
		return codigoCurso;
	}

	public void setCodigoCurso(Integer codigoCurso) {
		this.codigoCurso = codigoCurso;
	}

	@XmlElement(name = "cep")
	public String getCep() {
		if (cep == null) {
			cep = "";
		}
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	@XmlElement(name = "complemento")
	public String getComplemento() {
		if (complemento == null) {
			complemento = "";
		}
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	@XmlElement(name = "cidade")
	public CidadeObject getCidade() {
		if (cidade == null) {
			cidade = new CidadeObject();
		}
		return cidade;
	}

	public void setCidade(CidadeObject cidade) {
		this.cidade = cidade;
	}

	@XmlElement(name = "setor")
	public String getSetor() {
		if (setor == null) {
			setor = "";
		}
		return setor;
	}

	public void setSetor(String setor) {
		this.setor = setor;
	}
	
	
	public PessoaObject converterPessoaVO(PessoaVO pessoaVO) {
		PessoaObject pessoaObject = new PessoaObject();
		pessoaObject.setCodigo(pessoaVO.getCodigo());
		pessoaObject.setNome(pessoaVO.getNome());
		pessoaObject.setCpf(pessoaVO.getCPF());		
		pessoaObject.setRg(pessoaVO.getRG());		
		
		return pessoaObject;
	}
	
	
}
