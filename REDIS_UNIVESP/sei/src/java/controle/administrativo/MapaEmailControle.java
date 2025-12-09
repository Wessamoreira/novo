package controle.administrativo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.richfaces.event.DataScrollEvent;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.DataModelo;
import controle.arquitetura.SuperControle;
import jobs.JobEnvioEmail;
import jobs.enumeradores.JobsEnum;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.EmailVO;
import negocio.comuns.arquitetura.SMSVO;
import negocio.comuns.job.RegistroExecucaoJobTotaisVO;
import negocio.comuns.job.RegistroExecucaoJobVO;
import negocio.comuns.moodle.OperacaoMoodleVO;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoOperacaoMoodleEnum;
import negocio.facade.jdbc.moodle.OperacaoMoodle;
import webservice.moodle.MensagensItemRSVO;

/**
 * 
 * @author Carlos
 */

@Controller("MapaEmailControle")
@Scope("viewScope")
@Lazy
public class MapaEmailControle extends SuperControle implements Serializable {

	private static final long serialVersionUID = -9085681387944125356L;
	private EmailVO emailVO;
	private SMSVO smsVO;
	private String nomeDestSms;
	private String numeroDestSms;
	private List<SMSVO> listaSms;
	private List<RegistroExecucaoJobVO> registros;
	private static final String CONTEXT_PARA_EDICAO = "emailItens";
	
	private int totalEmailsAguardandoEnvio;
	private int totalEmailsNaoEnviados;
	private RegistroExecucaoJobTotaisVO totaisUltimaHora;
	private RegistroExecucaoJobTotaisVO totaisUltimas24Horas;
	private String assunto;
	private Date dataEnvio;
	private String destinatario;
	private String emailDestinatario;
	private String  remetente; 
	private String emailRemetente;
	private DataModelo dataModeloOperacaoMoodleProcessamentoPendente;
	private DataModelo dataModeloOperacaoMoodleProcessamentoErro;
	private DataModelo dataModeloRegistroExecucaoJob;
	private OperacaoMoodleVO operacaoMoodle;
	private MensagensItemRSVO mensagensItemRS;
	

	public MapaEmailControle() {
		setControleConsultaOtimizado(new DataModelo());
		getControleConsultaOtimizado().setOffset(0);
		getControleConsultaOtimizado().setLimitePorPagina(10);
		consultarEmails();
	}
	
