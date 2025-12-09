package negocio.facade.jdbc.financeiro.remessa.contapagar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaPagarControleRemessaContaPagarVO;
import negocio.comuns.financeiro.ContaPagarRegistroArquivoVO;
import negocio.comuns.financeiro.ControleCobrancaPagarVO;
import negocio.comuns.financeiro.ControleRemessaContaPagarVO;
import negocio.comuns.financeiro.MapaRemessaLoteContaPagarVO;
import negocio.comuns.financeiro.RegistroDetalhePagarVO;
import negocio.comuns.financeiro.RegistroHeaderLotePagarVO;
import negocio.comuns.financeiro.RegistroTrailerLotePagarVO;
import negocio.comuns.financeiro.enumerador.BancoEnum;
import negocio.comuns.financeiro.enumerador.OrdenarContaPagarRegistroArquivoEnum;
import negocio.comuns.financeiro.enumerador.TipoLancamentoContaPagarEnum;
import negocio.comuns.financeiro.enumerador.TipoServicoContaPagarEnum;
import negocio.comuns.utilitarias.Comando;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.EditorOC;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.interfaces.financeiro.remessa.ControleRemessaContaPagarLayoutInterfaceFacade;

public class ItauRemessaContaPagarCNAB240 extends ControleRemessaContaPagarLayoutImpl implements ControleRemessaContaPagarLayoutInterfaceFacade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1785795891974209873L;
	List<MapaRemessaLoteContaPagarVO> listaMapaRemessaContaPagarVO = new ArrayList<MapaRemessaLoteContaPagarVO>();
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public EditorOC executarGeracaoDadosArquivoRemessa(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		EditorOC editorOC = new EditorOC();
		Comando cmd = new Comando();
		separarDadosRemessaPorLote(listaDadosRemessaVOs);
		controleRemessaVO.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(controleRemessaVO.getUnidadeEnsinoVO().getCodigo(), false, usuario));
		controleRemessaVO.setIncrementalMXCP(getFacadeFactory().getControleRemessaMXFacade().consultarIncrementalCPPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), usuario));
		//controleRemessaVO.setIncrementalMX(236);
		gerarDadosHeaderArquivo(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, usuario);
		executarGeracaoLote(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, usuario);
		gerarDadosTraillerArquivo(editorOC, cmd, controleRemessaVO);
		editorOC.adicionarComando(cmd);
		getFacadeFactory().getControleRemessaMXFacade().alterarIncrementalPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), null, controleRemessaVO.getIncrementalMXCP() + 1, usuario);
		return editorOC;
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void separarDadosRemessaPorLote(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs) {
		listaMapaRemessaContaPagarVO.clear();
		for (ContaPagarControleRemessaContaPagarVO obj : listaDadosRemessaVOs) {
			MapaRemessaLoteContaPagarVO mapa = consultarMapaRemessaContaPagarVO(obj.getTipoServicoContaPagar() ,  obj.getTipoLancamentoContaPagar());
			if(!Uteis.isAtributoPreenchido(mapa.getTipoServicoContaPagar())){
				mapa.setTipoLancamentoContaPagar(obj.getTipoLancamentoContaPagar());
				mapa.setTipoServicoContaPagar(obj.getTipoServicoContaPagar());
			}
			mapa.getListaRemessaContaPagar().add(obj);
			mapa.setTotalRemessaContaPagar(mapa.getTotalRemessaContaPagar() + obj.getPrevisaoValorPagoDescontosMultas());
			addMapaRemessaContaPagarVO(mapa);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void executarGeracaoLote(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		controleRemessaVO.setNumeroIncremental(0);
		for (MapaRemessaLoteContaPagarVO mapaRemessaContaPagarVO : listaMapaRemessaContaPagarVO) {
			controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental()+ 1);
		     gerarDadosHeaderLote(editorOC, cmd, controleRemessaVO, mapaRemessaContaPagarVO, configuracaoFinanceiroVO, usuario);
		     controleRemessaVO.setNumeroSegmentoIncremental(0);
		     for (ContaPagarControleRemessaContaPagarVO remessaContaPagarVO : mapaRemessaContaPagarVO.getListaRemessaContaPagar()) {
		    	validarDadosParaTodosSegmento(remessaContaPagarVO);
		     	if(remessaContaPagarVO.getTipoLancamentoContaPagar().isCreditoContaCorrente() 
		     			|| remessaContaPagarVO.getTipoLancamentoContaPagar().isCreditoContaPoupanca()
		     			|| remessaContaPagarVO.getTipoLancamentoContaPagar().isTransferencia()
		     			|| remessaContaPagarVO.getTipoLancamentoContaPagar().isCaixaAutenticacao()
		     			|| remessaContaPagarVO.getTipoLancamentoContaPagar().isOrdemPagamento()){		     		
		     		gerarDetalheSegmentoA(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, remessaContaPagarVO);
		     		if(Uteis.isAtributoPreenchido(remessaContaPagarVO.getModalidadeTransferenciaBancariaEnum()) && remessaContaPagarVO.getModalidadeTransferenciaBancariaEnum().isPix()) {		     			
		     			gerarDetalheSegmentoB(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, remessaContaPagarVO);
		     		}
		     	}else if(remessaContaPagarVO.getTipoLancamentoContaPagar().isLiquidacaoTituloOutroBanco()){		     		
		     		gerarDetalheSegmentoJ(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, remessaContaPagarVO);
		     		gerarDetalheSegmentoJ52(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, remessaContaPagarVO);
		     	}else if(remessaContaPagarVO.getTipoLancamentoContaPagar().isDarfNormalSemCodigoBarra()
		     			|| remessaContaPagarVO.getTipoLancamentoContaPagar().isDarfSimplesSemCodigoBarra()
		     			|| remessaContaPagarVO.getTipoLancamentoContaPagar().isDpvatRenavam()
		     			|| remessaContaPagarVO.getTipoLancamentoContaPagar().isGareDrSemCodigoBarra()
		     			|| remessaContaPagarVO.getTipoLancamentoContaPagar().isGareIcmsSemCodigoBarra()
		     			|| remessaContaPagarVO.getTipoLancamentoContaPagar().isGareItcmdSemCodigoBarra()
		     			|| remessaContaPagarVO.getTipoLancamentoContaPagar().isGpsSemCodigoBarra()
		     			|| remessaContaPagarVO.getTipoLancamentoContaPagar().isIpvaComRenavam()
		     			|| remessaContaPagarVO.getTipoLancamentoContaPagar().isLicenciamentoComRenavam()){		     		
		     		gerarDetalheSegmentoN(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, remessaContaPagarVO);
		     		
		     	}else if(remessaContaPagarVO.getTipoLancamentoContaPagar().isPagamentoContasTributosComCodigoBarra()){	
		     		gerarDetalheSegmentoO(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, remessaContaPagarVO);
		     	}
		     }
		     gerarDadosTraillerLote(editorOC, cmd, controleRemessaVO, mapaRemessaContaPagarVO, configuracaoFinanceiroVO, usuario);
		}
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDadosHeaderArquivo(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		String linha = "";
		// CÓDIGO DO BCO NA COMPENSAÇÃO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
		// LOTE DE SERVIÇO 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000", 4, 7, " ", false, false);
		// REGISTRO HEADER DE ARQUIVO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 8, 8, " ", false, false);
		// COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 9, 14, " ", false, false);
		// Nr DA VERSÃO DO LAYOUT DO ARQUIVO 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "080", 15, 17, " ", false, false);
		// TIPO DE INSCRIÇÃO DA EMPRESA		
		// 1 = CPF
		// 2 = CNPJ
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 18, 18, " ", false, false);
		// CNPJ EMPRESA DEBITADA
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 19, 32, " ", false, false);
		//COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 33, 52, " ", false, false);
		// NÚMERO AGÊNCIA DEBITADA
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), 53, 57, "0", false, false);
		// COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 58, 58, " ", false, false);
		// NÚMERO DE C/C DEBITADA
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getNumero(), 59, 70, "0", false, false);
		// COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 71, 71, " ", false, false);
		// Digito Verificador da Agencia / Conta
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getDigito(), 72, 72, " ", false, false);
		// NOME DA EMPRESA
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(controleRemessaVO.getUnidadeEnsinoVO().getRazaoSocial())), 30), 73, 102, " ", false, false);
		// NOME DO BANCO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "BANCO ITAU SA", 103, 132, " ", true, false);
		// COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 133, 142, " ", false, false);
		// CÓDIGO REMESSA/RETORNO
		// 1 = remessa
		// 2 = retorno
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 143, 143, " ", true, false);
		// DATA DE GERAÇÃO DO ARQUIVO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyyyy"), 144, 151, " ", false, false);
		// HORA DE GERAÇÃO DO ARQUIVO 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.gethoraHHMMSS(new Date()), 152, 157, "0", false, false);
		// COMPLEMENTO DE REGISTRO 
		linha = editorOC.adicionarCampoLinhaVersao2(linha,"",  158, 166, "0", false, false);
		// DENSIDADE DE GRAVAÇÃO DO ARQUIVO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 167,171, "0", false, false);
		// COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 172, 240, " ", false, false);
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void gerarDadosTraillerArquivo(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO) throws Exception {
		String linha = "";
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "9999", 4, 7, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 8, 8, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 9, 17, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental()), 18, 23, "0", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(cmd.getNrLinhas() + 1), 6), 24, 29, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 30, 240, " ", false, false);		
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);
		String linhaFinalArquivo = "";		
		cmd.adicionarLinhaComando(linhaFinalArquivo, 0);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDadosHeaderLote(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, MapaRemessaLoteContaPagarVO mapaRemessaContaPagar, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		String linha = "";
		// CÓDIGO BANCO NA COMPENSAÇÃO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
		// LOTE IDENTIFICAÇÃO DE PAGTOS
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 4), 4, 7, " ", false, false);
		// REGISTRO HEADER DE LOTE
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 8, 8, " ", false, false);
		// TIPO DA OPERAÇÃO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "C", 9, 9, " ", false, false);
		// TIPO DE PAGTO 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, mapaRemessaContaPagar.getTipoServicoContaPagar().getValor(), 10, 11, " ", false, false);
		// Forma de lancamento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, mapaRemessaContaPagar.getTipoLancamentoContaPagar().getValor(), 12, 13, " ", false, false);
		// Numero da Versao do Lote
		if(executarVerificacaoExistenciaPixListaRemessa(mapaRemessaContaPagar.getListaRemessaContaPagar())) {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "030", 14, 16, " ", false, false);
		}else{
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "040", 14, 16, " ", false, false);
		}		
		// filler espaco em branco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 17, 17, " ", false, false);
		// TIPO INSCRIÇÃO EMPRESA DEBITADA
		// 0 = Isento / Nao Informado
		// 1 = CPF
		// 2 = CNPJ
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 18, 18, " ", false, false);
		// CNPJ EMPRESA DEBITADA
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 19, 32, "0", false, false);
		// IDENTIFICAÇÃO DO LANÇAMENTO NO EXTRATO DO FAVORECIDO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 33, 36, " ", false, false);
		//COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 37, 52, " ", false, false);
		// NÚMERO AGÊNCIA DEBITADA
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), 53, 57, "0", false, false);
		// COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 58, 58, " ", false, false);
		// NÚMERO DE C/C DEBITADA
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getNumero(), 59, 70, "0", false, false);
		// COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha," ", 71, 71, " ", false, false);
		// DAC DA AGÊNCIA/CONTA DEBITADA
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getDigito(), 72, 72, " ", false, false);
		// NOME DA EMPRESA DEBITADA
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(controleRemessaVO.getUnidadeEnsinoVO().getRazaoSocial())), 30), 73, 102, " ", false, false);
		
		//FINALIDADE DO LOTE FINALIDADE DOS PAGTOS DO LOTE
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 103, 132, " ", false, false);
		//HISTÓRICO DE C/C COMPLEMENTO HISTÓRICO C/C DEBITADA 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 133, 142, " ", false, false);		
		// Opcional
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(controleRemessaVO.getUnidadeEnsinoVO().getEndereco())), 30), 143, 172, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removeCaractersEspeciais(controleRemessaVO.getUnidadeEnsinoVO().getNumero()), 173,177, "0", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(controleRemessaVO.getUnidadeEnsinoVO().getComplemento())),15), 178 , 192, "0", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(controleRemessaVO.getUnidadeEnsinoVO().getCidade().getNome())),20), 193,212, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(Uteis.removerAcentuacao(controleRemessaVO.getUnidadeEnsinoVO().getCEP())), 213, 220, "0", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(controleRemessaVO.getUnidadeEnsinoVO().getCidade().getEstado().getSigla())) , 221,222, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 223, 240, " ", false, false);
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private boolean executarVerificacaoExistenciaPixListaRemessa(List<ContaPagarControleRemessaContaPagarVO> listaRemessaContaPagar) {	
		/*boolean existeContaPix = false ;
		for(ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO :   listaRemessaContaPagar) {			
			if(Uteis.isAtributoPreenchido(contaPagarControleRemessaContaPagarVO.getModalidadeTransferenciaBancariaEnum()) &&  contaPagarControleRemessaContaPagarVO.getModalidadeTransferenciaBancariaEnum().isPix()) {
				existeContaPix = true ;
			}			
		}*/
		return listaRemessaContaPagar.stream().anyMatch(b-> Uteis.isAtributoPreenchido(b.getModalidadeTransferenciaBancariaEnum()) && b.getModalidadeTransferenciaBancariaEnum().isPix());
		//return existeContaPix;
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDadosTraillerLote(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, MapaRemessaLoteContaPagarVO mapaRemessaContaPagarVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		String linha = "";
		// Codigo do Banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
		// Lote de Servico
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 4), 4, 7, " ", false, false);
		// Tipo de Registro
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "5", 8, 8, " ", false, false);
		// filter espaco em branco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 9, 17, " ", false, false);
		// QuantidadeRegistroLote
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroSegmentoIncremental() + 2), 6), 18, 23, "0", false, false);
		// Somatória dos Valores
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(mapaRemessaContaPagarVO.getTotalRemessaContaPagar())), 16), 24, 41, "0", false, false);
		// Somatória Quantidade Moeda 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 42, 59, "0", false, false);
		//COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 60, 230, " ", false, false);
		// Ocorrências para o Retorno
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 231, 240, " ", false, false);
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);
	}	

	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDetalheSegmentoA(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaPagarControleRemessaContaPagarVO dadosRemessaVO) throws Exception {
		validarDadosSegmentoA(dadosRemessaVO);
		controleRemessaVO.setNumeroSegmentoIncremental(controleRemessaVO.getNumeroSegmentoIncremental() + 1);
		String linha = "";
		// CÓDIGO BANCO NA COMPENSAÇÃO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
		// LOTE DE SERVIÇO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 4), 4, 7, " ", false, false);
		// REGISTRO DETALHE DE LOTE
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
		// Nº SEQUENCIAL REGISTRO NO LOTE 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroSegmentoIncremental()), 5), 9, 13, " ", false, false);
		// CÓDIGO SEGMENTO REG. DETALHE 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "A", 14, 14, " ", false, false);
		// Tipo de Movimento
		// 0 = Indica INCLUSÃO
		// 3 = Indica ESTORNO (somente para retorno)
		// 5 = Indica ALTERAÇÃO
		// 8 = Indica INCLUSÃO COMPROR*
		// 9 = Indica EXCLUSÃO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 15, 17, "0", false, false);		
		// Código Câmara Compensação
		// 000 = CC
		// 018 = TED CIP
		// 810 = TED STR /No caso do Favorecido/Beneficiário da TED ser uma Instituição Financeira, utilizar 810/TED STR.
		// 700 = DOC
		// 888 =TED CIP ou STR Utilizado quando a Instituição Financeira de crédito não possuir Código na Câmara de Compensação. (Nota G030)
		// 009 =PIX
		if(dadosRemessaVO.getTipoLancamentoContaPagar().isCreditoContaCorrente() || dadosRemessaVO.getTipoLancamentoContaPagar().isCreditoContaPoupanca() || dadosRemessaVO.getTipoLancamentoContaPagar().isCaixaAutenticacao()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "000", 18, 20, " ", false, false);		
		}else if(dadosRemessaVO.getTipoLancamentoContaPagar().isTransferencia() && dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isDoc()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "700", 18, 20, " ", false, false);
		}else if(dadosRemessaVO.getTipoLancamentoContaPagar().isTransferencia() && dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isTed() && !dadosRemessaVO.isTipoSacadoBanco()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "018", 18, 20, " ", false, false);
		}else if(dadosRemessaVO.getTipoLancamentoContaPagar().isTransferencia() && dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isTed() && dadosRemessaVO.isTipoSacadoBanco()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "810", 18, 20, " ", false, false);
		}else if(dadosRemessaVO.getTipoLancamentoContaPagar().isPixTransferencia() && dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isPix() ){
				linha = editorOC.adicionarCampoLinhaVersao2(linha, "009", 18, 20, " ", false, false);
		}else if (dadosRemessaVO.getTipoLancamentoContaPagar().isOrdemPagamento()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 18, 20, " ", false, false);
		}
		// CÓDIGO BANCO FAVORECIDO 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getBancoRecebimento().getNrBanco(), 21, 23, "0", false, false);
		
		// AGÊNCIA CONTA FAVORECIDO
		
		
			 if(dadosRemessaVO.getBancoRecebimento().getNrBanco().equals(Bancos.ITAU.getNumeroBanco())) {				 
			    linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 24, 24, "0", false, false);
				linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNumeroAgenciaRecebimento(), 25, 28, "0", false, false);
				linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 29,29, " ", false, false);
				linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 30,35, "0", false, false);
				linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getContaCorrenteRecebimento(), 36,41, "0", false, false);
				linha = editorOC.adicionarCampoLinhaVersao2(linha, " " , 42,42, " ", false, false);
				linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getDigitoCorrenteRecebimento(), 43, 43, "0", false, false);
				if(dadosRemessaVO.getTipoLancamentoContaPagar().isOrdemPagamento()) {
					linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 36,41, "0", false, false);
					linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 43, 43, "0", false, false);
				}					
			 }else{				 
			    linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNumeroAgenciaRecebimento(), 24, 28, "0", false, false);
				linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 29,29, " ", false, false);
				linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getContaCorrenteRecebimento(), 30,41, "0", false, false);				
				linha = editorOC.adicionarCampoLinhaVersao2(linha, "" , 42,42, " ", false, false);
				linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getDigitoCorrenteRecebimento(), 43, 43, "0", false, false);
				
			 }			   
			
		//NOME DO FAVORECIDO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(dadosRemessaVO.getNomeFavorecido())), 30), 44, 73, " ", true, false);
		// Nro. do Documento Cliente		
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoTransmissaoRemessanossonumero(), 74, 93, "0", false, false);	
		/*if(Uteis.isAtributoPreenchido(dadosRemessaVO.getCodigoAgrupamentoContasPagar())) {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoAgrupamentoContasPagar(), 74, 93, "0", false, false);
		}else {
		  linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero().toString(), 74, 93, "0", false, false);
		}*/
		// DATA PREVISTA PARA PAGTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 94, 101, " ", false, false);	
		// Tipo da Moeda
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "REA", 102, 104, " ", false, false);
		// IDENTIFICAÇÃO DA INSTITUIÇÃO PARA O SPB 
	
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 105, 112, " ", false, false);
		//CONTA PAGAMENTO / PIX
		if(dadosRemessaVO.getTipoLancamentoContaPagar().isPixTransferencia() && (dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isTed() || (dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isDoc() && !dadosRemessaVO.getFinalidadeDocEnum().isDocPoupanca()))){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 113,114, " ", false, false);
		}else if(dadosRemessaVO.getTipoLancamentoContaPagar().isPixTransferencia() && dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isDoc() && dadosRemessaVO.getFinalidadeDocEnum().isDocPoupanca()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "03", 113,114, " ", false, false);
		}else if(dadosRemessaVO.getTipoLancamentoContaPagar().isPixTransferencia() && dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isPix() ){
		    linha = editorOC.adicionarCampoLinhaVersao2(linha, "04", 113,114, " ", false, false);
		}else {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "PG", 113,114, " ", false, false);
		}
		//COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 115,119, "0", false, false);

		// Valor do Pagamento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 120, 134, "0", false, false);
		// Nro. do Documento Banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "" , 135, 149, " ", false, false);
		//COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 150,154, " ", false, false);
		// DATA REAL EFETIVAÇÃO DO PAGTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 155, 162, " ", false, false);
		// Valor Real do Pagamento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13) , 163, 177, "0", false, false);
		// Informação 2 / Mensagem		
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 178, 203, " ", false, false);
		//Nr DE INSCRIÇÃO DO FAVORECIDO (CPF/CNPJ)
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(dadosRemessaVO.getCnpjOuCpfFavorecido()), 204, 217, "0", false, false);
		// Finalidade do DOC
		// 01 = Crédito em Conta Corrente
		// 02 = Pagamento de Aluguel / Condomínio
		// 03 = Pagamento de Duplicatas e Títulos
		// 04 = Pagamento de Dividendos
		// 05 = Pagamento de Mensalidades Escolares
		// 07 = Pagamento a Fornecedor / Honorários
		// 08 = Pagamento de Câmbio / Fundos / Bolsas
		// 09 = Repasse de Arrecadação / Pagamento de Tributos
		// 11 = DOC para Poupança
		// 12 = DOC para Depósito Judicial
		// 13 = Pensão Alimentícia
		// 99 = Outros
		
		if(dadosRemessaVO.getTipoLancamentoContaPagar().isTransferencia() && dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isDoc()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getFinalidadeDocEnum().getValor(), 218, 219, " ", false, false);		
		}else{
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 218, 219, " ", false, false);
		}
		// Finalidade de TED
		// 00001 = Pagamento de Impostos Tributos e Taxas
		// 00002 = Pagamento a Concessionárias Serviço Público
		// 00003 = Pagamento de Dividendos
		// 00005 = Pagamento a Fornecedores
		// 00006 = Pagamento de Honorários
		// 00007 = Pagamento de aluguéis e taxas condomínio
		// 00008 = Pagamento de duplicatas e títulos
		// 00009 = Pagamento de mensalidade escolar
		// 00010 = Crédito em conta
		// 00101 = Pensão alimentícia
		// 99999 Outros
		if(dadosRemessaVO.getTipoLancamentoContaPagar().isTransferencia() && dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isTed()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getFinalidadeTedEnum().getValor(), 220, 224, " ", false, false);	
		}else{
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 220, 224, " ", false, false);
		}		
		
		//COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 225, 229, " ", false, false);
		// Emissão de Aviso ao Favorecido
		// 0 = Não Emite Aviso
		// 2 = Emite Aviso Somente para o Remetente
		// 5 = Emite Aviso Somente para o Favorecido
		// 6 = Emite Aviso para o Remetente e Favorecido
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 230, 230, " ", false, false);
		// Ocorrências para o Retorno
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 231, 240, " ", false, false);
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDetalheSegmentoB(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaPagarControleRemessaContaPagarVO dadosRemessaVO) throws Exception {
		validarDadosSegmentoB(dadosRemessaVO);
		controleRemessaVO.setNumeroSegmentoIncremental(controleRemessaVO.getNumeroSegmentoIncremental() + 1);
		String linha = "";
		// CÓDIGO BANCO NA COMPENSAÇÃO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
		// LOTE DE SERVIÇO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 4), 4, 7, " ", false, false);
		// REGISTRO DETALHE DO LOTE
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
		// Nº SEQUENCIAL REGISTRO NO LOTE
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroSegmentoIncremental()), 4), 9, 13, " ", false, false);
		// CÓDIGO SEGMENTO REG. DETALHE
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "B", 14, 14, " ", false, false);
		//Filler espaco em branco COMPLEMENTO DE REGISTRO 
		if(dadosRemessaVO.getTipoLancamentoContaPagar().isPixTransferencia() && dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isPix()){
			//TIPO IDENTIFICAÇÃO DE CHAVE PIX
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getTipoIdentificacaoChavePixEnum().getValor(), 15, 16, " ", false, false);
			//Filler espaco em branco COMPLEMENTO DE REGISTRO 
			linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 17, 17, " ", false, false);			
		}else {			
			linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 15, 17, " ", false, false);
		}
		// Tipo de Inscrição do Favorecido 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getTipoInscricaoFavorecido(), 18, 18, " ", false, false);
		//CNPJ/CPF do Favorecido
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(dadosRemessaVO.getCnpjOuCpfFavorecido()), 19, 32, "0", false, false);
		
		if(dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isPix()){
			//COMPLEMENTO DE REGISTRO
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 33, 62, " ", true, false);
			//INFORMAÇÃO ENTRE USUÁRIOS
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 63, 127, "0", true, false);
			//CHAVE DE ENDEREÇAMENTO
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getChaveEnderecamentoPix(), 128, 227, " ", true, false);
			
		}else {
			//Logradouro do Favorecido
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(dadosRemessaVO.getLogradouroFavorecido())), 30), 33, 62, " ", true, false);
			//Número do Local do Favorecido 
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(dadosRemessaVO.getNumeroEnderecoFavorecido())), 5), 63, 67, "0", true, false);
			//Complemento do Local Favorecido
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(dadosRemessaVO.getComplementoFavorecido())), 15), 68, 82, " ", true, false);
			//Bairro do Favorecido
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(dadosRemessaVO.getBairroFavorecido())), 15), 83, 97, " ", true, false);
	        //Cidade do Favorecido
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(dadosRemessaVO.getCidadeFavorecido())), 20), 98, 117, " ", true, false);
	        //CEP do Favorecido
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(Uteis.removerAcentuacao(dadosRemessaVO.getCepFavorecido())), 118, 125, " ", true, false);
			//Estado do Favorecido
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(dadosRemessaVO.getEstadoFavorecido())), 2), 126, 127, " ", true, false);
			//ENDEREÇO DE E-MAIL 
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getEmailFavorecido(), 128, 227, " ", false, false);
		}
		
		//COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 228, 230, " ", false, false);
		//fCÓDIGO DE OCORRÊNCIAS NO RETORNO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 231, 240, " ", false, false);		
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);
	}
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDetalheSegmentoJ(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaPagarControleRemessaContaPagarVO dadosRemessaVO) throws Exception {
		validarDadosSegmentoJ(dadosRemessaVO);
		controleRemessaVO.setNumeroSegmentoIncremental(controleRemessaVO.getNumeroSegmentoIncremental() + 1);
		String linha = "";
		// CÓDIGO BANCO NA COMPENSAÇÃO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
		// LOTE DE SERVIÇO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 4), 4, 7, " ", false, false);
		// Tipo de Registro
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
		// Número Sequencial do Registro no Lote
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroSegmentoIncremental()), 5), 9, 13, " ", false, false);
		// Código Segmento do Registro Detalhe
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "J", 14, 14, " ", false, false);
		// TIPO DE MOVIMENTO
		// 0 = Indica INCLUSÃO
		// 3 = Indica ESTORNO (somente para retorno)
		// 5 = Indica ALTERAÇÃO
		// 8 = Indica INCLUSÃO COMPROR*
		// 9 = Indica EXCLUSÃO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 15, 17, "0", false, false);		
		//Código de Barras
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoBarra(), 18, 61, " ", false, false);		
		//Nome do Cedente
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(dadosRemessaVO.getNomeFavorecido())), 30), 62, 91, " ", true, false);
		// Data Vencimento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 92, 99, " ", false, false);
		// Valor Nominal do Título
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 100, 114, "0", false, false);
		// Valor Desconto + Abatimento
		linha = editorOC.adicionarCampoLinhaVersao2(linha,"", 115, 129, "0", false, false);
		// Valor Multa + Juros
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 130, 144, "0", false, false);		
		// DATA DO PAGAMENTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 145, 152, " ", false, false);
		// VALOR DO PAGAMENTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 153, 167, "0", false, false);
		// COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 168, 182, "0", false, false);
		//Nº DOCTO ATRIBUÍDO PELA EMPRESA
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoTransmissaoRemessanossonumero(), 183, 202, "0", false, false);
		/*if(Uteis.isAtributoPreenchido(dadosRemessaVO.getCodigoAgrupamentoContasPagar())) {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoAgrupamentoContasPagar(), 183, 202, "0", false, false);
		}else {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(dadosRemessaVO.getNossoNumero().toString()), 183, 202, "0", false, false);
		}*/
		// COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 203, 215, "0", false, false);
		// NÚMERO ATRIBUÍDO PELO BANCO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 216,230, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 231, 240, " ", false, false);
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);
	}
	
	
	 
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDetalheSegmentoJ52(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaPagarControleRemessaContaPagarVO dadosRemessaVO) throws Exception {
		controleRemessaVO.setNumeroSegmentoIncremental(controleRemessaVO.getNumeroSegmentoIncremental() + 1);
		String linha = "";
		// Codigo do Banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
		// Lote de Servico
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 4), 4, 7, " ", false, false);
		// Tipo de Registro
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
		// Número Sequencial do Registro no Lote
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroSegmentoIncremental()), 5), 9, 13, " ", false, false);
		// Código Segmento do Registro Detalhe
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "J", 14, 14, " ", false, false);
		// filter
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 15, 17, "0", false, false);
		
		//Identificação Registro Opcional
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "52", 18,19, " ", false, false);
		//Sacado
		// Tipo de Inscrição do Favorecido 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getTipoInscricaoFavorecido(), 20, 20, " ", false, false);
		//CNPJ/CPF do Favorecido
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(dadosRemessaVO.getCnpjOuCpfFavorecido()), 21, 35, "0", false, false);
		//Nome do Favorecido
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(dadosRemessaVO.getNomeFavorecido(), 40), 36, 75, " ", true, false);
		// Cedente
		// Tipo de Inscrição do Cedente
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 76, 76, " ", false, false);
		// Numero de Inscricao da Cedente
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 77, 91, "0", false, false);
		// nome Cedente
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(controleRemessaVO.getUnidadeEnsinoVO().getRazaoSocial(), 40), 92, 131, " ", false, false);
		if(dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isPix() &&   dadosRemessaVO.getTipoLancamentoContaPagar().isPixTransferencia()) {
			// URL / CHAVE PIX
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getChaveEnderecamentoPix(), 132, 208, " ", false, false);
			// CÓDIGO DE IDENTIFICAÇÃO DO QR-CODE
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getIdentificacaoQRCODE(), 209, 240, " ", false, false);	
		}else {
		// Cedente
		// Tipo de Inscrição do Cedente
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 132, 132, " ", false, false);
		// Numero de Inscricao da Cedente
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 133, 147, "0", false, false);
		// nome Cedente
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(controleRemessaVO.getUnidadeEnsinoVO().getRazaoSocial(), 40), 148, 187, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 188, 240, " ", false, false);
		}
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);
	}
	
	
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDetalheSegmentoN(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaPagarControleRemessaContaPagarVO dadosRemessaVO) throws Exception {
		controleRemessaVO.setNumeroSegmentoIncremental(controleRemessaVO.getNumeroSegmentoIncremental() + 1);
		String linha = "";
		// Codigo do Banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
		// Lote de Servico
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 4), 4, 7, " ", false, false);
		// Tipo de Registro
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
		// Número Sequencial do Registro no Lote
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroSegmentoIncremental()), 5), 9, 13, " ", false, false);
		// Código Segmento do Registro Detalhe
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 14, 14, " ", false, false);
		// Tipo de Movimento
		// 000 = Indica INCLUSÃO
		// 003 = Indica ESTORNO (somente para retorno)
		// 005 = Indica ALTERAÇÃO
		// 008 = Indica INCLUSÃO COMPROR*
		// 009 = Indica EXCLUSÃO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 15, 17, "0", false, false);	
		
		// Informações Complementares de acordo com o respectivo tributo 
		if(dadosRemessaVO.getTipoLancamentoContaPagar().isGpsSemCodigoBarra()){
 			gerarDetalheSegmentoN1(linha, editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO);
 		}else if(dadosRemessaVO.getTipoLancamentoContaPagar().isDarfNormalSemCodigoBarra()){
 			gerarDetalheSegmentoN2(linha, editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO);
 		}else if(dadosRemessaVO.getTipoLancamentoContaPagar().isDarfSimplesSemCodigoBarra()){
 			gerarDetalheSegmentoN3(linha, editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO);
 		}else if(dadosRemessaVO.getTipoLancamentoContaPagar().isGareDrSemCodigoBarra()
     			|| dadosRemessaVO.getTipoLancamentoContaPagar().isGareIcmsSemCodigoBarra()
     			|| dadosRemessaVO.getTipoLancamentoContaPagar().isGareItcmdSemCodigoBarra()){
 			gerarDetalheSegmentoN4(linha, editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, dadosRemessaVO);
 		}		
		//NOME DO CONTRIBUINTE		
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(dadosRemessaVO.getNomeFavorecido())), 30), 166,195, " ", true, false);
		// Nro. do Documento Cliente
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoTransmissaoRemessanossonumero(), 196,215, " ", false, false);
		/*if(Uteis.isAtributoPreenchido(dadosRemessaVO.getCodigoAgrupamentoContasPagar())) {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoAgrupamentoContasPagar(), 196,215, " ", false, false);
		}else {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(dadosRemessaVO.getNossoNumero().toString()), 196,215, " ", false, false);
		}*/		
		//NÚMERO ATRIBUÍDO PELO BANCO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 216,230, " ", false, false);
		//CÓDIGO DE OCORRÊNCIAS P/ RETORNO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 231, 240, " ", false, false);
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);
	}
	
	/**
	 * Informações complementares para pagamento da GPS
	 * @param editorOC
	 * @param cmd
	 * @param controleRemessaVO
	 * @param configuracaoFinanceiroVO
	 * @param dadosRemessaVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDetalheSegmentoN1(String linha, EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaPagarControleRemessaContaPagarVO dadosRemessaVO) throws Exception {
		validarDadosSegmentoN1(dadosRemessaVO);		
		// IDENTIFICAÇÃO DO TRIBUTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 18,19, " ", false, false);
		// CÓDIGO DE PAGAMENTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getTipoIdentificacaoContribuinte().getValor(), 117, 118, "0", false, false);
		// Mes e Ano de Competencia
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataFatoGerador(), "MMyyyy"), 24,29, " ", false, false);
		// IDENTIFICAÇÃO CNPJ/CEI/NIT/PIS DO CONTRIBUINTE 
		linha = editorOC.adicionarCampoLinhaVersao2(linha,  dadosRemessaVO.getIdentificacaoContribuinte(), 30,43, "0", false, false);		
		// Valor Previsto do Pagamento do INSS
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 44, 57, "0", false, false);		
		//VALOR DE OUTRAS ENTIDADES
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " " ,58,71 , "0", false, false);		
		//ATUALIZAÇÃO MONETÁRIA
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " " ,72,85, "0", false, false);		
		//VALOR ARRECADADO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " " ,86,99, "0", false, false);		
		//DATA DA ARRECADAÇÃO/ EFETIVAÇÃO DO PAGAMENTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha,  Uteis.getData(dadosRemessaVO.getDataVencimento(),"ddMMyyyy"), 100,107, "0", false, false);		
		//COMPLEMENTO DO REGISTRO 
		linha = editorOC.adicionarCampoLinhaVersao2(linha,  " " , 108,115  ," ", false, false);
		//INFORMAÇÕES COMPLEMENTARES
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 116,165, " ", false, false);
	}
	
	/**
	 * Informações complementares para pagamento de DARF Normal
	 * @param editorOC
	 * @param cmd
	 * @param controleRemessaVO
	 * @param configuracaoFinanceiroVO
	 * @param dadosRemessaVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDetalheSegmentoN2(String linha, EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaPagarControleRemessaContaPagarVO dadosRemessaVO) throws Exception {
		validarDadosSegmentoN2(dadosRemessaVO);
		// IDENTIFICAÇÃO DO TRIBUTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "02" , 18,19, " ", false, false);
		// CÓDIGO DA RECEITA
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoReceitaTributo(), 20,23, "0", false, false);
		// Identificação do Contribuinte
		linha = editorOC.adicionarCampoLinhaVersao2(linha,  dadosRemessaVO.getTipoIdentificacaoContribuinte().getValor(), 24,24, "0", false, false);
		// CPF OU CNPJ DO CONTRIBUINTE
		linha = editorOC.adicionarCampoLinhaVersao2(linha,  dadosRemessaVO.getIdentificacaoContribuinte(), 25,38 , " ", false, false);
		// Período de Apuração 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataFatoGerador(), "ddMMyyyy"), 39,46, " ", false, false);
		// Numero de Referencia
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNumeroReferencia(), 47,63 , "0", false, false);
		//Valor Principal
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 64,77, "0", false, false);		
		//Valor da multa
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 78,91, "0", false, false);
		//Valor Dos juro/encargos
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 92,105, "0", false, false);		
		//VALOR TOTAL A SER PAGO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 106, 119, "0", false, false);
		// Data de Vencimento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 120,127, " ", false, false);
		//DATA DO PAGAMENTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 128,135 , " ", false, false);
		//COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 136,165, " ", false, false);
	}
	
	/**
	 * Informações complementares para pagamento de DARF Simples
	 * @param editorOC
	 * @param cmd
	 * @param controleRemessaVO
	 * @param configuracaoFinanceiroVO
	 * @param dadosRemessaVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDetalheSegmentoN3(String linha, EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaPagarControleRemessaContaPagarVO dadosRemessaVO) throws Exception {
		validarDadosSegmentoN3(dadosRemessaVO);
		// Código da Receita do Tributo
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "03", 18,19, " ", false, false);
		// Código da Receita do Tributo
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoReceitaTributo(), 20,23, " ", false, false);
		// Tipo de Identificação do Contribuinte
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getTipoIdentificacaoContribuinte().getValor(), 24,24, "0", false, false);
		// Identificação do Contribuinte
		linha = editorOC.adicionarCampoLinhaVersao2(linha,  dadosRemessaVO.getIdentificacaoContribuinte(), 25,38," ", false, false);		
		// Período de Apuração 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataFatoGerador(), "ddMMyyyy"),39,46, " ", false, false);
		//Valor da Receita Bruta Acumulada
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorReceitaBrutaAcumulada())), 13), 47, 55, "0", false, false);
		//Percentual  da Receita Bruta Acumulada
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorReceitaBrutaAcumulada())),5), 56,59 , "0", false, false);
		//COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 60 , 63 , " ", false, false);
		//Valor Principal
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 64,77, "0", false, false);
		//Valor da multa
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 78, 91, "0", false, false);
		//Valor Dos juro/encargos
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 92,105, " ", false, false);		
		//VALOR TOTAL A SER PAGO
		linha = editorOC.adicionarCampoLinhaVersao2(linha,  Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 106,119 , " ", false, false);
		//DATA DE VENCIMENTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 120,127 , " ", false, false);
		// DATA DO PAGAMENTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 128,135, " ", false, false);
		//COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 136,165, " ", false, false);
	}
	
	/**
	 * Informações complementares para pagamento de GARE-SP.
	 * @param editorOC
	 * @param cmd
	 * @param controleRemessaVO
	 * @param configuracaoFinanceiroVO
	 * @param dadosRemessaVO
	 * @throws Exception
	 */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDetalheSegmentoN4(String linha, EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaPagarControleRemessaContaPagarVO dadosRemessaVO) throws Exception {
		validarDadosSegmentoN4(dadosRemessaVO);
		// IDENTIFICAÇÃO DO TRIBUTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "05", 18,19 , " ", false, false);
		// Código da Receita do Tributo
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoReceitaTributo(), 20, 23, " ", false, false);
		// Tipo de Identificação do Contribuinte
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getTipoIdentificacaoContribuinte().getValor(), 24,24, "0", false, false);
		// Identificação do Contribuinte
		linha = editorOC.adicionarCampoLinhaVersao2(linha,  dadosRemessaVO.getIdentificacaoContribuinte(), 25,38 , "0", false, false);
		// Inscrição Estadual / Código do Município / Numero Declaração
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getInscricaoEstadualFavorecido() + dadosRemessaVO.getInscricaoMunicipalFavorecido() + dadosRemessaVO.getNumeroReferencia(), 39,50, "0", false, false);
		//Divida Ativa / Numero Etiqueta
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 51,63 , "0", false, false);
		// Periodo Referencia
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataFatoGerador(), "MMyyyy"), 64,69 , " ", false, false);
		//Numero da Parcela / Notificação 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 70,82 , "0", false, false);
		//Valor Principal
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 83,96 , "0", false, false);
		//Valor Dos juro/encargos
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 97,110 , "0", false, false);	
		//Valor da multa
		linha = editorOC.adicionarCampoLinhaVersao2(linha,"", 111,124, "0", false, false);
		//VALOR DO PAGAMENTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 125,138 , "0", false, false);
		//DATA DE VENCIMENTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 139,146 , " ", false, false);
		// DATA DO PAGAMENTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 147,154 , " ", false, false);
		// COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " " , 155,165  , " ", false, false);
				
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDetalheSegmentoO(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaPagarControleRemessaContaPagarVO dadosRemessaVO) throws Exception {
		validarDadosSegmentoO(dadosRemessaVO);
		controleRemessaVO.setNumeroSegmentoIncremental(controleRemessaVO.getNumeroSegmentoIncremental() + 1);
		String linha = "";
		// CÓDIGO BANCO NA COMPENSAÇÃO 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "341", 1, 3, " ", false, false);
		//LOTE DE SERVIÇO 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 4), 4, 7, " ", false, false);
		// REGISTRO DETALHE DE LOTE
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
		// Nº SEQUENCIAL REGISTRO NO LOTE
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroSegmentoIncremental()), 5), 9, 13, " ", false, false);
		// CÓDIGO SEGMENTO REG. DETALHE
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "O", 14, 14, " ", false, false);
		// Tipo de Movimento
		// 000 = Indica INCLUSÃO
		// 003 = Indica ESTORNO (somente para retorno)
		// 005 = Indica ALTERAÇÃO
		// 008 = Indica INCLUSÃO COMPROR*
		// 009 = Indica EXCLUSÃO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 15, 17, "0", false, false);		
		//Código de Barras
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoBarra(), 18, 65, " ", false, false);
		// Nome do Favorecido
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(dadosRemessaVO.getNomeFavorecido())), 30), 66,95, " ", true, false);	
		// Data do Vecimento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 96,103, " ", false, false);
		//TIPO DE MOEDA 
		linha = editorOC.adicionarCampoLinhaVersao2(linha,"REA", 104, 106, " ", false, false);
		// QUANTIDADE DE MOEDA
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " " , 107, 121, "0", false, false);
		//VALOR PREVISTO DO PAGAMENTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 122, 136, "0", false, false);
		//DATA DO PAGAMENTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento() , "ddMMyyyy"), 137,144, "0", false, false);
		//VALOR DE EFETIVAÇÃO DO PAGAMENTO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 145,159, "0", false, false);
		//COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 160, 162, " ", false, false);
		//NÚMERO DA NOTA FISCAL
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " " , 163,171, " ", false, false);
		//COMPLEMENTO DE REGISTRO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 172 , 174, " ", false, false);
		// Nº DOCTO ATRIBUÍDO PELA EMPRESA
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoTransmissaoRemessanossonumero(), 175, 194, " ", false, false);
		/*if(Uteis.isAtributoPreenchido(dadosRemessaVO.getCodigoAgrupamentoContasPagar())) {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoAgrupamentoContasPagar(), 175, 194, " ", false, false);
		}else {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(dadosRemessaVO.getNossoNumero().toString()), 175,194, " ", false, false);
		}*/
		// COMPLEMENTO DE REGISTRO 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 195,215, " ", false, false);
		//NÚMERO ATRIBUÍDO PELO BANCO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 216,230, " ", false, false);		
		// Ocorrências para o Retorno
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 231, 240, " ", false, false);
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);
	}
	
	public  void validarDadosParaTodosSegmento(ContaPagarControleRemessaContaPagarVO remessaContaPagarVO) throws ConsistirException{
		if (!Uteis.isAtributoPreenchido(remessaContaPagarVO.getContaPagar().getTipoServicoContaPagar())) {
			throw new ConsistirException("O campo Tipo Serviço (Conta à Pagar) deve ser informado. Para a conta de número: " + remessaContaPagarVO.getContaPagar().getNossoNumero());
		}
		if (!Uteis.isAtributoPreenchido(remessaContaPagarVO.getContaPagar().getTipoLancamentoContaPagar())) {
			throw new ConsistirException("O campo Tipo Lançamento (Conta à Pagar) deve ser informado. Para a conta de número: " + remessaContaPagarVO.getContaPagar().getNossoNumero());
		}
		if (!Uteis.isAtributoPreenchido(remessaContaPagarVO.getContaPagar().getFormaPagamentoVO())) {
			throw new ConsistirException("O campo Forma Pagamento (Conta à Pagar) deve ser informado. Para a conta de número: " + remessaContaPagarVO.getContaPagar().getNossoNumero());
		}
		if (!Uteis.isAtributoPreenchido(remessaContaPagarVO.getContaPagar().getNrDocumento())) {
			throw new ConsistirException("O campo Número do Documento(Conta à Pagar) deve ser informado. Para a conta de número: " + remessaContaPagarVO.getContaPagar().getNossoNumero());
		}
		
	}
	public  void validarDadosSegmentoA(ContaPagarControleRemessaContaPagarVO contaPagar) throws ConsistirException{
		if(Uteis.isAtributoPreenchido(contaPagar.getModalidadeTransferenciaBancariaEnum()) &&  contaPagar.getModalidadeTransferenciaBancariaEnum().isPix()) {
			if (contaPagar.getTipoLancamentoContaPagar().isPixTransferencia() && contaPagar.getModalidadeTransferenciaBancariaEnum().isPix() && !Uteis.isAtributoPreenchido(contaPagar.getFinalidadeTedEnum())) {
				throw new StreamSeiException("O campo Finalidade Pix Favorecido deve ser informado.");
			}
		}else {
			if(!Uteis.isAtributoPreenchido(contaPagar.getBancoRecebimento().getCodigo())){
				throw  new ConsistirException("O campo Banco recebimento Favorecido deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
			}
			if(!Uteis.isAtributoPreenchido(contaPagar.getNumeroAgenciaRecebimento())){
				throw  new ConsistirException("O campo Agência recebimento Favorecido deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
			}
			if(!Uteis.isAtributoPreenchido(contaPagar.getContaCorrenteRecebimento())){
				throw  new ConsistirException("O campo Conta corrente recebimento Favorecido deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());		
			}
			if(!Uteis.isAtributoPreenchido(contaPagar.getNomeFavorecido())){
				throw  new ConsistirException("O campo Nome Favorecido deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
			}
			if(contaPagar.getTipoLancamentoContaPagar().isTransferencia() && contaPagar.getModalidadeTransferenciaBancariaEnum().isDoc() && !Uteis.isAtributoPreenchido(contaPagar.getFinalidadeDocEnum())){
				throw  new ConsistirException("O campo Finalidade Doc Favorecido deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
			}
			if(contaPagar.getTipoLancamentoContaPagar().isTransferencia() && contaPagar.getModalidadeTransferenciaBancariaEnum().isTed() && !Uteis.isAtributoPreenchido(contaPagar.getFinalidadeTedEnum())){
				throw  new ConsistirException("O campo Finalidade Ted Favorecido deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
			}
		}
		
		
	}
	
	
	public  void validarDadosSegmentoB(ContaPagarControleRemessaContaPagarVO contaPagar) throws ConsistirException{
		if(!Uteis.isAtributoPreenchido(contaPagar.getTipoInscricaoFavorecido())){
			throw  new ConsistirException("O campo Tipo Inscrição Favorecido deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getCnpjOuCpfFavorecido())){
			throw  new ConsistirException("O campo Inscrição Favorecido deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
	}
	
	
	public  void validarDadosSegmentoJ(ContaPagarControleRemessaContaPagarVO contaPagar) throws ConsistirException{
		if(!Uteis.isAtributoPreenchido(contaPagar.getCodigoBarra())){
			throw  new ConsistirException("O campo Código Barra deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel1())){
			throw  new ConsistirException("O campo Linha Digitável 1 deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel2())){
			throw  new ConsistirException("O campo Linha Digitável 2 deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel3())){
			throw  new ConsistirException("O campo Linha Digitável 3 deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel4())){
			throw  new ConsistirException("O campo Linha Digitável 4 deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel5())){
			throw  new ConsistirException("O campo Linha Digitável 5 deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel6())){
			throw  new ConsistirException("O campo Linha Digitável 6 deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel7())){
			throw  new ConsistirException("O campo Linha Digitável 7 deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel8())){
			throw  new ConsistirException("O campo Linha Digitável 8 deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
	}
	
	
	public  void validarDadosSegmentoN1(ContaPagarControleRemessaContaPagarVO contaPagar) throws ConsistirException{
		if(!Uteis.isAtributoPreenchido(contaPagar.getCodigoReceitaTributo())){
			throw  new ConsistirException("O campo Código Receita Tributo deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getTipoIdentificacaoContribuinte())){
			throw  new ConsistirException("O campo Tipo Identificação Contribuinte deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getIdentificacaoContribuinte())){
			throw  new ConsistirException("O campo Identificação Contribuinte  deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
	}
	
	
	public  void validarDadosSegmentoN2(ContaPagarControleRemessaContaPagarVO contaPagar) throws ConsistirException{
		if(!Uteis.isAtributoPreenchido(contaPagar.getCodigoReceitaTributo())){
			throw  new ConsistirException("O campo Código Receita Tributo deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getTipoIdentificacaoContribuinte())){
			throw  new ConsistirException("O campo Tipo Identificação Contribuinte deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getIdentificacaoContribuinte())){
			throw  new ConsistirException("O campo Identificação Contribuinte  deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getNumeroReferencia())){
			throw  new ConsistirException("O campo Número de Referencia  deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
	}
	
	
	public void validarDadosSegmentoN3(ContaPagarControleRemessaContaPagarVO contaPagar) throws ConsistirException{
		if(!Uteis.isAtributoPreenchido(contaPagar.getCodigoReceitaTributo())){
			throw  new ConsistirException("O campo Código Receita Tributo deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getTipoIdentificacaoContribuinte())){
			throw  new ConsistirException("O campo Tipo Identificação Contribuinte deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getIdentificacaoContribuinte())){
			throw  new ConsistirException("O campo Identificação Contribuinte  deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getValorReceitaBrutaAcumulada())){
			throw  new ConsistirException("O campo Valor da Receita Bruta Acumulada  deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getPercentualReceitaBrutaAcumulada())){
			throw  new ConsistirException("O campo Percentual da Receita Bruta Acumulada  deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
	}
	
	
	public void validarDadosSegmentoN4(ContaPagarControleRemessaContaPagarVO contaPagar) throws ConsistirException{
		if(!Uteis.isAtributoPreenchido(contaPagar.getCodigoReceitaTributo())){
			throw  new ConsistirException("O campo Código Receita Tributo deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getTipoIdentificacaoContribuinte())){
			throw  new ConsistirException("O campo Tipo Identificação Contribuinte deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getIdentificacaoContribuinte())){
			throw  new ConsistirException("O campo Identificação Contribuinte  deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getInscricaoEstadualFavorecido())){
			throw  new ConsistirException("O campo Inscrição estadual deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getInscricaoMunicipalFavorecido())){
			throw  new ConsistirException("O campo Inscrição Municipal deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getNumeroReferencia())){
			throw  new ConsistirException("O campo Número Declaração   deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
	}
	
	
	public  void validarDadosSegmentoO(ContaPagarControleRemessaContaPagarVO contaPagar) throws ConsistirException{
		if(!Uteis.isAtributoPreenchido(contaPagar.getCodigoBarra())){
			throw  new ConsistirException("O campo Código Barra deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel1())){
			throw  new ConsistirException("O campo Linha Digitável 1 deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel2())){
			throw  new ConsistirException("O campo Linha Digitável 2 deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel3())){
			throw  new ConsistirException("O campo Linha Digitável 3 deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
		if(!Uteis.isAtributoPreenchido(contaPagar.getLinhaDigitavel4())){
			throw  new ConsistirException("O campo Linha Digitável 4 deve ser informado. Para a conta de número: " + contaPagar.getContaPagar().getNossoNumero());
		}
	}
	
	
	
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void addMapaRemessaContaPagarVO(MapaRemessaLoteContaPagarVO mapa){
		int index = 0;
		for (MapaRemessaLoteContaPagarVO objExistente : listaMapaRemessaContaPagarVO) {
			if(objExistente.equalsMapaRemessaLoteContaPagarVO(mapa)){
				listaMapaRemessaContaPagarVO.set(index, mapa);
				return ;
			}
			index++;			
		}
		listaMapaRemessaContaPagarVO.add(mapa);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private MapaRemessaLoteContaPagarVO consultarMapaRemessaContaPagarVO(TipoServicoContaPagarEnum tipoServicoContaPagarEnum , TipoLancamentoContaPagarEnum tipoLancamentoContaPagarEnum){
		for (MapaRemessaLoteContaPagarVO objExistente : listaMapaRemessaContaPagarVO) {
			if(objExistente.equalsMapaRemessaLoteContaPagarVO(tipoServicoContaPagarEnum, tipoLancamentoContaPagarEnum)){
				return objExistente;
			}			
		}
		return new MapaRemessaLoteContaPagarVO();
	}
	
	
	
	
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void processarArquivoRetornoPagar(ControleCobrancaPagarVO controleCobrancaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception{
		BufferedReader reader = null;
		String linha;
		File arquivo = null;
		int registro = 0;
		try {
			if(!Uteis.isAtributoPreenchido(controleCobrancaVO.getArquivoRetornoContaPagar().getNome())){
				throw new Exception("O campo Upload Arquivo deve-se informar.");
			}
            arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + controleCobrancaVO.getArquivoRetornoContaPagar().getPastaBaseArquivoEnum().getValue() + File.separator  + controleCobrancaVO.getArquivoRetornoContaPagar().getNome());
            reader = new BufferedReader(new FileReader(arquivo));
            while ((linha = reader.readLine()) != null) {
                if (linha.equals("")) {
                    continue;
                }
                registro = Uteis.getValorInteiro(linha.substring(7, 8));
                switch (registro) {
                    case 0:
                    	processarHeaderArquivoRetorno(linha, controleCobrancaVO);
                    	break;
                    case 1:
                    	processarHeaderLoteArquivoRetorno(linha, controleCobrancaVO);
                    	break;
                    case 3:
                    	processarDetalheLoteArquivoRetorno(linha, controleCobrancaVO, usuarioVO);
                    	break;
                    case 5:
                    	processarTraillerLoteArquivoRetorno(linha, controleCobrancaVO);
                    	break;
                    case 9:
                    	processarTraillerArquivoRetorno(linha, controleCobrancaVO);
                    	break;
                }
            }
            reader = null;
            Collections.sort(controleCobrancaVO.getContaPagarRegistroArquivoVOs(), OrdenarContaPagarRegistroArquivoEnum.OBSERVACAO_NOME.asc());
        } catch (StringIndexOutOfBoundsException e) {
            throw new ConsistirException("O arquivo selecionado é inválido pois não possui a quantidade adequada de caracteres. Detalhe: " + e.getMessage());
        }
	}
	
	private void processarHeaderArquivoRetorno(String linha, ControleCobrancaPagarVO obj) throws Exception {
		obj.getRegistroHeaderPagarVO().setCodigoBanco(linha.substring(0, 3));
        obj.getRegistroHeaderPagarVO().setLoteServico(Uteis.getValorString(linha.substring(3, 7)));
        obj.getRegistroHeaderPagarVO().setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
        obj.getRegistroHeaderPagarVO().setTipoInscricaoEmpresa(Uteis.getValorInteiro(linha.substring(17, 18)));
        obj.getRegistroHeaderPagarVO().setNumeroInscricaoEmpresa(Uteis.getValorLong(linha.substring(18, 32)));       
        obj.getRegistroHeaderPagarVO().setNumeroAgencia(Uteis.getValorInteiro(linha.substring(52, 57)));      
        obj.getRegistroHeaderPagarVO().setNumeroConta(Uteis.getValorString(linha.substring(58, 70)));       
        obj.getRegistroHeaderPagarVO().setDigitoAgenciaConta(Uteis.getValorInteiro(linha.substring(71, 72)));
        obj.getRegistroHeaderPagarVO().setNomeEmpresa(Uteis.getValorString(linha.substring(72, 102)));
        obj.getRegistroHeaderPagarVO().setNomeBanco(Uteis.getValorString(linha.substring(102, 132)));
        obj.getRegistroHeaderPagarVO().setCodigoRemessaRetorno(Uteis.getValorInteiro(linha.substring(142, 143)));
        obj.getRegistroHeaderPagarVO().setDataGeracaoArquivo(Uteis.getData(linha.substring(143, 151), "ddMMyy"));
        obj.getRegistroHeaderPagarVO().setHoraGeracaoArquivo(Uteis.getValorString(linha.substring(151, 157)));        
        obj.getRegistroHeaderPagarVO().setDensidadeGravacao(Uteis.getValorInteiro(linha.substring(166, 171)));      
        obj.getRegistroHeaderPagarVO().setControleCobrancaPagarVO(obj);
    }
	
	private void processarTraillerArquivoRetorno(String linha, ControleCobrancaPagarVO obj) throws Exception {
		obj.getRegistroTrailerPagarVO().setCodigoBanco(linha.substring(0, 3));
		obj.getRegistroTrailerPagarVO().setLoteServico(Uteis.getValorString(linha.substring(3, 7)));
		obj.getRegistroTrailerPagarVO().setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
		obj.getRegistroTrailerPagarVO().setQuantidadeLote(Uteis.getValorInteiro(linha.substring(17, 23)));
		obj.getRegistroTrailerPagarVO().setQuantidadeRegistro(Uteis.getValorInteiro(linha.substring(23, 29)));
		obj.getRegistroTrailerPagarVO().setControleCobrancaPagarVO(obj);
	}
	
	private void processarHeaderLoteArquivoRetorno(String linha, ControleCobrancaPagarVO obj) throws Exception {
		RegistroHeaderLotePagarVO registroHeaderLote = new RegistroHeaderLotePagarVO();
		registroHeaderLote.setCodigoBanco(linha.substring(0, 3));
		registroHeaderLote.setLoteServico(Uteis.getValorString(linha.substring(3, 7)));
		registroHeaderLote.setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
		registroHeaderLote.setTipoOperacao(Uteis.getValorString(linha.substring(8, 9)));
		registroHeaderLote.setTipoServico(Uteis.getValorString(linha.substring(9, 11)));
		registroHeaderLote.setFormaLancamento(Uteis.getValorString(linha.substring(11, 13)));
		registroHeaderLote.setNumeroVersaoLote(Uteis.getValorString(linha.substring(13, 16)));
		registroHeaderLote.setTipoInscricaoEmpresa(Uteis.getValorInteiro(linha.substring(17, 18)));
		registroHeaderLote.setNumeroInscricaoEmpresa(Uteis.getValorLong(linha.substring(18, 32)));
		registroHeaderLote.setCodigoConvenioBanco(linha.substring(32, 52));
		registroHeaderLote.setNumeroAgencia(Uteis.getValorInteiro(linha.substring(52, 57)));
		//registroHeaderLote.setDigitoAgencia((linha.substring(57, 58)));
		registroHeaderLote.setNumeroConta(Uteis.getValorString(linha.substring(58, 70)));
		//registroHeaderLote.setDigitoConta(Uteis.getValorString(linha.substring(70, 71)));
		registroHeaderLote.setDigitoAgenciaConta(Uteis.getValorInteiro(linha.substring(71, 72)));
		registroHeaderLote.setNomeEmpresa(Uteis.getValorString(linha.substring(72, 102)));		
		registroHeaderLote.setControleCobrancaPagarVO(obj);
		obj.getListaRegistroHeaderLotePagarVO().add(registroHeaderLote);
	}
	
	private void processarTraillerLoteArquivoRetorno(String linha, ControleCobrancaPagarVO obj) throws Exception {
		RegistroHeaderLotePagarVO registroHeaderLote = consultarRegistroHeaderLotePagarVO(obj, linha.substring(3, 7));
		RegistroTrailerLotePagarVO registroTrailerLotePaga  = new RegistroTrailerLotePagarVO();
		registroTrailerLotePaga.setCodigoBanco(linha.substring(0, 3));
		registroTrailerLotePaga.setLoteServico(linha.substring(3, 7));
		registroTrailerLotePaga.setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
		registroTrailerLotePaga.setQuantidadeRegistroLote(Uteis.getValorInteiro(linha.substring(17, 23)));
		registroTrailerLotePaga.setSomatoraRegistroLote(Uteis.getValorDoubleComCasasDecimais(linha.substring(23, 41)));
		registroTrailerLotePaga.setNumeroAvisoDebito(linha.substring(59, 65));
		registroHeaderLote.setRegistroTrailerLotePagarVO(registroTrailerLotePaga);
		addRegistroHeaderLotePagarVO(obj, registroHeaderLote);
	}
	
	private void processarDetalheLoteArquivoRetorno(String linha, ControleCobrancaPagarVO obj, UsuarioVO usuarioVO) throws Exception {
		RegistroHeaderLotePagarVO registroHeaderLote = consultarRegistroHeaderLotePagarVO(obj, linha.substring(3, 7));
		RegistroDetalhePagarVO registroDetalhe = new RegistroDetalhePagarVO();
		registroDetalhe.setRegistroHeaderLotePagarVO(registroHeaderLote);
		registroDetalhe.setCodigoBanco(linha.substring(0, 3));
		registroDetalhe.setLoteServico(linha.substring(3, 7));
		registroDetalhe.setTipoRegistro(Uteis.getValorInteiro(linha.substring(7, 8)));
		registroDetalhe.setNumeroSequencialRegistroLote(linha.substring(8, 13));
		registroDetalhe.setCodigoSegmentoRegistroLote(linha.substring(13, 14));
		if(registroDetalhe.getCodigoSegmentoRegistroLote().equals("A")){
			processarDetalheSegmentoARetorno(linha, registroDetalhe);				
		}else if(registroDetalhe.getCodigoSegmentoRegistroLote().equals("J")){
			processarDetalheSegmentoJRetorno(linha, registroDetalhe);
		}else if(registroDetalhe.getCodigoSegmentoRegistroLote().equals("O")){
			processarDetalheSegmentoORetorno(linha, registroDetalhe);
		}else if(registroDetalhe.getCodigoSegmentoRegistroLote().equals("B")){
			processarDetalheSegmentoBRetorno(linha, registroDetalhe);
		}else if(registroDetalhe.getCodigoSegmentoRegistroLote().equals("Z")){
			processarDetalheSegmentoZRetorno(linha, registroDetalhe);
		}
		if(registroDetalhe.getCodigoSegmentoRegistroLote().equals("A") 
				|| registroDetalhe.getCodigoSegmentoRegistroLote().equals("J")
				|| registroDetalhe.getCodigoSegmentoRegistroLote().equals("O")){			
				getFacadeFactory().getControleRemessaContaPagarFacade().executarGeracaoContaPagarRegistrosArquivoAgrupados(linha ,obj,registroDetalhe,registroHeaderLote ,usuarioVO);				
		}
				
		addRegistroHeaderLotePagarVO(obj, registroHeaderLote);
	}
	
	private void processarDetalheSegmentoARetorno(String linha, RegistroDetalhePagarVO registroDetalhe) throws Exception {
		registroDetalhe.setTipoMovimento(linha.substring(14, 15));	
		registroDetalhe.setCodigoInstrucaoMovimento(linha.substring(15, 17));	
		registroDetalhe.setCodigoCamaraCompesacao(linha.substring(17, 20));
		registroDetalhe.setCodigoBancoFavorecido(Uteis.getValorInteiro(linha.substring(20, 23)));
		registroDetalhe.setNumeroAgenciaFavorecido(Uteis.getValorInteiro(linha.substring(23, 28)));
		registroDetalhe.setDigitoAgenciaFavorecido(linha.substring(28, 29));
		registroDetalhe.setNumeroContaFavorecido(linha.substring(29, 41));
		registroDetalhe.setDigitoContaFavorecido(linha.substring(41, 42));
		registroDetalhe.setDigitoAgenciaContaFavorecido(linha.substring(42, 43));
		registroDetalhe.setNomeFavorecido(linha.substring(43, 73));
		
		if(Uteis.getValorString(linha.substring(90, 93)).matches("[A-Z]*")) {		
		    registroDetalhe.setNossoNumeroContaAgrupada(Uteis.getValorLong(linha.substring(73, 93)) + Uteis.getValorString(linha.substring(90, 93)));
		}else {
			registroDetalhe.setNossoNumero(Uteis.getValorLong(linha.substring(73, 93)));
		}
		registroDetalhe.setDataPagamento(Uteis.getData(linha.substring(93, 101), "ddMMyy"));		
		registroDetalhe.setValorPagamento(Uteis.getValorDoubleComCasasDecimais(linha.substring(119, 134)));
		registroDetalhe.setNumeroDocumento(linha.substring(134, 154));
		registroDetalhe.setDataRealPagamento(Uteis.getData(linha.substring(154, 162), "ddMMyy"));
		registroDetalhe.setValorRealPagamento(Uteis.getValorDoubleComCasasDecimais(linha.substring(162, 177)));
		registroDetalhe.setInformacao2(linha.substring(177, 217));
		registroDetalhe.setFinalidadeDoc(linha.substring(217, 219));
		registroDetalhe.setFinalidadeTed(linha.substring(219, 224));
		registroDetalhe.setCodigoFinalidadeComplementar(linha.substring(224, 226));
		registroDetalhe.setMotivoRegeicao(linha.substring(230, 240));
	}
	
	private void processarDetalheSegmentoJRetorno(String linha, RegistroDetalhePagarVO registroDetalhe) throws Exception {
		registroDetalhe.setTipoMovimento(linha.substring(14, 15));	
		registroDetalhe.setCodigoInstrucaoMovimento(linha.substring(15, 17));	
		registroDetalhe.setCodigoBarra(linha.substring(17, 61));
		registroDetalhe.setNomeFavorecido(linha.substring(61, 91));
		registroDetalhe.setDataVencimento(Uteis.getData(linha.substring(91, 99), "ddMMyy"));
		registroDetalhe.setDataPagamento(Uteis.getData(linha.substring(144, 152), "ddMMyy"));
		registroDetalhe.setValorPagamento(Uteis.getValorDoubleComCasasDecimais(linha.substring(152, 167)));
		if(Uteis.getValorString(linha.substring(199, 202)).matches("[A-Z]*")) {		
			registroDetalhe.setNossoNumeroContaAgrupada(Uteis.getValorLong(linha.substring(182, 202)) + Uteis.getValorString(linha.substring(199, 202)));
		}else {
			registroDetalhe.setNossoNumero(Uteis.getValorLong(linha.substring(182, 202)));
		}
		
		registroDetalhe.setNumeroDocumento(linha.substring(202, 222));
		registroDetalhe.setMotivoRegeicao(linha.substring(230, 240));
	}
	
	private void processarDetalheSegmentoORetorno(String linha, RegistroDetalhePagarVO registroDetalhe) throws Exception {
		registroDetalhe.setTipoMovimento(linha.substring(14, 15));	
		registroDetalhe.setCodigoInstrucaoMovimento(linha.substring(15, 17));	
		registroDetalhe.setCodigoBarra(linha.substring(17, 61));
		registroDetalhe.setNomeFavorecido(linha.substring(61, 91));
		registroDetalhe.setDataVencimento(Uteis.getData(linha.substring(91, 99), "ddMMyy"));
		registroDetalhe.setDataPagamento(Uteis.getData(linha.substring(99, 107), "ddMMyy"));
		registroDetalhe.setValorPagamento(Uteis.getValorDoubleComCasasDecimais(linha.substring(107, 122)));
		if(Uteis.getValorString(linha.substring(140, 142)).matches("[A-Z]*")) {		
			registroDetalhe.setNossoNumeroContaAgrupada(Uteis.getValorLong(linha.substring(122, 142)) + Uteis.getValorString(linha.substring(140, 142)));
		}else {
			registroDetalhe.setNossoNumero(Uteis.getValorLong(linha.substring(122, 142)));
		}		
		registroDetalhe.setNumeroDocumento(linha.substring(142, 162));
		registroDetalhe.setMotivoRegeicao(linha.substring(230, 240));
	}
	
	private void processarDetalheSegmentoBRetorno(String linha, RegistroDetalhePagarVO registroDetalhe) throws Exception {
		registroDetalhe.setTipoInscricaoFavorecido(Uteis.getValorInteiro(linha.substring(17, 18)));	
		registroDetalhe.setNumeroInscricaoFavorecido(Uteis.getValorLong(linha.substring(18, 32)));
		registroDetalhe.setLogradouroFavorecido(linha.substring(32, 62));
		
		registroDetalhe.setNumeroEnderecoFavorecido(linha.substring(62, 67));
		registroDetalhe.setComplementoEnderecoFavorecido(linha.substring(67, 82));
		registroDetalhe.setBairroFavorecido(linha.substring(82, 97));
		registroDetalhe.setCidadeFavorecido(linha.substring(97, 117));
		registroDetalhe.setCepFavorecido(linha.substring(117, 125));
		registroDetalhe.setEstadoFavorecido(linha.substring(125, 127));
		registroDetalhe.setDataVencimento(Uteis.getData(linha.substring(127, 135), "ddMMyy"));
		registroDetalhe.setValorPagamento(Uteis.getValorDoubleComCasasDecimais(linha.substring(135, 150)));
		registroDetalhe.setValorDesconto(Uteis.getValorDoubleComCasasDecimais(linha.substring(165, 180)));
		registroDetalhe.setValorJuro(Uteis.getValorDoubleComCasasDecimais(linha.substring(180, 195)));
		registroDetalhe.setValorMulta(Uteis.getValorDoubleComCasasDecimais(linha.substring(195, 210)));
		registroDetalhe.setHorarioEnvioTed(linha.substring(210, 214));
		registroDetalhe.setCodigoHistoricoParaCredito(linha.substring(225, 229));
		registroDetalhe.setTedInstituicaoFinanceira(linha.substring(231, 232));
		registroDetalhe.setNumeroISPB(linha.substring(232, 240));
	}
	
	private void processarDetalheSegmentoZRetorno(String linha, RegistroDetalhePagarVO registroDetalhe) throws Exception {
		registroDetalhe.setAutenticacaoPagamento(linha.substring(14, 78));	
		registroDetalhe.setProtocoloPagamento(linha.substring(78, 103));
		registroDetalhe.setMotivoRegeicao(linha.substring(230, 240));
	}
	
	private void addRegistroHeaderLotePagarVO(ControleCobrancaPagarVO obj , RegistroHeaderLotePagarVO registroHeaderLote) throws Exception{
		int index = 0;
		for (RegistroHeaderLotePagarVO objExistente : obj.getListaRegistroHeaderLotePagarVO()) {
			if(objExistente.getLoteServico().equals(registroHeaderLote.getLoteServico())){
				obj.getListaRegistroHeaderLotePagarVO().set(index, registroHeaderLote);
				return;
			}
			index++;
		}
	}
	
	private RegistroHeaderLotePagarVO consultarRegistroHeaderLotePagarVO(ControleCobrancaPagarVO obj , String loteServico) throws Exception{
		for (RegistroHeaderLotePagarVO objExistente : obj.getListaRegistroHeaderLotePagarVO()) {
			if(objExistente.getLoteServico().equals(loteServico)){
				return objExistente;
			}
		}
		throw new Exception("Não foi encontrado o numero do lote " + loteServico);
	}

	
	
	
	

	

}
