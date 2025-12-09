package negocio.comuns.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.sad.ReceitaDWVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade Recebimento. Classe do tipo VO - Value Object composta pelos atributos da
 * entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class RecebimentoVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private Double valor;
    private Double valorEntrada;
    private Double valorRecebido;
    private String descricao;
    private String nrDocumento;
    private String codigoBarra;
    private String tipoOrigem;
    private String codigoOrigem;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>ContaCorrente </code>.
     */
    private ContaCorrenteVO contaCorrente;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>CentroReceita </code>.
     */
    private CentroReceitaVO centroReceita;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Matricula </code>.
     */
    private MatriculaVO matriculaAluno;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>Pessoa </code>.
     */
    private FuncionarioVO funcionario;
    /**
     * Atributo responsável por manter o objeto relacionado da classe <code>ContaReceber </code>.
     */
    private ContaReceberVO contaReceber;
    private UnidadeEnsinoVO unidadeEnsino;
    private Integer nrParcela;
    private Integer intervaloParcela;
    private List renegociacaoContaReceberVOs;
    private String tipoPessoa;
    private PessoaVO candidato;
    private DescontoProgressivoVO descontoProgressivo;
    private PessoaVO pessoa;
    private Boolean renegociar;
    private ConfiguracaoFinanceiroVO configuracaoFinanceiroVO;
    private FormaPagamentoVO formaPagamentoVO;
    private ChequeVO chequeVO;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>Recebimento</code>. Cria uma nova instância desta entidade, inicializando
     * automaticamente seus atributos (Classe VO).
     */
    public RecebimentoVO() {
        super();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>RecebimentoVO</code>. Todos os tipos de
     * consistência de dados são e devem ser implementadas neste método. São validações típicas: verificação de campos
     * obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo o atributo e o
     *                erro ocorrido.
     */
    public static void validarDados(RecebimentoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Recebimento) deve ser informado.");
        }

        if (obj.getTipoPessoa().equals("AL") && obj.getMatriculaAluno().getMatricula().equals("")) {
            throw new ConsistirException("O campo ALUNO (Recebimento) deve ser informado.");
        } else if (obj.getTipoPessoa().equals("AL")) {
            obj.setPessoa(obj.getMatriculaAluno().getAluno());
        }
        if (obj.getTipoPessoa().equals("FU") && obj.getFuncionario().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo FUNCIONÁRIO (Recebimento) deve ser informado.");
        } else if (obj.getTipoPessoa().equals("FU")) {
            obj.setPessoa(obj.getFuncionario().getPessoa());
        }
        if (obj.getTipoPessoa().equals("CA") && obj.getCandidato().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo CANDIDATO (Recebimento) deve ser informado.");
        } else if (obj.getTipoPessoa().equals("CA")) {
            obj.setPessoa(obj.getCandidato());
        }
        if (obj.getTipoPessoa().equals("RE") && obj.getPessoa().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo REQUERENTE (Recebimento) deve ser informado.");
        } else if (obj.getTipoPessoa().equals("RE")) {
            obj.setPessoa(obj.getPessoa());
        }

        if (obj.getNrDocumento().equals("")) {
            throw new ConsistirException("O campo NR. DOCUMENTO (Recebimento) deve ser informado.");
        }
        if (obj.getValor().doubleValue() == 0) {
            throw new ConsistirException("O campo VALOR (Recebimento) deve ser informado.");
        }

        if (obj.getContaCorrente() == null || obj.getContaCorrente().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo CONTA CORRENTE (Recebimento) deve ser informado.");
        }
        if ((obj.getCentroReceita() == null) || (obj.getCentroReceita().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CENTRO DE RECEITA (Recebimento) deve ser informado.");
        }

    }

    public void montarContaReceberRecebimento(ContaReceberVO obj, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
        setContaReceber(obj);

        setCentroReceita(obj.getCentroReceita());

        setDescontoProgressivo(obj.getDescontoProgressivo());

        getContaCorrente().setCodigo(obj.getContaCorrente());

        getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());

        getPessoa().setCodigo(obj.getPessoa().getCodigo());

        if (obj.getTipoPessoa().equals("AL")) {
            getMatriculaAluno().setMatricula(obj.getMatriculaAluno().getMatricula());
            getMatriculaAluno().getAluno().setNome(obj.getMatriculaAluno().getAluno().getNome());

        } else if (obj.getTipoPessoa().equals("FU")) {
            getFuncionario().setCodigo(obj.getFuncionario().getCodigo());
            getFuncionario().getPessoa().setNome(obj.getFuncionario().getPessoa().getNome());
            getFuncionario().setMatricula(obj.getFuncionario().getMatricula());

        } else if (obj.getTipoPessoa().equals("CA")) {
            getCandidato().setCodigo(obj.getCandidato().getCodigo());
            getCandidato().setNome(obj.getCandidato().getNome());
            getCandidato().setCPF(obj.getCandidato().getCPF());
        } else if (obj.getTipoPessoa().equals("RE")) {
            getPessoa().setCodigo(obj.getPessoa().getCodigo());
            getPessoa().setNome(obj.getPessoa().getNome());
            getPessoa().setCPF(obj.getPessoa().getCPF());
        }

        setDescricao(obj.getDescricaoPagamento());
        setCodigoOrigem(obj.getCodOrigem());
        setTipoOrigem(obj.getTipoOrigem());
        setNrDocumento(obj.getNrDocumento());
        setCodigoBarra(obj.getCodigoBarra());
        setTipoPessoa(obj.getTipoPessoa());
        setConfiguracaoFinanceiroVO(configuracaoFinanceiroVO);
        calcularValorFinal();

    }

    public void calcularValorFinal() {
        Double valorPrm = getContaReceber().getValor();
        Double desconto = getContaReceber().getValorDesconto();
        Double valorFinal = 0.0;
        if (getContaReceber().getTipoDesconto().equals("PO")) {
            valorFinal = valorPrm - (valorPrm * (desconto) / 100);
        } else if (getContaReceber().getTipoDesconto().equals("VA")) {
            valorFinal = valorPrm - desconto;
        } else {
            valorFinal = valorPrm;
        }

        Long diasAtraso = Uteis.nrDiasEntreDatas(new Date(), getContaReceber().getDataVencimento());
        if (diasAtraso > 0) {
            double valorComMulta = (valorPrm * (getContaReceber().getMultaPorcentagem()) / 100);
            if (getConfiguracaoFinanceiroVO().getTipoCalculoJuro().equals("CO")) {
                double atraso = ((diasAtraso * 100.0) / 30.0) / 100.0;
                double valorComJuro = valorPrm * Math.pow(((getContaReceber().getJuroPorcentagem() / 100) * 1), atraso);
                valorFinal = valorPrm + valorComMulta + valorComJuro;
            } else {
                double valorComJuro = (valorPrm * (getContaReceber().getJuroPorcentagem() / 100) * 1);
                valorFinal = valorPrm + valorComMulta + ((valorComJuro / 30.0) * diasAtraso);
            }
            setValorRecebido(Uteis.arredondar(valorFinal, 2, 0));
            setValor(Uteis.arredondar(valorFinal, 2, 0));
        } else {
            Long diasAntecipados = Uteis.nrDiasEntreDatas(getContaReceber().getDataVencimento(), new Date());
            if (getContaReceber().getDescontoProgressivo().getCodigo().intValue() != 0) {
                if (diasAntecipados >= getContaReceber().getDescontoProgressivo().getDiaLimite1()) {
                    valorFinal = valorPrm
                            - (valorPrm * (getContaReceber().getDescontoProgressivo().getPercDescontoLimite1()) / 100);
                } else if (diasAntecipados >= getContaReceber().getDescontoProgressivo().getDiaLimite2()) {
                    valorFinal = valorPrm
                            - (valorPrm * (getContaReceber().getDescontoProgressivo().getPercDescontoLimite2()) / 100);
                } else if (diasAntecipados >= getContaReceber().getDescontoProgressivo().getDiaLimite3()) {
                    valorFinal = valorPrm
                            - (valorPrm * (getContaReceber().getDescontoProgressivo().getPercDescontoLimite3()) / 100);
                } else if (diasAntecipados >= getContaReceber().getDescontoProgressivo().getDiaLimite4()) {
                    valorFinal = valorPrm
                            - (valorPrm * (getContaReceber().getDescontoProgressivo().getPercDescontoLimite4()) / 100);
                }
            }
            setValorRecebido(Uteis.arredondar(valorFinal, 2, 0));
            setValor(Uteis.arredondar(valorFinal, 2, 0));
        }

    }

    public Boolean getExisteContaReceber() {
        if (getContaReceber().getCodigo().intValue() != 0) {
            return true;
        }
        return false;
    }

    public Boolean getTipoCandidato() {
        if (getTipoPessoa().equals("CA")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;

    }

    public Boolean getTipoRequisitante() {
        if (getTipoPessoa().equals("RE")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;

    }

    public Boolean getTipoFuncionario() {
        if (getTipoPessoa().equals("FU")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;

    }

    public Boolean getTipoAluno() {
        if (getTipoPessoa().equals("AL")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;

    }

    public void validarDadosRenegociacao() throws ConsistirException {
        if (getNrParcela().intValue() == 0) {
            throw new ConsistirException("O campo NÚMERO DE PARCELAS (Renegociação Conta Receber) deve ser informado.");
        }
        if (getIntervaloParcela().intValue() == 0) {
            throw new ConsistirException("O campo INTERVALO ENTRE PARCELAS (Renegociação Conta Receber) deve ser informado.");
        }
    }

    public void gerarParcelas() throws ConsistirException, Exception {

        validarDadosRenegociacao();
        int nrParcela = 1;
        setRenegociacaoContaReceberVOs(new ArrayList(0));
        ContaReceberVO obj = new ContaReceberVO();
        calcularValorFinal();
        double valor = getValorRecebido() - getValorEntrada();
        while (getNrParcela().intValue() >= nrParcela) {

            Date dataPrevisao = new Date();
            obj.setParcela(String.valueOf(nrParcela) + "/" + String.valueOf(getNrParcela()));
            dataPrevisao = (Uteis.obterDataFutura(dataPrevisao, getIntervaloParcela().intValue() * nrParcela));
            obj.setDataVencimento(dataPrevisao);

            double valorRestante = valor / getNrParcela().intValue();

            obj.setValor(Uteis.arredondar(new Double(valorRestante), 2, 0));
            // obj.setValorFinal(Uteis.arredondar(new Double(valorRestante), 2,
            // 0));
            obj.setNrDocumento(getNrDocumento() + "-" + nrParcela);

            obj.getCentroReceita().setCodigo(getCentroReceita().getCodigo());
            obj.setContaCorrente(getContaCorrente().getCodigo());

            obj.getMatriculaAluno().setMatricula(getMatriculaAluno().getMatricula());
            obj.getFuncionario().setCodigo(getFuncionario().getCodigo());
            obj.getCandidato().setCodigo(getCandidato().getCodigo());
            obj.getUnidadeEnsino().setCodigo(getUnidadeEnsino().getCodigo());
            obj.setOrigemNegociacaoReceber(getContaReceber().getCodigo());
            obj.setDescricaoPagamento("(Renegociação)\n" + getContaReceber().getDescricaoPagamento());
            obj.setTipoOrigem(getContaReceber().getTipoOrigem());
            obj.setTipoPessoa(getContaReceber().getTipoPessoa());
            obj.setCodOrigem(getContaReceber().getCodOrigem());
            obj.setJuroPorcentagem(getContaReceber().getJuroPorcentagem());
            obj.setMultaPorcentagem(getContaReceber().getMultaPorcentagem());

            obj.getPessoa().setCodigo(getPessoa().getCodigo());
            obj.getDescontoProgressivo().setCodigo(getDescontoProgressivo().getCodigo());
            obj.setData(new Date());
            this.getRenegociacaoContaReceberVOs().add(obj);
            obj = new ContaReceberVO();
            nrParcela++;
        }
        setValorRecebido(getValorEntrada());
    }

    public String getDadosPessoaAtiva() {
        String pessoaAtiva = "";
        if (getTipoPessoa().equals("CA")) {
            pessoaAtiva = getTipoPessoa_apresentar().toUpperCase() + " - " + candidato.getNome() + " - "
                    + candidato.getCPF();
        } else if (getTipoPessoa().equals("AL")) {
            pessoaAtiva = getTipoPessoa_apresentar().toUpperCase() + " - " + matriculaAluno.getAluno().getNome()
                    + " - " + matriculaAluno.getMatricula();
        } else if (getTipoPessoa().equals("FU")) {
            pessoaAtiva = getTipoPessoa_apresentar().toUpperCase() + " - " + funcionario.getPessoa().getNome() + " - "
                    + funcionario.getMatricula();

        } else if (getTipoPessoa().equals("RE")) {
            pessoaAtiva = getTipoPessoa_apresentar().toUpperCase() + " - " + getPessoa().getNome() + " - "
                    + getPessoa().getCPF();
        }
        return pessoaAtiva;
    }

    public Boolean getExisteDescontoProgressivo() {
        if (getDescontoProgressivo().getCodigo().intValue() != 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public String getDescricaoDescontoProgressivo() {
        String descricao = "";
        if (getDescontoProgressivo().getCodigo().intValue() != 0) {
            descricao = descricao + "Desconto de "
                    + Uteis.arredondar(getDescontoProgressivo().getPercDescontoLimite1(), 2, 0) + "% até "
                    + getDescontoProgressivo().getDiaLimite1() + " dias antes do vencimento da fatura";
            descricao = descricao + "\nDesconto de "
                    + Uteis.arredondar(getDescontoProgressivo().getPercDescontoLimite2(), 2, 0) + "% até "
                    + getDescontoProgressivo().getDiaLimite2() + " dias antes do vencimento da fatura";
            descricao = descricao + "\nDesconto de "
                    + Uteis.arredondar(getDescontoProgressivo().getPercDescontoLimite3(), 2, 0) + "% até "
                    + getDescontoProgressivo().getDiaLimite3() + " dias antes do vencimento da fatura";
            descricao = descricao + "\nDesconto de "
                    + Uteis.arredondar(getDescontoProgressivo().getPercDescontoLimite4(), 2, 0) + "% até "
                    + getDescontoProgressivo().getDiaLimite4() + " dias antes do vencimento da fatura";
        }
        return descricao;
    }

    public ReceitaDWVO getReceitaDWVO(MatriculaVO matricula, MatriculaPeriodoVO matriculaPeriodo, InscricaoVO inscricao) {
        ReceitaDWVO obj = new ReceitaDWVO();
        obj.setAno(Integer.parseInt(Uteis.getAnoDataAtual4Digitos()));
        obj.setMes(Uteis.getMesDataAtual());
        obj.getCentroReceita().setCodigo(getCentroReceita().getCodigo());
        obj.getCurso().setCodigo(matricula.getCurso().getCodigo());
        obj.setNivelEducacional(matricula.getCurso().getNivelEducacional());
        obj.getProcessoMatricula().setCodigo(matriculaPeriodo.getProcessoMatricula());
        obj.getProcSeletivo().setCodigo(inscricao.getProcSeletivo().getCodigo());
        obj.getTurno().setCodigo(matricula.getTurno().getCodigo());
        obj.getUnidadeEnsino().setCodigo(getUnidadeEnsino().getCodigo());
        obj.setValor(getValorRecebido());
        obj.getAreaConhecimento().setCodigo(matricula.getCurso().getAreaConhecimento().getCodigo());
        obj.setData(new Date());

        return obj;
    }

    public DescontoProgressivoVO getDescontoProgressivo() {
        if (descontoProgressivo == null) {
            descontoProgressivo = new DescontoProgressivoVO();
        }
        return descontoProgressivo;
    }

    public void setDescontoProgressivo(DescontoProgressivoVO descontoProgressivo) {
        this.descontoProgressivo = descontoProgressivo;
    }

    public Integer getIntervaloParcela() {
        if (intervaloParcela == null) {
            intervaloParcela = 0;
        }
        return intervaloParcela;
    }

    public void setIntervaloParcela(Integer intervaloParcela) {
        this.intervaloParcela = intervaloParcela;
    }

    public Integer getNrParcela() {
        if (nrParcela == null) {
            nrParcela = 0;
        }
        return nrParcela;
    }

    public void setNrParcela(Integer nrParcela) {
        this.nrParcela = nrParcela;
    }

    public List getRenegociacaoContaReceberVOs() {
        if (renegociacaoContaReceberVOs == null) {
            renegociacaoContaReceberVOs = new ArrayList(0);
        }
        return renegociacaoContaReceberVOs;
    }

    public void setRenegociacaoContaReceberVOs(List renegociacaoContaReceberVOs) {
        this.renegociacaoContaReceberVOs = renegociacaoContaReceberVOs;
    }

    public Boolean getRenegociar() {
        if (renegociar == null) {
            renegociar = Boolean.FALSE;
        }
        return renegociar;
    }

    public void setRenegociar(Boolean renegociar) {
        this.renegociar = renegociar;
    }

    /**
     * Retorna o objeto da classe <code>ContaReceber</code> relacionado com ( <code>Recebimento</code>).
     */
    public ContaReceberVO getContaReceber() {
        if (contaReceber == null) {
            contaReceber = new ContaReceberVO();
        }
        return (contaReceber);
    }

    /**
     * Define o objeto da classe <code>ContaReceber</code> relacionado com ( <code>Recebimento</code>).
     */
    public void setContaReceber(ContaReceberVO obj) {
        this.contaReceber = obj;
    }

    public PessoaVO getCandidato() {
        if (candidato == null) {
            candidato = new PessoaVO();
        }
        return candidato;
    }

    public void setCandidato(PessoaVO candidato) {
        this.candidato = candidato;
    }

    public String getTipoPessoa_apresentar() {
        if (tipoPessoa.equals("CA")) {
            return "Candidato";
        }
        if (tipoPessoa.equals("FU")) {
            return "Funcionario";
        }
        if (tipoPessoa.equals("AL")) {
            return "Aluno";
        }
        if (tipoPessoa.equals("RE")) {
            return "Requisitante";
        }
        return tipoPessoa;
    }

    public String getTipoPessoa() {
        if (tipoPessoa == null) {
            tipoPessoa = "";
        }
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    /**
     * Retorna o objeto da classe <code>Pessoa</code> relacionado com ( <code>Recebimento</code>).
     */
    public FuncionarioVO getFuncionario() {
        if (funcionario == null) {
            funcionario = new FuncionarioVO();
        }
        return (funcionario);
    }

    /**
     * Define o objeto da classe <code>Pessoa</code> relacionado com ( <code>Recebimento</code>).
     */
    public void setFuncionario(FuncionarioVO obj) {
        this.funcionario = obj;
    }

    /**
     * Retorna o objeto da classe <code>Matricula</code> relacionado com ( <code>Recebimento</code>).
     */
    public MatriculaVO getMatriculaAluno() {
        if (matriculaAluno == null) {
            matriculaAluno = new MatriculaVO();
        }
        return (matriculaAluno);
    }

    /**
     * Define o objeto da classe <code>Matricula</code> relacionado com ( <code>Recebimento</code>).
     */
    public void setMatriculaAluno(MatriculaVO obj) {
        this.matriculaAluno = obj;
    }

    /**
     * Retorna o objeto da classe <code>CentroReceita</code> relacionado com ( <code>Recebimento</code>).
     */
    public CentroReceitaVO getCentroReceita() {
        if (centroReceita == null) {
            centroReceita = new CentroReceitaVO();
        }
        return (centroReceita);
    }

    /**
     * Define o objeto da classe <code>CentroReceita</code> relacionado com ( <code>Recebimento</code>).
     */
    public void setCentroReceita(CentroReceitaVO obj) {
        this.centroReceita = obj;
    }

    /**
     * Retorna o objeto da classe <code>ContaCorrente</code> relacionado com ( <code>Recebimento</code>).
     */
    public ContaCorrenteVO getContaCorrente() {
        if (contaCorrente == null) {
            contaCorrente = new ContaCorrenteVO();
        }
        return (contaCorrente);
    }

    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return pessoa;
    }

    public void setPessoa(PessoaVO pessoa) {
        this.pessoa = pessoa;
    }

    /**
     * Define o objeto da classe <code>ContaCorrente</code> relacionado com ( <code>Recebimento</code>).
     */
    public void setContaCorrente(ContaCorrenteVO obj) {
        this.contaCorrente = obj;
    }

    public String getCodigoOrigem() {
        if (codigoOrigem == null) {
            codigoOrigem = "";
        }
        return (codigoOrigem);
    }

    public void setCodigoOrigem(String codigoOrigem) {
        this.codigoOrigem = codigoOrigem;
    }

    public String getTipoOrigem() {
        if (tipoOrigem == null) {
            tipoOrigem = "";
        }
        return (tipoOrigem);
    }

    public void setTipoOrigem(String tipoOrigem) {
        this.tipoOrigem = tipoOrigem;
    }

    public String getTipoOrigem_apresentar() {
        if (getTipoOrigem().equals("IPS")) {
            return ("Inscrição Processo Seletivo");
        }
        if (getTipoOrigem().equals("MAT")) {
            return ("Matrícula");
        }
        if (getTipoOrigem().equals("REQ")) {
            return ("Requerimento");
        }
        if (getTipoOrigem().equals("BIB")) {
            return ("Biblioteca");
        }
        if (getTipoOrigem().equals("MEN")) {
            return ("Mensalidade");
        }
        return "";
    }

    public String getNrDocumento() {
        if (nrDocumento == null) {
            nrDocumento = "";
        }
        return (nrDocumento);
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
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

    public String getCodigoBarra() {
        if (codigoBarra == null) {
            codigoBarra = "";
        }
        return codigoBarra;
    }

    public void setCodigoBarra(String codigoBarra) {
        this.codigoBarra = codigoBarra;
    }

    public Boolean getEditarDados() {
        if (getCodigo().intValue() != 0) {
            return true;
        }
        return false;
    }

    public Double getValorRecebido() {
        if (!getExisteContaReceber()) {
            valorRecebido = valor;
        }
        return valorRecebido;
    }

    public void setValorRecebido(Double valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public Double getValorEntrada() {
        if (valorEntrada == null) {
            valorEntrada = 0.0;
        }
        return valorEntrada;
    }

    public void setValorEntrada(Double valorEntrada) {
        this.valorEntrada = valorEntrada;
    }

    public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO() {
        if (configuracaoFinanceiroVO == null) {
            configuracaoFinanceiroVO = new ConfiguracaoFinanceiroVO();
        }
        return configuracaoFinanceiroVO;
    }

    public void setConfiguracaoFinanceiroVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
        this.configuracaoFinanceiroVO = configuracaoFinanceiroVO;
    }

    /**
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa.
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

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    /**
     * @return the formaPagamentoVO
     */
    public FormaPagamentoVO getFormaPagamentoVO() {
        if (formaPagamentoVO == null) {
            formaPagamentoVO = new FormaPagamentoVO();
        }
        return formaPagamentoVO;
    }

    /**
     * @param formaPagamentoVO
     *            the formaPagamentoVO to set
     */
    public void setFormaPagamentoVO(FormaPagamentoVO formaPagamentoVO) {
        this.formaPagamentoVO = formaPagamentoVO;
    }

    /**
     * @return the chequeVO
     */
    public ChequeVO getChequeVO() {
        if (chequeVO == null) {
            chequeVO = new ChequeVO();
        }
        return chequeVO;
    }

    /**
     * @param chequeVO
     *            the chequeVO to set
     */
    public void setChequeVO(ChequeVO chequeVO) {
        this.chequeVO = chequeVO;
    }
}
