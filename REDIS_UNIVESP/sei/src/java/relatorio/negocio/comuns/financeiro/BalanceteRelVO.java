/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.financeiro;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.utilitarias.Uteis;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;

/**
 * 
 * @author otimize-ti
 */
public class BalanceteRelVO {

	protected Double valor;
	protected String entidade;
	protected Integer codigoEntidade;
	protected String tipo;
	protected Double ano;
	protected Double mes;
	protected Double valorTotalReceita;
	protected Double valorTotalDespesa;
	protected Double valorTotalLucroPrejuizo;
	protected Date dataInicio;
	protected Date dataFim;
	protected String pesquisar;
	protected List<BalanceteRelVO> listaBalanceteRelVOTotal;
	protected List<BalanceteRelVO> listaBalanceteRelVOReceita;
	protected List<BalanceteRelVO> listaBalanceteRelVODespesa;
	protected List<BalanceteRelVO> listaBalanceteRelVOReceitaAgrupada;
	protected List<BalanceteRelVO> listaBalanceteRelVODespesaAgrupada;
	protected List<BalanceteRelVO> listaBalanceteRelVOReceitaAgrupadaData;
	protected List<BalanceteRelVO> listaBalanceteRelVODespesaAgrupadaData;
	protected List<BalanceteRelVO> listaBalanceteRelVOLucroPrejuizo;
	protected DefaultCategoryDataset balanceteBarra;
	protected TimeSeriesCollection balanceteLinhaTempo;
	protected List<Map<String, Double>> listaMesAno;
	protected JRDataSource listaReceitaAgrupada;
	protected JRDataSource listaDespesaAgrupada;

	public BalanceteRelVO() {
		inicilizarDados();
	}

	public void inicilizarDados() {
		setEntidade("");
		setCodigoEntidade(0);
		setTipo("");
		setPesquisar("PR");
		setAno(new Double(0.0));
		setMes(new Double(0.0));
		inicializarValoresReceitaDespesaLucro();
		setDataInicio(Uteis.getNewDateComMesesAMenos(1));
		setDataFim(new Date());
		setListaBalanceteRelVOTotal(new ArrayList<BalanceteRelVO>(0));		
	}

	public void inicializarValoresReceitaDespesaLucro() {
		setValor(new Double(0.0));
		setValorTotalReceita(new Double(0.0));
		setValorTotalDespesa(new Double(0.0));
		setValorTotalLucroPrejuizo(new Double(0.0));
		setListaBalanceteRelVODespesa(new ArrayList<BalanceteRelVO>(0));
		setListaBalanceteRelVODespesaAgrupada(new ArrayList<BalanceteRelVO>(0));
		setListaBalanceteRelVOReceita(new ArrayList<BalanceteRelVO>(0));
		setListaBalanceteRelVOReceitaAgrupada(new ArrayList<BalanceteRelVO>(0));
		setListaBalanceteRelVOReceitaAgrupadaData(new ArrayList<BalanceteRelVO>(0));
		setListaBalanceteRelVODespesaAgrupadaData(new ArrayList<BalanceteRelVO>(0));
		setListaBalanceteRelVOLucroPrejuizo(new ArrayList<BalanceteRelVO>(0));
		setListaMesAno(new ArrayList<Map<String, Double>>());
	}

	public void montarCentroReceitaDespesa() throws ParseException {
		inicializarValoresReceitaDespesaLucro();
		for (BalanceteRelVO obj : getListaBalanceteRelVOTotal()) {
			if (obj.getTipo().equals("Receita")) {
				getListaBalanceteRelVOReceita().add(obj);
				setValorTotalReceita(getValorTotalReceita() + obj.getValor());
			} else {
				getListaBalanceteRelVODespesa().add(obj);
				setValorTotalDespesa(getValorTotalDespesa() + obj.getValor());
			}
		}
		setValorTotalLucroPrejuizo(getValorTotalReceita() - getValorTotalDespesa());
		gerarGraficoBarraBalancete(getValorTotalReceita(), getValorTotalDespesa(), getValorTotalLucroPrejuizo());
		gerarGraficoLinhaTempoBalancete();
		montarCentroReceitaAgrupada();
		montarCentroDespesaAgrupada();
	}

