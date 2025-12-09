/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.financeiro.remessa.CNAB400;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ControleRemessaVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.utilitarias.Comando;
import negocio.comuns.utilitarias.EditorOC;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.remessa.ControleRemessaCNAB400LayoutInterfaceFacade;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 *
 * @author Otimize-04
 */
public class SantanderControleRemessaCNAB400 extends ControleAcesso implements ControleRemessaCNAB400LayoutInterfaceFacade {

    private static final String PATTERN = "ddMMyy";
    public Double valorTotal = 0.0;    

    public SantanderControleRemessaCNAB400() {
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public EditorOC executarGeracaoDadosArquivoRemessa(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        EditorOC editorOC = new EditorOC();
        Comando cmd = new Comando();
        
        controleRemessaVO.setNumeroIncremental(1);
        //Gera o Header
        gerarDadosHeaderSANTANDER(editorOC, cmd, controleRemessaVO, controleRemessaVO.getUnidadeEnsinoVO(), configuracaoFinanceiroVO, usuario);
        //Gera o Detalhe
        executarGeracaoDetalheSANTANDER(listaDadosRemessaVOs, editorOC, cmd, controleRemessaVO.getDataInicio(), controleRemessaVO.getDataFim(), controleRemessaVO, configuracaoFinanceiroVO, usuario);
        //Gera o Trailler
        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);

        if (controleRemessaVO.getContaCorrenteVO().getUtilizaCobrancaPartilhada()) {
        	gerarDadosTraillerSANTANDER(editorOC, cmd, controleRemessaVO.getNumeroIncremental(), (controleRemessaVO.getNumeroIncremental() - 2)/2);            
            String linha = "";
//            linha += "\r";
            cmd.adicionarLinhaComando(linha, 0);        	
        } else {
        	gerarDadosTraillerSANTANDER(editorOC, cmd, controleRemessaVO.getNumeroIncremental(), controleRemessaVO.getNumeroIncremental());
        }

        editorOC.adicionarComando(cmd);
        return editorOC;
    }
        
        
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarGeracaoDetalheSANTANDER(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, EditorOC editorOC, Comando cmd, Date dataInicio, Date dataFim, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        try {
            if (!listaDadosRemessaVOs.isEmpty()) {
                Iterator i = listaDadosRemessaVOs.iterator();
                while (i.hasNext()) {
                	
                    ControleRemessaContaReceberVO dadosRemessaVO = (ControleRemessaContaReceberVO) i.next();
                    if (dadosRemessaVO.getApresentarArquivoRemessa()) {
                    	if (!dadosRemessaVO.getBaixarConta()) {
	                    	valorTotal += dadosRemessaVO.getValorComAcrescimo();
	                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
	                        gerarDadosTransacaoSANTANDER(editorOC, cmd, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), controleRemessaVO, "01");
	    					if (controleRemessaVO.getContaCorrenteVO().getUtilizaCobrancaPartilhada()) {
	    						controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
	    						gerarDadosTransacaoPartilhado(editorOC, cmd, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), controleRemessaVO);
	    					}
                    	} else {
	                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
	                        gerarDadosTransacaoSANTANDER(editorOC, cmd, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), controleRemessaVO, "02");
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
    public void executarGeracaoDetalheREAL(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, EditorOC editorOC, Comando cmd, Date dataInicio, Date dataFim, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
    	try {
    		if (!listaDadosRemessaVOs.isEmpty()) {
    			Iterator i = listaDadosRemessaVOs.iterator();
    			while (i.hasNext()) {
    				
    				ControleRemessaContaReceberVO dadosRemessaVO = (ControleRemessaContaReceberVO) i.next();
    				if (dadosRemessaVO.getApresentarArquivoRemessa()) {
    					valorTotal += dadosRemessaVO.getValorComAcrescimo();
    					controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
    					gerarDadosTransacaoREAL(editorOC, cmd, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), controleRemessaVO);
    					if (controleRemessaVO.getContaCorrenteVO().getUtilizaCobrancaPartilhada()) {
    						controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
    						gerarDadosTransacaoPartilhado(editorOC, cmd, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), controleRemessaVO);
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
    public void executarGeracaoDetalhe2(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, EditorOC editorOC, Comando cmd, Date dataInicio, Date dataFim, ControleRemessaVO controleRemessaVO, double valorTotalTitulo, int quantidadeDoc, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        try {
            if (!listaDadosRemessaVOs.isEmpty()) {
                Iterator i = listaDadosRemessaVOs.iterator();
                while (i.hasNext()) {
                    ControleRemessaContaReceberVO dadosRemessaVO = (ControleRemessaContaReceberVO) i.next();
                    valorTotalTitulo += dadosRemessaVO.getValorComAcrescimo();
                    quantidadeDoc += 1;
                    if (dadosRemessaVO.getApresentarArquivoRemessa()) {
                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
                        gerarDadosTransacao2(editorOC, cmd, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), controleRemessaVO);
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
    public void gerarDadosHeaderSANTANDER(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 1, 1, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 2, 2, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "REMESSA", 3, 9, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 10, 11, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "COBRANCA       ", 12, 26, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCodigoTransmissaoRemessa(), 27, 46, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(unidadeEnsinoVO.getRazaoSocial()), 30), 47, 76, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "033", 77, 79, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "SANTANDER", 80, 94, " ", true, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyy"), 95, 100, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 101, 116, "0", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 117, 391, " ", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000", 392, 394, " ", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000001", 395, 400, " ", false, false);
        linha += "\r";
        //controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
        cmd.adicionarLinhaComando(linha, 0);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gerarDadosHeaderREAL(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
    	String linha = "";
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 1, 1, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 2, 2, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "REMESSA", 3, 9, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 10, 11, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "COBRANCA       ", 12, 26, " ", true, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 27, 27, " ", true, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCodigoTransmissaoRemessa(), 28, 39, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 40, 46, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(unidadeEnsinoVO.getRazaoSocial()), 30), 47, 76, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "033", 77, 79, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "BANCO REAL", 80, 94, " ", true, false);        
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyy"), 95, 100, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "01600BPI", 101, 108, " ", false, false);        
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 109, 394, " ", false, false);        
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "000001", 395, 400, " ", false, false);
    	linha += "\r";
    	//controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
    	cmd.adicionarLinhaComando(linha, 0);
    }
  
    public void gerarDadosTransacaoREAL(EditorOC editorOC, Comando cmd, ControleRemessaContaReceberVO dadosRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial, ControleRemessaVO controleRemessaVO) throws Exception {
        String linha = "";
        //TIPO DE REGISTRO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 1, 1, " ", false, false);
        //CÓDIGO DE INSCRIÇÃO EMPRESA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "02", 2, 3, " ", false, false);
        //Nº DE INSCRIÇÃO DA EMPRESA(CPF/CNPJ)
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 4, 17, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 18, 18, " ", false, false);
        //AGÊNCIA MANTENEDORA DA CONTA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(dadosRemessaVO.getAgencia()), 19, 22, " ", false, false);
        //ZEROS
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 23, 23, " ", false, false);
        //NÚMERO DA CONTA CORRENTE DA EMPRESA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getConvenio(), 24, 30, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 31, 38, " ", false, false);
        //linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 39, 62, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(dadosRemessaVO.getNossoNumero()), 39, 62, " ", true, false);		
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 63, 64, " ", false, false);
        //NOSSO NÚMERO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(dadosRemessaVO.getNossoNumero()), 65, 71, " ", false, false);
        //QTDE DE MOEDA VARIÁVEL
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 72, 72, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 73, 74, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 75, 75, " ", false, false);

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
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(multaStr)), 13), 76, 88, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 89, 95, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "000000000", 96, 104, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 105, 105, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 106, 107, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 108, 108, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 109, 110, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(dadosRemessaVO.getNossoNumero()), 111, 120, " ", false, false);
    	

        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), PATTERN), 121, 126, " ", false, false);
        //VALOR DO TITULO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(dadosRemessaVO.getValorComAcrescimo().toString())), 13), 127, 139, " ", false, false);
        //CÓDIGO DO BANCO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "033", 140, 142, " ", false, false);
        //AGÊNCIA COBRADORA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00000", 143, 147, " ", false, false);
        //ESPÉCIE DO TÍTULO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "07", 148, 149, " ", false, false);
        //ACEITE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 150, 150, " ", false, false);
        //DATA DE EMISSÃO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(new Date(), PATTERN), 151, 156, " ", false, false);
        //INSTRUÇÃO 1
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 157, 158, " ", false, false);
        //INSTRUÇÃO 2
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 159, 160, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 161, 161, " ", false, false);
        //JUROS DE 1 DIA DE ATRASO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(dadosRemessaVO.getJuro().toString())), 12), 162, 173, " ", false, false);

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
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 219, 220, " ", false, false);
        //NÚMERO DE INSCRIÇÃO  CPF OU CNPJ
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()), 11).replaceAll(" ", ""), 221, 229, "", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000", 230, 232, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString())), 11).replaceAll(" ", "").substring(9), 233, 234, "", false, false);
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
        //SACADOR OU AVALISTA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 352, 391, " ", true, false);
        //BRANCOS
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 392, 392, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "07", 393, 394, " ", false, false);
        //NÚMERO SEQUENCIAL
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 395, 400, "0", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }

    public void gerarDadosTransacaoSANTANDER(EditorOC editorOC, Comando cmd, ControleRemessaContaReceberVO dadosRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial, ControleRemessaVO controleRemessaVO, String codMovRemessa) throws Exception {
    	String linha = "";
    	//TIPO DE REGISTRO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 1, 1, " ", false, false);
    	//CÓDIGO DE INSCRIÇÃO EMPRESA
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "02", 2, 3, " ", false, false);
    	//Nº DE INSCRIÇÃO DA EMPRESA(CPF/CNPJ)
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 4, 17, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCodigoTransmissaoRemessa(), 18, 37, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(dadosRemessaVO.getNossoNumero()), 38, 62, " ", true, false);		
    	//NOSSO NÚMERO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(dadosRemessaVO.getNossoNumero()), 63, 70, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 71, 76, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 77, 77, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "4", 78, 78, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0200", 79, 82, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 83, 84, " ", false, false);
