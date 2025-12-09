/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package negocio.comuns.administrativo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * 
 * @author Mauro
 */
public class PersonalizacaoMensagemAutomaticaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private Integer codigo;
	private String assunto;
	private String tags;
	private String mensagem;
	private Boolean desabilitarEnvioMensagemAutomatica;
	private TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum;
	private String caminhoImagemPadraoCima;
	private String caminhoImagemPadraoBaixo;
	private String imgCima;
	private String imgBaixo;
	private Boolean desabilitarEnvioMensagemSMSAutomatica;
	private String mensagemSMS;
	private Boolean copiaFollowUp;
	private Boolean enviarCopiaPais;
	private Boolean nivelEducacionalInfantil;
	private Boolean nivelEducacionalBasico;
	private Boolean nivelEducacionalMedio;
	private Boolean nivelEducacionalExtensao;
	
	private Boolean nivelEducacionalSequencial;
	private Boolean nivelEducacionalGraduacaoTecnologica;
	private Boolean nivelEducacionalSuperior;
	private Boolean nivelEducacionalPosGraduacao;
	private Boolean nivelEducacionalMestrado;
	private Boolean nivelEducacionalProfissionalizante;
	private List<PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO> personalizacaoMensagemAutomaticaUnidadeEnsinoVOs;
	private UsuarioVO usuarioUltimaAlteracao;
	private Date dataUltimaAlteracao;
	private Boolean enviarEmailInstitucional;
	private CursoVO cursoVO;
	private Boolean notificacaoConfirmacaoNovaMatricula;
	private Boolean notificacaoRenovacaoMatricula;
	private Boolean enviarEmail;
	private Integer tipoRequerimento;
	// atributo transient
	private Boolean viaTipoRequerimento;
	
	public String getAssunto() {
		if (assunto == null) {
			assunto = "";
		}
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public Boolean getDesabilitarEnvioMensagemAutomatica() {
		if (desabilitarEnvioMensagemAutomatica == null) {
			desabilitarEnvioMensagemAutomatica = Boolean.TRUE;
		}
		return desabilitarEnvioMensagemAutomatica;
	}

	public void setDesabilitarEnvioMensagemAutomatica(Boolean desabilitarEnvioMensagemAutomatica) {
		this.desabilitarEnvioMensagemAutomatica = desabilitarEnvioMensagemAutomatica;
	}

	public String getMensagem() {
		if (mensagem == null) {
			mensagem = "";
		}
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getTags() {
		if (tags == null) {
			tags = "";
		}
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public TemplateMensagemAutomaticaEnum getTemplateMensagemAutomaticaEnum() {
		// if (templateMensagemAutomaticaEnum == null) {
		// templateMensagemAutomaticaEnum =
		// TemplateMensagemAutomaticaEnum.MENSAGEM_EMPRESTIMO_DEVOLUCAO;
		// }
		return templateMensagemAutomaticaEnum;
	}

	public void setTemplateMensagemAutomaticaEnum(TemplateMensagemAutomaticaEnum templateMensagemAutomaticaEnum) {
		this.templateMensagemAutomaticaEnum = templateMensagemAutomaticaEnum;
	}

	public String getCaminhoImagemPadraoCima() {
		if (caminhoImagemPadraoCima == null) {
			caminhoImagemPadraoCima = "";
		}
		return caminhoImagemPadraoCima;
	}

	public void setCaminhoImagemPadraoCima(String caminhoImagemPadraoCima) {

		this.caminhoImagemPadraoCima = caminhoImagemPadraoCima;
	}

	public String getCaminhoImagemPadraoBaixo() {
		if (caminhoImagemPadraoBaixo == null) {
			caminhoImagemPadraoBaixo = "";
		}
		return caminhoImagemPadraoBaixo;
	}

	public void setCaminhoImagemPadraoBaixo(String caminhoImagemPadraoBaixo) {
		this.caminhoImagemPadraoBaixo = caminhoImagemPadraoBaixo;
	}

	public String getImgCima() {
		if (imgCima == null) {
			imgCima = "";
		}
		return imgCima;
	}

	public void setImgCima(String imgCima) {
		this.imgCima = imgCima;
	}

	public String getImgBaixo() {
		if (imgBaixo == null) {
			imgBaixo = "";
		}
		return imgBaixo;
	}

	public void setImgBaixo(String imgBaixo) {
		this.imgBaixo = imgBaixo;
	}

	public String getMensagemComLayout(String mensagem) {

		// obeter o ip ou dns;
		// String dominio = UteisEmail.getURLAplicacao("/imagens/email/");
		String dominio = null;
		try {
			dominio = "../resources/imagens/email/";
		} catch (Exception e) {
			// //System.out.println("Comunicacao Erro:" + e.getMessage());
		}
		setCaminhoImagemPadraoCima(dominio + "cima_sei.jpg");
		setCaminhoImagemPadraoBaixo(dominio + "baixo_sei.jpg");

		StringBuilder sb = new StringBuilder();
//		sb.append("<html><head><meta http-equiv=\"Content-Type\" content=\"text/xhtml; charset=UTF-8\" /></head><body>");
//		sb.append("<div class=\"Section1\">");
//		sb.append("<div>");
//		sb.append("<table class=\"MsoNormalTable\" style=\"mso-cellspacing: 0cm; mso-yfti-tbllook: 1184; mso-padding-alt: 0cm 0cm 0cm 0cm;\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\" width=\"300\">");
//		sb.append("<tbody>");
//		sb.append("<tr style=\"height: 59.25pt; mso-yfti-irow: 0; mso-yfti-firstrow: yes;\">");
//		sb.append("<td style=\"height: 59.25pt; padding: 0cm;\" colspan=\"3\">");
//		sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt; mso-no-proof: yes;\">");
		sb.append("<p><img id=\"_x0000_i1025\" src=\"" + getCaminhoImagemPadraoCima() + "\" alt=\"" + getImgCima() + "\" width=\"750px\" border=\"0\" /></p>");
//		sb.append("</span></p>");
//		sb.append("</td>");
//		sb.append("</tr>");
//		sb.append("<tr style=\"mso-yfti-irow:2\">");
//		sb.append("<td style=\"background: #FFFFFF; padding: 0cm;\" width=\"650px\">");
		sb.append("<p><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
		sb.append("<p><span style=\"font-family: Arial; color: black; font-size: 10pt;\"><texto></span></p>");
		sb.append("<p><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
		sb.append("<p><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
		sb.append("<p>" + mensagem + "<span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
		sb.append("<p><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
		sb.append("<p><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
		sb.append("<p><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");
		sb.append("<p><span style=\"font-family: Arial; color: black; font-size: 10pt;\">&nbsp;</span></p>");

//		sb.append("</span></td>");
//		sb.append("</tr>");
//		sb.append("<tr style=\"height: 43.5pt; mso-yfti-irow: 4; mso-yfti-lastrow: yes;\">");
//		sb.append("<td style=\"height: 43.5pt; padding: 0cm;\" colspan=\"3\">");
//		sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"font-family: Arial; color: black; font-size: 10pt; mso-no-proof: yes;\">");
		sb.append("<p><img id=\"_x0000_i1028\" src=\"" + getCaminhoImagemPadraoBaixo() + "\" border=\"0\" alt=\"" + getImgBaixo() + "\" width=\"750px\" border=\"0\" /></p>");
//		sb.append("</td>");
//		sb.append("</tr>");
//		sb.append("</tbody>");
//		sb.append("</table>");
//		sb.append("</div>");
//		sb.append("<p class=\"MsoNormal\" style=\"margin:0cm;margin-bottom:.0001pt\"><span style=\"color: black;\">&nbsp;</span></p>");
//		sb.append("</div>");
//		sb.append("</body></html>");
		return sb.toString();
	}

	public String getApresentarTemplateMensagemAutomaticas() {
		return UteisJSF.internacionalizar("enum_TemplateMensagemAutomaticaEnum_" + templateMensagemAutomaticaEnum.toString());
	}

	public Boolean getDesabilitarEnvioMensagemSMSAutomatica() {
		if (desabilitarEnvioMensagemSMSAutomatica == null) {
			desabilitarEnvioMensagemSMSAutomatica = Boolean.TRUE;
		}
		return desabilitarEnvioMensagemSMSAutomatica;
	}

	public void setDesabilitarEnvioMensagemSMSAutomatica(Boolean desabilitarEnvioMensagemSMSAutomatica) {
		this.desabilitarEnvioMensagemSMSAutomatica = desabilitarEnvioMensagemSMSAutomatica;
	}

	public String getMensagemSMS() {
		if (mensagemSMS == null) {
			mensagemSMS = "";
		}
		return mensagemSMS;
	}

	public void setMensagemSMS(String mensagemSMS) {
		this.mensagemSMS = mensagemSMS;
	}

	public Boolean getCopiaFollowUp() {
		if (copiaFollowUp == null) {
			copiaFollowUp = Boolean.FALSE;
		}
		return copiaFollowUp;
	}

	public void setCopiaFollowUp(Boolean copiaFollowUp) {
		this.copiaFollowUp = copiaFollowUp;
	}

	public Boolean getEnviarCopiaPais() {
		if (enviarCopiaPais == null) {
			enviarCopiaPais = false;
		}
		return enviarCopiaPais;
	}

	public void setEnviarCopiaPais(Boolean enviarCopiaPais) {
		this.enviarCopiaPais = enviarCopiaPais;
	}

	public Boolean getNivelEducacionalInfantil() {
		if(nivelEducacionalInfantil == null) {
			nivelEducacionalInfantil = Boolean.FALSE;
		}
		return nivelEducacionalInfantil;
	}

	public void setNivelEducacionalInfantil(Boolean nivelEducacionalInfantil) {
		this.nivelEducacionalInfantil = nivelEducacionalInfantil;
	}

	public Boolean getNivelEducacionalBasico() {
		if(nivelEducacionalBasico == null) {
			nivelEducacionalBasico = Boolean.FALSE;
		}
		return nivelEducacionalBasico;
	}

	public void setNivelEducacionalBasico(Boolean nivelEducacionalBasico) {
		this.nivelEducacionalBasico = nivelEducacionalBasico;
	}

	public Boolean getNivelEducacionalMedio() {
		if(nivelEducacionalMedio == null) {
			nivelEducacionalMedio = Boolean.FALSE;
		}
		return nivelEducacionalMedio;
	}

	public void setNivelEducacionalMedio(Boolean nivelEducacionalMedio) {
		this.nivelEducacionalMedio = nivelEducacionalMedio;
	}

	public Boolean getNivelEducacionalExtensao() {
		if(nivelEducacionalExtensao == null) {
			nivelEducacionalExtensao = Boolean.FALSE;
		}
		return nivelEducacionalExtensao;
	}

	public void setNivelEducacionalExtensao(Boolean nivelEducacionalExtensao) {
		this.nivelEducacionalExtensao = nivelEducacionalExtensao;
	}

	public Boolean getNivelEducacionalSequencial() {
		if(nivelEducacionalSequencial == null) {
			nivelEducacionalSequencial = Boolean.FALSE;
		}
		return nivelEducacionalSequencial;
	}

	public void setNivelEducacionalSequencial(Boolean nivelEducacionalSequencial) {
		this.nivelEducacionalSequencial = nivelEducacionalSequencial;
	}

	public Boolean getNivelEducacionalGraduacaoTecnologica() {
		if(nivelEducacionalGraduacaoTecnologica == null) {
			nivelEducacionalGraduacaoTecnologica = Boolean.FALSE;
		}
		return nivelEducacionalGraduacaoTecnologica;
	}

	public void setNivelEducacionalGraduacaoTecnologica(Boolean nivelEducacionalGraduacaoTecnologica) {
		this.nivelEducacionalGraduacaoTecnologica = nivelEducacionalGraduacaoTecnologica;
	}

	public Boolean getNivelEducacionalSuperior() {
		if(nivelEducacionalSuperior == null) {
			nivelEducacionalSuperior = Boolean.FALSE;
		}
		return nivelEducacionalSuperior;
	}

	public void setNivelEducacionalSuperior(Boolean nivelEducacionalSuperior) {
		this.nivelEducacionalSuperior = nivelEducacionalSuperior;
	}

	public Boolean getNivelEducacionalPosGraduacao() {
		if(nivelEducacionalPosGraduacao == null) {
			nivelEducacionalPosGraduacao = Boolean.FALSE;
		}
		return nivelEducacionalPosGraduacao;
	}

	public void setNivelEducacionalPosGraduacao(Boolean nivelEducacionalPosGraduacao) {
		this.nivelEducacionalPosGraduacao = nivelEducacionalPosGraduacao;
	}

	public Boolean getNivelEducacionalMestrado() {
		if(nivelEducacionalMestrado == null) {
			nivelEducacionalMestrado = Boolean.FALSE;
		}
		return nivelEducacionalMestrado;
	}

	public void setNivelEducacionalMestrado(Boolean nivelEducacionalMestrado) {
		this.nivelEducacionalMestrado = nivelEducacionalMestrado;
	}

	public Boolean getNivelEducacionalProfissionalizante() {
		if(nivelEducacionalProfissionalizante == null) {
			nivelEducacionalProfissionalizante = Boolean.FALSE;
		}
		return nivelEducacionalProfissionalizante;
	}

	public void setNivelEducacionalProfissionalizante(Boolean nivelEducacionalProfissionalizante) {
		this.nivelEducacionalProfissionalizante = nivelEducacionalProfissionalizante;
	}

	public List<PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO> getPersonalizacaoMensagemAutomaticaUnidadeEnsinoVOs() {
		if(personalizacaoMensagemAutomaticaUnidadeEnsinoVOs == null) {
			personalizacaoMensagemAutomaticaUnidadeEnsinoVOs = new ArrayList<PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO>(0);
		}
		return personalizacaoMensagemAutomaticaUnidadeEnsinoVOs;
	}

	public void setPersonalizacaoMensagemAutomaticaUnidadeEnsinoVOs(
			List<PersonalizacaoMensagemAutomaticaUnidadeEnsinoVO> personalizacaoMensagemAutomaticaUnidadeEnsinoVOs) {
		this.personalizacaoMensagemAutomaticaUnidadeEnsinoVOs = personalizacaoMensagemAutomaticaUnidadeEnsinoVOs;
	}
	
	public UsuarioVO getUsuarioUltimaAlteracao() {
		if (usuarioUltimaAlteracao == null) {
			usuarioUltimaAlteracao = new UsuarioVO();
		}
		return usuarioUltimaAlteracao;
	}
	
	public void setUsuarioUltimaAlteracao(UsuarioVO usuarioUltimaAlteracao) {
		this.usuarioUltimaAlteracao = usuarioUltimaAlteracao;
	}
	
	public String getDataUltimaAlteracaoApresentar() {
		return Uteis.getDataComHora(getDataUltimaAlteracao());
	}
	
	public Date getDataUltimaAlteracao() {
		if (dataUltimaAlteracao == null) {
			dataUltimaAlteracao = new Date();
		}
		return dataUltimaAlteracao;
	}
	
	public void setDataUltimaAlteracao(Date dataUltimaAlteracao) {
		this.dataUltimaAlteracao = dataUltimaAlteracao;
	}

	public Boolean getEnviarEmailInstitucional() {
		if(enviarEmailInstitucional == null) {
			enviarEmailInstitucional = Boolean.FALSE;
		}
		return enviarEmailInstitucional;
	}

	public void setEnviarEmailInstitucional(Boolean enviarEmailInstitucional) {
		this.enviarEmailInstitucional = enviarEmailInstitucional;
	}

	public Boolean getNotificacaoConfirmacaoNovaMatricula() {
		if (notificacaoConfirmacaoNovaMatricula == null) {
			notificacaoConfirmacaoNovaMatricula = false;
		}
		return notificacaoConfirmacaoNovaMatricula;
	}

	public void setNotificacaoConfirmacaoNovaMatricula(Boolean notificacaoConfirmacaoNovaMatricula) {
		this.notificacaoConfirmacaoNovaMatricula = notificacaoConfirmacaoNovaMatricula;
	}

	public Boolean getNotificacaoRenovacaoMatricula() {
		if (notificacaoRenovacaoMatricula == null) {
			notificacaoRenovacaoMatricula = false;
		}
		return notificacaoRenovacaoMatricula;
	}

	public void setNotificacaoRenovacaoMatricula(Boolean notificacaoRenovacaoMatricula) {
		this.notificacaoRenovacaoMatricula = notificacaoRenovacaoMatricula;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public Boolean getEnviarEmail() {
		if (enviarEmail== null) {
			enviarEmail = Boolean.TRUE;
		}
		return enviarEmail;
	}

	public void setEnviarEmail(Boolean enviarEmail) {
		this.enviarEmail = enviarEmail;
	}

	public int getCount() {
		return getMensagemSMS().length();
	}

	public Integer getTipoRequerimento() {
		if (tipoRequerimento == null) {
			tipoRequerimento = 0;
		}
		return tipoRequerimento;
	}

	public void setTipoRequerimento(Integer tipoRequerimento) {
		this.tipoRequerimento = tipoRequerimento;
	}

	public Boolean getViaTipoRequerimento() {
		if (viaTipoRequerimento == null) {
			viaTipoRequerimento = Boolean.FALSE;
		}
		return viaTipoRequerimento;
	}

	public void setViaTipoRequerimento(Boolean viaTipoRequerimento) {
		this.viaTipoRequerimento = viaTipoRequerimento;
	}

	public boolean isNivelMontarDadosBasicos() {
		return Uteis.isAtributoPreenchido(getNivelMontarDados()) && getNivelMontarDados().equals(NivelMontarDados.BASICO);
	}

	public boolean isNivelMontarDadosTodos() {
		return Uteis.isAtributoPreenchido(getNivelMontarDados()) && getNivelMontarDados().equals(NivelMontarDados.TODOS);
	}
	
	public void validarDados() throws Exception {
		Uteis.checkState(!Uteis.isAtributoPreenchido(getTemplateMensagemAutomaticaEnum()), "O campo TEMPLATE da mensagem automática deve ser informado, template: " + getTemplateMensagemAutomaticaEnum().getModuloTemplate());
		Uteis.checkState(!Uteis.isAtributoPreenchido(getAssunto()), "O campo ASSUNTO da mensagem automática deve ser informado, template: " + UteisJSF.internacionalizar("enum_TemplateMensagemAutomaticaEnum_" + getTemplateMensagemAutomaticaEnum().toString()));
	}
}
