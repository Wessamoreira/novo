package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

/**
 * Reponsável por manter os dados da entidade ContaReceber. Classe do tipo VO - Value Object composta pelos atributos da
 * entidade com visibilidade protegida e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 *
 * @see SuperVO
 */
public class ContaReceberLogVO extends SuperVO {

    private Integer codigo;
    private Integer contaReceber;
    private DescontoProgressivoVO descontoProgressivo;
    private ContaCorrenteVO contaCorrente;
    private FuncionarioVO funcionario;
    private PessoaVO pessoa;
    private MatriculaVO matriculaAluno;
    private String tipoOrigem;
    private String parcela;
    private String codigoBarra;
    private String nrDocumento;
    private Double valorDesconto;
    private Double valor;
    private Date dataVencimento;
    private String descricaoPagamento;
    private String situacao;
    private String codOrigem;
    private Date data;
    private PessoaVO candidato;
    private String tipoPessoa;
    private Double valorRecebido;
    private String tipoDesconto;
    private UnidadeEnsinoVO unidadeEnsino;
    private Double valorDescontoRecebido;
    private Double valorDescontoInstituicao;
    private Double valorDescontoConvenio;
    private ConvenioVO convenio;
    private String linhaDigitavelCodigoBarras;
    private String nossoNumero;
    private ParceiroVO parceiro;
    private FornecedorVO fornecedor;
    private TurmaVO turma;
    private MatriculaPeriodoVO matriculaPeriodo;
    private Double valorDescontoProgressivo;
    private Boolean recebimentoBancario;
    private Integer ordemDescontoAluno;
    private Boolean ordemDescontoAlunoValorCheio;
    private Integer ordemPlanoDesconto;
    private Boolean ordemPlanoDescontoValorCheio;
    private Integer ordemConvenio;
    private Boolean ordemConvenioValorCheio;
    private Integer ordemDescontoProgressivo;
    private Boolean ordemDescontoProgressivoValorCheio;
    private String justificativaDesconto;
    private String descontoProgressivoUtilizado;
    private Double valorDescontoCalculadoPrimeiraFaixaDescontos;
    private Double valorDescontoCalculadoSegundaFaixaDescontos;
    private Double valorDescontoCalculadoTerceiraFaixaDescontos;
    private Double valorDescontoCalculadoQuartaFaixaDescontos;
    private Double valorDescontoLancadoRecebimento;
    private String tipoDescontoLancadoRecebimento;
    private Double valorCalculadoDescontoLancadoRecebimento;
    private Double valorDescontoAlunoJaCalculado;
    private Date dataCriacao;
    private Double acrescimo;
    private Boolean usaDescontoCompostoPlanoDesconto;
    // Código do usuário
    private UsuarioVO responsavel;
    private String operacao;
    private PessoaVO responsavelFinanceiro;
    private Double juro;
    private Double juroPorcentagem;
    private Double multa;
    private Double multaPorcentagem;
    public static final long serialVersionUID = 1L;

    public ContaReceberLogVO() {
        super();
    }

    public Double getAcrescimo() {
        if (acrescimo == null) {
            acrescimo = 0.0;
        }
        return acrescimo;
    }

