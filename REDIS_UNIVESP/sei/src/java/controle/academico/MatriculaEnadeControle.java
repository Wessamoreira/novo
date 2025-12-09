package controle.academico;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.EnadeVO;
import negocio.comuns.academico.MatriculaEnadeVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoEnadeVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.diplomaDigital.versao1_05.TEnumCondicaoEnade;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

@Controller("MatriculaEnadeControle")
@Scope("viewScope")
@Lazy
public class MatriculaEnadeControle extends SuperControle implements Serializable {

	private MatriculaVO matriculaVO;
	private MatriculaVO matriculaSelecionadaVO;
	private MatriculaEnadeVO matriculaEnadeVO;
	private TurmaVO turmaVO;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private String campoConsultaEnade;
	private String valorConsultaEnade;
	private List<EnadeVO> listaConsultaEnade;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private List<MatriculaVO> listaConsultaAluno;
	private String campoConsultaAluno;
	private String valorConsultaAluno;
	private List<MatriculaVO> listaMatriculaVOs;
	private List<MatriculaEnadeVO> listaMatriculaEnadeVOs;
	private Boolean apresentarTelaMatricula;
	private Date valorConsultaData;
	private List<SelectItem> listaSelectItemTextoEnade;
	private String unidadeEnsinoApresentar;
	private String cursoApresentar;
	private String turnoApresentar;
	private Boolean marcarTodosCursos;
	private Boolean marcarTodosTurnos;
	private String periodoLetivoInicial;
	private String periodoLetivoFinal;
	private String ano;
	private String abaNavegar;
	private FileUploadEvent fileUploadEvent;
	private ArquivoVO arquivoEnadeVO ;
	private List<MatriculaEnadeVO> listaMatriculaArquivoEnadeVOs;
	private List<MatriculaEnadeVO> listaMatriculaArquivoEnadeErroVOs;
	private ProgressBarVO progressBarVO;
	private Boolean apresentarBotaoUploadArquivoEnade ;
	private Boolean apresentarBotaoProcessarArquivoEnade ;
	private Boolean apresentarResultadoArquivoMatriculaEnade;
	private CursoVO cursoVOFiltrarArquivoMatriculaEnade;
	private List<SelectItem> listaSelectItemCondicaoEnade;
	
	private static final long serialVersionUID = 1L;

