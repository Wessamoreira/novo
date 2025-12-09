package jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CartaoCreditoDebitoRecorrenciaPessoaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroCartaoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ConfiguracaoRecebimentoCartaoOnlineVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.OperadoraCartaoVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.utilitarias.Stopwatch;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.TipoCartaoOperadoraCartaoEnum;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;

/**
 * @author Victor Hugo de Paula Costa - 13 de mai de 2016
 *
 */
@Service
@Lazy
public class JobBaixaCartaoCreditoDCC extends SuperFacadeJDBC implements Runnable {
	
	private Boolean jobExecutadaManualmente;
	private List<LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO> listaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static final String PATH_CHAVE_PRIVADA = "negocio/comuns/utilitarias/chave/private.key";

	
	@Override
	public void run() {
		if (getFacadeFactory() == null) {
			return;
		}
		RegistroExecucaoJobVO registroExecucaoJobVO = new RegistroExecucaoJobVO();
		Stopwatch tempoExcecucao = new Stopwatch();
		getListaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs().clear();
		UsuarioVO usuarioVO = null;
		String ipServidor = "";
		String idCliente = "";
		try {
			ConfiguracaoGeralSistemaVO conf = getAplicacaoControle().getConfiguracaoGeralSistemaVO(0, null);
			idCliente = conf.getIdAutenticacaoServOtimize();
			ipServidor = conf.getIpServidor();
			usuarioVO = getFacadeFactory().getUsuarioFacade().consultarPorPessoa(conf.getUsuarioResponsavelOperacoesExternas().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, null);
		} catch (Exception e2) {
			usuarioVO = new UsuarioVO();
		}
		try {
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getAplicacaoControle().getConfiguracaoFinanceiroVO(0);
			
			if (Uteis.validarEnvioEmail(ipServidor)) {
				registroExecucaoJobVO.setNome("JobBaixaCartaoCreditoDCC");
				registroExecucaoJobVO.setDataInicio(new Date());
				System.out.println("Execução - JobBaixaCartaoCreditoDCC - Início: "+Uteis.getDataComHoraCompleta(new Date())+"");
				tempoExcecucao.start();

				List<CartaoCreditoDebitoRecorrenciaPessoaVO> listaCartaoCreditoDebitoRecorrenciaPessoaVOs = getFacadeFactory().getCartaoCreditoDebitoRecorrenciaPessoaFacade().consultarContasRecorrenciaAptasPagamento();
				if (!listaCartaoCreditoDebitoRecorrenciaPessoaVOs.isEmpty()) {
					registroExecucaoJobVO.setTotal(listaCartaoCreditoDebitoRecorrenciaPessoaVOs.size());
					HashMap<Integer, ContaReceberVO> mapContaReceberVOs = new HashMap<Integer, ContaReceberVO>(0);
					HashMap<Integer, ConfiguracaoFinanceiroVO> mapConfiguracaoFinanceiroUnidadeEnsinoVOs = new HashMap<Integer, ConfiguracaoFinanceiroVO>(0);
					Map<String, ConfiguracaoFinanceiroCartaoVO> mapConfiguracaoFinanceiroCartaoVOs = new HashMap<String, ConfiguracaoFinanceiroCartaoVO>(0);
					Map<Integer, ConfiguracaoRecebimentoCartaoOnlineVO> mapConfPorUnidadeFinanceiro = new HashMap<Integer, ConfiguracaoRecebimentoCartaoOnlineVO>(0);

					for (CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO : listaCartaoCreditoDebitoRecorrenciaPessoaVOs) {

						List<ContaReceberVO> listaContaReceberVOs = getFacadeFactory().getContaReceberFacade().consultarContaReceberRecorrenciaAptasPagamentoPorMatriculaFormaPadraoPagamento(cartaoCreditoDebitoRecorrenciaPessoaVO.getMatriculaVO().getMatricula(), cartaoCreditoDebitoRecorrenciaPessoaVO.getFormaPadraoDataBaseCartaoRecorrente(), mapContaReceberVOs);
						if (listaContaReceberVOs.isEmpty()) {
							inicializarDadosLogExecucaoJobCartaoCredito(cartaoCreditoDebitoRecorrenciaPessoaVO, null, false, "Não foi encontrado Conta Receber para recebimento.", usuarioVO);
							continue;
						}

						for (ContaReceberVO contaReceberVO : listaContaReceberVOs) {
							try {
								ConfiguracaoFinanceiroVO configuracaoFinanceiroPrivilegiandoUnidadeEnsinoVO = null;
								contaReceberVO = getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(contaReceberVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_TODOS, configuracaoFinanceiroVO, usuarioVO);
								if (!mapConfiguracaoFinanceiroUnidadeEnsinoVOs.containsKey(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo())) {
									configuracaoFinanceiroPrivilegiandoUnidadeEnsinoVO = getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo(), usuarioVO);
								} else {
									configuracaoFinanceiroPrivilegiandoUnidadeEnsinoVO = mapConfiguracaoFinanceiroUnidadeEnsinoVOs.get(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo());
								}
								Map<Integer, ConfiguracaoRecebimentoCartaoOnlineVO> configuracaoRecebimentoCartaoOnlineVOs = realizarVerificacaoPermiteRecebimentoOnlineUsarMinhasContasVisaoAluno(contaReceberVO.getMatriculaAluno().getMatricula(), contaReceberVO, mapConfPorUnidadeFinanceiro, usuarioVO);
								NegociacaoRecebimentoVO negociacaoRecebimentoVO = inicializarDadosNegociacaoRecebimentoAluno(contaReceberVO, configuracaoRecebimentoCartaoOnlineVOs, configuracaoFinanceiroPrivilegiandoUnidadeEnsinoVO, cartaoCreditoDebitoRecorrenciaPessoaVO, usuarioVO);
								adicionarFormaPagamentoNegociacaoRecebimento(negociacaoRecebimentoVO, negociacaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO(), cartaoCreditoDebitoRecorrenciaPessoaVO.getOperadoraCartaoVO(), cartaoCreditoDebitoRecorrenciaPessoaVO, mapConfiguracaoFinanceiroCartaoVOs, configuracaoFinanceiroPrivilegiandoUnidadeEnsinoVO, usuarioVO);
							
								realizarRecebimentoCartaoCredito(negociacaoRecebimentoVO, configuracaoFinanceiroPrivilegiandoUnidadeEnsinoVO, usuarioVO);
								inicializarDadosLogExecucaoJobCartaoCredito(cartaoCreditoDebitoRecorrenciaPessoaVO, contaReceberVO, false, "Recorrência executada com Sucesso.", usuarioVO);
								registroExecucaoJobVO.setTotalSucesso(registroExecucaoJobVO.getTotalSucesso() + 1);
							} catch (Exception e) {
								inicializarDadosLogExecucaoJobCartaoCredito(cartaoCreditoDebitoRecorrenciaPessoaVO, contaReceberVO, true, e.getMessage(), usuarioVO);
								registroExecucaoJobVO.setTotalErro(registroExecucaoJobVO.getTotalErro() + 1);
								e.printStackTrace();
							}
						}
					}
				}
				tempoExcecucao.stop();
				registroExecucaoJobVO.setTempoExecucao(tempoExcecucao.getElapsedTicks());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, null);
				System.out.println("Execução - JobBaixaCartaoCreditoDCC - Fim: "+Uteis.getDataComHoraCompleta(new Date())+"");
			}
		} catch (Exception e) {
			try {
				registroExecucaoJobVO.setTotalErro(registroExecucaoJobVO.getTotalErro() + 1);
				registroExecucaoJobVO.setErro(e.getMessage());
				getFacadeFactory().getRegistroExecucaoJobFacade().incluirRegistroExecucaoJob(registroExecucaoJobVO, null);
				JobEnvioEmail.enviarSMSNotificacaoEquipeOtimize("ERRO EXECUÇÃO JOB BAIXA CARTÃO CRÉDITO NO DIA/HORA "+Uteis.getDataComHora(new Date())+" - CLIENTE: "+ idCliente+"");
				
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	public void inicializarDadosLogExecucaoJobCartaoCredito(CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO, ContaReceberVO contaReceberVO, Boolean erro, String observacao, UsuarioVO usuarioVO) {
		LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO obj = getFacadeFactory().getLogExecucaoJobCartaoCreditoDebitoRecorrenciaFacade().inicializarDadosLogExecucaoJobCartaoCredito(cartaoCreditoDebitoRecorrenciaPessoaVO, contaReceberVO, erro, observacao, this.getJobExecutadaManualmente(), usuarioVO);
		getListaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs().add(obj);
		try {
			getFacadeFactory().getLogExecucaoJobCartaoCreditoDebitoRecorrenciaFacade().incluir(obj, usuarioVO);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public Map<Integer, ConfiguracaoRecebimentoCartaoOnlineVO> realizarVerificacaoPermiteRecebimentoOnlineUsarMinhasContasVisaoAluno(String matriculaAluno, ContaReceberVO obj, Map<Integer, ConfiguracaoRecebimentoCartaoOnlineVO> mapConfPorUnidadeFinanceiro, UsuarioVO usuarioVO) throws Exception {

		MatriculaPeriodoVO matriculaPeriodoVO = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaBasicaUltimaMatriculaPeriodoAtivaPorMatriculaDadosJobDCC(matriculaAluno, usuarioVO);

		if (!obj.getBloquearPagamentoCartaoPorOutrosDebitos() && obj.isHabilitarBotaoRecebimento()) {
			Boolean permiteRecebimentoCartaoOnline = false;
			ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO = null;
			ConfiguracaoFinanceiroVO configuracaoFinanceiroVO = getAplicacaoControle().getConfiguracaoFinanceiroVO(obj.getUnidadeEnsinoFinanceira().getCodigo());
			if (!mapConfPorUnidadeFinanceiro.containsKey(obj.getUnidadeEnsinoFinanceira().getCodigo())) {
				permiteRecebimentoCartaoOnline = getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().verificarExistenciaConfiguracaoFinanceiroCartaoPorCodigoConfiguracaoFinanceiro(configuracaoFinanceiroVO.getCodigo());
				if (permiteRecebimentoCartaoOnline) {
					configuracaoRecebimentoCartaoOnlineVO = getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().consultarConfiguracaoRecebimentoCartaoOnlineDisponivel(matriculaPeriodoVO.getTurma().getCodigo(), matriculaPeriodoVO.getMatriculaVO().getCurso().getCodigo(), TipoNivelEducacional.getEnum(matriculaPeriodoVO.getMatriculaVO().getCurso().getNivelEducacional()).getValor(), obj.getUnidadeEnsinoFinanceira().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
					mapConfPorUnidadeFinanceiro.put(obj.getUnidadeEnsinoFinanceira().getCodigo(), configuracaoRecebimentoCartaoOnlineVO);
				} else {
					mapConfPorUnidadeFinanceiro.put(obj.getUnidadeEnsinoFinanceira().getCodigo(), null);
				}
			} else {
				configuracaoRecebimentoCartaoOnlineVO = mapConfPorUnidadeFinanceiro.get(obj.getUnidadeEnsinoFinanceira().getCodigo());
				permiteRecebimentoCartaoOnline = Uteis.isAtributoPreenchido(configuracaoRecebimentoCartaoOnlineVO);
			}
			obj.setConfiguracaoFinanceiro(configuracaoFinanceiroVO);// importante para rotina de apresentar nome origem na visao do aluno//pedro
																	// andrade.
			if (Uteis.isAtributoPreenchido(configuracaoRecebimentoCartaoOnlineVO)) {

				if (permiteRecebimentoCartaoOnline && getFacadeFactory().getConfiguracaoRecebimentoCartaoOnlineFacade().verificarContasRecebimentoOnlineContaReceberVO(obj, configuracaoRecebimentoCartaoOnlineVO, null, false, false, false, false, false, false, usuarioVO)) {
					obj.setPermiteRecebimentoCartaoCreditoOnline(permiteRecebimentoCartaoOnline);
				}
			}
		}
		return mapConfPorUnidadeFinanceiro;
	}

	public void adicionarFormaPagamentoNegociacaoRecebimento(NegociacaoRecebimentoVO negociacaoRecebimentoVO, ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO, OperadoraCartaoVO operadoraCartaoVO, CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO, Map<String, ConfiguracaoFinanceiroCartaoVO> mapConfiguracaoFinanceiroCartaoVOs, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		String valueConfiguracaoFinacneiroCartao = operadoraCartaoVO.getCodigo().toString() + "_" + configuracaoFinanceiroVO.getCodigo().toString();
		ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO = null;
		if (!mapConfiguracaoFinanceiroCartaoVOs.containsKey(valueConfiguracaoFinacneiroCartao)) {
			configuracaoFinanceiroCartaoVO = getFacadeFactory().getConfiguracaoFinanceiroCartaoFacade().consultarPorOperadoraCartaoConfiguracaoFinanceiro(operadoraCartaoVO, configuracaoFinanceiroVO, usuario);
			mapConfiguracaoFinanceiroCartaoVOs.put(valueConfiguracaoFinacneiroCartao, configuracaoFinanceiroCartaoVO);
		} else {
			configuracaoFinanceiroCartaoVO = mapConfiguracaoFinanceiroCartaoVOs.get(valueConfiguracaoFinacneiroCartao);
		}
		int parcelas = 1;
		if (TipoCartaoOperadoraCartaoEnum.CARTAO_CREDITO.name().equals(configuracaoFinanceiroCartaoVO.getOperadoraCartaoVO().getTipo())) {
			Date menorDataVencimento = null;
			for (ContaReceberNegociacaoRecebimentoVO c : negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs()) {
				if (Uteis.isAtributoPreenchido(c.getContaReceber().getDataVencimento()) && (menorDataVencimento == null || c.getContaReceber().getDataVencimento().before(menorDataVencimento))) {
					menorDataVencimento = c.getContaReceber().getDataVencimento();
				}
			}
			parcelas = configuracaoRecebimentoCartaoOnlineVO.getQtdeParcelasPermitida(negociacaoRecebimentoVO.getResiduo(), menorDataVencimento, usuario, negociacaoRecebimentoVO.getListaTipoOrigemContaReceber());
		}
		getFacadeFactory().getFormaPagamentoNegociacaoRecebimentoFacade().adicionarNovoCartaoCredito(negociacaoRecebimentoVO, configuracaoFinanceiroCartaoVO, parcelas, 1, usuario);
		negociacaoRecebimentoVO.setValorTotalRecebimento(0.0);
		for (FormaPagamentoNegociacaoRecebimentoVO obj : negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs()) {
			negociacaoRecebimentoVO.setValorTotalRecebimento(negociacaoRecebimentoVO.getValorTotalRecebimento() + obj.getValorRecebimento());
			inicializarDadosFormaPagamentoNegociacaoRecebimentoCartaoCredito(negociacaoRecebimentoVO, obj, cartaoCreditoDebitoRecorrenciaPessoaVO, configuracaoFinanceiroCartaoVO, usuario);
		}
	}

	public void inicializarDadosFormaPagamentoNegociacaoRecebimentoCartaoCredito(NegociacaoRecebimentoVO negociacaoRecebimentoVO, FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO, CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO, ConfiguracaoFinanceiroCartaoVO configuracaoFinanceiroCartaoVO, UsuarioVO usuario) throws Exception {
		String caminhoPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		String caminhoChavePrivada = caminhoPath.substring(1, caminhoPath.lastIndexOf('/') + 1).concat(PATH_CHAVE_PRIVADA);
		formaPagamentoNegociacaoRecebimentoVO.setQtdeParcelasCartaoCredito(1);
		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNomeCartaoCredito(cartaoCreditoDebitoRecorrenciaPessoaVO.getNomeCartao());
		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setNumeroCartao(Uteis.decriptografarPorAlgoritimoRSA(cartaoCreditoDebitoRecorrenciaPessoaVO.getNumeroCartao(), caminhoChavePrivada));
		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setMesValidade(cartaoCreditoDebitoRecorrenciaPessoaVO.getMesValidade());
		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setAnoValidade(cartaoCreditoDebitoRecorrenciaPessoaVO.getAnoValidade());
		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setCodigoVerificacao(Uteis.decriptografarPorAlgoritimoRSA(cartaoCreditoDebitoRecorrenciaPessoaVO.getCodigoSeguranca(), caminhoChavePrivada));
		formaPagamentoNegociacaoRecebimentoVO.setValorRecebimento(negociacaoRecebimentoVO.getValorTotalRecebimento());
		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setTipoFinanciamentoEnum(negociacaoRecebimentoVO.getConfiguracaoRecebimentoCartaoOnlineVO().getTipoFinanciamentoPermitido(formaPagamentoNegociacaoRecebimentoVO.getValorRecebimento(), negociacaoRecebimentoVO.realizarCalculoMaiorDataVencimento(), usuario, negociacaoRecebimentoVO.getListaTipoOrigemContaReceber()));
		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setValorParcela(negociacaoRecebimentoVO.getValorTotalRecebimento());
		formaPagamentoNegociacaoRecebimentoVO.setConfiguracaoFinanceiroCartaoVO(configuracaoFinanceiroCartaoVO);
		formaPagamentoNegociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoCartaoCreditoVO().setConfiguracaoFinanceiroCartaoVO(configuracaoFinanceiroCartaoVO);
	}

	public NegociacaoRecebimentoVO inicializarDadosNegociacaoRecebimentoAluno(ContaReceberVO contaReceberVO, Map<Integer, ConfiguracaoRecebimentoCartaoOnlineVO> configuracaoRecebimentoCartaoOnlineVOs, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, CartaoCreditoDebitoRecorrenciaPessoaVO cartaoCreditoDebitoRecorrenciaPessoaVO, UsuarioVO usuario) throws Exception {

		if (configuracaoRecebimentoCartaoOnlineVOs.isEmpty() || !configuracaoRecebimentoCartaoOnlineVOs.containsKey(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo()) || !Uteis.isAtributoPreenchido(configuracaoRecebimentoCartaoOnlineVOs.get(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo()))) {
			throw new Exception(UteisJSF.internacionalizar("msg_RecebimentoCartao_semConfiguracao"));
		}
		ConfiguracaoRecebimentoCartaoOnlineVO configuracaoRecebimentoCartaoOnlineVO = (configuracaoRecebimentoCartaoOnlineVOs.get(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo()));

		contaReceberVO.setRealizandoRecebimento(true);
//		if (getConfiguracaoFinanceiroPrevilegiandoUnidadeEnsino(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo(), usuario).getVencimentoParcelaDiaUtil()) {
		if (configuracaoFinanceiroVO.getVencimentoParcelaDiaUtil()) {
			contaReceberVO.getDataOriginalVencimento();
			contaReceberVO.setDataVencimentoDiaUtil(contaReceberVO.getDataVencimento());
			contaReceberVO.setDataVencimentoDiaUtil(getFacadeFactory().getContaReceberFacade().obterDataVerificandoDiaUtil(contaReceberVO.getDataVencimento(), contaReceberVO.getUnidadeEnsino().getCidade().getCodigo(), usuario));
			contaReceberVO.setDataVencimento(contaReceberVO.getDataVencimentoDiaUtil());
		}
		ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO = new ContaReceberNegociacaoRecebimentoVO();
		contaReceberNegociacaoRecebimentoVO.setContaReceber(contaReceberVO);
		contaReceberNegociacaoRecebimentoVO.setValorTotal(contaReceberVO.getCalcularValorFinal(new Date(), configuracaoFinanceiroVO, false, usuario));
		NegociacaoRecebimentoVO negociacaoRecebimentoVO = new NegociacaoRecebimentoVO();
		negociacaoRecebimentoVO.setMatricula(contaReceberVO.getMatriculaAluno().getMatricula());
		negociacaoRecebimentoVO.adicionarObjContaReceberNegociacaoRecebimentoVOs(contaReceberNegociacaoRecebimentoVO);
		negociacaoRecebimentoVO.setValorTotal(negociacaoRecebimentoVO.getValorTotal() + contaReceberVO.getValorReceberCalculado());
		negociacaoRecebimentoVO.setTipoOrigemContaReceber(TipoOrigemContaReceber.getEnum(contaReceberVO.getTipoOrigem()));
		negociacaoRecebimentoVO.setConfiguracaoRecebimentoCartaoOnlineVO(configuracaoRecebimentoCartaoOnlineVO);
		negociacaoRecebimentoVO.setCriarRegistroRecorrenciaDCC(false);
		negociacaoRecebimentoVO.setRealizandoPagamentoJobRecorrencia(true);
		negociacaoRecebimentoVO.setJobExecutadaManualmente(this.getJobExecutadaManualmente());
		negociacaoRecebimentoVO.setCartaoCreditoDebitoRecorrenciaPessoaVO(cartaoCreditoDebitoRecorrenciaPessoaVO);
		return negociacaoRecebimentoVO;

	}

	public void realizarRecebimentoCartaoCredito(NegociacaoRecebimentoVO negociacaoRecebimentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		getFacadeFactory().getNegociacaoRecebimentoFacade().realizarRecebimentoCartaoCreditoMatriculaRenovacaoOnline(negociacaoRecebimentoVO, negociacaoRecebimentoVO.getMatricula(), configuracaoFinanceiroVO, usuario);
	}

	public Boolean getJobExecutadaManualmente() {
		if (jobExecutadaManualmente == null) {
			jobExecutadaManualmente = false;
		}
		return jobExecutadaManualmente;
	}


	public void setJobExecutadaManualmente(Boolean jobExecutadaManualmente) {
		this.jobExecutadaManualmente = jobExecutadaManualmente;
	}
	
	public List<LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO> getListaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs() {
		if (listaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs == null) {
			listaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs = new ArrayList<LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO>(0);
		}
		return listaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs;
	}

	public void setListaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs(List<LogExecucaoJobCartaoCreditoDebitoRecorrenciaVO> listaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs) {
		this.listaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs = listaLogExecucaoJobCartaoCreditoDebitoRecorrenciaVOs;
	}
}
