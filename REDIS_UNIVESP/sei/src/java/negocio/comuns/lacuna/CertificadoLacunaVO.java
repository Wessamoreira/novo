package negocio.comuns.lacuna;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.amazonaws.services.certificatemanager.model.ExtendedKeyUsage;
import com.fasterxml.jackson.annotation.JsonProperty;

import negocio.comuns.academico.DocumentoAssinadoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;

public class CertificadoLacunaVO {

	@JsonProperty("subjectName")
	private String subjectName;
	@JsonProperty("subjectDN")
	private String subjectDN;
	@JsonProperty("issuerName")
	private String issuerName;
	@JsonProperty("issuerDN")
	private String issuerDN;
	@JsonProperty("email")
	private String email;
	@JsonProperty("thumbprint")
	private String thumbprint;
	@JsonProperty("keyUsage")
	private KeyUsage keyUsage;
	@JsonProperty("extendedKeyUsage")
	private ExtendedKeyUsage extendedKeyUsage;
	@JsonProperty("canRemove")
	private Boolean canRemove;
	@JsonProperty("isRemote")
	private Boolean isRemote;
	@JsonProperty("validityStart")
	private Date validityStart;
	@JsonProperty("validityEnd")
	private Date validityEnd;
	@JsonProperty("pkiBrazil")
	private PkiBrazil pkiBrazil;
	@JsonProperty("certificatePolicies")
	private List<Object> certificatePolicies;
	@JsonProperty("$$hashKey")
	private String $$hashKey;
	@JsonProperty("$$mdSelectId")
	private float $$mdSelectId;

	public void validarDados() throws Exception {
		if (!(Objects.nonNull(toString()) && !toString().isEmpty())) {
			throw new Exception("Não foi possível localizar os dados do certificado escolhido para assinatura com o provedor.");
		}
		if (!(Objects.nonNull(getPkiBrazil()) && !getPkiBrazil().toString().isEmpty())) {
			throw new Exception("Não foi possível localizar os dados do certificado escolhido para assinatura com o provedor.");
		}
		if (!(Objects.nonNull(getThumbprint()) && !getThumbprint().isEmpty())) {
			throw new Exception("O certificado selecionado não possui a propriedade \"ThumbPrint\" para realizar a assinatura do xml");
		}
	}

	public String getSubjectName() {
		return subjectName;
	}

	public String getSubjectDN() {
		return subjectDN;
	}

	public String getIssuerName() {
		return issuerName;
	}

	public String getIssuerDN() {
		return issuerDN;
	}

	public String getEmail() {
		return email;
	}

	public String getThumbprint() {
		return thumbprint;
	}

	public KeyUsage getKeyUsage() {
		return keyUsage;
	}

	public ExtendedKeyUsage getExtendedKeyUsage() {
		return extendedKeyUsage;
	}

	public Boolean getCanRemove() {
		return canRemove;
	}

	public Boolean getIsRemote() {
		return isRemote;
	}

	public Date getValidityStart() {
		return validityStart;
	}
	
	public String getValidityStart_apresentar() {
		return Uteis.isAtributoPreenchido(getValidityStart()) ? Uteis.getData(getValidityStart()) : Constantes.EMPTY;
	}

	public Date getValidityEnd() {
		return validityEnd;
	}
	
	public String getValidityEnd_apresentar() {
		return Uteis.isAtributoPreenchido(getValidityEnd()) ? Uteis.getData(getValidityEnd()) : Constantes.EMPTY;
	}
	
	public boolean isCertificadoValido() {
		return Uteis.isAtributoPreenchido(getValidityEnd()) ? new Date().compareTo(getValidityEnd()) <= 0 : Boolean.TRUE;
	}
	
	public boolean isApresentarCertificadoListagem(UsuarioVO usuarioLogado, UnidadeEnsinoVO unidadeExpedicaoDiploma, Boolean assinarPorCnpj, Integer ordemAssinatura) {
		if (!isCertificadoValido() || Objects.isNull(getPkiBrazil())) {
			return Boolean.FALSE;
		}
		if (assinarPorCnpj && getPkiBrazil().getIsPessoaJuridica() && Uteis.isAtributoPreenchido(getPkiBrazil().getCnpj())) {
			String cnpj = ordemAssinatura.equals(3) ? unidadeExpedicaoDiploma.getCNPJ() : unidadeExpedicaoDiploma.getCnpjUnidadeCertificadora();
			return Objects.equals(Uteis.retirarMascaraCNPJ(getPkiBrazil().getCnpj()), Uteis.retirarMascaraCNPJ(cnpj));
		} else if (!assinarPorCnpj && getPkiBrazil().getIsPessoaFisica() && Uteis.isAtributoPreenchido(getPkiBrazil().getCpf())) {
			return Objects.equals(Uteis.retirarMascaraCPF(getPkiBrazil().getCpf()), Uteis.retirarMascaraCPF(usuarioLogado.getPessoa().getCPF()));
		} else {
			return Boolean.FALSE;
		}
	}

