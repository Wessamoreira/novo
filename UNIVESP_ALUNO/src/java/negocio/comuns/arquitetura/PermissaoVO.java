package negocio.comuns.arquitetura;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.enumeradores.PerfilAcessoModuloEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoEnumInterface;
import negocio.comuns.arquitetura.enumeradores.TipoVisaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.OpcaoPerfilAcesso;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.PerfilAcesso;

/**
 * Reponsável por manter os dados da entidade Permissao. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 * @see PerfilAcesso
 */
public class PermissaoVO extends SuperVO {

	public static final long serialVersionUID = 1L;
	
    private Integer codPerfilAcesso;
    private Integer responsavel;
    private String nomeEntidade;
    @Deprecated
    private String tituloApresentacao;    
    private String permissoes;
    @Deprecated
    private Integer tipoPermissao;
    @Deprecated
    private String valorEspecifico;
    @Deprecated
    private String valorInicial;
    @Deprecated
    private String valorFinal;
    /**
     * Transientes usados na tela de perfil de acesso;
     */
    
    private Enum<? extends PerfilAcessoPermissaoEnumInterface> permissao;	
    private List<PermissaoVO> funcionalidades;
    private StringBuilder descricaoFuncinalidades;
	private Boolean total;
	private Boolean totalSemExcluir;
	private Boolean incluir;
	private Boolean alterar;
	private Boolean excluir;
	private Boolean consultar;
	private TipoVisaoEnum tipoVisao;
	private String marcarFuncionalidades;
	private Boolean marcarFuncionalidadesPorEntidade;

    /**
     * Construtor padrão da classe <code>Permissao</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public PermissaoVO() {
        super();        
    }
    
    public PermissaoVO clone() throws CloneNotSupportedException {    	
		return (PermissaoVO) super.clone();		
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>PermissaoVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     *                               o atributo e o erro ocorrido.
     */
    public static void validarDados(PermissaoVO obj) throws ConsistirException {
        if (obj.getNomeEntidade().equals("")) {
            throw new ConsistirException("O campo NOME DA ENTIDADE (Permissão) deve ser informado.");
        }
    }

