/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.crm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.crm.ArquivoEtapaWorkflowVO;
import negocio.comuns.crm.CursoEtapaWorkflowVO;
import negocio.comuns.crm.EtapaWorkflowAntecedenteVO;
import negocio.comuns.crm.EtapaWorkflowVO;
import negocio.comuns.crm.SituacaoProspectPipelineVO;
import negocio.comuns.crm.SituacaoProspectWorkflowVO;
import negocio.comuns.crm.WorkflowVO;
import negocio.comuns.crm.enumerador.SituacaoProspectPipelineControleEnum;
import negocio.comuns.crm.enumerador.TipoSituacaoWorkflowEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilNavegacao;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;

/**
 *
 * @author MarcoTulio
 */
@Controller("WorkflowControle")
@Scope("viewScope")
@Lazy
public class WorkflowControle extends SuperControle implements Serializable {

    private WorkflowVO workflowVO;
    private EtapaWorkflowVO etapaWorkflowVO;
    private EtapaWorkflowAntecedenteVO etapaWorkflowAntecedenteVO;
    private ArquivoEtapaWorkflowVO arquivoEtapaWorkflowVO;
    private String campoConsultarCurso;
    private String valorConsultarCurso;
    private List listaConsultarCurso;
    private CursoEtapaWorkflowVO cursoEtapaWorkflowVO;
    protected List listaSelectItemCurso;
    private SituacaoProspectWorkflowVO situacaoProspectWorkflowVO;
    protected List listaSelectItemSituacaoProspectPipeline;
    protected List listaSelectItemSituacaoDefinirProspectFinal;
    private Boolean apresentarAbaEtapaWorkflow;
    private Boolean apresentarAbaArquivo;
    private List listaSelectItemEtapasAntecedentes;
    private String controleAbas;
    private Boolean direcao;
    private Boolean mostrarInputEfetivacaoVendaHistorica;
    private TipoSituacaoWorkflowEnum tipoSituacaoWorkflowEnum;
    private String tipoSituacaoWorkflowEnumStr;

