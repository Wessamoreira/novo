package controle.administrativo;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * campanhaMarketingForm.jsp campanhaMarketingCons.jsp) com as funcionalidades da classe <code>CampanhaMarketing</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see CampanhaMarketing
 * @see CampanhaMarketingVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.CampanhaMarketingMidiaVO;
import negocio.comuns.administrativo.CampanhaMarketingVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.TipoMidiaCaptacaoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis; @Controller("CampanhaMarketingControle")
@Scope("request")
@Lazy
public class CampanhaMarketingControle extends SuperControle implements Serializable {

    private CampanhaMarketingVO campanhaMarketingVO;
    protected List listaSelectItemTipoMidiaCaptacao;
    private CampanhaMarketingMidiaVO campanhaMarketingMidiaVO;
    private String requisitante_Erro;
    private String responsavel_Erro;
    private String valorConsultaRequisitante;
    private String campoConsultaRequisitante;
    private List listaConsultaRequisitante;

    public CampanhaMarketingControle() throws Exception {
        //obterUsuarioLogado();
        setControleConsulta(new ControleConsulta());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>CampanhaMarketing</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {         removerObjetoMemoria(this);
        setCampanhaMarketingVO(new CampanhaMarketingVO());
        setCampanhaMarketingMidiaVO(new CampanhaMarketingMidiaVO());
        setCampoConsultaRequisitante("");
        setValorConsultaRequisitante("");
        setListaConsultaRequisitante(new ArrayList(0));
        //inicializarRequisitanteCampanhaMarketingUsuarioLogado();
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_entre_dados");
        return "editar";
    }

//    public void inicializarRequisitanteCampanhaMarketingUsuarioLogado() {
//        try {
//            campanhaMarketingVO.setRequisitante(getUsuarioLogado().getPessoa());
//        } catch (Exception e) {
//            //System.out.println("MENSAGEM => " + e.getMessage());;
//        }
//    }
    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>CampanhaMarketing</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() throws Exception {
        CampanhaMarketingVO obj = (CampanhaMarketingVO) context().getExternalContext().getRequestMap().get("campanhaMarketing");
        obj = montarDadosCampanhaMarketingVOCompleta(obj);
        obj.setNovoObj(Boolean.FALSE);
        setCampanhaMarketingVO(obj);
        setCampanhaMarketingMidiaVO(new CampanhaMarketingMidiaVO());
        setCampoConsultaRequisitante("");
        setValorConsultaRequisitante("");
        setListaConsultaRequisitante(new ArrayList(0));
        inicializarListasSelectItemTodosComboBox();
        setMensagemID("msg_dados_editar");
        return "editar";
    }

    public CampanhaMarketingVO montarDadosCampanhaMarketingVOCompleta(CampanhaMarketingVO obj){
        try {
            return getFacadeFactory().getCampanhaMarketingFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return new CampanhaMarketingVO();
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>CampanhaMarketing</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public void gravar() {
        try {
            if (campanhaMarketingVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getCampanhaMarketingFacade().incluir(campanhaMarketingVO, getUsuarioLogado());
            } else {
                getFacadeFactory().getCampanhaMarketingFacade().alterar(campanhaMarketingVO, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP CampanhaMarketingCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                int valorInt = 0;
                if (!getControleConsulta().getValorConsulta().equals("")) {
                    valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                }
                objs = getFacadeFactory().getCampanhaMarketingFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("descricao")) {
                objs = getFacadeFactory().getCampanhaMarketingFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("situacao")) {
                objs = getFacadeFactory().getCampanhaMarketingFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("dataRequisicao")) {
                objs = getFacadeFactory().getCampanhaMarketingFacade().consultarPorDataRequisicao(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(new Date(), 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
                objs = getFacadeFactory().getCampanhaMarketingFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("matricula")) {
                objs = getFacadeFactory().getCampanhaMarketingFacade().consultarPorMatricula(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
                objs = getFacadeFactory().getCampanhaMarketingFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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

    public void consultarRequisitante() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaRequisitante().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaRequisitante(),this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
            }
            if (getCampoConsultaRequisitante().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaRequisitante(),this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
            }
            setListaConsultaRequisitante(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarRequisitante() {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionario");
        this.campanhaMarketingVO.setRequisitante(obj);
        obj = null;
        setCampoConsultaRequisitante("");
        setValorConsultaRequisitante("");
        listaConsultaRequisitante.clear();

    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>CampanhaMarketingVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getCampanhaMarketingFacade().excluir(campanhaMarketingVO);
            setCampanhaMarketingVO(new CampanhaMarketingVO());
            novo();
            setMensagemID("msg_dados_excluidos");
            return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "editar";
        }
    }

    public void adicionarMidia() throws Exception {
        try {
            if (!getCampanhaMarketingVO().getCodigo().equals(0)) {
                campanhaMarketingMidiaVO.setCodigo(getCampanhaMarketingVO().getCodigo());
            }
            if (getCampanhaMarketingMidiaVO().getMidia().getCodigo().intValue() != 0) {
                Integer campoConsulta = getCampanhaMarketingMidiaVO().getMidia().getCodigo();
                TipoMidiaCaptacaoVO midia = getFacadeFactory().getTipoMidiaCaptacaoFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                getCampanhaMarketingMidiaVO().setMidia(midia);
            }

            getCampanhaMarketingVO().adicionarObjCampanhaMarketingMidiaVOs(getCampanhaMarketingMidiaVO());
            this.setCampanhaMarketingMidiaVO(new CampanhaMarketingMidiaVO());
            setMensagemID("msg_dados_adicionados");

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    /* Método responsável por disponibilizar dados de um objeto da classe <code>UnidadeEnsinoCurso</code>
     * para edição pelo usuário.
     */
    public void editarMidia() throws Exception {
        CampanhaMarketingMidiaVO obj = (CampanhaMarketingMidiaVO) context().getExternalContext().getRequestMap().get("campanhaMarketingMidia");
        setCampanhaMarketingMidiaVO(obj);

    }

    /* Método responsável por remover um novo objeto da classe <code>UnidadeEnsinoCurso</code>
     * do objeto <code>unidadeEnsinoVO</code> da classe <code>UnidadeEnsino</code>
     */
    public void removerMidia() throws Exception {
        CampanhaMarketingMidiaVO obj = (CampanhaMarketingMidiaVO) context().getExternalContext().getRequestMap().get("campanhaMarketingMidia");
        getCampanhaMarketingVO().excluirObjCampanhaMarketingMidiaVOs(obj.getMidia().getCodigo());
        setMensagemID("msg_dados_excluidos");

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

    /* Método responsável por inicializar List<SelectItem> de valores do 
     * ComboBox correspondente ao atributo <code>situacao</code>
     */
    public List getListaSelectItemSituacaoCampanhaMarketing() throws Exception {
        List objs = new ArrayList(0);
        Hashtable situacaoCampanhaMarketings = (Hashtable) Dominios.getSituacaoCampanhaMarketing();
        Enumeration keys = situacaoCampanhaMarketings.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoCampanhaMarketings.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

//    public void consultarResponsavelPorChavePrimaria() {
//        try {
//            Integer campoConsulta = campanhaMarketingVO.getResponsavelAutorizacao().getCodigo();
//            PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(campoConsulta);
//            campanhaMarketingVO.getResponsavelAutorizacao().setNome(pessoa.getNome());
//            this.setResponsavel_Erro("");
//            setMensagemID("msg_dados_consultados");
//        } catch (Exception e) {
//            setMensagemID("msg_erro_dadosnaoencontrados");
//            campanhaMarketingVO.getResponsavelAutorizacao().setNome("");
//            campanhaMarketingVO.getResponsavelAutorizacao().setCodigo(0);
//            this.setResponsavel_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
//        }
//    }    
    /**
     * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave primária.
     * Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária da entidade
     * montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarPorMatricula() {
        try {
            String campoConsulta = campanhaMarketingVO.getRequisitante().getMatricula();
            FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorRequisitanteMatricula(campoConsulta,this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            campanhaMarketingVO.setRequisitante(funcionario);
            this.setRequisitante_Erro("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            campanhaMarketingVO.getRequisitante().getPessoa().setNome("");
            campanhaMarketingVO.getRequisitante().setMatricula("");
            this.setRequisitante_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>TipoMidiaCaptacao</code>.
     */
    public void montarListaSelectItemTipoMidiaCaptacao(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarTipoMidiaCaptacaoPorNomeMidia(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                TipoMidiaCaptacaoVO obj = (TipoMidiaCaptacaoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNomeMidia()));
            }
            setListaSelectItemTipoMidiaCaptacao(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>TipoMidiaCaptacao</code>.
     * Buscando todos os objetos correspondentes a entidade <code>TipoMidiaCaptacao</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemTipoMidiaCaptacao() {
        try {
            montarListaSelectItemTipoMidiaCaptacao("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nomeMidia</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarTipoMidiaCaptacaoPorNomeMidia(String nomeMidiaPrm) throws Exception {
        List lista = getFacadeFactory().getTipoMidiaCaptacaoFacade().consultarPorNomeMidia(nomeMidiaPrm, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemTipoMidiaCaptacao();
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("situacao", "Situação"));
        itens.add(new SelectItem("dataRequisicao", "Data Requisição"));
        itens.add(new SelectItem("matricula", "Matrícula Requisitante"));
        itens.add(new SelectItem("nomePessoa", "Nome Requisitante"));
        return itens;
    }

    public boolean isCampoData(){
        if (getControleConsulta().getCampoConsulta().equals("dataRequisicao")){
            return true;
        }
        return false;
    }

    public List getTipoConsultaComboRequisitante() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matricula"));
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

    public CampanhaMarketingMidiaVO getCampanhaMarketingMidiaVO() {
        return campanhaMarketingMidiaVO;
    }

    public void setCampanhaMarketingMidiaVO(CampanhaMarketingMidiaVO campanhaMarketingMidiaVO) {
        this.campanhaMarketingMidiaVO = campanhaMarketingMidiaVO;
    }

    public String getResponsavel_Erro() {
        return responsavel_Erro;
    }

    public void setResponsavel_Erro(String responsavel_Erro) {
        this.responsavel_Erro = responsavel_Erro;
    }

    public String getRequisitante_Erro() {
        return requisitante_Erro;
    }

    public void setRequisitante_Erro(String requisitante_Erro) {
        this.requisitante_Erro = requisitante_Erro;
    }

    public List getListaSelectItemTipoMidiaCaptacao() {
        return (listaSelectItemTipoMidiaCaptacao);
    }

    public void setListaSelectItemTipoMidiaCaptacao(List listaSelectItemTipoMidiaCaptacao) {
        this.listaSelectItemTipoMidiaCaptacao = listaSelectItemTipoMidiaCaptacao;
    }

    public CampanhaMarketingVO getCampanhaMarketingVO() {
        return campanhaMarketingVO;
    }

    public void setCampanhaMarketingVO(CampanhaMarketingVO campanhaMarketingVO) {
        this.campanhaMarketingVO = campanhaMarketingVO;
    }

    public String getCampoConsultaRequisitante() {
        return campoConsultaRequisitante;
    }

    public void setCampoConsultaRequisitante(String campoConsultaRequisitante) {
        this.campoConsultaRequisitante = campoConsultaRequisitante;
    }

    public List getListaConsultaRequisitante() {
        return listaConsultaRequisitante;
    }

    public void setListaConsultaRequisitante(List listaConsultaRequisitante) {
        this.listaConsultaRequisitante = listaConsultaRequisitante;
    }

    public String getValorConsultaRequisitante() {
        return valorConsultaRequisitante;
    }

    public void setValorConsultaRequisitante(String valorConsultaRequisitante) {
        this.valorConsultaRequisitante = valorConsultaRequisitante;
    }

    /**
     * Rotina responsável por alterar a situação de uma campanha de forma a autorizar sua produção e execucação.
     * Usuário deverá possuir acesso a esta ação em seu perfil de acesso.
     */
    public void autorizarCampanha() {
        try {
            getFacadeFactory().getCampanhaMarketingFacade().autorizarCampanha(campanhaMarketingVO, getUsuarioLogado());
            campanhaMarketingVO.setResponsavelAutorizacao(getUsuarioLogadoClone());
            gravar();
            setMensagemID("msg_campanhaMarketing_campanhaAutorizada");


        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void indeferirCampanha() {
        try {
            getFacadeFactory().getCampanhaMarketingFacade().indeferirCampanha(campanhaMarketingVO, getUsuarioLogado());
            campanhaMarketingVO.setResponsavelAutorizacao(getUsuarioLogadoClone());
            gravar();
            setMensagemID("msg_campanhaMarketing_campanhaIndeferida");

        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    /**
     * Rotina responsável alterar a situação de uma campanha de forma a indicar a finalização de uma campanha.
     */
    public void finalizarCampanha() {
        try {
            getFacadeFactory().getCampanhaMarketingFacade().finalizarCampanha(campanhaMarketingVO, getUsuarioLogado());
            campanhaMarketingVO.setResponsavelFinalizacao(getUsuarioLogadoClone());
            gravar();
            setMensagemID("msg_campanhaMarketing_campanhaFinalizada");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        campanhaMarketingVO = null;
        Uteis.liberarListaMemoria(listaSelectItemTipoMidiaCaptacao);
        campanhaMarketingMidiaVO = null;
        requisitante_Erro = null;
        responsavel_Erro = null;
    }
}
