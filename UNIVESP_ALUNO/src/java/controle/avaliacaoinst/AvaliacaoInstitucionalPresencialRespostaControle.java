package controle.avaliacaoinst;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jakarta.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalPresencialRespostaVO;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("AvaliacaoInstitucionalPresencialRespostaControle")
@Scope("viewScope")
@Lazy
public class AvaliacaoInstitucionalPresencialRespostaControle extends SuperControle {

	private static final long serialVersionUID = -3322625942514839780L;

	private AvaliacaoInstitucionalPresencialRespostaVO avaliacaoInstitucionalPresencialRespostaVO;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private Date dataIni;
    private Date dataFim;
    private String valorConsultaAvaliacaoInstitucional;
    private String campoConsultaAvaliacaoInstitucional;
    private List<AvaliacaoInstitucionalVO> listaConsultaAvaliacaoInstitucional;
    private String valorConsultaCurso;
    private String campoConsultaCurso;
    private List<CursoVO> listaConsultaCurso;
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    private List<TurmaVO> listaConsultaTurma;
    private String valorConsultaDisciplina;
    private String campoConsultaDisciplina;
    private List<DisciplinaVO> listaConsultaDisciplina;
    private String valorConsultaProfessor;
    private String campoConsultaProfessor;
    private List<FuncionarioVO> listaConsultaProfessor;

    public AvaliacaoInstitucionalPresencialRespostaControle() throws Exception {
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>AvaliacaoInstitucional</code> para edição
     * pelo usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setAvaliacaoInstitucionalPresencialRespostaVO(new AvaliacaoInstitucionalPresencialRespostaVO());
        inicializarListasSelectItemTodosComboBox();
        incializarResponsavel();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoInstitucionalPresencialRespostaForm.xhtml");
    }

