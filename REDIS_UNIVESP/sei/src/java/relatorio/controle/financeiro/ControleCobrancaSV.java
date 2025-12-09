package relatorio.controle.financeiro;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import negocio.comuns.utilitarias.Uteis;

import relatorio.arquitetura.SuperRelatorioSV;
import relatorio.negocio.jdbc.financeiro.ControleCobrancaRel;

/**
 *
 * @author Diego
 */
public class ControleCobrancaSV extends SuperRelatorioSV {

    public static final long serialVersionUID = 1L;
    private ControleCobrancaRel controleCobrancaRel;

    public void imprimirPDF(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String titulo = null;
        String xml = null;
        String design = null;
        try {
        	Integer registroArquivo = Integer.parseInt(request.getParameter("registroArquivo"));
            controleCobrancaRel.setDescricaoFiltros("");
            controleCobrancaRel.setCodigoRegistroArquivo(registroArquivo);
            titulo = " Controle de Cobrança ";
            xml = controleCobrancaRel.emitirRelatorio();
            design = ControleCobrancaRel.getDesignIReportRelatorio();
            apresentarRelatorio(request, response, ControleCobrancaRel.getIdEntidade(), xml,
                    titulo, getUnidadeEnsinoLogado().getNome(), "", "PDF",
                    "/" + ControleCobrancaRel.getIdEntidade() + "/registros",
                    design, "", "true", "ControleCobrancaSV","");
        } catch (Exception e) {
            throw e;
        } finally {
            titulo = null;
            xml = null;
            design = null;
        }

    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {        	
            controleCobrancaRel = new ControleCobrancaRel();
            imprimirPDF(request, response);
        } catch (Exception e) {

        }finally{
            Uteis.removerObjetoMemoria(this);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    public String getServletInfo() {
        return "Short description";
    }
    
}
