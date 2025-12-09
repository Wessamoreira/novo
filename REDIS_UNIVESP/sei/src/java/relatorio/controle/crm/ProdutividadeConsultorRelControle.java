package relatorio.controle.crm;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.crm.ProdutividadeConsultorRelVO;
import relatorio.negocio.jdbc.crm.ProdutividadeConsultorRel;

/**
 *
 * @author Pedro Andrade
 */
@Controller("ProdutividadeConsultorRelControle")
@Scope("viewScope")
@Lazy
public class ProdutividadeConsultorRelControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4304444750714876700L;
	private List<ProdutividadeConsultorRelVO> listaProdutividadeConsultor;
	private ProdutividadeConsultorRelVO produtividadeConsultorFiltro;

	private String campoConsultaCampanha;
	private String valorConsultaCampanha;
	private List listaConsultaCampanha;

	private List listaConsultaPessoa;
	private String campoConsultaPessoa;
	private String valorConsultaPessoa;

	public ProdutividadeConsultorRelControle() {
		consultarUnidadeEnsino();
	}

	public void imprimirPDF() {
		String titulo = null;
		String design = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ProdutividadeConsultorRelControle", "Inicializando Geração de Relatório Produtividade Consultor", "Emitindo Relatório");
			if (getProdutividadeConsultorFiltro().isTipoRelatorioSintetico()) {
				titulo = "Produtividade Consultor Sintético";
			} else {
				titulo = "Produtividade Consultor Analítico";
			}
			design = ProdutividadeConsultorRel.getDesignIReportRelatorio(getProdutividadeConsultorFiltro());
			setListaProdutividadeConsultor(getFacadeFactory().getProdutividadeConsultorRelFacade().criarObjeto(getProdutividadeConsultorFiltro(), getUsuarioLogado()));
			if (!getListaProdutividadeConsultor().isEmpty()) {
				getSuperParametroRelVO().setTituloRelatorio(titulo);
				getSuperParametroRelVO().setNomeDesignIreport(design);
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(ProdutividadeConsultorRel.caminhoBaseRelatorio());
				getSuperParametroRelVO().setCaminhoBaseRelatorio(ProdutividadeConsultorRel.caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setListaObjetos(getListaProdutividadeConsultor());
				if(getProdutividadeConsultorFiltro().getListaCursoVO().stream().anyMatch(CursoVO::getFiltrarCursoVO)){
					getSuperParametroRelVO().adicionarParametro("cursoFiltro", getProdutividadeConsultorFiltro().getCursoApresentar());
				}else{
					getSuperParametroRelVO().adicionarParametro("cursoFiltro", "Todos");
				}
				if(Uteis.isAtributoPreenchido(getProdutividadeConsultorFiltro().getCampanhaVO())){
					getSuperParametroRelVO().adicionarParametro("campanha", getProdutividadeConsultorFiltro().getCampanhaVO().getDescricao());
				}
				if(Uteis.isAtributoPreenchido(getProdutividadeConsultorFiltro().getConsultor())){
					getSuperParametroRelVO().adicionarParametro("consultor", getProdutividadeConsultorFiltro().getConsultor().getNome());
				}
				getSuperParametroRelVO().adicionarParametro("unidadesEnsino", getProdutividadeConsultorFiltro().getUnidadeEnsinoApresentar());
				getSuperParametroRelVO().adicionarParametro("periodo", UteisData.getPeriodoPorExtenso(getProdutividadeConsultorFiltro().getDataInicio(), getProdutividadeConsultorFiltro().getDataFim()));
				getSuperParametroRelVO().adicionarParametro("situacao", getProdutividadeConsultorFiltro().getSituacao());
				getSuperParametroRelVO().adicionarParametro("tipoRelatorio", getProdutividadeConsultorFiltro().getTipoRelatorio());
				setMensagemID("msg_relatorio_ok");
				realizarImpressaoRelatorio();
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "ProdutividadeConsultorRelControle", "Finalizando Geração de Relatório Produtividade Consultor", "Emitindo Relatório");

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			titulo = null;
			design = null;
		}
	}

	public void consultarUnidadeEnsino() {
		try {
			getProdutividadeConsultorFiltro().getListaUnidadeEnsinoVO().clear();
			if (getUnidadeEnsinoLogado().getCodigo() > 0) {
				getProdutividadeConsultorFiltro().setListaUnidadeEnsinoVO((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioNomeEntidadePermissao("", "", Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado())));
				for (UnidadeEnsinoVO obj : getProdutividadeConsultorFiltro().getListaUnidadeEnsinoVO()) {
					obj.setFiltrarUnidadeEnsino(true);
				}
			} else {
				getProdutividadeConsultorFiltro().setListaUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoFaltandoLista(getProdutividadeConsultorFiltro().getListaUnidadeEnsinoVO(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void marcarTodasUnidadesEnsinoAction() {
		getProdutividadeConsultorFiltro().getListaUnidadeEnsinoVO().forEach(ue -> ue.setFiltrarUnidadeEnsino(getMarcarTodasUnidadeEnsino()));
		verificarTodasUnidadesSelecionadas();
	}

	public void verificarTodasUnidadesSelecionadas() {
		try {
			getProdutividadeConsultorFiltro().setUnidadeEnsinoApresentar(getProdutividadeConsultorFiltro().getListaUnidadeEnsinoVO()
					.stream().filter(UnidadeEnsinoVO::getFiltrarUnidadeEnsino).map(UnidadeEnsinoVO::getNome).collect(Collectors.joining("; ")));
			getProdutividadeConsultorFiltro().setCursoApresentar("");
			getProdutividadeConsultorFiltro().setListaCursoVO(getFacadeFactory().getCursoFacade().consultarCursoPorNomePeriodicidadeEUnidadeEnsinoVOs("", "", null, getProdutividadeConsultorFiltro().getListaUnidadeEnsinoVO(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void marcarTodosCursosAction() throws Exception {
		for (CursoVO cursoVO : getProdutividadeConsultorFiltro().getListaCursoVO()) {
			cursoVO.setFiltrarCursoVO(getMarcarTodosCursos());
		}
		verificarTodosCursosSelecionados();
	}

	public void verificarTodosCursosSelecionados() {
		getProdutividadeConsultorFiltro().setCursoApresentar(getProdutividadeConsultorFiltro().getListaCursoVO().stream()
				.filter(CursoVO::getFiltrarCursoVO).map(CursoVO::getNome).collect(Collectors.joining("; ")));
	}

	public void consultarCampanha() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCampanha().equals("descricao")) {
				objs = getFacadeFactory().getCampanhaFacade().consultarPorDescricao(getValorConsultaCampanha(), "", getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaCampanha(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCampanha() {
		CampanhaVO obj = (CampanhaVO) context().getExternalContext().getRequestMap().get("campanhaItens");
		try {
			getProdutividadeConsultorFiltro().setCampanhaVO(obj);
			setListaConsultaCampanha(new ArrayList<>());
			setValorConsultaCampanha("");
			setMensagemID("", "");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			obj = null;
		}

	}

	public void consultarPessoa() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaPessoa().equals("nome")) {
				if (getValorConsultaPessoa().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaPessoa(), TipoPessoa.FUNCIONARIO.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
			}
			if (getCampoConsultaPessoa().equals("cpf")) {
				if (getValorConsultaPessoa().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaPessoa(), TipoPessoa.FUNCIONARIO.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
			}
			setListaConsultaPessoa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarPessoa() {
		PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("pessoaItens");
		try {
			getProdutividadeConsultorFiltro().setConsultor(obj);
			setListaConsultaPessoa(new ArrayList<>());
			setValorConsultaPessoa("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			obj = null;
		}
	}

	public Boolean getApresentarCampoCpf() {
		return getCampoConsultaPessoa().equals("cpf") ? true : false;
	}

	public void limparCampoCurso() {
		try {
			getProdutividadeConsultorFiltro().getListaCursoVO().forEach(c -> c.setFiltrarCursoVO(false));
			verificarTodosCursosSelecionados();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCampoCampanha() {
		try {
			getProdutividadeConsultorFiltro().setCampanhaVO(new CampanhaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCampoConsultor() {
		try {
			getProdutividadeConsultorFiltro().setConsultor(new PessoaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List getTipoConsultaComboSituacao() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("todas", "Todas"));
		itens.add(new SelectItem("realizado", "Realizado"));
		itens.add(new SelectItem("naoRealizado", "Não Realizado"));
		itens.add(new SelectItem("reagendado", "Reagendado"));
		return itens;
	}

	public List getTipoConsultaComboCampanha() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		return itens;
	}

	public List getTipoConsultaComboPessoa() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public List getTipoConsultaComboTipoRelatorio() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("sintetico", "Sintetico"));
		itens.add(new SelectItem("analitico", "Analitico"));
		return itens;
	}

	public List<ProdutividadeConsultorRelVO> getListaProdutividadeConsultor() {
		if (listaProdutividadeConsultor == null) {
			listaProdutividadeConsultor = new ArrayList<ProdutividadeConsultorRelVO>();
		}
		return listaProdutividadeConsultor;
	}

	public void setListaProdutividadeConsultor(List<ProdutividadeConsultorRelVO> listaProdutividadeConsultor) {
		this.listaProdutividadeConsultor = listaProdutividadeConsultor;
	}

	public List getListaConsultaPessoa() {
		if (listaConsultaPessoa == null) {
			listaConsultaPessoa = new ArrayList<PessoaVO>();
		}
		return listaConsultaPessoa;
	}

	public void setListaConsultaPessoa(List listaConsultaPessoa) {
		this.listaConsultaPessoa = listaConsultaPessoa;
	}

	public String getCampoConsultaPessoa() {
		if (campoConsultaPessoa == null) {
			campoConsultaPessoa = "";
		}
		return campoConsultaPessoa;
	}

	public void setCampoConsultaPessoa(String campoConsultaPessoa) {
		this.campoConsultaPessoa = campoConsultaPessoa;
	}

	public String getValorConsultaPessoa() {
		if (valorConsultaPessoa == null) {
			valorConsultaPessoa = "";
		}
		return valorConsultaPessoa;
	}

	public void setValorConsultaPessoa(String valorConsultaPessoa) {
		this.valorConsultaPessoa = valorConsultaPessoa;
	}

	public String getCampoConsultaCampanha() {
		if (campoConsultaCampanha == null) {
			campoConsultaCampanha = "";
		}
		return campoConsultaCampanha;
	}

	public void setCampoConsultaCampanha(String campoConsultaCampanha) {
		this.campoConsultaCampanha = campoConsultaCampanha;
	}

	public String getValorConsultaCampanha() {
		if (valorConsultaCampanha == null) {
			valorConsultaCampanha = "";
		}
		return valorConsultaCampanha;
	}

	public void setValorConsultaCampanha(String valorConsultaCampanha) {
		this.valorConsultaCampanha = valorConsultaCampanha;
	}

	public List getListaConsultaCampanha() {
		if (listaConsultaCampanha == null) {
			listaConsultaCampanha = new ArrayList<CampanhaVO>();
		}
		return listaConsultaCampanha;
	}

	public void setListaConsultaCampanha(List listaConsultaCampanha) {
		this.listaConsultaCampanha = listaConsultaCampanha;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}

	public ProdutividadeConsultorRelVO getProdutividadeConsultorFiltro() {
		if (produtividadeConsultorFiltro == null) {
			produtividadeConsultorFiltro = new ProdutividadeConsultorRelVO();
		}
		return produtividadeConsultorFiltro;
	}

	public void setProdutividadeConsultorFiltro(ProdutividadeConsultorRelVO produtividadeConsultorFiltro) {
		this.produtividadeConsultorFiltro = produtividadeConsultorFiltro;
	}

}
