/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.financeiro.remessa.contapagar.CNAB400;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaPagarControleRemessaContaPagarVO;
import negocio.comuns.financeiro.ControleCobrancaPagarVO;
import negocio.comuns.financeiro.ControleRemessaContaPagarVO;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.TipoContaEnum;
import negocio.comuns.financeiro.enumerador.TipoLancamentoContaPagarEnum;
import negocio.comuns.financeiro.enumerador.TipoServicoContaPagarEnum;
import negocio.comuns.utilitarias.Comando;
import negocio.comuns.utilitarias.EditorOC;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.financeiro.remessa.contapagar.ControleRemessaContaPagarLayoutImpl;
import negocio.interfaces.financeiro.remessa.ControleRemessaContaPagarLayoutInterfaceFacade;

/**
 *
 * @author Otimize-04
 */
public class BradescoRemessaContaPagarCNAB400 extends ControleRemessaContaPagarLayoutImpl implements ControleRemessaContaPagarLayoutInterfaceFacade {

    private static final long serialVersionUID = 1L;
    private static final String PATTERN = "ddMMyy";

    public BradescoRemessaContaPagarCNAB400() {
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public EditorOC executarGeracaoDadosArquivoRemessa(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        EditorOC editorOC = new EditorOC();
        Comando cmd = new Comando();
        controleRemessaVO.setNumeroIncremental(1);
        controleRemessaVO.setIncrementalMXCP(getFacadeFactory().getControleRemessaMXFacade().consultarIncrementalCPPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), usuario));
        //Gera o Header
        gerarDadosHeader(editorOC, cmd, controleRemessaVO, controleRemessaVO.getUnidadeEnsinoVO(), configuracaoFinanceiroVO, usuario);
        //Gera o Detalhe Segmento P
        executarGeracaoDetalhe(listaDadosRemessaVOs, editorOC, cmd, controleRemessaVO.getDataInicio(), controleRemessaVO.getDataFim(), controleRemessaVO, configuracaoFinanceiroVO, usuario);
        //Gera o Trailler
        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
        gerarDadosTrailler(editorOC, cmd, controleRemessaVO, controleRemessaVO.getNumeroIncremental());

        editorOC.adicionarComando(cmd);
//        getFacadeFactory().getControleRemessaMXFacade().alterarIncrementalPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), controleRemessaVO.getIncrementalMX() + 1, usuario);
        return editorOC;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarGeracaoDetalhe(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs, EditorOC editorOC, Comando cmd, Date dataInicio, Date dataFim, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        try {
            if (!listaDadosRemessaVOs.isEmpty()) {
                Iterator i = listaDadosRemessaVOs.iterator();
                while (i.hasNext()) {
                    ContaPagarControleRemessaContaPagarVO dadosRemessaVO = (ContaPagarControleRemessaContaPagarVO) i.next();
                    if (dadosRemessaVO.getApresentarArquivoRemessa()) {
                    	if (!dadosRemessaVO.getContaRemetidaComAlteracao().booleanValue() || dadosRemessaVO.getSituacaoControleRemessaContaReceber().equals(SituacaoControleRemessaContaReceberEnum.REMETIDA_TITULO_CANCELADO)) {
                    		controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
                    		gerarDadosDetalheSegmentoP(editorOC, cmd, dadosRemessaVO, controleRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), "01");
                    		controleRemessaVO.setValorTotalRemessa(dadosRemessaVO.getContaPagar().getValorPrevisaoPagamento() + controleRemessaVO.getValorTotalRemessa());
                    		dadosRemessaVO.setSituacaoControleRemessaContaReceber(null);
                    	}
                    }
                }
            } else {
                throw new Exception("Nenhuma conta foi encontrada com os dados informados, por favor verifique o período.");
            }
            //getFacadeFactory().getContaReceberFacade().alterarDataArquivoRemessa(listaDadosRemessaVOs, new Date(), usuarioVO);
        } catch (Exception e) {
            throw e;
        } finally {
        }

    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gerarDadosHeader(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 1, 1, " ", false, false);
        // Codigo de Comunicação Fornecida pelo Bradesco
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCodigoComunicacaoRemessaCP(), 2, 9, "0", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 10, 10, " ", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(unidadeEnsinoVO.getCNPJ()), 11, 25, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(Uteis.removerAcentuacao(unidadeEnsinoVO.getRazaoSocial())).toUpperCase(), 26, 65, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "20", 66, 67, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 68, 68, " ", false, false);
        //linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 69, 73, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getIncrementalMXCP()), 5), 69, 73, "0", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 74, 78, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(new Date(), "yyyyMMdd"), 79, 86, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.gethoraHHMMSS_tamanho6(new Date()), 87, 92, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 93, 477, " ", false, false);
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(controleRemessaVO.getNumeroIncremental().toString(), 9), 478, 486, " ", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 487, 494, " ", false, false);
        //Data Gravação Remessa
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000001", 495, 500, " ", true, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }

	public void gerarDadosDetalheSegmentoP(EditorOC editorOC, Comando cmd, ContaPagarControleRemessaContaPagarVO dadosRemessaVO, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial, String tipoOcorrenciaContaRemessa) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 1, 1, " ", false, false);
