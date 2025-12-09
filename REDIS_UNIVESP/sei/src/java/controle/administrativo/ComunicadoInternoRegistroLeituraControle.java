package controle.administrativo;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * comunicadoInternoRegistroLeituraForm.jsp comunicadoInternoRegistroLeituraCons.jsp) com as funcionalidades da classe <code>ComunicadoInternoRegistroLeitura</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see ComunicadoInternoRegistroLeitura
 * @see ComunicadoInternoRegistroLeituraVO
*/
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.ComunicadoInternoRegistroLeituraVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("ComunicadoInternoRegistroLeituraControle")
@Scope("request")
@Lazy
public class ComunicadoInternoRegistroLeituraControle extends SuperControle implements Serializable {
    private ComunicadoInternoRegistroLeituraVO comunicadoInternoRegistroLeituraVO;
    
    public ComunicadoInternoRegistroLeituraControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
    * Rotina responsável por disponibilizar um novo objeto da classe <code>ComunicadoInternoRegistroLeitura</code>
    * para edição pelo usuário da aplicação.
    */
    public String novo() {         removerObjetoMemoria(this);
        setComunicadoInternoRegistroLeituraVO(new ComunicadoInternoRegistroLeituraVO());
        setMensagemID("msg_entre_dados");
        return "editar";
    }

    /**
    * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ComunicadoInternoRegistroLeitura</code> para alteração.
    * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
    */
    public String editar() {
        ComunicadoInternoRegistroLeituraVO obj = (ComunicadoInternoRegistroLeituraVO)context().getExternalContext().getRequestMap().get("comunicadoInternoRegistroLeitura");
        obj.setNovoObj(Boolean.FALSE);
        setComunicadoInternoRegistroLeituraVO(obj);
        setMensagemID("msg_dados_editar");
        return "editar";
    }

    /**
    * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ComunicadoInternoRegistroLeitura</code>.
    * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
    * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
    */
    public String gravar() {
        try {
            if (comunicadoInternoRegistroLeituraVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getComunicadoInternoRegistroLeituraFacade().incluir(comunicadoInternoRegistroLeituraVO);
            } else {
                getFacadeFactory().getComunicadoInternoRegistroLeituraFacade().alterar(comunicadoInternoRegistroLeituraVO);
            }
            setMensagemID("msg_dados_gravados");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    /**
    * Rotina responsavel por executar as consultas disponiveis no JSP ComunicadoInternoRegistroLeituraCons.jsp.
    * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
    * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
    */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getComunicadoInternoRegistroLeituraFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("comunicadoInterno")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getComunicadoInternoRegistroLeituraFacade().consultarPorComunicadoInterno(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("destinatario")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getComunicadoInternoRegistroLeituraFacade().consultarPorDestinatario(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
            definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
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
     * Operação responsável por processar a exclusão um objeto da classe <code>ComunicadoInternoRegistroLeituraVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getComunicadoInternoRegistroLeituraFacade().excluir(comunicadoInternoRegistroLeituraVO);
            setComunicadoInternoRegistroLeituraVO( new ComunicadoInternoRegistroLeituraVO());
            setMensagemID("msg_dados_excluidos");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    public void irPaginaInicial() throws Exception{
        controleConsulta.setPaginaAtual(1);
        this.consultar();
    }

    public void irPaginaAnterior() throws Exception{
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
        this.consultar();
    }

    public void irPaginaPosterior() throws Exception{
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
        this.consultar();
    }

    public void irPaginaFinal() throws Exception{
        controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
        this.consultar();
    }

    /**
    * Rotina responsável por preencher a combo de consulta da telas.
    */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("comunicadoInterno", "Comunicado Interno"));
        itens.add(new SelectItem("destinatario", "Destinatário"));
        return itens;
    }

    /**
    * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
    */
    public String inicializarConsultar() {         removerObjetoMemoria(this);
        setPaginaAtualDeTodas("0/0");
        setListaConsulta(new ArrayList(0));
        definirVisibilidadeLinksNavegacao(0, 0);
        setMensagemID("msg_entre_prmconsulta");
        return "consultar";
    }

    public ComunicadoInternoRegistroLeituraVO getComunicadoInternoRegistroLeituraVO() {
        return comunicadoInternoRegistroLeituraVO;
    }
     
    public void setComunicadoInternoRegistroLeituraVO(ComunicadoInternoRegistroLeituraVO comunicadoInternoRegistroLeituraVO) {
        this.comunicadoInternoRegistroLeituraVO = comunicadoInternoRegistroLeituraVO;
    }
}