package controle.academico;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DropEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.GradeDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.TipoRecursoEducacionalEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.PermissaoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.academico.Arquivo;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("ArquivoControle")
@Scope("viewScope")
public class ArquivoControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private ArquivoVO arquivoVO;
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
	private Integer codigoArquivoBase;
	private String responsavelUploadErro;
	private String campoConsultarDisciplina;
	private String valorConsultarDisciplina;
	private List<DisciplinaVO> listaConsultarDisciplina;
	private List<SelectItem> listaDisciplinaTurma;
	private Boolean mostrarComboDisciplina;
	private Boolean mostrarComboTurma;
	private List<ArquivoVO> listaArquivos;
	private List<ArquivoVO> listaArquivosResposta;
	private List<SelectItem> listaSelectItemTurma;
	private List<SelectItem> listaSelectItemDisciplinasTurma;
	private List<SelectItem> listaSelectItemTurmaDisciplina;
	private List<ArquivoVO> arquivosAnexadosInstituicao;
	private List<ArquivoVO> arquivosAnexadosProfessor;
	private List<ArquivoVO> arquivosAnexadosDisciplinaTurma;
	private List<ArquivoVO> arquivoVOs;
	private String nomeDisciplinaTela;
	private String nomeTab;
	private DisciplinaVO disciplinaVO;
	private TurmaVO turmaVO;
	private List<MatriculaVO> matriculaVOs;
	private Boolean permiteProfessorExcluirArquivosInstituicao;
	private String campoConsultaProfessor;
	private String valorConsultaProfessor;
	private List<PessoaVO> listaConsultaProfessor;
	private PessoaVO professorVO;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private Boolean apresentarDataDisponibilizacaoMaterial;
	private MatriculaVO matriculaVO;
	private Boolean dentroDataMaximaLimiteDownload;
	private Boolean permiteGravarVisaoCoordenador;
	private Integer turma;
	private Boolean permitirUsuarioRealizarUpload;
	private String erroUpload;
	private String msgErroUpload;
	private Boolean buscarTurmasAnteriores;
	private Boolean subir;
	private Boolean editandoArquivo;
	private String onCompletePanelUpload;
	private String agrupadorTemp;
	private Map<String, ArquivoVO> mapArquivoAgrupamentoDisciplinaTurmaProfessor;
	private ArquivoVO arquivoExcluirVO;
	private ArquivoVO arquivoFilhoExcluirVO;
	private Boolean excluindoArquivoFilho;
	private boolean apresentarComboboxPeriodoVisaoAluno = false;
	private List<SelectItem> listaSelectItemAnoSemestre;
	private String anoSemestre;
	private Integer qtdeDownloadsMaterialNaoRealizados;
	private String ano;
	private String semestre;
	private List listaSelectSemestre;
	
	//@transient
	private Integer qtdeCaractereLimiteDownloadMaterial;
	

	private String campoConsultaSituacao;
	private String anoPeriodoUpload;
	private String semestrePeriodoUpload;
	private String anoPeriodoDisponibilizacao;
	private String semestrePeriodoDisponibilizacao;
	private Boolean postarMaterialComTurmaObrigatoriamenteInformado;
	
	/**
	 * Interface <code>ArquivoInterfaceFacade</code> responsável pela interconexão
	 * da camada de controle com a camada de negócio. Criando uma independência da
	 * camada de controle com relação a tenologia de persistência dos dados
	 * (DesignPatter: Façade).
	 */
	public ArquivoControle() throws Exception {
		verificarPermiteProfessorExcluirArquivosInstituicao();
		verificarPermissaoAlteracaoVisualizacaoDataDisponibilizacaoMaterial();
		verificarUsuarioPossuiPermissaoRealizarUpload();
		verificarPostarMaterialComTurmaObrigatoriamenteInformado();
		inicializarDataSemestreCamporFiltroVisaoAdministrativa();
		setMensagemID("msg_entre_prmconsulta");
	}

	private void inicializarDataSemestreCamporFiltroVisaoAdministrativa() {
		setAnoPeriodoUpload(Uteis.getAnoDataAtual4Digitos());
		setSemestrePeriodoUpload(Uteis.getSemestreAtual());
		setAnoPeriodoDisponibilizacao(Uteis.getAnoDataAtual4Digitos());
		setSemestrePeriodoDisponibilizacao(Uteis.getSemestreAtual());
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>Arquivo</code> para edição pelo usuário da aplicação.
	 */
	public void novo() {
		setArquivoVO(null);
		getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ARQUIVO);
		setListaDisciplinaTurma(null);
		setMostrarComboDisciplina(Boolean.FALSE);
		setMostrarComboTurma(Boolean.FALSE);
		setListaArquivos(null);
		setListaArquivosResposta(null);
		setListaSelectItemDisciplinasTurma(null);
		setListaSelectItemTurma(null);
		setListaSelectItemTurmaDisciplina(null);
		setArquivosAnexadosInstituicao(null);
		setArquivosAnexadosProfessor(null);
		setArquivosAnexadosDisciplinaTurma(null);
		getArquivoVO().setTurma(getTurmaVO());
		getArquivoVO().setDisciplina(getDisciplinaVO());
		setAno(Uteis.getAnoDataAtual4Digitos());
		setSemestre(Uteis.getSemestreAtual());
		setMensagemID("msg_entre_dados");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>Arquivo</code> para alteração. O objeto desta classe é disponibilizado
	 * na session da página (request) para que o JSP correspondente possa
	 * disponibilizá-lo para edição.
	 */
	public void editar() {
		try {
			setArquivoVO((ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens"));
			getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ARQUIVO);
			// setTurmaVO(getArquivoVO().getTurma().clone());
			setEditandoArquivo(true);
			setQtdeCaractereLimiteDownloadMaterial(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesQtdeCaractereLimiteDownloadMaterial());
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da
	 * classe <code>Arquivo</code>. Caso o objeto seja novo (ainda não gravado no
	 * BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o
	 * <code>alterar()</code>. Se houver alguma inconsistência o objeto não é
	 * gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de
	 * erro.
	 */
	public void persistir() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Incluir Arquivo", "Incluindo");
		if (!getEditandoArquivo()) {
			getArquivoVO().setDisciplina(getDisciplinaVO().clone());
			getArquivoVO().setTurma(getTurmaVO().clone());
		}
		getFacadeFactory().getArquivoFacade().persistir(getArquivoVO(), true, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
		// PersonalizacaoMensagemAutomaticaVO mensagemTemplate =
		// getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_DOWNLOAD_ANTECEDENCIA_MATERIAL,
		// false, null);
		//
		// if (mensagemTemplate != null &&
		// !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
		// ConfiguracaoGeralSistemaVO config =
		// getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
		// SqlRowSet rs =
		// getFacadeFactory().getArquivoFacade().consultarAlunoEmPeriodoAulaNotificarDownloadMaterial(UteisData.getDataJDBC(new
		// Date()).toString(), arquivoVO.getDisciplina().getCodigo(),
		// arquivoVO.getTurma().getCodigo(), arquivoVO.getProfessor().getCodigo());
		// while (rs.next()) {
		// getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemDownloadMaterialAlunosPeriodoAula(rs,
		// mensagemTemplate, config, getUsuarioLogado());
		// }
		// }

		getArquivoVO().setKeyAgrupadorTurmaDisciplinaProfessor(null);
		if (!getEditandoArquivo() || !getAgrupadorTemp().equals(getArquivoVO().getAgrupador())) {
			if (getMapArquivoAgrupamentoDisciplinaTurmaProfessor().containsKey(getArquivoVO().getKeyAgrupadorTurmaDisciplinaProfessor())) {
				getFacadeFactory().getArquivoFacade().adicionarArquivoIndice(getMapArquivoAgrupamentoDisciplinaTurmaProfessor().get(getArquivoVO().getKeyAgrupadorTurmaDisciplinaProfessor()).getArquivoFilhoVOs(), getArquivoVO());
			} else {
				getFacadeFactory().getArquivoFacade().adicionarArquivoIndice(new ArrayList<ArquivoVO>(), getArquivoVO());
			}
		}
		setMensagemID("msg_dados_gravados");
		consultaArquivos();
		setNomeTab("tabArquivos");
		setArquivoVO(null);
		getArquivoVO().setDisciplina(getDisciplinaVO().clone());
		getArquivoVO().setTurma((TurmaVO) getTurmaVO().clone());
		getArquivoVO().setProfessor((PessoaVO) getProfessorVO().clone());
		registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Incluir Arquivo", "Incluindo");
		setOnCompletePanelUpload("RichFaces.$('panelUploadArquivo').hide()");
		setEditandoArquivo(false);
	}

	public String gravarBackUp() {
		ArquivoVO arquivoTemp = getArquivoVO();
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Incluir Arquivo", "Incluindo");
			getArquivoVO().setDisciplina(getDisciplinaVO());
			getArquivoVO().setTurma(getTurmaVO());
			getFacadeFactory().getArquivoFacade().incluirBackUp(getArquivoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			setMensagemID("msg_dados_gravados");
			consultaArquivosBackUp();
			setNomeTab("tabArquivos");
			setArquivoVO(null);
			getArquivoVO().setDisciplina(arquivoTemp.getDisciplina());
			getArquivoVO().setTurma(arquivoTemp.getTurma());
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Incluir Arquivo", "Incluindo");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		} finally {
			arquivoTemp = null;
			getArquivoVO().setDescricao("");
			getArquivoVO().setNome("");
		}
	}

	public void gravarProfessor() throws Exception {
		try {
			ArquivoVO arquivoTemp = getArquivoVO();
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Incluir Arquivo - Gravar Professor", "Incluindo");
			getArquivoVO().setProfessor(getUsuarioLogado().getPessoa());
			if (!getEditandoArquivo() || !getAgrupadorTemp().equals(getArquivoVO().getAgrupador())) {
				if (getMapArquivoAgrupamentoDisciplinaTurmaProfessor().containsKey(getArquivoVO().getKeyAgrupadorTurmaDisciplinaProfessor())) {
					getFacadeFactory().getArquivoFacade().adicionarArquivoIndice(getMapArquivoAgrupamentoDisciplinaTurmaProfessor().get(getArquivoVO().getKeyAgrupadorTurmaDisciplinaProfessor()).getArquivoFilhoVOs(), getArquivoVO());
				} else {
					getFacadeFactory().getArquivoFacade().adicionarArquivoIndice(new ArrayList<ArquivoVO>(), getArquivoVO());
				}
			}
			getFacadeFactory().getArquivoFacade().persistir(getArquivoVO(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			consultarArquivosVisaoProfessor();
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Incluir Arquivo - Gravar Professor", "Incluindo");
			ConfiguracaoGeralSistemaVO config = null; 
			if (getUnidadeEnsinoLogado().getConfiguracoes().getCodigo() != null && getUnidadeEnsinoLogado().getConfiguracoes().getCodigo() != 0) {
				config = getAplicacaoControle().getConfiguracaoGeralSistemaVO(getUnidadeEnsinoLogado().getCodigo(), null);
			} else {
				config = getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, null);				
			}
			getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemPostagemMaterial(config, getArquivoVO());
			setArquivoVO(null);
			getArquivoVO().setDisciplina(arquivoTemp.getDisciplina());
			getArquivoVO().setTurma(arquivoTemp.getTurma());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			throw e;
		}
	}

	public static void verificarPermissaoUsuarioVisualizarDataDisponibilizacaoMaterial(UsuarioVO usuario, String nomeEntidade) throws Exception {
		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public void verificarPermissaoAlteracaoVisualizacaoDataDisponibilizacaoMaterial() {
		Boolean liberar = false;
		try {
			verificarPermissaoUsuarioVisualizarDataDisponibilizacaoMaterial(getUsuarioLogado(), "ApresentarDataDisponibilizacaoMaterial");
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		this.setApresentarDataDisponibilizacaoMaterial(liberar);
	}

	public void verificarPermissaoParaVisualizacaoDataDisponibilizacaoMaterial() {
		try {
			ControleAcesso.alterar("ApresentarDataDisponibilizacaoMaterial", getUsuarioLogado());
			this.setApresentarDataDisponibilizacaoMaterial(true);
		} catch (Exception e) {
			this.setApresentarDataDisponibilizacaoMaterial(false);
		}
	}

	public void limparCampoDataIndisponibilizacao() {
		
			getArquivoVO().setDataIndisponibilizacao(null);
		
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * ArquivoCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	@Override
	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Consultar Arquivo", "Consultando");
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getArquivoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getArquivoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataUpload")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getArquivoFacade().consultarPorDataUpload(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoUsuario")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getArquivoFacade().consultarPorCodigoUsuario(new Integer(valorInt), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("dataDisponibilizacao")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getArquivoFacade().consultarPorDataDisponibilizacao(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeDisciplina")) {
				objs = getFacadeFactory().getArquivoFacade().consultarPorNomeDisciplina(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("identificadorTurmaTurma")) {
				objs = getFacadeFactory().getArquivoFacade().consultarPorIdentificadorTurmaTurma(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("situacao")) {
				objs = getFacadeFactory().getArquivoFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Consultar Arquivo", "Consultando");
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	public String excluir() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Inciando Excluir Arquivo", "Excluindo");
			ArquivoVO arquivo = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
			getFacadeFactory().getArquivoFacade().excluir(arquivo, true, "Upload", getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			getArquivoVOs().remove(arquivo);
			setMensagemID("msg_dados_excluidos");
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Excluir Arquivo", "Excluindo");
			return "editar";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "editar";
		}
	}

	public void consultarProfessor() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaProfessor().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("cpf")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorCPF(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaProfessor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarProfessor() {
		setProfessorVO((PessoaVO) context().getExternalContext().getRequestMap().get("professorItens"));
		getArquivosAnexadosDisciplinaTurma().clear();
		getMapArquivoAgrupamentoDisciplinaTurmaProfessor().clear();
		getArquivoVOs().clear();
		getArquivoVO().setProfessor(getProfessorVO());
		// consultaArquivos();
	}

	public void consultarDisciplinasTurma() throws Exception {
		if (getArquivoVO().getTurma().getTurmaAgrupada()) {
			montarListaDisciplinaAgrupada();
		} else {
			montarListaDisciplinaNaoAgrupada();
		}
		if ((getListaDisciplinaTurma() != null) && (!getListaDisciplinaTurma().isEmpty())) {
			setMostrarComboDisciplina(true);
		}
	}

	public void montarListaDisciplinaAgrupada() throws Exception {
		List resultadoConsulta = consultarDisciplinaTurmaAgrupada();
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			DisciplinaVO obj = (DisciplinaVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getCodigo() + " - " + obj.getNome()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaDisciplinaTurma(objs);
	}

	public List<DisciplinaVO> consultarDisciplinaTurmaAgrupada() throws Exception {
		List<DisciplinaVO> objs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaTurmaAgrupada(getArquivoVO().getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return objs;
	}

	public void montarListaDisciplinaNaoAgrupada() throws Exception {
		if (getArquivoVO().getTurma().getPeridoLetivo().getCodigo().equals(0)) {
			setListaDisciplinaTurma(new ArrayList(0));
			return;
		}
		List resultadoConsulta = new ArrayList(0);
		resultadoConsulta = consultarDisciplinaPorCodigoPeriodoLetivo(getArquivoVO().getTurma().getPeridoLetivo().getCodigo());
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		objs.add(new SelectItem(0, ""));
		while (i.hasNext()) {
			GradeDisciplinaVO obj = (GradeDisciplinaVO) i.next();
			objs.add(new SelectItem(obj.getDisciplina().getCodigo(), obj.getCodigo() + " - " + obj.getDisciplina().getNome()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaDisciplinaTurma(objs);
	}

	public List consultarDisciplinaPorCodigoPeriodoLetivo(Integer prm) throws Exception {
		List lista = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(prm, false, getUsuarioLogado(), null);
		return lista;
	}

	public void limparCampoTurma() {
		getArquivoVO().setTurma(null);
		setArquivosAnexadosDisciplinaTurma(null);
		setArquivoVOs(null);
	}

	public void limparCampoProfessor() {
		setProfessorVO(new PessoaVO());
		getArquivoVO().setProfessor(null);
		setArquivosAnexadosDisciplinaTurma(null);
		setArquivoVOs(null);
		getArquivosAnexadosDisciplinaTurma().clear();
		getMapArquivoAgrupamentoDisciplinaTurmaProfessor().clear();
		getArquivoVOs().clear();
		// consultaArquivos();
	}

	/**
	 * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
	 */
	public List<SelectItem> tipoConsultarComboTurma;

	public List<SelectItem> getTipoConsultarComboTurma() {
		if (tipoConsultarComboTurma == null) {
			tipoConsultarComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultarComboTurma.add(new SelectItem("codigo", "Código"));
			tipoConsultarComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
			tipoConsultarComboTurma.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
			tipoConsultarComboTurma.add(new SelectItem("nomeTurno", "Turno"));
		}
		return tipoConsultarComboTurma;
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Disciplina</code> por meio dos parametros informados no richmodal. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca
	 * pelos parâmentros informados no richModal montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarDisciplina() {
		try {
			List<DisciplinaVO> objs = new ArrayList<DisciplinaVO>(0);
			if (getCampoConsultarDisciplina().equals("codigo")) {
				if (getValorConsultarDisciplina().equals("")) {
					setValorConsultarDisciplina("0");
				}
				if (getValorConsultarDisciplina().trim() != null || !getValorConsultarDisciplina().trim().isEmpty()) {
					Uteis.validarSomenteNumeroString(getValorConsultarDisciplina().trim());
				}
				Integer valorInt = Integer.parseInt(getValorConsultarDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultarDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultarDisciplina(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultarDisciplina(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarDisciplina(new ArrayList<DisciplinaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplina() throws Exception {
		DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
		if (getMensagemDetalhada().equals("")) {
			this.getArquivoVO().setDisciplina(obj);
		}
		setDisciplinaVO(obj);
		getArquivosAnexadosDisciplinaTurma().clear();
		getMapArquivoAgrupamentoDisciplinaTurmaProfessor().clear();
		getArquivoVOs().clear();
		Uteis.liberarListaMemoria(this.getListaConsultarDisciplina());
		this.setValorConsultarDisciplina(null);
		this.setCampoConsultarDisciplina(null);
		// montarListaSelectItemTurmaDisciplina();
		setMostrarComboTurma(true);
		setArquivosAnexadosDisciplinaTurma(null);
		// consultaArquivos();
	}

	public void limparCampoDisciplina() {
		setDisciplinaVO(null);
		setMostrarComboTurma(false);
		setNomeDisciplinaTela("");
		getArquivosAnexadosDisciplinaTurma().clear();
		setValorConsultaTurma(null);
		setListaConsultaTurma(null);
		getArquivoVO().setDisciplina(new DisciplinaVO());
		setArquivoVOs(null);
		setTurmaVO(null);
		getArquivosAnexadosDisciplinaTurma().clear();
		getMapArquivoAgrupamentoDisciplinaTurmaProfessor().clear();
		getArquivoVOs().clear();
	}

	/**
	 * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
	 */
	public List<SelectItem> getTipoConsultarComboDisciplina() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Usuario</code>
	 * por meio de sua respectiva chave primária. Esta rotina é utilizada
	 * fundamentalmente por requisições Ajax, que realizam busca pela chave primária
	 * da entidade montando automaticamente o resultado da consulta para
	 * apresentação.
	 */
	public void consultarUsuarioPorChavePrimaria() {
		try {
			Integer campoConsulta = arquivoVO.getResponsavelUpload().getCodigo();
			UsuarioVO usuario = getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			arquivoVO.getResponsavelUpload().setCodigo(usuario.getCodigo());
			this.setResponsavelUpload_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			arquivoVO.getResponsavelUpload().setCodigo(0);
			arquivoVO.getResponsavelUpload().setCodigo(0);
			this.setResponsavelUpload_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Rotina responsável por atribui um javascript com o método de mascara para
	 * campos do tipo Data, CPF, CNPJ, etc.
	 */
	public String getMascaraConsulta() {
		return "";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("dataUpload", "Data Upload"));
		itens.add(new SelectItem("codigoUsuario", "Responsável Upload"));
		itens.add(new SelectItem("dataDisponibilizacao", "Data Disponibilização"));
		itens.add(new SelectItem("nomeDisciplina", "Disciplina"));
		itens.add(new SelectItem("identificadorTurmaTurma", "Turma"));
		itens.add(new SelectItem("situacao", "Situação"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de
	 * uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(null);
		setMensagemID("msg_entre_prmconsulta");
		return "consultar";
	}

	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
				if (uploadEvent.getUploadedFile() != null && uploadEvent.getUploadedFile().getSize() > (getConfiguracaoGeralPadraoSistema().getTamanhoMaximoUpload() * 1024 * 1024)) {
					setErroUpload("RichFaces.$('panelMsgErroUpload').show()");
					setMsgErroUpload("Prezado professor/coordenador, seu arquivo excede o tamanho estipulado pela Instituição, por favor reduza o arquivo ou divida em partes antes de efetuar a postagem. Obrigado.");
				} else {
					setErroUpload("RichFaces.$('panelMsgErroUpload').hide()");
					getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.ARQUIVO_TMP, getUsuarioLogado());
				}
			} else {
				getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.ARQUIVO_TMP, getUsuarioLogado());
			}
			if (getArquivoVO().getDescricao().contains(".pdf")) {
				setIconeArquivo("fas fa-file-pdf text-danger");
			} else if (getArquivoVO().getDescricao().contains(".png") || getArquivoVO().getDescricao().contains(".jpg")) {
				setIconeArquivo("fas fa-file-image text-primary");
			} else if (getArquivoVO().getDescricao().contains(".xlsx")) {
				setIconeArquivo("fas fa-file-excel text-success");
			} else {
				setIconeArquivo("fas fa-file");
			}
			setApresentarNomeArquivo(getArquivoVO().getDescricao());
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}
	
	private String iconeArquivo;
	
	public String getIconeArquivo() {
		if (iconeArquivo == null) {
			iconeArquivo = "";
		}
		return iconeArquivo;
	}
	
	public void setIconeArquivo(String iconeArquivo) {
		this.iconeArquivo = iconeArquivo;
	}
	
	public void copiarArquivo(File origem, File destino) throws Exception {
		// Cria channel no origem
		FileChannel oriChannel = new FileInputStream(origem.getAbsolutePath()).getChannel();
		// Cria channel no destino
		FileChannel destChannel = new FileOutputStream(destino.getAbsolutePath()).getChannel();
		// Copia conteúdo da origem no destino
		destChannel.transferFrom(oriChannel, 0, oriChannel.size());
		// Fecha channels
		oriChannel.close();
		destChannel.close();
	}

	private File obterNomeArquivoFisicoComAcentos(String arquivoSendoBuscado, Map<String, File> mapaArquivosExistentesComNomesSemAcentos) throws Exception {
		for (String nomeArquivoExistente : mapaArquivosExistentesComNomesSemAcentos.keySet()) {
			if (arquivoSendoBuscado.equals(nomeArquivoExistente)) {
				return mapaArquivosExistentesComNomesSemAcentos.get(nomeArquivoExistente);
			}
		}
		return null;
	}

	public void organizarArquivosDiretorioCEAFI() {
		try {
			// Na base de dados do CEAFI existem arquivps que estão cadastrados
			// sem acentos,
			// contudo os mesmos existem nos diretórios com acento.
			Map<String, File> mapaArquivosComNomesSemAcentos = new HashMap<String, File>();
			String nomeArquivoOrigem = "c:" + File.separator + "data" + File.separator + "www" + File.separator + "upload" + File.separator + "arquivo";
			File diretorioArquivos = new File(nomeArquivoOrigem);
			for (File f : diretorioArquivos.listFiles()) {
				if (f.isFile()) {
					String nomeArquivoSemAcento = Uteis.removerAcentos(f.getAbsolutePath());
					mapaArquivosComNomesSemAcentos.put(nomeArquivoSemAcento, f);
				}
			}

			// ALTERAR O METODO ABAIXO PARA TRAZER TODAS AS DISCIPLINAS >= E NAO
			// SOMENTE UMA COMO É (=)
			List<ArquivoVO> arquivosOrganizar = getFacadeFactory().getArquivoFacade().consultarArquivoPorDisciplinaTurma(0, 0, 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado(),"","","","","");
			Iterator i = arquivosOrganizar.iterator();

			while (i.hasNext()) {
				ArquivoVO arquivoOrganizar = (ArquivoVO) i.next();
				File pastaDestino = new File("c:" + arquivoOrganizar.getPastaBaseArquivo());
				if (!pastaDestino.exists()) {
					pastaDestino.mkdir();
				}
				File arquivoOrigemTransferir = new File(nomeArquivoOrigem + File.separator + arquivoOrganizar.getNome());
				if (!arquivoOrigemTransferir.exists()) {
					File arquivoOrigemLocalizadoComNomeSemAcento = obterNomeArquivoFisicoComAcentos(nomeArquivoOrigem + File.separator + arquivoOrganizar.getNome(), mapaArquivosComNomesSemAcentos);
					if (arquivoOrigemLocalizadoComNomeSemAcento != null) {
						arquivoOrigemTransferir = arquivoOrigemLocalizadoComNomeSemAcento;
					}
				}
				String novoNomeArquivo = Uteis.removerAcentos(arquivoOrganizar.getNome());
				novoNomeArquivo = novoNomeArquivo.replaceAll("-", "_");

				File arquivoDestino = new File("c:" + arquivoOrganizar.getPastaBaseArquivo() + File.separator + novoNomeArquivo);
				if (arquivoOrigemTransferir.exists()) {
					arquivoOrganizar.setNome(novoNomeArquivo);
					arquivoOrganizar.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ARQUIVO);
					getFacadeFactory().getArquivoFacade().alterar(arquivoOrganizar, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
					copiarArquivo(arquivoOrigemTransferir, arquivoDestino);
					// System.out.println("OK..: " + arquivoOrganizar.getNome()
					// + " - " + arquivoOrganizar.getPastaBaseArquivo());
				} else {
					// System.out.println("ERRO..: " +
					// arquivoOrganizar.getNome() + " - " +
					// arquivoOrganizar.getPastaBaseArquivo());
				}
			}
			// System.out.println("=====================================================");
			// System.out.println(" FINALIZADA ALTERAÇÃO DE PASTA DE ARQUIVOS");
			// System.out.println("=====================================================");
			// getArquivo

			// getArquivoVO().setResponsavelUpload(getUsuarioLogadoClone());
			// getArquivoVO().setDataUpload(new Date());
			// if (getArquivoVO().getManterDisponibilizacao()) {
			// getArquivoVO().setDataDisponibilizacao(getArquivoVO().getDataUpload());
			// getArquivoVO().setDataIndisponibilizacao(null);
			// }
			// getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
			// getArquivoVO().setOrigem(OrigemArquivo.INSTITUICAO.getValor());
			//
			// ArquivoVO.validarDados(getArquivoVO());
			//
			// getArquivoVO().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplinaVO().getCodigo(),
			// Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			// gravar();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerAcentosCaracteresEspeciaisArquivos() {
		try {
			getFacadeFactory().getArquivoFacade().removerAcentosCaracteresEspeciaisArquivos(getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarArquivoLista() {
		List<ArquivoVO> arquivoTempVOs = new ArrayList<ArquivoVO>(0);
		try {
			
			validarPermissao();
			getArquivoVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(getConfiguracaoGeralPadraoSistema().getServidorArquivoOnline()));
			
			arquivoTempVOs = gerarCloneArquivo();
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Adicionar Lista Download Arquivo", "Downloading");
			getArquivoVO().setResponsavelUpload(getUsuarioLogadoClone());
			getArquivoVO().setDataUpload(new Date());
			if (getArquivoVO().getManterDisponibilizacao()) {
				getArquivoVO().setDataDisponibilizacao(getArquivoVO().getDataUpload());
				getArquivoVO().setDataIndisponibilizacao(null);
			}
			getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
			getArquivoVO().setOrigem(OrigemArquivo.PROFESSOR.getValor());
			getArquivoVO().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			persistir();
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Adicionar Lista Download Arquivo", "Downloading");
		} catch (Exception e) {
			setArquivoVOs(arquivoTempVOs);
			setOnCompletePanelUpload("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarArquivoListaArquivoInstitucional() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Adicionar Lista Download Arquivo", "Downloading");
			getArquivoVO().setResponsavelUpload(getUsuarioLogadoClone());
			getArquivoVO().setDataUpload(new Date());
			if (getArquivoVO().getManterDisponibilizacao()) {
				getArquivoVO().setDataDisponibilizacao(getArquivoVO().getDataUpload());
				getArquivoVO().setDataIndisponibilizacao(null);
			}
			getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
			getArquivoVO().setOrigem(OrigemArquivo.INSTITUICAO.getValor());
			getArquivoVO().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			persistir();
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Adicionar Lista Download Arquivo", "Downloading");
		} catch (Exception e) {
			setArquivoVOs(null);
			setOnCompletePanelUpload("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarArquivoListaBackUp() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Adicionar Lista Download Arquivo", "Downloading");
			getArquivoVO().setResponsavelUpload(getUsuarioLogadoClone());
			getArquivoVO().setDataUpload(new Date());
			if (getArquivoVO().getManterDisponibilizacao()) {
				getArquivoVO().setDataDisponibilizacao(getArquivoVO().getDataUpload());
				getArquivoVO().setDataIndisponibilizacao(null);
			}
			getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
			getArquivoVO().setOrigem(OrigemArquivo.INSTITUICAO.getValor());
			getArquivoVO().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getDisciplinaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			getArquivoVO().setProfessor(getProfessorVO());
			gravarBackUp();
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Adicionar Lista Download Arquivo", "Downloading");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarArquivoListaProfessor() {
		List<ArquivoVO> arquivoTempVOs = new ArrayList<ArquivoVO>(0);
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			setOnCompletePanelUpload("");
			validarPermissao();
			arquivoTempVOs = gerarCloneArquivo();
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Adicionar Lista Download Arquivo - Professor", "Downloading");
			getFacadeFactory().getArquivoFacade().validarDadosDisponibilizarMaterialAcademico(getArquivoVO());
			getArquivoVO().setResponsavelUpload(getUsuarioLogadoClone());
//			getArquivoVO().setDataUpload(new Date());
			if (getArquivoVO().getManterDisponibilizacao()) {
				getArquivoVO().setDataDisponibilizacao(getArquivoVO().getDataUpload());
				getArquivoVO().setDataIndisponibilizacao(null);
			}
			getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
			getArquivoVO().setOrigem(OrigemArquivo.PROFESSOR.getValor());
			if (getArquivoVO().getTurma().getCodigo() != 0) {
				getArquivoVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getArquivoVO().getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			getArquivoVO().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getArquivoVO().getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			gravarProfessor();
			setOnCompletePanelUpload("RichFaces.$('panelUploadArquivo').hide()");
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Adicionar Lista Download Arquivo - Professor", "Downloading");
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setArquivoVOs(arquivoTempVOs);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void adicionarArquivoResposta() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Adicionar Lista Download Arquivo - Arquivo Resposta", "Downloading");
			getArquivoVO().setArquivoResposta(getCodigoArquivoBase());
			getArquivoVO().setResponsavelUpload(getUsuarioLogadoClone());
			getArquivoVO().setOrigem(OrigemArquivo.ALUNO.getValor());
			getListaArquivosResposta().add(getArquivoVO());
			getFacadeFactory().getArquivoFacade().incluir(getArquivoVO(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			consultarArquivosPorDisciplina();
			setMensagemID("msg_arquivo_enviado");
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Adicionar Lista Download Arquivo - Arquivo Resposta", "Downloading");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getMudaAba() {
		return "tabArquivos";
	}

	public void removerArquivoListaProfessor() throws Exception {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Remover Arquivo Download - Lista Professor", "Downloading - Removendo");
			ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
			obj.setManterDisponibilizacao(false);
			obj.setDataIndisponibilizacao(Uteis.getDataJDBCTimestamp(new Date()));
			getFacadeFactory().getArquivoFacade().alterarManterDisponibilizacao(obj, true, "Upload", getUsuarioLogado());
			getFacadeFactory().getArquivoFacade().excluir(obj, true, "Upload", getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			excluirObjArquivoVOProfessor(obj.getDescricao());
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Remover Arquivo Download - Lista Professor", "Downloading - Removendo");
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerArquivoLista() throws Exception {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Remover Arquivo Download ", "Downloading - Removendo");
			ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
			getFacadeFactory().getArquivoFacade().excluir(obj, true, "Upload", getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			excluirObjArquivoVO(obj.getDescricao());
			consultarArquivosDoProfessor();
			setMensagemID("msg_dados_excluidos");
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Remover Arquivo Download ", "Downloading - Removendo");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void removerArquivoListaBackUp() throws Exception {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Remover Arquivo Download ", "Downloading - Removendo");
			ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
			getFacadeFactory().getArquivoFacade().excluirBackUp(obj, true, "Upload", getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			excluirObjArquivoVO(obj.getDescricao());
			consultarArquivosDoProfessor();
			setMensagemID("msg_dados_excluidos");
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Remover Arquivo Download ", "Downloading - Removendo");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void excluirObjArquivoVO(String descricao) throws Exception {
		int index = 0;
		Iterator i = getArquivosAnexadosDisciplinaTurma().iterator();
		while (i.hasNext()) {
			ArquivoVO objExistente = (ArquivoVO) i.next();
			if (objExistente.getDescricao().equals(descricao)) {
				getArquivosAnexadosDisciplinaTurma().remove(index);
				// excluir();
				return;
			}
			index++;
		}
	}

	public void excluirObjArquivoVOProfessor(String descricao) throws Exception {
		int index = 0;
		Iterator i = getArquivosAnexadosProfessor().iterator();
		while (i.hasNext()) {
			ArquivoVO objExistente = (ArquivoVO) i.next();
			if (objExistente.getDescricao().equals(descricao)) {
				getArquivosAnexadosProfessor().remove(index);
				return;
			}
			index++;
		}
	}

	public void novoVisaoProfessor() throws Exception {
		try {
			setArquivoVO(new ArquivoVO());
			getArquivoVO().setDisciplina(getDisciplinaVO().clone());
			if (getTurmaVO().getCodigo() > 0) {
				getArquivoVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurmaVO().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			}
			getArquivoVO().setProfessor(getUsuarioLogado().getPessoa());
			getArquivoVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(getConfiguracaoGeralPadraoSistema().getServidorArquivoOnline()));
			setQtdeCaractereLimiteDownloadMaterial(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesQtdeCaractereLimiteDownloadMaterial());
			validarPermissao();
			setOnCompletePanelUpload("RichFaces.$('panelUploadArquivo').show()");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setOnCompletePanelUpload("RichFaces.$('panelUploadArquivo').hide()");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@PostConstruct
	public void inicializarDadosVisaoProfessor() throws Exception {
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			if (getNomeTelaAtual().equals("/visaoProfessor/uploadArquivosProfessor.xhtml")) {
				registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Menu Download - Visao Professor", "Downloading");
				novo();
				montarListaSelectItemTurmaProfessor();
				montarListaSelectItemDisciplinasProfessor();
				verificarPermissaoParaVisualizacaoDataDisponibilizacaoMaterial();
				verificarUsuarioPossuiPermissaoRealizarUpload();
				executarMetodoControle(VisaoProfessorControle.class.getSimpleName(), "inicializarUploadArquivosProfessor");
				consultarArquivosVisaoProfessor();
				registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Menu Download - Visao Professor", "Downloading");
			} else if (getNomeTelaAtual().equals("/visaoProfessor/downloadArquivosComunsProfessor.xhtml")) {
				novoVisaoProfessorArquivosComuns();
			}

		}
	}

	@PostConstruct
	public void inicializarDadosVisaoCoordenador() {
		try {
			if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				if (getNomeTelaAtual().equals("/visaoCoordenador/uploadArquivosCoordenador.xhtml")) {
					registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Menu Download - Visao Coordenador", "Downloading");
					novo();
					montarListaSelectItemTurmaCoordenador();
					montarListaSelectItemDisciplinasCoordenador();
					verificarPermissaoParaVisualizacaoDataDisponibilizacaoMaterial();
					verificarUsuarioPossuiPermissaoRealizarUpload();
					executarMetodoControle(VisaoCoordenadorControle.class.getSimpleName(), "inicializarUploadArquivosCoordenador");
					consultarArquivosVisaoCoordenador();
					registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Menu Download - Visao Coordenador", "Downloading");
				} else if (getNomeTelaAtual().equals("/visaoCoordenador/downloadArquivosComunsCoordenador.xhtml")) {
					novoVisaoCoordenadorArquivosComuns();
				}
			}
		} catch (Exception e) {

		}
	}

	public void novoVisaoCoordenador() throws Exception {
		setArquivoVO(new ArquivoVO());
		TurmaVO turmaVO = getTurmaVO().getClone();
		for (SelectItem selectItem : getListaSelectItemTurma()) {
			if (selectItem.getValue().equals(getTurmaVO().getCodigo())) {
				turmaVO.setIdentificadorTurma(selectItem.getLabel());
				break;
			}
		}
		getArquivoVO().setDisciplina(getDisciplinaVO().clone());
		getArquivoVO().setTurma(turmaVO);
		getArquivoVO().setProfessor(getUsuarioLogado().getPessoa());
		getArquivoVO().setServidorArquivoOnline(ServidorArquivoOnlineEnum.getEnum(getConfiguracaoGeralPadraoSistema().getServidorArquivoOnline()));
		setQtdeCaractereLimiteDownloadMaterial(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesQtdeCaractereLimiteDownloadMaterial());
	}

	public void montarListaSelectItemDisciplinasCoordenador() throws Exception {
		getListaSelectItemDisciplinasTurma().clear();
		List<DisciplinaVO> resultadoConsulta = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaCoordenadorPorTurma(getUsuarioLogado().getPessoa().getCodigo(), getTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		boolean existeDisciplina =  false;
		for (DisciplinaVO obj : resultadoConsulta) {
			getListaSelectItemDisciplinasTurma().add(new SelectItem(obj.getCodigo(), obj.getCodigo() + " - " + obj.getNome()));
			if(Uteis.isAtributoPreenchido(getDisciplinaVO()) && getDisciplinaVO().getCodigo().equals(obj.getCodigo())) {
				existeDisciplina =  true;
			}
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		if(Uteis.isAtributoPreenchido(resultadoConsulta) && !existeDisciplina) {
			setDisciplinaVO(resultadoConsulta.get(0));
		}
		Collections.sort((List<SelectItem>) getListaSelectItemDisciplinasTurma(), ordenador);
	}

	@SuppressWarnings("CallToThreadDumpStack")
	public void montarListaSelectItemTurmaCoordenador() {
		try {
			montarListaSelectItemTurmaCoordenador("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
			// System.out.println("Erro montarListaSelectItemTurmaCoordenador: "
			// + e.getMessage());
		}
	}

	public void montarListaSelectItemTurmaCoordenador(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		getListaSelectItemTurma().clear();
		try {
			if (getListaSelectItemTurma().isEmpty()) {
				resultadoConsulta = consultarTurmaCoordenador();
				i = resultadoConsulta.iterator();
				getListaSelectItemTurma().clear();
				getListaSelectItemTurma().add(new SelectItem(0, "TODAS AS TURMAS"));
				while (i.hasNext()) {
					TurmaVO obj = (TurmaVO) i.next();
					getListaSelectItemTurma().add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma().toString()));
					removerObjetoMemoria(obj);
				}
				resultadoConsulta.clear();
				resultadoConsulta = null;
				i = null;
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List consultarTurmaCoordenador() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenadorAnoSemestre(getUsuarioLogado().getPessoa().getCodigo(), false, false, false, getBuscarTurmasAnteriores(), getAno(), getSemestre(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
	}

	public void montarListaDisciplinaTurmaVisaoCoordenador() {
		try {
			getArquivoVO().setDisciplina(null);
			getArquivosAnexadosInstituicao().clear();
			setArquivosAnexadosInstituicao(null);
			getArquivosAnexadosProfessor().clear();
			setArquivosAnexadosProfessor(null);
			getArquivoVOs().clear();
			getMapArquivoAgrupamentoDisciplinaTurmaProfessor().clear();
			montarListaSelectItemDisciplinasCoordenador();
			if (Uteis.isAtributoPreenchido(getDisciplinaVO())) {
				consultarArquivosVisaoCoordenador();
			}
		} catch (Exception e) {
			setListaSelectItemDisciplinasTurma(null);
		}
	}

	public List<DisciplinaVO> consultarDisciplinaTurmaVisaoCoordenador() throws Exception {
		return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaCoordenadorPorTurma(getUsuarioLogado().getPessoa().getCodigo(), getTurmaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	public void adicionarArquivoListaCoordenador() {
		List<ArquivoVO> arquivoTempVOs = new ArrayList<ArquivoVO>(0);
		try {
			executarValidacaoSimulacaoVisaoCoordenador();
			setOnCompletePanelUpload("");
			validarPermissao();
			arquivoTempVOs = gerarCloneArquivo();
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Adicionar Lista Download Arquivo - Coordenador", "Downloading");
			getFacadeFactory().getArquivoFacade().validarDadosDisponibilizarMaterialAcademico(getArquivoVO());
			getArquivoVO().setResponsavelUpload(getUsuarioLogadoClone());
//			getArquivoVO().setDataUpload(new Date());
			if (getArquivoVO().getManterDisponibilizacao()) {
				getArquivoVO().setDataDisponibilizacao(getArquivoVO().getDataUpload());
				getArquivoVO().setDataIndisponibilizacao(null);
			}
			getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
			getArquivoVO().setOrigem(OrigemArquivo.PROFESSOR.getValor());
			if (getArquivoVO().getTurma().getCodigo() != 0) {
				getArquivoVO().setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getArquivoVO().getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			}
			getArquivoVO().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getArquivoVO().getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			gravarCoordenador();
			setOnCompletePanelUpload("RichFaces.$('panelUploadArquivo').hide()");
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Adicionar Lista Download Arquivo - Coordenador", "Downloading");
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			getArquivoVOs().clear();
			getArquivoVOs().addAll(getMapArquivoAgrupamentoDisciplinaTurmaProfessor().values());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gravarCoordenador() throws Exception {
		ArquivoVO arquivoTemp = getArquivoVO();
		registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Incluir Arquivo - Gravar Coordenador", "Incluindo");
		if (!getEditandoArquivo() || !getAgrupadorTemp().equals(getArquivoVO().getAgrupador())) {
			if (getMapArquivoAgrupamentoDisciplinaTurmaProfessor().containsKey(getArquivoVO().getKeyAgrupadorTurmaDisciplinaProfessor())) {
				getFacadeFactory().getArquivoFacade().adicionarArquivoIndice(getMapArquivoAgrupamentoDisciplinaTurmaProfessor().get(getArquivoVO().getKeyAgrupadorTurmaDisciplinaProfessor()).getArquivoFilhoVOs(), getArquivoVO());
			} else {
				getFacadeFactory().getArquivoFacade().adicionarArquivoIndice(new ArrayList<ArquivoVO>(), getArquivoVO());
			}
		}
		getFacadeFactory().getArquivoFacade().persistir(getArquivoVO(), false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
		consultarArquivosVisaoCoordenador();
		setArquivoVO(null);
		getArquivoVO().setDisciplina(arquivoTemp.getDisciplina());
		getArquivoVO().setTurma(arquivoTemp.getTurma());
		registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Incluir Arquivo - Gravar Coordenador", "Incluindo");
		setMensagemID("msg_dados_gravados");
	}

	public String novoVisaoProfessorArquivosComuns() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Menu Download Arquivos Instituicionais - Visao Professor", "Downloading");
		List<ArquivoVO> lista = getFacadeFactory().getArquivoFacade().consultarArquivoInstituicionalParaProfessor(getUsuarioLogado(), null);
		getListaArquivos().clear();
		getListaArquivos().addAll(lista);
		// for (ArquivoVO arquivo : lista) {
		// if (arquivo.getApresentarPortalProfessor()) {
		// getListaArquivos().add(arquivo);
		// }
		// }
		executarMetodoControle(VisaoProfessorControle.class.getSimpleName(), "inicializarUploadArquivosProfessor");
		registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Menu Download Arquivos Instituicionais - Visao Professor", "Downloading");
		return Uteis.getCaminhoRedirecionamentoNavegacao("downloadArquivosComunsProfessor.xhtml");
	}

	public String novoVisaoCoordenadorArquivosComuns() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Menu Download Arquivos Instituicionais - Visao Professor", "Downloading");
		List<ArquivoVO> lista = getFacadeFactory().getArquivoFacade().consultarArquivoInstituicionalParaCoordenador(getUsuarioLogado(), null);
		getListaArquivos().clear();
		getListaArquivos().addAll(lista);
		// getListaArquivos().clear();
		// for (ArquivoVO arquivo : lista) {
		// if (arquivo.getApresentarPortalCoordenador()) {
		// getListaArquivos().add(arquivo);
		// }
		// }
		executarMetodoControle(VisaoProfessorControle.class.getSimpleName(), "inicializarUploadArquivosProfessor");
		registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Menu Download Arquivos Instituicionais - Visao Professor", "Downloading");
		return Uteis.getCaminhoRedirecionamentoNavegacao("downloadArquivosComunsCoordenador.xhtml");
	}

	@PostConstruct
	public void inicializarDadosVisaoAluno() {
		if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
			try {
				registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Menu Download - Visao Aluno", "Downloading");
				String matricula = "";
				setMatriculaVO(new MatriculaVO());
				VisaoAlunoControle visaoAluno = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
				if (visaoAluno != null) {
					visaoAluno.inicializarDownloadArquivos();
					matricula = visaoAluno.getMatricula().getMatricula();
					setMatriculaVO(visaoAluno.getMatricula());
				}
				getFacadeFactory().getArquivoFacade().validarDownloadArquivoAluno(getUsuarioLogadoClone());
				novo();
				executarVerificacaoApresentarComboboxPeriodoVisaoAluno();
				registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Menu Download - Visao Aluno", "Downloading");
				verificarQuantidadeDownloadsNaoRealizadosDownloadVisaoAluno();
			} catch (Exception e) {

			}
		}
	}

	public String novoVisaoAlunoArquivosComuns() throws Exception {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Menu Download Arquivos Comuns - Visao Aluno", "Downloading");
			String matricula = "";
			VisaoAlunoControle visaoAluno = (VisaoAlunoControle) context().getExternalContext().getSessionMap().get("VisaoAlunoControle");
			if (visaoAluno != null) {
				visaoAluno.inicializarDownloadArquivos();
				// matricula = visaoAluno.getMatricula().getMatricula();
				// getMatriculaVO().setMatricula(matricula);
				// getFacadeFactory().getMatriculaFacade().carregarDados(getMatriculaVO(),
				// NivelMontarDados.BASICO, getUsuarioLogado());
				// getArquivoVO().setNivelEducacional(getMatriculaVO().getCurso().getNivelEducacional());
				List<ArquivoVO> lista = getFacadeFactory().getArquivoFacade().consultarArquivoInstituicionalParaAluno(getUsuarioLogado(), visaoAluno.getMatricula().getMatricula(), null);
				getListaArquivos().clear();
				getListaArquivos().addAll(lista);
			}
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Menu Download - Visao Aluno", "Downloading");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("downloadArquivosComuns.xhtml");
	}

	public void consultaArquivos() {
		try {
			if (!Uteis.isAtributoPreenchido(getDisciplinaVO())) {
				throw new Exception("O campo DISCIPLINA deve ser informado.");
			}
			setArquivosAnexadosDisciplinaTurma(getFacadeFactory().getArquivoFacade().consultarArquivoPorDisciplinaTurma(getDisciplinaVO().getCodigo(), getTurmaVO().getCodigo(), getProfessorVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado(),
					getCampoConsultaSituacao(),getAnoPeriodoUpload(),getSemestrePeriodoUpload(),getAnoPeriodoDisponibilizacao(),getSemestrePeriodoDisponibilizacao()));
		 
			setArquivoVOs(null);
			montarDadosArquivoIndice(getArquivosAnexadosDisciplinaTurma());
			setMensagemID("msg_dados_consultados");
			setNomeTab("tabArquivos");
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultaArquivosBackUp() {
		try {
			setArquivosAnexadosDisciplinaTurma(getFacadeFactory().getArquivoFacade().consultarArquivoPorDisciplinaTurmaBackUp(getDisciplinaVO().getCodigo(), getTurmaVO().getCodigo(), getProfessorVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			setNomeDisciplinaTela(getDisciplinaVO().getNome());
			setNomeTab("tabArquivos");
		} catch (Exception e) {
			setArquivosAnexadosDisciplinaTurma(null);
		}
	}

	@SuppressWarnings("LocalVariableHidesMemberVariable")
	public void montarListaSelectItemTurmaDisciplina() {
		List listaResultado = null;
		Iterator i = null;
		try {
			List obj = new ArrayList(0);
			listaResultado = consultarTurmasPorDisciplina();
			obj.add(new SelectItem(0, ""));
			i = listaResultado.iterator();
			String value = "";
			while (i.hasNext()) {
				TurmaVO turma = (TurmaVO) i.next();
				if (turma.getTurmaAgrupada()) {
					value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
				} else {
					value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno " + turma.getTurno().getNome();
				}
				obj.add(new SelectItem(turma.getCodigo(), value));
			}
			setListaSelectItemTurmaDisciplina(obj);
		} catch (Exception e) {
			setListaSelectItemTurmaDisciplina(new ArrayList(0));
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			i = null;
		}
	}

	@SuppressWarnings("LocalVariableHidesMemberVariable")
	public List consultarTurmasPorDisciplina() throws Exception {
		List listaConsulta = getFacadeFactory().getTurmaFacade().consultarTurmaPorDisciplina(getArquivoVO().getDisciplina().getNome(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return listaConsulta;
	}

	@SuppressWarnings("LocalVariableHidesMemberVariable")
	public void montarListaSelectItemTurmaProfessor() {
		List<TurmaVO> listaTurmas = null;
		Map<Integer, String> mapAuxiliarSelectItem = new HashMap<Integer, String>(0);
		String value = null;
		String nomeCurso = null;
		try {
			listaTurmas = consultarTurmaPorProfessor();
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, "TODAS AS TURMAS"));
			for (TurmaVO turmaVO : listaTurmas) {
				nomeCurso = this.aplicarRegraNomeCursoApresentarCombobox(turmaVO);
				value = turmaVO.getIdentificadorTurma() + " : " + nomeCurso + turmaVO.getTurno().getNome();
				mapAuxiliarSelectItem.put(turmaVO.getCodigo(), value);

			}
			for (Map.Entry<Integer, String> item : mapAuxiliarSelectItem.entrySet()) {
				Integer chave = item.getKey();
				String valor = item.getValue();
				getListaSelectItemTurma().add(new SelectItem(chave, valor));
			}
			SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
			Collections.sort(getListaSelectItemTurma(), ordenador);

			removerObjetoMemoria(turmaVO);
			mapAuxiliarSelectItem = null;
			ordenador = null;
			montarListaDisciplinaTurmaVisaoProfessor();
		} catch (Exception e) {
			getListaSelectItemTurma().clear();
		} finally {
			Uteis.liberarListaMemoria(listaTurmas);
			value = null;
			nomeCurso = null;
		}
	}

	public List<TurmaVO> consultarTurmaPorProfessor() throws Exception {
		if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), getBuscarTurmasAnteriores(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, true, true);
		} else if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorPosGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), getBuscarTurmasAnteriores(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), true, false, true);
		} else {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), getBuscarTurmasAnteriores(), "AT", 0, getUsuarioLogado().getVisaoLogar().equals("professor"), false, false, true);
		}
//		return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado().getVisaoLogar().equals("professor"), true, true);
	}

	public void montarListaDisciplinaTurmaVisaoProfessor() {
		try {
			getArquivoVO().setDisciplina(null);
			getArquivosAnexadosInstituicao().clear();
			setArquivosAnexadosInstituicao(null);
			getArquivosAnexadosProfessor().clear();
			setArquivosAnexadosProfessor(null);
			getArquivoVOs().clear();
			getMapArquivoAgrupamentoDisciplinaTurmaProfessor().clear();
			montarListaSelectItemDisciplinasProfessor();
			if (Uteis.isAtributoPreenchido(getDisciplinaVO())) {
				consultarArquivosVisaoProfessor();
			}
		} catch (Exception e) {
			setListaSelectItemDisciplinasTurma(null);
		}
	}

	public List<DisciplinaVO> consultarDisciplinaProfessorTurma() throws Exception {
		return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurma(getUsuarioLogado().getPessoa().getCodigo(), getTurmaVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
	}

	public void consultarArquivos() throws Exception {
		try {
			getMapArquivoAgrupamentoDisciplinaTurmaProfessor().clear();
			getArquivoVOs().clear();
			consultarArquivosDaInstituicao();
			consultarArquivosDoProfessor();
			if (getArquivoVO().getDisciplina().getCodigo() == null || getArquivoVO().getDisciplina().getCodigo().equals(0)) {
				setMensagemID("msg_entre_dados");
			} else {
				verificarQuantidadeDiasMaximoLimiteDownload();
			}
			setTurma(getArquivoVO().getTurma().getCodigo());
			if (getArquivoVOs().isEmpty()) {
				setMensagemID("msg_DownloadArquivo_inexistente");
			} else {
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setArquivosAnexadosInstituicao(null);
			setArquivosAnexadosInstituicao(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarArquivosVisaoProfessor() throws Exception {
		try {
			getMapArquivoAgrupamentoDisciplinaTurmaProfessor().clear();
			getArquivoVOs().clear();
			getArquivosAnexadosProfessor().clear();
			consultarArquivosDaInstituicaoVisaoProfessor();
			consultarArquivosDoProfessorVisaoProfessor();
			if (getArquivoVOs().isEmpty()) {
				setMensagemID("msg_DownloadArquivo_inexistente");
			} else {
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setArquivosAnexadosInstituicao(null);
			setArquivosAnexadosInstituicao(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarArquivosVisaoCoordenador() throws Exception {
		try {
			getArquivoVOs().clear();
			getMapArquivoAgrupamentoDisciplinaTurmaProfessor().clear();
			getArquivosAnexadosProfessor().clear();
			consultarArquivosDaInstituicaoVisaoCoordenador();
			consultarArquivosDoProfessorVisaoCoordenador();
			if (getArquivoVOs().isEmpty()) {
				setMensagemID("msg_DownloadArquivo_inexistente");
			} else {
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			setArquivosAnexadosInstituicao(null);
			setArquivosAnexadosProfessor(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarArquivosDaInstituicao() throws Exception {
		String ano = "";
		String semestre = "";
		if (!getAnoSemestre().trim().isEmpty()) {
			ano = getAnoSemestre().substring(0, 4);
			if (getAnoSemestre().length() == 6) {
				semestre = getAnoSemestre().substring(5, 6);
			}
		}
		setArquivosAnexadosInstituicao(getFacadeFactory().getArquivoFacade().consultarArquivoAtivosPorMatriculaDisciplinaAnoSemestreOrigem(getMatriculaVO().getMatricula(), getDisciplinaVO().getCodigo(), ano, semestre, OrigemArquivo.INSTITUICAO.getValor()));
	}

	public void consultarArquivosDoProfessor() throws Exception {
		String ano = "";
		String semestre = "";
		if (!getAnoSemestre().trim().isEmpty()) {
			ano = getAnoSemestre().substring(0, 4);
			if (getAnoSemestre().length() == 6) {
				semestre = getAnoSemestre().substring(5, 6);
			}
		}
		setArquivosAnexadosProfessor(getFacadeFactory().getArquivoFacade().consultarArquivoAtivosPorMatriculaDisciplinaAnoSemestreOrigem(getMatriculaVO().getMatricula(), getDisciplinaVO().getCodigo(), ano, semestre, OrigemArquivo.PROFESSOR.getValor()));
		montarDadosArquivoIndice(getArquivosAnexadosProfessor());
	}

	public void consultarArquivosDaInstituicaoVisaoProfessor() throws Exception {
		setArquivosAnexadosInstituicao(getFacadeFactory().getArquivoFacade().consultarArquivosAtivosPorDisciplinaTurmaInstituicaoVisaoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getTurmaVO().getCodigo(), getDisciplinaVO().getCodigo()));
	}

	public void consultarArquivosDoProfessorVisaoProfessor() throws Exception {
		setArquivosAnexadosProfessor(getFacadeFactory().getArquivoFacade().consultarArquivoAtivosPorProfessorDisciplinaTurmaVisaoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getTurmaVO().getCodigo(), getDisciplinaVO().getCodigo(),getCampoConsultaSituacao(),getAnoPeriodoUpload(),getSemestrePeriodoUpload(),getAnoPeriodoDisponibilizacao(),getSemestrePeriodoDisponibilizacao()));
		montarDadosArquivoIndice(getArquivosAnexadosProfessor());
	}

	public void consultarArquivosDaInstituicaoVisaoCoordenador() throws Exception {
		setArquivosAnexadosInstituicao(getFacadeFactory().getArquivoFacade().consultarArquivosAtivosPorDisciplinaTurmaInstituicaoVisaoCoordenador(getTurmaVO().getCodigo(), getDisciplinaVO().getCodigo()));
	}

	public void consultarArquivosDoProfessorVisaoCoordenador() throws Exception {
		setArquivosAnexadosProfessor(getFacadeFactory().getArquivoFacade().consultarArquivoAtivosPorDisciplinaTurmaVisaoCoordenador(getTurmaVO().getCodigo(), getDisciplinaVO().getCodigo(), getUnidadeEnsinoLogado().getCodigo(),getCampoConsultaSituacao(),getAnoPeriodoUpload(),getSemestrePeriodoUpload(),getAnoPeriodoDisponibilizacao(),getSemestrePeriodoDisponibilizacao()));
		montarDadosArquivoIndice(getArquivosAnexadosProfessor());
	}

	private void montarDadosArquivoIndice(List<ArquivoVO> arquivosConsultadosVOs) throws Exception {
		setArquivoVOs(null);
		setMapArquivoAgrupamentoDisciplinaTurmaProfessor(getFacadeFactory().getArquivoFacade().montarDadosArquivoIndice(arquivosConsultadosVOs, getUsuarioLogado()));
		getArquivoVOs().addAll(getMapArquivoAgrupamentoDisciplinaTurmaProfessor().values());
		if(!arquivosConsultadosVOs.isEmpty()) {
			persistirDadosPadroesUltimaConsultaRealizada();
		}
	}

	public void consultarArquivosPorDisciplina() throws Exception {
		try {
			setArquivosAnexadosProfessor(getFacadeFactory().getArquivoFacade().consultarArquivoPorTurmaDisciplinaOrigemAtivos(getTurmaVO().getCodigo(), getDisciplinaVO().getCodigo(), OrigemArquivo.PROFESSOR, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setArquivosAnexadosInstituicao(getFacadeFactory().getArquivoFacade().consultarArquivoPorTurmaDisciplinaOrigemAtivos(0, getDisciplinaVO().getCodigo(), OrigemArquivo.INSTITUICAO, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarArquivosResposta() {
		try {
			selecionarArquivo();
			setListaArquivosResposta(getFacadeFactory().getArquivoFacade().consultarArquivosRespostaDosAlunos(OrigemArquivo.ALUNO, getArquivoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void zeraMensagens() throws Exception {
		setMensagem("");
		setMensagemDetalhada("");
		consultarArquivosDaInstituicao();
	}

	public void zeraMensagensLimpo() throws Exception {
		setMensagem("");
		setMensagemDetalhada("");
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				// objs =
				// getFacadeFactory().getTurmaFacade().consultarTurmaPorDisciplina(getArquivoVO().getDisciplina().getNome(),
				// false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,
				// getUsuarioLogado());
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCodigoDisciplina(getValorConsultaTurma(), getArquivoVO().getDisciplina().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			if(getListaConsultaTurma().isEmpty()) {
				throw new Exception("Não a uma Turma vinculada a Disciplina ( " + getDisciplinaVO().getNome() + " )");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			setTurmaVO((TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens"));
			getArquivosAnexadosDisciplinaTurma().clear();
			getMapArquivoAgrupamentoDisciplinaTurmaProfessor().clear();
			getArquivoVOs().clear();
			getArquivoVO().setTurma(getTurmaVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparTurma() {
		try {
			setTurmaVO(null);
			setArquivosAnexadosDisciplinaTurma(null);
			setArquivoVOs(null);
			getArquivosAnexadosDisciplinaTurma().clear();
			getMapArquivoAgrupamentoDisciplinaTurmaProfessor().clear();
			getArquivoVOs().clear();
			getArquivoVO().setTurma(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		return itens;
	}

	public void montarListaDisciplinaTurmaVisaoAluno() {
		try {
			List<DisciplinaVO> resultado = consultarDisciplinaAluno(false);
			setListaSelectItemDisciplinasTurma(UtilSelectItem.getListaSelectItem(resultado, "codigo", "nome", false));
			getArquivosAnexadosInstituicao().clear();
			getArquivosAnexadosProfessor().clear();
			MatriculaPeriodoTurmaDisciplinaVO mptd = (MatriculaPeriodoTurmaDisciplinaVO)context().getExternalContext().getSessionMap().remove("disciplinaSelecionada");
			if(getVisaoAlunoControle() != null && Uteis.isAtributoPreenchido(mptd)
					&& getListaSelectItemDisciplinasTurma().stream().anyMatch(t -> t.getValue().equals(mptd.getDisciplina().getCodigo()))) {
				getDisciplinaVO().setCodigo(mptd.getDisciplina().getCodigo());				
				consultarArquivos();
			}else if (!getListaSelectItemDisciplinasTurma().isEmpty()) {
				getDisciplinaVO().setCodigo((Integer) ((SelectItem) getListaSelectItemDisciplinasTurma().get(0)).getValue());
				consultarArquivos();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			setListaSelectItemDisciplinasTurma(new ArrayList<>(0));
		}
	}

	public List<DisciplinaVO> consultarDisciplinaAluno(boolean periodoLetivoAtual) throws Exception {
		String ano = "";
		String semestre = "";
		if (!getAnoSemestre().trim().isEmpty() && !periodoLetivoAtual) {
			ano = getAnoSemestre().substring(0, 4);
			if (getAnoSemestre().length() == 6) {
				semestre = getAnoSemestre().substring(5, 6);
			}
		} else {
			ano = Uteis.getAnoDataAtual();
			semestre = Uteis.getSemestreAtual();
		}
		return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaAluno(getUnidadeEnsinoLogado().getCodigo(), checarPeriodicidade(), getMatriculaVO().getMatricula(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado(), ano, semestre);
	}

	@SuppressWarnings("LocalVariableHidesMemberVariable")
	private String checarPeriodicidade() throws Exception {
		return getMatriculaVO().getCurso().getPeriodicidade();
		// MatriculaVO matriculaVO = new MatriculaVO();
		// try {
		// setMatriculaVOs(getFacadeFactory().getMatriculaFacade().consultarMatriculaPorCodigoPessoa(getRealizarValidacaoParaObterQualSeraUsuarioCorrente(getUsuarioLogadoClone()).getPessoa().getCodigo(),
		// getUnidadeEnsinoLogado().getCodigo(), false,
		// Uteis.NIVELMONTARDADOS_DADOSBASICOS,
		// getConfiguracaoFinanceiroPadraoSistema(),
		// getUsuarioLogado()));
		// matriculaVO = getMatriculaVOs().get(0);
		// if (matriculaVO.getCurso().getPeriodicidade().equals("AN")) {
		// return "AN";
		// } else if (matriculaVO.getCurso().getPeriodicidade().equals("IN")) {
		// return "IN";
		// } else if (matriculaVO.getCurso().getPeriodicidade().equals("SE")) {
		// return "SE";
		// }
		// return "";
		// } finally {
		// matriculaVO = null;
		// }
	}

	public String selecionarArquivo() {
		try {
			ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
			setArquivoVO(obj);
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	public String selecionarArquivoParaResponderProfessor() {
		try {
			ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
			setCodigoArquivoBase(obj.getCodigo());
			setArquivoVO(getFacadeFactory().getArquivoFacade().consultarDadosDoArquivoASerRespondido(obj.getCodigo(), getUsuarioLogado()));
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	public String getCaminhoServidorDownload() {
		try {
			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getArquivoVO(), PastaBaseArquivoEnum.ARQUIVO, getConfiguracaoGeralPadraoSistema());
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public String getCaminhoServidorDownloadArquivosComuns() {
		try {
			ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
			return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + "/" + PastaBaseArquivoEnum.COMUM.getValue() + "/" + obj.getNivelEducacional() + "/" + obj.getNome();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}
	
	public void realizarDownload() {
		ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
		try {
			realizarDownloadArquivo(obj);
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getCaminhoServidorDownloadArquivo() {
		try {
			if (getArquivoVO().getOrigem().equals("IN")) {
				return "location.href='" + getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getArquivoVO(), PastaBaseArquivoEnum.COMUM, getConfiguracaoGeralPadraoSistema()) + "'";
			}
			return "location.href='" + getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getArquivoVO(), PastaBaseArquivoEnum.ARQUIVO, getConfiguracaoGeralPadraoSistema()) + "'";
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public String getDownloadArquivo() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Download Arquivo - Visao Aluno/Professor", "Downloading");
			if (getArquivoVO() != null && getUsuarioLogado().getIsApresentarVisaoAluno() && !getUsuarioLogado().getPermiteSimularNavegacaoAluno()) {
				getFacadeFactory().getDownloadFacade().registrarDownload(getArquivoVO(), getUsuarioLogado(), getVisaoAlunoControle().getMatriculaPeriodoVO(), getMatriculaVO(), getConfiguracaoFinanceiroPadraoSistema());
			}
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.setAttribute("codigoArquivo", getArquivoVO().getCodigo());
			request.setAttribute("nomeArquivo", arquivoVO.getNome());
			request.setAttribute("urlAcessoArquivo", getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(arquivoVO, PastaBaseArquivoEnum.ARQUIVO, getConfiguracaoGeralPadraoSistema()));
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Download Arquivo - Visao Aluno/Professor", "Downloading");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "";
	}

	public String getRegistrarDownloadVisaoAluno() throws Exception {
		selecionarArquivo();
		getFacadeFactory().getDownloadFacade().registrarDownload(getArquivoVO(), getUsuarioLogado(), getVisaoAlunoControle().getMatriculaPeriodoVO(), getMatriculaVO(), getConfiguracaoFinanceiroPadraoSistema());
		return "";
	}

	public void registrarDownloadVisaoAluno() throws Exception {
		selecionarArquivo();
		getFacadeFactory().getDownloadFacade().registrarDownload(getArquivoVO(), getUsuarioLogado(), getVisaoAlunoControle().getMatriculaPeriodoVO(), getMatriculaVO(), getConfiguracaoFinanceiroPadraoSistema());
	}

	public void realizarDownloadArquivoResposta() {
		getDownloadArquivo();
		getArquivoVO().setDownloadRealizado(true);
	}

	public void verificarPermissaoParaGravarUploadVisaoCoordenador() {
		try {
			if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
				ControleAcesso.incluir("Upload", getUsuarioLogado());
				setPermiteGravarVisaoCoordenador(true);
			}
		} catch (Exception e) {
			setPermiteGravarVisaoCoordenador(false);
		}
	}

	public String getCampoConsultarDisciplina() {
		return campoConsultarDisciplina;
	}

	public void setCampoConsultarDisciplina(String campoConsultarDisciplina) {
		this.campoConsultarDisciplina = campoConsultarDisciplina;
	}

	public String getValorConsultarDisciplina() {
		return valorConsultarDisciplina;
	}

	public void setValorConsultarDisciplina(String valorConsultarDisciplina) {
		this.valorConsultarDisciplina = valorConsultarDisciplina;
	}

	public List getListaConsultarDisciplina() {
		return listaConsultarDisciplina;
	}

	public void setListaConsultarDisciplina(List listaConsultarDisciplina) {
		this.listaConsultarDisciplina = listaConsultarDisciplina;
	}

	public String getResponsavelUpload_Erro() {
		return responsavelUploadErro;
	}

	public void setResponsavelUpload_Erro(String responsavelUpload_Erro) {
		this.responsavelUploadErro = responsavelUpload_Erro;
	}

	public ArquivoVO getArquivoVO() {
		if (arquivoVO == null) {
			arquivoVO = new ArquivoVO();
		}
		return arquivoVO;
	}

	public void setArquivoVO(ArquivoVO arquivoVO) {
		this.arquivoVO = arquivoVO;
	}

	public List<SelectItem> getListaDisciplinaTurma() {
		if (listaDisciplinaTurma == null) {
			listaDisciplinaTurma = new ArrayList<SelectItem>(0);
		}
		return listaDisciplinaTurma;
	}

	public void setListaDisciplinaTurma(List<SelectItem> listaDisciplinaTurma) {
		this.listaDisciplinaTurma = listaDisciplinaTurma;
	}

	public Boolean getMostrarComboDisciplina() {
		return mostrarComboDisciplina;
	}

	public void setMostrarComboDisciplina(Boolean mostrarComboDisciplina) {
		this.mostrarComboDisciplina = mostrarComboDisciplina;
	}

	public Boolean getMostrarComboTurma() {
		return mostrarComboTurma;
	}

	public void setMostrarComboTurma(Boolean mostrarComboTurma) {
		this.mostrarComboTurma = mostrarComboTurma;
	}

	public List<ArquivoVO> getListaArquivos() {
		if (listaArquivos == null) {
			listaArquivos = new ArrayList<ArquivoVO>(0);
		}
		return listaArquivos;
	}

	public void setListaArquivos(List<ArquivoVO> listaArquivos) {
		this.listaArquivos = listaArquivos;
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

	public List<SelectItem> getListaSelectItemDisciplinasTurma() {
		if (listaSelectItemDisciplinasTurma == null) {
			listaSelectItemDisciplinasTurma = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplinasTurma;
	}

	public void setListaSelectItemDisciplinasTurma(List listaSelectItemDisciplinasTurma) {
		this.listaSelectItemDisciplinasTurma = listaSelectItemDisciplinasTurma;
	}

	public List getListaSelectItemTurmaDisciplina() {
		if (listaSelectItemTurmaDisciplina == null) {
			listaSelectItemTurmaDisciplina = new ArrayList(0);
		}
		return listaSelectItemTurmaDisciplina;
	}

	public void setListaSelectItemTurmaDisciplina(List listaSelectItemTurmaDisciplina) {
		this.listaSelectItemTurmaDisciplina = listaSelectItemTurmaDisciplina;
	}

	public List<ArquivoVO> getArquivosAnexadosInstituicao() {
		if (arquivosAnexadosInstituicao == null) {
			arquivosAnexadosInstituicao = new ArrayList(0);
		}
		return arquivosAnexadosInstituicao;
	}

	public void setArquivosAnexadosInstituicao(List<ArquivoVO> arquivosAnexadosInstituicao) {
		this.arquivosAnexadosInstituicao = arquivosAnexadosInstituicao;
	}

	public List<ArquivoVO> getArquivosAnexadosProfessor() {
		if (arquivosAnexadosProfessor == null) {
			arquivosAnexadosProfessor = new ArrayList<ArquivoVO>(0);
		}
		return arquivosAnexadosProfessor;
	}

	public void setArquivosAnexadosProfessor(List<ArquivoVO> arquivosAnexadosProfessor) {
		this.arquivosAnexadosProfessor = arquivosAnexadosProfessor;
	}

	public List<ArquivoVO> getArquivosAnexadosDisciplinaTurma() {
		if (arquivosAnexadosDisciplinaTurma == null) {
			arquivosAnexadosDisciplinaTurma = new ArrayList<ArquivoVO>(0);
		}
		return arquivosAnexadosDisciplinaTurma;
	}

	public void setArquivosAnexadosDisciplinaTurma(List<ArquivoVO> arquivosAnexadosDisciplinaTurma) {
		this.arquivosAnexadosDisciplinaTurma = arquivosAnexadosDisciplinaTurma;
	}

	public List<ArquivoVO> getArquivoVOs() {
		if (arquivoVOs == null) {
			arquivoVOs = new ArrayList<ArquivoVO>(0);
		}
		return arquivoVOs;
	}

	public void setArquivoVOs(List<ArquivoVO> arquivoVOs) {
		this.arquivoVOs = arquivoVOs;
	}

	public String getNomeDisciplinaTela() {
		if (nomeDisciplinaTela == null) {
			nomeDisciplinaTela = "";
		}
		return nomeDisciplinaTela;
	}

	public void setNomeDisciplinaTela(String nomeDisciplinaTela) {
		this.nomeDisciplinaTela = nomeDisciplinaTela;
	}

	public String getNomeTab() {
		if (nomeTab == null) {
			nomeTab = "";
		}
		return nomeTab;
	}

	public void setNomeTab(String nomeTab) {
		this.nomeTab = nomeTab;
	}

	public DisciplinaVO getDisciplinaVO() {
		if (disciplinaVO == null) {
			disciplinaVO = new DisciplinaVO();
		}
		return disciplinaVO;
	}

	public void setDisciplinaVO(DisciplinaVO disciplinaVO) {
		this.disciplinaVO = disciplinaVO;
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

	public List<ArquivoVO> getListaArquivosResposta() {
		if (listaArquivosResposta == null) {
			listaArquivosResposta = new ArrayList<ArquivoVO>(0);
		}
		return listaArquivosResposta;
	}

	public void setListaArquivosResposta(List<ArquivoVO> listaArquivosResposta) {
		this.listaArquivosResposta = listaArquivosResposta;
	}

	public void setMatriculaVOs(List<MatriculaVO> matriculaVOs) {
		this.matriculaVOs = matriculaVOs;
	}

	public List<MatriculaVO> getMatriculaVOs() {
		if (matriculaVOs == null) {
			matriculaVOs = new ArrayList<MatriculaVO>(0);
		}
		return matriculaVOs;
	}

	public void setCodigoArquivoBase(Integer codigoArquivoBase) {
		this.codigoArquivoBase = codigoArquivoBase;
	}

	public Integer getCodigoArquivoBase() {
		if (codigoArquivoBase == null) {
			codigoArquivoBase = 0;
		}
		return codigoArquivoBase;
	}

	public boolean getIsApresentarDadosArquivoAposSelecionarDisciplina() {
		return getArquivoVO().getDisciplina().getCodigo() != 0;
	}

	public void verificarPermiteProfessorExcluirArquivosInstituicao() {
		Boolean liberar;
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidade("PermitirProfessorExcluirArquivoInstituicao", getUsuarioLogado());
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		setPermiteProfessorExcluirArquivosInstituicao(liberar);
	}

	public Boolean getPermiteProfessorExcluirArquivosInstituicao() {
		if (permiteProfessorExcluirArquivosInstituicao == null) {
			permiteProfessorExcluirArquivosInstituicao = Boolean.FALSE;
		}
		return permiteProfessorExcluirArquivosInstituicao;
	}

	public void setPermiteProfessorExcluirArquivosInstituicao(Boolean permiteProfessorExcluirArquivosInstituicao) {
		this.permiteProfessorExcluirArquivosInstituicao = permiteProfessorExcluirArquivosInstituicao;
	}

	public void removerArquivoListaInstituicao() throws Exception {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Iniciando Remover Arquivo Download - Lista Instituicao", "Downloading - Removendo");
			ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
			obj.setManterDisponibilizacao(false);
			obj.setDataIndisponibilizacao(Uteis.getDataJDBCTimestamp(new Date()));
			getFacadeFactory().getArquivoFacade().alterarManterDisponibilizacao(obj, true, "Upload", getUsuarioLogado());
			getFacadeFactory().getArquivoFacade().excluir(obj, true, "Upload", getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			excluirObjArquivoVOInstituicao(obj.getNome());
			setMensagemID("msg_dados_excluidos");
			registrarAtividadeUsuario(getUsuarioLogado(), "ArquivoControle", "Finalizando Remover Arquivo Download - Lista Instituicao", "Downloading - Removendo");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void excluirObjArquivoVOInstituicao(String nome) throws Exception {
		int index = 0;
		Iterator i = getArquivosAnexadosInstituicao().iterator();
		while (i.hasNext()) {
			ArquivoVO objExistente = (ArquivoVO) i.next();
			if (objExistente.getNome().equals(nome)) {
				getArquivosAnexadosInstituicao().remove(index);
				return;
			}
			index++;
		}
	}

	public boolean getApresentarListaArquivosAnexadosProfessor() {
		if (getArquivosAnexadosProfessor().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public boolean getApresentarListaArquivosAnexadosInstituicao() {
		if (getArquivosAnexadosInstituicao().isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public void verificarQuantidadeDiasMaximoLimiteDownload() throws Exception {
		try {
			getMatriculaVO().setCurso(getFacadeFactory().getCursoFacade().consultaRapidaPorMatricula(getMatriculaVO().getMatricula(), false, getUsuarioLogado()));
			if ((!getArquivosAnexadosProfessor().isEmpty() || !getArquivosAnexadosInstituicao().isEmpty()) && getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {
				if (!getMatriculaVO().getCurso().getLimitarQtdeDiasMaxDownload()) {
					getMatriculaVO().getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorChavePrimaria(getMatriculaVO().getCurso().getConfiguracaoAcademico().getCodigo(), getUsuarioLogado()));
				}
				if (getMatriculaVO().getCurso().getLimitarQtdeDiasMaxDownload() || getMatriculaVO().getCurso().getConfiguracaoAcademico().getLimitarQtdeDiasMaxDownload()) {
					setDentroDataMaximaLimiteDownload(getFacadeFactory().getArquivoFacade().verificarDataDownloadDentroQuantidadeDiasMaximoLimite(getMatriculaVO().getMatricula(), getMatriculaVO().getCurso(), getArquivoVO().getTurma(), getArquivoVO().getDisciplina()));
				} else {
					setDentroDataMaximaLimiteDownload(true);
				}
				if (!getDentroDataMaximaLimiteDownload()) {
					throw new ConsistirException("Data limite para download de material foi ultrapassada. Favor entrar em contato com a Instituição de Ensino.");
				} else {
					setMensagemID("");
					setMensagem("");
				}
			} else {
				setDentroDataMaximaLimiteDownload(true);
				setMensagemID("");
				setMensagem("");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	public static void verificarPermissaoUsuarioRealizarUpload(UsuarioVO usuario, String nomeEntidade) throws Exception {
		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public void verificarUsuarioPossuiPermissaoRealizarUpload() {
		Boolean liberar = false;
		try {
			verificarPermissaoUsuarioRealizarUpload(getUsuarioLogado(), "PermitirUsuarioRealizarUpload");
			liberar = true;
		} catch (Exception e) {
			liberar = false;
		}
		this.setPermitirUsuarioRealizarUpload(liberar);
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

	public void montarListaSelectItemDisciplinasProfessor() throws Exception {
		List<DisciplinaVO> resultadoConsulta = null;
		getListaSelectItemDisciplinasTurma().clear();
		getListaSelectItemDisciplinasTurma().add(new SelectItem(0, ""));
//		String semestreAtual = Uteis.getSemestreAtual();
//		String anoAtual = Uteis.getData(new Date(), "yyyy");
//		if(getBuscarTurmasAnteriores()){
//			semestreAtual = "";
//			anoAtual= "";
//		}
		resultadoConsulta = getFacadeFactory().getDisciplinaFacade().consultarDisciplinasDoProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), null,  getTurmaVO().getCodigo(), getSemestre(), getAno(), Uteis.NIVELMONTARDADOS_COMBOBOX, true,false, getUsuarioLogado());
		Iterator<DisciplinaVO> i = resultadoConsulta.iterator();
		while (i.hasNext()) {
			DisciplinaVO obj =  i.next();
			getListaSelectItemDisciplinasTurma().add(new SelectItem(obj.getCodigo(), obj.getCodigo() + " - " + obj.getNome()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort(getListaSelectItemDisciplinasTurma(), ordenador);
		resultadoConsulta = null;
	}

	public Boolean getApresentarCampoCpf() {
		return getCampoConsultaProfessor().equals("cpf");
	}

	/**
	 * @return the campoConsultaProfessor
	 */
	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = "";
		}
		return campoConsultaProfessor;
	}

	/**
	 * @param campoConsultaProfessor
	 *            the campoConsultaProfessor to set
	 */
	public void setCampoConsultaProfessor(String campoConsultaProfessor) {
		this.campoConsultaProfessor = campoConsultaProfessor;
	}

	/**
	 * @return the valorConsultaProfessor
	 */
	public String getValorConsultaProfessor() {
		if (valorConsultaProfessor == null) {
			valorConsultaProfessor = "";
		}
		return valorConsultaProfessor;
	}

	/**
	 * @param valorConsultaProfessor
	 *            the valorConsultaProfessor to set
	 */
	public void setValorConsultaProfessor(String valorConsultaProfessor) {
		this.valorConsultaProfessor = valorConsultaProfessor;
	}

	/**
	 * @return the listaConsultaProfessor
	 */
	public List getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList(0);
		}
		return listaConsultaProfessor;
	}

	/**
	 * @param listaConsultaProfessor
	 *            the listaConsultaProfessor to set
	 */
	public void setListaConsultaProfessor(List listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	public List<SelectItem> getTipoConsultaComboProfessor() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public Boolean getApresentarDataDisponibilizacaoMaterial() {
		if (apresentarDataDisponibilizacaoMaterial == null) {
			apresentarDataDisponibilizacaoMaterial = Boolean.FALSE;
		}
		return apresentarDataDisponibilizacaoMaterial;
	}

	public void setApresentarDataDisponibilizacaoMaterial(Boolean apresentarDataDisponibilizacaoMaterial) {
		this.apresentarDataDisponibilizacaoMaterial = apresentarDataDisponibilizacaoMaterial;
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

	public Boolean getDentroDataMaximaLimiteDownload() {
		if (dentroDataMaximaLimiteDownload == null) {
			dentroDataMaximaLimiteDownload = false;
		}
		return dentroDataMaximaLimiteDownload;
	}

	public void setDentroDataMaximaLimiteDownload(Boolean dentroDataMaximaLimiteDownload) {
		this.dentroDataMaximaLimiteDownload = dentroDataMaximaLimiteDownload;
	}

	public Boolean getPermiteGravarVisaoCoordenador() {
		if (permiteGravarVisaoCoordenador == null) {
			verificarPermissaoParaGravarUploadVisaoCoordenador();
		}
		return permiteGravarVisaoCoordenador;
	}

	public void setPermiteGravarVisaoCoordenador(Boolean permiteGravarVisaoCoordenador) {
		this.permiteGravarVisaoCoordenador = permiteGravarVisaoCoordenador;
	}

	public void verificarArquivosExistenteHD() throws Exception {
		getFacadeFactory().getArquivoFacade().verificarArquivosExistentesHD(getConfiguracaoGeralPadraoSistema());
	}

	/**
	 * @return the turma
	 */
	public Integer getTurma() {
		if (turma == null) {
			turma = 0;
		}
		return turma;
	}

	/**
	 * @param turma
	 *            the turma to set
	 */
	public void setTurma(Integer turma) {
		this.turma = turma;
	}

	public Boolean getPermitirUsuarioRealizarUpload() {
		if (permitirUsuarioRealizarUpload == null) {
			permitirUsuarioRealizarUpload = Boolean.FALSE;
		}
		return permitirUsuarioRealizarUpload;
	}

	public void setPermitirUsuarioRealizarUpload(Boolean permitirUsuarioRealizarUpload) {
		this.permitirUsuarioRealizarUpload = permitirUsuarioRealizarUpload;
	}

	public String getErroUpload() {
		if (erroUpload == null) {
			erroUpload = "";
		}
		return erroUpload;
	}

	public void setErroUpload(String erroUpload) {
		this.erroUpload = erroUpload;
	}

	public String getMsgErroUpload() {
		if (msgErroUpload == null) {
			msgErroUpload = "";
		}
		return msgErroUpload;
	}

	public void setMsgErroUpload(String msgErroUpload) {
		this.msgErroUpload = msgErroUpload;
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

	public Boolean getBuscarTurmasAnteriores() {
		if (buscarTurmasAnteriores == null) {
			buscarTurmasAnteriores = Boolean.FALSE;
		}
		return buscarTurmasAnteriores;
	}

	public void setBuscarTurmasAnteriores(Boolean buscarTurmasAnteriores) {
		this.buscarTurmasAnteriores = buscarTurmasAnteriores;
	}

	public void alterarDisponibilidadeMaterial() throws Exception {
		try {
			getFacadeFactory().getArquivoFacade().alterarDisponibilidadeMaterial((ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens"), false, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getSubir() {
		if (subir == null) {
			subir = false;
		}
		return subir;
	}

	public void setSubir(Boolean subir) {
		this.subir = subir;
	}

	public void alterarOrdemArquivo() {
		try {
			ArquivoVO arquivo1 = (ArquivoVO) getRequestMap().get("arquivoItens");
			ArquivoVO arquivo2 = null;
			List<ArquivoVO> arquivoManipularVOs = getMapArquivoAgrupamentoDisciplinaTurmaProfessor().get(arquivo1.getKeyAgrupadorTurmaDisciplinaProfessor()).getArquivoFilhoVOs();
			if (getSubir()) {
				if (arquivo1.getIndiceAgrupador().equals(1)) {
					return;
				}
				arquivo2 = arquivoManipularVOs.get(arquivoManipularVOs.indexOf(arquivo1) - 1);
			} else {
				if (arquivo1.getIndiceAgrupador().equals(arquivoManipularVOs.size())) {
					return;
				}
				arquivo2 = arquivoManipularVOs.get(arquivoManipularVOs.indexOf(arquivo1) + 1);
			}
			if (arquivo1.getIndiceAgrupador().equals(0) || arquivo2.getIndiceAgrupador().equals(0)) {
				return;
			}
			getFacadeFactory().getArquivoFacade().alterarOrdemArquivo(arquivoManipularVOs, arquivo1, arquivo2, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarOrdemArquivoFilho() {
		try {
			ArquivoVO arquivoVO = (ArquivoVO) getRequestMap().get("arquivoItens");
			ArquivoVO arquivoFilho1 = (ArquivoVO) getRequestMap().get("arquivoFilhoVOItens");
			ArquivoVO arquivoFilho2 = null;
			if (getSubir()) {
				if (arquivoFilho1.getIndice() <= 1) {
					return;
				}
				arquivoFilho2 = arquivoVO.getArquivoFilhoVOs().get(arquivoVO.getArquivoFilhoVOs().indexOf(arquivoFilho1) - 1);
			} else {
				if (arquivoFilho1.getIndice().equals(arquivoVO.getArquivoFilhoVOs().size())) {
					return;
				}
				arquivoFilho2 = arquivoVO.getArquivoFilhoVOs().get(arquivoVO.getArquivoFilhoVOs().indexOf(arquivoFilho1) + 1);
			}
			getFacadeFactory().getArquivoFacade().alterarOrdemArquivoFilho(arquivoVO.getArquivoFilhoVOs(), arquivoFilho1, arquivoFilho2, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void editarArquivoFilho() {
		try {
			setArquivoVO((ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoFilhoVOItens"));
			getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ARQUIVO);
			// setTurmaVO(getArquivoVO().getTurma().clone());
			setAgrupadorTemp(getArquivoVO().getAgrupador());
			setQtdeCaractereLimiteDownloadMaterial(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesQtdeCaractereLimiteDownloadMaterial());
			setEditandoArquivo(true);
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarOrdemArquivo(DropEvent dropEvent) {
		try {
			ArquivoVO arquivo1 = (ArquivoVO) dropEvent.getDragValue();
			ArquivoVO arquivo2 = (ArquivoVO) dropEvent.getDropValue();
			if (arquivo1.getIndiceAgrupador().equals(0) || arquivo2.getIndiceAgrupador().equals(0)) {
				return;
			}
			List<ArquivoVO> arquivoManipularVOs = getMapArquivoAgrupamentoDisciplinaTurmaProfessor().get(arquivo1.getKeyAgrupadorTurmaDisciplinaProfessor()).getArquivoFilhoVOs();
			getFacadeFactory().getArquivoFacade().alterarOrdemArquivo(arquivoManipularVOs, arquivo1, arquivo2, getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void alterarOrdemArquivoFilho(DropEvent dropEvent) {
		try {
			ArquivoVO arquivoVO = (ArquivoVO) getRequestMap().get("arquivoItens");
			Optional<ArquivoVO> arquivo1 = Optional.ofNullable((ArquivoVO) dropEvent.getDragValue());
			Optional<ArquivoVO> arquivo2 = Optional.ofNullable((ArquivoVO) dropEvent.getDropValue());
			if (arquivo1.isPresent() && arquivo2.isPresent()) {
				List<ArquivoVO> arquivoManipularVOs = getMapArquivoAgrupamentoDisciplinaTurmaProfessor().get(arquivo1.get().getKeyAgrupadorTurmaDisciplinaProfessor()).getArquivoFilhoVOs();
				getFacadeFactory().getArquivoFacade().alterarOrdemArquivoDragDrop(arquivoManipularVOs, arquivoVO.getArquivoFilhoVOs(), arquivo1.get(), arquivo2.get(), getUsuarioLogado());
				if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
					consultarArquivosVisaoCoordenador();
				} else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					consultarArquivosVisaoProfessor();
				} else {
					consultaArquivos();
				}
			}
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void excluirArquivoFilho() {
		try {
			if (Uteis.isAtributoPreenchido(getArquivoFilhoExcluirVO())) {
				alterarOrdemArquivoFilhoExclusao(getArquivoExcluirVO(), getArquivoFilhoExcluirVO());
				getFacadeFactory().getArquivoFacade().excluir(getArquivoFilhoExcluirVO(), true, "Upload", getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
				alterarOrdemArquivoExclusao(getArquivoExcluirVO(), getArquivoFilhoExcluirVO());
				if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
					consultarArquivosVisaoCoordenador();
				} else if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					consultarArquivosVisaoProfessor();
				} else {
					consultaArquivos();
				}
			}
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			setArquivoExcluirVO(null);
			setArquivoFilhoExcluirVO(null);
		}
	}

	private void alterarOrdemArquivoFilhoExclusao(ArquivoVO arquivoVO, ArquivoVO arquivoFilhoVO) throws Exception {
		while (arquivoVO.getArquivoFilhoVOs().size() > (arquivoVO.getArquivoFilhoVOs().indexOf(arquivoFilhoVO) + 1)) {
			ArquivoVO arquivoFilho2 = arquivoVO.getArquivoFilhoVOs().get(arquivoVO.getArquivoFilhoVOs().indexOf(arquivoFilhoVO) + 1);
			getFacadeFactory().getArquivoFacade().alterarOrdemArquivoFilho(arquivoVO.getArquivoFilhoVOs(), arquivoFilhoVO, arquivoFilho2, getUsuarioLogado());
		}
	}

	private void alterarOrdemArquivoExclusao(ArquivoVO arquivoVO, ArquivoVO arquivoFilhoVO) throws Exception {
		arquivoVO.getArquivoFilhoVOs().remove(arquivoFilhoVO);
		if (getMapArquivoAgrupamentoDisciplinaTurmaProfessor().containsKey(arquivoVO.getKeyAgrupadorTurmaDisciplinaProfessor())) {
			List<ArquivoVO> arquivoVOs = getMapArquivoAgrupamentoDisciplinaTurmaProfessor().get(arquivoVO.getKeyAgrupadorTurmaDisciplinaProfessor()).getArquivoFilhoVOs();
			if (!Uteis.isAtributoPreenchido(arquivoVO.getArquivoFilhoVOs())) {
				while (arquivoVOs.size() > (arquivoVOs.indexOf(arquivoVO) + 1)) {
					ArquivoVO arquivoVO2 = arquivoVOs.get(arquivoVOs.indexOf(arquivoVO) + 1);
					getFacadeFactory().getArquivoFacade().alterarOrdemArquivo(arquivoVOs, arquivoVO, arquivoVO2, getUsuarioLogado());
				}
				arquivoVOs.remove(arquivoVO);
				if (arquivoVOs.isEmpty()) {
					getMapArquivoAgrupamentoDisciplinaTurmaProfessor().remove(arquivoVO.getKeyAgrupadorTurmaDisciplinaProfessor());
				}
			}
		}
	}

	public Boolean getEditandoArquivo() {
		if (editandoArquivo == null) {
			editandoArquivo = false;
		}
		return editandoArquivo;
	}

	public void setEditandoArquivo(Boolean editandoArquivo) {
		this.editandoArquivo = editandoArquivo;
	}

	public String getOnCompletePanelUpload() {
		if (onCompletePanelUpload == null) {
			onCompletePanelUpload = "";
		}
		return onCompletePanelUpload;
	}

	public void setOnCompletePanelUpload(String onCompletePanelUpload) {
		this.onCompletePanelUpload = onCompletePanelUpload;
	}

	private List<ArquivoVO> gerarCloneArquivo() throws Exception {
		List<ArquivoVO> arquivoTempVOs = new ArrayList<ArquivoVO>();
		for (ArquivoVO arquivo : getArquivoVOs()) {
			ArquivoVO arquivoClone = (ArquivoVO) arquivo.clone();
			arquivoClone.setArquivoFilhoVOs(null);
			for (ArquivoVO filho : arquivo.getArquivoFilhoVOs()) {
				ArquivoVO arquivoFilhoClone = (ArquivoVO) filho.clone();
				arquivoClone.getArquivoFilhoVOs().add(arquivoFilhoClone);
			}
			arquivoTempVOs.add(arquivoClone);
		}
		return arquivoTempVOs;
	}

	public void selecionarArquivoParaDownload() {
		try {
			ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
			context().getExternalContext().getSessionMap().put("arquivoVO", obj);
			if (obj != null && getUsuarioLogado().getIsApresentarVisaoAluno() && !getUsuarioLogado().getPermiteSimularNavegacaoAluno()) {
				getFacadeFactory().getDownloadFacade().registrarDownload(obj, getUsuarioLogado(), getVisaoAlunoControle().getMatriculaPeriodoVO(), getMatriculaVO(), getConfiguracaoFinanceiroPadraoSistema());
			}
			verificarQuantidadeDownloadsNaoRealizadosDownloadVisaoAluno();
//			if(!obj.getServidorArquivoOnline().equals(ServidorArquivoOnlineEnum.APACHE)) {
				context().getExternalContext().dispatch("/DownloadSV");
				FacesContext.getCurrentInstance().responseComplete();
//			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public String selecionarArquivoFilhoParaDownload() {
		try {
			ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoFilhoVOItens");
			context().getExternalContext().getSessionMap().put("arquivoVO", obj);
			if (obj != null && getUsuarioLogado().getIsApresentarVisaoAluno() && !getUsuarioLogado().getPermiteSimularNavegacaoAluno()) {
				getFacadeFactory().getDownloadFacade().registrarDownload(obj, getUsuarioLogado(), getVisaoAlunoControle().getMatriculaPeriodoVO(), getMatriculaVO(), getConfiguracaoFinanceiroPadraoSistema());
			}
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
		return "";
	}

	public void carregarDadosPanelUpload() {
		try {
			PessoaVO professor = (PessoaVO) getProfessorVO().clone();
			if (!Uteis.isAtributoPreenchido(getDisciplinaVO())) {
				throw new Exception("O campo DISCIPLINA deve ser informado.");
			}
			setArquivoVO(null);
			setListaDisciplinaTurma(null);
			setMostrarComboDisciplina(Boolean.FALSE);
			setMostrarComboTurma(Boolean.FALSE);
			setListaArquivos(null);
			setListaArquivosResposta(null);
			setListaSelectItemDisciplinasTurma(null);
			setListaSelectItemTurma(null);
			setListaSelectItemTurmaDisciplina(null);
			setArquivosAnexadosInstituicao(null);
			setArquivosAnexadosProfessor(null);
			setArquivosAnexadosDisciplinaTurma(null);
			getArquivoVO().setTurma(getTurmaVO());
			getArquivoVO().setDisciplina(getDisciplinaVO());
			getArquivoVO().setProfessor(professor);
			getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ARQUIVO);
			setArquivosAnexadosDisciplinaTurma(getFacadeFactory().getArquivoFacade().consultarArquivoPorDisciplinaTurma(getDisciplinaVO().getCodigo(), getTurmaVO().getCodigo(), getProfessorVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado(),"","","","",""));
			setQtdeCaractereLimiteDownloadMaterial(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesQtdeCaractereLimiteDownloadMaterial());
			setArquivoVOs(null);
			montarDadosArquivoIndice(getArquivosAnexadosDisciplinaTurma());
			setOnCompletePanelUpload("RichFaces.$('panelUploadArquivo').show()");
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			setOnCompletePanelUpload("");
		}
	}

	public void excluirArquivo() {
		try {
			if (Uteis.isAtributoPreenchido(getArquivoExcluirVO())) {
				getFacadeFactory().getArquivoFacade().excluir(getArquivoExcluirVO(), true, "Upload", getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
				ArquivoVO arquivoAgrupadorTurmaDisciplina = getMapArquivoAgrupamentoDisciplinaTurmaProfessor().get(getArquivoExcluirVO().getKeyAgrupadorTurmaDisciplinaProfessor());
				if (!getArquivoExcluirVO().getIndiceAgrupador().equals(0)) {
					int aux = 0;
					while (arquivoAgrupadorTurmaDisciplina.getArquivoFilhoVOs().size() > 0 
							&& arquivoAgrupadorTurmaDisciplina.getArquivoFilhoVOs().indexOf(getArquivoExcluirVO()) >= 0
							&& arquivoAgrupadorTurmaDisciplina.getArquivoFilhoVOs().size() > (arquivoAgrupadorTurmaDisciplina.getArquivoFilhoVOs().indexOf(getArquivoExcluirVO()) + 1)) {
						if (aux > 0 && (Integer)(arquivoAgrupadorTurmaDisciplina.getArquivoFilhoVOs().indexOf(getArquivoExcluirVO()) + 1) <= aux) {
							System.out.println("LOG_LOOPING_ARQUIVO");
							System.out.println("lista = " + arquivoAgrupadorTurmaDisciplina.getArquivoFilhoVOs().size());
							System.out.println("arquivo.indexof = " + (arquivoAgrupadorTurmaDisciplina.getArquivoFilhoVOs().indexOf(getArquivoExcluirVO()) + 1));
							System.out.println("arquivo.codigo = " + getArquivoExcluirVO().getCodigo());
							System.out.println("arquivo.indice = " + getArquivoExcluirVO().getIndice());
							System.out.println("arquivo.indiceAgrupador = " + getArquivoExcluirVO().getIndiceAgrupador());
							throw new Exception("Erro ao excluir arquivo!");
						} else {
							aux = (arquivoAgrupadorTurmaDisciplina.getArquivoFilhoVOs().indexOf(getArquivoExcluirVO()) + 1);
						}
						ArquivoVO arquivoVO2 = arquivoAgrupadorTurmaDisciplina.getArquivoFilhoVOs().get(arquivoAgrupadorTurmaDisciplina.getArquivoFilhoVOs().indexOf(getArquivoExcluirVO()) + 1);
						getFacadeFactory().getArquivoFacade().alterarOrdemArquivo(arquivoAgrupadorTurmaDisciplina.getArquivoFilhoVOs(), getArquivoExcluirVO(), arquivoVO2, getUsuarioLogado());
					}
				}
				arquivoAgrupadorTurmaDisciplina.getArquivoFilhoVOs().remove(getArquivoExcluirVO());
				if (arquivoAgrupadorTurmaDisciplina.getArquivoFilhoVOs().isEmpty()) {
					getMapArquivoAgrupamentoDisciplinaTurmaProfessor().remove(arquivoAgrupadorTurmaDisciplina.getKeyAgrupadorTurmaDisciplinaProfessor());
					getArquivoVOs().clear();
					getArquivoVOs().addAll(getMapArquivoAgrupamentoDisciplinaTurmaProfessor().values());
				}
			}

			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			setArquivoExcluirVO(null);
			setArquivoFilhoExcluirVO(null);
		}
	}

	/**
	 * Utilizado para manter o agrupador do arquivo filho quando ele for editado,
	 * para caso seja alterado o agrupador ele gere um novo agrupador.
	 * 
	 * @author Wellington Rodrigues - 17 de ago de 2015
	 * @return
	 */
	public String getAgrupadorTemp() {
		if (agrupadorTemp == null) {
			agrupadorTemp = "";
		}
		return agrupadorTemp;
	}

	public void setAgrupadorTemp(String agrupadorTemp) {
		this.agrupadorTemp = agrupadorTemp;
	}

	public void executarVerificacaoApresentarComboboxPeriodoVisaoAluno() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirAlunoVizualizarMateriaisPeriodoConcluido", getUsuarioLogado());
			setApresentarComboboxPeriodoVisaoAluno(true);
		} catch (Exception e) {
			setApresentarComboboxPeriodoVisaoAluno(false);
		}
		if (PeriodicidadeEnum.INTEGRAL.getValor().equals(getMatriculaVO().getCurso().getPeriodicidade())) {
			montarListaDisciplinaTurmaVisaoAluno();
		} else {
			montarListaSelectItemAnoSemestre();
		}
	}

	/**
	 * @return the mapArquivoAgrupamentoDisciplinaTurmaProfessor
	 */
	public Map<String, ArquivoVO> getMapArquivoAgrupamentoDisciplinaTurmaProfessor() {
		if (mapArquivoAgrupamentoDisciplinaTurmaProfessor == null) {
			mapArquivoAgrupamentoDisciplinaTurmaProfessor = new HashMap<String, ArquivoVO>(0);
		}
		return mapArquivoAgrupamentoDisciplinaTurmaProfessor;
	}

	/**
	 * @param mapArquivoAgrupamentoDisciplinaTurmaProfessor
	 *            the mapArquivoAgrupamentoDisciplinaTurmaProfessor to set
	 */
	public void setMapArquivoAgrupamentoDisciplinaTurmaProfessor(Map<String, ArquivoVO> mapArquivoAgrupamentoDisciplinaTurmaProfessor) {
		this.mapArquivoAgrupamentoDisciplinaTurmaProfessor = mapArquivoAgrupamentoDisciplinaTurmaProfessor;
	}

	/**
	 * @return the arquivoExcluirVO
	 */
	public ArquivoVO getArquivoExcluirVO() {
		if (arquivoExcluirVO == null) {
			arquivoExcluirVO = new ArquivoVO();
		}
		return arquivoExcluirVO;
	}

	/**
	 * @param arquivoExcluirVO
	 *            the arquivoExcluirVO to set
	 */
	public void setArquivoExcluirVO(ArquivoVO arquivoExcluirVO) {
		this.arquivoExcluirVO = arquivoExcluirVO;
	}

	/**
	 * @return the arquivoFilhoExcluirVO
	 */
	public ArquivoVO getArquivoFilhoExcluirVO() {
		if (arquivoFilhoExcluirVO == null) {
			arquivoFilhoExcluirVO = new ArquivoVO();
		}
		return arquivoFilhoExcluirVO;
	}

	/**
	 * @param arquivoFilhoExcluirVO
	 *            the arquivoFilhoExcluirVO to set
	 */
	public void setArquivoFilhoExcluirVO(ArquivoVO arquivoFilhoExcluirVO) {
		this.arquivoFilhoExcluirVO = arquivoFilhoExcluirVO;
	}

	/**
	 * @return the excluindoArquivoFilho
	 */
	public Boolean getExcluindoArquivoFilho() {
		if (excluindoArquivoFilho == null) {
			excluindoArquivoFilho = false;
		}
		return excluindoArquivoFilho;
	}

	/**
	 * @param excluindoArquivoFilho
	 *            the excluindoArquivoFilho to set
	 */
	public void setExcluindoArquivoFilho(Boolean excluindoArquivoFilho) {
		this.excluindoArquivoFilho = excluindoArquivoFilho;
	}

	/**
	 * @return the professorVO
	 */
	public PessoaVO getProfessorVO() {
		if (professorVO == null) {
			professorVO = new PessoaVO();
		}
		return professorVO;
	}

	/**
	 * @param professorVO
	 *            the professorVO to set
	 */
	public void setProfessorVO(PessoaVO professorVO) {
		this.professorVO = professorVO;
	}

	public boolean isApresentarComboboxPeriodoVisaoAluno() {
		return apresentarComboboxPeriodoVisaoAluno;
	}

	public void setApresentarComboboxPeriodoVisaoAluno(boolean apresentarComboboxPeriodoVisaoAluno) {
		this.apresentarComboboxPeriodoVisaoAluno = apresentarComboboxPeriodoVisaoAluno;
	}

	private void montarListaSelectItemAnoSemestre() throws Exception {
		setListaSelectItemAnoSemestre(new ArrayList<SelectItem>(0));
		setAnoSemestre(getFacadeFactory().getHistoricoFacade().inicializarDadosAnoSemestreHistoricoPriorizandoAtivoConcluido(getMatriculaVO().getMatricula(), getListaSelectItemAnoSemestre()));
		if(getVisaoAlunoControle() != null && Uteis.isAtributoPreenchido(getVisaoAlunoControle().getFiltroAnoSemestreTelaInicial())) {
			setAnoSemestre(getVisaoAlunoControle().getFiltroAnoSemestreTelaInicial());
		}
		if (Uteis.isAtributoPreenchido(getListaSelectItemAnoSemestre())) {
			montarListaDisciplinaTurmaVisaoAluno();
		}
	}

	public List<SelectItem> getListaSelectItemAnoSemestre() {
		if (listaSelectItemAnoSemestre == null) {
			listaSelectItemAnoSemestre = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemAnoSemestre;
	}

	public void setListaSelectItemAnoSemestre(List<SelectItem> listaSelectItemAnoSemestre) {
		this.listaSelectItemAnoSemestre = listaSelectItemAnoSemestre;
	}

	public String getAnoSemestre() {
		if (anoSemestre == null) {
			anoSemestre = "";
		}
		return anoSemestre;
	}

	public void setAnoSemestre(String anoSemestre) {
		this.anoSemestre = anoSemestre;
	}

	public String aplicarRegraNomeCursoApresentarCombobox(TurmaVO turmaVO) {
		String nomeApresentar = null;
		if (turmaVO.getTurmaAgrupada()) {
			nomeApresentar = "";
		} else {
			nomeApresentar = turmaVO.getCurso().getNome();
		}
		if (!nomeApresentar.equals("")) {
			nomeApresentar += " - ";
		}
		return nomeApresentar;
	}

	public String getCaminhoServidorDownloadBackup() {
		try {
			ArquivoVO obj = (ArquivoVO) context().getExternalContext().getRequestMap().get("arquivoItens");
			context().getExternalContext().getSessionMap().put("arquivoVO", obj);

		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
		return "";
	}

	public void verificarQuantidadeDownloadsNaoRealizadosDownloadVisaoAluno() {
		setQtdeDownloadsMaterialNaoRealizados(0);
		List<ArquivoVO> totalArquivoFilhoVO = new ArrayList<ArquivoVO>();
		try {
			totalArquivoFilhoVO.addAll(getFacadeFactory().getArquivoFacade().consultarMateriaisPendentesAluno(getMatriculaVO().getMatricula(), Uteis.getAnoDataAtual(), Uteis.getSemestreAtual(), "PR", getMatriculaVO().getCurso().getPeriodicidade(), null));
			setQtdeDownloadsMaterialNaoRealizados(totalArquivoFilhoVO.size());
			getVisaoAlunoControle().setQtdeDownloadsMaterialNaoRealizados(totalArquivoFilhoVO.size());
		} catch (Exception e) {

		} finally {
			Uteis.liberarListaMemoria(totalArquivoFilhoVO);
		}
	}

	public Integer getQtdeDownloadsMaterialNaoRealizados() {
		if (qtdeDownloadsMaterialNaoRealizados == null) {
			qtdeDownloadsMaterialNaoRealizados = 0;
		}
		return qtdeDownloadsMaterialNaoRealizados;
	}

	public void setQtdeDownloadsMaterialNaoRealizados(Integer qtdeDownloadsMaterialNaoRealizados) {
		this.qtdeDownloadsMaterialNaoRealizados = qtdeDownloadsMaterialNaoRealizados;
	}
	
	/**
	 * Ao mexer na estrutura do return validar das telas de uploadArquivos, uploadArquivosProfessor e uploadArquivosCoordenador pois e utilizados nos dois modal Pedro Andrade
	 * @return
	 */
	public String getExibirQtdeCaractereLimiteDownloadMaterial() {
		return getQtdeCaractereLimiteDownloadMaterial() > 0 ? "contar('formUploadArquivo:descricaoCont','formUploadArquivo:contador','"+getQtdeCaractereLimiteDownloadMaterial()+"');": "";
	}
	
	public Integer getQtdeCaractereLimiteDownloadMaterial() {
		if (qtdeCaractereLimiteDownloadMaterial == null) {
			qtdeCaractereLimiteDownloadMaterial = 0;
		}
		return qtdeCaractereLimiteDownloadMaterial;
	}

	public void setQtdeCaractereLimiteDownloadMaterial(Integer qtdeCaractereLimiteDownloadMaterial) {
		this.qtdeCaractereLimiteDownloadMaterial = qtdeCaractereLimiteDownloadMaterial;
	}

	public void atualizarListaDeDadosPorBuscaAnteriores() {
		try {
			montarListaSelectItemTurmaProfessor();
			montarListaSelectItemDisciplinasProfessor();
		} catch (Exception ex) {
			setMensagemDetalhada("msg_erro", ex.getMessage());
		}
	}

	public void marcarDesmacarCampoPeriodoDisponibilizacao() {
		if(!getArquivoVO().getManterDisponibilizacao()) {
			getArquivoVO().setApresentarDeterminadoPeriodo(false);
			limparCampoDataIndisponibilizacao();
		}
	}
	
	public void validarPermissao()  throws Exception {		
		
		List<PermissaoVO> listaPerfilMaterial;
		
				
			listaPerfilMaterial = (getFacadeFactory().getPermissaoFacade().validarPermissaoMaterial(getUsuarioLogado().getPerfilAcesso().getCodigo()));
			
			for(PermissaoVO nomeEntidade : listaPerfilMaterial) {
				
				Arquivo.validarDadosMaterial(arquivoVO , nomeEntidade.getNomeEntidade());
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
	
	public List getListaSelectSemestre() {
		if (listaSelectSemestre == null) {
			listaSelectSemestre = new ArrayList(0);
			listaSelectSemestre.add(new SelectItem("", " "));
			listaSelectSemestre.add(new SelectItem("1", "1º"));
			listaSelectSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectSemestre;
	}	
	
	public List<SelectItem> tipoConsultaComboSituacao;

	public List<SelectItem> getTipoConsultaComboSituacao() {
		if (tipoConsultaComboSituacao == null) {
			tipoConsultaComboSituacao = new ArrayList<SelectItem>(0);
			tipoConsultaComboSituacao.add(new SelectItem("", "Todos"));
			tipoConsultaComboSituacao.add(new SelectItem("ativo", "Ativo"));
			tipoConsultaComboSituacao.add(new SelectItem("inativo", "Inativo"));
			
		}
		return tipoConsultaComboSituacao;
	}

	public String getCampoConsultaSituacao() {
		if(campoConsultaSituacao == null ) {
			campoConsultaSituacao = "";
		}
		return campoConsultaSituacao;
	}

	public void setCampoConsultaSituacao(String campoConsultaSituacao) {
		this.campoConsultaSituacao = campoConsultaSituacao;
	}
	
	public String getAnoPeriodoUpload() {
		return anoPeriodoUpload;
	}

	public void setAnoPeriodoUpload(String anoPeriodoUpload) {
		this.anoPeriodoUpload = anoPeriodoUpload;
	}

	public String getSemestrePeriodoUpload() {
		return semestrePeriodoUpload;
	}

	public void setSemestrePeriodoUpload(String semestrePeriodoUpload) {
		this.semestrePeriodoUpload = semestrePeriodoUpload;
	}

	public String getAnoPeriodoDisponibilizacao() {
		return anoPeriodoDisponibilizacao;
	}

	public void setAnoPeriodoDisponibilizacao(String anoPeriodoDisponibilizacao) {
		this.anoPeriodoDisponibilizacao = anoPeriodoDisponibilizacao;
	}

	public String getSemestrePeriodoDisponibilizacao() {
		return semestrePeriodoDisponibilizacao;
	}

	public void setSemestrePeriodoDisponibilizacao(String semestrePeriodoDisponibilizacao) {
		this.semestrePeriodoDisponibilizacao = semestrePeriodoDisponibilizacao;
	}
	@PostConstruct
	private void executarPreechimentoCamposUltimaConsultaRealizada() {
		if(!getUsuarioLogado().getIsApresentarVisaoAluno() && !getUsuarioLogado().getIsApresentarVisaoPais() && !getUsuarioLogado().getIsApresentarVisaoProfessor()) {
		try {		
			Map<String, String> mapResultado = getFacadeFactory().getLayoutPadraoFacade().consultarValoresPadroes(new String[] {"ano", "semestre", "anoPeriodoUpload", "semestrePeriodoUpload", "anoPeriodoDisponibilizacao", "semestrePeriodoDisponibilizacao", "campoConsultaSituacao", "turma", "disciplina","professorAdmCordenador"}, ArquivoControle.class.getSimpleName());
			if(mapResultado.containsKey("ano")) {
				setAno(mapResultado.get("ano"));
			}
			if(mapResultado.containsKey("semestre")) {
				setSemestre(mapResultado.get("semestre"));
			}
			if(mapResultado.containsKey("anoPeriodoUpload")) {
				setAnoPeriodoUpload(mapResultado.get("anoPeriodoUpload"));
			}
			if(mapResultado.containsKey("semestrePeriodoUpload")) {
				setSemestrePeriodoUpload(mapResultado.get("semestrePeriodoUpload"));
			}
			if(mapResultado.containsKey("anoPeriodoDisponibilizacao")) {
				setAnoPeriodoDisponibilizacao(mapResultado.get("anoPeriodoDisponibilizacao"));
			}
			if(mapResultado.containsKey("semestrePeriodoDisponibilizacao")) {
				setSemestrePeriodoDisponibilizacao(mapResultado.get("semestrePeriodoDisponibilizacao"));
			}
			if(mapResultado.containsKey("campoConsultaSituacao")) {
				setCampoConsultaSituacao(mapResultado.get("campoConsultaSituacao"));
			}
//			if (mapResultado.containsKey("turma") && Uteis.isAtributoPreenchido(Integer.parseInt(mapResultado.get("turma")))) {
//				setTurmaVO(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(Integer.parseInt(mapResultado.get("turma")),  Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
//				this.getArquivoVO().setTurma(getTurmaVO());
//			}
//			if (mapResultado.containsKey("disciplina") && Uteis.isAtributoPreenchido(Integer.parseInt(mapResultado.get("disciplina")))) {
//				setDisciplinaVO(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(Integer.parseInt(mapResultado.get("disciplina")),  Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
//				this.getArquivoVO().setDisciplina(getDisciplinaVO());
//			}

//			if (mapResultado.containsKey("professorAdmCordenador") && Uteis.isAtributoPreenchido(Integer.parseInt(mapResultado.get("professorAdmCordenador")))) {
//				setProfessorVO(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(Integer.parseInt(mapResultado.get("professorAdmCordenador")), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
//				getArquivoVO().setProfessor(getProfessorVO());
//			}
		}catch (Exception e) {

		}	
		}
	}
	public void persistirDadosPadroesUltimaConsultaRealizada() {
		try {
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getAnoPeriodoUpload(),ArquivoControle.class.getSimpleName(), "anoPeriodoUpload", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSemestrePeriodoUpload(),ArquivoControle.class.getSimpleName(), "semestrePeriodoUpload", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getAnoPeriodoDisponibilizacao(),ArquivoControle.class.getSimpleName(), "anoPeriodoDisponibilizacao", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSemestrePeriodoDisponibilizacao(),ArquivoControle.class.getSimpleName(), "semestrePeriodoDisponibilizacao", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getAno(),ArquivoControle.class.getSimpleName(), "ano", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getSemestre(),ArquivoControle.class.getSimpleName(), "semestre", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getCampoConsultaSituacao(),ArquivoControle.class.getSimpleName(), "campoConsultaSituacao", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getTurmaVO().getCodigo().toString(),ArquivoControle.class.getSimpleName(), "turma", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getDisciplinaVO().getCodigo().toString(),ArquivoControle.class.getSimpleName(), "disciplina", getUsuarioLogado());
			getFacadeFactory().getLayoutPadraoFacade().persistirLayoutPadrao2(getProfessorVO().getCodigo().toString(),ArquivoControle.class.getSimpleName(), "professorAdmCordenador", getUsuarioLogado());
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getPostarMaterialComTurmaObrigatoriamenteInformado() {
		if(postarMaterialComTurmaObrigatoriamenteInformado == null) {
			postarMaterialComTurmaObrigatoriamenteInformado = Boolean.FALSE;
		}
		return postarMaterialComTurmaObrigatoriamenteInformado;
	}

	public void setPostarMaterialComTurmaObrigatoriamenteInformado(
			Boolean postarMaterialComTurmaObrigatoriamenteInformado) {
		this.postarMaterialComTurmaObrigatoriamenteInformado = postarMaterialComTurmaObrigatoriamenteInformado;
	}
	
	public void verificarPostarMaterialComTurmaObrigatoriamenteInformado() {
		Boolean postarMaterialComTurmaObrigatoriamenteInformado;
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidade("PostarMaterialComTurmaObrigatoriamenteInformado", getUsuarioLogado());
			postarMaterialComTurmaObrigatoriamenteInformado = true;
		} catch (Exception e) {
			postarMaterialComTurmaObrigatoriamenteInformado = false;
		}
		setPostarMaterialComTurmaObrigatoriamenteInformado(postarMaterialComTurmaObrigatoriamenteInformado);
	}

	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		return configuracaoGeralSistemaVO;
	}

	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}
	
	public void renderizarUpload() {
		setExibirUpload(false);
	}
	
private String apresentarNomeArquivo;
	
	public String getApresentarNomeArquivo() {
		if (apresentarNomeArquivo == null) {
			apresentarNomeArquivo = "";
		}
		return apresentarNomeArquivo;
	}
	
	public void setApresentarNomeArquivo(String apresentarNomeArquivo) {
		this.apresentarNomeArquivo = apresentarNomeArquivo;
	}
	
}
