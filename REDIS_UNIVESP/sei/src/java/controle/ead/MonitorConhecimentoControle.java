package controle.ead;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ConteudoVO;
import negocio.comuns.academico.UnidadeConteudoVO;
import negocio.comuns.ead.AnotacaoDisciplinaVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaQuestaoVO;
import negocio.comuns.ead.AvaliacaoOnlineMatriculaVO;
import negocio.comuns.ead.GraficoAproveitamentoAvaliacaoVO;
import negocio.comuns.ead.ListaExercicioVO;
import negocio.comuns.ead.MonitorConhecimentoVO;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoAtividadeRespostaEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;

/**
 * 
 * @author Victor Hugo de Paula Costa 01/04/2015
 *
 */
@Controller("MonitorConhecimentoControle")
@Scope("viewScope")
@Lazy
public class MonitorConhecimentoControle extends SuperControle {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MonitorConhecimentoVO monitorConhecimentoVO;
	private List<AnotacaoDisciplinaVO> listaAnotacaoDisciplinaVOs;
	private List<AnotacaoDisciplinaVO> listaAnotacaoDisciplinaVOColegas;
	private Boolean mostrarGabarito;
	private List<AvaliacaoOnlineMatriculaQuestaoVO> avaliacaoOnlineMatriculaQuestaoVOs;
	private List<AvaliacaoOnlineMatriculaVO> avaliacaoOnlineMatriculaVOs;
	private String acerteiErrei;	
	private ConteudoVO conteudoVO;
	private Integer qtdeQuestoesFaceis;
	private Integer qtdeQuestoesMedias;
	private Integer qtdeQuestoesDificeis;
	private ListaExercicioVO listaExercicioVO;

