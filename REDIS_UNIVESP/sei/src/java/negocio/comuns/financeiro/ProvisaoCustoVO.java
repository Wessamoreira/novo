package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemMapaLancamentoFuturo;
import negocio.comuns.utilitarias.dominios.SituacaoProvisaoCusto;
import negocio.comuns.utilitarias.dominios.TipoMapaLancamentoFuturo;

/**
 * Reponsável por manter os dados da entidade ProvisaoCusto. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class ProvisaoCustoVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private Double valor;
    private String descricao;
    private Date dataPrestacaoConta;
    private Double valorTroco;
    private String situacao;
    /**
     * Atributo responsável por manter os objetos da classe
     * <code>ItensProvisao</code>.
     */
    private List itensProvisaoVOs;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Usuario </code>.
     */
    private UsuarioVO responsavel;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Funcionario </code>.
     */
    private FuncionarioVO requisitante;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ContaCorrente </code>.
     */
    private ContaCorrenteVO contaCorrente;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>ContaCorrente </code>.
     */
    private ContaCorrenteVO contaCorrenteTroco;
    private MapaLancamentoFuturoVO mapaLancamentoFuturo;
    private FuncionarioCargoVO funcionarioCargoCentroCusto;
    private Double valorFinal;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ProvisaoCusto</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public ProvisaoCustoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>ProvisaoCustoVO</code>. Todos os tipos de consistência de dados são
     * e devem ser implementadas neste método. São validações típicas:
     * verificação de campos obrigatórios, verificação de valores válidos para
     * os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(ProvisaoCustoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Provisão de Custo) deve ser informado.");
        }
        if ((obj.getResponsavel() == null) || (obj.getResponsavel().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo RESPONSÁVEL (Provisão de Custo) deve ser informado.");
        }
        if ((obj.getRequisitante() == null) || (obj.getRequisitante().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo REQUISITANTE (Provisão de Custo) deve ser informado.");
        }
        if (obj.getValor().doubleValue() == 0) {
            throw new ConsistirException("O campo VALOR (Provisão de Custo) deve ser informado.");
        }
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO (Provisão de Custo) deve ser informado.");
        }
        if (obj.getDataPrestacaoConta() == null) {
            throw new ConsistirException("O campo DATA DA PRESTAÇÃO DE CONTA (Provisão de Custo) deve ser informado.");
        }
        if ((obj.getContaCorrente() == null) || (obj.getContaCorrente().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CONTA CORRENTE (Provisão de Custo) deve ser informado.");
        }
        if (!obj.isNovoObj()
                && (obj.getFuncionarioCargoCentroCusto() == null || obj.getFuncionarioCargoCentroCusto().getCodigo() == 0)) {
            throw new ConsistirException("O campo CENTRO DE CUSTO (Provisão de Custo) do funcionario deve ser informado.");
        }
        if (obj.isProvisaoPrecisaGuardarTroco()
                && ((obj.getContaCorrenteTroco() == null) || (obj.getContaCorrenteTroco().getCodigo().intValue() == 0))) {
            throw new ConsistirException("O campo CONTA CORRENTE TROCO (Provisão de Custo) deve ser informado.");
        }
    }

    public boolean isProvisaoPrecisaGuardarTroco() {
        if (getValorTroco() > 0 && !isNovoObj()) {
            return true;
        }
        return false;
    }

    public boolean isProvisaoValorFinalMaiorQueInicial() {
        if (getValorFinal() > (getValor() + getValorTroco())) {
            return true;
        }
        return false;
    }

    public boolean isSituacaoFinalizado() {
        return SituacaoProvisaoCusto.FINALIZADO.getValor().equals(getSituacao());
    }

    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo
     * String.
     */
    public void realizarUpperCaseDados() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
        setDescricao(getDescricao().toUpperCase());
        setSituacao(getSituacao().toUpperCase());
    }

    public void atualizarValorFinalETroco() {
        double valorFim = 0;
        for (ItensProvisaoVO obj : (List<ItensProvisaoVO>) itensProvisaoVOs) {
            valorFim += obj.getValor().doubleValue();
        }
        setValorFinal(valorFim);
        if (getValorRestanteParaDeclarar() > 0) {
            setValorTroco(getValorRestanteParaDeclarar());
        } else {
            setValorTroco(0.0);
        }
    }

    public Double getValorRestanteParaDeclarar() {
        double valorRestante = 0;
        for (ItensProvisaoVO obj : (List<ItensProvisaoVO>) itensProvisaoVOs) {
            valorRestante += obj.getValor().doubleValue();
        }
        return getValor().doubleValue() - valorRestante;
    }

    /**
     * Operação responsável por adicionar um novo objeto da classe
     * <code>ItensProvisaoVO</code> ao List <code>itensProvisaoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>ItensProvisao</code> - getDescricao() - como identificador (key) do
     * objeto no List.
     *
     * @param obj
     *            Objeto da classe <code>ItensProvisaoVO</code> que será
     *            adiocionado ao Hashtable correspondente.
     */
    public void adicionarObjItensProvisaoVOs(ItensProvisaoVO obj) throws Exception {
        ItensProvisaoVO.validarDados(obj);
        int index = 0;
        Iterator i = getItensProvisaoVOs().iterator();
        while (i.hasNext()) {
            ItensProvisaoVO objExistente = (ItensProvisaoVO) i.next();
            if (objExistente.getDescricao().equals(obj.getDescricao())) {
                getItensProvisaoVOs().set(index, obj);
                return;
            }
            index++;
        }
        getItensProvisaoVOs().add(obj);
    }

    public void gerarMapaLancamentoFuturo() throws Exception {
        MapaLancamentoFuturoVO mapaLancamentoFuturoVO = new MapaLancamentoFuturoVO();
        mapaLancamentoFuturoVO.setTipoOrigem(OrigemMapaLancamentoFuturo.PROVISAO_CUSTO.getValor());
        mapaLancamentoFuturoVO.setTipoMapaLancamentoFuturo(TipoMapaLancamentoFuturo.PROVISAO_CUSTO.getValor());
        mapaLancamentoFuturoVO.setDataPrevisao(getDataPrestacaoConta());
        mapaLancamentoFuturoVO.setDataEmissao(getData());
        mapaLancamentoFuturoVO.setValor(getValor());
        mapaLancamentoFuturoVO.setSacado(getRequisitante().getPessoa().getNome());
        setMapaLancamentoFuturo(mapaLancamentoFuturoVO);
    }

    /**
     * Operação responsável por excluir um objeto da classe
     * <code>ItensProvisaoVO</code> no List <code>itensProvisaoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>ItensProvisao</code> - getDescricao() - como identificador (key) do
     * objeto no List.
     *
     * @param descricao
     *            Parâmetro para localizar e remover o objeto do List.
     */
    public void excluirObjItensProvisaoVOs(String descricao) throws Exception {
        int index = 0;
        Iterator i = getItensProvisaoVOs().iterator();
        while (i.hasNext()) {
            ItensProvisaoVO objExistente = (ItensProvisaoVO) i.next();
            if (objExistente.getDescricao().equals(descricao)) {
                getItensProvisaoVOs().remove(index);
                return;
            }
            index++;
        }
    }

    /**
     * Operação responsável por consultar um objeto da classe
     * <code>ItensProvisaoVO</code> no List <code>itensProvisaoVOs</code>.
     * Utiliza o atributo padrão de consulta da classe
     * <code>ItensProvisao</code> - getDescricao() - como identificador (key) do
     * objeto no List.
     *
     * @param descricao
     *            Parâmetro para localizar o objeto do List.
     */
    public ItensProvisaoVO consultarObjItensProvisaoVO(String descricao) throws Exception {
        Iterator i = getItensProvisaoVOs().iterator();
        while (i.hasNext()) {
            ItensProvisaoVO objExistente = (ItensProvisaoVO) i.next();
            if (objExistente.getDescricao().equals(descricao)) {
                return objExistente;
            }
        }
        return null;
    }

    /**
     * Retorna o objeto da classe <code>ContaCorrente</code> relacionado com (
     * <code>ProvisaoCusto</code>).
     */
    public ContaCorrenteVO getContaCorrenteTroco() {
        if (contaCorrenteTroco == null) {
            contaCorrenteTroco = new ContaCorrenteVO();
        }
        return (contaCorrenteTroco);
    }

    /**
     * Define o objeto da classe <code>ContaCorrente</code> relacionado com (
     * <code>ProvisaoCusto</code>).
     */
    public void setContaCorrenteTroco(ContaCorrenteVO obj) {
        this.contaCorrenteTroco = obj;
    }

    /**
     * Retorna o objeto da classe <code>ContaCorrente</code> relacionado com (
     * <code>ProvisaoCusto</code>).
     */
    public ContaCorrenteVO getContaCorrente() {
        if (contaCorrente == null) {
            contaCorrente = new ContaCorrenteVO();
        }
        return (contaCorrente);
    }

    /**
     * Define o objeto da classe <code>ContaCorrente</code> relacionado com (
     * <code>ProvisaoCusto</code>).
     */
    public void setContaCorrente(ContaCorrenteVO obj) {
        this.contaCorrente = obj;
    }

    /**
     * Retorna o objeto da classe <code>Funcionario</code> relacionado com (
     * <code>ProvisaoCusto</code>).
     */
    public FuncionarioVO getRequisitante() {
        if (requisitante == null) {
            requisitante = new FuncionarioVO();
        }
        return (requisitante);
    }

    /**
     * Define o objeto da classe <code>Funcionario</code> relacionado com (
     * <code>ProvisaoCusto</code>).
     */
    public void setRequisitante(FuncionarioVO obj) {
        this.requisitante = obj;
    }

    /**
     * Retorna o objeto da classe <code>Usuario</code> relacionado com (
     * <code>ProvisaoCusto</code>).
     */
    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return (responsavel);
    }

    /**
     * Define o objeto da classe <code>Usuario</code> relacionado com (
     * <code>ProvisaoCusto</code>).
     */
    public void setResponsavel(UsuarioVO obj) {
        this.responsavel = obj;
    }

    /**
     * Retorna Atributo responsável por manter os objetos da classe
     * <code>ItensProvisao</code>.
     */
    public List getItensProvisaoVOs() {
        if (itensProvisaoVOs == null) {
            itensProvisaoVOs = new ArrayList();
        }
        return (itensProvisaoVOs);
    }

    /**
     * Define Atributo responsável por manter os objetos da classe
     * <code>ItensProvisao</code>.
     */
    public void setItensProvisaoVOs(List itensProvisaoVOs) {
        this.itensProvisaoVOs = itensProvisaoVOs;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return (situacao);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
     */
    public String getSituacao_Apresentar() {
        return SituacaoProvisaoCusto.getDescricao(getSituacao());
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Double getValorTroco() {
        if (valorTroco == null) {
            valorTroco = 0.0;
        }
        return (valorTroco);
    }

    public void setValorTroco(Double valorTroco) {
        this.valorTroco = valorTroco;
    }

    public Date getDataPrestacaoConta() {
        if (dataPrestacaoConta == null) {
            dataPrestacaoConta = new Date();
        }
        return (dataPrestacaoConta);
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato
     * padrão dd/mm/aaaa.
     */
    public String getDataPrestacaoConta_Apresentar() {
        return (Uteis.getData(getDataPrestacaoConta()));
    }

    public void setDataPrestacaoConta(Date dataPrestacaoConta) {
        this.dataPrestacaoConta = dataPrestacaoConta;
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

    public Double getValor() {
        if (valor == null) {
            valor = 0.0;
        }
        return (valor);
    }

    public void setValor(Double valor) {
        this.valor = valor;
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
        return (Uteis.getData(getData()));
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

    /**
     * @return the mapaLancamentoFuturo
     */
    public MapaLancamentoFuturoVO getMapaLancamentoFuturo() {
        if (mapaLancamentoFuturo == null) {
            mapaLancamentoFuturo = new MapaLancamentoFuturoVO();
        }
        return mapaLancamentoFuturo;
    }

    /**
     * @param mapaLancamentoFuturo
     *            the mapaLancamentoFuturo to set
     */
    public void setMapaLancamentoFuturo(MapaLancamentoFuturoVO mapaLancamentoFuturo) {
        this.mapaLancamentoFuturo = mapaLancamentoFuturo;
    }

    /**
     * @return the funcionarioCargoCentroCusto
     */
    public FuncionarioCargoVO getFuncionarioCargoCentroCusto() {
        if (funcionarioCargoCentroCusto == null) {
            funcionarioCargoCentroCusto = new FuncionarioCargoVO();
        }
        return funcionarioCargoCentroCusto;
    }

    /**
     * @param funcionarioCargoCentroCusto
     *            the funcionarioCargoCentroCusto to set
     */
    public void setFuncionarioCargoCentroCusto(FuncionarioCargoVO funcionarioCargoCentroCusto) {
        this.funcionarioCargoCentroCusto = funcionarioCargoCentroCusto;
    }

    /**
     * @return the valorFinal
     */
    public Double getValorFinal() {
        if (valorFinal == null) {
            valorFinal = 0.0;
        }
        return valorFinal;
    }

    /**
     * @param valorFinal
     *            the valorFinal to set
     */
    public void setValorFinal(Double valorFinal) {
        this.valorFinal = valorFinal;
    }
}
