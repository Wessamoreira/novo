package controle.secretaria;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.FilterFactory;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MapaRegistroEvasaoCursoMatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.secretaria.MapaRegistroEvasaoCursoVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoTrancamentoEnum;
import negocio.facade.jdbc.academico.MapaRegistroEvasaoCurso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 * @author Pedro - 19 de set de 2022
 *
 */
@Controller("MapaRegistroEvasaoCursoControle")
@Scope("viewScope")
public class MapaRegistroEvasaoCursoControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 9103613726027353023L;
	private static final String TELA_FORM = "mapaRegistroEvasaoCursoForm.xhtml";
	private static final String TELA_CONS = "mapaRegistroEvasaoCursoCons.xhtml";
	private static final String CONTEXT_PARA_EDICAO = "mapaRegistroOperacao";
	private MapaRegistroEvasaoCursoVO mapaRegistroEvasaoCursoVO;
	private MapaRegistroEvasaoCursoMatriculaPeriodoVO mapaRegistroEvasaoCursoMatriculaPeriodoVO;
	private String unidadeEnsinoApresentar;
	private String cursoApresentar;
	private String turnoApresentar;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemPeriodicidade;
	private List<SelectItem> listaSelectItemMotivoCancelamentoTrancamento;	
	private boolean matriculaSelecionada = false;
	private String filtroMatricula;
	private String filtroNomeAluno;
	private String filtroNomeUnidadeEnsino;
	private String filtroNomeCurso;
	private String filtroNomeTurma;
	private List<SelectItem> listaSelectItemTipoTrancamento;
	private String tipoTrancamento;
	
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private List<CursoVO> cursoVOs;
	
	private String valorConsultaTurno;
	private String campoConsultaTurno;
	private List<TurnoVO> listaConsultaTurno;
	
	private String valorConsultaUsuario;
	private String campoConsultaUsuario;
	private DataModelo controleConsultaUsuario;
	
	private String valorConsultaMatricula;
	private String campoConsultaMatricula;
	private List<MatriculaVO> matriculaVOs;
	private String campoNavegacaoOrigem;
	
	
	public MapaRegistroEvasaoCursoControle()  {
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
		getControleConsultaOtimizado().setDataFim(new Date());
		getControleConsultaOtimizado().setPage(1);
		getControleConsultaOtimizado().setPaginaAtual(1);
		montarListaSelectItemUnidadeEnsino();
		setMensagemID("msg_entre_prmconsulta");
	}
	
	public void  realizarNavegacaoOrigemMapaRegistroEvasaoCurso() {
		try {
			MapaRegistroEvasaoCursoMatriculaPeriodoVO obj = (MapaRegistroEvasaoCursoMatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("mapaRegistroMatriculaPeriodoItem");
			if(Uteis.isAtributoPreenchido(obj.getCancelamentoVO())) {
				context().getExternalContext().getSessionMap().put("cancelamento", obj.getCancelamentoVO());
				removerControleMemoriaFlash("CancelamentoControle");
				removerControleMemoriaTela("CancelamentoControle");
				setCampoNavegacaoOrigem("popup('../academico/cancelamentoForm.xhtml', 'cancelamentoForm' , 1024, 800)");
			}else {
				context().getExternalContext().getSessionMap().put("trancamento", obj.getTrancamentoVO());
				removerControleMemoriaFlash("TrancamentoControle");
				removerControleMemoriaTela("TrancamentoControle");
				setCampoNavegacaoOrigem("popup('../academico/trancamentoForm.xhtml', 'trancamentoForm' , 1024, 800)");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setCampoNavegacaoOrigem("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	

	public String novo() {
		removerObjetoMemoria(this);
		setMapaRegistroEvasaoCursoVO(new MapaRegistroEvasaoCursoVO());
		getMapaRegistroEvasaoCursoVO().getUsuarioResponsavel().setCodigo(getUsuarioLogado().getCodigo());
		getMapaRegistroEvasaoCursoVO().getUsuarioResponsavel().setNome(getUsuarioLogado().getNome());
		getMapaRegistroEvasaoCursoVO().setDataRegistro(new Date());
		consultarUnidadeEnsinoFiltroRelatorio("", Boolean.FALSE);
		verificarTodasUnidadesSelecionadas();
		montarTodosListaSelectItem();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}
	
	public String editar() {
		try {
			MapaRegistroEvasaoCursoVO obj = (MapaRegistroEvasaoCursoVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setMapaRegistroEvasaoCursoVO(getFacadeFactory().getMapaRegistroEvasaoCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setTurnoVOs(null);
			setCursoVOs(null);
			consultarUnidadeEnsinoFiltroRelatorio("");
			getUnidadeEnsinoVOs().stream().forEach(p -> p.setFiltrarUnidadeEnsino(false));
			getMapaRegistroEvasaoCursoVO().getUnidadeEnsinoVOs().stream().forEach(p -> getUnidadeEnsinoVOs().stream().filter(ue -> ue.getCodigo().equals(p.getCodigo())).forEach(ue -> ue.setFiltrarUnidadeEnsino(true)));
			verificarTodasUnidadesSelecionadas();
			getMapaRegistroEvasaoCursoVO().getCursoVOs().stream().forEach(p -> getCursoVOs().stream().filter(curso -> curso.getCodigo().equals(p.getCodigo())).forEach(curso -> curso.setFiltrarCursoVO(true)));
			getMapaRegistroEvasaoCursoVO().getTurnoVOs().stream().forEach(p -> getTurnoVOs().stream().filter(turno -> turno.getCodigo().equals(p.getCodigo())).forEach(turno -> turno.setFiltrarTurnoVO(true)));
			verificarTodosCursosSelecionados();
			verificarTodosTurnosSelecionados();
			montarTodosListaSelectItem();
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}
	
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setDataIni(Uteis.getDataPrimeiroDiaMes(new Date()));
		getControleConsultaOtimizado().setDataFim(new Date());		
		getControleConsultaOtimizado().setPage(1);
		getControleConsultaOtimizado().setPaginaAtual(1);
		setMapaRegistroEvasaoCursoVO(new MapaRegistroEvasaoCursoVO());
		getMapaRegistroEvasaoCursoVO().setDataRegistro(new Date());
		montarListaSelectItemUnidadeEnsino();
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);

	}
	
	public String consultar() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getMapaRegistroEvasaoCursoFacade().consultar(getControleConsultaOtimizado(), getMapaRegistroEvasaoCursoVO());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}
	
	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultar();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarMatriculaPeriodoAptaRegistro() {
		try {
			getMapaRegistroEvasaoCursoVO().getUnidadeEnsinoVOs().clear();
			getMapaRegistroEvasaoCursoVO().getCursoVOs().clear();
			getMapaRegistroEvasaoCursoVO().getTurnoVOs().clear();
			getMapaRegistroEvasaoCursoVO().setUnidadeEnsinoVOs(getUnidadeEnsinoVOMarcadasParaSeremUtilizadas());
			getMapaRegistroEvasaoCursoVO().setCursoVOs(getCursoVOsMarcadosParaSeremUtilizados());
			getMapaRegistroEvasaoCursoVO().setTurnoVOs(getTurnoVOsMarcadosParaSeremUtilizados());
			setMatriculaSelecionada(true);
			Uteis.checkState(!Uteis.isAtributoPreenchido(getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum()), UteisJSF.internacionalizar("msg_MapaRegistroAbandonoCursoTrancamento_tipoTrancamento"));
			Uteis.checkState(!Uteis.isAtributoPreenchido(getMapaRegistroEvasaoCursoVO().getAno()), UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_ano"));
			Uteis.checkState(!getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isRenovacaoAutomatica() && !Uteis.isAtributoPreenchido(getMapaRegistroEvasaoCursoVO().getAnoRegistroEvasao()), "O ANO registro de evasão deve ser informada");
			Uteis.checkState(!getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isRenovacaoAutomatica() && getMapaRegistroEvasaoCursoVO().getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL.getValor()) && !Uteis.isAtributoPreenchido(getMapaRegistroEvasaoCursoVO().getSemestreRegistroEvasao()), "O SEMESTRE registro de evasão deve ser informada");
			Uteis.checkState(getApresentarCampoSemestre() && !Uteis.isAtributoPreenchido(getMapaRegistroEvasaoCursoVO().getSemestre()), UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_semestre"));
			Uteis.checkState(!getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isRenovacaoAutomatica() && !Uteis.isAtributoPreenchido(getMapaRegistroEvasaoCursoVO().getJustificativa()), "O campo Justificativa deve ser informado.");
			Uteis.checkState(!getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isRenovacaoAutomatica() && !Uteis.isAtributoPreenchido(getMapaRegistroEvasaoCursoVO().getMotivoCancelamentoTrancamento()), "O campo Motivo Cancelamento/Trancamento deve ser informado.");
			Uteis.checkState(getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isRenovacaoAutomatica() && getMapaRegistroEvasaoCursoVO().getQtdMesAlunosRenovacaoSemAcessoAva().equals(0), "O campo Quantidade de Dias para Alunos com Renovação Automática sem Acessar o AVA deve ser informado.");
			Uteis.checkState(getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isCancelamentoExcessoTrancamento() && !Uteis.isAtributoPreenchido(getMapaRegistroEvasaoCursoVO().getQtdTrancamentoEmExcesso()), "A quantidade de Trancamento em excesso para que seja realizado o cancelamento da Matrícula deve ser informado.");
			Uteis.checkState(getMapaRegistroEvasaoCursoVO().getUnidadeEnsinoVOs().stream().allMatch(p-> !p.getFiltrarUnidadeEnsino()), "Deve ser informado pelo menos uma UNIDADE DE ENSINO para geração do Mapa.");
//			Uteis.checkState((getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isAbandonoCurso() || getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isTrancamento()) 
//					&& getMapaRegistroEvasaoCursoVO().getAno().equals(Uteis.getAnoDataAtual()) && getMapaRegistroEvasaoCursoVO().getSemestre().equals(Uteis.getSemestreAtual()),"Não é possível realizar essa operação, pois o Ano/Semestre não pode ser corrente.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(getMapaRegistroEvasaoCursoVO().getNivelEducacional()), "O campo Nivel Educacional deve ser informado.");
			Integer anoSemestreFiltro = Integer.parseInt(getApresentarCampoSemestre() ? getMapaRegistroEvasaoCursoVO().getAno() + getMapaRegistroEvasaoCursoVO().getSemestre() : getMapaRegistroEvasaoCursoVO().getAno() );
			Integer anoSemestreCorrente = Integer.parseInt(getApresentarCampoSemestre() ? Uteis.getAnoDataAtual() + Uteis.getSemestreAtual() : Uteis.getAnoDataAtual());
			Uteis.checkState(anoSemestreFiltro > anoSemestreCorrente, "Não é possível realizar essa operação, pois o Ano/Semestre  não pode ser maior que o Ano/Semestre Corrente.");
			getFacadeFactory().getMapaRegistroEvasaoCursoFacade().consultarPorUnidadeEnsinoCursoTurnoMapaRegistroAbandonoCursoTrancamento(getMapaRegistroEvasaoCursoVO(), false, getUsuarioLogado());
			inicializarDadosMarcacaoLista();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void persistir() {
		try {
			validarAnoSemestre(Boolean.TRUE);
			getFacadeFactory().getMapaRegistroEvasaoCursoFacade().persistir(getMapaRegistroEvasaoCursoVO(), true, getConfiguracaoGeralPadraoSistema(), getUsuarioLogadoClone());
			getMapaRegistroEvasaoCursoVO().getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO().removeIf(p-> !p.getMatriculaPeriodoVO().getMatriculaVO().getAlunoSelecionado());
			setMensagemID("msg_dados_gravados_trancamento", Uteis.SUCESSO, Boolean.TRUE);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void executarOperacaoMapaRegistroEvasaoCurso() {
		Map<Integer, ConfiguracaoGeralSistemaVO> configuracaoGeralSistemaVOs = new HashMap<Integer, ConfiguracaoGeralSistemaVO>(0);
		Map<Integer, ConfiguracaoFinanceiroVO> configuracaoFinanceiroVOs = new HashMap<Integer, ConfiguracaoFinanceiroVO>(0);		
		try {
			for (MapaRegistroEvasaoCursoMatriculaPeriodoVO mrecmp : getMapaRegistroEvasaoCursoVO().getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO()) {				
				try {
					if (getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().getForcarEncerramento() || !getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().getAtivado()) {
						break;
					}
					if (getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().getAtivado() && mrecmp.getMatriculaPeriodoVO().getMatriculaVO().getAlunoSelecionado() && !mrecmp.getSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum().equals(SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum.PROCESSADO)) {
						getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().setStatus("Registrando Matrícula n° " + mrecmp.getMatriculaPeriodoVO().getMatriculaVO().getMatricula() + " (" + getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().getProgresso() + " de " + getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().getMaxValue() + ")");
						if (!configuracaoGeralSistemaVOs.containsKey(mrecmp.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo())) {
							configuracaoGeralSistemaVOs.put(mrecmp.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsadaUnidadEnsino(mrecmp.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone()));
						}
						if (!configuracaoFinanceiroVOs.containsKey(mrecmp.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo())) {
							configuracaoFinanceiroVOs.put(mrecmp.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorUnidadeEnsino(mrecmp.getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone()));
						}
						getFacadeFactory().getMapaRegistroEvasaoCursoMatriculaPeriodoFacade().executarOperacaoMapaRegistroEvasaoCurso(mrecmp, false, configuracaoGeralSistemaVOs, configuracaoFinanceiroVOs, getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().getUsuarioVO());
						getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().incrementar();
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
//					getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().incrementar();
				}
			}
		} catch (Exception e) {
			getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().setForcarEncerramento(true);
		} finally {
//			getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().incrementar();
			getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().setForcarEncerramento(true);
			configuracaoGeralSistemaVOs = null;
			configuracaoFinanceiroVOs = null;
		}
	}
	
	public void realizarInicioProgressBar() {
		try {
			getAplicacaoControle().setProgressBarMapaRegistroEvasaoCursoVO(new ProgressBarVO());
			Uteis.checkState(getMapaRegistroEvasaoCursoVO().getQuantidadeAlunosSelecionados() == 0L, "Não foi Selecionado nenhum aluno para ser realizado a operação");
			getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().resetar();
			getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().setAplicacaoControle(getAplicacaoControle());
			getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().setUsuarioVO(getUsuarioLogadoClone());
			getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().iniciar(0l, getMapaRegistroEvasaoCursoVO().getQuantidadeAlunosSelecionados().intValue(), "Iniciando Processamento da(s) operações.", true, this, "executarOperacaoMapaRegistroEvasaoCurso");
			setOncompleteModal("RichFaces.$('panelConfirmacao').hide();");
		} catch (Exception e) {
			setOncompleteModal("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());	
		} 
	}
	
	
	public void realizarEstornoMapaRegistroEvasaoCursoMatriculaPeriodoIndividual() {
		try {
			setMapaRegistroEvasaoCursoMatriculaPeriodoVO((MapaRegistroEvasaoCursoMatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("mapaRegistroMatriculaPeriodoItem"));
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void executarOperacaoEstornoMapaRegistroEvasaoCurso() {
		try {
			if(Uteis.isAtributoPreenchido(getMapaRegistroEvasaoCursoMatriculaPeriodoVO())) {
				getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().setStatus("Estornando Matrícula n° " + getMapaRegistroEvasaoCursoMatriculaPeriodoVO().getMatriculaPeriodoVO().getMatriculaVO().getMatricula() + " (" + getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().getProgresso() + " de " + getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().getMaxValue() + ")");
				getFacadeFactory().getMapaRegistroEvasaoCursoMatriculaPeriodoFacade().executarEstornoMapaRegistroEvasaoCursoMatriculaPeriodo(getMapaRegistroEvasaoCursoMatriculaPeriodoVO(), false, getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().getUsuarioVO());
				getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().setForcarEncerramento(true);
				getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().incrementar();
			}else {
				for (MapaRegistroEvasaoCursoMatriculaPeriodoVO mrecmp : getMapaRegistroEvasaoCursoVO().getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO()) {
					if (getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().getAtivado()
							&& mrecmp.getSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum().isProcessado()) {
						try {
							if(getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().getForcarEncerramento() 
									|| !getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().getAtivado()){
								break;
							}							
							getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().setStatus("Estornando Matrícula n° " + mrecmp.getMatriculaPeriodoVO().getMatriculaVO().getMatricula() + " (" + getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().getProgresso() + " de " + getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().getMaxValue() + ")");
							getFacadeFactory().getMapaRegistroEvasaoCursoMatriculaPeriodoFacade().executarEstornoMapaRegistroEvasaoCursoMatriculaPeriodo(mrecmp, false, getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().getUsuarioVO());
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().incrementar();
						}
					}
				}			
			}
		} catch (Exception e) {
			getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().setForcarEncerramento(true);
		} finally {
			setMapaRegistroEvasaoCursoMatriculaPeriodoVO(new MapaRegistroEvasaoCursoMatriculaPeriodoVO());
			getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().incrementar();
			getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().setForcarEncerramento(true);
		}
	}
	
	public void realizarInicioProgressBarEstorno() {
		try {
			Long qtd = null;
			if(Uteis.isAtributoPreenchido(getMapaRegistroEvasaoCursoMatriculaPeriodoVO())) {
				qtd = 1L;	
			}else {
				qtd = getMapaRegistroEvasaoCursoVO().getQuantidadeRegistroParaEstorno();
			}
			getAplicacaoControle().setProgressBarMapaRegistroEvasaoCursoVO(new ProgressBarVO());			
			Uteis.checkState(qtd == 0L, "Não foi Selecionado nenhum aluno para ser realizado a operação");
			getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().resetar();
			getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().setAplicacaoControle(getAplicacaoControle());
			getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().setUsuarioVO(getUsuarioLogadoClone());
			getAplicacaoControle().getProgressBarMapaRegistroEvasaoCursoVO().iniciar(0l, qtd.intValue(), "Iniciando Processamento da(s) operações.", true, this, "executarOperacaoEstornoMapaRegistroEvasaoCurso");
			setOncompleteModal("RichFaces.$('panelEstornoMapaRegistroEvasaoCurso').hide();");
		} catch (Exception e) {
			setOncompleteModal("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());	
		} 
	}
	
	public void fecharEstornoMapaRegistroEvasaoCursoMatriculaPeriodo() {
		try {
			setMapaRegistroEvasaoCursoMatriculaPeriodoVO(new MapaRegistroEvasaoCursoMatriculaPeriodoVO());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void fecharConfirmacaoGravacao() {
		try {
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			unidade.setFiltrarUnidadeEnsino(getMarcarTodasUnidadeEnsino());
		}
		verificarTodasUnidadesSelecionadas();
	}

	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
			if (obj.getFiltrarUnidadeEnsino()) {
				unidade.append(obj.getNome().trim());
				if (getUnidadeEnsinoVOs().size() > 1) {
					unidade.append("; ");
				}
			}
		}
		setUnidadeEnsinoApresentar(unidade.toString());
		consultarCurso();
		consultarTurnoFiltroRelatorio();
	}

	public void marcarTodosCursosAction() throws Exception {
		for (CursoVO cursoVO : getCursoVOs()) {
			cursoVO.setFiltrarCursoVO(getMarcarTodosCursos());
		}
		verificarTodosCursosSelecionados();
	}

	public void verificarTodosCursosSelecionados() {
		StringBuilder curso = new StringBuilder();
		for (CursoVO obj : getCursoVOs()) {
			if (obj.getFiltrarCursoVO()) {
				curso.append(obj.getCodigo()).append(" - ");
				curso.append(obj.getNome());
				if (getCursoVOs().size() > 1) {
					curso.append("; ");
				}
			}
		}
		setCursoApresentar(curso.toString());
	}

	public void marcarTodosTurnosAction() throws Exception {
		for (TurnoVO turnoVO : getTurnoVOs()) {
			turnoVO.setFiltrarTurnoVO(getMarcarTodosTurnos());
		}
		verificarTodosTurnosSelecionados();
	}

	public void verificarTodosTurnosSelecionados() {
		StringBuilder turno = new StringBuilder();
		for (TurnoVO obj : getTurnoVOs()) {
			if (obj.getFiltrarTurnoVO()) {
				turno.append(obj.getNome());
				if (getTurnoVOs().size() > 1) {
					turno.append("; ");
				}
			}
		}
		setTurnoApresentar(turno.toString());
	}

	public void consultarCurso() {
		try {
			setCursosApresentar("");			
			setTurnosApresentar("");
			if(getMapaRegistroEvasaoCursoVO().getPeriodicidade().equals(PeriodicidadeEnum.ANUAL.getValor())) {
				getMapaRegistroEvasaoCursoVO().setSemestre(Constantes.EMPTY);
				getMapaRegistroEvasaoCursoVO().setSemestreRegistroEvasao(Constantes.EMPTY);
			}
			if (getUnidadeEnsinoVOs().isEmpty()) {
				setCursoVOs(null);
				setTurnoVOs(null);
				return;
			}			
			setCursoVOs(getFacadeFactory().getCursoFacade().consultarCursoPorNomePeriodicidadeNivelEducacionalEUnidadeEnsinoVOs("", getMapaRegistroEvasaoCursoVO().getPeriodicidade(), 
					getMapaRegistroEvasaoCursoVO().getNivelEducacional(), getUnidadeEnsinoVOs(), getUsuario()));
			verificarTodosCursosSelecionados();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarDadosMarcacaoLista() {
		for (MapaRegistroEvasaoCursoMatriculaPeriodoVO mractmp : getMapaRegistroEvasaoCursoVO().getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO()) {
			mractmp.getMatriculaPeriodoVO().getMatriculaVO().setAlunoSelecionado(isMatriculaSelecionada());
		}
	}
	public void marcarDesmarcarTodos(FilterFactory filterFactory) {		
		for (MapaRegistroEvasaoCursoMatriculaPeriodoVO mractmp : getMapaRegistroEvasaoCursoVO().getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO()) {
			if(mractmp.getSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum().isAguardandoProcessamento()) {
				marcarDesmarcarTodosFiltroTabela(mractmp.getMatriculaPeriodoVO(), filterFactory);	
			}
		}
	}	
		

	public MapaRegistroEvasaoCursoVO getMapaRegistroEvasaoCursoVO() {
		if (mapaRegistroEvasaoCursoVO == null) {
			mapaRegistroEvasaoCursoVO = new MapaRegistroEvasaoCursoVO();
		}
		return mapaRegistroEvasaoCursoVO;
	}

	public void setMapaRegistroEvasaoCursoVO(MapaRegistroEvasaoCursoVO mapaRegistroEvasaoCursoVO) {
		this.mapaRegistroEvasaoCursoVO = mapaRegistroEvasaoCursoVO;
	}
	
	

	public MapaRegistroEvasaoCursoMatriculaPeriodoVO getMapaRegistroEvasaoCursoMatriculaPeriodoVO() {
		if (mapaRegistroEvasaoCursoMatriculaPeriodoVO == null) {
			mapaRegistroEvasaoCursoMatriculaPeriodoVO = new MapaRegistroEvasaoCursoMatriculaPeriodoVO();
		}
		return mapaRegistroEvasaoCursoMatriculaPeriodoVO;
	}

	public void setMapaRegistroEvasaoCursoMatriculaPeriodoVO(MapaRegistroEvasaoCursoMatriculaPeriodoVO mapaRegistroEvasaoCursoMatriculaPeriodoVO) {
		this.mapaRegistroEvasaoCursoMatriculaPeriodoVO = mapaRegistroEvasaoCursoMatriculaPeriodoVO;
	}

	public String getUnidadeEnsinoApresentar() {
		if (unidadeEnsinoApresentar == null) {
			unidadeEnsinoApresentar = "";
		}
		return unidadeEnsinoApresentar;
	}

	public void setUnidadeEnsinoApresentar(String unidadeEnsinoApresentar) {
		this.unidadeEnsinoApresentar = unidadeEnsinoApresentar;
	}

	public String getCursoApresentar() {
		if (cursoApresentar == null) {
			cursoApresentar = "";
		}
		return cursoApresentar;
	}

	public void setCursoApresentar(String cursoApresentar) {
		this.cursoApresentar = cursoApresentar;
	}

	public String getTurnoApresentar() {
		if (turnoApresentar == null) {
			turnoApresentar = "";
		}
		return turnoApresentar;
	}

	public void setTurnoApresentar(String turnoApresentar) {
		this.turnoApresentar = turnoApresentar;
	}
	

	public List<SelectItem> getListaSelectItemPeriodicidade() {
		if (listaSelectItemPeriodicidade == null) {
			listaSelectItemPeriodicidade = new ArrayList<SelectItem>(0);
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.ANUAL.getValor(), PeriodicidadeEnum.ANUAL.getDescricao()));
			listaSelectItemPeriodicidade.add(new SelectItem(PeriodicidadeEnum.SEMESTRAL.getValor(), PeriodicidadeEnum.SEMESTRAL.getDescricao()));
		}
		return listaSelectItemPeriodicidade;
	}	

	public boolean getApresentarCampoSemestre() {
		return PeriodicidadeEnum.SEMESTRAL.getValor().equals(getMapaRegistroEvasaoCursoVO().getPeriodicidade());
	}

	public boolean isMatriculaSelecionada() {
		return matriculaSelecionada;
	}

	public void setMatriculaSelecionada(boolean matriculaSelecionada) {
		this.matriculaSelecionada = matriculaSelecionada;
	}
	
	
	public void limparCamposRegrasTipoTrancamento() {
		try {
			if(getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isAbandonoCurso()
					|| getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isTrancamento()) {
				getMapaRegistroEvasaoCursoVO().setQtdDisciplinaReprovadas(0);
				getMapaRegistroEvasaoCursoVO().setQtdDiasAlunosSemAcessoAva(0);
				getMapaRegistroEvasaoCursoVO().setQtdTrancamentoEmExcesso(0);
				getMapaRegistroEvasaoCursoVO().setQtdMesAlunosRenovacaoSemAcessoAva(0);				
			}else if(getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isJubilamento()) {
				getMapaRegistroEvasaoCursoVO().setQtdDisciplinaReprovadas(0);
				getMapaRegistroEvasaoCursoVO().setQtdDiasAlunosSemAcessoAva(0);
				getMapaRegistroEvasaoCursoVO().setQtdTrancamentoEmExcesso(2);
				getMapaRegistroEvasaoCursoVO().setQtdMesAlunosRenovacaoSemAcessoAva(0);
			}else if(getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isCancelamentoIngressante()) {
				getMapaRegistroEvasaoCursoVO().setQtdTrancamentoEmExcesso(0);
				getMapaRegistroEvasaoCursoVO().setQtdMesAlunosRenovacaoSemAcessoAva(0);				
			}else if(getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isCancelamentoExcessoTrancamento()) {				
				getMapaRegistroEvasaoCursoVO().setQtdDisciplinaReprovadas(0);
				getMapaRegistroEvasaoCursoVO().setQtdDiasAlunosSemAcessoAva(0);
				getMapaRegistroEvasaoCursoVO().setQtdMesAlunosRenovacaoSemAcessoAva(0);
				getMapaRegistroEvasaoCursoVO().setQtdTrancamentoEmExcesso(2);				
			}else if(getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isRenovacaoAutomatica()) {				
				getMapaRegistroEvasaoCursoVO().setQtdDisciplinaReprovadas(0);
				getMapaRegistroEvasaoCursoVO().setQtdDiasAlunosSemAcessoAva(0);
				getMapaRegistroEvasaoCursoVO().setQtdTrancamentoEmExcesso(0);
			}
			getMapaRegistroEvasaoCursoVO().setAnoRegistroEvasao(Constantes.EMPTY);
			getMapaRegistroEvasaoCursoVO().setSemestreRegistroEvasao(Constantes.EMPTY);
			getMapaRegistroEvasaoCursoVO().getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparUnidadeEnsino() {
		try {
			setUnidadeEnsinoApresentar(null);
			setMarcarTodasUnidadeEnsino(false);
			marcarTodasUnidadesEnsinoAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparCurso() {
		try {
			setCursoApresentar(null);
			setMarcarTodosCursos(false);
			marcarTodosCursosAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurno() {
		try {
			setTurnoApresentar(null);
			setMarcarTodosTurnos(false);
			marcarTodosTurnosAction();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	public String getFiltroMatricula() {
		if (filtroMatricula == null) {
			filtroMatricula = "";
		}
		return filtroMatricula;
	}

	public void setFiltroMatricula(String filtroMatricula) {
		this.filtroMatricula = filtroMatricula;
	}
	

	public void marcarDesmarcarTodosFiltroTabela(MatriculaPeriodoVO obj, FilterFactory filterFactory) {
		if (filterFactory != null) {
			setFiltroMatricula(filterFactory.getMapFilter().get("filtroMatricula").getFiltro());
			setFiltroNomeAluno(filterFactory.getMapFilter().get("filtroNomeAluno").getFiltro());
			setFiltroNomeUnidadeEnsino(filterFactory.getMapFilter().get("filtroNomeUnidadeEnsino").getFiltro());
			setFiltroNomeCurso(filterFactory.getMapFilter().get("filtroNomeCurso").getFiltro());
			setFiltroNomeTurma(filterFactory.getMapFilter().get("filtroNomeTurma").getFiltro());
		}
		
		if (!getFiltroMatricula().trim().isEmpty() || !getFiltroNomeAluno().trim().isEmpty() || !getFiltroNomeUnidadeEnsino().trim().isEmpty() 
				|| !getFiltroNomeCurso().trim().isEmpty() || !getFiltroNomeTurma().trim().isEmpty()) {
			
			boolean filtrarMatricula = filtrarMatricula(obj);
			boolean filtrarAluno = filtrarNomeAluno(obj);
			boolean filtrarUnidadeEnsino = filtrarNomeUnidadeEnsino(obj);
			boolean filtrarCurso = filtrarNomeCurso(obj);
			boolean filtrarTurma = filtrarNomeTurma(obj);
			if (filtrarMatricula && filtrarAluno && filtrarUnidadeEnsino && filtrarCurso && filtrarTurma) {
				obj.getMatriculaVO().setAlunoSelecionado(isMatriculaSelecionada());
			}
		} else {
			obj.getMatriculaVO().setAlunoSelecionado(isMatriculaSelecionada());
		}
		
	}
	
	public boolean filtrarMatricula(Object obj) {
		if (!getFiltroMatricula().trim().isEmpty()) {
			if (obj instanceof MatriculaPeriodoVO) {
				if(((MatriculaPeriodoVO) obj).getMatricula().toUpperCase().contains(getFiltroMatricula().toUpperCase().trim())){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;		
	}
	
	
	public void consultarTurnoVO() {
		try {
			getListaConsultaTurno().clear();
			if (getCampoConsultaTurno().equals("nome")) {
				setListaConsultaTurno(getFacadeFactory().getTurnoFacade().consultarPorNome(getValorConsultaTurno(), getMapaRegistroEvasaoCursoVO().getCursoFiltro().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurno(new ArrayList<TurnoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarTurnoVO() {
		try {
			TurnoVO obj = (TurnoVO) context().getExternalContext().getRequestMap().get("turnoItem");
			getMapaRegistroEvasaoCursoVO().setTurnoFiltro(obj);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurnoVO() {
		try {
			getMapaRegistroEvasaoCursoVO().setTurnoFiltro(new TurnoVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public void consultarCursoVO() {
		try {
			getCursoVOs().clear();
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				setCursoVOs(getFacadeFactory().getCursoFacade().consultarCursoPorCodigoPeriodicidadeNivelEducacionalEUnidadeEnsinoVO(valorInt,"", getMapaRegistroEvasaoCursoVO().getNivelEducacional(),getMapaRegistroEvasaoCursoVO().getUnidadeEnsinoFiltro().getCodigo(),getUsuarioLogado()));
			}
			if (getCampoConsultaCurso().equals("nome")) {
				if (getValorConsultaCurso().trim().isEmpty() || getValorConsultaCurso().trim().contains("%%") || getValorConsultaCurso().trim().length() < 3) {
					throw new Exception("Informe 3 caracteres válidos para realizar a consulta.");
				}
				setCursoVOs(getFacadeFactory().getCursoFacade().consultarCursoPorNomePeriodicidadeNivelEducacionalEUnidadeEnsinoVO(getValorConsultaCurso(),"",getMapaRegistroEvasaoCursoVO().getNivelEducacional(),getMapaRegistroEvasaoCursoVO().getUnidadeEnsinoFiltro().getCodigo(),getUsuarioLogado()));
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setCursoVOs(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void selecionarCurso() {
		try {
			getMapaRegistroEvasaoCursoVO().setCursoFiltro((CursoVO)getRequestMap().get("cursoItem"));			
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
	
	public void limparCursoVO() {
		try {
			getMapaRegistroEvasaoCursoVO().setCursoFiltro(null);	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
				
	}
	
	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMapaRegistroEvasaoCursoVO().getMatriculaFiltro().getMatricula(), getMapaRegistroEvasaoCursoVO().getUnidadeEnsinoFiltro().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			Uteis.checkState(objAluno.getMatricula().equals(""), "Aluno de matrícula " + getMapaRegistroEvasaoCursoVO().getMatriculaFiltro().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			getMapaRegistroEvasaoCursoVO().setMatriculaFiltro(objAluno);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getMapaRegistroEvasaoCursoVO().setMatriculaFiltro(new MatriculaVO());
		}
	}
	
	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaMatricula().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaMatricula().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaMatricula(), getMapaRegistroEvasaoCursoVO().getUnidadeEnsinoFiltro().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaMatricula().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaMatricula(),getMapaRegistroEvasaoCursoVO().getUnidadeEnsinoFiltro().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaMatricula().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaMatricula(),getMapaRegistroEvasaoCursoVO().getUnidadeEnsinoFiltro().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaMatricula().equals("registroAcademico")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaMatricula(),getMapaRegistroEvasaoCursoVO().getUnidadeEnsinoFiltro().getCodigo(), false, getUsuarioLogado());
			}
			setMatriculaVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMatriculaVOs(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarAluno() {
		try {
			MatriculaVO obj = (MatriculaVO)getRequestMap().get("matriculaItem");
			getMapaRegistroEvasaoCursoVO().setMatriculaFiltro(obj);
			//getMatriculaVO().setMatricula(obj.getMatricula());
			selecionarMatriculaPorMatricula();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarMatriculaPorMatricula() {
		try {
			consultarMatriculaPorMatricula(getValorAutoComplete(getMapaRegistroEvasaoCursoVO().getMatriculaFiltro().getMatricula()));
			inicializarMensagemVazia();	
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public void consultarMatriculaPorMatricula(String valor) {
		try {
			List<MatriculaVO> matriculas = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(valor, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getMapaRegistroEvasaoCursoVO().setMatriculaFiltro(matriculas.get(0));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private String getValorAutoComplete(String valor) {
		if (valor != null) {
			return valor;
		}
		return "";
	}
	
	public void limparMatricula() {
		try {
			getMapaRegistroEvasaoCursoVO().setMatriculaFiltro(null);	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}		
	}
	
	public void limparUsuario() {
		try {
			getMapaRegistroEvasaoCursoVO().setUsuarioResponsavel(null);	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}		
	}
	
	public void selecionarUsuario() {
		try {
			UsuarioVO obj = (UsuarioVO)getRequestMap().get("usuarioConsultaItens");
			getMapaRegistroEvasaoCursoVO().setUsuarioResponsavel(obj);
			setControleConsultaUsuario(new DataModelo());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarUsuario() {
	    try {
	    	getControleConsultaUsuario().setPage(1);
	    	getControleConsultaUsuario().setPaginaAtual(1);
	        realizarConsultaUsuario();
	    } catch (Exception e) {
	    	getControleConsultaUsuario().getListaConsulta().clear();
	    	getControleConsultaUsuario().setTotalRegistrosEncontrados(0);
	         setMensagemDetalhada("msg_erro", e.getMessage());
	    }

	}
	
    private void realizarConsultaUsuario() throws Exception {
		super.consultar();
		getControleConsultaUsuario().getListaConsulta().clear();
		getControleConsultaUsuario().setLimitePorPagina(8);
		List<UsuarioVO> listaConsultaUsuarios = new ArrayList<UsuarioVO>(0);
		if (getControleConsulta().getCampoConsulta().equals("nome")) {
			if (getControleConsultaUsuario().getValorConsulta().length() < 2) {
				throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
			}
			listaConsultaUsuarios = getFacadeFactory().getUsuarioFacade().consultarPorNomeTipoEspecificoUsuarioAlunoProfessorCoordenador(getControleConsultaUsuario().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), getControleConsultaUsuario().getLimitePorPagina(), getControleConsultaUsuario().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getControleConsultaUsuario(), getUsuarioLogado());
		}
		if (getControleConsulta().getCampoConsulta().equals("cpf")) {
			if (getControleConsultaUsuario().getValorConsulta().length() < 2) {
				throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
			}
			listaConsultaUsuarios = getFacadeFactory().getUsuarioFacade().consultarPorCPF(getControleConsultaUsuario().getValorConsulta(), getControleConsultaUsuario().getLimitePorPagina(), getControleConsultaUsuario().getOffset(), false,  getControleConsultaUsuario(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		}
		if (getControleConsulta().getCampoConsulta().equals("emailInstitucional")) {
			if (getControleConsultaUsuario().getValorConsulta().length() < 2) {
				throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
			}
			listaConsultaUsuarios = getFacadeFactory().getUsuarioFacade().consultarPorEmailInstitucional(getControleConsultaUsuario().getValorConsulta(), getControleConsultaUsuario().getLimitePorPagina(), getControleConsultaUsuario().getOffset(),false, getControleConsultaUsuario(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		}
		getControleConsultaUsuario().setListaConsulta(listaConsultaUsuarios);
	}
    
    public void scrollerListenerUsuario(DataScrollEvent DataScrollEvent) {
    	getControleConsultaUsuario().setPaginaAtual(DataScrollEvent.getPage());
    	getControleConsultaUsuario().setPage(DataScrollEvent.getPage());
		try {
			realizarConsultaUsuario();
		} catch (Exception e) {
			getControleConsultaUsuario().getListaConsulta().clear();
			getControleConsultaUsuario().setTotalRegistrosEncontrados(0);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
    
    public void anularDataModelo(){
    	setControleConsultaUsuario(null);
    }
	
    public String getMascaraConsultaUsuario() {
		if (getControleConsulta().getCampoConsulta().equals("cpf")) {
			return "return mascara(this.form,'formModal:valorConsulta','999.999.999-99',event)";
		}
		return "";
	}
	
	public List<SelectItem> tipoConsultaUsuario;	
	public List<SelectItem> getTipoConsultaUsuario() {
		if(tipoConsultaUsuario == null) {
			tipoConsultaUsuario = new ArrayList<SelectItem>(0);
			tipoConsultaUsuario.add(new SelectItem("nome", "Nome"));
			tipoConsultaUsuario.add(new SelectItem("cpf", "CPF"));
			tipoConsultaUsuario.add(new SelectItem("emailInstitucional", "E-mail Institucional"));	
		}
		return tipoConsultaUsuario;
	}
	
	public void montarTodosListaSelectItem() {
//		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemMotivoCancelamentoTrancamento();
	}
	
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			getListaSelectItemUnidadeEnsino().clear();
			List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome", true));			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void montarListaSelectItemMotivoCancelamentoTrancamento() {
		try {
			List<MotivoCancelamentoTrancamentoVO> resultadoConsulta = getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorNomeAtivo("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemMotivoCancelamentoTrancamento(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}
	
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if(listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino =  new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}
	
	public List<SelectItem> getListaSelectItemMotivoCancelamentoTrancamento() {
		if (listaSelectItemMotivoCancelamentoTrancamento == null) {
			listaSelectItemMotivoCancelamentoTrancamento = new ArrayList<SelectItem>();
		}
		return listaSelectItemMotivoCancelamentoTrancamento;
	}
	
	public void setListaSelectItemMotivoCancelamentoTrancamento(List<SelectItem> listaSelectItemMotivoCancelamentoTrancamento) {
		this.listaSelectItemMotivoCancelamentoTrancamento = listaSelectItemMotivoCancelamentoTrancamento;
	}
	
	public List<SelectItem> tipoConsultaComboTurno;	
	public List<SelectItem> getTipoConsultaComboTurno() {
		if(tipoConsultaComboTurno == null) {
			tipoConsultaComboTurno = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurno.add(new SelectItem("nome", "Nome Turno"));
		}
		return tipoConsultaComboTurno;
	}
	
	public String getValorConsultaTurno() {
		if (valorConsultaTurno == null) {
			valorConsultaTurno = "";
		}
		return valorConsultaTurno;
	}

	public void setValorConsultaTurno(String valorConsultaTurno) {
		this.valorConsultaTurno = valorConsultaTurno;
	}

	public String getCampoConsultaTurno() {
		if (campoConsultaTurno == null) {
			campoConsultaTurno = "";
		}
		return campoConsultaTurno;
	}

	public void setCampoConsultaTurno(String campoConsultaTurno) {
		this.campoConsultaTurno = campoConsultaTurno;
	}

	public List<TurnoVO> getListaConsultaTurno() {
		if (listaConsultaTurno == null) {
			listaConsultaTurno = new ArrayList<TurnoVO>();
		}
		return listaConsultaTurno;
	}

	public void setListaConsultaTurno(List<TurnoVO> listaConsultaTurno) {
		this.listaConsultaTurno = listaConsultaTurno;
	}
	
	public List<SelectItem> tipoConsultaComboCurso;	
	public List<SelectItem> getTipoConsultaComboCurso() {
		if(tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
		}
		return tipoConsultaComboCurso;
	}

	public String getValorConsultaCurso() {
		if(valorConsultaCurso == null) {
			valorConsultaCurso =  "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public String getCampoConsultaCurso() {
		if(campoConsultaCurso == null) {
			campoConsultaCurso =  "nome";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public List<CursoVO> getCursoVOs() {
		if(cursoVOs == null) {
			cursoVOs =  new ArrayList<CursoVO>(0);
		}
		return cursoVOs;
	}

	public void setCursoVOs(List<CursoVO> cursoVOs) {
		this.cursoVOs = cursoVOs;
	}
	
	public String getCampoConsultaUsuario() {
		if (campoConsultaUsuario == null) {
			campoConsultaUsuario = "";
		}
		return campoConsultaUsuario;
	}

	public void setCampoConsultaUsuario(String campoConsultaUsuario) {
		this.campoConsultaUsuario = campoConsultaUsuario;
	}

	public String getValorConsultaUsuario() {
		if (valorConsultaUsuario == null) {
			valorConsultaUsuario = "";
		}
		return valorConsultaUsuario;
	}

	public void setValorConsultaUsuario(String valorConsultaUsuario) {
		this.valorConsultaUsuario = valorConsultaUsuario;
	}
	
	
	
	public DataModelo getControleConsultaUsuario() {
		if (controleConsultaUsuario == null) {
			controleConsultaUsuario = new DataModelo();
		}
		return controleConsultaUsuario;
	}

	public void setControleConsultaUsuario(DataModelo controleConsultaUsuario) {
		this.controleConsultaUsuario = controleConsultaUsuario;
	}

	public List<SelectItem> tipoConsultaComboAluno;
	public List<SelectItem> getTipoConsultaComboAluno() {
		if(tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
			tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno"));
			tipoConsultaComboAluno.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
			tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
			tipoConsultaComboAluno.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboAluno;
	}
	

	public String getValorConsultaMatricula() {
		if(valorConsultaMatricula == null) {
			valorConsultaMatricula =  "";
		}
		return valorConsultaMatricula;
	}

	public void setValorConsultaMatricula(String valorConsultaMatricula) {
		this.valorConsultaMatricula = valorConsultaMatricula;
	}

	public String getCampoConsultaMatricula() {
		if(campoConsultaMatricula == null) {
			campoConsultaMatricula =  "nomePessoa";
		}
		return campoConsultaMatricula;
	}

	public void setCampoConsultaMatricula(String campoConsultaMatricula) {
		this.campoConsultaMatricula = campoConsultaMatricula;
	}

	public List<MatriculaVO> getMatriculaVOs() {
		if(matriculaVOs == null) {
			matriculaVOs =  new ArrayList<MatriculaVO>(0);
		}
		return matriculaVOs;
	}

	public void setMatriculaVOs(List<MatriculaVO> matriculaVOs) {
		this.matriculaVOs = matriculaVOs;
	}
	
	public boolean filtrarNomeAluno(Object obj) {
		if (!getFiltroNomeAluno().trim().isEmpty()) {
			if (obj instanceof MatriculaPeriodoVO) {
				if(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(((MatriculaPeriodoVO) obj).getMatriculaVO().getAluno().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltroNomeAluno().toUpperCase().trim())))){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;		
	}
	
	public boolean filtrarNomeUnidadeEnsino(Object obj) {
		if (!getFiltroNomeUnidadeEnsino().trim().isEmpty()) {
			if (obj instanceof MatriculaPeriodoVO) {
				if(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(((MatriculaPeriodoVO) obj).getMatriculaVO().getUnidadeEnsino().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltroNomeUnidadeEnsino().toUpperCase().trim())))){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;		
	}
	
	public boolean filtrarNomeCurso(Object obj) {
		if (!getFiltroNomeCurso().trim().isEmpty()) {
			if (obj instanceof MatriculaPeriodoVO) {
				if(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(((MatriculaPeriodoVO) obj).getMatriculaVO().getCurso().getNome())).toUpperCase().contains(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(getFiltroNomeCurso().toUpperCase().trim())))){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;		
	}
	
	public boolean filtrarNomeTurma(Object obj) {
		if (!getFiltroNomeTurma().trim().isEmpty()) {
			if (obj instanceof MatriculaPeriodoVO) {
				if(((MatriculaPeriodoVO) obj).getTurma().getIdentificadorTurma().toUpperCase().contains(getFiltroNomeTurma().toUpperCase().trim())){
					return true;
				}
				return false;
			}else{
				return false;
			}
		}
		return true;		
	}

	public String getFiltroNomeAluno() {
		if (filtroNomeAluno == null) {
			filtroNomeAluno = "";
		}
		return filtroNomeAluno;
	}

	public void setFiltroNomeAluno(String filtroNomeAluno) {
		this.filtroNomeAluno = filtroNomeAluno;
	}

	public String getFiltroNomeUnidadeEnsino() {
		if (filtroNomeUnidadeEnsino == null) {
			filtroNomeUnidadeEnsino = "";
		}
		return filtroNomeUnidadeEnsino;
	}

	public void setFiltroNomeUnidadeEnsino(String filtroNomeUnidadeEnsino) {
		this.filtroNomeUnidadeEnsino = filtroNomeUnidadeEnsino;
	}

	public String getFiltroNomeCurso() {
		if (filtroNomeCurso == null) {
			filtroNomeCurso = "";
		}
		return filtroNomeCurso;
	}

	public void setFiltroNomeCurso(String filtroNomeCurso) {
		this.filtroNomeCurso = filtroNomeCurso;
	}

	public String getFiltroNomeTurma() {
		if (filtroNomeTurma == null) {
			filtroNomeTurma = "";
		}
		return filtroNomeTurma;
	}

	public void setFiltroNomeTurma(String filtroNomeTurma) {
		this.filtroNomeTurma = filtroNomeTurma;
	}
	
	public List<SelectItem> getListaSelectItemTipoTrancamento() {
		if(listaSelectItemTipoTrancamento == null) {
			listaSelectItemTipoTrancamento = new ArrayList<SelectItem>();
			new ArrayList<>(Arrays.asList(TipoTrancamentoEnum.values())).stream().map(t -> new SelectItem(t, t.getDescricao())).forEach(listaSelectItemTipoTrancamento::add);
		}
		return listaSelectItemTipoTrancamento;
	}

	public String getTipoTrancamento() {
		if(tipoTrancamento == null) {
			tipoTrancamento = "";
		}
		return tipoTrancamento;
	}

	public void setTipoTrancamento(String tipoTrancamento) {
		this.tipoTrancamento = tipoTrancamento;
	}
	
	public String getCampoNavegacaoOrigem() {
		if (campoNavegacaoOrigem == null) {
			campoNavegacaoOrigem = "";
		}
		return campoNavegacaoOrigem;
	}

	public void setCampoNavegacaoOrigem(String campoNavegacaoOrigem) {
		this.campoNavegacaoOrigem = campoNavegacaoOrigem;
	}
	
	public String formatarApresentacaoListas(String lista) {
		if (lista.length() > 200) {
			return lista.substring(0, 200) + " ...";
		}
		return lista;
	}
	
	public void realizarImpressaoExcel() throws Exception {
		try {
			getSuperParametroRelVO().setNomeDesignIreport(MapaRegistroEvasaoCurso.designIReportRelatorioExcel());
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
			getSuperParametroRelVO().setSubReport_Dir(MapaRegistroEvasaoCurso.caminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			String titulo = "Mapa Registro Evasão Curso";
			getSuperParametroRelVO().setTituloRelatorio(titulo);
			if (unidadeEnsinoVO.getExisteLogoRelatorio()) {
				String urlLogoUnidadeEnsinoRelatorio = unidadeEnsinoVO.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/" + unidadeEnsinoVO.getNomeArquivoLogoRelatorio();
				String urlLogo = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + urlLogoUnidadeEnsinoRelatorio;
				getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", urlLogo);
			} else {
				getSuperParametroRelVO().adicionarParametro("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
			}				
			getSuperParametroRelVO().setListaObjetos(getMapaRegistroEvasaoCursoVO().getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO());
			getSuperParametroRelVO().adicionarParametro("nomeResponsavel", getMapaRegistroEvasaoCursoVO().getUsuarioResponsavel().getNome());
			getSuperParametroRelVO().adicionarParametro("dataRegistro", getMapaRegistroEvasaoCursoVO().getDataRegistroApresentar());
			getSuperParametroRelVO().adicionarParametro("tipoTrancamentoEnum", getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().getDescricao());
			String periodicidade = "";
			if(getMapaRegistroEvasaoCursoVO().getPeriodicidade().equals("SE")) {
				periodicidade = "Semestral";
			}else {
				periodicidade = "Anual";
			}
			getSuperParametroRelVO().adicionarParametro("periodicidade", periodicidade);
			getSuperParametroRelVO().adicionarParametro("ano", getMapaRegistroEvasaoCursoVO().getAno());
			getSuperParametroRelVO().adicionarParametro("semestre", getMapaRegistroEvasaoCursoVO().getSemestre());
			getSuperParametroRelVO().adicionarParametro("justificativa", getMapaRegistroEvasaoCursoVO().getJustificativa());
			if (Uteis.isAtributoPreenchido(getMapaRegistroEvasaoCursoVO().getMotivoCancelamentoTrancamento())) {
				MotivoCancelamentoTrancamentoVO lista = getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorChavePrimaria(getMapaRegistroEvasaoCursoVO().getMotivoCancelamentoTrancamento().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuario());
				getSuperParametroRelVO().adicionarParametro("motivoCancelamentoTrancamento", lista.getNome());
			}
			getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoApresentar());
			getSuperParametroRelVO().adicionarParametro("nivelEducacional", TipoNivelEducacional.getDescricao(getMapaRegistroEvasaoCursoVO().getNivelEducacional()));
			String textoCurso = "Todos";
			if (!getCursoApresentar().isEmpty()) {
				textoCurso = formatarApresentacaoListas(getCursoApresentar());
			}
			getSuperParametroRelVO().setCurso(textoCurso);
			String textoTurno = "Todos";
			if (!getTurnoApresentar().isEmpty()) {
				textoTurno = formatarApresentacaoListas(getTurnoApresentar());
			}
			getSuperParametroRelVO().setTurno(textoTurno);
			Integer	totalAlunosSituacaoProcessados = 0;
			for(MapaRegistroEvasaoCursoMatriculaPeriodoVO mapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO: getMapaRegistroEvasaoCursoVO().getMapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO()) {
				if(mapaRegistroAbandonoCursoTrancamentoMatriculaPeriodoVO.getSituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum().equals(SituacaoMapaRegistroEvasaoCursoMatriculaPeriodoEnum.PROCESSADO)) {
					totalAlunosSituacaoProcessados += 1;		
				}
			
			}
			getSuperParametroRelVO().adicionarParametro("quantidadeAlunosProcessados", totalAlunosSituacaoProcessados);
			
			getSuperParametroRelVO().adicionarParametro("trazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte", getMapaRegistroEvasaoCursoVO().isTrazerAlunoPreMatriculadoRenovouAnoSemestreSeguinte());
			getSuperParametroRelVO().adicionarParametro("trazerAlunoTrancadoAbandonadoAnoSemestreBase", getMapaRegistroEvasaoCursoVO().isTrazerAlunoTrancadoAbandonadoAnoSemestreBase());
			getSuperParametroRelVO().adicionarParametro("qtdDisciplinaReprovadas", getMapaRegistroEvasaoCursoVO().getQtdDisciplinaReprovadas());
			getSuperParametroRelVO().adicionarParametro("qtdDiasAlunosSemAcessoAva", getMapaRegistroEvasaoCursoVO().getQtdDiasAlunosSemAcessoAva());
			getSuperParametroRelVO().adicionarParametro("qtdTrancamentoEmExcesso", getMapaRegistroEvasaoCursoVO().getQtdTrancamentoEmExcesso());
			getSuperParametroRelVO().adicionarParametro("considerarTrancamentoConsecutivo", getMapaRegistroEvasaoCursoVO().isConsiderarTrancamentoConsecutivo());
			getSuperParametroRelVO().adicionarParametro("qtdMesAlunosRenovacaoSemAcessoAva", getMapaRegistroEvasaoCursoVO().getQtdMesAlunosRenovacaoSemAcessoAva());

			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}
	
	public void validarAnoSemestre(Boolean validarAnoSemestreMatriculaPeriodo) {
		try {
			if (!getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isRenovacaoAutomatica()) {
				String ano = new String(getMapaRegistroEvasaoCursoVO().getAno());
				String semestre = new String(getMapaRegistroEvasaoCursoVO().getSemestre());
				String anoRegistroEvasao = new String(getMapaRegistroEvasaoCursoVO().getAnoRegistroEvasao());
				String semestreRegistroEvasao = new String(getMapaRegistroEvasaoCursoVO().getSemestreRegistroEvasao());
				getMapaRegistroEvasaoCursoVO().setAno(Constantes.EMPTY);
				getMapaRegistroEvasaoCursoVO().setSemestre(Constantes.EMPTY);
				getMapaRegistroEvasaoCursoVO().setAnoRegistroEvasao(Constantes.EMPTY);
				getMapaRegistroEvasaoCursoVO().setSemestreRegistroEvasao(Constantes.EMPTY);
				if (!Uteis.isAtributoPreenchido(getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum())) {
					Uteis.checkState(Boolean.TRUE, UteisJSF.internacionalizar("msg_MapaRegistroAbandonoCursoTrancamento_tipoTrancamento"));
				}
				if (validarAnoSemestreMatriculaPeriodo) {
					if (getMapaRegistroEvasaoCursoVO().getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL.getValor())) {
						Uteis.checkState(!Uteis.isAtributoPreenchido(ano), UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_ano"));
						getMapaRegistroEvasaoCursoVO().setAno(ano);
						Uteis.checkState(!Uteis.isAtributoPreenchido(semestre), UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_semestre"));
						getMapaRegistroEvasaoCursoVO().setSemestre(semestre);
						if (getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isCancelamentoExcessoTrancamento() || getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isCancelamentoIngressante()) {
							if (Uteis.isAtributoPreenchido(anoRegistroEvasao)) {
								Uteis.checkState(Integer.valueOf(anoRegistroEvasao) < Integer.valueOf(ano), "O ANO de registro de evasão não pode ser anterior ao ano da matrícula do aluno");
								getMapaRegistroEvasaoCursoVO().setAnoRegistroEvasao(anoRegistroEvasao);
								if (Uteis.isAtributoPreenchido(semestreRegistroEvasao)) {
									Uteis.checkState(Integer.valueOf(anoRegistroEvasao).equals(Integer.valueOf(ano)) && Integer.valueOf(semestreRegistroEvasao) < Integer.valueOf(semestre), "O ANO/SEMESTRE de registro de evasão não pode ser anterior ano/semestre da matrícula do aluno");
									getMapaRegistroEvasaoCursoVO().setSemestreRegistroEvasao(semestreRegistroEvasao);
								}
							}
						} else {
							if (Uteis.isAtributoPreenchido(anoRegistroEvasao)) {
								Uteis.checkState(Integer.valueOf(anoRegistroEvasao) < Integer.valueOf(ano), "O ANO de registro de evasão não pode ser anterior ao ano da matrícula do aluno");
								getMapaRegistroEvasaoCursoVO().setAnoRegistroEvasao(anoRegistroEvasao);
								if (Uteis.isAtributoPreenchido(semestreRegistroEvasao)) {
									Uteis.checkState(Integer.valueOf(anoRegistroEvasao).equals(Integer.valueOf(ano)) && (Integer.valueOf(semestreRegistroEvasao) <= Integer.valueOf(semestre)), "O ANO/SEMESTRE de registro de evasão não pode ser anterior é igual ao ano/semestre da matrícula do aluno");
									getMapaRegistroEvasaoCursoVO().setSemestreRegistroEvasao(semestreRegistroEvasao);
								}
							}
						}
					} else {
						Uteis.checkState(!Uteis.isAtributoPreenchido(ano), UteisJSF.internacionalizar("msg_AlunosMatriculadosGeralRel_ano"));
						getMapaRegistroEvasaoCursoVO().setAno(ano);
						if (getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isCancelamentoExcessoTrancamento() || getMapaRegistroEvasaoCursoVO().getTipoTrancamentoEnum().isCancelamentoIngressante()) {
							Uteis.checkState(Integer.valueOf(anoRegistroEvasao) < Integer.valueOf(ano), "O ANO de registro de evasão não pode ser anterior ao ano da matrícula do aluno");
						} else {
							Uteis.checkState(Integer.valueOf(anoRegistroEvasao) <= Integer.valueOf(ano), "O ANO de registro de evasão não pode ser anterior é igual ao ano da matrícula do aluno");
						}
						getMapaRegistroEvasaoCursoVO().setAnoRegistroEvasao(anoRegistroEvasao);
					}
				} else {
					if (Uteis.isAtributoPreenchido(ano)) {
						getMapaRegistroEvasaoCursoVO().setAno(ano);
						getMapaRegistroEvasaoCursoVO().setSemestre(semestre);
						getMapaRegistroEvasaoCursoVO().setAnoRegistroEvasao(Constantes.EMPTY);
						getMapaRegistroEvasaoCursoVO().setSemestreRegistroEvasao(Constantes.EMPTY);
					}
				}
				limparMensagem();
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
}