package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas matriculaForm.jsp
 * matriculaCons.jsp) com as funcionalidades da classe <code>Matricula</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Matricula
 * @see MatriculaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import controle.financeiro.MapaLancamentoFuturoControle;
import controle.processosel.VisaoCandidatoControle;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.DocumetacaoMatriculaVO;
import negocio.comuns.academico.GradeCurricularVO;
import negocio.comuns.academico.ItemPlanoFinanceiroAlunoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PeriodoLetivoVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.ProcessoMatriculaCalendarioVO;
import negocio.comuns.academico.TransferenciaEntradaDisciplinasAproveitadasVO;
import negocio.comuns.academico.TransferenciaEntradaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ConvenioCursoVO;
import negocio.comuns.financeiro.ConvenioTurnoVO;
import negocio.comuns.financeiro.ConvenioUnidadeEnsinoVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.financeiro.MapaLancamentoFuturoVO;
import negocio.comuns.financeiro.OrdemDescontoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.facade.jdbc.academico.Matricula;
import negocio.facade.jdbc.arquitetura.Usuario;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.financeiro.BoletoBancarioSV; @Controller("MatriculaControle")
@Scope("request")
@Lazy
public class MatriculaControle extends SuperControle implements Serializable {

	private MatriculaVO matriculaVO;
	private ProcessoMatriculaCalendarioVO processoCalendarioMatriculaVO;
	private String aluno_Erro;
	private List listaSelectItemUnidadeEnsino;
	private String curso_Erro;
	private String inscricao_Erro;
	private String responsavelLiberacaoMatricula_Erro;
	private String usuario_Erro;
	private List listaSelectItemTurno;
	private List listaSelectItemTipoMidiaCaptacao;
	private List listaSelectItemTurma;
	private List listaDisciplinasGradeCurricular;
	private PlanoFinanceiroAlunoVO planoFinanceiroAlunoVO;
	private ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoVO;
	protected Integer codigoUnidadeEnsinoCurso;
	protected List listaSelectItemConvenio;
	protected List listaSelectItemPlanoDesconto;
	private List listaSelectItemDescontoProgresivo;
	private DocumetacaoMatriculaVO documetacaoMatriculaVO;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private List listaSelectItemGradeCurricular;
	private String responsavelRenovacaoMatricula_Erro;
	private List listaSelectItemPeriodoLetivoMatricula;
	private String turma_Erro;
	private ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO = null;
	private Boolean imprimir;
	protected String userName;
	protected String senha;
	private Boolean matriculaForaPrazo;
	// campo para tela do candidato
	private Boolean apresentarPanelMatriculaCandidato;
	protected String guiaAba;
	protected List listaGradeDisciplinas;
	protected List listaSelectItemPeriodoLetivo;
	protected List listaSelectItemCurso;
	protected List listaConsultaCurso;
	protected List listaConsultaConvenio;
	protected List listaConsultaPlanoDesconto;
	protected String campoConsultaCurso;
	protected String valorConsultaCurso;
	protected List listaConsultaAluno;
	protected String campoConsultaAluno;
	protected String valorConsultaAluno;
	protected PlanoFinanceiroCursoVO planoFinanceiroCursoVO;
	protected CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVO;
	protected boolean validarCadastrarAluno;
	protected boolean liberarAvancar;
	protected boolean pedirLiberacaoMatricula;
	protected boolean exibirMatricula;
	protected List listaSelectItemDescontoAlunoMatricula;
	protected List listaSelectItemDescontoAlunoParcela;
	protected MapaLancamentoFuturoVO mapaLancamentoFuturoVO;
	protected List ordemDesconto;
	protected OrdemDescontoVO ordemDescontoVO;
	protected Boolean turmaComVagasPreenchidas;
	protected Boolean turmaComLotacaoPreenchida;

