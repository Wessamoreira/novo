package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade FluxoCaixa. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class FluxoCaixaVO extends SuperVO {

    private Integer codigo;
    private Date dataAbertura;
    private Double saldoInicial;
    private Double saldoFinal;
    private Double saldoDinheiro;
    private Double saldoCheque;
    public Date dataFechamento;
    private String situacao;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>MovimentacaoCaixa</code>.
     */
    private List<MovimentacaoCaixaVO> movimentacaoCaixaVOs;
    private List<ChequeVO> chequeVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Usuario </code>.
     */
    private UsuarioVO responsavelAbertura;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ContaCorrente </code>.
     */
    private ContaCorrenteVO contaCaixa;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Usuario </code>.
     */
    private UsuarioVO responsavelFechamento;
    private Integer unidadeEnsino;
    private Double saldoCartao;
    private Double saldoDeposito;
    private Double saldoBoletoBancario;
    private Double saldoCartaoDebito;
    private Double saldoIsencao;
    private Double saldoPermuta;
    private Double saldoCreditoDebitoContaCorrente;
    public static final long serialVersionUID = 1L;
    
    public enum EnumCampoConsultaFluxoCaixa {
		CONTA_CAIXA, DATA_ABERTURA, RESPONSAVEL_ABERTURA;
	}

    /**
     * Construtor padrão da classe <code>FluxoCaixa</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public FluxoCaixaVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>FluxoCaixaVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(FluxoCaixaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }

        if ((obj.getContaCaixa() == null) || (obj.getContaCaixa().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CONTA CAIXA (Fluxo de Caixa) deve ser informado.");
        }
        if(obj.getSituacao().equals("F") && obj.getDataAbertura().compareTo(obj.getDataFechamento())>0){
        	throw new ConsistirException("O campo DATA FECHAMENTO (Fluxo de Caixa) deve ser maior que a DATA DE ABERTURA, provavelmente o servidor está com o horário desconfigurado.");
        }
        if(!Uteis.isAtributoPreenchido(obj.getUnidadeEnsino()) && !obj.getContaCaixa().getUnidadeEnsinoContaCorrenteVOs().isEmpty()) {
        	obj.setUnidadeEnsino(obj.getContaCaixa().getUnidadeEnsinoContaCorrenteVOs().get(0).getUnidadeEnsino().getCodigo());
        }
    }

    public static void validarDadosExclusao(FluxoCaixaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (!obj.getMovimentacaoCaixaVOs().isEmpty()) {
            throw new ConsistirException("Não é possivel excluir um fluxo de caixa com movimentações já realizadas.");
        }
    }

    public Boolean getEdicao() {
        if (getCodigo().intValue() != 0) {
            return true;
        }
        return false;
    }

    public String getImagemBotao() {
        if (getEdicao()) {
            return "../../resources/imagens/botaoFecharCaixa.png";
        }
        return "../../resources/imagens/botaoAbrirCaixa.png";
    }

    public Boolean getApresentarMovimentacaoCaixa() {
        return !getMovimentacaoCaixaVOs().isEmpty();
    }

    public Boolean getFinalizado() {
        if (getSituacao().equals("F")) {
            return true;
        }
        return false;
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>MovimentacaoCaixaVO</code> ao List
     * <code>movimentacaoCaixaVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>MovimentacaoCaixa</code> - getCodigoOrigem() - como
     * identificador (key) do objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>MovimentacaoCaixaVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjMovimentacaoCaixaVOs(MovimentacaoCaixaVO obj) throws Exception {
        MovimentacaoCaixaVO.validarDados(obj);
        int index = 0;
        Iterator<MovimentacaoCaixaVO> i = getMovimentacaoCaixaVOs().iterator();
        while (i.hasNext()) {
            MovimentacaoCaixaVO objExistente = (MovimentacaoCaixaVO) i.next();
            if (objExistente.getCodigoOrigem().equals(obj.getCodigoOrigem())) {
                getMovimentacaoCaixaVOs().set(index, obj);
                return;
            }
            index++;
        }
        getMovimentacaoCaixaVOs().add(obj);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>MovimentacaoCaixaVO</code> no List
     * <code>movimentacaoCaixaVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>MovimentacaoCaixa</code> - getCodigoOrigem() - como
     * identificador (key) do objeto no List.
     *
     * @param codigoOrigem
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjMovimentacaoCaixaVOs(Integer codigoOrigem) throws Exception {
        int index = 0;
        Iterator<MovimentacaoCaixaVO> i = getMovimentacaoCaixaVOs().iterator();
        while (i.hasNext()) {
            MovimentacaoCaixaVO objExistente = (MovimentacaoCaixaVO) i.next();
            if (objExistente.getCodigoOrigem().equals(codigoOrigem)) {
                getMovimentacaoCaixaVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>MovimentacaoCaixaVO</code> no List
     * <code>movimentacaoCaixaVOs</code>. Utiliza o atributo padrão de consulta
     * da classe <code>MovimentacaoCaixa</code> - getCodigoOrigem() - como
     * identificador (key) do objeto no List.
     *
     * @param codigoOrigem
     *            Parâmetro para localizar o objeto do List.
     */
    public MovimentacaoCaixaVO consultarObjMovimentacaoCaixaVO(Integer codigoOrigem) throws Exception {
        Iterator<MovimentacaoCaixaVO> i = getMovimentacaoCaixaVOs().iterator();
        while (i.hasNext()) {
            MovimentacaoCaixaVO objExistente = (MovimentacaoCaixaVO) i.next();
            if (objExistente.getCodigoOrigem().equals(codigoOrigem)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com (
     * <code>FluxoCaixa</code>).
     */
    public UsuarioVO getResponsavelFechamento() {
        if (responsavelFechamento == null) {
            responsavelFechamento = new UsuarioVO();
        }
        return (responsavelFechamento);
    }

    /**
     * Define o objeto da classe <code>Usuario</code> relacionado com (
     * <code>FluxoCaixa</code>).
     */
    public void setResponsavelFechamento(UsuarioVO obj) {
        this.responsavelFechamento = obj;
    }

    /**
     * Retorna o objeto da classe <code>ContaCorrente</code> relacionado com (
     * <code>FluxoCaixa</code>).
     */
    public ContaCorrenteVO getContaCaixa() {
        if (contaCaixa == null) {
            contaCaixa = new ContaCorrenteVO();
        }
        return (contaCaixa);
    }

    /**
     * Define o objeto da classe <code>ContaCorrente</code> relacionado com (
     * <code>FluxoCaixa</code>).
     */
    public void setContaCaixa(ContaCorrenteVO obj) {
        this.contaCaixa = obj;
    }

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com (
     * <code>FluxoCaixa</code>).
     */
    public UsuarioVO getResponsavelAbertura() {
        if (responsavelAbertura == null) {
            responsavelAbertura = new UsuarioVO();
        }
        return (responsavelAbertura);
    }

    /**
     * Define o objeto da classe <code>Usuario</code> relacionado com (
     * <code>FluxoCaixa</code>).
     */
    public void setResponsavelAbertura(UsuarioVO obj) {
        this.responsavelAbertura = obj;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>MovimentacaoCaixa</code>.
     */
    public List<MovimentacaoCaixaVO> getMovimentacaoCaixaVOs() {
        if (movimentacaoCaixaVOs == null) {
            movimentacaoCaixaVOs = new ArrayList<MovimentacaoCaixaVO>(0);
        }
        return (movimentacaoCaixaVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>MovimentacaoCaixa</code>.
     */
    public void setMovimentacaoCaixaVOs(List<MovimentacaoCaixaVO> movimentacaoCaixaVOs) {
        this.movimentacaoCaixaVOs = movimentacaoCaixaVOs;
    }

    public Date getDataFechamento() {
        if (dataFechamento == null) {
            dataFechamento = new Date();
        }
        return (dataFechamento);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataFechamento_Apresentar() {
        if (dataFechamento == null) {
            return "";
        }
        return (Uteis.getDataComHora(dataFechamento));
    }

    public void setDataFechamento(Date dataFechamento) {
        this.dataFechamento = dataFechamento;
    }

    public Double getSaldoFinal() {
        if (saldoFinal == null) {
            saldoFinal = 0.0;
        }
        return (saldoFinal);
    }

    public void setSaldoFinal(Double saldoFinal) {
        this.saldoFinal = saldoFinal;
    }

    public Double getSaldoInicial() {
        if (saldoInicial == null) {
            saldoInicial = 0.0;
        }
        return (saldoInicial);
    }

    public void setSaldoInicial(Double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public Date getDataAbertura() {
        if (dataAbertura == null) {
            dataAbertura = new Date();
        }
        return (dataAbertura);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataAbertura_Apresentar() {
        return (Uteis.getDataComHora(getDataAbertura()));
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public String getSituacao_Apresentar() {
        if (getSituacao().equals("A")) {
            return "Aberto";
        }
        if (getSituacao().equals("F")) {
            return "Fechado";
        }
        return "";
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "A";
        }
        return situacao;
    }

    public Integer getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = 0;
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(Integer unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
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

    public void setSaldoCartao(Double saldoCartao) {
        this.saldoCartao = saldoCartao;
    }

    public Double getSaldoCartao() {
        if (saldoCartao == null) {
            saldoCartao = 0.0;
        }
        return saldoCartao;
    }

    /**
     * @return the saldoDeposito
     */
    public Double getSaldoDeposito() {
        if (saldoDeposito == null) {
            saldoDeposito = 0.0;
        }
        return saldoDeposito;
    }

    /**
     * @param saldoDeposito the saldoDeposito to set
     */
    public void setSaldoDeposito(Double saldoDeposito) {
        this.saldoDeposito = saldoDeposito;
    }

    public Double getSaldoBoletoBancario() {
        if (saldoBoletoBancario == null) {
            saldoBoletoBancario = 0.0;
        }
        return saldoBoletoBancario;
    }

    public void setSaldoBoletoBancario(Double saldoBoletoBancario) {
        this.saldoBoletoBancario = saldoBoletoBancario;
    }

    /**
     * @return the saldoDinheiro
     */
    public Double getSaldoDinheiro() {
        if (saldoDinheiro == null) {
            saldoDinheiro = 0.0;
        }
        return saldoDinheiro;
    }

    /**
     * @param saldoDinheiro the saldoDinheiro to set
     */
    public void setSaldoDinheiro(Double saldoDinheiro) {
        this.saldoDinheiro = saldoDinheiro;
    }

    /**
     * @return the saldoCheque
     */
    public Double getSaldoCheque() {
        if (saldoCheque == null) {
            saldoCheque = 0.0;
        }
        return saldoCheque;
    }

    /**
     * @param saldoCheque the saldoCheque to set
     */
    public void setSaldoCheque(Double saldoCheque) {
        this.saldoCheque = saldoCheque;
    }

    /**
     * @return the chequeVOs
     */
    public List<ChequeVO> getChequeVOs() {
        if (chequeVOs == null) {
            chequeVOs = new ArrayList<ChequeVO>();
        }
        return chequeVOs;
    }

    /**
     * @param chequeVOs the chequeVOs to set
     */
    public void setChequeVOs(List<ChequeVO> chequeVOs) {
        this.chequeVOs = chequeVOs;
    }
    
	@Override
	public String toString() {
		return "Flux de Caixa [" + this.getCodigo() + "]: " + 
                        " Data Abertura: " + this.getDataAbertura_Apresentar() + 
                        " Situação: " + this.getSituacao_Apresentar() + 
                        " Saldo Inicial: " + Uteis.getDoubleFormatado(this.getSaldoInicial()) + 
                        " Saldo Final: " + Uteis.getDoubleFormatado(this.getSaldoFinal()) +
                        " Código da Unidade de Ensino: " + this.getUnidadeEnsino() +
                        " Data Fechamento: " + this.getDataFechamento_Apresentar() + 
                        " Usuário Aberturda: " + this.getResponsavelAbertura().getNome() +
                        " Usuário Fechamento: " + this.getResponsavelFechamento().getNome();
	}

	public Double getSaldoCartaoDebito() {
		if (saldoCartaoDebito == null) {
			saldoCartaoDebito = 0.0;
		}
		return saldoCartaoDebito;
	}

	public void setSaldoCartaoDebito(Double saldoCartaoDebito) {
		this.saldoCartaoDebito = saldoCartaoDebito;
	}

	public Double getSaldoIsencao() {
		if (saldoIsencao == null) {
			saldoIsencao = 0.0;
		}
		return saldoIsencao;
	}

	public void setSaldoIsencao(Double saldoIsencao) {
		this.saldoIsencao = saldoIsencao;
	}

	public Double getSaldoPermuta() {
		if (saldoPermuta == null) {
			saldoPermuta = 0.0;
		}
		return saldoPermuta;
	}

	public void setSaldoPermuta(Double saldoPermuta) {
		this.saldoPermuta = saldoPermuta;
	}

	public Double getSaldoCreditoDebitoContaCorrente() {
		if (saldoCreditoDebitoContaCorrente == null) {
			saldoCreditoDebitoContaCorrente = 0.0;
		}
		return saldoCreditoDebitoContaCorrente;
	}

	public void setSaldoCreditoDebitoContaCorrente(Double saldoCreditoDebitoContaCorrente) {
		this.saldoCreditoDebitoContaCorrente = saldoCreditoDebitoContaCorrente;
	}
	
	
    
}