	public void montarCentroReceitaAgrupada() {
		for (BalanceteRelVO objRec : getListaBalanceteRelVOReceita()) {
			boolean novoObj = true;
			if (listaBalanceteRelVOReceitaAgrupada.size() == 0) {
				listaBalanceteRelVOReceitaAgrupada.add(objRec);
			} else {
				for (BalanceteRelVO objRecExistente : listaBalanceteRelVOReceitaAgrupada) {
					if (objRec.getEntidade().equals(objRecExistente.getEntidade())) {
						objRecExistente.setValor(objRecExistente.getValor() + objRec.getValor());
						novoObj = false;
						break;
					}
				}
				if (novoObj) {
					listaBalanceteRelVOReceitaAgrupada.add(objRec);
				}
			}
		}
		setListaReceitaAgrupada(new JRBeanArrayDataSource(getListaBalanceteRelVOReceitaAgrupada().toArray()));
	}

	public void montarCentroDespesaAgrupada() {

		for (BalanceteRelVO objDesp : getListaBalanceteRelVODespesa()) {
			boolean novoObj = true;
			if (listaBalanceteRelVODespesaAgrupada.size() == 0) {
				listaBalanceteRelVODespesaAgrupada.add(objDesp);
			} else {
				for (BalanceteRelVO objDespExistente : listaBalanceteRelVODespesaAgrupada) {
					if (objDesp.getEntidade().equals(objDespExistente.getEntidade())) {
						objDespExistente.setValor(objDespExistente.getValor() + objDesp.getValor());
						novoObj = false;
						break;
					}
				}
				if (novoObj) {
					listaBalanceteRelVODespesaAgrupada.add(objDesp);
				}
			}
		}
		setListaDespesaAgrupada(new JRBeanArrayDataSource(getListaBalanceteRelVODespesaAgrupada().toArray()));
	}

	public void gerarGraficoBarraBalancete(Double valorTotalReceita, Double valorTotalDespesa, Double valorTotalLucroPrejuizo) {
		balanceteBarra = new DefaultCategoryDataset();
		balanceteBarra.addValue(valorTotalReceita, "Receita", "Receita");
		balanceteBarra.addValue(valorTotalDespesa, "Despesa", "Despesa");
		balanceteBarra.addValue(valorTotalLucroPrejuizo, "Lucro/Prejuizo", "Lucro/Prejuizo");
	}

	public void gerarGraficoLinhaTempoBalancete() throws ParseException {
		balanceteLinhaTempo = new TimeSeriesCollection();
		setListaBalanceteRelVOReceitaAgrupadaData(agruparPorData(getListaBalanceteRelVOReceita()));
		setListaBalanceteRelVODespesaAgrupadaData(agruparPorData(getListaBalanceteRelVODespesa()));
		preencherMeses();
		gerarGraficoLinhaTempoCredito();
		gerarGraficoLinhaTempoDebito();
		gerarGraficoLinhaTempoLucroPrejuizo();
	}

	public void preencherMeses() {
		boolean novoMesAnoReceita = true;
		boolean novoMesAnoDespesa = true;
		for (Map<String, Double> map : listaMesAno) {
			novoMesAnoReceita = true;
			novoMesAnoDespesa = true;
			for (BalanceteRelVO receita : getListaBalanceteRelVOReceitaAgrupadaData()) {
				if (map.get("mes").equals(receita.getMes()) && map.get("ano").equals(receita.getAno())) {
					novoMesAnoReceita = Boolean.FALSE;
				}
			}
			for (BalanceteRelVO despesa : getListaBalanceteRelVODespesaAgrupadaData()) {
				if (map.get("mes").equals(despesa.getMes()) && map.get("ano").equals(despesa.getAno())) {
					novoMesAnoDespesa = Boolean.FALSE;
				}
			}
			if (novoMesAnoReceita) {
				BalanceteRelVO obj = new BalanceteRelVO();
				obj.setMes(map.get("mes"));
				obj.setAno(map.get("ano"));
				obj.setValorTotalReceita(0.0);
				listaBalanceteRelVOReceitaAgrupadaData.add(obj);
			}
			if (novoMesAnoDespesa) {
				BalanceteRelVO obj = new BalanceteRelVO();
				obj.setMes(map.get("mes"));
				obj.setAno(map.get("ano"));
				obj.setValorTotalDespesa(0.0);
				listaBalanceteRelVODespesaAgrupadaData.add(obj);
			}
		}
	}

