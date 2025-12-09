package relatorio.controle.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.Uteis;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.EnvelopeRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.EnvelopeRel;

/**
 *
 * @author Carlos
 */
@Controller("EnvelopeRelControle")
@Scope("viewScope")
@Lazy
public class EnvelopeRelControle extends SuperControleRelatorio {

    private UnidadeEnsinoVO unidadeEnsinoVO;
    private MatriculaVO matriculaVO;
    private CidadeVO cidadeVO;
    private TurmaVO turmaVO;
    private String campoConsultaAluno;
    private String valorConsultaAluno;
    private List listaConsultaAluno;
    private List listaSelectItemUnidadeEnsino;
    private String tipoRelatorio;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List listaConsultaTurma;
    private Boolean trazerAlunoAtivo;
    private Integer campoConsultaTipoEnvelope;
    private RequerimentoVO requerimentoVO;

    public EnvelopeRelControle() {
        inicializarListasSelectItemTodosComboBox();
        setTipoRelatorio("aluno");
    }

    private void inicializarDadosControle() {
        inicializarListasSelectItemTodosComboBox();
        setTipoRelatorio("aluno");
    }

    public void imprimirPDF() {
        List<EnvelopeRelVO> listaObjetos = null;
        try {
            registrarAtividadeUsuario(getUsuarioLogado(), "EnvelopeRelControle", "Inicializando Geração de Relatório Modelo de Envelope", "Emitindo Relatório");
            listaObjetos = getFacadeFactory().getEnvelopeRelFacade().executarConsultaParametrizada(getMatriculaVO().getMatricula(), getTurmaVO().getCodigo(), getMatriculaVO().getCurso().getCodigo(), getTipoRelatorio(), getTrazerAlunoAtivo(), getUsuarioLogado());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(EnvelopeRel.getDesignIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(EnvelopeRel.getCaminhoBaseDesignIReportRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório Modelo de Envelope");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(EnvelopeRel.getCaminhoBaseDesignIReportRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
                getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
                getSuperParametroRelVO().setCurso(getMatriculaVO().getCurso().getNome());
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                inicializarDadosControle();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
            registrarAtividadeUsuario(getUsuarioLogado(), "EnvelopeRelControle", "Finalizando Geração de Relatório Modelo de Envelope", "Emitindo Relatório");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }



    public void imprimirRequerimentoPDF() {
        List<EnvelopeRelVO> listaObjetos = null;
        try {

            listaObjetos = getFacadeFactory().getEnvelopeRelFacade().montarDadosEnvelopeRequerimento(getUnidadeEnsinoLogado(), getRequerimentoVO());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(EnvelopeRel.getDesignIReportRelatorioEnvelopeRequerimento());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(EnvelopeRel.getCaminhoBaseDesignIReportRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório Modelo de Envelope Requerimento");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(EnvelopeRel.getCaminhoBaseDesignIReportRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                //getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
                getSuperParametroRelVO().setTurma(getTurmaVO().getIdentificadorTurma());
                getSuperParametroRelVO().setCurso(getMatriculaVO().getCurso().getNome());
                getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
                realizarImpressaoRelatorio();
                removerObjetoMemoria(this);
                inicializarDadosControle();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
            getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
        }
    }


    public void consultarAluno() {
        try {
            getFacadeFactory().getEnvelopeRelFacade().validarDadosConsultaAlunoUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
            List objs = new ArrayList(0);
            if (getValorConsultaAluno().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                if (!obj.getMatricula().equals("")) {
                    objs.add(obj);
                }
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomeCurso")) {
                objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            }

            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarAlunoPorMatricula() throws Exception {
        try {
            getFacadeFactory().getEnvelopeRelFacade().validarDadosConsultaAlunoUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
            MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getMatriculaVO().getMatricula(), this.getUnidadeEnsinoVO().getCodigo(), false,
                    Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            getFacadeFactory().getEnvelopeRelFacade().validarDadosConsultaAluno(objAluno);
            setCidadeVO(getFacadeFactory().getCidadeFacade().consultarPorMatriculaAluno(getMatriculaVO().getMatricula(), false, getUsuarioLogado()));
            setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodo(getMatriculaVO().getMatricula(), getUsuarioLogado()));
            getTurmaVO().setChancelaVO(getFacadeFactory().getChancelaFacade().consultarPorCodigoTurma(getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            setMatriculaVO(objAluno);
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setMatriculaVO(new MatriculaVO());
        }
    }

    public void limparDadosAluno() throws Exception {
        setMatriculaVO(null);
        setTurmaVO(null);
    }
    
    public void limparIdentificador() throws Exception {
        setTurmaVO(null);
    }
    
    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        setMatriculaVO(obj);
        setCidadeVO(getFacadeFactory().getCidadeFacade().consultarPorMatriculaAluno(getMatriculaVO().getMatricula(), false, getUsuarioLogado()));
        setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodo(getMatriculaVO().getMatricula(), getUsuarioLogado()));
        getTurmaVO().setChancelaVO(getFacadeFactory().getChancelaFacade().consultarPorCodigoTurma(getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
        obj = null;
        setValorConsultaAluno("");
        setCampoConsultaAluno("");
        getListaConsultaAluno().clear();
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        return itens;
    }

    public List<SelectItem> getListaSelectItemTipoRelatorio() {
        List<SelectItem> itens = new ArrayList<SelectItem>();
        itens.add(new SelectItem("aluno", "Aluno"));
        itens.add(new SelectItem("turma", "Turma"));
        return itens;
    }

    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemUnidadeEnsino();
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
           // //System.out.println("MENSAGEM => " + e.getMessage());;
        }

    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
                objs.add(new SelectItem("", ""));
            }
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false,
                Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }

    public void consultarCurso() {
        try {
            getFacadeFactory().getEnvelopeRelFacade().validarDadosConsultaAlunoUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
            List objs = new ArrayList(0);
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarCurso() {
        try {
            UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItens");
            getMatriculaVO().setCurso(obj.getCurso());
            listaConsultaCurso.clear();
            this.setValorConsultaCurso("");
            this.setCampoConsultaCurso("");
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaComboTurma() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("identificadorTurma", "Identificador"));
        itens.add(new SelectItem("nomeTurno", "Turno"));
        return itens;
    }


    public List<SelectItem> getTipoConsultaComboTipoEnvelope() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem(0, "Envelope Documentos"));
        itens.add(new SelectItem(1, "Envelope Requerimento"));
        return itens;
    }

    public void consultarTurma() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaTurma().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaTurma().equals("codigo")) {
                if (getValorConsultaTurma().equals("")) {
                    setValorConsultaCurso("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaTurma());
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorCodigoTurmaCursoUnidadeEnsino(new Integer(valorInt), getMatriculaVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), getMatriculaVO().getCurso().getCodigo(), getMatriculaVO().getTurno().getCodigo(), false, getUsuarioLogado());

            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaTurma(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() throws Exception {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
            setTurmaVO(obj);
            getMatriculaVO().setCurso(obj.getCurso());
            obj = null;
            setValorConsultaTurma("");
            setCampoConsultaTurma("");
            getListaConsultaTurma().clear();
        } catch (Exception e) {
            setTurmaVO(new TurmaVO());
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarRequerimento() {
        try {
            RequerimentoVO obj = (RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItens");
            obj = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
            setRequerimentoVO(obj);

            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Requerimento</code> por meio de sua respectiva
     * chave primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave
     * primária da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarRequerimentoPorChavePrimaria() {
        try {
            Integer campoConsulta = getRequerimentoVO().getCodigo();
            RequerimentoVO requerimento = getFacadeFactory().getRequerimentoFacade().consultarPorChavePrimariaFiltrandoPorUnidadeEnsino(campoConsulta, "",
                    getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());

            setRequerimentoVO(requerimento);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
        }
    }
    
	public void consultarRequerimento() {
		try {

			List objs = new ArrayList(0);
			if (getCampoConsultaRequerimento().equals("codigo")) {
				int valorInt = Uteis.getValorInteiro(getValorConsultaRequerimento());
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorCodigo(new Integer(valorInt), "", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("data")) {
				Date valorData = Uteis.getDate(getValorConsultaRequerimento());
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), "", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("nomeTipoRequerimento")) {
				if (getValorConsultaRequerimento().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorNomeTipoRequerimento(getValorConsultaRequerimento(), this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("situacao")) {
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorSituacao(getValorConsultaRequerimento(), "", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("situacaoFinanceira")) {
				if (getValorConsultaRequerimento().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorSituacaoFinanceira(getValorConsultaRequerimento(), "", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("nomePessoa")) {
				if (getValorConsultaRequerimento().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorNomePessoa(getValorConsultaRequerimento(), "", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("cpfPessoa")) {
				if (getValorConsultaRequerimento().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorNomeCPFPessoa(getValorConsultaRequerimento(), "", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getConfiguracaoFinanceiroPadraoSistema());
			}
			if (getCampoConsultaRequerimento().equals("matriculaMatricula")) {
				if (getValorConsultaRequerimento().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getRequerimentoFacade().consultarPorMatriculaMatricula(getValorConsultaRequerimento(), "", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null, getConfiguracaoFinanceiroPadraoSistema());
			}

			setListaConsultaRequerimento(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaRequerimento(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

    
    public boolean getIsReadOnly() {
        return getTipoRelatorio().equals("aluno");
    }
    
    public boolean getIsApresentraDadosTurma() {
        return getTipoRelatorio().equals("turma");
    }
    
    public String getStyleClassApresentar() {
        if (getTipoRelatorio().equals("aluno")) {
            return "camposSomenteLeitura";
        }
        return "campos";
    }

    public MatriculaVO getMatriculaVO() {
        if (matriculaVO == null) {
            matriculaVO = new MatriculaVO();
        }
        return matriculaVO;
    }

    public void setMatriculaVO(MatriculaVO matriculaVO) {
        this.matriculaVO = matriculaVO;
    }

    public String getCampoConsultaAluno() {
        if (campoConsultaAluno == null) {
            campoConsultaAluno = "";
        }
        return campoConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    public String getValorConsultaAluno() {
        if (valorConsultaAluno == null) {
            valorConsultaAluno = "";
        }
        return valorConsultaAluno;
    }

    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

    public List getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList(0);
        }
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
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

    public CidadeVO getCidadeVO() {
        if (cidadeVO == null) {
            cidadeVO = new CidadeVO();
        }
        return cidadeVO;
    }

    public void setCidadeVO(CidadeVO cidadeVO) {
        this.cidadeVO = cidadeVO;
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
            listaConsultaTurma = new ArrayList(0);
        }
        return listaConsultaTurma;
    }

    public void setListaConsultaTurma(List listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;
    }

    public Boolean getTrazerAlunoAtivo() {
        if (trazerAlunoAtivo == null) {
            trazerAlunoAtivo = Boolean.TRUE;
        }
        return trazerAlunoAtivo;
    }

    public void setTrazerAlunoAtivo(Boolean trazerAlunoAtivo) {
        this.trazerAlunoAtivo = trazerAlunoAtivo;
    }

    public Integer getCampoConsultaTipoEnvelope() {
        if (campoConsultaTipoEnvelope == null){
            campoConsultaTipoEnvelope = 0;
        }
        return campoConsultaTipoEnvelope;
    }

    public void setCampoConsultaTipoEnvelope(Integer campoConsultaTipoEnvelope) {
        this.campoConsultaTipoEnvelope = campoConsultaTipoEnvelope;
    }

    public RequerimentoVO getRequerimentoVO() {
        if (requerimentoVO == null){
            requerimentoVO = new RequerimentoVO();
        }
        return requerimentoVO;
    }

    public void setRequerimentoVO(RequerimentoVO requerimentoVO) {
        this.requerimentoVO = requerimentoVO;
    }

    public Boolean getMostrarAluno() {
        if (getRequerimentoVO() != null){
            if (!getRequerimentoVO().getMatricula().getMatricula().equals("")){
                return true;
            }
        }
        return false;
    }

    public String getApresentarNomeRequerimento() {
        if (getRequerimentoVO().getCodigo() != null && getRequerimentoVO().getCodigo().intValue() != 0 && getRequerimentoVO().getTipoRequerimento().getCodigo() != null && getRequerimentoVO().getTipoRequerimento().getCodigo().intValue() != 0){
            return getRequerimentoVO().getTipoRequerimento().getNome() + " - " + getRequerimentoVO().getData_Apresentar();
        }
        return "";
    }

   public void limparDadosRequerimento() {
        setRequerimentoVO(new RequerimentoVO());
    }



}
