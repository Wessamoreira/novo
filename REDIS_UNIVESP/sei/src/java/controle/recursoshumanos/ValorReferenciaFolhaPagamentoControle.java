package controle.recursoshumanos;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.apache.commons.lang.SerializationUtils;
import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.administrativo.CategoriaGEDVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.recursoshumanos.FaixaValorVO;
import negocio.comuns.recursoshumanos.ValorReferenciaFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoValorReferenciaEnum;
import negocio.comuns.recursoshumanos.enumeradores.ValorFixoEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 * Classe responsavel por implementar a interacao entre os componentes JSF das
 * paginas valorReferenciaFolhaPagamentoForm.xhtml e valorReferenciaFolhaPagamentoCons.xhtl com as
 * funcionalidades da classe <code>ValorReferenciaFolhaPagamentoVO</code>.
 * Implemtacao da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see CategoriaGEDVO
 */
@Controller("ValorReferenciaFolhaPagamentoControle")
@Scope("viewScope")
@Lazy
public class ValorReferenciaFolhaPagamentoControle extends SuperControle {

	private static final long serialVersionUID = 1455114507405935993L;

	public ValorReferenciaFolhaPagamentoControle() {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}
	
	private static final String TELA_CONS = "valorReferenciaFolhaPagamentoCons";
	private static final String TELA_FORM = "valorReferenciaFolhaPagamentoForm";
	private static final String CONTEXT_PARA_EDICAO = "tabelaReferenciaFP";

	private ValorReferenciaFolhaPagamentoVO valorReferenciaFolhaPagamento;
	private FaixaValorVO faixaValor;
	
	private List<SelectItem> listaSelectItemImposto;
	private List<SelectItem> listaSelectItemReferencia;
	private List<SelectItem> listaSelectTipoValorReferencia;
	private List<FaixaValorVO> listaAnteriorFaixaValores;

	private boolean valorReferenciaInativa = false;
	
	private Date valorInicioVigencia;
	private Date valorFinalVigencia;

	private String situacao;
	private String referencia;

