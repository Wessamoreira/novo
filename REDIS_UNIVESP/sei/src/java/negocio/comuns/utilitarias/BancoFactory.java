package negocio.comuns.utilitarias;

import java.io.Serializable;

import negocio.comuns.utilitarias.boleto.BoletoBanco;
import negocio.comuns.utilitarias.boleto.JBoletoBean;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.BancoDoBrasilLayout;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.BancoDoBrasilLayoutCNAB240;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.BanestesLayoutCNAB400;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.BradescoLayout;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.CaixaEconomicaLayout;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.CaixaEconomicaSICOOB15Layout;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.CaixaEconomicaSICOOBLayout;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.DaycovalLayout;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.HSBCLayout;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.HSBCLayoutCNAB240;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.ItauLayout;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.ItauLayoutCNAB240;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.ItauLayoutRetornoRemessa;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.LayoutBancos;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.SafraLayout;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.SantanderLayout;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.SantanderLayoutCNAB400;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.SicoobLayout;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.SicoobLayoutCNAB240;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.SicredLayout;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.SicredLayoutCNAB240;
import negocio.comuns.utilitarias.boleto.arquivos.bancos.UnibancoLayout;
import negocio.comuns.utilitarias.boleto.bancos.BancoBrasil;
import negocio.comuns.utilitarias.boleto.bancos.BancoGenerico;
import negocio.comuns.utilitarias.boleto.bancos.BancoReal;
import negocio.comuns.utilitarias.boleto.bancos.Banestes;
import negocio.comuns.utilitarias.boleto.bancos.Bradesco;
import negocio.comuns.utilitarias.boleto.bancos.CaixaEconomica;
import negocio.comuns.utilitarias.boleto.bancos.CaixaEconomicaSicob;
import negocio.comuns.utilitarias.boleto.bancos.CaixaEconomicaSicob15;
import negocio.comuns.utilitarias.boleto.bancos.Daycoval;
import negocio.comuns.utilitarias.boleto.bancos.Hsbc;
import negocio.comuns.utilitarias.boleto.bancos.Itau;
import negocio.comuns.utilitarias.boleto.bancos.NossaCaixa;
import negocio.comuns.utilitarias.boleto.bancos.Safra;
import negocio.comuns.utilitarias.boleto.bancos.Santander;
import negocio.comuns.utilitarias.boleto.bancos.Sicoob;
import negocio.comuns.utilitarias.boleto.bancos.Sicred;
import negocio.comuns.utilitarias.boleto.bancos.Unibanco;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.facade.jdbc.financeiro.remessa.CNAB240.BancoBrasilControleRemessaCNAB240;
import negocio.facade.jdbc.financeiro.remessa.CNAB240.CaixaEconomicaControleRemessaCNAB240;
import negocio.facade.jdbc.financeiro.remessa.CNAB240.ItauControleRemessaCNAB240;
import negocio.facade.jdbc.financeiro.remessa.CNAB240.SantanderControleRemessaCNAB240;
import negocio.facade.jdbc.financeiro.remessa.CNAB240.SicoobControleRemessaCNAB240;
import negocio.facade.jdbc.financeiro.remessa.CNAB240.SicoobCredSaudeControleRemessaCNAB240;
import negocio.facade.jdbc.financeiro.remessa.CNAB240.SicrediControleRemessaCNAB240;
import negocio.facade.jdbc.financeiro.remessa.CNAB400.BancoBrasilControleRemessaCNAB400;
import negocio.facade.jdbc.financeiro.remessa.CNAB400.BanestesControleRemessaCNAB400;
import negocio.facade.jdbc.financeiro.remessa.CNAB400.BradescoControleRemessaCNAB400;
import negocio.facade.jdbc.financeiro.remessa.CNAB400.DaycovalControleRemessaCNAB400;
import negocio.facade.jdbc.financeiro.remessa.CNAB400.ItauControleRemessaCNAB400;
import negocio.facade.jdbc.financeiro.remessa.CNAB400.SafraControleRemessaCNAB400;
import negocio.facade.jdbc.financeiro.remessa.CNAB400.SantanderControleRemessaCNAB400;
import negocio.facade.jdbc.financeiro.remessa.CNAB400.SicoobControleRemessaCNAB400;
import negocio.facade.jdbc.financeiro.remessa.CNAB400.SicoobCredSaudeControleRemessaCNAB400;
import negocio.facade.jdbc.financeiro.remessa.CNAB400.SicredControleRemessaCNAB400;
import negocio.facade.jdbc.financeiro.remessa.contapagar.BradescoRemessaContaPagarCNAB240;
import negocio.facade.jdbc.financeiro.remessa.contapagar.CaixaEconomicaRemessaContaPagarCNAB240;
import negocio.facade.jdbc.financeiro.remessa.contapagar.ItauRemessaContaPagarCNAB240;
import negocio.facade.jdbc.financeiro.remessa.contapagar.SantanderRemessaContaPagarCNAB240;
import negocio.facade.jdbc.financeiro.remessa.contapagar.SicoobRemessaContaPagarCNAB240;
import negocio.facade.jdbc.financeiro.remessa.contapagar.CNAB400.BradescoRemessaContaPagarCNAB400;
import negocio.interfaces.financeiro.remessa.ControleRemessaCNAB240LayoutInterfaceFacade;
import negocio.interfaces.financeiro.remessa.ControleRemessaCNAB400LayoutInterfaceFacade;
import negocio.interfaces.financeiro.remessa.ControleRemessaContaPagarLayoutInterfaceFacade;

