package controle.academico;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.model.SelectItem;

import org.apache.commons.lang3.SerializationUtils;
import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.ColacaoGrauVO;
import negocio.comuns.academico.ControleLivroFolhaReciboVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaUnidadeEnsinoVO;
import negocio.comuns.academico.ControleLivroRegistroDiplomaVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProgramacaoFormaturaAlunoVO;
import negocio.comuns.academico.ProgramacaoFormaturaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.TipoLivroRegistroDiplomaEnum;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.SituacaoColouGrauProgramacaoFormaturaAluno;
import negocio.comuns.utilitarias.dominios.SituacaoControleLivroRegistroDiploma;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.academico.ControleLivroFolhaRecibo;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.jdbc.academico.ControleLivroRegistroDiplomaRel;

@SuppressWarnings("unchecked")
@Controller("ControleLivroRegistroDiplomaControle")
@Scope("viewScope")
@Lazy
public class ControleLivroRegistroDiplomaControle extends SuperControleRelatorio implements Serializable {

	private static final long serialVersionUID = 1L;

	private ControleLivroRegistroDiplomaRel controleLivroRegistroDiplomaRel;
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
	private ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO;
	private ControleLivroFolhaReciboVO controleLivroFolhaReciboVO;
	private ControleLivroFolhaReciboVO controleLivroFolhaReciboExcluirVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private MatriculaVO matriculaVO;
	private TurmaVO turmaVO;
	private ColacaoGrauVO colacaoVO;
	private FuncionarioVO funcionarioVO;
	private CargoVO cargoVO;
	private FuncionarioVO funcionario2VO;
	private CargoVO cargo2VO;
	private String valorConsultaFiltros;
	private Boolean filtroColacao;
	private Boolean filtroTurma;
	private Boolean filtroMatricula;
	private Boolean filtroProgramacaoFormatura;
	private List<ControleLivroFolhaReciboVO> listaControleLivroFolhaRecibo;
	private List<MatriculaVO> listaConsultaMatricula;
	private List<SelectItem> selectItemsCargoFuncionario;
	private List<SelectItem> selectItemsCargoFuncionario2;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemCurso;
	private List<MatriculaVO> listaConsultaAluno;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List<ColacaoGrauVO> listaConsultaColacao;
	private String valorConsultaColacao;
	private String campoConsultaColacao;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private String ano;
	private String semestre;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	private String layoutLivroRegistroDiploma;
	private Boolean ocultarCabecalho;
	private Boolean utilizarLogoUnidadeEnsino;
	private boolean abrirPainelMensagemAbrirLivro;
	private TipoLivroRegistroDiplomaEnum tipoLivroRegistroDiplomaEnum;
	private String nomeCargo1Apresentar;
    private String nomeCargo2Apresentar;
    private List<SelectItem> listaNivelEducacional;
    private List<SelectItem> listaSelectItemFiltros;
    private ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO;
    private String nivelEducacionalSelecionado;
    private ControleLivroFolhaRecibo controleLivroFolhaRecibo;
	private ControleLivroFolhaReciboVO controleLivroFolhaReciboInfo;
	private ControleLivroFolhaReciboVO cancelarObj;
	private ControleLivroFolhaReciboVO reverterCancelamento;
	private String situacaoAnterior;
	private DataModelo dataModeloAluno;
	private int totalAlunos;
	private int totalEmitido;
	private int totalPendente;
	private int totalCancelado;
	private int nrLivroTemp;
	private int nrFolhaReciboTemp;
	private int nrRegistroTemp;
	private int nrMaxFolhaLivroTemp;
	private String tituloAssinaturaFunc1;
    private String tituloAssinaturaFunc2;
    private ProgramacaoFormaturaVO programacaoFormaturaVO;
    private String campoConsultaProgramacao;
    private List<SelectItem> tipoConsultaComboProgramacaoFormatura;
    private String valorConsultaProgramacao;
    private List<ProgramacaoFormaturaVO> listaConsultaProgramacaoFormaturaVOs;
    private List<SelectItem> listaColouGrau;
    private String colouGrau;
    private String reconhecimentoUtilizarLayout3;
    

	public ControleLivroRegistroDiplomaControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		montarListaSelectItemUnidadeEnsino();
		setMensagemID("msg_entre_prmconsulta");
	}

	public String novo() throws Exception {
		removerObjetoMemoria(this);
		setControleLivroRegistroDiplomaVO(new ControleLivroRegistroDiplomaVO());
		setTurmaVO(new TurmaVO());
		setMatriculaVO(new MatriculaVO());
		setColacaoGrauVO(new ColacaoGrauVO());
		setFuncionarioVO(new FuncionarioVO());
		setCargoVO(new CargoVO());
		setListaControleLivroFolhaRecibo(new ArrayList<>(0));
		setListaSelectItemUnidadeEnsino(new ArrayList<>(0));
		setListaSelectItemCurso(new ArrayList<>(0));
		setValorConsultaFiltros("");
		setFiltroColacao(false);
		setFiltroMatricula(false);
		setFiltroTurma(false);
		setAno("");
		setSemestre("");
		inicializarListasSelectItemTodosComboBox();
		getFacadeFactory().getControleLivroRegistroDiplomaUnidadeEnsinoFacade().carregarUnidadeEnsinoNaoSelecionado(getControleLivroRegistroDiplomaVO());
		getControleLivroRegistroDiplomaVO().setUnidadeEnsinoDescricao("");
        carregarLayoutPadrao();
		setMensagemID("msg_entre_dados");
		setAbrirPainelMensagemAbrirLivro(false);
		return Uteis.getCaminhoRedirecionamentoNavegacao("controleLivroRegistroDiplomaForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>ControleLivroRegistroDiploma</code> para alteração. O objeto desta
	 * classe é disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 * 
	 * @throws Exception
	 */
	public String editar()  {
		try {
		ControleLivroRegistroDiplomaVO obj = (ControleLivroRegistroDiplomaVO) context().getExternalContext().getRequestMap().get("controleLivroRegistroDiplomaItens");
		editarLivro(obj);
		setMensagemID("msg_dados_editar");		
		return Uteis.getCaminhoRedirecionamentoNavegacao("controleLivroRegistroDiplomaForm.xhtml");
		}catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("controleLivroRegistroDiplomaCons.xhtml");
		}
	}
	
	private void editarLivro(ControleLivroRegistroDiplomaVO obj) throws Exception {
		obj.setNovoObj(Boolean.FALSE);
		setControleLivroRegistroDiplomaVO(getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		inicializarListasSelectItemTodosComboBox();
		setListaControleLivroFolhaRecibo(getControleLivroFolhaRecibo().consultarDadosControleLivroFolhaRecibo(getControleLivroRegistroDiplomaVO(),  false, getUsuarioLogado()));
		Boolean zerarPorCurso = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getZerarNumeroRegistroPorCurso();
		if (getControleLivroRegistroDiplomaVO().getCodigo() != null && getControleLivroRegistroDiplomaVO().getCodigo() != 0) {
			getControleLivroRegistroDiplomaVO().setNumeroRegistro(getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarMaxNumeroRegistro(getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), zerarPorCurso));
			atualizarSituacaoControleLivroRecibo();
		} else {
			getControleLivroRegistroDiplomaVO().setNumeroRegistro(getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarMaxNumeroRegistro(getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), zerarPorCurso));
			atualizarSituacaoControleLivroRecibo();
		}
        carregarLayoutPadrao();
        setAbrirPainelMensagemAbrirLivro(false);
        consultarFuncionarioPorMatricula();
        consultarFuncionarioPorMatricula2();
        getFacadeFactory().getControleLivroRegistroDiplomaUnidadeEnsinoFacade().carregarUnidadeEnsinoNaoSelecionado(getControleLivroRegistroDiplomaVO());
        verificarTodasUnidadesSelecionadas();
		guardaVariaveis();
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>ControleLivroRegistroDiploma</code>. Caso o objeto seja
	 * novo (ainda não gravado no BD) é acionado a operação
	 * <code>incluir()</code>. Caso contrário é acionado o
	 * <code>alterar()</code>. Se houver alguma inconsistência o objeto não é
	 * gravado, sendo re-apresentado para o usuário juntamente com uma mensagem
	 * de erro.
	 */
	public String gravar() {
		try {
			String matriculaAluno = "";
			List<ControleLivroFolhaReciboVO> listaLivro = getListaControleLivroFolhaRecibo().stream().filter(cl -> !Uteis.isAtributoPreenchido(cl.getVia())).collect(Collectors.toList());
			if(!listaLivro.isEmpty()) {
				for (ControleLivroFolhaReciboVO obj : listaLivro) {
					matriculaAluno += obj.getMatricula().getMatricula() + " - " + obj.getMatricula().getAluno().getNome() + ",  ";
				}
				throw new Exception("A VIA (Controle Livro Folha Recibo) do(s) Aluno(s): " + matriculaAluno + " Não podem ser menor ou igual a 0.");
			}
			if (getControleLivroRegistroDiplomaVO().isNovoObj()) {
				getControleLivroRegistroDiplomaVO().setSituacaoFechadoAberto(SituacaoControleLivroRegistroDiploma.ABERTO.getValor());
				getFacadeFactory().getControleLivroRegistroDiplomaFacade().incluir(getControleLivroRegistroDiplomaVO(), getListaControleLivroFolhaRecibo(), getUsuarioLogado());
			} else {
				getFacadeFactory().getControleLivroRegistroDiplomaFacade().alterar(getControleLivroRegistroDiplomaVO(), getListaControleLivroFolhaRecibo(), getUsuarioLogado());
			}
			persistirLayoutPadrao();
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setAbrirPainelMensagemAbrirLivro(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("controleLivroRegistroDiplomaForm.xhtml");
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>ControleLivroRegistroDiplomaVO</code> Após a exclusão ela
	 * automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			excluirListaAlunosLivroFolhaReciboMatricula();
			getFacadeFactory().getControleLivroRegistroDiplomaFacade().excluir(getControleLivroRegistroDiplomaVO(), getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} 
		return Uteis.getCaminhoRedirecionamentoNavegacao("controleLivroRegistroDiplomaForm.xhtml");
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * ControleLivroRegistroDiplomaCons.jsp. Define o tipo de consulta a ser
	 * executada, por meio de ComboBox denominado campoConsulta, disponivel
	 * neste mesmo JSP. Como resultado, disponibiliza um List com os objetos
	 * selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List<ControleLivroRegistroDiplomaVO> objs = new ArrayList<>(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				if (getControleConsulta().getValorConsulta().trim() != null || !getControleConsulta().getValorConsulta().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta().trim());
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("curso")) {
				objs = getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorNomeCurso(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("aluno")) {
				objs = getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorNomeAluno(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				objs = getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorMatricula(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("tipoLivroRegistro")) {
				objs = getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorTipoLivroRegistro(getTipoLivroRegistroDiplomaEnum().name(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("numeroRegistro")) {
				objs = getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorNumeroLivroRegistro(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("registroAcademico")) {
				objs = getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorRegistroAcademico(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			totalizadorAlunosLivro();
			atualizarSituacaoControleLivroRecibo();
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

		return Uteis.getCaminhoRedirecionamentoNavegacao("controleLivroRegistroDiplomaCons.xhtml");
	}
	
	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<>(0);
			if (getValorConsultaAluno().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				setDataModeloAluno(new DataModelo());
				return;
			}
			List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> listaUnidadesSelecionadas = getControleLivroRegistroDiplomaVO().getControleLivroRegistroDiplomaUnidadeEnsinoVOs().stream().filter(uni -> uni.getSelecionado()).collect(Collectors.toList());
			getDataModeloAluno().setLimitePorPagina(10);
			getDataModeloAluno().setPaginaAtual(1);
			getDataModeloAluno().setOffset(0);
			if (getCampoConsultaAluno().equals("nomePessoa")) {			
				getDataModeloAluno().setValorConsulta(getValorConsultaAluno());				
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getDataModeloAluno(), listaUnidadesSelecionadas, false,"", getControleLivroRegistroDiplomaVO().getNivelEducacional(), getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), getUsuarioLogado());
				getDataModeloAluno().setListaConsulta(objs);
			}
			if (getCampoConsultaAluno().equals("matricula")) {				
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaAlunoPorDataModelo(getValorConsultaAluno(), listaUnidadesSelecionadas, getDataModeloAluno(), false,"", getControleLivroRegistroDiplomaVO().getNivelEducacional(), getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), getUsuarioLogado());
				getDataModeloAluno().setListaConsulta(objs);
			}
			if (getCampoConsultaAluno().equals("registroAcademico")) {			
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorRegistroAcademicoAlunoPorDataModelo(getValorConsultaAluno(), listaUnidadesSelecionadas, getDataModeloAluno(), false,"", getControleLivroRegistroDiplomaVO().getNivelEducacional(), getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), getUsuarioLogado());
				getDataModeloAluno().setListaConsulta(objs);		
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List<TurmaVO> objs = new ArrayList<>(0);
			List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> listaUnidadesSelecionadas = getControleLivroRegistroDiplomaVO().getControleLivroRegistroDiplomaUnidadeEnsinoVOs().stream().filter(uni -> uni.getSelecionado()).collect(Collectors.toList());
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), listaUnidadesSelecionadas, getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getControleLivroRegistroDiplomaVO().getNivelEducacional(), getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarColacao() {
		try {
			super.consultar();
			List<ColacaoGrauVO> objs = new ArrayList<>(0);
			if (getCampoConsultaColacao().equals("titulo")) {
				objs = getFacadeFactory().getColacaoGrauFacade().consultarPorTitulo(getValorConsultaColacao(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaColacao(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getIsDesabilitarCamposEdicao() {
		if (getControleLivroRegistroDiplomaVO().getSituacaoFechadoAberto().equals(SituacaoControleLivroRegistroDiploma.FECHADO.getValor())) {
			return true;
		} else {
			return false;
		}
	}

	public void fecharLivroRegistrosDiploma() throws Exception {

		if (getControleLivroRegistroDiplomaVO().getSituacaoFechadoAberto().equals(SituacaoControleLivroRegistroDiploma.ABERTO.getValor())) {
			try {

				getControleLivroRegistroDiplomaVO().setSituacaoFechadoAberto(SituacaoControleLivroRegistroDiploma.FECHADO.getValor().toString());
				getFacadeFactory().getControleLivroRegistroDiplomaFacade().alterar(getControleLivroRegistroDiplomaVO(), getListaControleLivroFolhaRecibo(), getUsuarioLogado());

				getControleLivroRegistroDiplomaVO().setNrLivro(getControleLivroRegistroDiplomaVO().getNrLivro() + 1);
				getControleLivroRegistroDiplomaVO().setNrFolhaRecibo((0));
				getControleLivroRegistroDiplomaVO().setSituacaoFechadoAberto(SituacaoControleLivroRegistroDiploma.ABERTO.getValor().toString());
				getFacadeFactory().getControleLivroRegistroDiplomaFacade().incluir(getControleLivroRegistroDiplomaVO(), new ArrayList<ControleLivroFolhaReciboVO>(), getUsuarioLogado());
				novo();

			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}

		}
	}

	public void imprimirPDF() {
		try {   
			persistirLayoutPadrao();
			getControleLivroRegistroDiplomaRel().setCargoFuncionario(getCargoVO());
			getControleLivroRegistroDiplomaRel().setFuncionario(getFuncionarioVO());
			getControleLivroRegistroDiplomaRel().setTituloFuncionario(getTituloAssinaturaFunc1());
			getControleLivroRegistroDiplomaRel().setCargoFuncionario2(getCargo2VO());
			getControleLivroRegistroDiplomaRel().setFuncionario2(getFuncionario2VO());
			getControleLivroRegistroDiplomaRel().setTituloFuncionario2(getTituloAssinaturaFunc2());
			getControleLivroRegistroDiplomaRel().setDescricaoFiltros("ApresentarCabecalho");
			if (getOcultarCabecalho()) {
				getControleLivroRegistroDiplomaRel().setDescricaoFiltros("ocultarCabecalho");
			}
			String nomeEntidade = getControleLivroRegistroDiplomaVO().getUnidadeEnsinoDescricao();
			if (nomeEntidade.length() > 50) {
				nomeEntidade = getControleLivroRegistroDiplomaVO().getUnidadeEnsinoDescricao().substring(0, 49);
				nomeEntidade += "...";
			}
			String design = ControleLivroRegistroDiplomaRel.getDesignIReportRelatorio();
			if (getLayoutLivroRegistroDiploma().equals("2")) {
				design = ControleLivroRegistroDiplomaRel.getDesignIReportRelatorio2();
			}

			if (getLayoutLivroRegistroDiploma().equals("3")) {
				design = ControleLivroRegistroDiplomaRel.getDesignIReportRelatorio3();
			}

			if (getLayoutLivroRegistroDiploma().equals("4")) {
				design = ControleLivroRegistroDiplomaRel.getDesignIReportRelatorio4();
			}

			if (getLayoutLivroRegistroDiploma().equals("5")) {
				design = ControleLivroRegistroDiplomaRel.getDesignIReportRelatorio5();
			}
			String titulo = "LIVRO DE CONTROLE DE REGISTROS DE CERTIFICADOS";
			if (getLayoutLivroRegistroDiploma().equals("2")) {
				titulo = "SECRETARIA DE EXPEDIÇÃO E REGISTRO DE DIPLOMAS";
			}
			boolean consultarCargo1 = true;
			boolean consultarCargo2 = true;
			if (getLayoutLivroRegistroDiploma().equals("3")) {
				for (FuncionarioCargoVO obj : getFuncionarioVO().getFuncionarioCargoVOs()) {
					if (obj.getCargo().getCodigo().intValue() == getCargoVO().getCodigo().intValue()) {
						if (obj.getCargoAtual().getCodigo().intValue() > 0 ) {
							getControleLivroRegistroDiplomaRel().setCargoFuncionario(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(obj.getCargoAtual().getCodigo(), false,Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
							consultarCargo1 = false;
						}
						if (!getNomeCargo1Apresentar().trim().isEmpty()) {
							getControleLivroRegistroDiplomaRel().getCargoFuncionario().setNome(getNomeCargo1Apresentar());
						}
					}
					if (obj.getCargo().getCodigo().intValue() == getCargo2VO().getCodigo().intValue()) {
						if (obj.getCargoAtual().getCodigo().intValue() > 0 ) {
							getControleLivroRegistroDiplomaRel().setCargoFuncionario2(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(obj.getCargoAtual().getCodigo(), false,Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
							consultarCargo2 = false;
						}
						if (!getNomeCargo2Apresentar().trim().isEmpty()) {
							getControleLivroRegistroDiplomaRel().getCargoFuncionario2().setNome(getNomeCargo2Apresentar());
						}
					}
				}
				
				titulo = "SECRETARIA DE EXPEDIÇÃO E REGISTRO DE DIPLOMAS";
			}
			if (getLayoutLivroRegistroDiploma().equals("5")) {
				titulo = "SECRETARIA DE EXPEDIÇÃO E REGISTRO DE DIPLOMAS";
			}
			getSuperParametroRelVO().adicionarParametro("nomeCargoApresentarCargo2", getNomeCargo2Apresentar());
			getSuperParametroRelVO().adicionarParametro("nomeCargoApresentarCargo1", getNomeCargo1Apresentar());
//			getSuperParametroRelVO().adicionarParametro("utilizarLogoUnidadeEnsino", getUtilizarLogoUnidadeEnsino());
			getSuperParametroRelVO().setTituloRelatorio(titulo);
			getSuperParametroRelVO().setNomeEmpresa(nomeEntidade);
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setSubReport_Dir(ControleLivroRegistroDiplomaRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setListaObjetos(getControleLivroRegistroDiplomaRel().criarObjeto(getControleLivroRegistroDiplomaVO(), consultarCargo1, consultarCargo2, 
					getLayoutLivroRegistroDiploma().equals("3") ? getReconhecimentoUtilizarLayout3() : "", getUsuarioLogado(), getLayoutLivroRegistroDiploma()));
			getSuperParametroRelVO().setCaminhoBaseRelatorio(ControleLivroRegistroDiplomaRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
//			if (Uteis.isAtributoPreenchido(getControleLivroRegistroDiplomaVO().getUnidadeEnsino())) {
//				getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getControleLivroRegistroDiplomaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
//			}
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
            getSuperParametroRelVO().setFiltros(getControleLivroRegistroDiplomaRel().getDescricaoFiltros());
//            UnidadeEnsinoVO ue = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorChavePrimariaDadosBasicosBoleto(getControleLivroRegistroDiplomaVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado());
//			if (ue.getExisteLogoRelatorio()) {
//				String urlLogoUnidadeEnsinoRelatorio = ue.getCaminhoBaseLogoRelatorio().replaceAll("\\\\", "/") + "/" + ue.getNomeArquivoLogoRelatorio();
//				String urlLogo = getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + urlLogoUnidadeEnsinoRelatorio;
//				getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", urlLogo);
//			} else {
//			}	
			getSuperParametroRelVO().getParametros().put("logoPadraoRelatorio", getCaminhoPastaWeb() + File.separator + "resources" + File.separator + "imagens" + File.separator + "logoPadraoRelatorio.png");
            realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");
			getControleLivroRegistroDiplomaRel().inicializarParametros();
			inicializarListasSelectItemTodosComboBox();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	
	public void imprimirExcel() {
		try {
			persistirLayoutPadrao();
			getControleLivroRegistroDiplomaRel().setCargoFuncionario(getCargoVO());
			getControleLivroRegistroDiplomaRel().setFuncionario(getFuncionarioVO());
			getControleLivroRegistroDiplomaRel().setCargoFuncionario2(getCargo2VO());
			getControleLivroRegistroDiplomaRel().setFuncionario2(getFuncionario2VO());
			getControleLivroRegistroDiplomaRel().setDescricaoFiltros("ApresentarCabecalho");
			if (getOcultarCabecalho()) {
				getControleLivroRegistroDiplomaRel().setDescricaoFiltros("ocultarCabecalho");
			}
			String nomeEntidade = getControleLivroRegistroDiplomaVO().getUnidadeEnsinoDescricao();
			if (nomeEntidade.length() > 50) {
				nomeEntidade = getControleLivroRegistroDiplomaVO().getUnidadeEnsinoDescricao().substring(0, 49);
				nomeEntidade += "...";
			}
			String design = ControleLivroRegistroDiplomaRel.getDesignIReportRelatorio5Excel();
			
			String titulo = "LIVRO DE CONTROLE DE REGISTROS DE CERTIFICADOS";
			getSuperParametroRelVO().setTituloRelatorio(titulo);
			getSuperParametroRelVO().setNomeEmpresa(nomeEntidade);
			getSuperParametroRelVO().setNomeDesignIreport(design);
			getSuperParametroRelVO().setSubReport_Dir(ControleLivroRegistroDiplomaRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
			getSuperParametroRelVO().setListaObjetos(getControleLivroRegistroDiplomaRel().criarObjeto(getControleLivroRegistroDiplomaVO(), true, true, 
					getLayoutLivroRegistroDiploma().equals("3") ? getReconhecimentoUtilizarLayout3() : "", getUsuarioLogado(), getLayoutLivroRegistroDiploma()));
			getSuperParametroRelVO().setCaminhoBaseRelatorio(ControleLivroRegistroDiplomaRel.getCaminhoBaseRelatorio());
			getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
//			if (Uteis.isAtributoPreenchido(getControleLivroRegistroDiplomaVO().getUnidadeEnsino())) {
//				getSuperParametroRelVO().setUnidadeEnsino((getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getControleLivroRegistroDiplomaVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado())).getNome());
//			}
			getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
            getSuperParametroRelVO().setFiltros(getControleLivroRegistroDiplomaRel().getDescricaoFiltros());
			realizarImpressaoRelatorio();
			setMensagemID("msg_relatorio_ok");
			getControleLivroRegistroDiplomaRel().inicializarParametros();
//			removerObjetoMemoria(this);
			inicializarListasSelectItemTodosComboBox();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}


	public void consultarFuncionarioPorMatricula() throws Exception {
		try {
			setFuncionarioVO(consultarFuncionarioPorMatriculaFuncionario(getFuncionarioVO().getMatricula()));
			setSelectItemsCargoFuncionario(montarComboCargoFuncionario(getFuncionarioVO().getFuncionarioCargoVOs()));
			if ((getSelectItemsCargoFuncionario() != null) && (getSelectItemsCargoFuncionario().size() > 0)) {
				setCargoVO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(((Integer) getSelectItemsCargoFuncionario().get(0).getValue()), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			limparMensagem();
		} catch (Exception e) {
			setFuncionarioVO(new FuncionarioVO());
			setCargoVO(new CargoVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
        
	public void consultarFuncionarioPorCodigo() throws Exception {
		try {
			setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(getFuncionarioVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if (!Uteis.isAtributoPreenchido(getFuncionarioVO())) {
				return;
			}
			setSelectItemsCargoFuncionario(montarComboCargoFuncionario(getFuncionarioVO().getFuncionarioCargoVOs()));
			if ((getSelectItemsCargoFuncionario() != null) && (getSelectItemsCargoFuncionario().size() > 0)) {
				setCargoVO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(((Integer) getSelectItemsCargoFuncionario().get(0).getValue()), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarFuncionarioPorCodigo2() throws Exception {
		try {
			setFuncionario2VO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(getFuncionario2VO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if (!Uteis.isAtributoPreenchido(getFuncionario2VO())) {
				return;
			}
			setSelectItemsCargoFuncionario2(montarComboCargoFuncionario(getFuncionario2VO().getFuncionarioCargoVOs()));
			if ((getSelectItemsCargoFuncionario2() != null) && (getSelectItemsCargoFuncionario2().size() > 0)) {
				setCargo2VO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(((Integer) getSelectItemsCargoFuncionario2().get(0).getValue()), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFuncionario() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		setFuncionarioVO(obj);
		consultarFuncionarioPorMatricula();
	}
	
	public void consultarFuncionario() {
		try {
			List<FuncionarioVO> objs = new ArrayList<>(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");

			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			executarMetodoControle(ExpedicaoDiplomaControle.class.getSimpleName(), "setMensagemID", "msg_dados_consultados");
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public FuncionarioVO consultarFuncionarioPorMatriculaFuncionario(String matricula) throws Exception {
		FuncionarioVO funcionarioVO = null;
		try {
			funcionarioVO = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(matricula, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(funcionarioVO)) {
				return funcionarioVO;
			} else {
				setMensagemDetalhada("msg_erro", "Funcionário de matrícula " + matricula + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {			
			//throw e;
			//setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new FuncionarioVO();
	}

	public List<SelectItem> montarComboCargoFuncionario(List<FuncionarioCargoVO> cargos) throws Exception {
		if (cargos != null && !cargos.isEmpty()) {
			List<SelectItem> selectItems = new ArrayList<SelectItem>();
			for (FuncionarioCargoVO funcionarioCargoVO : cargos) {
				selectItems.add(new SelectItem(funcionarioCargoVO.getCargo().getCodigo(), funcionarioCargoVO.getCargo().getNome() + " - " + funcionarioCargoVO.getUnidade().getNome()));
			}
			return selectItems;
		} else {
			setMensagemDetalhada("O Funcionário selecionado não possui cargo configurado");
		}
		return null;
	}

	public void limparDadosFuncionario() {
		setFuncionarioVO(new FuncionarioVO());
	}

    public void limparDadosFuncionario2() {
		setFuncionario2VO(new FuncionarioVO());
	}

	public void limparDadosTurma() {
		setTurmaVO(new TurmaVO());
	}

	public void limparDadosColacao() {
		setColacaoGrauVO(new ColacaoGrauVO());
	}

	public void limparDados() {
		setMatriculaVO(new MatriculaVO());
	}

	public boolean isApresentarCampoCargoFuncionario() {
		return isApresentarCampos() && Uteis.isAtributoPreenchido(getFuncionarioVO());
	}

	public List<SelectItem> getListaSelectItemSemestre() {
		List<SelectItem> lista = new ArrayList<>(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("aluno", "Aluno"));
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("registroAcademico", "Registro Acadêmico"));
		itens.add(new SelectItem("unidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("tipoLivroRegistro", "Tipo Livro Registro"));
		itens.add(new SelectItem("numeroRegistro", "Número de Registro"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<>(0);

		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboMatricula() {
		List<SelectItem> itens = new ArrayList<SelectItem>();
		itens.add(new SelectItem("nomePessoa", "Nome Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("registroAcademico",  "Registro Acadêmico"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboColacao() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("titulo", "Titulo"));
		return itens;
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(obj);
			List<ControleLivroFolhaReciboVO> controleLivroFolhaReciboVOs = getFacadeFactory().getControleLivroFolhaReciboFacade().consultarPorMatriculaCursoTurmaSituacaoControleLivroRegistroDiploma(getMatriculaVO().getMatricula(), getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), "AB", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getControleLivroRegistroDiplomaVO().getNrLivro());
			if (!controleLivroFolhaReciboVOs.isEmpty()) {
				int index = 0;
				for (ControleLivroFolhaReciboVO reciboVO : controleLivroFolhaReciboVOs) {
						getListaControleLivroFolhaRecibo().set(index, reciboVO);
					index++;
				}
			}
			getControleLivroRegistroDiplomaVO().setCurso(obj.getCurso());
			setCampoConsultaTurma("");
			setValorConsultaTurma("");
			if(getTurmaVO().getIntegral()) {
				setAno("");
			}
			if(getTurmaVO().getAnual()) {
				setSemestre("");
			}
			setListaConsultaTurma(new ArrayList<>(0));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarMatricula() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		setMatriculaVO(obj);
		setCampoConsultaAluno("");
		setValorConsultaAluno("");
		setListaConsultaAluno(new ArrayList<>(0));
	}

	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<>(0);
			List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> listaUnidade = getControleLivroRegistroDiplomaVO().getControleLivroRegistroDiplomaUnidadeEnsinoVOs().stream().filter(uni -> uni.getSelecionado()).collect(Collectors.toList());
			if (Uteis.isAtributoPreenchido(listaUnidade)) {
				if (getCampoConsultaCurso().equals("nome")) {
					if(getControleLivroRegistroDiplomaVO().getTipoLivroRegistroDiplomaEnum().name().equals("CERTIFICADO") && !getControleLivroRegistroDiplomaVO().getNivelEducacional().equals("0") || getControleLivroRegistroDiplomaVO().getTipoLivroRegistroDiplomaEnum().name().equals("DIPLOMA")) {
						objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(getValorConsultaCurso(), listaUnidade, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
					}else {
						throw new ConsistirException(UteisJSF.internacionalizar("msg_ControleLivroRegistroDiploma_nivelEducacional"));
					}
				}

				setListaConsultaCurso(objs);
				setMensagemID("msg_dados_consultados");
			}else {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getValorConsultaCurso(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado());
				setListaConsultaCurso(objs);
			}
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {			
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getControleLivroRegistroDiplomaVO().setCurso(obj);

			ControleLivroRegistroDiplomaVO clrd = getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorCodigoCurso(getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), getControleLivroRegistroDiplomaVO().getNivelEducacional(),  getControleLivroRegistroDiplomaVO().getTipoLivroRegistroDiplomaEnum(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if(Uteis.isAtributoPreenchido(clrd)) {
				editarLivro(clrd);
			}
			Boolean zerarPorCurso = getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getZerarNumeroRegistroPorCurso();
			if (getControleLivroRegistroDiplomaVO().getCodigo() != null && getControleLivroRegistroDiplomaVO().getCodigo() != 0) {
				setListaControleLivroFolhaRecibo(getFacadeFactory().getControleLivroFolhaReciboFacade().consultarDadosControleLivroFolhaRecibo(getControleLivroRegistroDiplomaVO(), false, getUsuarioLogado()));
				getControleLivroRegistroDiplomaVO().setNumeroRegistro(getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarMaxNumeroRegistro(getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), zerarPorCurso));
			} else {
				getControleLivroRegistroDiplomaVO().setNumeroRegistro(getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarMaxNumeroRegistro(getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), zerarPorCurso));
			}
//			setUnidadeEnsinoVO(getControleLivroRegistroDiplomaVO().getUnidadeEnsino());
			limparDados();
			limparDadosTurma();
			limparDadosColacao();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCurso() throws Exception {
		try {
			getControleLivroRegistroDiplomaVO().setCurso(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public void selecionarColacao() {
		ColacaoGrauVO obj = (ColacaoGrauVO) context().getExternalContext().getRequestMap().get("colacaoItens");
		setColacaoGrauVO(obj);
		setCampoConsultaColacao("");
		setValorConsultaColacao("");
		setListaConsultaColacao(new ArrayList<>(0));
	}

//	public void montarNrlivroRegistro() throws Exception {
//		setControleLivroRegistroDiplomaVO(getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorUnidadeEnsinoCurso(getUnidadeEnsinoVO().getCodigo(), getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), SituacaoControleLivroRegistroDiploma.ABERTO.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
//		if (getControleLivroRegistroDiplomaVO().getNrLivro() == 0) {
//			setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoUnidadeEnsino(this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
//			getControleLivroRegistroDiplomaVO().setNrLivro(getFacadeFactory().getControleLivroRegistroDiplomaFacade().obterNumeroLivroRegistro(getUnidadeEnsinoVO().getCodigo(), getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), getUsuarioLogado()) + 1);
//			getControleLivroRegistroDiplomaVO().setNrMaximoFolhasLivro(getConfiguracaoGeralSistemaVO().getNrMaximoFolhaRecibo());
//		}
//		setListaControleLivroFolhaRecibo(getFacadeFactory().getControleLivroFolhaReciboFacade().consultarDadosControleLivroFolhaRecibo(getControleLivroRegistroDiplomaVO(), false, getUsuarioLogado()));
//	}

	public void montarListaSelectItemCurso() throws Exception {
		try {
			List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> listaUnidade = getControleLivroRegistroDiplomaVO().getControleLivroRegistroDiplomaUnidadeEnsinoVOs().stream().filter(uni -> uni.getSelecionado()).collect(Collectors.toList());
			List<CursoVO> resultadoConsulta = consultarCursoPelaUnidadeEnsino(listaUnidade);
			setListaSelectItemCurso(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<CursoVO> consultarCursoPelaUnidadeEnsino(List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> controleLivroRegistroDiplomaUnidadeEnsinoVOs) throws Exception {
		List<CursoVO> lista = getFacadeFactory().getCursoFacade().consultarPorUnidadesEnsinos(controleLivroRegistroDiplomaUnidadeEnsinoVOs, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 * 
	 * @throws Exception
	 */
	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemFiltros();
	}

	public void removerControleLivroFolhaRecibo() throws Exception {
		try {
			//ControleLivroFolhaReciboVO obj = (ControleLivroFolhaReciboVO) getRequestMap().get("controleLivroFolhaRecibo");
			getControleLivroFolhaReciboVO().setDocumentacaoControleLivroReciboVOs(getListaControleLivroFolhaRecibo());
			getControleLivroFolhaReciboVO().excluirObjControleLivroReciboVOs(getControleLivroFolhaReciboExcluirVO());
			if(Uteis.isAtributoPreenchido(getControleLivroFolhaReciboExcluirVO().getCodigo())){
				getFacadeFactory().getControleLivroFolhaReciboFacade().excluir(getControleLivroFolhaReciboExcluirVO(), getUsuarioLogado());
			}
			getControleLivroRegistroDiplomaVO().setNrFolhaRecibo(getListaControleLivroFolhaRecibo().size());
			setMensagemID("msg_dados_excluidos");
			atualizarSituacaoControleLivroRecibo();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally{
			setControleLivroFolhaReciboExcluirVO(null);
		}
	}

	public void selecionarFiltro() throws Exception {
		setarFalseNosFiltros();
		setarNewNosObjetosDeConsulta();
		if (getValorConsultaFiltros().equals("colacao")) {
			setFiltroColacao(true);
		} else if (getValorConsultaFiltros().equals("turma")) {
			setFiltroTurma(true);
		} else if (getValorConsultaFiltros().equals("matricula") || getValorConsultaFiltros().equals("registroAcademico")) {
			getControleConsulta().setCampoConsulta(getValorConsultaFiltros());
			getControleConsulta().setValorConsulta("");
			setFiltroMatricula(true);
		} else if (getValorConsultaFiltros().equals("programacaoFormatura")) {
			setFiltroProgramacaoFormatura(true);
		}
	}

	private void setarFalseNosFiltros() {
		setFiltroColacao(false);
		setFiltroMatricula(false);
		setFiltroTurma(false);
		setFiltroProgramacaoFormatura(false);
	}

	private void setarNewNosObjetosDeConsulta() {
		setTurmaVO(new TurmaVO());
		setColacaoGrauVO(new ColacaoGrauVO());
		setMatriculaVO(new MatriculaVO());
		setProgramacaoFormaturaVO(new ProgramacaoFormaturaVO());
	}

	public void montarListaSelectItemFiltros() {
		if(!Uteis.isAtributoPreenchido(getListaSelectItemFiltros())) {
			getListaSelectItemFiltros().add(new SelectItem("", ""));
			getListaSelectItemFiltros().add(new SelectItem("matricula", "Matrícula"));
			getListaSelectItemFiltros().add(new SelectItem("turma", "Turma"));
			getListaSelectItemFiltros().add(new SelectItem("colacao", "Colação"));		
			getListaSelectItemFiltros().add(new SelectItem("registroAcademico", "Registro Acadêmico"));
			getListaSelectItemFiltros().add(new SelectItem("programacaoFormatura", "Programação de Formatura"));
		}
	}	
	
	public List<SelectItem> getListaSelectItemFiltros() {
		if(listaSelectItemFiltros == null) {
			listaSelectItemFiltros = new ArrayList<SelectItem>();
		}
		return listaSelectItemFiltros;
	}

	public void setListaSelectItemFiltros(List<SelectItem> listaSelectItemFiltros) {
		this.listaSelectItemFiltros = listaSelectItemFiltros;
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	@SuppressWarnings({ "deprecation", "static-access" })
	public void adicionarLivroFolhaReciboMatricula() throws Exception {
		listaConsultaMatricula = new ArrayList<MatriculaVO>(0);
		List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> listaUnidade = getControleLivroRegistroDiplomaVO().getControleLivroRegistroDiplomaUnidadeEnsinoVOs().stream().filter(uni -> uni.getSelecionado()).collect(Collectors.toList());
		try {
			getControleLivroRegistroDiplomaVO().validarDados(getControleLivroRegistroDiplomaVO());
			if (getFiltroMatricula()) {
				if (!getMatriculaVO().getMatricula().equals("")) {
					listaConsultaMatricula.add(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimariaEUnidadesEnsinos(getMatriculaVO().getMatricula(), listaUnidade, NivelMontarDados.TODOS, getUsuarioLogado()));
				} else {
					throw new ConsistirException("Informe Matricula Aluno.");
				}
			} else if(Uteis.isAtributoPreenchido(getControleLivroRegistroDiplomaVO().getTipoLivroRegistroDiplomaEnum()) && getControleLivroRegistroDiplomaVO().getTipoLivroRegistroDiplomaEnum().name().equals("DIPLOMA") && !Uteis.isAtributoPreenchido(getControleLivroRegistroDiplomaVO().getCurso())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_ControleLivroRegistroDiploma_curso"));
			} else if(Uteis.isAtributoPreenchido(getControleLivroRegistroDiplomaVO().getTipoLivroRegistroDiplomaEnum()) && getControleLivroRegistroDiplomaVO().getTipoLivroRegistroDiplomaEnum().name().equals("CERTIFICADO") && !Uteis.isAtributoPreenchido(getControleLivroRegistroDiplomaVO().getNivelEducacional())) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_ControleLivroRegistroDiploma_nivelEducacional"));
			} else if( getControleLivroRegistroDiplomaVO().getNrLivro() == 0 ) {
				throw new ConsistirException(UteisJSF.internacionalizar("O campo  Livro deve ser diferente de zero."));
			} else if(getControleLivroRegistroDiplomaVO().getNrFolhaRecibo() == 0) {
				throw new ConsistirException(UteisJSF.internacionalizar("O campo Folha/Recibo deve ser diferente de zero."));
			} else if(getControleLivroRegistroDiplomaVO().getNumeroRegistro() == null) {
				throw new ConsistirException(UteisJSF.internacionalizar("O campo Último Registro deve ser diferente de zero."));
			} else if(getControleLivroRegistroDiplomaVO().getNrMaximoFolhasLivro() == 0) {
				throw new ConsistirException(UteisJSF.internacionalizar("O campo Nº Máximo de Folhas deve ser diferente de zero."));
			} else if (getFiltroTurma()) {
				if (!getTurmaVO().getCurso().getNivelEducacionalPosGraduacao()) {
					if (getTurmaVO().getCodigo() == 0 && getAno().equals("") && getSemestre().equals("")) {
						throw new ConsistirException("Informe todos os dados.");
					}
				} else {
					if (getTurmaVO().getCodigo() == 0) {
						throw new ConsistirException("Informe todos os dados.");
					}
				}
				listaConsultaMatricula = (getFacadeFactory().getMatriculaFacade().consultarPorCursoTurmaAnoSemestre(getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), getTurmaVO().getCodigo(), getAno(), getSemestre(), "", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getControleLivroRegistroDiplomaVO().getNivelEducacional(), getUsuarioLogado()));
			} else if (getFiltroColacao()) {
				if (getColacaoGrauVO().getCodigo() != 0) {				
					listaConsultaMatricula.addAll(getFacadeFactory().getMatriculaFacade().consultarPorColacaoGrau(getColacaoGrauVO().getCodigo(), getColouGrau(), getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), listaUnidade, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
	
				} else {
					throw new ConsistirException("Informe a Colacao de Grau.");
				}
			} else if (getFiltroProgramacaoFormatura()) {
				listaConsultaMatricula.addAll(getFacadeFactory().getMatriculaFacade().consultarPorProgramacaoFormatura(getProgramacaoFormaturaVO().getCodigo(), getColouGrau(), getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), listaUnidade, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
			}

			getFacadeFactory().getControleLivroRegistroDiplomaFacade().validarNumeroRegistroMenorUltimo(getControleLivroRegistroDiplomaVO(), getListaControleLivroFolhaRecibo());
			getFacadeFactory().getControleLivroRegistroDiplomaFacade().validarFolhaReciboAtualMenorUltimo(getControleLivroRegistroDiplomaVO(), getListaControleLivroFolhaRecibo());

			if (listaConsultaMatricula.isEmpty()) {
				throw new ConsistirException("Não foram encontrados alunos com os parâmetros escolhidos!");
			}
			String msgRetorno = getFacadeFactory().getControleLivroFolhaReciboFacade().adicionarListaLivroFolhaReciboMatricula(getControleLivroRegistroDiplomaVO(), new UnidadeEnsinoVO(), getControleLivroRegistroDiplomaVO().getCurso(), listaConsultaMatricula, getListaControleLivroFolhaRecibo(), programacaoFormaturaAlunoVO, getFiltroProgramacaoFormatura(), getUsuarioLogado());
			if(Uteis.isAtributoPreenchido(msgRetorno)) {
				setMensagemDetalhada("msg_dados_adicionados", msgRetorno);
			}else {
				setMensagemID("msg_dados_adicionados");
			}

			totalizadorAlunosLivro();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gravarListaAlunosLivroFolhaReciboMatricula() throws Exception {
		Iterator<ControleLivroFolhaReciboVO> i = getListaControleLivroFolhaRecibo().iterator();
		while (i.hasNext()) {
			ControleLivroFolhaReciboVO obj = (ControleLivroFolhaReciboVO) i.next();
			getFacadeFactory().getControleLivroFolhaReciboFacade().incluir(obj, getUsuarioLogado());	
		}
	}

	public void excluirListaAlunosLivroFolhaReciboMatricula() throws Exception {
		Iterator<ControleLivroFolhaReciboVO> i = getListaControleLivroFolhaRecibo().iterator();
		while (i.hasNext()) {
			ControleLivroFolhaReciboVO obj = (ControleLivroFolhaReciboVO) i.next();
			getFacadeFactory().getControleLivroFolhaReciboFacade().excluir(obj, getUsuarioLogado());
		}
	}

	public List<ControleLivroFolhaReciboVO> getListaControleLivroFolhaRecibo() {
		if (listaControleLivroFolhaRecibo == null) {
			listaControleLivroFolhaRecibo = new ArrayList<>(0);
		}
		return listaControleLivroFolhaRecibo;
	}

	public void setListaControleLivroFolhaRecibo(List<ControleLivroFolhaReciboVO> listaControleLivroFolhaRecibo) {
		this.listaControleLivroFolhaRecibo = listaControleLivroFolhaRecibo;
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<>(0));
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("controleLivroRegistroDiplomaCons.xhtml");
	}

	public List<SelectItem> getListaControleLivroRegistroDiplomaSituacaoAbertoFechado() throws Exception {
		List<SelectItem> opcoes = new ArrayList<SelectItem>();
		opcoes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoControleLivroRegistroDiploma.class, false);
		return opcoes;
	}

	

	public boolean getIsApresentarAno() {
		if (!getTurmaVO().getCodigo().equals(0)) {
			if (getTurmaVO().getSemestral() || getTurmaVO().getAnual()) {
				setAno(getAno());
				return true;
			} else {
				setAno("");
				setSemestre("");
				return false;
			}
		}
		return true;
	}

	public boolean getIsApresentarSemestre() {
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
	}

	public boolean verificarContemMatricula(List<ControleLivroFolhaReciboVO> controleLivroFolhaReciboVOs, MatriculaVO matriculaVO) {
		if (matriculaVO == null) {
			for (int i = 0; i < controleLivroFolhaReciboVOs.size(); i++)
				if (controleLivroFolhaReciboVOs.get(i) == null)
					return i >= 0;
		} else {
			for (int i = 0; i < controleLivroFolhaReciboVOs.size(); i++)
				if (!Uteis.isAtributoPreenchido(controleLivroFolhaReciboVOs.get(i).getCodigo()) && matriculaVO.getMatricula().equals(controleLivroFolhaReciboVOs.get(i).getMatricula().getMatricula()))
					return i >= 0;
		}
		return false;
	}

	public void consultarAlunoPorMatricula() {
		try {
			List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> listaUnidadesSelecionadas = getControleLivroRegistroDiplomaVO().getControleLivroRegistroDiplomaUnidadeEnsinoVOs().stream().filter(uni -> uni.getSelecionado()).collect(Collectors.toList());
			MatriculaVO objAluno = null ;
			if(getValorConsultaFiltros().equals("matricula")) {
				 objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimariaEUnidadesSelecionadas(getMatriculaVO().getMatricula(), listaUnidadesSelecionadas, getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(0), getUsuarioLogado());
				if (objAluno.getMatricula().equals("") ) {
					throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado, verifique se o mesmo está vinculado a unidade de ensino"+ (getControleLivroRegistroDiplomaVO().getTipoLivroRegistroDiplomaEnum().equals(TipoLivroRegistroDiplomaEnum.CERTIFICADO) ? ", nivel educacional" : "") +" e curso selecionados.");
				}
			}else if(getValorConsultaFiltros().equals("registroAcademico")) {
				 objAluno = getFacadeFactory().getMatriculaFacade().consultarMatriculaPorRegistroAcademico(getMatriculaVO().getAluno().getRegistroAcademico(), listaUnidadesSelecionadas, getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(0), getUsuarioLogado());
				if (objAluno.getMatricula().equals("") ) {
					throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado, verifique se o mesmo está vinculado a unidade de ensino"+ (getControleLivroRegistroDiplomaVO().getTipoLivroRegistroDiplomaEnum().equals(TipoLivroRegistroDiplomaEnum.CERTIFICADO) ? ", nivel educacional" : "") +" e curso selecionados.");
				}
			}		
			setMatriculaVO(objAluno);
			this.setMatriculaVO(objAluno);
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());			
		    getControleConsulta().setValorConsulta("");				
			this.setMatriculaVO(new MatriculaVO());
		}
	}

    /**
     * @return the layoutLivroRegistroDiploma
     */
    public String getLayoutLivroRegistroDiploma() {
        if (layoutLivroRegistroDiploma == null) { 
            layoutLivroRegistroDiploma = "Layout 1";
        }
        return layoutLivroRegistroDiploma;
    }

    /**
     * @param layoutLivroRegistroDiploma the layoutLivroRegistroDiploma to set
     */
    public void setLayoutLivroRegistroDiploma(String layoutLivroRegistroDiploma) {
        this.layoutLivroRegistroDiploma = layoutLivroRegistroDiploma;
    }
    
    public List<SelectItem> listaSelectItemLayout;
    public List<SelectItem> getListaSelectItemLayout() throws Exception {        
        if(listaSelectItemLayout == null) {
        	listaSelectItemLayout =  new ArrayList<SelectItem>();
        	listaSelectItemLayout.add(new SelectItem("1", "Layout 1"));
        	listaSelectItemLayout.add(new SelectItem("2", "Layout 2"));
        	listaSelectItemLayout.add(new SelectItem("3", "Layout 3"));
        	listaSelectItemLayout.add(new SelectItem("4", "Layout 4"));
        	listaSelectItemLayout.add(new SelectItem("5", "Layout 5"));
        }
        return listaSelectItemLayout;
    }

    /**
     * @return the ocultarCabecalho
     */
    public Boolean getOcultarCabecalho() {
        if (ocultarCabecalho == null) { 
            ocultarCabecalho = Boolean.FALSE;
        }
        return ocultarCabecalho;
    }

    /**
     * @param ocultarCabecalho the ocultarCabecalho to set
     */
    public void setOcultarCabecalho(Boolean ocultarCabecalho) {
        this.ocultarCabecalho = ocultarCabecalho;
    }

    /**
     * @return the funcionario2VO
     */
    public FuncionarioVO getFuncionario2VO() {
        if (funcionario2VO == null) { 
            funcionario2VO = new FuncionarioVO();
        }
        return funcionario2VO;
    }

    /**
     * @param funcionario2VO the funcionario2VO to set
     */
    public void setFuncionario2VO(FuncionarioVO funcionario2VO) {
        this.funcionario2VO = funcionario2VO;
    }

    /**
     * @return the cargo2VO
     */
    public CargoVO getCargo2VO() {
        if (cargo2VO == null) { 
            cargo2VO = new CargoVO();
        }
        return cargo2VO;
    }

    /**
     * @param cargo2VO the cargo2VO to set
     */
    public void setCargo2VO(CargoVO cargo2VO) {
        this.cargo2VO = cargo2VO;
    }
    
    public void selecionarFuncionario2() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		setFuncionario2VO(obj);
		consultarFuncionarioPorMatricula2();
    }    
    
    public void consultarFuncionarioPorMatricula2() throws Exception {
        try {
            setFuncionario2VO(consultarFuncionarioPorMatriculaFuncionario(getFuncionario2VO().getMatricula()));
            setSelectItemsCargoFuncionario2(montarComboCargoFuncionario(getFuncionario2VO().getFuncionarioCargoVOs()));
            if ((getSelectItemsCargoFuncionario2() != null) && (getSelectItemsCargoFuncionario2().size() > 0)) {
                setCargo2VO(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(((Integer) getSelectItemsCargoFuncionario2().get(0).getValue()), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            }
            limparMensagem();
	} catch (Exception e) {
			setFuncionario2VO(new FuncionarioVO());
			setCargo2VO(new CargoVO());
            setMensagemDetalhada("msg_erro", e.getMessage());
	}
    }    
    
    public void prepararParaImprimirPDF() {
    	try {
    		definePadraoUsadoRelatorioNomesCargosFuncionarios();
    	} catch (Exception ex){
    		if(getCargoVO().getCodigo() == null) {
    		setMensagemDetalhada("msg_erro", ex.getMessage());
    		}
    	}
    }

    /**
     * @return the selectItemsCargoFuncionario2
     */
    public List<SelectItem> getSelectItemsCargoFuncionario2() {
        if (selectItemsCargoFuncionario2 == null) {
            selectItemsCargoFuncionario2 = new ArrayList<SelectItem>();
	}        
        return selectItemsCargoFuncionario2;
    }

    /**
     * @param selectItemsCargoFuncionario2 the selectItemsCargoFuncionario2 to set
     */
    public void setSelectItemsCargoFuncionario2(List<SelectItem> selectItemsCargoFuncionario2) {
        this.selectItemsCargoFuncionario2 = selectItemsCargoFuncionario2;
    }
    
    public boolean isApresentarCampoCargoFuncionario2() {
		return isApresentarCampos() && Uteis.isAtributoPreenchido(getFuncionario2VO());
    }    
    
    private void persistirLayoutPadrao() throws Exception {
        //String valor, String entidade, String campo,
        getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao(getControleLivroRegistroDiplomaVO().getCodigo().toString(), "controleLivroRegistroDiploma", "layoutLivroRegistroDiploma", getFuncionarioVO().getCodigo(), getFuncionario2VO().getCodigo(), null, getOcultarCabecalho(), getTituloAssinaturaFunc1(), getTituloAssinaturaFunc2(), "", "", "", getUsuarioLogado(), getNomeCargo1Apresentar(), getNomeCargo2Apresentar());
    }    
    
    private void carregarLayoutPadrao() throws Exception {
        LayoutPadraoVO layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("controleLivroRegistroDiploma", "layoutLivroRegistroDiploma", false, getUsuarioLogado());
        this.setLayoutLivroRegistroDiploma(layoutPadraoVO.getValor());
        this.getFuncionarioVO().setCodigo(layoutPadraoVO.getAssinaturaFunc1());
        this.getFuncionario2VO().setCodigo(layoutPadraoVO.getAssinaturaFunc2());
        setTituloAssinaturaFunc1(layoutPadraoVO.getTituloAssinaturaFunc1());
        setTituloAssinaturaFunc2(layoutPadraoVO.getTituloAssinaturaFunc2());
        this.setOcultarCabecalho(layoutPadraoVO.getApresentarTopoRelatorio());
        setNomeCargo1Apresentar(layoutPadraoVO.getNomeCargo1Apresentar());
        setNomeCargo2Apresentar(layoutPadraoVO.getNomeCargo2Apresentar());
        consultarFuncionarioPorCodigo();
        consultarFuncionarioPorCodigo2();
    }  
    
	public void abrirLivroRegistrosDiploma() {
		try {
			getControleLivroRegistroDiplomaVO().setSituacaoFechadoAberto(SituacaoControleLivroRegistroDiploma.ABERTO.getValor());
			getFacadeFactory().getControleLivroRegistroDiplomaFacade().alterar(getControleLivroRegistroDiplomaVO(), getListaControleLivroFolhaRecibo(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public boolean getPermitirReabrirLivroFechado() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirReabrirLivroFechado", getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	@SuppressWarnings("static-access")
	public void executarVerificacaoExisteControleLivroRegistroDiploma() {
		try {
			List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> listaUnidade = getControleLivroRegistroDiplomaVO().getControleLivroRegistroDiplomaUnidadeEnsinoVOs().stream().filter(uni -> uni.getSelecionado()).collect(Collectors.toList());
			if (Uteis.isAtributoPreenchido(getControleLivroRegistroDiplomaVO().getTipoLivroRegistroDiplomaEnum())) {
				ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO = getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorUnidadeEnsinoCursoLivro(listaUnidade, getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), getControleLivroRegistroDiplomaVO().getNrLivro(), "", getControleLivroRegistroDiplomaVO().getTipoLivroRegistroDiplomaEnum().toString(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				if (Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaVO)) {
					setControleLivroRegistroDiplomaVO(controleLivroRegistroDiplomaVO);
					montarListaSelectItemUnidadeEnsino();
					getControleLivroRegistroDiplomaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
					setListaControleLivroFolhaRecibo(getFacadeFactory().getControleLivroFolhaReciboFacade().consultarDadosControleLivroFolhaRecibo(getControleLivroRegistroDiplomaVO(), false, getUsuarioLogado()));
					for (ControleLivroFolhaReciboVO controleLivroFolhaReciboVO : getListaControleLivroFolhaRecibo()) {
						controleLivroFolhaReciboVO.setControleLivroRegistroDiploma(controleLivroRegistroDiplomaVO);
					}
					getControleLivroRegistroDiplomaVO().setNumeroRegistro(getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarMaxNumeroRegistro(getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getZerarNumeroRegistroPorCurso()));
					carregarLayoutPadrao();
					setAbrirPainelMensagemAbrirLivro(false);
				} else {
					setAbrirPainelMensagemAbrirLivro(true);
				}
	
				totalizadorAlunosLivro();
				if (getControleLivroRegistroDiplomaVO().getTipoLivroRegistroDiplomaEnum().name().equals(tipoLivroRegistroDiplomaEnum.CERTIFICADO.name())) {
					getControleLivroRegistroDiplomaVO().setNivelEducacional("");
				}
			}
		} catch (Exception e) {
			setAbrirPainelMensagemAbrirLivro(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		atualizarSituacaoControleLivroRecibo();
	}

	public boolean isAbrirPainelMensagemAbrirLivro() {
		return abrirPainelMensagemAbrirLivro;
	}

	public void setAbrirPainelMensagemAbrirLivro(boolean abrirPainelMensagemAbrirLivro) {
		this.abrirPainelMensagemAbrirLivro = abrirPainelMensagemAbrirLivro;
	}

	public void executarInicializacaoDadosNovoLivro() {

		try {
			getControleLivroRegistroDiplomaVO().setNumeroRegistro(getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarMaxNumeroRegistro(
					getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getZerarNumeroRegistroPorCurso()));

			if (!Uteis.isAtributoPreenchido(getControleLivroRegistroDiplomaVO().getNivelEducacional())) {
				getControleLivroRegistroDiplomaVO().setNivelEducacional(getNivelEducacionalSelecionado());
			}

			setValorConsultaFiltros("");
			setMatriculaVO(new MatriculaVO());
			setColacaoGrauVO(new ColacaoGrauVO());
			setFuncionarioVO(new FuncionarioVO());
			setCargoVO(new CargoVO());
			setListaControleLivroFolhaRecibo(new ArrayList<>(0));
			setFiltroColacao(false);
			setFiltroMatricula(false);
			setFiltroTurma(false);
			setAno("");
			setSemestre("");

			inicializarListasSelectItemTodosComboBox();
			carregarLayoutPadrao();
			setAbrirPainelMensagemAbrirLivro(false);
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void selecionarNivelEducacional() {
		setNivelEducacionalSelecionado(getControleLivroRegistroDiplomaVO().getNivelEducacional());
	}

//	public void executarConsultaUltimoLivro() {
//		try {
//			List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> listaUnidade = getControleLivroRegistroDiplomaVO().getControleLivroRegistroDiplomaUnidadeEnsinoVOs().stream().filter(uni -> uni.getSelecionado()).collect(Collectors.toList());
//			Integer ultimoLivro = getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorUnidadeEnsinoCursoUltimoLivroCadastrado(getUnidadeEnsinoVO().getCodigo(), getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_TODOS, controleLivroRegistroDiplomaVO.getNivelEducacional(), getUsuarioLogado());
//			if(Uteis.isAtributoPreenchido(ultimoLivro)){
//				ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO = getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarPorUnidadeEnsinoCursoLivro(listaUnidade, getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), ultimoLivro, "", "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
//				setControleLivroRegistroDiplomaVO(controleLivroRegistroDiplomaVO);
//				montarListaSelectItemUnidadeEnsino();
//				if(Uteis.isAtributoPreenchido(getControleLivroRegistroDiplomaVO().getCurso().getNome())){
//					getControleLivroRegistroDiplomaVO().setCurso(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, getUsuarioLogado()));
//				}
//				setListaControleLivroFolhaRecibo(getFacadeFactory().getControleLivroFolhaReciboFacade().consultarDadosControleLivroFolhaRecibo(getControleLivroRegistroDiplomaVO(), false, getUsuarioLogado()));				
//				getControleLivroRegistroDiplomaVO().setNumeroRegistro(getFacadeFactory().getControleLivroRegistroDiplomaFacade().consultarMaxNumeroRegistro(getControleLivroRegistroDiplomaVO().getCurso().getCodigo(), getConfiguracaoGeralSistemaPrivilegiandoACfgDaUnidade().getZerarNumeroRegistroPorCurso()));
//				carregarLayoutPadrao();
//			}
//			if(!Uteis.isAtributoPreenchido(ultimoLivro)) {
//				controleLivroRegistroDiplomaVO.setNrLivro(0);
//			}
//			setAbrirPainelMensagemAbrirLivro(false);
//			setMensagemID("msg_dados_consultados");	
//			totalizadorAlunosLivro();
//		} catch (Exception e) {
//			setAbrirPainelMensagemAbrirLivro(false);
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
//	}

	private void definePadraoUsadoRelatorioNomesCargosFuncionarios() throws Exception {
		if (!getNomeCargo1Apresentar().equals("")) {
			getCargoVO().setNome(getNomeCargo1Apresentar());
		} else if (getCargoVO().getCodigo().intValue() > 0) {
			CargoVO carVO = getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getCargoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuario());
			getCargoVO().setNome(carVO.getNome());
		}
		if (!getNomeCargo2Apresentar().equals("")) {
			getCargo2VO().setNome(getNomeCargo2Apresentar());
		} else if (getCargo2VO().getCodigo().intValue() > 0) {
			CargoVO carVO = getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(getCargo2VO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuario());
			getCargo2VO().setNome(carVO.getNome());
		}
	}

	public boolean isVerificaUnidadeEnsinoDiferenteNull() {
		listarNivelEducacional();

		return Uteis.isAtributoPreenchido(getControleLivroRegistroDiplomaVO().getTipoLivroRegistroDiplomaEnum());		 
	}

	public void listarNivelEducacional() {
		List<TipoNivelEducacional> niveisEducacionais = new ArrayList<TipoNivelEducacional>(
				EnumSet.allOf(TipoNivelEducacional.class));
		setListaNivelEducacional(new ArrayList<SelectItem>());
		getListaNivelEducacional().add(new SelectItem("0", ""));
		for (TipoNivelEducacional niveis : niveisEducacionais) {
			getListaNivelEducacional().add(new SelectItem(niveis.getValor(), niveis.getDescricao()));
		}
	}

	public Boolean getIsDisableBotaoAdicionarAlunosLivro() {
		if(getControleLivroRegistroDiplomaVO().getTipoLivroRegistroDiplomaEnum().isTipoRegistroDiploma()
				&& Uteis.isAtributoPreenchido(getControleLivroRegistroDiplomaVO().getCurso().getNome()) || getControleLivroRegistroDiplomaVO().getTipoLivroRegistroDiplomaEnum().isTipoRegistroCertificado()
				&& !Uteis.isAtributoPreenchido(getControleLivroRegistroDiplomaVO().getCurso().getNome())){
			return true;
		}
		return false  ;
			
	}

	public void montarInformacaoAluno() {
		try {
			ControleLivroFolhaReciboVO obj = (ControleLivroFolhaReciboVO) context().getExternalContext().getRequestMap().get("controleLivroFolhaReciboItens");
			TurmaVO turmaVO = getFacadeFactory().getTurmaFacade().consultaRapidaPorMatriculaUltimaMatriculaPeriodoPorAnoSemestrePeriodoLetivo(obj.getMatricula().getMatricula(), getUsuarioLogado());
			obj.getMatricula().setTurma(turmaVO.getIdentificadorTurma());
			setControleLivroFolhaReciboInfo(obj);
			buscaUsuarioAlterouSituacao();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void cancelarAlunoLivroRegistroDiploma() {
		try {
			setCancelarObj((ControleLivroFolhaReciboVO) context().getExternalContext().getRequestMap().get("controleLivroFolhaReciboItens"));
			setSituacaoAnterior(controleLivroFolhaReciboVO.getSituacao());
			getCancelarObj().setSituacao("Cancelado");
			totalizadorAlunosLivro();
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		 	e.printStackTrace();
		}
	}

	public void reverterCancelamentoAluno() {
		try {
			setReverterCancelamento((ControleLivroFolhaReciboVO) context().getExternalContext().getRequestMap().get("controleLivroFolhaReciboItens"));
			String situacao =  getFacadeFactory().getControleLivroFolhaReciboFacade().verificaSituacaoDiploma(getReverterCancelamento().getMatricula().getMatricula(), getUsuarioLogado());
			getReverterCancelamento().setSituacao(situacao);	
			totalizadorAlunosLivro();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void atualizarSituacaoControleLivroRecibo() {
		try {
			for (ControleLivroFolhaReciboVO clfr : getListaControleLivroFolhaRecibo()) {
				if(!clfr.isSituacaoCancelado()) {
					clfr.setSituacao(getFacadeFactory().getControleLivroFolhaReciboFacade().verificaSituacaoDiploma(clfr.getMatricula().getMatricula(), getUsuarioLogado()));					
					totalizadorAlunosLivro();
				}
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void totalizadorAlunosLivro() {
		try {
			int j = 0;
			int k = 0;
			int l = 0;
			for (ControleLivroFolhaReciboVO clfr : getListaControleLivroFolhaRecibo()) {					
				if(clfr.isSituacaoCancelado()) {
					j++;
				}
				if(clfr.isSituacaoEmitido()) {
					k++;		
				}
				if(clfr.isSituacaoPendente()) {
					l++;			
				}
			}	 
			setTotalAlunos(getListaControleLivroFolhaRecibo().size());
			setTotalEmitido(k);
			setTotalPendente(l);
			setTotalCancelado(j);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selecionarControleLivroFolhaReciboVOAlerta() {
		try {
			setControleLivroFolhaReciboVO((ControleLivroFolhaReciboVO) context().getExternalContext().getRequestMap().get("controleLivroFolhaReciboItens"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean isExibeBotaoAdicionaAlunoLivro() {
		if((controleLivroRegistroDiplomaVO.getTipoLivroRegistroDiplomaEnum().isTipoRegistroCertificado() && Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaVO.getNivelEducacional()))
				|| (controleLivroRegistroDiplomaVO.getTipoLivroRegistroDiplomaEnum().isTipoRegistroDiploma() && Uteis.isAtributoPreenchido(getControleLivroRegistroDiplomaVO().getCurso().getNome()))) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean isDesabilitaBotaoAdicionaAlunoLivro() {
		if(controleLivroRegistroDiplomaVO.getNivelEducacional() =="0") {
			return false;
		}else {
			return true;
		}
	}
	public void buscaUsuarioAlterouSituacao() {
		ControleLivroFolhaReciboVO obj = (ControleLivroFolhaReciboVO) context().getExternalContext().getRequestMap().get("controleLivroFolhaReciboItens");
		obj.getResponsavel().getNome();
	}

	public boolean isRenderizaBotaoAdicionarAlunosLivro() {
		if(Uteis.isAtributoPreenchido(controleLivroRegistroDiplomaVO.getNivelEducacional()) || controleLivroRegistroDiplomaVO.getTipoLivroRegistroDiplomaEnum().name().equals("DIPLOMA") || getControleLivroRegistroDiplomaVO().getCurso().getNome() !=null) {
			return true;
		}
		return false;
	}
	
	public void verificaTipoLivroRegistro() {
		if(controleLivroRegistroDiplomaVO.getTipoLivroRegistroDiplomaEnum().isTipoRegistroCertificado() || controleLivroRegistroDiplomaVO.getTipoLivroRegistroDiplomaEnum().isTipoRegistroDiploma()) {
			controleLivroRegistroDiplomaVO.setNrLivro(0);
		}
	}

	public void  permaneceValoresNaTela() {
		 Integer nrLivro = getControleLivroRegistroDiplomaVO().getNrLivro();
		 getControleLivroRegistroDiplomaVO().setNrLivro(nrLivro);	 
	}

	public void guardaVariaveis() {
		int nrLivro = getControleLivroRegistroDiplomaVO().getNrLivro();
		int nrFolhaRecido = getControleLivroRegistroDiplomaVO().getNrFolhaRecibo();
		int nrRegistro = getControleLivroRegistroDiplomaVO().getNumeroRegistro();
		int nrMaxFolhaLivro = getControleLivroRegistroDiplomaVO().getNrMaximoFolhasLivro();
		setNrLivroTemp(nrLivro);
		setNrFolhaReciboTemp(nrFolhaRecido);
		setNrRegistroTemp(nrRegistro);
		setNrMaxFolhaLivroTemp(nrMaxFolhaLivro);
    }

	public void recuperarNumeroLivro() {
		 getControleLivroRegistroDiplomaVO().setNrLivro(getNrLivroTemp());
		 getControleLivroRegistroDiplomaVO().setNrFolhaRecibo(getNrFolhaReciboTemp());
		 getControleLivroRegistroDiplomaVO().setNumeroRegistro(getNrRegistroTemp());
		 getControleLivroRegistroDiplomaVO().setNrMaximoFolhasLivro(getNrMaxFolhaLivroTemp());
	}
	
	public void scrollerListenerAluno(DataScrollEvent dataScrollerEvent) {
		try {
			getDataModeloAluno().setPaginaAtual(dataScrollerEvent.getPage());
			getDataModeloAluno().setPage(dataScrollerEvent.getPage());
			consultarAluno();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	 
	public List<SelectItem> getListaNivelEducacional() {
		return listaNivelEducacional;
	}

	public void setListaNivelEducacional(List<SelectItem> listaNivelEducacional) {
		this.listaNivelEducacional = listaNivelEducacional;
	}

	public String getNivelEducacionalSelecionado() {
		return nivelEducacionalSelecionado;
	}

	public void setNivelEducacionalSelecionado(String nivelEducacionalSelecionado) {
		this.nivelEducacionalSelecionado = nivelEducacionalSelecionado;
	}

	public ProgramacaoFormaturaAlunoVO getProgramacaoFormaturaAlunoVO() {
		if(programacaoFormaturaAlunoVO == null) {
			programacaoFormaturaAlunoVO = new ProgramacaoFormaturaAlunoVO();
		}
		return programacaoFormaturaAlunoVO;
	}

	public void setProgramacaoFormaturaAlunoVO(ProgramacaoFormaturaAlunoVO programacaoFormaturaAlunoVO) {
		this.programacaoFormaturaAlunoVO = programacaoFormaturaAlunoVO;
	}

	public ControleLivroFolhaRecibo getControleLivroFolhaRecibo() {
		if(controleLivroFolhaRecibo == null) {
			try {
				controleLivroFolhaRecibo = new ControleLivroFolhaRecibo();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return controleLivroFolhaRecibo;
	}

	public void setControleLivroFolhaRecibo(ControleLivroFolhaRecibo controleLivroFolhaRecibo) {
		this.controleLivroFolhaRecibo = controleLivroFolhaRecibo;
	}

	public ControleLivroFolhaReciboVO getControleLivroFolhaReciboInfo() {
		return controleLivroFolhaReciboInfo;
	}

	public void setControleLivroFolhaReciboInfo(ControleLivroFolhaReciboVO controleLivroFolhaReciboInfo) {
		this.controleLivroFolhaReciboInfo = controleLivroFolhaReciboInfo;
	}

	public ControleLivroFolhaReciboVO getCancelarObj() {
		return cancelarObj;
	}

	public void setCancelarObj(ControleLivroFolhaReciboVO cancelarObj) {
		this.cancelarObj = cancelarObj;
	}

	public String getSituacaoAnterior() {
		return situacaoAnterior;
	}

	public void setSituacaoAnterior(String situacaoAnterior) {
		this.situacaoAnterior = situacaoAnterior;
	}

	public ControleLivroFolhaReciboVO getReverterCancelamento() {
		return reverterCancelamento;
	}

	public void setReverterCancelamento(ControleLivroFolhaReciboVO reverterCancelamento) {
		this.reverterCancelamento = reverterCancelamento;
	}

	public int getTotalAlunos() {
		return totalAlunos;
	}

	public void setTotalAlunos(int totalAlunos) {
		this.totalAlunos = totalAlunos;
	}

	public int getTotalEmitido() {
		return totalEmitido;
	}

	public void setTotalEmitido(int totalEmitido) {
		this.totalEmitido = totalEmitido;
	}

	public int getTotalPendente() {
		return totalPendente;
	}

	public void setTotalPendente(int totalPendente) {
		this.totalPendente = totalPendente;
	}

	public int getTotalCancelado() {
		return totalCancelado;
	}

	public void setTotalCancelado(int totalCancelado) {
		this.totalCancelado = totalCancelado;
	}

	public int getNrLivroTemp() {
		return nrLivroTemp;
	}

	public void setNrLivroTemp(int nrLivroTemp) {
		this.nrLivroTemp = nrLivroTemp;
	}
	public String getTituloAssinaturaFunc1() {
		if (tituloAssinaturaFunc1 == null) {
			tituloAssinaturaFunc1 = "";
		}
		return tituloAssinaturaFunc1;
	}

	public void setTituloAssinaturaFunc1(String tituloAssinaturaFunc1) {
		this.tituloAssinaturaFunc1 = tituloAssinaturaFunc1;
	}

	public String getTituloAssinaturaFunc2() {
		if (tituloAssinaturaFunc2 == null) {
			tituloAssinaturaFunc2 = "";
		}
		return tituloAssinaturaFunc2;
	}

	public void setTituloAssinaturaFunc2(String tituloAssinaturaFunc2) {
		this.tituloAssinaturaFunc2 = tituloAssinaturaFunc2;
	}
	
	
	public int getNrFolhaReciboTemp() {
		return nrFolhaReciboTemp;
	}

	public void setNrFolhaReciboTemp(int nrFolhaReciboTemp) {
		this.nrFolhaReciboTemp = nrFolhaReciboTemp;
	}

	public int getNrRegistroTemp() {
		return nrRegistroTemp;
	}

	public void setNrRegistroTemp(int nrRegistroTemp) {
		this.nrRegistroTemp = nrRegistroTemp;
	}

	public int getNrMaxFolhaLivroTemp() {
		return nrMaxFolhaLivroTemp;
	}

	public void setNrMaxFolhaLivroTemp(int nrMaxFolhaLivroTemp) {
		this.nrMaxFolhaLivroTemp = nrMaxFolhaLivroTemp;
	}
	
	/**
	 * @return the controleLivroFolhaReciboExcluirVO
	 */
	public ControleLivroFolhaReciboVO getControleLivroFolhaReciboExcluirVO() {
		if (controleLivroFolhaReciboExcluirVO == null) {
			controleLivroFolhaReciboExcluirVO = new ControleLivroFolhaReciboVO();
		}
		return controleLivroFolhaReciboExcluirVO;
	}

	/**
	 * @param controleLivroFolhaReciboExcluirVO the controleLivroFolhaReciboExcluirVO to set
	 */
	public void setControleLivroFolhaReciboExcluirVO(ControleLivroFolhaReciboVO controleLivroFolhaReciboExcluirVO) {
		this.controleLivroFolhaReciboExcluirVO = controleLivroFolhaReciboExcluirVO;
	}
	
	public boolean isCampoConsultaTipoLivroRegistro(){
		return getControleConsulta().getCampoConsulta().equals("tipoLivroRegistro");
	}

	public TipoLivroRegistroDiplomaEnum getTipoLivroRegistroDiplomaEnum() {
		return tipoLivroRegistroDiplomaEnum;
	}

	public void setTipoLivroRegistroDiplomaEnum(TipoLivroRegistroDiplomaEnum tipoLivroRegistroDiplomaEnum) {
		this.tipoLivroRegistroDiplomaEnum = tipoLivroRegistroDiplomaEnum;
	}
	
	public String getNomeCargo1Apresentar() {
		if (nomeCargo1Apresentar == null) {
			nomeCargo1Apresentar = "";
		}
		return nomeCargo1Apresentar;
	}

	public void setNomeCargo1Apresentar(String nomeCargo1Apresentar) {
		this.nomeCargo1Apresentar = nomeCargo1Apresentar;
	}

	public String getNomeCargo2Apresentar() {
		if (nomeCargo2Apresentar == null) {
			nomeCargo2Apresentar = "";
		}
		return nomeCargo2Apresentar;
	}

	public void setNomeCargo2Apresentar(String nomeCargo2Apresentar) {
		this.nomeCargo2Apresentar = nomeCargo2Apresentar;
	}
	
	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public List<TurmaVO> getListaConsultaTurma() {
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getValorConsultaTurma() {
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
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

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
	}

	public List<SelectItem> getListaSelectItemCurso() {
		if (listaSelectItemCurso == null) {
			listaSelectItemCurso = new ArrayList<>(0);
		}
		return (listaSelectItemCurso);
	}

	public void setListaSelectItemCurso(List<SelectItem> listaSelectItemCurso) {
		this.listaSelectItemCurso = listaSelectItemCurso;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<>(0);
		}
		return (listaSelectItemUnidadeEnsino);
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public ControleLivroRegistroDiplomaVO getControleLivroRegistroDiplomaVO() {
		if (controleLivroRegistroDiplomaVO == null) {
			controleLivroRegistroDiplomaVO = new ControleLivroRegistroDiplomaVO();
		}
		return controleLivroRegistroDiplomaVO;
	}

	public void setControleLivroRegistroDiplomaVO(ControleLivroRegistroDiplomaVO controleLivroRegistroDiplomaVO) {
		this.controleLivroRegistroDiplomaVO = controleLivroRegistroDiplomaVO;
	}

	public String getValorConsultaFiltros() {
		if (valorConsultaFiltros == null) {
			valorConsultaFiltros = "";
		}
		return valorConsultaFiltros;
	}

	public void setValorConsultaFiltros(String valorConsultaFiltros) {
		this.valorConsultaFiltros = valorConsultaFiltros;
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

	public Boolean getFiltroTurma() {
		if (filtroTurma == null) {
			filtroTurma = false;
		}
		return filtroTurma;
	}

	public void setFiltroTurma(Boolean filtroTurma) {
		this.filtroTurma = filtroTurma;
	}

	public Boolean getFiltroColacao() {
		if (filtroColacao == null) {
			filtroColacao = false;
		}
		return filtroColacao;
	}

	public void setFiltroColacao(Boolean filtroColacao) {
		this.filtroColacao = filtroColacao;
	}

	public Boolean getFiltroMatricula() {
		if (filtroMatricula == null) {
			filtroMatricula = false;
		}
		return filtroMatricula;
	}

	public void setFiltroMatricula(Boolean filtroMatricula) {
		this.filtroMatricula = filtroMatricula;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
	}

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setColacaoGrauVO(ColacaoGrauVO colacaoVO) {
		this.colacaoVO = colacaoVO;
	}

	public ColacaoGrauVO getColacaoGrauVO() {
		if (colacaoVO == null) {
			colacaoVO = new ColacaoGrauVO();
		}
		return colacaoVO;
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

	public String getValorConsultaColacao() {
		return valorConsultaColacao;
	}

	public void setValorConsultaColacao(String valorConsultaColacao) {
		this.valorConsultaColacao = valorConsultaColacao;
	}

	public String getCampoConsultaColacao() {
		return campoConsultaColacao;
	}

	public void setCampoConsultaColacao(String campoConsultaColacao) {
		this.campoConsultaColacao = campoConsultaColacao;
	}

	public List<ColacaoGrauVO> getListaConsultaColacao() {
		if (listaConsultaColacao == null) {
			listaConsultaColacao = new ArrayList<>();
		}
		return listaConsultaColacao;
	}

	public void setListaConsultaColacao(List<ColacaoGrauVO> listaConsultaColacao) {
		this.listaConsultaColacao = listaConsultaColacao;
	}

//	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
//		if (unidadeEnsinoVO == null) {
//			unidadeEnsinoVO = new UnidadeEnsinoVO();
//		}
//		return unidadeEnsinoVO;
//	}
//
//	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
//		this.unidadeEnsinoVO = unidadeEnsinoVO;
//	}

	public ControleLivroFolhaReciboVO getControleLivroFolhaReciboVO() {
		if (controleLivroFolhaReciboVO == null) {
			controleLivroFolhaReciboVO = new ControleLivroFolhaReciboVO();
		}
		return controleLivroFolhaReciboVO;
	}

	public void setControleLivroFolhaReciboVO(ControleLivroFolhaReciboVO controleLivroFolhaReciboVO) {
		this.controleLivroFolhaReciboVO = controleLivroFolhaReciboVO;
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

	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		if (configuracaoGeralSistemaVO == null) {
			configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
		}
		return configuracaoGeralSistemaVO;
	}

	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}

	public ControleLivroRegistroDiplomaRel getControleLivroRegistroDiplomaRel() {
		if (controleLivroRegistroDiplomaRel == null) {
			controleLivroRegistroDiplomaRel = new ControleLivroRegistroDiplomaRel();
		}
		return controleLivroRegistroDiplomaRel;
	}

	public void setControleLivroRegistroDiplomaRel(ControleLivroRegistroDiplomaRel controleLivroRegistroDiplomaRel) {
		this.controleLivroRegistroDiplomaRel = controleLivroRegistroDiplomaRel;
	}

	public FuncionarioVO getFuncionarioVO() {
		if (funcionarioVO == null) {
			funcionarioVO = new FuncionarioVO();
		}
		return funcionarioVO;
	}

	public void setFuncionarioVO(FuncionarioVO funcionarioVO) {
		this.funcionarioVO = funcionarioVO;
	}

	public CargoVO getCargoVO() {
		if (cargoVO == null) {
			cargoVO = new CargoVO();
		}
		return cargoVO;
	}

	public void setCargoVO(CargoVO cargoVO) {
		this.cargoVO = cargoVO;
	}

	public boolean isApresentarCampos() {
		return getControleLivroRegistroDiplomaVO().getNrLivro() != 0 && getControleLivroRegistroDiplomaVO().getNrFolhaRecibo() != 0;
	}
	
	public List<SelectItem> getSelectItemsCargoFuncionario() {
		if (selectItemsCargoFuncionario == null) {
			selectItemsCargoFuncionario = new ArrayList<SelectItem>();
		}
		return selectItemsCargoFuncionario;
	}

	public void setSelectItemsCargoFuncionario(List<SelectItem> selectItemsCargoFuncionario) {
		this.selectItemsCargoFuncionario = selectItemsCargoFuncionario;
	}

	public Integer getAnoAtual() {
		return Uteis.getAnoData(new Date());
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<>(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
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
			listaConsultaCurso = new ArrayList<>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}
	
	private List<SelectItem> listaSelectTipoLivroRegistroDiplomaEnum;

	public List<SelectItem> getListaSelectTipoLivroRegistroDiplomaEnum() {
		if (listaSelectTipoLivroRegistroDiplomaEnum == null) {
			listaSelectTipoLivroRegistroDiplomaEnum = new ArrayList<>();
			listaSelectTipoLivroRegistroDiplomaEnum.add(new SelectItem("", ""));
			for (TipoLivroRegistroDiplomaEnum tipoLivroRegistroDiplomaEnum : TipoLivroRegistroDiplomaEnum.values()) {
				listaSelectTipoLivroRegistroDiplomaEnum.add(new SelectItem(tipoLivroRegistroDiplomaEnum, internacionalizarEnum(tipoLivroRegistroDiplomaEnum)));
			}
		}
		return listaSelectTipoLivroRegistroDiplomaEnum;
	}
	
	public DataModelo getDataModeloAluno() {
		if (dataModeloAluno == null) {
			dataModeloAluno = new DataModelo();
		}
		return dataModeloAluno;
	}

	public void setDataModeloAluno(DataModelo dataModeloAluno) {
		this.dataModeloAluno = dataModeloAluno;
	}

//	public Boolean getUtilizarLogoUnidadeEnsino() {
//		if (utilizarLogoUnidadeEnsino == null) {
//			utilizarLogoUnidadeEnsino = Boolean.FALSE;
//		}
//		return utilizarLogoUnidadeEnsino;
//	}
//
//	public void setUtilizarLogoUnidadeEnsino(Boolean utilizarLogoUnidadeEnsino) {
//		this.utilizarLogoUnidadeEnsino = utilizarLogoUnidadeEnsino;
//	}
	
	public void marcarTodasUnidadesEnsinoAction() {
		for (ControleLivroRegistroDiplomaUnidadeEnsinoVO unidade : getControleLivroRegistroDiplomaVO().getControleLivroRegistroDiplomaUnidadeEnsinoVOs()) {
			unidade.setSelecionado(getMarcarTodasUnidadeEnsino());
		}
		verificarTodasUnidadesSelecionadas();
	}
	
	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getControleLivroRegistroDiplomaVO().getControleLivroRegistroDiplomaUnidadeEnsinoVOs().size() > 1) {
			for (ControleLivroRegistroDiplomaUnidadeEnsinoVO obj : getControleLivroRegistroDiplomaVO().getControleLivroRegistroDiplomaUnidadeEnsinoVOs()) {
				if (obj.getSelecionado()) {
					unidade.append(obj.getUnidadeEnsino().getNome().trim()).append("; ");
				}
				getControleLivroRegistroDiplomaVO().setUnidadeEnsinoDescricao(unidade.toString());
			}
		} else {
			if (!getControleLivroRegistroDiplomaVO().getControleLivroRegistroDiplomaUnidadeEnsinoVOs().isEmpty()) {
				if (getControleLivroRegistroDiplomaVO().getControleLivroRegistroDiplomaUnidadeEnsinoVOs().get(0).getSelecionado()) {
					getControleLivroRegistroDiplomaVO().setUnidadeEnsinoDescricao(getUnidadeEnsinoVOs().get(0).getNome());
				}
			}
		}
	}
	
	
	public Boolean getFiltroProgramacaoFormatura() {
		if (filtroProgramacaoFormatura == null) {
			filtroProgramacaoFormatura = false;
		}
		return filtroProgramacaoFormatura;
	}
	
	public void setFiltroProgramacaoFormatura(Boolean filtroProgramacaoFormatura) {
		this.filtroProgramacaoFormatura = filtroProgramacaoFormatura;
	}
	
	public ProgramacaoFormaturaVO getProgramacaoFormaturaVO() {
		if (programacaoFormaturaVO == null) {
			programacaoFormaturaVO = new ProgramacaoFormaturaVO();
		}
		return programacaoFormaturaVO;
	}
	
	public void setProgramacaoFormaturaVO(ProgramacaoFormaturaVO programacaoFormaturaVO) {
		this.programacaoFormaturaVO = programacaoFormaturaVO;
	}
	
	public void limparProgramacaoFormatura() {
		setProgramacaoFormaturaVO(new ProgramacaoFormaturaVO());
	}
	
	public String getCampoConsultaProgramacao() {
		if (campoConsultaProgramacao == null) {
			campoConsultaProgramacao = "codigo";
		}
		return campoConsultaProgramacao;
	}
	
	public void setCampoConsultaProgramacao(String campoConsultaProgramacao) {
		this.campoConsultaProgramacao = campoConsultaProgramacao;
	}
	
	public List<SelectItem> getTipoConsultaComboProgramacaoFormatura() {
		if (tipoConsultaComboProgramacaoFormatura == null) {
			tipoConsultaComboProgramacaoFormatura = new ArrayList<SelectItem>(0);
			tipoConsultaComboProgramacaoFormatura.add(new SelectItem("colacaoGrau","Colação Grau"));
			tipoConsultaComboProgramacaoFormatura.add(new SelectItem("codigo","Código"));
		}
		return tipoConsultaComboProgramacaoFormatura;
	}
	
	public void setTipoConsultaComboProgramacaoFormatura(List<SelectItem> tipoConsultaComboProgramacaoFormatura) {
		this.tipoConsultaComboProgramacaoFormatura = tipoConsultaComboProgramacaoFormatura;
	}
	
	public String getValorConsultaProgramacao() {
		if (valorConsultaProgramacao == null) {
			valorConsultaProgramacao = "";
		}
		return valorConsultaProgramacao;
	}
	
	public void setValorConsultaProgramacao(String valorConsultaProgramacao) {
		this.valorConsultaProgramacao = valorConsultaProgramacao;
	}
	
	public void consultarProgramacaoFormatura() {
		setListaConsultaProgramacaoFormaturaVOs(new ArrayList<>(0));
		try {
			List<ControleLivroRegistroDiplomaUnidadeEnsinoVO> listaUnidadesSelecionadas = getControleLivroRegistroDiplomaVO().getControleLivroRegistroDiplomaUnidadeEnsinoVOs().stream().filter(uni -> uni.getSelecionado()).collect(Collectors.toList());
			if (getCampoConsultaProgramacao().equals("codigo")) {
				if (getValorConsultaProgramacao().equals("")) {
					setValorConsultaProgramacao("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaProgramacao());
				setListaConsultaProgramacaoFormaturaVOs(getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorCodigoEUnidadeEnsinos(Integer.valueOf(valorInt), listaUnidadesSelecionadas, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			} else if (getCampoConsultaProgramacao().equals("colacaoGrau")) {
				setListaConsultaProgramacaoFormaturaVOs(getFacadeFactory().getProgramacaoFormaturaFacade().consultarPorColacaoGrau(getValorConsultaProgramacao(), listaUnidadesSelecionadas, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
		} catch (Exception e) {
			setListaConsultaProgramacaoFormaturaVOs(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public List<ProgramacaoFormaturaVO> getListaConsultaProgramacaoFormaturaVOs() {
		if (listaConsultaProgramacaoFormaturaVOs == null) {
			listaConsultaProgramacaoFormaturaVOs = new ArrayList<ProgramacaoFormaturaVO>(0);
		}
		return listaConsultaProgramacaoFormaturaVOs;
	}
	
	public void setListaConsultaProgramacaoFormaturaVOs(List<ProgramacaoFormaturaVO> listaConsultaProgramacaoFormaturaVOs) {
		this.listaConsultaProgramacaoFormaturaVOs = listaConsultaProgramacaoFormaturaVOs;
	}
	
	public void selecionarProgramacao() {
		ProgramacaoFormaturaVO obj = (ProgramacaoFormaturaVO) context().getExternalContext().getRequestMap().get("programacaoItens");
		setProgramacaoFormaturaVO(obj);
		setCampoConsultaProgramacao("");
		setValorConsultaProgramacao("");
		setListaConsultaProgramacaoFormaturaVOs(new ArrayList<ProgramacaoFormaturaVO>(0));
	}
	
	public List<SelectItem> getListaColouGrau() {
		if (listaColouGrau == null) {
			listaColouGrau = new ArrayList<SelectItem>(0);
			listaColouGrau.add(new SelectItem("ambos", "Ambos"));
			listaColouGrau.add(new SelectItem(SituacaoColouGrauProgramacaoFormaturaAluno.COLOU_GRAU.getValor(), SituacaoColouGrauProgramacaoFormaturaAluno.COLOU_GRAU.getDescricao()));
			listaColouGrau.add(new SelectItem(SituacaoColouGrauProgramacaoFormaturaAluno.NAO_COLOU.getValor(), SituacaoColouGrauProgramacaoFormaturaAluno.NAO_COLOU.getDescricao()));
			listaColouGrau.add(new SelectItem(SituacaoColouGrauProgramacaoFormaturaAluno.NAO_INFORMADO.getValor(), SituacaoColouGrauProgramacaoFormaturaAluno.NAO_INFORMADO.getDescricao()));
		}
		return listaColouGrau;
	}
	
	public void setListaColouGrau(List<SelectItem> listaColouGrau) {
		this.listaColouGrau = listaColouGrau;
	}
	
	public String getColouGrau() {
		if (colouGrau == null) {
			colouGrau = "";
		}
		return colouGrau;
	}
	
	public void setColouGrau(String colouGrau) {
		this.colouGrau = colouGrau;
	}
	
	public void validarVia() {
		ControleLivroFolhaReciboVO obj = (ControleLivroFolhaReciboVO) context().getExternalContext().getRequestMap().get("controleLivroFolhaReciboItens");
		try {
			if (!Uteis.isAtributoPreenchido(obj.getVia())) {
				throw new Exception("A VIA (Controle Livro Folha Recibo) não pode ser menor de 0.");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getReconhecimentoUtilizarLayout3() {
		if (reconhecimentoUtilizarLayout3 == null) {
			reconhecimentoUtilizarLayout3 = "RenovacaoReconhecimentoCurso";
		}
		return reconhecimentoUtilizarLayout3;
	}

	public void setReconhecimentoUtilizarLayout3(String reconhecimentoUtilizarLayout3) {
		this.reconhecimentoUtilizarLayout3 = reconhecimentoUtilizarLayout3;
	}
}
