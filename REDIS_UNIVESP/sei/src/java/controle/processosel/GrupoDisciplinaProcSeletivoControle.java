package controle.processosel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.processosel.DisciplinasGrupoDisciplinaProcSeletivoVO;
import negocio.comuns.processosel.DisciplinasProcSeletivoVO;
import negocio.comuns.processosel.GrupoDisciplinaProcSeletivoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoGrupoDisciplinaProcSeletivoEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.facade.jdbc.processosel.ProcSeletivo;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas procSeletivoForm.jsp procSeletivoCons.jsp) com as
 * funcionalidades da classe <code>ProcSeletivo</code>. Implemtação da camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see ProcSeletivo
 * @see ProcSeletivoVO
 */

@Controller("GrupoDisciplinaProcSeletivoControle")
@Scope("viewScope")
public class GrupoDisciplinaProcSeletivoControle extends SuperControle {

	private static final long serialVersionUID = 1L;
	private GrupoDisciplinaProcSeletivoVO grupoDisciplinaProcSeletivoVO;
	private DisciplinasGrupoDisciplinaProcSeletivoVO disciplinasGrupoDisciplinaProcSeletivoVO;
	private List<SelectItem> listaSelectItemDisciplinasProcSeletivo;

	public GrupoDisciplinaProcSeletivoControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar um novo objeto da classe <code>ProcSeletivo</code> para edição pelo usuário da aplicação.
	 */
	@SuppressWarnings("rawtypes")
	public String novo() {
		removerObjetoMemoria(this);
		setGrupoDisciplinaProcSeletivoVO(new GrupoDisciplinaProcSeletivoVO());
		inicializarListasSelectItemTodosComboBox();
		inicializarResponsavelUsuarioLogado();
		setDisciplinasGrupoDisciplinaProcSeletivoVO(new DisciplinasGrupoDisciplinaProcSeletivoVO());
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("grupoDisciplinaProcSeletivoForm.xhtml");
	}

