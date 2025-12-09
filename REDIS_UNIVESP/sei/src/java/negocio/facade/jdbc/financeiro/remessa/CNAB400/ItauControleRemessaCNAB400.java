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
import negocio.comuns.financeiro.RegistroArquivoVO;
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

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Otimize-04
 */
public class ItauControleRemessaCNAB400 extends ControleAcesso implements ControleRemessaCNAB400LayoutInterfaceFacade {

	public ControleRemessaVO controleRemessaVO;
	private static final String PATTERN = "ddMMyy";

    public ItauControleRemessaCNAB400() {
    }

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
                        processarHeaderRetorno(linha, controleRemessaVO.getRegistroHeaderRetornoVO());
                        break;
                    case 1:
                    	//O sistema valida para processar o arquivo somente se a operação for diferente de 09 porque esse código indica devolução do banco dos boletos.
                        if (linha.substring(0, 1).equals("1")) {
                            controleRemessaVO.getRegistroDetalheRetornoVOs().add(processarRegistroTransacao(linha, new RegistroDetalheVO(), controleRemessaVO.getContaCorrenteVO().getCarteira(), configuracaoFinanceiroVO, usuarioVO, controleRemessaVO.getRegistroHeaderRetornoVO()));
                        }
                        break;
                    case 9:
                        processarRegistroRetornoTrailler(linha, controleRemessaVO.getRegistroTrailerRetornoVO());
                        break;
                }
            }
            if (contador == 2) {
                throw new Exception("Não existem registros de contas nesse arquivo de retorno de remessa. Entre em contato com o seu banco!");
            }