	public void gerarGraficoLinhaTempoCredito() {
		TimeSeries ts = new TimeSeries("Receita", Month.class);
		for (BalanceteRelVO balanceteRelVO : getListaBalanceteRelVOReceitaAgrupadaData()) {
			ts.add(new Month(balanceteRelVO.getMes().intValue(), new Year(balanceteRelVO.getAno().intValue())), balanceteRelVO.getValorTotalReceita());
		}
		balanceteLinhaTempo.addSeries(ts);
	}

	public JRDataSource getListaGeral() {
		JRDataSource jr = new JRBeanArrayDataSource(getListaBalanceteRelVOLucroPrejuizo().toArray());
		return jr;
	}
	
	public void gerarGraficoLinhaTempoDebito() {
		TimeSeries ts = new TimeSeries("Despesa", Month.class);
		for (BalanceteRelVO balanceteRelVO : getListaBalanceteRelVODespesaAgrupadaData()) {
			ts.add(new Month(balanceteRelVO.getMes().intValue(), new Year(balanceteRelVO.getAno().intValue())), balanceteRelVO.getValorTotalDespesa());
		}
		balanceteLinhaTempo.addSeries(ts);
	}

	public List<BalanceteRelVO> agruparPorData(List<BalanceteRelVO> lista) {
		List<BalanceteRelVO> balanceteRelVOs = new ArrayList<BalanceteRelVO>(0);
		for (BalanceteRelVO balanceteRelVO : lista) {
			agruparBalanceteRelVO(balanceteRelVOs, balanceteRelVO);
		}
		return balanceteRelVOs;
	}

	public List<BalanceteRelVO> getCalcularLucroPrejuizo() throws ParseException {
		List<BalanceteRelVO> listaLucroPrejuizo = new ArrayList<BalanceteRelVO>(0);
		for (BalanceteRelVO balanceteRelVO : getListaBalanceteRelVODespesaAgrupadaData()) {
			agruparListaLucroPrejuizo(listaLucroPrejuizo, balanceteRelVO);
		}
		for (BalanceteRelVO balanceteRelVO : getListaBalanceteRelVOReceitaAgrupadaData()) {
			agruparListaLucroPrejuizo(listaLucroPrejuizo, balanceteRelVO);
		}
		Collections.sort(listaLucroPrejuizo, new Uteis.OrdenaListaBalanceteRelVOPorData());
		int x = 1;
		Double lucroPrejuizo = 0.0;
		for (BalanceteRelVO balanceteRelVO : listaLucroPrejuizo) {
			if (x <= listaLucroPrejuizo.size()) {
				lucroPrejuizo = balanceteRelVO.getValorTotalReceita() - balanceteRelVO.getValorTotalDespesa() + lucroPrejuizo;
				balanceteRelVO.setValorTotalLucroPrejuizo(lucroPrejuizo);
			}
			x++;
		}
		setListaBalanceteRelVOLucroPrejuizo(listaLucroPrejuizo);
		return listaLucroPrejuizo;

	}

	public Date getData() throws ParseException {
		String data = "1/" + String.valueOf(mes.intValue()) + "/" + String.valueOf(ano.intValue());
		Date dataFinal = Uteis.getData(data, "dd/MM/yyyy");
		return dataFinal;
	}

	public void agruparListaLucroPrejuizo(List<BalanceteRelVO> lista, BalanceteRelVO balanceteRelVO) {
		for (BalanceteRelVO balanceteRelVO2 : lista) {
			if (balanceteRelVO2.getMes().doubleValue() == balanceteRelVO.getMes().doubleValue() && balanceteRelVO.getAno().doubleValue() == balanceteRelVO2.getAno().doubleValue()) {
				if (balanceteRelVO.getTipo().equals("Receita")) {
					balanceteRelVO2.setValorTotalReceita(balanceteRelVO2.getValorTotalReceita() + balanceteRelVO.getValorTotalReceita());
				}
				if (balanceteRelVO.getTipo().equals("Despesa")) {
					balanceteRelVO2.setValorTotalDespesa(balanceteRelVO2.getValorTotalDespesa() + balanceteRelVO.getValorTotalDespesa());
				}
				return;
			}
		}
		if (balanceteRelVO.getTipo().equals("Receita")) {
			balanceteRelVO.setValorTotalReceita(balanceteRelVO.getValorTotalReceita());
		}
		if (balanceteRelVO.getTipo().equals("Despesa")) {
			balanceteRelVO.setValorTotalDespesa(balanceteRelVO.getValorTotalDespesa());
		}
		lista.add(balanceteRelVO);
	}

