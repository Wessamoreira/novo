package relatorio.controle.recursoshumanos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.FormaContratacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoRecebimentoEnum;
import negocio.comuns.arquitetura.faturamento.nfe.ConsistirException;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.CompetenciaFolhaPagamentoVO.EnumCampoConsultaCompetencia;
import negocio.comuns.recursoshumanos.CompetenciaPeriodoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.ContraChequeEventoRelVO;
import negocio.comuns.recursoshumanos.FichaFinanceiraRelVO;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("FichaFinanceiraRelControle")
@Scope("viewScope")
@Lazy
public class FichaFinanceiraRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 4659176644735909105L;
	
	private TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamento;
	private CompetenciaFolhaPagamentoVO competencia;
	private CompetenciaPeriodoFolhaPagamentoVO periodo;

	private Boolean exibirEventoZerados;

	private List<SelectItem> listaSelectItemFormacontratacao;
	private List<SelectItem> listaSelectItemRecebimento;
	private List<SelectItem> listaSelectItemSituacao;

	protected List<SelectItem> listaSelectItemCompetenciaPeriodo;

	public String[] formaContratacao = {};
	public String[] recebimento = {};
	public String[] situacao = {};

	private Boolean marcarTodosFormaContratacao;
	private Boolean marcarTodosRecebimento;
	private Boolean marcarTodosSituacao;

	private List<FuncionarioCargoVO> funcionarioCargoVOs;
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;

	private List<CompetenciaFolhaPagamentoVO> competenciaFolhaPagamentoVOs;
	private String campoConsultaCompetencia;
	private String valorConsultaCompetencia;

	/**
	 * Metodo que gera o relatorio pelos filtros informados.
	 */
	public void gerarRelatorio() {
		imprimirRelatorioFichaFinanceira();
	}

	/**
	 * Imprimir relatorio.
	 */
	@SuppressWarnings("unchecked")
	public void imprimirRelatorioFichaFinanceira() {
		String retornoTipoRelatorio = null;
		try {
			if (Uteis.isAtributoPreenchido(periodo.getCodigo().longValue())) {
				periodo = getFacadeFactory().getCompetenciaPeriodoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(periodo.getCodigo().longValue());
			}

			List<FichaFinanceiraRelVO> listaObjetos = getFacadeFactory().getContraChequeInterfaceFacade().montarDadosRelatorioFichaFinanceira(getTemplateLancamentoFolhaPagamento(), competencia, periodo);
			if (Uteis.isAtributoPreenchido(listaObjetos)) {
				getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "recursoshumanos" + File.separator + "RelatorioFichaFinanceira" + ".jrxml");
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "recursoshumanos" + File.separator);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("FOLHA ANALÍTICA"); 

				List<ContraChequeEventoRelVO> lista = getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarContraChequeEventos(getTemplateLancamentoFolhaPagamento(), competencia, periodo, new SecaoFolhaPagamentoVO());
				getSuperParametroRelVO().getParametros().put("listaContraChequeEventos", lista);
				
				List<ContraChequeEventoRelVO> listaSecao = getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarContraChequeEventosSecao(getTemplateLancamentoFolhaPagamento(), competencia, periodo);
				
				for (ContraChequeEventoRelVO contraChequeEventoRelVO : listaSecao) {
					contraChequeEventoRelVO.getSecaoFolhaPagamento().setCodigo(contraChequeEventoRelVO.getCodigo());
					List<ContraChequeEventoRelVO> listaContraChequeEventoSecao = getFacadeFactory().getContraChequeEventoInterfaceFacade().consultarContraChequeEventos(getTemplateLancamentoFolhaPagamento(), competencia, periodo, contraChequeEventoRelVO.getSecaoFolhaPagamento());
					
					contraChequeEventoRelVO.setListaEventosFolhaPagamento(listaContraChequeEventoSecao);
				}
				
				getSuperParametroRelVO().getParametros().put("listaContraChequeEventosSecao", listaSecao);
				
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio("relatorio" + File.separator + "designRelatorio" + File.separator + "recursoshumanos" + File.separator);
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setTipoRelatorio(retornoTipoRelatorio);
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			retornoTipoRelatorio = null;
		}
	}
 
	/**
	 * Consulta a competencia da folha de pagamento e monta os peridos de acordo
	 * com a competencia.
	 */
	public void consultarCompetenciaFolhaPagamentoPorDataCompetencia() {
		try {
			setCompetencia(getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarCompetenciaFolhaPagamentoPorMesAno(UteisData.getMesData(getCompetencia().getDataCompetencia()), UteisData.getAnoData(getCompetencia().getDataCompetencia()), false, getUsuarioLogado()));
			if (Uteis.isAtributoPreenchido(getCompetencia())) {
				montarCompetenciaPeriodo(getCompetencia());
			} else {
				setListaSelectItemCompetenciaPeriodo(new ArrayList<>());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void montarCompetenciaPeriodo(CompetenciaFolhaPagamentoVO competenciaFolhaPagamentoVO) {
		List<CompetenciaPeriodoFolhaPagamentoVO> periodos;
		try {
			periodos = getFacadeFactory().getCompetenciaPeriodoFolhaPagamentoInterfaceFacade().consultarPorCompetenciaFolhaPagamento(competenciaFolhaPagamentoVO);
			setListaSelectItemCompetenciaPeriodo(UtilSelectItem.getListaSelectItem(periodos, "codigo", "periodoApresentacao", true));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFuncionarioPorMatricula() {
		try {
			if (Uteis.isAtributoPreenchido(getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO().getMatriculaCargo())) {
				FuncionarioCargoVO funcionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(funcionarioCargo);

				setMensagemID("msg_entre_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosFuncionario () {
		getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(new FuncionarioCargoVO());
		setValorConsultaFuncionario("");
	}

	public void limparDadosCompetencia() {
		setCompetencia(new CompetenciaFolhaPagamentoVO());
		setListaSelectItemCompetenciaPeriodo(new ArrayList<SelectItem>());
		setValorConsultaCompetencia("");
	}
	
	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}
	
	/**
	 * Seleciona a competencia pesquisada.
	 */
	public void selecionarCompetencia() {
		CompetenciaFolhaPagamentoVO competencia = (CompetenciaFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("competenciaItem");
		setCompetencia(competencia);

		try {
			montarCompetenciaPeriodo(competencia);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", "msg_erro_LancamentoFolhaPagamento_competenciaAtivaNaoEncontrado");
		}
	}

	@SuppressWarnings("unchecked")
	public void consultarCompetencia() {
		try {

			if (getCampoConsultaCompetencia().equals(EnumCampoConsultaCompetencia.CODIGO.name())) {
				if (getValorConsultaCompetencia().trim().isEmpty() || !Uteis.getIsValorNumerico(getValorConsultaCompetencia())) {
					throw new ConsistirException(UteisJSF.internacionalizar("prt_SecaoFolhaPagamento_ConsultaCampoCodigoInvalido"));
				}
			}

			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			getControleConsultaOtimizado().setValorConsulta(getValorConsultaCompetencia());
			getControleConsultaOtimizado().setCampoConsulta(getCampoConsultaCompetencia());
			getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarPorEnumCampoConsulta(getControleConsultaOtimizado(), null);

			setCompetenciaFolhaPagamentoVOs((List<CompetenciaFolhaPagamentoVO>) getControleConsultaOtimizado().getListaConsulta());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Seleciona o funcionario cargo pesquisado.
	 */
	public void selecionarFuncionarioCargo() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioCargoItem");
		getTemplateLancamentoFolhaPagamento().setFuncionarioCargoVO(obj);
		
		valorConsultaFuncionario = "";
		campoConsultaFuncionario = "";
		getFuncionarioCargoVOs().clear();
	}

	/**
	 * Consulta responsavel por retornar os usuarios do popup de pesquisa 
	 * do funcionario
	 */
	public void consultarFuncionario() {
		try {
			List<FuncionarioCargoVO> objs = new ArrayList<FuncionarioCargoVO>(0);

			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				setListaConsulta(new ArrayList<FuncionarioCargoVO>(0));
				return;
			}
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeFuncionario(getControleConsultaOtimizado(), getValorConsultaFuncionario(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getValorConsultaFuncionario(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setFuncionarioCargoVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public boolean getApresentarResultadoConsultaFuncionarioCargo() {
		return getFuncionarioCargoVOs().size() > 0;
	}

	// GETTER AND SETTER
	public List<SelectItem> getListaSelectItemFormacontratacao() {
		if (listaSelectItemFormacontratacao == null || listaSelectItemFormacontratacao.isEmpty()) {
			listaSelectItemFormacontratacao = new ArrayList<>();
			try {
				for (FormaContratacaoFuncionarioEnum formaContratacaoFuncionarioEnum : FormaContratacaoFuncionarioEnum.values()) {
					listaSelectItemFormacontratacao.add(new SelectItem(formaContratacaoFuncionarioEnum, formaContratacaoFuncionarioEnum.getDescricao()));
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return listaSelectItemFormacontratacao;
	}

	public void setListaSelectItemFormacontratacao(List<SelectItem> listaSelectItemFormacontratacao) {
		this.listaSelectItemFormacontratacao = listaSelectItemFormacontratacao;
	}

	public List<SelectItem> getListaSelectItemRecebimento() {
		if (listaSelectItemRecebimento == null || listaSelectItemRecebimento.isEmpty()) {
			listaSelectItemRecebimento = new ArrayList<SelectItem>(0);
			try {
				for (TipoRecebimentoEnum tipoRecebimentoEnum : TipoRecebimentoEnum.values()) {
					listaSelectItemRecebimento
							.add(new SelectItem(tipoRecebimentoEnum, tipoRecebimentoEnum.getDescricao()));
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return listaSelectItemRecebimento;
	}

	public void setListaSelectItemRecebimento(List<SelectItem> listaSelectItemRecebimento) {
		this.listaSelectItemRecebimento = listaSelectItemRecebimento;
	}

	public List<SelectItem> getListaSelectItemSituacao() {
		if (listaSelectItemSituacao == null || listaSelectItemSituacao.isEmpty()) {
			listaSelectItemSituacao = new ArrayList<SelectItem>(0);
			try {
				for (SituacaoFuncionarioEnum situacaoFuncionarioEnum : SituacaoFuncionarioEnum.values()) {
					listaSelectItemSituacao.add(new SelectItem(situacaoFuncionarioEnum, situacaoFuncionarioEnum.getDescricao()));
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}
		return listaSelectItemSituacao;
	}
	
	public void selecionarTodosFormaContratacao() {
		String contratacao = "";
		if(getMarcarTodosFormaContratacao()) {
			for (FormaContratacaoFuncionarioEnum formaContratacaoFuncionarioEnum : FormaContratacaoFuncionarioEnum.values()) {
				contratacao += contratacao.concat(formaContratacaoFuncionarioEnum.toString()).concat(";");
			}
		}

		getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario(contratacao);
		formaContratacao = contratacao.split(";");
	}

	public void selecionarTodosRecebimento() {
		String tipoRecebimento = "";
		if(getMarcarTodosRecebimento()) {
			for (TipoRecebimentoEnum formaRecebimentoEnum : TipoRecebimentoEnum.values()) {
				tipoRecebimento += tipoRecebimento.concat(formaRecebimentoEnum.toString()).concat(";");
			}
		}

		getTemplateLancamentoFolhaPagamento().setTipoRecebimento(tipoRecebimento);
		recebimento = tipoRecebimento.split(";");
	}

	public void selecionarTodosSituacao() {
		String todasSituacoes = "";
		if(getMarcarTodosSituacao()) {
			for (SituacaoFuncionarioEnum situacaoFuncionarioEnum : SituacaoFuncionarioEnum.values()) {
				todasSituacoes += todasSituacoes.concat(situacaoFuncionarioEnum.toString()).concat(";");
			}
		}

		getTemplateLancamentoFolhaPagamento().setSituacaoFuncionario(todasSituacoes);
		situacao = todasSituacoes.split(";");
	}

	public void selecionarFormaContratacao() {
		if (formaContratacao.length == FormaContratacaoFuncionarioEnum.values().length) {
			setMarcarTodosFormaContratacao(true);
		} else {
			setMarcarTodosFormaContratacao(false);			
		}
		getTemplateLancamentoFolhaPagamento().setFormaContratacaoFuncionario(montarString(formaContratacao));
	}

	public void selecionarRecebimento() {
		if (recebimento.length == TipoRecebimentoEnum.values().length) {
			setMarcarTodosRecebimento(true);
		} else {
			setMarcarTodosRecebimento(false);			
		}
		getTemplateLancamentoFolhaPagamento().setTipoRecebimento(montarString(recebimento));
	}

	public void selecionarSituacao() {
		if (situacao.length == SituacaoFuncionarioEnum.values().length) {
			setMarcarTodosSituacao(true);
		} else {
			setMarcarTodosSituacao(false);			
		}
		getTemplateLancamentoFolhaPagamento().setSituacaoFuncionario(montarString(situacao));
	}

	public Boolean getMarcarTodosFormaContratacao() {
		if (marcarTodosFormaContratacao == null)
			marcarTodosFormaContratacao = false;
		return marcarTodosFormaContratacao;
	}

	public void setMarcarTodosFormaContratacao(Boolean marcarTodosFormaContratacao) {
		this.marcarTodosFormaContratacao = marcarTodosFormaContratacao;
	}

	public Boolean getMarcarTodosRecebimento() {
		if (marcarTodosRecebimento == null)
			marcarTodosRecebimento = false;
		return marcarTodosRecebimento;
	}

	public void setMarcarTodosRecebimento(Boolean marcarTodosRecebimento) {
		this.marcarTodosRecebimento = marcarTodosRecebimento;
	}

	public Boolean getMarcarTodosSituacao() {
		if (marcarTodosSituacao == null)
			marcarTodosSituacao = false;
		return marcarTodosSituacao;
	}

	public void setMarcarTodosSituacao(Boolean marcarTodosSituacao) {
		this.marcarTodosSituacao = marcarTodosSituacao;
	}

	public CompetenciaFolhaPagamentoVO getCompetencia() {
		if (competencia == null) {
			competencia = new CompetenciaFolhaPagamentoVO();
		}
		return competencia;
	}

	public void setCompetencia(CompetenciaFolhaPagamentoVO competencia) {
		this.competencia = competencia;
	}

	public Boolean getExibirEventoZerados() {
		if (exibirEventoZerados == null) {
			exibirEventoZerados = false;
		}
		return exibirEventoZerados;
	}

	public void setExibirEventoZerados(Boolean exibirEventoZerados) {
		this.exibirEventoZerados = exibirEventoZerados;
	}

	public String[] getFormaContratacao() {
		return formaContratacao;
	}

	public void setFormaContratacao(String[] formaContratacao) {
		this.formaContratacao = formaContratacao;
	}

	public String[] getRecebimento() {
		return recebimento;
	}

	public void setRecebimento(String[] recebimento) {
		this.recebimento = recebimento;
	}

	public String[] getSituacao() {
		return situacao;
	}

	public void setSituacao(String[] situacao) {
		this.situacao = situacao;
	}

	public void setListaSelectItemSituacao(List<SelectItem> listaSelectItemSituacao) {
		this.listaSelectItemSituacao = listaSelectItemSituacao;
	}

	public List<SelectItem> getListaSelectItemCompetenciaPeriodo() {
		if (listaSelectItemCompetenciaPeriodo == null) {
			listaSelectItemCompetenciaPeriodo = new ArrayList<>();
		}
		return listaSelectItemCompetenciaPeriodo;
	}

	public void setListaSelectItemCompetenciaPeriodo(List<SelectItem> listaSelectItemCompetenciaPeriodo) {
		this.listaSelectItemCompetenciaPeriodo = listaSelectItemCompetenciaPeriodo;
	}

	public CompetenciaPeriodoFolhaPagamentoVO getPeriodo() {
		if (periodo == null) {
			periodo = new CompetenciaPeriodoFolhaPagamentoVO();
		}
		return periodo;
	}

	public void setPeriodo(CompetenciaPeriodoFolhaPagamentoVO periodo) {
		this.periodo = periodo;
	}

	public List<FuncionarioCargoVO> getFuncionarioCargoVOs() {
		if (funcionarioCargoVOs == null) {
			funcionarioCargoVOs = new ArrayList<>();
		}
		return funcionarioCargoVOs;
	}

	public void setFuncionarioCargoVOs(List<FuncionarioCargoVO> funcionarioCargoVOs) {
		this.funcionarioCargoVOs = funcionarioCargoVOs;
	}

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public List<CompetenciaFolhaPagamentoVO> getCompetenciaFolhaPagamentoVOs() {
		if (competenciaFolhaPagamentoVOs == null) {
			competenciaFolhaPagamentoVOs = new ArrayList<>();
		}
		return competenciaFolhaPagamentoVOs;
	}

	public void setCompetenciaFolhaPagamentoVOs(List<CompetenciaFolhaPagamentoVO> competenciaFolhaPagamentoVOs) {
		this.competenciaFolhaPagamentoVOs = competenciaFolhaPagamentoVOs;
	}

	public String getCampoConsultaCompetencia() {
		if (campoConsultaCompetencia == null) {
			campoConsultaCompetencia = "";
		}
		return campoConsultaCompetencia;
	}

	public void setCampoConsultaCompetencia(String campoConsultaCompetencia) {
		this.campoConsultaCompetencia = campoConsultaCompetencia;
	}

	public String getValorConsultaCompetencia() {
		if (valorConsultaCompetencia == null) {
			valorConsultaCompetencia = "";
		}
		return valorConsultaCompetencia;
	}

	public void setValorConsultaCompetencia(String valorConsultaCompetencia) {
		this.valorConsultaCompetencia = valorConsultaCompetencia;
	}

	public TemplateLancamentoFolhaPagamentoVO getTemplateLancamentoFolhaPagamento() {
		if (templateLancamentoFolhaPagamento == null) {
			templateLancamentoFolhaPagamento = new TemplateLancamentoFolhaPagamentoVO();
		}
		return templateLancamentoFolhaPagamento;
	}

	public void setTemplateLancamentoFolhaPagamento(TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamento) {
		this.templateLancamentoFolhaPagamento = templateLancamentoFolhaPagamento;
	}
	
	private String montarString(String[] texto) { 
		String novoTexto = "";
		for (String string : texto) {
			if (texto.length <= 1) {				
				novoTexto += string;
			} else {
				novoTexto += string.concat(";");
			}
		}
		
		return novoTexto;
	}
}