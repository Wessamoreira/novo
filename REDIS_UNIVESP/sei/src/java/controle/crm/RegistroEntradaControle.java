package controle.crm;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.crm.ProspectsVO;
import negocio.comuns.crm.RegistroEntradaProspectsVO;
import negocio.comuns.crm.RegistroEntradaVO;
import negocio.comuns.crm.enumerador.TipoUploadEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.crm.RegistroEntrada;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * registroEntradaForm.jsp registroEntradaCons.jsp) com as funcionalidades da classe <code>RegistroEntrada</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see RegistroEntrada
 * @see RegistroEntradaVO
 */
@Controller("RegistroEntradaControle")
@Scope("viewScope")
@Lazy
public class RegistroEntradaControle extends SuperControle {

    private RegistroEntradaVO registroEntradaVO;
    protected List listaSelectItemUnidadeEnsino;
    protected List listaSelectItemCursoEntrada;
    private RegistroEntradaProspectsVO registroEntradaProspectsVO;
    protected List listaSelectItemProspects;
    private List tipoConsulta;
    private List<RegistroEntradaVO> consultaPrincipal;
    private Boolean paginarParaSegundaPagina;
    private SuperControleRelatorio superControleRelatorio;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;
    private List<String> listaErros;

    public RegistroEntradaControle() throws Exception {
        montarListaSelectItemUnidadeEnsino();
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>RegistroEntrada</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {
        getListaErros().clear();
        setRegistroEntradaVO(new RegistroEntradaVO());
        getRegistroEntradaVO().setTipoUpload(TipoUploadEnum.EXCEL);
        inicializarListasSelectItemTodosComboBox();
        getRegistroEntradaVO().setNovoObj(true);
        getControleConsultaOtimizado().getListaConsulta().clear();
        setRegistroEntradaProspectsVO(new RegistroEntradaProspectsVO());
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>RegistroEntrada</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
        try {
            RegistroEntradaVO obj = (RegistroEntradaVO) context().getExternalContext().getRequestMap().get("registroEntradaItens");
            inicializarAtributosRelacionados(obj);
            obj.setNovoObj(false);
            setRegistroEntradaVO(getFacadeFactory().getRegistroEntradaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado()));
            inicializarListasSelectItemTodosComboBox();
            recarregarLista();
            setMensagemID("msg_dados_editar", Uteis.ALERTA);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaForm.xhtml");
    }

    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultarRegistroEntradaProspect();
    }