    public WorkflowControle() throws Exception {
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Workflow</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {
        setWorkflowVO(new WorkflowVO());
        inicializarListasSelectItemTodosComboBox();
        setSituacaoProspectWorkflowVO(new SituacaoProspectWorkflowVO());
        setCursoEtapaWorkflowVO(new CursoEtapaWorkflowVO());
        setArquivoEtapaWorkflowVO(new ArquivoEtapaWorkflowVO());
        setEtapaWorkflowVO(new EtapaWorkflowVO());
        setControleAbas("workflow");
        try {
            getWorkflowVO().setSituacaoProspectWorkflowVOs(getFacadeFactory().getSituacaoProspectPipelineFacade().consultarSituacaoProspectWorkflowsInicial(Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
        getFacadeFactory().getWorkflowFacade().ordenarListaSituacaoProspectWorkflow(getWorkflowVO().getSituacaoProspectWorkflowVOs());
        setApresentarAbaEtapaWorkflow(false);
        setMensagemID("msg_entre_dados", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("workflowForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Workflow</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar() {
        try {
            WorkflowVO obj = (WorkflowVO) context().getExternalContext().getRequestMap().get("workflowItens");
            obj = getFacadeFactory().getWorkflowFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            obj.setNovoObj(new Boolean(false));
            setWorkflowVO(obj);
            inicializarListasSelectItemTodosComboBox();
            Ordenacao.ordenarLista(getWorkflowVO().getEtapaWorkflowVOs(), "nivelApresentacao");
            setSituacaoProspectWorkflowVO(new SituacaoProspectWorkflowVO());
            setCursoEtapaWorkflowVO(new CursoEtapaWorkflowVO());
            setArquivoEtapaWorkflowVO(new ArquivoEtapaWorkflowVO());
            setEtapaWorkflowVO(new EtapaWorkflowVO());
            setApresentarAbaEtapaWorkflow(false);
            setControleAbas("workflow");
            getListaConsulta().clear();
            getFacadeFactory().getWorkflowFacade().ordenarListaSituacaoProspectWorkflow(getWorkflowVO().getSituacaoProspectWorkflowVOs());
            setMensagemID("msg_dados_editar", Uteis.ALERTA);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("workflowForm.xhtml");
    }

    public void clonar() throws Exception {
        getWorkflowVO().setCodigo(0);
        getWorkflowVO().setNome(getWorkflowVO().getNome() + " - Clone");
        getWorkflowVO().setNovoObj(Boolean.TRUE);
        getWorkflowVO().setTipoSituacaoWorkflow(TipoSituacaoWorkflowEnum.EM_CONSTRUCAO);
        for (EtapaWorkflowVO etapaWorkflow : getWorkflowVO().getEtapaWorkflowVOs()) {
            etapaWorkflow.setWorkflow(getWorkflowVO());
        }
        for (SituacaoProspectWorkflowVO situacaoProspectWorkflow : getWorkflowVO().getSituacaoProspectWorkflowVOs()) {
            situacaoProspectWorkflow.setWorkflow(getWorkflowVO());
        }
        setMensagemID("msg_dados_clonados");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Workflow</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String persistir() {
        try {
            getFacadeFactory().getWorkflowFacade().persistir(getWorkflowVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
            setControleAbas("workflow");
            return Uteis.getCaminhoRedirecionamentoNavegacao("workflowForm.xhtml");
        }
    }

    public void ativar() {
        try {
            getWorkflowVO().setTipoSituacaoWorkflow(TipoSituacaoWorkflowEnum.ATIVO);
            getFacadeFactory().getWorkflowFacade().persistir(workflowVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            setMensagemID("msg_dados_ativado", Uteis.SUCESSO);
        } catch (Exception e) {
            getWorkflowVO().setTipoSituacaoWorkflow(TipoSituacaoWorkflowEnum.EM_CONSTRUCAO);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void cancelar() {
        try {
            getWorkflowVO().setTipoSituacaoWorkflow(TipoSituacaoWorkflowEnum.INATIVO);
            getFacadeFactory().getWorkflowFacade().persistir(workflowVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            setMensagemID("msg_dados_inativados", Uteis.SUCESSO);
        } catch (Exception e) {
            getWorkflowVO().setTipoSituacaoWorkflow(TipoSituacaoWorkflowEnum.ATIVO);
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP WorkflowCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            setListaConsulta(getFacadeFactory().getWorkflowFacade().consultar(getControleConsulta().getValorConsulta(), getControleConsulta().getCampoConsulta(), getTipoSituacaoWorkflowEnum(), false, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("workflowCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>WorkflowVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            if (getWorkflowVO().getCodigo() != 0) {
                getFacadeFactory().getWorkflowFacade().excluir(workflowVO, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            }
            novo();
            setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        } finally {
        	return Uteis.getCaminhoRedirecionamentoNavegacao("workflowForm.xhtml");
        }
    }

    /* Método responsável por adicionar um novo objeto da classe <code>SituacaoProspectWorkflow</code>
     * para o objeto <code>workflowVO</code> da classe <code>Workflow</code>
     */
    public void adicionarSituacaoProspectWorkflow() throws Exception {
        try {
            if (!getWorkflowVO().getCodigo().equals(0)) {
                getSituacaoProspectWorkflowVO().setWorkflow(getWorkflowVO());
            }
            if (getSituacaoProspectWorkflowVO().getSituacaoProspectPipeline().getCodigo().intValue() != 0) {
                Integer campoConsulta = getSituacaoProspectWorkflowVO().getSituacaoProspectPipeline().getCodigo();
                SituacaoProspectPipelineVO situacaoProspectPipeline = getFacadeFactory().getSituacaoProspectPipelineFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                getSituacaoProspectWorkflowVO().setSituacaoProspectPipeline(situacaoProspectPipeline);
            }
            if (getFacadeFactory().getSituacaoProspectWorkflowFacade().verificarExisteSituacao(getWorkflowVO().getSituacaoProspectWorkflowVOs(), getSituacaoProspectWorkflowVO())) {
                getFacadeFactory().getWorkflowFacade().adicionarObjSituacaoProspectWorkflowVOs(getWorkflowVO(), getSituacaoProspectWorkflowVO());
                setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
            } else {
                setMensagemID("msg_controleExistente", Uteis.ALERTA);
            }
            setMostrarInputEfetivacaoVendaHistorica(Boolean.FALSE);
            getFacadeFactory().getWorkflowFacade().ordenarListaSituacaoProspectWorkflow(getWorkflowVO().getSituacaoProspectWorkflowVOs());
            this.setSituacaoProspectWorkflowVO(new SituacaoProspectWorkflowVO());

        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }


    /* Método responsável por remover um novo objeto da classe <code>SituacaoProspectWorkflow</code>
     * do objeto <code>workflowVO</code> da classe <code>Workflow</code>
     */
    public void removerSituacaoProspectWorkflow() {
        try {
            SituacaoProspectWorkflowVO obj = (SituacaoProspectWorkflowVO) context().getExternalContext().getRequestMap().get("situacaoProspectWorkflowItens");
            getFacadeFactory().getWorkflowFacade().excluirObjSituacaoProspectWorkflowVOs(getWorkflowVO(), obj.getSituacaoProspectPipeline().getCodigo());
            setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }

    }

    /* Método responsável por adicionar um novo objeto da classe <code>CursoWorkflow</code>
     * para o objeto <code>workflowVO</code> da classe <code>Workflow</code>
     */
    public void adicionarCursoEtapaWorkflow() {
        try {
            if (!getWorkflowVO().getCodigo().equals(0)) {
                cursoEtapaWorkflowVO.setEtapaWorkflow(getEtapaWorkflowVO());
            }
            getFacadeFactory().getEtapaWorkflowFacade().adicionarObjCursoEtapaWorkflowVOs(getEtapaWorkflowVO(), getCursoEtapaWorkflowVO());
            this.setCursoEtapaWorkflowVO(new CursoEtapaWorkflowVO());
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    /* Método responsável por disponibilizar dados de um objeto da classe <code>CursoWorkflow</code>
     * para edição pelo usuário.
     */
    public void editarCursoWorkflow() {
        try {
            CursoEtapaWorkflowVO obj = (CursoEtapaWorkflowVO) context().getExternalContext().getRequestMap().get("cursoWorkflowItens");
            setCursoEtapaWorkflowVO(obj);
            setMensagemDetalhada("", "");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    /* Método responsável por remover um novo objeto da classe <code>CursoWorkflow</code>
     * do objeto <code>workflowVO</code> da classe <code>Workflow</code>
     */
    public void removerCursoWorkflow() {
        try {
            CursoEtapaWorkflowVO obj = (CursoEtapaWorkflowVO) context().getExternalContext().getRequestMap().get("cursoWorkflowItens");
            getFacadeFactory().getEtapaWorkflowFacade().excluirObjCursoEtapaWorkflowVOs(getEtapaWorkflowVO(), obj.getCurso().getCodigo());
            setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void upLoadArquivo(FileUploadEvent uploadEvent) {
        try {
            if (uploadEvent.getUploadedFile() != null && uploadEvent.getUploadedFile().getSize() > 15360000) {
                throw new Exception("Seu arquivo excede o tamanho estipulado pela Instituição, por favor reduza o arquivo ou divida em partes antes de efetuar a postagem. Obrigado.");
            } else {
                getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getArquivoEtapaWorkflowVO().getArquivo(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.ARQUIVO_TMP, getUsuarioLogado());
                getArquivoEtapaWorkflowVO().getArquivo().setResponsavelUpload(getUsuarioLogadoClone());

            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            uploadEvent = null;
        }
    }

    /* Método responsável por adicionar um novo objeto da classe <code>ArquivoEtapaWorkflow</code>
     * para o objeto <code>workflowVO</code> da classe <code>Workflow</code>
     */
    public void adicionarArquivoEtapaWorkflow() {
        try {
            if (!getWorkflowVO().getCodigo().equals(0)) {
                arquivoEtapaWorkflowVO.setEtapaWorkflow(getEtapaWorkflowVO());
            }
            getFacadeFactory().getEtapaWorkflowFacade().adicionarObjArquivoEtapaWorkflowVOs(getEtapaWorkflowVO(), getArquivoEtapaWorkflowVO());
            this.setArquivoEtapaWorkflowVO(new ArquivoEtapaWorkflowVO());
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    /* Método responsável por disponibilizar dados de um objeto da classe <code>ArquivoEtapaWorkflow</code>
     * para edição pelo usuário.
     */
    public void editarArquivoEtapaWorkflow() {
        try {
            ArquivoEtapaWorkflowVO obj = (ArquivoEtapaWorkflowVO) context().getExternalContext().getRequestMap().get("arquivoWorkflowItens");
            setArquivoEtapaWorkflowVO(obj);
            setMensagemDetalhada("", "");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    /* Método responsável por remover um novo objeto da classe <code>ArquivoEtapaWorkflow</code>
     * do objeto <code>workflowVO</code> da classe <code>Workflow</code>
     */
    public void removerArquivoEtapaWorkflow() {
        try {
            ArquivoEtapaWorkflowVO obj = (ArquivoEtapaWorkflowVO) context().getExternalContext().getRequestMap().get("arquivoWorkflowItens");
            getFacadeFactory().getEtapaWorkflowFacade().excluirObjArquivoEtapaWorkflowVOs(getEtapaWorkflowVO(), obj.getArquivo().getNome());
            setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }

    }

    /* Método responsável por adicionar um novo objeto da classe <code>EtapaWorkflow</code>
     * para o objeto <code>workflowVO</code> da classe <code>Workflow</code>
     */
    public void adicionarEtapaWorkflow() {
        try {
            if (!getWorkflowVO().getCodigo().equals(0)) {
                getEtapaWorkflowVO().setWorkflow(getWorkflowVO());
            }
            if (getEtapaWorkflowVO().getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.INICIAL)) {
                for (EtapaWorkflowVO obj : getWorkflowVO().getEtapaWorkflowVOs()) {
                    if (obj.getSituacaoDefinirProspectFinal().getSituacaoProspectPipeline().getControle().equals(SituacaoProspectPipelineControleEnum.INICIAL) && !obj.getNome().equals(getEtapaWorkflowVO().getNome())) {
                        throw new ConsistirException("Já existe uma etapa com Situacao Prospect Final o qual o controle é INICIAL, por favor escolha outra.");
                    }
                }
            }
            getFacadeFactory().getWorkflowFacade().adicionarObjEtapaWorkflowVOs(getWorkflowVO(), getEtapaWorkflowVO(), getUsuarioLogado());
            montarListaSelectItemEtapaAntecedentes(getWorkflowVO());
            Ordenacao.ordenarLista(getWorkflowVO().getEtapaWorkflowVOs(), "nivelApresentacao");
            this.setEtapaWorkflowVO(new EtapaWorkflowVO());
            setApresentarAbaEtapaWorkflow(false);
            setMensagemID("msg_etapaIncluida_sucesso", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void montarListaSelectItemEtapaAntecedentes() throws Exception {
    	montarListaSelectItemEtapaAntecedentes(getWorkflowVO());
    }
	
    public void realizarAlteracaoScript() {
        try {
            getFacadeFactory().getEtapaWorkflowFacade().alterarScriptCorEtapa(getEtapaWorkflowVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            setApresentarAbaEtapaWorkflow(false);
//            String receptiva = "receptiva";
//            String ativa = "ativo";
//            if (getWorkflowVO().getNome().toLowerCase().contains(receptiva)) {
//                getControladorInteracaoWorkflowNivelAplicacaoControle().setInteracaoWorkflowNivelAplicacaoVONovoProspect(new InteracaoWorkflowVO());
//            } else if (getWorkflowVO().getNome().toLowerCase().contains(ativa)) {
//                getControladorInteracaoWorkflowNivelAplicacaoControle().setInteracaoWorkflowNivelAplicacaoVOLigacaoAtivaSemAgenda(new InteracaoWorkflowVO());
//            }
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    /* Método responsável por disponibilizar dados de um objeto da classe <code>EtapaWorkflow</code>
     * para edição pelo usuário.
     */
    public void editarEtapaWorkflow() {
        try {
            EtapaWorkflowVO obj = (EtapaWorkflowVO) context().getExternalContext().getRequestMap().get("etapaWorkflowItens");
            setEtapaWorkflowVO(obj);
            realizarValidacaoAdicionarEtapa();
            setMensagemDetalhada("", "");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }


    }

    /* Método responsável por remover um novo objeto da classe <code>EtapaWorkflow</code>
     * do objeto <code>workflowVO</code> da classe <code>Workflow</code>
     */
    public void removerEtapaWorkflow() {
        try {
            EtapaWorkflowVO obj = (EtapaWorkflowVO) context().getExternalContext().getRequestMap().get("etapaWorkflowItens");
            getFacadeFactory().getWorkflowFacade().excluirObjEtapaWorkflowVOs(getWorkflowVO(), obj.getNome());
            for (EtapaWorkflowVO object : getWorkflowVO().getEtapaWorkflowVOs()) {
                for (int i = 0; i < object.getEtapaWorkflowAntecedenteVOs().size(); i++) {
                    EtapaWorkflowAntecedenteVO object2 = object.getEtapaWorkflowAntecedenteVOs().get(i);
                    if (object2.getEtapaAntecedente().getNome().equals(obj.getNome())) {
                        object.getEtapaWorkflowAntecedenteVOs().remove(object2);
                    }
                }
            }
            setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }


    }
    /* Método responsável por adicionar um novo objeto da classe <code>EtapaWorkflow</code>
     * para o objeto <code>workflowVO</code> da classe <code>Workflow</code>
     */

    public void adicionarEtapaWorkflowSubordinada() {
        try {
            getFacadeFactory().getEtapaWorkflowFacade().adicionarObjEtapaWorkflowSubordinadaVOs(getEtapaWorkflowVO(), getEtapaWorkflowAntecedenteVO());
            setEtapaWorkflowAntecedenteVO(new EtapaWorkflowAntecedenteVO());
            setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }


    /* Método responsável por remover um novo objeto da classe <code>EtapaWorkflow</code>
     * do objeto <code>workflowVO</code> da classe <code>Workflow</code>
     */
    public void removerEtapaWorkflowSubordinada() {
        try {
            EtapaWorkflowAntecedenteVO obj = (EtapaWorkflowAntecedenteVO) context().getExternalContext().getRequestMap().get("etapaWorkflowAntecedenteItens");
            getFacadeFactory().getEtapaWorkflowFacade().excluirObjEtapaWorkflowVOs(getEtapaWorkflowVO(), obj.getEtapaAntecedente().getNome());
            montarListaSelectItemEtapaAntecedentes(getWorkflowVO());
            setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void montarListaSelectItemEtapaAntecedentes(WorkflowVO obj) throws Exception {
        getListaSelectItemEtapasAntecedentes().clear();
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0 + " " + 0, ""));
        int i = 0;
        for (EtapaWorkflowVO ew : obj.getEtapaWorkflowVOs()) {
            if (!ew.getNome().equals(getEtapaWorkflowVO().getNome()) && ew.getNivelApresentacao() <= getEtapaWorkflowVO().getNivelApresentacao()) {
                i = getEtapaWorkflowVO().getEtapaWorkflowAntecedenteVOs().size();
                if (i == 0) {
                    objs.add(new SelectItem(ew.getNome() + " " + ew.getNivelApresentacao(), ew.getNome().toString()));
                } else {
                    for (EtapaWorkflowAntecedenteVO objeto : getEtapaWorkflowVO().getEtapaWorkflowAntecedenteVOs()) {
                        if (objeto.getEtapaAntecedente().getNome().equals(ew.getNome())) {
                            break;
                        } else if (i == 1) {
                            objs.add(new SelectItem(ew.getNome() + " " + ew.getNivelApresentacao(), ew.getNome().toString()));
                        }
                        i--;
                    }
                }
            }
//            if (!etapaWorkflow.getNome().equals(ew.getNome())) {
//                objs.add(new SelectItem(ew.getNome(), ew.getNome().toString()));
//            }
        }
        setListaSelectItemEtapasAntecedentes(objs);
    }

    public void realizarValidacaoAdicionarEtapa() {
        try {
            setEtapaWorkflowAntecedenteVO(new EtapaWorkflowAntecedenteVO());
            setCursoEtapaWorkflowVO(new CursoEtapaWorkflowVO());
            setArquivoEtapaWorkflowVO(new ArquivoEtapaWorkflowVO());
            montarListaSelectItemEtapaAntecedentes(getWorkflowVO());
            setApresentarAbaEtapaWorkflow(true);
            montarListaSelectItemSituacaoDefinirProspectFinal();
            setControleAbas("etapaWorkflow");
            setMensagemID("msg_entre_dados", Uteis.ALERTA);
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
        }
    }

    public void voltarAbaWorkflow() {
        setApresentarAbaEtapaWorkflow(false);
        this.setEtapaWorkflowVO(new EtapaWorkflowVO());
        setMensagemDetalhada("", "");
    }

//    public void realizarValidacaoAdicionarArquivo() {
//        try {
//            setApresentarAbaArquivo(true);
//            setControleAbas("arquivo");
//        } catch (Exception ex) {
//            setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
//        }
//    }
    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>SituacaoProspectPipeline</code>.
     */
    public void montarListaSelectItemSituacaoProspectPipeline(String prm) throws Exception {
        List resultadoConsulta = consultarSituacaoProspectPipelinePorNome(prm);
        Iterator i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        while (i.hasNext()) {
            SituacaoProspectPipelineVO obj = (SituacaoProspectPipelineVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
        }
        setListaSelectItemSituacaoProspectPipeline(objs);
        Uteis.liberarListaMemoria(resultadoConsulta);
    }

    public void montarListaSelectItemSituacaoDefinirProspectFinal(String prm) throws Exception {
        Iterator i = getWorkflowVO().getSituacaoProspectWorkflowVOs().iterator();
        List objs = new ArrayList(0);
        objs.add(new SelectItem(0, ""));
        while (i.hasNext()) {
            SituacaoProspectWorkflowVO obj = (SituacaoProspectWorkflowVO) i.next();
            objs.add(new SelectItem(obj.getSituacaoProspectPipeline().getCodigo(), obj.getSituacaoProspectPipeline().getNome().toString()));
        }
        setListaSelectItemSituacaoDefinirProspectFinal(objs);
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>SituacaoProspectPipeline</code>.
     * Buscando todos os objetos correspondentes a entidade <code>SituacaoProspectPipeline</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemSituacaoProspectPipeline() {
        try {
            montarListaSelectItemSituacaoProspectPipeline("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }
    }

    public void montarListaSelectItemSituacaoDefinirProspectFinal() {
        try {
            montarListaSelectItemSituacaoDefinirProspectFinal("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());
            ;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarSituacaoProspectPipelinePorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getSituacaoProspectPipelineFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemSituacaoProspectPipeline();
    }

    public void moverSituacaoProspectWorkflowParaCima() {
        setDirecao(Boolean.FALSE);
        SituacaoProspectWorkflowVO obj = (SituacaoProspectWorkflowVO) context().getExternalContext().getRequestMap().get("situacaoProspectWorkflowItens");
        getFacadeFactory().getWorkflowFacade().mudarPosicaoListaSituacaoProspectWorkflow(getWorkflowVO().getSituacaoProspectWorkflowVOs(), obj, getDirecao());
    }

    public void moverSituacaoProspectWorkflowParaBaixo() {
        setDirecao(Boolean.TRUE);
        SituacaoProspectWorkflowVO obj = (SituacaoProspectWorkflowVO) context().getExternalContext().getRequestMap().get("situacaoProspectWorkflowItens");
        getFacadeFactory().getWorkflowFacade().mudarPosicaoListaSituacaoProspectWorkflow(getWorkflowVO().getSituacaoProspectWorkflowVOs(), obj, getDirecao());
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Curso</code> por meio dos parametros informados no richmodal.
     * Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos parâmentros informados no richModal
     * montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarCurso() {
        try {
            setListaConsultarCurso(getFacadeFactory().getCursoFacade().consultar(getCampoConsultarCurso(), getValorConsultarCurso(), 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
        } catch (ConsistirException e) {
            setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
        } catch (Exception e) {
            getListaConsultarCurso().clear();
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void selecionarCurso() {
        try {
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
            if (getMensagemDetalhada().equals("")) {
                this.getArquivoEtapaWorkflowVO().setCurso(obj);
            }
            Uteis.liberarListaMemoria(this.getListaConsultarCurso());
            this.setValorConsultarCurso(null);
            this.setCampoConsultarCurso(null);
            setMensagemDetalhada("", "");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }


    }

    public void selecionarCursoEtapaWorkflow() {
        try {
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
            if (getMensagemDetalhada().equals("")) {
                this.getCursoEtapaWorkflowVO().setCurso(obj);
            }
            Uteis.liberarListaMemoria(this.getListaConsultarCurso());
            this.setValorConsultarCurso(null);
            this.setCampoConsultarCurso(null);
            setMensagemDetalhada("", "");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }


    }

    public void limparCampoCurso() {
        this.getArquivoEtapaWorkflowVO().setCurso(new CursoVO());
    }

    public List getTipoConsultarComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
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
//        limparValorConsulta();
        setMensagemID("msg_entre_prmconsulta", Uteis.ALERTA);
        return Uteis.getCaminhoRedirecionamentoNavegacao("workflowCons.xhtml");
    }

    public boolean getPossibilidadeGravar() {
        if (getWorkflowVO().getTipoSituacaoWorkflow().equals(TipoSituacaoWorkflowEnum.EM_CONSTRUCAO)) {
            return true;
        } else if (getWorkflowVO().isNovoObj().booleanValue()) {
            return true;
        }
        return false;

    }

    public boolean getAtivar() {
        if (getWorkflowVO().isNovoObj().booleanValue()) {
            return false;
        } else if (getWorkflowVO().getTipoSituacaoWorkflow().equals(TipoSituacaoWorkflowEnum.EM_CONSTRUCAO)) {
            return true;
        }

        return false;
    }

    public boolean getCancelar() {
        if (getWorkflowVO().getTipoSituacaoWorkflow().equals(TipoSituacaoWorkflowEnum.ATIVO)) {
            return true;
        }
        return false;
    }

    public boolean getClonar() {
        if (getWorkflowVO().isNovoObj().booleanValue()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean.
     * Garantindo uma melhor atuação do Garbage Coletor do Java. A mesma é automaticamente
     * quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        workflowVO = null;
        etapaWorkflowVO = null;
        arquivoEtapaWorkflowVO = null;
        cursoEtapaWorkflowVO = null;
        Uteis.liberarListaMemoria(listaSelectItemCurso);
        situacaoProspectWorkflowVO = null;
        Uteis.liberarListaMemoria(listaSelectItemSituacaoProspectPipeline);
    }

    public List getListaSelectItemSituacaoProspectPipeline() {
        if (listaSelectItemSituacaoProspectPipeline == null) {
            listaSelectItemSituacaoProspectPipeline = new ArrayList(0);
        }
        return (listaSelectItemSituacaoProspectPipeline);
    }

    public void setListaSelectItemSituacaoProspectPipeline(List listaSelectItemSituacaoProspectPipeline) {
        this.listaSelectItemSituacaoProspectPipeline = listaSelectItemSituacaoProspectPipeline;
    }

    public SituacaoProspectWorkflowVO getSituacaoProspectWorkflowVO() {
        if (situacaoProspectWorkflowVO == null) {
            situacaoProspectWorkflowVO = new SituacaoProspectWorkflowVO();
        }
        return situacaoProspectWorkflowVO;
    }

    public void setSituacaoProspectWorkflowVO(SituacaoProspectWorkflowVO situacaoProspectWorkflowVO) {
        this.situacaoProspectWorkflowVO = situacaoProspectWorkflowVO;
    }

    public List getListaSelectItemCurso() {
        if (listaSelectItemCurso == null) {
            listaSelectItemCurso = new ArrayList(0);
        }
        return (listaSelectItemCurso);
    }

    public void setListaSelectItemCurso(List listaSelectItemCurso) {
        this.listaSelectItemCurso = listaSelectItemCurso;
    }

    public CursoEtapaWorkflowVO getCursoEtapaWorkflowVO() {
        if (cursoEtapaWorkflowVO == null) {
            cursoEtapaWorkflowVO = new CursoEtapaWorkflowVO();
        }
        return cursoEtapaWorkflowVO;
    }

    public void setCursoEtapaWorkflowVO(CursoEtapaWorkflowVO cursoEtapaWorkflowVO) {
        this.cursoEtapaWorkflowVO = cursoEtapaWorkflowVO;
    }

    public String getCampoConsultarCurso() {
        return campoConsultarCurso;
    }

    public void setCampoConsultarCurso(String campoConsultarCurso) {
        this.campoConsultarCurso = campoConsultarCurso;
    }

    public String getValorConsultarCurso() {
        if (valorConsultarCurso == null) {
            valorConsultarCurso = "";
        }
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return valorConsultarCurso.toUpperCase();
        }
        return valorConsultarCurso;
    }

    public void setValorConsultarCurso(String valorConsultarCurso) {
        this.valorConsultarCurso = valorConsultarCurso;
    }

    public List getListaConsultarCurso() {
        return listaConsultarCurso;
    }

    public void setListaConsultarCurso(List listaConsultarCurso) {
        this.listaConsultarCurso = listaConsultarCurso;
    }

    public ArquivoEtapaWorkflowVO getArquivoEtapaWorkflowVO() {
        if (arquivoEtapaWorkflowVO == null) {
            arquivoEtapaWorkflowVO = new ArquivoEtapaWorkflowVO();
        }
        return arquivoEtapaWorkflowVO;
    }

    public void setArquivoEtapaWorkflowVO(ArquivoEtapaWorkflowVO arquivoEtapaWorkflowVO) {
        this.arquivoEtapaWorkflowVO = arquivoEtapaWorkflowVO;
    }

    public EtapaWorkflowVO getEtapaWorkflowVO() {
        if (etapaWorkflowVO == null) {
            etapaWorkflowVO = new EtapaWorkflowVO();
        }
        return etapaWorkflowVO;
    }

    public void setEtapaWorkflowVO(EtapaWorkflowVO etapaWorkflowVO) {
        this.etapaWorkflowVO = etapaWorkflowVO;
    }

    public WorkflowVO getWorkflowVO() {
        if (workflowVO == null) {
            workflowVO = new WorkflowVO();
        }
        return workflowVO;
    }

    public void setWorkflowVO(WorkflowVO workflowVO) {
        this.workflowVO = workflowVO;
    }

    public Boolean getApresentarAbaEtapaWorkflow() {
        return apresentarAbaEtapaWorkflow;
    }

    public void setApresentarAbaEtapaWorkflow(Boolean apresentarAbaEtapaWorkflow) {
        this.apresentarAbaEtapaWorkflow = apresentarAbaEtapaWorkflow;
    }

    public List getListaSelectItemEtapasAntecedentes() {
        if (listaSelectItemEtapasAntecedentes == null) {
            listaSelectItemEtapasAntecedentes = new ArrayList(0);
        }
        return listaSelectItemEtapasAntecedentes;
    }

    public void setListaSelectItemEtapasAntecedentes(List listaSelectItemetapasAntecedentes) {
        this.listaSelectItemEtapasAntecedentes = listaSelectItemetapasAntecedentes;
    }

    public String getControleAbas() {
        if (controleAbas == null) {
            controleAbas = "";
        }
        return controleAbas;
    }

    public void setControleAbas(String controleAbas) {
        this.controleAbas = controleAbas;
    }

    public Boolean getApresentarAbaArquivo() {
        return apresentarAbaArquivo;
    }

    public void setApresentarAbaArquivo(Boolean apresentarAbaArquivo) {
        this.apresentarAbaArquivo = apresentarAbaArquivo;
    }

    public EtapaWorkflowAntecedenteVO getEtapaWorkflowAntecedenteVO() {
        if (etapaWorkflowAntecedenteVO == null) {
            etapaWorkflowAntecedenteVO = new EtapaWorkflowAntecedenteVO();
        }
        return etapaWorkflowAntecedenteVO;
    }

    public void setEtapaWorkflowAntecedenteVO(EtapaWorkflowAntecedenteVO etapaWorkflowAntecedenteVO) {
        this.etapaWorkflowAntecedenteVO = etapaWorkflowAntecedenteVO;
    }

    public List getListaSelectItemSituacaoDefinirProspectFinal() {
        if (listaSelectItemSituacaoDefinirProspectFinal == null) {
            listaSelectItemSituacaoDefinirProspectFinal = new ArrayList(0);
        }
        return listaSelectItemSituacaoDefinirProspectFinal;
    }

    public void setListaSelectItemSituacaoDefinirProspectFinal(List listaSelectItemSituacaoDefinirProspectFinal) {
        this.listaSelectItemSituacaoDefinirProspectFinal = listaSelectItemSituacaoDefinirProspectFinal;
    }

    public Boolean getAntiPenultimaSituacaoProspectWorkflow() {
        SituacaoProspectWorkflowVO obj = (SituacaoProspectWorkflowVO) context().getExternalContext().getRequestMap().get("situacaoProspectWorkflowItens");
        int i;
        for (i = 0; i < getWorkflowVO().getSituacaoProspectWorkflowVOs().size(); i++) {
            if (getWorkflowVO().getSituacaoProspectWorkflowVOs().get(i).getSituacaoProspectPipeline().getCodigo().equals(obj.getSituacaoProspectPipeline().getCodigo())) {
                break;
            }
        }
        if (i == 0) {
            return false;
        } else if (i == getWorkflowVO().getSituacaoProspectWorkflowVOs().size() - 1) {
            return false;
        } else if (i == getWorkflowVO().getSituacaoProspectWorkflowVOs().size() - 2) {
            return false;
        } else if (i == getWorkflowVO().getSituacaoProspectWorkflowVOs().size() - 3) {
            return false;
        }
        return true;
    }

    public Boolean getSegundaSituacaoProspectWorkflow() {
        SituacaoProspectWorkflowVO obj = (SituacaoProspectWorkflowVO) context().getExternalContext().getRequestMap().get("situacaoProspectWorkflowItens");
        int i;
        for (i = 0; i < getWorkflowVO().getSituacaoProspectWorkflowVOs().size(); i++) {
            if (getWorkflowVO().getSituacaoProspectWorkflowVOs().get(i).getSituacaoProspectPipeline().getCodigo().equals(obj.getSituacaoProspectPipeline().getCodigo())) {
                break;
            }
        }
        if (i == 0) {
            return false;
        } else if (i == 1) {
            return false;
        } else if (i == getWorkflowVO().getSituacaoProspectWorkflowVOs().size() - 1) {
            return false;
        } else if (i == getWorkflowVO().getSituacaoProspectWorkflowVOs().size() - 2) {
            return false;
        }
        return true;
    }

    public void mostrarInputEfetivacaoVendaHistorica() {
        setMostrarInputEfetivacaoVendaHistorica(Boolean.TRUE);
    }

    public void naoMostrarInputEfetivacaoVendaHistorica() {
        setMostrarInputEfetivacaoVendaHistorica(Boolean.FALSE);
    }

    public Boolean getDirecao() {
        if (direcao == null) {
            direcao = Boolean.FALSE;
        }
        return direcao;
    }

    public void setDirecao(Boolean direcao) {
        this.direcao = direcao;
    }

    public Boolean getMostrarInputEfetivacaoVendaHistorica() {
        if (mostrarInputEfetivacaoVendaHistorica == null) {
            mostrarInputEfetivacaoVendaHistorica = Boolean.FALSE;
        }
        return mostrarInputEfetivacaoVendaHistorica;
    }

    public void setMostrarInputEfetivacaoVendaHistorica(Boolean mostrarInputEfetivacaoVendaHistorica) {
        this.mostrarInputEfetivacaoVendaHistorica = mostrarInputEfetivacaoVendaHistorica;
    }

    public InteracaoWorkflowNivelAplicacaoControle getControladorInteracaoWorkflowNivelAplicacaoControle() {
        return (InteracaoWorkflowNivelAplicacaoControle) UtilNavegacao.getControlador("InteracaoWorkflowNivelAplicacaoControle");
    }

	public TipoSituacaoWorkflowEnum getTipoSituacaoWorkflowEnum() {
		if (tipoSituacaoWorkflowEnumStr == null || tipoSituacaoWorkflowEnumStr.equals("") || tipoSituacaoWorkflowEnumStr.equals("ATIVO")) {
			tipoSituacaoWorkflowEnum = TipoSituacaoWorkflowEnum.ATIVO;
		}
		if (tipoSituacaoWorkflowEnumStr.equals("INATIVO")) {
			tipoSituacaoWorkflowEnum = TipoSituacaoWorkflowEnum.INATIVO;
		}
		if (tipoSituacaoWorkflowEnumStr.equals("EM_CONSTRUCAO")) {
			tipoSituacaoWorkflowEnum = TipoSituacaoWorkflowEnum.EM_CONSTRUCAO;
		}
		return tipoSituacaoWorkflowEnum;
	}

	public void setTipoSituacaoWorkflowEnum(TipoSituacaoWorkflowEnum tipoSituacaoWorkflowEnum) {
		this.tipoSituacaoWorkflowEnum = tipoSituacaoWorkflowEnum;
	}

	public String getTipoSituacaoWorkflowEnumStr() {
		if (tipoSituacaoWorkflowEnumStr == null) {
			tipoSituacaoWorkflowEnumStr = "";
		}
		return tipoSituacaoWorkflowEnumStr;
	}

	public void setTipoSituacaoWorkflowEnumStr(String tipoSituacaoWorkflowEnumStr) {
		this.tipoSituacaoWorkflowEnumStr = tipoSituacaoWorkflowEnumStr;
	}
}
