package controle.secretaria;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.FilterFactory;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.enumeradores.OperacaoEnsalacaoBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;
import negocio.comuns.ead.SalaAulaBlackboardOperacaoVO;
import negocio.comuns.secretaria.CalendarioAgrupamentoTccVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.blackboard.SalaAulaBlackboardOperacao;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;


@Controller("CalendarioAgrupamentoTccControle")
@Scope("viewScope")
@Lazy
public class CalendarioAgrupamentoTccControle extends SuperControle{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5038521991143941173L;
	private static final String TELA_FORM = "calendarioAgrupamentoTccForm.xhtml";
	private static final String TELA_CONS = "calendarioAgrupamentoTccCons.xhtml";
	private static final String CONTEXT_PARA_EDICAO = "calendarioAgrupamentoTccItens";
	private CalendarioAgrupamentoTccVO calendarioAgrupamentoTccVO;
	private List<SelectItem> listaClassificacaoAgrupamento;
	private DataModelo dadosConsultaSalaAula;
	private MatriculaVO matricula;
	private List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVO;
	private Integer disciplina;
	private List<SelectItem> listaSelectItemDisciplina;
	
	@PostConstruct
	public void init() {
		postConstructInicializarProjetoIntegradorAluno();
		postConstructInicializarTccAluno();
	}
	
	
	public void postConstructInicializarProjetoIntegradorAluno() {
		try {
			if(context() != null && context().getExternalContext().getSessionMap().get("capi") != null && context().getExternalContext().getSessionMap().get("capi") instanceof CalendarioAgrupamentoTccVO) {
			CalendarioAgrupamentoTccVO ca = (CalendarioAgrupamentoTccVO) context().getExternalContext().getSessionMap().get("capi");
			if (Uteis.isAtributoPreenchido(ca) && ca.getClassificacaoAgrupamento().isProjetoIntegrador()
					&& context().getExternalContext().getSessionMap().get("matriculapi") != null
					&& context().getExternalContext().getSessionMap().get("matriculapi") instanceof MatriculaVO
					&& context().getExternalContext().getSessionMap().get("listaMptd") != null
					&& context().getExternalContext().getSessionMap().get("listaMptd") instanceof List) {				
				getListaSelectItemDisciplina().clear();
				setMatricula((MatriculaVO) context().getExternalContext().getSessionMap().get("matriculapi"));	
				setListaMatriculaPeriodoTurmaDisciplinaVO((List<MatriculaPeriodoTurmaDisciplinaVO>) context().getExternalContext().getSessionMap().get("listaMptd"));
				getListaMatriculaPeriodoTurmaDisciplinaVO().stream().forEach(p-> {
					if(!Uteis.isAtributoPreenchido(getDisciplina()) && (ca.getCalendarioAgrupamentoDisciplinaVOs().isEmpty()
							|| ca.getCalendarioAgrupamentoDisciplinaVOs().stream().anyMatch(t -> t.getDisciplinaVO().getCodigo().equals(p.getDisciplina().getCodigo())))) {
						setDisciplina(p.getDisciplina().getCodigo());
					}
					getListaSelectItemDisciplina().add(new SelectItem(p.getDisciplina().getCodigo(), p.getDisciplina().getDescricaoAbreviaturaNome()));
				});
				if(Uteis.isAtributoPreenchido(getDisciplina())) {					
					setCalendarioAgrupamentoTccVO(ca);				
					if(getCalendarioAgrupamentoTccVO().isPeriodoLiberado()) {
						consultarSalaAulaBlackboardGruposDisponiveis();	
					}
				}
			}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(context() != null ) {
				context().getExternalContext().getSessionMap().remove("capi");
				context().getExternalContext().getSessionMap().remove("matriculapi");
				context().getExternalContext().getSessionMap().remove("listaMptd");
			}
		}
		
	}
		
	
	public void postConstructInicializarTccAluno() {
		try {
			if (context() != null && context().getExternalContext().getSessionMap().get("catcc") != null && context().getExternalContext().getSessionMap().get("catcc") instanceof Boolean) {
				Boolean isCalendarioAgrupamentoTcc = (Boolean) context().getExternalContext().getSessionMap().get("catcc");
				if (isCalendarioAgrupamentoTcc != null && isCalendarioAgrupamentoTcc && context().getExternalContext().getSessionMap().get("matriculatcc") != null && context().getExternalContext().getSessionMap().get("matriculatcc") instanceof MatriculaVO && context().getExternalContext().getSessionMap().get("matriculaperiodotcc") != null && context().getExternalContext().getSessionMap().get("matriculaperiodotcc") instanceof MatriculaPeriodoVO) {
					setMatricula((MatriculaVO) context().getExternalContext().getSessionMap().get("matriculatcc"));
					DisciplinaVO disciplinaVO = getFacadeFactory().getDisciplinaFacade().consultarPorGradeCurricularDisciplinaPadraoTcc(getMatricula().getGradeCurricularAtual().getCodigo(), false, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
					boolean isAlunoJaAprovado = getFacadeFactory().getHistoricoFacade().consultarSeExisteHistoricoDisciplinaClassificadaTCCAprovadasPorMatricula(getMatricula().getMatricula(), getUsuarioLogadoClone());
					if (isAlunoJaAprovado) {
						setCalendarioAgrupamentoTccVO(getFacadeFactory().getCalendarioAgrupamentoTccFacade().consultarPorClassificacaoPorHistoricoAnoSemestreAprovadoPorDisciplina(disciplinaVO.getCodigo(), getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone()));
						getCalendarioAgrupamentoTccVO().setPeriodoLiberado(false);
						getCalendarioAgrupamentoTccVO().setAlunoJaAprovado(isAlunoJaAprovado);
						 if(Uteis.isAtributoPreenchido(getCalendarioAgrupamentoTccVO())){
							 getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardGrupoPorMatriculaPorTipoSalaAulaBlackboardEnum(getMatricula(), 0, TipoSalaAulaBlackboardEnum.TCC_GRUPO, getCalendarioAgrupamentoTccVO().getAno(), getCalendarioAgrupamentoTccVO().getSemestre(), getDadosConsultaSalaAula(), getUsuarioLogadoClone());	 
						 }
					} else {
						MatriculaPeriodoVO mp = ((MatriculaPeriodoVO) context().getExternalContext().getSessionMap().get("matriculaperiodotcc"));						
						if (Uteis.isAtributoPreenchido(disciplinaVO)) {
							setCalendarioAgrupamentoTccVO(getFacadeFactory().getCalendarioAgrupamentoTccFacade().consultarPorClassificacaoPorAnoPorSemestrePorDisciplina(disciplinaVO.getCodigo(), ClassificacaoDisciplinaEnum.TCC, mp.getAno(), mp.getSemestre(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone()));
							if (Uteis.isAtributoPreenchido(getCalendarioAgrupamentoTccVO()) && UteisData.getCompareData(new Date(), getCalendarioAgrupamentoTccVO().getDataInicial()) >= 0 && UteisData.getCompareData(getCalendarioAgrupamentoTccVO().getDataFinal(), new Date()) >= 0) {
								getCalendarioAgrupamentoTccVO().setPeriodoLiberado(true);
								consultarSalaAulaBlackboardGruposDisponiveis();
							} else if(Uteis.isAtributoPreenchido(getCalendarioAgrupamentoTccVO())){
								getCalendarioAgrupamentoTccVO().setPeriodoLiberado(false);
								getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardGrupoPorMatriculaPorTipoSalaAulaBlackboardEnum(getMatricula(), 0, TipoSalaAulaBlackboardEnum.TCC_GRUPO, getCalendarioAgrupamentoTccVO().getAno(), getCalendarioAgrupamentoTccVO().getSemestre(), getDadosConsultaSalaAula(), getUsuarioLogadoClone());
							}
						} else {
							setCalendarioAgrupamentoTccVO(new CalendarioAgrupamentoTccVO());
							getCalendarioAgrupamentoTccVO().setPeriodoLiberado(false);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (context() != null) {
				context().getExternalContext().getSessionMap().remove("catcc");
				context().getExternalContext().getSessionMap().remove("matriculatcc");
				context().getExternalContext().getSessionMap().remove("matriculaperiodotcc");
			}
		}
	}
	
	
	
	public void realizarNavegacaoSalaAulaBlackboardPorTcc() {
		try {
			SalaAulaBlackboardPessoaVO sabAluno = new SalaAulaBlackboardPessoaVO();
			if (getMatricula().getAluno().getListaPessoaEmailInstitucionalVO().isEmpty()) {
				sabAluno.setPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(getMatricula().getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone()));
			} else {
				sabAluno.setPessoaEmailInstitucionalVO(getMatricula().getAluno().getListaPessoaEmailInstitucionalVO().get(0));
			}
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarVerificacaoSeAlunoEstaVinculadoSalaAulaBlackboard(getMatricula().getSalaAulaBlackboardTcc(), sabAluno, getUsuarioLogadoClone());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarSalaAulaBlackboardGruposDisponiveis() {
		try {
			limparMensagem();
			setDadosConsultaSalaAula(new DataModelo(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, 12, 0, getUsuarioLogadoClone()));
			getDadosConsultaSalaAula().setPaginaAtual(1);
			getDadosConsultaSalaAula().setPage(1);
			if(getCalendarioAgrupamentoTccVO().getClassificacaoAgrupamento().isTcc() ) {
				getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardGrupoTcc(getMatricula().getMatricula(), getMatricula().getGradeCurricularAtual(), getCalendarioAgrupamentoTccVO().getAno(), getCalendarioAgrupamentoTccVO().getSemestre(), getDadosConsultaSalaAula(), getUsuarioLogadoClone());
				if(getMatricula().getIsAtiva()){
					getCalendarioAgrupamentoTccVO().setPermiteAdicionarUsuarioGrupo(!getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarSeExisteInscricaoSalaAulaBlackboardGrupo(getMatricula(), getDisciplina(), TipoSalaAulaBlackboardPessoaEnum.ALUNO, TipoSalaAulaBlackboardEnum.TCC_GRUPO, getCalendarioAgrupamentoTccVO().getAno(), getCalendarioAgrupamentoTccVO().getSemestre(), getUsuarioLogadoClone()));
				}else{
					getCalendarioAgrupamentoTccVO().setPermiteAdicionarUsuarioGrupo(false);
				}
			}else if(getCalendarioAgrupamentoTccVO().getClassificacaoAgrupamento().isProjetoIntegrador()){
				if(getVisaoAlunoControle() != null) {
					setCalendarioAgrupamentoTccVO(getFacadeFactory().getCalendarioAgrupamentoTccFacade().consultarPorClassificacaoPorAnoPorSemestrePorDisciplina(getDisciplina(), ClassificacaoDisciplinaEnum.PROJETO_INTEGRADOR, getVisaoAlunoControle().getMatriculaPeriodoVO().getAno(), getVisaoAlunoControle().getMatriculaPeriodoVO().getSemestre(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone()));					
				}
				if(Uteis.isAtributoPreenchido(getCalendarioAgrupamentoTccVO()) 
						&& UteisData.getCompareData(new Date(), getCalendarioAgrupamentoTccVO().getDataInicial()) >= 0 
						&& UteisData.getCompareData(getCalendarioAgrupamentoTccVO().getDataFinal(), new Date()) >= 0 ) {
						getCalendarioAgrupamentoTccVO().setPeriodoLiberado(true);
						getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardGrupoProjetoIntegrador(getMatricula().getMatricula(), getMatricula().getUnidadeEnsino().getCodigo(), getDisciplina(), getCalendarioAgrupamentoTccVO().getAno(), getCalendarioAgrupamentoTccVO().getSemestre(), getDadosConsultaSalaAula(), getUsuarioLogadoClone());
					if(getMatricula().getIsAtiva()){
						getCalendarioAgrupamentoTccVO().setPermiteAdicionarUsuarioGrupo(!getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarSeExisteInscricaoSalaAulaBlackboardGrupo(getMatricula(), getDisciplina(), TipoSalaAulaBlackboardPessoaEnum.ALUNO, TipoSalaAulaBlackboardEnum.PROJETO_INTEGRADOR_GRUPO, getCalendarioAgrupamentoTccVO().getAno(), getCalendarioAgrupamentoTccVO().getSemestre(), getUsuarioLogadoClone()));
					}else{
						getCalendarioAgrupamentoTccVO().setPermiteAdicionarUsuarioGrupo(false);
					}
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void realizarInclusaoSalaAulaGrupo() {
		SalaAulaBlackboardVO obj = null;
		SalaAulaBlackboardPessoaVO sabAluno = null; 
		try {
			Uteis.checkState(!getMatricula().getIsAtiva(), "Nã foi possível realizar essa operação, pois a matrícula do aluno não se encontra Ativa.");
			obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap() .get("salaAulaItem");
			sabAluno = new SalaAulaBlackboardPessoaVO();
			if (getMatricula().getAluno().getListaPessoaEmailInstitucionalVO().isEmpty()) {
				sabAluno.setPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(getMatricula().getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone()));
			} else {
				sabAluno.setPessoaEmailInstitucionalVO(getMatricula().getAluno().getListaPessoaEmailInstitucionalVO().get(0));
			}
			sabAluno.setMatricula(getMatricula().getMatricula());
			if(getCalendarioAgrupamentoTccVO().getClassificacaoAgrupamento().isProjetoIntegrador()) {
				sabAluno.setMatriculaPeriodoTurmaDisciplina(getListaMatriculaPeriodoTurmaDisciplinaVO().stream().filter(p-> p.getDisciplina().getCodigo().equals(getDisciplina())).findFirst().get().getCodigo());
			}
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarEnvioConviteAlunoSalaAulaBlackboardGrupo(obj, sabAluno, OperacaoEnsalacaoBlackboardEnum.INCLUIR,getUsuarioLogadoClone());
			realizarAtualizacaoSalaAulaBlackboardPessoa(obj);
		} catch (Exception e) {
			if(obj != null && sabAluno != null) {
				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirLogErro(obj, TipoSalaAulaBlackboardPessoaEnum.ALUNO, sabAluno.getPessoaEmailInstitucionalVO().getPessoaVO().getCodigo(), sabAluno.getPessoaEmailInstitucionalVO().getEmail(), sabAluno.getMatricula(), sabAluno.getMatriculaPeriodoTurmaDisciplina(), obj.getAno(), obj.getSemestre(), obj.getIdSalaAulaBlackboard(), SalaAulaBlackboardOperacao.ORIGEM_SALA_AULA_BLACKBOARD_PESSOA, SalaAulaBlackboardOperacao.OPERACAO_INCLUIR, e.getMessage(), getUsuarioLogado());
			}
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	
	
	public void realizarExclusaoSalaAulaGrupo() {
		SalaAulaBlackboardVO obj = null;
		SalaAulaBlackboardPessoaVO sabAluno = null; 
		try {
			obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap() .get("salaAulaItem");
			sabAluno = obj.getSalaAulaBlackboardGrupoPessoaVO(getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarEnvioConviteAlunoSalaAulaBlackboardGrupo(obj, sabAluno, OperacaoEnsalacaoBlackboardEnum.EXCLUIR, getUsuarioLogadoClone());
			realizarAtualizacaoSalaAulaBlackboardPessoa(obj);
		} catch (Exception e) {
			if(obj != null && sabAluno != null) {
				getFacadeFactory().getSalaAulaBlackboardOperacaoFacade().incluirLogErro(obj, TipoSalaAulaBlackboardPessoaEnum.ALUNO, sabAluno.getPessoaEmailInstitucionalVO().getPessoaVO().getCodigo(), sabAluno.getPessoaEmailInstitucionalVO().getEmail(), sabAluno.getMatricula(), sabAluno.getMatriculaPeriodoTurmaDisciplina(), obj.getAno(), obj.getSemestre(), obj.getIdSalaAulaBlackboard(), SalaAulaBlackboardOperacao.ORIGEM_SALA_AULA_BLACKBOARD_PESSOA, SalaAulaBlackboardOperacao.OPERACAO_DELETAR, e.getMessage(), getUsuarioLogado());
			}
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private void realizarAtualizacaoSalaAulaBlackboardPessoa(SalaAulaBlackboardVO obj) throws Exception {
		if(obj.getTipoSalaAulaBlackboardEnum().isTccGrupo()) {
			getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardGrupoTcc(getMatricula().getMatricula(), getMatricula().getGradeCurricularAtual(), getCalendarioAgrupamentoTccVO().getAno(), getCalendarioAgrupamentoTccVO().getSemestre(), getDadosConsultaSalaAula(), getUsuarioLogadoClone());	
		}else {
			getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardGrupoProjetoIntegrador(getMatricula().getMatricula(), getMatricula().getUnidadeEnsino().getCodigo(), getDisciplina(), getCalendarioAgrupamentoTccVO().getAno(), getCalendarioAgrupamentoTccVO().getSemestre(), getDadosConsultaSalaAula(), getUsuarioLogadoClone());
		}
		if(getMatricula().getIsAtiva()){
			getCalendarioAgrupamentoTccVO().setPermiteAdicionarUsuarioGrupo(!getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarSeExisteInscricaoSalaAulaBlackboardGrupo(getMatricula(), getDisciplina(), TipoSalaAulaBlackboardPessoaEnum.ALUNO, obj.getTipoSalaAulaBlackboardEnum(), getCalendarioAgrupamentoTccVO().getAno(), getCalendarioAgrupamentoTccVO().getSemestre(), getUsuarioLogadoClone()));
		}else{
			getCalendarioAgrupamentoTccVO().setPermiteAdicionarUsuarioGrupo(false);
		}
	}
	
	public void consultarAlunosPorNomeEmail()  {
		try {
			FiltroRelatorioAcademicoVO fra = new FiltroRelatorioAcademicoVO();
			fra.realizarDesmarcarTodasSituacoes();
			SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap() .get("salaAulaItem");
			obj.getDadosConsultaAlunos().setPage(0);
			obj.getDadosConsultaAlunos().setPaginaAtual(0);
			getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarAlunosPorSalaAulaBlackboardOtimizado(obj, fra, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void scrollerListenerSalaAula(DataScrollEvent dataScrollerEvent)  {
		try {
			getDadosConsultaSalaAula().setPage(dataScrollerEvent.getPage());
			getDadosConsultaSalaAula().setPaginaAtual(dataScrollerEvent.getPage());
			if(getCalendarioAgrupamentoTccVO().getClassificacaoAgrupamento().isTcc()) {
				getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardGrupoTcc(getMatricula().getMatricula(), getMatricula().getGradeCurricularAtual(), getCalendarioAgrupamentoTccVO().getAno(), getCalendarioAgrupamentoTccVO().getSemestre(), getDadosConsultaSalaAula(), getUsuarioLogadoClone());
			}else {
				getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardGrupoProjetoIntegrador(getMatricula().getMatricula(), getMatricula().getUnidadeEnsino().getCodigo(), getDisciplina(), getCalendarioAgrupamentoTccVO().getAno(), getCalendarioAgrupamentoTccVO().getSemestre(), getDadosConsultaSalaAula(), getUsuarioLogadoClone());
			}	
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
	}
	
	public void scrollerListenerAlunos(DataScrollEvent dataScrollerEvent)  {
		try {
			FiltroRelatorioAcademicoVO fra = new FiltroRelatorioAcademicoVO();
			fra.realizarDesmarcarTodasSituacoes();
			SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap() .get("salaAulaItem");
			obj.getDadosConsultaAlunos().setPage(dataScrollerEvent.getPage());
			obj.getDadosConsultaAlunos().setPaginaAtual(dataScrollerEvent.getPage());			
			getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarAlunosPorSalaAulaBlackboardOtimizado(obj, fra, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String novo() {
		removerObjetoMemoria(this);
		setCalendarioAgrupamentoTccVO(new CalendarioAgrupamentoTccVO());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String editar() {
		try {
			CalendarioAgrupamentoTccVO obj = (CalendarioAgrupamentoTccVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setCalendarioAgrupamentoTccVO(getFacadeFactory().getCalendarioAgrupamentoTccFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getCalendarioAgrupamentoTccVO().getCalendarioAgrupamentoDisciplinaVOs().addAll(getFacadeFactory().getCalendarioAgrupamentoDisciplinaFacade().consultarPorDisciplinaNaoSelecionadaPorCalendarioAgrupamento(getCalendarioAgrupamentoTccVO(), getUsuarioLogado()));
			Ordenacao.ordenarLista(getCalendarioAgrupamentoTccVO().getCalendarioAgrupamentoDisciplinaVOs(), "ordenacao");
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public void persistir() {
		try {
			getFacadeFactory().getCalendarioAgrupamentoTccFacade().persistir(getCalendarioAgrupamentoTccVO(), true, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_gravados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getCalendarioAgrupamentoTccFacade().excluir(getCalendarioAgrupamentoTccVO(), true, getUsuarioLogado());
			novo();
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}

	public String consultar() {
		try {
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getCalendarioAgrupamentoTccFacade().consultar(getControleConsultaOtimizado(), getCalendarioAgrupamentoTccVO());
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

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}
	
	public List<SelectItem> getListaClassificacaoAgrupamento() {
		if (listaClassificacaoAgrupamento == null) {
			listaClassificacaoAgrupamento = new ArrayList<SelectItem>(0);
			listaClassificacaoAgrupamento.add(new SelectItem(ClassificacaoDisciplinaEnum.NENHUMA.name(), ""));
			listaClassificacaoAgrupamento.add(new SelectItem(ClassificacaoDisciplinaEnum.TCC.name(), "Tcc"));
			listaClassificacaoAgrupamento.add(new SelectItem(ClassificacaoDisciplinaEnum.PROJETO_INTEGRADOR.name(), "Projeto Integrador"));
		}
		return listaClassificacaoAgrupamento;
	}
	
	public void setListaClassificacaoAgrupamento(List<SelectItem> listaClassificacaoAgrupamento) {
		this.listaClassificacaoAgrupamento = listaClassificacaoAgrupamento;
	}

	public CalendarioAgrupamentoTccVO getCalendarioAgrupamentoTccVO() {
		if (calendarioAgrupamentoTccVO == null) {
			calendarioAgrupamentoTccVO = new CalendarioAgrupamentoTccVO();
		}
		return calendarioAgrupamentoTccVO;
	}

	public void setCalendarioAgrupamentoTccVO(CalendarioAgrupamentoTccVO calendarioAgrupamentoTccVO) {
		this.calendarioAgrupamentoTccVO = calendarioAgrupamentoTccVO;
	}
	
	

	public List<MatriculaPeriodoTurmaDisciplinaVO> getListaMatriculaPeriodoTurmaDisciplinaVO() {
		if (listaMatriculaPeriodoTurmaDisciplinaVO == null) {
			listaMatriculaPeriodoTurmaDisciplinaVO = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>();
		}
		return listaMatriculaPeriodoTurmaDisciplinaVO;
	}

	public void setListaMatriculaPeriodoTurmaDisciplinaVO(List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplinaVO) {
		this.listaMatriculaPeriodoTurmaDisciplinaVO = listaMatriculaPeriodoTurmaDisciplinaVO;
	}

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}
	
	

	public Integer getDisciplina() {
		if (disciplina == null) {
			disciplina = 0;
		}
		return disciplina;
	}

	public void setDisciplina(Integer disciplina) {
		this.disciplina = disciplina;
	}
	
	

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new  ArrayList<>();
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public DataModelo getDadosConsultaSalaAula() {
		if (dadosConsultaSalaAula == null) {
			dadosConsultaSalaAula = new DataModelo();
			dadosConsultaSalaAula.setLimitePorPagina(12);
			dadosConsultaSalaAula.setPaginaAtual(0);
			dadosConsultaSalaAula.setPage(0);
		}
		return dadosConsultaSalaAula;
	}

	public void setDadosConsultaSalaAula(DataModelo dadosConsultaSalaAula) {
		this.dadosConsultaSalaAula = dadosConsultaSalaAula;
	}
	

	public void consultarDisciplinas() {
		try {
			limparMensagem();
			if(getCalendarioAgrupamentoTccVO().getClassificacaoAgrupamento().isProjetoIntegrador() || getCalendarioAgrupamentoTccVO().getClassificacaoAgrupamento().isTcc()) {
				getCalendarioAgrupamentoTccVO().setCalendarioAgrupamentoDisciplinaVOs(getFacadeFactory().getCalendarioAgrupamentoDisciplinaFacade().consultarPorDisciplinaNaoSelecionadaPorCalendarioAgrupamento(getCalendarioAgrupamentoTccVO(), getUsuarioLogado()));
				getCalendarioAgrupamentoTccVO().getCalendarioAgrupamentoDisciplinaVOs().removeIf(d -> !d.getDisciplinaVO().getClassificacaoDisciplina().equals(getCalendarioAgrupamentoTccVO().getClassificacaoAgrupamento()));
			}else {
				getCalendarioAgrupamentoTccVO().setCalendarioAgrupamentoDisciplinaVOs(null);
			}
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		
	}
	
	public void adicionarTodasDisciplinas() {
		final FilterFactory ff = (FilterFactory) getControlador("FilterFactory");
		getCalendarioAgrupamentoTccVO().getCalendarioAgrupamentoDisciplinaVOs().forEach(t -> { if(!t.getSelecionado() && ff.getMapFilter().get("filtrosDisNaoSel").accept(t)) {
			t.setSelecionado(true);
		}});
	}
	
	public void removerTodasDisciplinas() {		
		final FilterFactory ff = (FilterFactory) getControlador("FilterFactory");
		getCalendarioAgrupamentoTccVO().getCalendarioAgrupamentoDisciplinaVOs().forEach(t -> { if(t.getSelecionado() && ff.getMapFilter().get("filtrosDisSel").accept(t)) {
			t.setSelecionado(false);
		}});
	}

}
