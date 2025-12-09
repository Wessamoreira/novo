package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade DevolChqR. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class DevolucaoChequeVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private ContaCorrenteVO contaCorrente;
    private ContaCorrenteVO contaCaixa;
    private String motivo;
    private Date dataReapresentacaoCheque1;
    private Date dataReapresentacaoCheque2;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Usuario </code>.
     */
    private UsuarioVO responsavel;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Cliente </code>.
     */
    private PessoaVO pessoa;
    private FornecedorVO fornecedor;
    private ParceiroVO parceiro;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ChqRLog </code>.
     */
    private ChequeVO cheque;
    private CentroReceitaVO centroReceita;
    private UnidadeEnsinoVO unidadeEnsino;
    private ContaReceberVO contaReceberVO;
    private Boolean desconsiderarConciliacaoBancaria;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>DevolChqR</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public DevolucaoChequeVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>DevolChqRVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(DevolucaoChequeVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Devolução Cheque) deve ser informado.");
        }
        if ((obj.getResponsavel() == null) || (obj.getResponsavel().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL (Devolução Cheque) deve ser informado.");
        }
        if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo UNIDADE ENSINO (Devolução Cheque) deve ser informado.");
        }
        if ((obj.getContaCaixa() == null) || (obj.getContaCaixa().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CONTA CAIXA (Devolução Cheque) deve ser informado.");
        }
        if ((obj.getContaCorrente() == null) || (obj.getContaCorrente().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CONTA CORRENTE (Devolução Cheque) deve ser informado.");
        }
        if ((obj.getCheque() == null) || (obj.getCheque().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CHEQUE (Devolução Cheque) deve ser informado.");
        }
        if ((obj.getPessoa() == null || obj.getPessoa().getCodigo().intValue() == 0) 
        		&& (obj.getFornecedor() == null || obj.getFornecedor().getCodigo().intValue() == 0)
        		&& (obj.getParceiro() == null || obj.getParceiro().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo PESSOA (Devolução Cheque) deve ser informado.");
        }

    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        setMotivo(motivo.toUpperCase());
    }

    public Boolean getExisteUnidadeEnsino() {
        if (getUnidadeEnsino().getCodigo().intValue() != 0) {
            return true;
        }
        return false;
    }

    /**
     * Retorna o objeto da classe <code>ChqRLog</code> relacionado com (
     * <code>DevolChqR</code>).
     */
    public ChequeVO getCheque() {
        if (cheque == null) {
            cheque = new ChequeVO();
        }
        return (cheque);
    }

    /**
     * Define o objeto da classe <code>ChqRLog</code> relacionado com (
     * <code>DevolChqR</code>).
     */
    public void setCheque(ChequeVO obj) {
        this.cheque = obj;
    }

    /**
     * Retorna o objeto da classe <code>Cliente</code> relacionado com (
     * <code>DevolChqR</code>).
     */
    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return (pessoa);
    }

    /**
     * Define o objeto da classe <code>Cliente</code> relacionado com (
     * <code>DevolChqR</code>).
     */
    public void setPessoa(PessoaVO obj) {
        this.pessoa = obj;
    }

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com (
     * <code>DevolChqR</code>).
     */
    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }

    /**
     * Define o objeto da classe <code>Usuario</code> relacionado com (
     * <code>DevolChqR</code>).
     */
    public void setResponsavel(UsuarioVO obj) {
        this.responsavel = obj;
    }

    public String getMotivo() {
        if (motivo == null) {
            motivo = "";
        }
        return (motivo);
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return (data);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return (Uteis.getData(data));
    }

    public void setData(Date data) {
        this.data = data;
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

    public ContaCorrenteVO getContaCorrente() {
        if (contaCorrente == null) {
            contaCorrente = new ContaCorrenteVO();
        }
        return contaCorrente;
    }

    public void setContaCorrente(ContaCorrenteVO contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    public ContaCorrenteVO getContaCaixa() {
        if (contaCaixa == null) {
            contaCaixa = new ContaCorrenteVO();
        }
        return contaCaixa;
    }

    public void setContaCaixa(ContaCorrenteVO contaCaixa) {
        this.contaCaixa = contaCaixa;
    }

    public CentroReceitaVO getCentroReceita() {
        if (centroReceita == null) {
            centroReceita = new CentroReceitaVO();
        }
        return centroReceita;
    }

    public void setCentroReceita(CentroReceitaVO centroReceita) {
        this.centroReceita = centroReceita;
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

	public ContaReceberVO getContaReceberVO() {
		if(contaReceberVO == null){
			contaReceberVO = new ContaReceberVO();
		}
		return contaReceberVO;
	}

	public void setContaReceberVO(ContaReceberVO contaReceberVO) {
		this.contaReceberVO = contaReceberVO;
	}

	public Date getDataReapresentacaoCheque1() {
		return dataReapresentacaoCheque1;
	}

	public void setDataReapresentacaoCheque1(Date dataReapresentacaoCheque1) {
		this.dataReapresentacaoCheque1 = dataReapresentacaoCheque1;
	}

	public Date getDataReapresentacaoCheque2() {
		return dataReapresentacaoCheque2;
	}

	public void setDataReapresentacaoCheque2(Date dataReapresentacaoCheque2) {
		this.dataReapresentacaoCheque2 = dataReapresentacaoCheque2;
	}

	public FornecedorVO getFornecedor() {
		if(fornecedor == null){
			fornecedor = new FornecedorVO();
		}
		return fornecedor;
	}

	public void setFornecedor(FornecedorVO fornecedor) {
		this.fornecedor = fornecedor;
	}

	public ParceiroVO getParceiro() {
		if(parceiro == null){
			parceiro = new ParceiroVO();
		}
		return parceiro;
	}

	public void setParceiro(ParceiroVO parceiro) {
		this.parceiro = parceiro;
	}
	
	public String getNomeApresentar(){
		if(getCheque().getPessoa().getCodigo() > 0){
			return getCheque().getPessoa().getNome();
		}
		if(getCheque().getParceiro().getCodigo() > 0){
			return getCheque().getParceiro().getNome();
		}
		if(getCheque().getFornecedor().getCodigo() > 0){
			return getCheque().getFornecedor().getNome();
		}
		return "";
	}

	public Boolean getDesconsiderarConciliacaoBancaria() {
		if (desconsiderarConciliacaoBancaria == null) {
			desconsiderarConciliacaoBancaria = false;
		}
		return desconsiderarConciliacaoBancaria;
	}

	public void setDesconsiderarConciliacaoBancaria(Boolean desconsiderarConciliacaoBancaria) {
		this.desconsiderarConciliacaoBancaria = desconsiderarConciliacaoBancaria;
	}
}
