package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.academico.CancelamentoVO;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.jdbc.academico.DeclaracaoCancelamentoRel;

@SuppressWarnings("unchecked")
@Controller("DeclaracaoCancelamentoRelControle")
@Scope("request")
@Lazy
public class DeclaracaoCancelamentoRelControle extends SuperControleRelatorio {

	private DeclaracaoCancelamentoRel declaracaoCancelamentoRel;
	private List listaObjetos;

	public DeclaracaoCancelamentoRelControle() throws Exception {
		//obterUsuarioLogado();
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void imprimirPDF(CancelamentoVO cancelamentoVO) {
		try {
			getDeclaracaoCancelamentoRel().setDescricaoFiltros("");
			String titulo = "DECLARAÇÃO DE CANCELAMENTO";
			getListaObjetos().clear();
			if (cancelamentoVO.getMatricula().getCurso().getNivelEducacional().equals("BA")
					|| cancelamentoVO.getMatricula().getCurso().getNivelEducacional().equals("ME")
					|| cancelamentoVO.getMatricula().getCurso().getNivelEducacional().equals("EX")
					|| cancelamentoVO.getMatricula().getCurso().getNivelEducacional().equals("PR")) {
				cancelamentoVO.setTitulacaoInstituicao("Instituto de Ensino");
			} else {
				cancelamentoVO.setTitulacaoInstituicao("Instituto de Ensino Superior");
			}
			getListaObjetos().add(cancelamentoVO);
			String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
			String design = DeclaracaoCancelamentoRel.getDesignIReportRelatorio();
			apresentarRelatorioObjetos(DeclaracaoCancelamentoRel.getIdEntidade(), titulo, nomeEntidade, "", "PDF", "", design, getUsuarioLogado().getNome(),
					getDeclaracaoCancelamentoRel().getDescricaoFiltros(), getListaObjetos(), DeclaracaoCancelamentoRel.getCaminhoBaseRelatorio());
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void setDeclaracaoCancelamentoRel(DeclaracaoCancelamentoRel declaracaoCancelamentoRel) {
		this.declaracaoCancelamentoRel = declaracaoCancelamentoRel;
	}

	public DeclaracaoCancelamentoRel getDeclaracaoCancelamentoRel() throws Exception {
		if (declaracaoCancelamentoRel == null) {
			declaracaoCancelamentoRel = new DeclaracaoCancelamentoRel();
		}
		return declaracaoCancelamentoRel;
	}

	public List getListaObjetos() {
		if (listaObjetos == null) {
			listaObjetos = new ArrayList(0);
		}
		return listaObjetos;
	}

	public void setListaObjetos(List listaObjetos) {
		this.listaObjetos = listaObjetos;
	}

}
