package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoVencimentoMatriculaPeriodo;

/**
 * Reponsável por manter os dados da entidade ContaReceber. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class MatriculaPeriodoPreMatriculaVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private MatriculaPeriodoVO matriculaPeriodo;
    private SituacaoVencimentoMatriculaPeriodo situacao;
    private ContaReceberVO contaReceber;
    private Date dataVencimento;
    private Double valor;
    private Double valorDesconto;
    private Double valorDescontoInstituicao;
    private Double valorDescontoConvenio;
    private String tipoDesconto;
    private String tipoAplicacaoDescontoBonusPorPreMatricula;
    private String tipoDescontoBonusPorPerMatricula;
    private Double valorDescontoBonusPorPreMatricula;
    private UsuarioVO responsavelPreMatricula;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ContaReceber</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public MatriculaPeriodoPreMatriculaVO() {
        super();
    }

    public static void validarDados(MatriculaPeriodoPreMatriculaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Conta a Receber) deve ser informado.");
        }
        if (obj.getDataVencimento() == null) {
            throw new ConsistirException("O campo DATA DE VENCIMENTO (Conta a Receber) deve ser informado.");
        }
        if (obj.getValor().doubleValue() == 0) {
            throw new ConsistirException("O campo VALOR (Conta a Receber) deve ser informado.");
        }
        if (obj.getTipoDesconto().equals("")) {
            obj.setTipoDesconto("PO");
        }
    }

    public Long getNrDiasAtraso() {
        Long dias = Uteis.nrDiasEntreDatas(new Date(), getDataVencimento());
        if (dias > 0) {
            return dias;
        }
        return 0l;
    }

    public Double getCalcularValorFinal() {
        Double valorFinal = 0.0;
        // realizar o calculo...
        return valorFinal;
    }

    public String getTipoDesc() {
        if (getTipoDesconto().equals("PO")) {
            return "(%)";
        } else if (getTipoDesconto().equals("VE")) {
            return "(R$)";
        }
        return "(%)";
    }

//	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO() {
//		try {
//			ConfiguracaoFinanceiroVO conf = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS);
//			return conf;
//		} catch (Exception e) {
//			return new ConfiguracaoFinanceiroVO();
//		}
//	}
    public Double getValorDesconto() {
        if (valorDesconto == null) {
            valorDesconto = 0.0;
        }
        return (valorDesconto);
    }

    public void setValorDesconto(Double valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public Double getValor() {
        if (valor == null) {
            valor = 0.0;
        }
        return (valor);
    }

    public Double getValor_Apresentar() {
        return (Uteis.arredondarDecimal(valor, 2));
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getDataVencimento() {
        if (dataVencimento == null) {
            dataVencimento = new Date();
        }
        return (dataVencimento);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataVencimento_Apresentar() {
        if (dataVencimento == null) {
            return "";
        }
        return (Uteis.getData(dataVencimento));
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
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
        if (data == null) {
            return "";
        }
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

    public String getTipoDesconto() {
        if (tipoDesconto == null) {
            tipoDesconto = "PO";
        }
        return tipoDesconto;
    }

    public void setTipoDesconto(String tipoDesconto) {
        this.tipoDesconto = tipoDesconto;
    }

    public Double getValorDescontoConvenio() {
        if (valorDescontoConvenio == null) {
            valorDescontoConvenio = 0.0;
        }
        return valorDescontoConvenio;
    }

    public void setValorDescontoConvenio(Double valorDescontoConvenio) {
        this.valorDescontoConvenio = valorDescontoConvenio;
    }

    public Double getValorDescontoInstituicao() {
        if (valorDescontoInstituicao == null) {
            valorDescontoInstituicao = 0.0;
        }
        return valorDescontoInstituicao;
    }

    public void setValorDescontoInstituicao(Double valorDescontoInstituicao) {
        this.valorDescontoInstituicao = valorDescontoInstituicao;
    }

    /**
     * @return the matriculaPeriodo
     */
    public MatriculaPeriodoVO getMatriculaPeriodo() {
        if (matriculaPeriodo == null) {
            matriculaPeriodo = new MatriculaPeriodoVO();
        }
        return matriculaPeriodo;
    }

    /**
     * @param matriculaPeriodo
     *            the matriculaPeriodo to set
     */
    public void setMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodo) {
        this.matriculaPeriodo = matriculaPeriodo;
    }

    /**
     * @return the situacao
     */
    public SituacaoVencimentoMatriculaPeriodo getSituacao() {
        return situacao;
    }

    /**
     * @param situacao
     *            the situacao to set
     */
    public void setSituacao(SituacaoVencimentoMatriculaPeriodo situacao) {
        this.situacao = situacao;
    }

    /**
     * @return the contaReceber
     */
    public ContaReceberVO getContaReceber() {
        if (contaReceber == null) {
            contaReceber = new ContaReceberVO();
        }
        return contaReceber;
    }

    /**
     * @param contaReceber
     *            the contaReceber to set
     */
    public void setContaReceber(ContaReceberVO contaReceber) {
        this.contaReceber = contaReceber;
    }
}
