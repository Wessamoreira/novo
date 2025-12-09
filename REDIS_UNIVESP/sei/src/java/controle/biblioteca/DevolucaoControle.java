package controle.biblioteca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.biblioteca.ConfiguracaoBibliotecaVO;
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.EstadoHistoricoExemplar;
import negocio.comuns.utilitarias.dominios.TipoPessoa; @Controller("DevolucaoControle")
@Scope("request")
@Lazy
public class DevolucaoControle extends SuperControle implements Serializable {

	private Boolean apresentarPanelMulta;
	private ItemEmprestimoVO itemEmprestimoVO;
	private EmprestimoVO emprestimoVO;
	private String campoConsultaEmprestimo;
	private String valorConsultaEmprestimo;
	private List<EmprestimoVO> listaConsultaEmprestimo;
	private List listaSelectItemEstadoExemplar;
	private ConfiguracaoBibliotecaVO confPadraoBib;

	public DevolucaoControle() throws Exception {
		getItemEmprestimoVO().setResponsavelDevolucao(getUsuarioLogadoClone());
		getItemEmprestimoVO().setDataDevolucao(new Date());
		setControleConsulta(new ControleConsulta());
		montarListaSelectItemEstadoExemplar();
		setMensagemID("msg_entre_prmconsulta");
	}

