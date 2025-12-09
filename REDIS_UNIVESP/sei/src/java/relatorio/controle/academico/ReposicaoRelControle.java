package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;


import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.ReposicaoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.ReposicaoRel;

@SuppressWarnings("unchecked")
@Controller("ReposicaoRelControle")
@Scope("viewScope")
@Lazy
public class ReposicaoRelControle extends SuperControleRelatorio {

    private UnidadeEnsinoVO unidadeEnsinoVO;
    private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
    private CursoVO cursoVO;
    private Date dataFim;
    private Date dataInicio;
    private Date dataPgtoInicio;
    private Date dataPgtoFim;
    private Date dataInclusaoInicio;
    private Date dataInclusaoFim;
    private Date dataAulaInicio;
    private Date dataAulaFim;
    private String tipo;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;
    private TurmaVO turmaVO;
    private List listaSelectItemUnidadeEnsino;
    private List listaSelectItemCurso;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List listaConsultaTurma;
    private DisciplinaVO disciplina;
    private List listaSelectItemDisciplina;
    private FuncionarioVO funcionarioResponsavel;
    private String campoConsultaFuncionarioResponsavel;
    private String valorConsultaFuncionarioResponsavel;
    private List listaConsultaFuncionarioResponsavel;

    public ReposicaoRelControle() throws Exception {
        incializarDados();
        setMensagemID("msg_entre_prmconsulta");
    }

    public void incializarDados() {
        montarListaSelectItemUnidadeEnsino();
    }

