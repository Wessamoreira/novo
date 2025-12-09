package controle.academico;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.HorarioTurmaVO;
import negocio.comuns.academico.SolicitacaoAberturaTurmaDisciplinaVO;
import negocio.comuns.academico.SolicitacaoAberturaTurmaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoSolicitacaoAberturaTurmaEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

@Controller("SolicitacaoAberturaTurmaControle")
@Lazy
@Scope("viewScope")
public class SolicitacaoAberturaTurmaControle extends SuperControle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3027762051135374385L;
	private SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO;
	private SolicitacaoAberturaTurmaDisciplinaVO solicitacaoAberturaTurmaDisciplinaVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemDisciplina;
	private List<SelectItem> listaSelectItemSituacao;
	private List<UnidadeEnsinoCursoVO> listaConsultaCurso;
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private List<PessoaVO> listaConsultaProfessor;
	private String valorConsultaProfessor;
	private String campoConsultaProfessor;
	private List<PessoaVO> listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private List<TurmaVO> listaConsultaTurma;
	private UnidadeEnsinoCursoVO unidadeEnsinoCurso;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private SituacaoSolicitacaoAberturaTurmaEnum situacaoSolicitacaoAberturaTurma;
	private ComunicacaoInternaVO notificacao;
	private Integer index;

	public String editar() {
		try {
			setSolicitacaoAberturaTurmaVO((SolicitacaoAberturaTurmaVO) getRequestMap().get("solicitacaoAberturaTurmaItens"));
			getSolicitacaoAberturaTurmaVO().setSolicitacaoAberturaTurmaDisciplinaVOs(getFacadeFactory().getSolicitacaoAberturaTurmaDisciplinaFacade().consultarPorSolicitacaoAberturaTurma(getSolicitacaoAberturaTurmaVO().getCodigo()));
			realizarGeracaoCalendarioSolicitacaoAberturaTurma();
			inicializarListaDisciplina();
			setMensagemID("msg_entre_dados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("solicitarAberturaTurmaForm.xhtml");
	}

	public String novo() {
		setSolicitacaoAberturaTurmaVO(null);
		setMensagemID("msg_entre_dados", Uteis.ALERTA);
		return Uteis.getCaminhoRedirecionamentoNavegacao("solicitarAberturaTurmaForm.xhtml");
	}

	public void persistir() {
		try {
			if (getSolicitacaoAberturaTurmaVO().getSituacaoSolicitacaoAberturaTurma().equals(SituacaoSolicitacaoAberturaTurmaEnum.EM_REVISAO)) {
				getFacadeFactory().getSolicitacaoAberturaTurmaFacade().registrarRevisaoRealizadaAberturaTurma(getSolicitacaoAberturaTurmaVO(), getUsuarioLogado());
			} else {
				getFacadeFactory().getSolicitacaoAberturaTurmaFacade().persitir(getSolicitacaoAberturaTurmaVO(), getUsuarioLogado());
			}
			realizarGeracaoCalendarioSolicitacaoAberturaTurma();
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarAutorizacaoSolicitacaoAberturaTurma() {
		try {
			getFacadeFactory().getSolicitacaoAberturaTurmaFacade().registrarAutorizacaoAberturaTurma(getSolicitacaoAberturaTurmaVO(), getUsuarioLogado());
			inicializarListaDisciplina();
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarNaoAutorizacaoSolicitacaoAberturaTurma() {
		try {
			getFacadeFactory().getSolicitacaoAberturaTurmaFacade().registrarNaoAutorizacaoAberturaTurma(getSolicitacaoAberturaTurmaVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarRevisaoSolicitacaoAberturaTurma() {
		try {
			getFacadeFactory().getSolicitacaoAberturaTurmaFacade().registrarRevisaoAberturaTurma(getSolicitacaoAberturaTurmaVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarFinalizacaoSolicitacaoAberturaTurma() {
		try {
			getFacadeFactory().getSolicitacaoAberturaTurmaFacade().registrarFinalizacaoAberturaTurma(getSolicitacaoAberturaTurmaVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarSolicitacaoAberturaTurmaDisciplina() {
		try {
			getFacadeFactory().getSolicitacaoAberturaTurmaFacade().adicionarSolicitacaoAberturaTurma(getSolicitacaoAberturaTurmaVO());

		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void realizarGeracaoCalendarioSolicitacaoAberturaTurma() {
		try {
			getFacadeFactory().getSolicitacaoAberturaTurmaFacade().realizarGeracaoCalendarioSolicitacaoAberturaTurma(getSolicitacaoAberturaTurmaVO());			
			limparMensagem();			
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inicializarListaDisciplina() throws Exception {
		getListaSelectItemDisciplina().clear();
		if (getSolicitacaoAberturaTurmaVO().getSituacaoSolicitacaoAberturaTurma().equals(SituacaoSolicitacaoAberturaTurmaEnum.AUTORIZADO)) {
			List<TurmaDisciplinaVO> turmaDisciplinaVOs = getFacadeFactory().getTurmaDisciplinaFacade().consultarPorCodigoTurma(getSolicitacaoAberturaTurmaVO().getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			getListaSelectItemDisciplina().add(new SelectItem(0, ""));
			for (TurmaDisciplinaVO turmaDisciplinaVO : turmaDisciplinaVOs) {
				getListaSelectItemDisciplina().add(new SelectItem(turmaDisciplinaVO.getDisciplina().getCodigo(), turmaDisciplinaVO.getDisciplina().getNome()));
			}
		}

	}
	
	
	private Boolean notificacaoEnviadaSucesso;
	public void enviarNotificacao() {
		try {
			getFacadeFactory().getSolicitacaoAberturaTurmaFacade().realizarEnvioNotificacao(getNotificacao(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setNotificacao(null);
			setNotificacaoEnviadaSucesso(true);
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setNotificacaoEnviadaSucesso(false);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	
	public Boolean getNotificacaoEnviadaSucesso() {
		if(notificacaoEnviadaSucesso == null){
			notificacaoEnviadaSucesso = false;
		}
		return notificacaoEnviadaSucesso;
	}

	public void setNotificacaoEnviadaSucesso(Boolean notificacaoEnviadaSucesso) {
		this.notificacaoEnviadaSucesso = notificacaoEnviadaSucesso;
	}

	public String getFecharModalNotificacao(){
		if(getNotificacaoEnviadaSucesso()){
			return "RichFaces.$('panelComunicado').hide()";
		}
		return "";
	} 

	public void abrirModalNotificacao() {
		limparMensagem();
		setNotificacao(null);
		getNotificacao().setPessoa(getSolicitacaoAberturaTurmaVO().getUsuarioSolicitacao().getPessoa());
		getNotificacao().setMensagem(getNotificacao().getMensagemComLayout());
		setNotificacaoEnviadaSucesso(false);
	}

	//@PostConstruct
	public String irPaginaConsulta() {
		setControleConsultaOtimizado(null);
		setSolicitacaoAberturaTurmaVO(null);
		setUnidadeEnsinoCurso(null);
		setSituacaoSolicitacaoAberturaTurma(SituacaoSolicitacaoAberturaTurmaEnum.AGUARDANDO_AUTORIZACAO);
		getSolicitacaoAberturaTurmaVO().getUnidadeEnsino().setCodigo(getUnidadeEnsinoLogado().getCodigo());
		getControleConsultaOtimizado().setPaginaAtual(1);
		consultar();
		return Uteis.getCaminhoRedirecionamentoNavegacao("solicitarAberturaTurmaCons.xhtml");
	}

	
	public String consultar() {
		try {
			getControleConsultaOtimizado().setLimitePorPagina(10);
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getSolicitacaoAberturaTurmaFacade().consultar(getSolicitacaoAberturaTurmaVO().getUnidadeEnsino().getCodigo(), getUnidadeEnsinoCurso().getCodigo(), getSolicitacaoAberturaTurmaVO().getTurma().getIdentificadorTurma(), getSituacaoSolicitacaoAberturaTurma(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getSolicitacaoAberturaTurmaFacade().consultarTotalRegistro(getSolicitacaoAberturaTurmaVO().getUnidadeEnsino().getCodigo(), getUnidadeEnsinoCurso().getCodigo(), getSolicitacaoAberturaTurmaVO().getTurma().getIdentificadorTurma(), getSituacaoSolicitacaoAberturaTurma()));
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (ConsistirException ex) {
			setConsistirExceptionMensagemDetalhada("msg_erro", ex, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("solicitarAberturaTurmaCons.xhtml");
	}

	private List<SelectItem> tipoConsultaComboProfessor;

	public List<SelectItem> getTipoConsultaComboProfessor() {
		if (tipoConsultaComboProfessor == null) {
			tipoConsultaComboProfessor = new ArrayList<SelectItem>(0);
			tipoConsultaComboProfessor.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboProfessor.add(new SelectItem("cpf", "CPF"));
		}
		return tipoConsultaComboProfessor;
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getSolicitacaoAberturaTurmaVO().getUnidadeEnsino().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getSolicitacaoAberturaTurmaVO().getUnidadeEnsino().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getSolicitacaoAberturaTurmaVO().getUnidadeEnsino().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getSolicitacaoAberturaTurmaVO().getUnidadeEnsino().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			getSolicitacaoAberturaTurmaVO().setTurma((TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens"));
			inicializarDadosTurma();
			limparMensagem();
		} catch (Exception e) {
			limparTurma();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private List<SelectItem> tipoConsultaComboTurma;

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
			tipoConsultaComboTurma.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
			tipoConsultaComboTurma.add(new SelectItem("nomeTurno", "Turno"));
			tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboTurma;
	}

	public void inicializarDadosTurma() throws Exception {
		HorarioTurmaVO horarioTurmaVO = null;
		if (getSolicitacaoAberturaTurmaVO().getTurma().getCurso().getNivelEducacional().equals("PO") || getSolicitacaoAberturaTurmaVO().getTurma().getCurso().getNivelEducacional().equals("EX")) {
			horarioTurmaVO = getFacadeFactory().getHorarioTurmaFacade().consultarPorCodigoTurmaUnico(getSolicitacaoAberturaTurmaVO().getTurma().getCodigo(), "", "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			horarioTurmaVO.setAnoVigente("");
			horarioTurmaVO.setSemestreVigente("");
		} else {
			horarioTurmaVO = getFacadeFactory().getHorarioTurmaFacade().consultarPorCodigoTurmaUnico(getSolicitacaoAberturaTurmaVO().getTurma().getCodigo(), Uteis.getSemestreAtual(), Uteis.getAnoDataAtual(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			horarioTurmaVO.setAnoVigente(Uteis.getAnoDataAtual());
			horarioTurmaVO.setSemestreVigente(Uteis.getSemestreAtual());
		}
		if (horarioTurmaVO.getCodigo() > 0) {
			throw new Exception("Já existe uma programação de aula para esta turma. Este cadastro é exclusivo para turmas sem aulas programadas");
		}
		SolicitacaoAberturaTurmaVO sat = getFacadeFactory().getSolicitacaoAberturaTurmaFacade().consultarSolicitacaoAberturaTurmaEmAbertoPorTurma(getSolicitacaoAberturaTurmaVO().getTurma().getCodigo());
		if (sat != null) {
			setSolicitacaoAberturaTurmaVO(sat);
			getSolicitacaoAberturaTurmaVO().setSolicitacaoAberturaTurmaDisciplinaVOs(getFacadeFactory().getSolicitacaoAberturaTurmaDisciplinaFacade().consultarPorSolicitacaoAberturaTurma(getSolicitacaoAberturaTurmaVO().getCodigo()));
			realizarGeracaoCalendarioSolicitacaoAberturaTurma();
		} else {
			getSolicitacaoAberturaTurmaVO().setQuantidadeModulo(getFacadeFactory().getTurmaDisciplinaFacade().consultarTotalDisciplinaTurma(getSolicitacaoAberturaTurmaVO().getTurma().getCodigo()));
			adicionarSolicitacaoAberturaTurmaDisciplina();
		}
		

	}

	public void consultarTurmaPorIdentificador() {
		try {
			getSolicitacaoAberturaTurmaVO().setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getSolicitacaoAberturaTurmaVO().getTurma(), getSolicitacaoAberturaTurmaVO().getTurma().getIdentificadorTurma(), getSolicitacaoAberturaTurmaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			inicializarDadosTurma();
			setMensagemID("msg_dados_consultados", Uteis.SUCESSO);
		} catch (Exception e) {
			limparTurma();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void limparTurma() {
		getSolicitacaoAberturaTurmaVO().setTurma(null);
		getSolicitacaoAberturaTurmaVO().setQuantidadeModulo(null);
		getSolicitacaoAberturaTurmaVO().getCalendarioHorarioAulaVOs().clear();
		getSolicitacaoAberturaTurmaVO().getSolicitacaoAberturaTurmaDisciplinaVOs().clear();
		setUnidadeEnsinoCurso(null);
	}

	public void paginarConsulta(DataScrollEvent event) {
		getControleConsultaOtimizado().setPaginaAtual(event.getPage());
		getControleConsultaOtimizado().setPage(event.getPage());
		consultar();
	}

	private List<SelectItem> tipoConsultaComboCurso;

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboCurso;
	}

	public void consultarCurso() {
		try {
			List<UnidadeEnsinoCursoVO> objs = new ArrayList<UnidadeEnsinoCursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidade(valorInt, getSolicitacaoAberturaTurmaVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), getSolicitacaoAberturaTurmaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<UnidadeEnsinoCursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
			setUnidadeEnsinoCurso(unidadeEnsinoCurso);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void abrirModalConsultaProfessor() {
		setSolicitacaoAberturaTurmaDisciplinaVO(new SolicitacaoAberturaTurmaDisciplinaVO());
		setSolicitacaoAberturaTurmaDisciplinaVO((SolicitacaoAberturaTurmaDisciplinaVO) getRequestMap().get("disciplinaItens"));
	}

	public Boolean getApresentarCampoCpf() {
		if (getCampoConsultaProfessor().equals("cpf")) {
			return true;
		}
		return false;
	}

	public void consultarProfessor() {
		try {
			super.consultar();
			List<PessoaVO> objs = new ArrayList<PessoaVO>(0);
			if (getCampoConsultaProfessor().equals("nome")) {
				if (getValorConsultaProfessor().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("cpf")) {
				if (getValorConsultaProfessor().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSLOGIN, getUsuarioLogado());
			}
			setListaConsultaProfessor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProfessor(new ArrayList<PessoaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarProfessor() {
		getSolicitacaoAberturaTurmaDisciplinaVO().setProfessor((PessoaVO) getRequestMap().get("professorItens"));
		limparMensagem();
	}

	public void consultarFuncionario() {
		try {
			super.consultar();
			List<PessoaVO> objs = new ArrayList<PessoaVO>(0);
			if (getCampoConsultaFuncionario().equals("nome")) {
				if (getValorConsultaFuncionario().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaFuncionario(), TipoPessoa.FUNCIONARIO.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cpf")) {
				if (getValorConsultaFuncionario().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaFuncionario(), TipoPessoa.FUNCIONARIO.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<PessoaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFuncionario() {
		getNotificacao().setPessoa(new PessoaVO());
		getNotificacao().setPessoa((PessoaVO) getRequestMap().get("funcionarioItens"));
		limparMensagem();
	}

	public SolicitacaoAberturaTurmaVO getSolicitacaoAberturaTurmaVO() {
		if (solicitacaoAberturaTurmaVO == null) {
			solicitacaoAberturaTurmaVO = new SolicitacaoAberturaTurmaVO();
		}
		return solicitacaoAberturaTurmaVO;
	}

	public void setSolicitacaoAberturaTurmaVO(SolicitacaoAberturaTurmaVO solicitacaoAberturaTurmaVO) {
		this.solicitacaoAberturaTurmaVO = solicitacaoAberturaTurmaVO;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
			listaSelectItemUnidadeEnsino.add(new SelectItem(0, ""));
			try {
				List<UnidadeEnsinoVO> unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarUnidadeEnsinoComboBox(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				for (UnidadeEnsinoVO obj : unidadeEnsinoVOs) {
					listaSelectItemUnidadeEnsino.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
			} catch (Exception e) {

			}
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<UnidadeEnsinoCursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<UnidadeEnsinoCursoVO> listaConsultaCurso) {
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

	public String getCampoConsultaCurso() {
		if (campoConsultaCurso == null) {
			campoConsultaCurso = "";
		}
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public SituacaoSolicitacaoAberturaTurmaEnum getSituacaoSolicitacaoAberturaTurma() {
		if (situacaoSolicitacaoAberturaTurma == null) {
			situacaoSolicitacaoAberturaTurma = SituacaoSolicitacaoAberturaTurmaEnum.AGUARDANDO_AUTORIZACAO;
		}
		return situacaoSolicitacaoAberturaTurma;
	}

	public void setSituacaoSolicitacaoAberturaTurma(SituacaoSolicitacaoAberturaTurmaEnum situacaoSolicitacaoAberturaTurma) {
		this.situacaoSolicitacaoAberturaTurma = situacaoSolicitacaoAberturaTurma;
	}

	public Boolean getIsPermiteAlterar() {
		return getSolicitacaoAberturaTurmaVO().isNovoObj() || (getSolicitacaoAberturaTurmaVO().getSituacaoSolicitacaoAberturaTurma().equals(SituacaoSolicitacaoAberturaTurmaEnum.AGUARDANDO_AUTORIZACAO) && !getIsRevisarSolicitacao()) || getIsPermiteRevisaoSolicitacao();
	}

	public List<SelectItem> getListaSelectItemSituacao() {
		if (listaSelectItemSituacao == null) {
			listaSelectItemSituacao = new ArrayList<SelectItem>(0);
			listaSelectItemSituacao.add(new SelectItem(SituacaoSolicitacaoAberturaTurmaEnum.TODAS, SituacaoSolicitacaoAberturaTurmaEnum.TODAS.getValorApresentar()));
			listaSelectItemSituacao.add(new SelectItem(SituacaoSolicitacaoAberturaTurmaEnum.AGUARDANDO_AUTORIZACAO, SituacaoSolicitacaoAberturaTurmaEnum.AGUARDANDO_AUTORIZACAO.getValorApresentar()));
			listaSelectItemSituacao.add(new SelectItem(SituacaoSolicitacaoAberturaTurmaEnum.EM_REVISAO, SituacaoSolicitacaoAberturaTurmaEnum.EM_REVISAO.getValorApresentar()));
			listaSelectItemSituacao.add(new SelectItem(SituacaoSolicitacaoAberturaTurmaEnum.AUTORIZADO, SituacaoSolicitacaoAberturaTurmaEnum.AUTORIZADO.getValorApresentar()));
			listaSelectItemSituacao.add(new SelectItem(SituacaoSolicitacaoAberturaTurmaEnum.FINALIZADO, SituacaoSolicitacaoAberturaTurmaEnum.FINALIZADO.getValorApresentar()));
			listaSelectItemSituacao.add(new SelectItem(SituacaoSolicitacaoAberturaTurmaEnum.NAO_AUTORIZADO, SituacaoSolicitacaoAberturaTurmaEnum.NAO_AUTORIZADO.getValorApresentar()));
		}
		return listaSelectItemSituacao;
	}

	public void setListaSelectItemSituacao(List<SelectItem> listaSelectItemSituacao) {
		this.listaSelectItemSituacao = listaSelectItemSituacao;
	}

	public boolean getIsRevisarSolicitacao() {
		return !getSolicitacaoAberturaTurmaVO().isNovoObj() 
				&& (getSolicitacaoAberturaTurmaVO().getSituacaoSolicitacaoAberturaTurma().equals(SituacaoSolicitacaoAberturaTurmaEnum.AGUARDANDO_AUTORIZACAO)
				|| getSolicitacaoAberturaTurmaVO().getSituacaoSolicitacaoAberturaTurma().equals(SituacaoSolicitacaoAberturaTurmaEnum.AUTORIZADO))
				&& getLoginControle().getPermissaoAcessoMenuVO().getRevisarSolicitacaoAberturaTurma();
	}

	public boolean getIsPermiteAutorizarSolicitacao() {
		return !getSolicitacaoAberturaTurmaVO().isNovoObj() && getSolicitacaoAberturaTurmaVO().getSituacaoSolicitacaoAberturaTurma().equals(SituacaoSolicitacaoAberturaTurmaEnum.AGUARDANDO_AUTORIZACAO) && getLoginControle().getPermissaoAcessoMenuVO().getAutorizarSolicitacaoAberturaTurma();
	}

	public boolean getIsPermiteNaoAutorizarSolicitacao() {
		return !getSolicitacaoAberturaTurmaVO().isNovoObj() && 
				(getSolicitacaoAberturaTurmaVO().getSituacaoSolicitacaoAberturaTurma().equals(SituacaoSolicitacaoAberturaTurmaEnum.AGUARDANDO_AUTORIZACAO) ||
						getSolicitacaoAberturaTurmaVO().getSituacaoSolicitacaoAberturaTurma().equals(SituacaoSolicitacaoAberturaTurmaEnum.AUTORIZADO))
				&& getLoginControle().getPermissaoAcessoMenuVO().getNaoAutorizarSolicitacaoAberturaTurma();
	}

	public boolean getIsPermiteRevisaoSolicitacao() {
		return !getSolicitacaoAberturaTurmaVO().isNovoObj() && getSolicitacaoAberturaTurmaVO().getSituacaoSolicitacaoAberturaTurma().equals(SituacaoSolicitacaoAberturaTurmaEnum.EM_REVISAO) && getLoginControle().getPermissaoAcessoMenuVO().getPermiteRevisarSolicitacaoAberturaTurma();
	}

	public boolean getIsPermiteFinalizarSolicitacao() {
		return !getSolicitacaoAberturaTurmaVO().isNovoObj() && getSolicitacaoAberturaTurmaVO().getSituacaoSolicitacaoAberturaTurma().equals(SituacaoSolicitacaoAberturaTurmaEnum.AUTORIZADO) && getLoginControle().getPermissaoAcessoMenuVO().getFinalizarSolicitacaoAberturaTurma();
	}

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>();
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
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

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCurso() {
		if (unidadeEnsinoCurso == null) {
			unidadeEnsinoCurso = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCurso;
	}

	public void setUnidadeEnsinoCurso(UnidadeEnsinoCursoVO unidadeEnsinoCurso) {
		this.unidadeEnsinoCurso = unidadeEnsinoCurso;
	}

	public List<PessoaVO> getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaProfessor;
	}

	public void setListaConsultaProfessor(List<PessoaVO> listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
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

	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = "";
		}
		return campoConsultaProfessor;
	}

	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}

	public SolicitacaoAberturaTurmaDisciplinaVO getSolicitacaoAberturaTurmaDisciplinaVO() {
		if (solicitacaoAberturaTurmaDisciplinaVO == null) {
			solicitacaoAberturaTurmaDisciplinaVO = new SolicitacaoAberturaTurmaDisciplinaVO();
		}
		return solicitacaoAberturaTurmaDisciplinaVO;
	}

	public void setSolicitacaoAberturaTurmaDisciplinaVO(SolicitacaoAberturaTurmaDisciplinaVO solicitacaoAberturaTurmaDisciplinaVO) {
		this.solicitacaoAberturaTurmaDisciplinaVO = solicitacaoAberturaTurmaDisciplinaVO;
	}

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public ComunicacaoInternaVO getNotificacao() {
		if (notificacao == null) {
			notificacao = new ComunicacaoInternaVO();
		}
		return notificacao;
	}

	public void setNotificacao(ComunicacaoInternaVO notificacao) {
		this.notificacao = notificacao;
	}

	public List<PessoaVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<PessoaVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public Integer getIndex() {
		if (index == null) {
			index = 0;
		}
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}
	
	public String getTituloScrollNext() {
		if (getIndex() == getSolicitacaoAberturaTurmaVO().getCalendarioHorarioAulaVOs().size()) {
			return "";
		}
		
		if (getSolicitacaoAberturaTurmaVO().getCalendarioHorarioAulaVOs().size() >= getIndex()) {
			return getSolicitacaoAberturaTurmaVO().getCalendarioHorarioAulaVOs().get(getIndex() - 1).getMesPosteriorAbreviado();
		}
		return "";
	}
	
	public String getTituloScrollFirst() {
		if (getIndex() - 1 < 1) {
			return "";
		}
		if (getSolicitacaoAberturaTurmaVO().getCalendarioHorarioAulaVOs().size() >= getIndex()) {
			return getSolicitacaoAberturaTurmaVO().getCalendarioHorarioAulaVOs().get(getIndex() - 1).getMesAnoAnteriorAbreviado();
		}
		return "";
	}

	public String getTituloScroll() {
		if (getIndex() - 1 < 0) {
			setIndex(1);
		}
		if (getSolicitacaoAberturaTurmaVO().getCalendarioHorarioAulaVOs().size() >= getIndex()) {
			return getSolicitacaoAberturaTurmaVO().getCalendarioHorarioAulaVOs().get(getIndex() - 1).getTituloCalendarioAbreviado();
		}
		return "";
	}

}