    public String getPermissoes() {
        if (permissoes == null) {
            permissoes = "";
        }
        return (permissoes);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico. 
     * Com base no valor de armazenamento do atributo esta função é capaz de retornar o 
     * de apresentação correspondente. Útil para campos como sexo, escolaridade, etc. 
     */
    public String getPermissoes_Apresentar() {
        if (permissoes.equals("(0)(9)(1)")) {
            return "Incluir e Consultar";
        }
        if (permissoes.equals("(0)")) {
            return "Consultar";
        }
        if (permissoes.equals("(12)")) {
            return "Relatorio";
        }
        if (permissoes.equals("(2)")) {
            return "Alterar";
        }
        if (permissoes.equals("(0)(1)(2)(9)(12)")) {
            return "Total (Sem Excluir)";
        }
        if (permissoes.equals("(0)(1)(2)(3)(9)(12)")) {
            return "Total";
        }
        if (permissoes.equals("(3)")) {
            return "Excluir";
        }
        if (permissoes.equals("(1)(9)")) {
            return "Incluir";
        }
        return (permissoes);
    }

    public void setPermissoes(String permissoes) {
        this.permissoes = permissoes;
    }

    public String getNomeEntidade() {
        if (nomeEntidade == null) {
            nomeEntidade = "";
        }
        return (nomeEntidade);
    }

    public void setNomeEntidade(String nomeEntidade) {
        this.nomeEntidade = nomeEntidade;
    }

    public Integer getCodPerfilAcesso() {
        return (codPerfilAcesso);
    }

    public void setCodPerfilAcesso(Integer codPerfilAcesso) {
        this.codPerfilAcesso = codPerfilAcesso;
    }

    @Deprecated
    public String getTituloApresentacao() {
        if (tituloApresentacao == null || tituloApresentacao.equals("")) {
            tituloApresentacao = nomeEntidade;
        }
        return tituloApresentacao;
    }

    @Deprecated
    public void setTituloApresentacao(String tituloApresentacao) {
        this.tituloApresentacao = tituloApresentacao;
    }

    @Deprecated
    public Integer getTipoPermissao() {
        if (tipoPermissao == null) {
            tipoPermissao = OpcaoPerfilAcesso.TP_ENTIDADE;
        }
        return tipoPermissao;
    }

    @Deprecated
    public void setTipoPermissao(Integer tipoPermissao) {
        this.tipoPermissao = tipoPermissao;
    }

    public String getValorEspecifico() {
        if (valorEspecifico == null) {
            valorEspecifico = "";
        }
        return valorEspecifico;
    }

    public void setValorEspecifico(String valorEspecifico) {
        this.valorEspecifico = valorEspecifico;
    }

    public String getValorInicial() {
        if (valorInicial == null) {
            valorInicial = "";
        }
        return valorInicial;
    }

    public void setValorInicial(String valorInicial) {
        this.valorInicial = valorInicial;
    }

    public String getValorFinal() {
        if (valorFinal == null) {
            valorFinal = "";
        }
        return valorFinal;
    }

    public void setValorFinal(String valorFinal) {
        this.valorFinal = valorFinal;
    }   

    /**
     * @return the responsavel
     */
    public Integer getResponsavel() {
        return responsavel;
    }

    /**
     * @param responsavel the responsavel to set
     */
    public void setResponsavel(Integer responsavel) {
        this.responsavel = responsavel;
    }

	/**
	 * @return the permissao
	 */
	public Enum<? extends PerfilAcessoPermissaoEnumInterface> getPermissao() {
		if (permissao == null) {
			permissao = PerfilAcessoModuloEnum.getEnumPorValor(getNomeEntidade());
		}
		return permissao;
	}

	/**
	 * @param permissao the permissao to set
	 */
	public void setPermissao(Enum<? extends PerfilAcessoPermissaoEnumInterface> permissao) {
		this.permissao = permissao;
	}

	/**
	 * @return the funcionalidades
	 */
	public List<PermissaoVO> getFuncionalidades() {
		if (funcionalidades == null) {
			funcionalidades = new ArrayList<PermissaoVO>(0);
		}
		return funcionalidades;
	}

	/**
	 * @param funcionalidades the funcionalidades to set
	 */
	public void setFuncionalidades(List<PermissaoVO> funcionalidades) {
		this.funcionalidades = funcionalidades;
	}

	/**
	 * @return the total
	 */
	public Boolean getTotal() {
		if (total == null) {
			total = getPermissoes().contains("("+Uteis.INCLUIR+")")
			          && getPermissoes().contains("("+Uteis.CONSULTAR+")")
			          && getPermissoes().contains("("+Uteis.ALTERAR+")")
			          && getPermissoes().contains("("+Uteis.EXCLUIR+")");;
		}
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(Boolean total) {
		this.total =  total;
	}

	/**
	 * @return the totalSemExcluir
	 */
	public Boolean getTotalSemExcluir() {
		if (totalSemExcluir == null) {
			totalSemExcluir = getPermissoes().contains("("+Uteis.INCLUIR+")")
					          && getPermissoes().contains("("+Uteis.CONSULTAR+")")
					          && getPermissoes().contains("("+Uteis.ALTERAR+")")
					          && !getPermissoes().contains("("+Uteis.EXCLUIR+")");
		}
		return totalSemExcluir;
	}

	/**
	 * @param totalSemExcluir the totalSemExcluir to set
	 */
	public void setTotalSemExcluir(Boolean totalSemExcluir) {
		this.totalSemExcluir = totalSemExcluir;
	}

	/**
	 * @return the incluir
	 */
	public Boolean getIncluir() {
		if (incluir == null) {
			incluir = getPermissoes().contains("("+Uteis.INCLUIR+")");
		}
		return incluir;
	}

	/**
	 * @param incluir the incluir to set
	 */
	public void setIncluir(Boolean incluir) {
		this.incluir = incluir;
	}

	/**
	 * @return the alterar
	 */
	public Boolean getAlterar() {
		if (alterar == null) {
			alterar = getPermissoes().contains("("+Uteis.ALTERAR+")");
		}
		return alterar;
	}

	/**
	 * @param alterar the alterar to set
	 */
	public void setAlterar(Boolean alterar) {
		this.alterar = alterar;
	}

	/**
	 * @return the excluir
	 */
	public Boolean getExcluir() {
		if (excluir == null) {
			excluir = getPermissoes().contains("("+Uteis.EXCLUIR+")");
		}
		return excluir;
	}

	/**
	 * @param excluir the excluir to set
	 */
	public void setExcluir(Boolean excluir) {
		this.excluir = excluir;
	}

	/**
	 * @return the consultar
	 */
	public Boolean getConsultar() {
		if (consultar == null) {
			consultar = getPermissoes().contains("("+Uteis.CONSULTAR+")");
		}
		return consultar;
	}

	/**
	 * @param consultar the consultar to set
	 */
	public void setConsultar(Boolean consultar) {
		this.consultar = consultar;
	}

	/**
	 * @return the tipoVisao
	 */
	public TipoVisaoEnum getTipoVisao() {
		if (tipoVisao == null) {
			tipoVisao = TipoVisaoEnum.ADMINISTRATIVA;
		}
		return tipoVisao;
	}

	/**
	 * @param tipoVisao the tipoVisao to set
	 */
	public void setTipoVisao(TipoVisaoEnum tipoVisao) {
		this.tipoVisao = tipoVisao;
	}
	
	public String getTitulo(){
		return ((PerfilAcessoPermissaoEnumInterface)getPermissao()).getDescricaoVisao(getTipoVisao());
	}
	
	public String getAjuda(){
		return ((PerfilAcessoPermissaoEnumInterface)getPermissao()).getAjudaVisao(getTipoVisao());
	}
	
	public List<String> getPaginaAcesso(){
		return ((PerfilAcessoPermissaoEnumInterface)getPermissao()).getPaginaAcessoVisao(getTipoVisao());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((permissao == null) ? 0 : permissao.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PermissaoVO other = (PermissaoVO) obj;
		if (nomeEntidade != other.nomeEntidade)
			return false;
		return true;
	}

	public StringBuilder getDescricaoFuncinalidades() {
		if (descricaoFuncinalidades == null) {
			descricaoFuncinalidades = new StringBuilder("");
		}
		return descricaoFuncinalidades;
	}

	public void setDescricaoFuncinalidades(StringBuilder descricaoFuncinalidades) {
		this.descricaoFuncinalidades = descricaoFuncinalidades;
	}
    	
	 public Boolean getApresentarOpcoes() {
	        if ((!this.getTotal().booleanValue()) && (!this.getTotalSemExcluir().booleanValue())) {
	            return true;
	        } else {
	            return false;
	        }
	    }
	 
	 public String getOrdenacao(){
		 return getPermissao() != null ? (((PerfilAcessoPermissaoEnumInterface)getPermissao()).getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum().getOrdem() < 10 ? 
				"0"+((PerfilAcessoPermissaoEnumInterface)getPermissao()).getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum().getOrdem(): 
					 ""+((PerfilAcessoPermissaoEnumInterface)getPermissao()).getPerfilAcessoSubModulo().getPerfilAcessoModuloEnum().getOrdem()) 
			    + (((PerfilAcessoPermissaoEnumInterface)getPermissao()).getPerfilAcessoSubModulo().getOrdem() < 10 ? "0"+((PerfilAcessoPermissaoEnumInterface)getPermissao()).getPerfilAcessoSubModulo().getOrdem() : ((PerfilAcessoPermissaoEnumInterface)getPermissao()).getPerfilAcessoSubModulo().getOrdem())
			    + getTitulo()
			    : "10000" ;
	 }

	public String getMarcarFuncionalidades() {
		if (marcarFuncionalidades == null) {
			marcarFuncionalidades = "";
		}
		return marcarFuncionalidades;
	}

	public void setMarcarFuncionalidades(String marcarFuncionalidades) {
		this.marcarFuncionalidades = marcarFuncionalidades;
	}

	public Boolean getMarcarFuncionalidadesPorEntidade() {
		if (marcarFuncionalidadesPorEntidade == null) {
			marcarFuncionalidadesPorEntidade = false;
		}
		return marcarFuncionalidadesPorEntidade;
	}

	public void setMarcarFuncionalidadesPorEntidade(Boolean marcarFuncionalidadesPorEntidade) {
		this.marcarFuncionalidadesPorEntidade = marcarFuncionalidadesPorEntidade;
	}

	@Override
	public String toString() {
		return "PermissaoVO ["+getNomeEntidade()+"]";
	}

	
}
