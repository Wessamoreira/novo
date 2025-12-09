package controle.biblioteca;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas bibliotecaForm.jsp
 * bibliotecaCons.jsp) com as funcionalidades da classe <code>Biblioteca</code>. Implemtação da camada controle (Backing
 * Bean).
 * 
 * @see SuperControle
 * @see Biblioteca
 * @see BibliotecaVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.biblioteca.BibliotecaVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaNivelEducacionalVO;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.UnidadeEnsinoBibliotecaVO;
import negocio.comuns.biblioteca.enumeradores.TipoImpressaoComprovanteBibliotecaEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;

@Controller("BibliotecaControle")
@Scope("viewScope")
@Lazy
public class BibliotecaControle extends SuperControle implements Serializable {

	private BibliotecaVO bibliotecaVO;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	protected List listaSelectItemCidade;
	private String campoConsultarConfiguracaoBiblioteca;
	private String valorConsultarConfiguracaoBiblioteca;
	private List listaConsultarConfiguracaoBiblioteca;
	private String campoConsultarBibliotecaria;
	private String valorConsultarBibliotecaria;
	private List listaConsultarBibliotecaria;
	protected List listaSelectItemUnidadeEnsino;
	private String campoConsultaCidade;
	private String valorConsultaCidade;
	private List<CidadeVO> listaConsultaCidade;
	private ConfiguracaoBibliotecaNivelEducacionalVO configuracaoBibliotecaNivelEducacionalVO;
	protected List listaSelectItemTurno;
	private List<SelectItem> listaSelectItemTipoImpressaoComprovante;
	private DataModelo centroResultadoDataModelo;

