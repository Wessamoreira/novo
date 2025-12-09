package negocio.facade.jdbc.financeiro;

import java.io.File;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import negocio.comuns.academico.ArquivoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaContaReceberVO;
import negocio.comuns.financeiro.ControleRemessaVO;
import negocio.comuns.financeiro.RegistroDetalheVO;
import negocio.comuns.financeiro.enumerador.MotivoRejeicaoRemessa;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaContaReceberEnum;
import negocio.comuns.financeiro.enumerador.SituacaoControleRemessaEnum;
import negocio.comuns.utilitarias.ArquivoHelper;
import negocio.comuns.utilitarias.BancoFactory;
import negocio.comuns.utilitarias.Comando;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.EditorOC;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.Bancos;
import negocio.comuns.utilitarias.dominios.ModeloGeracaoBoletoSicoob;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.ControleRemessaInterfaceFacade;
import negocio.interfaces.financeiro.remessa.ControleRemessaCNAB240LayoutInterfaceFacade;
import negocio.interfaces.financeiro.remessa.ControleRemessaCNAB400LayoutInterfaceFacade;
import relatorio.negocio.comuns.financeiro.FiltroRelatorioFinanceiroVO;
import webservice.boletoonline.RegistroOnlineBoleto;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>ControleRemessaVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>ControleRemessaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ControleRemessaVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class ControleRemessa extends ControleAcesso implements ControleRemessaInterfaceFacade {

    private ArquivoHelper arquivoHelper = new ArquivoHelper();
    private PrintWriter printWriter;
    protected static String idEntidade;

    public ControleRemessa() throws Exception {
        super();
        setIdEntidade("ControleRemessa");
    }

    /*
     * (non-Javadoc)
     *
     * @see negocio.facade.jdbc.financeiro.ControleRemessaInterfaceFacade#novo()
     */
    public ControleRemessaVO novo() throws Exception {
        ControleRemessa.incluir(getIdEntidade());
        ControleRemessaVO obj = new ControleRemessaVO();
        return obj;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * negocio.facade.jdbc.financeiro.ControleRemessaInterfaceFacade#incluir(negocio.comuns.financeiro.ControleRemessaVO
     * )
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ControleRemessaVO obj, final FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiroVO, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "INSERT INTO ControleRemessa ( responsavel, dataGeracao, arquivoRemessa, dataInicio, dataFim, contaCorrente, unidadeEnsino, banco, tipoRemessa, situacao, "
            		+ "tipoOrigemInscricaoProcessoSeletivo, tipoOrigemMatricula, tipoOrigemRequerimento, tipoOrigemBiblioteca, tipoOrigemMensalidade, tipoOrigemDevolucaoCheque, tipoOrigemNegociacao, tipoOrigemBolsaCusteadaConvenio, tipoOrigemContratoReceita, tipoOrigemOutros, tipoOrigemInclusaoReposicao, remessaonline) "
                    + "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setDate(2, Uteis.getDataJDBC(obj.getDataGeracao()));
                    if (obj.getArquivoRemessa().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(3, obj.getArquivoRemessa().getCodigo().intValue());
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
                    if (filtroRelatorioFinanceiroVO != null) {
                        sqlInserir.setBoolean(11, filtroRelatorioFinanceiroVO.getTipoOrigemInscricaoProcessoSeletivo());
                    } else {
                        sqlInserir.setNull(11, 0);
                    }
                    if (filtroRelatorioFinanceiroVO != null) {
                    	sqlInserir.setBoolean(12, filtroRelatorioFinanceiroVO.getTipoOrigemMatricula());
                    } else {
                    	sqlInserir.setNull(12, 0);
                    }
                    if (filtroRelatorioFinanceiroVO != null) {
                    	sqlInserir.setBoolean(13, filtroRelatorioFinanceiroVO.getTipoOrigemRequerimento());
                    } else {
                    	sqlInserir.setNull(13, 0);
                    }
                    if (filtroRelatorioFinanceiroVO != null) {
                    	sqlInserir.setBoolean(14, filtroRelatorioFinanceiroVO.getTipoOrigemBiblioteca());
                    } else {
                    	sqlInserir.setNull(14, 0);
                    }
                    if (filtroRelatorioFinanceiroVO != null) {
                    	sqlInserir.setBoolean(15, filtroRelatorioFinanceiroVO.getTipoOrigemMensalidade());
                    } else {
                    	sqlInserir.setNull(15, 0);
                    }
                    if (filtroRelatorioFinanceiroVO != null) {
                    	sqlInserir.setBoolean(16, filtroRelatorioFinanceiroVO.getTipoOrigemDevolucaoCheque());
                    } else {
                    	sqlInserir.setNull(16, 0);
                    }
                    if (filtroRelatorioFinanceiroVO != null) {
                    	sqlInserir.setBoolean(17, filtroRelatorioFinanceiroVO.getTipoOrigemNegociacao());
                    } else {
                    	sqlInserir.setNull(17, 0);
                    }
                    if (filtroRelatorioFinanceiroVO != null) {
                    	sqlInserir.setBoolean(18, filtroRelatorioFinanceiroVO.getTipoOrigemBolsaCusteadaConvenio());
                    } else {
                    	sqlInserir.setNull(18, 0);
                    }
                    if (filtroRelatorioFinanceiroVO != null) {
                    	sqlInserir.setBoolean(19, filtroRelatorioFinanceiroVO.getTipoOrigemContratoReceita());
                    } else {
                    	sqlInserir.setNull(19, 0);
                    }
                    if (filtroRelatorioFinanceiroVO != null) {
                    	sqlInserir.setBoolean(20, filtroRelatorioFinanceiroVO.getTipoOrigemOutros());
                    } else {
                    	sqlInserir.setNull(20, 0);
                    }
                    if (filtroRelatorioFinanceiroVO != null) {
                    	sqlInserir.setBoolean(21, filtroRelatorioFinanceiroVO.getTipoOrigemInclusaoReposicao());
                    } else {
                    	sqlInserir.setNull(21, 0);
                    }
                    sqlInserir.setBoolean(22, obj.getRemessaOnline().booleanValue());
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
	public void incluirControleOnline(ContaReceberVO contaReceber, ControleRemessaContaReceberVO crcr, UsuarioVO usuarioVO) throws Exception {
		try {
	        String sqlStr = "SELECT codigo FROM ControleRemessa WHERE remessaonline = true and datageracao = current_date and contacorrente = " + contaReceber.getContaCorrenteVO().getCodigo() + " order by codigo desc limit 1";
	        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
			if (tabelaResultado.next()) {
				getFacadeFactory().getControleRemessaContaReceberFacade().incluir(crcr, tabelaResultado.getInt("codigo"), usuarioVO);
			} else {
				ControleRemessaVO cr = new ControleRemessaVO();
				cr.setResponsavel(usuarioVO);
				cr.setDataGeracao(new Date());
				cr.setDataInicio(new Date());
				cr.setDataFim(new Date());
				cr.setContaCorrenteVO(contaReceber.getContaCorrenteVO());
				cr.setUnidadeEnsinoVO(contaReceber.getUnidadeEnsino());
				cr.setBancoVO(contaReceber.getContaCorrenteVO().getAgencia().getBanco());
	            cr.setRemessaOnline(Boolean.TRUE);
				this.incluir(cr, null, usuarioVO);
				getFacadeFactory().getControleRemessaContaReceberFacade().incluir(crcr, cr.getCodigo(), usuarioVO);
			}
		} catch (Exception e) {
			throw e;
		}
	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)    
    public void alterarArquivo(ControleRemessaVO controleRemessaVO, ArquivoVO arquivoRemessaVO, UsuarioVO usuarioVO) throws Exception {
    	try {
    		ControleRemessa.alterar(getIdEntidade(), false, usuarioVO);
    		StringBuilder sqlStr = new StringBuilder();
    		controleRemessaVO.setArquivoRemessa(arquivoRemessaVO);
    		sqlStr.append("UPDATE ControleRemessa set arquivoRemessa = ").append(arquivoRemessaVO.getCodigo()).append(" WHERE (codigo = ").append(controleRemessaVO.getCodigo()).append(")");
    		getConexao().getJdbcTemplate().update(sqlStr.toString());
    	} catch (Exception e) {
    		throw e;
    	}
    }

    public void alterarIncrementalMX(ControleRemessaVO controleRemessaVO, UsuarioVO usuarioVO) throws Exception {
    	try {
    		StringBuilder sqlStr = new StringBuilder();
    		sqlStr.append("UPDATE ControleRemessa set incrementalMX = ").append(controleRemessaVO.getIncrementalMX()).append(" WHERE (codigo = ").append(controleRemessaVO.getCodigo()).append(")");
    		getConexao().getJdbcTemplate().update(sqlStr.toString());
    	} catch (Exception e) {
    		throw e;
    	}
    }
	
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)    
    public void realizarEstorno(ControleRemessaVO controleRemessaVO, UsuarioVO usuarioVO) throws Exception {
    	try {
    		StringBuilder sqlStr = new StringBuilder();
    		sqlStr.append("UPDATE ControleRemessa set situacao = '").append(controleRemessaVO.getSituacaoControleRemessa().getValor()).append("' WHERE (codigo = ").append(controleRemessaVO.getCodigo()).append(")"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
    		getConexao().getJdbcTemplate().update(sqlStr.toString());
    	} catch (Exception e) {
    		throw e;
    	}
    }
    
    /*
     * (non-Javadoc)
     *
     * @see
     * negocio.facade.jdbc.financeiro.ControleRemessaInterfaceFacade#excluir(negocio.comuns.financeiro.ControleRemessaVO
     * )
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ControleRemessaVO obj, UsuarioVO usuario) throws Exception {
        try {
            ControleRemessa.excluir(getIdEntidade(), true, usuario);
            String sql = "DELETE FROM ControleRemessa WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
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
        //ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ControleRemessa WHERE responsavel = " + valorConsulta.intValue() + "";
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
        //ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ControleRemessa.*, banco.nome as nomeBanco, contacorrente.numero as numeroContaCorrente, contacorrente.digito as digitoContaCorrente  FROM ControleRemessa "
        		+ "inner join banco on banco.codigo = controleremessa.banco "
        		+ "inner join contacorrente on contacorrente.codigo = controleremessa.contacorrente "
        		+ "WHERE ControleRemessa.codigo = " + valorConsulta.intValue() + " ORDER BY ControleRemessa.codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorNomeSacado(String valorConsulta, SituacaoControleRemessaEnum situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	//ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "SELECT distinct ControleRemessa.*, banco.nome as nomeBanco, contacorrente.numero as numeroContaCorrente, contacorrente.digito as digitoContaCorrente  FROM ControleRemessa "
    			+ "inner join banco on banco.codigo = controleremessa.banco "
    			+ "inner join contacorrente on contacorrente.codigo = controleremessa.contacorrente "
    			+ "left join controleremessacontareceber on controleremessacontareceber.controleRemessa = controleRemessa.codigo "
    			+ "left join contareceber on controleremessacontareceber.contareceber = contareceber.codigo "
    			+ "left join pessoa on contareceber.pessoa = pessoa.codigo "
    			+ " WHERE pessoa.nome ilike '" + valorConsulta + "%' ";
    			if (situacao.getValor() != "" ) {
    				sqlStr += " AND ControleRemessa.situacao = '" + situacao.getValor() + "' ";
    			}
    			sqlStr += " AND ((dataGeracao >= '" + Uteis.getDataJDBC(dataIni) + "') and (dataGeracao <= '"
    			+ Uteis.getDataJDBC(dataFim) + "')) ORDER BY dataGeracao";
    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorCPFSacado(String valorConsulta, SituacaoControleRemessaEnum situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	//ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	String sqlStr = "SELECT distinct ControleRemessa.*, banco.nome as nomeBanco, contacorrente.numero as numeroContaCorrente, contacorrente.digito as digitoContaCorrente  FROM ControleRemessa "
    			+ "inner join banco on banco.codigo = controleremessa.banco "
    			+ "inner join contacorrente on contacorrente.codigo = controleremessa.contacorrente "
    			+ "left join controleremessacontareceber on controleremessacontareceber.controleRemessa = controleRemessa.codigo "
    			+ "left join contareceber on controleremessacontareceber.contareceber = contareceber.codigo "
    			+ "left join pessoa on contareceber.pessoa = pessoa.codigo "
    			+ " WHERE lower (replace(replace((pessoa.cpf),'.',''),'-','')) like('" + Uteis.retirarMascaraCPF(valorConsulta.toLowerCase()) + "%')" ;
    	if (situacao.getValor() != "" ) {
    		sqlStr += " AND ControleRemessa.situacao = '" + situacao.getValor() + "' ";
    	}
    	sqlStr += " AND ((dataGeracao >= '" + Uteis.getDataJDBC(dataIni) + "') and (dataGeracao <= '"
    			+ Uteis.getDataJDBC(dataFim) + "')) ORDER BY dataGeracao";
    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public List consultarPorContaCorrente(String valorConsulta, SituacaoControleRemessaEnum situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	String sqlStr = "SELECT distinct ControleRemessa.*, banco.nome as nomeBanco, contacorrente.numero as numeroContaCorrente, contacorrente.digito as digitoContaCorrente  FROM ControleRemessa "
    			+ "inner join banco on banco.codigo = controleremessa.banco "
    			+ "inner join contacorrente on contacorrente.codigo = controleremessa.contacorrente "
//    			+ "left join controleremessacontareceber on controleremessacontareceber.controleRemessa = controleRemessa.codigo "
//    			+ "left join contareceber on controleremessacontareceber.contareceber = contareceber.codigo "
//    			+ "left join pessoa on contareceber.pessoa = pessoa.codigo "
    			+ " WHERE contacorrente.numero ilike '" + valorConsulta.toLowerCase() + "%'" ;    	
    	if (situacao.getValor() != "" ) {
    		sqlStr += " AND ControleRemessa.situacao = '" + situacao.getValor() + "' ";
    	}
    	sqlStr += " AND ((dataGeracao >= '" + Uteis.getDataJDBC(dataIni) + "') and (dataGeracao <= '"
    			+ Uteis.getDataJDBC(dataFim) + "')) ORDER BY dataGeracao";
    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorMatricula(String valorConsulta, SituacaoControleRemessaEnum situacao, Date dataIni, Date dataFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	String sqlStr = "SELECT distinct ControleRemessa.*, banco.nome as nomeBanco, contacorrente.numero as numeroContaCorrente, contacorrente.digito as digitoContaCorrente  FROM ControleRemessa "
    			+ "inner join banco on banco.codigo = controleremessa.banco "
    			+ "inner join contacorrente on contacorrente.codigo = controleremessa.contacorrente "
    			+ "left join controleremessacontareceber on controleremessacontareceber.controleRemessa = controleRemessa.codigo "
    			+ "left join contareceber on controleremessacontareceber.contareceber = contareceber.codigo "
    			+ "left join pessoa on contareceber.pessoa = pessoa.codigo "
    			+ " WHERE contareceber.matriculaaluno = '" + valorConsulta.toLowerCase() + "'" ;    	
    	if (situacao.getValor() != "" ) {
    		sqlStr += " AND ControleRemessa.situacao = '" + situacao.getValor() + "' ";
    	}
    	sqlStr += " AND ((dataGeracao >= '" + Uteis.getDataJDBC(dataIni) + "') and (dataGeracao <= '"
    			+ Uteis.getDataJDBC(dataFim) + "')) ORDER BY dataGeracao";
    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorNossoNumero(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	String sqlStr = "SELECT distinct ControleRemessa.*, banco.nome as nomeBanco, contacorrente.numero as numeroContaCorrente, contacorrente.digito as digitoContaCorrente  FROM ControleRemessa "
    			+ "inner join banco on banco.codigo = controleremessa.banco "
    			+ "inner join contacorrente on contacorrente.codigo = controleremessa.contacorrente "
    			+ "left join controleremessacontareceber on controleremessacontareceber.controleRemessa = controleRemessa.codigo "
    			//+ "left join contareceber on controleremessacontareceber.contareceber = contareceber.codigo "
    			//+ "left join pessoa on contareceber.pessoa = pessoa.codigo "
    			+ " WHERE controleremessacontareceber.nossonumero = '" + valorConsulta + "'" 
    			+" ORDER BY dataGeracao";
    	
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public Integer consultarPorNrBanco(String valorConsulta) throws Exception {    	
    	String sqlStr = "SELECT count(codigo) as qtd FROM ControleRemessa where banco = " + valorConsulta + " ";
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
     * @return List Contendo vários objetos da classe <code>ControleRemessaVO</code> resultantes da consulta.
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
     * objeto da classe <code>ControleRemessaVO</code>.
     *
     * @return O objeto da classe <code>ControleRemessaVO</code> com os dados devidamente montados.
     */
    public static ControleRemessaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleRemessaVO obj = new ControleRemessaVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.getResponsavel().setCodigo(dadosSQL.getInt("responsavel"));
        obj.setDataGeracao(dadosSQL.getDate("dataGeracao"));
        obj.setTipoRemessa(dadosSQL.getString("tipoRemessa"));		
        obj.setRemessaOnline(dadosSQL.getBoolean("remessaOnline"));		
        obj.getArquivoRemessa().setCodigo(dadosSQL.getInt("arquivoRemessa"));
        obj.getBancoVO().setCodigo(dadosSQL.getInt("banco"));
        obj.getBancoVO().setNome(dadosSQL.getString("nomeBanco"));
        obj.setDataInicio(dadosSQL.getDate("dataInicio"));
        obj.setDataFim(dadosSQL.getDate("dataFim"));
        obj.setIncrementalMX(dadosSQL.getInt("incrementalMX"));
        obj.getUnidadeEnsinoVO().setCodigo(dadosSQL.getInt("unidadeEnsino"));
        obj.getContaCorrenteVO().setCodigo(dadosSQL.getInt("contaCorrente"));
        obj.getContaCorrenteVO().setNumero(dadosSQL.getString("numeroContaCorrente"));
        obj.getContaCorrenteVO().setDigito(dadosSQL.getString("digitoContaCorrente"));
        obj.setSituacaoControleRemessa(SituacaoControleRemessaEnum.getEnum(dadosSQL.getString("situacao")));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        montarDadosArquivo(obj, nivelMontarDados, usuario);
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        return obj;
    }

    public static void montarDadosArquivo(ControleRemessaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getArquivoRemessa().getCodigo().intValue() == 0) {
            obj.setArquivoRemessa(new ArquivoVO());
            return;
        }
        obj.setArquivoRemessa(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoRemessa().getCodigo(), nivelMontarDados,
                usuario));
    }

    public static void montarDadosResponsavel(ControleRemessaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados,
                usuario));
    }

    public List consultarPorDataGeracao(Date prmIni, Date prmFim, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT ControleRemessa.*, banco.nome as nomeBanco, contacorrente.numero as numeroContaCorrente, contacorrente.digito as digitoContaCorrente FROM ControleRemessa "
        		+ "inner join banco on banco.codigo = controleremessa.banco "
        		+ "inner join contacorrente on contacorrente.codigo = controleremessa.contacorrente "
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
    public ControleRemessaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
            throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT ControleRemessa.*, banco.nome as nomeBanco, contacorrente.numero as numeroContaCorrente, contacorrente.digito as digitoContaCorrente  FROM ControleRemessa "
            		+ "inner join banco on banco.codigo = controleremessa.banco "
            		+ "inner join contacorrente on contacorrente.codigo = controleremessa.contacorrente "
            		+ " WHERE ControleRemessa.codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ControleRemessa ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    @Override
    public HashMap<String, String> consultarMotivoRejeicaoBanco(Integer codigoBanco, String cnab) throws Exception {
    	String sql = "select * from bancomotivorejeicao  where 1=1 and (banco is null or banco = ?) ";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoBanco});
    	HashMap<String, String> hash = new HashMap<String, String>();	
    	while (tabelaResultado.next()) {
    		hash.put(tabelaResultado.getString("codigorejeicao"), tabelaResultado.getString("motivorejeicao"));
        }
        return hash;
    }

    public FiltroRelatorioFinanceiroVO consultarFiltrosRelatorioPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario)
    		throws Exception {
    	String sql = "SELECT ControleRemessa.*, banco.nome as nomeBanco, contacorrente.numero as numeroContaCorrente, contacorrente.digito as digitoContaCorrente  FROM ControleRemessa "
    			+ "inner join banco on banco.codigo = controleremessa.banco "
    			+ "inner join contacorrente on contacorrente.codigo = controleremessa.contacorrente "
    			+ " WHERE ControleRemessa.codigo = ?";
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
    	if (!tabelaResultado.next()) {
    		throw new ConsistirException("Dados Não Encontrados ( ControleRemessa ).");
    	}
		FiltroRelatorioFinanceiroVO obj = new FiltroRelatorioFinanceiroVO(false);        
        obj.setTipoOrigemBiblioteca(tabelaResultado.getBoolean("tipoOrigemBiblioteca"));
        obj.setTipoOrigemBolsaCusteadaConvenio(tabelaResultado.getBoolean("tipoOrigemBolsaCusteadaConvenio"));
        obj.setTipoOrigemContratoReceita(tabelaResultado.getBoolean("tipoOrigemContratoReceita"));
        obj.setTipoOrigemDevolucaoCheque(tabelaResultado.getBoolean("tipoOrigemDevolucaoCheque"));
        obj.setTipoOrigemInclusaoReposicao(tabelaResultado.getBoolean("tipoOrigemInclusaoReposicao"));
        obj.setTipoOrigemInscricaoProcessoSeletivo(tabelaResultado.getBoolean("tipoOrigemInscricaoProcessoSeletivo"));
        obj.setTipoOrigemMatricula(tabelaResultado.getBoolean("tipoOrigemMatricula"));
        obj.setTipoOrigemMensalidade(tabelaResultado.getBoolean("tipoOrigemMensalidade"));
        obj.setTipoOrigemNegociacao(tabelaResultado.getBoolean("tipoOrigemNegociacao"));
        obj.setTipoOrigemOutros(tabelaResultado.getBoolean("tipoOrigemOutros"));
        obj.setTipoOrigemRequerimento(tabelaResultado.getBoolean("tipoOrigemRequerimento"));
        return obj;
    }
    
    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ControleRemessa.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        ControleRemessa.idEntidade = idEntidade;
    }

    public void gerarDadosHeader(EditorOC editorOC, Comando cmd, ControleRemessaVO controleRemessaVO, int numeroSequencial, UsuarioVO usuario,
            UnidadeEnsinoVO unidadeEnsinoVO)
            throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 1, 1, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 2, 2, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "REMESSA", 3, 9, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 10, 11, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "COBRANCA", 12, 26, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 27, 46, " ", false, false);

        linha = editorOC.adicionarCampoLinhaVersao2(linha, unidadeEnsinoVO.getRazaoSocial().toUpperCase(), 47, 76, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "237", 77, 79, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "Bradesco", 80, 94, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(controleRemessaVO.getDataGeracao(), "ddMMyy"), 95, 100, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 101, 108, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "MX", 109, 110, " ", false, false);

        Integer codigoRemessa = consultarCodigoRemessa();
        linha = editorOC.adicionarCampoLinhaVersao2(linha, codigoRemessa.toString(), 111, 117, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 118, 394, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "000001", 395, 400, " ", false, false);

        numeroSequencial++;
        cmd.adicionarLinhaComando(linha, 0);
    }

    public void gerarDadosTransacao(EditorOC editorOC, Comando cmd, ContaReceberVO contaReceberVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO,
            int numeroSequencial) throws Exception {
        String linha = "";

        linha = editorOC.adicionarCampoLinhaVersao2(linha, "1", 1, 1, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 2, 20, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0" + configuracaoFinanceiroVO.getContaCorrentePadraoControleCobranca().getCarteira()
                + configuracaoFinanceiroVO.getContaCorrentePadraoControleCobranca().getAgencia().getNumeroAgencia()
                + configuracaoFinanceiroVO.getContaCorrentePadraoControleCobranca().getNumero(), 21, 37, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 38, 62, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "237", 63, 65, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 66, 66, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removeCaractersEspeciais(configuracaoFinanceiroVO.getPercentualMultaPadrao().toString()), 67, 70, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, contaReceberVO.getNossoNumero(), 71, 81, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 82, 82, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 83, 92, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "2", 93, 93, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 94, 94, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 95, 104, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 105, 105, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 106, 106, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 107, 108, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 109, 110, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, contaReceberVO.getNrDocumento(), 111, 120, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(contaReceberVO.getDataVencimento(), "ddMMyy"), 121, 126, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removeCaractersEspeciais(contaReceberVO.getValor().toString()), 127, 139, "0", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 140, 147, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "99", 148, 149, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "N", 150, 150, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.getData(new Date(), "ddMMyy"), 151, 156, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 157, 160, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 161, 179, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removeCaractersEspeciais(contaReceberVO.getValorDescontoRecebido().toString()), 180, 192, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 193, 205, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "0", 206, 218, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "01", 219, 220, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removeCaractersEspeciais(contaReceberVO.getPessoa().getCPF()), 221, 234, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, contaReceberVO.getPessoa().getNome().toUpperCase(), 235, 274, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, contaReceberVO.getPessoa().getEndereco().toUpperCase() + " "
                + contaReceberVO.getPessoa().getSetor().toUpperCase(), 275, 314, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 315, 326, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, Uteis.removeCaractersEspeciais(contaReceberVO.getPessoa().getCEP()), 327, 334, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 335, 394, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 395, 400, "0", false, false);
        cmd.adicionarLinhaComando(linha, 0);
    }

    public void gerarDadosTrailler(EditorOC editorOC, Comando cmd, int numeroSequencial) throws Exception {
        String linha = "";
        linha = editorOC.adicionarCampoLinhaVersao2(linha, "9", 1, 1, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, " ", 2, 394, " ", false, false);
        linha = editorOC.adicionarCampoLinhaVersao2(linha, String.valueOf(numeroSequencial), 395, 400, "0", false, false);
        cmd.adicionarLinhaComando(linha, 0);
    }
    
    
    public void gerarDadosArquivoRemessa(List<ContaReceberVO> contaReceberVOs, ControleRemessaVO controleRemessaVO, String caminhoPasta,
            UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO,
            UnidadeEnsinoVO unidadeEnsinoVO, Boolean arquivoTeste)
            throws Exception {

        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int character = (int) (Math.random() * 26);
        String letter = alphabet.substring(character, character + 1);
        String nomeArquivo = "";
        if (arquivoTeste) {
            nomeArquivo = "CB" + Uteis.getData(new Date(), "ddMM") + Math.round(Math.random() * 10) + letter + ".TST";
        } else {
            nomeArquivo = "CB" + Uteis.getData(new Date(), "ddMM") + Math.round(Math.random() * 10) + letter + ".REM";
        }
        executarCriacaoArquivoTexto(caminhoPasta + File.separator + PastaBaseArquivoEnum.REMESSA_TMP.getValue(), nomeArquivo);

        EditorOC editorOC = new EditorOC();
        Comando cmd = new Comando();
        int numeroSequencial = 1;
        
        unidadeEnsinoVO = (getAplicacaoControle().getUnidadeEnsinoVO(unidadeEnsinoVO.getCodigo(), usuario));

        gerarDadosHeader(editorOC, cmd, controleRemessaVO, numeroSequencial, usuario, unidadeEnsinoVO);

        Iterator i = contaReceberVOs.iterator();
        while (i.hasNext()) {
            numeroSequencial++;
            ContaReceberVO contaReceberVO = (ContaReceberVO) i.next();
            gerarDadosTransacao(editorOC, cmd, contaReceberVO, configuracaoFinanceiroVO, numeroSequencial);
        }
        numeroSequencial++;
        gerarDadosTrailler(editorOC, cmd, numeroSequencial);

        editorOC.adicionarComando(cmd);

        getPrintWriter().print(editorOC.getText());
        getPrintWriter().close();

        controleRemessaVO.getArquivoRemessa().setNome(nomeArquivo);
        controleRemessaVO.getArquivoRemessa().setOrigem(OrigemArquivo.REMESSA.getValor());

        controleRemessaVO.getArquivoRemessa().setPastaBaseArquivo(PastaBaseArquivoEnum.REMESSA_TMP.getValue());
        controleRemessaVO.getArquivoRemessa().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.REMESSA_TMP);

        getFacadeFactory().getArquivoFacade().incluir(controleRemessaVO.getArquivoRemessa(), usuario, configuracaoGeralSistemaVO);
        incluir(controleRemessaVO, null, usuario);

    }

    private Integer consultarCodigoRemessa() {
        String sqlStr = "SELECT MAX(codigo) FROM controleRemessa";
        try {
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
            if (!tabelaResultado.next()) {
                return 0;
            }
            return tabelaResultado.getInt("max");
        } finally {
            sqlStr = null;
        }

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
    
    public String executarGeracaoNomeArquivoRemessa(ControleRemessaVO controleRemessaVO ,UsuarioVO usuarioVO) throws Exception {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        int character = (int) (Math.random() * 26);
        String letter = alphabet.substring(character, character + 1);
        String nomeArquivo = "";
        if (controleRemessaVO.getBancoVO().getNrBanco().equals(Bancos.DAYCOVAL.getNumeroBanco())) {
        	Integer qtd = this.consultarPorNrBanco(controleRemessaVO.getBancoVO().getNrBanco());
        	qtd++;
        	nomeArquivo = "EEC" + Uteis.getData(new Date(), "ddMM") + qtd + ".TXT";	
        } else if (controleRemessaVO.getBancoVO().getNrBanco().equals(Bancos.SICRED.getNumeroBanco())) {
        	String mesStr = "";
        	Integer mes = Uteis.getMesDataAtual();
        	mesStr = mes.toString();
        	if (mes == 10) {
        		mesStr = "O";
        	} else if (mes == 11) {
        		mesStr = "N";
        	} else if (mes == 12) {
        		mesStr = "D";
        	}
    		//nomeArquivo = controleRemessaVO.getContaCorrenteVO().getNumero() + mesStr + Uteis.getData(new Date(), "dd") + ".CRM";    		
    		nomeArquivo = controleRemessaVO.getContaCorrenteVO().getNumero() + mesStr + Uteis.getData(new Date(), "dd") + "."+ realizarCriacaoExtensaoArquivoRemessaBancoSicredi(controleRemessaVO ,usuarioVO);	
        } else if (controleRemessaVO.getContaCorrenteVO().getUtilizaCobrancaPartilhada()) {
        	nomeArquivo = "C" + Uteis.getData(new Date(), "ddMM") + Math.round(Math.random() * 10) + letter + ".TXT";
        } else {
        	nomeArquivo = "C" + Uteis.getData(new Date(), "ddMM") + Math.round(Math.random() * 10) + letter + ".REM";
        }      
        return nomeArquivo;
    }

    public String realizarCriacaoExtensaoArquivoRemessaBancoSicredi(ControleRemessaVO controleRemessaVO , UsuarioVO usuarioVO)  {    	
    	try {
			Integer qtdRemessasGeradasDiaAtual =  consultarQuantidadePorDataContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), Uteis.getDateHoraInicialDia(new Date()), new Date());
			   String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";			  
			   String  primeiraLetraExtensao = "R" ; // assume fixo a primeira letra sendo como R 
			 
			   Integer posicaoSegundoCampoAlfabeto = qtdRemessasGeradasDiaAtual / 26 ;
			   String  segundaLetraExtensao = alphabet.substring(posicaoSegundoCampoAlfabeto, posicaoSegundoCampoAlfabeto + 1);	
			   
			   Integer posicaoTerceiroCampoAlfabeto = qtdRemessasGeradasDiaAtual % 26 ;
			   String  terceiraLetraExtensao = alphabet.substring(posicaoTerceiroCampoAlfabeto, posicaoTerceiroCampoAlfabeto + 1);			   
			  
			   return  primeiraLetraExtensao + segundaLetraExtensao +  terceiraLetraExtensao ; 
		} catch (Exception e) {		
			e.printStackTrace();
		}    	
    	return "CRM" ;
		
	}

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarGeracaoArquivoRemessa(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, UnidadeEnsinoVO unidadeEnsinoVO, String caminhoBaseArquivo, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiro, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
        controleRemessaVO.setBancoVO(getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(controleRemessaVO.getBancoVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuarioVO));
        controleRemessaVO.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(controleRemessaVO.getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioVO));
        String nomeArquivo = executarGeracaoNomeArquivoRemessa(controleRemessaVO,usuarioVO);
        if (!controleRemessaVO.getBancoVO().getNrBanco().equals(Bancos.SICRED.getNumeroBanco())){
            while(getFacadeFactory().getArquivoFacade().verificarExisteArquivoMesmoNomeAnoUpload(nomeArquivo, UteisData.getAnoDataString(new Date()), false, usuarioVO)) {
            	nomeArquivo = executarGeracaoNomeArquivoRemessa(controleRemessaVO,usuarioVO);
            }        	
        }

        if (controleRemessaVO.getBancoVO().getNrBanco().equals(Bancos.SICOOB.getNumeroBanco()) &&
        		controleRemessaVO.getBancoVO().getModeloGeracaoBoleto().equals(ModeloGeracaoBoletoSicoob.FEBRABAN.getValor())) {
        	controleRemessaVO.getBancoVO().setNrBanco(controleRemessaVO.getBancoVO().getNrBanco() + "CREDSAUDE");
        }
        executarCriacaoArquivoTexto(caminhoBaseArquivo + File.separator + PastaBaseArquivoEnum.REMESSA.getValue(), nomeArquivo);
        
        controleRemessaVO.setUnidadeEnsinoVO(getFacadeFactory().getUnidadeEnsinoFacade().consultaRapidaPorCodigo(unidadeEnsinoVO.getCodigo(), false, usuarioVO));
        EditorOC editorOC = null;
        controleRemessaVO.getArquivoRemessa().setCodigo(0);
        incluir(controleRemessaVO, filtroRelatorioFinanceiro, usuarioVO);
        
        editorOC = executarGeracaoDadosArquivoRemessa(listaDadosRemessaVOs, controleRemessaVO, unidadeEnsinoVO, caminhoBaseArquivo, configuracaoFinanceiroVO, configuracaoGeralSistemaVO, usuarioVO);
        
        this.alterarIncrementalMX(controleRemessaVO, usuarioVO);

        if (controleRemessaVO.getBancoVO().getNrBanco().equals("756CREDSAUDE") &&
        		controleRemessaVO.getBancoVO().getModeloGeracaoBoleto().equals(ModeloGeracaoBoletoSicoob.FEBRABAN.getValor())) {
        	controleRemessaVO.getBancoVO().setNrBanco("756");
        }
        if (editorOC == null) {
        	listaDadosRemessaVOs.clear();
        	throw new Exception("Não foi possível gerar o arquivo de remessa do banco selecionado! Verifique se a geração para esse banco está implementada!");
        }
        getPrintWriter().print(editorOC.getText());
        getPrintWriter().close();

        controleRemessaVO.getArquivoRemessa().setNome(nomeArquivo);
        controleRemessaVO.getArquivoRemessa().setOrigem(OrigemArquivo.REMESSA.getValor());

        controleRemessaVO.getArquivoRemessa().setPastaBaseArquivo(PastaBaseArquivoEnum.REMESSA.getValue());
        controleRemessaVO.getArquivoRemessa().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.REMESSA);

        getFacadeFactory().getArquivoFacade().incluir(controleRemessaVO.getArquivoRemessa(), usuarioVO, configuracaoGeralSistemaVO);
        alterarArquivo(controleRemessaVO, controleRemessaVO.getArquivoRemessa(), usuarioVO);
        for (ControleRemessaContaReceberVO obj : listaDadosRemessaVOs) {
        	if (obj.getApresentarArquivoRemessa()) {
        		getFacadeFactory().getControleRemessaContaReceberFacade().incluir(obj, controleRemessaVO.getCodigo(), usuarioVO);
        	}
        }		
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarEnvioRemessaOnline(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, UnidadeEnsinoVO unidadeEnsinoVO, String caminhoBaseArquivo, FiltroRelatorioFinanceiroVO filtroRelatorioFinanceiro, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
    	controleRemessaVO.setContaCorrenteVO(getFacadeFactory().getContaCorrenteFacade().consultarPorChavePrimaria(controleRemessaVO.getContaCorrenteVO().getCodigo(), false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioVO));
    	Iterator i = listaDadosRemessaVOs.iterator();
    	while (i.hasNext()) {
    		ControleRemessaContaReceberVO obj = (ControleRemessaContaReceberVO)i.next();
    		obj.getContaReceber();
			try {
				RegistroOnlineBoleto registrar = new RegistroOnlineBoleto();
				ContaReceberVO contaReceber = new ContaReceberVO();
				contaReceber.setCodigo(obj.getContaReceber());
				contaReceber.setContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo());
				contaReceber.setContaCorrenteVO(controleRemessaVO.getContaCorrenteVO());
				getFacadeFactory().getContaReceberFacade().carregarDados(contaReceber, NivelMontarDados.BASICO ,configuracaoFinanceiroVO , usuarioVO);
				registrar.enviarBoletoRemessaOnline(contaReceber, obj, configuracaoGeralSistemaVO, configuracaoFinanceiroVO, usuarioVO);
				registrar = null;
				contaReceber = null;
			} catch (Exception e) {
				// caso ocorra erro o processamento irá continuar, até concluir todos da lista.
				// dentro do metodo de envio j´salvo o motivo em cada conta enviada.
			}
    	}
    }
    
    public EditorOC executarGeracaoDadosArquivoRemessa(List<ControleRemessaContaReceberVO> listaDadosRemessaVOs, ControleRemessaVO controleRemessaVO, UnidadeEnsinoVO unidadeEnsinoVO, String caminhoBaseArquivo, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuarioVO) throws Exception {
    	EditorOC editorOC = null;
    	if (controleRemessaVO.getContaCorrenteVO().getCnab().equals("CNAB240")) {
    		editorOC = (EditorOC) getLayoutCNAB240(controleRemessaVO, usuarioVO).executarGeracaoDadosArquivoRemessa(listaDadosRemessaVOs, controleRemessaVO, configuracaoFinanceiroVO, usuarioVO);
    	} else {
    		editorOC = (EditorOC) getLayoutCNAB400(controleRemessaVO, usuarioVO).executarGeracaoDadosArquivoRemessa(listaDadosRemessaVOs, controleRemessaVO, configuracaoFinanceiroVO, usuarioVO);
    	}
    	return editorOC;
    }

    public ControleRemessaCNAB240LayoutInterfaceFacade getLayoutCNAB240(ControleRemessaVO controleRemessaVO, UsuarioVO usuario) throws Exception {    	
    	ControleRemessaCNAB240LayoutInterfaceFacade layout = (ControleRemessaCNAB240LayoutInterfaceFacade) BancoFactory.getLayoutInstanciaControleRemessaCNAB240(controleRemessaVO.getBancoVO().getNrBanco(), controleRemessaVO.getContaCorrenteVO().getCnab());
    	if (layout == null) {
        	throw new ConsistirException("Serviço não implementado para CNAB 240 (CONTA CORRENTE).");
        }
    	return layout;
    }

    public ControleRemessaCNAB400LayoutInterfaceFacade getLayoutCNAB400(ControleRemessaVO controleRemessaVO, UsuarioVO usuario) throws Exception {    	
    	ControleRemessaCNAB400LayoutInterfaceFacade layout = (ControleRemessaCNAB400LayoutInterfaceFacade) BancoFactory.getLayoutInstanciaControleRemessaCNAB400(controleRemessaVO.getBancoVO().getNrBanco(), controleRemessaVO.getContaCorrenteVO().getCnab());
    	if (layout == null) {
        	throw new ConsistirException("Serviço não implementado para CNAB 400 (CONTA CORRENTE).");
        }
    	return layout;
    }

    @Deprecated
 	public ControleRemessaVO processarArquivo(ControleRemessaVO controleRemessaVO, List listaDadosRemessaVOs, String caminho, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuarioVO) throws Exception {
    	if (controleRemessaVO.getContaCorrenteVO().getCnab().equals("CNAB240")) {
    		getLayoutCNAB240(controleRemessaVO, usuarioVO).processarArquivoRetorno(controleRemessaVO, caminho + (controleRemessaVO.getNomeArquivo().equals("") ? "" : File.separator + controleRemessaVO.getNomeArquivo()), configuracaoFinanceiroVO, usuarioVO);
    	} else {
    		getLayoutCNAB400(controleRemessaVO, usuarioVO).processarArquivoRetorno(controleRemessaVO, caminho + (controleRemessaVO.getNomeArquivo().equals("") ? "" : File.separator + controleRemessaVO.getNomeArquivo()), configuracaoFinanceiroVO, usuarioVO);
    	}
		Boolean processarLayoutCaixaSoComErro = true;
    	HashMap<String, String> listaRejeicao = consultarMotivoRejeicaoBanco(controleRemessaVO.getBancoVO().getCodigo(), controleRemessaVO.getContaCorrenteVO().getCnab());
    	if(controleRemessaVO.getBancoVO().getNrBanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco())){
    		if ( controleRemessaVO.getIncrementalMX().intValue() > 0 && (controleRemessaVO.getIncrementalMX().intValue() != controleRemessaVO.getRegistroHeaderRetornoVO().getNumeroSequencialArquivo().intValue())) {
    			throw new Exception ("O Arquivo de Retorno selecionado não corresponde a remessa selecionada. Verifique o arquivo de seguencial => " + controleRemessaVO.getIncrementalMX());
    		}
    	}
    	if (controleRemessaVO.getRegistroDetalheRetornoVOs().size() > 0) {
    		Iterator i = controleRemessaVO.getRegistroDetalheRetornoVOs().iterator();
    		while (i.hasNext()) {
    			RegistroDetalheVO registro = (RegistroDetalheVO)i.next();
    			// Verifica se ocorreu erro no registro do arquivo.
				// registro.getMotivoRegeicao().equals("")
    			
    			//
					String nossoNumero = registro.getIdentificacaoTituloEmpresa();
					Iterator j = listaDadosRemessaVOs.iterator();
					while (j.hasNext()) {
						ControleRemessaContaReceberVO obj = (ControleRemessaContaReceberVO)j.next();
						if(Uteis.isAtributoPreenchido(nossoNumero)){
							String nossonumerocomparar = obj.getNossoNumero();
							if (!nossoNumero.contains("P") && !nossoNumero.contains("-")) { 
								nossoNumero = new BigInteger(nossoNumero).toString();
							}
							if (!nossonumerocomparar.contains("P") && !nossonumerocomparar.contains("-")) {
								nossonumerocomparar = new BigInteger(nossonumerocomparar).toString();
							}
							if (nossonumerocomparar.equals(nossoNumero)) {
								processarLayoutCaixaSoComErro = false;
//								if (registro.getIdentificacaoOcorrencia() == 3 || registro.getIdentificacaoOcorrencia() == 15 || registro.getIdentificacaoOcorrencia() == 16 || registro.getIdentificacaoOcorrencia() == 27) {
								if (registro.getIdentificacaoOcorrencia() != 2 && registro.getIdentificacaoOcorrencia() != 9 && registro.getIdentificacaoOcorrencia() != 10  && registro.getIdentificacaoOcorrencia() != 6 
										&& (registro.getIdentificacaoOcorrencia() == 3 && !registro.getMotivoRegeicao().equals("09") && !registro.getMotivoRegeicao().equals("00"))) {
									obj.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.ESTORNADO);
		    			    		obj.setUsuarioEstorno(usuarioVO);
		    			    		obj.setDataEstorno(new Date());
//		    			    		obj.setMotivoEstorno(registro.getMotivoRegeicao());
		    			    		obj.setMotivoEstorno( obterMotivoRejeicao(listaRejeicao, registro.getMotivoRegeicao()));
		    			    		if (Uteis.isAtributoPreenchido(controleRemessaVO.getBancoVO().getNrBanco()) && controleRemessaVO.getBancoVO().getNrBanco().equals(Bancos.BRADESCO.getNumeroBanco())) {
										validarMotivoRejeicaoBradesco(registro.getMotivoRegeicao() , obj, usuarioVO);
									}else {
										getFacadeFactory().getContaReceberFacade().alterarDataArquivoRemessaContasRejeitadas(obj.getContaReceber(), usuarioVO);
									}
									getFacadeFactory().getControleRemessaContaReceberFacade().realizarEstorno(obj, usuarioVO);
								} else {
									/*if (registro.getIdentificacaoOcorrencia() == 9 || registro.getIdentificacaoOcorrencia() == 10) {
										obj.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.REMETIDA_TITULO_CANCELADO);
									} else {*/
										obj.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.REMETIDA);
									//}
									getFacadeFactory().getControleRemessaContaReceberFacade().realizarRegistroDadosAceite(obj, usuarioVO);
									
								}
								getFacadeFactory().getContaReceberFacade().alterarIdentificacaoTituloBanco(nossoNumero, registro.getIdentificacaoTituloBanco(), usuarioVO);						
							}
						} else if(!Uteis.isAtributoPreenchido(registro.getIdentificacaoTituloEmpresa()) && controleRemessaVO.getBancoVO().getNrBanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco()) ){
							// regra utilizada para caixa economica federal que possui um arquivo de retorno diferente dos demais bancos
							if (obj.getPosicaoNoArquivoRemessa() == (registro.getIdentificacaoOcorrencia() + 2) / 3) {
								obj.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.ESTORNADO);
	    			    		obj.setUsuarioEstorno(usuarioVO);
	    			    		obj.setDataEstorno(new Date());
	    			    		obj.setMotivoEstorno(registro.getMotivoRegeicao());
							}
						}
	    			}
    		}
    		if(controleRemessaVO.getBancoVO().getNrBanco().equals(Bancos.CAIXA_ECONOMICA_FEDERAL.getNumeroBanco()) && processarLayoutCaixaSoComErro){
				Iterator j = listaDadosRemessaVOs.iterator();
				while (j.hasNext()) {
					ControleRemessaContaReceberVO obj = (ControleRemessaContaReceberVO)j.next();
					if (obj.getSituacaoControleRemessaContaReceber().equals(SituacaoControleRemessaContaReceberEnum.ESTORNADO)) {
			            getFacadeFactory().getContaReceberFacade().alterarDataArquivoRemessaContasRejeitadas(obj.getContaReceber(), usuarioVO);
			            getFacadeFactory().getControleRemessaContaReceberFacade().realizarEstorno(obj, usuarioVO);
					} else {
						if (obj.getContaRemetidaComAlteracao()) {
							obj.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.REMETIDA_TITULO_CANCELADO);
						} else {
							obj.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.REMETIDA);
						}
						getFacadeFactory().getControleRemessaContaReceberFacade().realizarRegistroDadosAceite(obj, usuarioVO);
					}
				}    			
    		}
    		controleRemessaVO.setSituacaoControleRemessa(SituacaoControleRemessaEnum.RETORNO_REMESSA_PROCESSADO);
    	}
    	realizarEstorno(controleRemessaVO, usuarioVO);
    	return controleRemessaVO;
	}
    
    @Override
    public void realizarAtualizacaoControleRemessaPorArquivoRetorno(RegistroDetalheVO registro, ControleRemessaContaReceberVO obj, HashMap<String, String> listaRejeicao, UsuarioVO usuarioVO) throws Exception {
		String nossoNumero = registro.getIdentificacaoTituloEmpresa();
		String nossonumerocomparar = obj.getNossoNumero();
		if (!nossoNumero.contains("P") && !nossoNumero.contains("-")) {
			nossoNumero = new BigInteger(nossoNumero).toString();
		}
		if (!nossonumerocomparar.contains("P") && !nossonumerocomparar.contains("-")) {
			nossonumerocomparar = new BigInteger(nossonumerocomparar).toString();
		}
		if (nossonumerocomparar.equals(nossoNumero)) {
//				if (registro.getIdentificacaoOcorrencia() == 3 || registro.getIdentificacaoOcorrencia() == 15 || registro.getIdentificacaoOcorrencia() == 16 || registro.getIdentificacaoOcorrencia() == 27) {
			if (registro.getIdentificacaoOcorrencia() != 2 
					&& registro.getIdentificacaoOcorrencia() != 9 
					&& registro.getIdentificacaoOcorrencia() != 10 && registro.getIdentificacaoOcorrencia() != 6
					&& (registro.getIdentificacaoOcorrencia() == 3 
					&& !registro.getMotivoRegeicao().equals("09") 
					&& !registro.getMotivoRegeicao().equals("00"))) {
				obj.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.ESTORNADO);
				obj.setUsuarioEstorno(usuarioVO);
				obj.setDataEstorno(new Date());
//		    		obj.setMotivoEstorno(registro.getMotivoRegeicao());
				obj.setMotivoEstorno(obterMotivoRejeicao(listaRejeicao, registro.getMotivoRegeicao()));
				getFacadeFactory().getContaReceberFacade().alterarDataArquivoRemessaContasRejeitadas(obj.getContaReceber(), usuarioVO);
				getFacadeFactory().getControleRemessaContaReceberFacade().realizarEstorno(obj, usuarioVO);
			} else {
				/*
				 * if (registro.getIdentificacaoOcorrencia() == 9 || registro.getIdentificacaoOcorrencia() == 10) { obj.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.REMETIDA_TITULO_CANCELADO); } else {
				 */
				obj.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.REMETIDA);
				// }
				getFacadeFactory().getControleRemessaContaReceberFacade().realizarRegistroDadosAceite(obj, usuarioVO);
			}
			getFacadeFactory().getContaReceberFacade().alterarIdentificacaoTituloBanco(nossoNumero, registro.getIdentificacaoTituloBanco(), usuarioVO);
		}
    }
 	
 	public String obterMotivoRejeicao( HashMap<String, String> hash, String codigoRejeicao ) {
 		String motivoRejeicao = "";
 		if (hash.isEmpty()) {
 			return codigoRejeicao;
 		} else if (codigoRejeicao.length() <= 2) {
 			motivoRejeicao = hash.get(codigoRejeicao);
 	 		if (motivoRejeicao == null) {
 	 			motivoRejeicao = codigoRejeicao;
 	 		}
 	 		return motivoRejeicao;
 		} else {
 			for (int i =2; i <= codigoRejeicao.length(); i++) {
 				String codTemp = codigoRejeicao.substring(i-2, i);
 				String valor = hash.get(codTemp);
 	 	 		if (valor != null) {
 	 	 			motivoRejeicao += valor + "; ";
 	 	 		}
 	 	 		i++;
 			}
 	 		return motivoRejeicao;
 		}
 	}
 	
 	public void validarMotivoRejeicaoBradesco(String codigoRejeicao , ControleRemessaContaReceberVO obj, UsuarioVO usuario) throws Exception { 		
 		boolean contaReceberJaRegistrada = false; 		
 		for (int i =2; i <= codigoRejeicao.length(); i++) {
				String codTemp = codigoRejeicao.substring(i-2, i);				
	 	 		if (codTemp.equals(MotivoRejeicaoRemessa.MOTIVO_63.getCodigo().toString())) {
	 	 			contaReceberJaRegistrada =  true;
	 	 		}
	 	 		i++;
			}
 		if (contaReceberJaRegistrada) {
 			obj.setSituacaoControleRemessaContaReceber(SituacaoControleRemessaContaReceberEnum.REMETIDA);
 		}else {
			getFacadeFactory().getContaReceberFacade().alterarDataArquivoRemessaContasRejeitadas(obj.getContaReceber(), usuario);
			
		}
 		
 	}

	@Override
	public void realizarVerificacaoUltimaRemessaCriadaAtualizandoIncrementalMXPorControleRemessa(ControleRemessaVO controleRemessaVO,	UsuarioVO usuarioLogado) throws Exception {
		ControleRemessaVO controleRemessa = null ;
		try {			
			 controleRemessa =  consultarPorChavePrimaria(controleRemessaVO.getCodigo() + 1, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);			
		    }catch (Exception e) {
		    	// caiu no catch e porque esta  e a ultima remessa criada 
		    	 if(controleRemessa == null ) {    		 
		    		   getFacadeFactory().getControleRemessaMXFacade().alterarIncrementalPorContaCorrente(controleRemessaVO.getContaCorrenteVO().getCodigo(), controleRemessaVO.getIncrementalMX() - 1, null, usuarioLogado);
				 }
			}
		   
	}
	
	
	
	 public Integer consultarQuantidadePorDataContaCorrente(Integer contaCorrente, Date dataIni , Date dataFim) throws Exception {    	
	    	String sqlStr = "SELECT count(codigo) as qtd FROM ControleRemessa where contacorrente = " + contaCorrente + " ";
	    	       sqlStr += " AND ((dataGeracao >= '" + Uteis.getDataJDBC(dataIni) + "') and (dataGeracao <= '"+ Uteis.getDataJDBC(dataFim) + "')) ";
	    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
	        if (tabelaResultado.next()) {
	        	return tabelaResultado.getInt("qtd");
	        } else {
	        	return 0;
	        }
	    }
 	
}