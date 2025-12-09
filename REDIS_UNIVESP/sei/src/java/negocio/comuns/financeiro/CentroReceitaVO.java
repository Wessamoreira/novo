package negocio.comuns.financeiro;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import controle.financeiro.RelatorioSEIDecidirControle;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade CentroReceita. Classe do tipo VO - Value Object composta pelos atributos
 * da entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
@XmlRootElement(name = "centroReceitaVO")
public class CentroReceitaVO extends SuperVO {

    private Integer codigo;
    private String descricao;
    private String identificadorCentroReceita;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>CentroReceita </code>.
     */
    private CentroReceitaVO centroReceitaPrincipal;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Departamento </code>.
     */
    private DepartamentoVO departamento;
    
    private Boolean informarManualmenteIdentificadorCentroReceita;
    
	/**
	 * Atributo responsável utilizado na classe
	 * {@link RelatorioSEIDecidirControle}
	 */
	private Boolean filtrarCentroReceitaVO;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>CentroReceita</code>. Cria uma nova instância desta entidade, inicializando
     * automaticamente seus atributos (Classe VO).
     */
    public CentroReceitaVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>CentroReceitaVO</code>. Todos os tipos de
     * consistência de dados são e devem ser implementadas neste método. São validações típicas: verificação de campos
     * obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o
     *                erro ocorrido.
     */
    public static void validarDados(CentroReceitaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getIdentificadorCentroReceita().equals("")) {
            throw new ConsistirException("O campo IDENTIFICADOR DO CENTRO DE RECEITA (Centro de Receitas) deve ser informado.");
        }
        if ((obj.getDepartamento() == null) || (obj.getDepartamento().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo DEPARTAMENTO (Centro de Receitas) deve ser informado.");
        }
    }

    /**
     * Retorna o objeto da classe <code>Departamento</code> relacionado com ( <code>CentroReceita</code>).
     */
    @XmlElement(name = "departamento")
    public DepartamentoVO getDepartamento() {
        if (departamento == null) {
            departamento = new DepartamentoVO();
        }
        return (departamento);
    }

    /**
     * Define o objeto da classe <code>Departamento</code> relacionado com ( <code>CentroReceita</code>).
     */
    public void setDepartamento(DepartamentoVO obj) {
        this.departamento = obj;
    }

    public CentroReceitaVO getCentroReceitaPrincipal() {
        if (centroReceitaPrincipal == null) {
            centroReceitaPrincipal = new CentroReceitaVO();
        }
        return centroReceitaPrincipal;
    }

    public void setCentroReceitaPrincipal(CentroReceitaVO centroReceitaPrincipal) {
        this.centroReceitaPrincipal = centroReceitaPrincipal;
    }

    @XmlElement(name = "identificadorCentroReceita")
    public String getIdentificadorCentroReceita() {
        if (identificadorCentroReceita == null) {
            identificadorCentroReceita = "";
        }
        return (identificadorCentroReceita);
    }

    public void setIdentificadorCentroReceita(String identificadorCentroReceita) {
        this.identificadorCentroReceita = identificadorCentroReceita;
    }

    @XmlElement(name = "descricao")
    public String getDescricao() {
        if (descricao == null) {
            descricao = "";
        }
        return (descricao);
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlElement(name = "codigo")
    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public boolean verificarCentroReceitaPrimeiroNivel() {
        if (this.getCentroReceitaPrincipal() == null || this.getCentroReceitaPrincipal().getCodigo().intValue() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public int obterNrNivelCentroReceita(CentroReceitaVO principal) {
    	 String mascara = principal.getIdentificadorCentroReceita();
    	 int quantidadeNiveis = 0;
         if (mascara != null) {
             int posicao = mascara.indexOf(".");
             while (posicao != -1) {
                 quantidadeNiveis++;
                 mascara = mascara.substring(posicao + 1);
                 posicao = mascara.indexOf(".");
             }
         }
    	return quantidadeNiveis;
    }

	public Boolean getFiltrarCentroReceitaVO() {
		if (filtrarCentroReceitaVO == null) {
			filtrarCentroReceitaVO = false;
		}
		return filtrarCentroReceitaVO;
	}

	public void setFiltrarCentroReceitaVO(Boolean filtrarCentroReceitaVO) {
		this.filtrarCentroReceitaVO = filtrarCentroReceitaVO;
	}

	public Boolean getInformarManualmenteIdentificadorCentroReceita() {
		if (informarManualmenteIdentificadorCentroReceita == null) {
			informarManualmenteIdentificadorCentroReceita = false;
		}
		return informarManualmenteIdentificadorCentroReceita;
	}

	public void setInformarManualmenteIdentificadorCentroReceita(Boolean informarManualmenteIdentificadorCentroReceita) {
		this.informarManualmenteIdentificadorCentroReceita = informarManualmenteIdentificadorCentroReceita;
	}
	
	public boolean getDesabilitarInformarManualmenteIdentificadorCentroReceita() {
		return Uteis.isAtributoPreenchido(getCodigo()) && getInformarManualmenteIdentificadorCentroReceita();
	}
}
