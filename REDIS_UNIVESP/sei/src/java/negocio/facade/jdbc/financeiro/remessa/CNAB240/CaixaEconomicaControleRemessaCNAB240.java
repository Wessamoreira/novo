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
import negocio.comuns.financeiro.DadosRemessaVO;
import negocio.comuns.financeiro.RegistroDetalheVO;
import negocio.comuns.financeiro.RegistroHeaderVO;
import negocio.comuns.financeiro.RegistroTrailerVO;
import negocio.comuns.financeiro.enumerador.SituacaoRegistroDetalheEnum;
import negocio.comuns.utilitarias.Comando;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.EditorOC;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.remessa.ControleRemessaCNAB240LayoutInterfaceFacade;

public class CaixaEconomicaControleRemessaCNAB240 extends ControleAcesso implements ControleRemessaCNAB240LayoutInterfaceFacade {

	private static final long serialVersionUID = 1L;

	@Override
	public EditorOC executarGeracaoDadosArquivoRemessa(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		EditorOC editorOC = new EditorOC();
        Comando cmd = new Comando();
        controleRemessaVO.setNumeroIncremental(1);
        controleRemessaVO.setIncrementalMX(getFacadeFactory().getControleRemessaMXFacade().consultarIncrementalPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), usuario));
        gerarHeaderArquivo(editorOC, cmd, controleRemessaVO, controleRemessaVO.getUnidadeEnsinoVO(), configuracaoFinanceiroVO, usuario);
        gerarHeaderLote(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO);
        executarGeracaoDetalhe(listaDadosRemessaVOs, editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, usuario);
        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
        gerarTrailerLote(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO);
        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 2);
        gerarTrailerArquivo(editorOC, cmd, controleRemessaVO);
        editorOC.adicionarComando(cmd);
        getFacadeFactory().getControleRemessaMXFacade().alterarIncrementalPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), controleRemessaVO.getIncrementalMX() + 1, null, usuario);
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
                    	/*if (!dadosRemessaVO.getBaixarConta()) {*/
                    		String tipoOcorrenciaContaRemessa =   dadosRemessaVO.getBaixarConta() ? "02" : "01";
                    		controleRemessaVO.setNumeroIncrementalTitulos(controleRemessaVO.getNumeroIncrementalTitulos()+ 1);
                    		
                    		gerarDetalheSegmentoP(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO, tipoOcorrenciaContaRemessa);
	                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
	                        gerarDetalheSegmentoQ(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO, tipoOcorrenciaContaRemessa);
	                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
	                        gerarDetalheSegmentoR(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO, tipoOcorrenciaContaRemessa);
	                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
	                        if(!dadosRemessaVO.getBaixarConta()) {	                        	
	                            controleRemessaVO.setValorTotalRemessa(controleRemessaVO.getValorTotalRemessa() + dadosRemessaVO.getValor());
	                        }
                    	/*}else{
                    		gerarDetalheSegmentoP(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO, "02");
	                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
                    	}*/
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
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000", 4, 7, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 9, 17, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 18, 18, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(unidadeEnsinoVO.getCNPJ()), 19, 32, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 33, 52, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), 53, 57, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 58, 58, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCodigoCedente(), 59, 65, "0", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 66, 71, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 72, 72, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(unidadeEnsinoVO.getRazaoSocial()), 30), 73, 102, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "CAIXA ECONOMICA FEDERAL", 103, 132, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 133, 142, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 143, 143, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyyyy"), 144, 151, " ", false, false);     
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.gethoraHHMMSS(controleRemessaVO.getDataGeracao()), 152, 157, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getIncrementalMX()), 6), 158, 163, " ", false, false); // Sequência (NSA) - *G018
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "101", 164, 166, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 167, 171, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 172, 191, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "REMESSA-PRODUCAO", 192, 211, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 212, 215, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 216, 240, " ", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gerarTrailerArquivo(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9999", 4, 7, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 9, 17, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000001", 18, 23, "0", false, false); // Quantidade de Lotes do Arquivo - G049
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 6), 24, 29, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 30, 35, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 36, 240, " ", false, false);
        cmd.adicionarLinhaComando(linha, 0);
    }
    
    public void gerarHeaderLote(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
    	String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "R", 9, 9, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 10, 11, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 12, 13, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "060", 14, 16, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 17, 17, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 18, 18, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 19, 33, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCodigoCedente(), 34, 39, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 40, 53, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), 54, 58, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 59, 59, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCodigoCedente(), 60, 65, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 66, 72, "0", false, false); // Código do Modelo Personalizado - C078
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 73, 73, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(controleRemessaVO.getUnidadeEnsinoVO().getRazaoSocial()), 30), 74, 103, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 104, 143, " ", false, false); // Mensagem 1 - C073
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 144, 183, " ", false, false); // Mensagem 2 - C073
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(controleRemessaVO.getIncrementalMX().toString(), 8), 184, 191, "0", false, false); // Número Remessa/Retorno - G079 
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyyyy"), 192, 199, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 200,207 , "0",false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 208, 240, " ", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }
    
    public void gerarTrailerLote(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
    	String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "5", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 9, 17, " ", false, false); // Uso Exclusivo FEBRABAN/CNAB
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 6), 18, 23, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncrementalTitulos()), 6), 24, 29, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(controleRemessaVO.getValorTotalRemessa())), 17), 30, 46, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 47, 52, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 53, 69, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 70, 75, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 76, 92, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 93, 123, " ", false, false); // Uso Exclusivo FEBRABAN/CNAB
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 124, 240, " ", false, false); // Uso Exclusivo FEBRABAN/CNAB
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }
    
    public void gerarDetalheSegmentoP(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleRemessaContaReceberVO dadosRemessaVO, String tipoOcorrenciaContaRemessa) throws Exception {
    	String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 5), 9, 13, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "P", 14, 14, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 15, 15, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, tipoOcorrenciaContaRemessa, 16, 17, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), 18, 22, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 23, 23, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCodigoCedente(), 24, 29, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 30, 40, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "14", 41, 42, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero(), 43, 57, "0", false, false); 
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 58, 58, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCarteira(), 59, 59, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 60, 60, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 61, 61, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 62, 62, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero(), 63, 73, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 74, 77, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 78, 85, " ", false, false);

        if (controleRemessaVO.getContaCorrenteVO().getCarteiraRegistrada() && controleRemessaVO.getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
	        //VALOR DO TITULO
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorBaseComAcrescimo())), 13), 86, 100, "0", false, false);
        } else {
	        //VALOR DO TITULO
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorComAcrescimo())), 13), 86, 100, "0", false, false);
        }
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 101, 105, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 106, 106, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "21", 107, 108, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 109, 109, " ", false, false);
        if (dadosRemessaVO.getDataEmissao() == null) {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyyyy"), 110, 117, " ", false, false);
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataEmissao(), "ddMMyyyy"), 110, 117, " ", false, false);
        }
        
      //JUROS DE 1 DIA DE ATRASO
        Double juro = 0.01;
        Double valorTitulo = Uteis.arrendondarForcando2CadasDecimais(configuracaoFinanceiroVO.getCobrarJuroMultaSobreValorCheioConta() ? dadosRemessaVO.getValorComAcrescimo() : dadosRemessaVO.getValor()-dadosRemessaVO.getValorAbatimento());
        Double juroFinal = (valorTitulo * juro) / 30;
        if(juroFinal < 0.01) {
        	juroFinal = 0.01 ;
        }
        String juroStr = "";
        if (juroFinal.toString().length() > 4) {
        	juroStr = (juroFinal.toString()).substring(0, 4);
        } else {
        	juroStr = juroFinal.toString();
        }
        Double juroDouble = new Double(juroStr);
        if (juroDouble > 0) {
//          linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 118, 118, " ", false, false); // Código do Juros de Mora - C018
//          linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 119, 126, " ", false, false); // Data do Juros de Mora - C019
//          linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(juroStr)), 13), 127, 141, "0", false, false);
          linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 118, 118, " ", false, false); // Código do Juros de Mora - C018
          linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(Uteis.obterDataAvancada(dadosRemessaVO.getDataVencimento(),1),"ddMMyyyy"), 119, 126, " ", false, false); // Data do Juros de Mora - C019
          linha = editorOC.adicionarCampoLinhaVersao2(linha, "100", 127, 141, "0", false, false);
      } else {
      	linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 118, 118, " ", false, false); // Código do Juros de Mora - C018
          linha = editorOC.adicionarCampoLinhaVersao2(linha, "00000000", 119, 126, " ", false, false); // Data do Juros de Mora - C019
          linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 127, 141, "0", false, false);
      } 
        
        
        

        
        
        
        
        
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, this.primeiroFaixaDesconto(controleRemessaVO, dadosRemessaVO, "numero"), 142, 142, "0", false, false); // Código do Desconto 1 - C021
        linha = editorOC.adicionarCampoLinhaVersao2(linha, this.primeiroFaixaDesconto(controleRemessaVO, dadosRemessaVO, "data"), 143, 150, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, this.primeiroFaixaDesconto(controleRemessaVO, dadosRemessaVO, "valor"), 151, 165, "0", false, false);        