    public void imprimirPDF() {

        String titulo = getTipo().equals("RE") ? "REPOSIÇÃO" : "INCLUSÃO";
        String nomeEntidade = super.getUnidadeEnsinoLogado().getNome();
        String design = "";
        design = ReposicaoRel.getDesignIReportRelatorio();

        List listaObjs = null;

        try {
        	if (getDataInicio() == null || getDataFim() == null) {
        		if (getDataPgtoInicio() == null || getDataPgtoFim() == null) {
        			throw new Exception("Deve ser informado ao menos um período de filtro no relatório( Data Inclusão ou Data Pgto).");
        		}
        	} else if (getDataPgtoInicio() == null || getDataPgtoFim() == null) {
        		if (getDataInicio() == null || getDataFim() == null) {
        			throw new Exception("Deve ser informado ao menos um período de filtro no relatório( Data Inclusão ou Data Pgto).");
        		}        		
        	}
            ReposicaoRel.validarDados(getUnidadeEnsinoVO(), getCursoVO());
            if (getDisciplina() != null && getDisciplina().getCodigo() != 0) {
                setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(disciplina.getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));

            }

            if (getUnidadeEnsinoCursoVO().getCurso().getCodigo().intValue() != 0 && getUnidadeEnsinoCursoVO().getUnidadeEnsino().intValue() != 0) {
                setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCursoUnidadeEnsino(getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoCursoVO().getUnidadeEnsino(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            }
            getFacadeFactory().getReposicaoRelFacade().setDescricaoFiltros("");

            listaObjs = getFacadeFactory().getReposicaoRelFacade().criarObjeto(getUnidadeEnsinoVO(), getUnidadeEnsinoCursoVO().getCurso(), getTurmaVO(), getDisciplina(), getDataInicio(), getDataFim(), getTipo(), getFuncionarioResponsavel().getCodigo(), getDataAulaInicio(), getDataAulaFim(), getDataInclusaoInicio(), getDataInclusaoFim(), getDataPgtoInicio(), getDataPgtoFim(), getUsuarioLogado());
            int qtdCursou = 0;
            int qtdNCursou = 0;
            int total = 0;
            Iterator i = listaObjs.iterator();
            while (i.hasNext()) {
                ReposicaoRelVO re = (ReposicaoRelVO)i.next();
                if (re.getSituacao().equals("Faltou")) {
                    qtdNCursou++;
                } else if (re.getSituacao().equals("Cursou")) {
                    qtdCursou++;
                } else {
                    total++;
                }
            }
            apresentarRelatorioObjetos(ReposicaoRel.getIdEntidade(), titulo, nomeEntidade, "", "PDF", "", design, getUsuarioLogado().getNome(), getFacadeFactory().getReposicaoRelFacade().getDescricaoFiltros(), listaObjs, ReposicaoRel.getCaminhoBaseRelatorio());
            if (!listaObjs.isEmpty()) {
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            titulo = null;
            nomeEntidade = null;
            design = null;
            Uteis.liberarListaMemoria(listaObjs);
        }
    }

    public void imprimirRelPDF() throws Exception {
        List listaObjs = null;
        try {
        	if (getDataInicio() == null || getDataFim() == null) {
        		if (getDataPgtoInicio() == null || getDataPgtoFim() == null) {
        			throw new Exception("Deve ser informado ao menos um período de filtro no relatório( Data Inclusão ou Data Pgto).");
        		}
        	} else if (getDataPgtoInicio() == null || getDataPgtoFim() == null) {
        		if (getDataInicio() == null || getDataFim() == null) {
        			throw new Exception("Deve ser informado ao menos um período de filtro no relatório( Data Inclusão ou Data Pgto).");
        		}        		
        	}        	
            ReposicaoRel.validarDados(getUnidadeEnsinoVO(), getCursoVO());
            if (getUnidadeEnsinoCursoVO().getCurso().getCodigo().intValue() != 0 && getUnidadeEnsinoCursoVO().getUnidadeEnsino().intValue() != 0) {
                setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCursoUnidadeEnsino(getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            }
            listaObjs = getFacadeFactory().getReposicaoRelFacade().criarObjeto(getUnidadeEnsinoVO(), getUnidadeEnsinoCursoVO().getCurso(), getTurmaVO(), getDisciplina(), getDataInicio(), getDataFim(), getTipo(), getFuncionarioResponsavel().getCodigo(), getDataAulaInicio(), getDataAulaFim(), getDataInclusaoInicio(), getDataInclusaoFim(), getDataPgtoInicio(), getDataPgtoFim(), getUsuarioLogado());
            int qtdCursou = 0;
            int qtdNCursou = 0;
            int qtdAindaNCursou = 0;
            int total = 0;
            Iterator i = listaObjs.iterator();
            while (i.hasNext()) {
                ReposicaoRelVO re = (ReposicaoRelVO)i.next();
                if (re.getSituacao().equals("Faltou/Não Cursou")) {
                    qtdNCursou++;
                } else if (re.getSituacao().equals("Cursou")) {
                    qtdCursou++;
                } else {
                    qtdAindaNCursou++;
                }
            }
            total = qtdNCursou + qtdCursou + qtdAindaNCursou;
            if (!listaObjs.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(ReposicaoRel.getDesignIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                String titulo = getTipo().equals("RE") ? "REPOSIÇÃO" : "INCLUSÃO";
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setListaObjetos(listaObjs);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(ReposicaoRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());

                getSuperParametroRelVO().setQuantidade(total);
                getSuperParametroRelVO().setQuantidadeAindaNCursou(qtdAindaNCursou);
                getSuperParametroRelVO().setQuantidadeCursou(qtdCursou);
                getSuperParametroRelVO().setQuantidadeNCursou(qtdNCursou);
                getSuperParametroRelVO().adicionarParametro("consultor", getFuncionarioResponsavel().getPessoa().getNome());
                getSuperParametroRelVO().adicionarParametro("periodoAula", Uteis.getData(getDataAulaInicio())+" à "+Uteis.getData(getDataAulaFim()));
                getSuperParametroRelVO().adicionarParametro("periodoPagamento", Uteis.getData(getDataPgtoInicio())+" à "+Uteis.getData(getDataPgtoFim()));
                getSuperParametroRelVO().adicionarParametro("periodoInclusao", Uteis.getData(getDataInicio())+" à "+Uteis.getData(getDataFim()));
                if(Uteis.isAtributoPreenchido(getTurmaVO())) {
                	getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
                }
                if(Uteis.isAtributoPreenchido(getDisciplina())) {
                	setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
                	getSuperParametroRelVO().setDisciplina(getDisciplina().getNome());
                }
                if (getCursoVO().getCodigo() > 0) {
                    CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
                    getSuperParametroRelVO().setCurso(curso.getNome());
                } else {
                    getSuperParametroRelVO().setCurso("Todas");
                }
                if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
               		setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
               		getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
               		getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
                }
                
                realizarImpressaoRelatorio();
                setMensagemID("msg_relatorio_ok");

            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }
    
    public void imprimirRelExcel() throws Exception {
        List listaObjs = null;
        try {
        	if (getDataInicio() == null || getDataFim() == null) {
        		if (getDataPgtoInicio() == null || getDataPgtoFim() == null) {
        			throw new Exception("Deve ser informado ao menos um período de filtro no relatório( Data Inclusão ou Data Pgto).");
        		}
        	} else if (getDataPgtoInicio() == null || getDataPgtoFim() == null) {
        		if (getDataInicio() == null || getDataFim() == null) {
        			throw new Exception("Deve ser informado ao menos um período de filtro no relatório( Data Inclusão ou Data Pgto).");
        		}        		
        	}
            ReposicaoRel.validarDados(getUnidadeEnsinoVO(), getCursoVO());
            if (getUnidadeEnsinoCursoVO().getCurso().getCodigo().intValue() != 0 && getUnidadeEnsinoCursoVO().getUnidadeEnsino().intValue() != 0) {
                setUnidadeEnsinoCursoVO(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCursoUnidadeEnsino(getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            }
            listaObjs = getFacadeFactory().getReposicaoRelFacade().criarObjeto(getUnidadeEnsinoVO(), getUnidadeEnsinoCursoVO().getCurso(), getTurmaVO(), getDisciplina(), getDataInicio(), getDataFim(), getTipo(), getFuncionarioResponsavel().getCodigo(), getDataAulaInicio(), getDataAulaFim(), getDataInclusaoInicio(), getDataInclusaoFim(), getDataPgtoInicio(), getDataPgtoFim(), getUsuarioLogado());
            int qtdCursou = 0;
            int qtdNCursou = 0;
            int qtdAindaNCursou = 0;
            int total = 0;
            Iterator i = listaObjs.iterator();
            while (i.hasNext()) {
                ReposicaoRelVO re = (ReposicaoRelVO)i.next();
                if (re.getSituacao().equals("Faltou/Não Cursou")) {
                    qtdNCursou++;
                } else if (re.getSituacao().equals("Cursou")) {
                    qtdCursou++;
                } else {
                    qtdAindaNCursou++;
                }
            }
            total = qtdNCursou + qtdCursou + qtdAindaNCursou;
            if (!listaObjs.isEmpty()) {
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setNomeDesignIreport(ReposicaoRel.getDesignIReportRelatorioLayoutExcel());
                getSuperParametroRelVO().setSubReport_Dir(ReposicaoRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                String titulo = getTipo().equals("RE") ? "REPOSIÇÃO" : "INCLUSÃO";
                getSuperParametroRelVO().setTituloRelatorio(titulo);
                getSuperParametroRelVO().setListaObjetos(listaObjs);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(ReposicaoRel.getCaminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().adicionarParametro("consultor", getFuncionarioResponsavel().getPessoa().getNome());
                getSuperParametroRelVO().adicionarParametro("periodoAula", Uteis.getData(getDataAulaInicio())+" à "+Uteis.getData(getDataAulaFim()));
                getSuperParametroRelVO().adicionarParametro("periodoPagamento", Uteis.getData(getDataPgtoInicio())+" à "+Uteis.getData(getDataPgtoFim()));
                getSuperParametroRelVO().adicionarParametro("periodoInclusao", Uteis.getData(getDataInicio())+" à "+Uteis.getData(getDataFim()));
                if(Uteis.isAtributoPreenchido(getTurmaVO())) {
                	getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
                }
                if(Uteis.isAtributoPreenchido(getDisciplina())) {
                	setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
                	getSuperParametroRelVO().setDisciplina(getDisciplina().getNome());
                }
                if (getCursoVO().getCodigo() > 0) {
                    CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getCursoVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
                    getSuperParametroRelVO().setCurso(curso.getNome());
                } else {
                    getSuperParametroRelVO().setCurso("Todas");
                }
                if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
               		setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
               		getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
               		getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
                }

                realizarImpressaoRelatorio();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List getListaSelectItemTipo() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("RE", "Reposição"));
        lista.add(new SelectItem("IN", "Inclusão"));
        return lista;
    }

    public void consultarFuncionarioResponsavel() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaFuncionarioResponsavel().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaFuncionarioResponsavel().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionarioResponsavel(), "", getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionarioResponsavel().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionarioResponsavel(), getUnidadeEnsinoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionarioResponsavel().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionarioResponsavel(), "", getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionarioResponsavel().equals("cargo")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionarioResponsavel(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionarioResponsavel().equals("departamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionarioResponsavel(), "FU", getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionarioResponsavel().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionarioResponsavel(), "FU", getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaFuncionarioResponsavel(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaFuncionarioResponsavel(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
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

    public void selecionarFuncionarioResponsavel() {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioResponsavelItens");
        setFuncionarioResponsavel(obj);
    }

    public void limparConsultaFuncionarioResponsavel() {
    	getListaConsultaFuncionarioResponsavel().clear();
        setValorConsultaFuncionarioResponsavel("");        
    }


    public void limparDadosFuncionarioResponsavel() {
        setFuncionarioResponsavel(new FuncionarioVO());
    }

    private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemCurso() throws Exception {
        List<UnidadeEnsinoCursoVO> resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarCursoPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
            setListaSelectItemCurso(new ArrayList(0));
            i = resultadoConsulta.iterator();
            getListaSelectItemCurso().add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) i.next();
                getListaSelectItemCurso().add(new SelectItem(unidadeEnsinoCurso.getCodigo(), unidadeEnsinoCurso.getNomeCursoTurno()));
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    private List<UnidadeEnsinoCursoVO> consultarCursoPorUnidadeEnsino(Integer codigoUnidadeEnsino) throws Exception {
        List<UnidadeEnsinoCursoVO> lista = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoUnidadeEnsino(codigoUnidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        return lista;
    }

    // public void montarListaSelectItemCurso() throws Exception {
    // try {
    // List<CursoVO> resultadoConsulta =
    // consultarCursoPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
    // setListaSelectItemCurso(UtilSelectItem.getListaSelectItem(resultadoConsulta,
    // "codigo", "nome"));
    // } catch (Exception e) {
    // setMensagemDetalhada("msg_erro", e.getMessage());
    // }
    // }
    //
    // private List<CursoVO> consultarCursoPorUnidadeEnsino(Integer
    // codigoUnidadeEnsino) throws Exception {
    // List<CursoVO> lista =
    // getFacadeFactory().getCursoFacade().consultarPorUnidadeEnsino(codigoUnidadeEnsino,
    // false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS);
    // return lista;
    // }
    public List getListaSelectItemSemestre() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("", ""));
        lista.add(new SelectItem("1", "1º"));
        lista.add(new SelectItem("2", "2º"));
        return lista;
    }

    public List getListaSelectItemSituacao() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("", ""));
        lista.add(new SelectItem("AT", "Matriculados"));
        lista.add(new SelectItem("PR", "Pré-Matriculados"));
        return lista;
    }

    public void consultarCurso() {
        try {

            List objs = new ArrayList(0);
            if (getCampoConsultaCurso().equals("codigo")) {
                if (getValorConsultaCurso().equals("")) {
                    setValorConsultaCurso("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCurso());
                objs = getFacadeFactory().getCursoFacade().consultarCursoPorCodigoUnidadeEnsino(valorInt, getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());

            }

            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getCursoFacade().consultarPorNomeCursoUnidadeEnsinoBasica(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void consultarTurma() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaTurma().equals("codigo")) {
                if (getCampoConsultaTurma().equals("")) {
                    setValorConsultaTurma("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaTurma());
                objs = getFacadeFactory().getTurmaFacade().consultarPorCodigoTurmaCursoEUnidadeEnsino(valorInt, getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nome")) {
                objs = getFacadeFactory().getTurmaFacade().consultarPorNomeTurmaCursoEUnidadeEnsino(getValorConsultaTurma(), getCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaTurma(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarCurso() throws Exception {
        try {
            // UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO)
            // context().getExternalContext().getRequestMap().get("curso");
            // setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCurso().getCodigo(),
            // Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            // getUnidadeEnsinoCursoVO().setCurso(getCursoVO());
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
            setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
            getUnidadeEnsinoCursoVO().setCurso(getCursoVO());
            montarListaSelectItemDisciplinaPorCurso();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() throws Exception {
        try {
            TurmaVO turma = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
            setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turma.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			if (getTurmaVO().getSubturma()) {
				setCursoVO(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			} else if (getTurmaVO().getTurmaAgrupada()) {
				setCursoVO(new CursoVO());
			} else {
				setCursoVO(getTurmaVO().getCurso());
			}
            montarListaSelectItemDisciplina();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparCurso() throws Exception {
        try {
            setCursoVO(new CursoVO());
            limparTurma();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
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

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
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

    public TurmaVO getTurmaVO() {
        if (turmaVO == null) {
            turmaVO = new TurmaVO();
        }
        return turmaVO;
    }

    public void setTurmaVO(TurmaVO turmaVO) {
        this.turmaVO = turmaVO;
    }

    public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
        this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
    }

    public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
        if (unidadeEnsinoCursoVO == null) {
            unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
        }
        return unidadeEnsinoCursoVO;
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
            listaConsultaTurma = new ArrayList<TurmaVO>();
        }
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

    public List getTipoConsultaComboTurma() {
        if (tipoConsultaComboCurso == null) {
            tipoConsultaComboCurso = new ArrayList(0);
            tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboCurso.add(new SelectItem("codigo", "Código"));
        }
        return tipoConsultaComboCurso;
    }

    public void limparTurma() throws Exception {
        try {
            setTurmaVO(new TurmaVO());
            setDisciplina(new DisciplinaVO());
        } catch (Exception e) {
        }
    }

    public void limparDados() {
        getUnidadeEnsinoCursoVO().setUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
        setCursoVO(new CursoVO());
        setTurmaVO(new TurmaVO());
    }

    public Boolean getApresentarDisciplina() {
        return (getTurmaVO() != null && getTurmaVO().getCodigo() != 0) || (getCursoVO() != null && getCursoVO().getCodigo() != 0);
    }

    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
    }

    public List getListaSelectItemDisciplina() {
        if (listaSelectItemDisciplina == null) {
            listaSelectItemDisciplina = new ArrayList<DisciplinaVO>();
        }
        return listaSelectItemDisciplina;
    }

    public void setListaSelectItemDisciplina(List listaSelectItemDisciplina) {
        this.listaSelectItemDisciplina = listaSelectItemDisciplina;
    }

    public void montarListaSelectItemDisciplina() throws Exception {
        try {
            setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
            listaSelectItemDisciplina.add(new SelectItem(0, ""));
            List<DisciplinaVO> listaDisciplina = getFacadeFactory().getDisciplinaFacade().consutlarDisciplinaPorCodigoTurma(getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());

            for (DisciplinaVO disc : listaDisciplina) {
                listaSelectItemDisciplina.add(new SelectItem(disc.getCodigo(), disc.getCodigo() + " - " + disc.getNome()));
            }
        } catch (Exception e) {
            setMensagemID("msg_entre_prmconsulta");
        }
    }

    public void montarListaSelectItemDisciplinaPorCurso() throws Exception {
        try {
            setListaSelectItemDisciplina(new ArrayList<SelectItem>(0));
            listaSelectItemDisciplina.add(new SelectItem(0, ""));
            List<DisciplinaVO> listaDisciplina = getFacadeFactory().getDisciplinaFacade().consutlarDisciplinaPorCurso(getCursoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());

            for (DisciplinaVO disc : listaDisciplina) {
                listaSelectItemDisciplina.add(new SelectItem(disc.getCodigo(), disc.getCodigo() + " - " + disc.getNome()));
            }
        } catch (Exception e) {
            setMensagemID("msg_entre_prmconsulta");
        }
    }

    public Boolean getApresentarNivelEducacionalPosGraduacao() {
        return (getCursoVO() != null && getCursoVO().getCodigo() != 0 && getCursoVO().getNivelEducacional().equals("PO") || getCursoVO().getNivelEducacional().equals("EX"));
    }

    public Boolean getApresentarTurma() {
        return (getUnidadeEnsinoCursoVO() != null && getUnidadeEnsinoCursoVO().getUnidadeEnsino() != 0);
    }

    /**
     * @return the dataFim
     */
    public Date getDataFim() {
//        if (dataFim == null) {
//            dataFim = Uteis.getDataUltimoDiaMes(new Date());
//        }
        return dataFim;
    }

    /**
     * @param dataFim the dataFim to set
     */
    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    /**
     * @return the dataInicio
     */
    public Date getDataInicio() {
//        if (dataInicio == null) {
//            dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
//        }
        return dataInicio;
    }

    /**
     * @param dataInicio the dataInicio to set
     */
    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        if (tipo == null) {
            tipo = "RE";
        }
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

	public FuncionarioVO getFuncionarioResponsavel() {
		if (funcionarioResponsavel == null) {
			funcionarioResponsavel = new  FuncionarioVO();
		}
		return funcionarioResponsavel;
	}

	public void setFuncionarioResponsavel(FuncionarioVO funcionarioResponsavel) {
		this.funcionarioResponsavel = funcionarioResponsavel;
	}
	
    /**
     * @return the campoConsultaFuncionarioResponsavel
     */
    public String getCampoConsultaFuncionarioResponsavel() {
        if (campoConsultaFuncionarioResponsavel == null) {
            campoConsultaFuncionarioResponsavel = "";
        }
        return campoConsultaFuncionarioResponsavel;
    }

    /**
     * @param campoConsultaFuncionarioResponsavel the campoConsultaFuncionarioResponsavel to set
     */
    public void setCampoConsultaFuncionarioResponsavel(String campoConsultaFuncionarioResponsavel) {
        this.campoConsultaFuncionarioResponsavel = campoConsultaFuncionarioResponsavel;
    }

    /**
     * @return the valorConsultaFuncionarioResponsavel
     */
    public String getValorConsultaFuncionarioResponsavel() {
        if (valorConsultaFuncionarioResponsavel == null) {
            valorConsultaFuncionarioResponsavel = "";
        }
        return valorConsultaFuncionarioResponsavel;
    }

    /**
     * @param valorConsultaFuncionarioResponsavel the valorConsultaFuncionarioResponsavel to set
     */
    public void setValorConsultaFuncionarioResponsavel(String valorConsultaFuncionarioResponsavel) {
        this.valorConsultaFuncionarioResponsavel = valorConsultaFuncionarioResponsavel;
    }

    /**
     * @return the listaConsultaFuncionarioResponsavel
     */
    public List getListaConsultaFuncionarioResponsavel() {
        if (listaConsultaFuncionarioResponsavel == null) {
            listaConsultaFuncionarioResponsavel = new ArrayList(0);
        }
        return listaConsultaFuncionarioResponsavel;
    }

    /**
     * @param listaConsultaFuncionarioResponsavel the listaConsultaFuncionarioResponsavel to set
     */
    public void setListaConsultaFuncionarioResponsavel(List listaConsultaFuncionarioResponsavel) {
        this.listaConsultaFuncionarioResponsavel = listaConsultaFuncionarioResponsavel;
    }

	public Date getDataPgtoInicio() {
		return dataPgtoInicio;
	}

	public void setDataPgtoInicio(Date dataPgtoInicio) {
		this.dataPgtoInicio = dataPgtoInicio;
	}

	public Date getDataPgtoFim() {
		return dataPgtoFim;
	}

	public void setDataPgtoFim(Date dataPgtoFim) {
		this.dataPgtoFim = dataPgtoFim;
	}

	public Date getDataInclusaoInicio() {
		return dataInclusaoInicio;
	}

	public void setDataInclusaoInicio(Date dataInclusaoInicio) {
		this.dataInclusaoInicio = dataInclusaoInicio;
	}

	public Date getDataInclusaoFim() {
		return dataInclusaoFim;
	}

	public void setDataInclusaoFim(Date dataInclusaoFim) {
		this.dataInclusaoFim = dataInclusaoFim;
	}

	public Date getDataAulaInicio() {
		return dataAulaInicio;
	}

	public void setDataAulaInicio(Date dataAulaInicio) {
		this.dataAulaInicio = dataAulaInicio;
	}

	public Date getDataAulaFim() {
		return dataAulaFim;
	}

	public void setDataAulaFim(Date dataAulaFim) {
		this.dataAulaFim = dataAulaFim;
	}

}
