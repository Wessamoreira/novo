package controle.financeiro;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import controle.arquitetura.SuperControle.MSG_TELA;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.ItemPlanoFinanceiroAlunoVO;
import negocio.comuns.academico.MatriculaPeriodoMensalidadeCalculadaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.PlanoFinanceiroAlunoLogVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoAcademicoEnum;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ConvenioVO;
import negocio.comuns.financeiro.OrdemDescontoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.FinanciamentoEstudantil;
import negocio.comuns.utilitarias.dominios.TipoCoberturaConvenio;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

/**
 * @author Wellington - 4 de jan de 2016
 */
@Controller("AlteracaoPlanoFinanceiroAlunoControle")
@Scope("viewScope")
@SuppressWarnings("rawtypes")
public class AlteracaoPlanoFinanceiroAlunoControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private MatriculaPeriodoVO matriculaPeriodoVO;
	private ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private boolean apresentarAbaDescontos = false;
	private boolean apresentarAbaDescontoPlanoFinanceiro = false;
	private boolean bloquearLancamentoDescontoMatricula = false;
	private boolean permiteAlterarOrdemDesconto = false;
	private boolean permiteAlteracaoDescontoProgressivoPrimeiraParcela = false;
	private boolean permiteAlteracaoCategoriaECondicaoPagamento = false;
	private boolean abrirPainelAvisoContaReceber = false;
	private boolean abrirPainelParcelaExtra = false;
	private boolean abrirPainelDigitarSenhaLiberacaoDescontoAluno = false;
	private boolean abrirPainelDescontoAluno = false;
	private boolean alterado = false;
	private List<ItemPlanoFinanceiroAlunoVO> itemPlanoFinanceiroAlunoConvenioVOs;
	private List<ItemPlanoFinanceiroAlunoVO> itemPlanoFinanceiroAlunoPlanoDescontoVOs;
	private List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs;
	private List<OrdemDescontoVO> ordemDescontoVOs;
	private List<ContaPagarVO> contaPagarRestituicaoConvenioAlunoVOs;
	private List<TurmaVO> turmaVOs;
	private List<PlanoFinanceiroAlunoLogVO> planoFinanceiroAlunoLogVOs;
	private List<SelectItem> listaSelectItemCampoConsulta;
	private List<SelectItem> listaSelectItemDescontoProgresivo;
	private List<SelectItem> listaSelectItemPlanoDesconto;
	private List<SelectItem> listaSelectItemConvenio;
	private List<SelectItem> listaSelectItemTipoDescontoAluno;
	private List<SelectItem> listaSelectItemCampoConsultaTurma;
	private List<SelectItem> listaSelectItemCampoConsultaCurso;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private String ano;
	private String semestre;
	private String username;
	private String senha;
	private String tipoDescontoMatricula;
	private String tipoDescontoParcela;
	private ConvenioVO convenioVO;
	private List<SelectItem> listaSelectItemParceiro;
	private ParceiroVO parceiroVO;	
	private boolean permitirUsuarioDesconsiderarDiferencaValorRateio = false;
	private Boolean permiteAlterarPlanoCondicaoFinanceiraCurso;
	private List listaSelectItemCategoriaPlanoFinanceiroCurso;
	private List listaSelectItemCondicaoPagamentoPlanoFinanceiro;
	private String tipoOperacaoFuncionalidadeLiberar;
	private String usernameLiberarOperacaoFuncionalidade;
	private String senhaLiberarOperacaoFuncionalidade;
	private List<OperacaoFuncionalidadeVO> operacaoFuncionalidadePersistirVOs;
	private String oncompleteOperacaoFuncionalidade;
	private boolean debitoFinanceiroAoIncluirConvenioLiberado = false;
	
	
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("alteracaoPlanoFinanceiroAlunoCons");
	}

	public String consultar() {
		try {
			getControleConsulta().setListaConsulta(getFacadeFactory().getAlteracaoPlanoFinanceiroAlunoFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getAno(), getSemestre(), "", getUnidadeEnsinoLogado(), true, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getControleConsulta().setListaConsulta(new ArrayList<MatriculaPeriodoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("alteracaoPlanoFinanceiroAlunoCons");
	}

	public String editar() {
		try {
			setMatriculaPeriodoVO((MatriculaPeriodoVO) getRequestMap().get("matriculaPeriodoItem"));
			getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(getMatriculaPeriodoVO(), NivelMontarDados.TODOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			getFacadeFactory().getMatriculaFacade().carregarDados(getMatriculaPeriodoVO().getMatriculaVO(), NivelMontarDados.TODOS, getUsuarioLogado());
			getFacadeFactory().getMatriculaPeriodoFacade().montarDadosProcessoMatriculaCalendarioVO(getMatriculaPeriodoVO().getMatriculaVO(), getMatriculaPeriodoVO(), NivelMontarDados.FORCAR_RECARGATODOSOSDADOS, getUsuarioLogado());
			getMatriculaPeriodoVO().setPlanoFinanceiroCursoPersistido(getMatriculaPeriodoVO().getPlanoFinanceiroCurso());
			getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCursoPersistido(getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso());
			getMatriculaPeriodoVO().setContaPagarRestituicaoVO(getFacadeFactory().getContaPagarFacade().consultaRapidaContaPagarRestituicaoValorPorMatriculaPeriodo(getMatriculaPeriodoVO().getMatricula(), getMatriculaPeriodoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			getFacadeFactory().getMatriculaFacade().inicializarPlanoFinanceiroMatriculaPeriodo(getMatriculaPeriodoVO().getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado());
			getMatriculaPeriodoVO().getMatriculaVO().setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if (!getConfiguracaoFinanceiroPadraoSistema().getRealizarMatriculaComFinanceiroManualAtivo()) {
				realizarCalculoValoresMatriculaMensalidadePeriodoAluno();
			}
			executarVerificacaoApresentarAbaDescontos();
			executarVerificacaoApresentarAbaDescontoPlanoFinanceiro();
			executarVerificacaoBloquearLancamentoDescontoMatricula();
			executarVerificacaoPermissaoAlterarOrdemDescontoMatricula();
			executarVerificacaoPermissaoAlterarCategoriaECondicaoPagamento();
			verificarUsuarioPodeAlterarDescontoProgressivoPrimeiraParcela();
			montarListaSelectItemParceiro();
			montarListaSelectItemFormaPagamento();

			carregarListaComAsOpcoesdoPlanoFinanceiro();
			
			setAbrirPainelAvisoContaReceber(false);
			setAbrirPainelParcelaExtra(false);
			setDebitoFinanceiroAoIncluirConvenioLiberado(false);
			getMatriculaPeriodoVO().setVindoTelaAlteracaoPlanoFinanceiroAluno(true);
			setParceiroVO(null);
			setAlterado(false);
			setMensagemID("msg_dados_editar");
			if (!Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo())) {
				getMatriculaPeriodoVO().setCondicaoPagamentoPlanoFinanceiroCurso(getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCursoPersistido());
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("alteracaoPlanoFinanceiroAlunoForm");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("alteracaoPlanoFinanceiroAlunoCons");
	}

	public void persistirValidandoContaReceberEmAbertoEParcelaExtraGerada() {
		try {
			
			if(!informacoesObrigatoriasPreenchidas()) {
				setMensagemDetalhada("msg_erro", UteisJSF.internacionalizar("msg_CamposObrigatoriosNaoPreenchidos"), Uteis.ERRO);
				return;
			}
			
			salvarLogDeLiberacaoParaAlterarCondicaoDePagamento();
			
			getFacadeFactory().getMatriculaFacade().calcularTotalDesconto(getMatriculaPeriodoVO().getMatriculaVO(), getMatriculaPeriodoVO(), getOrdemDescontoVOs(), getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
			executarValidacaoSeraGeradoParcelaExtraRepassarMatricula();
			validarContaReceberAbertaEntregue();
			if (!isAbrirPainelAvisoContaReceber() && !Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getValorDiferencaParcelasPagasParcelasGeradas())) {
				persistirComThrows();
			} else {
				executarVerificacaoAbrirPainelParcelaExtraComThrows();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			setAbrirPainelAvisoContaReceber(false);
		}
	}

	private void salvarLogDeLiberacaoParaAlterarCondicaoDePagamento() {
		
		try {
			for (OperacaoFuncionalidadeVO operacaoFuncionalidadeVO : getOperacaoFuncionalidadePersistirVOs()) {
				operacaoFuncionalidadeVO.setCodigoOrigem(getMatriculaPeriodoVO().getCodigo().toString());
				getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(operacaoFuncionalidadeVO);
			}
			getOperacaoFuncionalidadePersistirVOs().clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void executarValidacaoSeraGeradoParcelaExtraRepassarMatricula() {
		try {
			setPermitirUsuarioDesconsiderarDiferencaValorRateio(false);
			getMatriculaPeriodoVO().getMatriculaPeriodoContaReceberLogVOs().stream()
			.forEach(p->{
				p.setLiberadoDiferencaValorRateio(false);
				p.setResponsavelLiberadoDiferencaValorRateio(new UsuarioVO());
			});
			getMatriculaPeriodoVO().setValorDiferencaParcelasPagasParcelasGeradas(getFacadeFactory().getMatriculaPeriodoFacade().executarValidacaoSeraGeradoParcelaExtraRepassarMatricula(getMatriculaPeriodoVO().getMatriculaVO(), getMatriculaPeriodoVO(), getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
			setValorTotalParcelasInclusao(Uteis.arrendondarForcando2CadasDecimais(getFacadeFactory().getContaReceberFacade().consultarValorParcelasInclusaoPorMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo(), getUsuarioLogado())));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void executarVerificacaoPermitirUsuarioDesconsiderarDiferencaValorRateio() {
		try {		
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(getUsernameLiberarOperacaoFuncionalidade(), getSenhaLiberarOperacaoFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.MATRICULA_PERMITIR_USUARIO_DESCONSIDERAR_DIFERENCA_VALOR_RATEIO, usuarioVerif);
			setPermitirUsuarioDesconsiderarDiferencaValorRateio(true);
			getMatriculaPeriodoVO().getMatriculaPeriodoContaReceberLogVOs().stream()
			.forEach(p->{
				p.setLiberadoDiferencaValorRateio(true);
				p.getResponsavelLiberadoDiferencaValorRateio().setCodigo(getUsuarioLogado().getCodigo());
				p.getResponsavelLiberadoDiferencaValorRateio().setUsername(getUsuarioLogado().getUsername());
				p.getResponsavelLiberadoDiferencaValorRateio().getPessoa().setCodigo(getUsuarioLogado().getPessoa().getCodigo());
				p.getResponsavelLiberadoDiferencaValorRateio().getPessoa().setNome(getUsuarioLogado().getPessoa().getNome());
				});
			getMatriculaPeriodoVO().setValorDiferencaParcelasPagasParcelasGeradas(getFacadeFactory().getMatriculaPeriodoFacade().executarValidacaoSeraGeradoParcelaExtraRepassarMatricula(getMatriculaPeriodoVO().getMatriculaVO(), getMatriculaPeriodoVO(), getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));
			setValorTotalParcelasInclusao(Uteis.arrendondarForcando2CadasDecimais(getFacadeFactory().getContaReceberFacade().consultarValorParcelasInclusaoPorMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo(), getUsuarioLogado())));
			setOncompleteOperacaoFuncionalidade("RichFaces.$('panelPermitirUsuarioDesconsiderarDiferencaValorRateio').hide()");
		} catch (Exception e) {
			setPermitirUsuarioDesconsiderarDiferencaValorRateio(false);
			setOncompleteOperacaoFuncionalidade("");
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
	
	public void executarVerificacaoUsuarioPossuiPermissaoLiberacaoUsuarioDesconsiderarDiferencaValorRateio() {
		try {
			setUsernameLiberarOperacaoFuncionalidade("");
			setSenhaLiberarOperacaoFuncionalidade("");
			setOncompleteOperacaoFuncionalidade("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
	
	public void executarValidacaoCaculcoSeraConsideradoDiferencaValorRateio() {
		try {					
			getMatriculaPeriodoVO().setValorDiferencaParcelasPagasParcelasGeradas(getFacadeFactory().getMatriculaPeriodoFacade().executarValidacaoSeraGeradoParcelaExtraRepassarMatricula(getMatriculaPeriodoVO().getMatriculaVO(), getMatriculaPeriodoVO(), getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado()));			
			setValorTotalParcelasInclusao(Uteis.arrendondarForcando2CadasDecimais(getFacadeFactory().getContaReceberFacade().consultarValorParcelasInclusaoPorMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo(), getUsuarioLogado())));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
	
	public boolean isPermitirUsuarioDesconsiderarDiferencaValorRateio() {
		return permitirUsuarioDesconsiderarDiferencaValorRateio;
	}

	public void setPermitirUsuarioDesconsiderarDiferencaValorRateio(boolean permitirUsuarioDesconsiderarDiferencaValorRateio) {
		this.permitirUsuarioDesconsiderarDiferencaValorRateio = permitirUsuarioDesconsiderarDiferencaValorRateio;
	}

	private void validarContaReceberAbertaEntregue() throws Exception {
		List<ContaReceberVO> contaReceberVOs = getFacadeFactory().getContaReceberFacade().consultaRapidaPorMatriculaContaReceberMatriculaMensalidadeImpressaoBoletoTrue(getMatriculaPeriodoVO().getMatriculaVO().getMatricula(), getMatriculaPeriodoVO().getCodigo());
		setAbrirPainelAvisoContaReceber(Uteis.isAtributoPreenchido(contaReceberVOs));
	}

	public void executarVerificacaoAbrirPainelParcelaExtra() {
		try {
			setAbrirPainelAvisoContaReceber(false);
			executarVerificacaoAbrirPainelParcelaExtraComThrows();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private void executarVerificacaoAbrirPainelParcelaExtraComThrows() throws Exception {
		if (Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getValorDiferencaParcelasPagasParcelasGeradas())) {
			setAbrirPainelParcelaExtra(true);
		} else {
			persistirComThrows();
		}
	}

	public void persistir() {
		try {
			persistirComThrows();
			salvarLogDeLiberacaoParaAlterarCondicaoDePagamento();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Valida se as informacoes na tela estao preenchidas
	 * @return
	 */
	private Boolean informacoesObrigatoriasPreenchidas() {
		if(getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getCodigo() > 0)
			return true;
		
		return false;
	}

	private void persistirComThrows() throws Exception {
		getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setResponsavel(getUsuarioLogadoClone());
		getFacadeFactory().getMatriculaFacade().alterar(getMatriculaPeriodoVO().getMatriculaVO(), getMatriculaPeriodoVO(), getMatriculaPeriodoVO().getProcessoMatriculaCalendarioVO(), getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
		setPlanoFinanceiroAlunoLogVOs(getFacadeFactory().getPlanoFinanceiroAlunoLogFacade().consultarPorMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo()));
		getPlanoFinanceiroAlunoLogVOs().add(getFacadeFactory().getPlanoFinanceiroAlunoLogFacade().realizarCriacaoLogPlanoFinanceiroAlunoAtual(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno()));
		getFacadeFactory().getMatriculaPeriodoVencimentoFacade().realizarGeracaoDescricaoDescontoMatriculaPeriodoVencimento(getMatriculaPeriodoVO().getMatriculaVO(), getMatriculaPeriodoVO(), getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().obterOrdemAplicacaoDescontosPadraoAtual(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()), getUsuarioLogado());
		setAbrirPainelAvisoContaReceber(false);
		setAbrirPainelParcelaExtra(false);
		setAlterado(true);
		setMensagemID("msg_dados_gravados");
	}

	private void executarVerificacaoApresentarAbaDescontos() throws Exception {
		setApresentarAbaDescontos(verificarUsuarioPossuiPermissaoFuncionalidade("AlteracaoPlanoFinanceiroAluno_VisualizarAbaDescontos"));
	}

	private void executarVerificacaoApresentarAbaDescontoPlanoFinanceiro() throws Exception {
		setApresentarAbaDescontoPlanoFinanceiro(verificarUsuarioPossuiPermissaoFuncionalidade("AlteracaoPlanoFinanceiroAluno_VisualizarAbaDescontoPlanoFinanceiro"));
	}

	private void executarVerificacaoBloquearLancamentoDescontoMatricula() throws Exception {
		setBloquearLancamentoDescontoMatricula(verificarUsuarioPossuiPermissaoFuncionalidade("AlteracaoPlanoFinanceiroAluno_BloquearLancamentoDescontoParcelaMatricula"));
	}

	private void executarVerificacaoPermissaoAlterarOrdemDescontoMatricula() throws Exception {
		setPermiteAlterarOrdemDesconto(verificarUsuarioPossuiPermissaoFuncionalidade("AlteracaoPlanoFinanceiroAluno_AlterarOrdemDescontos"));
	}

	private void verificarUsuarioPodeAlterarDescontoProgressivoPrimeiraParcela() throws Exception {
		setPermiteAlteracaoDescontoProgressivoPrimeiraParcela(verificarUsuarioPossuiPermissaoFuncionalidade("AlteracaoPlanoFinanceiroAluno_LiberarDescontoProgressivoPrimeiraParcela"));
	}
	
	public boolean isPermiteAlteracaoCategoriaECondicaoPagamento() {
		return permiteAlteracaoCategoriaECondicaoPagamento;
	}

	public void setPermiteAlteracaoCategoriaECondicaoPagamento(boolean permiteAlteracaoCategoriaECondicaoPagamento) {
		this.permiteAlteracaoCategoriaECondicaoPagamento = permiteAlteracaoCategoriaECondicaoPagamento;
	}
	
	private void executarVerificacaoPermissaoAlterarCategoriaECondicaoPagamento() throws Exception {
		setPermiteAlteracaoCategoriaECondicaoPagamento(verificarUsuarioPossuiPermissaoFuncionalidade("AlteracaoPlanoFinanceiroAluno_AlterarCategoriaECondicaoPagamento"));
	}

	private boolean verificarUsuarioPossuiPermissaoFuncionalidade(String funcionalidade) throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidade(funcionalidade, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	private void realizarCalculoValoresMatriculaMensalidadePeriodoAluno() throws Exception {
		executarInicializacaoDadosBasicosCalculoEstimadoValoresMatriculaAluno();
		executarInicializacaoTodasListaSelectItem();
		getFacadeFactory().getMatriculaPeriodoFacade().realizarCalculoValorMatriculaEMensalidade(getMatriculaPeriodoVO().getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado());
		calcularValorTotalDesconto();
	}

	private void executarInicializacaoDadosBasicosCalculoEstimadoValoresMatriculaAluno() throws Exception {
		getFacadeFactory().getMatriculaPeriodoFacade().inicializarPlanoFinanceiroAlunoMatriculaPeriodo(getMatriculaPeriodoVO().getMatriculaVO(), getMatriculaPeriodoVO(), getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno(), true, getUsuarioLogado());
		setOrdemDescontoVOs(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().obterOrdemAplicacaoDescontosPadraoAtual());
		montarItemPlanoFinanceiroAluno();
	}

	@SuppressWarnings("unchecked")
	private void montarItemPlanoFinanceiroAluno() throws Exception {
		setItemPlanoFinanceiroAlunoPlanoDescontoVOs(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getItemPlanoFinanceiroAlunoVOs("PD"));
		setItemPlanoFinanceiroAlunoConvenioVOs(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getItemPlanoFinanceiroAlunoVOs("CO"));
	}

	private void executarInicializacaoTodasListaSelectItem() throws Exception {
		montarListaSelectItemConvenio();
		montarListaSelectItemPlanoDesconto();
		montarListaSelectItemDescontoProgressivo();
		montarListaSelectItemTipoDescontoAluno();
	}

	public List<SelectItem> getListaSelectItemCampoConsulta() {
		if (listaSelectItemCampoConsulta == null) {
			listaSelectItemCampoConsulta = new ArrayList<SelectItem>(0);
			listaSelectItemCampoConsulta.add(new SelectItem("aluno", "Aluno"));
			listaSelectItemCampoConsulta.add(new SelectItem("curso", "Curso"));
			listaSelectItemCampoConsulta.add(new SelectItem("matricula", "Matrícula"));
			listaSelectItemCampoConsulta.add(new SelectItem("turma", "Turma"));
		}
		return listaSelectItemCampoConsulta;
	}

	public void montarListaSelectItemConvenio() {
		try {
			List<ConvenioVO> convenioVOs = getFacadeFactory().getConvenioFacade().consultarConvenioAptoUsarNaMatriculaPorParceiro(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo(), getMatriculaPeriodoVO().getMatriculaVO().getCurso().getCodigo(), getMatriculaPeriodoVO().getMatriculaVO().getTurno().getCodigo(), getParceiroVO().getCodigo(), true, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemConvenio(UtilSelectItem.getListaSelectItem(convenioVOs, "codigo", "descricao"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemPlanoDesconto() {
		try {
			setListaSelectItemPlanoDesconto(new ArrayList<SelectItem>(0));
			List<PlanoDescontoVO> planoDescontoVOs = new ArrayList<PlanoDescontoVO>(0);
			if (getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getDefinirPlanoDescontoApresentarMatricula()) {
				planoDescontoVOs = getFacadeFactory().getPlanoDescontoDisponivelMatriculaFacade().consultarPlanoDescontoPorCodigoPlanoDescontoDisponivelMatricula(getMatriculaPeriodoVO(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			} else {
				planoDescontoVOs = getFacadeFactory().getPlanoDescontoFacade().consultarPlanoDescontoAtivoPorUnidadeEnsinoNivelComboBox(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino(), true, getUsuarioLogado());
			}
			getListaSelectItemPlanoDesconto().add(new SelectItem(0, ""));
			for (PlanoDescontoVO obj : planoDescontoVOs) {
				if (!Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoVO()) || obj.getUnidadeEnsinoVO().getCodigo().equals(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo())) {
					getListaSelectItemPlanoDesconto().add(new SelectItem(obj.getCodigo(), obj.getNome()));
				}
			}
			Collections.sort(getListaSelectItemPlanoDesconto(), new SelectItemOrdemValor());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	@SuppressWarnings("unchecked")
	private void montarListaSelectItemDescontoProgressivo() throws Exception {
		List<DescontoProgressivoVO> descontoProgressivoVOs = getFacadeFactory().getDescontoProgressivoFacade().consultarPorNome("", false, getUsuarioLogado());
		setListaSelectItemDescontoProgresivo(UtilSelectItem.getListaSelectItem(descontoProgressivoVOs, "codigo", "nome"));
	}

	private void montarListaSelectItemTipoDescontoAluno() {
		setListaSelectItemTipoDescontoAluno(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoDescontoAluno.class, false));
	}

	public void limparDadosConsultaTurma() {
		setValorConsultaTurma("");
		setTurmaVOs(new ArrayList<TurmaVO>(0));
		getControleConsulta().setListaConsulta(new ArrayList<>());
		setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
		setAno("");
		setSemestre("");
	}

	public boolean getApresentarCampoCurso() {
		return getControleConsulta().getCampoConsulta().equals("curso");
	}

	public boolean getApresentarCampoTurma() {
		return getControleConsulta().getCampoConsulta().equals("turma");
	}

	public void selecionarTurma() {
		TurmaVO obj = (TurmaVO) getRequestMap().get("turmaItem");
		getControleConsulta().setValorConsulta(obj.getIdentificadorTurma());
		getControleConsulta().setListaConsulta(new ArrayList(0));
		setValorConsultaTurma("");
		setTurmaVOs(new ArrayList<TurmaVO>(0));
		setMensagemID("msg_dados_selecionados");
	}

	public List<SelectItem> getListaSelectItemCampoConsultaTurma() {
		if (listaSelectItemCampoConsultaTurma == null) {
			listaSelectItemCampoConsultaTurma = new ArrayList<SelectItem>(0);
			listaSelectItemCampoConsultaTurma.add(new SelectItem("identificadorTurma", "Identificador"));
		}
		return listaSelectItemCampoConsultaTurma;
	}

	public void consultarTurma() {
		try {
			if (getCampoConsultaTurma().equals("identificadorTurma"))
				setTurmaVOs(getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), 0, this.getUnidadeEnsinoLogado().getCodigo(), false, false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void consultarCurso() {
		try {
			List<UnidadeEnsinoCursoVO> objs = new ArrayList<UnidadeEnsinoCursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				if (getValorConsultaCurso().trim() != null || !getValorConsultaCurso().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultaCurso().trim());
				}
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultaRapidaPorCursoUnidade(Integer.parseInt(getValorConsultaCurso()), 0, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorNomeCursoUnidadeEnsino(getValorConsultaCurso(), 0, false, "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setUnidadeEnsinoCursoVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setUnidadeEnsinoCursoVOs(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			setUnidadeEnsinoCursoVO((UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItem"));
			getControleConsulta().setValorConsulta(getUnidadeEnsinoCursoVO().getCodigo().toString());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		setValorConsultaCurso("");
		setUnidadeEnsinoCursoVOs(new ArrayList<UnidadeEnsinoCursoVO>(0));
	}

	public List<SelectItem> getListaSelectItemCampoConsultaCurso() {
		if (listaSelectItemCampoConsultaCurso == null) {
			listaSelectItemCampoConsultaCurso = new ArrayList<SelectItem>(0);
			listaSelectItemCampoConsultaCurso.add(new SelectItem("nome", "Nome"));
			listaSelectItemCampoConsultaCurso.add(new SelectItem("codigo", "Código"));
		}
		return listaSelectItemCampoConsultaCurso;
	}

	public void limparDadosConsultaCurso() {
		setValorConsultaCurso("");
		setUnidadeEnsinoCursoVOs(new ArrayList<UnidadeEnsinoCursoVO>(0));
		getControleConsulta().setListaConsulta(new ArrayList<>());
		setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
		setAno("");
		setSemestre("");
	}

	private void montarListaContaPagarRestituicaoConvenioAluno() throws Exception {
		setContaPagarRestituicaoConvenioAlunoVOs(getFacadeFactory().getContaPagarFacade().consultaRapidaContaPagarPorMatriculaPeriodoConvenio(getMatriculaPeriodoVO().getMatriculaVO().getMatricula(), getMatriculaPeriodoVO().getCodigo(), getItemPlanoFinanceiroAlunoVO().getConvenio().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
	}

	public String getOncompleteRemoverConvenioPlanoFinanceiroAlunoVerificandoRestituicao() {
		if(Uteis.isAtributoPreenchido(getDirecionamentoListaContaPagarRestituicaoConvenioAluno())){
			return getDirecionamentoListaContaPagarRestituicaoConvenioAluno();
		}
		if (Uteis.isAtributoPreenchido(getContaPagarRestituicaoConvenioAlunoVOs()))
			return "RichFaces.$('panelContaPagarRestituicaoConvenioAluno').show()";
		return "";
	}

	public void alterarTipoDescontoAlunoMatricula() {
		try {
			if (getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getTipoDescontoMatricula().equals("PO")) {
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setPercDescontoMatricula(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getValorDescontoMatricula());
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setValorDescontoMatricula(0.0);
			} else if (getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getTipoDescontoMatricula().equals("VA")) {
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setValorDescontoMatricula(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getPercDescontoMatricula());
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setPercDescontoMatricula(0.0);
			}
			calcularValorTotalDesconto();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			if (e.getMessage().contains("A soma dos descontos é maior que o valor da Matrícula ou Parcela (")) {
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setPercDescontoMatricula(0.0);
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setValorDescontoMatricula(0.0);
				try {
					calcularValorTotalDesconto();
				} catch (Exception ex) {
					setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
				}
			}
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarTipoDescontoAlunoParcela() {
		try {
			if (getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getTipoDescontoParcela().equals("PO")) {
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setPercDescontoParcela(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getValorDescontoParcela());
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setValorDescontoParcela(0.0);
			} else if (getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getTipoDescontoParcela().equals("VA")) {
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setValorDescontoParcela(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getPercDescontoParcela());
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setPercDescontoParcela(0.0);
			}
			calcularValorTotalDesconto();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			if (e.getMessage().contains("A soma dos descontos é maior que o valor da Matrícula ou Parcela (")) {
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setPercDescontoParcela(0.0);
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setValorDescontoParcela(0.0);
				try {
					calcularValorTotalDesconto();
				} catch (Exception ex) {
					setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
				}
			}
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarItemPlanoFinanceiroAlunoPlanoDesconto() {
		try {
			if (Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno())) {
				getItemPlanoFinanceiroAlunoVO().setPlanoFinanceiroAluno(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(getItemPlanoFinanceiroAlunoVO().getPlanoDesconto())) {
				getItemPlanoFinanceiroAlunoVO().setTipoItemPlanoFinanceiro("PD");
				getItemPlanoFinanceiroAlunoVO().setPlanoDesconto(getFacadeFactory().getPlanoDescontoFacade().consultarPorChavePrimaria(getItemPlanoFinanceiroAlunoVO().getPlanoDesconto().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			} else {
				throw new Exception("O campo PLANO DE DESCONTO não foi informado!");
			}			
			getFacadeFactory().getPlanoFinanceiroAlunoFacade().adicionarObjItemPlanoFinanceiroAlunoVOs(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno(), getItemPlanoFinanceiroAlunoVO());
			calcularValorTotalDesconto();
			setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
			montarItemPlanoFinanceiroAluno();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			if (e.getMessage().contains("A soma dos descontos é maior que o valor da Matrícula ou Parcela (")) {
				if (getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getItemPlanoFinanceiroAlunoVOs().contains(getItemPlanoFinanceiroAlunoVO())) {
					getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getItemPlanoFinanceiroAlunoVOs().remove(getItemPlanoFinanceiroAlunoVO());
					setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
					try {
						calcularValorTotalDesconto();
					} catch (Exception ex) {
						setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
					}
				}
			}
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void removerItemPlanoFinanceiroAluno() {
		try {
			setItemPlanoFinanceiroAlunoVO((ItemPlanoFinanceiroAlunoVO) context().getExternalContext().getRequestMap().get("itemPlanoFinanceiroAlunoItem"));
			getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().excluirObjItemPlanoFinanceiroAlunoVOs(getItemPlanoFinanceiroAlunoVO().getPlanoDesconto().getCodigo());
			montarItemPlanoFinanceiroAluno();
			calcularValorTotalDesconto();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void adicionarItemPlanoFinanceiroAlunoConvenio() {
		try {
			if (Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno())) {
				getItemPlanoFinanceiroAlunoVO().setPlanoFinanceiroAluno(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getCodigo());
			}
			if (Uteis.isAtributoPreenchido(getItemPlanoFinanceiroAlunoVO().getConvenio())) {
				getItemPlanoFinanceiroAlunoVO().setTipoItemPlanoFinanceiro("CO");
				getItemPlanoFinanceiroAlunoVO().setConvenio(getFacadeFactory().getConvenioFacade().consultarPorChavePrimaria(getItemPlanoFinanceiroAlunoVO().getConvenio().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			} else {
				throw new Exception("O campo CONVÊNIO nao está informado!");
			}
			if(isHabilitarRecursoValidacaoDebitoInclusaoConvenio()
	        		&& getFacadeFactory().getParceiroFacade().realizarVerificacaoDebitoFinanceiroAoIncluirConvenioMatricula(getItemPlanoFinanceiroAlunoVO().getConvenio().getParceiro().getCodigo())
	        		&& !isDebitoFinanceiroAoIncluirConvenioLiberado()) {
				setOncompleteModal("RichFaces.$('panelHabilitarRecursoValidacaoDebitoInclusaoConvenio').show()");
				setUsernameLiberarOperacaoFuncionalidade("");
				setSenhaLiberarOperacaoFuncionalidade("");
				inicializarMensagemVazia();
			}else {
				adicionarItemPlanoFinanceiroAlunoConvenioHabilitado();   
				setMensagemID("msg_dados_adicionados");
			}
		} catch (Exception e) {
			if (e.getMessage().contains("A soma dos descontos é maior que o valor da Matrícula ou Parcela (")) {
				if (getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getItemPlanoFinanceiroAlunoVOs().contains(getItemPlanoFinanceiroAlunoVO())) {
					getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getItemPlanoFinanceiroAlunoVOs().remove(getItemPlanoFinanceiroAlunoVO());
					setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
					try {
						calcularValorTotalDesconto();
					} catch (Exception ex) {
						setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
					}
				}
			}
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private void adicionarItemPlanoFinanceiroAlunoConvenioHabilitado() throws Exception {
		if (getItemPlanoFinanceiroAlunoVO().getConvenio().getPossuiDescontoAntecipacao()) {
			for (ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAluno : getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getItemPlanoFinanceiroAlunoVOs()) {
				if (itemPlanoFinanceiroAluno.getConvenio().getPossuiDescontoAntecipacao()) {
					throw new Exception("Não é possível incluir 2 convênios que possuem desconto de antecipação.");
				}
			}
		}			
		getFacadeFactory().getPlanoFinanceiroAlunoFacade().adicionarObjItemPlanoFinanceiroAlunoVOs(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno(), getItemPlanoFinanceiroAlunoVO());
		calcularValorTotalDesconto();
		setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
		montarItemPlanoFinanceiroAluno();
	}
	
	public void realizarVerificacaoUsuarioLiberacaoInclusaoConvenioDebitoFinanceiro() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberarOperacaoFuncionalidade(), this.getSenhaLiberarOperacaoFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.MATRICULA_LIBERAR_INCLUSAO_CONVENIO_DEBITO_FINANCEIRO, usuarioVerif);
			adicionarItemPlanoFinanceiroAlunoConvenioHabilitado();
			OperacaoFuncionalidadeVO of = getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.PARCEIRO, getItemPlanoFinanceiroAlunoVO().getConvenio().getParceiro().getCodigo().toString(), OperacaoFuncionalidadeEnum.PARCEIRO_LIBERAR_INCLUSAO_CONVENIO_DEBITO_FINANCEIRO, usuarioVerif, "");
			getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(of);
			setUsernameLiberarOperacaoFuncionalidade("");
			setSenhaLiberarOperacaoFuncionalidade("");
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
			setOncompleteModal("RichFaces.$('panelHabilitarRecursoValidacaoDebitoInclusaoConvenio').hide()");
		} catch (Exception e) {
			if (getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getItemPlanoFinanceiroAlunoVOs().contains(getItemPlanoFinanceiroAlunoVO())) {
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getItemPlanoFinanceiroAlunoVOs().remove(getItemPlanoFinanceiroAlunoVO());
				setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
				try {
					calcularValorTotalDesconto();
				} catch (Exception ex) {
					setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
				}
			}		
			setOncompleteModal("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public boolean isHabilitarRecursoValidacaoDebitoInclusaoConvenio() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.MATRICULA_HABILITAR_RECURSO_VALIDACAO_DEBITO_INCLUSAO_CONVENIO, getUsuarioLogadoClone());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean isDebitoFinanceiroAoIncluirConvenioLiberado() {
		return debitoFinanceiroAoIncluirConvenioLiberado;
	}

	public void setDebitoFinanceiroAoIncluirConvenioLiberado(boolean debitoFinanceiroAoIncluirConvenioLiberado) {
		this.debitoFinanceiroAoIncluirConvenioLiberado = debitoFinanceiroAoIncluirConvenioLiberado;
	}

	public void removerConvenioPlanoFinanceiroAlunoVerificandoRestituicao() {
		try {
			setDirecionamentoListaContaPagarRestituicaoConvenioAluno("");
//			setItemPlanoFinanceiroAlunoVO((ItemPlanoFinanceiroAlunoVO) context().getExternalContext().getRequestMap().get("itemPlanoFinanceiroAlunoItem"));
			montarListaContaPagarRestituicaoConvenioAluno();
			if (getContaPagarRestituicaoConvenioAlunoVOs().isEmpty()) {
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().excluirObjItemPlanoFinanceiroAlunoConvenioVOs(getItemPlanoFinanceiroAlunoVO().getConvenio().getCodigo());
				montarItemPlanoFinanceiroAluno();
				calcularValorTotalDesconto();
				setMensagemID("msg_dados_excluidos");
				setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void atualizarValorCheioPlanoFinanceiroAluno() {
		OrdemDescontoVO ordemClicou = (OrdemDescontoVO) context().getExternalContext().getRequestMap().get("ordemItem");
		try {
			getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().atualizarSituacaoValorCheioOrdemDesconto(ordemClicou);
			calcularValorTotalDesconto();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			if (e.getMessage().contains("A soma dos descontos é maior que o valor da Matrícula ou Parcela (")) {
				ordemClicou.setValorCheio(!ordemClicou.getValorCheio());
				try {
					calcularValorTotalDesconto();
				} catch (Exception ex) {
					setMensagemDetalhada("msg_erro", ex.getMessage(), Uteis.ERRO);
				}
			}
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void subirItemLista() {
		try {
			OrdemDescontoVO ordemSubir = (OrdemDescontoVO) context().getExternalContext().getRequestMap().get("ordemItem");
			getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().alterarOrdemAplicacaoDescontosSubindoItem(getOrdemDescontoVOs(), ordemSubir);
			calcularValorTotalDesconto();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			if (e.getMessage().contains("A soma dos descontos é maior que o valor da Matrícula ou Parcela (")) {
				descerItemLista();
			}
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void descerItemLista() {
		try {
			OrdemDescontoVO ordemDescer = (OrdemDescontoVO) context().getExternalContext().getRequestMap().get("ordemItem");
			getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().alterarOrdemAplicacaoDescontosDescentoItem(getOrdemDescontoVOs(), ordemDescer);
			calcularValorTotalDesconto();
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			if (e.getMessage().contains("A soma dos descontos é maior que o valor da Matrícula ou Parcela (")) {
				subirItemLista();
			}
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void calcularTotalDesconto() {
		try {
			calcularValorTotalDesconto();
			setMensagemID("msg_dados_calculados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private void calcularValorTotalDesconto() throws Exception {
		try {
			getFacadeFactory().getMatriculaFacade().calcularTotalDesconto(getMatriculaPeriodoVO().getMatriculaVO(), getMatriculaPeriodoVO(), getOrdemDescontoVOs(), getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
		} catch (Exception e) {
			if (e.getMessage().contains("A soma dos descontos é maior que o valor da Matrícula ou Parcela (")) {
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setPercDescontoMatricula(0.0);
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setValorDescontoMatricula(0.0);
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setPercDescontoParcela(0.0);
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setValorDescontoParcela(0.0);
			}
			throw e;
		}
	}

	public void limparDadosConsulta() {
		limparDadosConsultaTurma();
		limparDadosConsultaCurso();
		getControleConsulta().setValorConsulta("");
		setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
	}

	public void limparUsernameSenhaLiberacaoDesconto() {
		setUsername("");
		setSenha("");
		setAbrirPainelDigitarSenhaLiberacaoDescontoAluno(true);
		setMensagemID("msg_entre_prmlogin");
	}

	public void executarVerificacaoUsuarioPodeLiberarDescontoAlunoManual() {
		try {
			UsuarioVO usuarioVerificar = ControleAcesso.verificarLoginUsuario(getUsername(), getSenha(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("AlteracaoPlanoFinanceiroAluno_PermitirLiberacaoDesbloqueioPorSenhaDescontoAluno", usuarioVerificar);
			getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA_PERIODO, getMatriculaPeriodoVO().getCodigo().toString(), OperacaoFuncionalidadeEnum.LIBERACAO_DESCONTO_ALUNO, getUsuarioLogado(), ""));
			setAbrirPainelDigitarSenhaLiberacaoDescontoAluno(false);
			setBloquearLancamentoDescontoMatricula(false);
			setAbrirPainelDescontoAluno(true);
			setMensagemID("msg_Liberacao_realizadaComSucesso");
		} catch (Exception e) {
			setBloquearLancamentoDescontoMatricula(true);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		} finally {
			setUsername("");
			setSenha("");
		}
	}

	public void inicializarDadosEPermissoesAposInclusaoDescontoAlunoPorPermissao() {
		try {
			if (!getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getTipoDescontoParcela().equals(getTipoDescontoParcela())) {
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setTipoDescontoParcela(getTipoDescontoParcela());
				if (getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getTipoDescontoParcela().equals("PO")) {
					getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setPercDescontoParcela(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getValorDescontoParcela());
					getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setValorDescontoParcela(0.0);
				} else if (getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getTipoDescontoParcela().equals("VA")) {
					getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setValorDescontoParcela(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getPercDescontoParcela());
					getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setPercDescontoParcela(0.0);
				}
			} else if (!getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getTipoDescontoMatricula().equals(getTipoDescontoMatricula())) {
				getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setTipoDescontoMatricula(getTipoDescontoMatricula());
				if (getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getTipoDescontoMatricula().equals("PO")) {
					getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setPercDescontoMatricula(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getValorDescontoMatricula());
					getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setValorDescontoMatricula(0.0);
				} else if (getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getTipoDescontoMatricula().equals("VA")) {
					getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setValorDescontoMatricula(getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().getPercDescontoMatricula());
					getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().setPercDescontoMatricula(0.0);
				}
			}
			calcularValorTotalDesconto();
			setBloquearLancamentoDescontoMatricula(true);
			setAbrirPainelDescontoAluno(false);
			setMensagemID("msg_dados_calculados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
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

	public MatriculaPeriodoVO getMatriculaPeriodoVO() {
		if (matriculaPeriodoVO == null) {
			matriculaPeriodoVO = new MatriculaPeriodoVO();
		}
		return matriculaPeriodoVO;
	}

	public void setMatriculaPeriodoVO(MatriculaPeriodoVO matriculaPeriodoVO) {
		this.matriculaPeriodoVO = matriculaPeriodoVO;
	}

	public boolean isApresentarAbaDescontos() {
		return apresentarAbaDescontos;
	}

	public void setApresentarAbaDescontos(boolean apresentarAbaDescontos) {
		this.apresentarAbaDescontos = apresentarAbaDescontos;
	}

	public boolean isApresentarAbaDescontoPlanoFinanceiro() {
		return apresentarAbaDescontoPlanoFinanceiro;
	}

	public void setApresentarAbaDescontoPlanoFinanceiro(boolean apresentarAbaDescontoPlanoFinanceiro) {
		this.apresentarAbaDescontoPlanoFinanceiro = apresentarAbaDescontoPlanoFinanceiro;
	}

	public boolean isBloquearLancamentoDescontoMatricula() {
		return bloquearLancamentoDescontoMatricula;
	}

	public void setBloquearLancamentoDescontoMatricula(boolean bloquearLancamentoDescontoMatricula) {
		this.bloquearLancamentoDescontoMatricula = bloquearLancamentoDescontoMatricula;
	}

	public List<ItemPlanoFinanceiroAlunoVO> getItemPlanoFinanceiroAlunoConvenioVOs() {
		if (itemPlanoFinanceiroAlunoConvenioVOs == null) {
			itemPlanoFinanceiroAlunoConvenioVOs = new ArrayList<ItemPlanoFinanceiroAlunoVO>(0);
		}
		return itemPlanoFinanceiroAlunoConvenioVOs;
	}

	public void setItemPlanoFinanceiroAlunoConvenioVOs(List<ItemPlanoFinanceiroAlunoVO> itemPlanoFinanceiroAlunoConvenioVOs) {
		this.itemPlanoFinanceiroAlunoConvenioVOs = itemPlanoFinanceiroAlunoConvenioVOs;
	}

	public List<ItemPlanoFinanceiroAlunoVO> getItemPlanoFinanceiroAlunoPlanoDescontoVOs() {
		if (itemPlanoFinanceiroAlunoPlanoDescontoVOs == null) {
			itemPlanoFinanceiroAlunoPlanoDescontoVOs = new ArrayList<ItemPlanoFinanceiroAlunoVO>(0);
		}
		return itemPlanoFinanceiroAlunoPlanoDescontoVOs;
	}

	public void setItemPlanoFinanceiroAlunoPlanoDescontoVOs(List<ItemPlanoFinanceiroAlunoVO> itemPlanoFinanceiroAlunoPlanoDescontoVOs) {
		this.itemPlanoFinanceiroAlunoPlanoDescontoVOs = itemPlanoFinanceiroAlunoPlanoDescontoVOs;
	}

	public List<SelectItem> getListaSelectItemDescontoProgresivo() {
		if (listaSelectItemDescontoProgresivo == null) {
			listaSelectItemDescontoProgresivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDescontoProgresivo;
	}

	public void setListaSelectItemDescontoProgresivo(List<SelectItem> listaSelectItemDescontoProgresivo) {
		this.listaSelectItemDescontoProgresivo = listaSelectItemDescontoProgresivo;
	}

	public List<SelectItem> getListaSelectItemPlanoDesconto() {
		if (listaSelectItemPlanoDesconto == null) {
			listaSelectItemPlanoDesconto = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemPlanoDesconto;
	}

	public void setListaSelectItemPlanoDesconto(List<SelectItem> listaSelectItemPlanoDesconto) {
		this.listaSelectItemPlanoDesconto = listaSelectItemPlanoDesconto;
	}

	public List<SelectItem> getListaSelectItemConvenio() {
		if (listaSelectItemConvenio == null) {
			listaSelectItemConvenio = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemConvenio;
	}

	public void setListaSelectItemConvenio(List<SelectItem> listaSelectItemConvenio) {
		this.listaSelectItemConvenio = listaSelectItemConvenio;
	}

	public List<OrdemDescontoVO> getOrdemDescontoVOs() {
		if (ordemDescontoVOs == null) {
			ordemDescontoVOs = new ArrayList<OrdemDescontoVO>(0);
		}
		return ordemDescontoVOs;
	}

	public void setOrdemDescontoVOs(List<OrdemDescontoVO> ordemDescontoVOs) {
		this.ordemDescontoVOs = ordemDescontoVOs;
	}

	public List<SelectItem> getListaSelectItemTipoDescontoAluno() {
		if (listaSelectItemTipoDescontoAluno == null) {
			listaSelectItemTipoDescontoAluno = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTipoDescontoAluno;
	}

	public void setListaSelectItemTipoDescontoAluno(List<SelectItem> listaSelectItemTipoDescontoAluno) {
		this.listaSelectItemTipoDescontoAluno = listaSelectItemTipoDescontoAluno;
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

	public List<TurmaVO> getTurmaVOs() {
		if (turmaVOs == null) {
			turmaVOs = new ArrayList<TurmaVO>(0);
		}
		return turmaVOs;
	}

	public void setTurmaVOs(List<TurmaVO> turmaVOs) {
		this.turmaVOs = turmaVOs;
	}

	public boolean isPermiteAlterarOrdemDesconto() {
		return permiteAlterarOrdemDesconto;
	}

	public void setPermiteAlterarOrdemDesconto(boolean permiteAlterarOrdemDesconto) {
		this.permiteAlterarOrdemDesconto = permiteAlterarOrdemDesconto;
	}

	public boolean isPermiteAlteracaoDescontoProgressivoPrimeiraParcela() {
		return permiteAlteracaoDescontoProgressivoPrimeiraParcela;
	}

	public void setPermiteAlteracaoDescontoProgressivoPrimeiraParcela(boolean permiteAlteracaoDescontoProgressivoPrimeiraParcela) {
		this.permiteAlteracaoDescontoProgressivoPrimeiraParcela = permiteAlteracaoDescontoProgressivoPrimeiraParcela;
	}

	public ItemPlanoFinanceiroAlunoVO getItemPlanoFinanceiroAlunoVO() {
		if (itemPlanoFinanceiroAlunoVO == null) {
			itemPlanoFinanceiroAlunoVO = new ItemPlanoFinanceiroAlunoVO();
		}
		return itemPlanoFinanceiroAlunoVO;
	}

	public void setItemPlanoFinanceiroAlunoVO(ItemPlanoFinanceiroAlunoVO itemPlanoFinanceiroAlunoVO) {
		this.itemPlanoFinanceiroAlunoVO = itemPlanoFinanceiroAlunoVO;
	}

	public List<ContaPagarVO> getContaPagarRestituicaoConvenioAlunoVOs() {
		if (contaPagarRestituicaoConvenioAlunoVOs == null) {
			contaPagarRestituicaoConvenioAlunoVOs = new ArrayList<ContaPagarVO>(0);
		}
		return contaPagarRestituicaoConvenioAlunoVOs;
	}

	public void setContaPagarRestituicaoConvenioAlunoVOs(List<ContaPagarVO> contaPagarRestituicaoConvenioAlunoVOs) {
		this.contaPagarRestituicaoConvenioAlunoVOs = contaPagarRestituicaoConvenioAlunoVOs;
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

	public List<UnidadeEnsinoCursoVO> getUnidadeEnsinoCursoVOs() {
		if (unidadeEnsinoCursoVOs == null) {
			unidadeEnsinoCursoVOs = new ArrayList<UnidadeEnsinoCursoVO>(0);
		}
		return unidadeEnsinoCursoVOs;
	}

	public void setUnidadeEnsinoCursoVOs(List<UnidadeEnsinoCursoVO> unidadeEnsinoCursoVOs) {
		this.unidadeEnsinoCursoVOs = unidadeEnsinoCursoVOs;
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

	public boolean isAbrirPainelAvisoContaReceber() {
		return abrirPainelAvisoContaReceber;
	}

	public void setAbrirPainelAvisoContaReceber(boolean abrirPainelAvisoContaReceber) {
		this.abrirPainelAvisoContaReceber = abrirPainelAvisoContaReceber;
	}

	public boolean isAbrirPainelParcelaExtra() {
		return abrirPainelParcelaExtra;
	}

	public void setAbrirPainelParcelaExtra(boolean abrirPainelParcelaExtra) {
		this.abrirPainelParcelaExtra = abrirPainelParcelaExtra;
	}
	
	public String getUsername() {
		if (username == null) {
			username = "";
		}
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getSenha() {
		if (senha == null) {
			senha = "";
		}
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean isAbrirPainelDigitarSenhaLiberacaoDescontoAluno() {
		return abrirPainelDigitarSenhaLiberacaoDescontoAluno;
	}

	public void setAbrirPainelDigitarSenhaLiberacaoDescontoAluno(boolean abrirPainelDigitarSenhaLiberacaoDescontoAluno) {
		this.abrirPainelDigitarSenhaLiberacaoDescontoAluno = abrirPainelDigitarSenhaLiberacaoDescontoAluno;
	}

	public boolean isAbrirPainelDescontoAluno() {
		return abrirPainelDescontoAluno;
	}

	public void setAbrirPainelDescontoAluno(boolean abrirPainelDescontoAluno) {
		this.abrirPainelDescontoAluno = abrirPainelDescontoAluno;
	}

	public String getTipoDescontoMatricula() {
		if (tipoDescontoMatricula == null) {
			tipoDescontoMatricula = "";
		}
		return tipoDescontoMatricula;
	}

	public void setTipoDescontoMatricula(String tipoDescontoMatricula) {
		this.tipoDescontoMatricula = tipoDescontoMatricula;
	}

	public String getTipoDescontoParcela() {
		if (tipoDescontoParcela == null) {
			tipoDescontoParcela = "";
		}
		return tipoDescontoParcela;
	}

	public void setTipoDescontoParcela(String tipoDescontoParcela) {
		this.tipoDescontoParcela = tipoDescontoParcela;
	}

	public boolean isAlterado() {
		return alterado;
	}

	public void setAlterado(boolean alterado) {
		this.alterado = alterado;
	}

	public List<PlanoFinanceiroAlunoLogVO> getPlanoFinanceiroAlunoLogVOs() {
		if (planoFinanceiroAlunoLogVOs == null) {
			planoFinanceiroAlunoLogVOs = new ArrayList<PlanoFinanceiroAlunoLogVO>(0);
		}
		return planoFinanceiroAlunoLogVOs;
	}

	public void setPlanoFinanceiroAlunoLogVOs(List<PlanoFinanceiroAlunoLogVO> planoFinanceiroAlunoLogVOs) {
		this.planoFinanceiroAlunoLogVOs = planoFinanceiroAlunoLogVOs;
	}

	public boolean isPossuiContaPagarRestituicaoConvenioAlunoBaixada() {
		for (ContaPagarVO cpVO : getContaPagarRestituicaoConvenioAlunoVOs()) {
			if (cpVO.getQuitada())
				return true;
		}
		return false;
	}

	public void removerConvenioComRestituicoesJaExistentesAlunos() {
		try {
			getMatriculaPeriodoVO().getListaConvenioRemoverContaPagarRestituicaoAluno().add(getItemPlanoFinanceiroAlunoVO().getConvenio());
			getMatriculaPeriodoVO().getMatriculaVO().getPlanoFinanceiroAluno().excluirObjItemPlanoFinanceiroAlunoConvenioVOs(getItemPlanoFinanceiroAlunoVO().getConvenio().getCodigo());
			montarItemPlanoFinanceiroAluno();
			calcularValorTotalDesconto();
			setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void fecharPanelContaPagarRestituir() {
		setItemPlanoFinanceiroAlunoVO(new ItemPlanoFinanceiroAlunoVO());
	}

	public void realizarValidacaoRemocaoItemPlanoFinanceiroAluno(){
		try{
			setDirecionamentoListaContaPagarRestituicaoConvenioAluno("");
			limparMensagem();
			ItemPlanoFinanceiroAlunoVO obj = (ItemPlanoFinanceiroAlunoVO) context().getExternalContext().getRequestMap().get("itemPlanoFinanceiroAlunoItem");
			setItemPlanoFinanceiroAlunoVO(obj);
			if (!getFacadeFactory().getContaReceberFacade().consultarExistenciaContaReceberConvenioRecebidaNegociadaPorMatriculaPeriodoEConvenio(getMatriculaPeriodoVO().getMatricula(), getMatriculaPeriodoVO().getCodigo(), obj.getConvenio().getCodigo())) {
				removerConvenioPlanoFinanceiroAlunoVerificandoRestituicao();
			}else{
				setDirecionamentoListaContaPagarRestituicaoConvenioAluno("RichFaces.$('panelRemoverConvenio').show()");
			}
		}catch(Exception e){
			setItemPlanoFinanceiroAlunoVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private String direcionamentoListaContaPagarRestituicaoConvenioAluno;
	
	/**
	 * @return the direcionamentoListaContaPagarRestituicaoConvenioAluno
	 */
	public String getDirecionamentoListaContaPagarRestituicaoConvenioAluno() {
		if (direcionamentoListaContaPagarRestituicaoConvenioAluno == null) {
			direcionamentoListaContaPagarRestituicaoConvenioAluno = "";
		}
		return direcionamentoListaContaPagarRestituicaoConvenioAluno;
	}

	/**
	 * @param direcionamentoListaContaPagarRestituicaoConvenioAluno
	 *            the direcionamentoListaContaPagarRestituicaoConvenioAluno to
	 *            set
	 */
	public void setDirecionamentoListaContaPagarRestituicaoConvenioAluno(String direcionamentoListaContaPagarRestituicaoConvenioAluno) {
		this.direcionamentoListaContaPagarRestituicaoConvenioAluno = direcionamentoListaContaPagarRestituicaoConvenioAluno;
	}
	
	
	public void montarCampoConvenioPorParceiro() {
		try {
			setDebitoFinanceiroAoIncluirConvenioLiberado(false);
			getItemPlanoFinanceiroAlunoVO().setConvenio(new ConvenioVO());
			setConvenioVO(new ConvenioVO());
			montarListaSelectItemConvenio();
			inicializarMensagemVazia();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public ConvenioVO getConvenioVO() {
		if (convenioVO == null) {
			convenioVO = new ConvenioVO();
		}
		return convenioVO;
	}

	public void setConvenioVO(ConvenioVO convenioVO) {
		this.convenioVO = convenioVO;
	}
	
	public ParceiroVO getParceiroVO() {
		if (parceiroVO == null) {
			parceiroVO = new ParceiroVO();
		}
		return parceiroVO;
	}

	public void setParceiroVO(ParceiroVO parceiroVO) {
		this.parceiroVO = parceiroVO;
	}

	public List<SelectItem> getListaSelectItemParceiro() {
		if (listaSelectItemParceiro == null) {
			listaSelectItemParceiro = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemParceiro;
	}

	public void setListaSelectItemParceiro(List<SelectItem> listaSelectItemParceiro) {
		this.listaSelectItemParceiro = listaSelectItemParceiro;
	}
	
	public void novoConvenio() {
		try {
			if(isHabilitarRecursoValidacaoDebitoInclusaoConvenio()
	        		&& getFacadeFactory().getParceiroFacade().realizarVerificacaoDebitoFinanceiroAoIncluirConvenioMatricula(getParceiroVO().getCodigo())
	        		&& !isDebitoFinanceiroAoIncluirConvenioLiberado()) {
				setManterModalConvenioAberto("RichFaces.$('panelHabilitarRecursoValidacaoDebitoInclusaoConvenioNovo').show()");
				setUsernameLiberarOperacaoFuncionalidade("");
				setSenhaLiberarOperacaoFuncionalidade("");
				inicializarMensagemVazia();
			}else {
				novoConvenioLiberado();
			}
		} catch (Exception e) {
			setManterModalConvenioAberto("");
			setConvenioVO(new ConvenioVO());
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	private void novoConvenioLiberado() throws Exception {
		setConvenioVO(getFacadeFactory().getConvenioFacade().consultarConvenioPadrao(getParceiroVO(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		getConvenioVO().setCodigo(0);
		getConvenioVO().setDescricao("");
		getConvenioVO().setAtivo(false);
		getConvenioVO().setDataAssinatura(new Date());
		getConvenioVO().setDataRequisicao(new Date());
		getConvenioVO().setConvenioTipoPadrao(false);
		getConvenioVO().setConvenioUnidadeEnsinoVOs(new ArrayList<>());
		getConvenioVO().setConvenioTurnoVOs(new ArrayList<>());
		getConvenioVO().setConvenioCursoVOs(new ArrayList<>());
		inicializarFuncionarioRequisitante();
		setManterModalConvenioAberto("RichFaces.$('panelAdicionarConvenio').show()");
		setMensagemID("msg_entre_dados");
	}
	
	public void realizarVerificacaoUsuarioLiberacaoInclusaoDebitoFinanceiroConvenioNovo() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberarOperacaoFuncionalidade(), this.getSenhaLiberarOperacaoFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoAcademicoEnum.MATRICULA_LIBERAR_INCLUSAO_CONVENIO_DEBITO_FINANCEIRO, usuarioVerif);
			novoConvenioLiberado();
			OperacaoFuncionalidadeVO of = getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.PARCEIRO, getParceiroVO().getCodigo().toString(), OperacaoFuncionalidadeEnum.PARCEIRO_LIBERAR_INCLUSAO_CONVENIO_DEBITO_FINANCEIRO, usuarioVerif, "");
			getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(of);
			setManterModalConvenioAberto("RichFaces.$('panelHabilitarRecursoValidacaoDebitoInclusaoConvenioNovo').hide();RichFaces.$('panelAdicionarConvenio').show();");
			setDebitoFinanceiroAoIncluirConvenioLiberado(true);
			setUsernameLiberarOperacaoFuncionalidade("");
			setSenhaLiberarOperacaoFuncionalidade("");
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());			
		} catch (Exception e) {
			setManterModalConvenioAberto("");
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void inicializarFuncionarioRequisitante() throws Exception {
		try {
			getConvenioVO().setRequisitante(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCodigoPessoa(getUsuarioLogado().getPessoa().getCodigo(), false, getUsuarioLogado()));
		} catch (Exception e) {
			//throw new Exception("A configuração do seu funcionário não permite que você cadastre um novo convênio.");
		}
	}
	
	public void adicionarNovoConvenioItemPlanoFinanceiro() throws Exception {
		try {
			getFacadeFactory().getConvenioFacade().realizarPersistenciaPorRenovacaoMatricula(getConvenioVO(), getParceiroVO(), getUsuarioLogado());
			getListaSelectItemConvenio().add(new SelectItem(getConvenioVO().getCodigo(), getConvenioVO().getDescricao()));
			getItemPlanoFinanceiroAlunoVO().setConvenio(getConvenioVO());
			adicionarItemPlanoFinanceiroAlunoConvenio();			
			setManterModalConvenioAberto("RichFaces.$('panelAdicionarConvenio').hide()");
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			setManterModalConvenioAberto("RichFaces.$('panelAdicionarConvenio').show()");
		}
	}

	public void atualizarDescontoConvenio() {
		getConvenioVO().setTipoDescontoMatricula(this.getConvenioVO().getTipoBolsaCusteadaParceiroMatricula());
		getConvenioVO().setDescontoMatricula(this.getConvenioVO().getBolsaCusteadaParceiroMatricula());
		getConvenioVO().setTipoDescontoParcela(this.getConvenioVO().getTipoBolsaCusteadaParceiroParcela());
		getConvenioVO().setDescontoParcela(this.getConvenioVO().getBolsaCusteadaParceiroParcela());
	}
	
	public void atualizarAplicarSobreValorBaseDeduzidoValorOutrosConvenios() {
		if (getConvenioVO().getAplicarSobreValorCheio()) {
			getConvenioVO().setAplicarSobreValorBaseDeduzidoValorOutrosConvenios(Boolean.FALSE);
		}
	}

	public void montarListasDescontosProgressivos() {
		List<DescontoProgressivoVO> descontoProgressivoVOs = new ArrayList<DescontoProgressivoVO>(0);
		try {
			montarListaSelectItemDescontoProgressivoParceiro(descontoProgressivoVOs);
			montarListaSelectItemDescontoProgressivoAluno(descontoProgressivoVOs);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			descontoProgressivoVOs = null;
		}
	}

	public void montarListaSelectItemDescontoProgressivoParceiro(List<DescontoProgressivoVO> descontoProgressivoVOs) throws Exception {
		descontoProgressivoVOs = getFacadeFactory().getDescontoProgressivoFacade().consultarPorNomeAtivos("", false, getUsuarioLogado());
		setListaSelectItemDescontoProgressivoParceiro(UtilSelectItem.getListaSelectItem(descontoProgressivoVOs, "codigo", "nome"));
	}

	public void montarListaSelectItemDescontoProgressivoAluno(List<DescontoProgressivoVO> descontoProgressivoVOs) throws Exception {
		descontoProgressivoVOs = getFacadeFactory().getDescontoProgressivoFacade().consultarPorNomeAtivos("", false, getUsuarioLogado());
		setListaSelectItemDescontoProgressivoAluno(UtilSelectItem.getListaSelectItem(descontoProgressivoVOs, "codigo", "nome"));
	}

	public List<SelectItem> listaSelectItemDescontoProgressivoParceiro;
	public List<SelectItem> getListaSelectItemDescontoProgressivoParceiro() {
		if (listaSelectItemDescontoProgressivoParceiro == null) {
			listaSelectItemDescontoProgressivoParceiro = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDescontoProgressivoParceiro;
	}

	public void setListaSelectItemDescontoProgressivoParceiro(List<SelectItem> listaSelectItemDescontoProgressivoParceiro) {
		this.listaSelectItemDescontoProgressivoParceiro = listaSelectItemDescontoProgressivoParceiro;
	}

	public List<SelectItem> listaSelectItemDescontoProgressivoAluno;
	public List<SelectItem> getListaSelectItemDescontoProgressivoAluno() {
		if (listaSelectItemDescontoProgressivoAluno == null) {
			listaSelectItemDescontoProgressivoAluno = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDescontoProgressivoAluno;
	}

	public void setListaSelectItemDescontoProgressivoAluno(List<SelectItem> listaSelectItemDescontoProgressivoAluno) {
		this.listaSelectItemDescontoProgressivoAluno = listaSelectItemDescontoProgressivoAluno;
	}

	public void montarListaSelectItemCentroReceita() {
		List<CentroReceitaVO> resultadoConsulta = null;
		Iterator<CentroReceitaVO> i = null;
		try {
			resultadoConsulta = getFacadeFactory().getCentroReceitaFacade().consultarPorDescricao("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CentroReceitaVO obj = (CentroReceitaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString() + "-" + obj.getIdentificadorCentroReceita().toString()));
			}
			setListaSelectItemCentroReceita(objs);
		} catch (Exception e) {
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List<SelectItem> listaSelectItemCentroReceita;
	public List<SelectItem> getListaSelectItemCentroReceita() {
		if (listaSelectItemCentroReceita == null) {
			listaSelectItemCentroReceita = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemCentroReceita;
	}

	/**
	 * @param listaSelectItemCentroReceita
	 *            the listaSelectItemCentroReceita to set
	 */
	public void setListaSelectItemCentroReceita(List<SelectItem> listaSelectItemCentroReceita) {
		this.listaSelectItemCentroReceita = listaSelectItemCentroReceita;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemFormaPagamento(String prm) throws Exception {
		List<FormaPagamentoVO> resultadoConsulta = null;
		Iterator<FormaPagamentoVO> i = null;
		try {
			resultadoConsulta = consultarFormaPagamentoPorNome(prm);
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				FormaPagamentoVO obj = (FormaPagamentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			Uteis.liberarListaMemoria(resultadoConsulta);
			setListaSelectItemFormaPagamento(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>FormaPagamento</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>FormaPagamento</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemFormaPagamento() {
		try {
			montarListaSelectItemFormaPagamento("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List<FormaPagamentoVO> consultarFormaPagamentoPorNome(String nomePrm) throws Exception {
		List<FormaPagamentoVO> lista = getFacadeFactory().getFormaPagamentoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * @return the listaSelectItemFormaPagamento
	 */
	public List<SelectItem> listaSelectItemFormaPagamento;
	public List<SelectItem> getListaSelectItemFormaPagamento() {
		if (listaSelectItemFormaPagamento == null) {
			listaSelectItemFormaPagamento = new ArrayList<SelectItem>();
		}
		return listaSelectItemFormaPagamento;
	}

	/**
	 * @param listaSelectItemFormaPagamento
	 *            the listaSelectItemFormaPagamento to set
	 */
	public void setListaSelectItemFormaPagamento(List<SelectItem> listaSelectItemFormaPagamento) {
		this.listaSelectItemFormaPagamento = listaSelectItemFormaPagamento;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo
	 * <code>tipoBolsaCusteadaParceiroParcela</code>
	 */
	public List<SelectItem> listaSelectItemTipoBolsaCusteadaParceiroParcelaConvenio;
	@SuppressWarnings("unchecked")
	public List<SelectItem> getListaSelectItemTipoBolsaCusteadaParceiroParcelaConvenio() throws Exception {
		if(listaSelectItemTipoBolsaCusteadaParceiroParcelaConvenio == null){
			listaSelectItemTipoBolsaCusteadaParceiroParcelaConvenio = new ArrayList<SelectItem>(0);
			listaSelectItemTipoBolsaCusteadaParceiroParcelaConvenio.add(new SelectItem("", ""));
			Hashtable<String, String> tipoValorConvenios = (Hashtable<String, String>) Dominios.getTipoValorConvenio();
			Enumeration<String> keys = tipoValorConvenios.keys();
			while (keys.hasMoreElements()) {
				String value = (String) keys.nextElement();
				String label = (String) tipoValorConvenios.get(value);
				listaSelectItemTipoBolsaCusteadaParceiroParcelaConvenio.add(new SelectItem(value, label));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort((List<SelectItem>) listaSelectItemTipoBolsaCusteadaParceiroParcelaConvenio, ordenador);
		}
		return listaSelectItemTipoBolsaCusteadaParceiroParcelaConvenio;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>cobertura</code>
	 */
	public List<SelectItem> listaSelectItemCoberturaConvenio;		
	public List<SelectItem> getListaSelectItemCoberturaConvenio() throws Exception {
		if(listaSelectItemCoberturaConvenio == null){
			listaSelectItemCoberturaConvenio =  UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoCoberturaConvenio.class);
		}
		return listaSelectItemCoberturaConvenio;
	}

	public List<SelectItem> listaSelectItemTipoFinanciamentoEstudantil;
	public List<SelectItem> getListaSelectItemTipoFinanciamentoEstudantil() {
		if(listaSelectItemTipoFinanciamentoEstudantil== null){
			listaSelectItemTipoFinanciamentoEstudantil=  UtilPropriedadesDoEnum.getListaSelectItemDoEnum(FinanciamentoEstudantil.class, "valor", "descricao", false);
		}
		return listaSelectItemTipoFinanciamentoEstudantil;
	}

	public boolean isApresentarBotaoNovoConvenio() {
		return getParceiroVO().getCodigo() != null && getParceiroVO().getCodigo() != 0 ? true : false;
	}

	/**
	 * @return the listaConsultaCentroDespesa
	 */
	public List<CategoriaDespesaVO> listaConsultaCentroDespesa;
	public List<CategoriaDespesaVO> getListaConsultaCentroDespesa() {
		if (listaConsultaCentroDespesa == null) {
			listaConsultaCentroDespesa = new ArrayList<CategoriaDespesaVO>(0);
		}
		return listaConsultaCentroDespesa;
	}

	/**
	 * @param listaConsultaCentroDespesa
	 *            the listaConsultaCentroDespesa to set
	 */
	public void setListaConsultaCentroDespesa(List<CategoriaDespesaVO> listaConsultaCentroDespesa) {
		this.listaConsultaCentroDespesa = listaConsultaCentroDespesa;
	}

	/**
	 * @return the valorConsultaCentroDespesa
	 */
	public String valorConsultaCentroDespesa;
	public String getValorConsultaCentroDespesa() {
		if (valorConsultaCentroDespesa == null) {
			valorConsultaCentroDespesa = "";
		}
		return valorConsultaCentroDespesa;
	}

	/**
	 * @param valorConsultaCentroDespesa
	 *            the valorConsultaCentroDespesa to set
	 */
	public void setValorConsultaCentroDespesa(String valorConsultaCentroDespesa) {
		this.valorConsultaCentroDespesa = valorConsultaCentroDespesa;
	}

	/**
	 * @return the campoConsultaCentroDespesa
	 */
	public String campoConsultaCentroDespesa;
	public String getCampoConsultaCentroDespesa() {
		if (campoConsultaCentroDespesa == null) {
			campoConsultaCentroDespesa = "";
		}
		return campoConsultaCentroDespesa;
	}

	/**
	 * @param campoConsultaCentroDespesa
	 *            the campoConsultaCentroDespesa to set
	 */
	public void setCampoConsultaCentroDespesa(String campoConsultaCentroDespesa) {
		this.campoConsultaCentroDespesa = campoConsultaCentroDespesa;
	}

	public void consultarCentroDespesa() {
		try {
			List<CategoriaDespesaVO> objs = new ArrayList<CategoriaDespesaVO>(0);
			if (getCampoConsultaCentroDespesa().equals("descricao")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaCentroDespesa().equals("identificadorCentroDespesa")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaCentroDespesa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCentroDespesa(new ArrayList<CategoriaDespesaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> tipoConsultaComboCentroDespesa;
	public List<SelectItem> getTipoConsultaComboCentroDespesa() {
		if(tipoConsultaComboCentroDespesa == null){
			tipoConsultaComboCentroDespesa = new ArrayList<SelectItem>(0);
			tipoConsultaComboCentroDespesa.add(new SelectItem("descricao", "Descrição"));
			tipoConsultaComboCentroDespesa.add(new SelectItem("identificadorCentroDespesa", "Identificador Centro Despesa"));
		}
		return tipoConsultaComboCentroDespesa;
	}

	public void limparDadoscategoriaDespesaRestituicaoConvenio() {
		getConvenioVO().setCategoriaDespesaRestituicaoConvenio(null);
		getConvenioVO().setCategoriaDespesaRestituicaoConvenio(new CategoriaDespesaVO());
	}

	public void selecionarCentroDespesa() {
		CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("centroDespesa");
		getConvenioVO().setCategoriaDespesaRestituicaoConvenio(obj);
	}

	public String manterModalConvenioAberto;
	public String getManterModalConvenioAberto() {
		if (manterModalConvenioAberto == null) {
			manterModalConvenioAberto = "";
		}
		return manterModalConvenioAberto;
	}

	public void setManterModalConvenioAberto(String manterModalConvenioAberto) {
		this.manterModalConvenioAberto = manterModalConvenioAberto;
	}
	
	private Boolean apresentarMensagemPlanoAlteradoConvenioParcelaRecebida;

	/**
	 * @return the apresentarMensagemPlanoAlteradoConvenioParcelaRecebida
	 */
	public Boolean getApresentarMensagemPlanoAlteradoConvenioParcelaRecebida() {
		if (apresentarMensagemPlanoAlteradoConvenioParcelaRecebida == null) {
			apresentarMensagemPlanoAlteradoConvenioParcelaRecebida = false;
		}
		return apresentarMensagemPlanoAlteradoConvenioParcelaRecebida;
	}

	/**
	 * @param apresentarMensagemPlanoAlteradoConvenioParcelaRecebida
	 *            the apresentarMensagemPlanoAlteradoConvenioParcelaRecebida to
	 *            set
	 */
	public void setApresentarMensagemPlanoAlteradoConvenioParcelaRecebida(Boolean apresentarMensagemPlanoAlteradoConvenioParcelaRecebida) {
		this.apresentarMensagemPlanoAlteradoConvenioParcelaRecebida = apresentarMensagemPlanoAlteradoConvenioParcelaRecebida;
	}
	
	private Double valorTotalParcelasInclusao;


	public Double getValorTotalParcelasInclusao() {
		if(valorTotalParcelasInclusao == null){
			valorTotalParcelasInclusao = 0.0;
		}
		return valorTotalParcelasInclusao;
	}

	public void setValorTotalParcelasInclusao(Double valorTotalParcelasInclusao) {
		this.valorTotalParcelasInclusao = valorTotalParcelasInclusao;
	}
	
	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Convenio</code>. Buscando todos os objetos correspondentes a
	 * entidade <code>Convenio</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da
	 * tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemParceiro() {
		try {
			montarListaSelectItemParceiro("");
		} catch (Exception e) {
			// System.out.println(e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Convenio</code>.
	 */
	public void montarListaSelectItemParceiro(String prm) throws Exception {
		List<ParceiroVO> resultadoConsulta = null;
		Iterator<ParceiroVO> i = null;
		try {
			resultadoConsulta = getFacadeFactory().getParceiroFacade().consultarPorNome(prm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ParceiroVO obj = (ParceiroVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemParceiro(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public Boolean getPermiteAlterarPlanoCondicaoFinanceiraCurso() {
		if (permiteAlterarPlanoCondicaoFinanceiraCurso == null)
			permiteAlterarPlanoCondicaoFinanceiraCurso = false;
		
		if(isPermiteAlteracaoCategoriaECondicaoPagamento())
			permiteAlterarPlanoCondicaoFinanceiraCurso = true;
		
		return permiteAlterarPlanoCondicaoFinanceiraCurso;
	}

	public void setPermiteAlterarPlanoCondicaoFinanceiraCurso(Boolean permiteAlterarPlanoCondicaoFinanceiraCurso) {
		this.permiteAlterarPlanoCondicaoFinanceiraCurso = permiteAlterarPlanoCondicaoFinanceiraCurso;
	}

	public List getListaSelectItemCategoriaPlanoFinanceiroCurso() {
		if (listaSelectItemCategoriaPlanoFinanceiroCurso == null)
			listaSelectItemCategoriaPlanoFinanceiroCurso = new ArrayList<>();
		return listaSelectItemCategoriaPlanoFinanceiroCurso;
	}

	public void setListaSelectItemCategoriaPlanoFinanceiroCurso(List listaSelectItemCategoriaPlanoFinanceiroCurso) {
		this.listaSelectItemCategoriaPlanoFinanceiroCurso = listaSelectItemCategoriaPlanoFinanceiroCurso;
	}
	
	public List getListaSelectItemCondicaoPagamentoPlanoFinanceiro() {
		if (listaSelectItemCondicaoPagamentoPlanoFinanceiro == null)
			listaSelectItemCondicaoPagamentoPlanoFinanceiro = new ArrayList<>();
		return listaSelectItemCondicaoPagamentoPlanoFinanceiro;
	}

	public void setListaSelectItemCondicaoPagamentoPlanoFinanceiro(List listaSelectItemCondicaoPagamentoPlanoFinanceiro) {
		this.listaSelectItemCondicaoPagamentoPlanoFinanceiro = listaSelectItemCondicaoPagamentoPlanoFinanceiro;
	}

	
	public String getTipoOperacaoFuncionalidadeLiberar() {
		if (tipoOperacaoFuncionalidadeLiberar == null)
			tipoOperacaoFuncionalidadeLiberar = "";
		return tipoOperacaoFuncionalidadeLiberar;
	}

	public void setTipoOperacaoFuncionalidadeLiberar(String tipoOperacaoFuncionalidadeLiberar) {
		this.tipoOperacaoFuncionalidadeLiberar = tipoOperacaoFuncionalidadeLiberar;
	}

	public String getUsernameLiberarOperacaoFuncionalidade() {
		if (usernameLiberarOperacaoFuncionalidade == null)
			usernameLiberarOperacaoFuncionalidade = "";
		return usernameLiberarOperacaoFuncionalidade;
	}

	public void setUsernameLiberarOperacaoFuncionalidade(String usernameLiberarOperacaoFuncionalidade) {
		this.usernameLiberarOperacaoFuncionalidade = usernameLiberarOperacaoFuncionalidade;
	}

	public String getSenhaLiberarOperacaoFuncionalidade() {
		if (senhaLiberarOperacaoFuncionalidade == null)
			senhaLiberarOperacaoFuncionalidade = "";
		return senhaLiberarOperacaoFuncionalidade;
	}

	public void setSenhaLiberarOperacaoFuncionalidade(String senhaLiberarOperacaoFuncionalidade) {
		this.senhaLiberarOperacaoFuncionalidade = senhaLiberarOperacaoFuncionalidade;
	}

	
	public List<OperacaoFuncionalidadeVO> getOperacaoFuncionalidadePersistirVOs() {
		if (operacaoFuncionalidadePersistirVOs == null)
			operacaoFuncionalidadePersistirVOs = new ArrayList<OperacaoFuncionalidadeVO>();
		return operacaoFuncionalidadePersistirVOs;
	}

	public void setOperacaoFuncionalidadePersistirVOs(List<OperacaoFuncionalidadeVO> operacaoFuncionalidadePersistirVOs) {
		this.operacaoFuncionalidadePersistirVOs = operacaoFuncionalidadePersistirVOs;
	}

	
	
	public String getOncompleteOperacaoFuncionalidade() {
		if (oncompleteOperacaoFuncionalidade == null)
			oncompleteOperacaoFuncionalidade = "";
		return oncompleteOperacaoFuncionalidade;
	}

	public void setOncompleteOperacaoFuncionalidade(String oncompleteOperacaoFuncionalidade) {
		this.oncompleteOperacaoFuncionalidade = oncompleteOperacaoFuncionalidade;
	}

	/**
	 * Vai no FACADE da condicao pagamento do plano financeiro curso e consulta as categorias do Plano Financeiro Curso
	 * 
	 * @return
	 * @throws Exception
	 */
	public List<String> consultarCategoriaDasCondicoesDePagamentoDoPlanoFinanceiroCurso() throws Exception {
		return getFacadeFactory().getCondicaoPagamentoPlanoFinanceiroCursoFacade().consultarCategoriaDasCondicoesDePagamentoDoPlanoFinanceiroCurso(getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getCodigo(), false, getUsuarioLogado());
	}
	
	/**
	 * Montar a lista para exibir as categorias do plano financeiro
	 * 
	 * @throws Exception
	 */
	public void montarListaSelectItemCategoriasDoPlanoFinanceiroCurso() throws Exception {
		setListaSelectItemCategoriaPlanoFinanceiroCurso(consultarCategoriaDasCondicoesDePagamentoDoPlanoFinanceiroCurso());
	}

	public List<CondicaoPagamentoPlanoFinanceiroCursoVO> consultarCondicoesDePagamentoDoPlanoFinanceiro() throws Exception {
		return getFacadeFactory().getMatriculaPeriodoFacade().executarMontagemComboCondicaoPagamentoPlanoFinanceiroCurso(getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getCodigo(), getMatriculaPeriodoVO().getCategoriaCondicaoPagamento(), getMatriculaPeriodoVO().getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado());
	}
	
	public void montarListaSelectItemCondicoesPagamentoDoPlanoFinanceiroCurso() throws Exception {
		setListaSelectItemCondicaoPagamentoPlanoFinanceiro(UtilSelectItem.getListaSelectItem(consultarCondicoesDePagamentoDoPlanoFinanceiro(), "codigo", "descricao", false));
	}
	
	public void carregarListaComAsOpcoesdoPlanoFinanceiro() {
		try {
			montarListaSelectItemCategoriasDoPlanoFinanceiroCurso();
			montarListaSelectItemCondicoesPagamentoDoPlanoFinanceiroCurso();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void montarListaSelectItemCondicoesPagamentoCategoriaAlterado() {
		try {
			if(Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getCategoriaCondicaoPagamento())){
				setListaSelectItemCondicaoPagamentoPlanoFinanceiro(UtilSelectItem.getListaSelectItem(consultarCondicoesDePagamentoDoPlanoFinanceiro(), "codigo", "descricao", true));
			}else{
				getListaSelectItemCondicaoPagamentoPlanoFinanceiro().clear();	
			}
			getMatriculaPeriodoVO().setMatriculaPeriodoMatriculaCalculadaVO(new MatriculaPeriodoMensalidadeCalculadaVO());
			getMatriculaPeriodoVO().getMatriculaPeriodoMaterialDidaticoCalculadaVOs().clear();
			getMatriculaPeriodoVO().getMatriculaPeriodoMensalidadeCalculadaVOs().clear();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void recalcularValoresDaMatriculaEDasParcelas() {
		try {
			if(Uteis.isAtributoPreenchido(getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso())){
				getFacadeFactory().getMatriculaFacade().inicializarPlanoFinanceiroMatriculaPeriodo(getMatriculaPeriodoVO().getMatriculaVO(), getMatriculaPeriodoVO(), getUsuarioLogado());
				getMatriculaPeriodoVO().getMatriculaVO().setPlanoFinanceiroAluno(getFacadeFactory().getPlanoFinanceiroAlunoFacade().consultarPorMatriculaPeriodo(getMatriculaPeriodoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				if (!getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getMatriculaPeriodoVO().getMatriculaVO().getUnidadeEnsino().getCodigo()).getRealizarMatriculaComFinanceiroManualAtivo()) {
					realizarCalculoValoresMatriculaMensalidadePeriodoAluno();
				}
				montarListaSelectItemParceiro();
				montarListaSelectItemFormaPagamento();	
			}else{
				getMatriculaPeriodoVO().setMatriculaPeriodoMatriculaCalculadaVO(new MatriculaPeriodoMensalidadeCalculadaVO());
				getMatriculaPeriodoVO().getMatriculaPeriodoMaterialDidaticoCalculadaVOs().clear();
				getMatriculaPeriodoVO().getMatriculaPeriodoMensalidadeCalculadaVOs().clear();
			}		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void validarLiberarOperacaoFuncionalidade() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberarOperacaoFuncionalidade(), this.getSenhaLiberarOperacaoFuncionalidade(), true, Uteis.NIVELMONTARDADOS_TODOS);
			if (getTipoOperacaoFuncionalidadeLiberar().equals("AlteracaoPlanoFinanceiroAluno_AlterarCategoriaECondicaoPagamento")) {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("AlteracaoPlanoFinanceiroAluno_AlterarCategoriaECondicaoPagamento", usuarioVerif);
				setPermiteAlterarPlanoCondicaoFinanceiraCurso(true);
				getOperacaoFuncionalidadePersistirVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.MATRICULA_PERIODO, getMatriculaPeriodoVO().getMatricula(), OperacaoFuncionalidadeEnum.ALTERAR_PLANO_FINANCEIRO_CATEGORIA_CONDICAO_PAGAMENTO, usuarioVerif, ""));
			}
			setMensagemID("msg_funcionalidadeLiberadaComSucesso", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void prepararLiberarAlteracaoCategoriaCondicaoDePagamentoDoPlanoFinanceiroCurso() {
		setTipoOperacaoFuncionalidadeLiberar("AlteracaoPlanoFinanceiroAluno_AlterarCategoriaECondicaoPagamento");
	}
	
	public String getDescricaoPlanoCondicaoFinanceiraAlunoAluno() {
		return getMatriculaPeriodoVO().getPlanoFinanceiroCurso().getDescricao() + " - " + getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCurso().getDescricao();
	}

	public String getDescricaoPlanoCondicaoFinanceiraAlunoPersistidaParaAluno() {
		return getMatriculaPeriodoVO().getPlanoFinanceiroCursoPersistido().getDescricao() + " - " + getMatriculaPeriodoVO().getCondicaoPagamentoPlanoFinanceiroCursoPersistido().getDescricao();
	}
}