public class BancoFactory implements Serializable {

    public static LayoutBancos getLayoutInstancia(int codigoBanco, String tipoCNAB) throws Exception {
        Bancos layout = Bancos.getEnum(codigoBanco);

        if (layout.getNumeroBanco().equals("001") && tipoCNAB.equals("CNAB240")) {
            return new BancoDoBrasilLayoutCNAB240();
        } else if (layout.getNumeroBanco().equals("033") && tipoCNAB.equals("CNAB400")) {
        	return new SantanderLayoutCNAB400();
        } else if (layout.getNumeroBanco().equals("399") && tipoCNAB.equals("CNAB240")) {
        	return new HSBCLayoutCNAB240();
        } else {
            if (layout != null) {

                switch (layout) {
                    case BRADESCO:
                        return new BradescoLayout();
                    case UNIBANCO:
                        return new UnibancoLayout();
                    case BANCO_DO_BRASIL:
                        return new BancoDoBrasilLayout();
                    case HSBC:
                        return new HSBCLayout();
                    case SANTANDER:
                        return new SantanderLayout();
                    case ITAU:                    
                    	if (tipoCNAB.equals("CNAB240")) {
                    		return new ItauLayoutCNAB240();
                    	} else {
                    		 return new ItauLayout();                   		
                    	}
                    case SAFRA:
                    	return new SafraLayout();
                    case CAIXA_ECONOMICA_FEDERAL:
                        return new CaixaEconomicaLayout();
                    case CAIXA_ECONOMICA_FEDERAL_SICOB:
                    	return new CaixaEconomicaSICOOBLayout();
                    case CAIXA_ECONOMICA_FEDERAL_SICOB_15:
                    	return new CaixaEconomicaSICOOB15Layout();
                    case ITAU_RETORNO_REMESSA:
                        return new ItauLayoutRetornoRemessa();
                    case DAYCOVAL:
                    	return new DaycovalLayout();
                    case SICRED:
                    	if (tipoCNAB.equals("CNAB240")) {
                    		return new SicredLayoutCNAB240();
                    	} else {
                    		return new SicredLayout();                    		
                    	}
                    	
                    case SICOOB:
                    	if (tipoCNAB.equals("CNAB240")) {
                    		return new SicoobLayoutCNAB240();
                    	} else {
                    		return new SicoobLayout();                    		
                    	}
                    case BANESTE: 
                    	return new BanestesLayoutCNAB400();
                }

            } else {
                throw new ConsistirException("Selecione um Banco");
            }
        }

        return null;
    }
    
