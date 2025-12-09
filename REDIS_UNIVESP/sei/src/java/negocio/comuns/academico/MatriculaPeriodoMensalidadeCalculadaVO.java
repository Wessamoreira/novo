package negocio.comuns.academico;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.financeiro.PlanoFinanceiroAlunoDescricaoDescontosVO;
import negocio.comuns.utilitarias.Uteis;

public class MatriculaPeriodoMensalidadeCalculadaVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3936650361635447487L;
	private Double valorMensalidadeCheio;
	private String nrParcelasPeriodo;
	private String descricaoParcela;
	private List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosMensalidade;
	private Integer parcelaInicial;
	private Integer parcelaFinal;
	private Date vencimentoPrimeiraParcela;
	
	
	
	public MatriculaPeriodoMensalidadeCalculadaVO() {
		super();
		// TODO Auto-generated constructor stub
	}		

	public MatriculaPeriodoMensalidadeCalculadaVO(String descricaoParcela, Double valorMensalidadeCheio, Integer parcelaInicial,Integer parcelaFinal) {
		super();
		this.descricaoParcela = descricaoParcela;
		this.valorMensalidadeCheio = valorMensalidadeCheio;
		this.parcelaInicial = parcelaInicial;
		this.parcelaFinal = parcelaFinal;
	}



	public Double getTotalMensalidadeCheio() {
		return Uteis.arrendondarForcando2CasasDecimais(getValorMensalidadeCheio() * getParcelaFinal());
	}
	
	public Double getValorMensalidadeCheio() {
		if(valorMensalidadeCheio == null){
			valorMensalidadeCheio = 0.0;
		}
		return valorMensalidadeCheio;
	}
	
	public void setValorMensalidadeCheio(Double valorMensalidadeCheio) {
		this.valorMensalidadeCheio = valorMensalidadeCheio;
	}
	
	public String getNrParcelasPeriodo() {
		if(nrParcelasPeriodo == null){
			if(getParcelaInicial().equals(1) && getParcelaFinal().equals(1)){
				nrParcelasPeriodo = getDescricaoParcela();
			}else if(getParcelaInicial().equals(getParcelaFinal())){
				nrParcelasPeriodo = (getParcelaInicial()+"º "+ getDescricaoParcela());
			}else if(getParcelaInicial().equals(getParcelaFinal()-1)){
				nrParcelasPeriodo = getDescricaoParcela()+" "+getParcelaInicial()+" e "+getParcelaFinal();						
			}else if(getParcelaFinal().equals(0)){
				nrParcelasPeriodo = getDescricaoParcela();				
			}else{
				nrParcelasPeriodo = getDescricaoParcela()+" "+getParcelaInicial()+" à "+getParcelaFinal();						
			}			
		}
		return nrParcelasPeriodo;
	}
	
	public void setNrParcelasPeriodo(String nrParcelasPeriodo) {
		this.nrParcelasPeriodo = nrParcelasPeriodo;
	}
	
	public List<PlanoFinanceiroAlunoDescricaoDescontosVO> getListaDescontosMensalidade() {
		if(listaDescontosMensalidade == null){
			listaDescontosMensalidade = new ArrayList<PlanoFinanceiroAlunoDescricaoDescontosVO>(0);
		}
		return listaDescontosMensalidade;
	}
	
	public void setListaDescontosMensalidade(List<PlanoFinanceiroAlunoDescricaoDescontosVO> listaDescontosMensalidade) {
		this.listaDescontosMensalidade = listaDescontosMensalidade;
	}

	public Integer getParcelaInicial() {
		if(parcelaInicial == null){
			parcelaInicial = 0;
		}
		return parcelaInicial;
	}

	public void setParcelaInicial(Integer parcelaInicial) {
		this.parcelaInicial = parcelaInicial;
	}

	public Integer getParcelaFinal() {
		if(parcelaFinal == null){
			parcelaFinal = 0;
		}
		return parcelaFinal;
	}

	public void setParcelaFinal(Integer parcelaFinal) {
		this.parcelaFinal = parcelaFinal;
	}

	public String getDescricaoParcela() {
		if (descricaoParcela == null) {
			descricaoParcela = "";
		}
		return descricaoParcela;
	}

	public void setDescricaoParcela(String descricaoParcela) {
		this.descricaoParcela = descricaoParcela;
	}

	public String getVencimentoPrimeiraParcela_Apresentar() {		
		return getVencimentoPrimeiraParcela() != null ? Uteis.getData(getVencimentoPrimeiraParcela()) : "";
	}
	
	public Date getVencimentoPrimeiraParcela() {		
		return vencimentoPrimeiraParcela;
	}

	public void setVencimentoPrimeiraParcela(Date vencimentoPrimeiraParcela) {
		this.vencimentoPrimeiraParcela = vencimentoPrimeiraParcela;
	}
	
	
	
	
	

}
