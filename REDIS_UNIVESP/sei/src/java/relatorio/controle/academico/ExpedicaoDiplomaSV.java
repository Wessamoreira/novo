package relatorio.controle.academico;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import negocio.comuns.utilitarias.UtilNavegacao;
import relatorio.arquitetura.SuperRelatorioSV;
import relatorio.negocio.jdbc.academico.DiplomaAlunoRel;
import controle.academico.ExpedicaoDiplomaControle;
@Deprecated
public class ExpedicaoDiplomaSV extends SuperRelatorioSV {

	protected DiplomaAlunoRel diplomaAlunoRel;
	protected String matricula;
	protected Integer codFuncPrincipal;
	protected Integer codFuncSecundario;
	protected Integer codCargoFuncPrincipal;
	protected Integer codCargoFuncSecundario;
	protected String numeroRegistroDiplomaViaAnterior;
	protected String numeroProcessoViaAnterior;
	protected String via;

	public void imprimirPDF(HttpServletRequest request, HttpServletResponse response) {
		try {
//			diplomaAlunoRel.popularObjetos(getMatricula(), getCodFuncPrincipal(), getCodFuncSecundario(), getCodCargoFuncPrincipal(), getCodCargoFuncSecundario(), null, null);
			diplomaAlunoRel.setDescricaoFiltros("");
			String titulo = "Diploma do Aluno";
			String nomeEmpresa = super.getUnidadeEnsinoLogado().getNome();
//			String design = diplomaAlunoRel.getDesignIReportRelatorio();
//			apresentarRelatorioObjetos(diplomaAlunoRel.getIdEntidade(), titulo, nomeEmpresa, "", "PDF", "", design, "", diplomaAlunoRel.getDescricaoFiltros(), diplomaAlunoRel.criarObjeto(getNumeroRegistroDiplomaViaAnterior(), getNumeroProcessoViaAnterior(), getVia()),
//					diplomaAlunoRel.getCaminhoBaseRelatorio());
		} catch (Exception e) {
//			ExpedicaoDiplomaControle expedicaoDiplomaControle = (ExpedicaoDiplomaControle) UtilNavegacao.getControlador("ExpedicaoDiplomaControle");
//			expedicaoDiplomaControle.setMensagemDetalhada("msg_erro", e.getMessage());
			response.setContentType("text/html");
			java.io.PrintWriter out = null;
			try {
			out = response.getWriter();
			} catch (IOException ex) {
			}
			out.println("<html><script language='javascript'>alert('Erro ao tentar imprimir o diploma -> " + e.getMessage() + "')</script></html>");
			out = null;
		}
	}

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws Exception
	 */
	protected void processRequest(HttpServletRequest request, HttpServletResponse response) {
		diplomaAlunoRel = new DiplomaAlunoRel();
		setMatricula(String.valueOf(request.getParameter("matricula")));
		setCodFuncPrincipal(Integer.parseInt(request.getParameter("codFuncPrincipal")));
		setCodFuncSecundario(Integer.parseInt(request.getParameter("codFuncSecundario")));
		setCodCargoFuncPrincipal(Integer.parseInt(request.getParameter("codCargoFuncPrincipal")));
		setCodCargoFuncSecundario(Integer.parseInt(request.getParameter("codCargoFuncSecundario")));
		setNumeroRegistroDiplomaViaAnterior(request.getParameter("numeroRegistroDiplomaViaAnterior"));
		setNumeroProcessoViaAnterior(request.getParameter("numeroProcessoViaAnterior"));
		setVia(request.getParameter("via"));
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
	public String getMatricula() {
		return matricula;
	}

	/**
	 * @param matricula
	 *            the matricula to set
	 */
	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public Integer getCodFuncPrincipal() {
		return codFuncPrincipal;
	}

	public void setCodFuncPrincipal(Integer codFuncPrincipal) {
		this.codFuncPrincipal = codFuncPrincipal;
	}

	public Integer getCodFuncSecundario() {
		return codFuncSecundario;
	}

	public void setCodFuncSecundario(Integer codFuncSecundario) {
		this.codFuncSecundario = codFuncSecundario;
	}

	public Integer getCodCargoFuncPrincipal() {
		return codCargoFuncPrincipal;
	}

	public void setCodCargoFuncPrincipal(Integer codCargoFuncPrincipal) {
		this.codCargoFuncPrincipal = codCargoFuncPrincipal;
	}

	public Integer getCodCargoFuncSecundario() {
		return codCargoFuncSecundario;
	}

	public void setCodCargoFuncSecundario(Integer codCargoFuncSecundario) {
		this.codCargoFuncSecundario = codCargoFuncSecundario;
	}

	public String getNumeroRegistroDiplomaViaAnterior() {
		return numeroRegistroDiplomaViaAnterior;
	}

	public void setNumeroRegistroDiplomaViaAnterior(String numeroRegistroDiplomaViaAnterior) {
		this.numeroRegistroDiplomaViaAnterior = numeroRegistroDiplomaViaAnterior;
	}

	public String getNumeroProcessoViaAnterior() {
		return numeroProcessoViaAnterior;
	}

	public void setNumeroProcessoViaAnterior(String numeroProcessoViaAnterior) {
		this.numeroProcessoViaAnterior = numeroProcessoViaAnterior;
	}

	public String getVia() {
		return via;
	}

	public void setVia(String via) {
		this.via = via;
	}
}