package controle.arquitetura;

import java.io.File;
import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.basico.enumeradores.LayoutComprovanteMatriculaEnum;
import negocio.comuns.utilitarias.Uteis;
import relatorio.arquitetura.SuperRelatorioSV;
import relatorio.negocio.jdbc.academico.ComprovanteRenovacaoMatriculaRel;

/**
 * Servlet implementation class ComprovanteRenovacaoMatriculaSV
 */
public class ComprovanteRenovacaoMatriculaSV extends SuperRelatorioSV {

    public static final long serialVersionUID = 1L;
    private String matricula;
    private String nivelEducacional;
    private Integer matriculaPeriodo;
    private Integer tipoLayout;
    private ComprovanteRenovacaoMatriculaRel comprovanteRenovacaoMatriculaRel;
    private Integer unidadeEnsino;
    

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ComprovanteRenovacaoMatriculaSV() {
        super();
    }

    private void imprimirPDF(HttpServletRequest request, HttpServletResponse response) {
        try {
        	UsuarioVO usuario = (UsuarioVO) request.getSession().getAttribute("usuario");
            String titulo = null;
            String design = null;
            String nomeRelatorio = null;
            UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsino(), false, null);
            if (getTipoLayout() == 2) {
            	titulo = "FICHA DE INSCRIÇÃO";
                design = getComprovanteRenovacaoMatriculaRel().getDesignIReportRelatorio().substring(0, getComprovanteRenovacaoMatriculaRel().getDesignIReportRelatorio().lastIndexOf(".")) + getTipoLayout() + ".jrxml";
                nomeRelatorio = getComprovanteRenovacaoMatriculaRel().getIdEntidade() + getTipoLayout();	
            } else {
            	if(getNivelEducacional().equals("PO") || getNivelEducacional().equals("EX")){
                    titulo = "FICHA DE INSCRIÇÃO";
                    design = getComprovanteRenovacaoMatriculaRel().getDesignIReportRelatorio();
                    nomeRelatorio = getComprovanteRenovacaoMatriculaRel().getIdEntidade();
                }else{
                    titulo = "COMPROVANTE DE RENOVAÇÃO DE MATRÍCULA";
                    ConfiguracoesVO configuracoesVO = getFacadeFactory().getConfiguracoesFacade().consultarConfiguracaoASerUsada(false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getUnidadeEnsino());
                    if(configuracoesVO == null || configuracoesVO.getLayoutPadraoComprovanteMatricula() == null || configuracoesVO.getLayoutPadraoComprovanteMatricula().equals(LayoutComprovanteMatriculaEnum.LAYOUT_01)){
                        design = getComprovanteRenovacaoMatriculaRel().getDesignComprovanteMatriculaIReportRelatorio();
                        nomeRelatorio = getComprovanteRenovacaoMatriculaRel().getIdEntidadeComprovanteMatricula();
                    }else if(configuracoesVO.getLayoutPadraoComprovanteMatricula().equals(LayoutComprovanteMatriculaEnum.LAYOUT_02)){
                        design =  ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "ComprovanteRenovacaoMatriculaRelLayout2" + ".jrxml");
                        nomeRelatorio = "ComprovanteRenovacaoMatriculaRelLayout2";
                        
                    }
                
                }
            }
              
            persistirLayoutPadrao(getTipoLayout().toString());
            apresentarRelatorioObjetos(nomeRelatorio, titulo, unidadeEnsinoVO.getNome(), "", "PDF", "", design, "", getComprovanteRenovacaoMatriculaRel().getDescricaoFiltros(), getComprovanteRenovacaoMatriculaRel().criarObjeto(matriculaPeriodo, matricula, usuario ), getComprovanteRenovacaoMatriculaRel().getCaminhoBaseRelatorio(), request, response);
        } catch (Exception e) {
            e.getMessage();
        }
    }
    
    private void persistirLayoutPadrao(String valor) throws Exception {
        getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(valor, "comprovanteMatricula", "tipoDesignRelatorioComprovanteMatricula", null);    
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request
     *            servlet request
     * @param response
     *            servlet response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
        setComprovanteRenovacaoMatriculaRel(new ComprovanteRenovacaoMatriculaRel());
        setMatricula(String.valueOf(request.getParameter("matricula")));
        setNivelEducacional(String.valueOf(request.getParameter("nivelEducacional")));
        setMatriculaPeriodo(Integer.valueOf(request.getParameter("matriculaPeriodo")));
        setTipoLayout(Integer.valueOf(request.getParameter("tipoLayout")));
        setUnidadeEnsino(Integer.valueOf(request.getParameter("unidadeEnsino")));
        imprimirPDF(request, response);
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Integer getMatriculaPeriodo() {
        return matriculaPeriodo;
    }

    public void setMatriculaPeriodo(Integer matriculaPeriodo) {
        this.matriculaPeriodo = matriculaPeriodo;
    }

    public ComprovanteRenovacaoMatriculaRel getComprovanteRenovacaoMatriculaRel() {
        return comprovanteRenovacaoMatriculaRel;
    }

    public void setComprovanteRenovacaoMatriculaRel(ComprovanteRenovacaoMatriculaRel comprovanteRenovacaoMatriculaRel) {
        this.comprovanteRenovacaoMatriculaRel = comprovanteRenovacaoMatriculaRel;
    }

    public String getNivelEducacional() {
        return nivelEducacional;
    }

    public void setNivelEducacional(String nivelEducacional) {
        this.nivelEducacional = nivelEducacional;
    }

	public Integer getTipoLayout() {
		if (tipoLayout == null) {
			tipoLayout = 0;
		}
		return tipoLayout;
	}

	public void setTipoLayout(Integer tipoLayout) {
		this.tipoLayout = tipoLayout;
	}

	public Integer getUnidadeEnsino() {
		if (unidadeEnsino == null) {
			unidadeEnsino = 0;
		}
		return unidadeEnsino;
	}

	public void setUnidadeEnsino(Integer unidadeEnsino) {
		this.unidadeEnsino = unidadeEnsino;
	}

	
	
	
}