//        Tipo de Inscrição do Fornecedor
//        1 = CPF
//        2 = CNPJ
//        3 = OUTROS
        String tipoInsc = "1";
        if (dadosRemessaVO.getTipoSacado().equals("AL") || dadosRemessaVO.getTipoSacado().equals("FU") || dadosRemessaVO.getTipoSacado().equals("RF")) {
            tipoInsc = "1";
        } else if (dadosRemessaVO.getTipoSacado().equals("FO")) {
        	if (dadosRemessaVO.getContaPagar().getFornecedor().getTipoEmpresa().equals("FI")) {
        		tipoInsc = "1";
        	} else {
        		tipoInsc = "2";
        	}
        } else if (dadosRemessaVO.getTipoSacado().equals("PA")) {
        	tipoInsc = "2";
        }
        linha = editorOC.adicionarCampoLinhaVersao2(linha, tipoInsc, 2, 2, " ", false, false);

        String cnpjcpf1 = "";
        String cnpjcpf2 = "";
        String cnpjcpf3 = "";                
        String cnpjcpf = Uteis.removerMascara(dadosRemessaVO.getCnpjOuCpfFavorecido());
        if (cnpjcpf.length() > 12) {
        	cnpjcpf1 = cnpjcpf.substring(0, 8);
        	cnpjcpf2 = cnpjcpf.substring(8, 12);
        	cnpjcpf3 = cnpjcpf.substring(12);
        } else {
        	cnpjcpf1 = cnpjcpf.substring(0, 9);
        	cnpjcpf2 = "0000";
        	cnpjcpf3 = cnpjcpf.substring(9);
        }
        linha = editorOC.adicionarCampoLinhaVersao2(linha, cnpjcpf1, 3, 11, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, cnpjcpf2, 12, 15, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, cnpjcpf3, 16, 17, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(Uteis.removerAcentuacao(dadosRemessaVO.getNomeFavorecido())).toUpperCase(), 18, 47, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(dadosRemessaVO.getLogradouroFavorecido()), 48, 87, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(Uteis.removerAcentuacao(dadosRemessaVO.getCepFavorecido())), 88, 95, " ", false, false);

        /// obs verifica, pois segundo manual esses campos somente sao preenchidos quando o banco for bradesco (237), 
        //  porem ainda nao foi feito o recurso para teste.
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getContaPagar().getBancoRemessaPagar().getNrBanco(), 96, 98, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getContaPagar().getNumeroAgenciaRecebimento(), 99, 103, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getContaPagar().getDigitoAgenciaRecebimento(), 104, 104, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getContaPagar().getContaCorrenteRecebimento(), 105, 117, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getContaPagar().getDigitoCorrenteRecebimento(), 118, 119, " ", false, false);
        
        // Número do pagamento
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getContaPagar().getNossoNumero().toString(), 120, 135, " ", false, false);
        // Carteira, somente banco 237 ( bradesco ). obter do codigo de barras conforme orientação manual
        if (dadosRemessaVO.getContaPagar().getTipoLancamentoContaPagar().name().equals(TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_OUTRO_BANCO.name()) &&
        	dadosRemessaVO.getContaPagar().getLinhaDigitavel1().length() > 0 && dadosRemessaVO.getContaPagar().getLinhaDigitavel1().substring(0, 2).equals("237")) {
	        	String carteira = "";
	        	if (!dadosRemessaVO.getContaPagar().getLinhaDigitavel1().equals("")) {
		        	carteira = dadosRemessaVO.getContaPagar().getLinhaDigitavel2().substring(4, 5);
		        	carteira += dadosRemessaVO.getContaPagar().getLinhaDigitavel3().substring(0, 1);
	        	} else if (!dadosRemessaVO.getContaPagar().getCodigoBarra().equals("")) {
		        	carteira = dadosRemessaVO.getContaPagar().getCodigoBarra().substring(24, 26);        		
	        	}
	        	linha = editorOC.adicionarCampoLinhaVersao2(linha, carteira, 136, 138, "0", false, false);
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 136, 138, "0", false, false);
        }
        // nosso numero, somente banco 237 ( bradesco ). obter do codigo de barras conforme orientação manual
        if (dadosRemessaVO.getContaPagar().getTipoLancamentoContaPagar().name().equals(TipoLancamentoContaPagarEnum.LIQUIDACAO_TITULO_OUTRO_BANCO.name()) &&
            	dadosRemessaVO.getContaPagar().getLinhaDigitavel1().length() > 0 && dadosRemessaVO.getContaPagar().getLinhaDigitavel1().substring(0, 2).equals("237")) {
        	String nossonumero = "";
        	if (!dadosRemessaVO.getContaPagar().getLinhaDigitavel1().equals("")) {
        		nossonumero = dadosRemessaVO.getContaPagar().getLinhaDigitavel3().substring(1);
        		nossonumero += dadosRemessaVO.getContaPagar().getLinhaDigitavel4().substring(0);
        		nossonumero += dadosRemessaVO.getContaPagar().getLinhaDigitavel5().substring(0, 3);
        	} else if (!dadosRemessaVO.getContaPagar().getCodigoBarra().equals("")) {
        		nossonumero = dadosRemessaVO.getContaPagar().getCodigoBarra().substring(26, 37);        		
        	}
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, nossonumero, 139, 150, "0", false, false);
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 139, 150, "0", false, false);
        }
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 151, 165, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "yyyyMMdd"), 166, 173, "", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 174, 181, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 182, 189, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 190, 190, "0", false, false);
        // Fator de Vencimento. obter do codigo de barras conforme orientação manual
        if (!dadosRemessaVO.getContaPagar().getLinhaDigitavel1().equals("") || !dadosRemessaVO.getContaPagar().getCodigoBarra().equals("")) {
        	String fatorVencimento = "";
        	if (!dadosRemessaVO.getContaPagar().getLinhaDigitavel8().equals("")) {
        		fatorVencimento = dadosRemessaVO.getContaPagar().getLinhaDigitavel8().substring(0, 4);
        	} else if (!dadosRemessaVO.getContaPagar().getCodigoBarra().equals("")) {
        		fatorVencimento = dadosRemessaVO.getContaPagar().getCodigoBarra().substring(5, 9);        		
        	}
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, fatorVencimento, 191, 194, "0", false, false);
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 191, 194, "0", false, false);
        }
        
        // Valor Documento. obter do codigo de barras conforme orientação manual