	/**
	 * Rotina responsavel por disponibilizar um novo objeto da classe
	 * <code>CategoriaGED</code> para edicao pelo usuario da aplicacao.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setValorReferenciaFolhaPagamento(new ValorReferenciaFolhaPagamentoVO());
		setMensagemID("msg_entre_dados");
		setValorReferenciaInativa(valorReferenciaFolhaPagamento.getSituacao().equals(SituacaoEnum.INATIVO));
		getValorReferenciaFolhaPagamento().setTipoValorReferencia(TipoValorReferenciaEnum.FAIXA_VALOR);
		
		montarInicioFimVigenciaPelaDataCompentencia();
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	/**
	 * Rotina responsavel por disponibilizar os dados de um objeto da classe
	 * <code>ValorReferenciaFolhaPagamentoVO</code> para alteracao. O objeto desta classe e
	 * disponibilizado na session da pagina (request) para que o JSP correspondente
	 * possa disponibiliza-lo para edicao.
	 */
	@SuppressWarnings("unchecked")
	public String editar() {
		ValorReferenciaFolhaPagamentoVO obj = (ValorReferenciaFolhaPagamentoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
		obj.setListaFaixaValores(getFacadeFactory().getFaixaValorInterfaceFacade().consultarPorTabelaReferenciaFolhaPagamento(obj.getCodigo(), Boolean.TRUE, getUsuarioLogado()));

		obj.setNovoObj(Boolean.FALSE);
		setValorReferenciaFolhaPagamento(obj);
		for (FaixaValorVO object : obj.getListaFaixaValores()) {
			getListaAnteriorFaixaValores().add(object);
		}

		if (!Uteis.isAtributoPreenchido(getValorReferenciaFolhaPagamento().getTipoValorReferencia())) {
			getValorReferenciaFolhaPagamento().setTipoValorReferencia(TipoValorReferenciaEnum.FAIXA_VALOR);
		}

		//Ordena a lista pelo limite inferior
		obj.getListaFaixaValores().sort((o1, o2) -> o1.getLimiteInferior().compareTo(o2.getLimiteInferior()));
		valorReferenciaFolhaPagamento.setListaFaixaValores(obj.getListaFaixaValores());
		
		setValorReferenciaInativa(valorReferenciaFolhaPagamento.getSituacao().equals(SituacaoEnum.INATIVO));
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	/**
	 * Rotina responsavel por organizar a paginacao entre as paginas resultantes
	 * de uma consulta.
	 */
	@SuppressWarnings("rawtypes")
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		getControleConsulta().setValorConsulta("");
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	/**
	 * Rotina responsavel por gravar no BD os dados editados de um novo objeto da
	 * classe <code>TabelaReferenciaFolhaPagamento</code>. Caso o objeto seja novo (ainda nao gravado
	 * no BD) e acionado a operacao <code>incluir()</code>. Caso contrario e
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistencia o objeto
	 * nao e gravado, sendo re-apresentado para o usuario juntamente com uma
	 * mensagem de erro.
	 */
	public String persistir() {
		try {
			getValorReferenciaFolhaPagamento().setValorFixo(valorReferenciaFolhaPagamento.getTipoValorReferencia().equals(TipoValorReferenciaEnum.FIXO) ? true : false);
			getFacadeFactory().getValorReferenciaFolhaPagamentoInterfaceFacade().persistir(valorReferenciaFolhaPagamento, listaAnteriorFaixaValores, Boolean.TRUE, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
		}
	}

	/**
	 * Operacao responsavel por processar a exclusao um objeto da classe
	 * <code>TabelaReferenciaFolhaPagamento</code> Apos a exclusao ela automaticamente aciona a
	 * rotina para uma nova inclusao.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getValorReferenciaFolhaPagamentoInterfaceFacade().excluir(valorReferenciaFolhaPagamento, Boolean.TRUE, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis do
	 * valorReferenciaFolhaPagamentoCons.xhtml. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List<ValorReferenciaFolhaPagamentoVO> objs = new ArrayList<>(0);

			if (getControleConsulta().getCampoConsulta().equals("referencia")) {
				getControleConsulta().setValorConsulta(getReferencia());
			}

			objs = getFacadeFactory().getValorReferenciaFolhaPagamentoInterfaceFacade().consultarPorFiltro(
					getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getSituacao(), getValorInicioVigencia(),
					getValorFinalVigencia(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());

			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TipoDocumentoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}
	
	public void montarInicioFimVigenciaPelaDataCompentencia() {
		try {
			Date dataCompetencia = getFacadeFactory().getCompetenciaFolhaPagamentoInterfaceFacade().consultarDataCompetenciaAtiva();
			dataCompetencia = UteisData.getPrimeiroDataMes(dataCompetencia);
			getValorReferenciaFolhaPagamento().setDataInicioVigencia(dataCompetencia);

			getValorReferenciaFolhaPagamento().setDataFimVigencia( UteisData.getUltimaDataMes(dataCompetencia));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void encerrarVigencia() {
		try {
			if (Uteis.isAtributoPreenchido(valorReferenciaFolhaPagamento.getCodigo())) {
				valorReferenciaFolhaPagamento.setAtualizarFinalVigencia(true);
				alterarDataFimVigencia();
				valorReferenciaFolhaPagamento.setSituacao(SituacaoEnum.INATIVO);
				getFacadeFactory().getValorReferenciaFolhaPagamentoInterfaceFacade().alterarSituacaoDataFinalVigencia(valorReferenciaFolhaPagamento, true, getUsuarioLogado());
				setValorReferenciaInativa(valorReferenciaFolhaPagamento.getSituacao().equals(SituacaoEnum.INATIVO));
				setMensagemID("msg_dados_vigencia_encerrada");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void clonarVigencia() {
		try {
			getFacadeFactory().getValorReferenciaFolhaPagamentoInterfaceFacade().clonarVigencia(valorReferenciaFolhaPagamento, true, getUsuarioLogado());
			setMensagemID("msg_dados_vigencia_clonada");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void alterarDataFimVigencia() {
		Date dataAtual = new Date();
		if (valorReferenciaFolhaPagamento.getAtualizarFinalVigencia()) {
			valorReferenciaFolhaPagamento.setDataFimVigencia(new Date());
		} else {
			valorReferenciaFolhaPagamento.setDataFimVigencia(dataAtual);
		}
	}
	
	public void adicionar() {
		try {
			validarDadosFaixaValor();
			validarDadosLimiteInferiorDuplicado();

			if (getFaixaValor().getItemEmEdicao()) {
				Iterator<FaixaValorVO> i = getValorReferenciaFolhaPagamento().getListaFaixaValores().iterator();
				int index = 0;
				int aux = -1;
				FaixaValorVO objAux = new FaixaValorVO();
				while(i.hasNext()) {
					FaixaValorVO objExistente = i.next();
					
					if ( (objExistente.getCodigo().equals(getFaixaValor().getCodigo()) || objExistente.getLimiteInferior().equals(getFaixaValor().getLimiteInferior()))
							&& objExistente.getItemEmEdicao()){
						getFaixaValor().setItemEmEdicao(false);
				       	aux = index;
				       	objAux = getFaixaValor();
		 			}
		            index++;
				}
	
				if(aux >= 0) {
					getValorReferenciaFolhaPagamento().getListaFaixaValores().set(aux, objAux);
				}
	
			} else {
				getValorReferenciaFolhaPagamento().getListaFaixaValores().add(getFaixaValor());
			}
			faixaValor = new FaixaValorVO();
			//Ordena a lista pelo limite inferior
			getValorReferenciaFolhaPagamento().getListaFaixaValores().sort((obj1, obj2) -> obj1.getLimiteInferior().compareTo(obj2.getLimiteInferior()));
			limparDadosFaixaValor();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	private void validarDadosLimiteInferiorDuplicado() throws Exception {
		Iterator<FaixaValorVO> teste = getValorReferenciaFolhaPagamento().getListaFaixaValores().iterator();
		while(teste.hasNext()) {
			FaixaValorVO objExistente = (FaixaValorVO) teste.next();
			if (objExistente.getLimiteInferior().equals(getFaixaValor().getLimiteInferior())) {
				throw new Exception("Já existe uma Faixa de Valor cadastrada com o mesmo valor de limite inferior.");
			}
		}
	}
	
	public String editarFaixaValor()  {
        try {
        	FaixaValorVO obj =(FaixaValorVO) context().getExternalContext().getRequestMap().get("faixaValores");
        	obj.setItemEmEdicao(true);
        	//Clona o objeto da grid que sera editado para criar outra referencia de memoria
        	setFaixaValor((FaixaValorVO) SerializationUtils.clone(obj));       	
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "";
    }
	
	public void cancelarFaixaValor() {
		setFaixaValor(new FaixaValorVO());
	}

	/**
	 * Rotina responsavel por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificador", "Identificador"));
		itens.add(new SelectItem("referencia", "Referência"));
		return itens;
	}

	/**
	 * Rotina responsavel por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaComboSituacao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("ATIVO", "Ativo"));
		itens.add(new SelectItem("INATIVO", "Inativo"));
		itens.add(new SelectItem("TODOS", "Todos"));
		return itens;
	}
	
	/**
	 * Rotina responsavel por preencher a combo de consulta das referencias .
	 */
	public List<SelectItem> getTipoConsultaComboReferencia() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens = ValorFixoEnum.getValorFixoEnum();
		return itens;
	}

	public boolean getApresantarEncerrarVigencia() {
		if (!Uteis.isAtributoPreenchido(getValorReferenciaFolhaPagamento().getCodigo())) {
			return false;
		}
		return !getValorReferenciaInativa();
	}

	/**
	 * Rotina responsavel por realizar a paginacao da pagina de valorReferenciaFolhaPagamentoCons.xhtml
	 * 
	 * @param DataScrollEvent
	 * @throws Exception
	 */
	public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }

	public void validarDadosFaixaValor() throws Exception {
		try {
			getFacadeFactory().getFaixaValorInterfaceFacade().validarDadosLimiteInferiorMaiorLimiteSuperior(this.faixaValor);
		} catch (Exception e) {
			throw e;
		}
	}

	@SuppressWarnings("rawtypes")
	public void remover(FaixaValorVO obj) {
		setMensagemDetalhada("");
		Iterator iterator = valorReferenciaFolhaPagamento.getListaFaixaValores().iterator();
		while(iterator.hasNext()) {
			FaixaValorVO faixaValorVO = (FaixaValorVO) iterator.next();
			if (faixaValorVO.getCodigo().equals(obj.getCodigo()) && faixaValorVO.getLimiteSuperior().equals(obj.getLimiteSuperior())) {
				iterator.remove();
			}
		}
	}

	private void limparDadosFaixaValor() {
		this.setFaixaValor(new FaixaValorVO());
		setMensagem("");
		setMensagemID("");
		setMensagemDetalhada("");
		setValorReferenciaInativa(valorReferenciaFolhaPagamento.getSituacao().equals(SituacaoEnum.INATIVO));
	}
	
	public void limparValorConsulta() {
		getControleConsulta().setValorConsulta("");
	}

	/**
	 * Operacao que libera todos os recursos (atributos, listas, objetos) do backing
	 * bean. Garantindo uma melhor atuacao do Garbage Coletor do Java. A mesma e
	 * automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		valorReferenciaFolhaPagamento = null;
		setListaAnteriorFaixaValores(null);
	}
	
	public List<SelectItem> getListaSelectItemReferencia() {
		try {
			if (listaSelectItemReferencia == null) {
				listaSelectItemReferencia = new ArrayList<>(0);
				List<SelectItem> valoresFixos = ValorFixoEnum.getValorFixoEnum();
				for (SelectItem selectItem : valoresFixos) {
					listaSelectItemReferencia.add(selectItem);
				}
			}
			return listaSelectItemReferencia;
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return new ArrayList<>(0);
	}

	public void setListaSelectItemReferencia(List<SelectItem> listaSelectItemReferencia) {
		this.listaSelectItemReferencia = listaSelectItemReferencia;
	}

	public List<SelectItem> getListaSelectItemImposto() {
		try {
			if (listaSelectItemImposto == null) {
				listaSelectItemImposto = new ArrayList<>(0);
				List<ImpostoVO> impostos = getFacadeFactory().getImpostoFacade().consultarImpostoComboBox(false, getUsuarioLogado());
				listaSelectItemImposto.add(new SelectItem(0, ""));
				for (ImpostoVO imposto : impostos) {
					listaSelectItemImposto.add(new SelectItem(imposto.getCodigo(), imposto.getNome()));
				}
			}
			return listaSelectItemImposto;
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return new ArrayList<SelectItem>(0);
	}

	public void setListaSelectItemImposto(List<SelectItem> listaSelectItemImposto) {
		this.listaSelectItemImposto = listaSelectItemImposto;
	}
	
	public List<SelectItem> getListaSelectTipoValorReferencia() {
		if (!Uteis.isAtributoPreenchido(listaSelectTipoValorReferencia)) {
			listaSelectTipoValorReferencia = new ArrayList<>(0);
			listaSelectTipoValorReferencia.addAll(TipoValorReferenciaEnum.getTipoValorReferenciaEnum());
		}
		return listaSelectTipoValorReferencia;
	}

	public void setListaSelectTipoValorReferencia(List<SelectItem> listaSelectTipoValorReferencia) {
		this.listaSelectTipoValorReferencia = listaSelectTipoValorReferencia;
	}

	public List<FaixaValorVO> getListaAnteriorFaixaValores() {
		if (listaAnteriorFaixaValores == null) {
			listaAnteriorFaixaValores = new ArrayList<>();
		}
		return listaAnteriorFaixaValores;
	}

	public void setListaAnteriorFaixaValores(List<FaixaValorVO> listaAnteriorFaixaValores) {
		this.listaAnteriorFaixaValores = listaAnteriorFaixaValores;
	}

	public ValorReferenciaFolhaPagamentoVO getValorReferenciaFolhaPagamento() {
		if (valorReferenciaFolhaPagamento == null) {
			valorReferenciaFolhaPagamento = new ValorReferenciaFolhaPagamentoVO();
		}
		return valorReferenciaFolhaPagamento;
	}

	public void setValorReferenciaFolhaPagamento(ValorReferenciaFolhaPagamentoVO valorReferenciaFolhaPagamento) {
		this.valorReferenciaFolhaPagamento = valorReferenciaFolhaPagamento;
	}

	public FaixaValorVO getFaixaValor() {
		if (faixaValor == null) {
			faixaValor = new FaixaValorVO();
		}
		return faixaValor;
	}

	public void setFaixaValor(FaixaValorVO faixaValor) {
		this.faixaValor = faixaValor;
	}

	public boolean getValorReferenciaInativa() {
		return valorReferenciaInativa;
	}

	public void setValorReferenciaInativa(boolean valorReferenciaInativa) {
		this.valorReferenciaInativa = valorReferenciaInativa;
	}

	public Date getValorInicioVigencia() {
		return valorInicioVigencia;
	}

	public void setValorInicioVigencia(Date valorInicioVigencia) {
		this.valorInicioVigencia = valorInicioVigencia;
	}

	public Date getValorFinalVigencia() {
		return valorFinalVigencia;
	}

	public void setValorFinalVigencia(Date valorFinalVigencia) {
		this.valorFinalVigencia = valorFinalVigencia;
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

	public String getReferencia() {
		if (referencia == null) {
			referencia = "";
		}
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}
}
