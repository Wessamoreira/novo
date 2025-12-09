package controle.arquitetura;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import relatorio.arquitetura.SuperRelatorioSV;

/**
 *
 * @author Edgar
 */
public class FavoritoSV extends SuperRelatorioSV {

    @SuppressWarnings("FieldNameHidesFieldInSuperclass")
    public static final long serialVersionUID = 1L;

    public void adicionarFavorito(HttpServletRequest request, HttpServletResponse response) {
        try {
            Boolean favoritoOn = Boolean.parseBoolean(request.getParameter("favoritoOn"));
            String pagina = request.getParameter("valor");
            String booleanMarcarFavorito = request.getParameter("param");
        } catch (Exception e) {
            //System.out.println("Erro FavoritoSV: " + e.getMessage());
            e.getMessage();
        } finally {
        }
    }

    /** in
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        adicionarFavorito(request, response);
    }

    public String getCaminhoPastaWeb() {
        String diretorioPastaWeb = null;
        ServletContext servletContext = (ServletContext) this.context().getExternalContext().getContext();
        diretorioPastaWeb = servletContext.getRealPath("");
        return diretorioPastaWeb;
    }
    // <editor-fold defaultstate="collapsed" desc="Métodos HttpServlet. Clique no sinal de + à esquerda para editar o código.">

    /**
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
