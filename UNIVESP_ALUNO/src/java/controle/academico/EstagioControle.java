package controle.academico;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import jakarta.faces.model.SelectItem;
import jakarta.servlet.http.HttpServletRequest;


import org.primefaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.DisciplinaVO;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.academico.ImpressaoContratoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoFuncionarioVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.PeriodicidadeEnum;
import negocio.comuns.academico.enumeradores.TipoEstagioEnum;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;

import negocio.comuns.basico.PessoaVO;

import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import relatorio.controle.arquitetura.SuperControleRelatorio;

/**
 * 
 * @author marco tulio
 * 
 */
@Controller("EstagioControle")
@Scope("viewScope")
@Lazy
public class EstagioControle extends SuperControleRelatorio {

	private String nomeUsuarioLiberar;
	private String senhaUsuarioLiberar;
	private Integer totalCargaHorariaSemanal;
	private Integer totalCargaHorariaDiaria;
	private List<EstagioVO> listaEstagioConcomitantes;
	private String resultadoValidacao;

	private static final long serialVersionUID = 1L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private String ano;
	private String semestre;
	private List<MatriculaVO> listaConsultaAluno;
	private String valorConsultaAluno;
	private String campoConsultaAluno;
	private MatriculaVO matriculaVO;
	private EstagioVO estagioVO;
	private List<EstagioVO> listaEstagios;
	private List<EstagioVO> listaEstagiosRemover;
	private List<TurmaVO> listaConsultaTurma;
	private String valorConsultaTurma;
	private String campoConsultaTurma;
	private List<CursoVO> listaConsultaCurso;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private TurmaVO turmaVO;
	private CursoVO cursoVO;

	protected String valorConsultaParceiro;
	protected String campoConsultaParceiro;
	private String resultadoConsultaParceiro;

	private String valorConsultaAreaProfissional;
	private String campoConsultaAreaProfissional;

	private String valorConsultaProfessor;
	private String campoConsultaProfessor;
	private List<PessoaVO> listaConsultaProfessor;

	private String valorConsultaDisciplinaMatricula;
	private String campoConsultaDisciplinaMatricula;
	private List<DisciplinaVO> listaConsultaDisciplinaMatricula;

	private List<TextoPadraoDeclaracaoVO> listaContratosEstagio;
	private EstagioVO estagioImprimirVO;
	private Boolean imprimirContrato;
	private Boolean isDownloadContrato;
	private ArquivoVO arquivoVO;
	private List<SelectItem> listaSelectItemDisciplina;
	private Boolean permiteImpressaoContratoEstagioObrigatorio;
	private Boolean permiteImpressaoContratoEstagioNaoObrigatorio;
	private Boolean permiteInformarSeguradoraEstagio;

	public EstagioControle() throws Exception {
		setImprimirContrato(Boolean.FALSE);
		montarListaSelectItemUnidadeEnsino();
		verificarPermitirInformarSeguradoraEstagio();
		setMensagemID("msg_dados_parametroConsulta");
	}

	public String novo() throws Exception {
		setEstagioVO(null);
		getListaEstagios().clear();
		getListaEstagiosRemover().clear();
		setMatriculaVO(null);
		setUnidadeEnsinoVO(null);
		getListaSelectItemUnidadeEnsino().clear();
		
		getListaConsultaAluno().clear();
		montarListaSelectItemUnidadeEnsino();
		alterarTipoEstagio();
		getListaSelectItemDisciplina().clear();
		getListaSelectItemAreaProfissional().clear();		
		setImprimirContrato(Boolean.FALSE);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("estagioForm.xhtml");
	}
	
	public String novoVisaoCoordenador() throws Exception {
		setEstagioVO(null);
		getListaEstagios().clear();
		getListaEstagiosRemover().clear();
		setMatriculaVO(null);
		setUnidadeEnsinoVO(null);
		getListaSelectItemUnidadeEnsino().clear();
		
		getListaConsultaAluno().clear();
		montarListaSelectItemUnidadeEnsino();
		alterarTipoEstagio();
		getListaSelectItemDisciplina().clear();
		getListaSelectItemAreaProfissional().clear();
		setImprimirContrato(Boolean.FALSE);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("estagioCoordenadorForm.xhtml");
	}

