/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.financeiro.remessa.CNAB400;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ControleCobrancaVO;
import negocio.comuns.financeiro.ControleRemessaVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.financeiro.RegistroDetalheVO;
import negocio.comuns.financeiro.RegistroHeaderVO;
import negocio.comuns.financeiro.RegistroTrailerVO;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaContaReceberEnum;
import negocio.comuns.utilitarias.Comando;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.EditorOC;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
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
public class SafraControleRemessaCNAB400 extends ControleAcesso implements ControleRemessaCNAB400LayoutInterfaceFacade {

    private static final long serialVersionUID = 1L;
    private static final String PATTERN = "ddMMyy";

    public SafraControleRemessaCNAB400() {
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public EditorOC executarGeracaoDadosArquivoRemessa(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        EditorOC editorOC = new EditorOC();
        Comando cmd = new Comando();
        Double valorTotalTitulo = 0.0;
        int quantidadeDoc = 0;        
        controleRemessaVO.setNumeroIncremental(1);
        controleRemessaVO.setIncrementalMX(getFacadeFactory().getControleRemessaMXFacade().consultarIncrementalPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), usuario));
        //Gera o Header
        gerarDadosHeader(editorOC, cmd, controleRemessaVO, controleRemessaVO.getUnidadeEnsinoVO(), configuracaoFinanceiroVO, usuario);
        //Gera o Detalhe Segmento P
        executarGeracaoDetalhe(listaDadosRemessaVOs, editorOC, cmd, controleRemessaVO.getDataInicio(), controleRemessaVO.getDataFim(), controleRemessaVO, valorTotalTitulo, quantidadeDoc, configuracaoFinanceiroVO, usuario);
        //Gera o Trailler
        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
        gerarDadosTrailler(editorOC, controleRemessaVO, cmd,  controleRemessaVO.getNumeroIncremental(), valorTotalTitulo, quantidadeDoc);

