package negocio.comuns.utilitarias.faturamento.nfe;

import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import negocio.comuns.faturamento.nfe.DadosEnvioVO;
import negocio.comuns.faturamento.nfe.ItemNfeVO;
import negocio.comuns.faturamento.nfe.NfeVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;

/**
 *
 * @author Euripedes Doutor
 */
public class GerarXMLEnvio {

    public GerarXMLEnvio() throws Exception {
    }

    public String gerarXMLEnvio(DadosEnvioVO dadosEnvio, NfeVO nfeVO) throws Exception {
        try {
            Document nfe;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            nfe = builder.newDocument();
            nfe.setXmlStandalone(true);
            nfe.setXmlVersion("1.0");

            gerarXml(nfe, nfeVO);
            return assinarXml(dadosEnvio, nfe, Servicos.ENVIAR);
        } catch (Exception e) {
            throw e;
        }
    }

    private String assinarXml(DadosEnvioVO dadosEnvio, Document nfe, String tipoServico) throws Exception {
        return Assinador.assinarNFe(dadosEnvio, nfe, tipoServico);
    }

    private void gerarXml(Document nfe, NfeVO obj) throws Exception {

        Element enviNFe = nfe.createElement("enviNFe");
        enviNFe.setAttribute("versao", DadosNfe.VERSAO_LAYOUT);
        enviNFe.setAttribute("xmlns", DadosNfe.NFE_XMLS);
        Element idLote = nfe.createElement("idLote");
        idLote.setTextContent(Uteis.getPreencherComZeroEsquerda(Uteis.removerMascara(obj.getIdLote()), 15));
        
        Element indSinc = nfe.createElement("indSinc");
        indSinc.setTextContent(obj.getIndSinc());
        
        enviNFe.appendChild(idLote);
        enviNFe.appendChild(indSinc);
        
        Element NFe = nfe.createElement("NFe");
        Element infNFe = nfe.createElement("infNFe");
        infNFe.setAttribute("versao", DadosNfe.VERSAO_LAYOUT);
        obj.setIdNfe(UteisNfe.criarIdNFe(Integer.parseInt(Uteis.removerMascara(obj.getNNF2())), EstadosCodigoIBGE.obterCodigoIBGEEstado(obj.getUfEmit()), obj.getDataEmissao(), obj.getCnpjEmit(), obj.getModelo(), obj.getSerie(), obj.getTipoEmissao()));
        infNFe.setAttribute("Id", obj.getIdNfe());
        gerarXmlIde(nfe, infNFe, obj);
        gerarXmlEmitente(nfe, infNFe, obj);
        gerarXmlDestinatario(nfe, infNFe, obj);
        gerarXmlItemNotaFiscal(nfe, infNFe, obj);
        gerarXmlTotal(nfe, infNFe, obj);
        gerarXmlTransporte(nfe, infNFe, obj);
//        gerarXmlCobranca(nfe, infNFe, obj.getContaReceberNfeVOs());
        gerarXmlInformacoesAdicionais(nfe, infNFe, obj.getObservacao(), obj.getObservacaoContribuinte());
        NFe.appendChild(infNFe);
        enviNFe.appendChild(NFe);
        nfe.appendChild(enviNFe);

    }
    
    private void gerarXmlTransporte(Document nfe, Element infNFe, NfeVO obj) throws Exception {
    	Element transp = nfe.createElement("transp");
    	
        Element modFrete = nfe.createElement("modFrete");
        modFrete.setTextContent("9");
        transp.appendChild(modFrete);
    	
    	infNFe.appendChild(transp);
    }

