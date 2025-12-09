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
import negocio.comuns.financeiro.enumerador.OrdenarContaPagarRegistroArquivoEnum;
import negocio.comuns.financeiro.enumerador.TipoLancamentoContaPagarEnum;
import negocio.comuns.financeiro.enumerador.TipoServicoContaPagarEnum;
import negocio.comuns.utilitarias.Comando;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.EditorOC;
import negocio.comuns.utilitarias.Uteis;
import negocio.interfaces.financeiro.remessa.ControleRemessaContaPagarLayoutInterfaceFacade;

/**
 *
 * @author Renato Borges.
 */
public class CaixaEconomicaRemessaContaPagarCNAB240 extends ControleRemessaContaPagarLayoutImpl implements ControleRemessaContaPagarLayoutInterfaceFacade {

	private static final long serialVersionUID = 1785795891974209873L;

	List<MapaRemessaLoteContaPagarVO> listaMapaRemessaContaPagarVO = new ArrayList<MapaRemessaLoteContaPagarVO>();
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public EditorOC executarGeracaoDadosArquivoRemessa(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		EditorOC editorOC = new EditorOC();
		Comando cmd = new Comando();
		separarDadosRemessaPorLote(listaDadosRemessaVOs);
		controleRemessaVO.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(controleRemessaVO.getUnidadeEnsinoVO().getCodigo(), false, usuario));
		controleRemessaVO.setIncrementalMXCP(getFacadeFactory().getControleRemessaMXFacade().consultarIncrementalCPPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), usuario));
		controleRemessaVO.setIncrementalMX(236);
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
			controleRemessaVO.setNumeroIncremental(controleRemessaVO.getNumeroIncremental() + 1);
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
		     		gerarDetalheSegmentoB(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, remessaContaPagarVO);
		     	}else if(remessaContaPagarVO.getTipoLancamentoContaPagar().isLiquidacaoTituloCarteiraCobrancaSantander()
		     			|| remessaContaPagarVO.getTipoLancamentoContaPagar().isLiquidacaoTituloOutroBanco()){		     		
		     		gerarDetalheSegmentoJ(editorOC, cmd, controleRemessaVO, configuracaoFinanceiroVO, remessaContaPagarVO);
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

		// Codigo do banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
		// Lote do servico
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0000", 4, 7, " ", false, false);
		// Codigo do Registro
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 8, 8, " ", false, false);
		// filler espaco em branco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 9, 17, " ", false, false);
		// Tipo da Inscricao da Empresa
		// 0 = Isento / Nao Informado
		// 1 = CPF
		// 2 = CNPJ
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 18, 18, " ", false, false);

		// numero da Inscricao da Empresa
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 19, 32, " ", false, false);
		// Codigo convenio do banco
		String convenio = controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia()+Uteis.preencherComZerosPosicoesVagas(controleRemessaVO.getContaCorrenteVO().getCodigoComunicacaoRemessaCP(), 12);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, convenio, 33, 38, "0", false, false);
		// Parâmetro de Transmissão 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), 39, 40, "0", false, false);
		// Ambiente Cliente 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "P", 41, 41, "0", false, false);
		// Ambiente CAIXA 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 42, 42, "0", false, false);

		// Origem Aplicativo 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 43, 45, " ", false, false);
		// Número de Versão 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 46, 49, "0", false, false);
		// filler espaco em branco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 50, 52, " ", false, false);		
		// Agencia da conta
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), 53, 57, "0", false, false);
		// Digito verificador da Agencia
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 58, 58, " ", false, false);

		// Número da conta 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getNumero(), 59, 70, "0", false, false);
		// Digito verificador da Conta corrente
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getDigito(), 71, 71, " ", false, false);
		// Digito Verificador da Agencia / Conta
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 72, 72, " ", false, false);
		// Nome da Empresa
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(controleRemessaVO.getUnidadeEnsinoVO().getRazaoSocial())), 30), 73, 102, " ", false, false);
		// Nome do Banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "CAIXA", 103, 132, " ", true, false);

		// filler espaco em branco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 133, 142, " ", true, false);
		// Codigo Remessa / Retorno
		// 1 = remessa
		// 2 = retonro
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 143, 143, " ", true, false);
		// Data da Geracao do Arquivo
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyyyy"), 144, 151, " ", false, false);
		// Hora da Geracao do Arquivo
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.gethoraHHMMSS(new Date()), 152, 157, "0", false, false);
		// Numero Sequencial do Arquivo (NSA)
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getIncrementalMXCP()), 6), 158, 163, " ", false, false);

		// Numero da Versao do Layout
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "080", 164, 166, " ", false, false);
		// Densidade de Gravacao Arquivo
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "01600", 167, 171, " ", false, false);
		// Uso Reservado do Banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 172, 191, " ", false, false);
		// Uso Reservado da Empresa
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "REMESSA/PRODUCAO", 192, 211, " ", true, false);

		// Filler espaco em branco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 212, 225, " ", false, false);
		// VAN
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 226, 228, "0", false, false);
		// Ocorrencias para o Retorno
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 229, 240, " ", false, false);
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void gerarDadosTraillerArquivo(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO) throws Exception {
		String linha = "";
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "9999", 4, 7, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 8, 8, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 9, 17, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(controleRemessaVO.getNumeroIncremental()), 18, 23, "0", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(cmd.getNrLinhas() + 1), 6), 24, 29, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 30, 35, "0", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 36, 240, " ", false, false);
		cmd.adicionarLinhaComando(linha, 0);
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDadosHeaderLote(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, MapaRemessaLoteContaPagarVO mapaRemessaContaPagar, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		String linha = "";
		// Codigo do Banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
		// Lote de Servico
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 4), 4, 7, " ", false, false);
		// Tipo de Registro
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 8, 8, " ", false, false);
		// Tipo da Operacao
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "C", 9, 9, " ", false, false);
		// Tipo de Serviço
		linha = editorOC.adicionarCampoLinhaVersao2(linha, mapaRemessaContaPagar.getTipoServicoContaPagar().getValor(), 10, 11, " ", false, false);
		// Forma de lancamento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, mapaRemessaContaPagar.getTipoLancamentoContaPagar().getValor(), 12, 13, " ", false, false);
		// Numero da Versao do Lote
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "041", 14, 16, " ", false, false);
		// filler espaco em branco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 17, 17, " ", false, false);
		// Tipo da Inscricao da Empresa
		// 0 = Isento / Nao Informado
		// 1 = CPF
		// 2 = CNPJ
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 18, 18, " ", false, false);
		// Numero de Inscricao da Empresa
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(controleRemessaVO.getUnidadeEnsinoVO().getCNPJ()), 19, 32, "0", false, false);
		// Codigo convenio do banco
		String convenio = "0104"+controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia();
		linha = editorOC.adicionarCampoLinhaVersao2(linha, convenio, 33, 38, "0", false, false);
		// Tipo do compromisso
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 39, 40, "0", false, false);
		// Código do compromisso
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0001", 41, 44, "0", false, false);
		// Parametro de transmissão
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(controleRemessaVO.getContaCorrenteVO().getCodigoComunicacaoRemessaCP(), 2), 45, 46, "0", false, false);
		// Filler
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 47, 52, " ", false, false);
		// Agencia da conta
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getNumeroAgencia(), 53, 57, "0", false, false);
		// Digito verificador da Agencia
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getAgencia().getDigito(), 58, 58, " ", false, false);
		// Numero da Conta corrente
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getNumero(), 59, 70, "0", false, false);
		// Digito verificador da Conta corrente
		linha = editorOC.adicionarCampoLinhaVersao2(linha, controleRemessaVO.getContaCorrenteVO().getDigito(), 71, 71, " ", false, false);
		// Digito Verificador da Agencia / Conta
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 72, 72, " ", false, false);
		// Nome da Empresa
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(controleRemessaVO.getUnidadeEnsinoVO().getRazaoSocial())), 30), 73, 102, " ", false, false);
		// Informacao 1 / Mensagem
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 103, 142, " ", false, false);
		// Opcional
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 143, 172, " ", false, false);
		// Numero Local Opcional
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 173, 177, "0", false, false);
		// Opcional
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 178, 212, " ", false, false);
		// CEP Opcional
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 213, 217, "0", false, false);
		// Opcional
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 218, 240, " ", false, false);
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDadosTraillerLote(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, MapaRemessaLoteContaPagarVO mapaRemessaContaPagarVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
		String linha = "";
		// Codigo do Banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
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
		// Número Aviso de Débito 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 60, 65, "0", false, false);
		// filler espaco em branco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 66, 230, " ", false, false);
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
		// Codigo do Banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
		// Lote de Servico
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 4), 4, 7, " ", false, false);
		// Tipo de Registro
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
		// Número Sequencial do Registro no Lote
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroSegmentoIncremental()), 5), 9, 13, " ", false, false);
		// Código Segmento do Registro Detalhe
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "A", 14, 14, " ", false, false);
		// Tipo de Movimento
		// 0 = Indica INCLUSÃO
		// 3 = Indica ESTORNO (somente para retorno)
		// 5 = Indica ALTERAÇÃO
		// 8 = Indica INCLUSÃO COMPROR*
		// 9 = Indica EXCLUSÃO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 15, 15, " ", false, false);
		// Código da Instrução para Movimento
		// 00 = Inclusão de Registro Detalhe Liberado
		// 09 = Inclusão do Registro Detalhe Bloqueado Pendente de Autorização
		// 10 = Alteração do Pagamento Liberado para Bloqueado (Bloqueio)
		// 11 = Alteração do Pagamento Bloqueado para Liberado (Desbloqueio)
		// 14 = Autorização do Pagamento
		// 33 = Estorno por Devolução da Câmara Centralizadora (somente Tipo de Movimento = 3)
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 16, 17, " ", false, false);
		// Código Câmara Compensação
		// 000 = CC
		// 018 = TED CIP
		// 810 = TED STR /No caso do Favorecido/Beneficiário da TED ser uma Instituição Financeira, utilizar 810/TED STR.
		// 700 = DOC
		// 888 =TED CIP ou STR Utilizado quando a Instituição Financeira de crédito não possuir Código na Câmara de Compensação. (Nota G030)
		if(dadosRemessaVO.getTipoLancamentoContaPagar().isCreditoContaCorrente() || dadosRemessaVO.getTipoLancamentoContaPagar().isCreditoContaPoupanca() || dadosRemessaVO.getTipoLancamentoContaPagar().isCaixaAutenticacao()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "000", 18, 20, " ", false, false);		
		}else if(dadosRemessaVO.getTipoLancamentoContaPagar().isTransferencia() && dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isDoc()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "700", 18, 20, " ", false, false);
		}else if(dadosRemessaVO.getTipoLancamentoContaPagar().isTransferencia() && dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isTed() && !dadosRemessaVO.isTipoSacadoBanco()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "018", 18, 20, " ", false, false);
		}else if(dadosRemessaVO.getTipoLancamentoContaPagar().isTransferencia() && dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isTed() && dadosRemessaVO.isTipoSacadoBanco()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "810", 18, 20, " ", false, false);
		}else if (dadosRemessaVO.getTipoLancamentoContaPagar().isOrdemPagamento()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 18, 20, " ", false, false);
		}
		// Código do Banco Favorecido
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getBancoRecebimento().getNrBanco(), 21, 23, "0", false, false);
		// Código da Agência Favorecido
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNumeroAgenciaRecebimento(), 24, 28, "0", false, false);
		// Dígito Verificador da Agência
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 29, 29, " ", false, false);
		// Conta Corrente do Favorecido
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getContaCorrenteRecebimento(), 30, 41, "0", false, false);
		// Dígito Verificador da Conta
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getDigitoCorrenteRecebimento(), 42, 42, "0", false, false);
		// Dígito Verificador da Agência/Conta
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 43, 43, " ", false, false);
		// Nome do Favorecido
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(dadosRemessaVO.getNomeFavorecido(), 30), 44, 73, " ", true, false);
		// Nro. do Documento Cliente
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoTransmissaoRemessanossonumero(), 74, 79, "0", false, false);		
        /*if(Uteis.isAtributoPreenchido(dadosRemessaVO.getCodigoTransmissaoRemessanossonumero())) {
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoTransmissaoRemessanossonumero(), 74, 79, "0", false, false);			
	    }else if(Uteis.isAtributoPreenchido(dadosRemessaVO.getCodigoAgrupamentoContasPagar())) {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoAgrupamentoContasPagar(), 74, 79, "0", false, false);
		}else {
		  linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero().toString(), 74, 79, "0", false, false);
		}*/
		
		//linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNossoNumero().toString(), 74, 79, "0", false, false);
		// Filler
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 80, 92, " ", false, false);
		// Tipo conta finalidade TED - 1
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 93, 93, "1", false, false);
		// Data do Pagamento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 94, 101, " ", false, false);
		// Tipo da Moeda
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "BRL", 102, 104, " ", false, false);
		// Quantidade de Moeda
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "000000000000000", 105, 119, " ", false, false);
		// Valor do Pagamento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 120, 134, "0", false, false);
		// Nro. do Documento Banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 135, 143, "0", false, false);
		//linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(dadosRemessaVO.getNrDocumento()), 135, 143, "0", false, false);
		// Filler
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 144, 146, " ", false, false);
		// Número documento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(dadosRemessaVO.getNrDocumento()), 147, 154, "0", false, false);
		// Data Real do Pagamento (Retorno)
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 155, 162, "0", false, false);
		// Valor Real do Pagamento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 163, 177, "0", false, false);
		// Informação 2 / Mensagem
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 178, 217, " ", false, false);
		
		if(dadosRemessaVO.getTipoLancamentoContaPagar().isTransferencia() && dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isDoc()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getFinalidadeDocEnum().getValor(), 218, 219, " ", false, false);		
		}else{
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 218, 219, "0", false, false);
		}

		// Uso FEBRABAN
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 220, 229, " ", false, false);

		// Emissão de Aviso ao Favorecido
		// 0 = Não Emite Aviso
		// 2 = Emite Aviso Somente para o Remetente
		// 5 = Emite Aviso Somente para o Favorecido
		// 6 = Emite Aviso para o Remetente e Favorecido
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 230, 230, "0", false, false);
		// Ocorrências para o Retorno
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 231, 240, " ", false, false);
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDetalheSegmentoB(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaPagarControleRemessaContaPagarVO dadosRemessaVO) throws Exception {
		validarDadosSegmentoB(dadosRemessaVO);
		controleRemessaVO.setNumeroSegmentoIncremental(controleRemessaVO.getNumeroSegmentoIncremental() + 1);
		String linha = "";
		// Codigo do Banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
		// Lote de Servico
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 4), 4, 7, " ", false, false);
		// Tipo de Registro
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
		// Número Sequencial do Registro no Lote
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroSegmentoIncremental()), 5), 9, 13, " ", false, false);
		// Código Segmento do Registro Detalhe
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "B", 14, 14, " ", false, false);
		//Filler espaco em branco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 15, 17, " ", false, false);
		// Tipo de Inscrição do Favorecido 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getTipoInscricaoFavorecido(), 18, 18, " ", false, false);
		//CNPJ/CPF do Favorecido
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(dadosRemessaVO.getCnpjOuCpfFavorecido()), 19, 32, "0", false, false);
		//Logradouro do Favorecido
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(dadosRemessaVO.getLogradouroFavorecido())), 30), 33, 62, " ", true, false);
		//Número do Local do Favorecido 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(dadosRemessaVO.getNumeroEnderecoFavorecido())), 5), 63, 67, " ", true, false);
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
		//Data de Vencimento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 128, 135, " ", false, false);
		//Valor do Documento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 136, 150, "0", false, false);
		//linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValor())), 13), 136, 150, "0", false, false);
		//Valor do Abatimento		
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 151, 165, "0", false, false);
		//Valor do Desconto 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 166, 180, "0", false, false);
		// valor mora
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 181, 195, "0", false, false);
		//valor Multa
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 196, 210, "0", false, false);
		