	public MatriculaControle() throws Exception {
		//obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setImprimir(false);
		setMensagemID("msg_entre_prmconsulta");
		setValidarCadastrarAluno(false);
		novo();
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>Matricula</code> para edição pelo usuário da
	 * aplicação.
	 */
	public String novo() throws Exception {         removerObjetoMemoria(this);
		setMatriculaForaPrazo(false);
		setMatriculaVO(new MatriculaVO());
		inicializarListasSelectItemTodosComboBox();
		setPlanoFinanceiroAlunoVO(new PlanoFinanceiroAlunoVO());
		setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
		setMatriculaPeriodoVO(new MatriculaPeriodoVO());
		setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
		setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
		inicializarUsuarioResponsavelMatriculaUsuarioLogado();
		setListaDisciplinasGradeCurricular(new ArrayList(0));
		setTurmaComLotacaoPreenchida(Boolean.FALSE);
		setTurmaComVagasPreenchidas(Boolean.FALSE);
		setPlanoFinanceiroCursoVO(new PlanoFinanceiroCursoVO());
		setListaConsultaConvenio(new ArrayList(0));
		setListaConsultaPlanoDesconto(new ArrayList(0));
		setGuiaAba("Inicio");
		setMensagemID("msg_entre_dados");
		setValidarCadastrarAluno(Boolean.FALSE);
		setLiberarAvancar(Boolean.FALSE);
		setPedirLiberacaoMatricula(Boolean.FALSE);
		setExibirMatricula(Boolean.FALSE);
		setMapaLancamentoFuturoVO(new MapaLancamentoFuturoVO());
		setOrdemDescontoVO(new OrdemDescontoVO());
		matriculaVO.setGuiaAba("Inicio");
		return "editar";
	}

	public void novaMatriculaDoCandidato() throws Exception {
		VisaoCandidatoControle visaoCandidato = (VisaoCandidatoControle) context().getExternalContext().getSessionMap().get("VisaoCandidatoControle");
		if (visaoCandidato != null) {
			visaoCandidato.inicializarMenuBoleto();
		}
	}

	public void inicializarUsuarioResponsavelMatriculaUsuarioLogado() {
		try {
			matriculaVO.setUsuario(getUsuarioLogadoClone());
			planoFinanceiroAlunoVO.setResponsavel(getUsuarioLogadoClone());
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Matricula</code> para alteração. O
	 * objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
	 * disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matricula");
		obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
		getPreencherDadosMatricula(obj);
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	public void getPreencherDadosMatricula(MatriculaVO obj) throws Exception {
		setMatriculaForaPrazo(false);
		setTurmaComLotacaoPreenchida(Boolean.FALSE);
		setTurmaComVagasPreenchidas(Boolean.FALSE);
		obj.setNovoObj(Boolean.FALSE);
		setMatriculaVO(obj);
		setPlanoFinanceiroAlunoVO(obj.getPlanoFinanceiroAluno());
		if (obj.getMatriculaPeriodoVOs() != null && !obj.getMatriculaPeriodoVOs().isEmpty()) {
			setMatriculaPeriodoVO((MatriculaPeriodoVO) obj.getMatriculaPeriodoVOs().get(0));
		}
		inicializarListasSelectItemTodosComboBox();
		inicializarResultadoProcSeletivoInscricao();
		getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
		montarListasPlanoDescontoConvenio();
		if (!matriculaVO.getSituacao().equals("PL") && !matriculaVO.getSituacao().equals("ID")) {
			matriculaVO.setGuiaAba("DadosBasicos");
		} else {
			navegarAbaDocumentacao();
			navegarAbaPlanoFinanceiroAluno();
		}
	}

	public String editarComMatricula(MatriculaVO obj) throws Exception {
		setMatriculaForaPrazo(false);
		setTurmaComLotacaoPreenchida(Boolean.FALSE);
		setTurmaComVagasPreenchidas(Boolean.FALSE);
		obj = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
		obj.setNovoObj(Boolean.FALSE);
		setMatriculaVO(obj);
		setPlanoFinanceiroAlunoVO(obj.getPlanoFinanceiroAluno());
		if (obj.getMatriculaPeriodoVOs() != null && !obj.getMatriculaPeriodoVOs().isEmpty()) {
			setMatriculaPeriodoVO((MatriculaPeriodoVO) obj.getMatriculaPeriodoVOs().get(0));
		}
		inicializarListasSelectItemTodosComboBox();
		inicializarResultadoProcSeletivoInscricao();
		getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
		setMensagemID("msg_dados_editar");
		return "editar";
	}

	public String subirItemLista() {
		setOrdemDescontoVO((OrdemDescontoVO) context().getExternalContext().getRequestMap().get("ordem"));
		for (int i = 1; i < getOrdemDesconto().size(); i++) {
			if (getOrdemDesconto().get(i).getDescricaoDesconto().equals(getOrdemDescontoVO().getDescricaoDesconto())) {
				OrdemDescontoVO ordem = (OrdemDescontoVO) getOrdemDesconto().get(i - 1);
				getOrdemDesconto().set(i - 1, getOrdemDescontoVO());
				getOrdemDesconto().set(i, ordem);
				calcularTotalDesconto();
				return "";
			}
		}
		return "";
	}

	public String descerItemLista() {
		setOrdemDescontoVO((OrdemDescontoVO) context().getExternalContext().getRequestMap().get("ordem"));
		for (int i = 0; i < getOrdemDesconto().size() - 1; i++) {
			if (getOrdemDesconto().get(i).getDescricaoDesconto().equals(getOrdemDescontoVO().getDescricaoDesconto())) {
				OrdemDescontoVO ordem = (OrdemDescontoVO) getOrdemDesconto().get(i + 1);
				getOrdemDesconto().set(i + 1, getOrdemDescontoVO());
				getOrdemDesconto().set(i, ordem);
				calcularTotalDesconto();
				return "";
			}
		}
		return "";
	}

	public void gravarForaPrazo() {
		try {
			verificaPermisaoMatriculaForaPrazo();
			setMatriculaForaPrazo(false);
			getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
			getMatriculaPeriodoVO().setResponsavelRenovacaoMatricula(getUsuarioLogadoClone());
			adicionarMatriculaPeriodo();
			matriculaVO.setPlanoFinanceiroAluno(getPlanoFinanceiroAlunoVO());
			if (matriculaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getMatriculaFacade().incluir(matriculaVO, getProcessoCalendarioMatriculaVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			} else {
				inicializarUsuarioResponsavelMatriculaUsuarioLogado();
				getFacadeFactory().getMatriculaFacade().alterar(matriculaVO, getProcessoCalendarioMatriculaVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMatriculaForaPrazo(false);
			setUserName("");
			setSenha("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gravarPagamentoMatriculaForaPrazo() {
		try {
			verificaPermisaoMatriculaForaPrazo();
			getFacadeFactory().getMatriculaPeriodoFacade().liberarMatriculaForaDoPrazo(matriculaVO, matriculaPeriodoVO, getProcessoCalendarioMatriculaVO(), getUsuarioLogado());
			setMatriculaForaPrazo(false);
			getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
			getMatriculaPeriodoVO().setResponsavelRenovacaoMatricula(getUsuarioLogadoClone());
			adicionarMatriculaPeriodo();
			matriculaVO.setPlanoFinanceiroAluno(getPlanoFinanceiroAlunoVO());
			if (matriculaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getMatriculaFacade().incluir(matriculaVO, getProcessoCalendarioMatriculaVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			} else {
				inicializarUsuarioResponsavelMatriculaUsuarioLogado();
				getFacadeFactory().getMatriculaFacade().alterar(matriculaVO, getProcessoCalendarioMatriculaVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMatriculaForaPrazo(false);
			setUserName("");
			setSenha("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificaPermisaoMatriculaForaPrazo() throws Exception {
		UsuarioVO usuario = Usuario.verificarLoginUsuario(getUserName(), getSenha(), true, Uteis.NIVELMONTARDADOS_TODOS);
		Matricula.verificarPermissaoUsuarioFuncionalidade(usuario, "MatriculaForaPrazo");
		getMatriculaPeriodoVO().setResponsavelMatriculaForaPrazo(usuario);
	}

	public void verificarPermissaoLiberarDescontoMatricula() throws Exception {
		if ((getMatriculaPeriodoVO().getValorDescontoMatricula().doubleValue() != 0.0) || (getMatriculaPeriodoVO().getValorDescontoMensalidade().doubleValue() != 0.0)) {
			try {
				if (isPedirLiberacaoMatricula()) {
					try {
						UsuarioVO usuario = Usuario.verificarLoginUsuario(getMatriculaVO().getUsuarioLiberacaoDesconto().getUsername(), getMatriculaVO().getUsuarioLiberacaoDesconto().getSenha(),
								true, Uteis.NIVELMONTARDADOS_TODOS);
						Matricula.verificarPermissaoUsuarioFuncionalidade(usuario, "Matricula_LiberarDescontoMatricula");
						getMatriculaVO().setUsuarioLiberacaoDesconto(usuario);
						gravar();
						getMatriculaVO().setUsuarioLiberacaoDesconto(new UsuarioVO());
						setPedirLiberacaoMatricula(Boolean.FALSE);
					} catch (Exception e) {
						setMensagemDetalhada("msg_erro", e.getMessage());
					}
				} else {
					UsuarioVO usuario = Usuario.verificarLoginUsuario(this.getUsuarioLogado().getUsername(), this.getUsuarioLogado().getSenha(), false, Uteis.NIVELMONTARDADOS_TODOS);
					Matricula.verificarPermissaoUsuarioFuncionalidade(usuario, "Matricula_LiberarDescontoMatricula");
					getMatriculaVO().setUsuarioLiberacaoDesconto(usuario);
					gravar();
					getMatriculaVO().setUsuarioLiberacaoDesconto(new UsuarioVO());
					setPedirLiberacaoMatricula(Boolean.FALSE);
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
				setPedirLiberacaoMatricula(Boolean.TRUE);
			}
		} else {
			gravar();
		}
	}

	public void indeferirMatricula() {
		try {
			UsuarioVO usuario = Usuario.verificarLoginUsuario(this.getUsuarioLogado().getUsername(), this.getUsuarioLogado().getSenha(), false, Uteis.NIVELMONTARDADOS_TODOS);
			Matricula.verificarPermissaoUsuarioFuncionalidade(usuario, "Matricula_LiberarDescontoMatricula");
			matriculaVO.setSituacao("ID");
			enviarComunicacaoAluno(usuario);
			enviarComunicacaoFuncionario(usuario);
			getFacadeFactory().getMatriculaFacade().excluirMatriculaDePendenciaFinanceira(matriculaVO, getUsuarioLogado());
			gravar();
		} catch (Exception e) {
			matriculaVO.setSituacao("PL");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void enviarComunicacaoAluno(UsuarioVO usuario) throws Exception {
		ComunicacaoInternaVO com = new ComunicacaoInternaVO();
		com.setAssunto("Matrícula Indeferida");
		com.setData(new Date());
		com.setEnviarEmail(false);
		com.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
		com.setMensagem("A solicitação de desconto para a matrícula " + matriculaVO.getMatricula() + " foi indeferida pelo departamento financeiro");
		com.setResponsavel(usuario.getPessoa());
		com.setTipoDestinatario("AL");
		com.setComunicadoInternoDestinatarioVOs(montarListaDestinatarioAluno());
		getFacadeFactory().getComunicacaoInternaFacade().incluir(com, false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(),null);
	}

	public void enviarComunicacaoFuncionario(UsuarioVO usuario) throws Exception {
		ComunicacaoInternaVO com = new ComunicacaoInternaVO();
		com.setAssunto("Matrícula Indeferida");
		com.setData(new Date());
		com.setEnviarEmail(false);
		com.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
		com.setMensagem("A solicitação de desconto para a matrícula " + matriculaVO.getMatricula() + " foi indeferida pelo departamento financeiro.\n" + "Entre em contato com o aluno: "
				+ matriculaVO.getAluno().getNome() + " pelo fone: " + matriculaVO.getAluno().getTelefoneRes());
		com.setResponsavel(usuario.getPessoa());
		com.setTipoDestinatario("FU");
		com.setComunicadoInternoDestinatarioVOs(montarListaDestinatarioFuncionario());
		getFacadeFactory().getComunicacaoInternaFacade().incluir(com, false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(),null);
	}

	public List<ComunicadoInternoDestinatarioVO> montarListaDestinatarioAluno() {
		try {
			List lista = new ArrayList(0);
			ComunicadoInternoDestinatarioVO CID = new ComunicadoInternoDestinatarioVO();
			CID.setDestinatario(matriculaVO.getAluno());
			CID.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
			CID.setTipoComunicadoInterno("AL");
			lista.add(CID);
			return lista;
		} catch (Exception e) {
			return new ArrayList(0);
		}
	}

	public List<ComunicadoInternoDestinatarioVO> montarListaDestinatarioFuncionario() {
		try {
			List lista = new ArrayList(0);
			ComunicadoInternoDestinatarioVO CID = new ComunicadoInternoDestinatarioVO();
			UsuarioVO responsavelMatricula = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(matriculaVO.getUsuario().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			CID.setDestinatario(responsavelMatricula.getPessoa());
			CID.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
			CID.setTipoComunicadoInterno("FU");
			lista.add(CID);
			return lista;
		} catch (Exception e) {
			return new ArrayList(0);
		}
	}

	public void enviarMatriculaMapaPendencia() throws Exception {
		getMatriculaVO().setUsuarioLiberacaoDesconto(new UsuarioVO());
		if (getMatriculaVO().getSituacao().equals("ID")) {
			getMatriculaVO().setMatriculaIndeferida(Boolean.TRUE);
		}
		getMatriculaVO().setSituacao("PL");
		gravar();
		getFacadeFactory().getMapaLancamentoFuturoFacade().incluir(getFacadeFactory().getMatriculaFacade().popularMapaLancamentoFuturo(matriculaVO, getProcessoCalendarioMatriculaVO()), getUsuarioLogado());
		setPedirLiberacaoMatricula(Boolean.FALSE);
		getMatriculaVO().setMatriculaIndeferida(Boolean.FALSE);
	}

	public void fecharForaPrazo() {
		setMatriculaForaPrazo(Boolean.FALSE);
		setUserName("");
		setSenha("");
	}

	public String getForaPrazo() {
		if (getMatriculaForaPrazo()) {
			return "RichFaces.$('panelForaPrazo').show()";
		}
		return "RichFaces.$('panelForaPrazo').hide()";
	}

	public String getPagamentoMatriculaForaPrazo() {
		if (getMatriculaForaPrazo()) {
			return "RichFaces.$('panelPagamentoMatriculaForaPrazo').show()";
		}
		return "RichFaces.$('panelPagamentoMatriculaForaPrazo').hide()";
	}

	public ProcessoMatriculaCalendarioVO verificarProcessoMatriculaDentroPrazo(Integer curso, Integer turno, Integer unidadeEnsino) throws Exception {
		try {
			setUserName("");
			setSenha("");
			List listaPeriodoMatricula = getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarProcessoMatriculaUnidadeEnsinoCursoDentroPrazo(curso, turno, unidadeEnsino, new Date(), false,
					Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			setMatriculaForaPrazo(false);
			return MatriculaVO.validarPeriodoProcessoMatricula(listaPeriodoMatricula);

		} catch (Exception e) {
			try {
				ProcessoMatriculaCalendarioVO obj = verificarProcessoMatriculaForaPrazo(curso, turno, unidadeEnsino);
				setMatriculaForaPrazo(true);
				return obj;
			} catch (Exception x) {
				setMatriculaForaPrazo(false);
				throw x;
			}
		}

	}

	public ProcessoMatriculaCalendarioVO verificarProcessoMatriculaForaPrazo(Integer curso, Integer turno, Integer unidadeEnsino) throws Exception {
		List listaPeriodoMatricula = getFacadeFactory().getProcessoMatriculaCalendarioFacade().consultarProcessoMatriculaUnidadeEnsinoCursoForaPrazo(curso, turno, unidadeEnsino, new Date(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return MatriculaVO.validarPeriodoProcessoMatricula(listaPeriodoMatricula);
	}

	public void inicializarControleTransferenciaEntrada() {
		TransferenciaEntradaControle transferenciaEntradaControle = (TransferenciaEntradaControle) context().getExternalContext().getSessionMap().get("TransferenciaEntradaControle");
		transferenciaEntradaControle.getTransferenciaEntradaVO().setMatricula(this.getMatriculaVO());
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Matricula</code>. Caso o
	 * objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
	 * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
	 * para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			setProcessoCalendarioMatriculaVO(verificarProcessoMatriculaDentroPrazo(matriculaVO.getCurso().getCodigo(), matriculaVO.getTurno().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo()));
			if (getMatriculaForaPrazo()) {
				return "editar";
			}
			getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
			getMatriculaPeriodoVO().setResponsavelRenovacaoMatricula(getUsuarioLogadoClone());
			adicionarMatriculaPeriodo();
			matriculaVO.setPlanoFinanceiroAluno(getPlanoFinanceiroAlunoVO());
			matriculaVO.setValorMatricula(getMatriculaPeriodoVO().getValorFinalMatricula());
			if (matriculaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getMatriculaFacade().incluir(matriculaVO, getProcessoCalendarioMatriculaVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			} else {
				inicializarUsuarioResponsavelMatriculaUsuarioLogado();
				getFacadeFactory().getMatriculaFacade().alterar(matriculaVO, getProcessoCalendarioMatriculaVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			}

			setMensagemID("msg_dados_gravados");
			navegarAbaDisciplinaMatriculado();
			return "editar";
		} catch (Exception e) {
			matriculaVO.setMatricula("");
			setMatriculaForaPrazo(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	public String navegarParaCadastroAluno() {
		return "cadastrarAluno";
	}

	public void navegarAbaDadosBasicos() {
		matriculaVO.setGuiaAba("DadosBasicos");
	}

	public void navegarAbaLiberadaMatricula() {
		matriculaVO.setGuiaAba("LiberadaMatricula");
	}

	public void navegarAbaPlanoFinanceiroAluno() {
		montarListaSelectItemDescontoProgressivo();
		montarListaSelectItemConvenio();
		setMensagemID("");
		setMensagemDetalhada("");
		calcularTotalDesconto();
		matriculaVO.setGuiaAba("PlanoFinanceiroAluno");
	}

	@SuppressWarnings( { "static-access"})
	public void navegarAbaDocumentacao() {
		try {
			matriculaPeriodoVO.setResponsavelRenovacaoMatricula(this.getUsuarioLogadoClone());
			if (!matriculaVO.getSituacao().equals("PL")) {
				matriculaVO.setUsuario(getUsuarioLogadoClone());
			}
			matriculaVO.validarDados(getMatriculaVO());
			matriculaPeriodoVO.validarDados(getMatriculaPeriodoVO());
			setProcessoCalendarioMatriculaVO(verificarProcessoMatriculaDentroPrazo(matriculaVO.getCurso().getCodigo(), matriculaVO.getTurno().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo()));
		
//                      ProcessoMatriculaCalendarioVO proc = obj.getProcessoMatriculaCalendarioVO();
                        TurmaVO turma = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getTurma().getCodigo().intValue(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
                        Integer nrMatricula = getFacadeFactory().getMatriculaPeriodoFacade().consultarQuantidadeAlunoMatriculadoTurma(turma.getCodigo().intValue(), getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO().getProcessoMatricula(), false, getUsuarioLogado());
                        matriculaPeriodoVO.validarVagaNaTurma(getMatriculaPeriodoVO(), nrMatricula, turma);

			matriculaVO.setGuiaAba("Documentacao");
			setMensagemID("");
			setMensagemDetalhada("");
			PlanoFinanceiroCursoVO planoFinanceiroTodosDados = getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorChavePrimaria(matriculaPeriodoVO.getPlanoFinanceiroCurso().getCodigo(), "AT",
					Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			setPlanoFinanceiroCursoVO(planoFinanceiroTodosDados);
			getFacadeFactory().getMatriculaPeriodoFacade().realizarCalculoValorMatriculaEMensalidade(matriculaVO, matriculaPeriodoVO,  getUsuarioLogado());
		} catch (Exception e) {
			navegarAbaDadosBasicos();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<TransferenciaEntradaDisciplinasAproveitadasVO> getDisciplinasAproveitamento() throws Exception {
		if (getMatriculaPeriodoVO().getTranferenciaEntrada() != 0) {
			TransferenciaEntradaVO transferenciaEntradaVO = getFacadeFactory().getTransferenciaEntradaFacade().consultarPorChavePrimaria(getMatriculaPeriodoVO().getTranferenciaEntrada(),
					Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (transferenciaEntradaVO.getTransferenciaEntradaDisciplinasAproveitadasVOs() != null || transferenciaEntradaVO.getTransferenciaEntradaDisciplinasAproveitadasVOs().size() != 0) {
				return transferenciaEntradaVO.getTransferenciaEntradaDisciplinasAproveitadasVOs();
			} else {
				return new ArrayList(0);
			}
		}
		return new ArrayList(0);
	}

	public void navegarAbaDisciplinaMatriculado() {
		matriculaVO.setGuiaAba("DisciplinaMatriculado");
	}

	public void navegarAbaInicio() {
		matriculaVO.setGuiaAba("Inicio");
	}

	public String navegarCadastrarAluno() {
		try {
			AlunoControle alunoControle = null;
			alunoControle = (AlunoControle) context().getExternalContext().getSessionMap().get(AlunoControle.class.getSimpleName());
			if (alunoControle == null) {
				alunoControle = new AlunoControle();
				context().getExternalContext().getSessionMap().put(AlunoControle.class.getSimpleName(), alunoControle);
			}
			alunoControle.novo();
			return "cadastrarAluno";
		} catch (Exception e) {
			return "";
		}
	}

	public String navegarMapaLancamentoFuturo() {
		try {
			return navegarPara(MapaLancamentoFuturoControle.class.getSimpleName(), "abaMatricula", "mapa");
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP MatriculaCons.jsp. Define o tipo de consulta a
	 * ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
	 * disponibiliza um List com os objetos selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				MatriculaVO obj = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				if (!obj.getMatricula().equals("")) {
					objs.add(obj);
				}
			}

			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("cpf")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorCPF(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomeCurso(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getMatriculaFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59),
						this.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("situacao")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("situacaoFinanceira")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorSituacaoFinanceira(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeResponsavel")) {
				objs = getFacadeFactory().getMatriculaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), true,
						Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>MatriculaVO</code> Após a exclusão ela
	 * automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			if (matriculaVO.getSituacaoFinanceira().equals("PF")) {
				matriculaVO.setPlanoFinanceiroAluno(getPlanoFinanceiroAlunoVO());
				getFacadeFactory().getMatriculaFacade().excluir(matriculaVO, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
				novo();
				setMensagemID("msg_dados_excluidos");
			} else {
				setMensagemDetalhada("Não é possivel Excluir uma Matrícula já Quitada Financeiramente");
			}
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	public void consultarCurso() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorCodigoCursoUnidadeEnsino(valorInt, matriculaVO.getUnidadeEnsino().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), matriculaVO.getUnidadeEnsino().getCodigo(), false, "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}

			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void montarConsultaCurso() {
		setListaConsultaCurso(new ArrayList(0));
	}

	public void selecionarCurso() throws Exception {
		try {
			UnidadeEnsinoCursoVO unidadeEnsinoCurso = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeensinocurso");
			getMatriculaVO().setCurso(unidadeEnsinoCurso.getCurso());
			getMatriculaVO().setTurno(unidadeEnsinoCurso.getTurno());
			GradeCurricularVO gradeCurricularVO = getFacadeFactory().getGradeCurricularFacade().consultarPorSituacaoGradeCurso(getMatriculaVO().getCurso().getCodigo(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			if (gradeCurricularVO.getCodigo() == 0) {
				throw new Exception("Não existe uma Grade Curricular Ativa para este curso.");
			}
			setMensagemDetalhada("");
			montarListaSelectItemGradeCurricular();
			getFacadeFactory().getMatriculaFacade().inicializarDocumentacaoMatriculaCurso(matriculaVO, getUsuarioLogado());
			matriculaPeriodoVO.setUnidadeEnsinoCurso(unidadeEnsinoCurso.getCodigo());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

	public void consultarAluno() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaAluno().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaAluno(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("CPF")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaAluno(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("RG")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorRG(getValorConsultaAluno(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}

			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void montarConsultaAluno() {
		setListaConsultaAluno(new ArrayList(0));
	}

	public void selecionarAluno() throws Exception {

		PessoaVO aluno = (PessoaVO) context().getExternalContext().getRequestMap().get("aluno");
		getMatriculaVO().getAluno().setNome(aluno.getNome());
		getMatriculaVO().getAluno().setCPF(aluno.getCPF());
		getMatriculaVO().getAluno().setRG(aluno.getRG());

	}

	public List getTipoConsultaComboAluno() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("RG", "RG"));
		return itens;
	}

	public boolean getExisteUnidadeEnsino() {
		if (getMatriculaVO().getUnidadeEnsino().getCodigo().intValue() != 0 || !(getMatriculaVO().getUnidadeEnsino().getNome().equals(""))) {
			return true;
		} else {
			return false;
		}
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe <code>MatriculaPeriodo</code> para o objeto
	 * <code>matriculaVO</code> da classe <code>Matricula</code>
	 */
	public void adicionarMatriculaPeriodo() throws Exception {
		if (!getMatriculaVO().getMatricula().equals("")) {
			matriculaPeriodoVO.setMatricula(getMatriculaVO().getMatricula());
		}
		if (getMatriculaPeriodoVO().getResponsavelRenovacaoMatricula().getCodigo().intValue() == 0) {
			getMatriculaPeriodoVO().setResponsavelRenovacaoMatricula(getUsuarioLogadoClone());
		}
		if (getMatriculaPeriodoVO().getPeridoLetivo().getCodigo().intValue() != 0) {
			Integer campoConsulta = getMatriculaPeriodoVO().getPeridoLetivo().getCodigo();
			PeriodoLetivoVO periodoLetivo = getFacadeFactory().getPeriodoLetivoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getMatriculaPeriodoVO().setPeridoLetivo(periodoLetivo);
		}
		if (getMatriculaPeriodoVO().getGradeCurricular().getCodigo().intValue() != 0) {
			Integer campoGradeCurricular = getMatriculaPeriodoVO().getGradeCurricular().getCodigo();
			GradeCurricularVO gradeCurricular = getFacadeFactory().getGradeCurricularFacade().consultarPorChavePrimaria(campoGradeCurricular, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getMatriculaPeriodoVO().setGradeCurricular(gradeCurricular);
		}
		getFacadeFactory().getMatriculaFacade().inicializarPlanoFinanceiroMatriculaPeriodo(this.getMatriculaVO(), this.getMatriculaPeriodoVO(), getUsuarioLogado());
		getMatriculaVO().adicionarObjMatriculaPeriodoVOs(getMatriculaPeriodoVO());
		setMensagemID("msg_dados_adicionados");
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe <code>MatriculaPeriodo</code> para edição pelo
	 * usuário.
	 */
	public String editarMatriculaPeriodo() throws Exception {
		MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodo");
		setMatriculaPeriodoVO(obj);
		return "editar";
	}

	/*
	 * Método responsável por remover um novo objeto da classe <code>MatriculaPeriodo</code> do objeto
	 * <code>matriculaVO</code> da classe <code>Matricula</code>
	 */
	public String removerMatriculaPeriodo() throws Exception {
		MatriculaPeriodoVO obj = (MatriculaPeriodoVO) context().getExternalContext().getRequestMap().get("matriculaPeriodo");
		getMatriculaVO().excluirObjMatriculaPeriodoVOs(obj.getPeridoLetivo().getCodigo());
		setMensagemID("msg_dados_excluidos");
		return "editar";
	}

	public void adicionarItemPlanoFinanceiroAlunoPlanoDesconto() throws Exception {
		try {
			if (!getPlanoFinanceiroAlunoVO().getCodigo().equals(0)) {
				itemPlanoFinanceiroAlunoVO.setPlanoFinanceiroAluno(getPlanoFinanceiroAlunoVO().getCodigo());
			}
			if (getItemPlanoFinanceiroAlunoVO().getPlanoDesconto().getCodigo().intValue() != 0) {
				getItemPlanoFinanceiroAlunoVO().setTipoItemPlanoFinanceiro("PD");
				Integer campoConsulta = getItemPlanoFinanceiroAlunoVO().getPlanoDesconto().getCodigo();
				PlanoDescontoVO planoDesconto = getFacadeFactory().getPlanoDescontoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				getItemPlanoFinanceiroAlunoVO().setPlanoDesconto(planoDesconto);
			}
			getFacadeFactory().getPlanoFinanceiroAlunoFacade().adicionarObjItemPlanoFinanceiroAlunoVOs(getPlanoFinanceiroAlunoVO(), getItemPlanoFinanceiroAlunoVO());
			this.setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
			calcularTotalDesconto();
			montarListasPlanoDescontoConvenio();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void adicionarItemPlanoFinanceiroAlunoConvenio() throws Exception {
		try {
			if (!getPlanoFinanceiroAlunoVO().getCodigo().equals(0)) {
				itemPlanoFinanceiroAlunoVO.setPlanoFinanceiroAluno(getPlanoFinanceiroAlunoVO().getCodigo());
			}
			if (getItemPlanoFinanceiroAlunoVO().getConvenio().getCodigo().intValue() != 0) {
				getItemPlanoFinanceiroAlunoVO().setTipoItemPlanoFinanceiro("CO");
				Integer campoConsulta = getItemPlanoFinanceiroAlunoVO().getConvenio().getCodigo();
				ConvenioVO convenio = getFacadeFactory().getConvenioFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
				getItemPlanoFinanceiroAlunoVO().setConvenio(convenio);
			}			
			getFacadeFactory().getPlanoFinanceiroAlunoFacade().adicionarObjItemPlanoFinanceiroAlunoVOs(getPlanoFinanceiroAlunoVO(), getItemPlanoFinanceiroAlunoVO());
			this.setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
			calcularTotalDesconto();
			montarListasPlanoDescontoConvenio();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void montarListasPlanoDescontoConvenio() {
		listaConsultaConvenio.clear();
		listaConsultaPlanoDesconto.clear();
		int indexConvenio = 0;
		int indexPlanoDesconto = 0;
		Iterator i = getPlanoFinanceiroAlunoVO().getItemPlanoFinanceiroAlunoVOs().iterator();
		while (i.hasNext()) {
			ItemPlanoFinanceiroAlunoVO objExistente = (ItemPlanoFinanceiroAlunoVO) i.next();
			if (objExistente.getTipoPlanoFinanceiroConvenio().booleanValue()) {
				listaConsultaConvenio.add(indexConvenio, objExistente);
				indexConvenio++;
			} else {
				listaConsultaPlanoDesconto.add(indexPlanoDesconto, objExistente);
				indexPlanoDesconto++;
			}
		}
	}

	public void calcularTotalDesconto() {
		matriculaPeriodoVO.descontoTotalMatricula(getPlanoFinanceiroCursoVO(), getPlanoFinanceiroAlunoVO(), getMatriculaPeriodoVO().getValorMatriculaCheio(), getOrdemDesconto(),
				getCondicaoPagamentoPlanoFinanceiroCursoVO());
		matriculaPeriodoVO.descontoTotalMensalidade(getPlanoFinanceiroCursoVO(), getPlanoFinanceiroAlunoVO(), getMatriculaPeriodoVO().getValorMensalidadeCheio(), getOrdemDesconto(),
				getCondicaoPagamentoPlanoFinanceiroCursoVO());
	}

	public void valorCheioTodosDescontos() {
		getMatriculaVO().getPlanoFinanceiroAluno().setOrdemDescontoAluno(0);
		getMatriculaVO().getPlanoFinanceiroAluno().setOrdemPlanoDesconto(0);
		getMatriculaVO().getPlanoFinanceiroAluno().setOrdemConvenio(0);
	}

	public void montarListaOrdemDesconto() throws Exception {
		if (matriculaVO.getMatricula().equals("")) {
			List obj = new ArrayList(0);
			OrdemDescontoVO ordem = new OrdemDescontoVO();
			ordem.setDescricaoDesconto("Desconto Aluno");
			ordem.setValorCheio(Boolean.TRUE);
			obj.add(ordem);
			ordem = new OrdemDescontoVO();
			ordem.setDescricaoDesconto("Plano Desconto");
			ordem.setValorCheio(Boolean.TRUE);
			obj.add(ordem);
			ordem = new OrdemDescontoVO();
			ordem.setDescricaoDesconto("Convênio");
			ordem.setValorCheio(Boolean.TRUE);
			obj.add(ordem);
			setOrdemDesconto(obj);
		} else {
			montarListaOrdemDescontoComMatricula();
		}
	}

	public void alterarTipoDescontoAlunoMatricula() {
		if (getPlanoFinanceiroAlunoVO().getTipoDescontoMatricula().equals("PO")) {
			getPlanoFinanceiroAlunoVO().setPercDescontoMatricula(getPlanoFinanceiroAlunoVO().getValorDescontoMatricula());
			getPlanoFinanceiroAlunoVO().setValorDescontoMatricula(0.0);
		} else if (getPlanoFinanceiroAlunoVO().getTipoDescontoMatricula().equals("VA")) {
			getPlanoFinanceiroAlunoVO().setValorDescontoMatricula(getPlanoFinanceiroAlunoVO().getPercDescontoMatricula());
			getPlanoFinanceiroAlunoVO().setPercDescontoMatricula(0.0);
		}
		calcularTotalDesconto();
	}

	public void alterarTipoDescontoAlunoParcela() {
		if (getPlanoFinanceiroAlunoVO().getTipoDescontoParcela().equals("PO")) {
			getPlanoFinanceiroAlunoVO().setPercDescontoParcela(getPlanoFinanceiroAlunoVO().getValorDescontoParcela());
			getPlanoFinanceiroAlunoVO().setValorDescontoParcela(0.0);
		} else if (getPlanoFinanceiroAlunoVO().getTipoDescontoParcela().equals("VA")) {
			getPlanoFinanceiroAlunoVO().setValorDescontoParcela(getPlanoFinanceiroAlunoVO().getPercDescontoParcela());
			getPlanoFinanceiroAlunoVO().setPercDescontoParcela(0.0);
		}
		calcularTotalDesconto();
	}

	/**
	 * Monta a ordem dos descontos, para valores abaixo de 3 a matrícula é calculada sobre o valor residual, para
	 * valores maior que 3 a matrícula é calculada sobre o valor cheio(valor Total).
	 * 
	 * @throws Exception
	 */
	public void montarListaOrdemDescontoComMatricula() throws Exception {
		MatriculaVO mat = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(matriculaVO.getMatricula(), matriculaVO.getUnidadeEnsino().getCodigo().intValue(), NivelMontarDados.getEnum(Uteis.NIVELMONTARDADOS_TODOS), getUsuarioLogado());
		List obj = new ArrayList(0);
		OrdemDescontoVO ordem = new OrdemDescontoVO();
		for (int i = 0; i <= 2; i++) {
			obj.add(ordem);
		}
		if (mat.getPlanoFinanceiroAluno().getOrdemDescontoAluno().intValue() < 3) {
			ordem.setDescricaoDesconto("Desconto Aluno");
			ordem.setValorCheio(Boolean.FALSE);
			obj.set(mat.getPlanoFinanceiroAluno().getOrdemDescontoAluno().intValue(), ordem);
		}
		if (mat.getPlanoFinanceiroAluno().getOrdemPlanoDesconto().intValue() < 3) {
			ordem = new OrdemDescontoVO();
			ordem.setDescricaoDesconto("Plano Desconto");
			ordem.setValorCheio(Boolean.FALSE);
			obj.set(mat.getPlanoFinanceiroAluno().getOrdemPlanoDesconto().intValue(), ordem);
		}
		if (mat.getPlanoFinanceiroAluno().getOrdemConvenio().intValue() < 3) {
			ordem = new OrdemDescontoVO();
			ordem.setDescricaoDesconto("Convênio");
			ordem.setValorCheio(Boolean.FALSE);
			obj.set(mat.getPlanoFinanceiroAluno().getOrdemConvenio().intValue(), ordem);
		}
		if (mat.getPlanoFinanceiroAluno().getOrdemDescontoAluno().intValue() >= 3 || mat.getPlanoFinanceiroAluno().getOrdemPlanoDesconto().intValue() >= 3
				|| mat.getPlanoFinanceiroAluno().getOrdemConvenio().intValue() >= 3) {
			montarListaOrdemDescontoComMatriculaValorTotal(mat, obj);
		} else {
			setOrdemDesconto(obj);
		}
	}

	public void montarListaOrdemDescontoComMatriculaValorTotal(MatriculaVO mat, List obj) {
		OrdemDescontoVO ordem = new OrdemDescontoVO();
		for (int i = 0; i <= 2; i++) {
			if (mat.getPlanoFinanceiroAluno().getOrdemDescontoAluno().intValue() == i + 3) {
				ordem = new OrdemDescontoVO();
				ordem.setDescricaoDesconto("Desconto Aluno");
				ordem.setValorCheio(Boolean.TRUE);
				obj.set(i, ordem);
			}
			if (mat.getPlanoFinanceiroAluno().getOrdemPlanoDesconto().intValue() == i + 3) {
				ordem = new OrdemDescontoVO();
				ordem.setDescricaoDesconto("Plano Desconto");
				ordem.setValorCheio(Boolean.TRUE);
				obj.set(i, ordem);
			}
			if (mat.getPlanoFinanceiroAluno().getOrdemConvenio().intValue() == i + 3) {
				ordem = new OrdemDescontoVO();
				ordem.setDescricaoDesconto("Convênio");
				ordem.setValorCheio(Boolean.TRUE);
				obj.set(i, ordem);
			}
		}
		setOrdemDesconto(obj);
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe <code>ItemPlanoFinanceiroAluno</code> para
	 * edição pelo usuário.
	 */
	public void editarItemPlanoFinanceiroAluno() throws Exception {
		ItemPlanoFinanceiroAlunoVO obj = (ItemPlanoFinanceiroAlunoVO) context().getExternalContext().getRequestMap().get("itemPlanoFinanceiroAluno");
		setItemPlanoFinanceiroAlunoVO(obj);

	}

	/*
	 * Método responsável por remover um novo objeto da classe <code>ItemPlanoFinanceiroAluno</code> do objeto
	 * <code>planoFinanceiroAlunoVO</code> da classe <code>PlanoFinanceiroAluno</code>
	 */
	public void removerItemPlanoFinanceiroAluno() throws Exception {
		ItemPlanoFinanceiroAlunoVO obj = (ItemPlanoFinanceiroAlunoVO) context().getExternalContext().getRequestMap().get("itemPlanoFinanceiroAluno");
		getPlanoFinanceiroAlunoVO().excluirObjItemPlanoFinanceiroAlunoVOs(obj.getPlanoDesconto().getCodigo());
		montarListasPlanoDescontoConvenio();
		calcularTotalDesconto();
		setMensagemID("msg_dados_excluidos");
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe <code>DocumetacaoMatricula</code> para o objeto
	 * <code>matriculaVO</code> da classe <code>Matricula</code>
	 */
	public String adicionarDocumetacaoMatricula() throws Exception {
		try {
			if (!getMatriculaVO().getMatricula().equals("")) {
				documetacaoMatriculaVO.setMatricula(getMatriculaVO().getMatricula());
			}
			getMatriculaVO().adicionarObjDocumetacaoMatriculaVOs(getDocumetacaoMatriculaVO());
			this.setDocumetacaoMatriculaVO(new DocumetacaoMatriculaVO());
			setMensagemID("msg_dados_adicionados");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe <code>DocumetacaoMatricula</code> para edição
	 * pelo usuário.
	 */
	public String editarDocumetacaoMatricula() throws Exception {
		DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatricula");
		setDocumetacaoMatriculaVO(obj);
		return "editar";
	}

	/*
	 * Método responsável por remover um novo objeto da classe <code>DocumetacaoMatricula</code> do objeto
	 * <code>matriculaVO</code> da classe <code>Matricula</code>
	 */
	public String removerDocumetacaoMatricula() throws Exception {
		DocumetacaoMatriculaVO obj = (DocumetacaoMatriculaVO) context().getExternalContext().getRequestMap().get("documetacaoMatricula");
		getMatriculaVO().excluirObjDocumetacaoMatriculaVOs(obj.getTipoDeDocumentoVO().getCodigo());
		setMensagemID("msg_dados_excluidos");
		return "editar";
	}

	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return "return mascara(this.form,'formCadastro:valorConsulta','99/99/9999',event);";
		}
		if (getControleConsulta().getCampoConsulta().equals("cpf")) {
			return "return mascara(this.form,'formCadastro:valorConsulta','999.999.999-99',event);";
		}
		return "";
	}

	public Boolean getApresentarComboBoxSituacao() {
		if (getControleConsulta().getCampoConsulta().equals("situacao")) {
			return true;
		}
		return false;
	}

	public void irPaginaInicial() throws Exception {
		// controleConsulta.setPaginaAtual(1);
		this.consultar();
	}

	public void irPaginaAnterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() throws Exception {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>PlanoDesconto</code>.
	 */
	public void montarListaSelectItemPlanoDesconto(String prm) throws Exception {
		List resultadoConsulta = consultarPlanoDescontoPorNome(prm);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			PlanoDescontoVO obj = (PlanoDescontoVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaSelectItemPlanoDesconto(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>PlanoDesconto</code>. Buscando todos os
	 * objetos correspondentes a entidade <code>PlanoDesconto</code>. Esta rotina não recebe parâmetros para filtragem
	 * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemPlanoDesconto() {
		try {
			montarListaSelectItemPlanoDesconto("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List consultarPlanoDescontoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getPlanoDescontoFacade().consultarPorNome(nomePrm, getMatriculaVO().getUnidadeEnsino().getCodigo(), false, getUsuarioLogado(), 0, 0);
		return lista;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>tipoItemPlanoFinanceiro</code>
	 */
	public List getListaSelectItemTipoItemPlanoFinanceiroItemPlanoFinanceiroAluno() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoItemPlanoFinanceiroAlunos = (Hashtable) Dominios.getTipoItemPlanoFinanceiroAluno();
		Enumeration keys = tipoItemPlanoFinanceiroAlunos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoItemPlanoFinanceiroAlunos.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>Convenio</code>.
	 */
	public void montarListaSelectItemConvenio(MatriculaVO matricula, String prm) throws Exception {
		List resultadoConsulta = consultarConvenioPorDescricao(prm);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			ConvenioVO obj = (ConvenioVO) i.next();
			if (validarConvenioUnidadeEnsino(matricula, obj) && validarConvenioCurso(matricula, obj) && validarConvenioTurno(matricula, obj)) {
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
			}
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaSelectItemConvenio(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>Convenio</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>Convenio</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto
	 * é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemConvenio() {
		try {
			montarListaSelectItemConvenio(getMatriculaVO(), "");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Responsavel por validar se a Uniadade de Ensino da Matricula esta inserida no Convenio
	 * 
	 * @param matricula
	 * @param convenio
	 * @return
	 */
	public boolean validarConvenioUnidadeEnsino(MatriculaVO matricula, ConvenioVO convenio) {
		if (convenio.isValidoParaTodaUnidadeEnsino().booleanValue()) {
			return true;
		} else {
			Iterator i = convenio.getConvenioUnidadeEnsinoVOs().iterator();
			while (i.hasNext()) {
				ConvenioUnidadeEnsinoVO unidadeEnsino = (ConvenioUnidadeEnsinoVO) i.next();
				if (matricula.getUnidadeEnsino().getCodigo().intValue() == unidadeEnsino.getUnidadeEnsino().getCodigo().intValue()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Responsavel por validar se o Curso da Matricula esta inserida no Convenio
	 * 
	 * @param matricula
	 * @param convenio
	 * @return
	 */
	public boolean validarConvenioCurso(MatriculaVO matricula, ConvenioVO convenio) {
		if (convenio.isValidoParaTodoCurso().booleanValue()) {
			return true;
		} else {
			Iterator i = convenio.getConvenioCursoVOs().iterator();
			while (i.hasNext()) {
				ConvenioCursoVO curso = (ConvenioCursoVO) i.next();
				if (matricula.getCurso().getCodigo().intValue() == curso.getCurso().getCodigo().intValue()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Responsavel por validar se o Turno da Matricula esta inserida no Convenio
	 * 
	 * @param matricula
	 * @param convenio
	 * @return
	 */
	public boolean validarConvenioTurno(MatriculaVO matricula, ConvenioVO convenio) {
		if (convenio.isValidoParaTodoTurno().booleanValue()) {
			return true;
		} else {
			Iterator i = convenio.getConvenioTurnoVOs().iterator();
			while (i.hasNext()) {
				ConvenioTurnoVO turno = (ConvenioTurnoVO) i.next();
				if (matricula.getTurno().getCodigo().intValue() == turno.getTurno().getCodigo().intValue()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List consultarConvenioPorDescricao(String descricaoPrm) throws Exception {
		List lista = getFacadeFactory().getConvenioFacade().consultarPorAtivoVigenteESituacao(true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>PeriodoLetivoMatricula</code>.
	 */
	public void montarListaSelectItemPeriodoLetivo(Integer prm) throws Exception {
		if (matriculaPeriodoVO.getGradeCurricular().getCodigo().equals(0)) {
			setListaSelectItemPeriodoLetivoMatricula(new ArrayList(0));
			return;
		}
		List resultadoConsulta = consultarPeriodoLetivoPorSigla(prm);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
			if (obj.getPeriodoLetivo().intValue() == 1) {
				getMatriculaPeriodoVO().setPeridoLetivo(obj);
			}
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaSelectItemPeriodoLetivoMatricula(objs);
		montarListaSelectItemTurma();
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>PeriodoLetivoMatricula</code>. Buscando
	 * todos os objetos correspondentes a entidade <code>PeriodoLetivo</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
	 * requisições Ajax.
	 */
	public void montarListaSelectItemPeriodoLetivo() {
		try {
			montarListaSelectItemPeriodoLetivo(getMatriculaPeriodoVO().getGradeCurricular().getCodigo());
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>sigla</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List consultarPeriodoLetivoPorSigla(Integer siglaPrm) throws Exception {
		List lista = getFacadeFactory().getPeriodoLetivoFacade().consultarPeriodoLetivos(siglaPrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>PeriodoLetivoMatricula</code>.
	 */
	public void montarListaSelectItemPeriodoLetivoMatricula(Integer prm) throws Exception {
		List resultadoConsulta = consultarPeriodoLetivoPorSigla(prm);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			PeriodoLetivoVO obj = (PeriodoLetivoVO) i.next();
			if (obj.getPeriodoLetivo().intValue() == 1) {
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
				getMatriculaPeriodoVO().setPeridoLetivo(obj);
			}
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaSelectItemPeriodoLetivoMatricula(objs);
		montarListaSelectItemTurma();
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>PeriodoLetivoMatricula</code>. Buscando
	 * todos os objetos correspondentes a entidade <code>PeriodoLetivo</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
	 * requisições Ajax.
	 */
	public void montarListaSelectItemPeriodoLetivoMatricula() {
		try {
			montarListaSelectItemPeriodoLetivoMatricula(getMatriculaPeriodoVO().getGradeCurricular().getCodigo());
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>PeriodoLetivo</code>.
	 */
	public void montarListaSelectItemGradeCurricular(String prm) throws Exception {
		if (getMatriculaVO().getUnidadeEnsino().getCodigo().equals(0)) {
			setListaSelectItemGradeCurricular(new ArrayList(0));
			return;
		}
		if (getMatriculaVO().getCurso().getCodigo().equals(0)) {
			setListaSelectItemGradeCurricular(new ArrayList(0));
			return;
		}
		List resultadoConsulta = consultarGradeCurricularPorDescricao(prm);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			GradeCurricularVO obj = (GradeCurricularVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			if (obj.getSituacao().equals("AT")) {
				getMatriculaPeriodoVO().setGradeCurricular(obj);
			}
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaSelectItemGradeCurricular(objs);
		montarListaSelectItemPeriodoLetivo();
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List consultarGradeCurricularPorDescricao(String descricaoPrm) throws Exception {
		List lista = getFacadeFactory().getGradeCurricularFacade().consultarPorCodigoCurso(getMatriculaVO().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemGradeCurricularEditar() {
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		objs.add(new SelectItem(matriculaPeriodoVO.getGradeCurricular().getCodigo(), matriculaPeriodoVO.getGradeCurricular().getNome()));
		setListaSelectItemGradeCurricular(objs);
		objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		objs.add(new SelectItem(matriculaPeriodoVO.getPeridoLetivo().getCodigo(), matriculaPeriodoVO.getPeridoLetivo().getDescricao()));
		setListaSelectItemPeriodoLetivoMatricula(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>PeriodoLetivo</code>. Buscando todos os
	 * objetos correspondentes a entidade <code>PeriodoLetivo</code>. Esta rotina não recebe parâmetros para filtragem
	 * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemGradeCurricular() {
		try {
			montarListaSelectItemGradeCurricular("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>situacao</code>
	 */
	public List getListaSelectItemSituacaoMatriculaPeriodo() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable matriculaPeriodoSituacaos = (Hashtable) Dominios.getMatriculaPeriodoSituacao();
		Enumeration keys = matriculaPeriodoSituacaos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) matriculaPeriodoSituacaos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>situacao</code>
	 */
	public List getListaSelectItemSituacaoDocumetacaoMatricula() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable situacaoEntregaDocumentacaos = (Hashtable) Dominios.getSituacaoEntregaDocumentacao();
		Enumeration keys = situacaoEntregaDocumentacaos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoEntregaDocumentacaos.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>tipoDocumento</code>
	 */
	public List getListaSelectItemTipoDocumentoDocumetacaoMatricula() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable tipoDocumentoDocumentacaoMatriculas = (Hashtable) Dominios.getTipoDocumentoDocumentacaoMatricula();
		Enumeration keys = tipoDocumentoDocumentacaoMatriculas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoDocumentoDocumentacaoMatriculas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do ComboBox correspondente ao atributo
	 * <code>situacao</code>
	 */
	public List getListaSelectItemSituacaoMatricula() throws Exception {
		List objs = new ArrayList(0);
		Hashtable situacaoMatriculas = (Hashtable) Dominios.getSituacaoMatricula();
		Enumeration keys = situacaoMatriculas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoMatriculas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	public List getListaSelectItemSituacaoFinanceiraMatricula() throws Exception {
		List objs = new ArrayList(0);
		Hashtable situacaoMatriculas = (Hashtable) Dominios.getSituacaoFinanceiraMatricula();
		Enumeration keys = situacaoMatriculas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoMatriculas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>TipoMidiaCaptacao</code>.
	 */
	public void montarListaSelectItemTipoMidiaCaptacao(String prm) throws Exception {
		List resultadoConsulta = consultarTipoMidiaCaptacaoPorNomeMidia(prm);
		setListaSelectItemTipoMidiaCaptacao(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nomeMidia"));
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>TipoMidiaCaptacao</code>. Buscando todos
	 * os objetos correspondentes a entidade <code>TipoMidiaCaptacao</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio
	 * requisições Ajax.
	 */
	public void montarListaSelectItemTipoMidiaCaptacao() {
		try {
			montarListaSelectItemTipoMidiaCaptacao("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nomeMidia</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List consultarTipoMidiaCaptacaoPorNomeMidia(String nomeMidiaPrm) throws Exception {
		List lista = getFacadeFactory().getTipoMidiaCaptacaoFacade().consultarPorNomeMidia(nomeMidiaPrm, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>Turno</code>.
	 */
	public void montarListaSelectItemTurno(String prm) throws Exception {
		List resultadoConsulta = consultarTurnoPorNome(prm);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			TurnoVO obj = (TurnoVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaSelectItemTurno(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>Turno</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>Turno</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
	 * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemTurno() {
		try {
			montarListaSelectItemTurno("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List consultarTurnoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getTurnoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public void inicializarResultadoProcSeletivoInscricao() {
		try {
			ResultadoProcessoSeletivoVO resultadoProcessoSeletivo = getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoInscricao_ResultadoUnico(
					matriculaVO.getInscricao().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
			this.setResultadoProcessoSeletivoVO(resultadoProcessoSeletivo);
			getResultadoProcessoSeletivoVO().getSituacaoAprovacaoGeralProcessoSeletivo();
		} catch (Exception e) {
			this.setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
		}
	}

	public void inicializarGradeCurricularMatriculaPeriodo() throws Exception {
		List gradeCurricularVO = getFacadeFactory().getGradeCurricularFacade().consultarPorCodigoCurso(matriculaVO.getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());

	}

	public void inicializarMatriculaComDadosInscricao() throws Exception {
		matriculaVO.setUnidadeEnsino(matriculaVO.getInscricao().getUnidadeEnsino());
		if (getResultadoProcessoSeletivoVO().getOpcaoCursoAprovadoProcessoSeletivo() == 1) {
			matriculaVO.getInscricao().setCursoOpcao1(
					getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getMatriculaVO().getInscricao().getCursoOpcao1().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			matriculaVO.setCurso(matriculaVO.getInscricao().getCursoOpcao1().getCurso());
			matriculaVO.setTurno(matriculaVO.getInscricao().getCursoOpcao1().getTurno());
			setCodigoUnidadeEnsinoCurso(matriculaVO.getInscricao().getCursoOpcao1().getCodigo());
			getMatriculaPeriodoVO().setUnidadeEnsinoCurso(getCodigoUnidadeEnsinoCurso());
		} else {
			if (getResultadoProcessoSeletivoVO().getOpcaoCursoAprovadoProcessoSeletivo() == 2) {
				matriculaVO.getInscricao().setCursoOpcao1(
						getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getMatriculaVO().getInscricao().getCursoOpcao2().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				matriculaVO.setCurso(matriculaVO.getInscricao().getCursoOpcao2().getCurso());
				matriculaVO.setTurno(matriculaVO.getInscricao().getCursoOpcao2().getTurno());
				setCodigoUnidadeEnsinoCurso(matriculaVO.getInscricao().getCursoOpcao2().getCodigo());
				getMatriculaPeriodoVO().setUnidadeEnsinoCurso(getCodigoUnidadeEnsinoCurso());
			} else {
				if (getResultadoProcessoSeletivoVO().getOpcaoCursoAprovadoProcessoSeletivo() == 3) {
					matriculaVO.getInscricao().setCursoOpcao1(
							getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(getMatriculaVO().getInscricao().getCursoOpcao3().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
					matriculaVO.setCurso(matriculaVO.getInscricao().getCursoOpcao3().getCurso());
					matriculaVO.setTurno(matriculaVO.getInscricao().getCursoOpcao3().getTurno());
					setCodigoUnidadeEnsinoCurso(matriculaVO.getInscricao().getCursoOpcao3().getCodigo());
					getMatriculaPeriodoVO().setUnidadeEnsinoCurso(getCodigoUnidadeEnsinoCurso());
				}
			}
		}
		matriculaVO.setAluno(matriculaVO.getInscricao().getCandidato());
		matriculaPeriodoVO.setData(matriculaVO.getData());
		matriculaPeriodoVO.setResponsavelRenovacaoMatricula(matriculaVO.getUsuario());
		matriculaPeriodoVO.setSituacao(matriculaVO.getSituacaoFinanceira());
		matriculaPeriodoVO.setUnidadeEnsinoCurso(codigoUnidadeEnsinoCurso);
		montarListaSelectItemGradeCurricular();
		if (matriculaVO.getDocumetacaoMatriculaVOs().size() == 0) {
			getFacadeFactory().getMatriculaFacade().inicializarDocumentacaoMatriculaCurso(this.getMatriculaVO(), getUsuarioLogado());
		}

	}

	public String getResultadoCandidato() throws Exception {
		if (getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("AP")) {
			List x = getListaCandidatosPorCurso();
			return "Candidato Aprovado para o Curso: " + getMatriculaVO().getInscricao().getCursoOpcao1().getCurso().getNome();
		} else if (getResultadoProcessoSeletivoVO().getResultadoSegundaOpcao().equals("AP")) {
			return "Candidato Aprovado para o Curso: " + getMatriculaVO().getInscricao().getCursoOpcao2().getCurso().getNome();
		} else if (getResultadoProcessoSeletivoVO().getResultadoTerceiraOpcao().equals("AP")) {
			return "Candidato Aprovado para o Curso: " + getMatriculaVO().getInscricao().getCursoOpcao3().getCurso().getNome();
		} else {
			return "Candidato não aprovado neste Processo Seletivo.";
		}
	}

	public List getListaCandidatosPorCurso() throws Exception {
		if (getResultadoProcessoSeletivoVO().getResultadoPrimeiraOpcao().equals("AP")) {
			return getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoECurso(getMatriculaVO().getInscricao().getProcSeletivo().getCodigo(),
					getMatriculaVO().getInscricao().getCursoOpcao1().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
		} else if (getResultadoProcessoSeletivoVO().getResultadoSegundaOpcao().equals("AP")) {
			return getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoECurso(getMatriculaVO().getInscricao().getProcSeletivo().getCodigo(),
					getMatriculaVO().getInscricao().getCursoOpcao2().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
		} else if (getResultadoProcessoSeletivoVO().getResultadoTerceiraOpcao().equals("AP")) {
			return getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoECurso(getMatriculaVO().getInscricao().getProcSeletivo().getCodigo(),
					getMatriculaVO().getInscricao().getCursoOpcao3().getCurso().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
		} else {
			return null;
		}
	}

	public void inicializarInscricaoEDadosRelacionados() {
		matriculaVO.setInscricao(new InscricaoVO());
		matriculaVO.setUnidadeEnsino(new UnidadeEnsinoVO());
		matriculaVO.setCurso(new CursoVO());
		matriculaVO.setTurno(new TurnoVO());
		matriculaVO.setAluno(new PessoaVO());
		setResultadoProcessoSeletivoVO(new ResultadoProcessoSeletivoVO());
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Inscricao</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarInscricaoPorChavePrimaria() throws Exception {
		try {
			Integer campoConsulta = matriculaVO.getInscricao().getCodigo();
			InscricaoVO inscricao = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			matriculaVO.setInscricao(inscricao);
			inicializarResultadoProcSeletivoInscricao();
			if (getResultadoProcessoSeletivoVO().getCodigo().intValue() == 0) {
				// Não encontrou nenhum resultado para a inscrição
				inicializarInscricaoEDadosRelacionados();
				this.setInscricao_Erro(getMensagemInternalizacao("msg_matricula_resultadoProcSeletivoNaoEncontrado"));
			} else {
				if (!getResultadoProcessoSeletivoVO().isAprovadoProcessoSeletivo()) {
					inicializarInscricaoEDadosRelacionados();
					this.setInscricao_Erro(getMensagemInternalizacao("msg_matricula_naoAprovadoResultadoProcSeletivo"));
				} else {
					inicializarMatriculaComDadosInscricao();
					this.setInscricao_Erro("");
					setMensagemID("msg_dados_consultados");
					setApresentarPanelMatriculaCandidato(true);
				}
			}

		} catch (Exception e) {
			novo();
			setTurma_Erro("");
			setMensagemID("");
			setMensagemDetalhada("msg_erro", "");
			inicializarInscricaoEDadosRelacionados();
			this.setInscricao_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	public void consultarInscricaoPorChavePrimariaVisaoCandidato() throws Exception {
		try {
			Integer campoInscricao = getMatriculaVO().getInscricao().getCodigo();
			MatriculaVO mat = getFacadeFactory().getMatriculaFacade().consultarPorInscricao(campoInscricao, false, Uteis.NIVELMONTARDADOS_TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			setMatriculaVO(mat);
			inicializarResultadoProcSeletivoInscricao();
			if (getResultadoProcessoSeletivoVO().getCodigo().intValue() == 0) {
				// Não encontrou nenhum resultado para a inscrição
				setApresentarPanelMatriculaCandidato(false);
				setMensagemDetalhada("msg_erro", "Inscrição Inválida");
				this.setInscricao_Erro(getMensagemInternalizacao("msg_matricula_resultadoProcSeletivoNaoEncontrado"));
			} else {
				if (!getResultadoProcessoSeletivoVO().isAprovadoProcessoSeletivo()) {
					setApresentarPanelMatriculaCandidato(false);
					setMensagemDetalhada("msg_erro", "Candidato Não Aprovado no Processo Seletivo");
					this.setInscricao_Erro(getMensagemInternalizacao("msg_matricula_naoAprovadoResultadoProcSeletivo"));
				} else {
					inicializarMatriculaComDadosInscricao();
					this.setInscricao_Erro("");
					setMensagemID("msg_dados_consultados");
				}
				setApresentarPanelMatriculaCandidato(true);
			}

		} catch (Exception e) {
			novo();
			setApresentarPanelMatriculaCandidato(false);
			setTurma_Erro("");
			setMensagemID("");
			setMensagemDetalhada("msg_erro", "Inscrição Inválida");
			this.setInscricao_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/* Verificando se candidato já se matriculou */
	public Boolean getCandidatoMatriculado() {
		if (getMatriculaVO().getMatricula().length() > 0) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Curso</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarCursoPorChavePrimaria() {
		try {
			Integer campoConsulta = matriculaVO.getCurso().getCodigo();
			CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_TODOS, false, getUsuarioLogado());
			matriculaVO.setCurso(curso);
			getFacadeFactory().getMatriculaFacade().inicializarDocumentacaoMatriculaCurso(matriculaVO, getUsuarioLogado());
			this.setCurso_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			matriculaVO.setCurso(new CursoVO());
			this.setCurso_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	public void liberarMatricula() {
		try {
			getFacadeFactory().getMatriculaPeriodoFacade().liberarPagamentoMatricula(matriculaVO, matriculaPeriodoVO, getProcessoCalendarioMatriculaVO(), getUsuarioLogado(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			setProcessoCalendarioMatriculaVO(verificarProcessoMatriculaDentroPrazo(matriculaVO.getCurso().getCodigo(), matriculaVO.getTurno().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo()));
			if (getMatriculaForaPrazo()) {
				return;
			}
			getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
			adicionarMatriculaPeriodo();
			matriculaVO.setPlanoFinanceiroAluno(getPlanoFinanceiroAlunoVO());
			if (matriculaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getMatriculaFacade().incluir(matriculaVO, getProcessoCalendarioMatriculaVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			} else {
				inicializarUsuarioResponsavelMatriculaUsuarioLogado();
				getFacadeFactory().getMatriculaFacade().alterar(matriculaVO, getProcessoCalendarioMatriculaVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaVO.getUnidadeEnsino().getCodigo()), getUsuarioLogado());
			}
			setMensagemID("msg_matricula_liberarMatricula");
		} catch (Exception e) {
			matriculaPeriodoVO.setResponsavelLiberacaoMatricula(new UsuarioVO());
			matriculaPeriodoVO.setDataLiberacaoMatricula(null);
			matriculaPeriodoVO.setSituacao("PF");
			setMensagemDetalhada("msg_erro", e.getMessage());
			// return "editar";
		}
	}

	public void emitirBoletoMatricula() {
		try {
			if (matriculaPeriodoVO.getSituacao().equals("PF")) {
				if (matriculaPeriodoVO.getNrDocumento().equals(""))  {
				//if (matriculaPeriodoVO.getNrDocumento().equals("") && matriculaPeriodoVO.getContaReceber().intValue() == 0) {
					getFacadeFactory().getMatriculaPeriodoFacade().emitirBoletoMatricula(matriculaPeriodoVO, getUsuarioLogado(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo()));
					if (matriculaVO.getMatricula().equals("")) {
						setProcessoCalendarioMatriculaVO(verificarProcessoMatriculaDentroPrazo(matriculaVO.getCurso().getCodigo(), matriculaVO.getTurno().getCodigo(), matriculaVO.getUnidadeEnsino().getCodigo()));
						if (getMatriculaForaPrazo()) {
							return;
						}
						getFacadeFactory().getDocumetacaoMatriculaFacade().executarGeracaoSituacaoDocumentacaoMatricula(matriculaVO, getUsuarioLogado());
						adicionarMatriculaPeriodo();
						matriculaVO.setPlanoFinanceiroAluno(getPlanoFinanceiroAlunoVO());
						if (matriculaVO.isNovoObj().booleanValue()) {
							getFacadeFactory().getMatriculaFacade().incluir(matriculaVO, getProcessoCalendarioMatriculaVO(), getCondicaoPagamentoPlanoFinanceiroCursoVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
						}
					}
					imprimirBoleto();
					setImprimir(true);
					setMensagemID("msg_inscricao_emitirBoletoPagamento");
				} else {
					getFacadeFactory().getMatriculaPeriodoFacade().emitirBoletoMatricula(matriculaPeriodoVO, getUsuarioLogado(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo()));
					getFacadeFactory().getMatriculaPeriodoFacade().alterar(matriculaPeriodoVO, matriculaVO, getProcessoCalendarioMatriculaVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(matriculaPeriodoVO.getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
					imprimirBoleto();
					setImprimir(true);
					setMensagemID("msg_inscricao_emitirBoletoPagamento");
				}
			} else {
				setMensagemID("msg_matricula_matriculaJaQuitadaFinanceiramente");
				setImprimir(false);
			}
		} catch (Exception e) {
			setMatriculaForaPrazo(false);
			getMatriculaPeriodoVO().setNrDocumento("");
			//getMatriculaPeriodoVO().setContaReceber(0);
			getMatriculaPeriodoVO().setDataEmissaoBoletoMatricula(null);
			getMatriculaPeriodoVO().setResponsavelEmissaoBoletoMatricula(new UsuarioVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
			// return "editar";
		}
	}

	public String getBoleto() {
		if (getImprimir()) {
			//return "abrirPopup('BoletoBancarioSV?codigoContaReceber=" + matriculaPeriodoVO.getContaReceber() + "&titulo=matricula', 'boletoMatricula', 780, 585)";
		}
		return "";
	}

	public void imprimirBoleto() throws Exception {
		BoletoBancarioSV ser = new BoletoBancarioSV();
		//ser.setCodigoContaReceber(getMatriculaPeriodoVO().getContaReceber());
	}

	public void montarListaSelectItemDescontoProgressivo(String prm) throws Exception {
		List resultadoConsulta = consultarDescontoProgressivoPorNome(prm);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			DescontoProgressivoVO obj = (DescontoProgressivoVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaSelectItemDescontoProgresivo(objs);
//		ConfiguracaoFinanceiroVO configuracaoFinanceiro = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(), null);
		ConfiguracaoFinanceiroVO configuracaoFinanceiro = getConfiguracaoFinanceiroPadraoSistema();
		if (matriculaVO.getMatricula().equals("")) {
			if (getCondicaoPagamentoPlanoFinanceiroCursoVO().getDescontoProgressivoPadrao().getCodigo().intValue() != 0) {
				getPlanoFinanceiroAlunoVO().getDescontoProgressivo().setCodigo(getCondicaoPagamentoPlanoFinanceiroCursoVO().getDescontoProgressivoPadrao().getCodigo());
				getPlanoFinanceiroAlunoVO().setJustificativa("Desconto Progressivo Geral é padrão para este Curso");
			} else if (configuracaoFinanceiro.getDescontoProgressivoPadrao().getCodigo().intValue() != 0) {
				getPlanoFinanceiroAlunoVO().getDescontoProgressivo().setCodigo(configuracaoFinanceiro.getDescontoProgressivoPadrao().getCodigo());
				getPlanoFinanceiroAlunoVO().setJustificativa("Desconto Progressivo Geral é padrão para esta Unidade de Ensino");
			}
		} else {
			getPlanoFinanceiroAlunoVO().getDescontoProgressivo().setCodigo(matriculaVO.getPlanoFinanceiroAluno().getDescontoProgressivo().getCodigo());
		}
	}

	public void montarListaSelectItemDescontoProgressivo() {
		try {
			montarListaSelectItemDescontoProgressivo("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public List consultarDescontoProgressivoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getDescontoProgressivoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>UnidadeEnsino</code>.
	 */
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
		setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>DescontoAlunoMatricula</code>.
	 */
	public void montarListaSelectItemDescontoAlunoMatricula() {
		List obj = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoDescontoAluno.class, false);
		setListaSelectItemDescontoAlunoMatricula(obj);
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>DescontoAlunoParcela</code>.
	 */
	public void montarListaSelectItemDescontoAlunoParcela() {
		List obj = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoDescontoAluno.class, false);
		setListaSelectItemDescontoAlunoParcela(obj);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os
	 * objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem
	 * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
	 * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
	 * correspondente
	 */
	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void emitirDadosMatricula() {
		try {
			imprimirMatricula();
			setExibirMatricula(true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirMatricula() throws Exception {
		//DadosMatriculaSV mat = new DadosMatriculaSV();
		//mat.setMatricula(matriculaVO.getMatricula());
	}

	public String getRelatorioMatricula() {
		if (isExibirMatricula()) {
			return "abrirPopup('DadosMatriculaSV?matricula=" + matriculaVO.getMatricula() + "&titulo=matricula', 'dadosMatricula', 780, 585)";
		}
		return "";

	}

	/**
	 * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemTipoMidiaCaptacao();
		setListaSelectItemPeriodoLetivoMatricula(new ArrayList(0));
		montarListaSelectItemTurma();
		montarListaSelectItemConvenio();
		montarListaSelectItemPlanoDesconto();
		montarListaSelectItemDescontoProgressivo();
		montarListaSelectItemGradeCurricular();
		montarListaSelectItemPeriodoLetivo();
		montarListaOrdemDesconto();
		montarListaSelectItemDescontoAlunoMatricula();
		montarListaSelectItemDescontoAlunoParcela();
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarPessoaPorChavePrimaria() {
		try {
			Integer campoConsulta = matriculaVO.getAluno().getCodigo();
//			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaCompletaPorChavePrimaria(campoConsulta, false, true, false, getUsuarioLogado());
			matriculaVO.setAluno(pessoa);
			// matriculaVO.getAluno().setNome(pessoa.getNome());
			this.setAluno_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			matriculaVO.getAluno().setCPF("");
			matriculaVO.getAluno().setNome("");
			matriculaVO.getAluno().setCodigo(0);
			this.setAluno_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	public void consultarAlunoPorCPF() {
		try {
			if (matriculaVO.getAluno().getCPF().length() == 14) {
				String campoConsulta = matriculaVO.getAluno().getCPF();
				PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(campoConsulta, 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				matriculaVO.setAluno(pessoa);
				this.setAluno_Erro("");
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			matriculaVO.getAluno().setCPF("");
			matriculaVO.getAluno().setNome("");
			matriculaVO.getAluno().setCodigo(0);
			this.setAluno_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarUsuarioMatriculaPorChavePrimaria() {
		try {
			Integer campoConsulta = matriculaVO.getUsuario().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			matriculaVO.getUsuario().setNome(pessoa.getNome());
			setUsuario_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			matriculaVO.getUsuario().setNome("");
			matriculaVO.getUsuario().setCodigo(0);
			setUsuario_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave
	 * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarResponsavelRenovacaoMatriculaPorChavePrimaria() {
		try {
			Integer campoConsulta = matriculaPeriodoVO.getResponsavelRenovacaoMatricula().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			matriculaPeriodoVO.getResponsavelRenovacaoMatricula().setNome(pessoa.getNome());
			setResponsavelRenovacaoMatricula_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			matriculaPeriodoVO.getResponsavelRenovacaoMatricula().setNome("");
			matriculaPeriodoVO.getResponsavelRenovacaoMatricula().setCodigo(0);
			setResponsavelRenovacaoMatricula_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	public Boolean getSituacaoFinanceira() {

		if (getControleConsulta().getCampoConsulta().equals("situacaoFinanceira")) {
			return true;
		}
		return false;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("cpf", "CPF"));
		itens.add(new SelectItem("matricula", "Matrícula"));
                itens.add(new SelectItem("nomeCurso", "Curso"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("situacao", "Situação"));
		itens.add(new SelectItem("situacaoFinanceira", "Situação Financeira"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {         removerObjetoMemoria(this);
		// setPaginaAtualDeTodas("0/0");
		setListaConsulta(new ArrayList(0));
		// definirVisibilidadeLinksNavegacao(0, 0);
		setMensagemID("msg_entre_prmconsulta");
		return "consultar";
	}

	public Integer getCodigoUnidadeEnsinoCurso() {
		return codigoUnidadeEnsinoCurso;
	}

	public void setCodigoUnidadeEnsinoCurso(Integer codigoUnidadeEnsinoCurso) {
		this.codigoUnidadeEnsinoCurso = codigoUnidadeEnsinoCurso;
	}

	public List getListaSelectItemDescontoProgresivo() {
		return listaSelectItemDescontoProgresivo;
	}

	public void setListaSelectItemDescontoProgresivo(List listaSelectItemDescontoProgresivo) {
		this.listaSelectItemDescontoProgresivo = listaSelectItemDescontoProgresivo;
	}

	public ItemPlanoFinanceiroAlunoVO getItemPlanoFinanceiroAlunoVO() {
		return itemPlanoFinanceiroAlunoVO;
	}

	public void setItemPlanoFinanceiroAlunoVO(ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoVO) {
		this.itemPlanoFinanceiroAlunoVO = itemPlanoFinanceiroAlunoVO;
	}

	public List getListaSelectItemConvenio() {
		return listaSelectItemConvenio;
	}

	public void setListaSelectItemConvenio(List listaSelectItemConvenio) {
		this.listaSelectItemConvenio = listaSelectItemConvenio;
	}

	public List getListaSelectItemPlanoDesconto() {
		return listaSelectItemPlanoDesconto;
	}

	public void setListaSelectItemPlanoDesconto(List listaSelectItemPlanoDesconto) {
		this.listaSelectItemPlanoDesconto = listaSelectItemPlanoDesconto;
	}

	public PlanoFinanceiroAlunoVO getPlanoFinanceiroAlunoVO() {
		return planoFinanceiroAlunoVO;
	}

	public void setPlanoFinanceiroAlunoVO(PlanoFinanceiroAlunoVO planoFinanceiroAlunoVO) {
		this.planoFinanceiroAlunoVO = planoFinanceiroAlunoVO;
	}

	public List getListaSelectItemPeriodoLetivoMatricula() {
		return (listaSelectItemPeriodoLetivoMatricula);
	}

	public void setListaSelectItemPeriodoLetivoMatricula(List listaSelectItemPeriodoLetivoMatricula) {
		this.listaSelectItemPeriodoLetivoMatricula = listaSelectItemPeriodoLetivoMatricula;
	}

	public String getResponsavelRenovacaoMatricula_Erro() {
		return responsavelRenovacaoMatricula_Erro;
	}

	public void setResponsavelRenovacaoMatricula_Erro(String responsavelRenovacaoMatricula_Erro) {
		this.responsavelRenovacaoMatricula_Erro = responsavelRenovacaoMatricula_Erro;
	}

	public List getListaSelectItemGradeCurricular() {
		return (listaSelectItemGradeCurricular);
	}

	public void setListaSelectItemGradeCurricular(List listaSelectItemGradeCurricular) {
		this.listaSelectItemGradeCurricular = listaSelectItemGradeCurricular;
	}

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public DocumetacaoMatriculaVO getDocumetacaoMatriculaVO() {
		return documetacaoMatriculaVO;
	}

	public void setDocumetacaoMatriculaVO(DocumetacaoMatriculaVO documetacaoMatriculaVO) {
		this.documetacaoMatriculaVO = documetacaoMatriculaVO;
	}

	public List getListaSelectItemTipoMidiaCaptacao() {
		return (listaSelectItemTipoMidiaCaptacao);
	}

	public void setListaSelectItemTipoMidiaCaptacao(List listaSelectItemTipoMidiaCaptacao) {
		this.listaSelectItemTipoMidiaCaptacao = listaSelectItemTipoMidiaCaptacao;
	}

	public List getListaSelectItemTurno() {
		return (listaSelectItemTurno);
	}

	public void setListaSelectItemTurno(List listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	public String getUsuario_Erro() {
		return usuario_Erro;
	}

	public void setUsuario_Erro(String usuario_Erro) {
		this.usuario_Erro = usuario_Erro;
	}

	public String getInscricao_Erro() {
		return inscricao_Erro;
	}

	public void setInscricao_Erro(String inscricao_Erro) {
		this.inscricao_Erro = inscricao_Erro;
	}

	public String getCurso_Erro() {
		return curso_Erro;
	}

	public void setCurso_Erro(String curso_Erro) {
		this.curso_Erro = curso_Erro;
	}

	public List getListaSelectItemUnidadeEnsino() {
		return (listaSelectItemUnidadeEnsino);
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public String getAluno_Erro() {
		return aluno_Erro;
	}

	public void setAluno_Erro(String aluno_Erro) {
		this.aluno_Erro = aluno_Erro;
	}

	public MatriculaVO getMatriculaVO() {
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public ResultadoProcessoSeletivoVO getResultadoProcessoSeletivoVO() {
		return resultadoProcessoSeletivoVO;
	}

	public void inicializarDisciplinasPeriodoLetivo() throws Exception {
		try {
			Integer prm = matriculaPeriodoVO.getPeridoLetivo().getCodigo();
			List l = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(prm, false, getUsuarioLogado(), null);
			matriculaPeriodoVO.getPeridoLetivo().setGradeDisciplinaVOs(l);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void setResultadoProcessoSeletivoVO(ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVO) {
		this.resultadoProcessoSeletivoVO = resultadoProcessoSeletivoVO;
	}

	public String getTurma_Erro() {
		return turma_Erro;
	}

	public void setTurma_Erro(String turma_Erro) {
		this.turma_Erro = turma_Erro;
	}

	public List getListaSelectItemTurma() {
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>identificadorTurma</code> Este atributo é uma lista (<code>List</code>)
	 * utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List consultarTurmaPorIdentificadorTurma(String identificadorTurmaPrm) throws Exception {
		List lista = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(identificadorTurmaPrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	/*
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
	 * relativo ao atributo <code>Turma</code>.
	 */
	public void montarListaSelectItemTurma(String prm) throws Exception {
		if ((matriculaVO.getCurso() == null) || (matriculaVO.getCurso().getCodigo().intValue() == 0)) {
			List objs = new ArrayList(0);
			setListaSelectItemTurma(objs);
			return;
		}
		if ((matriculaVO.getTurno() == null) || (matriculaVO.getTurno().getCodigo().intValue() == 0)) {
			List objs = new ArrayList(0);
			setListaSelectItemTurma(objs);
			return;
		}
		if ((matriculaPeriodoVO.getGradeCurricular() == null) || (matriculaPeriodoVO.getGradeCurricular().getCodigo().intValue() == 0)) {
			List objs = new ArrayList(0);
			setListaSelectItemTurma(objs);
			return;
		}

		if ((matriculaPeriodoVO.getPeridoLetivo() == null) || (matriculaPeriodoVO.getPeridoLetivo().getCodigo().intValue() == 0)) {
			List objs = new ArrayList(0);
			setListaSelectItemTurma(objs);
			return;
		}

		List resultadoConsulta = consultarTurmaPorIdentificadorTurma(prm);
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			TurmaVO obj = (TurmaVO) i.next();
			if ((obj.getUnidadeEnsino().getCodigo().equals(matriculaVO.getUnidadeEnsino().getCodigo())) && (obj.getTurno().getCodigo().equals(matriculaVO.getTurno().getCodigo()))
					&& (obj.getCurso().getCodigo().equals(matriculaVO.getCurso().getCodigo())) && (obj.getGradeCurricularVO().getCodigo().equals(matriculaPeriodoVO.getGradeCurricular().getCodigo()))
					&& (obj.getPeridoLetivo().getCodigo().equals(matriculaPeriodoVO.getPeridoLetivo().getCodigo()))) {
				objs.add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma()));
			}
		}
		if (objs.size() <= 1) {
			this.setTurma_Erro("Não existe uma turma cadastrada para esse curso, para o período e turno informado.");
		} else {
			this.setTurma_Erro("");
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaSelectItemTurma(objs);
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>Turma</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>Turma</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
	 * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemTurma() {
		try {
			montarListaSelectItemTurma("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public ProcessoMatriculaCalendarioVO getProcessoCalendarioMatriculaVO() {
		return processoCalendarioMatriculaVO;
	}

	public void setProcessoCalendarioMatriculaVO(ProcessoMatriculaCalendarioVO processoCalendarioMatriculaVO) {
		this.processoCalendarioMatriculaVO = processoCalendarioMatriculaVO;
	}

	public Boolean getMatriculaForaPrazo() {
		return matriculaForaPrazo;
	}

	public void setMatriculaForaPrazo(Boolean matriculaForaPrazo) {
		this.matriculaForaPrazo = matriculaForaPrazo;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List getListaDisciplinasGradeCurricular() {
		return listaDisciplinasGradeCurricular;
	}

	public void setListaDisciplinasGradeCurricular(List listaDisciplinasGradeCurricular) {
		this.listaDisciplinasGradeCurricular = listaDisciplinasGradeCurricular;
	}

	public String getResponsavelLiberacaoMatricula_Erro() {
		return responsavelLiberacaoMatricula_Erro;
	}

	public void setResponsavelLiberacaoMatricula_Erro(String responsavelLiberacaoMatricula_Erro) {
		this.responsavelLiberacaoMatricula_Erro = responsavelLiberacaoMatricula_Erro;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		matriculaVO = null;
		aluno_Erro = null;
		Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
		curso_Erro = null;
		inscricao_Erro = null;
		responsavelLiberacaoMatricula_Erro = null;
		usuario_Erro = null;
		Uteis.liberarListaMemoria(listaSelectItemTurno);
		Uteis.liberarListaMemoria(listaSelectItemTipoMidiaCaptacao);
		Uteis.liberarListaMemoria(listaSelectItemTurma);
		Uteis.liberarListaMemoria(listaDisciplinasGradeCurricular);
		// matriculaFacade = null;
		// pessoaFacade = null;
		// unidadeEnsinoFacade = null;
		// cursoFacade = null;
		// inscricaoFacade = null;
		// turnoFacade = null;
		// tipoMidiaCaptacaoFacade = null;
		// resultadoProcessoSeletivoFacade = null;
		// turmaFacade = null;
		documetacaoMatriculaVO = null;
		matriculaPeriodoVO = null;
		Uteis.liberarListaMemoria(listaSelectItemGradeCurricular);
		responsavelRenovacaoMatricula_Erro = null;
		Uteis.liberarListaMemoria(listaSelectItemPeriodoLetivoMatricula);
		turma_Erro = null;
		// gradeCurricularFacade = null;
		resultadoProcessoSeletivoVO = null;
	}

	public Boolean getImprimir() {
		return imprimir;
	}

	public void setImprimir(Boolean imprimir) {
		this.imprimir = imprimir;
	}

	/**
	 * @return the apresentarPanelMatriculaCandidato
	 */
	public Boolean getApresentarPanelMatriculaCandidato() {
		return apresentarPanelMatriculaCandidato;
	}

	/**
	 * @param apresentarPanelMatriculaCandidato
	 *            the apresentarPanelMatriculaCandidato to set
	 */
	public void setApresentarPanelMatriculaCandidato(Boolean apresentarPanelMatriculaCandidato) {
		this.apresentarPanelMatriculaCandidato = apresentarPanelMatriculaCandidato;
	}

	/**
	 * @return the guiaAba
	 */
	public String getGuiaAba() {
		return guiaAba;
	}

	/**
	 * @param guiaAba
	 *            the guiaAba to set
	 */
	public void setGuiaAba(String guiaAba) {
		this.guiaAba = guiaAba;
	}

	/**
	 * @return the listaGradeDisciplinas
	 */
	public List getListaGradeDisciplinas() {
		return listaGradeDisciplinas;
	}

	/**
	 * @param listaGradeDisciplinas
	 *            the listaGradeDisciplinas to set
	 */
	public void setListaGradeDisciplinas(List listaGradeDisciplinas) {
		this.listaGradeDisciplinas = listaGradeDisciplinas;
	}

	/**
	 * @return the listaSelectItemPeriodoLetivo
	 */
	public List getListaSelectItemPeriodoLetivo() {
		return listaSelectItemPeriodoLetivo;
	}

	/**
	 * @param listaSelectItemPeriodoLetivo
	 *            the listaSelectItemPeriodoLetivo to set
	 */
	public void setListaSelectItemPeriodoLetivo(List listaSelectItemPeriodoLetivo) {
		this.listaSelectItemPeriodoLetivo = listaSelectItemPeriodoLetivo;
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

	/**
	 * @return the listaConsultaConvenio
	 */
	public List getListaConsultaConvenio() {
		return listaConsultaConvenio;
	}

	/**
	 * @param listaConsultaConvenio
	 *            the listaConsultaConvenio to set
	 */
	public void setListaConsultaConvenio(List listaConsultaConvenio) {
		this.listaConsultaConvenio = listaConsultaConvenio;
	}

	/**
	 * @return the listaConsultaPlanoDesconto
	 */
	public List getListaConsultaPlanoDesconto() {
		return listaConsultaPlanoDesconto;
	}

	/**
	 * @param listaConsultaPlanoDesconto
	 *            the listaConsultaPlanoDesconto to set
	 */
	public void setListaConsultaPlanoDesconto(List listaConsultaPlanoDesconto) {
		this.listaConsultaPlanoDesconto = listaConsultaPlanoDesconto;
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
	 * @return the listaConsultaAluno
	 */
	public List getListaConsultaAluno() {
		return listaConsultaAluno;
	}

	/**
	 * @param listaConsultaAluno
	 *            the listaConsultaAluno to set
	 */
	public void setListaConsultaAluno(List listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	/**
	 * @return the campoConsultaAluno
	 */
	public String getCampoConsultaAluno() {
		return campoConsultaAluno;
	}

	/**
	 * @param campoConsultaAluno
	 *            the campoConsultaAluno to set
	 */
	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
	}

	/**
	 * @return the valorConsultaAluno
	 */
	public String getValorConsultaAluno() {
		return valorConsultaAluno;
	}

	/**
	 * @param valorConsultaAluno
	 *            the valorConsultaAluno to set
	 */
	public void setValorConsultaAluno(String valorConsultaAluno) {
		this.valorConsultaAluno = valorConsultaAluno;
	}

	/**
	 * @return the planoFinanceiroCursoVO
	 */
	public PlanoFinanceiroCursoVO getPlanoFinanceiroCursoVO() {
		return planoFinanceiroCursoVO;
	}

	/**
	 * @param planoFinanceiroCursoVO
	 *            the planoFinanceiroCursoVO to set
	 */
	public void setPlanoFinanceiroCursoVO(PlanoFinanceiroCursoVO planoFinanceiroCursoVO) {
		this.planoFinanceiroCursoVO = planoFinanceiroCursoVO;
	}

	/**
	 * @return the validarCadastrarAluno
	 */
	public boolean isValidarCadastrarAluno() {
		return validarCadastrarAluno;
	}

	/**
	 * @param validarCadastrarAluno
	 *            the validarCadastrarAluno to set
	 */
	public void setValidarCadastrarAluno(boolean validarCadastrarAluno) {
		this.validarCadastrarAluno = validarCadastrarAluno;
	}

	/**
	 * @return the liberarAvancar
	 */
	public boolean isLiberarAvancar() {
		return liberarAvancar;
	}

	/**
	 * @param liberarAvancar
	 *            the liberarAvancar to set
	 */
	public void setLiberarAvancar(boolean liberarAvancar) {
		this.liberarAvancar = liberarAvancar;
	}

	/**
	 * @return the pedirLiberacaoMatricula
	 */
	public boolean isPedirLiberacaoMatricula() {
		return pedirLiberacaoMatricula;
	}

	/**
	 * @param pedirLiberacaoMatricula
	 *            the pedirLiberacaoMatricula to set
	 */
	public void setPedirLiberacaoMatricula(boolean pedirLiberacaoMatricula) {
		this.pedirLiberacaoMatricula = pedirLiberacaoMatricula;
	}

	/**
	 * @return the turmaComVagasPreenchidas
	 */
	public Boolean getTurmaComVagasPreenchidas() {
		return turmaComVagasPreenchidas;
	}

	/**
	 * @param turmaComVagasPreenchidas
	 *            the turmaComVagasPreenchidas to set
	 */
	public void setTurmaComVagasPreenchidas(Boolean turmaComVagasPreenchidas) {
		this.turmaComVagasPreenchidas = turmaComVagasPreenchidas;
	}

	/**
	 * @return the turmaComLotacaoPreenchida
	 */
	public Boolean getTurmaComLotacaoPreenchida() {
		return turmaComLotacaoPreenchida;
	}

	/**
	 * @param turmaComLotacaoPreenchida
	 *            the turmaComLotacaoPreenchida to set
	 */
	public void setTurmaComLotacaoPreenchida(Boolean turmaComLotacaoPreenchida) {
		this.turmaComLotacaoPreenchida = turmaComLotacaoPreenchida;
	}

	/**
	 * @return the exibirMatricula
	 */
	public boolean isExibirMatricula() {
		return exibirMatricula;
	}

	/**
	 * @param exibirMatricula
	 *            the exibirMatricula to set
	 */
	public void setExibirMatricula(boolean exibirMatricula) {
		this.exibirMatricula = exibirMatricula;
	}

	/**
	 * @return the listaSelectItemDescontoAlunoMatricula
	 */
	public List getListaSelectItemDescontoAlunoMatricula() {
		return listaSelectItemDescontoAlunoMatricula;
	}

	/**
	 * @param listaSelectItemDescontoAlunoMatricula
	 *            the listaSelectItemDescontoAlunoMatricula to set
	 */
	public void setListaSelectItemDescontoAlunoMatricula(List listaSelectItemDescontoAlunoMatricula) {
		this.listaSelectItemDescontoAlunoMatricula = listaSelectItemDescontoAlunoMatricula;
	}

	/**
	 * @return the listaSelectItemDescontoAlunoParcela
	 */
	public List getListaSelectItemDescontoAlunoParcela() {
		return listaSelectItemDescontoAlunoParcela;
	}

	/**
	 * @param listaSelectItemDescontoAlunoParcela
	 *            the listaSelectItemDescontoAlunoParcela to set
	 */
	public void setListaSelectItemDescontoAlunoParcela(List listaSelectItemDescontoAlunoParcela) {
		this.listaSelectItemDescontoAlunoParcela = listaSelectItemDescontoAlunoParcela;
	}

	/**
	 * @return the mapaLancamentoFuturoVO
	 */
	public MapaLancamentoFuturoVO getMapaLancamentoFuturoVO() {
		return mapaLancamentoFuturoVO;
	}

	/**
	 * @param mapaLancamentoFuturoVO
	 *            the mapaLancamentoFuturoVO to set
	 */
	public void setMapaLancamentoFuturoVO(MapaLancamentoFuturoVO mapaLancamentoFuturoVO) {
		this.mapaLancamentoFuturoVO = mapaLancamentoFuturoVO;
	}

	/**
	 * @return the ordemDesconto
	 */
	public List<OrdemDescontoVO> getOrdemDesconto() {
		return ordemDesconto;
	}

	/**
	 * @param ordemDesconto
	 *            the ordemDesconto to set
	 */
	public void setOrdemDesconto(List ordemDesconto) {
		this.ordemDesconto = ordemDesconto;
	}

	/**
	 * @return the ordemDescontoVO
	 */
	public OrdemDescontoVO getOrdemDescontoVO() {
		return ordemDescontoVO;
	}

	/**
	 * @param ordemDescontoVO
	 *            the ordemDescontoVO to set
	 */
	public void setOrdemDescontoVO(OrdemDescontoVO ordemDescontoVO) {
		this.ordemDescontoVO = ordemDescontoVO;
	}

	/**
	 * @return the condicaoPagamentoPlanoFinanceiroCursoVO
	 */
	public CondicaoPagamentoPlanoFinanceiroCursoVO getCondicaoPagamentoPlanoFinanceiroCursoVO() {
		if (condicaoPagamentoPlanoFinanceiroCursoVO == null) {
			condicaoPagamentoPlanoFinanceiroCursoVO = new CondicaoPagamentoPlanoFinanceiroCursoVO();
		}
		return condicaoPagamentoPlanoFinanceiroCursoVO;
	}

	/**
	 * @param condicaoPagamentoPlanoFinanceiroCursoVO
	 *            the condicaoPagamentoPlanoFinanceiroCursoVO to set
	 */
	public void setCondicaoPagamentoPlanoFinanceiroCursoVO(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVO) {
		this.condicaoPagamentoPlanoFinanceiroCursoVO = condicaoPagamentoPlanoFinanceiroCursoVO;
	}
}