	public void agruparBalanceteRelVO(List<BalanceteRelVO> lista, BalanceteRelVO balanceteRelVO) {
		Map<String, Double> mesAno;
		boolean novoMesAno = true;
		for (BalanceteRelVO balanceteRelVO2 : lista) {
			if (balanceteRelVO2.getMes().doubleValue() == balanceteRelVO.getMes().doubleValue() && balanceteRelVO.getAno().doubleValue() == balanceteRelVO2.getAno().doubleValue()) {
				for (Map<String, Double> map : listaMesAno) {
					if (map.get("mes").equals(balanceteRelVO.getMes()) && map.get("ano").equals(balanceteRelVO.getAno())) {
						novoMesAno = Boolean.FALSE;
					}
				}
				if (balanceteRelVO.getTipo().equals("Receita")) {
					balanceteRelVO2.setValorTotalReceita(balanceteRelVO2.getValorTotalReceita() + balanceteRelVO.getValor());
				}
				if (balanceteRelVO.getTipo().equals("Despesa")) {
					balanceteRelVO2.setValorTotalDespesa(balanceteRelVO2.getValorTotalDespesa() + balanceteRelVO.getValor());
				}
				return;
			}
		}
		if (balanceteRelVO.getTipo().equals("Receita")) {
			for (Map<String, Double> map : listaMesAno) {
				if (map.get("mes").equals(balanceteRelVO.getMes()) && map.get("ano").equals(balanceteRelVO.getAno())) {
					novoMesAno = Boolean.FALSE;
				}
			}
			balanceteRelVO.setValorTotalReceita(balanceteRelVO.getValor());
		}
		if (balanceteRelVO.getTipo().equals("Despesa")) {
			for (Map<String, Double> map : listaMesAno) {
				if (map.get("mes").equals(balanceteRelVO.getMes()) && map.get("ano").equals(balanceteRelVO.getAno())) {
					novoMesAno = Boolean.FALSE;
				}
			}
			balanceteRelVO.setValorTotalDespesa(balanceteRelVO.getValor());
		}
		if (novoMesAno) {
			mesAno = new HashMap<String, Double>();
			mesAno.put("mes", balanceteRelVO.getMes());
			mesAno.put("ano", balanceteRelVO.getAno());
			listaMesAno.add(mesAno);
		}

		lista.add(balanceteRelVO);
	}

	public void gerarGraficoLinhaTempoLucroPrejuizo() throws ParseException {

		TimeSeries ts = new TimeSeries("Lucro/Prejuizo", Month.class);
		for (BalanceteRelVO balanceteRelVO : getCalcularLucroPrejuizo()) {
			balanceteRelVO.setTipo("LucroPrejuizo");
			ts.add(new Month(balanceteRelVO.getMes().intValue(), new Year(balanceteRelVO.getAno().intValue())), balanceteRelVO.getValorTotalLucroPrejuizo());
		}
		balanceteLinhaTempo.addSeries(ts);
	}

	/**
	 * @return the valor
	 */
	public Double getValor() {
		return valor;
	}

