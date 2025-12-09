/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.AlunoNaoCursouDisciplinaFiltroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.AlunoNaoCursouDisciplinaRel;
import relatorio.negocio.jdbc.academico.FiltroRelatorioAcademicoVO;

/**
 *
 * @author Otimize-Not
 */
@Controller("AlunoQueDeveDisciplinaRelControle")
@Scope("request")
@Lazy
public class AlunoQueDeveDisciplinaRelControle extends SuperControleRelatorio {

    private AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private List<DisciplinaVO> listaConsultaDisciplina;
    private String campoConsultaDisciplina;
    private String valorConsultaDisciplina;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List listaConsultaTurma;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;

    public AlunoQueDeveDisciplinaRelControle() {
        inicializarListaOrdenacao();
    }

    public void imprimirPDF() {
        try {
            String titulo = "Alunos Que Deve Disciplina";
            String design = getFacadeFactory().getAlunoNaoCursouDisciplinaRelFacade().designIReportRelatorio(TipoRelatorioEnum.PDF);
            getFacadeFactory().getAlunoNaoCursouDisciplinaRelFacade().consultarRelatorio(getAlunoNaoCursouDisciplinaFiltroRelVO(), new FiltroRelatorioAcademicoVO());
            List listaObjetos = new ArrayList(0);
            listaObjetos.add(getAlunoNaoCursouDisciplinaFiltroRelVO());
            apresentarRelatorioObjetos(AlunoNaoCursouDisciplinaRel.getIdEntidade(), titulo, getUnidadeEnsinoLogado().getNome(), "", "PDF", "/" + AlunoNaoCursouDisciplinaRel.getIdEntidade() + "/registros", design,
                    getUsuarioLogado().getNome(), "", listaObjetos, getFacadeFactory().getAlunoNaoCursouDisciplinaRelFacade().caminhoIReportRelatorio());
            setMensagemID("msg_relatorio_ok");


        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            removerObjetoMemoria(this);
        }

    }

    public void subirListaOrdenacao() {
        String ordem = (String) context().getExternalContext().getRequestMap().get("ordem");
        for (int i = 1; i < getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().size(); i++) {
            if (getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().get(i).equals(ordem)) {
                if (i == 1) {
                    return;
                }
                String ordemTrocar = getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().get(i - 1);
                getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().set(i - 1, ordem);
                getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().set(i, ordemTrocar);
                return;
            }
        }
    }

    public void descerListaOrdenacao() {
        String ordem = (String) context().getExternalContext().getRequestMap().get("ordem");
        for (int i = 1; i < getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().size(); i++) {
            if (getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().get(i).equals(ordem)) {
                if (getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().size() == i) {
                    return;
                }
                String ordemTrocar = getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().get(i + 1);
                getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().set(i + 1, ordem);
                getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().set(i, ordemTrocar);
                return;
            }
        }
    }

