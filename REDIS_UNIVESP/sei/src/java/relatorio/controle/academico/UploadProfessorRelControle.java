/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.controle.academico;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import controle.arquitetura.SelectItemOrdemValor;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 *
 * @author Philippe
 */
@Controller("UploadProfessorRelControle")
@Scope("viewScope")
@Lazy
public class UploadProfessorRelControle extends SuperControleRelatorio {

    private UnidadeEnsinoVO unidadeEnsinoVO;
    private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
    private TurmaVO turmaVO;
    private PessoaVO professor;
    private List listaSelectItemUnidadeEnsino;
    private List listaSelectItemTurma;
    private String campoConsultaCurso;
    private String valorConsultaCurso;
    private List listaConsultaCurso;
    private String campoConsultaTurma;
    private String valorConsultaTurma;
    private List listaConsultaProfessor;
    private String campoConsultaProfessor;
    private String valorConsultaProfessor;
    private List listaConsultaTurma;
    private String ano;
	private String semestre;

    public UploadProfessorRelControle() throws Exception {
    }

    @PostConstruct
    public void init() {
    if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
    	setUnidadeEnsinoVO(getUnidadeEnsinoLogadoClone());
    	montarListaSelectItemTurma();
    } else {
    	montarListaSelectItemUnidadeEnsino();
    }
    }
    
    public void imprimirPDF() {
        List listaObjetos = null;
        try {
            if (getLoginControle().getOpcao().equals("coordenador") && getTurmaVO().getCodigo().equals(0)) {
                throw new ConsistirException("O campo TURMA deve ser informado");
            }
            listaObjetos = getFacadeFactory().getUploadProfessorRelFacade().criarObjeto(getUnidadeEnsinoVO().getCodigo(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getProfessor().getCodigo());
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getUploadProfessorRelFacade().designIReportRelatorio());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getTipoDescontoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Uploads Professor");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getTipoDescontoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
			setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getSuperParametroRelVO().adicionarLogoUnidadeEnsinoSelecionada(getUnidadeEnsinoVO());
		}
                realizarImpressaoRelatorio();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }

    public void consultarTurma() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                if (getValorConsultaTurma().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
                if (getValorConsultaTurma().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsinoCurso(getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), 0, 0);
            }
            if (getCampoConsultaTurma().equals("nomeTurno")) {
                if (getValorConsultaTurma().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurnoCurso(getValorConsultaTurma(), getUnidadeEnsinoCursoVO().getCurso().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeCurso")) {
                if (getValorConsultaTurma().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaTurma(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarTurma() {
        try {
            TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
            setTurmaVO(obj);
            if (getUnidadeEnsinoCursoVO().getCodigo() == 0) {
                getUnidadeEnsinoCursoVO().setCurso(getTurmaVO().getCurso());
            }
            if (getUnidadeEnsinoVO().getCodigo() == 0) {
                setUnidadeEnsinoVO(getTurmaVO().getUnidadeEnsino());
            }
            getListaConsultaTurma().clear();
            this.setValorConsultaTurma("");
            this.setCampoConsultaTurma("");
            setMensagemID("", "");
        } catch (Exception e) {
        }
    }

    public void limparTurma() throws Exception {
        try {
            setTurmaVO(null);
        } catch (Exception e) {
        }
    }

    public void limparProfessor() throws Exception {
        try {
            setProfessor(null);
        } catch (Exception e) {
        }
    }

    public void mostrarUnidadeEnsinoCursoTurma() throws Exception {
        getUnidadeEnsinoCursoVO().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
        setTurmaVO(getFacadeFactory().getTurmaFacade().consultaRapidaPorChavePrimariaDadosBasicosTurmaAgrupada(getTurmaVO().getCodigo(), getUsuarioLogado()));
        setUnidadeEnsinoVO(getTurmaVO().getUnidadeEnsino());
    }

    public List<SelectItem> tipoConsultaComboTurma;
    public List<SelectItem>  getTipoConsultaComboTurma() {
    	if(tipoConsultaComboTurma == null) {
    	tipoConsultaComboTurma = new ArrayList<SelectItem> (0);
        tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
        tipoConsultaComboTurma.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        tipoConsultaComboTurma.add(new SelectItem("nomeTurno", "Turno"));
        tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
    	}
        return tipoConsultaComboTurma;
    }

    public void consultarCurso() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
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
            UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItens");
            setUnidadeEnsinoCursoVO(obj);
            if (getUnidadeEnsinoVO().getCodigo() == 0) {
                getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoCursoVO().getUnidadeEnsino());
            }
            setTurmaVO(null);
            setValorConsultaCurso("");
            setCampoConsultaCurso("");
            getListaConsultaCurso().clear();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparCurso() {
        setUnidadeEnsinoCursoVO(null);
    }

    public void limparDadosRelacionadosUnidadeEnsino() {
        setUnidadeEnsinoCursoVO(null);
        setTurmaVO(null);
    }

    public void consultarProfessor() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaProfessor().equals("nome")) {
                objs = getFacadeFactory().getPessoaFacade().consultarProfessorPorNome(getValorConsultaProfessor(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            setListaConsultaProfessor(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaProfessor(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarProfessor() throws Exception {
        PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("professorItens");
        setProfessor(obj);
        setValorConsultaProfessor("");
        setCampoConsultaProfessor("");
        getListaConsultaProfessor().clear();
    }

    public List<SelectItem> tipoConsultaComboProfessor;
    public List<SelectItem> getTipoConsultaComboProfessor() {
    	if(tipoConsultaComboProfessor == null) {
    	tipoConsultaComboProfessor = new ArrayList<SelectItem>(0);
    	tipoConsultaComboProfessor.add(new SelectItem("nome", "Nome"));
    	}
        return tipoConsultaComboProfessor;
    }
    
    public List<SelectItem> tipoConsultaComboCurso;
    public List<SelectItem> getTipoConsultaComboCurso() {
    	if(tipoConsultaComboCurso == null) {
    		tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
    		tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
    	}
    	return tipoConsultaComboCurso;
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
            resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(prm, 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            if (resultadoConsulta.isEmpty()) {
                resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            }
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
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
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemTurma() {
        try {
            montarListaSelectItemTurma("");
        } catch (Exception e) {
          //  //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemTurma(String prm) throws Exception {

    	List listaResultado = null;
		Iterator i = null;
		Map<Integer, String> hashTurmasAdicionadas = new HashMap<Integer, String>(0);
		try {
			listaResultado = consultarTurmaPorIdentificador("");
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			i = listaResultado.iterator();
			String value = "";
			while (i.hasNext()) {
				TurmaVO turma = (TurmaVO) i.next();
				if (turma.getTurmaAgrupada()) {
					List<TurmaVO> listaTurmasAgrupadas = getFacadeFactory().getTurmaFacade().consultaRapidaTurmasNasQuaisTurmaParticipaDeAgrupamento(turma.getCodigo(), false, getUsuarioLogado());
					for (TurmaVO turmaParticipaAgrupamento : listaTurmasAgrupadas) {
						if (!hashTurmasAdicionadas.containsKey(turmaParticipaAgrupamento.getCodigo())) {
							value = turmaParticipaAgrupamento.getIdentificadorTurma() + " - Curso " + turmaParticipaAgrupamento.getCurso().getNome() + " - Turno " + turmaParticipaAgrupamento.getTurno().getNome();
							getListaSelectItemTurma().add(new SelectItem(turmaParticipaAgrupamento.getCodigo(), value));
							hashTurmasAdicionadas.put(turmaParticipaAgrupamento.getCodigo(), turmaParticipaAgrupamento.getIdentificadorTurma());
						}
					}
				} else {
					if (!hashTurmasAdicionadas.containsKey(turma.getCodigo())) {
						value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno " + turma.getTurno().getNome();
						getListaSelectItemTurma().add(new SelectItem(turma.getCodigo(), value));
						hashTurmasAdicionadas.put(turma.getCodigo(), turma.getIdentificadorTurma());
					}
				}
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List) getListaSelectItemTurma(), ordenador);
		} catch (Exception e) {
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			hashTurmasAdicionadas = null;
			i = null;
		}
    }

    public List consultarTurmaPorIdentificador(String nomePrm) throws Exception {
    	return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenadorAnoSemestre(getUsuarioLogado().getPessoa().getCodigo(), false, false, true, false, getAno(), getSemestre(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
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

    public PessoaVO getProfessor() {
        if (professor == null) {
            professor = new PessoaVO();


        }
        return professor;


    }

    public void setProfessor(PessoaVO professor) {
        this.professor = professor;


    }

    public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
        if (unidadeEnsinoCursoVO == null) {
            unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();


        }
        return unidadeEnsinoCursoVO;


    }

    public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
        this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;


    }

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);


        }
        return listaSelectItemUnidadeEnsino;


    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;


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

    public List<CursoVO> getListaConsultaCurso() {
        if (listaConsultaCurso == null) {
            listaConsultaCurso = new ArrayList<CursoVO>(0);


        }
        return listaConsultaCurso;


    }

    public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
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

    public List getListaConsultaProfessor() {
        if (listaConsultaProfessor == null) {
            listaConsultaProfessor = new ArrayList(0);


        }
        return listaConsultaProfessor;


    }

    public void setListaConsultaProfessor(List listaConsultaProfessor) {
        this.listaConsultaProfessor = listaConsultaProfessor;


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

    public List<TurmaVO> getListaConsultaTurma() {
        if (listaConsultaTurma == null) {
            listaConsultaTurma = new ArrayList<TurmaVO>(0);


        }
        return listaConsultaTurma;


    }

    public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
        this.listaConsultaTurma = listaConsultaTurma;


    }

    public List<SelectItem> getListaSelectItemTurma() {
        if (listaSelectItemTurma == null) {
            listaSelectItemTurma = new ArrayList<SelectItem>(0);


        }
        return listaSelectItemTurma;


    }

    public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
        this.listaSelectItemTurma = listaSelectItemTurma;


    }
    
    public String getAno() {
		if (ano == null) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()
					&& getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				ano = getVisaoProfessorControle().getAno();
			} else if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()
					&& getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				ano = getVisaoCoordenadorControle().getAno();
			} else {
				ano = Uteis.getAnoDataAtual4Digitos();
			}
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()
					&& getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				semestre = getVisaoProfessorControle().getSemestre();
			} else if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()
					&& getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				semestre = getVisaoCoordenadorControle().getSemestre();
			} else {
				semestre = Uteis.getSemestreAtual();
			}
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public boolean getIsApresentarAnoVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor")
				|| getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()) {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (getTurmaVO().getSemestral()) {
						setAno(getAno());
						return true;
					} else if (getTurmaVO().getAnual()) {
						setAno(getAno());
						return true;
					} else {
						setAno("");
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	public boolean getIsApresentarSemestreVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor")
				|| getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()) {
				if (!getTurmaVO().getCodigo().equals(0)) {
					if (getTurmaVO().getSemestral()) {
						setSemestre(getSemestre());
						return true;
					} else {
						setSemestre("");
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	public List<SelectItem> listaSelectSemestre;

	public List<SelectItem> getListaSelectSemestre() {
		if (listaSelectSemestre == null) {
			listaSelectSemestre = new ArrayList<SelectItem>(0);
			listaSelectSemestre.add(new SelectItem("", ""));
			listaSelectSemestre.add(new SelectItem("1", "1º"));
			listaSelectSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectSemestre;
	}
}
