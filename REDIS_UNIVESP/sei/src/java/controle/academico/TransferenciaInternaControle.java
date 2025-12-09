package controle.academico;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.HistoricoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.TransferenciaEntradaDisciplinasAproveitadasVO;
import negocio.comuns.academico.TransferenciaEntradaRegistroAulaFrequenciaVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoHistorico;
import negocio.comuns.utilitarias.dominios.SituacaoTransferenciaEntrada;
import negocio.comuns.utilitarias.dominios.TipoTransferenciaEntrada;
import negocio.facade.jdbc.academico.Matricula;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.academico.DeclaracaoTransferenciaInternaRelControle;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("TransferenciaInternaControle")
@Scope("viewScope")
@Lazy
public class TransferenciaInternaControle extends SuperControleRelatorio implements Serializable {

	private TransferenciaEntradaVO transferenciaEntradaVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private Integer disciplina;
	private Boolean matriculado;
	private List listaSelectItemPeriodoLetivo;
	private List listaSelectItemUnidadeEnsino;
	private List listaSelectItemTurma;
	private List listaSelectItemDisciplina;
	private List listaConsultaCurso;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List listaConsultaAluno;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List listaSelectItemGradeCurricular;
	private List<ContaReceberVO> listaContaReceber;
	private String mensagemTransferencia;
	private String apresentarRichMensagem;
	private String ano;
	private String semestre;
	private Boolean realizandoTranferenciaEntradaMatriculaExistente;
	private MatriculaVO matriculaSelecionada;
	private List<SelectItem> listaSelectItemMatriculado;
	private String situacaoMatriculado;
	private Boolean possuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral;

	@SuppressWarnings("OverridableMethodCallInConstructor")
	public TransferenciaInternaControle() throws Exception {
		// obterUsuarioLogado();
		setTipoRequerimento("TI");
		montarListaSelectItemUnidadeEnsino();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsï¿½vel por disponibilizar um novo objeto da classe
	 * <code>TransferenciaEntrada</code> para ediï¿½ï¿½o pelo usuï¿½rio da
	 * aplicaï¿½ï¿½o.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setTransferenciaEntradaVO(new TransferenciaEntradaVO());
		inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado();
		setListaSelectItemDisciplina(null);
		setListaSelectItemPeriodoLetivo(null);
		setListaSelectItemTurma(null);
		setListaSelectItemGradeCurricular(null);
		getListaContaReceber().clear();
		montarListaSelectItemUnidadeEnsino();
		setTipoRequerimento("TI");
		setOncompleteModal("");
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaInternaForm.xhtml");
	}

	public void inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado() {
		try {
			getTransferenciaEntradaVO().getResponsavelAutorizacao().setCodigo(getUsuarioLogado().getCodigo());
			getTransferenciaEntradaVO().getResponsavelAutorizacao().setNome(getUsuarioLogado().getNome());
		} catch (Exception e) {
			// System.out.println("Erro TransferenciaInternaControle.inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado: "
			// + e.getMessage());
		}
	}

