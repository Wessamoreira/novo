/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.dominios.TipoTextoPadrao;

/**
 *
 * @author Philippe
 */
public class LogImpressaoContratoVO extends SuperVO {

    private Integer codigo;
    private MatriculaVO matricula;
    private UsuarioVO usuarioRespImpressao;
    private String tipoContrato;
    private TextoPadraoVO textoPadrao;
    private Boolean assinado;
    private UsuarioVO usuarioRespAssinatura;
    private ImpressaoContratoVO impressaoContrato;
    private Date dataGeracao;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public MatriculaVO getMatricula() {
        if (matricula == null) {
            matricula = new MatriculaVO();
        }
        return matricula;
    }

    public void setMatricula(MatriculaVO matricula) {
        this.matricula = matricula;
    }

    public UsuarioVO getUsuarioRespImpressao() {
        if (usuarioRespImpressao == null) {
            usuarioRespImpressao = new UsuarioVO();
        }
        return usuarioRespImpressao;
    }

    public void setUsuarioRespImpressao(UsuarioVO usuarioRespImpressao) {
        this.usuarioRespImpressao = usuarioRespImpressao;
    }

    public String getTipoContrato_Apresentar() {
        return TipoTextoPadrao.getDescricao(getTipoContrato());
    }

    public String getTipoContrato() {
        if (tipoContrato == null) {
            tipoContrato = "";
        }
        return tipoContrato;
    }

    public void setTipoContrato(String tipoContrato) {
        this.tipoContrato = tipoContrato;
    }

    public TextoPadraoVO getTextoPadrao() {
        if (textoPadrao == null) {
            textoPadrao = new TextoPadraoVO();
        }
        return textoPadrao;
    }

    public void setTextoPadrao(TextoPadraoVO textoPadrao) {
        this.textoPadrao = textoPadrao;
    }

    public Boolean getAssinado() {
        if (assinado == null) {
            assinado = Boolean.FALSE;
        }
        return assinado;
    }

    public void setAssinado(Boolean assinado) {
        this.assinado = assinado;
    }

    public UsuarioVO getUsuarioRespAssinatura() {
        if (usuarioRespAssinatura == null) {
            usuarioRespAssinatura = new UsuarioVO();
        }
        return usuarioRespAssinatura;
    }

    public void setUsuarioRespAssinatura(UsuarioVO usuarioRespAssinatura) {
        this.usuarioRespAssinatura = usuarioRespAssinatura;
    }
    public Date getDataGeracao() {
		return dataGeracao;
	}

	public void setDataGeracao(Date dataGeracao) {
		this.dataGeracao = dataGeracao;
	}

	public ImpressaoContratoVO getImpressaoContrato() {
		if (impressaoContrato == null) {
			impressaoContrato = new ImpressaoContratoVO();
        }    
		return impressaoContrato;
	}

	public void setImpressaoContrato(ImpressaoContratoVO impressaoContrato) {
		this.impressaoContrato = impressaoContrato;
	}
	
	public boolean isExisteDocumentoAssinado(){
		return getImpressaoContrato().getDocumentoAssinado().getCodigo() != null ? true : false;
	}
}
