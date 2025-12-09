/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.financeiro.remessa.CNAB240;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.utilitarias.Comando;
import negocio.comuns.utilitarias.EditorOC;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.remessa.ControleRemessaCNAB240LayoutInterfaceFacade;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;



/**
 *
 * @author Otimize-04
 */
public class SicoobCredSaudeControleRemessaCNAB240 extends ControleAcesso implements ControleRemessaCNAB240LayoutInterfaceFacade {

	private static final String PATTERN = "ddMMyyyy";

	public SicoobCredSaudeControleRemessaCNAB240() {
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public EditorOC executarGeracaoDadosArquivoRemessa(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		EditorOC editorOC = new EditorOC();
		Comando cmd = new Comando();
		double valorTotalTitulo = 0.0;
		int quantidadeDoc = 0;
		controleRemessaVO.setNumeroIncremental(1);
		controleRemessaVO.setIncrementalMX(getFacadeFactory().getControleRemessaMXFacade().consultarIncrementalPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), usuario));		
		// Gera o Header
		// gerarDadosHeader(editorOC, cmd, controleRemessaVO,
		// controleRemessaVO.getUnidadeEnsinoVO(), configuracaoFinanceiroVO,
		// usuario);
		gerarDadosHeaderArquivo(editorOC, cmd, controleRemessaVO, controleRemessaVO.getUnidadeEnsinoVO(), configuracaoFinanceiroVO, usuario);
		gerarDadosHeaderLote(editorOC, cmd, controleRemessaVO, controleRemessaVO.getUnidadeEnsinoVO(), configuracaoFinanceiroVO, usuario);
		// Gera o Detalhe
		executarGeracaoDetalhe(listaDadosRemessaVOs, editorOC, cmd, controleRemessaVO.getDataInicio(), controleRemessaVO.getDataFim(), controleRemessaVO, valorTotalTitulo, quantidadeDoc, configuracaoFinanceiroVO, usuario);
		// Gera o Trailler
		controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() - 1);
		gerarDadosTraillerLote(editorOC, controleRemessaVO, cmd, controleRemessaVO.getNumeroIncremental(), valorTotalTitulo, quantidadeDoc);
		gerarDadosTraillerArquivo(editorOC, cmd, controleRemessaVO.getNumeroIncremental(), valorTotalTitulo, quantidadeDoc);
		getFacadeFactory().getControleRemessaMXFacade().alterarIncrementalPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), controleRemessaVO.getIncrementalMX() + 1, null, usuario);		
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
						gerarDadosSegmentoP240(editorOC, cmd, controleRemessaVO, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental());
						controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
						gerarDadosSegmentoQ240(editorOC, cmd, controleRemessaVO, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental());
						controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
						gerarDadosSegmentoR240(editorOC, cmd, controleRemessaVO, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental());
						controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
						gerarDadosSegmentoS240(editorOC, cmd, controleRemessaVO, dadosRemessaVO, configuracaoFinanceiroVO, controleRemessaVO.getNumeroIncremental());
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
	public void gerarDadosHeaderArquivo(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		String linha = "";
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "756", 1, 3, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000", 4, 7, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 8, 8, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 9, 17, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 18, 18, " ", false, false);
		// numero inscricao
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(unidadeEnsinoVO.getCNPJ()), 19, 32, "0", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 33, 52, " ", true, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia() + controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 53, 58, "0", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getNumero() + controleRemessaVO.getContaCorrenteVO().getDigito(), 59, 71, "0", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 72, 72, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(unidadeEnsinoVO.getRazaoSocial()), 73, 102, " ", true, false);

		linha = editorOC.adicionarCampoLinhaVersao2(linha, "SICOOB", 103, 132, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 133, 142, " ", true, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 143, 143, " ", true, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyyyy"), 144, 151, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getHoraAtual().replaceAll(":", ""), 152, 157, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "000001", 158, 163, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "081", 164, 166, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "00000", 167, 171, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 172, 240, " ", false, false);
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gerarDadosHeaderLote(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, UnidadeEnsinoVO unidadeEnsinoVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		String linha = "";
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "756", 1, 3, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 8, 8, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "R", 9, 9, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 10, 11, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 12, 13, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "040", 14, 16, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 17, 17, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 18, 18, " ", false, false);
		// numero inscricao		
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(unidadeEnsinoVO.getCNPJ()), 19, 33, "0", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 34, 53, " ", true, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia() + controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 54, 59, "0", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getNumero() + controleRemessaVO.getContaCorrenteVO().getDigito(), 60, 72, "0", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 73, 73, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerAcentuacao(unidadeEnsinoVO.getRazaoSocial()), 74, 103, " ", true, false);

		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 104, 143, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 144, 183, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getIncrementalMX().toString(), 184, 191, "0", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyyyy"), 192, 199, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "00000000", 200, 208, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 209, 240, " ", false, false);
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);
	}	

	public void gerarDadosSegmentoP240(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ControleRemessaContaReceberVO dadosRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial) throws Exception {
		try {
			String linha = "";
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "756", 1, 3, "0", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, "0", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, "0", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental() - 1 ), 9, 13, "0", false, false); // Nº Sequencial do Registro no Lote - G038
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "P", 14, 14, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 15, 15, " ", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 16, 17, " ", false, false);

			linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia() + controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 18, 23, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getNumero() + controleRemessaVO.getContaCorrenteVO().getDigito(), 24, 36, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 37, 37, " ", false, false);

			String digito = "";
			//String digito = getModulo11Sicoob(dadosRemessaVO.getNossoNumero(), controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), controleRemessaVO.getContaCorrenteVO().getConvenio());
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "00"+dadosRemessaVO.getNossoNumero() + digito + "01" + controleRemessaVO.getContaCorrenteVO().getCodigoCedente() + "4" + "     ", 38, 57, " ", false, false);			

			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCarteira(), 58, 58, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 59, 59, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 60, 60, " ", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 61, 61, " ", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 62, 62, " ", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNrDocumento(), 63, 77, " ", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), PATTERN), 78, 85, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais2(Uteis.retornarDuasCasasDecimais(dadosRemessaVO.getValorComAcrescimo().toString())), 13), 86, 100, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "00000", 101, 105, " ", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 106, 106, " ", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "21", 107, 108, " ", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "A", 109, 109, " ", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(new Date(), PATTERN), 110, 117, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 118, 118, " ", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), PATTERN), 119, 126, "0", false, false);
			Double juro = 0.01;
			Double valorTitulo = Uteis.arrendondarForcando2CadasDecimais(configuracaoFinanceiroVO.getCobrarJuroMultaSobreValorCheioConta() ? dadosRemessaVO.getValorComAcrescimo() : dadosRemessaVO.getValor()-dadosRemessaVO.getValorAbatimento());
			Double juroFinal = (valorTitulo * juro) / 30;
			String juroStr = "";
			if (juroFinal.toString().length() > 4) {
				juroStr = (juroFinal.toString()).substring(0, 4);
			} else {
				juroStr = juroFinal.toString();
			}
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.retornarDuasCasasDecimais(juroStr)), 13), 127, 141, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 142, 142, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), PATTERN), 143, 150, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 151, 165, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 166, 180, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 181, 195, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero(), 196, 220, " ", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 221, 221, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 222, 223, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 224, 224, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 225, 227, " ", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "09", 228, 229, " ", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000000000", 230, 239, " ", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 240, 240, " ", false, false);
			linha += "\r";
			cmd.adicionarLinhaComando(linha, 0);
		} catch (Exception e) {
			String err = e.getMessage();
			err = "";
			throw e;
		}
	}

	public void gerarDadosSegmentoQ240(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ControleRemessaContaReceberVO dadosRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial) throws Exception {
		try {
			String linha = "";
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "756", 1, 3, "0", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, "0", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, "0", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental() - 1), 9, 13, "0", false, false); // Nº Sequencial do Registro no Lote - G038
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "Q", 14, 14, "0", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 15, 15, " ", false, false);
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 16, 17, " ", false, false);
			
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
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.retornarPrefixoOuSufixoDoCep(dadosRemessaVO.getCep(), "prefixo"), 129, 133, "0", false, false);
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.retornarPrefixoOuSufixoDoCep(dadosRemessaVO.getCep(), "sufixo"), 134, 136, "0", false, false);
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getCidade()), 15), 137, 151, " ", true, false);
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removerAcentuacao(dadosRemessaVO.getEstado()) , 2), 152, 153, " ", true, false);
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 154, 154, " ", false, false);
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 155, 169, "0", false, false);
	        //linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 155, 169, "0", false, false);
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 170, 209, " ", false, false); 
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000", 210, 212, " ", false, false);
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 213, 232, "0", false, false); // Nosso Nº no Banco Correspondente - C032
	        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 233, 240, " ", false, false);
			linha += "\r";
			cmd.adicionarLinhaComando(linha, 0);
		} catch (Exception e) {
			String err = e.getMessage();
			err = "";
			throw e;
		}
	}

	public void gerarDadosSegmentoR240(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ControleRemessaContaReceberVO dadosRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial) throws Exception {
		try {
			String linha = "";
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "756", 1, 3, " ", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, "0", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, "0", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental() - 1), 9, 13, "0", false, false); // Nº Sequencial do Registro no Lote - G038
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "R", 14, 14, " ", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 15, 15, " ", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 16, 17, "0", false, false); // Código de Movimento Remessa - C004
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 18, 18, " ", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 19, 26, "0", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 27, 41, "0", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 42, 65, "0", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 66, 99, "0", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 100, 199, " ", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 200, 215, "0", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 216, 216, " ", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 217, 228, "0", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 229, 230, " ", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 231, 231, "0", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 232, 240, " ", false, false);
			linha += "\r";
			cmd.adicionarLinhaComando(linha, 0);
		} catch (Exception e) {
			String err = e.getMessage();
			err = "";
			throw e;
		}
	}

	public void gerarDadosSegmentoS240(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, ControleRemessaContaReceberVO dadosRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, int numeroSequencial) throws Exception {
		try {
			String linha = "";
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "756", 1, 3, " ", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, "0", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental() - 1), 9, 13, "0", false, false); // Nº Sequencial do Registro no Lote - G038
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "S", 14, 14, " ", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 15, 15, " ", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 16, 17, "0", false, false); // Código de Movimento Remessa - C004
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 18, 18, " ", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 19, 65, " ", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 66, 66, " ", false, false);
	    	linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 67, 240, " ", false, false);
			linha += "\r";
			cmd.adicionarLinhaComando(linha, 0);
		} catch (Exception e) {
			String err = e.getMessage();
			err = "";
			throw e;
		}
	}
	

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gerarDadosTraillerLote(EditorOC editorOC, ControleRemessaVO controleRemessaVO, Comando cmd, int numeroSequencial, double valorTotalTitulo, int quantidadeDocu) throws Exception {
		String linha = "";
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "756", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 4, 7, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "5", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 9, 17, " ", false, false); // Uso Exclusivo FEBRABAN/CNAB
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental()), 18, 23, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental() / 4), 24, 29, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais2(Uteis.retornarDuasCasasDecimais(new Double(valorTotalTitulo).toString())), 15), 30, 46, "0", false, false);        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 47, 240, " ", false, false);
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);

	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void gerarDadosTraillerArquivo(EditorOC editorOC, Comando cmd, int numeroSequencial, double valorTotalTitulo, int quantidadeDocu) throws Exception {
		String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "756", 1, 3, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9999", 4, 7, "9", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 8, 8, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 9, 17, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 18, 23, "0", false, false); // Quantidade de Lotes do Arquivo - G049
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 24, 29, "0", false, false); // Quantidade de Registros do Arquivo - G056        
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000000", 30, 35, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 36, 240, " ", false, false);
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);

	}

	public String getModulo11Sicoob(String campo, String numeroAgencia, String convenio) {
    	//Modulo 11 - 3791
		campo = numeroAgencia + "000" + convenio + campo;
    	int multiplicador = 3;
    	int multiplicacao = 0;
    	int soma_campo = 0;
    	
    	
    	for (int i = campo.length(); i > 0; i--) {
    		multiplicacao = Integer.parseInt(campo.substring(i - 1, i)) * multiplicador;
    		
    		soma_campo = soma_campo + multiplicacao;
    		
    		
    		//multiplicador++;
    		if (multiplicador == 3) {
    			multiplicador = 7;
    		} else if (multiplicador == 7) {
    			multiplicador = 9;
    		} else if (multiplicador == 9) {
    			multiplicador = 1;
    		} else if (multiplicador == 1) {
    			multiplicador = 3;
    		}
    	}
    	
    	int dac = (soma_campo % 11);
    	    	
    	if ((dac == 0 || dac == 1)) {
    		dac = 0;
    	} else {
    		dac = 11 - (soma_campo % 11);
    	}
    	return ((Integer) dac).toString();
    }	

	@Override
	public void processarArquivoRetorno(ControleRemessaVO controleRemessaVO, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
