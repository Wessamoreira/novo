package relatorio.controle.academico;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.academico.MatriculaInterfaceFacade;
import relatorio.arquitetura.SuperRelatorioSV;
import relatorio.negocio.jdbc.academico.DadosMatriculaRel;

public class DadosMatriculaSV extends SuperRelatorioSV {

    protected DadosMatriculaRel dadosMatriculaRel;
    protected MatriculaVO matriculaVO;
    protected MatriculaInterfaceFacade matriculaFacade;
    protected Integer matriculaPeriodo;
    private Integer codigoConfiguracaoGeralSistema;
    private String nomeUnidadeEnsino;

    private void imprimirPDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            String titulo = " PLANO DE ESTUDO ";
            String nomeEntidade = getNomeUnidadeEnsino();
            String design = dadosMatriculaRel.getDesignIReportRelatorio();

            ConfiguracaoGeralSistemaVO configuracaoGeralSistema = new ConfiguracaoGeralSistemaVO();
            try {
                configuracaoGeralSistema = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoConfiguracaoGeralSistema(
                        getCodigoConfiguracaoGeralSistema(), false, new UsuarioVO(), Uteis.NIVELMONTARDADOS_DADOSBASICOS );
            } catch (Exception e) {
            	 e.getMessage();
            }
            apresentarRelatorioObjetos(dadosMatriculaRel.getIdEntidade(), titulo, nomeEntidade, "", "PDF", "", design, "", dadosMatriculaRel.getDescricaoFiltros(),dadosMatriculaRel.criarObjeto(matriculaPeriodo, null, configuracaoGeralSistema, null), dadosMatriculaRel.getCaminhoBaseRelatorio(), request, response);

        } catch (Exception e) {
            e.getMessage();
        }
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     *
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        dadosMatriculaRel = new DadosMatriculaRel();
        setMatriculaPeriodo(Integer.valueOf(request.getParameter("matriculaPeriodo")));
        setNomeUnidadeEnsino(request.getParameter("nomeUnidadeEnsino"));
        String codigoConfiguracaoGeralSistema = request.getParameter("configuracaoGeralSistema");
        if(codigoConfiguracaoGeralSistema != null && !codigoConfiguracaoGeralSistema.isEmpty()){
        	setCodigoConfiguracaoGeralSistema(Integer.valueOf(codigoConfiguracaoGeralSistema));	
        }else{
        	setCodigoConfiguracaoGeralSistema(0);
        }
        

        imprimirPDF(request, response);
    }

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     */
    public String getServletInfo() {
        return "Short description";
    }

    /**
     * @return the matricula
     */
    public Integer getMatriculaPeriodo() {
        return matriculaPeriodo;
    }

    /**
     * @param matricula
     *            the matricula to set
     */
    public void setMatriculaPeriodo(Integer matriculaPeriodo) {
        this.matriculaPeriodo = matriculaPeriodo;
    }

    /**
     * @return the nomeUnidadeEnsino
     */
    public String getNomeUnidadeEnsino() {
        if (nomeUnidadeEnsino == null) {
            nomeUnidadeEnsino = "";
        }
        return nomeUnidadeEnsino;
    }

    /**
     * @param nomeUnidadeEnsino the nomeUnidadeEnsino to set
     */
    public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
        this.nomeUnidadeEnsino = nomeUnidadeEnsino;
    }

    /**
     * @return the codigoConfiguracaoGeralSistema
     */
    public Integer getCodigoConfiguracaoGeralSistema() {
        return codigoConfiguracaoGeralSistema;
    }

    /**
     * @param codigoConfiguracaoGeralSistema the codigoConfiguracaoGeralSistema to set
     */
    public void setCodigoConfiguracaoGeralSistema(Integer codigoConfiguracaoGeralSistema) {
        this.codigoConfiguracaoGeralSistema = codigoConfiguracaoGeralSistema;
    }
}
