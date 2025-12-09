package controle.administrativo;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.ConfiguracaoDiplomaDigitalVO;
import negocio.comuns.academico.CursoTurnoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MaterialUnidadeEnsinoVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.academico.enumeradores.SituacaoPlanoFinanceiroCursoEnum;
import negocio.comuns.academico.enumeradores.TipoAutorizacaoCursoEnum;
import negocio.comuns.administrativo.AbastecimentoEnergiaEnum;
import negocio.comuns.administrativo.ConfiguracaoMobileVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoCentroResultadoVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoNivelEducacionalCentroResultadoVO;
import negocio.comuns.administrativo.UnidadeEnsinoTipoRequerimentoCentroResultadoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.VisaoVO;
import negocio.comuns.administrativo.enumeradores.AbastecimentoAguaEnum;
import negocio.comuns.administrativo.enumeradores.AguaConsumidaEnum;
import negocio.comuns.administrativo.enumeradores.CategoriaEscolaPrivadaEnum;
import negocio.comuns.administrativo.enumeradores.ConveniadaPoderPublicoEnum;
import negocio.comuns.administrativo.enumeradores.DependenciaAdministativaEnum;
import negocio.comuns.administrativo.enumeradores.DestinoLixoEnum;
import negocio.comuns.administrativo.enumeradores.EsgotoSanitarioEnum;
import negocio.comuns.administrativo.enumeradores.FormaOcupacaoPredioEnum;
import negocio.comuns.administrativo.enumeradores.LocalDeFuncionamentoEscolarEnum;
import negocio.comuns.administrativo.enumeradores.LocalizacaoDiferenciadaEscolaEnum;
import negocio.comuns.administrativo.enumeradores.LocalizacaoZonaEscolaEnum;
import negocio.comuns.administrativo.enumeradores.TratamentoLixoEnum;
import negocio.comuns.administrativo.enumeradores.UnidadeVinculadaEscolaEducacaoBasicaEnum;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.contabil.ConfiguracaoContabilVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.faturamento.nfe.ConfiguracaoNotaFiscalVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ChancelaVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.PerfilEconomicoVO;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilReflexao;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.administrativo.UnidadeEnsino;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas unidadeEnsinoForm.jsp unidadeEnsinoCons.jsp) com as funcionalidades
 * da classe <code>UnidadeEnsino</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see UnidadeEnsino
 * @see UnidadeEnsinoVO
 */

