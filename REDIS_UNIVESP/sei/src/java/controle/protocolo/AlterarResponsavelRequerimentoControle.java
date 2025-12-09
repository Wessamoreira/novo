package controle.protocolo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.protocolo.AlterarResponsavelRequerimentoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("AlterarResponsavelRequerimentoControle")
@Scope("viewScope")
@Lazy
public class AlterarResponsavelRequerimentoControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private AlterarResponsavelRequerimentoVO alterarResponsavelRequerimentoVO;
	private List<AlterarResponsavelRequerimentoVO> alterarResponsavelRequerimentoVOs;
	private Date dataIni;
	private Date dataFim;
	private Boolean todoPeriodo;
	
	private String nomeUnidadeEnsino;
	
	private CursoVO cursoVO;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	
	private TurmaVO turmaVO;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	
	private DepartamentoVO departamentoVO;
	private List<SelectItem> listaSelectItemDepartamento;
	private TipoRequerimentoVO tipoRequerimentoVO;
	private List<SelectItem> listaSelectItemTipoRequerimento;
	
	private String campoConsultaResponsavelRequerimento;
	private String valorConsultaResponsavelRequerimento;
	private List<FuncionarioVO> listaConsultaResponsavelRequerimento;
	
	private String campoConsultaResponsavelAnterior;
	private String valorConsultaResponsavelAnterior;
	private List<FuncionarioVO> listaConsultaResponsavelAnterior;
	
	private String campoConsultaResponsavelAlteracao;
	private String valorConsultaResponsavelAlteracao;
	private List<UsuarioVO> listaConsultaResponsavelAlteracao;

	public AlterarResponsavelRequerimentoControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		montarListaSelectItemDepartamento();
		montarListaSelectItemTipoRequerimento();
		setMensagemID("msg_entre_prmconsulta");
	}
	
	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			if (Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado().getCodigo())) {
				setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuario(getUsuarioLogado()));
			}else {
				setUnidadeEnsinoVOs(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(0, false, getUsuarioLogado()));
			}			
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				obj.setFiltrarUnidadeEnsino(true);
			}
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome()).append("; ");
				}
			}
			setNomeUnidadeEnsino(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					setNomeUnidadeEnsino(getUnidadeEnsinoVOs().get(0).getNome());
				} else {
					setNomeUnidadeEnsino("");
				}
			}
		}
	}
	
	public void limparDadosControlador() {
		setAlterarResponsavelRequerimentoVO(new AlterarResponsavelRequerimentoVO());
		getAlterarResponsavelRequerimentoVO().setDataAlteracao(new Date());
		getAlterarResponsavelRequerimentoVO().setResponsavelAlteracao(getUsuarioLogadoClone());
		setDataIni(null);
		setDataFim(null);
		setTodoPeriodo(true);
	}

	public String novo() {
		try {
			limparDadosControlador();
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("alterarResponsavelRequerimentoForm");
	}

	public String editar() throws Exception {
		try {
			setAlterarResponsavelRequerimentoVO((AlterarResponsavelRequerimentoVO) context().getExternalContext().getRequestMap().get("alterarResponsavelRequerimentoItens"));
			getAlterarResponsavelRequerimentoVO().setNovoObj(Boolean.FALSE);
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("alterarResponsavelRequerimentoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public String gravar() {
		try {
			getFacadeFactory().getAlterarResponsavelRequerimentoFacade().incluir(getAlterarResponsavelRequerimentoVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("alterarResponsavelRequerimentoForm");
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

	public String inicializarConsultar() throws Exception {
		try {
			limparDadosControlador();
			setMensagemID("msg_entre_prmconsulta");
			return Uteis.getCaminhoRedirecionamentoNavegacao("alterarResponsavelRequerimentoCons");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
		getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
		consultarOtimizado();
	}
	
    public Boolean getApresentarPaginador() {
        return !getAlterarResponsavelRequerimentoVO().getRequerimentoVOs().isEmpty();
    }

	public String consultarOtimizado() throws Exception {
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsultaOtimizado().setLimitePorPagina(10);
		try {
			if (getDataIni() != null && getDataFim() != null && getDataIni().after(getDataFim())) {
				throw new Exception("A data inicial do período deve ser menor que a data final.");
			}
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getAlterarResponsavelRequerimentoFacade().consultarOtimizado(getAlterarResponsavelRequerimentoVO(), getDataIni(), getDataFim(), getTodoPeriodo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getConfiguracaoFinanceiroPadraoSistema()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getAlterarResponsavelRequerimentoFacade().consultarTotalRegistros(getAlterarResponsavelRequerimentoVO(), getDataIni(), getDataFim(), getTodoPeriodo(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("alterarResponsavelRequerimentoCons");
	}
	
    public void selecionarTodos() {
        try {
            if (!getAlterarResponsavelRequerimentoVO().getRequerimentoVOs().isEmpty()) {
                Iterator i = getAlterarResponsavelRequerimentoVO().getRequerimentoVOs().iterator();
                while (i.hasNext()) {
                    RequerimentoVO req = (RequerimentoVO) i.next();
                    req.setSelecionado(Boolean.TRUE);
                }
            }
        } catch (Exception e) {
        	getAlterarResponsavelRequerimentoVO().setRequerimentoVOs(null);
        }
    }

    public void desmarcarTodos() {
        try {
            if (!getAlterarResponsavelRequerimentoVO().getRequerimentoVOs().isEmpty()) {
                Iterator i = getAlterarResponsavelRequerimentoVO().getRequerimentoVOs().iterator();
                while (i.hasNext()) {
                    RequerimentoVO req = (RequerimentoVO) i.next();
                    req.setSelecionado(Boolean.FALSE);
                }
            }
        } catch (Exception e) {
        	getAlterarResponsavelRequerimentoVO().setRequerimentoVOs(null);
        }
    }
    
    public String consultarRequerimentos() {
		try {
			if (getAlterarResponsavelRequerimentoVO().getResponsavelAnterior().getCodigo().equals(0)) {
				throw new Exception("O filtro RESPONSAVEL ANTERIOR deve ser informado.");
			}
			if (getDataIni() != null && getDataFim() != null && getDataIni().after(getDataFim())) {
				throw new Exception("A data inicial do período deve ser menor que a data final.");
			}
			getAlterarResponsavelRequerimentoVO().setRequerimentoVOs(getFacadeFactory().getRequerimentoFacade()
					.consultarOtimizadoParaAlterarResponsavel(getAlterarResponsavelRequerimentoVO(), getUnidadeEnsinoVOs(), getCursoVO(), getTurmaVO(), getDepartamentoVO(), getTipoRequerimentoVO(), getDataIni(), getDataFim(), getTodoPeriodo(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    	return "";
    }
    
    public void selecionarItemRequerimento() {
    	RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItens");
    }
    
	public void montarListaSelectItemDepartamento() {
		try {
			montarListaSelectItemDepartamento("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarListaSelectItemDepartamento(String prm) throws Exception {
		List<DepartamentoVO> resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getDepartamentoFacade().consultarPorUnidadeEnsino(getUnidadeEnsinoVOs(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado(), false, true, false, false, false, false, false);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				DepartamentoVO obj = (DepartamentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemDepartamento(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}
    
	public void montarListaSelectItemTipoRequerimento() {
		try {
			montarListaSelectItemTipoRequerimento("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
    
	public void montarListaSelectItemTipoRequerimento(String prm) throws Exception {
		List<TipoRequerimentoVO> resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarTipoRequerimentoComboBox();
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				TipoRequerimentoVO obj = (TipoRequerimentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemTipoRequerimento(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}
	
	public List<TipoRequerimentoVO> consultarTipoRequerimentoComboBox() throws Exception {
		return getFacadeFactory().getTipoRequerimentoFacade().consultarTipoRequerimentoComboBox(false, "AT", getUnidadeEnsinoLogado().getCodigo(), getCursoVO().getCodigo(), false, getUsuarioLogado(), false);
	}

	public AlterarResponsavelRequerimentoVO getAlterarResponsavelRequerimentoVO() {
		if (alterarResponsavelRequerimentoVO == null) {
			alterarResponsavelRequerimentoVO = new AlterarResponsavelRequerimentoVO();
		}
		return alterarResponsavelRequerimentoVO;
	}

	public void setAlterarResponsavelRequerimentoVO(AlterarResponsavelRequerimentoVO alterarResponsavelRequerimentoVO) {
		this.alterarResponsavelRequerimentoVO = alterarResponsavelRequerimentoVO;
	}

	public List<AlterarResponsavelRequerimentoVO> getAlterarResponsavelRequerimentoVOs() {
		if (alterarResponsavelRequerimentoVOs == null) {
			alterarResponsavelRequerimentoVOs = new ArrayList<AlterarResponsavelRequerimentoVO>(0);
		}
		return alterarResponsavelRequerimentoVOs;
	}

	public void setAlterarResponsavelRequerimentoVOs(List<AlterarResponsavelRequerimentoVO> alterarResponsavelRequerimentoVOs) {
		this.alterarResponsavelRequerimentoVOs = alterarResponsavelRequerimentoVOs;
	}
	
	public List<CursoVO> autocompleteCurso(Object suggest) {
		try {
			return getFacadeFactory().getCursoFacade().consultaRapidaPorNomeAutoComplete((String) suggest, getUnidadeEnsinoLogado().getCodigo(), 20, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
			return new ArrayList<CursoVO>();
		}
	}
	
	public void limparDadosCurso() throws Exception {
		setCursoVO(new CursoVO());
	}
	
	public void limparCurso() {
		try {
			getListaConsultaCurso().clear();
			setValorConsultaCurso(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarCursoCons() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getCursoFacade().consultarPorCodigo(valorInt, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCurso(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}

			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}
	
	public void selecionarCursoCons() throws Exception {
		try {
			CursoVO curso = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			setCursoVO(curso);
			setMensagemDetalhada("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<TurmaVO> autocompleteTurma(Object suggest) {
		try {
			return getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso((String) suggest, getCursoVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
			return new ArrayList<TurmaVO>();
		}
	}
	
	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(obj);
			setCursoVO(obj.getCurso());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparTurma() {
		try {
			getListaConsultaTurma().clear();
			setValorConsultaTurma(null);
			setTurmaVO(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosTurma() throws Exception {
		setTurmaVO(new TurmaVO());
	}
	
	public void consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getCursoVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}
	
	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}
	
	public String getNomeUnidadeEnsino() {
		if (nomeUnidadeEnsino == null) {
			nomeUnidadeEnsino = "";
		}
		return nomeUnidadeEnsino;
	}

	public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
		this.nomeUnidadeEnsino = nomeUnidadeEnsino;
	}
	
	public void atualizarListaComboBox() {
		try {
			montarListaSelectItemDepartamento("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Date getDataIni() {
		return dataIni;
	}

	public void setDataIni(Date dataIni) {
		this.dataIni = dataIni;
	}

	public Date getDataFim() {
		return dataFim;
	}

	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}

	public Boolean getTodoPeriodo() {
		if (todoPeriodo == null) {
			todoPeriodo = true;
		}
		return todoPeriodo;
	}

	public void setTodoPeriodo(Boolean todoPeriodo) {
		this.todoPeriodo = todoPeriodo;
	}
	
	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public DepartamentoVO getDepartamentoVO() {
		if (departamentoVO == null) {
			departamentoVO = new DepartamentoVO();
		}
		return departamentoVO;
	}

	public void setDepartamentoVO(DepartamentoVO departamentoVO) {
		this.departamentoVO = departamentoVO;
	}

	public TipoRequerimentoVO getTipoRequerimentoVO() {
		if (tipoRequerimentoVO == null) {
			tipoRequerimentoVO = new TipoRequerimentoVO();
		}
		return tipoRequerimentoVO;
	}

	public void setTipoRequerimentoVO(TipoRequerimentoVO tipoRequerimentoVO) {
		this.tipoRequerimentoVO = tipoRequerimentoVO;
	}
	
	public List<SelectItem> getListaSelectItemTipoRequerimento() {
		return listaSelectItemTipoRequerimento;
	}

	public void setListaSelectItemTipoRequerimento(List<SelectItem> listaSelectItemTipoRequerimento) {
		this.listaSelectItemTipoRequerimento = listaSelectItemTipoRequerimento;
	}

	public List<SelectItem> getListaSelectItemDepartamento() {
		return listaSelectItemDepartamento;
	}

	public void setListaSelectItemDepartamento(
			List<SelectItem> listaSelectItemDepartamento) {
		this.listaSelectItemDepartamento = listaSelectItemDepartamento;
	}
	
	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getValorConsultaCurso() {
		if (valorConsultaCurso == null) {
			valorConsultaCurso = "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}
	
	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}
	
	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public String getValorConsultaTurma() {
		if (valorConsultaTurma == null) {
			valorConsultaTurma = "";
		}
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}
	
	public void atualizarControlador() {
		getAlterarResponsavelRequerimentoVO().getMotivoAlteracao();
	}
	
	/* INICIO RESPONSAVEL REQUERIMENTO */
	
	public void limparDadosResponsavelRequerimento() {
		getAlterarResponsavelRequerimentoVO().setResponsavelRequerimento(new FuncionarioVO());
	}
	
	public List<FuncionarioVO> autocompleteFuncionario(Object suggest) {
		try {
			return getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomePessoaAutoComplete((String) suggest, getUnidadeEnsinoLogado().getCodigo(), 20, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return new ArrayList<FuncionarioVO>();
		}
	}
	
	public void selecionarResponsavelRequerimento() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("responsavelRequerimentoItens");
		getAlterarResponsavelRequerimentoVO().setResponsavelRequerimento(obj);
	}
	
	public void consultarResponsavelRequerimento() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getValorConsultaResponsavelRequerimento().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaResponsavelRequerimento().equals("NOME")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaResponsavelRequerimento(), getAlterarResponsavelRequerimentoVO().getResponsavelRequerimento().getDepartamento().getCodigo(), "", getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaResponsavelRequerimento().equals("MATRICULA")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaResponsavelRequerimento(), getAlterarResponsavelRequerimentoVO().getResponsavelRequerimento().getDepartamento().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null, null, getUsuarioLogado());
			}
			if (getCampoConsultaResponsavelRequerimento().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaResponsavelRequerimento(), getAlterarResponsavelRequerimentoVO().getResponsavelRequerimento().getDepartamento().getCodigo(), "", getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaResponsavelRequerimento().equals("CARGO")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaResponsavelRequerimento(), getAlterarResponsavelRequerimentoVO().getResponsavelRequerimento().getDepartamento().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaResponsavelRequerimento().equals("UNIDADEENSINO")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaResponsavelRequerimento(), "FU", getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaResponsavelRequerimento(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaResponsavelRequerimento(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("NOME", "Nome"));
		itens.add(new SelectItem("MATRICULA", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("CARGO", "Cargo"));
		itens.add(new SelectItem("UNIDADEENSINO", "Unidade de Ensino"));
		return itens;
	}

	public String getCampoConsultaResponsavelRequerimento() {
		if (campoConsultaResponsavelRequerimento == null) {
			campoConsultaResponsavelRequerimento = "";
		}
		return campoConsultaResponsavelRequerimento;
	}

	public void setCampoConsultaResponsavelRequerimento(String campoConsultaResponsavelRequerimento) {
		this.campoConsultaResponsavelRequerimento = campoConsultaResponsavelRequerimento;
	}

	public String getValorConsultaResponsavelRequerimento() {
		if (valorConsultaResponsavelRequerimento == null) {
			valorConsultaResponsavelRequerimento = "";
		}
		return valorConsultaResponsavelRequerimento;
	}

	public void setValorConsultaResponsavelRequerimento(String valorConsultaResponsavelRequerimento) {
		this.valorConsultaResponsavelRequerimento = valorConsultaResponsavelRequerimento;
	}

	public List<FuncionarioVO> getListaConsultaResponsavelRequerimento() {
		if (listaConsultaResponsavelRequerimento == null) {
			listaConsultaResponsavelRequerimento = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaResponsavelRequerimento;
	}

	public void setListaConsultaResponsavelRequerimento(List<FuncionarioVO> listaConsultaResponsavelRequerimento) {
		this.listaConsultaResponsavelRequerimento = listaConsultaResponsavelRequerimento;
	}
	
	/* FIM RESPONSAVEL REQUERIMENTO */
	
	/* INICIO RESPONSAVEL ALTERACAO */
	
	public void limparDadosResponsavelAlteracao() {
		getAlterarResponsavelRequerimentoVO().setResponsavelAlteracao(new UsuarioVO());
	}
	
	public List<UsuarioVO> autocompleteUsuario(Object suggest) {
		try {
			return getFacadeFactory().getUsuarioFacade().consultaRapidaPorNomeUsuarioAutoComplete((String) suggest, 20, 0, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return new ArrayList<UsuarioVO>();
		}
	}
	
	public void selecionarResponsavelAlteracao() throws Exception {
		UsuarioVO obj = (UsuarioVO) context().getExternalContext().getRequestMap().get("responsavelAlteracaoItens");
		getAlterarResponsavelRequerimentoVO().setResponsavelAlteracao(obj);
	}
	
	public void consultarResponsavelAlteracao() {
		try {
			List<UsuarioVO> objs = new ArrayList<UsuarioVO>(0);
			if (getValorConsultaResponsavelAlteracao().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaResponsavelAlteracao().equals("NOME")) {
				objs = getFacadeFactory().getUsuarioFacade().consultaRapidaPorNomeFuncionario(getValorConsultaResponsavelAlteracao(), 0, "", getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaResponsavelAlteracao().equals("MATRICULA")) {
				objs = getFacadeFactory().getUsuarioFacade().consultaRapidaPorMatriculaFuncionario(getValorConsultaResponsavelAlteracao(), 0, getUnidadeEnsinoLogado().getCodigo(), null, null, Uteis.NIVELMONTARDADOS_COMBOBOX, null, null, getUsuarioLogado());
			}
			if (getCampoConsultaResponsavelAlteracao().equals("CPF")) {
				objs = getFacadeFactory().getUsuarioFacade().consultaRapidaPorCPFFuncionario(getValorConsultaResponsavelAlteracao(), 0, "", getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaResponsavelAlteracao().equals("CARGO")) {
				objs = getFacadeFactory().getUsuarioFacade().consultaRapidaPorCargoFuncionario(getValorConsultaResponsavelAlteracao(), 0, getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaResponsavelAlteracao().equals("UNIDADEENSINO")) {
				objs = getFacadeFactory().getUsuarioFacade().consultaRapidaFuncionarioPorUnidadeEnsino(getValorConsultaResponsavelAlteracao(), null, getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultaResponsavelAlteracao(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaResponsavelAlteracao(new ArrayList<UsuarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<SelectItem> getTipoConsultaComboUsuario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("NOME", "Nome"));
		itens.add(new SelectItem("MATRICULA", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("CARGO", "Cargo"));
		itens.add(new SelectItem("UNIDADEENSINO", "Unidade de Ensino"));
		return itens;
	}

	public String getCampoConsultaResponsavelAlteracao() {
		if (campoConsultaResponsavelAlteracao == null) {
			campoConsultaResponsavelAlteracao = "";
		}
		return campoConsultaResponsavelAlteracao;
	}

	public void setCampoConsultaResponsavelAlteracao(String campoConsultaResponsavelAlteracao) {
		this.campoConsultaResponsavelAlteracao = campoConsultaResponsavelAlteracao;
	}

	public String getValorConsultaResponsavelAlteracao() {
		if (valorConsultaResponsavelAlteracao == null) {
			valorConsultaResponsavelAlteracao = "";
		}
		return valorConsultaResponsavelAlteracao;
	}

	public void setValorConsultaResponsavelAlteracao(String valorConsultaResponsavelAlteracao) {
		this.valorConsultaResponsavelAlteracao = valorConsultaResponsavelAlteracao;
	}

	public List<UsuarioVO> getListaConsultaResponsavelAlteracao() {
		if (listaConsultaResponsavelAlteracao == null) {
			listaConsultaResponsavelAlteracao = new ArrayList<UsuarioVO>(0);
		}
		return listaConsultaResponsavelAlteracao;
	}

	public void setListaConsultaResponsavelAlteracao(List<UsuarioVO> listaConsultaResponsavelAlteracao) {
		this.listaConsultaResponsavelAlteracao = listaConsultaResponsavelAlteracao;
	}

	/* FIM RESPONSAVEL ALTERACAO */
	
	/* INICIO RESPONSAVEL ANTERIOR */
	
	public void limparDadosResponsavelAnterior() {
		getAlterarResponsavelRequerimentoVO().setResponsavelAnterior(new FuncionarioVO());
	}
	
	public void selecionarResponsavelAnterior() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("responsavelAnteriorItens");
		getAlterarResponsavelRequerimentoVO().setResponsavelAnterior(obj);
	}
	
	public void consultarResponsavelAnterior() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getValorConsultaResponsavelAnterior().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			if (getCampoConsultaResponsavelAnterior().equals("NOME")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaResponsavelAnterior(), getAlterarResponsavelRequerimentoVO().getResponsavelAnterior().getDepartamento().getCodigo(), "", getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaResponsavelAnterior().equals("MATRICULA")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaResponsavelAnterior(), getAlterarResponsavelRequerimentoVO().getResponsavelAnterior().getDepartamento().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null, null, getUsuarioLogado());
			}
			if (getCampoConsultaResponsavelAnterior().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaResponsavelAnterior(), getAlterarResponsavelRequerimentoVO().getResponsavelAnterior().getDepartamento().getCodigo(), "", getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaResponsavelAnterior().equals("CARGO")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaResponsavelAnterior(), getAlterarResponsavelRequerimentoVO().getResponsavelAnterior().getDepartamento().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, null, null, null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaResponsavelAnterior().equals("UNIDADEENSINO")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaResponsavelAnterior(), "FU", getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaResponsavelAnterior(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaResponsavelAnterior(new ArrayList<FuncionarioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getCampoConsultaResponsavelAnterior() {
		return campoConsultaResponsavelAnterior;
	}

	public void setCampoConsultaResponsavelAnterior(
			String campoConsultaResponsavelAnterior) {
		this.campoConsultaResponsavelAnterior = campoConsultaResponsavelAnterior;
	}

	public String getValorConsultaResponsavelAnterior() {
		return valorConsultaResponsavelAnterior;
	}

	public void setValorConsultaResponsavelAnterior(
			String valorConsultaResponsavelAnterior) {
		this.valorConsultaResponsavelAnterior = valorConsultaResponsavelAnterior;
	}

	public List<FuncionarioVO> getListaConsultaResponsavelAnterior() {
		return listaConsultaResponsavelAnterior;
	}

	public void setListaConsultaResponsavelAnterior(
			List<FuncionarioVO> listaConsultaResponsavelAnterior) {
		this.listaConsultaResponsavelAnterior = listaConsultaResponsavelAnterior;
	}

	/* FIM RESPONSAVEL ANTERIOR */
	
}
