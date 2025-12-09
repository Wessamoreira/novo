package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DisciplinaForaGradeVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.InclusaoDisciplinaForaGradeVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("InclusaoDisciplinaForaGradeControle")
@Scope("viewScope")
@Lazy
public class InclusaoDisciplinaForaGradeControle extends SuperControle implements Serializable {

    private static final long serialVersionUID = 1L;
    private InclusaoDisciplinaForaGradeVO inclusaoDisciplinaForaGradeVO;
    private DisciplinaForaGradeVO disciplinaForaGradeVO;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private List<MatriculaVO> listaConsultaAluno;
    private String valorConsultaCidade;
    private String campoConsultaCidade;
    private List<CidadeVO> listaConsultaCidade;
    private List<SelectItem> listaSelectItemPeriodoLetivo;
    private String campoConsultaDisciplinaIncluida;
    private String valorConsultaDisciplinaIncluida;
    private List listaConsultaDisciplinaIncluida;

    public InclusaoDisciplinaForaGradeControle() {
        setMensagemID("msg_entre_prmconsulta");
    }

    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList<>(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoDisciplinaForaGradeCons.xhtml");
    }

    public String novo() throws Exception {
        removerObjetoMemoria(this);
        setInclusaoDisciplinaForaGradeVO(new InclusaoDisciplinaForaGradeVO());
        setDisciplinaForaGradeVO(new DisciplinaForaGradeVO());
        getInclusaoDisciplinaForaGradeVO().setNovoObj(Boolean.TRUE);
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoDisciplinaForaGradeForm.xhtml");
    }

