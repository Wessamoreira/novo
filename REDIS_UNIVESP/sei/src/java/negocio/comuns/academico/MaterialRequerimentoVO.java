package negocio.comuns.academico;

import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.RequerimentoHistoricoVO;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Curso. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
@XmlRootElement(name = "materialRequerimento")
public class MaterialRequerimentoVO extends SuperVO {

    private Integer codigo;
    private Integer requerimento;
    private String descricao;
    private ArquivoVO arquivoVO;
    private Boolean disponibilizarParaRequerente;
    private UsuarioVO usuarioDisponibilizouArquivo;
    private Date dataDisponibilizacaoArquivo;
    private RequerimentoHistoricoVO requerimentoHistorico;
    
    private Boolean permitirExcluirArquivoMaterial;
    
    public static final long serialVersionUID = 1L;

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

    @XmlElement(name = "descricao")
    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlElement(name = "requerimento")
    public Integer getRequerimento() {
        if (requerimento == null) {
        	requerimento = 0;
        }
        return requerimento;
    }

    public void setRequerimento(Integer requerimento) {
        this.requerimento = requerimento;
    }

    /**
     * @return the arquivoVO
     */
    @XmlElement(name = "arquivoVO")
    public ArquivoVO getArquivoVO() {
        if (arquivoVO == null) {
            arquivoVO = new ArquivoVO();
        }
        return arquivoVO;
    }

    /**
     * @param arquivoVO the arquivoVO to set
     */
    public void setArquivoVO(ArquivoVO arquivoVO) {
        this.arquivoVO = arquivoVO;
    }

    @XmlElement(name = "disponibilizarParaRequerente")
	public Boolean getDisponibilizarParaRequerente() {
		if(disponibilizarParaRequerente == null) {
			disponibilizarParaRequerente = Boolean.FALSE;
		}
		return disponibilizarParaRequerente;
	}

	public void setDisponibilizarParaRequerente(Boolean disponibilizarParaRequerente) {
		this.disponibilizarParaRequerente = disponibilizarParaRequerente;
	}

	public UsuarioVO getUsuarioDisponibilizouArquivo() {
		if(usuarioDisponibilizouArquivo == null) {
			usuarioDisponibilizouArquivo = new UsuarioVO();
		}
		return usuarioDisponibilizouArquivo;
	}

	public void setUsuarioDisponibilizouArquivo(UsuarioVO usuarioDisponibilizouArquivo) {
		this.usuarioDisponibilizouArquivo = usuarioDisponibilizouArquivo;
	}

	public Date getDataDisponibilizacaoArquivo() {
		return dataDisponibilizacaoArquivo;
	}

	public void setDataDisponibilizacaoArquivo(Date dataDisponibilizacaoArquivo) {
		this.dataDisponibilizacaoArquivo = dataDisponibilizacaoArquivo;
	}

	public RequerimentoHistoricoVO getRequerimentoHistorico() {
		if(requerimentoHistorico == null) {
			requerimentoHistorico = new RequerimentoHistoricoVO();
		}
		return requerimentoHistorico;
	}

	public void setRequerimentoHistorico(RequerimentoHistoricoVO requerimentoHistorico) {
		this.requerimentoHistorico = requerimentoHistorico;
	}
	
	public String getDataDisponibilizacaoArquivo_Apresentar() {
		return Uteis.getData(dataDisponibilizacaoArquivo) + " " + Uteis.getHoraMinutoComMascara(dataDisponibilizacaoArquivo);
	}

	public Boolean getPermitirExcluirArquivoMaterial() {
		if(permitirExcluirArquivoMaterial == null) {
			permitirExcluirArquivoMaterial = true;
		}
		return permitirExcluirArquivoMaterial;
	}

	public void setPermitirExcluirArquivoMaterial(Boolean permitirExcluirArquivoMaterial) {
		this.permitirExcluirArquivoMaterial = permitirExcluirArquivoMaterial;
	}
	
	
    
}
