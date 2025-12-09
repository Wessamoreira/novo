/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package webservice.servicos;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FormacaoAcademicaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.CursoInteresseVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.enumerador.TipoProspectEnum;
import negocio.comuns.processosel.PreInscricaoLogVO;
import negocio.comuns.processosel.PreInscricaoVO;
import negocio.comuns.processosel.enumeradores.SituacaoLogPreInscricaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.FacadeFactory;

/**
 *
 * @author Edigar
 * 
 * Ver html de exemplo chamado preInscricaoExterna.html, 
 * observe que as combobox de cidade e naturalidade 
 * devem ser geradas de acordo com a tabela cidade da base do cliente.
 * Nem todos os campos precisam ser usado e porém deve ser mantidos os campos Nome, Email e um dos telefones.
 * Caso esteja a configuração padrão do sistema esteja definido pra validar o cpf na pré-inscrição o mesmo deve ser mantido 
 * 
 */
public class PreInscricaoSV extends HttpServlet {
    
    public static final long serialVersionUID = 1L;
    private WebApplicationContext applicationContext;
    private FacadeFactory facadeFactory;    
    
   @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        final WebApplicationContext context =
                WebApplicationContextUtils.getWebApplicationContext(
                config.getServletContext());

        setApplicationContext(context);
    }
   
   public boolean consultaProspectPorEmail(ProspectsVO prospectVO) throws Exception {
       ProspectsVO pro = getFacadeFactory().getProspectsFacade().consultarPorEmailUnico(prospectVO.getEmailPrincipal(), false, null);
       if (pro.getCodigo().intValue() != 0) {
           prospectVO = pro;
           return true;
       }
       return false;
   }
    
    public ProspectsVO consultaProspectPessoaEmail(PessoaVO pessoaVO){
        ProspectsVO prospectVO = new ProspectsVO();
        if (pessoaVO.getCodigo() == 0 && !pessoaVO.getEmail().trim().equals("")){
            prospectVO.setEmailPrincipal(pessoaVO.getEmail());
            prospectVO.setTipoProspect(TipoProspectEnum.FISICO);
            try {
                ProspectsVO pro = getFacadeFactory().getProspectsFacade().consultarPorEmailUnico(prospectVO.getEmailPrincipal(), false, null);
                if (pro.getCodigo().intValue() != 0) {
                    pessoaVO.setCodProspect(pro.getCodigo());
                    pessoaVO.setNovoObj(Boolean.FALSE);
                    pessoaVO.setCPF(pro.getCpf());
                    pessoaVO.setNome(pro.getNome());
                    pessoaVO.setNomeBatismo(pro.getNomeBatismo());
                    pessoaVO.setSexo(pro.getSexo());
                    pessoaVO.setEmail(pro.getEmailPrincipal());
                    pessoaVO.setTelefoneRes(pro.getTelefoneResidencial());
                    pessoaVO.setCelular(pro.getCelular());
                    pessoaVO.setDataNasc(pro.getDataNascimento());
                    pessoaVO.setCEP(pro.getCEP());
                    pessoaVO.setEndereco(pro.getEndereco());
                    pessoaVO.setSetor(pro.getSetor());
                    pessoaVO.setCidade(pro.getCidade());
                    return pro;
                } else {
                    PessoaVO p = getFacadeFactory().getPessoaFacade().consultarPorEmailUnico(pessoaVO.getEmail(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, null);
                    if (p.getCodigo().intValue() != 0) {
                        pessoaVO = p;
                    } 
                    return null;
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
        return null;
    }    

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws Exception 
     * @throws BeansException 
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws BeansException, Exception {
        response.setContentType("text/html;charset=UTF-8");
        String curso = "";
        String turno = "";
        String nome = "";
        String email = "";
        Boolean ocorreuErro = false;
        PrintWriter out = response.getWriter();  
        //VO responsavel por manter o log, setando campo a campo do que vem da requisição, para gravar um possivel Exception 
        PreInscricaoLogVO preInscricaoLogVO = new PreInscricaoLogVO();
        try {
//        	out.println();
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>SEI - Retorno Pré-inscrição</title>");  
//            out.println("</head>");
//            out.println("<body>");
            // Obtendo configuracao geral do sistema
            ConfiguracaoGeralSistemaVO configuracaoVO = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, 0);
            if (request.getRemoteAddr().contains("66.249.89.141") || request.getRemoteAddr().equals("66.249.89.141")) {
            	 throw new Exception ("Erro, inscrição.");
            }
            
            curso = request.getParameter("curso");
            nome = request.getParameter("nome");
            turno = request.getParameter("turno");
            email = request.getParameter("email");
            String unidade = request.getParameter("unidade");
            String celular = request.getParameter("celular");
            String residencial = request.getParameter("residencial");
            String cursoGraduado = request.getParameter("cursoGraduacao");
            String escolaridade = request.getParameter("escolaridade");
            String situacao = request.getParameter("situacaoCursoGraduacao");
            // Unidade
            UnidadeEnsinoVO unidadeEnsinoVO = new UnidadeEnsinoVO();
            unidadeEnsinoVO.setCodigo(Integer.valueOf(unidade));
            preInscricaoLogVO.setUnidadeEnsino(Integer.valueOf(unidade));
            unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(unidadeEnsinoVO.getCodigo(), false, null);
            // Curso
            CursoVO cursoVO = new CursoVO();
            cursoVO.setCodigo(Integer.valueOf(curso));
            preInscricaoLogVO.setCurso(Integer.valueOf(curso));
            cursoVO = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(cursoVO.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, null);            
            // Turno
            TurnoVO turnoVO = new TurnoVO();
			if (turno != null) {
				turnoVO.setCodigo(Integer.valueOf(turno));
				preInscricaoLogVO.setTurno(Integer.valueOf(turno));
				turnoVO = getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(turnoVO.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, null);            
			}
            // Curso de Interesse
            CursoInteresseVO cursoInt = new CursoInteresseVO();
            cursoInt.setCurso(cursoVO);
            cursoInt.setTurno(turnoVO);
            cursoInt.setDataCadastro(new Date());
            // Formacao Academica
            FormacaoAcademicaVO formacaoProspectVO = null;
            if ((cursoGraduado != null) && (!cursoGraduado.isEmpty())) {
                formacaoProspectVO = new FormacaoAcademicaVO();
                formacaoProspectVO.setEscolaridade("GR");
                formacaoProspectVO.setCurso(cursoGraduado);
                formacaoProspectVO.setInstituicao("Informação Pendente");
                formacaoProspectVO.setSituacao(situacao);
                preInscricaoLogVO.setEscolaridade(formacaoProspectVO.getEscolaridade());
                preInscricaoLogVO.setCursoGraduado(cursoGraduado);
                preInscricaoLogVO.setSituacaoCursoGraduacao(situacao);
            }           
            
            // Prospect
            ProspectsVO prospect = new ProspectsVO();
            prospect.setTipoProspect(TipoProspectEnum.FISICO);
            prospect.setNome(nome);
            prospect.setNomeBatismo(nome);
            preInscricaoLogVO.setNomeProspect(nome);            
            prospect.setEmailPrincipal(email);
            preInscricaoLogVO.setEmailProspect(email);
            prospect.setCelular(celular);
            preInscricaoLogVO.setCelularProspect(celular);
            prospect.setCpf(request.getParameter("cpf"));
            preInscricaoLogVO.setCpfProspect(prospect.getCpf());
            prospect.setRg(request.getParameter("rg"));
            preInscricaoLogVO.setRgProspect(prospect.getRg());
            prospect.setOrgaoEmissor(request.getParameter("emissorrg"));
            preInscricaoLogVO.setEmissorrgProspect(prospect.getOrgaoEmissor());
            prospect.setEstadoEmissor(request.getParameter("estadoemissorg"));
            preInscricaoLogVO.setEstadoemissorgProspect(prospect.getEstadoEmissor());
            prospect.setEstadoCivil(request.getParameter("estadocivil"));
            preInscricaoLogVO.setEstadocivilProspect(prospect.getEstadoCivil());
            if(request.getParameter("dataexprg") != null){
            	prospect.setDataExpedicao(Uteis.getData(request.getParameter("dataexprg"), "dd/MM/yyyy"));
            	preInscricaoLogVO.setDataexprgProspect(prospect.getDataExpedicao());
            }
            if(request.getParameter("datanasc") != null){
            	prospect.setDataNascimento(Uteis.getData(request.getParameter("datanasc"), "dd/MM/yyyy"));
            	preInscricaoLogVO.setDatanascProspect(prospect.getDataNascimento());
            }
            prospect.setSexo(request.getParameter("sexo"));
            preInscricaoLogVO.setSexoProspect(prospect.getSexo());
            prospect.setCEP(request.getParameter("cep"));
            preInscricaoLogVO.setCepProspect(prospect.getCEP());
            prospect.setEndereco(request.getParameter("endereco"));
            preInscricaoLogVO.setEnderecoProspect(prospect.getEndereco());
            prospect.setSetor(request.getParameter("setor"));
            preInscricaoLogVO.setSetorProspect(prospect.getSetor());
            if(request.getParameter("cidade") != null && !request.getParameter("cidade").trim().isEmpty()){
            	prospect.getCidade().setCodigo(Integer.valueOf(request.getParameter("cidade")));
            	preInscricaoLogVO.setCidadeProspect(Integer.valueOf(request.getParameter("cidade")));
            }
            if(request.getParameter("naturalidade") != null && !request.getParameter("naturalidade").trim().isEmpty()){
            	prospect.getNaturalidade().setCodigo(Integer.valueOf(request.getParameter("naturalidade")));
            	preInscricaoLogVO.setNaturalidadeProspect(Integer.valueOf(request.getParameter("naturalidade")));
            }
            prospect.setTelefoneResidencial(residencial);
            preInscricaoLogVO.setTelResidencialProspect(prospect.getTelefoneResidencial());
            prospect.setUnidadeEnsino(unidadeEnsinoVO);
			if (formacaoProspectVO != null) {
				prospect.getFormacaoAcademicaVOs().add(formacaoProspectVO);
			}
            prospect.getCursoInteresseVOs().add(cursoInt);
            
            
            // Preinscricao
            PreInscricaoVO preinscricaoVO = new PreInscricaoVO();
            preinscricaoVO.setUnidadeEnsino(unidadeEnsinoVO);
            preinscricaoVO.setCurso(cursoVO);
            preinscricaoVO.setData(new Date());
            preinscricaoVO.setProspect(prospect);
           
            getFacadeFactory().getPreInscricaoFacade().incluirPreInscricaoAPartirSiteOuHomePreInscricao(preinscricaoVO, configuracaoVO);
            preInscricaoLogVO.setProspectsVO(preinscricaoVO.getProspect());
        } catch (Exception e) {
        	preInscricaoLogVO.setMensagemErro(e.getMessage());
            ocorreuErro = Boolean.TRUE;
            RequestDispatcher rd = request.getRequestDispatcher(request.getParameter("urlRetornoErro"));
            out.println("<h1>ERRO</h1>");
            out.println("<h2>" + e.getMessage() + "</h2>");
            rd.include(request, response);
        } finally {            
//        	preInscricaoLogVO.setFalhaPreInscricao(ocorreuErro);
        	preInscricaoLogVO.setSituacaoLogPreInscricao(ocorreuErro ? SituacaoLogPreInscricaoEnum.FALHA : SituacaoLogPreInscricaoEnum.SUCESSO);
        	getFacadeFactory().getPreInscricaoLogFacade().incluir(preInscricaoLogVO);
            if (!ocorreuErro) {            	
            	RequestDispatcher rd = request.getRequestDispatcher(request.getParameter("urlRetornoSucesso"));
                out.println("<h1>SUCESSO</h1>");
                out.println("<h2>Pré-inscrição realizada com sucesso!</h2>");                
                rd.include(request, response);
            }
//            out.println("</body>");
//            out.println("</html>");
            out.close();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
			processRequest(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
			processRequest(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
		}
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    /**
     * @return the applicationContext
     */
    public WebApplicationContext getApplicationContext() throws Exception {
        if (applicationContext == null) {
            throw new Exception("Não foi possível obter o Contexto do Spring");
        }
        return applicationContext;
    }

    /**
     * @param applicationContext the applicationContext to set
     */
    public void setApplicationContext(WebApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void setFacadeFactory(FacadeFactory facadeFactory) {
        this.facadeFactory = facadeFactory;
    }

    public FacadeFactory getFacadeFactory() throws BeansException, Exception {
        if (facadeFactory == null) {
            facadeFactory = (FacadeFactory) getApplicationContext().getBean("facadeFactory");
        }
        return facadeFactory;
    }
}
