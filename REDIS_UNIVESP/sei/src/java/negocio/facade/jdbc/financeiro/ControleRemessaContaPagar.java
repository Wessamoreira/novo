package negocio.facade.jdbc.financeiro;
import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaPagarControleRemessaContaPagarVO;
import negocio.comuns.financeiro.ContaPagarRegistroArquivoVO;
import negocio.comuns.financeiro.ContaPagarVO;
import negocio.comuns.financeiro.ControleCobrancaPagarVO;
import negocio.comuns.financeiro.ControleRemessaContaPagarVO;
import negocio.comuns.financeiro.RegistroDetalhePagarVO;
import negocio.comuns.financeiro.RegistroHeaderLotePagarVO;
import negocio.comuns.financeiro.UnidadeEnsinoContaCorrenteVO;
import negocio.comuns.financeiro.enumerador.ModalidadeTransferenciaBancariaEnum;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.BancoFactory;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.EditorOC;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.TipoFormaPagamento;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ControleRemessaContaPagarInterfaceFacade;
import negocio.interfaces.financeiro.remessa.ControleRemessaContaPagarLayoutInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>ControleRemessaContaPagarVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>ControleRemessaContaPagarVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ControleRemessaContaPagarVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ControleRemessaContaPagar extends ControleAcesso implements ControleRemessaContaPagarInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3124112768648176880L;
	private ArquivoHelper arquivoHelper = new ArquivoHelper();
    private PrintWriter printWriter;
    protected static String idEntidade;
  

    public ControleRemessaContaPagar() throws Exception {
        super();
        setIdEntidade("ControleRemessaContaPagar");
    }

    /*
     * (non-Javadoc)
     *
     * @see negocio.facade.jdbc.financeiro.ControleRemessaInterfaceFacade#novo()
     */
    public ControleRemessaContaPagarVO novo() throws Exception {
        ControleRemessaContaPagar.incluir(getIdEntidade());
        ControleRemessaContaPagarVO obj = new ControleRemessaContaPagarVO();
        return obj;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * negocio.facade.jdbc.financeiro.ControleRemessaInterfaceFacade#incluir(negocio.comuns.financeiro.ControleRemessaContaPagarVO
     * )
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ControleRemessaContaPagarVO obj, UsuarioVO usuario) throws Exception {
        try {
            ControleRemessa.incluir(getIdEntidade(), true, usuario);
            final String sql = "INSERT INTO ControleRemessaContaPagar ( responsavel, dataGeracao, arquivoRemessaContaPagar, dataInicio, dataFim, contaCorrente, unidadeEnsino, banco, tipoRemessa, situacao ,  arquivoRemessaPixContaPagar ) "
                    + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ,?  ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataGeracao()));
                    if (obj.getArquivoRemessaContaPagar().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(3, obj.getArquivoRemessaContaPagar().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(3, 0);
                    }
                    sqlInserir.setDate(4, Uteis.getDataJDBC(obj.getDataInicio()));
                    sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataFim()));
                    if (obj.getContaCorrenteVO().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(6, obj.getContaCorrenteVO().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(6, 0);
                    }
                    if (obj.getUnidadeEnsinoVO().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(7, obj.getUnidadeEnsinoVO().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(7, 0);
                    }
                    if (obj.getBancoVO().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(8, obj.getBancoVO().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(8, 0);
                    }
                	sqlInserir.setString(9, obj.getTipoRemessa());					
                	sqlInserir.setString(10, obj.getSituacaoControleRemessa().getValor());  
                	if (obj.getArquivoRemessaPixContaPagar().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(11, obj.getArquivoRemessaPixContaPagar().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(11, 0);
                    }
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
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)    
	public void alterarArquivo(ControleRemessaContaPagarVO controleRemessaContaPagarVO, ArquivoVO arquivoRemessaVO,   ArquivoVO arquivoPixRemessaVO ,  UsuarioVO usuarioVO) throws Exception {
		try {
			ControleRemessaContaPagar.alterar(getIdEntidade(), false, usuarioVO);
			StringBuilder sqlStr = new StringBuilder();
			if(Uteis.isAtributoPreenchido(arquivoRemessaVO)) {
				controleRemessaContaPagarVO.setArquivoRemessaContaPagar(arquivoRemessaVO);
				sqlStr.append("UPDATE ControleRemessaContaPagar set arquivoRemessaContaPagar = ").append(arquivoRemessaVO.getCodigo()).append(" WHERE (codigo = ").append(controleRemessaContaPagarVO.getCodigo()).append(")");
				getConexao().getJdbcTemplate().update(sqlStr.toString());
			}else if (Uteis.isAtributoPreenchido(arquivoPixRemessaVO)) {
				controleRemessaContaPagarVO.setArquivoRemessaPixContaPagar(arquivoPixRemessaVO);
				sqlStr.append("UPDATE ControleRemessaContaPagar set arquivoRemessaPixContaPagar = ").append(arquivoPixRemessaVO.getCodigo()).append(" WHERE (codigo = ").append(controleRemessaContaPagarVO.getCodigo()).append(")");
				getConexao().getJdbcTemplate().update(sqlStr.toString());
			}
		} catch (Exception e) {
			throw e;
		}
	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)    
    public void realizarEstorno(ControleRemessaContaPagarVO controleRemessaContaPagarVO, UsuarioVO usuarioVO) throws Exception {
    	try {
    		StringBuilder sqlStr = new StringBuilder();
    		sqlStr.append("UPDATE ControleRemessaContaPagar set situacao = '").append(controleRemessaContaPagarVO.getSituacaoControleRemessa().getValor()).append("' WHERE (codigo = ").append(controleRemessaContaPagarVO.getCodigo()).append(")"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
    		getConexao().getJdbcTemplate().update(sqlStr.toString());
    	} catch (Exception e) {
    		throw e;
    	}
    }
    
    /*
     * (non-Javadoc)
     *
     * @see
     * negocio.facade.jdbc.financeiro.ControleRemessaInterfaceFacade#excluir(negocio.comuns.financeiro.ControleRemessaContaPagarVO
     * )
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ControleRemessaContaPagarVO obj, UsuarioVO usuario) throws Exception {
        try {
            ControleRemessaContaPagar.excluir(getIdEntidade(), true, usuario);
            String sql = "DELETE FROM ControleRemessaContaPagar WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see negocio.facade.jdbc.financeiro.ControleRemessaInterfaceFacade#consultarPorResponsavel(java.lang.Integer,
     * boolean, int)
     */
    public List consultarPorResponsavel(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sqlStr = "SELECT * FROM ControleRemessaContaPagar WHERE responsavel = " + valorConsulta.intValue() + "";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /*
     * (non-Javadoc)
     *
     * @see negocio.facade.jdbc.financeiro.ControleRemessaInterfaceFacade#consultarPorCodigo(java.lang.Integer, boolean,
     * int)
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        String sqlStr = "SELECT ControleRemessaContaPagar.*, banco.nome as nomeBanco, contacorrente.numero as numeroContaCorrente, contacorrente.digito as digitoContaCorrente  FROM ControleRemessaContaPagar "
        		+ "inner join banco on banco.codigo = controleremessaContaPagar.banco "
        		+ "inner join contacorrente on contacorrente.codigo = controleremessaContaPagar.contacorrente "
        		+ "WHERE ControleRemessaContaPagar.codigo = " + valorConsulta.intValue() + " ORDER BY ControleRemessaContaPagar.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorContaCorrente(String valorConsulta, SituacaoControleRemessaEnum situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	String sqlStr = "SELECT distinct ControleRemessaContaPagar.*, banco.nome as nomeBanco, contacorrente.numero as numeroContaCorrente, contacorrente.digito as digitoContaCorrente  FROM ControleRemessaContaPagar "
    			+ "inner join banco on banco.codigo = controleremessaContaPagar.banco "
    			+ "inner join contacorrente on contacorrente.codigo = controleremessaContaPagar.contacorrente "
    			+ " WHERE contacorrente.numero ilike '" + valorConsulta.toLowerCase() + "%'" ;    	
    	if (situacao.getValor() != "" ) {
    		sqlStr += " AND ControleRemessaContaPagar.situacao = '" + situacao.getValor() + "' ";
    	}
    	sqlStr += " AND ((dataGeracao >= '" + Uteis.getDataJDBC(dataIni) + "') and (dataGeracao <= '"
    			+ Uteis.getDataJDBC(dataFim) + "')) ORDER BY dataGeracao";
    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public List consultarPorNossoNumero(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	String sqlStr = "SELECT distinct ControleRemessaContaPagar.*, banco.nome as nomeBanco, contacorrente.numero as numeroContaCorrente, contacorrente.digito as digitoContaCorrente  FROM ControleRemessa "
    			+ "inner join banco on banco.codigo = ControleRemessaContaPagar.banco "
    			+ "inner join contacorrente on contacorrente.codigo = ControleRemessaContaPagar.contacorrente "
    			+ "left join contaPagarControleRemessaContaPagar on contaPagarControleRemessaContaPagar.ControleRemessaContaPagar = ControleRemessaContaPagar.codigo "
    			+ "left join Contapagar on Contapagar.codigo = contaPagarControleRemessaContaPagar.Contapagar "
    			+ "left join pessoa on Contapagar.pessoa = pessoa.codigo "
    			+ " WHERE Contapagar.codigo = '" + valorConsulta + "'" 
    			+" ORDER BY dataGeracao";
    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public List consultarPorNomeSacado(String valorConsulta, SituacaoControleRemessaEnum situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	//ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuilder sql = new StringBuilder();
    	sql.append(" SELECT distinct ControleRemessaContaPagar.*, banco.nome as nomeBanco, contacorrente.numero as numeroContaCorrente, contacorrente.digito as digitoContaCorrente  FROM ControleRemessaContaPagar ");
    	sql.append(" inner join banco on banco.codigo = ControleRemessaContaPagar.banco ");
    	sql.append(" inner join contacorrente on contacorrente.codigo = ControleRemessaContaPagar.contacorrente ");
    	sql.append(" left join contaPagarControleRemessaContaPagar on contaPagarControleRemessaContaPagar.ControleRemessaContaPagar = ControleRemessaContaPagar.codigo ");
    	sql.append(" left join ContaPagar on ContaPagar.codigo = contaPagarControleRemessaContaPagar.ContaPagar ");
    	sql.append(" left join fornecedor on fornecedor.codigo = contapagar.fornecedor ");
    	sql.append(" left join funcionario on funcionario.codigo = contapagar.funcionario  ");
    	sql.append(" left join parceiro on parceiro.codigo = contapagar.parceiro  ");
    	sql.append(" left join pessoa as pessoafuncionario on pessoafuncionario.codigo = funcionario.pessoa ");
    	sql.append(" left join pessoa as responsavelFinanceiro on responsavelFinanceiro.codigo = contapagar.responsavelFinanceiro ");
    	sql.append(" left join pessoa on pessoa.codigo = contapagar.pessoa ");
    	//sql.append(" left join banco on banco.codigo = contapagar.banco ");    	
    	sql.append(" where (upper(sem_acentos(fornecedor.nome)) like(sem_acentos('");    	sql.append(valorConsulta.toUpperCase());
    	sql.append("%'))");
    	sql.append(" or upper(sem_acentos(pessoafuncionario.nome)) like(sem_acentos('");
    	sql.append(valorConsulta.toUpperCase());
    	sql.append("%')) or upper(sem_acentos(parceiro.nome)) like(sem_acentos('");
    	sql.append(valorConsulta.toUpperCase());
    	sql.append("%')) or upper(sem_acentos(banco.nome)) like(sem_acentos('");
    	sql.append(valorConsulta.toUpperCase());
    	sql.append("%')) or upper(sem_acentos(pessoa.nome)) like(sem_acentos('");
    	sql.append(valorConsulta.toUpperCase());
    	sql.append("%')) ");
    	sql.append(" or upper(sem_acentos(responsavelFinanceiro.nome)) like(sem_acentos('");
    	sql.append(valorConsulta.toUpperCase());
    	sql.append("%')))");
    	if (situacao.getValor() != "" ) {
    		sql.append(" AND ControleRemessa.situacao = '" + situacao.getValor() + "' ");
    	}
    	sql.append(" AND ((dataGeracao >= '" + Uteis.getDataJDBC(dataIni) + "') and (dataGeracao <= '"+ Uteis.getDataJDBC(dataFim) + "')) ORDER BY dataGeracao");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }    

    public Integer consultarPorNrBanco(String valorConsulta) throws Exception {    	
    	String sqlStr = "SELECT count(codigo) as qtd FROM ControleRemessaContaPagar where banco = " + valorConsulta + " ";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
        	return tabelaResultado.getInt("qtd");
        } else {
        	return 0;
        }
    }

   /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>ControleRemessaContaPagarVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>ControleRemessaContaPagarVO</code>.
     *
     * @return O objeto da classe <code>ControleRemessaContaPagarVO</code> com os dados devidamente montados.
     */
    public static ControleRemessaContaPagarVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleRemessaContaPagarVO obj = new ControleRemessaContaPagarVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
        obj.setDataGeracao(dadosSQL.getDate("dataGeracao"));
        obj.setTipoRemessa(dadosSQL.getString("tipoRemessa"));		
        obj.getArquivoRemessaContaPagar().setCodigo(dadosSQL.getInt("arquivoRemessaContaPagar"));
        obj.getBancoVO().setCodigo(dadosSQL.getInt("banco"));
        obj.getBancoVO().setNome(dadosSQL.getString("nomeBanco"));
        obj.setDataInicio(dadosSQL.getDate("dataInicio"));
        obj.setDataFim(dadosSQL.getDate("dataFim"));
        obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino"));
        obj.getContaCorrenteVO().setCodigo(dadosSQL.getInt("contaCorrente"));
        obj.getContaCorrenteVO().setNumero(dadosSQL.getString("numeroContaCorrente"));
        obj.getContaCorrenteVO().setDigito(dadosSQL.getString("digitoContaCorrente"));
        obj.setSituacaoControleRemessa(SituacaoControleRemessaEnum.getEnum(dadosSQL.getString("situacao")));
        obj.getArquivoRemessaPixContaPagar().setCodigo(dadosSQL.getInt("arquivoRemessaPixContaPagar"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        montarDadosArquivo(obj, nivelMontarDados, usuario);
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        return obj;
    }

    public static void montarDadosArquivo(ControleRemessaContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getArquivoRemessaContaPagar().getCodigo().intValue() == 0 && obj.getArquivoRemessaPixContaPagar().getCodigo().intValue() == 0) { 
           obj.setArquivoRemessaContaPagar(new ArquivoVO());
           obj.setArquivoRemessaPixContaPagar(new ArquivoVO());       
            return;
        }        
		if (Uteis.isAtributoPreenchido(obj.getArquivoRemessaContaPagar().getCodigo())) {
			obj.setArquivoRemessaContaPagar(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(
					obj.getArquivoRemessaContaPagar().getCodigo(), nivelMontarDados, usuario));
		} else {
			obj.setArquivoRemessaContaPagar(new ArquivoVO());
		}
		if (Uteis.isAtributoPreenchido(obj.getArquivoRemessaPixContaPagar().getCodigo())) {
			obj.setArquivoRemessaPixContaPagar(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(
					obj.getArquivoRemessaPixContaPagar().getCodigo(), nivelMontarDados, usuario));
		} else {
			obj.setArquivoRemessaPixContaPagar(new ArquivoVO());
		}      
       
    }

    public static void montarDadosResponsavel(ControleRemessaContaPagarVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados,
                usuario));
    }

    public List consultarPorDataGeracao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ControleRemessaContaPagar.*, banco.nome as nomeBanco, contacorrente.numero as numeroContaCorrente, contacorrente.digito as digitoContaCorrente FROM ControleRemessaContaPagar "
        		+ "inner join banco on banco.codigo = controleremessaContaPagar.banco "
        		+ "inner join contacorrente on contacorrente.codigo = controleremessaContaPagar.contacorrente "
        		+ "WHERE ((dataGeracao >= '" + Uteis.getDataJDBC(prmIni) + "') and (dataGeracao <= '"
                + Uteis.getDataJDBC(prmFim) + "')) ORDER BY dataGeracao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /*
     * (non-Javadoc)
     *
     * @see negocio.facade.jdbc.financeiro.ControleRemessaInterfaceFacade#consultarPorChavePrimaria(java.lang.Integer,
     * boolean, int)
     */
    public ControleRemessaContaPagarVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        String sql = "SELECT ControleRemessaContaPagar.*, banco.nome as nomeBanco, contacorrente.numero as numeroContaCorrente, contacorrente.digito as digitoContaCorrente  FROM ControleRemessaContaPagar "
            		+ "inner join banco on banco.codigo = controleremessaContaPagar.banco "
            		+ "inner join contacorrente on contacorrente.codigo = ControleRemessaContaPagar.contacorrente "
            		+ " WHERE ControleRemessaContaPagar.codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ControleRemessaContaPagar ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ControleRemessaContaPagar.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        ControleRemessaContaPagar.idEntidade = idEntidade;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private File executarCriacaoArquivoTexto(String caminhoPasta, String nomeArquivo) throws Exception {
		arquivoHelper.criarCaminhoPastaDeInclusaoArquivo(caminhoPasta);
        setPrintWriter(arquivoHelper.criarArquivoTexto(caminhoPasta, nomeArquivo, true));
        return new File(caminhoPasta + File.separator + nomeArquivo);
    }

    public void setPrintWriter(PrintWriter printWriter) {
        this.printWriter = printWriter;
    }

    public PrintWriter getPrintWriter() {
        return printWriter;
    }
    
    public EditorOC executarGeracaoDadosArquivoRemessa(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs, ControleRemessaContaPagarVO controleRemessaContaPagarVO, String caminhoBaseArquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
    	EditorOC editorOC = null;
		editorOC = (EditorOC) getLayout(controleRemessaContaPagarVO, usuarioVO).executarGeracaoDadosArquivoRemessa(listaDadosRemessaVOs, controleRemessaContaPagarVO, configuracaoFinanceiroVO, usuarioVO);
    	return editorOC;
    }

    public ControleRemessaContaPagarLayoutInterfaceFacade getLayout(ControleRemessaContaPagarVO controleRemessaVO, UsuarioVO usuario) throws Exception {    	
    	ControleRemessaContaPagarLayoutInterfaceFacade layout = (ControleRemessaContaPagarLayoutInterfaceFacade) BancoFactory.getLayoutInstanciaControleRemessaContaPagar(controleRemessaVO.getBancoVO().getNrBanco(),controleRemessaVO.getContaCorrenteVO().getCnab());
        return layout;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarGeracaoArquivoRemessaContaPagar(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs,  Boolean agruparContasMesmoFornecedor ,
    		ControleRemessaContaPagarVO controleRemessaContaPagarVO, 
    		String caminhoBaseArquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, 
    		ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, 
    		UsuarioVO usuarioVO, 
    		ContaPagarVO contaPagarVO) throws Exception {    	
       
        controleRemessaContaPagarVO.setBancoVO(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(controleRemessaContaPagarVO.getBancoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
        controleRemessaContaPagarVO.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(controleRemessaContaPagarVO.getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioVO));
            	
        
        UnidadeEnsinoVO unidPadraoGeracaoRemessa = new UnidadeEnsinoVO();
        if (controleRemessaContaPagarVO.getContaCorrenteVO().getUnidadeEnsinoContaCorrenteVOs().isEmpty()) {
        	throw new Exception("Não é possível gerar a remessa da conta corrente informada! No cadastro da conta corrente deve existir pelo menos uma unidade de ensino marcada como UTILIZAR CONTROLE REMESSA.");
        } else {            	
        	for (UnidadeEnsinoContaCorrenteVO uni : controleRemessaContaPagarVO.getContaCorrenteVO().getUnidadeEnsinoContaCorrenteVOs() ){
        		if (uni.getUtilizarRemessa().booleanValue()	) {
        			unidPadraoGeracaoRemessa.setCodigo(uni.getUnidadeEnsino().getCodigo());
        		}
        	}
        }       

        if(!Uteis.isAtributoPreenchido(unidPadraoGeracaoRemessa)){
        	throw new Exception("Não é possível gerar a remessa da conta corrente informada! No cadastro da conta corrente deve existir pelo menos uma unidade de ensino marcada como UTILIZAR CONTROLE REMESSA.");
        } else {
        	controleRemessaContaPagarVO.setUnidadeEnsinoVO(unidPadraoGeracaoRemessa);
        }
        controleRemessaContaPagarVO.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(controleRemessaContaPagarVO.getUnidadeEnsinoVO().getCodigo(), false, usuarioVO));        
       
        if(listaDadosRemessaVOs.isEmpty()) {
        	throw new Exception("Não foi possível gerar o arquivo de remessa do banco selecionado! Não Existe dados para serem gerados !");

    	}
    	if(!Uteis.isAtributoPreenchido(controleRemessaContaPagarVO.getCodigo())) {    		
          incluir(controleRemessaContaPagarVO,  usuarioVO);
    	}    	
    	Map<String ,List<ContaPagarControleRemessaContaPagarVO>>  mapaContasPixDocTed = realizarSepararContasModalidadeTransferenciaBancariaPix(listaDadosRemessaVOs);
    	for(String chaves :  mapaContasPixDocTed.keySet()) {     		
    		if(chaves.equals(ModalidadeTransferenciaBancariaEnum.PIX.name())) {
    			if(!mapaContasPixDocTed.get(chaves).isEmpty()) {    				
    				executarGeracaoArquivoRemessaContaPagarModalidadeTransferenciaBancariaPix(mapaContasPixDocTed.get(chaves) , controleRemessaContaPagarVO , configuracaoFinanceiroVO , configuracaoGeralSistemaVO ,caminhoBaseArquivo ,agruparContasMesmoFornecedor ,usuarioVO);
    			}
    		}else {
    			if(!mapaContasPixDocTed.get(chaves).isEmpty()) {
    			   executarGeracaoArquivoRemessaContaPagarModalidadeTransferenciaBancaria(mapaContasPixDocTed.get(chaves) , controleRemessaContaPagarVO , configuracaoFinanceiroVO , configuracaoGeralSistemaVO ,caminhoBaseArquivo ,agruparContasMesmoFornecedor ,usuarioVO);
    			}
    		}
    		
    		
    	}
        for (ContaPagarControleRemessaContaPagarVO obj : listaDadosRemessaVOs) {
        	if (obj.getApresentarArquivoRemessa()) {        		
        		if(!Uteis.isAtributoPreenchido(obj.getCodigo())) {        			
        			getFacadeFactory().getContaPagarControleRemessaContaPagarFacade().incluir(obj, controleRemessaContaPagarVO.getCodigo(), usuarioVO);
        		}
        	}
        }
    }

    
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void executarGeracaoArquivoRemessaContaPagarModalidadeTransferenciaBancaria(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs, ControleRemessaContaPagarVO controleRemessaContaPagarVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String caminhoBaseArquivo, Boolean agruparContasMesmoFornecedor, UsuarioVO usuarioVO) throws Exception {
    	    	
    	 String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
         int character = (int) (Math.random() * 26);
         String letter = alphabet.substring(character, character + 1);
         String nomeArquivo = "";
     	 nomeArquivo = "PG" + Uteis.getData(new Date(), "ddMM") + Math.round(Math.random() * 10) + letter + ".REM";
         executarCriacaoArquivoTexto(caminhoBaseArquivo + File.separator + PastaBaseArquivoEnum.REMESSA_PG.getValue(), nomeArquivo);
     	
     	 EditorOC editorOC = null; 
     	 List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOsClone = null;
         if(agruparContasMesmoFornecedor) {
         	  listaDadosRemessaVOsClone  = new ArrayList<ContaPagarControleRemessaContaPagarVO>(listaDadosRemessaVOs);        	  
               realizarAgrupamentoValorEUnificarContasAgrupadasParaRemessa(listaDadosRemessaVOsClone,controleRemessaContaPagarVO.getCodigo());        	
         	  editorOC = executarGeracaoDadosArquivoRemessa(listaDadosRemessaVOsClone, controleRemessaContaPagarVO, caminhoBaseArquivo, configuracaoFinanceiroVO, configuracaoGeralSistemaVO, usuarioVO);
              
         }else {
           editorOC = executarGeracaoDadosArquivoRemessa(listaDadosRemessaVOs, controleRemessaContaPagarVO, caminhoBaseArquivo, configuracaoFinanceiroVO, configuracaoGeralSistemaVO, usuarioVO);
         }
         if (editorOC == null) {
         	listaDadosRemessaVOs.clear();
         	listaDadosRemessaVOsClone.clear();
         	throw new Exception("Não foi possível gerar o arquivo de remessa do banco selecionado! Verifique se a geração para esse banco está implementada!");
         }
         getPrintWriter().print(editorOC.getText());
         getPrintWriter().close();

         controleRemessaContaPagarVO.getArquivoRemessaContaPagar().setNome(nomeArquivo);
         controleRemessaContaPagarVO.getArquivoRemessaContaPagar().setOrigem(OrigemArquivo.REMESSA_PG.getValor());
         controleRemessaContaPagarVO.getArquivoRemessaContaPagar().setPastaBaseArquivo(PastaBaseArquivoEnum.REMESSA_PG.getValue());
         controleRemessaContaPagarVO.getArquivoRemessaContaPagar().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.REMESSA_PG);

         getFacadeFactory().getArquivoFacade().incluir(controleRemessaContaPagarVO.getArquivoRemessaContaPagar(), usuarioVO, configuracaoGeralSistemaVO);
         alterarArquivo(controleRemessaContaPagarVO, controleRemessaContaPagarVO.getArquivoRemessaContaPagar() , new ArquivoVO(), usuarioVO);
		
	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void executarGeracaoArquivoRemessaContaPagarModalidadeTransferenciaBancariaPix(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs, ControleRemessaContaPagarVO controleRemessaContaPagarVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, String caminhoBaseArquivo, Boolean agruparContasMesmoFornecedor, UsuarioVO usuarioVO) throws Exception {
		
		    String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	        int character = (int) (Math.random() * 26);
	        String letter = alphabet.substring(character, character + 1);
	        String nomeArquivo = "";
	    	nomeArquivo = "PG_PIX" + Uteis.getData(new Date(), "ddMM") + Math.round(Math.random() * 10) + letter + ".REM";
	        executarCriacaoArquivoTexto(caminhoBaseArquivo + File.separator + PastaBaseArquivoEnum.REMESSA_PG.getValue(), nomeArquivo);
	    	
	    	EditorOC editorOC = null; 
	    	 List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOsClone = null;
	        if(agruparContasMesmoFornecedor) {
	        	  listaDadosRemessaVOsClone  = new ArrayList<ContaPagarControleRemessaContaPagarVO>(listaDadosRemessaVOs);        	  
	              realizarAgrupamentoValorEUnificarContasAgrupadasParaRemessa(listaDadosRemessaVOsClone,controleRemessaContaPagarVO.getCodigo());        	
	        	  editorOC = executarGeracaoDadosArquivoRemessa(listaDadosRemessaVOsClone, controleRemessaContaPagarVO, caminhoBaseArquivo, configuracaoFinanceiroVO, configuracaoGeralSistemaVO, usuarioVO);
	             
	        }else {
	          editorOC = executarGeracaoDadosArquivoRemessa(listaDadosRemessaVOs, controleRemessaContaPagarVO, caminhoBaseArquivo, configuracaoFinanceiroVO, configuracaoGeralSistemaVO, usuarioVO);
	        }
	        if (editorOC == null) {
	        	listaDadosRemessaVOs.clear();
	        	listaDadosRemessaVOsClone.clear();
	        	throw new Exception("Não foi possível gerar o arquivo de remessa do banco selecionado! Verifique se a geração para esse banco está implementada!");
	        }
	        getPrintWriter().print(editorOC.getText());
	        getPrintWriter().close();

	        controleRemessaContaPagarVO.getArquivoRemessaPixContaPagar().setNome(nomeArquivo);
	        controleRemessaContaPagarVO.getArquivoRemessaPixContaPagar().setOrigem(OrigemArquivo.REMESSA_PG.getValor());
	        controleRemessaContaPagarVO.getArquivoRemessaPixContaPagar().setPastaBaseArquivo(PastaBaseArquivoEnum.REMESSA_PG.getValue());
	        controleRemessaContaPagarVO.getArquivoRemessaPixContaPagar().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.REMESSA_PG);

	        getFacadeFactory().getArquivoFacade().incluir(controleRemessaContaPagarVO.getArquivoRemessaPixContaPagar(), usuarioVO, configuracaoGeralSistemaVO);
	        alterarArquivo(controleRemessaContaPagarVO, new ArquivoVO(), controleRemessaContaPagarVO.getArquivoRemessaPixContaPagar(), usuarioVO);
	       
		
	}
    
	@Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public void addContaPagarControleRemessaContaPagarVO(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs, List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaSemBancoVOs, List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaOutroBancoVOs,  ContaPagarControleRemessaContaPagarVO obj){
    	int index = 0;
    	obj.getContaPagar().setBancoRemessaPagar(obj.getBancoRemessaPagar());
    	obj.getContaPagar().setBancoRecebimento(obj.getBancoRecebimento());
    	obj.getContaPagar().setNumeroAgenciaRecebimento(obj.getNumeroAgenciaRecebimento());
    	obj.getContaPagar().setContaCorrenteRecebimento(obj.getContaCorrenteRecebimento());
    	obj.getContaPagar().setDigitoAgenciaRecebimento(obj.getDigitoAgenciaRecebimento());
    	obj.getContaPagar().setDigitoCorrenteRecebimento(obj.getDigitoCorrenteRecebimento());
    	obj.getContaPagar().setNrDocumento(obj.getNrDocumento());
    	
    	obj.getContaPagar().setTipoServicoContaPagar(obj.getTipoServicoContaPagar());
    	obj.getContaPagar().setTipoLancamentoContaPagar(obj.getTipoLancamentoContaPagar());
    	obj.getContaPagar().setModalidadeTransferenciaBancariaEnum(obj.getModalidadeTransferenciaBancariaEnum());
    	obj.getContaPagar().setFinalidadeDocEnum(obj.getFinalidadeDocEnum());	
    	obj.getContaPagar().setFinalidadeTedEnum(obj.getFinalidadeTedEnum());	
    	
    	
    	obj.getContaPagar().setTipoIdentificacaoContribuinte(obj.getTipoIdentificacaoContribuinte());
    	obj.getContaPagar().setCodigoReceitaTributo(obj.getCodigoReceitaTributo());
    	obj.getContaPagar().setIdentificacaoContribuinte(obj.getIdentificacaoContribuinte());
    	obj.getContaPagar().setNumeroReferencia(obj.getNumeroReferencia());
    	obj.getContaPagar().setValorReceitaBrutaAcumulada(obj.getValorReceitaBrutaAcumulada());
    	obj.getContaPagar().setPercentualReceitaBrutaAcumulada(obj.getPercentualReceitaBrutaAcumulada());
    	
    	obj.setValor(obj.getContaPagar().getValor());
    	obj.setLinhaDigitavel1(obj.getContaPagar().getLinhaDigitavel1());
    	obj.setLinhaDigitavel2(obj.getContaPagar().getLinhaDigitavel2());
    	obj.setLinhaDigitavel3(obj.getContaPagar().getLinhaDigitavel3());
    	obj.setLinhaDigitavel4(obj.getContaPagar().getLinhaDigitavel4());
    	obj.setLinhaDigitavel5(obj.getContaPagar().getLinhaDigitavel5());
    	obj.setLinhaDigitavel6(obj.getContaPagar().getLinhaDigitavel6());
    	obj.setLinhaDigitavel7(obj.getContaPagar().getLinhaDigitavel7());
    	obj.setLinhaDigitavel8(obj.getContaPagar().getLinhaDigitavel8());    	
    	obj.setCodigoBarra(obj.getContaPagar().getCodigoBarra());
    	
    	boolean encontrou = false;
    	for (ContaPagarControleRemessaContaPagarVO objExistente : listaDadosRemessaVOs) {
    		if(objExistente.getContaPagar().getCodigo().equals(obj.getContaPagar().getCodigo())){
    			listaDadosRemessaVOs.set(index, obj);
    			encontrou = true;
    			return;
    		}
			index++;
		}
    	if (!encontrou) {
    		listaDadosRemessaVOs.add(obj);
    	}
    	for (ContaPagarControleRemessaContaPagarVO objExistente : listaDadosRemessaOutroBancoVOs) {
    		if(objExistente.getContaPagar().getCodigo().equals(obj.getContaPagar().getCodigo())){
    			listaDadosRemessaOutroBancoVOs.remove(objExistente);
    			break;
    		}
		}
    	for (ContaPagarControleRemessaContaPagarVO objExistente : listaDadosRemessaSemBancoVOs) {
    		if(objExistente.getContaPagar().getCodigo().equals(obj.getContaPagar().getCodigo())){
    			listaDadosRemessaSemBancoVOs.remove(objExistente);
    			break;
    		}
    	}
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)  
	public void realizarAgrupamentoContasPagar(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs) {
    	 
    	 List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOsClone = new ArrayList<ContaPagarControleRemessaContaPagarVO>(listaDadosRemessaVOs);    	
    	 Map<String, List<ContaPagarControleRemessaContaPagarVO>> map = new HashMap<String, List<ContaPagarControleRemessaContaPagarVO>>();
    	 List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOsApresentarArquivoRemessaDesmarcado = new ArrayList<ContaPagarControleRemessaContaPagarVO>(0);   
    	 String primeiraLetra = "A" ;
    	 String segundaLetra = "A" ;
    	 String ultimaLetra = "A" ;
    	 String[] codigoAgrupamento = {primeiraLetra ,segundaLetra ,ultimaLetra};
    	 
    	 // separa as contas que estao  desmarcado apresentarRemessa .
    	 Iterator listaIterator =  listaDadosRemessaVOsClone.iterator();
    	 while(listaIterator.hasNext()) {
    		 ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO = (ContaPagarControleRemessaContaPagarVO) listaIterator.next();
    		 if(!contaPagarControleRemessaContaPagarVO.getApresentarArquivoRemessa()) {
    			 listaDadosRemessaVOsApresentarArquivoRemessaDesmarcado.add(contaPagarControleRemessaContaPagarVO);
    			 listaIterator.remove();
    			 
    		 }
    	 }
    	
    	
    	 int contador = 0 ;
    	for(ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO : listaDadosRemessaVOs) {
    		ContaPagarVO contaPagar = contaPagarControleRemessaContaPagarVO.getContaPagar();    		
    		List<ContaPagarControleRemessaContaPagarVO> listaRemessa =  realizarValidacaoRegrasAgrupamento(contaPagar,listaDadosRemessaVOsClone);
    		if(Uteis.isAtributoPreenchido(listaRemessa)) {				
    			if(contador == 0 ) {
    				 String  codigoAgrupamentoTexto = "";
					for (int i = 0; i < codigoAgrupamento.length; i++) {
						codigoAgrupamentoTexto = codigoAgrupamentoTexto + codigoAgrupamento[i];
					}
					map.put(codigoAgrupamentoTexto, listaRemessa);
    			}else {    				 
    				codigoAgrupamento = Uteis.realizarCriacaoCodigoAgrupamento(codigoAgrupamento);
    				 String  codigoAgrupamentoTexto = "";
    				for (int i = 0; i < codigoAgrupamento.length; i++) {
   					 codigoAgrupamentoTexto   = codigoAgrupamentoTexto + codigoAgrupamento[i];
   				    }
      		       map.put(codigoAgrupamentoTexto, listaRemessa);
    			}
    			contador ++ ;
    		}
    		  
    	}
    	listaDadosRemessaVOs.clear();
    	for(String chaves :  map.keySet()) {    		
    	   List<ContaPagarControleRemessaContaPagarVO> listaRemessa  = map.get(chaves);    		
    		for(ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO : listaRemessa) {
    			if(contaPagarControleRemessaContaPagarVO.getApresentarArquivoRemessa()) {
    				contaPagarControleRemessaContaPagarVO.setCodigoAgrupamentoContasPagar(chaves);
        			//contaPagarControleRemessaContaPagarVO.setCodigoOrigemAgrupamentoContasPagar(chaves);
        			listaDadosRemessaVOs.add(contaPagarControleRemessaContaPagarVO);
        		}
    			
    		
    		}
    		
    	}
    	
    	Ordenacao.ordenarLista(listaDadosRemessaVOs, "codigoAgrupamentoContasPagar");
    	listaDadosRemessaVOs.addAll(listaDadosRemessaVOsApresentarArquivoRemessaDesmarcado);
    	 
	}
    
    private List<ContaPagarControleRemessaContaPagarVO> realizarValidacaoRegrasAgrupamento(ContaPagarVO contaPagar ,List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs) {
    	List<ContaPagarControleRemessaContaPagarVO>  listaRemessaContaPagarVO = new ArrayList<ContaPagarControleRemessaContaPagarVO>(0);    	
    
    	Iterator<ContaPagarControleRemessaContaPagarVO> i = listaDadosRemessaVOs.iterator();
    	while (i.hasNext()) {
    		ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO2 = i.next();
    		
    		
    		ContaPagarVO contaPagar2 = contaPagarControleRemessaContaPagarVO2.getContaPagar();
		    boolean tipoSacadoIgual = Boolean.FALSE;			    
		   
		    if(contaPagar.isTipoSacadoFornecedor()  && contaPagar.getFornecedor().getCodigo().equals(contaPagar2.getFornecedor().getCodigo())) {
		    	tipoSacadoIgual = Boolean.TRUE;
		    }else if(contaPagar.isTipoSacadoFuncionario() &&  contaPagar.getFuncionario().getCodigo().equals(contaPagar2.getFuncionario().getCodigo())) {
		    	tipoSacadoIgual = Boolean.TRUE;
		    }else if(contaPagar.isTipoSacadoResponsavelFinanceiro() && contaPagar.getResponsavelFinanceiro().getCodigo().equals(contaPagar2.getResponsavelFinanceiro().getCodigo())) {
		    	tipoSacadoIgual = Boolean.TRUE;
			}else if(contaPagar.isTipoSacadoParceiro() && contaPagar.getParceiro().getCodigo().equals(contaPagar2.getParceiro().getCodigo())) {
				tipoSacadoIgual = Boolean.TRUE;
			}else if(contaPagar.isTipoSacadoAluno() && contaPagar.getPessoa().getCodigo().equals(contaPagar2.getPessoa().getCodigo())) {
				tipoSacadoIgual = Boolean.TRUE;
			}else if(contaPagar.isTipoSacadoBanco() &&  contaPagar.getBanco().equals(contaPagar2.getBanco())){
				tipoSacadoIgual = Boolean.TRUE;
			}
		    if (tipoSacadoIgual && contaPagar.getFormaPagamentoVO().getCodigo().equals(contaPagar2.getFormaPagamentoVO().getCodigo()) && contaPagar.getDataVencimento().equals(contaPagar2.getDataVencimento())) {
		    	if (contaPagar.getFormaPagamentoVO().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.BOLETO_BANCARIO)) {
		    		if (contaPagar.getCodigoBarra().equals(contaPagar2.getCodigoBarra())) {
		    			contaPagarControleRemessaContaPagarVO2.setObservacaoAgrupamento("Esta conta foi agrupada  . " );
		    			listaRemessaContaPagarVO.add(contaPagarControleRemessaContaPagarVO2);
		    			i.remove();
		    		}
		    	} else if (contaPagar.isApresentarDadosContaBancaria()
		    			&& (contaPagar.getBancoRecebimento().getCodigo().equals(contaPagar2.getBancoRecebimento().getCodigo()))
		    			&& (contaPagar.getNumeroAgenciaRecebimento().equals(contaPagar2.getNumeroAgenciaRecebimento()))
		    			&& (contaPagar.getDigitoAgenciaRecebimento().equals(contaPagar2.getDigitoAgenciaRecebimento()))
		    			&& (contaPagar.getContaCorrenteRecebimento().equals(contaPagar2.getContaCorrenteRecebimento()))
		    			&& (contaPagar.getDigitoCorrenteRecebimento().equals(contaPagar2.getDigitoCorrenteRecebimento()))
		    			&& (Uteis.isAtributoPreenchido(contaPagar.getTipoLancamentoContaPagar()) && 
		    					Uteis.isAtributoPreenchido(contaPagar2.getTipoLancamentoContaPagar()) 
		    					&& contaPagar.getTipoLancamentoContaPagar().equals(contaPagar2.getTipoLancamentoContaPagar())
		    					&& !contaPagar.getTipoLancamentoContaPagar().isTransferencia() ? true : 
		    				contaPagar.getModalidadeTransferenciaBancariaEnum() != null && contaPagar.getModalidadeTransferenciaBancariaEnum().equals(contaPagar2.getModalidadeTransferenciaBancariaEnum())
		    				&& (contaPagar.getTipoContaEnum() == null || (contaPagar.getTipoContaEnum() != null && contaPagar.getTipoContaEnum().equals(contaPagar2.getTipoContaEnum()))))) {
		    		contaPagarControleRemessaContaPagarVO2.setObservacaoAgrupamento("Esta conta foi agrupada  . " );
		    		listaRemessaContaPagarVO.add(contaPagarControleRemessaContaPagarVO2);
		    		i.remove();
		    	}
		    }
    	}    	
    		
    	return listaRemessaContaPagarVO;
    }
    
  
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)  
	public void  realizarDesagruparContasPagar(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs) {
    	for(ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO :listaDadosRemessaVOs) {
    		contaPagarControleRemessaContaPagarVO.setCodigoAgrupamentoContasPagar(null);    	
    	}
    	
    	
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)  
	public void  realizarRemoverAgrupamentoContasPagar(ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO , List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs) {
    	    try {
    	    	    contaPagarControleRemessaContaPagarVO.setCodigoAgrupamentoContasPagar(null);   
    	    	    Map<String, List<ContaPagarControleRemessaContaPagarVO>> mapa = realizarSepararContaPagarAgrupadaNaoAgrupada(listaDadosRemessaVOs);
    				List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOAgrupada = mapa.get("listaAgrupada");
    				List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOSemAgrupador = mapa.get("listaSemAgrupar");
    				listaDadosRemessaVOs.clear();
    				listaDadosRemessaVOs.addAll(listaDadosRemessaVOAgrupada);
    				Ordenacao.ordenarLista(listaDadosRemessaVOs, "dataVencimento");
    				realizarAgrupamentoContasPagar(listaDadosRemessaVOs);			
    				
    				
    			
    				
    				Ordenacao.ordenarLista(listaDadosRemessaVOs, "codigoAgrupamentoContasPagar");
    				listaDadosRemessaVOs.addAll(listaDadosRemessaVOSemAgrupador);
        	          	 
			} catch (Exception e) {
				throw e;
			}
    	        	
    }
    
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)  
	public List<ContaPagarControleRemessaContaPagarVO>  realizarVizualizarContasMesmoAgrupador(String codigoAgrupador , List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOsContasAgrupadas) {
    	 List<ContaPagarControleRemessaContaPagarVO> lista =  listaDadosRemessaVOsContasAgrupadas.stream().filter(p -> p.getCodigoAgrupamentoContasPagar().equals(codigoAgrupador)).collect(Collectors.toList());
         return lista ;
    }
    
    
	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
	public void realizarAdicionarAgrupamentoContasPagar(ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO,List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs) {
		try {
			listaDadosRemessaVOs.remove(contaPagarControleRemessaContaPagarVO);
			Map<String, List<ContaPagarControleRemessaContaPagarVO>> mapa = realizarSepararContaPagarAgrupadaNaoAgrupada(listaDadosRemessaVOs);
			List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOAgrupada = mapa.get("listaAgrupada");
			List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOSemAgrupador = mapa.get("listaSemAgrupar");
			listaDadosRemessaVOs.clear();
			listaDadosRemessaVOs.addAll(listaDadosRemessaVOAgrupada);
			listaDadosRemessaVOs.add(contaPagarControleRemessaContaPagarVO);			
			Ordenacao.ordenarLista(listaDadosRemessaVOs, "dataVencimento");
			realizarAgrupamentoContasPagar(listaDadosRemessaVOs);	
			listaDadosRemessaVOs.addAll(listaDadosRemessaVOSemAgrupador);
		} catch (Exception e) {
			throw e;
		}

	}
    
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)  
    public Map<String, List<ContaPagarControleRemessaContaPagarVO>> realizarSepararContaPagarAgrupadaNaoAgrupada(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs) {
   	 Map<String, List<ContaPagarControleRemessaContaPagarVO>> map = new HashMap<String, List<ContaPagarControleRemessaContaPagarVO>>();
   	 List<ContaPagarControleRemessaContaPagarVO> listaAgrupada = new ArrayList<ContaPagarControleRemessaContaPagarVO>(0);
   	 List<ContaPagarControleRemessaContaPagarVO> listaSemAgrupar = new ArrayList<ContaPagarControleRemessaContaPagarVO>(0);
     for(ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO :listaDadosRemessaVOs) {
    	  if(Uteis.isAtributoPreenchido(contaPagarControleRemessaContaPagarVO.getCodigoAgrupamentoContasPagar())){
    		  listaAgrupada.add(contaPagarControleRemessaContaPagarVO);
    	  }else {
    		  listaSemAgrupar.add(contaPagarControleRemessaContaPagarVO) ;
    	  }
     }
     map.put("listaAgrupada", listaAgrupada) ;
     map.put("listaSemAgrupar", listaSemAgrupar) ;
   	 return map;
    }
    
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)  
    public Map<String, List<ContaPagarControleRemessaContaPagarVO>> realizarSepararListaContaPagarAgrupadas(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs) {
    	  Map<String, List<ContaPagarControleRemessaContaPagarVO>> map = new HashMap<String, List<ContaPagarControleRemessaContaPagarVO>>(0);
    	     List<ContaPagarControleRemessaContaPagarVO> listaA = new ArrayList<ContaPagarControleRemessaContaPagarVO>(listaDadosRemessaVOs);
    	   	 List<ContaPagarControleRemessaContaPagarVO> listaB = new ArrayList<ContaPagarControleRemessaContaPagarVO>(listaDadosRemessaVOs);
    	   	 
    	   	 Iterator<ContaPagarControleRemessaContaPagarVO> a = listaA.iterator();
    	   	while(a.hasNext()) {
	    	   	 String codigoAgrupador = "";
	    	   	 ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO = a.next();    
	 		     List<ContaPagarControleRemessaContaPagarVO> lista = new ArrayList<ContaPagarControleRemessaContaPagarVO>(0);
	 	         Iterator<ContaPagarControleRemessaContaPagarVO> i = listaB.iterator();	 	      
	 	         while(i.hasNext()) {	 	        	 
	 	        	  ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO2 = i.next();    
	 	        	  if(contaPagarControleRemessaContaPagarVO.getCodigoAgrupamentoContasPagar().equals(contaPagarControleRemessaContaPagarVO2.getCodigoAgrupamentoContasPagar())) {
	 	        		 lista.add(contaPagarControleRemessaContaPagarVO2);
	   				     codigoAgrupador =contaPagarControleRemessaContaPagarVO.getCodigoAgrupamentoContasPagar()  ;   	
	   				     i.remove();  				  
	   			      }
	 	        }
	 	        if(!lista.isEmpty()) {
	 	        	if(!Uteis.isAtributoPreenchido(codigoAgrupador)) {
	 	        		codigoAgrupador = "NAOPOSSUIAGRUPADOR";
	 	        	}
	 	        	map.put(codigoAgrupador, lista);	 	        	
	 	        }   	   		
    	   	}    	
      return map;
    }
    
    
    private void realizarAgrupamentoValorEUnificarContasAgrupadasParaRemessa(List<ContaPagarControleRemessaContaPagarVO> listaRemessa, Integer codigoControleRemessaContaPagar ){  	
    	   
    	    List<ContaPagarControleRemessaContaPagarVO> listaRemessaUnificadas = new ArrayList<ContaPagarControleRemessaContaPagarVO>(0);
    	    Map<String, List<ContaPagarControleRemessaContaPagarVO>> mapaRemessaSeparadaAgrupador = getFacadeFactory().getControleRemessaContaPagarFacade().realizarSepararListaContaPagarAgrupadas(listaRemessa);
	 		for(String chaves :  mapaRemessaSeparadaAgrupador.keySet()) { 
	    	   List<ContaPagarControleRemessaContaPagarVO> listaRemessaVO  = mapaRemessaSeparadaAgrupador.get(chaves);
	    	    ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVoUnificada  = null ; 
	    	    Double valor = 0.0;	    	   
	    	    Double descontos = 0.0 ;
	    	    Double multas = 0.0 ;
	    	    Double juros = 0.0 ;
	    	    String codigoTransmissao ="" ;
	    	    if(!chaves.equals("NAOPOSSUIAGRUPADOR")) {
	    	    	 codigoTransmissao =  realizarMontarCodigoTransmissaoRemessaContaPagar(4);
	    	    }
	    	   for(ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO :listaRemessaVO) {    		   
	    		    if(chaves.equals("NAOPOSSUIAGRUPADOR")) {				    	   
			    	   contaPagarControleRemessaContaPagarVO.setCodigoTransmissaoRemessanossonumero(realizarMontarCodigoTransmissaoRemessaContaPagar(4));
			    	   contaPagarControleRemessaContaPagarVoUnificada =   (ContaPagarControleRemessaContaPagarVO) Uteis.clonar(contaPagarControleRemessaContaPagarVO);  
			    	   listaRemessaUnificadas.add(contaPagarControleRemessaContaPagarVoUnificada) ;	
		    		 }else {
		    			 String codigoAgrupado =  contaPagarControleRemessaContaPagarVO.getCodigoAgrupamentoContasPagar();
	    			    codigoAgrupado =   codigoAgrupado.substring(codigoAgrupado.length() - 3  , codigoAgrupado.length());	    			   
	    			    contaPagarControleRemessaContaPagarVO.setCodigoAgrupamentoContasPagar(codigoControleRemessaContaPagar + codigoAgrupado);	    			    
	    			    contaPagarControleRemessaContaPagarVO.setCodigoTransmissaoRemessanossonumero(codigoTransmissao);
	    			    contaPagarControleRemessaContaPagarVoUnificada =   (ContaPagarControleRemessaContaPagarVO) Uteis.clonar(contaPagarControleRemessaContaPagarVO);
	    			    
	    			    valor =  valor +  contaPagarControleRemessaContaPagarVO.getValor();    	
		    		    descontos = descontos + contaPagarControleRemessaContaPagarVO.getDesconto();
		    		    multas = multas +  contaPagarControleRemessaContaPagarVO.getMulta();
		    		    juros = juros + contaPagarControleRemessaContaPagarVO.getJuro();	
	    			     
		    		 }
	    		   
	    	   }
	    	   if(!chaves.equals("NAOPOSSUIAGRUPADOR")) {
		    	    contaPagarControleRemessaContaPagarVoUnificada.setValor(Uteis.arrendondarForcando2CasasDecimais(valor));
		    	    contaPagarControleRemessaContaPagarVoUnificada.setDesconto(Uteis.arrendondarForcando2CasasDecimais(descontos));
		    	    contaPagarControleRemessaContaPagarVoUnificada.setJuro(Uteis.arrendondarForcando2CasasDecimais(juros));
		    	    contaPagarControleRemessaContaPagarVoUnificada.setMulta(Uteis.arrendondarForcando2CasasDecimais(multas));
		    	    listaRemessaUnificadas.add(contaPagarControleRemessaContaPagarVoUnificada)   ;
	    	   }  
 		     } 	 		
	 		listaRemessa.clear();
	 		listaRemessa.addAll(listaRemessaUnificadas);
	 		Ordenacao.ordenarLista(listaRemessa, "codigoAgrupamentoContasPagar"); 	
    }

	

	@Override
	public void executarGeracaoContaPagarRegistrosArquivoAgrupados(String  linha ,ControleCobrancaPagarVO obj,RegistroDetalhePagarVO registroDetalhe,RegistroHeaderLotePagarVO registroHeaderLote, UsuarioVO usuarioVO  ) throws Exception {
		List<ContaPagarControleRemessaContaPagarVO> listaRemessa = new ArrayList<ContaPagarControleRemessaContaPagarVO>(0);
		if(Uteis.isAtributoPreenchido(registroDetalhe.getNossoNumeroContaAgrupada()) ){			
			listaRemessa  = getFacadeFactory().getContaPagarControleRemessaContaPagarFacade().consultarPorCodigoAgrupamento(registroDetalhe.getNossoNumeroContaAgrupada(),usuarioVO );			
		}else {
			listaRemessa  = getFacadeFactory().getContaPagarControleRemessaContaPagarFacade().consultarPorCodigoTransmissaoNossoNumero(registroDetalhe.getNossoNumero().toString(),usuarioVO );
			if(listaRemessa.isEmpty()) {			
				listaRemessa = getFacadeFactory().getContaPagarControleRemessaContaPagarFacade().consultarPorNossoNumero(registroDetalhe.getNossoNumero().toString(), usuarioVO);			
			}
		 }
		   Double valorTotalContasAgrupadas = 0.0; 
		   Boolean contasAgrupadasComErroValorPagamento = Boolean.TRUE;
		   for (ContaPagarControleRemessaContaPagarVO contaRemessa : listaRemessa) {	
			   valorTotalContasAgrupadas = valorTotalContasAgrupadas + contaRemessa.getPrevisaoValorPagoDescontosMultas();
		   }
		   if(listaRemessa.isEmpty() ||  registroDetalhe.getValorPagamento().equals(Uteis.arrendondarForcando2CadasDecimais(valorTotalContasAgrupadas))){
			   contasAgrupadasComErroValorPagamento = Boolean.FALSE; 
		    }	
		  		   
			   for (ContaPagarControleRemessaContaPagarVO contaRemessa : listaRemessa) {			   
			    	RegistroDetalhePagarVO registroDetalheClone = (RegistroDetalhePagarVO) Uteis.clonar(registroDetalhe) ;
			    	registroDetalheClone.setNossoNumeroContaAgrupada(contaRemessa.getCodigoAgrupamentoContasPagar());
			      	registroDetalheClone.setRegistroHeaderLotePagarVO(registroHeaderLote);
			      	registroDetalheClone.setValorPagamento(contaRemessa.getPrevisaoValorPagoDescontosMultas());
			    	ContaPagarRegistroArquivoVO cpraAgrupada = new ContaPagarRegistroArquivoVO(contaRemessa.getContaPagar(), obj,registroDetalheClone ,contasAgrupadasComErroValorPagamento );			    	       
					if (Uteis.isAtributoPreenchido(cpraAgrupada.getContaPagarVO())) {
						cpraAgrupada.getContaPagarVO().setObservacao("Conta Localizada!");
						if (!cpraAgrupada.isContaPagarEfetivado()) {
							cpraAgrupada.getContaPagarVO().setObservacao(cpraAgrupada.getMotivoRejeicao_Apresentar());
						} else if (cpraAgrupada.getRegistroDetalhePagarVO().getValorPagamento().equals(0.0) || cpraAgrupada.getValorPagamentoDivergente()) {
							cpraAgrupada.getRegistroDetalhePagarVO().setValorPagamento(0.0);
							cpraAgrupada.getContaPagarVO().setObservacao("Conta com problemas no valor pago!");
						} else if (cpraAgrupada.getContaPagarVO().getQuitada()) {
							cpraAgrupada.getContaPagarVO().setObservacao("Conta já Paga!");
						}
					} else if (!cpraAgrupada.isContaPagarEfetivado()) {
						cpraAgrupada.getContaPagarVO().setObservacao(cpraAgrupada.getMotivoRejeicao_Apresentar());
					} else {
						cpraAgrupada.getContaPagarVO().setObservacao("Conta Não Localizada!");
					}
				    registroHeaderLote.getListaRegistroDetalhePagarVO().add(registroDetalheClone);
				    if(!linha.substring(17, 19).contains("52")) {
				    	obj.getContaPagarRegistroArquivoVOs().add(cpraAgrupada);
				   }
				    						
								
			 }		
	}

	@Override
	public void realizarValidarExistenciaContaComFormaPagamentoBoletoCodigoBarrasIgualSemAgrupador(List<ContaPagarControleRemessaContaPagarVO> listaRemessa) throws Exception {
		
			Map<String, List<ContaPagarControleRemessaContaPagarVO>> mapa = realizarSepararContaPagarAgrupadaNaoAgrupada(listaRemessa);
			List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOAgrupada = mapa.get("listaAgrupada");
			List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOSemAgrupador = mapa.get("listaSemAgrupar");
			if (!listaDadosRemessaVOSemAgrupador.isEmpty()) {				
				for(ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO : listaDadosRemessaVOSemAgrupador) {
					if(contaPagarControleRemessaContaPagarVO.getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.BOLETO_BANCARIO)) {						
						for(ContaPagarControleRemessaContaPagarVO contaPagarControleRemessaContaPagarVO2 : listaDadosRemessaVOAgrupada) {
							if(contaPagarControleRemessaContaPagarVO2.getContaPagar().getFormaPagamentoVO().getTipoFormaPagamentoEnum().equals(TipoFormaPagamento.BOLETO_BANCARIO)) {								
								if(contaPagarControleRemessaContaPagarVO.getContaPagar().getCodigoBarra().equals(contaPagarControleRemessaContaPagarVO2.getContaPagar().getCodigoBarra())){
									boolean tipoSacadoIgual = Boolean.FALSE;    
									  
								    if(contaPagarControleRemessaContaPagarVO.getContaPagar().isTipoSacadoFornecedor()  && contaPagarControleRemessaContaPagarVO.getContaPagar().getFornecedor().getCodigo().equals(contaPagarControleRemessaContaPagarVO2.getContaPagar().getFornecedor().getCodigo())) {
								    	tipoSacadoIgual = Boolean.TRUE;
								    }else if(contaPagarControleRemessaContaPagarVO.getContaPagar().isTipoSacadoFuncionario() &&  contaPagarControleRemessaContaPagarVO.getContaPagar().getFuncionario().getCodigo().equals(contaPagarControleRemessaContaPagarVO2.getContaPagar().getFuncionario().getCodigo())) {
								    	tipoSacadoIgual = Boolean.TRUE;
								    }else if(contaPagarControleRemessaContaPagarVO.getContaPagar().isTipoSacadoResponsavelFinanceiro() && contaPagarControleRemessaContaPagarVO.getContaPagar().getResponsavelFinanceiro().getCodigo().equals(contaPagarControleRemessaContaPagarVO2.getContaPagar().getResponsavelFinanceiro().getCodigo())) {
								    	tipoSacadoIgual = Boolean.TRUE;
									}else if(contaPagarControleRemessaContaPagarVO.getContaPagar().isTipoSacadoParceiro() && contaPagarControleRemessaContaPagarVO.getContaPagar().getParceiro().getCodigo().equals(contaPagarControleRemessaContaPagarVO2.getContaPagar().getParceiro().getCodigo())) {
										tipoSacadoIgual = Boolean.TRUE;
									}else if(contaPagarControleRemessaContaPagarVO.getContaPagar().isTipoSacadoAluno() && contaPagarControleRemessaContaPagarVO.getContaPagar().getPessoa().getCodigo().equals(contaPagarControleRemessaContaPagarVO2.getContaPagar().getPessoa().getCodigo())) {
										tipoSacadoIgual = Boolean.TRUE;
									}else if(contaPagarControleRemessaContaPagarVO.getContaPagar().isTipoSacadoBanco() &&  contaPagarControleRemessaContaPagarVO.getContaPagar().getBanco().equals(contaPagarControleRemessaContaPagarVO2.getContaPagar().getBanco())){
										tipoSacadoIgual = Boolean.TRUE;
									}
								    if(tipoSacadoIgual) {								    	
								    	throw new Exception("Não é possível gerar a remessa da conta corrente informada! Existem contas com  forma de pagamento BOLETO BANCARIO com codigo de barras iguais  desagrupadas  realize o agrupamento das contas  para gerar o arquivo .");
								    }
								}
							}
						}
					}					
				}				
			}		
		
	}
	
	
	
	
	@Override
    public String realizarMontarCodigoTransmissaoRemessaContaPagar(int qtd) {    	
    	SqlRowSet rs2 = getConexao().getJdbcTemplate().queryForRowSet(" select transmissaonossonumero_codigo::text ||  substring((anoremessa::text) from 3 for 4)  as transmissaosequencialnossonumero from inserircodigotransmissaonossonumero("+(qtd)+ ")" );
   		if (rs2.next()) {						
   			return  rs2.getString("transmissaosequencialnossonumero");			
		}
   		return "" ;   		
	}
	
	
	 @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	    private Map<String ,List<ContaPagarControleRemessaContaPagarVO>> realizarSepararContasModalidadeTransferenciaBancariaPix(List<ContaPagarControleRemessaContaPagarVO> listaDadosRemessaVOs) throws Exception {
			Map<String ,List<ContaPagarControleRemessaContaPagarVO>> map = new HashMap<String, List<ContaPagarControleRemessaContaPagarVO>>();
			List<ContaPagarControleRemessaContaPagarVO> listaPix = new ArrayList<ContaPagarControleRemessaContaPagarVO>();
			List<ContaPagarControleRemessaContaPagarVO> listaTedDoc = new ArrayList<ContaPagarControleRemessaContaPagarVO>();
			for(ContaPagarControleRemessaContaPagarVO  contaPagarControleRemessaContaPagarVO : listaDadosRemessaVOs) {
				if(Uteis.isAtributoPreenchido(contaPagarControleRemessaContaPagarVO.getModalidadeTransferenciaBancariaEnum()) && contaPagarControleRemessaContaPagarVO.getModalidadeTransferenciaBancariaEnum().isPix()) {
					listaPix.add(contaPagarControleRemessaContaPagarVO);
				}else {
					listaTedDoc.add(contaPagarControleRemessaContaPagarVO);
				}
			}		
			
			map.put(ModalidadeTransferenciaBancariaEnum.PIX.name(), listaPix);
			map.put(ModalidadeTransferenciaBancariaEnum.TED.name(), listaTedDoc);
		   
	       return map;
	    }

	
    
    
    
}