    public static ControleRemessaCNAB400LayoutInterfaceFacade getLayoutInstanciaControleRemessaCNAB400(String numeroBanco, String tipoCNAB) throws Exception {
    	Bancos layout = Bancos.getEnum(numeroBanco);
    	
    	if (layout == null && numeroBanco.equals("756CREDSAUDE") && tipoCNAB.equals("CNAB400")) {
    		return new SicoobCredSaudeControleRemessaCNAB400();
    	} else if (layout.getNumeroBanco().equals(Bancos.BRADESCO.getNumeroBanco()) && tipoCNAB.equals("CNAB400")) {
    		return new BradescoControleRemessaCNAB400();
    	} else if (layout.getNumeroBanco().equals(Bancos.SAFRA.getNumeroBanco()) && tipoCNAB.equals("CNAB400")) {
    		return new SafraControleRemessaCNAB400();
    	} else if (layout.getNumeroBanco().equals(Bancos.SANTANDER.getNumeroBanco()) && tipoCNAB.equals("CNAB400")) {
    		return new SantanderControleRemessaCNAB400();
    	} else if (layout.getNumeroBanco().equals(Bancos.BANCO_DO_BRASIL.getNumeroBanco()) && tipoCNAB.equals("CNAB400")) {
    		return new BancoBrasilControleRemessaCNAB400();			
    	} else if (layout.getNumeroBanco().equals(Bancos.SICOOB.getNumeroBanco()) && tipoCNAB.equals("CNAB400")) {
    		return new SicoobControleRemessaCNAB400();
    	} else if (layout.getNumeroBanco().equals(Bancos.SICRED.getNumeroBanco()) && tipoCNAB.equals("CNAB400")) {
    		return new SicredControleRemessaCNAB400();
    	} else if (layout.getNumeroBanco().equals(Bancos.DAYCOVAL.getNumeroBanco())) {
    		return new DaycovalControleRemessaCNAB400();
    	} else if (layout.getNumeroBanco().equals(Bancos.BANESTE.getNumeroBanco())) {
    		return new BanestesControleRemessaCNAB400();
    	} else {
    		if (layout != null) {
    			
    			switch (layout) {
    			case ITAU:
    				return new ItauControleRemessaCNAB400();
				default:
					break;
    			}
    			
    		} else {
    			throw new ConsistirException("Selecione um Banco");
    		}
    	}
    	
    	return null;
    }