        editorOC.adicionarComando(cmd);
        getFacadeFactory().getControleRemessaMXFacade().alterarIncrementalPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), controleRemessaVO.getIncrementalMX() + 1, null, usuario);
        return editorOC;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarGeracaoDetalhe(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, EditorOC editorOC, Comando cmd, Date dataInicio, Date dataFim,  ControleRemessaVO controleRemessaVO, Double valorTotalTitulo, int quantidadeDoc, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        try {
            if (!listaDadosRemessaVOs.isEmpty()) {
                Iterator i = listaDadosRemessaVOs.iterator();
                while (i.hasNext()) {

                    ControleRemessaContaReceberVO dadosRemessaVO = (ControleRemessaContaReceberVO) i.next();
                    quantidadeDoc += 1;                    
                    if (dadosRemessaVO.getApresentarArquivoRemessa()) {
                    	if (dadosRemessaVO.getBaixarConta()) {
                    		controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
                    		gerarDadosDetalheSegmentoP(editorOC, cmd, dadosRemessaVO, controleRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), "02", valorTotalTitulo);
                    	} else {
                    		controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
                    		gerarDadosDetalheSegmentoP(editorOC, cmd, dadosRemessaVO, controleRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), "01", valorTotalTitulo);
                    		dadosRemessaVO.setSituacaoControleRemessaContaReceber(null);
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
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia() + controleRemessaVO.getContaCorrenteVO().getNumero() + controleRemessaVO.getContaCorrenteVO().getDigito(), 27, 40, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 41, 46, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(Uteis.removerAcentuacao(unidadeEnsinoVO.getRazaoSocial())).toUpperCase(), 47, 76, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "422", 77, 79, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "SAFRA", 80, 90, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 91, 94, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(new Date(), "ddMMyy"), 95, 100, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 101, 391, " ", true, false);
        //Número Remessa
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(controleRemessaVO.getIncrementalMX().toString(), 3), 392, 394, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000001", 395, 400, " ", true, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }

	public void gerarDadosDetalheSegmentoP(EditorOC editorOC, Comando cmd, ControleRemessaContaReceberVO dadosRemessaVO, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial, String tipoOcorrenciaContaRemessa, Double valorTotalTitulo) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 1, 1, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "02", 2, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 4, 17, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia() + controleRemessaVO.getContaCorrenteVO().getNumero() + controleRemessaVO.getContaCorrenteVO().getDigito(), 18, 31, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 32, 37, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 38, 62, " ", false, false);
        // adicionar o digito verificador no nosso numero
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero(), 63, 71, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 72, 101, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 102, 102, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 103, 104, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 105, 105, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 106, 107, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 108, 108, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, tipoOcorrenciaContaRemessa, 109, 110, " ", false, false);
    	
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNrDocumento(), 111, 120, " ", false, false);
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), PATTERN), 121, 126, " ", false, false);
        if (controleRemessaVO.getContaCorrenteVO().getCarteiraRegistrada() && controleRemessaVO.getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
	        //VALOR DO TITULO
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorBaseComAcrescimo())), 13), 127, 139, " ", false, false);
	        valorTotalTitulo += dadosRemessaVO.getValorBaseComAcrescimo();
        } else {
	        //VALOR DO TITULO
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorComAcrescimo())), 13), 127, 139, " ", false, false);
	        valorTotalTitulo += dadosRemessaVO.getValorComAcrescimo();
        }
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "422", 140, 142, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00000", 143, 147, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "09", 148, 149, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 150, 150, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(new Date(), PATTERN), 151, 156, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 157, 158, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "16", 159, 160, " ", false, false);

        Double juro = 0.01;
        Double valorTitulo = Uteis.arrendondarForcando2CadasDecimais(configuracaoFinanceiroVO.getCobrarJuroMultaSobreValorCheioConta() ? dadosRemessaVO.getValorComAcrescimo() : dadosRemessaVO.getValor()-dadosRemessaVO.getValorAbatimento());
        Double juroFinal = (valorTitulo * juro) / 30;        	        
        
        String juroStr = juroFinal.toString();
        if (juroStr.length() > 4) {
        	juroStr = (juroStr).substring(0, 4);        	
        }
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(juroStr)), 13), 161, 173, " ", false, false);
        
        if (dadosRemessaVO.getValorDescontoDataLimite() != 0 
        		&& (dadosRemessaVO.getDataLimiteConcessaoDesconto() == null || 
        			(dadosRemessaVO.getDataLimiteConcessaoDesconto() != null && 
        				(dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >0 
        						|| Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto()).equals(Uteis.getData(controleRemessaVO.getDataGeracao()))
        				)
        			)
        			)
        	) {
        //if (dadosRemessaVO.getValorDescontoDataLimite() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto() != null && (Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto()).equals(Uteis.getData(controleRemessaVO.getDataGeracao()))))) {
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto(), PATTERN), 174, 179, "0", false, false);
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(dadosRemessaVO.getValorDescontoDataLimite().toString())), 13), 180, 192, " ", false, false);
        } else {
     		if (dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
				linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto2(), PATTERN), 174, 179, " ", false, false);
				linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorDescontoDataLimite2())), 13), 180, 192, "0", false, false);
				dadosRemessaVO.setValorDescontoDataLimite2(0.0);                
     		} else if (dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
                linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto3(), PATTERN), 174, 179, " ", false, false);
                linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorDescontoDataLimite3())), 13), 180, 192, "0", false, false);
                dadosRemessaVO.setValorDescontoDataLimite3(0.0);
     		} else {
                linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 174, 179, "0", false, false); // Data do Desconto 1 - C022                	
                linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 180, 192, "0", false, false); // Valor/Percentual a ser Concedido - C023
     		}        
 		}
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 193, 205, "0", false, false);

        
//        // ABATIMENTO
//        if (controleRemessaVO.getContaCorrenteVO().getCarteiraRegistrada() && controleRemessaVO.getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
//	        //VALOR DO TITULO
//        	if (dadosRemessaVO.getValorBaseComAcrescimo() > 0 && dadosRemessaVO.getValorBaseComAcrescimo() > dadosRemessaVO.getValorComAcrescimo()) {
//        		Double valorDescFinal = dadosRemessaVO.getValorBaseComAcrescimo() - dadosRemessaVO.getValorComAcrescimo();
//        		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(valorDescFinal)), 13), 206, 218, " ", false, false);        		
//        	} else {
//        		if (dadosRemessaVO.getValorAbatimento() > 0) {
//            		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorAbatimento())), 13), 206, 218, " ", false, false);
//            	} else {
//            		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000000", 206, 218, " ", false, false);
//            	}
//        	}
//        } else {
//        	if (dadosRemessaVO.getValorAbatimento() > 0) {
//        		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorAbatimento())), 13), 206, 218, " ", false, false);
//        	} else {
//        		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000000", 206, 218, " ", false, false);
//        	}
//        }


        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(Uteis.obterDataFutura(dadosRemessaVO.getDataVencimento(), 1), PATTERN), 206, 211, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0200", 212, 215, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 216, 218, " ", false, false);
        
        if (dadosRemessaVO.getCodigoInscricao() == 0 || dadosRemessaVO.getCodigoInscricao() == 1) {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 219, 220, " ", false, false);
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()), 11).replaceAll(" ", ""), 221, 234, "0", false, false);        	
        } else {
        	String nr = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()).replaceAll(" ", "");
        	if (nr.length() == 14) {
        		linha = editorOC.adicionarCampoLinhaVersao2(linha, "02", 219, 220, " ", false, false);
                linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()), 14).replaceAll(" ", ""), 221, 234, "0", false, false);                		
        	} else {
        		linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 219, 220, " ", false, false);
                linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()), 11).replaceAll(" ", ""), 221, 234, "0", false, false);        
        	}
        }

        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(dadosRemessaVO.getNomeSacado()), 235, 274, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(dadosRemessaVO.getLogradouro()), 275, 314, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(dadosRemessaVO.getBairro()), 315, 324, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 325, 326, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(Uteis.removerAcentuacao(dadosRemessaVO.getCep())) , 327, 334, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(dadosRemessaVO.getCidade()), 335, 349, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(dadosRemessaVO.getEstado()), 350, 351, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " " , 352, 388, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "422" , 389, 391, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(controleRemessaVO.getIncrementalMX().toString(), 3), 392, 394, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha,  Uteis.preencherComZerosPosicoesVagas(String.valueOf(numeroSequencial), 6) , 395, 400, " ", false, false);
        
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }

	public void gerarDadosDetalheSegmentoTipo2(EditorOC editorOC, Comando cmd, ControleRemessaContaReceberVO dadosRemessaVO, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial, String tipoOcorrenciaContaRemessa) throws Exception {
		String linha = "";
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 1, 1, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 2, 81, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 82, 161, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 162, 241, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 242, 321, " ", false, false);
		
		if (dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >=0 ))) {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto2(), PATTERN), 322, 327, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(dadosRemessaVO.getValorDescontoDataLimite2().toString())), 13), 328, 340, " ", false, false);
		} else {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 322, 327, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 328, 340, " ", false, false);        	
		}
		if (dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >=0 ))) {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto3(), PATTERN), 341, 346, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(dadosRemessaVO.getValorDescontoDataLimite3().toString())), 13), 347, 359, " ", false, false);
		} else {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 341, 346, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 347, 359, " ", false, false);        	
		}
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " " , 360, 366, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCarteira() , 367, 369, "0", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia() , 370, 374, "0", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getNumero() + controleRemessaVO.getContaCorrenteVO().getDigito(), 375, 382, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero(), 383, 393, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, modulo11(controleRemessaVO.getContaCorrenteVO().getCarteira() + dadosRemessaVO.getNossoNumero()), 394, 394, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha,  Uteis.preencherComZerosPosicoesVagas(String.valueOf(numeroSequencial), 6) , 395, 400, " ", false, false);		
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
    public void gerarDadosTrailler(EditorOC editorOC, ControleRemessaVO controleRemessaVO, Comando cmd, int numeroSequencial, Double valorTotalTitulo, int quantidadeDocu) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 1, 1, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 2, 368, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(quantidadeDocu), 369, 376, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(valorTotalTitulo)), 13), 377, 391, "0", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(controleRemessaVO.getIncrementalMX().toString(), 3), 392, 394, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(numeroSequencial), 6), 395, 400, " ", false, false);
        linha += "\r\n";
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
                        controleRemessaVO.getRegistroDetalheRetornoVOs().add(processarRegistroTransacao(linha, new RegistroDetalheVO(), configuracaoFinanceiroVO, usuarioVO));
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
        header.setCodigoServico(Uteis.getValorInteiro(linha.substring(9, 11)));
        header.setCodigoEmpresa(Uteis.getValorInteiro(linha.substring(26, 40)));
        header.setNomeEmpresa(linha.substring(46, 76));
        header.setCodigoBanco(linha.substring(76, 79));
        header.setDataGeracaoArquivo(Uteis.getData(linha.substring(94, 100), PATTERN));
        //header.setNumeroAvisoBancario(Uteis.getValorInteiro(linha.substring(108, 113)));
        //header.setDataCredito(Uteis.getData(linha.substring(379, 385), PATTERN));
        header.setNumeroSequencialRegistro(Uteis.getValorInteiro(linha.substring(394, 400)));
		header.setLinhaHeader(linha);
    }
	
	private RegistroDetalheVO processarRegistroTransacao(String linha, RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		detalhe.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
        detalhe.setCedenteTipoInscricaoEmpresa(Uteis.getValorInteiro(linha.substring(1, 3)));
        detalhe.setCedenteNumeroInscricaoEmpresa(linha.substring(3, 17));
        
        detalhe.setCedenteNumeroAgencia(linha.substring(17, 22));
        detalhe.setCedenteNumeroConta(linha.substring(21, 31));      
//        detalhe.setCedenteIdentificacaoNoBanco(linha.substring(20, 37));
        detalhe.setSacadoNumeroInscricaoControle(linha.substring(37, 62));
        detalhe.setIdentificacaoTituloEmpresa(linha.substring(62, 71));

//        detalhe.setIndicadorRateioCredito(linha.substring(102, 104));

        detalhe.setMotivoRegeicao(linha.substring(104, 107));
        detalhe.setCarteira(Uteis.getValorInteiro(linha.substring(107, 108)));
        detalhe.setIdentificacaoOcorrencia(Uteis.getValorInteiro(linha.substring(108, 110)));
        
        detalhe.setDataOcorrencia(Uteis.getData(linha.substring(110, 116), PATTERN));

        //detalhe.setNumeroDocumentoCobranca(linha.substring(116, 126));
        detalhe.setIdentificacaoTituloBanco(linha.substring(116, 126));
        
        detalhe.setDataVencimentoTitulo(Uteis.getData(linha.substring(146, 152), PATTERN));
        detalhe.setValorNominalTitulo(Uteis.getValorDoubleComCasasDecimais(linha.substring(152, 165)));

        //detalhe.setSacadoBancoCodigo(linha.substring(165, 168));
        //detalhe.setSacadoAgenciaCodigo(linha.substring(168, 173));
        
        detalhe.setTarifaCobranca(Uteis.getValorDoubleComCasasDecimais(linha.substring(175, 188)));
        detalhe.setValorDespesas(Uteis.getValorDoubleComCasasDecimais(linha.substring(175, 188)));
        detalhe.setValorOutrasDespesas(Uteis.getValorDoubleComCasasDecimais(linha.substring(188, 201)));
        detalhe.setValorIOF(Uteis.getValorDoubleComCasasDecimais(linha.substring(214, 227)));
        
        detalhe.setValorAbatimento(Uteis.getValorDoubleComCasasDecimais(linha.substring(227, 240)));
        
        detalhe.setDesconto(Uteis.getValorDoubleComCasasDecimais(linha.substring(240, 253)));
        
        if(linha.substring(108, 110).equals("06") || linha.substring(108, 110).equals("17") || linha.substring(108, 110).equals("15") ) {
            detalhe.setValorPago(Uteis.getValorDoubleComCasasDecimais(linha.substring(253, 266)));
        }else {
        	detalhe.setValorPago(0.0);
        }        
        detalhe.setJurosMora(Uteis.getValorDoubleComCasasDecimais(linha.substring(266, 279)));
        detalhe.setValorOutrosCreditos(Uteis.getValorDoubleComCasasDecimais(linha.substring(279, 292)));
        
        //detalhe.setConfirmacaoInstituicaoProtesto(linha.substring(294, 295));
        detalhe.setDataCredito(Uteis.getData(linha.substring(295, 301), PATTERN));
        //detalhe.setMotivoRegeicao(linha.substring(318, 328));
        detalhe.setNumeroSequencialRegistroLote(Uteis.getValorInteiro(linha.substring(394, 400)));
        return detalhe;
    }
	
    private void processarRegistroTrailler(String linha, RegistroTrailerVO trailler) {
    	trailler.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
        trailler.setIdentificacaoRetorno(Uteis.getValorInteiro(linha.substring(1, 2)));
        trailler.setIdentificacaoTipoRegistro(Uteis.getValorInteiro(linha.substring(2, 4)));
        trailler.setCodigoBanco(linha.substring(4, 7));
        trailler.setQuantidadeTitulosEmCobranca(Uteis.getValorInteiro(linha.substring(17, 25)));
        trailler.setValorTitulosEmCobranca(Uteis.getValorDoubleComCasasDecimais(linha.substring(25, 39)));
        trailler.setNumeroAvisoBancario(Uteis.getValorInteiro(linha.substring(39, 47)));
        trailler.setNumeroSequencialRegistro(Uteis.getValorInteiro(linha.substring(394, 400)));	
    }

}