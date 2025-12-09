/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.controle.processosel;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import relatorio.arquitetura.SuperRelatorioSV;
import relatorio.negocio.interfaces.processosel.EmitirBoletoInscricaoRelInterfaceFacade;
import relatorio.negocio.jdbc.processosel.EmitirBoletoInscricaoRel;
import controle.processosel.InscricaoControle;

/**
 *
 * @author Edgar
 */
public class EmitirBoletoInscricaoSV extends SuperRelatorioSV {

    InscricaoControle inscricaoControle;
    private EmitirBoletoInscricaoRelInterfaceFacade emitirBoletoInscricaoRel;
    

    public void imprimirHTML(HttpServletRequest request, HttpServletResponse response, Integer codigoContaReceber) {
        try {
            String titulo = "";
            if (request.getParameter("titulo").equals("matricula")) {
                titulo = "Impressão do Boleto de Matrícula";
            } else if (request.getParameter("titulo").equals("inscricao")) {
                titulo = "Impressão do Boleto de Inscrição Vestibular";
            } else if (request.getParameter("titulo").equals("requerimento")) {
                titulo = "Impressão do Boleto do Requerimento";
            } else {
                titulo = "Impressão do Boleto";
            }
            String xml = emitirBoletoInscricaoRel.emitirRelatorio(codigoContaReceber);
            String design = EmitirBoletoInscricaoRel.getDesignIReportRelatorio();
            apresentarRelatorio(request, response, EmitirBoletoInscricaoRel.getIdEntidade(), xml,
                    titulo, "", "", "HTML",
                    "/" + EmitirBoletoInscricaoRel.getIdEntidade() + "/registros",
                    design, this.getNomeUsuario(), "true","EmitirBoletoInscricaoSV", "");
        } catch (Exception e) {
            inscricaoControle.setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	Integer codigoContaReceber = Integer.parseInt(request.getParameter("codigoContaReceber"));
        emitirBoletoInscricaoRel = new EmitirBoletoInscricaoRel();
        imprimirHTML(request, response, codigoContaReceber);
    }

    
    // <editor-fold defaultstate="collapsed" desc="Métodos HttpServlet. Clique no sinal de + à esquerda para editar o código.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
