package controle.academico;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas planoDescontoForm.jsp
 * planoDescontoCons.jsp) com as funcionalidades da classe <code>PlanoDesconto</code>. Implemtação da camada controle
 * (Backing Bean).
 * 
 * @see SuperControle
 * @see PlanoDesconto
 * @see PlanoDescontoVO
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.faces.model.SelectItem;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.financeiro.CategoriaDescontoVO;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;

@Controller("PlanoDescontoControle")
@Scope("viewScope")
@Lazy
public class PlanoDescontoControle extends SuperControle implements Serializable {

	private PlanoDescontoVO planoDescontoVO;
	private List<SelectItem> listaOpcoesConsultaComboAtivoInativo;
	private List listaSelectItemUnidadeEnsino;
        private List listaSelectItemCategoriaDesconto;
        

	public PlanoDescontoControle() throws Exception {
		// obterUsuarioLogado();
		setControleConsulta(new ControleConsulta());		
		setMensagemID("msg_entre_prmconsulta");
	}

	public String novo() {
		removerObjetoMemoria(this);
		setPlanoDescontoVO(new PlanoDescontoVO());
		montarListaSelectItemUnidadeEnsino();
                montarListaSelectItemCategoriaDesconto();
		setMensagemID("msg_entre_dados");
		return Uteis.getCaminhoRedirecionamentoNavegacao("planoDescontoForm.xhtml");
	}

	public String editar() {
		PlanoDescontoVO obj = (PlanoDescontoVO) context().getExternalContext().getRequestMap().get("planoDescontoItens");
		obj.setNovoObj(Boolean.FALSE);
		montarListaSelectItemUnidadeEnsino();
                montarListaSelectItemCategoriaDesconto();
		setPlanoDescontoVO(obj);
		setMensagemID("msg_dados_editar");
		return Uteis.getCaminhoRedirecionamentoNavegacao("planoDescontoForm.xhtml");
	}

	public String gravar() {
		try {
			if (planoDescontoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getPlanoDescontoFacade().incluir(planoDescontoVO, getUsuarioLogado());
			} else {
				getFacadeFactory().getPlanoDescontoFacade().alterar(planoDescontoVO, getUsuarioLogado());
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoDescontoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoDescontoForm.xhtml");
		}
	}

	public String gravarDadosDescritivos() {
		try {
			if (!planoDescontoVO.isNovoObj().booleanValue()) {
				getFacadeFactory().getPlanoDescontoFacade().alterarDadosDescritivos(planoDescontoVO);
			}
			setMensagemID("msg_dados_gravados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoDescontoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoDescontoForm.xhtml");
		}
	}

	 public void scrollerListener(DataScrollEvent DataScrollEvent) throws Exception {
	        getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
	        getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());	        
	        consultar();
	  }
	
