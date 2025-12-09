package controle.compras;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.SerializationUtils;
import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.academico.VisaoCoordenadorControle;
import controle.academico.VisaoProfessorControle;
import controle.arquitetura.DataModelo;
import controle.arquitetura.QuestionarioRespostaControle;
import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.QuestionarioRespostaOrigemVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoComprasEnum;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.RequisicaoItemVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("RequisicaoControle")
@Scope("viewScope")
@Lazy
public class RequisicaoControle extends QuestionarioRespostaControle implements Serializable {

	private static final long serialVersionUID = -7723425206030105067L;

	private RequisicaoVO requisicaoVO;
	private RequisicaoItemVO requisicaoItemVO;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private List<SelectItem> listaSelectItemCentroCusto;
	private List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum;

	private String campoConsultaProduto;
	private String valorConsultaProduto;
	private List<ProdutoServicoVO> listaConsultaProduto;

	private String campoConsultaCategoriaProduto;
	private String valorConsultaCategoriaProduto;
	private List<CategoriaProdutoVO> listaConsultaCategoriaProduto;

	private String campoConsultaSacadoFuncionario;
	private String valorConsultaSacadoFuncionario;
	private List<FuncionarioVO> listaConsultaFuncionario;

	private String campoConsultaSacadoFornecedor;
	private String valorConsultaSacadoFornecedor;
	private List<FornecedorVO> listaConsultaFornecedor;

	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private List<SelectItem> listaSelectItemTurma;

	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;

	private String campoConsultaCursoTurno;
	private String valorConsultaCursoTurno;
	private List<UnidadeEnsinoCursoVO> listaConsultaCursoTurno;

	private String campoConsultaCentroDespesa;
	private String valorConsultaCentroDespesa;
	private List<CategoriaDespesaVO> listaConsultaCentroDespesa;

	private String campoConsultaDepartamento;
	private String valorConsultaDepartamento;
	private List<DepartamentoVO> listaConsultaDepartamento;

	private String valorConsultaSituacaoAutorizacao;
	private String valorConsultaSituacaoEntregaRecebimento;

	private DataModelo responsavelDataModelo;
	private String nomeUsuarioPesquisa;
	private String codigoPesquisa;
	private String nomeDepartamentoPesquisa;
	private String solicitantePesquisa;
	private String categoriaPesquisa;
	private String produtoCategoriaPesquisa;
	private Integer unidadeEnsinoPesquisa;
	private DataModelo centroResultadoDataModelo;
	private boolean centroResultadoAdministrativo = false;
	private String nomeCategoriaProduto;
	private String descricaoCentroResultadoAcademico;
	
	private ConfiguracaoFinanceiroVO configuracaoFinanceiroVO;

	private QuestionarioVO questionarioVO;
	private String gravadoComSucesso;
	private Boolean abrirModalQuestionarioAbertura;
	private Boolean abrirModalQuestionarioFechamento;

	private static final String DESIGN_RELATORIO = "RequisicaoAnaliticoRel";
	private static final String DESIGN_RELATORIO_QUESTIONARIO = "RequisicaoAnaliticoQuestionarioRel";

	public RequisicaoControle() {
		setControleConsulta(new ControleConsulta());
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setDataIni(new Date());
		getControleConsultaOtimizado().setDataFim(Uteis.getNewDateComUmMesAMais());
		montarListaSelectItemUnidadeEnsino();
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
	}

	public String getObterDescricaoCorrespondenteRequisicao() {
		if (this.requisicaoVO.isSituacaoAutorizacaoPendente()) { // pendente
			return "Aguardando Autorização";
		}
		if (this.requisicaoVO.isSituacaoAutorizacaoIndeferido()) { // indeferido
			return "Negada";
		}
		if (Boolean.TRUE.equals(requisicaoVO.getAutorizado())) { // pendente
			if (this.requisicaoVO.getSituacaoEntrega().equals("FI")) {
				return "Autorizada e Entregue";
			} else if (this.requisicaoVO.getSituacaoEntrega().equals("PA")) {
				return "Autorizada e Parcialmente Entregue";
			} else {
				return "Autorizada e Aguardando Operações de Retirada/Cotação/Compra Direta";
			}
		}
		return "Aguardando Autorização";
	}

	public String getObterIconeCorrespondenteRequisicao() {
		if (this.requisicaoVO.isSituacaoAutorizacaoPendente()) { // pendente
			return "requisicaoAguardando.png";
		}
		if (this.requisicaoVO.isSituacaoAutorizacaoIndeferido()) { // indeferido
			return "requisicaoNegada.png";
		}
		if (this.requisicaoVO.getAutorizado()) { // pendente
			if (this.requisicaoVO.getSituacaoEntrega().equals("FI")) {
				return "requisicaoEntregue.png";
			} else {
				return "requisicaoAutorizada.png";
			}
		}
		return "requisicaoAguardando.png";
	}

