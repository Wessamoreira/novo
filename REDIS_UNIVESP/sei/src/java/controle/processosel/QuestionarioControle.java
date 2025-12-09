package controle.processosel;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * questionarioForm.jsp questionarioCons.jsp) com as funcionalidades da classe <code>Questionario</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Questionario
 * @see QuestionarioVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.processosel.PerguntaQuestionarioVO;
import negocio.comuns.processosel.PerguntaVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.enumeradores.EscopoPerguntaEnum;
import negocio.comuns.processosel.enumeradores.TipoEscopoQuestionarioPerguntaEnum;
import negocio.comuns.processosel.enumeradores.TipoEscopoQuestionarioRequerimentoEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.processosel.RespostaPergunta;
import relatorio.controle.avaliacaoInst.QuestionarioRelControle;

@Controller("QuestionarioControle")
@Scope("viewScope")
@Lazy
public class QuestionarioControle extends QuestionarioRelControle implements Serializable {

	private static final long serialVersionUID = 8986695731379405942L;

	private QuestionarioVO questionarioVO;
	private PerguntaQuestionarioVO perguntaQuestionarioVO;
	protected List<SelectItem> listaSelectItemPergunta;
	private String valorConsultaPergunta;
	private String campoConsultaPergunta;
	private List<PerguntaVO> listaConsultaPergunta;
	private Boolean possibilidadeExcluirQuestionario;
	private String tipoEscopoBase;
	private List<SelectItem> listaSelectItemEscopoRequerimento;
	
	public QuestionarioControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
		String escopo = (String) ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("escopo");
		
