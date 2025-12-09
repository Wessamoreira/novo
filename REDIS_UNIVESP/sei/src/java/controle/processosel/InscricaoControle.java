package controle.processosel;

/**
 * Classe responsável por implementar a interação entre os componentes JSF das páginas
 * inscricaoForm.jsp inscricaoCons.jsp) com as funcionalidades da classe <code>Inscricao</code>.
 * Implemtação da camada controle (Backing Bean).
 * @see SuperControle
 * @see Inscricao
 * @see InscricaoVO
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.DataScrollEvent;
import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SelectItemOrdemValor;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.crm.CompromissoAgendaPessoaHorarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.processosel.DisciplinasProcSeletivoVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ItemProcSeletivoDataProvaVO;
import negocio.comuns.processosel.ProcSeletivoCursoVO;
import negocio.comuns.processosel.ProcSeletivoUnidadeEnsinoVO;
import negocio.comuns.processosel.ProcSeletivoVO;
import negocio.comuns.processosel.RespostaPerguntaVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import negocio.comuns.processosel.enumeradores.SituacaoInscricaoEnum;
import negocio.comuns.processosel.enumeradores.TipoAvaliacaoProcessoSeletivoEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Dominios;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.processosel.DisciplinasGrupoDisciplinaProcSeletivo;
import negocio.facade.jdbc.processosel.ProcSeletivoUnidadeEnsino;

@Controller("InscricaoControle")
@Scope("viewScope")
@Lazy
public class InscricaoControle extends SuperControle implements Serializable {

    private InscricaoVO inscricaoVO;
    protected List listaSelectItemUnidadeEnsino;
    protected List listaSelectItemSituacaoInscricao;	
    private String candidato_Erro;
    protected List listaSelectItemProcSeletivo;
    protected List listaSelectItemCursoOpcao;
    protected List listaSelectItemOpcaolinguaEstrangeira;
    private String responsavel_Erro;
    private String respLiberacaoPgtoInsc_Erro;
    private String responsavelLiberacaoForaPrazo_Erro;
    private Boolean opcao1;
    private Boolean opcao2;
    private Boolean opcao3;
    private Boolean imprimir;
    protected List listaConsultaAluno;
    protected String campoConsultaAluno;
    protected String valorConsultaAluno;
    protected List listaTipoSelecao;
    private Integer itemProcessoSeletivoMaiorDataProva;
    
    protected String valorConsultaProcSeletivo;
	protected String campoConsultaProcSeletivo;
	protected List listaConsultaProcSeletivo;
    
    private List listaSelectItemDatasProva;
    private Boolean permitirRealizarCancelamentoInscricao;
    private Boolean permitirRealizarCancelamentoDevidoOutraInscricao;
    /**
     * Atributo utilizado pois preciso armazenar o código do processo seletivo selecionado, para utilizá-lo
     * em um segundo momento. Ou seja, quando o usuário informar a unidade de ensino e for necessário montar
     * os cursos a serem listados.
     */
    private Integer tmpCodigoProcessoSeletivo = 0;
    protected Boolean apresentarBotaoEmitirBoleto;
    private List listaSelectItemUnidadeEnsinoCons;
    private Integer codigoUnidadeEnsinoConsulta;
    private String numeroInscricaoConsulta;
    private String nomeCanditadoConsulta;
    private Date dataInicioInscricaoConsulta;
    private Date dataFimInscricaoConsulta;
    private Date dataInicioProvaConsulta;
    private Date dataFimProvaConsulta;
    private Boolean filtroSituacaoInscricaoConsultaAtivo;
    private Boolean filtroSituacaoInscricaoConsultaCancelouOutraInscricao;
    private Boolean filtroSituacaoInscricaoConsultaNaoCompareceu;
    private Boolean filtroSituacaoInscricaoConsultaCancelado;
    private List listaSelectItemProcessoSeletivo;
    private Integer codigoProcessoSeletivoConsulta;
    private DataModelo controleConsultaCandidato;
    private ItemProcSeletivoDataProvaVO ItemProcSeletivoDataProvaVOTemp ; 

    public InscricaoControle() throws Exception {
        //obterUsuarioLogado();
        setOpcao1(Boolean.FALSE);
        setOpcao2(Boolean.FALSE);
        setOpcao3(Boolean.FALSE);
        inicializarListasSelectItemTodosComboBox();
		getControleConsultaOtimizado().getListaConsulta().clear();
		getControleConsultaOtimizado().setPage(0);
		getControleConsultaOtimizado().setPaginaAtual(1);
		getControleConsultaOtimizado().setTotalRegistrosEncontrados(0);
		getControleConsultaOtimizado().setLimitePorPagina(10);
        // inicializarInscricaoControle();
        setControleConsulta(new ControleConsulta());
        setImprimir(false);
        setMensagemID("msg_entre_prmconsulta");
    }
    
    @PostConstruct
    public void realizarCarregamentoInscricaoVindoTelaFichaAluno() {
    	InscricaoVO obj = (InscricaoVO) context().getExternalContext().getSessionMap().get("inscricaoFichaAluno");
    	if (obj != null && !obj.getCodigo().equals(0)) {
    		try {
				obj = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				tmpCodigoProcessoSeletivo = obj.getProcSeletivo().getCodigo();
	            obj.setNovoObj(Boolean.FALSE);
	            setInscricaoVO(obj);
	            consultarProcessoSeletivoPorChavePrimaria();
	            consultarUnidadeEnsinoPorChavePrimaria();
	            inicializarListasSelectItemTodosComboBox();
	            if (getInscricaoVO().getSituacao().equals("CO")) {
	                this.setApresentarBotaoEmitirBoleto(Boolean.FALSE);
	            }
	            if (!getInscricaoVO().getUnidadeEnsino().getCodigo().equals(0)) {
	                if (getInscricaoVO().getProcSeletivo().getNrOpcoesCurso_Apresentar().equals("1")) {
	                    setOpcao1(Boolean.TRUE);
	                    setOpcao2(Boolean.FALSE);
	                    setOpcao3(Boolean.FALSE);
	                } else if (getInscricaoVO().getProcSeletivo().getNrOpcoesCurso_Apresentar().equals("2")) {
	                    setOpcao1(Boolean.TRUE);
	                    setOpcao2(Boolean.TRUE);
	                    setOpcao3(Boolean.FALSE);
	                } else {
	                    setOpcao1(Boolean.TRUE);
	                    setOpcao2(Boolean.TRUE);
	                    setOpcao3(Boolean.TRUE);
	                }
	                montarListaSelectItemCursoOpcao();
	            }
	            montarListaSelectItemDataProvaTotal();
	            setMensagemID("msg_dados_editar");
			} catch (Exception e) {
				 setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("inscricaoFichaAluno");
			}
    	}
    }
    
    @PostConstruct
    public void realizarCarregamentoInscricaoVindoTelaAgenda() {
    	CompromissoAgendaPessoaHorarioVO obj = (CompromissoAgendaPessoaHorarioVO) context().getExternalContext().getSessionMap().get("compromissoAgendaPessoaHorarioCandidatoVO");
    	if (obj != null && !obj.getCodigo().equals(0)) {
    		try {
    			getInscricaoVO().getCandidato().setCPF(obj.getProspect().getCpf());
    			consultarCandidatoPorCPF();
	            setMensagemID("msg_dados_editar");
			} catch (Exception e) {
				 setMensagemDetalhada("msg_erro", e.getMessage());
			} finally {
				context().getExternalContext().getSessionMap().remove("compromissoAgendaPessoaHorarioCandidatoVO");
			}
    	}
    }