    public void setAcrescimo(Double acrescimo) {
        this.acrescimo = acrescimo;
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

    public String getCodOrigem() {
        if (codOrigem == null) {
            codOrigem = "";
        }
        return codOrigem;
    }

    public void setCodOrigem(String codOrigem) {
        this.codOrigem = codOrigem;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
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

    public ContaCorrenteVO getContaCorrente() {
        if (contaCorrente == null) {
            contaCorrente = new ContaCorrenteVO();
        }
        return contaCorrente;
    }

    public void setContaCorrente(ContaCorrenteVO contaCorrente) {
        this.contaCorrente = contaCorrente;
    }

    public Integer getContaReceber() {
        if (contaReceber == null) {
            contaReceber = 0;
        }
        return contaReceber;
    }

    public void setContaReceber(Integer contaReceber) {
        this.contaReceber = contaReceber;
    }

    public ConvenioVO getConvenio() {
        if (convenio == null) {
            convenio = new ConvenioVO();
        }
        return convenio;
    }

    public void setConvenio(ConvenioVO convenio) {
        this.convenio = convenio;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Date getDataVencimento() {
        return dataVencimento;
    }

    public void setDataVencimento(Date dataVencimento) {
        this.dataVencimento = dataVencimento;
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

    public String getDescontoProgressivoUtilizado() {
        return descontoProgressivoUtilizado;
    }

    public void setDescontoProgressivoUtilizado(String descontoProgressivoUtilizado) {
        this.descontoProgressivoUtilizado = descontoProgressivoUtilizado;
    }

    public String getDescricaoPagamento() {
        if (descricaoPagamento == null) {
            descricaoPagamento = "";
        }
        return descricaoPagamento;
    }

    public void setDescricaoPagamento(String descricaoPagamento) {
        this.descricaoPagamento = descricaoPagamento;
    }

    public FuncionarioVO getFuncionario() {
        if (funcionario == null) {
            funcionario = new FuncionarioVO();
        }
        return funcionario;
    }

    public void setFuncionario(FuncionarioVO funcionario) {
        this.funcionario = funcionario;
    }

    public String getJustificativaDesconto() {
        if (justificativaDesconto == null) {
            justificativaDesconto = "";
        }
        return justificativaDesconto;
    }

    public void setJustificativaDesconto(String justificativaDesconto) {
        this.justificativaDesconto = justificativaDesconto;
    }

    public String getLinhaDigitavelCodigoBarras() {
        if (linhaDigitavelCodigoBarras == null) {
            linhaDigitavelCodigoBarras = "";
        }
        return linhaDigitavelCodigoBarras;
    }

    public void setLinhaDigitavelCodigoBarras(String linhaDigitavelCodigoBarras) {
        this.linhaDigitavelCodigoBarras = linhaDigitavelCodigoBarras;
    }

    public MatriculaVO getMatriculaAluno() {
        if (matriculaAluno == null) {
            matriculaAluno = new MatriculaVO();
        }
        return matriculaAluno;
    }

    public void setMatriculaAluno(MatriculaVO matriculaAluno) {
        this.matriculaAluno = matriculaAluno;
    }

    public MatriculaPeriodoVO getMatriculaPeriodo() {
        if (matriculaPeriodo == null) {
            matriculaPeriodo = new MatriculaPeriodoVO();
        }
        return matriculaPeriodo;
    }

    public void setMatriculaPeriodo(MatriculaPeriodoVO matriculaPeriodo) {
        this.matriculaPeriodo = matriculaPeriodo;
    }

    public String getNossoNumero() {
        if (nossoNumero == null) {
            nossoNumero = "";
        }
        return nossoNumero;
    }

    public void setNossoNumero(String nossoNumero) {
        this.nossoNumero = nossoNumero;
    }

    public String getNrDocumento() {
        if (nrDocumento == null) {
            nrDocumento = "";
        }
        return nrDocumento;
    }

    public void setNrDocumento(String nrDocumento) {
        this.nrDocumento = nrDocumento;
    }

    public Integer getOrdemConvenio() {
        if (ordemConvenio == null) {
            ordemConvenio = 0;
        }
        return ordemConvenio;
    }

    public void setOrdemConvenio(Integer ordemConvenio) {
        this.ordemConvenio = ordemConvenio;
    }

    public Boolean getOrdemConvenioValorCheio() {
        if (ordemConvenioValorCheio == null) {
            ordemConvenioValorCheio = false;
        }
        return ordemConvenioValorCheio;
    }

    public void setOrdemConvenioValorCheio(Boolean ordemConvenioValorCheio) {
        this.ordemConvenioValorCheio = ordemConvenioValorCheio;
    }

    public Integer getOrdemDescontoAluno() {
        if (ordemDescontoAluno == null) {
            ordemDescontoAluno = 0;
        }
        return ordemDescontoAluno;
    }

    public void setOrdemDescontoAluno(Integer ordemDescontoAluno) {
        this.ordemDescontoAluno = ordemDescontoAluno;
    }

    public Boolean getOrdemDescontoAlunoValorCheio() {
        if (ordemDescontoAlunoValorCheio == null) {
            ordemDescontoAlunoValorCheio = false;
        }
        return ordemDescontoAlunoValorCheio;
    }

    public void setOrdemDescontoAlunoValorCheio(Boolean ordemDescontoAlunoValorCheio) {
        this.ordemDescontoAlunoValorCheio = ordemDescontoAlunoValorCheio;
    }

    public Integer getOrdemDescontoProgressivo() {
        if (ordemDescontoProgressivo == null) {
            ordemDescontoProgressivo = 0;
        }
        return ordemDescontoProgressivo;
    }

    public void setOrdemDescontoProgressivo(Integer ordemDescontoProgressivo) {
        this.ordemDescontoProgressivo = ordemDescontoProgressivo;
    }

    public Boolean getOrdemDescontoProgressivoValorCheio() {
        if (ordemDescontoProgressivoValorCheio == null) {
            ordemDescontoProgressivoValorCheio = false;
        }
        return ordemDescontoProgressivoValorCheio;
    }

    public void setOrdemDescontoProgressivoValorCheio(Boolean ordemDescontoProgressivoValorCheio) {
        this.ordemDescontoProgressivoValorCheio = ordemDescontoProgressivoValorCheio;
    }

    public Integer getOrdemPlanoDesconto() {
        if (ordemPlanoDesconto == null) {
            ordemPlanoDesconto = 0;
        }
        return ordemPlanoDesconto;
    }

    public void setOrdemPlanoDesconto(Integer ordemPlanoDesconto) {
        this.ordemPlanoDesconto = ordemPlanoDesconto;
    }

    public Boolean getOrdemPlanoDescontoValorCheio() {
        if (ordemPlanoDescontoValorCheio == null) {
            ordemPlanoDescontoValorCheio = false;
        }
        return ordemPlanoDescontoValorCheio;
    }

    public void setOrdemPlanoDescontoValorCheio(Boolean ordemPlanoDescontoValorCheio) {
        this.ordemPlanoDescontoValorCheio = ordemPlanoDescontoValorCheio;
    }

    public ParceiroVO getParceiro() {
        if (parceiro == null) {
            parceiro = new ParceiroVO();
        }
        return parceiro;
    }

    public void setParceiro(ParceiroVO parceiro) {
        this.parceiro = parceiro;
    }

    public String getParcela() {
        if (parcela == null) {
            parcela = "";
        }
        return parcela;
    }

    public void setParcela(String parcela) {
        this.parcela = parcela;
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

    public Boolean getRecebimentoBancario() {
        return recebimentoBancario;
    }

    public void setRecebimentoBancario(Boolean recebimentoBancario) {
        this.recebimentoBancario = recebimentoBancario;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getTipoDesconto() {
        if (tipoDesconto == null) {
            tipoDesconto = "";
        }
        return tipoDesconto;
    }

    public void setTipoDesconto(String tipoDesconto) {
        this.tipoDesconto = tipoDesconto;
    }

    public String getTipoDescontoLancadoRecebimento() {
        if (tipoDescontoLancadoRecebimento == null) {
            tipoDescontoLancadoRecebimento = "";
        }
        return tipoDescontoLancadoRecebimento;
    }

    public void setTipoDescontoLancadoRecebimento(String tipoDescontoLancadoRecebimento) {
        this.tipoDescontoLancadoRecebimento = tipoDescontoLancadoRecebimento;
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

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public Boolean getUsaDescontoCompostoPlanoDesconto() {
        if (usaDescontoCompostoPlanoDesconto == null) {
            usaDescontoCompostoPlanoDesconto = false;
        }
        return usaDescontoCompostoPlanoDesconto;
    }

    public void setUsaDescontoCompostoPlanoDesconto(Boolean usaDescontoCompostoPlanoDesconto) {
        this.usaDescontoCompostoPlanoDesconto = usaDescontoCompostoPlanoDesconto;
    }

    public Double getValor() {
        if (valor == null) {
            valor = 0.0;
        }
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getValorCalculadoDescontoLancadoRecebimento() {
        if (valorCalculadoDescontoLancadoRecebimento == null) {
            valorCalculadoDescontoLancadoRecebimento = 0.0;
        }
        return valorCalculadoDescontoLancadoRecebimento;
    }

    public void setValorCalculadoDescontoLancadoRecebimento(Double valorCalculadoDescontoLancadoRecebimento) {
        this.valorCalculadoDescontoLancadoRecebimento = valorCalculadoDescontoLancadoRecebimento;
    }

    public Double getValorDesconto() {
        if (valorDesconto == null) {
            valorDesconto = 0.0;
        }
        return valorDesconto;
    }

    public void setValorDesconto(Double valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    public Double getValorDescontoAlunoJaCalculado() {
        if (valorDescontoAlunoJaCalculado == null) {
            valorDescontoAlunoJaCalculado = 0.0;
        }
        return valorDescontoAlunoJaCalculado;
    }

    public void setValorDescontoAlunoJaCalculado(Double valorDescontoAlunoJaCalculado) {
        this.valorDescontoAlunoJaCalculado = valorDescontoAlunoJaCalculado;
    }

    public Double getValorDescontoCalculadoPrimeiraFaixaDescontos() {
        if (valorDescontoCalculadoPrimeiraFaixaDescontos == null) {
            valorDescontoCalculadoPrimeiraFaixaDescontos = 0.0;
        }
        return valorDescontoCalculadoPrimeiraFaixaDescontos;
    }

    public void setValorDescontoCalculadoPrimeiraFaixaDescontos(Double valorDescontoCalculadoPrimeiraFaixaDescontos) {
        this.valorDescontoCalculadoPrimeiraFaixaDescontos = valorDescontoCalculadoPrimeiraFaixaDescontos;
    }

    public Double getValorDescontoCalculadoQuartaFaixaDescontos() {
        if (valorDescontoCalculadoQuartaFaixaDescontos == null) {
            valorDescontoCalculadoQuartaFaixaDescontos = 0.0;
        }
        return valorDescontoCalculadoQuartaFaixaDescontos;
    }

    public void setValorDescontoCalculadoQuartaFaixaDescontos(Double valorDescontoCalculadoQuartaFaixaDescontos) {
        this.valorDescontoCalculadoQuartaFaixaDescontos = valorDescontoCalculadoQuartaFaixaDescontos;
    }

    public Double getValorDescontoCalculadoSegundaFaixaDescontos() {
        if (valorDescontoCalculadoSegundaFaixaDescontos == null) {
            valorDescontoCalculadoSegundaFaixaDescontos = 0.0;
        }
        return valorDescontoCalculadoSegundaFaixaDescontos;
    }

    public void setValorDescontoCalculadoSegundaFaixaDescontos(Double valorDescontoCalculadoSegundaFaixaDescontos) {
        this.valorDescontoCalculadoSegundaFaixaDescontos = valorDescontoCalculadoSegundaFaixaDescontos;
    }

    public Double getValorDescontoCalculadoTerceiraFaixaDescontos() {
        if (valorDescontoCalculadoTerceiraFaixaDescontos == null) {
            valorDescontoCalculadoTerceiraFaixaDescontos = 0.0;
        }
        return valorDescontoCalculadoTerceiraFaixaDescontos;
    }

    public void setValorDescontoCalculadoTerceiraFaixaDescontos(Double valorDescontoCalculadoTerceiraFaixaDescontos) {
        this.valorDescontoCalculadoTerceiraFaixaDescontos = valorDescontoCalculadoTerceiraFaixaDescontos;
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

    public Double getValorDescontoLancadoRecebimento() {
        if (valorDescontoLancadoRecebimento == null) {
            valorDescontoLancadoRecebimento = 0.0;
        }
        return valorDescontoLancadoRecebimento;
    }

    public void setValorDescontoLancadoRecebimento(Double valorDescontoLancadoRecebimento) {
        this.valorDescontoLancadoRecebimento = valorDescontoLancadoRecebimento;
    }

    public Double getValorDescontoProgressivo() {
        if (valorDescontoProgressivo == null) {
            valorDescontoProgressivo = 0.0;
        }
        return valorDescontoProgressivo;
    }

    public void setValorDescontoProgressivo(Double valorDescontoProgressivo) {
        this.valorDescontoProgressivo = valorDescontoProgressivo;
    }

    public Double getValorDescontoRecebido() {
        if (valorDescontoRecebido == null) {
            valorDescontoRecebido = 0.0;
        }
        return valorDescontoRecebido;
    }

    public void setValorDescontoRecebido(Double valorDescontoRecebido) {
        this.valorDescontoRecebido = valorDescontoRecebido;
    }

    public Double getValorRecebido() {
        if (valorRecebido == null) {
            valorRecebido = 0.0;
        }
        return valorRecebido;
    }

    public void setValorRecebido(Double valorRecebido) {
        this.valorRecebido = valorRecebido;
    }

    public UsuarioVO getResponsavel() {
        if (responsavel == null) {
            responsavel = new UsuarioVO();
        }
        return responsavel;
    }

    public void setResponsavel(UsuarioVO responsavel) {
        this.responsavel = responsavel;
    }

    public String getOperacao() {
        if (operacao == null) {
            operacao = "";
        }
        return operacao;
    }

    public void setOperacao(String operacao) {
        this.operacao = operacao;
    }

    public PessoaVO getResponsavelFinanceiro() {
        if (responsavelFinanceiro == null) {
            responsavelFinanceiro = new PessoaVO();
        }
        return responsavelFinanceiro;
    }

    public void setResponsavelFinanceiro(PessoaVO responsavelFinanceiro) {
        this.responsavelFinanceiro = responsavelFinanceiro;
    }

    public FornecedorVO getFornecedor() {
        if (fornecedor == null) {
            fornecedor = new FornecedorVO();
        }
        return fornecedor;
    }

    public void setFornecedor(FornecedorVO fornecedor) {
        this.fornecedor = fornecedor;
    }

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
    }

    public String getTipoOrigem_apresentar() {
        return TipoOrigemContaReceber.getDescricao(getTipoOrigem());
    }

    public String getTipoOrigem() {
        if (tipoOrigem == null) {
            tipoOrigem = "";
        }
        return tipoOrigem;
    }

    public void setTipoOrigem(String tipoOrigem) {
        this.tipoOrigem = tipoOrigem;
    }

    public Boolean getTipoAluno() {
        if (getTipoPessoa().equals("AL")) {
            return Boolean.TRUE;
        }
        return (Boolean.FALSE);
    }

    public Boolean getTipoResponsavelFinanceiro() {
        return getTipoPessoa().equals(TipoPessoa.RESPONSAVEL_FINANCEIRO.getValor());
    }

    public Boolean getTipoFornecedor() {
        return getTipoPessoa().equals(TipoPessoa.FORNECEDOR.getValor());
    }

    public Boolean getTipoRequerente() {
        if (getTipoPessoa().equals("RE")) {
            return Boolean.TRUE;
        }
        return (Boolean.FALSE);
    }

    public Boolean getTipoFuncionario() {
        if (getTipoPessoa().equals("FU")) {
            return (Boolean.TRUE);
        }
        return (Boolean.FALSE);
    }

    public Boolean getTipoParceiro() {
        if (getTipoPessoa().equals("PA")) {
            return (Boolean.TRUE);
        }
        return (Boolean.FALSE);
    }

    public Boolean getTipoCandidato() {
        if (getTipoPessoa().equals("CA")) {
            return (Boolean.TRUE);
        }
        return (Boolean.FALSE);
    }

    public Boolean getPossuiOrigem() {
        if (getTipoOrigem().equals("")) {
            return false;
        }
        return true;
    }

    public String getTipoPessoaApresentar() {
        return TipoPessoa.getDescricao(getTipoPessoa());
    }

    public String getDadosPessoaAtiva() {
        try {
            TipoPessoa tipoPessoaLocal = TipoPessoa.getEnum(getTipoPessoa());
            if (tipoPessoaLocal != null) {
                switch (tipoPessoaLocal) {
                    case ALUNO:
                        if (getMatriculaAluno().getMatricula() != null && !getMatriculaAluno().getMatricula().equals("")) {
                            return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getMatriculaAluno().getAluno().getNome() + " - " + getMatriculaAluno().getMatricula();
                        }
                        return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getPessoa().getNome();
                    case RESPONSAVEL_FINANCEIRO:
                        if (getMatriculaAluno().getMatricula() != null && !getMatriculaAluno().getMatricula().equals("")) {
                            return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getResponsavelFinanceiro().getNome() + " (ALUNO(A) " + getMatriculaAluno().getAluno().getNome() + " - " + getMatriculaAluno().getMatricula() + ")";
                        }
                        return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getResponsavelFinanceiro().getNome();
                    case CANDIDATO:
                        return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getCandidato().getNome();
                    case FUNCIONARIO:
                        return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getFuncionario().getPessoa().getNome();
                    case REQUERENTE:
                        return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getPessoa().getNome();
                    case PARCEIRO: {
                        if ((getConvenio() != null) && (!getConvenio().getCodigo().equals(0))) {
                            return "PARCEIRO - " + getConvenio().getParceiro().getNome();
                        } else {
                            return "PARCEIRO - " + this.getParceiro().getNome();
                        }
                    }
                    case FORNECEDOR: {
                        if (getFornecedor().getTipoEmpresa().equals("FI")) {
                            return "FORNECEDOR - " + this.getFornecedor().getNome();
                        } else {
                            return "FORNECEDOR - " + this.getFornecedor().getNome();
                        }

                    }
                    default: {
                        return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getPessoa().getNome();
                    }
                }
            } else {
                return getMatriculaAluno().getAluno().getNome();
            }
            //return getTipoPessoa();
        } catch (Exception e) {
            if (getMatriculaAluno().getMatricula() != null && !getMatriculaAluno().getMatricula().equals("")) {
                return TipoPessoa.getDescricao(getTipoPessoa()).toUpperCase() + " - " + getMatriculaAluno().getAluno().getNome() + " - " + getMatriculaAluno().getMatricula();
            }
            return getMatriculaAluno().getAluno().getNome();
        }

    }

    public String getNomePessoa() {
        TipoPessoa tipoPessoaLocal = TipoPessoa.getEnum(getTipoPessoa());
        if (tipoPessoaLocal != null) {
            switch (tipoPessoaLocal) {
                case ALUNO:
                    return getPessoa().getNome();
                case RESPONSAVEL_FINANCEIRO:
                    return getResponsavelFinanceiro().getNome();
                case CANDIDATO:
                    return getCandidato().getNome();
                case FUNCIONARIO:
                    return getFuncionario().getPessoa().getNome();
                case REQUERENTE:
                    return getPessoa().getNome();
                case PARCEIRO:
                    return getParceiro().getNome();
                case FORNECEDOR:
                    return getFornecedor().getNome();
                default:
                    return getPessoa().getNome();

            }
        }
        return "";
    }

    public Boolean getIsPermiteInformarTurma() {
        return getTipoParceiro();
    }

    public String getTipoDesconto_Apresentar() {
        if (getTipoDesconto().equals("VE")) {
            return ("Valor Específico");
        }
        if (getTipoDesconto().equals("PO")) {
            return ("Porcentagem");
        }
        return "";
    }

    public Boolean getIsTipoDescontoPorcentagem() {
        if (this.getTipoDesconto().equals("PO")) {
            return true;
        }
        return false;
    }

    public boolean getIsPossuiValorLancadoRecebimento() {
        if (!getValorCalculadoDescontoLancadoRecebimento().equals(0.0)) {
            return true;
        }
        return false;
    }

    public String getTipoDescLancado() {
        if (getTipoDescontoLancadoRecebimento().equals("PO")) {
            return "(%)";
        } else if (getTipoDescontoLancadoRecebimento().equals("VE")) {
            return "(R$)";
        }
        return "(%)";
    }

    public String getSituacao_Apresentar() {
        return SituacaoContaReceber.getDescricao(situacao);
    }

    public boolean getSituacao_ApresentarIsentos() {
        if (getSituacao().equals("RE") || getSituacao().equals("RM") || getSituacao().equals("NE")) {
            return false;
        }
        return true;
    }

    public Double getJuroPorcentagem() {
        if (juroPorcentagem == null) {
            juroPorcentagem = 0.0;
        }
        return (juroPorcentagem);
    }

    public void setJuroPorcentagem(Double juroPorcentagem) {
        this.juroPorcentagem = juroPorcentagem;
    }

    public Double getJuro() {
        if (juro == null) {
            juro = 0.0;
        }
        return (juro);
    }

    public void setJuro(Double juro) {
        this.juro = juro;
    }

    public Double getMultaPorcentagem() {
        if (multaPorcentagem == null) {
            multaPorcentagem = 0.0;
        }
        return (multaPorcentagem);
    }

    public void setMultaPorcentagem(Double multaPorcentagem) {
        this.multaPorcentagem = multaPorcentagem;
    }

    public Double getMulta() {
        if (multa == null) {
            multa = 0.0;
        }
        return (multa);
    }

    public void setMulta(Double multa) {
        this.multa = multa;
    }
}
