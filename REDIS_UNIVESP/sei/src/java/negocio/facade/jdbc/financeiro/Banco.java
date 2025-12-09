package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.ModeloBoletoVO;
import negocio.comuns.financeiro.enumerador.TipoCobrancaPixEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.ModeloGeracaoBoleto;
import negocio.comuns.utilitarias.dominios.ModeloGeracaoBoletoSicoob;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.BancoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>BancoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>BancoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see BancoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Banco extends ControleAcesso implements BancoInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = -5592967301519260006L;
	protected static String idEntidade = "Banco";
   

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>BancoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>BancoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final BancoVO obj, UsuarioVO usuario) throws Exception {
        try {
            BancoVO.validarDados(obj);
			Banco.incluir(getIdEntidade(), true, usuario);
            final String sql = "INSERT INTO Banco( nome, nrBanco, modeloboletomatricula, modeloboletomensalidade, modeloboletoprocessoseletivo, modeloboletorequerimento, modeloboletooutros, "
            		+ "digito, modeloBoletoRenegociacao, modeloGeracaoBoleto, modeloBoletoMaterialDidatico, urlApiPixProducao, urlApiPixHomologacao, urlApiPixAutenticacao,"
            		+ " orientacaoPix, integracaoPix, possuiWebhook,  tipoCobrancaPixEnum) "
            		+ "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        	obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(final Connection con) throws SQLException {
					final PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setString(2, obj.getNrBanco());
                    if (obj.getModeloBoletoMatricula().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(3, obj.getModeloBoletoMatricula().getCodigo());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    if (obj.getModeloBoletoMensalidade().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(4, obj.getModeloBoletoMensalidade().getCodigo());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    if (obj.getModeloBoletoProcessoSeletivo().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(5, obj.getModeloBoletoProcessoSeletivo().getCodigo());
                    } else {
                        sqlInserir.setNull(5, 0);
                    }
                    if (obj.getModeloBoletoRequerimento().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(6, obj.getModeloBoletoRequerimento().getCodigo());
                    } else {
                        sqlInserir.setNull(6, 0);
                    }
                    if (obj.getModeloBoletoOutros().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(7, obj.getModeloBoletoOutros().getCodigo());
                    } else {
                        sqlInserir.setNull(7, 0);
                    }
                    sqlInserir.setString(8, obj.getDigito());
                    if (obj.getModeloBoletoRenegociacao().getCodigo().intValue() != 0) {
                    	sqlInserir.setInt(9, obj.getModeloBoletoRenegociacao().getCodigo());
                    } else {
                    	sqlInserir.setNull(9, 0);
                    }
                    if (!obj.getNrBanco().equals("104") && !obj.getNrBanco().equals("756")) {
                    	obj.setModeloGeracaoBoleto("");
                    }
                    sqlInserir.setString(10, obj.getModeloGeracaoBoleto());
                    int i = 10;
                    Uteis.setValuePreparedStatement(obj.getModeloBoletoMaterialDidatico(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getUrlApiPixProducao(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getUrlApiPixHomologacao(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getUrlApiPixAutenticacao(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getOrientacaoPix(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.isIntegracaoPix(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.isPossuiWebhook(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getTipoCobrancaPixEnum().name(), ++i, sqlInserir);
                    return sqlInserir;
                }
        	}, new ResultSetExtractor<Object>() {
				public Object extractData(final ResultSet rs) throws SQLException {
					if (rs.next()) {
						obj.setNovoObj(Boolean.FALSE);
						return rs.getInt("codigo");
					}
					return null;
				}
			}));
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {

            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>BancoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>BancoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final BancoVO obj, UsuarioVO usuario) throws Exception {
        try {
            BancoVO.validarDados(obj);
            Banco.alterar(getIdEntidade(), true, usuario);
            final String sql = "UPDATE Banco set nome=?, nrBanco=?, modeloboletomatricula=?, modeloboletomensalidade=?, modeloboletoprocessoseletivo=?,"
            		+ " modeloboletorequerimento=?, modeloboletooutros=?, digito=?, modeloBoletoRenegociacao = ?, modeloGeracaoBoleto=?, modeloBoletoMaterialDidatico=?, "
            		+ " urlApiPixProducao=?, urlApiPixHomologacao=?, urlApiPixAutenticacao=?, orientacaoPix=?, "
            		+ " integracaoPix=?, possuiWebhook=?,  tipoCobrancaPixEnum=? "
            		+ " WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setString(2, obj.getNrBanco());
                    if (obj.getModeloBoletoMatricula().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(3, obj.getModeloBoletoMatricula().getCodigo());
                    } else {
                        sqlAlterar.setNull(3, 0);
                    }
                    if (obj.getModeloBoletoMensalidade().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(4, obj.getModeloBoletoMensalidade().getCodigo());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
                    if (obj.getModeloBoletoProcessoSeletivo().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(5, obj.getModeloBoletoProcessoSeletivo().getCodigo());
                    } else {
                        sqlAlterar.setNull(5, 0);
                    }
                    if (obj.getModeloBoletoRequerimento().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(6, obj.getModeloBoletoRequerimento().getCodigo());
                    } else {
                        sqlAlterar.setNull(6, 0);
                    }
                    if (obj.getModeloBoletoOutros().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(7, obj.getModeloBoletoOutros().getCodigo());
                    } else {
                        sqlAlterar.setNull(7, 0);
                    }
                    sqlAlterar.setString(8, obj.getDigito());
                    if (obj.getModeloBoletoRenegociacao().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(9, obj.getModeloBoletoRenegociacao().getCodigo());
                    } else {
                        sqlAlterar.setNull(9, 0);
                    }
                    if (!obj.getNrBanco().equals("104") && !obj.getNrBanco().equals("756")) {
                    	obj.setModeloGeracaoBoleto("");
                    }                    
                    sqlAlterar.setString(10, obj.getModeloGeracaoBoleto());
                    int i = 10;
                    Uteis.setValuePreparedStatement(obj.getModeloBoletoMaterialDidatico(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getUrlApiPixProducao(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getUrlApiPixHomologacao(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getUrlApiPixAutenticacao(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getOrientacaoPix(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.isIntegracaoPix(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.isPossuiWebhook(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getTipoCobrancaPixEnum().name(), ++i, sqlAlterar);
                    sqlAlterar.setInt(++i, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>BancoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>BancoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(BancoVO obj, UsuarioVO usuario) throws Exception {
        try {
			Banco.excluir(getIdEntidade(), true, usuario);
            String sql = "DELETE FROM Banco WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Banco</code> através do valor do atributo 
     * <code>String nrBanco</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>BancoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<BancoVO> consultarPorNrBanco(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Banco WHERE upper( nrBanco ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY nrBanco";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    public BancoVO consultarPorCodigoContaReceber(Integer codigoContaReceber, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("select * from banco b ");
        sqlStr.append("inner join agencia a on a.banco = b.codigo ");
        sqlStr.append("inner join contacorrente cc on cc.agencia = a.codigo ");
        sqlStr.append("inner join contareceber cr on cr.contacorrente = cc.codigo ");
        sqlStr.append("where cr.codigo = ").append(codigoContaReceber);
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (!tabelaResultado.next()) {
            return new BancoVO();
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }
    
    public BancoVO consultarPorCodigoAgencia(Integer agencia, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select banco.* from banco ");
    	sb.append(" inner join agencia on agencia.banco = banco.codigo ");
    	sb.append(" where agencia.codigo = ").append(agencia);
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	  if (!tabelaResultado.next()) {
              return new BancoVO();
          }
    	return (montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
    	
    	
    }
    
    @Override
    public BancoVO consultarPorNumeroContaCorrentePorDigitoContaCorrente(String numero, String digito, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select distinct banco.codigo, banco.nome, banco.nrBanco from banco ");
    	sb.append(" inner join agencia on agencia.banco = banco.codigo ");
    	sb.append(" inner join contacorrente on contacorrente.agencia = agencia.codigo ");
    	sb.append("WHERE contacaixa  = false ");
    	if (digito != null && !digito.trim().isEmpty()) {
    		sb.append(" and trim(leading '0' from contacorrente.numero) = '").append(StringUtils.stripStart(numero, "0")).append("' ");
        	sb.append(" and contacorrente.digito = '").append(digito).append("' ");
        }else {
        	sb.append(" and ( trim(leading '0' from contacorrente.numero) = '").append(StringUtils.stripStart(numero, "0")).append("' ");
        	sb.append(" or (trim(leading '0' from contacorrente.numero)||contacorrente.digito) = '").append(StringUtils.stripStart(numero, "0")).append("' ");
        	sb.append(" or (trim(leading '0' from agencia.numeroagencia)||contacorrente.numero||contacorrente.digito) = '").append(StringUtils.stripStart(numero, "0")).append("' ");
        	sb.append(" ) ");
        }
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	BancoVO bancoVO = null;
    	if (tabelaResultado.next()) {
    		bancoVO = new BancoVO();
    		bancoVO.setCodigo(tabelaResultado.getInt("codigo"));
    		bancoVO.setNome(tabelaResultado.getString("nome"));
    		bancoVO.setNrBanco(tabelaResultado.getString("nrBanco"));
    	}
    	return bancoVO;
    	
    }

    /**
     * Responsável por realizar uma consulta de <code>Banco</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>BancoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<BancoVO> consultarPorNome(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Banco WHERE upper(sem_acentos( nome )) ilike(sem_acentos('" + valorConsulta.toUpperCase() + "%')) ORDER BY nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    /**
     * Responsável por realizar uma consulta de <code>Banco</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>BancoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<BancoVO>consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Banco WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados));
    }

    public List<BancoVO> consultarPorBancoNivelComboBox(boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT codigo, nome, nrBanco FROM Banco ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        List <BancoVO>vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            BancoVO obj = new BancoVO();
            obj.setCodigo(tabelaResultado.getInt("codigo"));
            obj.setNome(tabelaResultado.getString("nome"));
            obj.setNrBanco(tabelaResultado.getString("nrBanco"));            
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>BancoVO</code> resultantes da consulta.
     */
    public static List<BancoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List<BancoVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>BancoVO</code>.
     * @return  O objeto da classe <code>BancoVO</code> com os dados devidamente montados.
     */
    public static BancoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        BancoVO obj = new BancoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.setNrBanco(dadosSQL.getString("nrBanco"));
        obj.setDigito(dadosSQL.getString("digito"));        
        obj.getModeloBoletoMatricula().setCodigo(dadosSQL.getInt("modeloboletomatricula"));
        obj.getModeloBoletoMensalidade().setCodigo(dadosSQL.getInt("modeloboletomensalidade"));
        obj.getModeloBoletoMaterialDidatico().setCodigo(dadosSQL.getInt("modeloboletomaterialdidatico"));
        obj.getModeloBoletoProcessoSeletivo().setCodigo(dadosSQL.getInt("modeloboletoprocessoseletivo"));
        obj.getModeloBoletoRequerimento().setCodigo(dadosSQL.getInt("modeloboletorequerimento"));
        obj.getModeloBoletoRenegociacao().setCodigo(dadosSQL.getInt("modeloboletorenegociacao"));
        obj.getModeloBoletoOutros().setCodigo(dadosSQL.getInt("modeloboletooutros"));
        if (obj.getNrBanco().equals("756")) {
        	obj.setModeloGeracaoBoleto(ModeloGeracaoBoletoSicoob.getEnum(dadosSQL.getString("modeloGeracaoBoleto")).getValor());
        } else {
        	if (Uteis.isAtributoPreenchido(dadosSQL.getString("modeloGeracaoBoleto"))) {
        		obj.setModeloGeracaoBoleto(ModeloGeracaoBoleto.getEnum(dadosSQL.getString("modeloGeracaoBoleto")).getValor());
        	}
        }
        obj.setUrlApiPixProducao(dadosSQL.getString("urlApiPixProducao"));
        obj.setUrlApiPixHomologacao(dadosSQL.getString("urlApiPixHomologacao"));
        obj.setUrlApiPixAutenticacao(dadosSQL.getString("urlApiPixAutenticacao"));
        obj.setOrientacaoPix(dadosSQL.getString("orientacaoPix"));
        obj.setIntegracaoPix(dadosSQL.getBoolean("integracaoPix"));
        obj.setPossuiWebhook(dadosSQL.getBoolean("possuiWebhook"));
        obj.setTipoCobrancaPixEnum(TipoCobrancaPixEnum.valueOf(dadosSQL.getString("tipoCobrancaPixEnum")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosModeloBoletoMatricula(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO());
        montarDadosModeloBoletoMensalidade(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO());
        montarDadosModeloBoletoMaterialDidatico(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO());
        montarDadosModeloBoletoProcessoSeletivo(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO());
        montarDadosModeloBoletoRequerimento(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO());
        montarDadosModeloBoletoRenegociacao(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO());
        montarDadosModeloBoletoOutros(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, new UsuarioVO());
        return obj;
    }

    public static void montarDadosModeloBoletoOutros(BancoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getModeloBoletoOutros().getCodigo().intValue() == 0) {
            obj.setModeloBoletoOutros(new ModeloBoletoVO());
            return;
        }
        obj.setModeloBoletoOutros(getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(obj.getModeloBoletoOutros().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosModeloBoletoRequerimento(BancoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getModeloBoletoRequerimento().getCodigo().intValue() == 0) {
            obj.setModeloBoletoRequerimento(new ModeloBoletoVO());
            return;
        }
        obj.setModeloBoletoRequerimento(getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(obj.getModeloBoletoRequerimento().getCodigo(), nivelMontarDados, usuario));
    }
    public static void montarDadosModeloBoletoRenegociacao(BancoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	if (obj.getModeloBoletoRenegociacao().getCodigo().intValue() == 0) {
    		obj.setModeloBoletoRenegociacao(new ModeloBoletoVO());
    		return;
    	}
    	obj.setModeloBoletoRenegociacao(getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(obj.getModeloBoletoRenegociacao().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosModeloBoletoProcessoSeletivo(BancoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getModeloBoletoProcessoSeletivo().getCodigo().intValue() == 0) {
            obj.setModeloBoletoProcessoSeletivo(new ModeloBoletoVO());
            return;
        }
        obj.setModeloBoletoProcessoSeletivo(getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(obj.getModeloBoletoProcessoSeletivo().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosModeloBoletoMensalidade(BancoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getModeloBoletoMensalidade().getCodigo().intValue() == 0) {
            obj.setModeloBoletoMensalidade(new ModeloBoletoVO());
            return;
        }
        obj.setModeloBoletoMensalidade(getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(obj.getModeloBoletoMensalidade().getCodigo(), nivelMontarDados, usuario));
    }
    
    public static void montarDadosModeloBoletoMaterialDidatico(BancoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	if (obj.getModeloBoletoMaterialDidatico().getCodigo().intValue() == 0) {
    		obj.setModeloBoletoMaterialDidatico(new ModeloBoletoVO());
    		return;
    	}
    	obj.setModeloBoletoMaterialDidatico(getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(obj.getModeloBoletoMaterialDidatico().getCodigo(), nivelMontarDados, usuario));
    }

    public static void montarDadosModeloBoletoMatricula(BancoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getModeloBoletoMatricula().getCodigo().intValue() == 0) {
            obj.setModeloBoletoMatricula(new ModeloBoletoVO());
            return;
        }
        obj.setModeloBoletoMatricula(getFacadeFactory().getModeloBoletoFacade().consultarPorChavePrimaria(obj.getModeloBoletoMatricula().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>BancoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public BancoVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM Banco WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Banco ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Banco.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        Banco.idEntidade = idEntidade;
    }
}