//		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 151, 165, "0", false, false);
//		//Valor do Desconto 
//		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getDesconto())),13), 166, 180, "0", false, false);
//		// valor mora
//		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getJuro())),13), 181, 195, "0", false, false);
//		//valor Multa
//		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getMulta())),13), 196, 210, "0", false, false);
		//Horário de Envio de TED
		if(dadosRemessaVO.getTipoLancamentoContaPagar().isTransferencia() && dadosRemessaVO.getModalidadeTransferenciaBancariaEnum().isTed()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(Uteis.gethoraHHMM(new Date())), 211, 214, "0", false, false);	
		}else{
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 211, 214, " ", false, false);
		}
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 215, 225, " ", false, false);
		// FEBRABAN
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 226, 240, " ", false, false);

		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDetalheSegmentoJ(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaPagarControleRemessaContaPagarVO dadosRemessaVO) throws Exception {
		validarDadosSegmentoJ(dadosRemessaVO);
		controleRemessaVO.setNumeroSegmentoIncremental(controleRemessaVO.getNumeroSegmentoIncremental() + 1);
		String linha = "";
		// Codigo do Banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
		// Lote de Servico
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 4), 4, 7, " ", false, false);
		// Tipo de Registro
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
		// Número Sequencial do Registro no Lote
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroSegmentoIncremental()), 5), 9, 13, " ", false, false);
		// Código Segmento do Registro Detalhe
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "J", 14, 14, " ", false, false);
		// Tipo de Movimento
		// 0 = Indica INCLUSÃO
		// 3 = Indica ESTORNO (somente para retorno)
		// 5 = Indica ALTERAÇÃO
		// 8 = Indica INCLUSÃO COMPROR*
		// 9 = Indica EXCLUSÃO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 15, 15, " ", false, false);
		// Código da Instrução para Movimento
		// 00 = Inclusão de Registro Detalhe Liberado
		// 09 = Inclusão do Registro Detalhe Bloqueado Pendente de Autorização
		// 10 = Alteração do Pagamento Liberado para Bloqueado (Bloqueio)
		// 11 = Alteração do Pagamento Bloqueado para Liberado (Desbloqueio)
		// 14 = Autorização do Pagamento
		// 33 = Estorno por Devolução da Câmara Centralizadora (somente Tipo de Movimento = 3)
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 16, 17, " ", false, false);
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
		// Data do Pagamento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 145, 152, " ", false, false);
		// Valor do Pagamento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 153, 167, "0", false, false);
		// Quantidade de Moeda
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "000000000000000", 168, 182, " ", false, false);
		// Nro. do Documento Cliente
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoTransmissaoRemessanossonumero(), 183, 188, "0", false, false);
		
		/*if(Uteis.isAtributoPreenchido(dadosRemessaVO.getCodigoTransmissaoRemessanossonumero())) {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoTransmissaoRemessanossonumero(), 183, 188, "0", false, false);
		}else if(Uteis.isAtributoPreenchido(dadosRemessaVO.getCodigoAgrupamentoContasPagar())) {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoAgrupamentoContasPagar(), 183, 188, "0", false, false);
		}else {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(dadosRemessaVO.getNossoNumero().toString()), 183, 188, "0", false, false);
		}*/
		
		//linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(dadosRemessaVO.getNossoNumero().toString()), 183, 188, "0", false, false);
		// Filler
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 189, 202, " ", false, false);
		// Nro. do Documento Banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 203, 211, " ", false, false);
		// Filler
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 212, 222, " ", false, false);
		// Tipo da Moeda
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "09", 223, 224, " ", false, false);
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 225, 240, " ", false, false);
		linha += "\r";
		cmd.adicionarLinhaComando(linha, 0);
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDetalheSegmentoN(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaPagarControleRemessaContaPagarVO dadosRemessaVO) throws Exception {
		controleRemessaVO.setNumeroSegmentoIncremental(controleRemessaVO.getNumeroSegmentoIncremental() + 1);
		String linha = "";
		// Codigo do Banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
		// Lote de Servico
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 4), 4, 7, " ", false, false);
		// Tipo de Registro
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
		// Número Sequencial do Registro no Lote
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroSegmentoIncremental()), 5), 9, 13, " ", false, false);
		// Código Segmento do Registro Detalhe
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 14, 14, " ", false, false);
		// Tipo de Movimento
		// 0 = Indica INCLUSÃO
		// 3 = Indica ESTORNO (somente para retorno)
		// 5 = Indica ALTERAÇÃO
		// 8 = Indica INCLUSÃO COMPROR*
		// 9 = Indica EXCLUSÃO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 15, 15, " ", false, false);
		// Código da Instrução para Movimento
		// 00 = Inclusão de Registro Detalhe Liberado
		// 09 = Inclusão do Registro Detalhe Bloqueado Pendente de Autorização
		// 10 = Alteração do Pagamento Liberado para Bloqueado (Bloqueio)
		// 11 = Alteração do Pagamento Bloqueado para Liberado (Desbloqueio)
		// 14 = Autorização do Pagamento
		// 33 = Estorno por Devolução da Câmara Centralizadora (somente Tipo de Movimento = 3)
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 16, 17, " ", false, false);
		// Nro. do Documento Cliente
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoTransmissaoRemessanossonumero(), 18,37, "0", false, false);
		
		/*if(Uteis.isAtributoPreenchido(dadosRemessaVO.getCodigoTransmissaoRemessanossonumero())) {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoTransmissaoRemessanossonumero(), 18,37, "0", false, false);
		}else if(Uteis.isAtributoPreenchido(dadosRemessaVO.getCodigoAgrupamentoContasPagar())) {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoAgrupamentoContasPagar(), 18,37, "0", false, false);
		}else {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(dadosRemessaVO.getNossoNumero().toString()), 18,37, "0", false, false);
		}*/
		
		// Nro. do Documento Banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 38, 57, "0", false, false);
		// Nome do contribuiente
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(Uteis.removeCaractersEspeciais(Uteis.removerAcentuacao(dadosRemessaVO.getNomeFavorecido())), 30), 58, 87, " ", true, false);
		// Data do Pagamento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 88, 95, " ", false, false);
		// Valor do Pagamento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 96, 110, "0", false, false);
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
		// Código da Receita do Tributo
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoReceitaTributo(), 111, 116, " ", false, false);
		// Tipo de Identificação do Contribuinte
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getTipoIdentificacaoContribuinte().getValor(), 117, 118, "0", false, false);
		// Identificação do Contribuinte
		linha = editorOC.adicionarCampoLinhaVersao2(linha,  dadosRemessaVO.getIdentificacaoContribuinte(), 119, 132, "0", false, false);
		// Codigo de identificação do tributo
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "17", 133, 134, " ", false, false);
		// Mes e Ano de Competencia
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataFatoGerador(), "MMyyyy"), 135, 140, " ", false, false);
		// Valor Previsto do Pagamento do INSS
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 141, 155, "0", false, false);
		// Ocorrências para o Retorno
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 156, 230, " ", false, false);
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
		// Código da Receita do Tributo
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoReceitaTributo(), 111, 116, " ", false, false);
		// Tipo de Identificação do Contribuinte
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getTipoIdentificacaoContribuinte().getValor(), 117, 118, "0", false, false);
		// Identificação do Contribuinte
		linha = editorOC.adicionarCampoLinhaVersao2(linha,  dadosRemessaVO.getIdentificacaoContribuinte(), 119, 132, "0", false, false);
		// Codigo de identificação do tributo
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "16", 133, 134, " ", false, false);
		// Período de Apuração 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataFatoGerador(), "ddMMyyyy"), 135, 142, " ", false, false);
		// Numero de Referencia
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getNumeroReferencia(), 143, 159, " ", false, false);
		//Valor Principal
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 160, 174, "0", false, false);
		//Valor da multa
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 175, 189, " ", false, false);
		//Valor Dos juro/encargos
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 190, 204, " ", false, false);
		
