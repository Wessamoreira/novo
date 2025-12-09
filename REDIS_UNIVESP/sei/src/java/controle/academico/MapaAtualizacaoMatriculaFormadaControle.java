/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.academico;

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
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
/**
 *
 * @author Philippe
 */
@Controller("MapaAtualizacaoMatriculaFormadaControle")
@Scope("viewScope")
@Lazy
public class MapaAtualizacaoMatriculaFormadaControle extends SuperControle {

    private MatriculaVO matricula;
    private TurmaVO turma;
    private CursoVO curso;	
    private UnidadeEnsinoVO unidadeEnsino;
    private String situacaoRegistroFormatura;
    private Boolean apresentarPesquisaPorAluno;
    private Boolean apresentarPesquisaPorTurma;
    private Boolean apresentarPesquisaPorCurso;	
    private String filtro;
    private Date dataConclusao;
    private List listaMatriculas;
    private List listaConsultaTurma;
    private String valorConsultaTurma;
    private String campoConsultaTurma;
    private List listaConsultaMatricula;
    private String valorConsultaMatricula;
    private String campoConsultaMatricula;
    private String ano;
    private String semestre;	
    private List listaSelectItemUnidadeEnsino;
	protected List listaSelectItemCurso;
	protected List listaConsultaCurso;	
	protected String campoConsultaCurso;
	protected String valorConsultaCurso;	
	protected Boolean permitiTrazerAlunoCurriculoNaoInteg;	
	protected Boolean curriculoNaoIntegralizado;		

    public MapaAtualizacaoMatriculaFormadaControle() {
        montarListaSelectItemUnidadeEnsino();
        verificaApresentarAlunoCurriculoNaoInteg();		
    }

