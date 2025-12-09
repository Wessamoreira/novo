package negocio.comuns.sad;

import java.util.Date;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade ReceitaDW. Classe do tipo VO - Value Object 
 * composta pelos atributos da entidade com visibilidade protegida e os métodos de acesso a estes atributos.
 * Classe utilizada para apresentar e manter em memória os dados desta entidade.
 * @see SuperVO
 */
public class ReceitaDWVO extends SuperVO {

    protected Integer codigo;
    protected Date data;
    protected Date dataFim;
    protected Integer mes;
    protected Integer ano;
    protected Double valor;
    protected String nivelEducacional;
    /** Atributo responsável por manter o objeto relacionado da classe <code>CentroReceita </code>.*/
    protected CentroReceitaVO centroReceita;
    /** Atributo responsável por manter o objeto relacionado da classe <code>UnidadeEnsino </code>.*/
    protected UnidadeEnsinoVO unidadeEnsino;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Curso </code>.*/
    protected CursoVO curso;
    /** Atributo responsável por manter o objeto relacionado da classe <code>Turno </code>.*/
    protected TurnoVO turno;
    /** Atributo responsável por manter o objeto relacionado da classe <code>PeriodoLetivo </code>.*/
    protected ProcessoMatriculaVO processoMatricula;
    protected ProcSeletivoVO procSeletivo;
    /** Atributo responsável por manter o objeto relacionado da classe <code>AreaConhecimento </code>.*/
    protected AreaConhecimentoVO areaConhecimento;
    protected TimeSeriesCollection linhaTempo;
    protected DefaultCategoryDataset barra;
    protected Boolean apresentarGraficoBarra;
    protected Boolean apresentarGraficoLinhaTempo;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>ReceitaDW</code>.
     * Cria uma nova instância desta entidade, inicializando automaticamente seus atributos (Classe VO).
     */
    public ReceitaDWVO() {
        super();
        inicializarDados();
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe <code>ReceitaDWVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     *                               o atributo e o erro ocorrido.
     */
    public static void validarDados(ReceitaDWVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }

    }

    /**
     * Operação reponsável por inicializar os atributos da classe.
     */
    public void inicializarDados() {
        setCodigo(new Integer(0));
        setData(Uteis.obterDataFutura(new Date(), -180));
        setDataFim(new Date());
        setMes(new Integer(0));
        setProcSeletivo(new ProcSeletivoVO());
        setProcessoMatricula(new ProcessoMatriculaVO());
        setAno(new Integer(0));
        setValor(0.0);
        setNivelEducacional("");
        setCentroReceita(new CentroReceitaVO());
        setUnidadeEnsino(new UnidadeEnsinoVO());
        setCurso(new CursoVO());
        setTurno(new TurnoVO());
        setAreaConhecimento(new AreaConhecimentoVO());
    }

    public TimeSeries consultarSerieLinhaTempo(String unidadeEnsino) {
        int x = getLinhaTempo().getSeriesCount();
        int index = 1;
        while (index <= x) {
            TimeSeries time = getLinhaTempo().getSeries(index - 1);
            if (time.getKey().equals(unidadeEnsino)) {
                return time;
            }
            index++;
        }
        return new TimeSeries(unidadeEnsino, Day.class);
    }

    public void adicionarSerieLinhaTempo(TimeSeries obj) {
        int x = getLinhaTempo().getSeriesCount();
        int index = 1;
        while (index <= x) {
            TimeSeries time = getLinhaTempo().getSeries(index - 1);
            if (time.getKey().equals(obj.getKey())) {

                return;
            }
            index++;
        }
        getLinhaTempo().addSeries(obj);
    }

