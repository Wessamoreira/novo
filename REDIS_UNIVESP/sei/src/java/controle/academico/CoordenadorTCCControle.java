package controle.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("CoordenadorTCCControle")
@Scope("viewScope")
@Lazy
public class CoordenadorTCCControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private UnidadeEnsinoVO unidadeEnsinoVO;
	private UnidadeEnsinoCursoVO unidadeEnsinoCursoVO;
	
	private List<FuncionarioVO> listaConsultaFuncionario;
	private String campoConsultaFuncionario;
	private String valorConsultaFuncionario;
	

	public CoordenadorTCCControle() throws Exception {
		setControleConsulta(new ControleConsulta());
		setMensagemID("msg_entre_prmconsulta");
	}

	/**
	 * Rotina responsável por disponibilizar os dados de um objeto da classe
	 * <code>UnidadeEnsino</code> para alteração. O objeto desta classe é
	 * disponibilizado na session da página (request) para que o JSP
	 * correspondente possa disponibilizá-lo para edição.
	 */
	public String editar() throws Exception {
		try {
			UnidadeEnsinoVO obj = (UnidadeEnsinoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoItens");
			obj = montarDadosUnidadeEnsinoVOCompleto(obj);
			setUnidadeEnsinoVO(obj);
			setUnidadeEnsinoCursoVO(new UnidadeEnsinoCursoVO());
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("coordenadorTCCForm.xhtml");
	}
	
	/*
	 * Método responsável por disponibilizar dados de um objeto da classe
	 * <code>UnidadeEnsinoCurso</code> para edição pelo usuário.
	 */
	public void editarUnidadeEnsinoCurso() throws Exception {
		UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItens");
		setUnidadeEnsinoCursoVO(obj);
	}
	
	/**
	 * Rotina responsável por gravar no BD os dados editados de um novo objeto
	 * da classe <code>UnidadeEnsino</code>. Caso o objeto seja novo (ainda não
	 * gravado no BD) é acionado a operação <code>incluir()</code>. Caso
	 * contrário é acionado o <code>alterar()</code>. Se houver alguma
	 * inconsistência o objeto não é gravado, sendo re-apresentado para o
	 * usuário juntamente com uma mensagem de erro.
	 */
	public void gravar() {
		try {
			getFacadeFactory().getUnidadeEnsinoFacade().alterarCoordenadoresTCC(unidadeEnsinoVO, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public UnidadeEnsinoVO montarDadosUnidadeEnsinoVOCompleto(UnidadeEnsinoVO obj) {	
		try {			
			return getAplicacaoControle().getUnidadeEnsinoVO(obj.getCodigo(), null);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return new UnidadeEnsinoVO();
	}

	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * UnidadeEnsinoCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			List<UnidadeEnsinoVO> objs = new ArrayList<UnidadeEnsinoVO>(0);
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNome(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("razaoSocial")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorRazaoSocial(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("nomeCidade")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorNomeCidade(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("CNPJ")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCnpj(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("inscEstadual")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorInscEstatual(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("RG")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorRg(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getControleConsulta().getCampoConsulta().equals("CPF")) {
				objs = getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCpf(getControleConsulta().getValorConsulta(), super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsulta(objs);
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("coordenadorTCCCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList<UnidadeEnsinoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("coordenadorTCCCons.xhtml");
		}
	}
	
	public List<SelectItem> getTipoConsultaFuncionario() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}

	public void consultarFuncionario() {
		try {
			List<FuncionarioVO> objs = new ArrayList<FuncionarioVO>(0);
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "", null, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), null, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaFuncionario(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void selecionarFuncionario() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
		getUnidadeEnsinoCursoVO().setCoordenadorTCC(obj);
		listaConsultaFuncionario.clear();
		this.setValorConsultaFuncionario("");
		this.setCampoConsultaFuncionario("");
	}
	
	public void selecionarFuncionarioUnidadeEnsino() {
		FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioUnidadeEnsinoItens");
		getUnidadeEnsinoVO().setCoordenadorTCC(obj);
		listaConsultaFuncionario.clear();
		this.setValorConsultaFuncionario("");
		this.setCampoConsultaFuncionario("");
	}
	
	public void limparFuncionario() {
		getUnidadeEnsinoVO().setCoordenadorTCC(new FuncionarioVO());
	}
	
	public void limparFuncionarioUnidadeEnsino() {
		UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItens");
		obj.setCoordenadorTCC(new FuncionarioVO());
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

	public Boolean getDesenharTurno() {
		if (getUnidadeEnsinoCursoVO().getCurso().getCodigo().intValue() != 0) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;

	}

	/**
	 * Rotina responsável por preencher a combo de consulta da telas.
	 */
	public List<SelectItem> getTipoConsultaCombo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("nomeCidade", "Cidade"));
		itens.add(new SelectItem("CNPJ", "CNPJ"));
		itens.add(new SelectItem("inscEstadual", "Inscrição Estadual"));
		return itens;
	}

	public String getMascaraConsulta() {
		if (getControleConsulta().getCampoConsulta().equals("CNPJ")) {
			return "return mascara(this.form,'formCadastro:valorConsulta','99.999.999/9999-99',event)";
		}
		return "";
	}

	/**
	 * Rotina responsável por organizar a paginação entre as páginas resultantes
	 * de uma consulta.
	 */
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList<UnidadeEnsinoVO>(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("coordenadorTCCCons.xhtml");
	}

	public UnidadeEnsinoCursoVO getUnidadeEnsinoCursoVO() {
		return unidadeEnsinoCursoVO;
	}

	public void setUnidadeEnsinoCursoVO(UnidadeEnsinoCursoVO unidadeEnsinoCursoVO) {
		this.unidadeEnsinoCursoVO = unidadeEnsinoCursoVO;
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

	public String getCampoConsultaFuncionario() {
		return campoConsultaFuncionario;
	}

	public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
		this.campoConsultaFuncionario = campoConsultaFuncionario;
	}

	public String getValorConsultaFuncionario() {
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}

	public List<FuncionarioVO> getListaConsultaFuncionario() {
		return listaConsultaFuncionario;
	}

	public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
		this.listaConsultaFuncionario = listaConsultaFuncionario;
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		unidadeEnsinoVO = null;
		unidadeEnsinoCursoVO = null;
	}
	
}