//        
//        ////////////////////////////////////// 
//        if (dadosRemessaVO.getValorDescontoDataLimite() != 0  && 
//        		(dadosRemessaVO.getDataLimiteConcessaoDesconto() == null || 
//        			(dadosRemessaVO.getDataLimiteConcessaoDesconto()!= null && 
//        				//dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >=0))) {
//        				(dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >0 
//            						|| Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto()).equals(Uteis.getData(controleRemessaVO.getDataGeracao())))))) {
//        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 142, 142, " ", false, false); // Código do Desconto 1 - C021        	
//        } else {
//        	// desconto 2 preenchido ai joga aqui....
//        	if (dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
//            	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 142, 142, " ", false, false); // Código do Desconto 1 - C021        	      		
//        	} else {
//        		if (dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
//        			linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 142, 142, " ", false, false); // Código do Desconto 1 - C021
//        		} else {
//        			linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 142, 142, " ", false, false); // Código do Desconto 1 - C021
//        		}
//        	}        	
//        }
//        if (dadosRemessaVO.getValorDescontoDataLimite() != 0 
//        		&& (dadosRemessaVO.getDataLimiteConcessaoDesconto() == null || 
//        			(dadosRemessaVO.getDataLimiteConcessaoDesconto() != null && 
//        				(dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >0 
//        						|| Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto()).equals(Uteis.getData(controleRemessaVO.getDataGeracao()))
//        				)
//        			)
//        			)
//        	) {		
////        if (dadosRemessaVO.getValorDescontoDataLimite() != 0 && dadosRemessaVO.getDataLimiteConcessaoDesconto()!= null && dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >=0) {
//            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto(), "ddMMyyyy"), 143, 150, " ", false, false);
//        } else {
//        	if (dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
//        		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto2(), "ddMMyyyy"), 143, 150, " ", false, false);        		
//        	} else {
//        		if (dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
//        			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto3(), "ddMMyyyy"), 143, 150, " ", false, false);
//        		} else {
//        			linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 143, 150, "0", false, false);        			
//        		}
//        	}
//            
//        }
//        if (dadosRemessaVO.getValorDescontoDataLimite() != 0 
//        		&& (dadosRemessaVO.getDataLimiteConcessaoDesconto() == null || 
//        			(dadosRemessaVO.getDataLimiteConcessaoDesconto() != null && 
//        				(dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >0 
//        						|| Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto()).equals(Uteis.getData(controleRemessaVO.getDataGeracao()))
//        				)
//        			)
//        			)
//        	) {
////        if (dadosRemessaVO.getValorDescontoDataLimite() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto()!= null && dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >=0))) {
//            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite(), 2)))), 13), 151, 165, "0", false, false);                    
//        } else {
//        	if (dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
//        		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite(), 2)))), 13), 151, 165, "0", false, false);
//        	} else {
//        		if (dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
//        			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite(), 2)))), 13), 151, 165, "0", false, false);
//        		} else {
//        			linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 151, 165, "0", false, false);        			
//        		}
//        	}
//        }
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 166, 180, "0", false, false);

        // ABATIMENTO
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
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero(), 196, 220, " ", true, false);
        if (controleRemessaVO.getContaCorrenteVO().getHabilitarProtestoBoleto()) {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 221, 221, "0", false, false); // Código para Protesto - C026
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getQtdDiasProtestoBoleto_Str(), 222, 223, "0", false, false); // Número de Dias para Protesto - C027
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 221, 221, "0", false, false); // Código para Protesto - C026
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 222, 223, "0", false, false); // Número de Dias para Protesto - C027
        }
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 224, 224, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getQtdDiasBaixaAutTitulo().toString(), 225, 227, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "09", 228, 229, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 230, 239, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 240, 240, " ", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }
    
