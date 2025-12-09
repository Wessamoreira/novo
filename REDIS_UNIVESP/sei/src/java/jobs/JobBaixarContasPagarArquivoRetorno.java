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
import negocio.comuns.financeiro.ContaPagarRegistroArquivoVO;
import negocio.comuns.financeiro.ControleCobrancaPagarVO;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoArquivoRetornoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoArquivoRetorno;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

public class JobBaixarContasPagarArquivoRetorno extends ControleAcesso implements Runnable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1816734772810289248L;
	private ControleCobrancaPagarVO controleCobrancaVO;
	private ConfiguracaoFinanceiroVO configuracaoFinanceiroSistemaVO;
	private UsuarioVO usuarioVO;
	private ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO;
	private ComunicacaoInternaVO comunicacaoInternaVO;
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
	private String situacao;

	public JobBaixarContasPagarArquivoRetorno(ControleCobrancaPagarVO controleCobranca, ConfiguracaoFinanceiroVO configuracaoFinanceiroSistemaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistema, UsuarioVO usuario) {
		this.controleCobrancaVO = controleCobranca;
		this.configuracaoFinanceiroSistemaVO = configuracaoFinanceiroSistemaVO;
		this.usuarioVO = usuario;
		this.configuracaoGeralSistemaVO = configuracaoGeralSistema;
	}
	
	
	private Map<Integer, List<ContaPagarRegistroArquivoVO>> realizarSeparacaoContaReceberPorLote(List<ContaPagarRegistroArquivoVO> contaPagarRegistroDetalheVOs) {
		Map<Integer, List<ContaPagarRegistroArquivoVO>> mapContasBaixar = new HashMap<Integer, List<ContaPagarRegistroArquivoVO>>(0);
		int x = 0;
		for (ContaPagarRegistroArquivoVO obj : contaPagarRegistroDetalheVOs) {
			if (x > 0 && mapContasBaixar.containsKey(x) && mapContasBaixar.get(x).size() < 100) {
				mapContasBaixar.get(x).add(obj);
			} else {
				mapContasBaixar.put(++x, new ArrayList<ContaPagarRegistroArquivoVO>(0));
				mapContasBaixar.get(x).add(obj);
			}
		}
		return mapContasBaixar;
	}

	public void run() {
		try {
			setSituacao("Consultando títulos para a baixa....");
			if (!getControleCobrancaVO().getCodigo().equals(0)) {
				setSituacao("Processando baixa dos títulos....");
				Map<Integer, List<ContaPagarRegistroArquivoVO>> mapContasBaixar = realizarSeparacaoContaReceberPorLote(getControleCobrancaVO().getContaPagarRegistroArquivoVOs());
				getControleCobrancaVO().setQtdeLote(mapContasBaixar.size());
				getControleCobrancaVO().setDataInicioProcessamento(new Date());
				getControleCobrancaVO().setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.EM_PROCESSAMENTO);
				getFacadeFactory().getControleCobrancaPagarFacade().realizarRegistroInicioProcessamento(getControleCobrancaVO(), getUsuarioVO());
				for (Integer lote : mapContasBaixar.keySet()) {
					getControleCobrancaVO().setLoteAtual(lote);					
					getFacadeFactory().getControleCobrancaPagarFacade().realizarRegistroLoteEmProcessamento(getControleCobrancaVO().getCodigo(), lote, getUsuarioVO());
					getFacadeFactory().getControleCobrancaPagarFacade().baixarContasPagarArquivoRetorno(getControleCobrancaVO(), mapContasBaixar.get(lote), getConfiguracaoFinanceiroSistemaVO(), getConfiguracaoGeralSistemaVO(), getUsuarioVO());
					mapContasBaixar.get(lote).clear();
					Thread.sleep(60000);
				}
				getControleCobrancaVO().setDataTerminoProcessamento(new Date());
				getControleCobrancaVO().setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.PROCESSAMENTO_CONCLUIDO);
				getFacadeFactory().getControleCobrancaPagarFacade().realizarRegistroTerminoProcessamento(getControleCobrancaVO().getCodigo(), getControleCobrancaVO().getDataTerminoProcessamento(), getUsuarioVO());
				Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor());
			//	enviarEmail();
				setSituacao("Títulos baixados com sucesso!");
			}
		} catch (Exception e) {
			getControleCobrancaVO().setDataTerminoProcessamento(new Date());
			getControleCobrancaVO().setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.ERRO_PROCESSAMENTO);
			getControleCobrancaVO().setMotivoErroProcessamento(e.getMessage());
			try {
				getFacadeFactory().getControleCobrancaPagarFacade().realizarRegistroErroProcessamento(getControleCobrancaVO().getCodigo(), e.getMessage(), getControleCobrancaVO().getDataTerminoProcessamento(), getUsuarioVO());
			} catch (Exception ex) {

			}
			if ((!Uteis.ARQUIVOS_CONTROLE_COBRANCA.containsKey(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo()))) || (Uteis.ARQUIVOS_CONTROLE_COBRANCA.containsKey(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo())) && !Uteis.ARQUIVOS_CONTROLE_COBRANCA.get(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo())).equals(SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor()))) {
				Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getControleCobrancaVO().getNomeArquivo_Apresentar(), getControleCobrancaVO().getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor());
			}
			setSituacao("ERRO no Processando da :" + e.getMessage());
		} finally {
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
				getComunicacaoInternaVO().setMensagem("Arquivo: " + getControleCobrancaVO().getNomeArquivo_Apresentar() + " - Situação: " + SituacaoArquivoRetorno.CONTAS_BAIXADAS.getDescricao());
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

	public ControleCobrancaPagarVO getControleCobrancaVO() {
		if (controleCobrancaVO == null) {
			controleCobrancaVO = new ControleCobrancaPagarVO();
		}
		return controleCobrancaVO;
	}

	public void setControleCobrancaVO(ControleCobrancaPagarVO controleCobrancaVO) {
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

}
