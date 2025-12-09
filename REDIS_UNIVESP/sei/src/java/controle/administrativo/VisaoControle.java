package controle.administrativo;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * visaoForm.jsp visaoCons.jsp) com as funcionalidades da classe <code>Visao</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Visao
 * @see VisaoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.VisaoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("VisaoControle")
@Scope("request")
@Lazy
public class VisaoControle extends SuperControle implements Serializable {

    private VisaoVO visaoVO;
    private List listaImagens;
    private Boolean css1;
    private Boolean css2;
    private Boolean css3;
    private Boolean css4;
    private Boolean css5;
    private Boolean css6;
    private Boolean existeImagem;
    
    
    public VisaoControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Visao</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() throws Exception {         removerObjetoMemoria(this);
        setVisaoVO(new VisaoVO());
        setCss1(Boolean.FALSE);
        setCss2(Boolean.FALSE);
        setCss3(Boolean.FALSE);
        setCss4(Boolean.FALSE);
        setCss5(Boolean.FALSE);
        setCss6(Boolean.FALSE);
        setExisteImagem(Boolean.FALSE);
       
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Visao</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
        VisaoVO obj = (VisaoVO) context().getExternalContext().getRequestMap().get("visao");
        obj.setNovoObj(Boolean.FALSE);
        setVisaoVO(obj);
//        getVisaoVO().setImagemTemp("../imagem/"+ getVisaoVO().getNomeImagemBackground());
//        if(getVisaoVO().getImagemBackground().equals("")){
//        setExisteImagem(Boolean.FALSE);
//        }else{
//            setExisteImagem(Boolean.TRUE);
//        }
        //validarPadraoCss();
        setMensagemID("msg_dados_editar");
        return "editar";
    }

//    public void validarPadraoCss() {
//        if (getVisaoVO().getCsspadrao().equals("Vermelho")) {
//            setCss1(Boolean.TRUE);
//        } else {
//            setCss1(Boolean.FALSE);
//        }
//        if (getVisaoVO().getCsspadrao().equals("Verde")) {
//            setCss2(Boolean.TRUE);
//        } else {
//            setCss2(Boolean.FALSE);
//        }
//        if (getVisaoVO().getCsspadrao().equals("Azul")) {
//            setCss3(Boolean.TRUE);
//        } else {
//            setCss3(Boolean.FALSE);
//        }
//        if (getVisaoVO().getCsspadrao().equals("Cinza")) {
//            setCss4(Boolean.TRUE);
//        } else {
//            setCss4(Boolean.FALSE);
//        }
//        if (getVisaoVO().getCsspadrao().equals("Roxo")) {
//            setCss5(Boolean.TRUE);
//        } else {
//            setCss5(Boolean.FALSE);
//        }
//        if (getVisaoVO().getCsspadrao().equals("Rosa")) {
//            setCss6(Boolean.TRUE);
//        } else {
//            setCss6(Boolean.FALSE);
//        }
//    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Visao</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (visaoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getVisaoFacade().incluir(visaoVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getVisaoFacade().alterar(visaoVO, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP VisaoCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
//            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
//                if (getControleConsulta().getValorConsulta().equals("")) {
//                    getControleConsulta().setValorConsulta("0");
//                }
//                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
//                objs = getFacadeFactory().getVisaoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS);
//            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getVisaoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>VisaoVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getVisaoFacade().excluir(visaoVO, getUsuarioLogado());
            setVisaoVO(new VisaoVO());
            setCss1(Boolean.FALSE);
            setCss2(Boolean.FALSE);
            setCss3(Boolean.FALSE);
            setCss4(Boolean.FALSE);
            setCss5(Boolean.FALSE); 
            setCss6(Boolean.FALSE); 
            setExisteImagem(Boolean.FALSE);
            setMensagemID("msg_dados_excluidos");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

//    public void upload(UploadEvent upload) {
//        try {
//            UploadItem item = upload.getUploadItem();
//            File item1 = item.getFile();
//
//            getVisaoVO().setImagemBackground(item.getFileName());
//
//            String nomeImagem = item.getFileName().substring(item.getFileName().lastIndexOf(File.separator) + 1);
//                        
//            item1.renameTo(new File(obterCaminhoWebFotos() + File.separator + nomeImagem));                                    
//            
//            getVisaoVO().setImagemTemp("../imagem/" + nomeImagem);
//            getVisaoVO().setNomeImagemBackground(nomeImagem);
//            setExisteImagem(Boolean.TRUE);
//            
//        } catch (Exception e) {
//            setMensagemDetalhada("msg_erro", e.getMessage());
//        }
//    }
    
    

//    public void ativarCss1() {
//        setCss2(Boolean.FALSE);
//        setCss3(Boolean.FALSE);
//        setCss4(Boolean.FALSE);
//        setCss5(Boolean.FALSE);
//        getVisaoVO().setCsspadrao("Vermelho");
//    }
//
//    public void ativarCss2() {
//        setCss1(Boolean.FALSE);
//        setCss3(Boolean.FALSE);
//        setCss4(Boolean.FALSE);
//        setCss5(Boolean.FALSE);
//        getVisaoVO().setCsspadrao("Verde");
//    }
//
//    public void ativarCss3() {
//        setCss1(Boolean.FALSE);
//        setCss2(Boolean.FALSE);
//        setCss4(Boolean.FALSE);
//        setCss5(Boolean.FALSE);
//        getVisaoVO().setCsspadrao("Azul");
//    }
//
//    public void ativarCss4() {
//        setCss1(Boolean.FALSE);
//        setCss2(Boolean.FALSE);
//        setCss3(Boolean.FALSE);
//        setCss5(Boolean.FALSE);
//        getVisaoVO().setCsspadrao("Cinza");
//    }
//
//    public void ativarCss5() {
//        setCss1(Boolean.FALSE);
//        setCss2(Boolean.FALSE);
//        setCss3(Boolean.FALSE);
//        setCss4(Boolean.FALSE);
//        getVisaoVO().setCsspadrao("Roxo");
//    }
//    public void ativarCss6() {
//        setCss1(Boolean.FALSE);
//        setCss2(Boolean.FALSE);
//        setCss3(Boolean.FALSE);
//        setCss4(Boolean.FALSE);
//        setCss5(Boolean.FALSE);
//        getVisaoVO().setCsspadrao("Rosa");
//    }


