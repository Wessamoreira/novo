package controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaDisciplinaVO;
import negocio.comuns.academico.GradeCurricularGrupoOptativaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaCursadaVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaMatrizCurricularVO;
import negocio.comuns.academico.MapaEquivalenciaDisciplinaVO;
import negocio.comuns.academico.MapaEquivalenciaMatrizCurricularVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.enumeradores.SituacaoMapaEquivalenciaEnum;
import negocio.comuns.academico.enumeradores.TipoRegraAnoSemestreEquivalenciaEnum;
import negocio.comuns.academico.enumeradores.TipoRegraCargaHorariaEquivalenciaEnum;
import negocio.comuns.academico.enumeradores.TipoRegraFrequenciaEquivalenciaEnum;
import negocio.comuns.academico.enumeradores.TipoRegraNotaEquivalenciaEnum;
import negocio.comuns.academico.enumeradores.TipoRegraPeriodoLetivoEnum;
import negocio.comuns.academico.enumeradores.TipoRelacionamentoDisciplinaEquivalenciaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.academico.MapaEquivalenciaMatrizCurricular;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("MapaEquivalenciaMatrizCurricularControle")
@Scope("viewScope")
@Lazy
public class MapaEquivalenciaMatrizCurricularControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2485219540895385773L;

	private MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO;
	private MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO;
	private MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO;
	private MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO;
	private List<DisciplinaVO> listaConsultaDisciplina;
	private List<DisciplinaVO> listaDisciplinaSemEquivalencia;
	private String valorConsultaDisciplina;
	private String campoConsultaDisciplina;
	private List<CursoVO> listaConsultaCurso;
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private List<SelectItem> listaSelectItemOpcaoConsulta;
	private List<SelectItem> listaSelectItemGradeCurricular;
	private List<SelectItem> listaSelectItemOpcaoConsultaCurso;
	private List<SelectItem> listaSelectItemOpcaoConsultaDisciplina;
	private List<SelectItem> listaSelectItemPeriodoLetivo;
	private List<SelectItem> listaSelectItemGrupoOptativa;
	private List<SelectItem> listaSelectItemTipoConsultaDisciplina;
	private List<SelectItem> listaSelectItemDisciplina;
	private List<SelectItem> listaSelectItemTipoRegraCargaHorariaEquivalencia;
	private List<SelectItem> listaSelectItemTipoRegraFrequenciaEquivalencia;
	private List<SelectItem> listaSelectItemTipoRegraNotaEquivalencia;
	private List<SelectItem> listaSelectItemTipoRegraAnoSemestreEquivalencia;
	private List<SelectItem> listaSelectItemTipoRelacionamentoDisciplinaEquivalencia;
	private List<SelectItem> listaSelectItemTipoRegraPeriodoLetivo;
	private Integer periodoLetivo;
	private Integer grupoOptativa;
	private Integer codigoOrigemDisciplina;
	private String tipoDisciplinaMatriz;
	private Integer filtroCodigoDisciplina;
	private String filtroNomeDisciplina;
	private String filtroOrigemDisciplina;
	private CursoVO cursoBuscaDisciplinaEquivalente;
	private GradeCurricularVO gradeBuscarDisciplinaEquivalente;
	private List<SelectItem> listaSelectItemGradeBuscarDisciplinaEquivalente;
	private List<SelectItem> listaSelectItemSituacaoConsulta;
	private String campoConsultaSituacao; 
	private String campoDisciplina;


	public String novo() {
		removerObjetoMemoria(this);
		setMapaEquivalenciaDisciplinaCursadaVO(null);
		setMapaEquivalenciaDisciplinaMatrizCurricularVO(null);
		setMapaEquivalenciaDisciplinaVO(null);
		setMapaEquivalenciaMatrizCurricularVO(null);
		setListaSelectItemDisciplina(null);
		setListaSelectItemPeriodoLetivo(null);
		setListaSelectItemGradeCurricular(null);
		setListaSelectItemGrupoOptativa(null);
		setListaSelectItemGradeBuscarDisciplinaEquivalente(null);  
		getListaDisciplinaSemEquivalencia();
		setPeriodoLetivo(0);
		setGrupoOptativa(0);
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("mapaEquivalenciaMatrizCurricularForm");
	}

	public String editar() {
		try {
			novo();
			setMapaEquivalenciaMatrizCurricularVO(getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().consultarPorChavePrimaria(((MapaEquivalenciaMatrizCurricularVO) getRequestMap().get("mapaEquivalenciaMatrizCurricularItem")).getCodigo(), NivelMontarDados.TODOS, false, getUsuarioLogado()));
			montarDadosGradeCurricular();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("mapaEquivalenciaMatrizCurricularForm");
	}

	public void clonar() {
		try {
			getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().clonar(getMapaEquivalenciaMatrizCurricularVO(), getUsuarioLogado());
			montarDadosGradeCurricular();
			setMensagemID("msg_dados_clonados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void persistir() {
		try {
			getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().persistir(getMapaEquivalenciaMatrizCurricularVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void ativar() {
		try {
			getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().realizarAtivacaoMapaEquivalencia(getMapaEquivalenciaMatrizCurricularVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inativar() {
		try {
			getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().realizarInativacaoMapaEquivalencia(getMapaEquivalenciaMatrizCurricularVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluir() {
		try {
			getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().excluir(getMapaEquivalenciaMatrizCurricularVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String irPaginaConsulta() {
//		setControleConsultaOtimizado(null);
		return Uteis.getCaminhoRedirecionamentoNavegacao("mapaEquivalenciaMatrizCurricularCons");
	}

	public String consultar() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().consultar(getControleConsultaOtimizado().getCampoConsulta(), getControleConsultaOtimizado().getValorConsulta(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, true, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getCampoConsultaSituacao(), getCampoConsultaDisciplina()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().consultarTotalRegistroEncontrado(getControleConsultaOtimizado().getCampoConsulta(), getControleConsultaOtimizado().getValorConsulta(), getCampoConsultaSituacao(), getCampoConsultaDisciplina()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("mapaEquivalenciaMatrizCurricularCons");
	}

	public void adicionarMapaEquivalenciaDisciplina() {
		try {
			getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().adicionarMapaEquivalenciaDisciplina(getMapaEquivalenciaMatrizCurricularVO(), getMapaEquivalenciaDisciplinaVO());
			setMapaEquivalenciaDisciplinaVO(new MapaEquivalenciaDisciplinaVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerMapaEquivalenciaDisciplina() {
		try {
			MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO = (MapaEquivalenciaDisciplinaVO) getRequestMap().get("mapaEquivalenciaDisciplinaItem");
			getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().removerMapaEquivalenciaDisciplina(getMapaEquivalenciaMatrizCurricularVO(), mapaEquivalenciaDisciplinaVO);
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarMapaEquivalenciaDisciplina() {
		try {
			MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO = (MapaEquivalenciaDisciplinaVO) getRequestMap().get("mapaEquivalenciaDisciplinaItem");
			setMapaEquivalenciaDisciplinaVO(new MapaEquivalenciaDisciplinaVO());
			setMapaEquivalenciaDisciplinaVO((MapaEquivalenciaDisciplinaVO) mapaEquivalenciaDisciplinaVO.clone());
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarInativacaoMapaEquivalenciaDisciplina() throws Exception {
		try {
			getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().realizarInativacaoMapaEquivalenciaDisciplina((MapaEquivalenciaDisciplinaVO) getRequestMap().get("mapaEquivalenciaDisciplinaItem"), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarAtivacaoMapaEquivalenciaDisciplina() throws Exception {
		try {
			getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().realizarAtivacaoMapaEquivalenciaDisciplina((MapaEquivalenciaDisciplinaVO) getRequestMap().get("mapaEquivalenciaDisciplinaItem"), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarDisciplinaSemEquivalencia() {
		try {
			setFiltroCodigoDisciplina(0);
			setFiltroNomeDisciplina("");
			setFiltroOrigemDisciplina("");
			// if(getListaDisciplinaSemEquivalencia().isEmpty()){
			setListaDisciplinaSemEquivalencia(getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().realizarVerificacaoDisciplinaNaoRealizadoEquivalencia(getMapaEquivalenciaMatrizCurricularVO()));
			// }
			// for (MapaEquivalenciaDisciplinaMatrizCurricularVO obj :
			// getMapaEquivalenciaDisciplinaVO().getMapaEquivalenciaDisciplinaMatrizCurricularVOs())
			// {
			// int index = 0;
			// for (DisciplinaVO disciplinaVO2 :
			// getListaDisciplinaSemEquivalencia()) {
			// if (disciplinaVO2.getCodigo().intValue() ==
			// obj.getDisciplinaVO().getCodigo().intValue()) {
			// getListaDisciplinaSemEquivalencia().remove(index);
			// break;
			// }
			// index++;
			// }
			// }
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarMapaEquivalenciaDisciplinaMatrizCurricularSemEquivalencia() {
		try {
			setMapaEquivalenciaDisciplinaMatrizCurricularVO(new MapaEquivalenciaDisciplinaMatrizCurricularVO());
			DisciplinaVO disciplinaVO = (DisciplinaVO) getRequestMap().get("disciplinaItem");
			getMapaEquivalenciaDisciplinaMatrizCurricularVO().setDisciplinaVO(disciplinaVO);
			getMapaEquivalenciaDisciplinaMatrizCurricularVO().setCargaHoraria(disciplinaVO.getCargaHorariaPrevista());
			getMapaEquivalenciaDisciplinaMatrizCurricularVO().setNumeroCredito(disciplinaVO.getNumeroCreditoPrevisto());
			getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().adicionarMapaEquivalenciaDisciplinaMatrizCurricularVOs(getMapaEquivalenciaMatrizCurricularVO(), getMapaEquivalenciaDisciplinaVO(), getMapaEquivalenciaDisciplinaMatrizCurricularVO());
			setMapaEquivalenciaDisciplinaMatrizCurricularVO(new MapaEquivalenciaDisciplinaMatrizCurricularVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
			// int index = 0;
			// for (DisciplinaVO disciplinaVO2 :
			// getListaDisciplinaSemEquivalencia()) {
			// if (disciplinaVO2.getCodigo().intValue() ==
			// disciplinaVO.getCodigo().intValue()) {
			// getListaDisciplinaSemEquivalencia().remove(index);
			// break;
			// }
			// index++;
			// }
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarMapaEquivalenciaDisciplinaMatrizCurricular() {
		try {
			getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().adicionarMapaEquivalenciaDisciplinaMatrizCurricularVOs(getMapaEquivalenciaMatrizCurricularVO(), getMapaEquivalenciaDisciplinaVO(), getMapaEquivalenciaDisciplinaMatrizCurricularVO(), getTipoDisciplinaMatriz(), getCodigoOrigemDisciplina());
			setCodigoOrigemDisciplina(0);
			setMapaEquivalenciaDisciplinaMatrizCurricularVO(new MapaEquivalenciaDisciplinaMatrizCurricularVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerMapaEquivalenciaDisciplinaMatrizCurricular() {
		try {
			getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().removerMapaEquivalenciaDisciplinaMatrizCurricularVOs(getMapaEquivalenciaDisciplinaVO(), (MapaEquivalenciaDisciplinaMatrizCurricularVO) getRequestMap().get("mapaEquivalenciaDisciplinaMatrizCurricularItem"));
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarMapaEquivalenciaDisciplinaCursada() {
		try {
			DisciplinaVO disciplinaVO = (DisciplinaVO) getRequestMap().get("disciplinaItem");
			if (disciplinaVO.getCargaHorariaPrevista().equals(0)) {
				throw new Exception("Informe a carga horária da disciplina para adicioná-la como equivalente");
			}
			if (getFacadeFactory().getMapaEquivalenciaMatrizCurricularFacade().realizarVerificacaoDisciplinaEquivalente(getMapaEquivalenciaMatrizCurricularVO().getGradeCurricular().getCodigo(), disciplinaVO.getCodigo())) {
				throw new Exception( "A disciplina selecionada faz parte da matriz curricular deste mapa de equivalência, sendo assim não é possível incluir esta disciplina.");
			}
			getMapaEquivalenciaDisciplinaCursadaVO().setDisciplinaVO(disciplinaVO);
			getMapaEquivalenciaDisciplinaCursadaVO().setCargaHoraria(disciplinaVO.getCargaHorariaPrevista());
			getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().adicionarMapaEquivalenciaDisciplinaCursadaVOs(getMapaEquivalenciaMatrizCurricularVO(), getMapaEquivalenciaDisciplinaVO(), getMapaEquivalenciaDisciplinaCursadaVO());
			setMapaEquivalenciaDisciplinaCursadaVO(new MapaEquivalenciaDisciplinaCursadaVO());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerMapaEquivalenciaDisciplinaCursada() {
		try {
			getFacadeFactory().getMapaEquivalenciaDisciplinaFacade().removerMapaEquivalenciaDisciplinaCursadaVOs(getMapaEquivalenciaDisciplinaVO(), (MapaEquivalenciaDisciplinaCursadaVO) getRequestMap().get("mapaEquivalenciaDisciplinaCursadaItem"));
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarCurso() {
		getListaConsultaCurso().clear();
		try {
			setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultar(getCampoConsultaCurso(), getValorConsultaCurso(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void selecionarCurso() {
		try {
			CursoVO curso = (CursoVO) getRequestMap().get("cursoItem");
			if (curso.getCodigo().intValue() != getMapaEquivalenciaMatrizCurricularVO().getCurso().getCodigo()) {

				getMapaEquivalenciaMatrizCurricularVO().setCurso(curso);
				getMapaEquivalenciaMatrizCurricularVO().setGradeCurricular(null);
				getMapaEquivalenciaMatrizCurricularVO().setMapaEquivalenciaDisciplinaVOs(null);
				montarDadosGradeCurricular();

			}
			getListaConsultaCurso().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarDisciplina() {
		getListaConsultaDisciplina().clear();
		try {
			if (getCampoConsultaDisciplina().equals("abreviatura")) {
				setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorAbreviatura(getValorConsultaDisciplina(), getCursoBuscaDisciplinaEquivalente().getCodigo(), getGradeBuscarDisciplinaEquivalente().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			} else if (getCampoConsultaDisciplina().equals("nome")) {
				setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), getCursoBuscaDisciplinaEquivalente().getCodigo(), getGradeBuscarDisciplinaEquivalente().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			} else {
				if (getValorConsultaDisciplina().trim().isEmpty()) {
					setValorConsultaDisciplina("0");
				}
				Integer codigo = Integer.valueOf(getValorConsultaDisciplina().trim());
				setListaConsultaDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(codigo, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (NumberFormatException e) {
			setMensagemDetalhada("msg_erro", "Informe apenas número.", Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarDadosGradeCurricular() {
		try {
			List<GradeCurricularVO> gradeCurricularVOs = getFacadeFactory().getGradeCurricularFacade().consultarGradeCurriculars(getMapaEquivalenciaMatrizCurricularVO().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemGradeCurricular().clear();
			for (GradeCurricularVO obj : gradeCurricularVOs) {
				getListaSelectItemGradeCurricular().add(new SelectItem(obj.getCodigo(), obj.getCodigo() + " - " + obj.getNome() + " ( " + obj.getSituacao_Apresentar() + " ) "));
				if (getMapaEquivalenciaMatrizCurricularVO().getGradeCurricular().getCodigo() == 0) {
					getMapaEquivalenciaMatrizCurricularVO().getGradeCurricular().setCodigo(obj.getCodigo());
				}
			}
			getListaDisciplinaSemEquivalencia();
			alterarMatrizCurricular();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarMatrizCurricular() {
		if (getMapaEquivalenciaMatrizCurricularVO().getSituacao().equals(SituacaoMapaEquivalenciaEnum.EM_CONSTRUCAO)) {
			setTipoDisciplinaMatriz("periodoLetivo");
			getMapaEquivalenciaDisciplinaVO().getMapaEquivalenciaDisciplinaMatrizCurricularVOs().clear();
			setPeriodoLetivo(0);
			setGrupoOptativa(0);
			montarDadosPeriodoLetivo();
			montarDadosGrupoOptativa();
			montarDadosDisciplina();
		}
	}

	public void montarDadosPeriodoLetivo() {
		try {
			List<PeriodoLetivoVO> periodoLetivoVOs = getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(getMapaEquivalenciaMatrizCurricularVO().getGradeCurricular().getCodigo(), true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemPeriodoLetivo().clear();
			for (PeriodoLetivoVO obj : periodoLetivoVOs) {
				getListaSelectItemPeriodoLetivo().add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
				if (getPeriodoLetivo() == 0) {
					setPeriodoLetivo(obj.getCodigo());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarDadosGrupoOptativa() {
		try {
			List<GradeCurricularGrupoOptativaVO> gradeCurricularGrupoOptativaVOs = getFacadeFactory().getGradeCurricularGrupoOptativaFacade().consultarPorGradeCurricular(getMapaEquivalenciaMatrizCurricularVO().getGradeCurricular().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			getListaSelectItemGrupoOptativa().clear();
			for (GradeCurricularGrupoOptativaVO obj : gradeCurricularGrupoOptativaVOs) {
				getListaSelectItemGrupoOptativa().add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
				if (getGrupoOptativa() == 0) {
					setGrupoOptativa(obj.getCodigo());
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void montarDadosDisciplina() {
		try {
			getListaSelectItemDisciplina().clear();
			if (getTipoDisciplinaMatriz().equals("periodoLetivo")) {
				List<GradeDisciplinaVO> gradeDisciplinaVOs = getFacadeFactory().getGradeDisciplinaFacade().consultaRapidaGradeDisciplinaPorPeriodoLetivo(getPeriodoLetivo(), false, getUsuarioLogado());
				for (GradeDisciplinaVO obj : gradeDisciplinaVOs) {
					getListaSelectItemDisciplina().add(new SelectItem(obj.getCodigo(), obj.getDisciplina().getCodigo() + " - " + obj.getDisciplina().getNome() + "(" + obj.getCargaHoraria() + "Hs)"));
				}
			} else {
				List<GradeCurricularGrupoOptativaDisciplinaVO> gradeCurricularGrupoOptativaDisciplinaVOs = getFacadeFactory().getGradeCurricularGrupoOptativaDisciplinaFacade().consultarPorGradeCurricularGrupoOptativa(getGrupoOptativa(), getUsuarioLogado());
				for (GradeCurricularGrupoOptativaDisciplinaVO obj : gradeCurricularGrupoOptativaDisciplinaVOs) {
					getListaSelectItemDisciplina().add(new SelectItem(obj.getDisciplina().getCodigo(), obj.getDisciplina().getCodigo() + " - " + obj.getDisciplina().getNome() + "(" + obj.getCargaHoraria() + "Hs)"));
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void paginarConsulta(DataScrollEvent DataScrollEvent) {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultar();
	}

	public MapaEquivalenciaMatrizCurricularVO getMapaEquivalenciaMatrizCurricularVO() {
		if (mapaEquivalenciaMatrizCurricularVO == null) {
			mapaEquivalenciaMatrizCurricularVO = new MapaEquivalenciaMatrizCurricularVO();
		}
		return mapaEquivalenciaMatrizCurricularVO;
	}

	public void setMapaEquivalenciaMatrizCurricularVO(MapaEquivalenciaMatrizCurricularVO mapaEquivalenciaMatrizCurricularVO) {
		this.mapaEquivalenciaMatrizCurricularVO = mapaEquivalenciaMatrizCurricularVO;
	}

	public MapaEquivalenciaDisciplinaVO getMapaEquivalenciaDisciplinaVO() {
		if (mapaEquivalenciaDisciplinaVO == null) {
			mapaEquivalenciaDisciplinaVO = new MapaEquivalenciaDisciplinaVO();
		}
		return mapaEquivalenciaDisciplinaVO;
	}

	public void setMapaEquivalenciaDisciplinaVO(MapaEquivalenciaDisciplinaVO mapaEquivalenciaDisciplinaVO) {
		this.mapaEquivalenciaDisciplinaVO = mapaEquivalenciaDisciplinaVO;
	}

	public MapaEquivalenciaDisciplinaMatrizCurricularVO getMapaEquivalenciaDisciplinaMatrizCurricularVO() {
		if (mapaEquivalenciaDisciplinaMatrizCurricularVO == null) {
			mapaEquivalenciaDisciplinaMatrizCurricularVO = new MapaEquivalenciaDisciplinaMatrizCurricularVO();
		}
		return mapaEquivalenciaDisciplinaMatrizCurricularVO;
	}

	public void setMapaEquivalenciaDisciplinaMatrizCurricularVO(MapaEquivalenciaDisciplinaMatrizCurricularVO mapaEquivalenciaDisciplinaMatrizCurricularVO) {
		this.mapaEquivalenciaDisciplinaMatrizCurricularVO = mapaEquivalenciaDisciplinaMatrizCurricularVO;
	}

	public MapaEquivalenciaDisciplinaCursadaVO getMapaEquivalenciaDisciplinaCursadaVO() {
		if (mapaEquivalenciaDisciplinaCursadaVO == null) {
			mapaEquivalenciaDisciplinaCursadaVO = new MapaEquivalenciaDisciplinaCursadaVO();
		}
		return mapaEquivalenciaDisciplinaCursadaVO;
	}

	public void setMapaEquivalenciaDisciplinaCursadaVO(MapaEquivalenciaDisciplinaCursadaVO mapaEquivalenciaDisciplinaCursadaVO) {
		this.mapaEquivalenciaDisciplinaCursadaVO = mapaEquivalenciaDisciplinaCursadaVO;
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

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
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

	public List<SelectItem> getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemGradeCurricular;
	}

	public void setListaSelectItemGradeCurricular(List<SelectItem> listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>();
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public List<SelectItem> getListaSelectItemTipoRegraCargaHorariaEquivalencia() {
		if (listaSelectItemTipoRegraCargaHorariaEquivalencia == null) {
			listaSelectItemTipoRegraCargaHorariaEquivalencia = new ArrayList<SelectItem>(0);
			for (TipoRegraCargaHorariaEquivalenciaEnum obj : TipoRegraCargaHorariaEquivalenciaEnum.values()) {
				listaSelectItemTipoRegraCargaHorariaEquivalencia.add(new SelectItem(obj, obj.getValorApresentar()));
			}
		}
		return listaSelectItemTipoRegraCargaHorariaEquivalencia;
	}

	public void setListaSelectItemTipoRegraCargaHorariaEquivalencia(List<SelectItem> listaSelectItemTipoRegraCargaHorariaEquivalencia) {
		this.listaSelectItemTipoRegraCargaHorariaEquivalencia = listaSelectItemTipoRegraCargaHorariaEquivalencia;
	}

	public List<SelectItem> getListaSelectItemTipoRegraFrequenciaEquivalencia() {
		if (listaSelectItemTipoRegraFrequenciaEquivalencia == null) {
			listaSelectItemTipoRegraFrequenciaEquivalencia = new ArrayList<SelectItem>(0);
			for (TipoRegraFrequenciaEquivalenciaEnum obj : TipoRegraFrequenciaEquivalenciaEnum.values()) {
				listaSelectItemTipoRegraFrequenciaEquivalencia.add(new SelectItem(obj, obj.getValorApresentar()));
			}
		}
		return listaSelectItemTipoRegraFrequenciaEquivalencia;
	}

	public void setListaSelectItemTipoRegraFrequenciaEquivalencia(List<SelectItem> listaSelectItemTipoRegraFrequenciaEquivalencia) {
		this.listaSelectItemTipoRegraFrequenciaEquivalencia = listaSelectItemTipoRegraFrequenciaEquivalencia;
	}

	public List<SelectItem> getListaSelectItemTipoRegraNotaEquivalencia() {
		if (listaSelectItemTipoRegraNotaEquivalencia == null) {
			listaSelectItemTipoRegraNotaEquivalencia = new ArrayList<SelectItem>(0);
			for (TipoRegraNotaEquivalenciaEnum obj : TipoRegraNotaEquivalenciaEnum.values()) {
				listaSelectItemTipoRegraNotaEquivalencia.add(new SelectItem(obj, obj.getValorApresentar()));
			}
		}
		return listaSelectItemTipoRegraNotaEquivalencia;
	}

	public void setListaSelectItemTipoRegraNotaEquivalencia(List<SelectItem> listaSelectItemTipoRegraNotaEquivalencia) {
		this.listaSelectItemTipoRegraNotaEquivalencia = listaSelectItemTipoRegraNotaEquivalencia;
	}

	public List<SelectItem> getListaSelectItemTipoRegraAnoSemestreEquivalencia() {
		if (listaSelectItemTipoRegraAnoSemestreEquivalencia == null) {
			listaSelectItemTipoRegraAnoSemestreEquivalencia = new ArrayList<SelectItem>(0);
			for (TipoRegraAnoSemestreEquivalenciaEnum obj : TipoRegraAnoSemestreEquivalenciaEnum.values()) {
				listaSelectItemTipoRegraAnoSemestreEquivalencia.add(new SelectItem(obj, obj.getValorApresentar()));
			}
		}
		return listaSelectItemTipoRegraAnoSemestreEquivalencia;
	}

	public void setListaSelectItemTipoRegraAnoSemestreEquivalencia(List<SelectItem> listaSelectItemTipoRegraAnoSemestreEquivalencia) {
		this.listaSelectItemTipoRegraAnoSemestreEquivalencia = listaSelectItemTipoRegraAnoSemestreEquivalencia;
	}

	public List<SelectItem> getListaSelectItemTipoRelacionamentoDisciplinaEquivalencia() {
		if (listaSelectItemTipoRelacionamentoDisciplinaEquivalencia == null) {
			listaSelectItemTipoRelacionamentoDisciplinaEquivalencia = new ArrayList<SelectItem>(0);
			for (TipoRelacionamentoDisciplinaEquivalenciaEnum obj : TipoRelacionamentoDisciplinaEquivalenciaEnum.values()) {
				listaSelectItemTipoRelacionamentoDisciplinaEquivalencia.add(new SelectItem(obj, obj.getValorApresentar()));
			}
		}
		return listaSelectItemTipoRelacionamentoDisciplinaEquivalencia;
	}

	public void setListaSelectItemTipoRelacionamentoDisciplinaEquivalencia(List<SelectItem> listaSelectItemTipoRelacionamentoDisciplinaEquivalencia) {
		this.listaSelectItemTipoRelacionamentoDisciplinaEquivalencia = listaSelectItemTipoRelacionamentoDisciplinaEquivalencia;
	}

	public List<SelectItem> getListaSelectItemPeriodoLetivo() {
		if (listaSelectItemPeriodoLetivo == null) {
			listaSelectItemPeriodoLetivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPeriodoLetivo;
	}

	public void setListaSelectItemPeriodoLetivo(List<SelectItem> listaSelectItemPeriodoLetivo) {
		this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
	}

	public List<SelectItem> getListaSelectItemOpcaoConsultaCurso() {
		if (listaSelectItemOpcaoConsultaCurso == null) {
			listaSelectItemOpcaoConsultaCurso = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsultaCurso.add(new SelectItem("nome", "Nome"));
			listaSelectItemOpcaoConsultaCurso.add(new SelectItem("codigo", "Código"));
			listaSelectItemOpcaoConsultaCurso.add(new SelectItem("abreviatura", "Abreviatura"));
		}
		return listaSelectItemOpcaoConsultaCurso;
	}

	public void setListaSelectItemOpcaoConsultaCurso(List<SelectItem> listaSelectItemOpcaoConsultaCurso) {
		this.listaSelectItemOpcaoConsultaCurso = listaSelectItemOpcaoConsultaCurso;
	}

	public List<SelectItem> getListaSelectItemOpcaoConsultaDisciplina() {
		if (listaSelectItemOpcaoConsultaDisciplina == null) {
			listaSelectItemOpcaoConsultaDisciplina = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsultaDisciplina.add(new SelectItem("nome", "Nome Disciplina"));
			listaSelectItemOpcaoConsultaDisciplina.add(new SelectItem("codigo", "Código Disciplina"));
		}
		return listaSelectItemOpcaoConsultaDisciplina;
	}

	public void setListaSelectItemOpcaoConsultaDisciplina(List<SelectItem> listaSelectItemOpcaoConsultaDisciplina) {
		this.listaSelectItemOpcaoConsultaDisciplina = listaSelectItemOpcaoConsultaDisciplina;
	}

	public List<SelectItem> getListaSelectItemGrupoOptativa() {
		if (listaSelectItemGrupoOptativa == null) {
			listaSelectItemGrupoOptativa = new ArrayList<SelectItem>();
		}
		return listaSelectItemGrupoOptativa;
	}

	public void setListaSelectItemGrupoOptativa(List<SelectItem> listaSelectItemGrupoOptativa) {
		this.listaSelectItemGrupoOptativa = listaSelectItemGrupoOptativa;
	}

	public List<SelectItem> getListaSelectItemTipoConsultaDisciplina() {
		if (listaSelectItemTipoConsultaDisciplina == null) {
			listaSelectItemTipoConsultaDisciplina = new ArrayList<SelectItem>();
			listaSelectItemTipoConsultaDisciplina.add(new SelectItem("periodoLetivo", "Período Letivo"));
			listaSelectItemTipoConsultaDisciplina.add(new SelectItem("grupoOptativa", "Grupo Optativa"));
		}
		return listaSelectItemTipoConsultaDisciplina;
	}

	public void setListaSelectItemTipoConsultaDisciplina(List<SelectItem> listaSelectItemTipoConsultaDisciplina) {
		this.listaSelectItemTipoConsultaDisciplina = listaSelectItemTipoConsultaDisciplina;
	}

	public Integer getPeriodoLetivo() {
		if (periodoLetivo == null) {
			periodoLetivo = 0;
		}
		return periodoLetivo;
	}

	public void setPeriodoLetivo(Integer periodoLetivo) {
		this.periodoLetivo = periodoLetivo;
	}

	public Integer getGrupoOptativa() {
		if (grupoOptativa == null) {
			grupoOptativa = 0;
		}
		return grupoOptativa;
	}

	public void setGrupoOptativa(Integer grupoOptativa) {
		this.grupoOptativa = grupoOptativa;
	}

	public String getTipoDisciplinaMatriz() {
		if (tipoDisciplinaMatriz == null) {
			tipoDisciplinaMatriz = "periodoLetivo";
		}
		return tipoDisciplinaMatriz;
	}

	public void setTipoDisciplinaMatriz(String tipoDisciplinaMatriz) {
		this.tipoDisciplinaMatriz = tipoDisciplinaMatriz;
	}

	public List<DisciplinaVO> getListaDisciplinaSemEquivalencia() {
		if (listaDisciplinaSemEquivalencia == null) {
			listaDisciplinaSemEquivalencia = new ArrayList<DisciplinaVO>(0);
		}
		return listaDisciplinaSemEquivalencia;
	}

	public void setListaDisciplinaSemEquivalencia(List<DisciplinaVO> listaDisciplinaSemEquivalencia) {
		this.listaDisciplinaSemEquivalencia = listaDisciplinaSemEquivalencia;
	}

	public List<SelectItem> getListaSelectItemOpcaoConsulta() {
		if (listaSelectItemOpcaoConsulta == null) {
			listaSelectItemOpcaoConsulta = new ArrayList<SelectItem>(0);
			listaSelectItemOpcaoConsulta.add(new SelectItem("curso", "Curso"));
			listaSelectItemOpcaoConsulta.add(new SelectItem("matrizCurricular", "Matriz Curricular"));
			listaSelectItemOpcaoConsulta.add(new SelectItem("descricao", "Descrição"));
			getControleConsultaOtimizado().setCampoConsulta("curso");
		}
		return listaSelectItemOpcaoConsulta;
	}

	public void setListaSelectItemOpcaoConsulta(List<SelectItem> listaSelectItemOpcaoConsulta) {
		this.listaSelectItemOpcaoConsulta = listaSelectItemOpcaoConsulta;
	}

	public Integer getFiltroCodigoDisciplina() {
		if (filtroCodigoDisciplina == null) {
			filtroCodigoDisciplina = 0;
		}
		return filtroCodigoDisciplina;
	}

	public void setFiltroCodigoDisciplina(Integer filtroCodigoDisciplina) {
		this.filtroCodigoDisciplina = filtroCodigoDisciplina;
	}

	public String getFiltroNomeDisciplina() {
		if (filtroNomeDisciplina == null) {
			filtroNomeDisciplina = "";
		}
		return filtroNomeDisciplina;
	}

	public void setFiltroNomeDisciplina(String filtroNomeDisciplina) {
		this.filtroNomeDisciplina = filtroNomeDisciplina;
	}

	public String getFiltroOrigemDisciplina() {
		if (filtroOrigemDisciplina == null) {
			filtroOrigemDisciplina = "";
		}
		return filtroOrigemDisciplina;
	}

	public void setFiltroOrigemDisciplina(String filtroOrigemDisciplina) {
		this.filtroOrigemDisciplina = filtroOrigemDisciplina;
	}

	public boolean filtrarCodigoDisciplina(Object obj) {
		if (getFiltroCodigoDisciplina() > 0) {
			Ordenacao.ordenarLista(getListaDisciplinaSemEquivalencia(), "codigo");
			if (obj instanceof DisciplinaVO) {
				if (((DisciplinaVO) obj).getCodigo().intValue() >= getFiltroCodigoDisciplina().intValue()) {
					return true;
				}
				return false;
			} else {
				return false;
			}
		}
		return true;
	}

	public boolean filtrarNomeDisciplina(Object obj) {
		if (!getFiltroNomeDisciplina().trim().isEmpty()) {
			Ordenacao.ordenarLista(getListaDisciplinaSemEquivalencia(), "nome");
			if (obj instanceof DisciplinaVO) {
				if (Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(((DisciplinaVO) obj).getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltroNomeDisciplina().toUpperCase().trim())))) {
					return true;
				}
				return false;
			} else {
				return false;
			}
		}
		return true;
	}

	public boolean filtrarOrigemDisciplina(Object obj) {
		if (!getFiltroOrigemDisciplina().trim().isEmpty()) {
			if (obj instanceof DisciplinaVO) {
				if (Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(((DisciplinaVO) obj).getDescricaoPeriodoLetivo())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltroOrigemDisciplina().trim().toUpperCase())))) {
					return true;
				}
				return false;
			} else {
				return false;
			}
		}
		return true;
	}

	public List<SelectItem> getListaSelectItemTipoRegraPeriodoLetivo() {
		if (listaSelectItemTipoRegraPeriodoLetivo == null) {
			listaSelectItemTipoRegraPeriodoLetivo = new ArrayList<SelectItem>(0);
			listaSelectItemTipoRegraPeriodoLetivo.add(new SelectItem(TipoRegraPeriodoLetivoEnum.PERIODO_MAIS_ANTIGO, TipoRegraPeriodoLetivoEnum.PERIODO_MAIS_ANTIGO.getValorApresentar()));
			listaSelectItemTipoRegraPeriodoLetivo.add(new SelectItem(TipoRegraPeriodoLetivoEnum.PERIODO_MAIS_ATUAL, TipoRegraPeriodoLetivoEnum.PERIODO_MAIS_ATUAL.getValorApresentar()));
			listaSelectItemTipoRegraPeriodoLetivo.add(new SelectItem(TipoRegraPeriodoLetivoEnum.PERIODO_MATRIZ_CURRICULAR, TipoRegraPeriodoLetivoEnum.PERIODO_MATRIZ_CURRICULAR.getValorApresentar()));
		}
		return listaSelectItemTipoRegraPeriodoLetivo;
	}

	public void setListaSelectItemTipoRegraPeriodoLetivo(List<SelectItem> listaSelectItemTipoRegraPeriodoLetivo) {
		this.listaSelectItemTipoRegraPeriodoLetivo = listaSelectItemTipoRegraPeriodoLetivo;
	}

	public void imprimirPDF() {
		try {
			getSuperParametroRelVO().setNomeDesignIreport(MapaEquivalenciaMatrizCurricular.designIReportRelatorio());
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(MapaEquivalenciaMatrizCurricular.caminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio(UteisJSF.internacionalizar("Menu_mapaEquivalenciaMatrizCurricular"));
			getSuperParametroRelVO().getListaObjetos().clear();
			getSuperParametroRelVO().getListaObjetos().add(getMapaEquivalenciaMatrizCurricularVO());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(MapaEquivalenciaMatrizCurricular.caminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setNomeEmpresa(getUnidadeEnsinoLogado().getNome());
			realizarImpressaoRelatorio();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}

	}

	public Integer getCodigoOrigemDisciplina() {
		if (codigoOrigemDisciplina == null) {
			codigoOrigemDisciplina = 0;
		}
		return codigoOrigemDisciplina;
	}

	public void setCodigoOrigemDisciplina(Integer codigoOrigemDisciplina) {
		this.codigoOrigemDisciplina = codigoOrigemDisciplina;
	}
	
	public void limparCursoDisciplinaEquivalente() {
		setCursoBuscaDisciplinaEquivalente(new CursoVO());
		getListaConsultaDisciplina().clear();
	}
	
	public void selecionarCursoBuscaDisciplinaEquivalente() {
		try {
			CursoVO curso = (CursoVO) getRequestMap().get("cursoBuscaEquivalente");
			setCursoBuscaDisciplinaEquivalente(curso);
			montarDadosGradeBuscaDisciplinaEquivalente();
			setGradeBuscarDisciplinaEquivalente(null);
			limparDisciplinaEquivalente();
			getListaConsultaCurso().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void montarDadosGradeBuscaDisciplinaEquivalente() {
		try {
			List<GradeCurricularVO> gradeCurricularVOs = getFacadeFactory().getGradeCurricularFacade().consultarGradeCurriculars(getCursoBuscaDisciplinaEquivalente().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getListaSelectItemGradeBuscarDisciplinaEquivalente().clear();
			getListaSelectItemGradeBuscarDisciplinaEquivalente().add(new SelectItem(new Integer(0), " "));
			for (GradeCurricularVO obj : gradeCurricularVOs) {
				getListaSelectItemGradeBuscarDisciplinaEquivalente().add(new SelectItem(obj.getCodigo(), obj.getCodigo() + " - " +  obj.getNome()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}	
	
	public void limparDisciplinaEquivalente() {
		getListaConsultaDisciplina().clear();
	}

	public CursoVO getCursoBuscaDisciplinaEquivalente() {
		if (cursoBuscaDisciplinaEquivalente == null) {
			cursoBuscaDisciplinaEquivalente = new CursoVO();
		}
		return cursoBuscaDisciplinaEquivalente;
	}

	public void setCursoBuscaDisciplinaEquivalente(CursoVO cursoBuscaDisciplinaEquivalente) {
		this.cursoBuscaDisciplinaEquivalente = cursoBuscaDisciplinaEquivalente;
	}

	public GradeCurricularVO getGradeBuscarDisciplinaEquivalente() {
		if (gradeBuscarDisciplinaEquivalente == null) {
			gradeBuscarDisciplinaEquivalente = new GradeCurricularVO();
		}
		return gradeBuscarDisciplinaEquivalente;
	}

	public void setGradeBuscarDisciplinaEquivalente(GradeCurricularVO gradeBuscarDisciplinaEquivalente) {
		this.gradeBuscarDisciplinaEquivalente = gradeBuscarDisciplinaEquivalente;
	}
	
	public List<SelectItem> getListaSelectItemGradeBuscarDisciplinaEquivalente() {
		if (listaSelectItemGradeBuscarDisciplinaEquivalente == null) {
			listaSelectItemGradeBuscarDisciplinaEquivalente = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemGradeBuscarDisciplinaEquivalente;
	}

	public void setListaSelectItemGradeBuscarDisciplinaEquivalente(List<SelectItem> listaSelectItemGradeBuscarDisciplinaEquivalente) {
		this.listaSelectItemGradeBuscarDisciplinaEquivalente = listaSelectItemGradeBuscarDisciplinaEquivalente;
	}
	
	public boolean getApresentarBuscaCursoDisciplinaEquivalente() {
		if (getCampoConsultaDisciplina().equals("codigo")) {
			return false;	
		}
		return true;
	}
	
	public boolean getApresentarBuscaGradeCursoDisciplinaEquivalente() {
		if ((getApresentarBuscaCursoDisciplinaEquivalente()) &&
		    (!getCursoBuscaDisciplinaEquivalente().getCodigo().equals(0))) {
			return true;
		}
		return false;
	}	

	public List<SelectItem> getListaSelectItemSituacaoConsulta() {
		if (listaSelectItemSituacaoConsulta == null) {
			listaSelectItemSituacaoConsulta = new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoConsulta.add(new SelectItem("", ""));
			listaSelectItemSituacaoConsulta.add(new SelectItem("ATIVO", "Ativo"));
			listaSelectItemSituacaoConsulta.add(new SelectItem("EM_CONSTRUCAO", "Em Contrução"));
			listaSelectItemSituacaoConsulta.add(new SelectItem("INATIVO", "Inativo"));

		}
		return listaSelectItemSituacaoConsulta;
	}

	public void setListaSelectItemSituacaoConsulta(List<SelectItem> listaSelectItemSituacaoConsulta) {
		this.listaSelectItemSituacaoConsulta = listaSelectItemSituacaoConsulta;
	}

	public String getCampoConsultaSituacao() {
		if(campoConsultaSituacao == null) {
			campoConsultaSituacao = "";
		}
		return campoConsultaSituacao;
	}

	public void setCampoConsultaSituacao(String campoConsultaSituacao) {
		this.campoConsultaSituacao = campoConsultaSituacao;
	}

	public String getCampoDisciplina() {
		if(campoDisciplina == null) {
			campoDisciplina = "";
		}
		return campoDisciplina;
	}

	public void setCampoDisciplina(String campoDisciplina) {
		this.campoDisciplina = campoDisciplina;
	}
	
	
}