    private void gerarXmlInformacoesAdicionais(Document nfe, Element infNFe, String obs, String obsCont) {
        if (!obs.trim().equals("") || !obsCont.trim().equals("")) {
            Element infAdic = nfe.createElement("infAdic");
            if (!obs.trim().equals("")) {
                Element infAdFisco = nfe.createElement("infAdFisco");
                infAdFisco.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obs.trim())).trim());
                infAdic.appendChild(infAdFisco);
            }
            if (!obsCont.trim().equals("")) {
                Element infCpl = nfe.createElement("infCpl");
                infCpl.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obsCont.trim())).trim().replaceAll("\r", ""));
                infAdic.appendChild(infCpl);
            }
            infNFe.appendChild(infAdic);
        }

    }

    private void gerarXmlTotal(Document nfe, Element infNFe, NfeVO obj) throws Exception {
        Element total = nfe.createElement("total");
        
        gerarXmlICMSTot(nfe, total, obj);
        gerarXmlISSQNtot(nfe, total, obj);
        if (obj.getPossuiAliquotasEspecificasParceiro()) {
        	gerarXmlRetTrib(nfe, total, obj);
        }        
        infNFe.appendChild(total);
    }
    
    private void gerarXmlRetTrib(Document nfe, Element infNFe, NfeVO obj) throws Exception {
    	Element retTrib = nfe.createElement("retTrib");
    	
    	if (!obj.getValorRetidoPIS().equals("0.00")) {
    		Element vRetPIS = nfe.createElement("vRetPIS");
    		vRetPIS.setTextContent(obj.getValorRetidoPIS());
    		retTrib.appendChild(vRetPIS);
    	}

    	if (!obj.getValorRetidoCOFINS().equals("0.00")) {
    		Element vRetCOFINS = nfe.createElement("vRetCOFINS");
    		vRetCOFINS.setTextContent(obj.getValorRetidoCOFINS());
    		retTrib.appendChild(vRetCOFINS);
    	}
        
    	if (!obj.getValorRetidoCSLL().equals("0.00")) {
    		Element vRetCSLL = nfe.createElement("vRetCSLL");
    		vRetCSLL.setTextContent(obj.getValorRetidoCSLL());
    		retTrib.appendChild(vRetCSLL);
    	}
        
    	if(!obj.getBaseCalculoIRRF().equals("0.0") && !obj.getBaseCalculoIRRF().equals("0.00")) {
    		Element vBCIRRF = nfe.createElement("vBCIRRF");
    		vBCIRRF.setTextContent(obj.getBaseCalculoIRRF());
    		retTrib.appendChild(vBCIRRF);
    	}
        
    	if (!obj.getValorRetidoIRRF().equals("0.00")) {
    		Element vIRRF = nfe.createElement("vIRRF");
    		vIRRF.setTextContent(obj.getValorRetidoIRRF());
    		retTrib.appendChild(vIRRF);
    	}
        
    	if (!obj.getBaseCalculoRetencaoPrevidenciaSocial().equals("0.00") && !obj.getBaseCalculoRetencaoPrevidenciaSocial().equals("0.0")) {
    		Element vBCRetPrev = nfe.createElement("vBCRetPrev");
    		vBCRetPrev.setTextContent(obj.getBaseCalculoRetencaoPrevidenciaSocial());
    		retTrib.appendChild(vBCRetPrev);
    	}
        
    	if (!obj.getValorRetidoPrevidenciaSocial().equals("0.00")) {
    		Element vRetPrev = nfe.createElement("vRetPrev");
    		vRetPrev.setTextContent(obj.getValorRetidoPrevidenciaSocial());
    		retTrib.appendChild(vRetPrev);
    	}
        
        infNFe.appendChild(retTrib);
    }
    
    private void gerarXmlICMSTot(Document nfe, Element infNFe, NfeVO obj) throws Exception {
	    Element ICMSTot = nfe.createElement("ICMSTot");
	    
        Element vBC = nfe.createElement("vBC");
        vBC.setTextContent("0.00");
        ICMSTot.appendChild(vBC);
        
        Element vICMS = nfe.createElement("vICMS");
        vICMS.setTextContent("0.00");
        ICMSTot.appendChild(vICMS);
        
        Element vICMSDeson = nfe.createElement("vICMSDeson");
        vICMSDeson.setTextContent("0.00");
        ICMSTot.appendChild(vICMSDeson);
        
        Element vBCST = nfe.createElement("vBCST");
        vBCST.setTextContent("0.00");
        ICMSTot.appendChild(vBCST);        
        
        Element vST = nfe.createElement("vST");
        vST.setTextContent("0.00");
        ICMSTot.appendChild(vST);  
        
        Element vProd = nfe.createElement("vProd");
        vProd.setTextContent("0.00");
        ICMSTot.appendChild(vProd); 
        
        Element vFrete = nfe.createElement("vFrete");
        vFrete.setTextContent("0.00");
        ICMSTot.appendChild(vFrete); 
        
        Element vSeg = nfe.createElement("vSeg");
        vSeg.setTextContent("0.00");
        ICMSTot.appendChild(vSeg);
        
        Element vDesc = nfe.createElement("vDesc");
        vDesc.setTextContent("0.00");
        ICMSTot.appendChild(vDesc);
        
        Element vII = nfe.createElement("vII");
        vII.setTextContent("0.00");
        ICMSTot.appendChild(vII);
        
        Element vIPI = nfe.createElement("vIPI");
        vIPI.setTextContent("0.00");
        ICMSTot.appendChild(vIPI);
        
        Element vPIS = nfe.createElement("vPIS");
        vPIS.setTextContent("0.00");
        ICMSTot.appendChild(vPIS);
        
        Element vCOFINS = nfe.createElement("vCOFINS");
        vCOFINS.setTextContent("0.00");
        ICMSTot.appendChild(vCOFINS);
        
        Element vOutro = nfe.createElement("vOutro");
        vOutro.setTextContent("0.00");
        ICMSTot.appendChild(vOutro);
        
        Element vNF = nfe.createElement("vNF");
        vNF.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(obj.getValorTotal())));
        ICMSTot.appendChild(vNF);
        
        Element vTotTrib = nfe.createElement("vTotTrib");
        vTotTrib.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(obj.getTotalIssqn()));
        ICMSTot.appendChild(vTotTrib);
        
	    infNFe.appendChild(ICMSTot);
    }
    
    private void gerarXmlISSQNtot(Document nfe, Element infNFe, NfeVO obj) throws Exception {
    	Element ISSQNtot = nfe.createElement("ISSQNtot");

        Element vServ = nfe.createElement("vServ");
        vServ.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(obj.getValorTotal())));
        ISSQNtot.appendChild(vServ);

        Element vBC = nfe.createElement("vBC");
        vBC.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(obj.getValorTotal())));
        ISSQNtot.appendChild(vBC);

        if(!Uteis.arrendondarForcando2CadasDecimaisStr(obj.getTotalIssqn()).equals("0.00")) {
        	Element vISS = nfe.createElement("vISS");
        	vISS.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(obj.getTotalIssqn()));
        	ISSQNtot.appendChild(vISS);
        }

        if(!(obj.getValorTotalPIS().equals("0.0") || obj.getValorTotalPIS().equals("0.00") || obj.getValorTotalPIS().equals(""))) {
        	Element vPIS = nfe.createElement("vPIS");
            vPIS.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(obj.getValorTotalPIS())));
            ISSQNtot.appendChild(vPIS);
        } 
        if(!(obj.getValorTotalCOFINS().equals("0.0") || obj.getValorTotalCOFINS().equals("0.00") || obj.getValorTotalCOFINS().equals(""))) {
        	Element vCOFINS = nfe.createElement("vCOFINS");
        	vCOFINS.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(obj.getValorTotalCOFINS())));
        	ISSQNtot.appendChild(vCOFINS);
        } 
        
        Element dCompet = nfe.createElement("dCompet");
        dCompet.setTextContent(UteisData.getDataAplicandoFormatacao(new Date(), "yyyy-MM-dd"));
        ISSQNtot.appendChild(dCompet);
        
        if(obj.getIssRetido()){
            Element vISSRet = nfe.createElement("vISSRet");
            vISSRet.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(obj.getValorRetencaoIss())));
            ISSQNtot.appendChild(vISSRet);
        }
        
        infNFe.appendChild(ISSQNtot);
    }

    private void gerarXmlItemNotaFiscal(Document nfe, Element infNFe, NfeVO nfeVO) throws Exception {
        int cont = 1;
        for (ItemNfeVO item : nfeVO.getItemNfeVOs()) {
            Element det = nfe.createElement("det");
            det.setAttribute("nItem", String.valueOf(cont));
            gerarXmlProdutoNotaFiscal(nfe, det, item, nfeVO);

            Element imposto = nfe.createElement("imposto");
            
            Element vTotTrib = nfe.createElement("vTotTrib");
            
            vTotTrib.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(item.getValorImpostoIssqn())));
            imposto.appendChild(vTotTrib);
            
