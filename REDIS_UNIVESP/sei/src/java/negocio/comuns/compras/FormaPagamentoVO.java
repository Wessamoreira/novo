package negocio.comuns.compras;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;

/**
 * Reponsável por manter os dados da entidade FormaPagamento. Classe do tipo VO
 * - Value Object composta pelos atributos da entidade com visibilidade
 * protegida e os métodos de acesso a estes atributos. Classe utilizada para
 * apresentar e manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class FormaPagamentoVO extends SuperVO {

    private Integer codigo;
    private String nome;
    private String tipo;
    private Boolean usaNoRecebimento;
    private Boolean filtrarFormaPagamento;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>FormaPagamento</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public FormaPagamentoVO() {
        super();
        inicializarDados();
    }

    public FormaPagamentoVO(Integer codigo) {
        super();
        inicializarDados();
        setCodigo(codigo);
    }
    
    public enum enumCampoConsultaFormaPagamento{
		NOME, CODIGO;
	}

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        setNome(nome.toUpperCase());
    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(0);
        setNome("");
        setTipo("");
    }

    public String getTipo_Apresentar() {
        return TipoFormaPagamento.getDescricao(tipo);
    }
    
    public TipoFormaPagamento getTipoFormaPagamentoEnum() {
    	return TipoFormaPagamento.getEnum(tipo);
    }

    public String getTipo() {
        if (tipo == null) {
            tipo = "";
        }
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return (nome);
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getCodigo() {
        if (this.codigo == null) {
            return 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public boolean isDinheiro() {
        if (this.getTipo().equals("DI")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCheque() {
        if (this.getTipo().equals("CH")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCartaoCredito() {
        if (this.getTipo().equals("CA")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isCartaoDebito() {
        if (this.getTipo().equals("CD")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isDebitoEmConta() {
        if (this.getTipo().equals("DE")) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isBoletoBancario() {
        if (this.getTipo().equals("BO")) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean isDeposito() {
    	if (this.getTipo().equals("DC")) {
    		return true;
    	} else {
    		return false;
    	}
    }

    public Boolean getUsaNoRecebimento() {
        if (usaNoRecebimento == null) {
            usaNoRecebimento = false;
        }
        return usaNoRecebimento;
    }

    public void setUsaNoRecebimento(Boolean usaNoRecebimento) {
        this.usaNoRecebimento = usaNoRecebimento;
    }

    public boolean isPermuta() {
    	if (this.getTipo().equals("PE")) {
    		return true;
    	} else {
    		return false;
    	}
    }  
    
    public boolean isInformaOperadoraCartao() {
		return isCartaoCredito() || isCartaoDebito();
	}

	public Boolean getFiltrarFormaPagamento() {
		if (filtrarFormaPagamento == null) {
			filtrarFormaPagamento = true;
		}
		return filtrarFormaPagamento;
	}

	public void setFiltrarFormaPagamento(Boolean filtrarFormaPagamento) {
		this.filtrarFormaPagamento = filtrarFormaPagamento;
	}


}
