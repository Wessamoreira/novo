package relatorio.controle.compras;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.compras.CompraRelVO;
import relatorio.negocio.jdbc.compras.CompraRel;

@SuppressWarnings({ "serial" , "deprecation"})
@Controller("CompraRelControle")
@Scope("viewScope")
@Lazy
public class CompraRelControle extends SuperControleRelatorio {	

	private String campoConsultaFornecedor;
	private String valorConsultaFornecedor;	
	private List<FornecedorVO> listaConsultaFornecedor;
	private FornecedorVO fornecedor;
	private Integer numeroCompra;
	private Double valorCompraInicio;
	private Double valorCompraFim;
	private Date dataInicio;
	private Date dataFim;
	private String valorConsultaSituacaoEntregaRecebimento;
	private String valorConsultaSituacaoFinanceira;
	
	public CompraRelControle() {		
		setMensagemID("msg_entre_prmrelatorio");
	}
	
	public String getCampoConsultaFornecedor() {
		if (campoConsultaFornecedor == null) {
			campoConsultaFornecedor = "";
		}
		return campoConsultaFornecedor;
	}

	public void setCampoConsultaFornecedor(String campoConsultaFornecedor) {
		this.campoConsultaFornecedor = campoConsultaFornecedor;
	}

	public String getValorConsultaFornecedor() {
		if (valorConsultaFornecedor == null) {
			valorConsultaFornecedor = "";
		}
		return valorConsultaFornecedor;
	}

	public void setValorConsultaFornecedor(String valorConsultaFornecedor) {
		this.valorConsultaFornecedor = valorConsultaFornecedor;
	}
	
	public List<FornecedorVO> getListaConsultaFornecedor() {
		if (listaConsultaFornecedor == null) {
			listaConsultaFornecedor = new ArrayList<FornecedorVO>(0);
		}
		return listaConsultaFornecedor;
	}
	
	public void setListaConsultaFornecedor(List<FornecedorVO> listaConsultaFornecedor) {
		this.listaConsultaFornecedor = listaConsultaFornecedor;
	}

	public FornecedorVO getFornecedor() {
		if (fornecedor == null) {
			fornecedor = new FornecedorVO();
		}
		return fornecedor;
	}

	public void setFornecedor(FornecedorVO fornecedor) {
		this.fornecedor = fornecedor;
	}

	public Integer getNumeroCompra() {
		return numeroCompra;
	}

	public void setNumeroCompra(Integer numeroCompra) {
		this.numeroCompra = numeroCompra;
	}

	public Double getValorCompraInicio() {
		if(valorCompraInicio == null) {
			valorCompraInicio = 0.0;
		}
		return valorCompraInicio;
	}

	public void setValorCompraInicio(Double valorCompraInicio) {
		this.valorCompraInicio = valorCompraInicio;
	}

	public Double getValorCompraFim() {
		if(valorCompraFim == null) {
			valorCompraFim = 0.0;
		}
		return valorCompraFim;
	}

