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
import negocio.interfaces.financeiro.remessa.ControleRemessaCNAB240LayoutInterfaceFacade;
import negocio.interfaces.financeiro.remessa.ControleRemessaCNAB400LayoutInterfaceFacade;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



/**
 *
 * @author Otimize-04
 */
public class SicoobCredSaudeControleRemessaCNAB400 extends ControleAcesso implements ControleRemessaCNAB400LayoutInterfaceFacade {

    private static final String PATTERN = "ddMMyy";

    public SicoobCredSaudeControleRemessaCNAB400() {
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public EditorOC executarGeracaoDadosArquivoRemessa(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        EditorOC editorOC = new EditorOC();
        Comando cmd = new Comando();
        double valorTotalTitulo = 0.0;
        int quantidadeDoc = 0;
        controleRemessaVO.setNumeroIncremental(1);
        //Gera o Header
        gerarDadosHeader(editorOC, cmd, controleRemessaVO, controleRemessaVO.getUnidadeEnsinoVO(), configuracaoFinanceiroVO, usuario);
        //Gera o Detalhe
        executarGeracaoDetalhe(listaDadosRemessaVOs, editorOC, cmd, controleRemessaVO.getDataInicio(), controleRemessaVO.getDataFim(), controleRemessaVO, valorTotalTitulo, quantidadeDoc, configuracaoFinanceiroVO, usuario);
        //Gera o Trailler
        controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
        gerarDadosTrailler(editorOC, cmd, controleRemessaVO.getNumeroIncremental(), valorTotalTitulo, quantidadeDoc);
        editorOC.adicionarComando(cmd);
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

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void gerarDadosHeader(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 1, 1, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 2, 2, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "REMESSA", 3, 9, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 10, 11, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "COBRANÇA", 12, 26, " ", true, false);
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia() + controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 27, 31, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getConvenio(), 32, 40, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 41, 46, " ", false, false);
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(unidadeEnsinoVO.getRazaoSocial()), 47, 76, " ", true, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "756", 77, 79, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "BANCOOBCED", 80, 94, " ", true, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyy"), 95, 100, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000001", 101, 107, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 108, 394, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000001", 395, 400, " ", false, false);
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
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 4, 17, "0", false, false);
        //AGÊNCIA MANTENEDORA DA CONTA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getAgencia(), 18, 21, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getDigitoAgencia(), 22, 22, "0", false, false);
        //CONTA MOVIMENTO CEDENTE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getContaCorrente().toString(), 23, 30, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getDigitoContaCorrente(), 31, 31, "0", false, false);
        //NR CONTROLE PARTICIPANTE, CONTROLE CEDENTE        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 32, 37, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 38, 62, " ", false, false);

        //NOSSO NÚMERO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero(), 63, 74, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 75, 76, "0", false, false);
                
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 77, 78, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 79, 81, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 82, 82, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 83, 85, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000", 86, 89, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00000", 90, 95, "0", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 96, 101, "0", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 102, 106, " ", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 107, 108, "0", false, false);        
        //CODIGO DA OCORRENCIA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 109, 110, " ", false, false);
        
        //NÚMERO DO DOCUMENTO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero().toString(), 111, 120, "0", false, false);
        //DATA VENCIMENTO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), PATTERN), 121, 126, "0", false, false);
        //VALOR DO TITULO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorComAcrescimo())), 13), 127, 139, " ", false, false);                
        //CÓDIGO DO BANCO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "756", 140, 142, "0", false, false);
        //AGÊNCIA COBRADORA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getAgencia(), 143, 146, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getDigitoAgencia(), 147, 147, "0", false, false);

        //ESPÉCIE DO TÍTULO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "99", 148, 149, " ", false, false);
        //ACEITE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 150, 150, " ", false, false);
        //DATA DE EMISSÃO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(new Date(), PATTERN), 151, 156, "0", false, false);
        //INSTRUÇÃO 1
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 157, 158, " ", false, false);
        //INSTRUÇÃO 2
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 159, 160, " ", false, false);

        
        //JUROS DE 1 DIA DE ATRASO
//        Double juro = 0.01;
//        Double valorTitulo = dadosRemessaVO.getValorComAcrescimo();
//        Double juroFinal = (valorTitulo * juro) / 30;
//        String juroStr = "";
//        if (juroFinal.toString().length() > 4) {
//        	juroStr = (juroFinal.toString()).substring(0, 4);
//        } else {
//        	juroStr = juroFinal.toString();
//        }
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais("0.01")), 13), 161, 173, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "010000", 161, 166, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "020000", 167, 172, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 173, 173, " ", false, false);

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
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 193, 193, " ", false, false);
        //VALOR DO I.O.F.
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000000", 194, 205, "0", false, false);
        //ABATIMENTO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000000", 206, 218, "0", false, false);
        //CÓDIGO DE INSCRIÇÃO  01=CPF  -  02=CNPJ
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01" , 219, 220, " ", false, false);
        //NÚMERO DE INSCRIÇÃO  CPF OU CNPJ
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(dadosRemessaVO.getNumeroInscricao().toString()).replaceAll(" ", ""), 14), 221, 234, "0", false, false);
        //NOME DO SACADO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(dadosRemessaVO.getNomeSacado().toString()), 235, 274, " ", true, false);
        //LOGRADOURO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(dadosRemessaVO.getLogradouro()), 275, 311, " ", true, false);
        //BAIRRO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(dadosRemessaVO.getBairro()), 312, 326, " ", true, false);
        //CEP
        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentos(Uteis.removeCaractersEspeciais(dadosRemessaVO.getCep()).replaceAll(" ", "")), 327, 334, " ", true, false);
        //CIDADE
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removeCaractersEspeciais2(dadosRemessaVO.getCidade()), 335, 349, " ", true, false);
        //SIGLA DO ESTADO
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(dadosRemessaVO.getEstado()), 350, 351, " ", true, false);
        //SACADOR OU AVALISTA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 352, 391, " ", true, false);
        //BRANCOS
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 392, 393, " ", false, false);
        //DATA DE MORA
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 394, 394, " ", false, false);
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
    public void gerarDadosTrailler(EditorOC editorOC, Comando cmd, int numeroSequencial, double valorTotalTitulo, int quantidadeDocu) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 1, 1, " ", false, false);
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(quantidadeDocu), 2, 7, " ", false, false);
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(valorTotalTitulo), 8, 20, " ", false, false);
//        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 21, 394, " ", false, false);
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

	@Override
	public void processarArquivoRetorno(ControleRemessaVO controleRemessaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		// TODO Auto-generated method stub
		
	}

}