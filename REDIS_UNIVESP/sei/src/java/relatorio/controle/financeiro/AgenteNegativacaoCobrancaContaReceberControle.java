package relatorio.controle.financeiro;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import jobs.RegistrarSerasaApiGeo;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.basico.enumeradores.TipoExigenciaDocumentoEnum;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaContaReceberVO;
import negocio.comuns.financeiro.AgenteNegativacaoCobrancaUnidadeEnsinoVO;
import negocio.comuns.financeiro.TipoDocumentoPendenciaAgenteCobrancaVO;
import negocio.comuns.financeiro.enumerador.IntegracaoNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.SituacaoParcelaRemoverSerasaApiGeo;
import negocio.comuns.financeiro.enumerador.TipoAgenteNegativacaoCobrancaContaReceberEnum;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.DiaSemanaJob;
import negocio.comuns.utilitarias.dominios.Horas;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import webservice.nfse.generic.AmbienteEnum;

@Controller("AgenteNegativacaoCobrancaContaReceberControle")
@Scope("viewScope")
@Lazy
public class AgenteNegativacaoCobrancaContaReceberControle extends SuperControleRelatorio {

	private static final long serialVersionUID = 1L;
	private AgenteNegativacaoCobrancaContaReceberVO agente;
	private RegistrarSerasaApiGeo registrarSerasaApiGeo;
	private List<TipoDocumentoVO> tipoDocumentoVOs;
	private List<SelectItem> listaSelectItemUnidadeEnsino;
	private Boolean marcarTodosTiposDocumento;
	
	 public void teste()  {
	    	try {
	    		ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguracaoPadraoSistema();
	    		RegistroExecucaoJobVO r = new RegistroExecucaoJobVO();
	    		r.setCodigoOrigem(6);
	    		r.setNome(JobsEnum.JOB_SERASA_API_GEO_REGISTRAR.getName());
	    		List<RegistroExecucaoJobVO> lista = new ArrayList<>();
	    		lista.add(r);
	    		registrarSerasaApiGeo = new  RegistrarSerasaApiGeo();
	    		registrarSerasaApiGeo.executeRegistarSerasaApiGeo(config, lista);
			} catch (Exception e) {
				// TODO: handle exception
			}
	    }
	
    public String novo() throws Exception {
        removerObjetoMemoria(this);
        removerObjetoMemoria(getAgente());
        setAgente(new AgenteNegativacaoCobrancaContaReceberVO());
        getAgente().setNovoObj(true);
    	consultarUnidadeEnsinoFiltroRelatorio("AgenteNegativacaoCobrancaContaReceberControle");
    	montarListaSelectItemUnidadeEnsino();
    	setMarcarTodasUnidadeEnsino(false);
    	marcarTodasUnidadesEnsinoAction();
    	consultarTipoDocumento();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("agenteNegativacaoCobrancaContaReceberForm");
    }
	
