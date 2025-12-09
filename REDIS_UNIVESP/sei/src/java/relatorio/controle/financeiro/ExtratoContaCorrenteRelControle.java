package relatorio.controle.financeiro;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ExtratoContaCorrenteVO;
import negocio.comuns.financeiro.enumerador.OrigemExtratoContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoSacadoExtratoContaCorrenteEnum;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.financeiro.ExtratoContaCorrenteRelVO;
import relatorio.negocio.jdbc.financeiro.ExtratoContaCorrenteRel;

@Controller("ExtratoContaCorrenteRelControle")
@Scope("viewScope")
@Lazy
public class ExtratoContaCorrenteRelControle extends SuperControleRelatorio implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -947648735418960778L;

    private Integer contaCorrente;
    private Date dataInicio;
    private Date dataFim;
    private Integer unidadeEnsino;
    private List<SelectItem> listaSelectItemUnidadeEnsino;
    private List<SelectItem> listaSelectItemContaCorrente;
    private List<SelectItem> listaSelectItemEntradaSaida;
    private List<SelectItem> listaSelectItemFormaPagamento;
    protected Boolean existeUnidadeEnsino;

    @PostConstruct
    public void inicializarUnidadeEnsino() {
        try {
            setUnidadeEnsino(getUnidadeEnsinoLogado().getCodigo());
            if (getUnidadeEnsino().intValue() == 0) {
                setExisteUnidadeEnsino(Boolean.FALSE);
            } else {
                setExisteUnidadeEnsino(Boolean.TRUE);
            }
        } catch (Exception e) {
            setExisteUnidadeEnsino(Boolean.FALSE);
        }
    }

    public Boolean getExisteUnidadeEnsino() {
        return existeUnidadeEnsino;
    }

    public void setExisteUnidadeEnsino(Boolean existeUnidadeEnsino) {
        this.existeUnidadeEnsino = existeUnidadeEnsino;
    }

    public void imprimirRelatorioExcel() {
        List<ExtratoContaCorrenteRelVO> listaObjetos = null;
        try {
            listaObjetos = getFacadeFactory().getExtratoContaCorrenteRelFacade().consultarDadosGeracaoRelatorio(getUnidadeEnsino(), getContaCorrente(), getDataInicio(), getDataFim(), false);
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().limparParametros();
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.EXCEL);
                getSuperParametroRelVO().setNomeDesignIreport(ExtratoContaCorrenteRel.designIReportRelatorio().replace(".jrxml", "Excel.jrxml"));
                adicionarParamentros(listaObjetos);
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

    public void adicionarParamentros(List<ExtratoContaCorrenteRelVO> listaObjetos) {
        
        getSuperParametroRelVO().setSubReport_Dir(ExtratoContaCorrenteRel.caminhoBaseRelatorio());
        getSuperParametroRelVO().setNomeUsuario(getUsuarioLogado().getNome());
        getSuperParametroRelVO().setTituloRelatorio("Relatório de Extrato Conta Corrente");
        getSuperParametroRelVO().setListaObjetos(listaObjetos);
        getSuperParametroRelVO().setCaminhoBaseRelatorio(getFacadeFactory().getContaReceberRelFacade().caminhoBaseRelatorio());
        getSuperParametroRelVO().setVersaoSoftware(getVersaoSistema());
        getSuperParametroRelVO().setPeriodo(String.valueOf(Uteis.getData(getDataInicio())) + "  a  " + String.valueOf(Uteis.getData(getDataFim())));
        for (SelectItem item : getListaSelectItemUnidadeEnsino()) {
            if (((Integer) item.getValue()).intValue() == getUnidadeEnsino().intValue()) {
                getSuperParametroRelVO().setUnidadeEnsino(item.getLabel());
                break;
            }
        }
        if (getContaCorrente() > 0) {
            for (SelectItem item : getListaSelectItemContaCorrente()) {
                if (((Integer) item.getValue()).intValue() == getContaCorrente().intValue()) {
                    getSuperParametroRelVO().setContaCorrente(item.getLabel());
                    break;
                }
            }
        } else {
            getSuperParametroRelVO().setContaCorrente("Todas");
        }

    }

    public void imprimirPDF() {
        List<ExtratoContaCorrenteRelVO> listaObjetos = null;
        try {
            listaObjetos = getFacadeFactory().getExtratoContaCorrenteRelFacade().consultarDadosGeracaoRelatorio(getUnidadeEnsino(), getContaCorrente(), getDataInicio(), getDataFim(), false);
            if (!listaObjetos.isEmpty()) {
                getSuperParametroRelVO().limparParametros();
                getSuperParametroRelVO().setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
                getSuperParametroRelVO().setNomeDesignIreport(ExtratoContaCorrenteRel.designIReportRelatorio());
                adicionarParamentros(listaObjetos);
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

    public Integer getContaCorrente() {
        if (contaCorrente == null) {
            contaCorrente = 0;
        }
        return contaCorrente;
    }

    public void setContaCorrente(Integer contaCorrente) {
        this.contaCorrente = contaCorrente;
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
            dataFim = new Date();
        }
        return dataFim;
    }

    public void setDataFim(Date dataFim) {
        this.dataFim = dataFim;
    }

    public Integer getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = 0;
        }
        return unidadeEnsino;
    }

    public void setUnidadeEnsino(Integer unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    @SuppressWarnings("unchecked")
    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<SelectItem>(0);
            List<UnidadeEnsinoVO> unidadeEnsinoVOs = null;
            try {
                unidadeEnsinoVOs = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome("", super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
                if (getUnidadeEnsinoLogado().getCodigo() == 0) {
                	listaSelectItemUnidadeEnsino.add(new SelectItem(0, ""));
                } else {
                	setUnidadeEnsino(unidadeEnsinoVOs.get(0).getCodigo());
                }
                for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
                    listaSelectItemUnidadeEnsino.add(new SelectItem(unidadeEnsinoVO.getCodigo(), unidadeEnsinoVO.getNome()));
                }
            } catch (Exception e) {

            }
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public void inicializarListaSelectItemContaCorrente() {
        if (listaSelectItemContaCorrente != null) {
            listaSelectItemContaCorrente.clear();
            listaSelectItemContaCorrente = null;
        }
        getListaSelectItemContaCorrente();
    }

    public List<SelectItem> getListaSelectItemContaCorrente() {
        if (listaSelectItemContaCorrente == null) {
            List<ContaCorrenteVO> contaCorrenteVOs = null;
            listaSelectItemContaCorrente = new ArrayList<SelectItem>(0);
            try {
            	contaCorrenteVOs  = getFacadeFactory().getContaCorrenteFacade().consultarPorNumero("", getUnidadeEnsino(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());            	
                //contaCorrenteVOs = getFacadeFactory().getContaCorrenteFacade().consultarContaCorrenteComboBoxPorUnidadeEnsino(getUnidadeEnsino(), false, getUsuarioLogado());
                listaSelectItemContaCorrente.add(new SelectItem(0, "Todas"));
                for (ContaCorrenteVO contaCorrenteVO : contaCorrenteVOs) {
                    if(!contaCorrenteVO.getContaCaixa()){
                        listaSelectItemContaCorrente.add(new SelectItem(contaCorrenteVO.getCodigo(), contaCorrenteVO.getDescricaoCompletaConta()));
                    }                    
                }
            } catch (Exception e) {

            } finally {
                if (contaCorrenteVOs != null) {
                    contaCorrenteVOs.clear();
                    contaCorrenteVOs = null;
                }
            }
        }
        return listaSelectItemContaCorrente;
    }

    public void setListaSelectItemContaCorrente(List<SelectItem> listaSelectItemContaCorrente) {
        this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
    }
    
    private List<ExtratoContaCorrenteRelVO> extratoContaCorrenteRelVOs;
    private Boolean usuarioPodeAlterarDataCompensacao;
    private Boolean usuarioPodeExcluirExtratoContaCorrente;
    private Boolean usuarioPodeIncluirExtratoContaCorrente;
    
    public void imprimirRelatorioTela() {        
        try {        	
        	getFacadeFactory().getExtratoContaCorrenteRelFacade().validarDados(getDataInicio(), getDataFim(), getContaCorrente(), true);        	
        	extratoContaCorrenteRelVOs = getFacadeFactory().getExtratoContaCorrenteRelFacade().consultarDadosGeracaoRelatorio(getUnidadeEnsino(), getContaCorrente(), getDataInicio(), getDataFim(), true);
            if (!extratoContaCorrenteRelVOs.isEmpty()) {            
                setMensagemID("msg_relatorio_ok");
            } else {
                setMensagemID("msg_relatorio_sem_dados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

	public List<ExtratoContaCorrenteRelVO> getExtratoContaCorrenteRelVOs() {
		if (extratoContaCorrenteRelVOs == null) {
			extratoContaCorrenteRelVOs = new ArrayList<ExtratoContaCorrenteRelVO>(0);
		}
		return extratoContaCorrenteRelVOs;
	}

	public void setExtratoContaCorrenteRelVOs(List<ExtratoContaCorrenteRelVO> extratoContaCorrenteRelVOs) {
		this.extratoContaCorrenteRelVOs = extratoContaCorrenteRelVOs;
	}

	public Boolean getUsuarioPodeAlterarDataCompensacao() {
		if (usuarioPodeAlterarDataCompensacao == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ExtratoContaCorrente_permitirAlterarDataCompensacao", getUsuarioLogado());				
				usuarioPodeAlterarDataCompensacao = true;
			}catch (Exception e) {
				usuarioPodeAlterarDataCompensacao = false;
			}
			
		}
		return usuarioPodeAlterarDataCompensacao;
	}

	public void setUsuarioPodeAlterarDataCompensacao(Boolean usuarioPodeAlterarDataCompensacao) {
		this.usuarioPodeAlterarDataCompensacao = usuarioPodeAlterarDataCompensacao;
	}

	public Boolean getUsuarioPodeExcluirExtratoContaCorrente() {
		if (usuarioPodeExcluirExtratoContaCorrente == null) {
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ExtratoContaCorrente_permitirExcluirExtratoContaCorrenteOrigemNaoLocalizada", getUsuarioLogado());				
				usuarioPodeExcluirExtratoContaCorrente = true;
			}catch (Exception e) {
				usuarioPodeExcluirExtratoContaCorrente = false;
			}
		}
		return usuarioPodeExcluirExtratoContaCorrente;
	}

	public void setUsuarioPodeExcluirExtratoContaCorrente(Boolean usuarioPodeExcluirExtratoContaCorrente) {
		this.usuarioPodeExcluirExtratoContaCorrente = usuarioPodeExcluirExtratoContaCorrente;
	}

	public Boolean getUsuarioPodeIncluirExtratoContaCorrente() {
		if (usuarioPodeIncluirExtratoContaCorrente == null) {			
			try {
				ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ExtratoContaCorrente_permitirIncluirExtratoContaCorrente", getUsuarioLogado());				
				usuarioPodeIncluirExtratoContaCorrente = true;
			}catch (Exception e) {
				usuarioPodeIncluirExtratoContaCorrente = false;
			}
		}
		return usuarioPodeIncluirExtratoContaCorrente;
	}

	public void setUsuarioPodeIncluirExtratoContaCorrente(Boolean usuarioPodeIncluirExtratoContaCorrente) {
		this.usuarioPodeIncluirExtratoContaCorrente = usuarioPodeIncluirExtratoContaCorrente;
	}
	
	private ExtratoContaCorrenteVO extratoContaCorrenteVO;	

	public ExtratoContaCorrenteVO getExtratoContaCorrenteVO() {		
		return extratoContaCorrenteVO;
	}

	public void setExtratoContaCorrenteVO(ExtratoContaCorrenteVO extratoContaCorrenteVO) {
		this.extratoContaCorrenteVO = extratoContaCorrenteVO;
	}
	
	
	public void novoExtrato() {
		ExtratoContaCorrenteRelVO extratoContaCorrenteRelVO = (ExtratoContaCorrenteRelVO)getRequestMap().get("conta");
		setExtratoContaCorrenteVO(new ExtratoContaCorrenteVO());
		getExtratoContaCorrenteVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
		getExtratoContaCorrenteVO().getResponsavel().setNome(getUsuarioLogado().getNome());
		getExtratoContaCorrenteVO().getContaCorrente().setCodigo(extratoContaCorrenteRelVO.getCodigoContaCorrente());
		getExtratoContaCorrenteVO().getUnidadeEnsino().setCodigo(getUnidadeEnsino());
		getExtratoContaCorrenteVO().setOrigemExtratoContaCorrente(OrigemExtratoContaCorrenteEnum.MANUAL);		
		getExtratoContaCorrenteVO().setTipoSacado(TipoSacadoExtratoContaCorrenteEnum.MANUAL);		
	}
	
	public void gravarExtrato() {
		try {
			setOncompleteModal("");
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ExtratoContaCorrente_permitirIncluirExtratoContaCorrente", getUsuarioLogado());
			if (Uteis.isAtributoPreenchido(getExtratoContaCorrenteVO().getFormaPagamento().getCodigo())) {
				getExtratoContaCorrenteVO().setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getExtratoContaCorrenteVO().getFormaPagamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			}
			getFacadeFactory().getExtratoContaCorrenteFacade().incluir(getExtratoContaCorrenteVO(), getUsuarioLogado());
			getExtratoContaCorrenteVO().setFormaPagamento(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(getExtratoContaCorrenteVO().getFormaPagamento().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
			getFacadeFactory().getExtratoContaCorrenteRelFacade().adicionarExtratoContaCorrente(getExtratoContaCorrenteRelVOs(), getExtratoContaCorrenteVO());			
			setOncompleteModal("RichFaces.$('panelIncluir').hide()");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
	
	
	public void excluirExtrato() {
		try {
			setOncompleteModal("");
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ExtratoContaCorrente_permitirExcluirExtratoContaCorrenteOrigemNaoLocalizada", getUsuarioLogado());
			getExtratoContaCorrenteVO().setDesconsiderarConciliacaoBancaria(true);
			getFacadeFactory().getExtratoContaCorrenteFacade().alterar(getExtratoContaCorrenteVO(), getUsuarioLogado());
			getFacadeFactory().getExtratoContaCorrenteRelFacade().removerExtratoContaCorrente(getExtratoContaCorrenteRelVOs(), getExtratoContaCorrenteVO());
			setOncompleteModal("RichFaces.$('panelExcluir').hide()");
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}
	
	public void alterarTodosExtrato() {
		try {
			ExtratoContaCorrenteRelVO extratoContaCorrenteRelVO = (ExtratoContaCorrenteRelVO)getRequestMap().get("conta");
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ExtratoContaCorrente_permitirAlterarDataCompensacao", getUsuarioLogado());
			getFacadeFactory().getExtratoContaCorrenteRelFacade().alterarTodosExtratoContaCorrente(extratoContaCorrenteRelVO, getUsuarioLogado());			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
		
	
	public void alterarExtrato() {
		try {		
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ExtratoContaCorrente_permitirAlterarDataCompensacao", getUsuarioLogado());
			getFacadeFactory().getExtratoContaCorrenteFacade().alterar(getExtratoContaCorrenteVO(), getUsuarioLogado());
			getExtratoContaCorrenteVO().setData(getExtratoContaCorrenteVO().getDataAlterar());
			for(ExtratoContaCorrenteRelVO extratoContaCorrenteRelVO: getExtratoContaCorrenteRelVOs()) {
				if(extratoContaCorrenteRelVO.getCodigoContaCorrente().equals(getExtratoContaCorrenteVO().getContaCorrente().getCodigo())) {	
					if(getExtratoContaCorrenteVO().getData().compareTo(extratoContaCorrenteRelVO.getDataInicio()) < 0 || 
						getExtratoContaCorrenteVO().getData().compareTo(extratoContaCorrenteRelVO.getDataFinal()) > 0) {
						getFacadeFactory().getExtratoContaCorrenteRelFacade().removerExtratoContaCorrente(extratoContaCorrenteRelVO, getExtratoContaCorrenteVO());							
					}else {
						Ordenacao.ordenarLista(extratoContaCorrenteRelVO.getExtratoContaCorrenteVOs(), "data");
					}
					break;
				}
			}
			setExtratoContaCorrenteVO(new ExtratoContaCorrenteVO());
			setMensagemID("msg_dados_gravados", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}		
	}

	public void validarExclusaoExtratoContaCorrente() {
		try {
			setOncompleteModal("");
			getFacadeFactory().getExtratoContaCorrenteFacade().validarExclusaoExtratoContaCorrente(getExtratoContaCorrenteVO(), getUsuarioLogado());
			setOncompleteModal("RichFaces.$('panelExcluir').show()");
			setMensagemID("msg_dados_excluidos", Uteis.SUCESSO);
		}catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}

	public List<SelectItem> getListaSelectItemEntradaSaida() {
		if (listaSelectItemEntradaSaida == null) {
			listaSelectItemEntradaSaida = new ArrayList<SelectItem>(0);
			listaSelectItemEntradaSaida.add(new SelectItem(TipoMovimentacaoFinanceira.SAIDA, TipoMovimentacaoFinanceira.SAIDA.getDescricao()));
			listaSelectItemEntradaSaida.add(new SelectItem(TipoMovimentacaoFinanceira.ENTRADA, TipoMovimentacaoFinanceira.ENTRADA.getDescricao()));
		}
		return listaSelectItemEntradaSaida;
	}

	public void setListaSelectItemEntradaSaida(List<SelectItem> listaSelectItemEntradaSaida) {
		this.listaSelectItemEntradaSaida = listaSelectItemEntradaSaida;
	}

	public List<SelectItem> getListaSelectItemFormaPagamento() {
		if (listaSelectItemFormaPagamento == null) {
			listaSelectItemFormaPagamento = new ArrayList<SelectItem>(0);
			try {				
				listaSelectItemFormaPagamento = UtilSelectItem.getListaSelectItem(getFacadeFactory().getFormaPagamentoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()), "codigo", "nome", false);
			}catch (Exception e) {
				
			}
		}
		return listaSelectItemFormaPagamento;
	}

	public void setListaSelectItemFormaPagamento(List<SelectItem> listaSelectItemFormaPagamento) {
		this.listaSelectItemFormaPagamento = listaSelectItemFormaPagamento;
	}
	

	public String navegarOrigemExtratoContaCorrente;

	public String getNavegarOrigemExtratoContaCorrente() {
		if (navegarOrigemExtratoContaCorrente == null) {
			navegarOrigemExtratoContaCorrente = "";
		}
		return navegarOrigemExtratoContaCorrente;
	}

	public void setNavegarOrigemExtratoContaCorrente(String navegarOrigemExtratoContaCorrente) {
		this.navegarOrigemExtratoContaCorrente = navegarOrigemExtratoContaCorrente;
	}
	
	
	public void realizarNavegacaoOrigemExtratoContaCorrente() {
		try {
			setNavegarOrigemExtratoContaCorrente("");
			ExtratoContaCorrenteVO extratoContaCorrenteVO = (ExtratoContaCorrenteVO) getRequestMap().get("extrato");
			if (extratoContaCorrenteVO.getPossuiOrigem()) {
				if(extratoContaCorrenteVO.getOrigemExtratoContaCorrente().equals(OrigemExtratoContaCorrenteEnum.PAGAMENTO)) {
					if(!getLoginControle().getPermissaoAcessoMenuVO().getPagamento()) {
						throw new Exception("O usuário "+getUsuarioLogado().getNome()+" não possui acesso a tela de PAGAMENTO.");
					}
					removerControleMemoriaFlashTela("NegociacaoPagamentoControle");
					context().getExternalContext().getSessionMap().put("pagamentoExtratoContaCorrente", extratoContaCorrenteVO.getCodigoOrigem());					
					setNavegarOrigemExtratoContaCorrente("abrirPopup('../negociacaoPagamentoForm.xhtml', 'NegociacaoPagamento', 950, 595);");
				}else if(extratoContaCorrenteVO.getOrigemExtratoContaCorrente().equals(OrigemExtratoContaCorrenteEnum.RECEBIMENTO)
						|| extratoContaCorrenteVO.getOrigemExtratoContaCorrente().equals(OrigemExtratoContaCorrenteEnum.COMPENSACAO_CARTAO)
						|| extratoContaCorrenteVO.getOrigemExtratoContaCorrente().equals(OrigemExtratoContaCorrenteEnum.COMPENSACAO_CHEQUE)) {
					if(!getLoginControle().getPermissaoAcessoMenuVO().getNegociacaoRecebimento()) {
						throw new Exception("O usuário "+getUsuarioLogado().getNome()+" não possui acesso a tela de RECEBIMENTO.");
					}
					removerControleMemoriaFlashTela("NegociacaoRecebimentoControle");
					context().getExternalContext().getSessionMap().put("negociacaoExtratoContaCorrente", extratoContaCorrenteVO.getCodigoOrigem());					
					setNavegarOrigemExtratoContaCorrente("abrirPopup('../negociacaoRecebimentoForm.xhtml', 'NegociacaoRecebimento', 950, 595);");
				}else if(extratoContaCorrenteVO.getOrigemExtratoContaCorrente().equals(OrigemExtratoContaCorrenteEnum.DEVOLUCAO_CHEQUE)) {
					if(!getLoginControle().getPermissaoAcessoMenuVO().getDevolucaoCheque()) {
						throw new Exception("O usuário "+getUsuarioLogado().getNome()+" não possui acesso a tela de DEVOLUÇÃO DE CHEQUE.");
					}
					removerControleMemoriaFlashTela("DevolucaoChequeControle");
					context().getExternalContext().getSessionMap().put("devolucaoChequeExtratoContaCorrente", extratoContaCorrenteVO.getCodigoOrigem());					
					setNavegarOrigemExtratoContaCorrente("abrirPopup('../devolucaoChequeForm.xhtml', 'DevolucaoCheque', 950, 595);");
				}else if(extratoContaCorrenteVO.getOrigemExtratoContaCorrente().equals(OrigemExtratoContaCorrenteEnum.MOVIMENTACAO_FINANCEIRA)) {
					if(!getLoginControle().getPermissaoAcessoMenuVO().getMovimentacaoFinanceira()) {
						throw new Exception("O usuário "+getUsuarioLogado().getNome()+" não possui acesso a tela de MOVIMENTAÇÃO FINANCEIRA.");
					}
					removerControleMemoriaFlashTela("MovimentacaoFinanceiraControle");
					context().getExternalContext().getSessionMap().put("movimentacaoFinanceiraExtratoContaCorrente", extratoContaCorrenteVO.getCodigoOrigem());					
					setNavegarOrigemExtratoContaCorrente("abrirPopupMaximizada('../movimentacaoFinanceiraForm.xhtml', 'MovimentacaoFinanceira');");
				}
			} else {
				throw new Exception("O extrato selecionado não possui origem válida no sistema, provavelmente esta origem foi excluida.");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
		
}