    public void irPaginaInicial() throws Exception {
        this.consultar();
    }

    public void irPaginaAnterior() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
        this.consultar();
    }

    public void irPaginaPosterior() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
        this.consultar();
    }

    public void irPaginaFinal() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
        this.consultar();
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        // itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {         removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return "consultar";
    }

    public VisaoVO getVisaoVO() {
        return visaoVO;
    }

    public void setVisaoVO(VisaoVO visaoVO) {
        this.visaoVO = visaoVO;
    }

    public List getListaImagens() {
        return listaImagens;
    }

    public void setListaImagens(List listaImagens) {
        this.listaImagens = listaImagens;
    }

    public Boolean getCss1() {
        return css1;
    }

    public void setCss1(Boolean css1) {
        this.css1 = css1;
    }

    public Boolean getCss2() {
        return css2;
    }

    public void setCss2(Boolean css2) {
        this.css2 = css2;
    }

    public Boolean getCss3() {
        return css3;
    }

    public void setCss3(Boolean css3) {
        this.css3 = css3;
    }

    public Boolean getCss4() {
        return css4;
    }

    public void setCss4(Boolean css4) {
        this.css4 = css4;
    }

    public Boolean getCss5() {
        return css5;
    }

    public void setCss5(Boolean css5) {
        this.css5 = css5;
    }

    public Boolean getExisteImagem() {
        return existeImagem;
    }

    public void setExisteImagem(Boolean existeImagem) {
        this.existeImagem = existeImagem;
    }

    public Boolean getCss6() {
        return css6;
    }

    public void setCss6(Boolean css6) {
        this.css6 = css6;
    }

}