package controle.academico;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemChave;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CalendarioRegistroAulaVO;
import negocio.comuns.academico.ConfiguracaoAcademicoVO;
import negocio.comuns.academico.ConteudoPlanejamentoVO;
import negocio.comuns.academico.DisciplinaAbonoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.FrequenciaAulaVO;
import negocio.comuns.academico.HorarioProfessorDiaItemVO;
import negocio.comuns.academico.HorarioProfessorDiaVO;
import negocio.comuns.academico.HorarioProfessorVO;
import negocio.comuns.academico.LayoutPadraoVO;
import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoEnsinoVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.academico.QuadroHorarioVO;
import negocio.comuns.academico.RegistroAulaVO;
import negocio.comuns.academico.TurmaDisciplinaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.arquitetura.UsuarioPerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.enumeradores.ConsiderarFeriadoEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.academico.RegistroAula;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.academico.DiarioRegistroAulaVO;
import relatorio.negocio.jdbc.academico.DiarioRel;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das
 * páginas registroAulaForm.jsp registroAulaCons.jsp) com as funcionalidades da
 * classe <code>RegistroAula</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see RegistroAula
 * @see RegistroAulaVO
 */
@Lazy
@Scope("viewScope")
@Controller("RegistroAulaControle")
public class RegistroAulaControle extends SuperControleRelatorio implements Serializable {

	private RegistroAulaVO registroAulaVO;
	private String programacaoAula_Erro;
	private String responsavelRegistroAula_Erro;
	private String turma_Erro;
	private String disciplina_Erro;
	private String diaSemana_Erro;
	private List<SelectItem> listaSelectItemDisciplinasProgramacaoAula;
	private List<SelectItem> listaSelectItemProfessor;
	private List<SelectItem> listaSelectItemProfessores;
	private List<SelectItem> listaSelectItemTurma;
	private List listaProfessor;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaTurma;
	private FrequenciaAulaVO frequenciaAulaVO;
	private String matricula_Erro;
	private String turma_cursoTurno;
	private boolean possuiPermissaoAlterarCargaHoraria;
	private String campoConsultaProfessor;
	private String valorConsultaProfessor;
	private List listaConsultaProfessor;
	/* Novo registro de aula com calendario */
	private QuadroHorarioVO quadroHorario;
	private Integer index;
	protected HorarioProfessorDiaVO horarioProfessorDiaVO;
	private Integer codigoTurno;
	private Boolean isMostrarListagemDeDisciplinas;
	private List<RegistroAulaVO> listaRegistrosAula;
	// Dados para Registro de Aula
	private TurmaVO turma;
	private DisciplinaVO disciplina;
	private String ano;
	private String semestre;
	private Integer cargaHoraria;
	private String tipoAula;
	private UsuarioVO responsavelRegistroAula;
	private PessoaVO professor;
	private String conteudo;
	private List<String> listaHorario;
	private Boolean isMostrarCalendario;
	private Boolean isMostrarBotaoExcluir;
	public static final long serialVersionUID = 1L;
	private Boolean permiteGravarVisaoCoordenador;
	private Boolean controlarMarcarDesmarcarTodos;
	private CalendarioRegistroAulaVO calendarioRegistroAula;
	private ConfiguracaoAcademicoVO configuracaoAcademicoVO;
	private String apresentarModalPermitirExcluirRegistroAulaOrfao;
	private List<RegistroAulaVO> registroAulaOrfaoVOs;
	private Boolean apresentarDataMatricula;
	private Boolean trazerAlunosTransferenciaMatriz;
	private PlanoEnsinoVO planoEnsinoVO;	
	private Boolean bloquearLancamentosNotasAulasFeriadosFinaisSemana;
	private Boolean enviarSms;
	private Boolean enviarEmail;
	private ComunicacaoInternaVO comunicadoInternaVO;
	private List<MatriculaPeriodoVO> matriculaPeriodoVOs;
	private String praticaSupervisionada;
	
	public String getTituloScrollFirst() {
		if (getIndex() - 1 < 1) {
			return "";
		}
		if (getQuadroHorario().getHorarioProfessorVO().getCalendarioHorarioAulaVOs().size() >= getIndex()) {
			return getQuadroHorario().getHorarioProfessorVO().getCalendarioHorarioAulaVOs().get(getIndex() - 2).getTituloCalendarioAbreviado();
		}
		return "";
	}

	public String getTituloScroll() {
		if (getIndex() - 1 < 0) {
			setIndex(1);
		}
		if (getQuadroHorario().getHorarioProfessorVO().getCalendarioHorarioAulaVOs().size() >= getIndex()) {
			return getQuadroHorario().getHorarioProfessorVO().getCalendarioHorarioAulaVOs().get(getIndex() - 1).getTituloCalendarioAbreviado();
		}
		return "";
	}

	public String getTituloScrollNext() {
		if (getIndex() == getQuadroHorario().getHorarioProfessorVO().getCalendarioHorarioAulaVOs().size()) {
			return "";
		}
		if (getQuadroHorario().getHorarioProfessorVO().getCalendarioHorarioAulaVOs().size() >= getIndex()) {
			return getQuadroHorario().getHorarioProfessorVO().getCalendarioHorarioAulaVOs().get(getIndex()).getTituloCalendarioAbreviado();
		}
		return "";
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

	public RegistroAulaControle() throws Exception {
		// obterUsuarioLogado();
		// verificarPermissaoParaGravarRegistroAulaVisaoCoordenador();
		
		// validarPermissaoAlterarCargaHoraria()
		// if (getUsuarioLogado().getVisaoLogar().equals("professor")) {
		// realizarVerificarVisuaizarBotaoExcluir();
		// }
		setMensagemID("msg_entre_prmconsulta");		
	}

	public void verificarPermissaoParaGravarRegistroAulaVisaoCoordenador() {
		try {
			if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
				ControleAcesso.incluir("RegistroAula", getUsuarioLogado());
				setPermiteGravarVisaoCoordenador(true);
			}
		} catch (Exception e) {
			setPermiteGravarVisaoCoordenador(false);
		}
	}

