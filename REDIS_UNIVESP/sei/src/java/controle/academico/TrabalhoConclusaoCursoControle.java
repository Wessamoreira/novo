package controle.academico;

import java.util.Date;

import javax.annotation.PostConstruct;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TrabalhoConclusaoCursoVO;
import negocio.comuns.academico.enumeradores.ClassificacaoDisciplinaEnum;
import negocio.comuns.blackboard.SalaAulaBlackboardPessoaVO;
import negocio.comuns.blackboard.SalaAulaBlackboardVO;
import negocio.comuns.blackboard.enumeradores.OperacaoEnsalacaoBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardEnum;
import negocio.comuns.blackboard.enumeradores.TipoSalaAulaBlackboardPessoaEnum;
import negocio.comuns.secretaria.CalendarioAgrupamentoTccVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

@Controller("TrabalhoConclusaoCursoControle")
@Lazy
@Scope("viewScope")
public class TrabalhoConclusaoCursoControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7480555046416116688L;
	private MatriculaVO matricula;
	private TrabalhoConclusaoCursoVO trabalhoConclusaoCurso;
	private CalendarioAgrupamentoTccVO calendarioAgrupamentoTccVO;
	private boolean calendarioAgrupamentoTccPeriodoLiberado = false;
	private boolean permiteAdicionarUsuarioGrupo = false;	
	private DataModelo dadosConsultaSalaAula;

	@PostConstruct
	public void postConstructInicializarTCCAluno() {
		try {
			MatriculaVO mat = null;
			if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
			VisaoAlunoControle visaoAlunoControle = (VisaoAlunoControle) getControlador("VisaoAlunoControle");
				if (visaoAlunoControle != null) {
					mat = visaoAlunoControle.getMatricula();
				}
			}
			inicializarTCCAluno(mat);
		} catch (Exception e) {
			
		}
	}
	
	public void inicializarTCCAluno(MatriculaVO mat) {
		try {
//			if ((getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) && mat != null && !mat.getMatricula().isEmpty()) {
//				setDadosConsultaSalaAula(new DataModelo(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, 10, 0, getUsuarioLogadoClone()));
//				getDadosConsultaSalaAula().setPaginaAtual(1);
//				getDadosConsultaSalaAula().setPage(1);
//				setMatricula((MatriculaVO) Uteis.clonar(mat));
//				MatriculaPeriodoVO  matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoPorMatricula(getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone());
//				//boolean existeInscricaoAlunoSalaAulaBlackboard = getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarSeExisteInscricaoSalaAulaBlackboardGrupo(getMatricula().getMatricula(), TipoSalaAulaBlackboardPessoaEnum.ALUNO, TipoSalaAulaBlackboardEnum.TCC_GRUPO, null, null, getUsuarioLogadoClone());
//				//boolean existeInscricaoAlunoSalaAulaBlackboardNoPeriodo = getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarSeExisteInscricaoSalaAulaBlackboardGrupo(getMatricula().getMatricula(), TipoSalaAulaBlackboardPessoaEnum.ALUNO, TipoSalaAulaBlackboardEnum.TCC_GRUPO, matriculaPeriodoVO.getAno(), matriculaPeriodoVO.getSemestre(), getUsuarioLogadoClone());
//				setCalendarioAgrupamentoTccVO(getFacadeFactory().getCalendarioAgrupamentoTccFacade().consultarPorClassificacaoPorAnoPorSemestre(ClassificacaoDisciplinaEnum.TCC, matriculaPeriodoVO.getAno(), matriculaPeriodoVO.getSemestre(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogadoClone()));
//				if (Uteis.isAtributoPreenchido(getCalendarioAgrupamentoTccVO()) && UteisData.getCompareData(new Date(), getCalendarioAgrupamentoTccVO().getDataInicial()) >= 0 && UteisData.getCompareData(getCalendarioAgrupamentoTccVO().getDataFinal(), new Date()) >= 0 && (!existeInscricaoAlunoSalaAulaBlackboard || (existeInscricaoAlunoSalaAulaBlackboard && existeInscricaoAlunoSalaAulaBlackboardNoPeriodo))) {
//					setCalendarioAgrupamentoTccPeriodoLiberado(true);
//					setPermiteAdicionarUsuarioGrupo(!existeInscricaoAlunoSalaAulaBlackboard);
//					getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardGrupoTcc(getMatricula().getMatricula(), getMatricula().getGradeCurricularAtual(), getCalendarioAgrupamentoTccVO().getAno(), getCalendarioAgrupamentoTccVO().getSemestre(), getDadosConsultaSalaAula(), getUsuarioLogadoClone());
//				} else {
//					setCalendarioAgrupamentoTccPeriodoLiberado(false);
//					//getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardGrupoPorMatricula(getMatricula().getMatricula(), getDadosConsultaSalaAula(), getUsuarioLogadoClone());
//				}
//			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
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
	
	public void realizarInclusaoSalaAulaGrupo() {
		try {
			SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap() .get("salaAulaItem");
			SalaAulaBlackboardPessoaVO sabAluno = new SalaAulaBlackboardPessoaVO();
			if (getMatricula().getAluno().getListaPessoaEmailInstitucionalVO().isEmpty()) {
				sabAluno.setPessoaEmailInstitucionalVO(getFacadeFactory().getPessoaEmailInstitucionalFacade().consultarPorPessoa(getMatricula().getAluno().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone()));
			} else {
				sabAluno.setPessoaEmailInstitucionalVO(getMatricula().getAluno().getListaPessoaEmailInstitucionalVO().get(0));
			}
			sabAluno.setMatricula(getMatricula().getMatricula());
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarEnvioConviteAlunoSalaAulaBlackboardGrupo(obj, sabAluno, OperacaoEnsalacaoBlackboardEnum.INCLUIR,getUsuarioLogadoClone());
			realizarAtualizacaoSalaAulaBlackboardPessoa(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	
	
	public void realizarExclusaoSalaAulaGrupo() {
		try {
			SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap() .get("salaAulaItem");
			SalaAulaBlackboardPessoaVO sabAluno = obj.getSalaAulaBlackboardGrupoPessoaVO(getUsuarioLogadoClone());
			getFacadeFactory().getSalaAulaBlackboardFacade().realizarEnvioConviteAlunoSalaAulaBlackboardGrupo(obj, sabAluno, OperacaoEnsalacaoBlackboardEnum.EXCLUIR, getUsuarioLogadoClone());
			realizarAtualizacaoSalaAulaBlackboardPessoa(obj);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private void realizarAtualizacaoSalaAulaBlackboardPessoa(SalaAulaBlackboardVO obj) throws Exception {
		//getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarAlunosPorSalaAulaBlackboardOtimizado(obj,  Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogadoClone());
		getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardGrupoTcc(getMatricula().getMatricula(), getMatricula().getGradeCurricularAtual(), getCalendarioAgrupamentoTccVO().getAno(), getCalendarioAgrupamentoTccVO().getSemestre(), getDadosConsultaSalaAula(), getUsuarioLogadoClone());
		//setPermiteAdicionarUsuarioGrupo(!getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarSeExisteInscricaoSalaAulaBlackboardGrupo(getMatricula().getMatricula(), TipoSalaAulaBlackboardPessoaEnum.ALUNO, TipoSalaAulaBlackboardEnum.TCC_GRUPO, getCalendarioAgrupamentoTccVO().getAno(), getCalendarioAgrupamentoTccVO().getSemestre(), getUsuarioLogadoClone()));
	}

	public void persistir() {
		try {
			executarValidacaoSimulacaoVisaoAluno();
			//getFacadeFactory().getTrabalhoConclusaoCursoFacade().persitir(getTrabalhoConclusaoCurso(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
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
	
	public void scrollerListenerSalaAula(DataScrollEvent dataScrollerEvent) throws Exception {
		getDadosConsultaSalaAula().setPage(dataScrollerEvent.getPage());
		getDadosConsultaSalaAula().setPaginaAtual(dataScrollerEvent.getPage());
		getFacadeFactory().getSalaAulaBlackboardFacade().consultarSalaAulaBlackboardGrupoTcc(getMatricula().getMatricula(), getMatricula().getGradeCurricularAtual(), getCalendarioAgrupamentoTccVO().getAno(), getCalendarioAgrupamentoTccVO().getSemestre(), getDadosConsultaSalaAula(), getUsuarioLogadoClone());
	}
	
	public void scrollerListenerAlunos(DataScrollEvent dataScrollerEvent)  {
		try {
			FiltroRelatorioAcademicoVO fra = new FiltroRelatorioAcademicoVO();
			fra.realizarDesmarcarTodasSituacoes();
			SalaAulaBlackboardVO obj = (SalaAulaBlackboardVO) context().getExternalContext().getRequestMap() .get("salaAulaItem");
			obj.getDadosConsultaAlunos().setPage(dataScrollerEvent.getPage());
			obj.getDadosConsultaAlunos().setPaginaAtual(dataScrollerEvent.getPage());			
			getFacadeFactory().getSalaAulaBlackboardPessoaFacade().consultarAlunosPorSalaAulaBlackboardOtimizado(obj, fra, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());	
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
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

	public TrabalhoConclusaoCursoVO getTrabalhoConclusaoCurso() {
		if (trabalhoConclusaoCurso == null) {
			trabalhoConclusaoCurso = new TrabalhoConclusaoCursoVO();
		}
		return trabalhoConclusaoCurso;
	}

	public void setTrabalhoConclusaoCurso(TrabalhoConclusaoCursoVO trabalhoConclusaoCurso) {
		this.trabalhoConclusaoCurso = trabalhoConclusaoCurso;
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
	
	

	public boolean isCalendarioAgrupamentoTccPeriodoLiberado() {
		return calendarioAgrupamentoTccPeriodoLiberado;
	}

	public void setCalendarioAgrupamentoTccPeriodoLiberado(boolean calendarioAgrupamentoTccPeriodoLiberado) {
		this.calendarioAgrupamentoTccPeriodoLiberado = calendarioAgrupamentoTccPeriodoLiberado;
	}

	public boolean isPermiteAdicionarUsuarioGrupo() {		
		return permiteAdicionarUsuarioGrupo;
	}

	public void setPermiteAdicionarUsuarioGrupo(boolean permiteAdicionarUsuarioGrupo) {
		this.permiteAdicionarUsuarioGrupo = permiteAdicionarUsuarioGrupo;
	}

	public DataModelo getDadosConsultaSalaAula() {
		if (dadosConsultaSalaAula == null) {
			dadosConsultaSalaAula = new DataModelo();
			dadosConsultaSalaAula.setLimitePorPagina(10);
			dadosConsultaSalaAula.setPaginaAtual(0);
			dadosConsultaSalaAula.setPage(0);
		}
		return dadosConsultaSalaAula;
	}

	public void setDadosConsultaSalaAula(DataModelo dadosConsultaSalaAula) {
		this.dadosConsultaSalaAula = dadosConsultaSalaAula;
	}
	
	
	
	
	
	

}