	public void inicializarResponsavelUsuarioLogado() {
		try {
			grupoDisciplinaProcSeletivoVO.setResponsavel(getUsuarioLogadoClone());
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());
		}
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe <code>ProcSeletivo</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		GrupoDisciplinaProcSeletivoVO obj = (GrupoDisciplinaProcSeletivoVO) context().getExternalContext().getRequestMap().get("grupoDisciplinaProcSeletivoItens");
		GrupoDisciplinaProcSeletivoVO grupoDisciplinaProcSeletivoVO = getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		obj.setNovoObj(Boolean.FALSE);
		setGrupoDisciplinaProcSeletivoVO(grupoDisciplinaProcSeletivoVO);
		inicializarListasSelectItemTodosComboBox();
		inicializarResponsavelUsuarioLogado();
		setDisciplinasGrupoDisciplinaProcSeletivoVO(new DisciplinasGrupoDisciplinaProcSeletivoVO());
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("grupoDisciplinaProcSeletivoForm.xhtml");
	}

	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>ProcSeletivo</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
	 */
	public void gravar() {
		try {
			if (grupoDisciplinaProcSeletivoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().incluir(grupoDisciplinaProcSeletivoVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().alterar(grupoDisciplinaProcSeletivoVO, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP ProcSeletivoCons.jsp. Define o tipo de consulta a ser executada, por meio de
	 * ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado, disponibiliza um List com os objetos selecionados na sessao da
	 * pagina.
	 */
	@SuppressWarnings("rawtypes")
	public String consultar() {
		try {
			super.consultar();
			List<GrupoDisciplinaProcSeletivoVO> objs = new ArrayList<GrupoDisciplinaProcSeletivoVO>(0);
			if (getControleConsulta().getCampoConsulta().equals("descricao")) {
				objs = getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().consultarPorDescricao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("grupoDisciplinaProcSeletivoCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("grupoDisciplinaProcSeletivoCons.xhtml");
		}
	}

	/**
	 * Operação responsável por processar a exclusão um objeto da classe <code>ProcSeletivoVO</code> Após a exclusão ela automaticamente aciona a
	 * rotina para uma nova inclusão.
	 */
	public String excluir() {
		try {
			getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().excluir(grupoDisciplinaProcSeletivoVO, getUsuarioLogado());
			novo();
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("grupoDisciplinaProcSeletivoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("grupoDisciplinaProcSeletivoForm.xhtml");
		}
	}

	/*
	 * Método responsável por adicionar um novo objeto da classe <code>ProcSeletivoDisciplinasProcSeletivo</code> para o objeto
	 * <code>procSeletivoVO</code> da classe <code>ProcSeletivo</code>
	 */
	public void adicionarDisciplinasGrupoDisciplinaProcSeletivo() throws Exception {
		try {
			if (!getGrupoDisciplinaProcSeletivoVO().getCodigo().equals(0)) {
				disciplinasGrupoDisciplinaProcSeletivoVO.setGrupoDisciplinaProcSeletivo(getGrupoDisciplinaProcSeletivoVO().getCodigo());
			}
			if (getDisciplinasGrupoDisciplinaProcSeletivoVO().getDisciplinasProcSeletivo().getCodigo().intValue() != 0) {
				Integer campoConsulta = getDisciplinasGrupoDisciplinaProcSeletivoVO().getDisciplinasProcSeletivo().getCodigo();
				DisciplinasProcSeletivoVO disciplinasProcSeletivo = getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorChavePrimaria(campoConsulta, getUsuarioLogado());
				getDisciplinasGrupoDisciplinaProcSeletivoVO().setDisciplinasProcSeletivo(disciplinasProcSeletivo);
			}
			if (getDisciplinasGrupoDisciplinaProcSeletivoVO().getDisciplinasProcSeletivo().getDisciplinaIdioma()) {
				getDisciplinasGrupoDisciplinaProcSeletivoVO().setVariavelNota("LE");
			}
			if (!getDisciplinasGrupoDisciplinaProcSeletivoVO().getVariavelNota().equals("")) {
				Iterator<DisciplinasGrupoDisciplinaProcSeletivoVO> i = getGrupoDisciplinaProcSeletivoVO().getDisciplinasGrupoDisciplinaProcSeletivoVOs().iterator();
				while (i.hasNext()) {
					DisciplinasGrupoDisciplinaProcSeletivoVO disc = (DisciplinasGrupoDisciplinaProcSeletivoVO) i.next();
					if (disc.getVariavelNota().equalsIgnoreCase("LE") && (!disc.getDisciplinasProcSeletivo().getDisciplinaIdioma())) {
						getDisciplinasGrupoDisciplinaProcSeletivoVO().setVariavelNota("");
						throw new Exception("Não é permitido utilizar a mesma variável para disciplinas diferentes, exceto as disciplinas de idioma que utilizam como padrão a variável LE.");
					}
					if (!disc.getDisciplinasProcSeletivo().getCodigo().equals(getDisciplinasGrupoDisciplinaProcSeletivoVO().getDisciplinasProcSeletivo().getCodigo())) {
						if ((disc.getVariavelNota().equalsIgnoreCase("LE") && (!disc.getDisciplinasProcSeletivo().getDisciplinaIdioma())) || (disc.getVariavelNota().equalsIgnoreCase(getDisciplinasGrupoDisciplinaProcSeletivoVO().getVariavelNota()) && !disc.getDisciplinasProcSeletivo().getDisciplinaIdioma())) {
							getDisciplinasGrupoDisciplinaProcSeletivoVO().setVariavelNota("");
							throw new Exception("Não é permitido utilizar a mesma variável para disciplinas diferentes, exceto as disciplinas de idioma que utilizam como padrão a variável LE.");
						}
					}
				}
			}
			getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().adicionarObjDisciplinasGrupoDisciplinaProcSeletivoVOs(getDisciplinasGrupoDisciplinaProcSeletivoVO(), getGrupoDisciplinaProcSeletivoVO().getDisciplinasGrupoDisciplinaProcSeletivoVOs());
			this.setDisciplinasGrupoDisciplinaProcSeletivoVO(new DisciplinasGrupoDisciplinaProcSeletivoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void validarVariavelNota() throws Exception {
		try {
			DisciplinasGrupoDisciplinaProcSeletivoVO obj = (DisciplinasGrupoDisciplinaProcSeletivoVO) context().getExternalContext().getRequestMap().get("disciplinasGrupoDisciplinaProcSeletivoItens");
			if (!obj.getVariavelNota().equals("")) {
				Iterator<DisciplinasGrupoDisciplinaProcSeletivoVO> i = getGrupoDisciplinaProcSeletivoVO().getDisciplinasGrupoDisciplinaProcSeletivoVOs().iterator();
				while (i.hasNext()) {
					DisciplinasGrupoDisciplinaProcSeletivoVO disc = (DisciplinasGrupoDisciplinaProcSeletivoVO) i.next();
					if (disc.getVariavelNota().equalsIgnoreCase("LE") && (!disc.getDisciplinasProcSeletivo().getDisciplinaIdioma())) {
						obj.setVariavelNota("");
						throw new Exception("Não é permitido utilizar a mesma variável para disciplinas diferentes, exceto as disciplinas de idioma que utilizam como padrão a variável LE.");
					}
					if (disc.getDisciplinasProcSeletivo().getCodigo().intValue() != obj.getDisciplinasProcSeletivo().getCodigo().intValue()) {
						if ((disc.getVariavelNota().equalsIgnoreCase("LE") && (!disc.getDisciplinasProcSeletivo().getDisciplinaIdioma())) || (disc.getVariavelNota().equalsIgnoreCase(obj.getVariavelNota()) && !disc.getDisciplinasProcSeletivo().getDisciplinaIdioma())) {
							obj.setVariavelNota("");
							throw new Exception("Não é permitido utilizar a mesma variável para disciplinas diferentes, exceto as disciplinas de idioma que utilizam como padrão a variável LE.");
						}
					}
				}
			}
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/*
	 * Método responsável por disponibilizar dados de um objeto da classe <code>ProcSeletivoDisciplinasProcSeletivo</code> para edição pelo usuário.
	 */
	public void editarDisciplinasGrupoDisciplinaProcSeletivo() throws Exception {
		DisciplinasGrupoDisciplinaProcSeletivoVO obj = (DisciplinasGrupoDisciplinaProcSeletivoVO) context().getExternalContext().getRequestMap().get("disciplinasGrupoDisciplinaProcSeletivoItens");
		setDisciplinasGrupoDisciplinaProcSeletivoVO(obj);
	}

	/*
	 * Método responsável por remover um novo objeto da classe <code>ProcSeletivoDisciplinasProcSeletivo</code> do objeto <code>procSeletivoVO</code>
	 * da classe <code>ProcSeletivo</code>
	 */
	public void removerDisciplinasGrupoDisciplinaProcSeletivo() throws Exception {
		DisciplinasGrupoDisciplinaProcSeletivoVO obj = (DisciplinasGrupoDisciplinaProcSeletivoVO) context().getExternalContext().getRequestMap().get("disciplinasGrupoDisciplinaProcSeletivoItens");
		getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().excluirObjDisciplinasGrupoDisciplinaProcSeletivoVOs(obj.getDisciplinasProcSeletivo().getCodigo(), getGrupoDisciplinaProcSeletivoVO().getDisciplinasGrupoDisciplinaProcSeletivoVOs());
		setMensagemID("msg_dados_excluidos");
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
	 * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>DisciplinasProcSeletivo</code>.
	 */
	public void montarListaSelectItemDisciplinasProcSeletivo(String prm) throws Exception {
		try {
			List<SelectItem> objs = UtilSelectItem.getListaSelectItem(consultarDisciplinasProcSeletivoPorNome(prm), "codigo", "nome", true);
			Ordenacao.ordenarLista(objs, "label");
			setListaSelectItemDisciplinasProcSeletivo(objs);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Método responsável por atualizar o ComboBox relativo ao atributo <code>DisciplinasProcSeletivo</code>. Buscando todos os objetos
	 * correspondentes a entidade <code>DisciplinasProcSeletivo</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é importante
	 * para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
	 */
	public void montarListaSelectItemDisciplinasProcSeletivo() {
		try {
			montarListaSelectItemDisciplinasProcSeletivo("");
		} catch (Exception e) {
			// System.out.println("MENSAGEM => " + e.getMessage());;
		}
	}

	/**
	 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este atributo é uma lista (
	 * <code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
	 */
	public List<DisciplinasProcSeletivoVO> consultarDisciplinasProcSeletivoPorNome(String nomePrm) throws Exception {
		return getFacadeFactory().getDisciplinasProcSeletivoFacade().consultarPorNome(nomePrm, false, getUsuarioLogado());
	}

	public void inicializarListasSelectItemTodosComboBox() {
		montarListaSelectItemDisciplinasProcSeletivo();
	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		// itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("descricao", "Descrição"));
		return itens;
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("grupoDisciplinaProcSeletivoCons.xhtml");
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
	}

	public GrupoDisciplinaProcSeletivoVO getGrupoDisciplinaProcSeletivoVO() {
		if (grupoDisciplinaProcSeletivoVO == null) {
			grupoDisciplinaProcSeletivoVO = new GrupoDisciplinaProcSeletivoVO();
		}
		return grupoDisciplinaProcSeletivoVO;
	}

	public void setGrupoDisciplinaProcSeletivoVO(GrupoDisciplinaProcSeletivoVO grupoDisciplinaProcSeletivoVO) {
		this.grupoDisciplinaProcSeletivoVO = grupoDisciplinaProcSeletivoVO;
	}

	public DisciplinasGrupoDisciplinaProcSeletivoVO getDisciplinasGrupoDisciplinaProcSeletivoVO() {
		if (disciplinasGrupoDisciplinaProcSeletivoVO == null) {
			disciplinasGrupoDisciplinaProcSeletivoVO = new DisciplinasGrupoDisciplinaProcSeletivoVO();
		}
		return disciplinasGrupoDisciplinaProcSeletivoVO;
	}

	public void setDisciplinasGrupoDisciplinaProcSeletivoVO(DisciplinasGrupoDisciplinaProcSeletivoVO disciplinasGrupoDisciplinaProcSeletivoVO) {
		this.disciplinasGrupoDisciplinaProcSeletivoVO = disciplinasGrupoDisciplinaProcSeletivoVO;
	}

	public List<SelectItem> getListaSelectItemDisciplinasProcSeletivo() {
		if (listaSelectItemDisciplinasProcSeletivo == null) {
			listaSelectItemDisciplinasProcSeletivo = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemDisciplinasProcSeletivo;
	}

	public void setListaSelectItemDisciplinasProcSeletivo(List<SelectItem> listaSelectItemDisciplinasProcSeletivo) {
		this.listaSelectItemDisciplinasProcSeletivo = listaSelectItemDisciplinasProcSeletivo;
	}

	public void ativar() {
		try {
			getGrupoDisciplinaProcSeletivoVO().setSituacao(SituacaoGrupoDisciplinaProcSeletivoEnum.ATIVA);
			getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().alterar(grupoDisciplinaProcSeletivoVO, getUsuarioLogado());
			setMensagemID("msg_dados_ativado");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void inativar() {
		try {
			getGrupoDisciplinaProcSeletivoVO().setSituacao(SituacaoGrupoDisciplinaProcSeletivoEnum.INATIVA);
			getFacadeFactory().getGrupoDisciplinaProcSeletivoFacade().alterar(grupoDisciplinaProcSeletivoVO, getUsuarioLogado());
			setMensagemID("msg_dados_inativado");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public boolean getApresentarAtivar() {
		if (getGrupoDisciplinaProcSeletivoVO().getSituacao().equals(SituacaoGrupoDisciplinaProcSeletivoEnum.EM_ELABORACAO)) {
			return true;
		}
		return false;
	}

	public boolean getApresentarInativar() {
		if (getGrupoDisciplinaProcSeletivoVO().getSituacao().equals(SituacaoGrupoDisciplinaProcSeletivoEnum.ATIVA)) {
			return true;
		}
		return false;
	}
}
