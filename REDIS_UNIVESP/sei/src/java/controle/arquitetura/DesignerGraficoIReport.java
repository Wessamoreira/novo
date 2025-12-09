package controle.arquitetura;

import java.awt.Color;
import java.text.AttributedString;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.Plot;
import org.jfree.data.general.PieDataset;

import net.sf.jasperreports.engine.JRAbstractChartCustomizer;
import net.sf.jasperreports.engine.JRChart;

public class DesignerGraficoIReport extends JRAbstractChartCustomizer {

	@Override
	public void customize(JFreeChart arg0, JRChart arg1) {
		if (arg0.getLegend() != null) {
			arg0.getLegend().setBorder(0, 0, 0, 0);

		}

		Plot plot = arg0.getPlot();
		if (plot instanceof CategoryPlot) {

			CategoryPlot categoryPlot = (CategoryPlot) plot;
			categoryPlot.getRangeAxis().setAutoRange(true);
			categoryPlot.getRangeAxis().setUpperMargin(0.3);

		} else if (plot instanceof PiePlot) {
			PiePlot piePlot = (PiePlot) plot;
			piePlot.setBaseSectionPaint(Color.WHITE);
			// Sombra do grafico
			piePlot.setBaseSectionOutlinePaint(Color.WHITE);
			// Background do tooltip
			piePlot.setLabelBackgroundPaint(Color.WHITE);
			// Sombra do tooltip
			piePlot.setLabelShadowPaint(Color.WHITE);
			piePlot.setBackgroundPaint(Color.WHITE);

			// Borda do tooltip
			piePlot.setLabelOutlinePaint(Color.WHITE);
			// Font do tooltip
			piePlot.setLabelFont(new java.awt.Font("Arial", java.awt.Font.CENTER_BASELINE, 6));
			if (piePlot.getLabelGenerator() == null) {

				PieSectionLabelGenerator pieSectionLabelGenerator = new PieSectionLabelGenerator() {
					@SuppressWarnings("rawtypes")
					@Override
					public String generateSectionLabel(PieDataset dataset, Comparable key) {

						return key.toString();
					}

					@SuppressWarnings("rawtypes")
					@Override
					public AttributedString generateAttributedSectionLabel(PieDataset dataset, Comparable arg1) {
		
						return null;
					}
				};

				piePlot.setLabelGenerator(pieSectionLabelGenerator);

			}

		}

	}

}
