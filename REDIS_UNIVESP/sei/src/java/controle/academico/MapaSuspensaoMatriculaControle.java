package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CancelamentoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.HorarioAlunoTurnoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.academico.PendenciaLiberacaoMatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.MotivoSolicitacaoLiberacaoMatriculaEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoJustificativaCancelamento;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * 
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Controller("MapaSuspensaoMatriculaControle")
@Scope("viewScope")
@Lazy
public class MapaSuspensaoMatriculaControle extends SuperControle implements Serializable {

	private List listaConsultaDocumentos;
	private MatriculaVO matriculaVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private TurmaVO turmaVO;
	private List listaSelectItemUnidadeEnsino;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaCurso;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaTurma;
	private List listaConsultaAluno;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private Boolean realizarAdiamentoSuspensaoMatricula;
	private String situacaoCancelamentoBloqueioMatricula;
	private MatriculaVO matriculaConsultada;
	private PendenciaLiberacaoMatriculaVO pendenciaLiberacaoMatriculaVO;
	private String tipoSuspensaoMatricula;
	private String situacaoSolicitacao;
	private Boolean permitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica;
	private Boolean permitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica;
	private Boolean permitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira;
	private Boolean permitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira;
	private Boolean permitirCancelarSuspensaoMatricula;
	private Boolean permitirUsuarioVisualizarApenasSuasSolicitacoes;
	private Boolean permitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private CancelamentoVO cancelamentoVO;
	private List<SelectItem> listaSelectItemMotivoCancelamentoTrancamento;
	private String onCompleteModalIndeferimento;
	//private String msgPermissaoNegadaLiberacaoSuspensaoMatriculaPendencia;
	private List<ContaReceberVO> listaContaReceber;
	private List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs;
	private List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs;