	public PkiBrazil getPkiBrazil() {
		return pkiBrazil;
	}

	public void setPkiBrazil(PkiBrazil pkiBrazil) {
		this.pkiBrazil = pkiBrazil;
	}

	public List<Object> getCertificatePolicies() {
		return certificatePolicies;
	}

	public String get$$hashKey() {
		return $$hashKey;
	}

	public float get$$mdSelectId() {
		return $$mdSelectId;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public void setSubjectDN(String subjectDN) {
		this.subjectDN = subjectDN;
	}

	public void setIssuerName(String issuerName) {
		this.issuerName = issuerName;
	}

	public void setIssuerDN(String issuerDN) {
		this.issuerDN = issuerDN;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setThumbprint(String thumbprint) {
		this.thumbprint = thumbprint;
	}

	public void setKeyUsage(KeyUsage keyUsageObject) {
		this.keyUsage = keyUsageObject;
	}

	public void setExtendedKeyUsage(ExtendedKeyUsage extendedKeyUsageObject) {
		this.extendedKeyUsage = extendedKeyUsageObject;
	}

	public void setCanRemove(Boolean canRemove) {
		this.canRemove = canRemove;
	}

	public void setIsRemote(Boolean isRemote) {
		this.isRemote = isRemote;
	}

	public void setValidityStart(Date validityStart) {
		this.validityStart = validityStart;
	}

	public void setValidityEnd(Date validityEnd) {
		this.validityEnd = validityEnd;
	}

	public void setCertificatePolicies(List<Object> certificatePolicies) {
		this.certificatePolicies = certificatePolicies;
	}

	public void set$$hashKey(String $$hashKey) {
		this.$$hashKey = $$hashKey;
	}

	public void set$$mdSelectId(float $$mdSelectId) {
		this.$$mdSelectId = $$mdSelectId;
	}

	@Override
	public String toString() {
		String string = "";
		if (Objects.nonNull(getSubjectName())) {
			string += "Subject Name: " + getSubjectName() + ", ";
		}
		if (Objects.nonNull(getSubjectDN())) {
			string += "Subject DN: " + getSubjectDN() + ", ";
		}
		if (Objects.nonNull(getIssuerName())) {
			string += "Issuer Name: " + getIssuerName() + ", ";
		}
		if (Objects.nonNull(getIssuerDN())) {
			string += "Issuer DN: " + getIssuerDN() + ", ";
		}
		if (Objects.nonNull(getEmail())) {
			string += "Email: " + getEmail() + ", ";
		}
		if (Objects.nonNull(getThumbprint())) {
			string += "Thumbprint: " + getThumbprint() + ", ";
		}
		if (Objects.nonNull(getPkiBrazil())) {
			string += "PKI BRAZIL: " + getPkiBrazil().toString() + ", ";
		}
		return string;
	}

	public static class KeyUsage {

		@JsonProperty("crlSign")
		private Boolean crlSign;
		@JsonProperty("dataEncipherment")
		private Boolean dataEncipherment;
		@JsonProperty("decipherOnly")
		private Boolean decipherOnly;
		@JsonProperty("digitalSignature")
		private Boolean digitalSignature;
		@JsonProperty("encipherOnly")
		private Boolean encipherOnly;
		@JsonProperty("keyAgreement")
		private Boolean keyAgreement;
		@JsonProperty("keyCertSign")
		private Boolean keyCertSign;
		@JsonProperty("keyEncipherment")
		private Boolean keyEncipherment;
		@JsonProperty("nonRepudiation")
		private Boolean nonRepudiation;

		public Boolean getCrlSign() {
			return crlSign;
		}

		public Boolean getDataEncipherment() {
			return dataEncipherment;
		}

		public Boolean getDecipherOnly() {
			return decipherOnly;
		}

		public Boolean getDigitalSignature() {
			return digitalSignature;
		}

		public Boolean getEncipherOnly() {
			return encipherOnly;
		}

		public Boolean getKeyAgreement() {
			return keyAgreement;
		}

		public Boolean getKeyCertSign() {
			return keyCertSign;
		}

		public Boolean getKeyEncipherment() {
			return keyEncipherment;
		}

		public Boolean getNonRepudiation() {
			return nonRepudiation;
		}

		public void setCrlSign(Boolean crlSign) {
			this.crlSign = crlSign;
		}

		public void setDataEncipherment(Boolean dataEncipherment) {
			this.dataEncipherment = dataEncipherment;
		}

		public void setDecipherOnly(Boolean decipherOnly) {
			this.decipherOnly = decipherOnly;
		}

		public void setDigitalSignature(Boolean digitalSignature) {
			this.digitalSignature = digitalSignature;
		}

		public void setEncipherOnly(Boolean encipherOnly) {
			this.encipherOnly = encipherOnly;
		}

		public void setKeyAgreement(Boolean keyAgreement) {
			this.keyAgreement = keyAgreement;
		}

		public void setKeyCertSign(Boolean keyCertSign) {
			this.keyCertSign = keyCertSign;
		}

		public void setKeyEncipherment(Boolean keyEncipherment) {
			this.keyEncipherment = keyEncipherment;
		}

		public void setNonRepudiation(Boolean nonRepudiation) {
			this.nonRepudiation = nonRepudiation;
		}
	}

	public static class PkiBrazil {

		@JsonProperty("cpf")
		private String cpf;
		@JsonProperty("cnpj")
		private String cnpj;
		@JsonProperty("responsavel")
		private String responsavel;
		@JsonProperty("dateOfBirth")
		private String dateOfBirth;
		@JsonProperty("certificateType")
		private String certificateType;
		@JsonProperty("isAplicacao")
		private Boolean isAplicacao;
		@JsonProperty("isPessoaFisica")
		private Boolean isPessoaFisica;
		@JsonProperty("isPessoaJuridica")
		private Boolean isPessoaJuridica;
		@JsonProperty("companyName")
		private String companyName;
		@JsonProperty("nis")
		private String nis;
		@JsonProperty("rgEmissor")
		private String rgEmissor;
		@JsonProperty("rgEmissorUF")
		private String rgEmissorUF;
		@JsonProperty("rgNumero")
		private String rgNumero;
		@JsonProperty("oabNumero")
		private String oabNumero;
		@JsonProperty("oabUF")
		private String oabUF;

		public String getCpf() {
			return cpf;
		}

		public String getCnpj() {
			return cnpj;
		}

		public String getResponsavel() {
			return responsavel;
		}

		public String getDateOfBirth() {
			return dateOfBirth;
		}

		public String getCertificateType() {
			return certificateType;
		}

		public Boolean getIsAplicacao() {
			return isAplicacao;
		}

		public Boolean getIsPessoaFisica() {
			return isPessoaFisica;
		}

		public Boolean getIsPessoaJuridica() {
			return isPessoaJuridica;
		}

		public String getCompanyName() {
			return companyName;
		}

		public String getNis() {
			return nis;
		}

		public String getRgEmissor() {
			return rgEmissor;
		}

		public String getRgEmissorUF() {
			return rgEmissorUF;
		}

		public String getRgNumero() {
			return rgNumero;
		}

		public String getOabNumero() {
			return oabNumero;
		}

		public String getOabUF() {
			return oabUF;
		}

		public void setCpf(String cpf) {
			this.cpf = cpf;
		}

		public void setCnpj(String cnpj) {
			this.cnpj = cnpj;
		}

		public void setResponsavel(String responsavel) {
			this.responsavel = responsavel;
		}

		public void setDateOfBirth(String dateOfBirth) {
			this.dateOfBirth = dateOfBirth;
		}

		public void setCertificateType(String certificateType) {
			this.certificateType = certificateType;
		}

		public void setIsAplicacao(Boolean isAplicacao) {
			this.isAplicacao = isAplicacao;
		}

		public void setIsPessoaFisica(Boolean isPessoaFisica) {
			this.isPessoaFisica = isPessoaFisica;
		}

		public void setIsPessoaJuridica(Boolean isPessoaJuridica) {
			this.isPessoaJuridica = isPessoaJuridica;
		}

		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}

		public void setNis(String nis) {
			this.nis = nis;
		}

		public void setRgEmissor(String rgEmissor) {
			this.rgEmissor = rgEmissor;
		}

		public void setRgEmissorUF(String rgEmissorUF) {
			this.rgEmissorUF = rgEmissorUF;
		}

		public void setRgNumero(String rgNumero) {
			this.rgNumero = rgNumero;
		}

		public void setOabNumero(String oabNumero) {
			this.oabNumero = oabNumero;
		}

		public void setOabUF(String oabUF) {
			this.oabUF = oabUF;
		}

		@Override
		public String toString() {
			String string = "";
			if (Objects.nonNull(getCpf())) {
				string += "CPF: " + getCpf() + ", ";
			}
			if (Objects.nonNull(getCnpj())) {
				string += "CNPJ: " + getCnpj() + ", ";
			}
			if (Objects.nonNull(getResponsavel())) {
				string += "Responsavel: " + getResponsavel() + ", ";
			}
			if (Objects.nonNull(getCertificateType())) {
				string += "Certificate Type: " + getCertificateType() + ", ";
			}
			return string;
		}
	}

}