    public String persistir() {
        try {
            getFacadeFactory().getInclusaoDisciplinaForaGradeFacade().persistir(getInclusaoDisciplinaForaGradeVO(), getUsuarioLogado());
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoDisciplinaForaGradeForm.xhtml");
    }

    public String excluir() {
        try {
            getFacadeFactory().getInclusaoDisciplinaForaGradeFacade().excluir(getInclusaoDisciplinaForaGradeVO(), getUsuarioLogado());
            setInclusaoDisciplinaForaGradeVO(new InclusaoDisciplinaForaGradeVO());
            setDisciplinaForaGradeVO(new DisciplinaForaGradeVO());
            return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoDisciplinaForaGradeForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoDisciplinaForaGradeForm.xhtml");
        }
    }

    public String editar() {
    	InclusaoDisciplinaForaGradeVO obj = (InclusaoDisciplinaForaGradeVO) context().getExternalContext().getRequestMap().get("inclusaoDisciplinaForaGradeItens");
        try {
			getFacadeFactory().getInclusaoDisciplinaForaGradeFacade().carregarDados(obj, NivelMontarDados.TODOS, getUsuarioLogado());
			setInclusaoDisciplinaForaGradeVO(obj);
			getInclusaoDisciplinaForaGradeVO().setMatriculaVO(getFacadeFactory().getInclusaoDisciplinaForaGradeFacade().consultarAlunoPorMatricula(getInclusaoDisciplinaForaGradeVO().getMatriculaVO(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
			getInclusaoDisciplinaForaGradeVO().setNovoObj(Boolean.FALSE);
			montarListaSelectItemPeriodoLetivo();
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoDisciplinaForaGradeForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoDisciplinaForaGradeCons.xhtml");
		}
    }

    public String consultar() {
        try {
            super.consultar();
            setListaConsulta(getFacadeFactory().getInclusaoDisciplinaForaGradeFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoDisciplinaForaGradeCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("inclusaoDisciplinaForaGradeCons.xhtml");
        }
    }

    public void consultarAluno() {
        try {
            setListaConsultaAluno(getFacadeFactory().getInclusaoDisciplinaForaGradeFacade().consultarAluno(getCampoConsultaAluno(), getValorConsultaAluno(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarAlunoPorMatricula() {
        try {
            getInclusaoDisciplinaForaGradeVO().setMatriculaVO(getFacadeFactory().getInclusaoDisciplinaForaGradeFacade().consultarAlunoPorMatricula(getInclusaoDisciplinaForaGradeVO().getMatriculaVO(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
            montarListaSelectItemPeriodoLetivo();
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            getInclusaoDisciplinaForaGradeVO().setMatriculaVO(new MatriculaVO());
        }
    }

    public void selecionarAluno() {
    	try {
    		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
    		obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
    		getInclusaoDisciplinaForaGradeVO().setMatriculaVO(obj);
    		montarListaSelectItemPeriodoLetivo();
    		setValorConsultaAluno("");
    		setCampoConsultaAluno("");
    		getListaConsultaAluno().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }

    public void montarListaSelectItemPeriodoLetivo() throws Exception {
    	getListaSelectItemPeriodoLetivo().clear();
        List<PeriodoLetivoVO> periodoLetivoVOs = getFacadeFactory().getPeriodoLetivoFacade().consultarTodosPeriodosPorMatricula(getInclusaoDisciplinaForaGradeVO().getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        for (PeriodoLetivoVO periodoLetivoVO : periodoLetivoVOs) {
            getListaSelectItemPeriodoLetivo().add(new SelectItem(periodoLetivoVO.getCodigo(), periodoLetivoVO.getDescricao()));
        }
    }

    public void selecionarDisciplinaForaGrade() {
        DisciplinaForaGradeVO obj = (DisciplinaForaGradeVO) context().getExternalContext().getRequestMap().get("disciplinaForaGradeVOItens");
        setDisciplinaForaGradeVO(obj);
    }

    public void validarDadosExistenciaMatriculaPeriodo() throws Exception {
        getFacadeFactory().getMatriculaPeriodoFacade().validarDadosExistenciaMatriculaPeriodo(getInclusaoDisciplinaForaGradeVO().getMatriculaVO().getMatricula(), getDisciplinaForaGradeVO().getAno(), getDisciplinaForaGradeVO().getSemestre(), getUsuarioLogado());
    }

    public void adicionarDisciplinaForaGrade() {
        try {
            getFacadeFactory().getInclusaoDisciplinaForaGradeFacade().adicionarDisciplinaForaGrade(getInclusaoDisciplinaForaGradeVO(), getDisciplinaForaGradeVO(), getUsuarioLogado());
            String instituicaoTmp = getDisciplinaForaGradeVO().getInstituicaoEnsino();
            CidadeVO cidade = new CidadeVO();
            cidade.setCodigo(getDisciplinaForaGradeVO().getCidade().getCodigo());
            cidade.setNome(getDisciplinaForaGradeVO().getCidade().getNome());
            cidade.getEstado().setNome(getDisciplinaForaGradeVO().getCidade().getEstado().getNome());
            cidade.getEstado().setSigla(getDisciplinaForaGradeVO().getCidade().getEstado().getSigla());
            Integer periodoLetivo = getDisciplinaForaGradeVO().getPeriodoLetivo().getCodigo();
            setDisciplinaForaGradeVO(new DisciplinaForaGradeVO());
            getDisciplinaForaGradeVO().setInstituicaoEnsino(instituicaoTmp);
            getDisciplinaForaGradeVO().setCidade(cidade);
            getDisciplinaForaGradeVO().getPeriodoLetivo().setCodigo(periodoLetivo);
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void removerDisciplinaForaGrade() {
        DisciplinaForaGradeVO obj = (DisciplinaForaGradeVO) context().getExternalContext().getRequestMap().get("disciplinaForaGradeVOItens");
        getFacadeFactory().getInclusaoDisciplinaForaGradeFacade().removerDisciplinaForaPrazo(getInclusaoDisciplinaForaGradeVO(), obj, getUsuarioLogado());
    }

    private List<SelectItem> listaSelectItemSituacaoHistorico;
    
    public List<SelectItem> getListaSelectItemSituacaoHistorico() {
    	if (listaSelectItemSituacaoHistorico == null) {
    		listaSelectItemSituacaoHistorico = new ArrayList<SelectItem>(0);
    		listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.APROVADO.getValor(), SituacaoHistorico.APROVADO.getDescricao()));
    		listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.APROVADO_APROVEITAMENTO.getValor(), SituacaoHistorico.APROVADO_APROVEITAMENTO.getDescricao()));
    		listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.REPROVADO.getValor(), SituacaoHistorico.REPROVADO.getDescricao()));
    		listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.REPROVADO_FALTA.getValor(), SituacaoHistorico.REPROVADO_FALTA.getDescricao()));
    		listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.CONCESSAO_CREDITO.getValor(), SituacaoHistorico.CONCESSAO_CREDITO.getDescricao()));
    		listaSelectItemSituacaoHistorico.add(new SelectItem(SituacaoHistorico.CONCESSAO_CARGA_HORARIA.getValor(), SituacaoHistorico.CONCESSAO_CARGA_HORARIA.getDescricao()));
    	}
        return listaSelectItemSituacaoHistorico;
    }

    private List<SelectItem> tipoConsultaComboSemestre;
    
    public List<SelectItem> getTipoConsultaComboSemestre() {
    	if (tipoConsultaComboSemestre == null) {
    		tipoConsultaComboSemestre = new ArrayList<>();
    		tipoConsultaComboSemestre.add(new SelectItem("", ""));
    		tipoConsultaComboSemestre.add(new SelectItem("1", "1º"));
    		tipoConsultaComboSemestre.add(new SelectItem("2", "2º"));
    	}
        return tipoConsultaComboSemestre;
    }
    
    private List<SelectItem> tipoConsultaCombo;

    public List<SelectItem> getTipoConsultaCombo() {
    	if (tipoConsultaCombo == null) {
    		tipoConsultaCombo = new ArrayList<>();
    		tipoConsultaCombo.add(new SelectItem("nome", "Nome"));
    		tipoConsultaCombo.add(new SelectItem("matricula", "Matrícula"));
    	}
        return tipoConsultaCombo;
    }
    
    private List<SelectItem> tipoConsultaComboAluno;

    public List<SelectItem> getTipoConsultaComboAluno() {
    	if (tipoConsultaComboAluno == null) {
    		tipoConsultaComboAluno = new ArrayList<>();
    		tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno"));
    		tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
    		tipoConsultaComboAluno.add(new SelectItem("nomeCurso", "Curso"));
    	}
        return tipoConsultaComboAluno;
    }

    public void consultarCidade() {
        try {
        	setListaConsultaCidade(new ArrayList<>(0));
            if (getCampoConsultaCidade().equals("codigo")) {
                if (getValorConsultaCidade().equals("")) {
                    setValorConsultaCidade("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCidade());
                setListaConsultaCidade(getFacadeFactory().getCidadeFacade().consultarPorCodigo(new Integer(valorInt), false, getUsuarioLogado()));
            } else if (getCampoConsultaCidade().equals("nome")) {
                if (getValorConsultaCidade().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                setListaConsultaCidade(getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado()));
            } else if (getCampoConsultaCidade().equals("estado")) {
            	setListaConsultaCidade(getFacadeFactory().getCidadeFacade().consultarPorSiglaEstado(getValorConsultaCidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCidade(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparCidade() {
        getDisciplinaForaGradeVO().setCidade(null);
    }

    /**
     * Método responsável por selecionar o objeto CidadeVO <code>Cidade/code>.
     */
    public void selecionarCidade() {
        CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
        getDisciplinaForaGradeVO().setCidade(obj);
        getListaConsultaCidade().clear();
        setValorConsultaCidade("");
        setCampoConsultaCidade("");
    }

    /**
     * Método responsável por carregar umaCombobox com os tipos de pesquisa de
     * Cidade <code>Cidade/code>.
     */
    private List<SelectItem> tipoConsultaCidade;
    
    public List<SelectItem> getTipoConsultaCidade() {
    	if (tipoConsultaCidade == null) {
    		tipoConsultaCidade = new ArrayList<>(0);
    		tipoConsultaCidade.add(new SelectItem("nome", "Nome"));
    		tipoConsultaCidade.add(new SelectItem("codigo", "Código"));
    		tipoConsultaCidade.add(new SelectItem("estado", "Sigla Estado"));
    	}
        return tipoConsultaCidade;
    }

    public void limparDadosAluno() throws Exception {
        removerObjetoMemoria(getInclusaoDisciplinaForaGradeVO().getMatriculaVO());
        getListaSelectItemPeriodoLetivo().clear();
    }

    public InclusaoDisciplinaForaGradeVO getInclusaoDisciplinaForaGradeVO() {
        if (inclusaoDisciplinaForaGradeVO == null) {
            inclusaoDisciplinaForaGradeVO = new InclusaoDisciplinaForaGradeVO();
        }
        return inclusaoDisciplinaForaGradeVO;
    }

    public void setInclusaoDisciplinaForaGradeVO(InclusaoDisciplinaForaGradeVO inclusaoDisciplinaForaGradeVO) {
        this.inclusaoDisciplinaForaGradeVO = inclusaoDisciplinaForaGradeVO;
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

    public String getCampoConsultaAluno() {
        if (campoConsultaAluno == null) {
            campoConsultaAluno = "";
        }
        return campoConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    public List<MatriculaVO> getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList<>(0);
        }
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public DisciplinaForaGradeVO getDisciplinaForaGradeVO() {
        if (disciplinaForaGradeVO == null) {
            disciplinaForaGradeVO = new DisciplinaForaGradeVO();
        }
        return disciplinaForaGradeVO;
    }

    public void setDisciplinaForaGradeVO(DisciplinaForaGradeVO disciplinaForaGradeVO) {
        this.disciplinaForaGradeVO = disciplinaForaGradeVO;
    }

    public List<SelectItem> getListaSelectItemPeriodoLetivo() {
        if (listaSelectItemPeriodoLetivo == null) {
            listaSelectItemPeriodoLetivo = new ArrayList<SelectItem>(0);
        }
        return listaSelectItemPeriodoLetivo;
    }

    public void setListaSelectItemPeriodoLetivo(List listaSelectItemPeriodoLetivo) {
        this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
    }

    public Boolean getApresentarAno() {
        return getInclusaoDisciplinaForaGradeVO().getMatriculaVO().getCurso().getPeriodicidade().equals("AN") || getInclusaoDisciplinaForaGradeVO().getMatriculaVO().getCurso().getPeriodicidade().equals("SE");
    }

    public Boolean getApresentarSemestre() {
        return getInclusaoDisciplinaForaGradeVO().getMatriculaVO().getCurso().getPeriodicidade().equals("SE");
    }

    public String getValorConsultaCidade() {
        if (valorConsultaCidade == null) {
            valorConsultaCidade = "";
        }
        return valorConsultaCidade;
    }

    public void setValorConsultaCidade(String valorConsultaCidade) {
        this.valorConsultaCidade = valorConsultaCidade;
    }

    public String getCampoConsultaCidade() {
        if (campoConsultaCidade == null) {
            campoConsultaCidade = "";
        }
        return campoConsultaCidade;
    }

    public void setCampoConsultaCidade(String campoConsultaCidade) {
        this.campoConsultaCidade = campoConsultaCidade;
    }

    public List<CidadeVO> getListaConsultaCidade() {
        if (listaConsultaCidade == null) {
            listaConsultaCidade = new ArrayList<CidadeVO>(0);
        }
        return listaConsultaCidade;
    }

    public void setListaConsultaCidade(List<CidadeVO> listaConsultaCidade) {
        this.listaConsultaCidade = listaConsultaCidade;
    }

    private List<SelectItem> tipoConsultaComboDisciplina;
    
    public List<SelectItem> getTipoConsultaComboDisciplina() {
    	if (tipoConsultaComboDisciplina == null) {
    		tipoConsultaComboDisciplina = new ArrayList<>(0);
    		tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
    		tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
    	}
        return tipoConsultaComboDisciplina;
    }

    /**
     * @return the campoConsultaDisciplinaIncluida
     */
    public String getCampoConsultaDisciplinaIncluida() {
        if (campoConsultaDisciplinaIncluida == null) {
            campoConsultaDisciplinaIncluida = "";
        }
        return campoConsultaDisciplinaIncluida;
    }

    /**
     * @param campoConsultaDisciplinaIncluida
     *            the campoConsultaDisciplinaIncluida to set
     */
    public void setCampoConsultaDisciplinaIncluida(String campoConsultaDisciplinaIncluida) {
        this.campoConsultaDisciplinaIncluida = campoConsultaDisciplinaIncluida;
    }

    /**
     * @return the valorConsultaDisciplinaIncluida
     */
    public String getValorConsultaDisciplinaIncluida() {
        if (valorConsultaDisciplinaIncluida == null) {
            valorConsultaDisciplinaIncluida = "";
        }
        return valorConsultaDisciplinaIncluida;
    }

    /**
     * @param valorConsultaDisciplinaIncluida
     *            the valorConsultaDisciplinaIncluida to set
     */
    public void setValorConsultaDisciplinaIncluida(String valorConsultaDisciplinaIncluida) {
        this.valorConsultaDisciplinaIncluida = valorConsultaDisciplinaIncluida;
    }

    /**
     * @return the listaConsultaDisciplinaIncluida
     */
    public List getListaConsultaDisciplinaIncluida() {
        if (listaConsultaDisciplinaIncluida == null) {
            listaConsultaDisciplinaIncluida = new ArrayList(0);
        }
        return listaConsultaDisciplinaIncluida;
    }

    /**
     * @param listaConsultaDisciplinaIncluida
     *            the listaConsultaDisciplinaIncluida to set
     */
    public void setListaConsultaDisciplinaIncluida(List listaConsultaDisciplinaIncluida) {
        this.listaConsultaDisciplinaIncluida = listaConsultaDisciplinaIncluida;
    }
    
    public void limparDadosDisciplinaIncluida() throws Exception {
        getDisciplinaForaGradeVO().setDisciplinaCadastrada(new DisciplinaVO());
        setListaConsultaDisciplinaIncluida(new ArrayList<>(0));
        setMensagemID("msg_entre_prmconsulta");
    }    
    
    public String getCssNomeDisciplina() {
        if (this.getDisciplinaForaGradeVO().getDisciplinaCadastrada().getCodigo().equals(0)) {
        	return "camposObrigatorios";
        } else {
            return "camposSomenteLeituraObrigatorio";
        }
    }
    
    public void selecionarDisciplinaIncluida() throws Exception {
        DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaIncluidaItens");
        getDisciplinaForaGradeVO().setDisciplinaCadastrada(obj);
        getDisciplinaForaGradeVO().setDisciplina(obj.getNome());
		setValorConsultaDisciplinaIncluida("");
		setCampoConsultaDisciplinaIncluida("");
		setListaConsultaDisciplinaIncluida(new ArrayList<>(0));
    }    

    public void consultarDisciplinaAproveitamento() {
        try {
            if (this.getCampoConsultaDisciplinaIncluida().equals("codigo")) {
                String valorBusca = this.getValorConsultaDisciplinaIncluida();
                if (valorBusca.equals("")) {
                    valorBusca = "0";
                }
                Integer valorBuscaInt = Integer.parseInt(valorBusca);
                setListaConsultaDisciplinaIncluida(getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(valorBuscaInt, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS ,getUsuarioLogado()));
            } else {
                setListaConsultaDisciplinaIncluida(getFacadeFactory().getDisciplinaFacade().consultarPorNome(this.getValorConsultaDisciplinaIncluida(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS ,getUsuarioLogado()));
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaDisciplinaIncluida(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
}
