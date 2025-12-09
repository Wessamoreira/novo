package controle.administrativo;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas 
 * configuracaoGeralSistemaForm.jsp configuracaoGeralSistemaCons.jsp) com as funcionalidades da classe <code>ConfiguracaoGeralSistema</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see ConfiguracaoGeralSistema
 * @see ConfiguracaoGeralSistemaVO
 */

import controle.arquitetura.ConfiguracaoControleInterface;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import controle.basico.ConfiguracoesControle;
import jobs.JobEnvioEmail;
import jobs.JobSymplicty;
import jobs.enumeradores.FornecedorSmsEnum;
import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.academico.MotivoCancelamentoTrancamentoVO;
import negocio.comuns.academico.TextoPadraoDeclaracaoVO;
import negocio.comuns.administrativo.*;
import negocio.comuns.arquitetura.PerfilAcessoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.ServidorArquivoOnlineEnum;
import negocio.comuns.avaliacaoinst.AvaliacaoInstitucionalVO;
import negocio.comuns.basico.ConfiguracoesVO;
import negocio.comuns.basico.PaizVO;
//import negocio.comuns.financeiro.PerfilEconomicoVO;
import negocio.comuns.processosel.QuestionarioVO;
import negocio.comuns.processosel.enumeradores.TipoEscopoQuestionarioPerguntaEnum;
import negocio.comuns.protocolo.TipoRequerimentoVO;
import negocio.comuns.utilitarias.*;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;


import org.apache.commons.beanutils.BeanUtils;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.primefaces.event.FileUploadEvent;


import jakarta.annotation.PostConstruct;
import jakarta.faces.model.SelectItem;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Base64.Decoder;

import com.jcraft.jsch.*;

@SuppressWarnings({"rawtypes", "unchecked"})
@Controller("ConfiguracaoGeralSistemaControle")
@Scope("viewScope")
@Lazy
public class ConfiguracaoGeralSistemaControle extends SuperControle implements ConfiguracaoControleInterface, Serializable {

	private static final long serialVersionUID = 7539596345559654886L;
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
	protected List<SelectItem> listaSelectItemVisaoPadraoPais;
	protected List<SelectItem> listaSelectItemPais;
	protected List<SelectItem> listaSelectItemFornecedorSMS;
	protected List<SelectItem> listaSelectItemMotivoPadraoAbandonoCurso;
	protected List<SelectItem> listaSelectItemVisaoPadraoAluno;
	protected List<SelectItem> listaSelectItemVisaoPadraoProfessor;
	protected List<SelectItem> listaSelectItemVisaoPadraoCandidato;
	protected List<SelectItem> listaSelectItemVisaoPadraoCoordenador;
	protected List<SelectItem> listaSelectItemPerfilPadraoAluno;
	protected List<SelectItem> listaSelectItemPerfilPadraoPais;
	protected List<SelectItem> listaSelectItemPerfilPadraoOuvidoria;
	protected List<SelectItem> listaSelectItemPerfilPadraoProfessorGraduacao;
	private List<SelectItem> listaSelectItemPerfilPadraoProfessorPosGraduacao;
	private List<SelectItem> listaSelectItemPerfilPadraoProfessorExtensao;
	protected List<SelectItem> listaSelectItemPerfilPadraoCandidato;
	protected List<SelectItem> listaSelectItemPerfilPadraoCoordenador;
	protected List<SelectItem> listaSelectItemPerfilPadraoParceiro;
	protected List<SelectItem> listaSelectItemQuestionario;
	protected List<SelectItem> listaSelectItemPerfilEconomico;
	private List<SelectItem> listaSelectAvaliacaoInstitucionalFinalModuloAluno;
	private List<SelectItem> listaSelectAvaliacaoInstitucionalFinalModuloProfessor;
	protected ConfiguracoesControle configuracoesControle;
	private List<FuncionarioVO> listaConsultaFuncionario;
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private ConfiguracoesVO configuracoesVO;
	private Boolean editandoAPartirFormConfiguracores;
	protected List<SelectItem> listaSelectItemDepartamento;
	private List<SelectItem> listaSelectItemTipoRequerimento;
	private List<SelectItem> listaSelectItemGrupoDestinatarios;
	private List<SelectItem> listaSelectItemServidorArquivoOnline;
	private String abaNavegar;
	protected List<SelectItem> listaSelectItemPerfilAcessoAlunoNaoAssinouContratoMatricula;
	private ConfiguracaoLdapVO configuracaoLdapVO;
	private List<SelectItem> listaSelectItemTextoPadraoDeclaracao;
	private Boolean apesentarPanelCancelamentoMatriculaOnline;
	
	protected List<SelectItem> listaSelectItemQuestionarioPlanoEnsino;
	protected List<SelectItem> listaSelectItemMotivoPadraoCancelamentoOutraMatricula;
	private Boolean apresentarDocumentoPortalTransparenciaComPendenciaAssinatura;
	private Boolean apresentarParaUsuarioOtimize;