	public MatriculaEnadeControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		if (context().getExternalContext().getSessionMap().get("matricula") != null) {
			setMatriculaVO((MatriculaVO) context().getExternalContext().getSessionMap().get("matricula"));
			context().getExternalContext().getSessionMap().remove("matricula");
		}
		setMensagemID("msg_entre_prmconsulta");
	}
	
	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {			
			consultarUnidadeEnsinoFiltroRelatorio("MatriculaEnade");
			verificarTodasUnidadesSelecionadas();
			if (!getMatriculaVO().getMatricula().isEmpty()) {
				consultarAlunoPorMatricula();
				consultar();
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
	}

	public void limparCurso() {
		try {
			setCursoApresentar(null);
			setMarcarTodosCursos(false);
			marcarTodosCursosAction();
			getListaMatriculaVOs().clear();
			getListaMatriculaEnadeVOs().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparTurno() {
		try {
			setTurnoApresentar(null);
			setMarcarTodosTurnos(false);
			marcarTodosTurnosAction();
			getListaMatriculaVOs().clear();
			getListaMatriculaEnadeVOs().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void limparTurma() throws Exception {
		try {
			setTurmaVO(null);
			getListaMatriculaVOs().clear();
			getListaMatriculaEnadeVOs().clear();
		} catch (Exception e) {
		}
	}

	public void limparDadosAluno() throws Exception {
		try {
			setMatriculaVO(null);
			getListaMatriculaVOs().clear();
			getListaMatriculaEnadeVOs().clear();
		} catch (Exception e) {
		}
	}

	public void limparDadosEnade() throws Exception {
		try {
			getMatriculaEnadeVO().setEnadeVO(null);
		} catch (Exception e) {
		}
	}

	public void preencherTodosListaAluno() {
		getFacadeFactory().getMatriculaEnadeFacade().preencherTodosListaAluno(getListaMatriculaVOs());
	}

	public void desmarcarTodosListaAluno() {
		getFacadeFactory().getMatriculaEnadeFacade().desmarcarTodosListaAluno(getListaMatriculaVOs());
	}

	@Override
	public String consultar() {
		try {
			getListaMatriculaVOs().clear();
			setListaMatriculaVOs(getFacadeFactory().getMatriculaEnadeFacade().consultar(getMatriculaVO().getMatricula(), getCursoVOs(), getTurmaVO().getCodigo(), getUnidadeEnsinoVOs(), getTurnoVOs(), getPeriodoLetivoInicial(), getPeriodoLetivoFinal(), getAno(),getFiltroRelatorioAcademicoVO(),"", getUsuarioLogado()));
			setListaMatriculaEnadeVOs(getFacadeFactory().getMatriculaEnadeFacade().consultarMatriculaEnadePorMatricula(getMatriculaVO().getMatricula(), getUsuarioLogado(), false));
			setMensagemID("msg_dados_consultados");
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void editar() {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		setMatriculaSelecionadaVO(new MatriculaVO());
		setMatriculaSelecionadaVO(obj);
		limparMensagem();

	}

	public void removerMatriculaEnade() {
		MatriculaEnadeVO obj = (MatriculaEnadeVO) context().getExternalContext().getRequestMap().get("matriculaEnadeItens");
		getFacadeFactory().getMatriculaEnadeFacade().removerMatriculaEnade(getMatriculaSelecionadaVO(), obj);
	}

	public void removerEnade() {
		MatriculaEnadeVO obj = (MatriculaEnadeVO) context().getExternalContext().getRequestMap().get("enadeItens");
		getFacadeFactory().getMatriculaEnadeFacade().removerEnade(obj, getListaMatriculaEnadeVOs());
	}

	public void adicionarMatriculaEnade() {
		try {
			getFacadeFactory().getMatriculaEnadeFacade().adicionarObjMatriculaEnadeVOs(getMatriculaEnadeVO(), listaMatriculaEnadeVOs);
			setMatriculaEnadeVO(new MatriculaEnadeVO());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void persistir() {
		try {
			getFacadeFactory().getMatriculaEnadeFacade().persistir(getListaMatriculaVOs(), getListaMatriculaEnadeVOs(),	getListaMatriculaArquivoEnadeVOs(), getUsuarioLogado());
			setListaMatriculaArquivoEnadeErroVOs(new ArrayList<MatriculaEnadeVO>(0));
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void persistirMatriculaEnade() {
		try {
			getFacadeFactory().getMatriculaEnadeFacade().alterarMatriculaEnade(getMatriculaSelecionadaVO(), getMatriculaSelecionadaVO().getMatriculaEnadeVOs(), getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

//	public void consultarTurma() {
//		try {
//			if (getUnidadeEnsinoVOs().isEmpty()) {
//				throw new Exception(getMensagemInternalizacao("msg_MatriculaEndade_unidadeEnsinoVOs_vazio"));
//			}
//			getFacadeFactory().getTurmaFacade().consultarTurma(getControleConsultaTurma(), null, getUnidadeEnsinoVOs(), null, getCursoVOs(), null, getTurnoVOs(), null, null, getUsuarioLogado());
//			
//			setMensagemID("msg_dados_consultados");
//		} catch (Exception e) {
//			setListaConsulta(new ArrayList<TurmaVO>(0));
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
//	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			setTurmaVO(obj);
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
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getCampoConsultaAluno().equals("nome")) {
				if (getTurmaVO().getCodigo() != 0) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoaCursoTurma(getValorConsultaAluno(), getUnidadeEnsinoVOs(), null, getTurmaVO().getCodigo(), false, getUsuarioLogado());
				} else if (getCursoVOs().stream().anyMatch(CursoVO::getFiltrarCursoVO)) {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoVOs(), getCursoVOs(), false, getUsuarioLogado());
				} else {
					objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), getUnidadeEnsinoVOs(), false, getUsuarioLogado());
				}
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String consultarEnade() {
		try {
			super.consultar();
			List<EnadeVO> objs = new ArrayList<EnadeVO>(0);
			if (getCampoConsultaEnade().equals("codigo")) {
				if (getValorConsultaEnade().equals("")) {
					setValorConsultaEnade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaEnade());
				objs = getFacadeFactory().getEnadeFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaEnade().equals("tituloEnade")) {
				objs = getFacadeFactory().getEnadeFacade().consultarPorTituloEnade(getValorConsultaEnade(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultaEnade().equals("dataPublicacaoPortariaDOU")) {

				objs = getFacadeFactory().getEnadeFacade().consultarPorDataPublicacaoPortariaDOU(Uteis.getDateTime(getValorConsultaData(), 0, 0, 0), Uteis.getDateTime(getValorConsultaData(), 23, 59, 59), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultaEnade(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsultaEnade(new ArrayList<EnadeVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	private List<SelectItem> tipoConsultaComboEnade;
	
	public List<SelectItem> getTipoConsultaComboEnade() {
		if (tipoConsultaComboEnade == null) {
			tipoConsultaComboEnade = new ArrayList<SelectItem>(0);
			tipoConsultaComboEnade.add(new SelectItem("tituloEnade", "Titulo Enade"));
			tipoConsultaComboEnade.add(new SelectItem("codigo", "Número"));
			tipoConsultaComboEnade.add(new SelectItem("dataPublicacaoPortariaDOU", "Data Publicacao no Diário Oficial"));
		}
		return tipoConsultaComboEnade;
	}

	public void consultarAlunoPorMatricula() {
		MatriculaVO objAluno = null;
		try {
			objAluno = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatriculaListaUnidadeEnsinoVOs(getMatriculaVO().getMatricula(), getUnidadeEnsinoVOs(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			setCampoConsultaAluno(null);
			setValorConsultaAluno(null);
			getListaConsultaAluno().clear();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {

		}
	}

	public void selecionarAluno() throws Exception {
		MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
		setMatriculaVO(obj);
		valorConsultaAluno = "";
		campoConsultaAluno = "";
		getListaConsultaAluno().clear();
	}

	public void selecionarEnade() {
		EnadeVO enadeVO = (EnadeVO) context().getExternalContext().getRequestMap().get("enadeItens");
		getMatriculaEnadeVO().setEnadeVO(enadeVO);
		montarListaSelectItemTextoEnade();
	}

	public void montarListaSelectItemTextoEnade() {
		try {
			List<TextoEnadeVO> textoEnadeVOs = getFacadeFactory().getTextoEnadeFacade().consultarPorEnade(getMatriculaEnadeVO().getEnadeVO().getCodigo());
			getListaSelectItemTextoEnade().clear();
			for (TextoEnadeVO texto : textoEnadeVOs) {
				getListaSelectItemTextoEnade().add(new SelectItem(texto.getCodigo(), texto.getTexto()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", "Não foi encontrado nenhum texto cadastrado para este enade.");
		}

	}

	public void avancarTelaEnade() {
		try {
			getFacadeFactory().getMatriculaEnadeFacade().validarDadosAvancarTelaEnade(getListaMatriculaVOs());
			getListaMatriculaEnadeVOs().clear();
			setApresentarTelaMatricula(Boolean.FALSE);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void voltarTelaMatricula() {
		setApresentarTelaMatricula(Boolean.TRUE);
	}
	
	private static List<SelectItem> listaTipoConsultaPorNome = Arrays.asList(new SelectItem("nome", "Nome"));
	
	private List<SelectItem> tipoConsultaComboAluno;

	public List<SelectItem> getTipoConsultaComboAluno() {
		if (tipoConsultaComboAluno == null) {
			tipoConsultaComboAluno = listaTipoConsultaPorNome;
		}
		return tipoConsultaComboAluno;
	}

	private List<SelectItem> tipoConsultaComboCurso;
	
	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = listaTipoConsultaPorNome;
		}
		return tipoConsultaComboCurso;
	}

	private List<SelectItem> tipoConsultaComboTipo;
	
	public List<SelectItem> getTipoConsultaComboTipo() {
		if (tipoConsultaComboTipo == null) {
			tipoConsultaComboTipo = new ArrayList<>(0);
			tipoConsultaComboTipo.add(new SelectItem("aluno", "Aluno"));
			tipoConsultaComboTipo.add(new SelectItem("cursoTurma", "Curso/Turma"));
		}
		return tipoConsultaComboTipo;
	}

	private List<SelectItem> tipoConsultaComboTurma;
	
	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<>(0);
			tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
			tipoConsultaComboTurma.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
			tipoConsultaComboTurma.add(new SelectItem("nomeTurno", "Turno"));
			tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
		}
		return tipoConsultaComboTurma;
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

	public TurmaVO getTurmaVO() {
		if (turmaVO == null) {
			turmaVO = new TurmaVO();
		}
		return turmaVO;
	}

	public void setTurmaVO(TurmaVO turmaVO) {
		this.turmaVO = turmaVO;
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>(0);
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
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

	public List<MatriculaVO> getListaMatriculaVOs() {
		if (listaMatriculaVOs == null) {
			listaMatriculaVOs = new ArrayList<MatriculaVO>(0);
		}
		return listaMatriculaVOs;
	}

	public void setListaMatriculaVOs(List<MatriculaVO> listaMatriculaVOs) {
		this.listaMatriculaVOs = listaMatriculaVOs;
	}

	public MatriculaEnadeVO getMatriculaEnadeVO() {
		if (matriculaEnadeVO == null) {
			matriculaEnadeVO = new MatriculaEnadeVO();
		}
		return matriculaEnadeVO;
	}

	public void setMatriculaEnadeVO(MatriculaEnadeVO matriculaEnadeVO) {
		this.matriculaEnadeVO = matriculaEnadeVO;
	}

	public List<MatriculaEnadeVO> getListaMatriculaEnadeVOs() {
		if (listaMatriculaEnadeVOs == null) {
			listaMatriculaEnadeVOs = new ArrayList<MatriculaEnadeVO>(0);
		}
		return listaMatriculaEnadeVOs;
	}

	public void setListaMatriculaEnadeVOs(List<MatriculaEnadeVO> listaMatriculaEnadeVOs) {
		this.listaMatriculaEnadeVOs = listaMatriculaEnadeVOs;
	}

	public Boolean getApresentarTelaMatricula() {
		if (apresentarTelaMatricula == null) {
			apresentarTelaMatricula = Boolean.TRUE;
		}
		return apresentarTelaMatricula;
	}

	public void setApresentarTelaMatricula(Boolean apresentarTelaMatricula) {
		this.apresentarTelaMatricula = apresentarTelaMatricula;
	}

	public String getCampoConsultaEnade() {
		if (campoConsultaEnade == null) {
			campoConsultaEnade = "";
		}
		return campoConsultaEnade;
	}

	public void setCampoConsultaEnade(String campoConsultaEnade) {
		this.campoConsultaEnade = campoConsultaEnade;
	}

	public String getValorConsultaEnade() {
		if (valorConsultaEnade == null) {
			valorConsultaEnade = "";
		}
		return valorConsultaEnade;
	}

	public void setValorConsultaEnade(String valorConsultaEnade) {
		this.valorConsultaEnade = valorConsultaEnade;
	}

	public List<EnadeVO> getListaConsultaEnade() {
		if (listaConsultaEnade == null) {
			listaConsultaEnade = new ArrayList<EnadeVO>(0);
		}
		return listaConsultaEnade;
	}

	public void setListaConsultaEnade(List<EnadeVO> listaConsultaEnade) {
		this.listaConsultaEnade = listaConsultaEnade;
	}

	public Date getValorConsultaData() {
		if (valorConsultaData == null) {
			valorConsultaData = new Date();
		}
		return valorConsultaData;
	}

	public void setValorConsultaData(Date valorConsultaData) {
		this.valorConsultaData = valorConsultaData;
	}

	public Boolean getApresentarCampoData() {
		if (getCampoConsultaEnade().equals("dataPublicacaoPortariaDOU")) {
			return true;
		}
		return false;
	}

	public List<SelectItem> getListaSelectItemTextoEnade() {
		if (listaSelectItemTextoEnade == null) {
			listaSelectItemTextoEnade = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemTextoEnade;
	}

	public void setListaSelectItemTextoEnade(List<SelectItem> listaSelectItemTextoEnade) {
		this.listaSelectItemTextoEnade = listaSelectItemTextoEnade;
	}
	
	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			unidade.setFiltrarUnidadeEnsino(getMarcarTodasUnidadeEnsino());
		}
		verificarTodasUnidadesSelecionadas();
	}
	
	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>(0);
		if (getUnidadeEnsinoVOs().size() >= 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome().trim()).append("; ");
					unidadeEnsinoVOs.add(obj);
				}
			}
			consultarCurso(unidadeEnsinoVOs);
			setUnidadeEnsinoApresentar(unidade.toString());
			verificarTodosCursosSelecionados();
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					setUnidadeEnsinoApresentar(getUnidadeEnsinoVOs().get(0).getNome());
				}
			} else {
				setUnidadeEnsinoApresentar(unidade.toString());
			}
		}
	}
	
	public void consultarCurso(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		try {
			if (unidadeEnsinoVOs.isEmpty()) {
				return;
			}
			List<CursoVO> cursoVOs = getFacadeFactory().getCursoFacade().consultarCursoPorNivelEducacionalEUnidadeEnsinoVOs("SU', 'GT", unidadeEnsinoVOs, getUsuarioLogado());
			if (getCursoVOs().isEmpty()) {
				setCursoVOs(cursoVOs);
			} else {
				for (CursoVO obj : cursoVOs) {
					if (!getCursoVOs().contains(obj)) {
						getCursoVOs().add(obj);
					}
				}
				for (Iterator<CursoVO> iterator = getCursoVOs().iterator(); iterator.hasNext();) {
					CursoVO cursoVO = (CursoVO) iterator.next();
					if (!cursoVOs.contains(cursoVO)) {
						iterator.remove();
					}
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarTurno(List<CursoVO> cursos) {
		try {
			if (cursos.isEmpty()) {
				return;
			}
			List<TurnoVO> turnos = getFacadeFactory().getTurnoFacade().consultarPorCodigoCursos(cursos, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (getCursoVOs().isEmpty()) {
				setTurnoVOs(turnos);
			} else {
				for (TurnoVO obj : turnos) {
					if (!getTurnoVOs().contains(obj)) {
						getTurnoVOs().add(obj);
					}
				}
				for (Iterator<TurnoVO> iterator = getTurnoVOs().iterator(); iterator.hasNext();) {
					TurnoVO turno = (TurnoVO) iterator.next();
					if (!turnos.contains(turno)) {
						iterator.remove();
					}
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getUnidadeEnsinoApresentar() {
		if (unidadeEnsinoApresentar == null) {
			unidadeEnsinoApresentar = "";
		}
		return unidadeEnsinoApresentar;
	}

	public void setUnidadeEnsinoApresentar(String unidadeEnsinoApresentar) {
		this.unidadeEnsinoApresentar = unidadeEnsinoApresentar;
	}
	
	public String getCursoApresentar() {
		if (cursoApresentar == null) {
			cursoApresentar = "";
		}
		return cursoApresentar;
	}

	public void setCursoApresentar(String cursoApresentar) {
		this.cursoApresentar = cursoApresentar;
	}

	public String getTurnoApresentar() {
		if (turnoApresentar == null) {
			turnoApresentar = "";
		}
		return turnoApresentar;
	}

	public void setTurnoApresentar(String turnoApresentar) {
		this.turnoApresentar = turnoApresentar;
	}
	
	public void marcarTodosCursosAction() throws Exception {
		for (CursoVO cursoVO : getCursoVOs()) {
			cursoVO.setFiltrarCursoVO(getMarcarTodosCursos());
		}
		verificarTodosCursosSelecionados();
	}
	
	public void verificarTodosCursosSelecionados() {
		StringBuilder curso = new StringBuilder();
		for (CursoVO obj : getCursoVOs()) {
			if (obj.getFiltrarCursoVO()) {
				curso.append(obj.getCodigo()).append(" - ");
				curso.append(obj.getNome()).append("; ");				
			}
		}
		setCursoApresentar(curso.toString());
		consultarTurno(getCursoVOs());
		verificarTodosTurnosSelecionados();
	}
	
	public Boolean getMarcarTodosCursos() {
		if (marcarTodosCursos == null) {
			marcarTodosCursos = false;
		}
		return marcarTodosCursos;
	}

	public void setMarcarTodosCursos(Boolean marcarTodosCursos) {
		this.marcarTodosCursos = marcarTodosCursos;
	}
	
	public Boolean getMarcarTodosTurnos() {
		if (marcarTodosTurnos == null) {
			marcarTodosTurnos = false;
		}
		return marcarTodosTurnos;
	}

	public void setMarcarTodosTurnos(Boolean marcarTodosTurnos) {
		this.marcarTodosTurnos = marcarTodosTurnos;
	}
	
	public void verificarTodosTurnosSelecionados() {
		boolean existeTurnoSelecionado = false;
		StringBuilder turno = new StringBuilder();
		if (getTurnoVOs().size() > 1) {
			for (TurnoVO obj : getTurnoVOs()) {
				if (obj.getFiltrarTurnoVO()) {
					turno.append(obj.getNome()).append("; ");
					existeTurnoSelecionado = true;
				}
			}
			setTurnoApresentar(turno.toString());
		} else {
			if (!getTurnoVOs().isEmpty()) {
				if (getTurnoVOs().get(0).getFiltrarTurnoVO()) {
					setTurnoApresentar(getTurnoVOs().get(0).getNome());
					existeTurnoSelecionado = true;
				}
			} else {
				setTurnoApresentar(turno.toString());
			}
		}
		if (!existeTurnoSelecionado) {
			setTurnoApresentar(turno.toString());
		}
	}
	
	public void marcarTodosTurnosAction() {
		for (TurnoVO turnoVO : getTurnoVOs()) {
			turnoVO.setFiltrarTurnoVO(getMarcarTodosTurnos());
		}
		verificarTodosTurnosSelecionados();
	}

	public String getPeriodoLetivoInicial() {
		if (periodoLetivoInicial == null) {
			periodoLetivoInicial = "";
		}
		return periodoLetivoInicial;
	}

	public void setPeriodoLetivoInicial(String periodoLetivoInicial) {
		this.periodoLetivoInicial = periodoLetivoInicial;
	}

	public String getPeriodoLetivoFinal() {
		if (periodoLetivoFinal == null) {
			periodoLetivoFinal = "";
		}
		return periodoLetivoFinal;
	}

	public void setPeriodoLetivoFinal(String periodoLetivoFinal) {
		this.periodoLetivoFinal = periodoLetivoFinal;
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
	
	public Integer getColumnCursoVO() throws Exception {
		if (getCursoVOs().size() > 4) {
			return 4;
		}
		return getCursoVOs().size();
	}

	public Integer getElementCursoVO() throws Exception {
		return getCursoVOs().size();
	}

	public Integer getColumnTurnoVO() throws Exception {
		if (getTurnoVOs().size() > 4) {
			return 4;
		}
		return getTurnoVOs().size();
	}

	public Integer getElementTurnoVO() throws Exception {
		return getTurnoVOs().size();
	}

	public MatriculaVO getMatriculaSelecionadaVO() {
		if (matriculaSelecionadaVO == null) {
			matriculaSelecionadaVO = new MatriculaVO();
		}
		return matriculaSelecionadaVO;
	}

	public void setMatriculaSelecionadaVO(MatriculaVO matriculaSelecionadaVO) {
		this.matriculaSelecionadaVO = matriculaSelecionadaVO;
	}
	
	
	public String getAbaNavegar() {
		if (abaNavegar == null) {
			abaNavegar = "tabDadosMatricula";
		}
		return abaNavegar;
	}

	public void setAbaNavegar(String abaNavegar) {
		this.abaNavegar = abaNavegar;
	}
	
	public FileUploadEvent getFileUploadEvent() {
		return fileUploadEvent;
	}

	public void setFileUploadEvent(FileUploadEvent fileUploadEvent) {
		this.fileUploadEvent = fileUploadEvent;
	}
	
	
	

	public ArquivoVO getArquivoEnadeVO() {
		if(arquivoEnadeVO == null ) {
			arquivoEnadeVO = new ArquivoVO();
		}
		return arquivoEnadeVO;
	}

	public void setArquivoEnadeVO(ArquivoVO arquivoEnadeVO) {
		this.arquivoEnadeVO = arquivoEnadeVO;
	}

	public List<MatriculaEnadeVO> getListaMatriculaArquivoEnadeVOs() {
		if(listaMatriculaArquivoEnadeVOs == null ) {			
		   listaMatriculaArquivoEnadeVOs = new ArrayList<MatriculaEnadeVO>(0);
		}
		return listaMatriculaArquivoEnadeVOs;
	}

	public void setListaMatriculaArquivoEnadeVOs(List<MatriculaEnadeVO> listaMatriculaArquivoEnadeVOs) {
		this.listaMatriculaArquivoEnadeVOs = listaMatriculaArquivoEnadeVOs;
	}

	public List<MatriculaEnadeVO> getListaMatriculaArquivoEnadeErroVOs() {
		if(listaMatriculaArquivoEnadeErroVOs == null ) {
			listaMatriculaArquivoEnadeErroVOs = new ArrayList<MatriculaEnadeVO>(0);
		}
		return listaMatriculaArquivoEnadeErroVOs;
	}

	public void setListaMatriculaArquivoEnadeErroVOs(List<MatriculaEnadeVO> listaMatriculaArquivoEnadeErroVOs) {
		this.listaMatriculaArquivoEnadeErroVOs = listaMatriculaArquivoEnadeErroVOs;
	}
	
	public ProgressBarVO getProgressBarVO() {
		if(progressBarVO == null) {
			progressBarVO = new ProgressBarVO();
		}
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}
	

	public void navegarAba(String aba) {		
		setAbaNavegar(aba);	
		setApresentarTelaMatricula(getAbaNavegar().equals("tabDadosMatricula"));
		
	}
	
	public void inicializarProcessoImportarAquivoEnade(FileUploadEvent uploadEvent) {		
		
		if (uploadEvent.getUploadedFile() != null) {	
			setProgressBarVO(new ProgressBarVO());
			limparMensagem();
			getProgressBarVO().setUsuarioVO(getUsuarioLogado());
			getProgressBarVO().setConfiguracaoGeralSistemaVO(getConfiguracaoGeralPadraoSistema());
			setFileUploadEvent(uploadEvent);			
			try {
				getFacadeFactory().getMatriculaEnadeFacade().inicializarDadosArquivoMatriculaEnade(getArquivoEnadeVO(), uploadEvent, getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
				liberarProcessarAquivoEnade();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
				e.printStackTrace();
			}

		} else {			
			setMensagemDetalhada("msg_erro", "Selecione um arquivo para a prosseguir com a importação.");
		}
	}
	
	
	public void realizarProcessamentoArquivoExcelMatriculaEnade() {
		try {	
			if(getFileUploadEvent() == null) {
				throw new Exception("Necessário anexar Arquivo.");
			}
			getFacadeFactory().getMatriculaEnadeFacade().realizarProcessamentoArquivoExcelMatriculaEnade(
					getFileUploadEvent(), 
					getArquivoEnadeVO().getNome(), 
					getListaMatriculaVOs(),
					getListaMatriculaArquivoEnadeVOs()  , 
					getListaMatriculaArquivoEnadeErroVOs(), 
					getCursoVOFiltrarArquivoMatriculaEnade() , 
					getProgressBarVO().getConfiguracaoGeralSistemaVO(),
					false,
					getProgressBarVO(), 
					getProgressBarVO().getUsuarioVO());	
			
			setApresentarResultadoArquivoMatriculaEnade(!getListaMatriculaArquivoEnadeVOs().isEmpty() || getListaMatriculaArquivoEnadeErroVOs().isEmpty());
			if(getApresentarResultadoArquivoMatriculaEnade()) {
				setMensagemID("msg_dados_importados");		
			} else {
				setMensagemID("Não foram encontrados resultados com o arquivo informado");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		
		}
		
	}
		
	
		
		
		public void verificarTodosCursosSelecionadosArquivoEnade() {
			CursoVO cursoVO = (CursoVO)  context().getExternalContext().getRequestMap().get("cursoItens");
			Boolean filtarCurso = cursoVO.getFiltrarCursoVO();
			iniciarlizarDadosProcessarArquivoEnade();
			if(filtarCurso) {			
				StringBuilder curso = new StringBuilder();			
				setApresentarBotaoProcessarArquivoEnade(Boolean.FALSE);
				setApresentarBotaoUploadArquivoEnade(Boolean.FALSE);
				setApresentarResultadoArquivoMatriculaEnade(Boolean.FALSE);			
				for (CursoVO obj : getCursoVOs()) {
					if (obj.getCodigo().equals(cursoVO.getCodigo())) {
						obj.setFiltrarCursoVO(Boolean.TRUE);
						curso.append(obj.getCodigo()).append(" - ");
						curso.append(obj.getNome()).append("; ");
						setApresentarBotaoUploadArquivoEnade(Boolean.TRUE);
					}
				}						
				setCursoApresentar(curso.toString());
				setCursoVOFiltrarArquivoMatriculaEnade(cursoVO);
			}
			
		}
		
	
		
		public Boolean getApresentarBotaoUploadArquivoEnade() {
			if(apresentarBotaoUploadArquivoEnade == null) {
				apresentarBotaoUploadArquivoEnade = Boolean.FALSE;
			}
			return apresentarBotaoUploadArquivoEnade;
		}
		
		public void setApresentarBotaoUploadArquivoEnade(Boolean apresentarBotaoUploadArquivoEnade) {
		   this.apresentarBotaoUploadArquivoEnade = apresentarBotaoUploadArquivoEnade ;
		}
		
		
		public Boolean getApresentarBotaoProcessarArquivoEnade() {
			if(apresentarBotaoProcessarArquivoEnade == null) {
				apresentarBotaoProcessarArquivoEnade = Boolean.FALSE;
			}
			return apresentarBotaoProcessarArquivoEnade;
		}
		
		public void setApresentarBotaoProcessarArquivoEnade(Boolean apresentarBotaoProcessarArquivoEnade) {
		   this.apresentarBotaoProcessarArquivoEnade = apresentarBotaoProcessarArquivoEnade ;
		}		
		
		public void liberarProcessarAquivoEnade() {
			setApresentarBotaoProcessarArquivoEnade(Boolean.TRUE);
			
		}

		public Boolean getApresentarResultadoArquivoMatriculaEnade() {
			if(apresentarResultadoArquivoMatriculaEnade == null ) {
				apresentarResultadoArquivoMatriculaEnade = Boolean.FALSE;
			}
			return apresentarResultadoArquivoMatriculaEnade;
		}

		public void setApresentarResultadoArquivoMatriculaEnade(Boolean apresentarResultadoArquivoMatriculaEnade) {
			this.apresentarResultadoArquivoMatriculaEnade = apresentarResultadoArquivoMatriculaEnade;
		}


		public void iniciarlizarDadosProcessarArquivoEnade() {
			
			for (CursoVO cursoVO : getCursoVOs()) {
				cursoVO.setFiltrarCursoVO(Boolean.FALSE);
			}
			setApresentarBotaoUploadArquivoEnade(Boolean.FALSE);
			setApresentarResultadoArquivoMatriculaEnade(Boolean.FALSE);	
			setApresentarBotaoProcessarArquivoEnade(Boolean.FALSE);
			setMarcarTodosCursos(Boolean.FALSE);
			setCursoApresentar("");			
			getListaMatriculaVOs().clear();
			getListaMatriculaArquivoEnadeVOs().clear(); 
			getListaMatriculaArquivoEnadeErroVOs().clear();
			setCursoVOFiltrarArquivoMatriculaEnade(null);
			setFileUploadEvent(null);
			setArquivoEnadeVO(null);
			limparMensagem();
			getArquivoEnadeVO().setNome("");
		}
		
		
		
		public CursoVO getCursoVOFiltrarArquivoMatriculaEnade() {
			if(cursoVOFiltrarArquivoMatriculaEnade== null) {
				cursoVOFiltrarArquivoMatriculaEnade = new CursoVO();
			}
			return cursoVOFiltrarArquivoMatriculaEnade;
		}

		public void setCursoVOFiltrarArquivoMatriculaEnade(CursoVO cursoVOFiltrarArquivoMatriculaEnade) {
			this.cursoVOFiltrarArquivoMatriculaEnade = cursoVOFiltrarArquivoMatriculaEnade;
		}
		
		public void limparDadosArquivo() {			
			setApresentarResultadoArquivoMatriculaEnade(Boolean.FALSE);	
			setApresentarBotaoProcessarArquivoEnade(Boolean.FALSE);			
			getListaMatriculaVOs().clear();
			getListaMatriculaArquivoEnadeVOs().clear(); 
			getListaMatriculaArquivoEnadeErroVOs().clear();
			setFileUploadEvent(null);
			setArquivoEnadeVO(null);
		}

	
	public void downloadLayoutPadraoImportacaoMatriculaEnade() throws Exception {
		try {
			File arquivo = new File(UteisJSF.getCaminhoWeb() + "arquivos" +  File.separator + "ModeloImportacaoMatriculaEnade.xlsx");
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", arquivo.getName());
			request.getSession().setAttribute("pastaBaseArquivo", arquivo.getPath().substring(0, arquivo.getPath().lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarTurma() {
		try {
			if (getUnidadeEnsinoVOs().isEmpty()) {
				throw new Exception(getMensagemInternalizacao("msg_MatriculaEndade_unidadeEnsinoVOs_vazio"));
			}
			super.consultar();
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurma().equals("identificadorTurma")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaCurso(getValorConsultaTurma(), getCursoVOs(), getUnidadeEnsinoVOs(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeUnidadeEnsino")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorUnidadeEnsinoCurso(getValorConsultaTurma(), getCursoVOs(), getUnidadeEnsinoVOs(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaTurma().equals("nomeTurno")) {
				if (getValorConsultaTurma().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
				objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorTurnoCurso(getValorConsultaTurma(), getCursoVOs(), getUnidadeEnsinoVOs(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
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
			setListaConsulta(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	   
	   public List<SelectItem> getListaSelectItemCondicaoEnade() {
			if (listaSelectItemCondicaoEnade == null) {
				List<SelectItem> objs = new ArrayList<SelectItem>(0);
				for (TEnumCondicaoEnade obj : TEnumCondicaoEnade.values()) {
					objs.add(new SelectItem(obj, obj.toString()));
				}
				return objs ; 
			}
			return listaSelectItemCondicaoEnade;
		}

		public void setListaSelectItemCondicaoEnade(List<SelectItem> listaSelectItemCondicaoEnade) {
			this.listaSelectItemCondicaoEnade = listaSelectItemCondicaoEnade;
		}
}
