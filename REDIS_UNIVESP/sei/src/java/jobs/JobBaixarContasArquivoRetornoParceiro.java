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
import negocio.comuns.financeiro.ProcessamentoArquivoRetornoParceiroAlunoVO;
import negocio.comuns.financeiro.ProcessamentoArquivoRetornoParceiroVO;
import negocio.comuns.financeiro.enumerador.SituacaoProcessamentoArquivoRetornoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoArquivoRetorno;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

public class JobBaixarContasArquivoRetornoParceiro extends ControleAcesso implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5858609045907402419L;

	private ProcessamentoArquivoRetornoParceiroVO processamentoArquivoRetornoParceiroVO;
	private ConfiguracaoFinanceiroVO configuracaoFinanceiroSistemaVO;
	private UsuarioVO usuarioVO;
	private ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO;
	private ComunicacaoInternaVO comunicacaoInternaVO;
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
	private String situacao;

	public JobBaixarContasArquivoRetornoParceiro(ProcessamentoArquivoRetornoParceiroVO processamentoArquivoRetornoParceiroVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroSistemaVO, UsuarioVO usuario) {
		this.processamentoArquivoRetornoParceiroVO = processamentoArquivoRetornoParceiroVO;
		this.configuracaoFinanceiroSistemaVO = configuracaoFinanceiroSistemaVO;
		this.usuarioVO = usuario;
	}

	private void realizarSeparacaoContaReceberPorLote(Map<Integer, List<ProcessamentoArquivoRetornoParceiroAlunoVO>> mapContasBaixar, List<ProcessamentoArquivoRetornoParceiroAlunoVO> contaReceberRegistroArquivoVOs) {
		int x = 0;
		for (ProcessamentoArquivoRetornoParceiroAlunoVO obj : contaReceberRegistroArquivoVOs) {
			if (x > 0 && mapContasBaixar.containsKey(x) && mapContasBaixar.get(x).size() < 100) {
				mapContasBaixar.get(x).add(obj);
			} else {
				mapContasBaixar.put(++x, new ArrayList<ProcessamentoArquivoRetornoParceiroAlunoVO>(0));
				mapContasBaixar.get(x).add(obj);
			}
		}
	}
	
	
	public void run() {
		try {
			setSituacao("Processando baixa dos títulos....");
			setProcessamentoArquivoRetornoParceiroVO(getFacadeFactory().getProcessamentoArquivoRetornoParceiroFacade().consultarPorChavePrimaria(getProcessamentoArquivoRetornoParceiroVO().getCodigo(), true, Uteis.NIVELMONTARDADOS_TODOS, getUsuarioVO()));
			Map<Integer, List<ProcessamentoArquivoRetornoParceiroAlunoVO>> mapContasBaixar = new HashMap<Integer, List<ProcessamentoArquivoRetornoParceiroAlunoVO>>(0);
			realizarSeparacaoContaReceberPorLote(mapContasBaixar, getProcessamentoArquivoRetornoParceiroVO().getListaContasAReceber());
			getProcessamentoArquivoRetornoParceiroVO().setQtdeLote(mapContasBaixar.size());
			getProcessamentoArquivoRetornoParceiroVO().setDataInicioProcessamento(new Date());
			getProcessamentoArquivoRetornoParceiroVO().setSituacaoProcessamento(SituacaoProcessamentoArquivoRetornoEnum.EM_PROCESSAMENTO);
			getFacadeFactory().getProcessamentoArquivoRetornoParceiroFacade().realizarRegistroInicioProcessamento(getProcessamentoArquivoRetornoParceiroVO().getCodigo(), getProcessamentoArquivoRetornoParceiroVO().getQtdeLote(), getProcessamentoArquivoRetornoParceiroVO().getDataInicioProcessamento(), getUsuarioVO());
			for (Integer lote : mapContasBaixar.keySet()) {
				getProcessamentoArquivoRetornoParceiroVO().setLoteAtual(lote);
				getFacadeFactory().getProcessamentoArquivoRetornoParceiroFacade().realizarRegistroLoteEmProcessamento(getProcessamentoArquivoRetornoParceiroVO().getCodigo(), lote, getUsuarioVO());
				getFacadeFactory().getProcessamentoArquivoRetornoParceiroFacade().baixarContasRegistroArquivo(getProcessamentoArquivoRetornoParceiroVO(), mapContasBaixar.get(lote), getConfiguracaoFinanceiroSistemaVO(), getUsuarioVO());
				mapContasBaixar.get(lote).clear();
				Thread.sleep(60000);
			}
			getProcessamentoArquivoRetornoParceiroVO().setContasBaixadas(true);
			getFacadeFactory().getProcessamentoArquivoRetornoParceiroFacade().preencherNegociacaoPagamentoVO(getProcessamentoArquivoRetornoParceiroVO(), getUsuarioVO());
			Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getProcessamentoArquivoRetornoParceiroVO().getNomeArquivo_Apresentar(), getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.CONTAS_BAIXADAS.getValor());
			enviarEmail();
			setSituacao("Títulos baixados com sucesso!");
		} catch (Exception e) {
			try {
				getProcessamentoArquivoRetornoParceiroVO().setMotivoErroProcessamento(e.getMessage());
				getFacadeFactory().getProcessamentoArquivoRetornoParceiroFacade().realizarRegistroErroProcessamento(getProcessamentoArquivoRetornoParceiroVO().getCodigo(), e.getMessage(), new Date(), getUsuarioVO());
			} catch (Exception ex) {

			}
			Uteis.ARQUIVOS_CONTROLE_COBRANCA.put(Uteis.getNomeArquivoComUnidadeEnsino(getProcessamentoArquivoRetornoParceiroVO().getNomeArquivo_Apresentar(), getProcessamentoArquivoRetornoParceiroVO().getUnidadeEnsinoVO().getCodigo()), SituacaoArquivoRetorno.ARQUIVO_PROCESSADO.getValor());
			setSituacao("ERRO no Processando da :" + e.getMessage());
		} finally {
			processamentoArquivoRetornoParceiroVO = null;
			configuracaoFinanceiroSistemaVO = null;
			usuarioVO = null;
			configuracaoGeralSistemaVO = null;
			comunicacaoInternaVO = null;
			comunicadoInternoDestinatarioVO = null;
		}
	}

	public void enviarEmail() {
		try {
			if (getProcessamentoArquivoRetornoParceiroVO().getResponsavel().getPessoa() == null || getProcessamentoArquivoRetornoParceiroVO().getResponsavel().getPessoa().getCodigo().equals(0)) {
				return;
			} else {
				getConfiguracaoGeralSistemaVO().setConfiguracoesVO(getFacadeFactory().getConfiguracoesFacade().consultarConfiguracaoPadraoSemControleAcesso());
				setConfiguracaoGeralSistemaVO(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarPorCodigoConfiguracoes(getConfiguracaoGeralSistemaVO().getConfiguracoesVO().getCodigo(), false, null, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
				getComunicacaoInternaVO().setResponsavel(getUsuarioVO().getPessoa());
				getComunicacaoInternaVO().setAssunto("Arquivo de Retorno Parceiro");
				getComunicacaoInternaVO().setEnviarEmail(true);
				getComunicacaoInternaVO().setTipoDestinatario("FU");
				getComunicacaoInternaVO().setTipoMarketing(false);
				getComunicacaoInternaVO().setTipoLeituraObrigatoria(false);
				getComunicacaoInternaVO().setDigitarMensagem(true);
				getComunicacaoInternaVO().setRemoverCaixaSaida(false);
				getComunicacaoInternaVO().setMensagem("Arquivo: " + getProcessamentoArquivoRetornoParceiroVO().getNomeArquivo() + " - Situação: " + SituacaoArquivoRetorno.CONTAS_BAIXADAS.getDescricao());
				adicionarDestinatarios();
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

	public ProcessamentoArquivoRetornoParceiroVO getProcessamentoArquivoRetornoParceiroVO() {
		if (processamentoArquivoRetornoParceiroVO == null) {
			processamentoArquivoRetornoParceiroVO = new ProcessamentoArquivoRetornoParceiroVO();
		}
		return processamentoArquivoRetornoParceiroVO;
	}

	public void setProcessamentoArquivoRetornoParceiroVO(ProcessamentoArquivoRetornoParceiroVO processamentoArquivoRetornoParceiroVO) {
		this.processamentoArquivoRetornoParceiroVO = processamentoArquivoRetornoParceiroVO;
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
