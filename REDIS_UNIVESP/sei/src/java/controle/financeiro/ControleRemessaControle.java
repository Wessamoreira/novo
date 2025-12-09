package controle.financeiro;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.DoubleSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import org.richfaces.event.FileUploadEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SelectItemOrdemValor;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.PersonalizacaoMensagemAutomaticaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TagsMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TemplateMensagemAutomaticaEnum;
import negocio.comuns.administrativo.enumeradores.TipoOrigemComunicacaoInternaEnum;
import negocio.comuns.arquitetura.OperacaoFuncionalidadeVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.arquitetura.enumeradores.Obrigatorio;
import negocio.comuns.arquitetura.enumeradores.OperacaoFuncionalidadeEnum;
import negocio.comuns.arquitetura.enumeradores.OrigemOperacaoFuncionalidadeEnum;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaVO;
import negocio.comuns.financeiro.UnidadeEnsinoContaCorrenteVO;
import negocio.comuns.financeiro.enumerador.BancoEnum;
import negocio.comuns.financeiro.enumerador.MotivoRejeicaoRemessa;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaEnum;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoArquivoRetornoEnum;
import negocio.comuns.utilitarias.ControleConsulta;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilSelectItem;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import relatorio.controle.arquitetura.SuperControleRelatorio;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import relatorio.negocio.comuns.financeiro.InadimplenciaRelVO;

@Controller("ControleRemessaControle")
@Scope("viewScope")
@Lazy
public class ControleRemessaControle extends SuperControleRelatorio implements Serializable {

    private ControleRemessaVO controleRemessaVO;
    private List listaSelectItemUnidadeEnsino;
    private UnidadeEnsinoVO unidadeEnsino;
    private List<ContaReceberVO> contaReceberVOs;
    private Boolean arquivoTeste;
    private List listaSelectItemContaCorrente;
    private List listaSelectItemBanco;
    private List listaSelectItemSituacao;
    private List<ControleRemessaContaReceberVO> listaDadosRemessaVOs;
    private List<ControleRemessaContaReceberVO> listaDadosRemessaErroVOs;
    private List<ControleRemessaContaReceberVO> listaEstornos;
    private ControleRemessaContaReceberVO controleRemessaContaReceberEstorno; 
    private Integer qtdRegistros;
    private Integer qtdRegistrosExclusao;
    private Integer qtdRegistrosErro;
    private Integer qtdRegistrosAlteracao;
    private Double vlrTotalRegistrosAlteracao;
    private Double vlrTotalRegistros;
    private Double vlrTotalRegistrosExclusao;
    private String situacaoControleRemessa;
    private SituacaoProcessamentoArquivoRetornoEnum situacaoProcessamento;
    private Boolean ativarPush;
    private String motivoEstorno;
    private Boolean estornoContaUnica;
    private ComunicacaoInternaVO comunicacaoEnviar;
    private String email;
    private Integer quantidadeAlunoMensagem;
    private Boolean enviarSMS;
    private FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO;    
	private List<ControleRemessaContaReceberVO> listaObjetos;
	private String userNameLiberarRemessaSemReajuste;
	private String senhaLiberarRemessaSemReajuste;
	private List<OperacaoFuncionalidadeVO> operacaoFuncionalidadeVOs;
	private ControleRemessaContaReceberVO controleRemessaContaReceberReajustePrecoVO;
	private Boolean fixarUnidadeEnsino;
	
    public ControleRemessaControle() throws Exception {        
        setControleConsulta(new ControleConsulta());
        getControleConsulta().setCampoConsulta("contaCorrente");
        getControleConsulta().setDataIni(new Date());
        getControleConsulta().setDataFim(Uteis.obterDataFutura(new Date(), 30));
        getFiltroRelatorioFinanceiroVO().setTipoOrigemMatricula(Boolean.TRUE);
        getFiltroRelatorioFinanceiroVO().setTipoOrigemMensalidade(Boolean.TRUE);
        getFiltroRelatorioFinanceiroVO().setSituacaoCancelado(Boolean.TRUE);
        getFiltroRelatorioFinanceiroVO().setSituacaoExcluida(Boolean.TRUE);
        getFiltroRelatorioFinanceiroVO().setSituacaoRenegociado(Boolean.TRUE);
        setAtivarPush(false);
        setMensagemID("msg_entre_prmconsulta");
    }

