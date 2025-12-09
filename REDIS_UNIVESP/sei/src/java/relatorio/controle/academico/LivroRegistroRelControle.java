package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.RelacaoEnderecoAlunoRel;

@SuppressWarnings("unchecked")
@Controller("LivroRegistroRelControle")
@Scope("viewScope")
@Lazy
public class LivroRegistroRelControle extends SuperControleRelatorio {

    protected List listaProfessor;
    protected List listaConsultaTurma;
    protected Boolean existeUnidadeEnsino;
    protected String valorConsultaTurma;
    protected String campoConsultaTurma;
    protected List listaSelectItemUnidadeEnsino;
    protected List<TurmaVO> listaSelectItemTurma;
    protected UnidadeEnsinoVO unidadeEnsinoVO;
    protected TurmaVO turmaVO;
    protected String ano;
    protected String semestre;

    public LivroRegistroRelControle() throws Exception {
        inicializarListasSelectItemTodosComboBox();
        inicializarUnidadeEnsino();
        //obterUsuarioLogado();
        setMensagemID("msg_entre_prmrelatorio");
    }

    private void inicializarUnidadeEnsino() {
        try {
            getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
            if (getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
                setExisteUnidadeEnsino(Boolean.FALSE);
            } else {
                setExisteUnidadeEnsino(Boolean.TRUE);
            }
        } catch (Exception e) {
            setExisteUnidadeEnsino(Boolean.FALSE);
        }
    }

    public void imprimirPDF() {
        List listaObjetos = new ArrayList(0);
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "LivroRegistroRelControle", "Inicializando Geração de Relatório Livro Registro", "Emitindo Relatório");
            listaObjetos = getFacadeFactory().getRelacaoEnderecoAlunoRelFacade().criarObjetoLivroRegistro(getTurmaVO(), getAno(), getSemestre(), getUnidadeEnsinoVO().getCodigo());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(RelacaoEnderecoAlunoRel.getDesignIReportRelatorioLivroRegistro());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(RelacaoEnderecoAlunoRel.getCaminhoBaseRelatorioLivroRegistro());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Livro de Registro de Certificado");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(RelacaoEnderecoAlunoRel.getCaminhoBaseRelatorioLivroRegistro());
                getSuperParametroRelVO().setNomeEmpresa("");
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setFiltros("");
                if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
			setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
		}
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                inicializarListasSelectItemTodosComboBox();
                inicializarUnidadeEnsino();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "LivroRegistroRelControle", "Finalizando Geração de Relatório Livro Registro", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            //removerObjetoMemoria(this);
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    public String getApresentarRelatrio() {
        if (getMensagemDetalhada().equals("")) {
            return "";
        }
        return "";
    }

    public void consultarTurma() {
        try {
            limparAnoSemestre();
            List objs = new ArrayList(0);
            if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
                if (getCampoConsultaTurma().equals("identificadorTurma")) {
                    objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false, false, "", false,
                            Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
                }
                if (getCampoConsultaTurma().equals("nomeCurso")) {
                    objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false, false, false,
                            Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
                }
            } else {
                throw new Exception("Selecione uma Unidade de Ensino.");
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaTurma(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparIdentificador() {
        setTurmaVO(new TurmaVO());
    }

    public void selecionarTurma() throws Exception {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
        setTurmaVO(obj);
        obj = null;
        valorConsultaTurma = "";
        campoConsultaTurma = "";
        listaConsultaTurma.clear();
    }

    public List getListaSelectSemestre() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("1", "1º"));
        lista.add(new SelectItem("2", "2º"));
        return lista;
    }

    public List getTipoConsultaComboTurma() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public Boolean getSolicitarSemestreAnoParaEmissaoDiario() {
        if (getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()) {
            return false;
        } else {
            return true;
        }
    }

    public void limparAnoSemestre() {
        setAno("");
        setSemestre("");
    }

    public void montarTurma() throws Exception {
        try {
            limparAnoSemestre();
            if (!getTurmaVO().getIdentificadorTurma().equals("") && !getUnidadeEnsinoVO().getCodigo().equals(0)) {
                setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoVO().getCodigo().intValue(),
                        false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
            } else {
                throw new Exception("Informe a Turma e a Unidade de Ensino.");
            }
            if (!getMostrarAnoSemestre()) {
                setSemestre("");
            }
            if (!getMostrarSemestre()) {
                setAno("");
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            removerObjetoMemoria(getTurmaVO());
            setTurmaVO(new TurmaVO());
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemUnidadeEnsino();
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
                objs.add(new SelectItem(0, ""));
            }
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemUnidadeEnsino(objs);
            Uteis.liberarListaMemoria(resultadoConsulta);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void limparDados() {
    }

    public boolean getMostrarTurmaAgrupadaSemestre() {
        if (getTurmaVO().getTurmaAgrupada() && getTurmaVO().getSemestral()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getMostrarTurmaAgrupadaAno() {
        if (getTurmaVO().getTurmaAgrupada() && getTurmaVO().getAnual()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getMostrarSemestre() {
        if (getTurmaVO().getSemestral()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getMostrarSemestreSemAno() {
        if (getTurmaVO().getSemestral() && !getTurmaVO().getAnual()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean getMostrarAnoSemestre() {
        if (getTurmaVO().getSemestral() || getTurmaVO().getAnual()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os
     * objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            ////System.out.println("MENSAGEM => " + e.getMessage());;
        }

    }

    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public List getListaSelectItemUnidadeEnsino() {
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public String getCampoConsultaTurma() {
        return campoConsultaTurma;
    }

    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;
    }

    public List getListaConsultaTurma() {
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

    public String getValorConsultaTurma() {
        return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
    }

    public List getListaProfessor() {
        return listaProfessor;
    }

    public void setListaProfessor(List listaProfessor) {
        this.listaProfessor = listaProfessor;
    }

    public Boolean getExisteUnidadeEnsino() {
        return existeUnidadeEnsino;
    }

    public void setExisteUnidadeEnsino(Boolean existeUnidadeEnsino) {
        this.existeUnidadeEnsino = existeUnidadeEnsino;
    }

    public List<TurmaVO> getListaSelectItemTurma() {
        if (listaSelectItemTurma == null) {
            listaSelectItemTurma = new ArrayList<TurmaVO>(0);
        }
        return listaSelectItemTurma;
    }

    public void setListaSelectItemTurma(List<TurmaVO> listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;
    }

    public boolean getIsBloquearSemestreAno() throws Exception {
        if (getUsuarioLogado().getVisaoLogar().equals("professor") && getTurmaVO().getCurso().getNivelEducacional().equals("SU")) {
            return true;
        } else {
            return false;
        }
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

    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getSemestre() {
        if (semestre == null) {
            semestre = "";
        }
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
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
}
