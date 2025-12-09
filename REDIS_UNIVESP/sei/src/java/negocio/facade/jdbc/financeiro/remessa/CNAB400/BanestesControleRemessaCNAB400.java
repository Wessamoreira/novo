package negocio.facade.jdbc.financeiro.remessa.CNAB400;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaVO;
import negocio.comuns.financeiro.RegistroDetalheVO;
import negocio.comuns.financeiro.RegistroHeaderVO;
import negocio.comuns.financeiro.RegistroTrailerVO;
import negocio.comuns.utilitarias.Comando;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.EditorOC;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.remessa.ControleRemessaCNAB400LayoutInterfaceFacade;

public class BanestesControleRemessaCNAB400 extends ControleAcesso implements ControleRemessaCNAB400LayoutInterfaceFacade {

    private static final String PATTERN = "ddMMyy";
    public Double valorTotal = 0.0;    

    public BanestesControleRemessaCNAB400() {
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public EditorOC executarGeracaoDadosArquivoRemessa(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        EditorOC editorOC = new EditorOC();
        Comando cmd = new Comando();
        
        controleRemessaVO.setNumeroIncremental(1);
        //Gera o Header
        gerarDadosHeaderBANESTES(editorOC, cmd, controleRemessaVO, controleRemessaVO.getUnidadeEnsinoVO(), configuracaoFinanceiroVO, usuario);
        //Gera o Detalhe
        executarGeracaoDetalheBANESTES(listaDadosRemessaVOs, editorOC, cmd, controleRemessaVO.getDataInicio(), controleRemessaVO.getDataFim(), controleRemessaVO, configuracaoFinanceiroVO, usuario);
        //Gera o Trailler
        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);

        if (controleRemessaVO.getContaCorrenteVO().getUtilizaCobrancaPartilhada()) {
        	gerarDadosTraillerBANESTES(editorOC, cmd, controleRemessaVO.getNumeroIncremental(), (controleRemessaVO.getNumeroIncremental() - 2)/2);            
            String linha = "";
//            linha += "\r";
            cmd.adicionarLinhaComando(linha, 0);        	
        } else {
        	gerarDadosTraillerBANESTES(editorOC, cmd, controleRemessaVO.getNumeroIncremental(), controleRemessaVO.getNumeroIncremental());
        }

        editorOC.adicionarComando(cmd);
        return editorOC;
    }
        
        
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarGeracaoDetalheBANESTES(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, EditorOC editorOC, Comando cmd, Date dataInicio, Date dataFim, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        try {
            if (!listaDadosRemessaVOs.isEmpty()) {
                Iterator i = listaDadosRemessaVOs.iterator();
                while (i.hasNext()) {
                	
                    ControleRemessaContaReceberVO dadosRemessaVO = (ControleRemessaContaReceberVO) i.next();
                    if (dadosRemessaVO.getApresentarArquivoRemessa()) {
                    	if (!dadosRemessaVO.getBaixarConta()) {
	                    	valorTotal += dadosRemessaVO.getValorComAcrescimo();
	                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
	                        gerarDadosTransacaoBANESTES(editorOC, cmd, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), controleRemessaVO, "01");
	    					if (controleRemessaVO.getContaCorrenteVO().getUtilizaCobrancaPartilhada()) {
	    						controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
	    						gerarDadosTransacaoPartilhado(editorOC, cmd, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), controleRemessaVO);
	    					}
                    	} else {
	                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
	                        gerarDadosTransacaoBANESTES(editorOC, cmd, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), controleRemessaVO, "02");
                    	}
                    }

                }
            } else {
                throw new Exception("Nenhuma conta foi encontrada com os dados informados, por favor verifique o período.");
            }
            getFacadeFactory().getContaReceberFacade().alterarDataArquivoRemessa(listaDadosRemessaVOs, new Date(), usuarioVO);
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

   

   
    public void gerarDetalheSegmentoP(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleRemessaContaReceberVO dadosRemessaVO) throws Exception {
    	String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "033", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental()), 9, 13, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "P", 14, 14, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 15, 15, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodMovRemessa(), 16, 17, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), 18, 21, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 22, 22, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getNumero(), 23, 31, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getDigito(), 32, 32, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getNumero(), 33, 41, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getDigito(), 42, 42, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 43, 44, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero().toString(), 45, 57, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "5", 58, 58, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 59, 59, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 60, 60, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 61, 61, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 62, 62, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNrDocumento().toString(), 63, 77, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 78, 85, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorComAcrescimo())), 13), 86, 100, "0", false, false);                
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), 101, 104, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 105, 105, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 106, 106, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "04", 107, 108, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 109, 109, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyyyy"), 110, 117, " ", false, false);
        Double juro = 0.01;
        Double valorTitulo = dadosRemessaVO.getValorComAcrescimo();
        Double juroFinal = (valorTitulo * juro) / 30;
        String juroStr = "";
        if (juroFinal.toString().length() > 4) {
        	juroStr = (juroFinal.toString()).substring(0, 4);
        } else {
        	juroStr = juroFinal.toString();
        }
        Double juroDouble = new Double(juroStr);
        if (juroDouble > 0) {
            linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 118, 118, " ", false, false); // Código do Juros de Mora - C018
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 119, 126, " ", false, false); // Data do Juros de Mora - C019
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(juroStr)), 13), 127, 141, "0", false, false);
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 118, 118, " ", false, false); // Código do Juros de Mora - C018
            linha = editorOC.adicionarCampoLinhaVersao2(linha, "00000000", 119, 126, " ", false, false); // Data do Juros de Mora - C019
            linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 127, 141, "0", false, false);
        }
        //JUROS DE 1 DIA DE ATRASO
     
        
        //DATA LIMITE PARA CONCESSÃO DE DESCONTO
        if (dadosRemessaVO.getValorDescontoDataLimite() != 0 
        		&& (dadosRemessaVO.getDataLimiteConcessaoDesconto() == null || 
        			(dadosRemessaVO.getDataLimiteConcessaoDesconto() != null && 
        				(dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >0 
        						|| Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto()).equals(Uteis.getData(controleRemessaVO.getDataGeracao()))
        				)
        			)
        			)
        	) {        
		//if (dadosRemessaVO.getValorDescontoDataLimite() != 0 && dadosRemessaVO.getDataLimiteConcessaoDesconto() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >=0 ) {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 142, 142, "0", false, false); // Código do Desconto 1 - C021
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto(), "ddMMyyyy"), 143, 150, " ", false, false);
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite(), 2)))), 13), 151, 165, "0", false, false);                    
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 142, 142, "0", false, false); // Código do Desconto 1 - C021
            linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 143, 150, "0", false, false); // Data do Desconto 1 - C022                	
            linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 151, 165, "0", false, false); // Valor/Percentual a ser Concedido - C023
        }
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 166, 180, "0", false, false); // Valor do IOF a ser Recolhido - C024
      //ABATIMENTO
        if (controleRemessaVO.getContaCorrenteVO().getCarteiraRegistrada() && controleRemessaVO.getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
	        //VALOR DO TITULO
        	if (dadosRemessaVO.getValorBaseComAcrescimo() > 0 && dadosRemessaVO.getValorBaseComAcrescimo() > dadosRemessaVO.getValorComAcrescimo()) {
        		Double valorDescFinal = dadosRemessaVO.getValorBaseComAcrescimo() - dadosRemessaVO.getValorComAcrescimo();
        		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(valorDescFinal)), 13), 181, 195, "0", false, false);        		
        	} else {
            	if (dadosRemessaVO.getValorAbatimento() > 0) {
            		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorAbatimento())), 13), 181, 195, "0", false, false);
            	} else {
            		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 181, 195, "0", false, false);
            	}         	
        	}
        } else {
        	if (dadosRemessaVO.getValorAbatimento() > 0) {
        		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorAbatimento())), 13), 181, 195, "0", false, false);
        	} else {
        		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 181, 195, "0", false, false);
        	}         	
        }
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero(), 196, 220, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 221, 221, "0", false, false); // Código para Protesto - C026
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 222, 223, "0", false, false); // Número de Dias para Protesto - C027
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 224, 224, "0", false, false); // Código para Baixa/Devolução - C028
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 225, 225, "0", false, false); // zero
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "30", 226, 227, "0", false, false); // Número de Dias para Baixa/Devolução - C029
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 228, 229, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 230, 239, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 240, 240, " ", false, false);
        //linha += "\r\n";
        cmd.adicionarLinhaComando(linha, 0);
    }
    
    public void gerarDetalheSegmentoQ(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleRemessaContaReceberVO dadosRemessaVO) throws Exception {
    	String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "033", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental()), 9, 13, "0", false, false); // Nº Sequencial do Registro no Lote - G038
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "Q", 14, 14, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 15, 15, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 16, 17, "0", false, false); // Código de Movimento Remessa - C004
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 18, 18, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(dadosRemessaVO.getNumeroInscricao()), 19, 33, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getNomeSacado()), 40), 34, 73, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getLogradouro()), 40), 74, 113, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getBairro()), 15), 114, 128, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(Uteis.removerAcentuacao(dadosRemessaVO.getCep())), 129, 136, " ", false, false);        
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.retornarPrefixoOuSufixoDoCep(Uteis.removerMascara(Uteis.removerAcentuacao(dadosRemessaVO.getCep())), "prefixo"), 129, 133, "0", false, false);
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.retornarPrefixoOuSufixoDoCep(Uteis.removerMascara(Uteis.removerAcentuacao(dadosRemessaVO.getCep())), "sufixo"), 134, 136, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getCidade()), 15), 137, 151, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getEstado()) , 2), 152, 153, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 154, 154, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 155, 169, "0", false, false); // Número de Inscrição - G006
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 170, 209, " ", false, false); 
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000", 210, 212, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 213, 215, "0", false, false); // Nosso Nº no Banco Correspondente - C032
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 216, 218, "0", false, false); // Nosso Nº no Banco Correspondente - C032
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 219, 221, "0", false, false); // Nosso Nº no Banco Correspondente - C032
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 222, 232, " ", false, false); // Nosso Nº no Banco Correspondente - C032
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 233, 240, " ", false, false);
        //linha += "\r\n";
        cmd.adicionarLinhaComando(linha, 0);
    }

    public void gerarDetalheSegmentoR(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleRemessaContaReceberVO dadosRemessaVO) throws Exception {
    	String linha = "";
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "033", 1, 3, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental()), 9, 13, "0", false, false); // Nº Sequencial do Registro no Lote - G038
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "R", 14, 14, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 15, 15, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 16, 17, "0", false, false); // Código de Movimento Remessa - C004
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 18, 18, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 19, 26, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 27, 41, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 42, 65, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 66, 66, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 67, 74, "0", false, false);
        //JUROS DE 1 DIA DE ATRASO
        Double juro = 0.01;
        Double valorTitulo = dadosRemessaVO.getValorComAcrescimo();
        Double juroFinal = (valorTitulo * juro) / 30;
        String juroStr = "";
        String multaStr = "000000000000200";
        if (juroFinal.toString().length() > 4) {
        	juroStr = (juroFinal.toString()).substring(0, 4);
        } else {
        	juroStr = juroFinal.toString();
        }    	
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(multaStr)), 15), 75, 89, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 90, 99, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 100, 179, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 180, 240, " ", false, false);
    	//linha += "\r\n";
    	cmd.adicionarLinhaComando(linha, 0);
    }

    public void gerarDetalheSegmentoS(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleRemessaContaReceberVO dadosRemessaVO) throws Exception {
    	String linha = "";
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "033", 1, 3, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental()), 9, 13, "0", false, false); // Nº Sequencial do Registro no Lote - G038
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "S", 14, 14, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 15, 15, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 16, 17, "0", false, false); // Código de Movimento Remessa - C004
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 18, 18, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 19, 65, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 66, 66, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 67, 240, " ", false, false);
    	//linha += "\r\n";
    	cmd.adicionarLinhaComando(linha, 0);
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarGeracaoDetalheCNAB240(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        try {
            if (!listaDadosRemessaVOs.isEmpty()) {
                Iterator i = listaDadosRemessaVOs.iterator();
                while (i.hasNext()) {
                    ControleRemessaContaReceberVO dadosRemessaVO = (ControleRemessaContaReceberVO) i.next();
                    if (dadosRemessaVO.getValorComAcrescimo() > 0) {
	                    if (dadosRemessaVO.getApresentarArquivoRemessa()) {
                    		if (dadosRemessaVO.getCodMovRemessa().equals("01")) {                        
		                        gerarDetalheSegmentoP(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO);
		                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
		                        gerarDetalheSegmentoQ(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO);                        
		                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
		                        gerarDetalheSegmentoR(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO);
		                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
		                        gerarDetalheSegmentoS(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO);
		                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
		                    } else {
		                        gerarDetalheSegmentoP(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO);
		                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);                    	
		                    }
	                    }
                    }
                }
            } else {
                throw new Exception("Nenhuma conta foi encontrada com os dados informados, por favor verifique o período.");
            }
            getFacadeFactory().getContaReceberFacade().alterarDataArquivoRemessa(listaDadosRemessaVOs, new Date(), usuarioVO);
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gerarDadosHeaderBANESTES(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 1, 1, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 2, 2, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "REMESSA", 3, 9, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 10, 11, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "COBRANCA", 12, 26, " ", true, false);     
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getNumeroDigitoSemEspaco(), 27, 37, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 38, 46, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(unidadeEnsinoVO.getRazaoSocial()), 30), 47, 76, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "021", 77, 79, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "BANESTES", 80, 94, " ", true, false);    
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyy"), 95, 100, " ", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 101, 394, " ", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000001", 395, 400, " ", false, false);
        linha += "\r";
        //controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
        cmd.adicionarLinhaComando(linha, 0);
    }

  
  
    
    public void gerarDadosTransacaoBANESTES(EditorOC editorOC, Comando cmd, ControleRemessaContaReceberVO dadosRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial, ControleRemessaVO controleRemessaVO, String codMovRemessa) throws Exception {
    	String linha = "";
    	//TIPO DE REGISTRO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 1, 1, " ", false, false);
    	//CÓDIGO DE INSCRIÇÃO EMPRESA
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "02", 2, 3, " ", false, false);
    	//Nº DE INSCRIÇÃO DA EMPRESA(CPF/CNPJ)
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 4, 17, "0", false, false);
    	// IDENTIFICAÇÃO DA EMPRESA NO BANESTES    	
         //CONTA MOVIMENTO CEDENTE
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getContaCorrente()+dadosRemessaVO.getDigitoContaCorrente(), 18, 28, "0", false, false);
         //NR CONTROLE PARTICIPANTE, CONTROLE CEDENTE        
         linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 29, 37, " ", false, false);
    	//IDENTIFICAÇÃO DA OPERACAO NA  EMPRESA 
         linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(dadosRemessaVO.getNossoNumero()), 38, 62, "0", false, false);        	
    	//NOSSO NÚMERO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(dadosRemessaVO.getNossoNumero()), 63, 72, "0", false, false);
    	//CODIGO MULTA
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 73, 73, "0", false, false);
    	// VALOR MULTA   	
    	
    	Double juro = 0.01;
    	Double valorTitulo = dadosRemessaVO.getValorComAcrescimo();
    	Double juroFinal = (valorTitulo * juro) / 30;
    	String juroStr = "";
    	String multaStr = "000000000000200";
    	if (juroFinal.toString().length() > 4) {
    		juroStr = (juroFinal.toString()).substring(0, 4);
    	} else {
    		juroStr = juroFinal.toString();
    	}    	
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(multaStr)), 13), 74, 82, " ", false, false);

    	//linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 74, 82, " ", false, false);
        //IDENTIFICACAO DO CARNÊ 
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 83, 88, " ", false, false);
    	// NUMERO PARCELAS CARNÊ 
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 89, 90, " ", false, false);
    	//QUANTIDADE PARCELAS DO CARNÊ
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 91, 92, " ", false, false);
    	// TIPO INSCRIÇÃO DO SACADOR AVALISTA  CPF 1 CNPJ 2 
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 93, 93, " ", false, false);
    	//INSCRIÇÃO DO SACADOR AVALISTA 
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()), 11).replaceAll(" ", ""),94, 107, "0", false, false);
        //CODIGO DA CARTEIRA 
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCarteira(), 108, 108, " ", false, false);
    	// IDENTIFICACAO OCORRENCIA 
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, codMovRemessa, 109, 110, " ", false, false);
    	//Nº DOCUMENTO 
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(dadosRemessaVO.getNrDocumento()), 111, 120, " ", true, false);
    	//DATA VENCIMENTO 
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), PATTERN), 121, 126, " ", false, false);
    	//VALOR NOMINAL TITULO   	
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(dadosRemessaVO.getValorComAcrescimo().toString())), 13), 127, 139, " ", false, false);
    	//CÓDIGO DO BANCO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Bancos.BANESTE.getNumeroBanco(), 140, 142, " ", false, false);
    	//AGÊNCIA COBRADORA
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "00501", 143, 147, " ", false, false);
    	//ESPÉCIE DO TÍTULO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 148, 149, " ", false, false);
    	//ACEITE
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 150, 150, " ", false, false);
    	//DATA DE EMISSÃO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(new Date(), PATTERN), 151, 156, " ", false, false);

    	//INSTRUÇÃO 1 - PROTESTAR ??
    	if (controleRemessaVO.getContaCorrenteVO().getHabilitarProtestoBoleto()) {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "P6", 157, 158, " ", false, false);
    	} else {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "P7", 157, 158, " ", false, false);
    	}
    	//INSTRUÇÃO 2
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 159, 160, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 161, 161, " ", false, false);
        //JUROS DE 1 DIA DE ATRASO
        Double juros = 0.01;
        Double valorTitulos = dadosRemessaVO.getValorComAcrescimo();
        
        Double juroFinalTemp = (valorTitulos * juros);
        Double jurosFinal = 0.0;
        if (juroFinalTemp < 1) {
        	jurosFinal = juroFinalTemp;
        } else {
        	jurosFinal = (valorTitulos * juros) / 30;        	
        }
        
        String juroString = jurosFinal.toString();
        if (juroString.length() > 4) {
        	juroString = (juroString).substring(0, 4);        	
        }
        
    	//JUROS DE 1 DIA DE ATRASO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(juroString)), 13), 162, 173, " ", false, false);
    	
        if (dadosRemessaVO.getValorDescontoDataLimite() != 0 && dadosRemessaVO.getDataLimiteConcessaoDesconto() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >=0 ) {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto(), "ddMMyy"), 174, 179, " ", false, false);
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(dadosRemessaVO.getValorDescontoDataLimite().toString())), 13), 180, 192, "0", false, false);
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 193, 205, "0", false, false); 
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 206, 218, "0", false, false); 
    	} else {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 174, 179, "0", false, false); // Código do Desconto 1 - C021
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 180, 192, "0", false, false); // Data do Desconto 1 - C022                	
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 193, 205, "0", false, false); // Valor/Percentual a ser Concedido - C023
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 206, 218, "0", false, false); // Valor/Percentual a ser Concedido - C023
    	}
    	//CÓDIGO DE INSCRIÇÃO  01=CPF  -  02=CNPJ
        String nr = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()).replaceAll(" ", "");
		if (nr.length() == 14) {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "02", 219, 220, " ", false, false);
		}else {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 219, 220, " ", false, false);
		}
    	//NÚMERO DE INSCRIÇÃO  CPF OU CNPJ
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()), 11).replaceAll(" ", ""), 221, 234, "0", false, false);
    	//NOME DO SACADO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNomeSacado().toString())), 235, 274, " ", true, false);
    	
    	//LOGRADOURO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getLogradouro())), 275, 314, " ", true, false);
    	//BAIRRO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getBairro())), 315, 326, " ", true, false);
    	//CEP
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getCep()).replaceAll(" ", "")), 327, 334, " ", true, false);
    	//CIDADE
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getCidade())), 335, 349, " ", true, false);
    	//SIGLA DO ESTADO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getEstado(), 350, 351, " ", true, false);
    	
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getUnidadeEnsinoVO().getNome(), 352, 391, " ", true, false);    
    	//BRANCOS    	
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 392, 393, "0", false, false);
    	
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 394, 394, " ", false, false);
    	//NÚMERO SEQUENCIAL
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 395, 400, "0", false, false);
    	linha += "\r";
    	cmd.adicionarLinhaComando(linha, 0);
    }

    public void gerarDadosTransacaoPartilhado(EditorOC editorOC, Comando cmd, ControleRemessaContaReceberVO dadosRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial, ControleRemessaVO controleRemessa) throws Exception {
    	String linha = "";
    	//TIPO DE REGISTRO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 1, 1, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(dadosRemessaVO.getNossoNumero()), 2, 11, " ", false, false);    	

    	int qtdCodigoReceptor = 1;
    	boolean codigoReceptor1Uti = true;
    	boolean codigoReceptor2Uti = false;
    	boolean codigoReceptor3Uti = false;
    	boolean codigoReceptor4Uti = false;
    	Double valorPartilhado1 = 0.0;
    	Double valorPartilhado2 = 0.0;
    	Double valorPartilhado3 = 0.0;
    	Double valorPartilhado4 = 0.0;
    	if (controleRemessa.getContaCorrenteVO().getPercReceptor1() > 0) {
    		double perc = controleRemessa.getContaCorrenteVO().getPercReceptor1() / 100;
    		valorPartilhado1 = dadosRemessaVO.getValorComAcrescimo() * perc;
			valorPartilhado1 = Uteis.arrendondarForcando2CadasDecimais(valorPartilhado1);
		}    
    	if (!controleRemessa.getContaCorrenteVO().getCodigoReceptor2().trim().equals("")) {
    		qtdCodigoReceptor++;
    		if (controleRemessa.getContaCorrenteVO().getPercReceptor2() > 0) {
	    		double perc = controleRemessa.getContaCorrenteVO().getPercReceptor2() / 100;
	    		valorPartilhado2 = dadosRemessaVO.getValorComAcrescimo() * perc;
				valorPartilhado2 = Uteis.arrendondarForcando2CadasDecimais(valorPartilhado2);
    		}
    		codigoReceptor2Uti= true;
    	}
    	if (!controleRemessa.getContaCorrenteVO().getCodigoReceptor3().trim().equals("")) {
    		qtdCodigoReceptor++;
    		if (controleRemessa.getContaCorrenteVO().getPercReceptor3() > 0) {
	    		double perc = controleRemessa.getContaCorrenteVO().getPercReceptor3() / 100;
	    		valorPartilhado3 = dadosRemessaVO.getValorComAcrescimo() * perc;
				valorPartilhado3 = Uteis.arrendondarForcando2CadasDecimais(valorPartilhado3);
    		}
    		codigoReceptor3Uti = true;
    	}
    	if (!controleRemessa.getContaCorrenteVO().getCodigoReceptor4().trim().equals("")) {
    		qtdCodigoReceptor++;
    		if (controleRemessa.getContaCorrenteVO().getPercReceptor4() > 0) {
	    		double perc = controleRemessa.getContaCorrenteVO().getPercReceptor4() / 100;
	    		valorPartilhado4 = dadosRemessaVO.getValorComAcrescimo() * perc;
				valorPartilhado4 = Uteis.arrendondarForcando2CadasDecimais(valorPartilhado4);
    		}    		
    		codigoReceptor4Uti = true;
    	}
    	
    	//CÓDIGO RECEPTOR 01
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessa.getContaCorrenteVO().getCodigoReceptor1(), 12, 13, " ", false, false);
    	// Valor Partilhado
    	if (codigoReceptor1Uti) {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(valorPartilhado1.toString())), 13), 14, 26, " ", false, false);
    	} else {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 14, 26, " ", false, false);
    	}
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000000", 27, 52, "0", false, false);
    	
    	//CÓDIGO RECEPTOR 02
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessa.getContaCorrenteVO().getCodigoReceptor2(), 53, 54, " ", false, false);
    	// Valor Partilhado
    	if (codigoReceptor2Uti) {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(valorPartilhado2.toString())), 13), 55, 67, " ", false, false);
    	} else {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 55, 67, " ", false, false);
    	}
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000000", 68, 93, "0", false, false);

    	//CÓDIGO RECEPTOR 03
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessa.getContaCorrenteVO().getCodigoReceptor3(), 94, 95, " ", false, false);
    	// Valor Partilhado
    	if (codigoReceptor3Uti) {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(valorPartilhado3.toString())), 13), 96, 108, " ", false, false);
    	} else {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 96, 108, "0", false, false);
    	}
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000000", 109, 134, "0", false, false);

    	//CÓDIGO RECEPTOR 04
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessa.getContaCorrenteVO().getCodigoReceptor4(), 135, 136, " ", false, false);
    	// Valor Partilhado
    	if (codigoReceptor4Uti) {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(valorPartilhado4.toString())), 13), 137, 149, " ", false, false);
    	} else {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 137, 149, "0", false, false);
    	}
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000000", 150, 175, "0", false, false);

    	
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 176, 177, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000000", 178, 216, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 217, 218, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000000", 219, 257, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 258, 394, " ", false, false);
    	
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 395, 400, "0", false, false);
    	linha += "\r";
    	cmd.adicionarLinhaComando(linha, 0);
    }
    
    
   

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gerarDadosTraillerBANESTES(EditorOC editorOC, Comando cmd, int numeroSequencial, int quantidadeDocu) throws Exception {
    	String linha = "";
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 1, 1, " ", false, false);    
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 2, 394, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 395, 400, "0", false, false);
    	linha += "\r";
    	cmd.adicionarLinhaComando(linha, 0);
    	
    }


    private Integer consultarCodigoRemessa() {
        String sqlStr = "SELECT MAX(codigo) FROM controleRemessa";
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
            if (!tabelaResultado.next()) {
                return 0;
            }
            return tabelaResultado.getInt("max");
        } finally {
            sqlStr = null;
        }
    }

	@Override
	public void processarArquivoRetorno(ControleRemessaVO controleRemessaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		try {
			File arquivo = controleRemessaVO.getArquivo(caminho);
            BufferedReader reader = new BufferedReader(new FileReader(arquivo));
            String linha;
            int registro = 0;
            Integer contador = 0;
            while ((linha = reader.readLine()) != null) {
                if (linha.equals("")) {
                    continue;
                }
                registro = Uteis.getValorInteiro(linha.substring(0, 1));

                switch (registro) {
                    case 0:
                        processarHeader(linha, controleRemessaVO.getRegistroHeaderRetornoVO());
                        break;
                    case 1:
                        controleRemessaVO.getRegistroDetalheRetornoVOs().add(processarRegistroTransacao(linha, new RegistroDetalheVO(), configuracaoFinanceiroVO, usuarioVO, controleRemessaVO.getRegistroHeaderRetornoVO()));
                        
                        break;
                    case 9:
                        processarRegistroTrailler(linha, controleRemessaVO.getRegistroTrailerRetornoVO());
                        break;
                }
            }
            if (contador == 2) {
                throw new Exception("Não existem registros de contas nesse arquivo de retorno. Entre em contato com o seu banco!");
            }
            reader = null;
        } catch (StringIndexOutOfBoundsException e) {
            throw new ConsistirException("O arquivo selecionado é inválido pois não possui a quantidade adequada de caracteres. Detalhe: " + e.getMessage());
        }		
		
	}
	
	
	 private void processarHeader(String linha, RegistroHeaderVO header) throws Exception {
    	 header.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
         header.setCodigoRemessaRetorno(Uteis.getValorInteiro(linha.substring(1, 2)));
         header.setLiteralRetorno(linha.substring(2, 9));
         header.setCodigoServico(Uteis.getValorInteiro(linha.substring(9, 11)));
         header.setLiteralCobranca(linha.substring(11,26));        
         header.setNumeroConta(Uteis.getValorInteiro(linha.substring(26,37)));
         header.setNomeEmpresa(linha.substring(46, 76));
         header.setCodigoBanco(linha.substring(76, 79));
         header.setNomeBanco(linha.substring(79,87));
         header.setDataGeracaoArquivo(Uteis.getData(linha.substring(94, 100), PATTERN));
         header.setNumeroSequencialArquivo(Uteis.getValorInteiro(linha.substring(394, 400)));
         header.setNumeroSequencialRegistro(Uteis.getValorInteiro(linha.substring(394, 400)));
 		 header.setLinhaHeader(linha);
    }
	 
	 
	 
	 private RegistroDetalheVO processarRegistroTransacao(String linha, RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, RegistroHeaderVO header) throws Exception {
   	  detalhe.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
   	  detalhe.setCedenteTipoInscricaoEmpresa(Uteis.getValorInteiro(linha.substring(1,3)));
         detalhe.setCedenteNumeroInscricaoEmpresa(linha.substring(3, 17));       
         detalhe.setCedenteNumeroConta(linha.substring(17, 28));      
         detalhe.setIdentificacaoTituloEmpresa(linha.substring(62, 72));  
       
         //Obtendo nossonumero gerado banco
         detalhe.setNossoNumero(linha.substring(62, 72)); 
         detalhe.setCodigoMovimentoRemessaRetorno(Uteis.getValorInteiro(linha.substring(82,84)));
         detalhe.setCarteira(Uteis.getValorInteiro(linha.substring(107, 108)));
         detalhe.setIdentificacaoOcorrencia(Uteis.getValorInteiro(linha.substring(108, 110)));
         detalhe.setDataOcorrencia(Uteis.getData(linha.substring(110, 116), PATTERN));        
         detalhe.setDataCredito(Uteis.getData(linha.substring(110, 116), PATTERN));        
         detalhe.setNumeroDocumentoCobranca(linha.substring(116,126));       
         detalhe.setDataVencimentoTitulo(Uteis.getData(linha.substring(146, 152), PATTERN));       
         detalhe.setValorNominalTitulo(Uteis.getValorDoubleComCasasDecimais(linha.substring(155, 165)));
//         detalhe.setSacadoBancoCodigo(linha.substring(165, 168));
         detalhe.setSacadoBancoCodigo(header.getCodigoBanco());
         detalhe.setSacadoAgenciaCodigo(linha.substring(168, 173));
         detalhe.setEspecieTitulo(Uteis.getValorInteiro(linha.substring(173,175)));
         detalhe.setValorTarifa(Uteis.getValorDoubleComCasasDecimais(linha.substring(175, 188)));
         detalhe.setValorOutrasDespesas(Uteis.getValorDoubleComCasasDecimais(linha.substring(188, 201)));        
         detalhe.setValorIOF(Uteis.getValorDoubleComCasasDecimais(linha.substring(214, 227)));
         detalhe.setValorAbatimento(Uteis.getValorDoubleComCasasDecimais(linha.substring(227, 240)));
         detalhe.setDesconto(Uteis.getValorDoubleComCasasDecimais(linha.substring(240, 253)));

         if(linha.substring(108, 110).equals("17") || linha.substring(108, 110).equals("06") ) {
         	detalhe.setValorPago(Uteis.getValorDoubleComCasasDecimais(linha.substring(253, 266)));
         }else {
         	detalhe.setValorPago(0.0);
         }

         detalhe.setJurosMora(Uteis.getValorDoubleComCasasDecimais(linha.substring(266, 279)));
         detalhe.setValorOutrosCreditos(Uteis.getValorDoubleComCasasDecimais(linha.substring(279,292)));
         detalhe.setMotivoOcorrencia(Uteis.getValorInteiro(linha.substring(318, 320)));
         detalhe.setCodigoMoeda(Uteis.getValorInteiro(linha.substring(393,394)));        
         detalhe.setNumeroSequencialRegistroLote(Uteis.getValorInteiro(linha.substring(394, 400)));
         detalhe.setNumeroSequencialRegistro(Uteis.getValorInteiro(linha.substring(394, 400)));


         //Método responsável por realizar a Criação da conta receber para o Cliente verificado atualmente.
//         getRegistroArquivoVO().getContaReceberRegistroArquivoVOs().add(executarCriacaoContaReceberRegistroArquivoVOs(detalhe, configuracaoFinanceiroVO, usuarioVO));
         return detalhe;
   }
	 
	 
	 private void processarRegistroTrailler(String linha, RegistroTrailerVO trailler) {
		 trailler.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
         trailler.setIdentificacaoRetorno(Uteis.getValorInteiro(linha.substring(1,2)));
         trailler.setIdentificacaoTipoRegistro(Uteis.getValorInteiro(linha.substring(2,4)));
         trailler.setCodigoBanco(linha.substring(4, 7));
         trailler.setQuantidadeTitulosEmCobranca(Uteis.getValorInteiro(linha.substring(17,25)));
         trailler.setValorTitulosEmCobranca(Uteis.getValorDoubleComCasasDecimais(linha.substring(25,39)));
         trailler.setNumeroAvisoBancarioEmCobranca(linha.substring(39,47));       
         trailler.setNumeroSequencialRegistro(Uteis.getValorInteiro(linha.substring(394, 400)));
    }
}
