package relatorio.controle.crm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.faces.model.SelectItem;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.crm.ExtratoComissaoRelVO;
import relatorio.negocio.jdbc.crm.ExtratoComissaoSinteticoRel;

/**
 *
 * @author Carlos
 */
@Controller("ExtratoComissaoSinteticoRelControle")
@Scope("viewScope")
@Lazy
public class ExtratoComissaoSinteticoRelControle extends SuperControleRelatorio {

    private UnidadeEnsinoVO unidadeEnsinoVO;
    private FuncionarioVO funcionarioVO;
    private TurmaVO turmaVO;
    private String valorConsultaMes;
    private String valorOrdenarPor;
    private String valorConsultaFuncionario;
    private String campoConsultaFuncionario;
    private List listaConsultaFuncionario;
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    private List listaConsultaTurma;
    private List listaSelectItemUnidadeEnsino;
    private String tipoRelatorio;

    public ExtratoComissaoSinteticoRelControle() {
        inicializarListasSelectItemTodosComboBox();
    }

    public void imprimirPDF() {
        String titulo = null;
        String design = null;
        List<ExtratoComissaoRelVO> listaExtratoComissaoVOs = new ArrayList(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "ExtratoComissaoSinteticoRelControle", "Inicializando Geração de Relatório Extrato Comissão", "Emitindo Relatório");
            if (getTipoRelatorio().equals("sintetico")) {
                titulo = "Extrato Comissão Sintético";
            } else {
                titulo = "Extrato Comissão Analítico";
            }
            design = ExtratoComissaoSinteticoRel.getDesignIReportRelatorio(getTipoRelatorio());
            listaExtratoComissaoVOs = getFacadeFactory().getExtratoComissaoSinteticoRelFacade().criarObjeto(getUnidadeEnsinoVO().getCodigo(), getFuncionarioVO().getPessoa().getCodigo(), getTurmaVO().getCodigo(), getTipoRelatorio(), getValorConsultaMes(), getValorOrdenarPor(), getUsuarioLogado());
            if (!listaExtratoComissaoVOs.isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(ExtratoComissaoSinteticoRel.caminhoBaseRelatorio());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(ExtratoComissaoSinteticoRel.caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                if (getUnidadeEnsinoVO().getCodigo() > 0) {
                    getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
                } else {
                    getSuperParametroRelVO().setUnidadeEnsino("TODAS");
                }
                getSuperParametroRelVO().setPeriodo(getValorConsultaMes());
                getSuperParametroRelVO().setListaObjetos(listaExtratoComissaoVOs);
                setMensagemID("msg_relatorio_ok");
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                inicializarListasSelectItemTodosComboBox();
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "ExtratoComissaoSinteticoRelControle", "Finalizando Geração de Relatório Extrato Comissão", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            titulo = null;
            design = null;
            Uteis.liberarListaMemoria(listaExtratoComissaoVOs);
        }
    }

    public void imprimirRelatorioExcel() {
        String titulo = null;
        String design = null;
        List<ExtratoComissaoRelVO> listaExtratoComissaoVOs = new ArrayList(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "ExtratoComissaoSinteticoRelControle", "Inicializando Geração de Relatório Extrato Comissão", "Emitindo Relatório");
            if (getTipoRelatorio().equals("sintetico")) {
                titulo = "Extrato Comissão Sintético";
            } else {
                titulo = "Extrato Comissão Analítico";
            }
            design = ExtratoComissaoSinteticoRel.getDesignIReportRelatorioExcel(getTipoRelatorio());
            listaExtratoComissaoVOs = getFacadeFactory().getExtratoComissaoSinteticoRelFacade().criarObjeto(getUnidadeEnsinoVO().getCodigo(), getFuncionarioVO().getPessoa().getCodigo(), getTurmaVO().getCodigo(), getTipoRelatorio(), getValorConsultaMes(), getValorOrdenarPor(), getUsuarioLogado());
            if (!listaExtratoComissaoVOs.isEmpty()) {
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setNomeDesignIreport(design);
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setSubReport_Dir(ExtratoComissaoSinteticoRel.caminhoBaseRelatorio());
                getSuperParametroRelVO().setCaminhoBaseRelatorio(ExtratoComissaoSinteticoRel.caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                if (getUnidadeEnsinoVO().getCodigo() > 0) {
                    getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
                } else {
                    getSuperParametroRelVO().setUnidadeEnsino("TODAS");
                }
                getSuperParametroRelVO().setPeriodo(getValorConsultaMes());
                getSuperParametroRelVO().setListaObjetos(listaExtratoComissaoVOs);
                setMensagemID("msg_relatorio_ok");
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                inicializarListasSelectItemTodosComboBox();
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "ExtratoComissaoSinteticoRelControle", "Finalizando Geração de Relatório Extrato Comissão", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            titulo = null;
            design = null;
            Uteis.liberarListaMemoria(listaExtratoComissaoVOs);
        }
    }

    public void consultarFuncionario() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaFuncionario().equals("")) {
                setListaConsulta(new ArrayList(0));
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("cargo")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("departamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaFuncionario(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarFuncionario() {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
        setFuncionarioVO(obj);
    }

    public void limparDadosFuncionario() {
        setFuncionarioVO(new FuncionarioVO());
    }

    public void limparConsultaFuncionario() {
        getListaConsultaFuncionario().clear();
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemUnidadeEnsino();
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            setListaSelectItemUnidadeEnsino(getFacadeFactory().getRankingFacade().montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparConsultaTurma() {
        getListaConsultaTurma().clear();
    }

    public void limparDadosTurma() {
        setTurmaVO(new TurmaVO());
    }

    public void consultarTurma() {
        try {
            super.consultar();
            setListaConsultaTurma(getFacadeFactory().getExtratoComissaoSinteticoRelFacade().consultarTurma(getCampoConsultaTurma(), getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        setTurmaVO(obj);
    }

    public List getTipoConsultaComboFuncionario() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("cargo", "Cargo"));
        itens.add(new SelectItem("departamento", "Departamento"));
        itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
        return itens;
    }

    public List getTipoConsultaComboOrdenar() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome do Consultor"));
        itens.add(new SelectItem("valor", "Valor a Receber"));
        return itens;
    }

    public List getTipoConsultaComboTipoRelatorio() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("analitico", "Analítico"));
        itens.add(new SelectItem("sintetico", "Sintético"));
        return itens;
    }

    public List getTipoConsultaComboTurma() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        return itens;
    }

    public FuncionarioVO getFuncionarioVO() {
        if (funcionarioVO == null) {
            funcionarioVO = new FuncionarioVO();
        }
        return funcionarioVO;
    }

    public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
        this.funcionarioVO = funcionarioVO;
    }

