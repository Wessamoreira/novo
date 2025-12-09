package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.GrupoContaPagarVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.ExtratoContaPagarRelVO;

@Controller("ExtratoContaPagarRelControle")
@Scope("viewScope")
@Lazy
public class ExtratoContaPagarRelControle extends SuperControleRelatorio {

	private static final long serialVersionUID = -3505209110624470399L;

	private Date dataInicio;
    private Date dataFim;
    private ContaPagarVO contaPagarVO;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private String filtroTipoData;
    private String filtroTipoFavorecido;
    private Integer codigoFavorecido;
    private String nomeFavorecido;

    private DataModelo dataModeloAluno;

    //Consulta Banco
    protected List<SelectItem> listaSelectItemBanco;
    //Consulta OperadoraCartao
    protected List<SelectItem> listaSelectItemOperadoraCartao;
    //Consulta Fornecedor
    protected String valorConsultaFornecedor;
    protected String campoConsultaFornecedor;
    protected List<FornecedorVO> listaConsultaFornecedor;
    //Consulta Funcionario
    private String valorConsultaFuncionario;
    private String campoConsultaFuncionario;
    private List<FuncionarioVO> listaConsultaFuncionario;
    
    private List listaConsultaParceiro;
    private String valorConsultaParceiro;
    private String campoConsultaParceiro;

    public ExtratoContaPagarRelControle() throws Exception {
        montarListaSelectItemUnidadeEnsino();
        setDataInicio(Uteis.getDataPrimeiroDiaMes(new Date()));
        setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
        setMensagemID("msg_entre_prmrelatorio");
    }

