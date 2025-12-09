package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import jakarta.faces. model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import controle.arquitetura.SelectItemOrdemValor;

@Controller("DisciplinaRelControle")
@Scope("viewScope")
@Lazy
public class DisciplinaRelControle extends SuperControleRelatorio {

    private String disciplinaErro;
    private String nomeCurso = "";
    private List listaSelectItemCurso;
    private List listaSelectItemPeriodoLetivo;
    private List listaSelectItemGradeCurricular;
    private String valorConsultaDisciplina;
    private String campoConsultaDisciplina;
    private List listaConsultaDisciplina;
    private List listaSelectItemTipoRelatorio;
    private String tipoRelatorio;
    private DisciplinaVO disciplinaVO;
    private CursoVO curso;
    private GradeCurricularVO gradeCurricular;
    private PeriodoLetivoVO periodoLetivo;

    public DisciplinaRelControle() throws Exception {
        inicializarListasSelectItemTodosComboBox();
        // obterUsuarioLogado();
        setMensagemID("msg_entre_prmrelatorio");
    }

    private void inicializarNomeCurso() throws Exception {
        setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getCurso().getCodigo().intValue(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false,
                getUsuarioLogado()));
        setNomeCurso("Curso: " + getCurso().getNome());
    }

    public void imprimirPDF() {
        List listaObjetos = new ArrayList(0);
        String retornoPeriodoLetivo = "Todos";
        String retornoDisciplina = "Todas";
        String retornoTipoRelatorio = "";
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "DisciplinaRelControle", "Inicializando Geração de Relatório Disciplina", "Emitindo Relatório");
            getFacadeFactory().getDisciplinaRelFacade().ValidarDados(getTipoRelatorio(), getCurso(), getGradeCurricular());
            if (getTipoRelatorio().equals("Sintetico")) {
            	GradeCurricularVO gradeCurricularVO = new GradeCurricularVO();
            	gradeCurricularVO = getFacadeFactory().getDisciplinaRelFacade().criarObjetoRelatorioSintetico(getTipoRelatorio(), getCurso(), getGradeCurricular(), getPeriodoLetivo(), getDisciplinaVO(), getUsuarioLogado());
            	if (gradeCurricularVO != null && !gradeCurricularVO.getCodigo().equals(0)) {
            		listaObjetos.add(gradeCurricularVO);
            	} else {
            		setMensagemID("msg_relatorio_sem_dados");
            	}
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getDisciplinaRelFacade().designIReportRelatorioSintetico());
            } else {
                listaObjetos = getFacadeFactory().getDisciplinaRelFacade().criarObjeto(getTipoRelatorio(), getCurso(), getGradeCurricular(), getPeriodoLetivo(),
                        getDisciplinaVO());
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getDisciplinaRelFacade().designIReportRelatorio(getTipoRelatorio()));
            }
            
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getDisciplinaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getDisciplinaRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório de Disciplina");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                if (getCurso().getCodigo() > 0) {
                    getSuperParametroRelVO().setCurso(
                            (getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getCurso().getCodigo(),
                            Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado())).getNome());
                } else {
                    getSuperParametroRelVO().setCurso("TODOS");
                }
                getSuperParametroRelVO().setMatrizCurricular(
                        (getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(getGradeCurricular().getCodigo(),
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());


                if (getPeriodoLetivo().getCodigo() != null && getPeriodoLetivo().getCodigo() > 0) {
                    retornoPeriodoLetivo = (getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(getPeriodoLetivo().getCodigo(),
                            Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getDescricao();
                }
                getSuperParametroRelVO().setPeriodo(retornoPeriodoLetivo);


                if (getDisciplinaVO().getCodigo() != null && getDisciplinaVO().getCodigo() > 0) {
                    retornoDisciplina = (getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplinaVO().getCodigo(),
                            Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome();
                }
                getSuperParametroRelVO().setDisciplina(retornoDisciplina);


                if (getTipoRelatorio().equals("Sintetico")) {
                    retornoTipoRelatorio = "Sintético";
                } else if (getTipoRelatorio().equals("ReferenciaBibliografica")) {
                    retornoTipoRelatorio = "Referência Bibliográfica";
                } else if (getTipoRelatorio().equals("PlanejamentoConteudo")) {
                    retornoTipoRelatorio = "Planejamento Conteúdo";
                }
                getSuperParametroRelVO().setTipoRelatorio(retornoTipoRelatorio);
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                inicializarListasSelectItemTodosComboBox();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "DisciplinaRelControle", "Finalizando Geração de Relatório Disciplina", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
            retornoPeriodoLetivo = null;
            retornoDisciplina = null;
            retornoTipoRelatorio = null;

        }
    }

    private void inicializarListasSelectItemTodosComboBox() throws Exception {
        montarListaSelectItemCurso();
        montarListaSelectItemTipoRelatorio();
        montarListaSelectItemDisciplina();

    }

    public void inicializarListasGradeCurricular() throws Exception {
        montarListaSelectItemGradeCurricular(getCurso().getCodigo());
    }

    public void montarListaSelectItemTipoRelatorio() {
        List<SelectItem> objs = new ArrayList<SelectItem>(0);
        objs.add(new SelectItem("Sintetico", "Sintético"));
        objs.add(new SelectItem("ReferenciaBibliografica", "Referência Bibliográfica"));
        objs.add(new SelectItem("PlanejamentoConteudo", "Planejamento Conteúdo"));
        setListaSelectItemTipoRelatorio(objs);
    }

    public void montarListaSelectItemCurso(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCursoPorNome(prm);
            i = resultadoConsulta.iterator();
            getListaSelectItemCurso().clear();
            getListaSelectItemCurso().add(new SelectItem(0, ""));
            while (i.hasNext()) {
                CursoVO obj = (CursoVO) i.next();
                getListaSelectItemCurso().add(new SelectItem(obj.getCodigo(), obj.getNome()));
                removerObjetoMemoria(obj);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemCurso() {
        try {
            montarListaSelectItemCurso("");
        } catch (Exception e) {
            ////System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public List consultarCursoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getCursoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemGradeCurricular(Integer prm) {
        List<GradeCurricularVO> resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarGradeCurricularCurso(prm);
            getListaSelectItemGradeCurricular().clear();
            getListaSelectItemGradeCurricular().add(new SelectItem(0, ""));
    		Collections.sort(resultadoConsulta, new Comparator<GradeCurricularVO>(){
	   		     public int compare(GradeCurricularVO o1, GradeCurricularVO o2){
	   		         if(o1.getSituacao() == o2.getSituacao()) {
	   		             return 0;
	   		         }
	   		         return o1.getSituacao().compareTo(o2.getSituacao());
	   		     }
	   		});
            i = resultadoConsulta.iterator();
            while (i.hasNext()) {
                GradeCurricularVO obj = (GradeCurricularVO) i.next();
                if (!obj.getSituacao().equals("CO")) {
                    getListaSelectItemGradeCurricular().add(new SelectItem(obj.getCodigo(), obj.getNome() + " - " + obj.getSituacao_Apresentar()));
                }
                removerObjetoMemoria(obj);
            }
        } catch (Exception e) {
        } finally {
            i = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
        }

    }

    public List consultarGradeCurricularCurso(Integer prm) throws Exception {
        List lista = getFacadeFactory().getGradeCurricularFacade().consultarPorCodigoCurso(prm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemPeriodoLetivo(String prm) throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarPeriodoLetivoPorGradecurricular(getGradeCurricular().getCodigo().intValue());
            i = resultadoConsulta.iterator();

            getListaSelectItemPeriodoLetivo().add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
                getListaSelectItemPeriodoLetivo().add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
                removerObjetoMemoria(obj);
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) getListaSelectItemPeriodoLetivo(), ordenador);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    private List consultarPeriodoLetivoPorGradecurricular(Integer codigo) throws Exception {
        List lista = getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(codigo, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemPeriodoLetivo() throws Exception {
        SelectItemOrdemValor ordenador = null;
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarPeriodoLetivoPorGradecurricular(getGradeCurricular().getCodigo().intValue());
            i = resultadoConsulta.iterator();
            getListaSelectItemPeriodoLetivo().clear();
            getListaSelectItemPeriodoLetivo().add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
                getListaSelectItemPeriodoLetivo().add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
                removerObjetoMemoria(obj);
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) getListaSelectItemPeriodoLetivo(), ordenador);
        } catch (Exception e) {
            throw e;
        } finally {
            ordenador = null;
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void atualizarListasGradeCurricularPeriodoLetivoEDisciplina() throws Exception {
        getListaConsultaDisciplina().clear();
        getListaSelectItemGradeCurricular().clear();
        getListaSelectItemPeriodoLetivo().clear();
        getPeriodoLetivo().setCodigo(0);
        getDisciplinaVO().setCodigo(0);
        getGradeCurricular().setCodigo(0);
        inicializarListasGradeCurricular();
        montarListaSelectItemPeriodoLetivo();
        montarListaSelectItemDisciplina();
    }

    public void atualizarListasPeriodoLetivoEDisciplina() throws Exception {
        getListaConsultaDisciplina().clear();
        getListaSelectItemPeriodoLetivo().clear();
        getPeriodoLetivo().setCodigo(0);
        getDisciplinaVO().setCodigo(0);

        montarListaSelectItemPeriodoLetivo();
        montarListaSelectItemDisciplina();
    }

    public void atualizarListasDisciplinas() throws Exception {
        getDisciplinaVO().setCodigo(0);
        getListaConsultaDisciplina().clear();
        montarListaSelectItemDisciplina();
    }

    public void montarListaSelectItemDisciplina() throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            getListaConsultaDisciplina().clear();
            if (getPeriodoLetivo().getCodigo() == 0) {
                resultadoConsulta = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorGradeCurricular(getGradeCurricular().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            } else {
                resultadoConsulta = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaPorGradeCurricularEPeriodoLetivo(getGradeCurricular().getCodigo(),
                        getPeriodoLetivo().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            i = resultadoConsulta.iterator();


            getListaConsultaDisciplina().add(new SelectItem(0, ""));
            while (i.hasNext()) {
                DisciplinaVO obj = (DisciplinaVO) i.next();
                getListaConsultaDisciplina().add(new SelectItem(obj.getCodigo(), obj.getNome()));
                removerObjetoMemoria(obj);
            }
            SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
            Collections.sort((List) getListaConsultaDisciplina(), ordenador);

        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }

    }

    public void consultarDisciplina() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaDisciplina().equals("codigo")) {
                if (getValorConsultaDisciplina().equals("")) {
                    setValorConsultaDisciplina("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaDisciplina());
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
                        getUsuarioLogado());
            }
            if (getCampoConsultaDisciplina().equals("nome")) {
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
                        getUsuarioLogado());
            }
            if (getCampoConsultaDisciplina().equals("areaConhecimento")) {
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeAreaConhecimento(getValorConsultaDisciplina(), true,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaDisciplina(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaDisciplina(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarDisciplina() {
        DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplina");
        setDisciplinaVO(obj);
        getListaConsultaDisciplina().clear();
        this.setValorConsultaDisciplina("");
        this.setCampoConsultaDisciplina("");
        obj = null;
    }

    public void ValidarCampos() throws ConsistirException {
        if ((getCurso().getCodigo().intValue() == 0) && (getDisciplinaVO().getNome().equals(""))) {
            throw new ConsistirException("Por Favor Informe o Campo do Curso ou da Disciplina");
        }
        if ((getCurso().getCodigo() != 0 && (getGradeCurricular().getCodigo() == null))) {
            throw new ConsistirException("Por Favor Informe o Campo da GradeCurricular");
        }
    }

    public List getTipoConsultaComboDisciplina() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("areaConhecimento", "Área de Conhecimento"));
        return itens;
    }

    public String getDisciplinaErro() {
        if (disciplinaErro == null) {
            disciplinaErro = "";
        }
        return disciplinaErro;
    }

    public void setDisciplinaErro(String disciplinaErro) {
        this.disciplinaErro = disciplinaErro;
    }

    public List getListaSelectItemCurso() {
        if (listaSelectItemCurso == null) {
            listaSelectItemCurso = new ArrayList(0);
        }
        return listaSelectItemCurso;
    }

    public void setListaSelectItemCurso(List listaSelectItemCurso) {
        this.listaSelectItemCurso = listaSelectItemCurso;
    }

    public String getNomeCurso() {
        if (nomeCurso == null) {
            nomeCurso = "";
        }
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
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

    public String getValorConsultaDisciplina() {
        if (valorConsultaDisciplina == null) {
            valorConsultaDisciplina = "";
        }
        return valorConsultaDisciplina;
    }

    public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
        this.valorConsultaDisciplina = valorConsultaDisciplina;
    }

    public List getListaConsultaDisciplina() {
        if (listaConsultaDisciplina == null) {
            listaConsultaDisciplina = new ArrayList(0);
        }
        return listaConsultaDisciplina;
    }

    public void setListaConsultaDisciplina(List listaConsultaDisciplina) {
        this.listaConsultaDisciplina = listaConsultaDisciplina;
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

    public List getListaSelectItemTipoRelatorio() {
        if (listaSelectItemTipoRelatorio == null) {
            listaSelectItemTipoRelatorio = new ArrayList(0);
        }
        return listaSelectItemTipoRelatorio;
    }

    public void setListaSelectItemTipoRelatorio(List listaSelectItemTipoRelatorio) {
        this.listaSelectItemTipoRelatorio = listaSelectItemTipoRelatorio;
    }

    public List getListaSelectItemGradeCurricular() {
        if (listaSelectItemGradeCurricular == null) {
            listaSelectItemGradeCurricular = new ArrayList(0);
        }
        return listaSelectItemGradeCurricular;
    }

    public void setListaSelectItemGradeCurricular(List listaSelectItemGradeCurricular) {
        this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
    }

    public List getListaSelectItemPeriodoLetivo() {
        if (listaSelectItemPeriodoLetivo == null) {
            listaSelectItemPeriodoLetivo = new ArrayList(0);
        }
        return listaSelectItemPeriodoLetivo;
    }

    public void setListaSelectItemPeriodoLetivo(List listaSelectItemPeriodoLetivo) {
        this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
    }

    public CursoVO getCurso() {
        if (curso == null) {
            curso = new CursoVO();
        }
        return curso;
    }

    public void setCurso(CursoVO curso) {
        this.curso = curso;
    }

    public DisciplinaVO getDisciplinaVO() {
        if (disciplinaVO == null) {
            disciplinaVO = new DisciplinaVO();
        }
        return disciplinaVO;
    }

    public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
        this.disciplinaVO = disciplinaVO;
    }

    public GradeCurricularVO getGradeCurricular() {
        if (gradeCurricular == null) {
            gradeCurricular = new GradeCurricularVO();
        }
        return gradeCurricular;
    }

    public void setGradeCurricular(GradeCurricularVO gradeCurricular) {
        this.gradeCurricular = gradeCurricular;
    }

    public PeriodoLetivoVO getPeriodoLetivo() {
        if (periodoLetivo == null) {
            periodoLetivo = new PeriodoLetivoVO();
        }
        return periodoLetivo;
    }

    public void setPeriodoLetivo(PeriodoLetivoVO periodoLetivo) {
        this.periodoLetivo = periodoLetivo;
    }
}
