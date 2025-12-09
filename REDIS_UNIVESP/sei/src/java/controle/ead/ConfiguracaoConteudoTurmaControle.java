package controle.ead;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.AvaliacaoOnlineVO;
import negocio.comuns.ead.TurmaDisciplinaConteudoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * @author Victor Hugo 08/01/2015
 */
@Controller("ConfiguracaoConteudoTurmaControle")
@Scope("viewScope")
public class ConfiguracaoConteudoTurmaControle extends SuperControle implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO;
	private List<SelectItem> listaSelectItemDisciplinasTurma;
	private String fecharModalPanelIncluir;
	private List<SelectItem> listaSelectItemTurma;
	private Boolean buscarTurmasAnteriores;
	private List<SelectItem> listaSelectItemConteudo;
	private List<SelectItem> listaSelectItemAvaliacaOnline;
	private String usuarioAlterouPorUltimo;
	private Date   dataModificacao;
	private String anoSemestre;
	//campo usado para preencher o log de alteração  feito quando alundo muda de conteudo 
	private String usuarioAlteracao;
	private Date dataAlteracao;
	private String matriculaAlteracao;
	private String registrosExcluidosLogAlteracao;
	private Boolean selecionarTodos;
	private MatriculaPeriodoTurmaDisciplinaVO matriculaperiodoVO;
	private String fecharModalPainelALunosConteudoDiferente  ;
	
 
	@PostConstruct
	public void init() {
		turmaDisciplinaConteudoVO = new TurmaDisciplinaConteudoVO();
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			montarListaSelectItemTurmaProfessor();
		}
		
		getControleConsulta().getListaConsulta().clear();
		setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
	}
	
	@SuppressWarnings("unchecked")
	public void persistir() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			for (TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO : (List<TurmaDisciplinaConteudoVO>) getControleConsulta().getListaConsulta()) {
				if (getTurmaDisciplinaConteudoVO().compareTo(turmaDisciplinaConteudoVO) == 0) {
					throw new Exception(UteisJSF.internacionalizar("msg_JaExisteUmaConfiguracaoParaEsteAnoSemestreCadastrada"));
				}
			}
			getFacadeFactory().getTurmaDisciplinaConteudoFacade().incluir(getTurmaDisciplinaConteudoVO(), false, getUsuarioLogado());
			setFecharModalPanelIncluir("RichFaces.$('panelIncluir').hide()");
			consultar();
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setFecharModalPanelIncluir("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarTurma() {
		try {
			if (getControleConsultaTurma().getCampoConsulta().equals("identificadorTurma")) {
				getControleConsultaTurma().setListaConsulta(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getControleConsultaTurma().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, false, "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsultaTurma().getCampoConsulta().equals("nomeTurno")) {
				getControleConsultaTurma().setListaConsulta(getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getControleConsultaTurma().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			if (getControleConsultaTurma().getCampoConsulta().equals("nomeCurso")) {
				getControleConsultaTurma().setListaConsulta(getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getControleConsultaTurma().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			getControleConsultaTurma().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarListaSelectItemTurmaProfessor() {
		List<TurmaVO> listaTurmas = null;
		List<Integer> mapAuxiliarSelectItem = new ArrayList();
		try {
			listaTurmas = consultarTurmaPorProfessor();
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			for (TurmaVO turmaVO : listaTurmas) {
				if(!mapAuxiliarSelectItem.contains(turmaVO.getCodigo())){
					getListaSelectItemTurma().add(new SelectItem(turmaVO.getCodigo(), turmaVO.aplicarRegraNomeCursoApresentarCombobox()));				
					mapAuxiliarSelectItem.add(turmaVO.getCodigo());
					removerObjetoMemoria(turmaVO);
				}
			}

		} catch (Exception e) {
			getListaSelectItemTurma().clear();
		} finally {
			Uteis.liberarListaMemoria(listaTurmas);
			mapAuxiliarSelectItem = null;
		}
	}

	public void montarInformacoesParaExclusaoConfiguracao() {
		TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO = ((TurmaDisciplinaConteudoVO) context().getExternalContext().getRequestMap().get("configuracaoItens"));
		getTurmaDisciplinaConteudoVO().setCodigo(turmaDisciplinaConteudoVO.getCodigo());
		getTurmaDisciplinaConteudoVO().setDisciplinaVO(turmaDisciplinaConteudoVO.getDisciplinaVO());
		getTurmaDisciplinaConteudoVO().setAno(turmaDisciplinaConteudoVO.getAno());
		getTurmaDisciplinaConteudoVO().setSemestre(turmaDisciplinaConteudoVO.getSemestre());
		
	}
	
	public void montarInformacoesParaExclusaoAlunoConteudoDiferente() {
		MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = ((MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("configuracaoItens"));
		setMatriculaperiodoVO(matriculaPeriodoTurmaDisciplinaVO);
		
	}
	
	public void montarInformacoesALunosMatriculadosComConteudoSemErro() {
		MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = ((MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("configuracaoItens"));
		String[] logAlteracao = matriculaPeriodoTurmaDisciplinaVO.getLogAlteracao().split(";");
		if (Uteis.isAtributoPreenchido(matriculaPeriodoTurmaDisciplinaVO.getLogAlteracao())) {
			registrosExcluidosLogAlteracao = logAlteracao[2].trim();
			dataAlteracao = UteisData.getData(logAlteracao[4].trim());
			usuarioAlteracao = logAlteracao[5].trim();			
		}else {
			registrosExcluidosLogAlteracao = null;
			dataAlteracao = null;
			usuarioAlteracao = null;		
		}
		 
		setMatriculaperiodoVO(matriculaPeriodoTurmaDisciplinaVO);
		
		
	}
	
	public void montarInformacoesALunosMatriculadosComConteudoComErro() {
		MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO = ((MatriculaPeriodoTurmaDisciplinaVO) context().getExternalContext().getRequestMap().get("configuracaoItens"));
		setMatriculaperiodoVO(matriculaPeriodoTurmaDisciplinaVO);
	}
	public void montarlistaTotalALunosConteudoCorretoTurma() {
		TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO = ((TurmaDisciplinaConteudoVO) context().getExternalContext().getRequestMap().get("configuracaoItens"));
		try {
			if(Uteis.isAtributoPreenchido(turmaDisciplinaConteudoVO.getCodigoMatPerTurmaDiscAlunoConteudoCorreto())) {				
				getControleConsulta().getListaConsulta2().clear();
				getControleConsulta().setListaConsulta2(getFacadeFactory().getTurmaDisciplinaConteudoFacade().consultarMatriculaPeriodoTurmaDisciplina(turmaDisciplinaConteudoVO.getCodigoMatPerTurmaDiscAlunoConteudoCorreto(), getUsuarioLogado()));
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public void montarlistaTotalALunosConteudoTurma() {
		TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO = ((TurmaDisciplinaConteudoVO) context().getExternalContext().getRequestMap().get("configuracaoItens"));
		try {
			if(Uteis.isAtributoPreenchido(turmaDisciplinaConteudoVO.getCodigoMatPerTurmaDiscAlunoConteudoCorreto())) {
				getControleConsulta().getListaConsulta2().clear();
		    	getControleConsulta().setListaConsulta2(getFacadeFactory().getTurmaDisciplinaConteudoFacade().consultarMatriculaPeriodoTurmaDisciplina(turmaDisciplinaConteudoVO.getCodigoMatPerTurmaDiscAlunoConteudoCorreto(), getUsuarioLogado()));
			}
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}
    public void montarlistaTotalALunosConteudoDiferenteTurma() {
    	TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO = ((TurmaDisciplinaConteudoVO) context().getExternalContext().getRequestMap().get("configuracaoItens"));
		try {
			getControleConsulta().setListaConsulta3(getFacadeFactory().getTurmaDisciplinaConteudoFacade().consultarMatriculaPeriodoTurmaDisciplina(turmaDisciplinaConteudoVO.getCodigoMatPerTurDisciplinaOutroAnoSemetreUsandoConteudoAntigo(), getUsuarioLogado()));
			setTurmaDisciplinaConteudoVO(turmaDisciplinaConteudoVO);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
	}
	public List<TurmaVO> consultarTurmaPorProfessor() {
		try {
			if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorGraduacao().getCodigo().equals(getUsuarioLogado().getPerfilAcesso().getCodigo())) {
				return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), Uteis.getSemestreAtual(), Uteis.getData(new Date(), "yyyy"), getBuscarTurmasAnteriores(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, true, false);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return null;
	}

	public void limparDadosTurma() {
		removerObjetoMemoria(getTurmaDisciplinaConteudoVO().getTurmaVO());
		getListaSelectItemDisciplinasTurma().clear();
		getControleConsulta().getListaConsulta().clear();
	}

	public void montarListaDisciplinasTurma() {
		try {
			setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(consultarDisciplina(getTurmaDisciplinaConteudoVO().getTurmaVO()), "codigo", "nome", true));
			montarComboBoxConteudo();
		} catch (Exception e) {
			setListaSelectItemDisciplinasTurma(new ArrayList<SelectItem>(0));
		}
	}

	public List<DisciplinaVO> consultarDisciplina(TurmaVO turmaVO) throws Exception {
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			return getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null, turmaVO.getCodigo(), null, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, false, getUsuarioLogado());
		} else {
			return getFacadeFactory().getDisciplinaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(turmaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		}
	}

	public void montarTurma() throws Exception {
		try {
			getTurmaDisciplinaConteudoVO().setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getTurmaDisciplinaConteudoVO().getTurmaVO().getIdentificadorTurma(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			consultar();
			montarListaDisciplinasTurma();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			removerObjetoMemoria(getTurmaDisciplinaConteudoVO().getTurmaVO());
			getTurmaDisciplinaConteudoVO().setTurmaVO(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
			obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), 0, getUsuarioLogado());
			getTurmaDisciplinaConteudoVO().setTurmaVO(obj);
			getControleConsultaTurma().getListaConsulta().clear();
			consultar();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarComboBoxConteudo() {
		try {
			List<ConteudoVO> resultado = null;
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				resultado = getFacadeFactory().getConteudoFacade().consultarPorCodigoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getTurmaDisciplinaConteudoVO().getDisciplinaVO().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			} else {
				resultado = getFacadeFactory().getConteudoFacade().consultarConteudoPorCodigoDisciplina(getTurmaDisciplinaConteudoVO().getDisciplinaVO().getCodigo(), NivelMontarDados.BASICO, false, getUsuarioLogado());
			}
			setListaSelectItemConteudo(UtilSelectItem.getListaSelectItem(resultado, "codigo", "descricao", true));
		} catch (Exception e) {
			setListaSelectItemConteudo(new ArrayList<SelectItem>(0));
		}
	}

	public String consultar() {
		try {
			if (getUsuarioLogado().getIsApresentarVisaoProfessor() && getTurmaDisciplinaConteudoVO().getTurmaVO().getCodigo() > 0) {
				getTurmaDisciplinaConteudoVO().setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorChavePrimariaDadosBasicosTurmaAgrupada(getTurmaDisciplinaConteudoVO().getTurmaVO().getCodigo(), getUsuarioLogado()));
				getControleConsulta().setListaConsulta(getFacadeFactory().getTurmaDisciplinaConteudoFacade().consultarConfiguracoesConteudoTurma(getTurmaDisciplinaConteudoVO().getTurmaVO().getCodigo(), getUsuarioLogado()));
			} else {
				getControleConsulta().setListaConsulta(getFacadeFactory().getTurmaDisciplinaConteudoFacade().consultarConfiguracoesConteudoTurma(getTurmaDisciplinaConteudoVO().getTurmaVO().getCodigo(), getUsuarioLogado()));
			}
			if (getControleConsulta().getListaConsulta().isEmpty()) {
				throw new Exception(UteisJSF.internacionalizar("msg_relatorio_vazio"));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void excluir() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getTurmaDisciplinaConteudoFacade().excluir(getTurmaDisciplinaConteudoVO(), true, getUsuarioLogado());
			consultar();
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void  alterarAlunoConteudoDiferente() {
		try {
		    setFecharModalPainelALunosConteudoDiferente(getFacadeFactory().getTurmaDisciplinaConteudoFacade().alterarALunoConteudoDisciplinaDiferente(getControleConsulta().getListaConsulta3(),getTurmaDisciplinaConteudoVO(),getFecharModalPainelALunosConteudoDiferente(), getUsuarioLogadoClone() , getTurmaDisciplinaConteudoVO().getConteudoVO().getCodigo()));
			consultar();		
			setMensagemID(MSG_TELA.msg_dados_atualizados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void montarListaSelectItemAvaliacaoOnline() {
		try {
			getListaSelectItemAvaliacaOnline().clear();
			List<AvaliacaoOnlineVO> resultado = null;
			if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				resultado = getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarAvaliacoesOnlinesPorDisciplinaEConteudoEAvaliacoesOnlinesRandomicas(getTurmaDisciplinaConteudoVO().getDisciplinaVO().getCodigo(), getTurmaDisciplinaConteudoVO().getConteudoVO().getCodigo(), true, getUsuarioLogado().getPessoa().getCodigo());
			} else {
				resultado = getFacadeFactory().getAvaliacaoOnlineInterfaceFacade().consultarAvaliacoesOnlinesPorDisciplinaEConteudoEAvaliacoesOnlinesRandomicas(getTurmaDisciplinaConteudoVO().getDisciplinaVO().getCodigo(), getTurmaDisciplinaConteudoVO().getConteudoVO().getCodigo(), false, null);
			}
			setListaSelectItemAvaliacaOnline(UtilSelectItem.getListaSelectItem(resultado, "codigo", "nome", true));
		} catch (Exception e) {
			setListaSelectItemAvaliacaOnline(new ArrayList<SelectItem>(0));
		}
	}

	public void buscarDadosLog() {
		
		try {
			TurmaDisciplinaConteudoVO obj = (TurmaDisciplinaConteudoVO) context().getExternalContext().getRequestMap().get("configuracaoItens");			
			setTurmaDisciplinaConteudoVO(obj);		
			setAnoSemestre(obj.getAno()+"/"+obj.getSemestre());
			List<UsuarioVO> resultado = null;
			resultado = getFacadeFactory().getUsuarioFacade().consultarPorCodigoUnico(getTurmaDisciplinaConteudoVO().getUsuarioVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			for(UsuarioVO list : resultado) {
				 getTurmaDisciplinaConteudoVO().setUsuarioPorExtenso(list.getUsername());
			}
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	// Getters and Setters
	public boolean getIsApresentarAno() {
		if (getTurmaDisciplinaConteudoVO().getTurmaVO().getCodigo() != 0) {
			if (getTurmaDisciplinaConteudoVO().getTurmaVO().getSemestral()) {
				getTurmaDisciplinaConteudoVO().setSemestre(Uteis.getSemestreAtual());
				return true;
			} else if (getTurmaDisciplinaConteudoVO().getTurmaVO().getAnual()) {
				return true;
			}
		}
		return false;
	}

	public boolean getIsApresentarSemestre() {
		if (getTurmaDisciplinaConteudoVO().getTurmaVO().getCodigo() != 0) {
			if (getTurmaDisciplinaConteudoVO().getTurmaVO().getSemestral()) {
				getTurmaDisciplinaConteudoVO().setSemestre(Uteis.getSemestreAtual());
				return true;
			}
		}
		return false;
	}

	public boolean getIsApresentarBotaoIncluirNovaConfiguracao() {
		return !getTurmaDisciplinaConteudoVO().getTurmaVO().getCodigo().equals(0);
	}

	public List<SelectItem> getCampoSemestreTurma() {
		List<SelectItem> objs = new ArrayList<SelectItem>(0);

		objs.add(new SelectItem("1", "1º"));
		objs.add(new SelectItem("2", "2º"));

		return objs;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade de Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public TurmaDisciplinaConteudoVO getTurmaDisciplinaConteudoVO() {
		if (turmaDisciplinaConteudoVO == null) {
			turmaDisciplinaConteudoVO = new TurmaDisciplinaConteudoVO();
		}
		return turmaDisciplinaConteudoVO;
	}

	public void setTurmaDisciplinaConteudoVO(TurmaDisciplinaConteudoVO turmaDisciplinaConteudoVO) {
		this.turmaDisciplinaConteudoVO = turmaDisciplinaConteudoVO;
	}

	public List<SelectItem> getListaSelectItemDisciplinasTurma() {
		if (listaSelectItemDisciplinasTurma == null) {
			listaSelectItemDisciplinasTurma = new ArrayList<SelectItem>();
		}
		return listaSelectItemDisciplinasTurma;
	}

	public void setListaSelectItemDisciplinasTurma(List<SelectItem> listaSelectItemDisciplinasTurma) {
		this.listaSelectItemDisciplinasTurma = listaSelectItemDisciplinasTurma;
	}

	public String getFecharModalPanelIncluir() {
		if (fecharModalPanelIncluir == null) {
			fecharModalPanelIncluir = "";
		}
		return fecharModalPanelIncluir;
	}

	public void setFecharModalPanelIncluir(String fecharModalPanelIncluir) {
		this.fecharModalPanelIncluir = fecharModalPanelIncluir;
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<SelectItem>();
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public Boolean getBuscarTurmasAnteriores() {
		if (buscarTurmasAnteriores == null) {
			buscarTurmasAnteriores = false;
		}
		return buscarTurmasAnteriores;
	}

	public void setBuscarTurmasAnteriores(Boolean buscarTurmasAnteriores) {
		this.buscarTurmasAnteriores = buscarTurmasAnteriores;
	}

	public List<SelectItem> getListaSelectItemConteudo() {
		if (listaSelectItemConteudo == null) {
			listaSelectItemConteudo = new ArrayList<SelectItem>();
		}
		return listaSelectItemConteudo;
	}

	public void setListaSelectItemConteudo(List<SelectItem> listaSelectItemConteudo) {
		this.listaSelectItemConteudo = listaSelectItemConteudo;
	}

	public List<SelectItem> getListaSelectItemAvaliacaOnline() {
		if (listaSelectItemAvaliacaOnline == null) {
			listaSelectItemAvaliacaOnline = new ArrayList<SelectItem>();
		}
		return listaSelectItemAvaliacaOnline;
	}

	public void setListaSelectItemAvaliacaOnline(List<SelectItem> listaSelectItemAvaliacaOnline) {
		this.listaSelectItemAvaliacaOnline = listaSelectItemAvaliacaOnline;
	}

	public String getUsuarioAlterouPorUltimo() {
		return usuarioAlterouPorUltimo;
	}

	public void setUsuarioAlterouPorUltimo(String usuarioAlterouPorUltimo) {
		this.usuarioAlterouPorUltimo = usuarioAlterouPorUltimo;
	}

	public Date getDataModificacao() {
		return dataModificacao;
	}

	public void setDataModificacao(Date dataModificacao) {
		this.dataModificacao = dataModificacao;
	}

	public String getAnoSemestre() {
		return anoSemestre;
	}

	public void setAnoSemestre(String anoSemestre) {
		this.anoSemestre = anoSemestre;
	}

	public String getUsuarioAlteracao() {
		return usuarioAlteracao;
	}

	public void setUsuarioAlteracao(String usuarioAlteracao) {
		this.usuarioAlteracao = usuarioAlteracao;
	}

	

	public MatriculaPeriodoTurmaDisciplinaVO getMatriculaperiodoVO() {
		return matriculaperiodoVO;
	}

	public void setMatriculaperiodoVO(MatriculaPeriodoTurmaDisciplinaVO matriculaperiodo) {
		this.matriculaperiodoVO = matriculaperiodo;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public String getMatriculaAlteracao() {
		return matriculaAlteracao;
	}

	public void setMatriculaAlteracao(String matriculaAlteracao) {
		this.matriculaAlteracao = matriculaAlteracao;
	}

	public String getRegistrosExcluidosLogAlteracao() {
		return registrosExcluidosLogAlteracao;
	}

	public void setRegistrosExcluidosLogAlteracao(String registrosExcluidosLogAlteracao) {
		this.registrosExcluidosLogAlteracao = registrosExcluidosLogAlteracao;
	}

	
	public void selecionarTodosDesmarcarTodos() {
		Iterator<MatriculaPeriodoTurmaDisciplinaVO> i = getControleConsulta().getListaConsulta3().iterator();
		while (i.hasNext()) {
			MatriculaPeriodoTurmaDisciplinaVO m = (MatriculaPeriodoTurmaDisciplinaVO) i.next();
			m.setSelecionarAlunoConteudoDiferente(getSelecionarTodos());
		}
	}
	
	
	/**
	 * @return the selecionarTodos
	 */
	public Boolean getSelecionarTodos() {
		if (selecionarTodos == null) {
			selecionarTodos = Boolean.FALSE;
		}
		return selecionarTodos;
	}

	/**
	 * @param selecionarTodos
	 *            the selecionarTodos to set
	 */
	public void setSelecionarTodos(Boolean selecionarTodos) {
		this.selecionarTodos = selecionarTodos;
	}

	
	
	public String  fecharModal() {
		return "#{rich:component('panelConfirmarExclusaoConteudoDiferente')}.hide();";
		
	}

	public String getFecharModalPainelALunosConteudoDiferente() {
		return fecharModalPainelALunosConteudoDiferente;
	}

	public void setFecharModalPainelALunosConteudoDiferente(String fecharModalPainelALunosConteudoDiferente) {
		this.fecharModalPainelALunosConteudoDiferente = fecharModalPainelALunosConteudoDiferente;
	}

	
}
