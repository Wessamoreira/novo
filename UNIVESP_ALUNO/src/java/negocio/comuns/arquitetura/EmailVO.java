/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.arquitetura;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author PEDRO
 */
public class EmailVO extends SuperVO {

    private Integer codigo;
    private String emailDest;
    private String nomeDest;
    private String emailRemet;
    private String nomeRemet;
    private String assunto;
    private String mensagem;
    private String caminhoAnexos;
    private Date dataCadastro;
    private Boolean anexarImagensPadrao;
    private Boolean redefinirSenha;
    private String caminhoLogoEmailCima;
	private String caminhoLogoEmailBaixo;
	private String erro;
	private Boolean multiplosDestinatarios;
	private String emailDestMultiplosDestinatarios;

    public Boolean getAnexarImagensPadrao() {
        if (anexarImagensPadrao == null) {
            anexarImagensPadrao = false;
        }
        return anexarImagensPadrao;
    }

    public void setAnexarImagensPadrao(Boolean anexarImagensPadrao) {
        this.anexarImagensPadrao = anexarImagensPadrao;
    }

    public Date getDataCadastro() {
        if (dataCadastro == null) {
            dataCadastro = new Date();
        }
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getAssunto() {
        if (assunto == null) {
            assunto = "";
        }
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
    }

    public String getCaminhoAnexos() {
        if (caminhoAnexos == null) {
            caminhoAnexos = "";
        }
        return caminhoAnexos;
    }

    public void setCaminhoAnexos(String caminhoAnexos) {
        this.caminhoAnexos = caminhoAnexos;
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

    public String getEmailDest() {
        if (emailDest == null) {
            emailDest = "";
        }
        return emailDest;
    }

    public void setEmailDest(String emailDest) {
        this.emailDest = emailDest;
    }

    public String getEmailRemet() {
        if (emailRemet == null) {
            emailRemet = "";
        }
        return emailRemet;
    }

    public void setEmailRemet(String emailRemet) {
        this.emailRemet = emailRemet;
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

    public String getNomeDest() {
        if (nomeDest == null) {
            nomeDest = "";
        }
        return nomeDest;
    }

    public void setNomeDest(String nomeDest) {
        this.nomeDest = nomeDest;
    }

    public String getNomeRemet() {
        if (nomeRemet == null) {
            nomeRemet = "";
        }
        return nomeRemet;
    }

    public void setNomeRemet(String nomeRemet) {
        this.nomeRemet = nomeRemet;
    }

    public Boolean getRedefinirSenha() {
        if (redefinirSenha == null) {
            redefinirSenha = false;
        }
        return redefinirSenha;
    }

    public void setRedefinirSenha(Boolean redefinirSenha) {
        this.redefinirSenha = redefinirSenha;
    }
	
    public String getDataCadastro_Apresentar() {
    	return Uteis.getData(getDataCadastro());
    }
    
    public String getCaminhoLogoEmailCima() {
		if (caminhoLogoEmailCima == null) {
			caminhoLogoEmailCima = "";
		}
		return caminhoLogoEmailCima;
	}

	public void setCaminhoLogoEmailCima(String caminhoLogoEmailCima) {
		this.caminhoLogoEmailCima = caminhoLogoEmailCima;
	}

	public String getCaminhoLogoEmailBaixo() {
		if (caminhoLogoEmailBaixo == null) {
			caminhoLogoEmailBaixo = "";
		}
		return caminhoLogoEmailBaixo;
	}

	public void setCaminhoLogoEmailBaixo(String caminhoLogoEmailBaixo) {
		this.caminhoLogoEmailBaixo = caminhoLogoEmailBaixo;
	}

	public String getErro() {
		if (erro == null) {
			erro = "";
		}
		return erro;
	}

	public void setErro(String erro) {
		this.erro = erro;
	}
	
	public Boolean getMultiplosDestinatarios() {
		if (multiplosDestinatarios == null) {
			multiplosDestinatarios = false;
		}
		return multiplosDestinatarios;
	}

	public void setMultiplosDestinatarios(Boolean multiplosDestinatarios) {
		this.multiplosDestinatarios = multiplosDestinatarios;
	}

	public String getEmailDestMultiplosDestinatarios() {
		if (emailDestMultiplosDestinatarios == null) {
			emailDestMultiplosDestinatarios = "";
		}
		return emailDestMultiplosDestinatarios;
	}

	public void setEmailDestMultiplosDestinatarios(String emailDestMultiplosDestinatarios) {
		this.emailDestMultiplosDestinatarios = emailDestMultiplosDestinatarios;
	}

	public EmailVO realizarMontagemJsonEmailMultiplosDestinatarios(Map<String, List<String>> mapDestinatarioEmails) {
		if (Uteis.isAtributoPreenchido(mapDestinatarioEmails)) {
			try {
				this.setEmailDest(new ObjectMapper().writeValueAsString(mapDestinatarioEmails));
			} catch (Exception e) {
				this.setEmailDest("");
			}
		}
		return this;
	}

	public String getEmailDestinatarioApresentar() {
		return getMultiplosDestinatarios() ? getEmailDestMultiplosDestinatarios() : getEmailDest();
	}
}
