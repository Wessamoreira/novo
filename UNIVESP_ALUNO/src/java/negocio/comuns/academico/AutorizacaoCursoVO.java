package negocio.comuns.academico;

import java.util.Date;

import negocio.comuns.academico.enumeradores.TipoAutorizacaoCursoEnum;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Constantes;

/**
 * Reponsável por manter os dados da entidade Curso. Classe do tipo VO - Value
 * Object composta pelos atributos da entidade com visibilidade protegida e os
 * métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class AutorizacaoCursoVO extends SuperVO {

    private Integer codigo;
    private Integer curso;
    private String nome;
    private Date data;
    /* Campo criado para atender condição onde curso foi iniciado sem reconhecimento,
     * neste caso uma normativa será inserida como se fosse um reconhecimento para ser exibida no histórico,
     * e esta data de vencimento define até quando esta normativa será considerada como um reconhecimento. 
     *  */
    private Date vencimento;
    private Integer codigoUsuario;
    //Atributo apenas para controle
    private Boolean autorizacaoUsadaPorMatricula;
    
    private TipoAutorizacaoCursoEnum tipoAutorizacaoCursoEnum;
    private String numero;
    private Date dataCredenciamento;
    private String veiculoPublicacao;
    private Integer secaoPublicacao;
    private Integer paginaPublicacao;
    private Integer numeroDOU;
    private Boolean emTramitacao;
    private String tipoProcesso;
    private Date dataCadastro;
    private Date dataProtocolo;
    public static final long serialVersionUID = 1L;

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getCodigoUsuario() {
        if (codigoUsuario == null) {
            codigoUsuario = 0;
        }
        return codigoUsuario;
    }

    public void setCodigoUsuario(Integer codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public Integer getCurso() {
        if (curso == null) {
            curso = 0;
        }
        return curso;
    }

    public void setCurso(Integer curso) {
        this.curso = curso;
    }

    public Boolean getAutorizacaoUsadaPorMatricula() {
        if (autorizacaoUsadaPorMatricula == null) {
            autorizacaoUsadaPorMatricula = Boolean.FALSE;
        }
        return autorizacaoUsadaPorMatricula;
    }

    public void setAutorizacaoUsadaPorMatricula(Boolean autorizacaoUsadaPorMatricula) {
        this.autorizacaoUsadaPorMatricula = autorizacaoUsadaPorMatricula;
    }
    
    /*
     * Não implementar Singleton neste método GET.
     */
    public Date getVencimento() {
        return vencimento;
    }

    public void setVencimento(Date vencimento) {
        this.vencimento = vencimento;
    }
    
    public TipoAutorizacaoCursoEnum getTipoAutorizacaoCursoEnum() {
        return tipoAutorizacaoCursoEnum;
    }

    public void setTipoAutorizacaoCursoEnum(TipoAutorizacaoCursoEnum tipoAutorizacaoCursoEnum) {
        this.tipoAutorizacaoCursoEnum = tipoAutorizacaoCursoEnum;
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

	public Date getDataCredenciamento() {
		return dataCredenciamento;
	}

	public void setDataCredenciamento(Date dataCredenciamento) {
		this.dataCredenciamento = dataCredenciamento;
	}

	public String getVeiculoPublicacao() {
		if (veiculoPublicacao == null) {
			veiculoPublicacao = Constantes.EMPTY;
		}
		return veiculoPublicacao;
	}

	public void setVeiculoPublicacao(String veiculoPublicacao) {
		this.veiculoPublicacao = veiculoPublicacao;
	}

	public Integer getSecaoPublicacao() {
		if (secaoPublicacao == null) {
			secaoPublicacao = 0;
		}
		return secaoPublicacao;
	}

	public void setSecaoPublicacao(Integer secaoPublicacao) {
		this.secaoPublicacao = secaoPublicacao;
	}

	public Integer getPaginaPublicacao() {
		if (paginaPublicacao == null) {
			paginaPublicacao = 0;
		}
		return paginaPublicacao;
	}

	public void setPaginaPublicacao(Integer paginaPublicacao) {
		this.paginaPublicacao = paginaPublicacao;
	}

	public Integer getNumeroDOU() {
		if (numeroDOU == null) {
			numeroDOU = 0;
		}
		return numeroDOU;
	}

	public void setNumeroDOU(Integer numeroDOU) {
		this.numeroDOU = numeroDOU;
	}

    public Boolean getEmTramitacao() {
		if (emTramitacao == null) {
			emTramitacao = Boolean.FALSE;
		}
		return emTramitacao;
	}

	public void setEmTramitacao(Boolean emTramitacao) {
		this.emTramitacao = emTramitacao;
	}

	public String getTipoProcesso() {
		if (tipoProcesso == null) {
			tipoProcesso = Constantes.EMPTY;
		}
		return tipoProcesso;
	}

	public void setTipoProcesso(String tipoProcesso) {
		this.tipoProcesso = tipoProcesso;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Date getDataProtocolo() {
		return dataProtocolo;
	}

	public void setDataProtocolo(Date dataProtocolo) {
		this.dataProtocolo = dataProtocolo;
	}
}
