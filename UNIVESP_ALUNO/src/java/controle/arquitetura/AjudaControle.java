package controle.arquitetura;

/**
 * Classe responsável por implementar a interação entre os componentes JSF da
 * página ajudaCons.jsp com as funcionalidades da classe
 * <code>ArtefatoAjudaVO</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @author Paulo Taucci
 * @see SuperControle
 * @see ArtefatoAjuda
 * @see ArtefatoAjudaVO
 */
import java.io.Serializable;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.basico.ArtefatoAjudaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("AjudaControle")
@Scope("session")
@Lazy
public class AjudaControle extends SuperControle implements Serializable {

    private String retornoTipoArtefato;
    private String urlVideo;
    private String moduloConsulta;

    public AjudaControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    public String novoVisaoProfessor() {
        return Uteis.getCaminhoRedirecionamentoNavegacao("artefatoAjudaProfessor.xhtml");
    }

    public String getRetornoTipoArtefato() {
        if (retornoTipoArtefato == null) {
            retornoTipoArtefato = "";
        }
        return retornoTipoArtefato;
    }

    public void setRetornoTipoArtefato(String retornoTipoArtefato) {
        this.retornoTipoArtefato = retornoTipoArtefato;
    }

    public String getUrlVideo() {
        if (urlVideo == null) {
            urlVideo = "";
        }
        return urlVideo;
    }

    public void setUrlVideo(String urlVideo) {
        this.urlVideo = urlVideo;
    }

    public void executarConsulta() {
        try {
        	if(Uteis.getIsValorNumerico(getModuloConsulta())) {
        		setListaConsulta(getFacadeFactory().getArtefatoAjudaFacade().consultarPorCodigo(Integer.parseInt(getModuloConsulta()), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        	}else {
        		setListaConsulta(getFacadeFactory().getArtefatoAjudaFacade().consultarArtefatoPorCodigos(getAplicacaoControle().getArtefatoAjudaKeys().get(getModuloConsulta())));
        	}
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
//            return Uteis.getCaminhoRedirecionamentoNavegacao("ajudaCons");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP
     * ajudaCons.jsp. Realiza uma consulta personalizada utilizando o método
     * "consultarPorTodosCamposESituacao" do facade ArtefatoAjuda. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da
     * pagina.
     *
     * @author Paulo Taucci
     */
    @SuppressWarnings("finally")
    public String consultar() {
        try {
            super.consultar();
            setListaConsulta(getFacadeFactory().getArtefatoAjudaFacade().consultarPorTodosCamposESituacao(
                    getControleConsulta().getValorConsulta(), false, false,
                    Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            return Uteis.getCaminhoRedirecionamentoNavegacao("ajudaCons");
        }
    }

    /**
     * Método responsável por executar a ação do link da página ajudaCons.jsp, que dependerá do tipo de Artefato.
     * Se for um vídeo abre um popup com o vídeo incorporado, se for um arquivo abre a caixa de download.
     * @author Paulo Taucci
     * @return
     */
    public String executarAcaoLink() {
        ArtefatoAjudaVO artefatoAjuda = (ArtefatoAjudaVO) context().getExternalContext().getRequestMap().get("artefatoAjudaItem");
        if (artefatoAjuda.getTipoArtefato().toString().equals("ARQUIVO")) {
            executarDownloadArquivo(artefatoAjuda.getLink());
        }
        if (artefatoAjuda.getTipoArtefato().toString().equals("VIDEO")) {
            executarAbrirVideo(artefatoAjuda.getLink());
        }
        return "";
    }

    /**
     * Método responsável por abrir a caixa de download de um arquivo. Como o arquivo será disponibilizado por FTP, basta
     * redirecionar para o link.
     * @author Paulo Taucci
     * @param link
     */
    public void executarDownloadArquivo(String link) {
        HttpServletResponse response = (HttpServletResponse) context().getExternalContext().getResponse();
        try {
            response.sendRedirect(link);
            context().responseComplete();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            setUrlVideo(null);
            setRetornoTipoArtefato("");
            response = null;
        }

    }

    /**
     * Método responsável por abrir um popup com o vídeo integrado para visualização.
     * @author Paulo Taucci
     * @param link
     */
    public void executarAbrirVideo(String link) {
        setUrlVideo(link);
        setRetornoTipoArtefato("abrirPopup('ajudaVideo.xhtml', 'ajudaVideo', 1290, 730);");
    }

    /**
     * @return the moduloConsulta
     */
    public String getModuloConsulta() {
        if (moduloConsulta == null) {
            moduloConsulta = "";
        }
        return moduloConsulta;
    }

    /**
     * @param moduloConsulta the moduloConsulta to set
     */
    public void setModuloConsulta(String moduloConsulta) {
        this.moduloConsulta = moduloConsulta;
    }
}