//		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getMulta())), 13), 175, 189, " ", false, false);
//		//Valor Dos juro/encargos
//		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getJuro())), 13), 190, 204, " ", false, false);
		// Data de Vencimento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 205, 212, " ", false, false);
		// Ocorrências para o Retorno
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 213, 230, " ", false, false);
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
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoReceitaTributo(), 111, 116, " ", false, false);
		// Tipo de Identificação do Contribuinte
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getTipoIdentificacaoContribuinte().getValor(), 117, 118, "0", false, false);
		// Identificação do Contribuinte
		linha = editorOC.adicionarCampoLinhaVersao2(linha,  dadosRemessaVO.getIdentificacaoContribuinte(), 119, 132, "0", false, false);
		// Codigo de identificação do tributo
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "18", 133, 134, " ", false, false);
		// Período de Apuração 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataFatoGerador(), "ddMMyyyy"), 135, 142, " ", false, false);
		//Valor da Receita Bruta Acumulada
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorReceitaBrutaAcumulada())), 13), 143, 157, "0", false, false);
		//Percentual  da Receita Bruta Acumulada
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getValorReceitaBrutaAcumulada())),5), 158, 164, "0", false, false);
		//Valor Principal
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 165, 179, "0", false, false);
		//Valor da multa
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 180, 194, " ", false, false);
		//Valor Dos juro/encargos
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 195, 209, " ", false, false);
		//Valor da multa
