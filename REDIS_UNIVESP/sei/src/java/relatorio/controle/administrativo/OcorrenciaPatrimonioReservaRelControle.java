/**
 * 
 */
package relatorio.controle.administrativo;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoConsultaLocalArmazenamentoEnum;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.patrimonio.LocalArmazenamentoVO;
import negocio.comuns.patrimonio.OcorrenciaPatrimonioVO;
import negocio.comuns.patrimonio.PatrimonioUnidadeVO;
import negocio.comuns.patrimonio.TextoPadraoPatrimonioVO;
import negocio.comuns.patrimonio.TipoPatrimonioVO;
import negocio.comuns.patrimonio.enumeradores.TipoOcorrenciaPatrimonioEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.administrativo.OcorrenciaPatrimonioRelVO;
import controle.arquitetura.DataModelo;

/**
 * @author Kennedy Souza
 *
 */
@Controller("OcorrenciaPatrimonioReservaRelControle")
@Scope("viewScope")
@Lazy
public class OcorrenciaPatrimonioReservaRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = -560946162252698196L;

	private OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO;
	private OcorrenciaPatrimonioVO ocorrenciaPatrimonioSelecionadoExclusaoVO;
	private List<OcorrenciaPatrimonioVO> ocorrenciaPatrimonioVOs;
	private List<LocalArmazenamentoVO> localArmazenamentoVOs;
	private Date dataInicialConsulta;
	private Date dataFinalConsulta;
	private Date horaInicialConsulta;
	private Date horaFinalConsulta;
	private Date dataHoraInicialConsulta;
	private Date dataHoraFinalConsulta;
	
	private DataModelo controleConsultaLocalArmazenamento;
	private DataModelo controleConsultaPatrimonioUnidade;
	private DataModelo controleConsultaFuncionario;

	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private UnidadeEnsinoVO unidadeEnsino;
	private List<SelectItem> listaSelectItemOpcaoConsultaFuncionario;
	private List<SelectItem> listaSelectItemOpcaoConsultaPatrimonioUnidade;
	private List<SelectItem> listaSelectItemOpcaoConsultaLocalArmazenamento;
	private List<SelectItem> listaSelectItemOpcaoConsulta;
	private List<SelectItem> listaSelectItemTextoPadraoPatrimonio;
	private List<SelectItem> listaSelectItemTipoOcorrenciaPatrimonio;
	private List<SelectItem> listaSelectItemTipoPatrimonio;
	
	private TipoConsultaLocalArmazenamentoEnum consultarLocalArmazenamentoPor;
	private OcorrenciaPatrimonioRelVO ocorrenciaPatrimonioRelVO;
	private int totalReserva;

	public String getGrafico() {
		Calendar calHoraInicio = Calendar.getInstance();
		Calendar calHoraTermino = Calendar.getInstance();
		StringBuilder stg = new StringBuilder();
		StringBuilder stgLinhasGraficos = new StringBuilder();
		int contador = 0;
		totalReserva = 0;

		stg.append(" js7_2(function () {                               ");
		stg.append(" 	window.chart2 = new Highcharts.Chart({         ");
		stg.append(" 		  chart: {                                 ");
		stg.append(" 	        renderTo: 'container',                 ");
		stg.append(" 	        type: 'columnrange',    zoomType: 'xy', ");
		stg.append(" 	        inverted: true                         ");
		stg.append(" 	    },                                         ");
		stg.append("       title: {                                    ");
		stg.append("           text: 'Selecione a Reserva Para Excluir - (Reservas)' ");
		stg.append("       },                                          ");
		stg.append("       xAxis: {                                    ");
		stg.append(" categories: [  ");

		if (getOcorrenciaPatrimonioVO().getIsApresentarDadosReservaUnidade()) {
			for (PatrimonioUnidadeVO unidade : getPatrimonioUnidadeVOs()) {
				atualizarDataHoras();
				if(!unidade.getListaOcorrencias().isEmpty()){
					stg.append(" '").append(unidade.getCodigoBarra()).append(("',  "));
				}

				for (OcorrenciaPatrimonioVO ocrrencia : unidade.getListaOcorrencias()) {
					StringBuilder strgHoraInicio = new StringBuilder();
					StringBuilder strgHoraTermino = new StringBuilder();	
					calHoraInicio.setTime(ocrrencia.getDataInicioReserva());
					calHoraTermino.setTime(ocrrencia.getDataTerminoReserva());

					strgHoraInicio.append(calHoraInicio.get(Calendar.YEAR)).append(", ").append(calHoraInicio.get(Calendar.MONTH)).append(", ").append(calHoraInicio.get(Calendar.DAY_OF_MONTH)).append(", ").append(calHoraInicio.get(Calendar.HOUR_OF_DAY)).append(", ").append(calHoraInicio.get(Calendar.MINUTE)).append(", ").append(calHoraInicio.get(Calendar.SECOND));
					strgHoraTermino.append(calHoraTermino.get(Calendar.YEAR)).append(", ").append(calHoraTermino.get(Calendar.MONTH)).append(", ").append(calHoraTermino.get(Calendar.DAY_OF_MONTH)).append(", ").append(calHoraTermino.get(Calendar.HOUR_OF_DAY)).append(", ").append(calHoraTermino.get(Calendar.MINUTE)).append(", ").append(calHoraTermino.get(Calendar.SECOND));

					stgLinhasGraficos.append("{x: " + contador + ",low: Date.UTC(" + strgHoraInicio + "),high: Date.UTC(" + strgHoraTermino + "),color: 'red',tipo: 'Ocupado', ocorrencia :"+ocrrencia.getCodigo()+
							", solicitante: '"+ocrrencia.getSolicitanteEmprestimo().getPessoa().getNome()+ "', localUso: '"+ocrrencia.getLocalArmazenamentoDestino().getLocalArmazenamento()+
							"', nomeUnidadeLocal:'"+unidade.getPatrimonioVO().getDescricao()+"'}, ");
					totalReserva++;
				}
				if(!unidade.getListaOcorrencias().isEmpty()){
					contador++;
				}
			}
		}else if (getOcorrenciaPatrimonioVO().getIsApresentarDadosReservaLocal()) {
					
			for(LocalArmazenamentoVO local : getLocalArmazenamentoVOs()){
				atualizarDataHoras();
				stg.append(" '").append(local.getCodigo()).append(("',  "));
				
				for (OcorrenciaPatrimonioVO ocrrencia : local.getListaOcorrencias()) {
					calHoraInicio.setTime(ocrrencia.getDataInicioReserva());
					calHoraTermino.setTime(ocrrencia.getDataTerminoReserva());
					StringBuilder strgHoraInicio = new StringBuilder();
					StringBuilder strgHoraTermino = new StringBuilder();
					strgHoraInicio.append(calHoraInicio.get(Calendar.YEAR)).append(", ").append(calHoraInicio.get(Calendar.MONTH)).append(", ").append(calHoraInicio.get(Calendar.DAY_OF_MONTH)).append(", ").append(calHoraInicio.get(Calendar.HOUR_OF_DAY)).append(", ").append(calHoraInicio.get(Calendar.MINUTE)).append(", ").append(calHoraInicio.get(Calendar.SECOND));
					strgHoraTermino.append(calHoraTermino.get(Calendar.YEAR)).append(", ").append(calHoraTermino.get(Calendar.MONTH)).append(", ").append(calHoraTermino.get(Calendar.DAY_OF_MONTH)).append(", ").append(calHoraTermino.get(Calendar.HOUR_OF_DAY)).append(", ").append(calHoraTermino.get(Calendar.MINUTE)).append(", ").append(calHoraTermino.get(Calendar.SECOND));
					
					stgLinhasGraficos.append("{x: " + contador + ",low: Date.UTC(" + strgHoraInicio + "),high: Date.UTC(" + strgHoraTermino + "),color: 'red',tipo: 'Ocupado', ocorrencia :"+ocrrencia.getCodigo()+
							", solicitante: '"+ocrrencia.getSolicitanteEmprestimo().getPessoa().getNome()+ "', localUso: '"+local.getLocalArmazenamento()+
							"', nomeUnidadeLocal:'"+local.getLocalArmazenamento()+"'}, ");
					totalReserva++;
				}
				if(!local.getListaOcorrencias().isEmpty()){
					contador++;
				}
			}
		}
		stg.append(" ],  ");
		stg.append(" 	min: 0,      ");
	 	stg.append(" 	 max: 5,     ");
	    stg.append("  scrollbar: {   ");
	    stg.append("   enabled: true ");
	 	stg.append(" 	 }           ");
		stg.append("       },                                          ");
		stg.append("       yAxis: {                                    ");
		stg.append("           type: 'datetime',                       ");
		stg.append("           title: { text: 'Horário' }              ");
		stg.append("       },                                          ");
		stg.append("  	    legend: { enabled: false },                ");
		stg.append("       exporting: { enabled: false },              ");
		stg.append("       credits: { enabled: false },                ");
		stg.append(" 			tooltip: {                             ");
		stg.append("             formatter: function () {              ");
		stg.append("                 console.log(this);                ");
		stg.append("                return '<b>  ' + this.x + ' - ' +this.point.nomeUnidadeLocal + '</b><br/><b>' + this.point.tipo + '</b> : no período entre <b>' + Highcharts.dateFormat('%d/%m/%y %H:%M', this.point.low) + '</b> e <b>' + 	Highcharts.dateFormat('%d/%m/%y %H:%M', this.point.high) + '</b><br/>'"
				+ " + '<b>Solicitante : </b>' +this.point.solicitante + '<br/>"
				+ " <b>Local Uso  : </b>' + this.point.localUso ;");
		stg.append("             }                                             ");
		stg.append("         },                                                ");
		stg.append(" plotOptions : {                                                  ");
		stg.append(" 	columnrange: {                                                ");
		stg.append("   	point: {                                                      ");
		stg.append("     	events: {                                                 ");
		stg.append("       	click:function(e) {                                       ");
		stg.append("         	selecaoUnidadeExcluir(e.point.ocorrencia); ");
		stg.append("        }                                                         ");
		stg.append("       }                                                          ");
		stg.append("     }                                                            ");
		stg.append("   }                                                              ");
		stg.append(" },																  ");
		stg.append(" 	    series: [{                                         ");
		stg.append(" 	        name: 'Name',                                 ");
		stg.append("             data: [" + stgLinhasGraficos + "] 			   ");
		stg.append(" 	    }]                                                 ");
		stg.append(" 		});                                                ");
		stg.append(" });                                                       ");

		return stg.toString();
	}

	public OcorrenciaPatrimonioVO getOcorrenciaPatrimonioVO() {
		if (ocorrenciaPatrimonioVO == null) {
			ocorrenciaPatrimonioVO = new OcorrenciaPatrimonioVO();
		}
		return ocorrenciaPatrimonioVO;
	}

	public void setOcorrenciaPatrimonioVO(OcorrenciaPatrimonioVO ocorrenciaPatrimonioVO) {
		this.ocorrenciaPatrimonioVO = ocorrenciaPatrimonioVO;
	}

	public List<OcorrenciaPatrimonioVO> getOcorrenciaPatrimonioVOs() {
		if (ocorrenciaPatrimonioVOs == null) {
			ocorrenciaPatrimonioVOs = new ArrayList<OcorrenciaPatrimonioVO>();
		}
		return ocorrenciaPatrimonioVOs;
	}

	public void setOcorrenciaPatrimonioVOs(List<OcorrenciaPatrimonioVO> ocorrenciaPatrimonioVOs) {
		this.ocorrenciaPatrimonioVOs = ocorrenciaPatrimonioVOs;
	}

	public Date getDataInicialConsulta() {
		if (dataInicialConsulta == null) {
			dataInicialConsulta = new Date();
		}
		return dataInicialConsulta;
	}

	public void setDataInicialConsulta(Date dataInicialConsulta) {
		this.dataInicialConsulta = dataInicialConsulta;
	}

	public Date getDataFinalConsulta() {
		if (dataFinalConsulta == null) {
			dataFinalConsulta = new Date();
		}
		return dataFinalConsulta;
	}

	public void setDataFinalConsulta(Date dataFinalConsulta) {
		this.dataFinalConsulta = dataFinalConsulta;
	}

	public Date getHoraInicialConsulta() {
		if (horaInicialConsulta == null) {
			horaInicialConsulta = new Date();
		}
		return horaInicialConsulta;
	}

	public void setHoraInicialConsulta(Date horaInicialConsulta) {
		this.horaInicialConsulta = horaInicialConsulta;
	}

	public Date getHoraFinalConsulta() {
		if (horaFinalConsulta == null) {
			horaFinalConsulta = UteisData.getDataAtualSomandoOuSubtraindoMinutos(60);
		}
		return horaFinalConsulta;
	}

	public void setHoraFinalConsulta(Date horaFinalConsulta) {
		this.horaFinalConsulta = horaFinalConsulta;
	}

	public Date getDataHoraInicialConsulta() {
		if (dataHoraInicialConsulta == null) {
			dataHoraInicialConsulta = new Date();
		}
		return dataHoraInicialConsulta;
	}

	public void setDataHoraInicialConsulta(Date dataHoraInicialConsulta) {
		this.dataHoraInicialConsulta = dataHoraInicialConsulta;
	}

	public Date getDataHoraFinalConsulta() {
		if (dataHoraFinalConsulta == null) {
			dataHoraFinalConsulta = new Date();
		}
		return dataHoraFinalConsulta;
	}

	public void setDataHoraFinalConsulta(Date dataHoraFinalConsulta) {
		this.dataHoraFinalConsulta = dataHoraFinalConsulta;
	}

	public void atualizarDataHoras() {
		Calendar calDataInicio = Calendar.getInstance();
		Calendar calDataTermino = Calendar.getInstance();
		Calendar calHoraInicio = Calendar.getInstance();
		Calendar calHoraTermino = Calendar.getInstance();

		calDataInicio.setTime(getDataInicialConsulta());
		calDataTermino.setTime(getDataFinalConsulta());

		calHoraInicio.setTime(getHoraInicialConsulta());
		calHoraTermino.setTime(getHoraFinalConsulta());

		calHoraInicio.set(Calendar.DAY_OF_MONTH, calDataInicio.get(Calendar.DAY_OF_MONTH));
		calHoraInicio.set(Calendar.MONTH, calDataInicio.get(Calendar.MONTH));
		calHoraInicio.set(Calendar.YEAR, calDataInicio.get(Calendar.YEAR));

		calHoraTermino.set(Calendar.DAY_OF_MONTH, calDataTermino.get(Calendar.DAY_OF_MONTH));
		calHoraTermino.set(Calendar.MONTH, calDataTermino.get(Calendar.MONTH));
		calHoraTermino.set(Calendar.YEAR, calDataTermino.get(Calendar.YEAR));

		setDataHoraInicialConsulta(calHoraInicio.getTime());
		setDataHoraFinalConsulta(calHoraTermino.getTime());

	}

	public void carregarListaHorariosLocal() {
		try {
			getLocalArmazenamentoVOs().clear();
		//	getPatrimonioUnidadeVOs().clear();
			atualizarDataHoras();
			validarFiltros();
			getLocalArmazenamentoVOs().addAll(getFacadeFactory().getLocalArmazenamentoFacade().consultarPorLocalDataUnidadeEnsinoSolicitanteParaListagemDeOcorrenciasPorLocal(getOcorrenciaPatrimonioVO().getLocalReservado().getCodigo(), getUnidadeEnsino(),  getDataHoraInicialConsulta(),
			getDataHoraFinalConsulta(),
			getOcorrenciaPatrimonioVO().getSolicitanteEmprestimo()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void carregarOcorrenciasUnidadesPratrimonio() {
		try {
			getPatrimonioUnidadeVOs().clear();
		//	getLocalArmazenamentoVOs().clear();
			atualizarDataHoras();
			validarFiltros();
			 getPatrimonioUnidadeVOs().addAll(getFacadeFactory().getPatrimonioUnidadeFacade().consultarPorTipoPatrimonioDataUnidadeEnsinoSolicitanteParaListagemDeOcorrenciasPorUnidade(getOcorrenciaPatrimonioVO().getTipoPatrimonioVO().getCodigo(),
			 getUnidadeEnsino(), getDataHoraInicialConsulta(),
			 getDataHoraFinalConsulta(),
			 getOcorrenciaPatrimonioVO().getSolicitanteEmprestimo()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private void validarFiltros() throws Exception {

		if (!Uteis.isAtributoPreenchido(getUnidadeEnsino())) {
			throw new Exception("Informe uma UNIDADE DE ENSINO");
		}
		if (getDataHoraInicialConsulta().after(getDataHoraFinalConsulta())) {
			throw new Exception("A Data Inicial não pode ser DEPOIS da Data Final ");
		}
		if (!Uteis.isAtributoPreenchido(ocorrenciaPatrimonioVO.getTipoOcorrenciaPatrimonio())) {
			throw new Exception("Informe o TIPO DE OCORRENCIA");
		}

	}

	private List<PatrimonioUnidadeVO> patrimonioUnidadeVOs;

	public List<PatrimonioUnidadeVO> getPatrimonioUnidadeVOs() {
		if (patrimonioUnidadeVOs == null) {
			patrimonioUnidadeVOs = new ArrayList<PatrimonioUnidadeVO>(0);
		}
		return patrimonioUnidadeVOs;
	}

	public void setPatrimonioUnidadeVOs(List<PatrimonioUnidadeVO> patrimonioUnidadeVOs) {
		this.patrimonioUnidadeVOs = patrimonioUnidadeVOs;
	}

	public List<SelectItem> getListaSelectItemOpcaoConsultaFuncionario() {
		if (listaSelectItemOpcaoConsultaFuncionario == null) {
			listaSelectItemOpcaoConsultaFuncionario = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsultaFuncionario.add(new SelectItem("nome", "Nome"));
			listaSelectItemOpcaoConsultaFuncionario.add(new SelectItem("matricula", "Matrícula"));
		}
		return listaSelectItemOpcaoConsultaFuncionario;
	}

	public void setListaSelectItemOpcaoConsultaFuncionario(List<SelectItem> listaSelectItemOpcaoConsultaFuncionario) {
		this.listaSelectItemOpcaoConsultaFuncionario = listaSelectItemOpcaoConsultaFuncionario;
	}

	public List<SelectItem> getListaSelectItemOpcaoConsultaPatrimonioUnidade() {
		if (listaSelectItemOpcaoConsultaPatrimonioUnidade == null) {
			listaSelectItemOpcaoConsultaPatrimonioUnidade = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsultaPatrimonioUnidade.add(new SelectItem("DESCRICAO", "Descrição"));
			listaSelectItemOpcaoConsultaPatrimonioUnidade.add(new SelectItem("CODIGO_BARRA", "Código Barra"));
			listaSelectItemOpcaoConsultaPatrimonioUnidade.add(new SelectItem("LOCAL_ARMAZENAMENTO", "Local"));
		}
		return listaSelectItemOpcaoConsultaPatrimonioUnidade;
	}

	public List<SelectItem> tipoConsultaComboFuncionario;

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		if (tipoConsultaComboFuncionario == null) {
			tipoConsultaComboFuncionario = new ArrayList<SelectItem>(0);
			tipoConsultaComboFuncionario.add(new SelectItem("codigo", "Codígo"));
			tipoConsultaComboFuncionario.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboFuncionario;
	}

	public List<SelectItem> getListaSelectItemOpcaoConsultaLocalArmazenamento() {
		if (listaSelectItemOpcaoConsultaLocalArmazenamento == null) {
			listaSelectItemOpcaoConsultaLocalArmazenamento = new ArrayList<SelectItem>(2);
			listaSelectItemOpcaoConsultaLocalArmazenamento.add(new SelectItem(TipoConsultaLocalArmazenamentoEnum.LOCAL, TipoConsultaLocalArmazenamentoEnum.LOCAL.getValorApresentar()));
			listaSelectItemOpcaoConsultaLocalArmazenamento.add(new SelectItem(TipoConsultaLocalArmazenamentoEnum.LOCAL_SUPERIOR, TipoConsultaLocalArmazenamentoEnum.LOCAL_SUPERIOR.getValorApresentar()));
		}
		return listaSelectItemOpcaoConsultaLocalArmazenamento;
	}

	public void setListaSelectItemOpcaoConsultaLocalArmazenamento(List<SelectItem> listaSelectItemOpcaoConsultaLocalArmazenamento) {
		this.listaSelectItemOpcaoConsultaLocalArmazenamento = listaSelectItemOpcaoConsultaLocalArmazenamento;
	}

	public List<SelectItem> getListaSelectItemOpcaoConsulta() {
		if (listaSelectItemOpcaoConsulta == null) {
			listaSelectItemOpcaoConsulta = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsulta.add(new SelectItem("CODIGO_BARRA", "Código Barra"));
			listaSelectItemOpcaoConsulta.add(new SelectItem("PATRIMONIO", "Patrimônio"));
			listaSelectItemOpcaoConsulta.add(new SelectItem("LOCAL_ARMAZENAMENTO", "Local Armazenamento"));
		}
		return listaSelectItemOpcaoConsulta;
	}

	public void setListaSelectItemOpcaoConsulta(List<SelectItem> listaSelectItemOpcaoConsulta) {
		this.listaSelectItemOpcaoConsulta = listaSelectItemOpcaoConsulta;
	}
	
	public void montarListaSelectItemTextoPadraoPatrimonio() {
		try {
			List<TextoPadraoPatrimonioVO> textoPadraoPatrimonioVOs = getFacadeFactory().getTextoPadraoPatrimonioFacade().consultarPorSituacaoTipoUsoCombobox(StatusAtivoInativoEnum.ATIVO, getOcorrenciaPatrimonioVO().getTipoOcorrenciaPatrimonio().getTipoUsoTextoPadrao());
			setListaSelectItemTextoPadraoPatrimonio(UtilSelectItem.getListaSelectItem(textoPadraoPatrimonioVOs, "codigo", "nome", false));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getListaSelectItemTextoPadraoPatrimonio() {
		if (listaSelectItemTextoPadraoPatrimonio == null) {
			listaSelectItemTextoPadraoPatrimonio = new ArrayList<SelectItem>();
		}
		return listaSelectItemTextoPadraoPatrimonio;
	}

	public void setListaSelectItemTextoPadraoPatrimonio(List<SelectItem> listaSelectItemTextoPadraoPatrimonio) {
		this.listaSelectItemTextoPadraoPatrimonio = listaSelectItemTextoPadraoPatrimonio;
	}

	
	
	public List<SelectItem> getListaSelectItemTipoOcorrenciaPatrimonio() {
		if (listaSelectItemTipoOcorrenciaPatrimonio == null) {
			listaSelectItemTipoOcorrenciaPatrimonio = new ArrayList<SelectItem>(0);
			listaSelectItemTipoOcorrenciaPatrimonio.add(new SelectItem(null, ""));

//			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirCadastrarOcorrenciaReservaUnidade()) {
				listaSelectItemTipoOcorrenciaPatrimonio.add(new SelectItem(TipoOcorrenciaPatrimonioEnum.RESERVA_UNIDADE, TipoOcorrenciaPatrimonioEnum.RESERVA_UNIDADE.getValorApresentar()));
//			}
//			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirCadastrarOcorrenciaReservaLocal()) {
				listaSelectItemTipoOcorrenciaPatrimonio.add(new SelectItem(TipoOcorrenciaPatrimonioEnum.RESERVA_LOCAL, TipoOcorrenciaPatrimonioEnum.RESERVA_LOCAL.getValorApresentar()));
//			}
		}
		return listaSelectItemTipoOcorrenciaPatrimonio;
	}

	public void setListaSelectItemTipoOcorrenciaPatrimonio(List<SelectItem> listaSelectItemTipoOcorrenciaPatrimonio) {
		this.listaSelectItemTipoOcorrenciaPatrimonio = listaSelectItemTipoOcorrenciaPatrimonio;
	}

	public List<SelectItem> getListaSelectItemTipoPatrimonio() {
		if (listaSelectItemTipoPatrimonio == null) {
			listaSelectItemTipoPatrimonio = new ArrayList<SelectItem>(0);
			listaSelectItemTipoPatrimonio.add(new SelectItem(null, ""));
			List<TipoPatrimonioVO> listaTipoPatrimonio;
			try {
				listaTipoPatrimonio = getFacadeFactory().getTipoPatrimonioFacede().consultarPorDescricao("%%", false, getUsuarioLogado());
				listaSelectItemTipoPatrimonio.addAll(UtilSelectItem.getListaSelectItem(listaTipoPatrimonio, "codigo", "descricao", false));
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return listaSelectItemTipoPatrimonio;
	}

	public void setListaSelectItemTipoPatrimonio(List<SelectItem> listaSelectItemTipoPatrimonio) {
		this.listaSelectItemTipoPatrimonio = listaSelectItemTipoPatrimonio;
	}
	
	public void consultarLocalArmazenamento() {
		try {
			getControleConsultaLocalArmazenamento().setLimitePorPagina(10);
			getControleConsultaLocalArmazenamento().setListaConsulta(getFacadeFactory().getLocalArmazenamentoFacade().consultar(getConsultarLocalArmazenamentoPor(), getControleConsultaLocalArmazenamento().getValorConsulta(), false, getUsuarioLogado(), getUnidadeEnsinoLogado(), getControleConsultaLocalArmazenamento().getLimitePorPagina(), getControleConsultaLocalArmazenamento().getOffset(), false));
			getControleConsultaLocalArmazenamento().setTotalRegistrosEncontrados(getFacadeFactory().getLocalArmazenamentoFacade().consultarTotalRegistro(getConsultarLocalArmazenamentoPor(), getControleConsultaLocalArmazenamento().getValorConsulta(), getUnidadeEnsinoLogado(), false));
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public void realizarPaginacaoConsultaPatrimonioUnidade(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaPatrimonioUnidade().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaPatrimonioUnidade().setPage(dataScrollerEvent.getPage());
		consultarPatrimonioUnidade();
	}
	
	public TipoConsultaLocalArmazenamentoEnum getConsultarLocalArmazenamentoPor() {
		if (consultarLocalArmazenamentoPor == null) {
			consultarLocalArmazenamentoPor = TipoConsultaLocalArmazenamentoEnum.LOCAL;
		}
		return consultarLocalArmazenamentoPor;
	}

	public void setConsultarLocalArmazenamentoPor(TipoConsultaLocalArmazenamentoEnum consultarLocalArmazenamentoPor) {
		this.consultarLocalArmazenamentoPor = consultarLocalArmazenamentoPor;
	}


	
	public void consultarPatrimonioUnidade() {
		try {
			getControleConsultaPatrimonioUnidade().setLimitePorPagina(10);
			getControleConsultaPatrimonioUnidade().setListaConsulta(getFacadeFactory().getPatrimonioUnidadeFacade().consultar(getControleConsultaPatrimonioUnidade().getCampoConsulta(), getControleConsultaPatrimonioUnidade().getValorConsulta(), false, getUsuarioLogado(), getUnidadeEnsinoLogado(), getControleConsultaPatrimonioUnidade().getLimitePorPagina(), getControleConsultaPatrimonioUnidade().getOffset()));
			getControleConsultaPatrimonioUnidade().setTotalRegistrosEncontrados(getFacadeFactory().getPatrimonioUnidadeFacade().consultarTotalRegistro(getControleConsultaPatrimonioUnidade().getCampoConsulta(), getControleConsultaPatrimonioUnidade().getValorConsulta(), getUnidadeEnsinoLogado()));
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarPaginacaoConsultaLocalArmazenamento(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaLocalArmazenamento().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaLocalArmazenamento().setPage(dataScrollerEvent.getPage());
		consultarLocalArmazenamento();
	}
	
	public DataModelo getControleConsultaLocalArmazenamento() {
		if (controleConsultaLocalArmazenamento == null) {
			controleConsultaLocalArmazenamento = new DataModelo();
		}
		return controleConsultaLocalArmazenamento;
	}

	public void setControleConsultaLocalArmazenamento(DataModelo controleConsultaLocalArmazenamento) {
		this.controleConsultaLocalArmazenamento = controleConsultaLocalArmazenamento;
	}

	public DataModelo getControleConsultaPatrimonioUnidade() {
		if (controleConsultaPatrimonioUnidade == null) {
			controleConsultaPatrimonioUnidade = new DataModelo();
		}
		return controleConsultaPatrimonioUnidade;
	}

	public void setControleConsultaPatrimonioUnidade(DataModelo controleConsultaPatrimonioUnidade) {
		this.controleConsultaPatrimonioUnidade = controleConsultaPatrimonioUnidade;
	}

	public DataModelo getControleConsultaFuncionario() {
		if (controleConsultaFuncionario == null) {
			controleConsultaFuncionario = new DataModelo();
		}
		return controleConsultaFuncionario;
	}

	public void setControleConsultaFuncionario(DataModelo controleConsultaFuncionario) {
		this.controleConsultaFuncionario = controleConsultaFuncionario;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
			try {
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				listaSelectItemUnidadeEnsino = UtilSelectItem.getListaSelectItem(unidadeEnsinoVOs, "codigo", "nome");
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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
	
	public OcorrenciaPatrimonioVO getOcorrenciaPatrimonioSelecionadoExclusaoVO() {
		if(ocorrenciaPatrimonioSelecionadoExclusaoVO == null){
			ocorrenciaPatrimonioSelecionadoExclusaoVO = new OcorrenciaPatrimonioVO();
		}
		return ocorrenciaPatrimonioSelecionadoExclusaoVO;
	}

	public void setOcorrenciaPatrimonioSelecionadoExclusaoVO(OcorrenciaPatrimonioVO ocorrenciaPatrimonioSelecionadoExclusaoVO) {
		this.ocorrenciaPatrimonioSelecionadoExclusaoVO = ocorrenciaPatrimonioSelecionadoExclusaoVO;
	}
	
	public List<LocalArmazenamentoVO> getLocalArmazenamentoVOs() {
		if(localArmazenamentoVOs == null ){
			localArmazenamentoVOs = new ArrayList<LocalArmazenamentoVO>();
		}
		return localArmazenamentoVOs;
	}

	public void setLocalArmazenamentoVOs(List<LocalArmazenamentoVO> localArmazenamentoVOs) {
		this.localArmazenamentoVOs = localArmazenamentoVOs;
	}
	

	public OcorrenciaPatrimonioRelVO getOcorrenciaPatrimonioRelVO() {
		if(ocorrenciaPatrimonioRelVO == null){
			ocorrenciaPatrimonioRelVO = new OcorrenciaPatrimonioRelVO();
		}
		return ocorrenciaPatrimonioRelVO;
	}

	public void setOcorrenciaPatrimonioRelVO(OcorrenciaPatrimonioRelVO ocorrenciaPatrimonioRelVO) {
		this.ocorrenciaPatrimonioRelVO = ocorrenciaPatrimonioRelVO;
	}

	public void consultarSolicitanteEmprestimo() {
		try {
			getControleConsultaFuncionario().setLimitePorPagina(10);
			getControleConsultaFuncionario().setListaConsulta(getFacadeFactory().getFuncionarioFacade().consultar(getControleConsultaFuncionario().getValorConsulta(), getControleConsultaFuncionario().getCampoConsulta(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setMensagemID("msg_entre_dados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarLocalASerRervado() {
		getOcorrenciaPatrimonioVO().setLocalReservado((LocalArmazenamentoVO) getRequestMap().get("localAReservarVO"));
		
	}	

	public void inicializarDadosTipoOcorrencia() {
		try {
			if (Uteis.isAtributoPreenchido(getOcorrenciaPatrimonioVO().getPatrimonioUnidade())) {
				inicializarDadosPatrimonioUnidade(getOcorrenciaPatrimonioVO().getPatrimonioUnidade());
			}
			if (getOcorrenciaPatrimonioVO().getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.EMPRESTIMO)) {
				getOcorrenciaPatrimonioVO().setDataOcorrencia(new Date());
			}
		} catch (Exception e) {

		}
	}
	
	public void inicializarDadosPatrimonioUnidade(PatrimonioUnidadeVO patrimonioUnidadeVO) {
		try {
			getFacadeFactory().getOcorrenciaPatrimonioFacade().inicializarDadosPatrimonioUnidade(getOcorrenciaPatrimonioVO(), patrimonioUnidadeVO, getUsuarioLogado());
			setControleConsultaPatrimonioUnidade(null);
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluir() {
		try {
			setOcorrenciaPatrimonioSelecionadoExclusaoVO(getFacadeFactory().getOcorrenciaPatrimonioFacade().consultarPorChavePrimaria(getOcorrenciaPatrimonioSelecionadoExclusaoVO().getCodigo(), false, getUsuarioLogado()));
			getFacadeFactory().getOcorrenciaPatrimonioFacade().excluir(getOcorrenciaPatrimonioSelecionadoExclusaoVO(), true, getUsuarioLogado());
			carregarListaHorariosLocal();
			carregarOcorrenciasUnidadesPratrimonio();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public boolean getIsApresentarDadosReservaUnidade(){
		if(Uteis.isAtributoPreenchido(getOcorrenciaPatrimonioVO().getTipoOcorrenciaPatrimonio())){
			return getOcorrenciaPatrimonioVO().getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_UNIDADE) ? true : false;
		}else {
			return false;
		}
	}
	
	public boolean getIsApresentarDadosReservaLocal(){
		if(Uteis.isAtributoPreenchido(getOcorrenciaPatrimonioVO().getTipoOcorrenciaPatrimonio())){
			return getOcorrenciaPatrimonioVO().getTipoOcorrenciaPatrimonio().equals(TipoOcorrenciaPatrimonioEnum.RESERVA_LOCAL) ? true : false;
		}else {
			return false;
		}
	}
	
	public boolean getIsApresentarGrafico(){
		if(Uteis.isAtributoPreenchido(getLocalArmazenamentoVOs()) || Uteis.isAtributoPreenchido(getPatrimonioUnidadeVOs())){
			return true;
		}else {
			return false;
		}
	}
	
	public void selecionarSolicitanteEmprestimo() {
		getOcorrenciaPatrimonioVO().setSolicitanteEmprestimo((FuncionarioVO) getRequestMap().get("funcionarioVOitem"));
	}
	
	public void limparSolicitanteEmprestimo() {
		getOcorrenciaPatrimonioVO().setSolicitanteEmprestimo(null);
	}
	
	public void imprimirPDF() {
        try {
            String titulo = "Relatorio Gestão Reserva Patrimônio";
            String design = getFacadeFactory().getOcorrenciaPatrimonioFacade().designOcorrenciaReservaRelatorio();
            List listaObjetos = new ArrayList(0);
            
            getSuperParametroRelVO().setTituloRelatorio(titulo);
            getSuperParametroRelVO().setNomeDesignIreport(design);
            getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
            getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
            getSuperParametroRelVO().setListaObjetos(new ArrayList<OcorrenciaPatrimonioRelVO>(0));
            getSuperParametroRelVO().setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "administrativo" + File.separator);
            if(!getPatrimonioUnidadeVOs().isEmpty()){
            	getSuperParametroRelVO().adicionarParametro("listaUnidadePatrimonio", getPatrimonioUnidadeVOs());
            }else if(!getLocalArmazenamentoVOs().isEmpty()){
            	getSuperParametroRelVO().adicionarParametro("listaLocalReservado", getLocalArmazenamentoVOs());
            }
            getSuperParametroRelVO().adicionarParametro("totalRegistros", totalReserva);
            if(Uteis.isAtributoPreenchido(getUnidadeEnsino().getCodigo())){
            	getSuperParametroRelVO().adicionarParametro("filtroUnidadeEnsino", getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
            }else{
            	getSuperParametroRelVO().adicionarParametro("filtroUnidadeEnsino", "TODOS");
            }
            getSuperParametroRelVO().adicionarParametro("filtroDataIncial", getDataHoraInicialConsulta());
            getSuperParametroRelVO().adicionarParametro("filtroDataTermino", getDataHoraFinalConsulta());
            if(Uteis.isAtributoPreenchido(getOcorrenciaPatrimonioVO().getSolicitanteEmprestimo().getPessoa().getCodigo())){
            	getSuperParametroRelVO().adicionarParametro("filtroRequisitante", getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(getOcorrenciaPatrimonioVO().getSolicitanteEmprestimo().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
            }else{
            	getSuperParametroRelVO().adicionarParametro("filtroRequisitante", "TODOS");
            }
            if (Uteis.isAtributoPreenchido(getOcorrenciaPatrimonioVO().getTipoPatrimonioVO().getCodigo())) {
            	getSuperParametroRelVO().adicionarParametro("filtroTipoPatrimonio", getFacadeFactory().getTipoPatrimonioFacede().consultarPorChavePrimaria(getOcorrenciaPatrimonioVO().getTipoPatrimonioVO().getCodigo(), false, getUsuarioLogado()).getDescricao());
			}else{
            	getSuperParametroRelVO().adicionarParametro("filtroTipoPatrimonio", "TODOS");
			}
            if (Uteis.isAtributoPreenchido(getOcorrenciaPatrimonioVO().getLocalReservado().getCodigo())) {
            	getSuperParametroRelVO().adicionarParametro("filtroLocal", getFacadeFactory().getLocalArmazenamentoFacade().consultarPorCodigo(getOcorrenciaPatrimonioVO().getLocalReservado().getCodigo(), false, getUsuarioLogado()).getLocalArmazenamento());
			}else{
				getSuperParametroRelVO().adicionarParametro("filtroLocal", "TODOS");
			}
            
            realizarImpressaoRelatorio();
            getPatrimonioUnidadeVOs().clear();
            getLocalArmazenamentoVOs().clear();
		
            setMensagemID("msg_relatorio_ok");

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            removerObjetoMemoria(this);
        }

    }
	
	public boolean getIsApresentarBotaoImprimir(){
		if((getIsApresentarDadosReservaUnidade() || getIsApresentarDadosReservaLocal()) && (!getPatrimonioUnidadeVOs().isEmpty() || !getLocalArmazenamentoVOs().isEmpty()) && (Uteis.isAtributoPreenchido(getUnidadeEnsino()) && Uteis.isAtributoPreenchido(getOcorrenciaPatrimonioVO().getTipoOcorrenciaPatrimonio()))){
			return true;
		}else{
			getPatrimonioUnidadeVOs().clear();
			getLocalArmazenamentoVOs().clear();
			return false;
		}
	}
	
}