    public String novo() {
        removerObjetoMemoria(this);
        try {
            montarListaSelectItemUnidadeEnsino();
            montarListaSelectItemLayoutsBancos();
            montarListaSelectItemSituacao();
            setControleRemessaVO(null);
            setContaReceberVOs(null);
            setQtdRegistros(0);
            setQtdRegistrosErro(0);
            setVlrTotalRegistros(0.0);
            setQtdRegistrosAlteracao(0);
            setVlrTotalRegistrosAlteracao(0.0);
            setVlrTotalRegistrosAlteracao(0.0);
            getControleRemessaVO().setResponsavel(getUsuarioLogadoClone());
            setQtdRegistros(0);
            setVlrTotalRegistros(0.0);
            setAtivarPush(false);
//            getControleConsulta().setDataIni(new Date());
//            getControleConsulta().setDataFim(Uteis.obterDataFutura(new Date(), 30));        
            getControleRemessaVO().setDataInicio(new Date());
            getControleRemessaVO().setDataFim(Uteis.obterDataFutura(new Date(), 30));
            validarUnidadeEnsinoUsuarioLogado();
            getFiltroRelatorioFinanceiroVO().setTipoOrigemMatricula(Boolean.TRUE);
            getFiltroRelatorioFinanceiroVO().setTipoOrigemMensalidade(Boolean.TRUE);
            getFiltroRelatorioFinanceiroVO().setSituacaoCancelado(Boolean.TRUE);
            getFiltroRelatorioFinanceiroVO().setSituacaoExcluida(Boolean.TRUE);
            getFiltroRelatorioFinanceiroVO().setSituacaoRenegociado(Boolean.TRUE);
            setMensagemID("msg_entre_dados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("controleRemessaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String editar() {
        try {
            setQtdRegistrosErro(0);
            setQtdRegistros(0);
            setVlrTotalRegistros(0.0);
            setQtdRegistrosAlteracao(0);
            setVlrTotalRegistrosAlteracao(0.0);    
            ControleRemessaVO obj = (ControleRemessaVO) context().getExternalContext().getRequestMap().get("controleRemessaItens");
            setControleRemessaVO(obj);
            montarListaSelectItemUnidadeEnsino();
            montarListaSelectItemLayoutsBancos();
            montarListaSelectItemContaCorrente();
            setListaDadosRemessaVOs(getFacadeFactory().getControleRemessaContaReceberFacade().consultaRapidaContasArquivoRemessaPorCodigoControleRemessa(obj, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));
            processarListaErro();
            setControleRemessaVO(getFacadeFactory().getControleRemessaFacade().consultarPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            setFiltroRelatorioFinanceiroVO(getFacadeFactory().getControleRemessaFacade().consultarFiltrosRelatorioPorChavePrimaria(obj.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            setControleRemessaVO(obj);
            getControleRemessaVO().setNovoObj(Boolean.FALSE);
            contabilizarContas();
            contabilizarContasExclusao();
            contabilizarContasAlteracao();            
            getControleRemessaVO().setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getControleRemessaVO().getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioLogado()));
            try {
            	getFacadeFactory().getLayoutPadraoFacade().consultarPadraoFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), "ControleRemessa"+getControleRemessaVO().getCodigo(), getUsuarioLogado());            	
            }catch (Exception e) {				
			}
            setAtivarPush(getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.EM_PROCESSAMENTO));
            return Uteis.getCaminhoRedirecionamentoNavegacao("controleRemessaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return "";
        }
    }

    public String gravar() {
        try {
            // getFacadeFactory().getControleRemessaFacade().incluir(getControleRemessaVO());
            setMensagemID("msg_dados_gravados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("controleRemessaForm.xhtml");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("controleRemessaForm.xhtml");
        }
    }

    public void gerarArquivo() {
        try {
//            if (getContaReceberVOs().isEmpty()) {
//                throw new Exception("Informe as contas a serem inseridas no arquivo de remessa!");
//            }
        	getListaEstornos().clear();
            for (ControleRemessaContaReceberVO obj : getListaDadosRemessaVOs()) {
                if (obj.getApresentarArquivoRemessa()) {
                    getListaEstornos().add(obj);
                }
            }
            String caminhoPasta = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoFixo();
            UnidadeEnsinoVO unidPadraoGeracaoRemessa = new UnidadeEnsinoVO();
            if (getControleRemessaVO().getContaCorrenteVO().getUnidadeEnsinoContaCorrenteVOs().isEmpty()) {
            	throw new Exception("Não é possível gerar a remessa da conta corrente informada! No cadastro da conta corrente deve existir pelo menos uma unidade de ensino marcada como UTILIZAR CONTROLE REMESSA.");
            } else {            	
            	for (UnidadeEnsinoContaCorrenteVO uni : getControleRemessaVO().getContaCorrenteVO().getUnidadeEnsinoContaCorrenteVOs() ){
            		if (uni.getUtilizarRemessa().booleanValue()) {
            			unidPadraoGeracaoRemessa.setCodigo(uni.getUnidadeEnsino().getCodigo());
            		}
            	}
            }
            if(!Uteis.isAtributoPreenchido(unidPadraoGeracaoRemessa)){
            	throw new Exception("Não é possível gerar a remessa da conta corrente informada! No cadastro da conta corrente deve existir pelo menos uma unidade de ensino marcada como UTILIZAR CONTROLE REMESSA.");
            }
            getFacadeFactory().getControleRemessaFacade().executarGeracaoArquivoRemessa(getListaEstornos(), getControleRemessaVO(), unidPadraoGeracaoRemessa, caminhoPasta,
                    getFiltroRelatorioFinanceiroVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(getControleRemessaVO().getUnidadeEnsinoVO().getCodigo()), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
            
            for (OperacaoFuncionalidadeVO operacaoFuncionalidadeVO : getOperacaoFuncionalidadeVOs()) {
				getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(operacaoFuncionalidadeVO);
			}
            try {
            	getFacadeFactory().getLayoutPadraoFacade().persistirFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), "ControleRemessa"+getControleRemessaVO().getCodigo(), getUsuarioLogado());
            }catch (Exception e) {				
			}
            setMensagemID("msg_arquivoGerado");
        } catch (Exception e) {
        	getControleRemessaVO().setNovoObj(Boolean.TRUE);
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void enviarRemessaArquivoOnline() {
    	try {
    		getListaEstornos().clear();
			setAtivarPush(true);
    		for (ControleRemessaContaReceberVO obj : getListaDadosRemessaVOs()) {
    			if (obj.getApresentarArquivoRemessa()) {
    				getListaEstornos().add(obj);
    			}
    		}
    		String caminhoPasta = getConfiguracaoGeralPadraoSistema().getLocalUploadArquivoTemp();
    		UnidadeEnsinoVO unidPadraoGeracaoRemessa = new UnidadeEnsinoVO();
    		if (getControleRemessaVO().getContaCorrenteVO().getUnidadeEnsinoContaCorrenteVOs().isEmpty()) {
    			throw new Exception("Não é possível gerar a remessa da conta corrente informada! No cadastro da conta corrente deve existir pelo menos uma unidade de ensino marcada como UTILIZAR CONTROLE REMESSA.");
    		} else {            	
    			for (UnidadeEnsinoContaCorrenteVO uni : getControleRemessaVO().getContaCorrenteVO().getUnidadeEnsinoContaCorrenteVOs() ){
    				if (uni.getUtilizarRemessa().booleanValue()) {
    					unidPadraoGeracaoRemessa.setCodigo(uni.getUnidadeEnsino().getCodigo());
    				}
    			}
    		}
    		if(!Uteis.isAtributoPreenchido(unidPadraoGeracaoRemessa)){
    			throw new Exception("Não é possível gerar a remessa da conta corrente informada! No cadastro da conta corrente deve existir pelo menos uma unidade de ensino marcada como UTILIZAR CONTROLE REMESSA.");
    		}
    		getFacadeFactory().getControleRemessaFacade().executarEnvioRemessaOnline(getListaEstornos(), getControleRemessaVO(), unidPadraoGeracaoRemessa, caminhoPasta,
    				getFiltroRelatorioFinanceiroVO(), getConfiguracaoFinanceiroPadraoSistema(), getConfiguracaoGeralPadraoSistema(), getUsuarioLogado());
    		
    		for (OperacaoFuncionalidadeVO operacaoFuncionalidadeVO : getOperacaoFuncionalidadeVOs()) {
    			getFacadeFactory().getOperacaoFuncionalidadeFacade().incluir(operacaoFuncionalidadeVO);
    		}
    		try {
    			getFacadeFactory().getLayoutPadraoFacade().persistirFiltroTipoOrigemContaReceber(getFiltroRelatorioFinanceiroVO(), "ControleRemessa"+getControleRemessaVO().getCodigo(), getUsuarioLogado());
    		}catch (Exception e) {				
    		}
    		setAtivarPush(false);
    		setMensagemID("msg_arquivoGerado");
    	} catch (Exception e) {
    		setAtivarPush(false);
    		getControleRemessaVO().setNovoObj(Boolean.TRUE);
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    }

    public void visualizarRemessa() {
        try {
            getListaDadosRemessaVOs().clear();
            if(!Uteis.isAtributoPreenchido(controleRemessaVO.getBancoVO().getCodigo())){
            	throw new Exception("O campo BANCO deve ser informado.");
            }
            if(!Uteis.isAtributoPreenchido(controleRemessaVO.getContaCorrenteVO().getCodigo())){
            	throw new Exception("O campo CONTA CORRENTE deve ser informado.");
            }
            getControleRemessaVO().setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(controleRemessaVO.getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
            // alterado para obter a unidadeensino da conta corrente e não a unidade informada na tela, evitando assim erros de geração de arquivo remessa.
            UnidadeEnsinoVO unid = new UnidadeEnsinoVO();
            if (getControleRemessaVO().getContaCorrenteVO().getUnidadeEnsinoContaCorrenteVOs().isEmpty()) {
            	throw new Exception("Não é possível gerar a remessa da conta corrente informada! No cadastro da conta corrente deve existir pelo menos uma unidade de ensino marcada como UTILIZAR CONTROLE REMESSA.");
            } else {
            	for (UnidadeEnsinoContaCorrenteVO uni : getControleRemessaVO().getContaCorrenteVO().getUnidadeEnsinoContaCorrenteVOs() ){
            		if (uni.getUtilizarRemessa().booleanValue()) {
            			unid.setCodigo(uni.getUnidadeEnsino().getCodigo());
            		}
            	}
            }
            if (unid.getCodigo().intValue() == 0) {
            	throw new Exception("Não é possível gerar a remessa da conta corrente informada! No cadastro da conta corrente deve existir pelo menos uma unidade de ensino marcada como UTILIZAR CONTROLE REMESSA.");
            } else {
            	if (!getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unid.getCodigo()).getUtilizarIntegracaoFinanceira().booleanValue()) {
                	long difDias = Uteis.nrDiasEntreDatas(getControleRemessaVO().getDataFim(), getControleRemessaVO().getDataInicio());
                	 
    	            if(controleRemessaVO.getContaCorrenteVO().getQtdDiasFiltroRemessa() < difDias) {
    	            	throw new Exception("Não é possível gerar a remessa da conta corrente informada! O período informado para geração deve ser inferior a " + controleRemessaVO.getContaCorrenteVO().getQtdDiasFiltroRemessa() + " dias");
    	            }            
                } else {
                	getControleRemessaVO().setDataFim(Uteis.obterDataAvancadaPorMes(getControleRemessaVO().getDataFim(), 6));
                }
            	if (getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unid.getCodigo()).getUtilizarIntegracaoFinanceira().booleanValue()) {
                    if(getFacadeFactory().getIntegracaoFinanceiroFacade().realizarVerificacaoProcessamentoIntegracaoFinanceira()){
                 	   throw new Exception("Prezado, a geração de remessa dos boletos está indisponível temporariamente, tente mais tarde.");
             	    }
					setListaDadosRemessaVOs(getFacadeFactory().getContaReceberFacade().consultaRapidaContasArquivoRemessaEntreDatasUtilizandoIntegracaoFinanceira(getControleRemessaVO().getDataInicio(), getControleRemessaVO().getDataFim(), getControleRemessaVO().getUnidadeEnsinoVO().getCodigo(), getControleRemessaVO(), getFiltroRelatorioFinanceiroVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unid.getCodigo()), getUsuarioLogado()));
            	} else {
            		setListaDadosRemessaVOs(getFacadeFactory().getContaReceberFacade().consultaRapidaContasArquivoRemessaEntreDatas(getControleRemessaVO().getDataInicio(), getControleRemessaVO().getDataFim(), getControleRemessaVO().getUnidadeEnsinoVO().getCodigo(), getControleRemessaVO(), getFiltroRelatorioFinanceiroVO(), getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(unid.getCodigo()), getUsuarioLogado()));
            		
            	}
            }
            if (getControleRemessaVO().getContaCorrenteVO().getUtilizaDadosInformadosCCparaGeracaoBoleto()) {
            	unid.setCNPJ(getControleRemessaVO().getContaCorrenteVO().getCNPJ());
            	unid.setRazaoSocial(getControleRemessaVO().getContaCorrenteVO().getRazaoSocial());
            	unid.setCEP(getControleRemessaVO().getContaCorrenteVO().getCEP());
            	unid.setEndereco(getControleRemessaVO().getContaCorrenteVO().getEndereco());
            	unid.setSetor(getControleRemessaVO().getContaCorrenteVO().getSetor());
            	unid.setComplemento(getControleRemessaVO().getContaCorrenteVO().getComplemento());
            	unid.setCidade(getControleRemessaVO().getContaCorrenteVO().getCidade());
//            	getControleRemessaVO().setUnidadeEnsinoVO(unid);
            }            		
            processarListaErro();            
            contabilizarContas();
            contabilizarContasExclusao();
            contabilizarContasAlteracao();                        
            if (getListaDadosRemessaVOs().isEmpty() && getListaDadosRemessaErroVOs().isEmpty()) {
            	setMensagemID("msg_dados_nenhum_registro");
            } else {
            	setMensagemID("msg_dados_consultados");
            }            
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

	public void contabilizarContas() {
		DoubleSummaryStatistics dss = getListaDadosRemessaVOs().stream()
				.filter(d -> d.getApresentarArquivoRemessa() && !d.getContaRemetidaComAlteracao() && d.getSituacaoConta().equals("AR"))
				.mapToDouble(ControleRemessaContaReceberVO::getValorComAcrescimo).summaryStatistics();
		Long quantidadeRegistros = dss.getCount();
		setQtdRegistros(quantidadeRegistros.intValue());
		setVlrTotalRegistros(dss.getSum());
	}
	
	public void contabilizarContasExclusao() {
		DoubleSummaryStatistics dss = getListaDadosRemessaVOs().stream()
				.filter(d -> d.getApresentarArquivoRemessa() && !d.getContaRemetidaComAlteracao() && !d.getSituacaoConta().equals("AR"))
				.mapToDouble(ControleRemessaContaReceberVO::getValorComAcrescimo).summaryStatistics();
		Long quantidadeRegistros = dss.getCount();
		setQtdRegistrosExclusao(quantidadeRegistros.intValue());
		setVlrTotalRegistrosExclusao(dss.getSum());
	}

	public void contabilizarContasAlteracao() {
		DoubleSummaryStatistics dss = getListaDadosRemessaVOs().stream()
				.filter(d -> d.getApresentarArquivoRemessa() && d.getContaRemetidaComAlteracao())
				.mapToDouble(ControleRemessaContaReceberVO::getValorComAcrescimo).summaryStatistics();
		Long quantidadeRegistros = dss.getCount();
		setQtdRegistrosAlteracao(quantidadeRegistros.intValue());
		setVlrTotalRegistrosAlteracao(dss.getSum());
	}

	public void realizarAtualizacaoProcessamento() {
		try {
//			if (getAtivarPush()) {
//				getFacadeFactory().getControleCobrancaFacade().realizarAtualizacaoDadosProcessamento(getControleCobrancaBancoVO());
//				if (getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.AGUARDANDO_PROCESSAMENTO) || getControleCobrancaBancoVO().getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.ERRO_PROCESSAMENTO) || getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.PROCESSAMENTO_INTERROMPIDO)) {
//					executarConsultaAutomatica();
//					if (getJobBaixarContasArquivoRetorno() != null) {
//						setMensagemDetalhada("", getJobBaixarContasArquivoRetorno().getSituacao());
//					}
//					if (getControleCobrancaBancoVO().getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.ERRO_PROCESSAMENTO) || getControleCobrancaBancoVO().getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.PROCESSAMENTO_INTERROMPIDO)) {
//						setMensagemDetalhada("msg_erro", getControleCobrancaBancoVO().getMotivoErroProcessamento(), true);
//						setAtivarPush(false);
//					}
//				}
//				if (getControleCobrancaBancoVO().getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.PROCESSAMENTO_CONCLUIDO)) {
//					setAtivarPush(false);
//					executarConsultaAutomatica();					
//				}
//			}
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), true);
			setAtivarPush(false);
		}
	}

    public void preencherTodasContas() {
        for (ControleRemessaContaReceberVO dadosRemessaVO : getListaDadosRemessaVOs()) {
        	if (!dadosRemessaVO.getContaRemetidaComAlteracao()) {
        		dadosRemessaVO.setApresentarArquivoRemessa(Boolean.TRUE);
        	}
        }
    	contabilizarContas();
    }

    public void desmarcarTodasContas() {
        for (ControleRemessaContaReceberVO dadosRemessaVO : getListaDadosRemessaVOs()) {
        	if (!dadosRemessaVO.getContaRemetidaComAlteracao()) {
        		dadosRemessaVO.setApresentarArquivoRemessa(Boolean.FALSE);
        	}
        }
    	contabilizarContas();
    }
    
    public void preencherTodasContasAlteracao() {
    	for (ControleRemessaContaReceberVO dadosRemessaVO : getListaDadosRemessaVOs()) {
    		if (dadosRemessaVO.getContaRemetidaComAlteracao()) {
    			dadosRemessaVO.setApresentarArquivoRemessa(Boolean.TRUE);
    		}
    	}
    	contabilizarContasAlteracao();
    }
    
    public void desmarcarTodasContasAlteracao() {
    	for (ControleRemessaContaReceberVO dadosRemessaVO : getListaDadosRemessaVOs()) {
    		if (dadosRemessaVO.getContaRemetidaComAlteracao()) {
    			dadosRemessaVO.setApresentarArquivoRemessa(Boolean.FALSE);
    		}
    	}
    	contabilizarContasAlteracao();
    }

    public void estornarContaReceber() {
        try {
        	if (getControleRemessaContaReceberEstorno().getMotivoEstorno().equals("")) {
        		throw new Exception("Informe o motivo do estorno!");
        	}
        	if (!getEstornoContaUnica()) {
		    	for (ControleRemessaContaReceberVO obj : getListaDadosRemessaVOs()) {
		    		if (!obj.getSituacaoControleRemessaContaReceber().getValor().equals(SituacaoControleRemessaContaReceberEnum.ESTORNADO.getValor())
		    				&& !obj.getSituacaoControleRemessaContaReceber().getValor().equals(SituacaoControleRemessaContaReceberEnum.ESTORNADO_PELO_USUARIO.getValor())) {
			    		obj.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.ESTORNADO_PELO_USUARIO);
			    		obj.setUsuarioEstorno(getUsuarioLogadoClone());
			    		obj.setDataEstorno(new Date());						
			    		obj.setMotivoEstorno(getControleRemessaContaReceberEstorno().getMotivoEstorno());
			    		getFacadeFactory().getContaReceberFacade().alterarDataArquivoRemessaContasRejeitadas(obj.getContaReceber(), getUsuarioLogado());
			            getFacadeFactory().getControleRemessaContaReceberFacade().realizarEstorno(obj, getUsuarioLogado());
		    		}
		        }
		    	getControleRemessaVO().setSituacaoControleRemessa(SituacaoControleRemessaEnum.ESTORNADO);
		    	getFacadeFactory().getControleRemessaFacade().realizarVerificacaoUltimaRemessaCriadaAtualizandoIncrementalMXPorControleRemessa(getControleRemessaVO(), getUsuarioLogado());
	            getFacadeFactory().getControleRemessaFacade().realizarEstorno(getControleRemessaVO(), getUsuarioLogado());
            } else {
	    		if (!getControleRemessaContaReceberEstorno().getSituacaoControleRemessaContaReceber().getValor().equals(SituacaoControleRemessaContaReceberEnum.ESTORNADO.getValor())
	    				&& !getControleRemessaContaReceberEstorno().getSituacaoControleRemessaContaReceber().getValor().equals(SituacaoControleRemessaContaReceberEnum.ESTORNADO_PELO_USUARIO.getValor())) {
	            	getControleRemessaContaReceberEstorno().setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.ESTORNADO);
		            getFacadeFactory().getContaReceberFacade().alterarDataArquivoRemessaContasRejeitadas(getControleRemessaContaReceberEstorno().getContaReceber(), getUsuarioLogado());
		            getFacadeFactory().getControleRemessaContaReceberFacade().realizarEstorno(getControleRemessaContaReceberEstorno(), getUsuarioLogado());
	    		}
            }
        	setListaDadosRemessaVOs(getFacadeFactory().getControleRemessaContaReceberFacade().consultaRapidaContasArquivoRemessaPorCodigoControleRemessa(getControleRemessaVO(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));			
            setMensagemID("msg_estornoRealizado");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public void verificarEstorno() {
        try {
        	setMotivoEstorno("");
        	setEstornoContaUnica(Boolean.TRUE);
            ControleRemessaContaReceberVO obj = (ControleRemessaContaReceberVO) context().getExternalContext().getRequestMap().get("contaEstornarItens");
            if (!obj.getSituacaoControleRemessaContaReceber().getValor().equals(SituacaoControleRemessaContaReceberEnum.ESTORNADO.getValor())) {
	            obj.setUsuarioEstorno(getUsuarioLogadoClone());
	            obj.setDataEstorno(new Date());
            }
            //Inicialmente Feito a validação do motivo Erro da remessa somente Banco para o Bradesco;
            if (Uteis.isAtributoPreenchido(getControleRemessaVO().getBancoVO().getNrBanco()) && getControleRemessaVO().getBancoVO().getNrBanco().contains(BancoEnum.BRADESCO_CNAB240.getNrBanco())) {
            	consultarMotivoEstorno(obj);           
			}
            setControleRemessaContaReceberEstorno(obj);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }
    
    public void estornarTodasContas() {
        try {
        	setMotivoEstorno("");
        	getControleRemessaContaReceberEstorno().setUsuarioEstorno(getUsuarioLogadoClone());
        	getControleRemessaContaReceberEstorno().setDataEstorno(new Date());
        	getControleRemessaContaReceberEstorno().setMotivoEstorno("");
        	getControleRemessaContaReceberEstorno().setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.AGUARDANDO_PROCESSAMENTO);
        	setEstornoContaUnica(Boolean.FALSE);
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

	public void enviarEmailAlunos() {
		ComunicacaoInternaVO comunicacaoInternaVO = null;
		try {
//			if (getComunicacaoEnviar().getMensagemSMS().length() > 150 && getEnviarSMS()) {
//				throw new Exception(getMensagemInternalizacao("msg_LimiteCampoTextoSms"));
//			}
//			if (getComunicacaoEnviar().getMensagemSMS().isEmpty() && getEnviarSMS()) {
//				throw new Exception(getMensagemInternalizacao("msg_EnviarSmsTextoVazio"));
//			}
			String corpoMensagem = getComunicacaoEnviar().getMensagem();
//			String corpoMensagemSMS = getComunicacaoEnviar().getMensagemSMS();
			List<ComunicadoInternoDestinatarioVO> listaComunicadoInternoDestinatarioVO = new ArrayList<ComunicadoInternoDestinatarioVO>(0);
			for (ControleRemessaContaReceberVO obj : getListaObjetos()) {
				if (!obj.getMensagemEnviada()) {
					comunicacaoInternaVO = getComunicacaoEnviar();
					comunicacaoInternaVO.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.FINANCEIRO_COBRANCA);
					comunicacaoInternaVO.getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());
					comunicacaoInternaVO.getComunicadoInternoDestinatarioVOs().clear();
					ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
					comunicadoInternoDestinatarioVO.setTipoComunicadoInterno("LE");
					PessoaVO pessoa = new PessoaVO();
					pessoa.setEmail(obj.getEmailSacado());
					pessoa.setNome(obj.getNomeSacado());
					comunicadoInternoDestinatarioVO.setDestinatario(pessoa);
					comunicacaoInternaVO.setAluno(pessoa);
					if (!obj.getEmailSacado().equals("")) {
						comunicadoInternoDestinatarioVO.setEmail(obj.getEmailSacado());
						comunicadoInternoDestinatarioVO.setNome(obj.getNomeSacado());
						comunicadoInternoDestinatarioVO.getDestinatario().setEmail(obj.getEmailSacado());
					} 
					//comunicadoInternoDestinatarioVO.getDestinatario().setCelular(obj.get());				
					if (obj.getEmailSacado().equals("")) {
						continue;
					}
					
					comunicadoInternoDestinatarioVO.getDestinatario().setNome(obj.getNomeSacado());
					listaComunicadoInternoDestinatarioVO.add(comunicadoInternoDestinatarioVO);
					comunicacaoInternaVO.setComunicadoInternoDestinatarioVOs(listaComunicadoInternoDestinatarioVO);
					comunicacaoInternaVO.setTipoRemetente("FU");
					comunicacaoInternaVO.setTipoDestinatario("AL");
					comunicacaoInternaVO.setTipoComunicadoInterno("LE");			
					comunicacaoInternaVO.setResponsavel(getUsuarioLogado().getPessoa());
					PessoaVO responsavel = getConfiguracaoGeralPadraoSistema().getResponsavelPadraoComunicadoInterno();
					comunicacaoInternaVO.setResponsavel(responsavel);
					comunicacaoInternaVO.setMensagem(obterMensagemFormatada(obj, corpoMensagem));
					//comunicacaoInternaVO.setMensagem(corpoMensagem);
	//				comunicacaoInternaVO.setEnviarSMS(getEnviarSMS());
	//				corpoMensagemSMS = obterMensagemSmsFormatadaMensagemCobrancaAlunoInadimplente(obj, corpoMensagemSMS);
	//				String msgSMS = corpoMensagemSMS;
	//				if (msgSMS.length() > 150) {
	//					msgSMS = msgSMS.substring(0, 150);
	//				}

					getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, false, getUsuarioLogado(), getConfiguracaoGeralPadraoSistema(),null);

					ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
					PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ALTERACAO_BOLETO_REMESSA, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUnidadeEnsino().getCodigo(), getUsuarioLogado(), null);
					if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
						comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
						comunicacaoEnviar.setMensagem(mensagemTemplate.getMensagem());
					}
					setEmail(getEmail().replaceAll("imagens/email/cima_sei.jpg", "../imagens/email/cima_sei.jpg"));
					setEmail(getEmail().replaceAll("imagens/email/baixo_sei.jpg", "../imagens/email/baixo_sei.jpg"));
					setComunicacaoEnviar(comunicacaoEnviar);
					obj.setMensagemEnviada(Boolean.TRUE);
				}
			}
			setMensagemID("msg_msg_emailsEnviados");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage());
		}
	}
	