//            gerarXMLICMS(nfe, imposto, item, nfeVO);
//            gerarXMLIPI(nfe, imposto, item, nfeVO);
            gerarXMLISSQN(nfe, imposto, item, nfeVO);
            gerarXMLPIS(nfe, imposto, item, nfeVO);
            gerarXMLCOFINS(nfe, imposto, item, nfeVO);
            
            det.appendChild(imposto);
            infNFe.appendChild(det);
            cont++;
        }

    }
    
    private void gerarXMLICMS(Document nfe, Element imposto, ItemNfeVO item, NfeVO nfeVO) throws Exception {
    	Element ICMS = nfe.createElement("ICMS");
    	
    	Element ICMS00 = nfe.createElement("ICMS00");
    	
        Element orig = nfe.createElement("orig");
        orig.setTextContent("0");
        ICMS00.appendChild(orig);
        
        Element CST = nfe.createElement("CST");
        CST.setTextContent("00");
        ICMS00.appendChild(CST);
        
        Element modBC = nfe.createElement("modBC");
        modBC.setTextContent("0");
        ICMS00.appendChild(modBC); 
        
        Element vBC = nfe.createElement("vBC");
        vBC.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(nfeVO.getValorTotal())));
        ICMS00.appendChild(vBC); 
        
        Element pICMS = nfe.createElement("pICMS");
        pICMS.setTextContent("0.00");
        ICMS00.appendChild(pICMS); 
        
        Element vICMS = nfe.createElement("vICMS");
        vICMS.setTextContent("0.00");
        ICMS00.appendChild(vICMS);  
    	
    	ICMS.appendChild(ICMS00);
    	
    	imposto.appendChild(ICMS);
    }
    
    private void gerarXMLIPI(Document nfe, Element imposto, ItemNfeVO item, NfeVO nfeVO) throws Exception {
    	Element IPI = nfe.createElement("IPI");
    	
        Element CNPJProd = nfe.createElement("CNPJProd");
        CNPJProd.setTextContent("00000000000000");
        IPI.appendChild(CNPJProd);
        
        Element cEnq = nfe.createElement("cEnq");
        cEnq.setTextContent("999");
        IPI.appendChild(cEnq);
        
        Element IPITrib = nfe.createElement("IPITrib");
        
        Element CST = nfe.createElement("CST");
        CST.setTextContent("00");
        IPITrib.appendChild(CST);  
        
        Element vBC = nfe.createElement("vBC");
        vBC.setTextContent("0.00");
        IPITrib.appendChild(vBC);  
        
        Element pIPI = nfe.createElement("pIPI");
        pIPI.setTextContent("0.00");
        IPITrib.appendChild(pIPI);  
        
        Element qUnid = nfe.createElement("qUnid");
        qUnid.setTextContent("0");
        IPITrib.appendChild(qUnid);
        
        Element vIPI = nfe.createElement("vIPI");
        vIPI.setTextContent("0.00");
        IPITrib.appendChild(vIPI); 
        
//        Element x = nfe.createElement("x");
//
//        Element qUnid = nfe.createElement("qUnid");
//        qUnid.setTextContent("0");
//        x.appendChild(qUnid);    
//        
//        Element vUnid = nfe.createElement("vUnid");
//        vUnid.setTextContent("0");
//        x.appendChild(vUnid);  
//        
//        IPITrib.appendChild(x);  
        
        IPI.appendChild(IPITrib);   
        
    	imposto.appendChild(IPI);
    }
    
    private void gerarXMLPIS(Document nfe, Element imposto, ItemNfeVO item, NfeVO nfeVO) throws Exception {
    	Element PIS = nfe.createElement("PIS");
        if (item.getCstPIS().equals("01") || 
        		item.getCstPIS().equals("02")) {
        	Element PISAliq = nfe.createElement("PISAliq");
        	Element CST = nfe.createElement("CST");
        	CST.setTextContent(item.getCstPIS());
        	PISAliq.appendChild(CST);
        	Element vBC = nfe.createElement("vBC");
            vBC.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(item.getValorProd())));
            PISAliq.appendChild(vBC);
            if(item.getAliquotaPIS().equals("")) {
            	throw new Exception(UteisJSF.internacionalizar("msg_NotaFiscalSaida_aliquotaPISObrigatoriaVazia"));
            }
            double valorAliq = Double.valueOf(item.getAliquotaPIS());
            if(valorAliq == 0.0 || valorAliq == 0.00) {
            	throw new Exception(UteisJSF.internacionalizar("msg_NotaFiscalSaida_aliquotaPISObrigatoriaVazia"));
            }
            Element pPIS = nfe.createElement("pPIS");
            pPIS.setTextContent(String.valueOf(valorAliq));
            PISAliq.appendChild(pPIS);
            Element vPIS = nfe.createElement("vPIS");
            vPIS.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(item.getValorTotalPIS())));
            PISAliq.appendChild(vPIS);
            PIS.appendChild(PISAliq);
        } else if (item.getCstPIS().equals("04") || 
        		item.getCstPIS().equals("05") || 
        		item.getCstPIS().equals("06") || 
        		item.getCstPIS().equals("07") || 
        		item.getCstPIS().equals("08") || 
        		item.getCstPIS().equals("09")) {
        	Element PISNT = nfe.createElement("PISNT");
        	Element CST = nfe.createElement("CST");
        	CST.setTextContent(item.getCstPIS());
        	PISNT.appendChild(CST); 
        	PIS.appendChild(PISNT);
        } else {
        	throw new Exception(UteisJSF.internacionalizar("msg_NotaFiscalSaida_cstPISObrigatoriaVazia"));
        }
    	imposto.appendChild(PIS);
    }
    
    private void gerarXMLCOFINS(Document nfe, Element imposto, ItemNfeVO item, NfeVO nfeVO) throws Exception {
    	Element COFINS = nfe.createElement("COFINS");
    	if (item.getCstCOFINS().equals("01") || 
    			item.getCstCOFINS().equals("02")) {
        	Element COFINSAliq = nfe.createElement("COFINSAliq");
        	Element CST = nfe.createElement("CST");
        	CST.setTextContent(item.getCstCOFINS());
        	COFINSAliq.appendChild(CST);
        	Element vBC = nfe.createElement("vBC");
            vBC.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(item.getValorProd())));
            COFINSAliq.appendChild(vBC);
            if(item.getAliquotaCOFINS().equals("")) {
            	throw new Exception(UteisJSF.internacionalizar("msg_NotaFiscalSaida_aliquotaCOFINSObrigatoriaVazia"));
            }
            double valorAliq = Double.valueOf(item.getAliquotaCOFINS());
            if(valorAliq == 0.0 || valorAliq == 0.00) {
            	throw new Exception(UteisJSF.internacionalizar("msg_NotaFiscalSaida_aliquotaCOFINSObrigatoriaVazia"));
            }
            int aux = (int) valorAliq;
            Element pCOFINS = nfe.createElement("pCOFINS");
            pCOFINS.setTextContent(String.valueOf(aux));
            COFINSAliq.appendChild(pCOFINS);
            Element vCOFINS = nfe.createElement("vCOFINS");
            vCOFINS.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(item.getValotTotalCOFINS())));
            COFINSAliq.appendChild(vCOFINS);
            COFINS.appendChild(COFINSAliq);
        } else if (item.getCstCOFINS().equals("04") || 
        		item.getCstCOFINS().equals("05") || 
        		item.getCstCOFINS().equals("06") || 
        		item.getCstCOFINS().equals("07") || 
        		item.getCstCOFINS().equals("08") || 
        		item.getCstCOFINS().equals("09")) {
        	Element COFINSNT = nfe.createElement("COFINSNT");
        	Element CST = nfe.createElement("CST");
        	CST.setTextContent(item.getCstCOFINS());
        	COFINSNT.appendChild(CST); 
        	COFINS.appendChild(COFINSNT);
        } else {
        	throw new Exception(UteisJSF.internacionalizar("msg_NotaFiscalSaida_cstCOFINSObrigatoriaVazia"));
        }
    	imposto.appendChild(COFINS);
    }
    
    private void gerarXMLISSQN(Document nfe, Element imposto, ItemNfeVO item, NfeVO nfeVO) throws Exception {
    	Element ISSQN = nfe.createElement("ISSQN");
        
        Element vBC = nfe.createElement("vBC");
        vBC.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(item.getValorProd())));
        ISSQN.appendChild(vBC);
        
		double valorAliq = Double.valueOf(item.getAliquotaIssqn());
		int aliq = (int) valorAliq;
        Element vAliq = nfe.createElement("vAliq");
        vAliq.setTextContent(String.valueOf(aliq));
        ISSQN.appendChild(vAliq);
        
        Element vISSQN = nfe.createElement("vISSQN");
        vISSQN.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(item.getValorImpostoIssqn())));
        ISSQN.appendChild(vISSQN);
        
        Element cMunFG = nfe.createElement("cMunFG");    
        cMunFG.setTextContent(nfeVO.getCodigoIBGEMunicipio());
        ISSQN.appendChild(cMunFG);
        
        Element cListServ = nfe.createElement("cListServ");    
        cListServ.setTextContent("08.01");
        ISSQN.appendChild(cListServ);
        
        if(nfeVO.getIssRetido()){
            Element vISSRet = nfe.createElement("vISSRet");
            vISSRet.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(item.getValorTotalRetencaoIss())));
            ISSQN.appendChild(vISSRet);
        }
        
        Element indISS = nfe.createElement("indISS");    
        indISS.setTextContent("1");
        ISSQN.appendChild(indISS);
        
        Element cServico = nfe.createElement("cServico");    
        cServico.setTextContent("04090");
        ISSQN.appendChild(cServico);
        
        Element cMun = nfe.createElement("cMun");    
        cMun.setTextContent(nfeVO.getCodigoIBGEMunicipio());
        ISSQN.appendChild(cMun);
        
        Element cPais = nfe.createElement("cPais");    
        cPais.setTextContent(nfeVO.getPaisEmit());
        ISSQN.appendChild(cPais);
        
        /*
         * Indicador de incentivo Fiscal 
         * 1=Sim; 2=Não;
         */
        Element indIncentivo = nfe.createElement("indIncentivo");    
        indIncentivo.setTextContent("1");
        ISSQN.appendChild(indIncentivo);
        
        imposto.appendChild(ISSQN);
    }

    private void gerarXmlProdutoNotaFiscal(Document nfe, Element det, ItemNfeVO obj, NfeVO nota) throws Exception {
        Element prod = nfe.createElement("prod");

        Element cProd = nfe.createElement("cProd");
        cProd.setTextContent(String.valueOf(obj.getCodigoProduto()));
        prod.appendChild(cProd);

        Element cEAN = nfe.createElement("cEAN");
        cEAN.setTextContent("");
        prod.appendChild(cEAN);

        Element xProd = nfe.createElement("xProd");
        xProd.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obj.getNomeProduto().trim())).trim());
        prod.appendChild(xProd);
        
		Element NCM = nfe.createElement("NCM");
		String codNcm = Uteis.removerMascara(obj.getNcm());
		NCM.setTextContent(codNcm);
		prod.appendChild(NCM);

        Element CFOP = nfe.createElement("CFOP");
        CFOP.setTextContent(Uteis.removerMascara(obj.getCfop()));
        prod.appendChild(CFOP);

        Element uCom = nfe.createElement("uCom");
        uCom.setTextContent("UN");
        prod.appendChild(uCom);

        Element qCom = nfe.createElement("qCom");
        qCom.setTextContent("1");
        prod.appendChild(qCom);

        Element vUnCom = nfe.createElement("vUnCom");
        vUnCom.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(obj.getValorProd())));
        prod.appendChild(vUnCom);

        Element vProd = nfe.createElement("vProd");
        vProd.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(obj.getValorProd())));
        prod.appendChild(vProd);

        Element cEANTrib = nfe.createElement("cEANTrib");
        cEANTrib.setTextContent("");
        prod.appendChild(cEANTrib);

        Element uTrib = nfe.createElement("uTrib");
        uTrib.setTextContent("UN");
        prod.appendChild(uTrib);

        Element qTrib = nfe.createElement("qTrib");
        qTrib.setTextContent("1");        
        prod.appendChild(qTrib);

        Element vUnTrib = nfe.createElement("vUnTrib");
        vUnTrib.setTextContent(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(obj.getValorProd())));
        prod.appendChild(vUnTrib);

        Element indTot = nfe.createElement("indTot");
        indTot.setTextContent("1");
        prod.appendChild(indTot);

        det.appendChild(prod);
    }

    private void gerarXmlDestinatario(Document nfe, Element infNFe, NfeVO obj) throws Exception {
        Element dest = nfe.createElement("dest");
        String cpfCnpj = "";
        Element cpfCnpjTag;

        if (obj.getTipoPessoaDest().equals("FI")) {
            cpfCnpjTag = nfe.createElement("CPF");
            cpfCnpj = Uteis.removerMascara(obj.getCpfDest().trim());
            
            if(!Uteis.validaCPF(cpfCnpj)) {
            	throw new Exception("O CPF do Destinatário da Nota está inválido.");
            }
            
        } else {
            cpfCnpjTag = nfe.createElement("CNPJ");
            cpfCnpj = Uteis.removerMascara(obj.getCnpjDest().trim());
        }
        cpfCnpjTag.setTextContent(cpfCnpj);
        dest.appendChild(cpfCnpjTag);
        if (obj.getNomeDest().trim().equals("")) {
            throw new Exception("Nome do Destinatário da Nota deve ser informado.");
        }
        Element xNome = nfe.createElement("xNome");
        xNome.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obj.getNomeDest())).trim());
        dest.appendChild(xNome);

        gerarXmlEnderecoDestinatario(nfe, dest, obj);

        Element indIEDest = nfe.createElement("indIEDest");
        if(obj.getTipoPessoaDest().equals("FI")) {
        	indIEDest.setTextContent("9");
        	dest.appendChild(indIEDest);
        } else if (obj.getTipoPessoaDest().equals("JU") && obj.getInscricaoEstDest().trim().equalsIgnoreCase("ISENTO")) {
        	indIEDest.setTextContent("9");
        	dest.appendChild(indIEDest);
//        	Element IE = nfe.createElement("IE");
//        	IE.setTextContent("");
//        	dest.appendChild(IE);
        } else if (obj.getTipoPessoaDest().equals("JU") && !obj.getInscricaoEstDest().trim().isEmpty()) {
        	indIEDest.setTextContent("1");
        	dest.appendChild(indIEDest);
			Element IE = nfe.createElement("IE");
			IE.setTextContent(UteisNfe.removeCaractersEspeciais(obj.getInscricaoEstDest().trim()));
			dest.appendChild(IE);
        } else {
        	throw new Exception(UteisJSF.internacionalizar("msg_NotaFiscalSaida_campoInscricaoEstaducalObrigatarioNoCadastroParceiro"));
        }
        infNFe.appendChild(dest);

    }

    private void gerarXmlEnderecoDestinatario(Document nfe, Element dest, NfeVO obj) throws Exception {
        Element enderDest = nfe.createElement("enderDest");
        if (obj.getLogradouroDest().trim().equals("")) {
            throw new Exception("O Endereço do Destinatario deve ser informado.");
        }
        if (obj.getLogradouroDest().length() > 60) {
            obj.setLogradouroDest(obj.getLogradouroDest().substring(0, 60));
        }
        Element xLgr = nfe.createElement("xLgr");
        xLgr.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obj.getLogradouroDest().trim())).trim());
        enderDest.appendChild(xLgr);

        if (UteisNfe.retirarCaracteresEspeciais(obj.getNrDest().trim()).trim().equals("")) {
            throw new Exception("O campo Número do Endereço do Destinatario deve se informado.");
        }
        Element nro = nfe.createElement("nro");
        nro.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obj.getNrDest().trim())).trim());
        enderDest.appendChild(nro);


        if (UteisNfe.retirarCaracteresEspeciais(obj.getBairroDest().trim()).trim().equals("")) {
            throw new Exception("O campo Bairro do Destinatario deve se informado.");
        }
        Element xBairro = nfe.createElement("xBairro");
        xBairro.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentos(obj.getBairroDest().trim())).trim());
        enderDest.appendChild(xBairro);


        if (obj.getCodigoIBGEMunicipioDest().trim().equals("") || obj.getCodigoIBGEMunicipioDest().trim().length() < 7) {
            throw new Exception("Código IBGE da cidade do Destinatário está inválido.");
        }
        Element cMun = nfe.createElement("cMun");
        cMun.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obj.getCodigoIBGEMunicipioDest().toString().trim())));
        enderDest.appendChild(cMun);

        Element xMun = nfe.createElement("xMun");
        xMun.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obj.getMunicipioDest().trim())).trim());
        enderDest.appendChild(xMun);

        if (obj.getUfDest().trim().equals("") || obj.getUfDest().trim().length() < 2) {
            throw new Exception("Sigla do Estado do Destinatario está inválido.");
        }
        Element UF = nfe.createElement("UF");
        UF.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obj.getUfDest()).trim()));
        enderDest.appendChild(UF);

        if (!obj.getCepDest().trim().equals("") && Uteis.removerMascara(obj.getCepDest()).trim().length() == 8) {
            Element CEP = nfe.createElement("CEP");
            CEP.setTextContent(Uteis.removerMascara(obj.getCepDest().trim()));
            enderDest.appendChild(CEP);
        }

        Element cPais = nfe.createElement("cPais");
        cPais.setTextContent(obj.getPaisDest());
        enderDest.appendChild(cPais);

        Element xPais = nfe.createElement("xPais");
        xPais.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obj.getNomePaisDest())));
        enderDest.appendChild(xPais);
        Element fone = nfe.createElement("fone");

        fone.setTextContent(Uteis.removerMascara(Uteis.retirarSinaisSimbolosEspacoString(obj.getFoneDest().trim())));

        if (!fone.getTextContent().trim().equals("")) {
            enderDest.appendChild(fone);
        }

        dest.appendChild(enderDest);
    }

    private void gerarXmlEmitente(Document nfe, Element infNFe, NfeVO obj) throws Exception {
        Element emit = nfe.createElement("emit");
        Element CNPJ = nfe.createElement("CNPJ");
        CNPJ.setTextContent(Uteis.removerMascara(obj.getCnpjEmit()));
        emit.appendChild(CNPJ);

        Element xNome = nfe.createElement("xNome");
        xNome.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentos(obj.getNomeEmit())).trim());
        emit.appendChild(xNome);

        Element xFant = nfe.createElement("xFant");
        xFant.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obj.getNomeFantasiaEmit())).trim());
        emit.appendChild(xFant);

        gerarXmlEnderecoEmitente(nfe, emit, obj);

        Element IE = nfe.createElement("IE");
        IE.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.retirarSinaisSimbolosEspacoString(obj.getInscricaoEstEmit())).trim());
        emit.appendChild(IE);
        
        if (Double.parseDouble(obj.getTotalSubTrib()) != 0.0) {
        	if (!obj.getInscricaoEstDest().trim().isEmpty() && !obj.getInscricaoEstDest().trim().equalsIgnoreCase("ISENTO")) {
	            Element IEST = nfe.createElement("IEST");
	            IEST.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.retirarSinaisSimbolosEspacoString(obj.getInscricaoEstDest())).trim());
	            emit.appendChild(IEST);
        	}
        }
        
        Element IM = nfe.createElement("IM");
        IM.setTextContent(obj.getInscricaoMunicipalEmit());
        emit.appendChild(IM);

        Element CRT = nfe.createElement("CRT");
        CRT.setTextContent(obj.getCrt());
        emit.appendChild(CRT);
        
        infNFe.appendChild(emit);
    }

    private void gerarXmlEnderecoEmitente(Document nfe, Element emit, NfeVO obj) throws Exception {
        Element enderEmit = nfe.createElement("enderEmit");

        if (!UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obj.getLogradouroEmit())).trim().equals("")) {
            Element xLgr = nfe.createElement("xLgr");
            xLgr.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obj.getLogradouroEmit())).trim());
            enderEmit.appendChild(xLgr);
        }

        if (!UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obj.getNrEmit())).trim().equals("")) {
            Element nro = nfe.createElement("nro");
            nro.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obj.getNrEmit())).trim());
            enderEmit.appendChild(nro);
        }

        if (!UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obj.getBairoEmit())).trim().equals("")) {
            Element xBairro = nfe.createElement("xBairro");
            xBairro.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obj.getBairoEmit())).trim());
            enderEmit.appendChild(xBairro);
        }

        Element cMun = nfe.createElement("cMun");
        cMun.setTextContent(Uteis.removerAcentuacao(obj.getCodigoIBGEMunicipio()));
        enderEmit.appendChild(cMun);

        Element xMun = nfe.createElement("xMun");
        xMun.setTextContent(Uteis.removerAcentuacao(obj.getMunicipioEmit()));
        enderEmit.appendChild(xMun);
        if (obj.getUfEmit().trim().length() < 2) {
            throw new Exception("Sigla do Estado do Emitente está inválido.");
        }
        Element UF = nfe.createElement("UF");
        UF.setTextContent(Uteis.removerAcentuacao(EstadosSiglas.obterSiglaEstado(obj.getUfEmit())));
        enderEmit.appendChild(UF);

        if (UteisNfe.removeCaractersEspeciais(obj.getCepEmit()).length() == 8) {
            Element CEP = nfe.createElement("CEP");
            CEP.setTextContent(UteisNfe.removeCaractersEspeciais(obj.getCepEmit()));
            enderEmit.appendChild(CEP);
        }

        Element cPais = nfe.createElement("cPais");
        cPais.setTextContent(obj.getPaisEmit());
        enderEmit.appendChild(cPais);

        Element xPais = nfe.createElement("xPais");
        xPais.setTextContent(obj.getNomePaisEmit());
        enderEmit.appendChild(xPais);

        if (!Uteis.retirarMascaraTelefone(obj.getFoneEmit()).equals("")) {
            Element fone = nfe.createElement("fone");
            fone.setTextContent(Uteis.retirarMascaraTelefone(obj.getFoneEmit()));
            enderEmit.appendChild(fone);
        }

        emit.appendChild(enderEmit);
    }

    private void gerarXmlIde(Document nfe, Element infNFe, NfeVO obj) throws Exception {
        Element ide = nfe.createElement("ide");
        Element cUF = nfe.createElement("cUF");
        cUF.setTextContent(EstadosCodigoIBGE.obterCodigoIBGEEstado(obj.getUfEmit()));
        ide.appendChild(cUF);

        Element cNF = nfe.createElement("cNF");
        String nrCodigoBarra = Uteis.getPreencherComZeroEsquerda(Uteis.removerMascara(obj.getnNF()), 8);
        cNF.setTextContent(nrCodigoBarra.trim());
        ide.appendChild(cNF);

        Element natOp = nfe.createElement("natOp");
        natOp.setTextContent(UteisNfe.retirarCaracteresEspeciais(Uteis.removerAcentuacao(obj.getNaturezaOperacao().toString()).trim()));
        ide.appendChild(natOp);

        Element indPag = nfe.createElement("indPag");
        obj.setFormaPagamento("0");
//        if (!obj.getFormaPagamento().equals("0") && !obj.getFormaPagamento().equals("1") && !obj.getFormaPagamento().equals("2")) {
//            throw new Exception("Informe uma forma de pagamento válida. (0)-À vista; (1)-À prazo; (2)-Outros;");
//        }
        indPag.setTextContent(obj.getFormaPagamento().trim());

        ide.appendChild(indPag);

        Element mod = nfe.createElement("mod");
        if (obj.getModelo().equals("1") || obj.getModelo().equals("01") || obj.getModelo().equalsIgnoreCase("1A")) {
            mod.setTextContent("55");
        } else {
            mod.setTextContent(obj.getModelo().trim());
        }
        ide.appendChild(mod);

        Element serie = nfe.createElement("serie");
        serie.setTextContent(obj.getSerie().trim());
        ide.appendChild(serie);

        Element nNF = nfe.createElement("nNF");
        nNF.setTextContent((new Integer((Uteis.removerMascara(obj.getNNF2())).trim())).toString());
        ide.appendChild(nNF);

        Element dhEmi = nfe.createElement("dhEmi");
        dhEmi.setTextContent(obj.getDataEmissao()+Uteis.getFusoHorario());
        ide.appendChild(dhEmi);

        if (!obj.getDataSaidaEntrada().equals("")) {
            Element dhSaiEnt = nfe.createElement("dhSaiEnt");
            String dhSaidaEntrada = UteisData.getDataAplicandoFormatacao(new Date(), "yyyy-MM-dd'T'HH:mm:ss");
            dhSaiEnt.setTextContent(dhSaidaEntrada+Uteis.getFusoHorario());
            ide.appendChild(dhSaiEnt);
        }

        Element tpNF = nfe.createElement("tpNF");
        tpNF.setTextContent(obj.getTpNF());
        ide.appendChild(tpNF);
        
        Element idDest = nfe.createElement("idDest");
        idDest.setTextContent(obj.getIdLocalDestinoOperacao());
        ide.appendChild(idDest);

        Element cMunFG = nfe.createElement("cMunFG");
        cMunFG.setTextContent(obj.getCodigoIBGEMunicipio());
        ide.appendChild(cMunFG);

        Element tpImp = nfe.createElement("tpImp");
        tpImp.setTextContent(obj.getTipoImpressao());
        ide.appendChild(tpImp);

        Element tpEmis = nfe.createElement("tpEmis");
        tpEmis.setTextContent(obj.getTipoEmissao());
        ide.appendChild(tpEmis);

        Element cDV = nfe.createElement("cDV");
        cDV.setTextContent(obj.getIdNfe().substring(obj.getIdNfe().length() - 1));
        ide.appendChild(cDV);

        Element tpAmb = nfe.createElement("tpAmb");
        tpAmb.setTextContent(obj.getAmbiente());
        ide.appendChild(tpAmb);

        Element finNFe = nfe.createElement("finNFe");
        finNFe.setTextContent(obj.getFinalidadeEmissao());
        ide.appendChild(finNFe);
        
        Element indFinal = nfe.createElement("indFinal");
        indFinal.setTextContent(obj.getIndOperacaoConsumidorFinal());
        ide.appendChild(indFinal);
        
        Element indPres = nfe.createElement("indPres");
        indPres.setTextContent(obj.getIndPresencaComprador());
        ide.appendChild(indPres);

        Element procEmi = nfe.createElement("procEmi");
        procEmi.setTextContent(obj.getProcessoEmissao());
        ide.appendChild(procEmi);

        Element verProc = nfe.createElement("verProc");
        verProc.setTextContent(DadosNfe.VERSAO_APLICATIVO);
        ide.appendChild(verProc);

        infNFe.appendChild(ide);

    }
    
}