//    public void inicializarInscricaoControle() {
//        try {
//            setInscricaoVO(new InscricaoVO());
//            setProcessoSeletivoVO(new ProcSeletivoVO());
//            tmpCodigoProcessoSeletivo = 0;
//            setOpcao1(Boolean.FALSE);
//            setOpcao2(Boolean.FALSE);
//            setOpcao3(Boolean.FALSE);
//            setApresentarBotaoEmitirBoleto(Boolean.TRUE);
//            setMensagemID("msg_entre_dados");
//
//            CandidatoControle candidatoControle = (CandidatoControle) context().getExternalContext().getSessionMap().get("CandidatoControle");
//            if (candidatoControle != null) {
//                this.inscricaoVO.setCandidato(candidatoControle.getPessoaVO());
//                this.inscricaoVO.setData(new Date());
//            }
//            VisaoCandidatoControle visaoCandidatoControle = (VisaoCandidatoControle) context().getExternalContext().getSessionMap().get("VisaoCandidatoControle");
//            if (visaoCandidatoControle != null) {
//                this.inscricaoVO.setProcSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(visaoCandidatoControle.getProcessoSeletivoVO().getCodigo().intValue(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));
//                tmpCodigoProcessoSeletivo = this.inscricaoVO.getProcSeletivo().getCodigo();
//                if (this.inscricaoVO.getProcSeletivo().getNrOpcoesCurso().equals("1")) {
//                    setOpcao1(true);
//                    setOpcao2(false);
//                    setOpcao3(false);
//                }
//                if (this.inscricaoVO.getProcSeletivo().getNrOpcoesCurso().equals("2")) {
//                    setOpcao1(true);
//                    setOpcao2(true);
//                    setOpcao3(false);
//                }
//                if (this.inscricaoVO.getProcSeletivo().getNrOpcoesCurso().equals("3")) {
//                    setOpcao1(true);
//                    setOpcao2(true);
//                    setOpcao3(true);
//                }
//                this.inscricaoVO.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(visaoCandidatoControle.getUnidadeEnsinoVO().getCodigo().intValue(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));
//                this.inscricaoVO.setCursoOpcao1(getFacadeFactory().getUnidadeEnsinoCursoFacade().consultarPorChavePrimaria(visaoCandidatoControle.getUnidadeEnsinoCursoVO().getCodigo().intValue(), Uteis.NIVELMONTARDADOS_DADOSBASICOS));
//                montarListaSelectItemCursoOpcao();
//                montarListaSelectItemOpcaoLinguaEstrangeira();
//            }
//        } catch (Exception e) {
//
//        }
//    }
    /**
     * Rotina responsável por disponibilizar um novo objeto da classe <code>Inscricao</code>
     * para edição pelo usuário da aplicação.
     */
    public String novo() {         //removerObjetoMemoria(this);
        setInscricaoVO(new InscricaoVO());
        tmpCodigoProcessoSeletivo = 0;
        inicializarResponsavelInscricaoUsuarioLogado();
        inicializarListasSelectItemTodosComboBox();        
        setOpcao1(Boolean.FALSE);
        setOpcao2(Boolean.FALSE);
        setOpcao3(Boolean.FALSE);
        setApresentarBotaoEmitirBoleto(Boolean.TRUE);
        setListaSelectItemDatasProva(new ArrayList(0));
        setMensagemID("msg_entre_dados");
        return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoForm.xhtml");
    }

    public void inicializarResponsavelInscricaoUsuarioLogado() {
        try {
        	  inscricaoVO.getResponsavel().setCodigo(getUsuarioLogado().getCodigo());
              inscricaoVO.getResponsavel().setNome(getUsuarioLogado().getNome());
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

//    public void inicializarApresentaInscricao() {
//        try {
//            Date dataProva = Uteis.getDate(getDataProvaSelecionada().substring(0, getDataProvaSelecionada().lastIndexOf(" -")));
//            String horaProva = getDataProvaSelecionada().substring(getDataProvaSelecionada().lastIndexOf("- ") + 1);
//            getInscricaoVO().setDataProva(dataProva);
//            getInscricaoVO().setHora(horaProva);
//        } catch (Exception e) {
//        }
//    }

    /**
     * Rotina responsável por disponibilizar os dados de um objeto da classe <code>Inscricao</code> para alteração.
     * O objeto desta classe é disponibilizado na session da página (request) para que o JSP correspondente possa disponibilizá-lo para edição.
     */
    public String editar()  {
        InscricaoVO obj = (InscricaoVO) context().getExternalContext().getRequestMap().get("inscricaoItens");
        try {
			obj = getFacadeFactory().getInscricaoFacade().consultarPorChavePrimaria(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
		
        tmpCodigoProcessoSeletivo = obj.getProcSeletivo().getCodigo();
        obj.setNovoObj(Boolean.FALSE);
        setInscricaoVO(obj);
        consultarProcessoSeletivoPorChavePrimaria();
        consultarUnidadeEnsinoPorChavePrimaria();
        inicializarListasSelectItemTodosComboBox();
        if (getInscricaoVO().getSituacao().equals("CO")) {
            this.setApresentarBotaoEmitirBoleto(Boolean.FALSE);
        }
        if (!getInscricaoVO().getUnidadeEnsino().getCodigo().equals(0)) {
            if (getInscricaoVO().getProcSeletivo().getNrOpcoesCurso_Apresentar().equals("1")) {
                setOpcao1(Boolean.TRUE);
                setOpcao2(Boolean.FALSE);
                setOpcao3(Boolean.FALSE);
            } else if (getInscricaoVO().getProcSeletivo().getNrOpcoesCurso_Apresentar().equals("2")) {
                setOpcao1(Boolean.TRUE);
                setOpcao2(Boolean.TRUE);
                setOpcao3(Boolean.FALSE);
            } else {
                setOpcao1(Boolean.TRUE);
                setOpcao2(Boolean.TRUE);
                setOpcao3(Boolean.TRUE);
            }
            montarListaSelectItemCursoOpcao();
        }
        montarListaSelectItemDataProvaTotal();
        setItemProcSeletivoDataProvaVOTemp(( ItemProcSeletivoDataProvaVO) Uteis.clonar(getInscricaoVO().getItemProcessoSeletivoDataProva()));
        setMensagemID("msg_dados_editar");       
        } catch (Exception e) {
         setMensagemDetalhada("msg_erro", e.getMessage()); 
		}
        return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoForm.xhtml");
    }

    /**
     * Rotina responsável por gravar no BD os dados editados de um novo objeto da classe <code>Inscricao</code>.
     * Caso o objeto seja novo (ainda não gravado no BD) é acionado a operação <code>incluir()</code>. Caso contrário é acionado o <code>alterar()</code>.
     * Se houver alguma inconsistência o objeto não é gravado, sendo re-apresentado para o usuário juntamente com uma mensagem de erro.
     */
    public String gravar() {
        try {        	
        	this.validarPermissaoUsuarioAlterarSituacaoCancelado();
        	this.validarAlterarDataProva();
        	
        	/*if (getListaSelectItemOpcaolinguaEstrangeira().size() > 1) {
				if ((inscricaoVO.getOpcaoLinguaEstrangeira() == null) || (inscricaoVO.getOpcaoLinguaEstrangeira().getCodigo().intValue() == 0) ) {
					if (getInscricaoVO().getFormaIngresso().equals("PS")) {
						throw new ConsistirException("O campo OPÇÃO LINGUA ESTRANGEIRA (Inscrição) deve ser informado.");
					}
					
				}
        	}*/        	
        	ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(inscricaoVO.getUnidadeEnsino().getCodigo());        	
            if (inscricaoVO.isNovoObj().booleanValue()) {
                getInscricaoVO().setInscricaoPresencial(Boolean.TRUE);                                    
                if (getInscricaoVO().getFormaIngresso().equals("PD") || getInscricaoVO().getFormaIngresso().equals("EN") || getInscricaoVO().getFormaIngresso().equals("TR")) {
            			getInscricaoVO().getItemProcessoSeletivoDataProva().setCodigo(getItemProcessoSeletivoMaiorDataProva());
            	}
               getFacadeFactory().getInscricaoFacade().incluir(inscricaoVO, getInscricaoVO().getArquivoVO(), configuracaoFinanceiroVO, getUsuarioLogado());                                                                                     
                setMensagemID("msg_dados_gravados");
            } else {
                inicializarResponsavelInscricaoUsuarioLogado();
                getFacadeFactory().getInscricaoFacade().alterar(inscricaoVO, getInscricaoVO().getArquivoVO(), getUsuarioLogado(), configuracaoFinanceiroVO);                
                setMensagemID("msg_dados_gravados");
            }
            return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoForm.xhtml");
        }
    }

    public String gravarDuranteEmissaoBoleto() {
        try {
            if (inscricaoVO.getSituacao().equals("PF")) {
                if (inscricaoVO.isNovoObj().booleanValue()) {
                    gravar();
                } else {
                    inicializarResponsavelInscricaoUsuarioLogado();
                    getFacadeFactory().getInscricaoFacade().alterar(inscricaoVO, getInscricaoVO().getArquivoVO(), getUsuarioLogado(), getConfiguracaoFinanceiroPadraoSistema());
                }
                return "ok";
            } else {
                return "Inscrição já quitada financeiramente";
            }
        } catch (Exception e) {
            return e.getMessage();
        }
    }

	  public String consultar() {
		    try {
		    	getControleConsultaOtimizado().setPage(0);
		        getControleConsultaOtimizado().setPaginaAtual(1);
		    	return consultarInscricao();
		    } catch (Exception e) {
		        setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		    }
		    return "";
	    }
    
    /**
     * Rotina responsavel por executar as consultas disponiveis no JSP InscricaoCons.jsp.
     * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
     * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
     */

    public String consultarInscricao() {
    	
		try {
			registrarAtividadeUsuario(getUsuarioLogado(), "InscricaoControle", "Inicializando Consultar Inscricao", "Consultando");
			super.consultar();
			getControleConsultaOtimizado().getListaConsulta().clear();
			getControleConsultaOtimizado().setLimitePorPagina(10);
            getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getInscricaoFacade().consultarInscricao(getCodigoProcessoSeletivoConsulta(), getCodigoUnidadeEnsinoConsulta(), getNumeroInscricaoConsulta(), getNomeCanditadoConsulta(), getDataInicioInscricaoConsulta(), getDataFimInscricaoConsulta(), getDataInicioProvaConsulta(), getDataFimProvaConsulta(), getFiltroSituacaoInscricaoConsultaAtivo(), getFiltroSituacaoInscricaoConsultaCancelouOutraInscricao(), getFiltroSituacaoInscricaoConsultaNaoCompareceu(), getFiltroSituacaoInscricaoConsultaCancelado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getInscricaoFacade().totalConsultaInscricao(getCodigoProcessoSeletivoConsulta(), getCodigoUnidadeEnsinoConsulta(), getNumeroInscricaoConsulta(), getNomeCanditadoConsulta(), getDataInicioInscricaoConsulta(), getDataFimInscricaoConsulta(), getDataInicioProvaConsulta(), getDataFimProvaConsulta(), getFiltroSituacaoInscricaoConsultaAtivo(), getFiltroSituacaoInscricaoConsultaCancelouOutraInscricao(), getFiltroSituacaoInscricaoConsultaNaoCompareceu(), getFiltroSituacaoInscricaoConsultaCancelado(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, getUsuarioLogado()));
			if (getControleConsultaOtimizado().getApresentarListaConsulta()) {
				setMensagemID("msg_dados_consultados");
			}			
			registrarAtividadeUsuario(getUsuarioLogado(), "InscricaoControle", "Finalizando Consultar Inscricao", "Consultando");
			setMensagemID("msg_dados_consultados");
			return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoCons.xhtml");
		} catch (Exception e) {
			setListaConsulta(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
			return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoCons.xhtml");
		}
       /* try {
        	
        	
        	
        	
        	
        	
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    throw new ConsistirException("Por favor informe o CÓDIGO desejado.");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
//                objs = getFacadeFactory().getInscricaoFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
                objs = getFacadeFactory().getInscricaoFacade().consultarPorCodigoUnidadeEnsino(new Integer(valorInt), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeUnidadeEnsino")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    throw new ConsistirException("Por favor informe o NOME da UNIDADE ENSINO desejado.");
                }
                objs = getFacadeFactory().getInscricaoFacade().consultarPorNomeUnidadeEnsino(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomePessoa")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    throw new ConsistirException("Por favor informe o NOME do CANDIDATO desejado.");
                }
//                objs = getFacadeFactory().getInscricaoFacade().consultarPorNomePessoa(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
                objs = getFacadeFactory().getInscricaoFacade().consultarPorNomePessoaUnidadeEnsino(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("descricaoProcSeletivo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    throw new ConsistirException("Por favor informe o NOME do PROCESSO SELETIVO desejado.");
                }
//                objs = getFacadeFactory().getInscricaoFacade().consultarPorDescricaoProcSeletivo(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
                objs = getFacadeFactory().getInscricaoFacade().consultarPorDescricaoProcSeletivoUnidadeEnsino(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeCurso")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    throw new ConsistirException("Por favor informe o NOME  do CURSO desejado.");
                }
//                objs = getFacadeFactory().getInscricaoFacade().consultarPorNomeCurso(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
                objs = getFacadeFactory().getInscricaoFacade().consultarPorNomeCursoUnidadeEnsino(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("data")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    throw new ConsistirException("Por favor informe o DATA desejado.");
                }
                Date valorData = Uteis.getDate(getControleConsulta().getValorConsulta());
//                objs = getFacadeFactory().getInscricaoFacade().consultarPorData(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
                objs = getFacadeFactory().getInscricaoFacade().consultarPorDataUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("situacao")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    throw new ConsistirException("Por favor informe o SITUAÇÃO desejado.");
                }
//                objs = getFacadeFactory().getInscricaoFacade().consultarPorSituacao(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
                objs = getFacadeFactory().getInscricaoFacade().consultarPorSituacaoUnidadeEnsino(getControleConsulta().getValorConsulta(), getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
            }
//            objs = ControleConsulta.obterSubListPaginaApresentar(objs, controleConsulta);
//            definirVisibilidadeLinksNavegacao(controleConsulta.getPaginaAtual(), controleConsulta.getNrTotalPaginas());
             objs = getFacadeFactory().getInscricaoFacade().consultaInscricao(getDescricaoProcessoSeletivoConsulta(), getCodigoUnidadeEnsinoConsulta(), getNumeroInscricaoConsulta(), getNomeCanditadoConsulta(), getDataInicioInscricaoConsulta(), getDataFimInscricaoConsulta(), getDataInicioProvaConsulta(), getDataFimProvaConsulta(), getSituacaoInscricaoConsulta(), getControleConsultaOtimizado().getLimitePorPagina(), getControleConsultaOtimizado().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoCons.xhtml");
        }*/
    }

    /**
     * Operação responsável por processar a exclusão um objeto da classe <code>InscricaoVO</code>
     * Após a exclusão ela automaticamente aciona a rotina para uma nova inclusão.
     */
    public String excluir() {
        try {
//            if (!inscricaoVO.getSituacao().equals("CO") ) {
                getFacadeFactory().getInscricaoFacade().excluir(inscricaoVO, getUsuarioLogado());
                novo();
                //setInscricaoVO( new InscricaoVO());
                setMensagemID("msg_dados_excluidos");
//            } else {
                //setMensagemDetalhada("Não é possivel Excluir uma Inscrição já Quitada");
//            }
                return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoForm.xhtml");
        }
    }

    public void irPaginaInicial() throws Exception {
        controleConsulta.setPaginaAtual(1);
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

    /* Método responsável por inicializar List<SelectItem> de valores do
     * ComboBox correspondente ao atributo <code>situacao</code>
     */
    public List getListaSelectItemSituacaoInscricao() throws Exception {
        List objs = new ArrayList(0);
        Hashtable situacaoProcessoSeletivos = (Hashtable) Dominios.getSituacaoProcessoSeletivo();
        Enumeration keys = situacaoProcessoSeletivos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) situacaoProcessoSeletivos.get(value);
            objs.add(new SelectItem(value, label));
        }
        return objs;
    }

    private List<SelectItem> listaSelectItemSituacaoInscricaoEnum;
    public List<SelectItem> getListaSelectItemSituacaoInscricaoEnum() throws Exception {
        if(listaSelectItemSituacaoInscricaoEnum == null){
            listaSelectItemSituacaoInscricaoEnum = new ArrayList<SelectItem>(0);
            for(SituacaoInscricaoEnum situacaoInscricaoEnum:SituacaoInscricaoEnum.values()){
            	listaSelectItemSituacaoInscricaoEnum.add(new SelectItem(situacaoInscricaoEnum.name(), situacaoInscricaoEnum.getValorApresentar()));
            }
        }
        return listaSelectItemSituacaoInscricaoEnum;
    }
    
    public List getTipoConsultaComboProcSeletivo() {
		List itens = new ArrayList(0);
		itens.add(new SelectItem("descricao", "Descrição"));
		itens.add(new SelectItem("dataInicio", "Data Início"));
		itens.add(new SelectItem("dataFim", "Data Fim"));
		itens.add(new SelectItem("dataProva", "Data Prova"));
		return itens;
	}
    
    public void montarListaUltimosProcSeletivos() {
		try {
			setListaConsultaProcSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarUltimosProcessosSeletivos(5, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
		} catch (Exception e) {
			setListaConsultaProcSeletivo(new ArrayList<ProcSeletivoVO>(0));
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
    
    public String getMascaraConsultaProcSeletivo() {
		if (getCampoConsultaProcSeletivo().equals("dataInicio") || getCampoConsultaProcSeletivo().equals("dataFim") || getCampoConsultaProcSeletivo().equals("dataProva")) {
			return "return mascara(this.form,'this.id','99/99/9999',event);";
		}
		return "";
	}
    
    public void selecionarProcSeletivo() {
		ProcSeletivoVO obj = (ProcSeletivoVO) context().getExternalContext().getRequestMap().get("procSeletivoItens");
		getInscricaoVO().setProcSeletivo(obj);
		consultarProcessoSeletivoPorChavePrimaria();
		//getInscricaoVO().setUnidadeEnsino(new UnidadeEnsinoVO());
		 if (!getInscricaoVO().getUnidadeEnsino().getCodigo().equals(0)) {
	            if (getInscricaoVO().getProcSeletivo().getNrOpcoesCurso_Apresentar().equals("1")) {
	                setOpcao1(Boolean.TRUE);
	                setOpcao2(Boolean.FALSE);
	                setOpcao3(Boolean.FALSE);
	            } else if (getInscricaoVO().getProcSeletivo().getNrOpcoesCurso_Apresentar().equals("2")) {
	                setOpcao1(Boolean.TRUE);
	                setOpcao2(Boolean.TRUE);
	                setOpcao3(Boolean.FALSE);
	            } else {
	                setOpcao1(Boolean.TRUE);
	                setOpcao2(Boolean.TRUE);
	                setOpcao3(Boolean.TRUE);
	            }
	            montarListaSelectItemCursoOpcao();
	        }
	}
    
    public void limparDados() {
    	getInscricaoVO().setProcSeletivo(new ProcSeletivoVO());
    	 setOpcao1(Boolean.FALSE);
         setOpcao2(Boolean.FALSE);
         setOpcao3(Boolean.FALSE);
	}
    
    public void consultarProcSeletivo() {
		try {
			List objs = new ArrayList(0);
			if (getCampoConsultaProcSeletivo().equals("descricao")) {
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(getValorConsultaProcSeletivo(), getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataInicio")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataInicioUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataFim")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataFimUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			if (getCampoConsultaProcSeletivo().equals("dataProva")) {
				Date valorData = Uteis.getDate(getValorConsultaProcSeletivo());
				objs = getFacadeFactory().getProcSeletivoFacade().consultarPorDataProvaUnidadeEnsino(Uteis.getDateTime(valorData, 0, 0, 0), Uteis.getDateTime(valorData, 23, 59, 59), getUnidadeEnsinoLogado().getCodigo(),false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
			}
			setListaConsultaProcSeletivo(objs);
			setMensagemID("msg_dados_consultados");
		} catch (Exception e) {
			setListaConsultaProcSeletivo(new ArrayList(0));
			setMensagemDetalhada("msg_erro", e.getMessage());

		}
	}

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>CursoOpcao1</code>.
     */
    public void montarListaSelectItemOpcaoLinguaEstrangeira(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            if (getInscricaoVO().getProcSeletivo().getCodigo().intValue() == 0) {
                setListaSelectItemOpcaolinguaEstrangeira(new ArrayList(0));
                getListaSelectItemOpcaolinguaEstrangeira().add(new SelectItem(0, ""));
                return;
            }
            resultadoConsulta = consultarPorOpcaoLinguaEstrangeira();
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            if(!i.hasNext()) {
              if(Uteis.isAtributoPreenchido(getInscricaoVO().getOpcaoLinguaEstrangeira())) {
            	  objs.add(new SelectItem(getInscricaoVO().getOpcaoLinguaEstrangeira().getCodigo(), getInscricaoVO().getOpcaoLinguaEstrangeira().getNome()));
              }	
            }
            while (i.hasNext()) {
                DisciplinasProcSeletivoVO dis = (DisciplinasProcSeletivoVO) i.next();
                objs.add(new SelectItem(dis.getCodigo(), dis.getNome()));
            }
            setListaSelectItemOpcaolinguaEstrangeira(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemCursoOpcao(String prm) throws Exception {
    	List<ProcSeletivoCursoVO> resultadoConsulta = null;
        boolean isExisteUnidadeEnsinoCursoCandidatoInscrito = false;
        Iterator i = null;
        try {
            if ((inscricaoVO.getProcSeletivo() == null) || (tmpCodigoProcessoSeletivo.intValue() == 0)) {
                List<SelectItem> objs = new ArrayList<SelectItem>(0);
                setListaSelectItemCursoOpcao(objs);
                return;
            }
            if ((inscricaoVO.getUnidadeEnsino() == null) || (inscricaoVO.getUnidadeEnsino().getCodigo().intValue() == 0)) {
                List<SelectItem> objs = new ArrayList<SelectItem>(0);
                setListaSelectItemCursoOpcao(objs);
                return;
            }
            resultadoConsulta = this.consultarPorProcessoSeletivoCurso();
            i = resultadoConsulta.iterator();
            List<SelectItem> objs = new ArrayList<SelectItem>(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ProcSeletivoCursoVO proc = (ProcSeletivoCursoVO) i.next();
                if (!proc.getUnidadeEnsinoCurso().getCurso().getNivelEducacionalPosGraduacao()) {
					if (getInscricaoVO().getCursoOpcao1().getCodigo().equals(proc.getUnidadeEnsinoCurso().getCodigo())) {
						isExisteUnidadeEnsinoCursoCandidatoInscrito = true;
					} else if (getInscricaoVO().getCursoOpcao1().getCurso().getCodigo().equals(proc.getUnidadeEnsinoCurso().getCurso().getCodigo())
							&& getInscricaoVO().getCursoOpcao1().getTurno().getCodigo().equals(proc.getUnidadeEnsinoCurso().getTurno().getCodigo())) {
						getInscricaoVO().setCursoOpcao1(proc.getUnidadeEnsinoCurso());
						isExisteUnidadeEnsinoCursoCandidatoInscrito = true;
					}
                    objs.add(new SelectItem(proc.getUnidadeEnsinoCurso().getCodigo(), proc.getUnidadeEnsinoCurso().getCurso().getNome() + " - " + proc.getUnidadeEnsinoCurso().getTurno().getNome()));
                }
            }
            if(!getInscricaoVO().getNovoObj() && !isExisteUnidadeEnsinoCursoCandidatoInscrito && getInscricaoVO().getUnidadeEnsino().getCodigo().equals(getInscricaoVO().getCursoOpcao1().getUnidadeEnsino())){
              //  objs.add(new SelectItem(getInscricaoVO().getCursoOpcao1().getCodigo(), getInscricaoVO().getCursoOpcao1().getCurso().getNome() + " - " + getInscricaoVO().getCursoOpcao1().getTurno().getNome()));
            }
            setListaSelectItemCursoOpcao(objs);
            montarListaSelectTipoSelecao();
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    public void montarListaSelectItemDataProva() {
        try {
            if (getInscricaoVO().getProcSeletivo().getCodigo().intValue() != 0) {
            	List<ItemProcSeletivoDataProvaVO> resultadoConsulta = getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorProcSelectivo(getInscricaoVO().getProcSeletivo().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            	setItemProcessoSeletivoMaiorDataProva(getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarMaiorDatas(getInscricaoVO().getProcSeletivo().getCodigo()).getCodigo());
            	List<SelectItem> objs = new ArrayList<SelectItem>(0);
				objs.add(new SelectItem("", ""));
				for (ItemProcSeletivoDataProvaVO proc : resultadoConsulta) {
					objs.add(new SelectItem(proc.getCodigo(), proc.getDataProva_Apresentar()));
				}
				SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
				Collections.sort((List<SelectItem>) objs, ordenador);
				if (!getApresentarCampoDataProva() && Uteis.isAtributoPreenchido(resultadoConsulta)) {
					getInscricaoVO().setItemProcessoSeletivoDataProva(resultadoConsulta.get(0));
				}
				setListaSelectItemDatasProva(objs);
            } else {
                setListaSelectItemDatasProva(new ArrayList(0));
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void montarListaSelectItemDataProvaTotal() {
        try {
            if (getInscricaoVO().getProcSeletivo().getCodigo().intValue() != 0) {
                
                List<ItemProcSeletivoDataProvaVO> resultadoConsulta = getFacadeFactory().getItemProcSeletivoDataProvaFacade().consultarPorCodigoProcessoSeletivo(getInscricaoVO().getProcSeletivo().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
                Iterator<ItemProcSeletivoDataProvaVO> i = resultadoConsulta.iterator();
                List<SelectItem> objs = new ArrayList<SelectItem>(0);
                objs.add(new SelectItem("", ""));
                while (i.hasNext()) {
                    ItemProcSeletivoDataProvaVO proc = (ItemProcSeletivoDataProvaVO) i.next();
//                    if (new Date().before(proc.getDataProva())) {
                    objs.add(new SelectItem(proc.getCodigo(), proc.getDataProva_Apresentar()));
//                    }
                }
                if (!getApresentarCampoDataProva() && Uteis.isAtributoPreenchido(resultadoConsulta)) {
					getInscricaoVO().setItemProcessoSeletivoDataProva(resultadoConsulta.get(0));
				}
                setListaSelectItemDatasProva(objs);
            } else {
                setListaSelectItemDatasProva(new ArrayList<SelectItem>(0));
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>CursoOpcao1</code>.
     * Buscando todos os objetos correspondentes a entidade <code>Curso</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemCursoOpcao() {
        try {
            montarListaSelectItemCursoOpcao("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    public void montarListaSelectItemOpcaoLinguaEstrangeira() {
        try {
            montarListaSelectItemOpcaoLinguaEstrangeira("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarPorProcessoSeletivoCurso() throws Exception {
        return getFacadeFactory().getProcSeletivoCursoFacade().consultarPorCodigoProcSeletivoUnidadeEnsinoOpcaoInscicao(getInscricaoVO().getProcSeletivo().getCodigo(), this.getInscricaoVO().getUnidadeEnsino().getCodigo(), getInscricaoVO().getData() , Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS , getUsuarioLogado());
    }

    public List consultarPorOpcaoLinguaEstrangeira() throws Exception {
        //return ProcSeletivoDisciplinasProcSeletivo.consultarProcSeletivoDisciplinasProcSeletivosLinguaExtrangeira(getInscricaoVO().getProcSeletivo().getCodigo(), getUsuarioLogado());
        return DisciplinasGrupoDisciplinaProcSeletivo.consultarDisciplinasGrupoDisciplinaProcSeletivosLinguaExtrangeira(getInscricaoVO().getProcSeletivo().getCodigo(), getUsuarioLogado());
//        List lista = new ArrayList(0);
//        Iterator i = this.getProcessoSeletivoVO().getProcSeletivoDisciplinasProcSeletivoVOs().iterator();
//        while (i.hasNext()) {
//            ProcSeletivoDisciplinasProcSeletivoVO p = (ProcSeletivoDisciplinasProcSeletivoVO) i.next();
//            if (p.getDisciplinasProcSeletivo().getDisciplinaIdioma().booleanValue()) {
//                lista.add(p.getDisciplinasProcSeletivo());
//            }
//        }
//        return lista;
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>ProcSeletivo</code>.
     */
    public void montarListaSelectItemProcSeletivo(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarProcSeletivoPorDescricao(prm);
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                ProcSeletivoVO obj = (ProcSeletivoVO) i.next();
                if ((obj.getDataInicio().before(new Date()) && obj.getDataFim().after(new Date()) && obj.getDataFim().compareTo(new Date()) > 0)
                        || (obj.getDataInicioInternet().before(new Date()) && obj.getDataFimInternet().after(new Date()) && obj.getDataFimInternet().compareTo(new Date()) > 0)
                        || (obj.getCodigo().equals(inscricaoVO.getProcSeletivo().getCodigo()))) {
                    objs.add(new SelectItem(obj.getCodigo(), obj.getDescricao()));
                }
            }
            setListaSelectItemProcSeletivo(objs);
        } catch (Exception e) {
            throw e;
        } finally {
            Uteis.liberarListaMemoria(resultadoConsulta);
            i = null;
        }
    }

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>ProcSeletivo</code>.
     * Buscando todos os objetos correspondentes a entidade <code>ProcSeletivo</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemProcSeletivo() {
        try {
            montarListaSelectItemProcSeletivo("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>descricao</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarProcSeletivoPorDescricao(String descricaoPrm) throws Exception {
        List lista = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino(descricaoPrm,  getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
        return lista;
    }

    public void consultarUnidadeEnsinoPorChavePrimaria() {
        try {
            Integer campoConsulta = getInscricaoVO().getUnidadeEnsino().getCodigo();
            UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(campoConsulta, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            getInscricaoVO().setUnidadeEnsino(unidadeEnsinoVO);
        } catch (Exception e) {
            getInscricaoVO().setUnidadeEnsino(new UnidadeEnsinoVO());
        }
        montarListaSelectItemCursoOpcao();
        if (getInscricaoVO().getProcSeletivo().getNrOpcoesCurso().equals("1")) {
            setOpcao1(true);
            setOpcao2(false);
            setOpcao3(false);
        }
        if (getInscricaoVO().getProcSeletivo().getNrOpcoesCurso().equals("2")) {
            setOpcao1(true);
            setOpcao2(true);
            setOpcao3(false);
        }
        if (getInscricaoVO().getProcSeletivo().getNrOpcoesCurso().equals("3")) {
            setOpcao1(true);
            setOpcao2(true);
            setOpcao3(true);
        }
    }

    public void consultarProcessoSeletivoPorChavePrimaria() {
        try {
            Integer campoConsulta = inscricaoVO.getProcSeletivo().getCodigo();
            inscricaoVO.setProcSeletivo(getFacadeFactory().getProcSeletivoFacade().consultarPorChavePrimaria(campoConsulta, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            inscricaoVO.getQuestionarioVO().setCodigo(getInscricaoVO().getProcSeletivo().getQuestionario().getCodigo());           
            tmpCodigoProcessoSeletivo = campoConsulta;
            montarListaSelectItemOpcaoLinguaEstrangeira();
            realizarResponderQuestionario();
            montarListaSelectItemDataProva();
        } catch (Exception e) {
            inscricaoVO.setProcSeletivo(new ProcSeletivoVO());
            inscricaoVO.setUnidadeEnsino(new UnidadeEnsinoVO());
            tmpCodigoProcessoSeletivo = 0;
        }
        setOpcao1(false);
        setOpcao2(false);
        setOpcao3(false);
        montarListaSelectItemUnidadeEnsino();
    }

    public void realizarResponderQuestionario() {
        try {
            if (getInscricaoVO().getQuestionarioVO().getCodigo() != 0) {
                getInscricaoVO().setQuestionarioVO(getFacadeFactory().getQuestionarioFacade().consultarPorChavePrimaria(getInscricaoVO().getQuestionarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
                if (getInscricaoVO().getCodigo().intValue() > 0) {
                    getFacadeFactory().getQuestionarioFacade().executarRestauracaoRespostaQuestionarioPorInscricao(getInscricaoVO().getCodigo().intValue(), getInscricaoVO().getQuestionarioVO());
                }
            }
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void validarDadosListaResposta() throws Exception {
        RespostaPerguntaVO obj = (RespostaPerguntaVO) context().getExternalContext().getRequestMap().get("respostaItens");
        if(obj.getSelecionado()) {
        	obj.setSelecionado(false);
        } else {
        	obj.setSelecionado(true);
        	getInscricaoVO().getQuestionarioVO().varrerListaQuestionarioRetornarPerguntaRespondida(obj);
        }
    }

    /**
     * Método responsável por processar a consulta na entidade <code>Pessoa</code> por meio de sua respectiva chave primária.
     * Esta rotina é utilizada fundamentalmente por requisições Ajax, que realizam busca pela chave primária da entidade
     * montando automaticamente o resultado da consulta para apresentação.
     */
    public void consultarCandidatoPorCPF() {
        try {
            String campoConsulta = getInscricaoVO().getCandidato().getCPF();
            if (campoConsulta.equals("")) {
                setMensagemID("msg_erro_dadosnaoencontrados");
                getInscricaoVO().setCandidato(null);
                this.setCandidato_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
                return;
            }
            PessoaVO pessoa = getFacadeFactory().getPessoaFacade().consultarPorCPFUnico(campoConsulta, 0, TipoPessoa.CANDIDATO.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            if (pessoa.getCodigo() != 0) {
                getInscricaoVO().setCandidato(pessoa);
                this.setCandidato_Erro("");
                setMensagemID("msg_dados_consultados");
            } else {
                setMensagemID("msg_erro_dadosnaoencontrados");
                getInscricaoVO().setCandidato(null);
                this.setCandidato_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
            }
        } catch (Exception e) {
            setMensagemID("msg_erro_dadosnaoencontrados");
            getInscricaoVO().setCandidato(null);
            this.setCandidato_Erro(getMensagemInternalizacao("msg_erro_dadosnaoencontrados"));
        }
    }

    /**
     * Método responsável por gerar uma lista de objetos do tipo <code>SelectItem</code> para preencher
     * o comboBox relativo ao atributo <code>UnidadeEnsino</code>.
     */
    public void montarListaSelectItemUnidadeEnsino(String prm) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;
        try {
            resultadoConsulta = consultarUnidadeEnsinoPorNome(prm);
            getInscricaoVO().getProcSeletivo().setProcSeletivoUnidadeEnsinoVOs(ProcSeletivoUnidadeEnsino.consultarProcSeletivoUnidadeEnsinos(getInscricaoVO().getProcSeletivo().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            i = resultadoConsulta.iterator();
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            while (i.hasNext()) {
                UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
                if(!Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado()) || (Uteis.isAtributoPreenchido(getUnidadeEnsinoLogado()) && getUnidadeEnsinoLogado().getCodigo().equals(obj.getCodigo()))) {
                List unidadesValidas = getInscricaoVO().getProcSeletivo().getProcSeletivoUnidadeEnsinoVOs();
                Iterator j = unidadesValidas.iterator();
                while (j.hasNext()) {
                    ProcSeletivoUnidadeEnsinoVO unidadeProcessoSeletivo = (ProcSeletivoUnidadeEnsinoVO) j.next();
                    if (unidadeProcessoSeletivo.getUnidadeEnsino().getCodigo().equals(obj.getCodigo())) {
                        objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
                        break;
                    }
                }
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

    /**
     * Método responsável por atualizar o ComboBox relativo ao atributo <code>UnidadeEnsino</code>.
     * Buscando todos os objetos correspondentes a entidade <code>UnidadeEnsino</code>. Esta rotina não recebe parâmetros para filtragem de dados, isto é
     * importante para a inicialização dos dados da tela para o acionamento por meio requisições Ajax.
     */
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            montarListaSelectItemUnidadeEnsino("");
        } catch (Exception e) {
            //System.out.println("MENSAGEM => " + e.getMessage());;
        }
    }

    /**
     * Método responsável por consultar dados da entidade <code><code> e montar o atributo <code>nome</code>
     * Este atributo é uma lista (<code>List</code>) utilizada para definir os valores a serem apresentados no ComboBox correspondente
     */
    public List consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        return lista;
    }

    /**
     * Método responsável por inicializar a lista de valores (<code>SelectItem</code>) para todos os ComboBox's.
     */
    public void inicializarListasSelectItemTodosComboBox() {
        montarListaSelectItemProcSeletivo();
        montarListaSelectItemUnidadeEnsino();
        montarListaSelectItemOpcaoLinguaEstrangeira();
        montarListaSelectItemUnidadeEnsinoCons();
        montarListaSelectItemProcessoSeletivo();
        setListaSelectItemCursoOpcao(new ArrayList(0));
    }

    /**
     * Rotina responsável por preencher a combo de consulta da telas.
     */
    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("nomePessoa", "Candidato"));
        itens.add(new SelectItem("nomeUnidadeEnsino", "Unidade Ensino"));
        itens.add(new SelectItem("codigo", "Número da Inscrição"));
        itens.add(new SelectItem("descricaoProcSeletivo", "Processo Seletivo"));
        itens.add(new SelectItem("nomeCurso", "Curso"));
        itens.add(new SelectItem("data", "Data"));
        itens.add(new SelectItem("situacao", "Situação"));
        return itens;
    }

    /**
     * Rotina responsável por organizar a paginação entre as páginas resultantes de uma consulta.
     */
    public String inicializarConsultar() {
        removerObjetoMemoria(this);
//        setPaginaAtualDeTodas("0/0");
//        setListaConsulta(new ArrayList(0));
//        definirVisibilidadeLinksNavegacao(0, 0);
        montarListaSelectItemUnidadeEnsinoCons();
        montarListaSelectItemProcessoSeletivo();
        getControleConsultaOtimizado().setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoCons.xhtml");
    }

    public boolean getExisteUnidadeEnsino() {
        if (getInscricaoVO().getUnidadeEnsino().getCodigo().intValue() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public List<SelectItem> tipoConsultaComboAluno;
    public List<SelectItem> getTipoConsultaComboAluno() {
    	if(tipoConsultaComboAluno == null) {
    	tipoConsultaComboAluno = new ArrayList<SelectItem>(0);
    	tipoConsultaComboAluno.add(new SelectItem("nome", "Nome"));
    	tipoConsultaComboAluno.add(new SelectItem("CPF", "CPF"));
    	tipoConsultaComboAluno.add(new SelectItem("RG", "RG"));
    }
        return tipoConsultaComboAluno;
    }

    public void consultarAluno() {
        try {
            if (getCampoConsultaAluno().equals("nome")) {
            	getControleConsultaCandidato().setListaConsulta(getFacadeFactory().getPessoaFacade().consultaRapidaResumidaPorNome(getValorConsultaAluno(), TipoPessoa.CANDIDATO.getValor(), getControleConsultaCandidato().getLimitePorPagina(), getControleConsultaCandidato().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            	getControleConsultaCandidato().setTotalRegistrosEncontrados(getFacadeFactory().getPessoaFacade().consultaTotalDeRegistroRapidaResumidaPorNome(getValorConsultaAluno(), TipoPessoa.CANDIDATO.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));            	
            }
            if (getCampoConsultaAluno().equals("CPF")) {
            	getControleConsultaCandidato().setListaConsulta(getFacadeFactory().getPessoaFacade().consultaRapidaResumidaPorCPF(getValorConsultaAluno(), TipoPessoa.CANDIDATO.getValor(), getControleConsultaCandidato().getLimitePorPagina(), getControleConsultaCandidato().getOffset(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            	getControleConsultaCandidato().setTotalRegistrosEncontrados(getFacadeFactory().getPessoaFacade().consultaTotalDeRegistroRapidaResumidaPorCPF(getValorConsultaAluno(), TipoPessoa.CANDIDATO.getValor(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            }
            if (getCampoConsultaAluno().equals("RG")) {
            	getControleConsultaCandidato().setListaConsulta(getFacadeFactory().getPessoaFacade().consultaRapidaResumidaPorRG(getValorConsultaAluno(), TipoPessoa.CANDIDATO.getValor(), getControleConsultaCandidato().getLimitePorPagina(), getControleConsultaCandidato().getOffset(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));
            	getControleConsultaCandidato().setTotalRegistrosEncontrados(getFacadeFactory().getPessoaFacade().consultaTotalDeRegistroRapidaResumidaPorRG(getValorConsultaAluno(), TipoPessoa.CANDIDATO.getValor(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado()));                
            }

            setMensagemID("msg_dados_consultados");

        } catch (Exception e) {
        	getControleConsultaCandidato().setListaConsulta(new ArrayList(0));
        	getControleConsultaCandidato().setTotalRegistrosEncontrados(0);
            setMensagemDetalhada("msg_erro", e.getMessage());

        }
    }

	public void scrollerListenerCandidato(DataScrollEvent DataScrollEvent) throws Exception {
		getControleConsultaCandidato().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaCandidato().setPage(DataScrollEvent.getPage());
		consultarAluno();
	}

    public void montarConsultaAluno() {
    	setControleConsultaOtimizado(null);
    }

    public void selecionarAluno() throws Exception {

        PessoaVO aluno = (PessoaVO) context().getExternalContext().getRequestMap().get("alunoItens");
        getInscricaoVO().setCandidato(aluno);
    }

    public String getCadastrarNovoAluno() throws Exception {
        try {
            navegarPara(CandidatoControle.class.getSimpleName(), "novo", "");
            executarMetodoControle(CandidatoControle.class.getSimpleName(), "novo");
            return "abrirPopup('candidatoForm.xhtml', 'candidatoForm', 780, 585);";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }
    

	@PostConstruct
	private void recuperarCandidatoPeloForm(){
//		 context().getExternalContext().getSessionMap().put(InscricaoControle.class.getSimpleName(), inscricaoControle);
		if (context().getExternalContext().getSessionMap().get("inscricaoVO") != null) {
			setInscricaoVO((InscricaoVO)context().getExternalContext().getSessionMap().get("inscricaoVO"));
			context().getExternalContext().getSessionMap().remove("inscricaoVO");
		}
	}

    public String getRespLiberacaoPgtoInsc_Erro() {
        if (respLiberacaoPgtoInsc_Erro == null) {
            respLiberacaoPgtoInsc_Erro = "";
        }
        return respLiberacaoPgtoInsc_Erro;
    }

    public void setRespLiberacaoPgtoInsc_Erro(String respLiberacaoPgtoInsc_Erro) {
        this.respLiberacaoPgtoInsc_Erro = respLiberacaoPgtoInsc_Erro;
    }

    public String getResponsavel_Erro() {
        if (responsavel_Erro == null) {
            responsavel_Erro = "";
        }
        return responsavel_Erro;
    }

    public void setResponsavel_Erro(String responsavel_Erro) {
        this.responsavel_Erro = responsavel_Erro;
    }

    public Boolean getOpcao1() {
        return opcao1;
    }

    public void setOpcao1(Boolean opcao1) {
        this.opcao1 = opcao1;
    }

    public Boolean getOpcao2() {
        return opcao2;
    }

    public void setOpcao2(Boolean opcao2) {
        this.opcao2 = opcao2;
    }

    public Boolean getOpcao3() {
        return opcao3;
    }

    public void setOpcao3(Boolean opcao3) {
        this.opcao3 = opcao3;
    }

    public List getListaSelectItemCursoOpcao() {
        if (listaSelectItemCursoOpcao == null) {
            listaSelectItemCursoOpcao = new ArrayList(0);
        }
        return (listaSelectItemCursoOpcao);
    }

    public void setListaSelectItemCursoOpcao(List listaSelectItemCursoOpcao) {
        this.listaSelectItemCursoOpcao = listaSelectItemCursoOpcao;
    }

    public List getListaSelectItemProcSeletivo() {
        if (listaSelectItemProcSeletivo == null) {
            listaSelectItemProcSeletivo = new ArrayList(0);
        }
        return (listaSelectItemProcSeletivo);
    }

    public void setListaSelectItemProcSeletivo(List listaSelectItemProcSeletivo) {
        this.listaSelectItemProcSeletivo = listaSelectItemProcSeletivo;
    }

    public String getCandidato_Erro() {
        if (candidato_Erro == null) {
            candidato_Erro = "";
        }
        return candidato_Erro;
    }

    public void setCandidato_Erro(String candidato_Erro) {
        this.candidato_Erro = candidato_Erro;
    }

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return (listaSelectItemUnidadeEnsino);
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public InscricaoVO getInscricaoVO() {
        if (inscricaoVO == null) {
            inscricaoVO = new InscricaoVO();
        }
        return inscricaoVO;
    }

    public void setInscricaoVO(InscricaoVO inscricaoVO) {
        this.inscricaoVO = inscricaoVO;
    }

    /**
     * Rotina responsável por liberar alterar a situação de uma campanha de forma a autorizar sua produção e execucação.
     * Usuário deverá possuir acesso a esta ação em seu perfil de acesso.
     */
    public String liberarInscricaoForaPrazo() {
        try {
            if (inscricaoVO.getSituacao().equals("PF") || inscricaoVO.getProcSeletivo().getValorInscricao().equals(0.0)) {
                getFacadeFactory().getInscricaoFacade().liberarInscricaoForaPrazo(inscricaoVO, getUsuarioLogado());
                gravar();
                setMensagemID("msg_inscricao_liberarInscricaoForaPrazo");
            } else {
                setMensagemID("msg_inscricao_inscricaoQuitado");
            }
            return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoForm.xhtml");
        } catch (Exception e) {
            inscricaoVO.setResponsavelLiberacaoForaPrazo(new UsuarioVO());
            inscricaoVO.setDataLiberacaoForaPrazo(null);
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoForm.xhtml");
        }
    }

    public void emitirBoletoPagamentoInscricaoEvent(ActionEvent evt) {
        emitirBoletoPagamentoInscricao();
    }

    public void emitirBoletoPagamentoInscricao() {
        try {
            if (inscricaoVO.getSituacao().equals("PG")) {
                setMensagemID("msg_requerimento_requerimentoJaQuitadaFinanceiramente");
                setImprimir(false);
            } else {
                if (inscricaoVO.getCodigo().intValue() == 0) {
                    String t = gravarDuranteEmissaoBoleto();
                    if (!t.equals("ok")) {
                        setImprimir(false);
                        setMensagemDetalhada("msg_erro", t);
                        return;
                    }
                }
                setImprimir(true);
                setMensagemID("msg_inscricao_emitirBoletoPagamento");
            }
            //return "editar";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            setImprimir(false);
            //return "editar";
        }

    }

    public String getBoleto() {
        if (getImprimir()) {
            return "abrirPopup('../../BoletoBancarioSV?codigoContaReceber=" + inscricaoVO.getContaReceber() + "&titulo=inscricao', 'boletoInscricao', 780, 585)";
        }
        return "";
    }

    /**
     * Rotina responsável por liberar alterar a situação de uma campanha de forma a autorizar sua produção e execucação.
     * Usuário deverá possuir acesso a esta ação em seu perfil de acesso.
     */
    public String liberarPagamentoInscricao() {
        try {
            if (inscricaoVO.getSituacao().equals("CO")) {
                setMensagemID("msg_inscricao_inscricaoJaQuitadaFinanceiramente");
                return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoForm.xhtml");
            }
            getFacadeFactory().getInscricaoFacade().liberarPagamentoInscricao(inscricaoVO, getUsuarioLogado());
            inscricaoVO.setLiberarPagamento(Boolean.TRUE);
            this.setApresentarBotaoEmitirBoleto(Boolean.FALSE);
            setMensagemID("msg_inscricao_liberarInscricao");
            return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoForm.xhtml");
        } catch (Exception e) {
            inscricaoVO.setRespLiberacaoPgtoInsc(new UsuarioVO());
            inscricaoVO.setData(null);
            inscricaoVO.setSituacao("PF");
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("inscricaoForm.xhtml");
        }
    }
    /* Método responsável por inicializar List<SelectItem> de valores do
     * ComboBox correspondente ao atributo <code>formaAcessoProcSeletivo</code>
     */

    public List getListaSelectItemFormaAcessoProcSeletivoInscricao() throws Exception {
        List objs = new ArrayList(0);
        Hashtable inscricaoFormaAcessos = (Hashtable) Dominios.getInscricaoFormaAcesso();
        Enumeration keys = inscricaoFormaAcessos.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) inscricaoFormaAcessos.get(value);
            objs.add(new SelectItem(value, label));
        }
        keys = null;
        return objs;
    }

    /* Método responsável por inicializar List<SelectItem> de valores do
     * ComboBox correspondente ao atributo <code>opcaoLinguaEstrangeira</code>
     */
    public List getListaSelectItemOpcaoLinguaEstrangeiraInscricao() throws Exception {
        List objs = new ArrayList(0);
        Hashtable inscricaoOpcaoLinguaEstrangeiras = (Hashtable) Dominios.getInscricaoOpcaoLinguaEstrangeira();
        Enumeration keys = inscricaoOpcaoLinguaEstrangeiras.keys();
        while (keys.hasMoreElements()) {
            String value = (String) keys.nextElement();
            String label = (String) inscricaoOpcaoLinguaEstrangeiras.get(value);
            objs.add(new SelectItem(value, label));
        }
        keys = null;
        return objs;
    }

    public String getResponsavelLiberacaoForaPrazo_Erro() {
        if (responsavelLiberacaoForaPrazo_Erro == null) {
            responsavelLiberacaoForaPrazo_Erro = "";
        }
        return responsavelLiberacaoForaPrazo_Erro;
    }

    public void setResponsavelLiberacaoForaPrazo_Erro(String responsavelLiberacaoForaPrazo_Erro) {
        this.responsavelLiberacaoForaPrazo_Erro = responsavelLiberacaoForaPrazo_Erro;
    }

    @Override
    protected void limparRecursosMemoria() {
        super.limparRecursosMemoria();
        inscricaoVO = null;
        Uteis.liberarListaMemoria(listaSelectItemUnidadeEnsino);
        candidato_Erro = null;
        Uteis.liberarListaMemoria(listaSelectItemProcSeletivo);
        Uteis.liberarListaMemoria(listaSelectItemCursoOpcao);
        responsavel_Erro = null;
        respLiberacaoPgtoInsc_Erro = null;
        responsavelLiberacaoForaPrazo_Erro = null;
        opcao1 = null;
        opcao2 = null;
        opcao3 = null;
        tmpCodigoProcessoSeletivo = null;
    }

    public List getListaSelectItemOpcaolinguaEstrangeira() {
        if (listaSelectItemOpcaolinguaEstrangeira == null) {
            listaSelectItemOpcaolinguaEstrangeira = new ArrayList(0);
        }
        return listaSelectItemOpcaolinguaEstrangeira;
    }

    public void setListaSelectItemOpcaolinguaEstrangeira(List listaSelectItemOpcaolinguaEstrangeira) {
        this.listaSelectItemOpcaolinguaEstrangeira = listaSelectItemOpcaolinguaEstrangeira;
    }

    public Boolean getApresentarBotaoEmitirBoletoTela() {
        if (inscricaoVO.getCodigo()>0 && inscricaoVO.getSituacao().equals("PF") && inscricaoVO.getItemProcessoSeletivoDataProva().getCodigo() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean getApresentarBotaoEmitirBoleto() {
        return apresentarBotaoEmitirBoleto;
    }

    public void setApresentarBotaoEmitirBoleto(Boolean apresentarBotaoEmitirBoleto) {
        this.apresentarBotaoEmitirBoleto = apresentarBotaoEmitirBoleto;
    }

    public Boolean getImprimir() {
        if (imprimir == null) {
            imprimir = Boolean.FALSE;
        }
        return imprimir;
    }

    public void setImprimir(Boolean imprimir) {
        this.imprimir = imprimir;
    }

    public String getCampoConsultaAluno() {
        if (campoConsultaAluno == null) {
            campoConsultaAluno = "";
        }
        return campoConsultaAluno;
    }

    public void setCampoConsultaAluno(String campoConsultaAluno) {
        this.campoConsultaAluno = campoConsultaAluno;
    }

    public List getListaConsultaAluno() {
        if (listaConsultaAluno == null) {
            listaConsultaAluno = new ArrayList(0);
        }
        return listaConsultaAluno;
    }

    public void setListaConsultaAluno(List listaConsultaAluno) {
        this.listaConsultaAluno = listaConsultaAluno;
    }

    public String getValorConsultaAluno() {
        if (valorConsultaAluno == null) {
            valorConsultaAluno = "";
        }
        return valorConsultaAluno;
    }

    public void setValorConsultaAluno(String valorConsultaAluno) {
        this.valorConsultaAluno = valorConsultaAluno;
    }

   

    /**
     * @return the listaSelectItemDatasProva
     */
    public List getListaSelectItemDatasProva() {
        if (listaSelectItemDatasProva == null) {
            listaSelectItemDatasProva = new ArrayList(0);
        }
        return listaSelectItemDatasProva;
    }

    /**
     * @param listaSelectItemDatasProva the listaSelectItemDatasProva to set
     */
    public void setListaSelectItemDatasProva(List listaSelectItemDatasProva) {
        this.listaSelectItemDatasProva = listaSelectItemDatasProva;
    }
    
    public boolean getIsApresentarCampoDescricaoNecessidadeEspecial() {
        return getInscricaoVO().getPortadorNecessidadeEspecial();
    }
    
    public boolean getIsApresentarBotaoEmitirBoleto() {
        if (getInscricaoVO().getSituacao().equals("PF")) {
            return true;
        } else {
            return false;
        }
    }
    
    public boolean getIsApresentarAbaPagamentoInscricao() {
        return !getInscricaoVO().getCodigo().equals(0) && getIsApresentarBotaoEmitirBoleto();
    }

	public String getValorConsultaProcSeletivo() {
		if (valorConsultaProcSeletivo == null) {
			valorConsultaProcSeletivo = "";
		}
		return valorConsultaProcSeletivo;
	}

	public void setValorConsultaProcSeletivo(String valorConsultaProcSeletivo) {
		this.valorConsultaProcSeletivo = valorConsultaProcSeletivo;
	}

	public String getCampoConsultaProcSeletivo() {
		if(campoConsultaProcSeletivo == null) {
			campoConsultaProcSeletivo = "";
		}
		return campoConsultaProcSeletivo;
	}

	public void setCampoConsultaProcSeletivo(String campoConsultaProcSeletivo) {
		this.campoConsultaProcSeletivo = campoConsultaProcSeletivo;
	}

	public List getListaConsultaProcSeletivo() {
		if (listaConsultaProcSeletivo == null) {
			listaConsultaProcSeletivo = new ArrayList(0);
		}
		return listaConsultaProcSeletivo;
	}

	public void setListaConsultaProcSeletivo(List listaConsultaProcSeletivo) {
		this.listaConsultaProcSeletivo = listaConsultaProcSeletivo;
	}
	
	public boolean getApresentarCampoDataProva() {
		return Uteis.isAtributoPreenchido(getInscricaoVO().getProcSeletivo()) && !TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR.equals(getInscricaoVO().getProcSeletivo().getTipoAvaliacaoProcessoSeletivo()) && getInscricaoVO().getFormaIngresso().equals("PS");
	}
	
	public boolean verificarUsuarioPossuiPermissaoRealizarCancelamentoInscricao(String identificadorAcaoPermissao) {
		try {
			ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(identificadorAcaoPermissao,getUsuarioLogado());
			return true;
		} catch (Exception e) {
			return false;
		}
	}
 

	public Boolean getPermitirRealizarCancelamentoInscricao() {
		if (permitirRealizarCancelamentoInscricao == null) {
			permitirRealizarCancelamentoInscricao = true;
		}
		return permitirRealizarCancelamentoInscricao;
	}

	public void setPermitirRealizarCancelamentoInscricao(Boolean permitirRealizarCancelamentoInscricao) {
		this.permitirRealizarCancelamentoInscricao = permitirRealizarCancelamentoInscricao;
	}
	
	public Boolean getPermitirRealizarCancelamentoDevidoOutraInscricao() {
		if(permitirRealizarCancelamentoDevidoOutraInscricao == null){
			permitirRealizarCancelamentoDevidoOutraInscricao = true;
		}
		return permitirRealizarCancelamentoDevidoOutraInscricao;
	}

	public void setPermitirRealizarCancelamentoDevidoOutraInscricao(
			Boolean permitirRealizarCancelamentoDevidoOutraInscricao) {
		this.permitirRealizarCancelamentoDevidoOutraInscricao = permitirRealizarCancelamentoDevidoOutraInscricao;
	}

	public void validarPermissaoUsuarioAlterarSituacaoCancelado() throws Exception {
		if (!getInscricaoVO().getSituacaoInscricaoOrigem().equals(SituacaoInscricaoEnum.CANCELADO) && getInscricaoVO().getSituacaoInscricao().equals(SituacaoInscricaoEnum.CANCELADO) && !getInscricaoVO().isNovoObj() && !this.verificarUsuarioPossuiPermissaoRealizarCancelamentoInscricao("PermitirRealizarCancelamento")) {
			setPermitirRealizarCancelamentoInscricao(false);
		}
    	if (!getPermitirRealizarCancelamentoInscricao()) {
    		setPermitirRealizarCancelamentoInscricao(true);
			throw new Exception("Você não possui permissão para alterar o campo (Situação Inscrição) para " + SituacaoInscricaoEnum.CANCELADO.getValorApresentar() + ".");
		}
    	
		if (!getInscricaoVO().getSituacaoInscricaoOrigem().equals(SituacaoInscricaoEnum.CANCELADO_OUTRA_INSCRICAO) && getInscricaoVO().getSituacaoInscricao().equals(SituacaoInscricaoEnum.CANCELADO_OUTRA_INSCRICAO) && !getInscricaoVO().isNovoObj() && !this.verificarUsuarioPossuiPermissaoRealizarCancelamentoInscricao("PermitirRealizarCancelamentoDevidoOutraInscricao")) {
			setPermitirRealizarCancelamentoDevidoOutraInscricao(false);
		}
    	if (!getPermitirRealizarCancelamentoDevidoOutraInscricao()) {
    		setPermitirRealizarCancelamentoDevidoOutraInscricao(true);
			throw new Exception("Você não possui permissão para alterar o campo (Situação Inscrição) para " + SituacaoInscricaoEnum.CANCELADO_OUTRA_INSCRICAO.getValorApresentar() + ".");
		}
    	if(!getInscricaoVO().getSituacaoInscricaoOrigem().equals(getInscricaoVO().getSituacaoInscricao()) && !getInscricaoVO().isNovoObj()) {
    		if(getInscricaoVO().getResultadoProcessoSeletivoVO().getCodigo().equals(0)) {
				 ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVOBuscar = getFacadeFactory().getResultadoProcessoSeletivoFacade().consultarPorCodigoInscricao_ResultadoUnico(
						 getInscricaoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				 if(resultadoProcessoSeletivoVOBuscar.getCodigo() > 0) {
						throw new Exception(" não é possível cancelar uma inscrição que possui resultado no processo seletivo .");

				 }
			}else if(getInscricaoVO().getResultadoProcessoSeletivoVO().getCodigo() > 0){
				throw new Exception(" não é possível cancelar uma inscrição que possui resultado no processo seletivo.");

			}
    	}
    	 
			 
		
    	
	}
	
	
	
	public List getListaTipoSelecao() {
		if (listaTipoSelecao == null) {
			listaTipoSelecao = new ArrayList(0);
		}
		return listaTipoSelecao;
	}

	public void setListaTipoSelecao(List listaTipoSelecao) {
		this.listaTipoSelecao = listaTipoSelecao;
	}
	
	
	public Integer getItemProcessoSeletivoMaiorDataProva() {
		if (itemProcessoSeletivoMaiorDataProva == null) {
			itemProcessoSeletivoMaiorDataProva = 0;
		}
		return itemProcessoSeletivoMaiorDataProva;
	}

	public void setItemProcessoSeletivoMaiorDataProva(Integer itemProcessoSeletivoMaiorDataProva) {
		this.itemProcessoSeletivoMaiorDataProva = itemProcessoSeletivoMaiorDataProva;
	}

	public boolean getApresentarFormaIngresso() {
		return Uteis.isAtributoPreenchido(getInscricaoVO().getProcSeletivo()) && !TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR.equals(getInscricaoVO().getProcSeletivo().getTipoAvaliacaoProcessoSeletivo());
	}
	public boolean getApresentarDataProva() {		
		return Uteis.isAtributoPreenchido(getInscricaoVO().getProcSeletivo()) && !TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR.equals(getInscricaoVO().getProcSeletivo().getTipoAvaliacaoProcessoSeletivo()) && getInscricaoVO().getFormaIngresso().equals("PS");
	}
	
	public boolean getApresentarUpload() {		
		return Uteis.isAtributoPreenchido(getInscricaoVO().getProcSeletivo()) && (!TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR.equals(getInscricaoVO().getProcSeletivo().getTipoAvaliacaoProcessoSeletivo()) 
				&& (getInscricaoVO().getFormaIngresso().equals("EN") || getInscricaoVO().getFormaIngresso().equals("PD") || getInscricaoVO().getFormaIngresso().equals("TR")) || (TipoAvaliacaoProcessoSeletivoEnum.AVALIACAO_CURRICULAR.equals(getInscricaoVO().getProcSeletivo().getTipoAvaliacaoProcessoSeletivo())));
	}
	
	public boolean getArquivoAnexado() {		
		return Uteis.isAtributoPreenchido(getInscricaoVO().getArquivoVO().getDescricao());
	}
	
	public void montarListaSelectTipoSelecao() {
		try {
			
			List objs = new ArrayList(0);
			
			objs.add(new SelectItem("", ""));
			
			if (getInscricaoVO().getProcSeletivo().getTipoProcessoSeletivo().equals(true)) {
				objs.add(new SelectItem("PS","PROCESSO SELETIVO"));
				
			}
			
			if (getInscricaoVO().getProcSeletivo().getTipoEnem().equals(true)) {
				objs.add(new SelectItem("EN", "ENEM/ANÁLISE DOCUMENTO"));
				
			}
			if (getInscricaoVO().getProcSeletivo().getTipoPortadorDiploma().equals(true)) {
				objs.add(new SelectItem("PD","PORTADOR DIPLOMA"));
				
			}	
			
			if (getInscricaoVO().getProcSeletivo().getTipoTransferencia().equals(true)) {
				objs.add(new SelectItem("TR","TRANSFERÊNCIA"));
				
			}
			
			setListaTipoSelecao(objs);
			
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void upLoadArquivo(FileUploadEvent uploadEvent) {
		try {
			getInscricaoVO().getArquivoVO().setCpfAlunoDocumentacao(inscricaoVO.getCandidato().getCPF());
			getFacadeFactory().getArquivoHelper().upLoad(uploadEvent, getInscricaoVO().getArquivoVO(), getConfiguracaoGeralPadraoSistema(), PastaBaseArquivoEnum.CERTIFICADOSINSCRICOES_TMP, getUsuarioLogado());
			//getArquivoVO().setDescricao(uploadEvent.getUploadedFile().getName().substring(0, uploadEvent.getUploadedFile().getName().lastIndexOf(".")));
			getInscricaoVO().getArquivoVO().setDescricao(uploadEvent.getUploadedFile().getName().substring(0, uploadEvent.getUploadedFile().getName().lastIndexOf(".")));
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		} finally {
			uploadEvent = null;
		}
	} 
	
	public void downloadArquivo() throws Exception {
		try {
			String arquivo = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator +  getInscricaoVO().getArquivoVO().getPastaBaseArquivoEnum().getValue() + File.separator  + getInscricaoVO().getArquivoVO().getCpfAlunoDocumentacao() + File.separator + getInscricaoVO().getArquivoVO().getNome();
			InputStream fs = new FileInputStream(arquivo);
			
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", getInscricaoVO().getArquivoVO().getNome());
			request.getSession().setAttribute("pastaBaseArquivo", arquivo.substring(0, arquivo.lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}

	public Integer getCodigoUnidadeEnsinoConsulta() {
		if(codigoUnidadeEnsinoConsulta == null) {
			codigoUnidadeEnsinoConsulta = 0;
		}
		return codigoUnidadeEnsinoConsulta;
	}

	public void setCodigoUnidadeEnsinoConsulta(Integer codigoUnidadeEnsinoConsulta) {
		this.codigoUnidadeEnsinoConsulta = codigoUnidadeEnsinoConsulta;
	}

	public String getNumeroInscricaoConsulta() {
		if(numeroInscricaoConsulta == null) {
			numeroInscricaoConsulta = "";
		}
		return numeroInscricaoConsulta;
	}

	public void setNumeroInscricaoConsulta(String numeroInscricaoConsulta) {
		this.numeroInscricaoConsulta = numeroInscricaoConsulta;
	}

	public String getNomeCanditadoConsulta() {
		if(nomeCanditadoConsulta == null) {
			nomeCanditadoConsulta = "";
		}
		return nomeCanditadoConsulta;
	}

	public void setNomeCanditadoConsulta(String nomeCanditadoConsulta) {
		this.nomeCanditadoConsulta = nomeCanditadoConsulta;
	}

	public Date getDataInicioInscricaoConsulta() {
		return dataInicioInscricaoConsulta;
	}

	public void setDataInicioInscricaoConsulta(Date dataInicioInscricaoConsulta) {
		this.dataInicioInscricaoConsulta = dataInicioInscricaoConsulta;
	}

	public Date getDataFimInscricaoConsulta() {
		return dataFimInscricaoConsulta;
	}

	public void setDataFimInscricaoConsulta(Date dataFimInscricaoConsulta) {
		this.dataFimInscricaoConsulta = dataFimInscricaoConsulta;
	}

	public Date getDataInicioProvaConsulta() {
		return dataInicioProvaConsulta;
	}

	public void setDataInicioProvaConsulta(Date dataInicioProvaConsulta) {
		this.dataInicioProvaConsulta = dataInicioProvaConsulta;
	}

	public Date getDataFimProvaConsulta() {
		return dataFimProvaConsulta;
	}

	public void setDataFimProvaConsulta(Date dataFimProvaConsulta) {
		this.dataFimProvaConsulta = dataFimProvaConsulta;
	}

	public List getListaSelectItemUnidadeEnsinoCons() {
		if (listaSelectItemUnidadeEnsinoCons == null) {
			listaSelectItemUnidadeEnsinoCons = new ArrayList(0);
		}
		return listaSelectItemUnidadeEnsinoCons;
	}

	public void setListaSelectItemUnidadeEnsinoCons(List listaSelectItemUnidadeEnsinoCons) {
		this.listaSelectItemUnidadeEnsinoCons = listaSelectItemUnidadeEnsinoCons;
	}
	
	private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNomeCons(String nomePrm) throws Exception {
		List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, getUsuarioLogado());
		return lista;
	}
	
	public void montarListaSelectItemUnidadeEnsinoCons() {
		try {
			List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNomeCons("");
			setListaSelectItemUnidadeEnsinoCons(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
			getListaSelectItemUnidadeEnsinoCons();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public void scrollListener(DataScrollEvent DataScrollEvent) {
		getControleConsultaOtimizado().setPaginaAtual(DataScrollEvent.getPage());
		getControleConsultaOtimizado().setPage(DataScrollEvent.getPage());
		consultarInscricao();
	}

	public Boolean getFiltroSituacaoInscricaoConsultaAtivo() {
		if(filtroSituacaoInscricaoConsultaAtivo == null) {
			filtroSituacaoInscricaoConsultaAtivo = Boolean.FALSE;
		}
		return filtroSituacaoInscricaoConsultaAtivo;
	}

	public void setFiltroSituacaoInscricaoConsultaAtivo(Boolean filtroSituacaoInscricaoConsultaAtivo) {
		this.filtroSituacaoInscricaoConsultaAtivo = filtroSituacaoInscricaoConsultaAtivo;
	}

	public Boolean getFiltroSituacaoInscricaoConsultaCancelouOutraInscricao() {
		if(filtroSituacaoInscricaoConsultaCancelouOutraInscricao == null) {
			filtroSituacaoInscricaoConsultaCancelouOutraInscricao = Boolean.FALSE;
		}
		return filtroSituacaoInscricaoConsultaCancelouOutraInscricao;
	}

	public void setFiltroSituacaoInscricaoConsultaCancelouOutraInscricao(
			Boolean filtroSituacaoInscricaoConsultaCancelouOutraInscricao) {
		this.filtroSituacaoInscricaoConsultaCancelouOutraInscricao = filtroSituacaoInscricaoConsultaCancelouOutraInscricao;
	}

	public Boolean getFiltroSituacaoInscricaoConsultaNaoCompareceu() {
		if(filtroSituacaoInscricaoConsultaNaoCompareceu == null) {
			filtroSituacaoInscricaoConsultaNaoCompareceu = Boolean.FALSE;
		}
		return filtroSituacaoInscricaoConsultaNaoCompareceu;
	}

	public void setFiltroSituacaoInscricaoConsultaNaoCompareceu(
			Boolean filtroSituacaoInscricaoConsultaNaoCompareceu) {
		this.filtroSituacaoInscricaoConsultaNaoCompareceu = filtroSituacaoInscricaoConsultaNaoCompareceu;
	}

	public Boolean getFiltroSituacaoInscricaoConsultaCancelado() {
		if(filtroSituacaoInscricaoConsultaCancelado == null) {
			filtroSituacaoInscricaoConsultaCancelado = Boolean.FALSE;
		}
		return filtroSituacaoInscricaoConsultaCancelado;
	}

	public void setFiltroSituacaoInscricaoConsultaCancelado(Boolean filtroSituacaoInscricaoConsultaCancelado) {
		this.filtroSituacaoInscricaoConsultaCancelado = filtroSituacaoInscricaoConsultaCancelado;
	}

	public List getListaSelectItemProcessoSeletivo() {
		if (listaSelectItemProcessoSeletivo == null) {
			listaSelectItemProcessoSeletivo = new ArrayList(0);
		}
		return listaSelectItemProcessoSeletivo;
	}

	public void setListaSelectItemProcessoSeletivo(List listaSelectItemProcessoSeletivo) {
		this.listaSelectItemProcessoSeletivo = listaSelectItemProcessoSeletivo;
	}
		
	public void montarListaSelectItemProcessoSeletivo() {
		try {
			List<ProcSeletivoVO> resultadoConsulta = consultarProcessoSeletivo();
			setListaSelectItemProcessoSeletivo(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "descricao"));
			getListaSelectItemProcessoSeletivo();
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	private List<ProcSeletivoVO> consultarProcessoSeletivo() throws Exception {
		List<ProcSeletivoVO> lista = getFacadeFactory().getProcSeletivoFacade().consultarPorDescricaoUnidadeEnsino("", getUnidadeEnsinoLogado().getCodigo(), true, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
		return lista;
	}

	public Integer getCodigoProcessoSeletivoConsulta() {
		if(codigoProcessoSeletivoConsulta == null) {
			codigoProcessoSeletivoConsulta = 0;
		}
		return codigoProcessoSeletivoConsulta;
	}

	public void setCodigoProcessoSeletivoConsulta(Integer codigoProcessoSeletivoConsulta) {
		this.codigoProcessoSeletivoConsulta = codigoProcessoSeletivoConsulta;
	}
	
    public void selecionarAlunoFiltro() throws Exception {

        PessoaVO aluno = (PessoaVO) context().getExternalContext().getRequestMap().get("alunoItens");
        setNomeCanditadoConsulta(aluno.getNome());
    }
    
    public void downloadArquivoTipoIngresso() {
    	try {
    		
    		InscricaoVO obj = (InscricaoVO) context().getExternalContext().getRequestMap().get("inscricaoItens");
    		
			String arquivo = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo() + File.separator +  obj.getArquivoVO().getPastaBaseArquivoEnum().getValue() + File.separator  + obj.getArquivoVO().getCpfAlunoDocumentacao() + File.separator + obj.getArquivoVO().getNome();
			InputStream fs = new FileInputStream(arquivo);
			
			HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
			request.getSession().setAttribute("nomeArquivo", obj.getArquivoVO().getNome());
			request.getSession().setAttribute("pastaBaseArquivo", arquivo.substring(0, arquivo.lastIndexOf(File.separator)));
			request.getSession().setAttribute("deletarArquivo", false);
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
			
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
    }
	
	public DataModelo getControleConsultaCandidato() {
		if(controleConsultaCandidato == null) {
			controleConsultaCandidato =  new DataModelo();
			controleConsultaCandidato.setLimitePorPagina(5);
}
		return controleConsultaCandidato;
	}

	public void setControleConsultaCandidato(DataModelo controleConsultaCandidato) {
		this.controleConsultaCandidato = controleConsultaCandidato;
	}
    
	
	public void validarAlterarDataProva() throws Exception{
		if (Uteis.isAtributoPreenchido(getItemProcSeletivoDataProvaVOTemp().getCodigo()) && !getInscricaoVO().isNovoObj()) {
			if (!getInscricaoVO().getItemProcessoSeletivoDataProva().getCodigo().equals(getItemProcSeletivoDataProvaVOTemp().getCodigo())) {
				ResultadoProcessoSeletivoVO resultadoProcessoSeletivoVOBuscar = getFacadeFactory()
						    .getResultadoProcessoSeletivoFacade().consultarPorCodigoInscricao_ResultadoUnico(
								getInscricaoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado());
				if (resultadoProcessoSeletivoVOBuscar.getCodigo() > 0) {
					throw new Exception(" não é possível alterar data da prova de  uma inscrição que possui resultado no processo seletivo .");
				} else {
					getInscricaoVO().setCodigoAutenticacaoNavegador(null);
					getInscricaoVO().setDataHoraInicio(null);
					getInscricaoVO().setDataHoraTermino(null);
				}
			}
		}

	}

	public ItemProcSeletivoDataProvaVO getItemProcSeletivoDataProvaVOTemp() {
		if(ItemProcSeletivoDataProvaVOTemp == null ){
			ItemProcSeletivoDataProvaVOTemp = new  ItemProcSeletivoDataProvaVO();
		}
		return ItemProcSeletivoDataProvaVOTemp;
	}

	public void setItemProcSeletivoDataProvaVOTemp(ItemProcSeletivoDataProvaVO itemProcSeletivoDataProvaVOTemp) {
		ItemProcSeletivoDataProvaVOTemp = itemProcSeletivoDataProvaVOTemp;
	}
	
	public String getMascaraConsulta() {
		if(getCampoConsultaAluno().equals("CPF")) {
			return "return mascara(this.form, 'formCpf:cpf', '999.999.999-99', event);";
		} return "";
	}
	
	
}
