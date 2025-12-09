package relatorio.controle.financeiro;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.processosel.InscricaoVO;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.interfaces.financeiro.BoletoBancarioRelInterfaceFacade;
import relatorio.negocio.jdbc.financeiro.BoletoBancarioRel;
import controle.processosel.InscricaoControle;

@Controller("BoletoBancarioRelControle")
@Scope("request")
@Lazy
public class BoletoBancarioRelControle extends SuperControleRelatorio {

    protected BoletoBancarioRel emitirBoletoInscricaoRel;
    protected Integer codigo;
    protected InscricaoVO inscricao;
    protected MatriculaPeriodoVO matriculaDireta;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public BoletoBancarioRelControle() throws Exception {
        inicializarComDadosBoletoImpressao();
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void inicializarComDadosBoletoImpressao() {
        setEmitirBoletoInscricaoRel(new BoletoBancarioRel());
        InscricaoControle inscricaoControle = (InscricaoControle) context().getExternalContext().getSessionMap().get("InscricaoControle");
        inscricao = inscricaoControle.getInscricaoVO();
        setCodigo(inscricaoControle.getInscricaoVO().getContaReceber());
    }

    @SuppressWarnings("OverridableMethodCallInConstructor")
    public BoletoBancarioRelControle(Integer codigo) throws Exception {
        setEmitirBoletoInscricaoRel(new BoletoBancarioRel());
        setCodigo(codigo);

        setMensagemID("msg_entre_prmrelatorio");
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

//	/**
//	 * Método responsável por gerar o relatório de <code>ServidorRel</code> no formato HTML
//	 */
//	public void imprimirHTML() {
//		try {
//			String titulo = " Impressão do Boleto de Inscrição do Vestibular";
////			String xml = emitirBoletoInscricaoRel.emitirRelatorio(codigo, getUsuarioLogado());
//                        List<BoletoBancarioRelVO> listaObjetos = emitirBoletoInscricaoRel.executarConsultaParametrizada(codigo, null, null, null, null, null, null, null, null, null, getUsuarioLogado());
//			String design = emitirBoletoInscricaoRel.getDesignIReportRelatorio();
//			apresentarRelatorioObjetos(emitirBoletoInscricaoRel.getIdEntidade(), titulo, "", "", "PDF", "/" + emitirBoletoInscricaoRel.getIdEntidade() + "/registros", design,
//					(getUsuarioLogado() == null) ? "": getUsuarioLogado().getNome(), emitirBoletoInscricaoRel.getDescricaoFiltros(), listaObjetos, "relatorio" + File.separator + "designRelatorio" + File.separator + "financeiro" + File.separator);
//			setMensagemID("msg_relatorio_ok");
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
//	}
    public BoletoBancarioRelInterfaceFacade getEmitirBoletoInscricaoRel() {
        return emitirBoletoInscricaoRel;
    }

    public void setEmitirBoletoInscricaoRel(BoletoBancarioRel emitirBoletoInscricaoRel) {
        this.emitirBoletoInscricaoRel = emitirBoletoInscricaoRel;
    }

    public MatriculaPeriodoVO getMatriculaDireta() {
        return matriculaDireta;
    }

    public void setMatriculaDireta(MatriculaPeriodoVO matriculaDireta) {
        this.matriculaDireta = matriculaDireta;
    }
}
