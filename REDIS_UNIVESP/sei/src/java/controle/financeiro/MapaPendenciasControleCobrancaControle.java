package controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.MapaPendenciasControleCobrancaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoBoletoBancario;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

@Controller("MapaPendenciasControleCobrancaControle")
@Scope("viewScope")
@Lazy
public class MapaPendenciasControleCobrancaControle extends SuperControle implements Serializable {

    private List<MapaPendenciasControleCobrancaVO> mapaPendenciasControleCobrancaVOs;
    private MapaPendenciasControleCobrancaVO mapaPendenciasControleCobrancaVO;
    private Boolean marcarTodos;
    private List<ContaReceberVO> contaReceberVOs;
    protected List<SelectItem> listaSelectItemPlanoDesconto;
    private List<SelectItem> listaSelectItemDescontoProgresivo;
    private PlanoDescontoVO planoDescontoVO;
    private DescontoProgressivoVO descontoProgressivoVO;
    private Date novaDataVencimento;
    private String matriculaConsulta;
    private String nomeConsulta;
    private Boolean todoPeriodo;
    private Date dataInicio;
    private Date dataFim;
    private String ano;
    private String semestre;
    private String campoConsultaPeriodo;
    private ContaReceberVO contaReceber;
    private String onCompleteModalContaReceber;
    

    public MapaPendenciasControleCobrancaControle() throws Exception {
        //obterUsuarioLogado();

        setMensagemID("msg_entre_prmconsulta");
    }

