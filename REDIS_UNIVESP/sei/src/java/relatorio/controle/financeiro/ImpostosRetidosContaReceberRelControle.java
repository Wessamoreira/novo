package relatorio.controle.financeiro;

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
import negocio.comuns.faturamento.nfe.ImpostoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.ImpostosRetidosContaReceberRelVO;
import relatorio.negocio.jdbc.financeiro.ImpostosRetidosContaReceberRel;

@SuppressWarnings({ "serial" , "deprecation"})
@Controller("ImpostosRetidosContaReceberRelControle")
@Scope("viewScope")
@Lazy
public class ImpostosRetidosContaReceberRelControle extends SuperControleRelatorio {	

	private String campoConsultaParceiro;
	private String valorConsultaParceiro;	
	private List<ParceiroVO> listaConsultaParceiro;
	private ParceiroVO parceiro;
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
	private String situacaoContaReceber;
	private String layout;
	private List<ImpostoVO> listaResumoPorImpostos;
	
	public ImpostosRetidosContaReceberRelControle() {		
		setMensagemID("msg_entre_prmrelatorio");
	}
	
	public String getCampoConsultaParceiro() {
		if (campoConsultaParceiro == null) {
			campoConsultaParceiro = "";
		}
		return campoConsultaParceiro;
	}

	public void setCampoConsultaParceiro(String campoConsultaParceiro) {
		this.campoConsultaParceiro = campoConsultaParceiro;
	}

	public String getValorConsultaParceiro() {
		if (valorConsultaParceiro == null) {
			valorConsultaParceiro = "";
		}
		return valorConsultaParceiro;
	}

	public void setValorConsultaParceiro(String valorConsultaParceiro) {
		this.valorConsultaParceiro = valorConsultaParceiro;
	}
	
	public List<ParceiroVO> getListaConsultaParceiro() {
		if (listaConsultaParceiro == null) {
			listaConsultaParceiro = new ArrayList<ParceiroVO>(0);
		}
		return listaConsultaParceiro;
	}
	
	public void setListaConsultaParceiro(List<ParceiroVO> listaConsultaParceiro) {
		this.listaConsultaParceiro = listaConsultaParceiro;
	}

	public ParceiroVO getParceiro() {
		if (parceiro == null) {
			parceiro = new ParceiroVO();
		}
		return parceiro;
	}

