package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * tipoCategoriaForm.jsp tipoCategoriaCons.jsp) com as funcionalidades da classe <code>TipoCategoria</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see TipoCategoria
 * @see TipoCategoriaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.TipoCategoriaVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis; @Controller("TipoCategoriaControle")
@Scope("viewScope")
@Lazy
public class TipoCategoriaControle extends SuperControle implements Serializable {

    private TipoCategoriaVO tipoCategoriaVO;
    
    public TipoCategoriaControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>TipoCategoria</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {         removerObjetoMemoria(this);
        setTipoCategoriaVO(new TipoCategoriaVO());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCategoriaForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>TipoCategoria</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
        TipoCategoriaVO obj = (TipoCategoriaVO) context().getExternalContext().getRequestMap().get("tipoCategoriaItens");
        obj = montarDadosTipoCategoriaVOCompleto(obj);
        obj.setNovoObj(Boolean.FALSE);
        setTipoCategoriaVO(obj);
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCategoriaForm.xhtml");
    }

    /**
     * Metodo responsavel por montar os dados do VO
     * @param obj
     * @return TipoCategoriaVO
     */
    public TipoCategoriaVO montarDadosTipoCategoriaVOCompleto(TipoCategoriaVO obj){
        try {
            return getFacadeFactory().getTipoCategoriaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return new TipoCategoriaVO();
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>TipoCategoria</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (getTipoCategoriaVO().isNovoObj().booleanValue()) {
                getFacadeFactory().getTipoCategoriaFacade().incluir(getTipoCategoriaVO(), getUsuarioLogado());
            } else {
                getFacadeFactory().getTipoCategoriaFacade().alterar(getTipoCategoriaVO(), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCategoriaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCategoriaForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP TipoCategoriaCons.jsp.
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
//                objs = getFacadeFactory().getTipoCategoriaFacade().consultarPorCodigo(new Integer(valorInt), true);
//            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getTipoCategoriaFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCategoriaCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCategoriaCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>TipoCategoriaVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getTipoCategoriaFacade().excluir(tipoCategoriaVO, getUsuarioLogado());
            setTipoCategoriaVO(new TipoCategoriaVO());
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCategoriaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCategoriaForm.xhtml");
        }
    }

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
        //itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {         removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("tipoCategoriaCons.xhtml");
    }

    public TipoCategoriaVO getTipoCategoriaVO() {
        if (tipoCategoriaVO == null) {
            tipoCategoriaVO = new TipoCategoriaVO();
        }
        return tipoCategoriaVO;
    }

    public void setTipoCategoriaVO(TipoCategoriaVO tipoCategoriaVO) {
        this.tipoCategoriaVO = tipoCategoriaVO;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        tipoCategoriaVO = null;
    }
}
