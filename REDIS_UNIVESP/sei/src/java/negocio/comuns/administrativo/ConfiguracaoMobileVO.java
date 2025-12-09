package negocio.comuns.administrativo;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;

/**
 * @author Victor Hugo de Paula Costa - 3 de nov de 2016
 *
 */
public class ConfiguracaoMobileVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	/**
	 * @author Victor Hugo de Paula Costa - 3 de nov de 2016
	 */
	private Integer codigo;
	private String nome;
	
	private String idRemetenteGoogle;
	private String idRemetenteGoogleProfessor;
	private byte[] certificadoAPNSApple;
	private byte[] certificadoAPNSAppleProfessor;
	private SituacaoEnum situacao;
	private String senhaCertificadoApns;
	private String senhaCertificadoApnsProfessor;
	private Boolean certificadoDestribuicao;
	private Boolean certificadoDestribuicaoProfessor;
	private Boolean padrao;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public String getIdRemetenteGoogle() {
		if (idRemetenteGoogle == null) {
			idRemetenteGoogle = "";
		}
		return idRemetenteGoogle;
	}

	public void setIdRemetenteGoogle(String idRemetenteGoogle) {
		this.idRemetenteGoogle = idRemetenteGoogle;
	}

	public byte[] getCertificadoAPNSApple() {
		if (certificadoAPNSApple == null) {
			certificadoAPNSApple = new byte[0];
		}
		return certificadoAPNSApple;
	}

	public void setCertificadoAPNSApple(byte[] certificadoAPNSApple) {
		this.certificadoAPNSApple = certificadoAPNSApple;
	}

	public SituacaoEnum getSituacao() {
		if (situacao == null) {
			situacao = SituacaoEnum.EM_CONSTRUCAO;
		}
		return situacao;
	}

	public void setSituacao(SituacaoEnum situacao) {
		this.situacao = situacao;
	}

	public String getNome() {
		if (nome == null) {
			nome = "";
		}
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
	
	public String getSenhaCertificadoApns() {
		if(senhaCertificadoApns == null){
			senhaCertificadoApns = "";
		}
		return senhaCertificadoApns;
	}

	public void setSenhaCertificadoApns(String senhaCertificadoApns) {
		this.senhaCertificadoApns = senhaCertificadoApns;
	}

	public Boolean getCertificadoDestribuicao() {
		if(certificadoDestribuicao == null){
			certificadoDestribuicao = false;
		}
		return certificadoDestribuicao;
	}

	public void setCertificadoDestribuicao(Boolean certificadoDestribuicao) {
		this.certificadoDestribuicao = certificadoDestribuicao;
	}

	/**
	 * @return the idRemetenteGoogleProfessor
	 */
	public String getIdRemetenteGoogleProfessor() {
		if (idRemetenteGoogleProfessor == null) {
			idRemetenteGoogleProfessor = "";
		}
		return idRemetenteGoogleProfessor;
	}

	/**
	 * @param idRemetenteGoogleProfessor the idRemetenteGoogleProfessor to set
	 */
	public void setIdRemetenteGoogleProfessor(String idRemetenteGoogleProfessor) {
		this.idRemetenteGoogleProfessor = idRemetenteGoogleProfessor;
	}

	/**
	 * @return the certificadoAPNSAppleProfessor
	 */
	public byte[] getCertificadoAPNSAppleProfessor() {
		if (certificadoAPNSAppleProfessor == null) {
			certificadoAPNSAppleProfessor = new byte[0];
		}
		return certificadoAPNSAppleProfessor;
	}

	/**
	 * @param certificadoAPNSAppleProfessor the certificadoAPNSAppleProfessor to set
	 */
	public void setCertificadoAPNSAppleProfessor(byte[] certificadoAPNSAppleProfessor) {
		this.certificadoAPNSAppleProfessor = certificadoAPNSAppleProfessor;
	}

	/**
	 * @return the senhaCertificadoApnsProfessor
	 */
	public String getSenhaCertificadoApnsProfessor() {
		if(senhaCertificadoApnsProfessor == null){
			senhaCertificadoApnsProfessor = "";
		}
		return senhaCertificadoApnsProfessor;
	}

	/**
	 * @param senhaCertificadoApnsProfessor the senhaCertificadoApnsProfessor to set
	 */
	public void setSenhaCertificadoApnsProfessor(String senhaCertificadoApnsProfessor) {
		this.senhaCertificadoApnsProfessor = senhaCertificadoApnsProfessor;
	}

	/**
	 * @return the certificadoDestribuicaoProfessor
	 */
	public Boolean getCertificadoDestribuicaoProfessor() {
		if(certificadoDestribuicaoProfessor == null){
			certificadoDestribuicaoProfessor = false;
		}
		return certificadoDestribuicaoProfessor;
	}

	/**
	 * @param certificadoDestribuicaoProfessor the certificadoDestribuicaoProfessor to set
	 */
	public void setCertificadoDestribuicaoProfessor(Boolean certificadoDestribuicaoProfessor) {
		this.certificadoDestribuicaoProfessor = certificadoDestribuicaoProfessor;
	}

	public Boolean getPadrao() {
		if(padrao == null){
			padrao = false;
		}
		return padrao;
	}

	public void setPadrao(Boolean padrao) {
		this.padrao = padrao;
	}
	
}
