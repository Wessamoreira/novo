package controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.financeiro.IndiceReajustePeriodoMatriculaPeriodoVencimentoVO;
import negocio.comuns.financeiro.IndiceReajustePeriodoVO;
import negocio.comuns.financeiro.IndiceReajusteVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.SituacaoExecucaoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("IndiceReajusteControle")
@Scope("viewScope")
@Lazy
public class IndiceReajusteControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = 1L;
	private IndiceReajusteVO indiceReajusteVO;
	private IndiceReajustePeriodoVO indiceReajustePeriodoVO;
	private List<SelectItem> listaSelectItemMes;
	private List<SelectItem> listaSelectItemAnoVOs;
	private IndiceReajustePeriodoVO indiceReajustePeriodoProcessadoVO;
	private Boolean apresentarBotaoCancelarReajustePreco;
	private IndiceReajustePeriodoMatriculaPeriodoVencimentoVO indiceReajustePeriodoMatriculaPeriodoVencimentoVO;
	private Boolean opcaoSelecaoMarcarDesmarcar;

	public IndiceReajusteControle() {
		super();
		try {
			verificarApresentarBotaoCancelarReajustePreco();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void persistir() {
		try {
			getFacadeFactory().getIndiceReajusteFacade().persistir(getIndiceReajusteVO(), true, getUsuarioLogado());
			setMensagemID("msg_dados_gravados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("indiceReajusteCons.xhtml");
	}
	
	public String novo() {
		removerObjetoMemoria(this);
		setIndiceReajusteVO(new IndiceReajusteVO());
		setIndiceReajustePeriodoVO(new IndiceReajustePeriodoVO());
		montarListaSelectItemAno();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("indiceReajusteForm.xhtml");
	}
	
	public String excluir() {
		try {
			getFacadeFactory().getIndiceReajusteFacade().excluir(getIndiceReajusteVO(), getUsuarioLogado());
			setIndiceReajusteVO(new IndiceReajusteVO());
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("indiceReajusteForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("indiceReajusteForm.xhtml");
		}
	}
	
	public String editar() {
		IndiceReajusteVO obj = (IndiceReajusteVO) context().getExternalContext().getRequestMap().get("indiceReajusteItens");
		try {
			getFacadeFactory().getIndiceReajusteFacade().carregarDados(obj, getUsuarioLogado());
			obj.setNovoObj(Boolean.FALSE);
			Ordenacao.ordenarListaDecrescente(obj.getListaIndiceReajustePeriodoVOs(), "ordenacaoMes");
			setIndiceReajusteVO(obj);
			montarListaSelectItemAno();
			verificarApresentarBotaoCancelarReajustePreco();
			setProgressBarVO(getFacadeFactory().getIndiceReajustePeriodoFacade().consultarProgressBarEmExecucao(obj));
			setMensagemID("msg_dados_editar");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		return Uteis.getCaminhoRedirecionamentoNavegacao("indiceReajusteForm.xhtml");
	}

	public void adicionarIndiceReajustePeriodo() {
		try {
			getFacadeFactory().getIndiceReajusteFacade().adicionarIndiceReajustePeriodo(this.getIndiceReajusteVO().getListaIndiceReajustePeriodoVOs(), getIndiceReajustePeriodoVO(), getUsuarioLogado());
			setIndiceReajustePeriodoVO(new IndiceReajustePeriodoVO());
			setMensagemID("msg_dados_adicionados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void removerIndiceReajustePeriodo() {
		try {
			IndiceReajustePeriodoVO obj = (IndiceReajustePeriodoVO) context().getExternalContext().getRequestMap().get("indiceReajusteItens");
			getFacadeFactory().getIndiceReajusteFacade().removerIndiceReajustePeriodo(this.getIndiceReajusteVO().getListaIndiceReajustePeriodoVOs(), obj, getUsuarioLogado());
			setMensagemID("msg_dados_excluidos");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String consultar() {
		setListaConsulta(getFacadeFactory().getIndiceReajusteFacade().consultar(getControleConsulta().getCampoConsulta(), getControleConsulta().getValorConsulta(), getUsuarioLogado()));
		return Uteis.getCaminhoRedirecionamentoNavegacao("indiceReajusteCons.xhtml");
	}
	
	public void inicializarDadosAlunosTiveramReajustePrecoProcessado() {
		IndiceReajustePeriodoVO obj = (IndiceReajustePeriodoVO) context().getExternalContext().getRequestMap().get("indiceReajusteItens");
		try {
			obj.setListaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs(getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().consultarPorIndiceReajustePeriodoSituacao(obj.getCodigo(), obj.getSituacaoExecucao(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		setIndiceReajustePeriodoProcessadoVO(obj);
	}

	public IndiceReajusteVO getIndiceReajusteVO() {
		if (indiceReajusteVO == null) {
			indiceReajusteVO = new IndiceReajusteVO();
		}
		return indiceReajusteVO;
	}

	public void setIndiceReajusteVO(IndiceReajusteVO indiceReajusteVO) {
		this.indiceReajusteVO = indiceReajusteVO;
	}

	public IndiceReajustePeriodoVO getIndiceReajustePeriodoVO() {
		if (indiceReajustePeriodoVO == null) {
			indiceReajustePeriodoVO = new IndiceReajustePeriodoVO();
		}
		return indiceReajustePeriodoVO;
	}

	public void setIndiceReajustePeriodoVO(IndiceReajustePeriodoVO indiceReajustePeriodoVO) {
		this.indiceReajustePeriodoVO = indiceReajustePeriodoVO;
	}
	
	public List<SelectItem> getListaSelectItemMes() {
		if (listaSelectItemMes == null) {
			listaSelectItemMes = new ArrayList<SelectItem>();
			for (MesAnoEnum mesAnoEnum : MesAnoEnum.values()) {
				listaSelectItemMes.add(new SelectItem(mesAnoEnum, mesAnoEnum.getMes()));
			}
		}
		return listaSelectItemMes;
	}
	
	public void setListaSelectItemMes(List<SelectItem> listaSelectItemMes) {
		this.listaSelectItemMes = listaSelectItemMes;
	}

	public List<SelectItem> getListaSelectItemAnoVOs() {
		if (listaSelectItemAnoVOs == null) {
			listaSelectItemAnoVOs = new ArrayList<SelectItem>(0);
		}
		return listaSelectItemAnoVOs;
	}

	public void setListaSelectItemAnoVOs(List<SelectItem> listaSelectItemAnoVOs) {
		this.listaSelectItemAnoVOs = listaSelectItemAnoVOs;
	}
	
	public void montarListaSelectItemAno() {
		List<String> listaAnoVOs = getFacadeFactory().getIndiceReajusteFacade().montarListaSelectItemAno(Uteis.getAnoDataAtual4Digitos(), getUsuarioLogado());
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		for (String ano : listaAnoVOs) {
			itens.add(new SelectItem(ano, ano));
		}
		setListaSelectItemAnoVOs(itens);
	}
	
	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("DESCRICAO", "Descrição"));
		return itens;
	}
	
	ProgressBarVO progressBarVO =  new ProgressBarVO();
	
	
	
	public ProgressBarVO getProgressBarVO() {
		if(progressBarVO == null) {
			progressBarVO =  new ProgressBarVO();
		}
		return progressBarVO;
	}

	public void setProgressBarVO(ProgressBarVO progressBarVO) {
		this.progressBarVO = progressBarVO;
	}

	
	public void executarIndiceReajustePeriodo() {
		IndiceReajustePeriodoVO obj = (IndiceReajustePeriodoVO) context().getExternalContext().getRequestMap().get("indiceReajusteItens");
		try {
			if (getIndiceReajusteVO().getCodigo().equals(0)) {
				getFacadeFactory().getIndiceReajusteFacade().persistir(getIndiceReajusteVO(), false, getUsuarioLogado());
			}
			if (obj.getCodigo().equals(0)) {
				obj.setIndiceReajusteVO(getIndiceReajusteVO());
				getFacadeFactory().getIndiceReajustePeriodoFacade().incluir(obj, getUsuarioLogado());
			}
			getProgressBarVO().resetar();
			getProgressBarVO().iniciar(1l, 10000, "Consultando contas.....", false, null, "");
			getFacadeFactory().getIndiceReajustePeriodoFacade().executarReajustePrecoContaReceber(getProgressBarVO(), getIndiceReajusteVO(), obj, getUsuarioLogado());
			verificarApresentarBotaoCancelarReajustePreco();
			setMensagemID(Uteis.SUCESSO, "msg_executandoReajustePrecoContaReceber");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void cancelarIndiceReajustePeriodo() {
		try {
			getProgressBarVO().resetar();
			getProgressBarVO().iniciar(1l, 10000, "Consultando contas.....", false, null, "");
			getFacadeFactory().getIndiceReajustePeriodoFacade().executarCancelamentoReajustePrecoContaReceber(getProgressBarVO(),getIndiceReajusteVO(), getIndiceReajustePeriodoProcessadoVO(), getUsuarioLogado());
			setIndiceReajustePeriodoProcessadoVO(new IndiceReajustePeriodoVO());
			setMensagemID("msg_cancelarReajustePrecoContaReceber");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}finally {
			progressBarVO.setProgresso(progressBarVO.getMaxValue().longValue());
			progressBarVO.setForcarEncerramento(true);
			progressBarVO.encerrar();
		}
	}
	
	public void cancelarIndiceReajustePeriodoMatriculaPeriodoVencimento() {
		try {
			getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().cancelarIndiceReajustePeriodoMatriculaPeriodoVencimento(getIndiceReajustePeriodoProcessadoVO().getListaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs(), getIndiceReajustePeriodoMatriculaPeriodoVencimentoVO().getMotivoCancelamento(), getUsuarioLogado());
//			getIndiceReajustePeriodoProcessadoVO().setListaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs(getFacadeFactory().getIndiceReajustePeriodoMatriculaPeriodoVencimentoFacade().consultarPorIndiceReajustePeriodoSituacao(getIndiceReajustePeriodoMatriculaPeriodoVencimentoVO().getIndiceReajustePeriodoVO().getCodigo(), SituacaoExecucaoEnum.PROCESSADO, getUsuarioLogado()));
			setMensagemID("msg_cancelarReajustePrecoContaReceber");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void inicializarDadosIndiceReajustePeriodoMatriculaPeriodoVencimento() {
		getIndiceReajustePeriodoMatriculaPeriodoVencimentoVO().setDataCancelamento(new Date());
		getIndiceReajustePeriodoMatriculaPeriodoVencimentoVO().setResponsavelCancelamentoVO(getUsuarioLogadoClone());
		getIndiceReajustePeriodoMatriculaPeriodoVencimentoVO().setMotivoCancelamento("");
		getIndiceReajustePeriodoMatriculaPeriodoVencimentoVO().setIndiceReajustePeriodoVO(getIndiceReajustePeriodoProcessadoVO());
	}
	
	public Boolean getApresentarBotaoExcluir() {
		for (IndiceReajustePeriodoVO indiceReajustePeriodoVO : getIndiceReajusteVO().getListaIndiceReajustePeriodoVOs()) {
			if (!indiceReajustePeriodoVO.getSituacaoExecucao().equals(SituacaoExecucaoEnum.AGUARDANDO_PROCESSAMENTO)) {
				return false;
			}
		}
		return true;
	}
	
	public void inicializarDadosIndiceReajustePeriodo() {
		IndiceReajustePeriodoVO obj = (IndiceReajustePeriodoVO) context().getExternalContext().getRequestMap().get("indiceReajusteItens");
		setIndiceReajustePeriodoProcessadoVO(obj);
	}

	public IndiceReajustePeriodoVO getIndiceReajustePeriodoProcessadoVO() {
		if (indiceReajustePeriodoProcessadoVO == null) {
			indiceReajustePeriodoProcessadoVO = new IndiceReajustePeriodoVO();
		}
		return indiceReajustePeriodoProcessadoVO;
	}

	public void setIndiceReajustePeriodoProcessadoVO(IndiceReajustePeriodoVO indiceReajustePeriodoProcessadoVO) {
		this.indiceReajustePeriodoProcessadoVO = indiceReajustePeriodoProcessadoVO;
	}
	
	public void verificarApresentarBotaoCancelarReajustePreco() throws Exception {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("PermitirRealizarCancelamentoReajustePreco", getUsuarioLogado());
			setApresentarBotaoCancelarReajustePreco(Boolean.TRUE);
		} catch (Exception e) {
			setApresentarBotaoCancelarReajustePreco(Boolean.FALSE);
		}
	}

	public Boolean getApresentarBotaoCancelarReajustePreco() {
		if (apresentarBotaoCancelarReajustePreco == null) {
			apresentarBotaoCancelarReajustePreco = false;
		}
		return apresentarBotaoCancelarReajustePreco;
	}

	public void setApresentarBotaoCancelarReajustePreco(Boolean apresentarBotaoCancelarReajustePreco) {
		this.apresentarBotaoCancelarReajustePreco = apresentarBotaoCancelarReajustePreco;
	}

	public IndiceReajustePeriodoMatriculaPeriodoVencimentoVO getIndiceReajustePeriodoMatriculaPeriodoVencimentoVO() {
		if (indiceReajustePeriodoMatriculaPeriodoVencimentoVO == null) {
			indiceReajustePeriodoMatriculaPeriodoVencimentoVO = new IndiceReajustePeriodoMatriculaPeriodoVencimentoVO();
		}
		return indiceReajustePeriodoMatriculaPeriodoVencimentoVO;
	}

	public void setIndiceReajustePeriodoMatriculaPeriodoVencimentoVO(IndiceReajustePeriodoMatriculaPeriodoVencimentoVO indiceReajustePeriodoMatriculaPeriodoVencimentoVO) {
		this.indiceReajustePeriodoMatriculaPeriodoVencimentoVO = indiceReajustePeriodoMatriculaPeriodoVencimentoVO;
	}
	
	public void selecionarTodosDesmarcarTodosIndicesReajusteProcessadoVencimento() {
		for (IndiceReajustePeriodoMatriculaPeriodoVencimentoVO obj : getIndiceReajustePeriodoProcessadoVO().getListaIndiceReajustePeriodoMatriculaPeriodoVencimentoVOs()) {
			if (!obj.getSituacao().equals(SituacaoExecucaoEnum.CANCELADO)) {
				obj.setSelecionado(getOpcaoSelecaoMarcarDesmarcar());
			} else {
				obj.setSelecionado(false);
			}
		}
	}

	public Boolean getOpcaoSelecaoMarcarDesmarcar() {
		if (opcaoSelecaoMarcarDesmarcar == null) {
			opcaoSelecaoMarcarDesmarcar = Boolean.FALSE;
		}
		return opcaoSelecaoMarcarDesmarcar;
	}

	public void setOpcaoSelecaoMarcarDesmarcar(Boolean opcaoSelecaoMarcarDesmarcar) {
		this.opcaoSelecaoMarcarDesmarcar = opcaoSelecaoMarcarDesmarcar;
	}
	

	public void atualizarStatusProcessamento() {
		for(IndiceReajustePeriodoVO indiceReajustePeriodoVO : getIndiceReajusteVO().getListaIndiceReajustePeriodoVOs()) {
			if(indiceReajustePeriodoVO.getSituacaoExecucao().equals(SituacaoExecucaoEnum.EM_PROCESSAMENTO)) {
				IndiceReajustePeriodoVO indiceReajustePeriodoVO2 = getFacadeFactory().getIndiceReajustePeriodoFacade().consultarPorChavePrimaria(indiceReajustePeriodoVO.getCodigo(), getUsuarioLogado());
				indiceReajustePeriodoVO.setSituacaoExecucao(indiceReajustePeriodoVO2.getSituacaoExecucao());
				indiceReajustePeriodoVO.setLogAlunosNaoDevemSofrerReajuste(indiceReajustePeriodoVO2.getLogAlunosNaoDevemSofrerReajuste());
				indiceReajustePeriodoVO.setLogErroProcessamento(indiceReajustePeriodoVO2.getLogErroProcessamento());
			}
		}
	}

}