	public MapaSuspensaoMatriculaControle() {
		//montarListaSelectItemUnidadeEnsino();
		verificarPermissaoLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica();
		verificarPermissaoVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica();
		verificarPermissaoLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira();
		verificarPermissaoVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira();			
		verificarPermissaoCancelarSuspensaoMatricula();
		verificarPermissaoUsuarioVisualizarApenasSuasSolicitacoes();
		verificarPermissaoVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidades();
		consultarUnidadeEnsino();
		consultarMatriculaSuspensa();
	}
	
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("MapaSuspensaoMatricula");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome()).append("; ");
				}
			}
			getUnidadeEnsinoVO().setNome(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					getUnidadeEnsinoVO().setNome(getUnidadeEnsinoVOs().get(0).getNome());
				}
			} else {
				getUnidadeEnsinoVO().setNome(unidade.toString());
			}
		}
	}	

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			if (getMarcarTodasUnidadeEnsino()) {
				unidade.setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				unidade.setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}
		verificarTodasUnidadesSelecionadas();
	}

	/*public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
			verificarPermissaoLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica();
			verificarPermissaoVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica();
			verificarPermissaoLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira();
			verificarPermissaoVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira();			
			verificarPermissaoCancelarSuspensaoMatricula();
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());			
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
	}*/

	public void limparDadosRelacionadosUnidadeEnsino() {
		setUnidadeEnsinoCursoVO(null);
		setTurmaVO(null);
	}

	public void limparCurso() {
		setUnidadeEnsinoCursoVO(null);
		getListaConsulta().clear();
	}

	public void limparTurma() throws Exception {
		try {
			setTurmaVO(null);
			getListaConsulta().clear();
		} catch (Exception e) {
		}
	}

	public void limparDadosAluno() throws Exception {
		try {
			setMatriculaVO(null);
			getListaConsulta().clear();
		} catch (Exception e) {
		}
	}
	
	public void consultarMatriculaSuspensa() {
		try {
			if(!getPermitirCancelarSuspensaoMatricula() && !getPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica() && !getPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira() && !getPermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica() && !getPermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira() && !getPermitirUsuarioVisualizarApenasSuasSolicitacoes() && !getPermitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade()) {
				throw new Exception(getMensagemInternalizacao("msg_MapaSuspensaoMatricula_permissaoNegada"));				
			}
			getListaConsulta().clear();
			setListaConsulta(getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaSuspensaParaSerListadaParaCancelamento(getUnidadeEnsinoVOs(), getMatriculaVO().getMatricula(), getUnidadeEnsinoCursoVO().getCurso().getCodigo() , getTurmaVO().getCodigo(), getConfiguracaoGeralPadraoSistema().getConfiguracoesVO().getNrDiasListarMatriculaPendenciaDocumentosParaCancelamento(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getTipoSuspensaoMatricula(), getSituacaoSolicitacao(), getPermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira(), getPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira(), getPermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica(), getPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica(), getPermitirUsuarioVisualizarApenasSuasSolicitacoes(), getPermitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade()));
			
			if(getTipoSuspensaoMatricula().equals("pendenciaSolicitacao") && getSituacaoSolicitacao().equals("deferido") && getListaConsulta().isEmpty()) {					
				setMensagemDetalhada(getMensagemInternalizacao("msg_MapaSuspensaoMatricula_avisoNenhumRegistroDeferidoUsuarioLogado"));
			}
			else if(getTipoSuspensaoMatricula().equals("pendenciaSolicitacao") && getSituacaoSolicitacao().equals("indeferido") && getListaConsulta().isEmpty()) {
				setMensagemDetalhada(getMensagemInternalizacao("msg_MapaSuspensaoMatricula_avisoNenhumRegistroIndeferidoUsuarioLogado"));
			}else {			
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
			}
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void visualizarDetalhes() throws Exception {
		MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
		try {
			setListaConsultaDocumentos(getFacadeFactory().getDocumetacaoMatriculaFacade().consultarDocumetacaoMatriculaPorMatriculaAlunoPendenteEntregaSuspendeMatricula(obj.getMatricula(), Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado()));
		} catch (Exception e) {
			setListaConsultaDocumentos(new ArrayList());
		}
	}
	
	public void cancelarSuspensaoMatricula() throws Exception {
		try {
			MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
			obj.getMatriculaVO().setMatriculaSuspensa(Boolean.FALSE);
			getFacadeFactory().getMatriculaFacade().alterarMatriculaSuspensao(obj.getMatriculaVO());
			setMensagemDetalhada("Cancelamento de suspensão de matrícula realizada com sucesso!");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCurso() {
		try {
			getFacadeFactory().getMatriculaEnadeFacade().validarDadosUnidadeEnsino(getUnidadeEnsinoVOs());
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVOs(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
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
			valorConsultaCurso = "";
			campoConsultaCurso = "";
			getListaConsultaCurso().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			getFacadeFactory().getMatriculaEnadeFacade().validarDadosUnidadeEnsino(getUnidadeEnsinoVOs());
			super.consultar();
			List objs = new ArrayList(0);
			List<CursoVO> listaCurso = new ArrayList<CursoVO>(0);
			listaCurso.add(getUnidadeEnsinoCursoVO().getCurso());
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), listaCurso, getUnidadeEnsinoVOs(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurnoCurso(getValorConsultaTurma(), listaCurso, getUnidadeEnsinoVOs(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVOs(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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

	public void consultarAluno() {
		try {
			getFacadeFactory().getMatriculaEnadeFacade().validarDadosUnidadeEnsino(getUnidadeEnsinoVOs());
			List objs = new ArrayList(0);
			List<CursoVO> listaCurso = new ArrayList<CursoVO>(0);
			listaCurso.add(getUnidadeEnsinoCursoVO().getCurso());
			if (getCampoConsultaAluno().equals("nome")) {
				if (getTurmaVO().getCodigo() != 0) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoaCursoTurma(getValorConsultaAluno(), getUnidadeEnsinoVOs(), listaCurso, getTurmaVO().getCodigo(), false, getUsuarioLogado());
				} else if (getUnidadeEnsinoCursoVO().getCurso().getCodigo() != 0) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoVOs(), listaCurso, false, getUsuarioLogado());
				} else if (getUnidadeEnsinoVO().getCodigo() != 0) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoVOs(), false, getUsuarioLogado());
				} else {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoVOs(), false, getUsuarioLogado());
				}
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		setMatriculaVO(obj);
		getUnidadeEnsinoCursoVO().setCurso(obj.getCurso());
		setUnidadeEnsinoVO(obj.getUnidadeEnsino());
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		getListaConsultaAluno().clear();
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public List getTipoConsultaComboTipo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("aluno", "Aluno"));
		itens.add(new SelectItem("cursoTurma", "Curso/Turma"));
		return itens;
	}

	public List getTipoConsultaComboTurma() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	/**
	 * @return the listaConsultaDocumentos
	 */
	public List getListaConsultaDocumentos() {
		if (listaConsultaDocumentos == null) {
			listaConsultaDocumentos = new ArrayList();
		}
		return listaConsultaDocumentos;
	}

	/**
	 * @param listaConsultaDocumentos
	 *            the listaConsultaDocumentos to set
	 */
	public void setListaConsultaDocumentos(List listaConsultaDocumentos) {
		this.listaConsultaDocumentos = listaConsultaDocumentos;
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

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			unidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
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

	public List getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
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
	
	public void inicializarDadosMatriculaCancelamentoBloqueioMatricula() {
		try {
			MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
			setMatriculaConsultada(getFacadeFactory().getMatriculaFacade().verificarBloqueioMatricula(obj.getMatriculaVO(), getUsuarioLogado(), null));
			getMatriculaConsultada().setResponsavelCancelamentoSuspensaoMatricula(getUsuarioLogadoClone());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}		
	}
	
	public void realizarCancelamentoBloqueioMatricula() {
		try {
			getFacadeFactory().getMatriculaFacade().realizarCancelamentoBloqueioMatricula(getMatriculaConsultada(), getUsuarioLogado());
			setSituacaoCancelamentoBloqueioMatricula("CANCELAMENTO_REALIZADO_COM_SUCESSO");
			limparCurso();
			limparTurma();
			limparDadosAluno();
			setMensagemID("msg_dados_cancelamentoSuspensaoMatricula");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setSituacaoCancelamentoBloqueioMatricula("");
		}
	}
	
	public void realizarAdiamentoBloqueioMatricula() {
		try {
			getFacadeFactory().getMatriculaFacade().realizarAdiamentoBloqueioMatricula(getMatriculaConsultada(), getUsuarioLogado());
			setSituacaoCancelamentoBloqueioMatricula("ADIAMENTO_REALIZADO_COM_SUCESSO");
			limparCurso();
			limparTurma();
			limparDadosAluno();
			setMensagemID("msg_dados_adiamentoSuspensaoMatricula");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setSituacaoCancelamentoBloqueioMatricula("");
		}
	}
	
	public Boolean getRealizarAdiamentoSuspensaoMatricula() {
		if (realizarAdiamentoSuspensaoMatricula == null) {
			realizarAdiamentoSuspensaoMatricula = false;
		}
		return realizarAdiamentoSuspensaoMatricula;
	}

	public void setRealizarAdiamentoSuspensaoMatricula(Boolean realizarAdiamentoSuspensaoMatricula) {
		this.realizarAdiamentoSuspensaoMatricula = realizarAdiamentoSuspensaoMatricula;
	}
	
	public String getSituacaoCancelamentoBloqueioMatricula() {
		if (situacaoCancelamentoBloqueioMatricula == null) {
			situacaoCancelamentoBloqueioMatricula = "";
		}
		return situacaoCancelamentoBloqueioMatricula;
	}
	
	public void setSituacaoCancelamentoBloqueioMatricula(String situacaoCancelamentoBloqueioMatricula) {
		this.situacaoCancelamentoBloqueioMatricula = situacaoCancelamentoBloqueioMatricula;
	}
	
	public String getOncompleteCancelamentoModalBloqueioMatricula() {
		if (getSituacaoCancelamentoBloqueioMatricula().equals("CANCELAMENTO_REALIZADO_COM_SUCESSO") || getSituacaoCancelamentoBloqueioMatricula().equals("ADIAMENTO_REALIZADO_COM_SUCESSO")) {
			return "RichFaces.$('panelCancelarBloquearMatricula').hide()";
		}
		return "";
	}
	
	public MatriculaVO getMatriculaConsultada() {
		if (matriculaConsultada == null) {
			matriculaConsultada = new MatriculaVO();
		}
		return matriculaConsultada;
	}
	
	public void setMatriculaConsultada(MatriculaVO matriculaConsultada) {
		this.matriculaConsultada = matriculaConsultada;
	}
	
	@PostConstruct
	public void buscarDadosMapaSuspensaoMatriculaVindoDaTelaDeMatriculaRenovacao() {
		if (context().getExternalContext().getSessionMap().get("matricula") != null) {
			setMatriculaVO((MatriculaVO)context().getExternalContext().getSessionMap().get("matricula"));
			getUnidadeEnsinoVOs().add(getMatriculaVO().getUnidadeEnsino());
			//setUnidadeEnsinoVO(getMatriculaVO().getUnidadeEnsino());
			setTipoSuspensaoMatricula("pendenciaSolicitacao");
			try {				
				setListaConsulta(getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaSuspensaParaSerListadaParaCancelamento(getUnidadeEnsinoVOs(), getMatriculaVO().getMatricula(), getUnidadeEnsinoCursoVO().getCurso().getCodigo() , getTurmaVO().getCodigo(), getConfiguracaoGeralPadraoSistema().getConfiguracoesVO().getNrDiasListarMatriculaPendenciaDocumentosParaCancelamento(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getTipoSuspensaoMatricula(), getSituacaoSolicitacao(), getPermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira(), getPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira(), getPermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica(), getPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica(), getPermitirUsuarioVisualizarApenasSuasSolicitacoes(), getPermitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade()));
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}	
		}
	}
	
	public void verificarPermissaoCancelarSuspensaoMatricula() {
		try {
			setOncompleteModal("");
			if(ControleAcesso.verificarPermissaoFuncionalidadeUsuario("MapaSuspensaoMatricula_PermitirLiberarSuspensaoMatricula", getUsuarioLogado())) {
				setPermitirCancelarSuspensaoMatricula(Boolean.TRUE);	
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}
	
	public void verificarPermissaoLiberarSuspensaoPendencias(String tipoSuspensao) {
		try {
			getListaContaReceber().clear();
			getHorarioAlunoTurnoVOs().clear();
			//setMsgPermissaoNegadaLiberacaoSuspensaoMatriculaPendencia("");
			MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
			setMatriculaVO(obj.getMatriculaVO());

			getMatriculaVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
			getFacadeFactory().getMatriculaFacade().carregarDados(getMatriculaVO(), NivelMontarDados.TODOS, getUsuarioLogado());			


				if((getPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica() || getPermitirUsuarioVisualizarApenasSuasSolicitacoes()) && tipoSuspensao.equals("suspensaoMatriculaPendenciaAcademica")) {
					consultarPendenciaMatricula(MotivoSolicitacaoLiberacaoMatriculaEnum.SOLICITAR_LIBERACAO_MATRICULA_APOS_X_MODULOS);
					setMatriculaPeriodoTurmaDisciplinaVOs(getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultarRapidaPorMatriculaTurmaDisciplinaAnoSemestreGradeCurricularAtual(getMatriculaVO().getMatricula(), 0, 0, Uteis.getAnoDataAtual4Digitos(), Uteis.getSemestreAtual(), false, true, true, true, false, "'PC'", "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					
					setHorarioAlunoTurnoVOs(getFacadeFactory().getHorarioAlunoFacade().consultarMeusHorariosDisciplinaAluno(getMatriculaPeriodoTurmaDisciplinaVOs(), getMatriculaVO().getUnidadeEnsino(), getUsuarioLogado(), false));
					
					setOncompleteModal("RichFaces.$('panelPendenciaMatricula').show()");					
				}
				else if((getPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira() || getPermitirUsuarioVisualizarApenasSuasSolicitacoes()) && tipoSuspensao.equals("suspensaoMatriculaPendenciaFinanceira")) {
					consultarPendenciaMatricula(MotivoSolicitacaoLiberacaoMatriculaEnum.SOLICITAR_APROVACAO_LIBERACAO_FINANCEIRA);
					getMatriculaPeriodoVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
					getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(obj, NivelMontarDados.TODOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());				
					
					setListaContaReceber(getFacadeFactory().getContaReceberFacade().consultaRapidaPorAlunoEMatriculaContasReceberVencidas(getMatriculaVO().getAluno().getCodigo(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo()).getUtilizarIntegracaoFinanceira(), getMatriculaPeriodoVO(), getUsuarioLogado()));
					setOncompleteModal("RichFaces.$('panelPendenciaMatricula').show()");					
				}	
				/*else {
					if(tipoSuspensao.equals("suspensaoMatriculaPendenciaAcademica")) {
						setMsgPermissaoNegadaLiberacaoSuspensaoMatriculaPendencia(getMensagemInternalizacao("msg_MapaSuspensaoMatricula_permissaoNegadaLiberarSuspensaoMatriculaPendenciaAcademica"));
						limparDadosAluno();
					}
					else if(tipoSuspensao.equals("suspensaoMatriculaPendenciaFinanceira")) {
						setMsgPermissaoNegadaLiberacaoSuspensaoMatriculaPendencia(getMensagemInternalizacao("msg_MapaSuspensaoMatricula_permissaoNegadaLiberarSuspensaoMatriculaPendenciaFinanceira"));
						limparDadosAluno();
					}
					setOncompleteModal("RichFaces.$('panelAvisoPermissaoNegada').show()");
				}*/
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public PendenciaLiberacaoMatriculaVO getPendenciaLiberacaoMatriculaVO() {
		if(pendenciaLiberacaoMatriculaVO == null) {
			pendenciaLiberacaoMatriculaVO = new PendenciaLiberacaoMatriculaVO();
		}
		return pendenciaLiberacaoMatriculaVO;
	}

	public void setPendenciaLiberacaoMatriculaVO(PendenciaLiberacaoMatriculaVO pendenciaLiberacaoMatriculaVO) {
		this.pendenciaLiberacaoMatriculaVO = pendenciaLiberacaoMatriculaVO;
	}
	
	public void consultarPendenciaMatricula(MotivoSolicitacaoLiberacaoMatriculaEnum motivoSolicitacaoMatriculaEnum) {
		try {
			MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodoItens");
			setMatriculaPeriodoVO(obj);			
			setPendenciaLiberacaoMatriculaVO(getFacadeFactory().getPendenciaLiberacaoMatriculaInterfaceFacade().consultarPendenciaLiberacaoMatriculaPendentePorMatriculaEMotivo(obj.getMatricula(), motivoSolicitacaoMatriculaEnum, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema()));
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}
	
	public List<SelectItem> getListaSelectItemTipoSuspensaoMatricula() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		if(getPermitirCancelarSuspensaoMatricula()) {
			itens.add(new SelectItem("suspensaoMatricula", "Suspensão Matrícula"));
		}
		if(getPermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica() || getPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica() || getPermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira() || getPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira() || getPermitirUsuarioVisualizarApenasSuasSolicitacoes() || getPermitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade()) {
			itens.add(new SelectItem("pendenciaSolicitacao", "Pendência Solicitação"));
		}
		return itens;
	}

	public String getTipoSuspensaoMatricula() {
		if(tipoSuspensaoMatricula == null || tipoSuspensaoMatricula.equals("")) {
			if(getPermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica() || getPermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira() || getPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica() || getPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira() || getPermitirUsuarioVisualizarApenasSuasSolicitacoes() || getPermitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade()) {
				setTipoSuspensaoMatricula("pendenciaSolicitacao");
			}else {
				setTipoSuspensaoMatricula("suspensaoMatricula");
			}
		}
		return tipoSuspensaoMatricula;
	}

	public void setTipoSuspensaoMatricula(String tipoSuspensaoMatricula) {
		this.tipoSuspensaoMatricula = tipoSuspensaoMatricula;
	}
	
	
	public List<SelectItem> getListaSelectItemSituacaoSolicitacao() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
			itens.add(new SelectItem("pendenteAprovacao", "Pendente de Aprovação"));
			itens.add(new SelectItem("deferido", "Deferido"));
			itens.add(new SelectItem("indeferido", "Indeferido"));

		return itens;
	}
	
	public String getSituacaoSolicitacao() {
		if(situacaoSolicitacao == null || situacaoSolicitacao.equals("")) {
			setSituacaoSolicitacao("pendenteAprovacao");
		}
		return situacaoSolicitacao;
	}

	public void setSituacaoSolicitacao(String situacaoSolicitacao) {
		this.situacaoSolicitacao = situacaoSolicitacao;
	}

	public Boolean getPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica() {
		if(permitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica == null) {
			permitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica = Boolean.FALSE;
		}
		return permitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica;
	}

	public void setPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica(
			Boolean permitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica) {
		this.permitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica = permitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica;
	}

	public Boolean getPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira() {
		if(permitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira == null) {
			permitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira = Boolean.FALSE;
		}
		return permitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira;
	}

	public void setPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira(
			Boolean permitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira) {
		this.permitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira = permitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira;
	}
	
	public void verificarPermissaoLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica()
	{
		try {
			if(ControleAcesso.verificarPermissaoFuncionalidadeUsuario("MapaSuspensaoMatricula_PermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica", getUsuarioLogado())){
				setPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaAcademica(Boolean.TRUE);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}		
	}
	
	public void verificarPermissaoVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica()
	{
		try {
			if(ControleAcesso.verificarPermissaoFuncionalidadeUsuario("MapaSuspensaoMatricula_PermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica", getUsuarioLogado())){
				setPermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica(Boolean.TRUE);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}		
	}
	
	public void verificarPermissaoLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira()
	{
		try {
			if(ControleAcesso.verificarPermissaoFuncionalidadeUsuario("MapaSuspensaoMatricula_PermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira", getUsuarioLogado())){
				setPermitirLiberarSolicitacaoSuspensaoMatriculaPendenciaFinanceira(Boolean.TRUE);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}		
	}
	
	public void verificarPermissaoVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira()
	{
		try {
			if(ControleAcesso.verificarPermissaoFuncionalidadeUsuario("MapaSuspensaoMatricula_PermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira", getUsuarioLogado())){
				setPermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira(Boolean.TRUE);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}		
	}
	
	public void verificarPermissaoUsuarioVisualizarApenasSuasSolicitacoes()
	{
		try {
			if(ControleAcesso.verificarPermissaoFuncionalidadeUsuario("MapaSuspensaoMatricula_PermitirUsuarioVisualizarApenasSuasSolicitacoes", getUsuarioLogado())){
				setPermitirUsuarioVisualizarApenasSuasSolicitacoes(Boolean.TRUE);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}		
	}
	
	public void verificarPermissaoVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidades()
	{
		try {
			if(ControleAcesso.verificarPermissaoFuncionalidadeUsuario("MapaSuspensaoMatricula_PermitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade", getUsuarioLogado())){
				setPermitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade(Boolean.TRUE);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}		
	}
	
	public void indeferirLiberacaoPendenciaMatricula() {				
		try {			
			getCancelamentoVO().setData(new Date());
			getCancelamentoVO().setMatricula(getPendenciaLiberacaoMatriculaVO().getMatricula());
			if(getPendenciaLiberacaoMatriculaVO().getMotivoSolicitacao().equals(MotivoSolicitacaoLiberacaoMatriculaEnum.SOLICITAR_LIBERACAO_MATRICULA_APOS_X_MODULOS)) {
				getCancelamentoVO().setJustificativa("Cancelamento de Matrícula devido a não liberação da Matricula Após X Módulos");
			}
			else if(getPendenciaLiberacaoMatriculaVO().getMotivoSolicitacao().equals(MotivoSolicitacaoLiberacaoMatriculaEnum.SOLICITAR_APROVACAO_LIBERACAO_FINANCEIRA)) {
				getCancelamentoVO().setJustificativa("Cancelamento de Matrícula devido a não liberação da Matrícula Com Débitos Financeiros");
			}
			getCancelamentoVO().setResponsavelAutorizacao(getUsuarioLogadoClone());
			getCancelamentoVO().setSituacao("FD");
			getCancelamentoVO().setDescricao(getPendenciaLiberacaoMatriculaVO().getMotivoIndeferimento());
			
			getFacadeFactory().getPendenciaLiberacaoMatriculaInterfaceFacade().indeferirLiberacaoPendenciaMatricula(getPendenciaLiberacaoMatriculaVO(), cancelamentoVO, getUsuarioLogado(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getMatriculaVO().getUnidadeEnsino().getCodigo()), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo()));
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoMatriculaPendenteAprovacaoIndeferido(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_MATRICULA_PENDENTE_APROVACAO_INDEFERIDO, getMatriculaPeriodoVO(), getPendenciaLiberacaoMatriculaVO(), getUsuarioLogado());
			/*setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			limparCurso();
			limparTurma();
			*/
			limparDadosAluno();
			setCancelamentoVO(new CancelamentoVO());
			consultarMatriculaSuspensa();
			setMensagemID("msg_dados_indeferido");
			setOnCompleteModalIndeferimento("RichFaces.$('panelJustificativa').hide();RichFaces.$('panelPendenciaMatricula').hide()");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setOnCompleteModalIndeferimento("");
		}		
		
	}
	
	public void deferirLiberacaoPendenciaMatricula() {		
		try {			
			
			Boolean existeOutraPendencia = getFacadeFactory().getPendenciaLiberacaoMatriculaInterfaceFacade().verificarSeOutraPendenciaExistente(getMatriculaVO().getMatricula(), getPendenciaLiberacaoMatriculaVO().getCodigo());
			
			if(!existeOutraPendencia) {
				getMatriculaVO().setMatricula(getPendenciaLiberacaoMatriculaVO().getMatricula().getMatricula());
				getMatriculaVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
				getFacadeFactory().getMatriculaFacade().carregarDados(getMatriculaVO(), NivelMontarDados.TODOS, getUsuarioLogado());			
				getMatriculaPeriodoVO().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
				getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(getMatriculaPeriodoVO(), NivelMontarDados.TODOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
				getFacadeFactory().getMatriculaFacade().inicializarTextoContratoPlanoFinanceiroAluno(getMatriculaVO(), getMatriculaPeriodoVO(), true, getUsuarioLogado());
				getFacadeFactory().getMatriculaFacade().gerenciarEntregaDocumentoMatricula(getMatriculaVO(), getUsuarioLogado());
				getFacadeFactory().getMatriculaPeriodoFacade().inicializarDadosDefinirDisciplinasMatriculaPeriodo(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado(), null, false, false);
				getMatriculaPeriodoVO().setPlanoFinanceiroCursoPersistido(getMatriculaPeriodoVO().getPlanoFinanceiroCurso());
				getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCursoPersistido(getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso());
				getFacadeFactory().getMatriculaFacade().inicializarPlanoFinanceiroMatriculaPeriodo(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado());
				getMatriculaVO().setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				getFacadeFactory().getMatriculaPeriodoFacade().inicializarPlanoFinanceiroAlunoMatriculaPeriodo(getMatriculaVO(), getMatriculaPeriodoVO(), getMatriculaVO().getPlanoFinanceiroAluno(), true, getUsuarioLogado());
				getFacadeFactory().getMatriculaPeriodoFacade().realizarCalculoValorMatriculaEMensalidade(getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado());
				getFacadeFactory().getMatriculaFacade().calcularTotalDesconto(getMatriculaVO(), getMatriculaPeriodoVO(), getMatriculaVO().getPlanoFinanceiroAluno().obterOrdemAplicacaoDescontosPadraoAtual(), getUsuarioLogado(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()));
				getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(getMatriculaVO(), getUsuarioLogado());				
			}
			
			getFacadeFactory().getPendenciaLiberacaoMatriculaInterfaceFacade().deferirLiberacaoPendenciaMatricula(getPendenciaLiberacaoMatriculaVO(), getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaVO().getUnidadeEnsino().getCodigo()), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade(getMatriculaVO().getUnidadeEnsino().getCodigo()));
			
			if(!existeOutraPendencia) {
				getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoMatriculaPendenteAprovacaoDeferido(TemplateMensagemAutomaticaEnum.MENSAGEM_NOTIFICACAO_MATRICULA_PENDENTE_APROVACAO_DEFERIDO, getMatriculaPeriodoVO(), getPendenciaLiberacaoMatriculaVO(), getMatriculaVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
			}
			//if(existeOutraPendencia) {
				limparDadosAluno();
				consultarMatriculaSuspensa();
			//}
			/*else {
				setUnidadeEnsinoVO(new UnidadeEnsinoVO());
				limparCurso();
				limparTurma();
				limparDadosAluno();
			}*/

			setMensagemID("msg_dados_deferido");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if(matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public CancelamentoVO getCancelamentoVO() {
		if(cancelamentoVO == null) {
			cancelamentoVO = new CancelamentoVO();
		}
		return cancelamentoVO;
	}

	public void setCancelamentoVO(CancelamentoVO cancelamentoVO) {
		this.cancelamentoVO = cancelamentoVO;
	}
	
	/**
	 * @return the listaSelectItemMotivoCancelamentoTrancamento
	 */
	public List<SelectItem> getListaSelectItemMotivoCancelamentoTrancamento() {
		if (listaSelectItemMotivoCancelamentoTrancamento == null) {
			listaSelectItemMotivoCancelamentoTrancamento = new ArrayList<SelectItem>();
		}
		return listaSelectItemMotivoCancelamentoTrancamento;
	}

	/**
	 * @param listaSelectItemMotivoCancelamentoTrancamento
	 *            the listaSelectItemMotivoCancelamentoTrancamento to set
	 */
	public void setListaSelectItemMotivoCancelamentoTrancamento(List<SelectItem> listaSelectItemMotivoCancelamentoTrancamento) {
		this.listaSelectItemMotivoCancelamentoTrancamento = listaSelectItemMotivoCancelamentoTrancamento;
	}
	
	public void montarListaSelectItemMotivoCancelamentoTrancamento(String motivoSolicitacaoLiberacaoMatricula) throws Exception {
		try {
			String tipoJustificativa = "";
			if(motivoSolicitacaoLiberacaoMatricula.equals(MotivoSolicitacaoLiberacaoMatriculaEnum.SOLICITAR_APROVACAO_LIBERACAO_FINANCEIRA.name())) {
				tipoJustificativa = TipoJustificativaCancelamento.INDEFERIMENTO_POR_PENDENCIA_FINANCEIRA.getValor();
			}
			else if(motivoSolicitacaoLiberacaoMatricula.equals(MotivoSolicitacaoLiberacaoMatriculaEnum.SOLICITAR_LIBERACAO_MATRICULA_APOS_X_MODULOS.name())) {
				tipoJustificativa = TipoJustificativaCancelamento.INDEFERIMENTO_POR_PENDENCIA_ACADEMICA.getValor();
			}
			
			List<MotivoCancelamentoTrancamentoVO> resultadoConsulta = consultarPorTipoJustificativaSituacao(tipoJustificativa);
			setListaSelectItemMotivoCancelamentoTrancamento(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<MotivoCancelamentoTrancamentoVO> consultarPorTipoJustificativaSituacao(String tipoJustificativa) throws Exception {
		return getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorTipoJustificativaSituacao(tipoJustificativa, "AT");
	}

	public String getOnCompleteModalIndeferimento() {
		if(onCompleteModalIndeferimento == null) {
			onCompleteModalIndeferimento = "";
		}
		return onCompleteModalIndeferimento;
	}

	public void setOnCompleteModalIndeferimento(String onCompleteModalIndeferimento) {
		this.onCompleteModalIndeferimento = onCompleteModalIndeferimento;
	}

	public Boolean getPermitirCancelarSuspensaoMatricula() {
		if(permitirCancelarSuspensaoMatricula == null) {
			permitirCancelarSuspensaoMatricula = Boolean.FALSE;
		}
		return permitirCancelarSuspensaoMatricula;
	}

	public void setPermitirCancelarSuspensaoMatricula(Boolean permitirCancelarSuspensaoMatricula) {
		this.permitirCancelarSuspensaoMatricula = permitirCancelarSuspensaoMatricula;
	}

	/*public String getMsgPermissaoNegadaLiberacaoSuspensaoMatriculaPendencia() {
		if(msgPermissaoNegadaLiberacaoSuspensaoMatriculaPendencia == null) {
			msgPermissaoNegadaLiberacaoSuspensaoMatriculaPendencia = "";
		}
		return msgPermissaoNegadaLiberacaoSuspensaoMatriculaPendencia;
	}

	public void setMsgPermissaoNegadaLiberacaoSuspensaoMatriculaPendencia(
			String msgPermissaoNegadaLiberacaoSuspensaoMatriculaPendencia) {
		this.msgPermissaoNegadaLiberacaoSuspensaoMatriculaPendencia = msgPermissaoNegadaLiberacaoSuspensaoMatriculaPendencia;
	}*/

	public Boolean getPermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica() {
		if(permitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica == null) {
			permitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica = Boolean.FALSE;
		}
		return permitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica;
	}

	public void setPermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica(
			Boolean permitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica) {
		this.permitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica = permitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaAcademica;
	}

	public Boolean getPermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira() {
		if(permitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira == null) {
			permitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira = Boolean.FALSE;
		}
		return permitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira;
	}

	public void setPermitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira(
			Boolean permitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira) {
		this.permitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira = permitirVisualizarSolicitacaoSuspensaoMatriculaPendenciaFinanceira;
	}
	
	/**
	 * @return the listaContaReceber
	 */
	public List<ContaReceberVO> getListaContaReceber() {
		if (listaContaReceber == null) {
			listaContaReceber = new ArrayList<ContaReceberVO>();
		}
		return listaContaReceber;
	}

	/**
	 * @param listaContaReceber
	 *            the listaContaReceber to set
	 */
	public void setListaContaReceber(List<ContaReceberVO> listaContaReceber) {
		this.listaContaReceber = listaContaReceber;
	}
	
	public List<MatriculaPeriodoTurmaDisciplinaVO> getMatriculaPeriodoTurmaDisciplinaVOs() {
		if (matriculaPeriodoTurmaDisciplinaVOs == null) {
			matriculaPeriodoTurmaDisciplinaVOs = new ArrayList<MatriculaPeriodoTurmaDisciplinaVO>(0);
		}
		return matriculaPeriodoTurmaDisciplinaVOs;
	}

	public void setMatriculaPeriodoTurmaDisciplinaVOs(List<MatriculaPeriodoTurmaDisciplinaVO> matriculaPeriodoTurmaDisciplinaVOs) {
		this.matriculaPeriodoTurmaDisciplinaVOs = matriculaPeriodoTurmaDisciplinaVOs;
	}

	public List<HorarioAlunoTurnoVO> getHorarioAlunoTurnoVOs() {
		if (horarioAlunoTurnoVOs == null) {
			horarioAlunoTurnoVOs = new ArrayList<HorarioAlunoTurnoVO>(0);
		}
		return horarioAlunoTurnoVOs;
	}

	public void setHorarioAlunoTurnoVOs(List<HorarioAlunoTurnoVO> horarioAlunoTurnoVOs) {
		this.horarioAlunoTurnoVOs = horarioAlunoTurnoVOs;
	}

	public Boolean getPermitirUsuarioVisualizarApenasSuasSolicitacoes() {
		if(permitirUsuarioVisualizarApenasSuasSolicitacoes == null) {
			permitirUsuarioVisualizarApenasSuasSolicitacoes = Boolean.FALSE;
		}
		return permitirUsuarioVisualizarApenasSuasSolicitacoes;
	}

	public void setPermitirUsuarioVisualizarApenasSuasSolicitacoes(
			Boolean permitirUsuarioVisualizarApenasSuasSolicitacoes) {
		this.permitirUsuarioVisualizarApenasSuasSolicitacoes = permitirUsuarioVisualizarApenasSuasSolicitacoes;
	}

	public Boolean getPermitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade() {
		if(permitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade == null) {
			permitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade = Boolean.FALSE;
		}
		return permitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade;
	}

	public void setPermitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade(
			Boolean permitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade) {
		this.permitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade = permitirVisualizarPendenciasDeferidasIndeferidasOutrosUsuariosMesmaUnidade;
	}

}