    public static ControleRemessaCNAB240LayoutInterfaceFacade getLayoutInstanciaControleRemessaCNAB240(String numeroBanco, String tipoCNAB) throws Exception {
    	Bancos layout = Bancos.getEnum(numeroBanco);
    	
    	if (layout == null && numeroBanco.equals("756CREDSAUDE") && tipoCNAB.equals("CNAB240")) {
    		return new SicoobCredSaudeControleRemessaCNAB240();    	
    	} else if (layout.getNumeroBanco().equals(Bancos.BANCO_DO_BRASIL.getNumeroBanco()) && tipoCNAB.equals("CNAB240")) {
    		return new BancoBrasilControleRemessaCNAB240();
    	} else if (layout.getNumeroBanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco()) && tipoCNAB.equals("CNAB240")) {
    		return new CaixaEconomicaControleRemessaCNAB240();
    	} else if (layout.getNumeroBanco().equals(Bancos.SANTANDER.getNumeroBanco()) && tipoCNAB.equals("CNAB240")) {
    		return new SantanderControleRemessaCNAB240();
    	} else if (layout.getNumeroBanco().equals(Bancos.SICOOB.getNumeroBanco()) && tipoCNAB.equals("CNAB240")) {
    		return new SicoobControleRemessaCNAB240();
    	}else if (layout.getNumeroBanco().equals(Bancos.ITAU.getNumeroBanco()) && tipoCNAB.equals("CNAB240")) {
    		return new ItauControleRemessaCNAB240();
    	}else if (layout.getNumeroBanco().equals(Bancos.SICRED.getNumeroBanco()) && tipoCNAB.equals("CNAB240")) {
    		return new SicrediControleRemessaCNAB240();
    	}else { 
    		if (layout != null) {
    			
    			switch (layout) {
    			case BANCO_DO_BRASIL:
    				return new BancoBrasilControleRemessaCNAB240();
				default:
					break;
    			}
    			
    		} else {
    			throw new ConsistirException("Selecione um Banco");
    		}    		
    	}    	
    	return null;
    }
    


    public static BoletoBanco getBoletoInstancia(String numeroBanco, JBoletoBean jBoletoBean) throws Exception {
        Bancos layout = Bancos.getEnum(numeroBanco);

        if (layout != null) {

            switch (layout) {
                case BRADESCO:
                    return new Bradesco(jBoletoBean);
                case UNIBANCO:
                    return new Unibanco(jBoletoBean);
                case REAL:
                    return new BancoReal(jBoletoBean);
                case BANCO_DO_BRASIL:
                    return new BancoBrasil(jBoletoBean);
                case CAIXA_ECONOMICA_FEDERAL:
                    return new CaixaEconomica(jBoletoBean);
                case CAIXA_ECONOMICA_FEDERAL_SICOB:
                	return new CaixaEconomicaSicob(jBoletoBean);
                case CAIXA_ECONOMICA_FEDERAL_SICOB_15:
                	return new CaixaEconomicaSicob15(jBoletoBean);
                case HSBC:
                    return new Hsbc(jBoletoBean);
                case ITAU:
                    return new Itau(jBoletoBean);
                case SAFRA:
                	return new Safra(jBoletoBean);
                case SANTANDER:
                    return new Santander(jBoletoBean);
                case NOSSA_CAIXA:
                    return new NossaCaixa(jBoletoBean);
                case SICRED:
                	return new Sicred(jBoletoBean);
                case SICOOB:
                	return new Sicoob(jBoletoBean);
                case DAYCOVAL:
                	return new Daycoval(jBoletoBean);                	
                case BANESTE:
                	return  new Banestes(jBoletoBean);

            }

        } else {
            return new BancoGenerico(jBoletoBean);
        }
        return null;
    }
	
    public static ControleRemessaContaPagarLayoutInterfaceFacade getLayoutInstanciaControleRemessaContaPagar(String numeroBanco, String tipoCNAB) throws Exception {
    	Bancos layout = Bancos.getEnum(numeroBanco);
        switch (layout) {
        case BRADESCO:        	
        		return new BradescoRemessaContaPagarCNAB240();        	
        case SANTANDER:
            return new SantanderRemessaContaPagarCNAB240();
		case BANCO_DO_BRASIL:
			break;
		case CAIXA_ECONOMICA_FEDERAL:
			return new CaixaEconomicaRemessaContaPagarCNAB240();
		case CAIXA_ECONOMICA_FEDERAL_SICOB:
			break;
		case CAIXA_ECONOMICA_FEDERAL_SICOB_15:
			break;
		case DAYCOVAL:
			break;
		case HSBC:
			break;
		case ITAU:
			 return new ItauRemessaContaPagarCNAB240();
		case ITAU_RETORNO_REMESSA:
			break;
		case NOSSA_CAIXA:
			break;
		case REAL:
			break;
		case SICOOB:
			return new SicoobRemessaContaPagarCNAB240();
		case SICRED:
			break;
		case UNIBANCO:
			break;
		default:
			break;
        }
        throw new ConsistirException("Selecione um Banco");
    }	
}
