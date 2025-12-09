/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.bancocurriculum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.bancocurriculum.AreaProfissionalVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("AreaProfissionalControle")
@Scope("viewScope")
@Lazy
public class AreaProfissionalControle extends SuperControle implements Serializable {

    private AreaProfissionalVO areaProfissionalVO;

    public AreaProfissionalControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        getControleConsulta().setCampoConsulta("descricaoAreaProfissional");
        setMensagemID("msg_entre_prmconsulta");
    }

    public String novo() throws Exception {
        registrarAtividadeUsuario(getUsuarioLogado(), "AreaProfissionalControle", "Novo Área Profissional", "Novo");
        removerObjetoMemoria(this);
        setAreaProfissionalVO(new AreaProfissionalVO());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("areaProfissionalForm.xhtml");
    }

    public String editar() throws Exception {
        registrarAtividadeUsuario(getUsuarioLogado(), "AreaProfissionalControle", "Inicializando Editar Área Profissional", "Editando");
        AreaProfissionalVO obj = (AreaProfissionalVO) context().getExternalContext().getRequestMap().get("areaProfissionalVOItens");
        setAreaProfissionalVO(getFacadeFactory().getAreaProfissionalFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        registrarAtividadeUsuario(getUsuarioLogado(), "AreaProfissionalControle", "Finalizando Editar Área Profissional", "Editando");
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("areaProfissionalForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novoo <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistên objeto da classe <code>Parceiro</code>. Caso o
     * objeto seja novo (ainda não gravado no BD) é acionado a operaçãcia o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (getAreaProfissionalVO().isNovoObj().booleanValue()) {
                registrarAtividadeUsuario(getUsuarioLogado(), "AreaProfissionalControle", "Inicializando Incluir Área Profissional", "Incluindo");
                getFacadeFactory().getAreaProfissionalFacade().incluir(getAreaProfissionalVO(), getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "AreaProfissionalControle", "Finalizando Incluir Área Profissional", "Incluindo");
            } else {
                registrarAtividadeUsuario(getUsuarioLogado(), "AreaProfissionalControle", "Inicializando Alterar Área Profissional", "Alterando");
                getFacadeFactory().getAreaProfissionalFacade().alterar(getAreaProfissionalVO(), getUsuarioLogado());
                registrarAtividadeUsuario(getUsuarioLogado(), "AreaProfissionalControle", "Finalizando Alterar Área Profissional", "Alterando");
            }
            setMensagemID("msg_dados_gravados");
            getListaConsulta().clear();
            return Uteis.getCaminhoRedirecionamentoNavegacao("areaProfissionalForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("areaProfissionalForm.xhtml");
        }
    }

    public String inativar() {
        try {
            getFacadeFactory().getAreaProfissionalFacade().inativar(getAreaProfissionalVO(), getUsuarioLogado());
            setMensagemID("msg_dados_gravados");
            getListaConsulta().clear();
            return Uteis.getCaminhoRedirecionamentoNavegacao("areaProfissionalForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("areaProfissionalForm.xhtml");
        }
    }

    public String ativar() {
        try {
            getAreaProfissionalVO().setSituacao("AT");
            getFacadeFactory().getAreaProfissionalFacade().alterar(getAreaProfissionalVO(), getUsuarioLogado());
            setMensagemID("msg_dados_gravados");
            getListaConsulta().clear();
            return Uteis.getCaminhoRedirecionamentoNavegacao("areaProfissionalForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("areaProfissionalForm.xhtml");
        }
    }

    public Boolean getAtivo() {
        if (getAreaProfissionalVO().getCodigo().intValue() != 0 && !getAreaProfissionalVO().getSituacao().equals("AT")) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean getInativo() {
        if (getAreaProfissionalVO().getCodigo().intValue() != 0 && getAreaProfissionalVO().getSituacao().equals("AT")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ParceiroCons.jsp. Define o tipo de consulta a ser
     * executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "AreaProfissionalControle", "Inicializando Consultar Área Profissional", "Consultando");
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getAreaProfissionalFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("descricaoAreaProfissional")) {
                objs = getFacadeFactory().getAreaProfissionalFacade().consultarPorDescricaoAreaProfissional(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("situacao")) {
                objs = getFacadeFactory().getAreaProfissionalFacade().consultarPorSituacaoAreaProfissional(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            registrarAtividadeUsuario(getUsuarioLogado(), "AreaProfissionalControle", "Finalizando Consultar Área Profissional", "Consultando");
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("areaProfissionalCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("areaProfissionalCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ParceiroVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "AreaProfissionalControle", "Inicializando Excluir Área Profissional", "Excluindo");
            getFacadeFactory().getAreaProfissionalFacade().excluir(getAreaProfissionalVO(), getUsuarioLogado());
            setAreaProfissionalVO(new AreaProfissionalVO());
            registrarAtividadeUsuario(getUsuarioLogado(), "AreaProfissionalControle", "Finalizando Excluir Área Profissional", "Excluindo");
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("areaProfissionalForm.xhtml"); 
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("areaProfissionalForm.xhtml");
        }
    }

    public List getListaSelectItemTipoAreaProfissional() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        Hashtable tipoAreaProfissional = new Hashtable();
        Enumeration keys = tipoAreaProfissional.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) tipoAreaProfissional.get(value);
            objs.add(new SelectItem(value, label));
        }
        SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
        Collections.sort((List) objs, ordenador);
        return objs;
    }

    public List getListaSelectItemSituacao() throws Exception {
        List objs = new ArrayList(0);
        objs.add(new SelectItem("", ""));
        objs.add(new SelectItem("AT", "Ativo"));
        objs.add(new SelectItem("IN", "Inativo"));
        return objs;
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("descricaoAreaProfissional", "Descrição da Área Profissional"));
        itens.add(new SelectItem("situacao", "Situação"));
        itens.add(new SelectItem("codigo", "Codigo"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        // setPaginaAtualDeTodas("0/0");
        setListaConsulta(new ArrayList(0));
        getControleConsulta().setValorConsulta("");
        getControleConsulta().setCampoConsulta("descricaoAreaProfissional");
        // definirVisibilidadeLinksNavegacao(0, 0);
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("areaProfissionalCons.xhtml");
    }

    public AreaProfissionalVO getAreaProfissionalVO() {
        if (areaProfissionalVO == null) {
            areaProfissionalVO = new AreaProfissionalVO();
        }
        return areaProfissionalVO;
    }

    public void setAreaProfissionalVO(AreaProfissionalVO areaProfissionalVO) {
        this.areaProfissionalVO = areaProfissionalVO;
    }

    public void irPaginaInicial() throws Exception {
        this.consultar();
    }
    
    
}
