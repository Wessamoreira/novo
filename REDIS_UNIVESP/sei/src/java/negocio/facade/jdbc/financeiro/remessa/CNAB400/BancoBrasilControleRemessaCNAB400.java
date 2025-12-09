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
public class BancoBrasilControleRemessaCNAB400 extends ControleAcesso implements ControleRemessaCNAB400LayoutInterfaceFacade {

    private static final long serialVersionUID = 1L;
    private static final String PATTERN = "ddMMyy";

    public BancoBrasilControleRemessaCNAB400() {
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public EditorOC executarGeracaoDadosArquivoRemessa(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        EditorOC editorOC = new EditorOC();
        Comando cmd = new Comando();
        controleRemessaVO.setNumeroIncremental(1);
        controleRemessaVO.setIncrementalMX(getFacadeFactory().getControleRemessaMXFacade().consultarIncrementalPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), usuario));
        //Gera o Header
        gerarDadosHeader(editorOC, cmd, controleRemessaVO, controleRemessaVO.getUnidadeEnsinoVO(), configuracaoFinanceiroVO, usuario);
        //Gera o Detalhe Segmento P
        executarGeracaoDetalhe(listaDadosRemessaVOs, editorOC, cmd, controleRemessaVO.getDataInicio(), controleRemessaVO.getDataFim(), controleRemessaVO, configuracaoFinanceiroVO, usuario);
        //Gera o Trailler
        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
        gerarDadosTrailler(editorOC, cmd, controleRemessaVO, controleRemessaVO.getNumeroIncremental());

