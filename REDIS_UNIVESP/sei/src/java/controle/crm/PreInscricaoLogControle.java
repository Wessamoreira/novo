package controle.crm;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.processosel.PreInscricaoLogVO;
import negocio.comuns.processosel.enumeradores.SituacaoLogPreInscricaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

/**
 *
 * @author Kennedy
 */
@Controller("PreInscricaoLogControle")
@Scope("viewScope")
@Lazy
public class PreInscricaoLogControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date dataInicioFiltro;
	private Date dataFimFiltro;
	private List listaSelectItemUnidadeEnsino;
	private Boolean consultaDataScroller;
	private PreInscricaoLogVO preInscricaoLogVO;
	private SituacaoLogPreInscricaoEnum situacaoLogPreInscricao;
	private HashMap<Integer, PreInscricaoLogVO> mapPreInscricaoLogVOs;
	private Boolean realizarConsultaSemLimitOffSet;

	public PreInscricaoLogControle() throws Exception {
		montarListaSelectItemUnidadeEnsino("");
	}

	public String consultar() {
		try {
			List objs = new ArrayList(0);
			if (!getConsultaDataScroller()) {
				getMapPreInscricaoLogVOs().clear();
			}
			super.consultar();
			getControleConsultaOtimizado().setLimitePorPagina(10);
			if (getControleConsulta().getCampoConsulta().equals("unidadeensino")) {
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPreInscricaoLogFacade().consultarPorUnidadeEnsino(new Integer(valorInt), dataInicioFiltro, dataFimFiltro, true, Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getSituacaoLogPreInscricao(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPreInscricaoLogFacade().consultarTotalRegistrosPorUnidadeEnsino(new Integer(valorInt), dataInicioFiltro, dataFimFiltro, true, getSituacaoLogPreInscricao(), getUsuarioLogado()));
			} else if (getControleConsulta().getCampoConsulta().equals("cursoCodigo")) {
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPreInscricaoLogFacade().consultarPorCodigoCurso(new Integer(valorInt), dataInicioFiltro, dataFimFiltro, true, Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getSituacaoLogPreInscricao(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPreInscricaoLogFacade().consultarTotalRegistrosPorCodigoCurso(new Integer(valorInt), dataInicioFiltro, dataFimFiltro, true, getSituacaoLogPreInscricao(), getUsuarioLogado()));
			} else if (getControleConsulta().getCampoConsulta().equals("cursoNome")) {
				objs = getFacadeFactory().getPreInscricaoLogFacade().consultarPorNomeCurso(getControleConsulta().getValorConsulta(), dataInicioFiltro, dataFimFiltro, true, Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getSituacaoLogPreInscricao(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPreInscricaoLogFacade().consultarTotalRegistrosNomeCurso(getControleConsulta().getValorConsulta(), dataInicioFiltro, dataFimFiltro, true, getSituacaoLogPreInscricao(), getUsuarioLogado()));
			} else if (getControleConsulta().getCampoConsulta().equals("email")) {
				objs = getFacadeFactory().getPreInscricaoLogFacade().consultarPorEmail(getControleConsulta().getValorConsulta(), dataInicioFiltro, dataFimFiltro, true, Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getSituacaoLogPreInscricao(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPreInscricaoLogFacade().consultarTotalRegistrosPorEmail(getControleConsulta().getValorConsulta(), dataInicioFiltro, dataFimFiltro, true, getSituacaoLogPreInscricao(), getUsuarioLogado()));
			} else if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getPreInscricaoLogFacade().consultarPorNomeProspect(getControleConsulta().getValorConsulta(), dataInicioFiltro, dataFimFiltro, true, Uteis.NIVELMONTARDADOS_TODOS, getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getSituacaoLogPreInscricao(), getUsuarioLogado());
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPreInscricaoLogFacade().consultarTotalRegistrosPorNomeProspect(getControleConsulta().getValorConsulta(), dataInicioFiltro, dataFimFiltro, true, getSituacaoLogPreInscricao(), getUsuarioLogado()));
			} 
			getControleConsultaOtimizado().setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("preInscricaoLogCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("preInscricaoLogCons.xhtml");
		}
	}
	
	public String editar() {
	    try {
		PreInscricaoLogVO obj = (PreInscricaoLogVO) context().getExternalContext().getRequestMap().get("item");
		obj.setNovoObj(Boolean.FALSE);
		setPreInscricaoLogVO(obj);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("preInscricaoLogForm.xhtml");
	    } catch (Exception e) {
		e.printStackTrace();
		return Uteis.getCaminhoRedirecionamentoNavegacao("preInscricaoLogCons.xhtml");
	    }
	}
	
	public void irPaginaInicial() throws Exception {
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	public Date getDataInicioFiltro() {
		if (dataInicioFiltro == null) {
			dataInicioFiltro = new Date();
		}
		return dataInicioFiltro;
	}

	public void setDataInicioFiltro(Date dataInicioFiltro) {
		this.dataInicioFiltro = dataInicioFiltro;
	}

	public Date getDataFimFiltro() {
		if (dataFimFiltro == null) {
			dataFimFiltro = new Date();
		}
		return dataFimFiltro;
	}

	public void setDataFimFiltro(Date dataFimFiltro) {
		this.dataFimFiltro = dataFimFiltro;
	}

	private void validarDados() throws ConsistirException {
		if (Uteis.isAtributoPreenchido(getDataInicioFiltro())) {
			throw new ConsistirException("O campo DATA INICIO deve ser informado.");
		}
		if (Uteis.isAtributoPreenchido(getDataFimFiltro())) {
			throw new ConsistirException("O campo DATA FIM deve ser informado.");
		}
	}

	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome Prospect"));
		itens.add(new SelectItem("unidadeensino", "Unidade de Ensino"));
		itens.add(new SelectItem("cursoNome", "Nome Cruso"));
		itens.add(new SelectItem("cursoCodigo", "Codigo Cruso"));
		itens.add(new SelectItem("email", "Email Prospect"));
		return itens;
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				objs.add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				removerObjetoMemoria(obj);
			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		realizarVerificacaoLogSelecionadoComFalhaGerarPreInscricao();
		setConsultaDataScroller(true);
		consultar();
		
		for (PreInscricaoLogVO obj : (List<PreInscricaoLogVO>) getControleConsultaOtimizado().getListaConsulta()) {
			if (getMapPreInscricaoLogVOs().containsKey(obj.getCodigo())) {
				obj.setSelecionado(true);
				break;
			} 
		}
	}

	public Boolean getConsultaDataScroller() {
		if (consultaDataScroller == null) {
			consultaDataScroller = false;
		}
		return consultaDataScroller;
	}

	public void setConsultaDataScroller(Boolean consultaDataScroller) {
		this.consultaDataScroller = consultaDataScroller;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public PreInscricaoLogVO getPreInscricaoLogVO() {
		if(preInscricaoLogVO == null){
			preInscricaoLogVO = new PreInscricaoLogVO();
		}
		return preInscricaoLogVO;
	}

	public void setPreInscricaoLogVO(PreInscricaoLogVO preInscricaoLogVO) {
		this.preInscricaoLogVO = preInscricaoLogVO;
	}

	public void gravarPreInscricaoComFalhaCadastro() {
        try {
            getFacadeFactory().getPreInscricaoLogFacade().gravarPreInscricaoComFalhaCadastro(getPreInscricaoLogVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            
        }
    }
	
	public void gravarPreInscricaoComFalhaSelecionadas() {
		try {
			List listaPreInscricaoLogVOs = consultarSemLimitOffSet();
			realizarVerificacaoLogSelecionadoComFalhaGerarPreInscricao();
			getFacadeFactory().getPreInscricaoLogFacade().gravarPreInscricaoComFalhaSelecionadas(listaPreInscricaoLogVOs, getMapPreInscricaoLogVOs(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			consultar();
            setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> getListaSelectItemSituacaoLogPreInscricaoVOs() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem(SituacaoLogPreInscricaoEnum.SUCESSO, "Sucesso"));
		itens.add(new SelectItem(SituacaoLogPreInscricaoEnum.FALHA, "Falha"));
		itens.add(new SelectItem(SituacaoLogPreInscricaoEnum.REENVIADO, "Reenviado"));
		return itens;
	}

	public SituacaoLogPreInscricaoEnum getSituacaoLogPreInscricao() {
		return situacaoLogPreInscricao;
	}

	public void setSituacaoLogPreInscricao(SituacaoLogPreInscricaoEnum situacaoLogPreInscricao) {
		this.situacaoLogPreInscricao = situacaoLogPreInscricao;
	}

	public HashMap<Integer, PreInscricaoLogVO> getMapPreInscricaoLogVOs() {
		if (mapPreInscricaoLogVOs == null) {
			mapPreInscricaoLogVOs = new HashMap<Integer, PreInscricaoLogVO>(0);
		}
		return mapPreInscricaoLogVOs;
	}

	public void setMapPreInscricaoLogVOs(HashMap<Integer, PreInscricaoLogVO> mapPreInscricaoLogVOs) {
		this.mapPreInscricaoLogVOs = mapPreInscricaoLogVOs;
	}
	
	public void realizarVerificacaoLogSelecionadoComFalhaGerarPreInscricao() {
		for (PreInscricaoLogVO obj : (List<PreInscricaoLogVO>) getControleConsultaOtimizado().getListaConsulta()) {
			if (obj.getSelecionado()) {
				if (!getMapPreInscricaoLogVOs().containsKey(obj.getCodigo())) {
					getMapPreInscricaoLogVOs().put(obj.getCodigo(), obj);
				} 
			} else {
				if (getMapPreInscricaoLogVOs().containsKey(obj.getCodigo())) {
					getMapPreInscricaoLogVOs().remove(obj.getCodigo());
				}
			}
		}
	}

	public Boolean getRealizarConsultaSemLimitOffSet() {
		if (realizarConsultaSemLimitOffSet == null) {
			realizarConsultaSemLimitOffSet = false;
		}
		return realizarConsultaSemLimitOffSet;
	}

	public void setRealizarConsultaSemLimitOffSet(Boolean realizarConsultaSemLimitOffSet) {
		this.realizarConsultaSemLimitOffSet = realizarConsultaSemLimitOffSet;
	}
	
	public List consultarSemLimitOffSet() {
		try {
			List objs = new ArrayList(0);
			if (!getConsultaDataScroller()) {
				getMapPreInscricaoLogVOs().clear();
			}
			super.consultar();
			if (getControleConsulta().getCampoConsulta().equals("unidadeensino")) {
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPreInscricaoLogFacade().consultarPorUnidadeEnsino(new Integer(valorInt), dataInicioFiltro, dataFimFiltro, true, Uteis.NIVELMONTARDADOS_TODOS, null, null, getSituacaoLogPreInscricao(), getUsuarioLogado());
				
			} else if (getControleConsulta().getCampoConsulta().equals("cursoCodigo")) {
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getPreInscricaoLogFacade().consultarPorCodigoCurso(new Integer(valorInt), dataInicioFiltro, dataFimFiltro, true, Uteis.NIVELMONTARDADOS_TODOS, null, null, getSituacaoLogPreInscricao(), getUsuarioLogado());

			} else if (getControleConsulta().getCampoConsulta().equals("cursoNome")) {
				objs = getFacadeFactory().getPreInscricaoLogFacade().consultarPorNomeCurso(getControleConsulta().getValorConsulta(), dataInicioFiltro, dataFimFiltro, true, Uteis.NIVELMONTARDADOS_TODOS, null, null, getSituacaoLogPreInscricao(), getUsuarioLogado());
			} else if (getControleConsulta().getCampoConsulta().equals("email")) {
				objs = getFacadeFactory().getPreInscricaoLogFacade().consultarPorEmail(getControleConsulta().getValorConsulta(), dataInicioFiltro, dataFimFiltro, true, Uteis.NIVELMONTARDADOS_TODOS, null, null, getSituacaoLogPreInscricao(), getUsuarioLogado());
			} else if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getPreInscricaoLogFacade().consultarPorNomeProspect(getControleConsulta().getValorConsulta(), dataInicioFiltro, dataFimFiltro, true, Uteis.NIVELMONTARDADOS_TODOS, null, null, getSituacaoLogPreInscricao(), getUsuarioLogado());
			} 
			return objs;
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return new ArrayList(0);
		}
	}
	
	public void marcarTodasPreInscricoesComFalhas() {
		List listaPreInscricaoLogVOs = consultarSemLimitOffSet();
		getMapPreInscricaoLogVOs().clear();
		for (PreInscricaoLogVO obj : (List<PreInscricaoLogVO>) listaPreInscricaoLogVOs) {
			getMapPreInscricaoLogVOs().put(obj.getCodigo(), obj);
		}
		for (PreInscricaoLogVO obj : (List<PreInscricaoLogVO>) getControleConsultaOtimizado().getListaConsulta()) {
			obj.setSelecionado(true);
		}
	}
	
	public void desmarcarTodasPreInscricoesComFalhas() {
		getMapPreInscricaoLogVOs().clear();
		for (PreInscricaoLogVO obj : (List<PreInscricaoLogVO>) getControleConsultaOtimizado().getListaConsulta()) {
			obj.setSelecionado(false);
		}
	}
	
}
