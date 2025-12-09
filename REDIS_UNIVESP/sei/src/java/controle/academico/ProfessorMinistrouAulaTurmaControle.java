package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas registroAulaForm.jsp
 * registroAulaCons.jsp) com as funcionalidades da classe <code>RegistroAula</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see RegistroAula
 * @see ProfessorTitularDisciplinaTurmaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemChave;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.FrequenciaAulaVO;
import negocio.comuns.academico.HorarioTurmaDisciplinaProgramadaVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.ProfessorTitularDisciplinaTurmaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.administrativo.FormacaoAcademica;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("ProfessorMinistrouAulaTurmaControle")
@Scope("viewScope")
@Lazy
public class ProfessorMinistrouAulaTurmaControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	// private ProfessorMinistrouAulaTurmaVO professorMinistrouAulaTurmaVO;
	private ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO;
	private String nomeUsuarioLogado;
	private String programacaoAula_Erro;
	private String responsavelRegistroAula_Erro;
	private String turma_Erro;
	private String disciplina_Erro;
	private String diaSemana_Erro;
	private List listaSelectItemDisciplinasProgramacaoAula;
	private List listaSelectItemProfessor;
	private List listaSelectItemTurma;
	private List listaProfessor;
	private List<ProfessorTitularDisciplinaTurmaVO> listaProfessoresTitularDisciplinaTurma;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List listaConsultaTurma;
	private FrequenciaAulaVO frequenciaAulaVO;
	private String matricula_Erro;
	private String turma_cursoTurno;
	private boolean possuiPermissaoAlterarCargaHoraria;
	private String campoConsultaProfessor;
	private String valorConsultaProfessor;
	private List<PessoaVO> listaConsultaProfessor;
	private ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVORemover;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	private String campoConsultaDisciplina;
	private String valorConsultaDisciplina;
	private List<DisciplinaVO> listaConsultaDisciplinaVOs;
	private List<SelectItem> tipoConsultaComboDisciplina;

	public ProfessorMinistrouAulaTurmaControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setListaSelectItemTurma(new ArrayList(0));
		setListaSelectItemDisciplinasProgramacaoAula(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		novo();
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>RegistroAula</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setTurma_cursoTurno("");
		setTurma_Erro("");
		setMensagemDetalhada("");
		setDiaSemana_Erro("");
		setProfessorTitularDisciplinaTurmaVO(new ProfessorTitularDisciplinaTurmaVO());
		setFrequenciaAulaVO(new FrequenciaAulaVO());
		setListaProfessor(new ArrayList(0));
		setListaProfessoresTitularDisciplinaTurma(new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0));
		inicializarListasSelectItemTodosComboBox();
		inicializarUsuarioResponsavelMatriculaUsuarioLogado();
		// carregarCargaHoraria();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("professorMinistrouAulaTurmaForm.xhtml");
	}

	public void inicializarUsuarioResponsavelMatriculaUsuarioLogado() {
		try {
			// professorTitularDisciplinaTurmaVO.setResponsavelRegistro(getUsuarioLogadoClone());
			setNomeUsuarioLogado(getUsuarioLogado().getNome());
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>RegistroAula</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		ProfessorTitularDisciplinaTurmaVO obj = (ProfessorTitularDisciplinaTurmaVO) context().getExternalContext().getRequestMap().get("registroAulaItens");
		obj = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		setTurma_Erro("");
		setProfessorTitularDisciplinaTurmaVO(obj);
		inicializarListasSelectItemTodosComboBox();
		getListaSelectItemProfessor().add(new SelectItem(obj.getProfessor().getCodigo(), obj.getProfessor().getPessoa().getNome()));
		obj.setNovoObj(Boolean.FALSE);
		setFrequenciaAulaVO(new FrequenciaAulaVO());
		VisaoProfessorControle visaoProfessor = (VisaoProfessorControle) context().getExternalContext().getSessionMap().get("VisaoProfessorControle");
		if (visaoProfessor != null) {
			visaoProfessor.inicializarMenuRegistroAula();
			montarListaDisciplinaTurmaVisaoProfessor();
			montarListaSelectItemTurma();
		}
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("professorMinistrouAulaTurmaForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>RegistroAula</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			validarDados();
			getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().alterarListaProfessoresTitularDisciplinaTurma(getListaProfessoresTitularDisciplinaTurma(), getProfessorTitularDisciplinaTurmaVO().getTipoDefinicaoProfessor(), getUsuarioLogado());
			setMensagemDetalhada("");
			setDiaSemana_Erro("");
			setTurma_cursoTurno("");
			setTurma_Erro("");
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("professorMinistrouAulaTurmaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("professorMinistrouAulaTurmaForm.xhtml");
		}
	}

	public void validarDados() throws Exception {
		if (getIsTipoDefinicaoTipoTurma()) {
			if (getProfessorTitularDisciplinaTurmaVO().getTurma().getIdentificadorTurma().equals("")) {
				throw new Exception("O campo TURMA deve ser informado.");
			}
		}
		if (getIsTipoDefinicaoTipoCurso()) {
			if (getProfessorTitularDisciplinaTurmaVO().getCursoVO().getCodigo().equals(0)) {
				throw new Exception("O campo CURSO deve ser informado.");
			}
		}
		if (getProfessorTitularDisciplinaTurmaVO().getDisciplina().getCodigo() == 0) {
			throw new Exception("O campo DISCIPLINA deve ser informado.");
		}
	}

	public void atualizarTitularSelecionado() {
		ProfessorTitularDisciplinaTurmaVO obj = (ProfessorTitularDisciplinaTurmaVO) context().getExternalContext().getRequestMap().get("professoresItens");
		getListaProfessoresTitularDisciplinaTurma().forEach(p -> p.setTitular(p.getProfessor().getPessoa().getCodigo().equals(obj.getProfessor().getPessoa().getCodigo())));
	}

	public void gravarVisaoProfessor() {
		try {
			// professorTitularDisciplinaTurmaVO.setDataRegistroAula(new
			// Date());
			if (professorTitularDisciplinaTurmaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().incluir(professorTitularDisciplinaTurmaVO, getUsuarioLogado());
			} else {
				inicializarUsuarioResponsavelMatriculaUsuarioLogado();
				getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().alterar(professorTitularDisciplinaTurmaVO, getUsuarioLogado());
			}
			setMensagemDetalhada("");
			setDiaSemana_Erro("");
			setTurma_cursoTurno("");
			setTurma_Erro("");
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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
				objs = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), "", "", super.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("cargaHoraria")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarPorCargaHoraria(new Integer(valorInt), "", "", super.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("conteudo")) {
				objs = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarPorConteudo(getControleConsulta().getValorConsulta(), "", "", super.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("identificadorTurma")) {
				objs = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarPorIdentificadorTurma(getControleConsulta().getValorConsulta(), "", "", super.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeDisciplina")) {
				objs = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarPorNomeDisciplina(getControleConsulta().getValorConsulta(), "", "", super.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeProfessor")) {
				objs = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarPorNomeProfessor(getControleConsulta().getValorConsulta(), "", "", super.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
				objs = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarPorNomeCurso(getControleConsulta().getValorConsulta(), "", "", super.getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoTurma")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarPorCodigoTurma(new Integer(valorInt), "", "", true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoDisciplina")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarPorCodigoDisciplina(new Integer(valorInt), "", "", true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
			definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("professorMinistrouAulaTurmaCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("professorMinistrouAulaTurmaCons.xhtml");
		}
	}

	public void consultarVisaoProfessor() {
		try {
			super.consultar();
			getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().validarConsultaDoUsuario(getUsuarioLogadoClone());
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("curso")) {
				objs = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarPorNomeCursoProfessor(getControleConsulta().getValorConsulta(), "", "", getUsuarioLogado().getPessoa().getCodigo(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("data")) {
				Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarPorDataProfessor(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), "", "", getUsuarioLogado().getPessoa().getCodigo(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("identificadorTurma")) {
				objs = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarPorIdentificadorTurmaProfessor(getControleConsulta().getValorConsulta(), "", "", getUsuarioLogado().getPessoa().getCodigo(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeDisciplina")) {
				objs = getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarPorNomeDisciplinaProfessor(getControleConsulta().getValorConsulta(), "", "", getUsuarioLogado().getPessoa().getCodigo(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	// public void paintConsultaAluno(OutputStream out, Object data) throws
	// IOException {
	// FrequenciaAulaVO pessoa = (FrequenciaAulaVO)
	// getProfessorTitularDisciplinaTurmaVO().getFrequenciaAulaVOs().get((Integer)
	// data);
	// BufferedImage bufferedImage = ImageIO.read(new BufferedInputStream(new
	// ByteArrayInputStream(pessoa.getMatricula().getAluno().getFoto())));
	// ImageIO.write(bufferedImage, "jpg", out);
	// }
	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>ProfessorTitularDisciplinaTurmaVO</code> Após a exclusão ela
	 * automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().excluir(professorTitularDisciplinaTurmaVO, getUsuarioLogado());
			setProfessorTitularDisciplinaTurmaVO(new ProfessorTitularDisciplinaTurmaVO());

			setFrequenciaAulaVO(new FrequenciaAulaVO());
			setMensagemDetalhada("");
			setDiaSemana_Erro("");
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("professorMinistrouAulaTurmaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("professorMinistrouAulaTurmaForm.xhtml");
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurma(), getValorConsultaTurma(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			professorTitularDisciplinaTurmaVO.setTurma(obj);
			consultarTurmaPorChavePrimaria();
		} catch (Exception e) {
		}
	}

	public void limparDados() {
		setProfessorTitularDisciplinaTurmaVO(new ProfessorTitularDisciplinaTurmaVO());
		montarListaSelectItemDisciplinasProgramacaoAula();
		setListaProfessoresTitularDisciplinaTurma(new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0));
	}

	public List getTipoConsultaComboTurma() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
		itens.add(new SelectItem("nomeTurno", "Turno"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List getListaSelectSemestre() {
		List lista = new ArrayList(0);
		lista.add(new SelectItem("", ""));
		lista.add(new SelectItem("1", "1º"));
		lista.add(new SelectItem("2", "2º"));
		return lista;
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe
	 * <code>FrequenciaAula</code> para o objeto
	 * <code>ProfessorTitularDisciplinaTurmaVO</code> da classe
	 * <code>RegistroAula</code>
	 */
	public String adicionarFrequenciaAula() throws Exception {
		try {
			if (!getProfessorTitularDisciplinaTurmaVO().getCodigo().equals(0)) {
				frequenciaAulaVO.setRegistroAula(getProfessorTitularDisciplinaTurmaVO().getCodigo());
			}
			if (!getFrequenciaAulaVO().getMatricula().getMatricula().equals("")) {
				String campoConsulta = getFrequenciaAulaVO().getMatricula().getMatricula();
				MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(campoConsulta, this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.getEnum(Uteis.NIVELMONTARDADOS_DADOSBASICOS), getUsuarioLogado());
				getFrequenciaAulaVO().setMatricula(matricula);
			}
			// getProfessorTitularDisciplinaTurmaVO().adicionarObjFrequenciaAulaVOs(getFrequenciaAulaVO());
			this.setFrequenciaAulaVO(new FrequenciaAulaVO());
			setMensagemID("msg_dados_adicionados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("professorMinistrouAulaTurmaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("professorMinistrouAulaTurmaForm.xhtml");
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
			MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(campoConsulta, this.getUnidadeEnsinoLogado().getCodigo(), NivelMontarDados.getEnum(Uteis.NIVELMONTARDADOS_TODOS), getUsuarioLogado());
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
			// Integer campoConsulta =
			// professorTitularDisciplinaTurmaVO.getResponsavelRegistro().getCodigo();
			// PessoaVO pessoa =
			// getFacadeFactory().getPessoaFacade().consultarPorChavePrimaria(campoConsulta,
			// false, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
			// professorTitularDisciplinaTurmaVO.getResponsavelRegistro().setNome(pessoa.getNome());
			this.setResponsavelRegistroAula_Erro("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemID("msg_erro_dadosnaoencontrados");
			// professorTitularDisciplinaTurmaVO.getResponsavelRegistro().setNome("");
			// professorTitularDisciplinaTurmaVO.getResponsavelRegistro().setCodigo(0);
			this.setResponsavelRegistroAula_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
		}
	}

	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>ProgramacaoAula</code> por meio de sua respectiva chave primária.
	 * Esta rotina é utilizada fundamentalmente por requisições Ajax, que
	 * realizam busca pela chave primária da entidade montando automaticamente o
	 * resultado da consulta para apresentação.
	 */
	public void montarListaProfessoresTitularDisciplinaTurma() throws Exception {
		try {
			setListaProfessoresTitularDisciplinaTurma(getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().montarProfessoresComProgramacaoAulaDisciplinaTurma(getProfessorTitularDisciplinaTurmaVO().getTurma().getCodigo(), getProfessorTitularDisciplinaTurmaVO().getCursoVO().getCodigo(), getProfessorTitularDisciplinaTurmaVO().getDisciplina().getCodigo(), getProfessorTitularDisciplinaTurmaVO().getAno(), getProfessorTitularDisciplinaTurmaVO().getSemestre(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
//			if(getListaProfessoresTitularDisciplinaTurma().isEmpty()) {
//				throw new Exception("A turma (" + getProfessorTitularDisciplinaTurmaVO().getTurma().getIdentificadorTurma() + ") não possuem um professor titular.");
//			}
			validarProgramacaoAulaProfessorTitularDisciplinaTurma();
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaProfessoresTitularDisciplinaTurma(new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarListaSelectItemTurma() {
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
				if (turma.getTurmaAgrupada()) {
					value = turma.getIdentificadorTurma() + " - Turno " + turma.getTurno().getNome();
				} else {
					value = turma.getIdentificadorTurma() + " - Curso " + turma.getCurso().getNome() + " - Turno " + turma.getTurno().getNome();
				}
				obj.add(new SelectItem(turma.getCodigo(), value));
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
		return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessor(getUsuarioLogado().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
	}

	// public boolean getPermiteAlterarCargaHoraria() {
	// if
	// (getProfessorTitularDisciplinaTurmaVO().getPermiteAlterarCargaHoraria()
	// || getPossuiPermissaoAlterarCargaHoraria()) {
	// return true;
	// }
	// return false;
	// }
	// public int getCount() {
	// return getProfessorTitularDisciplinaTurmaVO().getConteudo().length();
	// }
	// public String getCssCargaHoraria() {
	// if
	// (getProfessorTitularDisciplinaTurmaVO().getPermiteAlterarCargaHoraria()
	// || getPossuiPermissaoAlterarCargaHoraria()) {
	// return "camposObrigatorios";
	// }
	// return "camposSomenteLeitura";
	// }
	/**
	 * Método responsável por processar a consulta na entidade
	 * <code>Turma</code> por meio de sua respectiva chave primária. Esta rotina
	 * é utilizada fundamentalmente por requisições Ajax, que realizam busca
	 * pela chave primária da entidade montando automaticamente o resultado da
	 * consulta para apresentação.
	 */
	public void consultarTurmaPorChavePrimaria() {
		try {
			getProfessorTitularDisciplinaTurmaVO().setAno("");
			getProfessorTitularDisciplinaTurmaVO().setSemestre("");
			String campoConsulta = getProfessorTitularDisciplinaTurmaVO().getTurma().getIdentificadorTurma();
			if (campoConsulta.trim().equalsIgnoreCase("")) {
				limparDados();
				return;
			} else {
				setListaProfessoresTitularDisciplinaTurma(new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0));
				TurmaVO turmaVO = getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(campoConsulta, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
				getProfessorTitularDisciplinaTurmaVO().setTurma(turmaVO);
				montarListaSelectItemDisciplinasProgramacaoAula();
				setMensagemID("msg_dados_consultados");
				if (!turmaVO.getTurmaAgrupada()) {
					if (turmaVO.getCurso().getPeriodicidade().equals("SE")) {
						getProfessorTitularDisciplinaTurmaVO().setSemestre(Uteis.getSemestreAtual());
						getProfessorTitularDisciplinaTurmaVO().setAno(Uteis.getAnoDataAtual4Digitos());
					} else if (turmaVO.getCurso().getPeriodicidade().equals("AN")) {
						getProfessorTitularDisciplinaTurmaVO().setSemestre("");
						getProfessorTitularDisciplinaTurmaVO().setAno(Uteis.getAnoDataAtual4Digitos());
					} else {
						getProfessorTitularDisciplinaTurmaVO().setSemestre("");
						getProfessorTitularDisciplinaTurmaVO().setAno("");
					}
				}
			}
		} catch (Exception e) {
			getProfessorTitularDisciplinaTurmaVO().setTurma(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void montarAlunosVisaoProfessor() throws Exception {
		try {
			TurmaVO turmas = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(professorTitularDisciplinaTurmaVO.getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado());
			professorTitularDisciplinaTurmaVO.setTurma(turmas);
			getProfessorTitularDisciplinaTurmaVO().setSemestre("1");
			getProfessorTitularDisciplinaTurmaVO().setAno(Uteis.getAnoDataAtual4Digitos());
			montarListaDisciplinaTurmaVisaoProfessor();
			setMensagemID("msg_dados_consultados");
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

	public List consultarDisciplinaProfessorTurma() throws Exception {
		List listaConsultas = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaProfessorTurma(getUsuarioLogado().getPessoa().getCodigo(), getProfessorTitularDisciplinaTurmaVO().getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return listaConsultas;
	}

	public void limparListaProfessores() {
		setListaProfessoresTitularDisciplinaTurma(new ArrayList<ProfessorTitularDisciplinaTurmaVO>(0));
	}

	// public void carregarCargaHoraria() {
	// try {
	// setMensagemDetalhada("", "");
	// setMensagemDetalhada("");
	// setDiaSemana_Erro("");
	// montarListaSelectItemProfessor(getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().carregarCargaHorariaEmontarListaProfessorRegistroAula(professorTitularDisciplinaTurmaVO));
	// setMensagemID("msg_dados_consultados");
	// } catch (Exception e) {
	// // professorTitularDisciplinaTurmaVO.setCargaHoraria(0);
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// }
	// }
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

	public void montarListaDisciplinaAgrupada() throws Exception {
		if (this.professorTitularDisciplinaTurmaVO.getTurma().getCodigo().intValue() == 0) {
			setListaSelectItemDisciplinasProgramacaoAula(new ArrayList(0));
			return;
		}
		List resultadoConsulta = consultarDisciplinaTurmaAgrupada();
		Iterator i = resultadoConsulta.iterator();
		List objs = new ArrayList(0);
		while (i.hasNext()) {
			DisciplinaVO obj = (DisciplinaVO) i.next();
			objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort((List) objs, ordenador);
		setListaSelectItemDisciplinasProgramacaoAula(objs);
	}

	public List<DisciplinaVO> consultarDisciplinaTurmaAgrupada() throws Exception {
		List<DisciplinaVO> objs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaTurmaAgrupada(getProfessorTitularDisciplinaTurmaVO().getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		return objs;
	}

	public void montarListaDisciplinaNaoAgrupada() throws Exception {
		if (this.professorTitularDisciplinaTurmaVO.getTurma().getCodigo().intValue() == 0) {
			setListaSelectItemDisciplinasProgramacaoAula(new ArrayList(0));
			return;
		}
		// getFacadeFactory().getTurmaFacade().carregarDados(this.getProfessorTitularDisciplinaTurmaVO().getTurma(), NivelMontarDados.TODOS, getUsuarioLogado());
		
//		List<HorarioTurmaDisciplinaProgramadaVO> listaTurmaDisciplinaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(this.getProfessorTitularDisciplinaTurmaVO().getTurma().getCodigo(), true, false);
//		List<TurmaDisciplinaVO> listaTurmaDisciplinaVOs = getFacadeFactory().getTurmaDisciplinaFacade().consultarPorCodigoTurma(getProfessorTitularDisciplinaTurmaVO().getTurma().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
		List<HorarioTurmaDisciplinaProgramadaVO> horarioTurmaDisciplinaProgramadaVOs = getFacadeFactory().getHorarioTurmaFacade().consultarHorarioTurmaDisciplinaProgramadaPorTurma(getProfessorTitularDisciplinaTurmaVO().getTurma().getCodigo(), true, false, 0);
		List objs = new ArrayList(0);
			for (HorarioTurmaDisciplinaProgramadaVO obj : horarioTurmaDisciplinaProgramadaVOs) {
				objs.add(new SelectItem(obj.getCodigoDisciplina(), obj.getCodigoDisciplina() + " - "+ obj.getNomeDisciplina()));
			}			
		setListaSelectItemDisciplinasProgramacaoAula(objs);
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Cidade</code>.
	 */
	public void montarListaSelectItemDisciplinasProgramacaoAula(String prm) throws Exception {
		// if
		// (getProfessorTitularDisciplinaTurmaVO().getTurma().getTurmaAgrupada())
		// {
		// montarListaDisciplinaAgrupada();
		// } else {
		montarListaDisciplinaNaoAgrupada();
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
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	public void montarListaSelectItemProfessor(List<PessoaVO> professores) {
		try {
			setListaSelectItemProfessor(new ArrayList(0));
			for (PessoaVO obj : professores) {
				getListaSelectItemProfessor().add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemDisciplinasProgramacaoAula();
		setListaSelectItemProfessor(new ArrayList(0));
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("identificadorTurma", "Identificador Turma"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		itens.add(new SelectItem("nomeProfessor", "Professor"));
		itens.add(new SelectItem("nomeDisciplina", "Disciplina"));
		return itens;
	}

	public List getTipoConsultaComboProfessor() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("data", "Data"));
		itens.add(new SelectItem("identificadorTurma", "Identificador Turma"));
		itens.add(new SelectItem("nomeDisciplina", "Disciplina"));
		return itens;
	}

	public List getTipoAulaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("P", "Programada"));
		itens.add(new SelectItem("R", "Reposição"));
		itens.add(new SelectItem("E", "Evento"));
		return itens;
	}

	/*
	 * Método responsável por inicializar List<SelectItem> de valores do
	 * ComboBox correspondente ao atributo <code>diaSemana</code>
	 */
	public List getListaSelectItemDiaSemanaProgramacaoAula() throws Exception {
		List objs = new ArrayList(0);
		objs.add(new SelectItem("", ""));
		Hashtable diaSemanas = (Hashtable) Dominios.getDiaSemana();
		Enumeration keys = diaSemanas.keys();
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) diaSemanas.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemChave ordenador = new SelectItemOrdemChave();
		Collections.sort((List) objs, ordenador);
		return objs;
	}

	// public void carregarNovoRegistroAula() {
	// try {
	// getProfessorTitularDisciplinaTurmaVO().setNovoObj(true);
	// //carregarCargaHoraria();
	// getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().montarAlunosTurma(getProfessorTitularDisciplinaTurmaVO());
	// setMensagemID("msg_entre_dados");
	// } catch (Exception e) {
	// setMensagemDetalhada("msg_erro", e.getMessage());
	// }
	// }
	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setPaginaAtualDeTodas("0/0");
		setListaConsulta(new ArrayList(0));
		definirVisibilidadeLinksNavegacao(0, 0);
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("professorMinistrouAulaTurmaCons.xhtml");
	}

	public String getCampoConsultaTurma() {
		return campoConsultaTurma;
	}

	public void setCampoConsultaTurma(String campoConsultaTurma) {
		this.campoConsultaTurma = campoConsultaTurma;
	}

	public List getListaConsultaTurma() {
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public String getValorConsultaTurma() {
		return valorConsultaTurma;
	}

	public void setValorConsultaTurma(String valorConsultaTurma) {
		this.valorConsultaTurma = valorConsultaTurma;
	}

	public String getMatricula_Erro() {
		return matricula_Erro;
	}

	public void setMatricula_Erro(String matricula_Erro) {
		this.matricula_Erro = matricula_Erro;
	}

	public FrequenciaAulaVO getFrequenciaAulaVO() {
		return frequenciaAulaVO;
	}

	public void setFrequenciaAulaVO(FrequenciaAulaVO frequenciaAulaVO) {
		this.frequenciaAulaVO = frequenciaAulaVO;
	}

	public String getResponsavelRegistroAula_Erro() {
		return responsavelRegistroAula_Erro;
	}

	public void setResponsavelRegistroAula_Erro(String responsavelRegistroAula_Erro) {
		this.responsavelRegistroAula_Erro = responsavelRegistroAula_Erro;
	}

	public String getProgramacaoAula_Erro() {
		return programacaoAula_Erro;
	}

	public void setProgramacaoAula_Erro(String programacaoAula_Erro) {
		this.programacaoAula_Erro = programacaoAula_Erro;
	}

	public ProfessorTitularDisciplinaTurmaVO getProfessorTitularDisciplinaTurmaVO() {
		if (professorTitularDisciplinaTurmaVO == null) {
			professorTitularDisciplinaTurmaVO = new ProfessorTitularDisciplinaTurmaVO();
		}
		return professorTitularDisciplinaTurmaVO;
	}

	public void setProfessorTitularDisciplinaTurmaVO(ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVO) {
		this.professorTitularDisciplinaTurmaVO = professorTitularDisciplinaTurmaVO;
	}

	public String getTurma_Erro() {
		return turma_Erro;
	}

	public void setTurma_Erro(String turma_Erro) {
		this.turma_Erro = turma_Erro;
	}

	public String getDisciplina_Erro() {
		return disciplina_Erro;
	}

	public void setDisciplina_Erro(String disciplina_Erro) {
		this.disciplina_Erro = disciplina_Erro;
	}

	public List getListaSelectItemDisciplinasProgramacaoAula() {
		return listaSelectItemDisciplinasProgramacaoAula;
	}

	public void setListaSelectItemDisciplinasProgramacaoAula(List listaSelectItemDisciplinasProgramacaoAula) {
		this.listaSelectItemDisciplinasProgramacaoAula = listaSelectItemDisciplinasProgramacaoAula;
	}

	public String getDiaSemana_Erro() {
		return diaSemana_Erro;
	}

	public void setDiaSemana_Erro(String diaSemana_Erro) {
		this.diaSemana_Erro = diaSemana_Erro;
	}

	public String getTurma_cursoTurno() {
		return turma_cursoTurno;
	}

	public void setTurma_cursoTurno(String turma_cursoTurno) {
		this.turma_cursoTurno = turma_cursoTurno;
	}

	public List getListaSelectItemProfessor() {
		return listaSelectItemProfessor;
	}

	public void setListaSelectItemProfessor(List listaSelectItemProfessor) {
		this.listaSelectItemProfessor = listaSelectItemProfessor;
	}

	public List getListaProfessor() {
		return listaProfessor;
	}

	public void setListaProfessor(List listaProfessor) {
		this.listaProfessor = listaProfessor;
	}

	public List getListaSelectItemTurma() {
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List listaSelectItemTurma) {
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
		professorTitularDisciplinaTurmaVO = null;
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
		return listaConsultaProfessor;
	}

	/**
	 * @param listaConsultaProfessor
	 *            the listaConsultaProfessor to set
	 */
	public void setListaConsultaProfessor(List listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	public List getTipoConsultaComboProfessorBusca() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
	}

	public void consultarProfessor() {
		try {
			super.consultar();
			List<PessoaVO> objs = new ArrayList(0);
			if (getCampoConsultaProfessor().equals("nome")) {
				objs = getFacadeFactory().getPessoaFacade().consultarPorNome(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, true, getUsuarioLogado());
			}
			if (getCampoConsultaProfessor().equals("cpf")) {
				objs = getFacadeFactory().getPessoaFacade().consultaRapidaPorCPF(getValorConsultaProfessor(), TipoPessoa.PROFESSOR.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, true, getUsuarioLogado());
				for (PessoaVO pessoa : objs) {
					pessoa.setFormacaoAcademicaVOs(FormacaoAcademica.consultarFormacaoAcademicas(pessoa.getCodigo(), false, false, getUsuarioLogado()));
				}
			}
			setListaConsultaProfessor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProfessor(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * @return the listaProfessoresTitularDisciplinaTurma
	 */
	public List<ProfessorTitularDisciplinaTurmaVO> getListaProfessoresTitularDisciplinaTurma() {
		return listaProfessoresTitularDisciplinaTurma;
	}

	/**
	 * @param listaProfessoresTitularDisciplinaTurma
	 *            the listaProfessoresTitularDisciplinaTurma to set
	 */
	public void setListaProfessoresTitularDisciplinaTurma(List<ProfessorTitularDisciplinaTurmaVO> listaProfessoresTitularDisciplinaTurma) {
		this.listaProfessoresTitularDisciplinaTurma = listaProfessoresTitularDisciplinaTurma;
	}

	public String getNomeUsuarioLogado() {
		if (nomeUsuarioLogado == null) {
			nomeUsuarioLogado = "";
		}
		return nomeUsuarioLogado;
	}

	public void setNomeUsuarioLogado(String nomeUsuarioLogado) {
		this.nomeUsuarioLogado = nomeUsuarioLogado;
	}
	
	public ProfessorTitularDisciplinaTurmaVO getProfessorTitularDisciplinaTurmaVORemover() {
		if (professorTitularDisciplinaTurmaVORemover == null) {
			professorTitularDisciplinaTurmaVORemover = new ProfessorTitularDisciplinaTurmaVO();
		}
		return professorTitularDisciplinaTurmaVORemover;
	}

	public void setProfessorTitularDisciplinaTurmaVORemover(
			ProfessorTitularDisciplinaTurmaVO professorTitularDisciplinaTurmaVORemover) {
		this.professorTitularDisciplinaTurmaVORemover = professorTitularDisciplinaTurmaVORemover;
	}
	
	public void prepararDadosRemoverProfessorTitularDisciplinaTurma() {
		ProfessorTitularDisciplinaTurmaVO professorRemover = (ProfessorTitularDisciplinaTurmaVO) context().getExternalContext().getRequestMap().get("professoresItens");
		setProfessorTitularDisciplinaTurmaVORemover(professorRemover);
	}

	public void removerProfessorTitularDisciplinaTurma() {
		if (Uteis.isAtributoPreenchido(getProfessorTitularDisciplinaTurmaVORemover())) {
			try {
				getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().excluir(getProfessorTitularDisciplinaTurmaVORemover(), getUsuarioLogado());
//				getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().excluirComBaseNaProgramacaoAula(getProfessorTitularDisciplinaTurmaVORemover().getTurma().getCodigo(), getProfessorTitularDisciplinaTurmaVORemover().getProfessor().getPessoa().getCodigo(), getProfessorTitularDisciplinaTurmaVORemover().getDisciplina().getCodigo(), getProfessorTitularDisciplinaTurmaVORemover().getSemestre(), getProfessorTitularDisciplinaTurmaVORemover().getAno(), getUsuario());
				try {
					getFacadeFactory().getProfessorTitularDisciplinaTurmaFacade().consultarPorChavePrimaria(getProfessorTitularDisciplinaTurmaVORemover().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuario());
				} catch (Exception e) {
					getListaProfessoresTitularDisciplinaTurma().removeIf(prof -> prof.getCodigo().equals(getProfessorTitularDisciplinaTurmaVORemover().getCodigo()));
					setMensagemID("msg_dados_gravados");
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		} else {
			getListaProfessoresTitularDisciplinaTurma().removeIf(prof -> prof.getProfessor().getPessoa().getCodigo().equals(getProfessorTitularDisciplinaTurmaVORemover().getProfessor().getPessoa().getCodigo()));
		}
	}
	
	public void adicionarProfessor() {
		try {
			PessoaVO professorAdicionar = (PessoaVO) context().getExternalContext().getRequestMap().get("professorVOItens");
			if (Uteis.isAtributoPreenchido(professorAdicionar)) {
				Predicate<ProfessorTitularDisciplinaTurmaVO> isProfessorJaAdicionado = p -> p.getProfessor().getPessoa().getCodigo().equals(professorAdicionar.getCodigo());
				if (getListaProfessoresTitularDisciplinaTurma().stream().noneMatch(isProfessorJaAdicionado)) {
					ProfessorTitularDisciplinaTurmaVO professorTitularAdicionar = new ProfessorTitularDisciplinaTurmaVO();
					professorTitularAdicionar.setTurma(getProfessorTitularDisciplinaTurmaVO().getTurma());
					professorTitularAdicionar.setDisciplina(getProfessorTitularDisciplinaTurmaVO().getDisciplina());
					professorTitularAdicionar.setAno(getProfessorTitularDisciplinaTurmaVO().getAno());
					professorTitularAdicionar.setSemestre(getProfessorTitularDisciplinaTurmaVO().getSemestre());
					professorTitularAdicionar.setDisciplinaEquivalenteTurmaAgrupadaVO(getProfessorTitularDisciplinaTurmaVO().getDisciplinaEquivalenteTurmaAgrupadaVO());
					professorTitularAdicionar.setProfessor(getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(professorAdicionar.getCodigo(), 0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
					professorTitularAdicionar.setNovoObj(Boolean.TRUE);
					professorTitularAdicionar.setTitular(Boolean.FALSE);
					professorTitularAdicionar.setCursoVO(getProfessorTitularDisciplinaTurmaVO().getCursoVO());
					professorTitularAdicionar.setResponsavelVO(getUsuarioLogadoClone());
					professorTitularAdicionar.setTipoDefinicaoProfessor(getProfessorTitularDisciplinaTurmaVO().getTipoDefinicaoProfessor());
					getListaProfessoresTitularDisciplinaTurma().add(professorTitularAdicionar);
					validarProgramacaoAulaProfessorTitularDisciplinaTurma();
				} else {
					throw new ConsistirException("O professor: " + professorAdicionar.getNome() + " já está adicionado à lista.");
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			setListaConsultaProfessor(new ArrayList(0));
			setValorConsultaProfessor("");
		}
	}
	
	public Boolean getApresentarBotaoConsultarAdicionarProfessores() {
		return Uteis.isAtributoPreenchido(getProfessorTitularDisciplinaTurmaVO().getTurma().getCodigo()) || Uteis.isAtributoPreenchido(getProfessorTitularDisciplinaTurmaVO().getCursoVO().getCodigo());
	}
	
	private void validarProgramacaoAulaProfessorTitularDisciplinaTurma() throws Exception {
		for (ProfessorTitularDisciplinaTurmaVO p : getListaProfessoresTitularDisciplinaTurma()) {
			p.setPossuiProgramacaoAula(getFacadeFactory().getHorarioTurmaDiaFacade()
					.consultarExistenciaProgramacaoAulaPorCursoTurmaProfessorData(0, 0, p.getTurma().getCodigo(), 
					p.getProfessor().getPessoa().getCodigo(), false, false, p.getAno(), p.getSemestre(), 
					p.getDisciplina().getCodigo(), false, getUsuario()));
		}
	}
	
	public void consultarCurso() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCurso().equals("nome")) {
                objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNome(getValorConsultaCurso(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, false, getUsuarioLogado());
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
            CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
            getProfessorTitularDisciplinaTurmaVO().setCursoVO(obj);
            getListaConsultaTurma().clear();
        } catch (Exception e) {
        }
    }
	
	public void limparCurso() throws Exception {
        try {
            getProfessorTitularDisciplinaTurmaVO().setCursoVO(null);
        } catch (Exception e) {
        }
    }
	
	public List getTipoConsultaComboCurso() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        return itens;
    }
	
	public void consultarDisciplina() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaDisciplina().equals("codigo")) {
				if (getValorConsultaDisciplina().equals("")) {
					setValorConsultaDisciplina("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaDisciplina());
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplina().equals("nome")) {
				objs = getFacadeFactory().getDisciplinaFacade().consultarPorNome(getValorConsultaDisciplina(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDisciplinaVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplinaVOs(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void selecionarDisciplina() throws Exception {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaItens");
			getProfessorTitularDisciplinaTurmaVO().setDisciplina(obj);
		} catch (Exception e) {
		}
	}
	
	public void limparDisciplina() {
        try {
        	getProfessorTitularDisciplinaTurmaVO().setDisciplina(null);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
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
			listaConsultaCurso = new ArrayList<CursoVO>(0);
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}
	
	public List getTipoComboTipoDefinicaoProfessor() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("TURMA", "Turma"));
		itens.add(new SelectItem("CURSO", "Curso"));
		return itens;
	}

	
	public Boolean getIsTipoDefinicaoTipoTurma() {
		return getProfessorTitularDisciplinaTurmaVO().getTipoDefinicaoProfessor().equals("TURMA");
	}
	
	public Boolean getIsTipoDefinicaoTipoCurso() {
		return getProfessorTitularDisciplinaTurmaVO().getTipoDefinicaoProfessor().equals("CURSO");
	}
	
	public Boolean getApresentarAnoBaseadoPeriodicidadeCurso() {
		return getProfessorTitularDisciplinaTurmaVO().getCursoVO().getPeriodicidade().equals("AN") || getProfessorTitularDisciplinaTurmaVO().getCursoVO().getPeriodicidade().equals("SE"); 
	}
	
	public Boolean getApresentarSemestreBaseadoPeriodicidadeCurso() {
		return getProfessorTitularDisciplinaTurmaVO().getCursoVO().getPeriodicidade().equals("SE"); 
	}

	public String getCampoConsultaDisciplina() {
		if (campoConsultaDisciplina == null) {
			campoConsultaDisciplina = "";
		}
		return campoConsultaDisciplina;
	}

	public void setCampoConsultaDisciplina(String campoConsultaDisciplina) {
		this.campoConsultaDisciplina = campoConsultaDisciplina;
	}

	public String getValorConsultaDisciplina() {
		if (valorConsultaDisciplina == null) {
			valorConsultaDisciplina = "";
		}
		return valorConsultaDisciplina;
	}

	public void setValorConsultaDisciplina(String valorConsultaDisciplina) {
		this.valorConsultaDisciplina = valorConsultaDisciplina;
	}

	public List<DisciplinaVO> getListaConsultaDisciplinaVOs() {
		if (listaConsultaDisciplinaVOs == null) {
			listaConsultaDisciplinaVOs = new ArrayList<DisciplinaVO>(0);
		}
		return listaConsultaDisciplinaVOs;
	}

	public void setListaConsultaDisciplinaVOs(List<DisciplinaVO> listaConsultaDisciplinaVOs) {
		this.listaConsultaDisciplinaVOs = listaConsultaDisciplinaVOs;
	}
	
	public List getTipoConsultaComboDisciplina() {
        if (tipoConsultaComboDisciplina == null) {
            tipoConsultaComboDisciplina = new ArrayList<SelectItem>(0);
            tipoConsultaComboDisciplina.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboDisciplina.add(new SelectItem("codigo", "Código"));
        }
        return tipoConsultaComboDisciplina;
    }
	
	public void limparCamposTipoDefinicao() {
		if (getProfessorTitularDisciplinaTurmaVO().getTipoDefinicaoProfessor().equals("TURMA")) {
			getProfessorTitularDisciplinaTurmaVO().setCursoVO(null);
		} else {
			getProfessorTitularDisciplinaTurmaVO().setTurma(null);
		}
		getProfessorTitularDisciplinaTurmaVO().setDisciplina(null);
		getListaProfessoresTitularDisciplinaTurma().clear();
	}

}