    public void verificaApresentarAlunoCurriculoNaoInteg() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("MapaAtualizacaoMatricula_permitirFormarAlunoCurriculoNaoInteg", getUsuarioLogado());
			setPermitiTrazerAlunoCurriculoNaoInteg(Boolean.TRUE);
		} catch (Exception e) {
			setPermitiTrazerAlunoCurriculoNaoInteg(Boolean.FALSE);
		}    	
    }
	
    public void novo() {
    	setMatricula(new MatriculaVO());
    	setTurma(new TurmaVO());
    	setCurso(new CursoVO());		
    	setUnidadeEnsino(new UnidadeEnsinoVO());
    	setFiltro(null);
    	setDataConclusao(null);
    	getListaMatriculas().clear();
    	getListaConsultaTurma().clear();
    	getListaSelectItemUnidadeEnsino().clear();
    	montarListaSelectItemUnidadeEnsino();
    	setApresentarPesquisaPorAluno(Boolean.FALSE);
    	setApresentarPesquisaPorCurso(Boolean.FALSE);
    	setApresentarPesquisaPorTurma(Boolean.FALSE);		
    }
    
	public void consultarMatriculasAtualizacao() {
        try {
        	if(getFiltro().trim().isEmpty()){
        		throw new Exception("O campo FILTRO deve ser informado.");
        	}
        	if(!Uteis.isAtributoPreenchido(getUnidadeEnsino())){
        		throw new Exception("O campo UNIDADE ENSINO deve ser informado.");
        	}
        	if(getApresentarPesquisaPorTurma() && !Uteis.isAtributoPreenchido(getTurma())){
        		throw new Exception("O campo TURMA deve ser informado.");
        	}
        	if(getApresentarPesquisaPorCurso() && !Uteis.isAtributoPreenchido(getCurso())){
        		throw new Exception("O campo CURSO deve ser informado.");
        	}
        	if(getApresentarPesquisaPorAluno() && !Uteis.isAtributoPreenchido(getMatricula().getAluno())){
        		throw new Exception("O campo MATRÍCULA deve ser informado.");
        	}
        	if(getIsApresentarAno() && getAno().trim().isEmpty()){
        		throw new Exception("O campo ANO deve ser informado.");
        	}
        	if(getIsApresentarAno() && getAno().length() != 4){
        		throw new Exception("O campo ANO deve ser informado com 4 dígitos.");
        	}
        	if(getIsApresentarSemestre() && getSemestre().trim().isEmpty()){
        		throw new Exception("O campo SEMESTRE deve ser informado.");
        	}
            
            getListaMatriculas().clear();
            if (getFiltro().equals("turma")) {
                setListaMatriculas(getFacadeFactory().getMatriculaFacade().consultaRapidaPorTurma(getTurma().getCodigo(), getAno(), getSemestre(), getUnidadeEnsino().getCodigo(), !getCurriculoNaoIntegralizado(), getSituacaoRegistroFormatura(), getUsuarioLogado()));
            } else if (getFiltro().equals("curso")) {
            	setListaMatriculas(getFacadeFactory().getMatriculaFacade().consultaRapidaPorCurso(getCurso().getCodigo(), getAno(), getSemestre(), getUnidadeEnsino().getCodigo(), !getCurriculoNaoIntegralizado(), getSituacaoRegistroFormatura(), getUsuarioLogado(), "AMBOS",  ""));
            } else {
            	if (!getCurriculoNaoIntegralizado()) {
	    	        if (!getFacadeFactory().getMatriculaFacade().isMatriculaIntegralizada(getMatricula(), getMatricula().getGradeCurricularAtual().getCodigo(), getUsuarioLogado(), null) ) {
	    	        	throw new Exception("A matrícula informada não pode ser definida como FORMADA, verifique se o histórico do aluno está como Currículo Integralizado.");
	    	        }
            	}
                getListaMatriculas().add(getMatricula());
            }
            if (!getListaMatriculas().isEmpty()) {
                for (Object obj : listaMatriculas) {
                    MatriculaVO matricula = (MatriculaVO) obj;
                    if (matricula.getDataConclusaoCurso() == null) {
                    	if(Uteis.isAtributoPreenchido(getDataConclusao())) {
                    		matricula.setDataConclusaoCurso(getDataConclusao());
                    	}else {
                    		matricula.setDataConclusaoCurso(getFacadeFactory().getMatriculaFacade().consultarDataConclusaoCursoPorMatricula(matricula.getMatricula()));
                    	}
                    }
                }
            }
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void gravarAtualizacaoMatricula() {
    	String situacaoAnt = null;
		try {
			for (Object obj : getListaMatriculas()) {
				MatriculaVO matricula = (MatriculaVO) obj;
				situacaoAnt = matricula.getSituacao();
				matricula.setSituacao("FO");
				matricula.setMsgErro("");
				matricula.setDataAtualizacaoMatriculaFormada(new Date());
				matricula.setResponsavelAtualizacaoMatriculaFormada(getUsuarioLogadoClone());
				getFacadeFactory().getMatriculaFacade().alterarSituacaoMatriculaFormadaAtualizacao(matricula, getUsuarioLogadoClone());
				setMatricula(null);
				setTurma(null);
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			matricula.setMsgErro(e.getMessage());
			matricula.setSituacao(situacaoAnt);
		}
	}

    public void consultarAluno() {
        try {
            List objs = new ArrayList(0);
            if (getValorConsultaMatricula().equals("")) {
                throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
            }
            if (getCampoConsultaMatricula().equals("matricula")) {
                MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimariaSituacaoDadosCompletos(getValorConsultaMatricula(), getUnidadeEnsino().getCodigo(), false, "'AT', 'FI'", getUsuarioLogado());
                if (!obj.getMatricula().equals("")) {
                    objs.add(obj);
                }
            }
            if (getCampoConsultaMatricula().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaMatricula(), getUnidadeEnsino().getCodigo(), false, "'AT', 'FI'","", getUsuarioLogado());
            }
            setListaConsultaMatricula(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaMatricula(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void consultarAlunoPorMatricula() {
        try {
            MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimariaSituacaoDadosCompletos(getMatricula().getMatricula(), getUnidadeEnsino().getCodigo(), false, "'AT', 'FI'", getUsuarioLogado());
            if (obj.getMatricula().equals("")) {
            	throw new Exception("Matrícula " + getMatricula().getMatricula() + " não foi encontrada ou a situação não está ativa. Verifique se o número de matrícula está correto.");
            }
            setMatricula(obj);
            getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());
            setMensagemDetalhada("");
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String remover() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        int index = 0;
        Iterator i = getListaMatriculas().iterator();
        while (i.hasNext()) {
            MatriculaVO objExistente = (MatriculaVO) i.next();
            if (objExistente.getMatricula().equals(obj.getMatricula())) {
            	getListaMatriculas().remove(index);
            	return Uteis.getCaminhoRedirecionamentoNavegacao("mapaAtualizacaoMatriculaFormadaForm.xhtml");
            }
            index++;
        }
        return Uteis.getCaminhoRedirecionamentoNavegacao("mapaAtualizacaoMatriculaFormadaForm.xhtml");
    }
    
    public void selecionarAluno() throws Exception {
        try {
	    	MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
	        MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
	        
	        setMatricula(objCompleto);	        
	        obj = null;
	        objCompleto = null;
	        setValorConsultaMatricula("");
	        setCampoConsultaMatricula("");
	        getListaConsultaMatricula().clear();
	        setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
        	setMatricula(new MatriculaVO());
        	setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void limparDadosMatricula() throws Exception {
        setMatricula(null);
    }

    public void limparDadosCurso() throws Exception {
    	setCurso(null);
    }

    public void consultarTurma() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getCampoConsultaTurma().equals("identificadorTurma")) {
                if (getValorConsultaTurma().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), 0, getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
                if (getValorConsultaTurma().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsinoCurso(getValorConsultaTurma(), 0, getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado(), 0, 0);
            }
            if (getCampoConsultaTurma().equals("nomeTurno")) {
                if (getValorConsultaTurma().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurnoCurso(getValorConsultaTurma(), 0, getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaTurma().equals("nomeCurso")) {
                if (getValorConsultaTurma().length() < 2) {
                    throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
                }
                objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), 0, false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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
            getFacadeFactory().getTurmaFacade().carregarDados(obj, NivelMontarDados.BASICO, getUsuarioLogado());
            setTurma(obj);            
            if (getUnidadeEnsino().getCodigo() == 0) {
                setUnidadeEnsino(getTurma().getUnidadeEnsino());
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
            setTurma(null);
        } catch (Exception e) {
        }
    }

    public List getTipoConsultaComboSituacaoRegistroFormatura() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("TO", "Todas"));
        itens.add(new SelectItem("RE", "Registradas"));
        itens.add(new SelectItem("NR", "Não Registradas"));
        return itens;
    }

    public List getTipoConsultaComboTurma() {
    	List itens = new ArrayList(0);
    	itens.add(new SelectItem("identificadorTurma", "Identificador"));
    	itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
    	return itens;
    }

    public List getTipoConsultaComboMatricula() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Aluno"));
        itens.add(new SelectItem("matricula", "Matrícula"));
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
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(valorInt, getUnidadeEnsino().getCodigo(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				if (getValorConsultaCurso().trim().isEmpty() || getValorConsultaCurso().trim().contains("%%") || getValorConsultaCurso().trim().length() < 3) {
					throw new Exception("Informe 3 caracteres válidos para realizar a consulta.");
				}
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsino().getCodigo(), false, "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparListaConsultaCurso() {
		setListaConsultaCurso(new ArrayList(0));
	}

	public void selecionarCurso() throws Exception {
		try {
			UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
			CursoVO cursoVO = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(unidadeEnsinoCurso.getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
			setCurso(cursoVO);
			setMensagemDetalhada("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}
	
    public List getTipoFiltroCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("", ""));
        itens.add(new SelectItem("curso", "Curso"));		
        itens.add(new SelectItem("turma", "Turma"));
        itens.add(new SelectItem("aluno", "Aluno"));
        return itens;
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }
   
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuarioLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
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

    public void apresentarMetodoPesquisa() {
        if (getFiltro().equals("turma")) {
            setApresentarPesquisaPorAluno(Boolean.FALSE);
            setApresentarPesquisaPorTurma(Boolean.TRUE);
            setApresentarPesquisaPorCurso(Boolean.FALSE);			
        } else if (getFiltro().equals("aluno")) {
            setApresentarPesquisaPorAluno(Boolean.TRUE);
            setApresentarPesquisaPorTurma(Boolean.FALSE);
            setApresentarPesquisaPorCurso(Boolean.FALSE);
        } else if (getFiltro().equals("curso")) {
        	setApresentarPesquisaPorAluno(Boolean.FALSE);
        	setApresentarPesquisaPorTurma(Boolean.FALSE);
        	setApresentarPesquisaPorCurso(Boolean.TRUE);			
        } else {
            setApresentarPesquisaPorAluno(Boolean.FALSE);
            setApresentarPesquisaPorTurma(Boolean.FALSE);
            setApresentarPesquisaPorCurso(Boolean.FALSE);			
        }
        setMatricula(null);
        setTurma(null);
        getListaMatriculas().clear();
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

    public Boolean getApresentarPesquisaPorAluno() {
        if (apresentarPesquisaPorAluno == null) {
            apresentarPesquisaPorAluno = Boolean.FALSE;
        }
        return apresentarPesquisaPorAluno;
    }

    public void setApresentarPesquisaPorAluno(Boolean apresentarPesquisaPorAluno) {
        this.apresentarPesquisaPorAluno = apresentarPesquisaPorAluno;
    }

    public Boolean getApresentarPesquisaPorTurma() {
        if (apresentarPesquisaPorTurma == null) {
            apresentarPesquisaPorTurma = Boolean.FALSE;
        }
        return apresentarPesquisaPorTurma;
    }

    public void setApresentarPesquisaPorTurma(Boolean apresentarPesquisaPorTurma) {
        this.apresentarPesquisaPorTurma = apresentarPesquisaPorTurma;
    }

    public String getFiltro() {
        if (filtro == null) {
            filtro = "";
        }
        return filtro;
    }

    public void setFiltro(String filtro) {
        this.filtro = filtro;
    }

    public Date getDataConclusao() {        
        return dataConclusao;
    }

    public void setDataConclusao(Date dataConclusao) {
        this.dataConclusao = dataConclusao;
    }

    public List getListaMatriculas() {
        if (listaMatriculas == null) {
            listaMatriculas = new ArrayList(0);
        }
        return listaMatriculas;
    }

    public void setListaMatriculas(List listaMatriculas) {
        this.listaMatriculas = listaMatriculas;
    }

    public Boolean getApresentarBotaoConsultar() {
        if (getTurma().getCodigo() != 0 || getMatricula().getAluno().getCodigo() != 0 || getCurso().getCodigo() != 0) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    public Boolean getApresentarConsulta() {
        if (getListaMatriculas().isEmpty()) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
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

    public String getCampoConsultaTurma() {
        if (campoConsultaTurma == null) {
            campoConsultaTurma = "";
        }
        return campoConsultaTurma;
    }

    public void setCampoConsultaTurma(String campoConsultaTurma) {
        this.campoConsultaTurma = campoConsultaTurma;
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

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

    public List getListaConsultaMatricula() {
        if (listaConsultaMatricula == null) {
            listaConsultaMatricula = new ArrayList(0);
        }
        return listaConsultaMatricula;
    }

    public void setListaConsultaMatricula(List listaConsultaMatricula) {
        this.listaConsultaMatricula = listaConsultaMatricula;
    }

    public String getValorConsultaMatricula() {
        if (valorConsultaMatricula == null) {
            valorConsultaMatricula = "";
        }
        return valorConsultaMatricula;
    }

    public void setValorConsultaMatricula(String valorConsultaMatricula) {
        this.valorConsultaMatricula = valorConsultaMatricula;
    }

    public String getCampoConsultaMatricula() {
        if (campoConsultaMatricula == null) {
            campoConsultaMatricula = "";
        }
        return campoConsultaMatricula;
    }

    public void setCampoConsultaMatricula(String campoConsultaMatricula) {
        this.campoConsultaMatricula = campoConsultaMatricula;
    }
	
    public Boolean getApresentarPesquisaPorCurso() {
    	if (apresentarPesquisaPorCurso == null) {
    		apresentarPesquisaPorCurso = Boolean.FALSE;
    	}
    	return apresentarPesquisaPorCurso;
    }
    
    public void setApresentarPesquisaPorCurso(Boolean apresentarPesquisaPorCurso) {
    	this.apresentarPesquisaPorCurso = apresentarPesquisaPorCurso;
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

	public String getAno() {
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}
	
	public boolean getIsApresentarAno() {		
		if ((getApresentarPesquisaPorCurso() && !getCurso().getPeriodicidade().equals("IN") && Uteis.isAtributoPreenchido(getCurso()))
				|| (getApresentarPesquisaPorTurma() && getTurma().getCodigo().intValue() > 0 && (getTurma().getAnual() || getTurma().getSemestral()))
				) {
			if( getAno().trim().isEmpty()){
				setAno(Uteis.getAnoDataAtual());
			}
				return true;
		}					
		setAno("");
		setSemestre("");
		return false;
	}

	public boolean getIsApresentarSemestre() {
		if ((getApresentarPesquisaPorCurso() && getCurso().getPeriodicidade().equals("SE") && Uteis.isAtributoPreenchido(getCurso()))
				|| (getApresentarPesquisaPorTurma() && getTurma().getCodigo().intValue() > 0 && getTurma().getSemestral())
				) {
			if(getSemestre().trim().isEmpty()){
				setSemestre(Uteis.getSemestreAtual());
			}
				return true;
		}		
		setAno("");
		setSemestre("");
		return false;
	}

	/**
	 * @return the campoConsultaCurso
	 */
	public String getCampoConsultaCurso() {
		return campoConsultaCurso;
	}

	/**
	 * @param campoConsultaCurso
	 *            the campoConsultaCurso to set
	 */
	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	/**
	 * @return the valorConsultaCurso
	 */
	public String getValorConsultaCurso() {
		return valorConsultaCurso;
	}

	/**
	 * @param valorConsultaCurso
	 *            the valorConsultaCurso to set
	 */
	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	/**
	 * @return the listaSelectItemCurso
	 */
	public List getListaSelectItemCurso() {
		return listaSelectItemCurso;
	}

	/**
	 * @param listaSelectItemCurso
	 *            the listaSelectItemCurso to set
	 */
	public void setListaSelectItemCurso(List listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	/**
	 * @return the listaConsultaCurso
	 */
	public List getListaConsultaCurso() {
		return listaConsultaCurso;
	}

	/**
	 * @param listaConsultaCurso
	 *            the listaConsultaCurso to set
	 */
	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public List getListaSelectItemSemestre() {
        List lista = new ArrayList(0);
        lista.add(new SelectItem("", ""));
        lista.add(new SelectItem("1", "1°"));
        lista.add(new SelectItem("2", "2°"));
        return lista;
    }

	
	public Boolean getPermitiTrazerAlunoCurriculoNaoInteg() {
		if (permitiTrazerAlunoCurriculoNaoInteg == null) {
			permitiTrazerAlunoCurriculoNaoInteg = Boolean.FALSE;
		}
		return permitiTrazerAlunoCurriculoNaoInteg;
	}

	public void setPermitiTrazerAlunoCurriculoNaoInteg(Boolean permitiTrazerAlunoCurriculoNaoInteg) {
		this.permitiTrazerAlunoCurriculoNaoInteg = permitiTrazerAlunoCurriculoNaoInteg;
	}

	public Boolean getCurriculoNaoIntegralizado() {
		if (curriculoNaoIntegralizado == null) {
			curriculoNaoIntegralizado = Boolean.FALSE;
		}
		return curriculoNaoIntegralizado;
	}

	public void setCurriculoNaoIntegralizado(Boolean curriculoNaoIntegralizado) {
		this.curriculoNaoIntegralizado = curriculoNaoIntegralizado;
	}

	public String getSituacaoRegistroFormatura() {
		if (situacaoRegistroFormatura == null) {
			situacaoRegistroFormatura = "TO";
		}
		return situacaoRegistroFormatura;
	}

	public void setSituacaoRegistroFormatura(String situacaoRegistroFormatura) {
		this.situacaoRegistroFormatura = situacaoRegistroFormatura;
	}

}
