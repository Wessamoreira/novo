package negocio.facade.jdbc.financeiro.remessa.CNAB400;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ControleRemessaVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.utilitarias.Comando;
import negocio.comuns.utilitarias.EditorOC;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.remessa.ControleRemessaCNAB400LayoutInterfaceFacade;

public class DaycovalControleRemessaCNAB400 extends ControleAcesso implements ControleRemessaCNAB400LayoutInterfaceFacade {

	private static final long serialVersionUID = 1L;
	 	
	@Override
	public EditorOC executarGeracaoDadosArquivoRemessa(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		EditorOC editorOC = new EditorOC();
        Comando cmd = new Comando();
        controleRemessaVO.setNumeroIncremental(1);
        gerarHeaderArquivo(editorOC, cmd, controleRemessaVO, controleRemessaVO.getUnidadeEnsinoVO(), configuracaoFinanceiroVO, usuario);
        //gerarHeaderLote(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO);
        executarGeracaoDetalhe(listaDadosRemessaVOs, editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, usuario);
        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
        //gerarTrailerLote(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO);
        gerarTrailerArquivo(editorOC, cmd, controleRemessaVO);
        editorOC.adicionarComando(cmd);
        return editorOC;
	}
	
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarGeracaoDetalhe(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        try {
            if (!listaDadosRemessaVOs.isEmpty()) {
                Iterator i = listaDadosRemessaVOs.iterator();
                while (i.hasNext()) {
                    ControleRemessaContaReceberVO dadosRemessaVO = (ControleRemessaContaReceberVO) i.next();
                    if (dadosRemessaVO.getApresentarArquivoRemessa()) {
                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
                        gerarDetalhe(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO);
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
    public void gerarHeaderArquivo(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 1, 1, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 2, 2, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "REMESSA", 3, 9, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 10, 11, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "COBRANCA", 12, 26, " ", true, false);
        //Identificacao Empresa no Banco => 027 - 038
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCodigoTransmissaoRemessa(), 27, 38, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 39, 46, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(unidadeEnsinoVO.getRazaoSocial()), 30), 47, 76, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "707", 77, 79, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "BANCO DAYCOVAL", 80, 94, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyy"), 95, 100, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 101, 394, " ", false, false);
        
        
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumero(), 53, 57, " ", false, false);
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 58, 58, " ", false, false);
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCodigoCedente(), 59, 64, " ", false, false);
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 133, 142, " ", true, false);
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 143, 143, " ", true, false);
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyyyy"), 144, 151, " ", false, false);     
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.gethoraHHMMSS(controleRemessaVO.getDataGeracao()), 152, 157, " ", false, false);
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 158, 163, " ", false, false); // Sequência (NSA) - *G018
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000001", 395, 400, " ", false, false);
        //linha += "\r\n";
        cmd.adicionarLinhaComando(linha, 0);
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gerarTrailerArquivo(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 1, 1, "", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 2, 394, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getNumeroIncremental().toString(), 395, 400, "0", false, false);
        linha += "\r\n";
        cmd.adicionarLinhaComando(linha, 0);
    }
    
    public void gerarHeaderLote(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
    	String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental()), 4, 7, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "R", 9, 9, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 10, 11, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 12, 13, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "030", 14, 16, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 17, 17, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 18, 18, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 19, 33, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCodigoCedente(), 34, 39, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 40, 53, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumero(), 54, 58, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 59, 59, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCodigoCedente(), 60, 65, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 66, 72, " ", false, false); // Código do Modelo Personalizado - C078
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 73, 73, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(controleRemessaVO.getUnidadeEnsinoVO().getRazaoSocial()), 30), 74, 103, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 104, 143, " ", false, false); // Mensagem 1 - C073
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 144, 183, " ", false, false); // Mensagem 2 - C073
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 184, 191, " ", false, false); // Número Remessa/Retorno - G079 
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyyyy"), 192, 199, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 200, 207, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 208, 240, " ", false, false);
        linha += "\r\n";
        cmd.adicionarLinhaComando(linha, 0);
    }
    
    public void gerarTrailerLote(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
    	String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental()), 4, 7, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "5", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 9, 17, " ", false, false); // Uso Exclusivo FEBRABAN/CNAB
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 18, 23, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 24, 29, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 30, 46, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 47, 52, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 53, 69, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 70, 75, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 76, 92, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 93, 123, " ", false, false); // Uso Exclusivo FEBRABAN/CNAB
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 124, 240, " ", false, false); // Uso Exclusivo FEBRABAN/CNAB
        linha += "\r\n";
        cmd.adicionarLinhaComando(linha, 0);
    }
    
    public void gerarDetalhe(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleRemessaContaReceberVO dadosRemessaVO) throws Exception {
    	String linha = "";          	
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 1, 1, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "02", 2, 3, " ", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 4, 17, " ", false, false);
        // identificacao da emrpesa no banco => 18 - 37
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCodigoTransmissaoRemessa(), 18, 37, " ", true, false);
        String digitonossonumero = Uteis.gerarDigitoVerificadorNossoNumeroCodigoCedenteBradesco(dadosRemessaVO.getCarteira(), dadosRemessaVO.getNossoNumero());
        String nossonumero = dadosRemessaVO.getNossoNumero() + digitonossonumero;  
        linha = editorOC.adicionarCampoLinhaVersao2(linha, nossonumero, 38, 62, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 63, 70, "0", false, false);
        // nosso numer com digito verificador calculado e concatenado
        linha = editorOC.adicionarCampoLinhaVersao2(linha, nossonumero, 71, 83, "0", false, false);       
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 84, 107, " ", false, false);       
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "4", 108, 108, " ", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 109, 110, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNrDocumento().toString(), 111, 120, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyy"), 121, 126, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorComAcrescimo())), 13), 127, 139, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "707", 140, 142, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 143, 146, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 147, 147, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "12", 148, 149, "", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 150, 150, "", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyy"), 151, 156, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 157, 160, "0", false, false);
		Double juro = 0.01;
        Double valorTitulo = Uteis.arrendondarForcando2CadasDecimais(configuracaoFinanceiroVO.getCobrarJuroMultaSobreValorCheioConta() ? dadosRemessaVO.getValorComAcrescimo() : dadosRemessaVO.getValor()-dadosRemessaVO.getValorAbatimento());
        Double juroFinal = (valorTitulo * juro) / 30;
        String juroStr = "";
        if (juroFinal.toString().length() > 4) {
        	juroStr = (juroFinal.toString()).substring(0, 4);
        } else {
        	juroStr = juroFinal.toString();
        }
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(juroStr.toString())), 13), 161, 173, "0", false, false);
        String data = Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto(), "ddMMyy");
        if (data.equals("")) {
//        	data = Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyy");
        	data = "000000";
        }
        linha = editorOC.adicionarCampoLinhaVersao2(linha, data, 174, 179, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorDescontoDataLimite())), 13), 180, 192, "0", false, false);                
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 193, 218, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 219, 220, " ", false, false); // Código do Juros de Mora - C018
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.retirarMascaraCPF(dadosRemessaVO.getNumeroInscricao())), 14), 221, 234, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(dadosRemessaVO.getNomeSacado(), 30), 235, 264, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 265, 274, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(dadosRemessaVO.getLogradouro(), 40), 275, 314, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(dadosRemessaVO.getBairro()), 12), 315, 326, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removeCaractersEspeciais(dadosRemessaVO.getCep()).replaceAll(" ", ""), 327, 334, " ", true, false);
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removeCaractersEspeciais(Uteis.retornarPrefixoOuSufixoDoCep(dadosRemessaVO.getCep(), "prefixo")), 327, 330, " ", true, false);
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removeCaractersEspeciais(Uteis.retornarPrefixoOuSufixoDoCep(dadosRemessaVO.getCep(), "sufixo")), 331, 334, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getCidade()), 15), 335, 349, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getEstado()) , 2), 350, 351, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(controleRemessaVO.getUnidadeEnsinoVO().getRazaoSocial()), 30), 352, 381, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 382, 391, " ", false, false); // Número de Inscrição - G006
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 392, 393, "", false, false); // Número de Inscrição - G006
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 394, 394, " ", false, false); // Nosso Nº no Banco Correspondente - C032
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getNumeroIncremental().toString(), 395, 400, "0", false, false);          
//        linha += "\r\n";
        cmd.adicionarLinhaComando(linha, 0);
    }
    
    public void gerarDetalheSegmentoQ(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleRemessaContaReceberVO dadosRemessaVO) throws Exception {
    	String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 4, 7, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 9, 13, " ", false, false); // Nº Sequencial do Registro no Lote - G038
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "Q", 14, 14, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 15, 15, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 16, 17, " ", false, false); // Código de Movimento Remessa - C004
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 18, 18, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 19, 33, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(dadosRemessaVO.getNomeSacado(), 40), 34, 73, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(dadosRemessaVO.getLogradouro(), 40), 74, 113, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(dadosRemessaVO.getBairro(), 15), 114, 128, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.retornarPrefixoOuSufixoDoCep(dadosRemessaVO.getCep(), "prefixo"), 129, 133, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.retornarPrefixoOuSufixoDoCep(dadosRemessaVO.getCep(), "sufixo"), 134, 136, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(dadosRemessaVO.getCidade(), 15), 137, 151, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(dadosRemessaVO.getEstado() , 2), 137, 151, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 154, 154, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 155, 169, " ", false, false); // Número de Inscrição - G006
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(dadosRemessaVO.getNomeSacado(), 40), 170, 209, " ", false, false); 
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 210, 212, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 213, 232, " ", false, false); // Nosso Nº no Banco Correspondente - C032
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 233, 240, " ", false, false);
//        linha += "\r\n";
        cmd.adicionarLinhaComando(linha, 0);
    }
    
	@Override
	public void processarArquivoRetorno(ControleRemessaVO controleRemessaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		// TODO Auto-generated method stub
		
	}
}