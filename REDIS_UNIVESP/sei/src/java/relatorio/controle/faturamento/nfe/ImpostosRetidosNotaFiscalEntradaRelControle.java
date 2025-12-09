package relatorio.controle.faturamento.nfe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.faturamento.nfe.ImpostosRetidosNotaFiscalEntradaRelVO;
import relatorio.negocio.jdbc.faturamento.nfe.ImpostosRetidosNotaFiscalEntradaRel;

@SuppressWarnings({ "serial" , "deprecation"})
@Controller("ImpostosRetidosNotaFiscalEntradaRelControle")
@Scope("viewScope")
@Lazy
public class ImpostosRetidosNotaFiscalEntradaRelControle extends SuperControleRelatorio {	

	private String campoConsultaFornecedor;
	private String valorConsultaFornecedor;	
	private List<FornecedorVO> listaConsultaFornecedor;
	private FornecedorVO fornecedor;
	private ImpostoVO impostoVO;
	private List<ImpostoVO> listaImpostos;
	private Boolean marcarTodosImpostos;
	private String campoConsultaCidade;
	private String valorConsultaCidade;
	private List listaConsultaCidade;
	private CidadeVO cidade;
	private EstadoVO estado;
	protected List<SelectItem> listaSelectItemEstado;
	private Date dataInicio;
	private Date dataFim;
	private Date dataEmissaoInicio;
	private Date dataEmissaoFim;
	private Date dataVencimentoInicio;
	private Date dataVencimentoFim;
	private String layout;
	private List<ImpostoVO> listaResumoPorImpostos;
	private List<ImpostosRetidosNotaFiscalEntradaRelVO> listaResumoImpostosPorUnidadeEnsino;
	private List<ImpostosRetidosNotaFiscalEntradaRelVO> listaResumoImpostosPorFornecedor;
	
