package controle.arquitetura;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import jakarta.faces.application.NavigationHandler;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.PhaseEvent;
import jakarta.faces.event.PhaseId;
import jakarta.faces.event.PhaseListener;
import jakarta.servlet.http.HttpServletRequest;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.administrativo.ConfiguracaoLdap;
import negocio.facade.jdbc.arquitetura.UsuarioPerfilAcesso;
import negocio.interfaces.administrativo.ConfiguracaoLdapInterfaceFacade;
import negocio.interfaces.arquitetura.UsuarioPerfilAcessoInterfaceFacade;
import negocio.comuns.administrativo.ConfiguracaoLdapVO;


public class ControleAcessoListener implements PhaseListener {

    private static final long serialVersionUID = 5123377234863336833L;
    
    private ConfiguracaoLdapInterfaceFacade configuracaoLdapInterfaceFacade;
    @Autowired
    private UsuarioPerfilAcessoInterfaceFacade usuarioPerfilAcessoInterfaceFacade;
    
    public void afterPhase(PhaseEvent event) {
        if (event != null && event.getPhaseId().equals(PhaseId.RESTORE_VIEW)) {
            FacesContext facesContext = event.getFacesContext();
            if (facesContext != null && facesContext.getViewRoot() != null) {
                if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("retornarLogin") != null &&
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("retornarLogin") instanceof Boolean
                        && (Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("retornarLogin")) {
                    try {
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("retornarLogin");
                        FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
                        return;
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                String currentPage = facesContext.getViewRoot().getViewId();      
                
                this.validarADLogin(facesContext, currentPage);
                
                if (currentPage.contains("visaoAdministrador")) {
                    /*
                     * Telas especficas para administrao do sistema, onde s  permitida login por um usurio administrador.
                     * Tabela usuario possui um campo booleano denominado administrador.
                     */
                    if (facesContext.getExternalContext().getSessionMap().get("administradorLogado") != null) {
                        setTelaAcessoUsuario(facesContext, currentPage, true);
                    }
                } else if (!currentPage.contains("index.xhtml")
                        && !currentPage.contains("admin.xhtml")
                        && (currentPage.contains(".xhtml")
                        && !currentPage.contains("redefinirSenha.xhtml")
                        && !currentPage.contains("importacaoForm.xhtml")
                        && !currentPage.contains("solicitarNovaSenha.xhtml")
                        && !currentPage.contains("index2Simulacao.xhtml")
                        && !currentPage.contains("mensagemRejeitada.xhtml")
                        && !currentPage.contains("acessoRestrito.xhtml")
                        && !currentPage.contains("paginaErro.xhtml")
                        && !currentPage.contains("paginaErroCustomizada.xhtml")
                        && !currentPage.contains("calendarioCandidato.xhtml")
                        && !currentPage.contains("homeCandidato.xhtml")
                        && !currentPage.contains("boletoCandidato.xhtml")
                        && !currentPage.contains("buscaCandidatoVaga.xhtml")
                        && !currentPage.contains("cursoCandidato.xhtml")
                        && !currentPage.contains("inscricaoCandidato.xhtml")
                        && !currentPage.contains("homePreInscricao.xhtml")
                        && !currentPage.contains("homeOuvidoriaForm.xhtml")
                        && !currentPage.contains("bancoCurriculos.xhtml")
                        && !currentPage.contains("liberarSessaoCliente.xhtml")
                        && !currentPage.contains("menuVisaoCandidato.xhtml")
                        && !currentPage.contains("resultadoCandidato.xhtml")
                        && !currentPage.contains("cadastroVisaoParceiro.xhtml")
                        && !currentPage.contains("visaoCandidato/comprovanteInscricao.xhtml")
                        && !currentPage.contains("documentoAssinado.xhtml")
                        && !currentPage.contains("matriculaExterna")
                        && !currentPage.contains("showCase")
                        && !currentPage.contains("minhaBiblioteca")
                        && !currentPage.contains("templates/template.xhtml")
                        && !currentPage.contains("templates/menuLateral.xhtml")
                        && !currentPage.contains("interacaoWorkflowForm.xhtml")
                        && !currentPage.contains("redefinicaoSenha.xhtml")
                        && !currentPage.contains("homeOuvidoriaCons.xhtml")
                        && !currentPage.contains("homeOuvidoriaForm.xhtml")
                        && !currentPage.contains("paginaLexMagister.xhtml")
                        && !currentPage.contains("visualizarCandidatoVagaCons.xhtml")
                        && !currentPage.contains("historico.xhtml")
    					&& !currentPage.contains("diploma.xhtml"))) {

                    LoginControle loginControle = (LoginControle) facesContext.getExternalContext().getSessionMap().get("LoginControle");
                    if (loginControle == null || loginControle.getUsuarioLogado() == null
                            || loginControle.getUsuarioLogado().getCodigo() == null || loginControle.getUsuarioLogado().getCodigo() == 0) {
                        FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("retornarLogin", true);
                    } else if ((!getTelaAcessoUsuario(facesContext).containsKey(currentPage) && !loginControle.autenticarPermissaoAcessoTela(currentPage))
                            || (getTelaAcessoUsuario(facesContext).containsKey(currentPage) && !getTelaAcessoUsuario(facesContext).get(currentPage))) {
                        if (loginControle.getUsuarioLogado().getIsApresentarVisaoAdministrativa() &&
                                (currentPage.contains("homeAdministrador.xhtml")
                                        || currentPage.contains("administrativo.xhtml")
                                        || currentPage.contains("academico.xhtml")
                                        || currentPage.contains("financeiro.xhtml")
                                        || currentPage.contains("processoSeletivo.xhtml")
                                        || currentPage.contains("EAD.xhtml")
                                        || currentPage.contains("biblioteca.xhtml")
                                        || currentPage.contains("crm.xhtml")
                                        || currentPage.contains("avaliacaoInstitucional.xhtml")
                                        || currentPage.contains("bancoCurriculo.xhtml")
                                        || currentPage.contains("planoOrcamentario.xhtml")
                                        || currentPage.contains("painelGestor.xhtml")
                                        || currentPage.contains("compras.xhtml")
                                        || currentPage.contains("avaliacaoInstitucionalPublicacaoRel.xhtml"))) {
                            setTelaAcessoUsuario(facesContext, currentPage, false);
                        } else if ((loginControle.getUsuarioLogado().getIsApresentarVisaoAluno() || loginControle.getUsuarioLogado().getIsApresentarVisaoPais()) &&
                                (currentPage.contains("telaInicialVisaoAluno.xhtml")
                                        || currentPage.contains("estudoOnlineForm.xhtml")
                                        || currentPage.contains("avaliacaoInstitucionalPublicacaoRel.xhtml"))) {
                            setTelaAcessoUsuario(facesContext, currentPage, false);
                        } else if ((loginControle.getUsuarioLogado().getIsApresentarVisaoProfessor()) &&
                                (currentPage.contains("telaInicialVisaoProfessor.xhtml")
                                        || currentPage.contains("avaliacaoInstitucionalPublicacaoRel.xhtml"))) {
                            setTelaAcessoUsuario(facesContext, currentPage, false);
                        } else if ((loginControle.getUsuarioLogado().getIsApresentarVisaoCoordenador()) &&
                                (currentPage.contains("telaInicialVisaoCoordenador.xhtml")
                                        || currentPage.contains("avaliacaoInstitucionalPublicacaoRel.xhtml"))) {
                            setTelaAcessoUsuario(facesContext, currentPage, false);
                        } else {
                            NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
                            nh.handleNavigation(facesContext, null, "acessoRestrito");
                            setTelaAcessoUsuario(facesContext, currentPage, false);
                        }
                    } else if (!loginControle.getUsuarioLogado().getIsApresentarVisaoAdministrativa() && currentPage.contains("avaliacaoInstitucionalPublicacaoRel.xhtml")) {
                    	setTelaAcessoUsuario(facesContext, currentPage, false);
                    } else {
                        setTelaAcessoUsuario(facesContext, currentPage, true);

                        /**
                         * tratativa feita para corrigir o ERRO 404 aps finalizar uma simulao de usuario
                         * comportamento do erro: ao simular um usuario no simulao de usuario e finalizar a simulao retorna
                         * para o homeAdministrativo, aps retornar ao homeAdministrativo e clicar no boto voltar do navegador
                         * acessava a pagina "acesso restrito" porem sem a devida tratativa e apresentava o erro 404.
                         */
                        if (loginControle.getUsuarioLogado().getIsApresentarVisaoAdministrativa() &&
                                (currentPage.contains("telaInicialVisaoCoordenador.xhtml") ||
                                        currentPage.contains("telaInicialVisaoAluno.xhtml") ||
                                        currentPage.contains("telaInicialVisaoProfessor.xhtml")
                                        //INICIO ALUNO
                                        || currentPage.contains("dadosPessoaisAluno.xhtml") || currentPage.contains("recadoAluno.xhtml")
                                        || currentPage.contains("atividadeDiscursivaAlunoCons.xhtml") || currentPage.contains("declaracaoImpostoRenda.xhtml")
                                        || currentPage.contains("minhasAdvertenciasAluno.xhtml") || currentPage.contains("meusHorariosAluno.xhtml")
                                        || currentPage.contains("minhasContasPagarAluno.xhtml") || currentPage.contains("requerimentoAluno.xhtml")
                                        || currentPage.contains("downloadArquivo.xhtml") || currentPage.contains("forumAlunoCons.xhtml")
                                        || currentPage.contains("meusAmigosAluno.xhtml") || currentPage.contains("meusProfessoresAluno.xhtml")
                                        || currentPage.contains("minhasDisciplinasAluno.xhtml") || currentPage.contains("planoDeEstudoAluno.xhtml")
                                        || currentPage.contains("minhasFaltasAluno.xhtml") || currentPage.contains("meusContratosAluno.xhtml")
                                        || currentPage.contains("entregaDocumentoAluno.xhtml") || currentPage.contains("buscaBibliotecaAluno.xhtml")
                                        || currentPage.contains("emprestimosAluno.xhtml") || currentPage.contains("matriculaOnlineVisaoAlunoForm.xhtml")
                                        || currentPage.contains("configuracaoAluno.xhtml")
                                        //FIM ALUNO

                                        //INICIO PROFESSOR
                                        || currentPage.contains("criterioAvaliacaoVisaoProfessorCons.xhtml") || currentPage.contains("dadosPessoaisProfessor.xhtml")
                                        || currentPage.contains("recadosProfessor.xhtml")
                                        || currentPage.contains("meusHorariosProfessor.xhtml") || currentPage.contains("registrarAulaProfessor.xhtml")
                                        || currentPage.contains("registrarNotaProfessor.xhtml") || currentPage.contains("requerimentoProfessorCons.xhtml")
                                        || currentPage.contains("advertenciaVisaoProfessor.xhtml") || currentPage.contains("disciplinaProfessor.xhtml")
                                        || currentPage.contains("cursoProfessor.xhtml") || currentPage.contains("uploadArquivosProfessor.xhtml")
                                        || currentPage.contains("atividadeComplementarProfessor.xhtml") || currentPage.contains("atividadeDiscursivaProfessorCons.xhtml")
                                        || currentPage.contains("avaliacaoOnlineProfessorCons.xhtml") || currentPage.contains("gestaoEventoConteudoCons.xhtml")
                                        || currentPage.contains("configuracaoConteudoTurmaProfessorCons.xhtml") || currentPage.contains("duvidaProfessorCons.xhtml")
                                        || currentPage.contains("forumProfessorCons.xhtml") || currentPage.contains("questaoProfessorCons.xhtml")
                                        || currentPage.contains("listaExercicioProfessorCons.xhtml") || currentPage.contains("questaoProfessorCons.xhtml")
                                        || currentPage.contains("listaExercicioProfessorCons.xhtml") || currentPage.contains("ataProvaProfessor.xhtml")
                                        || currentPage.contains("relatorioProfessor.xhtml") || currentPage.contains("buscaBibliotecaProfessor.xhtml")
                                        || currentPage.contains("emprestimosProfessor.xhtml") || currentPage.contains("buscaBibliotecaProfessor.xhtml")
                                        || currentPage.contains("emprestimosProfessor.xhtml") || currentPage.contains("configuracaoProfessor.xhtml")
                                        || currentPage.contains("artefatoAjudaProfessor.xhtml")
                                        //FIM PROFESSOR

                                        //INICIO COORDENADOR
                                        || currentPage.contains("dadosPessoaisCoordenador.xhtml") || currentPage.contains("recadosCoordenador.xhtml")
                                        || currentPage.contains("horariosProfessorVisaoCoordenador.xhtml") || currentPage.contains("criterioAvaliacaoVisaoCoordenadorCons.xhtml")
                                        || currentPage.contains("estagioCoordenadorCons.xhtml") || currentPage.contains("registrarAulaCoordenador.xhtml")
                                        || currentPage.contains("requisicaoCoordenador.xhtml") || currentPage.contains("registrarNotaCoordenador.xhtml")
                                        || currentPage.contains("requerimentoCoordenadorCons.xhtml") || currentPage.contains("disciplinaCoordenador.xhtml")
                                        || currentPage.contains("cursoCoordenador.xhtml") || currentPage.contains("uploadArquivosCoordenador.xhtml")
                                        || currentPage.contains("acompanhamentoAtividadeComplementarCoordenador.xhtml") || currentPage.contains("registroAtividadeComplementarConsCoordenador.xhtml")
                                        || currentPage.contains("advertenciaConsCoordenador.xhtml") || currentPage.contains("monitoramentoAlunosEADVisaoCoodernadorCons.xhtml")
                                        || currentPage.contains("ocorrenciaPatrimonioCoordenadorCons.xhtml") || currentPage.contains("relatorioCoordenador.xhtml")
                                        || currentPage.contains("mapaDocumentoAssinadoPessoaCoordenadorForm.xhtml") || currentPage.contains("ouvidoriaCoordenadorForm.xhtml")
                                        || currentPage.contains("configuracaoCoordenador.xhtml")
                                        //FIM COORDENADOR

                                )) {
                            setTelaAcessoUsuario(facesContext, currentPage, false);
                            NavigationHandler nh = facesContext.getApplication().getNavigationHandler();
                            nh.handleNavigation(facesContext, null, "acessoRestrito");
                            setTelaAcessoUsuario(facesContext, currentPage, false);
                        }
                        /**
                         * Fim tratativa
                         */
                    }
                } else if (!currentPage.contains("index.xhtml")
                        && !currentPage.contains("admin.xhtml")
                        && !currentPage.contains("showCase.xhtml")
                        && (currentPage.contains(".xhtml") || currentPage.contains(".html") || currentPage.contains(".xhtml"))
                        && (currentPage.contains("redefinirSenha.xhtml")
                        || currentPage.contains("importacaoForm.xhtml")
                        || currentPage.contains("solicitarNovaSenha.xhtml")
                        || currentPage.contains("index2Simulacao.xhtml")
                        || currentPage.contains("mensagemRejeitada.xhtml")
                        || currentPage.contains("paginaErro.xhtml")
                        || currentPage.contains("calendarioCandidato.xhtml")
                        || currentPage.contains("homeCandidato.xhtml")
                        || currentPage.contains("boletoCandidato.xhtml")
                        || currentPage.contains("buscaCandidatoVaga.xhtml")
                        || currentPage.contains("cursoCandidato.xhtml")
                        || currentPage.contains("inscricaoCandidato.xhtml")
                        || currentPage.contains("homePreInscricao.xhtml")
                        || currentPage.contains("homeOuvidoriaForm.xhtml")
                        || currentPage.contains("bancoCurriculos.xhtml")
                        || currentPage.contains("liberarSessaoCliente.xhtml")
                        || currentPage.contains("menuVisaoCandidato.xhtml")
                        || currentPage.contains("resultadoCandidato.xhtml")
                        || currentPage.contains("cadastroVisaoParceiro.xhtml")
                        || currentPage.contains("comprovanteInscricao.xhtml")
                        || currentPage.contains("correcaoBancoDados.xhtml")
                        || currentPage.contains("redefinicaoSenha.xhtml")
                        || currentPage.contains("visualizarCandidatoVagaCons.xhtml"))) {
                    if (facesContext.getExternalContext().getSessionMap().get("usuarioLogado") != null) {
                        LoginControle loginControle = (LoginControle) facesContext.getExternalContext().getSessionMap().get("LoginControle");
                        if (loginControle != null) {
                            loginControle.abrirHomeCandidato();
                        }
                        facesContext.getExternalContext().getSessionMap().remove("usuarioLogado");
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Boolean> getTelaAcessoUsuario(FacesContext facesContext) {
        if (!facesContext.getExternalContext().getSessionMap().containsKey("ListaTelaAcesso")) {
            facesContext.getExternalContext().getSessionMap().put("ListaTelaAcesso", new HashMap<String, Boolean>(0));
        }
        return (Map<String, Boolean>) facesContext.getExternalContext().getSessionMap().get("ListaTelaAcesso");
    }

    private void setTelaAcessoUsuario(FacesContext facesContext, String tela, Boolean permiteAcesso) {
        getTelaAcessoUsuario(facesContext).put(tela, permiteAcesso);
    }

    @SuppressWarnings({"unused", "unchecked"})
    private void removerBackingbeanMemoria(FacesContext facesContext) {
        Map<String, String> keepAlive = (Map<String, String>) facesContext.getExternalContext().getSessionMap().get("KeepAlive");
        Map<String, Object> objSessions = facesContext.getExternalContext().getSessionMap();
        for (Map.Entry<String, Object> object : objSessions.entrySet()) {
            if (object.getValue() instanceof SuperControle
                    && getIsRemoverMemoria(keepAlive, object.getValue().getClass().getCanonicalName())) {
                SuperControle obj = (SuperControle) object.getValue();
                try {
                    limparRecursosMemoria(obj);
                } catch (IllegalArgumentException ex) {
                } catch (IllegalAccessException ex) {
                }
                obj.limparRecursosMemoria();
                obj = null;
                facesContext.getExternalContext().getSessionMap().remove(object.getKey());
            }
        }
    }

    public void limparRecursosMemoria(Object obj) throws IllegalArgumentException, IllegalAccessException {
        Class<?> classe = obj.getClass();
        for (Field field : classe.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(obj) != null) {
                try {
                    if (field.getType().getName().equals("java.lang.Integer")
                            || field.getType().getName().equals("java.lang.String")
                            || field.getType().getName().equals("java.lang.Double")
                            || field.getType().getName().equals("java.lang.Boolean")
                            || field.getType().getName().equals("java.lang.Long")) {
                        field.set(obj, null);
                    } else if (field.getType().getName().equals("java.util.List")
                            || field.getType().getName().equals("java.util.ArrayList")) {
                        List<?> lista = (List<?>) field.get(obj);
                        lista.clear();
                        field.set(obj, null);
                    } else if (field.get(obj) instanceof SuperVO) {
                        field.set(obj, null);
                    } else if (field.get(obj) instanceof SuperControle) {
                        field.set(obj, null);
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    private boolean getIsRemoverMemoria(Map<String, String> keepAlive, String entidade) {
        if (keepAlive.containsKey(entidade)) {
            return false;
        }
        return true;
    }

    public void beforePhase(PhaseEvent event) {
        if (FacesContext.getCurrentInstance() != null) {
            if (FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("retornarLogin") != null &&
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("retornarLogin") instanceof Boolean
                    && (Boolean) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("retornarLogin")) {
                try {
                    FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("retornarLogin");
                    FacesContext.getCurrentInstance().getExternalContext().redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath() + "/index.xhtml");
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    private void validarADLogin(FacesContext facesContext, String currentPage) {
        try {
            LoginControle loginControle = (LoginControle) facesContext.getExternalContext().getSessionMap().get("LoginControle");
            HttpServletRequest requisicao = (HttpServletRequest) facesContext.getExternalContext().getRequest();
            String saml = requisicao.getParameter("SAMLResponse");
            
            if (Uteis.isAtributoPreenchido(saml)) {
                String decriptado = new String(Base64.getDecoder().decode(saml.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
                Document documento = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(decriptado)));
                String emailInstitucional = this.obterEmail(documento);
                
                if(loginControle != null && (loginControle.getUsuarioLogado() == null || loginControle.getUsuarioLogado().getCodigo().intValue() == 0  || !loginControle.getEmailInstitucional().equals(emailInstitucional) ) ){
                    loginControle.setUsuario(new UsuarioVO());
                    if(Uteis.isAtributoPreenchido(emailInstitucional) && (!Uteis.isAtributoPreenchido(loginControle.getEmailInstitucional()) || loginControle.getEmailInstitucional().equals(emailInstitucional))) {
                        loginControle.setEmailInstitucional(emailInstitucional);
                        loginControle.setLogadoComEmailInstitucional(true); 
                    }   
                    
                    // --- CORREÇÃO AQUI ---
                    // Antes: facesContext.getExternalContext().redirect(loginControle.login3());
                    // Agora: Apenas chamamos o método, pois ele já é void e redireciona por dentro.
                    loginControle.login3(); 
                }
            } else if (currentPage.contains("index.xhtml")) {
                if (loginControle != null
                    && loginControle.getUsuarioLogado().getCodigo() != 0
                    && !loginControle.getUsuarioLogado().getVisaoLogar().trim().isEmpty() ) {
                    
                    Long tempoMaximo = loginControle.getSessionAplicacao().getMaxInactiveInterval() * 1000l;
                    
                    if (loginControle.getSessionAplicacao().getLastAccessedTime() + tempoMaximo < new Date().getTime()) {
                        if (loginControle.getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
                            loginControle.logout();
                        } else {
                            loginControle.logoutVisao();
                        }
                        facesContext.getExternalContext()
                                .redirect(FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
                                        + "/index.xhtml");
                    } else {
                        String retorno = "";
                        if (loginControle.getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
                            loginControle.getUsuario().setUsuarioPerfilAcessoVOs(getUsuarioPerfilAcessoInterfaceFacade().consultarUsuarioPerfilAcesso(loginControle.getUsuarioLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, loginControle.getUsuarioLogado()));                            
                            retorno = loginControle.logarDiretamenteComoAdministrador();
                        } else if (loginControle.getUsuarioLogado().getIsApresentarVisaoProfessor()) {
                            retorno = loginControle.logarDiretamenteComoProfessor();
                        } else if (loginControle.getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
                            retorno = loginControle.logarDiretamenteComoCoordenador();
                        } else if (loginControle.getUsuarioLogado().getIsApresentarVisaoAluno()) {
                            retorno = loginControle.logarDiretamenteComoAluno();
                        } else if (loginControle.getUsuarioLogado().getIsApresentarVisaoPais()) {
                            retorno = loginControle.logarDiretamenteComoPais();
                        }
                        
                        if (Uteis.isAtributoPreenchido(retorno)) {
                            // Tratamento para garantir o caminho correto
                            String path = retorno;
                            if(!path.startsWith("/")) {
                                path = "/" + path;
                            }
                            // Remove query params antigos do JSF se existirem
                            if(path.contains("?faces-redirect")) {
                                path = path.split("\\?")[0];
                            }

                            facesContext.getExternalContext().redirect(
                                    FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()
                                            + path);
                        }
                    }
                } else if (Uteis.isAtributoPreenchido(Uteis.getCookie("SAML")) 
                        && (loginControle == null || loginControle.getUsuarioLogado() == null || loginControle.getUsuarioLogado().getCodigo() == 0)) {
                    
                    String emailInstitucional = Uteis.getCookie("SAML");
                    
                    // Adicionei o import da classe ConfiguracaoLdapVO se não tiver
                    ConfiguracaoLdapVO configuracaoLdapVO = getConfiguracaoLdapInterfaceFacade().consultarConfiguracaoLdaps()
                            .stream()
                            .filter(conf -> emailInstitucional.toLowerCase().endsWith(conf.getDominio().toLowerCase()))
                            .findFirst()
                            .orElseThrow(() -> new Exception("Configurações de login para o domínio informado não encontrada."));
                    
                    saml = Uteis.obterSaml(configuracaoLdapVO);
                    String base64 = Uteis.obterRequisicaoBase64Saml(saml);
                    facesContext.getExternalContext().redirect(Uteis.obterUrlRedirecionamentoSaml(configuracaoLdapVO, base64, emailInstitucional));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String obterEmail(Document documento) throws XPathExpressionException {
        return XPathFactory.newInstance()
                .newXPath()
                .compile("/Response/Assertion/Subject/NameID/text()")
                .evaluate(documento);
    }
    
	public ConfiguracaoLdapInterfaceFacade getConfiguracaoLdapInterfaceFacade() {
		if (configuracaoLdapInterfaceFacade == null) {
			try {
				configuracaoLdapInterfaceFacade = new ConfiguracaoLdap();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return configuracaoLdapInterfaceFacade;
	}

	public void setConfiguracaoLdapInterfaceFacade(ConfiguracaoLdapInterfaceFacade configuracaoLdapInterfaceFacade) {
		this.configuracaoLdapInterfaceFacade = configuracaoLdapInterfaceFacade;
	}

	public UsuarioPerfilAcessoInterfaceFacade getUsuarioPerfilAcessoInterfaceFacade() {
		if(usuarioPerfilAcessoInterfaceFacade == null) {
			try {
				usuarioPerfilAcessoInterfaceFacade =  new UsuarioPerfilAcesso();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return usuarioPerfilAcessoInterfaceFacade;
	}

	public void setUsuarioPerfilAcessoInterfaceFacade(
			UsuarioPerfilAcessoInterfaceFacade usuarioPerfilAcessoInterfaceFacade) {
		this.usuarioPerfilAcessoInterfaceFacade = usuarioPerfilAcessoInterfaceFacade;
	}
	
	

}