    public String consultar() {
        try {
            setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarTodasPendencias(getMatriculaConsulta(), getNomeConsulta(), getTodoPeriodo(), getDataInicio(), getDataFim(), getAno(), getSemestre(), getCampoConsultaPeriodo(), false, getConfiguracaoFinanceiroPadraoSistema(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
            setMensagemID("msg_dados_consultados");
//            return "consultar";
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public void criarContasReceberBaseadasNasPendencias() {
        try {
            ContaCorrenteVO contaCorrente = new ContaCorrenteVO();
            for (MapaPendenciasControleCobrancaVO mpccVO : getMapaPendenciasControleCobrancaVOs()) {
                if (mpccVO.getSelecionado()) {
                    //Double valorDiferenca = mpccVO.getContaReceber().getValor() - mpccVO.getContaReceber().getValorRecebido() - mpccVO.getContaReceber().getValorDescontoRecebido();
                    getFacadeFactory().getContaReceberFacade().carregarDados(mpccVO.getContaReceber(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
                    
                    //contaCorrente = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getConfiguracaoFinanceiroPadraoSistema().getContaCorrentePadraoMensalidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                    contaCorrente = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(mpccVO.getContaReceber().getContaCorrente(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                    MatriculaVO matricula = null;
                    if (!mpccVO.getContaReceber().getMatriculaAluno().getMatricula().equals("")) {
                    	getFacadeFactory().getMatriculaFacade().carregarDados(mpccVO.getContaReceber().getMatriculaAluno(), getUsuarioLogado());
                    	matricula = mpccVO.getContaReceber().getMatriculaAluno();
                    }                    
//                  getFacadeFactory().getContaReceberFacade().criarContaReceber(null, mpccVO.getContaReceber().getParceiroVO(), mpccVO.getContaReceber().getPessoa(),
//                  mpccVO.getMatricula().getUnidadeEnsino(), contaCorrente, Integer.valueOf(mpccVO.getContaReceber().getCodOrigem()), TipoOrigemContaReceber.OUTROS.getValor(),
//                  getNovaDataVencimento(), getNovaDataVencimento(), mpccVO.getValorDiferenca(), getConfiguracaoFinanceiroPadraoSistema().getCentroReceitaParcelaAvulsaControleCobranca().getCodigo(), 1, 1, TipoBoletoBancario.OUTROS.getValor(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), mpccVO.getContaReceber().getFornecedor());
//                  getFacadeFactory().getMapaPendenciasControleCobrancaFacade().excluir(mpccVO, getUsuarioLogado());
                 // Foi alterado o parametro codorigem para codigo, possibilitando o rastreamento da origem da parcela extra
                    getFacadeFactory().getContaReceberFacade().criarContaReceber(matricula, mpccVO.getContaReceber().getParceiroVO(), mpccVO.getContaReceber().getPessoa(),
                            mpccVO.getMatricula().getUnidadeEnsino(), contaCorrente, Integer.valueOf(mpccVO.getContaReceber().getCodigo()), TipoOrigemContaReceber.OUTROS.getValor(),
                            getNovaDataVencimento(), getNovaDataVencimento(), mpccVO.getValorDiferenca(), getConfiguracaoFinanceiroPadraoSistema().getCentroReceitaParcelaAvulsaControleCobranca().getCodigo(), 1, 1, TipoBoletoBancario.OUTROS.getValor(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), mpccVO.getContaReceber().getFornecedor(), " Origem - Mapa Pendência Controle Cobrança - Conta Originou - " + mpccVO.getContaReceber().getNossoNumero());
                    getFacadeFactory().getMapaPendenciasControleCobrancaFacade().excluir(mpccVO, getUsuarioLogado());
                }
            }
            setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarTodasPendencias(getMatriculaConsulta(), getNomeConsulta(), getTodoPeriodo(), getDataInicio(), getDataFim(), getAno(), getSemestre(), getCampoConsultaPeriodo(), false, getConfiguracaoFinanceiroPadraoSistema(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
            contaCorrente = null;
            setMensagemID("msg_contareCeber_contasCriadas");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void isentarContasReceberBaseadasNasPendencias() {
        try {
            ControleAcesso.verificarPermissaoUsuarioFuncionalidade("ContaReceber_IsentarContas", getUsuarioLogado());
            for (MapaPendenciasControleCobrancaVO mpccVO : getMapaPendenciasControleCobrancaVOs()) {
                if (mpccVO.getSelecionado()) {
                    getFacadeFactory().getMapaPendenciasControleCobrancaFacade().excluir(mpccVO, getUsuarioLogado());
                }
            }
            setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarTodasPendencias(getMatriculaConsulta(), getNomeConsulta(), getTodoPeriodo(), getDataInicio(), getDataFim(), getAno(), getSemestre(), getCampoConsultaPeriodo(), false, getConfiguracaoFinanceiroPadraoSistema(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));            
            setMensagemID("msg_contareCeber_dadosIsentados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void setMapaPendenciasControleCobrancaVOs(List<MapaPendenciasControleCobrancaVO> mapaPendenciasControleCobrancaVOs) {
        this.mapaPendenciasControleCobrancaVOs = mapaPendenciasControleCobrancaVOs;
    }

    public List<MapaPendenciasControleCobrancaVO> getMapaPendenciasControleCobrancaVOs() {
        if (mapaPendenciasControleCobrancaVOs == null) {
            mapaPendenciasControleCobrancaVOs = new ArrayList<MapaPendenciasControleCobrancaVO>(0);
        }
        return mapaPendenciasControleCobrancaVOs;
    }

    public void setMarcarTodos(Boolean marcarTodos) {
        this.marcarTodos = marcarTodos;
    }

    public Boolean getMarcarTodos() {
        if (marcarTodos == null) {
            marcarTodos = false;
        }
        return marcarTodos;
    }

    public void selecionarTodos() {
        if (getMarcarTodos() == true) {
            for (MapaPendenciasControleCobrancaVO mapaPendenciasControleCobrancaVO : getMapaPendenciasControleCobrancaVOs()) {
                mapaPendenciasControleCobrancaVO.setSelecionado(true);
            }
        } else {
            for (MapaPendenciasControleCobrancaVO mapaPendenciasControleCobrancaVO : getMapaPendenciasControleCobrancaVOs()) {
                mapaPendenciasControleCobrancaVO.setSelecionado(false);
            }
        }
    }

    public MapaPendenciasControleCobrancaVO getMapaPendenciasControleCobrancaVO() {
        if (mapaPendenciasControleCobrancaVO == null) {
            mapaPendenciasControleCobrancaVO = new MapaPendenciasControleCobrancaVO();
        }
        return mapaPendenciasControleCobrancaVO;
    }

    public void setMapaPendenciasControleCobrancaVO(MapaPendenciasControleCobrancaVO mapaPendenciasControleCobrancaVO) {
        this.mapaPendenciasControleCobrancaVO = mapaPendenciasControleCobrancaVO;
    }

    public List<ContaReceberVO> getContaReceberVOs() {
        if (contaReceberVOs == null) {
            contaReceberVOs = new ArrayList<ContaReceberVO>();
        }
        return contaReceberVOs;
    }

    public void setContaReceberVOs(List<ContaReceberVO> contaReceberVOs) {
        this.contaReceberVOs = contaReceberVOs;
    }

    public void removerMapaPendenciasControleCobrancaVOs() {
        int x = 0;
        for (MapaPendenciasControleCobrancaVO mp : getMapaPendenciasControleCobrancaVOs()) {
            if (mp.getCodigo().equals(getMapaPendenciasControleCobrancaVO().getCodigo())) {
                getMapaPendenciasControleCobrancaVOs().remove(x);
                return;
            }
            x++;
        }
    }

    public void gravarAlteracaoContaReceber() {
        try {
            getFacadeFactory().getMapaPendenciasControleCobrancaFacade().executarAplicacaoDescontosContaReceber(getMapaPendenciasControleCobrancaVO(), getContaReceberVOs(), getPlanoDescontoVO(), getDescontoProgressivoVO(), getUsuarioLogado());
            removerMapaPendenciasControleCobrancaVOs();
            setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public String voltarMapaPendencia() {
        setMapaPendenciasControleCobrancaVO(null);
        removerObjetoMemoria(getPlanoDescontoVO());
        removerObjetoMemoria(getDescontoProgressivoVO());
        Uteis.liberarListaMemoria(getContaReceberVOs());
        Uteis.liberarListaMemoria(getListaSelectItemDescontoProgresivo());
        Uteis.liberarListaMemoria(getListaSelectItemPlanoDesconto());
        return Uteis.getCaminhoRedirecionamentoNavegacao("mapaPendenciasControleCobrancaForm");
    }

    public String consultarContarReceberAluno() {
        try {
            setMapaPendenciasControleCobrancaVO((MapaPendenciasControleCobrancaVO) context().getExternalContext().getRequestMap().get("mapaPendencias"));
            setContaReceberVOs(getFacadeFactory().getContaReceberFacade().consultaCompletaPorMatriculaPeriodoTipoMensalidade(getMapaPendenciasControleCobrancaVO().getContaReceber().getMatriculaPeriodo(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            montarListaSelectItemDescontoProgressivo();
            montarListaSelectItemPlanoDesconto();
            setMensagemID("msg_dados_editar",Uteis.ALERTA);
//            return "editar";
            return Uteis.getCaminhoRedirecionamentoNavegacao("aplicarDescontoContaReceberForm");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
//            return "consultar";
            return Uteis.getCaminhoRedirecionamentoNavegacao("mapaPendenciasControleCobrancaForm");
        }

    }
    
    public void selecionarPendenciaCobranca() {
        try {
            setMapaPendenciasControleCobrancaVO((MapaPendenciasControleCobrancaVO) context().getExternalContext().getRequestMap().get("mapaPendencias"));
            getContaReceber().setDataVencimento(new Date());
            getContaReceber().setValor(getMapaPendenciasControleCobrancaVO().getValorDiferenca());
            getContaReceber().setData(new Date());
            getContaReceber().setMatriculaAluno(getMapaPendenciasControleCobrancaVO().getMatricula());
            setMensagemID("msg_dados_editar",Uteis.ALERTA);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }

    }
    
    public void criarContaReceber(){
    	try {
    		validarDadosContaCorrente(getContaReceber());
    		ContaCorrenteVO contaCorrente = new ContaCorrenteVO();
    		getFacadeFactory().getContaReceberFacade().carregarDados(getMapaPendenciasControleCobrancaVO().getContaReceber(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            contaCorrente = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getConfiguracaoFinanceiroPadraoSistema().getContaCorrentePadraoMensalidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            UnidadeEnsinoVO unid = null;
            if (!getMapaPendenciasControleCobrancaVO().getMatricula().getMatricula().equals("")) {
            	unid = getMapaPendenciasControleCobrancaVO().getMatricula().getUnidadeEnsino();
            } else {
            	unid = getMapaPendenciasControleCobrancaVO().getContaReceber().getUnidadeEnsino();
            }
            Integer codOrigem = 0;
            if (!getMapaPendenciasControleCobrancaVO().getContaReceber().getCodOrigem().equals("")) {
            	codOrigem = Integer.valueOf(getMapaPendenciasControleCobrancaVO().getContaReceber().getCodOrigem());
            }
            ContaReceberVO obj = getFacadeFactory().getContaReceberFacade().criarContaReceberSemInclusao(null, getMapaPendenciasControleCobrancaVO().getContaReceber().getParceiroVO(), getMapaPendenciasControleCobrancaVO().getContaReceber().getPessoa(),
            		unid, unid, contaCorrente, codOrigem, TipoOrigemContaReceber.OUTROS.getValor(),
                    getNovaDataVencimento(), getNovaDataVencimento(), getMapaPendenciasControleCobrancaVO().getValorDiferenca(), getConfiguracaoFinanceiroPadraoSistema().getCentroReceitaParcelaAvulsaControleCobranca().getCodigo(), 1, 1, TipoBoletoBancario.OUTROS.getValor(), ano, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado(), getMapaPendenciasControleCobrancaVO().getContaReceber().getFornecedor());
            obj.setDataVencimento(getContaReceber().getDataVencimento());
            obj.setValor(getContaReceber().getValor());
            obj.setValorDesconto(getContaReceber().getValorDesconto());
            obj.setTipoDesconto("VA");
            getFacadeFactory().getContaReceberFacade().incluir(obj, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
            getFacadeFactory().getMapaPendenciasControleCobrancaFacade().excluir(getMapaPendenciasControleCobrancaVO(), getUsuarioLogado());
            setMapaPendenciasControleCobrancaVOs(getFacadeFactory().getMapaPendenciasControleCobrancaFacade().consultarTodasPendencias(getMatriculaConsulta(), getNomeConsulta(), getTodoPeriodo(), getDataInicio(), getDataFim(), getAno(), getSemestre(), getCampoConsultaPeriodo(), false, getConfiguracaoFinanceiroPadraoSistema(), getUnidadeEnsinoLogado().getCodigo(), getUsuarioLogado()));
            contaCorrente = null;
            setContaReceber(new ContaReceberVO());
            setOnCompleteModalContaReceber("RichFaces.$('panelGerarParcelaAlteracoes').hide();");
            setMensagemID("msg_contareCeber_contasCriadas");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void validarDadosContaCorrente(ContaReceberVO obj) throws Exception{
    	try {
			if(obj.getDataVencimento().before(Uteis.getDateSemHora(new Date()))){
				throw new Exception("A nova Data de Vencimento não pode ser anterior ao dia de hoje");
			}
			if(obj.getValor() == 0.0){
				throw new Exception("A Conta Receber não pode ser gerada com Valor 0.0. Neste caso isente a Conta.");
			}
			if(obj.getValorDesconto() > 0.0){
				if(obj.getValor().compareTo(obj.getValorDesconto()) == 0){
					throw new Exception("A Conta Receber não pode ser gerada com Valor de Valor do Descontos iguais. Neste caso isente a Conta.");
				}
			}
		} catch (Exception e) {
			throw e;
		}
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>PlanoDesconto</code>.
     */
    public void montarListaSelectItemPlanoDesconto(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        SelectItemOrdemValor ordenador = null;
        try {
            resultadoConsulta = consultarPlanoDescontoPorNome(prm);
            i = resultadoConsulta.iterator();
            getListaSelectItemPlanoDesconto().clear();
            getListaSelectItemPlanoDesconto().add(new SelectItem(0, ""));
            while (i.hasNext()) {
                PlanoDescontoVO obj = (PlanoDescontoVO) i.next();
                getListaSelectItemPlanoDesconto().add(new SelectItem(obj.getCodigo(), obj.getNome()));
                removerObjetoMemoria(obj);
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) getListaSelectItemPlanoDesconto(), ordenador);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
            ordenador = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>PlanoDesconto</code>. Buscando todos os
     * objetos correspondentes a entidade <code>PlanoDesconto</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemPlanoDesconto() {
        try {
            montarListaSelectItemPlanoDesconto("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List consultarPlanoDescontoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getPlanoDescontoFacade().consultarPorNomeSomenteAtiva(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    public void montarListaSelectItemDescontoProgressivo(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        SelectItemOrdemValor ordenador = null;
        try {
            resultadoConsulta = consultarDescontoProgressivoPorNome(prm);
            i = resultadoConsulta.iterator();
            getListaSelectItemDescontoProgresivo().clear();
            getListaSelectItemDescontoProgresivo().add(new SelectItem(0, ""));
            while (i.hasNext()) {
                DescontoProgressivoVO obj = (DescontoProgressivoVO) i.next();
                getListaSelectItemDescontoProgresivo().add(new SelectItem(obj.getCodigo(), obj.getNome()));
                removerObjetoMemoria(obj);
            }
            ordenador = new SelectItemOrdemValor();
            Collections.sort((List) getListaSelectItemDescontoProgresivo(), ordenador);            
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    public void montarListaSelectItemDescontoProgressivo() {
        try {
            montarListaSelectItemDescontoProgressivo("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }
    
    public List getTipoConsultaComboPeriodo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("DATA_VENCIMENTO", "Data Vencimetno"));
        itens.add(new SelectItem("DATA_PAGAMENTO", "Data Pagamento"));
        itens.add(new SelectItem("DATA_PROCESSAMENTO", "Data Processamento"));
        itens.add(new SelectItem("ANO_SEMESTRE", "Ano/Semestre"));
        return itens;
    }

    public List getTipoConsultaComboSemestre() {
    	List<SelectItem> itens = new ArrayList<SelectItem>(0);
    	itens.add(new SelectItem("", ""));
    	itens.add(new SelectItem("1", "1º Semestre"));
    	itens.add(new SelectItem("2", "2º Semestre"));
    	return itens;
    }
    

    public List consultarDescontoProgressivoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getDescontoProgressivoFacade().consultarPorNomeAtivos(nomePrm, false, getUsuarioLogado());
        return lista;
    }

    public List getListaSelectItemPlanoDesconto() {
        if (listaSelectItemPlanoDesconto == null) {
            listaSelectItemPlanoDesconto = new ArrayList();
        }
        return listaSelectItemPlanoDesconto;
    }

    public void setListaSelectItemPlanoDesconto(List listaSelectItemPlanoDesconto) {
        this.listaSelectItemPlanoDesconto = listaSelectItemPlanoDesconto;
    }

    public List getListaSelectItemDescontoProgresivo() {
        if (listaSelectItemDescontoProgresivo == null) {
            listaSelectItemDescontoProgresivo = new ArrayList();
        }
        return listaSelectItemDescontoProgresivo;
    }

    public void setListaSelectItemDescontoProgresivo(List listaSelectItemDescontoProgresivo) {
        this.listaSelectItemDescontoProgresivo = listaSelectItemDescontoProgresivo;
    }

    public DescontoProgressivoVO getDescontoProgressivoVO() {
        if (descontoProgressivoVO == null) {
            descontoProgressivoVO = new DescontoProgressivoVO();
        }
        return descontoProgressivoVO;
    }

    public void setDescontoProgressivoVO(DescontoProgressivoVO descontoProgressivoVO) {
        this.descontoProgressivoVO = descontoProgressivoVO;
    }

    public PlanoDescontoVO getPlanoDescontoVO() {
        if (planoDescontoVO == null) {
            planoDescontoVO = new PlanoDescontoVO();
        }
        return planoDescontoVO;
    }

    public void setPlanoDescontoVO(PlanoDescontoVO planoDescontoVO) {
        this.planoDescontoVO = planoDescontoVO;
    }

    /**
     * @return the novaDataVencimento
     */
    public Date getNovaDataVencimento() {
        if(novaDataVencimento == null) {
            novaDataVencimento = new Date();
        }
        return novaDataVencimento;
    }

    /**
     * @param novaDataVencimento the novaDataVencimento to set
     */
    public void setNovaDataVencimento(Date novaDataVencimento) {
        this.novaDataVencimento = novaDataVencimento;
    }

	public String getMatriculaConsulta() {
		if (matriculaConsulta == null) {
			matriculaConsulta = "";
		}
		return matriculaConsulta;
	}

	public void setMatriculaConsulta(String matriculaConsulta) {
		this.matriculaConsulta = matriculaConsulta;
	}

	public String getNomeConsulta() {
		if (nomeConsulta == null) {
			nomeConsulta = "";
		}
		return nomeConsulta;
	}

	public void setNomeConsulta(String nomeConsulta) {
		this.nomeConsulta = nomeConsulta;
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

	public String getAno() {
		if (ano == null) {
			ano = "";
		}
		return ano;
	}

	public void setAno(String ano) {
		this.ano = ano;
	}

	public String getSemestre() {
		if (semestre == null) {
			semestre = "";
		}
		return semestre;
	}

	public void setSemestre(String semestre) {
		this.semestre = semestre;
	}

	public Boolean getTodoPeriodo() {
		if (todoPeriodo == null) {
			todoPeriodo = Boolean.TRUE;
		}
		return todoPeriodo;
	}

	public void setTodoPeriodo(Boolean todoPeriodo) {
		this.todoPeriodo = todoPeriodo;
	}

	public String getCampoConsultaPeriodo() {
		if (campoConsultaPeriodo == null) {
			campoConsultaPeriodo = "DATA_VENCIMENTO";
		}
		return campoConsultaPeriodo;
	}

	public void setCampoConsultaPeriodo(String campoConsultaPeriodo) {
		this.campoConsultaPeriodo = campoConsultaPeriodo;
	}
	
	public Boolean getApresentarFiltroData() {
		return !getTodoPeriodo() && getCampoConsultaPeriodo().contains("DATA_");
	}

	public Boolean getApresentarFiltroAnoSemestre() {
		return !getTodoPeriodo() && getCampoConsultaPeriodo().equals("ANO_SEMESTRE");
	}
	
	public Boolean getApresentarFiltroPeriodo() {
		return !getTodoPeriodo();
	}

	public ContaReceberVO getContaReceber() {
		if(contaReceber == null){
			contaReceber = new ContaReceberVO();
		}
		return contaReceber;
	}

	public void setContaReceber(ContaReceberVO contaReceber) {
		this.contaReceber = contaReceber;
	}

	public String getOnCompleteModalContaReceber() {
		if(onCompleteModalContaReceber == null){
			onCompleteModalContaReceber = "";
		}
		return onCompleteModalContaReceber;
	}

	public void setOnCompleteModalContaReceber(String onCompleteModalContaReceber) {
		this.onCompleteModalContaReceber = onCompleteModalContaReceber;
	}
	
	
}
