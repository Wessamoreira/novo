package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import controle.academico.RenovarMatriculaControle;
import negocio.comuns.academico.CondicaoPagamentoPlanoDescontoVO;
import negocio.comuns.academico.CondicaoPagamentoPlanoFinanceiroCursoVO;
import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.PlanoDescontoDisponivelMatriculaVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.academico.PlanoFinanceiroCursoVO;
import negocio.comuns.academico.enumeradores.TipoGeracaoMaterialDidaticoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CentroReceitaVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.CondicaoPagamentoPlanoFinanceiroCursoInterfaceFacade;
import webservice.servicos.CondicaoPagamentoRSVO;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>PlanoFinanceiroCursoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>PlanoFinanceiroCursoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see PlanoFinanceiroCursoVO
 * @see ControleAcesso
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class CondicaoPagamentoPlanoFinanceiroCurso extends ControleAcesso implements CondicaoPagamentoPlanoFinanceiroCursoInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = -4671655217721187102L;
	protected static String idEntidade;

    public CondicaoPagamentoPlanoFinanceiroCurso() throws Exception {
        super();
        setIdEntidade("PlanoFinanceiroCurso");
    }

    public CondicaoPagamentoPlanoFinanceiroCursoVO novo() throws Exception {
        CondicaoPagamentoPlanoFinanceiroCurso.incluir(getIdEntidade());
        CondicaoPagamentoPlanoFinanceiroCursoVO obj = new CondicaoPagamentoPlanoFinanceiroCursoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>PlanoFinanceiroCursoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PlanoFinanceiroCursoVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final CondicaoPagamentoPlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws Exception {
        try {
            CondicaoPagamentoPlanoFinanceiroCursoVO.validarDados(obj);
            /**
             * @author Leonardo Riciolle
             * Comentado 23/10/2014
             */
            //CondicaoPagamentoPlanoFinanceiroCurso.incluir(getIdEntidade());
            final String sql = "INSERT INTO CondicaoPagamentoPlanoFinanceiroCurso( "
            		+ "valorMatricula, valorParcela, nrParcelasPeriodo, descontoProgressivoPadrao, "
                    + "valorMatriculaSistemaPorCredito, valorMinimoParcelaSistemaPorCredito, valorUnitarioCredito, restituirDiferencaValorMatriculaSistemaPorCredito, descricao, nomeFormula, "
                    + "formulaCalculoValorFinal, formulaUsoVariavel1, formulaCalculoVariavel1, utilizarVariavel1, variavel1, formulaUsoVariavel2, formulaCalculoVariavel2, utilizarVariavel2, variavel2, "
                    + "formulaUsoVariavel3, formulaCalculoVariavel3, utilizarVariavel3, variavel3, tituloVariavel1, tituloVariavel2, tituloVariavel3, planoFinanceiroCurso, utilizarValorMatriculaFixo, "
                    + "valorFixoDisciplinaIncluida, valorDescontoDisciplinaExcluida, gerarParcelasSeparadasParaDisciplinasIncluidas, "
                    + "gerarCobrancaPorDisciplinasIncluidas, gerarDescontoPorDiscipliaExcluidas, gerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo, numeroMaximoDisciplinaCursarParaGerarDescontos, ratiarValorDiferencaInclusaoExclusaoParaTodasParcelas, "
                    + "descricaoDuracao, textoPadraoContratoMatricula, descontoProgressivoPadraoMatricula, situacao, dataAtivacao, responsavelAtivacao, dataInativacao, responsavelInativacao, "
                    + "naoControlarMatricula, apresentarCondicaoVisaoAluno, definirPlanoDescontoApresentarMatricula, naoRegerarParcelaVencida, centroreceita, aplicarCalculoComBaseDescontosCalculados, descontoprogressivoprimeiraparcela,"
                    + "tipoCobrancaInclusaoDisciplinaRegular, cobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo, tipoCobrancaInclusaoDisciplinaDependencia, "
                    + "cobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet, tipoCalculoParcela, tipoUsoCondicaoPagamento, "
                    + "valorPorAtividadeComplementar, tipoCobrancaAtividadeComplementar, nrCreditoPorAtividadeComplementar, "
                    + "tipoCobrancaENADE, nrCreditoPorENADE, valorPorENADE, tipoCobrancaEstagio, nrCreditoPorEstagio, valorPorEstagio, "
                    + "valorFixoDisciplinaIncluidaEAD, utilizarPoliticaCobrancaEspecificaParaOptativas, tipoCobrancaDisciplinaOptativa, "
                    + "cobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet, valorDisciplinaOptativa, valorDisciplinaOptativaEAD, "
                    + "valorDisciplinaIncluidaDependencia, valorDisciplinaIncluidaDependenciaEAD, utilizarPoliticaDescontoDiscipRegularFeitasViaEAD,"
                    + "tipoDescontoDisciplinaRegularEAD, valorDescontoDisciplinaRegularEAD, tipoCobrancaExclusaoDisciplinaRegular, "
                    + "valorFixoDisciplinaExcluidaEAD, apresentarSomenteParaDeterminadaFormaIngresso, apresentarSomenteParaIngressanteNoSemestreAno, "
                    + "formaIngressoEntrevista, formaIngressoPortadorDiploma, formaIngressoTransferenciaInterna, formaIngressoProcessoSeletivo, "
                    + "formaIngressoTransferenciaExterna, formaIngressoReingresso, formaIngressoProuni, formaIngressoEnem, formaIngressoOutroTipoSelecao, "
                    + "anoIngressante, semestreIngressante, apresentarSomenteParaAlunosIntegralizandoCurso, considerarIntegralizandoEstiverCursandoUltimoPer2Vez, "
                    + "tipoControleAlunoIntegralizandoCurso, valorBaseDefinirAlunoIntegralizandoCurso, permiteRecebimentoOnlineCartaoCredito, "
                    + "apresentarMatriculaOnlineExterna, apresentarMatriculaOnlineProfessor, apresentarMatriculaOnlineCoordenador, "
                    + "considerarValorRateioBaseadoValorBaseComDescontosAplicados,abaterValorRateiroComoDescontoRateio, gerarParcelasExtrasSeparadoMensalidadeAReceber, "
                    + "considerarValorRateioExtraParcelaVencida, categoria, naocontrolarvalorparcela,"
                    + "gerarParcelaMaterialDidatico,usarUnidadeEnsinoEspecifica ,controlaDiaBaseVencimentoParcelaMaterialDidatico, aplicarDescontosParcelasNoMaterialDidatico, "
                    + "aplicarDescontoMaterialDidaticoDescontoAluno, aplicarDescontoMaterialDidaticoDescontoInstitucional, aplicarDescontoMaterialDidaticoDescontoProgressivo, "
                    + "aplicarDescontoMaterialDidaticoDescontoConvenio, aplicarDescontosDesconsiderandosVencimento, unidadeEnsinoFinanceira, quantidadeParcelasMaterialDidatico, "
                    + "diaBaseVencimentoParcelaOutraUnidade, valorPorParcelaMaterialDidatico, tipoGeracaoMaterialDidatico, apresentarCondPagtoCRM, regerarFinanceiro, "
                    + "apresentarSomenteParaRenovacoesNoSemestreAno, anoRenovacao, semestreRenovacao, lancarValorRatiadoSobreValorBaseContaReceber, valorPrimeiraParcela, vencimentoPrimeiraParcelaAntesMaterialDidatico,  "
                    + "usaValorPrimeiraParcela, considerarParcelasMaterialDidaticoReajustePreco "
                    + " ) VALUES ("
            		+ "?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, "
                    + "?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, ?,"
                    + "?, ?, ?, "
                    + "?, ?, ?, "
                    + "?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, "
                    + "?, ?, ?, "
                    + "?, ?, ?, "
                    + "?, ?, ?,"
                    + "?, ?, ?, "
                    + "?, ?, ?, "
                    + "?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, "
                    + "?, ?, ?, "
                    + "?, ?, ?, "
                    + "?, ?, ?, "
                    + "?, ?, ?,"
                    + "?, ? ,?, ?, "
                    + "?, ?, ?, "
                    + "?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, "
                    + "?, ?, ?, ?, ?, ?, "         
                    + "?, ? "
                    + " ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setDouble(1, Uteis.arrendondarForcando2CadasDecimais(obj.getValorMatricula().doubleValue()));
                    sqlInserir.setDouble(2, Uteis.arrendondarForcando2CadasDecimais(obj.getValorParcela().doubleValue()));
                    sqlInserir.setInt(3, obj.getNrParcelasPeriodo().intValue());
                    if (obj.getDescontoProgressivoPadrao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(4, obj.getDescontoProgressivoPadrao().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    sqlInserir.setDouble(5, Uteis.arrendondarForcando2CadasDecimais(obj.getValorMatriculaSistemaPorCredito().doubleValue()));
                    sqlInserir.setDouble(6, Uteis.arrendondarForcando2CadasDecimais(obj.getValorMinimoParcelaSistemaPorCredito().doubleValue()));
                    sqlInserir.setDouble(7, obj.getValorUnitarioCredito());
                    sqlInserir.setBoolean(8, obj.getRestituirDiferencaValorMatriculaSistemaPorCredito().booleanValue());
                    sqlInserir.setString(9, obj.getDescricao());
                    sqlInserir.setString(10, obj.getNomeFormula());
                    sqlInserir.setString(11, obj.getFormulaCalculoValorFinal());
                    sqlInserir.setString(12, obj.getFormulaUsoVariavel1());
                    sqlInserir.setString(13, obj.getFormulaCalculoVariavel1());
                    sqlInserir.setBoolean(14, obj.getUtilizarVariavel1());
                    sqlInserir.setDouble(15, Uteis.arrendondarForcando2CadasDecimais(obj.getVariavel1()));
                    sqlInserir.setString(16, obj.getFormulaUsoVariavel2());
                    sqlInserir.setString(17, obj.getFormulaCalculoVariavel2());
                    sqlInserir.setBoolean(18, obj.getUtilizarVariavel2());
                    sqlInserir.setDouble(19, Uteis.arrendondarForcando2CadasDecimais(obj.getVariavel2()));
                    sqlInserir.setString(20, obj.getFormulaUsoVariavel3());
                    sqlInserir.setString(21, obj.getFormulaCalculoVariavel3());
                    sqlInserir.setBoolean(22, obj.getUtilizarVariavel3());
                    sqlInserir.setDouble(23, Uteis.arrendondarForcando2CadasDecimais(obj.getVariavel3()));
                    sqlInserir.setString(24, obj.getTituloVariavel1());
                    sqlInserir.setString(25, obj.getTituloVariavel2());
                    sqlInserir.setString(26, obj.getTituloVariavel3());
                    sqlInserir.setInt(27, obj.getPlanoFinanceiroCurso());
                    sqlInserir.setBoolean(28, obj.getUtilizarValorMatriculaFixo());
                    sqlInserir.setDouble(29, obj.getValorFixoDisciplinaIncluida());
                    sqlInserir.setDouble(30, obj.getValorDescontoDisciplinaExcluida());
                    sqlInserir.setBoolean(31, obj.getGerarParcelasSeparadasParaDisciplinasIncluidas());
                    sqlInserir.setBoolean(32, obj.getGerarCobrancaPorDisciplinasIncluidas());
                    sqlInserir.setBoolean(33, obj.getGerarDescontoPorDiscipliaExcluidas());
                    sqlInserir.setBoolean(34, obj.getGerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo());
                    sqlInserir.setInt(35, obj.getNumeroMaximoDisciplinaCursarParaGerarDescontos());
                    sqlInserir.setBoolean(36, obj.getRatiarValorDiferencaInclusaoExclusaoParaTodasParcelas());
                    sqlInserir.setString(37, obj.getDescricaoDuracao());
                    if (obj.getTextoPadraoContratoMatricula().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(38, obj.getTextoPadraoContratoMatricula().getCodigo());
                    } else {
                        sqlInserir.setNull(38, 0);
                    }
                    if (obj.getDescontoProgressivoPadraoMatricula().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(39, obj.getDescontoProgressivoPadraoMatricula().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(39, 0);
                    }
                    sqlInserir.setString(40, obj.getSituacao());
                    sqlInserir.setDate(41, Uteis.getDataJDBC(obj.getDataAtivacao()));
                    if (obj.getResponsavelAtivacao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(42, obj.getResponsavelAtivacao().getCodigo());
                    } else {
                        sqlInserir.setNull(42, 0);
                    }
                    sqlInserir.setDate(43, Uteis.getDataJDBC(obj.getDataInativacao()));
                    if (obj.getResponsavelInativacao().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(44, obj.getResponsavelInativacao().getCodigo());
                    } else {
                        sqlInserir.setNull(44, 0);
                    }
                    sqlInserir.setBoolean(45, obj.getNaoControlarMatricula());
                    sqlInserir.setBoolean(46, obj.getApresentarCondicaoVisaoAluno());
                    sqlInserir.setBoolean(47, obj.getDefinirPlanoDescontoApresentarMatricula());
                    sqlInserir.setBoolean(48, obj.getNaoRegerarParcelaVencida());
                    if (obj.getCentroReceita().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(49, obj.getCentroReceita().getCodigo());
                    } else {
                        sqlInserir.setNull(49, 0);
                    }
                    sqlInserir.setBoolean(50, obj.getAplicarCalculoComBaseDescontosCalculados());
                    sqlInserir.setInt(51, obj.getDescontoProgressivoPrimeiraParcela().getCodigo());
                    int i = 52;
                    sqlInserir.setString(i++, obj.getTipoCobrancaInclusaoDisciplinaRegular());
                    sqlInserir.setBoolean(i++, obj.getCobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo());
                    sqlInserir.setString(i++, obj.getTipoCobrancaInclusaoDisciplinaDependencia());
                    sqlInserir.setBoolean(i++, obj.getCobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet());
                    sqlInserir.setString(i++, obj.getTipoCalculoParcela());
                    sqlInserir.setString(i++, obj.getTipoUsoCondicaoPagamento());
                    
                    sqlInserir.setDouble(i++, obj.getValorPorAtividadeComplementar());
                    sqlInserir.setString(i++, obj.getTipoCobrancaAtividadeComplementar());
                    sqlInserir.setInt(i++, obj.getNrCreditoPorAtividadeComplementar());
                    sqlInserir.setString(i++, obj.getTipoCobrancaENADE());
                    sqlInserir.setInt(i++, obj.getNrCreditoPorENADE());
                    sqlInserir.setDouble(i++, obj.getValorPorENADE());
                    sqlInserir.setString(i++, obj.getTipoCobrancaEstagio());
                    sqlInserir.setInt(i++, obj.getNrCreditoPorEstagio());
                    sqlInserir.setDouble(i++, obj.getValorPorEstagio());
                    sqlInserir.setDouble(i++, obj.getValorFixoDisciplinaIncluidaEAD());
                    sqlInserir.setBoolean(i++, obj.getUtilizarPoliticaCobrancaEspecificaParaOptativas());
                    sqlInserir.setString(i++, obj.getTipoCobrancaDisciplinaOptativa());
                    sqlInserir.setBoolean(i++, obj.getCobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet());
                    sqlInserir.setDouble(i++, obj.getValorDisciplinaOptativa());
                    sqlInserir.setDouble(i++, obj.getValorDisciplinaOptativaEAD());
                    sqlInserir.setDouble(i++, obj.getValorDisciplinaIncluidaDependencia());
                    sqlInserir.setDouble(i++, obj.getValorDisciplinaIncluidaDependenciaEAD());
                    sqlInserir.setBoolean(i++, obj.getUtilizarPoliticaDescontoDiscipRegularFeitasViaEAD());
                    sqlInserir.setString(i++, obj.getTipoDescontoDisciplinaRegularEAD());
                    sqlInserir.setDouble(i++, obj.getValorDescontoDisciplinaRegularEAD());
                    sqlInserir.setString(i++, obj.getTipoCobrancaExclusaoDisciplinaRegular());
                    sqlInserir.setDouble(i++, obj.getValorFixoDisciplinaExcluidaEAD());
                    sqlInserir.setBoolean(i++, obj.getApresentarSomenteParaDeterminadaFormaIngresso());
                    sqlInserir.setBoolean(i++, obj.getApresentarSomenteParaIngressanteNoSemestreAno());
                    sqlInserir.setBoolean(i++, obj.getFormaIngressoEntrevista());
                    sqlInserir.setBoolean(i++, obj.getFormaIngressoPortadorDiploma());
                    sqlInserir.setBoolean(i++, obj.getFormaIngressoTransferenciaInterna());
                    sqlInserir.setBoolean(i++, obj.getFormaIngressoProcessoSeletivo());
                    sqlInserir.setBoolean(i++, obj.getFormaIngressoTransferenciaExterna());
                    sqlInserir.setBoolean(i++, obj.getFormaIngressoReingresso());
                    sqlInserir.setBoolean(i++, obj.getFormaIngressoProuni());
                    sqlInserir.setBoolean(i++, obj.getFormaIngressoEnem());
                    sqlInserir.setBoolean(i++, obj.getFormaIngressoOutroTipoSelecao());
                    sqlInserir.setString(i++, obj.getAnoIngressante());
                    sqlInserir.setString(i++, obj.getSemestreIngressante());
                    sqlInserir.setBoolean(i++, obj.getApresentarSomenteParaAlunosIntegralizandoCurso());
                    sqlInserir.setBoolean(i++, obj.getConsiderarIntegralizandoEstiverCursandoUltimoPer2Vez());
                    sqlInserir.setString(i++, obj.getTipoControleAlunoIntegralizandoCurso());
                    sqlInserir.setDouble(i++, obj.getValorBaseDefinirAlunoIntegralizandoCurso());
                    sqlInserir.setBoolean(i++, obj.getPermiteRecebimentoOnlineCartaoCredito());
                    sqlInserir.setBoolean(i++, obj.getApresentarMatriculaOnlineExterna());
                    sqlInserir.setBoolean(i++, obj.getApresentarMatriculaOnlineProfessor());
                    sqlInserir.setBoolean(i++, obj.getApresentarMatriculaOnlineCoordenador());
                    sqlInserir.setBoolean(i++, obj.getConsiderarValorRateioBaseadoValorBaseComDescontosAplicados());
                    sqlInserir.setBoolean(i++, obj.getAbaterValorRateiroComoDescontoRateio());
                    sqlInserir.setBoolean(i++, obj.getGerarParcelasExtrasSeparadoMensalidadeAReceber());
                    sqlInserir.setBoolean(i++, obj.getConsiderarValorRateioExtraParcelaVencida());
                    sqlInserir.setString(i++, obj.getCategoria());
                    sqlInserir.setBoolean(i++, obj.getNaoControlarValorParcela());
                    Uteis.setValuePreparedStatement(obj.isGerarParcelaMaterialDidatico(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.isUsarUnidadeEnsinoEspecifica(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.isControlaDiaBaseVencimentoParcelaMaterialDidatico(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.isAplicarDescontosParcelasNoMaterialDidatico(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.isAplicarDescontoMaterialDidaticoDescontoAluno(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.isAplicarDescontoMaterialDidaticoDescontoInstitucional(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.isAplicarDescontoMaterialDidaticoDescontoProgressivo(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.isAplicarDescontoMaterialDidaticoDescontoConvenio(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.isAplicarDescontosDesconsiderandosVencimento(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoFinanceira(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getQuantidadeParcelasMaterialDidatico(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getDiaBaseVencimentoParcelaOutraUnidade(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getValorPorParcelaMaterialDidatico(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getTipoGeracaoMaterialDidatico(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getApresentarCondPagtoCRM(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getRegerarFinanceiro(), i++, sqlInserir);
                    
                    Uteis.setValuePreparedStatement(obj.getApresentarSomenteParaRenovacoesNoSemestreAno(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getAnoRenovacao(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getSemestreRenovacao(), i++, sqlInserir);            
                    Uteis.setValuePreparedStatement(obj.getLancarValorRatiadoSobreValorBaseContaReceber(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getValorPrimeiraParcela(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.isVencimentoPrimeiraParcelaAntesMaterialDidatico(), i++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.isUsaValorPrimeiraParcela(), i++, sqlInserir);            
                    Uteis.setValuePreparedStatement(obj.isConsiderarParcelasMaterialDidaticoReajustePreco(), i++, sqlInserir);
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            if (obj.getDefinirPlanoDescontoApresentarMatricula()) {
                incluirPlanoDescontoDisponivelMatricula(obj, usuario);
            }
            getFacadeFactory().getCondicaoPlanoFinanceiroCursoTurmaFacade().incluirCondicaoPlanoFinanceiroCursoTurma(obj);
            incluirPlanoDescontoPadrao(obj, usuario);
            obj.setNovoObj(Boolean.FALSE);

        } catch (Exception e) {
            obj.setCodigo(0);
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirPlanoDescontoPadrao(CondicaoPagamentoPlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws Exception {
        for (CondicaoPagamentoPlanoDescontoVO condicaoPagamentoPlanoDescontoVO : obj.getCondicaoPagamentoPlanoDescontoVOs()) {
            condicaoPagamentoPlanoDescontoVO.setCondicaoPagamentoPlanoFinanceiroCurso(obj.getCodigo());
            if (condicaoPagamentoPlanoDescontoVO.getNovoObj().booleanValue()) {
                getFacadeFactory().getCondicaoPagamentoPlanoDescontoFacade().incluir(condicaoPagamentoPlanoDescontoVO, usuario);
            } else {
                getFacadeFactory().getCondicaoPagamentoPlanoDescontoFacade().alterar(condicaoPagamentoPlanoDescontoVO, usuario);
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirPlanoDescontoDisponivelMatricula(CondicaoPagamentoPlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws Exception {
        for (PlanoDescontoDisponivelMatriculaVO planoDescontoDisponivelMatricula : obj.getPlanoDescontoDisponivelMatriculaVOs()) {
            planoDescontoDisponivelMatricula.setCondicaoPagamentoPlanoFinanceiroCurso(obj.getCodigo());
            if (planoDescontoDisponivelMatricula.getNovoObj().booleanValue()) {
                getFacadeFactory().getPlanoDescontoDisponivelMatriculaFacade().incluir(planoDescontoDisponivelMatricula, usuario);
            } else {
                getFacadeFactory().getPlanoDescontoDisponivelMatriculaFacade().alterar(planoDescontoDisponivelMatricula, usuario);
            }
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>PlanoFinanceiroCursoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PlanoFinanceiroCursoVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final CondicaoPagamentoPlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws Exception {
        try {
            CondicaoPagamentoPlanoFinanceiroCursoVO.validarDados(obj);
            /**
             * @author Leonardo Riciolle
             * Comentado 23/10/2014
             */
			// CondicaoPagamentoPlanoFinanceiroCurso.alterar(getIdEntidade());
            final String sql = "UPDATE CondicaoPagamentoPlanoFinanceiroCurso set valorMatricula=?, valorParcela=?, nrParcelasPeriodo=?, descontoProgressivoPadrao=?, "
                    + "valorMatriculaSistemaPorCredito=?, valorMinimoParcelaSistemaPorCredito=?, valorUnitarioCredito=?, restituirDiferencaValorMatriculaSistemaPorCredito=?, descricao=?, nomeFormula=?, "
                    + "formulaCalculoValorFinal=?, formulaUsoVariavel1=?, formulaCalculoVariavel1=?, utilizarVariavel1=?, variavel1=?, formulaUsoVariavel2=?, formulaCalculoVariavel2=?, utilizarVariavel2=?, "
                    + "variavel2=?, formulaUsoVariavel3=?, formulaCalculoVariavel3=?, utilizarVariavel3=?, variavel3=? , tituloVariavel1=?, tituloVariavel2=?, tituloVariavel3=?, planoFinanceiroCurso=?, "
                    + "utilizarValorMatriculaFixo=?, valorFixoDisciplinaIncluida=?, valorDescontoDisciplinaExcluida=?, gerarParcelasSeparadasParaDisciplinasIncluidas=?,"
                    + "gerarCobrancaPorDisciplinasIncluidas=?, gerarDescontoPorDiscipliaExcluidas=?, gerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo=?, "
                    + "numeroMaximoDisciplinaCursarParaGerarDescontos=?, ratiarValorDiferencaInclusaoExclusaoParaTodasParcelas=?, "
                    + "descricaoDuracao=?, textoPadraoContratoMatricula=?, descontoProgressivoPadraoMatricula=?, situacao=?, dataAtivacao=?, responsavelAtivacao=?, dataInativacao=?, responsavelInativacao=?, "
                    + "naoControlarMatricula=?, apresentarCondicaoVisaoAluno=?, definirPlanoDescontoApresentarMatricula=?, naoRegerarParcelaVencida = ?, centroreceita = ?, "
                    + "aplicarCalculoComBaseDescontosCalculados=?, descontoprogressivoprimeiraparcela=?, "
                    + "tipoCobrancaInclusaoDisciplinaRegular=?, cobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo=?, tipoCobrancaInclusaoDisciplinaDependencia=?, "
                    + "cobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet=?, tipoCalculoParcela=?, tipoUsoCondicaoPagamento=?, "
                    + "valorPorAtividadeComplementar=?, tipoCobrancaAtividadeComplementar=?, nrCreditoPorAtividadeComplementar=?, "
                    + "tipoCobrancaENADE=?, nrCreditoPorENADE=?, valorPorENADE=?, tipoCobrancaEstagio=?, nrCreditoPorEstagio=?, valorPorEstagio=?, "
                    + "valorFixoDisciplinaIncluidaEAD=?, utilizarPoliticaCobrancaEspecificaParaOptativas=?, tipoCobrancaDisciplinaOptativa=?, "
                    + "cobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet=?, valorDisciplinaOptativa=?, valorDisciplinaOptativaEAD=?, "
                    + "valorDisciplinaIncluidaDependencia=?, valorDisciplinaIncluidaDependenciaEAD=?, utilizarPoliticaDescontoDiscipRegularFeitasViaEAD=?, "
                    + "tipoDescontoDisciplinaRegularEAD=?, valorDescontoDisciplinaRegularEAD=?, "
                    + "tipoCobrancaExclusaoDisciplinaRegular=?, valorFixoDisciplinaExcluidaEAD=?, apresentarSomenteParaDeterminadaFormaIngresso=?, "
                    + "apresentarSomenteParaIngressanteNoSemestreAno=?, formaIngressoEntrevista=?, formaIngressoPortadorDiploma=?, formaIngressoTransferenciaInterna=?, "
                    + "formaIngressoProcessoSeletivo=?, formaIngressoTransferenciaExterna=?, formaIngressoReingresso=?, formaIngressoProuni=?, formaIngressoEnem=?, "
                    + "formaIngressoOutroTipoSelecao=?, anoIngressante=?, semestreIngressante=?, apresentarSomenteParaAlunosIntegralizandoCurso=?, "
                    + "considerarIntegralizandoEstiverCursandoUltimoPer2Vez=?, tipoControleAlunoIntegralizandoCurso=?, valorBaseDefinirAlunoIntegralizandoCurso=?, "
                    + "permiteRecebimentoOnlineCartaoCredito=?, apresentarMatriculaOnlineExterna=?, apresentarMatriculaOnlineProfessor=?, "
                    + "apresentarMatriculaOnlineCoordenador=?, considerarValorRateioBaseadoValorBaseComDescontosAplicados = ?,abaterValorRateiroComoDescontoRateio = ?, "
                    + "gerarParcelasExtrasSeparadoMensalidadeAReceber = ?, considerarValorRateioExtraParcelaVencida = ?, categoria = ?, naocontrolarvalorparcela = ?, "
                    + "gerarParcelaMaterialDidatico=?,usarUnidadeEnsinoEspecifica=? ,controlaDiaBaseVencimentoParcelaMaterialDidatico=? , aplicarDescontosParcelasNoMaterialDidatico=?, "
                    + "aplicarDescontoMaterialDidaticoDescontoAluno=?, aplicarDescontoMaterialDidaticoDescontoInstitucional=?, aplicarDescontoMaterialDidaticoDescontoProgressivo=?, "
                    + "aplicarDescontoMaterialDidaticoDescontoConvenio=?, aplicarDescontosDesconsiderandosVencimento=?, unidadeEnsinoFinanceira=?, quantidadeParcelasMaterialDidatico=?, "
                    + "diaBaseVencimentoParcelaOutraUnidade=?, valorPorParcelaMaterialDidatico=?, tipoGeracaoMaterialDidatico=?, apresentarCondPagtoCRM=?, regerarFinanceiro=?,  "
                    + "apresentarSomenteParaRenovacoesNoSemestreAno=?, anoRenovacao=?, semestreRenovacao=?, lancarValorRatiadoSobreValorBaseContaReceber =?, valorPrimeiraParcela=?, vencimentoPrimeiraParcelaAntesMaterialDidatico=?, "
                    + "usaValorPrimeiraParcela=?, considerarParcelasMaterialDidaticoReajustePreco = ? "
                    + "WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    int i = 1;
                    sqlAlterar.setDouble(i++, Uteis.arrendondarForcando2CadasDecimais(obj.getValorMatricula().doubleValue()));
                    sqlAlterar.setDouble(i++, Uteis.arrendondarForcando2CadasDecimais(obj.getValorParcela().doubleValue()));
                    sqlAlterar.setInt(i++, obj.getNrParcelasPeriodo().intValue());
                    if (obj.getDescontoProgressivoPadrao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(i++, obj.getDescontoProgressivoPadrao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(i++, 0);
                    }
                    sqlAlterar.setDouble(i++, Uteis.arrendondarForcando2CadasDecimais(obj.getValorMatriculaSistemaPorCredito()));
                    sqlAlterar.setDouble(i++, Uteis.arrendondarForcando2CadasDecimais(obj.getValorMinimoParcelaSistemaPorCredito()));
                    sqlAlterar.setDouble(i++, obj.getValorUnitarioCredito());
                    sqlAlterar.setBoolean(i++, obj.getRestituirDiferencaValorMatriculaSistemaPorCredito().booleanValue());
                    sqlAlterar.setString(i++, obj.getDescricao());
                    sqlAlterar.setString(i++, obj.getNomeFormula());
                    sqlAlterar.setString(i++, obj.getFormulaCalculoValorFinal());
                    sqlAlterar.setString(i++, obj.getFormulaUsoVariavel1());
                    sqlAlterar.setString(i++, obj.getFormulaCalculoVariavel1());
                    sqlAlterar.setBoolean(i++, obj.getUtilizarVariavel1());
                    sqlAlterar.setDouble(i++, Uteis.arrendondarForcando2CadasDecimais(obj.getVariavel1()));
                    sqlAlterar.setString(i++, obj.getFormulaUsoVariavel2());
                    sqlAlterar.setString(i++, obj.getFormulaCalculoVariavel2());
                    sqlAlterar.setBoolean(i++, obj.getUtilizarVariavel2());
                    sqlAlterar.setDouble(i++, Uteis.arrendondarForcando2CadasDecimais(obj.getVariavel2()));
                    sqlAlterar.setString(i++, obj.getFormulaUsoVariavel3());
                    sqlAlterar.setString(i++, obj.getFormulaCalculoVariavel3());
                    sqlAlterar.setBoolean(i++, obj.getUtilizarVariavel3());
                    sqlAlterar.setDouble(i++, Uteis.arrendondarForcando2CadasDecimais(obj.getVariavel3()));
                    sqlAlterar.setString(i++, obj.getTituloVariavel1());
                    sqlAlterar.setString(i++, obj.getTituloVariavel2());
                    sqlAlterar.setString(i++, obj.getTituloVariavel3());
                    sqlAlterar.setInt(i++, obj.getPlanoFinanceiroCurso());
                    sqlAlterar.setBoolean(i++, obj.getUtilizarValorMatriculaFixo());
                    sqlAlterar.setDouble(i++, obj.getValorFixoDisciplinaIncluida());
                    sqlAlterar.setDouble(i++, obj.getValorDescontoDisciplinaExcluida());
                    sqlAlterar.setBoolean(i++, obj.getGerarParcelasSeparadasParaDisciplinasIncluidas());
                    sqlAlterar.setBoolean(i++, obj.getGerarCobrancaPorDisciplinasIncluidas());
                    sqlAlterar.setBoolean(i++, obj.getGerarDescontoPorDiscipliaExcluidas());
                    sqlAlterar.setBoolean(i++, obj.getGerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo());
                    sqlAlterar.setInt(i++, obj.getNumeroMaximoDisciplinaCursarParaGerarDescontos());
                    sqlAlterar.setBoolean(i++, obj.getRatiarValorDiferencaInclusaoExclusaoParaTodasParcelas());
                    sqlAlterar.setString(i++, obj.getDescricaoDuracao());
                    if (obj.getTextoPadraoContratoMatricula().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(i++, obj.getTextoPadraoContratoMatricula().getCodigo());
                    } else {
                        sqlAlterar.setNull(i++, 0);
                    }
                    if (obj.getDescontoProgressivoPadraoMatricula().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(i++, obj.getDescontoProgressivoPadraoMatricula().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(i++, 0);
                    }
                    sqlAlterar.setString(i++, obj.getSituacao());
                    sqlAlterar.setDate(i++, Uteis.getDataJDBC(obj.getDataAtivacao()));
                    if (obj.getResponsavelAtivacao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(i++, obj.getResponsavelAtivacao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(i++, 0);
                    }
                    sqlAlterar.setDate(i++, Uteis.getDataJDBC(obj.getDataInativacao()));
                    if (obj.getResponsavelInativacao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(i++, obj.getResponsavelInativacao().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(i++, 0);
                    }
                    sqlAlterar.setBoolean(i++, obj.getNaoControlarMatricula());
                    sqlAlterar.setBoolean(i++, obj.getApresentarCondicaoVisaoAluno());
                    sqlAlterar.setBoolean(i++, obj.getDefinirPlanoDescontoApresentarMatricula());
                    sqlAlterar.setBoolean(i++, obj.getNaoRegerarParcelaVencida());
                    if (obj.getCentroReceita().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(i++, obj.getCentroReceita().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(i++, 0);
                    }
                    sqlAlterar.setBoolean(i++, obj.getAplicarCalculoComBaseDescontosCalculados());
                    sqlAlterar.setInt(i++, obj.getDescontoProgressivoPrimeiraParcela().getCodigo());

                    
                    sqlAlterar.setString(i++, obj.getTipoCobrancaInclusaoDisciplinaRegular());
                    sqlAlterar.setBoolean(i++, obj.getCobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo());
                    sqlAlterar.setString(i++, obj.getTipoCobrancaInclusaoDisciplinaDependencia());
                    sqlAlterar.setBoolean(i++, obj.getCobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet());
                    sqlAlterar.setString(i++, obj.getTipoCalculoParcela());
                    sqlAlterar.setString(i++, obj.getTipoUsoCondicaoPagamento());
                    
                    sqlAlterar.setDouble(i++, obj.getValorPorAtividadeComplementar());
                    sqlAlterar.setString(i++, obj.getTipoCobrancaAtividadeComplementar());
                    sqlAlterar.setInt(i++, obj.getNrCreditoPorAtividadeComplementar());
                    sqlAlterar.setString(i++, obj.getTipoCobrancaENADE());
                    sqlAlterar.setInt(i++, obj.getNrCreditoPorENADE());
                    sqlAlterar.setDouble(i++, obj.getValorPorENADE());
                    sqlAlterar.setString(i++, obj.getTipoCobrancaEstagio());
                    sqlAlterar.setInt(i++, obj.getNrCreditoPorEstagio());
                    sqlAlterar.setDouble(i++, obj.getValorPorEstagio());                  
                    
                    sqlAlterar.setDouble(i++, obj.getValorFixoDisciplinaIncluidaEAD());
                    sqlAlterar.setBoolean(i++, obj.getUtilizarPoliticaCobrancaEspecificaParaOptativas());
                    sqlAlterar.setString(i++, obj.getTipoCobrancaDisciplinaOptativa());
                    sqlAlterar.setBoolean(i++, obj.getCobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet());
                    sqlAlterar.setDouble(i++, obj.getValorDisciplinaOptativa());
                    sqlAlterar.setDouble(i++, obj.getValorDisciplinaOptativaEAD());
                    
                    sqlAlterar.setDouble(i++, obj.getValorDisciplinaIncluidaDependencia());
                    sqlAlterar.setDouble(i++, obj.getValorDisciplinaIncluidaDependenciaEAD());
                    sqlAlterar.setBoolean(i++, obj.getUtilizarPoliticaDescontoDiscipRegularFeitasViaEAD());
                    sqlAlterar.setString(i++, obj.getTipoDescontoDisciplinaRegularEAD());
                    sqlAlterar.setDouble(i++, obj.getValorDescontoDisciplinaRegularEAD());                    
                    sqlAlterar.setString(i++, obj.getTipoCobrancaExclusaoDisciplinaRegular());
                    sqlAlterar.setDouble(i++, obj.getValorFixoDisciplinaExcluidaEAD());
                    sqlAlterar.setBoolean(i++, obj.getApresentarSomenteParaDeterminadaFormaIngresso());
                    sqlAlterar.setBoolean(i++, obj.getApresentarSomenteParaIngressanteNoSemestreAno());
                    sqlAlterar.setBoolean(i++, obj.getFormaIngressoEntrevista());
                    sqlAlterar.setBoolean(i++, obj.getFormaIngressoPortadorDiploma());
                    sqlAlterar.setBoolean(i++, obj.getFormaIngressoTransferenciaInterna());
                    sqlAlterar.setBoolean(i++, obj.getFormaIngressoProcessoSeletivo());
                    sqlAlterar.setBoolean(i++, obj.getFormaIngressoTransferenciaExterna());
                    sqlAlterar.setBoolean(i++, obj.getFormaIngressoReingresso());
                    sqlAlterar.setBoolean(i++, obj.getFormaIngressoProuni());
                    sqlAlterar.setBoolean(i++, obj.getFormaIngressoEnem());
                    sqlAlterar.setBoolean(i++, obj.getFormaIngressoOutroTipoSelecao());
                    sqlAlterar.setString(i++, obj.getAnoIngressante());
                    sqlAlterar.setString(i++, obj.getSemestreIngressante());
                    sqlAlterar.setBoolean(i++, obj.getApresentarSomenteParaAlunosIntegralizandoCurso());
                    sqlAlterar.setBoolean(i++, obj.getConsiderarIntegralizandoEstiverCursandoUltimoPer2Vez());
                    sqlAlterar.setString(i++, obj.getTipoControleAlunoIntegralizandoCurso());
                    sqlAlterar.setDouble(i++, obj.getValorBaseDefinirAlunoIntegralizandoCurso());
                    sqlAlterar.setBoolean(i++, obj.getPermiteRecebimentoOnlineCartaoCredito());
                    sqlAlterar.setBoolean(i++, obj.getApresentarMatriculaOnlineExterna());
                    sqlAlterar.setBoolean(i++, obj.getApresentarMatriculaOnlineProfessor());
                    sqlAlterar.setBoolean(i++, obj.getApresentarMatriculaOnlineCoordenador());
                    sqlAlterar.setBoolean(i++, obj.getConsiderarValorRateioBaseadoValorBaseComDescontosAplicados());
                    sqlAlterar.setBoolean(i++, obj.getAbaterValorRateiroComoDescontoRateio());
                    sqlAlterar.setBoolean(i++, obj.getGerarParcelasExtrasSeparadoMensalidadeAReceber());
                    sqlAlterar.setBoolean(i++, obj.getConsiderarValorRateioExtraParcelaVencida());
                    sqlAlterar.setString(i++, obj.getCategoria());
                    sqlAlterar.setBoolean(i++, obj.getNaoControlarValorParcela());
                    Uteis.setValuePreparedStatement(obj.isGerarParcelaMaterialDidatico(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.isUsarUnidadeEnsinoEspecifica(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.isControlaDiaBaseVencimentoParcelaMaterialDidatico(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.isAplicarDescontosParcelasNoMaterialDidatico(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.isAplicarDescontoMaterialDidaticoDescontoAluno(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.isAplicarDescontoMaterialDidaticoDescontoInstitucional(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.isAplicarDescontoMaterialDidaticoDescontoProgressivo(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.isAplicarDescontoMaterialDidaticoDescontoConvenio(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.isAplicarDescontosDesconsiderandosVencimento(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoFinanceira(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getQuantidadeParcelasMaterialDidatico(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getDiaBaseVencimentoParcelaOutraUnidade(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getValorPorParcelaMaterialDidatico(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getTipoGeracaoMaterialDidatico(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getApresentarCondPagtoCRM(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getRegerarFinanceiro(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getApresentarSomenteParaRenovacoesNoSemestreAno(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getAnoRenovacao(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getSemestreRenovacao(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getLancarValorRatiadoSobreValorBaseContaReceber(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getValorPrimeiraParcela(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.isVencimentoPrimeiraParcelaAntesMaterialDidatico(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.isUsaValorPrimeiraParcela(), i++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.isConsiderarParcelasMaterialDidaticoReajustePreco(), i++, sqlAlterar);
                    sqlAlterar.setInt(i++, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            if (obj.getDefinirPlanoDescontoApresentarMatricula()) {
                alterarPlanoDescontoDisponivelMatricula(obj, usuario);
            }

            getFacadeFactory().getCondicaoPagamentoPlanoDescontoFacade().alterarPlanoDescontoPadraoCondicaoPagamentoVOs(obj.getCodigo(), obj.getCondicaoPagamentoPlanoDescontoVOs(), usuario);

            getFacadeFactory().getCondicaoPlanoFinanceiroCursoTurmaFacade().alterarCondicaoPlanoFinanceiroCursoTurma(obj);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarPlanoDescontoDisponivelMatricula(CondicaoPagamentoPlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws Exception {
        for (PlanoDescontoDisponivelMatriculaVO planoDesconto : obj.getPlanoDescontoDisponivelMatriculaVOs()) {
            planoDesconto.setCondicaoPagamentoPlanoFinanceiroCurso(obj.getCodigo());
            if (!Uteis.isAtributoPreenchido(planoDesconto.getCodigo())) {
                getFacadeFactory().getPlanoDescontoDisponivelMatriculaFacade().incluir(planoDesconto, usuario);
            } else {
                getFacadeFactory().getPlanoDescontoDisponivelMatriculaFacade().alterar(planoDesconto, usuario);
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirCondicaoPagamentoNaoUtilizada(Integer planoFinanceiroCursoPrm, List<CondicaoPagamentoPlanoFinanceiroCursoVO> objetosPersistir, UsuarioVO usuario) throws Exception {
        CondicaoPagamentoPlanoFinanceiroCurso.excluir(getIdEntidade());
        String sql = "DELETE FROM CondicaoPagamentoPlanoFinanceiroCurso WHERE (planoFinanceiroCurso = ?)";
        Iterator i = objetosPersistir.iterator();
        while (i.hasNext()) {
            CondicaoPagamentoPlanoFinanceiroCursoVO obj = (CondicaoPagamentoPlanoFinanceiroCursoVO) i.next();
            if (obj.getCodigo().intValue() != 0) {
                sql += " and codigo != " + obj.getCodigo().intValue();
            }
        }
        getConexao().getJdbcTemplate().update(sql+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), new Object[]{planoFinanceiroCursoPrm});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirCondicaoPagamentoNaoUtilizadaMatriculaPeriodo(Integer planoFinanceiroCursoPrm, List<CondicaoPagamentoPlanoFinanceiroCursoVO> objetosPersistir, UsuarioVO usuario) throws Exception {
        CondicaoPagamentoPlanoFinanceiroCurso.excluir(getIdEntidade());
        String sql = "DELETE FROM CondicaoPagamentoPlanoFinanceiroCurso WHERE (planoFinanceiroCurso = ?)";
        sql += " and codigo not in( select distinct condicaopagamentoplanofinanceirocurso from matriculaperiodo where planofinanceirocurso = ? and condicaopagamentoplanofinanceirocurso is not null)";
        Iterator i = objetosPersistir.iterator();
        while (i.hasNext()) {
            CondicaoPagamentoPlanoFinanceiroCursoVO obj = (CondicaoPagamentoPlanoFinanceiroCursoVO) i.next();
            if (obj.getCodigo().intValue() != 0) {
                sql += " and codigo != " + obj.getCodigo().intValue();
            }
        }
        getConexao().getJdbcTemplate().update(sql + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), new Object[]{planoFinanceiroCursoPrm, planoFinanceiroCursoPrm});
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>PlanoFinanceiroCursoVO</code>. Sempre localiza o
     * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>PlanoFinanceiroCursoVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(CondicaoPagamentoPlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws Exception {
        try {
        	/**
             * @author Leonardo Riciolle
             * Comentado 23/10/2014
             */
			// CondicaoPagamentoPlanoFinanceiroCurso.excluir(getIdEntidade());
            String sql = "DELETE FROM CondicaoPagamentoPlanoFinanceiroCurso WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>PlanoFinanceiroCurso</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>PlanoFinanceiroCursoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<CondicaoPagamentoPlanoFinanceiroCursoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM CondicaoPagamentoPlanoFinanceiroCurso WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<CondicaoPagamentoPlanoFinanceiroCursoVO> consultarPorCodigoPlanoFinanceiroCurso(Integer valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT * FROM CondicaoPagamentoPlanoFinanceiroCurso WHERE planoFinanceiroCurso = ");
        sqlStr.append(valorConsulta.intValue());
        if (!situacao.equals("")) {
            sqlStr.append(" and situacao = '");
            sqlStr.append(situacao.toString());
            sqlStr.append("'");
        }
        sqlStr.append("  ORDER BY descricao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public List<CondicaoPagamentoPlanoFinanceiroCursoVO> consultarPorCodigoPlanoFinanceiroCursoECategoria(Integer codigoPlanoFinanceiroCurso, String categoria, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("SELECT * FROM CondicaoPagamentoPlanoFinanceiroCurso WHERE planoFinanceiroCurso = ");
        sqlStr.append(codigoPlanoFinanceiroCurso.intValue());
        
        if(categoria!= null && !categoria.equals("")) {
        	sqlStr.append(" and categoria like '").append(categoria).append("' ");	
        } else {
        	//Buscar as condicoes de pagamento antigas ( com o valor do campo categoria nulo no banco de dados)
        	sqlStr.append(" and (categoria like '").append(categoria).append("' ");
        	sqlStr.append(" or categoria is null )");
        }
       	
        
        if (!situacao.equals("")) {
            sqlStr.append(" and situacao = '");
            sqlStr.append(situacao.toString());
            sqlStr.append("'");
        }
        sqlStr.append("  ORDER BY descricao");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<CondicaoPagamentoPlanoFinanceiroCursoVO> consultarPorCodigoUnidadeEnsino(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	//ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sqlStr = new StringBuilder();
    	sqlStr.append("select CondicaoPagamentoPlanoFinanceiroCurso.*, Turma.codigo as codTurma from CondicaoPagamentoPlanoFinanceiroCurso  ");
    	sqlStr.append("inner join turma on turma.planofinanceirocurso = CondicaoPagamentoPlanoFinanceiroCurso.planofinanceirocurso ");
    	sqlStr.append(" and turma.categoriacondicaopagamento  = CondicaoPagamentoPlanoFinanceiroCurso.categoria ");
    	sqlStr.append("where turma.unidadeensino = ");
    	sqlStr.append(valorConsulta.intValue());    	
    	sqlStr.append(" and CondicaoPagamentoPlanoFinanceiroCurso.situacao = 'AT' and CondicaoPagamentoPlanoFinanceiroCurso.apresentarCondPagtoCRM = true and turma.situacao = 'AB' ORDER BY turma.identificadorturma, CondicaoPagamentoPlanoFinanceiroCurso.codigo");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    	
    	List<CondicaoPagamentoPlanoFinanceiroCursoVO> vetResultado = new ArrayList<CondicaoPagamentoPlanoFinanceiroCursoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosConsultarPorUnidadeEnsino(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;    	
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>PlanoFinanceiroCursoVO</code> resultantes da consulta.
     */
    public static List<CondicaoPagamentoPlanoFinanceiroCursoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<CondicaoPagamentoPlanoFinanceiroCursoVO> vetResultado = new ArrayList<CondicaoPagamentoPlanoFinanceiroCursoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>PlanoFinanceiroCursoVO</code>.
     *
     * @return O objeto da classe <code>PlanoFinanceiroCursoVO</code> com os dados devidamente montados.
     */
    public static CondicaoPagamentoPlanoFinanceiroCursoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        CondicaoPagamentoPlanoFinanceiroCursoVO obj = new CondicaoPagamentoPlanoFinanceiroCursoVO();
        
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.setPlanoFinanceiroCurso(dadosSQL.getInt("planoFinanceiroCurso"));
        obj.setCategoria(dadosSQL.getString("categoria"));
        
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            obj.setGerarParcelaMaterialDidatico(dadosSQL.getBoolean("gerarParcelaMaterialDidatico"));
            obj.setRegerarFinanceiro(dadosSQL.getBoolean("regerarFinanceiro"));
            return obj;
        }
        obj.setValorMatricula(new Double(dadosSQL.getDouble("valorMatricula")));
        obj.setNaoControlarMatricula(dadosSQL.getBoolean("naoControlarMatricula"));
        obj.setNaoRegerarParcelaVencida(dadosSQL.getBoolean("naoRegerarParcelaVencida"));
        obj.getDescontoProgressivoPadraoMatricula().setCodigo(new Integer(dadosSQL.getInt("descontoProgressivoPadraoMatricula")));
        obj.setValorParcela(new Double(dadosSQL.getDouble("valorParcela")));
        obj.setValorPrimeiraParcela(new Double(dadosSQL.getDouble("valorPrimeiraParcela")));
        obj.setVencimentoPrimeiraParcelaAntesMaterialDidatico(dadosSQL.getBoolean("vencimentoPrimeiraParcelaAntesMaterialDidatico"));
        obj.setUsaValorPrimeiraParcela(dadosSQL.getBoolean("usaValorPrimeiraParcela"));
        obj.setNrParcelasPeriodo(new Integer(dadosSQL.getInt("nrParcelasPeriodo")));
        obj.getDescontoProgressivoPadrao().setCodigo(new Integer(dadosSQL.getInt("descontoProgressivoPadrao")));
        obj.getDescontoProgressivoPrimeiraParcela().setCodigo(new Integer(dadosSQL.getInt("descontoprogressivoprimeiraparcela")));
        obj.getCentroReceita().setCodigo(new Integer(dadosSQL.getInt("centroReceita")));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setDataAtivacao(dadosSQL.getDate("dataAtivacao"));
        obj.getResponsavelAtivacao().setCodigo(dadosSQL.getInt("responsavelAtivacao"));
        obj.setDataInativacao(dadosSQL.getDate("dataInativacao"));
        obj.getResponsavelInativacao().setCodigo(dadosSQL.getInt("responsavelInativacao"));
        obj.setDefinirPlanoDescontoApresentarMatricula(dadosSQL.getBoolean("definirPlanoDescontoApresentarMatricula"));
        obj.setApresentarCondPagtoCRM(dadosSQL.getBoolean("apresentarCondPagtoCRM"));
        
        obj.setNaoControlarValorParcela(dadosSQL.getBoolean("naoControlarValorParcela"));
        
        obj.getTextoPadraoContratoMatricula().setCodigo(new Integer(dadosSQL.getInt("textoPadraoContratoMatricula")));
        
      
        obj.setValorMatriculaSistemaPorCredito(new Double(dadosSQL.getDouble("valorMatriculaSistemaPorCredito")));
        obj.setValorMinimoParcelaSistemaPorCredito(new Double(dadosSQL.getDouble("valorMinimoParcelaSistemaPorCredito")));
        obj.setValorUnitarioCredito(new Double(dadosSQL.getDouble("valorUnitarioCredito")));
        obj.setRestituirDiferencaValorMatriculaSistemaPorCredito(dadosSQL.getBoolean("restituirDiferencaValorMatriculaSistemaPorCredito"));
        
        obj.setNomeFormula(dadosSQL.getString("nomeFormula"));
        obj.setFormulaCalculoValorFinal(dadosSQL.getString("formulaCalculoValorFinal"));
        obj.setFormulaUsoVariavel1(dadosSQL.getString("formulaUsoVariavel1"));
        obj.setFormulaCalculoVariavel1(dadosSQL.getString("formulaCalculoVariavel1"));
        obj.setUtilizarVariavel1(dadosSQL.getBoolean("utilizarVariavel1"));
        obj.setVariavel1(dadosSQL.getDouble("variavel1"));
        obj.setFormulaUsoVariavel2(dadosSQL.getString("formulaUsoVariavel2"));
        obj.setFormulaCalculoVariavel2(dadosSQL.getString("formulaCalculoVariavel2"));
        obj.setUtilizarVariavel2(dadosSQL.getBoolean("utilizarVariavel2"));
        obj.setVariavel2(dadosSQL.getDouble("variavel2"));
        obj.setFormulaUsoVariavel3(dadosSQL.getString("formulaUsoVariavel3"));
        obj.setFormulaCalculoVariavel3(dadosSQL.getString("formulaCalculoVariavel3"));
        obj.setUtilizarVariavel3(dadosSQL.getBoolean("utilizarVariavel3"));
        obj.setVariavel3(dadosSQL.getDouble("variavel3"));
        obj.setTituloVariavel1(dadosSQL.getString("tituloVariavel1"));
        obj.setTituloVariavel2(dadosSQL.getString("tituloVariavel2"));
        obj.setTituloVariavel3(dadosSQL.getString("tituloVariavel3"));
        obj.setPlanoFinanceiroCurso(dadosSQL.getInt("planoFinanceiroCurso"));
        obj.setUtilizarValorMatriculaFixo(dadosSQL.getBoolean("utilizarValorMatriculaFixo"));
        obj.setRatiarValorDiferencaInclusaoExclusaoParaTodasParcelas(dadosSQL.getBoolean("ratiarValorDiferencaInclusaoExclusaoParaTodasParcelas"));
        obj.setValorFixoDisciplinaIncluida(dadosSQL.getDouble("valorFixoDisciplinaIncluida"));
        obj.setValorDescontoDisciplinaExcluida(dadosSQL.getDouble("valorDescontoDisciplinaExcluida"));
        obj.setGerarParcelasSeparadasParaDisciplinasIncluidas(dadosSQL.getBoolean("gerarParcelasSeparadasParaDisciplinasIncluidas"));
        obj.setApresentarCondicaoVisaoAluno(dadosSQL.getBoolean("apresentarCondicaoVisaoAluno"));
        obj.setGerarCobrancaPorDisciplinasIncluidas(dadosSQL.getBoolean("gerarCobrancaPorDisciplinasIncluidas"));
        obj.setGerarDescontoPorDiscipliaExcluidas(dadosSQL.getBoolean("gerarDescontoPorDiscipliaExcluidas"));
        obj.setGerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo(dadosSQL.getBoolean("gerarDescontoPorDisciplinExcluidasSomenteUltimoPeriodo"));
        obj.setNumeroMaximoDisciplinaCursarParaGerarDescontos(dadosSQL.getInt("numeroMaximoDisciplinaCursarParaGerarDescontos"));
        obj.setAplicarCalculoComBaseDescontosCalculados(dadosSQL.getBoolean("aplicarCalculoComBaseDescontosCalculados"));

        obj.setDescricaoDuracao(dadosSQL.getString("descricaoDuracao"));

        obj.setTipoCobrancaInclusaoDisciplinaRegular(dadosSQL.getString("tipoCobrancaInclusaoDisciplinaRegular"));
        obj.setCobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo(dadosSQL.getBoolean("cobrarDiscipRegularQuandoUltrapassarCreditosCHMaxPeriodoLetivo"));
        obj.setTipoCobrancaInclusaoDisciplinaDependencia(dadosSQL.getString("tipoCobrancaInclusaoDisciplinaDependencia"));
        obj.setCobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet(dadosSQL.getBoolean("cobrarDiscipDependenciaQuandoUltrapassarCreditosCHMaxPerLet"));
        obj.setTipoCalculoParcela(dadosSQL.getString("tipoCalculoParcela"));
        obj.setTipoUsoCondicaoPagamento(dadosSQL.getString("tipoUsoCondicaoPagamento"));

        obj.setValorPorAtividadeComplementar(dadosSQL.getDouble("valorPorAtividadeComplementar"));
        obj.setTipoCobrancaAtividadeComplementar(dadosSQL.getString("tipoCobrancaAtividadeComplementar"));
        obj.setNrCreditoPorAtividadeComplementar(dadosSQL.getInt("nrCreditoPorAtividadeComplementar"));
        obj.setTipoCobrancaENADE(dadosSQL.getString("tipoCobrancaENADE"));
        obj.setNrCreditoPorENADE(dadosSQL.getInt("nrCreditoPorENADE"));
        obj.setValorPorENADE(dadosSQL.getDouble("valorPorENADE"));
        obj.setTipoCobrancaEstagio(dadosSQL.getString("tipoCobrancaEstagio"));
        obj.setNrCreditoPorEstagio(dadosSQL.getInt("nrCreditoPorEstagio"));
        obj.setValorPorEstagio(dadosSQL.getDouble("valorPorEstagio"));
        obj.setValorFixoDisciplinaIncluidaEAD(dadosSQL.getDouble("valorFixoDisciplinaIncluidaEAD"));
        obj.setUtilizarPoliticaCobrancaEspecificaParaOptativas(dadosSQL.getBoolean("utilizarPoliticaCobrancaEspecificaParaOptativas"));
        obj.setTipoCobrancaDisciplinaOptativa(dadosSQL.getString("tipoCobrancaDisciplinaOptativa"));
        obj.setCobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet(dadosSQL.getBoolean("cobrarDiscipOptativaQuandoUltrapassarCredCHMaxPerLet"));
        obj.setValorDisciplinaOptativa(dadosSQL.getDouble("valorDisciplinaOptativa"));
        obj.setValorDisciplinaOptativaEAD(dadosSQL.getDouble("valorDisciplinaOptativaEAD"));
        obj.setValorDisciplinaIncluidaDependencia(dadosSQL.getDouble("valorDisciplinaIncluidaDependencia"));
        obj.setValorDisciplinaIncluidaDependenciaEAD(dadosSQL.getDouble("valorDisciplinaIncluidaDependenciaEAD"));
        obj.setUtilizarPoliticaDescontoDiscipRegularFeitasViaEAD(dadosSQL.getBoolean("utilizarPoliticaDescontoDiscipRegularFeitasViaEAD"));
        obj.setTipoDescontoDisciplinaRegularEAD(dadosSQL.getString("tipoDescontoDisciplinaRegularEAD"));
        obj.setValorDescontoDisciplinaRegularEAD(dadosSQL.getDouble("valorDescontoDisciplinaRegularEAD"));    
        
        obj.setTipoCobrancaExclusaoDisciplinaRegular(dadosSQL.getString("tipoCobrancaExclusaoDisciplinaRegular"));
        obj.setValorFixoDisciplinaExcluidaEAD(dadosSQL.getDouble("valorFixoDisciplinaExcluidaEAD"));
        obj.setApresentarSomenteParaDeterminadaFormaIngresso(dadosSQL.getBoolean("apresentarSomenteParaDeterminadaFormaIngresso"));
        obj.setApresentarSomenteParaIngressanteNoSemestreAno(dadosSQL.getBoolean("apresentarSomenteParaIngressanteNoSemestreAno"));
        obj.setFormaIngressoEnem(dadosSQL.getBoolean("formaIngressoEnem"));
        obj.setFormaIngressoEntrevista(dadosSQL.getBoolean("formaIngressoEntrevista"));
        obj.setFormaIngressoOutroTipoSelecao(dadosSQL.getBoolean("formaIngressoOutroTipoSelecao"));
        obj.setFormaIngressoPortadorDiploma(dadosSQL.getBoolean("formaIngressoPortadorDiploma"));
        obj.setFormaIngressoProcessoSeletivo(dadosSQL.getBoolean("formaIngressoProcessoSeletivo"));
        obj.setFormaIngressoProuni(dadosSQL.getBoolean("formaIngressoProuni"));
        obj.setFormaIngressoReingresso(dadosSQL.getBoolean("formaIngressoReingresso"));
        obj.setFormaIngressoTransferenciaExterna(dadosSQL.getBoolean("formaIngressoTransferenciaExterna"));
        obj.setFormaIngressoTransferenciaInterna(dadosSQL.getBoolean("formaIngressoTransferenciaInterna"));
        obj.setSemestreIngressante(dadosSQL.getString("semestreIngressante"));
        obj.setAnoIngressante(dadosSQL.getString("anoIngressante"));
        obj.setApresentarSomenteParaAlunosIntegralizandoCurso(dadosSQL.getBoolean("apresentarSomenteParaAlunosIntegralizandoCurso"));
        obj.setConsiderarIntegralizandoEstiverCursandoUltimoPer2Vez(dadosSQL.getBoolean("considerarIntegralizandoEstiverCursandoUltimoPer2Vez"));
        obj.setTipoControleAlunoIntegralizandoCurso(dadosSQL.getString("tipoControleAlunoIntegralizandoCurso"));
        obj.setValorBaseDefinirAlunoIntegralizandoCurso(dadosSQL.getDouble("valorBaseDefinirAlunoIntegralizandoCurso"));
        obj.setPermiteRecebimentoOnlineCartaoCredito(dadosSQL.getBoolean("permiteRecebimentoOnlineCartaoCredito"));
        obj.setApresentarMatriculaOnlineExterna(dadosSQL.getBoolean("apresentarMatriculaOnlineExterna"));
        obj.setApresentarMatriculaOnlineProfessor(dadosSQL.getBoolean("apresentarMatriculaOnlineProfessor"));
        obj.setApresentarMatriculaOnlineCoordenador(dadosSQL.getBoolean("apresentarMatriculaOnlineCoordenador"));
        obj.setConsiderarValorRateioBaseadoValorBaseComDescontosAplicados(dadosSQL.getBoolean("considerarValorRateioBaseadoValorBaseComDescontosAplicados"));
        obj.setAbaterValorRateiroComoDescontoRateio(dadosSQL.getBoolean("abaterValorRateiroComoDescontoRateio"));
        obj.setGerarParcelasExtrasSeparadoMensalidadeAReceber(dadosSQL.getBoolean("gerarParcelasExtrasSeparadoMensalidadeAReceber"));
        obj.setConsiderarValorRateioExtraParcelaVencida(dadosSQL.getBoolean("considerarValorRateioExtraParcelaVencida"));
        
        obj.setGerarParcelaMaterialDidatico(dadosSQL.getBoolean("gerarParcelaMaterialDidatico"));
        obj.setUsarUnidadeEnsinoEspecifica(dadosSQL.getBoolean("usarUnidadeEnsinoEspecifica"));
        obj.setControlaDiaBaseVencimentoParcelaMaterialDidatico(dadosSQL.getBoolean("controlaDiaBaseVencimentoParcelaMaterialDidatico"));
        obj.setAplicarDescontosParcelasNoMaterialDidatico(dadosSQL.getBoolean("aplicarDescontosParcelasNoMaterialDidatico"));
        obj.setAplicarDescontoMaterialDidaticoDescontoAluno(dadosSQL.getBoolean("aplicarDescontoMaterialDidaticoDescontoAluno"));
        obj.setAplicarDescontoMaterialDidaticoDescontoInstitucional(dadosSQL.getBoolean("aplicarDescontoMaterialDidaticoDescontoInstitucional"));
        obj.setAplicarDescontoMaterialDidaticoDescontoProgressivo(dadosSQL.getBoolean("aplicarDescontoMaterialDidaticoDescontoProgressivo"));
        obj.setAplicarDescontoMaterialDidaticoDescontoConvenio(dadosSQL.getBoolean("aplicarDescontoMaterialDidaticoDescontoConvenio"));
        obj.setAplicarDescontosDesconsiderandosVencimento(dadosSQL.getBoolean("aplicarDescontosDesconsiderandosVencimento"));
        obj.getUnidadeEnsinoFinanceira().setCodigo(dadosSQL.getInt("unidadeEnsinoFinanceira"));;
        obj.setQuantidadeParcelasMaterialDidatico(dadosSQL.getLong("quantidadeParcelasMaterialDidatico"));
        obj.setDiaBaseVencimentoParcelaOutraUnidade(dadosSQL.getLong("diaBaseVencimentoParcelaOutraUnidade"));
        obj.setValorPorParcelaMaterialDidatico(dadosSQL.getDouble("valorPorParcelaMaterialDidatico"));
        obj.setRegerarFinanceiro(dadosSQL.getBoolean("regerarFinanceiro"));
        if(Uteis.isAtributoPreenchido(dadosSQL.getString("tipoGeracaoMaterialDidatico"))){
        	obj.setTipoGeracaoMaterialDidatico(TipoGeracaoMaterialDidaticoEnum.valueOf(dadosSQL.getString("tipoGeracaoMaterialDidatico")));	
        }

        obj.setApresentarSomenteParaRenovacoesNoSemestreAno(dadosSQL.getBoolean("apresentarSomenteParaRenovacoesNoSemestreAno"));
        obj.setSemestreRenovacao(dadosSQL.getString("semestreRenovacao"));
        obj.setAnoRenovacao(dadosSQL.getString("anoRenovacao"));
        obj.setLancarValorRatiadoSobreValorBaseContaReceber(dadosSQL.getBoolean("lancarValorRatiadoSobreValorBaseContaReceber"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        montarDadosCentroReceita(obj, nivelMontarDados, usuario);
        obj.setCondicaoPagamentoPlanoDescontoVOs(getFacadeFactory().getCondicaoPagamentoPlanoDescontoFacade().consultarPorCondicaoPagamentoPlanoFinanceiroCurso(obj.getCodigo(), false, usuario));
        obj.setCondicaoPlanoFinanceiroCursoTurmaVOs(getFacadeFactory().getCondicaoPlanoFinanceiroCursoTurmaFacade().consultarPorCondicaoPlanoFinanceiroCurso(obj.getCodigo()));
        obj.setNovoObj(false);
        
        if (obj.getDefinirPlanoDescontoApresentarMatricula()) {
            obj.setPlanoDescontoDisponivelMatriculaVOs(montarDadosPlanoDescontoDisponivelMatricula(obj, nivelMontarDados, usuario));
        }
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosTextoPadrao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosDescontoProgressivoPadrao(obj, nivelMontarDados, usuario);
        montarDadosDescontoProgressivoPrimeiraParcela(obj, nivelMontarDados, usuario);
        montarDadosUnidadeEnsinoFinanceira(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);

        return obj;
    }

    
    public static CondicaoPagamentoPlanoFinanceiroCursoVO montarDadosConsultarPorUnidadeEnsino(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	CondicaoPagamentoPlanoFinanceiroCursoVO obj = new CondicaoPagamentoPlanoFinanceiroCursoVO();
		obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
		obj.setDescricao(dadosSQL.getString("descricao"));
		obj.setPlanoFinanceiroCurso(dadosSQL.getInt("planoFinanceiroCurso"));
    	obj.setValorParcela(new Double(dadosSQL.getDouble("valorParcela")));
    	obj.setNrParcelasPeriodo(new Integer(dadosSQL.getInt("nrParcelasPeriodo")));
    	obj.setCodTurma(new Integer(dadosSQL.getInt("codTurma")));    	
    	return obj;
    }

    public static List<PlanoDescontoDisponivelMatriculaVO> montarDadosPlanoDescontoDisponivelMatricula(CondicaoPagamentoPlanoFinanceiroCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<PlanoDescontoDisponivelMatriculaVO> lista = new ArrayList<PlanoDescontoDisponivelMatriculaVO>(0);
        lista = getFacadeFactory().getPlanoDescontoDisponivelMatriculaFacade().consultarPorCodigoPlanoDescontoDisponivelMatricula(obj.getCodigo(), false, nivelMontarDados, usuario);
        return lista;
    }

    public static void montarDadosTextoPadrao(CondicaoPagamentoPlanoFinanceiroCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getTextoPadraoContratoMatricula().getCodigo().intValue() != 0) {
            obj.setTextoPadraoContratoMatricula(getFacadeFactory().getTextoPadraoFacade().consultarPorChavePrimaria(obj.getTextoPadraoContratoMatricula().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
        }
    }

    public static void montarDadosCentroReceita(CondicaoPagamentoPlanoFinanceiroCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getCentroReceita().getCodigo().intValue() == 0) {
            obj.setCentroReceita(new CentroReceitaVO());
            return;
        }
        obj.setCentroReceita(getFacadeFactory().getCentroReceitaFacade().consultarPorChavePrimaria(obj.getCentroReceita().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>DescontoProgressivoVO</code> relacionado ao
     * objeto <code>PlanoFinanceiroCursoVO</code>. Faz uso da chave primária da classe
     * <code>DescontoProgressivoVO</code> para realizar a consulta.
     *
     * @param obj
     *            Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosDescontoProgressivoPadrao(CondicaoPagamentoPlanoFinanceiroCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDescontoProgressivoPadrao().getCodigo().intValue() == 0) {
            obj.setDescontoProgressivoPadrao(new DescontoProgressivoVO());
            return;
        }
        obj.setDescontoProgressivoPadrao(getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(obj.getDescontoProgressivoPadrao().getCodigo(), usuario));
    }

    public static void montarDadosDescontoProgressivoPrimeiraParcela(CondicaoPagamentoPlanoFinanceiroCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getDescontoProgressivoPrimeiraParcela().getCodigo().intValue() == 0) {
            obj.setDescontoProgressivoPrimeiraParcela(new DescontoProgressivoVO());
            return;
        }
        obj.setDescontoProgressivoPrimeiraParcela(getFacadeFactory().getDescontoProgressivoFacade().consultarPorChavePrimaria(obj.getDescontoProgressivoPrimeiraParcela().getCodigo(), usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>PlanoFinanceiroCursoVO</code> através de sua chave
     * primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public CondicaoPagamentoPlanoFinanceiroCursoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM CondicaoPagamentoPlanoFinanceiroCurso WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados (Condição Pagamento Plano Financeiro Curso).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return CondicaoPagamentoPlanoFinanceiroCurso.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        CondicaoPagamentoPlanoFinanceiroCurso.idEntidade = idEntidade;
    }

    public void validarDadosAdicionarCondicaoPagamentoPlanoFinanceiroCurso(CondicaoPagamentoPlanoFinanceiroCursoVO obj, UsuarioVO usuario) throws ConsistirException {
        if (obj.getDescricao().equals("")) {
            throw new ConsistirException("O campo DESCRIÇÃO (Condição Plano Financeiro Curso) deve ser informado.");
        }
        
        if (obj.getTipoCalculoParcela().equals("VF")) {
            if (obj.getTipoUsoCondicaoPagamento().equals("MATRICULA_REGULAR")) {
                if (obj.getNrParcelasPeriodo().intValue() == 0 && !obj.getNaoControlarValorParcela()) {
                    throw new ConsistirException("O campo NÚMERO PARCELAS PERÍODO (Condição Plano Financeiro Curso) deve ser informado.");
                }
                if ((obj.getUtilizarValorMatriculaFixo()) && (!obj.getNaoControlarMatricula())) {
                    if (obj.getValorMatricula().doubleValue() == 0) {
                        throw new ConsistirException("O campo VALOR INTEGRAL MATRÍCULA (Condição Plano Financeiro Curso) deve ser informado.");
                    }
                }
                if (obj.getValorParcela().doubleValue() == 0 && !obj.getNaoControlarValorParcela()) {
                    throw new ConsistirException("O campo VALOR INTEGRAL PARCELA (Condição Plano Financeiro Curso) deve ser informado.");
                }
            } else {
                if (obj.getUtilizaTipoCobrancaPorCreditoParaMatriculaEspecial()) {
                    if (obj.getValorUnitarioCredito().doubleValue() == 0.0) {
                        throw new ConsistirException("O campo VALOR UNITÁRIO CRÉDITO (Condição Plano Financeiro Curso) deve ser informado PARA CONDIÇÕES DE PAGAMENTO MATRÍCULAS ESPECIAIS.");
                    }
                }
            }
        }
        if (obj.getTipoCalculoParcela().equals("VC")) {
            if (obj.getUtilizarValorMatriculaFixo() &&  obj.getValorUnitarioCredito().doubleValue() == 0.0  && !obj.getNaoControlarValorParcela()) {
                throw new ConsistirException("O campo VALOR UNITÁRIO CRÉDITO (Condição Plano Financeiro Curso) deve ser informado.");
            }
            if (obj.getNrParcelasPeriodo().intValue() == 0 && !obj.getNaoControlarValorParcela()) {
                throw new ConsistirException("O campo NÚMERO PARCELAS PERÍODO (Condição Plano Financeiro Curso) deve ser informado.");
            }
        }

        if (obj.getTipoCalculoParcela().equals("FC")) {
            if (obj.getNrParcelasPeriodo().intValue() == 0  && !obj.getNaoControlarValorParcela()) {
                throw new ConsistirException("O campo NÚMERO PARCELAS PERÍODO (Plano Financeiro Curso) deve ser informado.");
            }
            if (obj.getFormulaCalculoValorFinal().equals("")) {
                throw new ConsistirException("O campo FÓRMULA CALCULO VALOR FINAL (Plano Financeiro Curso) deve ser informado.");
            }
        }
        
        if(obj.isGerarParcelaMaterialDidatico()){
        	if (obj.isUsarUnidadeEnsinoEspecifica() && !Uteis.isAtributoPreenchido(obj.getUnidadeEnsinoFinanceira())) {
                throw new ConsistirException("O campo Unidade Ensino Gerar Parcela (Aba Material Didátivo) deve ser informado.");
            }
        	if (!Uteis.isAtributoPreenchido(obj.getQuantidadeParcelasMaterialDidatico())) {
        		throw new ConsistirException("O campo Quantidade Parcelas Material Didático (Aba Material Didátivo) deve ser informado.");
        	}
        	if (!Uteis.isAtributoPreenchido(obj.getValorPorParcelaMaterialDidatico())) {
        		throw new ConsistirException("O campo Valor Por Parcela Material Didático (Aba Material Didátivo) deve ser informado.");
        	}
        	if (obj.isControlaDiaBaseVencimentoParcelaMaterialDidatico() && !Uteis.isAtributoPreenchido(obj.getDiaBaseVencimentoParcelaOutraUnidade())) {
        		throw new ConsistirException("O campo Dia Base De Vencimento Parcela Material Didático (Aba Material Didátivo) deve ser informado.");
        	}
        	if (obj.isAplicarDescontosParcelasNoMaterialDidatico() && !obj.isAplicarDescontoMaterialDidaticoDescontoAluno() && !obj.isAplicarDescontoMaterialDidaticoDescontoConvenio() && !obj.isAplicarDescontoMaterialDidaticoDescontoInstitucional() && !obj.isAplicarDescontoMaterialDidaticoDescontoProgressivo()) {
        		throw new ConsistirException("Como o campo \"Aplicar Descontos Parcelas No Material Didático\" esta marcado. Deve ser informado pelo menos umas das 4 opções de Descontos: Aluno, Institucional, Progressivo ou Convênio. (Aba Material Didátivo).");
        	}
        }
        Uteis.checkState(obj.isUsaValorPrimeiraParcela() && !Uteis.isAtributoPreenchido(obj.getValorPrimeiraParcela()), "O Valor 1ª Mensalidade (Entrada) deve ser informado. ");
        if(!obj.isUsaValorPrimeiraParcela() && Uteis.isAtributoPreenchido(obj.getValorPrimeiraParcela())) {
        	obj.setValorPrimeiraParcela(0.0);	
        }
    }

    public void inicializarDadosAtivacao(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoVO, UsuarioVO usuarioVO) throws Exception {
        condicaoPagamentoVO.setSituacao("AT");
        condicaoPagamentoVO.setDataAtivacao(new Date());
        condicaoPagamentoVO.setResponsavelAtivacao(usuarioVO);
    }

    public void inicializarDadosInativacao(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoVO, UsuarioVO usuarioVO) throws Exception {
        condicaoPagamentoVO.setSituacao("IN");
        condicaoPagamentoVO.setDataInativacao(new Date());
        condicaoPagamentoVO.setResponsavelInativacao(usuarioVO);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarAtivacaoCondicaoPagamento(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoVO, Boolean ativado, UsuarioVO usuarioVO) throws Exception {
        inicializarDadosAtivacao(condicaoPagamentoVO, usuarioVO);
        alterarSituacaoAtivacao(condicaoPagamentoVO.getResponsavelAtivacao().getCodigo(), condicaoPagamentoVO.getCodigo(), condicaoPagamentoVO.getDataAtivacao());
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoAtivacao(Integer responsavelAtivacao, Integer condicaoPagamentoVO, Date dataAtivacao) throws Exception {
        PlanoFinanceiroCurso.alterar(getIdEntidade());
        String sqlStr = "UPDATE condicaoPagamentoPlanoFinanceiroCurso set situacao = 'AT', responsavelAtivacao = ?, dataAtivacao = ? where codigo = ?";
        getConexao().getJdbcTemplate().update(sqlStr, new Object[]{responsavelAtivacao, dataAtivacao, condicaoPagamentoVO});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarInativacaoCondicaoPagamento(CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoVO, Boolean ativado, UsuarioVO usuarioVO) throws Exception {
        inicializarDadosInativacao(condicaoPagamentoVO, usuarioVO);
        alterarSituacaoInativacao(condicaoPagamentoVO.getResponsavelInativacao().getCodigo(), condicaoPagamentoVO.getCodigo(), condicaoPagamentoVO.getDataInativacao());
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacaoInativacao(Integer responsavelInativacao, Integer condicaoPagamento, Date dataInativacao) throws Exception {
        PlanoFinanceiroCurso.alterar(getIdEntidade());
        String sqlStr = "UPDATE condicaoPagamentoPlanoFinanceiroCurso set situacao = 'IN', responsavelInativacao = ?, dataInativacao = ? where codigo = ?";
        getConexao().getJdbcTemplate().update(sqlStr, new Object[]{responsavelInativacao, dataInativacao, condicaoPagamento});
    }

    @Override
    public void adicionarCondicaoPlanoFinanceiroCursoTurma(CondicaoPagamentoPlanoFinanceiroCursoVO obj, Integer turma) throws Exception {
        obj.getCondicaoPlanoFinanceiroCursoTurmaVOs().clear();
        obj.setCondicaoPlanoFinanceiroCursoTurmaVOs(getFacadeFactory().getCondicaoPlanoFinanceiroCursoTurmaFacade().consultarDadosParaCriacaoCondicaoPlanoFinanceiroCursoTurma(turma));
    }

    @Override
    public Boolean consultarCondicaoPlanoAlunoControlaMatriculaPorMatriculaPeriodo(Integer matriculaPeriodo) throws Exception {
        StringBuilder sql = new StringBuilder(" SELECT naocontrolarmatricula from   planofinanceiroaluno ");
        sql.append(" inner join condicaopagamentoplanofinanceirocurso on planofinanceiroaluno.condicaopagamentoplanofinanceirocurso = condicaopagamentoplanofinanceirocurso.codigo ");
        sql.append(" where planofinanceiroaluno.matriculaperiodo = ").append(matriculaPeriodo);
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (rs.next()) {
            return !rs.getBoolean("naocontrolarmatricula");
        }
        return null;
    }

    @Override
    public Boolean consultarAplicarCalculoComBaseDescontosCalculadosPorContaReceber(Integer contaReceber, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sql = new StringBuilder(" SELECT aplicarCalculoComBaseDescontosCalculados FROM contaReceber ");
        sql.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = contaReceber.matriculaPeriodo ");
        sql.append(" inner join condicaopagamentoplanofinanceirocurso on condicaopagamentoplanofinanceirocurso.codigo = matriculaPeriodo.condicaopagamentoplanofinanceirocurso ");
        sql.append(" where contaReceber.codigo = ").append(contaReceber);
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (rs.next()) {
            return rs.getBoolean("aplicarCalculoComBaseDescontosCalculados");
        }
        return true;
    }

    public void validarDadosPlanoDesconto(PlanoDescontoVO planoDescontoVO) throws Exception {
        if (planoDescontoVO.getCodigo().equals(0)) {
            throw new Exception("O campo Plano Desconto deve ser informado.");
        }
    }

    public void adicionarItemPlanoDesconto(CondicaoPagamentoPlanoDescontoVO condicaoplanoDescontoVO, List<CondicaoPagamentoPlanoDescontoVO> condicaoPagamentoPlanoDescontoVOs, UsuarioVO usuarioVO) throws Exception {
        validarDadosPlanoDesconto(condicaoplanoDescontoVO.getPlanoDescontoVO());
        condicaoplanoDescontoVO.setPlanoDescontoVO(getFacadeFactory().getPlanoDescontoFacade().consultarPorChavePrimaria(condicaoplanoDescontoVO.getPlanoDescontoVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioVO));
        int index = 0;
        for (CondicaoPagamentoPlanoDescontoVO objExistente : condicaoPagamentoPlanoDescontoVOs) {
            if (objExistente.getPlanoDescontoVO().getCodigo().equals(condicaoplanoDescontoVO.getPlanoDescontoVO().getCodigo())) {
                condicaoPagamentoPlanoDescontoVOs.set(index, condicaoplanoDescontoVO);
                return;
            }
            index++;
        }
        condicaoPagamentoPlanoDescontoVOs.add(condicaoplanoDescontoVO);
    }
    
    
    @Override
    public List<CondicaoPagamentoPlanoFinanceiroCursoVO> consultarCondicaoPagamentoPlanoFinanceiroCursoFiltrarRenovacaoTurmaNivelCombobox(Integer unidadeEnsino, Integer curso, Integer turma, Integer gradeCurricular, Integer planoFinanceiroCurso, String ano, String semestre) throws Exception{
    	StringBuilder sql  =  new StringBuilder("select distinct CondicaoPagamentoPlanoFinanceiroCurso.codigo, CondicaoPagamentoPlanoFinanceiroCurso.descricao from CondicaoPagamentoPlanoFinanceiroCurso ");
    	sql.append(" inner join planofinanceiroaluno on planofinanceiroaluno.CondicaoPagamentoPlanoFinanceiroCurso = CondicaoPagamentoPlanoFinanceiroCurso.codigo ");
    	sql.append(" inner join matriculaperiodo on planofinanceiroaluno.matriculaperiodo = matriculaperiodo.codigo ");
    	sql.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
    	sql.append(" where 1=1 ");
    	if(Uteis.isAtributoPreenchido(unidadeEnsino)){
    		sql.append(" and matricula.unidadeensino = ").append(unidadeEnsino);
    	}
    	if(Uteis.isAtributoPreenchido(curso)){
    		sql.append(" and matricula.curso = ").append(curso);
    	}
    	if(Uteis.isAtributoPreenchido(turma)){
    		sql.append(" and matriculaperiodo.turma = ").append(turma);
    	}
    	if(ano != null && !ano.trim().isEmpty()){
    		sql.append(" and matriculaperiodo.ano = '").append(ano).append("' ");
    		if(semestre != null && !semestre.trim().isEmpty()){
    			sql.append(" and matriculaperiodo.semestre = '").append(semestre).append("' ");
    		}
    	}
    	if(gradeCurricular != null && gradeCurricular > 0){
    		sql.append(" and matricula.gradeCurricularAtual = ").append(gradeCurricular);
    	}
    	if(planoFinanceiroCurso != null && planoFinanceiroCurso > 0){
    		sql.append(" and CondicaoPagamentoPlanoFinanceiroCurso.planoFinanceiroCurso = ").append(planoFinanceiroCurso);
    	}
    	sql.append(" order by CondicaoPagamentoPlanoFinanceiroCurso.descricao ");
    	SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	List<CondicaoPagamentoPlanoFinanceiroCursoVO> condicaoPagamentoPlanoFinanceiroCursoVOs = new ArrayList<CondicaoPagamentoPlanoFinanceiroCursoVO>(0);
    	CondicaoPagamentoPlanoFinanceiroCursoVO obj = null;
    	while(rs.next()){
    		obj = new CondicaoPagamentoPlanoFinanceiroCursoVO();
    		obj.setCodigo(rs.getInt("codigo"));
    		obj.setDescricao(rs.getString("descricao"));
    		condicaoPagamentoPlanoFinanceiroCursoVOs.add(obj);
    	}
    	return condicaoPagamentoPlanoFinanceiroCursoVOs;
    	
    }

    
    /**
     * Consulta agrupada das categorias da condicao de pagamento
     * 
     */
    public List<String> consultarCategoriaDasCondicoesDePagamentoDoPlanoFinanceiroCurso (Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	List<String> categorias = new ArrayList<>();
    	
    	//Inicializar a opcao com vazio faz com que a implementacao da nova funcionalidade seja tranparente para o usuario
    	categorias.add("");
    	
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT categoria FROM CondicaoPagamentoPlanoFinanceiroCurso  ")
        .append(" WHERE planoFinanceiroCurso = ").append(valorConsulta.intValue())
        .append(" and trim(categoria) <> '' and situacao = 'AT' ")
        .append(" group by categoria ");
        sqlStr.append(" ORDER BY categoria ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        while (tabelaResultado.next()) {
            categorias.add(tabelaResultado.getString("categoria"));
        }
        return categorias;
    }
    
    public static void montarDadosUnidadeEnsinoFinanceira(CondicaoPagamentoPlanoFinanceiroCursoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsinoFinanceira().getCodigo() != 0) {
        	obj.setUnidadeEnsinoFinanceira(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsinoFinanceira().getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
        }
    }
    
    @Override
    public CondicaoPagamentoPlanoFinanceiroCursoVO consultarPorMatriculaPeriodo(Integer matriculaPeriodo, UsuarioVO usuarioVO) {
    	StringBuilder sb = new StringBuilder("select condicaoPagamentoPlanoFinanceiroCurso.codigo, condicaoPagamentoPlanoFinanceiroCurso.descricao, condicaoPagamentoPlanoFinanceiroCurso.nrparcelasperiodo, condicaoPagamentoPlanoFinanceiroCurso.considerarParcelasMaterialDidaticoReajustePreco, ");
    	sb.append(" condicaoPagamentoPlanoFinanceiroCurso.quantidadeparcelasmaterialdidatico from condicaoPagamentoPlanoFinanceiroCurso ");
    	sb.append(" inner join matriculaperiodo on matriculaperiodo.condicaopagamentoplanofinanceirocurso = condicaopagamentoplanofinanceirocurso.codigo ");
    	sb.append(" where matriculaPeriodo.codigo = ? ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), matriculaPeriodo);
    	CondicaoPagamentoPlanoFinanceiroCursoVO obj = null;
    	if (tabelaResultado.next()) {
    		obj = new CondicaoPagamentoPlanoFinanceiroCursoVO();
    		obj.setCodigo(tabelaResultado.getInt("codigo"));
    		obj.setDescricao(tabelaResultado.getString("descricao"));
    		obj.setNrParcelasPeriodo(tabelaResultado.getInt("nrparcelasperiodo"));
    		obj.setConsiderarParcelasMaterialDidaticoReajustePreco(tabelaResultado.getBoolean("considerarParcelasMaterialDidaticoReajustePreco"));
    		obj.setQuantidadeParcelasMaterialDidatico(tabelaResultado.getLong("quantidadeparcelasmaterialdidatico"));
    	}
    	return obj;
    }
    
    
    @Override
    public List<CondicaoPagamentoRSVO> consultarCondicaoPagamentoPlanoFinanceiroCursoParaMatriculaOnlineProcessoSeletivo(ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, RenovarMatriculaControle renovarMatriculaControle) {
		CondicaoPagamentoRSVO condicaoPagamentoRSVO = null;
		List<CondicaoPagamentoRSVO> condicaoPagamentoRSVOs = new ArrayList<CondicaoPagamentoRSVO>();	
		try {
				
			if(Uteis.isAtributoPreenchido(renovarMatriculaControle.getMatriculaPeriodoVO().getTurma().getCodigo())){					
				getFacadeFactory().getMatriculaFacade().inicializarPlanoFinanceiroMatriculaPeriodo(renovarMatriculaControle.getMatriculaVO(), renovarMatriculaControle.getMatriculaPeriodoVO(), null);
			}else {
				getFacadeFactory().getMatriculaFacade().inicializarPlanoFinanceiroMatriculaPeriodoSemTurmaInicializada(renovarMatriculaControle.getMatriculaVO(), renovarMatriculaControle.getMatriculaPeriodoVO(), null);
			}
						
			List<CondicaoPagamentoPlanoFinanceiroCursoVO> condicaoPagamentoPlanoFinanceiroCursoVOs = getFacadeFactory().getPlanoFinanceiroCursoFacade().inicializarListaSelectItemPlanoFinanceiroCursoParaTurma(renovarMatriculaControle.getRealizandoNovaMatriculaAluno(), renovarMatriculaControle.getBanner(), renovarMatriculaControle.getMatriculaPeriodoVO(), renovarMatriculaControle.getMatriculaVO(), null);
			for (CondicaoPagamentoPlanoFinanceiroCursoVO condicaoPagamentoPlanoFinanceiroCursoVO : condicaoPagamentoPlanoFinanceiroCursoVOs) {
				condicaoPagamentoRSVO = new CondicaoPagamentoRSVO();
				condicaoPagamentoRSVO.setCodigo(condicaoPagamentoPlanoFinanceiroCursoVO.getCodigo());
				condicaoPagamentoRSVO.setNome(condicaoPagamentoPlanoFinanceiroCursoVO.getDescricao());
				condicaoPagamentoRSVO.setValorMatricula(condicaoPagamentoPlanoFinanceiroCursoVO.getValorMatricula());
				condicaoPagamentoRSVO.setValorMensalidade(condicaoPagamentoPlanoFinanceiroCursoVO.getValorParcela());
				condicaoPagamentoRSVO.setParcelas(condicaoPagamentoPlanoFinanceiroCursoVO.getNrParcelasPeriodo());
				condicaoPagamentoRSVO.setCategoria(condicaoPagamentoPlanoFinanceiroCursoVO.getCategoria());
				condicaoPagamentoRSVOs.add(condicaoPagamentoRSVO);
			}
	} catch (Exception e) {
		e.printStackTrace();
	}
		return condicaoPagamentoRSVOs;
	}
}