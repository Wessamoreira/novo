package relatorio.controle.sad;

import java.util.List;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultKeyedValueDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;

@SuppressWarnings("unchecked")
@Controller("GraficosGerenciaisControle")
@Scope("request")
@Lazy
public class GraficosGerenciaisControle extends SuperControleRelatorio {

	private List listaSelectItemFolhaPagamento;
	private List listaSelectItemSituacaoFuncional;
	private List situacaoFuncionalProcessar;
	private List listaSelectItemTipoBeneficio;

	/** Creates a new instance of GraficosGerenciaisControle */
	public GraficosGerenciaisControle() {
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void inicializarListasSelectItemTodosComboBox() {
	}

	public List getListaSelectItemFolhaPagamento() {
		return listaSelectItemFolhaPagamento;
	}

	public void setListaSelectItemFolhaPagamento(List listaSelectItemFolhaPagamento) {
		this.listaSelectItemFolhaPagamento = listaSelectItemFolhaPagamento;
	}

	public List getListaSelectItemSituacaoFuncional() {
		return listaSelectItemSituacaoFuncional;
	}

	public void setListaSelectItemSituacaoFuncional(List listaSelectItemSituacaoFuncional) {
		this.listaSelectItemSituacaoFuncional = listaSelectItemSituacaoFuncional;
	}

	public List getSituacaoFuncionalProcessar() {
		return situacaoFuncionalProcessar;
	}

	public void setSituacaoFuncionalProcessar(List situacaoFuncionalProcessar) {
		this.situacaoFuncionalProcessar = situacaoFuncionalProcessar;
	}

	public void setListaSelectItemTipoBeneficio(List listaSelectItemTipoBeneficio) {
		this.listaSelectItemTipoBeneficio = listaSelectItemTipoBeneficio;
	}

	public XYDataset getDadosXYDataset() {
		TimeSeriesCollection timeSeriesDataSet = new TimeSeriesCollection();
		TimeSeries s1 = new TimeSeries("Unidade Goiânia", Month.class);
		s1.add(new Month(1, 2002), 414200.9);
		s1.add(new Month(2, 2002), 401380.7);
		s1.add(new Month(3, 2002), 413700.3);
		s1.add(new Month(4, 2002), 514030.9);
		s1.add(new Month(5, 2002), 610039.8);
		s1.add(new Month(6, 2002), 613007.0);
		s1.add(new Month(7, 2002), 673002.8);

		TimeSeries s2 = new TimeSeries("Unidade Salvador", Month.class);
		s2.add(new Month(1, 2002), 61101.7);
		s2.add(new Month(2, 2002), 61101.0);
		s2.add(new Month(3, 2002), 71009.6);
		s2.add(new Month(4, 2002), 71103.2);
		s2.add(new Month(5, 2002), 51011.6);
		s2.add(new Month(6, 2002), 51008.8);
		s2.add(new Month(7, 2002), 71010.6);

		TimeSeries s3 = new TimeSeries("Unidade Niterói", Month.class);
		s3.add(new Month(1, 2002), 21101.7);
		s3.add(new Month(2, 2002), 31101.0);
		s3.add(new Month(3, 2002), 10009.6);
		s3.add(new Month(4, 2002), 11103.2);
		s3.add(new Month(5, 2002), 51011.6);
		s3.add(new Month(6, 2002), 21008.8);
		s3.add(new Month(7, 2002), 41010.6);

		timeSeriesDataSet.addSeries(s1);
		timeSeriesDataSet.addSeries(s2);
		timeSeriesDataSet.addSeries(s3);
		return timeSeriesDataSet;
	}

	public DefaultCategoryDataset getCategoryDataset() {
		DefaultCategoryDataset categoryDataSet;

		// row keys...
		String series1 = "Direito";
		String series2 = "Enfermagem";
		String series3 = "Computação";
		// column keys...
		String category1 = "Unidade Goiânia";
		String category2 = "Unidade Salvador";
		String category3 = "Unidade Niterói";
		String category4 = "Todas Unidades";

		// create the dataset...
		categoryDataSet = new DefaultCategoryDataset();
		categoryDataSet.addValue(55, series1, category1);
		categoryDataSet.addValue(15, series1, category2);
		categoryDataSet.addValue(30, series1, category3);
		categoryDataSet.addValue(40, series1, category4);
		categoryDataSet.addValue(40, series2, category1);
		categoryDataSet.addValue(20, series2, category2);
		categoryDataSet.addValue(30, series2, category3);
		categoryDataSet.addValue(30, series2, category4);
		categoryDataSet.addValue(40, series3, category1);
		categoryDataSet.addValue(40, series3, category2);
		categoryDataSet.addValue(30, series3, category3);
		categoryDataSet.addValue(30, series3, category4);
		return categoryDataSet;
	}

	public XYDataset getFirstXYDataset() {
		XYSeriesCollection conjunto = new XYSeriesCollection();

		XYSeries series = new XYSeries("Verbas");
		series.setDescription("Despesas com verbas - Folha Inativo");
		series.add(1, 8);
		series.add(2, 7);
		series.add(3, 6);
		series.add(4, 5);
		series.add(5, 4);
		series.add(6, 3);
		series.add(7, 7);
		series.add(8, 8);

		XYSeries series2 = new XYSeries("Verbas");
		series2.setDescription("Despesas com verbas - Folha Ativo");
		series2.add(1, 2);
		series2.add(2, 3);
		series2.add(3, 8);
		series2.add(4, 5);
		series2.add(5, 2);
		series2.add(6, 3);
		series2.add(7, 5);
		series2.add(8, 6);

		conjunto.addSeries(series);
		conjunto.addSeries(series2);

		return conjunto;
	}

	public DefaultKeyedValueDataset getDadosChaveValor() {
		/*
		 * JFreeChart chart = ChartFactory.createXYBarChart(mensagem, mensagem, apresentarIrUltimo, mensagem, arg4,
		 * arg5, apresentarIrUltimo, apresentarIrUltimo, apresentarIrUltimo).createTimeSeriesChart(
		 * "Multiple Axis Demo 3", "Time of Day", "Primary Range Axis", dataset1, true, true, false );
		 * 
		 * DefaultKeyedValueDataset teste1 = new DefaultKeyedValueDataset(); teste1.setValue("Valor1", 1);
		 * teste1.setValue("Valor1", 2); teste1.setValue("Valor1", 3); teste1.setValue("Valor2", 3);
		 * teste1.setValue("Valor2", 2); teste1.setValue("Valor2", 1); return teste1;
		 */
		return null;
	}

	public DefaultPieDataset getDadosGraficoServidoresPorSituacaoFuncional() {
		DefaultPieDataset ds = null;
		try {
			// if (params.containsKey("year")) {
			// year = (String) params.get("year");
			// }
			ds = new DefaultPieDataset();
			// while (rs.next()) {
			ds.setValue("Unidade Goiânia", new Integer(3403));
			ds.setValue("Unidade Salvador", new Integer(2780));
			ds.setValue("Unidade Niterói", new Integer(6788));
			// }
		} catch (Exception se) {
			throw new RuntimeException(se);
		}
		return ds;
	}

}
