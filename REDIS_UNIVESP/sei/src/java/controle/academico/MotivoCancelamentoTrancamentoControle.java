package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas turnoForm.jsp turnoCons.jsp) com
 * as funcionalidades da classe <code>Turno</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see Turno
 * @see TurnoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.TipoJustificativaCancelamento;

@Controller("MotivoCancelamentoTrancamentoControle")
@Scope("viewScope")
@Lazy
public class MotivoCancelamentoTrancamentoControle extends SuperControle implements Serializable {

    private MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamentoVO;
    
    public MotivoCancelamentoTrancamentoControle() throws Exception {
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Turno</code> para edição pelo usuário da
     * aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setMotivoCancelamentoTrancamentoVO(new MotivoCancelamentoTrancamentoVO());
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("motivoCancelamentoTrancamentoForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Turno</code> para alteração. O objeto
     * desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() {
        MotivoCancelamentoTrancamentoVO obj = (MotivoCancelamentoTrancamentoVO) context().getExternalContext().getRequestMap().get("motivoCancelamentoTrancamentoItens");
        try {
            obj = getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            obj.setNovoObj(Boolean.FALSE);
            setMotivoCancelamentoTrancamentoVO(obj);
            setMensagemID("msg_dados_editar");
            return Uteis.getCaminhoRedirecionamentoNavegacao("motivoCancelamentoTrancamentoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("motivoCancelamentoTrancamentoCons.xhtml");

        }
    }

    public String ativar() {
        try {
            getMotivoCancelamentoTrancamentoVO().setSituacao("AT");
            getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().alterar(motivoCancelamentoTrancamentoVO, getUsuarioLogado());
            setMensagemID("msg_dados_ativado");
            return Uteis.getCaminhoRedirecionamentoNavegacao("motivoCancelamentoTrancamentoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("motivoCancelamentoTrancamentoForm.xhtml");
        }
    }

    public String inativar() {
        try {
            getMotivoCancelamentoTrancamentoVO().setSituacao("IN");
            getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().alterar(motivoCancelamentoTrancamentoVO, getUsuarioLogado());
            setMensagemID("msg_dados_inativado");
            return Uteis.getCaminhoRedirecionamentoNavegacao("motivoCancelamentoTrancamentoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("motivoCancelamentoTrancamentoForm.xhtml");
        }
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Turno</code>. Caso o
     * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (motivoCancelamentoTrancamentoVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().incluir(motivoCancelamentoTrancamentoVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().alterar(motivoCancelamentoTrancamentoVO, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("motivoCancelamentoTrancamentoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("motivoCancelamentoTrancamentoForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP TurnoCons.jsp. Define o tipo de consulta a ser
     * executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
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
                objs = getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("motivoCancelamentoTrancamentoCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("motivoCancelamentoTrancamentoCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>TurnoVO</code> Após a exclusão ela
     * automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().excluir(motivoCancelamentoTrancamentoVO, getUsuarioLogado());
            setMotivoCancelamentoTrancamentoVO(new MotivoCancelamentoTrancamentoVO());
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("motivoCancelamentoTrancamentoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("motivoCancelamentoTrancamentoForm.xhtml");
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("motivoCancelamentoTrancamentoCons.xhtml");
    }

    public MotivoCancelamentoTrancamentoVO getMotivoCancelamentoTrancamentoVO() {
        return motivoCancelamentoTrancamentoVO;
    }

    public void setMotivoCancelamentoTrancamentoVO(MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamentoVO) {
        this.motivoCancelamentoTrancamentoVO = motivoCancelamentoTrancamentoVO;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        motivoCancelamentoTrancamentoVO = null;

    }

    public List getListaSelectItemTipoJustificativaCancelamento() throws Exception {
        return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoJustificativaCancelamento.class, "valor", "descricao", false);
    }

}