	/**
	 * Rotina responsavel por executar as consultas disponiveis no JSP
	 * PlanoDescontoCons.jsp. Define o tipo de consulta a ser executada, por
	 * meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
	 * Como resultado, disponibiliza um List com os objetos selecionados na
	 * sessao da pagina.
	 */
	public String consultar() {
		try {
			super.consultar();
			getControleConsultaOtimizado().getListaConsulta().clear();
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(0);
			getControleConsultaOtimizado().setLimitePorPagina(10);
			if (getControleConsulta().getCampoConsulta().equals("codigo")) {
				if (getControleConsulta().getValorConsulta().equals("")) {
					getControleConsulta().setValorConsulta("0");
				}
				int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPlanoDescontoFacade().consultarPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPlanoDescontoFacade().consultarTotalRegistroPorCodigo(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo()));
			}
			if (getControleConsulta().getCampoConsulta().equals("nome")) {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPlanoDescontoFacade().consultarPorNome(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPlanoDescontoFacade().consultarTotalRegistroPorNome(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo()));
			}
			if (getControleConsulta().getCampoConsulta().equals("ativo/inativo")) {
				getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getPlanoDescontoFacade().consultarPorAtivoOuInativo(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, getUsuarioLogado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset()));
				getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getPlanoDescontoFacade().consultarTotalRegistroPorAtivoOuInativo(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo()));
			}			
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoDescontoCons.xhtml");
		} catch (Exception e) {
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoDescontoCons.xhtml");
		}
	}

	public String excluir() {
		try {
			getFacadeFactory().getPlanoDescontoFacade().excluir(planoDescontoVO, getUsuarioLogado());
			setPlanoDescontoVO(new PlanoDescontoVO());
			setMensagemID("msg_dados_excluidos");
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoDescontoForm.xhtml");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("planoDescontoForm.xhtml");
		}
	}
        
	public void montarListaSelectItemCategoriaDesconto() {
		try {
			montarListaSelectItemCategoriaDesconto("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}        

	public void montarListaSelectItemUnidadeEnsino() {
		try {
			montarListaSelectItemUnidadeEnsino("");
		} catch (Exception e) {
			//System.out.println("MENSAGEM => " + e.getMessage());
			;
		}
	}
        
	public void montarListaSelectItemCategoriaDesconto(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getCategoriaDescontoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
			objs.add(new SelectItem(0, ""));
			while (i.hasNext()) {
				CategoriaDescontoVO obj = (CategoriaDescontoVO) i.next();
				objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
			}
			setListaSelectItemCategoriaDesconto(objs);
		} catch (Exception e) {
			throw e;
		} finally {
			Uteis.liberarListaMemoria(resultadoConsulta);
			i = null;
		}
	}        

	public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
		List resultadoConsulta = null;
		Iterator i = null;
		try {
			resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorUsuarioUnidadeEnsinoVinculadaAoUsuario(getUsuarioLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			if (resultadoConsulta.isEmpty()) {
				resultadoConsulta = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(prm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			i = resultadoConsulta.iterator();
			List objs = new ArrayList(0);
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

	public void irPaginaInicial() throws Exception {
		
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setPage(1);
		this.consultar();
	}
	

	public List<SelectItem> getListaSelectItemTipoDesconto() throws Exception {
		return UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TipoDescontoAluno.class, false);
	}

	public List getTipoConsultaCombo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("nome", "Nome"));
		itens.add(new SelectItem("ativo/inativo", "Ativo/Inativo"));
		itens.add(new SelectItem("codigo", "Código"));
		return itens;
	}

	public String inicializarConsultar() {
		removerObjetoMemoria(this);
		setListaConsulta(new ArrayList(0));
		setMensagemID("msg_entre_prmconsulta");
		return Uteis.getCaminhoRedirecionamentoNavegacao("planoDescontoCons.xhtml");
	}

	public PlanoDescontoVO getPlanoDescontoVO() {
		return planoDescontoVO;
	}

	public void setPlanoDescontoVO(PlanoDescontoVO planoDescontoVO) {
		this.planoDescontoVO = planoDescontoVO;
	}

	public Boolean getIncluindoPlanoDescontoVO() {
		if (!this.getPlanoDescontoVO().getCodigo().equals(0)) {
			return false;
		}
		return true;
	}

	public Boolean getIsPodeAlterar() {
		return !getIncluindoPlanoDescontoVO() && !getPlanoDescontoVO().getAtivo();
	}

	public Boolean getIsPodeAlterarDadosDescritivos() {
		return !getIncluindoPlanoDescontoVO() && getPlanoDescontoVO().getAtivo();
	}

	@Override
	protected void limparRecursosMemoria() {
		super.limparRecursosMemoria();
		planoDescontoVO = null;
	}

	public void ativarInativar() {
		try {
			setMensagemID(getFacadeFactory().getPlanoDescontoFacade().realizarAtivacaoInativacao(getPlanoDescontoVO(), getUsuarioLogado()));
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Boolean getIsApresentarBotaoAtivar() {
		return !getPlanoDescontoVO().getAtivo() && !getPlanoDescontoVO().getCodigo().equals(0);
	}

	public Boolean getIsApresentarBotaoDesativar() {
		return getPlanoDescontoVO().getAtivo() && !getPlanoDescontoVO().getCodigo().equals(0);
	}

	public Boolean getIsApresentarDadosAtivacao() {
		return getPlanoDescontoVO().getDataAtivacao() != null && getPlanoDescontoVO().getAtivo();
	}

	public Boolean getIsApresentarDadosInativacao() {
		return getPlanoDescontoVO().getDataInativacao() != null && !getPlanoDescontoVO().getAtivo();
	}

	public Boolean getIsConsultaPorAtivoInativo() {
		return getControleConsulta().getCampoConsulta().equals("ativo/inativo");
	}

	public List<SelectItem> getOpcoesConsultaComboAtivoInativo() {
		if (listaOpcoesConsultaComboAtivoInativo == null) {
			listaOpcoesConsultaComboAtivoInativo = new ArrayList<SelectItem>();
			listaOpcoesConsultaComboAtivoInativo.add(new SelectItem("ativo", "Ativo"));
			listaOpcoesConsultaComboAtivoInativo.add(new SelectItem("inativo", "Inativo"));
		}
		return listaOpcoesConsultaComboAtivoInativo;
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

	public void realizarMarcacaoDiaUtil() {
		getFacadeFactory().getPlanoDescontoFacade().realizarMarcacaoDiaUtil(getPlanoDescontoVO());
	}

	public void realizarMarcacaoDiaFixo() {
		getFacadeFactory().getPlanoDescontoFacade().realizarMarcacaoDiaFixo(getPlanoDescontoVO());
	}

	public void realizarMarcacaoDescontoValidoAteDataVencimento() {
		if (getPlanoDescontoVO().getDescontoValidoAteDataVencimento().booleanValue()) {
			getPlanoDescontoVO().setDiasValidadeVencimento(0);
		}
		getFacadeFactory().getPlanoDescontoFacade().realizarMarcacaoDescontoValidoAteDataVencimento(getPlanoDescontoVO());
	}
        
        public void atualizarAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto() {
            if (this.getPlanoDescontoVO().getAplicarSobreValorCheio()) {
                this.getPlanoDescontoVO().setAplicarSobreValorBaseDeduzidoValorOutrosPlanosDesconto(Boolean.FALSE);
            }
        }

    /**
     * @return the listaSelectItemCategoriaDesconto
     */
    public List getListaSelectItemCategoriaDesconto() {
        if (listaSelectItemCategoriaDesconto == null) {
            listaSelectItemCategoriaDesconto = new ArrayList(0);
	}
        return listaSelectItemCategoriaDesconto;
    }

    /**
     * @param listaSelectItemCategoriaDesconto the listaSelectItemCategoriaDesconto to set
     */
    public void setListaSelectItemCategoriaDesconto(List listaSelectItemCategoriaDesconto) {
        this.listaSelectItemCategoriaDesconto = listaSelectItemCategoriaDesconto;
    }

}