	public void setValorCompraFim(Double valorCompraFim) {
		this.valorCompraFim = valorCompraFim;
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

	/**
	 * @return the valorConsultaSituacaoAutorizacao
	 */
	public String getValorConsultaSituacaoFinanceira() {
		return valorConsultaSituacaoFinanceira;
	}

	/**
	 * @param valorConsultaSituacaoAutorizacao
	 *            the valorConsultaSituacaoAutorizacao to set
	 */
	public void setValorConsultaSituacaoFinanceira(String valorConsultaSituacaoFinanceira) {
		this.valorConsultaSituacaoFinanceira = valorConsultaSituacaoFinanceira;
	}
	
	public Date getDataInicio() {
		if (dataInicio == null) {
			dataInicio = Uteis.getNewDateComMesesAMenos(1);
		}
		return dataInicio;
	}
	
	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFim() {
		if (dataFim == null) {
			dataFim = new Date();
		}
		return dataFim;
	}
	
	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
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
		List<CompraRelVO> listaObjetos = null;	
		try {

			registrarAtividadeUsuario(getUsuarioLogado(), "CompraRelControle", "Inicializando Geração de Relatório Compra" + this.getUnidadeEnsinoVO().getNome() + " - " + getUsuarioLogado().getCodigo() + " - " + getUsuarioLogado().getPerfilAcesso().getCodigo(), "Emitindo Relatório");
			getFacadeFactory().getCompraRelFacade().validarDados(getFornecedor(), getDataInicio(), getDataFim(), getValorCompraInicio(), getValorCompraFim());	
			
			listaObjetos = getFacadeFactory().getCompraRelFacade().criarObjeto(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getFornecedor(), getNumeroCompra(), getValorCompraInicio(), getValorCompraFim(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoFinanceira(), getDataInicio(), getDataFim(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getCompraRelFacade().designIReportRelatorio());
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(CompraRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório Compra");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getCompraRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				getSuperParametroRelVO().adicionarParametro("fornecedor", getFornecedor().getNome());
				getSuperParametroRelVO().adicionarParametro("dataInicio", getDataInicio());
				getSuperParametroRelVO().adicionarParametro("dataFim", getDataFim());				
				getSuperParametroRelVO().adicionarParametro("situacaoRecebimento", getValorConsultaSituacaoEntregaRecebimento());
				getSuperParametroRelVO().adicionarParametro("situacaoFinanceira", getValorConsultaSituacaoFinanceira());
				getSuperParametroRelVO().adicionarParametro("numeroCompra", getNumeroCompra());
				getSuperParametroRelVO().adicionarParametro("valorCompraInicio", getValorCompraInicio());
				getSuperParametroRelVO().adicionarParametro("valorCompraFim", getValorCompraFim());
				
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
	            limparUnidadeEnsino();
	            limparFornecedor(); 
	            setValorConsultaSituacaoEntregaRecebimento("");
	            setValorConsultaSituacaoFinanceira("");
	            setNumeroCompra(null);
	            setValorCompraInicio(0.0);
	            setValorCompraFim(0.0);
	            registrarAtividadeUsuario(getUsuarioLogado(), "CompraRelControle", "Finalizando Geração de Relatório Compra", "Emitindo Relatório");	            
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
		List<CompraRelVO> listaObjetos = null;		
		try {

			registrarAtividadeUsuario(getUsuarioLogado(), "CompraRelControle", "Inicializando Geração de Relatório Compra" + this.getUnidadeEnsinoVO().getNome() + " - " + getUsuarioLogado().getCodigo() + " - " + getUsuarioLogado().getPerfilAcesso().getCodigo(), "Emitindo Relatório");
			getFacadeFactory().getCompraRelFacade().validarDados(getFornecedor(), getDataInicio(), getDataFim(), getValorCompraInicio(), getValorCompraFim());
			
			listaObjetos = getFacadeFactory().getCompraRelFacade().criarObjeto(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getFornecedor(), getNumeroCompra(), getValorCompraInicio(), getValorCompraFim(), getValorConsultaSituacaoEntregaRecebimento(), getValorConsultaSituacaoFinanceira(), getDataInicio(), getDataFim(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getCompraRelFacade().designIReportRelatorioExcel());
		        getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
		        getSuperParametroRelVO().setSubReport_Dir(CompraRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório Compra");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getCompraRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				getSuperParametroRelVO().adicionarParametro("fornecedor", getFornecedor().getNome());
				getSuperParametroRelVO().adicionarParametro("dataInicio", getDataInicio());
				getSuperParametroRelVO().adicionarParametro("dataFim", getDataFim());
				getSuperParametroRelVO().adicionarParametro("situacaoRecebimento", getValorConsultaSituacaoEntregaRecebimento());
				getSuperParametroRelVO().adicionarParametro("situacaoFinanceira", getValorConsultaSituacaoFinanceira());
				getSuperParametroRelVO().adicionarParametro("numeroCompra", getNumeroCompra());
				getSuperParametroRelVO().adicionarParametro("valorCompraInicio", getValorCompraInicio());
				getSuperParametroRelVO().adicionarParametro("valorCompraFim", getValorCompraFim());

				realizarImpressaoRelatorio();
				//removerObjetoMemoria(this);				
				setMensagemID("msg_relatorio_ok");
	            limparUnidadeEnsino();
	            limparFornecedor();

	            registrarAtividadeUsuario(getUsuarioLogado(), "CompraRelControle", "Finalizando Geração de Relatório Compra", "Emitindo Relatório");	
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

    public void consultarFornecedor() {
        try {
            List<FornecedorVO> objs = new ArrayList<FornecedorVO>(0);
            if (getCampoConsultaFornecedor().equals("nome")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("razaoSocial")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(), "", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("RG")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorRG(getValorConsultaFornecedor(), "", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("CPF")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("CNPJ")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("codigo")) {
                if (getCampoConsultaFornecedor().equals("")) {
                   setCampoConsultaFornecedor("0");
                }
                int valorInt = Integer.parseInt(getCampoConsultaFornecedor());
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCodigo(new Integer(valorInt), "", true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaFornecedor(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
        	setListaConsultaFornecedor(new ArrayList<FornecedorVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

	public void selecionarFornecedor() {
		try {
			FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
			setFornecedor(obj);
			getListaConsultaFornecedor().clear();
			this.setValorConsultaFornecedor("");
			this.setCampoConsultaFornecedor("");
			setMensagemID("", "");
		} catch (Exception e) {
		}
	}

	public void limparFornecedor()  {
		try {			
			setFornecedor(null);			
		} catch (Exception e) {
		}
	}
	
    /**
     * Rotina responsável por preencher a combo de consulta da tela Consultar Fornecedor.
     */
	public List<SelectItem> getTipoConsultaComboFornecedor() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("RG", "RG"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("CNPJ", "CNPJ"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}
		
	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("CompraRel");
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
	
	public void limparUnidadeEnsino(){
		super.limparUnidadeEnsinos();
	}
	
	public List getListaSelectItemSituacaoEntregaRecebimento() throws Exception {
		List objs = new ArrayList(0);
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
	
	public List getListaSelectItemSituacaoFinanceira() throws Exception {
		List objs = new ArrayList(0);
		Hashtable situacaoFinanceira = (Hashtable) Dominios.getSituacaoFinanceira();
		Enumeration keys = situacaoFinanceira.keys();
		objs.add(new SelectItem("", ""));
		while (keys.hasMoreElements()) {
			String value = (String) keys.nextElement();
			String label = (String) situacaoFinanceira.get(value);
			objs.add(new SelectItem(value, label));
		}
		SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
		Collections.sort(objs, ordenador);
		return objs;
	}
	
}