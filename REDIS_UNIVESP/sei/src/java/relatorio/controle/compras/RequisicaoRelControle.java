package relatorio.controle.compras;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.CategoriaProdutoVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.RequisicaoVO;
import negocio.comuns.compras.enumeradores.TipoAutorizacaoRequisicaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.utilitarias.Uteis;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;

/**
 *
 * @author Philippe
 */
@Controller("RequisicaoRelControle")
@Scope("viewScope")
@Lazy
public class RequisicaoRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = -3586821488043087671L;

	private Date dataInicio;
    private Date dataFim;
	private UnidadeEnsinoVO unidadeEnsinoVO;
    private CategoriaDespesaVO categoriaDespesaVO;
    private CategoriaProdutoVO categoriaProdutoVO;
    private DepartamentoVO departamentoVO;
    private ProdutoServicoVO produtoServicoVO;
    private String situacaoEntrega;
    private String campoConsultaProduto;
    private String valorConsultaProduto;
    private List listaConsultaProduto;
    private String campoConsultaCategoriaProduto;
    private String valorConsultaCategoriaProduto;
    private List listaConsultaCategoriaProduto;
    private String campoConsultaDepartamento;
    private String valorConsultaDepartamento;
    private List listaConsultaDepartamento;
    private List listaConsultaCategoriaDespesa;
    private String valorConsultaCategoriaDespesa;
    private String campoConsultaCategoriaDespesa;
    private List listaSelectItemUnidadeEnsino;
    private String layout;
    private Boolean desabilitarSituacaoEntrega;
    private RequisicaoVO requisicaoVO;
	private DataModelo requisitanteDataModelo;
	private DataModelo centroResultadoDataModelo;
	private CentroResultadoVO centroResultadoEstoqueVO;
	private List<CentroResultadoVO> listaCentroResultadoEstoque;
	private Boolean marcarTodosCentroResultadoEstoque;
	private Date dataInicioPeriodoConsumo;
    private Date dataFimPeriodoConsumo;

    public RequisicaoRelControle() {
        montarListaSelectItemUnidadeEnsino();
        if (getListaSelectItemUnidadeEnsino().size() == 1) {
            UnidadeEnsinoVO obj = (UnidadeEnsinoVO) getListaSelectItemUnidadeEnsino().get(0);
            setUnidadeEnsinoVO(obj);
        }
        setRequisicaoVO(new RequisicaoVO());
        getRequisicaoVO().setTipoAutorizacaoRequisicaoEnum(TipoAutorizacaoRequisicaoEnum.NENHUM);
		getRequisicaoVO().setSituacaoTipoAutorizacaoRequisicaoEnum("");
    
    }

    public void imprimirPDF() {
        List listaObjetos = null;
        try {
        	//getFacadeFactory().getRequisicaoRelFacade().validarDados(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), dataInicio, dataFim, getLayout());
        	if (getLayout().equals("RequisicaoRel")) {
        		listaObjetos = getFacadeFactory().getRequisicaoRelFacade().criarObjetoLayout1(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getDepartamentoVO(), getCategoriaDespesaVO(), getCategoriaProdutoVO(), getProdutoServicoVO(), getSituacaoEntrega(), getDataInicio(), getDataFim(), getRequisicaoVO());
        	} else if  (getLayout().equals("RequisicaoRel2")) {
        		listaObjetos = getFacadeFactory().getRequisicaoRelFacade().criarObjetoLayout2(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getDepartamentoVO(), getCategoriaDespesaVO(), getCategoriaProdutoVO(), getProdutoServicoVO(), getSituacaoEntrega(), getDataInicio(), getDataFim(), getRequisicaoVO());
        	}
        	else if  (getLayout().equals("RequisicaoRel3")){
        		listaObjetos = getFacadeFactory().getRequisicaoRelFacade().criarObjetoLayout3(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getDepartamentoVO(), getCategoriaDespesaVO(), getCategoriaProdutoVO(), getProdutoServicoVO(), getSituacaoEntrega(), getDataInicio(), getDataFim(), getRequisicaoVO());
        	}
        	else {
        		listaObjetos = getFacadeFactory().getRequisicaoRelFacade().criarObjetoLayout4(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getDepartamentoVO(), getCategoriaDespesaVO(), getCategoriaProdutoVO(), getProdutoServicoVO(), getSituacaoEntrega(), getRequisicaoVO(), obterListaCentroResultadoEstoqueSelecionado(), getDataInicioPeriodoConsumo(), getDataFimPeriodoConsumo());
        	}
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getRequisicaoRelFacade().designIReportRelatorio(getLayout()));
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getRequisicaoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Requisições");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getRequisicaoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                if (!getUnidadeEnsinoVO().getNome().equals("")) {
                	getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
                    //getSuperParametroRelVO().adicionarParametro("unidadeEnsino", getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
                }
                else {
                	getSuperParametroRelVO().adicionarParametro("unidadeEnsino", "Todos");
                }
				getSuperParametroRelVO().adicionarParametro("dataInicio", getDataInicio());
				getSuperParametroRelVO().adicionarParametro("dataFim", getDataFim());
				getSuperParametroRelVO().adicionarParametro("departamento", getDepartamentoVO().getNome());
				getSuperParametroRelVO().adicionarParametro("categoriaDespesa", getCategoriaDespesaVO().getDescricao());
				getSuperParametroRelVO().adicionarParametro("categoriaProduto", getCategoriaProdutoVO().getNome());
				getSuperParametroRelVO().adicionarParametro("produto", getProdutoServicoVO().getNome());
				getSuperParametroRelVO().adicionarParametro("requisitante", getRequisicaoVO().getSolicitanteRequisicao().getPessoa().getNome());
				getSuperParametroRelVO().adicionarParametro("centroResultado", getRequisicaoVO().getCentroResultadoAdministrativo().getDescricao());
				getSuperParametroRelVO().adicionarParametro("situacao", getSituacaoEntrega());
                if (!getCentroResultadoEstoqueVO().getDescricao().equals("")) {
				getSuperParametroRelVO().adicionarParametro("centroResultadoEstoque", getCentroResultadoEstoqueVO().getDescricao());
                }
                else {
                	getSuperParametroRelVO().adicionarParametro("centroResultadoEstoque", "Todos");
                }
				getSuperParametroRelVO().adicionarParametro("dataInicioPeriodoConsumo", getDataInicioPeriodoConsumo());
				getSuperParametroRelVO().adicionarParametro("dataFimPeriodoConsumo", getDataFimPeriodoConsumo());
                realizarImpressaoRelatorio();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }
    
    public void imprimirExcel() {
        List listaObjetos = null;
        try {
        	if (getLayout().equals("RequisicaoRel")) {
        		listaObjetos = getFacadeFactory().getRequisicaoRelFacade().criarObjetoLayout1(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getDepartamentoVO(), getCategoriaDespesaVO(), getCategoriaProdutoVO(), getProdutoServicoVO(), getSituacaoEntrega(), getDataInicio(), getDataFim(), getRequisicaoVO());
        	} else if (getLayout().equals("RequisicaoRel2")) {
        		listaObjetos = getFacadeFactory().getRequisicaoRelFacade().criarObjetoLayout2(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getDepartamentoVO(), getCategoriaDespesaVO(), getCategoriaProdutoVO(), getProdutoServicoVO(), getSituacaoEntrega(), getDataInicio(), getDataFim(), getRequisicaoVO());
        	}else if (getLayout().equals("RequisicaoRel3")) {
        		listaObjetos = getFacadeFactory().getRequisicaoRelFacade().criarObjetoLayout3(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getDepartamentoVO(), getCategoriaDespesaVO(), getCategoriaProdutoVO(), getProdutoServicoVO(), getSituacaoEntrega(), getDataInicio(), getDataFim(), getRequisicaoVO());
        	}
        	else {
        		listaObjetos = getFacadeFactory().getRequisicaoRelFacade().criarObjetoLayout4(obterListaUnidadeEnsinoSelecionada(getUnidadeEnsinoVOs()), getDepartamentoVO(), getCategoriaDespesaVO(), getCategoriaProdutoVO(), getProdutoServicoVO(), getSituacaoEntrega(), getRequisicaoVO(), obterListaCentroResultadoEstoqueSelecionado(), getDataInicioPeriodoConsumo(), getDataFimPeriodoConsumo());
        	}
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getRequisicaoRelFacade().designIReportRelatorioExcel(getLayout()));
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getRequisicaoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Requisições");
                getSuperParametroRelVO().setListaObjetos(listaObjetos);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getRequisicaoRelFacade().caminhoBaseRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                if (!getUnidadeEnsinoVO().getNome().equals("")) {
                	getSuperParametroRelVO().setUnidadeEnsino(getUnidadeEnsinoVO().getNome());
                    //getSuperParametroRelVO().adicionarParametro("unidadeEnsino", getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()).getNome());
                }
                else {
                	getSuperParametroRelVO().adicionarParametro("unidadeEnsino", "Todos");
                }
				getSuperParametroRelVO().adicionarParametro("dataInicio", getDataInicio());
				getSuperParametroRelVO().adicionarParametro("dataFim", getDataFim());
				getSuperParametroRelVO().adicionarParametro("departamento", getDepartamentoVO().getNome());
				getSuperParametroRelVO().adicionarParametro("categoriaDespesa", getCategoriaDespesaVO().getDescricao());
				getSuperParametroRelVO().adicionarParametro("categoriaProduto", getCategoriaProdutoVO().getNome());
				getSuperParametroRelVO().adicionarParametro("produto", getProdutoServicoVO().getNome());
				getSuperParametroRelVO().adicionarParametro("requisitante", getRequisicaoVO().getSolicitanteRequisicao().getPessoa().getNome());
				getSuperParametroRelVO().adicionarParametro("centroResultado", getRequisicaoVO().getCentroResultadoAdministrativo().getDescricao());
				getSuperParametroRelVO().adicionarParametro("situacao", getSituacaoEntrega());
                if (!getCentroResultadoEstoqueVO().getDescricao().equals("")) {
				getSuperParametroRelVO().adicionarParametro("centroResultadoEstoque", getCentroResultadoEstoqueVO().getDescricao());
                }
                else {
                	getSuperParametroRelVO().adicionarParametro("centroResultadoEstoque", "Todos");
                }
				getSuperParametroRelVO().adicionarParametro("dataInicioPeriodoConsumo", getDataInicioPeriodoConsumo());
				getSuperParametroRelVO().adicionarParametro("dataFimPeriodoConsumo", getDataFimPeriodoConsumo());
                realizarImpressaoRelatorio();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaObjetos);
        }
    }
    
    public String consultarDepartamento() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaDepartamento().equals("codigo")) {
                int valorInt = 0;
                if (!getValorConsultaDepartamento().equals("")) {
                    valorInt = Integer.parseInt(getValorConsultaDepartamento());
                }
                objs = getFacadeFactory().getDepartamentoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaDepartamento().equals("nome")) {
                objs = getFacadeFactory().getDepartamentoFacade().consultarPorNome(getValorConsultaDepartamento(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaDepartamento(objs);
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }
    
    public void selecionarDepartamento() throws Exception {
    	DepartamentoVO obj = (DepartamentoVO) context().getExternalContext().getRequestMap().get("departamentoItens");
        setDepartamentoVO(obj);
        getListaConsultaDepartamento().clear();
        valorConsultaDepartamento = null;
        campoConsultaDepartamento = null;
    }

    public void consultarProduto() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaProduto().equals("codigo")) {
                if (getValorConsultaProduto().equals("")) {
                    setValorConsultaProduto("0");
                }
                objs = getFacadeFactory().getProdutoServicoFacade().consultarPorCodigo(new Integer(getValorConsultaProduto()),
                        false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaProduto().equals("nome")) {
                objs = getFacadeFactory().getProdutoServicoFacade().consultarPorNome(getValorConsultaProduto(),
                        null, false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultaProduto(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaProduto(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarProduto() throws Exception {
        ProdutoServicoVO obj = (ProdutoServicoVO) context().getExternalContext().getRequestMap().get("produtoItens");
        setProdutoServicoVO(obj);
        getListaConsultaProduto().clear();
        valorConsultaProduto = null;
        campoConsultaProduto = null;
    }

    public void selecionarCategoriaProduto() throws Exception {
        CategoriaProdutoVO obj = (CategoriaProdutoVO) context().getExternalContext().getRequestMap().get("categoriaProdutoItens");
        this.setCategoriaProdutoVO(obj);
        this.setCategoriaDespesaVO(obj.getCategoriaDespesa());
        this.listaConsultaCategoriaProduto.clear();
        this.valorConsultaCategoriaProduto = null;
        this.campoConsultaCategoriaProduto = null;

    }

    public void consultarCategoriaDespesa() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCategoriaDespesa().equals("descricao")) {
                objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getValorConsultaCategoriaDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            if (getCampoConsultaCategoriaDespesa().equals("identificadorCategoriaDespesa")) {
                objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getValorConsultaCategoriaDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
            }
            setListaConsultaCategoriaDespesa(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCategoriaDespesa(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarCategoriaDespesa() throws Exception {
        CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("categoriaDespesaItens");
        setCategoriaDespesaVO(obj);
        getListaConsultaCategoriaDespesa().clear();
        valorConsultaCategoriaDespesa = null;
        campoConsultaCategoriaDespesa = null;
    }

    public List getTipoConsultaComboCategoriaDespesa() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("descricao", "Descrição"));
        itens.add(new SelectItem("identificadorCategoriaDespesa", "Identificador Centro Despesa"));
        return itens;
    }
    
    public List getTipoLayoutCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("RequisicaoRel", "Layout 1 - Sintético Por Produto"));
        itens.add(new SelectItem("RequisicaoRel2", "Layout 2 - Sintético Por Produto Requisições Finalizadas"));
        itens.add(new SelectItem("RequisicaoRel3", "Layout 3 -  Analítico Por Requisição"));
        itens.add(new SelectItem("RequisicaoRel4", "Layout 4 -  Consumo Sintético"));
        return itens;
    }

    public List getTipoConsultaComboCategoriaProduto() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public List getTipoConsultaComboProduto() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }
    
    public List getTipoConsultaComboDepartamento() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("codigo", "Código"));
        return itens;
    }

    public List getTipoSituacaoEntregaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("todas", "Todas"));
        itens.add(new SelectItem("PE", "Pendente"));
        itens.add(new SelectItem("PA", "Parcial"));
        itens.add(new SelectItem("FI", "Finalizada"));
        return itens;
    }

    public void limparDadosDepartamento() {
        setDepartamentoVO(null);
    }
    
    public void limparDadosProduto() {
        setProdutoServicoVO(null);
    }

    public void limparDadosCategoriaDespesa() {
        setCategoriaDespesaVO(null);
    }

    public void limparDadosCategoriaProduto() {
        setCategoriaProdutoVO(null);
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
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
	
	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("RequisicaoRel");
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
    
    public String consultarProdutoServico() {
        try {
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                int valorInt = 0;
                if (!getControleConsulta().getValorConsulta().equals("")) {
                    valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                }
                objs = getFacadeFactory().getProdutoServicoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getProdutoServicoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), null, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "consultar";
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "consultar";
        }
    }

    public void consultarCategoriaProduto() {
        try {
            List objs = new ArrayList(0);
            if (getCampoConsultaCategoriaProduto().equals("codigo")) {
                if (getValorConsultaCategoriaProduto().equals("")) {
                    setValorConsultaCategoriaProduto("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaCategoriaProduto());
                objs = getFacadeFactory().getCategoriaProdutoFacade().consultarPorCodigo(new Integer(valorInt), false, Uteis.NIVELMONTARDADOS_TODOS,
                        getUsuarioLogado());
            }
            if (getCampoConsultaCategoriaProduto().equals("nome")) {
                objs = getFacadeFactory().getCategoriaProdutoFacade().consultarPorNome(getValorConsultaCategoriaProduto(), false, Uteis.NIVELMONTARDADOS_TODOS,
                        getUsuarioLogado());
            }
            setListaConsultaCategoriaProduto(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaCategoriaProduto(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuarioLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            if (resultadoConsulta.isEmpty()) {
                resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            }
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            if (getUnidadeEnsinoLogado().getCodigo() == 0) {
            	objs.add(new SelectItem(0, ""));
            }
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                if (getUnidadeEnsinoLogado().getCodigo() == 0) {
                	objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
                } else if (obj.getCodigo().equals(getUnidadeEnsinoLogado().getCodigo())) {
                	setUnidadeEnsinoVO(obj);
                }
            }
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }
    
    public void selecionarRelatorioRequisicoesFinalizadas() {
    	if (getLayout().equals("RequisicaoRel2")|| getLayout().equals("RequisicaoRel4")) {
    		setSituacaoEntrega("FI");
    		getRequisicaoVO().setSituacaoAutorizacao("AU");
    		getRequisicaoVO().setTipoAutorizacaoRequisicaoEnum(TipoAutorizacaoRequisicaoEnum.NENHUM);
    		getRequisicaoVO().setSituacaoTipoAutorizacaoRequisicaoEnum("");
    		setDesabilitarSituacaoEntrega(Boolean.TRUE);
    	} else {
    		setDesabilitarSituacaoEntrega(Boolean.FALSE);
    	}
    }

    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
        return lista;
    }

    /*public UnidadeEnsinoVO getUnidadeEnsinoVO() {
        if (unidadeEnsinoVO == null) {
            unidadeEnsinoVO = new UnidadeEnsinoVO();
        }
        return unidadeEnsinoVO;
    }

    public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
        this.unidadeEnsinoVO = unidadeEnsinoVO;
    }*/

    public CategoriaDespesaVO getCategoriaDespesaVO() {
        if (categoriaDespesaVO == null) {
            categoriaDespesaVO = new CategoriaDespesaVO();
        }
        return categoriaDespesaVO;
    }

    public void setCategoriaDespesaVO(CategoriaDespesaVO categoriaDespesaVO) {
        this.categoriaDespesaVO = categoriaDespesaVO;
    }

    public CategoriaProdutoVO getCategoriaProdutoVO() {
        if (categoriaProdutoVO == null) {
            categoriaProdutoVO = new CategoriaProdutoVO();
        }
        return categoriaProdutoVO;
    }

    public void setCategoriaProdutoVO(CategoriaProdutoVO categoriaProdutoVO) {
        this.categoriaProdutoVO = categoriaProdutoVO;
    }

    public ProdutoServicoVO getProdutoServicoVO() {
        if (produtoServicoVO == null) {
            produtoServicoVO = new ProdutoServicoVO();
        }
        return produtoServicoVO;
    }

    public void setProdutoServicoVO(ProdutoServicoVO produtoServicoVO) {
        this.produtoServicoVO = produtoServicoVO;
    }

    public String getCampoConsultaProduto() {
        if (campoConsultaProduto == null) {
            campoConsultaProduto = "";
        }
        return campoConsultaProduto;
    }

    public void setCampoConsultaProduto(String campoConsultaProduto) {
        this.campoConsultaProduto = campoConsultaProduto;
    }

    public String getValorConsultaProduto() {
        if (valorConsultaProduto == null) {
            valorConsultaProduto = "";
        }
        return valorConsultaProduto;
    }

    public void setValorConsultaProduto(String valorConsultaProduto) {
        this.valorConsultaProduto = valorConsultaProduto;
    }

    public List getListaConsultaProduto() {
        if (listaConsultaProduto == null) {
            listaConsultaProduto = new ArrayList(0);
        }
        return listaConsultaProduto;
    }

    public void setListaConsultaProduto(List listaConsultaProduto) {
        this.listaConsultaProduto = listaConsultaProduto;
    }

    public String getCampoConsultaCategoriaProduto() {
        if (campoConsultaCategoriaProduto == null) {
            campoConsultaCategoriaProduto = "";
        }
        return campoConsultaCategoriaProduto;
    }

    public void setCampoConsultaCategoriaProduto(String campoConsultaCategoriaProduto) {
        this.campoConsultaCategoriaProduto = campoConsultaCategoriaProduto;
    }

    public String getValorConsultaCategoriaProduto() {
        if (valorConsultaCategoriaProduto == null) {
            valorConsultaCategoriaProduto = "";
        }
        return valorConsultaCategoriaProduto;
    }

    public void setValorConsultaCategoriaProduto(String valorConsultaCategoriaProduto) {
        this.valorConsultaCategoriaProduto = valorConsultaCategoriaProduto;
    }

    public List getListaConsultaCategoriaProduto() {
        if (listaConsultaCategoriaProduto == null) {
            listaConsultaCategoriaProduto = new ArrayList(0);
        }
        return listaConsultaCategoriaProduto;
    }

    public void setListaConsultaCategoriaProduto(List listaConsultaCategoriaProduto) {
        this.listaConsultaCategoriaProduto = listaConsultaCategoriaProduto;
    }

    public List getListaConsultaCategoriaDespesa() {
        if (listaConsultaCategoriaDespesa == null) {
            listaConsultaCategoriaDespesa = new ArrayList(0);
        }
        return listaConsultaCategoriaDespesa;
    }

    public void setListaConsultaCategoriaDespesa(List listaConsultaCategoriaDespesa) {
        this.listaConsultaCategoriaDespesa = listaConsultaCategoriaDespesa;
    }

    public String getValorConsultaCategoriaDespesa() {
        if (valorConsultaCategoriaDespesa == null) {
            valorConsultaCategoriaDespesa = "";
        }
        return valorConsultaCategoriaDespesa;
    }

    public void setValorConsultaCategoriaDespesa(String valorConsultaCategoriaDespesa) {
        this.valorConsultaCategoriaDespesa = valorConsultaCategoriaDespesa;
    }

    public String getCampoConsultaCategoriaDespesa() {
        if (campoConsultaCategoriaDespesa == null) {
            campoConsultaCategoriaDespesa = "";
        }
        return campoConsultaCategoriaDespesa;
    }

    public void setCampoConsultaCategoriaDespesa(String campoConsultaCategoriaDespesa) {
        this.campoConsultaCategoriaDespesa = campoConsultaCategoriaDespesa;
    }

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public String getSituacaoEntrega() {
        if (situacaoEntrega == null) {
            situacaoEntrega = "todas";
        }
        return situacaoEntrega;
    }

    public void setSituacaoEntrega(String situacaoEntrega) {
        this.situacaoEntrega = situacaoEntrega;
    }

	public String getCampoConsultaDepartamento() {
		if (campoConsultaDepartamento == null) {
			campoConsultaDepartamento = "";
		}
		return campoConsultaDepartamento;
	}

	public void setCampoConsultaDepartamento(String campoConsultaDepartamento) {
		this.campoConsultaDepartamento = campoConsultaDepartamento;
	}

	public String getValorConsultaDepartamento() {
		if (valorConsultaDepartamento == null) {
			valorConsultaDepartamento = "";
		}
		return valorConsultaDepartamento;
	}

	public void setValorConsultaDepartamento(String valorConsultaDepartamento) {
		this.valorConsultaDepartamento = valorConsultaDepartamento;
	}

	public List getListaConsultaDepartamento() {
		if (listaConsultaDepartamento == null) {
			listaConsultaDepartamento = new ArrayList(0);
		}
		return listaConsultaDepartamento;
	}

	public void setListaConsultaDepartamento(List listaConsultaDepartamento) {
		this.listaConsultaDepartamento = listaConsultaDepartamento;
	}

	public DepartamentoVO getDepartamentoVO() throws Exception{
		if (departamentoVO == null) {
			departamentoVO = new DepartamentoVO();
		}
		return departamentoVO;
	}

	public void setDepartamentoVO(DepartamentoVO departamentoVO) {
		this.departamentoVO = departamentoVO;
	}

	public String getLayout() {
		if (layout == null) {
			layout = "RequisicaoRel";
		}
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public Boolean getDesabilitarSituacaoEntrega() {
		if (desabilitarSituacaoEntrega == null) {
			desabilitarSituacaoEntrega = Boolean.FALSE;
		}
		return desabilitarSituacaoEntrega;
	}

	public void setDesabilitarSituacaoEntrega(Boolean desabilitarSituacaoEntrega) {
		this.desabilitarSituacaoEntrega = desabilitarSituacaoEntrega;
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

	public RequisicaoVO getRequisicaoVO() {
		if (requisicaoVO == null) {
			requisicaoVO = new RequisicaoVO();
		}
		return requisicaoVO;
	}

	public void setRequisicaoVO(RequisicaoVO requisicaoVO) {
		this.requisicaoVO = requisicaoVO;
	}	
	
	public void inicializaConsultaRequisitante() {
		this.limparRequisitanteDataModelo();
		inicializarMensagemVazia();
	}
	
	public void selecionarRequisitante() {
		try {
			UsuarioVO obj = (UsuarioVO) context().getExternalContext().getRequestMap().get("requisitanteItem");
			getRequisicaoVO().setSolicitanteRequisicao(obj);
			setMensagemID(MSG_TELA.msg_dados_selecionados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}
	
	public void consultarRequisitante() {
		try {
			this.getRequisitanteDataModelo().setListaConsulta(getFacadeFactory().getUsuarioFacade().consultarUsuarioPorUnidadeEnsino(getRequisitanteDataModelo().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), this.getRequisitanteDataModelo().getLimitePorPagina(), this.getRequisitanteDataModelo().getOffset(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, this.getUsuarioLogado()));
			this.getRequisitanteDataModelo().setTotalRegistrosEncontrados(getFacadeFactory().getUsuarioFacade().consultarUsuarioPorUnidadeEnsinoContador(getRequisitanteDataModelo().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, this.getUsuarioLogado()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public List<SelectItem> getTipoConsultaComboRequisitante() {
		List<SelectItem> itens = new ArrayList<>(0);
		itens.add(new SelectItem("nome", "Nome"));
		return itens;
	}

	public DataModelo getRequisitanteDataModelo() throws Exception {

		this.requisitanteDataModelo = Optional.ofNullable(this.requisitanteDataModelo).orElseGet(() -> {
			DataModelo dataModelo = new DataModelo();
			dataModelo.setOffset(0);
			dataModelo.setLimitePorPagina(10);

			dataModelo.setPaginaAtual(1);
			return dataModelo;
		});
		return requisitanteDataModelo;
	}

	private void limparRequisitanteDataModelo() {
		this.requisitanteDataModelo = null;		
	}
	
	public void scrollerListenerUsuario(DataScrollEvent dataScrollEvent) throws Exception {
		getRequisitanteDataModelo().setPaginaAtual(dataScrollEvent.getPage());
		getRequisitanteDataModelo().setPage(dataScrollEvent.getPage());
		consultarRequisitante();
	}

	public void setRequisitanteDataModelo(DataModelo requisitanteDataModelo) {
		this.requisitanteDataModelo = requisitanteDataModelo;
	}
	
	public void limparRequisitante() {
		this.requisicaoVO.setSolicitanteRequisicao(null);
	}
	
	public DataModelo getCentroResultadoDataModelo() {
		centroResultadoDataModelo = Optional.ofNullable(centroResultadoDataModelo).orElse(new DataModelo());
		return centroResultadoDataModelo;
	}

	public void setCentroResultadoDataModelo(DataModelo centroResultadoDataModelo) {
		this.centroResultadoDataModelo = centroResultadoDataModelo;
	}

	
	public void inicializarDadoConsultaCentroResultadoAdministrativo() {
		try {
			inicializarDadosComunsCentroResultado();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}

	}
	
	private void inicializarDadosComunsCentroResultado() {
		setCentroResultadoDataModelo(new DataModelo());
		getCentroResultadoDataModelo().setCampoConsulta(CentroResultadoVO.enumCampoConsultaCentroResultado.DESCRICAO_CENTRO_RESULTADO.name());
	}

	public void selecionarCentroResultado() {
		try {
			CentroResultadoVO obj = (CentroResultadoVO) context().getExternalContext().getRequestMap().get("centroResultadoItens");
				getRequisicaoVO().setCentroResultadoAdministrativo(obj);

		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerCentroResultado(DataScrollEvent dataScrollerEvent) {
		try {
			getCentroResultadoDataModelo().setPaginaAtual(dataScrollerEvent.getPage());
			getCentroResultadoDataModelo().setPage(dataScrollerEvent.getPage());
			consultarCentroResultado();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void consultarCentroResultado() {
		try {
			super.consultar();
			getCentroResultadoDataModelo().preencherDadosParaConsulta(false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			getFacadeFactory().getCentroResultadoFacade().consultar(SituacaoEnum.ATIVO, false, getRequisicaoVO().getDepartamento(), getRequisicaoVO().getCurso(), getRequisicaoVO().getTurma(), getCentroResultadoDataModelo());
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void limparCentroResultado() {
		getRequisicaoVO().setCentroResultadoAdministrativo(null);
	}
	
	public CentroResultadoVO getCentroResultadoEstoqueVO() {
		if (centroResultadoEstoqueVO == null) {
			centroResultadoEstoqueVO = new CentroResultadoVO();
		}
		return centroResultadoEstoqueVO;
	}

	public void setCentroResultadoEstoqueVO(CentroResultadoVO centroResultadoEstoqueVO) {
		this.centroResultadoEstoqueVO = centroResultadoEstoqueVO;
	}
	
	public List<CentroResultadoVO> getListaCentroResultadoEstoque() {
		if (listaCentroResultadoEstoque == null || listaCentroResultadoEstoque.isEmpty()) {
			listaCentroResultadoEstoque = new ArrayList<CentroResultadoVO>(0);
		}
		return listaCentroResultadoEstoque;
	}

	public void setListaCentroResultadoEstoque(List<CentroResultadoVO> listaCentroResultadoEstoque) {
		this.listaCentroResultadoEstoque = listaCentroResultadoEstoque;
	}
	
	public Boolean getMarcarTodosCentroResultadoEstoque() {
		if (marcarTodosCentroResultadoEstoque == null) {
			marcarTodosCentroResultadoEstoque = Boolean.FALSE;
		}
		return marcarTodosCentroResultadoEstoque;
	}

	public void setMarcarTodosCentroResultadoEstoque(Boolean marcarTodosCentroResultadoEstoque) {
		this.marcarTodosCentroResultadoEstoque = marcarTodosCentroResultadoEstoque;
	}
	
	@PostConstruct
	public void consultarCentroResultadoEstoque() {
		try {
			montarListaSelectItemCentroResultadoEstoque("");
			verificarTodosCentroResultadoEstoqueSelecionados();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void verificarTodosCentroResultadoEstoqueSelecionados() {
		StringBuilder centroResultadoEstoque = new StringBuilder();
		if (getListaCentroResultadoEstoque().size() > 1) {
			for (CentroResultadoVO obj : getListaCentroResultadoEstoque()) {
				if (obj.getFiltrarCentroResultado()) {
					centroResultadoEstoque.append(obj.getDescricao()).append("; ");
				} 
			}
			getCentroResultadoEstoqueVO().setDescricao(centroResultadoEstoque.toString());
		} else {
			if (!getListaCentroResultadoEstoque().isEmpty()) {
				if (getListaCentroResultadoEstoque().get(0).getFiltrarCentroResultado()) {
					getCentroResultadoEstoqueVO().setDescricao(getListaCentroResultadoEstoque().get(0).getDescricao());
				}
			} else {
				getCentroResultadoEstoqueVO().setDescricao(centroResultadoEstoque.toString());
			}
		}
		
	}

	public void marcarTodosCentroResultadoEstoqueAction() {
		for (CentroResultadoVO centroResultadoEstoque : getListaCentroResultadoEstoque()) {
			if (getMarcarTodosCentroResultadoEstoque()) {
				centroResultadoEstoque.setFiltrarCentroResultado(Boolean.TRUE);
			} else {
				centroResultadoEstoque.setFiltrarCentroResultado(Boolean.FALSE);
			}
		}
		verificarTodosCentroResultadoEstoqueSelecionados();
	}
	
	public List<CentroResultadoVO> obterListaCentroResultadoEstoqueSelecionado() {
		List<CentroResultadoVO> objs = new ArrayList<CentroResultadoVO>(0);
		this.listaCentroResultadoEstoque.forEach(obj->{
			if (obj.getFiltrarCentroResultado()) {
				objs.add(obj);
			}
		});
		return objs;
	}
	
	/**
	 * Método responsável por gerar uma lista de objetos do tipo
	 * <code>SelectItem</code> para preencher o comboBox relativo ao atributo
	 * <code>FormaPagamento</code>.
	 */
	public void montarListaSelectItemCentroResultadoEstoque(String prm) throws Exception {
		List<CentroResultadoVO> resultadoConsulta = null;
		try {
			setControleConsultaOtimizado(new DataModelo(true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			resultadoConsulta = getFacadeFactory().getCentroResultadoFacade().consultaResultadoEstoque(getControleConsultaOtimizado());
			getListaCentroResultadoEstoque().clear();
			resultadoConsulta.stream().forEach(p->{
				getListaCentroResultadoEstoque().add(p);
			});
			for (CentroResultadoVO obj : getListaCentroResultadoEstoque()) {
				obj.setFiltrarCentroResultado(true);
			}
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
		}
	}
	
	public void limparCentroResultadoEstoque(){
		setMarcarTodosCentroResultadoEstoque(false);
		marcarTodosCentroResultadoEstoqueAction();
		setCentroResultadoEstoqueVO(null);
	}

	public List<SelectItem> getListaSelectItemSituacaoAutorizacao() {
		List<SelectItem> objs = new ArrayList<>(0);
		objs.add(new SelectItem("", "Todas"));
		objs.add(new SelectItem("AU", "Autorizado"));
		objs.add(new SelectItem("PE", "Pendente"));
		objs.add(new SelectItem("IN", "Indeferido"));
		return objs;
	}
	
	public List<SelectItem> getListaSelectItemTipoAutorizacaoRequisicaoEnum() {
		List<SelectItem> objs = new ArrayList<>(0);
		objs.add(new SelectItem(TipoAutorizacaoRequisicaoEnum.NENHUM, "Todas"));
		objs.add(new SelectItem(TipoAutorizacaoRequisicaoEnum.COTACAO, "Cotação"));
		objs.add(new SelectItem(TipoAutorizacaoRequisicaoEnum.COMPRA_DIRETA, "Compra Direta"));
		objs.add(new SelectItem(TipoAutorizacaoRequisicaoEnum.RETIRADA, "Retirada"));
		return objs;
	}
	
		
	public List<SelectItem> getListaSelectItemSituacaoTipoAutorizacaoRequisicaoEnum() {
		List<SelectItem> objs = new ArrayList<>(0);
		if(Uteis.isAtributoPreenchido(getRequisicaoVO().getTipoAutorizacaoRequisicaoEnum()) && getRequisicaoVO().getTipoAutorizacaoRequisicaoEnum().isCotacao()) {
			objs.add(new SelectItem("AC", "Aguardando Cotação"));			
			objs.add(new SelectItem("CO", "Cotado"));			
		}else if(Uteis.isAtributoPreenchido(getRequisicaoVO().getTipoAutorizacaoRequisicaoEnum()) && getRequisicaoVO().getTipoAutorizacaoRequisicaoEnum().isCompraDireta()) {
			objs.add(new SelectItem("AD", "Aguardando Compra Direta"));			
			objs.add(new SelectItem("CD", "Comprado"));
		}
		return objs;
	}
	
	public void limparCamposSituacaoTipoAutorizacaoRequisicaoEnum() {
		if(!Uteis.isAtributoPreenchido(getRequisicaoVO().getTipoAutorizacaoRequisicaoEnum()) 
				|| (getRequisicaoVO().getTipoAutorizacaoRequisicaoEnum().isRetirada())) {
			getRequisicaoVO().setSituacaoTipoAutorizacaoRequisicaoEnum("");
		}
	}
	


	public Date getDataInicioPeriodoConsumo() {
		if (dataInicioPeriodoConsumo == null) {
			dataInicioPeriodoConsumo = Uteis.getNewDateComMesesAMenos(1);
		}
		return dataInicioPeriodoConsumo;
	}

	public void setDataInicioPeriodoConsumo(Date dataInicioPeriodoConsumo) {
		this.dataInicioPeriodoConsumo = dataInicioPeriodoConsumo;
	}

	public Date getDataFimPeriodoConsumo() {
		if (dataFimPeriodoConsumo == null) {
			dataFimPeriodoConsumo = new Date();
		}
		return dataFimPeriodoConsumo;
	}

	public void setDataFimPeriodoConsumo(Date dataFimPeriodoConsumo) {
		this.dataFimPeriodoConsumo = dataFimPeriodoConsumo;
	}	
	
}
