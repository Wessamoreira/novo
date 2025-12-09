package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MapaAberturaTurmaVO;
import negocio.comuns.academico.TurmaAberturaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.controle.academico.AberturaTurmaRelControle;

/**
 *
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Controller("MapaAberturaTurmaControle")
@Scope("viewScope")
@Lazy
public class MapaAberturaTurmaControle extends SuperControle implements Serializable {

    private MapaAberturaTurmaVO mapaAberturaTurmaVO;
    private TurmaAberturaVO TurmaAberturaVO;
    private TurmaAberturaVO TurmaAberturaVOTemp;
    private Date dataInicio;
    private Date dataFim;
    private Date dataTemp;
    private UnidadeEnsinoVO unidadeEnsinoVO;
    private TurmaVO turmaVO;
    private CursoVO cursoVO;
    private String situacao;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List listaConsultaTurma;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;
    private List listaSelectItemUnidadeEnsino;
    private Boolean permitirUsuarioEditarSituacaoTurma;
    private Boolean permitirUsuarioExcluirSituacaoTurma;
    private Boolean apresentarGrafico;
    private Boolean possuiPermissaoVisualizarAbaPendencias;
    private Boolean enviarComunicadoPorEmail;
    private UsuarioVO usuarioDestinatarioVO;
    private String mensagemNotificacao;
    private String campoConsultaUsuario;
    private String valorConsultaUsuario;
    private Boolean marcarTodasUnidadeEnsino;
    private List listaConsultaUsuario;
    private AberturaTurmaRelControle aberturaTurmaRelControle;
    //rogerio
    private List listaConsultarUnidadeEnsino;
    private List<UnidadeEnsinoVO> listaUnidades;


    public MapaAberturaTurmaControle() {
        inicializarListasSelectItemTodosComboBox();
        consultarAberturaTurma();
        verificarPermissaoUsuarioVisualizarAbaPendencias();
        verificarUsuarioPossuiPermissaoEditarSituacaoTurma();
        verificarUsuarioPossuiPermissaoExcluirSituacaoTurma();
        consultarUnidadeEnsino();
        adicionarListaUnidades();
    }

    public void consultarAberturaTurma() {
        try {
            getMapaAberturaTurmaVO().setTurmaAberturaVOs(getFacadeFactory().getMapaAberturaTurmaFacade().consultarAberturaTurma(getTurmaVO().getCodigo(), getListaUnidades(), getCursoVO().getCodigo(), getSituacao(), getDataInicio(), getDataFim(), getUsuarioLogado()));
            getMapaAberturaTurmaVO().setTurmaAberturaPendenciasVOs(getFacadeFactory().getMapaAberturaTurmaFacade().consultarAberturaTurma(getTurmaVO().getCodigo(), getListaUnidades(), getCursoVO().getCodigo(), "AC", null, Uteis.getDataPassada(new Date(), 1), getUsuarioLogado()));
            if (!getMapaAberturaTurmaVO().getTurmaAberturaVOs().isEmpty()) {
                getMapaAberturaTurmaVO().setGraficoPizza(getFacadeFactory().getMapaAberturaTurmaFacade().montarGraficoPizza(getMapaAberturaTurmaVO().getTurmaAberturaVOs()));
            }
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public static void verificarPermissaoUsuarioVisualizarPendencias(UsuarioVO usuario, String nomeEntidade) throws Exception {
        ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
    }

    public void verificarPermissaoUsuarioVisualizarAbaPendencias() {
        Boolean liberar = false;
        try {
            verificarPermissaoUsuarioVisualizarPendencias(getUsuarioLogado(), "AberturaTurma_PermitirUsuarioVisualizarAbaPendencias");
            liberar = true;
        } catch (Exception e) {
            liberar = false;
        }
        this.setPossuiPermissaoVisualizarAbaPendencias(liberar);
    }

    public void imprimirRelatorioExcel() {
        try {
            aberturaTurmaRelControle = null;
            aberturaTurmaRelControle = (AberturaTurmaRelControle) context().getExternalContext().getSessionMap().get(AberturaTurmaRelControle.class.getSimpleName());
            if (aberturaTurmaRelControle == null) {
                aberturaTurmaRelControle = new AberturaTurmaRelControle();
                context().getExternalContext().getSessionMap().put(AberturaTurmaRelControle.class.getSimpleName(), aberturaTurmaRelControle);
            }
            if (!getMapaAberturaTurmaVO().getTurmaAberturaVOs().isEmpty()) {
                getAberturaTurmaRelControle().setTurmaVO(getTurmaVO());
//                getAberturaTurmaRelControle().setUnidadeEnsinoVO(getUnidadeEnsinoVO());
                getAberturaTurmaRelControle().setListaUnidades(getListaUnidades());
                getAberturaTurmaRelControle().setCursoVO(getCursoVO());
                getAberturaTurmaRelControle().setSituacao(getSituacao());
                getAberturaTurmaRelControle().setDataInicioTelaMapaAberturaTurma(getDataInicio());
                getAberturaTurmaRelControle().setDataFimTelaMapaAberturaTurma(getDataFim());
                getAberturaTurmaRelControle().imprimirRelatorioExcelTelaMapaAberturaTurma();
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void visualizarGrafico() {
        try {
            setApresentarGrafico(true);
            if (!getMapaAberturaTurmaVO().getTurmaAberturaVOs().isEmpty()) {
                getMapaAberturaTurmaVO().setGraficoPizza(getFacadeFactory().getMapaAberturaTurmaFacade().montarGraficoPizza(getMapaAberturaTurmaVO().getTurmaAberturaVOs()));
            }
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void persistirAberturaTurma() {
        try {
            getFacadeFactory().getMapaAberturaTurmaFacade().persistirAberturaTurma(getTurmaVO(), getTurmaAberturaVO(), getDataTemp(), getUsuarioLogado());
            setTurmaVO(null);
            consultarAberturaTurma();
            setMensagemID("msg_dados_gravados");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void editar() throws Exception {
        TurmaAberturaVO obj = (TurmaAberturaVO) context().getExternalContext().getRequestMap().get("turmaAberturaItens");
        setTurmaAberturaVO(obj);
        setDataTemp(obj.getData());
        setTurmaVO(obj.getTurma());
        getTurmaVO().getTurmaAberturaVOs().clear();
    }

    public void excluirItem() throws Exception {
        try {
            TurmaAberturaVO obj = (TurmaAberturaVO) context().getExternalContext().getRequestMap().get("turmaAberturaItens");
            //setTurmaAberturaVO(obj);
            getFacadeFactory().getTurmaAberturaFacade().excluir(obj);
            getMapaAberturaTurmaVO().getTurmaAberturaVOs().remove(obj);
            consultarAberturaTurma();
            setMensagemID("msg_dados_excluidos");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemUnidadeEnsino();
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        try {
            setListaSelectItemUnidadeEnsino(getFacadeFactory().getAberturaTurmaFacade().montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoLogado(), getUsuarioLogado()));
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    public void consultarTurma() {
        try {
            setListaConsultaTurma(getFacadeFactory().getMapaAberturaTurmaFacade().consultarTurma(getCampoConsultaTurma(), getUnidadeEnsinoVO(), getValorConsultaTurma(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaTurma(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarUsuario() {
        try {
            setListaConsultaUsuario(getFacadeFactory().getMapaAberturaTurmaFacade().consultarUsuario(getCampoConsultaUsuario(), getUnidadeEnsinoVO(), getValorConsultaUsuario(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaUsuario(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarUsuario() {
        UsuarioVO obj = (UsuarioVO) context().getExternalContext().getRequestMap().get("usuarioItens");
        setUsuarioDestinatarioVO(obj);
        setCampoConsultaUsuario("");
        setValorConsultaUsuario("");
        setListaConsultaUsuario(new ArrayList(0));
    }

    public void limparConsultaUsuario() {
        getListaConsultaUsuario().clear();
    }

    public void limparDadosUsuario() {
        setUsuarioDestinatarioVO(null);
    }

    public void consultarTurmaPorIdentificadorTurma() {
        try {
            setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getTurmaVO(), getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            setCursoVO(getTurmaVO().getCurso());
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setTurmaVO(new TurmaVO());
            setCursoVO(new CursoVO());
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarCurso() {
        try {
            setListaConsultaCurso(getFacadeFactory().getMapaAberturaTurmaFacade().consultarCurso(getCampoConsultaCurso(), getUnidadeEnsinoVO(), getValorConsultaCurso(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarCurso() throws Exception {
        try {
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
            setCursoVO(obj);
            setTurmaVO(null);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() throws Exception {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
        setCursoVO(getTurmaVO().getCurso());
        obj = null;
        valorConsultaTurma = "";
        campoConsultaTurma = "";
        listaConsultaTurma.clear();
    }

    public void limparIdentificador() {
        setTurmaVO(null);
    }

    public void limparCurso() throws Exception {
        try {
            setCursoVO(null);
        } catch (Exception e) {
        }
    }

    public List getTipoConsultaComboSituacao() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", "TODOS"));
        itens.add(new SelectItem("AC", "A confirmar"));
        itens.add(new SelectItem("AD", "Adiada"));
        itens.add(new SelectItem("CO", "Confirmada"));
        itens.add(new SelectItem("IN", "Inaugurada"));
        itens.add(new SelectItem("CA", "Cancelada"));
        return itens;
    }

    public List getTipoConsultaComboTurma() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade de Ensino"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }
    List<SelectItem> tipoConsultaComboCurso;

    public List getTipoConsultaComboCurso() {
        if (tipoConsultaComboCurso == null) {
            tipoConsultaComboCurso = new ArrayList(0);
            tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
        }
        return tipoConsultaComboCurso;
    }

    public void adicionarTurmaAbertura() throws Exception {
        try {
            if (getTurmaAberturaVO().getTurma().getCodigo() == 0) {
                getTurmaAberturaVO().setTurma(getTurmaVO());
            }
            if (getTurmaAberturaVO().getUsuario().getCodigo().equals(0)) {
                getTurmaAberturaVO().setUsuario(getUsuarioLogadoClone());
            }
            getFacadeFactory().getTurmaFacade().adicionarObjTurmaAberturaVOs(getTurmaVO(), getTurmaAberturaVO());
            this.setTurmaAberturaVO(new TurmaAberturaVO());
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public static void verificarPermissaoUsuarioEditarSituacaoTurma(UsuarioVO usuario, String nomeEntidade) throws Exception {
        ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(nomeEntidade, usuario);
    }

    public static void verificarPermissaoUsuarioExcluirSituacaoTurma(UsuarioVO usuario, String nomeEntidade) throws Exception {
        ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(nomeEntidade, usuario);
    }

    public void verificarUsuarioPossuiPermissaoEditarSituacaoTurma() {
        Boolean liberar = false;
        try {
            verificarPermissaoUsuarioEditarSituacaoTurma(getUsuarioLogado(), "PermitirEditarSituacaoTurma");
            liberar = true;
        } catch (Exception e) {
            liberar = false;
        }
        this.setPermitirUsuarioEditarSituacaoTurma(liberar);
    }

    public void verificarUsuarioPossuiPermissaoExcluirSituacaoTurma() {
        Boolean liberar = false;
        try {
            verificarPermissaoUsuarioExcluirSituacaoTurma(getUsuarioLogado(), "PermitirExcluirSituacaoTurma");
            liberar = true;
        } catch (Exception e) {
            liberar = false;
        }
        this.setPermitirUsuarioExcluirSituacaoTurma(liberar);
    }

    public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = new Date();
        }
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }

    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    public CursoVO getCursoVO() {
        if (cursoVO == null) {
            cursoVO = new CursoVO();
        }
        return cursoVO;
    }

    public void setCursoVO(CursoVO cursoVO) {
        this.cursoVO = cursoVO;
    }

    public String getSituacao() {
        if (situacao == null) {
            situacao = "";
        }
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
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

    public String getValorConsultaTurma() {
        if (valorConsultaTurma == null) {
            valorConsultaTurma = "";
        }
        return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
    }

    public List getListaConsultaTurma() {
        if (listaConsultaTurma == null) {
            listaConsultaTurma = new ArrayList(0);
        }
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
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

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public MapaAberturaTurmaVO getMapaAberturaTurmaVO() {
        if (mapaAberturaTurmaVO == null) {
            mapaAberturaTurmaVO = new MapaAberturaTurmaVO();
        }
        return mapaAberturaTurmaVO;
    }

    public void setMapaAberturaTurmaVO(MapaAberturaTurmaVO mapaAberturaTurmaVO) {
        this.mapaAberturaTurmaVO = mapaAberturaTurmaVO;
    }

    public TurmaAberturaVO getTurmaAberturaVO() {
        if (TurmaAberturaVO == null) {
            TurmaAberturaVO = new TurmaAberturaVO();
        }
        return TurmaAberturaVO;
    }

    public void setTurmaAberturaVO(TurmaAberturaVO TurmaAberturaVO) {
        this.TurmaAberturaVO = TurmaAberturaVO;
    }

    public TurmaAberturaVO getTurmaAberturaVOTemp() {
        if (TurmaAberturaVOTemp == null) {
            TurmaAberturaVOTemp = new TurmaAberturaVO();
        }
        return TurmaAberturaVOTemp;
    }

    public void setTurmaAberturaVOTemp(TurmaAberturaVO TurmaAberturaVOTemp) {
        this.TurmaAberturaVOTemp = TurmaAberturaVOTemp;
    }

    public Date getDataTemp() {
        if (dataTemp == null) {
            dataTemp = new Date();
        }
        return dataTemp;
    }

    public void setDataTemp(Date dataTemp) {
        this.dataTemp = dataTemp;
    }

    public Boolean getPermitirUsuarioEditarSituacaoTurma() {
        if (permitirUsuarioEditarSituacaoTurma == null) {
            permitirUsuarioEditarSituacaoTurma = false;
        }
        return permitirUsuarioEditarSituacaoTurma;
    }

    public void setPermitirUsuarioEditarSituacaoTurma(Boolean permitirUsuarioEditarSituacaoTurma) {
        this.permitirUsuarioEditarSituacaoTurma = permitirUsuarioEditarSituacaoTurma;
    }

    public Boolean getPermitirUsuarioExcluirSituacaoTurma() {
        if (permitirUsuarioExcluirSituacaoTurma == null) {
            permitirUsuarioExcluirSituacaoTurma = false;
        }
        return permitirUsuarioExcluirSituacaoTurma;
    }

    public void setPermitirUsuarioExcluirSituacaoTurma(Boolean permitirUsuarioExcluirSituacaoTurma) {
        this.permitirUsuarioExcluirSituacaoTurma = permitirUsuarioExcluirSituacaoTurma;
    }

    public boolean getIsPossuiListaTurmaAbertura() {
        if (!getMapaAberturaTurmaVO().getTurmaAberturaVOs().isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * @return the apresentarGrafico
     */
    public Boolean getApresentarGrafico() {
        if (apresentarGrafico == null) {
            apresentarGrafico = false;
        }
        return apresentarGrafico;
    }

    /**
     * @param apresentarGrafico the apresentarGrafico to set
     */
    public void setApresentarGrafico(Boolean apresentarGrafico) {
        this.apresentarGrafico = apresentarGrafico;
    }

    public Boolean getPossuiPermissaoVisualizarAbaPendencias() {
        if (possuiPermissaoVisualizarAbaPendencias == null) {
            possuiPermissaoVisualizarAbaPendencias = Boolean.FALSE;
        }
        return possuiPermissaoVisualizarAbaPendencias;
    }

    public void setPossuiPermissaoVisualizarAbaPendencias(Boolean possuiPermissaoVisualizarAbaPendencias) {
        this.possuiPermissaoVisualizarAbaPendencias = possuiPermissaoVisualizarAbaPendencias;
    }

    public void enviarEmailUsuario() {
        try {
            getFacadeFactory().getMapaAberturaTurmaFacade().enviarEmailUsuario(getUsuarioDestinatarioVO(), getUnidadeEnsinoVO(), getEnviarComunicadoPorEmail(), getMensagemNotificacao(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
            setMensagemNotificacao("");
            setMensagemID("msg_msg_enviados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setUsuarioDestinatarioVO(null);
            setMensagemNotificacao("");
        }
    }

    public String getAbriModalEnviarEmail() {
        if (getTurmaAberturaVO().getAbrirModalEnviarEmail()) {
            return "RichFaces.$('panelEnviarEmail').show()";
        }
        return "";
    }

    public Boolean getEnviarComunicadoPorEmail() {
        if (enviarComunicadoPorEmail == null) {
            enviarComunicadoPorEmail = Boolean.TRUE;
        }
        return enviarComunicadoPorEmail;
    }

    public void setEnviarComunicadoPorEmail(Boolean enviarComunicadoPorEmail) {
        this.enviarComunicadoPorEmail = enviarComunicadoPorEmail;
    }

    public String getMensagemNotificacao() {
        if (mensagemNotificacao == null) {
            mensagemNotificacao = "";
        }
        return mensagemNotificacao;
    }

    public void setMensagemNotificacao(String mensagemNotificacao) {
        this.mensagemNotificacao = mensagemNotificacao;
    }

    public String getCampoConsultaUsuario() {
        if (campoConsultaUsuario == null) {
            campoConsultaUsuario = "";
        }
        return campoConsultaUsuario;
    }

    public void setCampoConsultaUsuario(String campoConsultaUsuario) {
        this.campoConsultaUsuario = campoConsultaUsuario;
    }

    public String getValorConsultaUsuario() {
        if (valorConsultaUsuario == null) {
            valorConsultaUsuario = "";
        }
        return valorConsultaUsuario;
    }

    public void setValorConsultaUsuario(String valorConsultaUsuario) {
        this.valorConsultaUsuario = valorConsultaUsuario;
    }

    public List getListaConsultaUsuario() {
        if (listaConsultaUsuario == null) {
            listaConsultaUsuario = new ArrayList(0);
        }
        return listaConsultaUsuario;
    }

    public void setListaConsultaUsuario(List listaConsultaUsuario) {
        this.listaConsultaUsuario = listaConsultaUsuario;
    }

    public UsuarioVO getUsuarioDestinatarioVO() {
        if (usuarioDestinatarioVO == null) {
            usuarioDestinatarioVO = new UsuarioVO();
        }
        return usuarioDestinatarioVO;
    }

    public void setUsuarioDestinatarioVO(UsuarioVO usuarioDestinatarioVO) {
        this.usuarioDestinatarioVO = usuarioDestinatarioVO;
    }

    public AberturaTurmaRelControle getAberturaTurmaRelControle() {
        return aberturaTurmaRelControle;
    }

    public void setAberturaTurmaRelControle(AberturaTurmaRelControle aberturaTurmaRelControle) {
        this.aberturaTurmaRelControle = aberturaTurmaRelControle;
    }

    public List<SelectItem> getTipoConsultaComboUsuario() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("username", "Login"));
        return itens;
    }

    public Boolean getIsTurmaAberturaVOsPreenchida() {
        if (getMapaAberturaTurmaVO().getTurmaAberturaVOs().isEmpty()) {
            return false;
        }
        return true;
    }

    public void consultarUnidadeEnsino() {
        try {
           // setListaConsultarUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoFaltandoLista(getPlanoOrcamentarioVO().obterListaUnidadeEnsino()));
            setListaConsultarUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoFaltandoLista(getListaUnidades(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaUnidades(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarUnidadeEnsino() throws Exception {
        UnidadeEnsinoVO unidadesPlano = (UnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoConsItens");
        for (UnidadeEnsinoVO unidadesPlano1 : getListaUnidades()) {
            if (unidadesPlano1.getCodigo().intValue() == unidadesPlano.getCodigo().intValue()) {
                return;
            }
        }
//        UnidadesPlanoOrcamentarioVO unidades = new UnidadesPlanoOrcamentarioVO();
//        unidades.setUnidadeEnsino(unidadesPlano);
//        getListaUnidades().add(unidades);
        getListaUnidades().add(unidadesPlano);
        consultarUnidadeEnsino();
    }

    public void adicionarListaUnidades() {
//        getPlanoOrcamentarioVO().adicionarListaUnidades(getListaConsultarUnidadeEnsino());
        setListaUnidades(getListaConsultarUnidadeEnsino());
    }

    public void removerTodasUnidadeEnsino() {
        getListaUnidades();
        setListaUnidades(null);
    }
    
    public Boolean getMarcarTodasUnidadeEnsino() {
		if (marcarTodasUnidadeEnsino == null) {
			marcarTodasUnidadeEnsino = false;
		}
		return marcarTodasUnidadeEnsino;
	}
    
    public void setMarcarTodasUnidadeEnsino(Boolean marcarTodasUnidadeEnsino) {
    	this.marcarTodasUnidadeEnsino = marcarTodasUnidadeEnsino;
    }
    
    public void desmarcarTodos() {
        Iterator i = getListaUnidades().iterator();
        while (i.hasNext()) {
            UnidadeEnsinoVO obj = (UnidadeEnsinoVO)i.next();
            obj.setEscolhidaParaFazerCotacao(Boolean.FALSE);
        }
    }

    public void marcarTodos() {
        Iterator i = getListaUnidades().iterator();
        while (i.hasNext()) {
            UnidadeEnsinoVO obj = (UnidadeEnsinoVO)i.next();
            obj.setEscolhidaParaFazerCotacao(Boolean.TRUE);
        }
    }
    
    public void realizarSelecaoCheckboxMarcarDesmarcar() {
    	if(getMarcarTodasUnidadeEnsino()) {
    		marcarTodos();
    	} else {
    		desmarcarTodos();
    	}
    }
    
    public String getIsApresentarTextoCheckBoxMarcarDesmarcarTodosUnidade() {
		if (getMarcarTodasSituacoesUnidade()) {
			return UteisJSF.internacionalizar("prt_Inadimplencia_desmarcarTodos");
		}
		return UteisJSF.internacionalizar("prt_Inadimplencia_marcarTodos");
	}
    
    public Boolean getMarcarTodasSituacoesUnidade() {
    	if(marcarTodasUnidadeEnsino == null) {
    		marcarTodasUnidadeEnsino = false;
    	}
    	return marcarTodasUnidadeEnsino;
    }
    
    public void setMarcarTodasSituacoesUnidade(Boolean marcarTodasSituacoesUnidade) {
    	this.marcarTodasUnidadeEnsino = marcarTodasSituacoesUnidade;
    }


    public void removerUnidadeEnsino() {
        UnidadeEnsinoVO unidadeEnsino = (UnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoItens");
        int index = 0;
        for (UnidadeEnsinoVO unidadesPlano1 : getListaUnidades()) {
            if (unidadesPlano1.getCodigo().intValue() == unidadeEnsino.getCodigo().intValue()) {
                getListaUnidades().remove(index);
                return;
            }
            index++;
        }
    }

    public Integer getColumn() {
        if (getListaUnidades().size() > 4) {
            return 4;
        }
        return getListaUnidades().size();
    }

    public Integer getElement() {
        return getListaUnidades().size();
    }

    public List getListaConsultarUnidadeEnsino() {
        if (listaConsultarUnidadeEnsino == null) {
            listaConsultarUnidadeEnsino = new ArrayList(0);
        }
        return listaConsultarUnidadeEnsino;
    }

    public void setListaConsultarUnidadeEnsino(List listaConsultarUnidadeEnsino) {
        this.listaConsultarUnidadeEnsino = listaConsultarUnidadeEnsino;
    }
    /**
     * @return the listaUnidades
     */
    public List<UnidadeEnsinoVO> getListaUnidades() {
        if (listaUnidades == null) {
            listaUnidades = new ArrayList();
        }
        return listaUnidades;
    }

    /**
     * @param listaUnidades the listaUnidades to set
     */
    public void setListaUnidades(List listaUnidades) {
        this.listaUnidades = listaUnidades;
    }
}