	public String excluir() {
		try {
			if (!getListaEstagios().isEmpty()) {
				
				novo();
				setMensagemID("msg_dados_excluidos");
			} else {
				novo();
				setMensagemID("msg_dados_naoExisteDadosExcluidos");
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("estagioForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("estagioForm.xhtml");
		}
	}
	
	public String excluirVisaoCoordenador() {
		try {
			if (!getListaEstagios().isEmpty()) {
				
				novo();
				setMensagemID("msg_dados_excluidos");
			} else {
				novo();
				setMensagemID("msg_dados_naoExisteDadosExcluidos");
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("estagioCoordenadorForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("estagioCoordenadorForm.xhtml");
		}
	}

	public String inicializarConsultar() throws Exception {
		setEstagioVO(new EstagioVO());
		getListaConsulta().clear();
		getControleConsultaOtimizado().getListaConsulta().clear();
		limparDadosAluno();
		limparDadosCurso();
		limparDadosTurma();
		setMensagemID("msg_dados_parametroConsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("estagioCons.xhtml");
	}
	
	public String inicializarConsultarVisaoCoordenador() throws Exception {
		setEstagioVO(new EstagioVO());
		getListaConsulta().clear();
		getControleConsultaOtimizado().getListaConsulta().clear();
		limparDadosAluno();
		limparDadosCurso();
		limparDadosTurma();
		setMensagemID("msg_dados_parametroConsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("estagioCoordenadorCons.xhtml");
	}

	public void adicionarEstagio() {
		try {
			
			validarDadosAdicionarEstagio(getUnidadeEnsinoVO(), getEstagioVO(), getMatriculaVO());
			
			
			setEstagioVO(new EstagioVO());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

//	public String getCaminhoServidorDownload() {
//		try {
//			EstagioVO obj = (EstagioVO) context().getExternalContext().getRequestMap().get("estagioItens");
//			return getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(obj.getArquivo(), PastaBaseArquivoEnum.ESTAGIO, getConfiguracaoGeralPadraoSistema());
//		} catch (Exception ex) {
//			setMensagemDetalhada("msg_erro", ex.getMessage());
//		}
//		return "";
//	}

	public void irPaginaInicial() throws Exception {
		getControleConsultaOtimizado().setPaginaAtual(1);
		if (getUsuarioLogado().getVisaoLogar().equals("coordenador")) {
			consultarVisaoCoordenador();
		}else {
			consultar();
		}
		
	}

	public void scrollerListener() throws Exception {
		
		consultar();
	}

	public String consultar() {
		try {
			if (getUnidadeEnsinoVO().getCodigo() != 0) {
				getControleConsultaOtimizado().getListaConsulta().clear();
				List<EstagioVO> objs = new ArrayList<EstagioVO>(0);
				getControleConsultaOtimizado().setLimitePorPagina(10);
				
				getControleConsultaOtimizado().setListaConsulta(objs);
				if (getControleConsultaOtimizado().getListaConsulta().isEmpty()) {
					setMensagemID("msg_erro_dadosnaoencontrados");
				} else {
					setMensagemID("msg_dados_consultados");
				}

			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("estagioCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<EstagioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("estagioCons.xhtml");
		}
	}
	
	public String consultarVisaoCoordenador() {
		try {
			if (getUnidadeEnsinoVO().getCodigo() != 0) {
				getControleConsultaOtimizado().getListaConsulta().clear();
				List<EstagioVO> objs = new ArrayList<EstagioVO>(0);
				getControleConsultaOtimizado().setLimitePorPagina(10);

				getControleConsultaOtimizado().setListaConsulta(objs);
				if (getControleConsultaOtimizado().getListaConsulta().isEmpty()) {
					setMensagemID("msg_erro_dadosnaoencontrados");
				} else {
					setMensagemID("msg_dados_consultados");
				}

			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
			return Uteis.getCaminhoRedirecionamentoNavegacao("estagioCoordenadorCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<EstagioVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("estagioCoordenadorCons.xhtml");
		}
	}


	public String gravar() {
			
		try {
			
			if (getListaEstagios().isEmpty() && getListaEstagiosRemover().isEmpty()) {
				throw new Exception("Não há itens para efetuar a gravação");
			}
			executarValidacaoSimulacaoVisaoCoordenador();			
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("estagioForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("estagioForm.xhtml");
		}
	}
	
	public String gravarVisaoCoordenador() {
		
		try {
			
			if (getListaEstagios().isEmpty() && getListaEstagiosRemover().isEmpty()) {
				throw new Exception("Não há itens para efetuar a gravação");
			}
			executarValidacaoSimulacaoVisaoCoordenador();			
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("estagioCoordenadorForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("estagioCoordenadorForm.xhtml");
		}
	}

	public void validarDadosAdicionarEstagio(UnidadeEnsinoVO unidadeEnsino, EstagioVO estagio, MatriculaVO matricula) throws Exception {		
		setResultadoValidacao("");
		try {
			if (unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
				throw new Exception("Por favor informe uma UNIDADE DE ENSINO antes de adicionar o Estágio");
			}
			//getFacadeFactory().getEstagioFacade().validarDados(estagio, getConfiguracaoGeralPadraoSistema());			
			
			if (listaEstagioConcomitantes != null) {
				StringBuilder erro = new StringBuilder("");
				erro.append("Estágio não pode ser registrado para o aluno.");
				totalCargaHorariaDiaria = obterTotalCargaHorariaDiaria(listaEstagioConcomitantes);
				if (getConfiguracaoGeralPadraoSistema().getCargaHorariaMaximaDiaria() > 0) {
					int totalCargaHorariaDiariaDisponivel = getConfiguracaoGeralPadraoSistema().getCargaHorariaMaximaDiaria() - totalCargaHorariaDiaria;
					if (getEstagioVO().getCargaHorariaDiaria().compareTo(totalCargaHorariaDiariaDisponivel) > 0) {
						// ultrapassou o limite diario de horas de estagio
						erro.append(" O mesmo está com carga horária diária de ").append(getEstagioVO().getCargaHorariaDiaria()).append("h, o que ultrapassa a quantidade de horas disponíveis para o dia de ").append(totalCargaHorariaDiariaDisponivel).append("h. Limite Diário: ").append(getConfiguracaoGeralPadraoSistema().getCargaHorariaMaximaDiaria()).append("h. ");
					}
				}
				
				setResultadoValidacao("PF('panelErroCargaHorariaMaxima').show()");
				throw new Exception(erro.toString());
			}
		} catch (Exception e) {			
			throw new Exception(e.getMessage());
		}

	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboSemestre() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("1", "1"));
		itens.add(new SelectItem("2", "2"));
		return itens;
	}

	public void selecionarAlunoCons() throws Exception {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
			setMatriculaVO(objCompleto);
			getUnidadeEnsinoVO().setCodigo(getMatriculaVO().getUnidadeEnsino().getCodigo());
			setMensagemDetalhada("");
			setCursoVO(new CursoVO());
			setTurmaVO(new TurmaVO());
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarAluno() throws Exception {
		try {
			getListaEstagios().clear();			
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
			MatriculaVO objCompleto = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula(), obj.getUnidadeEnsino().getCodigo(), NivelMontarDados.TODOS, getUsuarioLogado());
			setMatriculaVO(objCompleto);			
			getUnidadeEnsinoVO().setCodigo(getMatriculaVO().getUnidadeEnsino().getCodigo());
			//getListaEstagios().addAll(getFacadeFactory().getEstagioFacade().consultarPorMatriculaAluno(getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			inicializarDadosAnoSemestreDeAcordoUltimaMatriculaPeriodo();
			montarListaSelectItemDisciplina();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCurso() throws Exception {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			obj = getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTA, false, getUsuarioLogado());
			setCursoVO(obj);
			setTurmaVO(new TurmaVO());
			setMatriculaVO(new MatriculaVO());
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarTurma() throws Exception {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			setTurmaVO(obj);
			setCursoVO(new CursoVO());
			setMatriculaVO(new MatriculaVO());
			getListaConsultaTurma().clear();
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setTurmaVO(new TurmaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAluno() {
		try {
			List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
			if (getValorConsultaAluno().equals("")) {
				throw new Exception("Deve ser informado pelo menos um valor para realizar a consulta.");
			}
			if (getCampoConsultaAluno().equals("matricula")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomePessoa")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			}
			if (getCampoConsultaAluno().equals("nomeCurso")) {
				objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomeCurso(getValorConsultaAluno(), this.getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
			}
			setListaConsultaAluno(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatriculaForm() {
		try {
			getListaEstagios().clear();
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), getUnidadeEnsinoVO().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			getUnidadeEnsinoVO().setCodigo(getMatriculaVO().getUnidadeEnsino().getCodigo());
			//getListaEstagios().addAll(getFacadeFactory().getEstagioFacade().consultarPorMatriculaAluno(getMatriculaVO().getMatricula(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setCursoVO(new CursoVO());
			setTurmaVO(new TurmaVO());
			inicializarDadosAnoSemestreDeAcordoUltimaMatriculaPeriodo();
			montarListaSelectItemDisciplina();
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMatriculaVO(null);			
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarAlunoPorMatricula() {
		try {
			MatriculaVO objAluno = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(getMatriculaVO().getMatricula(), getUnidadeEnsinoVO().getCodigo(), NivelMontarDados.BASICO, getUsuarioLogado());
			if (objAluno.getMatricula().equals("")) {
				throw new Exception("Aluno de matrícula " + getMatriculaVO().getMatricula() + " não encontrado. Verifique se o número de matrícula está correto.");
			}
			setMatriculaVO(objAluno);
			getUnidadeEnsinoVO().setCodigo(getMatriculaVO().getUnidadeEnsino().getCodigo());
			setCursoVO(new CursoVO());
			setTurmaVO(new TurmaVO());
			setMensagemDetalhada("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void consultarTurma() {
		try {
			List<TurmaVO> objs = new ArrayList<TurmaVO>(0);
			if (getUnidadeEnsinoVO().getCodigo() != 0) {
				if (getCampoConsultaTurma().equals("identificadorTurma")) {
					objs = getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurmaUnidadeEnsinoCursoTurno(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), 0, 0, false, getUsuarioLogado());
				}
				if (getCampoConsultaTurma().equals("nomeCurso")) {
					objs = getFacadeFactory().getTurmaFacade().consultarPorNomeCurso(getValorConsultaTurma(), getUnidadeEnsinoVO().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				setListaConsultaTurma(objs);
				setMensagemID("msg_dados_consultados");
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
		} catch (Exception e) {
			setListaConsultaTurma(new ArrayList<TurmaVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void consultarCurso() {
		try {
			List<CursoVO> objs = new ArrayList<CursoVO>(0);
			if (getUnidadeEnsinoVO().getCodigo() != 0) {
				if (getCampoConsultaCurso().equals("nome")) {
					objs = getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(getValorConsultaCurso(), getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				}
				setListaConsultaCurso(objs);
				setMensagemID("msg_dados_consultados");
			} else {
				throw new Exception("Por Favor Informe a Unidade de Ensino.");
			}
		} catch (Exception e) {
			setListaConsultaCurso(new ArrayList<CursoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	

	

	public String editar() throws Exception {
		try {
			getListaEstagios().clear();
			getListaSelectItemAreaProfissional().clear();
			EstagioVO obj = (EstagioVO) context().getExternalContext().getRequestMap().get("estagioItens");
			obj.setNovoObj(Boolean.FALSE);
			consultarAlunoPorMatricula();
			setImprimirContrato(Boolean.FALSE);
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("estagioForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("estagioForm.xhtml");
		}

	}
	
	public String editarVisaoCoordenador() throws Exception {
		try {
			getListaEstagios().clear();
			getListaSelectItemAreaProfissional().clear();
			EstagioVO obj = (EstagioVO) context().getExternalContext().getRequestMap().get("estagioItens");
			obj.setNovoObj(Boolean.FALSE);
			
			
			consultarAlunoPorMatricula();
			
			
			setImprimirContrato(Boolean.FALSE);
			setMensagemID("msg_dados_editar");
			return Uteis.getCaminhoRedirecionamentoNavegacao("estagioCoordenadorForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("estagioCoordenadorForm.xhtml");
		}

	}

	public void editarLista() {
		EstagioVO obj = (EstagioVO) context().getExternalContext().getRequestMap().get("estagioItens");
		setEstagioVO(obj);
		consultarAlunoPorMatricula();
		montarListaSelectItemDisciplina();
	}
	

	public void alterarUnidadeEnsino() {
		getListaEstagios().clear();
		getListaEstagiosRemover().clear();
		setMatriculaVO(null);
	}

	public void removerEstagioLista() {
		try {
			EstagioVO obj = (EstagioVO) context().getExternalContext().getRequestMap().get("estagioItens");
			getListaEstagiosRemover().add(obj);
			getListaEstagios().remove(obj);			
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("", ""));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turma", "Turma"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboAluno() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nomePessoa", "Aluno"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
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
			// System.out.println(e.getMessage());
		}
	}

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List<UnidadeEnsinoVO> resultadoConsulta = null;
		Iterator<UnidadeEnsinoVO> i = null;
		try {
			resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(prm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List<SelectItem> objs = new ArrayList<SelectItem>(0);
			if (super.getUnidadeEnsinoLogado().getCodigo().equals(0)) {
				objs.add(new SelectItem(0, ""));
			}
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));

			}
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
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

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

	public List<MatriculaVO> getListaConsultaAluno() {
		if (listaConsultaAluno == null) {
			listaConsultaAluno = new ArrayList<MatriculaVO>(0);
		}
		return listaConsultaAluno;
	}

	public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
		this.listaConsultaAluno = listaConsultaAluno;
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

	public String getCampoConsultaAluno() {
		if (campoConsultaAluno == null) {
			campoConsultaAluno = "";
		}
		return campoConsultaAluno;
	}

	public void setCampoConsultaAluno(String campoConsultaAluno) {
		this.campoConsultaAluno = campoConsultaAluno;
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

	public EstagioVO getEstagioVO() {
		if (estagioVO == null) {
			estagioVO = new EstagioVO();
		}
		return estagioVO;
	}

	public void setEstagioVO(EstagioVO estagioVO) {
		this.estagioVO = estagioVO;
	}

	public List<EstagioVO> getListaEstagios() {
		if (listaEstagios == null) {
			listaEstagios = new ArrayList<EstagioVO>(0);
		}
		return listaEstagios;
	}

	public void setListaEstagios(List<EstagioVO> listaEstagios) {
		this.listaEstagios = listaEstagios;
	}

	public List<EstagioVO> getListaEstagiosRemover() {
		if (listaEstagiosRemover == null) {
			listaEstagiosRemover = new ArrayList<EstagioVO>(0);
		}
		return listaEstagiosRemover;
	}

	public void setListaEstagiosRemover(List<EstagioVO> listaEstagiosRemover) {
		this.listaEstagiosRemover = listaEstagiosRemover;
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

	public List<CursoVO> getListaConsultaCurso() {
		if (listaConsultaCurso == null) {
			listaConsultaCurso = new ArrayList<CursoVO>();
		}
		return listaConsultaCurso;
	}

	public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
		this.listaConsultaCurso = listaConsultaCurso;
	}

	public CursoVO getCursoVO() {
		if (cursoVO == null) {
			cursoVO = new CursoVO();
		}
		return cursoVO;
	}

	public void setCursoVO(CursoVO cursoVO) {
		this.cursoVO = cursoVO;
	}

	public Boolean getApresentarCampoConsultaMatricula() {
		if (getControleConsulta().getCampoConsulta().equals("matricula")) {
			return Boolean.TRUE;
		}
		setMatriculaVO(new MatriculaVO());
		return Boolean.FALSE;
	}

	public void limparDadosAluno() throws Exception {
		removerObjetoMemoria(getMatriculaVO());
		removerObjetoMemoria(getEstagioVO());
		setUnidadeEnsinoVO(null);
		getListaSelectItemDisciplina().clear();
		setMensagemID("msg_entre_dados");
	}

	public void limparDadosCurso() throws Exception {
		removerObjetoMemoria(getCursoVO());
		setMensagemID("msg_entre_dados");
	}

	public void limparDadosTurma() throws Exception {
		removerObjetoMemoria(getTurmaVO());
		setMensagemID("msg_entre_dados");
	}

	public Boolean getApresentarCampoConsultaCurso() {
		if (getControleConsulta().getCampoConsulta().equals("curso")) {
			return Boolean.TRUE;
		}
		setCursoVO(new CursoVO());
		return Boolean.FALSE;
	}

	public Boolean getApresentarCampoConsultaTurma() {
		if (getControleConsulta().getCampoConsulta().equals("turma")) {
			return Boolean.TRUE;
		}
		setTurmaVO(new TurmaVO());
		return Boolean.FALSE;
	}

	public Boolean getMostrarCampoAno() {
		if (getCursoVO().getCodigo() != null && getCursoVO().getCodigo() != 0) {
			return (getCursoVO().getPeriodicidade().equals("SE") || getCursoVO().getPeriodicidade().equals("AN"));
		}
		if (getTurmaVO().getCodigo() != null && getTurmaVO().getCodigo() != 0) {
			return (getTurmaVO().getCurso().getPeriodicidade().equals("SE") || getTurmaVO().getCurso().getPeriodicidade().equals("AN"));
		}
		return Boolean.FALSE;
	}

	public Boolean getMostrarCampoSemestre() {
		if (getCursoVO().getCodigo() != null && getCursoVO().getCodigo() != 0) {
			return getCursoVO().getPeriodicidade().equals("SE");
		}
		if (getTurmaVO().getCodigo() != null && getTurmaVO().getCodigo() != 0) {
			return getTurmaVO().getCurso().getPeriodicidade().equals("SE");
		}
		return Boolean.FALSE;
	}

	public void montarTurma() throws Exception {
		try {
			if (!getTurmaVO().getIdentificadorTurma().equals("")) {
				if (getUnidadeEnsinoVO().getCodigo().intValue() == 0) {
					throw new Exception("Por Favor Informe a Unidade de Ensino.");
				} else {
					setTurmaVO(getFacadeFactory().getTurmaFacade().consultarTurmaPorIdentificadorTurma(getTurmaVO().getIdentificadorTurma(), getUnidadeEnsinoLogado().getCodigo().intValue(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
				}
			} else {
				throw new Exception("Informe a Turma.");
			}
			if (getTurmaVO().getCodigo() != 0) {
				setMensagemID("msg_dados_consultados");
			} else {
				setMensagemID("Dados não encontratos");
			}
		} catch (Exception e) {
			setTurmaVO(null);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getListaSelectItemTipoEstagio() {
		return TipoEstagioEnum.getCombobox();
	}

	public List getTipoConsultaComboParceiro() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("RG", "RG"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
		return itens;
	}

	/**
	 * @return the valorConsultaParceiro
	 */
	public String getValorConsultaParceiro() {
		if (valorConsultaParceiro == null) {
			valorConsultaParceiro = "";
		}
		return valorConsultaParceiro;
	}

	/**
	 * @param valorConsultaParceiro
	 *            the valorConsultaParceiro to set
	 */
	public void setValorConsultaParceiro(String valorConsultaParceiro) {
		this.valorConsultaParceiro = valorConsultaParceiro;
	}

	/**
	 * @return the campoConsultaParceiro
	 */
	public String getCampoConsultaParceiro() {
		if (campoConsultaParceiro == null) {
			campoConsultaParceiro = "";
		}
		return campoConsultaParceiro;
	}

	/**
	 * @param campoConsultaParceiro
	 *            the campoConsultaParceiro to set
	 */
	public void setCampoConsultaParceiro(String campoConsultaParceiro) {
		this.campoConsultaParceiro = campoConsultaParceiro;
	}

	/**
	 * @return the listaConsultaParceiro
	 */
	

	public void limparParceiro() {
		getListaSelectItemAreaProfissional().clear();
		limparMensagem();
	}

	

	

	/**
	 * @return the resultadoConsultaParceiro
	 */
	public String getResultadoConsultaParceiro() {
		if (resultadoConsultaParceiro == null) {
			resultadoConsultaParceiro = "";
		}
		return resultadoConsultaParceiro;
	}

	/**
	 * @param resultadoConsultaParceiro
	 *            the resultadoConsultaParceiro to set
	 */
	public void setResultadoConsultaParceiro(String resultadoConsultaParceiro) {
		this.resultadoConsultaParceiro = resultadoConsultaParceiro;
	}

	/**
	 * @return the valorConsultaAreaProfissional
	 */
	public String getValorConsultaAreaProfissional() {
		if (valorConsultaAreaProfissional == null) {
			valorConsultaAreaProfissional = "";
		}
		return valorConsultaAreaProfissional;
	}

	/**
	 * @param valorConsultaAreaProfissional
	 *            the valorConsultaAreaProfissional to set
	 */
	public void setValorConsultaAreaProfissional(String valorConsultaAreaProfissional) {
		this.valorConsultaAreaProfissional = valorConsultaAreaProfissional;
	}

	/**
	 * @return the campoConsultaAreaProfissional
	 */
	public String getCampoConsultaAreaProfissional() {
		if (campoConsultaAreaProfissional == null) {
			campoConsultaAreaProfissional = "descricao";
		}
		return campoConsultaAreaProfissional;
	}

	/**
	 * @param campoConsultaAreaProfissional
	 *            the campoConsultaAreaProfissional to set
	 */
	public void setCampoConsultaAreaProfissional(String campoConsultaAreaProfissional) {
		this.campoConsultaAreaProfissional = campoConsultaAreaProfissional;
	}

	/**
	 * @return the listaConsultaAreaProfissional
	 */
	

	public List getTipoConsultaComboAreaProfissional() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		return itens;
	}

	public void limparAreaProfissional() {
		limparMensagem();
	}

	

	

	public List getTipoConsultaComboProfessor() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("cpf", "CPF"));
		return itens;
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
			setListaConsultaProfessor(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarProfessor() {
		try {
			PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("professorItens");
			
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparProfessor() {		
		limparMensagem();
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
	 * @return the campoConsultaProfessor
	 */
	public String getCampoConsultaProfessor() {
		if (campoConsultaProfessor == null) {
			campoConsultaProfessor = "nome";
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
	 * @return the listaConsultaProfessor
	 */
	public List<PessoaVO> getListaConsultaProfessor() {
		if (listaConsultaProfessor == null) {
			listaConsultaProfessor = new ArrayList<PessoaVO>(0);
		}
		return listaConsultaProfessor;
	}

	/**
	 * @param listaConsultaProfessor
	 *            the listaConsultaProfessor to set
	 */
	public void setListaConsultaProfessor(List<PessoaVO> listaConsultaProfessor) {
		this.listaConsultaProfessor = listaConsultaProfessor;
	}

	public Boolean getApresentarCampoConsultaUnidadeEnsino() {
		if (getControleConsulta().getCampoConsulta().equals("matricula")) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	public String getURLImprimirContrato() {
		if (getImprimirContrato()) {
			return "abrirPopup('../../VisualizarContrato', 'RelatorioContrato', 730, 545);";
		}
		return "";
	}

	public ImpressaoContratoVO prepararImpressaoContratoEstagio() throws Exception {
		ImpressaoContratoVO impressaoContratoVO = new ImpressaoContratoVO();
		
		
		if (!getEstagioImprimirVO().getDisciplina().getCodigo().equals(0)) {
			getEstagioImprimirVO().setDisciplina(getFacadeFactory().getDisciplinaFacade().consultarPorChavePrimaria(getEstagioImprimirVO().getDisciplina().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
		}
		
		
		impressaoContratoVO.setEstagioVO(getEstagioImprimirVO());
		
		return impressaoContratoVO;
	}
	
	public void imprimirTextoPadraoEstagioHtml() {
		try {
			limparMensagem();
			setImprimirContrato(Boolean.FALSE);
			this.setCaminhoRelatorio("");
			TextoPadraoDeclaracaoVO textoPadraoImprimirVO = (TextoPadraoDeclaracaoVO) context().getExternalContext().getRequestMap().get("textoPadraoItens");
			validarDadosPermissaoImpressaoContrato(textoPadraoImprimirVO);						
			ImpressaoContratoVO impressaoContratoVO = prepararImpressaoContratoEstagio();
			impressaoContratoVO.setGerarNovoArquivoAssinado(false);
			impressaoContratoVO.setImpressaoPdf(false);
//			this.setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(textoPadraoImprimirVO.getCodigo(), impressaoContratoVO, impressaoContratoVO, textoPadraoImprimirVO.getTipo(), impressaoContratoVO.getMatriculaPeriodoVO().getTurma(), impressaoContratoVO.getDisciplinaVO(),  getUsuarioLogado(), true));
			if(Uteis.isAtributoPreenchido(getCaminhoRelatorio())){
				setImprimirContrato(true);	
			}
			setFazerDownload(false);
			setMensagemID("msg_impressaoContrato_contratoDeclaracao");
		} catch (Exception e) {
			setImprimirContrato(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirTextoPadraoEstagio() {
		try {
			limparMensagem();
			setFazerDownload(false);
			this.setCaminhoRelatorio("");
			TextoPadraoDeclaracaoVO textoPadraoImprimirVO = (TextoPadraoDeclaracaoVO) context().getExternalContext().getRequestMap().get("textoPadraoItens");
			validarDadosPermissaoImpressaoContrato(textoPadraoImprimirVO);			
			ImpressaoContratoVO impressaoContratoVO = prepararImpressaoContratoEstagio();
			impressaoContratoVO.setGerarNovoArquivoAssinado(false);
			impressaoContratoVO.setImpressaoPdf(true);
//			this.setCaminhoRelatorio(getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(textoPadraoImprimirVO.getCodigo(), impressaoContratoVO, impressaoContratoVO, textoPadraoImprimirVO.getTipo(), impressaoContratoVO.getMatriculaPeriodoVO().getTurma(), impressaoContratoVO.getDisciplinaVO(),  getUsuarioLogado(), true));
			setFazerDownload(true);		
			setMensagemID("msg_impressaoContrato_contratoDeclaracao");
		} catch (Exception e) {
			setImprimirContrato(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirTextoPadraoEstagioDOC() {
		try {
			TextoPadraoDeclaracaoVO textoPadraoImprimirVO = (TextoPadraoDeclaracaoVO) context().getExternalContext().getRequestMap().get("textoPadraoItens");
			validarDadosPermissaoImpressaoContrato(textoPadraoImprimirVO);			
			ImpressaoContratoVO impressaoContratoVO = prepararImpressaoContratoEstagio();
			impressaoContratoVO.setGerarNovoArquivoAssinado(true);
//			String arquivo = (getFacadeFactory().getImpressaoDeclaracaoFacade().imprimirDeclaracao(textoPadraoImprimirVO.getCodigo(), impressaoContratoVO, impressaoContratoVO, textoPadraoImprimirVO.getTipo(), impressaoContratoVO.getMatriculaPeriodoVO().getTurma(), impressaoContratoVO.getDisciplinaVO(), getUsuarioLogado(), true));
//			setIsDownloadContrato(Uteis.isAtributoPreenchido(arquivo));	

			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			String textoHTML = (String) request.getSession().getAttribute("textoRelatorio");
			if (!textoHTML.contains("<meta http-equiv=\"Content-Type\"")) {
				textoHTML = textoHTML.replaceAll("</head>", "<meta http-equiv=\"Content-Type\" content=\"text/html;charset=UTF-8\"></head>");
			}
			ArquivoHelper.criarArquivoDOC(textoHTML, getArquivoVO(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());

			// getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("EN",
			// getUnidadeEnsinoLogado().getCodigo(), "", false,
			// Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			setMensagemID("msg_impressao_sucesso");
		} catch (Exception e) {
			setImprimirContrato(Boolean.FALSE);
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String getDownloadContrato() {
		try {
			if (getIsDownloadContrato()) {
				context().getExternalContext().getSessionMap().put("nomeArquivo", getArquivoVO().getNome());
				context().getExternalContext().getSessionMap().put("pastaBaseArquivo", getArquivoVO().getPastaBaseArquivo());
				context().getExternalContext().getSessionMap().put("deletarArquivo", Boolean.TRUE);
				return "location.href='../../DownloadSV'";
			}
			return "";
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	public void consultarTextoPadraoEstagio() {
		try {
			super.consultar();
			setEstagioImprimirVO((EstagioVO) context().getExternalContext().getRequestMap().get("estagioItens"));
			List objs = new ArrayList();
			List objsEstagio = new ArrayList();
			if (getEstagioImprimirVO().getTipoEstagio().equals(TipoEstagioEnum.NAO_OBRIGATORIO)) {
				objsEstagio = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("EN", getUnidadeEnsinoLogado().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				objs.addAll(objsEstagio);
			} else {
				objsEstagio = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("EO", getUnidadeEnsinoLogado().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				objs.addAll(objsEstagio);
			}
			objsEstagio = getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("ES", getUnidadeEnsinoLogado().getCodigo(), "", false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			objs.addAll(objsEstagio);
			setListaContratosEstagio(objs);
			if (objs.isEmpty()) {
				throw new Exception("Não existem Textos Padrões registrados para Estágio. Textos padrões para estágio (contratos, termos, declarações) podem ser cadastrados no módulo acadêmico: Texto Padrão Declaração.");
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * @return the listaContratosEstagio
	 */
	public List<TextoPadraoDeclaracaoVO> getListaContratosEstagio() {
		if (listaContratosEstagio == null) {
			listaContratosEstagio = new ArrayList<TextoPadraoDeclaracaoVO>(0);
		}
		return listaContratosEstagio;
	}

	/**
	 * @param listaContratosEstagio
	 *            the listaContratosEstagio to set
	 */
	public void setListaContratosEstagio(List<TextoPadraoDeclaracaoVO> listaContratosEstagio) {
		this.listaContratosEstagio = listaContratosEstagio;
	}

	/**
	 * @return the estagioImprimirVO
	 */
	public EstagioVO getEstagioImprimirVO() {
		return estagioImprimirVO;
	}

	/**
	 * @param estagioImprimirVO
	 *            the estagioImprimirVO to set
	 */
	public void setEstagioImprimirVO(EstagioVO estagioImprimirVO) {
		this.estagioImprimirVO = estagioImprimirVO;
	}

	/**
	 * @return the imprimirContrato
	 */
	public Boolean getImprimirContrato() {
		if (imprimirContrato == null) {
			imprimirContrato = Boolean.FALSE;
		}
		return imprimirContrato;
	}

	/**
	 * @param imprimirContrato
	 *            the imprimirContrato to set
	 */
	public void setImprimirContrato(Boolean imprimirContrato) {
		this.imprimirContrato = imprimirContrato;
	}

	public Boolean getIsDownloadContrato() {
		if (isDownloadContrato == null) {
			isDownloadContrato = Boolean.FALSE;
		}
		return isDownloadContrato;
	}

	public void setIsDownloadContrato(Boolean isDownloadContrato) {
		this.isDownloadContrato = isDownloadContrato;
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

	/**
	 * @return the valorConsultaDisciplinaMatricula
	 */
	public String getValorConsultaDisciplinaMatricula() {
		if (valorConsultaDisciplinaMatricula == null) {
			valorConsultaDisciplinaMatricula = "";
		}
		return valorConsultaDisciplinaMatricula;
	}

	/**
	 * @param valorConsultaDisciplinaMatricula
	 *            the valorConsultaDisciplinaMatricula to set
	 */
	public void setValorConsultaDisciplinaMatricula(String valorConsultaDisciplinaMatricula) {
		this.valorConsultaDisciplinaMatricula = valorConsultaDisciplinaMatricula;
	}

	/**
	 * @return the campoConsultaDisciplinaMatricula
	 */
	public String getCampoConsultaDisciplinaMatricula() {
		if (campoConsultaDisciplinaMatricula == null) {
			campoConsultaDisciplinaMatricula = "";
		}
		return campoConsultaDisciplinaMatricula;
	}

	/**
	 * @param campoConsultaDisciplinaMatricula
	 *            the campoConsultaDisciplinaMatricula to set
	 */
	public void setCampoConsultaDisciplinaMatricula(String campoConsultaDisciplinaMatricula) {
		this.campoConsultaDisciplinaMatricula = campoConsultaDisciplinaMatricula;
	}

	/**
	 * @return the listaConsultaDisciplinaMatricula
	 */
	public List<DisciplinaVO> getListaConsultaDisciplinaMatricula() {
		if (listaConsultaDisciplinaMatricula == null) {
			listaConsultaDisciplinaMatricula = new ArrayList<DisciplinaVO>(0);
		}
		return listaConsultaDisciplinaMatricula;
	}

	/**
	 * @param listaConsultaDisciplinaMatricula
	 *            the listaConsultaDisciplinaMatricula to set
	 */
	public void setListaConsultaDisciplinaMatricula(List<DisciplinaVO> listaConsultaDisciplinaMatricula) {
		this.listaConsultaDisciplinaMatricula = listaConsultaDisciplinaMatricula;
	}

	public List getTipoConsultaComboDisciplinaMatricula() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void consultarDisciplinaMatricula() {
		try {
			super.consultar();
			getEstagioVO().setMatriculaVO(getMatriculaVO());
			List objs = new ArrayList(0);
			if (getCampoConsultaDisciplinaMatricula().equals("nome")) {
				String valorConsultar = getValorConsultaDisciplinaMatricula();
				if (valorConsultar.equals("")) {
					valorConsultar = "%%";
				}
				objs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaMatrizCurricularAluno(getEstagioVO().getMatriculaVO().getMatricula(), valorConsultar, null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDisciplinaMatricula().equals("codigo")) {
				if (getValorConsultaDisciplinaMatricula().equals("")) {
					setValorConsultaDisciplinaMatricula("0");
				}
				Integer codigoFiltrar = Integer.valueOf(getValorConsultaDisciplinaMatricula());
				objs = getFacadeFactory().getDisciplinaFacade().consultarDisciplinaMatrizCurricularAluno(getEstagioVO().getMatriculaVO().getMatricula(), null, codigoFiltrar, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDisciplinaMatricula(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaDisciplinaMatricula(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarDisciplinaMatricula() {
		try {
			DisciplinaVO obj = (DisciplinaVO) context().getExternalContext().getRequestMap().get("disciplinaMatricula");
			this.getEstagioVO().setDisciplina(obj);
			setMensagemID("msg_dados_selecionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparDisciplinaMatricula() {
		this.getEstagioVO().setDisciplina(new DisciplinaVO());
		limparMensagem();
	}

	public void prepararConsultaEstagio() {
		if (getControleConsulta().getCampoConsulta().equals("matricula")) {
			setAno("");
			setSemestre("");
		} else {
			setAno(Uteis.getAnoDataAtual4Digitos());
			setSemestre(Uteis.getSemestreAtual());
		}
	}

	public String getCssCampoSomenteLeituraObrigatorioParaEstagioObrigatorio() {
		if (getEstagioVO().getTipoEstagio().equals(TipoEstagioEnum.OBRIGATORIO)) {
			return "camposSomenteLeituraObrigatorio";
		} else {
			return "camposSomenteLeitura";
		}
	}

	public String getCssCampoObrigatorioParaEstagioNaoObrigatorio() {
		if (getEstagioVO().getTipoEstagio().equals(TipoEstagioEnum.NAO_OBRIGATORIO)) {
			return "camposObrigatorios";
		} else {
			return "campos";
		}
	}

	public Boolean getPermiteAlterarSeguradoApolice() {
		if (getEstagioVO().getTipoEstagio().equals(TipoEstagioEnum.NAO_OBRIGATORIO) || ((getEstagioVO().getTipoEstagio().equals(TipoEstagioEnum.OBRIGATORIO)) && (getPermiteInformarSeguradoraEstagio()) && (!getConfiguracaoGeralPadraoSistema().getForcarSeguradoraEApoliceParaEstagioObrigatorio()))) {
			return true;
		}
		return false;
	}

	public String getCssSeguradoraEstagio() {
		if (getEstagioVO().getTipoEstagio().equals(TipoEstagioEnum.NAO_OBRIGATORIO) || ((getEstagioVO().getTipoEstagio().equals(TipoEstagioEnum.OBRIGATORIO)) && (getPermiteInformarSeguradoraEstagio()) && (!getConfiguracaoGeralPadraoSistema().getForcarSeguradoraEApoliceParaEstagioObrigatorio()))) {
			return "camposObrigatorios";
		} else {
			return "camposSomenteLeitura";
		}
	}

	/**
	 * @return the nomeUsuarioLiberar
	 */
	public String getNomeUsuarioLiberar() {
		if (nomeUsuarioLiberar == null) {
			nomeUsuarioLiberar = "";
		}
		return nomeUsuarioLiberar;
	}

	/**
	 * @param nomeUsuarioLiberar
	 *            the nomeUsuarioLiberar to set
	 */
	public void setNomeUsuarioLiberar(String nomeUsuarioLiberar) {
		this.nomeUsuarioLiberar = nomeUsuarioLiberar;
	}

	/**
	 * @return the senhaUsuarioLiberar
	 */
	public String getSenhaUsuarioLiberar() {
		if (senhaUsuarioLiberar == null) {
			senhaUsuarioLiberar = "";
		}
		return senhaUsuarioLiberar;
	}

	/**
	 * @param senhaUsuarioLiberar
	 *            the senhaUsuarioLiberar to set
	 */
	public void setSenhaUsuarioLiberar(String senhaUsuarioLiberar) {
		this.senhaUsuarioLiberar = senhaUsuarioLiberar;
	}

	public void alterarTipoEstagio() {
		if (getEstagioVO().getTipoEstagio().equals(TipoEstagioEnum.OBRIGATORIO)) {
			if (getConfiguracaoGeralPadraoSistema().getForcarSeguradoraEApoliceParaEstagioObrigatorio()) {
	
			}
		} else {
			
		}
	}

	private Integer obterTotalCargaHorariaDiaria(List<EstagioVO> estagios) {
		if ((estagios == null) || (estagios.isEmpty())) {
			return 0;
		}
		int totalCargaHorariaDiariaTemp = 0;
		for (EstagioVO estagioConcomitante : estagios) {
			totalCargaHorariaDiariaTemp += estagioConcomitante.getCargaHorariaDiaria();
		}
		return totalCargaHorariaDiariaTemp;
	}

	

	/**
	 * @return the totalCargaHorariaSemanal
	 */
	public Integer getTotalCargaHorariaSemanal() {
		if (totalCargaHorariaSemanal == null) {
			totalCargaHorariaSemanal = 0;
		}
		return totalCargaHorariaSemanal;
	}

	/**
	 * @param totalCargaHorariaSemanal
	 *            the totalCargaHorariaSemanal to set
	 */
	public void setTotalCargaHorariaSemanal(Integer totalCargaHorariaSemanal) {
		this.totalCargaHorariaSemanal = totalCargaHorariaSemanal;
	}

	/**
	 * @return the totalCargaHorariaDiaria
	 */
	public Integer getTotalCargaHorariaDiaria() {
		if (totalCargaHorariaDiaria == null) {
			totalCargaHorariaDiaria = 0;
		}
		return totalCargaHorariaDiaria;
	}

	/**
	 * @param totalCargaHorariaDiaria
	 *            the totalCargaHorariaDiaria to set
	 */
	public void setTotalCargaHorariaDiaria(Integer totalCargaHorariaDiaria) {
		this.totalCargaHorariaDiaria = totalCargaHorariaDiaria;
	}

	/**
	 * @return the listaEstagioConcomitantes
	 */
	public List<EstagioVO> getListaEstagioConcomitantes() {
		if (listaEstagioConcomitantes == null) {
			listaEstagioConcomitantes = new ArrayList();
		}
		return listaEstagioConcomitantes;
	}

	/**
	 * @param listaEstagioConcomitantes
	 *            the listaEstagioConcomitantes to set
	 */
	public void setListaEstagioConcomitantes(List<EstagioVO> listaEstagioConcomitantes) {
		this.listaEstagioConcomitantes = listaEstagioConcomitantes;
	}

	/**
	 * @return the resultadoValidacao
	 */
	public String getResultadoValidacao() {
		if (resultadoValidacao == null) {
			resultadoValidacao = "";
		}
		return resultadoValidacao;
	}

	/**
	 * @param resultadoValidacao
	 *            the resultadoValidacao to set
	 */
	public void setResultadoValidacao(String resultadoValidacao) {
		this.resultadoValidacao = resultadoValidacao;
	}

	public void prepararUsuarioSenhaLiberarValidacaoDados() {
		setNomeUsuarioLiberar("");
		setSenhaUsuarioLiberar("");
	}

	public void liberarGravarEstagioSemValidacaoDados() {
		boolean usuarioValido = false;
		UsuarioVO usuarioVerif = null;
		try {
			usuarioVerif = getFacadeFactory().getControleAcessoFacade().verificarLoginUsuario(this.getNomeUsuarioLiberar(), this.getSenhaUsuarioLiberar(), true, Uteis.NIVELMONTARDADOS_TODOS);
			usuarioValido = true;
		} catch (Exception e) {
		}
		boolean usuarioTemPermissaoLiberar = false;
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Estagio_liberarValidacao", usuarioVerif);
			usuarioTemPermissaoLiberar = true;
		} catch (Exception e) {
		}
		try {
			if (!usuarioValido) {
				throw new Exception("Usuário/Senha Inválidos");
			}
			if (!usuarioTemPermissaoLiberar) {
				throw new Exception("Você não tem permissão para liberar a validação / registro do estágio.");
			}
			setNomeUsuarioLiberar("");
			setSenhaUsuarioLiberar("");			
			setMensagemID("msg_dados_liberados");
		} catch (Exception e) {
			setNomeUsuarioLiberar("");
			setSenhaUsuarioLiberar("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void liberarAlterarSeguradoraApoliceEstagioObrigatorio() {
		try {			
			setMensagemID("msg_dados_liberados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	

	public void montarListaSelectItemDisciplina() {
		getListaSelectItemDisciplina().clear();
		List<DisciplinaVO> listaDisciplinaVOs = getFacadeFactory().getDisciplinaFacade().consultarApenasDisciplinaEstagioPorMatriculaAnoSemestreNivelComboBox(getMatriculaVO().getMatricula(), "", "", getEstagioVO().getCodigo(), getUsuarioLogado());		
		getListaSelectItemDisciplina().add(new SelectItem("", ""));
		boolean encontrouDisciplinaEstagio = false;
		for (DisciplinaVO disciplinaVO : listaDisciplinaVOs) {
			getListaSelectItemDisciplina().add(new SelectItem(disciplinaVO.getCodigo(), disciplinaVO.getNome()));
			if (getEstagioVO().getDisciplina().getCodigo().equals(disciplinaVO.getCodigo())) {
				encontrouDisciplinaEstagio = true;
			}
		}
		if (!getEstagioVO().getDisciplina().getCodigo().equals(0) && !encontrouDisciplinaEstagio) {
			getListaSelectItemDisciplina().add(new SelectItem(getEstagioVO().getDisciplina().getCodigo(), getEstagioVO().getDisciplina().getNome()));
		}
	}

	public List<SelectItem> getListaSelectItemDisciplina() {
		if (listaSelectItemDisciplina == null) {
			listaSelectItemDisciplina = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplina;
	}

	public void setListaSelectItemDisciplina(List<SelectItem> listaSelectItemDisciplina) {
		this.listaSelectItemDisciplina = listaSelectItemDisciplina;
	}
	
	public void inicializarDadosAnoSemestreDeAcordoUltimaMatriculaPeriodo() {
		if (getMatriculaVO().getCurso().getPeriodicidade().equals(PeriodicidadeEnum.ANUAL.getValor()) || getMatriculaVO().getCurso().getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL.getValor())) {
			MatriculaPeriodoVO obj = getFacadeFactory().getMatriculaPeriodoFacade().consultarUltimoAnoSemestrePorMatricula(getMatriculaVO().getMatricula(), getUsuarioLogado());
			if (!obj.getCodigo().equals(0)) {				
			}
		}
	}
	
	public Boolean getApresentarAno() {
		return getMatriculaVO().getCurso().getPeriodicidade().equals(PeriodicidadeEnum.ANUAL.getValor()) || getMatriculaVO().getCurso().getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL.getValor());
	}

	public Boolean getApresentarSemestre() {
		return getMatriculaVO().getCurso().getPeriodicidade().equals(PeriodicidadeEnum.SEMESTRAL.getValor());
	}

	public String getStyleClassTipoEstagio() {
		if (!getEstagioVO().getCodigo().equals(0)) {
			return "camposSomenteLeitura";
		}
		return "camposObrigatorios";
	}
	
	public Boolean getDisableTipoEstagio() {
		return !getEstagioVO().getCodigo().equals(0);
	}
	
	private List<SelectItem> listaSelectItemAreaProfissional;
	


	public List<SelectItem> getListaSelectItemAreaProfissional() {
		if (listaSelectItemAreaProfissional == null) {
			listaSelectItemAreaProfissional = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemAreaProfissional;
	}

	public void setListaSelectItemAreaProfissional(List<SelectItem> listaSelectItemAreaProfissional) {
		this.listaSelectItemAreaProfissional = listaSelectItemAreaProfissional;
	}

	public String getStyleClassAreaAtuacao() {
		if (getEstagioVO().getTipoEstagio().equals(TipoEstagioEnum.NAO_OBRIGATORIO)) {
			return "campos";
		}
		return "camposObrigatorios";
	}
	
	public void validarDadosPermissaoImpressaoContrato(TextoPadraoDeclaracaoVO textoPadraoImprimirVO) throws Exception {
		if (!textoPadraoImprimirVO.getTextoPadraoDeclaracaofuncionarioVOs().isEmpty()) {
			Iterator i = textoPadraoImprimirVO.getTextoPadraoDeclaracaofuncionarioVOs().iterator();
			boolean encontrouFuncionario = false;
			while (i.hasNext()) {
				TextoPadraoDeclaracaoFuncionarioVO func = (TextoPadraoDeclaracaoFuncionarioVO)i.next();
				if (func.getFuncionario().getPessoa().getCodigo().intValue() == getUsuarioLogado().getPessoa().getCodigo().intValue()) {
					return;
				}
			}			
		}
		if (getEstagioImprimirVO().getTipoEstagio().equals(TipoEstagioEnum.OBRIGATORIO)) {
			verificarPermissaoImpressaoContratoObrigatorio();
			if (!getPermiteImpressaoContratoEstagioObrigatorio()) {
				throw new Exception("Usuário não possui permissão para realizar impressão de Contrato Obrigatório.");
			}
		}
		if (getEstagioImprimirVO().getTipoEstagio().equals(TipoEstagioEnum.NAO_OBRIGATORIO)) {
			verificarPermissaoImpressaoContratoNaoObrigatorio();
			if (!getPermiteImpressaoContratoEstagioNaoObrigatorio()) {
				throw new Exception("Usuário não possui permissão para realizar impressão de Contrato Não Obrigatório.");
			}
		}
	}
	
	public void verificarPermissaoImpressaoContratoObrigatorio() throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Estagio_permitirImpressaoContratoEstagioObrigatorio", getUsuarioLogado());
			setPermiteImpressaoContratoEstagioObrigatorio(Boolean.TRUE);
		} catch (Exception e) {
			setPermiteImpressaoContratoEstagioObrigatorio(Boolean.FALSE);
		}
	}
	
	public void verificarPermissaoImpressaoContratoNaoObrigatorio() throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Estagio_permitirImpressaoContratoEstagioNaoObrigatorio", getUsuarioLogado());
			setPermiteImpressaoContratoEstagioNaoObrigatorio(Boolean.TRUE);
		} catch (Exception e) {
			setPermiteImpressaoContratoEstagioNaoObrigatorio(Boolean.FALSE);
		}
	}

	public Boolean getPermiteImpressaoContratoEstagioObrigatorio() {
		if (permiteImpressaoContratoEstagioObrigatorio == null) {
			permiteImpressaoContratoEstagioObrigatorio = false;
		}
		return permiteImpressaoContratoEstagioObrigatorio;
	}

	public void setPermiteImpressaoContratoEstagioObrigatorio(Boolean permiteImpressaoContratoEstagioObrigatorio) {
		this.permiteImpressaoContratoEstagioObrigatorio = permiteImpressaoContratoEstagioObrigatorio;
	}

	public Boolean getPermiteImpressaoContratoEstagioNaoObrigatorio() {
		if (permiteImpressaoContratoEstagioNaoObrigatorio == null) {
			permiteImpressaoContratoEstagioNaoObrigatorio = false;
		}
		return permiteImpressaoContratoEstagioNaoObrigatorio;
	}

	public void setPermiteImpressaoContratoEstagioNaoObrigatorio(Boolean permiteImpressaoContratoEstagioNaoObrigatorio) {
		this.permiteImpressaoContratoEstagioNaoObrigatorio = permiteImpressaoContratoEstagioNaoObrigatorio;
	}
	
	
	public Integer getTotalHorasCumpridas() {
		Integer total = 0;
		for (EstagioVO estagioVO : getListaEstagios()) {
			total = total + estagioVO.getCargaHoraria();
		}
		return total;
	}
	
	public Boolean getDesabilitarCampoUnidadeEnsino() {
		return !getMatriculaVO().getMatricula().equals("");
	}
	
	public String getCssCampoUnidadeEnsino() {
		if (!getMatriculaVO().getMatricula().equals("")) {
			return "camposSomenteLeitura";
		}
		return "camposObrigatorios";
	}
	
	
	
	
	public Boolean getPermiteInformarSeguradoraEstagio() {
		if(permiteInformarSeguradoraEstagio == null) {
			permiteInformarSeguradoraEstagio = Boolean.FALSE;
		}
		return permiteInformarSeguradoraEstagio;
	}

	public void setPermiteInformarSeguradoraEstagio(Boolean permiteInformarSeguradoraEstagio) {
		this.permiteInformarSeguradoraEstagio = permiteInformarSeguradoraEstagio;
	}

	public void verificarPermitirInformarSeguradoraEstagio() throws Exception {
		try {
			getFacadeFactory().getControleAcessoFacade().verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("Estagio_permitirInformarSeguradoraEstagio", getUsuarioLogado());
			setPermiteInformarSeguradoraEstagio(Boolean.TRUE);
		} catch (Exception e) {
			setPermiteInformarSeguradoraEstagio(Boolean.FALSE);
		}
	}

}