    public void imprimirRelatorioExcel() {
        List<ExtratoContaPagarRelVO> listaExtratoContaPagarRelVO = null;
        try {
            getFacadeFactory().getExtratoContaPagarRelFacade().validarDados(getCodigoFavorecido(), getDataInicio(), getDataFim());
            listaExtratoContaPagarRelVO = getFacadeFactory().getExtratoContaPagarRelFacade().criarObjeto(getCodigoFavorecido(), getNomeFavorecido(), getFiltroTipoFavorecido(), getFiltroTipoData(), getDataInicio(), getDataFim(), getContaPagarVO().getUnidadeEnsino());
            if (!listaExtratoContaPagarRelVO.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getExtratoContaPagarRelFacade().getDesignIReportRelatorioExcel());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getExtratoContaPagarRelFacade().caminhoBaseIReportRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório de Extrato Contas a Pagar");
                getSuperParametroRelVO().setListaObjetos(listaExtratoContaPagarRelVO);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getExtratoContaPagarRelFacade().caminhoBaseIReportRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setQuantidade(listaExtratoContaPagarRelVO.size());
                getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
                if (getFiltroTipoData().equals("vencimento")) {
                    getSuperParametroRelVO().setOrdenadoPor("Data de Vencimento");
                } else {
                    getSuperParametroRelVO().setOrdenadoPor("Data Fato Gerador");
                }
                realizarImpressaoRelatorio();
                removerObjetos();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaExtratoContaPagarRelVO);
        }
    }

    public void imprimirPDF() {
        List<ExtratoContaPagarRelVO> listaExtratoContaPagarRelVO = null;
        try {
            getFacadeFactory().getExtratoContaPagarRelFacade().validarDados(getCodigoFavorecido(), getDataInicio(), getDataFim());
            listaExtratoContaPagarRelVO = getFacadeFactory().getExtratoContaPagarRelFacade().criarObjeto(getCodigoFavorecido(), getNomeFavorecido(), getFiltroTipoFavorecido(), getFiltroTipoData(), getDataInicio(), getDataFim(), getContaPagarVO().getUnidadeEnsino());
            if (!listaExtratoContaPagarRelVO.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getExtratoContaPagarRelFacade().getDesignIReportRelatorioPdf());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getExtratoContaPagarRelFacade().caminhoBaseIReportRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório de Extrato Contas a Pagar");
                getSuperParametroRelVO().setListaObjetos(listaExtratoContaPagarRelVO);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getExtratoContaPagarRelFacade().caminhoBaseIReportRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setQuantidade(listaExtratoContaPagarRelVO.get(0).getListaContaPagarVOs().size());
                getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));

                if (getFiltroTipoData().equals("vencimento")) {
                    getSuperParametroRelVO().setOrdenadoPor("Data de Vencimento");
                } else {
                    getSuperParametroRelVO().setOrdenadoPor("Data Fato Gerador");
                }
                realizarImpressaoRelatorio();
                removerObjetos();
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        } finally {
            Uteis.liberarListaMemoria(listaExtratoContaPagarRelVO);
        }
    }

    public String imprimirPDFGrupoContaPagar(GrupoContaPagarVO grupoContaPagar) throws Exception {
        List<ExtratoContaPagarRelVO> listaExtratoContaPagarRelVO = new ArrayList<ExtratoContaPagarRelVO>();
        try {
            ExtratoContaPagarRelVO extratoContaPagarRelVO = new ExtratoContaPagarRelVO();
            extratoContaPagarRelVO.setFavorecido(grupoContaPagar.getFavorecido());
            getFacadeFactory().getExtratoContaPagarRelFacade().calcularTotalPagarTotalPagoExtratoContaPagar(extratoContaPagarRelVO, grupoContaPagar.getContaPagarVOs());
            extratoContaPagarRelVO.setListaContaPagarVOs(grupoContaPagar.getContaPagarVOs());
            getFacadeFactory().getExtratoContaPagarRelFacade().montarDadosBancoRecebimento(extratoContaPagarRelVO);
            if (!extratoContaPagarRelVO.getListaContaPagarVOs().isEmpty()) {
                if (!extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFornecedor().getCodigo().equals(0)) {
                    extratoContaPagarRelVO.setNomeBanco(extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFornecedor().getNomeBanco());
                    extratoContaPagarRelVO.setNumeroBancoRecebimento(extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFornecedor().getNumeroBancoRecebimento());
                    extratoContaPagarRelVO.setNumeroAgenciaRecebimento(extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFornecedor().getNumeroAgenciaRecebimento());
                    extratoContaPagarRelVO.setContaCorrenteRecebimento(extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFornecedor().getContaCorrenteRecebimento());
                    extratoContaPagarRelVO.setTipoFavorecidoFornecedorFuncionario(true);
                } else if (!extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFuncionario().getCodigo().equals(0)) {
                    extratoContaPagarRelVO.setNomeBanco(extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFuncionario().getNomeBanco());
                    extratoContaPagarRelVO.setNumeroBancoRecebimento(extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFuncionario().getNumeroBancoRecebimento());
                    extratoContaPagarRelVO.setNumeroAgenciaRecebimento(extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFuncionario().getNumeroAgenciaRecebimento());
                    extratoContaPagarRelVO.setContaCorrenteRecebimento(extratoContaPagarRelVO.getListaContaPagarVOs().get(0).getFuncionario().getContaCorrenteRecebimento());
                    extratoContaPagarRelVO.setTipoFavorecidoFornecedorFuncionario(true);
                } else {
                    extratoContaPagarRelVO.setTipoFavorecidoFornecedorFuncionario(false);
                }
            }

            listaExtratoContaPagarRelVO.add(extratoContaPagarRelVO);
            if (!listaExtratoContaPagarRelVO.isEmpty()) {
                getSuperParametroRelVO().setNomeDesignIreport(getFacadeFactory().getExtratoContaPagarRelFacade().getDesignIReportRelatorioPdf());
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setSubReport_Dir(getFacadeFactory().getExtratoContaPagarRelFacade().caminhoBaseIReportRelatorio());
                getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
                getSuperParametroRelVO().setTituloRelatorio("Relatório de Extrato Contas a Pagar");
                getSuperParametroRelVO().setListaObjetos(listaExtratoContaPagarRelVO);
                getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getExtratoContaPagarRelFacade().caminhoBaseIReportRelatorio());
                getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
                getSuperParametroRelVO().setQuantidade(listaExtratoContaPagarRelVO.get(0).getListaContaPagarVOs().size());
                
                if(getSuperParametroRelVO().getPeriodo() == null){
                	getSuperParametroRelVO().setPeriodo("");
                	getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(grupoContaPagar.getContaPagarVOs().get(0).getDataVencimento())) + "  a  " + String.valueOf(Uteis.getData(grupoContaPagar.getContaPagarVOs().get(grupoContaPagar.getContaPagarVOs().size() - 1).getDataVencimento())));
                }
                getSuperParametroRelVO().setOrdenadoPor("Data de Vencimento");
                realizarImpressaoRelatorio();
                Uteis.liberarListaMemoria(listaExtratoContaPagarRelVO);
                return getCaminhoRelatorio();
            } else {
                Uteis.liberarListaMemoria(listaExtratoContaPagarRelVO);
                return "";
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void removerObjetos() {
        removerObjetoMemoria(this);
        setDataInicio(Uteis.getDataPrimeiroDiaMes(new Date()));
        setDataFim(Uteis.getDataUltimoDiaMes(new Date()));
        montarListaSelectItemUnidadeEnsino();
    }

    public List<SelectItem> getListaSelectItemTipoSacado() throws Exception {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("AL", "Aluno/Candidato"));
        itens.add(new SelectItem("BA", "Banco"));
        itens.add(new SelectItem("FO", "Fornecedor"));        
        itens.add(new SelectItem("FU", "Funcionário"));
        itens.add(new SelectItem("PA", "Parceiro"));
        itens.add(new SelectItem("RF", "Responsável Financeiro"));
        itens.add(new SelectItem("OC", "Operadora Cartão"));
        return itens;
    }

    public List<SelectItem> getListaFiltroData() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("vencimento", "Data de Vencimento"));
        itens.add(new SelectItem("fatoGerador", "Data Fato Gerador"));
        return itens;
    }

    public List<SelectItem> getTipoConsultaComboAluno() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nomePessoa", "Aluno/Candidato"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        return itens;
    }

    public void consultarAluno() {
        try {
        	try {
    			if (getDataModeloAluno().getValorConsulta().equals("")) {
    				setMensagemID("msg_entre_prmconsulta");
    				return;
    			}
    			List<MatriculaVO> objs = new ArrayList<>(0);
    			if (getDataModeloAluno().getCampoConsulta().equals("matricula")) {
    				objs.add(getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getDataModeloAluno().getValorConsulta(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
    				getDataModeloAluno().setListaConsulta(objs);
    				if (Uteis.isAtributoPreenchido(objs) && Uteis.isAtributoPreenchido(objs.get(0).getAluno().getNome())) {					
    					getDataModeloAluno().setTotalRegistrosEncontrados(1);
    				} else {
    					getDataModeloAluno().setTotalRegistrosEncontrados(0);
    				}
    			}
    			if (getDataModeloAluno().getCampoConsulta().equals("nomePessoa")) {
    				getDataModeloAluno().setLimitePorPagina(10);
    				getFacadeFactory().getMatriculaFacade().consultarMatriculas(getDataModeloAluno(), getUsuarioLogado());
    			}
    			setMensagemID("msg_dados_consultados");
    		} catch (Exception e) {
    			setMensagemDetalhada("msg_erro", e.getMessage());
    		}
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarAluno() throws Exception {
        MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("matriculaItens");
        getContaPagarVO().setPessoa(obj.getAluno());
        setCodigoFavorecido(getContaPagarVO().getPessoa().getCodigo());
        setNomeFavorecido(getContaPagarVO().getPessoa().getNome());
    }

    public void montarListaSelectItemBanco() {
        try {
            montarListaSelectItemBanco("");
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }

    public void montarListaSelectItemBanco(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarBancoPorNome(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                BancoVO obj = (BancoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome().toString()));
            }
            setListaSelectItemBanco(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List<BancoVO> consultarBancoPorNome(String nomePrm) throws Exception {
        List<BancoVO> lista = getFacadeFactory().getBancoFacade().consultarPorNome(nomePrm, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        Ordenacao.ordenarLista(lista, "nome");
        return lista;
    }

    public void selecionarBanco() throws Exception {
        try {
            getContaPagarVO().setBanco(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(getContaPagarVO().getBanco().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            setCodigoFavorecido(getContaPagarVO().getBanco().getCodigo());
            setNomeFavorecido(getContaPagarVO().getBanco().getNome());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaComboFornecedor() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("razaoSocial", "Razão Social"));
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nomeCidade", "Cidade"));
        itens.add(new SelectItem("CNPJ", "CNPJ"));
        itens.add(new SelectItem("inscEstadual", "Inscrição Estadual"));
        itens.add(new SelectItem("RG", "RG"));
        itens.add(new SelectItem("CPF", "CPF"));
        return itens;
    }

    public void consultarFornecedor() {
        try {
            List<FornecedorVO> objs = new ArrayList<>(0);
            if (getCampoConsultaFornecedor().equals("codigo")) {
                if (getValorConsultaFornecedor().equals("")) {
                    setValorConsultaFornecedor("0");
                }
                int valorInt = Integer.parseInt(getValorConsultaFornecedor());
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCodigo(new Integer(valorInt), "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("nome")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("razaoSocial")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("nomeCidade")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorNomeCidade(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("CNPJ")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("inscEstadual")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorInscEstadual(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("RG")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorRG(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getCampoConsultaFornecedor().equals("CPF")) {
                objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsultaFornecedor(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaFornecedor(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void scrollerListener(DataScrollEvent dataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(dataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(dataScrollEvent.getPage());
        consultar();
    }
    
    public void scrollerListenerAluno(DataScrollEvent dataScrollEvent) {
		getDataModeloAluno().setPaginaAtual(dataScrollEvent.getPage());
		getDataModeloAluno().setPage(dataScrollEvent.getPage());
		consultarAluno();
	}

    public void selecionarFornecedor() {
        FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItem");
        getContaPagarVO().setFornecedor(obj);
        setCodigoFavorecido(getContaPagarVO().getFornecedor().getCodigo());
        setNomeFavorecido(getContaPagarVO().getFornecedor().getNome());
    }

    public List<SelectItem> getTipoConsultaComboFuncionario() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("CPF", "CPF"));
        itens.add(new SelectItem("cargo", "Cargo"));
        itens.add(new SelectItem("departamento", "Departamento"));
        return itens;
    }

    public void consultarFuncionario() {
        try {
            List<FuncionarioVO> objs = new ArrayList<>(0);
            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNome(getValorConsultaFuncionario(), "", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorMatricula(getValorConsultaFuncionario(), 0, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCPF(getValorConsultaFuncionario(), "", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("cargo")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorCargo(getValorConsultaFuncionario(), 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("departamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultaRapidaPorNomeDepartamento(getValorConsultaFuncionario(), "FU", 0, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaFuncionario(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsultaFornecedor(new ArrayList<>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarFuncionario() {
        try {
            FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItem");
            getContaPagarVO().setFuncionario(obj);
            setCodigoFavorecido(getContaPagarVO().getFuncionario().getPessoa().getCodigo());
            setNomeFavorecido(getContaPagarVO().getFuncionario().getPessoa().getNome());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }

    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
                setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
                getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
                getContaPagarVO().setUnidadeEnsino(getUnidadeEnsinoLogado());
                return;
            }
            resultadoConsulta = consultarUnidadeEnsinoPorCodigo(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
            }
            setListaSelectItemUnidadeEnsino(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public List consultarUnidadeEnsinoPorCodigo(String numeroPrm) throws Exception {
        return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(numeroPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
    }

    public void limparDadosFavorecido() {
        getContaPagarVO().setPessoa(null);
        getContaPagarVO().setBanco(null);
        getContaPagarVO().setFornecedor(null);
        getContaPagarVO().setFuncionario(null);
        getContaPagarVO().setResponsavelFinanceiro(null);
        getContaPagarVO().setParceiro(null);
        if (getFiltroTipoFavorecido().equals("BA")) {
            montarListaSelectItemBanco();
        }
    }

    @Override
    public void limparMensagem() {
        setMensagem("");
        setMensagemDetalhada("");
        setMensagemID("");
        setCodigoFavorecido(0);
        setNomeFavorecido("");
    }

    public void limparDadosAluno() {
        getContaPagarVO().setPessoa(new PessoaVO());
    }

    public void limparDadosFornecedor() {
        getContaPagarVO().setFornecedor(new FornecedorVO());
    }

    public void limparDadosFuncionario() {
        getContaPagarVO().setFuncionario(new FuncionarioVO());
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

    public String getCampoConsultaFuncionario() {
        if (campoConsultaFuncionario == null) {
            campoConsultaFuncionario = "";
        }
        return campoConsultaFuncionario;
    }

    public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
        this.campoConsultaFuncionario = campoConsultaFuncionario;
    }

    public ContaPagarVO getContaPagarVO() {
        if (contaPagarVO == null) {
            contaPagarVO = new ContaPagarVO();
        }
        return contaPagarVO;
    }

    public void setContaPagarVO(ContaPagarVO contaPagarVO) {
        this.contaPagarVO = contaPagarVO;
    }

    public Date getDataFim() {
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Date getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(Date dataInicio) {
        this.dataInicio = dataInicio;
    }

    public String getFiltroTipoData() {
        if (filtroTipoData == null) {
            filtroTipoData = "vencimento";
        }
        return filtroTipoData;
    }

    public void setFiltroTipoData(String filtroTipoData) {
        this.filtroTipoData = filtroTipoData;
    }

    public String getFiltroTipoFavorecido() {
        if (filtroTipoFavorecido == null) {
            filtroTipoFavorecido = "AL";
        }
        return filtroTipoFavorecido;
    }

    public void setFiltroTipoFavorecido(String filtroTipoFavorecido) {
        this.filtroTipoFavorecido = filtroTipoFavorecido;
    }

    public List<FornecedorVO> getListaConsultaFornecedor() {
        if (listaConsultaFornecedor == null) {
            listaConsultaFornecedor = new ArrayList<>(0);
        }
        return listaConsultaFornecedor;
    }

    public void setListaConsultaFornecedor(List<FornecedorVO> listaConsultaFornecedor) {
        this.listaConsultaFornecedor = listaConsultaFornecedor;
    }

    public List<FuncionarioVO> getListaConsultaFuncionario() {
        if (listaConsultaFuncionario == null) {
            listaConsultaFuncionario = new ArrayList<>(0);
        }
        return listaConsultaFuncionario;
    }

    public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
    }

    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<>(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
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

    public String getValorConsultaFuncionario() {
        if (valorConsultaFuncionario == null) {
            valorConsultaFuncionario = "";
        }
        return valorConsultaFuncionario;
    }

    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
    }

    public Integer getCodigoFavorecido() {
        if (codigoFavorecido == null) {
            codigoFavorecido = 0;
        }
        return codigoFavorecido;
    }

    public void setCodigoFavorecido(Integer codigoFavorecido) {
        this.codigoFavorecido = codigoFavorecido;
    }

    public String getNomeFavorecido() {
        if (nomeFavorecido == null) {
            nomeFavorecido = "";
        }
        return nomeFavorecido;
    }

    public void setNomeFavorecido(String nomeFavorecido) {
        this.nomeFavorecido = nomeFavorecido;
    }

    public List<SelectItem> getListaSelectItemBanco() {
        if (listaSelectItemBanco == null) {
            listaSelectItemBanco = new ArrayList<>(0);
        }
        return listaSelectItemBanco;
    }

    public void setListaSelectItemBanco(List<SelectItem> listaSelectItemBanco) {
        this.listaSelectItemBanco = listaSelectItemBanco;
    }

    public boolean getIsTipoFavorecidoAluno() {
        return getFiltroTipoFavorecido().equals("AL");
    }
    
    public boolean getIsTipoFavorecidoOperadoraCartao() {
    	return getFiltroTipoFavorecido().equals("OC");
    }

    public boolean getIsTipoFavorecidoBanco() {
        return getFiltroTipoFavorecido().equals("BA");
    }

    public boolean getIsTipoFavorecidoFornecedor() {
        return getFiltroTipoFavorecido().equals("FO");
    }

    public boolean getIsTipoFavorecidoFuncionario() {
        return getFiltroTipoFavorecido().equals("FU");
    }
    
    public boolean getIsTipoFavorecidoParceiro() {
        return getFiltroTipoFavorecido().equals("PA");
    }
    
    public boolean getIsTipoFavorecidoResponsavelFinanceiro() {
        return getFiltroTipoFavorecido().equals("RF");
    }
    
   public List getListaConsultaParceiro() {
        if (listaConsultaParceiro == null) {
            listaConsultaParceiro = new ArrayList<>(0);
        }
        return listaConsultaParceiro;
    }

    public void setListaConsultaParceiro(List listaConsultaParceiro) {
        this.listaConsultaParceiro = listaConsultaParceiro;
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

    public String getCampoConsultaParceiro() {
        if (campoConsultaParceiro == null) {
            campoConsultaParceiro = "";
        }
        return campoConsultaParceiro;
    }

    public void setCampoConsultaParceiro(String campoConsultaParceiro) {
        this.campoConsultaParceiro = campoConsultaParceiro;
    }
    
    protected List<PessoaVO> listaConsultaResponsavelFinanceiro;
    protected String valorConsultaResponsavelFinanceiro;
    protected String campoConsultaResponsavelFinanceiro;
    
    
    public void consultarResponsavelFinanceiro() {
        try {

            if (getValorConsultaResponsavelFinanceiro().trim().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            getFacadeFactory().getPessoaFacade().setIdEntidade("ResponsavelFinanceiro");
            getListaConsultaResponsavelFinanceiro().clear();
            if (getCampoConsultaResponsavelFinanceiro().equals("nome")) {
                setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
            }
            if (getCampoConsultaResponsavelFinanceiro().equals("nomeAluno")) {
                setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorNomeAlunoResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
            }
            if (getCampoConsultaResponsavelFinanceiro().equals("CPF")) {
                setListaConsultaResponsavelFinanceiro(getFacadeFactory().getPessoaFacade().consultaRapidaPorCpfResponsavelFinanceiro(getValorConsultaResponsavelFinanceiro(), this.getUnidadeEnsinoLogado().getCodigo(), false, getUsuarioLogado()));
            }

            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaResponsavelFinanceiro(new ArrayList<PessoaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    private List<SelectItem> tipoConsultaComboResponsavelFinanceiro;

    public List<SelectItem> getTipoConsultaComboResponsavelFinanceiro() {
        if (tipoConsultaComboResponsavelFinanceiro == null) {
            tipoConsultaComboResponsavelFinanceiro = new ArrayList<SelectItem>(0);
            tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nomeAluno", "Aluno"));
            tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("CPF", "CPF"));
        }
        return tipoConsultaComboResponsavelFinanceiro;
    }
    
    public void selecionarResponsavelFinanceiro() {
        try {
            PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiro");
            getContaPagarVO().setResponsavelFinanceiro(obj);   
            setCodigoFavorecido(getContaPagarVO().getResponsavelFinanceiro().getCodigo());
            setNomeFavorecido(getContaPagarVO().getResponsavelFinanceiro().getNome());
            getListaConsultaResponsavelFinanceiro().clear();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public List<PessoaVO> getListaConsultaResponsavelFinanceiro() {
        if (listaConsultaResponsavelFinanceiro == null) {
            listaConsultaResponsavelFinanceiro = new ArrayList<PessoaVO>(0);
        }
        return listaConsultaResponsavelFinanceiro;
    }

    public void setListaConsultaResponsavelFinanceiro(List<PessoaVO> listaConsultaResponsavelFinanceiro) {
        this.listaConsultaResponsavelFinanceiro = listaConsultaResponsavelFinanceiro;
    }

    public String getValorConsultaResponsavelFinanceiro() {
        if (valorConsultaResponsavelFinanceiro == null) {
            valorConsultaResponsavelFinanceiro = "";
        }
        return valorConsultaResponsavelFinanceiro;
    }

    public void setValorConsultaResponsavelFinanceiro(String valorConsultaResponsavelFinanceiro) {
        this.valorConsultaResponsavelFinanceiro = valorConsultaResponsavelFinanceiro;
    }

    public String getCampoConsultaResponsavelFinanceiro() {
        if (campoConsultaResponsavelFinanceiro == null) {
            campoConsultaResponsavelFinanceiro = "";
        }
        return campoConsultaResponsavelFinanceiro;
    }

    public void setCampoConsultaResponsavelFinanceiro(String campoConsultaResponsavelFinanceiro) {
        this.campoConsultaResponsavelFinanceiro = campoConsultaResponsavelFinanceiro;
    }
    
    public void consultarParceiro() {
        try {
            List objs = new ArrayList<>(0);
            if (getCampoConsultaParceiro().equals("nome")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiro().equals("razaoSocial")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaParceiro(objs);
            setMensagemID(
                    "msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList<>(0));
            setMensagemDetalhada(
                    "msg_erro", e.getMessage());
        }
    }

    public List<SelectItem> getTipoConsultaParceiro() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("razaoSocial", "Razão social"));
        return itens;
    }

    public void selecionarParceiro() {
        ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItem");
        getContaPagarVO().setParceiro(obj);
        setCodigoFavorecido(getContaPagarVO().getParceiro().getCodigo());
        setNomeFavorecido(getContaPagarVO().getParceiro().getNome());
        listaConsultaParceiro.clear();
        this.setValorConsultaParceiro("");
        this.setCampoConsultaParceiro("");
    }
    
    public void limparDadosParceiro(){
        getContaPagarVO().getParceiro().setCodigo(0);
        getContaPagarVO().getParceiro().setNome("");
    }
    
   
    
    public void limparDadosResponsavelFinanceiro(){ 
        getContaPagarVO().getResponsavelFinanceiro().setCodigo(0);
        getContaPagarVO().getResponsavelFinanceiro().setNome("");
        
    }

	public List<SelectItem> getListaSelectItemOperadoraCartao() {
		if (listaSelectItemOperadoraCartao == null) {
			listaSelectItemOperadoraCartao = new ArrayList<SelectItem>(0);
			try {
				List<OperadoraCartaoVO> operadoraCartaoVOs =  getFacadeFactory().getOperadoraCartaoFacade().consultarPorTipo(TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO.name(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
				listaSelectItemOperadoraCartao = UtilSelectItem.getListaSelectItem(operadoraCartaoVOs, "codigo", "nome");
			}catch (Exception e) {
			}
		}
		return listaSelectItemOperadoraCartao;
	}

	public void setListaSelectItemOperadoraCartao(List listaSelectItemOperadoraCartao) {
		this.listaSelectItemOperadoraCartao = listaSelectItemOperadoraCartao;
	}
    
	public void selecionarOperadoraCartao() throws Exception {
        try {
            getContaPagarVO().setOperadoraCartao(getFacadeFactory().getOperadoraCartaoFacade().consultarPorChavePrimaria(getContaPagarVO().getOperadoraCartao().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
            setCodigoFavorecido(getContaPagarVO().getOperadoraCartao().getCodigo());
            setNomeFavorecido(getContaPagarVO().getOperadoraCartao().getNome());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

	public DataModelo getDataModeloAluno() {
		if (dataModeloAluno == null) {
			dataModeloAluno = new DataModelo();
		}
		return dataModeloAluno;
	}

	public void setDataModeloAluno(DataModelo dataModeloAluno) {
		this.dataModeloAluno = dataModeloAluno;
	}
}