	public void setParceiro(ParceiroVO parceiro) {
		this.parceiro = parceiro;
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
	
	public String getSituacaoContaReceber() {
		if (situacaoContaReceber == null) {
			situacaoContaReceber = "ambas";
		}
		return situacaoContaReceber;
	}

	public void setSituacaoContaReceber(String situacaoContaReceber) {
		this.situacaoContaReceber = situacaoContaReceber;
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
		List<ImpostosRetidosContaReceberRelVO> listaObjetos = null;	
		try {

			registrarAtividadeUsuario(getUsuarioLogado(), "ImpostosRetidosContaReceberRelControle", "Inicializando Geração de Relatório Impostos Retidos Conta Receber" + this.getUnidadeEnsinoVO().getNome() + " - " + getUsuarioLogado().getCodigo() + " - " + getUsuarioLogado().getPerfilAcesso().getCodigo(), "Emitindo Relatório");
			getFacadeFactory().getImpostosRetidosContaReceberRelFacade().validarDados(getDataInicio(), getDataFim());	
			setListaResumoPorImpostos(getFacadeFactory().getImpostosRetidosContaReceberRelFacade().criarListaImposto(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getParceiro(), getEstado(), getCidade(), obterListaImpostoSelecionado(), getDataInicio(), getDataFim(), getSituacaoContaReceber()));			
					
			listaObjetos = getFacadeFactory().getImpostosRetidosContaReceberRelFacade().criarObjeto(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getParceiro(), getEstado(), getCidade(), obterListaImpostoSelecionado(), getDataInicio(), getDataFim(), getSituacaoContaReceber(), getLayout(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getImpostosRetidosContaReceberRelFacade().designIReportRelatorio(getLayout()));
				getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
				getSuperParametroRelVO().setSubReport_Dir(ImpostosRetidosContaReceberRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório Impostos Retidos Conta Receber");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getImpostosRetidosContaReceberRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
				getSuperParametroRelVO().adicionarParametro("parceiro", getParceiro().getNome());
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
				getSuperParametroRelVO().adicionarParametro("situacaoContaReceber", getSituacaoContaReceber());
				getSuperParametroRelVO().adicionarParametro("listaResumoPorImpostos", getListaResumoPorImpostos());
				
				realizarImpressaoRelatorio();
				setMensagemID("msg_relatorio_ok");
	            limparUnidadeEnsino();
	            limparImpostos();
	            limparCidade();
	            limparParceiro();
	            limparEstado();   
	            setSituacaoContaReceber("ambas");
	            registrarAtividadeUsuario(getUsuarioLogado(), "ImpostosRetidosContaReceberRel", "Finalizando Geração de Relatório Impostos Retidos Conta Receber", "Emitindo Relatório");	            
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
		List<ImpostosRetidosContaReceberRelVO> listaObjetos = null;		
		try {

			registrarAtividadeUsuario(getUsuarioLogado(), "ImpostosRetidosContaReceberRelControle", "Inicializando Geração de Relatório Impostos Retidos Conta Receber" + this.getUnidadeEnsinoVO().getNome() + " - " + getUsuarioLogado().getCodigo() + " - " + getUsuarioLogado().getPerfilAcesso().getCodigo(), "Emitindo Relatório");
			getFacadeFactory().getImpostosRetidosContaReceberRelFacade().validarDados(getDataInicio(), getDataFim());
			setListaResumoPorImpostos(getFacadeFactory().getImpostosRetidosContaReceberRelFacade().criarListaImposto(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getParceiro(), getEstado(), getCidade(), obterListaImpostoSelecionado(), getDataInicio(), getDataFim(), getSituacaoContaReceber()));
						
			listaObjetos = getFacadeFactory().getImpostosRetidosContaReceberRelFacade().criarObjeto(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getParceiro(), getEstado(), getCidade(), obterListaImpostoSelecionado(), getDataInicio(), getDataFim(), getSituacaoContaReceber(), getLayout(), getUsuarioLogado());
			if (!listaObjetos.isEmpty()) {
				getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getImpostosRetidosContaReceberRelFacade().designIReportRelatorioExcel(getLayout()));
		        getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
		        getSuperParametroRelVO().setSubReport_Dir(ImpostosRetidosContaReceberRel.getCaminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
				getSuperParametroRelVO().setTituloRelatorio("Relatório Impostos Retidos Conta Receber");
				getSuperParametroRelVO().setListaObjetos(listaObjetos);
				getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getImpostosRetidosContaReceberRelFacade().caminhoBaseRelatorio());
				getSuperParametroRelVO().setNomeEmpresa("");
				getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
				getSuperParametroRelVO().setFiltros("");
				getSuperParametroRelVO().adicionarParametro("parceiro", getParceiro().getNome());
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
				getSuperParametroRelVO().adicionarParametro("situacaoContaReceber", getSituacaoContaReceber());
				getSuperParametroRelVO().adicionarParametro("listaResumoPorImpostos", getListaResumoPorImpostos());

				realizarImpressaoRelatorio();
				//removerObjetoMemoria(this);				
				setMensagemID("msg_relatorio_ok");
	            limparUnidadeEnsino();
	            limparImpostos();
	            limparCidade();
	            limparParceiro();
	            limparEstado();   
	            setSituacaoContaReceber("ambas");
	            registrarAtividadeUsuario(getUsuarioLogado(), "ImpostosRetidosContaReceberRel", "Finalizando Geração de Relatório Impostos Retidos Conta Receber", "Emitindo Relatório");	
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

    public void consultarParceiro() {
        try {
            List<ParceiroVO> objs = new ArrayList<ParceiroVO>(0);
            if (getCampoConsultaParceiro().equals("nome")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiro().equals("razaoSocial")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiro().equals("RG")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorRG(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiro().equals("CPF")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorCPF(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiro().equals("CNPJ")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorCNPJ(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiro().equals("tipoParceiro")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorTipoParceiro(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaParceiro(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaParceiro(new ArrayList<ParceiroVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

	public void selecionarParceiro() {
		try {
			ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
			setParceiro(obj);
			getListaConsultaParceiro().clear();
			this.setValorConsultaParceiro("");
			this.setCampoConsultaParceiro("");
			setMensagemID("", "");
		} catch (Exception e) {
		}
	}

	public void limparParceiro()  {
		try {			
			setParceiro(null);			
		} catch (Exception e) {
		}
	}
	
    /**
     * Rotina responsável por preencher a combo de consulta da tela Consultar Parceiro.
     */
	public List<SelectItem> getTipoConsultaComboParceiro() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("razaoSocial", "Razão Social"));
		itens.add(new SelectItem("RG", "RG"));
		itens.add(new SelectItem("CPF", "CPF"));
		itens.add(new SelectItem("CNPJ", "CNPJ"));
		itens.add(new SelectItem("tipoParceiro", "Tipo Parceiro"));
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
			consultarUnidadeEnsinoFiltroRelatorio("ImpostosRetidosContaReceberRel");
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
				objs = getFacadeFactory().getCidadeFacade().consultarPorNomeCodigoEstado(getValorConsultaCidade(), false, getEstado().getCodigo(),  getUsuarioLogado());
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
	
	public List<SelectItem> getListaSelectItemSituacaoContaReceber() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("ambas", "Ambas"));
		itens.add(new SelectItem("aReceber", "A Receber"));
		itens.add(new SelectItem("recebido", "Recebido"));
		return itens;
	}
	
	public List<SelectItem> getListaSelectItemLayout() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("sinteticoPorCidade", "Sintético Por Cidade"));
		itens.add(new SelectItem("analitico", "Analítico"));
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