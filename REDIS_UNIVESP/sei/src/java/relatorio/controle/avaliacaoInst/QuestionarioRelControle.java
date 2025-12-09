package relatorio.controle.avaliacaoInst;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.avaliacaoInst.QuestionarioRelVO;
import relatorio.negocio.jdbc.avaliacaoInst.QuestionarioRel;

@Controller("QuestionarioRelControle")
@Scope("viewScope")
@Lazy
public class QuestionarioRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;

	private QuestionarioVO questionarioVO;
	private String campoConsultaQuestionario;
	private String valorConsultaQuestionario;
	private List<QuestionarioVO> listaConsultaQuestionario;

	public void imprimirPDF() {
		String titulo = "Questionário";
		String design = QuestionarioRel.getDesignIReportRelatorio();
		List<QuestionarioRelVO> listaRegistro = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "QuestionarioRelControle", "Inicializando Geração de Relatório Questionário", "Emitindo Relatório");
			listaRegistro = getFacadeFactory().getQuestionarioRelFacade().criarObjeto(getQuestionarioVO(), getUsuarioLogado());
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(QuestionarioRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio(titulo);
			getSuperParametroRelVO().setListaObjetos(listaRegistro);
			getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(QuestionarioRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setQuantidade(listaRegistro.size());
			realizarImpressaoRelatorio();
			removerObjetoMemoria(this);
			registrarAtividadeUsuario(getUsuarioLogado(), "QuestionarioRelControle", "Finalizando Geração de Relatório Questionário", "Emitindo Relatório");
			setMensagemID("msg_relatorio_ok");

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			titulo = null;
			design = null;
			Uteis.liberarListaMemoria(listaRegistro);
		}
	}

	public void imprimirPDF(QuestionarioVO obj) {
		String titulo = "Questionário";
		String design = QuestionarioRel.getDesignIReportRelatorio1();
		List<QuestionarioRelVO> listaRegistro = null;
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "QuestionarioRelControle", "Inicializando Geração de Relatório Questionário", "Emitindo Relatório");
			listaRegistro = getFacadeFactory().getQuestionarioRelFacade().criarObjeto(obj, getUsuarioLogado());
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir(QuestionarioRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setTituloRelatorio(titulo);
			getSuperParametroRelVO().setListaObjetos(listaRegistro);
			getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(QuestionarioRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			getSuperParametroRelVO().setQuantidade(listaRegistro.size());
			realizarImpressaoRelatorio();
			registrarAtividadeUsuario(getUsuarioLogado(), "QuestionarioRelControle", "Finalizando Geração de Relatório Questionário", "Emitindo Relatório");
			setMensagemID("msg_relatorio_ok");
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			titulo = null;
			design = null;
			Uteis.liberarListaMemoria(listaRegistro);
		}
	}
	
	public void consultarQuestionario() {
		try {
			setListaConsultaQuestionario(getFacadeFactory().getQuestionarioRelFacade().consultar(getCampoConsultaQuestionario(), getValorConsultaQuestionario(), getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarQuestionario() {
		QuestionarioVO obj = (QuestionarioVO) context().getExternalContext().getRequestMap().get("questionarioItens");
		setQuestionarioVO(obj);
	}

	public void limparQuestionario() {
		setQuestionarioVO(new QuestionarioVO());
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public QuestionarioVO getQuestionarioVO() {
		if (questionarioVO == null) {
			questionarioVO = new QuestionarioVO();
		}
		return questionarioVO;
	}

	public void setQuestionarioVO(QuestionarioVO questionarioVO) {
		this.questionarioVO = questionarioVO;
	}

	public String getCampoConsultaQuestionario() {
		if (campoConsultaQuestionario == null) {
			campoConsultaQuestionario = "";
		}
		return campoConsultaQuestionario;
	}

	public void setCampoConsultaQuestionario(String campoConsultaQuestionario) {
		this.campoConsultaQuestionario = campoConsultaQuestionario;
	}

	public String getValorConsultaQuestionario() {
		if (valorConsultaQuestionario == null) {
			valorConsultaQuestionario = "";
		}
		return valorConsultaQuestionario;
	}

	public void setValorConsultaQuestionario(String valorConsultaQuestionario) {
		this.valorConsultaQuestionario = valorConsultaQuestionario;
	}

	public List<QuestionarioVO> getListaConsultaQuestionario() {
		if (listaConsultaQuestionario == null) {
			listaConsultaQuestionario = new ArrayList<>(0);
		}
		return listaConsultaQuestionario;
	}

	public void setListaConsultaQuestionario(List<QuestionarioVO> listaConsultaQuestionario) {
		this.listaConsultaQuestionario = listaConsultaQuestionario;
	}

}