	public ConfiguracaoGeralSistemaControle() {
		// obterUsuarioLogado();
		novo();
		// consultarConfiguracaoGeralPadrao();
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * @author Geber
	 * 
	 * Método para recuperar a configuração criada em configuracoesForm.xhtml, pelo fato do ConfiguracaoAcademicoControle ser instanciado por outro controle (ConfiguraçõesControle)
	 * foi necessário criar essa função, pois após a navegação da página o esse controlador era instanciado e os dados perdidos. 
	 * Após recuperar o objeto da sessão, é setado no atributo configuraçõesVO e removido da sessão.
	 * 
	 */
	@PostConstruct
	private void recuperarConfiguracaoPeloForm(){
		if(context().getExternalContext().getSessionMap().get("configuracoesItem") != null) {
			setConfiguracoesVO((ConfiguracoesVO) context().getExternalContext().getSessionMap().get("configuracoesItem"));
			context().getExternalContext().getSessionMap().remove("configuracoesItem");
			editar();
		}		
	}

	public void iniciarControleConfiguracao(ConfiguracoesVO configuracoesVO, ConfiguracoesControle configuracoesControle) {
		try {
			setConfiguracoesControle(configuracoesControle);
			setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoConfiguracoes(configuracoesVO.getCodigo(), false, getUsuarioLogado(), Uteis.NIVELMONTARDADOS_TODOS));
			if (configuracoesVO.getCodigo().intValue() == 0) {
				novo();
			} else {
				getConfiguracaoGeralSistemaVO().setConfiguracoesVO(configuracoesVO);
			}			
		}catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparCamposParaClone() {
		getConfiguracaoGeralSistemaVO().setCodigo(0);
		getConfiguracaoGeralSistemaVO().setNovoObj(true);
		getConfiguracaoGeralSistemaVO().getCertificadoParaDocumento().setCodigo(0);
		getConfiguracaoGeralSistemaVO().getConfiguracaoAtualizacaoCadastralVO().setCodigo(0);
		getConfiguracaoGeralSistemaVO().getConfiguracaoCandidatoProcessoSeletivoVO().setCodigo(0);
	}

	public void consultarConfiguracaoGeralPadrao() {
		try {
			setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoUnidadeEnsino(0, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		} catch (Exception e) {
			setConfiguracaoGeralSistemaVO(new ConfiguracaoGeralSistemaVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe
	 * <code>ConfiguracaoGeralSistema</code> para edição pelo usuário da
	 * aplicação.
	 */
	public void novo() {
		setConfiguracaoGeralSistemaVO(new ConfiguracaoGeralSistemaVO());
		inicializarListasSelectItemTodosComboBox();
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>ConfiguracaoGeralSistema</code> para alteração. O objeto desta
	 * classe é disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		try {
			setConfiguracoesVO(getFacadeFactory().getConfiguracoesFacade().consultarPorChavePrimaria(getConfiguracoesVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			ConfiguracaoGeralSistemaVO objGeral = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoConfiguracoes(getConfiguracoesVO().getCodigo(), false, getUsuarioLogado(), Uteis.NIVELMONTARDADOS_TODOS);			
			setConfiguracaoGeralSistemaVO(objGeral);
			inicializarListasSelectItemTodosComboBox();
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return  Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoGeralSistemaForm");
	}

	public void gravar() {
		try {
			configuracaoGeralSistemaVO.setUpdated(new Date());
			if (configuracaoGeralSistemaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getConfiguracaoGeralSistemaFacade().incluir(configuracaoGeralSistemaVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getConfiguracaoGeralSistemaFacade().alterar(configuracaoGeralSistemaVO, getUsuarioLogado());
				getAplicacaoControle().removerConfiguracaoGeralSistemaEmNivelAplicacao(configuracaoGeralSistemaVO);
			}
			if(configuracaoGeralSistemaVO.getConfiguracoesVO().getPadrao()) {
				getAplicacaoControle().setConfiguracaoAparenciaSistemaVO(null);
				getAplicacaoControle().getConfiguracaoAparenciaSistemaVO();
			}
			Uteis.criarDiretoriosDeArquivos(configuracaoGeralSistemaVO);
			if(configuracaoGeralSistemaVO.getConfiguracoesVO().getPadrao()) {
				if(configuracaoGeralSistemaVO.getApresentarMensagemTelaLogin()){
					getAplicacaoControle().setMensagemTelaLogin(configuracaoGeralSistemaVO.getMensagemTelaLogin());
				}else{
					getAplicacaoControle().setMensagemTelaLogin("");
				}
			}
			
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void forcarAtualizacaoTodosAlunos() {
		try {
			getFacadeFactory().getPessoaFacade().resetarDataAtualizacaoCadastralGeral(getUsuarioLogado());
			setMensagemID("msg_OperacaoRealizadaSucesso");
		} catch (Exception e) {
		}
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>ConfiguracaoGeralSistema</code>. Caso o objeto seja novo
	 * (ainda não gravado no BD) é acionado a operação <code>incluir()</code>.
	 * Caso contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 * 
	 * @deprecated Era utilizado quando todas as matriculas eram gravadas ao
	 *             mesmo tempo. removida por Edigar em 27/06/12
	 */
	public void gravar(ConfiguracoesVO configuracoesVO) {
		try {
			
			getConfiguracaoGeralSistemaVO().setConfiguracoesVO(configuracoesVO);
			if (configuracaoGeralSistemaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getConfiguracaoGeralSistemaFacade().incluir(configuracaoGeralSistemaVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getConfiguracaoGeralSistemaFacade().alterar(configuracaoGeralSistemaVO, getUsuarioLogado());
				getAplicacaoControle().removerConfiguracaoGeralSistemaEmNivelAplicacao(configuracaoGeralSistemaVO);
			}
		}catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * ConfiguracaoGeralSistemaCons.jsp. Define o tipo de consulta a ser
	 * executada, por meio de ComboBox denominado campoConsulta, disponivel
	 * neste mesmo JSP. Como resultado, disponibiliza um List com os objetos
	 * selecionados na sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List objs = new ArrayList(0);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("smptPadrao")) {
				objs = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorSmptPadrao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeVisao")) {
				objs = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorNomeVisao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeVisao")) {
				objs = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorNomeVisao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeVisao")) {
				objs = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorNomeVisao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePerfilAcesso")) {
				objs = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorNomePerfilAcesso(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePerfilAcesso")) {
				objs = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorNomePerfilAcesso(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePerfilAcesso")) {
				objs = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorNomePerfilAcesso(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
			definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe
	 * <code>ConfiguracaoGeralSistemaVO</code> Após a exclusão ela
	 * automaticamente aciona a rotina para uma nova inclusão.
	 * 
	 * @return
	 */
	public String excluir() {
		try {
			getFacadeFactory().getConfiguracaoGeralSistemaFacade().excluir(configuracaoGeralSistemaVO, getUsuarioLogado());
			setConfiguracaoGeralSistemaVO(new ConfiguracaoGeralSistemaVO());
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return "editar";
	}

	public void irPaginaInicial() {
		controleConsulta.setPaginaAtual(1);
		this.consultar();
	}

	public void irPaginaAnterior() {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
		this.consultar();
	}

	public void irPaginaPosterior() {
		controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
		this.consultar();
	}

	public void irPaginaFinal() {
		controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
		this.consultar();
	}

	public void montarListaSelectItemGrupoDestinatarios() {
//		try {
//			List lista = getFacadeFactory().getGrupoDestinatariosFacade().consultarPorCodigo(0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
//			setListaSelectItemGrupoDestinatarios(UtilSelectItem.getListaSelectItem(lista, "codigo", "nomeGrupo"));
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>PerfilPadraoCandidato</code>.
	 */
	public void montarListaSelectItemPerfilPadraoCandidato(String prm) {
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
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>PerfilPadraoProfessor</code>.
	 */
	public void montarListaSelectItemPerfilPadraoProfessor(String prm) {
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
			setListaSelectItemPerfilPadraoProfessorGraduacao(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemPerfilPadraoProfessorPosGraduacao(String prm) {
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
			setListaSelectItemPerfilPadraoProfessorPosGraduacao(objs);
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
		}
	}
	
	public void montarListaSelectItemPerfilPadraoProfessorExtensao() {
		try {
			montarListaSelectItemPerfilPadraoProfessorExtensao("");
		} catch (Exception e) {
		}
	}
	
	public void montarListaSelectItemPerfilPadraoProfessorExtensao(String prm) {
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
			setListaSelectItemPerfilPadraoProfessorExtensao(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>PerfilPadraoProfessor</code>.
	 */
	public void montarListaSelectItemPerfilPadraoCoordenador(String prm) {
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
			setListaSelectItemPerfilPadraoCoordenador(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemPerfilPadraoProfessorPosGraduacao() {
		try {
			montarListaSelectItemPerfilPadraoProfessorPosGraduacao("");
		} catch (Exception e) {
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
	public void montarListaSelectItemPerfilPadraoCoordenador() {
		try {
			montarListaSelectItemPerfilPadraoCoordenador("");
		} catch (Exception e) {
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>PerfilPadraoProfessor</code>.
	 */
	public void montarListaSelectItemPerfilPadraoParceiro(String prm) {
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
			setListaSelectItemPerfilPadraoParceiro(objs);
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
	public void montarListaSelectItemPerfilPadraoParceiro() {
		try {
			montarListaSelectItemPerfilPadraoParceiro("");
		} catch (Exception e) {
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>PerfilPadraoAluno</code>.
	 */
	public void montarListaSelectItemPerfilPadraoAluno(String prm) {
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

	public void montarListaSelectItemPerfilPadraoAluno() {
		try {
			montarListaSelectItemPerfilPadraoAluno("");
		} catch (Exception e) {
		}
	}

	public void montarListaSelectItemPerfilPadraoPais(String prm) {
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
			setListaSelectItemPerfilPadraoPais(objs);
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
	public void montarListaSelectItemPerfilPadraoPais() {
		try {
			montarListaSelectItemPerfilPadraoPais("");
		} catch (Exception e) {
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarPerfilAcessoPorNome(String nomePrm) {
		try {
			List lista = getFacadeFactory().getPerfilAcessoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			return lista;
		}catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
			return new ArrayList(0);
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo
	 * <code>PerfilPadraoAluno</code>. Buscando todos os objetos correspondentes
	 * a entidade <code>PerfilAcesso</code>. Esta rotina não recebe parâmetros
	 * para filtragem de dados, isto é importante para a inicialização dos dados
	 * da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemPerfilPadraoOuvidoria() {
		try {
			montarListaSelectItemPerfilPadraoOuvidoria("");
		} catch (Exception e) {
		}
	}

	public void montarListaSelectItemPerfilPadraoOuvidoria(String prm) {
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
			setListaSelectItemPerfilPadraoOuvidoria(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>PerfilPadraoAluno</code>.
	 */
	public void montarListaSelectItemQuestionario(String prm) {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarQuestionarioPorDescricao(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				QuestionarioVO obj = (QuestionarioVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
			}
			setListaSelectItemQuestionario(objs);
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
	public void montarListaSelectItemQuestionario() {
		try {
			montarListaSelectItemQuestionario("");
		} catch (Exception e) {
		}
	}

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */
	public List consultarQuestionarioPorDescricao(String nomePrm) {
		try {
			List lista = getFacadeFactory().getQuestionarioFacade().consultarPorDescricao(nomePrm, "AI", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			return lista;
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return new ArrayList(0);
		}
	}

//	/**
//	 * Método responsável por gerar uma lista de objetos do tipo
//	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
//	 * <code>VisaoPadraoCandidato</code>.
//	 */
//	public void montarListaSelectItemVisaoPadraoCandidato(String prm) {
//		List resultadoConsulta = null;
//		Iterator i = null;
//		try {
//			resultadoConsulta = consultarVisaoPorNome(prm);
//			i = resultadoConsulta.iterator();
//			List objs = new ArrayList(0);
//			objs.add(new SelectItem(0, ""));
//			while (i.hasNext()) {
//				VisaoVO obj = (VisaoVO) i.next();
//				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
//			}
//			setListaSelectItemVisaoPadraoCandidato(objs);
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			Uteis.liberarListaMemoria(resultadoConsulta);
//			i = null;
//		}
//	}
//
//	/**
//	 * Método responsável por atualizar o ComboBox relativo ao atributo
//	 * <code>VisaoPadraoCandidato</code>. Buscando todos os objetos
//	 * correspondentes a entidade <code>Visao</code>. Esta rotina não recebe
//	 * parâmetros para filtragem de dados, isto é importante para a
//	 * inicialização dos dados da tela para o acionamento por meio requisições
//	 * Ajax.
//	 */
//	public void montarListaSelectItemVisaoPadraoCandidato() {
//		try {
//			montarListaSelectItemVisaoPadraoCandidato("");
//		} catch (Exception e) {
//		}
//	}
//
//	/**
//	 * Método responsável por gerar uma lista de objetos do tipo
//	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
//	 * <code>VisaoPadraoProfessor</code>.
//	 */
//	public void montarListaSelectItemVisaoPadraoProfessor(String prm) {
//		List resultadoConsulta = null;
//		Iterator i = null;
//		try {
//			resultadoConsulta = consultarVisaoPorNome(prm);
//			i = resultadoConsulta.iterator();
//			List objs = new ArrayList(0);
//			objs.add(new SelectItem(0, ""));
//			while (i.hasNext()) {
//				VisaoVO obj = (VisaoVO) i.next();
//				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
//			}
//			setListaSelectItemVisaoPadraoProfessor(objs);
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			Uteis.liberarListaMemoria(resultadoConsulta);
//			i = null;
//		}
//	}
//
//	/**
//	 * Método responsável por atualizar o ComboBox relativo ao atributo
//	 * <code>VisaoPadraoProfessor</code>. Buscando todos os objetos
//	 * correspondentes a entidade <code>Visao</code>. Esta rotina não recebe
//	 * parâmetros para filtragem de dados, isto é importante para a
//	 * inicialização dos dados da tela para o acionamento por meio requisições
//	 * Ajax.
//	 */
//	public void montarListaSelectItemVisaoPadraoProfessor() {
//		try {
//			montarListaSelectItemVisaoPadraoProfessor("");
//		} catch (Exception e) {
//		}
//	}
//
//	/**
//	 * Método responsável por gerar uma lista de objetos do tipo
//	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
//	 * <code>VisaoPadraoProfessor</code>.
//	 */
//	public void montarListaSelectItemVisaoPadraoCoordenador(String prm) {
//		List resultadoConsulta = null;
//		Iterator i = null;
//		try {
//			resultadoConsulta = consultarVisaoPorNome(prm);
//			i = resultadoConsulta.iterator();
//			List objs = new ArrayList(0);
//			objs.add(new SelectItem(0, ""));
//			while (i.hasNext()) {
//				VisaoVO obj = (VisaoVO) i.next();
//				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
//			}
//			setListaSelectItemVisaoPadraoCoordenador(objs);
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			Uteis.liberarListaMemoria(resultadoConsulta);
//			i = null;
//		}
//	}
//
//	/**
//	 * Método responsável por atualizar o ComboBox relativo ao atributo
//	 * <code>VisaoPadraoProfessor</code>. Buscando todos os objetos
//	 * correspondentes a entidade <code>Visao</code>. Esta rotina não recebe
//	 * parâmetros para filtragem de dados, isto é importante para a
//	 * inicialização dos dados da tela para o acionamento por meio requisições
//	 * Ajax.
//	 */
//	public void montarListaSelectItemVisaoPadraoCoordenador() {
//		try {
//			montarListaSelectItemVisaoPadraoCoordenador("");
//		} catch (Exception e) {
//		}
//	}
//
//	/**
//	 * Método responsável por gerar uma lista de objetos do tipo
//	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
//	 * <code>VisaoPadraoAluno</code>.
//	 */
//	public void montarListaSelectItemVisaoPadraoPais(String prm) {
//		List resultadoConsulta = null;
//		Iterator i = null;
//		try {
//			resultadoConsulta = consultarVisaoPorNome(prm);
//			i = resultadoConsulta.iterator();
//			List objs = new ArrayList(0);
//			objs.add(new SelectItem(0, ""));
//			while (i.hasNext()) {
//				VisaoVO obj = (VisaoVO) i.next();
//				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
//			}
//			setListaSelectItemVisaoPadraoPais(objs);
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			Uteis.liberarListaMemoria(resultadoConsulta);
//			i = null;
//		}
//	}
//
//	public void montarListaSelectItemVisaoPadraoPais() {
//		try {
//			montarListaSelectItemVisaoPadraoPais("");
//		} catch (Exception e) {
//		}
//	}
//
//	public void montarListaSelectItemVisaoPadraoAluno(String prm) {
//		List resultadoConsulta = null;
//		Iterator i = null;
//		try {
//			resultadoConsulta = consultarVisaoPorNome(prm);
//			i = resultadoConsulta.iterator();
//			List objs = new ArrayList(0);
//			objs.add(new SelectItem(0, ""));
//			while (i.hasNext()) {
//				VisaoVO obj = (VisaoVO) i.next();
//				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
//			}
//			setListaSelectItemVisaoPadraoAluno(objs);
//		} catch (Exception e) {
//			throw e;
//		} finally {
//			Uteis.liberarListaMemoria(resultadoConsulta);
//			i = null;
//		}
//	}
//
//	/**
//	 * Método responsável por atualizar o ComboBox relativo ao atributo
//	 * <code>VisaoPadraoAluno</code>. Buscando todos os objetos correspondentes
//	 * a entidade <code>Visao</code>. Esta rotina não recebe parâmetros para
//	 * filtragem de dados, isto é importante para a inicialização dos dados da
//	 * tela para o acionamento por meio requisições Ajax.
//	 */
//	public void montarListaSelectItemVisaoPadraoAluno() {
//		try {
//			montarListaSelectItemVisaoPadraoAluno("");
//		} catch (Exception e) {
//		}
//	}
//
//	/**
//	 * Método responsável por consultar dados da entidade
//	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
//	 * lista (<code>List</code>) utilizada para definir os valores a serem
//	 * apresentados no ComboBox correspondente
//	 */
//	public List consultarVisaoPorNome(String nomePrm) {
//		try {
//			List lista = getFacadeFactory().getVisaoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
//			return lista;
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//			return new ArrayList(0);
//		}		
//	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>Cidade</code>.
	 */


	public void montarListaSelectAvaliacaoInstitucionalFinalModuloAluno(String prm) {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarAvaliacaoInstitucionalFinalModuloAluno(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				AvaliacaoInstitucionalVO obj = (AvaliacaoInstitucionalVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectAvaliacaoInstitucionalFinalModuloAluno(objs);
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
	public void montarListaSelectAvaliacaoInstitucionalFinalModuloAluno() {
		try {
			montarListaSelectAvaliacaoInstitucionalFinalModuloAluno("");
		} catch (Exception e) {
		}
	}

	public void montarListaSelectAvaliacaoInstitucionalFinalModuloProfessor(String prm) {
		SelectItemOrdemValor ordenador = null;
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarAvaliacaoInstitucionalFinalModuloProfessor(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				AvaliacaoInstitucionalVO obj = (AvaliacaoInstitucionalVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			ordenador = new SelectItemOrdemValor();
			Collections.sort((List) objs, ordenador);
			setListaSelectAvaliacaoInstitucionalFinalModuloProfessor(objs);
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
	public void montarListaSelectAvaliacaoInstitucionalFinalModuloProfessor() {
		try {
			montarListaSelectAvaliacaoInstitucionalFinalModuloProfessor("");
		} catch (Exception e) {
		}
	}

	

	/**
	 * Método responsável por consultar dados da entidade
	 * <code><code> e montar o atributo <code>nome</code> Este atributo é uma
	 * lista (<code>List</code>) utilizada para definir os valores a serem
	 * apresentados no ComboBox correspondente
	 */

	public List consultarAvaliacaoInstitucionalFinalModuloProfessor(String nomePrm) {
		try {
			List lista = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorSituacaoAtivaUltimoModuloPublicoAlvo(false, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			return lista;
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return new ArrayList(0);
		}
	}

	public List consultarAvaliacaoInstitucionalFinalModuloAluno(String nomePrm) {
		try {
			List lista = getFacadeFactory().getAvaliacaoInstitucionalFacade().consultaRapidaPorSituacaoAtivaUltimoModuloPublicoAlvo(true, false, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			return lista;
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return new ArrayList(0);
		}
	}

	/**
	 * Método responsável por inicializar a lista de valores (
	 * <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
//		montarListaSelectItemVisaoPadraoAluno();
		montarListaSelectItemTipoRequerimento();
//		montarListaSelectItemVisaoPadraoPais();
//		montarListaSelectItemVisaoPadraoProfessor();
//		montarListaSelectItemVisaoPadraoCandidato();
//		montarListaSelectItemVisaoPadraoCoordenador();
		montarListaSelectItemPerfilPadraoAluno();
		montarListaSelectItemPerfilPadraoPais();
		montarListaSelectItemPerfilPadraoOuvidoria();
		montarListaSelectItemPerfilPadraoProfessor();
		montarListaSelectItemPerfilPadraoProfessorPosGraduacao();
		montarListaSelectItemPerfilPadraoProfessorExtensao();
		montarListaSelectItemPerfilPadraoCandidato();
		montarListaSelectItemPerfilPadraoCoordenador();
		montarListaSelectItemPerfilPadraoParceiro();
		montarListaSelectItemQuestionario();
//		montarListaSelectItemPerfilEconomico();
		montarListaSelectAvaliacaoInstitucionalFinalModuloAluno();
		montarListaSelectAvaliacaoInstitucionalFinalModuloProfessor();
		montarListaSelectItemDepartamento();
		montarListaSelectItemGrupoDestinatarios();
		montarListaSelectItemDosServidoresDeArquivoOnline();
		montarListaSelectItemPerfilAcessoAlunoNaoAssinouContratoMatricula();
		montarListaTextoPadraoDeclaracao();

		montarListaSelectItemQuestionarioPlanoEnsino();
		montarListaSelectItemMotivoPadraoCancelamentoOutraMatricula();
	}

	public void montarListaSelectItemDepartamento(String prm) {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarDepartamentoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				DepartamentoVO obj = (DepartamentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			setListaSelectItemDepartamento(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemDepartamento() {
		try {
			montarListaSelectItemDepartamento("");
		} catch (Exception e) {
		}
	}

	public List consultarDepartamentoPorNome(String nomePrm) {
		try {
			List lista = getFacadeFactory().getDepartamentoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			return lista;
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return new ArrayList(0);
		}
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);

		itens.add(new SelectItem("smptPadrao", "SMTP Padrão"));
		itens.add(new SelectItem("nomeVisao", "Visão Padrão Aluno"));
		itens.add(new SelectItem("nomeVisao", "Visão Padrão Pais"));
		itens.add(new SelectItem("nomeVisao", "Visão Padrão Professor"));
		itens.add(new SelectItem("nomeVisao", "Visão Padrão Candidato"));
		itens.add(new SelectItem("nomePerfilAcesso", "Perfil Acesso Padrão Aluno"));
		itens.add(new SelectItem("nomePerfilAcesso", "Perfil Acesso Padrão Pais"));
		itens.add(new SelectItem("nomePerfilAcesso", "Perfil Acesso Padrão Professor"));
		itens.add(new SelectItem("nomePerfilAcesso", "Perfil Acesso Padrão Candidato"));
		itens.add(new SelectItem("codigoUnidadeEnsino", "Unidade de Ensino"));
		return itens;
	}

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
		return "consultar";
	}

	public void consultarFuncionario() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");

			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, null, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCidade(getValorConsultaFuncionario(), 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionario(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarResponsavelPadraoComunicadoInterno() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("responsavelPadraoComunicadoInterno");
		getConfiguracaoGeralSistemaVO().setResponsavelPadraoComunicadoInterno(obj.getPessoa());
	}

	public void selecionarFuncionarioBancoCurriculo() {
		try {
			FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("responsavelPadraoComunicadoInterno");
			getConfiguracaoGeralSistemaVO().setFuncionarioRespAlteracaoDados(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimaria(obj.getCodigo(), obj.getUnidadeEnsino().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
			getListaConsultaFuncionario().clear();
			setCampoConsultaFuncionario("");
			setValorConsultaFuncionario("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void limparModalDadosFuncionarioRespAlteracaoDados() {
		getListaConsultaFuncionario().clear();
		getConfiguracaoGeralSistemaVO().setFuncionarioRespAlteracaoDados(null);
	}

	public void limparDadosResponsavelPadraoComunicadoInterno() {
		getConfiguracaoGeralSistemaVO().setResponsavelPadraoComunicadoInterno(null);
	}

	public void limparModalDadosResponsavelPadraoComunicadoInterno(){
		getConfiguracaoGeralSistemaVO().setResponsavelPadraoComunicadoInterno(null);
		setValorConsultaFuncionario("");
		getListaConsultaFuncionario().clear();
	}

	public List getTipoConsultaComboFuncionario() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("cargo", "Cargo"));
		itens.add(new SelectItem("departamento", "Departamento"));
		return itens;
	}
	
	
	
	private void montarListaTextoPadraoDeclaracao() {
		try {
			setListaSelectItemTextoPadraoDeclaracao(new ArrayList<>(0));
			getListaSelectItemTextoPadraoDeclaracao().add(new SelectItem(0, ""));
			for (TextoPadraoDeclaracaoVO textoPadraoDeclaracaoVO : getFacadeFactory().getTextoPadraoDeclaracaoFacade().consultarPorTipo("OT",0,"",false,Uteis.NIVELMONTARDADOS_COMBOBOX,getUsuarioLogado())) {
		    getListaSelectItemTextoPadraoDeclaracao().add(new SelectItem(textoPadraoDeclaracaoVO.getCodigo(), textoPadraoDeclaracaoVO.getDescricao()));
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	

	public void montarListaSelectItemTipoRequerimento() {
		try {
			montarListaSelectItemTipoRequerimento("");
		} catch (Exception e) {
		}
	}

	public void montarListaSelectItemTipoRequerimento(String prm) {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarTipoRequerimentoComboBox();
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				TipoRequerimentoVO obj = (TipoRequerimentoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemTipoRequerimento(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List consultarTipoRequerimentoComboBox() {
		List lista = new ArrayList(0);
		try {
			lista = getFacadeFactory().getTipoRequerimentoFacade().consultarTipoRequerimentoComboBox(false, "AT", 0, 0, true ,getUsuarioLogado(), false);			
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return lista;
	}

	public List getListaSelectItemPerfilPadraoCandidato() {
		return (listaSelectItemPerfilPadraoCandidato);
	}

	public void setListaSelectItemPerfilPadraoCandidato(List listaSelectItemPerfilPadraoCandidato) {
		this.listaSelectItemPerfilPadraoCandidato = listaSelectItemPerfilPadraoCandidato;
	}

	public List getListaSelectItemPerfilPadraoProfessorGraduacao() {
		if (listaSelectItemPerfilPadraoProfessorGraduacao == null) {
			listaSelectItemPerfilPadraoProfessorGraduacao = new ArrayList(0);
		}
		return (listaSelectItemPerfilPadraoProfessorGraduacao);
	}

	public void setListaSelectItemPerfilPadraoProfessorGraduacao(List listaSelectItemPerfilPadraoProfessorGraduacao) {
		this.listaSelectItemPerfilPadraoProfessorGraduacao = listaSelectItemPerfilPadraoProfessorGraduacao;
	}

	public List getListaSelectItemPerfilPadraoAluno() {
		return (listaSelectItemPerfilPadraoAluno);
	}

	public void setListaSelectItemPerfilPadraoAluno(List listaSelectItemPerfilPadraoAluno) {
		this.listaSelectItemPerfilPadraoAluno = listaSelectItemPerfilPadraoAluno;
	}

	public List getListaSelectItemPerfilPadraoPais() {
		return (listaSelectItemPerfilPadraoPais);
	}

	public void setListaSelectItemPerfilPadraoPais(List listaSelectItemPerfilPadraoPais) {
		this.listaSelectItemPerfilPadraoPais = listaSelectItemPerfilPadraoPais;
	}

	public List getListaSelectItemPerfilPadraoOuvidoria() {
		return listaSelectItemPerfilPadraoOuvidoria;
	}

	public void setListaSelectItemPerfilPadraoOuvidoria(List listaSelectItemPerfilPadraoOuvidoria) {
		this.listaSelectItemPerfilPadraoOuvidoria = listaSelectItemPerfilPadraoOuvidoria;
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

	public List getListaSelectItemVisaoPadraoPais() {
		return (listaSelectItemVisaoPadraoPais);
	}

	public void setListaSelectItemVisaoPadraoPais(List listaSelectItemVisaoPadraoPais) {
		this.listaSelectItemVisaoPadraoPais = listaSelectItemVisaoPadraoPais;
	}

	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		return configuracaoGeralSistemaVO;
	}

	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}

	public List getListaSelectItemQuestionario() {
		return listaSelectItemQuestionario;
	}

	public void setListaSelectItemQuestionario(List listaSelectItemQuestionario) {
		this.listaSelectItemQuestionario = listaSelectItemQuestionario;
	}

	public List getListaSelectItemPerfilEconomico() {
		return listaSelectItemPerfilEconomico;
	}

	public void setListaSelectItemPerfilEconomico(List listaSelectItemPerfilEconomico) {
		this.listaSelectItemPerfilEconomico = listaSelectItemPerfilEconomico;
	}

	public ConfiguracoesControle getConfiguracoesControle() {
		return configuracoesControle;
	}

	public void setConfiguracoesControle(ConfiguracoesControle configuracoesControle) {
		this.configuracoesControle = configuracoesControle;
	}

	public List getListaSelectItemPerfilPadraoCoordenador() {
		return listaSelectItemPerfilPadraoCoordenador;
	}

	public void setListaSelectItemPerfilPadraoCoordenador(List listaSelectItemPerfilPadraoCoordenador) {
		this.listaSelectItemPerfilPadraoCoordenador = listaSelectItemPerfilPadraoCoordenador;
	}

	public List getListaSelectItemVisaoPadraoCoordenador() {
		return listaSelectItemVisaoPadraoCoordenador;
	}

	public void setListaSelectItemVisaoPadraoCoordenador(List listaSelectItemVisaoPadraoCoordenador) {
		this.listaSelectItemVisaoPadraoCoordenador = listaSelectItemVisaoPadraoCoordenador;
	}

	public List getListaSelectItemPerfilPadraoProfessorPosGraduacao() {
		if (listaSelectItemPerfilPadraoProfessorPosGraduacao == null) {
			listaSelectItemPerfilPadraoProfessorPosGraduacao = new ArrayList(0);
		}
		return listaSelectItemPerfilPadraoProfessorPosGraduacao;
	}

	public void setListaSelectItemPerfilPadraoProfessorPosGraduacao(List listaSelectItemPerfilPadraoProfessorPosGraduacao) {
		this.listaSelectItemPerfilPadraoProfessorPosGraduacao = listaSelectItemPerfilPadraoProfessorPosGraduacao;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		if (listaConsultaFuncionario == null) {
			listaConsultaFuncionario = new ArrayList<FuncionarioVO>(0);
		}
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public String getCampoConsultaFuncionario() {
		if (campoConsultaFuncionario == null) {
			campoConsultaFuncionario = "";
		}
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public List getListaSelectItemPerfilPadraoParceiro() {
		return listaSelectItemPerfilPadraoParceiro;
	}

	public void setListaSelectItemPerfilPadraoParceiro(List listaSelectItemPerfilPadraoParceiro) {
		this.listaSelectItemPerfilPadraoParceiro = listaSelectItemPerfilPadraoParceiro;
	}

	/**
	 * @return the configuracoesVO
	 */
	public ConfiguracoesVO getConfiguracoesVO() {
		return configuracoesVO;
	}

	/**
	 * @param configuracoesVO
	 *            the configuracoesVO to set
	 */
	public void setConfiguracoesVO(ConfiguracoesVO configuracoesVO) {
		this.configuracoesVO = configuracoesVO;
	}

	/**
	 * @return the editandoAPartirFormConfiguracores
	 */
	public Boolean getEditandoAPartirFormConfiguracores() {
		if (editandoAPartirFormConfiguracores == null) {
			return Boolean.FALSE;
		}
		return editandoAPartirFormConfiguracores;
	}

	/**
	 * @param editandoAPartirFormConfiguracores
	 *            the editandoAPartirFormConfiguracores to set
	 */
	public void setEditandoAPartirFormConfiguracores(Boolean editandoAPartirFormConfiguracores) {
		this.editandoAPartirFormConfiguracores = editandoAPartirFormConfiguracores;
	}

	/**
	 * @return the listaSelectAvaliacaoInstitucionalFinalModuloAluno
	 */
	public List getListaSelectAvaliacaoInstitucionalFinalModuloAluno() {
		return listaSelectAvaliacaoInstitucionalFinalModuloAluno;
	}

	/**
	 * @param listaSelectAvaliacaoInstitucionalFinalModuloAluno
	 *            the listaSelectAvaliacaoInstitucionalFinalModuloAluno to set
	 */
	public void setListaSelectAvaliacaoInstitucionalFinalModuloAluno(List listaSelectAvaliacaoInstitucionalFinalModuloAluno) {
		this.listaSelectAvaliacaoInstitucionalFinalModuloAluno = listaSelectAvaliacaoInstitucionalFinalModuloAluno;
	}

	/**
	 * @return the listaSelectAvaliacaoInstitucionalFinalModuloProfessor
	 */
	public List getListaSelectAvaliacaoInstitucionalFinalModuloProfessor() {
		return listaSelectAvaliacaoInstitucionalFinalModuloProfessor;
	}

	/**
	 * @param listaSelectAvaliacaoInstitucionalFinalModuloProfessor
	 *            the listaSelectAvaliacaoInstitucionalFinalModuloProfessor to
	 *            set
	 */
	public void setListaSelectAvaliacaoInstitucionalFinalModuloProfessor(List listaSelectAvaliacaoInstitucionalFinalModuloProfessor) {
		this.listaSelectAvaliacaoInstitucionalFinalModuloProfessor = listaSelectAvaliacaoInstitucionalFinalModuloProfessor;
	}

	/**
	 * @return the listaSelectItemDepartamento
	 */
	public List getListaSelectItemDepartamento() {
		if (listaSelectItemDepartamento == null) {
			listaSelectItemDepartamento = new ArrayList();
		}
		return listaSelectItemDepartamento;
	}

	/**
	 * @param listaSelectItemDepartamento
	 *            the listaSelectItemDepartamento to set
	 */
	public void setListaSelectItemDepartamento(List listaSelectItemDepartamento) {
		this.listaSelectItemDepartamento = listaSelectItemDepartamento;
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
	
	
	public List<SelectItem> getListaSelectItemTextoPadraoDeclaracao() {
		if(listaSelectItemTextoPadraoDeclaracao == null) {
			listaSelectItemTextoPadraoDeclaracao = new ArrayList<>(0);
		}
		return listaSelectItemTextoPadraoDeclaracao;
	}

	public void setListaSelectItemTextoPadraoDeclaracao(List<SelectItem> listaSelectItemTextoPadraoDeclaracao) {
		this.listaSelectItemTextoPadraoDeclaracao = listaSelectItemTextoPadraoDeclaracao;
	}

	public List<SelectItem> getListaSelectItemPais() {
		if (listaSelectItemPais == null) {
			listaSelectItemPais = new ArrayList<SelectItem>(0);
			try {
				List<PaizVO> paizVOs = getFacadeFactory().getPaizFacade().consultarPorNome("", false, getUsuarioLogado());
				for (PaizVO paiz : paizVOs) {
					listaSelectItemPais.add(new SelectItem(paiz.getCodigo(), paiz.getNome()));
					if (getConfiguracaoGeralSistemaVO().getPaisPadrao().getCodigo() == 0 && paiz.getNome().equalsIgnoreCase("brasil")) {
						getConfiguracaoGeralSistemaVO().setPaisPadrao(paiz);
					}
				}
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		return listaSelectItemPais;
	}

	public void setListaSelectItemPais(List<SelectItem> listaSelectItemPais) {
		this.listaSelectItemPais = listaSelectItemPais;
	}

	public List getListaSelectItemGrupoDestinatarios() {
		if (listaSelectItemGrupoDestinatarios == null) {
			listaSelectItemGrupoDestinatarios = new ArrayList(0);
		}
		return listaSelectItemGrupoDestinatarios;
	}

	public void setListaSelectItemGrupoDestinatarios(List listaSelectItemGrupoDestinatarios) {
		this.listaSelectItemGrupoDestinatarios = listaSelectItemGrupoDestinatarios;
	}

	public List<SelectItem> getListaSelectItemMotivoPadraoAbandonoCurso() {
		if (listaSelectItemMotivoPadraoAbandonoCurso == null) {
			listaSelectItemMotivoPadraoAbandonoCurso = new ArrayList<SelectItem>(0);
			montarListaSelectItemMotivoPadraoAbandonoCurso();
		}
		return listaSelectItemMotivoPadraoAbandonoCurso;
	}

	public void setListaSelectItemMotivoPadraoAbandonoCurso(List<SelectItem> listaSelectItemMotivoTrancamentoCancelamento) {
		this.listaSelectItemMotivoPadraoAbandonoCurso = listaSelectItemMotivoTrancamentoCancelamento;
	}

	public void montarListaSelectItemMotivoPadraoAbandonoCurso() {
		try {
			List<MotivoCancelamentoTrancamentoVO> motivoCancelamentoTrancamentoVOs = getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorNomeAtivo("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getListaSelectItemMotivoPadraoAbandonoCurso().clear();
			getListaSelectItemMotivoPadraoAbandonoCurso().add(new SelectItem(0, ""));
			for (MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamentoVO : motivoCancelamentoTrancamentoVOs) {
				getListaSelectItemMotivoPadraoAbandonoCurso().add(new SelectItem(motivoCancelamentoTrancamentoVO.getCodigo(), motivoCancelamentoTrancamentoVO.getNome()));
			}
		} catch (Exception e) {

		}
	}

	public void realizarTesteEnvioEmail() {
		JobEnvioEmail threadEmail = new JobEnvioEmail();
		ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
		try {
			threadEmail.realizarTesteEnvioEmail(getConfiguracaoGeralSistemaVO().getEmailRemetente(), getConfiguracaoGeralSistemaVO().getEmailRemetente(), getConfiguracaoGeralSistemaVO().getEmailRemetente(), getConfiguracaoGeralSistemaVO().getEmailRemetente(), "Teste Configuração Email", comunicacaoInternaVO.getMensagemComLayout("E-mail configurado com sucesso..."), getConfiguracaoGeralSistemaVO().getServidorEmailUtilizaSSL(), getConfiguracaoGeralSistemaVO().getServidorEmailUtilizaTSL(), getConfiguracaoGeralSistemaVO().getSmptPadrao(), getConfiguracaoGeralSistemaVO().getIpServidor(), getConfiguracaoGeralSistemaVO().getLogin(), getConfiguracaoGeralSistemaVO().getSenha(), getConfiguracaoGeralSistemaVO().getPortaSmtpPadrao());
			setMensagemID("msg_email_configurado", Uteis.SUCESSO, true);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			threadEmail = null;
			comunicacaoInternaVO = null;
		}
	}

	public List getListaSelectItemPerfilPadraoProfessorExtensao() {
		if (listaSelectItemPerfilPadraoProfessorExtensao == null) {
			listaSelectItemPerfilPadraoProfessorExtensao = new ArrayList(0);
		}
		return listaSelectItemPerfilPadraoProfessorExtensao;
	}

	public void setListaSelectItemPerfilPadraoProfessorExtensao(List listaSelectItemPerfilPadraoProfessorExtensao) {
		this.listaSelectItemPerfilPadraoProfessorExtensao = listaSelectItemPerfilPadraoProfessorExtensao;
	}
	
	/**
	 * @author Victor Hugo de Paula Costa 22/09/2015 09:05
	 */
	private String campoConsultaFuncionarioOperacoesExternas;
	private String valorConsultaFuncionarioOperacoesExternas;
	private List<FuncionarioVO> listaConsultaResponsavelOperacoesExternas;
	
	public String getCampoConsultaFuncionarioOperacoesExternas() {
		if(campoConsultaFuncionarioOperacoesExternas == null) {
			campoConsultaFuncionarioOperacoesExternas = "";
		}
		return campoConsultaFuncionarioOperacoesExternas;
	}

	public void setCampoConsultaFuncionarioOperacoesExternas(String campoConsultaFuncionarioOperacoesExternas) {
		this.campoConsultaFuncionarioOperacoesExternas = campoConsultaFuncionarioOperacoesExternas;
	}

	public String getValorConsultaFuncionarioOperacoesExternas() {
		if(valorConsultaFuncionarioOperacoesExternas == null) {
			valorConsultaFuncionarioOperacoesExternas = "";
		}
		return valorConsultaFuncionarioOperacoesExternas;
	}

	public void setValorConsultaFuncionarioOperacoesExternas(String valorConsultaFuncionarioOperacoesExternas) {
		this.valorConsultaFuncionarioOperacoesExternas = valorConsultaFuncionarioOperacoesExternas;
	}

	public List<FuncionarioVO> getListaConsultaResponsavelOperacoesExternas() {
		if(listaConsultaResponsavelOperacoesExternas == null) {
			listaConsultaResponsavelOperacoesExternas = new ArrayList<FuncionarioVO>();
		}
		return listaConsultaResponsavelOperacoesExternas;
	}

	public void setListaConsultaResponsavelOperacoesExternas(List<FuncionarioVO> listaConsultaResponsavelOperacoesExternas) {
		this.listaConsultaResponsavelOperacoesExternas = listaConsultaResponsavelOperacoesExternas;
	}

	public void consultarFuncionarioOperacoesExternas() {
		try {
			List objs = new ArrayList(0);
			if (getValorConsultaFuncionarioOperacoesExternas().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				return;
			}
			getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");

			if (getCampoConsultaFuncionarioOperacoesExternas().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionarioOperacoesExternas(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionarioOperacoesExternas().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionarioOperacoesExternas(), 0, null, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionarioOperacoesExternas().equals("nomeCidade")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCidade(getValorConsultaFuncionarioOperacoesExternas(), 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionarioOperacoesExternas().equals("CPF")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionarioOperacoesExternas(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionarioOperacoesExternas().equals("cargo")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionarioOperacoesExternas(), 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionarioOperacoesExternas().equals("departamento")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionarioOperacoesExternas(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionarioOperacoesExternas().equals("unidadeEnsino")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorUnidadeEnsino(getValorConsultaFuncionarioOperacoesExternas(), "", 0, null, true, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaResponsavelOperacoesExternas(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFuncionario(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarResponsavelOperacoesExternas() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("responsavelOperacoesExternas");
		getConfiguracaoGeralSistemaVO().setUsuarioResponsavelOperacoesExternas(obj.getPessoa());
	}
	
	public void limparDadosResponsavelOperacoesExternas() {
		getConfiguracaoGeralSistemaVO().setUsuarioResponsavelOperacoesExternas(null);
		setValorConsultaFuncionarioOperacoesExternas("");
		getListaConsultaFuncionario().clear();
	}
	
	public void limparDadosUpLoadArquivo() {
		getConfiguracaoGeralSistemaVO().setCertificadoParaDocumento(new ArquivoVO());
	}
	
	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getConfiguracaoGeralSistemaVO().getCertificadoParaDocumento(), getConfiguracaoGeralPadraoSistema(),PastaBaseArquivoEnum.CERTIFICADO_DOCUMENTOS_TMP, getUsuarioLogado());
			getConfiguracaoGeralSistemaVO().getCertificadoParaDocumento().setOrigem(OrigemArquivo.CERTIFICADO_DIGITAL_INSTITUICAO.getValor());
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	}

	public void montarListaSelectItemDosServidoresDeArquivoOnline() {
		setListaSelectItemServidorArquivoOnline(ServidorArquivoOnlineEnum.listaSelectItemServidorArquivoOnlineEnum(true));
	}
	
	public List<SelectItem> getListaSelectItemServidorArquivoOnline() {
		if (listaSelectItemServidorArquivoOnline == null)
			listaSelectItemServidorArquivoOnline = new ArrayList<>();
		return listaSelectItemServidorArquivoOnline;
	}

	public void setListaSelectItemServidorArquivoOnline(List<SelectItem> listaSelectItemServidorArquivoOnline) {
		this.listaSelectItemServidorArquivoOnline = listaSelectItemServidorArquivoOnline;
	}
	public String navegarAba(String aba) {
		setAbaNavegar(aba);
		if(aba.equals("redeServidores")) {
			return  Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoGeralSistemaServidorForm");
		}
		return  Uteis.getCaminhoRedirecionamentoNavegacao("configuracaoGeralSistemaForm");		
	}
	
	public String getAbaNavegar() {
		if (abaNavegar == null) {
			abaNavegar = "tabBasico";
		}
		return abaNavegar;
	}

	public void setAbaNavegar(String abaNavegar) {
		this.abaNavegar = abaNavegar;
	}

	public void excluirCertificado() {
		try {
			getFacadeFactory().getConfiguracaoGeralSistemaFacade().excluirCertificado(getConfiguracaoGeralSistemaVO(), getUsuarioLogado());
			getAplicacaoControle().removerConfiguracaoGeralSistemaEmNivelAplicacao(configuracaoGeralSistemaVO);
			setMensagemID("msg_dados_excluidos", Uteis.ERRO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
	
		}
	}

	public List<SelectItem> getListaSelectItemFormatoSenhaLdap() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("UTF-16LE", "UTF-16LE"));
		itens.add(new SelectItem("SHA256", "SHA 256"));
		itens.add(new SelectItem("SHA", "SHA1"));
		itens.add(new SelectItem("MSCHAPV2", "MSCHAPV2"));
		return itens;
	}
	public List<SelectItem> getListaSelectItemPerfilAcessoAlunoNaoAssinouContratoMatricula() {
		return listaSelectItemPerfilAcessoAlunoNaoAssinouContratoMatricula;
	}

	public void setListaSelectItemPerfilAcessoAlunoNaoAssinouContratoMatricula(
			List<SelectItem> listaSelectItemPerfilAcessoAlunoNaoAssinouContratoMatricula) {
		this.listaSelectItemPerfilAcessoAlunoNaoAssinouContratoMatricula = listaSelectItemPerfilAcessoAlunoNaoAssinouContratoMatricula;
	}
	
	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>PerfilPadraoCandidato</code>.
	 */
	public void montarListaSelectItemPerfilAcessoAlunoNaoAssinouContratoMatricula(String prm) {
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
			setListaSelectItemPerfilAcessoAlunoNaoAssinouContratoMatricula(objs);
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
	public void montarListaSelectItemPerfilAcessoAlunoNaoAssinouContratoMatricula() {
		try {
			montarListaSelectItemPerfilAcessoAlunoNaoAssinouContratoMatricula("");
		} catch (Exception e) {
		}
	}
	
	private List<SelectItem> listaSelectItemConfiguracaoAparenciaSistema;

	public List<SelectItem> getListaSelectItemConfiguracaoAparenciaSistema() {
		if(listaSelectItemConfiguracaoAparenciaSistema == null) {
			listaSelectItemConfiguracaoAparenciaSistema =  new ArrayList<SelectItem>(0);
			montarListaSelectItemConfiguracaoAparenciaSistema();
		}
		return listaSelectItemConfiguracaoAparenciaSistema;
	}

	public void setListaSelectItemConfiguracaoAparenciaSistema(
			List<SelectItem> listaSelectItemConfiguracaoAparenciaSistema) {
		this.listaSelectItemConfiguracaoAparenciaSistema = listaSelectItemConfiguracaoAparenciaSistema;
	}

	public void montarListaSelectItemConfiguracaoAparenciaSistema() {
		try {
			setListaSelectItemConfiguracaoAparenciaSistema(UtilSelectItem.getListaSelectItem(getFacadeFactory().getConfiguracaoAparenciaSistemaFacade().consultar("", "%%", NivelMontarDados.COMBOBOX, false, getUsuarioLogado()), "codigo", "nome"));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}


	public ConfiguracaoLdapVO getConfiguracaoLdapVO() {
		if (configuracaoLdapVO == null) {
			configuracaoLdapVO = new ConfiguracaoLdapVO();
		}
		return configuracaoLdapVO;
	}

	public void setConfiguracaoLdapVO(ConfiguracaoLdapVO configuracaoLdapVO) {
		this.configuracaoLdapVO = configuracaoLdapVO;
	}

	
	
	public void adicionarConfiguracaoLdap() {
		try {
			getFacadeFactory().getConfiguracaoLdapInterfaceFacade().validarDados(getConfiguracaoLdapVO());			
			if (getConfiguracaoGeralSistemaVO().getConfiguracaoLdapVOs().stream().filter(objExistente -> objExistente.getDominio().equals(getConfiguracaoLdapVO().getDominio())).count() >= 1l) {
				throw new ConsistirException("Já existe uma INTEGRAÇÃO LDAP para o DOMÍNIO "+getConfiguracaoLdapVO().getDominio()+", informe outro domínio.");
			}
			getConfiguracaoGeralSistemaVO().getConfiguracaoLdapVOs().add(getConfiguracaoLdapVO());			
			setConfiguracaoLdapVO(new ConfiguracaoLdapVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void editarConfiguracaoLdap() {
		try {
			ConfiguracaoLdapVO configuracaoLDAPItem = (ConfiguracaoLdapVO) context().getExternalContext().getRequestMap().get("configuracaoLDAPItem");
			configuracaoLDAPItem.setItemEmEdicao(true);
			configuracaoLDAPItem.setNovoObj(false);
			setConfiguracaoLdapVO((ConfiguracaoLdapVO) BeanUtils.cloneBean(configuracaoLDAPItem));
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", UteisJSF.internacionalizar("prt_RegistroEntrada_erro"));
		}
	}

	public String removerConfiguracaoLdap() {
		try {
			ConfiguracaoLdapVO configuracaoLDAPItem = (ConfiguracaoLdapVO) context().getExternalContext().getRequestMap().get("configuracaoLDAPItem");
			getConfiguracaoGeralSistemaVO().getConfiguracaoLdapVOs().remove(configuracaoLDAPItem);
			setMensagemID("msg_dados_removidos", Uteis.ALERTA);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public void cancelarEdicaoObjetoConfiguracaoLdap() {
		setConfiguracaoLdapVO(new ConfiguracaoLdapVO());
	}
	
	public void testarConexaoLdap(ConfiguracaoLdapVO configuracaoLdapVO) {
		try {
			getFacadeFactory().getConfiguracaoLdapInterfaceFacade().validarDados(configuracaoLdapVO);
			if(!getFacadeFactory().getLdapFacade().testarConexaoLdap(configuracaoLdapVO, getUsuarioLogadoClone())) {
				throw new Exception("Falha ao conectar a " + configuracaoLdapVO.getHostnameLdap());
			}
			setMensagemID("msg_dados_conexao_validados", Uteis.SUCESSO, true);
		} catch (Exception e) {
			e.printStackTrace();
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	private UsuarioVO usuarioTesteVO;
	private ConfiguracaoLdapVO configuracaoLdapTesteVO;
		
	
	public UsuarioVO getUsuarioTesteVO() {
		if(usuarioTesteVO == null) {
			usuarioTesteVO =  new UsuarioVO();
		}
		return usuarioTesteVO;
	}

	public void setUsuarioTesteVO(UsuarioVO usuarioTesteVO) {
		this.usuarioTesteVO = usuarioTesteVO;
	}

	public ConfiguracaoLdapVO getConfiguracaoLdapTesteVO() {
		if(configuracaoLdapTesteVO == null) {
			configuracaoLdapTesteVO =  new ConfiguracaoLdapVO();
		}
		return configuracaoLdapTesteVO;
	}

	public void setConfiguracaoLdapTesteVO(ConfiguracaoLdapVO configuracaoLdapTesteVO) {
		this.configuracaoLdapTesteVO = configuracaoLdapTesteVO;
	}
	
	public void iniciarTesteCriacaoConta(ConfiguracaoLdapVO configuracaoLdapVO) {
		limparMensagem();
		setConfiguracaoLdapTesteVO(configuracaoLdapVO);
		getUsuarioTesteVO().setSenha("123456");
		criptografarSenha();		
		if(getUsuarioTesteVO().getPessoa().getNome().isEmpty()) {			
			getUsuarioTesteVO().getPessoa().setNome("Usuario Teste");
			getUsuarioTesteVO().getPessoa().setCPF("99.999.999-99");
			getUsuarioTesteVO().getPessoa().setEmail("rodrigo@otimize-ti.com.br");					
			getUsuarioTesteVO().setUsername("T0000000001");
		}
	}

	public void realizarCriacaoContaTeste() {
		try {
			getFacadeFactory().getLdapFacade().executarCriacaoContaTeste(getConfiguracaoLdapTesteVO(), getUsuarioTesteVO(), getUsuarioTesteVO().getSenha());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO, true);
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public void realizarExclusaoContaTeste() {
		try {
			getFacadeFactory().getLdapFacade().executarExclusaoContaTeste(getConfiguracaoLdapTesteVO(), getUsuarioTesteVO(), getUsuarioTesteVO().getSenha());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO, true);
		} catch (Exception e) {			
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	public void criptografarSenha() {
		try {			
			getUsuarioTesteVO().setSenhaCriptografada(Uteis.encriptar(getUsuarioTesteVO().getSenha()));
			getUsuarioTesteVO().setSenhaSHA(Uteis.encriptarSHA(getUsuarioTesteVO().getSenha()));
			java.util.Base64.Encoder codificador = java.util.Base64.getEncoder();
			getUsuarioTesteVO().setSenhaSHABase64("{SHA}" + codificador.encodeToString(Hex.decode(getUsuarioTesteVO().getSenhaSHA().getBytes())));

			getUsuarioTesteVO().setSenhaMSCHAPV2(Uteis.encriptarMSCHAPV2(getUsuarioTesteVO().getSenha()));
		} catch (UnsupportedEncodingException e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getListaSelectItemQuestionarioPlanoEnsino() {
		return listaSelectItemQuestionarioPlanoEnsino;
	}

	public void setListaSelectItemQuestionarioPlanoEnsino(List<SelectItem> listaSelectItemQuestionarioPlanoEnsino) {
		this.listaSelectItemQuestionarioPlanoEnsino = listaSelectItemQuestionarioPlanoEnsino;
	}
	
	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>PerfilPadraoAluno</code>.
	 */
	public void montarListaSelectItemQuestionarioPlanoEnsino() {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarQuestionarioPlanoEnsinoPorDescricao();
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				QuestionarioVO obj = (QuestionarioVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao().toString()));
			}
			setListaSelectItemQuestionarioPlanoEnsino(objs);
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
	public List consultarQuestionarioPlanoEnsinoPorDescricao() {
		try {
			List lista = getFacadeFactory().getQuestionarioFacade().consultarPorEscopoSituacao(TipoEscopoQuestionarioPerguntaEnum.PLANO_ENSINO, "AT", false, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			return lista;
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return new ArrayList(0);
		}
	}

	public Boolean getApesentarPanelCancelamentoMatriculaOnline() {
		if(apesentarPanelCancelamentoMatriculaOnline == null ) {
			apesentarPanelCancelamentoMatriculaOnline = Boolean.FALSE;
		}
		return apesentarPanelCancelamentoMatriculaOnline;
	}

	public void setApesentarPanelCancelamentoMatriculaOnline(Boolean apesentarPanelCancelamentoMatriculaOnline) {
		this.apesentarPanelCancelamentoMatriculaOnline = apesentarPanelCancelamentoMatriculaOnline;
	}
	
	public List<SelectItem> getListaSelectItemFornecedorSMS() {
		if (listaSelectItemFornecedorSMS == null) {
			listaSelectItemFornecedorSMS = FornecedorSmsEnum.getListaSelectItemItemFornecedorSMS();
		}
		return listaSelectItemFornecedorSMS;
	}

	public void setListaSelectItemFornecedorSMS(List<SelectItem> listaSelectItemFornecedorSMS) {
		this.listaSelectItemFornecedorSMS = listaSelectItemFornecedorSMS;
	}	
		
	public List<SelectItem> getListaSelectItemMotivoPadraoCancelamentoOutraMatricula() {
		if (listaSelectItemMotivoPadraoCancelamentoOutraMatricula == null) {
			listaSelectItemMotivoPadraoCancelamentoOutraMatricula = new ArrayList<SelectItem>(0);
			
		}
		return listaSelectItemMotivoPadraoCancelamentoOutraMatricula;
	}

	public void setListaSelectItemMotivoPadraoCancelamentoOutraMatricula(List<SelectItem> listaSelectItemMotivoPadraoCancelamentoOutraMatricula) {
		this.listaSelectItemMotivoPadraoCancelamentoOutraMatricula = listaSelectItemMotivoPadraoCancelamentoOutraMatricula;
	}

	public void montarListaSelectItemMotivoPadraoCancelamentoOutraMatricula() {
		try {
			List<MotivoCancelamentoTrancamentoVO> motivoCancelamentoTrancamentoVOs = getFacadeFactory().getMotivoCancelamentoTrancamentoFacade().consultarPorNomeAtivo("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			getListaSelectItemMotivoPadraoCancelamentoOutraMatricula().clear();
			getListaSelectItemMotivoPadraoCancelamentoOutraMatricula().add(new SelectItem(0, ""));
			for (MotivoCancelamentoTrancamentoVO motivoCancelamentoTrancamentoVO : motivoCancelamentoTrancamentoVOs) {
				getListaSelectItemMotivoPadraoCancelamentoOutraMatricula().add(new SelectItem(motivoCancelamentoTrancamentoVO.getCodigo(), motivoCancelamentoTrancamentoVO.getNome()));
			}
		} catch (Exception e) {

		}
	}
	
	public Boolean getApresentarDocumentoPortalTransparenciaComPendenciaAssinatura() {
		if (apresentarDocumentoPortalTransparenciaComPendenciaAssinatura == null) {
			apresentarDocumentoPortalTransparenciaComPendenciaAssinatura = false;
		}
		return apresentarDocumentoPortalTransparenciaComPendenciaAssinatura;
	}

	public void setApresentarDocumentoPortalTransparenciaComPendenciaAssinatura(
			Boolean apresentarDocumentoPortalTransparenciaComPendenciaAssinatura) {
		this.apresentarDocumentoPortalTransparenciaComPendenciaAssinatura = apresentarDocumentoPortalTransparenciaComPendenciaAssinatura;
	}

	public void renderizarCamposAtualizacaoCadastral() {
		getFacadeFactory().getConfiguracaoAtualizacaoCadastralFacade().aplicarRegraRenderizacaoCamposAtualizacaoCadastral(getConfiguracaoGeralSistemaVO().getConfiguracaoAtualizacaoCadastralVO());
	}
	 
	

	    public Boolean testarConexaoSFTP() {
	        String host = getConfiguracaoGeralSistemaVO().getHostIntegracaoSistemaSymplicty();
	        int port = getConfiguracaoGeralSistemaVO().getPortIntegracaoSistemaSymplicty();
	        String usuario = getConfiguracaoGeralSistemaVO().getUserIntegracaoSistemaSymplicty();
	        String senha = getConfiguracaoGeralSistemaVO().getPassIntegracaoSistemaSymplicty();

	        Session session = null;
	        ChannelSftp sftpChannel = null;

	        try {
	            JSch jsch = new JSch();
	            session = jsch.getSession(usuario, host, port);
	            session.setPassword(senha);
	            session.setConfig("StrictHostKeyChecking", "no");
	            session.connect(5000); 

	            Channel channel = session.openChannel("sftp");
	            channel.connect();
	            sftpChannel = (ChannelSftp) channel;


	            return true;
	        } catch (JSchException e) {
	            return false;
	        } finally {
	            if (sftpChannel != null) sftpChannel.disconnect();
	            if (session != null) session.disconnect();
	        }
	    }

	
	public void testarConexaoSymplicty() {
		Boolean sucesso = testarConexaoSFTP();
        if (sucesso) {
        	setMensagemID("Conexão bem sucedida.", Uteis.SUCESSO, true);        
        }else {
        	setMensagemID("Falha na conexão.", Uteis.ALERTA, true);
        }
	}
	
	

    public void executarJobAgora() {
        JobSymplicty job = new JobSymplicty();
        job.executarJobSymplicty();
    }

	public Boolean getApresentarParaUsuarioOtimize() {
		if (apresentarParaUsuarioOtimize == null) {
			apresentarParaUsuarioOtimize = Uteis.isAtributoPreenchido(getUsuarioLogado()) && getUsuarioLogado().getCodigo().equals(1);
		}
		return apresentarParaUsuarioOtimize;
	}
}
