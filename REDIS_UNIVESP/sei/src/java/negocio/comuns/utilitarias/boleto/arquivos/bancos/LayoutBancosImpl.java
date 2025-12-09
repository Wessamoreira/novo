package negocio.comuns.utilitarias.boleto.arquivos.bancos;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberAgrupadaVO;
import negocio.comuns.financeiro.ContaReceberHistoricoVO;
import negocio.comuns.financeiro.ContaReceberNaoLocalizadaArquivoRetornoVO;
import negocio.comuns.financeiro.ContaReceberNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.ContaReceberRegistroArquivoVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleCobrancaVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaVO;
import negocio.comuns.financeiro.FormaPagamentoNegociacaoRecebimentoVO;
import negocio.comuns.financeiro.MapaPendenciasControleCobrancaVO;
import negocio.comuns.financeiro.NegociacaoRecebimentoVO;
import negocio.comuns.financeiro.RegistroArquivoVO;
import negocio.comuns.financeiro.RegistroDetalheVO;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoArquivoRetornoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ProgressBarVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.comuns.utilitarias.dominios.SituacaoArquivoRetorno;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;
import negocio.facade.jdbc.arquitetura.FacadeManager;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;
import negocio.facade.jdbc.financeiro.ContaReceber;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public abstract class LayoutBancosImpl extends SuperFacadeJDBC {

	protected FormaPagamentoNegociacaoRecebimentoVO criarFormaPagamentoNegociacaoRecebimentoVO(ContaReceberVO contaReceberVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, RegistroDetalheVO detalhe, UsuarioVO usuarioVO, ContaCorrenteVO contaCorrenteVO) throws Exception {
		FormaPagamentoNegociacaoRecebimentoVO formaPagamentoNegociacaoRecebimentoVO = new FormaPagamentoNegociacaoRecebimentoVO();	
		if (contaCorrenteVO != null && contaCorrenteVO.getCodigo().intValue() != 0) {
			formaPagamentoNegociacaoRecebimentoVO.setContaCorrente(contaCorrenteVO);
		} else {
			formaPagamentoNegociacaoRecebimentoVO.setContaCorrente((ContaCorrenteVO) Uteis.clonar(configuracaoFinanceiroVO.getContaCorrentePadraoControleCobranca()));
		}
		if(Uteis.isAtributoPreenchido(configuracaoFinanceiroVO) && Uteis.isAtributoPreenchido(configuracaoFinanceiroVO.getFormaPagamentoPadraoControleCobranca())) {
			formaPagamentoNegociacaoRecebimentoVO.setFormaPagamento((FormaPagamentoVO) Uteis.clonar(configuracaoFinanceiroVO.getFormaPagamentoPadraoControleCobranca()));	
		}else {
			ConfiguracaoFinanceiroVO configFinVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorUnidadeEnsino(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
			formaPagamentoNegociacaoRecebimentoVO.setFormaPagamento((FormaPagamentoVO) Uteis.clonar(configFinVO.getFormaPagamentoPadraoControleCobranca()));
		}
		
		formaPagamentoNegociacaoRecebimentoVO.setValorRecebimento(detalhe.getValorPago());
		formaPagamentoNegociacaoRecebimentoVO.setDataCredito(Uteis.getDataMinutos(detalhe.getDataCredito()));
		return formaPagamentoNegociacaoRecebimentoVO;
	}

	public void criarNegociacaoRecebimentoVOs(List<ContaReceberVO> contaReceberVOs, RegistroArquivoVO arquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO responsavel, ContaCorrenteVO contaCorrenteVO, Integer diasVariacaoDataVencimento) throws Exception {
		NegociacaoRecebimentoVO negociacaoRecebimentoVO = null;
		RegistroDetalheVO detalhe = new RegistroDetalheVO();
		int index = 0;
		for (ContaReceberVO contaReceberVO : contaReceberVOs) {
			detalhe = arquivo.getRegistroDetalhe(contaReceberVO.getCodigo());
			if (!contaReceberVO.isGerarNegociacaoRecebimento()) {
				continue;
			}
			if (detalhe == null || detalhe.getCodigoContaReceber().intValue() == 0) {
				continue;
			}
			contaReceberVO = getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(contaReceberVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, responsavel);
			preencherContaReceberVOComArquivoRetorno(contaReceberVO, detalhe, configuracaoFinanceiroVO, responsavel, diasVariacaoDataVencimento);
			ConfiguracaoFinanceiroVO configFinVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorUnidadeEnsino(contaReceberVO.getUnidadeEnsinoFinanceira().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, responsavel);
			negociacaoRecebimentoVO = criarNegociacaoRecebimentoVO(contaReceberVO, contaCorrenteVO, configFinVO, responsavel);

			negociacaoRecebimentoVO.setData(Uteis.getDataMinutos(detalhe.getDataOcorrencia()));

			negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().add(criarFormaPagamentoNegociacaoRecebimentoVO(contaReceberVO, configFinVO, detalhe, responsavel, contaCorrenteVO));
			negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().add(criarContaReceberNegociacaoRecebimentoVO(contaReceberVO, detalhe));
			negociacaoRecebimentoVO.setValorTotal(contaReceberVO.getValorRecebido());
			negociacaoRecebimentoVO.setTipoPessoa(contaReceberVO.getTipoPessoa());
			negociacaoRecebimentoVO.setRecebimentoBoletoAutomatico(Boolean.TRUE);
			if (negociacaoRecebimentoVO.getTipoPessoa().equals("AL")) {
				negociacaoRecebimentoVO.setMatricula(contaReceberVO.getMatriculaAluno().getMatricula());
			}
			contaReceberVO.setSituacao("RE");
			detalhe.setBoletoBaixado(true);
			contaReceberVOs.set(index, contaReceberVO);
			index++;
			incluirNegociacaoRecebimentoControleCobranca(negociacaoRecebimentoVO, configFinVO, responsavel);
		}
	}

	public void criarNegociacaoRecebimentoVOsBaixandoContas(List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs, RegistroArquivoVO arquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleCobrancaVO controleCobrancaVO, UsuarioVO usuario) throws Exception {
		NegociacaoRecebimentoVO negociacaoRecebimentoVO = null;
		RegistroDetalheVO detalhe = new RegistroDetalheVO();
		ContaCorrenteVO contaCorrenteVO = controleCobrancaVO.getContaCorrenteVO();
		int index = 0;
		int cont = 0;
		realizarCriacaoContaReceberMedianteContaAgrupada(contaReceberRegistroArquivoVOs);
		getFacadeFactory().getContaReceberRegistroArquivoFacade().montarDadosAtualizacaoContaReceber(contaReceberRegistroArquivoVOs, controleCobrancaVO.getResponsavel());
		HashMap<Integer, ContaReceberVO> map = new HashMap<Integer, ContaReceberVO>(0);
		HashMap<Integer, Integer> mapCodigoContaAgrupada = null;
		controleCobrancaVO.setMotivoErroProcessamento("");
		
		controleCobrancaVO.setMotivoErroProcessamento("");
		for (ContaReceberRegistroArquivoVO contaReceberRegistroArquivoVO : contaReceberRegistroArquivoVOs) {
			cont++;
			try {
				processarContaReceberRegistroArquivo(contaReceberRegistroArquivoVOs, arquivo, controleCobrancaVO, contaReceberRegistroArquivoVO, detalhe, map, mapCodigoContaAgrupada, contaCorrenteVO, negociacaoRecebimentoVO, usuario, index);
 			
			} catch (Exception e) {
				controleCobrancaVO.setDataTerminoProcessamento(new Date());
				controleCobrancaVO.setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.ERRO_PROCESSAMENTO);
				String complementoMensagemErro = "";
				if (Uteis.isAtributoPreenchido(contaReceberRegistroArquivoVO.getContaReceberVO().getNossoNumero())) {
					complementoMensagemErro = "Nosso Número: " + contaReceberRegistroArquivoVO.getContaReceberVO().getNossoNumero();
				}
				controleCobrancaVO.setMotivoErroProcessamento(controleCobrancaVO.getMotivoErroProcessamento().equals("") ? e.getMessage() + complementoMensagemErro : controleCobrancaVO.getMotivoErroProcessamento() + "; " + e.getMessage() + complementoMensagemErro);
				try {
					getFacadeFactory().getControleCobrancaFacade().realizarRegistroErroProcessamento(controleCobrancaVO.getCodigo(), controleCobrancaVO.getSituacaoProcessamento(),controleCobrancaVO.getMotivoErroProcessamento(), controleCobrancaVO.getDataTerminoProcessamento(), usuario);
				} catch (Exception ex) {

				}
				if ((!Uteis.ARQUIVOS_CONTROLE_COBRANCA.containsKey(Uteis.getNomeArquivoComUnidadeEnsino(controleCobrancaVO.getNomeArquivo_Apresentar(), controleCobrancaVO.getUnidadeEnsinoVO().getCodigo()))) || (Uteis.ARQUIVOS_CONTROLE_COBRANCA.containsKey(Uteis.getNomeArquivoComUnidadeEnsino(controleCobrancaVO.getNomeArquivo_Apresentar(), controleCobrancaVO.getUnidadeEnsinoVO().getCodigo())) && !Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(controleCobrancaVO.getNomeArquivo_Apresentar(), controleCobrancaVO.getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor()))) {
					Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(controleCobrancaVO.getNomeArquivo_Apresentar(), controleCobrancaVO.getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor());
				}
			}
		}
		realizarBaixaContaAgrupada(arquivo, controleCobrancaVO, contaReceberRegistroArquivoVOs, mapCodigoContaAgrupada);
		// Comentando pelo Rodrigo pois o mesmo só pode ser registrado como baixado se a job já processou todas as contas
		// Portanto este foi enviado para a JobBaixarContasArquivoRetorno		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void processarContaReceberRegistroArquivo(List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs, RegistroArquivoVO arquivo, ControleCobrancaVO controleCobrancaVO, ContaReceberRegistroArquivoVO contaReceberRegistroArquivoVO, RegistroDetalheVO detalhe, HashMap<Integer, ContaReceberVO> map, HashMap<Integer, Integer> mapCodigoContaAgrupada,  ContaCorrenteVO contaCorrenteVO, NegociacaoRecebimentoVO negociacaoRecebimentoVO, UsuarioVO usuario, int index) throws Exception {
		if (contaReceberRegistroArquivoVO.getContaReceberAgrupada()) {
			contaReceberRegistroArquivoVOs.set(index, contaReceberRegistroArquivoVO);
			index++;
			contaReceberRegistroArquivoVO.getContaReceberVO().setSituacao("RE");
			return;
		}
		detalhe = arquivo.getRegistroDetalhe(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo());
		
		if (contaReceberRegistroArquivoVO.getContaReceberVO().getObservacao().equals("Conta Não Localizada!")) {
			ContaReceberNaoLocalizadaArquivoRetornoVO obj = (ContaReceberNaoLocalizadaArquivoRetornoVO) getFacadeFactory().getContaReceberNaoLocalizadaArquivoRetornoFacade().consultarPorNossoNumeroUnico(contaReceberRegistroArquivoVO.getContaReceberVO().getNossoNumero(), detalhe.getDataOcorrencia(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, controleCobrancaVO.getResponsavel());
			if (obj.getCodigo() != 0) {
				getFacadeFactory().getContaReceberNaoLocalizadaArquivoRetornoFacade().alterar(obj, controleCobrancaVO.getResponsavel());
			} else {
				obj.setNossoNumero(contaReceberRegistroArquivoVO.getContaReceberVO().getNossoNumero());
				obj.setDataVcto(contaReceberRegistroArquivoVO.getContaReceberVO().getDataVencimento());
				obj.setSituacao(contaReceberRegistroArquivoVO.getContaReceberVO().getSituacao());
				obj.setValor(contaReceberRegistroArquivoVO.getContaReceberVO().getValor());
				obj.setDataPagamento(detalhe.getDataOcorrencia());
				obj.setValorRecebido(contaReceberRegistroArquivoVO.getContaReceberVO().getValorRecebido());
				obj.getContaCorrenteVO().setCodigo(controleCobrancaVO.getContaCorrenteVO().getCodigo());
				getFacadeFactory().getContaReceberNaoLocalizadaArquivoRetornoFacade().incluir(obj, controleCobrancaVO.getResponsavel());
			}

			getFacadeFactory().getContaReceberRegistroArquivoFacade().alterar(contaReceberRegistroArquivoVO, usuario);
			//getFacadeFactory().getRegistroDetalheFacade().alterar(detalhe, usuario);
			return;			
		}
		if (!map.containsKey(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo())) {
			if (!contaReceberRegistroArquivoVO.getContaReceberVO().isGerarNegociacaoRecebimento()
					&& (getFacadeFactory().getContaReceberRegistroArquivoFacade().verificarExistenciaContaReceberRecebidaDuplicidade(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo(), arquivo.getCodigo(), detalhe.getDataOcorrencia())
						|| (contaReceberRegistroArquivoVO.getContaRecebidaDuplicidade() && getFacadeFactory().getContaReceberFacade().verificaSituacaoContaReceberPorCodigoSituacao(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo(), "RE", usuario)) )
					) {
				// insere no historico da contareceber a tenta de baixa
				// do
				// arquivo novamente, seja pela duplidade do arquivo ou
				// seja
				// pelo fato de ter renomeado o arquivo de retorno.
				contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta Pgto Duplicidade!");					
				ContaReceberHistoricoVO crh = new ContaReceberHistoricoVO();
				crh.setContaReceber(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo());
				crh.setData(new Date());
				crh.setMotivo("Conta Receber já processada. Tentativa em duplicidade!");
				crh.setNomeArquivo("");
				crh.setNossoNumero(contaReceberRegistroArquivoVO.getContaReceberVO().getNossoNumero());
				crh.setResponsavel(controleCobrancaVO.getResponsavel());
				crh.setValorRecebimento(contaReceberRegistroArquivoVO.getContaReceberVO().getValorRecebido());
				contaReceberRegistroArquivoVO.setContaRecebidaDuplicidade(Boolean.TRUE);
				getFacadeFactory().getContaReceberHistoricoFacade().incluir(crh, usuario);
				crh = null;
				return;
			}
			if(!contaReceberRegistroArquivoVO.getContaReceberVO().isGerarNegociacaoRecebimento()){
				return;
			}
			if (detalhe == null || detalhe.getCodigoContaReceber().intValue() == 0 || detalhe.getValorPago().doubleValue() == 0) {
				return;
			}
			
			ConfiguracaoFinanceiroVO configFinVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorUnidadeEnsino(contaReceberRegistroArquivoVO.getContaReceberVO().getUnidadeEnsinoFinanceira().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario);

			if (controleCobrancaVO.getContaCorrenteVO() == null || controleCobrancaVO.getContaCorrenteVO().getCodigo().equals(0)) {
				contaCorrenteVO = getFacadeFactory().getContaCorrenteFacade().consultarContaCorrentePadraoPorContaReceber(detalhe.getCodigoContaReceber(), configFinVO);
			}

			preencherContaReceberVOComArquivoRetorno(contaReceberRegistroArquivoVO.getContaReceberVO(), detalhe, configFinVO, controleCobrancaVO.getResponsavel(), contaReceberRegistroArquivoVO.getDiasVariacaoDataVencimento());
			
			contaReceberRegistroArquivoVO.getContaReceberVO().setRealizandoBaixaAutomatica(true);
			
			negociacaoRecebimentoVO = criarNegociacaoRecebimentoVO(contaReceberRegistroArquivoVO.getContaReceberVO(),  contaCorrenteVO, configFinVO, controleCobrancaVO.getResponsavel());
			
			if (negociacaoRecebimentoVO.getContaCorrenteCaixa().getUtilizaAbatimentoNoRepasseRemessaBanco()) {
				negociacaoRecebimentoVO.setValorTaxaBancaria(contaReceberRegistroArquivoVO.getContaReceberVO().getTaxaBoleto());
			}
			
			negociacaoRecebimentoVO.setData(Uteis.getDataMinutos(detalhe.getDataOcorrencia()));
			negociacaoRecebimentoVO.setDataCreditoBoletoBancario(Uteis.getDataMinutos(detalhe.getDataCredito()));

			negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().add(criarFormaPagamentoNegociacaoRecebimentoVO(contaReceberRegistroArquivoVO.getContaReceberVO(), configFinVO, detalhe, controleCobrancaVO.getResponsavel(), contaCorrenteVO));
			negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().add(criarContaReceberNegociacaoRecebimentoVO(contaReceberRegistroArquivoVO.getContaReceberVO(), detalhe));
			negociacaoRecebimentoVO.setValorTotal(contaReceberRegistroArquivoVO.getContaReceberVO().getValorRecebido());
			negociacaoRecebimentoVO.setTipoPessoa(contaReceberRegistroArquivoVO.getContaReceberVO().getTipoPessoa());
			negociacaoRecebimentoVO.setRecebimentoBoletoAutomatico(Boolean.TRUE);
			if (negociacaoRecebimentoVO.getTipoPessoa().equals("AL")) {
				negociacaoRecebimentoVO.setMatricula(contaReceberRegistroArquivoVO.getContaReceberVO().getMatriculaAluno().getMatricula());
			}
			contaReceberRegistroArquivoVO.getContaReceberVO().setRealizandoRecebimento(true);
			contaReceberRegistroArquivoVO.getContaReceberVO().setSituacao("RE");
			detalhe.setBoletoBaixado(true);
			contaReceberRegistroArquivoVOs.set(index, contaReceberRegistroArquivoVO);
			index++;				
//			incluirNegociacaoRecebimentoControleCobranca(negociacaoRecebimentoVO, configuracaoFinanceiroVO, controleCobrancaVO.getResponsavel());
			
			incluirNegociacaoRecebimentoControleCobranca(negociacaoRecebimentoVO, configFinVO, usuario);
			
			getFacadeFactory().getCampanhaFacade().finalizarAgendaCompromissoContaReceber(negociacaoRecebimentoVO);
			
			getFacadeFactory().getRegistroDetalheFacade().alterar(detalhe, controleCobrancaVO.getResponsavel());
			contaReceberRegistroArquivoVO.getContaReceberVO().getRegistroArquivoVO().setCodigo(arquivo.getCodigo());
			contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta já Recebida!");
			contaReceberRegistroArquivoVO.getContaReceberVO().setGerarNegociacaoRecebimento(false);
			

			if (!contaReceberRegistroArquivoVO.getContaReceberVO().getContaReceberAgrupada().equals(0)) {
				if (mapCodigoContaAgrupada == null) {
					mapCodigoContaAgrupada = new HashMap<Integer, Integer>(0);
				}
				if (!mapCodigoContaAgrupada.containsKey(contaReceberRegistroArquivoVO.getContaReceberVO().getContaReceberAgrupada())) {
					mapCodigoContaAgrupada.put(contaReceberRegistroArquivoVO.getContaReceberVO().getContaReceberAgrupada(), contaReceberRegistroArquivoVO.getContaReceberVO().getContaReceberAgrupada());
				}
			}
			getFacadeFactory().getContaReceberRegistroArquivoFacade().alterar(contaReceberRegistroArquivoVO, controleCobrancaVO.getResponsavel());
			map.put(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo(), contaReceberRegistroArquivoVO.getContaReceberVO());
			negociacaoRecebimentoVO = null;
		} else {
			contaReceberRegistroArquivoVO.setContaReceberVO(map.get(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo()));
			if (!contaReceberRegistroArquivoVO.getContaReceberVO().isGerarNegociacaoRecebimento()) {
				// insere no historico da contareceber a tenta de baixa
				// do
				// arquivo novamente, seja pela duplidade do arquivo ou
				// seja
				// pelo fato de ter renomeado o arquivo de retorno.
				ContaReceberHistoricoVO crh = new ContaReceberHistoricoVO();
				crh.setContaReceber(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo());
				crh.setData(new Date());
				crh.setMotivo("Conta Receber já processada. Tentativa em duplicidade!");
				crh.setNomeArquivo("");
				crh.setNossoNumero(contaReceberRegistroArquivoVO.getContaReceberVO().getNossoNumero());
				crh.setResponsavel(controleCobrancaVO.getResponsavel());
				crh.setValorRecebimento(contaReceberRegistroArquivoVO.getContaReceberVO().getValorRecebido());
				contaReceberRegistroArquivoVO.setContaRecebidaDuplicidade(Boolean.TRUE);					
				getFacadeFactory().getContaReceberHistoricoFacade().incluir(crh, usuario);
				getFacadeFactory().getContaReceberRegistroArquivoFacade().alterar(contaReceberRegistroArquivoVO, controleCobrancaVO.getResponsavel());
				crh = null;
				return;
			}
		}
	}
	
	// Método responsável por alterar a situação da conta a receber agrupada
	// dentro da lista apresentada do arquivo de retorno
	// Carlos
	public void realizarBaixaContaAgrupada(RegistroArquivoVO arquivo, ControleCobrancaVO controleCobrancaVO, List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs, HashMap<Integer, Integer> mapCodigoContaAgrupada) throws Exception {
		RegistroDetalheVO detalhe = new RegistroDetalheVO();
		if (mapCodigoContaAgrupada != null) {
			for (Integer codigoContaAgrupada : mapCodigoContaAgrupada.keySet()) {
				for (ContaReceberRegistroArquivoVO contaReceberRegistroArquivoVO : contaReceberRegistroArquivoVOs) {
					if (codigoContaAgrupada.equals(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo())) {
						detalhe = arquivo.getRegistroDetalhe(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo());
						if (detalhe != null) {
							getFacadeFactory().getContaReceberAgrupadaFacade().alterarSituacaoContaAgrupadaArquivoRetorno(detalhe.getValorPago(), detalhe.getValorLiquido(), detalhe.getDataOcorrencia(), new Date(), codigoContaAgrupada, SituacaoContaReceber.RECEBIDO.name(), controleCobrancaVO.getResponsavel());
							getFacadeFactory().getRegistroDetalheFacade().alterar(detalhe, controleCobrancaVO.getResponsavel());
						}
						contaReceberRegistroArquivoVO.getContaReceberVO().setSituacao("RE");
						contaReceberRegistroArquivoVO.getContaReceberVO().getRegistroArquivoVO().setCodigo(arquivo.getCodigo());
						contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta já Recebida!");
						contaReceberRegistroArquivoVO.getContaReceberVO().setGerarNegociacaoRecebimento(false);
					}
				}
			}
		}
	}

	public void realizarCriacaoContaReceberMedianteContaAgrupada(List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs) {
		List<ContaReceberRegistroArquivoVO> listaContaReceberRegistroArquivoVOs = new ArrayList<ContaReceberRegistroArquivoVO>(0);
		for (ContaReceberRegistroArquivoVO contaReceberRegistroArquivoVO : contaReceberRegistroArquivoVOs) {
			if (contaReceberRegistroArquivoVO.getContaReceberAgrupada()) {
				List<Integer> listaCodigoContaReceber = getFacadeFactory().getContaReceberFacade().consultarContaReceberPorContaAgrupada(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo());
				if (!listaCodigoContaReceber.isEmpty()) {
					for (Integer codigoContaReceber : listaCodigoContaReceber) {
						ContaReceberVO contaReceberVO = new ContaReceberVO();
						contaReceberVO.setCodigo(codigoContaReceber);
						contaReceberVO.setContaReceberAgrupada(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo());
						ContaReceberRegistroArquivoVO obj = new ContaReceberRegistroArquivoVO();
						obj.setContaReceberVO(contaReceberVO);
						listaContaReceberRegistroArquivoVOs.add(obj);
					}
				}
			}
		}
		contaReceberRegistroArquivoVOs.addAll(listaContaReceberRegistroArquivoVOs);
	}

	// public void
	// criarNegociacaoRecebimentoVOsBaixandoContasAgrupadas(List<ContaReceberRegistroArquivoVO>
	// contaReceberRegistroArquivoVOs, RegistroArquivoVO arquivo,
	// ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleCobrancaVO
	// controleCobrancaVO) throws Exception {
	// NegociacaoRecebimentoVO negociacaoRecebimentoVO = null;
	// RegistroDetalheVO detalhe = new RegistroDetalheVO();
	// ContaCorrenteVO contaCorrenteVO = null;
	// int index = 0;
	//
	// realizarCriacaoContaReceberMedianteContaAgrupada(contaReceberRegistroArquivoVOs);
	// getFacadeFactory().getContaReceberRegistroArquivoFacade().realizarAtualizacaoDadosContaReceberEmContaReceberRegistroArquivo(contaReceberRegistroArquivoVOs,
	// controleCobrancaVO.getResponsavel());
	//
	// HashMap<Integer, ContaReceberVO> map = new HashMap<Integer,
	// ContaReceberVO>(0);
	// for (ContaReceberRegistroArquivoVO contaReceberRegistroArquivoVO :
	// contaReceberRegistroArquivoVOs) {
	// detalhe =
	// arquivo.getRegistroDetalhe(contaReceberRegistroArquivoVO.getContaReceberAgrupadaVO().getCodigo());
	// if
	// (contaReceberRegistroArquivoVO.getContaReceberVO().getObservacao().equals("Conta Não Localizada!"))
	// {
	// ContaReceberNaoLocalizadaArquivoRetornoVO obj =
	// (ContaReceberNaoLocalizadaArquivoRetornoVO)
	// getFacadeFactory().getContaReceberNaoLocalizadaArquivoRetornoFacade().consultarPorNossoNumeroUnico(contaReceberRegistroArquivoVO.getContaReceberVO().getNossoNumero(),
	// false, Uteis.NIVELMONTARDADOS_COMBOBOX,
	// controleCobrancaVO.getResponsavel());
	// if (obj.getCodigo() != 0) {
	// getFacadeFactory().getContaReceberNaoLocalizadaArquivoRetornoFacade().alterar(obj,
	// controleCobrancaVO.getResponsavel());
	// } else {
	// obj.setNossoNumero(contaReceberRegistroArquivoVO.getContaReceberVO().getNossoNumero());
	// obj.setDataVcto(contaReceberRegistroArquivoVO.getContaReceberVO().getDataVencimento());
	// obj.setSituacao(contaReceberRegistroArquivoVO.getContaReceberVO().getSituacao());
	// obj.setValor(contaReceberRegistroArquivoVO.getContaReceberVO().getValor());
	// obj.setDataPagamento(detalhe.getDataOcorrencia());
	// obj.setValorRecebido(contaReceberRegistroArquivoVO.getContaReceberVO().getValorRecebido());
	// getFacadeFactory().getContaReceberNaoLocalizadaArquivoRetornoFacade().incluir(obj,
	// controleCobrancaVO.getResponsavel());
	// }
	// }
	// if
	// (!map.containsKey(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo()))
	// {
	// if
	// (!contaReceberRegistroArquivoVO.getContaReceberVO().isGerarNegociacaoRecebimento())
	// {
	// // insere no historico da contareceber a tenta de baixa do
	// // arquivo novamente, seja pela duplidade do arquivo ou seja
	// // pelo fato de ter renomeado o arquivo de retorno.
	// ContaReceberHistoricoVO crh = new ContaReceberHistoricoVO();
	// crh.setContaReceber(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo());
	// crh.setData(new Date());
	// crh.setMotivo("Conta Receber já processada. Tentativa em duplicidade!");
	// crh.setNomeArquivo("");
	// crh.setNossoNumero(contaReceberRegistroArquivoVO.getContaReceberVO().getNossoNumero());
	// crh.setResponsavel(controleCobrancaVO.getResponsavel());
	// crh.setValorRecebimento(contaReceberRegistroArquivoVO.getContaReceberVO().getValorRecebido());
	// getFacadeFactory().getContaReceberHistoricoFacade().incluir(crh);
	// continue;
	// }
	// if (detalhe == null || detalhe.getCodigoContaReceber().intValue() == 0) {
	// continue;
	// }
	//
	// if (contaCorrenteVO == null || contaCorrenteVO.getCodigo().equals(0)) {
	// contaCorrenteVO =
	// getFacadeFactory().getContaCorrenteFacade().consultarContaCorrentePadraoPorContaReceberAgrupada(detalhe.getCodigoContaReceber(),
	// configuracaoFinanceiroVO);
	// }
	//
	// preencherContaReceberVOComArquivoRetorno(contaReceberRegistroArquivoVO.getContaReceberVO(),
	// detalhe, configuracaoFinanceiroVO, controleCobrancaVO.getResponsavel(),
	// contaReceberRegistroArquivoVO.getDiasVariacaoDataVencimento());
	// negociacaoRecebimentoVO =
	// criarNegociacaoRecebimentoVO(contaReceberRegistroArquivoVO.getContaReceberVO(),
	// configuracaoFinanceiroVO, controleCobrancaVO.getResponsavel());
	//
	// negociacaoRecebimentoVO.setData(Uteis.getDataMinutos(detalhe.getDataOcorrencia()));
	// negociacaoRecebimentoVO.setDataCreditoBoletoBancario(Uteis.getDataMinutos(detalhe.getDataCredito()));
	//
	// negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().add(criarFormaPagamentoNegociacaoRecebimentoVO(contaReceberRegistroArquivoVO.getContaReceberVO(),
	// configuracaoFinanceiroVO, detalhe, controleCobrancaVO.getResponsavel(),
	// contaCorrenteVO));
	// negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().add(criarContaReceberNegociacaoRecebimentoVO(contaReceberRegistroArquivoVO.getContaReceberVO(),
	// detalhe));
	// negociacaoRecebimentoVO.setValorTotal(contaReceberRegistroArquivoVO.getContaReceberVO().getValorRecebido());
	// negociacaoRecebimentoVO.setTipoPessoa(contaReceberRegistroArquivoVO.getContaReceberVO().getTipoPessoa());
	// negociacaoRecebimentoVO.setRecebimentoBoletoAutomatico(Boolean.TRUE);
	// if (negociacaoRecebimentoVO.getTipoPessoa().equals("AL")) {
	// negociacaoRecebimentoVO.setMatricula(contaReceberRegistroArquivoVO.getContaReceberVO().getMatriculaAluno().getMatricula());
	// }
	// contaReceberRegistroArquivoVO.getContaReceberVO().setSituacao("RE");
	// detalhe.setBoletoBaixado(true);
	// contaReceberRegistroArquivoVOs.set(index, contaReceberRegistroArquivoVO);
	// index++;
	// incluirNegociacaoRecebimentoControleCobranca(negociacaoRecebimentoVO,
	// configuracaoFinanceiroVO, controleCobrancaVO.getResponsavel());
	// getFacadeFactory().getRegistroDetalheFacade().alterar(detalhe,
	// controleCobrancaVO.getResponsavel());
	// contaReceberRegistroArquivoVO.getContaReceberVO().getRegistroArquivoVO().setCodigo(arquivo.getCodigo());
	// contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta já Recebida!");
	// contaReceberRegistroArquivoVO.getContaReceberVO().setGerarNegociacaoRecebimento(false);
	// getFacadeFactory().getContaReceberRegistroArquivoFacade().alterar(contaReceberRegistroArquivoVO,
	// controleCobrancaVO.getResponsavel());
	// map.put(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo(),
	// contaReceberRegistroArquivoVO.getContaReceberVO());
	// } else {
	// contaReceberRegistroArquivoVO.setContaReceberVO(map.get(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo()));
	// if
	// (!contaReceberRegistroArquivoVO.getContaReceberVO().isGerarNegociacaoRecebimento())
	// {
	// // insere no historico da contareceber a tenta de baixa do
	// // arquivo novamente, seja pela duplidade do arquivo ou seja
	// // pelo fato de ter renomeado o arquivo de retorno.
	// ContaReceberHistoricoVO crh = new ContaReceberHistoricoVO();
	// crh.setContaReceber(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo());
	// crh.setData(new Date());
	// crh.setMotivo("Conta Receber já processada. Tentativa em duplicidade!");
	// crh.setNomeArquivo("");
	// crh.setNossoNumero(contaReceberRegistroArquivoVO.getContaReceberVO().getNossoNumero());
	// crh.setResponsavel(controleCobrancaVO.getResponsavel());
	// crh.setValorRecebimento(contaReceberRegistroArquivoVO.getContaReceberVO().getValorRecebido());
	// getFacadeFactory().getContaReceberHistoricoFacade().incluir(crh);
	// continue;
	// }
	// getFacadeFactory().getContaReceberRegistroArquivoFacade().alterar(contaReceberRegistroArquivoVO,
	// controleCobrancaVO.getResponsavel());
	// }
	// // //System.out.println(contaReceberRegistroArquivo++);
	// }
	// arquivo.setContasBaixadas(true);
	// getFacadeFactory().getRegistroArquivoFacade().alterar(arquivo, false,
	// controleCobrancaVO.getResponsavel());
	// }

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void incluirNegociacaoRecebimentoControleCobranca(NegociacaoRecebimentoVO negociacaoRecebimentoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		HashMap<String, String> hashAux = new HashMap<String, String>();
		if (!hashAux.containsKey(negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().get(0).getContaReceber().getMatriculaAluno().getMatricula())) {
			hashAux.put(negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().get(0).getContaReceber().getMatriculaAluno().getMatricula(), "");
		} else {
			negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().get(0).getContaReceber().setControlarConcorrencia(Boolean.TRUE);
		}
		getFacadeFactory().getNegociacaoRecebimentoFacade().incluir(negociacaoRecebimentoVO, configuracaoFinanceiroVO, false, usuario);

	}

	// public List<NegociacaoRecebimentoVO>
	// criarNegociacaoRecebimentoVOs(List<ContaReceberVO> contaReceberVOs,
	// RegistroArquivoVO arquivo, ConfiguracaoFinanceiroVO
	// configuracaoFinanceiroVO, UsuarioVO responsavel) throws Exception {
	// NegociacaoRecebimentoVO negociacaoRecebimentoVO;
	// List<NegociacaoRecebimentoVO> negociacaoRecebimentoVOs = new
	// ArrayList<NegociacaoRecebimentoVO>(0);
	// RegistroDetalheVO detalhe;
	// int index = 0;
	// for (ContaReceberVO contaReceberVO : contaReceberVOs) {
	// detalhe = arquivo.getRegistroDetalhe(contaReceberVO.getCodigo());
	// if (!contaReceberVO.isGerarNegociacaoRecebimento()) {
	// continue;
	// }
	// if (detalhe == null || detalhe.getCodigoContaReceber().intValue() == 0) {
	// continue;
	// }
	// contaReceberVO =
	// getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(contaReceberVO.getCodigo(),
	// false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO,
	// responsavel);
	// preencherContaReceberVOComArquivoRetorno(contaReceberVO, detalhe,
	// configuracaoFinanceiroVO, responsavel);
	// negociacaoRecebimentoVO = criarNegociacaoRecebimentoVO(contaReceberVO,
	// configuracaoFinanceiroVO, responsavel);
	//
	// negociacaoRecebimentoVO.setData(detalhe.getDataOcorrencia());
	//
	// negociacaoRecebimentoVO.getFormaPagamentoNegociacaoRecebimentoVOs().add(criarFormaPagamentoNegociacaoRecebimentoVO(contaReceberVO,
	// configuracaoFinanceiroVO, detalhe, responsavel));
	// negociacaoRecebimentoVO.getContaReceberNegociacaoRecebimentoVOs().add(criarContaReceberNegociacaoRecebimentoVO(contaReceberVO,
	// detalhe));
	// negociacaoRecebimentoVO.setValorTotal(contaReceberVO.getValorRecebido());
	// negociacaoRecebimentoVO.setTipoPessoa(contaReceberVO.getTipoPessoa());
	// negociacaoRecebimentoVO.setRecebimentoBoletoAutomatico(Boolean.TRUE);
	// if (negociacaoRecebimentoVO.getTipoPessoa().equals("AL")) {
	// negociacaoRecebimentoVO.setMatricula(contaReceberVO.getMatriculaAluno().getMatricula());
	// }
	// contaReceberVO.setSituacao("RE");
	// negociacaoRecebimentoVOs.add(negociacaoRecebimentoVO);
	// detalhe.setBoletoBaixado(true);
	// contaReceberVOs.set(index, contaReceberVO);
	// index++;
	// }
	// return negociacaoRecebimentoVOs;
	// }
	//
	protected void preencherContaReceberVOComArquivoRetorno(ContaReceberVO contaReceberVO, RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, Integer diasVariacaoDataVencimento) throws Exception {
		contaReceberVO.setRealizandoRecebimento(true);
		Date dataVencimentoOriginal = null;
		if (configuracaoFinanceiroVO.getVencimentoParcelaDiaUtil()) {
			dataVencimentoOriginal = contaReceberVO.getDataVencimento();
		}
		verificarDataVencimentoUtilizarDiaUtil(contaReceberVO, configuracaoFinanceiroVO, usuarioVO);
		contaReceberVO.getCalcularValorFinal(detalhe.getDataOcorrencia(), configuracaoFinanceiroVO, false, detalhe.getDataOcorrencia(), usuarioVO);
		if (configuracaoFinanceiroVO.getVencimentoParcelaDiaUtil()) {
			contaReceberVO.setDataVencimento(dataVencimentoOriginal);
		}
		// Acrescentado regra para somente gerar pendência somente se o valor for maior que que o valor configurado na configuração financeira do sistema -> campo = ValorMinimoGerarPendenciaControleCobranca
		if (detalhe.getValorPago() < contaReceberVO.getValorRecebido() && (contaReceberVO.getValorRecebido() - detalhe.getValorPago()) > configuracaoFinanceiroVO.getValorMinimoGerarPendenciaControleCobranca()) {
			MapaPendenciasControleCobrancaVO mapaPendenciasControleCobrancaVO = new MapaPendenciasControleCobrancaVO();
			if (Uteis.isAtributoPreenchido(contaReceberVO.getMatriculaAluno().getMatricula())) {
				mapaPendenciasControleCobrancaVO.setMatricula(contaReceberVO.getMatriculaAluno());
			}
			mapaPendenciasControleCobrancaVO.setContaReceber(contaReceberVO);
			mapaPendenciasControleCobrancaVO.setValorDiferenca(contaReceberVO.getValorRecebido() - detalhe.getValorPago());
			mapaPendenciasControleCobrancaVO.setDataPagamento(detalhe.getDataOcorrencia());
			mapaPendenciasControleCobrancaVO.setJuro(contaReceberVO.getJuro());
			mapaPendenciasControleCobrancaVO.setMulta(contaReceberVO.getMulta());
			getFacadeFactory().getMapaPendenciasControleCobrancaFacade().incluir(mapaPendenciasControleCobrancaVO, usuarioVO);

			contaReceberVO.setTipoDescontoLancadoRecebimento("VA");
			contaReceberVO.setValorDescontoLancadoRecebimento(contaReceberVO.getValorRecebido() - detalhe.getValorPago());
			contaReceberVO.setValorCalculadoDescontoLancadoRecebimento(contaReceberVO.getValorRecebido() - detalhe.getValorPago());
		} else if (detalhe.getValorPago() > contaReceberVO.getValorRecebido()) {
			contaReceberVO.setAcrescimo(detalhe.getValorPago() - contaReceberVO.getValorRecebido());
		}
		contaReceberVO.setValorRecebido(detalhe.getValorPago());
		contaReceberVO.setTaxaBoleto(detalhe.getTarifaCobranca());		
	}

	protected NegociacaoRecebimentoVO criarNegociacaoRecebimentoVO(ContaReceberVO contaReceberVO, ContaCorrenteVO contaCorrenteVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO,  UsuarioVO usuarioVO) throws Exception {		
		ContaCorrenteVO contaCorrenteCaixa = null;
		if (Uteis.isAtributoPreenchido(contaCorrenteVO)) {
			contaCorrenteCaixa = contaCorrenteVO;
		} else {
			contaCorrenteCaixa = consultarContaCorrentePadrao(configuracaoFinanceiroVO, usuarioVO);
		}
		return getFacadeFactory().getNegociacaoRecebimentoFacade().criarNegociacaoRecebimentoVOPorBaixaAutomatica(contaReceberVO, contaCorrenteCaixa, configuracaoFinanceiroVO, usuarioVO);
	}

	private ContaCorrenteVO consultarContaCorrentePadrao(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		// ConfiguracaoFinanceiroVO configuracaoFinanceiroVO =
		// getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarConfiguracaoASerUsada(Uteis.NIVELMONTARDADOS_TODOS,
		// usuarioVO, null);
		if (Uteis.isAtributoPreenchido(configuracaoFinanceiroVO) && Uteis.isAtributoPreenchido(configuracaoFinanceiroVO.getContaCorrentePadraoControleCobranca())) {
			return configuracaoFinanceiroVO.getContaCorrentePadraoControleCobranca();
		}
		throw new ConsistirException("Não há Conta-Corrente padrão para Controle Cobrança na configuração padrão no sistema ou não há configuração vinculada a esta Unidade de Ensino.");
	}

	protected ContaReceberNegociacaoRecebimentoVO criarContaReceberNegociacaoRecebimentoVO(ContaReceberVO contaReceberVO, RegistroDetalheVO detalhe) throws Exception {
		ContaReceberNegociacaoRecebimentoVO contaReceberNegociacaoRecebimentoVO = new ContaReceberNegociacaoRecebimentoVO();
		contaReceberNegociacaoRecebimentoVO.setContaReceber(contaReceberVO);
		if (!contaReceberVO.getContaReceberAgrupada().equals(0)) {
			contaReceberNegociacaoRecebimentoVO.setValorTotal(contaReceberVO.getValor());
		} else {
			contaReceberNegociacaoRecebimentoVO.setValorTotal(detalhe.getValorPago());
		}
		return contaReceberNegociacaoRecebimentoVO;
	}

	public abstract ContaReceberVO consultarContaReceber(RegistroDetalheVO registroDetalheVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception;

	public abstract ContaReceberVO criarContaReceberVO(RegistroDetalheVO registroDetalheVO);

	public List<ContaReceberVO> listarContaReceberVOs(RegistroArquivoVO registroArquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO responsavel) throws Exception {
		ContaReceberVO contaReceberVO;
		List<ContaReceberVO> contaReceberVOs = new ArrayList<ContaReceberVO>(0);
		for (RegistroDetalheVO detalhe : registroArquivo.getRegistroDetalheVOs()) {
			contaReceberVO = consultarContaReceber(detalhe, configuracaoFinanceiroVO, responsavel);
			contaReceberVO.setObservacao("Conta Localizada!");

			if (contaReceberVO.getCodigo().intValue() == 0) {
				contaReceberVO = criarContaReceberVO(detalhe);
				contaReceberVO.setNossoNumero(detalhe.getIdentificacaoTituloEmpresa());
				contaReceberVO.setNrDocumento(detalhe.getNumeroDocumentoCobranca());
				contaReceberVO.setObservacao("Conta Não Localizada!");
				detalhe.setBoletoNaoEncontrado(true);
			}

			if (detalhe.getDataVencimentoTitulo() != null && (!detalhe.getDataVencimentoTitulo().equals(contaReceberVO.getDataVencimento()))) {
				contaReceberVO.setObservacao("Contas Localizadas (Data de vencimento da conta no sistema é divergente da data de vencimento no arquivo de retorno).");
			}

			if (contaReceberVO.getSituacao().equals(SituacaoContaReceber.RECEBIDO.getValor())) {
				contaReceberVO.setObservacao("Conta já Recebida!");
				contaReceberVO.setGerarNegociacaoRecebimento(false);
			} else {
				contaReceberVO.setGerarNegociacaoRecebimento(true);
			}
			/**
			 * Conta que foi identificada pelo arquivo de retorno, foi paga por
			 * um boleto bancário Logo o atributo recebimentoBancario é setado
			 * como true.
			 */
			contaReceberVO.setRecebimentoBancario(true);
			contaReceberVO.setValorRecebido(detalhe.getValorPago());
			contaReceberVOs.add(contaReceberVO);
		}
		return contaReceberVOs;
	}

	public ContaReceberVO executarCriacaoContaReceberVOs(RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO responsavel) throws Exception {
		ContaReceberVO contaReceberVO;

		contaReceberVO = consultarContaReceber(detalhe, configuracaoFinanceiroVO, responsavel);
		contaReceberVO.setObservacao("Conta Localizada!");

		if (contaReceberVO.getCodigo().intValue() == 0) {
			contaReceberVO = criarContaReceberVO(detalhe);
			contaReceberVO.setNossoNumero(detalhe.getIdentificacaoTituloEmpresa());
			contaReceberVO.setNrDocumento(detalhe.getNumeroDocumentoCobranca());
			contaReceberVO.setObservacao("Conta Não Localizada!");
			detalhe.setBoletoNaoEncontrado(true);
		}

		if (detalhe.getDataVencimentoTitulo() != null && (!detalhe.getDataVencimentoTitulo().equals(contaReceberVO.getDataVencimento()))) {
			contaReceberVO.setObservacao("Contas Localizadas (Data de vencimento da conta no sistema é divergente da data de vencimento no arquivo de retorno).");
		}

		if (contaReceberVO.getSituacao().equals(SituacaoContaReceber.RECEBIDO.getValor())) {
			contaReceberVO.setObservacao("Conta já Recebida!");
			contaReceberVO.setGerarNegociacaoRecebimento(false);
		} else {
			contaReceberVO.setGerarNegociacaoRecebimento(true);
		}
		/**
		 * Conta que foi identificada pelo arquivo de retorno, foi paga por um
		 * boleto bancário Logo o atributo recebimentoBancario é setado como
		 * true.
		 */
		contaReceberVO.setRecebimentoBancario(true);
		contaReceberVO.setValorRecebido(detalhe.getValorPago());

		// contaReceberVO.setContaReceberRegistroArquivo(contaReceberVO.getCodigo());
		return contaReceberVO;
	}
	
	public void validarContaCorrenteExistenteParaCriarContaReceberRegistroArquivo(ControleCobrancaVO controleCobrancaVO, RegistroArquivoVO registroArquivoVO, Boolean bancoBrasil, String caminho, UsuarioVO usuario) throws Exception {
		List<ContaCorrenteVO> listaContaCorrenteVOs = getFacadeFactory().getContaCorrenteFacade().consultarPorBancoPorNumeroContaCorrentePorDigitoContaCorrente(Bancos.getEnum(controleCobrancaVO.getBanco()).getNumeroBanco(), registroArquivoVO.getRegistroHeader().getNumeroConta().toString(), registroArquivoVO.getRegistroHeader().getDigitoConta(), registroArquivoVO.getRegistroHeader().getCodigoConvenioBanco(), 0, false, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
		if (listaContaCorrenteVOs.isEmpty()){
			throw new Exception("Não foi encontrado nenhuma conta corrente com o número : "+registroArquivoVO.getRegistroHeader().getNumeroConta().toString());
		}
		controleCobrancaVO.setListaContaCorrenteVOs(listaContaCorrenteVOs);
		registroArquivoVO.setListaContaCorrenteVOs(listaContaCorrenteVOs);
		if (listaContaCorrenteVOs.size() == 1) {
			controleCobrancaVO.setContaCorrenteVO(listaContaCorrenteVOs.get(0));
		}
		registroArquivoVO.setContaCorrenteVO(controleCobrancaVO.getContaCorrenteVO());
	   // executarCriacaoContaReceberRegistroArquivoVOs(registroArquivoVO, bancoBrasil, caminho);	
	}
	
	

	public void executarCriacaoContaReceberRegistroArquivoVOs(RegistroArquivoVO registroArquivoVO, ProgressBarVO progressBarVO, Boolean bancoBrasil, String caminho, UsuarioVO usuario) throws Exception {
		Map<BigInteger, ContaReceberVO> contaReceberVOs = getFacadeFactory().getContaReceberFacade().consultarPorNossoNumeroControleCobranca(registroArquivoVO, progressBarVO, bancoBrasil);
		if(progressBarVO != null) {
			progressBarVO.setProgresso(0L);
		}
		registroArquivoVO.setTotalTaxaPagaAluno(0.0);
		registroArquivoVO.setTotalTaxaBoletoCobradoBanco(0.0);
		HashMap<String, String> listaRejeicao = getFacadeFactory().getControleRemessaFacade().consultarMotivoRejeicaoBanco(registroArquivoVO.getContaCorrenteVO().getAgencia().getBanco().getCodigo(), registroArquivoVO.getContaCorrenteVO().getCnab());
		for (RegistroDetalheVO detalhe : registroArquivoVO.getRegistroDetalheVOs()) {
			try {
				if (detalhe != null && detalhe.getSituacaoRegistroDetalheEnum().isSituacaoBaixado()) {
					executarCriacaoContaReceberRegistroArquivoVOsBaixadas(detalhe, registroArquivoVO, contaReceberVOs, progressBarVO, bancoBrasil, listaRejeicao, usuario);
				}else if (detalhe != null && (detalhe.getSituacaoRegistroDetalheEnum().isSituacaoConfirmado() || detalhe.getSituacaoRegistroDetalheEnum().isSituacaoRejeitado())) {
					ControleRemessaContaReceberVO crcr = getFacadeFactory().getControleRemessaContaReceberFacade().consultaRapidaContaArquivoRemessaPorNossoNumeroContaReceber(registroArquivoVO.getListaContaCorrenteVOs(), detalhe.getIdentificacaoTituloEmpresa());
					if (crcr != null && crcr.getCodigo().intValue() > 0 && !crcr.getSituacaoControleRemessaContaReceber().equals(SituacaoControleRemessaContaReceberEnum.REMETIDA)) {
						getFacadeFactory().getControleRemessaFacade().realizarAtualizacaoControleRemessaPorArquivoRetorno(detalhe, crcr, listaRejeicao, usuario);
					}
				}
				if (progressBarVO != null) {
					progressBarVO.setStatus(" Processando Contas Localizadas - ".concat(progressBarVO.getProgresso().toString()).concat(" de ").concat(progressBarVO.getMaxValue().toString()).concat(" ("+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(progressBarVO.getPorcentagem(), 0)+"%)"));
				}
			} finally {
				if (progressBarVO != null) {
					progressBarVO.incrementarSemStatus();
				}
			}
		}
		
		
		//Não remover esse codigo sem falar com Rodrigo ou Pedro Andrade.
//		if (!listaControleRemessaContaReceber.isEmpty()) {
//			try {
//				/// processar remessa de contas
//				ControleRemessaVO controleRemessa = new ControleRemessaVO();
//				List<ContaCorrenteVO> listaContaCorrente = getFacadeFactory().getContaCorrenteFacade().consultarPorNossoNumero(listaControleRemessaContaReceber, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, new UsuarioVO());
//				Iterator i = listaContaCorrente.iterator();
//				while (i.hasNext()) {
//					ContaCorrenteVO conta = (ContaCorrenteVO)i.next();
//					controleRemessa.setContaCorrenteVO(conta);
//					controleRemessa.getBancoVO().setNrBanco(conta.getAgencia().getBanco().getNrBanco());
//					try {
//						getFacadeFactory().getControleRemessaFacade().processarArquivo(controleRemessa, listaControleRemessaContaReceber, caminho, new ConfiguracaoFinanceiroVO(), new UsuarioVO());
//					} catch (Exception e) {
//						System.out.println("ERRO REMESSA 01 = " + e.getMessage());
//					}
//				}
//			} catch (Exception e) {
//				System.out.println("ERRO REMESSA 02 = " + e.getMessage());
//			}
//		}
	}
	
	public void executarCriacaoContaReceberRegistroArquivoVOsConfirmadasOuRejeitada(RegistroDetalheVO detalhe, RegistroArquivoVO registroArquivoVO, List<ControleRemessaContaReceberVO> listaControleRemessaContaReceber) throws Exception {
		// localiza objeto para processar remessa da conta.
		ControleRemessaContaReceberVO crcr = getFacadeFactory().getControleRemessaContaReceberFacade().consultaRapidaContaArquivoRemessaPorNossoNumeroContaReceber(registroArquivoVO.getListaContaCorrenteVOs(), detalhe.getIdentificacaoTituloEmpresa());
		if (listaControleRemessaContaReceber.stream().noneMatch(p-> p.getCodigo().equals(crcr.getCodigo())) && crcr.getCodigo().intValue() > 0) {
			listaControleRemessaContaReceber.add(crcr);							
		}
	}
	
	public void executarCriacaoContaReceberRegistroArquivoVOsBaixadas(RegistroDetalheVO detalhe, RegistroArquivoVO registroArquivoVO, Map<BigInteger, ContaReceberVO> contaReceberVOs, ProgressBarVO progressBarVO, Boolean bancoBrasil, HashMap<String, String> listaRejeicao, UsuarioVO usuario ) throws Exception {

		/*
		 * @author Wendel Rodrigues
		 * @version 5.0.3.1
		 * @since 19 de março de 2015
		 * A rotina padrão do banco após receber o arquivo remessa é enviar a instituição um arquivo de retorno para confirmação de entrada dos títulos.
		 * Porém nem todas as contas que voltam no arquivo de remessa possuem valor, pelo motivo de ainda não terem sido recebidas pelo banco. 
		 * Ao processar o arquivo de retorno o sistema não pode processar as contas com o valor pago menor ou igual à zero.
		 */
		
		if (detalhe.getContaReceberAgrupada()) {
			return;
		}
		registroArquivoVO.setTotalTaxaBoletoCobradoBanco(registroArquivoVO.getTotalTaxaBoletoCobradoBanco() + detalhe.getTarifaCobranca());

		ContaReceberRegistroArquivoVO contaReceberRegistroArquivoVO = new ContaReceberRegistroArquivoVO();

		if (detalhe.getIdentificacaoTituloEmpresa() != null && !detalhe.getIdentificacaoTituloEmpresa().equals("")) {
			if (detalhe.getIdentificacaoTituloEmpresa().contains("P")) {
				contaReceberRegistroArquivoVO.setContaReceberVO(contaReceberVOs.get(new BigInteger(detalhe.getIdentificacaoTituloEmpresa().replace("P", ""))));
			} else {
				contaReceberRegistroArquivoVO.setContaReceberVO(contaReceberVOs.get(new BigInteger(detalhe.getIdentificacaoTituloEmpresa())));
			}
			if ((contaReceberRegistroArquivoVO.getContaReceberVO() == null || contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo().intValue() == 0) && (bancoBrasil)) {
				contaReceberRegistroArquivoVO.setContaReceberVO(contaReceberVOs.get(new BigInteger(detalhe.getIdentificacaoTituloEmpresa().substring(7))));
			}
			if ((contaReceberRegistroArquivoVO.getContaReceberVO() == null || contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo().intValue() == 0) && detalhe.getIdentificacaoTituloEmpresa().substring(0, 1).equals("0")) {
				if (detalhe.getIdentificacaoTituloEmpresa().contains("P")) {
					contaReceberRegistroArquivoVO.setContaReceberVO(contaReceberVOs.get(new BigInteger(detalhe.getIdentificacaoTituloEmpresa().replace("P", "").substring(1, 4) + "0" + detalhe.getIdentificacaoTituloEmpresa().replace("P", "").substring(4))));
				} else {
					contaReceberRegistroArquivoVO.setContaReceberVO(contaReceberVOs.get(new BigInteger(detalhe.getIdentificacaoTituloEmpresa().substring(1, 4) + "0" + detalhe.getIdentificacaoTituloEmpresa().substring(4))));
				}
			}
			if ((contaReceberRegistroArquivoVO.getContaReceberVO() == null || contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo().intValue() == 0) && detalhe.getIdentificacaoTituloEmpresa().substring(0, 1).equals("0")) {
				if (detalhe.getIdentificacaoTituloEmpresa().contains("P")) {
					contaReceberRegistroArquivoVO.setContaReceberVO(contaReceberVOs.get(new BigInteger(1 + detalhe.getIdentificacaoTituloEmpresa().replace("P", ""))));
				} else {
					contaReceberRegistroArquivoVO.setContaReceberVO(contaReceberVOs.get(new BigInteger(1 + detalhe.getIdentificacaoTituloEmpresa())));
				}
			}
			if ((contaReceberRegistroArquivoVO.getContaReceberVO() == null || contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo().intValue() == 0)) {
				// Procurar contas da PROCESSUS que foram geradas com 13 dígitos mas deveriam ser 16 dígitos
				// Nesse caso vem no arquivo de retorno está com 16 dígitos, mas no sistema está apenas com 13
				// Autor Carlos
				// 17/03/2014
				if (detalhe.getIdentificacaoTituloEmpresa().length() > 13) {
					if (detalhe.getIdentificacaoTituloEmpresa().contains("P")) {
						contaReceberRegistroArquivoVO.setContaReceberVO(contaReceberVOs.get(new BigInteger(detalhe.getIdentificacaoTituloEmpresa().replace("P", "").substring(0, 13))));
					} else {
						contaReceberRegistroArquivoVO.setContaReceberVO(contaReceberVOs.get(new BigInteger(detalhe.getIdentificacaoTituloEmpresa().substring(0, 13))));
					}
				}
			}
			contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta Localizada!");
			if (detalhe.getCarteira() == 112 && registroArquivoVO.getRegistroHeader().getCodigoBanco() == "341") {
				if ((contaReceberRegistroArquivoVO.getContaReceberVO() == null || contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo().intValue() == 0)) {
					if (detalhe.getIdentificacaoTituloBanco().contains("P")) {
						contaReceberRegistroArquivoVO.setContaReceberVO(contaReceberVOs.get(new BigInteger(detalhe.getIdentificacaoTituloBanco().replace("P", ""))));
					} else {
						contaReceberRegistroArquivoVO.setContaReceberVO(contaReceberVOs.get(new BigInteger(detalhe.getIdentificacaoTituloBanco())));
					}
				}
			}
			if (contaReceberRegistroArquivoVO.getContaReceberVO() == null || contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo().intValue() == 0) {
				contaReceberRegistroArquivoVO.setContaReceberVO(criarContaReceberVO(detalhe));
				contaReceberRegistroArquivoVO.getContaReceberVO().setNossoNumero(detalhe.getIdentificacaoTituloEmpresa());
				contaReceberRegistroArquivoVO.getContaReceberVO().setNrDocumento(detalhe.getNumeroDocumentoCobranca());
				contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta Não Localizada!");
				detalhe.setCodigoContaReceber(0);
				detalhe.setBoletoNaoEncontrado(true);
			} else {
				detalhe.setCodigoContaReceber(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo());
				registroArquivoVO.setTotalTaxaPagaAluno(registroArquivoVO.getTotalTaxaPagaAluno() + contaReceberRegistroArquivoVO.getContaReceberVO().getTaxaBoleto());
				if (detalhe.getValorPago().equals(0.0) && detalhe.getCodigoMovimentoRemessaRetorno() != 2 && detalhe.getCodigoMovimentoRemessaRetorno() != 28) {
					contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta com problemas no valor recebido!");
				}
			}

			// if (detalhe.getValorPago().equals(0.0)) {
			// contaReceberRegistroArquivoVO.setContaReceberVO(criarContaReceberVO(detalhe));
			// contaReceberRegistroArquivoVO.getContaReceberVO().setNossoNumero(detalhe.getIdentificacaoTituloEmpresa());
			// contaReceberRegistroArquivoVO.getContaReceberVO().setNrDocumento(detalhe.getNumeroDocumentoCobranca());
			// contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta Não Paga!");
			// detalhe.setCodigoContaReceber(0);
			// detalhe.setBoletoNaoEncontrado(true);
			// }

			if (detalhe.getDataVencimentoTitulo() != null && (!detalhe.getDataVencimentoTitulo().equals(contaReceberRegistroArquivoVO.getContaReceberVO().getDataVencimento()))) {

				contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Contas Localizadas (Data de vencimento da conta no sistema é divergente da data de vencimento no arquivo de retorno).");
			}

			if (contaReceberRegistroArquivoVO.getContaReceberVO().getSituacao().equals(SituacaoContaReceber.RECEBIDO.getValor())) {

				contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta já Recebida!");
				contaReceberRegistroArquivoVO.getContaReceberVO().setGerarNegociacaoRecebimento(false);
				contaReceberRegistroArquivoVO.setContaRecebidaDuplicidade(Boolean.TRUE);						
				contaReceberRegistroArquivoVO.setContaRecebidaNegociada(Boolean.FALSE);						
			} else if (contaReceberRegistroArquivoVO.getContaReceberVO().getSituacao().equals(SituacaoContaReceber.NEGOCIADO.getValor())) {
				contaReceberRegistroArquivoVO.setContaRecebidaDuplicidade(Boolean.FALSE);					
				contaReceberRegistroArquivoVO.setContaRecebidaNegociada(Boolean.TRUE);					
				contaReceberRegistroArquivoVO.getContaReceberVO().setGerarNegociacaoRecebimento(true);
			} else {
				contaReceberRegistroArquivoVO.setContaRecebidaDuplicidade(Boolean.FALSE);					
				contaReceberRegistroArquivoVO.setContaRecebidaNegociada(Boolean.FALSE);					
				contaReceberRegistroArquivoVO.getContaReceberVO().setGerarNegociacaoRecebimento(true);
			}
			/**
			 * Conta que foi identificada pelo arquivo de retorno, foi paga por um boleto bancário Logo o atributo recebimentoBancario é setado como true.
			 */
			contaReceberRegistroArquivoVO.getContaReceberVO().setRecebimentoBancario(true);
			contaReceberRegistroArquivoVO.getContaReceberVO().setDataCredito(detalhe.getDataCredito());					
			if (detalhe.getValorPago() > 0) {
				contaReceberRegistroArquivoVO.getContaReceberVO().setValorRecebido(detalhe.getValorPago());
			}
			if (Uteis.isAtributoPreenchido(detalhe.getValorPago()) && detalhe.getValorPago() > 0) {
				registroArquivoVO.getContaReceberRegistroArquivoVOs().add(contaReceberRegistroArquivoVO);
				if (contaReceberRegistroArquivoVO.getContaReceberVO().getSituacao().equals("NE") || contaReceberRegistroArquivoVO.getContaRecebidaNegociada()) {
					registroArquivoVO.getContaReceberNegociadaRegistroArquivoVOs().add(contaReceberRegistroArquivoVO);
				}
			}
			// localiza objeto para processar remessa da conta.
			ControleRemessaContaReceberVO crcr = getFacadeFactory().getControleRemessaContaReceberFacade().consultaRapidaContaArquivoRemessaPorNossoNumeroContaReceber(registroArquivoVO.getListaContaCorrenteVOs(), contaReceberRegistroArquivoVO.getContaReceberVO().getNossoNumero());
			if (crcr != null && crcr.getCodigo().intValue() > 0 && !crcr.getSituacaoControleRemessaContaReceber().equals(SituacaoControleRemessaContaReceberEnum.REMETIDA)) {											
				getFacadeFactory().getControleRemessaFacade().realizarAtualizacaoControleRemessaPorArquivoRetorno(detalhe, crcr, listaRejeicao, usuario);
			}
		}	
	}
	
	public String getCaminhoPastaArquivosCobranca() {
		return getCaminhoPastaWeb() + File.separator + "arquivos";
	}

	public String getCaminhoPastaWeb() {
		if (diretorioPastaWeb == null || diretorioPastaWeb.equals("")) {
			ServletContext servletContext = (ServletContext) this.context().getExternalContext().getContext();
			diretorioPastaWeb = servletContext.getRealPath("");
		}
		return diretorioPastaWeb;
	}	

	public List<RegistroDetalheVO> realizarCriacaoRegistroDetalheMedianteContaAgrupada(RegistroDetalheVO detalhe) {
		List<RegistroDetalheVO> listaRegistroDetalheVOs = null;
		List<ContaReceberVO> listaContaReceberVos = getFacadeFactory().getContaReceberFacade().consultarContaReceberPorNossoNumeroContaAgrupada(detalhe.getIdentificacaoTituloEmpresa());
		if (!listaContaReceberVos.isEmpty()) {
			listaRegistroDetalheVOs = new ArrayList<RegistroDetalheVO>(0);
			for (ContaReceberVO contaReceberVO : listaContaReceberVos) {
				RegistroDetalheVO registroDetalheVO = new RegistroDetalheVO();
				registroDetalheVO.setCodigoContaReceber(contaReceberVO.getCodigo());
				registroDetalheVO.setIdentificacaoTituloEmpresa(contaReceberVO.getNossoNumero());
				registroDetalheVO.setDataOcorrencia(detalhe.getDataOcorrencia());
				registroDetalheVO.setDataCredito(detalhe.getDataCredito());
				registroDetalheVO.setValorPago(detalhe.getValorPago());
				registroDetalheVO.setValorDesconto(detalhe.getValorDesconto());
				registroDetalheVO.setJurosMora(detalhe.getJurosMora());
				registroDetalheVO.setContaReceberAgrupada(Boolean.TRUE);
				listaRegistroDetalheVOs.add(registroDetalheVO);
			}
		}
		return listaRegistroDetalheVOs;
	}

	public void executarCriacaoContaReceberRegistroArquivoContaAgrupadaVOs(RegistroArquivoVO registroArquivoVO, Boolean bancoBrasil) throws Exception {
		Map<BigInteger, ContaReceberAgrupadaVO> contaReceberAgrupadaVOs = getFacadeFactory().getContaReceberAgrupadaFacade().consultarPorNossoNumeroControleCobranca(registroArquivoVO, bancoBrasil);
		registroArquivoVO.setTotalTaxaPagaAluno(0.0);
		registroArquivoVO.setTotalTaxaBoletoCobradoBanco(0.0);
		for (RegistroDetalheVO detalhe : registroArquivoVO.getRegistroDetalheContaAgrupadaVOs()) {
			if (!detalhe.getContaReceberAgrupada()) {
				continue;
			}
			registroArquivoVO.setTotalTaxaBoletoCobradoBanco(registroArquivoVO.getTotalTaxaBoletoCobradoBanco() + detalhe.getTarifaCobranca());

			ContaReceberRegistroArquivoVO contaReceberRegistroArquivoVO = new ContaReceberRegistroArquivoVO();

			if (detalhe.getIdentificacaoTituloEmpresa() != null && !detalhe.getIdentificacaoTituloEmpresa().equals("")) {

				ContaReceberAgrupadaVO contaReceberAgrupadaVO = contaReceberAgrupadaVOs.get(new BigInteger(detalhe.getIdentificacaoTituloEmpresa()));

				if ((contaReceberAgrupadaVO == null || contaReceberAgrupadaVO.getCodigo().intValue() == 0) && (bancoBrasil)) {
					contaReceberAgrupadaVO = contaReceberAgrupadaVOs.get(new BigInteger(detalhe.getIdentificacaoTituloEmpresa().substring(7)));
				}
				if ((contaReceberAgrupadaVO == null || contaReceberAgrupadaVO.getCodigo().intValue() == 0) && detalhe.getIdentificacaoTituloEmpresa().substring(0, 1).equals("0")) {
					contaReceberAgrupadaVO = contaReceberAgrupadaVOs.get(new BigInteger(detalhe.getIdentificacaoTituloEmpresa().substring(1, 4) + "0" + detalhe.getIdentificacaoTituloEmpresa().substring(4)));
				}

				if (contaReceberAgrupadaVO == null || contaReceberAgrupadaVO.getCodigo().intValue() == 0) {
					contaReceberRegistroArquivoVO.setContaReceberVO(criarContaReceberVO(detalhe));
					contaReceberRegistroArquivoVO.getContaReceberVO().setNossoNumero(detalhe.getIdentificacaoTituloEmpresa());
					contaReceberRegistroArquivoVO.getContaReceberVO().setNrDocumento(detalhe.getNumeroDocumentoCobranca());
					contaReceberRegistroArquivoVO.setContaReceberAgrupada(Boolean.TRUE);
					contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta Não Localizada!");
					detalhe.setCodigoContaReceber(0);
					detalhe.setBoletoNaoEncontrado(true);
				} else {
					contaReceberRegistroArquivoVO.setContaReceberVO(criarContaReceberContaAgrupadaVO(detalhe, contaReceberAgrupadaVO));
					contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta Localizada!");
					detalhe.setCodigoContaReceber(contaReceberAgrupadaVO.getCodigo());
					registroArquivoVO.setTotalTaxaPagaAluno(registroArquivoVO.getTotalTaxaPagaAluno() + contaReceberRegistroArquivoVO.getContaReceberVO().getTaxaBoleto());
				}

				if (detalhe.getValorPago().equals(0.0)) {
					contaReceberRegistroArquivoVO.setContaReceberVO(criarContaReceberVO(detalhe));
					contaReceberRegistroArquivoVO.getContaReceberVO().setNossoNumero(detalhe.getIdentificacaoTituloEmpresa());
					contaReceberRegistroArquivoVO.getContaReceberVO().setNrDocumento(detalhe.getNumeroDocumentoCobranca());
					contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta Não Paga!");
					detalhe.setCodigoContaReceber(0);
					detalhe.setBoletoNaoEncontrado(true);
				}

				if (detalhe.getDataVencimentoTitulo() != null  && contaReceberAgrupadaVO.getDataVencimento() != null && (!detalhe.getDataVencimentoTitulo().equals(contaReceberAgrupadaVO.getDataVencimento()))) {

					contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Contas Localizadas (Data de vencimento da conta no sistema é divergente da data de vencimento no arquivo de retorno).");
				}

				if (contaReceberAgrupadaVO != null && contaReceberAgrupadaVO.getSituacao() != null && contaReceberAgrupadaVO.getSituacao().getValor().equals(SituacaoContaReceber.RECEBIDO.getValor())) {

					contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta já Recebida!");
					contaReceberRegistroArquivoVO.getContaReceberVO().setGerarNegociacaoRecebimento(false);
				} else {
					contaReceberRegistroArquivoVO.getContaReceberVO().setGerarNegociacaoRecebimento(true);
				}
				/**
				 * Conta que foi identificada pelo arquivo de retorno, foi paga
				 * por um boleto bancário Logo o atributo recebimentoBancario é
				 * setado como true.
				 */
				contaReceberRegistroArquivoVO.getContaReceberVO().setRecebimentoBancario(true);
				contaReceberRegistroArquivoVO.getContaReceberVO().setValorRecebido(detalhe.getValorPago());
				contaReceberRegistroArquivoVO.setContaReceberAgrupada(Boolean.TRUE);
				registroArquivoVO.getContaReceberRegistroArquivoVOs().add(contaReceberRegistroArquivoVO);
			}
		}

	}

	public ContaReceberVO criarContaReceberContaAgrupadaVO(RegistroDetalheVO registroDetalhe, ContaReceberAgrupadaVO contaReceberAgrupadaVO) {
		ContaReceberVO contaReceberVO = new ContaReceberVO();

		contaReceberVO.setNossoNumero(registroDetalhe.getIdentificacaoTituloEmpresa());
		contaReceberVO.setNrDocumento(registroDetalhe.getNumeroDocumentoCobranca());
		contaReceberVO.setJuro(registroDetalhe.getJurosMora());
		contaReceberVO.setDataVencimento(registroDetalhe.getDataVencimentoTitulo());
		contaReceberVO.setValor(registroDetalhe.getValorNominalTitulo());
		contaReceberVO.setValorRecebido(registroDetalhe.getValorPago());
		contaReceberVO.setPessoa(contaReceberAgrupadaVO.getPessoa());
		contaReceberVO.setCodigo(contaReceberAgrupadaVO.getCodigo());
		contaReceberVO.setSituacao(contaReceberAgrupadaVO.getSituacao().getValor());
		return contaReceberVO;
	}

	public void executarCriacaoContaReceberRegistroArquivoRetornoRemessaVOs(RegistroArquivoVO registroArquivoVO, ProgressBarVO progressBarVO, Boolean bancoBrasil) throws Exception {
		Map<BigInteger, ContaReceberVO> contaReceberVOs = getFacadeFactory().getContaReceberFacade().consultarPorNossoNumeroControleCobranca(registroArquivoVO, progressBarVO, bancoBrasil);

		for (RegistroDetalheVO detalhe : registroArquivoVO.getRegistroDetalheVOs()) {
			ContaReceberRegistroArquivoVO contaReceberRegistroArquivoVO = new ContaReceberRegistroArquivoVO();

			if (detalhe.getIdentificacaoTituloEmpresa() != null && !detalhe.getIdentificacaoTituloEmpresa().equals("")) {
				contaReceberRegistroArquivoVO.setContaReceberVO(contaReceberVOs.get(new BigInteger(detalhe.getIdentificacaoTituloEmpresa())));
				if ((contaReceberRegistroArquivoVO.getContaReceberVO() == null || contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo().intValue() == 0) && (bancoBrasil)) {
					contaReceberRegistroArquivoVO.setContaReceberVO(contaReceberVOs.get(new BigInteger(detalhe.getIdentificacaoTituloEmpresa().substring(7))));
				}
				if ((contaReceberRegistroArquivoVO.getContaReceberVO() == null || contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo().intValue() == 0) && detalhe.getIdentificacaoTituloEmpresa().substring(0, 1).equals("0")) {
					contaReceberRegistroArquivoVO.setContaReceberVO(contaReceberVOs.get(new BigInteger(detalhe.getIdentificacaoTituloEmpresa().substring(1, 4) + "0" + detalhe.getIdentificacaoTituloEmpresa().substring(4))));
				}

				detalhe.setCodigoContaReceber(contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo());

				if (detalhe.getDataVencimentoTitulo() != null && (!detalhe.getDataVencimentoTitulo().equals(contaReceberRegistroArquivoVO.getContaReceberVO().getDataVencimento()))) {

					contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Contas Localizadas (Data de vencimento da conta no sistema é divergente da data de vencimento no arquivo de retorno).");
				}
				/**
				 * Conta que foi identificada pelo arquivo de retorno, foi paga
				 * por um boleto bancário Logo o atributo recebimentoBancario é
				 * setado como true.
				 */
				contaReceberRegistroArquivoVO.getContaReceberVO().setRecebimentoBancario(true);
				contaReceberRegistroArquivoVO.getContaReceberVO().setValorRecebido(detalhe.getValorPago());
				if (!detalhe.getMotivoRegeicao().trim().equals("") && !detalhe.getMotivoRegeicao().equals("00000000")) {
					contaReceberRegistroArquivoVO.setMotivoRejeicao(detalhe.getMotivoRegeicao());
					contaReceberRegistroArquivoVO.getContaReceberVO().setNossoNumeroBanco("");
					registroArquivoVO.getContaReceberRegistroArquivoRejeitadasVOs().add(contaReceberRegistroArquivoVO);
				} else {
					contaReceberRegistroArquivoVO.getContaReceberVO().setNossoNumeroBanco(detalhe.getIdentificacaoTituloBanco());
					registroArquivoVO.getContaReceberRegistroArquivoVOs().add(contaReceberRegistroArquivoVO);
				}
			}
		}

	}
	// public ContaReceberRegistroArquivoVO
	// executarCriacaoContaReceberRegistroArquivoVOs(RegistroDetalheVO detalhe,
	// ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO responsavel)
	// throws Exception {
	// ContaReceberRegistroArquivoVO contaReceberRegistroArquivoVO = new
	// ContaReceberRegistroArquivoVO();
	//
	// contaReceberRegistroArquivoVO.setContaReceberVO(consultarContaReceber(detalhe,
	// configuracaoFinanceiroVO, responsavel));
	// contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta Localizada!");
	//
	// if
	// (contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo().intValue()
	// == 0) {
	// contaReceberRegistroArquivoVO.setContaReceberVO(criarContaReceberVO(detalhe));
	// contaReceberRegistroArquivoVO.getContaReceberVO().setNossoNumero(detalhe.getIdentificacaoTituloEmpresa());
	// contaReceberRegistroArquivoVO.getContaReceberVO().setNrDocumento(detalhe.getNumeroDocumentoCobranca());
	// contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta Não Localizada!");
	// detalhe.setBoletoNaoEncontrado(true);
	// }
	//
	// if (detalhe.getDataVencimentoTitulo() != null &&
	// (!detalhe.getDataVencimentoTitulo().equals(contaReceberRegistroArquivoVO.getContaReceberVO().getDataVencimento())))
	// {
	// contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta com problemas na data de vencimento!");
	// }
	//
	// if
	// (contaReceberRegistroArquivoVO.getContaReceberVO().getSituacao().equals(SituacaoContaReceber.RECEBIDO.getValor()))
	// {
	// contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta já Recebida!");
	// contaReceberRegistroArquivoVO.getContaReceberVO().setGerarNegociacaoRecebimento(false);
	// } else {
	// contaReceberRegistroArquivoVO.getContaReceberVO().setGerarNegociacaoRecebimento(true);
	// }
	// /**
	// * Conta que foi identificada pelo arquivo de retorno, foi paga por um
	// boleto bancário Logo o atributo recebimentoBancario é setado como true.
	// */
	// contaReceberRegistroArquivoVO.getContaReceberVO().setRecebimentoBancario(true);
	// contaReceberRegistroArquivoVO.getContaReceberVO().setValorRecebido(detalhe.getValorPago());
	//
	// //
	// contaReceberVO.setContaReceberRegistroArquivo(contaReceberVO.getCodigo());
	// return contaReceberRegistroArquivoVO;
	// }
	
	public void verificarDataVencimentoUtilizarDiaUtil(ContaReceberVO contaReceberVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		if (configuracaoFinanceiroVO.getVencimentoParcelaDiaUtil()) {
			contaReceberVO.getDataOriginalVencimento();
			contaReceberVO.setDataVencimentoDiaUtil(getFacadeFactory().getContaReceberFacade().obterDataVerificandoDiaUtil(contaReceberVO.getDataVencimento(), contaReceberVO.getUnidadeEnsino().getCidade().getCodigo(), usuarioVO));
			contaReceberVO.setDataVencimento(contaReceberVO.getDataVencimentoDiaUtil());
		}
	}
}
