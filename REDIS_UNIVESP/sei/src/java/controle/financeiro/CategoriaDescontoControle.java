package controle.financeiro;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas categoriaDespesaForm.jsp
 * categoriaDespesaCons.jsp) com as funcionalidades da classe <code>CategoriaDespesa</code>. Implemtação da camada
 * controle (Backing Bean).
 * 
 * @see SuperControle
 * @see CategoriaDespesa
 * @see CategoriaDespesaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.financeiro.CategoriaDescontoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("CategoriaDescontoControle")
@Scope("viewScope")
@Lazy
public class CategoriaDescontoControle extends SuperControle implements Serializable {

    private CategoriaDescontoVO categoriaDescontoVO;

    public CategoriaDescontoControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    public String novo() throws Exception {
        registrarAtividadeUsuario(getUsuarioLogado(), "CategoriaDescontoControle", "Nova Categoria Desconto", "Novo");
        removerObjetoMemoria(this);
        setCategoriaDescontoVO(new CategoriaDescontoVO());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaDescontoForm");
    }

    public String editar() throws Exception {
        CategoriaDescontoVO obj = (CategoriaDescontoVO) context().getExternalContext().getRequestMap().get("categoriaDescontoItem");
        obj.setNovoObj(Boolean.FALSE);
        setCategoriaDescontoVO(obj);
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaDescontoForm");
    }

    private CategoriaDescontoVO montarCategoriaDescontoVOCompleto(CategoriaDescontoVO obj) {
        try {
            return getFacadeFactory().getCategoriaDescontoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return new CategoriaDescontoVO();
    }

    public String gravar() {
        try {
            if (categoriaDescontoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getCategoriaDescontoFacade().incluir(categoriaDescontoVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getCategoriaDescontoFacade().alterar(categoriaDescontoVO, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaDescontoForm");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaDescontoForm");
        }
    }

    @Override
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            int valorInt = 0;
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (!getControleConsulta().getValorConsulta().equals("")) {
                    valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                }
                objs = getFacadeFactory().getCategoriaDescontoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getCategoriaDescontoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaDescontoCons");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaDescontoCons");
        }
    }

    public String excluir() {
        try {
            getFacadeFactory().getCategoriaDescontoFacade().excluir(categoriaDescontoVO, getUsuarioLogado());
            setCategoriaDescontoVO(new CategoriaDescontoVO());
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaDescontoForm");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaDescontoForm");
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

    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("categoriaDescontoCons");
    }

    public CategoriaDescontoVO getCategoriaDescontoVO() {
        if (categoriaDescontoVO == null) {
            categoriaDescontoVO = new CategoriaDescontoVO();
        }
        return categoriaDescontoVO;
    }

    public void setCategoriaDescontoVO(CategoriaDescontoVO categoriaDescontoVO) {
        this.categoriaDescontoVO = categoriaDescontoVO;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        categoriaDescontoVO = null;
    }


}
