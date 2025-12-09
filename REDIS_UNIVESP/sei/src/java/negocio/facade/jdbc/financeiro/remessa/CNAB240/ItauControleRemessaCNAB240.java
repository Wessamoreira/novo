package negocio.facade.jdbc.financeiro.remessa.CNAB240;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

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
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.remessa.ControleRemessaCNAB240LayoutInterfaceFacade;

public class ItauControleRemessaCNAB240 extends ControleAcesso implements ControleRemessaCNAB240LayoutInterfaceFacade {

	 private static final String PATTERN = "ddMMyy";
	 public Double valorTotal = 0.0;    
	
	public ItauControleRemessaCNAB240() {
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public EditorOC executarGeracaoDadosArquivoRemessa(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		EditorOC editorOC = new EditorOC();
        Comando cmd = new Comando();
        controleRemessaVO.setNumeroIncremental(1);
		controleRemessaVO.setIncrementalMX(getFacadeFactory().getControleRemessaMXFacade().consultarIncrementalPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), usuario));		
        gerarHeaderArquivo(editorOC, cmd, controleRemessaVO, controleRemessaVO.getUnidadeEnsinoVO(), configuracaoFinanceiroVO, usuario);
        //controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
        gerarHeaderLote(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO);
        executarGeracaoDetalhe(listaDadosRemessaVOs, editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, usuario);
        //controleRemessaVO.setNumeroIncremental((controleRemessaVO.getNumeroIncremental() * 4) - 2);
        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
        gerarTrailerLote(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO);
        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 2);
        gerarTrailerArquivo(editorOC, cmd, controleRemessaVO.getNumeroIncremental());
        getFacadeFactory().getControleRemessaMXFacade().alterarIncrementalPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), controleRemessaVO.getIncrementalMX() + 1, null, usuario);		
        editorOC.adicionarComando(cmd);
        return editorOC;
	}
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gerarHeaderArquivo(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000", 4, 7, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 9, 17, " ", false, false);     
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 18, 18, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(unidadeEnsinoVO.getCNPJ()), 19, 32, "0", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 33, 52, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 53, 53, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), 54, 57, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 58, 58, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000", 59, 65, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getNumero(), 66, 70, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 71, 71, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getDigito(), 72, 72, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(unidadeEnsinoVO.getRazaoSocial()), 30), 73, 102, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "BANCO ITAU SA", 103, 132, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 133, 142, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 143, 143, "", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyyyy"), 144, 151, " ", false, false);     
        linha = editorOC.adicionarCampoLinhaVersao2(linha, UteisData.getHoraHHMMSS(controleRemessaVO.getDataGeracao()), 152, 157, " ", false, false); // Sequência (NSA) - *G018//      
        linha = editorOC.adicionarCampoLinhaVersao2(linha,  " ", 158, 163, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "040", 164, 166, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 167, 240, " ", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }
    
    public void gerarHeaderLote(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
    	String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "R", 9, 9, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 10, 11, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 12, 13, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "030", 14, 16, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 17, 17, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 18, 18, " ", false, false);
        // numero de inscrição empresa CNPJ
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 19, 33, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 34, 53, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 54, 54, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), 55, 58, "0", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 59, 59, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000", 60, 66, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getNumero(), 67, 71, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 72, 72, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getDigito(), 73, 73, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(controleRemessaVO.getUnidadeEnsinoVO().getRazaoSocial()), 30), 74, 103, "0", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 104, 183, " ", false, false); // Mensagem 1 - C073
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 184, 191, " ", false, false); // Número Remessa/Retorno
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyyyy"), 192, 199, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 200, 207, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 208, 240, " ", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }
           
    public void gerarTrailerLote(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
    	String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "5", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 9, 17, " ", false, false); // Uso Exclusivo FEBRABAN/CNAB
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental()), 18, 23, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 24, 29, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 30, 46, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 47, 52, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 53, 69, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 70, 115, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 116, 123, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 124, 240, " ", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gerarTrailerArquivo(EditorOC editorOC, Comando cmd, Integer numIncremental) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9999", 4, 7, "9", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 9, 17, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000001", 18, 23, "0", false, false); // Quantidade de Lotes do Arquivo - G049
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numIncremental), 24, 29, "0", false, false); // Quantidade de Registros do Arquivo - G056        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 30, 35, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 36, 240, " ", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }           

    public void gerarDetalheSegmentoP(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleRemessaContaReceberVO dadosRemessaVO, String codMovRemessa) throws Exception {
    	String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental()), 9, 13, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "P", 14, 14, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 15, 15, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, codMovRemessa, 16, 17, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 18, 18, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), 19, 22, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 23, 23, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 24, 30, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getNumero(), 31, 35, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 36, 36, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 37, 37, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha,  dadosRemessaVO.getCarteira().toString(), 38, 40, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 41, 48, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getDigito(), 49, 49, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 50, 57, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 58, 62, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNrDocumento().toString(), 63, 72, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 73, 77, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 78, 85, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorComAcrescimo())), 13), 86, 100, "", false, false);   
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), 101, 105, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 106, 106, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "04", 107, 108, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 109, 109, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyyyy"), 110, 117, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 118, 118, "0", false, false);     
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
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 119, 126, " ", false, false); // Data do Juros de Mora - C019
            linha = editorOC.adicionarCampoLinhaVersao2(linha, "100000", 127, 141, " ", false, false);
        } else {      
            linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 119, 126, "0", false, false); // Data do Juros de Mora - C019
            linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 127, 141, "0", false, false);
        }      
         linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 142, 142, "0", false, false);
        
		// DATA LIMITE PARA CONCESSÃO DE DESCONTO
		if (dadosRemessaVO.getValorDescontoDataLimite() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto() == null
				|| (dadosRemessaVO.getDataLimiteConcessaoDesconto() != null && (dadosRemessaVO
						.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) > 0
						|| Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto())
								.equals(Uteis.getData(controleRemessaVO.getDataGeracao())))))) {

			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto(), "ddMMyyyy"), 143, 150, " ", false, false);
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite(), 2)))), 13), 151, 165, "0", false, false);                    
        } else {
    		if (dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
            	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto2(), "ddMMyyyy"), 143, 150, " ", false, false);
                linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite2(), 2)))), 13), 151, 165, "0", false, false);
                dadosRemessaVO.setValorDescontoDataLimite2(0.0);                
    		} else if (dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
            	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto3(), "ddMMyyyy"), 143, 150, " ", false, false);
                linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite3(), 2)))), 13), 151, 165, "0", false, false);
                dadosRemessaVO.setValorDescontoDataLimite3(0.0);
    		} else {            	
                linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 143, 150, "0", false, false); // Data do Desconto 1 - C022                	
                linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 151, 165, "0", false, false); // Valor/Percentual a ser Concedido - C023
    		}
        }        
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 166, 180, "0", false, false); // Valor do IOF a ser Recolhido - C024

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
        if (controleRemessaVO.getContaCorrenteVO().getHabilitarProtestoBoleto()) {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 221, 221, "0", false, false); // Código para Protesto - C026
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getQtdDiasProtestoBoleto_Str(), 222, 223, "0", false, false); // Número de Dias para Protesto - C027
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 221, 221, "0", false, false); // Código para Protesto - C026
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 222, 223, "0", false, false); // Número de Dias para Protesto - C027
        }
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 224, 224, " ", false, false); // Código para Baixa/Devolução - C028
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 225, 226, " ", false, false); // zero        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 227, 239, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 240, 240, " ", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }
    
    public void gerarDetalheSegmentoQ(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleRemessaContaReceberVO dadosRemessaVO, String codMovRemessa) throws Exception {
    	String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental()), 9, 13, "0", false, false); // Nº Sequencial do Registro no Lote - G038
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "Q", 14, 14, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 15, 15, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, codMovRemessa, 16, 17, "0", false, false); // Código de Movimento Remessa - C004
        if (dadosRemessaVO.getCodigoInscricao() == 0 || dadosRemessaVO.getCodigoInscricao() == 1) {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 18, 18, " ", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha,Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()),11).replaceAll(" ", ""),19, 33, "0", false, false);
		} else {
			String nr = Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()).replaceAll(" ", "");
			if (nr.length() == 14) {
				linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 18, 18, " ", false, false);
				linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()), 14).replaceAll(" ", ""), 19, 33, "0", false, false);
			} else {
				linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 18, 18, " ", false, false);
				linha = editorOC.adicionarCampoLinhaVersao2(linha,	Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()),11).replaceAll(" ", ""),	19, 33, "0", false, false);
			}
		}	        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNomeSacado())), 40), 34, 73, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getLogradouro()), 40), 74, 113, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getBairro()), 15), 114, 128, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.retornarPrefixoOuSufixoDoCep(Uteis.removerMascara(Uteis.removerAcentuacao(dadosRemessaVO.getCep())), "prefixo"), 129, 133, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.retornarPrefixoOuSufixoDoCep(Uteis.removerMascara(Uteis.removerAcentuacao(dadosRemessaVO.getCep())), "sufixo"), 134, 136, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getCidade()), 15), 137, 151, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getEstado()) , 2), 152, 153, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 154, 154, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()), 11).replaceAll(" ", ""), 155, 169, "0", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNomeSacado().toString())), 170, 199, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 200, 209, " ", false, false); // Nosso Nº no Banco Correspondente - C032
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 210, 212, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 213, 240, " ", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }

    public void gerarDetalheSegmentoR(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleRemessaContaReceberVO dadosRemessaVO, String codMovRemessa) throws Exception {
    	String linha = "";
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000", 4, 7, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental()), 9, 13, "0", false, false); // Nº Sequencial do Registro no Lote - G038
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "R", 14, 14, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 15, 15, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, codMovRemessa, 16, 17, "0", false, false); // Código de Movimento Remessa - C004
     	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 18, 18, "0", false, false); 
    	
		if (dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
   
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto2(), "ddMMyyyy"), 19, 26, " ", false, false);
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite2(), 2)))), 13), 27, 41, "0", false, false);  
         	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 42, 42, "0", false, false); // Código do Desconto 1 - C021
		} else {
			// verifica se tem terceiro desconto
			if (dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
	        	
	            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto3(), "ddMMyyyy"), 19, 26, " ", false, false);
	            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite3(), 2)))), 13), 27, 41, "0", false, false);
	        	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 42, 42, "0", false, false); // Código do Desconto 1 - C02
	            dadosRemessaVO.setValorDescontoDataLimite3(0.0);
			} else {
				linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 19, 65, "0", false, false);
			}
		}
		
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 66, 66, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 67, 74, "0", false, false);
        //JUROS DE 1 DIA DE ATRASO
        Double juro = 0.01;
        Double valorTitulo = dadosRemessaVO.getValorComAcrescimo();
        String multaStr = "000000000000200";
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(multaStr)), 15), 75, 89, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 90, 99, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 100, 139, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 140, 199, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 200, 207, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 208, 215, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 216, 216, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 217, 228, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 229, 230, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 231, 231, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 232, 240, " ", false, false);
    	linha += "\r";
    	cmd.adicionarLinhaComando(linha, 0);
    }

    public void gerarDetalheSegmentoY(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleRemessaContaReceberVO dadosRemessaVO, String codMovRemessa) throws Exception {
    	String linha = "";
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental()), 9, 13, "0", false, false); // Nº Sequencial do Registro no Lote - G038
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "Y", 14, 14, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 15, 15, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, codMovRemessa, 16, 17, "0", false, false); // Código de Movimento Remessa - C004
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 18, 19, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 20, 20, " ", false, false);    	
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).trim()), 11).replaceAll(" ", ""), 21, 35, "0", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getNomeSacado()), 40), 36, 75, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getLogradouro()), 40), 76, 115, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getBairro()), 15), 116, 130, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(Uteis.removerAcentuacao(dadosRemessaVO.getCep())), 131, 138, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getCidade()), 15), 139, 153, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getEstado()) , 2), 154, 155, " ", true, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 156, 240, " ", false, false);
    	linha += "\r";
    	cmd.adicionarLinhaComando(linha, 0);
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarGeracaoDetalhe(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        try {
            if (!listaDadosRemessaVOs.isEmpty()) {
                Iterator i = listaDadosRemessaVOs.iterator();
                while (i.hasNext()) {
                    ControleRemessaContaReceberVO dadosRemessaVO = (ControleRemessaContaReceberVO) i.next();
                    if (dadosRemessaVO.getValorComAcrescimo() > 0) {
	                    if (dadosRemessaVO.getApresentarArquivoRemessa()) {
	                    	if (!dadosRemessaVO.getBaixarConta()) {
		                        gerarDetalheSegmentoP(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO, "01");
		                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
		                        gerarDetalheSegmentoQ(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO, "01");                        
		                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
//		                        gerarDetalheSegmentoR(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO, "01");
//		                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
//		                        gerarDetalheSegmentoY(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO, "01");
		                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
		                    } else {
		                        gerarDetalheSegmentoP(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO, "02");
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


	@Override
	public void processarArquivoRetorno(ControleRemessaVO controleRemessaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		try {
            File arquivo = controleRemessaVO.getArquivo(caminho);
            BufferedReader reader = new BufferedReader(new FileReader(arquivo));
            String linha;
            int registro = 0;
            String codSegmento = "";
            Integer contador = 0;
            while ((linha = reader.readLine()) != null) {
                if (linha.equals("")) {
                    continue;
                }
                registro = Uteis.getValorInteiro(linha.substring(7, 8));


                switch (registro) {
                    case 0:
                        processarHeaderArquivoRetorno(linha, controleRemessaVO.getRegistroHeaderRetornoVO());
                        break;
                    case 3:
                        codSegmento = (linha.substring(13, 14));
                        if (codSegmento.equals("T")) {
                        	RegistroDetalheVO registroDetalhe = new RegistroDetalheVO();
                        	processarRegistroDetalheSegmentoT(linha, registroDetalhe);
                        	controleRemessaVO.getRegistroDetalheRetornoVOs().add(registroDetalhe);
                        }
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

	private void processarHeaderArquivoRetorno(String linha, RegistroHeaderVO header) throws Exception {
        header.setCodigoBanco(linha.substring(0, 3));
        header.setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
        header.setTipoInscricaoEmpresa(Uteis.getValorInteiro(linha.substring(17,18)));
        header.setNumeroInscricaoEmpresa(Uteis.getValorLong(linha.substring(18, 32)));
        header.setNumeroAgencia(Uteis.getValorInteiro(linha.substring(53, 57)));
        header.setDigitoAgencia((linha.substring(57, 58)));
        header.setNumeroConta(Uteis.getValorInteiro(linha.substring(65, 70)));
       
        header.setNomeEmpresa(linha.substring(72, 102));
        header.setNomeBanco(linha.substring(102, 132));
        header.setCodigoRemessaRetorno(Uteis.getValorInteiro(linha.substring(142, 143)));
        header.setDataGeracaoArquivo(Uteis.getData(linha.substring(143, 151), PATTERN));
        header.setNumeroSequencialArquivo(Uteis.getValorInteiro(linha.substring(157, 163)));
        header.setNumeroVersaoArquivo(Uteis.getValorInteiro(linha.substring(163, 166)));
		header.setLinhaHeader(linha);
    }
	
	private void processarRegistroDetalheSegmentoT(String linha, RegistroDetalheVO detalhe) throws Exception {
        detalhe.setCodigoBanco(linha.substring(0, 3));
        detalhe.setLoteServico(Uteis.getValorInteiro(linha.substring(3, 7)));
        detalhe.setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
        detalhe.setNumeroSequencialRegistroLote(Uteis.getValorInteiro(linha.substring(8, 13)));
        detalhe.setCodigoSeguimentoRegistroDetalhe(linha.substring(13, 14));     
        detalhe.setIdentificacaoOcorrencia(Uteis.getValorInteiro(linha.substring(15, 17)));
        detalhe.setCedenteNumeroAgencia(linha.substring(18, 22));
        detalhe.setCedenteNumeroConta(linha.substring(30,35));       
        detalhe.setCodigoCarteira(Uteis.getValorInteiro(linha.substring(53, 54)));
        detalhe.setNumeroDocumentoCobranca(linha.substring(58, 68));
        detalhe.setDataVencimentoTitulo(Uteis.getData(linha.substring(73, 81), PATTERN));
        detalhe.setValorNominalTitulo(Uteis.getValorDoubleComCasasDecimais(linha.substring(81, 96)));
        //detalhe.setCodigoBanco(Uteis.getValorInteiro(linha.substring(92, 95)));
        detalhe.setIdentificacaoTituloEmpresa(linha.substring(106, 130));
        detalhe.setSacadoTipoInscricao(Uteis.getValorInteiro(linha.substring(132, 133)));
        detalhe.setSacadoNumeroInscricaoControle(linha.substring(133, 148));
        detalhe.setSacadoNome(linha.substring(148, 178));
        detalhe.setValorDespesas(Uteis.getValorDoubleComCasasDecimais(linha.substring(198, 213)));
       // detalhe.setMotivoRegeicao(linha.substring(208, 210));
     
    }
	
	private RegistroDetalheVO processarRegistroDetalheSegmentoU(String linha, RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
        detalhe.setJurosMora(Uteis.getValorDoubleComCasasDecimais(linha.substring(17, 32)));
        detalhe.setValorDesconto(Uteis.getValorDoubleComCasasDecimais(linha.substring(32, 47)));
        detalhe.setValorAbatimento(Uteis.getValorDoubleComCasasDecimais(linha.substring(47, 62)));
        detalhe.setValorIOF(Uteis.getValorDoubleComCasasDecimais(linha.substring(62, 77)));
        if(linha.substring(15, 17).equals("04") || linha.substring(15, 17).equals("05") || linha.substring(15, 17).equals("06") || linha.substring(15, 17).equals("08") || linha.substring(15, 17).equals("09") || linha.substring(15, 17).equals("17") ) {
        	detalhe.setValorPago(Uteis.getValorDoubleComCasasDecimais(linha.substring(77, 92)));
        }else {
        	detalhe.setValorPago(0.0);
        }
        detalhe.setValorLiquido(Uteis.getValorDoubleComCasasDecimais(linha.substring(92, 107)));
        detalhe.setValorOutrasDespesas(Uteis.getValorDoubleComCasasDecimais(linha.substring(107, 122)));
        detalhe.setValorOutrosCreditos(Uteis.getValorDoubleComCasasDecimais(linha.substring(122, 137)));
        detalhe.setDataOcorrencia(Uteis.getData(linha.substring(137, 145), PATTERN));
        detalhe.setDataCredito(Uteis.getData(linha.substring(145, 153), PATTERN));
        return detalhe;
    }	
	
	private void processarRegistroTrailler(String linha, RegistroTrailerVO trailler) {
        trailler.setCodigoBanco(linha.substring(0, 3));
        trailler.setNumeroLote(Uteis.getValorInteiro(linha.substring(3, 7)));
        trailler.setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
        trailler.setQuantidadeLote(Uteis.getValorInteiro(linha.substring(17, 23)));
        trailler.setQuantidadeRegistro(Uteis.getValorInteiro(linha.substring(23, 29)));
    }
}
