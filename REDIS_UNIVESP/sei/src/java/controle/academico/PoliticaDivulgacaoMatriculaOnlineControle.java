/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controle.academico;

import java.io.File;
/**
 * 
 * @author Manoel
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO;
import negocio.comuns.academico.PoliticaDivulgacaoMatriculaOnlineVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.ead.enumeradores.TipoPeriodoLetivoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.Escolaridade;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

@Controller("PoliticaDivulgacaoMatriculaOnlineControle")
@Scope("viewScope")
public class PoliticaDivulgacaoMatriculaOnlineControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private PoliticaDivulgacaoMatriculaOnlineVO politicaDivulgacaoMatriculaOnlineVO;
	private PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO politicaDivulgacaoMatriculaOnlinePublicoAlvoVO;
	private String publicoAlvo;
	private String nivelEducacionalAluno;
	private String nivelEducacionalCoordenador;
	private String formacaoAcademicaFuncionario;
	private String valorConsultaCurso;
	private String campoConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	private String valorConsultaTurno;
	private String campoConsultaTurno;
	private List<TurnoVO> listaConsultaTurno;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private String responsavel;
	private String valorConsultaCargo;
	private String campoConsultaCargo;
	private List<CargoVO> listaConsultaCargo;
	private String valorConsultaDepartamento;
	private String campoConsultaDepartamento;
	private List<DepartamentoVO> listaConsultaDepartamento;
	private List listaSelectItemUnidadeEnsino;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<UnidadeEnsinoVO> listaUnidadeEnsino;
	private String dataCadastro;
	private UnidadeEnsinoVO unidadeAluno;
	private CursoVO cursoAluno;
	private TurnoVO turnoAluno;
	private TurmaVO turmaAluno;
	private DepartamentoVO departamentoFuncionario;
	private CargoVO cargoFuncionario;
	private UnidadeEnsinoVO unidadeCoordenador;
	private UnidadeEnsinoVO unidadeFuncionario;
	private CursoVO cursoCoordenador;
	private TurnoVO turnoCoordenador;
	private TurmaVO turmaCoordenador;
	private List listaNivelEducacional;
	private List listaFormacaoAcademica;
	private UnidadeEnsinoVO unidadeProfessor;
	private String nivelEducacionalProfessor;
	private CursoVO cursoProfessor;
	private TurnoVO turnoProfessor;
	private TurmaVO turmaProfessor;
	private Boolean apresentarResponsavelAtivacao;
	private Boolean apresentarResponsavelInativacao;
	private Boolean bloquearAlterarListaNivelEducacional;

	public PoliticaDivulgacaoMatriculaOnlineControle() throws Exception {
		inicializarListasSelectItemTodosComboBox();
		inicializarObjetoPoliticaDivulgacaoMatriculaOnlineVO();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_politica_inicio");
		setMensagemDetalhada("");
		setPublicoAlvo("ALUNO");
	}

	public void inicializarListasSelectItemTodosComboBox() throws Exception {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemNivelEducacional();
		montarListaSelectItemFormacaoAcademica();
	}

	public void definePublicoAlvoCoordenador() {
		setPublicoAlvo("COORDENADOR");
		setListaConsultaCurso(null);
	}

	public void definePublicoAlvoAluno() {
		setPublicoAlvo("ALUNO");
		setListaConsultaCurso(null);
	}

	public void definePublicoAlvoFuncionario() {
		setPublicoAlvo("FUNCIONARIO");
		setListaConsultaCurso(null);
	}

	public void definePublicoAlvoProfessor() {
		setPublicoAlvo("PROFESSOR");
		setListaConsultaCurso(null);
	}

	public void inicializarObjetoPoliticaDivulgacaoMatriculaOnlineVO() {
		PoliticaDivulgacaoMatriculaOnlineVO politicaDivulgacaoMatriculaOnline;
		politicaDivulgacaoMatriculaOnline = (PoliticaDivulgacaoMatriculaOnlineVO) context().getExternalContext().getSessionMap().get(PoliticaDivulgacaoMatriculaOnlineVO.class.getSimpleName());
		if (politicaDivulgacaoMatriculaOnline != null) {
			setPoliticaDivulgacaoMatriculaOnlineVO(politicaDivulgacaoMatriculaOnline);
		}
	}

	public void adicionarItemPoliticaDivulgacaoMatriculaOnlinePublicoAlvoFuncionario() {
		Integer codigoCurso = 0;
		try {
			if (!getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo().equals(0)) {
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setPoliticaDivulgacaoMatriculaOnline(getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo());
			}
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setUnidadeEnsino(getUnidadeFuncionario());
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setDepartamento(getDepartamentoFuncionario());
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setCargo(getCargoFuncionario());
				getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade().adicionarObjPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO(), getPoliticaDivulgacaoMatriculaOnlineVO(), getPublicoAlvo(), getFormacaoAcademicaFuncionario(), getListaUnidadeEnsino(), getUsuarioLogado(), codigoCurso);
				setUnidadeFuncionario(new UnidadeEnsinoVO());
				setFormacaoAcademicaFuncionario("");
				setDepartamentoFuncionario(new DepartamentoVO());
				setCargoFuncionario(new CargoVO());
			setMensagemID("msg_politica_adicionada");
			setPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO(new PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO());
		} catch (Exception e) {
			setMensagemID("");
			setMensagemDetalhada(e.getMessage());
		}

	}

	public void uploadArquivoLogo(FileUploadEvent event) {
		try {
			getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().upLoadLogoPoliticaDivulgacaoMatriculaOnline(event, getPoliticaDivulgacaoMatriculaOnlineVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_dados_adicionados", Uteis.SUCESSO);
		} catch (ConsistirException e) {
			setConsistirExceptionMensagemDetalhada("msg_erro", e, Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);

		}
	}

	public void persistir() {
		try {
			// getPoliticaDivulgacaoMatriculaOnlineVO().setPublicoAlvo(getPublicoAlvo());
			if (getPoliticaDivulgacaoMatriculaOnlineVO().getNovoObj()) {
				getPoliticaDivulgacaoMatriculaOnlineVO().setUsuario(getUsuarioLogadoClone());
			}
			getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().persistir(getPoliticaDivulgacaoMatriculaOnlineVO(), getUsuarioLogado(), getPublicoAlvo(), getConfiguracaoGeralPadraoSistema());
			setMensagemID("msg_politica_gravada");
		} catch (Exception e) {
			setMensagemID("");
			setMensagemDetalhada(e.getMessage());
		}
	}

	public String getUrlLogoApresentar() {
		if (getPoliticaDivulgacaoMatriculaOnlineVO().getExisteLogo()) {
			try {
				return getConfiguracaoGeralPadraoSistema().getUrlExternoDownloadArquivo() + File.separator + getPoliticaDivulgacaoMatriculaOnlineVO().getCaminhoBaseLogo().replaceAll("\\\\", "/") + "/" + getPoliticaDivulgacaoMatriculaOnlineVO().getNomeArquivoLogo();
			} catch (Exception e) {
				return "";
			}
		}
		return "";
	}

	public String novo() {
		setPoliticaDivulgacaoMatriculaOnlineVO(new PoliticaDivulgacaoMatriculaOnlineVO());
		setPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO(new PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO());
		setUnidadeAluno(new UnidadeEnsinoVO());
		setNivelEducacionalAluno("");
		setCursoAluno(new CursoVO());
		setTurnoAluno(new TurnoVO());
		setTurmaAluno(new TurmaVO());
		setUnidadeCoordenador(new UnidadeEnsinoVO());
		setNivelEducacionalCoordenador("");
		setCursoCoordenador(new CursoVO());
		setTurnoCoordenador(new TurnoVO());
		setTurmaCoordenador(new TurmaVO());
		setUnidadeFuncionario(new UnidadeEnsinoVO());
		setDepartamentoFuncionario(new DepartamentoVO());
		setCargoFuncionario(new CargoVO());
		setFormacaoAcademicaFuncionario("");
		setUnidadeProfessor(new UnidadeEnsinoVO());
		setNivelEducacionalProfessor("");
		setCursoProfessor(new CursoVO());
		setTurnoProfessor(new TurnoVO());
		setTurmaProfessor(new TurmaVO());
		setMensagemID("msg_politica_politica");
		setMensagemDetalhada("");
		apresentarResponsavel();
		return Uteis.getCaminhoRedirecionamentoNavegacao("politicaDivulgacaoMatriculaOnlineForm");
	}

	public String consultarPoliticaDivulgacaoMatriculaOnline() {
		setMensagemID("msg_politica_inicio");
		setMensagemDetalhada("");
		consultar();
		return Uteis.getCaminhoRedirecionamentoNavegacao("politicaDivulgacaoMatriculaOnlineCons");
	}

	public void removerPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs() {
		try {
			if (getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().getPublicoAlvo().equals("COORDENADOR")) {
				setPublicoAlvo("COORDENADOR");
			}
			if (getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().getPublicoAlvo().equals("FUNCIONARIO")) {
				setPublicoAlvo("FUNCIONARIO");
			}
			if (getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().getPublicoAlvo().equals("ALUNO")) {
				setPublicoAlvo("ALUNO");
			}
			if (getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().getPublicoAlvo().equals("PROFESSOR")) {
				setPublicoAlvo("PROFESSOR");
			}
			getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade().removerPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(getPoliticaDivulgacaoMatriculaOnlineVO(), getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO(), getPublicoAlvo());
			setPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO(new PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO());
			setMensagemID("msg_politica_removida");
		} catch (Exception e) {
			setMensagemID("");
			setMensagemDetalhada(e.getMessage());
		}

	}

	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "PoliticadivulgacaomatriculaonlineControle", "Inicializando Consultar Politica", "Consultando");
			super.consultar();
			List<PoliticaDivulgacaoMatriculaOnlineVO> objs = new ArrayList(0);
			objs = getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().consultarPorNomePolitica(getControleConsulta().getCampoConsulta(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado(), getListaUnidadeEnsino());
			setListaConsulta(objs);
			getPoliticaDivulgacaoMatriculaOnlineVO().setNovoObj(Boolean.FALSE);
			registrarAtividadeUsuario(getUsuarioLogado(), "PoliticadivulgacaomatriculaonlineControle", "Finalizando Consultar Politica", "Finalizando");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("politicaDivulgacaoMatriculaOnlineCons");
	}

	public String editarPoliticaDivulgacaoMatriculaOnline() throws Exception {
		PoliticaDivulgacaoMatriculaOnlineVO obj = (PoliticaDivulgacaoMatriculaOnlineVO) context().getExternalContext().getRequestMap().get("politica");
		setPoliticaDivulgacaoMatriculaOnlineVO(obj);
		getPoliticaDivulgacaoMatriculaOnlineVO().setNovoObj(Boolean.FALSE);
		apresentarResponsavel();
		setMensagemID("msg_dados_editar");
		setPublicoAlvo("ALUNO");
		return Uteis.getCaminhoRedirecionamentoNavegacao("politicaDivulgacaoMatriculaOnlineForm");

	}

	public void editarPoliticaDivulgacaoMatriculaOnlinePublicoAlvo() throws Exception {
		PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO obj = (PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO) context().getExternalContext().getRequestMap().get("publico");
		if (getPublicoAlvo().equals("ALUNO")) {
			setUnidadeAluno(obj.getUnidadeEnsino());
			setCursoAluno(obj.getCurso());
			setTurnoAluno(obj.getTurno());
			setTurmaAluno(obj.getTurma());
			TipoNivelEducacional tipoNivelEducacinal = TipoNivelEducacional.getEnum(obj.getNivelEducacional());
			setNivelEducacionalAluno(tipoNivelEducacinal == null ? "" : tipoNivelEducacinal.getValor());

		}
		if (getPublicoAlvo().equals("COORDENADOR")) {
			setUnidadeCoordenador(obj.getUnidadeEnsino());
			setCursoCoordenador(obj.getCurso());
			setTurnoCoordenador(obj.getTurno());
			setTurmaCoordenador(obj.getTurma());
			TipoNivelEducacional tipoNivelEducacinal = TipoNivelEducacional.getEnum(obj.getNivelEducacional());
			setNivelEducacionalCoordenador(tipoNivelEducacinal == null ? "" : tipoNivelEducacinal.getValor());

		}
		if (getPublicoAlvo().equals("FUNCIONARIO")) {
			setUnidadeFuncionario(obj.getUnidadeEnsino());
			setDepartamentoFuncionario(obj.getDepartamento());
			setCargoFuncionario(obj.getCargo());
			Escolaridade escolaridade = Escolaridade.getEnum(obj.getEscolaridade());
			setFormacaoAcademicaFuncionario(escolaridade == null ? "" : escolaridade.getValor());

		}
		if (getPublicoAlvo().equals("PROFESSOR")) {
			setUnidadeProfessor(obj.getUnidadeEnsino());
			setCursoProfessor(obj.getCurso());
			setTurnoProfessor(obj.getTurno());
			setTurmaProfessor(obj.getTurma());
			TipoNivelEducacional tipoNivelEducacinal = TipoNivelEducacional.getEnum(obj.getNivelEducacional());
			setNivelEducacionalProfessor(tipoNivelEducacinal == null ? "" : tipoNivelEducacinal.getValor());

		}
		setPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO(obj);
	}

	public String excluirPoliticaDivulgacaoMatriculaOnline() {
		try {
			getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().excluirPoliticaDivulgacaoMatriculaOnline(getPoliticaDivulgacaoMatriculaOnlineVO(), getUsuarioLogado());
			setPoliticaDivulgacaoMatriculaOnlineVO(new PoliticaDivulgacaoMatriculaOnlineVO());
			setMensagemID("msg_dados_excluidos");
			setPublicoAlvo("");
		} catch (Exception e) {
			setMensagemDetalhada(e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("politicaDivulgacaoMatriculaOnlineForm");
	}

	public List getListaFormacaoAcademica() {
		if (listaFormacaoAcademica == null) {
			listaFormacaoAcademica = new ArrayList(0);
		}
		return listaFormacaoAcademica;
	}

	public void setListaFormacaoAcademica(List listaFormacaoAcademica) {
		this.listaFormacaoAcademica = listaFormacaoAcademica;
	}

	public List getListaNivelEducacional() {
		if (listaNivelEducacional == null) {
			listaNivelEducacional = new ArrayList(0);
		}
		return listaNivelEducacional;
	}

	public void setListaNivelEducacional(List listaNivelEducacional) {
		this.listaNivelEducacional = listaNivelEducacional;
	}

	public void montarListaSelectItemNivelEducacional() throws Exception {
		List<SelectItem> opcoes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, true);
		for (SelectItem item : opcoes) {
			getListaNivelEducacional().add(item);
		}

	}

	public void montarListaSelectItemFormacaoAcademica() throws Exception {
		List<SelectItem> opcoes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(Escolaridade.class, true);
		for (SelectItem item : opcoes) {
			getListaFormacaoAcademica().add(item);
		}

	}

	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getPublicoAlvo().equals("ALUNO")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsinoNivelEducacional(getValorConsultaCurso(), getUnidadeAluno().getCodigo(), getNivelEducacionalAluno(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			} else if (getPublicoAlvo().equals("COORDENADOR")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsinoNivelEducacional(getValorConsultaCurso(), getUnidadeCoordenador().getCodigo(), getNivelEducacionalCoordenador(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			} else if (getPublicoAlvo().equals("PROFESSOR")) {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsinoNivelEducacional(getValorConsultaCurso(), getUnidadeProfessor().getCodigo(), getNivelEducacionalProfessor(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			} else {
				objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeCursoUnidadeEnsinoNivelEducacional(getValorConsultaCurso(), 0, "", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCurso(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void consultarTurno() {
		try {
			List<TurnoVO> objs = new ArrayList<TurnoVO>(0);
			if (getCampoConsultaTurno().equals("nome")) {
				objs = getFacadeFactory().getTurnoFacade().consultarPorNome(getValorConsultaTurno(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaTurno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurno(new ArrayList<TurnoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getCampoConsultaTurno().equals("nome")) {
				objs = getFacadeFactory().getTurmaFacade().consultarPorIdentificadorTurma(getValorConsultaTurma(), getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaTurma(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCargo() {
		try {
			List<CargoVO> objs = new ArrayList<CargoVO>(0);
			if (getCampoConsultaCargo().equals("nome")) {
				objs = getFacadeFactory().getCargoFacade().consultarPorNome(getValorConsultaCargo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaCargo(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarDepartamento() {
		try {
			List<DepartamentoVO> objs = new ArrayList<DepartamentoVO>(0);
			if (getCampoConsultaDepartamento().equals("nome")) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorNome(getValorConsultaDepartamento(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaDepartamento(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
		if (getPublicoAlvo().equals("ALUNO")) {
			setCursoAluno(obj);
			setBloquearAlterarListaNivelEducacional(Boolean.TRUE);
		}
		if (getPublicoAlvo().equals("COORDENADOR")) {
			setCursoCoordenador(obj);
			setBloquearAlterarListaNivelEducacional(Boolean.TRUE);
		}
		if (getPublicoAlvo().equals("PROFESSOR")) {
			setCursoProfessor(obj);
			setBloquearAlterarListaNivelEducacional(Boolean.TRUE);
		}
	}
	
	public void selecionarCursoPolitica() throws Exception {
		CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItem");
		getPoliticaDivulgacaoMatriculaOnlineVO().setCursoVO(obj);
	}

	public void selecionarTurno() throws Exception {
		try {
			TurnoVO obj = (TurnoVO) context().getExternalContext().getRequestMap().get("turnoItem");
			if (getPublicoAlvo().equals("ALUNO")) {
				setTurnoAluno(obj);
			}
			if (getPublicoAlvo().equals("COORDENADOR")) {
				setTurnoCoordenador(obj);
			}
			if (getPublicoAlvo().equals("PROFESSOR")) {
				setTurnoProfessor(obj);
			}

		} catch (Exception e) {
		}
	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItem");
			if (getPublicoAlvo().equals("ALUNO")) {
				setTurmaAluno(obj);
			}
			if (getPublicoAlvo().equals("COORDENADOR")) {
				setTurmaCoordenador(obj);
			}
			if (getPublicoAlvo().equals("PROFESSOR")) {
				setTurmaProfessor(obj);
			}

		} catch (Exception e) {
		}
	}

	public void selecionarCargo() throws Exception {
		try {
			CargoVO obj = (CargoVO) context().getExternalContext().getRequestMap().get("cargoItem");
			setCargoFuncionario(obj);
		} catch (Exception e) {
		}
	}

	public void selecionarDepartamento() throws Exception {
		try {
			DepartamentoVO obj = (DepartamentoVO) context().getExternalContext().getRequestMap().get("departamentoItem");
			setDepartamentoFuncionario(obj);
		} catch (Exception e) {
		}
	}

	public PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO() {
		if (politicaDivulgacaoMatriculaOnlinePublicoAlvoVO == null) {
			politicaDivulgacaoMatriculaOnlinePublicoAlvoVO = new PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO();
		}
		return politicaDivulgacaoMatriculaOnlinePublicoAlvoVO;
	}

	public void setPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO(PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO politicaDivulgacaoMatriculaOnlinePublicoAlvoVO) {
		this.politicaDivulgacaoMatriculaOnlinePublicoAlvoVO = politicaDivulgacaoMatriculaOnlinePublicoAlvoVO;
	}

	public String getPublicoAlvo() {
		if (publicoAlvo == null) {
			publicoAlvo = "";
		}
		return publicoAlvo;
	}

	public void setPublicoAlvo(String publicoAlvo) {
		this.publicoAlvo = publicoAlvo;
	}

	public PoliticaDivulgacaoMatriculaOnlineVO getPoliticaDivulgacaoMatriculaOnlineVO() {
		if (politicaDivulgacaoMatriculaOnlineVO == null) {
			politicaDivulgacaoMatriculaOnlineVO = new PoliticaDivulgacaoMatriculaOnlineVO();
		}
		return politicaDivulgacaoMatriculaOnlineVO;
	}

	public void setPoliticaDivulgacaoMatriculaOnlineVO(PoliticaDivulgacaoMatriculaOnlineVO politicaDivulgacaoMatriculaOnlineVO) {
		this.politicaDivulgacaoMatriculaOnlineVO = politicaDivulgacaoMatriculaOnlineVO;
	}

	public String getNivelEducacionalAluno() {
		if (nivelEducacionalAluno == null) {
			nivelEducacionalAluno = "";
		}
		return nivelEducacionalAluno;
	}

	public void setNivelEducacionalAluno(String nivelEducacionalAluno) {
		this.nivelEducacionalAluno = nivelEducacionalAluno;
	}

	public List<SelectItem> tipoConsultaComboCurso;

	public List<SelectItem> getTipoConsultaComboCurso() {
		if (tipoConsultaComboCurso == null) {
			tipoConsultaComboCurso = new ArrayList<SelectItem>(0);
			tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboCurso;
	}

	public List<SelectItem> tipoConsultaComboTurno;

	public List<SelectItem> getTipoConsultaComboTurno() {
		if (tipoConsultaComboTurno == null) {
			tipoConsultaComboTurno = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurno.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboTurno;
	}

	public List<SelectItem> tipoConsultaComboTurma;

	public List<SelectItem> getTipoConsultaComboTurma() {
		if (tipoConsultaComboTurma == null) {
			tipoConsultaComboTurma = new ArrayList<SelectItem>(0);
			tipoConsultaComboTurma.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboTurma;
	}

	public List<SelectItem> tipoConsultaComboCargo;

	public List<SelectItem> getTipoConsultaComboCargo() {
		if (tipoConsultaComboCargo == null) {
			tipoConsultaComboCargo = new ArrayList<SelectItem>(0);
			tipoConsultaComboCargo.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboCargo;
	}

	public List<SelectItem> tipoConsultaComboDepartamento;

	public List<SelectItem> getTipoConsultaComboDepartamento() {
		if (tipoConsultaComboDepartamento == null) {
			tipoConsultaComboDepartamento = new ArrayList<SelectItem>(0);
			tipoConsultaComboDepartamento.add(new SelectItem("nome", "Nome"));
		}
		return tipoConsultaComboDepartamento;
	}

	public List<SelectItem> listaSelectItemTipoPeriodoLetivoEnum;

	public List<SelectItem> getListaSelectItemTipoPeriodoLetivoEnum() {
		if(listaSelectItemTipoPeriodoLetivoEnum == null) {
			listaSelectItemTipoPeriodoLetivoEnum = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoPeriodoLetivoEnum.class, "name", "valorApresentar", false);
		}
		return listaSelectItemTipoPeriodoLetivoEnum;
	}

	public void setListaSelectItemTipoPeriodoLetivoEnum(List<SelectItem> listaSelectItemTipoPeriodoLetivoEnum) {
		this.listaSelectItemTipoPeriodoLetivoEnum = listaSelectItemTipoPeriodoLetivoEnum;
	}
	
	public String getValorConsultaCurso() {
		if(valorConsultaCurso == null) {
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

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>();
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public String getValorConsultaTurno() {
		if (valorConsultaTurno == null) {
			valorConsultaTurno = "";
		}
		return valorConsultaTurno;
	}

	public void setValorConsultaTurno(String valorConsultaTurno) {
		this.valorConsultaTurno = valorConsultaTurno;
	}

	public String getCampoConsultaTurno() {
		if (campoConsultaTurno == null) {
			campoConsultaTurno = "";
		}
		return campoConsultaTurno;
	}

	public void setCampoConsultaTurno(String campoConsultaTurno) {
		this.campoConsultaTurno = campoConsultaTurno;
	}

	public List<TurnoVO> getListaConsultaTurno() {
		if (listaConsultaTurno == null) {
			listaConsultaTurno = new ArrayList<TurnoVO>();
		}
		return listaConsultaTurno;
	}

	public void setListaConsultaTurno(List<TurnoVO> listaConsultaTurno) {
		this.listaConsultaTurno = listaConsultaTurno;
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

	public List<TurmaVO> getListaConsultaTurma() {
		if (listaConsultaTurma == null) {
			listaConsultaTurma = new ArrayList<TurmaVO>();
		}
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getResponsavel() {
		if (getPoliticaDivulgacaoMatriculaOnlineVO().getNovoObj()) {
			responsavel = getUsuarioLogado().getNome();
		} else {
			responsavel = getPoliticaDivulgacaoMatriculaOnlineVO().getUsuario().getNome();
		}
		return responsavel;
	}

	public String getValorConsultaCargo() {
		if (valorConsultaCargo == null) {
			valorConsultaCargo = "";
		}
		return valorConsultaCargo;
	}

	public void setValorConsultaCargo(String valorConsultaCargo) {
		this.valorConsultaCargo = valorConsultaCargo;
	}

	public String getCampoConsultaCargo() {
		if (campoConsultaCargo == null) {
			campoConsultaCargo = "";
		}
		return campoConsultaCargo;
	}

	public void setCampoConsultaCargo(String campoConsultaCargo) {
		this.campoConsultaCargo = campoConsultaCargo;
	}

	public List<CargoVO> getListaConsultaCargo() {
		if (listaConsultaCargo == null) {
			listaConsultaCargo = new ArrayList<CargoVO>();
		}
		return listaConsultaCargo;
	}

	public void setListaConsultaCargo(List<CargoVO> listaConsultaCargo) {
		this.listaConsultaCargo = listaConsultaCargo;
	}

	public String getValorConsultaDepartamento() {
		if (valorConsultaDepartamento == null) {
			valorConsultaDepartamento = "";
		}
		return valorConsultaDepartamento;
	}

	public void setValorConsultaDepartamento(String valorConsultaDepartamento) {
		this.valorConsultaDepartamento = valorConsultaDepartamento;
	}

	public String getCampoConsultaDepartamento() {
		if (campoConsultaDepartamento == null) {
			campoConsultaDepartamento = "";
		}
		return campoConsultaDepartamento;
	}

	public void setCampoConsultaDepartamento(String campoConsultaDepartamento) {
		this.campoConsultaDepartamento = campoConsultaDepartamento;
	}

	public List<DepartamentoVO> getListaConsultaDepartamento() {
		if (listaConsultaDepartamento == null) {
			listaConsultaDepartamento = new ArrayList<DepartamentoVO>();
		}
		return listaConsultaDepartamento;
	}

	public void setListaConsultaDepartamento(List<DepartamentoVO> listaConsultaDepartamento) {
		this.listaConsultaDepartamento = listaConsultaDepartamento;
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

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			setUnidadeEnsinoVO(new UnidadeEnsinoVO());
			if (getIsExisteUnidadeEnsino()) {
				montarListaSelectItemUnidadeEnsino(getUnidadeEnsinoVO().getNome());
			} else {
				montarListaSelectItemUnidadeEnsino("");
			}
			setMensagemID("");
		} catch (Exception e) {
		}
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

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(prm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				objs.add(new SelectItem(0, "Todas as Unidades"));
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
				getListaUnidadeEnsino().add(obj);

			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public boolean getIsExisteUnidadeEnsino() {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() == 0) {
				return false;
			} else {
				getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
				getUnidadeEnsinoVO().setNome(getUnidadeEnsinoLogado().getNome());
				return true;
			}
		} catch (Exception ex) {
			return false;
		}
	}

	public List<UnidadeEnsinoVO> getListaUnidadeEnsino() {
		if (listaUnidadeEnsino == null) {
			listaUnidadeEnsino = new ArrayList<UnidadeEnsinoVO>();
		}
		return listaUnidadeEnsino;
	}

	public void setListaUnidadeEnsino(List<UnidadeEnsinoVO> listaUnidadeEnsino) {
		this.listaUnidadeEnsino = listaUnidadeEnsino;
	}

	public String getDataCadastro() {
		if (getPoliticaDivulgacaoMatriculaOnlineVO().getNovoObj()) {
			return (Uteis.getDataAtualAplicandoFormatacao("dd/MM/yyyy HH:mm"));
		} else {
			return Uteis.getData(getPoliticaDivulgacaoMatriculaOnlineVO().getDataCadastro(), "dd/MM/yyyy HH:mm");
		}

	}

	public void setDataCadastro(String dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public UnidadeEnsinoVO getUnidadeAluno() {
		if (unidadeAluno == null) {
			unidadeAluno = new UnidadeEnsinoVO();
		}
		return unidadeAluno;
	}

	public void setUnidadeAluno(UnidadeEnsinoVO unidadeAluno) {
		this.unidadeAluno = unidadeAluno;
	}

	public CursoVO getCursoAluno() {
		if (cursoAluno == null) {
			cursoAluno = new CursoVO();
		}
		return cursoAluno;
	}

	public void setCursoAluno(CursoVO cursoAluno) {
		this.cursoAluno = cursoAluno;
	}

	public TurnoVO getTurnoAluno() {
		if (turnoAluno == null) {
			turnoAluno = new TurnoVO();
		}
		return turnoAluno;
	}

	public void setTurnoAluno(TurnoVO turnoAluno) {
		this.turnoAluno = turnoAluno;
	}

	public TurmaVO getTurmaAluno() {
		if (turmaAluno == null) {
			turmaAluno = new TurmaVO();
		}
		return turmaAluno;
	}

	public void setTurmaAluno(TurmaVO turmaAluno) {
		this.turmaAluno = turmaAluno;
	}

	public String getNivelEducacionalCoordenador() {
		if (nivelEducacionalCoordenador == null) {
			nivelEducacionalCoordenador = "";
		}
		return nivelEducacionalCoordenador;
	}

	public void setNivelEducacionalCoordenador(String nivelEducacionalCoordenador) {
		this.nivelEducacionalCoordenador = nivelEducacionalCoordenador;
	}

	public String getFormacaoAcademicaFuncionario() {
		if (formacaoAcademicaFuncionario == null) {
			formacaoAcademicaFuncionario = "";
		}
		return formacaoAcademicaFuncionario;
	}

	public void setFormacaoAcademicaFuncionario(String formacaoAcademicaFuncionario) {
		this.formacaoAcademicaFuncionario = formacaoAcademicaFuncionario;
	}

	public UnidadeEnsinoVO getUnidadeCoordenador() {
		if (unidadeCoordenador == null) {
			unidadeCoordenador = new UnidadeEnsinoVO();
		}
		return unidadeCoordenador;
	}

	public void setUnidadeCoordenador(UnidadeEnsinoVO unidadeCoordenador) {
		this.unidadeCoordenador = unidadeCoordenador;
	}

	public UnidadeEnsinoVO getUnidadeFuncionario() {
		if (unidadeFuncionario == null) {
			unidadeFuncionario = new UnidadeEnsinoVO();
		}
		return unidadeFuncionario;
	}

	public void setUnidadeFuncionario(UnidadeEnsinoVO unidadeFuncionario) {
		this.unidadeFuncionario = unidadeFuncionario;
	}

	public CursoVO getCursoCoordenador() {
		if (cursoCoordenador == null) {
			cursoCoordenador = new CursoVO();
		}
		return cursoCoordenador;
	}

	public void setCursoCoordenador(CursoVO cursoCoordenador) {
		this.cursoCoordenador = cursoCoordenador;
	}

	public DepartamentoVO getDepartamentoFuncionario() {
		if (departamentoFuncionario == null) {
			departamentoFuncionario = new DepartamentoVO();
		}
		return departamentoFuncionario;
	}

	public void setDepartamentoFuncionario(DepartamentoVO departamentoFuncionario) {
		this.departamentoFuncionario = departamentoFuncionario;
	}

	public CargoVO getCargoFuncionario() {
		if (cargoFuncionario == null) {
			cargoFuncionario = new CargoVO();
		}
		return cargoFuncionario;
	}

	public void setCargoFuncionario(CargoVO cargoFuncionario) {
		this.cargoFuncionario = cargoFuncionario;
	}

	public TurnoVO getTurnoCoordenador() {
		if (turnoCoordenador == null) {
			turnoCoordenador = new TurnoVO();
		}
		return turnoCoordenador;
	}

	public void setTurnoCoordenador(TurnoVO turnoCoordenador) {
		this.turnoCoordenador = turnoCoordenador;
	}

	public TurmaVO getTurmaCoordenador() {
		if (turmaCoordenador == null) {
			turmaCoordenador = new TurmaVO();
		}
		return turmaCoordenador;
	}

	public void setTurmaCoordenador(TurmaVO turmaCoordenador) {
		this.turmaCoordenador = turmaCoordenador;
	}

	public UnidadeEnsinoVO getUnidadeProfessor() {
		if (unidadeProfessor == null) {
			unidadeProfessor = new UnidadeEnsinoVO();
		}
		return unidadeProfessor;
	}

	public void setUnidadeProfessor(UnidadeEnsinoVO unidadeProfessor) {
		this.unidadeProfessor = unidadeProfessor;
	}

	public String getNivelEducacionalProfessor() {
		if (nivelEducacionalProfessor == null) {
			nivelEducacionalProfessor = "";
		}
		return nivelEducacionalProfessor;
	}

	public void setNivelEducacionalProfessor(String nivelEducacionalProfessor) {
		this.nivelEducacionalProfessor = nivelEducacionalProfessor;
	}

	public TurnoVO getTurnoProfessor() {
		if (turnoProfessor == null) {
			turnoProfessor = new TurnoVO();
		}
		return turnoProfessor;
	}

	public void setTurnoProfessor(TurnoVO turnoProfessor) {
		this.turnoProfessor = turnoProfessor;
	}

	public TurmaVO getTurmaProfessor() {
		if (turmaProfessor == null) {
			turmaProfessor = new TurmaVO();
		}
		return turmaProfessor;
	}

	public void setTurmaProfessor(TurmaVO turmaProfessor) {
		this.turmaProfessor = turmaProfessor;
	}

	public CursoVO getCursoProfessor() {
		if (cursoProfessor == null) {
			cursoProfessor = new CursoVO();
		}
		return cursoProfessor;
	}

	public void setCursoProfessor(CursoVO cursoProfessor) {
		this.cursoProfessor = cursoProfessor;
	}
	
	public void ativarPoliticaDivulgacaoMatriculaOnline() {
		try {
			if (getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo().equals(0)) {
				throw new Exception(UteisJSF.internacionalizar("msg_PoliticaDivulgacaoMatriculaOnline_graveAPoliticaDivulgacaoMatriculaOnlinePrimeiro"));
			}
			getPoliticaDivulgacaoMatriculaOnlineVO().setSituacaoEnum(SituacaoEnum.ATIVO);
			getPoliticaDivulgacaoMatriculaOnlineVO().setResponsavelAtivacao(getUsuarioLogadoClone());
			getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().alterarSituacaoPoliticaDivulgacaoMatriculaOnlineAtivo(getPoliticaDivulgacaoMatriculaOnlineVO(), false, getUsuarioLogado());
			apresentarResponsavel();
			setMensagemID("msg_dados_ativado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public void inativarPoliticaDivulgacaoMatriculaOnline() {
		try {
			getPoliticaDivulgacaoMatriculaOnlineVO().setSituacaoEnum(SituacaoEnum.INATIVO);
			getPoliticaDivulgacaoMatriculaOnlineVO().setResponsavelInativacao(getUsuarioLogadoClone());
			getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlineInterfaceFacade().alterarSituacaoPoliticaDivulgacaoMatriculaOnlineInativo(getPoliticaDivulgacaoMatriculaOnlineVO(), false, getUsuarioLogado());
			apresentarResponsavel();
			setMensagemID("msg_dados_inativado", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void apresentarResponsavel() {
		if (getPoliticaDivulgacaoMatriculaOnlineVO().getSituacaoEnum().equals(SituacaoEnum.INATIVO)) {
			setApresentarResponsavelInativacao(true);
			setApresentarResponsavelAtivacao(true);
		} else if (getPoliticaDivulgacaoMatriculaOnlineVO().getSituacaoEnum().equals(SituacaoEnum.ATIVO)) {
			setApresentarResponsavelInativacao(false);
			setApresentarResponsavelAtivacao(true);
		} else if (getPoliticaDivulgacaoMatriculaOnlineVO().getSituacaoEnum().equals(SituacaoEnum.EM_CONSTRUCAO)) {
			setApresentarResponsavelInativacao(false);
			setApresentarResponsavelAtivacao(false);
		}
	}

	public Boolean getApresentarResponsavelAtivacao() {
		if(apresentarResponsavelAtivacao == null) {
			apresentarResponsavelAtivacao = false;
		}
		return apresentarResponsavelAtivacao;
	}

	public void setApresentarResponsavelAtivacao(Boolean apresentarResponsavelAtivacao) {
		this.apresentarResponsavelAtivacao = apresentarResponsavelAtivacao;
	}

	public Boolean getApresentarResponsavelInativacao() {
		if(apresentarResponsavelInativacao == null) {
			apresentarResponsavelInativacao = false;
		}
		return apresentarResponsavelInativacao;
	}

	public void setApresentarResponsavelInativacao(Boolean apresentarResponsavelInativacao) {
		this.apresentarResponsavelInativacao = apresentarResponsavelInativacao;
	}
	
	public void adicionarItemPoliticaDivulgacaoMatriculaOnlinePublicoAlvoAluno() {
		Integer codigoCurso = 0;
		try {
			if (!getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo().equals(0)) {
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setPoliticaDivulgacaoMatriculaOnline(getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo());
			}
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setUnidadeEnsino(getUnidadeAluno());
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setCurso(getCursoAluno());
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setTurno(getTurnoAluno());
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setTurma(getTurmaAluno());
				codigoCurso = getCursoAluno().getCodigo();
				getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade().adicionarObjPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO(), getPoliticaDivulgacaoMatriculaOnlineVO(), getPublicoAlvo(), getNivelEducacionalAluno(), getListaUnidadeEnsino(), getUsuarioLogado(),codigoCurso);
				setUnidadeAluno(new UnidadeEnsinoVO());
				setNivelEducacionalAluno("");
				setCursoAluno(new CursoVO());
				setTurnoAluno(new TurnoVO());
				setTurmaAluno(new TurmaVO());
				setBloquearAlterarListaNivelEducacional(Boolean.FALSE);
			setMensagemID("msg_politica_adicionada");
			setPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO(new PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO());
		} catch (Exception e) {
			setMensagemID("");
			setMensagemDetalhada(e.getMessage());
		}
	}
	
	public void adicionarItemPoliticaDivulgacaoMatriculaOnlinePublicoAlvoProfessor() {
		Integer codigoCurso = 0;
		try {
			if (!getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo().equals(0)) {
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setPoliticaDivulgacaoMatriculaOnline(getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo());
			}
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setUnidadeEnsino(getUnidadeProfessor());
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setCurso(getCursoProfessor());
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setTurno(getTurnoProfessor());
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setTurma(getTurmaProfessor());
				codigoCurso = getCursoProfessor().getCodigo();
				getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade().adicionarObjPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO(), getPoliticaDivulgacaoMatriculaOnlineVO(), getPublicoAlvo(), getNivelEducacionalProfessor(), getListaUnidadeEnsino(), getUsuarioLogado(), codigoCurso);
				setUnidadeProfessor(new UnidadeEnsinoVO());
				setNivelEducacionalProfessor("");
				setCursoProfessor(new CursoVO());
				setTurnoProfessor(new TurnoVO());
				setTurmaProfessor(new TurmaVO());
				setBloquearAlterarListaNivelEducacional(Boolean.FALSE);
			setMensagemID("msg_politica_adicionada");
			setPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO(new PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO());
		} catch (Exception e) {
			setMensagemID("");
			setMensagemDetalhada(e.getMessage());
		}
	}
	
	public void adicionarItemPoliticaDivulgacaoMatriculaOnlinePublicoAlvoCoordenador() {
		Integer codigoCurso = 0;
		try {
			if (!getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo().equals(0)) {
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setPoliticaDivulgacaoMatriculaOnline(getPoliticaDivulgacaoMatriculaOnlineVO().getCodigo());
			}
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setUnidadeEnsino(getUnidadeCoordenador());
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setCurso(getCursoCoordenador());
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setTurno(getTurnoCoordenador());
				getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO().setTurma(getTurmaCoordenador());
				codigoCurso = getCursoCoordenador().getCodigo();
				getFacadeFactory().getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoInterfaceFacade().adicionarObjPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVOs(getPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO(), getPoliticaDivulgacaoMatriculaOnlineVO(), getPublicoAlvo(), getNivelEducacionalCoordenador(), getListaUnidadeEnsino(), getUsuarioLogado(), codigoCurso);
				setUnidadeCoordenador(new UnidadeEnsinoVO());
				setNivelEducacionalCoordenador("");
				setCursoCoordenador(new CursoVO());
				setTurnoCoordenador(new TurnoVO());
				setTurmaCoordenador(new TurmaVO());
				setBloquearAlterarListaNivelEducacional(Boolean.FALSE);
			setMensagemID("msg_politica_adicionada");
			setPoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO(new PoliticaDivulgacaoMatriculaOnlinePublicoAlvoVO());
		} catch (Exception e) {
			setMensagemID("");
			setMensagemDetalhada(e.getMessage());
		}
	}
	
	public void limparCurso(){		
		setCursoAluno(new CursoVO());
		setCursoCoordenador(new CursoVO());
		setCursoProfessor(new CursoVO());
		setBloquearAlterarListaNivelEducacional(Boolean.FALSE);
	}

	public Boolean getBloquearAlterarListaNivelEducacional() {
		if (bloquearAlterarListaNivelEducacional == null) {
			bloquearAlterarListaNivelEducacional = Boolean.FALSE;
		}
		return bloquearAlterarListaNivelEducacional;
	}

	public void setBloquearAlterarListaNivelEducacional(Boolean bloquearAlterarListaNivelEducacional) {
		this.bloquearAlterarListaNivelEducacional = bloquearAlterarListaNivelEducacional;
	}
	
	
}
