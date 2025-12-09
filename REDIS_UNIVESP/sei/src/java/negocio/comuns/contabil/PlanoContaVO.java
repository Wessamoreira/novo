package negocio.comuns.contabil;

import java.util.ArrayList;
import java.util.List;

import controle.arquitetura.TreeNodeCustomizado;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

/**
 * Reponsável por manter os dados da entidade PlanoConta. Classe do tipo VO - Value Object composta pelos atributos da
 * entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class PlanoContaVO extends SuperVO {

    protected Integer codigo;
    protected PlanoContaVO planoContaPrincipal;
    protected String identificadorPlanoConta;
    protected Integer codigoReduzido;
    protected String descricao;    
    protected Integer nivelPlanoConta;
    private String codigoPatrimonial;
    /** Atributo responsável por manter o objeto relacionado da classe <code>UnidadeEnsino </code>. */
    protected UnidadeEnsinoVO unidadeEnsino;
    protected Boolean adicionarConta;
    protected Boolean apresentarCheckBoxNaoSelecionado;
    protected Boolean adicionarContasUsuario;
    protected List<PlanoContaVO> listaNivelSubordinado;
    private TreeNodeCustomizado arvorePlanoConta;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>PlanoConta</code>. Cria uma nova instância desta entidade, inicializando
     * automaticamente seus atributos (Classe VO).
     */
    public PlanoContaVO() {
        super();
    }

    public String getDescricao() {
        if (descricao == null) {
        	descricao = "";
        }
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getIdentificadorPlanoConta() {
        if (identificadorPlanoConta == null) {
        	identificadorPlanoConta = "";
        }
        return (identificadorPlanoConta);
    }

    public void setIdentificadorPlanoConta(String identificadorPlanoConta) {
        this.identificadorPlanoConta = identificadorPlanoConta;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public boolean verificarPlanoContaPrimeiroNivel() {
        if (this.getPlanoContaPrincipal() == null || this.getPlanoContaPrincipal().getCodigo().intValue() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public PlanoContaVO getPlanoContaPrincipal() {
        if (planoContaPrincipal == null) {
            planoContaPrincipal = new PlanoContaVO();
        }
        return planoContaPrincipal;
    }

    public void setPlanoContaPrincipal(PlanoContaVO planoContaPrincipal) {
        this.planoContaPrincipal = planoContaPrincipal;
    }       
    
    public Boolean getAdicionarConta() {
        if (adicionarConta == null) {
            adicionarConta = Boolean.FALSE;
        }
        return adicionarConta;
    }

    public void setAdicionarConta(Boolean adicionarConta) {
        this.adicionarConta = adicionarConta;
    }

    public Boolean getApresentarCheckBoxNaoSelecionado() {
        if (apresentarCheckBoxNaoSelecionado == null) {
            apresentarCheckBoxNaoSelecionado = Boolean.FALSE;
        }
        return apresentarCheckBoxNaoSelecionado;
    }

    public void setApresentarCheckBoxNaoSelecionado(Boolean apresentarCheckBoxNaoSelecionado) {
        this.apresentarCheckBoxNaoSelecionado = apresentarCheckBoxNaoSelecionado;
    }

    public Boolean getAdicionarContasUsuario() {
        if (adicionarContasUsuario == null) {
            adicionarContasUsuario = Boolean.FALSE;
        }
        return adicionarContasUsuario;
    }

    public void setAdicionarContasUsuario(Boolean adicionarContasUsuario) {
        this.adicionarContasUsuario = adicionarContasUsuario;
    }

    public Integer getNivelPlanoConta() {
        if (nivelPlanoConta == null) {
            nivelPlanoConta = 0;
        }
        return nivelPlanoConta;
    }

    public void setNivelPlanoConta(Integer nivelPlanoConta) {
        this.nivelPlanoConta = nivelPlanoConta;
    }

    public List<PlanoContaVO> getListaNivelSubordinado() {
        if (listaNivelSubordinado == null) {
            listaNivelSubordinado = new ArrayList<PlanoContaVO>(0);
        }
        return listaNivelSubordinado;
    }

    public void setListaNivelSubordinado(List<PlanoContaVO> listaNivelSubordinado) {
        this.listaNivelSubordinado = listaNivelSubordinado;
    }

    public JRDataSource getNivelSubordinadoJRDataSource() {
        JRDataSource jr = new JRBeanArrayDataSource(getListaNivelSubordinado().toArray());
        return jr;
    }

    public Boolean getNivel1() {
        Integer i = Uteis.contarQuantidadeDePontos(getIdentificadorPlanoConta(), ".");
        if (i == 0) {
            return true;
        }
        return false;
    }

    public Boolean getNivel2() {
        Integer i = Uteis.contarQuantidadeDePontos(getIdentificadorPlanoConta(), ".");
        if (i == 1) {
            return true;
        }
        return false;
    }

    public Boolean getNivel3() {
        Integer i = Uteis.contarQuantidadeDePontos(getIdentificadorPlanoConta(), ".");
        if (i == 2) {
            return true;
        }
        return false;
    }

    public Boolean getNivelMaiorQue3() {
        Integer i = Uteis.contarQuantidadeDePontos(getIdentificadorPlanoConta(), ".");
        if (i > 2) {
            return true;
        }
        return false;
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

	public TreeNodeCustomizado getArvorePlanoConta() {
		return arvorePlanoConta;
	}

	public void setArvorePlanoConta(TreeNodeCustomizado arvorePlanoConta) {
		this.arvorePlanoConta = arvorePlanoConta;
	}
	
	public String getIdentificacaoPlanoContaComDescricao(){
		if(Uteis.isAtributoPreenchido(getIdentificadorPlanoConta())){
			return getIdentificadorPlanoConta() + " - " + getDescricao();
		}
		return "";
	}
    
	public String getCodigoPatrimonial() {
    	if (codigoPatrimonial == null) {
    		codigoPatrimonial = "";
    	}
		return codigoPatrimonial;
	}
    
    public void setCodigoPatrimonial(String codigoPatrimonial) {
		this.codigoPatrimonial = codigoPatrimonial;
	}

	public Integer getCodigoReduzido() {
		if(codigoReduzido == null){
			codigoReduzido = 0;
		}
		return codigoReduzido;
	}

	public void setCodigoReduzido(Integer codigoReduzido) {
		this.codigoReduzido = codigoReduzido;
	}

	
    
    
}
