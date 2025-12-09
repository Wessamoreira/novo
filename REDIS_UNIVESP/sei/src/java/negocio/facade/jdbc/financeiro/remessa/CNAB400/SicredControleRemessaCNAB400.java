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
import negocio.comuns.utilitarias.dominios.Bancos;
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
public class SicredControleRemessaCNAB400 extends ControleAcesso implements ControleRemessaCNAB400LayoutInterfaceFacade {

    private static final long serialVersionUID = 1L;
    private static final String PATTERN = "ddMMyy";

    public SicredControleRemessaCNAB400() {
    }


    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public EditorOC executarGeracaoDadosArquivoRemessa(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        EditorOC editorOC = new EditorOC();
        Comando cmd = new Comando();
        double valorTotalTitulo = 0.0;
        int quantidadeDoc = 0;
        controleRemessaVO.setNumeroIncremental(1);
        controleRemessaVO.setIncrementalMX(getFacadeFactory().getControleRemessaMXFacade().consultarIncrementalPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), usuario));
        //Gera o Header
        gerarDadosHeader(editorOC, cmd, controleRemessaVO, controleRemessaVO.getUnidadeEnsinoVO(), configuracaoFinanceiroVO, usuario);
        //Gera o Detalhe
        executarGeracaoDetalhe(listaDadosRemessaVOs, editorOC, cmd, controleRemessaVO.getDataInicio(), controleRemessaVO.getDataFim(), controleRemessaVO, valorTotalTitulo, quantidadeDoc, configuracaoFinanceiroVO, usuario);
        //Gera o Trailler
        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
        gerarDadosTrailler(editorOC, cmd, controleRemessaVO, controleRemessaVO.getNumeroIncremental(), valorTotalTitulo, quantidadeDoc);
        editorOC.adicionarComando(cmd);
        getFacadeFactory().getControleRemessaMXFacade().alterarIncrementalPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), controleRemessaVO.getIncrementalMX() + 1, null, usuario);        
        return editorOC;
    }


    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarGeracaoDetalhe(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, EditorOC editorOC, Comando cmd, Date dataInicio, Date dataFim, ControleRemessaVO controleRemessaVO, double valorTotalTitulo, int quantidadeDoc, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        try {
        	
            if (!listaDadosRemessaVOs.isEmpty()) {
                Iterator i = listaDadosRemessaVOs.iterator();
                while (i.hasNext()) {
                    ControleRemessaContaReceberVO dadosRemessaVO = (ControleRemessaContaReceberVO) i.next();
                    valorTotalTitulo += dadosRemessaVO.getValorComAcrescimo();
                    quantidadeDoc += 1;
                    if (dadosRemessaVO.getApresentarArquivoRemessa()) {
                    	if (!dadosRemessaVO.getBaixarConta()) {
                        //if (!dadosRemessaVO.getContaRemetidaComAlteracao().booleanValue() || dadosRemessaVO.getSituacaoControleRemessaContaReceber().equals(SituacaoControleRemessaContaReceberEnum.REMETIDA_TITULO_CANCELADO)) {
                            controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
                            gerarDadosTransacao2(editorOC, cmd, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), controleRemessaVO, "01");
                    		dadosRemessaVO.setSituacaoControleRemessaContaReceber(null);
                    	} else {
//                    	} else if (dadosRemessaVO.getContaRemetidaComAlteracao_dataDescProgressivo().booleanValue() || dadosRemessaVO.getContaRemetidaComAlteracao_valorBase().booleanValue() || dadosRemessaVO.getContaRemetidaComAlteracao_valorDescProgressivo().booleanValue() 
//                				|| dadosRemessaVO.getContaRemetidaComAlteracao_dataVencimento().booleanValue() || dadosRemessaVO.getContaRemetidaComAlteracao_valorComAbatimentoAdicionadoOuModificado().booleanValue() || dadosRemessaVO.getContaRemetidaComAlteracao_valorComAbatimentoRemovido().booleanValue()) {
                    		controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
                            gerarDadosTransacao2(editorOC, cmd, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), controleRemessaVO, "02");
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
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getConvenio(), 27, 31, "", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 32, 45, "0", false, false);

        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 46, 76, " ", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "748", 77, 79, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "SICREDI", 80, 94, " ", true, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "yyyyMMdd"), 95, 102, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 103, 110, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(controleRemessaVO.getIncrementalMX().toString(), 7), 111, 117, "0", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 118, 390, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2.00", 391, 394, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000001", 395, 400, " ", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }

    public void gerarDadosTransacao2(EditorOC editorOC, Comando cmd, ControleRemessaContaReceberVO dadosRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial, ControleRemessaVO controleRemessaVO, String tipoOcorrenciaContaRemessa) throws Exception {
        try {
    	String linha = "";
        //TIPO DE REGISTRO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 1, 1, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "A", 2, 2, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "A", 3, 3, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "A", 4, 4, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 5, 16, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "A", 17, 17, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "A", 18, 18, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "B", 19, 19, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 20, 47, " ", false, false);
        //NOSSO NÚMERO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero(), 48, 56, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 57, 62, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "yyyyMMdd"), 63, 70, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 71, 71, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 72, 72, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 73, 73, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "B", 74, 74, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 75, 76, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 77, 78, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 79, 82, " ", false, false);
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 83, 92, "0", false, false);
//                
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0200", 93, 96, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 97, 108, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, tipoOcorrenciaContaRemessa, 109, 110, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNrDocumento(), 111, 120, " ", false, false);
        //DATA VENCIMENTO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), PATTERN), 121, 126, "0", false, false);
        if (controleRemessaVO.getContaCorrenteVO().getCarteiraRegistrada() && controleRemessaVO.getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
	        //VALOR DO TITULO
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorBaseComAcrescimo())), 13), 127, 139, " ", false, false);
        } else {
	        //VALOR DO TITULO
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorComAcrescimo())), 13), 127, 139, " ", false, false);
        }
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 140, 148, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "K", 148, 148, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 149, 149, " ", false, false);
        //DATA DE EMISSÃO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(new Date(), PATTERN), 151, 156, "0", false, false);
        //INSTRUÇÃO 1
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 157, 158, " ", false, false);
        //INSTRUÇÃO 2
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 159, 160, " ", false, false);
        //JUROS DE 1 DIA DE ATRASO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000100", 161, 173, " ", false, false);
        
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
        //if (dadosRemessaVO.getValorDescontoDataLimite() > 0 && dadosRemessaVO.getDataLimiteConcessaoDesconto() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >=0 ) {
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto(), PATTERN), 174, 179, " ", false, false);
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite(), 2)))), 13), 180, 192, "0", false, false);                    
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "000000", 174, 179, "0", false, false);
            linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000000", 180, 192, "0", false, false);
        }
        //MOEDA
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
        //CÓDIGO DE INSCRIÇÃO  01=CPF  -  02=CNPJ
        if (dadosRemessaVO.getCodigoInscricao() == 0 || dadosRemessaVO.getCodigoInscricao() == 1) {
    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "10", 219, 220, " ", false, false);
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()), 11).replaceAll(" ", ""), 221, 234, "0", false, false);        
    	} else {
        	String nr = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()).replaceAll(" ", "");
        	if (nr.length() == 14) {
	    		linha = editorOC.adicionarCampoLinhaVersao2(linha, "20", 219, 220, " ", false, false);
	            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()), 14).replaceAll(" ", ""), 221, 234, "0", false, false);
        	} else {
        		linha = editorOC.adicionarCampoLinhaVersao2(linha, "10", 219, 220, " ", false, false);
                linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()), 11).replaceAll(" ", ""), 221, 234, "0", false, false);        		
        	}
    	}
        
        //NÚMERO DE INSCRIÇÃO  CPF OU CNPJ
        //NOME DO SACADO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(dadosRemessaVO.getNomeSacado().toString()), 235, 274, " ", true, false);
        //LOGRADOURO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(dadosRemessaVO.getLogradouro()), 275, 314, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00000", 315, 319, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000000", 320, 325, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 326, 326, " ", true, false);
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(Uteis.removerAcentuacao(dadosRemessaVO.getCep()).replaceAll(" ", "")), 327, 334, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00000", 335, 339, " ", true, false);
        //SACADOR OU AVALISTA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 340, 394, " ", true, false);
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
    public void gerarDadosTrailler(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, int numeroSequencial, double valorTotalTitulo, int quantidadeDocu) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 1, 1, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 2, 2, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "748", 3, 5, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getConvenio(), 6, 10, "", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 11, 394, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 395, 400, "0", false, false);
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

	@Override
	public void processarArquivoRetorno(ControleRemessaVO controleRemessaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		// TODO Auto-generated method stub
		
	}
}