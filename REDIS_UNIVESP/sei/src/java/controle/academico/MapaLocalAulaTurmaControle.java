package controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.LocalAulaVO;
import negocio.comuns.academico.MapaLocalAulaTurmaVO;
import negocio.comuns.academico.SalaLocalAulaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("MapaLocalAulaTurmaControle")
@Lazy
@Scope("viewScope")
public class MapaLocalAulaTurmaControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -761814048905689780L;
	private Integer unidadeEnsinoVO;
	private TurmaVO turmaVO;
	private CursoVO cursoVO;
	private DisciplinaVO disciplinaVO;
	private List<SelectItem> listaSelectItemDisciplina;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private Date dataInicio;
	private Date dataTermino;
	private List<UnidadeEnsinoCursoVO> listaConsultaCurso;
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private String valorConsultaDisciplina;
	private String campoConsultaDisciplina;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List<MapaLocalAulaTurmaVO> mapaLocalAulaTurmaVOs;
	private List<SelectItem> listaSelectItemLocalAula;
	private Map<Integer, List<SelectItem>> listaSelectItemSalaLocalAula;
	private Boolean permiteModificarDados;

	public MapaLocalAulaTurmaControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		verificaPermissaoModificarDados();
		setMensagemID("msg_entre_prmconsulta");
	}
	
    public void verificaPermissaoModificarDados() {
        Boolean liberar = false;
        try {
        	ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("MapaLocalAulaTurma_PermiteModificarDados", getUsuarioLogado());
            liberar = true;
        } catch (Exception e) {
            liberar = false;
        }
        this.setPermiteModificarDados(liberar);
    }
	
	public void persistir() {
		try {
			MapaLocalAulaTurmaVO mapaLocalAulaTurmaVO = (MapaLocalAulaTurmaVO) getRequestMap().get("mapaLocalAulaTurmaItens");
			mapaLocalAulaTurmaVO.getTurmaDisciplina().setTurma(mapaLocalAulaTurmaVO.getTurma().getCodigo().intValue());
			getFacadeFactory().getTurmaDisciplinaFacade().alterarLocalSala(mapaLocalAulaTurmaVO.getTurmaDisciplina(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	public void notificar() {
		try {
			MapaLocalAulaTurmaVO mapaLocalAulaTurmaVO = (MapaLocalAulaTurmaVO) getRequestMap().get("mapaLocalAulaTurmaItens");
                        getFacadeFactory().getTurmaFacade().carregarDados(mapaLocalAulaTurmaVO.getTurma(), getUsuarioLogado());
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoLocalAulaTurma(mapaLocalAulaTurmaVO, getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getGrupoDestinatarioMapaLocalAula().getCodigo(), getUsuarioLogado());
			setMensagemID("msg_msg_emailsEnviados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarDadosMapa() {
		try {
			getMapaLocalAulaTurmaVOs().clear();
			setMapaLocalAulaTurmaVOs(getFacadeFactory().getTurmaDisciplinaFacade().consultarMapaLocalAulaTurma(getUnidadeEnsinoVO(), getDataInicio(), getDataTermino(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), getDisciplinaVO().getCodigo()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void limparLocalAula(){
		MapaLocalAulaTurmaVO mapaLocalAulaTurmaVO = (MapaLocalAulaTurmaVO) getRequestMap().get("mapaLocalAulaTurmaItens");
		mapaLocalAulaTurmaVO.getTurmaDisciplina().setSalaLocalAula(null);
	}

	private List<SelectItem> tipoConsultaComboDisciplina;

	public List<SelectItem> getTipoConsultaComboDisciplina() {
		if (tipoConsultaComboDisciplina == null) {
			tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
			tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboDisciplina;
	}

	public void consultarDisciplina() {
		try {
			getListaConsultaDisciplina().clear();
			setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorNomeCursoTurma(getValorConsultaDisciplina(), getCursoVO().getCodigo(), getTurmaVO().getCodigo(), getUnidadeEnsinoVO(), false, Uteis.NIVELMONTARDADOS_PROCESSAMENTO, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplina() {
		setDisciplinaVO((DisciplinaVO) getRequestMap().get("disciplinaItens"));
	}

	public void limparDisciplina() {
		setDisciplinaVO(null);
	}

	public void consultarTurma() {
		try {

			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoVO(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoVO(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoVO(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			setTurmaVO((TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens"));
			inicializarDadosTurma();
			if(Uteis.isAtributoPreenchido(getTurmaVO())) {
				setUnidadeEnsinoVO(getTurmaVO().getUnidadeEnsino().getCodigo());
			}
			limparMensagem();
		} catch (Exception e) {
			limparTurma();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<SelectItem> tipoConsultaComboTurma;

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
			tipoConsultaComboTurma.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
			tipoConsultaComboTurma.add(new SelectItem("nomeTurno", "Turno"));
			tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboTurma;
	}

	public void inicializarDadosTurma() throws Exception {
//		List<TurmaDisciplinaVO> turmaDisciplinaVOs = getFacadeFactory().getTurmaDisciplinaFacade().consultarPorCodigoTurma(getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
//		getListaSelectItemDisciplina().clear();
//		getListaSelectItemDisciplina().add(new SelectItem(0, ""));
//		for (TurmaDisciplinaVO turmaDisciplinaVO : turmaDisciplinaVOs) {
//			getListaSelectItemDisciplina().add(new SelectItem(turmaDisciplinaVO.getDisciplina().getCodigo(), turmaDisciplinaVO.getDisciplina().getNome()));
//		}
	}

	public void consultarTurmaPorIdentificador() {
		try {
			setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurmaVO(), getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoVO(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			inicializarDadosTurma();
			if(Uteis.isAtributoPreenchido(getTurmaVO())) {
				setUnidadeEnsinoVO(getTurmaVO().getUnidadeEnsino().getCodigo());
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			limparTurma();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparTurma() {
		setTurmaVO(null);
		setDisciplinaVO(null);
		getListaSelectItemDisciplina().clear();
		getListaConsultaDisciplina().clear();
		getMapaLocalAulaTurmaVOs().clear();
	}

	public void limparCurso() {
		setCursoVO(null);
		getListaConsultaDisciplina().clear();
		getMapaLocalAulaTurmaVOs().clear();
	}

	private List<SelectItem> tipoConsultaComboCurso;

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboCurso;
	}

	public void alterarUnidadeEnsino() {
		getListaConsultaDisciplina().clear();
		getListaConsultaCurso().clear();
		getListaConsultaTurma().clear();
		limparCurso();
		limparTurma();
		getMapaLocalAulaTurmaVOs().clear();
		
	}

	public void consultarCurso() {
		try {
			List<UnidadeEnsinoCursoVO> objs = new ArrayList<UnidadeEnsinoCursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidade(valorInt, getUnidadeEnsinoVO(), getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
			setCursoVO(unidadeEnsinoCurso.getCurso());
			if (!getTurmaVO().getCurso().getCodigo().equals(getCursoVO().getCodigo())) {
				limparTurma();
			}
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<UnidadeEnsinoCursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<UnidadeEnsinoCursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
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

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>();
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
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

	public String getCampoConsultaTurma() {
		if (campoConsultaTurma == null) {
			campoConsultaTurma = "";
		}
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public Integer getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = 0;
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(Integer unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
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

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
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

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
			listaSelectItemUnidadeEnsino.add(new SelectItem(0, ""));
			try {
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				for (UnidadeEnsinoVO obj : unidadeEnsinoVOs) {					
					listaSelectItemUnidadeEnsino.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
			} catch (Exception e) {

			}
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = new Date();
		}
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataTermino() {
		if (dataTermino == null) {
			dataTermino = new Date();
		}
		return dataTermino;
	}

	public void setDataTermino(Date dataTermino) {
		this.dataTermino = dataTermino;
	}

	public List<MapaLocalAulaTurmaVO> getMapaLocalAulaTurmaVOs() {
		if (mapaLocalAulaTurmaVOs == null) {
			mapaLocalAulaTurmaVOs = new ArrayList<MapaLocalAulaTurmaVO>(0);
		}
		return mapaLocalAulaTurmaVOs;
	}

	public void setMapaLocalAulaTurmaVOs(List<MapaLocalAulaTurmaVO> mapaLocalAulaTurmaVOs) {
		this.mapaLocalAulaTurmaVOs = mapaLocalAulaTurmaVOs;
	}

	public List<DisciplinaVO> getListaConsultaDisciplina() {
		if (listaConsultaDisciplina == null) {
			listaConsultaDisciplina = new ArrayList<DisciplinaVO>(0);
		}
		return listaConsultaDisciplina;
	}

	public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
		this.listaConsultaDisciplina = listaConsultaDisciplina;
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

	public String getCampoConsultaDisciplina() {
		if (campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public List<SelectItem> getListaSelectItemLocalAula() {
		if (listaSelectItemLocalAula == null) {
			listaSelectItemLocalAula = new ArrayList<SelectItem>(0);
			try {
				List<LocalAulaVO> localAulaVOs = getFacadeFactory().getLocalAulaFacade().consultaLocalSalaAulaPorSituacao(StatusAtivoInativoEnum.ATIVO, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				listaSelectItemLocalAula.add(new SelectItem(0, ""));
				for (LocalAulaVO localAulaVO : localAulaVOs) {
					//System.out.print(" => " + localAulaVO.getCodigo() + " - " + localAulaVO.getLocal());
					listaSelectItemLocalAula.add(new SelectItem(localAulaVO.getCodigo(), localAulaVO.getLocal()));
				}
			} catch (Exception e) {
				listaSelectItemLocalAula = new ArrayList<SelectItem>(0);
				listaSelectItemLocalAula.add(new SelectItem(0, ""));
			}
		}
		return listaSelectItemLocalAula;
	}

	public void setListaSelectItemLocalAula(List<SelectItem> listaSelectItemLocalAula) {
		this.listaSelectItemLocalAula = listaSelectItemLocalAula;
	}

	public List<SelectItem> getListaSelectItemSalaLocalAula() {
		if (listaSelectItemSalaLocalAula == null) {
			listaSelectItemSalaLocalAula = new HashMap<Integer, List<SelectItem>>(0);
		}
		MapaLocalAulaTurmaVO mapaLocalAulaTurmaVO = (MapaLocalAulaTurmaVO) getRequestMap().get("mapaLocalAulaTurmaItens");
		if (!listaSelectItemSalaLocalAula.containsKey(mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getCodigo())) {
			if (mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getCodigo() == 0) {
				listaSelectItemSalaLocalAula.put(0, new ArrayList<SelectItem>(0));
			} else {
				try {
					LocalAulaVO localAulaVO = getFacadeFactory().getLocalAulaFacade().consultarPorChavePrimaria(mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
					if (localAulaVO.getSituacao().equals(StatusAtivoInativoEnum.ATIVO)) {
						List<SelectItem> listaSelectItem = new ArrayList<SelectItem>(0);
						listaSelectItem.add(new SelectItem(0, ""));
						for (SalaLocalAulaVO salaLocalAulaVO : localAulaVO.getSalaLocalAulaVOs()) {
							listaSelectItem.add(new SelectItem(salaLocalAulaVO.getCodigo(), salaLocalAulaVO.getSala()));
						}
						listaSelectItemSalaLocalAula.put(mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getCodigo(), listaSelectItem);
					} else {
						mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().setCodigo(0);
						listaSelectItemSalaLocalAula.put(0, new ArrayList<SelectItem>(0));
					}
				} catch (Exception e) {
					List<SelectItem> listaSelectItem = new ArrayList<SelectItem>(0);
					listaSelectItem.add(new SelectItem(0, ""));
					listaSelectItemSalaLocalAula.put(mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getCodigo(), listaSelectItem);
				}
			}
		}
		return listaSelectItemSalaLocalAula.get(mapaLocalAulaTurmaVO.getTurmaDisciplina().getLocalAula().getCodigo());
	}

	public void setListaSelectItemSalaLocalAula(Map<Integer, List<SelectItem>> listaSelectItemSalaLocalAula) {
		this.listaSelectItemSalaLocalAula = listaSelectItemSalaLocalAula;
	}
	public Boolean getPermiteModificarDados() {
		if (permiteModificarDados == null) {
			permiteModificarDados = Boolean.FALSE;
		}
		return permiteModificarDados;
	}
	public void setPermiteModificarDados(Boolean permiteModificarDados) {
		this.permiteModificarDados = permiteModificarDados;
	}

}
