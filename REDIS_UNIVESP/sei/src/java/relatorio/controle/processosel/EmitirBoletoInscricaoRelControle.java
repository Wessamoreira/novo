package relatorio.controle.processosel;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.processosel.InscricaoVO;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.interfaces.processosel.EmitirBoletoInscricaoRelInterfaceFacade;
import relatorio.negocio.jdbc.processosel.EmitirBoletoInscricaoRel;
import controle.processosel.InscricaoControle;

@Controller("EmitirBoletoInscricaoRelControle")
@Scope("request")
@Lazy
public class EmitirBoletoInscricaoRelControle extends SuperControleRelatorio {

	protected EmitirBoletoInscricaoRel emitirBoletoInscricaoRel;
	protected Integer codigo;
	protected InscricaoVO inscricao;
	protected MatriculaPeriodoVO matriculaDireta;

	public EmitirBoletoInscricaoRelControle() throws Exception {
		inicializarComDadosBoletoImpressao();
		setMensagemID("msg_entre_prmrelatorio");
	}

	public void inicializarComDadosBoletoImpressao() {
		setEmitirBoletoInscricaoRel(new EmitirBoletoInscricaoRel());
		InscricaoControle inscricaoControle = (InscricaoControle) context().getExternalContext().getSessionMap().get("InscricaoControle");
		inscricao = inscricaoControle.getInscricaoVO();
		setCodigo(inscricaoControle.getInscricaoVO().getContaReceber());
	}

	public EmitirBoletoInscricaoRelControle(Integer codigo) throws Exception {
		setEmitirBoletoInscricaoRel(new EmitirBoletoInscricaoRel());
		setCodigo(codigo);

		setMensagemID("msg_entre_prmrelatorio");
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	/**
	 * Método responsável por gerar o relatório de <code>ServidorRel</code> no formato HTML
	 */
	public void imprimirHTML() {
		try {
			String titulo = " Impressão do Boleto de Inscrição do Vestibular";
			String xml = emitirBoletoInscricaoRel.emitirRelatorio(codigo);
			String design = emitirBoletoInscricaoRel.getDesignIReportRelatorio();
			apresentarRelatorio(emitirBoletoInscricaoRel.getIdEntidade(), xml, titulo, "", "", "HTML", "/" + emitirBoletoInscricaoRel.getIdEntidade() + "/registros", design,
					getUsuarioLogado().getNome(), emitirBoletoInscricaoRel.getDescricaoFiltros());
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public EmitirBoletoInscricaoRelInterfaceFacade getEmitirBoletoInscricaoRel() {
		return emitirBoletoInscricaoRel;
	}

	public void setEmitirBoletoInscricaoRel(EmitirBoletoInscricaoRel emitirBoletoInscricaoRel) {
		this.emitirBoletoInscricaoRel = emitirBoletoInscricaoRel;
	}

	public MatriculaPeriodoVO getMatriculaDireta() {
		return matriculaDireta;
	}

	public void setMatriculaDireta(MatriculaPeriodoVO matriculaDireta) {
		this.matriculaDireta = matriculaDireta;
	}

}
