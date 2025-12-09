package negocio.comuns.utilitarias;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.collections.map.HashedMap;

import controle.arquitetura.AplicacaoControle;
import controle.arquitetura.SuperControle;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;

public class ProgressBarVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7701144610790627236L;
	private Long progresso;
	private Boolean ativado;
	private Boolean pollAtivado;
	private String status;
	private Integer maxValue;
	private Long progresso1;
	private Boolean assincrono;
	private Exception exception;	
	private Thread progressBarThread;
	private UsuarioVO usuarioVO;
	private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO;
	private ConfiguracaoFinanceiroVO configuracaoFinanceiroVO;
	private String caminhoWebRelatorio;
	private String urlAplicacaoExterna;
	private UnidadeEnsinoVO UnidadeEnsinoVO;
	private SuperControle superControle;
	private AplicacaoControle aplicacaoControle;
	private String msgLogs;
	private String urlLogoUnidadeEnsino;
	private Map<String, Object> params;
	
	public Long getProgresso() {
		if (progresso == null) {
			progresso = 0L;
		}
		return progresso;
	}
	public void setProgresso(Long progresso) {
		this.progresso = progresso;
	}
	public Boolean getAtivado() {
		if (ativado == null) {
			ativado = Boolean.FALSE;
		}
		return ativado;
	}
	public void setAtivado(Boolean ativado) {
		this.ativado = ativado;
	}
	public String getStatus() {
		if (status == null) {
			status = "";
		}
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getMaxValue() {
		if (maxValue == null) {
			maxValue = 0;
		}
		return maxValue;
	}
	public void setMaxValue(Integer maxValue) {
		this.maxValue = maxValue;
	}
	/**
	 * @return the progresso1
	 */
	public Long getProgresso1() {
		if (progresso1 == null) {
			progresso1 = 0L;
		}
		return progresso1;
	}
	/**
	 * @param progresso1 the progresso1 to set
	 */
	public void setProgresso1(Long progresso1) {
		this.progresso1 = progresso1;
	}
	
	public Exception getException() {		
		return exception;
	}
	public void setException(Exception exception) {
		this.exception = exception;
	}
	public Boolean getPollAtivado() {
		if (pollAtivado == null) {
			pollAtivado = false;
		}
		return pollAtivado;
	}
	public void setPollAtivado(Boolean pollAtivado) {
		this.pollAtivado = pollAtivado;
	}
	
	public synchronized void incrementarSemStatus(){	
		if(getAtivado()){			
			progresso++;	
		}
	}
	
	public void incrementar(){	
		if(getAtivado()){			
			progresso++;										
			setStatus("Processando "+(progresso)+"/"+(getMaxValue())+" ("+Uteis.formatarDeAcordoQuantidadeCasasDecimaisAposVirgulaDouble(getPorcentagem(), 0)+"%)");				
		}
	}
	
	public void decrementar(){	
		if(getAtivado()){			
			progresso--;
		}
	}
	
	public void resetar(){
		setAtivado(false);
		setPollAtivado(false);
		setForcarEncerramento(false);
		setProgresso(0l);
		setMaxValue(0);
		setStatus("Aguardando...");
		setException(null);		
		setProgressBarThread(null);
		setSuperControle(null);
		
	}
	
	public void iniciar(Long valorInicial, Integer valorFinal, String statusInicial, Boolean assincrono, SuperControle controle, String metodo){
		setAtivado(true);
		setProgresso(valorInicial);
		setMaxValue(valorFinal);
		setStatus(statusInicial);
		setPollAtivado(true);
		setForcarEncerramento(false);
		setAssincrono(assincrono);			
		setSuperControle(controle);
		if(valorInicial < valorFinal && assincrono){
			setProgressBarThread(new Thread(new ProgressBarThread(controle, metodo), controle.getClass().getSimpleName()+" - "+metodo));
		}
	}
	
	public void iniciarAssincrono(){
		if(getAtivado() && getAssincrono() && getProgressBarThread() != null && !getProgressBarThread().isAlive()){					
			if(getProgresso().intValue() >= getMaxValue().intValue() || getForcarEncerramento() || getProgresso() < 0) {
				setAtivado(false);			
				setPollAtivado(false);						
				setProgresso(-1l);	
			}else {
				getProgressBarThread().start();
			}			
		}
	}
	

	public void encerrar(){
		encerrarPool();
		if(!getAssincrono() && ((getProgresso()) >= getMaxValue() || getForcarEncerramento())){
			setAtivado(false);			
			setPollAtivado(false);						
			setProgresso(-1l);
		}		
	}
	
	public void forcarEncerramento() {
		if (getForcarEncerramento()) {
			setAtivado(false);			
			setPollAtivado(false);						
			setProgresso(-1l);
		}
	}
	
	public void encerrarDashboard(){	
		encerrarPool();
		if(((getProgresso()) >= getMaxValue() || getForcarEncerramento())){
			setAtivado(false);			
			setPollAtivado(false);						
			setProgresso(-1l);					
		}		
	}
	
	public void encerrarPool(){		
		if(getAssincrono() && !getAtivado() && getPollAtivado()){
			setPollAtivado(getAtivado());
		}				
	}	
	
	public Boolean forcarEncerramento;
	
	public Boolean getForcarEncerramento() {
		if(forcarEncerramento == null){
			forcarEncerramento = false;
		}
		return forcarEncerramento;
	}
	public void setForcarEncerramento(Boolean forcarEncerramento) {
		this.forcarEncerramento = forcarEncerramento;
	}
	public Boolean getAssincrono() {
		if(assincrono == null){
			assincrono = false;
		}
		return assincrono;
	}
	
	public void setAssincrono(Boolean assincrono) {
		this.assincrono = assincrono;
	}

	public Thread getProgressBarThread() {
		return progressBarThread;
	}
	public void setProgressBarThread(Thread progressBarThread) {
		this.progressBarThread = progressBarThread;
	}


	public class ProgressBarThread extends SuperControle implements Runnable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = -1962718763318259338L;
		SuperControle controle;
		String metodo;		

		public ProgressBarThread(SuperControle controle, String metodo) {
			super();
			this.controle = controle;			
			this.metodo = metodo;			
		}



		@Override
		public void run() {
			if(controle != null && !metodo.trim().isEmpty()){				
				try{
					Method metodoControle = controle.getClass().getMethod(metodo);					
					metodoControle.invoke(controle);					
				}catch(NoSuchMethodException e){
					setForcarEncerramento(true);					
					controle.setMensagemDetalhada("msg_erro", "Método "+metodo+" não encontrado no controle "+controle.getClass().getSimpleName(), Uteis.ERRO);
				}catch(Exception e){
					setForcarEncerramento(true);					
					controle.setMensagemDetalhada("msg_erro", e.getMessage(), Uteis.ERRO);					
				}finally {
					encerrar();					
				}
			}
		}
		
	}
	
	public String oncomplete;
	
	public String getOncomplete(){				
		if((getProgresso() >= getMaxValue()) || getForcarEncerramento() || (getProgresso().equals(-1l) && !getAtivado())){			
			return oncomplete;
		} 
		return "";
	}
	
	public void setOncomplete(String oncomplete) {
		this.oncomplete = oncomplete;
	}
	
	public Double getPorcentagem(){
		if(getMaxValue() <= 0){
			return 0.0;
		}
		if(getProgresso().equals(0l)) {
			return 0.1;
		}
		return getProgresso().doubleValue()*100.0/getMaxValue().doubleValue();
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
	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		if(configuracaoGeralSistemaVO == null){
			configuracaoGeralSistemaVO = new ConfiguracaoGeralSistemaVO();
		}
		return configuracaoGeralSistemaVO;
	}
	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}
	public String getCaminhoWebRelatorio() {
		if(caminhoWebRelatorio == null){
			caminhoWebRelatorio = "";
		}
		return caminhoWebRelatorio;
	}
	public void setCaminhoWebRelatorio(String caminhoWebRelatorio) {
		this.caminhoWebRelatorio = caminhoWebRelatorio;
	}
	public String getUrlAplicacaoExterna() {
		if(urlAplicacaoExterna == null){
			urlAplicacaoExterna = "";
		}
		return urlAplicacaoExterna;
	}
	public void setUrlAplicacaoExterna(String urlAplicacaoExterna) {
		this.urlAplicacaoExterna = urlAplicacaoExterna;
	}
	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO() {
		if (configuracaoFinanceiroVO == null) {
			configuracaoFinanceiroVO = new ConfiguracaoFinanceiroVO();
		}
		return configuracaoFinanceiroVO;
	}
	public void setConfiguracaoFinanceiroVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
		this.configuracaoFinanceiroVO = configuracaoFinanceiroVO;
	}
	public SuperControle getSuperControle() {		
		return superControle;
	}
	public void setSuperControle(SuperControle superControle) {
		this.superControle = superControle;
	}
	public AplicacaoControle getAplicacaoControle() {		
		return aplicacaoControle;
	}
	public void setAplicacaoControle(AplicacaoControle aplicacaoControle) {
		this.aplicacaoControle = aplicacaoControle;
	}

	public UnidadeEnsinoVO getUnidadeEnsinoVO() {
		if (UnidadeEnsinoVO == null) {
			UnidadeEnsinoVO = new UnidadeEnsinoVO();
		}
		return UnidadeEnsinoVO;
	}

	public void setUnidadeEnsinoVO(UnidadeEnsinoVO unidadeEnsinoVO) {
		UnidadeEnsinoVO = unidadeEnsinoVO;
	}
	
	public String getMsgLogs() {
		if (msgLogs == null) {
			msgLogs = "";
		}
		return msgLogs;
	}
	public void setMsgLogs(String msgLogs) {
		this.msgLogs = msgLogs;
	}
	
	public String preencherCabecalhoOperacao(String titulo) {
		StringBuilder dados = new StringBuilder();
		dados.append(System.lineSeparator());
		dados.append("-------------------------------");
		dados.append(titulo);
		dados.append("-------------------------------");
		return dados.toString();
	}
	
	public String getPreencherCamposDescricaoOperacaoLog(String texto) {
		StringBuilder sb = new StringBuilder();
		sb.append(System.lineSeparator());
		sb.append(texto);
		return sb.toString();		
	}
	
	public String getPreencherStatusProgressBarVO(ProgressBarVO progressBarVO, String entidade, String chave) {
		StringBuilder sb = new StringBuilder();
		sb.append(" Processando ").append(progressBarVO.getProgresso()).append(" de ").append(progressBarVO.getMaxValue());
		sb.append(" - (").append(entidade).append(" Atual Nº = ").append(chave).append(")");
		return sb.toString();		
	}
	public String getUrlLogoUnidadeEnsino() {
		return urlLogoUnidadeEnsino;
	}
	public void setUrlLogoUnidadeEnsino(String urlLogoUnidadeEnsino) {
		this.urlLogoUnidadeEnsino = urlLogoUnidadeEnsino;
	}
	public Map<String, Object> getParams() {
		if(params == null) {
			params =  new HashMap<String, Object>(0);
		}
		return params;
	}
	public void setParams(Map<String, Object> params) {
		this.params = params;
	}

	public void encerrarForcado(){
		setForcarEncerramento(true);
		if(getProgressBarThread() != null && getProgressBarThread().isAlive()) {
			getProgressBarThread().interrupt();
		}
	}
}