    public void criarGraficoLinhaTempo(SqlRowSet dadosSql) throws Exception {
        setLinhaTempo(new TimeSeriesCollection());
        while (dadosSql.next()) {

            Date data = dadosSql.getDate("data");

            Double valor = new Double(dadosSql.getInt("valor"));
            String unidadeEnsino = dadosSql.getString("unidadeEnsino");
            if (unidadeEnsino == null) {
                unidadeEnsino = "Holding";
            }
            TimeSeries ts = consultarSerieLinhaTempo(unidadeEnsino);
            ts.add(new Day(data), valor);
            adicionarSerieLinhaTempo(ts);
        }

        setApresentarGraficoLinhaTempo(Boolean.TRUE);
        setApresentarGraficoBarra(Boolean.FALSE);
    }

//    public void criarGraficoPizza(ResultSet dadosSql) {
//
//        try {
//            setPizza(new DefaultPieDataset());
//            while (dadosSql.next()) {
//                String situacao = dadosSql.getString("situacao");
//                Integer peso = new Integer(dadosSql.getInt("peso"));
//                if (situacao.equals("AT")) {
//                    pizza.setValue("Ativo", peso);
//                }
//                if (situacao.equals("TR")) {
//                    pizza.setValue("Trancado", peso);
//                }
//                if (situacao.equals("CA")) {
//                    pizza.setValue("Cancelado", peso);
//                }
//            }
//            setApresentarGraficoLinhaTempo(Boolean.FALSE);
//            setApresentarGraficoBarra(Boolean.FALSE);
//            setApresentarGraficoConjunto(Boolean.FALSE);
//            setApresentarGraficoPizza(Boolean.TRUE);
//        } catch (Exception se) {
//            throw new RuntimeException(se);
//        }
//    }
    public void criarGraficoBarra(SqlRowSet dadosSql) throws Exception {
        Double valorTotal = 0.0;
        setBarra(new DefaultCategoryDataset());
        while (dadosSql.next()) {
            Double valor = new Double(dadosSql.getInt("valor"));
            String unidadeEnsino = dadosSql.getString("unidadeEnsino");
            if (unidadeEnsino == null) {
                unidadeEnsino = "Holding";
            }
            getBarra().addValue(valor, unidadeEnsino, unidadeEnsino);
            valorTotal = valorTotal + valor;
        }
        if (valorTotal.doubleValue() > 0 && getUnidadeEnsino().getCodigo().intValue() == 0) {
            getBarra().addValue(valorTotal, "TODAS", "TODAS");
        }
        setApresentarGraficoLinhaTempo(Boolean.FALSE);
        setApresentarGraficoBarra(Boolean.TRUE);
    }

    public void criarGraficoConjunto() {
//        conjunto = new XYSeriesCollection();
//
//        XYSeries series = new XYSeries("Verbas");
//        series.setDescription("Despesas com verbas - Folha Inativo");
//        series.add(1, 8);
//        series.add(2, 7);
//        series.add(3, 6);
//        series.add(4, 5);
//        series.add(5, 4);
//        series.add(6, 3);
//        series.add(7, 7);
//        series.add(8, 8);
//
//        XYSeries series2 = new XYSeries("Verbas");
//        series2.setDescription("Despesas com verbas - Folha Ativo");
//        series2.add(1, 2);
//        series2.add(2, 3);
//        series2.add(3, 8);
//        series2.add(4, 5);
//        series2.add(5, 2);
//        series2.add(6, 3);
//        series2.add(7, 5);
//        series2.add(8, 6);
//
//        conjunto.addSeries(series);
//        conjunto.addSeries(series2);
    }

    /**
     * Retorna o objeto da classe <code>AreaConhecimento</code> relacionado com (<code>ReceitaDW</code>).
     */
    public AreaConhecimentoVO getAreaConhecimento() {
        if (areaConhecimento == null) {
            areaConhecimento = new AreaConhecimentoVO();
        }
        return (areaConhecimento);
    }

    /**
     * Define o objeto da classe <code>AreaConhecimento</code> relacionado com (<code>ReceitaDW</code>).
     */
    public void setAreaConhecimento(AreaConhecimentoVO obj) {
        this.areaConhecimento = obj;
    }