//            executarCriacaoContaReceberRegistroArquivoVOs(getRegistroArquivoVO(), false);
            reader = null;
        } catch (StringIndexOutOfBoundsException e) {
            throw new ConsistirException("O arquivo selecionado é inválido pois não possui a quantidade adequada de caracteres. Detalhe: " + e.getMessage());
        }
    }
     
    private void processarHeaderRetorno(String linha, RegistroHeaderVO header) throws Exception {
        header.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
        header.setCodigoServico(Uteis.getValorInteiro(linha.substring(9, 11)));
        header.setNumeroAgencia(Uteis.getValorInteiro(linha.substring(26, 30)));
        header.setNumeroConta(Uteis.getValorInteiro(linha.substring(32, 37)));
        header.setNomeEmpresa(linha.substring(46, 76));
        header.setCodigoBanco(linha.substring(76, 79));
        header.setDataGeracaoArquivo(Uteis.getData(linha.substring(94, 100), PATTERN));
        header.setDataCredito(Uteis.getData(linha.substring(108, 114), "yyMMdd"));
//        header.setNumeroSequencialArquivo(Uteis.getValorInteiro(linha.substring(391, 394)));
        header.setNumeroSequencialRegistro(Uteis.getValorInteiro(linha.substring(394, 400)));
    }
	
    private RegistroDetalheVO processarRegistroTransacao(String linha, RegistroDetalheVO detalhe, String carteira, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO, RegistroHeaderVO header) throws Exception {
        detalhe.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
        detalhe.setCedenteNumeroInscricaoEmpresa(linha.substring(17, 28));
        detalhe.setCedenteNumeroAgencia(linha.substring(17, 21));
        detalhe.setCedenteNumeroConta(linha.substring(21, 28));
		detalhe.setIdentificacaoTituloEmpresa(linha.substring(37, 62));
    
        if (!carteira.equals("112")) {
        	detalhe.setIdentificacaoTituloEmpresa(linha.substring(62, 76));
            detalhe.setIdentificacaoTituloBanco(linha.substring(62, 76));                	
        } else {
        	detalhe.setIdentificacaoTituloEmpresa(linha.substring(37, 62));
        	detalhe.setIdentificacaoTituloBanco(linha.substring(85, 94));
        }
        
        detalhe.setCarteira(Uteis.getValorInteiro(linha.substring(107, 108)));
        detalhe.setIdentificacaoOcorrencia(Uteis.getValorInteiro(linha.substring(108, 110)));
        
        detalhe.setDataOcorrencia(Uteis.getData(linha.substring(110, 116), PATTERN));
        
        if (!linha.substring(146, 152).equals("000000") && !linha.substring(146, 152).equals("      ")) {
        	detalhe.setDataVencimentoTitulo(Uteis.getData(linha.substring(146, 152), PATTERN));
        } else {
        	detalhe.setDataVencimentoTitulo(Uteis.getData(linha.substring(110, 116), PATTERN));
        }
        detalhe.setValorNominalTitulo(Uteis.getValorDoubleComCasasDecimais(linha.substring(152, 165)));
//        detalhe.setSacadoBancoCodigo(linha.substring(165, 168));
        detalhe.setSacadoBancoCodigo(header.getCodigoBanco());
        detalhe.setSacadoAgenciaCodigo(linha.substring(168, 173));
        detalhe.setValorDespesas(Uteis.getValorDoubleComCasasDecimais(linha.substring(175, 188)));
        detalhe.setTarifaCobranca(Uteis.getValorDoubleComCasasDecimais(linha.substring(177, 188)));
        detalhe.setValorIOF(Uteis.getValorDoubleComCasasDecimais(linha.substring(214, 227)));
        detalhe.setValorAbatimento(Uteis.getValorDoubleComCasasDecimais(linha.substring(227, 240)));
        detalhe.setDesconto(Uteis.getValorDoubleComCasasDecimais(linha.substring(240, 253)));
        detalhe.setValorPago(Uteis.getValorDoubleComCasasDecimais(linha.substring(253, 266)));
        if (!detalhe.getValorPago().equals(0.0)) {
        	detalhe.setValorPago(detalhe.getValorPago() + detalhe.getTarifaCobranca());
        }
        detalhe.setJurosMora(Uteis.getValorDoubleComCasasDecimais(linha.substring(266, 279)));
        detalhe.setDataCredito(Uteis.getData(linha.substring(295, 301), "ddMMyy"));
        detalhe.setMotivoRegeicao(linha.substring(377, 385));
        detalhe.setNumeroSequencialRegistroLote(Uteis.getValorInteiro(linha.substring(394, 400)));
        return detalhe;
    }
        
    private void processarRegistroRetornoTrailler(String linha, RegistroTrailerVO trailler) {
        trailler.setTipoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
        trailler.setQuantidadeTitulosEmCobranca(Uteis.getValorInteiro(linha.substring(1, 9)));
        trailler.setValorTitulosEmCobranca(Uteis.getValorDoubleComCasasDecimais(linha.substring(9, 23)));
        trailler.setNumeroSequencialRegistro(Uteis.getValorInteiro(linha.substring(394, 400)));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public EditorOC executarGeracaoDadosArquivoRemessa(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        EditorOC editorOC = new EditorOC();
        Comando cmd = new Comando();
        controleRemessaVO.setNumeroIncremental(1);
        //Gera o Header
        gerarDadosHeader(editorOC, cmd, controleRemessaVO, controleRemessaVO.getUnidadeEnsinoVO(), configuracaoFinanceiroVO, usuario);
        //Gera o Detalhe
        executarGeracaoDetalhe(listaDadosRemessaVOs, editorOC, cmd, controleRemessaVO.getDataInicio(), controleRemessaVO.getDataFim(), controleRemessaVO, configuracaoFinanceiroVO, usuario);
        //Gera o Trailler
        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
        gerarDadosTrailler(editorOC, cmd, controleRemessaVO.getNumeroIncremental());

        editorOC.adicionarComando(cmd);
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
                    	if (!dadosRemessaVO.getBaixarConta()) {
                    		controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
                            gerarDadosTransacao2(editorOC, cmd, controleRemessaVO, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), "01");
                            if (controleRemessaVO.getContaCorrenteVO().getCarteiraRegistrada()) {
                                controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
                            	gerarDadosMultaOpcional(editorOC, cmd, controleRemessaVO, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental());
                            	controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
                            	gerarDadosTransacaoOpcional(editorOC, cmd, controleRemessaVO, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental());
                            }
                    	} else {
	                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
                            gerarDadosTransacao2(editorOC, cmd, controleRemessaVO, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), "02");
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
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), 27, 30, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 31, 32, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getNumero(), 33, 37, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getDigito(), 38, 38, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 39, 46, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(unidadeEnsinoVO.getRazaoSocial()), 47, 76, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 77, 79, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "BANCO ITAU SA", 80, 94, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyy"), 95, 100, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 101, 394, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000001", 395, 400, " ", false, false);
        linha += "\r";
        //controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
        cmd.adicionarLinhaComando(linha, 0);
    }

    public void gerarDadosTransacao2(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ControleRemessaContaReceberVO dadosRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial, String codMovRemessa) throws Exception {
        String linha = "";
        //TIPO DE REGISTRO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 1, 1, "0", false, false);
        //CÓDIGO DE INSCRIÇÃO EMPRESA => 01 = CPF / 02 = CNPJ
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "02", 2, 3, "0", false, false);
        //Nº DE INSCRIÇÃO DA EMPRESA(CPF/CNPJ)
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 4, 17, "0", false, false);
        //AGÊNCIA MANTENEDORA DA CONTA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getAgencia(), 18, 21, "0", false, false);
        //ZEROS 
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 22, 23, "0", false, false);
        //NÚMERO DA CONTA CORRENTE DA EMPRESA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getContaCorrente().toString(), 24, 28, "0", false, false);
        //DAC
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getDigitoContaCorrente().toString(), 29, 29, "0", false, false);
        //BRANCOS
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "   ", 30, 33, " ", false, false);
        //INSTRUÇÃO ALEGAÇÃO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000", 34, 37, "0", false, false);
        //USO DA EMPRESA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero(), 38, 62, "0", false, false);
        //NOSSO NÚMERO       
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero(), 63, 70, "0", false, false);
       
        //QTDE DE MOEDA VARIÁVEL
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000000", 71, 83, "0", false, false);
        //NÚMERO DA CARTEIRA NO BANCO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCarteira().toString(), 84, 86, "0", false, false);
        //USO DO BANCO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 87, 107, " ", false, false);
        //CODIGO DA CARTEIRA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "I", 108, 108, " ", false, false);
        //CÓDIGO DA OCORRÊNCIA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, codMovRemessa, 109, 110, "0", false, false);
        //NÚMERO DO DOCUMENTO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNrDocumento().toString(), 111, 120, " ", true, false);
        //DATA VENCIMENTO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), PATTERN), 121, 126, "0", false, false);

        if (controleRemessaVO.getContaCorrenteVO().getCarteiraRegistrada() && controleRemessaVO.getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
	        //VALOR DO TITULO
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorBaseComAcrescimo())), 13), 127, 139, " ", false, false);
        } else {
	        //VALOR DO TITULO
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorComAcrescimo())), 13), 127, 139, " ", false, false);
        }
        //CÓDIGO DO BANCO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Bancos.ITAU.getNumeroBanco(), 140, 142, "0", false, false);
        //AGÊNCIA COBRADORA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00000", 143, 147, "0", false, false);
        //ESPÉCIE DO TÍTULO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "04", 148, 149, " ", false, false);
        //ACEITE
        if (controleRemessaVO.getContaCorrenteVO().getCarteiraRegistrada() && controleRemessaVO.getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "A", 150, 150, " ", false, false);	
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 150, 150, " ", false, false);
        }
        
        //DATA DE EMISSÃO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(new Date(), PATTERN), 151, 156, "0", false, false);
        //INSTRUÇÃO 1
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "24", 157, 158, " ", false, false);
        //INSTRUÇÃO 2
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "10", 159, 160, " ", false, false);
        //JUROS DE 1 DIA DE ATRASO
        Double juro = 0.01;
        Double valorTitulo = Uteis.arrendondarForcando2CadasDecimais(configuracaoFinanceiroVO.getCobrarJuroMultaSobreValorCheioConta() ? dadosRemessaVO.getValorComAcrescimo() : dadosRemessaVO.getValor()-dadosRemessaVO.getValorAbatimento());
                
        Double juroFinal = Uteis.arrendondarForcando2CadasDecimais((valorTitulo * juro) / 30);        	        
        
        String juroStr = juroFinal.toString();
        if (juroStr.length() > 4) {
        	juroStr = (juroStr).substring(0, 4);        	
        }
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(juroStr)), 13), 161, 173, " ", false, false);
//???????????????????
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
        //if (dadosRemessaVO.getDataLimiteConcessaoDesconto() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >=0 ) {
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto(), PATTERN), 174, 179, " ", false, false);
        } else {
            linha = editorOC.adicionarCampoLinhaVersao2(linha, "000000", 174, 179, " ", false, false);
        }
        //VALOR DO DESCONTO
        if (dadosRemessaVO.getValorDescontoDataLimite() != 0) {
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite(), 2)))), 13), 180, 192, " ", false, false);
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
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()), 11).replaceAll(" ", ""), 221, 234, "0", false, false);        
        //NOME DO SACADO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNomeSacado().toString(), 235, 264, " ", true, false);
        //BRANCOS
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 265, 274, " ", false, false);
        //LOGRADOURO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getLogradouro(), 275, 314, " ", true, false);
        //BAIRRO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getBairro(), 315, 326, " ", true, false);
        //CEP
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(Uteis.removerAcentuacao(dadosRemessaVO.getCep())) , 327, 334, " ", false, false);
        //CIDADE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removeCaractersEspeciais(dadosRemessaVO.getCidade()), 335, 349, " ", true, false);
        //SIGLA DO ESTADO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getEstado(), 350, 351, " ", true, false);
        //SACADOR OU AVALISTA
        if (controleRemessaVO.getContaCorrenteVO().getCarteiraRegistrada() && controleRemessaVO.getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "PROTESTAR 5 DIAS APOS VENCTO.", 352, 381, " ", true, false);
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 352, 381, " ", true, false);
        }
        //BRANCOS
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 382, 385, " ", false, false);
        //DATA DE MORA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000000", 386, 391, " ", false, false);
        //PRAZO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 392, 393, " ", false, false);
        //BRANCOS
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 394, 394, " ", false, false);
        //NÚMERO SEQUENCIAL
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 395, 400, "0", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }

    public void gerarDadosTransacaoOpcional(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ControleRemessaContaReceberVO dadosRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial) throws Exception {
    	String linha = "";
    	//TIPO DE REGISTRO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "5", 1, 1, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 2, 121, " ", false, false);
    	//CÓDIGO DA OCORRÊNCIA
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 122, 123, "0", false, false);
    	//NÚMERO DE INSCRIÇÃO  CPF OU CNPJ
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()), 11).substring(0, 9), 124, 137, "0", false, false);        
    	//LOGRADOURO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getLogradouro(), 138, 177, " ", true, false);
    	//BAIRRO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getBairro(), 178, 189, " ", true, false);
    	//CEP
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(Uteis.removerAcentuacao(dadosRemessaVO.getCep())) , 190, 197, " ", false, false);
    	//CIDADE
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removeCaractersEspeciais(dadosRemessaVO.getCidade()), 198, 212, " ", true, false);
    	//SIGLA DO ESTADO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getEstado(), 213, 214, " ", true, false);
    	//BRANCOS
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 215, 394, " ", false, false);
    	//NÚMERO SEQUENCIAL
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 395, 400, "0", false, false);
    	linha += "\r";
    	cmd.adicionarLinhaComando(linha, 0);
    }

    public void gerarDadosMultaOpcional(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ControleRemessaContaReceberVO dadosRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial) throws Exception {
    	String linha = "";
    	//TIPO DE REGISTRO
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 1, 1, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 2, 2, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(Uteis.obterDataFutura(dadosRemessaVO.getDataVencimento(), 1), "ddMMyyyy"), 3, 10, "0", false, false);
    	Double multa = 0.0;
    	Double taxa = 2.0/100;
    	Double valorTitulo = Uteis.arrendondarForcando2CadasDecimais(configuracaoFinanceiroVO.getCobrarJuroMultaSobreValorCheioConta() ? dadosRemessaVO.getValorComAcrescimo() : dadosRemessaVO.getValor()-dadosRemessaVO.getValorAbatimento());
    	multa = valorTitulo * taxa; 
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(multa)), 13), 11, 23, " ", false, false);
    	//BRANCOS
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 24, 394, " ", false, false);
    	//NÚMERO SEQUENCIAL
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 395, 400, "0", false, false);
    	linha += "\r";
    	cmd.adicionarLinhaComando(linha, 0);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gerarDadosTrailler(EditorOC editorOC, Comando cmd, int numeroSequencial) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 1, 1, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 2, 394, " ", false, false);
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
    
    public ControleRemessaVO getControleRemessaVO() {
		if (controleRemessaVO == null) {
			controleRemessaVO = new ControleRemessaVO();
		}
		return controleRemessaVO;
	}

	public void setControleRemessaVO(ControleRemessaVO controleRemessaVO) {
		this.controleRemessaVO = controleRemessaVO;
	}
    
}