    public String getValorConsultaMes() {
        if (valorConsultaMes == null) {
            valorConsultaMes = "";
        }
        return valorConsultaMes;
    }

    public void setValorConsultaMes(String valorConsultaMes) {
        this.valorConsultaMes = valorConsultaMes;
    }

    public String getValorOrdenarPor() {
        if (valorOrdenarPor == null) {
            valorOrdenarPor = "";
        }
        return valorOrdenarPor;
    }

    public void setValorOrdenarPor(String valorOrdenarPor) {
        this.valorOrdenarPor = valorOrdenarPor;
    }

    public String getValorConsultaFuncionario() {
        if (valorConsultaFuncionario == null) {
            valorConsultaFuncionario = "";
        }
        return valorConsultaFuncionario;
    }

    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
    }

    public String getCampoConsultaFuncionario() {
        if (campoConsultaFuncionario == null) {
            campoConsultaFuncionario = "";
        }
        return campoConsultaFuncionario;
    }

    public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
        this.campoConsultaFuncionario = campoConsultaFuncionario;
    }

    public List getListaConsultaFuncionario() {
        if (listaConsultaFuncionario == null) {
            listaConsultaFuncionario = new ArrayList(0);
        }
        return listaConsultaFuncionario;
    }

    public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
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

    public String getTipoRelatorio() {
        if (tipoRelatorio == null) {
            tipoRelatorio = "";
        }
        return tipoRelatorio;
    }

    public void setTipoRelatorio(String tipoRelatorio) {
        this.tipoRelatorio = tipoRelatorio;
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

    public String getCampoConsultaTurma() {
        if (campoConsultaTurma == null) {
            campoConsultaTurma = "";
        }
        return campoConsultaTurma;
    }

    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;
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

    public boolean getApresentarCampoRelSintetico() {
        return getTipoRelatorio().equals("sintetico");
    }
}