//    public void primeiroValor() {
//    	if (primeiro is not null) {
//    		return 
//    	}
//    	if (segundo is not null) {
//    		return
//    	}
//    	if (terceiro is not null) {
//    		return
//    	}
//    }
//	
//    
//    public void segundoValor() {
//    	if (primeiro is not null && segundo is not null) {
//    		return
//    	}
//    	if (primeiro is null and segundo is not null and terceiro is not null) {
//    		return 
//    	}
//    	
//    }
//    
//    public void terceiroValor() {
//    	if (primeiro is not null and segundo is not null and terceiro is not null) {
//    		return
//    	}
//    }    
    
    public String primeiroFaixaDesconto(ControleRemessaVO controleRemessaVO, ControleRemessaContaReceberVO dadosRemessaVO, String parametro) {
		if (parametro.equals("numero")) {
	        if (dadosRemessaVO.getValorDescontoDataLimite() != 0  && (dadosRemessaVO.getDataLimiteConcessaoDesconto() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto()!= null && (dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >0 || Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto()).equals(Uteis.getData(controleRemessaVO.getDataGeracao())))))) {
				return "1"; 
			}
	        if (dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
				return "1";
			}
	        if (dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {										
				return "1";
			}
	        return "0";
		} else if (parametro.equals("data")) {
	        if (dadosRemessaVO.getValorDescontoDataLimite() != 0  && (dadosRemessaVO.getDataLimiteConcessaoDesconto() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto()!= null && (dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >0 || Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto()).equals(Uteis.getData(controleRemessaVO.getDataGeracao())))))) {
				return Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto(), "ddMMyyyy"); 
			}
	        if (dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
				return Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto2(), "ddMMyyyy");
			}
	        if (dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {										
				return Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto3(), "ddMMyyyy");
			}		
			return "";
		} else if (parametro.equals("valor")) {
	        if (dadosRemessaVO.getValorDescontoDataLimite() != 0  && (dadosRemessaVO.getDataLimiteConcessaoDesconto() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto()!= null && (dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >0 || Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto()).equals(Uteis.getData(controleRemessaVO.getDataGeracao())))))) {
				return Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite(), 2)))), 13); 
			}
	        if (dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
				return Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite2(), 2)))), 13);
			}
	        if (dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {										
				return Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite3(), 3)))), 13);
			}		
			return "";
		}
		return "";
	}

    public String segundaFaixaDesconto(ControleRemessaVO controleRemessaVO, ControleRemessaContaReceberVO dadosRemessaVO, String parametro) {
		if (parametro.equals("numero")) {
	        if ((dadosRemessaVO.getValorDescontoDataLimite() != 0  && (dadosRemessaVO.getDataLimiteConcessaoDesconto() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto()!= null && (dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >0 || Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto()).equals(Uteis.getData(controleRemessaVO.getDataGeracao())))))) && 
	        	(dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0)))) {
				return "1"; 
			}
	        if ((dadosRemessaVO.getValorDescontoDataLimite() != 0  || dadosRemessaVO.getDataLimiteConcessaoDesconto() == null) && 
	        	(dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) && 
	        	(dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0)))) {										
				return "1";
			}
	        return "0";
		} else if (parametro.equals("data")) {
			if ((dadosRemessaVO.getValorDescontoDataLimite() != 0  && (dadosRemessaVO.getDataLimiteConcessaoDesconto() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto()!= null && (dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >0 || Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto()).equals(Uteis.getData(controleRemessaVO.getDataGeracao())))))) && 
	        	(dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0)))) {
				return Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto2(), "ddMMyyyy"); 
			}
			if ((dadosRemessaVO.getValorDescontoDataLimite() != 0  || dadosRemessaVO.getDataLimiteConcessaoDesconto() == null) && 
	        	(dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) && 
	        	(dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0)))) {										
				return Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto3(), "ddMMyyyy");
			}
			return "";
		} else if (parametro.equals("valor")) {
			if ((dadosRemessaVO.getValorDescontoDataLimite() != 0  && (dadosRemessaVO.getDataLimiteConcessaoDesconto() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto()!= null && (dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >0 || Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto()).equals(Uteis.getData(controleRemessaVO.getDataGeracao())))))) && 
	        	(dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0)))) {
				return Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite2(), 2)))), 13); 
			}
			if ((dadosRemessaVO.getValorDescontoDataLimite() != 0  || dadosRemessaVO.getDataLimiteConcessaoDesconto() == null) && 
	        	(dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) && 
	        	(dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0)))) {										
				return Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite3(), 2)))), 13);
			}
			return "";
		}
		return "";
	}    

    public String terceiraFaixaDesconto(ControleRemessaVO controleRemessaVO, ControleRemessaContaReceberVO dadosRemessaVO, String parametro) {
		if (parametro.equals("numero")) {
	        if ((dadosRemessaVO.getValorDescontoDataLimite() != 0  && (dadosRemessaVO.getDataLimiteConcessaoDesconto() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto()!= null && (dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >0 || Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto()).equals(Uteis.getData(controleRemessaVO.getDataGeracao())))))) && 
	        	(dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) && 
	        	(dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0)))) {										
				return "1";
			}
	        return "0";
		} else if (parametro.equals("data")) {
	        if ((dadosRemessaVO.getValorDescontoDataLimite() != 0  && (dadosRemessaVO.getDataLimiteConcessaoDesconto() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto()!= null && (dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >0 || Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto()).equals(Uteis.getData(controleRemessaVO.getDataGeracao())))))) && 
	        	(dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) && 
	        	(dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0)))) {										
				return Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto3(), "ddMMyyyy");
			}
			return "";
		} else if (parametro.equals("valor")) {
	        if ((dadosRemessaVO.getValorDescontoDataLimite() != 0  && (dadosRemessaVO.getDataLimiteConcessaoDesconto() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto()!= null && (dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >0 || Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto()).equals(Uteis.getData(controleRemessaVO.getDataGeracao())))))) && 
	        	(dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) && 
	        	(dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0)))) {										
				return Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite3(), 2)))), 13);
			}
			return "";
		}
		return "";
	}    
    
    public void gerarDetalheSegmentoQ(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleRemessaContaReceberVO dadosRemessaVO, String tipoOcorrenciaContaRemessa) throws Exception {
    	String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 5), 9, 13, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "Q", 14, 14, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 15, 15, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, tipoOcorrenciaContaRemessa, 16, 17, " ", false, false); // Código de Movimento Remessa - C004
        
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
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getNomeSacado()), 40), 34, 73, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getLogradouro()), 40), 74, 113, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getBairro()), 15), 114, 128, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(Uteis.removerAcentuacao(dadosRemessaVO.getCep())), 129, 136, " ", false, false);
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.retornarPrefixoOuSufixoDoCep(dadosRemessaVO.getCep(), "prefixo"), 129, 133, " ", false, false);
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.retornarPrefixoOuSufixoDoCep(dadosRemessaVO.getCep(), "sufixo"), 134, 136, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getCidade().toUpperCase()), 15), 137, 151, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getEstado()), 2), 152, 153, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 154, 154, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 155, 169, "0", false, false);//"", 155, 169, "0", false, false); // Número de Inscrição - G006
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 170, 209, " ", false, false); 
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 210, 212, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 213, 232, " ", false, false); // Nosso Nº no Banco Correspondente - C032
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 233, 240, " ", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }

    public void gerarDetalheSegmentoR(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleRemessaContaReceberVO dadosRemessaVO, String tipoOcorrenciaContaRemessa) throws Exception {
    	String linha = "";
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 5), 9, 13, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "R", 14, 14, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 15, 15, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, tipoOcorrenciaContaRemessa, 16, 17, " ", false, false); // Código de Movimento Remessa - C004

    	linha = editorOC.adicionarCampoLinhaVersao2(linha, this.segundaFaixaDesconto(controleRemessaVO, dadosRemessaVO, "numero"), 18, 18, "0", false, false); // Código do Desconto 1 - C021
        linha = editorOC.adicionarCampoLinhaVersao2(linha, this.segundaFaixaDesconto(controleRemessaVO, dadosRemessaVO, "data"), 19, 26, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, this.segundaFaixaDesconto(controleRemessaVO, dadosRemessaVO, "valor"), 27, 41, "0", false, false);            

    	linha = editorOC.adicionarCampoLinhaVersao2(linha, this.terceiraFaixaDesconto(controleRemessaVO, dadosRemessaVO, "numero"), 42, 42, "0", false, false); // Código do Desconto 1 - C021
        linha = editorOC.adicionarCampoLinhaVersao2(linha, this.terceiraFaixaDesconto(controleRemessaVO, dadosRemessaVO, "data"), 43, 50, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, this.terceiraFaixaDesconto(controleRemessaVO, dadosRemessaVO, "valor"), 51, 65, "0", false, false);            

    	