        editorOC.adicionarComando(cmd);
        getFacadeFactory().getControleRemessaMXFacade().alterarIncrementalPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), controleRemessaVO.getIncrementalMX() + 1, null, usuario);
        return editorOC;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarGeracaoDetalhe(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, EditorOC editorOC, Comando cmd, Date dataInicio, Date dataFim, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        try {
            if (!listaDadosRemessaVOs.isEmpty()) {
                Iterator i = listaDadosRemessaVOs.iterator();
                while (i.hasNext()) {

                    ControleRemessaContaReceberVO dadosRemessaVO = (ControleRemessaContaReceberVO) i.next();
                    if (dadosRemessaVO.getApresentarArquivoRemessa()) {
                    	if (dadosRemessaVO.getBaixarConta()) {
	                    	controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
	                        gerarDadosDetalheSegmentoP(editorOC, cmd, dadosRemessaVO, controleRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), "02");
                    	} else {
	                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
	                        gerarDadosDetalheSegmentoP(editorOC, cmd, dadosRemessaVO, controleRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), "01");
	                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
	                        gerarDadosDetalheSegmentoMulta(editorOC, cmd, dadosRemessaVO, controleRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), "01");
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
    public void gerarDadosHeader(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 1, 1, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 2, 2, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "REMESSA", 3, 9, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 10, 11, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "COBRANCA", 12, 26, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia() + controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 27, 31, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getNumero() + controleRemessaVO.getContaCorrenteVO().getDigito(), 32, 40, "0", false, false);
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getConvenio(), 32, 40, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 41, 46, "0", false, false);
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(unidadeEnsinoVO.getRazaoSocial()), 47, 76, " ", true, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "001", 77, 79, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "BANCODOBRASIL", 80, 94, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyy"), 95, 100, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000001", 101, 107, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 108, 129, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getConvenio(), 130, 136, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 137, 394, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000001", 395, 400, " ", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }

    public void gerarDadosDetalheSegmentoP(EditorOC editorOC, Comando cmd, ControleRemessaContaReceberVO dadosRemessaVO, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial, String codMovRemessa) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "7", 1, 1, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 2, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 4, 17, " ", false, false);        

        //AGÊNCIA MANTENEDORA DA CONTA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getAgencia(), 18, 21, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getDigitoAgencia(), 22, 22, "0", false, false);
        //CONTA MOVIMENTO CEDENTE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getContaCorrente().toString(), 23, 30, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getDigitoContaCorrente(), 31, 31, "0", false, false);

        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getConvenio(), 32, 38, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNrDocumento(), 39, 63, "0", false, false);

        //NOSSO NÚMERO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getConvenio(), 64, 70, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero(), 71, 80, "0", false, false);        
                
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 81, 82, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 83, 84, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 85, 87, " ", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 88, 88, " ", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 89, 91, " ", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "019", 92, 94, "", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 95, 95, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 96, 101, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 102, 106, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCarteira(), 107, 108, " ", false, false);
        /// CODIGO PARA INFORMAR REGISTRO DO TITULO NO BANCO, em CASO DE ALTERAÇÃO ESSE CAMPO DEVE MUDAR.
        linha = editorOC.adicionarCampoLinhaVersao2(linha, codMovRemessa, 109, 110, " ", false, false);
        
        //NÚMERO DO DOCUMENTO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero().toString(), 111, 120, "0", false, false);
        //DATA VENCIMENTO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), PATTERN), 121, 126, "0", false, false);
        //VALOR DO TITULO
        if (controleRemessaVO.getContaCorrenteVO().getCarteiraRegistrada() && controleRemessaVO.getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
	        //VALOR DO TITULO
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorBaseComAcrescimo())), 13), 127, 139, "0", false, false);
        } else {
	        //VALOR DO TITULO
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorComAcrescimo())), 13), 127, 139, "0", false, false);
        }
        //CÓDIGO DO BANCO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Bancos.BANCO_DO_BRASIL.getNumeroBanco(), 140, 142, "0", false, false);
        //AGÊNCIA COBRADORA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 143, 146, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 147, 147, " ", false, false);

        //ESPÉCIE DO TÍTULO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "12", 148, 149, " ", false, false);
        //ACEITE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 150, 150, " ", false, false);
        //DATA DE EMISSÃO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(new Date(), PATTERN), 151, 156, "0", false, false);
        // INSTRUÇÃO 1
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
        Double juroFinal = (valorTitulo * juro) / 30;
        String juroStr = "";
        if (juroFinal.toString().length() > 4) {
        	juroStr = (juroFinal.toString()).substring(0, 4);
        } else {
        	juroStr = juroFinal.toString();
        }
        Double juroDouble = new Double(juroStr);
        if (juroDouble > 0) {
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(juroStr)), 13), 161, 173, "0", false, false);
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 161, 173, "0", false, false);
        }  
        

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
        //if (dadosRemessaVO.getValorDescontoDataLimite() > 0 && dadosRemessaVO.getDataLimiteConcessaoDesconto() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >= 0) {
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto(), PATTERN), 174, 179, " ", false, false);
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite(), 2)))), 13), 180, 192, "0", false, false);                    
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "000000", 174, 179, "0", false, false);
            linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000000", 180, 192, "0", false, false);
        }
        //VALOR DO I.O.F.
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 193, 205, "0", false, false);
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

        
        if (dadosRemessaVO.getCodigoInscricao() == 0 || dadosRemessaVO.getCodigoInscricao() == 1) {
            //CÓDIGO DE INSCRIÇÃO  01=CPF  -  02=CNPJ
            linha = editorOC.adicionarCampoLinhaVersao2(linha, "01" , 219, 220, " ", false, false);
            //NÚMERO DE INSCRIÇÃO  CPF OU CNPJ
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).replaceAll(" ", ""), 14), 221, 234, "0", false, false);
        } else {
        	String nr = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()).replaceAll(" ", "");
        	if (nr.length() == 14) {
        		linha = editorOC.adicionarCampoLinhaVersao2(linha, "02", 219, 220, " ", false, false);
                linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()), 14).replaceAll(" ", ""), 221, 234, "0", false, false);
        	} else {
        		linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 219, 220, " ", false, false);
                linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).replaceAll(" ", ""), 14), 221, 234, "0", false, false);
        	}
        }
        
        //NOME DO SACADO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(dadosRemessaVO.getNomeSacado().toString()), 235, 271, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 272, 274, " ", true, false);
        //LOGRADOURO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(dadosRemessaVO.getLogradouro()), 275, 314, " ", true, false);
        //BAIRRO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(dadosRemessaVO.getBairro()), 315, 326, " ", true, false);
        //CEP
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getCep()).replaceAll(" ", "")), 327, 334, " ", true, false);
        //CIDADE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removeCaractersEspeciais2(dadosRemessaVO.getCidade()), 335, 349, " ", true, false);
        //SIGLA DO ESTADO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(dadosRemessaVO.getEstado()), 350, 351, " ", true, false);
        //SACADOR OU AVALISTA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 352, 391, " ", true, false);
    	//BRANCOS
    	if (controleRemessaVO.getContaCorrenteVO().getHabilitarProtestoBoleto()) {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getQtdDiasProtestoBoleto_Str(), 392, 393, " ", false, false);
    	} else {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "  ", 392, 393, " ", false, false);
    	}
        
        //DATA DE MORA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 394, 394, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 395, 400, "0", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }

    public void gerarDadosDetalheSegmentoMulta(EditorOC editorOC, Comando cmd, ControleRemessaContaReceberVO dadosRemessaVO, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial, String codMovRemessa) throws Exception {
    	String linha = "";
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "5", 1, 1, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "99", 2, 3, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 4, 4, " ", false, false);        
    	
    	//DATA VENCIMENTO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), PATTERN), 5, 10, "0", false, false);

    	
    	//MULTA DE 1 DIA DE ATRASO
    	Double juro = 0.02;
    	Double valorTitulo = Uteis.arrendondarForcando2CadasDecimais(configuracaoFinanceiroVO.getCobrarJuroMultaSobreValorCheioConta() ? dadosRemessaVO.getValorComAcrescimo() : dadosRemessaVO.getValor()-dadosRemessaVO.getValorAbatimento());
    	Double juroFinal = (valorTitulo * juro);
    	String juroStr = "";
    	if (juroFinal.toString().length() > 4) {
    		juroStr = (juroFinal.toString()).substring(0, 4);
    	} else {
    		juroStr = juroFinal.toString();
    	}
    	Double juroDouble = new Double(juroStr);
    	if (juroDouble > 0) {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(juroStr)), 12), 11, 22, "0", false, false);
    	} else {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 11, 22, "0", false, false);
    	}      	
    	//BRANCOS
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 23, 394, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 395, 400, "0", false, false);
    	linha += "\r";
    	cmd.adicionarLinhaComando(linha, 0);
    }

    public String modulo11(String campo) throws Exception {
//        int type = 7;
//        int multiplicador = 2;
//        int multiplicacao = 0;
//        int soma_campo = 0;
//
//        for (int i = campo.length(); i > 0; i--) {
//            multiplicacao = Integer.parseInt(campo.substring(i - 1, i)) * multiplicador;
//
//            soma_campo = soma_campo + multiplicacao;
//
//            if (type == 7) {
//                multiplicador = 7;
//                type = 6;
//            } else if (type < 7 && type > 2) {
//                multiplicador--;
//                type = multiplicador;
//            } else if (type == 2) {
//                multiplicador = 2;
//                type = 7;
//            }
//        }
//
//        int dac = 11 - (soma_campo % 11);
//
//        if (dac == 1) {
//            return "P";
//        } else if (dac == 0) {
//            return "0";
//        }
//        return ((Integer) dac).toString();
        
        
        
//      //Modulo 11 - 3791
//      		campo = obj.getContaCorrenteVO().getAgencia().getNumeroAgencia() + "000" + obj.getContaCorrenteVO().getConvenio() + campo;
          	int multiplicador = 2;
          	int multiplicacao = 0;
          	int soma_campo = 0;
          	
          	
          	for (int i = campo.length(); i > 0; i--) {
          		multiplicacao = Integer.parseInt(campo.substring(i - 1, i)) * multiplicador;
          		
          		soma_campo = soma_campo + multiplicacao;
          		
          		
          		multiplicador++;
          		if (multiplicador == 8) {
          			multiplicador = 2;
          		}
          	}
          	
          	int dac = (soma_campo % 11);
			if (dac == 1) {
				return "P";
			} else if (dac == 0) {
				return "0";
			} else {
          		dac = 11 - (soma_campo % 11);
          	}
          	return ((Integer) dac).toString();        
    }


    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gerarDadosTrailler(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, int numeroSequencial) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 1, 1, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 2, 394, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(numeroSequencial), 6), 395, 400, " ", false, false);
        //linha += "\r\n";
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

    public String verificarDecimal(String valor) {
        String decimal = valor.substring(valor.lastIndexOf(".") + 1);
        if (decimal.length() == 1) {
            return valor + "0";
        }
        return valor;
    }

	@Override
	public void processarArquivoRetorno(ControleRemessaVO controleRemessaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