	/**
	 * @param valor
	 *            the valor to set
	 */
	public void setValor(Double valor) {
		this.valor = valor;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param tipo
	 *            the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the valorTotalReceita
	 */
	public Double getValorTotalReceita() {
		return valorTotalReceita;
	}

	/**
	 * @param valorTotalReceita
	 *            the valorTotalReceita to set
	 */
	public void setValorTotalReceita(Double valorTotalReceita) {
		this.valorTotalReceita = valorTotalReceita;
	}

	/**
	 * @return the valorTotalDespesa
	 */
	public Double getValorTotalDespesa() {
		return valorTotalDespesa;
	}

	/**
	 * @param valorTotalDespesa
	 *            the valorTotalDespesa to set
	 */
	public void setValorTotalDespesa(Double valorTotalDespesa) {
		this.valorTotalDespesa = valorTotalDespesa;
	}

	/**
	 * @return the valorTotalLucroPrejuizo
	 */
	public Double getValorTotalLucroPrejuizo() {
		return valorTotalLucroPrejuizo;
	}

	/**
	 * @param valorTotalLucroPrejuizo
	 *            the valorTotalLucroPrejuizo to set
	 */
	public void setValorTotalLucroPrejuizo(Double valorTotalLucroPrejuizo) {
		this.valorTotalLucroPrejuizo = valorTotalLucroPrejuizo;
	}

	/**
	 * @return the dataInicio
	 */
	public Date getDataInicio() {
		return dataInicio;
	}

	/**
	 * @param dataInicio
	 *            the dataInicio to set
	 */
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	/**
	 * @return the dataFim
	 */
	public Date getDataFim() {
		return dataFim;
	}

	/**
	 * @param dataFim
	 *            the dataFim to set
	 */
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	/**
	 * @return the pesquisar
	 */
	public String getPesquisar() {
		return pesquisar;
	}

	/**
	 * @param pesquisar
	 *            the pesquisar to set
	 */
	public void setPesquisar(String pesquisar) {
		this.pesquisar = pesquisar;
	}

	/**
	 * @return the entidade
	 */
	public String getEntidade() {
		return entidade;
	}

	/**
	 * @param entidade
	 *            the entidade to set
	 */
	public void setEntidade(String entidade) {
		this.entidade = entidade;
	}

	/**
	 * @return the codigoEntidade
	 */
	public Integer getCodigoEntidade() {
		return codigoEntidade;
	}

	/**
	 * @param codigoEntidade
	 *            the codigoEntidade to set
	 */
	public void setCodigoEntidade(Integer codigoEntidade) {
		this.codigoEntidade = codigoEntidade;
	}

	/**
	 * @return the ano
	 */
	public Double getAno() {
		return ano;
	}

	/**
	 * @param ano
	 *            the ano to set
	 */
	public void setAno(Double ano) {
		this.ano = ano;
	}

	/**
	 * @return the mes
	 */
	public Double getMes() {
		return mes;
	}

	/**
	 * @param mes
	 *            the mes to set
	 */
	public void setMes(Double mes) {
		this.mes = mes;
	}

	/**
	 * @return the listaBalanceteRelVOTotal
	 */
	public List<BalanceteRelVO> getListaBalanceteRelVOTotal() {
		return listaBalanceteRelVOTotal;
	}

	/**
	 * @param listaBalanceteRelVOTotal
	 *            the listaBalanceteRelVOTotal to set
	 */
	public void setListaBalanceteRelVOTotal(List<BalanceteRelVO> listaBalanceteRelVOTotal) {
		this.listaBalanceteRelVOTotal = listaBalanceteRelVOTotal;
	}

	/**
	 * @return the listaBalanceteRelVOReceita
	 */
	public List<BalanceteRelVO> getListaBalanceteRelVOReceita() {
		return listaBalanceteRelVOReceita;
	}

	/**
	 * @param listaBalanceteRelVOReceita
	 *            the listaBalanceteRelVOReceita to set
	 */
	public void setListaBalanceteRelVOReceita(List<BalanceteRelVO> listaBalanceteRelVOReceita) {
		this.listaBalanceteRelVOReceita = listaBalanceteRelVOReceita;
	}

	/**
	 * @return the listaBalanceteRelVODespesa
	 */
	public List<BalanceteRelVO> getListaBalanceteRelVODespesa() {
		return listaBalanceteRelVODespesa;
	}

	/**
	 * @param listaBalanceteRelVODespesa
	 *            the listaBalanceteRelVODespesa to set
	 */
	public void setListaBalanceteRelVODespesa(List<BalanceteRelVO> listaBalanceteRelVODespesa) {
		this.listaBalanceteRelVODespesa = listaBalanceteRelVODespesa;
	}

	/**
	 * @return the listaBalanceteRelVOReceitaAgrupada
	 */
	public List<BalanceteRelVO> getListaBalanceteRelVOReceitaAgrupada() {
		return listaBalanceteRelVOReceitaAgrupada;
	}

	/**
	 * @param listaBalanceteRelVOReceitaAgrupada
	 *            the listaBalanceteRelVOReceitaAgrupada to set
	 */
	public void setListaBalanceteRelVOReceitaAgrupada(List<BalanceteRelVO> listaBalanceteRelVOReceitaAgrupada) {
		this.listaBalanceteRelVOReceitaAgrupada = listaBalanceteRelVOReceitaAgrupada;
	}

	/**
	 * @return the listaBalanceteRelVODespesaAgrupada
	 */
	public List<BalanceteRelVO> getListaBalanceteRelVODespesaAgrupada() {
		return listaBalanceteRelVODespesaAgrupada;
	}

	/**
	 * @param listaBalanceteRelVODespesaAgrupada
	 *            the listaBalanceteRelVODespesaAgrupada to set
	 */
	public void setListaBalanceteRelVODespesaAgrupada(List<BalanceteRelVO> listaBalanceteRelVODespesaAgrupada) {
		this.listaBalanceteRelVODespesaAgrupada = listaBalanceteRelVODespesaAgrupada;
	}

	/**
	 * @return the balanceteBarra
	 */
	public DefaultCategoryDataset getBalanceteBarra() {
		return balanceteBarra;
	}

	/**
	 * @param balanceteBarra
	 *            the balanceteBarra to set
	 */
	public void setBalanceteBarra(DefaultCategoryDataset balanceteBarra) {
		this.balanceteBarra = balanceteBarra;
	}

	/**
	 * @return the balancetelinhaTempo
	 */
	public TimeSeriesCollection getBalanceteLinhaTempo() {
		return balanceteLinhaTempo;
	}

	/**
	 * @param balancetelinhaTempo
	 *            the balancetelinhaTempo to set
	 */
	public void setBalanceteLinhaTempo(TimeSeriesCollection balanceteLinhaTempo) {
		this.balanceteLinhaTempo = balanceteLinhaTempo;
	}

	/**
	 * @return the listaBalanceteRelVOReceitaAgrupadaData
	 */
	public List<BalanceteRelVO> getListaBalanceteRelVOReceitaAgrupadaData() {
		return listaBalanceteRelVOReceitaAgrupadaData;
	}

	/**
	 * @param listaBalanceteRelVOReceitaAgrupadaData
	 *            the listaBalanceteRelVOReceitaAgrupadaData to set
	 */
	public void setListaBalanceteRelVOReceitaAgrupadaData(List<BalanceteRelVO> listaBalanceteRelVOReceitaAgrupadaData) {
		this.listaBalanceteRelVOReceitaAgrupadaData = listaBalanceteRelVOReceitaAgrupadaData;
	}

	/**
	 * @return the listaBalanceteRelVODespesaAgrupadaData
	 */
	public List<BalanceteRelVO> getListaBalanceteRelVODespesaAgrupadaData() {
		return listaBalanceteRelVODespesaAgrupadaData;
	}

	/**
	 * @param listaBalanceteRelVODespesaAgrupadaData
	 *            the listaBalanceteRelVODespesaAgrupadaData to set
	 */
	public void setListaBalanceteRelVODespesaAgrupadaData(List<BalanceteRelVO> listaBalanceteRelVODespesaAgrupadaData) {
		this.listaBalanceteRelVODespesaAgrupadaData = listaBalanceteRelVODespesaAgrupadaData;
	}

	/**
	 * @return the listaMesAno
	 */
	public List<Map<String, Double>> getListaMesAno() {
		return listaMesAno;
	}

	/**
	 * @param listaMesAno
	 *            the listaMesAno to set
	 */
	public void setListaMesAno(List<Map<String, Double>> listaMesAno) {
		this.listaMesAno = listaMesAno;
	}

	/**
	 * @return the listaBalanceteRelVOLucroPrejuizo
	 */
	public List<BalanceteRelVO> getListaBalanceteRelVOLucroPrejuizo() {
		return listaBalanceteRelVOLucroPrejuizo;
	}

	/**
	 * @param listaBalanceteRelVOLucroPrejuizo
	 *            the listaBalanceteRelVOLucroPrejuizo to set
	 */
	public void setListaBalanceteRelVOLucroPrejuizo(List<BalanceteRelVO> listaBalanceteRelVOLucroPrejuizo) {
		this.listaBalanceteRelVOLucroPrejuizo = listaBalanceteRelVOLucroPrejuizo;
	}

	/**
	 * @param listaReceitaAgrupada the listaReceitaAgrupada to set
	 */
	public void setListaReceitaAgrupada(JRDataSource listaReceitaAgrupada) {
		this.listaReceitaAgrupada = listaReceitaAgrupada;
	}

	/**
	 * @param listaDespesaAgrupada the listaDespesaAgrupada to set
	 */
	public void setListaDespesaAgrupada(JRDataSource listaDespesaAgrupada) {
		this.listaDespesaAgrupada = listaDespesaAgrupada;
	}

	/**
	 * @return the listaReceitaAgrupada
	 */
	public JRDataSource getListaReceitaAgrupada() {
		return listaReceitaAgrupada;
	}

	/**
	 * @return the listaDespesaAgrupada
	 */
	public JRDataSource getListaDespesaAgrupada() {
		return listaDespesaAgrupada;
	}

}
