/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package controle.crm;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.CampanhaVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;


/**
 *
 * @author edigarjr
 */
@Controller("CompromissoAgendaPessoaHorarioControle")
@Scope("session")
@Lazy
public class CompromissoAgendaPessoaHorarioControle extends SuperControle {
    private CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO;
    private String campoConsultarProspect;
    private String valorConsultarProspect;
    private List listaConsultarProspect;
    private String campoConsultarCampanha;
    private String valorConsultarCampanha;
    private List listaConsultarCampanha;

    public CompromissoAgendaPessoaHorarioControle() throws Exception {
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
    }

    /**
    * Rotina responsável por disponibilizar um novo objeto da classe <code>CompromissoAgendaPessoaHorario</code>
    * para edição pelo usuário da aplicação.
    */
    public String novo() {
        setCompromissoAgendaPessoaHorarioVO(new CompromissoAgendaPessoaHorarioVO());
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
        return "editar";
    }

    /**
    * Rotina responsável por disponibilizar os dados de um objeto da classe <code>CompromissoAgendaPessoaHorario</code> para alteração.
    * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
    */
    public String editar() {
        CompromissoAgendaPessoaHorarioVO obj = (CompromissoAgendaPessoaHorarioVO)context().getExternalContext().getRequestMap().get("compromissoAgendaPessoaHorario");
        inicializarAtributosRelacionados(obj);
        obj.setNovoObj(new Boolean(false));
        setCompromissoAgendaPessoaHorarioVO(obj);
        setMensagemID("msg_dados_editar", Uteis.ALERTA);
        return "editar";
    }

    /**
    * Método responsável inicializar objetos relacionados a classe <code>CompromissoAgendaPessoaHorarioVO</code>.
    * Esta inicialização é necessária por exigência da tecnologia JSF, que não trabalha com valores nulos para estes atributos.
    */
    public void inicializarAtributosRelacionados(CompromissoAgendaPessoaHorarioVO obj) {
        if (obj.getProspect() == null) {
            obj.setProspect(new ProspectsVO());
        }
        if (obj.getCampanha() == null) {
            obj.setCampanha(new CampanhaVO());
        }
    }

    /**
    * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>CompromissoAgendaPessoaHorario</code>.
    * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
    * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
    */
    public String persistir() {
        try {
            getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().persistir(compromissoAgendaPessoaHorarioVO, getUsuarioLogado());
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
            return "editar";
        }
    }

    /**
    * Rotina responsavel por executar as consultas disponiveis no JSP CompromissoAgendaPessoaHorarioCons.jsp.
    * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
    * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
    */
    public String consultar() {
        try {
            super.consultar();
            setListaConsulta(getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), false,getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
            return "consultar";
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>CompromissoAgendaPessoaHorarioVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getCompromissoAgendaPessoaHorarioFacade().excluir(compromissoAgendaPessoaHorarioVO, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
            return "editar";
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Campanha</code> por meio dos parametros informados no richmodal.
     * Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos parâmentros informados no richModal
     * montando automaticamente o resultado da consulta para apresentação.
    */
   

    public void selecionarCampanha() throws Exception {
        CampanhaVO obj = (CampanhaVO) context().getExternalContext().getRequestMap().get("campanha");
        if (getMensagemDetalhada().equals("")) {
            this.getCompromissoAgendaPessoaHorarioVO().setCampanha(obj);
        }
        Uteis.liberarListaMemoria(this.getListaConsultarCampanha());
        this.setValorConsultarCampanha(null);
        this.setCampoConsultarCampanha(null);
    }

    public void limparCampoCampanha() {
        this.getCompromissoAgendaPessoaHorarioVO().setCampanha( new CampanhaVO());
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Prospects</code> por meio dos parametros informados no richmodal.
     * Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos parâmentros informados no richModal
     * montando automaticamente o resultado da consulta para apresentação.
    */
    public void consultarProspect() {
        try {
            setListaConsultarProspect(getFacadeFactory().getProspectsFacade().consultar(getValorConsultarProspect(), getUnidadeEnsinoLogado().getCodigo(), getCampoConsultarProspect(), false,getUsuarioLogado(), ""));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (Exception e) {
            getListaConsultarProspect().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void selecionarProspect() throws Exception {
        ProspectsVO obj = (ProspectsVO) context().getExternalContext().getRequestMap().get("prospects");
        if (getMensagemDetalhada().equals("")) {
            this.getCompromissoAgendaPessoaHorarioVO().setProspect(obj);
        }
        Uteis.liberarListaMemoria(this.getListaConsultarProspect());
        this.setValorConsultarProspect(null);
        this.setCampoConsultarProspect(null);
    }

    public void limparCampoProspect() {
        this.getCompromissoAgendaPessoaHorarioVO().setProspect( new ProspectsVO());
    }

    /**
    * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
    */
    public String getMascaraConsulta() {
        return "";
    }

    /**
    * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
    */
    public String inicializarConsultar() {
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
        return "consultar";
    }

    /**
    * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean.
    * Garantindo uma melhor atuação do Garbage Coletor do Java. A mesma é automaticamente
    * quando realiza o logout.
    */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        compromissoAgendaPessoaHorarioVO = null;
    }

    public String getCampoConsultarCampanha() {
        return campoConsultarCampanha;
    }

    public void setCampoConsultarCampanha(String campoConsultarCampanha) {
        this.campoConsultarCampanha = campoConsultarCampanha;
    }

    public String getValorConsultarCampanha() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return valorConsultarCampanha.toUpperCase();
        }
        return valorConsultarCampanha;
    }

    public void setValorConsultarCampanha(String valorConsultarCampanha) {
        this.valorConsultarCampanha = valorConsultarCampanha;
    }

    public List getListaConsultarCampanha() {
        return listaConsultarCampanha;
    }

    public void setListaConsultarCampanha(List listaConsultarCampanha) {
        this.listaConsultarCampanha = listaConsultarCampanha;
    }

    public String getCampoConsultarProspect() {
        return campoConsultarProspect;
    }

    public void setCampoConsultarProspect(String campoConsultarProspect) {
        this.campoConsultarProspect = campoConsultarProspect;
    }

    public String getValorConsultarProspect() {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return valorConsultarProspect.toUpperCase();
        }
        return valorConsultarProspect;
    }

    public void setValorConsultarProspect(String valorConsultarProspect) {
        this.valorConsultarProspect = valorConsultarProspect;
    }

    public List getListaConsultarProspect() {
        return listaConsultarProspect;
    }

    public void setListaConsultarProspect(List listaConsultarProspect) {
        this.listaConsultarProspect = listaConsultarProspect;
    }

    public CompromissoAgendaPessoaHorarioVO getCompromissoAgendaPessoaHorarioVO() {
        if (compromissoAgendaPessoaHorarioVO == null) {;
            compromissoAgendaPessoaHorarioVO = new CompromissoAgendaPessoaHorarioVO();
        }
        return compromissoAgendaPessoaHorarioVO;
    }

    public void setCompromissoAgendaPessoaHorarioVO(CompromissoAgendaPessoaHorarioVO compromissoAgendaPessoaHorarioVO) {
        this.compromissoAgendaPessoaHorarioVO = compromissoAgendaPessoaHorarioVO;
    }
}
