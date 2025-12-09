package controle.recursoshumanos;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;
import javax.script.ScriptEngine;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.google.common.base.Preconditions;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.arquitetura.enumeradores.AtivoInativoEnum;
import negocio.comuns.recursoshumanos.FormulaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.IncidenciaFolhaPagamentoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

@Controller("FormulaFolhaPagamentoControle")
@Scope("viewScope")
@Lazy
public class FormulaFolhaPagamentoControle extends SuperControle {

	private static final long serialVersionUID = 1455114507405935993L;
	
	private static final String MENSAGEM_RETORNO_FORMULA = "Aguardando Execução da Fórmula";

	private FormulaFolhaPagamentoVO formulaFolhaPagamentoVO;
	private FuncionarioCargoVO funcionarioVO;

	private String retornoFuncao;
	private String retornoFuncaoFormatado;

	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	private List<FuncionarioCargoVO> listaConsultaFuncionario;

	private String valorConsultaSituacao;
	
	private String valorFiltro;
	
	@PostConstruct
	public void init() {
		setControleConsulta(new ControleConsulta());
        
        FormulaFolhaPagamentoVO formulaFolhaPagamento = (FormulaFolhaPagamentoVO) context().getExternalContext().getSessionMap().get("formulaFolhaPagamentoChamadoDaTelaDeEventoVO");
		
		try {
			if (Uteis.isAtributoPreenchido(formulaFolhaPagamento)) {
				getControleConsultaOtimizado().setNivelMontarDados(Uteis.NIVELMONTARDADOS_DADOSBASICOS);
				formulaFolhaPagamento = getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(formulaFolhaPagamento.getCodigo());
				editar(formulaFolhaPagamento);
			} else {
				getFormulaFolhaPagamentoVO().setSituacao(AtivoInativoEnum.ATIVO);
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("eventoFolhaPagamentoDoContraCheque");
		}
		
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	public FormulaFolhaPagamentoControle() {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
		setRetornoFuncao(MENSAGEM_RETORNO_FORMULA);
		getFormulaFolhaPagamentoVO().setResultadoLog(MENSAGEM_RETORNO_FORMULA);
	}

	public String novo() {
		this.setFuncionarioVO(new FuncionarioCargoVO());
		setFormulaFolhaPagamentoVO(new FormulaFolhaPagamentoVO());
		getFormulaFolhaPagamentoVO().setSituacao(AtivoInativoEnum.ATIVO);
		setMensagemID("msg_entre_dados");
		setRetornoFuncao(MENSAGEM_RETORNO_FORMULA);
		getFormulaFolhaPagamentoVO().setResultadoLog(MENSAGEM_RETORNO_FORMULA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("formulaFolhaPagamentoForm");
	}

	public String editar() {
		FormulaFolhaPagamentoVO obj = (FormulaFolhaPagamentoVO) context().getExternalContext().getRequestMap().get("incidenciaFP");
		return editar(obj);
	}

	public String editar(FormulaFolhaPagamentoVO obj) {
		obj.setNovoObj(Boolean.FALSE);
		setFormulaFolhaPagamentoVO(obj);
		setMensagemID("msg_dados_editar");
		setRetornoFuncao(MENSAGEM_RETORNO_FORMULA);
		getFormulaFolhaPagamentoVO().setResultadoLog(MENSAGEM_RETORNO_FORMULA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("formulaFolhaPagamentoForm");
	}

	public String inicializarConsultar() {
		this.setFuncionarioVO(new FuncionarioCargoVO());
		setListaConsulta(new ArrayList<IncidenciaFolhaPagamentoVO>(0));
		setMensagemID("msg_entre_prmconsulta");
		getControleConsulta().setValorConsulta("");
		return Uteis.getCaminhoRedirecionamentoNavegacao("formulaFolhaPagamentoCons");
	}

	public String gravar() {
		try {
			getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().persistir(formulaFolhaPagamentoVO,
					Boolean.TRUE, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}

	public String excluir() {
		try {
			getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().excluir(formulaFolhaPagamentoVO, Boolean.TRUE,
					getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("formulaFolhaPagamentoForm");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("formulaFolhaPagamentoForm");
		}
	}

	public String consultar() {
		try {
			super.consultar();
			List<FormulaFolhaPagamentoVO> objs = new ArrayList<>(0);

			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta());
			}

			objs = getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().consultarPorFiltro(
					getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), true,
					Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getValorConsultaSituacao());

			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
	}
	
	/**
	 * Altera o status da situacao para Inativo.
	 * 
	 * @return
	 */
	public String inativar() {
		try {
			getFormulaFolhaPagamentoVO().setSituacao(AtivoInativoEnum.INATIVO);
			getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().inativar(getFormulaFolhaPagamentoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_inativado");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}

	/**
	 * Altera o status da situacao para ativo.
	 * 
	 * @return
	 */
	public String ativar() {
		try {
			getFormulaFolhaPagamentoVO().setSituacao(AtivoInativoEnum.ATIVO);
			getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().inativar(getFormulaFolhaPagamentoVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_inativado");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificador", "Identificador"));
		return itens;
	}

	/**
	 * Consulta paginada da formula.
	 * 
	 * @param dataScrollEvent
	 */
	public void scrollerListener(DataScrollEvent dataScrollEvent) {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollEvent.getPage());
		consultar();
	}

	public List<SelectItem> getTipoConsultaComboFuncionarios() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem("nome", "Nome"));

		return itens;
	}

	public void executarFormula() {
		try {			
			Preconditions.checkState(Uteis.isAtributoPreenchido(getFormulaFolhaPagamentoVO().getFormula()), "É necessário informar uma Fórmula");
			Preconditions.checkState(getFuncionarioVO().getCodigo() != 0, "É necessário informar um Funcionário!!!");
			
			ScriptEngine engine = getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().inicializaEngineFormula();

			Object valor = getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().executarFormula(this.getFormulaFolhaPagamentoVO(), getFuncionarioVO(), getUsuarioLogado(), engine);
			if (Uteis.isAtributoPreenchido(valor) || valor != null) {
				this.setRetornoFuncao(valor.toString());
				this.setRetornoFuncaoFormatado(getFormulaFolhaPagamentoVO().getResultadoLog());
			} else {
				this.setRetornoFuncao("Retorno vazio da fórmula.");
			}

			getFormulaFolhaPagamentoVO().setResultadoLog(getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().formatarLog(getFormulaFolhaPagamentoVO().getResultadoLog()));
			
			setMensagemID("msg_entre_prmconsulta");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), "Houve um erro ao processar a Fórmula.");
			this.setRetornoFuncao(MENSAGEM_RETORNO_FORMULA);
			getFormulaFolhaPagamentoVO().setResultadoLog(e.getMessage());
		}
	}

	public void imprimir() {
		try {
			Preconditions.checkState(Uteis.isAtributoPreenchido(getFormulaFolhaPagamentoVO().getResultadoLog()), "Não existe log para ser impresso!");
			Preconditions.checkState(getFormulaFolhaPagamentoVO().getResultadoLog() != MENSAGEM_RETORNO_FORMULA, "Não existe log para ser impresso!");
			String resultadoLog = getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().formatarLog(getRetornoFuncaoFormatado());
			getFacadeFactory().getArquivoHelper().downloadArquivoTXT(resultadoLog, "LogFormula");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarFuncionario() {
		FuncionarioCargoVO funcionario = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
		this.setFuncionarioVO(funcionario);
		this.listaConsultaFuncionario.clear();
		this.valorConsultaFuncionario = null;
		this.campoConsultaFuncionario = null;
	}

	public void consultarFuncionario() {
		try {

			if (this.getCampoConsultaFuncionario().equals("nome")) {
				if (getValorConsultaFuncionario().length() < 3) {
					throw new Exception(UteisJSF.internacionalizar("msg_erro_informaDadosParaPesquisar"));
				}
				this.setListaConsultaFuncionario(getFacadeFactory().getFuncionarioCargoFacade()
						.consultarPorNomeFuncionario(getControleConsultaOtimizado(), this.getValorConsultaFuncionario(),
								Uteis.NIVELMONTARDADOS_COMBOBOX, this.getUsuarioLogado()));
			}

			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			this.setListaConsultaFuncionario(new ArrayList<>());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public boolean apresentarMapasGerais() { 
		return getValorFiltro().equals("TODOS") || getValorFiltro().equals("MAPAS_GERAIS") ? true : false;
	}

	public boolean apresentarMapasFolhaPagamento() { 
		return getValorFiltro().equals("TODOS") || getValorFiltro().equals("MAPAS_FOLHA_NORMAL") ? true : false;
	}

	public boolean apresentarMapasFerias() { 
		return getValorFiltro().equals("TODOS") || getValorFiltro().equals("MAPAS_FERIAS") ? true : false;
	}

	public boolean apresentarMapasDecimoTerceiro() { 
		return getValorFiltro().equals("TODOS") || getValorFiltro().equals("MAPAS_DECIMO_TERCEIRO") ? true : false;
	}

	public void consultarFuncionarioPelaMatriculaCargo() {

		try {
			setFuncionarioVO(getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getFuncionarioVO().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		} catch (Exception e) {
			setFuncionarioVO(new FuncionarioCargoVO());
			e.printStackTrace();
		}
	}
	
	public void limparFuncionario() {
		setFuncionarioVO(new FuncionarioCargoVO());
	}
	
	public FormulaFolhaPagamentoVO getFormulaFolhaPagamentoVO() {
		this.formulaFolhaPagamentoVO = Optional.ofNullable(this.formulaFolhaPagamentoVO)
				.orElse(new FormulaFolhaPagamentoVO());
		return this.formulaFolhaPagamentoVO;
	}

	public void setFormulaFolhaPagamentoVO(FormulaFolhaPagamentoVO formulaFolhaPagamentoVO) {
		this.formulaFolhaPagamentoVO = formulaFolhaPagamentoVO;
	}

	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaPessoa) {
		this.campoConsultaFuncionario = campoConsultaPessoa;
	}

	public String getValorConsultaFuncionario() {
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaPessoa) {
		this.valorConsultaFuncionario = valorConsultaPessoa;
	}

	public List<FuncionarioCargoVO> getListaConsultaFuncionario() {
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioCargoVO> listaConsultaPessoa) {
		this.listaConsultaFuncionario = listaConsultaPessoa;
	}

	public FuncionarioCargoVO getFuncionarioVO() {
		this.funcionarioVO = Optional.ofNullable(this.funcionarioVO).orElse(new FuncionarioCargoVO());
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioCargoVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public String getRetornoFuncao() {
		return retornoFuncao;
	}

	public void setRetornoFuncao(String retornoFuncao) {
		this.retornoFuncao = retornoFuncao;
	}

	public String getValorConsultaSituacao() {
		if (valorConsultaSituacao == null) {
			valorConsultaSituacao = "";
		}
		return valorConsultaSituacao;
	}

	public void setValorConsultaSituacao(String valorConsultaSituacao) {
		this.valorConsultaSituacao = valorConsultaSituacao;
	}

	public String getRetornoFuncaoFormatado() {
		if (retornoFuncaoFormatado == null) {
			retornoFuncaoFormatado = "";
		}
		return retornoFuncaoFormatado;
	}

	public void setRetornoFuncaoFormatado(String retornoFuncaoFormatado) {
		this.retornoFuncaoFormatado = retornoFuncaoFormatado;
	}

	public String getValorFiltro() {
		if (valorFiltro == null) {
			valorFiltro = "TODOS";
		}
		return valorFiltro;
	}

	public void setValorFiltro(String valorFiltro) {
		this.valorFiltro = valorFiltro;
	}
}