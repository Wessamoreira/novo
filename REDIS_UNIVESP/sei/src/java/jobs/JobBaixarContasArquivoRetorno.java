package jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ContaReceberRegistroArquivoVO;
import negocio.comuns.financeiro.ControleCobrancaVO;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoArquivoRetornoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoArquivoRetorno;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

public class JobBaixarContasArquivoRetorno extends ControleAcesso implements Runnable {

	private ControleCobrancaVO controleCobrancaVO;
	private ConfiguracaoFinanceiroVO configuracaoFinanceiroSistemaVO;
	private UsuarioVO usuarioVO;
	private ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO;
	private ComunicacaoInternaVO comunicacaoInternaVO;
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
	private String situacao;
	private ContaCorrenteVO contaCorrenteVO;

	public JobBaixarContasArquivoRetorno(ControleCobrancaVO controleCobranca, ConfiguracaoFinanceiroVO configuracaoFinanceiroSistemaVO, UsuarioVO usuario) {
		this.controleCobrancaVO = controleCobranca;
		this.configuracaoFinanceiroSistemaVO = configuracaoFinanceiroSistemaVO;
		this.usuarioVO = usuario;
		this.contaCorrenteVO = controleCobranca.getContaCorrenteVO();
	}

	private Map<Integer, List<ContaReceberRegistroArquivoVO>> realizarSeparacaoContaReceberPorLote(List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs) {
		Map<Integer, List<ContaReceberRegistroArquivoVO>> mapContasBaixar = new HashMap<Integer, List<ContaReceberRegistroArquivoVO>>(0);
		int x = 0;
		for (ContaReceberRegistroArquivoVO obj : contaReceberRegistroArquivoVOs) {
			if (x > 0 && mapContasBaixar.containsKey(x) && mapContasBaixar.get(x).size() < 100) {
				mapContasBaixar.get(x).add(obj);
			} else {
				mapContasBaixar.put(++x, new ArrayList<ContaReceberRegistroArquivoVO>(0));
				mapContasBaixar.get(x).add(obj);
			}
		}
		return mapContasBaixar;
	}

	private Map<Integer, List<ContaReceberRegistroArquivoVO>> obterMapContasLocalizadasNaoBaixadas(List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs) {
		Map<Integer, List<ContaReceberRegistroArquivoVO>> mapContasBaixar = new HashMap<Integer, List<ContaReceberRegistroArquivoVO>>(0);
		int x = 0;
		mapContasBaixar.put(x, new ArrayList<>());
		for (ContaReceberRegistroArquivoVO obj : contaReceberRegistroArquivoVOs) {
			if (obj.getContaReceberVO().getObservacao().equals("Conta Localizada!") && obj.getContaReceberVO().getSituacaoAReceber()) {
				mapContasBaixar.get(x).add(obj);
			}
		}
		return mapContasBaixar;
	}