	// public void validarPermissaoAlterarCargaHoraria() {
	// try {
	// RegistroAula.verificaPermissaoAlterarCargaHoraria();
	// setPossuiPermissaoAlterarCargaHoraria(true);
	// } catch (Exception e) {
	// setPossuiPermissaoAlterarCargaHoraria(false);
	// }
	// }
	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>RegistroAula</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {		
		setMensagemDetalhada("");
		// setDiaSemana_Erro("");
		// setProfessor(new PessoaVO());
		// setAno("");
		// setSemestre("");
		// setRegistroAulaVO(new RegistroAulaVO());
		// setTurma(new TurmaVO());
		// setDisciplina(new DisciplinaVO());
		// setConteudo("");
		// setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
		// setListaSelectItemProfessores(new ArrayList<SelectItem>(0));
		// // setListaSelectItemProfessor(new ArrayList<SelectItem>(0));
		// setListaSelectItemTurma(new ArrayList<SelectItem>(0));
		// setListaSelectItemDisciplinasProgramacaoAula(new
		// ArrayList<SelectItem>(0));
		// setQuadroHorario(new QuadroHorarioVO());
		// setHorarioProfessorDiaVO(new HorarioProfessorDiaVO());
		// setFrequenciaAulaVO(new FrequenciaAulaVO());
		// setIsMostrarCalendario(false);
		// setIsMostrarListagemDeDisciplinas(false);
		// setListaProfessor(new ArrayList(0));

		
		inicializarListasSelectItemTodosComboBox();
		setIsMostrarBotaoExcluir(false);
		setMensagemID("msg_entre_dados");
		if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroAulaForm.xhtml");
		}
		return "";
	}

	public String novoVisaoProfessor() throws Exception {
		try {
			novo();
			setTurma(null);
			setCalendarioRegistroAula(null);
			getFacadeFactory().getRegistroAulaFacade().validarConsultaDoUsuario(getUsuarioLogadoClone());			setIsMostrarCalendario(false);
			setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
			setProfessor(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(getUsuarioLogado().getPessoa().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			montarListaSelectItemTurma();
			VisaoProfessorControle visaoProfessor = (VisaoProfessorControle) context().getExternalContext().getSessionMap().get("VisaoProfessorControle");
			if (visaoProfessor != null) {
				visaoProfessor.inicializarMenuRegistroAula();
			}
			verificarBloquearLancamentosNotasAulasFeriadosFinaisSemana();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("registrarAulaProfessor.xhtml");
	}

	public String novoVisaoCoordenador() throws Exception {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RegistroAulaControle", "Novo Visão Coordenador", "Novo");
			novo();
			getFacadeFactory().getRegistroAulaFacade().validarConsultaDoUsuario(getUsuarioLogadoClone());
			montarListaSelectItemTurmaCoordenador();
			montarListaSelectItemProfessoresTurmaCoordenador();
//			VisaoCoordenadorControle visaoCoordenador = (VisaoCoordenadorControle) context().getExternalContext().getSessionMap().get("VisaoCoordenadorControle");
//			if (visaoCoordenador != null) {
//				visaoCoordenador.inicializarMenuRegistroAula();
//			}
			verificarBloquearLancamentosNotasAulasFeriadosFinaisSemana();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("registrarAulaCoordenador.xhtml");
	}

	public void montarListaSelectItemTurmaCoordenador() {
		try {
			setTurma(null);
			setDisciplina(null);
			getListaSelectItemDisciplinasProgramacaoAula().clear();
			getListaSelectItemProfessores().clear();
			setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
			setConteudo("");
			setIsMostrarCalendario(false);
			setQuadroHorario(new QuadroHorarioVO());
			setProfessor(new PessoaVO());
			setIsMostrarListagemDeDisciplinas(false);
			List<TurmaVO> resultadoConsulta = null;
			Iterator<TurmaVO> i = null;
			resultadoConsulta = consultarTurmaCoordenador();
			i = resultadoConsulta.iterator();
			getListaSelectItemTurma().clear();
			getListaSelectItemTurma().add(new SelectItem(0, ""));
			while (i.hasNext()) {
				TurmaVO obj = (TurmaVO) i.next();
				getListaSelectItemTurma().add(new SelectItem(obj.getCodigo(), obj.getIdentificadorTurma().toString()));
				removerObjetoMemoria(obj);
			}
			resultadoConsulta.clear();
			resultadoConsulta = null;
			i = null;					
		} catch (Exception e) {
			System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	public List<TurmaVO> consultarTurmaCoordenador() throws Exception {
		// return
		// getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenador(getUsuarioLogado().getPessoa().getCodigo(),
		// false, true, true, getUnidadeEnsinoLogado().getCodigo(), false,
		// getUsuarioLogado());
		return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenadorAnoSemestre(getUsuarioLogado().getPessoa().getCodigo(), false, false, true, false, getAno(), getSemestre(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
	}

	public void montarListaSelectItemProfessoresTurmaCoordenador() {
		try {
			if (getTurma().getCodigo() != 0) {
				getTurma().setNivelMontarDados(NivelMontarDados.NAO_INICIALIZADO);
//				getFacadeFactory().getTurmaFacade().carregarDados(getTurma(), NivelMontarDados.BASICO, getUsuarioLogado());
				setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				if (getTurma().getCurso().getNivelEducacionalPosGraduacao()) {
					setAno("");
					setSemestre("");
				}
			}
			getProfessor().setCodigo(0);
			getListaSelectItemDisciplinasProgramacaoAula().clear();
			setIsMostrarCalendario(false);
			montarListaSelectItemProfessoresTurmaCoordenador("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public void montarListaSelectItemProfessoresTurmaCoordenador(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			getListaSelectItemProfessores().clear();
			if (getTurma().getCodigo() != null && !getTurma().getCodigo().equals(0)) {
				resultadoConsulta = consultarProfessoresTurmaCoordenador();
				i = resultadoConsulta.iterator();
				getListaSelectItemProfessores().clear();
				getListaSelectItemProfessores().add(new SelectItem(0, ""));
				while (i.hasNext()) {
					PessoaVO obj = (PessoaVO) i.next();
					getListaSelectItemProfessores().add(new SelectItem(obj.getCodigo(), obj.getNome()));
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

	public List consultarProfessoresTurmaCoordenador() throws Exception {
		return getFacadeFactory().getPessoaFacade().consultarProfessoresDaTurmaPorTurma(getTurma().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), getSemestre(), getAno(), false, getUsuarioLogado());
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>RegistroAula</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		RegistroAulaVO obj = (RegistroAulaVO) context().getExternalContext().getRequestMap().get("registroAulaItens");
		getFacadeFactory().getRegistroAulaFacade().carregarDados(obj, getUsuarioLogado());
		setTurma_Erro("");
		setRegistroAulaVO(obj);
		obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado()));
		setTurma(obj.getTurma());
		setDisciplina(obj.getDisciplina());
		setHorarioProfessorDiaVO(getFacadeFactory().getHorarioProfessorDiaFacade().consultarPorDiaProfessorTurno(obj.getData(), getProfessor().getCodigo(), obj.getTurma().getTurno().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
		setConteudo(obj.getConteudo());
		setTipoAula(obj.getTipoAula());
		for (HorarioProfessorDiaItemVO aula : getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs()) {
			if (aula.getDisciplinaVO().getCodigo().intValue() == disciplina.getCodigo().intValue()) {
				obj.setHorario(aula.getHorario());
			}
		}
		getListaRegistrosAula().add(obj);
		inicializarListasSelectItemTodosComboBox();
		// getListaSelectItemProfessor().add(new
		// SelectItem(obj.getProfessor().getCodigo(),
		// obj.getProfessor().getNome()));
		obj.setNovoObj(Boolean.FALSE);
		setFrequenciaAulaVO(new FrequenciaAulaVO());
		VisaoProfessorControle visaoProfessor = (VisaoProfessorControle) context().getExternalContext().getSessionMap().get("VisaoProfessorControle");
		if (visaoProfessor != null) {
			visaoProfessor.inicializarMenuRegistroAula();
			montarListaDisciplinaTurmaVisaoProfessor();
			montarListaSelectItemTurma();
		}
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroAulaForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>RegistroAula</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public void gravar() {
		try {
			persistir("RegistroAula", "Alteração pela Visão Administrativa");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void gravarVisaoProfessor() {

		ComunicacaoInternaVO comunicacaoInternaVO = null;
		try {
			persistir("RegistroAula", "Inserção pela Visão do Professor");
			if (!getListaRegistrosAula().isEmpty()) {
				
				if (getTurma().getGrupoDestinatarios().getCodigo() != 0) {
					List<ProfessorTitularDisciplinaTurmaVO> listaProfessores = new ArrayList<>(0);
					comunicacaoInternaVO = inicializarDadosPadrao(new ComunicacaoInternaVO());
					LayoutPadraoVO layoutPadraoVO = getFacadeFactory().getLayoutPadraoFacade().consultarPorEntidadeCampo("diario", "designDiario", false, getUsuarioLogado());
					String tipoLayout = "";
					if (!layoutPadraoVO.getValor().equals("")) {
						tipoLayout = layoutPadraoVO.getValor();
					}
					ProfessorTitularDisciplinaTurmaVO p = new ProfessorTitularDisciplinaTurmaVO();
					p = getFacadeFactory().getDiarioRelFacade().consultarProfessorTitularTurma(getTurma(), getDisciplina().getCodigo(), getAno(), getSemestre(), true, getUsuarioLogado());
					listaProfessores.add(p);
					
					List<DiarioRegistroAulaVO> listaObjetos = new ArrayList<>(0);
					listaObjetos = getFacadeFactory().getDiarioRelFacade().consultarRegistroAula(listaProfessores, getDisciplina().getCodigo(), getTurma(), getSemestre(), getAno(), getUsuarioLogado(), null, false, true, "", tipoLayout, "", "", "", "", 0, false, false, false, getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados(), "", new Date(), new Date(), new ArrayList<String>(0));

					String nomeRelatorio = "";
					if (!listaObjetos.isEmpty()) {
						nomeRelatorio += "-";
						DiarioRegistroAulaVO reg = (DiarioRegistroAulaVO) listaObjetos.get(0);
						nomeRelatorio += Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(reg.getDisciplina().getNome());
						nomeRelatorio += "-";
						nomeRelatorio += Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(reg.getTurma().getIdentificadorTurma());
						nomeRelatorio += "-";
					}
					File file = UteisJSF.realizarGeracaoArquivoPDF(getUsuarioLogado(), new Long(0), listaObjetos, "Diário", DiarioRel.getIdEntidade() + nomeRelatorio, DiarioRel.getDesignIReportRelatorio(tipoLayout), DiarioRel.getCaminhoBaseRelatorio(), null);
					
					PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getRegistroAulaFacade().carregarDadosMensagemPersonalizada(getUsuarioLogadoClone(), getTurma().getUnidadeEnsino().getCodigo());
					if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
						if (file != null) {
							ArquivoVO arquivoVO = new ArquivoVO();
							arquivoVO.setExtensao("pdf");
							arquivoVO.setPastaBaseArquivo(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator +PastaBaseArquivoEnum.ANEXO_EMAIL.getValue());
							arquivoVO.setPastaBaseArquivoEnum(PastaBaseArquivoEnum.ANEXO_EMAIL);
							arquivoVO.setNome(file.getName());
							arquivoVO.setDescricao(file.getName().substring(0, file.getName().lastIndexOf(".")));
							arquivoVO.setManterDisponibilizacao(true);
							arquivoVO.setSituacao("AT");
							arquivoVO.setControlarDownload(false);
							FileUtils.copyFileToDirectory(file, new File(getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ANEXO_EMAIL.getValue()), true);
							comunicacaoInternaVO.setArquivoAnexo(arquivoVO);
						}
						String dataRegistro = Uteis.getData(new Date(), "dd/MM/yyyy"); 
						comunicacaoInternaVO.setAssunto(mensagemTemplate.getAssunto());
						comunicacaoInternaVO.setMensagem(mensagemTemplate.getMensagem());
						setEnviarEmail(Boolean.TRUE);
						if (!mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
							comunicacaoInternaVO.setMensagemSMS(mensagemTemplate.getMensagemSMS());
							setEnviarSms(Boolean.TRUE);
						} 
						executarValidacaoSimulacaoVisaoProfessor();
						getFacadeFactory().getRegistroAulaFacade().executarEnvioComunicadoInternoRegistroAula(comunicacaoInternaVO, dataRegistro, getConteudo(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(), getEnviarSms(), getEnviarEmail());
					} 
					
					p = null;
					listaProfessores = null;
					layoutPadraoVO = null;
					tipoLayout = null;
					listaObjetos = null;
				}
				// setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
				// setTipoAula("P");
				// setConteudo("");

			}
			setMensagemDetalhada("");
			setDiaSemana_Erro("");
			setTurma_cursoTurno("");
			setTurma_Erro("");
			setIsMostrarBotaoExcluir(Boolean.TRUE);
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void persistir(String idEntidade, String operacao) throws Exception {
		setOncompleteModal("");
		executarValidacaoSimulacaoVisaoProfessor();
		alterarPraticaSupervisionadaListaRegistroAula(getListaRegistrosAula(), getPraticaSupervisionada());
		getFacadeFactory().getRegistroAulaFacade().persistir(getListaRegistrosAula(), getConteudo(), getTipoAula(), getPermiteLancamentoAulaFutura(), idEntidade, operacao, getUsuarioLogado(), true);
		if (!getListaRegistrosAula().isEmpty()) {
			realizarMarcarDiaLancado(getListaRegistrosAula().get(0).getData());
		}
		// setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
		// setTipoAula("P");
		// setConteudo("");
		setIsMostrarCalendario(true);
		setIsMostrarBotaoExcluir(Boolean.TRUE);
		setMensagemDetalhada("");
		setDiaSemana_Erro("");
		setTurma_cursoTurno("");
		setTurma_Erro("");
		setMatriculaPeriodoVOs(getFacadeFactory().getMatriculaPeriodoFacade().consultarMatriculaPeriodoPrecisaReprovarPeriodoLetivoPorRegistroAula(getListaRegistrosAula(), getAno(), getSemestre(), getUsuarioLogado()));
		if(!getMatriculaPeriodoVOs().isEmpty()){
			setOncompleteModal("RichFaces.$('panelReprovarPeriodoLetivo').show();");
		}
		setMensagemID("msg_dados_gravados");

	}

	public String gerarMensagemRedefinirSenha(String conteudoAula) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("\r <br />");
		sb.append("Este é um email automático enviado pelo SEI, informando que: \r <br />");
		sb.append("O Usuário \"").append(getUsuarioLogado().getNome()).append("\" \r <br />");
		sb.append("Registrou aula no dia: ").append(Uteis.getData(new Date(), "dd/MM/yyyy")).append(" <br />");
		sb.append("Com o Conteúdo: \"").append(conteudoAula).append("\" <br />");
		sb.append("O PDF contendo o Diário está em anexo. <br />");
		sb.append("\r <br />");
		return getMensagemFormatada(sb.toString());
	}

	public String gerarMensagemRegistroAula2(String conteudoAula) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("\r <br />");
		sb.append("Este é um email automático enviado pelo SEI, informando que: \r <br />");
		sb.append("O Usuário \"").append(getUsuarioLogado().getNome()).append("\" \r <br />");
		sb.append("Registrou aula no dia: ").append(Uteis.getData(new Date(), "dd/MM/yyyy")).append(" <br />");
		sb.append("Com o Conteúdo: \"").append(conteudoAula).append("\" <br />");
		sb.append("O PDF contendo o Diário está em anexo. <br />");
		sb.append("\r <br />");
		return sb.toString();
	}

	public String getMensagemFormatada(String mensagem) throws Exception {
		String temp = getConfiguracaoGeralPadraoSistema().getMensagemPadrao();
		if (temp.equals("")) {
			return mensagem;
		}
		String caminho = getCaminhoPastaWeb();
		temp = temp.replaceAll("http://localhost:8080/SEI/", caminho);
		temp = temp.replace("<TEXTO PADRAO>", mensagem);
		return temp;
	}

	public Boolean getPermiteLancamentoAulaFutura() {
		Boolean permite = false;
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermiteLancamentoAulaFuturaProfessor", getUsuarioLogado());
				permite = true;
			} catch (Exception e) {
				permite = false;
			}
			return permite;
		}
		if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermiteLancamentoAulaFuturaCoordenador", getUsuarioLogado());
				permite = true;
			} catch (Exception e) {
				permite = false;
			}
			return permite;
		}
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermiteLancamentoAulaFutura", getUsuarioLogado());
			permite = true;
		} catch (Exception e) {
			permite = false;
		}
		return permite;
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * RegistroAulaCons.jsp. Define o tipo de consulta a ser executada, por meio
	 * de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como
	 * resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getRegistroAulaFacade().consultaRapidaPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), "", "", super.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("cargaHoraria")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getRegistroAulaFacade().consultaRapidaPorCargaHoraria(new Integer(valorInt), "", "", super.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("conteudo")) {
				objs = getFacadeFactory().getRegistroAulaFacade().consultaRapidaPorConteudo(getControleConsulta().getValorConsulta(), "", "", super.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("identificadorTurma")) {
				objs = getFacadeFactory().getRegistroAulaFacade().consultaRapidaPorIdentificadorTurma(getControleConsulta().getValorConsulta(), "", "", super.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeDisciplina")) {
				objs = getFacadeFactory().getRegistroAulaFacade().consultaRapidaPorNomeDisciplina(getControleConsulta().getValorConsulta(), "", "", super.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeProfessor")) {
				objs = getFacadeFactory().getRegistroAulaFacade().consultaRapidaPorNomeProfessor(getControleConsulta().getValorConsulta(), "", "", super.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				objs = getFacadeFactory().getRegistroAulaFacade().consultaRapidaPorNomeCurso(getControleConsulta().getValorConsulta(), "", "", super.getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoTurma")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getRegistroAulaFacade().consultaRapidaPorCodigoTurma(new Integer(valorInt), "", "", true, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoDisciplina")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getRegistroAulaFacade().consultaRapidaPorCodigoDisciplina(new Integer(valorInt), "", "", true, getUsuarioLogado());
			}
			// objs = ControleConsulta.obterSubListPaginaApresentar(objs,
			// controleConsulta);
			// definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(),
			// controleConsulta.getNrTotalPaginas());
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroAulaCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroAulaCons.xhtml");
		}
	}

	public void consultarVisaoProfessor() {
		try {
			super.consultar();
			getFacadeFactory().getRegistroAulaFacade().validarConsultaDoUsuario(getUsuarioLogadoClone());
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("curso")) {
				objs = getFacadeFactory().getRegistroAulaFacade().consultarPorNomeCursoProfessor(getControleConsulta().getValorConsulta(), "", "", getUsuarioLogado().getPessoa().getCodigo(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				Date valorData = getControleConsulta().getDataIni();
				objs = getFacadeFactory().getRegistroAulaFacade().consultarPorDataProfessor(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), "", "", getUsuarioLogado().getPessoa().getCodigo(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("identificadorTurma")) {
				objs = getFacadeFactory().getRegistroAulaFacade().consultarPorIdentificadorTurmaProfessor(getControleConsulta().getValorConsulta(), "", "", getUsuarioLogado().getPessoa().getCodigo(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeDisciplina")) {
				objs = getFacadeFactory().getRegistroAulaFacade().consultarPorNomeDisciplinaProfessor(getControleConsulta().getValorConsulta(), "", "", getUsuarioLogado().getPessoa().getCodigo(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			// objs = ControleConsulta.obterSubListPaginaApresentar(objs,
			// controleConsulta);

			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean getIsConsultaPorData() {
		return getControleConsulta().getCampoConsulta() != null && getControleConsulta().getCampoConsulta().equals("data");
	}

	public boolean getIsConsultaPorTexto() {
		return getControleConsulta().getCampoConsulta() != null && !getControleConsulta().getCampoConsulta().equals("data");
	}

	public void paintConsultaAluno(OutputStream out, Object data) throws IOException {
		// FrequenciaAulaVO pessoa = (FrequenciaAulaVO)
		// getRegistroAulaVO().getFrequenciaAulaVOs().get((Integer) data);
		// BufferedImage bufferedImage = ImageIO.read(new
		// BufferedInputStream(new
		// ByteArrayInputStream(pessoa.getMatricula().getAluno().getFoto())));
		// ImageIO.write(bufferedImage, "jpg", out);

		ArquivoHelper arquivoHelper = new ArquivoHelper();
		List<FrequenciaAulaVO> listaConsultas = getRegistroAulaVO().getFrequenciaAulaVOs();
		try {
			arquivoHelper.renderizarImagemNaTela(out, listaConsultas.get((Integer) data).getMatricula().getAluno().getArquivoImagem(), getConfiguracaoGeralPadraoSistema(), getCaminhoPastaWeb(), "foto_usuario.jpg");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			arquivoHelper = null;
			listaConsultas = null;
		}

	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>RegistroAulaVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public void excluir() {
		excluir("Operação -> Exclusão pela Visão Administrativa");
	}

	public void excluir(String acaoLog) {
		try {
			setCalendarioRegistroAula(null);
			if (!getListaRegistrosAula().isEmpty()) {
				executarValidacaoSimulacaoVisaoProfessor();
				getFacadeFactory().getRegistroAulaFacade().excluir(getListaRegistrosAula(), acaoLog, getUsuarioLogado(), Boolean.FALSE);
				for (RegistroAulaVO registroAula : getListaRegistrosAula()) {
					if (registroAula.getCodigo().intValue() != 0) {
						realizarDesmarcarDiaLancado(registroAula.getData());
						registroAula.getFrequenciaAulaVOs().clear();

					}
				}
				setIsMostrarBotaoExcluir(false);
				setMensagemDetalhada("");
				setDiaSemana_Erro("");
				setMensagemID("msg_dados_excluidos");
				setRegistroAulaVO(null);
				getListaRegistrosAula().clear();
				setFrequenciaAulaVO(null);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>RegistroAulaVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public void excluirVisaoProfessor() {
		excluir("Operação -> Exclusão pela Visão do Professor");
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeCurso")) {
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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
			inicializarDadosTurma(obj.getCodigo());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private void inicializarDadosTurma(Integer turma) throws Exception{
		setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(turma, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
		if (getTurma().getSubturma()) {
			getTurma().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurma().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
		}
		setQuadroHorario(new QuadroHorarioVO());
		setDisciplina(new DisciplinaVO());
		setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
		setQuadroHorario(new QuadroHorarioVO());
		setHorarioProfessorDiaVO(new HorarioProfessorDiaVO());
		setFrequenciaAulaVO(new FrequenciaAulaVO());
		setIsMostrarCalendario(false);
		setIsMostrarListagemDeDisciplinas(false);
		if (getTurma().getPeriodicidade().equals("SE")) {
			setSemestre(getSemestre());
			setAno(getAno());
		} else if (getTurma().getPeriodicidade().equals("AN")) {
			setSemestre("");
			setAno(getAno());
		} else {
			setSemestre("");
			setAno("");
		}
		montarListaDisciplinaTurmaVisaoSecretaria();
		setIsMostrarBotaoExcluir(false);
	}

	private List tipoConsultaComboTurma;

	public List getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
			tipoConsultaComboTurma.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
			tipoConsultaComboTurma.add(new SelectItem("nomeTurno", "Turno"));
			tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboTurma;
	}

	private List listaSelectSemestre;

	public List getListaSelectSemestre() {
		if (listaSelectSemestre == null) {
			listaSelectSemestre = new ArrayList(0);
			listaSelectSemestre.add(new SelectItem("", " "));
			listaSelectSemestre.add(new SelectItem("1", "1º"));
			listaSelectSemestre.add(new SelectItem("2", "2º"));
		}
		return listaSelectSemestre;
	}

	public boolean getMostrarAnoSemestre() {
		if ((getTurma().getSemestral() && !getTurma().getCurso().getLiberarRegistroAulaEntrePeriodo()) || getTurma().getAnual()) {
			return true;
		} else {
			return false;
		}
	}

	public boolean getMostrarSemestre() {
		if ((getTurma().getSemestral() && !getTurma().getCurso().getLiberarRegistroAulaEntrePeriodo())) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>FrequenciaAula</code> para o objeto <code>registroAulaVO</code> da
	 * classe <code>RegistroAula</code>
	 */
	public String adicionarFrequenciaAula() throws Exception {
		try {
			if (!getRegistroAulaVO().getCodigo().equals(0)) {
				frequenciaAulaVO.setRegistroAula(getRegistroAulaVO().getCodigo());
			}
			if (!getFrequenciaAulaVO().getMatricula().getMatricula().equals("")) {
				String campoConsulta = getFrequenciaAulaVO().getMatricula().getMatricula();
				MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(campoConsulta, this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
				getFrequenciaAulaVO().setMatricula(matricula);
			}
			getFacadeFactory().getRegistroAulaFacade().adicionarFrequenciaAulaVOs(getFrequenciaAulaVO(), getRegistroAulaVO().getFrequenciaAulaVOs());
			setFrequenciaAulaVO(new FrequenciaAulaVO());
			setMensagemID("msg_dados_adicionados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroAulaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("registroAulaForm.xhtml");
		}
	}

	public void irPaginaInicial() throws Exception {
		controleConsulta.setPaginaAtual(1);
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
	 * Método responsável por processar a consulta na entidade
	 * <code>Matricula</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarMatriculaPorChavePrimaria() {
		try {
			String campoConsulta = frequenciaAulaVO.getMatricula().getMatricula();
			MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(campoConsulta, this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
			frequenciaAulaVO.getMatricula().setMatricula(matricula.getMatricula());
			this.setMatricula_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			frequenciaAulaVO.getMatricula().setMatricula("");
			this.setMatricula_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Pessoa</code> por meio de sua respectiva chave primária. Esta
	 * rotina é utilizada fundamentalmente por requisições Ajax, que realizam
	 * busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void consultarPessoaPorChavePrimaria() {
		try {
			Integer campoConsulta = registroAulaVO.getResponsavelRegistroAula().getCodigo();
			PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			registroAulaVO.getResponsavelRegistroAula().setNome(pessoa.getNome());
			this.setResponsavelRegistroAula_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			registroAulaVO.getResponsavelRegistroAula().setNome("");
			registroAulaVO.getResponsavelRegistroAula().setCodigo(0);
			this.setResponsavelRegistroAula_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/***
	 * Método responsável por montar o calendário de aula, baseado nos filtros
	 * informados: Ano / Semestre, disciplina e professor
	 */
	public void montarCalendarioDeAulas() {

		try {
			if (getDisciplina().getCodigo().intValue() != 0) {
				getQuadroHorario().setTurno(getTurma().getTurno());
				// realizarAtualizarQuadroHorario(true);
				realizarAtualizarQuadroHorarioVisaoSecretaria(true);
				setIsMostrarCalendario(true);
				setIsMostrarListagemDeDisciplinas(true);
			} else {
				setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
				setQuadroHorario(new QuadroHorarioVO());
				setConteudo("");
				setMensagemID("");
				setMensagem("");
				setIsMostrarCalendario(false);
				setIsMostrarListagemDeDisciplinas(false);
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarCalendarioDeAulasVisaoSecretaria() {
		try {
			limparCamposCalendarioAulas();
			if (!getDisciplina().getCodigo().equals(0) && !getProfessor().getCodigo().equals(0)) {
				getQuadroHorario().setTurno(getTurma().getTurno());
				realizarAtualizarQuadroHorarioVisaoSecretaria(true);
				setIsMostrarCalendario(true);
				setIsMostrarListagemDeDisciplinas(true);
			} 
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarHorarioTurmaDia() {
		try {
			if(Uteis.isAtributoPreenchido(getHorarioProfessorDiaVO().getCodigo())){
			//boolean permitirRealizarLancamentoAlunosPreMatriculados = getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
			setApresentarModalPermitirExcluirRegistroAulaOrfao("");
			setIsMostrarBotaoExcluir(false);
			setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
			setIsMostrarListagemDeDisciplinas(Boolean.FALSE);
			setConteudo("");
			setTipoAula("P");
			setPraticaSupervisionada("");
			setListaRegistrosAula(getFacadeFactory().getRegistroAulaFacade().realizarGeracaoRegistroAulaPeloHorarioProfessorDia(getHorarioProfessorDiaVO(), getUsuarioLogado(), getTurma(), getDisciplina(), getProfessor(), getAno(), getSemestre(), getTrazerAlunosTransferenciaMatriz()));
			if (getHorarioProfessorDiaVO().getIsLancadoRegistro()) {
				//montarRegistrosAula(false, getHorarioProfessorDiaVO().getIsLancadoRegistro(), permitirRealizarLancamentoAlunosPreMatriculados);
				realizarVerificarVisuaizarBotaoExcluir();
				for (RegistroAulaVO registroAula : getListaRegistrosAula()) {
					setConteudo(registroAula.getConteudo());
					setTipoAula(registroAula.getTipoAula());
					setPraticaSupervisionada(registroAula.getPraticaSupervisionada());
					break;
				}
				/**
				 * Regra adicionada para validar Registros de Aulas que estão
				 * órfãos no sistema, caso exista será exibido um modal
				 * verificando a possibilidade de exclusão dos mesmos.
				 */
				if (getListaRegistrosAula().isEmpty()) {
					setRegistroAulaOrfaoVOs(getFacadeFactory().getRegistroAulaFacade().consultarPorHorarioTurmaDia(getHorarioProfessorDiaVO(), getTurma().getCodigo(), getDisciplina().getCodigo(), getProfessor().getCodigo(), getAno(), getSemestre(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					if (!getRegistroAulaOrfaoVOs().isEmpty()) {
						setApresentarModalPermitirExcluirRegistroAulaOrfao("RichFaces.$('panelExcluirRegistroAulaOrfao').show()");
					}
				}
			} else {
				//montarAlunosTurma(false, getHorarioProfessorDiaVO().getIsLancadoRegistro(), permitirRealizarLancamentoAlunosPreMatriculados);
				setIsMostrarBotaoExcluir(false);
			}
			setMensagemID("msg_dados_editar");
			}
		} catch (Exception e) {
			setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
			getRegistroAulaVO().setFrequenciaAulaVOs(new ArrayList<FrequenciaAulaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean verificarPermissaoVisualizarMatriculaTR_CA() {
		try {
			if (getUsuarioLogado().getUsuarioPerfilAcessoVOs().isEmpty()) {
				UsuarioPerfilAcessoVO u = new UsuarioPerfilAcessoVO();
				u.setPerfilAcesso(getUsuarioLogado().getPerfilAcesso());
				getUsuarioLogado().getUsuarioPerfilAcessoVOs().add(u);
			}
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("RegistrarAula_VisualizarMatriculaTR_CA", getUsuarioLogado());
			return (true);
		} catch (Exception e) {
			return (false);
		}
	}

	public Boolean verificarPermissaoVisualizarDataMatricula() {
		try {
			if (getUsuarioLogado().getUsuarioPerfilAcessoVOs().isEmpty()) {
				UsuarioPerfilAcessoVO u = new UsuarioPerfilAcessoVO();
				u.setPerfilAcesso(getUsuarioLogado().getPerfilAcesso());
				getUsuarioLogado().getUsuarioPerfilAcessoVOs().add(u);
			}
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("RegistrarAula_VisualizarDataMatricula", getUsuarioLogado());
			return (true);
		} catch (Exception e) {
			return (false);
		}
	}

	public Boolean verificarPermissaoCoordVisualizarMatriculaTR_CA() {
		try {
			if (getUsuarioLogado().getUsuarioPerfilAcessoVOs().isEmpty()) {
				UsuarioPerfilAcessoVO u = new UsuarioPerfilAcessoVO();
				u.setPerfilAcesso(getUsuarioLogado().getPerfilAcesso());
				getUsuarioLogado().getUsuarioPerfilAcessoVOs().add(u);
			}
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("RegistrarAulaCoord_VisualizarMatriculaTR_CA", getUsuarioLogado());
			return (true);
		} catch (Exception e) {
			return (false);
		}
	}
		
	public void selecionarHorarioTurmaDiaVisaoProfessor() {
		try {
			//boolean permitirRealizarLancamentoAlunosPreMatriculados = getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
			setIsMostrarBotaoExcluir(false);
			setHorarioProfessorDiaVO((HorarioProfessorDiaVO) context().getExternalContext().getRequestMap().get("horarioProgramacaoDiaItens"));
			
			setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
			setIsMostrarListagemDeDisciplinas(Boolean.FALSE);
			setConteudo("");
			setTipoAula("P");
			setPraticaSupervisionada("");
			//boolean somenteAlunosAtivos = !verificarPermissaoVisualizarMatriculaTR_CA();
			setApresentarDataMatricula(verificarPermissaoVisualizarDataMatricula());
			setCalendarioRegistroAula(getFacadeFactory().getCalendarioRegistroAulaFacade().consultarPorCalendarioRegistroAulaUtilizar(getTurma().getUnidadeEnsino().getCodigo(), getTurma().getCodigo(), getTurma().getTurmaAgrupada(), getUsuarioLogado().getPessoa().getCodigo(), Uteis.getData(getHorarioProfessorDiaVO().getData(), "yyyy"), false, getUsuarioLogado()));
			setListaRegistrosAula(getFacadeFactory().getRegistroAulaFacade().realizarGeracaoRegistroAulaPeloHorarioProfessorDia(getHorarioProfessorDiaVO(), getUsuarioLogado(), getTurma(), getDisciplina(), getProfessor(), getAno(), getSemestre(), getTrazerAlunosTransferenciaMatriz()));
			if (getHorarioProfessorDiaVO().getIsLancadoRegistro()) {
//				montarRegistrosAula(somenteAlunosAtivos, getHorarioProfessorDiaVO().getIsLancadoRegistro(), permitirRealizarLancamentoAlunosPreMatriculados);
				realizarVerificarVisuaizarBotaoExcluir();
				for (RegistroAulaVO registroAula : getListaRegistrosAula()) {
					setConteudo(registroAula.getConteudo());
					setTipoAula(registroAula.getTipoAula());
					setPraticaSupervisionada(registroAula.getPraticaSupervisionada());
					break;
				}
//				Ordenacao.ordenarLista(getListaRegistrosAula(), "horario");
			}		else {
//				montarAlunosTurma(somenteAlunosAtivos, getHorarioProfessorDiaVO().getIsLancadoRegistro(), permitirRealizarLancamentoAlunosPreMatriculados);
				setIsMostrarBotaoExcluir(false);
			}
			
			
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
			getRegistroAulaVO().setFrequenciaAulaVOs(new ArrayList<FrequenciaAulaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			e.printStackTrace();
		}
	}

	public void selecionarHorarioTurmaDiaVisaoCoordenador() {
		try {
			//boolean permitirRealizarLancamentoAlunosPreMatriculados = getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados();
			setIsMostrarBotaoExcluir(false);
			setHorarioProfessorDiaVO((HorarioProfessorDiaVO) context().getExternalContext().getRequestMap().get("horarioProgramacaoDiaItens"));
			setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
			setIsMostrarListagemDeDisciplinas(Boolean.FALSE);
			setConteudo("");
			setTipoAula("P");
			//boolean somenteAlunoAtivo = !verificarPermissaoCoordVisualizarMatriculaTR_CA();
			setListaRegistrosAula(getFacadeFactory().getRegistroAulaFacade().realizarGeracaoRegistroAulaPeloHorarioProfessorDia(getHorarioProfessorDiaVO(), getUsuarioLogado(), getTurma(), getDisciplina(), getProfessor(), getAno(), getSemestre(), getTrazerAlunosTransferenciaMatriz()));
			if (getHorarioProfessorDiaVO().getIsLancadoRegistro()) {
				realizarVerificarVisuaizarBotaoExcluir();
				for (RegistroAulaVO registroAula : getListaRegistrosAula()) {
					setConteudo(registroAula.getConteudo());
					setTipoAula(registroAula.getTipoAula());
					break;
				}
				//montarRegistrosAula(somenteAlunoAtivo, getHorarioProfessorDiaVO().getIsLancadoRegistro(), permitirRealizarLancamentoAlunosPreMatriculados);
			} else {
				setIsMostrarBotaoExcluir(false);
				//montarAlunosTurma(somenteAlunoAtivo, getHorarioProfessorDiaVO().getIsLancadoRegistro(), permitirRealizarLancamentoAlunosPreMatriculados);
			}
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
			getRegistroAulaVO().setFrequenciaAulaVOs(new ArrayList<FrequenciaAulaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@Deprecated
	public void realizarAtualizarQuadroHorario(Boolean detalhado) throws Exception {
		// Obtém os horários do professor
		try {
			List lista = getFacadeFactory().getHorarioProfessorFacade().consultarPorProfessor(getProfessor().getCodigo(), null, null, getUsuarioLogado());

			Iterator i = lista.iterator();
			while (i.hasNext()) {
				HorarioProfessorVO horarioProfessor = (HorarioProfessorVO) i.next();
				if (horarioProfessor.getTurno().getCodigo().equals(getQuadroHorario().getTurno().getCodigo())) {
					setQuadroHorario(getFacadeFactory().getHorarioProfessorFacade().atualizarDadosQuadroHorario(horarioProfessor, getQuadroHorario(), detalhado, null, null, getUsuarioLogado()));
					montarDadosHorarioProfessorDia();
					adicionarQuadroHorario();
					// return;
				}
			}
			getFacadeFactory().getHorarioProfessorFacade().montarDadosListaQuadroHorarioVO(getQuadroHorario(), getUsuarioLogado());
			adicionarQuadroHorario();
			getRegistroAulaVO().getProfessor().montarListaHorarioProfessor();
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
	}

	public void realizarAtualizarQuadroHorarioVisaoSecretaria(Boolean detalhado) throws Exception {
		// Obtém os horários do professor
		// HorarioProfessorVO horarioProfessor =
		// getFacadeFactory().getHorarioProfessorFacade().consultarPorProfessorTurno(getProfessor().getCodigo().intValue(),
		// getTurma().getTurno().getCodigo().intValue(), getUsuarioLogado());
		if (getTurma().getTurmaAgrupada() && !getTurma().getCodigo().equals(0)) {
			getTurma().getCurso().setLiberarRegistroAulaEntrePeriodo(getFacadeFactory().getTurmaFacade().consultarLiberarRegistroAulaEnterPeriodoTurmaAgrupada(getTurma().getCodigo(), getUsuarioLogado()));
		}
		HorarioProfessorVO horarioProfessor = new HorarioProfessorVO();
		if (getTurma().getCurso().getNivelEducacionalPosGraduacao() || (getTurma().getCurso().getLiberarRegistroAulaEntrePeriodo())) {
			horarioProfessor = getFacadeFactory().getHorarioProfessorFacade().consultarRapidaHorarioProfessorTurno(getProfessor().getCodigo().intValue(), getTurma().getTurno().getCodigo().intValue(), getUsuarioLogado());
		} else {
			// int qtdeHorarioProfessor =
			// getFacadeFactory().getHorarioProfessorFacade().consultarPorProfessorTurnoAnoSemestreQtdeHorarioProfessor(getProfessor().getCodigo().intValue(),
			// getTurma().getTurno().getCodigo().intValue(), getSemestre(),
			// getAno(), getUsuarioLogado());
			// if (qtdeHorarioProfessor > 1) {
			// horarioProfessor =
			// getFacadeFactory().getHorarioProfessorFacade().consultarPorProfessorTurnoAnoSemestre(getProfessor().getCodigo().intValue(),
			// getTurma().getTurno().getCodigo().intValue(), getSemestre(),
			// getAno(), getTurma().getCodigo(), getUsuarioLogado());
			// } else {
			horarioProfessor = getFacadeFactory().getHorarioProfessorFacade().consultarPorProfessorTurnoAnoSemestre(getProfessor().getCodigo().intValue(), getTurma().getTurno().getCodigo().intValue(), getSemestre(), getAno(), getTurma().getCodigo(), getUsuarioLogado());
			// }
		}

		if (horarioProfessor.getTurno().getCodigo().equals(getQuadroHorario().getTurno().getCodigo())) {
			horarioProfessor.setHorarioProfessorDiaVOs(getFacadeFactory().getHorarioProfessorDiaFacade().consultarHorarioProfessorDia(horarioProfessor.getCodigo(), horarioProfessor.getTurno().getCodigo(), horarioProfessor.getProfessor().getCodigo(), getDisciplina().getCodigo(), getTurma().getCodigo(), null, null, null, null, null, getAno(), getSemestre()));
			getQuadroHorario().setHorarioProfessorVO(horarioProfessor);
//			getFacadeFactory().getHorarioProfessorFacade().montarDadosHorarioProfessorDiaItemVOsRegistroAula(horarioProfessor, getUsuarioLogado());
			getQuadroHorario().ordenarListas();
			montarDadosHorarioProfessorDia();
			adicionarQuadroHorario();
			return;
		} else {

			getFacadeFactory().getHorarioProfessorFacade().montarDadosListaQuadroHorarioVO(getQuadroHorario(), getUsuarioLogado());
			adicionarQuadroHorario();
			getRegistroAulaVO().getProfessor().montarListaHorarioProfessor();
		}
	}

	public void montarDadosHorarioProfessorDia() throws Exception {
		// List<HorarioProfessorDiaVO>
		// lstHorarioProfessorDiaVODaTurmaEDisciplina = new
		// ArrayList<HorarioProfessorDiaVO>(0);
		// for (HorarioProfessorDiaVO dia :
		// getQuadroHorario().getHorarioProfessorVO().getHorarioProfessorDiaVOs())
		// {
		// // pegar item a item para comparar os códigos da turma
		// List<Integer> listaTurmas = new ArrayList<Integer>();
		// List<Integer> listaDisciplinas = new ArrayList<Integer>();
		// realizarObterDisciplinaETurma(listaTurmas, listaDisciplinas, dia);
		// // Boolean possuiTurmaAgrupada = Boolean.FALSE;
		// int entrou = 0;
		// for (int i = 0; i < listaTurmas.size(); i++) {
		// dia.setIsLancadoRegistro(executarVerificarAulaRegistrada(dia));
		// /**
		// * Esta validação era utilizada devido a anteriormente o sistema
		// * não registrar aula para turma agrupada, sendo assim ele
		// * montava os horários da turma origem e da turma agrupada para
		// * realizar o registro de aula, já que o sistema agora trata o
		// * registro de aula para turma agrupada não há a necessidade
		// * desta.
		// */
		// // if (entrou == 0) {
		// // possuiTurmaAgrupada =
		// getFacadeFactory().getTurmaAgrupadaFacade().consultarPossuiTurmaAgrupada(listaTurmas.get(i),
		// getTurma().getCodigo().intValue());
		// // }
		// // if ((listaTurmas.get(i).intValue() ==
		// getTurma().getCodigo().intValue() || possuiTurmaAgrupada) &&
		// (listaDisciplinas.get(i).intValue() ==
		// getDisciplina().getCodigo().intValue())) {
		// if ((listaTurmas.get(i).intValue() ==
		// getTurma().getCodigo().intValue()) &&
		// (listaDisciplinas.get(i).intValue() ==
		// getDisciplina().getCodigo().intValue())) {
		// if (entrou == 0) {
		// lstHorarioProfessorDiaVODaTurmaEDisciplina.add(dia);
		// entrou++;
		// }
		// }
		// }
		// listaTurmas = null;
		// listaDisciplinas = null;
		// }
		// getQuadroHorario().getHorarioProfessorVO().setHorarioProfessorDiaVOs(null);
		// getQuadroHorario().getHorarioProfessorVO().setHorarioProfessorDiaVOs(lstHorarioProfessorDiaVODaTurmaEDisciplina);
		// for (HorarioProfessorDiaVO dia :
		// getQuadroHorario().getHorarioProfessorVO().getHorarioProfessorDiaVOs())
		// {
		// HorarioProfessorDia.montarDadosHorarioTurmaDiaItem(dia,
		// getDisciplina().getCodigo());
		// }
		// lstHorarioProfessorDiaVODaTurmaEDisciplina = null;
		getFacadeFactory().getHorarioProfessorFacade().inicializarDadosCalendario(getQuadroHorario().getHorarioProfessorVO(), getUsuarioLogado());
	}

	public void realizarMarcarDiaLancado(Date diaLancado) throws Exception {
		for (HorarioProfessorDiaVO dia : getQuadroHorario().getHorarioProfessorVO().getHorarioProfessorDiaVOs()) {
			if (dia.getData().compareTo(diaLancado) == 0) {
				dia.setIsLancadoRegistro(true);
				for (HorarioProfessorDiaItemVO horarioProfessorDiaItemVO : dia.getHorarioProfessorDiaItemVOs()) {
					horarioProfessorDiaItemVO.setAulaJaRegistrada(true);
				}
				return;
			}
		}
	}

	public void realizarDesmarcarDiaLancado(Date diaLancado) throws Exception {
		for (HorarioProfessorDiaVO dia : getQuadroHorario().getHorarioProfessorVO().getHorarioProfessorDiaVOs()) {
			if (dia.getData().compareTo(diaLancado) == 0) {
				dia.setIsLancadoRegistro(false);
				for (HorarioProfessorDiaItemVO horarioProfessorDiaItemVO : dia.getHorarioProfessorDiaItemVOs()) {
					horarioProfessorDiaItemVO.setAulaJaRegistrada(false);
				}
				return;
			}
		}
	}

	public boolean executarVerificarAulaRegistrada(HorarioProfessorDiaVO horarioProfessorDiaVO) throws Exception {
		// RegistroAulaVO registroAula =
		// getFacadeFactory().getRegistroAulaFacade().consultarPorDataTurmaProfessor(horarioProfessorDiaVO.getData(),
		// getTurma(), getDisciplina().getCodigo(), getProfessor().getCodigo(),
		// getAno(), getSemestre(), Uteis.NIVELMONTARDADOS_DADOSBASICOS,
		// getUsuarioLogado());
		Boolean existeRegistroAula = getFacadeFactory().getRegistroAulaFacade().realizarVerificacaoPorDataTurmaProfessorDisciplina(horarioProfessorDiaVO.getData(), getTurma(), getDisciplina().getCodigo(), getProfessor().getCodigo(), getAno(), getSemestre());
		return existeRegistroAula;
		// if (registroAula == null || registroAula.getCodigo().intValue() == 0)
		// {
		// return false;
		// } else {
		// return true;
		// }
	}	

	public void adicionarQuadroHorario() throws Exception {
		try {
			getRegistroAulaVO().getProfessor().adicionarObjQuadroHorarioVOs(getQuadroHorario());
			setMensagemID("msg_dados_adicionados");
			if (!getQuadroHorario().getHorarioProfessorVO().getCalendarioHorarioAulaVOs().isEmpty()) {
				setIsMostrarCalendario(Boolean.TRUE);
				Ordenacao.ordenarLista(getQuadroHorario().getHorarioProfessorVO().getCalendarioHorarioAulaVOs(), "campoOrdenacao");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>ProgramacaoAula</code> por meio de sua respectiva chave primária.
	 * Esta rotina é utilizada fundamentalmente por requisições Ajax, que
	 * realizam busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void montarAlunosTurma(boolean filtroVisaoProfessor, Boolean isLancadoRegistro, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception {
		try {
			List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorCodigoTurmaDisciplinaSemestreAnoFiltroVisaoProfessor(null, disciplina.getCodigo(), ano, semestre, "", false, filtroVisaoProfessor, getTrazerAlunoPendenteFinanceiramente(), "", "", false, getUsuarioLogado(), getTurma(), getTrazerAlunosTransferenciaMatriz(), permitirRealizarLancamentoAlunosPreMatriculados);
			getFacadeFactory().getRegistroAulaFacade().montarRegistrosAula(getListaHorario(), getHorarioProfessorDiaVO(), getListaRegistrosAula(), getDisciplina(), getProfessor(), getAno(), getSemestre(), getCargaHoraria(), getResponsavelRegistroAula(), getTipoAula(), getTurma(), getConteudo(), filtroVisaoProfessor, listaMatriculaPeriodoTurmaDisciplina, getUsuarioLogado(), true, isLancadoRegistro, getTrazerAlunoPendenteFinanceiramente(), getTrazerAlunosTransferenciaMatriz(), getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados());
			if (getTurma().getTurmaAgrupada()) {
				carregarConfiguracaoAcademicaCursoDaMatricula();
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getRegistroAulaVO().setFrequenciaAulaVOs(new ArrayList<FrequenciaAulaVO>(0));
			throw e;
		}
	}

	public void aplicarPresencaTodasAulas() {
		FrequenciaAulaVO frequenciaAlterar = (FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaAulaItens");
		for (RegistroAulaVO registros : this.listaRegistrosAula) {
			for (FrequenciaAulaVO freq : registros.getFrequenciaAulaVOs()) {
				if (freq.getMatricula().getMatricula().equals(frequenciaAlterar.getMatricula().getMatricula())) {
					if(!frequenciaAlterar.getFrequenciaOculta() && frequenciaAlterar.getEditavel() && !frequenciaAlterar.getJustificado() && !frequenciaAlterar.getAbonado()){
						freq.setPresente(Boolean.TRUE);
					}
				}
			}
		}
	}

	public void aplicarFaltaTodasAulas() {
		FrequenciaAulaVO frequenciaAlterar = (FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaAulaItens");
		for (RegistroAulaVO registros : this.listaRegistrosAula) {
			for (FrequenciaAulaVO freq : registros.getFrequenciaAulaVOs()) {
				if (freq.getMatricula().getMatricula().equals(frequenciaAlterar.getMatricula().getMatricula())) {
					freq.setPresente(Boolean.FALSE);
				}
			}
		}

	}

	public void removerAbonoFrequenciaAula() {
		try {
			FrequenciaAulaVO frequenciaAlterar = (FrequenciaAulaVO) context().getExternalContext().getRequestMap().get("frequenciaItem");
			frequenciaAlterar.setAbonado(Boolean.FALSE);
			frequenciaAlterar.setJustificado(Boolean.FALSE);
			frequenciaAlterar.setPresente(Boolean.TRUE);
			frequenciaAlterar.setFrequenciaOculta(Boolean.FALSE);
			frequenciaAlterar.setDisciplinaAbonoVO(null);
			frequenciaAlterar.setEditavel(Boolean.TRUE);
			frequenciaAlterar.setRemoverAbono(Boolean.TRUE);
			
			setMensagemID("msg_dados_excluidos");
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarRegistrosAula(boolean filtroVisaoProfessor, Boolean isLancadoRegistro, boolean permitirRealizarLancamentoAlunosPreMatriculados) throws Exception {
		setListaRegistrosAula(getFacadeFactory().getRegistroAulaFacade().consultaRapidaPorHorarioTurmaDia(getHorarioProfessorDiaVO(), getTurma(), getDisciplina().getCodigo(), getProfessor().getCodigo(), getAno(), getSemestre(), getTurma().getCurso().getLiberarRegistroAulaEntrePeriodo(), getUsuarioLogado(), filtroVisaoProfessor, getTrazerAlunoPendenteFinanceiramente(), getTrazerAlunosTransferenciaMatriz()));
		List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().consultaRapidaPorCodigoTurmaDisciplinaSemestreAnoFiltroVisaoProfessor(getListaRegistrosAula(), disciplina.getCodigo(), ano, semestre, "", false, filtroVisaoProfessor, getTrazerAlunoPendenteFinanceiramente(), "", "", false, getUsuarioLogado(), getTurma(), getTrazerAlunosTransferenciaMatriz(), permitirRealizarLancamentoAlunosPreMatriculados);
		getFacadeFactory().getRegistroAulaFacade().montarRegistrosAula(getListaHorario(), getHorarioProfessorDiaVO(), getListaRegistrosAula(), getDisciplina(), getProfessor(), getAno(), getSemestre(), getCargaHoraria(), getResponsavelRegistroAula(), getTipoAula(), getTurma(), getConteudo(), filtroVisaoProfessor, isLancadoRegistro, listaMatriculaPeriodoTurmaDisciplina, getUsuarioLogado(), getTrazerAlunoPendenteFinanceiramente(), getTrazerAlunosTransferenciaMatriz(), getConfiguracaoGeralPadraoSistema().getPermitirProfessorRealizarLancamentoAlunosPreMatriculados());
		for (RegistroAulaVO registroAula : listaRegistrosAula) {
			setConteudo(registroAula.getConteudo());
			setTipoAula(registroAula.getTipoAula());
			break;
		}
		Ordenacao.ordenarLista(listaRegistrosAula, "nrAula");
		setMensagemID("msg_dados_consultados");
	}

	public void montarListaSelectItemTurma() {
		List<Integer> mapAuxiliarSelectItem = new ArrayList();
		List listaResultado = null;
		Iterator i = null;
		try {
			List obj = new ArrayList(0);
			listaResultado = consultarTurmaPorProfessor();
			obj.add(new SelectItem(0, ""));
			i = listaResultado.iterator();
			String value = "";
			while (i.hasNext()) {
				TurmaVO turma = (TurmaVO) i.next();
				if(!mapAuxiliarSelectItem.contains(turma.getCodigo())){
					obj.add(new SelectItem(turma.getCodigo(), turma.aplicarRegraNomeCursoApresentarCombobox()));
            		mapAuxiliarSelectItem.add(turma.getCodigo());
				}
			}
			setListaSelectItemTurma(obj);
		} catch (Exception e) {
			setListaSelectItemTurma(new ArrayList(0));
		} finally {
			Uteis.liberarListaMemoria(listaResultado);
			i = null;
		}
	}

	public List consultarTurmaPorProfessor() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), getSemestre(), getAno(), "AT", getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado().getVisaoLogar().equals("professor"), true, true);
		// if (getUsuarioLogado().getVisaoLogar().equals("professor")) {
		// return
		// getFacadeFactory().getTurmaFacade().consultaRapidaTurmaPorProfessorSemestreAnoSituacaoValidandoHorarioTurma(getUsuarioLogado().getPessoa().getCodigo(),
		// Uteis.getSemestreAtual(), Uteis.getData(new Date(), "yyyy"), "AT", 0,
		// false, getUsuarioLogado());
		// } else {
		// return
		// getFacadeFactory().getTurmaFacade().consultaRapidaTurmaPorProfessor(getUsuarioLogado().getPessoa().getCodigo(),
		// getUnidadeEnsinoLogado().getCodigo().intValue(), false,
		// getUsuarioLogado());
		// }
	}

	public boolean getPermiteAlterarCargaHoraria() {
		// if (getRegistroAulaVO().getPermiteAlterarCargaHoraria() ||
		if (getPossuiPermissaoAlterarCargaHoraria()) {
			return true;
		}
		return false;
	}

	public int getCount() {
		return getRegistroAulaVO().getConteudo().length();
	}

	public String getCssCargaHoraria() {
		if (getRegistroAulaVO().getPermiteAlterarCargaHoraria() || getPossuiPermissaoAlterarCargaHoraria()) {
			return "camposObrigatorios";
		}
		return "camposSomenteLeitura";
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Turma</code> por meio de sua respectiva chave primária. Esta rotina
	 * é utilizada fundamentalmente por requisições Ajax, que realizam busca
	 * pela chave primária da entidade montando automaticamente o resultado da
	 * consulta para apresentação.
	 */
	public void consultarTurmaPorChavePrimaria() {
		try {
			setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
			// registroAulaVO.setFrequenciaAulaVOs(new ArrayList(0));
			String campoConsulta = getTurma().getIdentificadorTurma();
			if (campoConsulta.equalsIgnoreCase("")) {
				throw new Exception();
			} else {
				// TurmaVO turmas =
				// getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(campoConsulta,
				// super.getUnidadeEnsinoLogado().getCodigo(), false,
				// Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS);
				TurmaVO turmas = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaEspecifico(getTurma(), campoConsulta, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
				setTurma(turmas);
				montarListaSelectItemDisciplinasProgramacaoAula();
				if (turmas.getSemestral()) {
					setSemestre(getSemestre());
					setAno(getAno());
				} else if (turmas.getAnual()) {
					setSemestre("");
					setAno(getAno());
				} else {
					setSemestre("");
					setAno("");
				}
				setMensagemID("msg_dados_consultados");
			}
		} catch (Exception e) {
			limparTurma();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarAlunosVisaoProfessor() throws Exception {
		try {
			setQuadroHorario(new QuadroHorarioVO());
			setDisciplina(new DisciplinaVO());
			setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));			
			setHorarioProfessorDiaVO(new HorarioProfessorDiaVO());
			setFrequenciaAulaVO(new FrequenciaAulaVO());
			setIsMostrarCalendario(false);
			setIsMostrarListagemDeDisciplinas(false);
			if (getTurma().getCodigo() > 0) {
				TurmaVO turmas = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
				setTurma(turmas);
				getIsApresentarSemestreVisaoProfessorCoordenador();
				montarListaDisciplinaTurmaVisaoProfessor();
				setMensagemID("msg_dados_consultados");
			} else {
				limparMensagem();
			}
			// montarComboDisciplinaPorAnoSemestre();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaDisciplinaTurmaVisaoProfessor() {
		try {
			List objs = new ArrayList(0);
			List resultado = consultarDisciplinaProfessorTurma();
			Iterator i = resultado.iterator();
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				DisciplinaVO obj = (DisciplinaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemDisciplinasProgramacaoAula(objs);

		} catch (Exception e) {
			setListaSelectItemDisciplinasProgramacaoAula(new ArrayList(0));
		}
	}

	public void montarListaDisciplinaTurmaVisaoSecretaria() {
		try {
			setQuadroHorario(new QuadroHorarioVO());
			setDisciplina(new DisciplinaVO());
			setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
			setQuadroHorario(new QuadroHorarioVO());
			setHorarioProfessorDiaVO(new HorarioProfessorDiaVO());
			setFrequenciaAulaVO(new FrequenciaAulaVO());
			setIsMostrarCalendario(false);
			setIsMostrarListagemDeDisciplinas(false);
			List objs = new ArrayList(0);
			List resultado = consultarDisciplinaProfessorTurmaVisaoSecretaria();
			Iterator i = resultado.iterator();
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				DisciplinaVO obj = (DisciplinaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemDisciplinasProgramacaoAula(objs);
			setIsMostrarListagemDeDisciplinas(Boolean.TRUE);
			setIndex(0);
		} catch (Exception e) {
			setListaSelectItemDisciplinasProgramacaoAula(new ArrayList(0));
		}
	}

	public void montarListaDisciplinaTurmaVisaoCoordenador() {
		try {
			setQuadroHorario(new QuadroHorarioVO());
			setDisciplina(new DisciplinaVO());
			setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
			setQuadroHorario(new QuadroHorarioVO());
			setHorarioProfessorDiaVO(new HorarioProfessorDiaVO());
			setFrequenciaAulaVO(new FrequenciaAulaVO());
			setIsMostrarCalendario(false);
			setIsMostrarListagemDeDisciplinas(false);
			setMensagemDetalhada("");
			if ((getTurma().getAnual() || getTurma().getSemestral()) && getAno().equals("") && !getProfessor().getCodigo().equals(0)) {
				getProfessor().setCodigo(0);
				throw new ConsistirException("Por favor informe o ano para buscar as disciplinas do professor.");
			}
			getDisciplina().setCodigo(0);
			List objs = new ArrayList(0);
			List resultado = consultarDisciplinaProfessorTurmaVisaoSecretaria();
			Iterator i = resultado.iterator();
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				DisciplinaVO obj = (DisciplinaVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemDisciplinasProgramacaoAula(objs);
			setIsMostrarListagemDeDisciplinas(Boolean.TRUE);
		} catch (Exception e) {
			setListaSelectItemDisciplinasProgramacaoAula(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarComboDisciplinaPorAnoSemestre() {
		try {
			getListaSelectItemProfessores().clear();
			setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
//			setListaSelectItemDisciplinasProgramacaoAula(new ArrayList<SelectItem>(0));
			setConteudo("");
			setIsMostrarCalendario(false);
//			setDisciplina(new DisciplinaVO());
			setQuadroHorario(new QuadroHorarioVO());
			setProfessor(new PessoaVO());
			setIsMostrarListagemDeDisciplinas(false);
			montarListaProfessoresTurmaDisciplina();
		} catch (Exception e) {
			setListaSelectItemDisciplinasProgramacaoAula(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List consultarDisciplinaProfessorTurma() throws Exception {
		if (getTurma().getCodigo() > 0 && getTurma().getTurmaAgrupada()) {
			getTurma().getCurso().setLiberarRegistroAulaEntrePeriodo(getFacadeFactory().getTurmaFacade().consultarLiberarRegistroAulaEnterPeriodoTurmaAgrupada(getTurma().getCodigo(), getUsuarioLogado()));
		}
		if (getTurma().getCurso().getNivelEducacionalPosGraduacao() || (getTurma().getCurso().getLiberarRegistroAulaEntrePeriodo())) {
			return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDia(getUsuarioLogado().getPessoa().getCodigo(), getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		} else {
			return getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurmaValidandoHorarioTurmaDiaSemestreAtual(getUsuarioLogado().getPessoa().getCodigo(), getTurma().getCodigo(), getAno(), getSemestre(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		}
	}

	public List consultarDisciplinaProfessorTurmaVisaoSecretaria() throws Exception {
		return getFacadeFactory().getDisciplinaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());

	}

	public void limparListaFrequencia() {
		getRegistroAulaVO().setFrequenciaAulaVOs(new ArrayList(0));
		montarListaDisciplinaTurmaVisaoCoordenador();
	}

	public void carregarCargaHoraria() {
		try {
			setMensagemDetalhada("", "");
			setMensagemDetalhada("");
			setDiaSemana_Erro("");
			// montarListaSelectItemProfessor(getFacadeFactory().getRegistroAulaFacade().carregarCargaHorariaEmontarListaProfessorRegistroAula(registroAulaVO,
			// getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			registroAulaVO.setCargaHoraria(0);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarDisciplinaPorNome(Integer prm) throws Exception {
		List lista = new ArrayList(0);
		lista = getFacadeFactory().getGradeDisciplinaFacade().consultarGradeDisciplinas(prm, false, getUsuarioLogado(), null);
		return lista;
	}

	// public void montarListaDisciplinaAgrupada() throws Exception {
	// if (this.registroAulaVO.getTurma().getCodigo().intValue() == 0) {
	// setListaSelectItemDisciplinasProgramacaoAula(new ArrayList(0));
	// return;
	// }
	// List resultadoConsulta = consultarDisciplinaTurmaAgrupada();
	// Iterator i = resultadoConsulta.iterator();
	// List objs = new ArrayList(0);
	// objs.add(new SelectItem(0, ""));
	// while (i.hasNext()) {
	// DisciplinaVO obj = (DisciplinaVO) i.next();
	// objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
	// }
	// SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
	// Collections.sort((List) objs, ordenador);
	// setListaSelectItemDisciplinasProgramacaoAula(objs);
	// }
	// public List<DisciplinaVO> consultarDisciplinaTurmaAgrupada() throws
	// Exception {
	// List<DisciplinaVO> objs =
	// getFacadeFactory().getDisciplinaFacade().consultarDisciplinaTurmaAgrupada(getRegistroAulaVO().getTurma().getCodigo(),
	// Uteis.NIVELMONTARDADOS_DADOSBASICOS);
	// return objs;
	// }
	public void montarListaDisciplinaNaoAgrupada() throws Exception {
		if (this.getTurma().getCodigo().intValue() == 0) {
			getListaSelectItemDisciplinasProgramacaoAula().clear();
			return;
		}
		getFacadeFactory().getTurmaFacade().carregarDados(this.getTurma(), NivelMontarDados.TODOS, getUsuarioLogado());
		// getTurma().setTurmaDisciplinaVOs(getFacadeFactory().getTurmaDisciplinaFacade().consultarTurmaDisciplinas(getTurma().getCodigo(),
		// false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS,
		// getUsuarioLogado()));
		getListaSelectItemDisciplinasProgramacaoAula().clear();
		getListaSelectItemDisciplinasProgramacaoAula().add(new SelectItem(0, ""));
		for (TurmaDisciplinaVO obj : getTurma().getTurmaDisciplinaVOs()) {
			getListaSelectItemDisciplinasProgramacaoAula().add(new SelectItem(obj.getDisciplina().getCodigo(), obj.getDisciplina().getNome()));
//			removerObjetoMemoria(obj);
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) getListaSelectItemDisciplinasProgramacaoAula(), ordenador);
	}

	public void montarListaProfessoresTurmaDisciplina() throws Exception {
		getListaSelectItemProfessores().clear();
		getListaSelectItemProfessores().add(new SelectItem(0, ""));
		if (this.getTurma().getCodigo().intValue() == 0) {
			return;
		}
		setProfessor(new PessoaVO());
		List<PessoaVO> listaProfessoresVOs = new ArrayList<PessoaVO>(0);
		try {
			if (getTurma().getSemestral() || getTurma().getAnual()) {
				if (getAno().length() != 4 || (getTurma().getSemestral() && getSemestre().isEmpty())) {
					limparCamposCalendarioAulas();
					return;
				}
				listaProfessoresVOs = getFacadeFactory().getPessoaFacade().consultarPorTurma(getTurma().getCodigo().intValue(), getDisciplina().getCodigo(), getAno(), getSemestre(), Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
			} else {
				listaProfessoresVOs = getFacadeFactory().getPessoaFacade().consultarPorTurma(getTurma().getCodigo().intValue(), getDisciplina().getCodigo(), "", "", Uteis.NIVELMONTARDADOS_COMBOBOX, false, getUsuarioLogado());
			}
			if (!getDisciplina().getCodigo().equals(0)
					&& Uteis.isAtributoPreenchido(listaProfessoresVOs)) {
				listaProfessoresVOs
				.stream()
				.peek(this::montarCalendarioAulas)
				.map(this::montarSelectItemProfessor)
				.forEach(getListaSelectItemProfessores()::add);
			} else if (!Uteis.isAtributoPreenchido(getProfessor())) {
				limparCamposCalendarioAulas();
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(listaProfessoresVOs);
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Cidade</code>.
	 */
	public void montarListaSelectItemDisciplinasProgramacaoAula(String prm) throws Exception {
		montarListaDisciplinaNaoAgrupada();
		// if (getRegistroAulaVO().getTurma().getTurmaAgrupada()) {
		// montarListaDisciplinaAgrupada();
		// } else {

		// }
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>Cidade</code>. Buscando todos os objetos correspondentes a entidade
	 * <code>Cidade</code>. Esta rotina não recebe parâmetros para filtragem de
	 * dados, isto é importante para a inicialização dos dados da tela para o
	 * acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemDisciplinasProgramacaoAula() {
		try {
			montarListaSelectItemDisciplinasProgramacaoAula("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	// public void montarListaSelectItemProfessor(List<PessoaVO> professores) {
	// try {
	// setListaSelectItemProfessor(new ArrayList(0));
	// for (PessoaVO obj : professores) {
	// getListaSelectItemProfessor().add(new SelectItem(obj.getCodigo(),
	// obj.getNome()));
	// }
	// } catch (Exception e) {
	// // System.out.println("MENSAGEM => " + e.getMessage());;
	// }
	// }
	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemDisciplinasProgramacaoAula();
		// setListaSelectItemProfessor(new ArrayList(0));
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	private List tipoConsultaCombo;

	public List getTipoConsultaCombo() {
		if (tipoConsultaCombo == null) {
			tipoConsultaCombo = new ArrayList(0);
			tipoConsultaCombo.add(new SelectItem("identificadorTurma", "Identificador Turma"));
			tipoConsultaCombo.add(new SelectItem("data", "Data"));
			tipoConsultaCombo.add(new SelectItem("nomeCurso", "Curso"));
			tipoConsultaCombo.add(new SelectItem("nomeProfessor", "Professor"));
			tipoConsultaCombo.add(new SelectItem("nomeDisciplina", "Disciplina"));
		}
		return tipoConsultaCombo;
	}

	private List tipoConsultaComboProfessor;

	public List getTipoConsultaComboProfessor() {
		if (tipoConsultaComboProfessor == null) {
			tipoConsultaComboProfessor = new ArrayList(0);
			tipoConsultaComboProfessor.add(new SelectItem("curso", "Curso"));
			tipoConsultaComboProfessor.add(new SelectItem("data", "Data"));
			tipoConsultaComboProfessor.add(new SelectItem("identificadorTurma", "Identificador Turma"));
			tipoConsultaComboProfessor.add(new SelectItem("nomeDisciplina", "Disciplina"));
		}
		return tipoConsultaComboProfessor;
	}

	private List tipoAulaCombo;

	public List getTipoAulaCombo() {
		if (tipoAulaCombo == null) {
			tipoAulaCombo = new ArrayList(0);
			tipoAulaCombo.add(new SelectItem("P", "Programada"));
			tipoAulaCombo.add(new SelectItem("R", "Reposição"));
			tipoAulaCombo.add(new SelectItem("E", "Evento"));
		}
		return tipoAulaCombo;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>diaSemana</code>
	 */
	private List listaSelectItemDiaSemanaProgramacaoAula;

	public List getListaSelectItemDiaSemanaProgramacaoAula() throws Exception {
		if (listaSelectItemDiaSemanaProgramacaoAula == null) {
			listaSelectItemDiaSemanaProgramacaoAula = new ArrayList(0);
			listaSelectItemDiaSemanaProgramacaoAula.add(new SelectItem("", ""));
			Hashtable diaSemanas = (Hashtable) Dominios.getDiaSemana();
			Enumeration keys = diaSemanas.keys();
			while (keys.hasMoreElements()) {
				String value = (String) keys.nextElement();
				String label = (String) diaSemanas.get(value);
				listaSelectItemDiaSemanaProgramacaoAula.add(new SelectItem(value, label));
			}
			SelectItemOrdemChave ordenador = new SelectItemOrdemChave();
			Collections.sort((List) listaSelectItemDiaSemanaProgramacaoAula, ordenador);
		}
		return listaSelectItemDiaSemanaProgramacaoAula;
	}

	public void carregarNovoRegistroAula() {
		try {
			getRegistroAulaVO().setNovoObj(true);
			// carregarCargaHoraria();
			RegistroAulaVO registroAulaAnterior = getFacadeFactory().getRegistroAulaFacade().consultarPorChavePrimaria(getRegistroAulaVO().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
			if (!getRegistroAulaVO().getData().equals(registroAulaAnterior.getData())) {
				getFacadeFactory().getRegistroAulaFacade().montarAlunosTurma(getRegistroAulaVO(), getUsuarioLogado());
			}
			setMensagemID("msg_entre_dados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		// setPaginaAtualDeTodas("0/0");
		// setListaConsulta(new ArrayList(0));
		// definirVisibilidadeLinksNavegacao(0, 0);
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("registroAulaCons.xhtml");
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

	public List getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList();
		}
		return listaConsultaTurma;
	}

	public void inicializarPlanoEnsino() {
		try {
			PlanoEnsinoControle planoEnsinoControle = (PlanoEnsinoControle) getControlador("PlanoEnsinoControle");
			planoEnsinoControle.setPlanoEnsinoVO(this.getPlanoEnsinoVO());
			planoEnsinoControle.getPlanoEnsinoVO().setGradeCurricular(getTurma().getGradeCurricularVO());
			planoEnsinoControle.realizarImpressaoPlanoEnsino();
			setFazerDownload(true);
			setCaminhoRelatorio(planoEnsinoControle.getCaminhoRelatorio());			
		} catch (Exception e) {
			
		}
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

	public String getMatricula_Erro() {
		if (matricula_Erro == null) {
			matricula_Erro = "";
		}
		return matricula_Erro;
	}

	public void setMatricula_Erro(String matricula_Erro) {
		this.matricula_Erro = matricula_Erro;
	}

	public FrequenciaAulaVO getFrequenciaAulaVO() {
		if (frequenciaAulaVO == null) {
			frequenciaAulaVO = new FrequenciaAulaVO();
		}
		return frequenciaAulaVO;
	}

	public void setFrequenciaAulaVO(FrequenciaAulaVO frequenciaAulaVO) {
		this.frequenciaAulaVO = frequenciaAulaVO;
	}

	public String getResponsavelRegistroAula_Erro() {
		if (responsavelRegistroAula_Erro == null) {
			responsavelRegistroAula_Erro = "";
		}
		return responsavelRegistroAula_Erro;
	}

	public void setResponsavelRegistroAula_Erro(String responsavelRegistroAula_Erro) {
		this.responsavelRegistroAula_Erro = responsavelRegistroAula_Erro;
	}

	public String getProgramacaoAula_Erro() {
		if (programacaoAula_Erro == null) {
			programacaoAula_Erro = "";
		}
		return programacaoAula_Erro;
	}

	public void setProgramacaoAula_Erro(String programacaoAula_Erro) {
		this.programacaoAula_Erro = programacaoAula_Erro;
	}

	public RegistroAulaVO getRegistroAulaVO() {
		if (registroAulaVO == null) {
			registroAulaVO = new RegistroAulaVO();
		}
		return registroAulaVO;
	}

	public void setRegistroAulaVO(RegistroAulaVO registroAulaVO) {
		this.registroAulaVO = registroAulaVO;
	}

	public String getTurma_Erro() {
		if (turma_Erro == null) {
			turma_Erro = "";
		}
		return turma_Erro;
	}

	public void setTurma_Erro(String turma_Erro) {
		this.turma_Erro = turma_Erro;
	}

	public String getDisciplina_Erro() {
		if (disciplina_Erro == null) {
			disciplina_Erro = "";
		}
		return disciplina_Erro;
	}

	public void setDisciplina_Erro(String disciplina_Erro) {
		this.disciplina_Erro = disciplina_Erro;
	}

	public List getListaSelectItemDisciplinasProgramacaoAula() {
		if (listaSelectItemDisciplinasProgramacaoAula == null) {
			listaSelectItemDisciplinasProgramacaoAula = new ArrayList();
		}
		return listaSelectItemDisciplinasProgramacaoAula;
	}

	public void setListaSelectItemDisciplinasProgramacaoAula(List listaSelectItemDisciplinasProgramacaoAula) {
		this.listaSelectItemDisciplinasProgramacaoAula = listaSelectItemDisciplinasProgramacaoAula;
	}

	public String getDiaSemana_Erro() {
		if (diaSemana_Erro == null) {
			diaSemana_Erro = "";
		}
		return diaSemana_Erro;
	}

	public void setDiaSemana_Erro(String diaSemana_Erro) {
		this.diaSemana_Erro = diaSemana_Erro;
	}

	public String getTurma_cursoTurno() {
		if (turma_cursoTurno == null) {
			turma_cursoTurno = "";
		}
		return turma_cursoTurno;
	}

	public void setTurma_cursoTurno(String turma_cursoTurno) {
		this.turma_cursoTurno = turma_cursoTurno;
	}

	public List getListaProfessor() {
		if (listaProfessor == null) {
			listaProfessor = new ArrayList();
		}
		return listaProfessor;
	}

	public void setListaProfessor(List listaProfessor) {
		this.listaProfessor = listaProfessor;
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList();
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
	}

	public boolean getPossuiPermissaoAlterarCargaHoraria() {
		return possuiPermissaoAlterarCargaHoraria;
	}

	public void setPossuiPermissaoAlterarCargaHoraria(boolean possuiPermissaoAlterarCargaHoraria) {
		this.possuiPermissaoAlterarCargaHoraria = possuiPermissaoAlterarCargaHoraria;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		registroAulaVO = null;
		programacaoAula_Erro = null;
		responsavelRegistroAula_Erro = null;
		turma_Erro = null;
		disciplina_Erro = null;
		diaSemana_Erro = null;
		Uteis.liberarListaMemoria(listaSelectItemDisciplinasProgramacaoAula);
		Uteis.liberarListaMemoria(listaSelectItemProfessor);
		Uteis.liberarListaMemoria(listaProfessor);
		frequenciaAulaVO = null;
		matricula_Erro = null;
		turma_cursoTurno = null;

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
			listaConsultaProfessor = new ArrayList();
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

	private List tipoConsultaComboProfessorBusca;

	public List getTipoConsultaComboProfessorBusca() {
		if (tipoConsultaComboProfessorBusca == null) {
			tipoConsultaComboProfessorBusca = new ArrayList();
			tipoConsultaComboProfessorBusca.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboProfessorBusca.add(new SelectItem("cpf", "CPF"));
		}
		return tipoConsultaComboProfessorBusca;
	}

	public void selecionarProfessor2() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("professorItens");
			this.setProfessor(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			FuncionarioVO fun = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(obj.getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			montarListaDisciplinaTurmaVisaoSecretaria();

		} catch (Exception e) {
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
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean getIsApresentarAnoVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()) {
				if (!getTurma().getCodigo().equals(0)) {
					if (getTurma().getSemestral()) {
						return true;
					} else if (getTurma().getAnual()) {
						return true;
					} else {
						setAno("");
						return false;
					}
				}
				return true;
			} else {
				if (!getTurma().getCodigo().equals(0)) {
					if (!(getTurma().getSemestral() || getTurma().getAnual())) {
						setAno("");
					}
				}
				return false;
			}
		}
		return true;
	}

	public void selecionarConteudoPlanoEnsino() {
		try {
			ConteudoPlanejamentoVO obj = (ConteudoPlanejamentoVO) context().getExternalContext().getRequestMap().get("planejamento");
			setConteudo(obj.getConteudo());
			setPraticaSupervisionada(obj.getPraticaSupervisionada());
			setMensagemID("msg_informe_dados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
		
	public void consultarUltimoPlanoEnsino(){
		try {
			IntStream cursoCodigos = IntStream.of(getTurma().getCurso().getCodigo());
			if (getTurma().getTurmaAgrupada()) {
				cursoCodigos = getTurma().getTurmaAgrupadaVOs().stream().mapToInt(turmaAgrupada -> turmaAgrupada.getTurma().getCurso().getCodigo());
			}
			for (Integer codigo : cursoCodigos.toArray()) {
				setPlanoEnsinoVO(getFacadeFactory().getPlanoEnsinoFacade().consultarPlanoEnsino(
						getTurma().getUnidadeEnsino().getCodigo(), codigo, getAno(), getSemestre(),  getDisciplina().getCodigo(), getTurma().getTurno().getCodigo(),
						getProfessor().getCodigo(), "AU", getUsuarioLogado()));
				if (Uteis.isAtributoPreenchido(getPlanoEnsinoVO())) {
					break;
				}
			}
			setMensagemID("msg_informe_dados", Uteis.SUCESSO);
			if (!Uteis.isAtributoPreenchido(getPlanoEnsinoVO())) {
				setMensagemDetalhada("msg_erro", "Não Existe Plano de Ensino vinculado com os campos informados.", Uteis.ERRO);
			}
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public String abrirModal() {
		return Uteis.isAtributoPreenchido(getPlanoEnsinoVO()) ? "RichFaces.$('panelPlanoDisciplina').show();" : "RichFaces.$('panelPlanoDisciplina').hide();";
	}
	
	public boolean getIsApresentarSemestreVisaoProfessorCoordenador() {
		if (getUsuarioLogado().getVisaoLogar().equals("professor") || getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			if (getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo()) {
				if (!getTurma().getCodigo().equals(0)) {
					if (getTurma().getSemestral()) {
						return true;
					} else {
						setSemestre("");
						return false;
					}
				}
				return true;
			} else {
				if (!getTurma().getCodigo().equals(0)) {
					if (!(getTurma().getSemestral())) {
						setSemestre("");
					}
				}
				return false;
			}
		}
		return true;
	}

	public void realizarLimpezaConsultaRegistroAula() {
		if (getListaConsulta() == null) {
			setListaConsulta(new ArrayList(0));
		}
		getListaConsulta().clear();
	}

	public boolean getIsConsultarPorData() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return true;
		}
		return false;
	}

	public boolean getIsNaoConsultarPorData() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return false;
		}
		return true;
	}

	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("data")) {
			return "return mascara(this.form,'formCadastro:dataConsulta','99/99/9999',event);";
		}

		return "";
	}

	public String getMascaraConsultaProfessor() {
		if (getCampoConsultaProfessor().equals("cpf")) {
			return "return mascara(this.form,'formProfessor:valorConsultarProfessor','999.999.999-99',event);";
		}
		return "";
	}

	public String getTamanhoMaximoCPF() {
		if (getCampoConsultaProfessor().equals("cpf")) {
			return "14";
		}
		return "150";
	}

	public QuadroHorarioVO getQuadroHorario() {
		if (quadroHorario == null) {
			quadroHorario = new QuadroHorarioVO();
		}
		return quadroHorario;
	}

	public void setQuadroHorario(QuadroHorarioVO quadroHorario) {
		this.quadroHorario = quadroHorario;
	}

	public HorarioProfessorDiaVO getHorarioProfessorDiaVO() {
		if (horarioProfessorDiaVO == null) {
			horarioProfessorDiaVO = new HorarioProfessorDiaVO();
		}
		return horarioProfessorDiaVO;
	}

	public void setHorarioProfessorDiaVO(HorarioProfessorDiaVO horarioProfessorDiaVO) {
		this.horarioProfessorDiaVO = horarioProfessorDiaVO;
	}

	public Integer getCodigoTurno() {
		if (codigoTurno == null) {
			codigoTurno = 0;
		}
		return codigoTurno;
	}

	public void realizarVerificarVisuaizarBotaoExcluir() {
		try {
			ControleAcesso.excluir("RegistroAula", true, getUsuarioLogado());
			setIsMostrarBotaoExcluir(true);
		} catch (Exception e) {
			setIsMostrarBotaoExcluir(false);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void setCodigoTurno(Integer codigoTurno) {
		this.codigoTurno = codigoTurno;
	}

	public Boolean getIsMostrarCalendario() {
		if (isMostrarCalendario == null) {
			isMostrarCalendario = Boolean.FALSE;
		}
		return isMostrarCalendario;
	}

	public void setIsMostrarCalendario(Boolean isMostrarCalendario) {
		this.isMostrarCalendario = isMostrarCalendario;
	}

	public Boolean getIsMostrarListagemDeDisciplinas() {
		if (isMostrarListagemDeDisciplinas == null) {
			isMostrarListagemDeDisciplinas = Boolean.FALSE;
		}
		return isMostrarListagemDeDisciplinas;
	}

	public void setIsMostrarListagemDeDisciplinas(Boolean isMostrarListagemDeDisciplinas) {
		this.isMostrarListagemDeDisciplinas = isMostrarListagemDeDisciplinas;
	}

	public List<RegistroAulaVO> getListaRegistrosAula() {
		if (listaRegistrosAula == null) {
			listaRegistrosAula = new ArrayList<RegistroAulaVO>(0);
		}
		return listaRegistrosAula;
	}

	public void setListaRegistrosAula(List<RegistroAulaVO> listaRegistrosAula) {
		this.listaRegistrosAula = listaRegistrosAula;
	}

	public String getAno() {
		if (ano == null) {
			if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				ano = getVisaoProfessorControle().getAno();
			}else if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				ano = getVisaoCoordenadorControle().getAno();
			}else {
				ano = Uteis.getAnoDataAtual4Digitos();
			}
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public Integer getCargaHoraria() {
		if (cargaHoraria == null) {
			cargaHoraria = 0;
		}
		return cargaHoraria;
	}

	public void setCargaHoraria(Integer cargaHoraria) {
		this.cargaHoraria = cargaHoraria;
	}

	public DisciplinaVO getDisciplina() {
		if (disciplina == null) {
			disciplina = new DisciplinaVO();
		}
		return disciplina;
	}

	public void setDisciplina(DisciplinaVO disciplina) {
		this.disciplina = disciplina;
	}

	public String getSemestre() {
		if (semestre == null) {			
			if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoProfessor()) {
				semestre = getVisaoProfessorControle().getSemestre();
			}else if(getLoginControle().getPermissaoAcessoMenuVO().getPermitirRegistrarAulaRetroativo() && getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				semestre = getVisaoCoordenadorControle().getSemestre();
			}else {
				semestre = Uteis.getSemestreAtual();
			}
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public String getTipoAula() {
		if (tipoAula == null) {
			tipoAula = "";
		}
		return tipoAula;
	}

	public void setTipoAula(String tipoAula) {
		this.tipoAula = tipoAula;
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

	public UsuarioVO getResponsavelRegistroAula() throws Exception {
		if (responsavelRegistroAula == null) {
			responsavelRegistroAula = getUsuarioLogado();
		}
		return responsavelRegistroAula;
	}

	public void setResponsavelRegistroAula(UsuarioVO responsavelRegistroAula) {
		this.responsavelRegistroAula = responsavelRegistroAula;
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

	public String getConteudo() {
		if (conteudo == null) {
			conteudo = "";
		}
		return conteudo;
	}

	public void setConteudo(String conteudo) {
		this.conteudo = conteudo;
	}

	public List<String> getListaHorario() {
		if (listaHorario == null) {
			listaHorario = new ArrayList<String>(0);
		}
		return listaHorario;
	}

	public void setListaHorario(List<String> listaHorario) {
		this.listaHorario = listaHorario;
	}

	public Boolean getIsMostrarComboProfessor() {
		if (getDisciplina().getCodigo().intValue() != 0) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsMostrarComboDisciplina() {
		if (getTurma().getCodigo().intValue() != 0) {
			return true;
		} else {
			return false;
		}
	}

	public Boolean getIsMostrarBotaoExcluir() {
		if (isMostrarBotaoExcluir == null) {
			isMostrarBotaoExcluir = false;
		}
		return isMostrarBotaoExcluir;
	}

	public void setIsMostrarBotaoExcluir(Boolean isMostrarBotaoExcluir) {
		this.isMostrarBotaoExcluir = isMostrarBotaoExcluir;
	}

	public void carregarTurma() {
		try {
			setQuadroHorario(new QuadroHorarioVO());
			setDisciplina(new DisciplinaVO());
			setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
			setQuadroHorario(new QuadroHorarioVO());
			setHorarioProfessorDiaVO(new HorarioProfessorDiaVO());
			setFrequenciaAulaVO(new FrequenciaAulaVO());
			setIsMostrarCalendario(false);
			setIsMostrarListagemDeDisciplinas(false);
			if(getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
				if(Uteis.isAtributoPreenchido(getTurma().getCodigo())) {
					setTurma(getFacadeFactory().getTurmaFacade().consultaRapidaPorChavePrimariaDadosBasicosTurmaAgrupada(getTurma().getCodigo(), getUsuarioLogado()));
				}else {
					limparMensagem();
					return;
				}
			}else {
				if(Uteis.isAtributoPreenchido(getTurma().getIdentificadorTurma())) {
					setTurma(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurmaEspecifico(getTurma().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo(), false, false, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				}else {
					limparMensagem();
					return;
				}
			}
			if (getTurma().getSubturma()) {
				getTurma().setCurso(getFacadeFactory().getCursoFacade().consultarCursoPorTurma(getTurma().getTurmaPrincipal(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			}
			if (getTurma().getTurmaAgrupada()) {
				if (getTurma().getSemestral()) {
					setSemestre(getSemestre());
					setAno(getAno());
				} else if (getTurma().getAnual()) {
					setSemestre("");
					setAno(getAno());
				} else {
					setSemestre("");
					setAno("");
				}
			} else {
				if (getTurma().getCurso().getPeriodicidade().equals("SE")) {
					setSemestre(getSemestre());
					setAno(getAno());
				} else if (getTurma().getCurso().getPeriodicidade().equals("AN")) {
					setSemestre("");
					setAno(getAno());
				} else {
					setSemestre("");
					setAno("");
				}
			}
			montarListaDisciplinaTurmaVisaoSecretaria();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			limparTurma();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemProfessores() {
		if (listaSelectItemProfessores == null) {
			listaSelectItemProfessores = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemProfessores;
	}

	public void setListaSelectItemProfessores(List<SelectItem> listaSelectItemProfessores) {
		this.listaSelectItemProfessores = listaSelectItemProfessores;
	}

	public boolean getIsApresentarBotaoGravar() {
		return getDisciplina().getCodigo() != 0 && !getListaRegistrosAula().isEmpty();
	}

	public boolean getIsApresentarDadosAposSelecionarTurma() {
		return getTurma().getCodigo() != 0;
	}

	public boolean getIsApresentarDadosAposSelecionarTurmaProfessor() {
		if (getTurma().getCodigo() != 0 && getProfessor().getCodigo() != 0) {
			if ((getTurma().getAnual() || getTurma().getSemestral()) && getAno().equals("")) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	public boolean getIsApresentarHorarioTurmaProfessorDia() {
		return (!getHorarioProfessorDiaVO().getHorarioProfessorDiaItemVOs().isEmpty());
	}

	public Boolean getPermiteGravarVisaoCoordenador() {
		if (permiteGravarVisaoCoordenador == null) {
			verificarPermissaoParaGravarRegistroAulaVisaoCoordenador();
		}
		return permiteGravarVisaoCoordenador;
	}

	public void setPermiteGravarVisaoCoordenador(Boolean permiteGravarVisaoCoordenador) {
		this.permiteGravarVisaoCoordenador = permiteGravarVisaoCoordenador;
	}

	public Integer getTamanhoListaAulas() {
		return getListaRegistrosAula().size();
	}

	public String getNomeColunas() {
		return "colunaMatricula,colunaNomeAluno,abono,colunaPresenca,colunaSituacao";
	}

	public Boolean getControlarMarcarDesmarcarTodos() {
		if (controlarMarcarDesmarcarTodos == null) {
			controlarMarcarDesmarcarTodos = false;
		}
		return controlarMarcarDesmarcarTodos;
	}

	public void setControlarMarcarDesmarcarTodos(Boolean controlarMarcarDesmarcarTodos) {
		this.controlarMarcarDesmarcarTodos = controlarMarcarDesmarcarTodos;
	}

	public void marcarDesmarcarTodos() {
		try {
			for (RegistroAulaVO ra : getListaRegistrosAula()) {
				getFacadeFactory().getRegistroAulaNotaFacade().marcarDesmarcarAlunoPresenteAula(getControlarMarcarDesmarcarTodos(), ra, "registroAulaForm", getUsuarioLogado());
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public boolean getLiberarRegistroAulaEntrePeriodo() {
		return getTurma().getCurso().getLiberarRegistroAulaEntrePeriodo();
	}

	public CalendarioRegistroAulaVO getCalendarioRegistroAula() {
		if (calendarioRegistroAula == null) {
			calendarioRegistroAula = new CalendarioRegistroAulaVO();
		}
		return calendarioRegistroAula;
	}

	public void setCalendarioRegistroAula(CalendarioRegistroAulaVO calendarioRegistroAula) {
		this.calendarioRegistroAula = calendarioRegistroAula;
	}

	public ConfiguracaoAcademicoVO getConfiguracaoAcademicoVO() {
		if (configuracaoAcademicoVO == null) {
			configuracaoAcademicoVO = new ConfiguracaoAcademicoVO();
		}
		return configuracaoAcademicoVO;
	}

	public void setConfiguracaoAcademicoVO(ConfiguracaoAcademicoVO configuracaoAcademicoVO) {
		this.configuracaoAcademicoVO = configuracaoAcademicoVO;
	}

	/**
	 * Método responsável por realizar a exclusão dos Registros de Aulas órfãos
	 * 
	 * @throws Exception
	 */
	public void excluirRegistroAulaOrfao() throws Exception {
		try {
			setCalendarioRegistroAula(null);
			if (!getRegistroAulaOrfaoVOs().isEmpty()) {
				getFacadeFactory().getRegistroAulaFacade().excluir(getRegistroAulaOrfaoVOs(), "Operação -> Exclusão pela Visão Administrativa", getUsuarioLogado(), Boolean.FALSE);
				for (RegistroAulaVO obj : getRegistroAulaOrfaoVOs()) {
					realizarDesmarcarDiaLancado(obj.getData());
					obj.getFrequenciaAulaVOs().clear();
				}
				setIsMostrarBotaoExcluir(false);
				setApresentarModalPermitirExcluirRegistroAulaOrfao("");
				setMensagemDetalhada("");
				setDiaSemana_Erro("");
				setMensagemID("msg_dados_excluidos");
				setRegistroAulaVO(null);
				getListaRegistrosAula().clear();
				setFrequenciaAulaVO(null);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<RegistroAulaVO> getRegistroAulaOrfaoVOs() {
		if (registroAulaOrfaoVOs == null) {
			registroAulaOrfaoVOs = new ArrayList<RegistroAulaVO>(0);
		}
		return registroAulaOrfaoVOs;
	}

	public void setRegistroAulaOrfaoVOs(List<RegistroAulaVO> registroAulaOrfaoVOs) {
		this.registroAulaOrfaoVOs = registroAulaOrfaoVOs;
	}

	public String getApresentarModalPermitirExcluirRegistroAulaOrfao() {
		if (apresentarModalPermitirExcluirRegistroAulaOrfao == null) {
			apresentarModalPermitirExcluirRegistroAulaOrfao = "";
		}
		return apresentarModalPermitirExcluirRegistroAulaOrfao;
	}

	public void setApresentarModalPermitirExcluirRegistroAulaOrfao(String apresentarModalPermitirExcluirRegistroAulaOrfao) {
		this.apresentarModalPermitirExcluirRegistroAulaOrfao = apresentarModalPermitirExcluirRegistroAulaOrfao;
	}
	
	public void limparTurma() {
		setTurma(null);
		removerObjetoMemoria(this);
		setAno(Uteis.getAnoDataAtual4Digitos());
		setSemestre(Uteis.getSemestreAtual());
		inicializarListasSelectItemTodosComboBox();
		setIsMostrarBotaoExcluir(false);
		setMensagemID("msg_entre_dados");
	}
	
	public boolean getIsApresentarCampos() {
		return Uteis.isAtributoPreenchido(getTurma());
	}
	
	public boolean getIsApresentarCampoAno() {
		return getTurma().getAnual() || getTurma().getSemestral();
	}

	public boolean getIsApresentarCampoSemestre() {
		return getTurma().getSemestral();
	}
	
	public boolean getTrazerAlunoPendenteFinanceiramente() throws Exception {
		return getFacadeFactory().getConfiguracaoGeralSistemaFacade().executarVerificacaoApresentarAlunoPendenteFinanceiramente(getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado());
	}

	public Boolean getApresentarDataMatricula() {
		if (apresentarDataMatricula == null) {
			apresentarDataMatricula = Boolean.FALSE;
		}
		return apresentarDataMatricula;
	}

	public void setApresentarDataMatricula(Boolean apresentarDataMatricula) {
		this.apresentarDataMatricula = apresentarDataMatricula;
	}

	public Boolean getTrazerAlunosTransferenciaMatriz() {
		if(trazerAlunosTransferenciaMatriz == null){
			trazerAlunosTransferenciaMatriz = false;
		}
		return trazerAlunosTransferenciaMatriz;
	}

	public void setTrazerAlunosTransferenciaMatriz(Boolean trazerAlunosTransferenciaMatriz) {
		this.trazerAlunosTransferenciaMatriz = trazerAlunosTransferenciaMatriz;
	}
	
	@PostConstruct
	public void realizarInicializacaoDadosNavegacaoPagina(){
		
	}

	public PlanoEnsinoVO getPlanoEnsinoVO() {
		if (planoEnsinoVO == null) {
			planoEnsinoVO = new PlanoEnsinoVO();
		}
		return planoEnsinoVO;
	}

	public void setPlanoEnsinoVO(PlanoEnsinoVO planoEnsinoVO) {
		this.planoEnsinoVO = planoEnsinoVO;
	}
	
	public void verificarBloquearLancamentosNotasAulasFeriadosFinaisSemana() {
		try {
			boolean feriadoNaData = false;
			boolean bloquearLancamentosNotasAulasFeriadosFinaisSemana = getConfiguracaoGeralPadraoSistema().getBloquearLancamentosNotasAulasFeriadosFinaisSemana();
			feriadoNaData = getFacadeFactory().getFeriadoFacade().verificarFeriadoNesteDia(new Date(), 0, ConsiderarFeriadoEnum.ACADEMICO, false, getUsuarioLogado());
			if (bloquearLancamentosNotasAulasFeriadosFinaisSemana && (feriadoNaData || UteisData.isFinalDeSemanaComSextaFeira(new Date(), false))) {
				setBloquearLancamentosNotasAulasFeriadosFinaisSemana(true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Boolean getBloquearLancamentosNotasAulasFeriadosFinaisSemana() {
		if (bloquearLancamentosNotasAulasFeriadosFinaisSemana == null) {
			bloquearLancamentosNotasAulasFeriadosFinaisSemana = false;
		}
		return bloquearLancamentosNotasAulasFeriadosFinaisSemana;
	}

	public void setBloquearLancamentosNotasAulasFeriadosFinaisSemana(Boolean bloquearLancamentosNotasAulasFeriadosFinaisSemana) {
		this.bloquearLancamentosNotasAulasFeriadosFinaisSemana = bloquearLancamentosNotasAulasFeriadosFinaisSemana;
	}

	public Boolean getEnviarSms() {
		if (enviarSms == null) {
			enviarSms = false;
		}
		return enviarSms;
	}

	public void setEnviarSms(Boolean enviarSms) {
		this.enviarSms = enviarSms;
	}

	public Boolean getEnviarEmail() {
		if (enviarEmail == null) {
			enviarEmail = false;
		}
		return enviarEmail;
	}

	public void setEnviarEmail(Boolean enviarEmail) {
		this.enviarEmail = enviarEmail;
	}

	public ComunicacaoInternaVO getComunicadoInternaVO() {
		if (comunicadoInternaVO == null) {
			comunicadoInternaVO = new ComunicacaoInternaVO();
		}
		return comunicadoInternaVO;
	}

	public void setComunicadoInternaVO(ComunicacaoInternaVO comunicadoInternaVO) {
		this.comunicadoInternaVO = comunicadoInternaVO;
	}
	
	

	public List<MatriculaPeriodoVO> getMatriculaPeriodoVOs() {
		if(matriculaPeriodoVOs == null){
			matriculaPeriodoVOs = new ArrayList<MatriculaPeriodoVO>(0);
		}
		return matriculaPeriodoVOs;
	}

	public void setMatriculaPeriodoVOs(List<MatriculaPeriodoVO> matriculaPeriodoVOs) {
		this.matriculaPeriodoVOs = matriculaPeriodoVOs;
	}

	public void realizarReprovacaoPeriodoLetivo(){
		try {
			setOncompleteModal("");
			executarValidacaoSimulacaoVisaoProfessor();
			getFacadeFactory().getHistoricoFacade().realizarAlteracaoHistoricoReprovadoPeriodoLetivoDeAcordoConfiguracaoAcademica(getMatriculaPeriodoVOs(), getAno(), getSemestre(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}	
	
	public void carregarConfiguracaoAcademicaCursoDaMatricula() {
		for (RegistroAulaVO registroAulaVO : getListaRegistrosAula()) { 
			for (FrequenciaAulaVO frequenciaAulaVO : registroAulaVO.getFrequenciaAulaVOs()) {
				try {
					if (!Uteis.isAtributoPreenchido(frequenciaAulaVO.getMatricula().getCurso().getConfiguracaoAcademico())) {
						frequenciaAulaVO.getMatricula().getCurso().setConfiguracaoAcademico(getFacadeFactory().getConfiguracaoAcademicoFacade().consultarPorCodigoCurso(frequenciaAulaVO.getMatricula().getCurso().getCodigo(), getUsuarioLogado()));
					}
					if (frequenciaAulaVO.getMatricula().getCurso().getConfiguracaoAcademico().getBloquearRegistroAulaAnteriorDataMatricula()) {
						Date dataRegistro = frequenciaAulaVO.getHistoricoVO().getMatriculaPeriodo().getData();
						if (frequenciaAulaVO.getHistoricoVO().getDataRegistro() != null) {
							dataRegistro = frequenciaAulaVO.getHistoricoVO().getDataRegistro();
						}
						if (dataRegistro.after(registroAulaVO.getData())) {
							frequenciaAulaVO.setBloqueadoDevidoDataMatricula(true);						
							frequenciaAulaVO.setFrequenciaOculta(true);
							frequenciaAulaVO.setPresente(false);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}
	
	public String getPraticaSupervisionada() {
		if (praticaSupervisionada == null) {
			praticaSupervisionada = "";
		}
		return praticaSupervisionada;
	}

	public void setPraticaSupervisionada(String praticaSupervisionada) {
		this.praticaSupervisionada = praticaSupervisionada;
	}
	
	private void alterarPraticaSupervisionadaListaRegistroAula(List<RegistroAulaVO> registroAulaVOs, String praticaSupervisionada) {
		registroAulaVOs.forEach(ravo -> {
			ravo.setPraticaSupervisionada(praticaSupervisionada);
		});
	}
	private Boolean permitirVisualizarFaltaOutroProfessor;
	
	public Boolean getPermitirVisualizarFaltaOutroProfessor() {
		if (permitirVisualizarFaltaOutroProfessor == null) {
			try {
				if(getUsuarioLogado().getIsApresentarVisaoCoordenador() || getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
					setPermitirVisualizarFaltaOutroProfessor(true);
				}else {
					ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("RegistrarAulaPermitirVisualizarFaltarAlunoOutroProfessor", getUsuarioLogado());
					setPermitirVisualizarFaltaOutroProfessor(true);
}
			} catch (Exception e) {
				setPermitirVisualizarFaltaOutroProfessor(false);
			}			
		}
		return permitirVisualizarFaltaOutroProfessor;
	}

	public void setPermitirVisualizarFaltaOutroProfessor(Boolean permitirVisualizarNotaFrequenciaOutroProfessor) {
		this.permitirVisualizarFaltaOutroProfessor = permitirVisualizarNotaFrequenciaOutroProfessor;
	}

		
	private MatriculaVO matriculaVO;
	
	public MatriculaVO getMatriculaVO() {
		if(matriculaVO == null) {
			matriculaVO =  new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}
	
	public void consultarFaltasAluno() {
		try {
			FrequenciaAulaVO frequenciaAulaVO = (FrequenciaAulaVO) getRequestMap().get("frequenciaAulaItens");
			setMatriculaVO(frequenciaAulaVO.getMatricula());
			setListaDetalhesMinhasFaltasVOs(getFacadeFactory().getRegistroAulaFacade().consultaRapidaFaltasAlunoTurma(getPermitirVisualizarFaltaOutroProfessor() ? 0 : getTurma().getCodigo(), getMatriculaVO().getMatricula(), getPermitirVisualizarFaltaOutroProfessor() ? 0 : getDisciplina().getCodigo(), getTurma().getSemestral() ? getSemestre() : "", getTurma().getIntegral() ? "" : getAno(), false, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados", Uteis.ALERTA);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
			
	}
	
	private List<RegistroAulaVO> listaDetalhesMinhasFaltasVOs;
	
	/**
	 * @return the listaDetalhesMinhasFaltasVOs
	 */
	public List<RegistroAulaVO> getListaDetalhesMinhasFaltasVOs() {
		if (listaDetalhesMinhasFaltasVOs == null) {
			listaDetalhesMinhasFaltasVOs = new ArrayList<RegistroAulaVO>();
		}
		return listaDetalhesMinhasFaltasVOs;
	}

	/**
	 * @param listaDetalhesMinhasFaltasVOs
	 *            the listaDetalhesMinhasFaltasVOs to set
	 */
	public void setListaDetalhesMinhasFaltasVOs(List<RegistroAulaVO> listaDetalhesMinhasFaltasVOs) {
		this.listaDetalhesMinhasFaltasVOs = listaDetalhesMinhasFaltasVOs;
	}

	public boolean renderizarDadosAluno(int row) {		
		return row == 0 || ( getTam() != 0 && (row%getTam()) == 0); 		
	} 
	
	public boolean isOcultarHorarioAulaVisaoProfessor() {
		if(getTurma() != null && getTurma().getTurno() != null && getTurma().getTurno().getCodigo() != null) {
			return getTurma().getTurno().getOcultarHorarioAulaVisaoProfessor();
		}
		return false;
	}

	@PostConstruct
	public void inicializarDados() {
		try {
		setIsMostrarBotaoExcluir(false);
		if (getUsuarioLogado().getIsApresentarVisaoAdministrativa()) {
			novo();
			if (context().getExternalContext().getSessionMap().containsKey("registroAula.turma")) {
				try{
					inicializarDadosTurma((Integer) context().getExternalContext().getSessionMap().get("registroAula.turma"));
					context().getExternalContext().getSessionMap().remove("registroAula.turma");
					setAno((String) context().getExternalContext().getSessionMap().get("registroAula.ano"));
					context().getExternalContext().getSessionMap().remove("registroAula.ano");
					setSemestre((String) context().getExternalContext().getSessionMap().get("registroAula.semestre"));
					context().getExternalContext().getSessionMap().remove("registroAula.semestre");
					if (context().getExternalContext().getSessionMap().containsKey("registroAula.disciplina")) {
						getDisciplina().setCodigo((Integer)context().getExternalContext().getSessionMap().get("registroAula.disciplina"));					
						context().getExternalContext().getSessionMap().remove("registroAula.disciplina");
						montarListaProfessoresTurmaDisciplina();
					}
				}catch(Exception e){
					setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
				}
			}
		}else		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			novoVisaoProfessor();
		}else		if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			novoVisaoCoordenador();
		}
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void montarListaProfessores() {
		try {
			montarListaProfessoresTurmaDisciplina();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
		
	private void montarCalendarioAulas(PessoaVO pessoaVO) {
		if (!Uteis.isAtributoPreenchido(getProfessor())) {
			setProfessor(pessoaVO);
			montarCalendarioDeAulasVisaoSecretaria();
		}
	}
	
	private SelectItem montarSelectItemProfessor(PessoaVO pessoaVO) {
		return new SelectItem(pessoaVO.getCodigo(), pessoaVO.getNome());
	}
	
	private void limparCamposCalendarioAulas() {
		setIndex(0);
		setQuadroHorario(new QuadroHorarioVO());
		setListaRegistrosAula(new ArrayList<RegistroAulaVO>(0));
		setHorarioProfessorDiaVO(new HorarioProfessorDiaVO());
		setFrequenciaAulaVO(new FrequenciaAulaVO());
		setMensagemID("");
		setMensagem("");
		setIsMostrarCalendario(false);
		setIsMostrarListagemDeDisciplinas(false);
	}
}