	public ImpostosRetidosNotaFiscalEntradaRelControle() {		
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

	public ImpostoVO getImpostoVO() {
		if (impostoVO == null) {
			impostoVO = new ImpostoVO();
		}
		return impostoVO;
	}

	public void setImpostoVO(ImpostoVO impostoVO) {
		this.impostoVO = impostoVO;
	}
	
	public List<ImpostoVO> getListaImpostos() {
		if (listaImpostos == null || listaImpostos.isEmpty()) {
			listaImpostos = new ArrayList<ImpostoVO>(0);
		}
		return listaImpostos;
	}

	public void setListaImpostos(List<ImpostoVO> listaImpostos) {
		this.listaImpostos = listaImpostos;
	}
	
	public Boolean getMarcarTodosImpostos() {
		if (marcarTodosImpostos == null) {
			marcarTodosImpostos = Boolean.FALSE;
		}
		return marcarTodosImpostos;
	}

	public void setMarcarTodosImpostos(Boolean marcarTodosImpostos) {
		this.marcarTodosImpostos = marcarTodosImpostos;
	}
	
	/**
	 * @return the campoConsultaCidade
	 */
	public String getCampoConsultaCidade() {
		if (campoConsultaCidade == null) {
			campoConsultaCidade = "";
		}
		return campoConsultaCidade;
	}

	/**
	 * @param campoConsultaCidade
	 *            the campoConsultaCidade to set
	 */
	public void setCampoConsultaCidade(String campoConsultaCidade) {
		this.campoConsultaCidade = campoConsultaCidade;
	}

	/**
	 * @return the valorConsultaCidade
	 */
	public String getValorConsultaCidade() {
		if (valorConsultaCidade == null) {
			valorConsultaCidade = "";
		}
		return valorConsultaCidade;
	}

	/**
	 * @param valorConsultaCidade
	 *            the valorConsultaCidade to set
	 */
	public void setValorConsultaCidade(String valorConsultaCidade) {
		this.valorConsultaCidade = valorConsultaCidade;
	}

	/**
	 * @return the listaConsultaCidade
	 */
	public List getListaConsultaCidade() {
		if (listaConsultaCidade == null) {
			listaConsultaCidade = new ArrayList<CidadeVO>(0);
		}
		return listaConsultaCidade;
	}

	/**
	 * @param listaConsultaCidade
	 *            the listaConsultaCidade to set
	 */
	public void setListaConsultaCidade(List listaConsultaCidade) {
		this.listaConsultaCidade = listaConsultaCidade;
	}
	
	public CidadeVO getCidade() {
		if (cidade == null) {
			cidade = new CidadeVO();
		}
		return cidade;
	}

	public void setCidade(CidadeVO cidade) {
		this.cidade = cidade;
	}
	
	public EstadoVO getEstado() {
		if (estado == null) {
			estado = new EstadoVO();
		}
		return estado;
	}

	public void setEstado(EstadoVO estado) {
		this.estado = estado;
	}
	
    public List<SelectItem> getListaSelectItemEstado() {
        if (listaSelectItemEstado == null) {
        	listaSelectItemEstado = new ArrayList(0);
        }
        return listaSelectItemEstado;
    }

    public void setListaSelectItemEstado(List<SelectItem> listaSelectItemEstado) {
        this.listaSelectItemEstado = listaSelectItemEstado;
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
	
	public Date getDataEmissaoInicio() {
		return dataEmissaoInicio;
	}

	public void setDataEmissaoInicio(Date dataEmissaoInicio) {
		this.dataEmissaoInicio = dataEmissaoInicio;
	}

	public Date getDataEmissaoFim() {
		return dataEmissaoFim;
	}

	public void setDataEmissaoFim(Date dataEmissaoFim) {
		this.dataEmissaoFim = dataEmissaoFim;
	}

	public Date getDataVencimentoInicio() {
		return dataVencimentoInicio;
	}

	public void setDataVencimentoInicio(Date dataVencimentoInicio) {
		this.dataVencimentoInicio = dataVencimentoInicio;
	}

	public Date getDataVencimentoFim() {
		return dataVencimentoFim;
	}

	public void setDataVencimentoFim(Date dataVencimentoFim) {
		this.dataVencimentoFim = dataVencimentoFim;
	}

	public String getLayout() {
		if (layout == null) {
			layout = "sinteticoPorCidade";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public List<ImpostoVO> getListaResumoPorImpostos() {
		return listaResumoPorImpostos;
	}

	public void setListaResumoPorImpostos(List<ImpostoVO> listaResumoPorImpostos) {
		this.listaResumoPorImpostos = listaResumoPorImpostos;
	} 
	
	public List<ImpostosRetidosNotaFiscalEntradaRelVO> getListaResumoImpostosPorUnidadeEnsino() {
		return listaResumoImpostosPorUnidadeEnsino;
	}

	public void setListaResumoImpostosPorUnidadeEnsino(
			List<ImpostosRetidosNotaFiscalEntradaRelVO> listaResumoImpostosPorUnidadeEnsino) {
		this.listaResumoImpostosPorUnidadeEnsino = listaResumoImpostosPorUnidadeEnsino;
	}

	public List<ImpostosRetidosNotaFiscalEntradaRelVO> getListaResumoImpostosPorFornecedor() {
		return listaResumoImpostosPorFornecedor;
	}

	public void setListaResumoImpostosPorFornecedor(
			List<ImpostosRetidosNotaFiscalEntradaRelVO> listaResumoImpostosPorFornecedor) {
		this.listaResumoImpostosPorFornecedor = listaResumoImpostosPorFornecedor;
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
		List<ImpostosRetidosNotaFiscalEntradaRelVO> listaObjetos = null;	
		try {

			registrarAtividadeUsuario(getUsuarioLogado(), "ImpostosRetidosNotaFiscalEntradaRelControle", "Inicializando Geração de Relatório Impostos Retidos Nota Fiscal Entrada" + this.getUnidadeEnsinoVO().getNome() + " - " + getUsuarioLogado().getCodigo() + " - " + getUsuarioLogado().getPerfilAcesso().getCodigo(), "Emitindo Relatório");
			getFacadeFactory().getImpostosRetidosNotaFiscalEntradaRelFacade().validarDados(getDataInicio(), getDataFim());	
			getFacadeFactory().getImpostosRetidosNotaFiscalEntradaRelFacade().validarData(getDataEmissaoInicio(), getDataEmissaoFim(), getDataVencimentoInicio(), getDataVencimentoFim());
			
			setListaResumoPorImpostos(getFacadeFactory().getImpostosRetidosNotaFiscalEntradaRelFacade().criarListaImposto(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getFornecedor(), getEstado(), getCidade(), obterListaImpostoSelecionado(), getDataInicio(), getDataFim(), getDataEmissaoInicio(),getDataEmissaoFim(), getDataVencimentoInicio(), getDataVencimentoFim()));			
			setListaResumoImpostosPorUnidadeEnsino(getFacadeFactory().getImpostosRetidosNotaFiscalEntradaRelFacade().criarListaImpostoPorUnidadeEnsino(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getFornecedor(), getEstado(), getCidade(), obterListaImpostoSelecionado(), getDataInicio(), getDataFim(),getDataEmissaoInicio(),getDataEmissaoFim(), getDataVencimentoInicio(), getDataVencimentoFim()));		
			setListaResumoImpostosPorFornecedor(getFacadeFactory().getImpostosRetidosNotaFiscalEntradaRelFacade().criarListaImpostoPorFornecedor(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getFornecedor(), getEstado(), getCidade(), obterListaImpostoSelecionado(), getDataInicio(), getDataFim(),getDataEmissaoInicio(),getDataEmissaoFim(), getDataVencimentoInicio(), getDataVencimentoFim()));	
			
			listaObjetos = getFacadeFactory().getImpostosRetidosNotaFiscalEntradaRelFacade().criarObjeto(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getFornecedor(), getEstado(), getCidade(), obterListaImpostoSelecionado(), getDataInicio(), getDataFim(),getDataEmissaoInicio(),getDataEmissaoFim(), getDataVencimentoInicio(), getDataVencimentoFim(), getLayout(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getImpostosRetidosNotaFiscalEntradaRelFacade().designIReportRelatorio(getLayout()));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(ImpostosRetidosNotaFiscalEntradaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório Impostos Retidos Nota Fiscal Entrada");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getImpostosRetidosNotaFiscalEntradaRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				if (getUnidadeEnsinoVO().getNome().length() > 300) {
					getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome().substring(0, 300) + "...");
				} else {
					getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				}
				getSuperParametroRelVO().adicionarParametro("fornecedor", getFornecedor().getNome());
				getSuperParametroRelVO().adicionarParametro("cidade", getCidade().getNome());
                if (getEstado().getCodigo().intValue() != 0) {
                    getSuperParametroRelVO().adicionarParametro("estado", getFacadeFactory().getEstadoFacade().consultarPorChavePrimaria(getEstado().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getSigla());
                }
                else {
                	getSuperParametroRelVO().adicionarParametro("estado", "Todos");
                }
				getSuperParametroRelVO().adicionarParametro("impostos", getImpostoVO().getNome());				
				getSuperParametroRelVO().adicionarParametro("dataInicio", getDataInicio());
				getSuperParametroRelVO().adicionarParametro("dataFim", getDataFim());
				getSuperParametroRelVO().adicionarParametro("dataEmissaoInicio", getDataEmissaoInicio());
				getSuperParametroRelVO().adicionarParametro("dataEmissaoFim", getDataEmissaoFim());
				getSuperParametroRelVO().adicionarParametro("dataVencimentoInicio", getDataVencimentoInicio());
				getSuperParametroRelVO().adicionarParametro("dataVencimentoFim", getDataVencimentoFim());
				getSuperParametroRelVO().adicionarParametro("listaResumoPorImpostos", getListaResumoPorImpostos());
				getSuperParametroRelVO().adicionarParametro("listaResumoImpostosPorUnidadeEnsino", getListaResumoImpostosPorUnidadeEnsino());
				getSuperParametroRelVO().adicionarParametro("listaResumoImpostosPorFornecedor", getListaResumoImpostosPorFornecedor());
				
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
//	            limparUnidadeEnsino();
//	            limparImpostos();
//	            limparCidade();
//	            limparFornecedor();
//	            limparEstado();   
	            registrarAtividadeUsuario(getUsuarioLogado(), "ImpostosRetidosNotaFiscalEntradaRel", "Finalizando Geração de Relatório Impostos Retidos Nota Fiscal Entrada", "Emitindo Relatório");	            
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
		List<ImpostosRetidosNotaFiscalEntradaRelVO> listaObjetos = null;		
		try {

			registrarAtividadeUsuario(getUsuarioLogado(), "ImpostosRetidosNotaFiscalEntradaRelControle", "Inicializando Geração de Relatório Impostos Retidos Nota Fiscal Entrada" + this.getUnidadeEnsinoVO().getNome() + " - " + getUsuarioLogado().getCodigo() + " - " + getUsuarioLogado().getPerfilAcesso().getCodigo(), "Emitindo Relatório");
			getFacadeFactory().getImpostosRetidosNotaFiscalEntradaRelFacade().validarDados(getDataInicio(), getDataFim());
			setListaResumoPorImpostos(getFacadeFactory().getImpostosRetidosNotaFiscalEntradaRelFacade().criarListaImposto(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getFornecedor(), getEstado(), getCidade(), obterListaImpostoSelecionado(), getDataInicio(), getDataFim(),getDataEmissaoInicio(),getDataEmissaoFim(), getDataVencimentoInicio(), getDataVencimentoFim()));
			setListaResumoImpostosPorUnidadeEnsino(getFacadeFactory().getImpostosRetidosNotaFiscalEntradaRelFacade().criarListaImpostoPorUnidadeEnsino(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getFornecedor(), getEstado(), getCidade(), obterListaImpostoSelecionado(), getDataInicio(), getDataFim(),getDataEmissaoInicio(),getDataEmissaoFim(), getDataVencimentoInicio(), getDataVencimentoFim()));	
			setListaResumoImpostosPorFornecedor(getFacadeFactory().getImpostosRetidosNotaFiscalEntradaRelFacade().criarListaImpostoPorFornecedor(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getFornecedor(), getEstado(), getCidade(), obterListaImpostoSelecionado(), getDataInicio(), getDataFim(),getDataEmissaoInicio(),getDataEmissaoFim(), getDataVencimentoInicio(), getDataVencimentoFim()));	
			
			listaObjetos = getFacadeFactory().getImpostosRetidosNotaFiscalEntradaRelFacade().criarObjeto(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getFornecedor(), getEstado(), getCidade(), obterListaImpostoSelecionado(), getDataInicio(), getDataFim(),getDataEmissaoInicio(),getDataEmissaoFim(), getDataVencimentoInicio(), getDataVencimentoFim(), getLayout(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getImpostosRetidosNotaFiscalEntradaRelFacade().designIReportRelatorioExcel(getLayout()));
		        getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
		        getSuperParametroRelVO().setSubReport_Dir(ImpostosRetidosNotaFiscalEntradaRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório Impostos Retidos Nota Fiscal Entrada");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getImpostosRetidosNotaFiscalEntradaRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				getSuperParametroRelVO().adicionarParametro("fornecedor", getFornecedor().getNome());
				getSuperParametroRelVO().adicionarParametro("cidade", getCidade().getNome());
                if (getEstado().getCodigo().intValue() != 0) {
                    getSuperParametroRelVO().adicionarParametro("estado", getFacadeFactory().getEstadoFacade().consultarPorChavePrimaria(getEstado().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getSigla());
                }
                else {
                	getSuperParametroRelVO().adicionarParametro("estado", "Todos");
                }
				getSuperParametroRelVO().adicionarParametro("impostos", getImpostoVO().getNome());
				getSuperParametroRelVO().adicionarParametro("dataInicio", getDataInicio());
				getSuperParametroRelVO().adicionarParametro("dataFim", getDataFim());
				getSuperParametroRelVO().adicionarParametro("listaResumoPorImpostos", getListaResumoPorImpostos());
				getSuperParametroRelVO().adicionarParametro("listaResumoImpostosPorUnidadeEnsino", getListaResumoImpostosPorUnidadeEnsino());
				getSuperParametroRelVO().adicionarParametro("listaResumoImpostosPorFornecedor", getListaResumoImpostosPorFornecedor());

				realizarImpressaoRelatorio();
				//removerObjetoMemoria(this);				
				setMensagemID("msg_relatorio_ok");
	            limparUnidadeEnsino();
	            limparImpostos();
	            limparCidade();
	            limparFornecedor();
	            limparEstado();   
	            registrarAtividadeUsuario(getUsuarioLogado(), "ImpostosRetidosNotaFiscalEntradaRel", "Finalizando Geração de Relatório Impostos Retidos Nota Fiscal Entrada", "Emitindo Relatório");	
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
		
    /* Método responsável por inicializar List<SelectItem> de valores do 
     * ComboBox correspondente ao atributo <code>estado</code>
     */
	@PostConstruct
    public void montarListaSelectItemEstado() throws Exception {
        try {
            List<EstadoVO> resultadoConsulta = getFacadeFactory().getEstadoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            setListaSelectItemEstado(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "sigla"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("ImpostosRetidosNotaFiscalEntradaRel");
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

	@PostConstruct
	public void consultarImposto() {
		try {
			montarListaSelectItemImposto("");
			verificarTodosImpostosSelecionados();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarTodosImpostosSelecionados() {
		StringBuilder imposto = new StringBuilder();
		if (getListaImpostos().size() > 1) {
			for (ImpostoVO obj : getListaImpostos()) {
				if (obj.getFiltrarImposto()) {
					imposto.append(obj.getNome()).append("; ");
				} 
			}
			getImpostoVO().setNome(imposto.toString());
		} else {
			if (!getListaImpostos().isEmpty()) {
				if (getListaImpostos().get(0).getFiltrarImposto()) {
					getImpostoVO().setNome(getListaImpostos().get(0).getNome());
				}
			} else {
				getImpostoVO().setNome(imposto.toString());
			}
		}
		
	}

	public void marcarTodosImpostosAction() {
		for (ImpostoVO imposto : getListaImpostos()) {
			if (getMarcarTodosImpostos()) {
				imposto.setFiltrarImposto(Boolean.TRUE);
			} else {
				imposto.setFiltrarImposto(Boolean.FALSE);
			}
		}
		verificarTodosImpostosSelecionados();
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
	
	
	public List<ImpostoVO> obterListaImpostoSelecionado() {
		List<ImpostoVO> objs = new ArrayList<ImpostoVO>(0);
		this.listaImpostos.forEach(obj->{
			if (obj.getFiltrarImposto()) {
				objs.add(obj);
			}
		});
		return objs;
	}
	
	public String consultarCidade() {
		try {
			getEstado().getCodigo();
			List objs = new ArrayList(0);
			if (getCampoConsultaCidade().equals("nome")) {
				objs = getFacadeFactory().getCidadeFacade().consultarPorNome(getValorConsultaCidade(), false, getUsuarioLogado());
			}
			setListaConsultaCidade(objs);
			setMensagemID("msg_dados_consultados");
			return "";

		} catch (Exception e) {
			setListaConsultaCidade(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return "";
		}
	}
	
	/**
	 * Método responsável por selecionar o objeto CidadeVO <code>Cidade/code>.
	 */	
	public void selecionarCidade() {
		try {
			CidadeVO obj = (CidadeVO) context().getExternalContext().getRequestMap().get("cidadeItens");
			setCidade(obj);
			setEstado(obj.getEstado());
			getListaConsultaCidade().clear();
			this.setValorConsultaCidade("");
			this.setCampoConsultaCidade("");
			setMensagemID("", "");
		} catch (Exception e) {
		}
	}

	public void limparCidade()  {
		try {			
			setCidade(null);			
		} catch (Exception e) {
		}
	}
	
	public void limparEstado()  {
		try {			
			setEstado(null);			
		} catch (Exception e) {
		}
	}
	
	public List<SelectItem> getListaSelectItemLayout() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("analitico", "Analítico"));
		itens.add(new SelectItem("sintetico", "Sintético"));
		return itens;
	}
	
	/**
	 * Método responsável por carregar umaCombobox com os tipos de pesquisa de Cidade <code>Cidade/code>.
	 */

	public List<SelectItem> getTipoConsultaCidade() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemImposto(String prm) throws Exception {
		List<ImpostoVO> resultadoConsulta = null;
		try {
			setControleConsultaOtimizado(new DataModelo(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getControleConsultaOtimizado().setLimitePorPagina(0);
			resultadoConsulta = getFacadeFactory().getImpostoFacade().consultaRapidaPorNome("", getControleConsultaOtimizado());
			getListaImpostos().clear();
			resultadoConsulta.stream().forEach(p->{
				getListaImpostos().add(p);
			});
			for (ImpostoVO obj : getListaImpostos()) {
				obj.setFiltrarImposto(true);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}
	
	public void limparImpostos(){
		setMarcarTodosImpostos(false);
		marcarTodosImpostosAction();
	}
	
	public void limparUnidadeEnsino(){
		super.limparUnidadeEnsinos();
	}
	
}