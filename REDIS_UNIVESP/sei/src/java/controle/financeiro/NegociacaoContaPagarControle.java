package controle.financeiro;

/**
 * 
 * negociacaoContaReceberCons.jsp) com as funcionalidades da classe <code>NegociacaoContaReceber</code>. Implemtação da
 * camada controle (Backing Bean).
 * 
 * @see SuperControle
 * @see NegociacaoContaReceber
 * @see NegociacaoContaReceberVO
 */
import java.io.Serializable;
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
import controle.arquitetura.SuperControle;
import negocio.comuns.academico.CursoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.TurnoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoCursoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.arquitetura.enumeradores.PerfilAcessoPermissaoFinanceiroEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.contabil.LancamentoContabilVO;
import negocio.comuns.contabil.PlanoContaVO;
import negocio.comuns.ead.enumeradores.SituacaoEnum;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.ContaPagarNegociadoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.NegociacaoContaPagarVO;
import negocio.comuns.financeiro.ParceiroVO;
import negocio.comuns.financeiro.enumerador.TipoAcrescimoEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoDescontoAluno;
import negocio.comuns.utilitarias.dominios.TipoSacado;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

@Controller("NegociacaoContaPagarControle")
@Scope("viewScope")
@Lazy
public class NegociacaoContaPagarControle extends SuperControle implements Serializable {

	 /**
		 * 
		 */
	private static final long serialVersionUID = 7722627309519956515L;
    private NegociacaoContaPagarVO negociacaoContaPagarVO;
    private List<SelectItem> listaSelectItemUnidadeEnsino;   
    private List<SelectItem> listaSelectItemBanco;   
    private List<SelectItem> listaSelectItemOperadoraCartao;  
    private List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum;
    private List<FuncionarioVO> listaConsultaFuncionario;    
    private String valorConsultaFuncionario;
    private String campoConsultaFuncionario;
    private List<MatriculaVO> listaConsultaAluno;
    private String valorConsultaAluno;
    private String campoConsultaAluno;
    private List<ContaPagarVO> listaConsultaContaPagar;
    private Date dataInicioContaPagar;   
    private Date dataFinalContaPagar;   
    private String abrirModalRenegociacao;
    private List<ParceiroVO> listaConsultaParceiro;
    private String valorConsultaParceiro;
    private String campoConsultaParceiro;        
    private List<FornecedorVO> listaConsultaFornecedor;
	protected String valorConsultaFornecedor;
	protected String campoConsultaFornecedor;
	protected String usernameLiberarDesconto;
	protected String senhaLiberarDesconto;
	public Boolean apresentarBotaoLiberarDesconto;	
    public String apresentarModalEstornoRenegociacao;    
    private List<SelectItem> listaSelectItemTipoAcrescimo;  
    private Double valorBaseParcela;
    private Double valorTotalApresentar;	
    private ContaPagarVO contaPagarVO;
    private CentroResultadoOrigemVO centroResultadoOrigemVO;
    private List<SelectItem> listaSelectItemUnidadeEnsinoCategoriaDespesa;
	private List<CategoriaDespesaVO> listaConsultaCentroDespesa;
	private String valorConsultaCentroDespesa;
	private String campoConsultaCentroDespesa;
	private String valorConsultaFuncionarioCentroCusto;
	private String campoConsultaFuncionarioCentroCusto;
	private List<FuncionarioCargoVO> listaConsultaFuncionarioCentroCusto;
	private String campoConsultaTurma;
	private String valorConsultaTurma;
	private List<TurmaVO> listaConsultaTurma;
	private String campoConsultaCurso;
	private String valorConsultaCurso;
	private List<CursoVO> listaConsultaCurso;
	private String campoConsultaCursoTurno;
	private String valorConsultaCursoTurno;
	private List<UnidadeEnsinoCursoVO> listaConsultaCursoTurno;
	private String campoConsultaDepartamento;
	private String valorConsultaDepartamento;
	private List<DepartamentoVO> listaConsultaDepartamento;
	private DataModelo centroResultadoDataModelo;
	private boolean centroResultadoAdministrativo = false;
	
	private List<SelectItem> tipoConsultaComboContaPagar;
	private String campoConsultaContaPagar;
	private String valorConsultaContaPagar;
	private LancamentoContabilVO lancamentoContabilVO;
	private String modalAptoParaSerFechado;
	private String valorConsultaPlanoConta;
	private String campoConsultaPlanoConta;
	private List<PlanoContaVO> listaConsultaPlanoConta;
    private List<MatriculaVO> listaConsultaCandidato;
    private String valorConsultaCandidato;
    private String campoConsultaCandidato;
    
