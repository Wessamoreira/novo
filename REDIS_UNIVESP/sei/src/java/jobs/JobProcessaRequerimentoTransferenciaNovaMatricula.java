package jobs;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.CalendarioAtividadeMatriculaVO;
import negocio.comuns.ead.enumeradores.SituacaoAvaliacaoOnlineMatriculaEnum;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.processosel.InscricaoVO;
import negocio.comuns.processosel.ResultadoProcessoSeletivoVO;
import negocio.comuns.protocolo.RequerimentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.SituacaoRequerimento;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;

public class JobProcessaRequerimentoTransferenciaNovaMatricula extends SuperFacadeJDBC implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String  requerimentos;
	private UsuarioVO usuario;
    private ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO ;
    private ConfiguracaoFinanceiroVO configuracaoFinanceiroVO ;
    private ConsistirException consistirException ;
    

	public JobProcessaRequerimentoTransferenciaNovaMatricula(String requerimentos , ConfiguracaoGeralSistemaVO confgeral , ConfiguracaoFinanceiroVO confFinanceiro ,  UsuarioVO usuario ) {
		super();
		this.requerimentos = requerimentos;
		this.usuario = usuario;
		this.configuracaoGeralSistemaVO = confgeral;
		this.configuracaoFinanceiroVO = confFinanceiro ;
	}

	@Override
	public void run() {	
			
			try {
				System.out.println("JobProcessaRequerimentoTransferenciaNovaMatricula iniciou - "+Uteis.getDataComHoraCompleta(new Date()));
								
				List<RequerimentoVO> listaRequerimento  = getFacadeFactory().getRequerimentoFacade().consultarPorCodigo(this.requerimentos, false , Uteis.NIVELMONTARDADOS_TODOS, this.usuario, configuracaoFinanceiroVO  );
				if(listaRequerimento != null && listaRequerimento.isEmpty()) {
					throw new Exception("não foi encontrado nenhum requerimento com os dados informados .");
				}
				Iterator it = listaRequerimento.iterator();
				RequerimentoVO obj  = null ;
				while(it.hasNext()) {
					try {						
					    obj = (RequerimentoVO) it.next();					
						if (obj.getTipoRequerimento().getIsTipoTransferenciaInterna()  && !obj.getSituacao().equals(SituacaoRequerimento.FINALIZADO_INDEFERIDO.getValor()) &&  !obj.getSituacao().equals(SituacaoRequerimento.FINALIZADO_DEFERIDO.getValor())) {
							getFacadeFactory().getTransferenciaEntradaFacade().realizarCriacaoTranferenciaInternaGerandoNovaMatriculaAproveitandoDisciplinasAprovadasProximoPeriodoLetivoPorRequerimento(obj, this.configuracaoGeralSistemaVO , this.configuracaoFinanceiroVO ,  false , this.usuario);
							
						}else {
							getConsistirException().adicionarListaMensagemErro("[Codigo requerimento : "+obj.getCodigo()+"]--[Motivo : "+SituacaoRequerimento.getDescricao(obj.getSituacao()) +"]");
						}
				  	}catch (Exception e){
				  		if(!getConsistirException().existeErroListaMensagemErro()) {
				  			getConsistirException().adicionarListaMensagemErro("ouve falha no processamento dos seguintes requerimentos .");
				  		}				  		
				  		getConsistirException().adicionarListaMensagemErro("[Codigo requerimento : "+obj.getCodigo()+"]--[Motivo : "+e.getMessage()+"]");
					}
				}
				if(getConsistirException().existeErroListaMensagemErro()) {
					throw new Exception(getConsistirException().getToStringMensagemErro());
				}
				
				System.out.println("JobProcessaRequerimentoTransferenciaNovaMatricula terminou  - "+Uteis.getDataComHoraCompleta(new Date()));

			 } catch (Exception e) {
					System.out.println("JobProcessaRequerimentoTransferenciaNovaMatricula erro  - "+Uteis.getDataComHoraCompleta(new Date()));
				e.printStackTrace();
			 }
		
	}
	
	


	public UsuarioVO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioVO usuario) {
		this.usuario = usuario;
	}

	public String getRequerimentos() {
		return requerimentos;
	}

	public void setRequerimentos(String requerimentos) {
		this.requerimentos = requerimentos;
	}

	public ConfiguracaoGeralSistemaVO getConfiguracaoGeralSistemaVO() {
		return configuracaoGeralSistemaVO;
	}

	public void setConfiguracaoGeralSistemaVO(ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
		this.configuracaoGeralSistemaVO = configuracaoGeralSistemaVO;
	}

	public ConfiguracaoFinanceiroVO getConfiguracaoFinanceiroVO() {
		return configuracaoFinanceiroVO;
	}

	public void setConfiguracaoFinanceiroVO(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
		this.configuracaoFinanceiroVO = configuracaoFinanceiroVO;
	}
	
	public ConsistirException getConsistirException() {
		if(consistirException == null ) {
			consistirException = new ConsistirException();
		}
		return consistirException;
	}

	public void setConsistirException(ConsistirException consistirException) {
		this.consistirException = consistirException;
	}

}