//    	if (dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
//        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 18, 18, "0", false, false); // Código do Desconto 1 - C021
//            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto2(), "ddMMyyyy"), 19, 26, " ", false, false);
//            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite2(), 2)))), 13), 27, 41, "0", false, false);            
//		} else {
//			// verifica se tem terceiro desconto
//			if (dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
//	        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 18, 18, "0", false, false); // Código do Desconto 1 - C021
//	            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto3(), "ddMMyyyy"), 19, 26, " ", false, false);
//	            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite3(), 2)))), 13), 27, 41, "0", false, false);
//	            dadosRemessaVO.setValorDescontoDataLimite3(0.0);
//			} else {
//				linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 18, 41, "0", false, false);
//			}
//		}
//		if (dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
//        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 42, 42, "0", false, false); // Código do Desconto 1 - C021
//            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto3(), "ddMMyyyy"), 43, 50, " ", false, false);
//            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite3(), 2)))), 13), 51, 65, "0", false, false);            
//		} else {
//			linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 42, 65, "0", false, false);
//		}
    	
//    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 18, 18, "0", false, false);
//    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 19, 26, "0", false, false);
//    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 27, 41, "0", false, false);    	
//    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 42, 42, "0", false, false);    	
//    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 43, 50, "0", false, false);    	
//    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 51, 65, "0", false, false);    	
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 66, 66, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(Uteis.obterDataAvancada(dadosRemessaVO.getDataVencimento(),1),"ddMMyyyy"), 67, 74, " ", false, false); // Data do Juros de Mora - C019
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "200", 75, 89, "0", false, false); // Juros de Mora por Dia/Taxa - C020    	
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 90, 240, " ", false, false);
    	linha += "\r";
    	cmd.adicionarLinhaComando(linha, 0);
    }
    
    

    public void gerarDetalheSegmentoT(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleRemessaContaReceberVO dadosRemessaVO) throws Exception {
    	String linha = "";
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 5), 9, 13, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "T", 14, 14, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 15, 15, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 16, 17, " ", false, false); // Código de Movimento Remessa - C004
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 18, 23, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCodigoCedente(), 24, 29, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 30, 32, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 33, 35, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 36, 39, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "14" + dadosRemessaVO.getNossoNumero(), 40, 56, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 57, 57, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 58, 58, "0", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNrDocumento(), 59, 69, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 70, 73, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 74, 81, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(dadosRemessaVO.getValorComAcrescimo().toString())), 13), 82, 96, "0", false, false); // Número de Inscrição - G006
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 97, 99, " ", false, false); // Número de Inscrição - G006    	
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), 100, 104, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 105, 105, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero(), 106, 130, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "09", 131, 132, " ", false, false); // Nosso Nº no Banco Correspondente - C032
        
        if ((dadosRemessaVO.getCodigoInscricao() == 0 || dadosRemessaVO.getCodigoInscricao() == 1) || Uteis.removerMascara(dadosRemessaVO.getNumeroInscricao()).length() == 11) {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 133, 133, " ", false, false);
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 133, 133, " ", false, false);
        }        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(dadosRemessaVO.getNumeroInscricao()), 134, 148, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(dadosRemessaVO.getNomeSacado(), 40), 149, 188, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 189, 198, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 199, 213, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 214, 223, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 224, 240, " ", false, false);
    	linha += "\r";
    	cmd.adicionarLinhaComando(linha, 0);
    }

    public void gerarDetalheSegmentoU(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleRemessaContaReceberVO dadosRemessaVO) throws Exception {
    	String linha = "";
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 5), 9, 13, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "U", 14, 14, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 15, 15, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 16, 17, " ", false, false); // Código de Movimento Remessa - C004
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 18, 18, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 19, 33, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 34, 73, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 74, 113, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 114, 128, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 129, 133, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 134, 136, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 137, 151, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 152, 153, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 154, 154, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 155, 169, "0", false, false); // Número de Inscrição - G006
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 170, 209, " ", false, false); 
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 210, 212, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 213, 232, " ", false, false); // Nosso Nº no Banco Correspondente - C032
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 233, 240, " ", false, false);
    	linha += "\r";
    	cmd.adicionarLinhaComando(linha, 0);
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
                        	RegistroDetalheVO registro2 = new RegistroDetalheVO();
                        	processarRegistroDetalheSegmentoT(linha, registro2);
                        	controleRemessaVO.getRegistroDetalheRetornoVOs().add(registro2);

                        } else if (codSegmento.equals("W")) {
                        	controleRemessaVO.getRegistroDetalheRetornoVOs().add(processarRegistroDetalheSegmentoW(linha, new RegistroDetalheVO(), controleRemessaVO, configuracaoFinanceiroVO, usuarioVO));
                        }
                        break;
                    case 9:
