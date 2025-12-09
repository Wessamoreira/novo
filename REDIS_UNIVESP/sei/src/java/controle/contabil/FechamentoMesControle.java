package controle.contabil;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas fechamentoMesForm.jsp
 * fechamentoMesCons.jsp) com as funcionalidades da classe <code>fechamentoMes</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see fechamentoMes
 * @see fechamentoMesVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.model.SelectItem;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.contabil.FechamentoMesContaCaixaVO;
import negocio.comuns.contabil.FechamentoMesUnidadeEnsinoVO;
import negocio.comuns.contabil.FechamentoMesVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;

@Controller("FechamentoMesControle")
@Scope("viewScope")
@Lazy
public class FechamentoMesControle extends SuperControle implements Serializable {

    private FechamentoMesVO fechamentoMesVO;
    private FechamentoMesVO fechamentoMesVOAnterior;
	protected String valorConsultaMes;
	protected String valorConsultaAno;
	protected UnidadeEnsinoVO unidadeEnsinoVO;
	protected ContaCorrenteVO contaCaixa;
	protected List<ContaCorrenteVO> contaCorrenteVOs;
	protected Boolean marcarTodasContas;

	public FechamentoMesControle() throws Exception {
        setControleConsulta(new ControleConsulta());
        getControleConsulta().setCampoConsulta("competencia");
        setFechamentoMesVO(new FechamentoMesVO());
        getFechamentoMesVO().setUsuario(getUsuarioLogadoClone());
        setMensagemID("msg_entre_prmconsulta");
    }

    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>fechamentoMes</code> para edição pelo
     * usuário da aplicação.
     */
    public String novo() {
        removerObjetoMemoria(this);
        setFechamentoMesVO(new FechamentoMesVO());
        getFechamentoMesVO().setUsuario(getUsuarioLogadoClone());
        inicializarListasSelectItemTodosComboBox();
        limparUnidadesEnsinoSelecionadas();
        limparContasCaixaSelecionadas();
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("fechamentoMesForm.xhtml");
    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>fechamentoMes</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa
     * disponibilizá-lo para edição.
     */
    public String editar() {
    	try {
    		FechamentoMesVO obj = (FechamentoMesVO) context().getExternalContext().getRequestMap().get("fechamentoMesItens");
    		obj.setNovoObj(Boolean.FALSE);
    		setFechamentoMesVO(getFacadeFactory().getFechamentoMesFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
    		setFechamentoMesVOAnterior(getFacadeFactory().getFechamentoMesFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        	inicializarListasSelectItemTodosComboBox();
        	selecionarUnidadesEnsinoFechamentoMes();
        	consultarContasCaixasUnidadeEnsino();
        	selecionarContasCaixaFechamentoMes();
        	setMensagemID("msg_dados_editar");
        	return Uteis.getCaminhoRedirecionamentoNavegacao("fechamentoMesForm.xhtml");
    	} catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("fechamentoMesForm.xhtml");
        }        
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>fechamentoMes</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é
     * acionado o <code>alterar()</code>. Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado
     * para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {
        	atualizarListaUnidadeUnsinoFechamentoVO();
        	atualizarListaContaCorrenteFechamentoVO();
            if (fechamentoMesVO.isNovoObj().booleanValue()) {
                getFacadeFactory().getFechamentoMesFacade().incluir(getFechamentoMesVO(), true, getUsuarioLogado());
            } else {
            	getFacadeFactory().getFechamentoMesFacade().verificarEGerarHistoricoModificacoesFechamentoMes(getFechamentoMesVO(), getFechamentoMesVOAnterior(), getUsuarioLogado());
                getFacadeFactory().getFechamentoMesFacade().alterar(getFechamentoMesVO(), true, getUsuarioLogado());
            }
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("fechamentoMesForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("fechamentoMesForm.xhtml");
        }
    }

    public void atualizarMeses() {
        try {
            setListaConsulta(getFacadeFactory().getFechamentoMesFacade().consultarPorCodigo(0, true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP fechamentoMesCons.jsp. Define o tipo de consulta
     * a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP. Como resultado,
     * disponibiliza um List com os objetos selecionados na sessao da pagina.
     */
    public String consultar() {
        try {
            super.consultar();
            List<FechamentoMesVO> objs = new ArrayList<FechamentoMesVO>(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getFechamentoMesFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }

            if (getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
                objs = getFacadeFactory().getFechamentoMesFacade().consultarPorNomeFantasiaUnidadeEnsino(getControleConsulta().getValorConsulta(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("mes")) {
                if (getValorConsultaMes().equals("")) {
                	setValorConsultaMes("0");
                }
                int valorIntMes = Integer.parseInt(getValorConsultaMes());
                objs = getFacadeFactory().getFechamentoMesFacade().consultarPorMes(new Integer(valorIntMes), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("ano")) {
                if (getValorConsultaAno().equals("")) {
                	setValorConsultaAno("0");
                }
                int valorIntAno = Integer.parseInt(getValorConsultaAno());
                objs = getFacadeFactory().getFechamentoMesFacade().consultarPorAno(new Integer(valorIntAno), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("competencia")) {
            	if (getValorConsultaMes().equals("")) {
                	setValorConsultaMes("0");
                }
            	int valorIntMes = Integer.parseInt(getValorConsultaMes());
            	if (getValorConsultaAno().equals("")) {
                	setValorConsultaAno("0");
                }
                int valorIntAno = Integer.parseInt(getValorConsultaAno());            	
                objs = getFacadeFactory().getFechamentoMesFacade().consultarPorCompetencia(valorIntMes, valorIntAno, true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }            
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("fechamentoMesCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList<FechamentoMesVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("fechamentoMesCons.xhtml");
        }
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>fechamentoMesVO</code> Após a exclusão
     * ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
            getFacadeFactory().getFechamentoMesFacade().excluir(fechamentoMesVO, getUsuarioLogado());
            novo();
            setMensagemID("msg_dados_excluidos");
            return Uteis.getCaminhoRedirecionamentoNavegacao("fechamentoMesForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("fechamentoMesForm.xhtml");
        }
    }

    public void fecharMeses() throws Exception {
        try {
            Iterator i = getListaConsulta().iterator();
            while (i.hasNext()) {
                FechamentoMesVO obj = (FechamentoMesVO) i.next();
                if (obj.getFechado()) {
                    verificarMesFinalizado(obj);
                    getFacadeFactory().getFechamentoMesFacade().fecharMes(obj);
                }
            }
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }

    }

    public void verificarMesFinalizado(FechamentoMesVO obj) throws Exception {
        String dataFim = Uteis.getMesReferencia(obj.getMes(), obj.getAno());
        Integer qtdDiasMes = Uteis.obterNrDiasNoMes(Uteis.getDate("01/" + dataFim));
        Date data = Uteis.getDate(qtdDiasMes + "/" + dataFim);
        if (data.after(new Date())) {
            throw new Exception("O mês " + obj.getMes() + "/" + obj.getAno() + "não pode ser finalizado antes de seu término");
        }
    }

    /**
     * Rotina responsável por atribui um javascript com o método de mascara para campos do tipo Data, CPF, CNPJ, etc.
     */
    public String getMascaraConsulta() {
        return "";
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List<SelectItem> tipoConsultaCombo;
    public List<SelectItem> tipoConsultaComboMes;
    protected String campoConsultaMes;
    protected String campoConsultaAno;
    
    public List<SelectItem> getTipoConsultaComboMes() {
    	if (tipoConsultaComboMes == null) {
    		tipoConsultaComboMes = new ArrayList<SelectItem>(0);
    		tipoConsultaComboMes.add(new SelectItem(1, "01"));
    		tipoConsultaComboMes.add(new SelectItem(2, "02"));
    		tipoConsultaComboMes.add(new SelectItem(3, "03"));
    		tipoConsultaComboMes.add(new SelectItem(4, "04"));
    		tipoConsultaComboMes.add(new SelectItem(5, "05"));
    		tipoConsultaComboMes.add(new SelectItem(6, "06"));
    		tipoConsultaComboMes.add(new SelectItem(7, "07"));
    		tipoConsultaComboMes.add(new SelectItem(8, "08"));
    		tipoConsultaComboMes.add(new SelectItem(9, "09"));
    		tipoConsultaComboMes.add(new SelectItem(10,"10"));
    		tipoConsultaComboMes.add(new SelectItem(11,"11"));
    		tipoConsultaComboMes.add(new SelectItem(12,"12"));
    	}
        return tipoConsultaComboMes;
    }    
    
    public List<SelectItem> getTipoConsultaCombo() {
        List<SelectItem> itens = new ArrayList<SelectItem>(0);
        itens.add(new SelectItem("codigo", "Código"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Nome Unidade Ensino"));
        itens.add(new SelectItem("mes", "Mês"));
        itens.add(new SelectItem("ano", "Ano"));
        itens.add(new SelectItem("competencia", "Competência - Mês/Ano"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("fechamentoMesCons.xhtml");
    }

    /**
     * Operação que libera todos os recursos (atributos, listas, objetos) do backing bean. Garantindo uma melhor atuação
     * do Garbage Coletor do Java. A mesma é automaticamente quando realiza o logout.
     */
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        fechamentoMesVO = null;
    }

    public FechamentoMesVO getFechamentoMesVO() {
        return fechamentoMesVO;
    }

    public void setFechamentoMesVO(FechamentoMesVO FechamentoMesVO) {
        this.fechamentoMesVO = FechamentoMesVO;
    }

	public String getCampoConsultaMes() {
		return campoConsultaMes;
	}

	public void setCampoConsultaMes(String campoConsultaMes) {
		this.campoConsultaMes = campoConsultaMes;
	}

	public String getCampoConsultaAno() {
		if (campoConsultaAno == null) { 
			campoConsultaAno = "2018";
		}
		return campoConsultaAno;
	}

	public void setCampoConsultaAno(String campoConsultaAno) {
		this.campoConsultaAno = campoConsultaAno;
	}
	
	public void limparDadosConsulta() {
		//System.out.print(this.getControleConsulta().getCampoConsulta().toString());
	}
	
	public Boolean getConsultarPorCodigo() {
		if (this.getControleConsulta().getCampoConsulta().equals("codigo")) {
			return Boolean.TRUE;
		} 
		return Boolean.FALSE;
	}		
	
	public Boolean getConsultarPorNomeUnidadeEnsino() {
		if (this.getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
			return Boolean.TRUE;
		} 
		return Boolean.FALSE;
	}	
	
	public Boolean getConsultarPorMes() {
		if (this.getControleConsulta().getCampoConsulta().equals("mes")) {
			return Boolean.TRUE;
		} 
		return Boolean.FALSE;
	}
	
	public Boolean getConsultarPorAno() {
		if (this.getControleConsulta().getCampoConsulta().equals("ano")) {
			return Boolean.TRUE;
		} 
		return Boolean.FALSE;
	}
	
	public Boolean getConsultarPorCompetencia() {
		if (this.getControleConsulta().getCampoConsulta().equals("competencia")) {
			return Boolean.TRUE;
		} 
		return Boolean.FALSE;
	}
	
    public String getValorConsultaMes() {
    	if (valorConsultaMes == null) { 
    		valorConsultaMes = String.valueOf(Uteis.getMesData(new Date()));
    		if (valorConsultaMes.length() == 1) {
    			valorConsultaMes = "0" + valorConsultaMes;
    		}
    	}
		return valorConsultaMes;
	}

	public void setValorConsultaMes(String valorConsultaMes) {
		this.valorConsultaMes = valorConsultaMes;
	}

	public String getValorConsultaAno() {
		if (valorConsultaAno == null) {
			valorConsultaAno = "2018";
		}
		return valorConsultaAno;
	}

	public void setValorConsultaAno(String valorConsultaAno) {
		this.valorConsultaAno = valorConsultaAno;
	}	
	
	public void alterarMarcacaoFecharCompetenciaDiaEspecifico() {
		if (!getFechamentoMesVO().getFecharCompetenciaDiaEspecifico()) {
			getFechamentoMesVO().setFecharCompetenciaUltimoDiaMes(Boolean.TRUE);
		} else {
			getFechamentoMesVO().setFecharCompetenciaUltimoDiaMes(Boolean.FALSE);
		}
	}

	public void alterarMarcacaoFecharCompetenciaUltimoDiaMes() {
		if (!getFechamentoMesVO().getFecharCompetenciaUltimoDiaMes()) {
			getFechamentoMesVO().setFecharCompetenciaDiaEspecifico(Boolean.TRUE);
		} else {
			getFechamentoMesVO().setFecharCompetenciaDiaEspecifico(Boolean.FALSE);
		}
	}
	
	public void alterarMarcacaoGerarPrevisaoReceitaCompetenciaUltimoDiaMes() {
		if (!getFechamentoMesVO().getGerarPrevisaoReceitaCompetenciaUltimoDiaMes()) {
			getFechamentoMesVO().setGerarPrevisaoReceitaCompetenciaDiaEspecifico(Boolean.TRUE);
		} else {
			getFechamentoMesVO().setGerarPrevisaoReceitaCompetenciaDiaEspecifico(Boolean.FALSE);
		}
	}	
	
	public void alterarMarcacaoGerarPrevisaoReceitaCompetenciaDiaEspecifico() {
		if (!getFechamentoMesVO().getGerarPrevisaoReceitaCompetenciaDiaEspecifico()) {
			getFechamentoMesVO().setGerarPrevisaoReceitaCompetenciaUltimoDiaMes(Boolean.TRUE);
		} else {
			getFechamentoMesVO().setGerarPrevisaoReceitaCompetenciaUltimoDiaMes(Boolean.FALSE);
		}
	}
	

    public UnidadeEnsinoVO getUnidadeEnsinoVO() {
    	if (unidadeEnsinoVO == null) { 
    		unidadeEnsinoVO = new UnidadeEnsinoVO();
    	}
		return unidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		this.unidadeEnsinoVO = unidadeEnsinoVO;
	}	
	
	public void atualizarListaUnidadeUnsinoFechamentoVO() {
		this.getFechamentoMesVO().setListaUnidadesEnsinoVOs(null);
		if (this.getFechamentoMesVO().getGerarFechamentoMesTodasUnidades()) {
			return;
		}
		if (getUnidadeEnsinoVOs().size() > 0) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getFiltrarUnidadeEnsino()) {
					FechamentoMesUnidadeEnsinoVO fechamentoUnidade = new FechamentoMesUnidadeEnsinoVO();
					fechamentoUnidade.setUnidadeEnsino(obj);
					fechamentoUnidade.setFechamentoMes(getFechamentoMesVO());
					getFechamentoMesVO().getListaUnidadesEnsinoVOs().add(fechamentoUnidade);
				}
			}
		}
	}
	
	public void selecionarUnidadesEnsinoFechamentoMes() {
		if (this.getFechamentoMesVO().getGerarFechamentoMesTodasUnidades()) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				obj.setFiltrarUnidadeEnsino(Boolean.TRUE);
			}
			return;
		}
		
		for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
			obj.setFiltrarUnidadeEnsino(Boolean.FALSE);
		}
		
		if (this.getFechamentoMesVO().getListaUnidadesEnsinoVOs().isEmpty()) {
			return;
		}
		
		StringBuilder unidade = new StringBuilder();
		for (FechamentoMesUnidadeEnsinoVO fecMesUnidade : this.getFechamentoMesVO().getListaUnidadesEnsinoVOs()) {
			for (UnidadeEnsinoVO obj : getUnidadeEnsinoVOs()) {
				if (obj.getCodigo().equals(fecMesUnidade.getUnidadeEnsino().getCodigo())) {
					obj.setFiltrarUnidadeEnsino(Boolean.TRUE);
					unidade.append(fecMesUnidade.getUnidadeEnsino().getNome()).append("; ");
				}
			}
		}
		getUnidadeEnsinoVO().setNome(unidade.toString());
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
	
	@PostConstruct
	public void consultarUnidadeEnsino() {
		try {
			consultarUnidadeEnsinoFiltroRelatorio("");
			verificarTodasUnidadesSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	
	
	public void limparUnidadesEnsinoSelecionadas() {
		setMarcarTodasUnidadeEnsino(Boolean.FALSE);
		marcarTodasUnidadesEnsinoAction();
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
	
    public void inicializarListasSelectItemTodosComboBox() {
        //montarListaSelectItemUnidadeEnsino();
    }	
    
    public Boolean getCampoBloqueadoEdicao() {
    	if (!this.getFechamentoMesVO().getCodigo().equals(0)) {
    		return Boolean.TRUE;
    	}
    	return Boolean.FALSE;
    }
    
    public FechamentoMesVO getFechamentoMesVOAnterior() {
    	if (fechamentoMesVOAnterior == null) { 
    		fechamentoMesVOAnterior = new FechamentoMesVO(); 
    	}
		return fechamentoMesVOAnterior;
	}

	public void setFechamentoMesVOAnterior(FechamentoMesVO fechamentoMesVOAnterior) {
		this.fechamentoMesVOAnterior = fechamentoMesVOAnterior;
	}    
	
	public void fecharCompetencia() {
		try {
			if (fechamentoMesVO.isNovoObj().booleanValue()) {
				 throw new Exception("Não é possível fechar uma competência antes que ela esteja persistida.");
			}
			gravar();
			getFacadeFactory().getFechamentoMesFacade().fecharCompetencia(getFechamentoMesVO(), true, getUsuarioLogado());
	        setMensagemID("msg_competencia_fechada");
	     } catch (Exception e) {
	    	 setMensagemDetalhada("msg_erro", e.getMessage());
	     }		
	}
	
	public ContaCorrenteVO getContaCaixa() {
		if (contaCaixa == null) { 
			contaCaixa = new ContaCorrenteVO();
		}
		return contaCaixa;
	}

	public void setContaCaixa(ContaCorrenteVO contaCaixa) {
		this.contaCaixa = contaCaixa;
	}	
	
	public List<ContaCorrenteVO> getContaCorrenteVOs() {
		if (contaCorrenteVOs == null) {
			contaCorrenteVOs = new ArrayList<ContaCorrenteVO>();
		}
		return contaCorrenteVOs;
	}

	public void setContaCorrenteVOs(List<ContaCorrenteVO> contaCorrenteVOs) {
		this.contaCorrenteVOs = contaCorrenteVOs;
	}	
	
	public void atualizarListaContaCorrenteFechamentoVO() {
		this.getFechamentoMesVO().setListaContaCaixaVOs(null);
		if (this.getFechamentoMesVO().getBloquearAberturaTodasContaCaixa()) {
			return;
		}
		if (getContaCorrenteVOs().size() > 0) {
			for (ContaCorrenteVO obj : getContaCorrenteVOs()) {
				if (obj.getFiltrarConta()) {
					FechamentoMesContaCaixaVO fechamentoContaCaixa = new FechamentoMesContaCaixaVO();
					fechamentoContaCaixa.setContaCaixa(obj);
					fechamentoContaCaixa.setFechamentoMes(getFechamentoMesVO());
					getFechamentoMesVO().getListaContaCaixaVOs().add(fechamentoContaCaixa);
				}
			}
		}
	}
	
	public void selecionarContasCaixaFechamentoMes() {
		if (this.getFechamentoMesVO().getBloquearAberturaTodasContaCaixa()) {
			for (ContaCorrenteVO obj : getContaCorrenteVOs()) {
				obj.setFiltrarConta(Boolean.TRUE);
			}
			return;
		}
		
		for (ContaCorrenteVO obj : getContaCorrenteVOs()) {
			obj.setFiltrarConta(Boolean.FALSE);
		}
		
		if (this.getFechamentoMesVO().getListaContaCaixaVOs().isEmpty()) {
			return;
		}
		
		StringBuilder conta = new StringBuilder();
		for (FechamentoMesContaCaixaVO fecMesConta : this.getFechamentoMesVO().getListaContaCaixaVOs()) {
			for (ContaCorrenteVO obj : getContaCorrenteVOs()) {
				if (obj.getCodigo().equals(fecMesConta.getContaCaixa().getCodigo())) {
					obj.setFiltrarConta(Boolean.TRUE);
					conta.append(fecMesConta.getContaCaixa().getNomeApresentacaoSistema()).append("; ");
				}
			}
		}
		getContaCaixa().setNome(conta.toString());
    }	
    
	public void verificarTodasContasCaixasSelecionadas() {
		StringBuilder contas = new StringBuilder();
		if (getContaCorrenteVOs().size() > 1) {
			for (ContaCorrenteVO obj : getContaCorrenteVOs()) {
				if (obj.getFiltrarConta()) {
					contas.append(obj.getNomeApresentacaoSistema()).append("; ");
				} 
			}
			getContaCaixa().setNome(contas.toString());
		} else {
			if (!getContaCorrenteVOs().isEmpty()) {
				if (getContaCorrenteVOs().get(0).getFiltrarConta()) {
					getContaCaixa().setNome(getContaCorrenteVOs().get(0).getNomeApresentacaoSistema());
				}
			} else {
				getContaCaixa().setNome(contas.toString());
			}
		}
	}    
	
	public void consultarContasCaixaUnidadeEnsino(List<UnidadeEnsinoVO> listaUnidadesEnsino) {
		try {
			getContaCorrenteVOs().clear();
			setContaCorrenteVOs(getFacadeFactory().getContaCorrenteFacade().consultarContaCaixaUnidadesEnsino(listaUnidadesEnsino, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
			for (ContaCorrenteVO conta : getContaCorrenteVOs()) {
				conta.setFiltrarConta(true);
			}
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultarUnidadeEnsino(new ArrayList<UnidadeEnsinoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	
	
	public void finalizarSelecaoUnidadesEnsino() {
		consultarContasCaixasUnidadeEnsino();
	}
	
	public void prepararFiltrasContasCaixas() {
		consultarContasCaixasUnidadeEnsino();
		verificarTodasContasCaixasSelecionadas();
	}
	
	public void consultarContasCaixasUnidadeEnsino() {
		try {
			List<UnidadeEnsinoVO> listaUnidades = new ArrayList<UnidadeEnsinoVO>();
			for (FechamentoMesUnidadeEnsinoVO obj : getFechamentoMesVO().getListaUnidadesEnsinoVOs()) {
				listaUnidades.add(obj.getUnidadeEnsino());
			}
			consultarContasCaixaUnidadeEnsino(listaUnidades);
			verificarTodasContasCaixasSelecionadas();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}	
	
	public void limparContasCaixaSelecionadas() {
		setMarcarTodasContas(Boolean.FALSE);
		marcarTodasContasCaixaAction();
	}
	
	public void marcarTodasContasCaixaAction() {	
		for (ContaCorrenteVO conta : getContaCorrenteVOs()) {
			if (getMarcarTodasContas()) {
				conta.setFiltrarConta(Boolean.TRUE);
			} else {
				conta.setFiltrarConta(Boolean.FALSE);
			}
		}
		verificarTodasContasCaixasSelecionadas();
	}

	public Boolean getMarcarTodasContas() {
		if (marcarTodasContas == null) { 
			marcarTodasContas = Boolean.FALSE;
		}
		return marcarTodasContas;
	}

	public void setMarcarTodasContas(Boolean marcarTodasContas) {
		this.marcarTodasContas = marcarTodasContas;
	}	
	
	public Boolean getApresentarBotaoReabrir() {
		if (getFechamentoMesVO().getFechado()) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}
	
	public Boolean getApresentarBotaoFechar() {
		if ((getFechamentoMesVO().getNovoObj()) || (getFechamentoMesVO().getPoliticaFechamentoMesPadrao())) {
			return Boolean.FALSE;
		}
		if (getFechamentoMesVO().getFechado()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}
	
	public String reabrirCompetencia() {
		try {
			if (getFechamentoMesVO().getNovoObj().booleanValue()) {
				 throw new Exception("Não é possível reabrir uma competência antes que ela esteja persistida.");
			}
			if (!getFechamentoMesVO().getFechado()) {
				 throw new Exception("Não é possível reabrir uma competência que não esteja fechada.");
			}
			gravar();
			getFacadeFactory().getFechamentoMesFacade().reabrirCompetencia(getFechamentoMesVO(), true, getUsuarioLogado());
	        setMensagemID("msg_competencia_reaberta");
	        return Uteis.getCaminhoRedirecionamentoNavegacao("fechamentoMesForm.xhtml");
	     } catch (Exception e) {
	    	 setMensagemDetalhada("msg_erro", e.getMessage());
	    	 return Uteis.getCaminhoRedirecionamentoNavegacao("fechamentoMesForm.xhtml");
	     }		
	}
	
	private List<SelectItem> listaSelectDataUtilizarVerificar;

	public List<SelectItem> getListaSelectDataUtilizarVerificar() {
		if (listaSelectDataUtilizarVerificar == null) {
			listaSelectDataUtilizarVerificar = new ArrayList<SelectItem>();
			listaSelectDataUtilizarVerificar.add(new SelectItem("DV", "Data Vencimento"));
			listaSelectDataUtilizarVerificar.add(new SelectItem("DC", "Data Competência"));
		}
		return listaSelectDataUtilizarVerificar;
	}
	
}
