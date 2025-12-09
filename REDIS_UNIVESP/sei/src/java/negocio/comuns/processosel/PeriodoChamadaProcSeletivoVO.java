package negocio.comuns.processosel;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;


@XmlRootElement(name = "periodoChamadaProcSeletivoVO")
public class PeriodoChamadaProcSeletivoVO extends SuperVO {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private Integer codigo;
	private Integer nrChamada;
	private Date  periodoInicialChamada;
	private Date  periodoFinalChamada;
	private Date  periodoInicialUploadDocumentoIndeferido;
	private Date  periodoFinalUploadDocumentoIndeferido;
	private Date  dataEnvioMensagemAtivacaoMatricula;

	private ProcSeletivoVO procSeletivoVO;
	
	  @XmlElement(name = "codigo")
	public Integer getCodigo() {
		if(codigo == null ) {
			codigo = 0;
		}
		return codigo;
	}
	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}
	
	  @XmlElement(name = "nrChamada")
	public Integer getNrChamada() {
		if(nrChamada == null ) {
			nrChamada = 0;
		}
		return nrChamada;
	}
	
	public void setNrChamada(Integer nrChamada) {
		this.nrChamada = nrChamada;
	}
	
	  @XmlElement(name = "periodoInicialChamada")
	public Date getPeriodoInicialChamada() {
		
		return periodoInicialChamada;
	}
	public void setPeriodoInicialChamada(Date periodoInicialChamada) {
		this.periodoInicialChamada = periodoInicialChamada;
	}
	
	  @XmlElement(name = "periodoFinalChamada")
	public Date getPeriodoFinalChamada() {
		
		return periodoFinalChamada;
	}
	
	public void setPeriodoFinalChamada(Date periodoFinalChamada) {
		this.periodoFinalChamada = periodoFinalChamada;
	}
	
	  @XmlElement(name = "procSeletivoVO")
	public ProcSeletivoVO getProcSeletivoVO() {
		if(procSeletivoVO == null ) {
			procSeletivoVO = new ProcSeletivoVO();
		}
		return procSeletivoVO;
	}
	
	public void setProcSeletivoVO(ProcSeletivoVO procSeletivoVO) {
		this.procSeletivoVO = procSeletivoVO;
	}
	
	
	
    public static void validarDados(final PeriodoChamadaProcSeletivoVO obj)  throws Exception {   	   	    	   	
    	Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getNrChamada()),"O Campo Nr Chamada deve ser informado.");
    	Uteis.checkState(obj.getNrChamada() <=0  ,"O Campo Número Chamada é Invalido.");
    	Uteis.checkState(obj.getPeriodoInicialChamada() == null  ,"O campo Período Inicial Chamada Processo Seletivo deve ser informado.");
    	Uteis.checkState(obj.getPeriodoFinalChamada() == null ,"O campo Período Final Chamada Processo Seletivo deve ser informado.");		
    	Uteis.checkState(obj.getPeriodoInicialChamada().compareTo(obj.getPeriodoFinalChamada()) == 0 ,"O campo DATA Período Final da Chamada do Processo Seletivo deve ser diferente do campo DATA Périodo Inicial Chamada (" + Uteis.getDataComHora(obj.getPeriodoFinalChamada()) + "). ");		
    	Uteis.checkState(obj.getPeriodoInicialChamada().after(obj.getPeriodoFinalChamada()) ,"O campo DATA Período Final da Chamada do Processo Seletivo deve ser maior que o campo DATA Périodo Inicial Chamda (" + Uteis.getDataComHora(obj.getPeriodoFinalChamada()) + "). ");		
    	Uteis.checkState(!Uteis.isAtributoPreenchido(obj.getPeriodoInicialUploadDocumentoIndeferido()) && Uteis.isAtributoPreenchido(obj.getPeriodoFinalUploadDocumentoIndeferido()) ,"O campo Data Inicio Período Adicional Upload Documento Indeferido deve ser informado. ");		
    	Uteis.checkState(Uteis.isAtributoPreenchido(obj.getPeriodoInicialUploadDocumentoIndeferido()) &&  !Uteis.isAtributoPreenchido(obj.getPeriodoFinalUploadDocumentoIndeferido()) ,"O campo Data Final Período Adicional Upload Documento Indeferido deve ser informado. ");		
    	Uteis.checkState((Uteis.isAtributoPreenchido(obj.getPeriodoInicialUploadDocumentoIndeferido()) &&  Uteis.isAtributoPreenchido(obj.getPeriodoFinalUploadDocumentoIndeferido())) && obj.getPeriodoInicialUploadDocumentoIndeferido().before(obj.getPeriodoFinalChamada()) ,"O campo Data Inicio Período Adicional Upload Documento Indeferido deve ser maior que o campo DATA Périodo Final Chamada (" + Uteis.getDataComHora(obj.getPeriodoFinalChamada()) + "). ");		
    	Uteis.checkState(Uteis.isAtributoPreenchido(obj.getPeriodoInicialUploadDocumentoIndeferido()) &&  Uteis.isAtributoPreenchido(obj.getPeriodoFinalUploadDocumentoIndeferido()) && (obj.getPeriodoInicialUploadDocumentoIndeferido().compareTo(obj.getPeriodoFinalUploadDocumentoIndeferido()) >= 0 ),"O campo Data Final Período Adicional Upload Documento Indeferido deve ser maior que o campo Data Inicio Período Adicional Upload Documento Indeferido (" + Uteis.getDataComHora(obj.getPeriodoFinalUploadDocumentoIndeferido()) + "). ");		

    }
    
    public static void validarDadosAdicionarObjLista(final PeriodoChamadaProcSeletivoVO obj , List<PeriodoChamadaProcSeletivoVO> objs)  throws Exception {   	   	
    	
        //O período da 1ª chamada deve ser antes do periodo da 2ª chamada e os períodos não poderão ocorrer dentro da mesma faixa.		
    	for(PeriodoChamadaProcSeletivoVO objPeriodoChamadaProcSeletivoVO  : objs) {   			
			Uteis.checkState(
					obj.getNrChamada() > objPeriodoChamadaProcSeletivoVO.getNrChamada()
					&& obj.getPeriodoInicialChamada().before(objPeriodoChamadaProcSeletivoVO.getPeriodoFinalChamada()),
					"Não foi possivel adicionar um período para a ("+obj.getNrChamada()+ "ª) chamada pois a ("+objPeriodoChamadaProcSeletivoVO.getNrChamada()+
					"ª) chamada está dentro deste período"); 		
    			
			Uteis.checkState(
					obj.getNrChamada() < objPeriodoChamadaProcSeletivoVO.getNrChamada() && obj.getPeriodoFinalChamada()
							.after(objPeriodoChamadaProcSeletivoVO.getPeriodoInicialChamada()),
							"Não foi possivel adicionar um período para a ("+obj.getNrChamada()+ "ª) chamada pois a ("+objPeriodoChamadaProcSeletivoVO.getNrChamada()+
					"ª) chamada está dentro deste período"); 		
    			
    		}
    		
    		
    		
    }
    
    
    public  String getNrChamada_Apresentar() {   	
      return getNrChamada()+"ª Chamada";	
    }
    public  String getPeriodoInicialChamada_Apresentar() {
    	if(getPeriodoInicialChamada() == null ) {
    	 return "";
    	}
    	return (Uteis.getData(getPeriodoInicialChamada() , "dd/MM/yyyy HH:mm"));
    	 		
    }
    public  String getPeriodoFinalChamada_Apresentar() {
    	if(getPeriodoFinalChamada() == null ) {
       	 return "";
       	}
       	return (Uteis.getData(getPeriodoFinalChamada() , "dd/MM/yyyy HH:mm"));
    }
    
    
    public  String getPeriodoInicialAdicionalUploadDocumentoIndeferido_Apresentar() {
    	if(getPeriodoInicialUploadDocumentoIndeferido() == null ) {
    		return "";
    	}
    	return (Uteis.getData(getPeriodoInicialUploadDocumentoIndeferido() , "dd/MM/yyyy HH:mm"));
    	
    }
    public  String getPeriodoFinalAdicionalUploadDocumentoIndeferido_Apresentar() {
    	if(getPeriodoFinalUploadDocumentoIndeferido() == null ) {
    		return "";
    	}
    	return (Uteis.getData(getPeriodoFinalUploadDocumentoIndeferido() , "dd/MM/yyyy HH:mm"));
    }
    
    
	@XmlElement(name = "periodoInicialUploadDocumentoIndeferido")
	public Date getPeriodoInicialUploadDocumentoIndeferido() {
		return periodoInicialUploadDocumentoIndeferido;
	}
	
	public void setPeriodoInicialUploadDocumentoIndeferido(Date periodoInicialUploadDocumentoIndeferido) {
		this.periodoInicialUploadDocumentoIndeferido = periodoInicialUploadDocumentoIndeferido;
	}
	
	@XmlElement(name = "periodoFinalUploadDocumentoIndeferido")
	public Date getPeriodoFinalUploadDocumentoIndeferido() {
		return periodoFinalUploadDocumentoIndeferido;
	}
	public void setPeriodoFinalUploadDocumentoIndeferido(Date periodoFinalUploadDocumentoIndeferido) {
		this.periodoFinalUploadDocumentoIndeferido = periodoFinalUploadDocumentoIndeferido;
	}
    
   
	
	public Boolean getAptoLiberarUploadDocumentoIndeferido() throws ParseException {		
			return	(UteisData.getCompareDataComHora(getPeriodoFinalChamada(),new Date()) <= 0)
			     && Uteis.isAtributoPreenchido(getPeriodoInicialUploadDocumentoIndeferido()) 
			     && Uteis.isAtributoPreenchido(getPeriodoFinalUploadDocumentoIndeferido()) 					
			     && UteisData.getCompareDataComHora(getPeriodoInicialUploadDocumentoIndeferido(),new Date()) <= 0
			     &&  UteisData.getCompareDataComHora(getPeriodoFinalUploadDocumentoIndeferido(),new Date()) >= 0 ;
		
	}
	
	public Boolean getDentroPrazoPeriodoChamada() throws ParseException {		
		return	 Uteis.isAtributoPreenchido(getPeriodoInicialChamada()) &&
				 Uteis.isAtributoPreenchido(getPeriodoFinalChamada())  && 
				 UteisData.getCompareDataComHora(getPeriodoInicialChamada(),new Date()) <= 0 && 
				UteisData.getCompareDataComHora(getPeriodoFinalChamada(),new Date()) >= 0 ;
}
	
	public String getDataEnvioMensagemAtivacaoMatricula_Apresentar() {
		if (getDataEnvioMensagemAtivacaoMatricula() == null) {
			return "";
		}
		return (Uteis.getData(getDataEnvioMensagemAtivacaoMatricula(), "dd/MM/yyyy"));
	}
	public Date getDataEnvioMensagemAtivacaoMatricula() {
		return dataEnvioMensagemAtivacaoMatricula;
	}
	public void setDataEnvioMensagemAtivacaoMatricula(Date dataEnvioMensagemAtivacaoMatricula) {
		this.dataEnvioMensagemAtivacaoMatricula = dataEnvioMensagemAtivacaoMatricula;
	}
    

}