    public void incializarResponsavel() {
        try {
            getAvaliacaoInstitucionalPresencialRespostaVO().setResponsavel(getUsuarioLogadoClone());
        } catch (Exception e) {
        }
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>AvaliacaoInstitucional</code> para
     * alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente
     * possa disponibilizá-lo para edição.
     */
    public String editar() {
        try {
            AvaliacaoInstitucionalPresencialRespostaVO obj = (AvaliacaoInstitucionalPresencialRespostaVO) context().getExternalContext().getRequestMap().get("avaliacaoInstitucionalPresencialRespostaItens");
            obj.setNovoObj(Boolean.FALSE);
            getFacadeFactory().getAvaliacaoInstitucionalPresencialRespostaFacade().carregarDados(obj, getUsuarioLogado());
            setAvaliacaoInstitucionalPresencialRespostaVO(obj);
            inicializarListasSelectItemTodosComboBox();
            incializarResponsavel();
            setMensagemID("msg_dados_editar");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoInstitucionalPresencialRespostaForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe
     * <code>AvaliacaoInstitucional</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação
     * <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o
     * objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
            if (avaliacaoInstitucionalPresencialRespostaVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getAvaliacaoInstitucionalPresencialRespostaFacade().incluir(avaliacaoInstitucionalPresencialRespostaVO,getUsuarioLogado());
            } else {
                avaliacaoInstitucionalPresencialRespostaVO.setDataAlteracao(new Date());
                incializarResponsavel();
                getFacadeFactory().getAvaliacaoInstitucionalPresencialRespostaFacade().alterar(avaliacaoInstitucionalPresencialRespostaVO, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoInstitucionalPresencialRespostaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoInstitucionalPresencialRespostaForm.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>AvaliacaoInstitucionalVO</code> Após a
     * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getAvaliacaoInstitucionalPresencialRespostaFacade().excluir(avaliacaoInstitucionalPresencialRespostaVO, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoInstitucionalPresencialRespostaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoInstitucionalPresencialRespostaForm.xhtml");
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP AvaliacaoInstitucionalCons.jsp. Define o tipo de
     * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    @Override
    public String consultar() {
        try {
            super.consultar();
            List<AvaliacaoInstitucionalPresencialRespostaVO> objs = new ArrayList<>(0);
            if (getControleConsulta().getCampoConsulta().equals("avaliacaoInstitucional")) {
                objs = getFacadeFactory().getAvaliacaoInstitucionalPresencialRespostaFacade().consultaRapidaPorAvaliacaoInstitucional(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
                objs = getFacadeFactory().getAvaliacaoInstitucionalPresencialRespostaFacade().consultaRapidaPorUnidadeEnsino(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("curso")) {
                objs = getFacadeFactory().getAvaliacaoInstitucionalPresencialRespostaFacade().consultaRapidaPorCurso(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("turma")) {
                objs = getFacadeFactory().getAvaliacaoInstitucionalPresencialRespostaFacade().consultaRapidaPorTurma(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("disciplina")) {
                objs = getFacadeFactory().getAvaliacaoInstitucionalPresencialRespostaFacade().consultaRapidaPorDisciplina(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("professor")) {
                objs = getFacadeFactory().getAvaliacaoInstitucionalPresencialRespostaFacade().consultaRapidaPorProfessor(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("dataCriacao")) {
                objs = getFacadeFactory().getAvaliacaoInstitucionalPresencialRespostaFacade().consultaRapidaPorDataCriacao(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(getControleConsulta().getDataFim(), 23, 59, 59), true,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoInstitucionalPresencialRespostaCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoInstitucionalPresencialRespostaCons.xhtml");
        }
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<>(0);
        itens.add(new SelectItem("avaliacaoInstitucional", "Avaliação Institucional"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        itens.add(new SelectItem("curso", "Curso"));
        itens.add(new SelectItem("turma", "Turma"));
        itens.add(new SelectItem("disciplina", "Disciplina"));
        itens.add(new SelectItem("professor", "Professor"));
        itens.add(new SelectItem("dataCriacao", "Data Criação"));

        return itens;
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP AvaliacaoInstitucionalCons.jsp. Define o tipo de
     * consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
     * resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public void consultarAvaliacaoInstitucional() {
        try {
            super.consultar();
            List<AvaliacaoInstitucionalVO> objs = new ArrayList<>(0);
            if (getCampoConsultaAvaliacaoInstitucional().equals("codigo")) {
                if (getValorConsultaAvaliacaoInstitucional().equals("")) {
                    setValorConsultaAvaliacaoInstitucional("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaAvaliacaoInstitucional());
                objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarPorCodigo(new Integer(valorInt), true, 10, null, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaAvaliacaoInstitucional().equals("data")) {
                objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultarPorData(Uteis.getDateTime(getDataIni(), 0, 0, 0), Uteis.getDateTime(getDataFim(), 23, 59, 59), null, 10, null, true,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaAvaliacaoInstitucional().equals("nome")) {
                objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorNome(getValorConsultaAvaliacaoInstitucional(), null, 10, null, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaAvaliacaoInstitucional().equals("nomeUnidadeEnsino")) {
                objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaAvaliacaoInstitucional(), null, 10, null, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaAvaliacaoInstitucional().equals("nomeCurso")) {
                objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorCurso(getValorConsultaAvaliacaoInstitucional(), null, 10, null, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaAvaliacaoInstitucional().equals("nomeTurma")) {
                objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorTurma(getValorConsultaAvaliacaoInstitucional(), null, 10, null, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaAvaliacaoInstitucional().equals("nomeDisciplina")) {
                objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorDisciplina(getValorConsultaAvaliacaoInstitucional(), null, 10, null, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
//            if (getCampoConsultaAvaliacaoInstitucional().equals("questionario")) {
//                objs = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorQuestionario(getValorConsultaAvaliacaoInstitucional(), true, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
//            }
            setListaConsultaAvaliacaoInstitucional(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAvaliacaoInstitucional(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaComboAvaliacaoInstitucional() {
        List<SelectItem> itens = new ArrayList<>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("data", "Data"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        itens.add(new SelectItem("nomeTurma", "Turma"));
        itens.add(new SelectItem("nomeDisciplina", "Disciplina"));
//        itens.add(new SelectItem("questionario", "Questinário"));
        return itens;
    }

    public void selecionarAvaliacaoInstitucional() {
        try {
            limparDadosCurso();
            limparDadosTurma();
            limparDadosProfessor();
            AvaliacaoInstitucionalVO obj = (AvaliacaoInstitucionalVO) context().getExternalContext().getRequestMap().get("avaliacaoInstitucionalItens");
            obj.setQuestionarioVO(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(obj.getQuestionarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            getAvaliacaoInstitucionalPresencialRespostaVO().setAvaliacaoInstitucional(obj);
            if (obj.getCurso().getCodigo().equals(0) && !obj.getTurma().getCodigo().equals(0)) {
                getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurma(), NivelMontarDados.BASICO, getUsuarioLogado());
                obj.setCurso(obj.getTurma().getCurso());
            }
            if (obj.getUnidadeEnsino().getCodigo().equals(0) && !obj.getTurma().getCodigo().equals(0)) {
                if (obj.getTurma().getUnidadeEnsino().getCodigo().equals(0)) {
                    getFacadeFactory().getTurmaFacade().carregarDados(obj.getTurma(), NivelMontarDados.BASICO, getUsuarioLogado());
                }
                obj.setUnidadeEnsino(obj.getTurma().getUnidadeEnsino());
            }
            getAvaliacaoInstitucionalPresencialRespostaVO().setUnidadeEnsino(obj.getUnidadeEnsino());
            getAvaliacaoInstitucionalPresencialRespostaVO().setCurso(obj.getCurso());
            getAvaliacaoInstitucionalPresencialRespostaVO().setTurma(obj.getTurma());
            getAvaliacaoInstitucionalPresencialRespostaVO().setDisciplina(obj.getDisciplina());
            if (obj.getUnidadeEnsino().getCodigo().equals(0) && !obj.getCurso().getCodigo().equals(0) && obj.getTurma().getCodigo().equals(0)) {
                montarListaSelectItemUnidadeEnsinoPorCurso();
                if (getListaSelectItemUnidadeEnsino().size() == 1) {
                    obj.getUnidadeEnsino().setCodigo((Integer) getListaSelectItemUnidadeEnsino().get(0).getValue());
                }
            }
            getListaConsultaAvaliacaoInstitucional().clear();
            this.setValorConsultaAvaliacaoInstitucional("");
            this.setCampoConsultaAvaliacaoInstitucional("");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void limparDadosAvaliacaoInstitucional() {
        try {
            getListaConsultaAvaliacaoInstitucional().clear();
            setValorConsultaAvaliacaoInstitucional(null);
            getAvaliacaoInstitucionalPresencialRespostaVO().setAvaliacaoInstitucional(null);
            inicializarListasSelectItemTodosComboBox();
            limparDadosCursoTurmaDisciplinaProfessor();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarCurso() {
        try {
            setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultar(getCampoConsultaCurso(), getValorConsultaCurso(), getAvaliacaoInstitucionalPresencialRespostaVO().getUnidadeEnsino().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaComboCurso() {
        List<SelectItem> itens = new ArrayList<>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public void selecionarCurso() {
        try {
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
            getAvaliacaoInstitucionalPresencialRespostaVO().setCurso(obj);
            getListaConsultaCurso().clear();
            this.setValorConsultaCurso("");
            this.setCampoConsultaCurso("");
            if (!getAvaliacaoInstitucionalPresencialRespostaVO().getAvaliacaoInstitucional().getTurma().getCodigo().equals(0)) {
                limparDadosTurma();
            }
            if (!getAvaliacaoInstitucionalPresencialRespostaVO().getAvaliacaoInstitucional().getDisciplina().getCodigo().equals(0)) {
                limparDadosDisciplina();
            }
            limparDadosProfessor();
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void limparDadosCurso() {
        try {
            getListaConsultaCurso().clear();
            setValorConsultaCurso(null);
            getAvaliacaoInstitucionalPresencialRespostaVO().setCurso(null);
            
            limparDadosDisciplina();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarTurma() {
        try {
            super.consultar();
            List<TurmaVO> objs = new ArrayList<>(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCursoDisciplina(getValorConsultaTurma(), getAvaliacaoInstitucionalPresencialRespostaVO().getCurso().getCodigo(), getAvaliacaoInstitucionalPresencialRespostaVO().getDisciplina().getCodigo(), getAvaliacaoInstitucionalPresencialRespostaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaComboTurma() {
        List<SelectItem> itens = new ArrayList<>(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        return itens;
    }

    public void selecionarTurma() {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
            getAvaliacaoInstitucionalPresencialRespostaVO().setTurma(obj);
            getAvaliacaoInstitucionalPresencialRespostaVO().setCurso(obj.getCurso());
            if (getAvaliacaoInstitucionalPresencialRespostaVO().getUnidadeEnsino().getCodigo().equals(0)) {
                getAvaliacaoInstitucionalPresencialRespostaVO().setUnidadeEnsino(obj.getUnidadeEnsino());
            }
            if (!getAvaliacaoInstitucionalPresencialRespostaVO().getAvaliacaoInstitucional().getDisciplina().getCodigo().equals(0)) {
                limparDadosDisciplina();
            }
            limparDadosProfessor();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparDadosTurma() {
        try {
            getListaConsultaTurma().clear();
            setValorConsultaTurma(null);
            setCampoConsultaTurma(null);
            getAvaliacaoInstitucionalPresencialRespostaVO().setTurma(null);
            if (getListaSelectItemUnidadeEnsino().size() > 1 && getAvaliacaoInstitucionalPresencialRespostaVO().getCurso().getCodigo().equals(0)) {
                getAvaliacaoInstitucionalPresencialRespostaVO().setCodigo(0);
            }
            if (getAvaliacaoInstitucionalPresencialRespostaVO().getAvaliacaoInstitucional().getDisciplina().getCodigo().equals(0)) {
                limparDadosDisciplina();
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarDisciplina() {
        try {
            List<DisciplinaVO> objs = new ArrayList<>(0);
            if (getCampoConsultaDisciplina().equals("codigo")) {
                if (getValorConsultaDisciplina().equals("")) {
                    setValorConsultaDisciplina("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaDisciplina());
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigoCursoTurma(new Integer(valorInt), getAvaliacaoInstitucionalPresencialRespostaVO().getCurso().getCodigo(), getAvaliacaoInstitucionalPresencialRespostaVO().getTurma().getCodigo(), getAvaliacaoInstitucionalPresencialRespostaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaDisciplina().equals("nome")) {
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeCursoTurma(getValorConsultaDisciplina(), getAvaliacaoInstitucionalPresencialRespostaVO().getCurso().getCodigo(), getAvaliacaoInstitucionalPresencialRespostaVO().getTurma().getCodigo(), getAvaliacaoInstitucionalPresencialRespostaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaDisciplina(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaDisciplina(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaComboDisciplina() {
        List<SelectItem> itens = new ArrayList<>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
//        itens.add(new SelectItem("areaConhecimento", "Área de Conhecimento"));
        return itens;
    }

    public void selecionarDisciplina() {
        try {
            DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
            getAvaliacaoInstitucionalPresencialRespostaVO().setDisciplina(obj);
            getListaConsultaDisciplina().clear();
            setValorConsultaDisciplina(null);
            setCampoConsultaDisciplina(null);
            limparDadosProfessor();
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void limparDadosDisciplina() {
        try {
            getListaConsultaDisciplina().clear();
            setValorConsultaDisciplina(null);
            setCampoConsultaDisciplina(null);
            getAvaliacaoInstitucionalPresencialRespostaVO().setDisciplina(null);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarProfessor() {
        try {
            super.consultar();
            List<FuncionarioVO> objs = new ArrayList<>(0);
            if (getCampoConsultaProfessor().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeProfessorTitularDisciplinaTurma(getValorConsultaProfessor(), getAvaliacaoInstitucionalPresencialRespostaVO().getUnidadeEnsino().getCodigo(), getAvaliacaoInstitucionalPresencialRespostaVO().getCurso().getCodigo(), getAvaliacaoInstitucionalPresencialRespostaVO().getTurma().getCodigo(), getAvaliacaoInstitucionalPresencialRespostaVO().getDisciplina().getCodigo(), "", "", null, true, false, getUsuarioLogado());
            }
            if (getCampoConsultaProfessor().equals("cpf")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCpfProfessorTitularDisciplinaTurma(getValorConsultaProfessor(), getAvaliacaoInstitucionalPresencialRespostaVO().getUnidadeEnsino().getCodigo(), getAvaliacaoInstitucionalPresencialRespostaVO().getCurso().getCodigo(), getAvaliacaoInstitucionalPresencialRespostaVO().getTurma().getCodigo(), getAvaliacaoInstitucionalPresencialRespostaVO().getDisciplina().getCodigo(), "", "", null, true, false, getUsuarioLogado());
            }
            setListaConsultaProfessor(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaComboProfessor() {
        List<SelectItem> itens = new ArrayList<>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("cpf", "CPF"));
        return itens;
    }

    public void selecionarProfessor() {
        try {
            FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("professorItens");
            getAvaliacaoInstitucionalPresencialRespostaVO().setProfessor(obj.getPessoa());
            getListaConsultaProfessor().clear();
            setValorConsultaProfessor(null);
            setCampoConsultaProfessor(null);
//            setMatriculaProfessor(getFacadeFactory().getFuncionarioFacade().consultarMatriculaFuncionarioPorCodigoPessoa(obj.getCodigo(), 0));
        } catch (Exception e) {
        }
    }

    public void limparDadosProfessor() {
        try {
            getListaConsultaProfessor().clear();
            setValorConsultaProfessor(null);
            setCampoConsultaProfessor(null);
            getAvaliacaoInstitucionalPresencialRespostaVO().setProfessor(null);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparDadosCursoTurmaDisciplinaProfessor() {
        getAvaliacaoInstitucionalPresencialRespostaVO().setCurso(null);
        getAvaliacaoInstitucionalPresencialRespostaVO().setTurma(null);
        getAvaliacaoInstitucionalPresencialRespostaVO().setDisciplina(null);
        getAvaliacaoInstitucionalPresencialRespostaVO().setProfessor(null);
        getAvaliacaoInstitucionalPresencialRespostaVO().getUnidadeEnsino().setCodigo(0);
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>QuestionarioUnidadeEnsino</code>.
     */
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List<UnidadeEnsinoVO> resultadoConsulta = null;
        Iterator<UnidadeEnsinoVO> i = null;
        try {
            if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
                getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome().toString()));
                getAvaliacaoInstitucionalPresencialRespostaVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
                return;
            }
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            Uteis.liberarListaMemoria(resultadoConsulta);
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>QuestionarioUnidadeEnsino</code>. Buscando
     * todos os objetos correspondentes a entidade <code>Questionario</code>. Esta rotina não recebe parâmetros para
     * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
     * requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
        	setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }

    public void montarListaSelectItemUnidadeEnsinoPorCurso() {
        List<UnidadeEnsinoVO> resultadoConsulta = null;
        Iterator<UnidadeEnsinoVO> i = null;
        try {
            resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigoCurso(getAvaliacaoInstitucionalPresencialRespostaVO().getCurso().getCodigo(), getAvaliacaoInstitucionalPresencialRespostaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            Uteis.liberarListaMemoria(resultadoConsulta);
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
        	setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String descricaoPrm) throws Exception {
        return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(descricaoPrm, this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List<QuestionarioVO> consultarQuestionarioPorDescricao(String descricaoPrm) throws Exception {
        return getFacadeFactory().getQuestionarioFacade().consultarPorEscopoSituacaoDiferenteEmConstrucao(descricaoPrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemUnidadeEnsino();
    }

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsulta() {
        if (getControleConsulta().getCampoConsulta().equals("data") || getControleConsulta().getCampoConsulta().equals("dataInicio") || getControleConsulta().getCampoConsulta().equals("dataFinal")) {
            return "return mascara(this.form,'form:valorConsulta','99/99/9999',event);";
        }
        return "";
    }

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsultaAvaliacaoInstitucional() {
        if (getCampoConsultaAvaliacaoInstitucional().equals("data") || getCampoConsultaAvaliacaoInstitucional().equals("dataInicio") || getCampoConsultaAvaliacaoInstitucional().equals("dataFinal")) {
            return "return mascara(this.form,'formAvaliacaoInstitucional:valorConsulta','99/99/9999',event);";
        }
        return "";
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList<>(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("avaliacaoInstitucionalPresencialRespostaCons.xhtml");
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
     * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
     */
    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        avaliacaoInstitucionalPresencialRespostaVO = null;
    }

    public void realizarAtivacaoSituacaoAvaliacao() {
        try {
            getFacadeFactory().getAvaliacaoInstitucionalFacade().alterarSituacaoAvaliacao(getAvaliacaoInstitucionalPresencialRespostaVO().getCodigo(), "AT",null, getUsuarioLogado());
            setMensagemID("msg_dados_ativado");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void realizarFinalizacaoSituacaoAvaliacao() {
        try {
            getFacadeFactory().getAvaliacaoInstitucionalFacade().alterarSituacaoAvaliacao(getAvaliacaoInstitucionalPresencialRespostaVO().getCodigo(), "FI",null, getUsuarioLogado());
            setMensagemID("msg_dados_finalizado");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public boolean getIsFiltroData() {
        if (getControleConsulta().getCampoConsulta().equals("dataCriacao")) {
            return true;
        }
        return false;
    }

    public boolean getIsFiltroNaoData() {
        if (!getControleConsulta().getCampoConsulta().equals("dataCriacao")) {
            return true;
        }
        return false;
    }

    public boolean getIsFiltroDataAvaliacaoInstitucional() {
        if (getCampoConsultaAvaliacaoInstitucional().equals("data") || getCampoConsultaAvaliacaoInstitucional().equals("dataInicio") || getCampoConsultaAvaliacaoInstitucional().equals("dataFinal")) {
            return true;
        }
        return false;
    }

    public boolean getIsFiltroNaoDataAvaliacaoInstitucional() {
        if (!getCampoConsultaAvaliacaoInstitucional().equals("data") && !getCampoConsultaAvaliacaoInstitucional().equals("dataInicio") && !getCampoConsultaAvaliacaoInstitucional().equals("dataFinal")) {
            return true;
        }
        return false;
    }

    public Boolean getApresentarCampoCpf() {
        if (getCampoConsultaProfessor().equals("cpf")) {
            return true;
        }
        return false;
    }

    public boolean getIsUnidadeEnsinoCadastradaAvaliacaoInstitucional() {
        if (!getAvaliacaoInstitucionalPresencialRespostaVO().getAvaliacaoInstitucional().getUnidadeEnsino().getCodigo().equals(0) || !getAvaliacaoInstitucionalPresencialRespostaVO().getTurma().getCodigo().equals(0) || !getAvaliacaoInstitucionalPresencialRespostaVO().getNovoObj()) {
            return true;
        }
        return false;
    }

    public boolean getIsCursoNaoCadastradoAvaliacaoInstitucional() {
        if (!getAvaliacaoInstitucionalPresencialRespostaVO().getAvaliacaoInstitucional().getCurso().getCodigo().equals(0) || !getAvaliacaoInstitucionalPresencialRespostaVO().getNovoObj()) {
            return false;
        }
        return true;
    }

    public boolean getIsTurmaNaoCadastradaAvaliacaoInstitucional() {
        if (!getAvaliacaoInstitucionalPresencialRespostaVO().getAvaliacaoInstitucional().getTurma().getCodigo().equals(0) || !getAvaliacaoInstitucionalPresencialRespostaVO().getNovoObj()) {
            return false;
        }
        return true;
    }

    public boolean getIsDisciplinaNaoCadastradaAvaliacaoInstitucional() {
        if (!getAvaliacaoInstitucionalPresencialRespostaVO().getAvaliacaoInstitucional().getDisciplina().getCodigo().equals(0) || !getAvaliacaoInstitucionalPresencialRespostaVO().getNovoObj()) {
            return false;
        }
        return true;
    }

    public boolean getIsAvaliacaoInstitucionalPresencialRespostaNaoCadastrada() {
        if (!getAvaliacaoInstitucionalPresencialRespostaVO().getNovoObj()) {
            return false;
        }
        return true;
    }

    public boolean getIsAvaliacaoInstitucionalSelecionada() {
        if (getAvaliacaoInstitucionalPresencialRespostaVO().getAvaliacaoInstitucional().getQuestionarioVO().getPerguntaQuestionarioVOs().isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Operação que inicializa as Interfaces Façades com os respectivos objetos de persistência dos dados no banco de
     * dados.
     */
    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<>(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public AvaliacaoInstitucionalPresencialRespostaVO getAvaliacaoInstitucionalPresencialRespostaVO() {
        if (avaliacaoInstitucionalPresencialRespostaVO == null) {
            avaliacaoInstitucionalPresencialRespostaVO = new AvaliacaoInstitucionalPresencialRespostaVO();
        }
        return avaliacaoInstitucionalPresencialRespostaVO;
    }

    public void setAvaliacaoInstitucionalPresencialRespostaVO(AvaliacaoInstitucionalPresencialRespostaVO avaliacaoInstitucionalPresencialRespostaVO) {
        this.avaliacaoInstitucionalPresencialRespostaVO = avaliacaoInstitucionalPresencialRespostaVO;
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

    public String getCampoConsultaDisciplina() {
        if (campoConsultaDisciplina == null) {
            campoConsultaDisciplina = "";
        }
        return campoConsultaDisciplina;
    }

    public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
        this.campoConsultaDisciplina = campoConsultaDisciplina;
    }

    public String getCampoConsultaTurma() {
        if (campoConsultaTurma == null) {
            campoConsultaTurma = "";
        }
        return campoConsultaTurma;
    }

    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;
    }

    public List<CursoVO> getListaConsultaCurso() {
        if (listaConsultaCurso == null) {
            listaConsultaCurso = new ArrayList<>(0);
        }
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
    }

    public List<DisciplinaVO> getListaConsultaDisciplina() {
        if (listaConsultaDisciplina == null) {
            listaConsultaDisciplina = new ArrayList<>(0);
        }
        return listaConsultaDisciplina;
    }

    public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
        this.listaConsultaDisciplina = listaConsultaDisciplina;
    }

    public List<TurmaVO> getListaConsultaTurma() {
        if (listaConsultaTurma == null) {
            listaConsultaTurma = new ArrayList<>(0);
        }
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
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

    public String getValorConsultaDisciplina() {
        if (valorConsultaDisciplina == null) {
            valorConsultaDisciplina = "";
        }
        return valorConsultaDisciplina;
    }

    public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
        this.valorConsultaDisciplina = valorConsultaDisciplina;
    }

    public String getValorConsultaTurma() {
        if (valorConsultaTurma == null) {
            valorConsultaTurma = "";
        }
        return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
    }

    public String getCampoConsultaAvaliacaoInstitucional() {
        if (campoConsultaAvaliacaoInstitucional == null) {
            campoConsultaAvaliacaoInstitucional = "";
        }
        return campoConsultaAvaliacaoInstitucional;
    }

    public void setCampoConsultaAvaliacaoInstitucional(String campoConsultaAvaliacaoInstitucional) {
        this.campoConsultaAvaliacaoInstitucional = campoConsultaAvaliacaoInstitucional;
    }

    public List<AvaliacaoInstitucionalVO> getListaConsultaAvaliacaoInstitucional() {
        if (listaConsultaAvaliacaoInstitucional == null) {
            listaConsultaAvaliacaoInstitucional = new ArrayList<>(0);
        }
        return listaConsultaAvaliacaoInstitucional;
    }

    public void setListaConsultaAvaliacaoInstitucional(List<AvaliacaoInstitucionalVO> listaConsultaAvaliacaoInstitucional) {
        this.listaConsultaAvaliacaoInstitucional = listaConsultaAvaliacaoInstitucional;
    }

    public String getValorConsultaAvaliacaoInstitucional() {
        if (valorConsultaAvaliacaoInstitucional == null) {
            valorConsultaAvaliacaoInstitucional = "";
        }
        return valorConsultaAvaliacaoInstitucional;
    }

    public void setValorConsultaAvaliacaoInstitucional(String valorConsultaAvaliacaoInstitucional) {
        this.valorConsultaAvaliacaoInstitucional = valorConsultaAvaliacaoInstitucional;
    }

    public Date getDataIni() {
        if (dataIni == null) {
            dataIni = new Date();
        }
        return dataIni;
    }

    public void setDataIni(Date dataIni) {
        this.dataIni = dataIni;
    }

    public Date getDataFim() {
        if (dataFim == null) {
            dataFim = new Date();
        }
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public String getCampoConsultaProfessor() {
        if (campoConsultaProfessor == null) {
            campoConsultaProfessor = "";
        }
        return campoConsultaProfessor;
    }

    public void setCampoConsultaProfessor(String campoConsultaProfessor) {
        this.campoConsultaProfessor = campoConsultaProfessor;
    }

    public String getValorConsultaProfessor() {
        if (valorConsultaProfessor == null) {
            valorConsultaProfessor = "";
        }
        return valorConsultaProfessor;
    }

    public void setValorConsultaProfessor(String valorConsultaProfessor) {
        this.valorConsultaProfessor = valorConsultaProfessor;
    }

    public List<FuncionarioVO> getListaConsultaProfessor() {
        if (listaConsultaProfessor == null) {
            listaConsultaProfessor = new ArrayList<>(0);
        }
        return listaConsultaProfessor;
    }

    public void setListaConsultaProfessor(List<FuncionarioVO> listaConsultaProfessor) {
        this.listaConsultaProfessor = listaConsultaProfessor;
    }
}