    @PostConstruct
    public void init() {
    	try {
	    	getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogado().getCodigo());
	    	getControleConsultaOtimizado().setLimitePorPagina(10);
	    	getControleConsultaOtimizado().setPage(0);
	    	getControleConsultaOtimizado().setPaginaAtual(0);
	    	montarListaSelectItemUnidadeEnsino();
	    	setMensagemID("msg_entre_prmconsulta");
    	} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
    }
    
    @PostConstruct
	public String realizarCarregamentoTela() {
		try {
			NegociacaoContaPagarVO obj = (NegociacaoContaPagarVO) context().getExternalContext().getSessionMap().get("negociacaoContaPagar");
			if (obj != null && !obj.getCodigo().equals(0)) {
				setNegociacaoContaPagarVO(getFacadeFactory().getNegociacaoContaPagarFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));            
	            inicializarListasSelectItemTodosComboBox();
	            executarVerificacaoUsuarioLogadoPodeLiberarDesconto();   
	            setOncompleteModal("");
				setMensagemID("msg_dados_editar");
			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			context().getExternalContext().getSessionMap().remove("negociacaoContaPagar");
		}
		return "";
	}
    
  
    public String novo() {
        removerObjetoMemoria(this);
        try {
            setNegociacaoContaPagarVO(null);
            inicializarListasSelectItemTodosComboBox();            
            incializarResponsavel();           
            executarVerificacaoUsuarioLogadoPodeLiberarDesconto();
            setValorBaseParcela(0.0);            
            setMensagemID("msg_entre_dados");
            setOncompleteModal("RichFaces.$('panelContaPagar').show();");
            return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoContaPagarForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoContaPagarForm.xhtml");
        }

    }

    public void incializarResponsavel() {
        try {
            getNegociacaoContaPagarVO().getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
            getNegociacaoContaPagarVO().getResponsavel().setNome(getUsuarioLogado().getNome());
        } catch (Exception e) {
        	setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
        }
    }

    public String editar() throws Exception {
        try {
        	NegociacaoContaPagarVO obj = (NegociacaoContaPagarVO) context().getExternalContext().getRequestMap().get("negociacaoContaPagarItens");
            setNegociacaoContaPagarVO(getFacadeFactory().getNegociacaoContaPagarFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));            
            inicializarListasSelectItemTodosComboBox();
            executarVerificacaoUsuarioLogadoPodeLiberarDesconto();   
            setOncompleteModal("");
            if (obj.getTipoCandidato() && Uteis.isAtributoPreenchido(getNegociacaoContaPagarVO().getPessoa())) {
            	getNegociacaoContaPagarVO().getMatriculaAluno().setAluno(getNegociacaoContaPagarVO().getPessoa());
            }
            setMensagemID("msg_dados_editar");
            return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoContaPagarForm.xhtml");
        } catch (Exception e) {            
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoContaPagarCons.xhtml");
        }
    }

    public void gravar() {
        try {        
        	if(getNegociacaoContaPagarVO().isNovoObj()) {
        		incializarResponsavel();
        		getFacadeFactory().getNegociacaoContaPagarFacade().incluir(getNegociacaoContaPagarVO(), true, getUsuarioLogado());
        	}else {
        		getFacadeFactory().getNegociacaoContaPagarFacade().alterarJustificativa(getNegociacaoContaPagarVO(), true, getUsuarioLogado());
        	}
            setMensagemID("msg_dados_gravados");
        } catch (Exception e) {
			if (e.getMessage().contains("fn_validarintegridadesituacaocontapagar")) {
				setMensagemDetalhada("msg_erro", "Conta Pagar Está Vinculada a Uma Negociação e a Situação é diferente de NEGOCIADA");
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
        }
    }

    @Override
    public String consultar() {
        try {
            super.consultar();            
            getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getNegociacaoContaPagarFacade().consultar(getControleConsultaOtimizado().getValorConsulta(), getControleConsultaOtimizado().getDataIni(), getControleConsultaOtimizado().getDataFim(), getUnidadeEnsinoVO().getCodigo(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getNegociacaoContaPagarFacade().consultarTotalRegistro(getControleConsultaOtimizado().getValorConsulta(), getControleConsultaOtimizado().getDataIni(), getControleConsultaOtimizado().getDataFim(), getUnidadeEnsinoVO().getCodigo()));
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoContaPagarCons.xhtml");
        } catch (Exception e) {
        	getControleConsultaOtimizado().getListaConsulta().clear();
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoContaPagarCons.xhtml");
        }
    }
    
    public void scrollerListener(DataScrollEvent dataScrollEvent) throws Exception {
        getControleConsultaOtimizado().setPaginaAtual(dataScrollEvent.getPage());
        getControleConsultaOtimizado().setPage(dataScrollEvent.getPage());     
        consultar();
    }
    
     /**
     * Operação responsável por processar a exclusão um objeto da classe <code>NegociacaoContaReceberVO</code> Após a
     * exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public void excluir() {                
        try {
            getFacadeFactory().getNegociacaoContaPagarFacade().excluir(getNegociacaoContaPagarVO(), true, getUsuarioLogado());                                   
            novo();
            setMensagemID("msg_dados_excluidos");                       
        } catch (Exception e) {
			if (e.getMessage().contains("fn_validarintegridadesituacaocontapagar")) {
				setMensagemDetalhada("msg_erro", "Conta Pagar Está Vinculada a Uma Negociação e a Situação é diferente de NEGOCIADA");
			} else {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
        }
    }

    public void consultarFuncionario() {
        try {
            List<FuncionarioVO> objs = new ArrayList<>(0);
            if (getValorConsultaFuncionario().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            getFacadeFactory().getPessoaFacade().setIdEntidade("Funcionario");
            if (getCampoConsultaFuncionario().equals("nome")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNome(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("matricula")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorMatricula(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("nomeCidade")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCidade(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("CPF")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorCPF(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("cargo")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeCargo(getValorConsultaFuncionario(), this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("departamento")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeDepartamento(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            if (getCampoConsultaFuncionario().equals("unidadeEnsino")) {
                objs = getFacadeFactory().getFuncionarioFacade().consultarPorNomeUnidadeEnsino(getValorConsultaFuncionario(), "FU", this.getUnidadeEnsinoLogado().getCodigo(), false,
                        Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
            }
            setListaConsultaFuncionario(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaFuncionario(new ArrayList<FuncionarioVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void selecionarFornecedor() {
		FornecedorVO obj = (FornecedorVO) context().getExternalContext().getRequestMap().get("fornecedorItens");
		getNegociacaoContaPagarVO().setFornecedor(obj);
		limparListaContaPagar();
	}

    public void executarVerificacaoUsuarioLogadoPodeLiberarDesconto() {
        try {
            ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("DescontoNegociacaoContaPagar", getUsuarioLogado());            
            this.setApresentarBotaoLiberarDesconto(Boolean.FALSE);
            this.setUsernameLiberarDesconto("");
            this.setSenhaLiberarDesconto("");            
        } catch (Exception e) {
            this.setUsernameLiberarDesconto("");
            this.setSenhaLiberarDesconto("");
            this.setApresentarBotaoLiberarDesconto(Boolean.TRUE);
        }
    }
    
    public void executarVerificacaoUsuarioPodeLiberarDesconto() {
        try {
            UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUsernameLiberarDesconto(), this.getSenhaLiberarDesconto(), true, Uteis.NIVELMONTARDADOS_TODOS);
            ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("DescontoNegociacaoContaPagar", usuarioVerif);            
            this.setApresentarBotaoLiberarDesconto(Boolean.FALSE);
            this.setUsernameLiberarDesconto("");
            this.setSenhaLiberarDesconto("");
            setMensagemID("msg_ConfirmacaoLiberacaoDesconto");
        } catch (Exception e) {
            this.setUsernameLiberarDesconto("");
            this.setSenhaLiberarDesconto("");
            this.setApresentarBotaoLiberarDesconto(Boolean.TRUE);
        }
    }
    
	private List<SelectItem> tipoConsultaComboFornecedor;

	public String getMascaraConsultaFornecedor(){
		if(getCampoConsultaFornecedor().equals("CPF")){
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '999.999.999-99', event);";
		}else if(getCampoConsultaFornecedor().equals("CNPJ")){
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '99.999.999/9999-99', event);";
		}else if(getCampoConsultaFornecedor().equals("codigo")){
			return "return mascara(this.form, 'formFornecedor:valorConsultaFornecedor', '99999999999999', event);";
		}
		return "";
	}
	
	public List<SelectItem> getTipoConsultaComboFornecedor() {
		if (tipoConsultaComboFornecedor == null) {
			tipoConsultaComboFornecedor = new ArrayList<>(0);
			tipoConsultaComboFornecedor.add(new SelectItem("nome", "Nome"));
			tipoConsultaComboFornecedor.add(new SelectItem("razaoSocial", "Razão Social"));
			tipoConsultaComboFornecedor.add(new SelectItem("CNPJ", "CNPJ"));
			tipoConsultaComboFornecedor.add(new SelectItem("CPF", "CPF"));
			tipoConsultaComboFornecedor.add(new SelectItem("RG", "RG"));
			tipoConsultaComboFornecedor.add(new SelectItem("codigo", "codigo"));					
		}
		return tipoConsultaComboFornecedor;
	}

	public void consultarFornecedor() {
		try {
			super.consultar();
			List<FornecedorVO> objs = new ArrayList<>(0);
			if (getCampoConsultaFornecedor().equals("codigo")) {
				if (getValorConsultaFornecedor().equals("")) {
					setValorConsultaFornecedor("0");
				}
				int valorInt = Integer.parseInt(getValorConsultaFornecedor());
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCodigo(valorInt, "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("nome")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorNome(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("razaoSocial")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRazaoSocial(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("RG")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorRG(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CPF")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCPF(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}
			if (getCampoConsultaFornecedor().equals("CNPJ")) {
				objs = getFacadeFactory().getFornecedorFacade().consultarPorCNPJ(getValorConsultaFornecedor(), "AT", false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado());
			}

			setListaConsultaFornecedor(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaFornecedor(new ArrayList<FornecedorVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
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

	public String getValorConsultaFornecedor() {
		if (valorConsultaFornecedor == null) {
			valorConsultaFornecedor = "";
		}
		return valorConsultaFornecedor;
	}

	public void setValorConsultaFornecedor(String valorConsultaFornecedor) {
		this.valorConsultaFornecedor = valorConsultaFornecedor;
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

	
	public void consultarContaPagar() {
		try {
			setListaConsultaContaPagar(getFacadeFactory().getContaPagarFacade().consultarPorPeriodoTipoSacadoCodigoSacadoAPagar(getCampoConsultaContaPagar(), getValorConsultaContaPagar(), getDataInicioContaPagar(), getDataFinalContaPagar(), getNegociacaoContaPagarVO().getTipoSacado(), getNegociacaoContaPagarVO().getCodigoSacado(), getNegociacaoContaPagarVO().getUnidadeEnsino().getCodigo(), getUsuarioLogado()));			
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			getListaConsultaContaPagar().clear();
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

    public void adicionarTodos() {
        try {  
        	StringBuilder erro = new StringBuilder("");
            for(ContaPagarVO contaPagar:getListaConsultaContaPagar()) {     
            	try {
            		getFacadeFactory().getNegociacaoContaPagarFacade().adicionarObjContaPagarNegociadoVOs(getNegociacaoContaPagarVO(), contaPagar, getUsuarioLogado());
            	}catch (Exception e) {
					if (!erro.toString().contains(e.getMessage())) {
						if (erro.length() > 0) {
							erro.append("\n");
						}
						erro.append(e.getMessage());
					}
				}
            }
            if(!getNegociacaoContaPagarVO().getContaPagarNegociadoVOs().isEmpty()) {
            	setOncompleteModal("");
            }
            getListaConsultaContaPagar().clear();
            if(erro.length() > 0) {
            	setMensagemID(erro.toString());
            }else {
            	setMensagemID("msg_dados_adicionados");
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

    public void selecionarContaPagar() {
        try {
            ContaPagarVO obj = (ContaPagarVO) context().getExternalContext().getRequestMap().get("contaPagarItens");
            getFacadeFactory().getNegociacaoContaPagarFacade().adicionarObjContaPagarNegociadoVOs(getNegociacaoContaPagarVO(), obj, getUsuarioLogado());
            getListaConsultaContaPagar().remove(obj);
            setOncompleteModal("");
            setMensagemID("msg_dados_adicionados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }    

    public void consultarAluno() {
        try {

            List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
            if (getValorConsultaAluno().equals("")) {
                setMensagemID("msg_entre_prmconsulta");
                return;
            }
            if (getCampoConsultaAluno().equals("matricula")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatricula(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), true, false, getUsuarioLogado());
            }
            if (getCampoConsultaAluno().equals("nomePessoa")) {
                objs = getFacadeFactory().getMatriculaFacade().consultaRapidaPorNomePessoa(getValorConsultaAluno(), this.getUnidadeEnsinoLogado().getCodigo(), true, false, getUsuarioLogado());
            }
            setListaConsultaAluno(objs);
            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
            setListaConsultaAluno(new ArrayList<MatriculaVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }
    public List<SelectItem> tipoConsultaComboAluno;

    public List<SelectItem> getTipoConsultaComboAluno() {
    	if(tipoConsultaComboAluno == null) {
    		tipoConsultaComboAluno = new ArrayList<>(0);
    		tipoConsultaComboAluno.add(new SelectItem("nomePessoa", "Aluno/Candidato"));
    		tipoConsultaComboAluno.add(new SelectItem("matricula", "Matrícula"));
    	}
        return tipoConsultaComboAluno;
    }
    
    public List<SelectItem> tipoConsultaComboCandidato;

    public List<SelectItem> getTipoConsultaComboCandidato() {
    	if(tipoConsultaComboCandidato == null) {
    		tipoConsultaComboCandidato = new ArrayList<>(0);
    		tipoConsultaComboCandidato.add(new SelectItem("nomePessoa", "Nome"));
    	}
        return tipoConsultaComboCandidato;
    }

    public List<SelectItem> tipoConsultaComboFuncionario;
    public List<SelectItem> getTipoConsultaComboFuncionario() {
        if(tipoConsultaComboFuncionario == null) {
        	tipoConsultaComboFuncionario = new ArrayList<>(0);
        	tipoConsultaComboFuncionario.add(new SelectItem("nome", "Nome"));
        	tipoConsultaComboFuncionario.add(new SelectItem("CPF", "CPF"));
        	tipoConsultaComboFuncionario.add(new SelectItem("matricula", "Matrícula"));
        	tipoConsultaComboFuncionario.add(new SelectItem("cargo", "Cargo"));
        	tipoConsultaComboFuncionario.add(new SelectItem("departamento", "Departamento"));
        }
        return tipoConsultaComboFuncionario;
    }

    public void selecionarFuncionario() {
        FuncionarioVO obj = (FuncionarioVO) context().getExternalContext().getRequestMap().get("funcionarioItens");
        this.getNegociacaoContaPagarVO().setFuncionario(obj);
        this.getNegociacaoContaPagarVO().setPessoa(obj.getPessoa());        
        limparListaContaPagar();
        Uteis.liberarListaMemoria(getListaConsultaFuncionario());
        campoConsultaFuncionario = null;
        valorConsultaFuncionario = null;
    }

	public void selecionarAluno() {
		try {
			MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("alunoItens");
			carregarDadosMatricula(obj);
			Uteis.liberarListaMemoria(getListaConsultaAluno());			
			campoConsultaAluno = null;
			valorConsultaAluno = null;
		} catch (Exception e) {
			getNegociacaoContaPagarVO().setMatriculaAluno(new MatriculaVO());
			getNegociacaoContaPagarVO().getContaPagarNegociadoVOs().clear();
			getNegociacaoContaPagarVO().getContaPagarGeradaVOs().clear();			
			getNegociacaoContaPagarVO().getMatriculaAluno().setMatricula("");
			getNegociacaoContaPagarVO().getMatriculaAluno().getAluno().setNome("");
			getNegociacaoContaPagarVO().setPessoa(null);
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	
	private void carregarDadosMatricula(MatriculaVO obj) throws Exception{
		this.getNegociacaoContaPagarVO().setMatriculaAluno(obj);
		this.getNegociacaoContaPagarVO().setPessoa(obj.getAluno());
		limparListaContaPagar();		
	}

    public void consultarAlunoPorMatricula() {
        try {
            if (!getNegociacaoContaPagarVO().getMatriculaAluno().getMatricula().equals("")) {
                MatriculaVO objs = getFacadeFactory().getMatriculaFacade().consultarPorObjetoMatricula(getNegociacaoContaPagarVO().getMatriculaAluno().getMatricula(), this.getUnidadeEnsinoLogado().getCodigo(), true, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,  getUsuarioLogado());
                carregarDadosMatricula(objs);
                if (!objs.getMatricula().equals("")) {
                    setMensagemID("msg_dados_consultados");
                    return;
                }
                getNegociacaoContaPagarVO().getMatriculaAluno().setMatricula("");
                getNegociacaoContaPagarVO().getMatriculaAluno().getAluno().setNome("");
                setMensagemID("msg_erro_dadosnaoencontrados");
            }
        } catch (Exception e) {
            getNegociacaoContaPagarVO().setMatriculaAluno(new MatriculaVO());
            getNegociacaoContaPagarVO().setPessoa(null);
            limparListaContaPagar();
            getNegociacaoContaPagarVO().getMatriculaAluno().setMatricula("");
            getNegociacaoContaPagarVO().getMatriculaAluno().getAluno().setNome("");
            setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
        }
    }

    public void consultarFuncionarioPorCodigo() {
        try {
            if (!this.getNegociacaoContaPagarVO().getFuncionario().getMatricula().equals("") 
            		&& getNegociacaoContaPagarVO().getTipoFuncionario()) {
                FuncionarioVO funcionario = getFacadeFactory().getFuncionarioFacade().consultarPorRequisitanteMatricula(this.getNegociacaoContaPagarVO().getFuncionario().getMatricula(),
                        this.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                limparListaContaPagar();
                if (funcionario.getCodigo().intValue() != 0) {
                    this.getNegociacaoContaPagarVO().setFuncionario(funcionario);
                    this.getNegociacaoContaPagarVO().setPessoa(funcionario.getPessoa());
                    setMensagemID("msg_dados_consultados");
                } else {
                    throw new Exception();
                }
            }
        } catch (Exception e) {
            getNegociacaoContaPagarVO().getFuncionario().setCodigo(0);
            getNegociacaoContaPagarVO().getFuncionario().setMatricula("");
            getNegociacaoContaPagarVO().getFuncionario().getPessoa().setNome("");
            getNegociacaoContaPagarVO().setPessoa(null);
            limparListaContaPagar();
            setMensagemID("msg_erro_dadosnaoencontrados");
        }
    }

    public List<SelectItem> listaSelectItemSacado;
    public List<SelectItem> getListaSelectItemSacado() {
        if(listaSelectItemSacado == null) {
        	listaSelectItemSacado = new ArrayList<>(0);        	
        	listaSelectItemSacado.add(new SelectItem(TipoSacado.ALUNO, TipoSacado.ALUNO.getDescricao()));
        	listaSelectItemSacado.add(new SelectItem(TipoSacado.CANDIDATO, TipoSacado.CANDIDATO.getDescricao()));
        	listaSelectItemSacado.add(new SelectItem(TipoSacado.BANCO, TipoSacado.BANCO.getDescricao()));
        	listaSelectItemSacado.add(new SelectItem(TipoSacado.FORNECEDOR, TipoSacado.FORNECEDOR.getDescricao()));
        	listaSelectItemSacado.add(new SelectItem(TipoSacado.FUNCIONARIO_PROFESSOR, TipoSacado.FUNCIONARIO_PROFESSOR.getDescricao()));
        	listaSelectItemSacado.add(new SelectItem(TipoSacado.PARCEIRO, TipoSacado.PARCEIRO.getDescricao()));
        	listaSelectItemSacado.add(new SelectItem(TipoSacado.RESPONSAVEL_FINANCEIRO, TipoSacado.RESPONSAVEL_FINANCEIRO.getDescricao()));
        }
        
        return listaSelectItemSacado;
    }

    public void gerarParcelas() {
        try {
        	setAbrirModalRenegociacao("");
        	getFacadeFactory().getNegociacaoContaPagarFacade().gerarParcelas(getNegociacaoContaPagarVO(), getUsuarioLogado());
        	setValorTotalApresentar(getFacadeFactory().getNegociacaoContaPagarFacade().calcularValorTotalConfirmacaoNegociacao(getNegociacaoContaPagarVO()));
            limparMensagem();
            setAbrirModalRenegociacao("RichFaces.$('panelConfirmarNegociacao').show();");
            getFacadeFactory().getNegociacaoContaPagarFacade().realizarGeracaoLancamentoContabilJuroMultaDesconto(getNegociacaoContaPagarVO(), false, getUsuarioLogado());
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String getFecharModal() {
        if (!getNegociacaoContaPagarVO().getNovoObj()) {
            return "RichFaces.$('panelConfirmarNegociacao').hide(); RichFaces.$('panelNegociar').hide()";
        }
        return "";
    }

   

    /*
     * Método responsável por remover um novo objeto da classe <code>ContaReceberNegociado</code> do objeto
     * <code>negociacaoContaReceberVO</code> da classe <code>NegociacaoContaReceber</code>
     */
    public void removerContaPagarNegociado() throws Exception {
        ContaPagarNegociadoVO obj = (ContaPagarNegociadoVO) context().getExternalContext().getRequestMap().get("contaPagarNegociadoItens");
        getFacadeFactory().getNegociacaoContaPagarFacade().removerObjContaPagarNegociadoVOs(getNegociacaoContaPagarVO(), obj);
        if(getNegociacaoContaPagarVO().getContaPagarNegociadoVOs().isEmpty()) {
        	setOncompleteModal("RichFaces.$('panelContaPagar').show();");
        }
        setMensagemID("msg_dados_excluidos");

    }    
    
    /**
     * Método responsável por processar a consulta na entidade <code>Matricula</code> por meio de sua respectiva chave
     * primária. Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária
     * da entidade montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarMatriculaPorChavePrimaria() {
        try {
            String campoConsulta = getNegociacaoContaPagarVO().getMatriculaAluno().getMatricula();
            MatriculaVO matricula = getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(campoConsulta, getNegociacaoContaPagarVO().getUnidadeEnsino().getCodigo(),
                    NivelMontarDados.TODOS, getUsuarioLogado());
            carregarDadosMatricula(matricula); 
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            getNegociacaoContaPagarVO().getMatriculaAluno().setMatricula("");
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher o comboBox
     * relativo ao atributo <code>UnidadeEnsino</code>.
     */
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List<UnidadeEnsinoVO> resultadoConsulta = null;
        Iterator<UnidadeEnsinoVO> i = null;
        try {
            if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
                UnidadeEnsinoVO unidadeEnsino = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
                getListaSelectItemUnidadeEnsino().add(new SelectItem(unidadeEnsino.getCodigo(), unidadeEnsino.getNome()));
                getNegociacaoContaPagarVO().getUnidadeEnsino().setCodigo(unidadeEnsino.getCodigo());
                unidadeEnsino = null;
                return;
            }
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = i.next();
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

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>. Buscando todos os
     * objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem
     * de dados, isto é importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() throws Exception {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code> Este
     * atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox
     * correspondente
     */
    public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        return getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, 0, false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() throws Exception {
        montarListaSelectItemUnidadeEnsino();        
    }

    
    public void consultarParceiro() {
        try {
            super.consultar();
            List<ParceiroVO> objs = new ArrayList<>(0);
            if (getCampoConsultaParceiro().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getParceiroFacade().consultarPorCodigo(valorInt, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiro().equals("nome")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorNome(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiro().equals("razaoSocial")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorRazaoSocial(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiro().equals("RG")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorRG(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiro().equals("CPF")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorCPF(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getCampoConsultaParceiro().equals("tipoParceiro")) {
                objs = getFacadeFactory().getParceiroFacade().consultarPorTipoParceiro(getValorConsultaParceiro(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            setListaConsultaParceiro(objs);
            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setListaConsulta(new ArrayList<ParceiroVO>(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void selecionarParceiro() {
        try {
            ParceiroVO obj = (ParceiroVO) context().getExternalContext().getRequestMap().get("parceiroItens");
            getNegociacaoContaPagarVO().setParceiro(obj);
            limparListaContaPagar();                      
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    
    public List<SelectItem> tipoConsultaComboParceiro;
    public List<SelectItem> getTipoConsultaComboParceiro() {
    	if(tipoConsultaComboParceiro == null) {
    		tipoConsultaComboParceiro = new ArrayList<>(0);
    		tipoConsultaComboParceiro.add(new SelectItem("nome", "Nome"));
    		tipoConsultaComboParceiro.add(new SelectItem("razaoSocial", "Razão Social"));
    		tipoConsultaComboParceiro.add(new SelectItem("RG", "RG"));
    		tipoConsultaComboParceiro.add(new SelectItem("CPF", "CPF"));        
    	}
        return tipoConsultaComboParceiro;
    }

    public boolean isCampoData() {
        return getControleConsulta().getCampoConsulta().equals("data");
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
    	try {
    		removerObjetoMemoria(this);
    		setControleConsultaOtimizado(null);
    		getControleConsultaOtimizado().setLimitePorPagina(10);
    		getControleConsultaOtimizado().setPaginaAtual(0);
    		getControleConsultaOtimizado().setPage(0);
    		montarListaSelectItemUnidadeEnsino("");
    		setMensagemID("msg_entre_prmconsulta");
		} catch (Exception e) {
			 setMensagemDetalhada("msg_erro", e.getMessage());
		}
        return Uteis.getCaminhoRedirecionamentoNavegacao("negociacaoContaPagarCons.xhtml");
    }
    
    public List<SelectItem> getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList<>(0);
        }
        return (listaSelectItemUnidadeEnsino);
    }

    public void setListaSelectItemUnidadeEnsino(List<SelectItem> listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public String getCampoConsultaAluno() {
        return campoConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    public String getCampoConsultaFuncionario() {
        return campoConsultaFuncionario;
    }

    public void setCampoConsultaFuncionario(String campoConsultaFuncionario) {
        this.campoConsultaFuncionario = campoConsultaFuncionario;
    }

    public List<MatriculaVO> getListaConsultaAluno() {
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List<MatriculaVO> listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public List<FuncionarioVO> getListaConsultaFuncionario() {
        return listaConsultaFuncionario;
    }

    public void setListaConsultaFuncionario(List<FuncionarioVO> listaConsultaFuncionario) {
        this.listaConsultaFuncionario = listaConsultaFuncionario;
    }

    public String getValorConsultaAluno() {
        return valorConsultaAluno;
    }

    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

  

    public String getValorConsultaFuncionario() {
        return valorConsultaFuncionario;
    }

    public void setValorConsultaFuncionario(String valorConsultaFuncionario) {
        this.valorConsultaFuncionario = valorConsultaFuncionario;
    }
 
    public String getAbrirModalRenegociacao() {
        if (abrirModalRenegociacao == null) {
            abrirModalRenegociacao = "";
        }
        return abrirModalRenegociacao;
    }

    public void setAbrirModalRenegociacao(String abrirModalRenegociacao) {
        this.abrirModalRenegociacao = abrirModalRenegociacao;
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
  
    public boolean getIsDesabilitarUnidadeEnsino() {
        return getNegociacaoContaPagarVO().getPossuiSacado();
    }

    private List<SelectItem> listaSelectItemTipoDesconto;   
    public List<SelectItem> getListaSelectItemTipoDesconto() throws Exception {   
    	if(listaSelectItemTipoDesconto == null){
    		listaSelectItemTipoDesconto = new ArrayList<>(0);
    		listaSelectItemTipoDesconto.add(new SelectItem(TipoDescontoAluno.PORCENTO, TipoDescontoAluno.PORCENTO.getSimbolo()));
    		listaSelectItemTipoDesconto.add(new SelectItem(TipoDescontoAluno.VALOR, TipoDescontoAluno.VALOR.getSimbolo()));
    	}
    	return listaSelectItemTipoDesconto;
    }

    private List<PessoaVO> listaConsultaResponsavelFinanceiro;
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
            tipoConsultaComboResponsavelFinanceiro = new ArrayList<>(0);
            tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nome", "Nome"));
            tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("nomeAluno", "Aluno"));
            tipoConsultaComboResponsavelFinanceiro.add(new SelectItem("CPF", "CPF"));
        }
        return tipoConsultaComboResponsavelFinanceiro;
    }
    
    public void selecionarResponsavelFinanceiro() {
        try {
            PessoaVO obj = (PessoaVO) context().getExternalContext().getRequestMap().get("responsavelFinanceiroItens");
            this.getNegociacaoContaPagarVO().setPessoa(obj);
            limparListaContaPagar();
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public List<PessoaVO> getListaConsultaResponsavelFinanceiro() {
        if (listaConsultaResponsavelFinanceiro == null) {
            listaConsultaResponsavelFinanceiro = new ArrayList<>(0);
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

	public String getUsernameLiberarDesconto() {
		if (usernameLiberarDesconto == null) {
			usernameLiberarDesconto = "";
		}
		return usernameLiberarDesconto;
	}

	public void setUsernameLiberarDesconto(String usernameLiberarDesconto) {
		this.usernameLiberarDesconto = usernameLiberarDesconto;
	}

	public String getSenhaLiberarDesconto() {
		if (senhaLiberarDesconto == null) {
			senhaLiberarDesconto = "";
		}
		return senhaLiberarDesconto;
	}

	public void setSenhaLiberarDesconto(String senhaLiberarDesconto) {
		this.senhaLiberarDesconto = senhaLiberarDesconto;
	}

	public Boolean getApresentarBotaoLiberarDesconto() {
		if (apresentarBotaoLiberarDesconto == null) {
			apresentarBotaoLiberarDesconto = Boolean.TRUE;
		}
		return apresentarBotaoLiberarDesconto;
	}

	public void setApresentarBotaoLiberarDesconto(Boolean apresentarBotaoLiberarDesconto) {
		this.apresentarBotaoLiberarDesconto = apresentarBotaoLiberarDesconto;
	}
 	

	public List<SelectItem> getListaSelectItemTipoAcrescimo() {
		if(listaSelectItemTipoAcrescimo == null){
			listaSelectItemTipoAcrescimo = UtilSelectItem.getListaSelectItemEnum(TipoAcrescimoEnum.values(), Obrigatorio.SIM);
		}
		return listaSelectItemTipoAcrescimo;
	}

	public void setListaSelectItemTipoAcrescimo(List<SelectItem> listaSelectItemTipoAcrescimo) {
		this.listaSelectItemTipoAcrescimo = listaSelectItemTipoAcrescimo;
	}
	

	public void realizarCancelamentoGeracaoParcelas(){	
		getNegociacaoContaPagarVO().getContaPagarGeradaVOs().clear();
		getNegociacaoContaPagarVO().getLancamentoContabilCreditoVOs().clear();
		getNegociacaoContaPagarVO().getLancamentoContabilDebitoVOs().clear();
		calcularValorTotalConfirmacaoNegociacao();
	}

	public void realizarCalculoValorBaseParcela(){
		try {
			setValorBaseParcela(getFacadeFactory().getNegociacaoContaPagarFacade().realizarCalculoValorBaseParcela(getNegociacaoContaPagarVO()));			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
		
	}

	public Double getValorBaseParcela() {
		if(valorBaseParcela == null){
			valorBaseParcela = 0.0;
		}
		return valorBaseParcela;
	}

	public void setValorBaseParcela(Double valorBaseParcela) {
		this.valorBaseParcela = valorBaseParcela;
	}	
	
	public void limparListaContaPagar() {
		getNegociacaoContaPagarVO().setValor(0.0);
		getNegociacaoContaPagarVO().setValorEntrada(0.0);
		getNegociacaoContaPagarVO().setDesconto(0.0);
		getNegociacaoContaPagarVO().setValorTotal(0.0);
		getNegociacaoContaPagarVO().getContaPagarGeradaVOs().clear();
		getNegociacaoContaPagarVO().getContaPagarNegociadoVOs().clear();	
		setOncompleteModal("RichFaces.$('panelContaPagar').show();");
	}

	public NegociacaoContaPagarVO getNegociacaoContaPagarVO() {
		if (negociacaoContaPagarVO == null) {
			negociacaoContaPagarVO = new NegociacaoContaPagarVO();
		}
		return negociacaoContaPagarVO;
	}

	public void setNegociacaoContaPagarVO(NegociacaoContaPagarVO negociacaoContaPagarVO) {
		this.negociacaoContaPagarVO = negociacaoContaPagarVO;
	}

	public List<ContaPagarVO> getListaConsultaContaPagar() {
		if (listaConsultaContaPagar == null) {
			listaConsultaContaPagar = new ArrayList<>(0);
		}
		return listaConsultaContaPagar;
	}

	public void setListaConsultaContaPagar(List<ContaPagarVO> listaConsultaContaPagar) {
		this.listaConsultaContaPagar = listaConsultaContaPagar;
	}

	public void setTipoConsultaComboFornecedor(List<SelectItem> tipoConsultaComboFornecedor) {
		this.tipoConsultaComboFornecedor = tipoConsultaComboFornecedor;
	}
	
	public void setListaSelectItemTipoDesconto(List<SelectItem> listaSelectItemTipoDesconto) {
		this.listaSelectItemTipoDesconto = listaSelectItemTipoDesconto;
	}

	public void setTipoConsultaComboResponsavelFinanceiro(List<SelectItem> tipoConsultaComboResponsavelFinanceiro) {
		this.tipoConsultaComboResponsavelFinanceiro = tipoConsultaComboResponsavelFinanceiro;
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


	public Date getDataInicioContaPagar() {
		if (dataInicioContaPagar == null) {
			dataInicioContaPagar = Uteis.getDataPrimeiroDiaMes(new Date());
		}
		return dataInicioContaPagar;
	}


	public void setDataInicioContaPagar(Date dataInicioContaPagar) {
		this.dataInicioContaPagar = dataInicioContaPagar;
	}


	public Date getDataFinalContaPagar() {
		if (dataFinalContaPagar == null) {
			dataFinalContaPagar = Uteis.getDataUltimoDiaMes(new Date());
		}
		return dataFinalContaPagar;
	}


	public void setDataFinalContaPagar(Date dataFinalContaPagar) {
		this.dataFinalContaPagar = dataFinalContaPagar;
	}


	public List<SelectItem> getListaSelectItemBanco() {
		if (listaSelectItemBanco == null) {
			listaSelectItemBanco =new ArrayList<>(0);
			try {				
				listaSelectItemBanco = UtilSelectItem.getListaSelectItem(getFacadeFactory().getBancoFacade().consultarPorNome("", false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()), 
						"codigo", "nome");
			} catch (Exception e) {			
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
			
		}
		return listaSelectItemBanco;
	}


	public void setListaSelectItemBanco(List<SelectItem> listaSelectItemBanco) {
		this.listaSelectItemBanco = listaSelectItemBanco;
	}


	public List<SelectItem> getListaSelectItemOperadoraCartao() {
		if (listaSelectItemOperadoraCartao == null) {
			listaSelectItemOperadoraCartao = new ArrayList<>(0);
			try {				
				listaSelectItemOperadoraCartao = UtilSelectItem.getListaSelectItem(getFacadeFactory().getOperadoraCartaoFacade().consultarPorTipo(TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO.name(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()), 
						"codigo", "nome");
			} catch (Exception e) {			
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
			
		}
		return listaSelectItemOperadoraCartao;
	}


	public void setListaSelectItemOperadoraCartao(List<SelectItem> listaSelectItemOperadoraCartao) {
		this.listaSelectItemOperadoraCartao = listaSelectItemOperadoraCartao;
	}
	
	public void removerCentroResultadoOrigem() {
		try {
			ContaPagarVO contaPagar = (ContaPagarVO) getRequestMap().get("contaPagarItens");
			CentroResultadoOrigemVO centroResultadoOrigem = (CentroResultadoOrigemVO) getRequestMap().get("centroResultadoOrigemItem");
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().removerCentroResultadoOrigem(contaPagar.getListaCentroResultadoOrigemVOs(), centroResultadoOrigem, getUsuarioLogado());
			getFacadeFactory().getNegociacaoContaPagarFacade().realizarGeracaoLancamentoContabilJuroMultaDesconto(getNegociacaoContaPagarVO(), false, getUsuarioLogado());
			setMensagemID("msg_dados_removidos", Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public void calcularValorTotalConfirmacaoNegociacao() {
   	 try {
   		 	setValorTotalApresentar(getFacadeFactory().getNegociacaoContaPagarFacade().calcularValorTotalConfirmacaoNegociacao(getNegociacaoContaPagarVO()));
			limparMensagem();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
   }
	
	public void calcularValorTotalConfirmacaoNegociacaoAlterandoValorConta() {
	   	 try {
	   		 	ContaPagarVO contaPagar = (ContaPagarVO) getRequestMap().get("contaPagarItens");
	   		 	getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarDistribuicaoValoresCentroResultado(contaPagar.getListaCentroResultadoOrigemVOs(), contaPagar.getValorFinalTemp(), getUsuarioLogado());
				setValorTotalApresentar(getFacadeFactory().getNegociacaoContaPagarFacade().calcularValorTotalConfirmacaoNegociacao(getNegociacaoContaPagarVO()));
				getFacadeFactory().getNegociacaoContaPagarFacade().realizarGeracaoLancamentoContabilJuroMultaDesconto(negociacaoContaPagarVO, false, getUsuarioLogado());
				limparMensagem();
			} catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
	   }
	
	 public Double getValorTotalApresentar() {
	        if (valorTotalApresentar == null) {
	            valorTotalApresentar = 0.0;
	        }
	        return valorTotalApresentar;
	    }

	    public void setValorTotalApresentar(Double valorTotalApresentar) {
	        this.valorTotalApresentar = valorTotalApresentar;
	    }
	    
	    
	    public void gerarOpcaoPagamento() {
	        try {
	            setAbrirModalRenegociacao("");
	            getFacadeFactory().getNegociacaoContaPagarFacade().validarDadosBasicos(getNegociacaoContaPagarVO());	            
	            setAbrirModalRenegociacao("RichFaces.$('panelNegociar').show()");
	            limparMensagem();
	        } catch (Exception e) {
	            setMensagemDetalhada("msg_erro", e.getMessage());
	        }
	    }
	    
	    
	    public void estornarNegociacaoContaPagar(){
	    	try {
	    		setOncompleteModal("");
	    		for(ContaPagarVO contaPagar: getNegociacaoContaPagarVO().getContaPagarGeradaVOs()) {
	    			getFacadeFactory().getContaPagarFacade().validarSeContaPagarExisteVinculoComArquivoRemessa(contaPagar);
	    		}
	    		setOncompleteModal("RichFaces.$('panelEstornoRenegociacaoPagar').show();");	        	
			} catch (Exception e) {
				  setMensagemDetalhada("msg_erro", e.getMessage());
			}
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

		public CentroResultadoOrigemVO getCentroResultadoOrigemVO() {
			if (centroResultadoOrigemVO == null) {
				centroResultadoOrigemVO = new CentroResultadoOrigemVO();
			}
			return centroResultadoOrigemVO;
		}

		public void setCentroResultadoOrigemVO(CentroResultadoOrigemVO centroResultadoOrigemVO) {
			this.centroResultadoOrigemVO = centroResultadoOrigemVO;
		}
		
		public void novoCentroResultadoOrigem() {
			setContaPagarVO((ContaPagarVO)getRequestMap().get("contaPagarItens"));
			setCentroResultadoOrigemVO(new CentroResultadoOrigemVO());	
			getCentroResultadoOrigemVO().setValor(Uteis.arrendondarForcando2CadasDecimais(getContaPagarVO().getValorFinalTemp() - getContaPagarVO().getPrecoCentroResultadoTotal()));
			getCentroResultadoOrigemVO().calcularPorcentagem(getContaPagarVO().getValorFinalTemp());
		}
		
		public void adicionarCentroResultadoVO() {
			try {
				setOncompleteModal("");
				getCentroResultadoOrigemVO().setQuantidade(1.0);
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().adicionarCentroResultadoOrigem(getContaPagarVO().getListaCentroResultadoOrigemVOs(), getCentroResultadoOrigemVO(), getContaPagarVO().getValorFinalTemp(), true, getUsuarioLogado());
				setCentroResultadoOrigemVO(getCentroResultadoOrigemVO().getClone());
				getFacadeFactory().getNegociacaoContaPagarFacade().realizarGeracaoLancamentoContabilJuroMultaDesconto(getNegociacaoContaPagarVO(), false, getUsuarioLogado());
				setOncompleteModal("RichFaces.$('panelCentroResultado').hide();");
				setMensagemID("msg_dados_adicionados");
			}catch (Exception e) {
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}

		public void consultarCentroDespesa() {
			try {
				List<CategoriaDespesaVO> objs = new ArrayList<>(0);
				if (getValorConsultaCentroDespesa().equals("")) {
					setMensagemID("msg_entre_prmconsulta");
					return;
				}				
				if (getCampoConsultaCentroDespesa().equals("descricao")) {
					objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorDescricao(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				}
				if (getCampoConsultaCentroDespesa().equals("identificadorCentroDespesa")) {
					objs = getFacadeFactory().getCategoriaDespesaFacade().consultarPorIdentificadorCategoriaDespesa(getValorConsultaCentroDespesa(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				}
				setListaConsultaCentroDespesa(objs);
				setMensagemID("msg_dados_consultados");
			} catch (Exception e) {
				setListaConsultaCentroDespesa(new ArrayList<CategoriaDespesaVO>(0));
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}	
		
		public void selecionarCentroDespesa() {
			try {
				CategoriaDespesaVO obj = (CategoriaDespesaVO) context().getExternalContext().getRequestMap().get("centroDespesaItens");
				getCentroResultadoOrigemVO().setCategoriaDespesaVO(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
				limparDadosPorCategoriaDespesa();
				montarListaSelectItemTipoNivelCentroResultadoEnum();
				montarListaSelectItemUnidadeEnsinoCategoriaDespesa();
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		
		public void limparDadosPorCategoriaDespesa() {
			getCentroResultadoOrigemVO().setUnidadeEnsinoVO(null);
			getCentroResultadoOrigemVO().setDepartamentoVO(null);
			getCentroResultadoOrigemVO().setFuncionarioCargoVO(null);
			getCentroResultadoOrigemVO().setTurmaVO(null);
			getCentroResultadoOrigemVO().setCursoVO(null);
			getCentroResultadoOrigemVO().setTurnoVO(null);
			getCentroResultadoOrigemVO().setCentroResultadoAdministrativo(null);
		}
		
		public List<SelectItem> tipoConsultaComboCentroDespesa;
		public List<SelectItem> getTipoConsultaComboCentroDespesa() {
			if(tipoConsultaComboCentroDespesa == null) {
				tipoConsultaComboCentroDespesa = new ArrayList<>(0);
				tipoConsultaComboCentroDespesa.add(new SelectItem("descricao", "Descrição"));
				tipoConsultaComboCentroDespesa.add(new SelectItem("identificadorCentroDespesa", "Identificador Centro Despesa"));
			}
			return tipoConsultaComboCentroDespesa;
		}
		
		public String getCampoConsultaCentroDespesa() {
			if (campoConsultaCentroDespesa == null) {
				campoConsultaCentroDespesa = "";
			}
			return campoConsultaCentroDespesa;
		}

		public void setCampoConsultaCentroDespesa(String campoConsultaCentroDespesa) {
			this.campoConsultaCentroDespesa = campoConsultaCentroDespesa;
		}

		public List<CategoriaDespesaVO> getListaConsultaCentroDespesa() {
			if (listaConsultaCentroDespesa == null) {
				listaConsultaCentroDespesa = new ArrayList<>(0);
			}
			return listaConsultaCentroDespesa;
		}

		public void setListaConsultaCentroDespesa(List<CategoriaDespesaVO> listaConsultaCentroDespesa) {
			this.listaConsultaCentroDespesa = listaConsultaCentroDespesa;
		}

		public String getValorConsultaCentroDespesa() {
			if (valorConsultaCentroDespesa == null) {
				valorConsultaCentroDespesa = "";
			}
			return valorConsultaCentroDespesa;
		}

		public void setValorConsultaCentroDespesa(String valorConsultaCentroDespesa) {
			this.valorConsultaCentroDespesa = valorConsultaCentroDespesa;
		}
		
		public void montarListaSelectItemUnidadeEnsinoCategoriaDespesa() throws Exception {
			getListaSelectItemUnidadeEnsinoCategoriaDespesa().clear();
			if (getUnidadeEnsinoLogado().getCodigo().intValue() != 0) {
				getListaSelectItemUnidadeEnsinoCategoriaDespesa().add(new SelectItem(getUnidadeEnsinoLogado().getCodigo(), getUnidadeEnsinoLogado().getNome()));
				return;
			}
			getListaSelectItemUnidadeEnsinoCategoriaDespesa().add(new SelectItem(0, ""));
			List<UnidadeEnsinoVO> listaUnidadeEnsino = consultarUnidadeEnsinoPorCodigo(0);
			listaUnidadeEnsino.stream().forEach(p -> getListaSelectItemUnidadeEnsinoCategoriaDespesa().add(new SelectItem(p.getCodigo(), p.getNome())));

		}
		
		public List<SelectItem> getListaSelectItemUnidadeEnsinoCategoriaDespesa() {
			listaSelectItemUnidadeEnsinoCategoriaDespesa = Optional.ofNullable(listaSelectItemUnidadeEnsinoCategoriaDespesa).orElse(new ArrayList<>());
			return listaSelectItemUnidadeEnsinoCategoriaDespesa;
		}

		public void setListaSelectItemUnidadeEnsinoCategoriaDespesa(List<SelectItem> listaSelectItemUnidadeEnsinoCategoriaDespesa) {
			this.listaSelectItemUnidadeEnsinoCategoriaDespesa = listaSelectItemUnidadeEnsinoCategoriaDespesa;
		}
		
		/**
		 * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>numero</code> Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
		 */
		public List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorCodigo(Integer numeroPrm) throws Exception {
			List<UnidadeEnsinoVO> lista = null;
			if ((numeroPrm != null && !numeroPrm.equals(0)) || (getUnidadeEnsinoLogado().getCodigo() != null && !getUnidadeEnsinoLogado().getCodigo().equals(0))) {
				lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorCodigo(numeroPrm, getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			} else {
				lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarTodasUnidades(false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
			}
			return lista;
		}
		
		public List<SelectItem> tipoConsultaComboTurma;
		public List<SelectItem> getTipoConsultaComboTurma() {
			if(tipoConsultaComboTurma == null) {
				tipoConsultaComboTurma = new ArrayList<>(0);
				tipoConsultaComboTurma.add(new SelectItem("identificadorTurma", "Identificador"));
				tipoConsultaComboTurma.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
				tipoConsultaComboTurma.add(new SelectItem("nomeTurno", "Turno"));
				tipoConsultaComboTurma.add(new SelectItem("nomeCurso", "Curso"));
			}
			return tipoConsultaComboTurma;
		}

		public void limparTurma() {
			try {
				getCentroResultadoOrigemVO().setTurmaVO(new TurmaVO());
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}

		public void selecionarTurma() {
			try {
				TurmaVO obj = (TurmaVO) context().getExternalContext().getRequestMap().get("turmaItens");
				obj = getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				getCentroResultadoOrigemVO().setTurmaVO(obj);
				getCentroResultadoOrigemVO().setCursoVO(obj.getCurso());
				getCentroResultadoOrigemVO().setTurnoVO(obj.getTurno());
				preencherDadosPorCategoriaDespesa();
				setValorConsultaTurma("");
				setCampoConsultaTurma("");
				setListaConsultaTurma(null);
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}

		public void consultarTurma() {
			try {
				super.consultar();
				Uteis.checkState(!Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO()), "O campo Unidade Ensino deve ser informado.");
				setListaConsultaTurma(getFacadeFactory().getTurmaFacade().consultar(getCampoConsultaTurma(), getValorConsultaTurma(), getCentroResultadoOrigemVO().getUnidadeEnsinoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
				setMensagemID("msg_dados_consultados");
			} catch (Exception e) {
				getListaConsultaTurma().clear();
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}

		public String getCampoConsultaTurma() {
			if (campoConsultaTurma == null) {
				campoConsultaTurma = "";
			}
			return campoConsultaTurma;
		}

		public void setCampoConsultaTurma(String campoConsultaTurma) {
			this.campoConsultaTurma = campoConsultaTurma;
		}

		public String getValorConsultaTurma() {
			if (valorConsultaTurma == null) {
				valorConsultaTurma = "";
			}
			return valorConsultaTurma;
		}

		public void setValorConsultaTurma(String valorConsultaTurma) {
			this.valorConsultaTurma = valorConsultaTurma;
		}

		public List<TurmaVO> getListaConsultaTurma() {
			if (listaConsultaTurma == null) {
				listaConsultaTurma = new ArrayList<>(0);
			}
			return listaConsultaTurma;
		}

		public void setListaConsultaTurma(List<TurmaVO> listaConsultaTurma) {
			this.listaConsultaTurma = listaConsultaTurma;
		}

		public List<SelectItem> tipoConsultaComboCurso;
		public List<SelectItem> getTipoConsultaComboCurso() {
			if(tipoConsultaComboCurso == null) {
				tipoConsultaComboCurso = new ArrayList<>(0);
				tipoConsultaComboCurso.add(new SelectItem("nome", "Nome"));
			}
			return tipoConsultaComboCurso;
		}

		public List<SelectItem> tipoConsultaComboCursoTurno;
		public List<SelectItem> getTipoConsultaComboCursoTurno() {
			if(tipoConsultaComboCursoTurno == null) {
			tipoConsultaComboCursoTurno = new ArrayList<>(0);
			tipoConsultaComboCursoTurno.add(new SelectItem("curso", "Curso"));
			tipoConsultaComboCursoTurno.add(new SelectItem("turno", "Turno"));
			}
			return tipoConsultaComboCursoTurno;
		}

		public void limparCurso() {
			try {
				getCentroResultadoOrigemVO().setCursoVO(new CursoVO());
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}

		public void consultarCurso() {
			try {
				Uteis.checkState(!Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO()), "O campo Unidade Ensino deve ser informado.");
				setListaConsultaCurso(getFacadeFactory().getCursoFacade().consultar(getCampoConsultaCurso(), getValorConsultaCurso(), getCentroResultadoOrigemVO().getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado()));
				setMensagemID("msg_dados_consultados");
			} catch (Exception e) {
				setListaConsultaCurso(null);
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}

		public void selecionarCurso() {
			try {
				CursoVO obj = (CursoVO) context().getExternalContext().getRequestMap().get("cursoItens");				
				getCentroResultadoOrigemVO().setCursoVO(obj);
				preencherDadosPorCategoriaDespesa();
				listaConsultaCurso.clear();
				this.setValorConsultaCurso("");
				this.setCampoConsultaCurso("");
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}

		public String getCampoConsultaCurso() {
			if (campoConsultaCurso == null) {
				campoConsultaCurso = "";
			}
			return campoConsultaCurso;
		}

		public void setCampoConsultaCurso(String campoConsultaCurso) {
			this.campoConsultaCurso = campoConsultaCurso;
		}

		public List<CursoVO> getListaConsultaCurso() {
			if (listaConsultaCurso == null) {
				listaConsultaCurso = new ArrayList<>(0);
			}
			return listaConsultaCurso;
		}

		public void setListaConsultaCurso(List<CursoVO> listaConsultaCurso) {
			this.listaConsultaCurso = listaConsultaCurso;
		}

		public String getValorConsultaCurso() {
			if (valorConsultaCurso == null) {
				valorConsultaCurso = "";
			}
			return valorConsultaCurso;
		}

		public void setValorConsultaCurso(String valorConsultaCurso) {
			this.valorConsultaCurso = valorConsultaCurso;
		}

		public void consultarCursoTurno() {
			try {
				Uteis.checkState(!Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO()), "O campo Unidade Ensino deve ser informado.");
				setListaConsultaCursoTurno(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultar(getCampoConsultaCursoTurno(), getValorConsultaCursoTurno(), getCentroResultadoOrigemVO().getUnidadeEnsinoVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
				setMensagemID("msg_dados_consultados");
			} catch (Exception e) {
				setListaConsultaCursoTurno(null);
				setMensagemDetalhada("msg_erro", e.getMessage());
			}
		}

		public void limparTurno() {
			try {
				getCentroResultadoOrigemVO().setTurnoVO(new TurnoVO());
				getCentroResultadoOrigemVO().setCursoVO(new CursoVO());
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		

		public String getCampoConsultaCursoTurno() {
			if (campoConsultaCursoTurno == null) {
				campoConsultaCursoTurno = "";
			}
			return campoConsultaCursoTurno;
		}

		public String getValorConsultaCursoTurno() {
			if (valorConsultaCursoTurno == null) {
				valorConsultaCursoTurno = "";
			}
			return valorConsultaCursoTurno;
		}

		public List<UnidadeEnsinoCursoVO> getListaConsultaCursoTurno() {
			if (listaConsultaCursoTurno == null) {
				listaConsultaCursoTurno = new ArrayList<>(0);
			}
			return listaConsultaCursoTurno;
		}

		public void setCampoConsultaCursoTurno(String campoConsultaCursoTurno) {
			this.campoConsultaCursoTurno = campoConsultaCursoTurno;
		}

		public void setValorConsultaCursoTurno(String valorConsultaCursoTurno) {
			this.valorConsultaCursoTurno = valorConsultaCursoTurno;
		}

		public void setListaConsultaCursoTurno(List<UnidadeEnsinoCursoVO> listaConsultaCursoTurno) {
			this.listaConsultaCursoTurno = listaConsultaCursoTurno;
		}

		public void selecionarCursoTurno() {
			try {
				UnidadeEnsinoCursoVO obj = (UnidadeEnsinoCursoVO) context().getExternalContext().getRequestMap().get("unidadeEnsinoCursoItens");
				getCentroResultadoOrigemVO().setCursoVO(obj.getCurso());
				getCentroResultadoOrigemVO().setTurnoVO(obj.getTurno());
				preencherDadosPorCategoriaDespesa();
				setListaConsultaCursoTurno(null);
				this.setValorConsultaCursoTurno("");
				this.setCampoConsultaCursoTurno("");
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		
		public void limparCamposPorTipoNivelCentroResultadoEnum() {
			try {
				getCentroResultadoOrigemVO().limparCamposPorTipoNivelCentroResultadoEnum();
				preencherDadosPorCategoriaDespesa();
				inicializarMensagemVazia();
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}

		}
		
		private void montarListaSelectItemTipoNivelCentroResultadoEnum() {
			try {
				getListaSelectItemTipoNivelCentroResultadoEnum().clear();
				if(getCentroResultadoOrigemVO().isCategoriaDespesaInformada()){
					getFacadeFactory().getCategoriaDespesaFacade().montarListaSelectItemTipoNivelCentroResultadoEnum(getCentroResultadoOrigemVO().getCategoriaDespesaVO(), getListaSelectItemTipoNivelCentroResultadoEnum());
					if(!getListaSelectItemTipoNivelCentroResultadoEnum().isEmpty() && !Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getTipoNivelCentroResultadoEnum())){
						getCentroResultadoOrigemVO().setTipoNivelCentroResultadoEnum((TipoNivelCentroResultadoEnum) getListaSelectItemTipoNivelCentroResultadoEnum().get(0).getValue());	
					}
				}
				inicializarMensagemVazia();
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
			
		}

		public void inicializarDadoConsultaCentroResultadoAdministrativo() {
			try {
				setCentroResultadoAdministrativo(true);
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
				if (isCentroResultadoAdministrativo()) {
					getCentroResultadoOrigemVO().setCentroResultadoAdministrativo(obj);
				}
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		
		public DataModelo getCentroResultadoDataModelo() {
			centroResultadoDataModelo = Optional.ofNullable(centroResultadoDataModelo).orElse(new DataModelo());
			return centroResultadoDataModelo;
		}

		public void setCentroResultadoDataModelo(DataModelo centroResultadoDataModelo) {
			this.centroResultadoDataModelo = centroResultadoDataModelo;
		}

		public boolean isCentroResultadoAdministrativo() {
			return centroResultadoAdministrativo;
		}

		public void setCentroResultadoAdministrativo(boolean centroResultadoAdministrativo) {
			this.centroResultadoAdministrativo = centroResultadoAdministrativo;
		}

		public Boolean permiteAlterarCentroResultado;
		public Boolean getPermiteAlterarCentroResultado() {
			if(permiteAlterarCentroResultado == null) {
				try {
					ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComPerfilUsuarioVOEspecifico(PerfilAcessoPermissaoFinanceiroEnum.PERMITE_ALTERAR_CENTRO_RESULTADO_CONTA_PAGAR, getUsuarioLogado());
					permiteAlterarCentroResultado = true;
				} catch (Exception e) {
					permiteAlterarCentroResultado = false;
				}
			}
			return permiteAlterarCentroResultado;
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
				getFacadeFactory().getCentroResultadoFacade().consultar(SituacaoEnum.ATIVO, true, getCentroResultadoOrigemVO().getDepartamentoVO(), getCentroResultadoOrigemVO().getCursoVO(), getCentroResultadoOrigemVO().getTurmaVO(), getCentroResultadoDataModelo());
				setMensagemID(MSG_TELA.msg_dados_consultados.name());
			} catch (Exception e) {
				getControleConsultaOtimizado().getListaConsulta().clear();
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}

		public void consultarFuncionarioCentroCusto() {
			try {
				List<FuncionarioCargoVO> objs = new ArrayList<>(0);
				if (getCampoConsultaFuncionarioCentroCusto().equals("nomeFuncionario")) {
					objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeFuncionarioUnidadeEnsinoSituacao(getValorConsultaFuncionarioCentroCusto(), null, true, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				if (getCampoConsultaFuncionarioCentroCusto().equals("nomeCargo")) {
					objs = getFacadeFactory().getFuncionarioCargoFacade().consultarPorNomeCargoUnidadeEnsinoSituacao(getValorConsultaFuncionarioCentroCusto(), null, true, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				setListaConsultaFuncionarioCentroCusto(objs);
				setMensagemID(MSG_TELA.msg_dados_consultados.name());
			} catch (Exception e) {
				setListaConsultaFuncionarioCentroCusto(new ArrayList<FuncionarioCargoVO>(0));
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}

		public void selecionarFuncionarioCentroCusto() {
			try {
				FuncionarioCargoVO obj = (FuncionarioCargoVO) context().getExternalContext().getRequestMap().get("funcionarioCentroCustoItens");
				getCentroResultadoOrigemVO().setFuncionarioCargoVO(obj);
				preencherDadosPorCategoriaDespesa();
				Uteis.liberarListaMemoria(getListaConsultaFuncionarioCentroCusto());
				campoConsultaFuncionarioCentroCusto = null;
				valorConsultaFuncionarioCentroCusto = null;
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			} catch (Exception e) {								
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		
		public void preencherDadosPorCategoriaDespesa() {
			try {
				if(Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getCategoriaDespesaVO())){
					getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().preencherDadosPorCategoriaDespesa(getCentroResultadoOrigemVO(), getUsuarioLogado());	
				}
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}

		public List<SelectItem> tipoConsultaComboFuncionarioCentroCusto;
		public List<SelectItem> getTipoConsultaComboFuncionarioCentroCusto() {
			if(tipoConsultaComboFuncionarioCentroCusto == null) {
				tipoConsultaComboFuncionarioCentroCusto = new ArrayList<>(0);
				tipoConsultaComboFuncionarioCentroCusto.add(new SelectItem("nomeFuncionario", "Nome"));
				tipoConsultaComboFuncionarioCentroCusto.add(new SelectItem("nomeCargo", "Cargo"));
			}
			return tipoConsultaComboFuncionarioCentroCusto;
		}

		public void realizarDistribuicaoValoresCentroResultado() {
			try {
				getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().realizarDistribuicaoValoresCentroResultado(getContaPagarVO().getListaCentroResultadoOrigemVOs(), getContaPagarVO().getValorFinalTemp(), getUsuarioLogado());
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}

		public void calcularPorcentagemCentroResultadoOrigem() {
			try {
				getCentroResultadoOrigemVO().calcularPorcentagem(getContaPagarVO().getValorFinalTemp());
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}

		public void calcularValorCentroResultadoOrigem() {
			try {
				getCentroResultadoOrigemVO().calcularValor(getContaPagarVO().getValorFinalTemp());
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		
		public void limparCentroResultadoAdministrativo() {
			getCentroResultadoOrigemVO().setCentroResultadoAdministrativo(new CentroResultadoVO());
		}
		
		public List<SelectItem> getListaSelectItemTipoNivelCentroResultadoEnum() {
			listaSelectItemTipoNivelCentroResultadoEnum = Optional.ofNullable(listaSelectItemTipoNivelCentroResultadoEnum).orElse(new ArrayList<>());
			return listaSelectItemTipoNivelCentroResultadoEnum;
		}

		public void setListaSelectItemTipoNivelCentroResultadoEnum(List<SelectItem> listaSelectItemTipoNivelCentroResultadoEnum) {
			this.listaSelectItemTipoNivelCentroResultadoEnum = listaSelectItemTipoNivelCentroResultadoEnum;
		}


		public String getCampoConsultaFuncionarioCentroCusto() {
			return campoConsultaFuncionarioCentroCusto;
		}

		public void setCampoConsultaFuncionarioCentroCusto(String campoConsultaFuncionario) {
			this.campoConsultaFuncionarioCentroCusto = campoConsultaFuncionario;
		}

		public List<FuncionarioCargoVO> getListaConsultaFuncionarioCentroCusto() {
			return listaConsultaFuncionarioCentroCusto;
		}

		public void setListaConsultaFuncionarioCentroCusto(List<FuncionarioCargoVO> listaConsultaFuncionario) {
			this.listaConsultaFuncionarioCentroCusto = listaConsultaFuncionario;
		}

		public String getValorConsultaFuncionarioCentroCusto() {
			return valorConsultaFuncionarioCentroCusto;
		}

		public void setValorConsultaFuncionarioCentroCusto(String valorConsultaFuncionario) {
			this.valorConsultaFuncionarioCentroCusto = valorConsultaFuncionario;
		}


		public void consultarDepartamento() {
			try {
				List<DepartamentoVO> objs = new ArrayList<>(0);
				Integer unidadeEnsino = 0;
				if (Uteis.isAtributoPreenchido(getCentroResultadoOrigemVO().getUnidadeEnsinoVO())) {
					unidadeEnsino = getCentroResultadoOrigemVO().getUnidadeEnsinoVO().getCodigo();
				} else {
					unidadeEnsino = getUnidadeEnsinoLogado().getCodigo();
				}
				if (getCampoConsultaDepartamento().equals("codigo")) {
					int valorInt = 0;
					if (!getValorConsultaDepartamento().equals("")) {
						valorInt = Integer.parseInt(getValorConsultaDepartamento());
					}
					objs = getFacadeFactory().getDepartamentoFacade().consultarPorCodigoPorUnidadeEnsino(valorInt, unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				if (getCampoConsultaDepartamento().equals("nome")) {
					objs = getFacadeFactory().getDepartamentoFacade().consultarPorNomePorUnidadeEnsino(getValorConsultaDepartamento(), unidadeEnsino, true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
				}
				setListaConsultaDepartamento(objs);
				setMensagemID("msg_dados_consultados");
				
			} catch (Exception e) {
				setListaConsultaDepartamento(new ArrayList<DepartamentoVO>(0));
				setMensagemDetalhada("msg_erro", e.getMessage());				
			}
		}

		public void selecionarDepartamento() {
			try {
				DepartamentoVO obj = (DepartamentoVO) context().getExternalContext().getRequestMap().get("departamentoItens");
				getCentroResultadoOrigemVO().setDepartamentoVO(obj);
				preencherDadosPorCategoriaDespesa();
				setCampoConsultaDepartamento("");
				setValorConsultaDepartamento("");
				getListaConsultaDepartamento().clear();
				setMensagemID(MSG_TELA.msg_dados_selecionados.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}

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

		public List<DepartamentoVO> getListaConsultaDepartamento() {
			if (listaConsultaDepartamento == null) {
				listaConsultaDepartamento = new ArrayList<>(0);
			}
			return listaConsultaDepartamento;
		}

		public void setListaConsultaDepartamento(List<DepartamentoVO> listaConsultaDepartamento) {
			this.listaConsultaDepartamento = listaConsultaDepartamento;
		}


		public List<SelectItem> tipoConsultaComboDepartamento;
		public List<SelectItem> getTipoConsultaComboDepartamento() {
			if(tipoConsultaComboDepartamento == null) {
				tipoConsultaComboDepartamento = new ArrayList<>(0);
				tipoConsultaComboDepartamento.add(new SelectItem("nome", "Nome"));
				tipoConsultaComboDepartamento.add(new SelectItem("codigo", "Código"));
			}
			return tipoConsultaComboDepartamento;
		}


		public void limparSacado() {
			getNegociacaoContaPagarVO().setMatriculaAluno(null);
			getNegociacaoContaPagarVO().setPessoa(null);
			getNegociacaoContaPagarVO().setParceiro(null);
			getNegociacaoContaPagarVO().setFuncionario(null);
			getNegociacaoContaPagarVO().setFornecedor(null);
			getNegociacaoContaPagarVO().setOperadoraCartaoVO(null);			
			limparListaContaPagar();
		}

		public List<SelectItem> getTipoConsultaComboContaPagar() {
			if (tipoConsultaComboContaPagar == null) {
				tipoConsultaComboContaPagar = new ArrayList<>(0);
				tipoConsultaComboContaPagar.add(new SelectItem("categoriaDespesa", "Categoria Despesa"));
				tipoConsultaComboContaPagar.add(new SelectItem("codigo", "Número (Código)"));
				tipoConsultaComboContaPagar.add(new SelectItem("departamento", "Departamento"));
				tipoConsultaComboContaPagar.add(new SelectItem("notaFiscal", "Nota Fiscal"));				
				tipoConsultaComboContaPagar.add(new SelectItem("nrDocumento", "Nrº Documento"));
				tipoConsultaComboContaPagar.add(new SelectItem("turma", "Turma"));
			}
			return tipoConsultaComboContaPagar;
		}

		public void setTipoConsultaComboContaPagar(List<SelectItem> tipoConsultaComboContaPagar) {
			this.tipoConsultaComboContaPagar = tipoConsultaComboContaPagar;
		}

		public String getCampoConsultaContaPagar() {
			if (campoConsultaContaPagar == null) {
				campoConsultaContaPagar = "notaFiscal";
			}
			return campoConsultaContaPagar;
		}

		public void setCampoConsultaContaPagar(String campoConsultaContaPagar) {
			this.campoConsultaContaPagar = campoConsultaContaPagar;
		}

		public String getValorConsultaContaPagar() {
			if (valorConsultaContaPagar == null) {
				valorConsultaContaPagar = "";
			}
			return valorConsultaContaPagar;
		}

		public void setValorConsultaContaPagar(String valorConsultaContaPagar) {
			this.valorConsultaContaPagar = valorConsultaContaPagar;
		}

		public void gerarLancamentoContabilPadrao() {
			try {
				getFacadeFactory().getNegociacaoContaPagarFacade().realizarGeracaoLancamentoContabilJuroMultaDesconto(getNegociacaoContaPagarVO(), true, getUsuarioLogado());
				setMensagemID("msg_dados_adicionados", Uteis.ALERTA);
			}catch (Exception  e) {
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		

		public String getModalAptoParaSerFechado() {
			if (modalAptoParaSerFechado == null) {
				modalAptoParaSerFechado = "";
			}
			return modalAptoParaSerFechado;
		}

		public void setModalAptoParaSerFechado(String modalAptoParaSerFechado) {
			this.modalAptoParaSerFechado = modalAptoParaSerFechado;
		}
		
		public LancamentoContabilVO getLancamentoContabilVO() {
			if (lancamentoContabilVO == null) {
				lancamentoContabilVO = new LancamentoContabilVO();
			}
			return lancamentoContabilVO;
		}

		public void setLancamentoContabilVO(LancamentoContabilVO lancamentoContabilVO) {
			this.lancamentoContabilVO = lancamentoContabilVO;
		}

		public String getValorConsultaPlanoConta() {
			if (valorConsultaPlanoConta == null) {
				valorConsultaPlanoConta = "";
			}
			return valorConsultaPlanoConta;
		}

		public void setValorConsultaPlanoConta(String valorConsultaPlanoConta) {
			this.valorConsultaPlanoConta = valorConsultaPlanoConta;
		}

		public String getCampoConsultaPlanoConta() {
			if (campoConsultaPlanoConta == null) {
				campoConsultaPlanoConta = "descricao";
			}
			return campoConsultaPlanoConta;
		}

		public void setCampoConsultaPlanoConta(String campoConsultaPlanoConta) {
			this.campoConsultaPlanoConta = campoConsultaPlanoConta;
		}

		public List<PlanoContaVO> getListaConsultaPlanoConta() {
			if (listaConsultaPlanoConta == null) {
				listaConsultaPlanoConta = new ArrayList<>();
			}
			return listaConsultaPlanoConta;
		}

		public void setListaConsultaPlanoConta(List<PlanoContaVO> listaConsultaPlanoConta) {
			this.listaConsultaPlanoConta = listaConsultaPlanoConta;
		}
		
		public void selecionarPlanoContaDebito() {
			try {
				PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("planoContaItens");
				getLancamentoContabilVO().setPlanoContaVO(obj);
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}

		public void consultarPlanoContaDebito() {
			try {
				setListaConsultaPlanoConta(getFacadeFactory().getPlanoContaFacade().consultar(getLancamentoContabilVO().getUnidadeEnsinoVO().getCodigo(), getCampoConsultaPlanoConta(), getValorConsultaPlanoConta(), false, getUsuarioLogado()));
				setMensagemID(MSG_TELA.msg_dados_consultados.name());
			} catch (Exception e) {
				setListaConsultaPlanoConta(new ArrayList<>());
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}

		public void selecionarPlanoContaCredito() {
			try {
				PlanoContaVO obj = (PlanoContaVO) context().getExternalContext().getRequestMap().get("planoContaItens");
				getLancamentoContabilVO().setPlanoContaVO(obj);
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}

		public void consultarPlanoContaCredito() {
			try {
				setListaConsultaPlanoConta(getFacadeFactory().getPlanoContaFacade().consultar(getLancamentoContabilVO().getUnidadeEnsinoVO().getCodigo(), getCampoConsultaPlanoConta(), getValorConsultaPlanoConta(), false, getUsuarioLogado()));
				setMensagemID(MSG_TELA.msg_dados_consultados.name());
			} catch (Exception e) {
				setListaConsultaPlanoConta(new ArrayList<>());
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}
		
		public List<SelectItem> getTipoConsultaComboPlanoConta() {
			List<SelectItem> itens = new ArrayList<>(0);
			itens.add(new SelectItem("codigo", "Código"));
			itens.add(new SelectItem("identificadorPlanoConta", "Identificador Plano Conta"));
			itens.add(new SelectItem("descricao", "Descrição"));
			return itens;
		}
		
		public void editarLancamentoContabil() {
			try {				
				LancamentoContabilVO obj = (LancamentoContabilVO) context().getExternalContext().getRequestMap().get("lancamentoContabilItens");
				setLancamentoContabilVO(obj);								
				setMensagemID(MSG_TELA.msg_dados_editar.name());
			} catch (Exception e) {
				setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
			}
		}

		public List<MatriculaVO> getListaConsultaCandidato() {
			return listaConsultaCandidato;
		}

		public void setListaConsultaCandidato(List<MatriculaVO> listaConsultaCandidato) {
			this.listaConsultaCandidato = listaConsultaCandidato;
		}

		public String getValorConsultaCandidato() {
			if (valorConsultaCandidato == null) {
				valorConsultaCandidato = "";
			}
			return valorConsultaCandidato;
		}

		public void setValorConsultaCandidato(String valorConsultaCandidato) {
			this.valorConsultaCandidato = valorConsultaCandidato;
		}

		public String getCampoConsultaCandidato() {
			if (campoConsultaCandidato == null) {
				campoConsultaCandidato = "nomePessoa";
			}
 			return campoConsultaCandidato;
		}

		public void setCampoConsultaCandidato(String campoConsultaCandidato) {
			this.campoConsultaCandidato = campoConsultaCandidato;
		}
		
		public void selecionarCandidato() {
			try {
				MatriculaVO obj = (MatriculaVO) context().getExternalContext().getRequestMap().get("candidatoItens");
				carregarDadosMatricula(obj);
				Uteis.liberarListaMemoria(getListaConsultaCandidato());			
				campoConsultaAluno = null;
				valorConsultaAluno = null;
			} catch (Exception e) {
				getNegociacaoContaPagarVO().setMatriculaAluno(new MatriculaVO());
				getNegociacaoContaPagarVO().getContaPagarNegociadoVOs().clear();
				getNegociacaoContaPagarVO().getContaPagarGeradaVOs().clear();			
				getNegociacaoContaPagarVO().getMatriculaAluno().setMatricula("");
				getNegociacaoContaPagarVO().getMatriculaAluno().getAluno().setNome("");
				getNegociacaoContaPagarVO().setPessoa(null);
				setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
			}
		}
		
	    public void consultarCandidato() {
	        try {

	            List<MatriculaVO> objs = new ArrayList<MatriculaVO>(0);
	            if (getValorConsultaCandidato().equals("")) {
	                setMensagemID("msg_entre_prmconsulta");
	                return;
	            }
	            if (getCampoConsultaCandidato().equals("nomePessoa")) {
	            	DataModelo dataModelo = new DataModelo();
	            	dataModelo.setValorConsulta(getValorConsultaCandidato());
	            	getFacadeFactory().getMatriculaFacade().consultarMatriculas(dataModelo, getUsuarioLogado());
	            	objs = (List<MatriculaVO>) dataModelo.getListaConsulta();
	            }
	            setListaConsultaCandidato(objs);
	            setMensagemID("msg_dados_consultados");

	        } catch (Exception e) {
	        	setListaConsultaCandidato(new ArrayList<MatriculaVO>(0));
	            setMensagemDetalhada("msg_erro", e.getMessage());

	        }
	    }		
		
}