//    	Double juro = 0.01;
//    	Double valorTitulo = dadosRemessaVO.getValorComAcrescimo();
//    	Double juroFinal = (valorTitulo * juro) / 30;
//    	String juroStr = "";
//    	String multaStr = "000000000000200";
//    	if (juroFinal.toString().length() > 4) {
//    		juroStr = (juroFinal.toString()).substring(0, 4);
//    	} else {
//    		juroStr = juroFinal.toString();
//    	}    	
//    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(multaStr)), 13), 76, 88, " ", false, false);

    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 85, 97, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 98, 101, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 102, 107, "0", false, false);
    	
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "5", 108, 108, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, codMovRemessa, 109, 110, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(dadosRemessaVO.getNossoNumero()), 111, 120, " ", true, false);
    	
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), PATTERN), 121, 126, " ", false, false);
    	//VALOR DO TITULO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(dadosRemessaVO.getValorComAcrescimo().toString())), 13), 127, 139, " ", false, false);
    	//CÓDIGO DO BANCO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "033", 140, 142, " ", false, false);
    	//AGÊNCIA COBRADORA
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getAgencia()+dadosRemessaVO.getDigitoAgencia(), 143, 147, " ", false, false);
    	//ESPÉCIE DO TÍTULO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 148, 149, " ", false, false);
    	//ACEITE
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 150, 150, " ", false, false);
    	//DATA DE EMISSÃO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(new Date(), PATTERN), 151, 156, " ", false, false);

    	//INSTRUÇÃO 1 - PROTESTAR ??
    	if (controleRemessaVO.getContaCorrenteVO().getHabilitarProtestoBoleto()) {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "06", 157, 158, " ", false, false);
    	} else {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 157, 158, " ", false, false);
    	}
    	//INSTRUÇÃO 2
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 159, 160, " ", false, false);
    	
        //JUROS DE 1 DIA DE ATRASO
        Double juro = 0.01;
        Double valorTitulo = Uteis.arrendondarForcando2CadasDecimais(configuracaoFinanceiroVO.getCobrarJuroMultaSobreValorCheioConta() ? dadosRemessaVO.getValorComAcrescimo() : dadosRemessaVO.getValor()-dadosRemessaVO.getValorAbatimento());
        
        Double juroFinalTemp = (valorTitulo * juro);
        Double juroFinal = 0.0;
        if (juroFinalTemp < 1) {
        	juroFinal = juroFinalTemp;
        } else {
        	juroFinal = (valorTitulo * juro) / 30;        	
        }
        
        String juroStr = juroFinal.toString();
        if (juroStr.length() > 4) {
        	juroStr = (juroStr).substring(0, 4);        	
        }
        
    	//JUROS DE 1 DIA DE ATRASO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(juroStr)), 13), 161, 173, " ", false, false);
    	
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
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 219, 220, " ", false, false);
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
    	//SACADOR OU AVALISTA
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 352, 382, " ", true, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "I", 383, 383, " ", true, false);
    	
    	int tamanho = controleRemessaVO.getContaCorrenteVO().getNumero().length();
    	String complemento = controleRemessaVO.getContaCorrenteVO().getNumero().substring(tamanho-1) + controleRemessaVO.getContaCorrenteVO().getDigito();
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, complemento, 384, 385, " ", true, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 386, 391, " ", true, false);
    	//BRANCOS
    	if (controleRemessaVO.getContaCorrenteVO().getHabilitarProtestoBoleto()) {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getQtdDiasProtestoBoleto_Str(), 392, 393, " ", false, false);
    	} else {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 392, 393, " ", false, false);
    	}
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 394, 394, " ", false, false);
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
    
    public void gerarDadosTransacao2(EditorOC editorOC, Comando cmd, ControleRemessaContaReceberVO dadosRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial, ControleRemessaVO controleRemessaVO) throws Exception {
        try {
    	String linha = "";
        //TIPO DE REGISTRO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 1, 1, "0", false, false);
        //CÓDIGO DE INSCRIÇÃO EMPRESA => 01 = CPF / 02 = CNPJ
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "02", 2, 3, "0", false, false);
        //Nº DE INSCRIÇÃO DA EMPRESA(CPF/CNPJ)
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(dadosRemessaVO.getCnpj()), 4, 17, "0", false, false);
        //AGÊNCIA MANTENEDORA DA CONTA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getAgencia(), 18, 21, "0", false, false);
        //CONTA MOVIMENTO CEDENTE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getContaCorrente().toString(), 22, 29, "0", false, false);
        //NR CONTROLE PARTICIPANTE, CONTROLE CEDENTE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 30, 62, " ", false, false);