	public void realizarGeracaoMonitoramentoDeConhecimentoAssuntos() {
		try {
			GraficoAproveitamentoAvaliacaoVO graficoAproveitamentoAvaliacaoVO = (GraficoAproveitamentoAvaliacaoVO) context().getExternalContext().getRequestMap().get("grafico");
			setMonitorConhecimentoVO(getFacadeFactory().getMonitorConhecimentoFacade().realizarGeracaoGraficosMonitorConhecimento(graficoAproveitamentoAvaliacaoVO, getMonitorConhecimentoVO(), 250, 350, 350, getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO(), getVisaoAlunoControle().getCursoVO().getCodigo(), getUsuarioLogado()));
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void realizarRevisaoConteudo() {
		try {
			setConteudoVO(getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getConteudo());
			getConteudoVO().setUnidadeConteudoVOs(new ArrayList<UnidadeConteudoVO>());			
			getFacadeFactory().getConteudoFacade().realizarGeracaoIndiceConteudo(getConteudoVO(), getMonitorConhecimentoVO().getGraficoAproveitamentoAvaliacaoVO().getCodigoTemaAssunto(), getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getMatricula(), getUsuarioLogado());
			context().getExternalContext().getSessionMap().put("booleanoReverConteudo", true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void realizarConsultaAnotacoes() {
		try {
			getListaAnotacaoDisciplinaVOs().clear();
			getListaAnotacaoDisciplinaVOColegas().clear();
			setListaAnotacaoDisciplinaVOs(getFacadeFactory().getAnotacaoDisciplinaInterfaceFacade().consultaAnotacoesPorTemaAssuntoEConteudo(getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getMatricula(), getMonitorConhecimentoVO().getGraficoAproveitamentoAvaliacaoVO().getCodigoTemaAssunto(), getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().getCodigo(), getUsuarioLogado()));
			setListaAnotacaoDisciplinaVOColegas(getFacadeFactory().getAnotacaoDisciplinaInterfaceFacade().consultaAnotacoesPorTemaAssuntoEConteudo(null, getMonitorConhecimentoVO().getGraficoAproveitamentoAvaliacaoVO().getCodigoTemaAssunto(), getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getConteudo().getCodigo(), getUsuarioLogado()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void realizarConsultaAvaliacoesOnlinesComQuestoesQueAcertou() {
		try {
			setAvaliacaoOnlineMatriculaVOs(getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().consultarAvaliacoesOnlineMatriculaRealizadas(getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getUsuarioLogado()));
			for (AvaliacaoOnlineMatriculaVO obj : getAvaliacaoOnlineMatriculaVOs()) {
				
				if (apresentarGabarito(obj)) {
					obj.setAvaliacaoOnlineMatriculaQuestaoVOs(getFacadeFactory().getAvaliacaoOnlineMatriculaQuestaoInterfaceFacade().consultarQuestoesQueAcertouOuErrou(obj.getCodigo(), getMonitorConhecimentoVO().getGraficoAproveitamentoAvaliacaoVO().getCodigoTemaAssunto(), SituacaoAtividadeRespostaEnum.ACERTOU, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					if (obj.getAvaliacaoOnlineMatriculaQuestaoVOs().isEmpty()) {
						obj.setNaoAcertouOuErrouNenhumaQuestao("Você não acertou nenhuma questão");
					}
					Ordenacao.ordenarLista(obj.getAvaliacaoOnlineMatriculaQuestaoVOs(), "ordemApresentacao");
				}
			}
			setAcerteiErrei("O Que Acertei");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void realizarConsultaAvaliacoesOnlinesComQuestoesQueErrou() {
		try {
			setAvaliacaoOnlineMatriculaVOs(getFacadeFactory().getAvaliacaoOnlineMatriculaInterfaceFacade().consultarAvaliacoesOnlineMatriculaRealizadas(getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getCodigo(), getUsuarioLogado()));
			for (AvaliacaoOnlineMatriculaVO obj : getAvaliacaoOnlineMatriculaVOs()) {
				if (apresentarGabarito(obj)) {
					obj.setAvaliacaoOnlineMatriculaQuestaoVOs(getFacadeFactory().getAvaliacaoOnlineMatriculaQuestaoInterfaceFacade().consultarQuestoesQueAcertouOuErrou(obj.getCodigo(), getMonitorConhecimentoVO().getGraficoAproveitamentoAvaliacaoVO().getCodigoTemaAssunto(), SituacaoAtividadeRespostaEnum.ERROU, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
					if (obj.getAvaliacaoOnlineMatriculaQuestaoVOs().isEmpty()) {
						obj.setNaoAcertouOuErrouNenhumaQuestao("Você não errou nenhuma questão");
					}
					Ordenacao.ordenarLista(obj.getAvaliacaoOnlineMatriculaQuestaoVOs(), "ordemApresentacao");
				}
			}
			setAcerteiErrei("O Que Errei");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void realizarConsultaQuantidadeQuestoesExistentesTemaAssunto() {
		try {
			setQtdeQuestoesFaceis(getFacadeFactory().getQuestaoFacade().consultarQuantidadeQuestoesPorDisciplinaNivelComplexidadeETemaAssunto(getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMonitorConhecimentoVO().getGraficoAproveitamentoAvaliacaoVO().getCodigoTemaAssunto(), NivelComplexidadeQuestaoEnum.FACIL, SituacaoQuestaoEnum.ATIVA, false, false, true, false));
			setQtdeQuestoesMedias(getFacadeFactory().getQuestaoFacade().consultarQuantidadeQuestoesPorDisciplinaNivelComplexidadeETemaAssunto(getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMonitorConhecimentoVO().getGraficoAproveitamentoAvaliacaoVO().getCodigoTemaAssunto(), NivelComplexidadeQuestaoEnum.MEDIO, SituacaoQuestaoEnum.ATIVA,false, false, true, false));
			setQtdeQuestoesDificeis(getFacadeFactory().getQuestaoFacade().consultarQuantidadeQuestoesPorDisciplinaNivelComplexidadeETemaAssunto(getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO().getDisciplina().getCodigo(), getMonitorConhecimentoVO().getGraficoAproveitamentoAvaliacaoVO().getCodigoTemaAssunto(), NivelComplexidadeQuestaoEnum.DIFICIL, SituacaoQuestaoEnum.ATIVA, false, false, true, false));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String realizarGeracaoListaExercicio() {
		try {
			setListaExercicioVO(getFacadeFactory().getMonitorConhecimentoFacade().realizarGeracaoListaExerciciMonitorConhecimento(getListaExercicioVO(), getMonitorConhecimentoVO().getGraficoAproveitamentoAvaliacaoVO().getAssunto(), getMonitorConhecimentoVO().getGraficoAproveitamentoAvaliacaoVO().getCodigoTemaAssunto(), getVisaoAlunoControle().getMatriculaPeriodoTurmaDisciplinaVO(), getUsuarioLogado()));
			context().getExternalContext().getSessionMap().put("listaExercicioMonitorConhecimento", getListaExercicioVO());
			context().getExternalContext().getSessionMap().put("booleanoListaExercicio", true);
			return Uteis.getCaminhoRedirecionamentoNavegacao("listaExercicioAlunoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return "";
		}
	}
	

	// Getters and Setters
	public MonitorConhecimentoVO getMonitorConhecimentoVO() {
		if (monitorConhecimentoVO == null) {
			monitorConhecimentoVO = new MonitorConhecimentoVO();
		}
		return monitorConhecimentoVO;
	}

	public void setMonitorConhecimentoVO(MonitorConhecimentoVO monitorConhecimentoVO) {
		this.monitorConhecimentoVO = monitorConhecimentoVO;
	}

	public List<AnotacaoDisciplinaVO> getListaAnotacaoDisciplinaVOs() {
		if (listaAnotacaoDisciplinaVOs == null) {
			listaAnotacaoDisciplinaVOs = new ArrayList<AnotacaoDisciplinaVO>();
		}
		return listaAnotacaoDisciplinaVOs;
	}

	public void setListaAnotacaoDisciplinaVOs(List<AnotacaoDisciplinaVO> listaAnotacaoDisciplinaVOs) {
		this.listaAnotacaoDisciplinaVOs = listaAnotacaoDisciplinaVOs;
	}

	public List<AnotacaoDisciplinaVO> getListaAnotacaoDisciplinaVOColegas() {
		if (listaAnotacaoDisciplinaVOColegas == null) {
			listaAnotacaoDisciplinaVOColegas = new ArrayList<AnotacaoDisciplinaVO>();
		}
		return listaAnotacaoDisciplinaVOColegas;
	}

	public void setListaAnotacaoDisciplinaVOColegas(List<AnotacaoDisciplinaVO> listaAnotacaoDisciplinaVOColegas) {
		this.listaAnotacaoDisciplinaVOColegas = listaAnotacaoDisciplinaVOColegas;
	}

	public Boolean getMostrarGabarito() {
		if (mostrarGabarito == null) {
			mostrarGabarito = true;
		}
		return mostrarGabarito;
	}

	public void setMostrarGabarito(Boolean mostrarGabarito) {
		this.mostrarGabarito = mostrarGabarito;
	}

	public List<AvaliacaoOnlineMatriculaQuestaoVO> getAvaliacaoOnlineMatriculaQuestaoVOs() {
		if (avaliacaoOnlineMatriculaQuestaoVOs == null) {
			avaliacaoOnlineMatriculaQuestaoVOs = new ArrayList<AvaliacaoOnlineMatriculaQuestaoVO>();
		}
		return avaliacaoOnlineMatriculaQuestaoVOs;
	}

	public void setAvaliacaoOnlineMatriculaQuestaoVOs(List<AvaliacaoOnlineMatriculaQuestaoVO> avaliacaoOnlineMatriculaQuestaoVOs) {
		this.avaliacaoOnlineMatriculaQuestaoVOs = avaliacaoOnlineMatriculaQuestaoVOs;
	}

	public List<AvaliacaoOnlineMatriculaVO> getAvaliacaoOnlineMatriculaVOs() {
		if (avaliacaoOnlineMatriculaVOs == null) {
			avaliacaoOnlineMatriculaVOs = new ArrayList<AvaliacaoOnlineMatriculaVO>();
		}
		return avaliacaoOnlineMatriculaVOs;
	}

	public void setAvaliacaoOnlineMatriculaVOs(List<AvaliacaoOnlineMatriculaVO> avaliacaoOnlineMatriculaVOs) {
		this.avaliacaoOnlineMatriculaVOs = avaliacaoOnlineMatriculaVOs;
	}

	public String getAcerteiErrei() {
		if (acerteiErrei == null) {
			acerteiErrei = "";
		}
		return acerteiErrei;
	}

	public void setAcerteiErrei(String acerteiErrei) {
		this.acerteiErrei = acerteiErrei;
	}

	
	public ConteudoVO getConteudoVO() {
		if (conteudoVO == null) {
			conteudoVO = new ConteudoVO();
		}
		return conteudoVO;
	}

	public void setConteudoVO(ConteudoVO conteudoVO) {
		this.conteudoVO = conteudoVO;
	}

	public Integer getQtdeQuestoesFaceis() {
		if (qtdeQuestoesFaceis == null) {
			qtdeQuestoesFaceis = 0;
		}
		return qtdeQuestoesFaceis;
	}

	public void setQtdeQuestoesFaceis(Integer qtdeQuestoesFaceis) {
		this.qtdeQuestoesFaceis = qtdeQuestoesFaceis;
	}

	public Integer getQtdeQuestoesMedias() {
		if (qtdeQuestoesMedias == null) {
			qtdeQuestoesMedias = 0;
		}
		return qtdeQuestoesMedias;
	}

	public void setQtdeQuestoesMedias(Integer qtdeQuestoesMedias) {
		this.qtdeQuestoesMedias = qtdeQuestoesMedias;
	}

	public Integer getQtdeQuestoesDificeis() {
		if (qtdeQuestoesDificeis == null) {
			qtdeQuestoesDificeis = 0;
		}
		return qtdeQuestoesDificeis;
	}

	public void setQtdeQuestoesDificeis(Integer qtdeQuestoesDificeis) {
		this.qtdeQuestoesDificeis = qtdeQuestoesDificeis;
	}

	public ListaExercicioVO getListaExercicioVO() {
		if(listaExercicioVO == null) {
			listaExercicioVO = new ListaExercicioVO();
		}
		return listaExercicioVO;
	}

	public void setListaExercicioVO(ListaExercicioVO listaExercicioVO) {
		this.listaExercicioVO = listaExercicioVO;
	}
	
	public String navegarParaEstudoOnline(Integer codigoPagina) {
		
		return "conteudoAlunoForm.xhtml?faces-redirect=true&amp;pagina=" + codigoPagina;
	}
	
	public Boolean apresentarGabarito(AvaliacaoOnlineMatriculaVO obj) {
		
		try {
			if (!obj.getAvaliacaoOnlineVO().getApresentarGabaritoProvaAlunoAposDataTerminoPeriodoRealizacao()) {
				return true;
			} else if (UteisData.validarDataInicialMaiorFinalComHora(new Date(), obj.getDataFimLiberacao())) {
			    return true;
			}else {
				return false;
			}
		} catch (ParseException e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			return false;
		}
		
	}
}