    /**
     * Retorna o objeto da classe <code>Turno</code> relacionado com (<code>ReceitaDW</code>).
     */
    public TurnoVO getTurno() {
        if (turno == null) {
            turno = new TurnoVO();
        }
        return (turno);
    }

    /**
     * Define o objeto da classe <code>Turno</code> relacionado com (<code>ReceitaDW</code>).
     */
    public void setTurno(TurnoVO obj) {
        this.turno = obj;
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (<code>ReceitaDW</code>).
     */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }

    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (<code>ReceitaDW</code>).
     */
    public void setCurso(CursoVO obj) {
        this.curso = obj;
    }

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (<code>ReceitaDW</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (<code>ReceitaDW</code>).
     */
    public void setUnidadeEnsino(UnidadeEnsinoVO obj) {
        this.unidadeEnsino = obj;
    }

    /**
     * Retorna o objeto da classe <code>CentroReceita</code> relacionado com (<code>ReceitaDW</code>).
     */
    public CentroReceitaVO getCentroReceita() {
        if (centroReceita == null) {
            centroReceita = new CentroReceitaVO();
        }
        return (centroReceita);
    }

    /**
     * Define o objeto da classe <code>CentroReceita</code> relacionado com (<code>ReceitaDW</code>).
     */
    public void setCentroReceita(CentroReceitaVO obj) {
        this.centroReceita = obj;
    }

    public String getNivelEducacional() {
        if (nivelEducacional == null) {
            nivelEducacional = "";
        }
        return (nivelEducacional);
    }

    /**
     * Operação responsável por retornar o valor de apresentação de um atributo com um domínio específico. 
     * Com base no valor de armazenamento do atributo esta função é capaz de retornar o 
     * de apresentação correspondente. Útil para campos como sexo, escolaridade, etc. 
     */
    public String getNivelEducacional_Apresentar() {
        if (nivelEducacional == null) {
            nivelEducacional = "";
        }
        if (nivelEducacional.equals("BA")) {
            return "Ensino Básico";
        }
        if (nivelEducacional.equals("PO")) {
            return "Pós-graduação";
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

    public Double getValor() {
        return (valor);
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Integer getAno() {
        return (ano);
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public ProcSeletivoVO getProcSeletivo() {
        if (procSeletivo == null) {
            procSeletivo = new ProcSeletivoVO();
        }
        return procSeletivo;
    }

    public void setProcSeletivo(ProcSeletivoVO procSeletivo) {
        this.procSeletivo = procSeletivo;
    }

    public ProcessoMatriculaVO getProcessoMatricula() {
        if (processoMatricula == null) {
            processoMatricula = new ProcessoMatriculaVO();
        }
        return processoMatricula;
    }

    public void setProcessoMatricula(ProcessoMatriculaVO processoMatricula) {
        this.processoMatricula = processoMatricula;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
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
     * Operação responsável por retornar um atributo do tipo data no formato padrão dd/mm/aaaa. 
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

    public DefaultCategoryDataset getBarra() {
        return barra;
    }

    public void setBarra(DefaultCategoryDataset barra) {
        this.barra = barra;
    }

    public TimeSeriesCollection getLinhaTempo() {
        return linhaTempo;
    }

    public void setLinhaTempo(TimeSeriesCollection linhaTempo) {
        this.linhaTempo = linhaTempo;
    }

    public Boolean getApresentarGraficoBarra() {
        return apresentarGraficoBarra;
    }

    public void setApresentarGraficoBarra(Boolean apresentarGraficoBarra) {
        this.apresentarGraficoBarra = apresentarGraficoBarra;
    }

    public Boolean getApresentarGraficoLinhaTempo() {
        return apresentarGraficoLinhaTempo;
    }

    public void setApresentarGraficoLinhaTempo(Boolean apresentarGraficoLinhaTempo) {
        this.apresentarGraficoLinhaTempo = apresentarGraficoLinhaTempo;
    }
}