	public String getShowHideModalEnviarEmail() {
		setQuantidadeAlunoMensagem(0);
    	for (ControleRemessaContaReceberVO dadosRemessaVO : getListaDadosRemessaVOs()) {    		
    		if (dadosRemessaVO.getApresentarArquivoRemessa().booleanValue()) {
    			if (dadosRemessaVO.getContaRemetidaComAlteracao() && !dadosRemessaVO.getMensagemEnviada()) {
    				getListaObjetos().add(dadosRemessaVO);
    				setQuantidadeAlunoMensagem(getQuantidadeAlunoMensagem() + 1);
    			}
    		}
    	}
		ComunicacaoInternaVO comunicacaoEnviar = inicializarDadosPadrao(new ComunicacaoInternaVO());
		try {
			PersonalizacaoMensagemAutomaticaVO mensagemTemplate = getFacadeFactory().getPersonalizacaoMensagemAutomaticaFacade().consultarPorNomeTemplate(TemplateMensagemAutomaticaEnum.MENSAGEM_ALTERACAO_BOLETO_REMESSA, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getControleRemessaVO().getUnidadeEnsinoVO().getCodigo(), getUsuarioLogado(), null);
			if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemAutomatica()) {
				comunicacaoEnviar.setAssunto(mensagemTemplate.getAssunto());
				comunicacaoEnviar.setMensagem(mensagemTemplate.getMensagem());
//					if (mensagemTemplate != null && !mensagemTemplate.getDesabilitarEnvioMensagemSMSAutomatica()) {
//						comunicacaoEnviar.setMensagemSMS(mensagemTemplate.getMensagemSMS());
//						comunicacaoEnviar.setEnviarSMS(Boolean.TRUE);
//						setEnviarSMS(Boolean.TRUE);
//					} else {
//						setEnviarSMS(Boolean.FALSE);
//					}
//					setEmail(mensagemTemplate.getMensagem());
			} else {
//					setEnviarSMS(Boolean.TRUE);
//					setEmail(comunicacaoEnviar.getMensagem());
//					comunicacaoEnviar.setAssunto("Alteração Boleto");
			}
			setEmail(getEmail().replaceAll("imagens/email/cima_sei.jpg", "../imagens/email/cima_sei.jpg"));
			setEmail(getEmail().replaceAll("imagens/email/baixo_sei.jpg", "../imagens/email/baixo_sei.jpg"));
			setComunicacaoEnviar(comunicacaoEnviar);
		} catch(Exception e) {
			setComunicacaoEnviar(comunicacaoEnviar);				
		}
		return "RichFaces.$('panelMensagem').show()";
	}

	public String obterMensagemFormatada(ControleRemessaContaReceberVO obj, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(),obj.getNomeSacado());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_SACADO.name(), obj.getNomeSacado());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), obj.getUnidadeEnsino().getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.VALOR_PARCELA.name(), Uteis.getDoubleFormatado(obj.getValorBase()));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.DATA_VENCIMENTO.name(), obj.getDataVencimento_Apresentar());
		return mensagemTexto;
	}
	
	public ComunicacaoInternaVO getComunicacaoEnviar() {
		if (comunicacaoEnviar == null) {
			comunicacaoEnviar = new ComunicacaoInternaVO();
		}
		return comunicacaoEnviar;
	}

	public void setComunicacaoEnviar(ComunicacaoInternaVO comunicacaoEnviar) {
		this.comunicacaoEnviar = comunicacaoEnviar;
	}
	
	public String obterMensagemSmsFormatadaMensagemCobrancaAlunoInadimplente(InadimplenciaRelVO inadimplenciaRelVO, final String mensagemTemplate) {
		String mensagemTexto = mensagemTemplate;
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_ALUNO.name(), Uteis.getNomeResumidoPessoa(inadimplenciaRelVO.getNome()));
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_SACADO.name(), inadimplenciaRelVO.getNome());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.MATRICULA.name(), inadimplenciaRelVO.getMatricula());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.NOME_UNIDADE_ENSINO.name(), inadimplenciaRelVO.getUnidadeEnsino());
		mensagemTexto = mensagemTexto.replaceAll(TagsMensagemAutomaticaEnum.LISTA_CONTARECEBER_DETALHE.name(), Matcher.quoteReplacement(inadimplenciaRelVO.getListaParcelaNotificacaoInadimplente()));
		return mensagemTexto;
	}
	
    public void consultarContasParaControleRemessa() {
        try {
            getContaReceberVOs().clear();
            if (getUnidadeEnsino().getCodigo() == 0) {
                throw new Exception("Informe a Unidade de Ensino");
            }
            if (getControleRemessaVO().getDataFim().before(getControleRemessaVO().getDataInicio())) {
                throw new Exception("Data Fim deve ser Maior que a Data Início");
            }


            setContaReceberVOs(getFacadeFactory().getContaReceberFacade().consultarContasQueNaoForamAdicionadasAoArquivoRemessaEntreDatas(
                    getControleRemessaVO().getDataInicio(), getControleRemessaVO().getDataFim(), getUnidadeEnsino().getCodigo(),
                    Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado()));

            setMensagemID("msg_dados_consultados");
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    public String consultar() {
        try {
            super.consultar();
            List objs = new ArrayList(0);
            if (getControleConsulta().getCampoConsulta().equals("codigo")) {
                if (getControleConsulta().getValorConsulta().equals("")) {
                    getControleConsulta().setValorConsulta("0");
                }
                int valorInt = Integer.parseInt(getControleConsulta().getValorConsulta());
                objs = getFacadeFactory().getControleRemessaFacade().consultarPorCodigo(new Integer(valorInt), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
                        getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("contaCorrente")) {
            	objs = getFacadeFactory().getControleRemessaFacade().consultarPorContaCorrente(getControleConsulta().getValorConsulta(), getControleRemessaVO().getSituacaoControleRemessa(), 
            			Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(getControleConsulta().getDataFim(), 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
            			getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nossoNumero")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
            	objs = getFacadeFactory().getControleRemessaFacade().consultarPorNossoNumero(getControleConsulta().getValorConsulta(), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
            			getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("nomeSacado")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}			
            	objs = getFacadeFactory().getControleRemessaFacade().consultarPorNomeSacado(getControleConsulta().getValorConsulta(), getControleRemessaVO().getSituacaoControleRemessa(), 
            			Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(getControleConsulta().getDataFim(), 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
            			getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("cpfSacado")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
            	objs = getFacadeFactory().getControleRemessaFacade().consultarPorCPFSacado(getControleConsulta().getValorConsulta(), getControleRemessaVO().getSituacaoControleRemessa(), 
            			Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(getControleConsulta().getDataFim(), 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
            			getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("matricula")) {
				if (getControleConsulta().getValorConsulta().length() < 2) {
					throw new Exception(getMensagemInternalizacao("msg_ParametroConsulta_vazio"));
				}
            	objs = getFacadeFactory().getControleRemessaFacade().consultarPorMatricula(getControleConsulta().getValorConsulta(), getControleRemessaVO().getSituacaoControleRemessa(), 
            			Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0), Uteis.getDateTime(getControleConsulta().getDataFim(), 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS,
            			getUsuarioLogado());
            }
            if (getControleConsulta().getCampoConsulta().equals("dataGeracao")) {
                objs = getFacadeFactory().getControleRemessaFacade().consultarPorDataGeracao(Uteis.getDateTime(getControleConsulta().getDataIni(), 0, 0, 0),
                        Uteis.getDateTime(getControleConsulta().getDataFim(), 23, 59, 59), true, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado());
            }
            setListaConsulta(objs);
            setMensagemID("msg_dados_consultados");
            return Uteis.getCaminhoRedirecionamentoNavegacao("controleRemessaCons.xhtml");
        } catch (Exception e) {
            setListaConsulta(new ArrayList(0));
            setMensagemDetalhada("msg_erro", e.getMessage());
            return Uteis.getCaminhoRedirecionamentoNavegacao("controleRemessaCons.xhtml");
        }
    }

    public void montarListaSelectItemLayoutsBancos() {
        try {
            List listaBancoVOs = getFacadeFactory().getBancoFacade().consultarPorBancoNivelComboBox(false, getUsuarioLogado());
            List objs = new ArrayList(0);
            objs.add(new SelectItem(0, ""));
            Iterator j = listaBancoVOs.iterator();
            while (j.hasNext()) {
                BancoVO item = (BancoVO) j.next();
                BancoEnum bancos = BancoEnum.getEnum("CNAB240", item.getNrBanco());
                if(bancos != null && bancos.getPossuiRemessa()) {
                	objs.add(new SelectItem(item.getCodigo(), item.getNome()));
                }else {
                	bancos = BancoEnum.getEnum("CNAB400", item.getNrBanco());
                	if(bancos != null && bancos.getPossuiRemessa()) {
                		objs.add(new SelectItem(item.getCodigo(), item.getNome()));
                	}
                }
            }
            SelectItemOrdemValor ordenador = new SelectItemOrdemValor();
            Collections.sort((List) objs, ordenador);
            setListaSelectItemBanco(objs);
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }

    }

	public void montarListaSelectItemSituacao() {
		List obj = UtilSelectItem.getListaSelectItemEnum(SituacaoControleRemessaEnum.values(), Obrigatorio.SIM);		
		setListaSelectItemSituacao(obj);
	}

    public List getTipoConsultaCombo() {
        List itens = new ArrayList(0);
        itens.add(new SelectItem("contaCorrente", "Conta Corrente"));
        //itens.add(new SelectItem("dataGeracao", "Data da Geração"));
        itens.add(new SelectItem("nomeSacado", "Nome Sacado"));        
        itens.add(new SelectItem("cpfSacado", "CPF Sacado"));        
        itens.add(new SelectItem("nossoNumero", "Nosso Número"));
        itens.add(new SelectItem("matricula", "Matrícula"));
        itens.add(new SelectItem("codigo", "Código Remessa"));
        return itens;
    }

    public boolean isCampoData() {
        if (!getControleConsulta().getCampoConsulta().equals("nossoNumero") && !getControleConsulta().getCampoConsulta().equals("codigo")) {
            return true;
        }
        return false;
    }   

    public boolean isCampoCPF() {
    	if (getControleConsulta().getCampoConsulta().equals("cpfSacado")) {
    		return true;
    	}
    	return false;
    }   
    
    public boolean isCampoNossoNumero() {
    	if (getControleConsulta().getCampoConsulta().equals("nossoNumero")) {
    		return true;
    	}
    	return false;
    }   
    
    public void montarListaSelectItemUnidadeEnsino() {
        try {
            List<UnidadeEnsinoVO> resultadoConsulta = consultarUnidadeEnsinoPorNome("");
            setListaSelectItemUnidadeEnsino(UtilSelectItem.getListaSelectItem(resultadoConsulta, "codigo", "nome"));
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
    }

    private List<UnidadeEnsinoVO> consultarUnidadeEnsinoPorNome(String nomePrm) throws Exception {
        List<UnidadeEnsinoVO> lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm,super.getUnidadeEnsinoLogado().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, getUsuarioLogado());
        return lista;
    }

    public void selecionarContaCorrente() {
    	getListaDadosRemessaErroVOs().clear();
    	getListaDadosRemessaVOs().clear();
    	if(Uteis.isAtributoPreenchido(getControleRemessaVO().getContaCorrenteVO())) {
    		try {
				getControleRemessaVO().setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(getControleRemessaVO().getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, getUsuarioLogado()));
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }
    
	public void montarListaSelectItemContaCorrente() {
		try {
        	getListaDadosRemessaErroVOs().clear();
        	getListaDadosRemessaVOs().clear();
            if (getControleRemessaVO().getBancoVO().getCodigo() != 0) {
            	getControleRemessaVO().setBancoVO(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(getControleRemessaVO().getBancoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
            	List<ContaCorrenteVO> listaContaCorrenteVOs = getFacadeFactory().getContaCorrenteFacade().consultaRapidaPorBancoControleRemessaNivelComboBox(getControleRemessaVO().getBancoVO().getCodigo(), getControleRemessaVO().getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado());
                List<SelectItem> objs = new ArrayList<SelectItem>(0);
                if (getControleRemessaVO().getBancoVO().getNrBanco().equals("707")) {
                	listaContaCorrenteVOs.addAll(getFacadeFactory().getContaCorrenteFacade().consultaRapidaPorBancoControleRemessaNivelComboBox("237", getControleRemessaVO().getUnidadeEnsinoVO().getCodigo(), false, getUsuarioLogado()));
                }
                objs.add(new SelectItem(0, ""));
				listaContaCorrenteVOs.forEach(cc -> {
					Optional.ofNullable(BancoEnum.getEnum(cc.getCnab(), cc.getAgencia().getBanco().getNrBanco())).ifPresent(b -> {
						if (!cc.getContaCaixa() && b.getPossuiRemessa()) {
							objs.add(new SelectItem(cc.getCodigo(), Uteis.isAtributoPreenchido(cc.getNomeApresentacaoSistema()) ? 
								cc.getNomeApresentacaoSistema() : cc.getNumero() + " - " + cc.getCarteira()));
						}
					});
				});
				objs.sort(Comparator.comparing(SelectItem::getLabel));
                setListaSelectItemContaCorrente(objs);
            }
            limparMensagem();
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }

    }

    public Boolean getApresentaContaCorrente() {
        return getControleRemessaVO().getBancoVO().getCodigo() != 0;
    }

    public String getMascaraConsulta() {
        return "";
    }

    public String inicializarConsultar() {
        removerObjetoMemoria(this);
        novo();
        setControleConsulta(new ControleConsulta());
        getControleConsulta().setCampoConsulta("contaCorrente");
        getControleRemessaVO().setDataInicio(new Date());
        getControleRemessaVO().setDataFim(Uteis.obterDataFutura(new Date(), 30));        
        setListaConsulta(new ArrayList(0));
        setMensagemID("msg_entre_prmconsulta");
        return Uteis.getCaminhoRedirecionamentoNavegacao("controleRemessaCons.xhtml");
    }

    public Boolean getApresentarDataTableContas() {
        if (getContaReceberVOs().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

	public void uploadArquivo(FileUploadEvent upload) {
		try {
			getControleRemessaVO().setArquivo(upload.getUploadedFile().getData());
			getControleRemessaVO().setNomeArquivo(Uteis.getNomeArquivo(upload.getUploadedFile().getName()));
		} catch (Exception e) {
			setMensagemID("");
			setMensagem("");
			setMensagemDetalhada(e.getMessage());
		}
	}
	
	public String processarArquivo() {
		try {
			getFacadeFactory().getControleRemessaFacade().processarArquivo(getControleRemessaVO(), getListaDadosRemessaVOs(), getCaminhoPastaArquivosCobranca(), getConfiguracaoFinanceiroPadraoSistema(), getUsuarioLogado());
			setMensagemID("msg_ProcessamentoRealizado");
			return Uteis.getCaminhoRedirecionamentoNavegacao("controleRemessaForm.xhtml");
	    } catch (Exception e) {
	        setMensagemDetalhada("msg_erro", e.getMessage());
	        return Uteis.getCaminhoRedirecionamentoNavegacao("controleRemessaForm.xhtml");
	    }		
	}
	
    public void removerContaLista() {
        ContaReceberVO obj = (ContaReceberVO) context().getExternalContext().getRequestMap().get("contaReceber");
        try {
            getContaReceberVOs().remove(obj);
        } finally {
            obj = null;
        }
    }

    public void selecionarArquivo() {
        ControleRemessaVO controleRemessa = (ControleRemessaVO) context().getExternalContext().getRequestMap().get("controleRemessaItens");
        setControleRemessaVO(controleRemessa);
    }
    
    public String realizarDownloadArquivo(){
    	try {
    		HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
    		controleRemessaVO.getArquivoRemessa().setPastaBaseArquivo(getFacadeFactory().getArquivoFacade().executarDefinicaoUrlFisicoAcessoArquivo(controleRemessaVO.getArquivoRemessa(), PastaBaseArquivoEnum.REMESSA, getConfiguracaoGeralPadraoSistema()).replace(controleRemessaVO.getArquivoRemessa().getNome(), ""));
    		request.getSession().setAttribute("arquivoVO",  controleRemessaVO.getArquivoRemessa());
			context().getExternalContext().dispatch("/DownloadSV");
			FacesContext.getCurrentInstance().responseComplete();
    	} catch (IOException e) {
    		setMensagemDetalhada("msg_erro", e.getMessage());
    	}
    	return "";
    }

    public String getCaminhoServidorDownloadArquivo() {
        try {
            return "location.href='" + getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(controleRemessaVO.getArquivoRemessa(), PastaBaseArquivoEnum.REMESSA, getConfiguracaoGeralPadraoSistema()) + "'";
        } catch (Exception ex) {
            setMensagemDetalhada("msg_erro", ex.getMessage());
        }
        return "";
    }

    public String getDownloadArquivoTelaForm() {
        try {
            HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
//            request.setAttribute("codigoArquivo", getControleRemessaVO().getArquivoRemessa().getCodigo());
            //request.setAttribute("urlAcessoArquivo", getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getControleRemessaVO().getArquivoRemessa(), PastaBaseArquivoEnum.REMESSA, getConfiguracaoGeralPadraoSistema(), request.getRemoteAddr()));
            context().getExternalContext().dispatch("/DownloadSV");
            FacesContext.getCurrentInstance().responseComplete();
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return "";
    }

    public String getDownloadArquivoTelaCons() {
        try {
            selecionarArquivo();
            HttpServletRequest request = (HttpServletRequest) context().getExternalContext().getRequest();
//            request.setAttribute("codigoArquivo", getControleRemessaVO().getArquivoRemessa().getCodigo());
            //request.setAttribute("urlAcessoArquivo", getFacadeFactory().getArquivoFacade().executarDefinicaoUrlAcessoArquivo(getControleRemessaVO().getArquivoRemessa(), PastaBaseArquivoEnum.REMESSA, getConfiguracaoGeralPadraoSistema(), request.getRemoteAddr()));
            context().getExternalContext().dispatch("/DownloadSV");
            FacesContext.getCurrentInstance().responseComplete();
            return "";
        } catch (Exception e) {
            setMensagemDetalhada("msg_erro", e.getMessage());
        }
        return "";
    }

    public boolean isApresentarBotaoRetorno() {
    	if (getControleRemessaVO().getSituacaoControleRemessa().equals(SituacaoControleRemessaEnum.AGUARDANDO_PROCESSAMENTO_RETORNO_REMESSA)
    			&& getControleRemessaVO().getCodigo().intValue() > 0 && !getControleRemessaVO().getRemessaOnline()) {
    		BancoEnum banco = BancoEnum.getEnum(this.getControleRemessaVO().getContaCorrenteVO().getCnab(), this.getControleRemessaVO().getContaCorrenteVO().getAgencia().getBanco().getNrBanco());
    		if (banco != null && banco.getPossuiRetornoRemessa()) {
    			return true;	
    		} else {
    			return false;
    		}
    		
    	} else {
    		return false;
    	}
    }
        
    public boolean isApresentarBotaoDownload() {
        return !getControleRemessaVO().isNovoObj() && !getControleRemessaVO().getRemessaOnline();
    }

    public void setContaReceberVOs(List<ContaReceberVO> contaReceberVOs) {
        this.contaReceberVOs = contaReceberVOs;
    }

    public List<ContaReceberVO> getContaReceberVOs() {
        if (contaReceberVOs == null) {
            contaReceberVOs = new ArrayList<ContaReceberVO>(0);
        }
        return contaReceberVOs;
    }

    public void setControleRemessaVO(ControleRemessaVO controleRemessaVO) {
        this.controleRemessaVO = controleRemessaVO;
    }

    public ControleRemessaVO getControleRemessaVO() {
        if (controleRemessaVO == null) {
            controleRemessaVO = new ControleRemessaVO();
        }
        return controleRemessaVO;
    }

    public void setListaSelectItemUnidadeEnsino(List listaSelectItemUnidadeEnsino) {
        this.listaSelectItemUnidadeEnsino = listaSelectItemUnidadeEnsino;
    }

    public List getListaSelectItemUnidadeEnsino() {
        if (listaSelectItemUnidadeEnsino == null) {
            listaSelectItemUnidadeEnsino = new ArrayList(0);
        }
        return listaSelectItemUnidadeEnsino;
    }

    public void setUnidadeEnsino(UnidadeEnsinoVO unidadeEnsino) {
        this.unidadeEnsino = unidadeEnsino;
    }

    public UnidadeEnsinoVO getUnidadeEnsino() {
        if (unidadeEnsino == null) {
            unidadeEnsino = new UnidadeEnsinoVO();
        }
        return unidadeEnsino;
    }

    public void setArquivoTeste(Boolean arquivoTeste) {
        this.arquivoTeste = arquivoTeste;
    }

    public Boolean getArquivoTeste() {
        if (arquivoTeste == null) {
            arquivoTeste = Boolean.FALSE;
        }
        return arquivoTeste;
    }

    public List getListaSelectItemContaCorrente() {
        if (listaSelectItemContaCorrente == null) {
            listaSelectItemContaCorrente = new ArrayList(0);
        }
        return listaSelectItemContaCorrente;
    }

    public void setListaSelectItemContaCorrente(List listaSelectItemContaCorrente) {
        this.listaSelectItemContaCorrente = listaSelectItemContaCorrente;
    }

    public List getListaSelectItemBanco() {
        if (listaSelectItemBanco == null) {
            listaSelectItemBanco = new ArrayList(0);
        }
        return listaSelectItemBanco;
    }

    public void setListaSelectItemBanco(List listaSelectItemBanco) {
        this.listaSelectItemBanco = listaSelectItemBanco;
    }

    public List<ControleRemessaContaReceberVO> getListaDadosRemessaVOs() {
        if (listaDadosRemessaVOs == null) {
            listaDadosRemessaVOs = new ArrayList<ControleRemessaContaReceberVO>(0);
        }
        return listaDadosRemessaVOs;
    }

    public void setListaDadosRemessaVOs(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs) {
        this.listaDadosRemessaVOs = listaDadosRemessaVOs;
    }

    public Boolean getApresentarBotaoGerarArquivo() {
    	try {
        if ((!getListaDadosRemessaErroVOs().isEmpty() || !getListaDadosRemessaVOs().isEmpty()) && getControleRemessaVO().isNovoObj()) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    	} catch (Exception e) {
    		e.getMessage();
        return Boolean.FALSE;
    	}
    }

    public Boolean getApresentarBotaoEnviarRemessaOnline() {
    	try {
    	if ((!getListaDadosRemessaErroVOs().isEmpty() || !getListaDadosRemessaVOs().isEmpty()) && getControleRemessaVO().isNovoObj() && getControleRemessaVO().getContaCorrenteVO().getHabilitarRegistroRemessaOnline())  {
    		return Boolean.TRUE;
    	}
    	return Boolean.FALSE;
    	} catch (Exception e) {
    		e.getMessage();
    		return Boolean.FALSE;
    	}
    }
    
    public List<ControleRemessaContaReceberVO> getListaEstornos() {
        if (listaEstornos == null) {
            listaEstornos = new ArrayList<ControleRemessaContaReceberVO>(0);
        }
        return listaEstornos;
    }

    public void setListaEstornos(List<ControleRemessaContaReceberVO> listaEstornos) {
        this.listaEstornos = listaEstornos;
    }

	public Integer getQtdRegistros() {
		if (qtdRegistros == null) {
			qtdRegistros = 0;
		}
		return qtdRegistros;
	}

	public void setQtdRegistros(Integer qtdRegistros) {
		this.qtdRegistros = qtdRegistros;
	}

	public Double getVlrTotalRegistros() {
		if (vlrTotalRegistros == null) {
			vlrTotalRegistros = 0.0;
		}
		return vlrTotalRegistros;
	}

	public void setVlrTotalRegistros(Double vlrTotalRegistros) {
		this.vlrTotalRegistros = vlrTotalRegistros;
	}
	
    public List getTipoRemessaCombo() {
    	List itens = new ArrayList(0);
    	itens.add(new SelectItem("RE", "Registro de Conta"));
    	itens.add(new SelectItem("AU", "Atualização de Conta"));
    	return itens;
    }	
	
    public Boolean getApresentaTipoRemessa() {
    	if (getControleRemessaVO().getBancoVO().getCodigo().intValue() > 0) {
    		try {
    			getControleRemessaVO().setBancoVO(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(getControleRemessaVO().getBancoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioLogado()));
    			return getControleRemessaVO().getBancoVO().getNrBanco().equals("033");
    		} catch (Exception e) {
    			return false;
    		}
    	}
    	return false;
    }
    

	public List getListaSelectItemSituacao() {
		if(listaSelectItemSituacao == null){
			listaSelectItemSituacao = new ArrayList(0);
		}
		return listaSelectItemSituacao;
	}

	public void setListaSelectItemSituacao(List listaSelectItemSituacao) {
		this.listaSelectItemSituacao = listaSelectItemSituacao;
	}

	public String getSituacaoControleRemessa() {
		if (situacaoControleRemessa == null) {
			situacaoControleRemessa = "";
		}
		return situacaoControleRemessa;
	}

	public void setSituacaoControleRemessa(String situacaoControleRemessa) {
		this.situacaoControleRemessa = situacaoControleRemessa;
	}

	public String getMotivoEstorno() {
		if (motivoEstorno == null) {
			motivoEstorno = "";
		}
		return motivoEstorno;
	}

	public void setMotivoEstorno(String motivoEstorno) {
		this.motivoEstorno = motivoEstorno;
	}

	public Boolean getEstornoContaUnica() {
		if (estornoContaUnica == null) {
			estornoContaUnica = Boolean.FALSE;
		}
		return estornoContaUnica;
	}

	public void setEstornoContaUnica(Boolean estornoContaUnica) {
		this.estornoContaUnica = estornoContaUnica;
	}

	public ControleRemessaContaReceberVO getControleRemessaContaReceberEstorno() {
		if (controleRemessaContaReceberEstorno == null) {
			controleRemessaContaReceberEstorno = new ControleRemessaContaReceberVO();
		}
		return controleRemessaContaReceberEstorno;
	}

	public void setControleRemessaContaReceberEstorno(ControleRemessaContaReceberVO controleRemessaContaReceberEstorno) {
		this.controleRemessaContaReceberEstorno = controleRemessaContaReceberEstorno;
	}

	public List<ControleRemessaContaReceberVO> getListaDadosRemessaErroVOs() {
		if (listaDadosRemessaErroVOs == null) {
			listaDadosRemessaErroVOs = new ArrayList<ControleRemessaContaReceberVO>();
		}
		return listaDadosRemessaErroVOs;
	}

	public void setListaDadosRemessaErroVOs(List<ControleRemessaContaReceberVO> listaDadosRemessaErroVOs) {
		this.listaDadosRemessaErroVOs = listaDadosRemessaErroVOs;
	}

	public void processarListaErro() {
		getListaDadosRemessaErroVOs().clear();
		setQtdRegistrosErro(0);
		if (!getListaDadosRemessaVOs().isEmpty()) {
			List<ControleRemessaContaReceberVO> listaAuxilizar = getListaDadosRemessaVOs()
					.stream().filter(c -> c.getSituacaoControleRemessaContaReceber().equals(SituacaoControleRemessaContaReceberEnum.ERRO_ENVIO))
					.collect(Collectors.toList());
			setListaDadosRemessaErroVOs(listaAuxilizar);
			if (!getListaDadosRemessaErroVOs().isEmpty()) {
				setQtdRegistrosErro(getListaDadosRemessaErroVOs().size());
				List<ControleRemessaContaReceberVO> listaAuxiliar = getListaDadosRemessaVOs()
						.stream()
						.filter(c -> !c.getSituacaoControleRemessaContaReceber().equals(SituacaoControleRemessaContaReceberEnum.ERRO_ENVIO))
						.collect(Collectors.toList());
				setListaDadosRemessaVOs(listaAuxiliar);
			}
		}	        
	}

	public String getLabelRemessaErro() {
		return "Registros Remessa c/ Erro (" + getQtdRegistrosErro() + ")";
	}
	
	public Integer getQtdRegistrosErro() {
		if (qtdRegistrosErro == null) {
			qtdRegistrosErro = 0;
		}
		return qtdRegistrosErro;
	}

	public void setQtdRegistrosErro(Integer qtdRegistrosErro) {
		this.qtdRegistrosErro = qtdRegistrosErro;
	}    
	
    public FiltroRelatorioFinanceiroVO getFiltroRelatorioFinanceiroVO() {
        if (filtroRelatorioFinanceiroVO == null) {
            filtroRelatorioFinanceiroVO = new FiltroRelatorioFinanceiroVO(getLoginControle().getPermissaoAcessoMenuVO().getPermitirApenasContasDaBiblioteca());
        }
        return filtroRelatorioFinanceiroVO;
    }

    public void setFiltroRelatorioFinanceiroVO(FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO) {
        this.filtroRelatorioFinanceiroVO = filtroRelatorioFinanceiroVO;
    }
    

    
	public Integer getQtdRegistrosAlteracao() {
		if (qtdRegistrosAlteracao == null) {
			qtdRegistrosAlteracao = 0;
		}
		return qtdRegistrosAlteracao;
	}
	
	public void setQtdRegistrosAlteracao(Integer qtdRegistrosAlteracao) {
		this.qtdRegistrosAlteracao = qtdRegistrosAlteracao;
	}
	
	public Double getVlrTotalRegistrosAlteracao() {
		if (vlrTotalRegistrosAlteracao == null) {
			vlrTotalRegistrosAlteracao = 0.0;
		}
		return vlrTotalRegistrosAlteracao;
	}
	
	public void setVlrTotalRegistrosAlteracao(Double vlrTotalRegistrosAlteracao) {
		this.vlrTotalRegistrosAlteracao = vlrTotalRegistrosAlteracao;
	}

	public Boolean getEnviarSMS() {
		if (enviarSMS == null) {
			enviarSMS = Boolean.TRUE;
		}
		return enviarSMS;
	}


	public void setEnviarSMS(Boolean enviarSMS) {
		this.enviarSMS = enviarSMS;
	}
	
	public String getEmail() {
		if (email == null) {
			email = "";
		}
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getQuantidadeAlunoMensagem() {
		if (quantidadeAlunoMensagem == null) {
			quantidadeAlunoMensagem = 0;
		}
		return quantidadeAlunoMensagem;
	}

	public void setQuantidadeAlunoMensagem(Integer quantidadeAlunoMensagem) {
		this.quantidadeAlunoMensagem = quantidadeAlunoMensagem;
	}

	public List<ControleRemessaContaReceberVO> getListaObjetos() {
		if (listaObjetos == null) {
			listaObjetos = new ArrayList<ControleRemessaContaReceberVO>(0);
		}
		return listaObjetos;
	}

	public void setListaObjetos(List<ControleRemessaContaReceberVO> listaObjetos) {
		this.listaObjetos = listaObjetos;
	}

	public void inicializarDadosLiberarReajustePreco() {
		ControleRemessaContaReceberVO obj = (ControleRemessaContaReceberVO) context().getExternalContext().getRequestMap().get("contaEstornarItens");
		setControleRemessaContaReceberReajustePrecoVO(obj);
	}
	
	public void realizarVerificacaoUusuarioLiberacaoEnviarRemessaSemReajustePreco() {
		try {
			UsuarioVO usuarioVerif = ControleAcesso.verificarLoginUsuario(this.getUserNameLiberarRemessaSemReajuste(), this.getSenhaLiberarRemessaSemReajuste(), true, Uteis.NIVELMONTARDADOS_TODOS);
			verificarPermissaoUsuarioLiberarEnviarRemessaSemReajustePrecoParcelas(usuarioVerif, "PermitirEnviarRemessaSemReajustePreco");
			getOperacaoFuncionalidadeVOs().add(getFacadeFactory().getOperacaoFuncionalidadeFacade().executarGeracaoOperacaoFuncionalidade(OrigemOperacaoFuncionalidadeEnum.CONTA_RECEBER, getControleRemessaContaReceberReajustePrecoVO().getContaReceber().toString(), OperacaoFuncionalidadeEnum.REMESSA_LIBERAR_ENVIAR_REMESSA_SEM_REAJUSTE_PRECO, usuarioVerif, ""));
			getControleRemessaContaReceberReajustePrecoVO().setParcelaDeveReceberReajustePreco(false);
			getControleRemessaContaReceberReajustePrecoVO().setApresentarArquivoRemessa(true);
			setMensagemID("msg_ConfirmacaoLiberacaoEnviarRemessaSemReajustePreco");
		} catch (Exception e) {
			setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);
		}
	}
	
	public static void verificarPermissaoUsuarioLiberarEnviarRemessaSemReajustePrecoParcelas(UsuarioVO usuario, String nomeEntidade) throws Exception {
		ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico(nomeEntidade, usuario);
	}

	public String getUserNameLiberarRemessaSemReajuste() {
		if (userNameLiberarRemessaSemReajuste == null) {
			userNameLiberarRemessaSemReajuste = "";
		}
		return userNameLiberarRemessaSemReajuste;
	}

	public void setUserNameLiberarRemessaSemReajuste(String userNameLiberarRemessaSemReajuste) {
		this.userNameLiberarRemessaSemReajuste = userNameLiberarRemessaSemReajuste;
	}

	public String getSenhaLiberarRemessaSemReajuste() {
		if (senhaLiberarRemessaSemReajuste == null) {
			senhaLiberarRemessaSemReajuste = "";
		}
		return senhaLiberarRemessaSemReajuste;
	}

	public void setSenhaLiberarRemessaSemReajuste(String senhaLiberarRemessaSemReajuste) {
		this.senhaLiberarRemessaSemReajuste = senhaLiberarRemessaSemReajuste;
	}

	public List<OperacaoFuncionalidadeVO> getOperacaoFuncionalidadeVOs() {
		if (operacaoFuncionalidadeVOs == null) {
			operacaoFuncionalidadeVOs = new ArrayList<OperacaoFuncionalidadeVO>(0);
		}
		return operacaoFuncionalidadeVOs;
	}

	public void setOperacaoFuncionalidadeVOs(List<OperacaoFuncionalidadeVO> operacaoFuncionalidadeVOs) {
		this.operacaoFuncionalidadeVOs = operacaoFuncionalidadeVOs;
	}

	public ControleRemessaContaReceberVO getControleRemessaContaReceberReajustePrecoVO() {
		if (controleRemessaContaReceberReajustePrecoVO == null) {
			controleRemessaContaReceberReajustePrecoVO = new ControleRemessaContaReceberVO();
		}
		return controleRemessaContaReceberReajustePrecoVO;
	}

	public void setControleRemessaContaReceberReajustePrecoVO(ControleRemessaContaReceberVO controleRemessaContaReceberReajustePrecoVO) {
		this.controleRemessaContaReceberReajustePrecoVO = controleRemessaContaReceberReajustePrecoVO;
	}
	
	public void validarUnidadeEnsinoUsuarioLogado() {
		if (Uteis.isAtributoPreenchido(getUnidadeEnsinoLogadoClone())) {
			getControleRemessaVO().getUnidadeEnsinoVO().setCodigo(getUnidadeEnsinoLogadoClone().getCodigo());
			setFixarUnidadeEnsino(Boolean.TRUE);
		}
	}

	public Boolean getFixarUnidadeEnsino() {
		if (fixarUnidadeEnsino == null) {
			fixarUnidadeEnsino = Boolean.FALSE;
		}
		return fixarUnidadeEnsino;
	}

	public void setFixarUnidadeEnsino(Boolean fixarUnidadeEnsino) {
		this.fixarUnidadeEnsino = fixarUnidadeEnsino;
	}

	public Integer getQtdRegistrosExclusao() {
		if(qtdRegistrosExclusao == null) {
			qtdRegistrosExclusao = 0;
		}
		return qtdRegistrosExclusao;
	}

	public void setQtdRegistrosExclusao(Integer qtdRegistrosExclusao) {
		this.qtdRegistrosExclusao = qtdRegistrosExclusao;
	}

	public Double getVlrTotalRegistrosExclusao() {
		if(vlrTotalRegistrosExclusao == null) {
			vlrTotalRegistrosExclusao = 0.0;
		}
		return vlrTotalRegistrosExclusao;
	}

	public void setVlrTotalRegistrosExclusao(Double vlrTotalRegistrosExclusao) {
		this.vlrTotalRegistrosExclusao = vlrTotalRegistrosExclusao;
	}
	
	public String getApresentarModalTotalPendenciaProcessamentoArquivoRetorno(){
		try {
			if(!getAtivarPush()){
				return "RichFaces.$('modalProcessamento').hide();";
			}
		} catch (Exception e) {
			
		}
		return "";
	}
	
	public SituacaoProcessamentoArquivoRetornoEnum getSituacaoProcessamento() {
		if (situacaoProcessamento == null) {
			situacaoProcessamento = SituacaoProcessamentoArquivoRetornoEnum.AGUARDANDO_PROCESSAMENTO;
		}
		return situacaoProcessamento;
	}

	public void setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum situacaoProcessamento) {
		this.situacaoProcessamento = situacaoProcessamento;
	}

	public Boolean getAtivarPush() {
		if (ativarPush == null) {
			ativarPush = false;
		}
		return ativarPush;
	}

	public void setAtivarPush(Boolean ativarPush) {
		this.ativarPush = ativarPush;
	}
	
	private void consultarMotivoEstorno(ControleRemessaContaReceberVO obj){
		 if (Uteis.isAtributoPreenchido(obj.getMotivoEstorno()) && !Uteis.isAtributoPreenchido(obj.getUsuarioEstorno().getNome()) && !Uteis.isAtributoPreenchido(obj.getCodigoMotivoEstorno())) {            	
     		obj.setCodigoMotivoEstorno(obj.getMotivoEstorno());
     		String motivosRejeicao_Apresentar = MotivoRejeicaoRemessa.getMensagem(obj.getCodigoMotivoEstorno());
     		obj.setMotivoEstorno(motivosRejeicao_Apresentar);				
		}
	}

	
}
