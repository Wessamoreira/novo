/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.financeiro.remessa.CNAB240;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ControleRemessaVO;
import negocio.comuns.financeiro.RegistroDetalheVO;
import negocio.comuns.financeiro.RegistroHeaderVO;
import negocio.comuns.financeiro.RegistroTrailerVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.utilitarias.Comando;
import negocio.comuns.utilitarias.EditorOC;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.remessa.ControleRemessaCNAB240LayoutInterfaceFacade;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author Otimize-04
 */
public class BancoBrasilControleRemessaCNAB240 extends ControleAcesso implements ControleRemessaCNAB240LayoutInterfaceFacade {

	private static final long serialVersionUID = 1L;
	private static final String PATTERN = "ddMMyyyy";

    public BancoBrasilControleRemessaCNAB240() {
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public EditorOC executarGeracaoDadosArquivoRemessa(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        EditorOC editorOC = new EditorOC();
        Comando cmd = new Comando();
        controleRemessaVO.setNumeroIncremental(1);
        controleRemessaVO.setIncrementalMX(getFacadeFactory().getControleRemessaMXFacade().consultarIncrementalPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), usuario));
        //Gera o Header        
        gerarDadosHeader(editorOC, cmd, controleRemessaVO, controleRemessaVO.getUnidadeEnsinoVO(), configuracaoFinanceiroVO, usuario);
        gerarHeaderLote(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO);
        //Gera o Detalhe Segmento P
        executarGeracaoDetalhe(listaDadosRemessaVOs, editorOC, cmd, controleRemessaVO.getDataInicio(), controleRemessaVO.getDataFim(), controleRemessaVO, configuracaoFinanceiroVO, usuario);
        //Gera o Trailler
        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
        gerarTrailerLote(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO);
        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
        gerarDadosTrailler(editorOC, cmd, controleRemessaVO, controleRemessaVO.getNumeroIncremental());

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
                    	if (dadosRemessaVO.getBaixarConta()) {
                            gerarDadosDetalheSegmentoP(editorOC, cmd, dadosRemessaVO, controleRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), "02");
                            controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
                    	} else {
	                        gerarDadosDetalheSegmentoP(editorOC, cmd, dadosRemessaVO, controleRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), "01");
	                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
	                        gerarDadosDetalheSegmentoQ(editorOC, cmd, dadosRemessaVO, controleRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental(), "01");
	                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
	                        gerarDadosDetalheSegmentoR(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO, controleRemessaVO.getNumeroIncremental(), "01");
	                        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
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
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Bancos.BANCO_DO_BRASIL.getNumeroBanco(), 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000", 4, 7, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 9, 17, " ", false, false);
        //Dados da empresa ----------
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 18, 18, " ", true, false);
        //Número de Inscrição da Empresa(CNPJ)
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(Uteis.removeCaractersEspeciais2(unidadeEnsinoVO.getCNPJ())), 19, 32, " ", true, false);
        //Código convenio no Banco
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getConvenio(), 33, 41, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0014", 42, 45, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCarteira(), 46, 47, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCodigoCedente(), 48, 50, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 51, 52, " ", false, false);
        //Agência Mantenedora
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia() + controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 53, 58, "0", false, false);
        //linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 59, 59, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(controleRemessaVO.getContaCorrenteVO().getNumero(), 12), 59, 70, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getDigito(), 71, 71, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 72, 72, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(controleRemessaVO.getUnidadeEnsinoVO().getRazaoSocial()), 73, 102, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "BANCO DO BRASIL S.A.", 103, 132, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 133, 142, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 143, 143, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(new Date(), "ddMMyyyy"), 144, 151, " ", false, false);

        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 152, 157, "0", true, false);
        //Número Remessa
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getArquivoRemessa().getCodigo().toString(), 158, 163, "0", false, false);
        //Data Gravação Remessa
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "083", 164, 166, "0", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 167, 211, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 212, 240, " ", true, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }
    
    public void gerarHeaderLote(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
    	String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Bancos.BANCO_DO_BRASIL.getNumeroBanco(), 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "R", 9, 9, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 10, 11, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 12, 13, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "042", 14, 16, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 17, 17, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 18, 18, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(Uteis.removeCaractersEspeciais2(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ())), 19, 33, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getConvenio(), 34, 42, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0014", 43, 46, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCarteira(), 47, 48, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getCodigoCedente(), 49, 51, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 52, 53, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), 54, 58, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 59, 59, " ", false, false);
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getNumero(), 60, 71, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getDigito(), 72, 72, " ", false, false);
                
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 73, 73, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(controleRemessaVO.getUnidadeEnsinoVO().getRazaoSocial()), 30), 74, 103, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 104, 143, " ", false, false); // Mensagem 1 - C073
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 144, 183, " ", false, false); // Mensagem 2 - C073
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(controleRemessaVO.getIncrementalMX().toString(), 8), 184, 191, "0", false, false); // Número Remessa/Retorno - G079 
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyyyy"), 192, 199, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 200, 240, " ", false, false);
        //linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 208, 240, " ", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }
    
    public void gerarTrailerLote(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) throws Exception {
    	String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Bancos.BANCO_DO_BRASIL.getNumeroBanco(), 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "5", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 9, 17, " ", false, false); // Uso Exclusivo FEBRABAN/CNAB
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental()), 18, 23, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 24, 29, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 30, 240, " ", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }    
    
    public void gerarDadosDetalheSegmentoP(EditorOC editorOC, Comando cmd, ControleRemessaContaReceberVO dadosRemessaVO, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial, String codMovRemessa) throws Exception {
        String linha = "";
      //---------------------DADOS DO CONTROLE E DO SERVIÇO -------------------//
        //BANCO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Bancos.BANCO_DO_BRASIL.getNumeroBanco(), 1, 3, " ", false, false);
        //LOTE DE SERVIÇO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
        //TIPO DE REGISTRO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
        //NÚMERO SEQUENCIAL DO REGISTRO NO LOTE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(numeroSequencial), 5), 9, 13, " ", false, false);
        //CÓD. SEGMENTO DO REGISTRO DETALHE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "P", 14, 14, " ", false, false);
        //USO EXCLUSIVO FEBRABAN
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 15, 15, " ", false, false);

        //CÓDIGO DE MOVIMENTO REMESSA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, codMovRemessa, 16, 17, " ", false, false);

        //---------------------DADOS DA CONTA CORRENTE -------------------//
        //AGÊNCIA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(dadosRemessaVO.getAgencia().toString(), 5), 18, 22, " ", false, false);
        //DÍGITO AGÊNCIA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito().toString(), 23, 23, " ", false, false);
        //NÚMERO CONTA CORRENTE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(dadosRemessaVO.getContaCorrente().toString(), 12), 24, 35, "0", false, false);
        //DÍGITO CONTA CORRENTE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getDigito(), 36, 36, " ", false, false);
        //DÍGITO VERIFICADOR DA AG/CONTA        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 37, 37, " ", false, false);
        //NOSSO NÚMERO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getConvenio() + dadosRemessaVO.getNossoNumero(), 38, 57, " ", true, false);                
        //---------------------CARACTERÍSTICA COBRANÇA -------------------//
        //CÓDIGO CARTEIRA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "7", 58, 58, " ", false, false);
        //FORMA DE CADASTRAMENTO DO TÍTULO NO BANCO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 59, 59, " ", false, false);
        //TIPO DE DOCUMENTO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 60, 60, " ", false, false);
        //IDENTIFICAÇÃO DA EMISSÃO DO BLOQUETO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 61, 61, " ", false, false);
        //IDENTIFICAÇÃO DA DISTRIBUIÇÃO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 62, 62, " ", false, false);

        //NÚMERO DO DOCUMENTO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNrDocumento().toString(), 63, 77, " ", true, false);
        //DATA VENCIMENTO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 78, 85, " ", false, false);
        //VALOR DO TITULO
        if (controleRemessaVO.getContaCorrenteVO().getCarteiraRegistrada() && controleRemessaVO.getContaCorrenteVO().getGerarRemessaSemDescontoAbatido()) {
	        //VALOR DO TITULO
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorBaseComAcrescimo())), 13), 86, 100, "0", false, false);
        } else {
	        //VALOR DO TITULO
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorComAcrescimo())), 13), 86, 100, "0", false, false);
        }        
        //AGÊNCIA COBRADORA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 101, 105, "0", false, false);
        //DÍGITO VERIFICADOR DA AGÊNCIA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 106, 106, "", false, false);
        //ESPÉCIE DO TÍTULO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "04", 107, 108, " ", false, false);
        //ACEITE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 109, 109, " ", false, false);
        //DATA DE EMISSÃO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(new Date(), PATTERN), 110, 117, " ", false, false);

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
            linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 118, 118, " ", false, false); // Código do Juros de Mora - C018
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 119, 126, " ", false, false); // Data do Juros de Mora - C019
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(juroStr)), 13), 127, 141, "0", false, false);
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 118, 118, " ", false, false); // Código do Juros de Mora - C018
            linha = editorOC.adicionarCampoLinhaVersao2(linha, "00000000", 119, 126, " ", false, false); // Data do Juros de Mora - C019
            linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 127, 141, "0", false, false);
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
		//if (dadosRemessaVO.getValorDescontoDataLimite() != 0 && dadosRemessaVO.getDataLimiteConcessaoDesconto() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto().compareTo(controleRemessaVO.getDataGeracao()) >= 0) {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 142, 142, "0", false, false); // Código do Desconto 1 - C021
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto(), "ddMMyyyy"), 143, 150, " ", false, false);
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite(), 2)))), 13), 151, 165, "0", false, false);            
        } else {
    		if (dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
            	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 142, 142, "0", false, false); // Código do Desconto 1 - C021
                linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto2(), "ddMMyyyy"), 143, 150, " ", false, false);
                linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite2(), 2)))), 13), 151, 165, "0", false, false);
                dadosRemessaVO.setValorDescontoDataLimite2(0.0);                
    		} else if (dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
            	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 142, 142, "0", false, false); // Código do Desconto 1 - C021
                linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto3(), "ddMMyyyy"), 143, 150, " ", false, false);
                linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite3(), 2)))), 13), 151, 165, "0", false, false);
                dadosRemessaVO.setValorDescontoDataLimite3(0.0);
    		} else {
            	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 142, 142, "0", false, false); // Código do Desconto 1 - C021
                linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 143, 150, "0", false, false); // Data do Desconto 1 - C022                	
                linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 151, 165, "0", false, false); // Valor/Percentual a ser Concedido - C023
    		}
        }        
        //VALOR DO I.O.F.
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000000000000000", 166, 180, "0", false, false);
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
        
        //IDENTIFICAÇÃO DO TITULO NA EMPRESA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero(), 196, 220, " ", false, false);
        //CODIGO PARA PROTESTO
        if (controleRemessaVO.getContaCorrenteVO().getHabilitarProtestoBoleto()) {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 221, 221, "0", false, false); // Código para Protesto - C026
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getQtdDiasProtestoBoleto_Str(), 222, 223, "0", false, false); // Número de Dias para Protesto - C027
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 221, 221, "0", false, false); // Código para Protesto - C026
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 222, 223, "0", false, false); // Número de Dias para Protesto - C027
        }
        //CÓDIGO PARA BAIXA/DEVOLUÇÃO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 224, 224, " ", false, false);
        //NÚMERO DE DIAS PARA BAIXA/DEVOLUÇÃO
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000", 225, 227, " ", true, false);
        if (controleRemessaVO.getContaCorrenteVO().getQtdDiasBaixaAutTitulo() > 0) {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getQtdDiasBaixaAutTitulo().toString(), 225, 227, "0", false, false);
        } else {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "000", 225, 227, " ", true, false);
        }
        //CÓDIGO DA MOEDA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "09", 228, 229, " ", false, false);
        //NÚMERO DO CONTRATO DA OPERAÇÃO DE CRÉDITO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000", 230, 239, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 240, 240, " ", false, false);
        linha += "\r";
        cmd.adicionarLinhaComando(linha, 0);
    }
    
    public void gerarDadosDetalheSegmentoQ(EditorOC editorOC, Comando cmd, ControleRemessaContaReceberVO dadosRemessaVO, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial, String codMovRemessa) throws Exception {
    	String linha = "";
        //---------------------DADOS DO CONTROLE E DO SERVIÇO -------------------//
          //BANCO
          linha = editorOC.adicionarCampoLinhaVersao2(linha, Bancos.BANCO_DO_BRASIL.getNumeroBanco(), 1, 3, " ", false, false);
          //LOTE DE SERVIÇO
          linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
          //TIPO DE REGISTRO
          linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
          //NÚMERO SEQUENCIAL DO REGISTRO NO LOTE
          linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(numeroSequencial), 5), 9, 13, " ", false, false);
          //CÓD. SEGMENTO DO REGISTRO DETALHE
          linha = editorOC.adicionarCampoLinhaVersao2(linha, "Q", 14, 14, " ", false, false);
          //USO EXCLUSIVO FEBRABAN
          linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 15, 15, " ", false, false);
          //CÓDIGO DE MOVIMENTO REMESSA
          linha = editorOC.adicionarCampoLinhaVersao2(linha, codMovRemessa, 16, 17, " ", false, false);
          
        //---------------------DADOS DO SACADO -------------------//
		// TIPO DE INSCRIÇÃO
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
          //NOME
          linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNomeSacado())), 34, 73, " ", true, false);
          //ENDEREÇO
          linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(dadosRemessaVO.getLogradouro())), 74, 113, " ", true, false);
          //BAIRRO
          linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(dadosRemessaVO.getBairro())), 114, 128, " ", true, false);
          //CEP
          linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(dadosRemessaVO.getCep()).replaceAll(" ", "")), 129, 136, " ", false, false);
          //CIDADE
          linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(Uteis.removeCaractersEspeciais(dadosRemessaVO.getCidade())), 137, 151, " ", true, false);
          //UF
          linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(dadosRemessaVO.getEstado()), 152, 153, " ", false, false);
          
        //---------------------DADOS DO SACADO AVALISTA -------------------//
          //TIPO DE INSCRIÇÃO
          linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 154, 154, " ", false, false);
          //NÚMERO DE INSCRIÇÃO
          linha = editorOC.adicionarCampoLinhaVersao2(linha, "000000000000000", 155, 169, " ", false, false);
          //NOME DO SACADOR AVALISTA
          linha = editorOC.adicionarCampoLinhaVersao2(linha, "                                        ", 170, 209, " ", false, false);

          //BANCO CORRESPONDENTE
          linha = editorOC.adicionarCampoLinhaVersao2(linha, "000", 210, 212, " ", false, false);
          //NOSSO NÚMERO BANCO CORRESPONDENTE
          linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 213, 232, " ", false, false);
          //USO EXCLUSIVO FEBRABAN
          linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 233, 240, " ", false, false);
          linha += "\r";
          cmd.adicionarLinhaComando(linha, 0);

    }

    public void gerarDadosDetalheSegmentoR(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleRemessaContaReceberVO dadosRemessaVO, int numeroSequencial, String codMovRemessa) throws Exception {
    	String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Bancos.BANCO_DO_BRASIL.getNumeroBanco(), 1, 3, " ", false, false);
        //LOTE DE SERVIÇO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
        //TIPO DE REGISTRO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
        //NÚMERO SEQUENCIAL DO REGISTRO NO LOTE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(numeroSequencial), 5), 9, 13, " ", false, false);
        //CÓD. SEGMENTO DO REGISTRO DETALHE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "R", 14, 14, " ", false, false);
        //USO EXCLUSIVO FEBRABAN
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 15, 15, " ", false, false);
        //CÓDIGO DE MOVIMENTO REMESSA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, codMovRemessa, 16, 17, " ", false, false);

        
		if (dadosRemessaVO.getValorDescontoDataLimite2() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto2() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto2() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto2().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 18, 18, "0", false, false); // Código do Desconto 1 - C021
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto2(), "ddMMyyyy"), 19, 26, " ", false, false);
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite2(), 2)))), 13), 27, 41, "0", false, false);            
		} else {
			// verifica se tem terceiro desconto
			if (dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
	        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 18, 18, "0", false, false); // Código do Desconto 1 - C021
	            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto3(), "ddMMyyyy"), 19, 26, " ", false, false);
	            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite3(), 2)))), 13), 27, 41, "0", false, false);
	            dadosRemessaVO.setValorDescontoDataLimite3(0.0);
			} else {
				linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 18, 41, " ", false, false);
			}
		}
		if (dadosRemessaVO.getValorDescontoDataLimite3() != 0 && (dadosRemessaVO.getDataLimiteConcessaoDesconto3() == null || (dadosRemessaVO.getDataLimiteConcessaoDesconto3() != null && dadosRemessaVO.getDataLimiteConcessaoDesconto3().compareTo(controleRemessaVO.getDataGeracao()) >= 0))) {
        	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 42, 42, "0", false, false); // Código do Desconto 1 - C021
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataLimiteConcessaoDesconto3(), "ddMMyyyy"), 43, 50, " ", false, false);
            linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(new Double(Uteis.truncar(dadosRemessaVO.getValorDescontoDataLimite3(), 2)))), 13), 51, 65, "0", false, false);            
		} else {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 42, 65, " ", false, false);
		}
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
    	linha += "\r";
    	cmd.adicionarLinhaComando(linha, 0);
    }

    public void gerarDadosDetalheSegmentoS(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ControleRemessaContaReceberVO dadosRemessaVO, int numeroSequencial) throws Exception {
    	String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Bancos.BANCO_DO_BRASIL.getNumeroBanco(), 1, 3, " ", false, false);
        //LOTE DE SERVIÇO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
        //TIPO DE REGISTRO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
        //NÚMERO SEQUENCIAL DO REGISTRO NO LOTE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(numeroSequencial), 5), 9, 13, " ", false, false);
        //CÓD. SEGMENTO DO REGISTRO DETALHE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "S", 14, 14, " ", false, false);
        //USO EXCLUSIVO FEBRABAN
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 15, 15, " ", false, false);
        //CÓDIGO DE MOVIMENTO REMESSA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 16, 17, " ", false, false);    	
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 18, 18, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 19, 65, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 66, 66, " ", false, false);
    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 67, 240, " ", false, false);
    	//linha += "\r\n";
    	cmd.adicionarLinhaComando(linha, 0);
    }    
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gerarDadosTrailler(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, int numIncremental) throws Exception {
        String linha = "";
        //BANCO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Bancos.BANCO_DO_BRASIL.getNumeroBanco(), 1, 3, " ", false, false);
        //LOTE DE SERVIÇO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9999", 4, 7, " ", false, false);
        //TIPO DE REGISTRO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 9, 17, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 18, 23, "0", false, false); // Quantidade de Lotes do Arquivo - G049
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numIncremental+1), 24, 29, "0", false, false); // Quantidade de Registros do Arquivo - G056        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 30, 240, " ", false, false);
        //linha += "\r";
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
		File arquivo = controleRemessaVO.getArquivo(caminho);
		BufferedReader reader = new BufferedReader(new FileReader(arquivo));
		String linha;
		String registro = "";
		Integer contador = 0;
		while ((linha = reader.readLine()) != null) {
			if (linha.equals("")) {
				continue;
			}
			registro = linha.substring(13, 14);

			switch (registro) {
			case "T":
				RegistroDetalheVO detalhe = processarRegistroDetalhe(linha, new RegistroDetalheVO(), configuracaoFinanceiroVO, usuarioVO);
				controleRemessaVO.getRegistroDetalheRetornoVOs().add(detalhe);
				break;
			}
			contador = contador + 1;
		}
		if (contador == 2) {
			throw new Exception("Não existem registros de contas nesse arquivo de retorno. Entre em contato com o seu banco!");
		}
		
	}
    
	private void processarHeader(String linha, RegistroHeaderVO header) throws Exception {
		header.setIdentificacaoRegistro(Uteis.getValorInteiro(linha.substring(0, 1)));
		header.setTipoRegistro(Uteis.getValorInteiro(linha.substring(1, 2)));
		header.setCodigoServico(Uteis.getValorInteiro(linha.substring(9, 11)));
		header.setNumeroAgencia(Uteis.getValorInteiro(linha.substring(26, 30)));
		header.setDigitoAgencia(linha.substring(30, 31));
		header.setNumeroConta(Uteis.getValorInteiro(linha.substring(31, 38)));
		header.setDigitoConta(linha.substring(38, 39));
		header.setDigitoAgenciaConta(Uteis.getValorInteiro(linha.substring(39, 40)));
		header.setNomeEmpresa(linha.substring(46, 76));
		header.setCodigoBanco(linha.substring(76, 79));
		header.setNomeBanco(linha.substring(79, 94));
		header.setDataGeracaoArquivo(Uteis.getData(linha.substring(94, 100), PATTERN));
		header.setNumeroSequencialRegistro(Uteis.getValorInteiro(linha.substring(100, 107)));
		header.setCodigoConvenioBanco(linha.substring(149, 156));
		header.setNumeroSequencialArquivo(Uteis.getValorInteiro(linha.substring(394, 400)));
	}
	
	private RegistroDetalheVO processarRegistroDetalhe(String linha, RegistroDetalheVO detalhe, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		detalhe.setTipoRegistro(Uteis.getValorInteiro(linha.substring(8, 9)));
		detalhe.setIdentificacaoTituloEmpresa(linha.substring(105, 130));
		detalhe.setTarifaCobranca(Uteis.getValorDoubleComCasasDecimais(linha.substring(198, 213)));
		detalhe.setIdentificacaoOcorrencia(Uteis.getValorInteiro(linha.substring(15, 17)));
        detalhe.setMotivoRegeicao(linha.substring(213, 223));
		return detalhe;
	}
	
}