@Controller("UnidadeEnsinoControle")
@Scope("viewScope")
@Lazy
public class UnidadeEnsinoControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String TELA_FORM = "unidadeEnsinoForm.xhtml";
	private static final String TELA_CONS = "unidadeEnsinoCons.xhtml";
	private static final String CONTEXT_PARA_EDICAO = "unidadeEnsinoItens";
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<SelectItem> listaSelectItemTurno;
	private List<CursoVO> listaConsultaCurso;
	private List<FuncionarioVO> listaConsultaFuncionario;
	protected List<SelectItem> listaSelectItemPlanoFinanceiroCurso;
	protected List<SelectItem> listaSelectItemVisaoPadraoAluno;
	protected List<SelectItem> listaSelectItemVisaoPadraoProfessor;
	protected List<SelectItem> listaSelectItemVisaoPadraoCandidato;
	protected List<SelectItem> listaSelectItemPerfilPadraoAluno;
	protected List<SelectItem> listaSelectItemPerfilPadraoProfessor;
	protected List<SelectItem> listaSelectItemPerfilPadraoCandidato;
	protected List<SelectItem> listaSelectItemPerfilEconomico;
	protected List<SelectItem> listaSelectItemQuestionario;
	protected List<SelectItem> listaSelectItemConfiguracaoNotaFiscal;
	protected List<SelectItem> listaSelectItemConfiguracaoContabil;
	private List<SelectItem> listaSelectItemConfiguracoes;
	private List<SelectItem> listaSelectItemContaCorrente;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	protected String campoConsultaCidade;
	protected String valorConsultaCidade;
	protected String campoConsultaFuncionario;
	protected String valorConsultaFuncionario;
	protected List<CidadeVO> listaConsultaCidade;
	private DataModelo centroResultadoDataModelo;
	private enumFiltroConsultaCentroResultado filtroConsultaCentroResultado;
	private UnidadeEnsinoCursoCentroResultadoVO unidadeEnsinoCentroResultadoVO;
	private UnidadeEnsinoNivelEducacionalCentroResultadoVO unidadeEnsinoNivelEducacionalCentroResultadoVO;
	private UnidadeEnsinoTipoRequerimentoCentroResultadoVO unidadeEnsinoTipoRequerimentoCentroResultadoVO;
	
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoEditado;
	private Integer turnoUnidadeEnsinoCursoEditado;
	private String curso_Erro;
	private Boolean matriz;
	private MaterialUnidadeEnsinoVO materialUnidadeEnsino;

	private List<SelectItem> listaSelectItemDependenciaAdministrativa;
	private List<SelectItem> listaSelectItemLocalizacaoZonaEscola;
	private List<SelectItem> listaSelectItemLocalizacaoDiferenciadaEscola;
	private List<SelectItem> listaSelectItemUnidadeVinculadaEscolaEducacaoBasica;
	private List<SelectItem> listaSelectItemCategoriaEscolaPrivada;
	private List<SelectItem> listaSelectItemConveniadaPoderPublico;
	private Boolean apresentarCategoriaEscolaConveniadaPoderPublico;
	private List<SelectItem> listaSelectItemLocalFuncionamentoEscolar;
	private Boolean apresentarFormaOcupacaoPredio;
	private List<SelectItem> listaSelectItemFormaOcupacaoPredio;
	private List<SelectItem> listaSelectItemConsumoAgua;
	private List<SelectItem> listaSelectItemAbastecimentoAgua;
	private List<SelectItem> listaSelectItemAbastecimentoEnergia;	
	private List<SelectItem> listaSelectItemEsgotoSanitario;
	private List<SelectItem> listaSelectItemDestinoLixo;
	private List<SelectItem> listaSelectItemTratamentoLixo;
	private List<SelectItem> listaSelectItemTipoRequerimento;
	private Boolean usuarioDesbloqueioAutenticado;
	private String usernameDesbloqueioAutenticado;
	private String senhaDesbloqueioAutenticado;
	private String modalGravarUnidade;
	private List<SelectItem> listaSelectItemConfiguracaoGEDVOs;
	protected List<SelectItem> listaSelectItemConfiguracaoMobile;
	private List<SelectItem> listaSelectItemChancela;
	private Boolean campoValorPorAluno;
	private List<SelectItem> listaSelectItemConfiguracoesDiplomaDigital;
	private List listaSelectItemTipoAutorizacao;

	public UnidadeEnsinoControle() throws Exception {
		setMatriz(Boolean.FALSE);
		setCurso_Erro("");
		verificaExisteMatriz();
		setControleConsulta(new ControleConsulta());
		setListaSelectItemTurno(new ArrayList<SelectItem>(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
	}

	public void verificaExisteMatriz() throws Exception {
		UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultarSeExisteUnidadeMatriz(true, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		if (obj.getCodigo().equals(0)) {
			setMatriz(Boolean.FALSE);
		} else {
			setMatriz(Boolean.TRUE);
		}
	}
	
	public void changeConfiguracaoPadrao(ValueChangeEvent event){
		this.unidadeEnsinoVO.setUsarConfiguracaoPadrao((Boolean)event.getNewValue());
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>UnidadeEnsino</code> para edição pelo usuário da aplicação.
	 */
	public String novo() throws Exception {
		removerObjetoMemoria(this);
		setUnidadeEnsinoVO(new UnidadeEnsinoVO());
		unidadeEnsinoVO.setTipoEmpresa("JU");
		verificaExisteMatriz();
		setCurso_Erro("");
		setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
		setUnidadeEnsinoCursoEditado(new UnidadeEnsinoCursoVO());
		setUnidadeEnsinoNivelEducacionalCentroResultadoVO(new UnidadeEnsinoNivelEducacionalCentroResultadoVO());
		getUnidadeEnsinoNivelEducacionalCentroResultadoVO().setTipoNivelEducacional(TipoNivelEducacional.BASICO);
		setUnidadeEnsinoTipoRequerimentoCentroResultadoVO(new UnidadeEnsinoTipoRequerimentoCentroResultadoVO());
		inicializarListasSelectItemTodosComboBox();
		montarCategoriaEscolaPrivadaEConveniadaPoderPublico();
		montarFormaOcupacaoPredio();
		setMensagemID(MSG_TELA.msg_entre_dados.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}
	
	public void limparDadosPorcentagemEValor() throws Exception {
		getUnidadeEnsinoVO().setValorFixoChancela(Double.valueOf(0));
		getUnidadeEnsinoVO().setPorcentagemChancela(Double.valueOf(0));
		getUnidadeEnsinoVO().setValorPorAluno(Boolean.FALSE);
	}
	
	public void verificarCampoValorFixo() {
		getUnidadeEnsinoVO().setPorcentagemChancela(Double.valueOf(0));
		setCampoValorPorAluno(Boolean.FALSE);
	}

	public void verificarCampoPorcentagem() {
		getUnidadeEnsinoVO().setValorFixoChancela(Double.valueOf(0));
		setCampoValorPorAluno(Boolean.TRUE);
	}
	
	public boolean getIsApresentarDadosChancela() {
		return !getUnidadeEnsinoVO().getTipoChancela().equals("");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>UnidadeEnsino</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		try {
			UnidadeEnsinoVO obj = (UnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoItem");
			getFacadeFactory().getUnidadeEnsinoFacade().carregarDados(obj, getUsuarioLogado());
			obj.setNovoObj(Boolean.FALSE);
			setUnidadeEnsinoVO(obj);
			verificaExisteMatriz();
			inicializarListasSelectItemTodosComboBox();
			setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
			setUnidadeEnsinoCursoEditado(new UnidadeEnsinoCursoVO());
			setUnidadeEnsinoNivelEducacionalCentroResultadoVO(new UnidadeEnsinoNivelEducacionalCentroResultadoVO());
			getUnidadeEnsinoNivelEducacionalCentroResultadoVO().setTipoNivelEducacional(TipoNivelEducacional.BASICO);
			setUnidadeEnsinoTipoRequerimentoCentroResultadoVO(new UnidadeEnsinoTipoRequerimentoCentroResultadoVO());
			montarCategoriaEscolaPrivadaEConveniadaPoderPublico();
			montarFormaOcupacaoPredio();
			setCurso_Erro("");
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}
	
	public void montarListaSelectItemChancela() {
		List<ChancelaVO> lista = new ArrayList<ChancelaVO>(0);
		try {
			lista = getFacadeFactory().getChancelaFacade().consultarPorCodigo(0, false,
					Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			setListaSelectItemChancela(UtilSelectItem.getListaSelectItem(lista, "codigo", "instituicaoChanceladora"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			lista = null;
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>UnidadeEnsino</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public void gravar() {
		try {
			if (unidadeEnsinoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getUnidadeEnsinoFacade().incluir(unidadeEnsinoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			} else {
				getFacadeFactory().getUnidadeEnsinoFacade().alterar(unidadeEnsinoVO, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			}
			getAplicacaoControle().removerUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
			getAplicacaoControle().removerConfiguracaoGeralSistemaPorUnidadeEnsino(unidadeEnsinoVO.getCodigo());
			getAplicacaoControle().removerConfiguracaoFinanceiraNoMapConfiguracaoUnidadeEnsino(unidadeEnsinoVO.getCodigo());
			setCurso_Erro("");
			setMensagemID("msg_dados_gravados");

		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * UnidadeEnsinoCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("razaoSocial")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorRazaoSocial(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCidade")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNomeCidade(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("CNPJ")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCnpj(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("inscEstadual")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorInscEstatual(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("RG")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorRg(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("CPF")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCpf(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}

	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getCampoConsultaCurso().equals("codigo")) {
				if (getValorConsultaCurso().equals("")) {
					setValorConsultaCurso("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCurso());
				objs = getFacadeFactory().getCursoFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaCurso().equals("nome")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getValorConsultaCurso(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, Boolean.FALSE, getUsuarioLogado());
			}

			setListaConsultaCurso(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCidade() {
		try {
			getFacadeFactory().getCidadeFacade().consultarCidade(getControleConsultaCidade(), false, getUsuarioLogado());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList<CidadeVO>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarFuncionario() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarFuncionario() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
		getUnidadeEnsinoVO().setDiretorGeral(obj);
		listaConsultaFuncionario.clear();
		this.setValorConsultaFuncionario("");
		this.setCampoConsultaFuncionario("");
	}

	public void selecionarResponsavelCobrancaUnidade() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
		getUnidadeEnsinoVO().setResponsavelCobrancaUnidade(getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(obj.getPessoa().getCodigo(), false, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
		listaConsultaFuncionario.clear();
		this.setValorConsultaFuncionario("");
		this.setCampoConsultaFuncionario("");
	}
	
	
	public void selecionarResponsavelNotificacaoAlteracaoCronogramaAula()  {
		try {			
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
			getUnidadeEnsinoVO().setResponsavelNotificacaoAlteracaoCronogramaAula(obj);
			listaConsultaFuncionario.clear();
			this.setValorConsultaFuncionario("");
			this.setCampoConsultaFuncionario("");
		}catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void selecionarOrientadorPadraoEstagio()  {
		try {			
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
			getUnidadeEnsinoVO().setOrientadorPadraoEstagio(obj);
			listaConsultaFuncionario.clear();
			this.setValorConsultaFuncionario("");
			this.setCampoConsultaFuncionario("");
		}catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	public void selecionarOperadorResponsavel()  {
		try {			
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
			getUnidadeEnsinoVO().setOperadorResponsavel(obj);
			listaConsultaFuncionario.clear();
			this.setValorConsultaFuncionario("");
			this.setCampoConsultaFuncionario("");
		}catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCurso() {
		CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
		getUnidadeEnsinoCursoVO().setCurso(obj);
		getUnidadeEnsinoCursoVO().setCodigoInep(obj.getIdCursoInep());
		getUnidadeEnsinoCursoVO().setCodigoCursoUnidadeEnsinoGinfes(obj.getCodigo());
		montarListaSelectItemTurno();
		setListaConsultaCurso(null);
		this.setValorConsultaCurso("");
		this.setCampoConsultaCurso("");
	}
	
	
	public void selecionarCursoGinfes() {
		CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");		
		getUnidadeEnsinoCursoVO().setCodigoCursoUnidadeEnsinoGinfes(obj.getCodigo());
		
	}

	public void selecionarCidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItems");
		getUnidadeEnsinoVO().setCidade(obj);
		getListaConsultaCidade().clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>UnidadeEnsinoVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getUnidadeEnsinoFacade().excluir(unidadeEnsinoVO, getUsuarioLogado());
			setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
	}
	
	public void validarUnidadeEnsinoCursoUtilizadaMatricula() throws Exception {
			Uteis.checkState(Uteis.isAtributoPreenchido(getUnidadeEnsinoCursoVO().getCodigo()) 
					&& Uteis.isAtributoPreenchido(getUnidadeEnsinoCursoVO().getTurno().getCodigo()) 
					&& Uteis.isAtributoPreenchido(getTurnoUnidadeEnsinoCursoEditado()) 
					&& !getUnidadeEnsinoCursoVO().getTurno().getCodigo().equals(getTurnoUnidadeEnsinoCursoEditado()) 
					&& getFacadeFactory().getUnidadeEnsinoCursoFacade().validarUnidadeEnsinoCursoExistenteMatriculaPeriodo(getUnidadeEnsinoCursoVO(), true), "Já existe Matriculas para essa Unidade Ensino Curso, por isso não é possível alterar seu turno.");
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>UnidadeEnsinoCurso</code> para o objeto
	 * <code>unidadeEnsinoVO</code> da classe <code>UnidadeEnsino</code>
	 */
	public void adicionarUnidadeEnsinoCurso() throws Exception {
		try {
			if (!getUnidadeEnsinoVO().getCodigo().equals(0)) {
				unidadeEnsinoCursoVO.setUnidadeEnsino(getUnidadeEnsinoVO().getCodigo());
			}
			if (getUnidadeEnsinoCursoVO().getCurso().getCodigo().intValue() != 0) {
				Integer campoConsulta = getUnidadeEnsinoCursoVO().getCurso().getCodigo();
				CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
				getUnidadeEnsinoCursoVO().setCurso(curso);
			}
			if (getUnidadeEnsinoCursoVO().getTurno().getCodigo().intValue() != 0) {
				Integer campoTurno = getUnidadeEnsinoCursoVO().getTurno().getCodigo();
				TurnoVO turno = getFacadeFactory().getTurnoFacade().consultarPorChavePrimaria(campoTurno, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				getUnidadeEnsinoCursoVO().setTurno(turno);
			}
			validarUnidadeEnsinoCursoUtilizadaMatricula();
			getUnidadeEnsinoVO().adicionarObjUnidadeEnsinoCursoVOs(getUnidadeEnsinoCursoEditado(), getUnidadeEnsinoCursoVO());
			this.setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
			this.setUnidadeEnsinoCursoEditado(new UnidadeEnsinoCursoVO());
			this.listaSelectItemTurno.clear();
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO, true);
		} catch (Exception e) {
			if (!getUnidadeEnsinoCursoEditado().getCurso().getCodigo().equals(0)) {
				this.setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
			}
			this.setUnidadeEnsinoCursoEditado(new UnidadeEnsinoCursoVO());
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			//getUnidadeEnsinoCursoVO().getTurno().setCodigo(getTurnoUnidadeEnsinoCursoEditado());

		}
	}
	
	public void cancelarUnidadeEnsinoCurso() {
		setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
		this.setUnidadeEnsinoCursoEditado(new UnidadeEnsinoCursoVO());
		setMensagemID(MSG_TELA.msg_entre_dados.name());
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>UnidadeEnsinoCurso</code> para edição pelo usuário.
	 */
	public void editarUnidadeEnsinoCurso() throws Exception {
		UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItem");
		setUnidadeEnsinoCursoVO(obj.getClone());
		setTurnoUnidadeEnsinoCursoEditado(obj.getTurno().getCodigo());
		/*getUnidadeEnsinoCursoVO().getCurso().setCodigo(obj.getCurso().getCodigo());
		getUnidadeEnsinoCursoVO().getCurso().setNome(obj.getCurso().getNome());
		getUnidadeEnsinoCursoVO().setNrVagasPeriodoLetivo(obj.getNrVagasPeriodoLetivo());
		getUnidadeEnsinoCursoVO().setSituacaoCurso(obj.getSituacaoCurso());
		getUnidadeEnsinoCursoVO().setUnidadeEnsino(obj.getUnidadeEnsino());
		getUnidadeEnsinoCursoVO().getTurno().setCodigo(obj.getTurno().getCodigo());
		getUnidadeEnsinoCursoVO().setPlanoFinanceiroCurso(obj.getPlanoFinanceiroCurso());*/
		montarListaSelectItemTurno();
		montarListaSelectItemPlanoFinanceiroCurso(true);
		setCurso_Erro("");
		setUnidadeEnsinoCursoEditado(obj);
	}

	/*
	 * Método responsável por remover um novo objeto da classe
	 * <code>UnidadeEnsinoCurso</code> do objeto <code>unidadeEnsinoVO</code> da
	 * classe <code>UnidadeEnsino</code>
	 */
	public void removerUnidadeEnsinoCurso()  {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItem");
			Uteis.checkState(Uteis.isAtributoPreenchido(obj.getCodigo()) 
					&& getFacadeFactory().getUnidadeEnsinoCursoFacade().validarUnidadeEnsinoCursoExistenteMatriculaPeriodo(obj, false), "Não é possível remover essa Unidade Ensino Curso, pois a mesma esta sendo usado para matriculas.");
			getUnidadeEnsinoVO().excluirObjUnidadeEnsinoCursoVOs(obj.getCurso().getCodigo(), obj.getTurno().getCodigo());
			setCurso_Erro("");
			setMensagemID("msg_dados_excluidos");	
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
	}
	
	public List<SelectItem> getListaSelectItemTipoChancela() throws Exception {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("PC", "Paga Chancela"));
		itens.add(new SelectItem("RC", "Receber Chancela"));
		return itens;
	}

	public void irPaginaInicial() throws Exception {
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

	public Boolean getMatriz() {
		return matriz;
	}

	public void setMatriz(Boolean matriz) {
		this.matriz = matriz;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Turno</code>.
	 */
	public void montarListaSelectItemTurno(Integer prm) throws Exception {
		List<SelectItem> resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarTurnoPorCurso(prm);
//			setListaSelectItemTurno(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CursoTurnoVO obj = (CursoTurnoVO) i.next();
				objs.add(new SelectItem(obj.getTurno().getCodigo(), obj.getTurno().getNome()));
			}
			setListaSelectItemTurno(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Turno</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Turno</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemTurno() {
		try {
			montarListaSelectItemTurno(getUnidadeEnsinoCursoVO().getCurso().getCodigo());
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public List consultarPlanoFunanceiroCurso(String descricao, Boolean editandoUnidadeEnsinoCurso) throws Exception {
		if (editandoUnidadeEnsinoCurso) {
			return getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPlanoFinanceiroUnidadeEnsinoCursoEspecificaEPlanoFinanceiroAtivoUnidade(getUnidadeEnsinoCursoVO().getCodigo(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		} 
		return getFacadeFactory().getPlanoFinanceiroCursoFacade().consultarPorUnidadeEnsino(getUnidadeEnsinoVO().getCodigo(), SituacaoPlanoFinanceiroCursoEnum.ATIVO, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	public void montarListaSelectItemPlanoFinanceiroCurso(String prm, Boolean editandoUnidadeEnsinoCurso) throws Exception {
		List resultadoConsulta = consultarPlanoFunanceiroCurso(prm, editandoUnidadeEnsinoCurso);
		setListaSelectItemPlanoFinanceiroCurso(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));
	}

	public void montarListaSelectItemPlanoFinanceiroCurso(Boolean editandoUnidadeEnsinoCurso) {
		try {
			montarListaSelectItemPlanoFinanceiroCurso("", editandoUnidadeEnsinoCurso);
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarTurnoPorCurso(Integer curso) throws Exception {
		List lista = getFacadeFactory().getCursoTurnoFacade().consultarCursoTurnos(curso, false, getUsuarioLogado());
		return lista;
	}

	/**
	 * Operação responsável por processar a consulta pelo parâmetro informado
	 * pelo usuário.<code>CursoVO</code> Após a consulta ela automaticamente
	 * adciona o código e o nome da cidade na tela.
	 */
	public List consultarCursoSuggestionbox(Object event) {
		try {
			String valor = event.toString();
			List lista = getFacadeFactory().getCursoFacade().consultarPorNome(valor, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			return lista;
		} catch (Exception e) {
			return new ArrayList(0);
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Curso</code> por meio de sua respectiva chave primária. Esta rotina
	 * é utilizada fundamentalmente por requisições Ajax, que realizam busca
	 * pela chave primária da entidade montando automaticamente o resultado da
	 * consulta para apresentação.
	 */
	public void consultarCursoPorChavePrimaria() {
		try {
			Integer campoConsulta = unidadeEnsinoCursoVO.getCurso().getCodigo();
			CursoVO curso = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
			unidadeEnsinoCursoVO.setCurso(curso);
			montarListaSelectItemTurno();
			this.setCurso_Erro("");
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			unidadeEnsinoCursoVO.getCurso().setNome("");
			unidadeEnsinoCursoVO.getCurso().setCodigo(0);
			this.setCurso_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	public void montarListaSelectItensConfiguracoes() throws Exception {
		List configuracoes = getFacadeFactory().getConfiguracoesFacade().listarTodasConfiguracoes(false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		List lista = UtilSelectItem.getListaSelectItem(configuracoes, "codigo", "nome");
		setListaSelectItemConfiguracoes(lista);
	}

	public void montarListaSelectItensContaCorrente() throws Exception {
		// List<ContaCorrenteVO> listaContaCorrenteTemp =
		// getFacadeFactory().getContaCorrenteFacade().consultarRapidaContaCorrentePorTipo(false,
		// 0, getUsuarioLogado());
		// List lista =
		// UtilSelectItem.getListaSelectItem(listaContaCorrenteTemp, "codigo",
		// "numero");
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getContaCorrenteFacade().consultarRapidaContaCorrentePorTipo(false, false, this.getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ContaCorrenteVO obj = (ContaCorrenteVO) i.next();
				if (obj.getSituacao().equals("AT")) {
	            	if(Uteis.isAtributoPreenchido(obj.getNomeApresentacaoSistema())){
	            		objs.add(new SelectItem(obj.getCodigo(), obj.getNomeApresentacaoSistema()));
	            	} else if (obj.getContaCaixa()) {
						objs.add(new SelectItem(obj.getCodigo(), "CC:" + obj.getNumero() + "-" + obj.getDigito()));
					} else {
						objs.add(new SelectItem(obj.getCodigo(), "Banco:" + obj.getAgencia().getBanco().getNome() + " Ag:" + obj.getAgencia().getNumeroAgencia() + "-" + obj.getAgencia().getDigito() + " CC:" + obj.getNumero() + "-" + obj.getDigito() + "-" + obj.getCarteira()));
					}
				}
			}
			setListaSelectItemContaCorrente(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public Boolean getDesenharTurno() {
		if (getUnidadeEnsinoCursoVO().getCurso().getCodigo().intValue() != 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;

	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>situacaoCurso</code>
	 */
	public List getListaSelectItemSituacaoCursoUnidadeEnsinoCurso() throws Exception {
		List objs = new ArrayList(0);
		Hashtable situacaoCursoAcademicos = (Hashtable) Dominios.getSituacaoCursoAcademico();
		Enumeration keys = situacaoCursoAcademicos.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoCursoAcademicos.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>tipoEmpresa</code>
	 */
	public List getListaSelectItemTipoEmpresaUnidadeEnsino() throws Exception {
		List objs = new ArrayList(0);
		Hashtable tipoEmpresas = (Hashtable) Dominios.getTipoEmpresa();
		Enumeration keys = tipoEmpresas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) tipoEmpresas.get(value);
			objs.add(new SelectItem(value, label));
		}
		return objs;
	}

	public void carregarEnderecoPessoa() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEndereco(unidadeEnsinoVO, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>PerfilPadraoCandidato</code>.
	 */
	public void montarListaSelectItemPerfilPadraoCandidato(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarPerfilAcessoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PerfilAcessoVO obj = (PerfilAcessoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemPerfilPadraoCandidato(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>PerfilPadraoCandidato</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>PerfilAcesso</code>. Esta rotina não
	 * recebe parâmetros para filtragem de dados, isto é importante para a
	 * inicialização dos dados da tela para o acionamento por meio requisições
	 * Ajax.
	 */
	public void montarListaSelectItemPerfilPadraoCandidato() {
		try {
			montarListaSelectItemPerfilPadraoCandidato("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>PerfilPadraoProfessor</code>.
	 */
	public void montarListaSelectItemPerfilPadraoProfessor(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarPerfilAcessoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PerfilAcessoVO obj = (PerfilAcessoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemPerfilPadraoProfessor(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>PerfilPadraoProfessor</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>PerfilAcesso</code>. Esta rotina não
	 * recebe parâmetros para filtragem de dados, isto é importante para a
	 * inicialização dos dados da tela para o acionamento por meio requisições
	 * Ajax.
	 */
	public void montarListaSelectItemPerfilPadraoProfessor() {
		try {
			montarListaSelectItemPerfilPadraoProfessor("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>PerfilPadraoAluno</code>.
	 */
	public void montarListaSelectItemPerfilPadraoAluno(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarPerfilAcessoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PerfilAcessoVO obj = (PerfilAcessoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemPerfilPadraoAluno(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>PerfilPadraoAluno</code>. Buscando todos os objetos correspondentes
	 * a entidade <code>PerfilAcesso</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemPerfilPadraoAluno() {
		try {
			montarListaSelectItemPerfilPadraoAluno("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarPerfilAcessoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getPerfilAcessoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>VisaoPadraoCandidato</code>.
	 */
	public void montarListaSelectItemVisaoPadraoCandidato(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarVisaoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				VisaoVO obj = (VisaoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemVisaoPadraoCandidato(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>VisaoPadraoCandidato</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>Visao</code>. Esta rotina não recebe
	 * parâmetros para filtragem de dados, isto é importante para a
	 * inicialização dos dados da tela para o acionamento por meio requisições
	 * Ajax.
	 */
	public void montarListaSelectItemVisaoPadraoCandidato() {
		try {
			montarListaSelectItemVisaoPadraoCandidato("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>VisaoPadraoProfessor</code>.
	 */
	public void montarListaSelectItemVisaoPadraoProfessor(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarVisaoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				VisaoVO obj = (VisaoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemVisaoPadraoProfessor(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>VisaoPadraoProfessor</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>Visao</code>. Esta rotina não recebe
	 * parâmetros para filtragem de dados, isto é importante para a
	 * inicialização dos dados da tela para o acionamento por meio requisições
	 * Ajax.
	 */
	public void montarListaSelectItemVisaoPadraoProfessor() {
		try {
			montarListaSelectItemVisaoPadraoProfessor("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>VisaoPadraoAluno</code>.
	 */
	public void montarListaSelectItemVisaoPadraoAluno(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarVisaoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				VisaoVO obj = (VisaoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemVisaoPadraoAluno(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>VisaoPadraoAluno</code>. Buscando todos os objetos correspondentes
	 * a entidade <code>Visao</code>. Esta rotina não recebe parâmetros para
	 * filtragem de dados, isto é importante para a inicialização dos dados da
	 * tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemVisaoPadraoAluno() {
		try {
			montarListaSelectItemVisaoPadraoAluno("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}
	
	public void uploadArquivoLogoAplicativo(FileUploadEvent event) {
		try {
			getFacadeFactory().getUnidadeEnsinoFacade().upLoadLogoUnidadeEnsino(event, getUnidadeEnsinoVO(), "APLICATIVO", getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			
		}
	}

	public void uploadArquivoLogo(FileUploadEvent event) {
		try {
			getFacadeFactory().getUnidadeEnsinoFacade().upLoadLogoUnidadeEnsino(event, getUnidadeEnsinoVO(), "TOPO", getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}
	
	public void uploadArquivoLogoPaginaInicial(FileUploadEvent event) {
		try {
			getFacadeFactory().getUnidadeEnsinoFacade().upLoadLogoUnidadeEnsino(event, getUnidadeEnsinoVO(), "INICIAL", getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}
	
	public void uploadArquivoLogoEmailCima(FileUploadEvent event) {
		try {
			getFacadeFactory().getUnidadeEnsinoFacade().upLoadLogoUnidadeEnsino(event, getUnidadeEnsinoVO(), "EMAIL_CIMA", getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}
	
	public void uploadArquivoLogoEmailBaixo(FileUploadEvent event) {
		try {
			getFacadeFactory().getUnidadeEnsinoFacade().upLoadLogoUnidadeEnsino(event, getUnidadeEnsinoVO(), "EMAIL_BAIXO", getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}
	
	public void uploadArquivoLogoMunicipio(FileUploadEvent event) {
		try {
			getFacadeFactory().getUnidadeEnsinoFacade().upLoadLogoUnidadeEnsino(event, getUnidadeEnsinoVO(), "MUNICIPIO", getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			
		}
	}

	public void removerMaterialUnidadeEnsino() {
		try {
			MaterialUnidadeEnsinoVO obj = (MaterialUnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("materialUnidadeEnsinoVOItem");
			realizarExcluirMaterialUnidadeEnsino(obj, 0);
			if (getMaterialUnidadeEnsino().getCodigo().equals(obj.getCodigo())) {
				setMaterialUnidadeEnsino(new MaterialUnidadeEnsinoVO());
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarExcluirMaterialUnidadeEnsino(MaterialUnidadeEnsinoVO obj, Integer aux) throws Exception {
		Iterator i = getUnidadeEnsinoVO().getMaterialUnidadeEnsinoVOs().iterator();
		while (i.hasNext()) {
			MaterialUnidadeEnsinoVO vo = (MaterialUnidadeEnsinoVO) i.next();
			if (vo.equals(obj)) {
				// caso o vo escolhido para remover tenha codigo, entao deve ser
				// tbm deletado do banco
				if (!vo.isNovoObj() && aux.intValue() == 0) {
					getFacadeFactory().getMaterialUnidadeEnsinoFacade().excluir(vo);
				}
				i.remove();
				break;
			}
		}
	}

	public void adicionarMaterialUnidadeEnsino() throws Exception {
		try {
			// getFacadeFactory().getCursoFacade().validarReconhecimentoCurso(getAutorizacaoCurso(),
			// getCursoVO());
			if (getMaterialUnidadeEnsino().getDescricao().isEmpty()) {
				throw new Exception("O campo DESCRIÇÃO é obrigatório");
			}
			realizarExcluirMaterialUnidadeEnsino(getMaterialUnidadeEnsino(), 1);
			getUnidadeEnsinoVO().getMaterialUnidadeEnsinoVOs().add(getMaterialUnidadeEnsino());
			setMaterialUnidadeEnsino(new MaterialUnidadeEnsinoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void uploadArquivoLogoIndex(FileUploadEvent event) {
		try {
			getFacadeFactory().getUnidadeEnsinoFacade().upLoadLogoUnidadeEnsino(event, getUnidadeEnsinoVO(), "INDEX", getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}

	public void uploadArquivoLogoRelatorio(FileUploadEvent event) {
		try {
			getFacadeFactory().getUnidadeEnsinoFacade().upLoadLogoUnidadeEnsino(event, getUnidadeEnsinoVO(), "RELATORIO", getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarVisaoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getVisaoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Cidade</code>.
	 */
	public void montarListaSelectItemPerfilEconomico(String prm) throws Exception {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarPerfilEconomicoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				PerfilEconomicoVO obj = (PerfilEconomicoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectItemPerfilEconomico(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			ordenador = null;
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Cidade</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Cidade</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemPerfilEconomico() {
		try {
			montarListaSelectItemPerfilEconomico("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarPerfilEconomicoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getPerfilEconomicoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>PerfilPadraoAluno</code>.
	 */
	public void montarListaSelectItemQuestionario(String prm) throws Exception {
		List resultadoConsulta = null;
		try {
			resultadoConsulta = consultarQuestionarioPorDescricao(prm);
			setListaSelectItemQuestionario(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>PerfilPadraoAluno</code>. Buscando todos os objetos correspondentes
	 * a entidade <code>PerfilAcesso</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemQuestionario() {
		try {
			montarListaSelectItemQuestionario("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarQuestionarioPorDescricao(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getQuestionarioFacade().consultarPorDescricao(nomePrm, "AI", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return lista;
	}

	public String getCaminhoServidorDownload() {
		try {
			MaterialUnidadeEnsinoVO obj = (MaterialUnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("materialUnidadeEnsinoVOItem");
			context().getExternalContext().getSessionMap().put("arquivoVO", obj.getArquivoVO());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getMaterialUnidadeEnsino().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.UNIDADEENSINO_TMP, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemTurno();
		montarListaSelectItensConfiguracoes();
		montarListaSelectItensContaCorrente();
		montarListaSelectItemPlanoFinanceiroCurso(false);
		montarListaSelectItemConfiguracaoNotaFiscal();
		montarListaSelectItemConfiguracaoContabil();
		montarListaSelectItemDependenciaAdministravia();
		montarListaSelectItemLocalizacaoZonaEscola();
		montarListaSelectItemLocalizacaoDiferenciadaEscola();
		montarListaSelectItemUnidadeVinculadaEscolaEducacaoBasica();
		montarListaSelectItemCategoriaEscolaPrivada();
		montarListaSelectItemConveniadaPoderPublico();
		montarListaSelectItemLocalFuncionamentoEscola();
		montarListaSelectItemFormaOcupacaoPredioEnum();
		montarListaSelectItemAguaConsumidaEnum();
		montarListaSelectItemAbastecimentoAguaEnum();
		montarListaSelectItemAbastecimentoEnergiaEnum();
		montarListaSelectItemEsgotoSanitarioEnum();
		montarListaSelectItemDestinoLixoEnum();
		montarListaSelectItemTratamentoLixoEnum();
		montarListaSelectItemTipoRequerimento();
		montarListaSelectItensConfiguracaoGED();
		montarListaSelectItemConfiguracaoMobile();
		montarListaSelectItemChancela();
		montarListaSelectItensConfiguracoesDiplomaDigital();
	}

	public void montarListaSelectItemDependenciaAdministravia() {
		try {
			setListaSelectItemDependenciaAdministrativa(new ArrayList<SelectItem>(0));
			for (DependenciaAdministativaEnum obj : DependenciaAdministativaEnum.values()) {
				getListaSelectItemDependenciaAdministrativa().add(new SelectItem(obj.getValor(), obj.getDescricao()));
			}
		} catch (Exception e) {
			setListaSelectItemDependenciaAdministrativa(new ArrayList<SelectItem>(0));
		}
	}

	public void montarListaSelectItemCategoriaEscolaPrivada() {
		try {
			setListaSelectItemCategoriaEscolaPrivada(new ArrayList<SelectItem>(0));
			getListaSelectItemCategoriaEscolaPrivada().add(new SelectItem("",""));
			for (CategoriaEscolaPrivadaEnum obj : CategoriaEscolaPrivadaEnum.values()) {
				getListaSelectItemCategoriaEscolaPrivada().add(new SelectItem(obj.getValor(), obj.getDescricao()));
			}
		} catch (Exception e) {
			setListaSelectItemCategoriaEscolaPrivada(new ArrayList<SelectItem>(0));
		}
	}
	public void montarListaSelectItemConveniadaPoderPublico() {
		try {
			setListaSelectItemConveniadaPoderPublico(new ArrayList<SelectItem>(0));
			getListaSelectItemConveniadaPoderPublico().add(new SelectItem("",""));
			for (ConveniadaPoderPublicoEnum obj : ConveniadaPoderPublicoEnum.values()) {
				getListaSelectItemConveniadaPoderPublico().add(new SelectItem(obj.getValor(), obj.getDescricao()));
			}
		} catch (Exception e) {
			setListaSelectItemConveniadaPoderPublico(new ArrayList<SelectItem>(0));
		}
	}
	
	public void montarListaSelectItemFormaOcupacaoPredioEnum() {
		try {
			setListaSelectItemFormaOcupacaoPredio(new ArrayList<SelectItem>(0));
			getListaSelectItemFormaOcupacaoPredio().add(new SelectItem("",""));
			for (FormaOcupacaoPredioEnum obj : FormaOcupacaoPredioEnum.values()) {
				getListaSelectItemFormaOcupacaoPredio().add(new SelectItem(obj.getValor(), obj.getDescricao()));
			}
		} catch (Exception e) {
			setListaSelectItemFormaOcupacaoPredio(new ArrayList<SelectItem>(0));
		}
	}
	
	public void montarListaSelectItemAguaConsumidaEnum() {
		try {
			setListaSelectItemConsumoAgua(new ArrayList<SelectItem>(0));
			getListaSelectItemConsumoAgua().add(new SelectItem("",""));
			for (AguaConsumidaEnum obj : AguaConsumidaEnum.values()) {
				getListaSelectItemConsumoAgua().add(new SelectItem(obj.getValor(), obj.getDescricao()));
			}
		} catch (Exception e) {
			setListaSelectItemConsumoAgua(new ArrayList<SelectItem>(0));
		}
	}
	
	
	public void montarListaSelectItemAbastecimentoAguaEnum() {
		try {
			setListaSelectItemAbastecimentoAgua(new ArrayList<SelectItem>(0));
			getListaSelectItemAbastecimentoAgua().add(new SelectItem("",""));
			for (AbastecimentoAguaEnum obj : AbastecimentoAguaEnum.values()) {
				getListaSelectItemAbastecimentoAgua().add(new SelectItem(obj.getValor(), obj.getDescricao()));
			}
		} catch (Exception e) {
			setListaSelectItemAbastecimentoAgua(new ArrayList<SelectItem>(0));
		}
	}
	
	public void montarListaSelectItemAbastecimentoEnergiaEnum() {
		try {
			setListaSelectItemAbastecimentoEnergia(new ArrayList<SelectItem>(0));
			getListaSelectItemAbastecimentoEnergia().add(new SelectItem("",""));
			for (AbastecimentoEnergiaEnum obj : AbastecimentoEnergiaEnum.values()) {
				getListaSelectItemAbastecimentoEnergia().add(new SelectItem(obj.getValor(), obj.getDescricao()));
			}
		} catch (Exception e) {
			setListaSelectItemAbastecimentoEnergia(new ArrayList<SelectItem>(0));
		}
	}
	
	public void montarListaSelectItemEsgotoSanitarioEnum() {
		try {
			setListaSelectItemEsgotoSanitario(new ArrayList<SelectItem>(0));
			getListaSelectItemEsgotoSanitario().add(new SelectItem("",""));
			for (EsgotoSanitarioEnum obj : EsgotoSanitarioEnum.values()) {
				getListaSelectItemEsgotoSanitario().add(new SelectItem(obj.getValor(), obj.getDescricao()));
			}
		} catch (Exception e) {
			setListaSelectItemEsgotoSanitario(new ArrayList<SelectItem>(0));
		}
	}
	
	public void montarListaSelectItemDestinoLixoEnum() {
		try {
			setListaSelectItemDestinoLixo(new ArrayList<SelectItem>(0));
			getListaSelectItemDestinoLixo().add(new SelectItem("",""));
			for (DestinoLixoEnum obj : DestinoLixoEnum.values()) {
				getListaSelectItemDestinoLixo().add(new SelectItem(obj.getValor(), obj.getDescricao()));
			}
		} catch (Exception e) {
			setListaSelectItemDestinoLixo(new ArrayList<SelectItem>(0));
		}
	}
	
	public void montarListaSelectItemTratamentoLixoEnum() {
		try {
			setListaSelectItemTratamentoLixo(new ArrayList<SelectItem>(0));
			getListaSelectItemTratamentoLixo().add(new SelectItem("",""));
			for (TratamentoLixoEnum obj : TratamentoLixoEnum.values()) {
				getListaSelectItemTratamentoLixo().add(new SelectItem(obj.getValor(), obj.getDescricao()));
			}
		} catch (Exception e) {
			setListaSelectItemTratamentoLixo(new ArrayList<SelectItem>(0));
		}
	}
	
	public void montarListaSelectItemLocalFuncionamentoEscola() {
		try {
			setListaSelectItemLocalFuncionamentoEscolar(new ArrayList<SelectItem>(0));
			getListaSelectItemLocalFuncionamentoEscolar().add(new SelectItem("",""));
			for (LocalDeFuncionamentoEscolarEnum obj : LocalDeFuncionamentoEscolarEnum.values()) {
				getListaSelectItemLocalFuncionamentoEscolar().add(new SelectItem(obj.getValor(), obj.getDescricao()));
			}
		} catch (Exception e) {
			setListaSelectItemLocalFuncionamentoEscolar(new ArrayList<SelectItem>(0));
		}
	}

	public void montarListaSelectItemLocalizacaoZonaEscola() {
		try {
			setListaSelectItemLocalizacaoZonaEscola(new ArrayList<SelectItem>(0));
			for (LocalizacaoZonaEscolaEnum obj : LocalizacaoZonaEscolaEnum.values()) {
				getListaSelectItemLocalizacaoZonaEscola().add(new SelectItem(obj.getValor(), obj.getDescricao()));
			}
		} catch (Exception e) {
			setListaSelectItemLocalizacaoZonaEscola(new ArrayList<SelectItem>(0));
		}
	}
	
	public void montarListaSelectItemLocalizacaoDiferenciadaEscola() {
		try {
			setListaSelectItemLocalizacaoDiferenciadaEscola(new ArrayList<SelectItem>(0));
			for (LocalizacaoDiferenciadaEscolaEnum obj : LocalizacaoDiferenciadaEscolaEnum.values()) {
				getListaSelectItemLocalizacaoDiferenciadaEscola().add(new SelectItem(obj.getValor(), obj.getDescricao()));
			}
		} catch (Exception e) {
			setListaSelectItemLocalizacaoDiferenciadaEscola(new ArrayList<SelectItem>(0));
		}
	}
	
	public void montarListaSelectItemUnidadeVinculadaEscolaEducacaoBasica() {
		try {
			setListaSelectItemUnidadeVinculadaEscolaEducacaoBasica(new ArrayList<SelectItem>(0));
			for (UnidadeVinculadaEscolaEducacaoBasicaEnum obj : UnidadeVinculadaEscolaEducacaoBasicaEnum.values()) {
				getListaSelectItemUnidadeVinculadaEscolaEducacaoBasica().add(new SelectItem(obj.getValor(), obj.getDescricao()));
			}
		} catch (Exception e) {
			setListaSelectItemUnidadeVinculadaEscolaEducacaoBasica(new ArrayList<SelectItem>(0));
		}
	}

	public void montarListaSelectItemConfiguracaoNotaFiscal() {
		try {
			montarListaSelectItemConfiguracaoNotaFiscal("");
		} catch (Exception e) {
			setListaSelectItemConfiguracaoNotaFiscal(new ArrayList(0));
		}
	}

	public void montarListaSelectItemConfiguracaoNotaFiscal(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarConfiguracaoNotaFiscalPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ConfiguracaoNotaFiscalVO obj = (ConfiguracaoNotaFiscalVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemConfiguracaoNotaFiscal(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarConfiguracaoNotaFiscalPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getConfiguracaoNotaFiscalFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}
	
	public void montarListaSelectItemConfiguracaoContabil() {
		try {
			montarListaSelectItemConfiguracaoContabil("");
		} catch (Exception e) {
			setListaSelectItemConfiguracaoContabil(new ArrayList(0));
		}
	}
	
	public void montarListaSelectItemConfiguracaoContabil(String prm) throws Exception {
		getListaSelectItemConfiguracaoContabil().clear();
		getListaSelectItemConfiguracaoContabil().add(new SelectItem(0, ""));
		List<ConfiguracaoContabilVO> resultadoConsulta = getFacadeFactory().getConfiguracaoContabilFacade().consultaRapidaPorCodigo(0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		resultadoConsulta.forEach(p->getListaSelectItemConfiguracaoContabil().add(new SelectItem(p.getCodigo(), p.getNome())));
	}
	

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> tipoConsultaCombo;
	public List<SelectItem> getTipoConsultaCombo() {
		if(tipoConsultaCombo  == null) {
		tipoConsultaCombo = new ArrayList<SelectItem>(0);
		// itens.add(new SelectItem("codigo", "Código"));
		tipoConsultaCombo.add(new SelectItem("nome", "Nome"));
		tipoConsultaCombo.add(new SelectItem("razaoSocial", "Razão Social"));
		tipoConsultaCombo.add(new SelectItem("nomeCidade", "Cidade"));
		tipoConsultaCombo.add(new SelectItem("CNPJ", "CNPJ"));
		tipoConsultaCombo.add(new SelectItem("inscEstadual", "Inscrição Estadual"));
		}
		return tipoConsultaCombo;
	}

	private static String CNPJ = "CNPJ";
	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals(CNPJ)) {
			return "return mascara(this.form,'formCadastro:valorConsulta','99.999.999/9999-99',event)";
		}
		return "";
	}

	public List getTipoConsultaComboCurso() {
		List itens = new ArrayList(0);
		// itens.add(new SelectItem("codigo", "Código"));

		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public List getTipoConsultaCidade() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public List getTipoConsultaFuncionario() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_CONS);
	}
	
	
	
	public void selecionarPorUnidadeEnsinoCursoCentroResultadoCurso() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
			getUnidadeEnsinoCursoCentroResultadoVO().setCursoVO(obj);
			setListaConsultaCurso(null);
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	public void limparCentroResultadoUnidadeEnsino() {
		try {
			getUnidadeEnsinoVO().setCentroResultadoVO(new CentroResultadoVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void limparCentroResultadoRequerimento() {
		try {
			getUnidadeEnsinoVO().setCentroResultadoRequerimentoVO(new CentroResultadoVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	public void inicializarUnidadeEnsinoCentroResultadoConsulta() {
		try {
			filtroConsultaCentroResultado = enumFiltroConsultaCentroResultado.UNIDADE_ENSINO;
			inicializarDadosCentroResultadoConsulta();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void inicializarRequerimentoCentroResultadoConsulta() {
		try {
			filtroConsultaCentroResultado = enumFiltroConsultaCentroResultado.REQUERIMENTO;
			inicializarDadosCentroResultadoConsulta();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void inicializarNivelEducacionalCentroResultadoConsulta() {
		try {
			filtroConsultaCentroResultado = enumFiltroConsultaCentroResultado.NIVEL_EDUCACIONAL;
			inicializarDadosCentroResultadoConsulta();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void inicializarCursoCentroResultadoConsulta() {
		try {
			filtroConsultaCentroResultado = enumFiltroConsultaCentroResultado.CURSO;
			inicializarDadosCentroResultadoConsulta();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void inicializarTipoRequerimentoCentroResultadoConsulta() {
		try {
			filtroConsultaCentroResultado = enumFiltroConsultaCentroResultado.TIPO_REQUERIMENTO;
			inicializarDadosCentroResultadoConsulta();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void inicializarDadosCentroResultadoConsulta() {
		setCentroResultadoDataModelo(new DataModelo());
		getCentroResultadoDataModelo().setCampoConsulta(CentroResultadoVO.enumCampoConsultaCentroResultado.DESCRICAO_CENTRO_RESULTADO.name());
	}

	public void selecionarCentroResultado() {
		try {
			CentroResultadoVO obj = (CentroResultadoVO) context().getExternalContext().getRequestMap().get("centroResultadoItens");
			switch (filtroConsultaCentroResultado) {
			case UNIDADE_ENSINO:
				getUnidadeEnsinoVO().setCentroResultadoVO(obj);
				break;
			case REQUERIMENTO:
				getUnidadeEnsinoVO().setCentroResultadoRequerimentoVO(obj);
				break;
			case CURSO:
				getUnidadeEnsinoCursoCentroResultadoVO().setCentroResultadoVO(obj);
				break;
			case TIPO_REQUERIMENTO:
				getUnidadeEnsinoTipoRequerimentoCentroResultadoVO().setCentroResultadoVO(obj);
				break;
			case NIVEL_EDUCACIONAL:
				getUnidadeEnsinoNivelEducacionalCentroResultadoVO().setCentroResultadoVO(obj);
				break;
			default:
				break;
			}
			
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerCentroResultado(DataScrollEvent dataScrollerEvent) {
		try {
			getCentroResultadoDataModelo().setPaginaAtual(dataScrollerEvent.getPage());
			getCentroResultadoDataModelo().setPage(dataScrollerEvent.getPage());
			consultarCentroResultado();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCentroResultado() {
		try {
			super.consultar();
			getCentroResultadoDataModelo().preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getCentroResultadoFacade().consultar(SituacaoEnum.ATIVO, true, null, null, null, getCentroResultadoDataModelo());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	public void addUnidadeEnsinoCursoCentroResultado() {
		try {
			getFacadeFactory().getUnidadeEnsinoFacade().addUnidadeEnsinoCursoCentroResultado(getUnidadeEnsinoVO(), getUnidadeEnsinoCursoCentroResultadoVO(), getUsuarioLogado());
			setUnidadeEnsinoCursoCentroResultadoVO(new UnidadeEnsinoCursoCentroResultadoVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void editarUnidadeEnsinoCursoCentroResultado() {
		try {
			setUnidadeEnsinoCursoCentroResultadoVO((UnidadeEnsinoCursoCentroResultadoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCentroResultadoItem"));
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}
	public void removerUnidadeEnsinoCursoCentroResultado() {
		try {
			setUnidadeEnsinoCursoCentroResultadoVO((UnidadeEnsinoCursoCentroResultadoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCentroResultadoItem"));
			getFacadeFactory().getUnidadeEnsinoFacade().removerUnidadeEnsinoCursoCentroResultado(getUnidadeEnsinoVO(), getUnidadeEnsinoCursoCentroResultadoVO(), getUsuarioLogado());
			setUnidadeEnsinoCursoCentroResultadoVO(new UnidadeEnsinoCursoCentroResultadoVO());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	public void adicionarUnidadeEnsinoNivelEducacionalCentroResultado() {
		try {
			getFacadeFactory().getUnidadeEnsinoNivelEducacionalCentroResultadoFacade().adicionarUnidadeEnsinoNivelEducacionalCentroResultadoVO(getUnidadeEnsinoVO(), getUnidadeEnsinoNivelEducacionalCentroResultadoVO(), getUsuarioLogado());
			setUnidadeEnsinoNivelEducacionalCentroResultadoVO(new UnidadeEnsinoNivelEducacionalCentroResultadoVO());
			getUnidadeEnsinoNivelEducacionalCentroResultadoVO().setTipoNivelEducacional(TipoNivelEducacional.BASICO);
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void editarUnidadeEnsinoNivelEducacionalCentroResultado() {
		try {
			setUnidadeEnsinoNivelEducacionalCentroResultadoVO((UnidadeEnsinoNivelEducacionalCentroResultadoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoNivelEducacionalCentroResultadoItem"));
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
	}
	public void removerUnidadeEnsinoNivelEducacionalCentroResultado() {
		try {
			setUnidadeEnsinoNivelEducacionalCentroResultadoVO((UnidadeEnsinoNivelEducacionalCentroResultadoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoNivelEducacionalCentroResultadoItem"));
			getFacadeFactory().getUnidadeEnsinoNivelEducacionalCentroResultadoFacade().removerUnidadeEnsinoNivelEducacionalCentroResultadoVO(getUnidadeEnsinoVO(), getUnidadeEnsinoNivelEducacionalCentroResultadoVO(), getUsuarioLogado());
			setUnidadeEnsinoNivelEducacionalCentroResultadoVO(new UnidadeEnsinoNivelEducacionalCentroResultadoVO());
			getUnidadeEnsinoNivelEducacionalCentroResultadoVO().setTipoNivelEducacional(TipoNivelEducacional.BASICO);
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	public void adicionarUnidadeEnsinoTipoRequerimentoCentroResultado() {
		try {
			getFacadeFactory().getUnidadeEnsinoTipoRequerimentoCentroResultadoFacade().adicionarUnidadeEnsinoTipoRequerimentoCentroResultadoVO(getUnidadeEnsinoVO(), getUnidadeEnsinoTipoRequerimentoCentroResultadoVO(), getUsuarioLogado());
			setUnidadeEnsinoTipoRequerimentoCentroResultadoVO(new UnidadeEnsinoTipoRequerimentoCentroResultadoVO());
			setMensagemID(MSG_TELA.msg_dados_adicionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void editarUnidadeEnsinoTipoRequerimentoCentroResultado() {
		try {
			setUnidadeEnsinoTipoRequerimentoCentroResultadoVO((UnidadeEnsinoTipoRequerimentoCentroResultadoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoTipoRequerimentoCentroResultadoItem"));
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
	}
	public void removerUnidadeEnsinoTipoRequerimentoCentroResultado() {
		try {
			setUnidadeEnsinoTipoRequerimentoCentroResultadoVO((UnidadeEnsinoTipoRequerimentoCentroResultadoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoTipoRequerimentoCentroResultadoItem"));
			getFacadeFactory().getUnidadeEnsinoTipoRequerimentoCentroResultadoFacade().removerUnidadeEnsinoTipoRequerimentoCentroResultadoVO(getUnidadeEnsinoVO(), getUnidadeEnsinoTipoRequerimentoCentroResultadoVO(), getUsuarioLogado());
			setUnidadeEnsinoTipoRequerimentoCentroResultadoVO(new UnidadeEnsinoTipoRequerimentoCentroResultadoVO());
			setMensagemID(MSG_TELA.msg_dados_excluidos.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	public void atualzarListaSelectItemTipoRequerimento() {
		try {
			montarListaSelectItemTipoRequerimento();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	
	public void montarListaSelectItemTipoRequerimento()  {
		List<TipoRequerimentoVO> resultadoConsulta = null;
		try {
			resultadoConsulta = getFacadeFactory().getTipoRequerimentoFacade().consultarTipoRequerimentoComboBox(false, "AT", getUnidadeEnsinoVO().getCodigo(), 0, true, getUsuarioLogado(), false);
			getListaSelectItemTipoRequerimento().clear();
			getListaSelectItemTipoRequerimento().add(new SelectItem(0, ""));
			resultadoConsulta.stream().forEach(p -> getListaSelectItemTipoRequerimento().add(new SelectItem(p.getCodigo(), p.getNome())));
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}
	
	public enum enumFiltroConsultaCentroResultado {
		UNIDADE_ENSINO, REQUERIMENTO, NIVEL_EDUCACIONAL, CURSO, TIPO_REQUERIMENTO;
	}
	
	public List getListaSelectItemTipoRequerimento() {
		if (listaSelectItemTipoRequerimento == null) {
			listaSelectItemTipoRequerimento = new ArrayList(0);
		}
		return listaSelectItemTipoRequerimento;
	}

	public void setListaSelectItemTipoRequerimento(List listaSelectItemTipoRequerimento) {
		this.listaSelectItemTipoRequerimento = listaSelectItemTipoRequerimento;
	}
	
	public DataModelo getCentroResultadoDataModelo() {
		centroResultadoDataModelo = Optional.ofNullable(centroResultadoDataModelo).orElse(new DataModelo());
		return centroResultadoDataModelo;
	}

	public void setCentroResultadoDataModelo(DataModelo centroResultadoDataModelo) {
		this.centroResultadoDataModelo = centroResultadoDataModelo;
	}

	public List getListaSelectItemPerfilPadraoCandidato() {
		return (listaSelectItemPerfilPadraoCandidato);
	}

	public void setListaSelectItemPerfilPadraoCandidato(List listaSelectItemPerfilPadraoCandidato) {
		this.listaSelectItemPerfilPadraoCandidato = listaSelectItemPerfilPadraoCandidato;
	}

	public List getListaSelectItemPerfilPadraoProfessor() {
		return (listaSelectItemPerfilPadraoProfessor);
	}

	public void setListaSelectItemPerfilPadraoProfessor(List listaSelectItemPerfilPadraoProfessor) {
		this.listaSelectItemPerfilPadraoProfessor = listaSelectItemPerfilPadraoProfessor;
	}

	public List getListaSelectItemPerfilPadraoAluno() {
		return (listaSelectItemPerfilPadraoAluno);
	}

	public void setListaSelectItemPerfilPadraoAluno(List listaSelectItemPerfilPadraoAluno) {
		this.listaSelectItemPerfilPadraoAluno = listaSelectItemPerfilPadraoAluno;
	}

	public List getListaSelectItemVisaoPadraoCandidato() {
		return (listaSelectItemVisaoPadraoCandidato);
	}

	public void setListaSelectItemVisaoPadraoCandidato(List listaSelectItemVisaoPadraoCandidato) {
		this.listaSelectItemVisaoPadraoCandidato = listaSelectItemVisaoPadraoCandidato;
	}

	public List getListaSelectItemVisaoPadraoProfessor() {
		return (listaSelectItemVisaoPadraoProfessor);
	}

	public void setListaSelectItemVisaoPadraoProfessor(List listaSelectItemVisaoPadraoProfessor) {
		this.listaSelectItemVisaoPadraoProfessor = listaSelectItemVisaoPadraoProfessor;
	}

	public List getListaSelectItemVisaoPadraoAluno() {
		return (listaSelectItemVisaoPadraoAluno);
	}

	public void setListaSelectItemVisaoPadraoAluno(List listaSelectItemVisaoPadraoAluno) {
		this.listaSelectItemVisaoPadraoAluno = listaSelectItemVisaoPadraoAluno;
	}

	public String getCurso_Erro() {
		return curso_Erro;
	}

	public void setCurso_Erro(String curso_Erro) {
		this.curso_Erro = curso_Erro;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		if(unidadeEnsinoCursoVO == null){
			unidadeEnsinoCursoVO = new UnidadeEnsinoCursoVO();
		}
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
	}

	public List getListaConsultaCidade() {
		if (listaConsultaCidade == null) {
			listaConsultaCidade = new ArrayList<>(0);
		}
		return (listaConsultaCidade);
	}

	public void setListaConsultaCidade(List listaConsultaCidade) {
		this.listaConsultaCidade = listaConsultaCidade;
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

	public List getListaSelectItemTurno() {
		return listaSelectItemTurno;
	}

	public void setListaSelectItemTurno(List listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoEditado() {
		return unidadeEnsinoCursoEditado;
	}

	public void setUnidadeEnsinoCursoEditado(UnidadeEnsinoCursoVO unidadeEnsinoCursoEditado) {
		this.unidadeEnsinoCursoEditado = unidadeEnsinoCursoEditado;
	}

	public Integer getTurnoUnidadeEnsinoCursoEditado() {
		if (turnoUnidadeEnsinoCursoEditado == null) {
			turnoUnidadeEnsinoCursoEditado = 0;
		}
		return turnoUnidadeEnsinoCursoEditado;
	}

	public void setTurnoUnidadeEnsinoCursoEditado(Integer turnoUnidadeEnsinoCursoEditado) {
		this.turnoUnidadeEnsinoCursoEditado = turnoUnidadeEnsinoCursoEditado;
	}

	public String getCampoConsultaCurso() {
		return campoConsultaCurso;
	}

	public void setCampoConsultaCurso(String campoConsultaCurso) {
		this.campoConsultaCurso = campoConsultaCurso;
	}

	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public List getListaConsultaFuncionario() {
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public List getListaConsultaCurso() {
		if(listaConsultaCurso == null){
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public String getValorConsultaCurso() {
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}

	public List getListaSelectItemPerfilEconomico() {
		return listaSelectItemPerfilEconomico;
	}

	public void setListaSelectItemPerfilEconomico(List listaSelectItemPerfilEconomico) {
		this.listaSelectItemPerfilEconomico = listaSelectItemPerfilEconomico;
	}

	public List getListaSelectItemQuestionario() {
		return listaSelectItemQuestionario;
	}

	public void setListaSelectItemQuestionario(List listaSelectItemQuestionario) {
		this.listaSelectItemQuestionario = listaSelectItemQuestionario;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		unidadeEnsinoVO = null;
		Uteis.liberarListaMemoria(listaConsultaCidade);
		Uteis.liberarListaMemoria(listaSelectItemTurno);
		Uteis.liberarListaMemoria(listaConsultaCurso);
		Uteis.liberarListaMemoria(listaSelectItemPerfilEconomico);
		Uteis.liberarListaMemoria(listaSelectItemPerfilPadraoAluno);
		Uteis.liberarListaMemoria(listaSelectItemPerfilPadraoCandidato);
		Uteis.liberarListaMemoria(listaSelectItemPerfilPadraoProfessor);
		Uteis.liberarListaMemoria(listaSelectItemVisaoPadraoAluno);
		Uteis.liberarListaMemoria(listaSelectItemVisaoPadraoCandidato);
		Uteis.liberarListaMemoria(listaSelectItemVisaoPadraoProfessor);
		Uteis.liberarListaMemoria(listaSelectItemQuestionario);
		campoConsultaCurso = null;
		valorConsultaCurso = null;
		campoConsultaCidade = null;
		valorConsultaCidade = null;
		unidadeEnsinoCursoVO = null;
		unidadeEnsinoCursoEditado = null;
		curso_Erro = null;
		matriz = null;
	}

	/**
	 * @return the campoConsultaCidade
	 */
	public String getCampoConsultaCidade() {
		return campoConsultaCidade;
	}

	/**
	 * @param campoConsultaCidade
	 *            the campoConsultaCidade to set
	 */
	public void setCampoConsultaCidade(String campoConsultaCidade) {
		this.campoConsultaCidade = campoConsultaCidade;
	}

	/**
	 * @return the valorConsultaCidade
	 */
	public String getValorConsultaCidade() {
		return valorConsultaCidade;
	}

	/**
	 * @param valorConsultaCidade
	 *            the valorConsultaCidade to set
	 */
	public void setValorConsultaCidade(String valorConsultaCidade) {
		this.valorConsultaCidade = valorConsultaCidade;
	}

	/**
	 * @return the listaSelectItemConfiguracoes
	 */
	public List getListaSelectItemConfiguracoes() {
		return listaSelectItemConfiguracoes;
	}

	/**
	 * @param listaSelectItemConfiguracoes
	 *            the listaSelectItemConfiguracoes to set
	 */
	public void setListaSelectItemConfiguracoes(List listaSelectItemConfiguracoes) {
		this.listaSelectItemConfiguracoes = listaSelectItemConfiguracoes;
	}

	/**
	 * @return the listaSelectItemPlanoFinanceiroCurso
	 */
	public List getListaSelectItemPlanoFinanceiroCurso() {
		return listaSelectItemPlanoFinanceiroCurso;
	}

	/**
	 * @param listaSelectItemPlanoFinanceiroCurso
	 *            the listaSelectItemPlanoFinanceiroCurso to set
	 */
	public void setListaSelectItemPlanoFinanceiroCurso(List listaSelectItemPlanoFinanceiroCurso) {
		this.listaSelectItemPlanoFinanceiroCurso = listaSelectItemPlanoFinanceiroCurso;
	}

	public List getListaSelectItemContaCorrente() {
		if (listaSelectItemContaCorrente == null) {
			listaSelectItemContaCorrente = new ArrayList(0);
		}
		return listaSelectItemContaCorrente;
	}

	public void setListaSelectItemContaCorrente(List listaSelectItemContaCorrente) {
		this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
	}

	public String getUrlLogoApresentar() {
		if (getUnidadeEnsinoVO().getExisteLogo()) {
			try {
				return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + getUnidadeEnsinoVO().getCaminhoBaseLogo().replaceAll("\\\\", "/") + "/" + getUnidadeEnsinoVO().getNomeArquivoLogo();
			} catch (Exception e) {
				return "/resources/imagens/topoLogoVisao2.png";
			}
		}
		return "/resources/imagens/topoLogoVisao2.png";
	}

	public String getUrlLogoRelatorioApresentar() {
		if (getUnidadeEnsinoVO().getExisteLogoRelatorio()) {
			try {
				return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + getUnidadeEnsinoVO().getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/" + getUnidadeEnsinoVO().getNomeArquivoLogoRelatorio();
			} catch (Exception e) {
				return "/resources/imagens/logoPadraoRelatorio.png";
			}
		}
		return "/resources/imagens/logoPadraoRelatorio.png";
	}

	public String getUrlLogoIndexApresentar() {
		if (getUnidadeEnsinoVO().getExisteLogoIndex()) {
			try {
				return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + getUnidadeEnsinoVO().getCaminhoBaseLogoIndex().replaceAll("\\\\", "/") + "/" + getUnidadeEnsinoVO().getNomeArquivoLogoIndex();
			} catch (Exception e) {
				return "/resources/imagens/logo.png";
			}
		}
		return "/resources/imagens/logo.png";
	}
	
	public String getUrlLogoPaginaInicialApresentar() {
		if (getUnidadeEnsinoVO().getExisteLogoPaginaInicial()) {
			try {
				return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + getUnidadeEnsinoVO().getCaminhoBaseLogoPaginaInicial().replaceAll("\\\\", "/") + "/" + getUnidadeEnsinoVO().getNomeArquivoLogoPaginaInicial();
			} catch (Exception e) {
				return "/resources/imagens/logoCentro.png";
			}
		}
		return "/resources/imagens/logoCentro.png";
	}
	
	public String getUrlLogoEmailCimaApresentar() {
		if (getUnidadeEnsinoVO().getExisteLogoEmailCima()) {
			try {				
				return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + getUnidadeEnsinoVO().getCaminhoBaseLogoEmailCima().replaceAll("\\\\", "/") + "/" + getUnidadeEnsinoVO().getNomeArquivoLogoEmailCima();
			} catch (Exception e) {
				return "/resources/imagens/email/cima_sei.png";
			}
		}
		return "/resources/imagens/email/cima_sei.png";
	}
	
	public String getUrlLogoEmailBaixoApresentar() {
		if (getUnidadeEnsinoVO().getExisteLogoEmailBaixo()) {
			try {
				return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + getUnidadeEnsinoVO().getCaminhoBaseLogoEmailBaixo().replaceAll("\\\\", "/") + "/" + getUnidadeEnsinoVO().getNomeArquivoLogoEmailBaixo();
			} catch (Exception e) {
				return "/resources/imagens/email/baixo_sei.png";
			}
		}
		return "/resources/imagens/email/baixo_sei.png";
	}
	
	public String getUrlLogoMunicipioApresentar() {
		if (getUnidadeEnsinoVO().getExisteLogoMunicipio()) {
			try {
				return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + getUnidadeEnsinoVO().getCaminhoBaseLogoMunicipio().replaceAll("\\\\", "/") + "/" + getUnidadeEnsinoVO().getNomeArquivoLogoMunicipio();
			} catch (Exception e) {
				return "/resources/imagens/logo.png";
			}
		}
		return "/resources/imagens/logo.png";
	}
	
	public String getUrlLogoAplicativoApresentar() {
		if (getUnidadeEnsinoVO().getExisteLogoAplicativo()) {
			try {
				return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + getUnidadeEnsinoVO().getCaminhoBaseLogoAplicativo().replaceAll("\\\\", "/") + "/" + getUnidadeEnsinoVO().getNomeArquivoLogoAplicativo();

			} catch (Exception e) {
				return "/resources/imagens/logoBranca.png";
			}
		}
		return "/resources/imagens/logoBranca.png";
	}

	/**
	 * @return the materialUnidadeEnsino
	 */
	public MaterialUnidadeEnsinoVO getMaterialUnidadeEnsino() {
		if (materialUnidadeEnsino == null) {
			materialUnidadeEnsino = new MaterialUnidadeEnsinoVO();
		}
		return materialUnidadeEnsino;
	}

	/**
	 * @param materialUnidadeEnsino
	 *            the materialUnidadeEnsino to set
	 */
	public void setMaterialUnidadeEnsino(MaterialUnidadeEnsinoVO materialUnidadeEnsino) {
		this.materialUnidadeEnsino = materialUnidadeEnsino;
	}

	public String getVerificarUltrapassouTamanhoMaximoUpload() {
		try {
			return "Arquivo não Enviado. Tamanho Máximo Permitido " + getConfiguracaoGeralPadraoSistema().getTamanhoMaximoUpload() + "MB.";
		} catch (Exception e) {
			return "";
		}
	}

	public String getTamanhoMaximoUpload() {
		try {
			return "Tamanho Máximo Permitido: " + getConfiguracaoGeralPadraoSistema().getTamanhoMaximoUpload() + "MB.";
		} catch (Exception e) {
			return "Tamanho Máximo Não Configurado";
		}
	}

	public List getListaSelectItemConfiguracaoNotaFiscal() {
		if (listaSelectItemConfiguracaoNotaFiscal == null) {
			listaSelectItemConfiguracaoNotaFiscal = new ArrayList(0);
		}
		return listaSelectItemConfiguracaoNotaFiscal;
	}

	public void setListaSelectItemConfiguracaoNotaFiscal(List listaSelectItemConfiguracaoNotaFiscal) {
		this.listaSelectItemConfiguracaoNotaFiscal = listaSelectItemConfiguracaoNotaFiscal;
	}

	public List<SelectItem> getListaSelectItemConfiguracaoContabil() {
		if (listaSelectItemConfiguracaoContabil == null) {
			listaSelectItemConfiguracaoContabil = new ArrayList(0);
		}
		return listaSelectItemConfiguracaoContabil;
	}

	public void setListaSelectItemConfiguracaoContabil(List<SelectItem> listaSelectItemConfiguracaoContabil) {
		this.listaSelectItemConfiguracaoContabil = listaSelectItemConfiguracaoContabil;
	}

	public List<SelectItem> getListaSelectItemDependenciaAdministrativa() {
		if (listaSelectItemDependenciaAdministrativa == null) {
			listaSelectItemDependenciaAdministrativa = new ArrayList<>(0);
		}
		return listaSelectItemDependenciaAdministrativa;
	}

	public void setListaSelectItemDependenciaAdministrativa(List<SelectItem> listaSelectItemDependenciaAdministrativa) {
		this.listaSelectItemDependenciaAdministrativa = listaSelectItemDependenciaAdministrativa;
	}

	public List<SelectItem> getListaSelectItemLocalizacaoZonaEscola() {
		if (listaSelectItemLocalizacaoZonaEscola == null) {
			listaSelectItemLocalizacaoZonaEscola = new ArrayList<>(0);
		}
		return listaSelectItemLocalizacaoZonaEscola;
	}

	public void setListaSelectItemLocalizacaoZonaEscola(List<SelectItem> listaSelectItemLocalizacaoZonaEscola) {
		this.listaSelectItemLocalizacaoZonaEscola = listaSelectItemLocalizacaoZonaEscola;
	}
	
	public List<SelectItem> getListaSelectItemLocalizacaoDiferenciadaEscola() {
		if (listaSelectItemLocalizacaoDiferenciadaEscola == null) {
			listaSelectItemLocalizacaoDiferenciadaEscola = new ArrayList<>(0);
		}
		return listaSelectItemLocalizacaoDiferenciadaEscola;
	}

	public void setListaSelectItemLocalizacaoDiferenciadaEscola(List<SelectItem> listaSelectItemLocalizacaoDiferenciadaEscola) {
		this.listaSelectItemLocalizacaoDiferenciadaEscola = listaSelectItemLocalizacaoDiferenciadaEscola;
	}

	public List<SelectItem> getListaSelectItemUnidadeVinculadaEscolaEducacaoBasica() {
		if (listaSelectItemUnidadeVinculadaEscolaEducacaoBasica == null) {
			listaSelectItemUnidadeVinculadaEscolaEducacaoBasica = new ArrayList<>(0);
		}
		return listaSelectItemUnidadeVinculadaEscolaEducacaoBasica;
	}

	public void setListaSelectItemUnidadeVinculadaEscolaEducacaoBasica(
			List<SelectItem> listaSelectItemUnidadeVinculadaEscolaEducacaoBasica) {
		this.listaSelectItemUnidadeVinculadaEscolaEducacaoBasica = listaSelectItemUnidadeVinculadaEscolaEducacaoBasica;
	}

	public List<SelectItem> getListaSelectItemCategoriaEscolaPrivada() {
		if (listaSelectItemCategoriaEscolaPrivada == null) {
			listaSelectItemCategoriaEscolaPrivada = new ArrayList<>(0);
		}
		return listaSelectItemCategoriaEscolaPrivada;
	}

	public void setListaSelectItemCategoriaEscolaPrivada(List<SelectItem> listaSelectItemCategoriaEscolaPrivada) {
		this.listaSelectItemCategoriaEscolaPrivada = listaSelectItemCategoriaEscolaPrivada;
	}

	public void montarCategoriaEscolaPrivadaEConveniadaPoderPublico() {
		try {
			if (getUnidadeEnsinoVO().getDependenciaAdministrativa().equals("PR")) {
				setApresentarCategoriaEscolaConveniadaPoderPublico(Boolean.TRUE);
			} else {
				setApresentarCategoriaEscolaConveniadaPoderPublico(Boolean.FALSE);
				getUnidadeEnsinoVO().setCategoriaEscolaPrivada("");
				getUnidadeEnsinoVO().setConveniadaPoderPublico("");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void montarFormaOcupacaoPredio(){
		if(getUnidadeEnsinoVO().getLocalFuncionamentoDaEscola().equals("PE")|| getUnidadeEnsinoVO().getLocalFuncionamentoDaEscola().equals("GR")){
			setApresentarFormaOcupacaoPredio(Boolean.TRUE);
		}else {
			setApresentarFormaOcupacaoPredio(Boolean.FALSE);
			getUnidadeEnsinoVO().setFormaOcupacaoPredio("");
			
		}
	}

	public Boolean getApresentarCategoriaEscolaConveniadaPoderPublico() {
		if (apresentarCategoriaEscolaConveniadaPoderPublico == null) {
			apresentarCategoriaEscolaConveniadaPoderPublico = Boolean.FALSE;
		}
		return apresentarCategoriaEscolaConveniadaPoderPublico;
	}

	public void setApresentarCategoriaEscolaConveniadaPoderPublico(Boolean apresentarCategoriaEscolaConveniadaPoderPublico) {
		this.apresentarCategoriaEscolaConveniadaPoderPublico = apresentarCategoriaEscolaConveniadaPoderPublico;
	}

	public List<SelectItem> getListaSelectItemConveniadaPoderPublico() {
		if(listaSelectItemConveniadaPoderPublico == null){
			listaSelectItemConveniadaPoderPublico = new ArrayList<>(0);
		}
		return listaSelectItemConveniadaPoderPublico;
	}

	public void setListaSelectItemConveniadaPoderPublico(List<SelectItem> listaSelectItemConveniadaPoderPublico) {
		this.listaSelectItemConveniadaPoderPublico = listaSelectItemConveniadaPoderPublico;
	}

	public List<SelectItem> getListaSelectItemLocalFuncionamentoEscolar() {
		if(listaSelectItemLocalFuncionamentoEscolar == null){
			listaSelectItemLocalFuncionamentoEscolar = new ArrayList<>(0);
		}
		return listaSelectItemLocalFuncionamentoEscolar;
	}

	public void setListaSelectItemLocalFuncionamentoEscolar(List<SelectItem> listaSelectItemLocalFuncionamentoEscolar) {
		this.listaSelectItemLocalFuncionamentoEscolar = listaSelectItemLocalFuncionamentoEscolar;
	}

	public Boolean getApresentarFormaOcupacaoPredio() {
		if(apresentarFormaOcupacaoPredio == null){
			apresentarFormaOcupacaoPredio = Boolean.FALSE;
		}
		return apresentarFormaOcupacaoPredio;
	}

	public void setApresentarFormaOcupacaoPredio(Boolean apresentarFormaOcupacaoPredio) {
		this.apresentarFormaOcupacaoPredio = apresentarFormaOcupacaoPredio;
	}

	public List<SelectItem> getListaSelectItemFormaOcupacaoPredio() {
		if(listaSelectItemFormaOcupacaoPredio == null){
			listaSelectItemFormaOcupacaoPredio = new ArrayList<>(0);
		}
		return listaSelectItemFormaOcupacaoPredio;
	}

	public void setListaSelectItemFormaOcupacaoPredio(List<SelectItem> listaSelectItemFormaOcupacaoPredio) {
		this.listaSelectItemFormaOcupacaoPredio = listaSelectItemFormaOcupacaoPredio;
	}

	public List<SelectItem> getListaSelectItemConsumoAgua() {
		if(listaSelectItemConsumoAgua == null){
			listaSelectItemConsumoAgua = new ArrayList<>(0);
		}
		return listaSelectItemConsumoAgua;
	}

	public void setListaSelectItemConsumoAgua(List<SelectItem> listaSelectItemConsumoAgua) {
		this.listaSelectItemConsumoAgua = listaSelectItemConsumoAgua;
	}

	public List<SelectItem> getListaSelectItemAbastecimentoAgua() {
		if(listaSelectItemAbastecimentoAgua == null){
			listaSelectItemAbastecimentoAgua = new ArrayList<>(0);
		}
		return listaSelectItemAbastecimentoAgua;
	}

	public void setListaSelectItemAbastecimentoAgua(List<SelectItem> listaSelectItemAbastecimentoAgua) {
		this.listaSelectItemAbastecimentoAgua = listaSelectItemAbastecimentoAgua;
	}

	public List<SelectItem> getListaSelectItemAbastecimentoEnergia() {
		if(listaSelectItemAbastecimentoEnergia == null){
			listaSelectItemAbastecimentoEnergia = new ArrayList<>(0);
		}
		return listaSelectItemAbastecimentoEnergia;
	}

	public void setListaSelectItemAbastecimentoEnergia(List<SelectItem> listaSelectItemAbastecimentoEnergia) {
		this.listaSelectItemAbastecimentoEnergia = listaSelectItemAbastecimentoEnergia;
	}

	public List<SelectItem> getListaSelectItemEsgotoSanitario() {
		if(listaSelectItemEsgotoSanitario == null){
			listaSelectItemEsgotoSanitario = new ArrayList<>(0);
		}
		return listaSelectItemEsgotoSanitario;
	}

	public void setListaSelectItemEsgotoSanitario(List<SelectItem> listaSelectItemEsgotoSanitario) {
		this.listaSelectItemEsgotoSanitario = listaSelectItemEsgotoSanitario;
	}

	public List<SelectItem> getListaSelectItemDestinoLixo() {
		if(listaSelectItemDestinoLixo == null){
			listaSelectItemDestinoLixo = new ArrayList<>(0);
		}
		return listaSelectItemDestinoLixo;
	}

	public void setListaSelectItemDestinoLixo(List<SelectItem> listaSelectItemDestinoLixo) {
		this.listaSelectItemDestinoLixo = listaSelectItemDestinoLixo;
	}

	public List<SelectItem> getListaSelectItemTratamentoLixo() {
		if(listaSelectItemTratamentoLixo == null){
			listaSelectItemTratamentoLixo = new ArrayList<>(0);
		}
		return listaSelectItemTratamentoLixo;
	}

	public void setListaSelectItemTratamentoLixo(List<SelectItem> listaSelectItemTratamentoLixo) {
		this.listaSelectItemTratamentoLixo = listaSelectItemTratamentoLixo;
	}
	
	public UnidadeEnsinoCursoCentroResultadoVO getUnidadeEnsinoCursoCentroResultadoVO() {
		unidadeEnsinoCentroResultadoVO = Optional.ofNullable(unidadeEnsinoCentroResultadoVO).orElse(new UnidadeEnsinoCursoCentroResultadoVO());
		return unidadeEnsinoCentroResultadoVO;
	}

	public void setUnidadeEnsinoCursoCentroResultadoVO(UnidadeEnsinoCursoCentroResultadoVO unidadeEnsinoCentroResultadoVO) {
		this.unidadeEnsinoCentroResultadoVO = unidadeEnsinoCentroResultadoVO;
	}
	
	public UnidadeEnsinoNivelEducacionalCentroResultadoVO getUnidadeEnsinoNivelEducacionalCentroResultadoVO() {
		unidadeEnsinoNivelEducacionalCentroResultadoVO = Optional.ofNullable(unidadeEnsinoNivelEducacionalCentroResultadoVO).orElse(new UnidadeEnsinoNivelEducacionalCentroResultadoVO());
		return unidadeEnsinoNivelEducacionalCentroResultadoVO;
	}

	public void setUnidadeEnsinoNivelEducacionalCentroResultadoVO(UnidadeEnsinoNivelEducacionalCentroResultadoVO unidadeEnsinoNivelEducacionalCentroResultadoVO) {
		this.unidadeEnsinoNivelEducacionalCentroResultadoVO = unidadeEnsinoNivelEducacionalCentroResultadoVO;
	}

	public UnidadeEnsinoTipoRequerimentoCentroResultadoVO getUnidadeEnsinoTipoRequerimentoCentroResultadoVO() {
		unidadeEnsinoTipoRequerimentoCentroResultadoVO = Optional.ofNullable(unidadeEnsinoTipoRequerimentoCentroResultadoVO).orElse(new UnidadeEnsinoTipoRequerimentoCentroResultadoVO());
		return unidadeEnsinoTipoRequerimentoCentroResultadoVO;
	}

	public void setUnidadeEnsinoTipoRequerimentoCentroResultadoVO(UnidadeEnsinoTipoRequerimentoCentroResultadoVO unidadeEnsinoTipoRequerimentoCentroResultadoVO) {
		this.unidadeEnsinoTipoRequerimentoCentroResultadoVO = unidadeEnsinoTipoRequerimentoCentroResultadoVO;
	}

	public Boolean getIsCidadeSantoAndre() {
		return "3547809".equals(getUnidadeEnsinoVO().getCidade().getCodigoIBGE());
	}

	public Boolean getApresentarMensagemAlertaCadastro() {
		if (this.getUnidadeEnsinoVO().getCodigo().intValue() == 0 && !getUsuarioDesbloqueioAutenticado()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	public Boolean getUsuarioDesbloqueioAutenticado() {
		if (usuarioDesbloqueioAutenticado == null) {
			usuarioDesbloqueioAutenticado = false;
		}
		return usuarioDesbloqueioAutenticado;
	}

	public void setUsuarioDesbloqueioAutenticado(Boolean usuarioDesbloqueioAutenticado) {
		this.usuarioDesbloqueioAutenticado = usuarioDesbloqueioAutenticado;
	}

	public String verificarUsuarioDesbloqueioAutenticacao() {
		try {
			if (getUsernameDesbloqueioAutenticado().equals("") || getSenhaDesbloqueioAutenticado().equals("")) {
				throw new Exception("Informe o usuário e senha para desbloqueio!");
			}
			if (getUsernameDesbloqueioAutenticado().equals("otimize-ti") && getSenhaDesbloqueioAutenticado().equals("otm13")) {
				setUsuarioDesbloqueioAutenticado(Boolean.TRUE);
				setModalGravarUnidade("#{rich:component('panelGravarUnidade')}.hide();");
			} else {
				throw new Exception("Usuário e/ou senha incorreto!");
			}
			setMensagemID("msg_PermissaoUsuarioRealizarMatriculaDisciplinaPreRequisito");
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
		} catch (Exception e) {
			setModalGravarUnidade("");
			setUsuarioDesbloqueioAutenticado(Boolean.FALSE);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao(TELA_FORM);
		}
	}
	
	public String getUsernameDesbloqueioAutenticado() {
		if (usernameDesbloqueioAutenticado == null) {
			usernameDesbloqueioAutenticado = "";
		}
		return usernameDesbloqueioAutenticado;
	}

	public void setUsernameDesbloqueioAutenticado(String usernameDesbloqueioAutenticado) {
		this.usernameDesbloqueioAutenticado = usernameDesbloqueioAutenticado;
	}

	public String getSenhaDesbloqueioAutenticado() {
		if (senhaDesbloqueioAutenticado == null) {
			senhaDesbloqueioAutenticado = "";
		}
		return senhaDesbloqueioAutenticado;
	}

	public void setSenhaDesbloqueioAutenticado(String senhaDesbloqueioAutenticado) {
		this.senhaDesbloqueioAutenticado = senhaDesbloqueioAutenticado;
	}

	public String getModalGravarUnidade() {
		if (modalGravarUnidade == null) {
			modalGravarUnidade = "#{rich:component('panelGravarUnidade')}.hide();";
		}
		return modalGravarUnidade;
	}

	public void setModalGravarUnidade(String modalGravarUnidade) {
		this.modalGravarUnidade = modalGravarUnidade;
	}
	
	public List<SelectItem> getListaSelectItemConfiguracaoGEDVOs() {
		if (listaSelectItemConfiguracaoGEDVOs == null) {
			listaSelectItemConfiguracaoGEDVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemConfiguracaoGEDVOs;
	}

	public void setListaSelectItemConfiguracaoGEDVOs(List<SelectItem> listaSelectItemConfiguracaoGEDVOs) {
		this.listaSelectItemConfiguracaoGEDVOs = listaSelectItemConfiguracaoGEDVOs;
	}
	
	public void montarListaSelectItensConfiguracaoGED() throws Exception {
		List configuracaoGEDVOs = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorNome("", false, getUsuarioLogado());
		List lista = UtilSelectItem.getListaSelectItem(configuracaoGEDVOs, "codigo", "nome", true);
		setListaSelectItemConfiguracaoGEDVOs(lista);
	}
	
	public List<SelectItem> getListaSelectItemConfiguracaoMobile() {
		if (listaSelectItemConfiguracaoMobile == null) {
			listaSelectItemConfiguracaoMobile = new ArrayList<SelectItem>();
		}
		return listaSelectItemConfiguracaoMobile;
	}

	public void setListaSelectItemConfiguracaoMobile(List<SelectItem> listaSelectItemConfiguracaoMobile) {
		this.listaSelectItemConfiguracaoMobile = listaSelectItemConfiguracaoMobile;
	}

	public void montarListaSelectItemConfiguracaoMobile() {
		try {
			montarListaSelectItemConfiguracaoMobile("");
		} catch (Exception e) {
			setListaSelectItemConfiguracaoMobile(new ArrayList(0));
		}
	}

	public void montarListaSelectItemConfiguracaoMobile(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarConfiguracaoMobilePorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				ConfiguracaoMobileVO obj = (ConfiguracaoMobileVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemConfiguracaoMobile(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}
	
	public List consultarConfiguracaoMobilePorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getConfiguracaoMobileFacade().consultarPorNomeAtivo(nomePrm, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}
	
	public List<SelectItem> getListaSelectItemChancela() {
		if (listaSelectItemChancela == null) {
			listaSelectItemChancela = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemChancela;
	}

	public void setListaSelectItemChancela(List<SelectItem> listaSelectItemChancela) {
		this.listaSelectItemChancela = listaSelectItemChancela;
	}
	
	public Boolean getCampoValorPorAluno() {
		return campoValorPorAluno;
	}

	public void setCampoValorPorAluno(Boolean campoValorPorAluno) {
		this.campoValorPorAluno = campoValorPorAluno;
	}
	
	public List getListaSelectItemTipoAutorizacao() {
		if (listaSelectItemTipoAutorizacao == null) {
			listaSelectItemTipoAutorizacao = new ArrayList<SelectItem>(0);
			listaSelectItemTipoAutorizacao.add(new SelectItem(Constantes.EMPTY, Constantes.EMPTY));
			for (TipoAutorizacaoCursoEnum enumerador : TipoAutorizacaoCursoEnum.values()) {
				listaSelectItemTipoAutorizacao.add(new SelectItem(enumerador, enumerador.getValorApresentar()));
			}
		}
		return listaSelectItemTipoAutorizacao;
	}
	
	public List<SelectItem> getListaSelectItemConfiguracoesDiplomaDigital() {
		if (listaSelectItemConfiguracoesDiplomaDigital == null) {
			listaSelectItemConfiguracoesDiplomaDigital = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemConfiguracoesDiplomaDigital;
	}
	
	public void setListaSelectItemConfiguracoesDiplomaDigital(List<SelectItem> listaSelectItemConfiguracoesDiplomaDigital) {
		this.listaSelectItemConfiguracoesDiplomaDigital = listaSelectItemConfiguracoesDiplomaDigital;
	}
	
	public void montarListaSelectItensConfiguracoesDiplomaDigital() throws Exception {
		setListaSelectItemConfiguracoesDiplomaDigital(new ArrayList<>(0));
		getListaSelectItemConfiguracoesDiplomaDigital().add(new SelectItem(0, ""));
		List<ConfiguracaoDiplomaDigitalVO> configuracoes = getFacadeFactory().getConfiguracaoDiplomaDigitalInterfaceFacade().listarTodasConfiguracoes(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		if (!configuracoes.isEmpty()) {
			for (ConfiguracaoDiplomaDigitalVO obj : configuracoes) {
				getListaSelectItemConfiguracoesDiplomaDigital().add(new SelectItem(obj.getCodigo(), obj.getDescricao() + (obj.getPadrao() ? " - PADRÃO" : "")));
			}
		}
	}
	
	public void selecionarCidadeMantenedoraRegistradora() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItems");
		getUnidadeEnsinoVO().setCidadeMantenedoraRegistradora(obj);
		getListaConsultaCidade().clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}
	
	public void selecionarCidadeMantenedora() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItems");
		getUnidadeEnsinoVO().setCidadeMantenedora(obj);
		getListaConsultaCidade().clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}
	
	public void selecionarCidadeRegistradora() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItems");
		getUnidadeEnsinoVO().setCidadeRegistradora(obj);
		getListaConsultaCidade().clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}
	
	public void carregarEnderecoRegistradora() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEnderecoRegistradora(getUnidadeEnsinoVO(), false, false, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void carregarEnderecoMantenedoraRegistradora() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEnderecoRegistradora(getUnidadeEnsinoVO(), true, false, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void carregarEnderecoMantenedora() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEnderecoRegistradora(getUnidadeEnsinoVO(), false, true, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void exportarExcelVagaOfertada() throws Exception{
		try {
			List<UnidadeEnsinoVO> listaUnidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarTodasUnidadeEnsinoVagaOfertada();
	        File arquivoExcel = getFacadeFactory().getUnidadeEnsinoFacade().carregarExcelVagaOfertadaUnidadeEnsino(listaUnidadeEnsino, getConfiguracaoGeralPadraoSistema());	        
			ArquivoVO arquivovo = new ArquivoVO();
			arquivovo.setNome(arquivoExcel.getName());
			arquivovo.setPastaBaseArquivo(PastaBaseArquivoEnum.UNIDADEENSINO_TMP.getValue());
	        realizarDownloadArquivo(arquivovo, getConfiguracaoGeralPadraoSistema());
			setMensagemID("msg_dados_exportados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		
		}
	}
	
	public String getUrlDownloadSV() {
		if(getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			return "location.href='../../DownloadSV'";
		}else {
			return "location.href='../DownloadSV'";
		}
	}
	
	public void importarExcelVagaOfertada(FileUploadEvent uploadEvent) throws Exception {
	    try {
	        limparListaUnidadeEnsinoVagaOfertada();
	        List<UnidadeEnsinoVO> listaTemporaria = getFacadeFactory().getUnidadeEnsinoFacade().importarPlanilhaUnidadeEnsinoVagaOfertada(uploadEvent, getUsuario());
	        setListaUnidadeEnsinoVagaOfertada(listaTemporaria);	
			setMensagemID("msg_dados_importados", Uteis.SUCESSO);

	        } catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
				setOncompleteModal("RichFaces.$('panelImportarPlanilhaVagaOfertada').hide()");

	    }
	}

	
	private List<UnidadeEnsinoVO> ListaUnidadeEnsinoVagaOfertada;

	public List<UnidadeEnsinoVO> getListaUnidadeEnsinoVagaOfertada() {
		if(ListaUnidadeEnsinoVagaOfertada == null) {
            ListaUnidadeEnsinoVagaOfertada = new ArrayList<>();
		}
		return ListaUnidadeEnsinoVagaOfertada;
	}

	public void setListaUnidadeEnsinoVagaOfertada(List<UnidadeEnsinoVO> listaUnidadeEnsinoVagaOfertada) {
		ListaUnidadeEnsinoVagaOfertada = listaUnidadeEnsinoVagaOfertada;
	}
	public void limparListaUnidadeEnsinoVagaOfertada() {
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		
		setListaUnidadeEnsinoVagaOfertada(new ArrayList<>());
	}
	public void gravarImportacaoExcelVagaOfertada() throws Exception {
		if (ListaUnidadeEnsinoVagaOfertada != null && ListaUnidadeEnsinoVagaOfertada.size() > 0) {
			for (UnidadeEnsinoVO unidadeEnsino : ListaUnidadeEnsinoVagaOfertada) {
				try {
					getFacadeFactory().getUnidadeEnsinoFacade().alterarNumeroVagaOfertadaUnidadeEnsino(unidadeEnsino);
				} catch (Exception e) {
					setMensagemDetalhada("msg_erro", e.getMessage());
					setOncompleteModal("");
				}
			}
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
			setOncompleteModal("RichFaces.$('panelImportarPlanilhaVagaOfertada').hide()");
		} else {	
			setMensagemDetalhada("msg_erro", "Arquivo para importação não localizado!");
			setOncompleteModal("");
		}
	}
	
}
