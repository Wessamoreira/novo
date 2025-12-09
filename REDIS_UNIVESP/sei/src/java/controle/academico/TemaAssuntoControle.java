package controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TemaAssuntoDisciplinaVO;
import negocio.comuns.academico.TemaAssuntoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 * @author Victor Hugo 27/02/2015
 */
@Controller("TemaAssuntoControle")
@Scope("viewScope")
@Lazy
public class TemaAssuntoControle extends SuperControle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private TemaAssuntoVO temaAssuntoVO;
	private String valorConsultaDisciplina;
	private List<SelectItem> tipoConsultaComboBoxDisciplina ;
	private String campoConsultaDisciplina;
	private List<DisciplinaVO> disciplinaVOs;
	private List<TemaAssuntoVO> listaConsultaTemaAssuntoVOs;
	private List<TemaAssuntoDisciplinaVO> listaConsultaTemaAssuntoDisciplinaVOs;
	private List<SelectItem> tipoConsultaComboBoxAssunto ;
	private DisciplinaVO disciplinaVO;
	private List<TemaAssuntoVO> listaTemaAssuntoVODisciplina;

	public TemaAssuntoControle() {
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
	}
     
	
	public String novo() {
		setDisciplinaVO(new DisciplinaVO());
		getListaConsultaTemaAssuntoVOs().clear();
		getListaTemaAssuntoVODisciplina().clear();
		setTemaAssuntoVO(new TemaAssuntoVO());
		getListaConsultaTemaAssuntoDisciplinaVOs().clear();
		limparMensagem();
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("temaAssuntoForm");
	}

	public void excluirTemaAssuntoDisciplina() {
		try {
			TemaAssuntoVO temaAssuntoVO = (TemaAssuntoVO) context().getExternalContext().getRequestMap().get("assuntos");
			getFacadeFactory().getTemaAssuntoDisciplinaFacade().excluir(getDisciplinaVO().getCodigo(), temaAssuntoVO.getCodigo(), false, getUsuarioLogado());
			getListaTemaAssuntoVODisciplina().remove(temaAssuntoVO);
			setMensagemID("msg_dado_excluido", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getTemaAssuntoDisciplinaFacade().excluirTodosTemaAssuntoDisciplina(getDisciplinaVO().getCodigo(), getListaTemaAssuntoVODisciplina(), false, getUsuarioLogado());
			getListaTemaAssuntoVODisciplina().clear();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String editar() {
		TemaAssuntoDisciplinaVO temaAssuntoDisciplinaVO = (TemaAssuntoDisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinasLista");
		setDisciplinaVO(temaAssuntoDisciplinaVO.getDisciplinaVO());
		setTemaAssuntoVO(new TemaAssuntoVO());
		getListaConsultaTemaAssuntoVOs().clear();
		consultarTemaAssuntoDisciplina();
		setMensagemID("msg_dados_editar", Uteis.ALERTA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("temaAssuntoForm");
	}

	public void gravarTemaAssunto() {
		try {
			getFacadeFactory().getTemaAssuntoFacade().persistir(getTemaAssuntoVO(), false, getUsuarioLogado());
			setTemaAssuntoVO(new TemaAssuntoVO());
			getListaConsultaTemaAssuntoVOs().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void gravar() {
		try {
			if(!Uteis.isAtributoPreenchido(disciplinaVO.getCodigo())) {
				throw new Exception("O Campo (DISCIPLINA) Deve Ser Preenchido");
			}
			
			if(getListaConsultaTemaAssuntoVOs().isEmpty()){
				throw new Exception("Deve Ser Incluído Pelo Menos Um Assunto");
			}
			
			getFacadeFactory().getTemaAssuntoDisciplinaFacade().incluirTemasAssuntoDisciplina(getListaConsultaTemaAssuntoDisciplinaVOs(), getUsuarioLogado());
			getListaConsultaTemaAssuntoDisciplinaVOs().clear();
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String voltarTelaConsulta() {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("temaAssuntoCons");
	}

	public void limparDadosTemaAssuntoVO() {
		setTemaAssuntoVO(new TemaAssuntoVO());
		getListaConsultaTemaAssuntoVOs().clear();
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
	}

	public String consultar() {
		try {
			getControleConsulta().setListaConsulta((getFacadeFactory().getTemaAssuntoFacade().consultar(getControleConsulta().getValorConsulta(),
					           getControleConsulta().getCampoConsulta(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado())));
			if (getControleConsulta().getListaConsulta().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void consultarTemaAssuntoDisciplina() {
		try {
			setListaTemaAssuntoVODisciplina((getFacadeFactory().getTemaAssuntoFacade().consultarTemaAssuntoPorCodigoDisciplina(getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado())));
			if (getListaTemaAssuntoVODisciplina().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * método que adiciona um tema assunto na lista e persisti o mesmo na base
	 * de dados.
	 */
	public void adicionarTemaAssuntoTemaAssuntoDisciplina() {
		try {
			TemaAssuntoVO temaAssuntoVO = (TemaAssuntoVO) context().getExternalContext().getRequestMap().get("assuntosIncluir");
			getFacadeFactory().getTemaAssuntoDisciplinaFacade().adicionarTemaAssuntoTemaAssuntoDisciplina(temaAssuntoVO, getListaConsultaTemaAssuntoDisciplinaVOs(), getListaTemaAssuntoVODisciplina(), getDisciplinaVO(), getUsuarioLogado());
			getListaTemaAssuntoVODisciplina().add(temaAssuntoVO);
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarTemaAssunto() {
		try {
			getListaConsultaTemaAssuntoVOs().clear();
			setListaConsultaTemaAssuntoVOs(getFacadeFactory().getTemaAssuntoFacade().consultarPorNome(getTemaAssuntoVO().getNome(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarTemaAssunto() {
		TemaAssuntoVO temaAssuntoVO = (TemaAssuntoVO) context().getExternalContext().getRequestMap().get("assuntosIncluir");
		setTemaAssuntoVO(temaAssuntoVO);
		setMensagemID("msg_dados_editar", Uteis.ALERTA);
	}

	public void selecionarDisciplina() throws Exception {
		try {
			DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaLista");
			setDisciplinaVO(disciplina);
			consultarTemaAssuntoDisciplina();
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarDisciplina() {
		try {
			if (getValorConsultaDisciplina().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getCampoConsultaDisciplina().equals("codigo")) {
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				DisciplinaVO disciplina = new DisciplinaVO();
				disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(new Integer(valorInt), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				if (!disciplina.equals(new DisciplinaVO()) || disciplina != null) {
					objs.add(disciplina);
				}
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setDisciplinaVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setDisciplinaVOs(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparDados() {
		setDisciplinaVO(new DisciplinaVO());
		getListaConsultaTemaAssuntoVOs().clear();
		getListaTemaAssuntoVODisciplina().clear();
		setTemaAssuntoVO(new TemaAssuntoVO());
		getListaConsultaTemaAssuntoDisciplinaVOs().clear();
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
	}

	// Getters and Setters
	public TemaAssuntoVO getTemaAssuntoVO() {
		if (temaAssuntoVO == null) {
			temaAssuntoVO = new TemaAssuntoVO();
		}
		return temaAssuntoVO;
	}

	public void setTemaAssuntoVO(TemaAssuntoVO temaAssuntoVO) {
		this.temaAssuntoVO = temaAssuntoVO;
	}

	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}
 
	public List<SelectItem> getTipoConsultaComboBoxDisciplina() {
		if (tipoConsultaComboBoxDisciplina == null) {
			tipoConsultaComboBoxDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboBoxDisciplina.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboBoxDisciplina.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboBoxDisciplina;
	}

	public void setTipoConsultaComboBoxDisciplina(List<SelectItem> tipoConsultaComboBoxDisciplina) {
		this.tipoConsultaComboBoxDisciplina = tipoConsultaComboBoxDisciplina;
	}

	public String getCampoConsultaDisciplina() {
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public List<DisciplinaVO> getDisciplinaVOs() {
		if (disciplinaVOs == null) {
			disciplinaVOs = new ArrayList<DisciplinaVO>();
		}
		return disciplinaVOs;
	}

	public void setDisciplinaVOs(List<DisciplinaVO> disciplinaVOs) {
		this.disciplinaVOs = disciplinaVOs;
	}
	
	public List<TemaAssuntoVO> getListaConsultaTemaAssuntoVOs() {
		if (listaConsultaTemaAssuntoVOs == null) {
			listaConsultaTemaAssuntoVOs = new ArrayList<TemaAssuntoVO>();
		}
		return listaConsultaTemaAssuntoVOs;
	}

	public void setListaConsultaTemaAssuntoVOs(List<TemaAssuntoVO> listaConsultaTemaAssuntoVOs) {
		this.listaConsultaTemaAssuntoVOs = listaConsultaTemaAssuntoVOs;
	}

	public List<TemaAssuntoDisciplinaVO> getListaConsultaTemaAssuntoDisciplinaVOs() {
		if (listaConsultaTemaAssuntoDisciplinaVOs == null) {
			listaConsultaTemaAssuntoDisciplinaVOs = new ArrayList<TemaAssuntoDisciplinaVO>();
		}
		return listaConsultaTemaAssuntoDisciplinaVOs;
	}

	public void setListaConsultaTemaAssuntoDisciplinaVOs(List<TemaAssuntoDisciplinaVO> listaConsultaTemaAssuntoDisciplinaVOs) {
		this.listaConsultaTemaAssuntoDisciplinaVOs = listaConsultaTemaAssuntoDisciplinaVOs;
	}
 
	
	public List<SelectItem> getTipoConsultaComboBoxAssunto() {
	    
		if (tipoConsultaComboBoxAssunto == null  ) {
			tipoConsultaComboBoxAssunto = new ArrayList<SelectItem>(0);
			tipoConsultaComboBoxAssunto.add(new SelectItem("nomeDisciplina", "Nome Disciplina"));
			tipoConsultaComboBoxAssunto.add(new SelectItem("codigoDisciplina", "Código Disciplina"));
			tipoConsultaComboBoxAssunto.add(new SelectItem("nomeAssunto", "Nome Assunto"));
			tipoConsultaComboBoxAssunto.add(new SelectItem("abreviatura", "Abreviatura Assunto"));
		}
		return tipoConsultaComboBoxAssunto;
	}

	public void setTipoConsultaComboBoxAssunto(List<SelectItem> tipoConsultaComboBoxAssunto) {
		this.tipoConsultaComboBoxAssunto = tipoConsultaComboBoxAssunto;
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
	}

	public List<TemaAssuntoVO> getListaTemaAssuntoVODisciplina() {
		if (listaTemaAssuntoVODisciplina == null) {
			listaTemaAssuntoVODisciplina = new ArrayList<TemaAssuntoVO>();
		}
		return listaTemaAssuntoVODisciplina;
	}

	public void setListaTemaAssuntoVODisciplina(List<TemaAssuntoVO> listaTemaAssuntoVODisciplina) {
		this.listaTemaAssuntoVODisciplina = listaTemaAssuntoVODisciplina;
	}
}
