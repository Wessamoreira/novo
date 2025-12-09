package controle.basico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.ConfiguracaoAcademicoControle;
import controle.administrativo.ConfiguracaoGeralSistemaControle;
import controle.arquitetura.ConfiguracaoControleInterface;
import controle.arquitetura.SuperControle;
/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * configuracoesForm.jsp configuracoesCons.jsp) com as funcionalidades da classe <code>Configuracoes</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Configuracoes
 * @see ConfiguracoesVO
 */
import controle.financeiro.CfgCustoAdministrativoControle;
import controle.financeiro.ConfiguracaoFinanceiroControle;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.ConfiguracoesDominio;

@Controller("ConfiguracoesControle")
@Scope("viewScope")
@Lazy
public class ConfiguracoesControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private ConfiguracoesVO configuracoesVO;

    /**
     * Interface <code>ConfiguracoesInterfaceFacade</code> responsável pela interconexão da camada de controle com a camada de negócio.
     * Criando uma independência da camada de controle com relação a tecnologia de persistência dos dados (DesignPatter: Façade).
     */
    public ConfiguracoesControle() throws Exception {
        //obterUsuarioLogado();
        //inicializarControles(new ConfiguracoesVO());
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Metodo que fornece a lista de controladores para serem iniciados manualmente.
     * @return
     * @throws Exception
     */
    private List<Class> getListaDeConfiguracaoControles() throws Exception {
        List<Class> configs = new ArrayList<Class>();
        configs = UtilPropriedadesDoEnum.getListaDeValuesDoEnum(ConfiguracoesDominio.class, "classe");
        return configs;
    }

    /**
     * Metodo que inicia manualmente todas as classes controladoras de configuracao,
     * ele garante que todos os controles estarão iniciados na gravação da nova configuracao.
     */
    public void inicializarControles(ConfiguracoesVO configuracoesVO) {
        try {
            Iterator<Class> configs = getListaDeConfiguracaoControles().iterator();
            ConfiguracaoControleInterface configuracao;
            Class config;
            while (configs.hasNext()) {
                config = configs.next();
                configuracao = (ConfiguracaoControleInterface) context().getExternalContext().getSessionMap().get(config.getSimpleName());
                if (configuracao == null) {
                    configuracao = (ConfiguracaoControleInterface) config.newInstance();
                    configuracao.iniciarControleConfiguracao(configuracoesVO, this);
                    context().getExternalContext().getSessionMap().put(config.getSimpleName(), configuracao);
                } else {
                    configuracao.iniciarControleConfiguracao(configuracoesVO, this);
                }
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<ConfiguracaoControleInterface> getListaComTodosControlesConfiguracoesInstanciados() throws Exception {
        Iterator i = context().getExternalContext().getSessionMap().values().iterator();
        Object sessionObject;
        List<ConfiguracaoControleInterface> configuracoes = new ArrayList<ConfiguracaoControleInterface>();
        while (i.hasNext()) {
            sessionObject = i.next();
            if (sessionObject instanceof ConfiguracaoControleInterface) {
                configuracoes.add((ConfiguracaoControleInterface) sessionObject);
            }
        }
        return configuracoes;
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Configuracoes</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setConfiguracoesVO(new ConfiguracoesVO());
        inicializarControles(getConfiguracoesVO());
        removerControleMemoriaFlash("ConfiguracaoGeralSistemaControle");
        removerControleMemoriaFlash("CfgCustoAdministrativoControle");
        removerControleMemoriaFlash("ConfiguracaoAcademicoControle");
        removerControleMemoriaFlash("ConfiguracaoFinanceiroControle");
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("configuracoesForm");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Configuracoes</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
        try {
            ConfiguracoesVO obj = (ConfiguracoesVO) context().getExternalContext().getRequestMap().get("configuracoesItem");
            context().getExternalContext().getSessionMap().put("configuracoesItem", obj);
            obj.setNovoObj(Boolean.FALSE);
            setConfiguracoesVO(obj);
            inicializarControles(getConfiguracoesVO());
            setMensagemID("msg_dados_editar");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("configuracoesForm");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Configuracoes</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public void gravar() {
        try {
            if (getConfiguracoesVO().isNovoObj().booleanValue()) {
                getFacadeFactory().getConfiguracoesFacade().incluir(getConfiguracoesVO(), getListaComTodosControlesConfiguracoesInstanciados(), getUsuarioLogado());
            } else {
                getFacadeFactory().getConfiguracoesFacade().alterar(getConfiguracoesVO(), getListaComTodosControlesConfiguracoesInstanciados(), this, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String navegarParaConfiguracaoAcademico() {
        try {
        	if(getNomeTelaAtual().contains("configuracoesCons")) {
        		context().getExternalContext().getSessionMap().put("configuracoesItem", (ConfiguracoesVO) context().getExternalContext().getRequestMap().get("configuracoesItem"));
        	}else {
        		context().getExternalContext().getSessionMap().put("configuracoesItem", configuracoesVO);
        	}
        	removerControleMemoriaFlashTela("ConfiguracaoAcademicoControle");
//            ConfiguracaoAcademicoControle objControle = null;
//            objControle = (ConfiguracaoAcademicoControle) context().getExternalContext().getSessionMap().get(ConfiguracaoAcademicoControle.class.getSimpleName());
//            if (objControle == null) {
//                objControle = new ConfiguracaoAcademicoControle();
//                context().getExternalContext().getSessionMap().put(ConfiguracaoAcademicoControle.class.getSimpleName(), objControle);
//            }
//            objControle.setConfiguracoesVO(configuracoesVO);
//            objControle.setEditandoAPartirFormConfiguracores(Boolean.TRUE);
//            objControle.editar();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("configuracoesForm");
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoAcademicoForm");
    }
    
    public String navegarParaConfiguracaoGeralSistema() {
        try {
        	if(getNomeTelaAtual().contains("configuracoesCons")) {
        		context().getExternalContext().getSessionMap().put("configuracoesItem", (ConfiguracoesVO) context().getExternalContext().getRequestMap().get("configuracoesItem"));
        	}else {
        		context().getExternalContext().getSessionMap().put("configuracoesItem", configuracoesVO);
        	}
        	removerControleMemoriaFlashTela("ConfiguracaoGeralSistemaControle");
//            ConfiguracaoGeralSistemaControle objControle = null;
//            objControle = (ConfiguracaoGeralSistemaControle) context().getExternalContext().getSessionMap().get(ConfiguracaoGeralSistemaControle.class.getSimpleName());
//            if (objControle == null) {
//                objControle = new ConfiguracaoGeralSistemaControle();
//                context().getExternalContext().getSessionMap().put(ConfiguracaoGeralSistemaControle.class.getSimpleName(), objControle);
//            }
//            objControle.setConfiguracoesVO(configuracoesVO);
//            objControle.setEditandoAPartirFormConfiguracores(Boolean.TRUE);
//            objControle.editar();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("configuracoesForm");
        }
        return  Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoGeralSistemaForm");
    }
    
    public String navegarParaConfiguracaoFinanceiro() {
        try {
        	if(getNomeTelaAtual().contains("configuracoesCons")) {
        		context().getExternalContext().getSessionMap().put("configuracoesItem", (ConfiguracoesVO) context().getExternalContext().getRequestMap().get("configuracoesItem"));
        	}else {
        		context().getExternalContext().getSessionMap().put("configuracoesItem", configuracoesVO);
        	}
        	removerControleMemoriaFlashTela("ConfiguracaoFinanceiroControle");
//            ConfiguracaoFinanceiroControle objControle = null;
//            objControle = (ConfiguracaoFinanceiroControle) context().getExternalContext().getSessionMap().get(ConfiguracaoFinanceiroControle.class.getSimpleName());
//            if (objControle == null) {
//                objControle = new ConfiguracaoFinanceiroControle();
//                context().getExternalContext().getSessionMap().put(ConfiguracaoFinanceiroControle.class.getSimpleName(), objControle);
//            }
//            objControle.setConfiguracoesVO(configuracoesVO);
//            objControle.setEditandoAPartirFormConfiguracores(Boolean.TRUE);
//            objControle.editar();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("configuracoesForm");
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoFinanceiroForm");
    }    
    
    public String navegarParaConfiguracaoCustoAdm() {
        try {
        	if(getNomeTelaAtual().contains("configuracoesCons")) {
        		context().getExternalContext().getSessionMap().put("configuracoesItem", (ConfiguracoesVO) context().getExternalContext().getRequestMap().get("configuracoesItem"));
        	}else {
        		context().getExternalContext().getSessionMap().put("configuracoesItem", configuracoesVO);
        	}
        	removerControleMemoriaFlashTela("CfgCustoAdministrativoControle");
//            CfgCustoAdministrativoControle objControle = null;
//            objControle = (CfgCustoAdministrativoControle) context().getExternalContext().getSessionMap().get(CfgCustoAdministrativoControle.class.getSimpleName());
//            if (objControle == null) {
//                objControle = new CfgCustoAdministrativoControle();
//                context().getExternalContext().getSessionMap().put(CfgCustoAdministrativoControle.class.getSimpleName(), objControle);
//            }
//            objControle.setConfiguracoesVO(configuracoesVO);
//            objControle.setEditandoAPartirFormConfiguracores(Boolean.TRUE);
//            objControle.editar();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("configuracoesForm");
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("cfgCustoAdministrativoForm");
    }     

    public boolean isExibirBotaoClonar() {
        return !getConfiguracoesVO().isNovoObj();
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP ConfiguracoesCons.jsp.
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
                objs = getFacadeFactory().getConfiguracoesFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getConfiguracoesFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public void clonarConfiguracao() {
        try {
            getConfiguracoesVO().setNovoObj(true);
            getConfiguracoesVO().setNome(getConfiguracoesVO().getNome() + " - Clone");
            getConfiguracoesVO().setCodigo(0);
            getListaComTodosControlesConfiguracoesInstanciados().forEach(ConfiguracaoControleInterface::limparCamposParaClone);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>ConfiguracoesVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public void excluir() {
        try {
            getFacadeFactory().getConfiguracoesFacade().excluir(getConfiguracoesVO(), getListaComTodosControlesConfiguracoesInstanciados(), this, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
//            return Uteis.getCaminhoRedirecionamentoNavegacao("configuracoesForm");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
//            return Uteis.getCaminhoRedirecionamentoNavegacao("configuracoesForm");
        }
    }

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsulta() {
        return "";
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Número"));
        return itens;
    }
    
    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
//        FacesContext context = FacesContext.getCurrentInstance();
//        String paginaAtual = context.getViewRoot().getViewId();
//        if(paginaAtual.lastIndexOf("configuracaoFinanceiroForm.xhtml") > -1){
//        	return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoFinanceiroCons");
//        }else if(paginaAtual.lastIndexOf("configuracaoAcademicoForm.xhtml") > -1){
//        	return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoAcademicoCons");
//        }else if(paginaAtual.lastIndexOf("configuracaoEadForm.xhtml") > -1){
//        	return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoEadCons");
//        }else if(paginaAtual.lastIndexOf("configuracaoAdministrativoForm.xhtml") > -1){
//        	return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoAdministrativoCons");
//        }else if(paginaAtual.lastIndexOf("configuracaoGeralSistemaForm.xhtml") > -1){
//        	return Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoGeralSistemaCons");
//        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("configuracoesCons");
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean.
     * Garantindo uma melhor atuação do Garbage Coletor do Java. A mesma é automaticamente
     * quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
                
    }

    public ConfiguracoesVO getConfiguracoesVO() {
        if (configuracoesVO == null) {
            configuracoesVO = new ConfiguracoesVO();
        }
        return configuracoesVO;
    }

    public void setConfiguracoesVO(ConfiguracoesVO configuracoesVO) {
        this.configuracoesVO = configuracoesVO;
    }
}
