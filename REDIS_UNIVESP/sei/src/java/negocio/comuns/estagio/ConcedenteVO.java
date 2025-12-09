package negocio.comuns.estagio;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;

public class ConcedenteVO extends SuperVO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6706386477674461149L;
	private Integer codigo;
	private TipoConcedenteVO tipoConcedenteVO;
	private String cnpj;
	private String concedente;
	private String telefone;
	private String cep;
	private String endereco;
	private String numero;
	private String bairro;
	private String complemento;
	private String cidade;	
	private String responsavelConcedente;
	private String cpfResponsavelConcedente;
	private String emailResponsavelConcedente;
	private String telefoneResponsavelConcedente;
	private String codigoEscolaMEC;
	private AtivoInativoEnum situacao;
	private String situacaoConsultar;
	

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public TipoConcedenteVO getTipoConcedenteVO() {
		if (tipoConcedenteVO == null) {
			tipoConcedenteVO = new TipoConcedenteVO();
		}
		return tipoConcedenteVO;
	}

	public void setTipoConcedenteVO(TipoConcedenteVO tipoConcedenteVO) {
		this.tipoConcedenteVO = tipoConcedenteVO;
	}

	public String getCnpj() {
		if (cnpj == null) {
			cnpj = "";
		}
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getConcedente() {
		if (concedente == null) {
			concedente = "";
		}
		return concedente;
	}

	public void setConcedente(String concedente) {
		this.concedente = concedente;
	}

	public String getTelefone() {
		if (telefone == null) {
			telefone = "";
		}
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCep() {
		if (cep == null) {
			cep = "";
		}
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEndereco() {
		if (endereco == null) {
			endereco = "";
		}
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getNumero() {
		if (numero == null) {
			numero = "";
		}
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getBairro() {
		if (bairro == null) {
			bairro = "";
		}
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}	

	public String getCidade() {
		if (cidade == null) {
			cidade = "";
		}
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getResponsavelConcedente() {
		if (responsavelConcedente == null) {
			responsavelConcedente = "";
		}
		return responsavelConcedente;
	}

	public void setResponsavelConcedente(String responsavelConcedente) {
		this.responsavelConcedente = responsavelConcedente;
	}

	public String getCpfResponsavelConcedente() {
		if (cpfResponsavelConcedente == null) {
			cpfResponsavelConcedente = "";
		}
		return cpfResponsavelConcedente;
	}

	public void setCpfResponsavelConcedente(String cpfResponsavelConcedente) {
		this.cpfResponsavelConcedente = cpfResponsavelConcedente;
	}

	public String getEmailResponsavelConcedente() {
		if (emailResponsavelConcedente == null) {
			emailResponsavelConcedente = "";
		}
		return emailResponsavelConcedente;
	}

	public void setEmailResponsavelConcedente(String emailResponsavelConcedente) {
		this.emailResponsavelConcedente = emailResponsavelConcedente;
	}

	public String getTelefoneResponsavelConcedente() {
		if (telefoneResponsavelConcedente == null) {
			telefoneResponsavelConcedente = "";
		}
		return telefoneResponsavelConcedente;
	}

	public void setTelefoneResponsavelConcedente(String telefoneResponsavelConcedente) {
		this.telefoneResponsavelConcedente = telefoneResponsavelConcedente;
	}

	public String getComplemento() {
		if (complemento == null) {
			complemento = "";
		}
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	
	public String getEnderecoCompleto() {
		StringBuilder sb = new StringBuilder("");
		if(getEndereco().trim().length() > 0) {
			sb.append(getEndereco());
		}
		if(getNumero().trim().length() > 0) {
			sb.append(" Nº:").append(getNumero());
		}
		if(getBairro().trim().length() > 0) {
			sb.append(" Bairro:").append(getBairro());
		}
		if(getCep().trim().length() > 0) {
			sb.append(" CEP:").append(getCep());
		}
		return sb.toString();
	}

	public String getCodigoEscolaMEC() {
		if (codigoEscolaMEC == null) {
			codigoEscolaMEC = "";
		}
		return codigoEscolaMEC;
	}

	public void setCodigoEscolaMEC(String codigoEscolaMEC) {
		this.codigoEscolaMEC = codigoEscolaMEC;
	}

	public AtivoInativoEnum getSituacao() {
		if(situacao == null) {
			situacao = AtivoInativoEnum.ATIVO;
		}
		return situacao;
	}

	public void setSituacao(AtivoInativoEnum situacao) {
		this.situacao = situacao;
	}

	public String getSituacaoConsultar() {
		return situacaoConsultar;
	}

	public void setSituacaoConsultar(String situacaoConsultar) {
		this.situacaoConsultar = situacaoConsultar;
	}
}
