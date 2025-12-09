package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.RegistroArquivoVO;
import negocio.comuns.financeiro.RegistroDetalheVO;
import negocio.comuns.financeiro.RegistroHeaderVO;
import negocio.comuns.financeiro.RegistroTrailerVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.RegistroArquivoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>RegistroArquivoVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>RegistroArquivoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see RegistroArquivoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class RegistroArquivo extends ControleAcesso implements RegistroArquivoInterfaceFacade {

    protected static String idEntidade;

    public RegistroArquivo() throws Exception {
        super();
        setIdEntidade("RegistroArquivo");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe
     * <code>RegistroArquivoVO</code>.
     */
    public RegistroArquivoVO novo() throws Exception {

        RegistroArquivoVO obj = new RegistroArquivoVO();
        return obj;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final RegistroArquivoVO obj, boolean controleAcesso, UsuarioVO usuario) throws Exception {
        try {

        	if (!obj.getRegistroDetalheVOs().isEmpty()) {
        		RegistroDetalheVO reg = (RegistroDetalheVO) obj.getRegistroDetalheVOs().get(0);
        		if (obj.getRegistroHeader().getNumeroConta() == 0) {
        			if (!reg.getCedenteNumeroConta().equals("")) {
        				obj.getRegistroHeader().setNumeroConta(Integer.parseInt(reg.getCedenteNumeroConta()));
        			}
        		}
        		if (obj.getRegistroHeader().getNumeroAgencia() == 0) {
        			if (!reg.getCedenteNumeroAgencia().equals("")) {
        				obj.getRegistroHeader().setNumeroAgencia(Integer.parseInt(reg.getCedenteNumeroAgencia()));
        			}
        		}
        	}
            getFacadeFactory().getRegistroHeaderFacade().incluir(obj.getRegistroHeader(), usuario);
            getFacadeFactory().getRegistroTrailerFacade().incluir(obj.getRegistroTrailer(), usuario);

            RegistroArquivoVO.validarDados(obj);
            final String sql = "INSERT INTO RegistroArquivo( registroHeader, registroTrailer, arquivoProcessado, contasBaixadas, totalTaxaBoletoCobradoBanco, totalTaxaPagaAluno ) VALUES ( ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getRegistroHeader().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getRegistroHeader().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if (obj.getRegistroTrailer().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getRegistroTrailer().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    sqlInserir.setBoolean(3, obj.getArquivoProcessado());
                    sqlInserir.setBoolean(4, obj.getContasBaixadas());
                    sqlInserir.setDouble(5, obj.getTotalTaxaBoletoCobradoBanco());
                    sqlInserir.setDouble(6, obj.getTotalTaxaPagaAluno());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Object>() {

                public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            getFacadeFactory().getRegistroDetalheFacade().incluirRegistroDetalhes(obj.getCodigo(), obj.getRegistroDetalheVOs(), usuario);
            getFacadeFactory().getContaReceberRegistroArquivoFacade().incluirContaReceberRegistroArquivo(obj.getCodigo(), obj.getContaReceberRegistroArquivoVOs(), usuario);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSemBaixarContas(final RegistroArquivoVO obj, boolean controleAcesso, UsuarioVO usuario) throws Exception {
        try {
        	alterar(obj, controleAcesso, usuario);
            getFacadeFactory().getRegistroDetalheFacade().incluirRegistroDetalhes(obj.getCodigo(), obj.getRegistroDetalheVOs(), usuario);
            getFacadeFactory().getContaReceberRegistroArquivoFacade().incluirContaReceberRegistroArquivo(obj.getCodigo(), obj.getContaReceberRegistroArquivoVOs(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>RegistroArquivoVO</code>. Sempre utiliza a chave primária da classe
     * como atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>RegistroArquivoVO</code> que será
     *            alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final RegistroArquivoVO obj, boolean controleAcesso, UsuarioVO usuario) throws Exception {
        try {
            RegistroArquivo.alterar(getIdEntidade(), controleAcesso, usuario);
            RegistroArquivoVO.validarDados(obj);
            final String sql = "UPDATE RegistroArquivo set registroHeader=?, registroTrailer=?, arquivoProcessado=?, contasBaixadas=?, totalTaxaBoletoCobradoBanco = ?, totalTaxaPagaAluno=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getRegistroHeader().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getRegistroHeader().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    if (obj.getRegistroTrailer().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getRegistroTrailer().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    sqlAlterar.setBoolean(3, obj.getArquivoProcessado());
                    sqlAlterar.setBoolean(4, obj.getContasBaixadas());
                    sqlAlterar.setDouble(5, obj.getTotalTaxaBoletoCobradoBanco());
                    sqlAlterar.setDouble(6, obj.getTotalTaxaPagaAluno());
                    sqlAlterar.setInt(7, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getRegistroDetalheFacade().incluirRegistroDetalhes(obj.getCodigo(), obj.getRegistroDetalheVOs(), usuario);
            getFacadeFactory().getContaReceberRegistroArquivoFacade().incluirContaReceberRegistroArquivo(obj.getCodigo(), obj.getContaReceberRegistroArquivoVOs(), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>RegistroArquivoVO</code>. Sempre localiza o registro a ser excluído
     * através da chave primária da entidade. Primeiramente verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>RegistroArquivoVO</code> que será
     *            removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public void excluir(RegistroArquivoVO obj, boolean controleAcesso, UsuarioVO usuario) throws Exception {
        try {
            RegistroArquivo.excluir(getIdEntidade(), controleAcesso, usuario);
            getFacadeFactory().getRegistroHeaderFacade().excluir(obj.getRegistroHeader(), usuario);
            getFacadeFactory().getContaReceberRegistroArquivoFacade().excluirPorCodigoRegistroArquivo(obj.getCodigo(), usuario);
            String sql = "DELETE FROM RegistroArquivo WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
           
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroArquivo</code>
     * através do valor do atributo <code>Integer registroHeader</code>. Retorna
     * os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz
     * uso da operação <code>montarDadosConsulta</code> que realiza o trabalho
     * de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     *         <code>RegistroArquivoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorRegistroHeader(Integer valorConsulta, String identificacaoTituloEmpresa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT distinct registroarquivo.*, min (registrodetalhe.datacredito) as datacredito, ");
        sqlStr.append(" count(registrodetalhe.codigo) as quantidadeConta, ");
        sqlStr.append(" sum(registrodetalhe.valorPago) as valorTotalConta ");
        sqlStr.append(" FROM registroarquivo ");
        sqlStr.append(" inner join registrodetalhe on registrodetalhe.registroarquivo = registroarquivo.codigo ");
        sqlStr.append(" where registroheader = ").append(valorConsulta);
        sqlStr.append(" group by registroarquivo.registrotrailer, registroarquivo.registroheader, registroarquivo.codigo, registroarquivo.arquivoprocessado, ");
        sqlStr.append(" registroarquivo.contasbaixadas, registroarquivo.totaltaxaboletocobradobanco, totaltaxapagaaluno ");
//        String sqlStr = "SELECT * FROM RegistroArquivo WHERE registroHeader >= " + valorConsulta.intValue() + " ORDER BY registroHeader";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, identificacaoTituloEmpresa, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>RegistroArquivo</code>
     * através do valor do atributo <code>Integer codigo</code>. Retorna os
     * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     *         <code>RegistroArquivoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, String identificacaoTituloEmpresa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT distinct registroarquivo.*, min (registrodetalhe.datacredito) as datacredito, ");
        sqlStr.append(" count(registrodetalhe.codigo) as quantidadeConta, ");
        sqlStr.append(" sum(registrodetalhe.valorPago) as valorTotalConta ");
        sqlStr.append(" FROM registroarquivo ");
        sqlStr.append(" inner join registrodetalhe on registrodetalhe.registroarquivo = registroarquivo.codigo ");
        sqlStr.append(" where registroarquivo.codigo = ").append(valorConsulta);
        sqlStr.append(" group by registroarquivo.registrotrailer, registroarquivo.registroheader, registroarquivo.codigo, registroarquivo.arquivoprocessado, ");
        sqlStr.append(" registroarquivo.contasbaixadas, registroarquivo.totaltaxaboletocobradobanco, totaltaxapagaaluno ");
//        String sqlStr = "SELECT * FROM RegistroArquivo WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, identificacaoTituloEmpresa, usuario));
    }

    public List<RegistroArquivoVO> consultarPorIdentificacaoTituloEmpresa(String identificacaoTituloEmpresa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT distinct registroarquivo.*, min(registrodetalhe.datacredito) as datacredito, ");
        sqlStr.append(" count(registrodetalhe.codigo) as quantidadeConta, ");
        sqlStr.append(" sum(registrodetalhe.valorPago) as valorTotalConta ");
        sqlStr.append(" FROM RegistroArquivo ");
        sqlStr.append(" inner join registrodetalhe on registrodetalhe.registroarquivo = registroarquivo.codigo ");
        sqlStr.append(" where registrodetalhe.identificacaotituloempresa like '%");
        sqlStr.append(identificacaoTituloEmpresa).append("%' ");
        sqlStr.append(" group by registroarquivo.registrotrailer, registroarquivo.registroheader, registroarquivo.codigo, registroarquivo.arquivoprocessado, ");
        sqlStr.append(" registroarquivo.contasbaixadas, registroarquivo.totaltaxaboletocobradobanco, totaltaxapagaaluno ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, identificacaoTituloEmpresa, usuario));
    }

    public List<RegistroArquivoVO> consultarPorDataGeracaoArquivo(Date dataInicio, Date dataFim, String identificacaoTituloEmpresa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT distinct registroarquivo.*, min (registrodetalhe.datacredito) as datacredito,  ");
        sqlStr.append(" count(registrodetalhe.codigo) as quantidadeConta, ");
        sqlStr.append(" sum(registrodetalhe.valorPago) as valorTotalConta ");
        sqlStr.append(" FROM RegistroArquivo ");
        sqlStr.append(" inner join registrodetalhe on registrodetalhe.registroarquivo = registroarquivo.codigo ");
        sqlStr.append(" where datacredito between '");
        sqlStr.append(Uteis.getDataJDBC(dataInicio)).append("' and '").append(Uteis.getDataJDBC(dataFim)).append("' ");
        sqlStr.append(" group by registroarquivo.registrotrailer, registroarquivo.registroheader, registroarquivo.codigo, registroarquivo.arquivoprocessado, ");
        sqlStr.append(" registroarquivo.contasbaixadas, registroarquivo.totaltaxaboletocobradobanco, totaltaxapagaaluno ");
        
//        String sql = "SELECT distinct registroarquivo.* "
//                + "FROM RegistroArquivo inner join registroheader on registroheader.codigo = registroarquivo.registroheader "
//                + "where registroheader.datageracaoarquivo between '" + Uteis.getDataJDBC(dataInicio) + "' and '" + Uteis.getDataJDBC(dataFim) + "'";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, identificacaoTituloEmpresa, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe
     *         <code>RegistroArquivoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, String identificacaoTituloEmpresa, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, identificacaoTituloEmpresa, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (<code>ResultSet</code>) em um objeto da classe
     * <code>RegistroArquivoVO</code>.
     *
     * @return O objeto da classe <code>RegistroArquivoVO</code> com os dados
     *         devidamente montados.
     */
    public static RegistroArquivoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, String identificacaoEmpresa, UsuarioVO usuario) throws Exception {
        RegistroArquivoVO obj = new RegistroArquivoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getRegistroHeader().setCodigo(dadosSQL.getInt("registroHeader"));
        obj.getRegistroTrailer().setCodigo(dadosSQL.getInt("registroTrailer"));
        obj.setArquivoProcessado(dadosSQL.getBoolean("arquivoProcessado"));
        obj.setContasBaixadas(dadosSQL.getBoolean("contasBaixadas"));
        obj.setTotalTaxaBoletoCobradoBanco(dadosSQL.getDouble("totalTaxaBoletoCobradoBanco"));
        obj.setTotalTaxaPagaAluno(dadosSQL.getDouble("totalTaxaPagaAluno"));
        obj.setDataCredito(dadosSQL.getDate("datacredito"));
        obj.setQuantidadeConta(dadosSQL.getInt("quantidadeConta"));
        obj.setValorTotalConta(dadosSQL.getDouble("valorTotalConta"));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosRegistroHeader(obj, nivelMontarDados, usuario);
        montarDadosRegistroTrailer(obj, nivelMontarDados, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setRegistroDetalheVOs(getFacadeFactory().getRegistroDetalheFacade().consultarPorRegistroArquivoIdentificacaoRegistroEmpresa(identificacaoEmpresa, obj.getCodigo(), false, nivelMontarDados, usuario));
        obj.setContaReceberRegistroArquivoVOs(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultaRapidaPorRegistroArquivo(obj.getCodigo(), 10, 0, "", "", "", false, usuario, false));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }
        return obj;
    }

    public static RegistroArquivoVO montarDadosCompleto(SqlRowSet dadosSQL, int nivelMontarDados, String identificacaoEmpresa, UsuarioVO usuario) throws Exception {
        RegistroArquivoVO obj = new RegistroArquivoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getRegistroHeader().setCodigo(dadosSQL.getInt("registroHeader"));
        obj.getRegistroTrailer().setCodigo(dadosSQL.getInt("registroTrailer"));
        obj.setArquivoProcessado(dadosSQL.getBoolean("arquivoProcessado"));
        obj.setTotalTaxaBoletoCobradoBanco(dadosSQL.getDouble("totalTaxaBoletoCobradoBanco"));
        obj.setTotalTaxaPagaAluno(dadosSQL.getDouble("totalTaxaPagaAluno"));
        obj.setContasBaixadas(dadosSQL.getBoolean("contasBaixadas"));
        obj.setQuantidadeConta(dadosSQL.getInt("quantidadeConta"));
        obj.setValorTotalConta(dadosSQL.getDouble("valorTotalConta"));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosRegistroHeader(obj, nivelMontarDados, usuario);
        montarDadosRegistroTrailer(obj, nivelMontarDados, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setRegistroDetalheVOs(getFacadeFactory().getRegistroDetalheFacade().consultarPorRegistroArquivoIdentificacaoRegistroEmpresa(identificacaoEmpresa, obj.getCodigo(), false, nivelMontarDados, usuario));
        obj.setContaReceberRegistroArquivoVOs(getFacadeFactory().getContaReceberRegistroArquivoFacade().consultaRapidaPorRegistroArquivo(obj.getCodigo(), null, null, "", "", "", false, usuario, null));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS) {
            return obj;
        }
        return obj;
    }

    public static void montarDadosRegistroHeader(RegistroArquivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getRegistroHeader().getCodigo().intValue() == 0) {
            obj.setRegistroHeader(new RegistroHeaderVO());
            return;
        }
        obj.setRegistroHeader(getFacadeFactory().getRegistroHeaderFacade().consultarPorChavePrimaria(obj.getRegistroHeader().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosRegistroTrailer(RegistroArquivoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getRegistroTrailer().getCodigo().intValue() == 0) {
            obj.setRegistroTrailer(new RegistroTrailerVO());
            return;
        }
        obj.setRegistroTrailer(getFacadeFactory().getRegistroTrailerFacade().consultarPorChavePrimaria(obj.getRegistroTrailer().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe
     * <code>RegistroArquivoVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto
     *                procurado.
     */
    public RegistroArquivoVO consultarPorChavePrimaria(Integer codigoPrm, String identificacaoTituloEmpresa, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT distinct registroarquivo.*, min (registrodetalhe.datacredito) as datacredito, ");
        sqlStr.append(" count(registrodetalhe.codigo) as quantidadeConta, ");
        sqlStr.append(" sum(registrodetalhe.valorPago) as valorTotalConta ");
        sqlStr.append(" FROM RegistroArquivo ");
        sqlStr.append(" inner join registrodetalhe on registrodetalhe.registroarquivo = registroarquivo.codigo ");
        sqlStr.append(" where registroarquivo.codigo = ? ");
        sqlStr.append(" group by registroarquivo.registrotrailer, registroarquivo.registroheader, registroarquivo.codigo, registroarquivo.arquivoprocessado, ");
        sqlStr.append(" registroarquivo.contasbaixadas, registroarquivo.totaltaxaboletocobradobanco, totaltaxapagaaluno ");
//        String sql = "SELECT * FROM RegistroArquivo WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( RegistroArquivo ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, identificacaoTituloEmpresa, usuario));
    }

    public RegistroArquivoVO consultarPorChavePrimariaCompleto(Integer codigoPrm, String identificacaoTituloEmpresa, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = new StringBuilder();
         
        sqlStr.append(" SELECT distinct registroarquivo.*, min (registrodetalhe.datacredito) as datacredito, ");
        sqlStr.append(" count(registrodetalhe.codigo) as quantidadeConta, ");
        sqlStr.append(" sum(registrodetalhe.valorPago) as valorTotalConta ");
        sqlStr.append(" FROM controlecobranca ");
        sqlStr.append(" inner join registroarquivo on registroarquivo.codigo = controlecobranca.registroarquivo ");
        sqlStr.append(" inner join registrodetalhe on registrodetalhe.registroarquivo = registroarquivo.codigo ");
        sqlStr.append(" where RegistroArquivo.codigo = ? ");
        sqlStr.append(" group by registroarquivo.registrotrailer, registroarquivo.registroheader, registroarquivo.codigo, registroarquivo.arquivoprocessado, ");
        sqlStr.append(" registroarquivo.contasbaixadas, registroarquivo.totaltaxaboletocobradobanco, totaltaxapagaaluno ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            return new RegistroArquivoVO();
        }
        return (montarDadosCompleto(tabelaResultado, nivelMontarDados, identificacaoTituloEmpresa, usuario));
    }

    public RegistroArquivoVO consultarPorControleCobranca(Integer controleCobranca, String identificacaoTituloEmpresa, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append(" SELECT distinct registroarquivo.*, min (registrodetalhe.datacredito) as datacredito, ");
        sqlStr.append(" count(registrodetalhe.codigo) as quantidadeConta, ");
        sqlStr.append(" sum(registrodetalhe.valorPago) as valorTotalConta ");
        sqlStr.append(" FROM controlecobranca ");
        sqlStr.append(" inner join registroarquivo on registroarquivo.codigo = controlecobranca.registroarquivo ");
        sqlStr.append(" inner join registrodetalhe on registrodetalhe.registroarquivo = registroarquivo.codigo ");
        sqlStr.append(" where controlecobranca.codigo = ?");
        sqlStr.append(" group by registroarquivo.registrotrailer, registroarquivo.registroheader, registroarquivo.codigo, registroarquivo.arquivoprocessado, ");
        sqlStr.append(" registroarquivo.contasbaixadas, registroarquivo.totaltaxaboletocobradobanco, totaltaxapagaaluno ");
//        StringBuilder sql = new StringBuilder("SELECT ra.* FROM controlecobranca cc ");
//        sql.append("INNER JOIN registroarquivo ra ON ra.codigo = cc.registroarquivo ");
//        sql.append("WHERE cc.codigo = ? ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{controleCobranca});
        if (!tabelaResultado.next()) {
            return new RegistroArquivoVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados, identificacaoTituloEmpresa, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return RegistroArquivo.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        RegistroArquivo.idEntidade = idEntidade;
    }
}