	public void consultarRegistros() {
		try {
			setTotalEmailsAguardandoEnvio(getFacadeFactory().getRegistroExecucaoJobFacade().consultarTotalEmailsAguardandoEnvio());
			setTotalEmailsNaoEnviados(getFacadeFactory().getRegistroExecucaoJobFacade().consultarTotalEmailsQueNaoForamEnviados());
			getFacadeFactory().getRegistroExecucaoJobFacade().consultarTotaisUltimaHora(getTotaisUltimaHora());
			getFacadeFactory().getRegistroExecucaoJobFacade().consultarTotaisUltimas24Horas(getTotaisUltimas24Horas());
			setRegistros(getFacadeFactory().getRegistroExecucaoJobFacade().consultarUltimosRegistros());
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public void consultarEmails() {
		try {
			getControleConsultaOtimizado().setListaConsulta(getFacadeFactory().getEmailFacade().consultarEmails(getControleConsultaOtimizado(), getAssunto(), getDataEnvio(), getDestinatario(), getEmailDestinatario(), getRemetente(), getEmailRemetente()));
			getControleConsultaOtimizado().getListaFiltros().clear();
			getControleConsultaOtimizado().setTotalRegistrosEncontrados(getFacadeFactory().getEmailFacade().consultarTotalEmails(getControleConsultaOtimizado(), getAssunto(), getDataEnvio(), getDestinatario(), getEmailDestinatario(), getRemetente(), getEmailRemetente()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception ex) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public void consultarSms() {
		try {
			getListaSms().clear();
			if (getNumeroDestSms().equals("") && (getNomeDestSms().equals("") || getNomeDestSms().length() < 1)) {
				throw new StreamSeiException("Informe ao menos 1 caracter para realizar a consulta!");
			}
			if (getNomeDestSms().equals("") && (getNumeroDestSms().equals("") || getNumeroDestSms().length() < 1)) {
				throw new StreamSeiException("Informe ao menos 1 caracter para realizar a consulta!");
			}
			setListaSms(getFacadeFactory().getSmsFacade().consultarSms(getNomeDestSms(), getNumeroDestSms()));
			setMensagemID(MSG_TELA.msg_dados_consultados.name());
		} catch (Exception ex) {
			getListaSms().clear();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), ex.getMessage());
		}
	}

	public void visualizarDetalhes() {
		try {
			EmailVO obj = (EmailVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			setEmailVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void visualizarDetalhesSMS() {
		try {
			SMSVO obj = (SMSVO) context().getExternalContext().getRequestMap().get("smsItens");
			setSmsVO(obj);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void enviarSms() {
		try {
			List<SMSVO> lista = new ArrayList<>();
			SMSVO sms = (SMSVO) context().getExternalContext().getRequestMap().get("smsItens");
			lista.add(sms);
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			JobEnvioEmail job = new JobEnvioEmail();
			job.setIpServidor(config.getIpServidor());
			job.setSmtpPadrao(config.getSmptPadrao());
			job.setLoginServidorSmtp(config.getLogin());
			job.setEmailRemetente(config.getEmailRemetente());
			job.setSenhaServidorSmtp(config.getSenha());
			job.setPortaSmtpPadrao(config.getPortaSmtpPadrao());
			if (Uteis.validarEnvioEmail(config.getIpServidor())) {
				job.enviarSMSs(lista, config);
				setMensagemID("msg_msg_smsEnviados");
			} else {
				throw new StreamSeiException("Não foi possível enviar o SMS, configuração do hostname inválido!");
			}
		} catch (Exception e) {
			consultarEmails();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void cancelarEnvioEmail() {
		try {
			EmailVO email = (EmailVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			JobEnvioEmail job = new JobEnvioEmail();
			job.realizarExclusaoEmail(email);
			consultarEmails();
			setMensagemID("", "");
			setMensagem("Email cancelado com sucesso!");
		} catch (Exception e) {
			consultarEmails();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void enviarEmail() {
		try {
			EmailVO email = (EmailVO) context().getExternalContext().getRequestMap().get(CONTEXT_PARA_EDICAO);
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			JobEnvioEmail job = new JobEnvioEmail();
			job.setIpServidor(config.getIpServidor());
			job.setSmtpPadrao(config.getSmptPadrao());
			job.setLoginServidorSmtp(config.getLogin());
			job.setEmailRemetente(config.getEmailRemetente());
			job.setSenhaServidorSmtp(config.getSenha());
			job.setPortaSmtpPadrao(config.getPortaSmtpPadrao());
			setMensagemID("", "");
			if (Uteis.validarEnvioEmail(config.getIpServidor())) {				
				if (email.getMultiplosDestinatarios()) {
					job.enviarEmailMultiplosDestinatarios(email.getEmailDest(), email.getEmailRemet(), email.getNomeRemet(), email.getAssunto(), email.getMensagem(), email.getCaminhoAnexos(), email.getAnexarImagensPadrao(), config.getServidorEmailUtilizaSSL(), config.getServidorEmailUtilizaTSL(), email.getCaminhoLogoEmailCima(), email.getCaminhoLogoEmailBaixo(), null, config.getAtivarDebugEmail());
				} else {
					job.enviarEmail(email.getEmailDest(), email.getNomeDest(), email.getEmailRemet(), email.getNomeRemet(), email.getAssunto(), email.getMensagem(), email.getCaminhoAnexos(), email.getAnexarImagensPadrao(), config.getServidorEmailUtilizaSSL(), config.getServidorEmailUtilizaTSL(), email.getCaminhoLogoEmailCima(), email.getCaminhoLogoEmailBaixo(), null, config.getAtivarDebugEmail());
				}
				job.realizarExclusaoEmail(email);
				consultarEmails();
				setMensagem("Email enviado com sucesso!");
			} else {
				setMensagem("Não foi possível enviar o Email, configuração do hostname inválido!");
			}
		} catch (Exception e) {
			consultarEmails();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void enviarTodos() {
		try {
			ConfiguracaoGeralSistemaVO config = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmail();
			setMensagemID("", "");
			if (Uteis.validarEnvioEmail(config.getIpServidor())) {
				getFacadeFactory().getEmailFacade().executarReagendamentoTodosEmails(getUsuarioLogado(), getAssunto(), getDataEnvio(), getDestinatario(), getEmailDestinatario(), getRemetente(), getEmailRemetente());
				setMensagem("Todos os emails foram agendados para envio!");
			} else {
				setMensagem("Não foi possível enviar o(s) Email(s), configuração do hostname inválido!");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void excluirTodos() {
		try {
			getFacadeFactory().getEmailFacade().executarTodosEmails(getAssunto(), getDataEnvio(), getDestinatario(), getEmailDestinatario(), getRemetente(), getEmailRemetente());
			getControleConsultaOtimizado().getListaConsulta().clear();
			setMensagemID("", "");
			setMensagem("Todos os emails foram excluídos com sucesso!");
		} catch (Exception e) {
			consultarEmails();
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public EmailVO getEmailVO() {
		if (emailVO == null) {
			emailVO = new EmailVO();
		}
		return emailVO;
	}

	public void setEmailVO(EmailVO emailVO) {
		this.emailVO = emailVO;
	}

	public String getNomeDestSms() {
		if (nomeDestSms == null) {
			nomeDestSms = "";
		}
		return nomeDestSms;
	}

	public void setNomeDestSms(String nomeDestSms) {
		this.nomeDestSms = nomeDestSms;
	}

	public String getNumeroDestSms() {
		if (numeroDestSms == null) {
			numeroDestSms = "";
		}
		return numeroDestSms;
	}

	public void setNumeroDestSms(String numeroDestSms) {
		this.numeroDestSms = numeroDestSms;
	}

	public List<SMSVO> getListaSms() {
		if (listaSms == null) {
			listaSms = new ArrayList<>();
		}
		return listaSms;
	}

	public void setListaSms(List<SMSVO> listaSms) {
		this.listaSms = listaSms;
	}

	public SMSVO getSmsVO() {
		if (smsVO == null) {
			smsVO = new SMSVO();
		}
		return smsVO;
	}

	public void setSmsVO(SMSVO smsVO) {
		this.smsVO = smsVO;
	}

	public List<RegistroExecucaoJobVO> getRegistros() {
		if (registros == null) {
			registros = new ArrayList<RegistroExecucaoJobVO>();
		}
		return registros;
	}

	public void setRegistros(List<RegistroExecucaoJobVO> registros) {
		this.registros = registros;
	}

	public Integer getLimiteEmails() {
		return getConfiguracaoGeralPadraoSistema().getQtdEmailEnvio();
	}

	public int getTotalEmailsAguardandoEnvio() {
		return totalEmailsAguardandoEnvio;
	}

	public void setTotalEmailsAguardandoEnvio(int totalEmailsAguardandoEnvio) {
		this.totalEmailsAguardandoEnvio = totalEmailsAguardandoEnvio;
	}

	public RegistroExecucaoJobTotaisVO getTotaisUltimaHora() {
		if (totaisUltimaHora == null) {
			totaisUltimaHora = new RegistroExecucaoJobTotaisVO();
		}
		return totaisUltimaHora;
	}

	public void setTotaisUltimaHora(RegistroExecucaoJobTotaisVO totaisUltimaHora) {
		this.totaisUltimaHora = totaisUltimaHora;
	}

	public RegistroExecucaoJobTotaisVO getTotaisUltimas24Horas() {
		if (totaisUltimas24Horas == null) {
			totaisUltimas24Horas = new RegistroExecucaoJobTotaisVO();
		}
		return totaisUltimas24Horas;
	}

	public void setTotaisUltimas24Horas(RegistroExecucaoJobTotaisVO totaisUltimas24Horas) {
		this.totaisUltimas24Horas = totaisUltimas24Horas;
	}

	public int getTotalEmailsNaoEnviados() {
		return totalEmailsNaoEnviados;
	}

	public void setTotalEmailsNaoEnviados(int totalEmailsNaoEnviados) {
		this.totalEmailsNaoEnviados = totalEmailsNaoEnviados;
	}
	
	public void scrollerListener(DataScrollEvent dataScrollerEvent) {
		try {
			getControleConsultaOtimizado().setPaginaAtual(dataScrollerEvent.getPage());
			getControleConsultaOtimizado().setPage(dataScrollerEvent.getPage());
			consultarEmails();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
	
	public void consultarPorEmailFiltro() {
		try {
			getControleConsultaOtimizado().setPaginaAtual(1);
			getControleConsultaOtimizado().setPage(1);
			consultarEmails();
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public String getAssunto() {
		return assunto;
	}

	public void setAssunto(String assunto) {
		this.assunto = assunto;
	}

	public Date getDataEnvio() {
		return dataEnvio;
	}

	public void setDataEnvio(Date dataEnvio) {
		this.dataEnvio = dataEnvio;
	}

	public String getDestinatario() {
		return destinatario;
	}

	public void setDestinatario(String destinatario) {
		this.destinatario = destinatario;
	}

	public String getEmailDestinatario() {
		return emailDestinatario;
	}

	public void setEmailDestinatario(String emailDestinatario) {
		this.emailDestinatario = emailDestinatario;
	}

	public String getRemetente() {
		return remetente;
	}

	public void setRemetente(String remetente) {
		this.remetente = remetente;
	}

	public String getEmailRemetente() {
		return emailRemetente;
	}

	public void setEmailRemetente(String emailRemetente) {
		this.emailRemetente = emailRemetente;
	}
	
	public DataModelo getDataModeloOperacaoMoodleProcessamentoPendente() {
		if (dataModeloOperacaoMoodleProcessamentoPendente == null) {
			dataModeloOperacaoMoodleProcessamentoPendente = new DataModelo();
		}
		return dataModeloOperacaoMoodleProcessamentoPendente;
	}

	public void setDataModeloOperacaoMoodleProcessamentoPendente(DataModelo dataModeloOperacaoMoodleProcessamentoPendente) {
		this.dataModeloOperacaoMoodleProcessamentoPendente = dataModeloOperacaoMoodleProcessamentoPendente;
	}

	public DataModelo getDataModeloOperacaoMoodleProcessamentoErro() {
		if (dataModeloOperacaoMoodleProcessamentoErro == null) {
			dataModeloOperacaoMoodleProcessamentoErro = new DataModelo();
		}
		return dataModeloOperacaoMoodleProcessamentoErro;
	}

	public void setDataModeloOperacaoMoodleProcessamentoErro(DataModelo dataModeloOperacaoMoodleProcessamentoErro) {
		this.dataModeloOperacaoMoodleProcessamentoErro = dataModeloOperacaoMoodleProcessamentoErro;
	}

	public DataModelo getDataModeloRegistroExecucaoJob() {
		if (dataModeloRegistroExecucaoJob == null) {
			dataModeloRegistroExecucaoJob = new DataModelo();
		}
		return dataModeloRegistroExecucaoJob;
	}

	public void setDataModeloRegistroExecucaoJob(DataModelo dataModeloRegistroExecucaoJob) {
		this.dataModeloRegistroExecucaoJob = dataModeloRegistroExecucaoJob;
	}

	public OperacaoMoodleVO getOperacaoMoodle() {
		if (operacaoMoodle == null) {
			operacaoMoodle = new OperacaoMoodleVO();
		}
		return operacaoMoodle;
	}

	public void setOperacaoMoodle(OperacaoMoodleVO operacaoMoodle) {
		this.operacaoMoodle = operacaoMoodle;
	}

	public MensagensItemRSVO getMensagensItemRS() {
		if (mensagensItemRS == null) {
			mensagensItemRS = new MensagensItemRSVO();
		}
		return mensagensItemRS;
	}

	public void setMensagensItemRS(MensagensItemRSVO mensagensItemRS) {
		this.mensagensItemRS = mensagensItemRS;
	}

	public void atualizarDadosProcessamentoMensagemMoodle() {
		try {
			getDataModeloRegistroExecucaoJob().setValorConsulta(JobsEnum.JOB_OPERACAO_MENSAGEM_MOODLE.getName());
			getFacadeFactory().getOperacaoMoodleInterfaceFacade().carregarDadosOperacoesMoodle(TipoOperacaoMoodleEnum.MENSAGENS, getDataModeloOperacaoMoodleProcessamentoPendente(), getDataModeloOperacaoMoodleProcessamentoErro());
			getFacadeFactory().getRegistroExecucaoJobFacade().consultarOtimizado(getDataModeloRegistroExecucaoJob());
			setMensagemID(MSG_TELA.msg_dados_consultados.name(), Uteis.SUCESSO);
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerDadosProcessamentoPendente(DataScrollEvent dataScrollEvent) throws Exception {
		try {
			getDataModeloOperacaoMoodleProcessamentoPendente().setPaginaAtual(dataScrollEvent.getPage());
			getDataModeloOperacaoMoodleProcessamentoPendente().setPage(dataScrollEvent.getPage());
			getFacadeFactory().getOperacaoMoodleInterfaceFacade().carregarDadosOperacoesMoodle(TipoOperacaoMoodleEnum.MENSAGENS, getDataModeloOperacaoMoodleProcessamentoPendente(), "PENDENTE");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerDadosProcessamentoErro(DataScrollEvent dataScrollEvent) throws Exception {
		try {
			getDataModeloOperacaoMoodleProcessamentoErro().setPaginaAtual(dataScrollEvent.getPage());
			getDataModeloOperacaoMoodleProcessamentoErro().setPage(dataScrollEvent.getPage());
			getFacadeFactory().getOperacaoMoodleInterfaceFacade().carregarDadosOperacoesMoodle(TipoOperacaoMoodleEnum.MENSAGENS, getDataModeloOperacaoMoodleProcessamentoErro(), "ERRO");
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void scrollerListenerRegistroExecucaoJob(DataScrollEvent dataScrollEvent) throws Exception {
		try {
			getDataModeloRegistroExecucaoJob().setValorConsulta(JobsEnum.JOB_OPERACAO_MENSAGEM_MOODLE.getName());
			getDataModeloRegistroExecucaoJob().setPaginaAtual(dataScrollEvent.getPage());
			getDataModeloRegistroExecucaoJob().setPage(dataScrollEvent.getPage());
			getFacadeFactory().getRegistroExecucaoJobFacade().consultarOtimizado(getDataModeloRegistroExecucaoJob());
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void visualizarOperacaoMoodleMensagem(String var) {
		setOncompleteModal(Constantes.EMPTY);
		try {
			OperacaoMoodleVO obj = (OperacaoMoodleVO) context().getExternalContext().getRequestMap().get(var);
			if (Uteis.isAtributoPreenchido(obj)) {
				if (!Uteis.isAtributoPreenchido(obj.getMensagensRSVO().getMensagens())) {
					obj.setMensagensRSVO(OperacaoMoodle.converterJsonParaObjetoMensagensRSVO(obj.getJsonMoodle()));
				}
				setOperacaoMoodle(obj);
				setOncompleteModal("RichFaces.$('panelVisualizarDadosOperacaoMoodle').show();");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}

	public void visualizarDestinatariosMensagensItem() {
		setOncompleteModal(Constantes.EMPTY);
		try {
			MensagensItemRSVO obj = (MensagensItemRSVO) context().getExternalContext().getRequestMap().get("mensagemItens");
			if (Objects.nonNull(obj)) {
				setMensagensItemRS(obj);
				setOncompleteModal("RichFaces.$('panelVisualizarDestinatariosMensagensItemRS').show();");
			}
		} catch (Exception e) {
			setMensagemDetalhada(MSG_TELA.msg_erro.name(), e.getMessage());
		}
	}
}