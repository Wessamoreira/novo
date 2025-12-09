/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.PlanoFinanceiroReposicaoVO;
import negocio.comuns.financeiro.TextoPadraoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoTextoPadrao;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 *
 * @author Carlos
 */
@Controller("PlanoFinanceiroReposicaoControle")
@Scope("viewScope")
@Lazy
public class PlanoFinanceiroReposicaoControle extends SuperControle implements Serializable {

    private PlanoFinanceiroReposicaoVO planoFinanceiroReposicaoVO;
    private List listaSelectItemTextoPadraoContrato;

    public PlanoFinanceiroReposicaoControle() {
        setControleConsulta(new ControleConsulta());
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_prmconsulta");
    }

    public void inicializarListasSelectItemTodosComboBox() {
        try {
            montarListaSelectItemTextoPadraoContrato();
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setPaginaAtualDeTodas("0/0");
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroReposicaoCons.xhtml");
    }

    public String novo() {
        removerObjetoMemoria(this);
        setPlanoFinanceiroReposicaoVO(new PlanoFinanceiroReposicaoVO());
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroReposicaoForm.xhtml");
    }

    /**
     * Rotina responsï¿½vel por disponibilizar os dados de um objeto da classe <code>PlanoFinanceiroCurso</code> para
     * alteraï¿½ï¿½o. O objeto desta classe ï¿½ disponibilizado na session da pï¿½gina (request) para que o JSP
     * correspondente possa disponibilizï¿½-lo para ediï¿½ï¿½o.
     */
    public String editar() throws Exception {
        PlanoFinanceiroReposicaoVO obj = (PlanoFinanceiroReposicaoVO) context().getExternalContext().getRequestMap().get("planoFinanceiroReposicaoItens");
        getFacadeFactory().getPlanoFinanceiroReposicaoFacade().carregarDados(obj, NivelMontarDados.TODOS, getUsuarioLogado());
        setPlanoFinanceiroReposicaoVO(obj);
        inicializarListasSelectItemTodosComboBox();
        getPlanoFinanceiroReposicaoVO().setNovoObj(Boolean.FALSE);
        setMensagemID("msg_dados_editar");
        return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroReposicaoForm.xhtml");
    }

    /**
     * Rotina responsï¿½vel por gravar no BD os dados editados de um novo objeto da classe
     * <code>PlanoFinanceiroCurso</code>. Caso o objeto seja novo (ainda nï¿½o gravado no BD) ï¿½ acionado a
     * operaï¿½ï¿½o <code>incluir()</code>. Caso contrï¿½rio ï¿½ acionado o <code>alterar()</code>. Se houver alguma
     * inconsistï¿½ncia o objeto nï¿½o ï¿½ gravado, sendo re-apresentado para o usuï¿½rio juntamente com uma mensagem de
     * erro.
     */
    public String persistir() {
        try {
            if (getPlanoFinanceiroReposicaoVO().getCodigo().intValue() == 0) {
                getFacadeFactory().getPlanoFinanceiroReposicaoFacade().incluir(getPlanoFinanceiroReposicaoVO(), getUsuarioLogado());
            } else {
                getFacadeFactory().getPlanoFinanceiroReposicaoFacade().alterar(getPlanoFinanceiroReposicaoVO(), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroReposicaoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroReposicaoForm.xhtml");
        }
    }

    @Override
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                Integer valorConsInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getPlanoFinanceiroReposicaoFacade().consultaRapidaPorCodigo(valorConsInt, false, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("descricao")) {
                objs = getFacadeFactory().getPlanoFinanceiroReposicaoFacade().consultaRapidaPorDescricao(getControleConsulta().getValorConsulta(), false, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroReposicaoCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroReposicaoCons.xhtml");
        }
    }

    public Boolean getApresentarBotaoAtivar() {
        if (getPlanoFinanceiroReposicaoVO().getSituacao().equals("EL") && !getPlanoFinanceiroReposicaoVO().getSituacao().equals("IN") && !getPlanoFinanceiroReposicaoVO().getSituacao().equals("AT") && !getPlanoFinanceiroReposicaoVO().getCodigo().equals(0)) {
            return true;
        }
        return false;
    }

    public Boolean getApresentarBotaoInativar() {
        if (getPlanoFinanceiroReposicaoVO().getSituacao().equals("AT") && !getPlanoFinanceiroReposicaoVO().getSituacao().equals("EL") && !getPlanoFinanceiroReposicaoVO().getSituacao().equals("IN")) {
            return true;
        }
        return false;
    }

    public void realizarAtivacaoPlanoFinanceiroReposicao() {
        try {
            if (getPlanoFinanceiroReposicaoVO().getCodigo().equals(0)) {
                throw new Exception("Dados ainda não gravados.");
            }
            getFacadeFactory().getPlanoFinanceiroReposicaoFacade().realizarAtivacaoCondicaoPagamento(getPlanoFinanceiroReposicaoVO(), Boolean.TRUE, getUsuarioLogado());
            getPlanoFinanceiroReposicaoVO().setSituacao("AT");
            setMensagemID("msg_dados_ativados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void realizarInativacaoPlanoFinanceiroReposicao() {
        try {
            getFacadeFactory().getPlanoFinanceiroReposicaoFacade().realizarInativacaoCondicaoPagamento(getPlanoFinanceiroReposicaoVO(), Boolean.FALSE, getUsuarioLogado());
            setMensagemID("msg_dados_inativados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }


    /**
     * Operaï¿½ï¿½o responsï¿½vel por processar a exclusï¿½o um objeto da classe <code>PlanoFinanceiroCursoVO</code>
     * Apï¿½s a exclusï¿½o ela automaticamente aciona a rotina para uma nova inclusï¿½o.
     */
    public String excluir() {
        try {
            getFacadeFactory().getPlanoFinanceiroReposicaoFacade().excluir(getPlanoFinanceiroReposicaoVO(), getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroReposicaoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("planoFinanceiroReposicaoForm.xhtml");
        }
    }

    public void montarListaSelectItemTextoPadraoContrato() throws Exception {
        List<TextoPadraoVO> textoPadraoVOMatricula = consultarTextoPadraoPorTipo(TipoTextoPadrao.INCLUSAO_REPOSICAO.getValor());
        setListaSelectItemTextoPadraoContrato(UtilSelectItem.getListaSelectItem(textoPadraoVOMatricula, "codigo", "descricao"));
    }

    @SuppressWarnings("unchecked")
    public List<TextoPadraoVO> consultarTextoPadraoPorTipo(String nomePrm) throws Exception {
        List<TextoPadraoVO> lista = getFacadeFactory().getTextoPadraoFacade().consultarPorTipoNivelComboBox(nomePrm, new UnidadeEnsinoVO(), "", false,  getUsuarioLogado());
        return lista;
    }

    public List getListaSelectItemTextoPadraoContrato() {
        if (listaSelectItemTextoPadraoContrato == null) {
            listaSelectItemTextoPadraoContrato = new ArrayList(0);
        }
        return listaSelectItemTextoPadraoContrato;
    }

    public void setListaSelectItemTextoPadraoContrato(List listaSelectItemTextoPadraoContrato) {
        this.listaSelectItemTextoPadraoContrato = listaSelectItemTextoPadraoContrato;
    }

    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public PlanoFinanceiroReposicaoVO getPlanoFinanceiroReposicaoVO() {
        return planoFinanceiroReposicaoVO;
    }

    public void setPlanoFinanceiroReposicaoVO(PlanoFinanceiroReposicaoVO planoFinanceiroReposicaoVO) {
        this.planoFinanceiroReposicaoVO = planoFinanceiroReposicaoVO;
    }

}
