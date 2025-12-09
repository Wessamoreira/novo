package relatorio.controle.recursoshumanos;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.administrativo.FuncionarioCargoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("FuncionarioRelControle")
@Scope("viewScope")
@Lazy
public class FuncionarioRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = -8297813585975013759L;

	private FuncionarioCargoVO funcionarioCargoVO;
	private Boolean utilizaRH;
	private List<SelectItem> listaSelectItemSituacaoFuncionario;

	private static final String CAMINHO_BASE_RH = "relatorio" + File.separator + "designRelatorio" + File.separator + "recursoshumanos" + File.separator;

	public FuncionarioRelControle() {
		montarListaSelectItemSituacaoFuncionario();
	}

	/**
	 * Imprime Relatorio em PDF
	 */
	public void imprimirPDF() {
		montarDadosRelatorio(TipoRelatorioEnum.PDF);
	}

	/**
	 * Imprime Relatorio em EXCEL
	 */
	public void imprimirEXCEL() {
		montarDadosRelatorio(TipoRelatorioEnum.EXCEL);
	}

	@SuppressWarnings("unchecked")
	private void montarDadosRelatorio(TipoRelatorioEnum tipoRelatorioEnum) {
		List<FuncionarioCargoVO> listaFuncionariosCargo = new ArrayList<>();
		try {
			listaFuncionariosCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarListaFuncionarioPorSituacao(
					SituacaoFuncionarioEnum.valueOf(getFuncionarioCargoVO().getSituacaoFuncionario()), getUtilizaRH(), Uteis.NIVELMONTARDADOS_COMBOBOX);

			if (!listaFuncionariosCargo.isEmpty()) {

				List<FuncionarioCargoRelVO> listaObjetos = montarDadosFuncionarioCargoVO(listaFuncionariosCargo);
				
				getSuperParametroRelVO().setNomeDesignIreport(CAMINHO_BASE_RH + "RelatorioFuncionarios" + ".jrxml");
				getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
				getSuperParametroRelVO().setSubReport_Dir(CAMINHO_BASE_RH);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório de Funcionários");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setCaminhoBaseRelatorio(CAMINHO_BASE_RH);
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");

				getSuperParametroRelVO().getParametros().put("situacao", SituacaoFuncionarioEnum.valueOf(getFuncionarioCargoVO().getSituacaoFuncionario()).getValorApresentar());

				realizarImpressaoRelatorio();
				removerObjetoMemoria(this);

				setMensagemID("msg_relatorio_ok");
			} else {
				setUsarTargetBlank("");
				setMensagemID("msg_relatorio_sem_dados");
			}
		} catch (Exception e) {
			setUsarTargetBlank("");
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaFuncionariosCargo);
		}
	}

	private List<FuncionarioCargoRelVO> montarDadosFuncionarioCargoVO(List<FuncionarioCargoVO> listaFuncionariosCargo) {
		List<FuncionarioCargoRelVO> listaObjetos = new ArrayList<>();
		for (FuncionarioCargoVO funcionarioCargo : listaFuncionariosCargo) {
			FuncionarioCargoRelVO obj = new FuncionarioCargoRelVO();
			obj.setMatriculaCargo(funcionarioCargo.getMatriculaCargo());
			obj.setNomeFuncionario(funcionarioCargo.getFuncionarioVO().getPessoa().getNome());
			obj.setCargo(funcionarioCargo.getCargo().getNome());
			obj.setIdentificadorSecao(funcionarioCargo.getSecaoFolhaPagamento().getIdentificador());
			obj.setSecao(funcionarioCargo.getSecaoFolhaPagamento().getDescricao());
			obj.setDepartamento(funcionarioCargo.getDepartamento().getNome());
			if (Uteis.isAtributoPreenchido(funcionarioCargo.getFormaContratacao())) {
				obj.setFormaContratacao(funcionarioCargo.getFormaContratacao().getValorApresentar());
			}
			
			listaObjetos.add(obj);
		}
		return listaObjetos;
	}

	/**
	 * Monta a combobox da situacao do funcionario cargo.
	 */
	private void montarListaSelectItemSituacaoFuncionario() {
		setListaSelectItemSituacaoFuncionario(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoFuncionarioEnum.class, false));
	}

	// GETTER AND SETTER
	public FuncionarioCargoVO getFuncionarioCargoVO() {
		if (funcionarioCargoVO == null) {
			funcionarioCargoVO = new FuncionarioCargoVO();
		}
		return funcionarioCargoVO;
	}

	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
	}

	public Boolean getUtilizaRH() {
		if (utilizaRH == null) {
			utilizaRH = Boolean.TRUE;
		}
		return utilizaRH;
	}

	public void setUtilizaRH(Boolean utilizaRH) {
		this.utilizaRH = utilizaRH;
	}

	public List<SelectItem> getListaSelectItemSituacaoFuncionario() {
		if (listaSelectItemSituacaoFuncionario == null) {
			montarListaSelectItemSituacaoFuncionario();
		}
		return listaSelectItemSituacaoFuncionario;
	}

	public void setListaSelectItemSituacaoFuncionario(List<SelectItem> listaSelectItemSituacaoFuncionario) {
		this.listaSelectItemSituacaoFuncionario = listaSelectItemSituacaoFuncionario;
	}
}
