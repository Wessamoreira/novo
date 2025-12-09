package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.AdiantamentoRelVO;
import relatorio.negocio.jdbc.financeiro.AdiantamentoRel;

@SuppressWarnings({ "serial" , "deprecation"})
@Controller("AdiantamentoRelControle")
@Scope("viewScope")
@Lazy
public class AdiantamentoRelControle extends SuperControleRelatorio {	

	private Date dataInicio;
	private Date dataFim;
	private String tipoSacado;
	private String situacaoContaPagar;
	private String situacaoAdiantamento;
	
	public AdiantamentoRelControle() {	
		setTipoSacado("FO");
		setMensagemID("msg_entre_prmrelatorio");
	}
	
	public Date getDataInicio() {
        if (dataInicio == null) {
            dataInicio = Uteis.getDataPrimeiroDiaMes(new Date());
        }
		return dataInicio;
	}
	
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
        if (dataFim == null) {
            dataFim = Uteis.getDataUltimoDiaMes(new Date());
        }
		return dataFim;
	}
	
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}
	
	public String getTipoSacado() {
		if (tipoSacado == null) {
			tipoSacado = "Todos";
		}
		return tipoSacado;
	}

	public void setTipoSacado(String tipoSacado) {
		this.tipoSacado = tipoSacado;
	}
	
	public String getSituacaoContaPagar() {
		if (situacaoContaPagar == null) {
			situacaoContaPagar = "todas";
		}
		return situacaoContaPagar;
	}

	public void setSituacaoContaPagar(String situacaoContaPagar) {
		this.situacaoContaPagar = situacaoContaPagar;
	}
	
	public String getSituacaoAdiantamento() {
		if (situacaoAdiantamento == null) {
			situacaoAdiantamento = "todas";
		}
		return situacaoAdiantamento;
	}

	public void setSituacaoAdiantamento(String situacaoAdiantamento) {
		this.situacaoAdiantamento = situacaoAdiantamento;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (unidadeEnsinoVO == null) {
			if (getUnidadeEnsinoLogado().getCodigo() > 0) {
				unidadeEnsinoVO = getUnidadeEnsinoLogado();
			} else {
				unidadeEnsinoVO = new UnidadeEnsinoVO();
			}
		}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}
		
	public void imprimirPDF() {
		List<AdiantamentoRelVO> listaObjetos = null;	
		try {

			registrarAtividadeUsuario(getUsuarioLogado(), "AdiantamentoRelControle", "Inicializando Geração de Relatório Adiantamento" + this.getUnidadeEnsinoVO().getNome() + " - " + getUsuarioLogado().getCodigo() + " - " + getUsuarioLogado().getPerfilAcesso().getCodigo(), "Emitindo Relatório");
			getFacadeFactory().getAdiantamentoRelFacade().validarDados(getDataInicio(), getDataFim());	
			
			listaObjetos = getFacadeFactory().getAdiantamentoRelFacade().criarObjeto(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getDataInicio(), getDataFim(), getTipoSacado(), getSituacaoContaPagar(), getSituacaoAdiantamento(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAdiantamentoRelFacade().designIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(AdiantamentoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório Adiantamento");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getAdiantamentoRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				
				getSuperParametroRelVO().adicionarParametro("dataInicio", getDataInicio());
				getSuperParametroRelVO().adicionarParametro("dataFim", getDataFim());
				getSuperParametroRelVO().adicionarParametro("tipoSacado", getTipoSacado());
				getSuperParametroRelVO().adicionarParametro("situacaoContaPagar", getSituacaoContaPagar());
				getSuperParametroRelVO().adicionarParametro("situacaoAdiantamento", getSituacaoAdiantamento());
				
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
	            limparUnidadeEnsino();
	            setTipoSacado("FO");
	            registrarAtividadeUsuario(getUsuarioLogado(), "AdiantamentoRel", "Finalizando Geração de Relatório Adiantamento", "Emitindo Relatório");	            
	            //removerObjetoMemoria(this);               
	            
			} else {
				setMensagemID("msg_relatorio_sem_dados");
				setFazerDownload(false);
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);	
		}
	}

	public void imprimirRelatorioExcel() {
		List<AdiantamentoRelVO> listaObjetos = null;		
		try {

			registrarAtividadeUsuario(getUsuarioLogado(), "AdiantamentoRelControle", "Inicializando Geração de Relatório Adiantamento" + this.getUnidadeEnsinoVO().getNome() + " - " + getUsuarioLogado().getCodigo() + " - " + getUsuarioLogado().getPerfilAcesso().getCodigo(), "Emitindo Relatório");
			getFacadeFactory().getAdiantamentoRelFacade().validarDados(getDataInicio(), getDataFim());
						
			listaObjetos = getFacadeFactory().getAdiantamentoRelFacade().criarObjeto(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getDataInicio(), getDataFim(), getTipoSacado(), getSituacaoContaPagar(), getSituacaoAdiantamento(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getAdiantamentoRelFacade().designIReportRelatorioExcel());
		        getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
		        getSuperParametroRelVO().setSubReport_Dir(AdiantamentoRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório Adiantamento");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getAdiantamentoRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");

				getSuperParametroRelVO().adicionarParametro("dataInicio", getDataInicio());
				getSuperParametroRelVO().adicionarParametro("dataFim", getDataFim());
				getSuperParametroRelVO().adicionarParametro("tipoSacado", getTipoSacado());
				getSuperParametroRelVO().adicionarParametro("situacaoContaPagar", getSituacaoContaPagar());
				getSuperParametroRelVO().adicionarParametro("situacaoAdiantamento", getSituacaoAdiantamento());

				realizarImpressaoRelatorio();
				//removerObjetoMemoria(this);				
				setMensagemID("msg_relatorio_ok");
	            limparUnidadeEnsino();
	            setTipoSacado("FO");

	            registrarAtividadeUsuario(getUsuarioLogado(), "AdiantamentoRel", "Finalizando Geração de Relatório Adiantamento", "Emitindo Relatório");	
			} else {
				setMensagemID("msg_relatorio_sem_dados");
				setFazerDownload(false);
			} 			
		
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			Uteis.liberarListaMemoria(listaObjetos);
		}
	}

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("AdiantamentoRel");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarTodasUnidadesSelecionadas() {
		StringBuilder unidade = new StringBuilder();
		if (getUnidadeEnsinoVOs().size() > 1) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					unidade.append(obj.getNome()).append("; ");
				} 
			}
			getUnidadeEnsinoVO().setNome(unidade.toString());
		} else {
			if (!getUnidadeEnsinoVOs().isEmpty()) {
				if (getUnidadeEnsinoVOs().get(0).getFiltrarUnidadeEnsino()) {
					getUnidadeEnsinoVO().setNome(getUnidadeEnsinoVOs().get(0).getNome());
				}
			} else {
				getUnidadeEnsinoVO().setNome(unidade.toString());
			}
		}
		
	}

	public void marcarTodasUnidadesEnsinoAction() {
		for (UnidadeEnsinoVO unidade : getUnidadeEnsinoVOs()) {
			if (getMarcarTodasUnidadeEnsino()) {
				unidade.setFiltrarUnidadeEnsino(Boolean.TRUE);
			} else {
				unidade.setFiltrarUnidadeEnsino(Boolean.FALSE);
			}
		}
		verificarTodasUnidadesSelecionadas();
	}

	public List<UnidadeEnsinoVO> obterListaUnidadeEnsinoSelecionada(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		List<UnidadeEnsinoVO> objs = new ArrayList<UnidadeEnsinoVO>(0);
		unidadeEnsinoVOs.forEach(obj->{
			if (obj.getFiltrarUnidadeEnsino()) {
				objs.add(obj);
			}
		});
		return objs;
	}
	
	public List<SelectItem> getListaSelectItemTipoSacado() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("todos", "Todos"));
		itens.add(new SelectItem("FO", "Fornecedor"));
		itens.add(new SelectItem("FU", "Funcionário"));
		itens.add(new SelectItem("PA", "Parceiro"));
		return itens;
	}
	
	public List<SelectItem> getListaSelectItemSituacaoContaPagar() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("todas", "Todas"));
		itens.add(new SelectItem("AP", "A Pagar"));
		itens.add(new SelectItem("PA", "Pago"));
		itens.add(new SelectItem("NE", "Negociado"));
		return itens;
	}
		
	public List<SelectItem> getListaSelectItemSituacaoAdiantamento() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("todas", "Todas"));
		itens.add(new SelectItem("NU", "Não Utilizado"));
		itens.add(new SelectItem("PU", "Parcialmente Utilizado"));
		itens.add(new SelectItem("UT", "Utilizado"));
		return itens;
	}

	public void limparUnidadeEnsino(){
		super.limparUnidadeEnsinos();
	}
	
}