package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.HistoricoAlunoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("DocumentoIntegralizacaoCurricularRelControle")
@Scope("request")
@Lazy
public class DocumentoIntegralizacaoCurricularRelControle extends SuperControleRelatorio {
	public void imprimirDocumentoIntegracaoCurricular(MatriculaVO matricula, GradeCurricularVO gradeCurricular) {
		HistoricoAlunoRelVO historicoTemp = null;
		getSuperParametroRelVO().setTituloRelatorio("DOCUMENTO DE INTEGRALIZAÇÃO CURRICULAR");
		getSuperParametroRelVO().setNomeEmpresa(super.getUnidadeEnsinoLogado().getNome());
		String design = "relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "DocumentoIntegralizacaoCurricularRel.jrxml";
		List<HistoricoAlunoRelVO> historicoAlunoRelVOs = new ArrayList<HistoricoAlunoRelVO>(0);
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "DocumentoIntegralizacaoCurricularRelControle", "Inicializando Geração Documento Integralização Curricular", "Emitindo Documento");
			HistoricoAlunoRelVO histAlunoRelVO = new HistoricoAlunoRelVO();
			getFacadeFactory().getMatriculaFacade().carregarDados(matricula, NivelMontarDados.TODOS, getUsuarioLogado());
			getFacadeFactory().getHistoricoAlunoRelFacade().setDescricaoFiltros("");
			getFiltroRelatorioAcademicoVO().realizarMarcarTodasSituacoesHistorico();
			historicoTemp = getFacadeFactory().getHistoricoAlunoRelFacade().criarObjeto(histAlunoRelVO, matricula, gradeCurricular, getFiltroRelatorioAcademicoVO(), 3, "", "", "aluno", "DocumentoIntegralizacaoCurricularRel", Boolean.TRUE, new Date(), Boolean.FALSE, Boolean.FALSE, true, getUsuarioLogado(), Boolean.TRUE, false, false, "", false, false, false, "PROFESSOR_TURMA_BASE", false, null);
			historicoTemp.setApresentarFrequencia(Boolean.FALSE);
			historicoTemp.setListaDocumentacaoPendente(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumetacaoMatriculaPorMatriculaAlunoEntregue(matricula.getMatricula(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, false, getUsuarioLogado()));
			int aux = historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs().size();
			historicoTemp.setListaDisciplinasACursar(historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs());
			getFacadeFactory().getHistoricoFacade().consultaRapidaPorMatriculaHistoricoDisciplinaACursar(matricula.getMatricula(), 3, historicoTemp.getListaDisciplinasACursar(), false, getUsuarioLogado());
			historicoTemp.setListaDisciplinasACursar(historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs().subList(aux, historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs().size()));
			historicoTemp.setHistoricoAlunoDisciplinaRelVOs(historicoTemp.getListaHistoricoAlunoDisciplinaRelVOs().subList(0, aux));
			historicoAlunoRelVOs.add(historicoTemp);
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setSubReport_Dir(getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setListaObjetos(historicoAlunoRelVOs);
			getSuperParametroRelVO().setQuantidade(historicoAlunoRelVOs.size());
			getSuperParametroRelVO().setCaminhoBaseRelatorio(getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
			UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (obj.getNomeExpedicaoDiploma().trim().isEmpty()) {
				getSuperParametroRelVO().setUnidadeEnsino(obj.getNome());
			} else {
				getSuperParametroRelVO().setUnidadeEnsino(obj.getNomeExpedicaoDiploma());
			}
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			realizarImpressaoRelatorio();
			registrarAtividadeUsuario(getUsuarioLogado(), "DocumentoIntegralizacaoCurricularRelControle", "Finalizando Geração Documento", "Emitindo Documento");
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			historicoTemp = null;
			design = null;
			Uteis.liberarListaMemoria(historicoAlunoRelVOs);
		}
	}

	public String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

}
