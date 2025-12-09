package negocio.comuns.sad;

import java.util.Date;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.ProcessoMatriculaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.pesquisa.AreaConhecimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 * Reponsável por manter os dados da entidade MatriculaDW. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class MatriculaDWVO extends SuperVO {

    private Integer codigo;
    private Date data;
    private Date dataFim;
    private Integer mes;
    private ProcessoMatriculaVO processoMatricula;
    private String situacao;
    private Integer peso;
    private Integer ano;
    private String nivelEducacional;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>UnidadeEnsino </code>.
     */
    private UnidadeEnsinoVO unidadeEnsino;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Curso </code>.
     */
    private CursoVO curso;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>Turno </code>.
     */
    private TurnoVO turno;
    /**
     * Atributo responsável por manter o objeto relacionado da classe
     * <code>AreaConhecimento </code>.
     */
    private AreaConhecimentoVO areaConhecimento;
    private TimeSeriesCollection linhaTempo;
    private DefaultCategoryDataset barra;
    private XYSeriesCollection conjunto;
    private DefaultPieDataset pizza;
    private Boolean apresentarGraficoPizza;
    private Boolean apresentarGraficoBarra;
    private Boolean apresentarGraficoLinhaTempo;
    private Boolean apresentarGraficoConjunto;
    public static final long serialVersionUID = 1L;

    /**
     * Construtor padrão da classe <code>MatriculaDW</code>. Cria uma nova
     * instância desta entidade, inicializando automaticamente seus atributos
     * (Classe VO).
     */
    public MatriculaDWVO() {
        super();
        inicializarDados();
    }

    public MatriculaDWVO(SqlRowSet dadosSql) {
        super();
        inicializarDados();
        criarGraficoPizza(dadosSql);
    }

    /**
     * Operação responsável por validar os dados de um objeto da classe
     * <code>MatriculaDWVO</code>. Todos os tipos de consistência de dados são e
     * devem ser implementadas neste método. São validações típicas: verificação
     * de campos obrigatórios, verificação de valores válidos para os atributos.
     *
     * @exception ConsistirExecption
     *                Se uma inconsistência for encontrada aumaticamente é
     *                gerada uma exceção descrevendo o atributo e o erro
     *                ocorrido.
     */
    public static void validarDados(MatriculaDWVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Matricula DW) deve ser informado.");
        }
        if (obj.getMes().intValue() == 0) {
            throw new ConsistirException("O campo MÊS (Matricula DW) deve ser informado.");
        }

        if (obj.getAno().intValue() == 0) {
            throw new ConsistirException("O campo ANO (Matricula DW) deve ser informado.");
        }
        if ((obj.getUnidadeEnsino() == null) || (obj.getUnidadeEnsino().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo UNIDADE DE ENSINO (Matricula DW) deve ser informado.");
        }

        if ((obj.getCurso() == null) || (obj.getCurso().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo CURSO (Matricula DW) deve ser informado.");
        }
        if ((obj.getTurno() == null) || (obj.getTurno().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo TURNO (Matricula DW) deve ser informado.");
        }
        if ((obj.getAreaConhecimento() == null) || (obj.getAreaConhecimento().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo ÁREA DE CONHECIMENTO (Matricula DW) deve ser informado.");
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
        setPeso(new Integer(1));
        setSituacao("AT");
        setAno(new Integer(0));
        setNivelEducacional("");
        setLinhaTempo(new TimeSeriesCollection());
        setPizza(new DefaultPieDataset());
        setBarra(new DefaultCategoryDataset());
        setApresentarGraficoBarra(Boolean.FALSE);
        setApresentarGraficoConjunto(Boolean.FALSE);
        setApresentarGraficoLinhaTempo(Boolean.FALSE);
        setApresentarGraficoPizza(Boolean.TRUE);

    }

    public void criarGraficoLinhaTempo(SqlRowSet dadosSql) throws Exception {
        setLinhaTempo(new TimeSeriesCollection());
        TimeSeries s1 = new TimeSeries("Ativo", Day.class);
        TimeSeries s2 = new TimeSeries("Trancado", Day.class);
        TimeSeries s3 = new TimeSeries("Cancelado", Day.class);
        while (dadosSql.next()) {

            Integer peso = new Integer(dadosSql.getInt("peso"));
            Date data = dadosSql.getDate("data");
            setSituacao(dadosSql.getString("situacao"));

            if (getSituacao().equals("AT")) {
                s1.add(new Day(data), peso);
            }
            if (getSituacao().equals("TR")) {
                s2.add(new Day(data), peso);
            }
            if (getSituacao().equals("CA")) {
                s3.add(new Day(data), peso);
            }

        }
        getLinhaTempo().addSeries(s1);
        getLinhaTempo().addSeries(s2);
        getLinhaTempo().addSeries(s3);

        setApresentarGraficoLinhaTempo(Boolean.TRUE);
        setApresentarGraficoBarra(Boolean.FALSE);
        setApresentarGraficoConjunto(Boolean.FALSE);
        setApresentarGraficoPizza(Boolean.FALSE);
    }

    public void criarGraficoPizza(SqlRowSet dadosSql) {

        try {
            setPizza(new DefaultPieDataset());
            while (dadosSql.next()) {
                String situacao = dadosSql.getString("situacao");
                Integer peso = new Integer(dadosSql.getInt("peso"));
                if (situacao.equals("AT")) {
                    pizza.setValue("Ativo", peso);
                }
                if (situacao.equals("TR")) {
                    pizza.setValue("Trancado", peso);
                }
                if (situacao.equals("CA")) {
                    pizza.setValue("Cancelado", peso);
                }
            }
            setApresentarGraficoLinhaTempo(Boolean.FALSE);
            setApresentarGraficoBarra(Boolean.FALSE);
            setApresentarGraficoConjunto(Boolean.FALSE);
            setApresentarGraficoPizza(Boolean.TRUE);
        } catch (Exception se) {
            throw new RuntimeException(se);
        }
    }

    public void criarGraficoBarra(SqlRowSet dadosSql) throws Exception {
        setBarra(new DefaultCategoryDataset());
        while (dadosSql.next()) {
            Integer peso = new Integer(dadosSql.getInt("peso"));
            setSituacao(dadosSql.getString("situacao"));
            getBarra().addValue(peso, getSituacao_Apresentar(), getSituacao_Apresentar());
        }
        setApresentarGraficoLinhaTempo(Boolean.FALSE);
        setApresentarGraficoBarra(Boolean.TRUE);
        setApresentarGraficoConjunto(Boolean.FALSE);
        setApresentarGraficoPizza(Boolean.FALSE);
    }

    public void criarGraficoConjunto() {
        // conjunto = new XYSeriesCollection();
        //
        // XYSeries series = new XYSeries("Verbas");
        // series.setDescription("Despesas com verbas - Folha Inativo");
        // series.add(1, 8);
        // series.add(2, 7);
        // series.add(3, 6);
        // series.add(4, 5);
        // series.add(5, 4);
        // series.add(6, 3);
        // series.add(7, 7);
        // series.add(8, 8);
        //
        // XYSeries series2 = new XYSeries("Verbas");
        // series2.setDescription("Despesas com verbas - Folha Ativo");
        // series2.add(1, 2);
        // series2.add(2, 3);
        // series2.add(3, 8);
        // series2.add(4, 5);
        // series2.add(5, 2);
        // series2.add(6, 3);
        // series2.add(7, 5);
        // series2.add(8, 6);
        //
        // conjunto.addSeries(series);
        // conjunto.addSeries(series2);
    }

    /**
     * Retorna o objeto da classe <code>AreaConhecimento</code> relacionado com
     * (<code>MatriculaDW</code>).
     */
    public AreaConhecimentoVO getAreaConhecimento() {
        if (areaConhecimento == null) {
            areaConhecimento = new AreaConhecimentoVO();
        }
        return (areaConhecimento);
    }

    /**
     * Define o objeto da classe <code>AreaConhecimento</code> relacionado com (
     * <code>MatriculaDW</code>).
     */
    public void setAreaConhecimento(AreaConhecimentoVO obj) {
        this.areaConhecimento = obj;
    }

    /**
     * Retorna o objeto da classe <code>Turno</code> relacionado com (
     * <code>MatriculaDW</code>).
     */
    public TurnoVO getTurno() {
        if (turno == null) {
            turno = new TurnoVO();
        }
        return (turno);
    }

    /**
     * Define o objeto da classe <code>Turno</code> relacionado com (
     * <code>MatriculaDW</code>).
     */
    public void setTurno(TurnoVO obj) {
        this.turno = obj;
    }

    /**
     * Retorna o objeto da classe <code>Curso</code> relacionado com (
     * <code>MatriculaDW</code>).
     */
    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return (curso);
    }

    /**
     * Define o objeto da classe <code>Curso</code> relacionado com (
     * <code>MatriculaDW</code>).
     */
    public void setCurso(CursoVO obj) {
        this.curso = obj;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
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

    public String getSituacao_Apresentar() {
        if (getSituacao() == null) {
            situacao = "";
        }
        if (situacao.equals("AT")) {
            return "Ativa";
        }
        if (situacao.equals("TR")) {
            return "Trancada";
        }
        if (situacao.equals("CA")) {
            return "Cancelada";
        }

        return "";
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

    /**
     * Retorna o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>MatriculaDW</code>).
     */
    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return (unidadeEnsino);
    }

    /**
     * Define o objeto da classe <code>UnidadeEnsino</code> relacionado com (
     * <code>MatriculaDW</code>).
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
     * Operação responsável por retornar o valor de apresentação de um atributo
     * com um domínio específico. Com base no valor de armazenamento do atributo
     * esta função é capaz de retornar o de apresentação correspondente. Útil
     * para campos como sexo, escolaridade, etc.
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

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
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
        return (codigo);
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public XYSeriesCollection getConjunto() {
        return conjunto;
    }

    public void setConjunto(XYSeriesCollection conjunto) {
        this.conjunto = conjunto;
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

    public DefaultPieDataset getPizza() {
        return pizza;
    }

    public void setPizza(DefaultPieDataset pizza) {
        this.pizza = pizza;
    }

    public Boolean getApresentarGraficoBarra() {
        return apresentarGraficoBarra;
    }

    public void setApresentarGraficoBarra(Boolean apresentarGraficoBarra) {
        this.apresentarGraficoBarra = apresentarGraficoBarra;
    }

    public Boolean getApresentarGraficoConjunto() {
        return apresentarGraficoConjunto;
    }

    public void setApresentarGraficoConjunto(Boolean apresentarGraficoConjunto) {
        this.apresentarGraficoConjunto = apresentarGraficoConjunto;
    }

    public Boolean getApresentarGraficoLinhaTempo() {
        return apresentarGraficoLinhaTempo;
    }

    public void setApresentarGraficoLinhaTempo(Boolean apresentarGraficoLinhaTempo) {
        this.apresentarGraficoLinhaTempo = apresentarGraficoLinhaTempo;
    }

    public Boolean getApresentarGraficoPizza() {
        return apresentarGraficoPizza;
    }

    public void setApresentarGraficoPizza(Boolean apresentarGraficoPizza) {
        this.apresentarGraficoPizza = apresentarGraficoPizza;
    }
}
