package negocio.comuns.gsuite;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonBackReference;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.annotation.ExcluirJsonAnnotation;
import negocio.comuns.utilitarias.Uteis;

@XmlRootElement(name = "googleMeetConvidadoVO")
public class GoogleMeetConvidadoVO extends SuperVO {

	private static final long serialVersionUID = -8627491299086238183L;

	private Integer codigo;	
	@JsonBackReference
	private GoogleMeetVO googleMeetVO;
	private PessoaGsuiteVO pessoaGsuiteVO;
	/**
	 * Atributo Transient
	 */
	@ExcluirJsonAnnotation
	private String matricula;

	@XmlElement(name = "codigo")
	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public GoogleMeetVO getGoogleMeetVO() {
		if (googleMeetVO == null) {
			googleMeetVO = new GoogleMeetVO();
		}
		return googleMeetVO;
	}

	public void setGoogleMeetVO(GoogleMeetVO googleMeetVO) {
		this.googleMeetVO = googleMeetVO;
	}

	@XmlElement(name = "pessoaGsuiteVO")
	public PessoaGsuiteVO getPessoaGsuiteVO() {
		if (pessoaGsuiteVO == null) {
			pessoaGsuiteVO = new PessoaGsuiteVO();
		}
		return pessoaGsuiteVO;
	}

	public void setPessoaGsuiteVO(PessoaGsuiteVO pessoaGsuiteVO) {
		this.pessoaGsuiteVO = pessoaGsuiteVO;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}
	
	public String getConviteEnviado() {
		return isExistePessoaSuite() && !getGoogleMeetVO().getLinkGoogleMeet().isEmpty() ? "Sim" :"Não";
	}
	
	public boolean isExistePessoaSuite() {
		return Uteis.isAtributoPreenchido(getCodigo());
	}
	
	

}
