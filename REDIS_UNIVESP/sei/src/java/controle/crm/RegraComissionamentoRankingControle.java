package controle.crm;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import controle.arquitetura.SuperControle;

@Controller("RegraComissionamentoRankingControle")
@Scope("viewScope")
public class RegraComissionamentoRankingControle extends SuperControle {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6369261636452964261L;
	private String regra;
	
	@PostConstruct
	public void consultarRegraComissionamentoRanking(){
		try{
			setRegra(getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarRegraComissionamentoRanking(getUnidadeEnsinoLogado().getCodigo()));
		}catch(Exception e){
			setRegra("");
		}
		
	}

	public String getRegra() {
		if(regra == null){
			regra = "";
		}
		return regra;
	}

	public void setRegra(String regra) {
		this.regra = regra;
	}
	
	
	
}