	public String editar()  {
		try {
			AgenteNegativacaoCobrancaContaReceberVO obj = (AgenteNegativacaoCobrancaContaReceberVO) context().getExternalContext().getRequestMap().get("agente");
	        context().getExternalContext().getSessionMap();
	        context().getExternalContext().getRequestMap();
	        setAgente(getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
	        consultarUnidadeEnsinoFiltroRelatorio("AgenteNegativacaoCobrancaContaReceberControle");
	        consultarTipoDocumento();
	        getUnidadeEnsinoVOs().stream().forEach(p->p.setFiltrarUnidadeEnsino(getAgente().getAgenteNegativacaoCobrancaUnidadeEnsinoVOs().stream().anyMatch(ancue -> ancue.getUnidadeEnsino().getCodigo().equals(p.getCodigo()))));
	        getTipoDocumentoVOs().stream().forEach(p->p.setFiltrarTipoDocumento(getAgente().getTipoDocumentoPendenciaAgenteCobrancaVOs().stream().anyMatch(tdpac -> tdpac.getTipoDocumento().getCodigo().equals(p.getCodigo()))));
	        montarListaSelectItemUnidadeEnsino();
	        setMensagemID("msg_dados_editar");	
		} catch (Exception e) {
			// TODO: handle exception
		}
        return Uteis.getCaminhoRedirecionamentoNavegacao("agenteNegativacaoCobrancaContaReceberForm");
    }

    public void gravar() {
        try {
            if (getAgente().isNovoObj()) {
                getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().incluir(getAgente(), getUsuarioLogado());
            } else {
                getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().alterar(getAgente(), getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String consultar() {
        try {
            super.consultar();
            List<AgenteNegativacaoCobrancaContaReceberVO> objs = new ArrayList<AgenteNegativacaoCobrancaContaReceberVO>(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorCodigo(new Integer(valorInt), true, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nome")) {
                objs = getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorNome(getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("tipo")) {
            	objs = getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().consultarPorTipo(getControleConsulta().getValorConsulta(), true, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return "";
        } catch (Exception e) {
            setListaConsulta(new ArrayList<AgenteNegativacaoCobrancaContaReceberVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public void excluir() {
        try {            
            getFacadeFactory().getAgenteNegativacaoCobrancaContaReceberFacade().excluir(agente, getUsuarioLogado());
            setAgente(new AgenteNegativacaoCobrancaContaReceberVO());
            setMensagemID("msg_dados_excluidos");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void limparCamposPossuiIntegracao(){
  	  try {            
  		  if(!getAgente().getPossuiIntegracao()) {
  			  getAgente().setAmbienteAgenteNegativacaoCobranca(AmbienteEnum.HOMOLOGACAO);
                getAgente().setIntegracao(IntegracaoNegativacaoCobrancaContaReceberEnum.NENHUM);
                getAgente().setApiKeySerasaApiGeo("");
                getAgente().setSenhaApiSerasaApiGeo("");
                getAgente().setCredorUnidadeEnsinoVO(new UnidadeEnsinoVO());                  
  		  }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
	}
    
    public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
        consultar();
    }
    
    public void verificarTodasUnidadesSelecionadas() {
    	getUnidadeEnsinoVOs().stream().filter(p-> p.getFiltrarUnidadeEnsino()).forEach(p->{
    		if(getAgente().getAgenteNegativacaoCobrancaUnidadeEnsinoVOs().stream().noneMatch(ancue -> ancue.getUnidadeEnsino().getCodigo().equals(p.getCodigo()))) {
    			AgenteNegativacaoCobrancaUnidadeEnsinoVO ancue = new AgenteNegativacaoCobrancaUnidadeEnsinoVO();
        		ancue.setUnidadeEnsino(p);
    			getAgente().getAgenteNegativacaoCobrancaUnidadeEnsinoVOs().add(ancue);
    		}
    	});
    	
    	getUnidadeEnsinoVOs().stream().filter(p-> !p.getFiltrarUnidadeEnsino()).forEach(p->{
    		getAgente().getAgenteNegativacaoCobrancaUnidadeEnsinoVOs().removeIf(ancue -> ancue.getUnidadeEnsino().getCodigo().equals(p.getCodigo()));
    	});
		
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
	
	public void limparUnidadeEnsino(){
		super.limparUnidadeEnsinos();
	}
	
	public List<SelectItem> getComboboxTipoAgenteNegativacaoCobrancaContaReceberEnum() {
		List<SelectItem> itens = new ArrayList<>();
		itens.add(new SelectItem(TipoAgenteNegativacaoCobrancaContaReceberEnum.NENHUM.name(), ""));
		if(!getAgente().getPossuiIntegracao()) {
			itens.add(new SelectItem(TipoAgenteNegativacaoCobrancaContaReceberEnum.AMBOS.name(), "Negativação/Cobrança"));	
		}
		itens.add(new SelectItem(TipoAgenteNegativacaoCobrancaContaReceberEnum.COBRANCA.name(), "Cobrança"));
		itens.add(new SelectItem(TipoAgenteNegativacaoCobrancaContaReceberEnum.NEGATIVACAO.name(), "Negativação"));
		return itens;
	}
	
	

    public void irPaginaInicial() throws Exception {
        removerObjetoMemoria(getAgente());
        //controleConsulta.setPaginaAtual(1);
        this.consultar();
    }

    public void irPaginaAnterior() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() - 1);
        this.consultar();
    }

    public void irPaginaPosterior() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getPaginaAtual() + 1);
        this.consultar();
    }

    public void irPaginaFinal() throws Exception {
        controleConsulta.setPaginaAtual(controleConsulta.getNrTotalPaginas());
        this.consultar();
    }
    
    public void consultarTipoDocumento() {
		try {
			getTipoDocumentoVOs().clear();
			getTipoDocumentoVOs().addAll(getFacadeFactory().getTipoDeDocumentoFacade().consultarPorTipoExigenciaDocumento(TipoExigenciaDocumentoEnum.EXIGENCIA_ALUNO, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getTipoDocumentoVOs().addAll(getFacadeFactory().getTipoDeDocumentoFacade().consultarPorTipoExigenciaDocumento(TipoExigenciaDocumentoEnum.EXIGENCIA_CURSO, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			//setTipoDocumentoVOs(getFacadeFactory().getTipoDeDocumentoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogadoClone()));
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setTipoDocumentoVOs(new ArrayList<TipoDocumentoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void verificarTodosTiposDocumentoSelecionados() {
		getTipoDocumentoVOs().stream().filter(p-> p.getFiltrarTipoDocumento()).forEach(p->{
    		if(getAgente().getTipoDocumentoPendenciaAgenteCobrancaVOs().stream().noneMatch(tdpac -> tdpac.getTipoDocumento().getCodigo().equals(p.getCodigo()))) {
    			TipoDocumentoPendenciaAgenteCobrancaVO tdpac = new TipoDocumentoPendenciaAgenteCobrancaVO();
    			tdpac.setTipoDocumento(p);
    			getAgente().getTipoDocumentoPendenciaAgenteCobrancaVOs().add(tdpac);
    		}
    	});
    	
		getTipoDocumentoVOs().stream().filter(p-> !p.getFiltrarTipoDocumento()).forEach(p->{
    		getAgente().getTipoDocumentoPendenciaAgenteCobrancaVOs().removeIf(tdpac -> tdpac.getTipoDocumento().getCodigo().equals(p.getCodigo()));
    	});
		
	}

	public void marcarTodosTiposDocumentoAction() {
		for (TipoDocumentoVO tipoDocumento : getTipoDocumentoVOs()) {
			if (getMarcarTodosTiposDocumento()) {
				tipoDocumento.setFiltrarTipoDocumento(Boolean.TRUE);
			} else {
				tipoDocumento.setFiltrarTipoDocumento(Boolean.FALSE);
			}
		}
		verificarTodosTiposDocumentoSelecionados();
	}
	
	public void limparTiposDocumentoAction(){
		setMarcarTodosTiposDocumento(false);
		marcarTodosTiposDocumentoAction();
	}
	
	public void montarListaSelectItemUnidadeEnsino() throws Exception {
		try {
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				setListaSelectItemUnidadeEnsino(new ArrayList<SelectItem>());
				getListaSelectItemUnidadeEnsino().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
				getAgente().setCredorUnidadeEnsinoVO(getUnidadeEnsinoLogadoClone());
				return;
			}
			List<UnidadeEnsinoVO> resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
		} catch (Exception e) {
			throw e;
		}
	}
    
    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        //itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nome", "Nome"));
        itens.add(new SelectItem("tipo", "Tipo"));
        return itens;
    }

    public void limparConsulta() {
    	getControleConsulta().setValorConsulta("");
    }
    
	public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList<AgenteNegativacaoCobrancaContaReceberVO>(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("agenteNegativacaoCobrancaContaReceberCons");
    }

	public AgenteNegativacaoCobrancaContaReceberVO getAgente() {
		if (agente == null) {
			agente = new AgenteNegativacaoCobrancaContaReceberVO();
		}
		return agente;
	}

	public void setAgente(AgenteNegativacaoCobrancaContaReceberVO agente) {
		this.agente = agente;
	}
	
	public void realizarSelecaoCheckboxMarcarDesmarcarTodosTipoOrigem() {
		if (agente.getMarcarTodosTipoOrigem()) {
			agente.realizarSelecaoTodasOrigens(true);
		} else {
			agente.realizarSelecaoTodasOrigens(false);
		}
	}
	
	public List<SelectItem> getComboDiaSemana() {
		return DiaSemanaJob.getComboDiaSemanaJob();
	}
	
	public List<SelectItem> getComboHoras() {
		return Horas.getComboHoras();
	}
	
	public List<SelectItem> getComboSituacaoParcelaRemoverSerasaApiGeo() {
		return SituacaoParcelaRemoverSerasaApiGeo.getComboSituacaoParcelaRemoverSerasaApiGeo();
	}
	
	public List<SelectItem> getListaSelectItemUnidadeEnsino() {
		if(listaSelectItemUnidadeEnsino == null) {
			listaSelectItemUnidadeEnsino = new ArrayList<>();
		}
		return listaSelectItemUnidadeEnsino;
	}

	public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
		this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
	}

	public List<TipoDocumentoVO> getTipoDocumentoVOs() {
		if (tipoDocumentoVOs == null) {
			tipoDocumentoVOs = new ArrayList<TipoDocumentoVO>(0);
		}
		return tipoDocumentoVOs;
	}

	public void setTipoDocumentoVOs(List<TipoDocumentoVO> tipoDocumentoVOs) {
		this.tipoDocumentoVOs = tipoDocumentoVOs;
	}
	
	public Boolean getMarcarTodosTiposDocumento() {
		if(marcarTodosTiposDocumento == null) {
			marcarTodosTiposDocumento = Boolean.FALSE;
		}
		return marcarTodosTiposDocumento;
	}

	public void setMarcarTodosTiposDocumento(Boolean marcarTodosTiposDocumento) {
		this.marcarTodosTiposDocumento = marcarTodosTiposDocumento;
	}
	
	public List<SelectItem> getListaSelectItemSituacaoParcelaNegociada() {
		List<SelectItem> itens = new ArrayList<SelectItem>(0);
		itens.add(new SelectItem("PR", "Pelo Menos 1 Parcela Negociada Recebida"));
		itens.add(new SelectItem("TR", "Todas Parcelas Negociadas Recebidas"));
		return itens;
	}

}