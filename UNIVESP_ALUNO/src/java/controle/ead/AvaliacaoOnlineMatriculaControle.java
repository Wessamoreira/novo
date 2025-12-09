package controle.ead;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import jakarta.annotation.PostConstruct;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ConteudoUnidadePaginaRecursoEducacionalVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaRespostaQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.enumeradores.SituacaoAvaliacaoOnlineMatriculaEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.TipoCalendarioAtividadeMatriculaEnum;
import negocio.comuns.ead.enumeradores.TipoOrigemEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
//import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 * @author Victor Hugo 27/10/2014
 */
@Controller("AvaliacaoOnlineMatriculaControle")
@Scope("viewScope")
@Lazy
public class AvaliacaoOnlineMatriculaControle extends AvaliacaoOnlineAlunoSuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs;
	private CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO;
	private Integer quantidadeRespondida;
	private boolean apresentarAvalicaoOnlineRea = false;
	private boolean apresentarOpcaoFecharGatilho = false;
	private ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO;
	private Date dataLimiteRealizacaoAvaliacaoOnline;

	@PostConstruct
	public void init() {
		try {
			Boolean isAvaliacaoOnlineRea = (Boolean) context().getExternalContext().getSessionMap()
					.get("avaliacaoOnlineRea");
			if (Uteis.isAtributoPreenchido(isAvaliacaoOnlineRea) && isAvaliacaoOnlineRea) {
				carregarDadosAvaliacaoOnlineMatriculaDoConteudoRea();
			} else {
				consultarCalendarioAtividadesMatriculaPeriodoTurmaDisciplina();
			}
		} catch (Exception e) {
			if (e.getMessage() == null && e instanceof ConsistirException) {
				ConsistirException consistirException = (ConsistirException) e;
				setListaMensagemErro(consistirException.getListaMensagemErro());
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
	}

	private void carregarDadosAvaliacaoOnlineMatriculaDoConteudoRea() throws Exception {
		try {
			setApresentarAvalicaoOnlineRea(true);
			setAvaliacaoOnlineMatriculaVO((AvaliacaoOnlineMatriculaVO) context().getExternalContext().getSessionMap().get("avaliacaoOnlineMatriculaVO"));
			setConteudoUnidadePaginaRecursoEducacionalVO((ConteudoUnidadePaginaRecursoEducacionalVO) context().getExternalContext().getSessionMap().get("conteudoUnidadePaginaRecursoEducacionalVO"));
			setApresentarOpcaoFecharGatilho((boolean) context().getExternalContext().getSessionMap().get("apresentarOpcaoFecharGatilho"));
//			setCalendarioAtividadeMatriculaVO(getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade().consultarPorCodigoMatriculaPeriodoTurmaDisciplinaETipoCalendarioAtividadeUltimo(getAvaliacaoOnlineMatriculaVO().getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), TipoCalendarioAtividadeMatriculaEnum.PERIODO_REALIZACAO_AVALIACAO_ONLINE_REA, TipoOrigemEnum.REA, getConteudoUnidadePaginaRecursoEducacionalVO().getCodigo().toString(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));	
		    if(!Uteis.isAtributoPreenchido(getAvaliacaoOnlineMatriculaVO())){
				getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().realizarGeracaoAvaliacaoOnlineRea(getAvaliacaoOnlineMatriculaVO(), getCalendarioAtividadeMatriculaVO(), getUsuarioLogado());
				setTotalQuestoes(getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().size());
			}else{
				setAvaliacaoOnlineMatriculaVO(getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().consultarPorChavePrimaria(getAvaliacaoOnlineMatriculaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				if (!getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum().equals(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO)) {
					setAvaliacaoOnlineMatriculaVO(getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().realizarVisualizacaoGabarito(getAvaliacaoOnlineMatriculaVO(), getUsuarioLogado()));
					setApresentarResultadoAvaliacaoOnline(true);
				}
				setTotalQuestoes(getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineMatriculaQuestaoVOs().size());
				getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade()
						.realizarCalculoQuantidadePerguntasRespondidas(getAvaliacaoOnlineMatriculaVO());
			}
		} catch (Exception e) {
			throw e;
		} finally {
			context().getExternalContext().getSessionMap().remove("avaliacaoOnlineRea");
			context().getExternalContext().getSessionMap().remove("avaliacaoOnlineMatriculaVO");
			context().getExternalContext().getSessionMap().remove("apresentarOpcaoFecharGatilho");
			context().getExternalContext().getSessionMap().remove("conteudoUnidadePaginaRecursoEducacionalVO");
		}
	}

	public String inicializarAvaliacaoOnlineMatricula() {
		return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineMatriculaCons.xhtml");
	}

	public String consultarCalendarioAtividadesMatriculaPeriodoTurmaDisciplina() {
//		if(getVisaoAlunoControle() != null) {
//		try {
//			setCalendarioAtividadeMatriculaVOs(getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade()
//					.consultarCalendarioAtividadeMatriculaVisaoAluno(
//							getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(),
//							getUsuarioLogado()));
//			
//			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
//			
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//		}
//		return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineMatriculaCons.xhtml");
//		}else {
//			return "";
//		}
		return "";
	}

	public void iniciarInstrucoesAvaliacaoOnline() {
		try {
			setListaMensagemErro(new ArrayList<String>(0));
			setCalendarioAtividadeMatriculaVO(new CalendarioAtividadeMatriculaVO());
			setAvaliacaoOnlineMatriculaVO(new AvaliacaoOnlineMatriculaVO());
			CalendarioAtividadeMatriculaVO calendarioMatricula = (CalendarioAtividadeMatriculaVO) context()
					.getExternalContext().getRequestMap().get("avaliacaoOnlineItens");
//			calendarioMatricula = getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade()
//					.consultarPorChavePrimaria(calendarioMatricula.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS,
//							getUsuarioLogado());
			setCalendarioAtividadeMatriculaVO(calendarioMatricula);
			setMensagem("");
			setMensagemID("");
			setMensagemDetalhada("");
			setIconeMensagem("");
			setSucesso(false);
			getListaMensagemErro().clear();
			getAvaliacaoOnlineMatriculaVO().setConfiguracaoEADVO(
					getFacadeFactory().getConfiguracaoEADFacade().consultarConfiguracaoEADPorTurma(
							getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			context().getExternalContext().getRequestMap().remove("avaliacaoOnlineItens");
		}
	}

	public String iniciarAvaliacaoOnline() {
		try {
			setMostrarGabarito(false);
			setApresentarResultadoAvaliacaoOnline(false);
			getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().realizarGeracaoAvaliacaoOnline(
					getAvaliacaoOnlineMatriculaVO(), getCalendarioAtividadeMatriculaVO(),
					getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getMatricula(),
					getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(),
					getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getTurma().getCodigo(),
					getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(),
					getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getAno(),
					getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getSemestre(),
					getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().getAvaliacaoOnlineTemaAssuntoVOs(),
					getUsuarioLogado(), getUsuarioLogado().getPermiteSimularNavegacaoAluno(), false);
			setTotalQuestoes(
					getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().getAvaliacaoOnlineQuestaoVOs().size());
			if (!getUsuarioLogado().getPermiteSimularNavegacaoAluno()) {
//				setCalendarioAtividadeMatriculaVOs(getFacadeFactory().getCalendarioAtividadeMatriculaInterfaceFacade()
//						.consultarCalendarioAtividadeMatriculaVisaoAluno(
//								getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(),
//								getUsuarioLogado()));
			}
			inicializarMensagemVazia();
			return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineMatriculaForm.xhtml");
		} catch (Exception e) {
			init();
			if (e.getMessage() == null && e instanceof ConsistirException) {
				ConsistirException consistirException = (ConsistirException) e;
				setListaMensagemErro(consistirException.getListaMensagemErro());
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineMatriculaCons.xhtml");
		}
	}

	public void realizarVerificacaoQuestaoUnicaEscolha() {
		try {
			AvaliacaoOnlineMatriculaRespostaQuestaoVO orq = (AvaliacaoOnlineMatriculaRespostaQuestaoVO) context()
					.getExternalContext().getRequestMap().get("opcaoRespostaQuestaoItens");
			if (orq.getMarcada()) {
				orq.setMarcada(false);
				getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade()
						.realizarCalculoQuantidadePerguntasRespondidas(getAvaliacaoOnlineMatriculaVO());
			} else {
				orq.setMarcada(true);
				getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().realizarVerificacaoQuestaoUnicaEscolha(
						getAvaliacaoOnlineMatriculaVO(), orq, getUsuarioLogado());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void realizarCorrecaoExercicio() {
		realizarFinalizacaoAvaliacaoOnline(true);
	}

	public void realizarFinalizacaoAvaliacaoOnline(boolean validarPerguntasRespondidas) {
		try {
//			executarValidacaoSimulacaoVisaoAluno();

			getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().executarCorrecaoAvaliacaoOnline(
					getAvaliacaoOnlineMatriculaVO(), getCalendarioAtividadeMatriculaVO(), validarPerguntasRespondidas,
					getUsuarioLogado(), getUsuarioLogado().getPermiteSimularNavegacaoAluno());
			setMensagem("");
			setMensagemID("");
			setMensagemDetalhada("");
			setIconeMensagem("");
			setSucesso(false);
			getListaMensagemErro().clear();
			setApresentarResultadoAvaliacaoOnline(true);
			setDataLimiteRealizacaoAvaliacaoOnline(getCalendarioAtividadeMatriculaVO().getDataFim());
			consultarCalendarioAtividadesMatriculaPeriodoTurmaDisciplina();
			if (getUsuarioLogado().getIsApresentarVisaoAluno()) {
				getVisaoAlunoControle().consultarAlertaAvaliacaoOnlineDisponivel();
			}
			if (getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO()
					.getApresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao()) {
//				if (UteisData.validarDataInicialMaiorFinalComHora(new Date(),
//						getDataLimiteRealizacaoAvaliacaoOnline())) {
//					setMostrarGabarito(true);
//				} 
//				else {
//					setMostrarGabarito(false);
//				}
			} else {
				setMostrarGabarito(true);
			}
		} catch (ConsistirException e) {
			setApresentarResultadoAvaliacaoOnline(false);
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setApresentarResultadoAvaliacaoOnline(false);
			verificarPrimeiraQuestaoNaoRespondida(getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineMatriculaQuestaoVOs());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String continuarAvaliacaoOnline() {
		try {
			setListaMensagemErro(new ArrayList<String>(0));
			setCalendarioAtividadeMatriculaVO((CalendarioAtividadeMatriculaVO) context().getExternalContext()
					.getRequestMap().get("avaliacaoOnlineItens"));
			setAvaliacaoOnlineMatriculaVO(getCalendarioAtividadeMatriculaVO().getAvaliacaoOnlineMatriculaVO());
			setAvaliacaoOnlineMatriculaVO(getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade()
					.consultarPorChavePrimaria(getAvaliacaoOnlineMatriculaVO().getCodigo(),
							Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if (!getAvaliacaoOnlineMatriculaVO().getSituacaoAvaliacaoOnlineMatriculaEnum()
					.equals(SituacaoAvaliacaoOnlineMatriculaEnum.EM_REALIZACAO)) {
				setAvaliacaoOnlineMatriculaVO(getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade()
						.realizarVisualizacaoGabarito(getAvaliacaoOnlineMatriculaVO(), getUsuarioLogado()));
				setApresentarResultadoAvaliacaoOnline(true);
			}
			setTotalQuestoes(getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineMatriculaQuestaoVOs().size());
			getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade()
					.realizarCalculoQuantidadePerguntasRespondidas(getAvaliacaoOnlineMatriculaVO());
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			context().getExternalContext().getRequestMap().remove("avaliacaoOnlineItens");
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineMatriculaForm.xhtml");
	}

	public String visualizarGabarito() {
		try {
			setListaMensagemErro(new ArrayList<String>(0));
			setDataLimiteRealizacaoAvaliacaoOnline(null);
			CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO = (CalendarioAtividadeMatriculaVO) context().getExternalContext().getRequestMap().get("avaliacaoOnlineItens");
			setDataLimiteRealizacaoAvaliacaoOnline(calendarioAtividadeMatriculaVO.getDataFim());
			setAvaliacaoOnlineMatriculaVO(calendarioAtividadeMatriculaVO.getAvaliacaoOnlineMatriculaVO());
			setAvaliacaoOnlineMatriculaVO(getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().realizarVisualizacaoGabarito(getAvaliacaoOnlineMatriculaVO(), getUsuarioLogado()));
			setTotalQuestoes(getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineMatriculaQuestaoVOs().size());
			if (getAvaliacaoOnlineMatriculaVO().getAvaliacaoOnlineVO().getApresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao()) {
//				if (UteisData.validarDataInicialMaiorFinalComHora(new Date(), getDataLimiteRealizacaoAvaliacaoOnline())) {
//					setMostrarGabarito(true);
//				} else {
//					setMostrarGabarito(false);
//				}
			} else {
				setMostrarGabarito(true);
			}
			setApresentarResultadoAvaliacaoOnline(true);
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoOnlineMatriculaForm.xhtml");
	}

	public void finalizarAvaliacaoOnlineTempoEsgotado() {
		realizarFinalizacaoAvaliacaoOnline(false);
	}

	public String navegarParaAvaliacaoOnlineRea() {
		try {
			context().getExternalContext().getSessionMap().put("avaliacaoOnlineRea", true);
			context().getExternalContext().getSessionMap().put("avaliacaoOnlineMatriculaVO",
					getAvaliacaoOnlineMatriculaVO());
			context().getExternalContext().getSessionMap().put("conteudoUnidadePaginaRecursoEducacionalVO",
					getConteudoUnidadePaginaRecursoEducacionalVO());
			context().getExternalContext().getSessionMap().put("apresentarOpcaoFecharGatilho",
					isApresentarOpcaoFecharGatilho());
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("conteudoAlunoForm.xhtml");

	}

	public Integer getQuantidadeRespondida() {
		if (quantidadeRespondida == null) {
			quantidadeRespondida = 0;
		}
		return quantidadeRespondida;
	}

	public void setQuantidadeRespondida(Integer quantidadeRespondida) {
		this.quantidadeRespondida = quantidadeRespondida;
	}

	public List<CalendarioAtividadeMatriculaVO> getCalendarioAtividadeMatriculaVOs() {
		if (calendarioAtividadeMatriculaVOs == null) {
			calendarioAtividadeMatriculaVOs = new ArrayList<CalendarioAtividadeMatriculaVO>();
		}
		return calendarioAtividadeMatriculaVOs;
	}

	public void setCalendarioAtividadeMatriculaVOs(
			List<CalendarioAtividadeMatriculaVO> calendarioAtividadeMatriculaVOs) {
		this.calendarioAtividadeMatriculaVOs = calendarioAtividadeMatriculaVOs;
	}

	public CalendarioAtividadeMatriculaVO getCalendarioAtividadeMatriculaVO() {
		if (calendarioAtividadeMatriculaVO == null) {
			calendarioAtividadeMatriculaVO = new CalendarioAtividadeMatriculaVO();
		}
		return calendarioAtividadeMatriculaVO;
	}

	public void setCalendarioAtividadeMatriculaVO(CalendarioAtividadeMatriculaVO calendarioAtividadeMatriculaVO) {
		this.calendarioAtividadeMatriculaVO = calendarioAtividadeMatriculaVO;
	}

	public boolean isApresentarAvalicaoOnlineRea() {
		return apresentarAvalicaoOnlineRea;
	}

	public void setApresentarAvalicaoOnlineRea(boolean apresentarAvalicaoOnlineRea) {
		this.apresentarAvalicaoOnlineRea = apresentarAvalicaoOnlineRea;
	}

	public ConteudoUnidadePaginaRecursoEducacionalVO getConteudoUnidadePaginaRecursoEducacionalVO() {
		if (conteudoUnidadePaginaRecursoEducacionalVO == null) {
			conteudoUnidadePaginaRecursoEducacionalVO = new ConteudoUnidadePaginaRecursoEducacionalVO();
		}
		return conteudoUnidadePaginaRecursoEducacionalVO;
	}

	public void setConteudoUnidadePaginaRecursoEducacionalVO(
			ConteudoUnidadePaginaRecursoEducacionalVO conteudoUnidadePaginaRecursoEducacionalVO) {
		this.conteudoUnidadePaginaRecursoEducacionalVO = conteudoUnidadePaginaRecursoEducacionalVO;
	}

	public boolean isApresentarOpcaoFecharGatilho() {
		return apresentarOpcaoFecharGatilho;
	}

	public void setApresentarOpcaoFecharGatilho(boolean apresentarOpcaoFecharGatilho) {
		this.apresentarOpcaoFecharGatilho = apresentarOpcaoFecharGatilho;
	}

	public Date getDataLimiteRealizacaoAvaliacaoOnline() {
		if (dataLimiteRealizacaoAvaliacaoOnline == null) {
			dataLimiteRealizacaoAvaliacaoOnline = new Date();
		}
		return dataLimiteRealizacaoAvaliacaoOnline;
	}

	public void setDataLimiteRealizacaoAvaliacaoOnline(Date dataLimiteRealizacaoAvaliacaoOnline) {
		this.dataLimiteRealizacaoAvaliacaoOnline = dataLimiteRealizacaoAvaliacaoOnline;
	}
	
	private void verificarPrimeiraQuestaoNaoRespondida(List <AvaliacaoOnlineMatriculaQuestaoVO> avaliacaoOnlineMatriculaQuestaoVO) {
		if (Uteis.isAtributoPreenchido(avaliacaoOnlineMatriculaQuestaoVO)) {			
		 	OptionalInt findFirst2 = IntStream.rangeClosed(0, avaliacaoOnlineMatriculaQuestaoVO.size() - 1).filter(i-> avaliacaoOnlineMatriculaQuestaoVO.get(i).getQuestaoVO().getQuestaoNaoRespondida())
			.findFirst();
			findFirst2.ifPresent(a-> setCodigoAvaliacaoNaoRespondido(a));
		}
	}

}