//                        processarRegistroTrailler(linha, controleRemessaVO.getRegistroTrailerRetornoVO());
                        break;
                }
            }
            reader = null;
        } catch (StringIndexOutOfBoundsException e) {
            throw new ConsistirException("O arquivo selecionado é inválido pois não possui a quantidade adequada de caracteres. Detalhe: " + e.getMessage());
        }		
	}
	
	private void processarHeaderArquivoRetorno(String linha, RegistroHeaderVO header) throws Exception {
        header.setCodigoBanco(linha.substring(0, 3));
        header.setNumeroLote(Uteis.getValorInteiro(linha.substring(3, 7)));
        header.setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
        header.setTipoInscricaoEmpresa(Uteis.getValorInteiro(linha.substring(17, 18)));
        header.setNumeroInscricaoEmpresa(Uteis.getValorLong(linha.substring(18, 32)));
        header.setNumeroAgencia(Uteis.getValorInteiro(linha.substring(52, 57)));
        header.setDigitoAgencia((linha.substring(57, 58)));
        header.setCodigoConvenioBanco(linha.substring(58, 64));
        header.setNomeEmpresa(linha.substring(72, 102));
        header.setNomeBanco(linha.substring(102, 132));
        header.setCodigoRemessaRetorno(Uteis.getValorInteiro(linha.substring(142, 143)));
        header.setDataGeracaoArquivo(Uteis.getData(linha.substring(143, 151), "ddMMyy"));
        header.setNumeroSequencialArquivo(Uteis.getValorInteiro(linha.substring(157, 163)));
        header.setNumeroVersaoArquivo(Uteis.getValorInteiro(linha.substring(163, 166)));
        header.setSituacaoRemessa(linha.substring(171, 211));
    }
    
	private void processarRegistroDetalheSegmentoT(String linha, RegistroDetalheVO detalhe) throws Exception {
        detalhe.setCodigoBanco(linha.substring(0, 3));
        detalhe.setLoteServico(Uteis.getValorInteiro(linha.substring(3, 7)));
        detalhe.setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
        detalhe.setNumeroSequencialRegistroLote(Uteis.getValorInteiro(linha.substring(8, 13)));
        detalhe.setCodigoSeguimentoRegistroDetalhe(linha.substring(13, 14));
        detalhe.setCodigoMovimentoRemessaRetorno(Uteis.getValorInteiro(linha.substring(15, 17)));
        detalhe.setIdentificacaoOcorrencia(Uteis.getValorInteiro(linha.substring(15, 17)));
		
        detalhe.setCodigoCedenteCodigoConvenioBanco(linha.substring(23, 29));
        detalhe.setIdentificacaoTituloBanco(linha.substring(41, 56));
        detalhe.setIdentificacaoTituloEmpresa(linha.substring(41, 56));
        detalhe.setCodigoCarteira(Uteis.getValorInteiro(linha.substring(57, 58)));
        detalhe.setNumeroDocumentoCobranca(linha.substring(58, 69));
        detalhe.setDataVencimentoTitulo(Uteis.getData(linha.substring(73, 81), "ddMMyy"));
        detalhe.setValorNominalTitulo(Uteis.getValorDoubleComCasasDecimais(linha.substring(81, 96)));
        detalhe.setSacadoBancoCodigo(linha.substring(0, 3));
        detalhe.setCodigoMoeda(Uteis.getValorInteiro(linha.substring(130, 132)));
        detalhe.setSacadoTipoInscricao(Uteis.getValorInteiro(linha.substring(132, 133)));
        detalhe.setSacadoNumeroInscricaoControle(linha.substring(133, 148));
        detalhe.setSacadoNome(linha.substring(148, 188));
        detalhe.setMotivoRegeicao(linha.substring(213, 215));
        if(detalhe.isValidarRegistroConfirmado()) {
        	detalhe.setSituacaoRegistroDetalheEnum(SituacaoRegistroDetalheEnum.CONFIRMADO);	
        }else  if(detalhe.isValidarRegistroRejeitado()) {
        	detalhe.setSituacaoRegistroDetalheEnum(SituacaoRegistroDetalheEnum.REJEITADO);
        }
    }
	
	private RegistroDetalheVO processarRegistroDetalheSegmentoW(String linha, RegistroDetalheVO detalhe, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		String erro = linha.substring(21, 23);
		detalhe.setMotivoRegeicao(linha.substring(28, 30));
		if (detalhe.getMotivoRegeicao().equals("")) {
			detalhe.setMotivoRegeicao(erro);
		}
		try {
			Integer valor = Uteis.getValorInteiro(erro);
			detalhe.setIdentificacaoOcorrencia(valor);
		} catch (Exception e) {
			detalhe.setIdentificacaoOcorrencia(0);						
		}
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