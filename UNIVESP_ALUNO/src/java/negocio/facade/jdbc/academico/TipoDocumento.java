package negocio.facade.jdbc.academico;

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

import negocio.comuns.academico.DocumentacaoGEDVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.academico.enumeradores.TipoDocumentacaoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.TipoExigenciaDocumentoEnum;
import negocio.comuns.secretaria.enumeradores.TipoUploadArquivoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TipoDocumentoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos
 * dados da classe <code>TipoDocumentoVO</code>. Responsável por implementar
 * operações como incluir, alterar, excluir e consultar pertinentes a classe
 * <code>TipoDocumentoVO</code>. Encapsula toda a interação com o banco de
 * dados.
 * 
 * @see TipoDocumentoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class TipoDocumento extends ControleAcesso implements TipoDocumentoInterfaceFacade {

    private static final long serialVersionUID = 3143452494036043955L;

    protected static String idEntidade;

    public TipoDocumento() throws Exception {
        super();
        setIdEntidade("TipoDocumento");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe
     * <code>TipoDocumentoVO</code>.
     */
    public TipoDocumentoVO novo() throws Exception {
        TipoDocumento.incluir(getIdEntidade());
        TipoDocumentoVO obj = new TipoDocumentoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe
     * <code>TipoDocumentoVO</code>. Primeiramente valida os dados (
     * <code>validarDados</code>) do objeto. Verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>TipoDocumentoVO</code> que será gravado
     *            no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TipoDocumentoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            TipoDocumentoVO.validarDados(obj);
            
            incluir(getIdEntidade(), true, usuarioVO);
            obj.realizarUpperCaseDados();
            final StringBuilder sql = new StringBuilder(" INSERT INTO TipoDocumento( nome, utilizaFuncionario, escolaridade, tipoExigenciaDocumento, documentoFrenteVerso, ");
            sql.append(" permitirPostagemPortalAluno, sexo, idade, estrangeiro, estadoCivil, transferencia,  inscricaoProcessoSeletivo, reabertura,  renovacao, portadorDiploma, enem, contrato, identificadorGED, categoriaGED, tipouploadarquivo, extensaoarquivo, documentoObrigatorioFuncionario, tipoIdadeExigida, tipoDocumentacaoEnum, enviarDocumentoXml ) ");
            sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  ) returning codigo");
            
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setBoolean(2, obj.getUtilizaFuncionario());
                    if (obj.getEscolaridade() != null) {
                        sqlInserir.setString(3, obj.getEscolaridade().toString());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    if (obj.getTipoExigenciaDocumento() != null) {
                        sqlInserir.setString(4, obj.getTipoExigenciaDocumento().name());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    sqlInserir.setBoolean(5, obj.getDocumentoFrenteVerso());
                    sqlInserir.setBoolean(6, obj.getPermitirPostagemPortalAluno());
                    sqlInserir.setString(7, obj.getSexo());
                    sqlInserir.setInt(8, obj.getIdade());
                    sqlInserir.setString(9, obj.getEstrangeiro());
                    sqlInserir.setString(10, obj.getEstadoCivil());
                    sqlInserir.setBoolean(11, obj.getTransferencia());
                    sqlInserir.setBoolean(12, obj.getInscricaoProcessoSeletivo());
                    sqlInserir.setBoolean(13, obj.getReabertura());
                    sqlInserir.setBoolean(14, obj.getRenovacao());
                    sqlInserir.setBoolean(15, obj.getPortadorDiploma());
                    sqlInserir.setBoolean(16, obj.getEnem());
                    sqlInserir.setBoolean(17, obj.getContrato());
                    sqlInserir.setString(18, obj.getIdentificadorGED());

                    if (Uteis.isAtributoPreenchido(obj.getCategoriaGED())) {
                    	sqlInserir.setInt(19, obj.getCategoriaGED().getCodigo());
                    } else {
                    	sqlInserir.setNull(19, 0);
                    }
                    int x = 20;
                    Uteis.setValuePreparedStatement(obj.getTipoUploadArquivo().name(), x++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getExtensaoArquivo(), x++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getDocumentoObrigatorioFuncionario(), x++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getTipoIdadeExigida(), x++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getTipoDocumentacaoEnum().name(), x++, sqlInserir);
                    sqlInserir.setBoolean(x++, obj.getEnviarDocumentoXml());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            getFacadeFactory().getTipoDocumentoEquivalenteFacade().incluirTipoDocumentoEquivalentes(obj.getCodigo(), obj.getTipoDocumentoEquivalenteVOs());
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TipoDocumentoVO obj) throws Exception {
        try {
            TipoDocumentoVO.validarDados(obj);
            obj.realizarUpperCaseDados();
            final StringBuilder sql = new StringBuilder(" INSERT INTO TipoDocumento( nome, utilizaFuncionario, escolaridade, tipoExigenciaDocumento, documentoFrenteVerso, ");
            sql.append(" permitirPostagemPortalAluno, sexo, idade, estrangeiro, estadoCivil, transferencia,  inscricaoProcessoSeletivo, reabertura,  renovacao, portadorDiploma, enem, contrato, identificadorGED, categoriaGED, tipouploadarquivo, extensaoarquivo, documentoObrigatorioFuncionario, tipoIdadeExigida, tipoDocumentacaoEnum, enviarDocumentoXml  ) ");
            sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?  ) returning codigo");
            
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setBoolean(2, obj.getUtilizaFuncionario());
                    if (obj.getEscolaridade() != null) {
                        sqlInserir.setString(3, obj.getEscolaridade().toString());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    if (obj.getTipoExigenciaDocumento() != null) {
                        sqlInserir.setString(4, obj.getTipoExigenciaDocumento().name());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    sqlInserir.setBoolean(5, obj.getDocumentoFrenteVerso());
                    sqlInserir.setBoolean(6, obj.getPermitirPostagemPortalAluno());
                    sqlInserir.setString(7, obj.getSexo());
                    sqlInserir.setInt(8, obj.getIdade());
                    sqlInserir.setString(9, obj.getEstrangeiro());
                    sqlInserir.setString(10, obj.getEstadoCivil());
                    sqlInserir.setBoolean(11, obj.getTransferencia());
                    sqlInserir.setBoolean(12, obj.getInscricaoProcessoSeletivo());
                    sqlInserir.setBoolean(13, obj.getReabertura());
                    sqlInserir.setBoolean(14, obj.getRenovacao());
                    sqlInserir.setBoolean(15, obj.getPortadorDiploma());
                    sqlInserir.setBoolean(16, obj.getEnem());
                    sqlInserir.setBoolean(17, obj.getContrato());
                    sqlInserir.setString(18, obj.getIdentificadorGED());
                    sqlInserir.setInt(19, obj.getCategoriaGED().getCodigo());
                    int x = 20;
                    Uteis.setValuePreparedStatement(obj.getTipoUploadArquivo().name(), x++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getExtensaoArquivo(), x++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getDocumentoObrigatorioFuncionario(), x++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getTipoIdadeExigida(), x++, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getTipoDocumentacaoEnum().name(), x++, sqlInserir);
                    sqlInserir.setBoolean(x++, obj.getEnviarDocumentoXml());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
            getFacadeFactory().getTipoDocumentoEquivalenteFacade().incluirTipoDocumentoEquivalentes(obj.getCodigo(), obj.getTipoDocumentoEquivalenteVOs());
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe
     * <code>TipoDocumentoVO</code>. Sempre utiliza a chave primária da classe
     * como atributo para localização do registro a ser alterado. Primeiramente
     * valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>TipoDocumentoVO</code> que será
     *            alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou
     *                validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final TipoDocumentoVO obj, UsuarioVO usuario) throws Exception {
        try {
            TipoDocumentoVO.validarDados(obj);
            alterar(getIdEntidade(), true, usuario);
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE TipoDocumento set nome=?, utilizaFuncionario=?, escolaridade = ?, tipoExigenciaDocumento=?, documentoFrenteVerso=?, permitirPostagemPortalAluno=?, sexo=?, idade = ?, estrangeiro = ?, estadoCivil = ?, transferencia = ?, inscricaoProcessoSeletivo = ?, reabertura = ?,  renovacao = ?, portadorDiploma=?, enem = ?, contrato=?, identificadorGED = ?, categoriaGED = ?, tipouploadarquivo = ?, extensaoarquivo=?, documentoObrigatorioFuncionario = ?, tipoIdadeExigida=?, tipoDocumentacaoEnum=?, enviarDocumentoXml=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setBoolean(2, obj.getUtilizaFuncionario());
                    if (obj.getEscolaridade() != null) {
                        sqlAlterar.setString(3, obj.getEscolaridade().toString());
                    } else {
                        sqlAlterar.setNull(3, 0);
                    }
                    if (obj.getTipoExigenciaDocumento() != null) {
                        sqlAlterar.setString(4, obj.getTipoExigenciaDocumento().name());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
                    sqlAlterar.setBoolean(5, obj.getDocumentoFrenteVerso());
                    sqlAlterar.setBoolean(6, obj.getPermitirPostagemPortalAluno());
                    sqlAlterar.setString(7, obj.getSexo());
                    sqlAlterar.setInt(8, obj.getIdade());
                    sqlAlterar.setString(9, obj.getEstrangeiro());
                    sqlAlterar.setString(10, obj.getEstadoCivil());
                    sqlAlterar.setBoolean(11, obj.getTransferencia());
                    sqlAlterar.setBoolean(12, obj.getInscricaoProcessoSeletivo());
                    sqlAlterar.setBoolean(13, obj.getReabertura());
                    sqlAlterar.setBoolean(14, obj.getRenovacao());
                    sqlAlterar.setBoolean(15, obj.getPortadorDiploma());
                    sqlAlterar.setBoolean(16, obj.getEnem());
                    sqlAlterar.setBoolean(17, obj.getContrato());
                    sqlAlterar.setString(18, obj.getIdentificadorGED());

                    if (Uteis.isAtributoPreenchido(obj.getCategoriaGED())) {
                    	sqlAlterar.setInt(19, obj.getCategoriaGED().getCodigo());
                    } else {
                    	sqlAlterar.setNull(19, 0);
                    }

                    int x = 20;
                    Uteis.setValuePreparedStatement(obj.getTipoUploadArquivo().name(), x++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getExtensaoArquivo(), x++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getDocumentoObrigatorioFuncionario(), x++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getTipoIdadeExigida(), x++, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getTipoDocumentacaoEnum().name(), x++, sqlAlterar);
                    sqlAlterar.setBoolean(x++, obj.getEnviarDocumentoXml());
                    sqlAlterar.setInt(x++, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
            getFacadeFactory().getTipoDocumentoEquivalenteFacade().alterarTipoDocumentoEquivalentes(obj.getCodigo(), obj.getTipoDocumentoEquivalenteVOs());
            realizarSincronizacaoDocumentacaoMatricula(obj, usuario);
            getAplicacaoControle().removerTipoDocumento(obj.getCodigo());
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarSincronizacaoDocumentacaoMatricula(TipoDocumentoVO obj, UsuarioVO usuarioVO) {
    	if (obj.getTipoExigenciaDocumento() != null && obj.getTipoExigenciaDocumento().equals(TipoExigenciaDocumentoEnum.EXIGENCIA_ALUNO)) {
            StringBuilder sql = new StringBuilder("");
            sql.append(" select m.aluno, max(case when dm.situacao = 'OK' then 1 else 0 end) as situacao, max(case when dm.arquivo is null then 0 else dm.arquivo end) as arquivo, ");
            sql.append(" max(case when dm.arquivoVerso is null then 0 else dm.arquivoVerso end) as arquivoVerso, max(dm.dataEntrega) as dataEntrega, dm.entregue as entregue, max(dm.usuario) usuario, ");
            sql.append(" max(case when dm.arquivoassinado is null then 0 else dm.arquivoassinado end) as arquivoassinado ");
            sql.append(" from documetacaomatricula dm ");
            sql.append(" inner join matricula m on m.matricula = dm.matricula ");
            sql.append(" where dm.tipodedocumento = ").append(obj.getCodigo());
            sql.append(" and dm.entregue and dm.situacao = 'OK' and exists ( select from documetacaomatricula d2 inner join matricula m2 on m2.matricula = d2.matricula ");
            sql.append(" where d2.matricula != dm.matricula and m2.aluno = m.aluno and d2.tipodedocumento = dm.tipodedocumento and coalesce(d2.entregue, false) = false ) ");
            sql.append(" group by m.aluno, dm.entregue ");
            SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());

            String situacao = null;
            Date dataEntrega = null;
            Integer arquivo = null;
            Integer arquivoVerso = null;
            Integer arquivoAssinado = null;
            Integer aluno = null;
            Integer usuario = null;
            Boolean entregue = null;
            Boolean possuiArquivo = null;
            while (rs.next()) {
                aluno = rs.getInt("aluno");
                situacao = rs.getInt("situacao") == 1 ? "OK" : "PE";
                arquivo = rs.getInt("arquivo") == 0 ? null : rs.getInt("arquivo");
                arquivoVerso = rs.getInt("arquivoVerso") == 0 ? null : rs.getInt("arquivoVerso");
                arquivoAssinado = rs.getInt("arquivoassinado") == 0 ? null : rs.getInt("arquivoassinado");
                usuario = rs.getInt("usuario");
                dataEntrega = rs.getDate("dataEntrega") == null ? null : rs.getDate("dataEntrega");
                entregue = rs.getBoolean("entregue");
                possuiArquivo = Uteis.isAtributoPreenchido(arquivo);
                getFacadeFactory().getDocumetacaoMatriculaFacade().alteraDocumentoAluno(situacao, dataEntrega, arquivo, arquivoVerso, arquivoAssinado, aluno, obj.getCodigo(), usuario, null, entregue, possuiArquivo);
            }
        }
    }
    
    /**
     * Operação responsável por excluir no BD um objeto da classe
     * <code>TipoDocumentoVO</code>. Sempre localiza o registro a ser excluído
     * através da chave primária da entidade. Primeiramente verifica a conexão
     * com o banco de dados e a permissão do usuário para realizar esta operacão
     * na entidade. Isto, através da operação <code>excluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>TipoDocumentoVO</code> que será
     *            removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(TipoDocumentoVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM TipoDocumento WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>TipoDocumento</code>
     * através do valor do atributo <code>String nome</code>. Retorna os
     * objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o
     * trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     *         <code>TipoDocumentoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<TipoDocumentoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TipoDocumento WHERE upper( nome ) like upper(?) ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    public List<TipoDocumentoVO> consultarUtilizadosPorFuncionarios(Integer idPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr =  new StringBuilder( "SELECT * FROM TipoDocumento WHERE documentoObrigatorioFuncionario = true ") ;
        sqlStr.append(" or exists( select DocumetacaoPessoa.codigo from DocumetacaoPessoa where DocumetacaoPessoa.tipodocumento = TipoDocumento.codigo and TipoDocumento.documentoObrigatorioFuncionario = false and DocumetacaoPessoa.pessoa = ? )");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), new Object[]{ idPessoa });
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }
    
    public List<TipoDocumentoVO> consultarTipoDocumentoUtilizadosPorFuncionarios(boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TipoDocumento WHERE utilizafuncionario = true and documentoObrigatorioFuncionario = false";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }
    
    
    public List<TipoDocumentoVO> consultarTipoDocumentoFuncionarioManual(Integer idPessoa, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuarioLogado) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuarioLogado); 
        String sqlStr = "SELECT TipoDocumento.* FROM TipoDocumento inner join DocumetacaoPessoa on DocumetacaoPessoa.tipodocumento = TipoDocumento.codigo where TipoDocumento.documentoObrigatorioFuncionario = false and DocumetacaoPessoa.pessoa = ?  ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{ idPessoa });
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>TipoDocumento</code>
     * através do valor do atributo <code>Integer codigo</code>. Retorna os
     * objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de
     * prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui
     *            permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe
     *         <code>TipoDocumentoVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<TipoDocumentoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TipoDocumento WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }
    
   public List<TipoDocumentoVO> consultarPorSituacaoMatricula(String matricula, int nivelMontarDados) throws Exception {
        String sqlStr = "SELECT distinct tipodocumento.* FROM tipodocumento ";
        sqlStr += "LEFT JOIN documetacaomatricula ON tipodocumento.codigo = documetacaomatricula.tipodedocumento ";
        sqlStr += "LEFT JOIN matriculaperiodo ON documetacaomatricula.matricula = matriculaperiodo.matricula ";
        sqlStr += "WHERE documetacaomatricula.entregue is false AND matriculaperiodo.matricula = '" + matricula + "' ORDER BY tipodocumento.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma
     * consulta ao banco de dados (<code>ResultSet</code>). Faz uso da operação
     * <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     *
     * @return List Contendo vários objetos da classe
     *         <code>TipoDocumentoVO</code> resultantes da consulta.
     */
    public  List<TipoDocumentoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List<TipoDocumentoVO> vetResultado = new ArrayList<TipoDocumentoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de
     * dados (<code>ResultSet</code>) em um objeto da classe
     * <code>TipoDocumentoVO</code>.
     *
     * @return O objeto da classe <code>TipoDocumentoVO</code> com os dados
     *         devidamente montados.
     *         
     * Atenção ao alterar o nivelmontardados desta entidade, pois a lista de tipoDocumentoEquivalenteVOs só deve ser montada para o TipoDocumento principal.
     * 
     */
    @SuppressWarnings("unchecked")
	public  TipoDocumentoVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        TipoDocumentoVO obj = new TipoDocumentoVO();
        obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
        obj.setNome(tabelaResultado.getString("nome"));
        obj.setUtilizaFuncionario(tabelaResultado.getBoolean("utilizaFuncionario"));
        obj.setDocumentoObrigatorioFuncionario(tabelaResultado.getBoolean("documentoObrigatorioFuncionario"));
        obj.setContrato(tabelaResultado.getBoolean("contrato"));
        obj.setEscolaridade(tabelaResultado.getString("escolaridade"));
        obj.setDocumentoFrenteVerso(tabelaResultado.getBoolean("documentoFrenteVerso"));
        obj.setPermitirPostagemPortalAluno(tabelaResultado.getBoolean("permitirPostagemPortalAluno"));
        obj.setSexo(tabelaResultado.getString("sexo"));
        obj.setIdade(tabelaResultado.getInt("idade"));
        obj.setEstrangeiro(tabelaResultado.getString("estrangeiro"));
        obj.setEstadoCivil(tabelaResultado.getString("estadoCivil"));
        obj.setTransferencia(tabelaResultado.getBoolean("transferencia"));
        obj.setInscricaoProcessoSeletivo(tabelaResultado.getBoolean("inscricaoProcessoSeletivo"));
        obj.setReabertura(tabelaResultado.getBoolean("reabertura"));
        obj.setRenovacao(tabelaResultado.getBoolean("renovacao"));
        obj.setPortadorDiploma(tabelaResultado.getBoolean("portadorDiploma"));
        obj.setEnem(tabelaResultado.getBoolean("enem"));
        obj.setIdentificadorGED(tabelaResultado.getString("identificadorGED"));
        obj.getCategoriaGED().setCodigo(tabelaResultado.getInt("categoriaGED"));
        if (tabelaResultado.getString("tipoExigenciaDocumento") != null) {
            obj.setTipoExigenciaDocumento(TipoExigenciaDocumentoEnum.valueOf(tabelaResultado.getString("tipoExigenciaDocumento")));
        } else {
            obj.setTipoExigenciaDocumento(null);
        }
        if (tabelaResultado.getString("tipoUploadArquivo") != null && !tabelaResultado.getString("tipoUploadArquivo").equals("")) {
       	 obj.setTipoUploadArquivo(TipoUploadArquivoEnum.valueOf(tabelaResultado.getString("tipoUploadArquivo")));
		} else {
			obj.setTipoUploadArquivo(null);
		}

		if (tabelaResultado.getString("extensaoArquivo") != null) {
			obj.setExtensaoArquivo(tabelaResultado.getString("extensaoArquivo"));
		}
		obj.setTipoIdadeExigida(tabelaResultado.getString("tipoIdadeExigida"));
		if (tabelaResultado.getString("tipoDocumentacaoEnum") != null) {
            obj.setTipoDocumentacaoEnum(TipoDocumentacaoEnum.valueOf(tabelaResultado.getString("tipoDocumentacaoEnum")));
        } else {
            obj.setTipoDocumentacaoEnum(null);
        }
        obj.setEnviarDocumentoXml(tabelaResultado.getBoolean("enviarDocumentoXml"));
		obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        // Atenção ao alterar o nivelmontardados desta entidade, pois a lista de tipoDocumentoEquivalenteVOs só deve ser montada para o TipoDocumento principal.
        obj.setTipoDocumentoEquivalenteVOs(getFacadeFactory().getTipoDocumentoEquivalenteFacade().consultarTipoDocumentoEquivalentes(obj.getCodigo(), false));
        montarDadosCategoriaGED(obj);
        return obj;
    }
    
    public  void montarDadosCategoriaGED(TipoDocumentoVO tipoDocumentoVO) throws Exception{
    	if(Uteis.isAtributoPreenchido(tipoDocumentoVO.getCategoriaGED())) {
    		tipoDocumentoVO.setCategoriaGED(getFacadeFactory().getCategoriaGEDInterfaceFacade().consultarPorChavePrimaria(tipoDocumentoVO.getCategoriaGED().getCodigo()));
    	}
    }
    
    public static TipoDocumentoVO montarDadosCategoriaGED(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        TipoDocumentoVO obj = new TipoDocumentoVO();
        obj.setCodigo(new Integer(tabelaResultado.getInt("codigo")));
        obj.setNome(tabelaResultado.getString("nome"));
        obj.setUtilizaFuncionario(tabelaResultado.getBoolean("utilizaFuncionario"));
        obj.setDocumentoObrigatorioFuncionario(tabelaResultado.getBoolean("documentoObrigatorioFuncionario"));
        obj.setContrato(tabelaResultado.getBoolean("contrato"));
        obj.setEscolaridade(tabelaResultado.getString("escolaridade"));
        obj.setDocumentoFrenteVerso(tabelaResultado.getBoolean("documentoFrenteVerso"));
        obj.setPermitirPostagemPortalAluno(tabelaResultado.getBoolean("permitirPostagemPortalAluno"));
        obj.setSexo(tabelaResultado.getString("sexo"));
        obj.setIdade(tabelaResultado.getInt("idade"));
        obj.setEstrangeiro(tabelaResultado.getString("estrangeiro"));
        obj.setEstadoCivil(tabelaResultado.getString("estadoCivil"));
        obj.setTransferencia(tabelaResultado.getBoolean("transferencia"));
        obj.setInscricaoProcessoSeletivo(tabelaResultado.getBoolean("inscricaoProcessoSeletivo"));
        obj.setReabertura(tabelaResultado.getBoolean("reabertura"));
        obj.setRenovacao(tabelaResultado.getBoolean("renovacao"));
        obj.setPortadorDiploma(tabelaResultado.getBoolean("portadorDiploma"));
        obj.setEnem(tabelaResultado.getBoolean("enem"));
        obj.setIdentificadorGED(tabelaResultado.getString("identificadorGED"));
        obj.getCategoriaGED().setCodigo(tabelaResultado.getInt("categoriaGED"));
        if (tabelaResultado.getString("tipoExigenciaDocumento") != null) {
            obj.setTipoExigenciaDocumento(TipoExigenciaDocumentoEnum.valueOf(tabelaResultado.getString("tipoExigenciaDocumento")));
        } else {
            obj.setTipoExigenciaDocumento(null);
        }
        obj.setTipoIdadeExigida(tabelaResultado.getString("tipoIdadeExigida"));

        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.getCategoriaGED().setDescricao(tabelaResultado.getString("descricao"));
        return obj;
    }

    /**
     * Operação responsável por localizar um objeto da classe
     * <code>TipoDocumentoVO</code> através de sua chave primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto
     *                procurado.
     */
    public TipoDocumentoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
       return getAplicacaoControle().getTipoDocumentoVO(codigoPrm, usuario);
        }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return TipoDocumento.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        TipoDocumento.idEntidade = idEntidade;
    }
    
    @Override
    public TipoDocumentoVO consultarPorCategoriaGedIdentificadorGED(String categoriaGed, String identificadorGED, String matricula) throws Exception {			
		StringBuilder sql = new StringBuilder("SELECT TipoDocumento.* ");
		if(Uteis.isAtributoPreenchido(matricula)) {
			sql.append(", exists(select documetacaomatricula.codigo from documetacaomatricula inner join TipoDocumento on TipoDocumento.codigo = documetacaomatricula.tipodedocumento where documetacaomatricula.matricula = ? and documetacaomatricula.tipodedocumento = TipoDocumento.codigo limit 1 ) as existeMatricula ");
		} else {
			sql.append(", false as existeMatricula ");
		}
		sql.append(" FROM TipoDocumento inner join categoriaged on categoriaged.codigo = TipoDocumento.categoriaged ");
		sql.append(" WHERE TRIM(categoriaged.identificador) = ? and TRIM(TipoDocumento.identificadorGed) = ?  ");
		sql.append(" limit 1 ");
		SqlRowSet tabelaResultado = null;
		if(Uteis.isAtributoPreenchido(matricula)) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), matricula, categoriaGed, identificadorGED);
		}else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), categoriaGed, identificadorGED);
		}
		if (tabelaResultado.next()) {
			return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS);
		}
	
	return null;
}

    @SuppressWarnings("rawtypes")
	@Override
	public List consultarPorCategoriaGED(Integer codigoCategoriaGED) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT TipoDocumento.* , CategoriaGED.descricao FROM TipoDocumento ");
		sql.append(" LEFT JOIN CategoriaGED ON CategoriaGED.codigo = TipoDocumento.categoriaGED");
		sql.append(" WHERE TipoDocumento.categoriaGED = '").append(codigoCategoriaGED).append("'");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        List<TipoDocumentoVO> vetResultado = new ArrayList<TipoDocumentoVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDadosCategoriaGED(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS));
        }
        return vetResultado;
	}
    
    @Override
	public List<TipoDocumentoVO> consultarPorIdentificadorGED(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM TipoDocumento WHERE upper( identificadorGED ) like(?) ORDER BY identificadorGED";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr,  "%"+valorConsulta.toUpperCase()+"%");
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }
	
	@Override
	public List<TipoDocumentoVO> consultarPorCategoriaGED(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT TipoDocumento.*, categoriaged.descricao FROM TipoDocumento left join categoriaged on categoriaged.codigo = TipoDocumento.categoriaged  WHERE upper( categoriaged.descricao ) like upper(?) ORDER BY categoriaged.descricao, TipoDocumento.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }
	
	@Override
	public List<TipoDocumentoVO> consultarPorIdentificadorGEDCategoriaGED(String campoConsulta, String valorConsulta, DocumentacaoGEDVO documentacaoGED, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sql = new StringBuilder(); 
        sql.append("SELECT * FROM TipoDocumento ");

        if (campoConsulta.equals("codigo")) {
			Uteis.validarSomenteNumeroString(valorConsulta);
			sql.append("WHERE categoriaged = ? and  codigo = ? ORDER BY identificadorGED");
		}

		if (campoConsulta.equals("nome")) {
			sql.append("WHERE categoriaged = ? and  upper( nome ) like(?) ORDER BY identificadorGED");
		}

		if (campoConsulta.equals("identificadorGED")) {
			sql.append("WHERE categoriaged = ? and  upper( identificadorGED ) like(?) ORDER BY identificadorGED");
		}

		SqlRowSet tabelaResultado = null; 
		if (campoConsulta.equals("codigo")) {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), documentacaoGED.getCategoriaGED().getCodigo(), Integer.valueOf(valorConsulta));
		} else {
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), documentacaoGED.getCategoriaGED().getCodigo(), "%"+valorConsulta.toUpperCase()+"%");
		}
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }
	
	@Override
	 public List<TipoDocumentoVO> consultarPorTipoExigenciaDocumento(TipoExigenciaDocumentoEnum tipoExigenciaDocumentoEnum, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
	        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), controlarAcesso, usuario);
	        String sqlStr = "SELECT * FROM TipoDocumento WHERE upper( tipoExigenciaDocumento ) like('" + tipoExigenciaDocumentoEnum.name() + "%') ORDER BY nome";
	        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
	        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
	    }
	
	@Override
	public TipoDocumentoVO consultarPorChavePrimariaUnico(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        getFacadeFactory().getControleAcessoFacade().consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM TipoDocumento WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( TipoDocumento - "+codigoPrm+" ).");
}
        return (montarDados(tabelaResultado, nivelMontarDados));
    }
}
