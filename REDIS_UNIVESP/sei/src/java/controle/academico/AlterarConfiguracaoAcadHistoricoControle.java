package controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@SuppressWarnings("unchecked")
@Controller("AlterarConfiguracaoAcadHistoricoControle")
@Scope("request")
@Lazy
public class AlterarConfiguracaoAcadHistoricoControle extends SuperControle {

    private UnidadeEnsinoVO unidadeEnsino;
    private CursoVO curso;
    private DisciplinaVO disciplina;
    private TurmaVO turma;
    private MatriculaVO matricula;
    private List listaSelectItemUnidadeEnsino;
    private String campoConsultaDisciplina;
    private String valorConsultaDisciplina;
    private List listaConsultaDisciplina;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;
    private Date dataInicio;
    private Date dataFim;
    private List listaConsultaTurma;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private String nivelEducacional;
    private String ordenacao;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private List listaConsultaAluno;

    
    public AlterarConfiguracaoAcadHistoricoControle() throws Exception {
        incializarDados();
        setMensagemID("msg_entre_prmconsulta");
    }

    public void limparListasConsultas() {
        removerObjetoMemoria(getDisciplina());
        removerObjetoMemoria(getCurso());
        removerObjetoMemoria(getTurma());
        getListaConsultaCurso().clear();
        getListaConsultaDisciplina().clear();
    }

    private void incializarDados() {
        montarListaSelectItemUnidadeEnsino();
    }

    public void selecionarNivelEducacional() {
        setUnidadeEnsino(null);
        setCurso(null);
        setTurma(null);
        setDisciplina(null);
    }
    
    public List getListaSelectItemSemestre() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("1", "1º"));
        lista.add(new SelectItem("2", "2º"));
        return lista;
    }
    

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					objs.add(obj);
				}
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatricula().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			this.setMatricula(objAluno);
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			this.setMatricula(new MatriculaVO());
		}
	}
	
	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matricula");
		MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
		setMatricula(objCompleto);
//		setDisciplina(new DisciplinaVO());
//		setAlunosTurma(new ArrayList<HistoricoVO>(0));
//		setListaSelectItemTurma(new ArrayList(0));
		obj = null;
		objCompleto = null;
		setValorConsultaAluno("");
		setCampoConsultaAluno("");
		getListaConsultaAluno().clear();
	}
    
	public Boolean getApresentarAno() {
		return ((this.getCurso().getPeriodicidade().equals("AN") || this.getCurso().getPeriodicidade().equals("SE")) && !this.getCurso().getCodigo().equals(0));
	}

	public Boolean getApresentarSemestre() {
		return ((this.getCurso().getPeriodicidade().equals("AN") || this.getCurso().getPeriodicidade().equals("SE")) && !this.getCurso().getCodigo().equals(0));
	}
    

    public void montarListaSelectItemUnidadeEnsino() {
        List<UnidadeEnsinoVO> resultadoConsulta = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome("");
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
        }
    }

    private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm,
                super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void consultarDisciplina() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaDisciplina().equals("codigo")) {
                if (getValorConsultaDisciplina().equals("")) {
                    setValorConsultaDisciplina("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaDisciplina());
                objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaDisciplina().equals("nome")) {
            	objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaDisciplina(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaDisciplina(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarDisciplina() throws Exception {
        try {
            DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplina");
            setDisciplina(obj);
        } catch (Exception e) {
        }
    }

    public void limparDisciplina() throws Exception {
        try {
            setDisciplina(null);
        } catch (Exception e) {
        }
    }

    public void consultarTurma() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsino().getCodigo(), getCurso().getCodigo(), 0, false, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaTurma(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() throws Exception {
        TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turma");
        setTurma(obj);
        setCurso(obj.getCurso());
        setUnidadeEnsino(obj.getUnidadeEnsino());
        removerObjetoMemoria(getDisciplina());
        obj = null;
        setValorConsultaTurma(null);
        setCampoConsultaTurma(null);
        Uteis.liberarListaMemoria(getListaConsultaTurma());
    }
    private List<SelectItem> tipoConsultaComboTurma;

    public List getTipoConsultaComboTurma() {
        if (tipoConsultaComboTurma == null) {
            tipoConsultaComboTurma = new ArrayList(0);
            tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
        }
        return tipoConsultaComboTurma;
    }
    private List<SelectItem> tipoConsultaComboDisciplina;

    public List getTipoConsultaComboDisciplina() {
        if (tipoConsultaComboDisciplina == null) {
            tipoConsultaComboDisciplina = new ArrayList(0);
            tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
        }
        return tipoConsultaComboDisciplina;
    }

    public List getTipoOrdenacaoCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
        itens.add(new SelectItem("data", "Data"));
        return itens;
    }

    public void consultarCurso() {
        try {
//            if (getUnidadeEnsino().getCodigo() == 0) {
//                throw new Exception("Informe a Unidade de Ensino.");
//            }
            List objs = new ArrayList(0);
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(getValorConsultaCurso(),
                        getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            setListaConsultaCurso(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCurso(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }
    
    public void selecionarConsultarPor() throws Exception {
        try {
        } catch (Exception e) {
        }
    }

    public void selecionarCurso() throws Exception {
        try {
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("curso");
            setCurso(obj);
            setDisciplina(null);
            setTurma(null);
            setListaConsultaDisciplina(null);
        } catch (Exception e) {
        }
    }

    public void limparCurso() throws Exception {
        try {
            setCurso(null);
            limparTurma();
        } catch (Exception e) {
        }
    }

    public void limparTurma() throws Exception {
        try {
            removerObjetoMemoria(getTurma());
            limparDisciplina();
        } catch (Exception e) {
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

    public List getTipoConsultaComboConsultarPor() {
        List<SelectItem> itens = new ArrayList(0);
        itens.add(new SelectItem("AL", "Aluno"));
        itens.add(new SelectItem("CU", "Curso"));
        itens.add(new SelectItem("CU", "Turma"));
        return itens;
    }

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
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

    public DisciplinaVO getDisciplina() {
        if (disciplina == null) {
            disciplina = new DisciplinaVO();
        }
        return disciplina;
    }

    public void setDisciplina(DisciplinaVO disciplina) {
        this.disciplina = disciplina;
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

    public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
        }
        return dataInicio;
    }

    public String periodo_Apresentar() {
        return Uteis.getData(getDataInicio()) + " à " + Uteis.getData(getDataFim());
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public Date getDataFim() {
        if (dataFim == null) {
            dataFim = Uteis.getDataUltimoDiaMes(new Date());
        }
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public TurmaVO getTurma() {
        if (turma == null) {
            turma = new TurmaVO();
        }
        return turma;
    }

    public void setTurma(TurmaVO turma) {
        this.turma = turma;
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

    public String getNivelEducacional() {
        if (nivelEducacional == null) {
            nivelEducacional = "";
        }
        return nivelEducacional;
    }

    public void setNivelEducacional(String nivelEducacional) {
        this.nivelEducacional = nivelEducacional;
    }

    public String getOrdenacao() {
        if (ordenacao == null) {
            ordenacao = "";
        }
        return ordenacao;
    }

    public void setOrdenacao(String ordenacao) {
        this.ordenacao = ordenacao;
    }

	public String getValorConsultaAluno() {
		return valorConsultaAluno;
	}

	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	public String getCampoConsultaAluno() {
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	public List getListaConsultaAluno() {
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public MatriculaVO getMatricula() {
		if (matricula == null) {
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}


}
