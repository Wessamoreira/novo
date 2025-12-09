package negocio.comuns.sad;

import java.util.Date;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsï¿½vel por manter os dados da entidade DespesaDW. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os mï¿½todos de acesso a estes atributos. Classe utilizada para apresentar
 * e manter em memï¿½ria os dados desta entidade.
 * 
 * @see SuperVO
 */
public class DespesaDWVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private Integer mes;
    private Integer ano;
    private CategoriaDespesaVO categoriaDespesa;
    private String tipoSacado;
    private String origem;
    private String nivelEducacional;
    private Double valor;
    /**
     * Atributo responsï¿½vel por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    private DepartamentoVO departamento;
    private FornecedorVO fornecedor;
    private BancoVO banco;
    private FuncionarioVO funcionario;
    private FuncionarioVO funcionarioCentroCusto;
    private TurmaVO turma;
    /**
     * Atributo responsï¿½vel por manter o objeto relacionado da classe
     * <code>Curso </code>.
     */
    private CursoVO curso;
    /**
     * Atributo responsï¿½vel por manter o objeto relacionado da classe
     * <code>Turno </code>.
     */
    private TurnoVO turno;
    /**
     * Atributo responsï¿½vel por manter o objeto relacionado da classe
     * <code>AreaConhecimento </code>.
     */
    private AreaConhecimentoVO areaConhecimento;
    public static final long serialVersionUID = 1L;
    // private TimeSeriesCollection linhaTempo;
    // private DefaultCategoryDataset barra;
    // private Boolean apresentarGraficoBarra;
    // private Boolean apresentarGraficoLinhaTempo;
    //
    // private DefaultPieDataset pizzaPorUnidade;
    // private DefaultPieDataset pizzaPorDepartamento;
    // private DefaultPieDataset pizzaPorCategoriaDespesa;
    //
    // private boolean nivelUnidade;

    /**
     * Construtor padrï¿½o da classe <code>DespesaDW</code>. Cria uma nova
     * instï¿½ncia desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public DespesaDWVO(String origem) {
        super();
        inicializarDados();
        this.origem = origem;
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por validar os dados de um objeto da classe
     * <code>DespesaDWVO</code>. Todos os tipos de consistï¿½ncia de dados sï¿½o
     * e devem ser implementadas neste mï¿½todo. Sï¿½o validaï¿½ï¿½es tï¿½picas:
     * verificaï¿½ï¿½o de campos obrigatï¿½rios, verificaï¿½ï¿½o de valores
     * vï¿½lidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistï¿½ncia for encontrada aumaticamente ï¿½
     *                gerada uma exceï¿½ï¿½o descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(DespesaDWVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Despesa DW) deve ser informado.");
        }
        if (obj.getMes().intValue() == 0) {
            throw new ConsistirException("O campo MÊS (Despesa DW) deve ser informado.");
        }

        if (obj.getAno().intValue() == 0) {
            throw new ConsistirException("O campo ANO (Despesa DW) deve ser informado.");
        }
        if (obj.getCategoriaDespesa().getCodigo().intValue() == 0) {
            throw new ConsistirException("O campo CATEGORIA DE DESPESA (Despesa DW) deve ser informado.");
        }

    }

    /**
     * Operaï¿½ï¿½o reponsï¿½vel por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(new Integer(0));
        setData(new Date());
        // setDataFim(new Date());
        setMes(new Integer(0));
        setAno(new Integer(0));
        setValor(0.0);
        setNivelEducacional("");
        setTipoSacado("");
        setOrigem("");
        // setNivelUnidade(true);
    }

    //
    // public TimeSeries consultarSerieLinhaTempo(String unidadeEnsino) {
    // int x = getLinhaTempo().getSeriesCount();
    // int index = 1;
    // while (index <= x) {
    // TimeSeries time = getLinhaTempo().getSeries(index - 1);
    // if (time.getKey().equals(unidadeEnsino)) {
    // return time;
    // }
    // index++;
    // }
    // return new TimeSeries(unidadeEnsino, Day.class);
    // }
    //
    // public void adicionarSerieLinhaTempo(TimeSeries obj) {
    // int x = getLinhaTempo().getSeriesCount();
    // int index = 1;
    // while (index <= x) {
    // TimeSeries time = getLinhaTempo().getSeries(index - 1);
    // if (time.getKey().equals(obj.getKey())) {
    //
    // return;
    // }
    // index++;
    // }
    // getLinhaTempo().addSeries(obj);
    // }
    //
    // public void criarGraficoLinhaTempo(ResultSet dadosSql) throws Exception {
    // setLinhaTempo(new TimeSeriesCollection());
    // while (dadosSql.next()) {
    //
    // Date data = dadosSql.getDate("data");
    //
    // Double valor = new Double(dadosSql.getInt("valor"));
    // String unidadeEnsino = dadosSql.getString("unidadeEnsino");
    // if (unidadeEnsino == null) {
    // unidadeEnsino = "Holding";
    // }
    // TimeSeries ts = consultarSerieLinhaTempo(unidadeEnsino);
    // ts.add(new Day(data), valor);
    // adicionarSerieLinhaTempo(ts);
    // }
    //
    // setApresentarGraficoLinhaTempo(Boolean.TRUE);
    // setApresentarGraficoBarra(Boolean.FALSE);
    // }
    // public void criarGraficoBarra(ResultSet dadosSql) throws Exception {
    // Double valorTotal = 0.0;
    // setBarra(new DefaultCategoryDataset());
    // while (dadosSql.next()) {
    // Double valor = new Double(dadosSql.getInt("valor"));
    // String unidadeEnsino = dadosSql.getString("unidadeEnsino");
    // if (unidadeEnsino == null) {
    // unidadeEnsino = "Holding";
    // }
    // getBarra().addValue(valor, unidadeEnsino, unidadeEnsino);
    // valorTotal = valorTotal + valor;
    // }
    // if (valorTotal.doubleValue() > 0 &&
    // getUnidadeEnsino().getCodigo().intValue() ==0) {
    // getBarra().addValue(valorTotal, "TODAS", "TODAS");
    // }
    // setApresentarGraficoLinhaTempo(Boolean.FALSE);
    // setApresentarGraficoBarra(Boolean.TRUE);
    // }
    public FornecedorVO getFornecedor() {
        if (fornecedor == null) {
            fornecedor = new FornecedorVO();
        }
        return fornecedor;
    }

    public void setFornecedor(FornecedorVO fornecedor) {
        this.fornecedor = fornecedor;
    }

    /**
     * Retorna o objeto da classe <code>AreaConhecimento</code> relacionado com
     * (<code>DespesaDW</code>).
     */
    public AreaConhecimentoVO getAreaConhecimento() {
        if (areaConhecimento == null) {
            areaConhecimento = new AreaConhecimentoVO();
        }
        return (areaConhecimento);
    }

    /**
     * Define o objeto da classe <code>AreaConhecimento</code> relacionado com (
     * <code>DespesaDW</code>).
     */
    public void setAreaConhecimento(AreaConhecimentoVO obj) {
        this.areaConhecimento = obj;
    }

    /**
     * Retorna o objeto da classe <code>Turno</code> relacionado com (
     * <code>DespesaDW</code>).
     */
    public TurnoVO getTurno() {
        if (turno == null) {
            turno = new TurnoVO();
        }
        return (turno);
    }

    /**
     * Define o objeto da classe <code>Turno</code> relacionado com (
     * <code>DespesaDW</code>).
     */
    public void setTurno(TurnoVO obj) {
        this.turno = obj;
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (
     * <code>DespesaDW</code>).
     */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }

    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (
     * <code>DespesaDW</code>).
     */
    public void setCurso(CursoVO obj) {
        this.curso = obj;
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>DespesaDW</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>DespesaDW</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    public String getNivelEducacional() {
        if (nivelEducacional == null) {
            nivelEducacional = "";
        }
        return (nivelEducacional);
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por retornar o valor de apresentaï¿½ï¿½o de um
     * atributo com um domï¿½nio especï¿½fico. Com base no valor de
     * armazenamento do atributo esta funï¿½ï¿½o ï¿½ capaz de retornar o de
     * apresentaï¿½ï¿½o correspondente. ï¿½til para campos como sexo,
     * escolaridade, etc.
     */
    public String getNivelEducacional_Apresentar() {
        if (nivelEducacional == null) {
            nivelEducacional = "";
        }
        if (nivelEducacional.equals("BA")) {
            return "Ensino Básico";
        }
        if (nivelEducacional.equals("PO")) {
            return "Pós-graduaçao";
        }
        if (nivelEducacional.equals("SU")) {
            return "Ensino Superior";
        }
        if (nivelEducacional.equals("ME")) {
            return "Ensino Médio";
        }
        return (nivelEducacional);
    }

    public void setNivelEducacional(String nivelEducacional) {
        this.nivelEducacional = nivelEducacional;
    }

    public CategoriaDespesaVO getCategoriaDespesa() {
        if (categoriaDespesa == null) {
            categoriaDespesa = new CategoriaDespesaVO();
        }
        return categoriaDespesa;
    }

    public void setCategoriaDespesa(CategoriaDespesaVO categoriaDespesa) {
        this.categoriaDespesa = categoriaDespesa;
    }

    public Integer getAno() {
        return (ano);
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getMes() {
        return (mes);
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public Date getData() {
        return (data);
    }

    /**
     * Operaï¿½ï¿½o responsï¿½vel por retornar um atributo do tipo data no
     * formato padrï¿½o dd/mm/aaaa.
     */
    public String getData_Apresentar() {
        return (Uteis.getData(data));
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Integer getCodigo() {
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    //
    // public Date getDataFim() {
    // return dataFim;
    // }
    //
    // public void setDataFim(Date dataFim) {
    // this.dataFim = dataFim;
    // }
    // public Boolean getApresentarGraficoBarra() {
    // return apresentarGraficoBarra;
    // }
    //
    // public void setApresentarGraficoBarra(Boolean apresentarGraficoBarra) {
    // this.apresentarGraficoBarra = apresentarGraficoBarra;
    // }
    //
    // public Boolean getApresentarGraficoLinhaTempo() {
    // return apresentarGraficoLinhaTempo;
    // }
    //
    // public void setApresentarGraficoLinhaTempo(Boolean
    // apresentarGraficoLinhaTempo) {
    // this.apresentarGraficoLinhaTempo = apresentarGraficoLinhaTempo;
    // }
    //
    // public DefaultCategoryDataset getBarra() {
    // return barra;
    // }
    //
    // public void setBarra(DefaultCategoryDataset barra) {
    // this.barra = barra;
    // }
    //
    // public TimeSeriesCollection getLinhaTempo() {
    // return linhaTempo;
    // }
    //
    // public void setLinhaTempo(TimeSeriesCollection linhaTempo) {
    // this.linhaTempo = linhaTempo;
    // }
    public BancoVO getBanco() {
        if (banco == null) {
            banco = new BancoVO();
        }
        return banco;
    }

    public void setBanco(BancoVO banco) {
        this.banco = banco;
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

    public String getTipoSacado() {
        return tipoSacado;
    }

    public void setTipoSacado(String tipoSacado) {
        this.tipoSacado = tipoSacado;
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

    public FuncionarioVO getFuncionarioCentroCusto() {
        if (funcionarioCentroCusto == null) {
            funcionarioCentroCusto = new FuncionarioVO();
        }
        return funcionarioCentroCusto;
    }

    public void setFuncionarioCentroCusto(FuncionarioVO funcionarioCentroCusto) {
        this.funcionarioCentroCusto = funcionarioCentroCusto;
    }

    public DepartamentoVO getDepartamento() {
        if (departamento == null) {
            departamento = new DepartamentoVO();
        }
        return departamento;
    }

    public void setDepartamento(DepartamentoVO departamento) {
        this.departamento = departamento;
    }

    public String getOrigem() {
        if (origem == null) {
            origem = "";
        }
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }
    //
    // public DefaultPieDataset getPizzaPorCategoriaDespesa() {
    // return pizzaPorCategoriaDespesa;
    // }
    //
    // public void setPizzaPorCategoriaDespesa(DefaultPieDataset
    // pizzaPorCategoriaDespesa) {
    // this.pizzaPorCategoriaDespesa = pizzaPorCategoriaDespesa;
    // }
    //
    // public DefaultPieDataset getPizzaPorDepartamento() {
    // return pizzaPorDepartamento;
    // }
    //
    // public void setPizzaPorDepartamento(DefaultPieDataset
    // pizzaPorDepartamento) {
    // this.pizzaPorDepartamento = pizzaPorDepartamento;
    // }
    //
    // public DefaultPieDataset getPizzaPorUnidade() {
    // return pizzaPorUnidade;
    // }
    //
    // public void setPizzaPorUnidade(DefaultPieDataset pizzaPorUnidade) {
    // this.pizzaPorUnidade = pizzaPorUnidade;
    // }
    //
    //
    // public boolean isNivelUnidade() {
    // return nivelUnidade;
    // }
    //
    // public void setNivelUnidade(boolean nivelUnidade) {
    // this.nivelUnidade = nivelUnidade;
    // }
}