//		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getMulta())), 13), 180, 194, " ", false, false);
//		//Valor Dos juro/encargos
//		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getJuro())), 13), 195, 209, " ", false, false);
		// Ocorrências para o Retorno
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 210, 230, " ", false, false);
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
		// Código da Receita do Tributo
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoReceitaTributo(), 111, 116, " ", false, false);
		// Tipo de Identificação do Contribuinte
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getTipoIdentificacaoContribuinte().getValor(), 117, 118, "0", false, false);
		// Identificação do Contribuinte
		linha = editorOC.adicionarCampoLinhaVersao2(linha,  dadosRemessaVO.getIdentificacaoContribuinte(), 119, 132, "0", false, false);
		// Codigo de identificação do tributo
		if(dadosRemessaVO.getTipoLancamentoContaPagar().isGareDrSemCodigoBarra()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "23", 133, 134, " ", false, false);
		}else if(dadosRemessaVO.getTipoLancamentoContaPagar().isGareIcmsSemCodigoBarra()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "22", 133, 134, " ", false, false);
		}else if(dadosRemessaVO.getTipoLancamentoContaPagar().isGareItcmdSemCodigoBarra()){
			linha = editorOC.adicionarCampoLinhaVersao2(linha, "24", 133, 134, " ", false, false);
		}
		// Data Vencimento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 135, 142, " ", false, false);
		// Inscrição Estadual / Código do Município / Numero Declaração
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getInscricaoEstadualFavorecido() + dadosRemessaVO.getInscricaoMunicipalFavorecido() + dadosRemessaVO.getNumeroReferencia(), 143, 154, " ", false, false);
		//Divida Ativa / Numero Etiqueta
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 155, 167, "0", false, false);
		// Periodo Referencia
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataFatoGerador(), "MMyyyy"), 168, 173, " ", false, false);
		//Numero da Parcela / Notificação 
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 174, 186, "0", false, false);
		//Valor Principal
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 187, 201, "0", false, false);
		
		//Valor da multa
		linha = editorOC.adicionarCampoLinhaVersao2(linha,"", 202, 215, " ", false, false);
		//Valor Dos juro/encargos
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 216, 229, " ", false, false);
		
		/*//Valor da multa
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getMulta())), 13), 202, 215, " ", false, false);
		//Valor Dos juro/encargos
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getJuro())), 13), 216, 229, " ", false, false);*/
		// filter
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "", 230, 230, " ", false, false);
		
	}
	
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	private void gerarDetalheSegmentoO(EditorOC editorOC, Comando cmd, ControleRemessaContaPagarVO controleRemessaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ContaPagarControleRemessaContaPagarVO dadosRemessaVO) throws Exception {
		validarDadosSegmentoO(dadosRemessaVO);
		controleRemessaVO.setNumeroSegmentoIncremental(controleRemessaVO.getNumeroSegmentoIncremental() + 1);
		String linha = "";
		// Codigo do Banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "104", 1, 3, " ", false, false);
		// Lote de Servico
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroIncremental()), 4), 4, 7, " ", false, false);
		// Tipo de Registro
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "3", 8, 8, " ", false, false);
		// Número Sequencial do Registro no Lote
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(String.valueOf(controleRemessaVO.getNumeroSegmentoIncremental()), 5), 9, 13, " ", false, false);
		// Código Segmento do Registro Detalhe
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "O", 14, 14, " ", false, false);
		// Tipo de Movimento
		// 0 = Indica INCLUSÃO
		// 3 = Indica ESTORNO (somente para retorno)
		// 5 = Indica ALTERAÇÃO
		// 8 = Indica INCLUSÃO COMPROR*
		// 9 = Indica EXCLUSÃO
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 15, 15, " ", false, false);
		// Código da Instrução para Movimento
		// 00 = Inclusão de Registro Detalhe Liberado
		// 09 = Inclusão do Registro Detalhe Bloqueado Pendente de Autorização
		// 10 = Alteração do Pagamento Liberado para Bloqueado (Bloqueio)
		// 11 = Alteração do Pagamento Bloqueado para Liberado (Desbloqueio)
		// 14 = Autorização do Pagamento
		// 33 = Estorno por Devolução da Câmara Centralizadora (somente Tipo de Movimento = 3)
		linha = editorOC.adicionarCampoLinhaVersao2(linha, "00", 16, 17, " ", false, false);
		//Código de Barras
		linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoBarra(), 18, 61, " ", false, false);
		// Nome do Favorecido
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.copiarDelimitandoTamanhoDoTexto(dadosRemessaVO.getNomeFavorecido(), 30), 62, 91, " ", true, false);	
		// Data do Vecimento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 92, 99, " ", false, false);
		// Data do Pagamento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(dadosRemessaVO.getDataVencimento(), "ddMMyyyy"), 100, 107, " ", false, false);
		// Valor do Pagamento
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.preencherComZerosPosicoesVagas(Uteis.removeCaractersEspeciais(Uteis.arrendondarForcando2CadasDecimaisStr(dadosRemessaVO.getPrevisaoValorPagoDescontosMultas())), 13), 108, 122, "0", false, false);
		// Nro. do Documento Cliente
        linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoTransmissaoRemessanossonumero(), 123,142, "0", false, false);
		
		/*if(Uteis.isAtributoPreenchido(dadosRemessaVO.getCodigoTransmissaoRemessanossonumero())) {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoTransmissaoRemessanossonumero(), 123,142, "0", false, false);
		}else if(Uteis.isAtributoPreenchido(dadosRemessaVO.getCodigoAgrupamentoContasPagar())) {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, dadosRemessaVO.getCodigoAgrupamentoContasPagar(), 123, 142, "0", false, false);
		}else {
			linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(dadosRemessaVO.getNossoNumero().toString()), 123, 142, "0", false, false);
		}*/
				
		// Nro. do Documento Banco
		linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removerMascara(dadosRemessaVO.getNrDocumento()), 143, 162, "0", false, false);
		// filler
		linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 163, 230, " ", false, false);
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
	
	@SuppressWarnings("resource")
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void processarArquivoRetornoPagar(ControleCobrancaPagarVO controleCobrancaVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception{
		String linha;
		File arquivo = null;
		int registro = 0;
		try {
			if(!Uteis.isAtributoPreenchido(controleCobrancaVO.getArquivoRetornoContaPagar().getNome())) {
				throw new Exception("O campo Upload Arquivo deve-se informar.");
			}
            arquivo = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoTemp() + File.separator + controleCobrancaVO.getArquivoRetornoContaPagar().getPastaBaseArquivoEnum().getValue() + File.separator  + controleCobrancaVO.getArquivoRetornoContaPagar().getNome());
            BufferedReader reader = new BufferedReader(new FileReader(arquivo));
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
        obj.getRegistroHeaderPagarVO().setCodigoConvenioBanco(Uteis.getValorString(linha.substring(32, 52)));
        obj.getRegistroHeaderPagarVO().setNumeroAgencia(Uteis.getValorInteiro(linha.substring(52, 57)));
        obj.getRegistroHeaderPagarVO().setDigitoAgencia(Uteis.getValorString((linha.substring(57, 58))));
        obj.getRegistroHeaderPagarVO().setNumeroConta(Uteis.getValorString(linha.substring(58, 70)));
        obj.getRegistroHeaderPagarVO().setDigitoConta(Uteis.getValorString(linha.substring(70, 71)));
        obj.getRegistroHeaderPagarVO().setDigitoAgenciaConta(Uteis.getValorInteiro(linha.substring(71, 72)));
        obj.getRegistroHeaderPagarVO().setNomeEmpresa(Uteis.getValorString(linha.substring(72, 102)));
        obj.getRegistroHeaderPagarVO().setNomeBanco(Uteis.getValorString(linha.substring(102, 132)));
        obj.getRegistroHeaderPagarVO().setCodigoRemessaRetorno(Uteis.getValorInteiro(linha.substring(142, 143)));
        obj.getRegistroHeaderPagarVO().setDataGeracaoArquivo(Uteis.getData(linha.substring(143, 151), "ddMMyy"));
        obj.getRegistroHeaderPagarVO().setHoraGeracaoArquivo(Uteis.getValorString(linha.substring(151, 157)));
        obj.getRegistroHeaderPagarVO().setNumeroSequencialArquivo(Uteis.getValorInteiro(linha.substring(157, 163)));
        obj.getRegistroHeaderPagarVO().setNumeroVersaoArquivo(Uteis.getValorInteiro(linha.substring(163, 166)));
        obj.getRegistroHeaderPagarVO().setDensidadeGravacao(Uteis.getValorInteiro(linha.substring(166, 171)));
        obj.getRegistroHeaderPagarVO().setReservadoBanco(Uteis.getValorString(linha.substring(171, 191)));
        obj.getRegistroHeaderPagarVO().setReservadoEmpresa(Uteis.getValorString(linha.substring(191, 211)));
        obj.getRegistroHeaderPagarVO().setOcorrenciaRetonro(Uteis.getValorString(linha.substring(230, 240)));
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
		registroHeaderLote.setDigitoAgencia((linha.substring(57, 58)));
		registroHeaderLote.setNumeroConta(Uteis.getValorString(linha.substring(58, 70)));
		registroHeaderLote.setDigitoConta(Uteis.getValorString(linha.substring(70, 71)));
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
			
			//if(Uteis.isAtributoPreenchido(registroDetalhe.getNossoNumeroContaAgrupada()) ){
				getFacadeFactory().getControleRemessaContaPagarFacade().executarGeracaoContaPagarRegistrosArquivoAgrupados(linha ,obj,registroDetalhe,registroHeaderLote ,usuarioVO);				
		   
			//}else {			
			//ContaPagarRegistroArquivoVO cpra = new ContaPagarRegistroArquivoVO(registroDetalhe.getContaPagarVO(), obj, registroDetalhe ,false);
			//getFacadeFactory().getContaPagarFacade().montarContaPagarPorRegistroDetalhe(cpra, usuarioVO);
		//	obj.getContaPagarRegistroArquivoVOs().add(cpra);
			//}
			
		}
		//if(!Uteis.isAtributoPreenchido(registroDetalhe.getNossoNumeroContaAgrupada())){
		//	registroHeaderLote.getListaRegistroDetalhePagarVO().add(registroDetalhe);
		//}
		
		
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
			registroDetalhe.setNossoNumero(Uteis.getValorLong(linha.substring(73, 79)));
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
