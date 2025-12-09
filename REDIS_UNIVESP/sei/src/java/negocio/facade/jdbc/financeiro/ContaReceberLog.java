package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ContaReceberLogVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContaReceberLogInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>ContaReceberVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>ContaReceberVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ContaReceberVO
 * @see ControleAcesso
 */
@Lazy
@Repository
@Scope("singleton")
public class ContaReceberLog extends ControleAcesso implements ContaReceberLogInterfaceFacade {

    protected static String idEntidade;
    public static final long serialVersionUID = 1L;

    public ContaReceberLog() throws Exception {
        super();
        setIdEntidade("ContaReceberLog");
    }

    public static String getIdEntidade() {
        return ContaReceberLog.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        ContaReceberLog.idEntidade = idEntidade;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void preencherContaReceberLog(ContaReceberVO obj, String operacao, UsuarioVO usuario) throws Exception {
//        try {
//        	if(obj.getNovoObj()){
//        		return;
//        	}
//            ContaReceberLogVO contaReceberLog = new ContaReceberLogVO();
//            contaReceberLog.setContaReceber(obj.getCodigo());
//            contaReceberLog.setOperacao(operacao);
//            contaReceberLog.setDataCriacao(new Date());
//            if (usuario != null) {
//                contaReceberLog.getResponsavel().setCodigo(usuario.getCodigo());
//            }
//            contaReceberLog.setMulta(obj.getMulta());
//            contaReceberLog.setMultaPorcentagem(obj.getMultaPorcentagem());
//            contaReceberLog.setJuro(obj.getJuro());
//            contaReceberLog.setJuroPorcentagem(obj.getJuroPorcentagem());
//            contaReceberLog.setTipoOrigem(obj.getTipoOrigem());
//            contaReceberLog.getDescontoProgressivo().setCodigo(obj.getDescontoProgressivo().getCodigo());
//            contaReceberLog.getContaCorrente().setCodigo(obj.getContaCorrente());
//            contaReceberLog.getFuncionario().setCodigo(obj.getFuncionario().getCodigo());
//            contaReceberLog.getPessoa().setCodigo(obj.getPessoa().getCodigo());
//            contaReceberLog.getMatriculaAluno().setMatricula(obj.getMatriculaAluno().getMatricula());
//            contaReceberLog.setParcela(obj.getParcela());
//            contaReceberLog.setCodigoBarra(obj.getCodigoBarra());
//            contaReceberLog.setNrDocumento(obj.getNrDocumento());
//            contaReceberLog.setValorDesconto(obj.getValorDesconto());
//            contaReceberLog.setValor(obj.getValor());
//            contaReceberLog.setDataVencimento(obj.getDataVencimento());
//            contaReceberLog.setDescricaoPagamento(obj.getDescricaoPagamento());
//            contaReceberLog.setSituacao(obj.getSituacao());
//            contaReceberLog.setCodOrigem(obj.getCodOrigem());
//            contaReceberLog.setData(obj.getData());
//            contaReceberLog.getCandidato().setCodigo(obj.getCandidato().getCodigo());
//            contaReceberLog.setTipoPessoa(obj.getTipoPessoa());
//            contaReceberLog.setValorRecebido(obj.getValorRecebido());
//            contaReceberLog.setTipoDesconto(obj.getTipoDesconto());
//            contaReceberLog.getUnidadeEnsino().setCodigo(obj.getUnidadeEnsino().getCodigo());
//            contaReceberLog.setValorDescontoRecebido(obj.getValorDescontoRecebido());
//            contaReceberLog.setValorDescontoInstituicao(obj.getValorDescontoInstituicao());
//            contaReceberLog.setValorDescontoConvenio(obj.getValorDescontoConvenio());
//            contaReceberLog.getConvenio().setCodigo(obj.getConvenio().getCodigo());
//            contaReceberLog.setLinhaDigitavelCodigoBarras(obj.getLinhaDigitavelCodigoBarras());
//            contaReceberLog.setNossoNumero(obj.getNossoNumero());
//            contaReceberLog.getParceiro().setCodigo(obj.getParceiroVO().getCodigo());
//            contaReceberLog.getMatriculaPeriodo().setCodigo(obj.getMatriculaPeriodo());
//            contaReceberLog.setValorDescontoProgressivo(obj.getValorDescontoProgressivo());
//            contaReceberLog.setRecebimentoBancario(obj.getRecebimentoBancario());
//            contaReceberLog.setOrdemDescontoAluno(obj.getOrdemDescontoAluno());
//            contaReceberLog.setOrdemDescontoAlunoValorCheio(obj.getOrdemDescontoAlunoValorCheio());
//            contaReceberLog.setOrdemPlanoDesconto(obj.getOrdemPlanoDesconto());
//            contaReceberLog.setOrdemPlanoDescontoValorCheio(obj.getOrdemPlanoDescontoValorCheio());
//            contaReceberLog.setOrdemConvenio(obj.getOrdemConvenio());
//            contaReceberLog.setOrdemConvenioValorCheio(obj.getOrdemConvenioValorCheio());
//            contaReceberLog.setOrdemDescontoProgressivo(obj.getOrdemDescontoProgressivo());
//            contaReceberLog.setOrdemDescontoProgressivoValorCheio(obj.getOrdemDescontoProgressivoValorCheio());
//            contaReceberLog.setJustificativaDesconto(obj.getJustificativaDesconto());
//            if (obj.getDescontoProgressivoUtilizado() != null) {
//                contaReceberLog.setDescontoProgressivoUtilizado(obj.getDescontoProgressivoUtilizado().getDescricao());
//            } else {
//                contaReceberLog.setDescontoProgressivoUtilizado(null);
//            }
//            contaReceberLog.setValorDescontoCalculadoPrimeiraFaixaDescontos(obj.getValorDescontoCalculadoPrimeiraFaixaDescontos());
//            contaReceberLog.setValorDescontoCalculadoSegundaFaixaDescontos(obj.getValorDescontoCalculadoSegundaFaixaDescontos());
//            contaReceberLog.setValorDescontoCalculadoTerceiraFaixaDescontos(obj.getValorDescontoCalculadoTerceiraFaixaDescontos());
//            contaReceberLog.setValorDescontoCalculadoQuartaFaixaDescontos(obj.getValorDescontoCalculadoQuartaFaixaDescontos());
//            contaReceberLog.setValorDescontoLancadoRecebimento(obj.getValorDescontoLancadoRecebimento());
//            contaReceberLog.setTipoDescontoLancadoRecebimento(obj.getTipoDescontoLancadoRecebimento());
//            contaReceberLog.setValorCalculadoDescontoLancadoRecebimento(obj.getValorCalculadoDescontoLancadoRecebimento());
//            contaReceberLog.setValorDescontoAlunoJaCalculado(obj.getValorDescontoAlunoJaCalculado());
//            contaReceberLog.setAcrescimo(obj.getAcrescimo());
//            contaReceberLog.setUsaDescontoCompostoPlanoDesconto(obj.getUsaDescontoCompostoPlanoDesconto());
//            contaReceberLog.getResponsavelFinanceiro().setCodigo(obj.getResponsavelFinanceiro().getCodigo());
//            contaReceberLog.getTurma().setCodigo(obj.getTurma().getCodigo());
//            contaReceberLog.getFornecedor().setCodigo(obj.getFornecedor().getCodigo());
//            incluir(contaReceberLog);
//        } catch (Exception e) {
//            throw e;
//        }
    }

  

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ContaReceberLogVO obj) throws Exception {
        try {
            final String sql = "INSERT INTO ContaReceberLog( contareceber, descontoprogressivo, contacorrente, funcionario, pessoa, matriculaaluno, parcela, " //1 - 7
                    + "codigobarra, nrdocumento, valordesconto, valor, datavencimento, descricaopagamento, situacao, codorigem, data, candidato, " //8 - 17
                    + "tipopessoa, valorrecebido, tipodesconto, unidadeensino, valordescontorecebido, descontoinstituicao, descontoconvenio, " //18 - 24
                    + "convenio, linhadigitavelcodigobarras, nossonumero, parceiro, matriculaperiodo, valordescontoprogressivo, " //25 - 30
                    + "recebimentobancario, ordemdescontoaluno, ordemdescontoalunovalorcheio, ordemplanodesconto, ordemplanodescontovalorcheio, " //31 - 35
                    + "ordemconvenio, ordemconveniovalorcheio, ordemdescontoprogressivo, ordemdescontoprogressivovalorcheio, justificativadesconto, " //36 - 40
                    + "descontoprogressivoutilizado, valordescontocalculadoprimeirafaixadescontos, valordescontocalculadosegundafaixadescontos, " //41 - 43
                    + "valordescontocalculadoterceirafaixadescontos, valordescontocalculadoquartafaixadescontos, valordescontolancadorecebimento, " //44 - 46
                    + "tipodescontolancadorecebimento, valorcalculadodescontolancadorecebimento, valordescontoalunojacalculado, datacriacao, " //47 - 50
                    + "acrescimo, usadescontocompostoplanodesconto, responsavel, operacao, responsavelFinanceiro, turma, fornecedor, tipoOrigem, juro, juroPorcentagem, multa, multaPorcentagem ) " //51 - 54
                    + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," //1 - 16
                    + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?," //17 - 38
                    + " ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo";  //39 - 55
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                    int i = 0;
                    sqlInserir.setInt(++i, obj.getContaReceber());
                    if (!obj.getDescontoProgressivo().getCodigo().equals(0)) {
                        sqlInserir.setInt(++i, obj.getDescontoProgressivo().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (!obj.getContaCorrente().getCodigo().equals(0)) {
                        sqlInserir.setInt(++i, obj.getContaCorrente().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (!obj.getFuncionario().getCodigo().equals(0)) {
                        sqlInserir.setInt(++i, obj.getFuncionario().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (!obj.getPessoa().getCodigo().equals(0)) {
                        sqlInserir.setInt(++i, obj.getPessoa().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (!obj.getMatriculaAluno().getMatricula().equals("")) {
                        sqlInserir.setString(++i, obj.getMatriculaAluno().getMatricula());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setString(++i, obj.getParcela());
                    sqlInserir.setString(++i, obj.getCodigoBarra());
                    sqlInserir.setString(++i, obj.getNrDocumento());
                    sqlInserir.setDouble(++i, obj.getValorDesconto());
                    sqlInserir.setDouble(++i, obj.getValor());
                    sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getDataVencimento()));
                    sqlInserir.setString(++i, obj.getDescricaoPagamento());
                    sqlInserir.setString(++i, obj.getSituacao());
                    sqlInserir.setString(++i, obj.getCodOrigem());
                    sqlInserir.setDate(++i, Uteis.getDataJDBC(obj.getData()));
                    if (!obj.getCandidato().getCodigo().equals(0)) {
                        sqlInserir.setInt(++i, obj.getCandidato().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setString(++i, obj.getTipoPessoa());
                    sqlInserir.setDouble(++i, obj.getValorRecebido());
                    sqlInserir.setString(++i, obj.getTipoDesconto());
                    if (!obj.getUnidadeEnsino().getCodigo().equals(0)) {
                        sqlInserir.setInt(++i, obj.getUnidadeEnsino().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setDouble(++i, obj.getValorDescontoRecebido());
                    if (!obj.getValorDescontoInstituicao().equals(0.0)) {
                        sqlInserir.setDouble(++i, obj.getValorDescontoInstituicao());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (!obj.getValorDescontoConvenio().equals(0.0)) {
                        sqlInserir.setDouble(++i, obj.getValorDescontoConvenio());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (!obj.getConvenio().getCodigo().equals(0)) {
                        sqlInserir.setInt(++i, obj.getConvenio().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setString(++i, obj.getLinhaDigitavelCodigoBarras());
                    sqlInserir.setString(++i, obj.getNossoNumero());
                    if (!obj.getParceiro().getCodigo().equals(0)) {
                        sqlInserir.setInt(++i, obj.getParceiro().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (!obj.getMatriculaPeriodo().getCodigo().equals(0)) {
                        sqlInserir.setInt(++i, obj.getMatriculaPeriodo().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (!obj.getValorDescontoProgressivo().equals(0.0)) {
                        sqlInserir.setDouble(++i, obj.getValorDescontoProgressivo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (obj.getRecebimentoBancario() == null) {
                        sqlInserir.setNull(++i, 0);
                    } else {
                        sqlInserir.setBoolean(++i, obj.getRecebimentoBancario());
                    }
                    sqlInserir.setInt(++i, obj.getOrdemDescontoAluno());
                    sqlInserir.setBoolean(++i, obj.getOrdemDescontoAlunoValorCheio());
                    sqlInserir.setInt(++i, obj.getOrdemPlanoDesconto());
                    sqlInserir.setBoolean(++i, obj.getOrdemPlanoDescontoValorCheio());
                    sqlInserir.setInt(++i, obj.getOrdemConvenio());
                    sqlInserir.setBoolean(++i, obj.getOrdemConvenioValorCheio());
                    sqlInserir.setInt(++i, obj.getOrdemDescontoProgressivo());
                    sqlInserir.setBoolean(++i, obj.getOrdemDescontoProgressivoValorCheio());
                    sqlInserir.setString(++i, obj.getJustificativaDesconto());
                    if (obj.getDescontoProgressivoUtilizado() == null) {
                        sqlInserir.setNull(++i, 0);
                    } else {
                        sqlInserir.setString(++i, obj.getDescontoProgressivoUtilizado());
                    }
                    sqlInserir.setDouble(++i, obj.getValorDescontoCalculadoPrimeiraFaixaDescontos());
                    sqlInserir.setDouble(++i, obj.getValorDescontoCalculadoSegundaFaixaDescontos());
                    sqlInserir.setDouble(++i, obj.getValorDescontoCalculadoTerceiraFaixaDescontos());
                    sqlInserir.setDouble(++i, obj.getValorDescontoCalculadoQuartaFaixaDescontos());
                    if (!obj.getValorDescontoLancadoRecebimento().equals(0.0)) {
                        sqlInserir.setDouble(++i, obj.getValorDescontoLancadoRecebimento());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setString(++i, obj.getTipoDescontoLancadoRecebimento());
                    if (!obj.getValorCalculadoDescontoLancadoRecebimento().equals(0.0)) {
                        sqlInserir.setDouble(++i, obj.getValorCalculadoDescontoLancadoRecebimento());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (!obj.getValorDescontoAlunoJaCalculado().equals(0.0)) {
                        sqlInserir.setDouble(++i, obj.getValorDescontoAlunoJaCalculado());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setTimestamp(++i, Uteis.getDataJDBCTimestamp(obj.getDataCriacao()));
                    sqlInserir.setDouble(++i, obj.getAcrescimo());
                    sqlInserir.setBoolean(++i, obj.getUsaDescontoCompostoPlanoDesconto());
                    if (!obj.getResponsavel().getCodigo().equals(0)) {
                        sqlInserir.setInt(++i, obj.getResponsavel().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    
                    sqlInserir.setString(++i, obj.getOperacao());
                    if (!obj.getResponsavelFinanceiro().getCodigo().equals(0)) {
                        sqlInserir.setInt(++i, obj.getResponsavelFinanceiro().getCodigo());
                    } else {
                        sqlInserir.setNull(++i, 0);
                    }
                    if (!obj.getTurma().getCodigo().equals(0)) {
                    	sqlInserir.setInt(++i, obj.getTurma().getCodigo());
                    } else {
                    	sqlInserir.setNull(++i, 0);
                    }
                    if (!obj.getFornecedor().getCodigo().equals(0)) {
                    	sqlInserir.setInt(++i, obj.getFornecedor().getCodigo());
                    } else {
                    	sqlInserir.setNull(++i, 0);
                    }
                    sqlInserir.setString(++i, obj.getTipoOrigem());
                    sqlInserir.setDouble(++i, obj.getJuro());
                    sqlInserir.setDouble(++i, obj.getJuroPorcentagem());
                    sqlInserir.setDouble(++i, obj.getMulta());
                    sqlInserir.setDouble(++i, obj.getMultaPorcentagem());
                    return sqlInserir;
                }
            },
                    new ResultSetExtractor<Integer>() {

                        public Integer extractData(ResultSet rs) throws SQLException, DataAccessException {
                            if (rs.next()) {
                                obj.setNovoObj(Boolean.FALSE);
                                return rs.getInt("codigo");
                            }
                            return null;
                        }
                    }));
        } catch (Exception e) {
            throw e;
        }
    }

  
}
