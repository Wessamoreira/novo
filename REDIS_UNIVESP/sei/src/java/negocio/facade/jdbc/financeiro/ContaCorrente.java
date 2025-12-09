package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.compras.FormaPagamentoVO;
import negocio.comuns.financeiro.AgenciaVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaCorrenteVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.financeiro.FluxoCaixaVO;
import negocio.comuns.financeiro.enumerador.AmbienteContaCorrenteEnum;
import negocio.comuns.financeiro.enumerador.TipoAutenticacaoRegistroRemessaOnlineEnum;
import negocio.comuns.financeiro.enumerador.TipoContaCorrenteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.TipoMovimentacaoFinanceira;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContaCorrenteInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>ContaCorrenteVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>ContaCorrenteVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ContaCorrenteVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ContaCorrente extends ControleAcesso implements ContaCorrenteInterfaceFacade {

    protected static String idEntidade;

    public ContaCorrente() throws Exception {
        super();
        setIdEntidade("ContaCorrente");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ContaCorrenteVO</code>.
     */
    public ContaCorrenteVO novo() throws Exception {
        ContaCorrente.incluir(getIdEntidade());
        ContaCorrenteVO obj = new ContaCorrenteVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ContaCorrenteVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ContaCorrenteVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ContaCorrenteVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        try {
            ContaCorrenteVO.validarDados(obj);
            ContaCorrente.incluir(getIdEntidade(), true, usuario);
            if (!obj.getArquivoCertificadoVO().getNome().equals("")) {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoCertificadoVO(), usuario, configuracaoGeralSistemaVO);
			}            
            if (!obj.getArquivoUnidadeCertificadoraVO().getNome().equals("")) {
            	getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoUnidadeCertificadoraVO(), usuario, configuracaoGeralSistemaVO);
            }            
            incluir(obj, "contaCorrente", new AtributoPersistencia()
            		.add("numero", obj.getNumero())
            		.add("dataAbertura", Uteis.getDataJDBC(obj.getDataAbertura()))
            		.add("saldo", obj.getSaldo())
            		.add("agencia", obj.getAgencia())
            		.add("contaCaixa", obj.getContaCaixa())
            		.add("digito", obj.getDigito())
            		.add("carteira", obj.getCarteira())
            		.add("convenio", obj.getConvenio())
            		.add("funcionarioresponsavel", obj.getFuncionarioResponsavel())
            		.add("codigoCedente", obj.getCodigoCedente())
            		.add("requerConfirmacaoMovimentacaoFinanceira", obj.getRequerConfirmacaoMovimentacaoFinanceira())
            		.add("mensagemCarteiraRegistrada", obj.getMensagemCarteiraRegistrada())
            		.add("carteiraRegistrada", obj.getCarteiraRegistrada())
            		.add("utilizarRenegociacao", obj.getUtilizarRenegociacao())
            		.add("situacao", obj.getSituacao())
            		.add("taxaBoleto", obj.getTaxaBoleto())
            		.add("utilizaDadosInformadosCCparaGeracaoBoleto", obj.getUtilizaDadosInformadosCCparaGeracaoBoleto())
            		.add("nome", obj.getNome())
            		.add("razaoSocial", obj.getRazaoSocial())
            		.add("endereco", obj.getEndereco())
            		.add("complemento", obj.getComplemento())
            		.add("setor", obj.getSetor())
            		.add("numeroEnd", obj.getNumeroEnd())
            		.add("cidade", obj.getCidade())
            		.add("CEP", obj.getCEP())
            		.add("CNPJ", obj.getCNPJ())
            		.add("inscEstadual", obj.getInscEstadual())
            		.add("telComercial1", obj.getTelComercial1())
            		.add("telComercial2", obj.getTelComercial2())
            		.add("telComercial3", obj.getTelComercial3())
            		.add("email", obj.getEmail())
            		.add("site", obj.getSite())
            		.add("fax", obj.getFax())
            		.add("mantenedora", obj.getMantenedora())
            		.add("bloquearEmissaoBoleto", obj.getBloquearEmissaoBoleto())
            		.add("digitoCodigoCedente", obj.getDigitoCodigoCedente())
            		.add("codigoTransmissaoRemessa", obj.getCodigoTransmissaoRemessa())
            		.add("cnab", obj.getCnab())
            		.add("utilizaCobrancaPartilhada", obj.getUtilizaCobrancaPartilhada())
            		.add("codigoReceptor1", obj.getCodigoReceptor1())
            		.add("codigoReceptor2", obj.getCodigoReceptor2())
            		.add("codigoReceptor3", obj.getCodigoReceptor3())
            		.add("codigoReceptor4", obj.getCodigoReceptor4())
            		.add("percReceptor1", obj.getPercReceptor1())
            		.add("percReceptor2", obj.getPercReceptor2())
            		.add("percReceptor3", obj.getPercReceptor3())
            		.add("percReceptor4", obj.getPercReceptor4())
            		.add("permiteEnviarBoletoVencido", obj.getPermiteEnviarBoletoVencido())
            		.add("qtdDiasBoletoVencido", obj.getPermiteEnviarBoletoVencido() ? obj.getQtdDiasBoletoVencido() : 0)
            		.add("qtdDiasFiltroRemessa", obj.getQtdDiasFiltroRemessa())
            		.add("qtdDiasBoletoAVencer", !obj.getPermiteEnviarBoletoVencido() ? obj.getQtdDiasBoletoAVencer() : 0)
            		.add("carteiraRegistradaEmissaoBoletoBanco", obj.getCarteiraRegistradaEmissaoBoletoBanco())
            		.add("nomeApresentacaoSistema", obj.getNomeApresentacaoSistema())
            		.add("gerarRemessaSemDescontoAbatido", obj.getGerarRemessaSemDescontoAbatido())
            		.add("controlarBloqueioEmissaoBoleto", obj.getControlarBloqueioEmissaoBoleto())
            		.add("bloquearEmissaoBoletoFimDeSemana", obj.getBloquearEmissaoBoletoFimDeSemana())
            		.add("bloquearEmissaoBoletoFeriado", obj.getBloquearEmissaoBoletoFeriado())
            		.add("bloquearEmissaoBoletoHoraIni", obj.getBloquearEmissaoBoletoHoraIni())
            		.add("bloquearEmissaoBoletoHoraFim", obj.getBloquearEmissaoBoletoHoraFim())
            		.add("mensagemBloqueioEmissaoBoleto", obj.getMensagemBloqueioEmissaoBoleto())
            		.add("formaPgtoTaxaBancaria", obj.getFormaPgtoTaxaBancaria())
            		.add("categoriaDespesaTaxaBancaria", obj.getCategoriaDespesaTaxaBancaria())
            		.add("gerarContaPagarTaxaBancaria", obj.getGerarContaPagarTaxaBancaria())
            		.add("utilizaAbatimentoNoRepasseRemessaBanco", obj.getUtilizaAbatimentoNoRepasseRemessaBanco())
            		.add("qtdDiasBaixaAutTitulo", obj.getQtdDiasBaixaAutTitulo())
            		.add("remessaBoletoEmitido", obj.getRemessaBoletoEmitido())
            		.add("habilitarRegistroRemessaOnline", obj.getHabilitarRegistroRemessaOnline())
            		.add("senhaCertificado", obj.getSenhaCertificado())
            		.add("caminhoCertificado", obj.getCaminhoCertificado())
            		.add("arquivoCertificado", obj.getArquivoCertificadoVO())
            		.add("dataVencimentoCertificado", Uteis.getDataJDBC(obj.getDataVencimentoCertificado()))
            		.add("senhaUnidadeCertificadora", obj.getSenhaUnidadeCertificadora())
            		.add("caminhoUnidadeCertificadora", obj.getCaminhoUnidadeCertificadora())
            		.add("arquivoUnidadeCertificadora", obj.getArquivoUnidadeCertificadoraVO())
            		.add("gerarRemessaMatriculaAut", obj.getGerarRemessaMatriculaAut())
            		.add("gerarRemessaParcelasAut", obj.getGerarRemessaParcelasAut())
            		.add("gerarRemessaNegociacaoAut", obj.getGerarRemessaNegociacaoAut())
            		.add("permitirEmissaoBoletoRemessaOnlineRejeita", obj.getPermitirEmissaoBoletoRemessaOnlineRejeita())
            		.add("gerarRemessaOutrosAut", obj.getGerarRemessaOutrosAut())
            		.add("gerarRemessaRequerimentoAut", obj.getGerarRemessaRequerimentoAut())
            		.add("gerarRemessaBibliotecaAut", obj.getGerarRemessaBibliotecaAut())
            		.add("gerarRemessaInscProcSeletivoAut", obj.getGerarRemessaInscProcSeletivoAut())
            		.add("gerarRemessaDevChequeAut", obj.getGerarRemessaDevChequeAut())
            		.add("gerarRemessaConvenioAut", obj.getGerarRemessaConvenioAut())
            		.add("gerarRemessaContratoReceitaAut", obj.getGerarRemessaContratoReceitaAut())
            		.add("gerarRemessaMateriaDidaticoAut", obj.getGerarRemessaMateriaDidaticoAut())
            		.add("gerarRemessaInclusaoReposicaoAut", obj.getGerarRemessaInclusaoReposicaoAut())
            		.add("codigoComunicacaoRemessaCP", obj.getCodigoComunicacaoRemessaCP())
            		.add("codigoEstacaoRemessa", obj.getCodigoEstacaoRemessa())
            		.add("tipoorigembiblioteca", obj.getTipoOrigemBiblioteca())
            		.add("tipoorigembolsacusteadaconvenio", obj.getTipoOrigemBolsaCusteadaConvenio())
            		.add("tipoorigemcontratoreceita", obj.getTipoOrigemContratoReceita())
            		.add("tipoorigemdevolucaocheque", obj.getTipoOrigemDevolucaoCheque())
            		.add("tipoorigeminclusaoreposicao", obj.getTipoOrigemInclusaoReposicao())
            		.add("tipoorigeminscricaoprocessoseletivo", obj.getTipoOrigemInscricaoProcessoSeletivo())
            		.add("tipoorigemmaterialdidatico", obj.getTipoOrigemMaterialDidatico())
            		.add("tipoorigemmatricula", obj.getTipoOrigemMatricula())
            		.add("tipoorigemmensalidade", obj.getTipoOrigemMensalidade())
            		.add("tipoorigemnegociacao", obj.getTipoOrigemNegociacao())
            		.add("tipoorigemoutros", obj.getTipoOrigemOutros())
            		.add("tipoorigemrequerimento", obj.getTipoOrigemRequerimento())
            		.add("realizarNegociacaoContaReceberVencidaAutomaticamente", obj.getRealizarNegociacaoContaReceberVencidaAutomaticamente())
            		.add("numeroDiaVencidoNegociarContaReceberAutomaticamente", obj.getNumeroDiaVencidoNegociarContaReceberAutomaticamente())
            		.add("numeroDiaAvancarVencimentoContaReceber", obj.getNumeroDiaAvancarVencimentoContaReceber())
            		.add("permiteEmissaoBoletoVencido", obj.getPermiteEmissaoBoletoVencido())
            		.add("tipoContaCorrente", obj.getTipoContaCorrenteEnum().name())
            		.add("utilizaTaxaCartaoDebito", obj.isUtilizaTaxaCartaoDebito())
            		.add("utilizaTaxaCartaoCredito", obj.isUtilizaTaxaCartaoCredito())
            		.add("habilitarProtestoBoleto", obj.getHabilitarProtestoBoleto())
            		.add("qtdDiasProtestoBoleto", obj.getQtdDiasProtestoBoleto())
            		.add("especieTituloBoleto", obj.getEspecieTituloBoleto())
            		.add("ambienteContaCorrenteEnum", obj.getAmbienteContaCorrenteEnum())
            		.add("tipoAutenticacaoRegistroRemessaOnlineEnum", obj.getTipoAutenticacaoRegistroRemessaOnlineEnum())
            		.add("tokenIdRegistroRemessaOnline", obj.getTokenIdRegistroRemessaOnline())
            		.add("tokenKeyRegistroRemessaOnline", obj.getTokenKeyRegistroRemessaOnline())
            		.add("tokenClienteRegistroRemessaOnline", obj.getTokenClienteRegistroRemessaOnline())
            		.add("permiteGerarRemessaOnlineBoletoVencido", obj.getPermiteGerarRemessaOnlineBoletoVencido())
            		.add("gerarRemessaBoletoVencidoMatricula", obj.getGerarRemessaBoletoVencidoMatricula())
            		.add("gerarRemessaBoletoVencidoParcelas", obj.getGerarRemessaBoletoVencidoParcelas())
            		.add("gerarRemessaBoletoVencidoNegociacao", obj.getGerarRemessaBoletoVencidoNegociacao())
            		.add("gerarRemessaBoletoVencidoOutros", obj.getGerarRemessaBoletoVencidoOutros())            		
            		.add("gerarRemessaBoletoVencidoBiblioteca", obj.getGerarRemessaBoletoVencidoBiblioteca())            		
            		.add("gerarRemessaBoletoVencidoDevCheque", obj.getGerarRemessaBoletoVencidoDevCheque())            		            		
            		.add("gerarRemessaBoletoVencidoConvenio", obj.getGerarRemessaBoletoVencidoConvenio())            		            		            		
            		.add("gerarRemessaBoletoVencidoContratoReceita", obj.getGerarRemessaBoletoVencidoContratoReceita())            		            		            		            		
            		.add("gerarRemessaBoletoVencidoMateriaDidatico", obj.getGerarRemessaBoletoVencidoMateriaDidatico())            		            		            		            		            		
            		.add("gerarRemessaBoletoVencidoInclusaoReposicao", obj.getGerarRemessaBoletoVencidoInclusaoReposicao())            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaOnlineMatricula", obj.getQtdeDiasVencidoPermitirRemessaOnlineMatricula())            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaOnlineParcela", obj.getQtdeDiasVencidoPermitirRemessaOnlineParcela())            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaOnlineNegociacao", obj.getQtdeDiasVencidoPermitirRemessaOnlineNegociacao())            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaOnlineOutros", obj.getQtdeDiasVencidoPermitirRemessaOnlineOutros())            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaOnlineBiblioteca", obj.getQtdeDiasVencidoPermitirRemessaOnlineBiblioteca())            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaOnlineDevCheque", obj.getQtdeDiasVencidoPermitirRemessaOnlineDevCheque())            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaConvenio", obj.getQtdeDiasVencidoPermitirRemessaConvenio())            		            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaContratoReceita", obj.getQtdeDiasVencidoPermitirRemessaContratoReceita())            		            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaMaterialDidatico", obj.getQtdeDiasVencidoPermitirRemessaMaterialDidatico())            		            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaInclusaoExclusao", obj.getQtdeDiasVencidoPermitirRemessaInclusaoExclusao())
            		.add("inicialGeracaoNossoNumero", obj.getInicialGeracaoNossoNumero())
            		.add("bloquearEmitirBoletoSemRegistroRemessa", obj.getBloquearEmitirBoletoSemRegistroRemessa())
            		.add("chaveTransacaoClienteRegistroRemessaOnline", obj.getChaveTransacaoClienteRegistroRemessaOnline())
            		.add("chaveAutenticacaoClienteRegistroRemessaOnline",obj.getChaveAutenticacaoClienteRegistroRemessaOnline())
            		.add("dataExpiracaoChaveTransacaoClienteRegistroRemessaOnline",Uteis.getDataJDBCTimestamp(obj.getDataExpiracaoChaveTransacaoClienteRegistroRemessaOnline()))
            		.add("habilitarRegistroPix",obj.getHabilitarRegistroPix())
            		.add("ambienteContaCorrentePix",obj.getAmbienteContaCorrentePix().name())
            		.add("tokenIdRegistroPix",obj.getTokenIdRegistroPix())
            		.add("tokenKeyRegistroPix",obj.getTokenKeyRegistroPix())
            		.add("chavePix",obj.getChavePix())
            		.add("chaveAplicacaoDesenvolvedorPix",obj.getChaveAplicacaoDesenvolvedorPix())
            		.add("formaRecebimentoPadraoPix",obj.getFormaRecebimentoPadraoPix())
            		.add("permiteRecebimentoBoletoVencidoRemessaOnline", obj.getPermiteRecebimentoBoletoVencidoRemessaOnline())
            		.add("numeroDiasLimiteRecebimentoBoletoVencidoRemessaOnline", obj.getNumeroDiasLimiteRecebimentoBoletoVencidoRemessaOnline())
            		.add("variacaoCarteira", obj.getVariacaoCarteira())
            		, usuario);                    
           
            getFacadeFactory().getUnidadeEnsinoContaCorrenteFacade().incluirUnidadeEnsinoContaCorrentes(obj.getCodigo(), obj.getUnidadeEnsinoContaCorrenteVOs());
            getFacadeFactory().getControleRemessaMXFacade().incluirControleRemessaMXContaCorrente(obj.getCodigo(), obj.getControleRemessaMXVO(), usuario);
            obj.setNovoObj(Boolean.FALSE);          
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void alterarSaldoContaCorrente(final Integer contaCorrente, final Double saldo, UsuarioVO usuario) throws Exception {
        final String sql = "UPDATE ContaCorrente set saldo=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setDouble(1, saldo.doubleValue());
                sqlAlterar.setInt(2, contaCorrente.intValue());
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void movimentarSaldoContaCorrente(Integer contaCorrente, String tipoMovimentacao, Double valor, UsuarioVO usuario) throws Exception {
        ContaCorrenteVO obj = consultarPorChavePrimaria(contaCorrente, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        if (obj.getCodigo().intValue() != 0) {
            if (tipoMovimentacao.equals(TipoMovimentacaoFinanceira.ENTRADA.getValor())) {
                obj.setSaldo(Uteis.arredondar((Uteis.arredondar(obj.getSaldo(), 2, 0) + valor), 2, 0));
            } else if (tipoMovimentacao.equals(TipoMovimentacaoFinanceira.SAIDA.getValor())) {
                obj.setSaldo(Uteis.arredondar((Uteis.arredondar(obj.getSaldo(), 2, 0) - valor), 2, 0));
            }
            alterarSaldoContaCorrente(obj.getCodigo(), obj.getSaldo(), usuario);
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ContaCorrenteVO</code>. Sempre
     * utiliza a chave primária da classe como atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do
     * usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ContaCorrenteVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ContaCorrenteVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) throws Exception {
        try {
            ContaCorrenteVO.validarDados(obj);
            ContaCorrente.alterar(getIdEntidade(), true, usuario);
			if (obj.getArquivoCertificadoVO().getCodigo().equals(0)) {
				if (!obj.getArquivoCertificadoVO().getNome().equals("")) {
					getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoCertificadoVO(), usuario, configuracaoGeralSistemaVO);
				}
			} else {
				getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoCertificadoVO(), usuario, configuracaoGeralSistemaVO);
			}
			if (obj.getArquivoUnidadeCertificadoraVO().getCodigo().equals(0)) {
				if (!obj.getArquivoUnidadeCertificadoraVO().getNome().equals("")) {
					getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoUnidadeCertificadoraVO(), usuario, configuracaoGeralSistemaVO);
				}
			} else {
				getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoUnidadeCertificadoraVO(), usuario, configuracaoGeralSistemaVO);
			}
			
			alterar(obj, "contaCorrente", new AtributoPersistencia()
            		.add("numero", obj.getNumero())
            		.add("dataAbertura", Uteis.getDataJDBC(obj.getDataAbertura()))
            		.add("saldo", obj.getSaldo())
            		.add("agencia", obj.getAgencia())
            		.add("contaCaixa", obj.getContaCaixa())
            		.add("digito", obj.getDigito())
            		.add("carteira", obj.getCarteira())
            		.add("convenio", obj.getConvenio())
            		.add("funcionarioresponsavel", obj.getFuncionarioResponsavel())
            		.add("codigoCedente", obj.getCodigoCedente())
            		.add("requerConfirmacaoMovimentacaoFinanceira", obj.getRequerConfirmacaoMovimentacaoFinanceira())
            		.add("mensagemCarteiraRegistrada", obj.getMensagemCarteiraRegistrada())
            		.add("carteiraRegistrada", obj.getCarteiraRegistrada())
            		.add("utilizarRenegociacao", obj.getUtilizarRenegociacao())
            		.add("situacao", obj.getSituacao())
            		.add("taxaBoleto", obj.getTaxaBoleto())
            		.add("utilizaDadosInformadosCCparaGeracaoBoleto", obj.getUtilizaDadosInformadosCCparaGeracaoBoleto())
            		.add("nome", obj.getNome())
            		.add("razaoSocial", obj.getRazaoSocial())
            		.add("endereco", obj.getEndereco())
            		.add("complemento", obj.getComplemento())
            		.add("setor", obj.getSetor())
            		.add("numeroEnd", obj.getNumeroEnd())
            		.add("cidade", obj.getCidade())
            		.add("CEP", obj.getCEP())
            		.add("CNPJ", obj.getCNPJ())
            		.add("inscEstadual", obj.getInscEstadual())
            		.add("telComercial1", obj.getTelComercial1())
            		.add("telComercial2", obj.getTelComercial2())
            		.add("telComercial3", obj.getTelComercial3())
            		.add("email", obj.getEmail())
            		.add("site", obj.getSite())
            		.add("fax", obj.getFax())
            		.add("mantenedora", obj.getMantenedora())
            		.add("bloquearEmissaoBoleto", obj.getBloquearEmissaoBoleto())
            		.add("digitoCodigoCedente", obj.getDigitoCodigoCedente())
            		.add("codigoTransmissaoRemessa", obj.getCodigoTransmissaoRemessa())
            		.add("cnab", obj.getCnab())
            		.add("utilizaCobrancaPartilhada", obj.getUtilizaCobrancaPartilhada())
            		.add("codigoReceptor1", obj.getCodigoReceptor1())
            		.add("codigoReceptor2", obj.getCodigoReceptor2())
            		.add("codigoReceptor3", obj.getCodigoReceptor3())
            		.add("codigoReceptor4", obj.getCodigoReceptor4())
            		.add("percReceptor1", obj.getPercReceptor1())
            		.add("percReceptor2", obj.getPercReceptor2())
            		.add("percReceptor3", obj.getPercReceptor3())
            		.add("percReceptor4", obj.getPercReceptor4())
            		.add("permiteEnviarBoletoVencido", obj.getPermiteEnviarBoletoVencido())
            		.add("qtdDiasBoletoVencido", obj.getPermiteEnviarBoletoVencido() ? obj.getQtdDiasBoletoVencido() : 0)
            		.add("qtdDiasFiltroRemessa", obj.getQtdDiasFiltroRemessa())
            		.add("qtdDiasBoletoAVencer", !obj.getPermiteEnviarBoletoVencido() ? obj.getQtdDiasBoletoAVencer() : 0)
            		.add("carteiraRegistradaEmissaoBoletoBanco", obj.getCarteiraRegistradaEmissaoBoletoBanco())
            		.add("nomeApresentacaoSistema", obj.getNomeApresentacaoSistema())
            		.add("gerarRemessaSemDescontoAbatido", obj.getGerarRemessaSemDescontoAbatido())
            		.add("controlarBloqueioEmissaoBoleto", obj.getControlarBloqueioEmissaoBoleto())
            		.add("bloquearEmissaoBoletoFimDeSemana", obj.getBloquearEmissaoBoletoFimDeSemana())
            		.add("bloquearEmissaoBoletoFeriado", obj.getBloquearEmissaoBoletoFeriado())
            		.add("bloquearEmissaoBoletoHoraIni", obj.getBloquearEmissaoBoletoHoraIni())
            		.add("bloquearEmissaoBoletoHoraFim", obj.getBloquearEmissaoBoletoHoraFim())
            		.add("mensagemBloqueioEmissaoBoleto", obj.getMensagemBloqueioEmissaoBoleto())
            		.add("formaPgtoTaxaBancaria", obj.getFormaPgtoTaxaBancaria())
            		.add("categoriaDespesaTaxaBancaria", obj.getCategoriaDespesaTaxaBancaria())
            		.add("gerarContaPagarTaxaBancaria", obj.getGerarContaPagarTaxaBancaria())
            		.add("utilizaAbatimentoNoRepasseRemessaBanco", obj.getUtilizaAbatimentoNoRepasseRemessaBanco())
            		.add("qtdDiasBaixaAutTitulo", obj.getQtdDiasBaixaAutTitulo())
            		.add("remessaBoletoEmitido", obj.getRemessaBoletoEmitido())
            		.add("habilitarRegistroRemessaOnline", obj.getHabilitarRegistroRemessaOnline())
            		.add("senhaCertificado", obj.getSenhaCertificado())
            		.add("caminhoCertificado", obj.getCaminhoCertificado())
            		.add("arquivoCertificado", obj.getArquivoCertificadoVO())
            		.add("dataVencimentoCertificado", Uteis.getDataJDBC(obj.getDataVencimentoCertificado()))
            		.add("senhaUnidadeCertificadora", obj.getSenhaUnidadeCertificadora())
            		.add("caminhoUnidadeCertificadora", obj.getCaminhoUnidadeCertificadora())
            		.add("arquivoUnidadeCertificadora", obj.getArquivoUnidadeCertificadoraVO())
            		.add("gerarRemessaMatriculaAut", obj.getGerarRemessaMatriculaAut())
            		.add("gerarRemessaParcelasAut", obj.getGerarRemessaParcelasAut())
            		.add("gerarRemessaNegociacaoAut", obj.getGerarRemessaNegociacaoAut())
            		.add("permitirEmissaoBoletoRemessaOnlineRejeita", obj.getPermitirEmissaoBoletoRemessaOnlineRejeita())
            		.add("gerarRemessaOutrosAut", obj.getGerarRemessaOutrosAut())
            		.add("gerarRemessaRequerimentoAut", obj.getGerarRemessaRequerimentoAut())
            		.add("gerarRemessaBibliotecaAut", obj.getGerarRemessaBibliotecaAut())
            		.add("gerarRemessaInscProcSeletivoAut", obj.getGerarRemessaInscProcSeletivoAut())
            		.add("gerarRemessaDevChequeAut", obj.getGerarRemessaDevChequeAut())
            		.add("gerarRemessaConvenioAut", obj.getGerarRemessaConvenioAut())
            		.add("gerarRemessaContratoReceitaAut", obj.getGerarRemessaContratoReceitaAut())
            		.add("gerarRemessaMateriaDidaticoAut", obj.getGerarRemessaMateriaDidaticoAut())
            		.add("gerarRemessaInclusaoReposicaoAut", obj.getGerarRemessaInclusaoReposicaoAut())
            		.add("codigoComunicacaoRemessaCP", obj.getCodigoComunicacaoRemessaCP())
            		.add("codigoEstacaoRemessa", obj.getCodigoEstacaoRemessa())
            		.add("tipoorigembiblioteca", obj.getTipoOrigemBiblioteca())
            		.add("tipoorigembolsacusteadaconvenio", obj.getTipoOrigemBolsaCusteadaConvenio())
            		.add("tipoorigemcontratoreceita", obj.getTipoOrigemContratoReceita())
            		.add("tipoorigemdevolucaocheque", obj.getTipoOrigemDevolucaoCheque())
            		.add("tipoorigeminclusaoreposicao", obj.getTipoOrigemInclusaoReposicao())
            		.add("tipoorigeminscricaoprocessoseletivo", obj.getTipoOrigemInscricaoProcessoSeletivo())
            		.add("tipoorigemmaterialdidatico", obj.getTipoOrigemMaterialDidatico())
            		.add("tipoorigemmatricula", obj.getTipoOrigemMatricula())
            		.add("tipoorigemmensalidade", obj.getTipoOrigemMensalidade())
            		.add("tipoorigemnegociacao", obj.getTipoOrigemNegociacao())
            		.add("tipoorigemoutros", obj.getTipoOrigemOutros())
            		.add("tipoorigemrequerimento", obj.getTipoOrigemRequerimento())
            		.add("realizarNegociacaoContaReceberVencidaAutomaticamente", obj.getRealizarNegociacaoContaReceberVencidaAutomaticamente())
            		.add("numeroDiaVencidoNegociarContaReceberAutomaticamente", obj.getNumeroDiaVencidoNegociarContaReceberAutomaticamente())
            		.add("numeroDiaAvancarVencimentoContaReceber", obj.getNumeroDiaAvancarVencimentoContaReceber())
            		.add("permiteEmissaoBoletoVencido", obj.getPermiteEmissaoBoletoVencido())
            		.add("tipoContaCorrente", obj.getTipoContaCorrenteEnum().name())
            		.add("utilizaTaxaCartaoDebito", obj.isUtilizaTaxaCartaoDebito())
            		.add("utilizaTaxaCartaoCredito", obj.isUtilizaTaxaCartaoCredito())
            		.add("habilitarProtestoBoleto", obj.getHabilitarProtestoBoleto())
            		.add("qtdDiasProtestoBoleto", obj.getQtdDiasProtestoBoleto())
            		.add("especieTituloBoleto", obj.getEspecieTituloBoleto())
            		.add("ambienteContaCorrenteEnum", obj.getAmbienteContaCorrenteEnum())
            		.add("tipoAutenticacaoRegistroRemessaOnlineEnum", obj.getTipoAutenticacaoRegistroRemessaOnlineEnum())
            		.add("tokenIdRegistroRemessaOnline", obj.getTokenIdRegistroRemessaOnline())
            		.add("tokenKeyRegistroRemessaOnline", obj.getTokenKeyRegistroRemessaOnline())
            		.add("tokenClienteRegistroRemessaOnline", obj.getTokenClienteRegistroRemessaOnline())    
            		.add("permiteGerarRemessaOnlineBoletoVencido", obj.getPermiteGerarRemessaOnlineBoletoVencido())
            		.add("gerarRemessaBoletoVencidoMatricula", obj.getGerarRemessaBoletoVencidoMatricula())
            		.add("gerarRemessaBoletoVencidoParcelas", obj.getGerarRemessaBoletoVencidoParcelas())
            		.add("gerarRemessaBoletoVencidoNegociacao", obj.getGerarRemessaBoletoVencidoNegociacao())
            		.add("gerarRemessaBoletoVencidoOutros", obj.getGerarRemessaBoletoVencidoOutros())            		
            		.add("gerarRemessaBoletoVencidoBiblioteca", obj.getGerarRemessaBoletoVencidoBiblioteca())            		
            		.add("gerarRemessaBoletoVencidoDevCheque", obj.getGerarRemessaBoletoVencidoDevCheque())            		            		
            		.add("gerarRemessaBoletoVencidoConvenio", obj.getGerarRemessaBoletoVencidoConvenio())            		            		            		
            		.add("gerarRemessaBoletoVencidoContratoReceita", obj.getGerarRemessaBoletoVencidoContratoReceita())            		            		            		            		
            		.add("gerarRemessaBoletoVencidoMateriaDidatico", obj.getGerarRemessaBoletoVencidoMateriaDidatico())            		            		            		            		            		
            		.add("gerarRemessaBoletoVencidoInclusaoReposicao", obj.getGerarRemessaBoletoVencidoInclusaoReposicao())            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaOnlineMatricula", obj.getQtdeDiasVencidoPermitirRemessaOnlineMatricula())            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaOnlineParcela", obj.getQtdeDiasVencidoPermitirRemessaOnlineParcela())            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaOnlineNegociacao", obj.getQtdeDiasVencidoPermitirRemessaOnlineNegociacao())            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaOnlineOutros", obj.getQtdeDiasVencidoPermitirRemessaOnlineOutros())            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaOnlineBiblioteca", obj.getQtdeDiasVencidoPermitirRemessaOnlineBiblioteca())            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaOnlineDevCheque", obj.getQtdeDiasVencidoPermitirRemessaOnlineDevCheque())            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaConvenio", obj.getQtdeDiasVencidoPermitirRemessaConvenio())            		            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaContratoReceita", obj.getQtdeDiasVencidoPermitirRemessaContratoReceita())            		            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaMaterialDidatico", obj.getQtdeDiasVencidoPermitirRemessaMaterialDidatico())            		            		            		            		            		            		            		
            		.add("qtdeDiasVencidoPermitirRemessaInclusaoExclusao", obj.getQtdeDiasVencidoPermitirRemessaInclusaoExclusao())
            		.add("inicialGeracaoNossoNumero", obj.getInicialGeracaoNossoNumero())
            		.add("bloquearEmitirBoletoSemRegistroRemessa", obj.getBloquearEmitirBoletoSemRegistroRemessa())
            		.add("chaveTransacaoClienteRegistroRemessaOnline", obj.getChaveTransacaoClienteRegistroRemessaOnline())
            		.add("chaveAutenticacaoClienteRegistroRemessaOnline",obj.getChaveAutenticacaoClienteRegistroRemessaOnline())
            		.add("dataExpiracaoChaveTransacaoClienteRegistroRemessaOnline",obj.getDataExpiracaoChaveTransacaoClienteRegistroRemessaOnline())
            		.add("habilitarRegistroPix",obj.getHabilitarRegistroPix())
            		.add("ambienteContaCorrentePix",obj.getAmbienteContaCorrentePix().name())
            		.add("tokenIdRegistroPix",obj.getTokenIdRegistroPix())
            		.add("tokenKeyRegistroPix",obj.getTokenKeyRegistroPix())
            		.add("chavePix",obj.getChavePix())
            		.add("chaveAplicacaoDesenvolvedorPix",obj.getChaveAplicacaoDesenvolvedorPix())
            		.add("formaRecebimentoPadraoPix",obj.getFormaRecebimentoPadraoPix())
            		.add("permiteRecebimentoBoletoVencidoRemessaOnline", obj.getPermiteRecebimentoBoletoVencidoRemessaOnline())
            		.add("numeroDiasLimiteRecebimentoBoletoVencidoRemessaOnline", obj.getNumeroDiasLimiteRecebimentoBoletoVencidoRemessaOnline())
            		.add("variacaoCarteira", obj.getVariacaoCarteira())
            		, new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);   

            getFacadeFactory().getUnidadeEnsinoContaCorrenteFacade().alterarUnidadeEnsinoContaCorrentes(obj.getCodigo(), obj.getUnidadeEnsinoContaCorrenteVOs());
            getFacadeFactory().getControleRemessaMXFacade().alterarControleRemessaMXContaCorrente(obj.getCodigo(), obj.getControleRemessaMXVO(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ContaCorrenteVO</code>. Sempre localiza o
     * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ContaCorrenteVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ContaCorrenteVO obj, UsuarioVO usuario) throws Exception {
        try {
            ContaCorrente.excluir(getIdEntidade(), true, usuario);
            String sql = "DELETE FROM ContaCorrente WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

   /* *//**
     * Responsável por realizar uma consulta de <code>ContaCorrente</code> através do valor do atributo
     * <code>nome</code> da classe <code>UnidadeEnsino</code> Faz uso da operação <code>montarDadosConsulta</code> que
     * realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>ContaCorrenteVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     *//*
    public List<ContaCorrenteVO> consultarPorNomeUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT distinct cc.* FROM ContaCorrente cc ");
        sqlStr.append("INNER JOIN unidadeEnsinoContaCorrente uecc ON cc.codigo = uecc.contacorrente ");
        sqlStr.append("INNER JOIN unidadeEnsino ue ON uecc.unidadeensino = ue.codigo ");
        sqlStr.append("WHERE UPPER(sem_acentos( ue.nome )) ilike(sem_acentos('").append(valorConsulta.toUpperCase()).append("%')) ");
        if (unidadeEnsino != 0) {
            sqlStr.append(" AND ue.codigo = ").append(unidadeEnsino);
        }
        //sqlStr.append(" ORDER BY ue.nome");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
        } finally {
            sqlStr = null;
        }
    }*/

    public List<ContaCorrenteVO> consultaRapidaPorBancoControleRemessaNivelComboBox(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT contaCorrente.codigo, contacorrente.gerarRemessaSemDescontoAbatido, contaCorrente.numero, contacorrente.carteira, contacorrente.variacaoCarteira , contaCorrente.digito, contaCorrente.taxaBoleto, contaCorrente.codigoTransmissaoRemessa, contaCorrente.cnab, agencia.numeroAgencia, ");
        sqlStr.append(" contacorrente.utilizaCobrancaPartilhada, contacorrente.codigoReceptor1, contacorrente.codigoReceptor2, contacorrente.codigoReceptor3, contacorrente.codigoReceptor4, contacorrente.percReceptor1, contacorrente.percReceptor2, contacorrente.percReceptor3, contacorrente.percReceptor4, ");
        sqlStr.append(" agencia.numero AS \"agencia.numero\", agencia.codigo AS \"agencia.codigo\", banco.nrbanco,contacorrente.nomeApresentacaoSistema,   ");
        sqlStr.append(" contacorrente.tipoContaCorrente AS \"contacorrente.tipoContaCorrente\" ");
        sqlStr.append(" FROM contaCorrente ");
        sqlStr.append(" INNER JOIN unidadeEnsinoContaCorrente uecc ON uecc.contacorrente = contaCorrente.codigo ");
        sqlStr.append(" INNER JOIN agencia ON agencia.codigo = contaCorrente.agencia");
        sqlStr.append(" INNER JOIN banco ON agencia.banco = banco.codigo");
        sqlStr.append(" WHERE agencia.banco = ").append(valorConsulta).append(" ");
        if (unidadeEnsino != 0) {
            sqlStr.append(" AND uecc.unidadeEnsino = ").append(unidadeEnsino);
        }
        sqlStr.append(" ORDER BY contaCorrente.numero ");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            List vetResultado = new ArrayList(0);
            while (tabelaResultado.next()) {
                ContaCorrenteVO obj = new ContaCorrenteVO();
                obj.setCodigo(tabelaResultado.getInt("codigo"));
                obj.setNomeApresentacaoSistema(tabelaResultado.getString("nomeApresentacaoSistema"));
                obj.setGerarRemessaSemDescontoAbatido(tabelaResultado.getBoolean("gerarRemessaSemDescontoAbatido"));
                obj.setNumero(tabelaResultado.getString("numero"));
                obj.setCarteira(tabelaResultado.getString("carteira"));
                obj.setVariacaoCarteira(tabelaResultado.getString("variacaocarteira"));
                obj.setDigito(tabelaResultado.getString("digito"));
                obj.setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(tabelaResultado.getString("contacorrente.tipoContaCorrente")));
                obj.setTaxaBoleto(tabelaResultado.getDouble("taxaBoleto"));
                obj.getAgencia().setNumeroAgencia(tabelaResultado.getString("numeroAgencia"));
                obj.setCodigoTransmissaoRemessa(tabelaResultado.getString("codigoTransmissaoRemessa"));                
                obj.setCnab(tabelaResultado.getString("cnab"));                
                obj.setUtilizaCobrancaPartilhada(tabelaResultado.getBoolean("utilizaCobrancaPartilhada"));                
                obj.setCodigoReceptor1(tabelaResultado.getString("codigoReceptor1"));                
                obj.setCodigoReceptor2(tabelaResultado.getString("codigoReceptor2"));                
                obj.setCodigoReceptor3(tabelaResultado.getString("codigoReceptor3"));                
                obj.setCodigoReceptor4(tabelaResultado.getString("codigoReceptor4"));                
                obj.setPercReceptor1(tabelaResultado.getDouble("percReceptor1"));                
                obj.setPercReceptor2(tabelaResultado.getDouble("percReceptor2"));                
                obj.setPercReceptor3(tabelaResultado.getDouble("percReceptor3"));                
                obj.setPercReceptor4(tabelaResultado.getDouble("percReceptor4"));                
                obj.getAgencia().setNumero(tabelaResultado.getString("agencia.numero"));
                obj.getAgencia().setCodigo(tabelaResultado.getInt("agencia.codigo"));
                obj.getAgencia().getBanco().setNrBanco(tabelaResultado.getString("nrBanco"));
                vetResultado.add(obj);
            }
            return vetResultado;
        } finally {
            sqlStr = null;
        }
    }

    public List<ContaCorrenteVO> consultaRapidaPorBancoControleRemessaNivelComboBox(String nrBanco, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT contaCorrente.codigo, contacorrente.gerarRemessaSemDescontoAbatido, contaCorrente.numero, contacorrente.carteira,  contacorrente.variacaoCarteira ,  contaCorrente.digito, contaCorrente.taxaBoleto, contaCorrente.codigoTransmissaoRemessa, contaCorrente.cnab, agencia.numeroAgencia, ");
    	sqlStr.append(" contacorrente.utilizaCobrancaPartilhada, contacorrente.codigoReceptor1, contacorrente.codigoReceptor2, contacorrente.codigoReceptor3, contacorrente.codigoReceptor4, contacorrente.percReceptor1, contacorrente.percReceptor2, contacorrente.percReceptor3, contacorrente.percReceptor4, ");
    	sqlStr.append(" agencia.numero AS \"agencia.numero\", agencia.codigo AS \"agencia.codigo\", banco.nrbanco,contacorrente.nomeApresentacaoSistema,  ");
    	sqlStr.append(" contacorrente.tipoContaCorrente AS \"contacorrente.tipoContaCorrente\" ");
        sqlStr.append(" FROM contaCorrente ");
    	sqlStr.append(" INNER JOIN unidadeEnsinoContaCorrente uecc ON uecc.contacorrente = contaCorrente.codigo ");
    	sqlStr.append(" INNER JOIN agencia ON agencia.codigo = contaCorrente.agencia");
    	sqlStr.append(" INNER JOIN banco ON agencia.banco = banco.codigo");
    	sqlStr.append(" WHERE banco.nrBanco = '").append(nrBanco).append("' ");
    	if (unidadeEnsino != 0) {
    		sqlStr.append(" AND uecc.unidadeEnsino = ").append(unidadeEnsino);
    	}
    	sqlStr.append(" ORDER BY contaCorrente.numero ");
    	try {
    		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    		List vetResultado = new ArrayList(0);
    		while (tabelaResultado.next()) {
    			ContaCorrenteVO obj = new ContaCorrenteVO();
    			obj.setCodigo(tabelaResultado.getInt("codigo"));
    			obj.setNomeApresentacaoSistema(tabelaResultado.getString("nomeApresentacaoSistema"));
                obj.setGerarRemessaSemDescontoAbatido(tabelaResultado.getBoolean("gerarRemessaSemDescontoAbatido"));
    			obj.setNumero(tabelaResultado.getString("numero"));
    			obj.setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(tabelaResultado.getString("contacorrente.tipoContaCorrente")));
    			obj.setCarteira(tabelaResultado.getString("carteira"));
    			 obj.setVariacaoCarteira(tabelaResultado.getString("variacaocarteira"));
    			obj.setDigito(tabelaResultado.getString("digito"));
    			obj.setTaxaBoleto(tabelaResultado.getDouble("taxaBoleto"));
    			obj.getAgencia().setNumeroAgencia(tabelaResultado.getString("numeroAgencia"));
    			obj.setCodigoTransmissaoRemessa(tabelaResultado.getString("codigoTransmissaoRemessa"));                
    			obj.setCnab(tabelaResultado.getString("cnab"));                
                obj.setUtilizaCobrancaPartilhada(tabelaResultado.getBoolean("utilizaCobrancaPartilhada"));                
                obj.setCodigoReceptor1(tabelaResultado.getString("codigoReceptor1"));                
                obj.setCodigoReceptor2(tabelaResultado.getString("codigoReceptor2"));                
                obj.setCodigoReceptor3(tabelaResultado.getString("codigoReceptor3"));                
                obj.setCodigoReceptor4(tabelaResultado.getString("codigoReceptor4"));                
                obj.setPercReceptor1(tabelaResultado.getDouble("percReceptor1"));                
                obj.setPercReceptor2(tabelaResultado.getDouble("percReceptor2"));                
                obj.setPercReceptor3(tabelaResultado.getDouble("percReceptor3"));                
                obj.setPercReceptor4(tabelaResultado.getDouble("percReceptor4"));                
    			obj.getAgencia().setNumero(tabelaResultado.getString("agencia.numero"));
    			obj.getAgencia().setCodigo(tabelaResultado.getInt("agencia.codigo"));
    			obj.getAgencia().getBanco().setNrBanco(tabelaResultado.getString("nrBanco"));
    			vetResultado.add(obj);
    		}
    		return vetResultado;
    	} finally {
    		sqlStr = null;
    	}
    }

	
    /**
     * Responsável por realizar uma consulta de <code>ContaCorrente</code> através do valor do atributo
     * <code>numeroAgencia</code> da classe <code>Agencia</code> Faz uso da operação <code>montarDadosConsulta</code>
     * que realiza o trabalho de prerarar o List resultante.
     *
     * @return List Contendo vários objetos da classe <code>ContaCorrenteVO</code> resultantes da consulta.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ContaCorrenteVO> consultarPorNumeroAgenciaAgencia(String valorConsulta, String situacao, TipoContaCorrenteEnum tipoContaCorrente, Integer unidadeEnsino, boolean controlarAcesso,
            int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
        sqlStr.append("inner join agencia a ON a.codigo = cc.agencia ");
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
    		sqlStr.append("inner join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
    	}
        sqlStr.append("WHERE upper( a.numeroAgencia ) like('").append(valorConsulta.toUpperCase()).append("%') ");
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
            sqlStr.append("and uecc.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (Uteis.isAtributoPreenchido(situacao)) {
        	sqlStr.append("and cc.situacao = '").append(situacao).append("' ");
        }
        if (Uteis.isAtributoPreenchido(tipoContaCorrente)) {
        	sqlStr.append("and cc.tipoContaCorrente = '").append(tipoContaCorrente).append("' ");
        }
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
        } finally {
            sqlStr = null;
        }

    }

    public List<ContaCorrenteVO> consultarPorNomeBanco(String valorConsulta, String situacao, TipoContaCorrenteEnum tipoContaCorrente, Integer unidadeEnsino, boolean controlarAcesso,
    		int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
    	sqlStr.append("inner join agencia a ON a.codigo = cc.agencia ");
    	sqlStr.append("inner join banco b ON b.codigo = a.banco ");
    	if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
    		sqlStr.append("inner join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
    	}
    	sqlStr.append("WHERE upper( b.nome ) ilike('").append(valorConsulta).append("%') ");
    	if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
            sqlStr.append("and uecc.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (Uteis.isAtributoPreenchido(situacao)) {
        	sqlStr.append("and cc.situacao = '").append(situacao).append("' ");
        }
        if (Uteis.isAtributoPreenchido(tipoContaCorrente)) {
        	sqlStr.append("and cc.tipoContaCorrente = '").append(tipoContaCorrente).append("' ");
        }
    	try {
    		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    	} finally {
    		sqlStr = null;
    	}
    	
    }

    public ContaCorrenteVO consultarPorNumeroAgenciaNumeroConta(String conta, String agencia, Integer unidadeEnsino, boolean controlarAcesso,
            int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
        sqlStr.append("inner join agencia a on a.codigo = cc.agencia ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("left join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
        }
        sqlStr.append("where a.numeroagencia ilike('").append(agencia).append("%') and cc.numero ilike('").append(conta).append("%') ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("and unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            if (tabelaResultado.next()) {
                return montarDados(tabelaResultado, nivelMontarDados, usuario);
            }
            return new ContaCorrenteVO();
        } finally {
            sqlStr = null;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>ContaCorrente</code> através do valor do atributo
     * <code>Date dataAbertura</code>. Retorna os objetos com valores pertecentes ao período informado por parâmetro.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ContaCorrenteVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ContaCorrenteVO> consultarPorDataAbertura(Date prmIni, Date prmFim, String situacao, TipoContaCorrenteEnum tipoContaCorrente, Integer unidadeEnsino, boolean controlarAcesso,
            int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
    		sqlStr.append("inner join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
    	}
        sqlStr.append("WHERE ((dataAbertura >= '").append(Uteis.getDataJDBC(prmIni)).append("') and (dataAbertura <= '").append(Uteis.getDataJDBC(prmFim)).append("')) ");
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
            sqlStr.append("and uecc.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (Uteis.isAtributoPreenchido(situacao)) {
        	sqlStr.append("and cc.situacao = '").append(situacao).append("' ");
        }
        if (Uteis.isAtributoPreenchido(tipoContaCorrente)) {
        	sqlStr.append("and cc.tipoContaCorrente = '").append(tipoContaCorrente).append("' ");
        }
        sqlStr.append("ORDER BY dataAbertura");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sqlStr = null;
        }
    }

    @Override
    public List<ContaCorrenteVO> consultarPorContaCaixa(Boolean valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, boolean situacaoAtiva,
            UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("left join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
        }
        sqlStr.append("WHERE contaCaixa = '").append(valorConsulta.booleanValue()).append("' ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("and unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (situacaoAtiva) {
        	sqlStr.append(" AND cc.situacao = 'AT' ");
        }
        sqlStr.append("ORDER BY codigo");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sqlStr = null;
        }
    }

    public List<ContaCorrenteVO> consultarPorContaCaixaDataAberturaFluxoCaixaSituacao(Boolean valorConsulta, Integer unidadeEnsino, Date dataAberturaFluxoCaixa, String situacaoFluxoCaixa, boolean controlarAcesso, int nivelMontarDados,
            UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("left join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
        }
        sqlStr.append("WHERE cc.contaCaixa = '").append(valorConsulta.booleanValue()).append("' ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("and unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        sqlStr.append(" and (select fc.dataAbertura from fluxocaixa fc where fc.contacaixa = cc.codigo AND situacao = '").append(situacaoFluxoCaixa);
        sqlStr.append("' order by fc.codigo desc limit 1) >= '").append(Uteis.getDataJDBC(dataAberturaFluxoCaixa)).append("' ");
        sqlStr.append(" and cc.funcionarioresponsavel is null ");
        sqlStr.append(" ORDER BY codigo");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sqlStr = null;
        }
    }

    public Boolean consultarSeUsuarioTemContaCaixaVinculadoAEle(Integer codigoPessoa) {
        String sqlStr = "SELECT * FROM CONTACORRENTE CC LEFT JOIN FUNCIONARIO F ON CC.FUNCIONARIORESPONSAVEL = F.CODIGO ";
        sqlStr += "LEFT JOIN PESSOA P ON F.PESSOA = P.CODIGO WHERE P.CODIGO = ?";
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoPessoa});
            if (tabelaResultado.next()) {
                return true;
            } else {
                return false;
            }
        } finally {
            sqlStr = null;
        }
    }

    public List<ContaCorrenteVO> consultarPorFuncionarioResponsavel(Integer codigoPessoa, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//        String sqlStr = "SELECT * FROM CONTACORRENTE CC LEFT JOIN FUNCIONARIO F ON CC.FUNCIONARIORESPONSAVEL = F.CODIGO ";
//        sqlStr += "LEFT JOIN PESSOA P ON F.PESSOA = P.CODIGO WHERE P.CODIGO = ?";
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT CC.* FROM CONTACORRENTE CC LEFT JOIN FUNCIONARIO F ON CC.FUNCIONARIORESPONSAVEL = F.CODIGO ");
        sb.append(" LEFT JOIN PESSOA P ON F.PESSOA = P.CODIGO ");
        sb.append(" INNER JOIN unidadeEnsinoContaCorrente ON unidadeEnsinoContaCorrente.contacorrente = CC.codigo ");
        sb.append(" WHERE (P.CODIGO = ").append(codigoPessoa).append(" or P.CODIGO is null) AND unidadeEnsinoContaCorrente.unidadeEnsino = ").append(unidadeEnsino);
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sb = null;
        }
    }

    public List<ContaCorrenteVO> consultarPorFuncionarioResponsavelDataAberturaFluxoCaixaSituacao(boolean contaCaixa, Integer codigoPessoa, Integer unidadeEnsino, Date dataAberturaFluxoCaixa, String situacaoFluxoCaixa, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT (CC.*) FROM CONTACORRENTE CC ");
        sqlStr.append(" LEFT JOIN FUNCIONARIO F ON CC.FUNCIONARIORESPONSAVEL = F.CODIGO ");
        sqlStr.append(" INNER JOIN unidadeEnsino"
                + "ContaCorrente ON unidadeEnsinoContaCorrente.contacorrente = CC.codigo ");
        sqlStr.append(" LEFT JOIN PESSOA P ON F.PESSOA = P.CODIGO ");
        sqlStr.append("WHERE 1=1 ");
        if (contaCaixa) {
            if (codigoPessoa != 0) {
                sqlStr.append(" AND P.CODIGO = ").append(codigoPessoa);
            }
        }
        sqlStr.append(" AND cc.contaCaixa = ").append(contaCaixa);
        try {
            ControleAcesso.verificarPermissaoUsuarioFuncionalidadeComUsuarioVOEspecifico("ContaCorrente_PagarReceberCaixaUnidadeDiferente", usuario);
        } catch (Exception e) {
            if (unidadeEnsino != 0) {
                sqlStr.append(" AND unidadeEnsinoContaCorrente.unidadeEnsino = ").append(unidadeEnsino);
            }
        }
        if (contaCaixa) {
            sqlStr.append(" AND (select fc.dataAbertura from fluxocaixa fc where fc.contacaixa = cc.codigo AND situacao = '").append(situacaoFluxoCaixa).append("' order by fc.codigo desc limit 1)");
            sqlStr.append(" >= '").append(Uteis.getDataJDBC(dataAberturaFluxoCaixa)).append("' ");
        }
        if (contaCaixa) {
            sqlStr.append(" union all ");
            sqlStr.append(" SELECT DISTINCT (CC.*) FROM CONTACORRENTE CC ");
            sqlStr.append(" INNER JOIN unidadeEnsinoContaCorrente ON unidadeEnsinoContaCorrente.contacorrente = CC.codigo  AND cc.contaCaixa = true ");
            sqlStr.append(" AND (select fc.dataAbertura from fluxocaixa fc where fc.contacaixa = cc.codigo AND situacao = '").append(situacaoFluxoCaixa).append("' order by fc.codigo desc limit 1)");
            sqlStr.append(" >= '").append(Uteis.getDataJDBC(dataAberturaFluxoCaixa)).append("' ");
            sqlStr.append(" and cc.funcionarioResponsavel is null ");
            if (unidadeEnsino != 0) {
                sqlStr.append(" AND unidadeEnsinoContaCorrente.unidadeEnsino = ").append(unidadeEnsino);
            }
        }

        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sqlStr = null;
        }
    }

    public List<ContaCorrenteVO> consultarPorContaCaixa(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("left join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
        }
        sqlStr.append("WHERE 1 = 1 ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("and unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        sqlStr.append("ORDER BY codigo");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sqlStr = null;
        }
    }
    
    @Override
    public List<ContaCorrenteVO> consultarContaCorrenteHabilitadoPix(Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
    	if (unidadeEnsino.intValue() != 0) {
    		sqlStr.append("left join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
    	}
    	sqlStr.append("WHERE cc.habilitarRegistroPix = true ");
    	if (unidadeEnsino.intValue() != 0) {
    		sqlStr.append("and unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
    	}
    	sqlStr.append("ORDER BY codigo");
    	try {
    		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    	} finally {
    		sqlStr = null;
    	}
    }

    
    public List<ContaCorrenteVO> consultarPorNumero(String valorConsulta, String situacao, TipoContaCorrenteEnum tipoContaCorrente, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
    		sqlStr.append("inner join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
    	}
        sqlStr.append("WHERE upper( numero ) like('").append(valorConsulta.toUpperCase()).append("%') ");
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
            sqlStr.append("and uecc.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (Uteis.isAtributoPreenchido(situacao)) {
        	sqlStr.append("and cc.situacao = '").append(situacao).append("' ");
        }
        if (Uteis.isAtributoPreenchido(tipoContaCorrente)) {
        	sqlStr.append("and cc.tipoContaCorrente = '").append(tipoContaCorrente).append("' ");
        }
        
        sqlStr.append("ORDER BY numero");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sqlStr = null;
        }
    }
    
    public List<ContaCorrenteVO> consultarPorNumero(String valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
    	if (unidadeEnsino.intValue() != 0) {
    		sqlStr.append("left join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
    	}
    	sqlStr.append("WHERE upper( numero ) like('").append(valorConsulta.toUpperCase()).append("%') ");
    	if (unidadeEnsino.intValue() != 0) {
    		sqlStr.append("and unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
    	}
    	sqlStr.append("ORDER BY cc.nomeApresentacaoSistema, numero ");
    	try {
    		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    	} finally {
    		sqlStr = null;
    	}
    }
   
    
    public List<ContaCorrenteVO> consultarPorBancoPorNumeroContaCorrentePorDigitoContaCorrente(String nrBanco, String numero, String digito, String convenio, Integer unidadeEnsino, boolean isSituacaoAtiva, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select t.* from (select DISTINCT(cc.*) from contacorrente cc ");
        sqlStr.append(" inner JOIN agencia ON agencia.codigo = cc.agencia ");
    	sqlStr.append(" inner JOIN banco ON agencia.banco = banco.codigo ");
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
        	sqlStr.append(" inner join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
        }
        
        sqlStr.append(" WHERE cc.tipocontacorrente = 'CORRENTE' ");
        
        if (Uteis.isAtributoPreenchido(nrBanco)) {
        	sqlStr.append("  and banco.nrbanco = '").append(nrBanco).append("'  ");
        }
        
        if (Uteis.isAtributoPreenchido(numero)) {
        	sqlStr.append(" and ( trim(leading '0' from cc.numero) = '").append(StringUtils.stripStart(numero, "0")).append("' ");
        	sqlStr.append(" or (trim(leading '0' from cc.numero)||cc.digito) = '").append(StringUtils.stripStart(numero, "0")).append("' ");
        	sqlStr.append(" or (trim(leading '0' from agencia.numeroagencia)||cc.numero||cc.digito) = '").append(StringUtils.stripStart(numero, "0")).append("' ");
        	sqlStr.append(" ) ");
        }
        
    	if (digito != null && !digito.trim().isEmpty()) {
        	sqlStr.append(" and cc.digito = '").append(digito).append("' ");
        }
    	
    	if (!convenio.equals("")) {
        	sqlStr.append(" and convenio ilike '%").append(convenio).append("' ");
        }
    	
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
        	sqlStr.append(" and uecc.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if(isSituacaoAtiva){
        	sqlStr.append(" and cc.situacao = 'AT' ");	
        }
        sqlStr.append(") as t ORDER BY case when t.tipocontacorrente = 'CORRENTE' then 0  when t.tipocontacorrente = 'APLICACAO' then 1 else 2 end, t.situacao  ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        
    }
    
  
    
    
    @Override
    public List<ContaCorrenteVO> consultarContaCaixa( Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,
            UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("left join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
        }
        sqlStr.append(" WHERE  contacaixa = true ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("and unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        sqlStr.append("ORDER BY numero");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sqlStr = null;
        }
    }
    
    @Override
    public List<ContaCorrenteVO> consultarContaCaixaUnidadesEnsino(List<UnidadeEnsinoVO> listaUnidadesEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
        if ((listaUnidadesEnsino != null) &&
            (!listaUnidadesEnsino.isEmpty())) {
            sqlStr.append("left join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
        }
        sqlStr.append(" WHERE  contacaixa = true ");
        if ((listaUnidadesEnsino != null) &&
            (!listaUnidadesEnsino.isEmpty())) {
            sqlStr.append("and unidadeEnsino in (");
            String virgula = "";
            for (UnidadeEnsinoVO unidade : listaUnidadesEnsino) {
            	sqlStr.append(virgula);
            	sqlStr.append(unidade.getCodigo().intValue());
            	virgula = ", ";
            }
            sqlStr.append(") ");
        }
        sqlStr.append("ORDER BY numero");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sqlStr = null;
        }
    }    
    
    @Override
    public List<ContaCorrenteVO> consultarContaCorrente( Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,
            UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("left join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
        }
        sqlStr.append(" WHERE  contacaixa = false ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("and unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        sqlStr.append("ORDER BY numero");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sqlStr = null;
        }
    }

    public List<ContaCorrenteVO> consultarContaCorrenteComboBox(Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select DISTINCT cc.codigo AS \"cc.codigo\", cc.numero, cc.digito,cc.nomeApresentacaoSistema, cc.tipoContaCorrente from contacorrente cc ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("left join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
        }
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("and unidadeEnsino = ");
            sqlStr.append(unidadeEnsino.intValue());
        }
        sqlStr.append("ORDER BY numero");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            List vetResultado = new ArrayList(0);
            while (tabelaResultado.next()) {
                ContaCorrenteVO obj = new ContaCorrenteVO();
                obj.setCodigo(new Integer(tabelaResultado.getInt("cc.codigo")));
                obj.setNumero(tabelaResultado.getString("numero"));
                obj.setDigito(tabelaResultado.getString("digito"));
                obj.setNomeApresentacaoSistema(tabelaResultado.getString("nomeApresentacaoSistema"));
                obj.setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(tabelaResultado.getString("tipoContaCorrente")));
                vetResultado.add(obj);
            }
            return vetResultado;
        } finally {
            sqlStr = null;
        }
    }
    /**
     * 
     */
    public List<ContaCorrenteVO> consultarContaCorrenteComboBoxPorUnidadeEnsino(Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder("SELECT DISTINCT contaCorrente.codigo, contacorrente.gerarRemessaSemDescontoAbatido, contaCorrente.numero, contacorrente.carteira,  contacorrente.variacaoCarteira ,  contaCorrente.digito, contaCorrente.taxaBoleto, contaCorrente.codigoTransmissaoRemessa, contaCorrente.cnab, agencia.numeroAgencia, ");
		sqlStr.append(" contacorrente.utilizaCobrancaPartilhada, contacorrente.codigoReceptor1, contacorrente.codigoReceptor2, contacorrente.codigoReceptor3, contacorrente.codigoReceptor4, contacorrente.percReceptor1, contacorrente.percReceptor2, contacorrente.percReceptor3, contacorrente.percReceptor4, ");
		sqlStr.append(" agencia.numero AS \"agencia.numero\", agencia.codigo AS \"agencia.codigo\", banco.nrbanco,contacorrente.nomeApresentacaoSistema, ");
		sqlStr.append(" contacorrente.tipoContaCorrente  ");
		sqlStr.append(" FROM contaCorrente  ");
		sqlStr.append(" INNER JOIN unidadeEnsinoContaCorrente uecc ON uecc.contacorrente = contaCorrente.codigo ");
		sqlStr.append(" INNER JOIN agencia ON agencia.codigo = contaCorrente.agencia");
		sqlStr.append(" INNER JOIN banco ON agencia.banco = banco.codigo");
		sqlStr.append(" WHERE uecc.unidadeEnsino = ").append(unidadeEnsino);
		sqlStr.append(" ORDER BY contaCorrente.numero ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		List vetResultado = new ArrayList(0);
		while (tabelaResultado.next()) {
			ContaCorrenteVO obj = new ContaCorrenteVO();
			obj.setCodigo(tabelaResultado.getInt("codigo"));
			obj.setNomeApresentacaoSistema(tabelaResultado.getString("nomeApresentacaoSistema"));
			obj.setGerarRemessaSemDescontoAbatido(tabelaResultado.getBoolean("gerarRemessaSemDescontoAbatido"));
			obj.setNumero(tabelaResultado.getString("numero"));
			obj.setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(tabelaResultado.getString("tipoContaCorrente")));
			obj.setCarteira(tabelaResultado.getString("carteira"));
			obj.setVariacaoCarteira(tabelaResultado.getString("variacaoCarteira"));
			obj.setDigito(tabelaResultado.getString("digito"));
			obj.setTaxaBoleto(tabelaResultado.getDouble("taxaBoleto"));
			obj.getAgencia().setNumeroAgencia(tabelaResultado.getString("numeroAgencia"));
			obj.setCodigoTransmissaoRemessa(tabelaResultado.getString("codigoTransmissaoRemessa"));
			obj.setCnab(tabelaResultado.getString("cnab"));
			obj.setUtilizaCobrancaPartilhada(tabelaResultado.getBoolean("utilizaCobrancaPartilhada"));
			obj.setCodigoReceptor1(tabelaResultado.getString("codigoReceptor1"));
			obj.setCodigoReceptor2(tabelaResultado.getString("codigoReceptor2"));
			obj.setCodigoReceptor3(tabelaResultado.getString("codigoReceptor3"));
			obj.setCodigoReceptor4(tabelaResultado.getString("codigoReceptor4"));
			obj.setPercReceptor1(tabelaResultado.getDouble("percReceptor1"));
			obj.setPercReceptor2(tabelaResultado.getDouble("percReceptor2"));
			obj.setPercReceptor3(tabelaResultado.getDouble("percReceptor3"));
			obj.setPercReceptor4(tabelaResultado.getDouble("percReceptor4"));
			obj.getAgencia().setNumero(tabelaResultado.getString("agencia.numero"));
			obj.getAgencia().setCodigo(tabelaResultado.getInt("agencia.codigo"));
			obj.getAgencia().getBanco().setNrBanco(tabelaResultado.getString("nrBanco"));
			vetResultado.add(obj);
		}
		return vetResultado;
	}
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void inativarConta(final ContaCorrenteVO obj, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "UPDATE contacorrente SET situacao=? WHERE ((codigo =? ))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, "IN");
                    sqlAlterar.setInt(2, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    public List<ContaCorrenteVO> consultarContaCorrenteCaixaPorNumero(String valorConsulta, Integer unidadeEnsino, boolean permissaoMovimentacaoContaCaixaContaCorrente, boolean controlarAcesso, int nivelMontarDados,
            UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("left join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
        }
        sqlStr.append("WHERE upper( numero ) like('").append(valorConsulta.toUpperCase()).append("%') ");
        if (!permissaoMovimentacaoContaCaixaContaCorrente) {
            sqlStr.append("AND contaCaixa = ").append(!permissaoMovimentacaoContaCaixaContaCorrente).append(" ");
        }
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("and unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        sqlStr.append(" AND cc.situacao = 'AT' ");
        sqlStr.append("ORDER BY numero");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sqlStr = null;
        }
    }
    
    public List<ContaCorrenteVO> consultarContaCorrenteCaixaPorNumeroPorUnidadeEnsino(String valorConsulta, List<UnidadeEnsinoVO> listaUnidadeEnsino, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
    	return consultarContaCorrenteCaixaPorNumeroPorUnidadeEnsino(valorConsulta, listaUnidadeEnsino, "", controlarAcesso, nivelMontarDados, usuario);
    }
    
	public List<ContaCorrenteVO> consultarContaCorrenteCaixaPorNumeroPorUnidadeEnsino(String valorConsulta, List<UnidadeEnsinoVO> listaUnidadeEnsino, String ordenarPor, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sqlStr = null;
    	if (ordenarPor.equals("tipo")) {
    		sqlStr = new StringBuilder("select * from ( select DISTINCT(cc.*) from contacorrente cc ");
    	} else {
    		sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc "); 
    	}
    	if (Uteis.isAtributoPreenchido(listaUnidadeEnsino)) {
    		sqlStr.append("left join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
    	}
    	sqlStr.append(" WHERE upper( numero ) like('").append(valorConsulta.toUpperCase()).append("%') ");
    	sqlStr.append(" AND cc.tipocontacorrente in ('CORRENTE','APLICACAO') ");
    	if (Uteis.isAtributoPreenchido(listaUnidadeEnsino)) {
    		sqlStr.append("and uecc.unidadeEnsino in (").append(UteisTexto.converteListaEntidadeCampoCodigoParaCondicaoIn(listaUnidadeEnsino)).append(") ");
    	}
    	sqlStr.append(" AND cc.situacao = 'AT' ");
    	if (ordenarPor.equals("tipo")) {
    		sqlStr.append(") as t ORDER BY case when t.tipocontacorrente = 'CORRENTE' then 1 else 2 end, t.situacao, numero");
    	} else {
    		sqlStr.append("ORDER BY numero");
    	}
    	try {
    		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    	} finally {
    		sqlStr = null;
    	}
    }

    /**
     * Responsável por realizar uma consulta de <code>ContaCorrente</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>ContaCorrenteVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ContaCorrenteVO> consultarPorCodigo(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,
            UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("left join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
        }
        sqlStr.append("WHERE codigo >= ").append(valorConsulta.intValue()).append(" ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("and unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        sqlStr.append("ORDER BY codigo");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sqlStr = null;
        }
    }

    public List<ContaCorrenteVO> consultarPorCodigoSomenteContasCorrente(Integer valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,
            UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("left join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
        }
        sqlStr.append("WHERE codigo >= ").append(valorConsulta.intValue()).append(" AND contacaixa = false ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("and unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        sqlStr.append("ORDER BY codigo");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sqlStr = null;
        }
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>ContaCorrenteVO</code> resultantes da consulta.
     */
    public static List<ContaCorrenteVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<ContaCorrenteVO> vetResultado = new ArrayList<ContaCorrenteVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>ContaCorrenteVO</code>.
     *
     * @return O objeto da classe <code>ContaCorrenteVO</code> com os dados devidamente montados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public static ContaCorrenteVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ContaCorrenteVO obj = new ContaCorrenteVO();
        obj.setNovoObj(Boolean.FALSE);
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setNumero(dadosSQL.getString("numero"));
        obj.setDigito(dadosSQL.getString("digito"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setNomeApresentacaoSistema(dadosSQL.getString("nomeApresentacaoSistema"));
		obj.setGerarRemessaSemDescontoAbatido(dadosSQL.getBoolean("gerarRemessaSemDescontoAbatido"));		
        obj.setRequerConfirmacaoMovimentacaoFinanceira(dadosSQL.getBoolean("requerConfirmacaoMovimentacaoFinanceira"));
        obj.setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(dadosSQL.getString("tipoContaCorrente")));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        
        obj.setDataAbertura(dadosSQL.getDate("dataAbertura"));
        obj.setSaldo(dadosSQL.getDouble("saldo"));
        obj.setTaxaBoleto(dadosSQL.getDouble("taxaBoleto"));
        obj.getAgencia().setCodigo(dadosSQL.getInt("agencia"));
        obj.setCodigoTransmissaoRemessa(dadosSQL.getString("codigoTransmissaoRemessa"));
        obj.setCodigoEstacaoRemessa(dadosSQL.getString("codigoEstacaoRemessa"));              
        obj.setCnab(dadosSQL.getString("cnab"));
        obj.setUtilizaCobrancaPartilhada(dadosSQL.getBoolean("utilizaCobrancaPartilhada"));                
        obj.setCodigoReceptor1(dadosSQL.getString("codigoReceptor1"));                
        obj.setCodigoReceptor2(dadosSQL.getString("codigoReceptor2"));                
        obj.setCodigoReceptor3(dadosSQL.getString("codigoReceptor3"));                
        obj.setCodigoReceptor4(dadosSQL.getString("codigoReceptor4"));
        obj.setPercReceptor1(dadosSQL.getDouble("percReceptor1"));                        
        obj.setPercReceptor2(dadosSQL.getDouble("percReceptor2"));                        
        obj.setPercReceptor3(dadosSQL.getDouble("percReceptor3"));                        
        obj.setPercReceptor4(dadosSQL.getDouble("percReceptor4"));   
        obj.setQtdDiasFiltroRemessa(dadosSQL.getInt("qtdDiasFiltroRemessa"));
        obj.setPermiteEnviarBoletoVencido(dadosSQL.getBoolean("permiteEnviarBoletoVencido"));
        obj.setCarteiraRegistradaEmissaoBoletoBanco(dadosSQL.getBoolean("carteiraRegistradaEmissaoBoletoBanco"));        
        obj.setQtdDiasBoletoVencido(dadosSQL.getInt("qtdDiasBoletoVencido"));
        obj.setQtdDiasBoletoAVencer(dadosSQL.getInt("qtdDiasBoletoAVencer"));                                
        obj.setContaCaixa(dadosSQL.getBoolean("contaCaixa"));
        obj.setBloquearEmissaoBoleto(dadosSQL.getBoolean("bloquearEmissaoBoleto"));
        obj.setCarteira(dadosSQL.getString("carteira"));
        obj.setVariacaoCarteira(dadosSQL.getString("variacaoCarteira"));
        obj.setConvenio(dadosSQL.getString("convenio"));
        obj.getFuncionarioResponsavel().setCodigo(dadosSQL.getInt("funcionarioresponsavel"));
        obj.setCodigoCedente(dadosSQL.getString("codigocedente"));
        obj.setDigitoCodigoCedente(dadosSQL.getString("digitoCodigocedente"));
        obj.setMensagemCarteiraRegistrada(dadosSQL.getString("mensagemCarteiraRegistrada"));
        obj.setCarteiraRegistrada(dadosSQL.getBoolean("carteiraRegistrada"));
        obj.setUtilizarRenegociacao(dadosSQL.getBoolean("utilizarRenegociacao"));
        obj.setUtilizaDadosInformadosCCparaGeracaoBoleto(dadosSQL.getBoolean("utilizaDadosInformadosCCparaGeracaoBoleto"));
        obj.setControleRemessaMXVO(getFacadeFactory().getControleRemessaMXFacade().consultarPorContaCorrente(obj.getCodigo(), nivelMontarDados, usuario));

        obj.setControlarBloqueioEmissaoBoleto(dadosSQL.getBoolean("controlarBloqueioEmissaoBoleto"));
        obj.setBloquearEmissaoBoletoFimDeSemana(dadosSQL.getBoolean("bloquearEmissaoBoletoFimDeSemana"));
        obj.setBloquearEmissaoBoletoFeriado(dadosSQL.getBoolean("bloquearEmissaoBoletoFeriado"));
        obj.setBloquearEmissaoBoletoHoraIni(dadosSQL.getString("bloquearEmissaoBoletoHoraIni"));
        obj.setBloquearEmissaoBoletoHoraFim(dadosSQL.getString("bloquearEmissaoBoletoHoraFim"));
        obj.setMensagemBloqueioEmissaoBoleto(dadosSQL.getString("mensagemBloqueioEmissaoBoleto"));
        obj.setGerarContaPagarTaxaBancaria(dadosSQL.getBoolean("gerarContaPagarTaxaBancaria"));
        obj.setUtilizaAbatimentoNoRepasseRemessaBanco(dadosSQL.getBoolean("utilizaAbatimentoNoRepasseRemessaBanco"));
        obj.setUtilizaTaxaCartaoDebito(dadosSQL.getBoolean("utilizaTaxaCartaoDebito"));
        obj.setUtilizaTaxaCartaoCredito(dadosSQL.getBoolean("utilizaTaxaCartaoCredito"));
        obj.setHabilitarProtestoBoleto(dadosSQL.getBoolean("habilitarProtestoBoleto"));
        obj.setQtdDiasProtestoBoleto(dadosSQL.getInt("qtdDiasProtestoBoleto"));
        obj.setQtdDiasBaixaAutTitulo(dadosSQL.getInt("qtdDiasBaixaAutTitulo"));
        obj.setRemessaBoletoEmitido(dadosSQL.getBoolean("remessaBoletoEmitido"));
        
        obj.setHabilitarRegistroRemessaOnline(dadosSQL.getBoolean("habilitarRegistroRemessaOnline"));
        obj.setAmbienteContaCorrenteEnum(AmbienteContaCorrenteEnum.valueOf(dadosSQL.getString("ambienteContaCorrenteEnum")));
        obj.setTipoAutenticacaoRegistroRemessaOnlineEnum(TipoAutenticacaoRegistroRemessaOnlineEnum.valueOf(dadosSQL.getString("tipoAutenticacaoRegistroRemessaOnlineEnum")));
        obj.setTokenIdRegistroRemessaOnline(dadosSQL.getString("tokenIdRegistroRemessaOnline"));
        obj.setTokenKeyRegistroRemessaOnline(dadosSQL.getString("tokenKeyRegistroRemessaOnline"));
        obj.setTokenClienteRegistroRemessaOnline(dadosSQL.getString("tokenClienteRegistroRemessaOnline"));
        obj.setSenhaCertificado(dadosSQL.getString("senhaCertificado"));
        obj.setCaminhoCertificado(dadosSQL.getString("caminhoCertificado"));
        obj.getArquivoCertificadoVO().setCodigo(dadosSQL.getInt("arquivoCertificado"));
        obj.setDataVencimentoCertificado(dadosSQL.getDate("dataVencimentoCertificado"));
        obj.setSenhaUnidadeCertificadora(dadosSQL.getString("senhaUnidadeCertificadora"));
        obj.setCaminhoUnidadeCertificadora(dadosSQL.getString("caminhoUnidadeCertificadora"));
        obj.getArquivoUnidadeCertificadoraVO().setCodigo(dadosSQL.getInt("arquivoUnidadeCertificadora"));
        obj.setGerarRemessaMatriculaAut(dadosSQL.getBoolean("gerarRemessaMatriculaAut"));
        obj.setGerarRemessaParcelasAut(dadosSQL.getBoolean("gerarRemessaParcelasAut"));      
        obj.setGerarRemessaNegociacaoAut(dadosSQL.getBoolean("gerarRemessaNegociacaoAut"));      

        obj.setGerarRemessaOutrosAut(dadosSQL.getBoolean("gerarRemessaOutrosAut"));      
        obj.setGerarRemessaRequerimentoAut(dadosSQL.getBoolean("gerarRemessaRequerimentoAut"));      
        obj.setGerarRemessaBibliotecaAut(dadosSQL.getBoolean("gerarRemessaBibliotecaAut"));      
        obj.setGerarRemessaInscProcSeletivoAut(dadosSQL.getBoolean("gerarRemessaInscProcSeletivoAut"));      
        obj.setGerarRemessaDevChequeAut(dadosSQL.getBoolean("gerarRemessaDevChequeAut"));      
        obj.setGerarRemessaConvenioAut(dadosSQL.getBoolean("gerarRemessaConvenioAut"));      
        obj.setGerarRemessaContratoReceitaAut(dadosSQL.getBoolean("gerarRemessaContratoReceitaAut"));      
        obj.setGerarRemessaMateriaDidaticoAut(dadosSQL.getBoolean("gerarRemessaMateriaDidaticoAut"));      
        obj.setGerarRemessaInclusaoReposicaoAut(dadosSQL.getBoolean("gerarRemessaInclusaoReposicaoAut"));      
        obj.setCodigoComunicacaoRemessaCP(dadosSQL.getString("codigoComunicacaoRemessaCP"));      
        obj.setCodigoEstacaoRemessa(dadosSQL.getString("codigoEstacaoRemessa"));    

        obj.setPermitirEmissaoBoletoRemessaOnlineRejeita(dadosSQL.getBoolean("permitirEmissaoBoletoRemessaOnlineRejeita"));      
        
        obj.getFormaPgtoTaxaBancaria().setCodigo(dadosSQL.getInt("formaPgtoTaxaBancaria"));
        obj.getCategoriaDespesaTaxaBancaria().setCodigo(dadosSQL.getInt("categoriaDespesaTaxaBancaria"));
        
        obj.setTipoOrigemBiblioteca(dadosSQL.getBoolean("tipoOrigemBiblioteca"));
        obj.setTipoOrigemBolsaCusteadaConvenio(dadosSQL.getBoolean("tipoOrigemBolsaCusteadaConvenio"));
        obj.setTipoOrigemContratoReceita(dadosSQL.getBoolean("tipoOrigemContratoReceita"));
        obj.setTipoOrigemDevolucaoCheque(dadosSQL.getBoolean("tipoOrigemDevolucaoCheque"));
        obj.setTipoOrigemInclusaoReposicao(dadosSQL.getBoolean("tipoOrigemInclusaoReposicao")); 
        obj.setTipoOrigemInscricaoProcessoSeletivo(dadosSQL.getBoolean("tipoOrigemInscricaoProcessoSeletivo"));
        obj.setTipoOrigemMaterialDidatico(dadosSQL.getBoolean("tipoOrigemMaterialDidatico"));
        obj.setTipoOrigemMatricula(dadosSQL.getBoolean("tipoOrigemMatricula"));
        obj.setTipoOrigemMensalidade(dadosSQL.getBoolean("tipoOrigemMensalidade"));        
        obj.setTipoOrigemNegociacao(dadosSQL.getBoolean("tipoOrigemNegociacao"));        
        obj.setTipoOrigemOutros(dadosSQL.getBoolean("tipoOrigemOutros"));
        obj.setTipoOrigemRequerimento(dadosSQL.getBoolean("tipoOrigemRequerimento"));           
        obj.setRealizarNegociacaoContaReceberVencidaAutomaticamente(dadosSQL.getBoolean("realizarNegociacaoContaReceberVencidaAutomaticamente"));
		obj.setNumeroDiaVencidoNegociarContaReceberAutomaticamente(dadosSQL.getInt("numeroDiaVencidoNegociarContaReceberAutomaticamente"));
		obj.setNumeroDiaAvancarVencimentoContaReceber(dadosSQL.getInt("numeroDiaAvancarVencimentoContaReceber"));
        obj.setPermiteEmissaoBoletoVencido(dadosSQL.getBoolean("permiteEmissaoBoletoVencido"));
        obj.setEspecieTituloBoleto(dadosSQL.getString("especieTituloBoleto"));
        obj.setPermiteGerarRemessaOnlineBoletoVencido(dadosSQL.getBoolean("permiteGerarRemessaOnlineBoletoVencido"));
        obj.setGerarRemessaBoletoVencidoMatricula(dadosSQL.getBoolean("gerarRemessaBoletoVencidoMatricula"));
        obj.setGerarRemessaBoletoVencidoParcelas(dadosSQL.getBoolean("gerarRemessaBoletoVencidoParcelas"));
        obj.setGerarRemessaBoletoVencidoNegociacao(dadosSQL.getBoolean("gerarRemessaBoletoVencidoNegociacao"));
        obj.setGerarRemessaBoletoVencidoOutros(dadosSQL.getBoolean("gerarRemessaBoletoVencidoOutros"));
        obj.setGerarRemessaBoletoVencidoBiblioteca(dadosSQL.getBoolean("gerarRemessaBoletoVencidoBiblioteca"));
        obj.setGerarRemessaBoletoVencidoDevCheque(dadosSQL.getBoolean("gerarRemessaBoletoVencidoDevCheque"));
        obj.setGerarRemessaBoletoVencidoConvenio(dadosSQL.getBoolean("gerarRemessaBoletoVencidoConvenio"));
        obj.setGerarRemessaBoletoVencidoContratoReceita(dadosSQL.getBoolean("gerarRemessaBoletoVencidoContratoReceita"));
        obj.setGerarRemessaBoletoVencidoMateriaDidatico(dadosSQL.getBoolean("gerarRemessaBoletoVencidoMateriaDidatico"));
        obj.setGerarRemessaBoletoVencidoInclusaoReposicao(dadosSQL.getBoolean("gerarRemessaBoletoVencidoInclusaoReposicao"));
        obj.setQtdeDiasVencidoPermitirRemessaOnlineMatricula(dadosSQL.getInt("qtdeDiasVencidoPermitirRemessaOnlineMatricula"));
        obj.setQtdeDiasVencidoPermitirRemessaOnlineParcela(dadosSQL.getInt("qtdeDiasVencidoPermitirRemessaOnlineParcela"));
        obj.setQtdeDiasVencidoPermitirRemessaOnlineNegociacao(dadosSQL.getInt("qtdeDiasVencidoPermitirRemessaOnlineNegociacao"));
        obj.setQtdeDiasVencidoPermitirRemessaOnlineOutros(dadosSQL.getInt("qtdeDiasVencidoPermitirRemessaOnlineOutros"));
        obj.setQtdeDiasVencidoPermitirRemessaOnlineBiblioteca(dadosSQL.getInt("qtdeDiasVencidoPermitirRemessaOnlineBiblioteca"));
        obj.setQtdeDiasVencidoPermitirRemessaOnlineDevCheque(dadosSQL.getInt("qtdeDiasVencidoPermitirRemessaOnlineDevCheque"));
        obj.setQtdeDiasVencidoPermitirRemessaConvenio(dadosSQL.getInt("qtdeDiasVencidoPermitirRemessaConvenio"));
        obj.setQtdeDiasVencidoPermitirRemessaContratoReceita(dadosSQL.getInt("qtdeDiasVencidoPermitirRemessaContratoReceita"));
        obj.setQtdeDiasVencidoPermitirRemessaMaterialDidatico(dadosSQL.getInt("qtdeDiasVencidoPermitirRemessaMaterialDidatico"));
        obj.setQtdeDiasVencidoPermitirRemessaInclusaoExclusao(dadosSQL.getInt("qtdeDiasVencidoPermitirRemessaInclusaoExclusao"));
        obj.setInicialGeracaoNossoNumero(dadosSQL.getString("inicialGeracaoNossoNumero"));
        obj.setBloquearEmitirBoletoSemRegistroRemessa(dadosSQL.getBoolean("bloquearEmitirBoletoSemRegistroRemessa"));
        obj.setChaveAutenticacaoClienteRegistroRemessaOnline(dadosSQL.getString("chaveAutenticacaoClienteRegistroRemessaOnline"));
        obj.setChaveTransacaoClienteRegistroRemessaOnline(dadosSQL.getString("chaveTransacaoClienteRegistroRemessaOnline"));
        obj.setDataExpiracaoChaveTransacaoClienteRegistroRemessaOnline(dadosSQL.getTimestamp("dataExpiracaoChaveTransacaoClienteRegistroRemessaOnline"));
        
        obj.setHabilitarRegistroPix(dadosSQL.getBoolean("habilitarregistropix"));
		obj.setAmbienteContaCorrentePix(AmbienteContaCorrenteEnum.valueOf(dadosSQL.getString("ambientecontacorrentepix")));
		obj.setTokenIdRegistroPix(dadosSQL.getString("tokenidregistropix"));
		obj.setTokenKeyRegistroPix(dadosSQL.getString("tokenkeyregistropix"));
		obj.setChavePix(dadosSQL.getString("chavepix"));
		obj.setChaveAplicacaoDesenvolvedorPix(dadosSQL.getString("chaveaplicacaodesenvolvedorpix"));
		
		if(Uteis.isAtributoPreenchido(dadosSQL.getInt("formarecebimentopadraopix"))) {
			obj.setFormaRecebimentoPadraoPix(getFacadeFactory().getFormaPagamentoFacade().consultarPorChavePrimaria(dadosSQL.getInt("formarecebimentopadraopix"), false, Uteis.NIVELMONTARDADOS_TODOS, usuario));	
		}
		 obj.setPermiteRecebimentoBoletoVencidoRemessaOnline(dadosSQL.getBoolean("permiteRecebimentoBoletoVencidoRemessaOnline"));		
		 obj.setNumeroDiasLimiteRecebimentoBoletoVencidoRemessaOnline(dadosSQL.getInt("numeroDiasLimiteRecebimentoBoletoVencidoRemessaOnline"));	
		                    		       
        if (obj.getUtilizaDadosInformadosCCparaGeracaoBoleto()) {
        	// montar dados da CC para geração de boletos
        	obj.setNome(dadosSQL.getString("nome"));
        	obj.setRazaoSocial(dadosSQL.getString("razaoSocial"));
        	obj.setMantenedora(dadosSQL.getString("mantenedora"));
        	obj.setCompleto(dadosSQL.getString("completo"));
        	obj.setSetor(dadosSQL.getString("setor"));
        	obj.setNumeroEnd(dadosSQL.getString("numeroEnd"));
        	obj.setComplemento(dadosSQL.getString("complemento"));
        	obj.setEndereco(dadosSQL.getString("endereco"));
        	obj.getCidade().setCodigo(dadosSQL.getInt("cidade"));
        	montarDadosCidade(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        	obj.setCEP(dadosSQL.getString("CEP"));
        	obj.setCNPJ(dadosSQL.getString("CNPJ"));
        	obj.setInscEstadual(dadosSQL.getString("inscEstadual"));
        	obj.setTelComercial1(dadosSQL.getString("telComercial1"));
        	obj.setTelComercial2(dadosSQL.getString("telComercial2"));
        	obj.setTelComercial3(dadosSQL.getString("telComercial3"));
        	obj.setEmail(dadosSQL.getString("email"));
        	obj.setSite(dadosSQL.getString("site"));
        	obj.setFax(dadosSQL.getString("fax"));    
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
        	if (dadosSQL.getInt("arquivoCertificado") > 0) {
            	obj.setArquivoCertificadoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(dadosSQL.getInt("arquivoCertificado"), Uteis.NIVELMONTARDADOS_TODOS, usuario));
            }
            if (dadosSQL.getInt("arquivoUnidadeCertificadora") > 0) {
            	obj.setArquivoUnidadeCertificadoraVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(dadosSQL.getInt("arquivoUnidadeCertificadora"), Uteis.NIVELMONTARDADOS_TODOS, usuario));
            }
        	montarDadosAgencia(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            return obj;
        }
        montarDadosAgencia(obj, nivelMontarDados, usuario);
        montarDadosFuncionarioResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);  
        if (dadosSQL.getInt("arquivoCertificado") > 0) {
        	obj.setArquivoCertificadoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(dadosSQL.getInt("arquivoCertificado"), Uteis.NIVELMONTARDADOS_TODOS, usuario));
        }
        if (dadosSQL.getInt("arquivoUnidadeCertificadora") > 0) {
        	obj.setArquivoUnidadeCertificadoraVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(dadosSQL.getInt("arquivoUnidadeCertificadora"), Uteis.NIVELMONTARDADOS_TODOS, usuario));
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        
        obj.setUnidadeEnsinoContaCorrenteVOs(getFacadeFactory().getUnidadeEnsinoContaCorrenteFacade().consultarPorContaCorrente(obj.getCodigo(), false, nivelMontarDados, usuario));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
        	return obj;
        }
        return obj;
    }

    public static void montarDadosFuncionarioResponsavel(ContaCorrenteVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFuncionarioResponsavel().getCodigo().intValue() == 0) {
            obj.setFuncionarioResponsavel(new FuncionarioVO());
            return;
        }
        obj.setFuncionarioResponsavel(getFacadeFactory().getFuncionarioFacade().consultaRapidaPorChavePrimaria(
                obj.getFuncionarioResponsavel().getCodigo(), false, usuario));
    }

    public static void montarDadosCidade(ContaCorrenteVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	if (obj.getCidade().getCodigo().intValue() == 0) {
    		obj.setCidade(new CidadeVO());
    		return;
    	}
    	obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
    }
    
    /**
     * Operação responsável por montar os dados de um objeto da classe <code>AgenciaVO</code> relacionado ao objeto
     * <code>ContaCorrenteVO</code>. Faz uso da chave primária da classe <code>AgenciaVO</code> para realizar a
     * consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosAgencia(ContaCorrenteVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getAgencia().getCodigo().intValue() == 0) {
            obj.setAgencia(new AgenciaVO());
            return;
        }
        obj.setAgencia(getFacadeFactory().getAgenciaFacade().consultarPorChavePrimaria(obj.getAgencia().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ContaCorrenteVO</code> através de sua chave
     * primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.SUPPORTS)
    public ContaCorrenteVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        return getAplicacaoControle().getContaCorrenteVO(codigoPrm, usuario);
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.SUPPORTS)
    public ContaCorrenteVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ContaCorrente WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ContaCorrente ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.SUPPORTS)
    public ContaCorrenteVO consultarChaveTransacaoClienteRemessaOnlineAndDataExpiracaoPorChavePrimariaUnica(Integer codigoPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT chavetransacaoclienteregistroremessaonline , dataexpiracaochavetransacaoclienteregistroremessaonline  FROM ContaCorrente WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ContaCorrente ).");
        }
        ContaCorrenteVO obj = new ContaCorrenteVO();
        obj.setChaveTransacaoClienteRegistroRemessaOnline(tabelaResultado.getString("chavetransacaoclienteregistroremessaonline"));
        obj.setDataExpiracaoChaveTransacaoClienteRegistroRemessaOnline(tabelaResultado.getTimestamp("dataExpiracaoChaveTransacaoClienteRegistroRemessaOnline"));
        return obj;
    }
    
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.SUPPORTS)
    public String consultarPorBancoPorNumeroContaCorrentePorDigitoContaCorrente(String nrBanco, String numero, String digito, UsuarioVO usuario) throws Exception {
    	StringBuilder sqlStr = new StringBuilder("select array_to_string(array_agg(t.nomesistema), ',') as nome from (");
    	sqlStr.append(" select DISTINCT( ");
    	sqlStr.append(" case when cc.nomeapresentacaosistema is null then cc.numero else cc.nomeapresentacaosistema end  ");
    	sqlStr.append(" ) as nomesistema ");
    	sqlStr.append(" from contacorrente cc  ");
    	sqlStr.append(" inner JOIN agencia ON agencia.codigo = cc.agencia ");
    	sqlStr.append(" inner JOIN banco ON agencia.banco = banco.codigo ");
    	sqlStr.append(" WHERE banco.nrbanco = '").append(nrBanco).append("'  ");    	
    	if (Uteis.isAtributoPreenchido(numero)) {
    		sqlStr.append(" and ( trim(leading '0' from cc.numero) = '").append(StringUtils.stripStart(numero, "0")).append("' ");
    		sqlStr.append(" or (trim(leading '0' from cc.numero)||cc.digito) = '").append(StringUtils.stripStart(numero, "0")).append("' ");
    		sqlStr.append(" or (trim(leading '0' from agencia.numeroagencia)||cc.numero||cc.digito) = '").append(StringUtils.stripStart(numero, "0")).append("' ");
    		sqlStr.append(" ) ");
    	}
    	if (digito != null && !digito.trim().isEmpty()) {
    		sqlStr.append(" and cc.digito = '").append(digito).append("' ");
    	}
    	sqlStr.append(") as t  ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	if (tabelaResultado.next()) {
    		return tabelaResultado.getString("nome");
        }
    	return "";
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ContaCorrente.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        ContaCorrente.idEntidade = idEntidade;
    }

    public List<ContaCorrenteVO> consultarRapidaContaCorrentePorTipo(Boolean contaCaixa, Boolean renegociacao, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
    	return consultarRapidaContaCorrentePorTipo(contaCaixa, renegociacao, false, unidadeEnsino, usuarioVO);
    }
    
    public List<ContaCorrenteVO> consultarRapidaContaCorrentePorTipo(Boolean contaCaixa, Boolean renegociacao, Boolean verificarUtilizarNegociacao, Integer unidadeEnsino, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sb = new StringBuilder(
                "Select DISTINCT(contacorrente.*), agencia.numeroAgencia as agencia_numeroAgencia, agencia.digito as agencia_digito, banco.nome as banco_nome, banco.nrBanco as banco_nrBanco from contacorrente ");
        sb.append(" left join agencia on agencia.codigo = contacorrente.agencia");
        sb.append(" left join banco on banco.codigo = agencia.banco ");
        sb.append(" left join UnidadeEnsinoContaCorrente on UnidadeEnsinoContaCorrente.contaCorrente = contacorrente.codigo ");
        sb.append(" left join UnidadeEnsino on UnidadeEnsino.codigo = unidadeEnsinocontacorrente.UnidadeEnsino ");
        sb.append(" WHERE contacaixa = ").append(contaCaixa);
        if (unidadeEnsino != null && unidadeEnsino > 0) {
            sb.append(" and  unidadeEnsinocontacorrente.unidadeensino = ").append(unidadeEnsino);
        }
        if (verificarUtilizarNegociacao) {
        	sb.append(" and UnidadeEnsinoContaCorrente.utilizarNegociacao = true ");
        }
        if (renegociacao) {
        	sb.append(" and contaCorrente.utilizarRenegociacao = true ");
        }
        List<ContaCorrenteVO> objs = new ArrayList<ContaCorrenteVO>(0);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        while (tabelaResultado.next()) {
            ContaCorrenteVO obj = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
            obj.getAgencia().setNumeroAgencia(tabelaResultado.getString("agencia_numeroAgencia"));
            obj.getAgencia().setDigito(tabelaResultado.getString("agencia_digito"));
            obj.getAgencia().getBanco().setNome(tabelaResultado.getString("banco_nome"));
            obj.getAgencia().getBanco().setNrBanco(tabelaResultado.getString("banco_nrBanco"));
            obj.setUnidadeEnsinoContaCorrenteVOs(getFacadeFactory().getUnidadeEnsinoContaCorrenteFacade().consultarPorContaCorrente(obj.getCodigo(),
                    false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
            objs.add(obj);
        }
        return objs;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public Double consultarSaldoContaCorrente(Integer codigoContaCorrente) throws Exception {
        String sqlStr = "SELECT saldo FROM contacorrente WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoContaCorrente});
        if (!tabelaResultado.next()) {
            return 0.0;
        }
        return tabelaResultado.getDouble("saldo");
    }

    public boolean consultarExistenciaContaGeradaPorContaCorrente(Integer contaCorrente, UsuarioVO usuarioVO) {
        StringBuilder sb = new StringBuilder();
        sb.append("select contareceber.codigo from contareceber where situacao = 'AR' and contacorrente = ").append(contaCorrente).append(" limit 1");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (tabelaResultado.next()) {
            return true;
        }
        return false;
    }

    public boolean apreasentarMensagemAlteracaoContaCorrenteMudancaCarteira(ContaCorrenteVO contaCorrenteAlteradaVO, StringBuilder mensagemAviso, UsuarioVO usuarioVO) throws Exception {
        boolean isContaCorrenteAlterada = false;
        boolean isExisteContaReceber = getFacadeFactory().getContaCorrenteFacade().consultarExistenciaContaGeradaPorContaCorrente(contaCorrenteAlteradaVO.getCodigo(), usuarioVO);
        if (isExisteContaReceber) {
            mensagemAviso.append("Atenção, os seguintes campos foram alterados e são importantes para geração de boletos: ");
            isContaCorrenteAlterada = executouAlteracaoCadastroContaCorrente(contaCorrenteAlteradaVO, usuarioVO);
            if (isContaCorrenteAlterada) {
                ContaCorrenteVO contaCorrenteDesatualizadaVO = consultarPorChavePrimaria(contaCorrenteAlteradaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
                if (!contaCorrenteAlteradaVO.getNumero().equals(contaCorrenteDesatualizadaVO.getNumero())) {
                    mensagemAviso.append("NÚMERO, ");
                }
                if (!contaCorrenteAlteradaVO.getDigito().equals(contaCorrenteDesatualizadaVO.getDigito())) {
                    mensagemAviso.append("DÍGITO, ");
                }
                if (!contaCorrenteAlteradaVO.getCarteira().equals(contaCorrenteDesatualizadaVO.getCarteira())) {
                    mensagemAviso.append("CARTEIRA, ");
                }
                if (!contaCorrenteAlteradaVO.getConvenio().equals(contaCorrenteDesatualizadaVO.getConvenio())) {
                    mensagemAviso.append("CONVÊNIO, ");
                }
                if (!contaCorrenteAlteradaVO.getCodigoCedente().equals(contaCorrenteDesatualizadaVO.getCodigoCedente())) {
                    mensagemAviso.append("CÓDIGO CEDENTE, ");
                }
                if (!contaCorrenteAlteradaVO.getAgencia().getCodigo().equals(contaCorrenteDesatualizadaVO.getAgencia().getCodigo())) {
                    mensagemAviso.append("AGÊNCIA, ");
                }
                mensagemAviso.replace(mensagemAviso.length() -2, mensagemAviso.length() -1, ".") ;
                mensagemAviso.append(" Será necessário realizar a mudança de carteira para alunos vinculados a essa conta corrente, caso contrário poderá ocorrer problemas no recebimento dos boletos junto ao banco.");
            }
        }
        return isContaCorrenteAlterada;
    }

    public boolean executouAlteracaoCadastroContaCorrente(ContaCorrenteVO contaCorrenteAlteradaVO, UsuarioVO usuarioVO) throws Exception {
        ContaCorrenteVO contaCorrenteDesatualizadaVO = consultarPorChavePrimaria(contaCorrenteAlteradaVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
        if (!contaCorrenteAlteradaVO.getNumero().equals(contaCorrenteDesatualizadaVO.getNumero())
                || !contaCorrenteAlteradaVO.getDigito().equals(contaCorrenteDesatualizadaVO.getDigito())
                || !contaCorrenteAlteradaVO.getCarteira().equals(contaCorrenteDesatualizadaVO.getCarteira())
                || !contaCorrenteAlteradaVO.getVariacaoCarteira().equals(contaCorrenteDesatualizadaVO.getVariacaoCarteira())
                || !contaCorrenteAlteradaVO.getConvenio().equals(contaCorrenteDesatualizadaVO.getConvenio())
                || !contaCorrenteAlteradaVO.getCodigoCedente().equals(contaCorrenteDesatualizadaVO.getCodigoCedente())
                || !contaCorrenteAlteradaVO.getAgencia().getCodigo().equals(contaCorrenteDesatualizadaVO.getAgencia().getCodigo())) {
            return true;
        }
        return false;
    }
    
    /* @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public ContaCorrenteVO consultarContaCorrentePadraoConfiguracaoFinanceiro(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
		if (Uteis.isAtributoPreenchido(configuracaoFinanceiroVO) && Uteis.isAtributoPreenchido(configuracaoFinanceiroVO.getContaCorrentePadraoControleCobranca())) {
			return configuracaoFinanceiroVO.getContaCorrentePadraoControleCobranca();
		}
		throw new ConsistirException("Não há Conta-Corrente padrão para Controle Cobrança na configuração padrão no sistema ou não há configuração vinculada a esta Unidade de Ensino.");
	}*/
    
    public ContaCorrenteVO consultarContaCorrentePadraoUnidadeEnsino(Integer unidadeEnsino, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("SELECT contaCorrente.codigo, contaCorrente.numero,contacorrente.nomeApresentacaoSistema, contacorrente.tipoContaCorrente FROM contaCorrente ");
    	sb.append(" INNER JOIN unidadeEnsino ON unidadeEnsino.contaCorrentePadrao = contaCorrente.codigo ");
    	sb.append(" WHERE unidadeEnsino.codigo = ").append(unidadeEnsino);
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	if (!tabelaResultado.next()) {
    		return new ContaCorrenteVO();
    	}
    	ContaCorrenteVO obj = new ContaCorrenteVO();
    	obj.setCodigo(tabelaResultado.getInt("codigo"));
    	obj.setNumero(tabelaResultado.getString("numero"));
    	obj.setNomeApresentacaoSistema(tabelaResultado.getString("nomeApresentacaoSistema"));
        obj.setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(tabelaResultado.getString("tipoContaCorrente")));
    	return obj;
    }
    
    public ContaCorrenteVO consultarContaCorrentePadraoPorContaReceber(Integer contaReceber, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("SELECT contaCorrente.codigo, contaCorrente.numero, contaCorrente.taxaBoleto,contaCorrente.nomeApresentacaoSistema, contaCorrente.tipoContaCorrente FROM contaCorrente ");
    	sb.append(" INNER JOIN contaReceber ON contaReceber.contaCorrente = contaCorrente.codigo ");
    	sb.append(" WHERE contaReceber.codigo = ").append(contaReceber);
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	if (!tabelaResultado.next()) {
    		return new ContaCorrenteVO();
    	}
    	ContaCorrenteVO obj = new ContaCorrenteVO();
    	obj.setCodigo(tabelaResultado.getInt("codigo"));
    	obj.setNumero(tabelaResultado.getString("numero"));
    	obj.setTaxaBoleto(tabelaResultado.getDouble("taxaBoleto"));
    	obj.setNomeApresentacaoSistema(tabelaResultado.getString("nomeApresentacaoSistema"));
    	obj.setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(tabelaResultado.getString("tipoContaCorrente")));
    	return obj;
    }
    
    public ContaCorrenteVO consultarContaCorrentePadraoPorContaReceberAgrupada(Integer contaReceberAgrupada, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("SELECT contaCorrente.codigo, contaCorrente.numero, contaCorrente.taxaBoleto,contaCorrente.nomeApresentacaoSistema, contaCorrente.tipoContaCorrente FROM contaCorrente ");
    	sb.append(" INNER JOIN contaReceberAgrupada ON contaReceberAgrupada.contaCorrente = contaCorrente.codigo ");
    	sb.append(" WHERE contaReceberAgrupada.codigo = ").append(contaReceberAgrupada);
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	if (!tabelaResultado.next()) {
    		return new ContaCorrenteVO();
    	}
    	ContaCorrenteVO obj = new ContaCorrenteVO();
    	obj.setCodigo(tabelaResultado.getInt("codigo"));
    	obj.setNumero(tabelaResultado.getString("numero"));
    	obj.setTaxaBoleto(tabelaResultado.getDouble("taxaBoleto"));
    	obj.setNomeApresentacaoSistema(tabelaResultado.getString("nomeApresentacaoSistema"));
    	obj.setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(tabelaResultado.getString("tipoContaCorrente")));
    	return obj;
    }
    
    public void inicializarDadosBancoContaCorrente(ContaCorrenteVO contaCorrenteVO, UsuarioVO usuarioVO) throws Exception {
    	BancoVO bancoVO = null;
    	if (!contaCorrenteVO.getAgencia().getCodigo().equals(0)) {
    		bancoVO = getFacadeFactory().getBancoFacade().consultarPorCodigoAgencia(contaCorrenteVO.getAgencia().getCodigo(), usuarioVO);
    	}
    	if (bancoVO != null && !bancoVO.getCodigo().equals(0)) {
    		contaCorrenteVO.getAgencia().setBanco(bancoVO);
    	}
    }
    
    public Boolean consultarFuncionarioResponsavelPorCaixa(Integer pessoa, Integer contaCorrente, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select contacorrente.codigo from contacorrente ");
		sb.append(" inner join funcionario on funcionario.codigo = funcionarioresponsavel ");
		sb.append(" where funcionario.pessoa = ").append(pessoa);
		sb.append(" and contacorrente.codigo = ").append(contaCorrente);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next()) {
			return true;
		}
		return false;
	}
    
    public Boolean consultarExistenciaContaGeradaPorContaCaixaExistente(Integer contaCorrenteCaixa, Integer unidadeEnsino) {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from negociacaorecebimento where negociacaorecebimento.contacorrentecaixa = ").append(contaCorrenteCaixa).append(" and negociacaorecebimento.unidadeensino = ").append(unidadeEnsino).append(" limit 1");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (tabelaResultado.next()) {
            return true;
        }
        return false;
    }
    
    @Override
    public List<ContaCorrenteVO> consultarPorNumeroAgenciaNumeroContaDigitoConta(String conta, String digitoConta, String agencia, Integer unidadeEnsino, String convenio, String cnab, boolean controlarAcesso,int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
        sqlStr.append("inner join agencia a on a.codigo = cc.agencia ");
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("left join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
        }
        sqlStr.append("where a.numeroagencia ilike '%").append(agencia).append("' and cc.numero ilike '%").append(conta).append("' ");
        if (!digitoConta.equals("")) {
        	sqlStr.append(" and cc.digito = '").append(digitoConta).append("' ");
        }
        if (!convenio.equals("")) {
        	sqlStr.append(" and convenio ilike '%").append(convenio).append("' ");
        }
        if (!cnab.equals("")) {
        	sqlStr.append(" and cnab ilike '%").append(cnab).append("' ");
        }
        if (unidadeEnsino.intValue() != 0) {
            sqlStr.append("and unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        sqlStr.append(" order by cc.codigo ");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
        } finally {
            sqlStr = null;
        }
    }
    
    public List<ContaCorrenteVO> consultarPorCodigoFuncionarioResponsavel(Integer codigoPessoa, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
//      String sqlStr = "SELECT * FROM CONTACORRENTE CC LEFT JOIN FUNCIONARIO F ON CC.FUNCIONARIORESPONSAVEL = F.CODIGO ";
//      sqlStr += "LEFT JOIN PESSOA P ON F.PESSOA = P.CODIGO WHERE P.CODIGO = ?";
      StringBuilder sb = new StringBuilder();
      sb.append("SELECT CC.* FROM CONTACORRENTE CC LEFT JOIN FUNCIONARIO F ON CC.FUNCIONARIORESPONSAVEL = F.CODIGO ");
      sb.append(" INNER JOIN PESSOA P ON F.PESSOA = P.CODIGO ");
      sb.append(" INNER JOIN unidadeEnsinoContaCorrente ON unidadeEnsinoContaCorrente.contacorrente = CC.codigo ");
      sb.append(" WHERE (P.CODIGO = ").append(codigoPessoa).append(" or P.CODIGO is null)");
      if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
    	  sb.append(" AND unidadeEnsinoContaCorrente.unidadeEnsino = ").append(unidadeEnsino);
      }
      
      try {
          SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
          return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
      } finally {
          sb = null;
      }
  }
    
    public List<ContaCorrenteVO> consultarPorNossoNumero(List<ControleRemessaContaReceberVO> listaContaReceber, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select distinct contacorrente.codigo, banco.codigo as banco, banco.nrbanco as nrBanco, agencia.codigo as agencia, contacorrente.cnab as cnab from contareceber  ");
    	sb.append("inner join contacorrente on contacorrente.codigo = contareceber.contacorrente ");
    	sb.append("inner join agencia on agencia.codigo = contacorrente.agencia ");
    	sb.append("inner join banco on banco.codigo = agencia.banco ");
    	sb.append(" WHERE contareceber.nossonumero in ('' ");
    	Iterator i = listaContaReceber.iterator();
    	while (i.hasNext()) {
    		ControleRemessaContaReceberVO obj = (ControleRemessaContaReceberVO)i.next();
    		sb.append(", '" + obj.getNossoNumero() + "'");
    	}
    	sb.append(")");
    	try {
    		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    		List<ContaCorrenteVO> vetResultado = new ArrayList<ContaCorrenteVO>(0);
            while (tabelaResultado.next()) {
            	ContaCorrenteVO obj = new ContaCorrenteVO();
                obj.setNovoObj(Boolean.FALSE);
                obj.setCodigo(tabelaResultado.getInt("codigo"));
                obj.setCnab(tabelaResultado.getString("cnab"));
                obj.getAgencia().setCodigo(tabelaResultado.getInt("agencia"));
                obj.getAgencia().getBanco().setCodigo(tabelaResultado.getInt("banco"));
                obj.getAgencia().getBanco().setNrBanco(tabelaResultado.getString("nrBanco"));
                vetResultado.add(obj);
            }
            return vetResultado;    		

    	} finally {
    		sb = null;
    	}
    }
    
    @Override
    public ContaCorrenteVO consultarContaCorrenteUsadaNaNegociacaoRecebimentoPorContaReceber(Integer contaReceber, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select contacorrente.*  ");
    	sb.append(" from contacorrente ");
    	sb.append(" inner join formapagamentonegociacaorecebimento on formapagamentonegociacaorecebimento.contacorrente = contacorrente.codigo ");
    	sb.append(" inner join contareceberrecebimento on contareceberrecebimento.formapagamentonegociacaorecebimento = formapagamentonegociacaorecebimento.codigo ");
    	sb.append(" where contareceberrecebimento.contareceber = ? ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), contaReceber);
        if (!tabelaResultado.next()) {
            return new ContaCorrenteVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
    }
    
    @Override
    public ContaCorrenteVO consultarContaCorrenteUsadaNaNegociacaoPagamentoPorContaPagar(Integer contaPagar, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append(" select contacorrente.*  ");
    	sb.append(" from contacorrente ");
    	sb.append(" inner join formapagamentonegociacaopagamento on formapagamentonegociacaopagamento.contacorrente = contacorrente.codigo ");
    	sb.append(" inner join contapagarpagamento on contapagarpagamento.formapagamentonegociacaopagamento = formapagamentonegociacaopagamento.codigo ");
    	sb.append(" where contapagarpagamento.contapagar = ? ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), contaPagar);
        if (!tabelaResultado.next()) {
            return new ContaCorrenteVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
    }
    
    
   // @Override
    public List<ContaCorrenteVO> consultarRapidaContaCorrentePorTipoSituacao(Boolean contaCaixa, Boolean renegociacao, Integer unidadeEnsino, String situacao, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sb = new StringBuilder(
                "Select DISTINCT(contacorrente.*), agencia.numeroAgencia as agencia_numeroAgencia, agencia.digito as agencia_digito, banco.nome as banco_nome, banco.nrBanco as banco_nrBanco from contacorrente ");
        sb.append(" left join agencia on agencia.codigo = contacorrente.agencia");
        sb.append(" left join banco on banco.codigo = agencia.banco ");
        sb.append(" left join UnidadeEnsinoContaCorrente on UnidadeEnsinoContaCorrente.contaCorrente = contacorrente.codigo ");
        sb.append(" left join UnidadeEnsino on UnidadeEnsino.codigo = unidadeEnsinocontacorrente.UnidadeEnsino ");
        sb.append(" WHERE contacaixa = ").append(contaCaixa);
        if (unidadeEnsino != null && unidadeEnsino > 0) {
            sb.append(" and  unidadeEnsinocontacorrente.unidadeensino = ").append(unidadeEnsino);
        }
        if (renegociacao) {
        	sb.append(" and contaCorrente.utilizarRenegociacao = true ");
        }
        if (!situacao.equals("")) {
        	sb.append(" and contaCorrente.situacao = '").append(situacao).append("' ");
        }
        List<ContaCorrenteVO> objs = new ArrayList<ContaCorrenteVO>(0);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        while (tabelaResultado.next()) {
            ContaCorrenteVO obj = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO);
            obj.getAgencia().setNumeroAgencia(tabelaResultado.getString("agencia_numeroAgencia"));
            obj.getAgencia().setDigito(tabelaResultado.getString("agencia_digito"));
            obj.getAgencia().getBanco().setNome(tabelaResultado.getString("banco_nome"));
            obj.getAgencia().getBanco().setNrBanco(tabelaResultado.getString("banco_nrBanco"));
            obj.setUnidadeEnsinoContaCorrenteVOs(getFacadeFactory().getUnidadeEnsinoContaCorrenteFacade().consultarPorContaCorrente(obj.getCodigo(),
                    false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
            objs.add(obj);
        }
        return objs;
    }
    
    @Override
    public ContaCorrenteVO consultarPorCondicaoRenegociacaoEUnidadeEnsino(Integer condicaoRenegociacao, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select contacorrente.codigo from contacorrente ");
    	sb.append(" inner join condicaorenegociacaounidadeensino on condicaorenegociacaounidadeensino.contacorrente = contacorrente.codigo ");
    	sb.append(" where condicaorenegociacao = ? ");
    	sb.append(" and unidadeensino = ? ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), condicaoRenegociacao, unidadeEnsino);
        if (!tabelaResultado.next()) {
            return new ContaCorrenteVO();
        }
        return getAplicacaoControle().getContaCorrenteVO(tabelaResultado.getInt("codigo"), usuarioVO);
    }
    
    @Override
    public ContaCorrenteVO consultarContaCorrentePorTurma(Integer turma,  int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select contacorrente.codigo , contacorrente.numero , contacorrente.digito , contacorrente.situacao , contacorrente.nomeApresentacaoSistema , contacorrente.gerarRemessaSemDescontoAbatido ,");
    	sb.append(" contacorrente.requerConfirmacaoMovimentacaoFinanceira , contacorrente.tipoContaCorrente from contacorrente"); 
    	sb.append(" inner join turma on turma.contacorrente = contacorrente.codigo ");
    	sb.append(" where turma.codigo  = ? ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), turma);
        if (!tabelaResultado.next()) {
            return new ContaCorrenteVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuarioVO));
    }
    
    public List<ContaCorrenteVO> consultarPorNomeApresentacao(String valorConsulta, String situacao, TipoContaCorrenteEnum tipoContaCorrente, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select DISTINCT(cc.*) from contacorrente cc ");
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
    		sqlStr.append("inner join unidadeensinocontacorrente uecc on cc.codigo = uecc.contacorrente ");
    	}
        sqlStr.append("WHERE upper( nomeapresentacaosistema ) like('").append(valorConsulta.toUpperCase()).append("%') ");
        if (Uteis.isAtributoPreenchido(unidadeEnsino)) {
            sqlStr.append("and uecc.unidadeEnsino = ").append(unidadeEnsino.intValue()).append(" ");
        }
        if (Uteis.isAtributoPreenchido(situacao)) {
        	sqlStr.append("and cc.situacao = '").append(situacao).append("' ");
        }
        if (Uteis.isAtributoPreenchido(tipoContaCorrente)) {
        	sqlStr.append("and cc.tipoContaCorrente = '").append(tipoContaCorrente).append("' ");
        }
        
        sqlStr.append("ORDER BY numero");
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        } finally {
            sqlStr = null;
        }
    }
    
    @Override
	public Double atualizarDadosSaldoContaCaixa(Integer contaCaixa, UsuarioVO usuario) throws Exception {

    	FluxoCaixaVO fluxoCaixaVO = new FluxoCaixaVO();
    	
    	if (Uteis.isAtributoPreenchido(contaCaixa)) {
			
    		FluxoCaixaVO fluxoCaixaUltimo = getFacadeFactory().getFluxoCaixaFacade().consultarUltimoFluxoCaixaPorContaCaixa(contaCaixa, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
			
			if (fluxoCaixaUltimo == null) {
				fluxoCaixaVO.setSaldoInicial(fluxoCaixaVO.getContaCaixa().getSaldo());
				fluxoCaixaVO.setSaldoFinal(fluxoCaixaVO.getContaCaixa().getSaldo());
			} else {
				fluxoCaixaVO.setSaldoInicial(Uteis.arrendondarForcando2CadasDecimais(fluxoCaixaUltimo.getSaldoFinal()));
			}
			
		} else {
			fluxoCaixaVO.setSaldoInicial(0.0);
		}
    	
    	return fluxoCaixaVO.getSaldoInicial();
    	
	}
    
    public List<ContaCorrenteVO> consultarPorContaCaixa(Boolean valorConsulta, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	return consultarPorContaCaixa(valorConsulta, unidadeEnsino, controlarAcesso, nivelMontarDados, false, usuario);
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void alterarChaveTransacaoRemessaOnlineContaCorrente(final Integer contaCorrente, final String chaveAutenticacao, Date dataExpiracaoChave ,UsuarioVO usuario) throws Exception {
        final String sql = "UPDATE ContaCorrente set chaveTransacaoClienteRegistroRemessaOnline=? , dataExpiracaoChaveTransacaoClienteRegistroRemessaOnline=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setString(1, chaveAutenticacao);               
                sqlAlterar.setTimestamp(2, Uteis.getDataJDBCTimestamp(dataExpiracaoChave));
                sqlAlterar.setInt(3, contaCorrente.intValue());
                return sqlAlterar;
            }
        });
    }
    
	public Optional<ContaCorrenteVO> consultarPorCodigoFluxoCaixa(Integer fluxoCaixa, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuarioVO);
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet("SELECT contacaixa FROM fluxocaixa WHERE codigo = ? ", fluxoCaixa);
		return tabelaResultado.next() ? Optional.ofNullable(getAplicacaoControle().getContaCorrenteVO(tabelaResultado.getInt("contacaixa"), usuarioVO)) : Optional.empty();
	}
}