//        if (!dadosRemessaVO.getContaPagar().getLinhaDigitavel8().equals("") || !dadosRemessaVO.getContaPagar().getCodigoBarra().equals("")) {
//        	String valorDoc = "";
//        	if (!dadosRemessaVO.getContaPagar().getLinhaDigitavel8().equals("")) {
//        		valorDoc = dadosRemessaVO.getContaPagar().getLinhaDigitavel8().substring(4);
//        	} else if (!dadosRemessaVO.getContaPagar().getCodigoBarra().equals("")) {
//        		valorDoc = dadosRemessaVO.getContaPagar().getCodigoBarra().substring(10, 20);        		
//        	}
//        	linha = editorOC.adicionarCampoLinhaVersao2(linha, valorDoc, 195, 204, "0", false, false);
//        } else {
//        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 195, 204, "0", false, false);
//        }
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getContaPagar().getValor())), 10), 195, 204, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getContaPagar().getValorPrevisaoPagamento())), 15), 205, 219, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getContaPagar().getDesconto())), 15), 220, 234, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getContaPagar().getJuro() + dadosRemessaVO.getContaPagar().getMulta())), 15), 235, 249, "0", false, false);
		//01 - Nota fiscal/Fatura
		//02 - fatura
		//03 - nota fiscal
		//04 - duplicata
		//05 - outros
    	if (dadosRemessaVO.getTipoServicoContaPagar() == null) {
    		dadosRemessaVO.setTipoServicoContaPagar(TipoServicoContaPagarEnum.BRADESCO_OUTROS);
    	}
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getTipoServicoContaPagar().getValor(), 250, 251, "", false, false);
        //Número Nota Fiscal/Fatura Duplicata
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getContaPagar().getNumeroNotaFiscalEntrada(), 252, 261, "0", false, false);
        //Série Documento
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 262, 263, " ", false, false);
        // Modalidade Pagamento
        //  01 Crédito em conta
        //  02 Cheque OP
        //  05 Crédito em conta real time
        //  03 DOC COMPE
        //  08 TED
        //  30 Rastreamento de Títulos
        //  31 Títulos Terceiros
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getContaPagar().getTipoLancamentoContaPagar().getValor(), 264, 265, " ", false, false);
        //linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 264, 265, " ", false, false);
        // Data para efetivação do pagamento, senao informar assume o campo data vencimento.
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 266, 273, " ", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 274, 276, " ", false, false);
        // Situação do agendamento
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 277, 278, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 279, 288, " ", false, false);
        // Tipo de Movimento
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 289, 289, " ", false, false);
        // Codigo de Movimento
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 290, 291, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 292, 295, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 296, 310, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 311, 325, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 326, 331, " ", false, false);
        // Avalista
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 332, 371, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 372, 372, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 373, 373, " ", false, false);
        // informações complementares
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 374, 413, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 414, 415, " ", false, false);
        // Uso da Empresa
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 416, 450, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 451, 472, " ", false, false);
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getContaPagar().getCodigo().toString(), 473, 477, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 478, 478, " ", false, false);
        // Tipo de conta fornecedor ( poupanca ou corrente ). Somente para modalidade 01 e 05
        if (dadosRemessaVO.getTipoContaEnum() == TipoContaEnum.CREDITO_EM_CONTA_CORRENTE) {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 479, 479, " ", false, false);	
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 479, 479, " ", false, false);
        }
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 480, 486, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 487, 494, " ", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(numeroSequencial), 6), 495, 500, " ", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }


    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gerarDadosTrailler(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, int numeroSequencial) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 1, 1, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(numeroSequencial), 6), 2, 7, " ", false, false);
        // Total valor de pagamentos
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(controleRemessaVO.getValorTotalRemessa())), 17), 8, 24, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 25, 494, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(numeroSequencial), 6), 495, 500, " ", false, false);
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
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void processarArquivoRetornoPagar(ControleCobrancaPagarVO controleCobrancaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception{
		
		
	}
	

}