	public String novo() {
		try {
			removerObjetoMemoria(this);
			registrarAtividadeUsuario(getUsuarioLogado(), "RequisicaoControle", "Novo Requisição", "Novo");
			setRequisicaoVO(new RequisicaoVO());
			inicializar();
			setMensagemID(MSG_TELA.msg_entre_dados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("requisicaoForm");
	}

	public String novoVisaoProfessor() {
		try {
			removerObjetoMemoria(this);
			registrarAtividadeUsuario(getUsuarioLogado(), "RequisicaoControle", "Novo Requisição Visao Professor", "Novo");
			setRequisicaoVO(new RequisicaoVO());
			inicializar();
			setMensagemID(MSG_TELA.msg_entre_dados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("requisicaoForm");
	}

	private void inicializar() {
		try {
			setRequisicaoItemVO(new RequisicaoItemVO());
			getRequisicaoVO().setResponsavelRequisicao(getUsuarioLogadoClone());
			getRequisicaoVO().setSolicitanteRequisicao(getUsuarioLogadoClone());
			getRequisicaoVO().setTipoSacado("FO");
			validarInicializacaoVisaoProfessorOuCoordenador();
			getListaSelectItemUnidadeEnsino().clear();
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	public void duplicar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RequisicaoControle", "Novo Requisição", "Novo");
			setRequisicaoVO(this.getRequisicaoVO().duplicar());
			getRequisicaoVO().setQuestionarioRespostaOrigemAberturaVO(new QuestionarioRespostaOrigemVO());
			getRequisicaoVO().setQuestionarioRespostaOrigemFechamentoVO(new QuestionarioRespostaOrigemVO());
			setRequisicaoItemVO(new RequisicaoItemVO());
			montarListaSelectItemCentroCustoAndUnidadeEnsino();
			validarInicializacaoVisaoProfessorOuCoordenador();
			setMensagemID(MSG_TELA.msg_entre_dados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void imprimirPDF() {
		 List<RequisicaoVO> listaObjetos = new ArrayList<>();
		 try {
			 if(getRequisicaoVO().getRequisicaoItemVOs().stream().anyMatch(item -> item.getCodigo().equals(0))) {
				 throw new Exception("Você adicionou novos itens a requisição, você deve gravar antes de realizar a impressão do relatório");
			 }
		 listaObjetos.add(getRequisicaoVO());
			 if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getRequisicaoRelFacade().designIReportRelatorio(DESIGN_RELATORIO));
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getRequisicaoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Requisições");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getRequisicaoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				//getSuperParametroRelVO().adicionarParametro("dataInicio", getDataInicio());

                realizarImpressaoRelatorio();
	            setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
	}

	public void imprimirPDFAberturaQuestionario() throws Exception {
		List<RequisicaoVO> listaObjetos = new ArrayList<>();
		
		if (Uteis.isAtributoPreenchido(getRequisicaoVO().getSolicitanteRequisicao().getPessoa().getCodigo())) {
			getRequisicaoVO().setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(
					getRequisicaoVO().getSolicitanteRequisicao().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(),
					false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		}
		
		getRequisicaoVO().getPerguntaRespostaOrigemVOs().addAll(getRequisicaoVO().getPerguntaRespostaOrigemVOs());
		
		RequisicaoVO requisicaoVOClonada = SerializationUtils.clone(getRequisicaoVO());
		requisicaoVOClonada.setQuestionarioRespostaOrigemFechamentoVO(new QuestionarioRespostaOrigemVO());
		listaObjetos.add(requisicaoVOClonada);
		try {
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setTituloRelatorio("Requisição de Diárias");
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getRequisicaoRelFacade().designIReportRelatorio(DESIGN_RELATORIO_QUESTIONARIO));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getRequisicaoRelFacade().caminhoBaseQuestionarioRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getRequisicaoRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void imprimirPDFEntregaQuestionario() throws Exception {
		List<RequisicaoVO> listaObjetos = new ArrayList<>();
		
		if (Uteis.isAtributoPreenchido(getRequisicaoVO().getSolicitanteRequisicao().getPessoa().getCodigo())) {
			getRequisicaoVO().setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(
					getRequisicaoVO().getSolicitanteRequisicao().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(),
					false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		}

		RequisicaoVO requisicaoVOClonada = SerializationUtils.clone(getRequisicaoVO());
		requisicaoVOClonada.setQuestionarioRespostaOrigemAberturaVO(new QuestionarioRespostaOrigemVO());
		listaObjetos.add(requisicaoVOClonada);
		try {
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setTituloRelatorio("Requisição de Diárias");
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoLogado().getNome());
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getRequisicaoRelFacade().designIReportRelatorio(DESIGN_RELATORIO_QUESTIONARIO));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getRequisicaoRelFacade().caminhoBaseQuestionarioRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getRequisicaoRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
			} else {
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public String editar() {
		RequisicaoVO obj = null;
		setAbrirModalQuestionarioAbertura(true);
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RequisicaoControle", "Inicializando Editar Requisição", "Editando");
			obj = (RequisicaoVO) context().getExternalContext().getRequestMap().get("requisicaoItemVOItens");
			setRequisicaoVO(getFacadeFactory().getRequisicaoFacade().consultarPorChavePrimaria(obj.getCodigo(), null, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setRequisicaoItemVO(new RequisicaoItemVO());
			setQuestionarioVO(getRequisicaoVO().getCategoriaProduto().getQuestionarioAberturaRequisicao());
			getQuestionarioVO().setPerguntaQuestionarioVOs(getFacadeFactory().getPerguntaQuestionarioFacade().consultarPorCodigoQuestionario(getQuestionarioVO().getCodigo(),
					false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if(Uteis.isAtributoPreenchido(obj.getQuestionarioRespostaOrigemAberturaVO().getCodigo())) {
				getRequisicaoVO().setQuestionarioRespostaOrigemAberturaVO(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorChavePrimaria(obj.getQuestionarioRespostaOrigemAberturaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));

				getRequisicaoVO().getQuestionarioRespostaOrigemAberturaVO().setPerguntaRespostaOrigemVOs(getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().consultarPorQuestionarioRequisicao(
						getRequisicaoVO().getQuestionarioRespostaOrigemAberturaVO().getQuestionarioVO().getCodigo(), getRequisicaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}

			if(Uteis.isAtributoPreenchido(obj.getQuestionarioRespostaOrigemFechamentoVO().getCodigo())) {
				getRequisicaoVO().setQuestionarioRespostaOrigemFechamentoVO(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorChavePrimaria(obj.getQuestionarioRespostaOrigemFechamentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));

				getRequisicaoVO().getQuestionarioRespostaOrigemFechamentoVO().setPerguntaRespostaOrigemVOs(getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().consultarPorQuestionarioRequisicao(
						getRequisicaoVO().getQuestionarioRespostaOrigemFechamentoVO().getQuestionarioVO().getCodigo(), getRequisicaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));

			}
			
			montarListaSelectItemTipoNivelCentroResultadoEnum();
			montarListaSelectItemCentroCustoAndUnidadeEnsino();
			validarInicializacaoVisaoProfessorOuCoordenador();
			montarQuestionario();
			registrarAtividadeUsuario(getUsuarioLogado(), "RequisicaoControle", "Finalizando Editar Requisição", "Editando");
			setMensagemID(MSG_TELA.msg_dados_editar.name());
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return "";
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("requisicaoForm.xhtml");
	}

	private void validarInicializacaoVisaoProfessorOuCoordenador() {
		if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
			VisaoCoordenadorControle visaoCoordenadorControle = (VisaoCoordenadorControle) context().getExternalContext().getSessionMap().get("VisaoCoordenadorControle");
			if (visaoCoordenadorControle != null) {
				visaoCoordenadorControle.inicializarRequisicaoEditar();
				return;
			}
		}
		if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
			VisaoProfessorControle visaoProfessor = (VisaoProfessorControle) context().getExternalContext().getSessionMap().get("VisaoProfessorControle");
			if (visaoProfessor != null) {
				visaoProfessor.inicializarRequisicaoEditar();
				return;
			}
		}
	}

	public void gravar() {
		try {
			if (Uteis.isAtributoPreenchido(getRequisicaoVO().getCategoriaProduto().getQuestionarioAberturaRequisicao())
					&& !Uteis.isAtributoPreenchido(getRequisicaoVO().getCodigo())) {
				getFacadeFactory().getRequisicaoFacade().validarDados(getRequisicaoVO(), getUsuarioLogado());

				setQuestionarioVO(getRequisicaoVO().getCategoriaProduto().getQuestionarioAberturaRequisicao());
				getQuestionarioVO().setPerguntaQuestionarioVOs(getFacadeFactory().getPerguntaQuestionarioFacade().consultarPorCodigoQuestionario(getQuestionarioVO().getCodigo(),
						false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				getRequisicaoVO().setQuestionarioRespostaOrigemAberturaVO(getFacadeFactory().getRequisicaoFacade().realizarCriacaoQuestionarioRespostaOrigem(getRequisicaoVO(), getQuestionarioVO(), getUsuarioLogado()));
				setAbrirModalQuestionarioAbertura(Boolean.TRUE);
			} else {
				persistir();
				setAbrirModalQuestionarioAbertura(Boolean.FALSE);
			}
		} catch (Exception e) {
			setAbrirModalQuestionarioAbertura(Boolean.FALSE);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private void persistir() throws Exception {
		registrarAtividadeUsuario(getUsuarioLogado(), "RequisicaoControle", "Inicializando Gravação Requisição", "Gravando");
		getFacadeFactory().getRequisicaoFacade().verificarBloqueioRequisicao(Uteis.BLOQUEIO_VERIFICAR, false, requisicaoVO);
		Uteis.checkState(getRequisicaoVO().getAutorizado(), "Essa Requisição já foi autorizada por isso não é mais permitido sua alteração.");
		getFacadeFactory().getRequisicaoFacade().persistir(requisicaoVO, true, getUsuarioLogado(), getConfiguracaoFinanceiroVO().getUsaPlanoOrcamentario());
		registrarAtividadeUsuario(getUsuarioLogado(), "RequisicaoControle", "Finalizando Gravação Requisição", "Gravando");
		setMensagemID("msg_dados_gravados");
	}   

	public void gravarRequisicaoComQuestionario() {
		try {
			getFacadeFactory().getRequisicaoFacade().persistirComQuestionarioAbertura(getRequisicaoVO(), false, getUsuarioLogado(), getConfiguracaoFinanceiroVO().getUsaPlanoOrcamentario());
			if(Uteis.isAtributoPreenchido(getRequisicaoVO().getQuestionarioRespostaOrigemAberturaVO().getCodigo())) {
				getRequisicaoVO().setQuestionarioRespostaOrigemAberturaVO(getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().consultarPorChavePrimaria(getRequisicaoVO().getQuestionarioRespostaOrigemAberturaVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));

				getRequisicaoVO().getQuestionarioRespostaOrigemAberturaVO().setPerguntaRespostaOrigemVOs(getFacadeFactory().getPerguntaRespostaOrigemInterfaceFacade().consultarPorQuestionarioRequisicao(
						getRequisicaoVO().getQuestionarioRespostaOrigemAberturaVO().getQuestionarioVO().getCodigo(), getRequisicaoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			}

			setAbrirModalQuestionarioAbertura(false);
			setMensagemID("msg_requisicao_respostaQuestionarioAbertura", Uteis.SUCESSO);
		} catch (Exception e) {
			getRequisicaoVO().getQuestionarioRespostaOrigemAberturaVO().setCodigo(0);
			setAbrirModalQuestionarioAbertura(true);
			getAcaoModalQuestionario();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void gravarRequisicaoComQuestionarioFechamento() {
		try {
			getFacadeFactory().getQuestionarioRespostaOrigemInterfaceFacade().incluir(getRequisicaoVO().getQuestionarioRespostaOrigemFechamentoVO(), getUsuario());
			getFacadeFactory().getRequisicaoFacade().alterarQuestionarioFechamentoRequisicao(getRequisicaoVO(), getUsuario());

			setAbrirModalQuestionarioFechamento(false);
			setMensagemID("msg_requisicao_respostaQuestionarioFechamento", Uteis.SUCESSO);
		} catch (Exception e) {
			setAbrirModalQuestionarioFechamento(true);
			getAcaoModalQuestionario();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String gravarVisaoProfessor() {
		try {
			executarValidacaoSimulacaoVisaoProfessor();
			registrarAtividadeUsuario(getUsuarioLogado(), "RequisicaoControle", "Inicializando Gravação Requisição Visao Professor", "Gravando");
			getFacadeFactory().getRequisicaoFacade().verificarBloqueioRequisicao(Uteis.BLOQUEIO_VERIFICAR, false, requisicaoVO);
			Uteis.checkState(getRequisicaoVO().getAutorizado(), "Essa Requisição já foi autorizada por isso não é mais permitido sua alteração.");
			getFacadeFactory().getRequisicaoFacade().persistir(requisicaoVO, true, getUsuarioLogado());
			registrarAtividadeUsuario(getUsuarioLogado(), "RequisicaoControle", "Finalizando Gravação Requisição Visao Professor", "Gravando");
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("requisicaoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("requisicaoForm.xhtml");
		}
	}
	
	public void validarDadosListaResposta1() {
		RespostaPerguntaVO obj = (RespostaPerguntaVO) context().getExternalContext().getRequestMap().get("respostaItens");
		if (obj.getSelecionado()) {
			obj.setSelecionado(false);
		} else {
			obj.setSelecionado(true);
			getQuestionarioVO().varrerListaQuestionarioRetornarPerguntaRespondida(obj);
		}
	}

	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultar();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public boolean getApresentarQuestionarioRespostaOrigemAbertura() {
		return Uteis.isAtributoPreenchido(getRequisicaoVO().getQuestionarioRespostaOrigemAberturaVO().getCodigo());
	}

	public boolean getApresentarQuestionarioRespostaOrigemFechamento() {
		return Uteis.isAtributoPreenchido(getRequisicaoVO().getQuestionarioRespostaOrigemFechamentoVO().getCodigo());		
	}

	@Override
	public String consultar() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RequisicaoControle", "Inicializando Consultar Requisição", "Consultando");
			super.consultar();
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getRequisicaoFacade().consultar(this.getCodigoPesquisa(), this.getNomeUsuarioPesquisa(), this.getSolicitantePesquisa(), this.getNomeDepartamentoPesquisa(), this.getCategoriaPesquisa(), this.getProdutoCategoriaPesquisa(), getValorConsultaSituacaoAutorizacao(), getValorConsultaSituacaoEntregaRecebimento(), getUnidadeEnsinoPesquisa(), getControleConsultaOtimizado(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			registrarAtividadeUsuario(getUsuarioLogado(), "RequisicaoControle", "Finalizando Consultar Requisição", "Consultando");
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		return "";
	}

	public void consultarVisaoProfessor() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RequisicaoControle", "Inicializando Consultar Requisição Visão Professor", "Consultando");
			super.consultar();
			setNomeUsuarioPesquisa(getUsuarioLogado().getNome());
			setSolicitantePesquisa(getUsuarioLogado().getNome());
			setUnidadeEnsinoPesquisa(getUnidadeEnsinoLogado().getCodigo());
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getRequisicaoFacade().consultar(this.getCodigoPesquisa(), this.getNomeUsuarioPesquisa(), this.getSolicitantePesquisa(), this.getNomeDepartamentoPesquisa(), this.getCategoriaPesquisa(), this.getProdutoCategoriaPesquisa(), getValorConsultaSituacaoAutorizacao(), getValorConsultaSituacaoEntregaRecebimento(), getUnidadeEnsinoPesquisa(), getControleConsultaOtimizado(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			registrarAtividadeUsuario(getUsuarioLogado(), "RequisicaoControle", "Finalizando Consultar Requisição Visão Professor", "Consultando");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarVisaoCoordenador() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RequisicaoControle", "Inicializando Consultar Requisição Visão Coordenador", "Consultando");
			super.consultar();
			setNomeUsuarioPesquisa(getUsuarioLogado().getNome());
			setSolicitantePesquisa(getUsuarioLogado().getNome());
			setUnidadeEnsinoPesquisa(getUnidadeEnsinoLogado().getCodigo());
			getControleConsultaOtimizado().preencherDadosParaConsulta(true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getRequisicaoFacade().consultar(this.getCodigoPesquisa(), this.getNomeUsuarioPesquisa(), this.getSolicitantePesquisa(), this.getNomeDepartamentoPesquisa(), this.getCategoriaPesquisa(), this.getProdutoCategoriaPesquisa(), getValorConsultaSituacaoAutorizacao(), getValorConsultaSituacaoEntregaRecebimento(), getUnidadeEnsinoPesquisa(), getControleConsultaOtimizado(), true, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			registrarAtividadeUsuario(getUsuarioLogado(), "RequisicaoControle", "Finalizando Consultar Requisição Visão Coordenador", "Consultando");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String excluir() {
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "RequisicaoControle", "Inicializando Excluir Requisição", "Excluindo");
			getFacadeFactory().getRequisicaoFacade().excluir(requisicaoVO, getUsuarioLogado());
			setRequisicaoVO(new RequisicaoVO());
			setRequisicaoItemVO(new RequisicaoItemVO());
			registrarAtividadeUsuario(getUsuarioLogado(), "RequisicaoControle", "Finalizando Excluir Requisição", "Excluindo");
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("requisicaoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("requisicaoForm.xhtml");
		}
	}

	public void autorizar() {
		try {
			getFacadeFactory().getRequisicaoFacade().verificarPermissaoAutorizarIndeferir(getUsuarioLogadoClone());
			requisicaoVO.setResponsavelAutorizacao(getUsuarioLogadoClone());
			requisicaoVO.setDataAutorizacao(new Date());
			requisicaoVO.setSituacaoAutorizacao("AU");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void indeferir() {
		try {
			if (requisicaoVO.getSituacaoAutorizacao().equals("AU")) {
				verificarItensCotacao();
			}
			getFacadeFactory().getRequisicaoFacade().verificarPermissaoAutorizarIndeferir(getUsuarioLogadoClone());
			requisicaoVO.setResponsavelAutorizacao(getUsuarioLogadoClone());
			requisicaoVO.setDataAutorizacao(new Date());
			requisicaoVO.setSituacaoAutorizacao("IN");

		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void adicionarRequisicaoItem() throws Exception {
		try {
			if (getRequisicaoVO().getRequisicaoItemVOs().size() > 0) {
				RequisicaoItemVO obj = (RequisicaoItemVO) getRequisicaoVO().getRequisicaoItemVOs().get(0);
				if (getRequisicaoItemVO().getProdutoServico().getExigeCompraCotacao().booleanValue() != obj.getProdutoServico().getExigeCompraCotacao().booleanValue()) {
					if (obj.getProdutoServico().getExigeCompraCotacao().booleanValue()) {
						throw new ConsistirException("Após informar o primeiro produto do tipo 'Exige Cotação', todos os outros deverão ser do mesmo tipo.");
					} else {
						throw new ConsistirException("Após informar o primeiro produto do tipo 'Não Exige Cotação', todos os outros deverão ser do mesmo tipo.");
					}
				}
			}
			getRequisicaoVO().adicionarObjRequisicaoItemVOs(getRequisicaoItemVO());
			this.setRequisicaoItemVO(new RequisicaoItemVO());
			setMensagemID("msg_dados_adicionados");

		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());

		}
	}
	
	public void montarQuestionario() {
		try {
			if (Uteis.isAtributoPreenchido(getRequisicaoVO()) && Uteis
					.isAtributoPreenchido(getRequisicaoVO().getCategoriaProduto().getQuestionarioEntregaRequisicao())) {

				List<RequisicaoVO> requisicoesVOs = getFacadeFactory().getRequisicaoFacade().consultarRequisicaoRespostaQuestionarioFechamento(getUsuarioLogado(), Uteis.NIVELMONTARDADOS_TODOS);
				if (Uteis.isAtributoPreenchido(requisicoesVOs)) {
					Boolean existeRequisicao = requisicoesVOs.stream().anyMatch(p -> p.getCodigo().equals(getRequisicaoVO().getCodigo()));
					if (existeRequisicao) {
						getRequisicaoVO().getCategoriaProduto().getQuestionarioEntregaRequisicao()
								.setPerguntaQuestionarioVOs(getFacadeFactory().getPerguntaQuestionarioFacade()
										.consultarPorCodigoQuestionario(getRequisicaoVO().getCategoriaProduto().getQuestionarioEntregaRequisicao().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));

						getRequisicaoVO().setQuestionarioRespostaOrigemFechamentoVO(getFacadeFactory().getRequisicaoFacade()
										.realizarCriacaoQuestionarioRespostaOrigem(getRequisicaoVO(), getRequisicaoVO().getCategoriaProduto().getQuestionarioEntregaRequisicao(),getUsuarioLogado()));
						setAbrirModalQuestionarioFechamento(true);
					}
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editarRequisicaoItem() throws Exception {
		RequisicaoItemVO obj = (RequisicaoItemVO) context().getExternalContext().getRequestMap().get("requisicaoItemVOItens");
		setRequisicaoItemVO(obj);
	}

	public void removerRequisicaoItem() throws Exception {
		RequisicaoItemVO obj = (RequisicaoItemVO) context().getExternalContext().getRequestMap().get("requisicaoItemVOItens");
		getRequisicaoVO().excluirObjRequisicaoItemVOs(obj.getProdutoServico().getCodigo());
		setMensagemID("msg_dados_excluidos");
	}

	public Integer getUsuarioFiltrarRequisicoes() {
		try {
			if (this.getUsuarioLogado().getPerfilAdministrador()) {
				return 0;
			} else {
				return this.getUsuarioLogado().getCodigo();
			}
		} catch (Exception e) {
			return -1;
		}
	}

	public void verificarItensCotacao() throws Exception {
		Iterator<RequisicaoItemVO> i = requisicaoVO.getRequisicaoItemVOs().iterator();
		while (i.hasNext()) {
			RequisicaoItemVO obj = (RequisicaoItemVO) i.next();
			if (obj.getCotacao().intValue() != 0) {
				throw new Exception("Existe ITEM(S) DA REQUISIÇÂO que já estão na lista de cotação");
			}
		}

	}

	public void selecionarProduto() {
		try {
			ProdutoServicoVO obj = (ProdutoServicoVO) context().getExternalContext().getRequestMap().get("produtoItem");
			this.getRequisicaoItemVO().setProdutoServico(getFacadeFactory().getProdutoServicoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			if (Uteis.isAtributoPreenchido(this.getRequisicaoItemVO().getProdutoServico().getValorUltimaCompra())) {
				this.getRequisicaoItemVO().setValorUnitario(this.getRequisicaoItemVO().getProdutoServico().getValorUltimaCompra());
			} else {
				this.getRequisicaoItemVO().setValorUnitario(this.getRequisicaoItemVO().getProdutoServico().getValorUnitario());
			}
			this.listaConsultaProduto.clear();
			this.valorConsultaProduto = null;
			this.campoConsultaProduto = null;
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void consultarProduto() {
		try {
			List<ProdutoServicoVO> objs = new ArrayList<>(0);
			if (getCampoConsultaProduto().equals("codigo")) {
				if (getValorConsultaProduto().equals("")) {
					setValorConsultaProduto("0");
				}
				objs = getFacadeFactory().getProdutoServicoFacade().consultarPorCodigoECategoriaProdutoAtivo(new Integer(getValorConsultaProduto()), getRequisicaoVO().getCategoriaProduto().getCodigo(), null, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaProduto().equals("nome")) {
				objs = getFacadeFactory().getProdutoServicoFacade().consultarPorNomeECategoriaProdutoAtivo(getValorConsultaProduto(), getRequisicaoVO().getCategoriaProduto().getCodigo(), null, false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsultaProduto(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setListaConsultaProduto(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboProduto() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public String getExibirRichModalConsultarProduto() {
		if (getRequisicaoVO().getCategoriaProduto() == null || getRequisicaoVO().getCategoriaProduto().getCodigo().intValue() == 0) {
			return "";
		}
		return "RichFaces.$('panelProduto').show()";
	}

	public void exibirMensagemErroConsultarProduto() {
		try {
			if (getRequisicaoVO().getCategoriaProduto() == null || getRequisicaoVO().getCategoriaProduto().getCodigo().intValue() == 0) {
				throw new Exception("Informe a CATEGORIA PRODUTO (Requisicão) antes de realizar esta consulta.");
			}
			setMensagemID(MSG_TELA.msg_entre_dados.name());
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarSacado() {
		try {
			if (getRequisicaoVO().isSacadoFuncionarioSelecionado()) {
				FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioSacado");
				getRequisicaoVO().setSacadoFuncionario(obj);
				getRequisicaoVO().setSacadoFornecedor(new FornecedorVO());
			} else if (getRequisicaoVO().isSacadoFornecedorSelecionado()) {
				FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorSacado");
				getRequisicaoVO().setSacadoFornecedor(obj);
				getRequisicaoVO().setSacadoFuncionario(new FuncionarioVO());
			}
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarFornecedorSacado() {
		try {
			super.consultar();
			List<FornecedorVO> objs = new ArrayList<>(0);
			if (getCampoConsultaSacadoFornecedor().equals("codigo")) {
				int valorInt = 0;
				if (!getValorConsultaSacadoFornecedor().equals("")) {
					valorInt = Integer.parseInt(getValorConsultaSacadoFornecedor());
				}
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCodigo(new Integer(valorInt), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaSacadoFornecedor().equals("nome")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaSacadoFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaSacadoFornecedor().equals("razaoSocial")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaSacadoFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaSacadoFornecedor().equals("nomeCidade")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorNomeCidade(getValorConsultaSacadoFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaSacadoFornecedor().equals("CNPJ")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaSacadoFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaSacadoFornecedor().equals("inscEstadual")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorInscEstadual(getValorConsultaSacadoFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaSacadoFornecedor().equals("RG")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRG(getValorConsultaSacadoFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaSacadoFornecedor().equals("CPF")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaSacadoFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFornecedor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarFuncionarioSacado() {
		try {
			super.consultar();
			List<FuncionarioVO> objs = new ArrayList<>(0);
			if (getCampoConsultaSacadoFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaSacadoFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaSacadoFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaSacadoFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaSacadoFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaSacadoFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaSacadoFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaSacadoFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaSacadoFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaSacadoFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaSacadoFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaSacadoFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaSacadoFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultaSacadoFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCategoriaProduto() {
		try {
			CategoriaProdutoVO obj = (CategoriaProdutoVO) context().getExternalContext().getRequestMap().get("categoriaProdutoItem");
			this.getRequisicaoVO().setCategoriaProduto(getFacadeFactory().getCategoriaProdutoFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			this.getRequisicaoVO().setCategoriaDespesa(this.getRequisicaoVO().getCategoriaProduto().getCategoriaDespesa());
			this.getRequisicaoVO().setCurso(new CursoVO());
			this.getRequisicaoVO().setTurma(new TurmaVO());
			this.getRequisicaoVO().setTurno(new TurnoVO());
			this.getRequisicaoVO().setFuncionarioCargoVO(new FuncionarioCargoVO());
			this.listaConsultaCategoriaProduto.clear();
			this.valorConsultaCategoriaProduto = null;
			this.campoConsultaCategoriaProduto = null;
			montarListaSelectItemTipoNivelCentroResultadoEnum();
			montarListaSelectItemCentroCustoAndUnidadeEnsino();
			setNomeCategoriaProduto(getRequisicaoVO().getCategoriaProduto().getNome());
			setDescricaoCentroResultadoAcademico(getRequisicaoVO().getCentroResultadoAdministrativo().getDescricao());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			this.getRequisicaoVO().setRequisicaoItemVOs(new ArrayList<>(0));
			setRequisicaoItemVO(new RequisicaoItemVO());
			getQuestionarioVO().setPerguntaQuestionarioVOs(getFacadeFactory().getPerguntaQuestionarioFacade().consultarPorCodigoQuestionario(obj.getCodigo(),
					false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCategoriaProduto() {
		try {
			List<CategoriaProdutoVO> objs = new ArrayList<>(0);
			if (getCampoConsultaCategoriaProduto().equals("codigo")) {
				if (getValorConsultaCategoriaProduto().equals("")) {
					setValorConsultaCategoriaProduto("0");
				}
				int valorInt = Uteis.getValorInteiro(getValorConsultaCategoriaProduto());
				objs = getFacadeFactory().getCategoriaProdutoFacade().consultarPorCodigo((valorInt), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaCategoriaProduto().equals("nome")) {
				objs = getFacadeFactory().getCategoriaProdutoFacade().consultarPorNome(getValorConsultaCategoriaProduto(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsultaCategoriaProduto(objs);
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			e.printStackTrace();
			setListaConsultaCategoriaProduto(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCategoriaProduto() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public void selecionarCentroDespesa() {
		try {
			CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("centroDespesaItens");
			this.getRequisicaoVO().setCategoriaDespesa(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			this.getRequisicaoVO().setCurso(new CursoVO());
			this.getRequisicaoVO().setTurma(new TurmaVO());
			this.getRequisicaoVO().setTurno(new TurnoVO());
			this.getRequisicaoVO().setFuncionarioCargoVO(new FuncionarioCargoVO());
			getRequisicaoVO().setCentroResultadoAdministrativo(new CentroResultadoVO());
			this.listaConsultaCentroDespesa.clear();
			this.valorConsultaCentroDespesa = null;
			this.campoConsultaCentroDespesa = null;
			montarListaSelectItemCentroCustoAndUnidadeEnsino();
			montarListaSelectItemTipoNivelCentroResultadoEnum();
			if (!getRequisicaoVO().getCategoriaDespesa().getExigeCentroCustoRequisitante()) {
				preencherDadosPorCategoriaDespesa();
			}
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCentroDespesa() {
		try {
			List<CategoriaDespesaVO> objs = new ArrayList<>(0);
			if (getCampoConsultaCentroDespesa().equals("descricao")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaCentroDespesa().equals("identificadorCentroDespesa")) {
				objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			setListaConsultaCentroDespesa(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCentroDespesa(new ArrayList<>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCentroDespesa() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("identificadorCentroDespesa", "Identificador Centro Despesa"));
		return itens;
	}

	public void inicializaConsultaResponsavel() {
		this.limparResponsavelDataModelo();
		inicializarMensagemVazia();
	}

	public void selecionarResponsavel() {
		try {
			UsuarioVO obj = (UsuarioVO) context().getExternalContext().getRequestMap().get("responsavelItem");
			this.requisicaoVO.setSolicitanteRequisicao(obj);
			this.getRequisicaoVO().setFuncionarioCargoVO(new FuncionarioCargoVO());
			this.montarListaSelectItemCentroCustoAndUnidadeEnsino();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void consultarResponsavel() {
		try {
			this.getResponsavelDataModelo().setListaConsulta(getFacadeFactory().getUsuarioFacade().consultarUsuarioPorUnidadeEnsino(getResponsavelDataModelo().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), this.getResponsavelDataModelo().getLimitePorPagina(), this.getResponsavelDataModelo().getOffset(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, this.getUsuarioLogado()));
			this.getResponsavelDataModelo().setTotalRegistrosEncontrados(getFacadeFactory().getUsuarioFacade().consultarUsuarioPorUnidadeEnsinoContador(getResponsavelDataModelo().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, this.getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboResponsavel() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public String getCampoConsultaProduto() {
		if (campoConsultaProduto == null) {
			campoConsultaProduto = "";
		}
		return campoConsultaProduto;
	}

	public void setCampoConsultaProduto(String campoConsultaProduto) {
		this.campoConsultaProduto = campoConsultaProduto;
	}

	public List<ProdutoServicoVO> getListaConsultaProduto() {
		if (listaConsultaProduto == null) {
			listaConsultaProduto = new ArrayList<>(0);
		}
		return listaConsultaProduto;
	}

	public void setListaConsultaProduto(List<ProdutoServicoVO> listaConsultaProduto) {
		this.listaConsultaProduto = listaConsultaProduto;
	}

	public String getValorConsultaProduto() {
		if (valorConsultaProduto == null) {
			valorConsultaProduto = "";
		}
		return valorConsultaProduto;
	}

	public void setValorConsultaProduto(String valorConsultaProduto) {
		this.valorConsultaProduto = valorConsultaProduto;
	}

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<>(0);

		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));
		itens.add(new SelectItem("unidadeEnsino", "Unidade de Ensino"));
		return itens;
	}

	public List<SelectItem> getTipoConsultaComboFornecedor() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("nomeCidade", "Cidade"));
		itens.add(new SelectItem("CNPJ", "CNPJ"));
		itens.add(new SelectItem("inscEstadual", "Inscrição Estadual"));
		itens.add(new SelectItem("RG", "RG"));
		itens.add(new SelectItem("CPF", "CPF"));
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
	
	public void limparCamposPorTipoNivelCentroResultadoEnum() {
		try {
			getRequisicaoVO().limparCamposPorTipoNivelCentroResultadoEnum();
			preencherDadosPorCategoriaDespesa();
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}
	
	private void montarListaSelectItemTipoNivelCentroResultadoEnum() {
		try {
			getListaSelectItemTipoNivelCentroResultadoEnum().clear();
			if(getRequisicaoVO().isCategoriaDespesaInformada()){
				getFacadeFactory().getCategoriaDespesaFacade().montarListaSelectItemTipoNivelCentroResultadoEnum(getRequisicaoVO().getCategoriaDespesa(), getListaSelectItemTipoNivelCentroResultadoEnum());
				if(!getListaSelectItemTipoNivelCentroResultadoEnum().isEmpty() && !Uteis.isAtributoPreenchido(getRequisicaoVO().getTipoNivelCentroResultadoEnum())){
					getRequisicaoVO().setTipoNivelCentroResultadoEnum((TipoNivelCentroResultadoEnum) getListaSelectItemTipoNivelCentroResultadoEnum().get(0).getValue());	
				}
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
		
	}

	private void montarListaSelectItemUnidadeEnsino() {
		try {
			getListaSelectItemUnidadeEnsino().clear();
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				UnidadeEnsinoVO obj = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
				setUnidadeEnsinoPesquisa(obj.getCodigo());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(obj.getCodigo(), obj.getNome()));
			} else {
				List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			}
			inicializarMensagemVazia();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}

	public void montarListaSelectItemCentroCustoAndUnidadeEnsino() {
		try {
			getListaSelectItemCentroCusto().clear();
			getListaSelectItemUnidadeEnsino().clear();
			List<Integer>listaUnidadeEnsinoFuncionarioCargo = new ArrayList<>();
			FuncionarioVO funcionario = null;
			if (Uteis.isAtributoPreenchido(getRequisicaoVO().getSolicitanteRequisicao().getPessoa().getCodigo())) {
				funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorCodigoPessoa(getRequisicaoVO().getSolicitanteRequisicao().getPessoa().getCodigo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				if (getRequisicaoVO().getCategoriaDespesa().getExigeCentroCustoRequisitante() && !Uteis.isAtributoPreenchido(funcionario.getFuncionarioCargoVOs())) {
					throw new StreamSeiException("Não foi encontado nenhum Funcionário Cargo para o Solicitante " + funcionario.getPessoa().getNome());
				}
				if (!getRequisicaoVO().getCategoriaDespesa().getExigeCentroCustoRequisitante()) {
					getListaSelectItemCentroCusto().add(new SelectItem(0, ""));
					getListaSelectItemUnidadeEnsino().add(new SelectItem(0, ""));
				}
				if (Uteis.isAtributoPreenchido(funcionario.getFuncionarioCargoVOs())) {
					if (funcionario.getFuncionarioCargoVOs().size() > 1) {
						getListaSelectItemCentroCusto().add(new SelectItem("", ""));
					}
					for (FuncionarioCargoVO funcionarioCargo : funcionario.getFuncionarioCargoVOs()) {
						if (isValidarSeCentroCustoNaoEstaAtivoParaRequisicao(funcionarioCargo)) {
							continue;
						}
						getListaSelectItemCentroCusto().add(new SelectItem(funcionarioCargo.getCodigo(), funcionarioCargo.getMatriculaCargo() + " - " + funcionarioCargo.getFuncionarioVO().getPessoa().getNome() + " - " +
								(Uteis.isAtributoPreenchido(funcionarioCargo.getDepartamento()) ? funcionarioCargo.getDepartamento().getNome() : "")));
						if (!getRequisicaoVO().getCategoriaDespesa().getExigeCentroCustoRequisitante() && !listaUnidadeEnsinoFuncionarioCargo.contains(funcionarioCargo.getCentroCusto().getUnidadeEnsino().getCodigo())) {
							getListaSelectItemUnidadeEnsino().add(new SelectItem(funcionarioCargo.getCentroCusto().getUnidadeEnsino().getCodigo(), funcionarioCargo.getCentroCusto().getUnidadeEnsino().getNome()));
							listaUnidadeEnsinoFuncionarioCargo.add(funcionarioCargo.getCentroCusto().getUnidadeEnsino().getCodigo());
						}
					}
				} else {
					montarListaSelectItemUnidadeEnsino();
				}
				if (getRequisicaoVO().getCategoriaDespesa().getExigeCentroCustoRequisitante() && getListaSelectItemCentroCusto().isEmpty()) {
					throw new StreamSeiException("Não foi possivel montar a lista de CENTRO DE CUSTO para o Funcionário " + funcionario.getPessoa().getNome() + " na Unidade de Ensino " + getUnidadeEnsinoLogado().getNome() + " .");
				}
				if (!getRequisicaoVO().getCategoriaDespesa().getExigeCentroCustoRequisitante() && getListaSelectItemUnidadeEnsino().isEmpty()) {
					throw new StreamSeiException("Não foi possivel montar a lista de Unidade Ensino para o Funcionário " + funcionario.getPessoa().getNome());
				}
				if (getRequisicaoVO().getCategoriaDespesa().getExigeCentroCustoRequisitante() && getListaSelectItemCentroCusto().size() == 1) {
					getRequisicaoVO().getFuncionarioCargoVO().setCodigo((Integer) (getListaSelectItemCentroCusto().get(0).getValue()));
					preencherDadosPorCategoriaDespesa();
				}
			} else {
				throw new StreamSeiException("Não foi possivel montar a lista de CENTRO DE CUSTO para o Funcionário " + getRequisicaoVO().getSolicitanteRequisicao().getUsername() + ".");
			}
			montarListaSelectItemTurmaProfessorOrCoordenador();
		} catch (Exception e) {
			setListaSelectItemCentroCusto(new ArrayList<SelectItem>(0));
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	private boolean isValidarSeCentroCustoNaoEstaAtivoParaRequisicao(FuncionarioCargoVO funcionarioCargo) {
		return (Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado().getCodigo()) && !funcionarioCargo.getUnidade().getCodigo().equals(getUnidadeEnsinoLogado().getCodigo())) || (!Uteis.isAtributoPreenchido(getRequisicaoVO().getCodigo()) && !funcionarioCargo.getAtivo()) || (Uteis.isAtributoPreenchido(getRequisicaoVO().getCodigo()) && !funcionarioCargo.getAtivo() && !funcionarioCargo.getCentroCusto().getDescricaoCentroCusto().equals(getRequisicaoVO().getCentroCusto().getDescricaoCentroCusto()));
	}

	public void preencherDadosPorCategoriaDespesa() {
		try {
			getFacadeFactory().getRequisicaoFacade().preencherDadosPorCategoriaDespesa(getRequisicaoVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	@SuppressWarnings("unchecked")
	public void montarListaSelectItemTurmaProfessorOrCoordenador() {
		try {
			if (getRequisicaoVO().getCategoriaDespesa().isInformaNivelAcademicoTurma()) {
				List<Integer> mapAuxiliarSelectItem = new ArrayList<>();
				List<TurmaVO> listaTurmas = new ArrayList<>();
				getListaSelectItemTurma().clear();
				if (getUsuarioLogado().getIsApresentarVisaoProfessor()) {
					listaTurmas = consultarTurmaPorProfessor();
				} else if (getUsuarioLogado().getIsApresentarVisaoCoordenador()) {
					listaTurmas = consultarTurmaCoordenador();
				}

				getListaSelectItemTurma().add(new SelectItem(0, ""));
				if (Uteis.isAtributoPreenchido(listaTurmas)) {
					for (TurmaVO turmaVO : listaTurmas) {
						if (!mapAuxiliarSelectItem.contains(turmaVO.getCodigo())) {
							getListaSelectItemTurma().add(new SelectItem(turmaVO.getCodigo(), turmaVO.aplicarRegraNomeCursoApresentarCombobox()));
							mapAuxiliarSelectItem.add(turmaVO.getCodigo());
						}
					}
					Collections.sort(getListaSelectItemTurma(), new SelectItemOrdemValor());
				}
			}
		} catch (Exception e) {
			getListaSelectItemTurma().clear();
		}
	}

	public List<TurmaVO> consultarTurmaPorProfessor() throws Exception {
		if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), Uteis.getSemestreAtual(), Uteis.getData(new Date(), "yyyy"), false, "AT", 0, getUsuarioLogado().getIsApresentarVisaoProfessor(), false, true, true);
		} else if (getConfiguracaoGeralPadraoSistema().getPerfilPadraoProfessorPosGraduacao().getCodigo().intValue() == getUsuarioLogado().getPerfilAcesso().getCodigo()) {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), Uteis.getSemestreAtual(), Uteis.getData(new Date(), "yyyy"), false, "AT", 0, getUsuarioLogado().getIsApresentarVisaoProfessor(), true, false, true);
		} else {
			return getFacadeFactory().getTurmaFacade().consultarTurmaPorProfessorAnoSemestreTurmaAnteriorNivelDadosCombobox(getUsuarioLogado().getPessoa().getCodigo(), Uteis.getSemestreAtual(), Uteis.getData(new Date(), "yyyy"), false, "AT", 0, getUsuarioLogado().getIsApresentarVisaoProfessor(), false, false, true);
		}
	}

	public List<TurmaVO> consultarTurmaCoordenador() throws Exception {
		return getFacadeFactory().getTurmaFacade().consultaRapidaPorCoordenadorAnoSemestre(getUsuarioLogado().getPessoa().getCodigo(), false, false, false, false, Uteis.getData(new Date(), "yyyy"), Uteis.getSemestreAtual(), getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado());
	}

	public List<SelectItem> getListaSelectItemTipoSacado() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem(TipoSacado.FORNECEDOR.getValor(), TipoSacado.FORNECEDOR.getDescricao()));
		itens.add(new SelectItem(TipoSacado.FUNCIONARIO_PROFESSOR.getValor(), TipoSacado.FUNCIONARIO_PROFESSOR.getDescricao()));
		return itens;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nomeUsuario", "Responsável Requisição"));
		itens.add(new SelectItem("codigo", "Número"));
		itens.add(new SelectItem("nomeDepartamento", "Departamento"));
		return itens;
	}

	/**
	 * Verifica se o campo selecionado é do tipo data.
	 * 
	 * @return boolean
	 */
	public boolean isCampoData() {
		return getControleConsulta().getCampoConsulta().equals("periodo");
	}

	/**
	 * Metodo responsavel por carregar a combo situacao de autorisacao da requisicao
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SelectItem> getListaSelectItemSituacaoAutorizacao() throws Exception {
		List<SelectItem> objs = new ArrayList<>(0);
		Hashtable situacaoAutorizacao = (Hashtable) Dominios.getSituacaoAutorizacao();
		Enumeration keys = situacaoAutorizacao.keys();
		objs.add(new SelectItem("", ""));
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoAutorizacao.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort(objs, ordenador);
		return objs;
	}

	public boolean isMostrarBotaoExcluir() {
		return this.getRequisicaoVO().getSituacaoAutorizacao().equals("PE") && !this.getRequisicaoVO().isNovoObj();
	}

	/**
	 * Metodo responsavel por carregar a combo situacao de entrega da requisicao
	 * 
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<SelectItem> getListaSelectItemSituacaoEntregaRecebimento() throws Exception {
		List<SelectItem> objs = new ArrayList<>(0);
		Hashtable situacaoEntregaRecebimento = (Hashtable) Dominios.getSituacaoEntregaRecebimento();
		Enumeration keys = situacaoEntregaRecebimento.keys();
		objs.add(new SelectItem("", ""));
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoEntregaRecebimento.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort(objs, ordenador);
		return objs;
	}

	/**
	 * 
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setDataIni(new Date());
		getControleConsultaOtimizado().setDataFim(Uteis.getNewDateComUmMesAMais());
		setMensagemID(MSG_TELA.msg_entre_prmconsulta.name());
		montarListaSelectItemUnidadeEnsino();
		return Uteis.getCaminhoRedirecionamentoNavegacao("requisicaoCons");
	}

	public void limparTurma() {
		try {
			getRequisicaoVO().setTurma(new TurmaVO());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarTurma() {
		try {
			TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
			getRequisicaoVO().setTurma(obj);
			preencherDadosPorCategoriaDespesa();
			valorConsultaTurma = "";
			campoConsultaTurma = "";
			listaConsultaTurma.clear();
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarTurma() {
		try {
			super.consultar();
			Uteis.checkState(!Uteis.isAtributoPreenchido(getRequisicaoVO().getUnidadeEnsino()), "O campo Unidade Ensino deve ser informado.");
			setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurma(), getValorConsultaTurma(), getRequisicaoVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboTurma() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("identificadorTurma", "Identificador"));
		itens.add(new SelectItem("nomeCurso", "Curso"));
		return itens;
	}

	public void limparCurso() {
		try {
			getRequisicaoVO().setCurso(new CursoVO());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCurso() {
		try {
			CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");
			getRequisicaoVO().setCurso(obj);
			preencherDadosPorCategoriaDespesa();
			listaConsultaCurso.clear();
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCurso() {
		try {
			Uteis.checkState(!Uteis.isAtributoPreenchido(getRequisicaoVO().getUnidadeEnsino()), "O campo Unidade Ensino deve ser informado.");
			setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultar(getCampoConsultaCurso(), getValorConsultaCurso(), getRequisicaoVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCurso(null);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCurso() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public void limparTurno() {
		try {
			getRequisicaoVO().setTurno(new TurnoVO());
			getRequisicaoVO().setCurso(new CursoVO());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCursoTurno() {
		try {
			UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItens");
			getRequisicaoVO().setTurno(obj.getTurno());
			getRequisicaoVO().setCurso(obj.getCurso());
			preencherDadosPorCategoriaDespesa();
			listaConsultaCurso.clear();
			this.setValorConsultaCurso("");
			this.setCampoConsultaCurso("");
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCursoTurno() {
		try {
			Uteis.checkState(!Uteis.isAtributoPreenchido(getRequisicaoVO().getUnidadeEnsino()), "O campo Unidade Ensino deve ser informado.");
			setListaConsultaCursoTurno(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultar(getCampoConsultaCursoTurno(), getValorConsultaCursoTurno(), getRequisicaoVO().getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaCursoTurno(null);
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboCursoTurno() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("curso", "Curso"));
		itens.add(new SelectItem("turno", "Turno"));
		return itens;
	}

	public void inicializarDadoConsultaCentroResultadoAdministrativo() {
		try {
			setCentroResultadoAdministrativo(true);
			inicializarDadosComunsCentroResultado();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}	

	private void inicializarDadosComunsCentroResultado() {
		setCentroResultadoDataModelo(new DataModelo());
		getCentroResultadoDataModelo().setCampoConsulta(CentroResultadoVO.enumCampoConsultaCentroResultado.DESCRICAO_CENTRO_RESULTADO.name());
	}

	public void selecionarCentroResultado() {
		try {
			CentroResultadoVO obj = (CentroResultadoVO) context().getExternalContext().getRequestMap().get("centroResultadoItens");
			if (isCentroResultadoAdministrativo()) {
				getRequisicaoVO().setCentroResultadoAdministrativo(obj);
			} 
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
			getFacadeFactory().getCentroResultadoFacade().consultar(SituacaoEnum.ATIVO, true, getRequisicaoVO().getDepartamento(), getRequisicaoVO().getCurso(), getRequisicaoVO().getTurma(), getCentroResultadoDataModelo());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public String consultarDepartamento() {
		try {
			List<DepartamentoVO> objs = new ArrayList<>(0);
			Integer unidadeEnsino = 0;
			if (Uteis.isAtributoPreenchido(getRequisicaoVO().getUnidadeEnsino())) {
				unidadeEnsino = getRequisicaoVO().getUnidadeEnsino().getCodigo();
			} else {
				unidadeEnsino = getUnidadeEnsinoLogado().getCodigo();
			}
			if (getCampoConsultaDepartamento().equals("codigo")) {
				int valorInt = 0;
				if (!getValorConsultaDepartamento().equals("")) {
					valorInt = Integer.parseInt(getValorConsultaDepartamento());
				}
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorCodigoPorUnidadeEnsino(new Integer(valorInt), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaDepartamento().equals("nome")) {
				objs = getFacadeFactory().getDepartamentoFacade().consultarPorNomePorUnidadeEnsino(getValorConsultaDepartamento(), unidadeEnsino, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaDepartamento(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList<>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	public void selecionarDepartamento()  {
		try {
			DepartamentoVO obj = (DepartamentoVO) context().getExternalContext().getRequestMap().get("departamentoItens");
			getRequisicaoVO().setDepartamento(obj);
			preencherDadosPorCategoriaDespesa();
			getListaConsultaDepartamento().clear();
			setValorConsultaDepartamento("");
			setCampoConsultaDepartamento("");
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaTurma().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public List<SelectItem> getTipoConsultaComboDepartamento() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public RequisicaoItemVO getRequisicaoItemVO() {
		if (requisicaoItemVO == null) {
			requisicaoItemVO = new RequisicaoItemVO();
		}
		return requisicaoItemVO;
	}

	public void setRequisicaoItemVO(RequisicaoItemVO requisicaoItemVO) {
		this.requisicaoItemVO = requisicaoItemVO;
	}

	public RequisicaoVO getRequisicaoVO() {
		if (requisicaoVO == null) {
			requisicaoVO = new RequisicaoVO();
		}
		return requisicaoVO;
	}

	public void setRequisicaoVO(RequisicaoVO requisicaoVO) {
		this.requisicaoVO = requisicaoVO;
	}

	public List<SelectItem> getListaSelectItemCentroCusto() {
		if (listaSelectItemCentroCusto == null) {
			listaSelectItemCentroCusto = new ArrayList<>();
		}
		return listaSelectItemCentroCusto;
	}

	public void setListaSelectItemCentroCusto(List<SelectItem> listaSelectItemCentroCusto) {
		this.listaSelectItemCentroCusto = listaSelectItemCentroCusto;
	}

	public String getCampoConsultaCategoriaProduto() {
		if (campoConsultaCategoriaProduto == null) {
			campoConsultaCategoriaProduto = "";
		}
		return campoConsultaCategoriaProduto;
	}

	public void setCampoConsultaCategoriaProduto(String campoConsultaCategoriaProduto) {
		this.campoConsultaCategoriaProduto = campoConsultaCategoriaProduto;
	}

	public List<CategoriaProdutoVO> getListaConsultaCategoriaProduto() {
		return listaConsultaCategoriaProduto;
	}

	public void setListaConsultaCategoriaProduto(List<CategoriaProdutoVO> listaConsultaCategoriaProduto) {
		this.listaConsultaCategoriaProduto = listaConsultaCategoriaProduto;
	}

	public String getValorConsultaCategoriaProduto() {
		return valorConsultaCategoriaProduto;
	}

	public void setValorConsultaCategoriaProduto(String valorConsultaCategoriaProduto) {
		this.valorConsultaCategoriaProduto = valorConsultaCategoriaProduto;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		requisicaoVO = null;
		requisicaoItemVO = null;
		campoConsultaProduto = null;
		valorConsultaProduto = null;
		Uteis.liberarListaMemoria(listaConsultaProduto);
	}

	/**
	 * @return the campoConsultaSacadoFuncionario
	 */
	public String getCampoConsultaSacadoFuncionario() {
		return campoConsultaSacadoFuncionario;
	}

	/**
	 * @param campoConsultaSacadoFuncionario
	 *            the campoConsultaSacadoFuncionario to set
	 */
	public void setCampoConsultaSacadoFuncionario(String campoConsultaSacadoFuncionario) {
		this.campoConsultaSacadoFuncionario = campoConsultaSacadoFuncionario;
	}

	/**
	 * @return the valorConsultaSacadoFuncionario
	 */
	public String getValorConsultaSacadoFuncionario() {
		return valorConsultaSacadoFuncionario;
	}

	/**
	 * @param valorConsultaSacadoFuncionario
	 *            the valorConsultaSacadoFuncionario to set
	 */
	public void setValorConsultaSacadoFuncionario(String valorConsultaSacadoFuncionario) {
		this.valorConsultaSacadoFuncionario = valorConsultaSacadoFuncionario;
	}

	/**
	 * @return the campoConsultaSacadoFornecedor
	 */
	public String getCampoConsultaSacadoFornecedor() {
		return campoConsultaSacadoFornecedor;
	}

	/**
	 * @param campoConsultaSacadoFornecedor
	 *            the campoConsultaSacadoFornecedor to set
	 */
	public void setCampoConsultaSacadoFornecedor(String campoConsultaSacadoFornecedor) {
		this.campoConsultaSacadoFornecedor = campoConsultaSacadoFornecedor;
	}

	/**
	 * @return the valorConsultaSacadoFornecedor
	 */
	public String getValorConsultaSacadoFornecedor() {
		return valorConsultaSacadoFornecedor;
	}

	/**
	 * @param valorConsultaSacadoFornecedor
	 *            the valorConsultaSacadoFornecedor to set
	 */
	public void setValorConsultaSacadoFornecedor(String valorConsultaSacadoFornecedor) {
		this.valorConsultaSacadoFornecedor = valorConsultaSacadoFornecedor;
	}

	/**
	 * @return the listaConsultaFuncionario
	 */
	public List<FuncionarioVO> getListaConsultaFuncionario() {
		return listaConsultaFuncionario;
	}

	/**
	 * @param listaConsultaFuncionario
	 *            the listaConsultaFuncionario to set
	 */
	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	/**
	 * @return the listaConsultaFornecedor
	 */
	public List<FornecedorVO> getListaConsultaFornecedor() {
		return listaConsultaFornecedor;
	}

	/**
	 * @param listaConsultaFornecedor
	 *            the listaConsultaFornecedor to set
	 */
	public void setListaConsultaFornecedor(List<FornecedorVO> listaConsultaFornecedor) {
		this.listaConsultaFornecedor = listaConsultaFornecedor;
	}

	/**
	 * @return the valorConsultaSituacaoAutorizacao
	 */
	public String getValorConsultaSituacaoAutorizacao() {
		return valorConsultaSituacaoAutorizacao;
	}

	/**
	 * @param valorConsultaSituacaoAutorizacao
	 *            the valorConsultaSituacaoAutorizacao to set
	 */
	public void setValorConsultaSituacaoAutorizacao(String valorConsultaSituacaoAutorizacao) {
		this.valorConsultaSituacaoAutorizacao = valorConsultaSituacaoAutorizacao;
	}

	/**
	 * @return the valorConsultaSituacaoEntregaRecebimento
	 */
	public String getValorConsultaSituacaoEntregaRecebimento() {
		return valorConsultaSituacaoEntregaRecebimento;
	}

	/**
	 * @param valorConsultaSituacaoEntregaRecebimento
	 *            the valorConsultaSituacaoEntregaRecebimento to set
	 */
	public void setValorConsultaSituacaoEntregaRecebimento(String valorConsultaSituacaoEntregaRecebimento) {
		this.valorConsultaSituacaoEntregaRecebimento = valorConsultaSituacaoEntregaRecebimento;
	}

	public List<SelectItem> getListaSelectItemTurma() {
		if (listaSelectItemTurma == null) {
			listaSelectItemTurma = new ArrayList<>(0);
		}
		return listaSelectItemTurma;
	}

	public void setListaSelectItemTurma(List<SelectItem> listaSelectItemTurma) {
		this.listaSelectItemTurma = listaSelectItemTurma;
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
		listaConsultaTurma = Optional.ofNullable(listaConsultaTurma).orElse(new ArrayList<>());
		return listaConsultaTurma;
	}

	public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
		this.listaConsultaTurma = listaConsultaTurma;
	}

	public void realizarLimpezaConsultaRequisicao() {
		getListaConsulta().clear();
	}

	public void realizarLimpezaSacadoFuncionario() {
		try {
			getRequisicaoVO().setSacadoFuncionario(new FuncionarioVO());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void realizarLimpezaSacadoFornecedor() {
		try {
			getRequisicaoVO().setSacadoFornecedor(new FornecedorVO());
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String getAcaoModalQuestionario() {
		if (getAbrirModalQuestionarioAbertura()) {
			return "RichFaces.$('panelQuestionarioAbertura').show()";
		}

		if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
			return "RichFaces.$('panelQuestionarioAbertura').hide(); " + getGravadoComSucesso();
		}
		return "RichFaces.$('panelQuestionarioAbertura').hide();";
	}

	public String getAcaoModalQuestionarioFechamento() {
		if (getAbrirModalQuestionarioAbertura()) {
			return "RichFaces.$('panelQuestionarioFechamento').show()";
		}
		
		if (getUsuarioLogado().getIsApresentarVisaoAluno() || getUsuarioLogado().getIsApresentarVisaoPais()) {
			return "RichFaces.$('panelQuestionarioFechamento').hide(); " + getGravadoComSucesso();
		}
		return "RichFaces.$('panelQuestionarioFechamento').hide();";
	}

	public boolean getIsApresentarCampoDepartamento() {
		return Uteis.isAtributoPreenchido(getRequisicaoVO().getTipoNivelCentroResultadoEnum()) && (getRequisicaoVO().getTipoNivelCentroResultadoEnum().isDepartamento());
	}
	
	public boolean getIsApresentarCampoCurso() {
		return Uteis.isAtributoPreenchido(getRequisicaoVO().getTipoNivelCentroResultadoEnum()) && (getRequisicaoVO().getTipoNivelCentroResultadoEnum().isCurso());
	}

	public boolean getIsApresentarCampoCursoTurno() {
		return Uteis.isAtributoPreenchido(getRequisicaoVO().getTipoNivelCentroResultadoEnum()) && (getRequisicaoVO().getTipoNivelCentroResultadoEnum().isCursoTurno());
	}

	public boolean getIsApresentarCampoTurma() {
		return Uteis.isAtributoPreenchido(getRequisicaoVO().getTipoNivelCentroResultadoEnum()) && (getRequisicaoVO().getTipoNivelCentroResultadoEnum().isTurma());
	}
	
	public boolean getIsApresentarCampoValorUltimaCompra() {
		return getConfiguracaoFinanceiroVO().getUsaPlanoOrcamentario();
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

	public String getCampoConsultaCursoTurno() {
		if (campoConsultaCursoTurno == null) {
			campoConsultaCursoTurno = "";
		}
		return campoConsultaCursoTurno;
	}

	public void setCampoConsultaCursoTurno(String campoConsultaCursoTurno) {
		this.campoConsultaCursoTurno = campoConsultaCursoTurno;
	}

	public String getValorConsultaCursoTurno() {
		if (valorConsultaCursoTurno == null) {
			valorConsultaCursoTurno = "";
		}
		return valorConsultaCursoTurno;
	}

	public void setValorConsultaCursoTurno(String valorConsultaCursoTurno) {
		this.valorConsultaCursoTurno = valorConsultaCursoTurno;
	}

	public List<UnidadeEnsinoCursoVO> getListaConsultaCursoTurno() {
		if (listaConsultaCursoTurno == null) {
			listaConsultaCursoTurno = new ArrayList<>(0);
		}
		return listaConsultaCursoTurno;
	}

	public void setListaConsultaCursoTurno(List<UnidadeEnsinoCursoVO> listaConsultaCursoTurno) {
		this.listaConsultaCursoTurno = listaConsultaCursoTurno;
	}

	public Boolean getPermiteAlterarCategoriaDespesa() {
		return this.getFacadeFactory().getRequisicaoFacade().getPermiteAlterarCategoriaDespesa(getUsuarioLogadoClone());
	}

	public Boolean isMostrarBotaoRequisitante() {
		return this.getFacadeFactory().getRequisicaoFacade().getPermiteAlterarRequisitante(getUsuarioLogado()) || this.getFacadeFactory().getRequisicaoFacade().getPermiteCadastrarRequisicaoTodasUnidadesEnsino(getUsuarioLogadoClone());
	}

	public void setListaConsultaCentroDespesa(List<CategoriaDespesaVO> listaConsultaCentroDespesa) {
		this.listaConsultaCentroDespesa = listaConsultaCentroDespesa;
	}

	public List<CategoriaDespesaVO> getListaConsultaCentroDespesa() {
		if (listaConsultaCentroDespesa == null) {
			listaConsultaCentroDespesa = new ArrayList<>(0);
		}
		return listaConsultaCentroDespesa;
	}

	public String getCampoConsultaCentroDespesa() {
		return campoConsultaCentroDespesa;
	}

	public void setCampoConsultaCentroDespesa(String campoConsultaCentroDespesa) {
		this.campoConsultaCentroDespesa = campoConsultaCentroDespesa;
	}

	public String getValorConsultaCentroDespesa() {
		return valorConsultaCentroDespesa;
	}

	public void setValorConsultaCentroDespesa(String valorConsultaCentroDespesa) {
		this.valorConsultaCentroDespesa = valorConsultaCentroDespesa;
	}

	public DataModelo getResponsavelDataModelo() throws Exception {

		this.responsavelDataModelo = Optional.ofNullable(this.responsavelDataModelo).orElseGet(() -> {
			DataModelo dataModelo = new DataModelo();
			dataModelo.setOffset(0);
			dataModelo.setLimitePorPagina(10);

			dataModelo.setPaginaAtual(1);
			return dataModelo;
		});
		return responsavelDataModelo;
	}

	private void limparResponsavelDataModelo() {
		this.responsavelDataModelo = null;
	}

	public void scrollerListenerUsuario(DataScrollEvent dataScrollEvent) throws Exception {
		getResponsavelDataModelo().setPaginaAtual(dataScrollEvent.getPage());
		getResponsavelDataModelo().setPage(dataScrollEvent.getPage());
		consultarResponsavel();
	}

	public void setResponsavelDataModelo(DataModelo responsavelDataModelo) {
		this.responsavelDataModelo = responsavelDataModelo;
	}

	public String getNomeUsuarioPesquisa() {
		return nomeUsuarioPesquisa;
	}

	public void setNomeUsuarioPesquisa(String nomeUsuarioPesquisa) {
		this.nomeUsuarioPesquisa = nomeUsuarioPesquisa;
	}

	public String getCodigoPesquisa() {
		return codigoPesquisa;
	}

	public void setCodigoPesquisa(String codigoPesquisa) {
		this.codigoPesquisa = codigoPesquisa;
	}

	public String getNomeDepartamentoPesquisa() {
		return nomeDepartamentoPesquisa;
	}

	public void setNomeDepartamentoPesquisa(String nomeDepartamentoPesquisa) {
		this.nomeDepartamentoPesquisa = nomeDepartamentoPesquisa;
	}

	public String getSolicitantePesquisa() {
		return solicitantePesquisa;
	}

	public void setSolicitantePesquisa(String solicitantePesquisa) {
		this.solicitantePesquisa = solicitantePesquisa;
	}

	public String getCategoriaPesquisa() {
		return categoriaPesquisa;
	}

	public void setCategoriaPesquisa(String categoriaPesquisa) {
		this.categoriaPesquisa = categoriaPesquisa;
	}

	public String getProdutoCategoriaPesquisa() {
		return produtoCategoriaPesquisa;
	}

	public void setProdutoCategoriaPesquisa(String produtoCategoriaPesquisa) {
		this.produtoCategoriaPesquisa = produtoCategoriaPesquisa;
	}

	public Integer getUnidadeEnsinoPesquisa() {
		return unidadeEnsinoPesquisa;
	}

	public void setUnidadeEnsinoPesquisa(Integer unidadeEnsinoPesquisa) {
		this.unidadeEnsinoPesquisa = unidadeEnsinoPesquisa;
	}

	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<>();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<SelectItem> getListaSelectItemTipoNivelCentroResultadoEnum() {
		listaSelectItemTipoNivelCentroResultadoEnum = Optional.ofNullable(listaSelectItemTipoNivelCentroResultadoEnum).orElse(new ArrayList<>());
		return listaSelectItemTipoNivelCentroResultadoEnum;
	}

	public void setListaSelectItemTipoNivelCentroResultadoEnum(List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum) {
		this.listaSelectItemTipoNivelCentroResultadoEnum = listaSelectItemTipoNivelCentroResultadoEnum;
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

	public String getValorConsultaDepartamento() {
		if (valorConsultaDepartamento == null) {
			valorConsultaDepartamento = "";
		}
		return valorConsultaDepartamento;
	}

	public void setValorConsultaDepartamento(String valorConsultaDepartamento) {
		this.valorConsultaDepartamento = valorConsultaDepartamento;
	}

	public List<DepartamentoVO> getListaConsultaDepartamento() {
		if (listaConsultaDepartamento == null) {
			listaConsultaDepartamento = new ArrayList<>(0);
		}
		return listaConsultaDepartamento;
	}

	public void setListaConsultaDepartamento(List<DepartamentoVO> listaConsultaDepartamento) {
		this.listaConsultaDepartamento = listaConsultaDepartamento;
	}

	public boolean isCentroResultadoAdministrativo() {
		return centroResultadoAdministrativo;
	}

	public void setCentroResultadoAdministrativo(boolean centroResultadoAdministrativo) {
		this.centroResultadoAdministrativo = centroResultadoAdministrativo;
	}

	public DataModelo getCentroResultadoDataModelo() {
		centroResultadoDataModelo = Optional.ofNullable(centroResultadoDataModelo).orElse(new DataModelo());
		return centroResultadoDataModelo;
	}

	public void setCentroResultadoDataModelo(DataModelo centroResultadoDataModelo) {
		this.centroResultadoDataModelo = centroResultadoDataModelo;
	}

	public boolean isPermiteAlterarCentroResultado() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITE_ALTERAR_CENTRO_RESULTADO_REQUISICAO, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public boolean isPermiteAnexarArquivo() {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoComprasEnum.PERMITIR_ANEXAR_ARQUIVO, getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			if (Uteis.isAtributoPreenchido(getRequisicaoVO().getArquivoVO().getNome())) {
				getFacadeFactory().getArquivoHelper().removerArquivoDiretorio(true, getRequisicaoVO().getArquivoVO(), getRequisicaoVO().getArquivoVO().getPastaBaseArquivo(), getConfiguracaoGeralPadraoSistema());
			}
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getRequisicaoVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.ARQUIVO_TMP, getUsuarioLogado());
			getRequisicaoVO().getArquivoVO().setDescricao(uploadEvent.getUploadedFile().getName().substring(0, uploadEvent.getUploadedFile().getName().lastIndexOf(".")));
			
			setOncompleteModal("RichFaces.$('panelArquivo').hide()");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	} 
	
	public void downloadArquivo() throws Exception {
		try {
		
			String arquivo = (getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator + PastaBaseArquivoEnum.ARQUIVO.getValue() + File.separator  + getRequisicaoVO().getArquivoVO().getCpfAlunoDocumentacao() + File.separator + getRequisicaoVO().getArquivoVO().getNome());
			InputStream fs = new FileInputStream(arquivo);
			
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", getRequisicaoVO().getArquivoVO().getNome());
			request.getSession().setAttribute("pastaBaseArquivo", arquivo.substring(0, arquivo.lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO() {
		if (configuracaoFinanceiroVO == null) {
			try {
				configuracaoFinanceiroVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getRequisicaoVO().getUnidadeEnsino().getCodigo());
			} catch (Exception e) {
				e.printStackTrace();
				configuracaoFinanceiroVO = new ConfiguracaoFinanceiroVO();
			}
		}
		return configuracaoFinanceiroVO;
	}

	public void setConfiguracaoFinanceiroVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
		this.configuracaoFinanceiroVO = configuracaoFinanceiroVO;
	}	
	
	public Boolean getAbrirModalQuestionarioAbertura() {
		if (abrirModalQuestionarioAbertura == null) {
			abrirModalQuestionarioAbertura = false;
		}
		return abrirModalQuestionarioAbertura;
	}

	public void setAbrirModalQuestionarioAbertura(Boolean abrirModalQuestionarioAbertura) {
		this.abrirModalQuestionarioAbertura = abrirModalQuestionarioAbertura;
	}

	public Boolean getAbrirModalQuestionarioFechamento() {
		if (abrirModalQuestionarioFechamento == null) {
			abrirModalQuestionarioFechamento = Boolean.FALSE;
		}
		return abrirModalQuestionarioFechamento;
	}

	public void setAbrirModalQuestionarioFechamento(Boolean abrirModalQuestionarioFechamento) {
		this.abrirModalQuestionarioFechamento = abrirModalQuestionarioFechamento;
	}

	public String getGravadoComSucesso() {
		if (gravadoComSucesso == null) {
			gravadoComSucesso = "";
		}
		return gravadoComSucesso;
	}

	public void setGravadoComSucesso(String gravadoComSucesso) {
		this.gravadoComSucesso = gravadoComSucesso;
	}

	public QuestionarioVO getQuestionarioVO() {
		if (questionarioVO == null) {
			questionarioVO = new QuestionarioVO();
		}
		return questionarioVO;
	}

	public void setQuestionarioVO(QuestionarioVO questionarioVO) {
		this.questionarioVO = questionarioVO;
	}
	public String getNomeCategoriaProduto() {
		if(nomeCategoriaProduto == null) {
			nomeCategoriaProduto = "";
		}
		return nomeCategoriaProduto;
	}

	public void setNomeCategoriaProduto(String nomeCategoriaProduto) {
		this.nomeCategoriaProduto = nomeCategoriaProduto;
	}

	public String getDescricaoCentroResultadoAcademico() {
		if(descricaoCentroResultadoAcademico == null) {
			descricaoCentroResultadoAcademico = "";
		}
		return descricaoCentroResultadoAcademico;
	}

	public void setDescricaoCentroResultadoAcademico(String descricaoCentroResultadoAcademico) {
		this.descricaoCentroResultadoAcademico = descricaoCentroResultadoAcademico;
	}
	public void removerArquivo() {
		try {			
			if (Uteis.isAtributoPreenchido(getRequisicaoVO().getArquivoVO().getNome())) {
				getFacadeFactory().getArquivoHelper().removerArquivoDiretorio(true, getRequisicaoVO().getArquivoVO(), getRequisicaoVO().getArquivoVO().getPastaBaseArquivo(), getConfiguracaoGeralPadraoSistema());
				getFacadeFactory().getArquivoFacade().excluir(getRequisicaoVO().getArquivoVO(), getUsuarioLogado(), getConfiguracaoGeralPadraoSistema());
			}
			getRequisicaoVO().setArquivoVO(null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}

	}

}

