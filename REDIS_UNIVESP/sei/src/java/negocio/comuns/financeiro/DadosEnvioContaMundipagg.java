package negocio.comuns.financeiro;

import negocio.comuns.arquitetura.SuperVO;

/**
 * 
 * @author Victor Hugo de Paula Costa 03/11/2015 17:09
 *
 */
public class DadosEnvioContaMundipagg extends SuperVO {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * AccountReference
	 */
	private String referenciaConta;
	/**
	 * AddressNumber
	 */
	private String numeroEndereco;
	/**
	 * City
	 */
	private String cidade;
	/**
	 * Complement
	 */
	private String complemento;
	/**
	 * Country
	 */
	private String pais;
	/**
	 * District
	 */
	private String distrito;
	/**
	 * State (SIGLA)
	 */
	private String estado;
	/**
	 * StreetAddress
	 */
	private String endereco;
	/**
	 * ZipCode
	 */
	private String cep;
	/**
	 * DbaName
	 */
	private String nomeFantasia;
	/**
	 * DocumentNumber
	 */
	private String cpfCnpj;
	/**
	 * DocumentType
	 */
	private String tipoDocumento;
	/**
	 * Email
	 */
	private String emailEmpresa;
	/**
	 * AccountNumber
	 */
	private String numeroContaBanco;
	/**
	 * AgencyNumber
	 */
	private String numeroAgencia;
	/**
	 * BankCode
	 */
	private String codigoBanco;
	/**
	 * Destination
	 */
	private String destino;
	/**
	 * Email
	 */
	private String emailDestino;
	/**
	 * LegalName
	 */
	private String razaoSocial;
	/**
	 * MCC
	 */
	private String mcc;
	/**
	 * PhoneNumber
	 */
	private String telefone;
	/**
	 * RequestKey (MerchantKey)
	 */
	private String requestKey;
	/**
	 * SmartWalletKey
	 */
	private String smartWalletKey;
	/**
	 * SocialUserName
	 */
	private String nomeSocialUsuario;
	
	public String getReferenciaConta() {
		if(referenciaConta == null) {
			referenciaConta = "";
		}
		return referenciaConta;
	}
	public void setReferenciaConta(String referenciaConta) {
		this.referenciaConta = referenciaConta;
	}
	public String getNumeroEndereco() {
		if(numeroEndereco == null) {
			numeroEndereco = "";
		}
		return numeroEndereco;
	}
	public void setNumeroEndereco(String numeroEndereco) {
		this.numeroEndereco = numeroEndereco;
	}
	public String getCidade() {
		if(cidade == null) {
			cidade = "";
		}
		return cidade;
	}
	public void setCidade(String cidade) {
		this.cidade = cidade;
	}
	public String getComplemento() {
		if(complemento == null) {
			complemento = "";
		}
		return complemento;
	}
	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}
	public String getPais() {
		if(pais == null) {
			pais = "";
		}
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getDistrito() {
		if(distrito == null) {
			distrito = "";
		}
		return distrito;
	}
	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}
	public String getEstado() {
		if(estado == null) {
			estado = "";
		}
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	public String getEndereco() {
		if(endereco == null) {
			endereco = "";
		}
		return endereco;
	}
	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}
	public String getCep() {
		if(cep == null) {
			cep = ""; 
		}
		return cep;
	}
	public void setCep(String cep) {
		this.cep = cep;
	}
	public String getNomeFantasia() {
		if(nomeFantasia == null) {
			nomeFantasia = "";
		}
		return nomeFantasia;
	}
	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}
	public String getCpfCnpj() {
		if(cpfCnpj == null) {
			cpfCnpj = "";
		}
		return cpfCnpj;
	}
	public void setCpfCnpj(String cpfCnpj) {
		this.cpfCnpj = cpfCnpj;
	}
	public String getTipoDocumento() {
		if(tipoDocumento == null) {
			tipoDocumento = "";
		}
		return tipoDocumento;
	}
	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}
	public String getEmailEmpresa() {
		if(emailEmpresa == null) {
			emailEmpresa = "";
		}
		return emailEmpresa;
	}
	public void setEmailEmpresa(String emailEmpresa) {
		this.emailEmpresa = emailEmpresa;
	}
	public String getNumeroContaBanco() {
		if(numeroContaBanco == null) {
			numeroContaBanco = "";
		}
		return numeroContaBanco;
	}
	public void setNumeroContaBanco(String numeroContaBanco) {
		this.numeroContaBanco = numeroContaBanco;
	}
	public String getNumeroAgencia() {
		if(numeroAgencia == null) {
			numeroAgencia = "";
		}
		return numeroAgencia;
	}
	public void setNumeroAgencia(String numeroAgencia) {
		this.numeroAgencia = numeroAgencia;
	}
	public String getCodigoBanco() {
		if(codigoBanco == null) {
			codigoBanco = "";
		}
		return codigoBanco;
	}
	public void setCodigoBanco(String codigoBanco) {
		this.codigoBanco = codigoBanco;
	}
	public String getDestino() {
		if(destino == null) {
			destino = "";
		}
		return destino;
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	public String getEmailDestino() {
		if(emailDestino == null) {
			emailDestino = "";
		}
		return emailDestino;
	}
	public void setEmailDestino(String emailDestino) {
		this.emailDestino = emailDestino;
	}
	public String getRazaoSocial() {
		if(razaoSocial == null) {
			razaoSocial = "";
		}
		return razaoSocial;
	}
	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}
	public String getMcc() {
		if(mcc == null) {
			mcc = "";
		}
		return mcc;
	}
	public void setMcc(String mcc) {
		this.mcc = mcc;
	}
	public String getTelefone() {
		if(telefone == null) {
			telefone = "";
		}
		return telefone;
	}
	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}
	public String getRequestKey() {
		if(requestKey == null) {
			requestKey = "";
		}
		return requestKey;
	}
	public void setRequestKey(String requestKey) {
		this.requestKey = requestKey;
	}
	public String getSmartWalletKey() {
		if(smartWalletKey == null) {
			smartWalletKey = "";
		}
		return smartWalletKey;
	}
	public void setSmartWalletKey(String smartWalletKey) {
		this.smartWalletKey = smartWalletKey;
	}
	public String getNomeSocialUsuario() {
		if(nomeSocialUsuario == null) {
			nomeSocialUsuario = "";
		}
		return nomeSocialUsuario;
	}
	public void setNomeSocialUsuario(String nomeSocialUsuario) {
		this.nomeSocialUsuario = nomeSocialUsuario;
	}
}