	public void run() {
		try {
			setSituacao("Consultando títulos para a baixa....");
			setControleCobrancaVO(getFacadeFactory().getControleCobrancaFacade().consultarPorChavePrimariaCompleto(getControleCobrancaVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSCONSULTARTODOS, getUsuarioVO()));
			getControleCobrancaVO().setContaCorrenteVO(getContaCorrenteVO());
			if (!getControleCobrancaVO().getCodigo().equals(0)) {
				setSituacao("Processando baixa dos títulos....");
				Map<Integer, List<ContaReceberRegistroArquivoVO>> mapContasBaixar = realizarSeparacaoContaReceberPorLote(getControleCobrancaVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs());
				getControleCobrancaVO().setQtdeLote(mapContasBaixar.size());
				getControleCobrancaVO().setDataInicioProcessamento(new Date());
				getControleCobrancaVO().setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.EM_PROCESSAMENTO);
				getFacadeFactory().getControleCobrancaFacade().realizarRegistroInicioProcessamento(getControleCobrancaVO().getCodigo(), getControleCobrancaVO().getQtdeLote(), getControleCobrancaVO().getDataInicioProcessamento(), getUsuarioVO());
				for (Integer lote : mapContasBaixar.keySet()) {
					getControleCobrancaVO().setLoteAtual(lote);					
					getFacadeFactory().getControleCobrancaFacade().realizarRegistroLoteEmProcessamento(getControleCobrancaVO().getCodigo(), lote, getUsuarioVO());
					getFacadeFactory().getControleCobrancaFacade().baixarContasRegistroArquivo(getControleCobrancaVO(), mapContasBaixar.get(lote), getControleCobrancaVO().getRegistroArquivoVO(), getConfiguracaoFinanceiroSistemaVO(), getUsuarioVO());
					mapContasBaixar.get(lote).clear();
					Thread.sleep(60000);
				}
			}
			// processar contas localizadas porém que não foram baixadas no primeiro processamento.
			Map<Integer, List<ContaReceberRegistroArquivoVO>> mapContasBaixar = obterMapContasLocalizadasNaoBaixadas(getControleCobrancaVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs());
			if (mapContasBaixar != null && mapContasBaixar.size() > 0) {
				getFacadeFactory().getControleCobrancaFacade().baixarContasRegistroArquivo(getControleCobrancaVO(), mapContasBaixar.get(0), getControleCobrancaVO().getRegistroArquivoVO(), getConfiguracaoFinanceiroSistemaVO(), getUsuarioVO());
			}

			getControleCobrancaVO().getRegistroArquivoVO().setContasBaixadas(true);
			getFacadeFactory().getRegistroArquivoFacade().alterar(getControleCobrancaVO().getRegistroArquivoVO(), false, controleCobrancaVO.getResponsavel());
			getControleCobrancaVO().setDataTerminoProcessamento(new Date());
			if (!getControleCobrancaVO().getSituacaoProcessamento().equals(SituacaoProcessamentoArquivoRetornoEnum.ERRO_PROCESSAMENTO)) {
				getControleCobrancaVO().setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.PROCESSAMENTO_CONCLUIDO);
				getFacadeFactory().getControleCobrancaFacade().realizarRegistroTerminoProcessamento(getControleCobrancaVO().getCodigo(), getControleCobrancaVO().getDataTerminoProcessamento(), getUsuarioVO());
			}
			if (!getControleCobrancaVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs().isEmpty()) {
				ContaReceberRegistroArquivoVO conta = getControleCobrancaVO().getRegistroArquivoVO().getContaReceberRegistroArquivoVOs().get(0);
				Integer codigoContaCorrente = conta.getContaReceberVO().getContaCorrenteVO().getCodigo();
				if (codigoContaCorrente > 0) {
					ContaCorrenteVO contaCorrente = getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(codigoContaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, controleCobrancaVO.getResponsavel());
					if (contaCorrente.getGerarContaPagarTaxaBancaria().booleanValue()) {
						contaCorrente.setFormaPgtoTaxaBancaria(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(contaCorrente.getFormaPgtoTaxaBancaria().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, getUsuarioVO()));
						if (getControleCobrancaVO().getRegistroArquivoVO().getTotalTaxaBoletoCobradoBanco() > 0) {
							getFacadeFactory().getContaPagarFacade().gerarContaPagarPaga(getControleCobrancaVO().getRegistroArquivoVO().getTotalTaxaBoletoCobradoBanco(), contaCorrente.getAgencia().getBanco().getCodigo(), contaCorrente.getFormaPgtoTaxaBancaria(), contaCorrente.getCategoriaDespesaTaxaBancaria(), conta.getContaReceberVO().getUnidadeEnsino(), contaCorrente, contaCorrente.getUtilizaAbatimentoNoRepasseRemessaBanco(), getControleCobrancaVO().getResponsavel());
						}
					}
				}
			}
			Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor());
			enviarEmail();
			setSituacao("Títulos baixados com sucesso!");
		} catch (Exception e) {
			getControleCobrancaVO().setDataTerminoProcessamento(new Date());
			getControleCobrancaVO().setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.ERRO_PROCESSAMENTO);
			getControleCobrancaVO().setMotivoErroProcessamento(e.getMessage());
			try {
				getFacadeFactory().getControleCobrancaFacade().realizarRegistroErroProcessamento(getControleCobrancaVO().getCodigo(), getControleCobrancaVO().getSituacaoProcessamento(),e.getMessage(), getControleCobrancaVO().getDataTerminoProcessamento(), getUsuarioVO());
			} catch (Exception ex) {

			}
			if ((!Uteis.ARQUIVOS_CONTROLE_COBRANCA.containsKey(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo()))) || (Uteis.ARQUIVOS_CONTROLE_COBRANCA.containsKey(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo())) && !Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor()))) {
				Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor());
			}
			setSituacao("ERRO no Processando da :" + e.getMessage());
			// System.out.println(e.getMessage());
		} finally {
			// controleCobrancaVO = null;
			configuracaoFinanceiroSistemaVO = null;
			usuarioVO = null;
			configuracaoGeralSistemaVO = null;
			comunicacaoInternaVO = null;
			comunicadoInternoDestinatarioVO = null;
		}
	}

	public void enviarEmail() {
		try {
			if (getControleCobrancaVO().getResponsavel().getPessoa() == null || getControleCobrancaVO().getResponsavel().getPessoa().getCodigo().equals(0)) {
				return;
			} else {
				getConfiguracaoGeralSistemaVO().setConfiguracoesVO(getFacadeFactory().getConfiguracoesFacade().consultarConfiguracaoPadraoSemControleAcesso());
				setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoConfiguracoes(getConfiguracaoGeralSistemaVO().getConfiguracoesVO().getCodigo(), false, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
				getComunicacaoInternaVO().setResponsavel(getUsuarioVO().getPessoa());
				getComunicacaoInternaVO().setAssunto("Arquivo de Retorno");
				getComunicacaoInternaVO().setEnviarEmail(true);
				getComunicacaoInternaVO().setTipoDestinatario("FU");
				getComunicacaoInternaVO().setTipoMarketing(false);
				getComunicacaoInternaVO().setTipoLeituraObrigatoria(false);
				getComunicacaoInternaVO().setDigitarMensagem(true);
				getComunicacaoInternaVO().setRemoverCaixaSaida(false);
				getComunicacaoInternaVO().setMensagem("Arquivo: " + getControleCobrancaVO().getNomeArquivo() + " - Situação: " + SituacaoArquivoRetorno.CONTAS_BAIXADAS.getDescricao());
				adicionarDestinatarios();
				// getUsuarioResponsavelEnviarMensagemProfessorVO().setPessoa(comunicacaoInternaVO.getResponsavel());
				// setUsuarioResponsavelEnviarMensagemProfessorVO(getUsuarioFacade().consultarPorPessoa(getUsuarioResponsavelEnviarMensagemProfessorVO().getPessoa().getCodigo(),
				// false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, null));
				// getComunicacaoInternaVO().setComunicadoInternoDestinatarioVOs(null);
				if (!getComunicacaoInternaVO().getComunicadoInternoDestinatarioVOs().isEmpty()) {
					getFacadeFactory().getComunicacaoInternaFacade().incluir(getComunicacaoInternaVO(), false, getUsuarioVO(), getConfiguracaoGeralSistemaVO(),null);
				}
			}
		} catch (Exception e) {
			// System.out.println("JobBaixarContas Erro:" + e.getMessage());
		}
	}

	public void adicionarDestinatarios() throws Exception {
		try {
			getComunicadoInternoDestinatarioVO().setDestinatario(getUsuarioVO().getPessoa());
			getComunicadoInternoDestinatarioVO().setTipoComunicadoInterno("LE");
			getComunicadoInternoDestinatarioVO().setDataLeitura(null);
			getComunicadoInternoDestinatarioVO().setCiJaRespondida(false);
			getComunicadoInternoDestinatarioVO().setCiJaLida(false);
			getComunicadoInternoDestinatarioVO().setRemoverCaixaEntrada(false);
			getComunicadoInternoDestinatarioVO().setMensagemMarketingLida(false);
			getComunicacaoInternaVO().adicionarObjComunicadoInternoDestinatarioVOs(getComunicadoInternoDestinatarioVO());
		} catch (Exception e) {
			// System.out.println("JobBaixarContas Erro:" + e.getMessage());
		}
	}

	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroSistemaVO() {
		if (configuracaoFinanceiroSistemaVO == null) {
			configuracaoFinanceiroSistemaVO = new ConfiguracaoFinanceiroVO();
		}
		return configuracaoFinanceiroSistemaVO;
	}

	public void setConfiguracaoFinanceiroSistemaVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroSistemaVO) {
		this.configuracaoFinanceiroSistemaVO = configuracaoFinanceiroSistemaVO;
	}

	public UsuarioVO getUsuarioVO() {
		if (usuarioVO == null) {
			usuarioVO = new UsuarioVO();
		}
		return usuarioVO;
	}

	public void setUsuarioVO(UsuarioVO usuarioVO) {
		this.usuarioVO = usuarioVO;
	}

	public ControleCobrancaVO getControleCobrancaVO() {
		if (controleCobrancaVO == null) {
			controleCobrancaVO = new ControleCobrancaVO();
		}
		return controleCobrancaVO;
	}

	public void setControleCobrancaVO(ControleCobrancaVO controleCobrancaVO) {
		this.controleCobrancaVO = controleCobrancaVO;
	}

	public ComunicacaoInternaVO getComunicacaoInternaVO() {
		if (comunicacaoInternaVO == null) {
			comunicacaoInternaVO = new ComunicacaoInternaVO();
		}
		return comunicacaoInternaVO;
	}

	public void setComunicacaoInternaVO(ComunicacaoInternaVO comunicacaoInternaVO) {
		this.comunicacaoInternaVO = comunicacaoInternaVO;
	}

	public ComunicadoInternoDestinatarioVO getComunicadoInternoDestinatarioVO() {
		if (comunicadoInternoDestinatarioVO == null) {
			comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
		}
		return comunicadoInternoDestinatarioVO;
	}

	public void setComunicadoInternoDestinatarioVO(ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO) {
		this.comunicadoInternoDestinatarioVO = comunicadoInternoDestinatarioVO;
	}

	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		if (configuracaoGeralSistemaVO == null) {
			configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
		}
		return configuracaoGeralSistemaVO;
	}

	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public ContaCorrenteVO getContaCorrenteVO() {
		if (contaCorrenteVO == null) {
			contaCorrenteVO = new ContaCorrenteVO();
		}
		return contaCorrenteVO;
	}

	public void setContaCorrenteVO(ContaCorrenteVO contaCorrenteVO) {
		this.contaCorrenteVO = contaCorrenteVO;
	}
}