//        //NOSSO NÚMERO
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero(), 63, 70, " ", true, false);
        //NOSSO NÚMERO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00000000", 63, 70, "0", true, false);
        //DATA SEGUNDO DESCONTO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 71, 77, " ", false, false);
        //MOEDA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 78, 78, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 79, 84, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 85, 107, " ", false, false);
        
        //NÚMERO DA CARTEIRA NO BANCO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCarteira().toString(), 108, 108, "5", false, false);
        //CODIGO DA OCORRENCIA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 109, 110, " ", false, false);
        
        
        //NÚMERO DO DOCUMENTO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero().toString(), 111, 120, " ", true, false);
        //linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNrDocumento().toString(), 111, 120, " ", true, false);
        //DATA VENCIMENTO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), PATTERN), 121, 126, "0", false, false);
        //VALOR DO TITULO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(dadosRemessaVO.getValorComAcrescimo().toString())), 13), 127, 139, " ", false, false);
        //CÓDIGO DO BANCO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Bancos.SANTANDER.getNumeroBanco(), 140, 142, "0", false, false);
        //AGÊNCIA COBRADORA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getAgencia() + dadosRemessaVO.getDigitoAgencia(), 143, 147, "0", false, false);

        //ESPÉCIE DO TÍTULO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 148, 149, " ", false, false);
        //ACEITE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 150, 150, " ", false, false);
        //DATA DE EMISSÃO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(new Date(), PATTERN), 151, 156, "0", false, false);
        //INSTRUÇÃO 1
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 157, 158, " ", false, false);
        //INSTRUÇÃO 2
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 159, 160, " ", false, false);

        
        //JUROS DE 1 DIA DE ATRASO
        Double juro = 0.01;
        Double valorTitulo = dadosRemessaVO.getValorComAcrescimo();
        Double juroFinal = (valorTitulo * juro) / 30;
        String juroStr = "";
        if (juroFinal.toString().length() > 4) {
        	juroStr = (juroFinal.toString()).substring(0, 4);
        } else {
        	juroStr = juroFinal.toString();
        }
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(juroStr)), 13), 161, 173, " ", false, false);
//???????????????????
        //DATA LIMITE PARA CONCESSÃO DE DESCONTO
        if (dadosRemessaVO.getDataLimiteConcessaoDesconto() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(new Date()) >=0 ) {
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto(), PATTERN), 174, 179, " ", false, false);
        } else {
            linha = editorOC.adicionarCampoLinhaVersao2(linha, "000000", 174, 179, " ", false, false);
        }
        //VALOR DO DESCONTO
        if (dadosRemessaVO.getValorDescontoDataLimite() != 0) {
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(dadosRemessaVO.getValorDescontoDataLimite().toString())), 13), 180, 192, " ", false, false);
        } else {
            linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000000", 180, 192, " ", false, false);
        }
        //VALOR DO I.O.F.
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000000", 193, 205, " ", false, false);
        //ABATIMENTO
        if (controleRemessaVO.getContaCorrenteVO().getCarteiraRegistrada() && controleRemessaVO.getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
	        //VALOR DO TITULO
        	if (dadosRemessaVO.getValorBaseComAcrescimo() > 0 && dadosRemessaVO.getValorBaseComAcrescimo() > dadosRemessaVO.getValorComAcrescimo()) {
        		Double valorDescFinal = dadosRemessaVO.getValorBaseComAcrescimo() - dadosRemessaVO.getValorComAcrescimo();
        		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(valorDescFinal)), 13), 206, 218, " ", false, false);        		
        	} else {
            	if (dadosRemessaVO.getValorAbatimento() > 0) {
            		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorAbatimento())), 13), 206, 218, " ", false, false);
            	} else {
            		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000000", 206, 218, " ", false, false);
            	}        	
        	}
        } else {
        	if (dadosRemessaVO.getValorAbatimento() > 0) {
        		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorAbatimento())), 13), 206, 218, " ", false, false);
        	} else {
        		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000000", 206, 218, " ", false, false);
        	}        	
        }
        //CÓDIGO DE INSCRIÇÃO  01=CPF  -  02=CNPJ
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0" + dadosRemessaVO.getCodigoInscricao().toString(), 219, 220, " ", false, false);
        //NÚMERO DE INSCRIÇÃO  CPF OU CNPJ
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()), 14), 221, 234, " ", false, false);
        //NOME DO SACADO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNomeSacado().toString(), 235, 264, " ", true, false);
        //BRANCOS
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 265, 274, " ", false, false);
        //LOGRADOURO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getLogradouro(), 275, 314, " ", true, false);
        //BAIRRO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getBairro(), 315, 326, " ", true, false);
        //CEP
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removeCaractersEspeciais(dadosRemessaVO.getCep()), 327, 334, " ", true, false);
        //CIDADE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removeCaractersEspeciais(dadosRemessaVO.getCidade()), 335, 349, " ", true, false);
        //SIGLA DO ESTADO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getEstado(), 350, 351, " ", true, false);
        //SACADOR OU AVALISTA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 352, 381, " ", true, false);
        //BRANCOS
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 382, 385, " ", false, false);
        //DATA DE MORA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 386, 391, " ", false, false);
        //PRAZO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 392, 393, " ", false, false);
        //BRANCOS
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 394, 394, " ", false, false);
        //NÚMERO SEQUENCIAL
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 395, 400, "0", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
        } catch (Exception e) {
        	String err = e.getMessage();
        	err = "";
        	throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gerarDadosTraillerREAL(EditorOC editorOC, Comando cmd, int numeroSequencial, int quantidadeDocu) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 1, 1, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(quantidadeDocu), 2, 7, "0", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(valorTotal.toString())), 13), 8, 20, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 21, 394, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 395, 400, "0", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);

    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gerarDadosTraillerSANTANDER(EditorOC editorOC, Comando cmd, int numeroSequencial, int quantidadeDocu) throws Exception {
    	String linha = "";
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 1, 1, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(quantidadeDocu), 2, 7, "0", false, false);        
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(valorTotal.toString())), 13), 8, 20, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 21, 394, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 395, 400, "0", false, false);
//    	linha += "\r";
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
		// TODO Auto-generated method stub
		
	}
}