		if (escopo != null && !escopo.trim().isEmpty()) {
			setTipoEscopoBase(escopo);
		}
		String idControlador = (String) ((HttpServletRequest) context().getExternalContext().getRequest()).getParameter("idControlador");
		if (idControlador != null && !idControlador.trim().isEmpty()) {
			setIdControlador(idControlador);
		}
	}
	
	public void inicializarEscopo(String escopo) {
    	liberarBackingBeanMemoria();

    	if (Uteis.isAtributoPreenchido(escopo)) {
    		setTipoEscopoBase(escopo);
    	}
    }

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Questionario</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {

		setQuestionarioVO(new QuestionarioVO());

		setPerguntaQuestionarioVO(new PerguntaQuestionarioVO());
		setCampoConsultaPergunta("");
		setValorConsultaPergunta("");
		setListaConsultaPergunta(new ArrayList<>(0));
		if (getTipoEscopoBase().equals("PS")) {
			getQuestionarioVO().setEscopo(TipoEscopoQuestionarioPerguntaEnum.PROCESSO_SELETIVO.getKey());
		}
		if (getTipoEscopoBase().equals("BC")) {
			getQuestionarioVO().setEscopo(TipoEscopoQuestionarioPerguntaEnum.BANCO_CURRICULUM.getKey());
		}
		if (getTipoEscopoBase().equals("RE")) {
			getQuestionarioVO().setEscopo(TipoEscopoQuestionarioPerguntaEnum.REQUERIMENTO.getKey());
		}
		if (getTipoEscopoBase().equals("AI")) {
			getQuestionarioVO().setEscopo(TipoEscopoQuestionarioPerguntaEnum.GERAL.getKey());
		}
		if (getTipoEscopoBase().equals("RQ")) {
			getQuestionarioVO().setEscopo(TipoEscopoQuestionarioPerguntaEnum.REQUISICAO.getKey());
		}
		if (getTipoEscopoBase().equals("PE")) {
			getQuestionarioVO().setEscopo(TipoEscopoQuestionarioPerguntaEnum.PLANO_ENSINO.getKey());
		}
		if (getTipoEscopoBase().equals("ES")) {
			getQuestionarioVO().setEscopo(TipoEscopoQuestionarioPerguntaEnum.ESTAGIO.getKey());
		}
		if (getTipoEscopoBase().equals("RF")) {
			getQuestionarioVO().setEscopo(TipoEscopoQuestionarioPerguntaEnum.RELATORIO_FACILITADOR.getKey());
		}
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("questionarioForm", getIdControlador()+"&escopo="+getTipoEscopoBase());
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Questionario</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		try {
			QuestionarioVO obj = (QuestionarioVO) context().getExternalContext().getRequestMap().get("questionarioItem");
			obj.setNovoObj(Boolean.FALSE);
			setQuestionarioVO(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setPossibilidadeExcluirQuestionario(!getFacadeFactory().getQuestionarioFacade().verificarQuestionarioVinculoAvaliacao(getQuestionarioVO().getCodigo(), getUsuarioLogado()));
			
			setPerguntaQuestionarioVO(new PerguntaQuestionarioVO());
			setCampoConsultaPergunta("");
			setValorConsultaPergunta("");
			setListaConsultaPergunta(new ArrayList<>(0));
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("questionarioForm", getIdControlador()+"&escopo="+getTipoEscopoBase());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("questionarioForm", getIdControlador()+"&escopo="+getTipoEscopoBase());
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>Questionario</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (questionarioVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getQuestionarioFacade().incluir(questionarioVO, true, getUsuarioLogado());
			} else {
				getFacadeFactory().getQuestionarioFacade().alterar(questionarioVO, true, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("questionarioForm", getIdControlador()+"&escopo="+getTipoEscopoBase());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("questionarioForm", getIdControlador()+"&escopo="+getTipoEscopoBase());
		}
	}

	public void clonar() {
		try {
			getQuestionarioVO().setCodigo(0);
			getQuestionarioVO().setNovoObj(Boolean.TRUE);
			getQuestionarioVO().setDescricao(getQuestionarioVO().getDescricao() + " - Clone");
			getQuestionarioVO().setSituacao("EC");
			for (PerguntaQuestionarioVO perguntaQuestionarioVOs : getQuestionarioVO().getPerguntaQuestionarioVOs()) {
				perguntaQuestionarioVOs.setCodigo(0);
				perguntaQuestionarioVOs.setQuestionario(0);
			}
			setMensagemID("msg_dados_clonados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * QuestionarioCons.jsp. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	@Override
	public String consultar() {
		try {
			super.consultar();
			List<QuestionarioVO> objs = new ArrayList<>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getQuestionarioFacade().consultarPorCodigo(new Integer(valorInt), getTipoEscopoBase(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("descricao")) {
				objs = getFacadeFactory().getQuestionarioFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), getTipoEscopoBase(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void montarRespostas() {
		PerguntaQuestionarioVO obj = (PerguntaQuestionarioVO) context().getExternalContext().getRequestMap().get("perguntaQuestionarioItem");
		questionarioVO.setRespostasVOs(obj.pergunta.getRespostaPerguntaVOs());
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>QuestionarioVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public void excluir() {
		try {
			switch (getTipoEscopoBase()) {
			case "AI":
				if (getFacadeFactory().getQuestionarioFacade().verificarQuestionarioVinculoAvaliacao(getQuestionarioVO().getCodigo(), getUsuarioLogado())) {
					throw new Exception("O questionário não pode ser excluído pois está vinculado à uma avaliação.");
				}
				break;
			default:
				break;
			}
			getFacadeFactory().getQuestionarioFacade().excluir(questionarioVO, true, getUsuarioLogado());
			setQuestionarioVO(new QuestionarioVO());

			setPerguntaQuestionarioVO(new PerguntaQuestionarioVO());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>PerguntaQuestionario</code> para o objeto
	 * <code>questionarioVO</code> da classe <code>Questionario</code>
	 */
	public void adicionarPerguntaQuestionario() throws Exception {
		try {
			if (!getQuestionarioVO().getCodigo().equals(0)) {
				perguntaQuestionarioVO.setQuestionario(getQuestionarioVO().getCodigo());
			}
			if (getPerguntaQuestionarioVO().getPergunta().getCodigo().intValue() != 0) {
				Integer campoConsulta = getPerguntaQuestionarioVO().getPergunta().getCodigo();
				PerguntaVO pergunta = getFacadeFactory().getPerguntaFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				getPerguntaQuestionarioVO().setPergunta(pergunta);
			}
			getQuestionarioVO().adicionarObjPerguntaQuestionarioVOs(getPerguntaQuestionarioVO());
			this.setPerguntaQuestionarioVO(new PerguntaQuestionarioVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirQuestionario() {
		try {
			imprimirPDF(getQuestionarioVO());			
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>PerguntaQuestionario</code> para edição pelo usuário.
	 */
	public void editarPerguntaQuestionario() throws Exception {
		PerguntaQuestionarioVO obj = (PerguntaQuestionarioVO) context().getExternalContext().getRequestMap().get("perguntaQuestionarioItem");
		setPerguntaQuestionarioVO(obj);
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>PerguntaQuestionario</code> do objeto <code>questionarioVO</code>
	 * da classe <code>Questionario</code>
	 */
	public void removerPerguntaQuestionario() throws Exception {
		PerguntaQuestionarioVO obj = (PerguntaQuestionarioVO) context().getExternalContext().getRequestMap().get("perguntaQuestionarioItem");
		getQuestionarioVO().excluirObjPerguntaQuestionarioVOs(obj.getPergunta().getCodigo());
		setMensagemID("msg_dados_excluidos");
	}

	public void consultarPergunta() {
		try {
			EscopoPerguntaEnum escopoBase = EscopoPerguntaEnum.getEnumCorrespondenteEscopoBase(getTipoEscopoBase());

			super.consultar();
			List<PerguntaVO> objs = new ArrayList<>(0);
			if (getCampoConsultaPergunta().equals("descricao")) {
				objs = getFacadeFactory().getPerguntaFacade().consultarPorDescricao(getValorConsultaPergunta(), escopoBase, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTARTODOS, getUsuarioLogado());
			}
			if (getCampoConsultaPergunta().equals("codigo")) {
				if (getValorConsultaPergunta().equals("")) {
					setValorConsultaPergunta("0");
			}
				int valorInt = Integer.parseInt(getValorConsultaPergunta());
				objs = getFacadeFactory().getPerguntaFacade().consultaPorCodigo(Integer.parseInt(getValorConsultaPergunta()), escopoBase, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTARTODOS, getUsuarioLogado());
			}						
						

			setListaConsultaPergunta(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarPergunta() {
		try {
			PerguntaVO obj = (PerguntaVO) context().getExternalContext().getRequestMap().get("perguntaItens");
			obj.setRespostaPerguntaVOs(RespostaPergunta.consultarRespostaPerguntas(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			obj.setListaPerguntaChecklistVO(getFacadeFactory().getPerguntaChecklistFacade().consultarPerguntaChecklistPorPerguntaVO(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone()));
			getPerguntaQuestionarioVO().setPergunta(obj);
			getQuestionarioVO().adicionarObjPerguntaQuestionarioVOs(getPerguntaQuestionarioVO());
			getFacadeFactory().getQuestionarioFacade().removerPerguntaListaQuestionario(obj.getCodigo(), getListaConsultaPergunta());
			setPerguntaQuestionarioVO(new PerguntaQuestionarioVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void realizarAtivacaoSituacaoAvaliacao() {
		try {
			if(getTipoEscopoBase().equals("PE") && !Uteis.isAtributoPreenchido(getQuestionarioVO().getPerguntaQuestionarioVOs())) {
				throw new Exception("Adicione ao menos uma pergunta ao formulário.");			
			}
			else {
				gravar();
			getFacadeFactory().getQuestionarioFacade().alterarSituacaoAvaliacao(getQuestionarioVO().getCodigo(), "AT", getUsuarioLogado());
			getQuestionarioVO().setSituacao("AT");
			setPossibilidadeExcluirQuestionario(!getFacadeFactory().getQuestionarioFacade().verificarQuestionarioVinculoAvaliacao(getQuestionarioVO().getCodigo(), getUsuarioLogado()));
			setMensagemID("msg_dados_ativado");
			}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void realizarFinalizacaoSituacaoAvaliacao() {
		try {
			getFacadeFactory().getQuestionarioFacade().alterarSituacaoAvaliacao(getQuestionarioVO().getCodigo(), "FI", getUsuarioLogado());
			getQuestionarioVO().setSituacao("FI");
			getQuestionarioVO().setDesabilitarAlterarEscopoQuestionarioRequerimento(true);
			setMensagemID("msg_dados_finalizado");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboPergunta() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void irPaginaInicial() throws Exception {
		// controleConsulta.setPaginaAtual(1);
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		// controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() -
		// 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		// controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() +
		// 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		// controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Situacao</code>.
	 */
	@SuppressWarnings("rawtypes")
	public List<SelectItem> getListaSelectItemSituacao() throws Exception {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);
		Hashtable situacao = (Hashtable) Dominios.getSituacaoQuestionario();
		Enumeration keys = situacao.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacao.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	public List<SelectItem> getListaSelectItemEscopoQuestionario() throws Exception {

		if (getTipoEscopoBase().equals("AI")) {
			return TipoEscopoQuestionarioPerguntaEnum.getListaSelectItemAvaliacaoInstitucional();
		}
		if (getTipoEscopoBase().equals("RE")) {
			return TipoEscopoQuestionarioPerguntaEnum.getListaSelectItemRequerimento();
		}
		if (getTipoEscopoBase().equals("BC")) {
			return TipoEscopoQuestionarioPerguntaEnum.getListaSelectItemBancoCurriculum();
		}
		if (getTipoEscopoBase().equals("PS")) {
			return TipoEscopoQuestionarioPerguntaEnum.getListaSelectItemProcessoSeletivo();
		}
		if (getTipoEscopoBase().equals("PE")) {
			return TipoEscopoQuestionarioPerguntaEnum.getListaSelectItemPlanoEnsino();
		}
		if (getTipoEscopoBase().equals("ES")) {
			return TipoEscopoQuestionarioPerguntaEnum.getListaSelectItemEstagio();
		}
		if (getTipoEscopoBase().equals("RQ")) {
			return TipoEscopoQuestionarioPerguntaEnum.getListaSelectItemRequisicao();
		}
		if (getTipoEscopoBase().equals("RF")) {
			return TipoEscopoQuestionarioPerguntaEnum.getListaSelectItemRelatorioFacilitaddor();
		}
		return new ArrayList<SelectItem>(0);
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		setListaConsulta(new ArrayList<>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("questionarioCons", getIdControlador()+"&escopo="+getTipoEscopoBase());
	}

	public List<SelectItem> getListaSelectItemPergunta() {
		if(listaSelectItemPergunta == null){
			listaSelectItemPergunta = new ArrayList<SelectItem>(0);
		}
		return (listaSelectItemPergunta);
	}

	public void setListaSelectItemPergunta(List<SelectItem> listaSelectItemPergunta) {
		this.listaSelectItemPergunta = listaSelectItemPergunta;
	}

	public PerguntaQuestionarioVO getPerguntaQuestionarioVO() {
		if(perguntaQuestionarioVO == null){
			perguntaQuestionarioVO = new PerguntaQuestionarioVO();
		}
		return perguntaQuestionarioVO;
	}

	public void setPerguntaQuestionarioVO(PerguntaQuestionarioVO perguntaQuestionarioVO) {
		this.perguntaQuestionarioVO = perguntaQuestionarioVO;
	}

	public QuestionarioVO getQuestionarioVO() {
		if(questionarioVO == null) {
			questionarioVO = new QuestionarioVO();
		}
		return questionarioVO;
	}

	public void setQuestionarioVO(QuestionarioVO questionarioVO) {
		this.questionarioVO = questionarioVO;
	}

	/**
	 * @return the valorConsultaPergunta
	 */
	public String getValorConsultaPergunta() {
		if(valorConsultaPergunta == null){
			valorConsultaPergunta = "";
		}
		return valorConsultaPergunta;
	}

	/**
	 * @param valorConsultaPergunta
	 *            the valorConsultaPergunta to set
	 */
	public void setValorConsultaPergunta(String valorConsultaPergunta) {
		this.valorConsultaPergunta = valorConsultaPergunta;
	}

	/**
	 * @return the campoConsultaPergunta
	 */
	public String getCampoConsultaPergunta() {
		if(campoConsultaPergunta == null){
			campoConsultaPergunta = "";
		}
		return campoConsultaPergunta;
	}

	/**
	 * @param campoConsultaPergunta
	 *            the campoConsultaPergunta to set
	 */
	public void setCampoConsultaPergunta(String campoConsultaPergunta) {
		this.campoConsultaPergunta = campoConsultaPergunta;
	}

	/**
	 * @return the listaConsultaPergunta
	 */
	public List<PerguntaVO> getListaConsultaPergunta() {
		if(listaConsultaPergunta == null){
			listaConsultaPergunta = new ArrayList<>(0);
		}
		return listaConsultaPergunta;
	}

	/**
	 * @param listaConsultaPergunta
	 *            the listaConsultaPergunta to set
	 */
	public void setListaConsultaPergunta(List<PerguntaVO> listaConsultaPergunta) {
		this.listaConsultaPergunta = listaConsultaPergunta;
	}

	public Boolean getApresentarBotaoAtivar() {
		if(getTipoEscopoBase().equals("PE")) {
			return (getQuestionarioVO().getSituacao().equals("EC") || getQuestionarioVO().getSituacao().equals("FI")) && !getQuestionarioVO().getNovoObj();
		}
		else {
			return getQuestionarioVO().getSituacao().equals("EC") && !getQuestionarioVO().getNovoObj();
		}
	}

	public Boolean getApresentarBotaoFinalizar() {
		return getQuestionarioVO().getSituacao().equals("AT") && !getQuestionarioVO().getNovoObj();
	}

	public Boolean getPossibilidadeExcluirQuestionario() {
		if (possibilidadeExcluirQuestionario == null) {
			possibilidadeExcluirQuestionario = Boolean.FALSE;
		}
		return possibilidadeExcluirQuestionario;
	}

	public void setPossibilidadeExcluirQuestionario(Boolean possibilidadeExcluirQuestionario) {
		this.possibilidadeExcluirQuestionario = possibilidadeExcluirQuestionario;
	}

	public Boolean getApresentarBotaoImprimir() {
		return !getQuestionarioVO().getCodigo().equals(0);
	}

	public void subirOpcaoPergunta() {
		try {
			PerguntaQuestionarioVO opc1 = (PerguntaQuestionarioVO) context().getExternalContext().getRequestMap().get("perguntaQuestionarioItem");
			if (opc1.getOrdem() > 1) {
				PerguntaQuestionarioVO opc2 = getQuestionarioVO().getPerguntaQuestionarioVOs().get(opc1.getOrdem() - 2);
				getFacadeFactory().getQuestionarioFacade().alterarOrdemPergunta(getQuestionarioVO(), opc1, opc2);
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void descerOpcaoPergunta() {
		try {
			PerguntaQuestionarioVO opc1 = (PerguntaQuestionarioVO) context().getExternalContext().getRequestMap().get("perguntaQuestionarioItem");
			if (getQuestionarioVO().getPerguntaQuestionarioVOs().size() >= opc1.getOrdem()) {
				PerguntaQuestionarioVO opc2 = getQuestionarioVO().getPerguntaQuestionarioVOs().get(opc1.getOrdem());
				getFacadeFactory().getQuestionarioFacade().alterarOrdemPergunta(getQuestionarioVO(), opc1, opc2);
			}
			limparMensagem();

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String getTipoEscopoBase() {
		if (tipoEscopoBase == null) {
			tipoEscopoBase = "AI";
		}
		return tipoEscopoBase;
	}

	public void setTipoEscopoBase(String tipoEscopoBase) {
		this.tipoEscopoBase = tipoEscopoBase;
	}

	public String getTituloPagina() {
		if (getTipoEscopoBase().equals("AI")) {
			return "Questionário - Avaliação Institucional";
		}
		if (getTipoEscopoBase().equals("RE")) {
			return "Questionário - Requerimento";
		}
		if (getTipoEscopoBase().equals("BC")) {
			return "Questionário - Banco Curriculum";
		}
		if (getTipoEscopoBase().equals("PS")) {
			return "Questionário - Processo Seletivo";
		}
		if (getTipoEscopoBase().equals("RQ")) {
			return "Questionário - Requisição";
		}
		if (getTipoEscopoBase().equals("PE")) {
			return "Formulário Plano de Ensino";
		}
		if (getTipoEscopoBase().equals("ES")) {
			return "Formulário Estágio";
		}
		if (getTipoEscopoBase().equals("RF")) {
			return "Questionário Relatório Facilitador";
		}

		return "Questionário";
	}

	public List<SelectItem> getListaSelectItemEscopoRequerimento() {
		if (listaSelectItemEscopoRequerimento == null) {
			listaSelectItemEscopoRequerimento = new ArrayList<SelectItem>(0);
			listaSelectItemEscopoRequerimento.add(new SelectItem(TipoEscopoQuestionarioRequerimentoEnum.REQUERENTE, TipoEscopoQuestionarioRequerimentoEnum.REQUERENTE.getValorApresentar()));
			listaSelectItemEscopoRequerimento.add(new SelectItem(TipoEscopoQuestionarioRequerimentoEnum.DEPARTAMENTO, TipoEscopoQuestionarioRequerimentoEnum.DEPARTAMENTO.getValorApresentar()));
		}
		return listaSelectItemEscopoRequerimento;
	}
	
	 public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
	        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
	        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
	        consultar();
	    }

	public void setListaSelectItemEscopoRequerimento(List<SelectItem> listaSelectItemEscopoRequerimento) {
		this.listaSelectItemEscopoRequerimento = listaSelectItemEscopoRequerimento;
	}
	
	public void limparFiltroConsulta() {
		getControleConsulta().setValorConsulta("");
	}
	
	public void confirmarAtivacaoFormulario() {
		if (getTipoEscopoBase().equals("PE")) {
			setOncompleteModal("RichFaces.$('confirmarAtivacaoFormulario').show()");
		}
		else {
			realizarAtivacaoSituacaoAvaliacao();
			setOncompleteModal("");
		}
	}

	public Boolean getApresentarBotaoVoltarFormularioParaConstrucao() {
		return !getQuestionarioVO().getSituacao().equals("EC") && getTipoEscopoBase().equals("PE") && !getQuestionarioVO().getNovoObj();
}
	
	public void voltarFormularioParaConstrucao() {
		try {
			if(!getFacadeFactory().getQuestionarioFacade().verificarQuestionarioVinculoPlanoEnsino(getQuestionarioVO().getCodigo(), getUsuarioLogado())) {
				getFacadeFactory().getQuestionarioFacade().alterarSituacaoAvaliacao(getQuestionarioVO().getCodigo(), "EC", getUsuarioLogado());
				getQuestionarioVO().setSituacao("EC");
				setMensagemID("msg_dados_emConstrucao");
			}
			else {
				throw new Exception("Não é possivel voltar para construção, pois este formulário já está sendo usado em um plano de ensino.");	
			}
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}

}
