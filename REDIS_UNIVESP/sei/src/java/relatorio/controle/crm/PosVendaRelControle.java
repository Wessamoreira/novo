package relatorio.controle.crm;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.basico.enumeradores.ModuloLayoutEtiquetaEnum;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.crm.PosVendaRelVO;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

/**
 * 
 * @author PedroOtimize
 *
 */
@Controller("PosVendaRelControle")
@Scope("viewScope")
@Lazy
public class PosVendaRelControle extends SuperControleRelatorio {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5500688572101273868L;
	private List<PosVendaRelVO> listaPosVendaRelVOs;
	private PosVendaRelVO posVendaRelVO;
	private FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;

	public PosVendaRelControle() {
		try {
			verificarLayoutPadrao();
			setMensagemID("msg_entre_prmrelatorio");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void imprimirRelatorioExcel() {
		try {
			getPosVendaRelVO().getListaDocumetacaoMatriculaVOs().clear();
			getPosVendaRelVO().getListaPosVendaPresencaVOs().clear();
			getListaPosVendaRelVOs().clear();
			getListaPosVendaRelVOs().addAll(getFacadeFactory().getPosVendaRelFacade().criarObjeto(getPosVendaRelVO(), getFiltroRelatorioAcademicoVO(), getUsuarioLogado()));
			if (!getListaPosVendaRelVOs().isEmpty()) {
				File arquivo = new File(UteisJSF.getCaminhoWebRelatorio() + File.separator + String.valueOf(new Date().getTime()) + ".xls");
				getFacadeFactory().getPosVendaRelFacade().montarExcel(getListaPosVendaRelVOs(), getPosVendaRelVO(), arquivo);
				HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
				request.getSession().setAttribute("nomeArquivo", arquivo.getName());
				request.getSession().setAttribute("pastaBaseArquivo", arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator)));
				request.getSession().setAttribute("deletarArquivo", false);
				context().getExternalContext().dispatch("/DownloadSV");
				FacesContext.getCurrentInstance().responseComplete();
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
			persistirLayoutPadrao();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			getPosVendaRelVO().setTurmaVO(obj);
			if (getPosVendaRelVO().getTurmaVO().getTurmaAgrupada()) {
				getPosVendaRelVO().setTurmaVO(new TurmaVO());
				throw new Exception("Não é possível emitir esse relatório para uma turma agrupada! Selecione uma turma específica.");
			}
			valorConsultaTurma = "";
			campoConsultaTurma = "";
			listaConsultaTurma.clear();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurma(), getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarTurmaRapida() {
		try {
			if (!getPosVendaRelVO().getTurmaVO().getIdentificadorTurma().equals("")) {
				getPosVendaRelVO().setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getPosVendaRelVO().getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				if (getPosVendaRelVO().getTurmaVO().getTurmaAgrupada()) {
					getPosVendaRelVO().setTurmaVO(new TurmaVO());
					throw new Exception("Não é possível emitir esse relatório para uma turma agrupada! Selecione uma turma específica.");
				}
			}
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void persistirLayoutPadrao() {
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(String.valueOf(getPosVendaRelVO().isTrazerAlunoTransferencia()), "posVendaRel", "trazerAlunoTransferencia", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getPosVendaRelVO().getQuantidadeDisciplinasConcluidas().toString(), "posVendaRel", "quantidadeDisciplinasConcluidas", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), "posVendaRel", getUsuarioLogado());
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void verificarLayoutPadrao() {
		try {
			Map<String, String> dadosPadroes = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[] { "quantidadeDisciplinasConcluidas", "trazerAlunoTransferencia" }, "posVendaRel");
			for (String key : dadosPadroes.keySet()) {
				if (key.equals("quantidadeDisciplinasConcluidas")) {
					getPosVendaRelVO().setQuantidadeDisciplinasConcluidas(Integer.parseInt(dadosPadroes.get(key)));
				} else if (key.equals("trazerAlunoTransferencia")) {
					getPosVendaRelVO().setTrazerAlunoTransferencia(Boolean.valueOf(dadosPadroes.get(key)));
				}
			}
			getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroSituacaoAcademica(getFiltroRelatorioAcademicoVO(), "posVendaRel", getUsuarioLogado());
			dadosPadroes = null;
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	public PosVendaRelVO getPosVendaRelVO() {
		posVendaRelVO = Optional.ofNullable(posVendaRelVO).orElse(new PosVendaRelVO());
		return posVendaRelVO;
	}

	public void setPosVendaRelVO(PosVendaRelVO posVendaRelVO) {
		this.posVendaRelVO = posVendaRelVO;
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
		listaConsultaTurma = Optional.ofNullable(listaConsultaTurma).orElse(new ArrayList<>());
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public List<PosVendaRelVO> getListaPosVendaRelVOs() {
		listaPosVendaRelVOs = Optional.ofNullable(listaPosVendaRelVOs).orElse(new ArrayList<>());
		return listaPosVendaRelVOs;
	}

	public void setListaPosVendaRelVOs(List<PosVendaRelVO> listaPosVendaRelVOs) {
		this.listaPosVendaRelVOs = listaPosVendaRelVOs;
	}

	public FiltroRelatorioAcademicoVO getFiltroRelatorioAcademicoVO() {
		filtroRelatorioAcademicoVO = Optional.ofNullable(filtroRelatorioAcademicoVO).orElse(new FiltroRelatorioAcademicoVO());
		return filtroRelatorioAcademicoVO;
	}

	public void setFiltroRelatorioAcademicoVO(FiltroRelatorioAcademicoVO filtroRelatorioAcademicoVO) {
		this.filtroRelatorioAcademicoVO = filtroRelatorioAcademicoVO;
	}

}