    public void consultarRegistroEntradaProspect() {
        try {
            getControleConsultaOtimizado().getListaConsulta().clear();
            getControleConsultaOtimizado().setLimitePorPagina(5);
            getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getRegistroEntradaProspectsFacade().consultarRegistroEntradaProspects(getRegistroEntradaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS,
                    getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), getUsuarioLogado()));
            getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getRegistroEntradaProspectsFacade().consultarTotalDeRegistroRegistroEntradaProspects(getRegistroEntradaVO().getCodigo(), getUsuarioLogado()));

        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    /**
     * Método responsável inicializar objetos relacionados a classe <code>RegistroEntradaVO</code>.
     * Esta inicialização é necessária por exigência da tecnologia JSF, que não trabalha com valores nulos para estes atributos.
     */
    public void inicializarAtributosRelacionados(RegistroEntradaVO obj) {
        if (obj.getUnidadeEnsino() == null) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
        }
        if (obj.getCursoEntrada() == null) {
            obj.setCursoEntrada(new CursoVO());
        }
    }

    public void limparCampoCurso() {
        getRegistroEntradaVO().setCursoEntrada(new CursoVO());

    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>RegistroEntrada</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String persistir() {
        try {
            getFacadeFactory().getRegistroEntradaFacade().persistir(getRegistroEntradaVO(), true, getUsuarioLogado());
            getFacadeFactory().getRegistroEntradaProspectsFacade().incluirRegistroEntradaProspectss(getRegistroEntradaVO(), getRegistroEntradaVO().getRegistroEntradaProspectsVOs(), getUsuarioLogado());
            recarregarLista();
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
            getRegistroEntradaVO().setNovoObj(Boolean.FALSE);
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaForm.xhtml");
        }
    }

    public void upLoadArquivo(FileUploadEvent upload) {
        try {
            getListaErros().clear();
            if (getRegistroEntradaVO().getUnidadeEnsino() == null || getRegistroEntradaVO().getUnidadeEnsino().getCodigo() == 0) {
                throw new Exception("O campo UNIDADE ENSINO deve ser informado.");
            } else if (getRegistroEntradaVO().getTipoUpload().equals(TipoUploadEnum.NENHUM)) {
                throw new Exception("O campo TIPO UPLOAD deve ser informado.");
//            } else if (getRegistroEntradaVO().getTipoUpload().equals(TipoUploadEnum.CSV)) {
//                getFacadeFactory().getRegistroEntradaFacade().realizarLeituraArquivoCsv(upload, getRegistroEntradaVO(), getRegistroEntradaVO().getDelimitador(), getUsuarioLogado());
            } else if (getRegistroEntradaVO().getTipoUpload().equals(TipoUploadEnum.EXCEL)) {
                setListaErros(getFacadeFactory().getRegistroEntradaFacade().realizarLeituraArquivoExcel(upload, getRegistroEntradaVO(), getUsuarioLogado()));
            }
            getControleConsultaOtimizado().setListaConsulta(getRegistroEntradaVO().getRegistroEntradaProspectsVOs());
            getControleConsultaOtimizado().setTotalRegistrosEncontrados(getRegistroEntradaVO().getRegistroEntradaProspectsVOs().size());
            getControleConsultaOtimizado().setLimitePorPagina(getRegistroEntradaVO().getRegistroEntradaProspectsVOs().size());
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP RegistroEntradaCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            setConsultaPrincipal(getFacadeFactory().getRegistroEntradaFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), false, getUsuarioLogado(), null));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>RegistroEntradaVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getRegistroEntradaFacade().excluir(registroEntradaVO, true, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaForm.xhtml");
    }

    /* Método responsável por adicionar um novo objeto da classe <code>RegistroEntradaProspects</code>
     * para o objeto <code>registroEntradaVO</code> da classe <code>RegistroEntrada</code>
     */
    public String adicionarRegistroEntradaProspects() throws Exception {
        try {
            if (!getRegistroEntradaVO().getCodigo().equals(0)) {
                registroEntradaProspectsVO.setRegistroEntrada(getRegistroEntradaVO());
            }
            if (getRegistroEntradaProspectsVO().getProspects().getCodigo().intValue() != 0) {
                Integer campoConsulta = getRegistroEntradaProspectsVO().getProspects().getCodigo();
                ProspectsVO prospects = getFacadeFactory().getProspectsFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                getRegistroEntradaProspectsVO().setProspects(prospects);
            }
//            getRegistroEntradaProspectsVO().setRegistroEntrada(getRegistroEntradaVO());
//            getFacadeFactory().getRegistroEntradaFacade().adicionarObjRegistroEntradaProspectsVOs( getRegistroEntradaVO(), getRegistroEntradaProspectsVO());
            this.setRegistroEntradaProspectsVO(new RegistroEntradaProspectsVO());
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaForm.xhtml");
        }
    }

    /* Método responsável por disponibilizar dados de um objeto da classe <code>RegistroEntradaProspects</code>
     * para edição pelo usuário.
     */
    public String editarRegistroEntradaProspects() throws Exception {
        RegistroEntradaProspectsVO obj = (RegistroEntradaProspectsVO) context().getExternalContext().getRequestMap().get("registroEntradaProspectsItens");
        setRegistroEntradaProspectsVO(obj);
        return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaForm.xhtml");
    }

    /* Método responsável por remover um novo objeto da classe <code>RegistroEntradaProspects</code>
     * do objeto <code>registroEntradaVO</code> da classe <code>RegistroEntrada</code>
     */
    public void removerRegistroEntradaProspects() throws Exception {
        RegistroEntradaProspectsVO obj = (RegistroEntradaProspectsVO) context().getExternalContext().getRequestMap().get("registroEntradaProspectsItens");
        getControleConsultaOtimizado().getListaConsulta().remove(obj);
        setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);

    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>Prospects</code>.
     */
    public void montarListaSelectItemProspects(String prm) throws Exception {
        List resultadoConsulta = consultarProspectsPorNome(prm);
        Iterator i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        while (i.hasNext()) {
            ProspectsVO obj = (ProspectsVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
        }
        Uteis.liberarListaMemoria(resultadoConsulta);
        setListaSelectItemProspects(objs);
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>Prospects</code>.
     * Buscando todos os objetos correspondentes a entidade <code>Prospects</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemProspects() {
        try {
            montarListaSelectItemProspects("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarProspectsPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getProspectsFacade().consultarPorNome(nomePrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), "");
        return lista;
    }

    public void imprimirPDF() {
        List prospectsVOs = new ArrayList(0);
        try {
            getFacadeFactory().getRegistroEntradaFacade().preencherListaDeProspectsParaRelatorio(prospectsVOs);
            getSuperControleRelatorio().getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getRegistroEntradaFacade().designIReportRelatorio());
            getSuperControleRelatorio().getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
            getSuperControleRelatorio().getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getRegistroEntradaFacade().caminhoBaseRelatorio());
            getSuperControleRelatorio().getSuperParametroRelVO().setTituloRelatorio("Registro Entrada");
            getSuperControleRelatorio().getSuperParametroRelVO().setListaObjetos(prospectsVOs);
            getSuperControleRelatorio().getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getRegistroEntradaFacade().caminhoBaseRelatorio());
            getSuperControleRelatorio().realizarImpressaoRelatorio();

            setMensagemID("msg_relatorio_ok");


        } catch (Exception ex) {
            getSuperControleRelatorio().setUsarTargetBlank("");
            setMensagemDetalhada("msg_erro", ex.getMessage());
        } finally {
            Uteis.liberarListaMemoria(prospectsVOs);

        }

    }

    public void consultarCurso() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getCursoFacade().consultarPorNome(getValorConsultaCurso(), getRegistroEntradaVO().getUnidadeEnsino().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarCurso() {
        try {
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
            getRegistroEntradaVO().setCursoEntrada(obj);
            if (listaConsulta != null) {
            	listaConsulta.clear();
            }
            this.setValorConsultaCurso("");
            this.setCampoConsultaCurso("");
            setMensagemID("", "");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }

    public void limparConsultaRichModal() {
        getListaConsulta().clear();
    }

    public List getTipoConsultaCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    public List getTipoConsulta() {
        if (tipoConsulta == null) {
            tipoConsulta = new ArrayList(0);
            tipoConsulta.add(new SelectItem("descricao", "Descrição"));
            tipoConsulta.add(new SelectItem("nomeUnidadeEnsino", "Nome Unidade Ensino"));
            tipoConsulta.add(new SelectItem("curso", "Curso"));
        }

        return tipoConsulta;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>UnidadeEnsino</code>.
     */
    public void montarListaSelectItemUnidadeEnsino() throws Exception {
        List resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuarioLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        if (resultadoConsulta.isEmpty()) {
            resultadoConsulta = consultarUnidadeEnsinoPorNome();
        }
        Iterator i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        while (i.hasNext()) {
            UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
        }
        Uteis.liberarListaMemoria(resultadoConsulta);
        setListaSelectItemUnidadeEnsino(objs);
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>.
     * Buscando todos os objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino();
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarUnidadeEnsinoPorNome() throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaUnidadeEnsino();
        //montarListaSelectItemProspects();
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
        getListaConsulta().clear();
        montarListaUnidadeEnsino();
        getConsultaPrincipal().clear();
        setRegistroEntradaVO(new RegistroEntradaVO());
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaCons.xhtml");
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean.
     * Garantindo uma melhor atuação do Garbage Coletor do Java. A mesma é automaticamente 
     * quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        registroEntradaVO = null;
        Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
        Uteis.liberarListaMemoria(listaSelectItemCursoEntrada);
        registroEntradaProspectsVO = null;
        Uteis.liberarListaMemoria(listaSelectItemProspects);
    }

    public List getListaSelectItemProspects() {
        if (listaSelectItemProspects == null) {
            listaSelectItemProspects = new ArrayList(0);
        }
        return (listaSelectItemProspects);
    }

    public void setListaSelectItemProspects(List listaSelectItemProspects) {
        this.listaSelectItemProspects = listaSelectItemProspects;
    }

    public RegistroEntradaProspectsVO getRegistroEntradaProspectsVO() {
        return registroEntradaProspectsVO;
    }

    public void setRegistroEntradaProspectsVO(RegistroEntradaProspectsVO registroEntradaProspectsVO) {
        this.registroEntradaProspectsVO = registroEntradaProspectsVO;
    }

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return (listaSelectItemUnidadeEnsino);
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public RegistroEntradaVO getRegistroEntradaVO() {
        if (registroEntradaVO == null) {
            ;
            registroEntradaVO = new RegistroEntradaVO();
        }
        return registroEntradaVO;
    }

    public void setRegistroEntradaVO(RegistroEntradaVO registroEntradaVO) {
        this.registroEntradaVO = registroEntradaVO;
    }

    public List<RegistroEntradaVO> getConsultaPrincipal() {
        if (consultaPrincipal == null) {
            consultaPrincipal = new ArrayList<RegistroEntradaVO>(0);
        }
        return consultaPrincipal;
    }

    public void setConsultaPrincipal(List<RegistroEntradaVO> consultaPrincipal) {
        this.consultaPrincipal = consultaPrincipal;
    }

    public Boolean getPaginarParaSegundaPagina() {
        if (paginarParaSegundaPagina == null) {
            paginarParaSegundaPagina = false;
        }
        return paginarParaSegundaPagina;
    }

    public void setPaginarParaSegundaPagina(Boolean paginarParaSegundaPagina) {
        this.paginarParaSegundaPagina = paginarParaSegundaPagina;
    }

    public SuperControleRelatorio getSuperControleRelatorio() {
        if (superControleRelatorio == null) {
            superControleRelatorio = new SuperControleRelatorio() {
            };
        }
        return superControleRelatorio;
    }

    public void setSuperControleRelatorio(SuperControleRelatorio superControleRelatorio) {
        this.superControleRelatorio = superControleRelatorio;
    }
 
    public String getCampoConsultaCurso() {
        if (campoConsultaCurso == null) {
            campoConsultaCurso = "";
        }
        return campoConsultaCurso;
    }

    public void setCampoConsultaCurso(String campoConsultaCurso) {
        this.campoConsultaCurso = campoConsultaCurso;
    }

    public String getValorConsultaCurso() {
        if (valorConsultaCurso == null) {
            valorConsultaCurso = "";
        }
        return valorConsultaCurso;
    }

    public void setValorConsultaCurso(String valorConsultaCurso) {
        this.valorConsultaCurso = valorConsultaCurso;
    }

    public List getListaConsultaCurso() {
        if (listaConsultaCurso == null) {
            listaConsultaCurso = new ArrayList(0);
        }
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
    }

    public Boolean getIsNovoObj() {
        if (getRegistroEntradaVO().getCodigo() == 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<String> getListaErros() {
        if (listaErros == null) {
            listaErros = new ArrayList<String>(0);
        }
        return listaErros;
    }

    public void setListaErros(List<String> listaErros) {
        this.listaErros = listaErros;
    }

    public Boolean getApresentarBotaoVisualizarLogErros() {
        if (getListaErros().isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }
    
	public void downloadLayoutPadraoExcel() throws Exception {
		try {
			File arquivo = new File(UteisJSF.getCaminhoWeb() + File.separator+ "resources" + File.separator + "layoutPadraoExcel" + File.separator + "layoutPadraoRegristroEntrada" + File.separator +  "ArquivoExcelLayoutPadrao.xlsx");
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", arquivo.getName());
			request.getSession().setAttribute("pastaBaseArquivo", arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String enviarProspectsParaRdStation() {
        	
		try {
			getFacadeFactory().getRegistroEntradaFacade().validarDados(getRegistroEntradaVO());
	    	if(getConfiguracaoGeralPadraoSistema().getAtivarIntegracaoRdStation()) {
	    		getFacadeFactory().getRegistroEntradaProspectsFacade().enviarProspectsParaRdStation(getRegistroEntradaVO().getRegistroEntradaProspectsVOs(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
	    		setMensagemID("msg_SolicitacaoProcessadaEmSegundoPlano", Uteis.SUCESSO);
	    	} else {
	    		setMensagemDetalhada("msg_IntegracaoRdStationDesativada", Uteis.ERRO);
	    	}			
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
            
       	return Uteis.getCaminhoRedirecionamentoNavegacao("registroEntradaForm.xhtml");
    }
	
	
	/**
	 * Metodo chamado para recarregar a lista de prospects
	 */
	public void recarregarLista() {
		getListaConsulta().clear();
        setRegistroEntradaProspectsVO(new RegistroEntradaProspectsVO());
        setControleConsultaOtimizado(new DataModelo());
        getControleConsultaOtimizado().setListaConsulta(getRegistroEntradaVO().getRegistroEntradaProspectsVOs());
        getControleConsultaOtimizado().setTotalRegistrosEncontrados(getRegistroEntradaVO().getRegistroEntradaProspectsVOs().size());
        getControleConsultaOtimizado().setLimitePorPagina(getRegistroEntradaVO().getRegistroEntradaProspectsVOs().size());
	}
}