    public void consultarDisciplina() {
        try {

            List objs = new ArrayList(0);
            if (getValorConsultaDisciplina().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaDisciplina().equals("codigo")) {
                int valorInt = Integer.parseInt(getValorConsultaDisciplina());
                DisciplinaVO disciplina = getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(new Integer(valorInt), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                if (!disciplina.equals(new DisciplinaVO()) || disciplina != null) {
                    objs.add(disciplina);
                }
            }
            if (getCampoConsultaDisciplina().equals("nome")) {
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaDisciplina().equals("areaConhecimento")) {
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorNomeAreaConhecimento(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }

            setListaConsultaDisciplina(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaDisciplina(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getTipoConsultaComboDisciplina() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("areaConhecimento", "Área de Conhecimento"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public void inicializarListaOrdenacao() {
        getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().add("Matrícula");
        getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().add("Aluno");
        getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().add("Curso");
        getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().add("Unidade Ensino");
        getAlunoNaoCursouDisciplinaFiltroRelVO().getOrdernarPor().add("Ano Ingresso");

    }

    public void selecionarDisciplina() {
        DisciplinaVO disciplina = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplina");
        getAlunoNaoCursouDisciplinaFiltroRelVO().setDisciplinaVO(disciplina);
    }

    public void consultarTurma() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeTurno")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeCurso")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public Integer getAnoAtual() {
        return Uteis.getAnoData(new Date());
    }

    public void consultarTurmaPorIdentificador() {
        try {
            if (getAlunoNaoCursouDisciplinaFiltroRelVO().getTurmaVO().getIdentificadorTurma().equalsIgnoreCase("")) {
                return;
            } else {
                TurmaVO turmaVO = getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getAlunoNaoCursouDisciplinaFiltroRelVO().getTurmaVO().getIdentificadorTurma(), super.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
                getAlunoNaoCursouDisciplinaFiltroRelVO().setTurmaVO(turmaVO);
                inicializarDadosCurso();
                montarListaSelectItemUnidadeEnsino();
                setMensagemID("msg_dados_consultados");
            }
        } catch (Exception e) {
            limparDadosTurma();
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turma");
            getAlunoNaoCursouDisciplinaFiltroRelVO().setTurmaVO(obj);
            inicializarDadosCurso();
            montarListaSelectItemUnidadeEnsino();
        } catch (Exception ex) {
            setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public void limparDadosCurso() {
        getAlunoNaoCursouDisciplinaFiltroRelVO().setCursoVO(new CursoVO());
//        getAlunoNaoCursouDisciplinaFiltroRelVO().getCursoVO().setCodigo(0);
//        getAlunoNaoCursouDisciplinaFiltroRelVO().getCursoVO().setNome("");
        try {
            montarListaSelectItemUnidadeEnsino();
        } catch (Exception ex) {
            Logger.getLogger(AlunoNaoCursouDisciplinaRelControle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void limparDadosDisciplina() {
        getAlunoNaoCursouDisciplinaFiltroRelVO().setDisciplinaVO(new DisciplinaVO());
//        getAlunoNaoCursouDisciplinaFiltroRelVO().getDisciplinaVO().setCodigo(0);
//        getAlunoNaoCursouDisciplinaFiltroRelVO().getDisciplinaVO().setNome("");
    }

    public void limparDadosTurma() {
        getAlunoNaoCursouDisciplinaFiltroRelVO().setTurmaVO(new TurmaVO());
//        getAlunoNaoCursouDisciplinaFiltroRelVO().getTurmaVO().setCodigo(0);
//        getAlunoNaoCursouDisciplinaFiltroRelVO().getTurmaVO().setIdentificadorTurma("");
        try {
            montarListaSelectItemUnidadeEnsino();
        } catch (Exception ex) {
            Logger.getLogger(AlunoNaoCursouDisciplinaRelControle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        // itens.add(new SelectItem("codigo", "Código"));

        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    public void consultarCurso() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCurso().equals("codigo")) {
                if (getValorConsultaCurso().equals("")) {
                    setValorConsultaCurso("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCurso());
                objs = getFacadeFactory().getCursoFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getValorConsultaCurso(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, Boolean.FALSE, getUsuarioLogado());
            }

            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaCurso(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarCurso() {
        try {
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("curso");
            getAlunoNaoCursouDisciplinaFiltroRelVO().setCursoVO(obj);
            montarListaSelectItemUnidadeEnsino();
            listaConsultaCurso.clear();
            this.setValorConsultaCurso("");
            this.setCampoConsultaCurso("");
        } catch (Exception ex) {
        }
    }

    public void inicializarDadosCurso() throws Exception {
        if (getAlunoNaoCursouDisciplinaFiltroRelVO().getTurmaVO().getCodigo().intValue() > 0 && getAlunoNaoCursouDisciplinaFiltroRelVO().getTurmaVO().getCurso().getCodigo().intValue() > 0) {
            getAlunoNaoCursouDisciplinaFiltroRelVO().setCursoVO(getAlunoNaoCursouDisciplinaFiltroRelVO().getTurmaVO().getCurso());
//            getAlunoNaoCursouDisciplinaFiltroRelVO().setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getAlunoNaoCursouDisciplinaFiltroRelVO().getTurmaVO().getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
    }

    @PostConstruct
    public void montarListaSelectItemUnidadeEnsino(){
        try {
			setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
			getListaSelectItemUnidadeEnsino().add(new SelectItem(0, "TODAS"));

			if (getAlunoNaoCursouDisciplinaFiltroRelVO().getTurmaVO().getUnidadeEnsino().getCodigo() != getUnidadeEnsinoLogado().getCodigo().intValue()
			        && getAlunoNaoCursouDisciplinaFiltroRelVO().getTurmaVO().getCodigo() != 0) {
			    UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getAlunoNaoCursouDisciplinaFiltroRelVO().getTurmaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			    getListaSelectItemUnidadeEnsino().add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getNome()));
			    return;
			}
			if (getUnidadeEnsinoLogado().getCodigo().intValue() > 0) {
			    getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
			    return;
			}
			List<UnidadeEnsinoVO> listUnidadeEnsinoVOs = null;
			if (getAlunoNaoCursouDisciplinaFiltroRelVO().getCursoVO().getCodigo() != 0) {
			    listUnidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigoCurso(getAlunoNaoCursouDisciplinaFiltroRelVO().getCursoVO().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			    return;
			} else {
			    listUnidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome("", 0, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			for (UnidadeEnsinoVO unidadeEnsinoVO : listUnidadeEnsinoVOs) {
			    getListaSelectItemUnidadeEnsino().add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getNome()));
			}
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}


    }

    public List getTipoConsultaComboTurma() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public AlunoNaoCursouDisciplinaFiltroRelVO getAlunoNaoCursouDisciplinaFiltroRelVO() {
        if (alunoNaoCursouDisciplinaFiltroRelVO == null) {
            alunoNaoCursouDisciplinaFiltroRelVO = new AlunoNaoCursouDisciplinaFiltroRelVO();
        }
        return alunoNaoCursouDisciplinaFiltroRelVO;
    }

    public void setAlunoNaoCursouDisciplinaFiltroRelVO(AlunoNaoCursouDisciplinaFiltroRelVO alunoNaoCursouDisciplinaFiltroRelVO) {
        this.alunoNaoCursouDisciplinaFiltroRelVO = alunoNaoCursouDisciplinaFiltroRelVO;
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

    public List<DisciplinaVO> getListaConsultaDisciplina() {
        if (listaConsultaDisciplina == null) {
            listaConsultaDisciplina = new ArrayList<DisciplinaVO>(0);
        }
        return listaConsultaDisciplina;
    }

    public void setListaConsultaDisciplina(List<DisciplinaVO> listaConsultaDisciplina) {
        this.listaConsultaDisciplina = listaConsultaDisciplina;
    }

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>();
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

    public String getValorConsultaTurma() {
        if (valorConsultaTurma == null) {
            valorConsultaTurma = "";
        }
        return valorConsultaTurma;
    }

    public void setValorConsultaTurma(String valorConsultaTurma) {
        this.valorConsultaTurma = valorConsultaTurma;
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

    public List getListaConsultaCurso() {
        if (listaConsultaCurso == null) {
            listaConsultaCurso = new ArrayList(0);
        }
        return listaConsultaCurso;
    }

    public void setListaConsultaCurso(List listaConsultaCurso) {
        this.listaConsultaCurso = listaConsultaCurso;
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
}