	/**
	 * Rotina responsï¿½vel por disponibilizar os dados de um objeto da classe
	 * <code>TransferenciaEntrada</code> para alteraï¿½ï¿½o. O objeto desta
	 * classe ï¿½ disponibilizado na session da pï¿½gina (request) para que o
	 * JSP correspondente possa disponibilizï¿½-lo para ediï¿½ï¿½o.
	 */
	public String editar() {
		try {
			TransferenciaEntradaVO obj = new TransferenciaEntradaVO();
			if(!getTransferenciaEntradaVO().getCodigo().equals(0)){
				 obj = getTransferenciaEntradaVO();
			} else {
			obj = (TransferenciaEntradaVO) context().getExternalContext().getRequestMap().get("transferenciaInternaItens");
			}
			obj = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			getListaSelectItemGradeCurricular().clear();
			getListaSelectItemGradeCurricular().add(new SelectItem(obj.getGradeCurricular().getCodigo(), obj.getGradeCurricular().getNome()));
			getListaSelectItemPeriodoLetivo().clear();
			getListaSelectItemPeriodoLetivo().add(new SelectItem(obj.getPeridoLetivo().getCodigo(), obj.getPeridoLetivo().getDescricao()));
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(obj.getTurma().getCodigo(), obj.getTurma().getIdentificadorTurma()));
			setTransferenciaEntradaVO(obj);
			getListaContaReceber().clear();
			if (getIsPossuiRequerimento()) {
				setListaContaReceber(getFacadeFactory().getContaReceberFacade().consultaRapidaPorAlunoTipoPessoaContasReceberVencidas(getTransferenciaEntradaVO().getCodigoRequerimento().getMatricula().getAluno().getCodigo(), "AL", getUsuarioLogado()));
			} else {
				setListaContaReceber(getFacadeFactory().getContaReceberFacade().consultaRapidaPorAlunoTipoPessoaContasReceberVencidas(getTransferenciaEntradaVO().getMatricula().getAluno().getCodigo(), "AL", getUsuarioLogado()));
			}
			if(obj.getSituacaoEfetivado() && !getFacadeFactory().getTransferenciaEntradaFacade().executarVerificarTransferenciaEntradaVinculadaMatricula(obj.getCodigo(), getUsuarioLogado())){
				getFacadeFactory().getTransferenciaEntradaFacade().alterarSituacao(obj.getCodigo(), SituacaoTransferenciaEntrada.EM_AVALIACAO.getValor());
				obj.setSituacao(SituacaoTransferenciaEntrada.EM_AVALIACAO.getValor());
			}
			montarListaSelectItemUnidadeEnsino("");
			obj.setNovoObj(Boolean.FALSE);
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaInternaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaInternaCons.xhtml");
		}
	}

	/**
	 * Rotina responsï¿½vel por gravar no BD os dados editados de um novo objeto
	 * da classe <code>TransferenciaEntrada</code>. Caso o objeto seja novo
	 * (ainda nï¿½o gravado no BD) ï¿½ acionado a operaï¿½ï¿½o
	 * <code>incluir()</code>. Caso contrï¿½rio ï¿½ acionado o
	 * <code>alterar()</code>. Se houver alguma inconsistï¿½ncia o objeto nï¿½o
	 * ï¿½ gravado, sendo re-apresentado para o usuï¿½rio juntamente com uma
	 * mensagem de erro.
	 */
	public String gravar() {
		try {
			if (getTransferenciaEntradaVO().getSituacao().equals(SituacaoTransferenciaEntrada.EM_AVALIACAO.getValor())) {
				inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado();
				if (getTransferenciaEntradaVO().isNovoObj().booleanValue()) {
					getFacadeFactory().getTransferenciaEntradaFacade().validarDadosEnturmacaoAlunoTransferencia(getTransferenciaEntradaVO(), getUsuarioLogado());
					getTransferenciaEntradaVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTransferenciaEntradaVO().getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
					getFacadeFactory().getTransferenciaEntradaFacade().incluir(getTransferenciaEntradaVO(), getConfiguracaoGeralPadraoSistema(), getConfiguracaoFinanceiroPadraoSistema(), true , true ,getUsuarioLogado());
				} else {
					getFacadeFactory().getTransferenciaEntradaFacade().alterar(getTransferenciaEntradaVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				}
			} else {
				throw new Exception(UteisJSF.internacionalizar("msg_TransferenciaInterna_ErroAlterarSomenteQuandoEmAvaliacao"));
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaInternaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaInternaForm.xhtml");
		}
	}

	public String matricular() {
		try {
			gravar();
			getFacadeFactory().getMatriculaPeriodoFacade().validarExistenciaMatriculaPeriodoAptaRealizarTransferenciaPorMatriculaSituacoesAtivaPreMatriculaAbandonoCursoTrancado(getTransferenciaEntradaVO().getMatricula().getMatricula());
			removerControleMemoriaFlashTela("RenovarMatriculaControle");
			context().getExternalContext().getSessionMap().put("TransferenciaInternaVO", getTransferenciaEntradaVO());
			return Uteis.getCaminhoRedirecionamentoNavegacao("renovarMatriculaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaInternaForm.xhtml");
		}
	}

	public void indeferir() {
		try {
			getTransferenciaEntradaVO().setSituacao("IN");
			if (!transferenciaEntradaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getTransferenciaEntradaFacade().verificarTransferenciaEntrada(getTransferenciaEntradaVO(), getTransferenciaEntradaVO().getSituacao(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			getTransferenciaEntradaVO().setSituacao("AV");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * TransferenciaEntradaCons.jsp. Define o tipo de consulta a ser executada,
	 * por meio de ComboBox denominado campoConsulta, disponivel neste mesmo
	 * JSP. Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	@Override
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorCodigo(new Integer(valorInt), getSituacaoMatriculado(), TipoTransferenciaEntrada.INTERNA, true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getSituacaoMatriculado(), TipoTransferenciaEntrada.INTERNA, true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("situacao")) {
				objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), getSituacaoMatriculado(), TipoTransferenciaEntrada.INTERNA, true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorMatriculaETipo(getControleConsulta().getValorConsulta(), getSituacaoMatriculado(), TipoTransferenciaEntrada.INTERNA, true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("curso")) {
				objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorCurso(getControleConsulta().getValorConsulta(), getSituacaoMatriculado(), TipoTransferenciaEntrada.INTERNA, true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("pessoa")) {
				objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorAluno(getControleConsulta().getValorConsulta(), getSituacaoMatriculado(), TipoTransferenciaEntrada.INTERNA, true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("registroAcademico")) {
				objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorRegistroAcademicoAluno(getControleConsulta().getValorConsulta(), getSituacaoMatriculado(), TipoTransferenciaEntrada.INTERNA, true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("requerimento")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					throw new Exception(UteisJSF.internacionalizar("msg_TransferenciaInterna_ErroInformeCodigoRequerimento"));
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorCodigoRequerimento(valorInt, getSituacaoMatriculado(), TipoTransferenciaEntrada.INTERNA, true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaInternaCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaInternaCons.xhtml");
		}
	}

	/**
	 * Operaï¿½ï¿½o responsï¿½vel por processar a exclusï¿½o um objeto da classe
	 * <code>TransferenciaEntradaVO</code> Apï¿½s a exclusï¿½o ela
	 * automaticamente aciona a rotina para uma nova inclusï¿½o.
	 */
	public String excluir() {
		try {
			if (!getTransferenciaEntradaVO().getSituacao().equals(SituacaoTransferenciaEntrada.EFETIVADO.getValor())) {
				getFacadeFactory().getTransferenciaEntradaFacade().excluir(getTransferenciaEntradaVO(), true, getUsuarioLogado());
				setTransferenciaEntradaVO(new TransferenciaEntradaVO());
			} else {
				throw new Exception(UteisJSF.internacionalizar("msg_TransferenciaInterna_ErroExcluirTransferenciaEfetivada"));
			}
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaInternaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaInternaForm.xhtml");
		}
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
			if (getCampoConsultaAluno().equals("nomeAluno")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("registroAcademico")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademico(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaAluno().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getTransferenciaEntradaVO().getMatricula().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getTransferenciaEntradaVO().getMatricula().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			} else {
			setMatriculaSelecionada(objAluno);
			}
			boolean existePendenciaFinanceira = getFacadeFactory().getContaReceberFacade().consultarExistenciaPendenciaFinanceiraMatricula(objAluno.getMatricula(), getUsuarioLogado());
			if (existePendenciaFinanceira && !objAluno.getCanceladoFinanceiro()) {
				setMensagemTransferencia(UteisJSF.internacionalizar("msg_LiberacaoFinanceira_transferenciaInterna"));
				setApresentarRichMensagem("RichFaces.$('panelMensagem').show()");
				getTransferenciaEntradaVO().setMatricula(new MatriculaVO());
				return;
			}
			setApresentarRichMensagem("");
			getTransferenciaEntradaVO().setMatricula(objAluno);
			getTransferenciaEntradaVO().getCodigoRequerimento().setMatricula(objAluno);
			getTransferenciaEntradaVO().setPessoa(objAluno.getAluno());
			if(!objAluno.getUnidadeEnsino().getNomeExpedicaoDiploma().trim().isEmpty()) {
				getTransferenciaEntradaVO().setInstituicaoOrigem(objAluno.getUnidadeEnsino().getNomeExpedicaoDiploma());
			}else if(!objAluno.getUnidadeEnsino().getRazaoSocial().trim().isEmpty()) {
				getTransferenciaEntradaVO().setInstituicaoOrigem(objAluno.getUnidadeEnsino().getRazaoSocial());
			}else {
				getTransferenciaEntradaVO().setInstituicaoOrigem(objAluno.getUnidadeEnsino().getNome());
			}
			getTransferenciaEntradaVO().setTipoTransferenciaEntrada(TipoTransferenciaEntrada.INTERNA.getValor());
			getTransferenciaEntradaVO().setCursoOrigem(objAluno.getCurso().getNome());
			verificarSeExisteTransferenciaInternaParaMatricula();
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			getTransferenciaEntradaVO().getCodigoRequerimento().setMatricula(new MatriculaVO());
			getTransferenciaEntradaVO().setPessoa(new PessoaVO());
			getTransferenciaEntradaVO().setMatricula(new MatriculaVO());
			getTransferenciaEntradaVO().setCursoOrigem("");
			getTransferenciaEntradaVO().setInstituicaoOrigem("");
			getTransferenciaEntradaVO().setUnidadeEnsino(new UnidadeEnsinoVO());
			getTransferenciaEntradaVO().setCurso(new CursoVO());
			getTransferenciaEntradaVO().setTurno(new TurnoVO());
			getListaSelectItemGradeCurricular().clear();
			getListaSelectItemPeriodoLetivo().clear();
			getListaSelectItemTurma().clear();
			getTransferenciaEntradaVO().getTransferenciaEntradaDisciplinasAproveitadasVOs().clear();
			setOncompleteModal("");
		}
	}

	public void limparDadosAluno() throws Exception {
		getTransferenciaEntradaVO().getCodigoRequerimento().setMatricula(new MatriculaVO());
		getTransferenciaEntradaVO().setPessoa(new PessoaVO());
		getTransferenciaEntradaVO().setMatricula(new MatriculaVO());
		getTransferenciaEntradaVO().setCursoOrigem("");
		getTransferenciaEntradaVO().setInstituicaoOrigem("");
		getTransferenciaEntradaVO().setUnidadeEnsino(new UnidadeEnsinoVO());
		getTransferenciaEntradaVO().setCurso(new CursoVO());
		getTransferenciaEntradaVO().setTurno(new TurnoVO());
		getListaSelectItemGradeCurricular().clear();
		getListaSelectItemPeriodoLetivo().clear();
		getListaSelectItemTurma().clear();
		getTransferenciaEntradaVO().getTransferenciaEntradaDisciplinasAproveitadasVOs().clear();
		setMensagemID("msg_entre_prmconsulta");
	}

	public void selecionarAluno() {
		getListaContaReceber().clear();
		try {

			if(getRealizandoTranferenciaEntradaMatriculaExistente()){
					getFacadeFactory().getTransferenciaEntradaFacade().realizarVinculoTransferenciaInternaMatriculaExistente(getTransferenciaEntradaVO(), getMatriculaSelecionada(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaSelecionada().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
					setMensagemID("msg_dados_gravados");
				}else{
				getFacadeFactory().getMatriculaPeriodoFacade().validarExistenciaMatriculaPeriodoAptaRealizarTransferenciaPorMatriculaSituacoesAtivaPreMatriculaAbandonoCursoTrancado(getMatriculaSelecionada().getMatricula());
				MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaSelecionada().getMatricula(), getMatriculaSelecionada().getUnidadeEnsino().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
				// boolean existePreMatriculaAtiva =
				// getFacadeFactory().getMatriculaPeriodoFacade().consultarExistenciaPreMatricula(objCompleto.getMatricula());
				// if (existePreMatriculaAtiva) {
				// setMensagemTransferencia(UteisJSF.internacionalizar("msg_PreMatriculaAtiva_transferenciaInterna"));
				// setApresentarRichMensagem("Richfaces.showModalPanel('panelMensagem')");
				// return;
				// }
				boolean existePendenciaFinanceira = getFacadeFactory().getContaReceberFacade().consultarExistenciaPendenciaFinanceiraMatricula(objCompleto.getMatricula(), getUsuarioLogado());
				if (existePendenciaFinanceira && !objCompleto.getCanceladoFinanceiro()) {
					setMensagemTransferencia(UteisJSF.internacionalizar("msg_LiberacaoFinanceira_transferenciaInterna"));
					setApresentarRichMensagem("RichFaces.$('panelMensagem').show()");
					return;
				}
				setApresentarRichMensagem("");
				getTransferenciaEntradaVO().setMatricula(objCompleto);
				getTransferenciaEntradaVO().getCodigoRequerimento().setMatricula(objCompleto);
				getTransferenciaEntradaVO().setPessoa(objCompleto.getAluno());
				if(!objCompleto.getUnidadeEnsino().getNomeExpedicaoDiploma().trim().isEmpty()) {
					getTransferenciaEntradaVO().setInstituicaoOrigem(objCompleto.getUnidadeEnsino().getNomeExpedicaoDiploma());
				}else if(!objCompleto.getUnidadeEnsino().getRazaoSocial().trim().isEmpty()) {
					getTransferenciaEntradaVO().setInstituicaoOrigem(objCompleto.getUnidadeEnsino().getRazaoSocial());
				}else {
					getTransferenciaEntradaVO().setInstituicaoOrigem(objCompleto.getUnidadeEnsino().getNome());
				}
				getTransferenciaEntradaVO().setTipoTransferenciaEntrada(TipoTransferenciaEntrada.INTERNA.getValor());
				getTransferenciaEntradaVO().setCursoOrigem(objCompleto.getCurso().getNome());
				if(Uteis.isAtributoPreenchido(objCompleto.getUltimoMatriculaPeriodoVO())) {
					getTransferenciaEntradaVO().setMatriculaPeriodo(objCompleto.getUltimoMatriculaPeriodoVO());
				}else {
					getTransferenciaEntradaVO().setMatriculaPeriodo(getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimaMatriculaPeriodoAtivaOuTrancadaPorMatriculaSemExcecao(objCompleto.getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
				}
//				setListaContaReceber(getFacadeFactory().getContaReceberFacade().consultaRapidaPorAlunoTipoPessoaContasReceberVencidas(objCompleto.getAluno().getCodigo(), "AL", getUsuarioLogado()));
				
				objCompleto = null;
				setValorConsultaAluno("");
				setCampoConsultaAluno("");
				getListaConsultaAluno().clear();
				if (getTransferenciaEntradaVO().getGradeCurricular().getCodigo() != null && getTransferenciaEntradaVO().getGradeCurricular().getCodigo() != 0) {
					montarListaDisciplinaAproveitamento();
				}
				}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}finally{
			setMatriculaSelecionada(null);
		}
	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomeAluno", "Nome Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		 itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	/*
	 * Mï¿½todo responsï¿½vel por adicionar um novo objeto da classe
	 * <code>TransferenciaEntradaDisciplinasAproveitadas</code> para o objeto
	 * <code>transferenciaEntradaVO</code> da classe
	 * <code>TransferenciaEntrada</code>
	 */
	public void adicionarTransferenciaEntradaDisciplinasAproveitadas() {
		try {
			List<HistoricoVO> historicoVOs = getFacadeFactory().getHistoricoFacade().consultarPorMatriculaDisciplinaAproveitamento(getTransferenciaEntradaVO().getCodigoRequerimento().getMatricula().getMatricula(), getDisciplina(), SituacaoHistorico.APROVADO.getValor(), false, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			getFacadeFactory().getTransferenciaEntradaFacade().adicionarDisciplinaSeremAproveitadaTransferenciaInterna(historicoVOs, getTransferenciaEntradaVO().getTransferenciaEntradaDisciplinasAproveitadasVOs(), getTransferenciaEntradaVO().getCurso().getCodigo(), getTransferenciaEntradaVO().getGradeCurricular().getCodigo(), getUsuarioLogado());
			Ordenacao.ordenarLista(getTransferenciaEntradaVO().getTransferenciaEntradaDisciplinasAproveitadasVOs(), "campoOrdenacao");
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Mï¿½todo responsï¿½vel por remover um novo objeto da classe
	 * <code>TransferenciaEntradaDisciplinasAproveitadas</code> do objeto
	 * <code>transferenciaEntradaVO</code> da classe
	 * <code>TransferenciaEntrada</code>
	 */
	public void removerTransferenciaEntradaDisciplinasAproveitadas() throws Exception {
		TransferenciaEntradaDisciplinasAproveitadasVO obj = (TransferenciaEntradaDisciplinasAproveitadasVO) context().getExternalContext().getRequestMap().get("transferenciaEntradaDisciplinasAproveitadasItens");
		getTransferenciaEntradaVO().excluirObjTransferenciaEntradaDisciplinasAproveitadasVOs(obj.getDisciplina().getCodigo());
		setMensagemID("msg_dados_excluidos");
		return;
	}

	public void irPaginaInicial() throws Exception {

		this.consultar();
	}

	/**
	 * Mï¿½todo responsï¿½vel por processar a consulta na entidade
	 * <code>Requerimento</code> por meio de sua respectiva chave primï¿½ria.
	 * Esta rotina ï¿½ utilizada fundamentalmente por requisiï¿½ï¿½es Ajax, que
	 * realizam busca pela chave primï¿½ria da entidade montando automaticamente
	 * o resultado da consulta para apresentaï¿½ï¿½o.
	 */
	public void consultarRequerimentoPorChavePrimaria() {
		try {
			limparMensagem();
			getListaContaReceber().clear();
			if (Uteis.isAtributoPreenchido(getTransferenciaEntradaVO().getCodigoRequerimento().getCodigo())) {
				verificarPermissaoRealizarTransferenciaInternaMesmoCursoMatriculaIntegral();
				setTransferenciaEntradaVO(getFacadeFactory().getTransferenciaEntradaFacade().montarDadosTransferenciaInternaPeloCodigoRequerimento(getTransferenciaEntradaVO().getCodigoRequerimento().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getConfiguracaoFinanceiroPadraoSistema(), getPossuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral(), getUsuarioLogado()));
				if(Uteis.isAtributoPreenchido(getTransferenciaEntradaVO().getCurso().getCodigo())){
					montarListaSelectItemGradeCurricular();
				}
				inicializarUsuarioResponsavelTransferenciaEntradaUsuarioLogado();
				boolean existePendenciaFinanceira = getFacadeFactory().getContaReceberFacade().consultarExistenciaPendenciaFinanceiraMatricula(getTransferenciaEntradaVO().getCodigoRequerimento().getMatricula().getMatricula(), getUsuarioLogado());
				String mensagem = "";
				if (existePendenciaFinanceira && !getTransferenciaEntradaVO().getCodigoRequerimento().getMatricula().getCanceladoFinanceiro()) {
					mensagem = getTransferenciaEntradaVO().getCodigoRequerimento().getMatricula().validaMatriculaLiberadaFinanceiroCancelamentoTrancamento(true);
				}
				if (!mensagem.equals("")) {
					setMensagemTransferencia(mensagem);
					setApresentarRichMensagem("RichFaces.$('panelMensagem').show()");
					return;
				}	
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			transferenciaEntradaVO.getCodigoRequerimento().setCodigo(0);
		}
	}

	public void selecionarRequerimento() {
		try {
			getTransferenciaEntradaVO().setCodigoRequerimento((RequerimentoVO) context().getExternalContext().getRequestMap().get("requerimentoItens"));
			consultarRequerimentoPorChavePrimaria();
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaDisciplinaAproveitamento() throws Exception {
		try {
			getTransferenciaEntradaVO().getTransferenciaEntradaDisciplinasAproveitadasVOs().clear();
			getTransferenciaEntradaVO().setTransferenciaEntradaDisciplinasAproveitadasVOs(getFacadeFactory().getTransferenciaEntradaFacade().buscarDisciplinaSeremAproveitadaTransferenciaInterna(getTransferenciaEntradaVO().getCodigoRequerimento().getMatricula().getAluno().getCodigo(), getTransferenciaEntradaVO().getCurso().getCodigo(), getTransferenciaEntradaVO().getGradeCurricular().getCodigo(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
		} catch (Exception e) {
			// System.out.println("Erro TransferenciaInternaControle.montarListaDisciplinaAproveitamento: "
			// + e.getMessage());
			throw e;
		}
	}

	/**
	 * Rotina responsï¿½vel por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("matricula", "Matricula"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		itens.add(new SelectItem("pessoa", "Aluno"));
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("requerimento", "Requerimento"));
		// itens.add(new SelectItem("situacao", "Situaï¿½ï¿½o"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Rotina responsï¿½vel por organizar a paginaï¿½ï¿½o entre as pï¿½ginas
	 * resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("transferenciaInternaCons.xhtml");
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(valorInt, transferenciaEntradaVO.getUnidadeEnsino().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), transferenciaEntradaVO.getUnidadeEnsino().getCodigo(), false, "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocursoItens");
			verificarPermissaoRealizarTransferenciaInternaMesmoCursoMatriculaIntegral();
			getFacadeFactory().getTransferenciaEntradaFacade().adicionarCursoTransferenciaInterna(getTransferenciaEntradaVO(), unidadeEnsinoCurso, getPossuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral());
			montarListaSelectItemGradeCurricular();
			if (!getTransferenciaEntradaVO().getUnidadeEnsino().getCodigo().equals(getTransferenciaEntradaVO().getMatricula().getUnidadeEnsino().getCodigo())
            		&& !getTransferenciaEntradaVO().getCurso().getCodigo().equals(getTransferenciaEntradaVO().getMatricula().getCurso().getCodigo())) {
				getTransferenciaEntradaVO().getGradeCurricular().setCodigo(0);
				getListaSelectItemPeriodoLetivo().clear();
				getListaSelectItemTurma().clear();
			}
			// montarListaSelectItemPeriodoLetivo();
			// montarListaDisciplinaAproveitamento();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDadosCurso() throws Exception {
		getTransferenciaEntradaVO().setCurso(new CursoVO());
		getTransferenciaEntradaVO().setTurno(new TurnoVO());
		getTransferenciaEntradaVO().setGradeCurricular(new GradeCurricularVO());
		getTransferenciaEntradaVO().setTurma(new TurmaVO());
		getListaSelectItemGradeCurricular().clear();
		getListaSelectItemPeriodoLetivo().clear();
		getListaSelectItemTurma().clear();
		getTransferenciaEntradaVO().getTransferenciaEntradaDisciplinasAproveitadasVOs().clear();
		setMensagemID("msg_entre_prmconsulta");
	}

	public void montarListaPeriodoLetivoDisciplinasAproveitadas() {
		try {
			getTransferenciaEntradaVO().getPeridoLetivo().setCodigo(0);
			getListaSelectItemPeriodoLetivo().clear();
			getListaSelectItemTurma().clear();
			montarListaSelectItemPeriodoLetivo();
			montarListaDisciplinaAproveitamento();
		} catch (Exception e) {
			// System.out.println("Erro TransferenciaInternaControle.montarListaPeriodoLetivoDisciplinasAproveitadas: "
			// + e.getMessage());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo
	 * <code>PlanoDesconto</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>PlanoDesconto</code>. Esta rotina nï¿½o recebe
	 * parï¿½metros para filtragem de dados, isto ï¿½ importante para a
	 * inicializaï¿½ï¿½o dos dados da tela para o acionamento por meio
	 * requisiï¿½ï¿½es Ajax.
	 */
	public void montarListaSelectItemGradeCurricular() {
		try {
			if(!getTransferenciaEntradaVO().getUnidadeEnsino().getCodigo().equals(getTransferenciaEntradaVO().getMatricula().getUnidadeEnsino().getCodigo())
            		&& getTransferenciaEntradaVO().getCurso().getCodigo().equals(getTransferenciaEntradaVO().getMatricula().getCurso().getCodigo())) {
				getListaSelectItemGradeCurricular().clear();
            	if(getTransferenciaEntradaVO().getMatricula().getGradeCurricularAtual().getNome().trim().isEmpty()) {
            		getTransferenciaEntradaVO().getMatricula().setGradeCurricularAtual(getFacadeFactory().getGradeCurricularFacade().consultarGradeCurricularAtualMatricula(getTransferenciaEntradaVO().getMatricula().getMatricula(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            	}
            	getListaSelectItemGradeCurricular().add(new SelectItem(getTransferenciaEntradaVO().getMatricula().getGradeCurricularAtual().getCodigo(), getTransferenciaEntradaVO().getMatricula().getGradeCurricularAtual().getNome()));
            	getTransferenciaEntradaVO().getGradeCurricular().setCodigo(getTransferenciaEntradaVO().getMatricula().getGradeCurricularAtual().getCodigo());
            	getTransferenciaEntradaVO().getPeridoLetivo().setCodigo(getTransferenciaEntradaVO().getMatriculaPeriodo().getPeriodoLetivo().getCodigo());
            	montarListaPeriodoLetivoDisciplinasAproveitadas();            	
            }else {
            	List<GradeCurricularVO> resultadoConsulta = consultarGradeCurricularPorCurso(getTransferenciaEntradaVO().getCurso().getCodigo());
            	getTransferenciaEntradaVO().getGradeCurricular().setCodigo(0);
            	getTransferenciaEntradaVO().getPeridoLetivo().setCodigo(0);
            	getTransferenciaEntradaVO().getTurma().setCodigo(0);
            	getListaSelectItemPeriodoLetivo().clear();
            	getListaSelectItemTurma().clear();
            	getListaSelectItemGradeCurricular().clear();
            	getListaSelectItemGradeCurricular().add(new SelectItem(0, ""));
            	for(GradeCurricularVO grade: resultadoConsulta){
            		getListaSelectItemGradeCurricular().add(new SelectItem(grade.getCodigo(), grade.getNome()+" ("+grade.getSituacao_Apresentar()+")"));
            	}			
            }
		} catch (Exception e) {
			// System.out.println("Erro TransferenciaInternaControle.montarListaSelectItemGradeCurricular: "
			// + e.getMessage());
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo ï¿½ uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarGradeCurricularPorCurso(Integer codigoCurso) throws Exception {
		List lista = getFacadeFactory().getGradeCurricularFacade().consultarPorSituacaoGradeCursoLista(codigoCurso, "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Mï¿½todo responsï¿½vel por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>.
	 */
	public void montarListaSelectItemDisciplina(Integer prm) throws Exception {
		Iterator i = getTransferenciaEntradaVO().getTransferenciaEntradaDisciplinasAproveitadasVOs().iterator();

		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			TransferenciaEntradaDisciplinasAproveitadasVO obj = (TransferenciaEntradaDisciplinasAproveitadasVO) i.next();
			objs.add(new SelectItem(obj.getDisciplina().getCodigo(), obj.getDisciplina().getNome()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaSelectItemDisciplina(objs);
	}

	/**
	 * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>UnidadeEnsino</code>. Esta rotina nï¿½o recebe
	 * parï¿½metros para filtragem de dados, isto ï¿½ importante para a
	 * inicializaï¿½ï¿½o dos dados da tela para o acionamento por meio
	 * requisiï¿½ï¿½es Ajax.
	 */
	public void montarListaSelectItemDisciplina() {
		try {
			montarListaSelectItemDisciplina(getTransferenciaEntradaVO().getTurma().getCodigo());
		} catch (Exception e) {
			// System.out.println("Erro TransferenciaInternaControle.montarListaSelectItemDisciplina: "
			// + e.getMessage());
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>PeriodoLetivoMatricula</code>.
	 */
	public void montarListaSelectItemPeriodoLetivo(Integer prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			getListaSelectItemPeriodoLetivo().clear();
			if (getTransferenciaEntradaVO().getGradeCurricular().getCodigo().equals(0)) {
				getTransferenciaEntradaVO().getPeridoLetivo().setCodigo(0);
				getTransferenciaEntradaVO().getTurma().setCodigo(0);
				getListaSelectItemPeriodoLetivo().clear();
				getListaSelectItemTurma().clear();
				return;
			}
			resultadoConsulta = consultarPeriodoLetivoPorSigla(prm);
			
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
				if (obj.getPeriodoLetivo().intValue() == 1 && !getTransferenciaEntradaVO().getUnidadeEnsino().getCodigo().equals(getTransferenciaEntradaVO().getMatricula().getUnidadeEnsino().getCodigo())
	            		&& !getTransferenciaEntradaVO().getCurso().getCodigo().equals(getTransferenciaEntradaVO().getMatricula().getCurso().getCodigo())) {
					getTransferenciaEntradaVO().setPeridoLetivo(obj);
				}else if(!getTransferenciaEntradaVO().getUnidadeEnsino().getCodigo().equals(getTransferenciaEntradaVO().getMatricula().getUnidadeEnsino().getCodigo())
	            		&& getTransferenciaEntradaVO().getCurso().getCodigo().equals(getTransferenciaEntradaVO().getMatricula().getCurso().getCodigo())
	            		&& obj.getCodigo().equals(getTransferenciaEntradaVO().getMatriculaPeriodo().getPeriodoLetivo().getCodigo())) {
					getTransferenciaEntradaVO().setPeridoLetivo(obj);
				}
			}
			//ordenador = new SelectItemOrdemValor();
			//Collections.sort((List) objs, ordenador);
			setListaSelectItemPeriodoLetivo(objs);
			montarListaSelectItemTurma();
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo
	 * <code>PeriodoLetivoMatricula</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>PeriodoLetivo</code>. Esta rotina nï¿½o
	 * recebe parï¿½metros para filtragem de dados, isto ï¿½ importante para a
	 * inicializaï¿½ï¿½o dos dados da tela para o acionamento por meio
	 * requisiï¿½ï¿½es Ajax.
	 */
	public void montarListaSelectItemPeriodoLetivo() throws Exception {
		try {
			montarListaSelectItemPeriodoLetivo(getTransferenciaEntradaVO().getGradeCurricular().getCodigo());
		} catch (Exception e) {
			// System.out.println("Erro TransferenciaInternaControle.montarListaSelectItemPeriodoLetivo: "
			// + e.getMessage());
			throw e;
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por consultar dados da entidade
	 * <code><code> e montar o atributo <code>sigla</code> Este atributo ï¿½ uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarPeriodoLetivoPorSigla(Integer siglaPrm) throws Exception {
		List lista = getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(siglaPrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/*
	 * Mï¿½todo responsï¿½vel por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Turma</code>.
	 */
	public void montarListaSelectItemTurma(String prm) throws Exception {
		// List objs = new ArrayList(0);
		getListaSelectItemTurma().clear();		
		if ((getTransferenciaEntradaVO().getCurso() == null) || (getTransferenciaEntradaVO().getCurso().getCodigo().intValue() == 0)) {
			// setListaSelectItemTurma(objs);
			return;
		}
		if ((getTransferenciaEntradaVO().getTurno() == null) || (getTransferenciaEntradaVO().getTurno().getCodigo().intValue() == 0)) {
			// setListaSelectItemTurma(objs);
			return;
		}
		if (getTransferenciaEntradaVO().getPeridoLetivo() == null || getTransferenciaEntradaVO().getPeridoLetivo().getCodigo() == 0) {
			// setListaSelectItemTurma(objs);
			return;
		}
		List<TurmaVO> resultadoConsulta = consultarTurmaPorIdentificadorTurma();
		if (!resultadoConsulta.isEmpty()) {
			setListaSelectItemTurma(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "identificadorTurma"));
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo
	 * <code>Turma</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Turma</code>. Esta rotina nï¿½o recebe parï¿½metros para filtragem
	 * de dados, isto ï¿½ importante para a inicializaï¿½ï¿½o dos dados da tela
	 * para o acionamento por meio requisiï¿½ï¿½es Ajax.
	 */
	public void montarListaSelectItemTurma() {
		try {
			montarListaSelectItemTurma("");
		} catch (Exception e) {
			getListaSelectItemTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
			// System.out.println("Erro TransferenciaInternaControle.montarListaSelectItemTurma: "
			// + e.getMessage());
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por consultar dados da entidade
	 * <code><code> e montar o atributo <code>identificadorTurma</code> Este
	 * atributo ï¿½ uma lista (<code>List</code>) utilizada para definir os
	 * valores a serem apresentados no ComboBox correspondente
	 */
	public List<TurmaVO> consultarTurmaPorIdentificadorTurma() throws Exception {
		if(Uteis.isAtributoPreenchido(getTransferenciaEntradaVO().getPeridoLetivo().getCodigo())){
			getTransferenciaEntradaVO().setPeridoLetivo(getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(getTransferenciaEntradaVO().getPeridoLetivo().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX,  getUsuarioLogado()));
		}
		List<TurmaVO> listaResultado = getFacadeFactory().getTurmaFacade().consultaRapidaPorNrPeriodoLetivoUnidadeEnsinoCursoTurno(getTransferenciaEntradaVO().getPeridoLetivo().getPeriodoLetivo(), getTransferenciaEntradaVO().getUnidadeEnsino().getCodigo(), getTransferenciaEntradaVO().getCurso().getCodigo(), getTransferenciaEntradaVO().getTurno().getCodigo(), getTransferenciaEntradaVO().getGradeCurricular().getCodigo(), true, false,false, false, "",  "",   false, getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema(), false);
		return listaResultado;
	}

	/**
	 * Mï¿½todo responsï¿½vel por atualizar o ComboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>UnidadeEnsino</code>. Esta rotina nï¿½o recebe
	 * parï¿½metros para filtragem de dados, isto ï¿½ importante para a
	 * inicializaï¿½ï¿½o dos dados da tela para o acionamento por meio
	 * requisiï¿½ï¿½es Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println("Erro TransferenciaInternaControle.montarListaSelectItemUnidadeEnsino"
			// + e.getMessage());
		}
	}

	/*
	 * Mï¿½todo responsï¿½vel por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>UnidadeEnsino</code>.
	 */

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		try {
			getListaSelectItemUnidadeEnsino().clear();
			List resultadoConsulta = consultarUnidadeEnsino(prm);
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			// System.out.println("Erro TransferenciaInternaControle.montarListaSelectItemUnidadeEnsino(parametro)"
			// + e.getMessage());
			throw e;
		}
	}

	/**
	 * Mï¿½todo responsï¿½vel por consultar dados da entidade
	 * <code>UnidadeEnsino<code> e montar o atributo <code>nome</code> Este
	 * atributo ï¿½ uma lista (<code>List</code>) utilizada para definir os
	 * valores a serem apresentados no ComboBox correspondente
	 */
	public List consultarUnidadeEnsino(String prm) throws Exception {
		List listaResultado = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(prm, 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return listaResultado;
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "CÃ³digo"));
		return itens;
	}

	public Boolean getIsPermitidoTransfInternaSemRequerimento() {
		try {
			if (getConfiguracaoGeralPadraoSistema().getPermiteTransferenciaInternaSemRequerimento()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return null;
		}
	}

	public void inicializarAnoSemestrePorPeriodicidade(String periodicidade) {
		if (periodicidade.equals("SE")) {
			setAno(Uteis.getAnoDataAtual4Digitos());
			setSemestre(Uteis.getSemestreAtual());
		} else if (periodicidade.equals("AN")) {
			setAno(Uteis.getAnoDataAtual4Digitos());
			setSemestre("");
		} else {
			setAno("");
			setSemestre("");
		}
	}

	public void consultarRegistroAulaLancadoNovaTurma() {
		inicializarAnoSemestrePorPeriodicidade(getTransferenciaEntradaVO().getCurso().getPeriodicidade());
		getTransferenciaEntradaVO().setTransferenciaEntradaRegistroAulaFrequenciaVOs(getFacadeFactory().getTransferenciaEntradaRegistroAulaFrequenciaFacade().consultarRegistroAulaPorTurmaAnoSemestreTransferenciaInterna(getTransferenciaEntradaVO().getTurma().getCodigo(), getTransferenciaEntradaVO().getMatricula().getMatricula(), getAno(), getSemestre(), getUsuarioLogado()));
	}

	public void realizarDesabilitacaoPresenca() {
		TransferenciaEntradaRegistroAulaFrequenciaVO obj = (TransferenciaEntradaRegistroAulaFrequenciaVO) context().getExternalContext().getRequestMap().get("registroAulaItens");
		obj.setPresente(Boolean.FALSE);
	}

	public void realizarDesabilitacaoAbono() {
		TransferenciaEntradaRegistroAulaFrequenciaVO obj = (TransferenciaEntradaRegistroAulaFrequenciaVO) context().getExternalContext().getRequestMap().get("registroAulaItens");
		obj.setAbonado(Boolean.FALSE);
	}

	public TransferenciaEntradaVO getTransferenciaEntradaVO() {
		if (transferenciaEntradaVO == null) {
			transferenciaEntradaVO = new TransferenciaEntradaVO();
		}
		return transferenciaEntradaVO;
	}

	public void setTransferenciaEntradaVO(TransferenciaEntradaVO transferenciaEntradaVO) {
		this.transferenciaEntradaVO = transferenciaEntradaVO;
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

	public List getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public List getListaSelectItemPeriodoLetivo() {
		if (listaSelectItemPeriodoLetivo == null) {
			listaSelectItemPeriodoLetivo = new ArrayList(0);
		}
		return listaSelectItemPeriodoLetivo;
	}

	public void setListaSelectItemPeriodoLetivo(List listaSelectItemPeriodoLetivo) {
		this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
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

	public List getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public List getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List listaSelectItemDisciplina) {

		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}

	public Integer getDisciplina() {
		if (disciplina == null) {
			disciplina = 0;
		}
		return disciplina;
	}

	public void setDisciplina(Integer disciplina) {
		this.disciplina = disciplina;
	}

	public Boolean getMatriculado() {
		if (matriculado == null) {
			matriculado = Boolean.TRUE;
		}
		return matriculado;
	}

	public void setMatriculado(Boolean matriculado) {
		this.matriculado = matriculado;
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

	public List getListaSelectItemGradeCurricular() {
		if (listaSelectItemGradeCurricular == null) {
			listaSelectItemGradeCurricular = new ArrayList(0);
		}
		return listaSelectItemGradeCurricular;
	}

	public void setListaSelectItemGradeCurricular(List listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
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

	public List<ContaReceberVO> getListaContaReceber() {
		if (listaContaReceber == null) {
			listaContaReceber = new ArrayList<ContaReceberVO>(0);
		}
		return listaContaReceber;
	}

	public void setListaContaReceber(List<ContaReceberVO> listaContaReceber) {
		this.listaContaReceber = listaContaReceber;
	}

	public boolean getIsMatrizCurricularGravada() {
		if (getTransferenciaEntradaVO().getNovoObj() || getTransferenciaEntradaVO().getSituacaoEmAvaliacao()) {
			return false;
		}
		return true;
	}

	public boolean getIsEmAvaliacao() {
		if (!getTransferenciaEntradaVO().getNovoObj() && getTransferenciaEntradaVO().getSituacaoEmAvaliacao()) {
			return true;
		}
		return false;
	}

	public boolean getIsPossuiRequerimento() {
		if (getTransferenciaEntradaVO().getCodigoRequerimento().getCodigo() != null && getTransferenciaEntradaVO().getCodigoRequerimento().getCodigo() != 0) {
			return true;
		}
		return false;
	}

	public boolean getIsNaoPossuiRequerimento() {
		if (getTransferenciaEntradaVO().getCodigoRequerimento().getCodigo() != null && getTransferenciaEntradaVO().getCodigoRequerimento().getCodigo() != 0) {
			return false;
		}
		return true;
	}

	public String getMensagemTransferencia() {
		return mensagemTransferencia;
	}

	public void setMensagemTransferencia(String mensagemTransferencia) {
		this.mensagemTransferencia = mensagemTransferencia;
	}

	public String getApresentarRichMensagem() {
		return apresentarRichMensagem;
	}

	public void setApresentarRichMensagem(String apresentarRichMensagem) {
		this.apresentarRichMensagem = apresentarRichMensagem;
	}

	public void imprimirPDF() {
		List<TransferenciaEntradaVO> listaObjetos = new ArrayList<>();
		listaObjetos.add(transferenciaEntradaVO);
		try {
			getSuperParametroRelVO().limparParametros();
			getSuperParametroRelVO().setNomeDesignIreport("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator+"DeclaracaoTransferenciaInterna.jrxml");
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
			getSuperParametroRelVO().setSubReport_Dir("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
			getSuperParametroRelVO().setTituloRelatorio("DECLARAÇÃO DE TRANSFERÊNCIA INTERNA");
			getSuperParametroRelVO().setListaObjetos(listaObjetos);
			getSuperParametroRelVO().setUnidadeEnsino(super.getUnidadeEnsinoLogado().getNome());
			getSuperParametroRelVO().setCaminhoBaseRelatorio("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarEimprimirPDF() {
		editar();
		executarMetodoControle(DeclaracaoTransferenciaInternaRelControle.class.getSimpleName(), "imprimirPDF", getTransferenciaEntradaVO());
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
	
	public boolean getApresentarBotaoEstornar(){
		return Uteis.isAtributoPreenchido(getTransferenciaEntradaVO().getCodigo()) && !getTransferenciaEntradaVO().getSituacaoEstornado() && getLoginControle().getPermissaoAcessoMenuVO().getPermiteEstornarTransferenciaInterna();
	} 
	
	public void executarEstorno(){
		try{
			getFacadeFactory().getTransferenciaEntradaFacade().realizarEstornoTransferenciaInterna(getTransferenciaEntradaVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getTransferenciaEntradaVO().getMatricula().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			setMensagemID("msg_estornoRealizado", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}


	
	public void consultarMatriculaExistente(){
		try {
			getListaConsultaAluno().clear();
			List<MatriculaVO> matriculasPessoa = matriculasPessoa = getFacadeFactory().getMatriculaFacade()
					.consultaRapidaPorCodigoPessoaCurso(
							getTransferenciaEntradaVO().getMatricula().getAluno().getCodigo(), getTransferenciaEntradaVO().getCurso().getCodigo(), getTransferenciaEntradaVO().getUnidadeEnsino().getCodigo(), false,
							getUsuarioLogado());
			for (MatriculaVO matriculaPessoa : matriculasPessoa) {
				if (!getTransferenciaEntradaVO().getMatricula().getMatricula().equals(matriculaPessoa.getMatricula()) 
						&& getTransferenciaEntradaVO().getMatricula().getCurso().getNivelEducacional().equals(matriculaPessoa.getCurso().getNivelEducacional())) {
					getListaConsultaAluno().add(matriculaPessoa);
				}
			}
			if(getListaConsultaAluno().isEmpty()){
				throw new Exception("Não foi encontrado outra matrícula deste aluno.");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public Boolean getRealizandoTranferenciaEntradaMatriculaExistente() {
		if(realizandoTranferenciaEntradaMatriculaExistente == null){
			realizandoTranferenciaEntradaMatriculaExistente = false;
		}
		return realizandoTranferenciaEntradaMatriculaExistente;
	}

	public void setRealizandoTranferenciaEntradaMatriculaExistente(
			Boolean realizandoTranferenciaEntradaMatriculaExistente) {
		this.realizandoTranferenciaEntradaMatriculaExistente = realizandoTranferenciaEntradaMatriculaExistente;
	}

	public MatriculaVO getMatriculaSelecionada() {
		if(matriculaSelecionada == null){
			matriculaSelecionada = new MatriculaVO();
		}
		return matriculaSelecionada;
	}

	public void setMatriculaSelecionada(MatriculaVO matriculaSelecionada) {
		this.matriculaSelecionada = matriculaSelecionada;
	}
	


	public void gravarData(){
		try{
			getFacadeFactory().getTransferenciaEntradaFacade().alteraData(getTransferenciaEntradaVO(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		}catch(Exception e){
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public List<SelectItem> getListaSelectItemMatriculado() {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("naoMatriculado", "Não"));
		objs.add(new SelectItem("matriculado", "Sim"));
		objs.add(new SelectItem("ambas", "Ambas"));
		return objs;
	}

	public void setListaSelectItemMatriculado(List<SelectItem> listaSelectItemMatriculado) {
		this.listaSelectItemMatriculado = listaSelectItemMatriculado;
	}

	public String getSituacaoMatriculado() {
		if (situacaoMatriculado == null) {
			situacaoMatriculado = "";
		}
		return situacaoMatriculado;
	}

	public void setSituacaoMatriculado(String situacaoMatriculado) {
		this.situacaoMatriculado = situacaoMatriculado;
	}
	
	public void verificarSeExisteTransferenciaInternaParaMatricula() throws Exception {
		TransferenciaEntradaVO transferenciaEntrdadaVO = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorMatriculaESituacao(getMatriculaSelecionada().getMatricula(), "AV", Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaSelecionada().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
		
		if(!transferenciaEntrdadaVO.getCodigo().equals(0)) {
			getTransferenciaEntradaVO().setCodigo(transferenciaEntrdadaVO.getCodigo());
			setOncompleteModal("RichFaces.$('panelAvisoJaExisteTransferenciaInternaParaMatricula').show()");
		}else {	
			selecionarAluno();
		}
	}
	
	public void verificarPermissaoRealizarTransferenciaInternaMesmoCursoMatriculaIntegral() {
		try {
			if (getTransferenciaEntradaVO().getMatricula().getCurso().getPeriodicidade().equals(PeriodicidadeEnum.INTEGRAL.getValor())) {
				Matricula.verificarPermissaoUsuarioFuncionalidade(getUsuarioLogado(), "PermiteRealizarTransferenciaInternaMesmoCursoMatriculaIntegral");
				setPossuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral(true);
			}
		} catch (Exception e) {
			setPossuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral(false);
		}
	}

	public Boolean getPossuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral() {
		if (possuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral == null) {
			possuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral = false;
		}
		return possuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral;
	}

	public void setPossuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral(Boolean possuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral) {
		this.possuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral = possuiPermissaoTransferirInternamenteMesmoCursoMatriculaIntegral;
	}
	
	public void verificarSeExisteTransferenciaInternaMatricula() throws Exception {
		TransferenciaEntradaVO transferenciaEntrdadaVO = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorMatriculaESituacao(getTransferenciaEntradaVO().getMatricula().getMatricula(), "AV", Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaSelecionada().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
		if(!transferenciaEntrdadaVO.getCodigo().equals(0)) {
			getTransferenciaEntradaVO().setCodigo(transferenciaEntrdadaVO.getCodigo());
			setOncompleteModal("RichFaces.$('panelAvisoJaExisteTransferenciaInternaParaMatricula').show()");
		}else {	
			transferenciaEntrdadaVO.getMatricula().getMatricula();
		}
	}
	
}
