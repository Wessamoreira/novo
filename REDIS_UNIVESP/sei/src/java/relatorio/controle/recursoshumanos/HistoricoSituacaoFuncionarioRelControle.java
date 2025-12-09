package relatorio.controle.recursoshumanos;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.recursoshumanos.HistoricoSituacaoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.administrativo.FuncionarioCargoRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

@Controller("HistoricoSituacaoFuncionarioRelControle")
@Scope("viewScope")
@Lazy
public class HistoricoSituacaoFuncionarioRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = -8297813585975013759L;

	private Date dataInicio;
	private Date dataFinal;
	private String situacao;
	private FuncionarioCargoVO funcionarioCargoVO;
	
	private String valorConsultaFuncionario;
	private String campoConsultaFuncionario;
	private List<FuncionarioCargoVO> funcionarioCargoVOs;

	private List<SelectItem> listaSelectItemSituacaoFuncionario;

	private static final String CAMINHO_BASE_RH = "relatorio" + File.separator + "designRelatorio" + File.separator + "recursoshumanos" + File.separator;

	public HistoricoSituacaoFuncionarioRelControle() {
		montarListaSelectItemSituacaoFuncionario();
	}

	/**
	 * Imprime Relatorio em PDF
	 */
	public void imprimirPDF() {
		montarDadosRelatorio(TipoRelatorioEnum.PDF);
	}

	private void montarDadosRelatorio(TipoRelatorioEnum tipoRelatorioEnum) {
		List<HistoricoSituacaoVO> historicoSituacaoVOs = new ArrayList<>();
		try {
			if (Uteis.isAtributoPreenchido(getDataFinal())) {
				if ( !UteisData.validarSeDataInicioMenorOuIgualQueDataFinalDesconsiderandoDias(getDataInicio(), getDataFinal())) {
					throw new Exception("Data Final não pode ser menor que a Data de Inicío.");
				}
			}

			historicoSituacaoVOs = getFacadeFactory().getHistoricoSituacaoInterfaceFacade().consultaHistoricoSituacao(
					getDataInicio(), getDataFinal(), getFuncionarioCargoVO(), getSituacao() );

			if (!historicoSituacaoVOs.isEmpty()) {

				List<FuncionarioCargoRelVO> listaObjetos = montarDadosFuncionarioCargoVO(historicoSituacaoVOs);

				getSuperParametroRelVO().setNomeDesignIreport(CAMINHO_BASE_RH + "RelatorioHistoricoSituacao" + ".jrxml");
				getSuperParametroRelVO().setTipoRelatorioEnum(tipoRelatorioEnum);
				getSuperParametroRelVO().setSubReport_Dir(CAMINHO_BASE_RH);
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório de Históricos dos Funcionários");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setCaminhoBaseRelatorio(CAMINHO_BASE_RH);
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");

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
			Uteis.liberarListaMemoria(historicoSituacaoVOs);
		}
	}

	private List<FuncionarioCargoRelVO> montarDadosFuncionarioCargoVO(List<HistoricoSituacaoVO> historicoSituacaoVOs) {
		List<FuncionarioCargoRelVO> listaObjetos = new ArrayList<>();
		for (HistoricoSituacaoVO historicoSituacao : historicoSituacaoVOs) {
			FuncionarioCargoRelVO obj = new FuncionarioCargoRelVO();
			obj.setMatriculaCargo(historicoSituacao.getFuncionarioCargo().getMatriculaCargo());
			obj.setNomeFuncionario(historicoSituacao.getFuncionarioCargo().getFuncionarioVO().getPessoa().getNome());
			obj.setDataMudanca(UteisData.getDataFormatada(historicoSituacao.getDataMudanca()));

			if (Uteis.isAtributoPreenchido(historicoSituacao.getSituacao())) {
				obj.setSituacao(historicoSituacao.getSituacao().getValorApresentar());
			}
			
			if (Uteis.isAtributoPreenchido(historicoSituacao.getMotivoMudanca())) {
				obj.setMotivoMudanca(historicoSituacao.getMotivoMudanca().getValorApresentar());
			}
			
			listaObjetos.add(obj);
		}
		return listaObjetos;
	}
	
	/**
	 * Consulta responsavel por retornar os usuarios do popup de pesquisa 
	 * do funcionario
	 */
	public void consultarFuncionario() {
		try {
			List<FuncionarioCargoVO> objs = new ArrayList<>(0);

			if (getValorConsultaFuncionario().equals("")) {
				setMensagemID("msg_entre_prmconsulta");
				setListaConsulta(new ArrayList<FuncionarioCargoVO>(0));
				return;
			}
			if (getCampoConsultaFuncionario().equals("nome")) {
				objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeFuncionario(getControleConsultaOtimizado(), getValorConsultaFuncionario(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			if (getCampoConsultaFuncionario().equals("matricula")) {
				objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getValorConsultaFuncionario(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			setFuncionarioCargoVOs(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void consultarFuncionarioPorMatricula() {
		try {
			if (Uteis.isAtributoPreenchido(getFuncionarioCargoVO().getMatriculaCargo())) {
				FuncionarioCargoVO funcionarioCargo = getFacadeFactory().getFuncionarioCargoFacade().consultarPorMatriculaCargo(getFuncionarioCargoVO().getMatriculaCargo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				setFuncionarioCargoVO(funcionarioCargo);

				setMensagemID("msg_entre_dados");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	/**
	 * Seleciona o funcionario cargo pesquisado.
	 */
	public void selecionarFuncionarioCargo() {
		FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioCargoItem");
		setFuncionarioCargoVO(obj);
		
		valorConsultaFuncionario = "";
		campoConsultaFuncionario = "";
		getFuncionarioCargoVOs().clear();
	}


	public void limparDadosFuncionario () {
		setFuncionarioCargoVO(new FuncionarioCargoVO());
		getFuncionarioCargoVO().setMatriculaCargo("");
	}

	/**
	 * Monta a combobox da situacao do funcionario cargo.
	 */
	private void montarListaSelectItemSituacaoFuncionario() {
		setListaSelectItemSituacaoFuncionario(UtilPropriedadesDoEnum.getListaSelectItemDoEnum(SituacaoFuncionarioEnum.class, false));
	}

	public List<SelectItem> getTipoConsultaComboFuncionario() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("matricula", "Matrícula"));
		return itens;
	}
	
	/**
	 * Monta a combo da situacao do funcionário cargo
	 * 
	 * @return
	 */
	public List<SelectItem> getSituacaoFuncionarioCargo() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem("", ""));
		SituacaoFuncionarioEnum[] values = SituacaoFuncionarioEnum.values();
		for (SituacaoFuncionarioEnum situacaoFuncionarioEnum : values) {
			itens.add(new SelectItem(situacaoFuncionarioEnum.getValor(), situacaoFuncionarioEnum.getDescricao()));
		}
		return itens;
	}
	
	public boolean getApresentarResultadoConsultaFuncionarioCargo() {
		return getFuncionarioCargoVOs().size() > 0;
	}

	// GETTER AND SETTER
	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public String getSituacao() {
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public FuncionarioCargoVO getFuncionarioCargoVO() {
		if (funcionarioCargoVO == null) {
			funcionarioCargoVO = new FuncionarioCargoVO();
		}
		return funcionarioCargoVO;
	}

	public void setFuncionarioCargoVO(FuncionarioCargoVO funcionarioCargoVO) {
		this.funcionarioCargoVO = funcionarioCargoVO;
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
	
	public String getValorConsultaFuncionario() {
		if (valorConsultaFuncionario == null) {
			valorConsultaFuncionario = "";
		}
		return valorConsultaFuncionario;
	}

	public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
		this.valorConsultaFuncionario = valorConsultaFuncionario;
	}
	
	public List<FuncionarioCargoVO> getFuncionarioCargoVOs() {
		if (funcionarioCargoVOs == null) {
			funcionarioCargoVOs = new ArrayList<>();
		}
		return funcionarioCargoVOs;
	}

	public void setFuncionarioCargoVOs(List<FuncionarioCargoVO> funcionarioCargoVOs) {
		this.funcionarioCargoVOs = funcionarioCargoVOs;
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