	public BibliotecaControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>Biblioteca</code> para edição pelo usuário da aplicação.
	 */
	public String novo() {
		removerObjetoMemoria(this);
		setBibliotecaVO(new BibliotecaVO());
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("bibliotecaForm.xhtml");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Biblioteca</code> para alteração. O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() {
		BibliotecaVO obj = (BibliotecaVO) context().getExternalContext().getRequestMap().get("bibliotecaItens");

		obj.setNovoObj(Boolean.FALSE);
		setBibliotecaVO(obj);
		inicializarListasSelectItemTodosComboBox();
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("bibliotecaForm.xhtml");
	}

	/**
	 * Método responsável inicializar objetos relacionados a classe <code>BibliotecaVO</code>. Esta inicialização é necessária por exigência da tecnologia JSF, que não trabalha com valores nulos para estes atributos.
	 */
	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Biblioteca</code>. Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public String gravar() {
		try {
			if (bibliotecaVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getBibliotecaFacade().incluir(bibliotecaVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getBibliotecaFacade().alterar(bibliotecaVO, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("bibliotecaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("bibliotecaForm.xhtml");
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP BibliotecaCons.jsp. Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
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
				objs = getFacadeFactory().getBibliotecaFacade().consultarPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getBibliotecaFacade().consultarPorNome(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCidade")) {
				objs = getFacadeFactory().getBibliotecaFacade().consultarPorNomeCidade(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("codigoConfiguracaoBiblioteca")) {
				Uteis.validarSomenteNumeroString(getControleConsulta().getValorConsulta());
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				objs = getFacadeFactory().getBibliotecaFacade().consultarPorCodigoConfiguracaoBiblioteca(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
				objs = getFacadeFactory().getBibliotecaFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("bibliotecaCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("bibliotecaCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>BibliotecaVO</code> Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getBibliotecaFacade().excluir(bibliotecaVO, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("bibliotecaForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("bibliotecaForm.xhtml");
		}
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>UnidadeEnsino</code>.
	 */
	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			Uteis.liberarListaMemoria(resultadoConsulta);
			setListaSelectItemUnidadeEnsino(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public void montarListaSelectItemTurno(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarUnidadeTurnoPorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				TurnoVO obj = (TurnoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			Uteis.liberarListaMemoria(resultadoConsulta);
			setListaSelectItemTurno(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	public List consultarUnidadeTurnoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getTurnoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	public void montarListaSelectItemTurno() {
		try {
			montarListaSelectItemTurno("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	public void carregarEnderecoPessoa() {
		try {
			getFacadeFactory().getEnderecoFacade().carregarEndereco(getBibliotecaVO(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio dos parametros informados no richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos parâmentros informados no richModal montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarBibliotecaria() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultarBibliotecaria().equals("codigo")) {
				if (getValorConsultarBibliotecaria().equals("")) {
					setValorConsultarBibliotecaria("0");
				}
				int valorInt = Integer.parseInt(getValorConsultarBibliotecaria());
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultarBibliotecaria().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultarBibliotecaria(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setListaConsultarBibliotecaria(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarBibliotecaria(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarAdicionarConfiguracaoBibliotecaNivelEducacional() {
		try {
			getFacadeFactory().getConfiguracaoBibliotecaNivelEducacionalFacade().executarAdicionarConfiguracaoBibliotecaNivelEducacional(getConfiguracaoBibliotecaNivelEducacionalVO(), getBibliotecaVO().getConfiguracaoBibliotecaNivelEducacionalVOs(), getUsuarioLogado());
			setConfiguracaoBibliotecaNivelEducacionalVO(new ConfiguracaoBibliotecaNivelEducacionalVO());
		} catch (Exception e) {
			setConfiguracaoBibliotecaNivelEducacionalVO(new ConfiguracaoBibliotecaNivelEducacionalVO());
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void executarRemoverConfiguracaoBibliotecaNivelEducacional() {
		try {
			ConfiguracaoBibliotecaNivelEducacionalVO obj = (ConfiguracaoBibliotecaNivelEducacionalVO) context().getExternalContext().getRequestMap().get("configuracaoItens");
			getFacadeFactory().getConfiguracaoBibliotecaNivelEducacionalFacade().executarRemoverConfiguracaoBibliotecaNivelEducacional(obj, getBibliotecaVO().getConfiguracaoBibliotecaNivelEducacionalVOs(), getUsuarioLogado());
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarCidade() {
		CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
		getBibliotecaVO().setCidade(obj);
		getListaConsultaCidade().clear();
		this.setValorConsultaCidade("");
		this.setCampoConsultaCidade("");
	}

	public void selecionarBibliotecaria() throws Exception {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		if (getMensagemDetalhada().equals("")) {
			this.getBibliotecaVO().setBibliotecaria(obj);
		}
		Uteis.liberarListaMemoria(this.getListaConsultarBibliotecaria());
		this.setValorConsultarBibliotecaria(null);
		this.setCampoConsultarBibliotecaria(null);
	}

	public void limparCampoBibliotecaria() {
		this.getBibliotecaVO().setBibliotecaria(new FuncionarioVO());
	}

	public void consultarCidade() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaCidade().equals("codigo")) {
				if (getValorConsultaCidade().equals("")) {
					setValorConsultaCidade("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaCidade());
				objs = getFacadeFactory().getCidadeFacade().consultaRapidaPorCodigo(new Integer(valorInt), false, getUsuarioLogado());
			}
			if (getCampoConsultaCidade().equals("nome")) {
				objs = getFacadeFactory().getCidadeFacade().consultaRapidaPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
			}

			setListaConsultaCidade(objs);
			setMensagemID("msg_dados_consultados");

		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

	public void adicionarTodasUnidadesEnsinoBiblioteca() {
		UnidadeEnsinoBibliotecaVO unidadeEnsinoBibliotecaVO = new UnidadeEnsinoBibliotecaVO();
		List<UnidadeEnsinoVO> unidadeEnsinoVOs = new ArrayList<UnidadeEnsinoVO>(0);
		try {
			unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", super.getUnidadeEnsinoLogado().getCodigo(), false,
					Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			getBibliotecaVO().getUnidadeEnsinoBibliotecaVOs().clear();
			for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
				unidadeEnsinoBibliotecaVO.setUnidadeEnsino(unidadeEnsinoVO);
				getBibliotecaVO().getUnidadeEnsinoBibliotecaVOs().add(unidadeEnsinoBibliotecaVO);
				unidadeEnsinoBibliotecaVO = new UnidadeEnsinoBibliotecaVO();
			}

		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			unidadeEnsinoVOs = null;
			setUnidadeEnsinoVO(null);
		}
	}

	public void adicionarUnidadeEnsinoBiblioteca() {
		UnidadeEnsinoBibliotecaVO unidadeEnsinoBibliotecaVO = new UnidadeEnsinoBibliotecaVO();
		try {
			setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false,
					Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			unidadeEnsinoBibliotecaVO.setUnidadeEnsino(getUnidadeEnsinoVO());

			for (UnidadeEnsinoBibliotecaVO unidEnsBibliotecaVO : getBibliotecaVO().getUnidadeEnsinoBibliotecaVOs()) {
				if (unidadeEnsinoBibliotecaVO.getUnidadeEnsino().getCodigo().equals(unidEnsBibliotecaVO.getUnidadeEnsino().getCodigo())) {
					throw new Exception("Essa unidade de ensino já se encontra na lista.");
				}
			}

			getBibliotecaVO().getUnidadeEnsinoBibliotecaVOs().add(unidadeEnsinoBibliotecaVO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			unidadeEnsinoBibliotecaVO = null;
			setUnidadeEnsinoVO(null);
		}
	}

	public void removerUnidadeEnsinoBiblioteca() {
		try {
			UnidadeEnsinoBibliotecaVO unidadeEnsinoBibliotecaVO = (UnidadeEnsinoBibliotecaVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoBiblioteca");
			for (UnidadeEnsinoBibliotecaVO unidEnsbibliotecaVO : getBibliotecaVO().getUnidadeEnsinoBibliotecaVOs()) {
				if (unidadeEnsinoBibliotecaVO.getUnidadeEnsino().getCodigo().equals(unidEnsbibliotecaVO.getUnidadeEnsino().getCodigo())) {
					getBibliotecaVO().getUnidadeEnsinoBibliotecaVOs().remove(unidadeEnsinoBibliotecaVO);
					break;
				}
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
		}
	}

	public List getTipoConsultaCidade() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
	 */
	public List getTipoConsultarComboBibliotecaria() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Método responsável por processar a consulta na entidade <code>ConfiguracaoBiblioteca</code> por meio dos parametros informados no richmodal. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pelos parâmentros informados no richModal montando automaticamente o resultado da consulta para apresentação.
	 */
	public void consultarConfiguracaoBiblioteca() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultarConfiguracaoBiblioteca().equals("codigo")) {
				if (getValorConsultarConfiguracaoBiblioteca().equals("")) {
					setValorConsultarConfiguracaoBiblioteca("0");
				}
				int valorInt = Integer.parseInt(getValorConsultarConfiguracaoBiblioteca());
				objs = getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			if (getCampoConsultarConfiguracaoBiblioteca().equals("nome")) {
				objs = getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarPorNome(getValorConsultarConfiguracaoBiblioteca(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
			}
			setListaConsultarConfiguracaoBiblioteca(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarConfiguracaoBiblioteca(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarConfiguracaoBiblioteca() throws Exception {
		ConfiguracaoBibliotecaVO obj = (ConfiguracaoBibliotecaVO) context().getExternalContext().getRequestMap().get("configuracaoBibliotecaItens");
		if (getMensagemDetalhada().equals("")) {
			this.getBibliotecaVO().setConfiguracaoBiblioteca(obj);
		}
		Uteis.liberarListaMemoria(this.getListaConsultarConfiguracaoBiblioteca());
		this.setValorConsultarConfiguracaoBiblioteca(null);
		this.setCampoConsultarConfiguracaoBiblioteca(null);
	}

	public void limparCampoConfiguracaoBiblioteca() {
		this.getBibliotecaVO().setConfiguracaoBiblioteca(new ConfiguracaoBibliotecaVO());
	}

	/**
	 * Rotina responsável por preencher a combo de consulta dos RichModal da telas.
	 */
	public List getTipoConsultarComboConfiguracaoBiblioteca() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo <code>Cidade</code>.
	 */
	public void montarListaSelectItemCidade(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = consultarCidadePorNome(prm);
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CidadeVO obj = (CidadeVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
			}
			Uteis.liberarListaMemoria(resultadoConsulta);
			setListaSelectItemCidade(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>Cidade</code>. Buscando todos os objetos correspondentes a entidade <code>Cidade</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemCidade() {
		try {
			montarListaSelectItemCidade("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List consultarCidadePorNome(String nomePrm) throws Exception {
		List lista = getFacadeFactory().getCidadeFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
		return lista;
	}

	/**
	 * Método responsável por inicializar a lista de valores ( <code>SelectItem</code>) para todos os ComboBox's.
	 */
	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemUnidadeEnsino();
		montarListaSelectItemTurno();
	}

	/**
	 * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
	 */
	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("CEP")) {
			return "return mascara(this.form, 'form:valorConsulta', '99.999-999', event);";
		}
		if (getControleConsulta().getCampoConsulta().equals("telefone1")) {
			return "return mascara(this.form, 'form:valorConsulta', '(99)9999-9999', event);";
		}
		if (getControleConsulta().getCampoConsulta().equals("telefone2")) {
			return "return mascara(this.form, 'form:valorConsulta', '(99)9999-9999', event);";
		}
		if (getControleConsulta().getCampoConsulta().equals("telefone3")) {
			return "return mascara(this.form, 'form:valorConsulta', '(99)9999-9999', event);";
		}
		return "";
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("nomeCidade", "Cidade"));
		itens.add(new SelectItem("nomePessoa", "Bibliotecaria"));
		itens.add(new SelectItem("codigoConfiguracaoBiblioteca", "Configuração Biblioteca"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("bibliotecaCons.xhtml");
	}

	/**
	 * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
	 */
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		bibliotecaVO = null;
		Uteis.liberarListaMemoria(listaSelectItemCidade);
		Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
		Uteis.liberarListaMemoria(listaSelectItemTurno);
	}

	public void inicializarDadoConsultaCentroResultado() {
		try {
			setCentroResultadoDataModelo(new DataModelo());
			getCentroResultadoDataModelo().setCampoConsulta(CentroResultadoVO.enumCampoConsultaCentroResultado.DESCRICAO_CENTRO_RESULTADO.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void selecionarCentroResultado() {
		try {
			CentroResultadoVO obj = (CentroResultadoVO) context().getExternalContext().getRequestMap().get("centroResultadoItens");
			getBibliotecaVO().setCentroResultadoVO(obj);
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

	public void limparCentroResultado() {
		try {
			this.getBibliotecaVO().setCentroResultadoVO(new CentroResultadoVO());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public DataModelo getCentroResultadoDataModelo() {
		centroResultadoDataModelo = Optional.ofNullable(centroResultadoDataModelo).orElse(new DataModelo());
		return centroResultadoDataModelo;
	}

	public void setCentroResultadoDataModelo(DataModelo centroResultadoDataModelo) {
		this.centroResultadoDataModelo = centroResultadoDataModelo;
	}

	public List getListaSelectItemUnidadeEnsino() {
		if (listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList(0);
		}
		return (listaSelectItemUnidadeEnsino);
	}

	public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public String getCampoConsultarBibliotecaria() {
		return campoConsultarBibliotecaria;
	}

	public void setCampoConsultarBibliotecaria(String campoConsultarBibliotecaria) {
		this.campoConsultarBibliotecaria = campoConsultarBibliotecaria;
	}

	public String getValorConsultarBibliotecaria() {
		return valorConsultarBibliotecaria;
	}

	public void setValorConsultarBibliotecaria(String valorConsultarBibliotecaria) {
		this.valorConsultarBibliotecaria = valorConsultarBibliotecaria;
	}

	public List getListaConsultarBibliotecaria() {
		return listaConsultarBibliotecaria;
	}

	public void setListaConsultarBibliotecaria(List listaConsultarBibliotecaria) {
		this.listaConsultarBibliotecaria = listaConsultarBibliotecaria;
	}

	public String getCampoConsultarConfiguracaoBiblioteca() {
		return campoConsultarConfiguracaoBiblioteca;
	}

	public void setCampoConsultarConfiguracaoBiblioteca(String campoConsultarConfiguracaoBiblioteca) {
		this.campoConsultarConfiguracaoBiblioteca = campoConsultarConfiguracaoBiblioteca;
	}

	public String getValorConsultarConfiguracaoBiblioteca() {
		return valorConsultarConfiguracaoBiblioteca;
	}

	public void setValorConsultarConfiguracaoBiblioteca(String valorConsultarConfiguracaoBiblioteca) {
		this.valorConsultarConfiguracaoBiblioteca = valorConsultarConfiguracaoBiblioteca;
	}

	public List getListaConsultarConfiguracaoBiblioteca() {
		return listaConsultarConfiguracaoBiblioteca;
	}

	public void setListaConsultarConfiguracaoBiblioteca(List listaConsultarConfiguracaoBiblioteca) {
		this.listaConsultarConfiguracaoBiblioteca = listaConsultarConfiguracaoBiblioteca;
	}

	public List getListaSelectItemCidade() {
		if (listaSelectItemCidade == null) {
			listaSelectItemCidade = new ArrayList(0);
		}
		return (listaSelectItemCidade);
	}

	public void setListaSelectItemCidade(List listaSelectItemCidade) {
		this.listaSelectItemCidade = listaSelectItemCidade;
	}

	public BibliotecaVO getBibliotecaVO() {
		if (bibliotecaVO == null) {
			bibliotecaVO = new BibliotecaVO();
		}
		return bibliotecaVO;
	}

	public void setBibliotecaVO(BibliotecaVO bibliotecaVO) {
		this.bibliotecaVO = bibliotecaVO;
	}

	public String getValorConsultaCidade() {
		return valorConsultaCidade;
	}

	public void setValorConsultaCidade(String valorConsultaCidade) {
		this.valorConsultaCidade = valorConsultaCidade;
	}

	public String getCampoConsultaCidade() {
		return campoConsultaCidade;
	}

	public void setCampoConsultaCidade(String campoConsultaCidade) {
		this.campoConsultaCidade = campoConsultaCidade;
	}

	public List<CidadeVO> getListaConsultaCidade() {
		if (listaConsultaCidade == null) {
			listaConsultaCidade = new ArrayList<CidadeVO>(0);
		}
		return listaConsultaCidade;
	}

	public void setListaConsultaCidade(List<CidadeVO> listaConsultaCidade) {
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

	public List<SelectItem> getListaSelectItemConfiguracaoBiblioteca() {
		try {
			List<ConfiguracaoBibliotecaVO> configuracaoBibliotecaVOs = getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarPorNome("%", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			return UtilSelectItem.getListaSelectItem(configuracaoBibliotecaVOs, "codigo", "nome");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new ArrayList<SelectItem>(0);
	}

	public List<SelectItem> getListaSelectItemNivelEducacional() {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoNivelEducacional.class, true);
	}

	public ConfiguracaoBibliotecaNivelEducacionalVO getConfiguracaoBibliotecaNivelEducacionalVO() {
		if (configuracaoBibliotecaNivelEducacionalVO == null) {
			configuracaoBibliotecaNivelEducacionalVO = new ConfiguracaoBibliotecaNivelEducacionalVO();
		}
		return configuracaoBibliotecaNivelEducacionalVO;
	}

	public void setConfiguracaoBibliotecaNivelEducacionalVO(ConfiguracaoBibliotecaNivelEducacionalVO configuracaoBibliotecaNivelEducacionalVO) {
		this.configuracaoBibliotecaNivelEducacionalVO = configuracaoBibliotecaNivelEducacionalVO;
	}

	public List getListaSelectItemTurno() {
		if (listaSelectItemTurno == null) {
			listaSelectItemTurno = new ArrayList(0);
		}
		return listaSelectItemTurno;
	}

	public void setListaSelectItemTurno(List listaSelectItemTurno) {
		this.listaSelectItemTurno = listaSelectItemTurno;
	}

	public List<SelectItem> getListaSelectItemTipoImpressaoComprovante() {
		if (listaSelectItemTipoImpressaoComprovante == null) {
			listaSelectItemTipoImpressaoComprovante = UtilSelectItem.getListaSelectItemEnum(TipoImpressaoComprovanteBibliotecaEnum.values(), Obrigatorio.SIM);
		}
		return listaSelectItemTipoImpressaoComprovante;
	}

	public void setListaSelectItemTipoImpressaoComprovante(List<SelectItem> listaSelectItemTipoImpressaoComprovante) {
		this.listaSelectItemTipoImpressaoComprovante = listaSelectItemTipoImpressaoComprovante;
	}

}