	public String getExibirPanelMulta() {
		try {
			if (getFacadeFactory().getContaReceberFacade().verificarContaReceberPessoaTipoBiblioteca(
					getEmprestimoVO().getPessoa().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado())) {
				return "RichFaces.$('panelMulta').show()";
			} else {
				return "";
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}

	/**
	 * Encaminha o usuário para a tela de recebimento.
	 * 
	 * @return
	 */
//	public String encaminharTelaRecebimento() {
//		try {
//			return navegarPara(
//					NegociacaoRecebimentoControle.class.getSimpleName(),
//					"setNegociacaoRecebimentoVOPreenchido",
//					"receber",
//					getFacadeFactory().getEmprestimoFacade().preencherDadosNegociacaoRecebimentoMultaBiblioteca(
//							getEmprestimoVO()));
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//			return "";
//		}
//	}

//	/**
//	 * Método que faz a devolução dos itens de um empréstimo.
//	 */
//	public void devolver() {
//		try {
//			setConfPadraoBib(getFacadeFactory().getConfiguracaoBibliotecaFacade().consultarConfiguracaoPadrao(true,
//					Uteis.NIVELMONTARDADOS_TODOS));
//			setEmprestimoVO(getFacadeFactory().getEmprestimoFacade().devolver(getEmprestimoVO(), false, getConfPadraoBib()));
//			// Após fazer a devolução, checar a situação dos itens do empréstimo
//			// para uma possível alteração para
//			// FINALIZADO.
//			if (getFacadeFactory().getEmprestimoFacade().checarDevolucaoRenovacaoTodosItens(getEmprestimoVO())) {
//				getFacadeFactory().getEmprestimoFacade().alterarSituacaoEmprestimoParaFinalizado(getEmprestimoVO());
//			}
//			setMensagemID("msg_dados_gravados");
//			setMensagemDetalhada("");
//		} catch (Exception e) {
//			setMensagemDetalhada("msg_erro", e.getMessage());
//		} finally {
//			setConfPadraoBib(null);
//		}
//	}

	/**
	 * Método que faz a renovação dos itens de um empréstimo.
	 */
	public void renovar() {
		try {

			// Verificar número de renovações que são permitidas para a obra.
//			getFacadeFactory().getEmprestimoFacade().verificarNrMaximoRenovacoesPessoa(getEmprestimoVO());

			// Verificar se pode ser renovado com base no número mínimo que deve
			// ficar na biblioteca.
//			getFacadeFactory().getEmprestimoFacade().verificarNrMinimoExemplaresFixosBiblioteca(getEmprestimoVO());

			// Verificar se não existem pendências financeiras na biblioteca que
			// impossibilitem a renovação
//			getFacadeFactory().getEmprestimoFacade().verificarPendenciasFinanceirasBiblioteca(getEmprestimoVO());

			// Ao fazer uma renovação, o fluxo a ser seguido será:
			// Gravar uma devolução e todos seus aspectos
			// Gravar a renovação e todos os seus aspectos
			// Checar se todos os itens estão devolvidos ou renovados para mudar
			// a situação do empréstimo para
			// FINALIZADO.
//			getFacadeFactory().getEmprestimoFacade().devolver(getEmprestimoVO(), true, getConfPadraoBib());
//			setEmprestimoVO(getFacadeFactory().getEmprestimoFacade().renovar(getEmprestimoVO()));
//			if (getFacadeFactory().getEmprestimoFacade().checarDevolucaoRenovacaoTodosItens(getEmprestimoVO())) {
//				getFacadeFactory().getEmprestimoFacade().alterarSituacaoEmprestimoParaFinalizado(getEmprestimoVO());
//			}
			setMensagemID("msg_dados_gravados");
			setMensagemDetalhada("");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	/**
	 * Método que pega o itemEmprestimo do contexto, pegando o valor boolean
	 * selecionado na tela.
	 */
	public void mudarItemEmprestimoSelecionado() {
		ItemEmprestimoVO obj = (ItemEmprestimoVO) context().getExternalContext().getRequestMap().get("itemEmprestimo");
	}

	@SuppressWarnings("unchecked")
	public String consultarEmprestimo() {
		try {
			List<EmprestimoVO> objs = new ArrayList<EmprestimoVO>(0);
			if (getCampoConsultaEmprestimo().equals("codigo")) {
				if (getValorConsultaEmprestimo().equals("")) {
					setValorConsultaEmprestimo("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaEmprestimo());
				objs = getFacadeFactory().getEmprestimoFacade().consultarPorCodigoSituacaoEmExecucao(new Integer(valorInt),
						"EX", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaEmprestimo().equals("codigoBarra")) {
				objs = getFacadeFactory().getEmprestimoFacade().consultarPorCodigoBarraExemplar(getValorConsultaEmprestimo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaEmprestimo().equals("aluno")) {
				objs = getFacadeFactory().getEmprestimoFacade().consultarPorTipoPessoa(getValorConsultaEmprestimo(), TipoPessoa.ALUNO.getValor(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaEmprestimo().equals("funcionario")) {
				objs = getFacadeFactory().getEmprestimoFacade().consultarPorTipoPessoa(getValorConsultaEmprestimo(), TipoPessoa.FUNCIONARIO.getValor(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			if (getCampoConsultaEmprestimo().equals("professor")) {
				objs = getFacadeFactory().getEmprestimoFacade().consultarPorTipoPessoa(getValorConsultaEmprestimo(), TipoPessoa.PROFESSOR.getValor(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
			}
			setListaConsultaEmprestimo(objs);
			setMensagemID("msg_dados_consultados");
			return "consultar";
		} catch (Exception e) {
			setListaConsultaEmprestimo(new ArrayList<EmprestimoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "consultar";
		}
	}

	public void selecionarEmprestimo() throws Exception {
		EmprestimoVO obj = (EmprestimoVO) context().getExternalContext().getRequestMap().get("emprestimo");
		if (getMensagemDetalhada().equals("")) {
			obj.setItemEmprestimoVOs(getFacadeFactory().getItemEmprestimoFacade().consultarItemEmprestimos(obj.getCodigo(),
					false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			this.setEmprestimoVO(obj);
		}
		Uteis.liberarListaMemoria(this.getListaConsultaEmprestimo());
		this.setValorConsultaEmprestimo(null);
		this.setCampoConsultaEmprestimo(null);
	}

	public void limparCampoEmprestimo() {
		setEmprestimoVO(new EmprestimoVO());
	}

	public List<SelectItem> getTipoConsultaComboEmprestimo() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("codigo", "Código"));
		itens.add(new SelectItem("codigoBarra", "Código de Barra"));
		itens.add(new SelectItem("aluno", "Aluno"));
		itens.add(new SelectItem("funcionario", "Funcionário"));
		itens.add(new SelectItem("professor", "Professor"));
		return itens;
	}

	public void montarListaSelectItemEstadoExemplar() {
		setListaSelectItemEstadoExemplar(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(EstadoHistoricoExemplar.class));
	}

	public ItemEmprestimoVO getItemEmprestimoVO() {
		if (itemEmprestimoVO == null) {
			itemEmprestimoVO = new ItemEmprestimoVO();
		}
		return itemEmprestimoVO;
	}

	public void setItemEmprestimoVO(ItemEmprestimoVO itemEmprestimoVO) {
		this.itemEmprestimoVO = itemEmprestimoVO;
	}

	public void setEmprestimoVO(EmprestimoVO emprestimoVO) {
		this.emprestimoVO = emprestimoVO;
	}

	public EmprestimoVO getEmprestimoVO() {
		if (emprestimoVO == null) {
			emprestimoVO = new EmprestimoVO();
		}
		return emprestimoVO;
	}

	public String getCampoConsultaEmprestimo() {
		if (campoConsultaEmprestimo == null) {
			campoConsultaEmprestimo = "";
		}
		return campoConsultaEmprestimo;
	}

	public void setCampoConsultaEmprestimo(String campoConsultaEmprestimo) {
		this.campoConsultaEmprestimo = campoConsultaEmprestimo;
	}

	public String getValorConsultaEmprestimo() {
		if (valorConsultaEmprestimo == null) {
			valorConsultaEmprestimo = "";
		}
		return valorConsultaEmprestimo;
	}

	public void setValorConsultaEmprestimo(String valorConsultaEmprestimo) {
		this.valorConsultaEmprestimo = valorConsultaEmprestimo;
	}

	public List<EmprestimoVO> getListaConsultaEmprestimo() {
		if (listaConsultaEmprestimo == null) {
			listaConsultaEmprestimo = new ArrayList<EmprestimoVO>(0);
		}
		return listaConsultaEmprestimo;
	}

	public void setListaConsultaEmprestimo(List<EmprestimoVO> listaConsultaEmprestimo) {
		this.listaConsultaEmprestimo = listaConsultaEmprestimo;
	}

	public List getListaSelectItemEstadoExemplar() {
		if (listaSelectItemEstadoExemplar == null) {
			listaSelectItemEstadoExemplar = new ArrayList(0);
		}
		return listaSelectItemEstadoExemplar;
	}

	public void setListaSelectItemEstadoExemplar(List listaSelectItemEstadoExemplar) {
		this.listaSelectItemEstadoExemplar = listaSelectItemEstadoExemplar;
	}

	public Boolean getApresentarPanelMulta() {
		if (apresentarPanelMulta == null) {
			apresentarPanelMulta = false;
		}
		return apresentarPanelMulta;
	}

	public void setApresentarPanelMulta(Boolean apresentarPanelMulta) {
		this.apresentarPanelMulta = apresentarPanelMulta;
	}

	public ConfiguracaoBibliotecaVO getConfPadraoBib() {
		if (confPadraoBib == null) {
			confPadraoBib = new ConfiguracaoBibliotecaVO();
		}
		return confPadraoBib;
	}

	public void setConfPadraoBib(ConfiguracaoBibliotecaVO confPadraoBib) {
		this.confPadraoBib = confPadraoBib;
	}
	
	
}
