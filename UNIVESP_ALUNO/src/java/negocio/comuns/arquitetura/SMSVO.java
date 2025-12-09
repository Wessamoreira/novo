/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.arquitetura;

import java.util.Date;

import negocio.comuns.utilitarias.Uteis;
/**
 *
 * @author PEDRO
 */
public class SMSVO extends SuperVO {

    private Integer codigo;
    private String assunto;
    private String mensagem;
    private String celular;
    private Date dataCadastro;
    private Boolean enviouSMS;
    private String msgEnvioSMS;
    private String nomeDest;
    private String cpfDest;
    private String codigoDest;
    private String matriculaDest;
        

    /**
	 * 
	 */
	public SMSVO() {
		super();
	}
	
	

	/**
	 * @param mensagem
	 * @param celular
	 * @param nomeDest
	 */
	public SMSVO(String mensagem, String celular, String nomeDest) {
		super();
		this.mensagem = mensagem;
		this.celular = celular;
		this.nomeDest = nomeDest;
	}

	public String getDataCadastro_Apresentar() {
		return Uteis.getData(getDataCadastro());
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

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
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

	public String getCelular() {
		if (celular == null) {
			celular = "";
		}
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public Boolean getEnviouSMS() {
		if (enviouSMS == null) {
			enviouSMS = Boolean.FALSE;
		}
		return enviouSMS;
	}

	public void setEnviouSMS(Boolean enviouSMS) {
		this.enviouSMS = enviouSMS;
	}

	public String getMsgEnvioSMS() {
		if (msgEnvioSMS == null) {
			msgEnvioSMS = "";
		}
		return msgEnvioSMS;
	}

	public void setMsgEnvioSMS(String msgEnvioSMS) {
		this.msgEnvioSMS = msgEnvioSMS;
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

	public String getCpfDest() {
		if (cpfDest == null) {
			cpfDest = "";
		}
		return cpfDest;
	}

	public void setCpfDest(String cpfDest) {
		this.cpfDest = cpfDest;
	}

	public String getCodigoDest() {
		if (codigoDest == null) {
			codigoDest = "";
		}
		return codigoDest;
	}

	public void setCodigoDest(String codigoDest) {
		this.codigoDest = codigoDest;
	}

	public String getMatriculaDest() {
		if (matriculaDest == null) {
			matriculaDest = "";
		}
		return matriculaDest;
	}

	public void setMatriculaDest(String matriculaDest) {
		this.matriculaDest = matriculaDest;
	}

}
