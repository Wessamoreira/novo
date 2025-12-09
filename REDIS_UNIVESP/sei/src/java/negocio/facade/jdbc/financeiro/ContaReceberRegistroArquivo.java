package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberRegistroArquivoVO;
import negocio.comuns.financeiro.ControleCobrancaVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.FaixaDescontoProgressivo;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ContaReceberRegistroArquivoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>ContaReceberRegistroArquivoVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>ContaReceberRegistroArquivoVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ContaReceberRegistroArquivoVO
 * @see ControleAcesso
 */
@Lazy
@Repository
@Scope("singleton")
public class ContaReceberRegistroArquivo extends ControleAcesso implements ContaReceberRegistroArquivoInterfaceFacade {

    protected static String idEntidade;
    public static final long serialVersionUID = 1L;

    public ContaReceberRegistroArquivo() throws Exception {
        super();
        setIdEntidade("ContaReceberRegistroArquivo");
    }

    public static String getIdEntidade() {
        return ContaReceber.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        ContaReceberRegistroArquivo.idEntidade = idEntidade;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirContaReceberRegistroArquivo(Integer registroArquivoPrm, List<ContaReceberRegistroArquivoVO> objetos, UsuarioVO usuario) throws Exception {
        for (ContaReceberRegistroArquivoVO obj : objetos) {
            obj.getContaReceberVO().getRegistroArquivoVO().setCodigo(registroArquivoPrm);
            if(obj.getCodigo() == 0) {
            	incluir(obj, usuario);	
            }else {
            	alterar(obj, usuario);
            }
            
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ContaReceberRegistroArquivoVO obj, UsuarioVO usuario) throws Exception {
        try {
            final String sql = "INSERT INTO ContaReceberRegistroArquivo( nrDocumento, nossonumero, valor, valorRecebido, dataVencimento, situacao, "
                    + "matriculaAluno, contaReceber, tipoPessoa, unidadeEnsino, pessoa, observacao, registroArquivo, parceiro, gerarnegociacaorecebimento, motivoRejeicao, contaReceberAgrupada, contaRecebidaDuplicidade, contaRecebidaNegociada) "
                    + " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario); //1 - 13
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                    PreparedStatement sqlInserir = cnctn.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getContaReceberVO().getNrDocumento());
                    sqlInserir.setString(2, obj.getContaReceberVO().getNossoNumero());
                    sqlInserir.setDouble(3, obj.getContaReceberVO().getValor().doubleValue());
                    sqlInserir.setDouble(4, obj.getContaReceberVO().getValorRecebido().doubleValue());
                    sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getContaReceberVO().getDataVencimento()));
                    sqlInserir.setString(6, obj.getContaReceberVO().getSituacao());
                    if (!obj.getContaReceberVO().getMatriculaAluno().getMatricula().equals("")) {
                        sqlInserir.setString(7, obj.getContaReceberVO().getMatriculaAluno().getMatricula());
                    } else {
                        sqlInserir.setNull(7, 0);
                    }
                    if (obj.getContaReceberVO().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(8, obj.getContaReceberVO().getCodigo());
                    } else {
                        sqlInserir.setNull(8, 0);
                    }
                    sqlInserir.setString(9, obj.getContaReceberVO().getTipoPessoa());
                    if (obj.getContaReceberVO().getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(10, obj.getContaReceberVO().getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(10, 0);
                    }
                    if (obj.getContaReceberVO().getPessoa().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(11, obj.getContaReceberVO().getPessoa().getCodigo());
                    } else {
                        sqlInserir.setNull(11, 0);
                    }
                    sqlInserir.setString(12, obj.getContaReceberVO().getObservacao());
                    sqlInserir.setInt(13, obj.getContaReceberVO().getRegistroArquivoVO().getCodigo());
                    if (obj.getContaReceberVO().getParceiroVO().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(14, obj.getContaReceberVO().getParceiroVO().getCodigo());
                    } else {
                        sqlInserir.setNull(14, 0);
                    }
                    sqlInserir.setBoolean(15, obj.getContaReceberVO().isGerarNegociacaoRecebimento());
                    sqlInserir.setString(16, obj.getMotivoRejeicao());
                    sqlInserir.setBoolean(17, obj.getContaReceberAgrupada());
                    sqlInserir.setBoolean(18, obj.getContaRecebidaDuplicidade());
                    sqlInserir.setBoolean(19, obj.getContaRecebidaNegociada());
                    return sqlInserir;
                }
            }, new ResultSetExtractor() {

                public Object extractData(ResultSet rs) throws SQLException, DataAccessException {
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

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void alterar(final ContaReceberRegistroArquivoVO obj, UsuarioVO usuario) throws Exception {
        try {
            try {
                final String sql = "UPDATE ContaReceberRegistroArquivo set nrDocumento=?, nossonumero=?, valor=?, valorRecebido=?, dataVencimento=?, "
                        + "situacao=?, matriculaAluno=?, contaReceber=?, tipoPessoa=?, unidadeEnsino=?, pessoa=?, observacao=?, registroArquivo=?, "
                        + "parceiro=?, gerarnegociacaorecebimento=?, motivoRejeicao=?, contaReceberAgrupada=?, contaRecebidaDuplicidade=?, contaRecebidaNegociada=?  WHERE (codigo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
                getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                    public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                        PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                        sqlAlterar.setString(1, obj.getContaReceberVO().getNrDocumento());
                        sqlAlterar.setString(2, obj.getContaReceberVO().getNossoNumero());
                        sqlAlterar.setDouble(3, obj.getContaReceberVO().getValor().doubleValue());
                        sqlAlterar.setDouble(4, obj.getContaReceberVO().getValorRecebido().doubleValue());
                        sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getContaReceberVO().getDataVencimento()));
                        sqlAlterar.setString(6, obj.getContaReceberVO().getSituacao());
                        if (!obj.getContaReceberVO().getMatriculaAluno().getMatricula().equals("")) {
                            sqlAlterar.setString(7, obj.getContaReceberVO().getMatriculaAluno().getMatricula());
                        } else {
                            sqlAlterar.setNull(7, 0);
                        }
                        if (obj.getContaReceberVO().getCodigo().intValue() != 0) {
                            sqlAlterar.setInt(8, obj.getContaReceberVO().getCodigo());
                        } else {
                            sqlAlterar.setNull(8, 0);
                        }
                        sqlAlterar.setString(9, obj.getContaReceberVO().getTipoPessoa());
                        if (obj.getContaReceberVO().getUnidadeEnsino().getCodigo().intValue() != 0) {
                            sqlAlterar.setInt(10, obj.getContaReceberVO().getUnidadeEnsino().getCodigo().intValue());
                        } else {
                            sqlAlterar.setNull(10, 0);
                        }
                        if (obj.getContaReceberVO().getPessoa().getCodigo().intValue() != 0) {
                            sqlAlterar.setInt(11, obj.getContaReceberVO().getPessoa().getCodigo());
                        } else {
                            sqlAlterar.setNull(11, 0);
                        }
                        sqlAlterar.setString(12, obj.getContaReceberVO().getObservacao());
                        sqlAlterar.setInt(13, obj.getContaReceberVO().getRegistroArquivoVO().getCodigo());
                        if (obj.getContaReceberVO().getParceiroVO().getCodigo().intValue() != 0) {
                            sqlAlterar.setInt(14, obj.getContaReceberVO().getParceiroVO().getCodigo());
                        } else {
                            sqlAlterar.setNull(14, 0);
                        }
                        sqlAlterar.setBoolean(15, obj.getContaReceberVO().isGerarNegociacaoRecebimento());
                        sqlAlterar.setString(16, obj.getMotivoRejeicao());
                        sqlAlterar.setBoolean(17, obj.getContaReceberAgrupada());
                        sqlAlterar.setBoolean(18, obj.getContaRecebidaDuplicidade());
                        sqlAlterar.setBoolean(19, obj.getContaRecebidaNegociada());
                        sqlAlterar.setInt(20, obj.getCodigo().intValue());
                        return sqlAlterar;
                    }
                });
            } finally {
            }
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPorCodigoRegistroArquivo(Integer codigoRegistroArquivo, UsuarioVO usuario) throws Exception{
    	StringBuilder sql = new StringBuilder("DELETE FROM ContaReceberRegistroArquivo where registroArquivo = ? "+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
    	getConexao().getJdbcTemplate().update(sql.toString(), codigoRegistroArquivo);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSituacao(final Integer codigo, final String situacao, UsuarioVO usuario) throws Exception {
        final String sql = "UPDATE ContaReceberRegistroArquivo set situacao=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                PreparedStatement sqlAlterar = cnctn.prepareStatement(sql);
                sqlAlterar.setString(1, situacao);
                sqlAlterar.setInt(2, codigo);
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ContaReceberRegistroArquivoVO obj, UsuarioVO usuario) throws Exception {
        try {
            String sql = "DELETE FROM ContaReceberRegistroArquivo WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    public Integer consultarQtdeContaReceberRegistroArquivoPorRegistroArquivo(Integer registroArquivo, String pessoa, String nossoNumero, String nrDocumento, boolean controlarAcesso, UsuarioVO usuario, Boolean contaRecebidaDuplicidade) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sql = new StringBuilder("SELECT count(distinct crra.codigo) as qtde ");
        sql.append("FROM contaReceberRegistroArquivo crra ");
        sql.append("LEFT JOIN contaReceber on contaReceber.codigo = crra.contaReceber ");               
        sql.append("LEFT JOIN pessoa p ON contaReceber.pessoa = p.codigo ");
        sql.append("LEFT JOIN pessoa resp ON contaReceber.responsavelFinanceiro = resp.codigo ");
        sql.append("LEFT JOIN parceiro parc ON parc.codigo = crra.parceiro ");
        sql.append("LEFT JOIN fornecedor ON fornecedor.codigo = contaReceber.fornecedor ");
        sql.append(" WHERE registroarquivo = ").append(registroArquivo);
        if (contaRecebidaDuplicidade != null) {
        	sql.append(" and crra.contaRecebidaDuplicidade = ").append(contaRecebidaDuplicidade);        	
        }     
        List<String> param = new ArrayList<String>();
        if(Uteis.isAtributoPreenchido(pessoa)) {
        	sql.append(" and (sem_acentos(p.nome) ilike (sem_acentos(?)) ");
        	sql.append(" or sem_acentos(resp.nome) ilike (sem_acentos(?)) ");
        	sql.append(" or sem_acentos(parc.nome) ilike (sem_acentos(?)) ");
        	sql.append(" or sem_acentos(fornecedor.nome) ilike (sem_acentos(?)) ");
        	sql.append(" or sem_acentos(contaReceber.matriculaaluno) ilike (sem_acentos(?)) ");
        	sql.append(" ) ");
        	param.add(pessoa+"%");
        	param.add(pessoa+"%");
        	param.add(pessoa+"%");
        	param.add(pessoa+"%");
        	param.add(pessoa+"%");
        }
        if(Uteis.isAtributoPreenchido(nrDocumento)) {
        	sql.append(" and sem_acentos(crra.nrdocumento) ilike (sem_acentos(?)) ");
        	param.add(nrDocumento+"%");
        }
        if(Uteis.isAtributoPreenchido(nossoNumero)) {
        	sql.append(" and sem_acentos(crra.nossonumero) ilike (sem_acentos(?)) ");
        	param.add(nossoNumero+"%");
        }        
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), param.toArray());
        if (resultado.next()) {
            return resultado.getInt("qtde");
        }
        return 0;
    }

    public List<ContaReceberRegistroArquivoVO> consultaRapidaPorRegistroArquivo(Integer registroArquivo, Integer qtde, Integer inicio,  String pessoa, String nossoNumero, String nrDocumento, boolean controlarAcesso, UsuarioVO usuario, Boolean contaRecebidaDuplicidade) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sql = getSQLPadraoConsultaBasica();
        sql.append(" WHERE crra.registroarquivo = ").append(registroArquivo);
        if (contaRecebidaDuplicidade != null) {
        	sql.append(" and crra.contaRecebidaDuplicidade = ").append(contaRecebidaDuplicidade);
        }
        List<String> param = new ArrayList<String>();
        if(Uteis.isAtributoPreenchido(pessoa)) {
        	sql.append(" and (sem_acentos(p.nome) ilike (sem_acentos(?)) ");
        	sql.append(" or sem_acentos(resp.nome) ilike (sem_acentos(?)) ");
        	sql.append(" or sem_acentos(parc.nome) ilike (sem_acentos(?)) ");
        	sql.append(" or sem_acentos(fornecedor.nome) ilike (sem_acentos(?)) ");
        	sql.append(" or sem_acentos(contaReceber.matriculaaluno) ilike (sem_acentos(?)) ");
        	sql.append(" ) ");
        	param.add(pessoa+"%");
        	param.add(pessoa+"%");
        	param.add(pessoa+"%");
        	param.add(pessoa+"%");
        	param.add(pessoa+"%");
        }
        if(Uteis.isAtributoPreenchido(nrDocumento)) {
        	sql.append(" and sem_acentos(crra.nrdocumento) ilike (sem_acentos(?)) ");
        	param.add(nrDocumento+"%");
        }
        if(Uteis.isAtributoPreenchido(nossoNumero)) {
        	sql.append(" and sem_acentos(crra.nossonumero) ilike (sem_acentos(?)) ");
        	param.add(nossoNumero+"%");
        }
        sql.append(" ORDER BY p.nome, parc.nome ");
        if (qtde != null) {
            sql.append(" LIMIT ").append(qtde);
            if (inicio != null) {
                sql.append(" OFFSET ").append(inicio);
            }
        }
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), param.toArray());
        param =  null;
        return montarDadosConsultaBasica(resultado);
    }

    public List<ContaReceberRegistroArquivoVO> consultaRapidaPorRegistroArquivoDuplicidade(Integer registroArquivo, Integer qtde, Integer inicio, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuffer sql = getSQLPadraoConsultaBasica();
    	sql.append(" WHERE crra.registroarquivo = ").append(registroArquivo);
    	sql.append(" and crra.contaRecebidaDuplicidade = true ");
    	sql.append(" ORDER BY p.nome, parc.nome ");
    	if (qtde != null) {
    		sql.append(" LIMIT ").append(qtde);
    		if (inicio != null) {
    			sql.append(" OFFSET ").append(inicio);
    		}
    	}
    	SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	return montarDadosConsultaBasica(resultado);
    }
	
    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer sql = new StringBuffer(" SELECT distinct crra.codigo as \"crra.codigo\", crra.nrdocumento, crra.nossonumero, crra.valor, crra.valorrecebido, crra.datavencimento, ");
        sql.append(" case when contareceber.situacao is null then crra.situacao	else contareceber.situacao	end as \"crra.situacao\", crra.motivoRejeicao as motivoRejeicao, crra.matriculaaluno, crra.contareceber, crra.tipopessoa, crra.unidadeensino as \"crra.unidadeensino\", ");
        sql.append(" crra.pessoa as \"crra.pessoa\", p.nome as \"p.nome\", crra.parceiro, parc.nome as \"parc.nome\", crra.observacao, crra.registroarquivo, crra.contaReceberAgrupada, ");
        sql.append(" contareceber.valor as \"cr.valor\",contareceber.juro as \"cr.juro\",contareceber.valorrecebido as \"cr.valorrecebido\",contareceber.multa as \"cr.multa\", ");
        sql.append(" contareceber.valordescontorecebido as \"cr.valordescontorecebido\",contareceber.parcela as \"cr.parcela\",contareceber.datavencimento as \"cr.datavencimento\", contareceber.acrescimo as \"cr.acrescimo\",  ");
        sql.append(" contareceber.unidadeensinofinanceira as \"cr.unidadeensinofinanceira\", ");
        sql.append(" parc.cpf as \"parc.cpf\", parc.cnpj as \"parc.cnpj\", crra.gerarnegociacaorecebimento, crra.contaRecebidaDuplicidade, crra.contaRecebidaNegociada, contareceber.matriculaperiodo, ");
        sql.append(" fornecedor.codigo as \"for.codigo\", fornecedor.nome as \"for.nome\" , fornecedor.cnpj as \"for.cnpj\" , fornecedor.cpf as \"for.cpf\",  fornecedor.tipoEmpresa as \"for.tipoEmpresa\", ");
        sql.append(" resp.codigo as \"resp.codigo\", resp.nome as \"resp.nome\", resp.cpf as \"resp.cpf\", negociacaorecebimento.data AS \"nr.dataRecebimento\", registrodetalhe.valorpago, ");        
        sql.append(" case when registrodetalhe.identificacaotituloempresa ilike '%'||contareceber.nossonumero  then false else CASE WHEN registrodetalhe.identificacaotituloempresa is null OR contareceber.nossonumero is null THEN FALSE ELSE true END end as cr_recebidooutronossonumero, ");
        sql.append(" case when registrodetalhe.identificacaotituloempresa ilike '%'||contareceber.nossonumero  then contareceber.nossonumero else registrodetalhe.identificacaotituloempresa end as nossonumerobaixa, ");
        sql.append(" case when registrodetalhe.codigo is null "); 
        sql.append(" then (select rdNaoLocalizado.sacadonome from  registrodetalhe rdNaoLocalizado where rdNaoLocalizado.identificacaotituloempresa  =  crra.nossonumero and rdNaoLocalizado.registroarquivo = crra.registroarquivo and rdNaoLocalizado.valorpago > 0 ) "); 
        sql.append("  else registrodetalhe.sacadonome end as \"registrodetalhe.sacadonome\", ");
        sql.append(" case when registrodetalhe.codigo is null "); 
        sql.append(" then (select rdNaoLocalizado.dataCredito from  registrodetalhe rdNaoLocalizado where rdNaoLocalizado.identificacaotituloempresa  =  crra.nossonumero and rdNaoLocalizado.registroarquivo = crra.registroarquivo and rdNaoLocalizado.valorpago > 0 ) "); 
        sql.append("  else registrodetalhe.dataCredito end as \"registrodetalhe.dataCredito\" "); 
        sql.append("  "); 
        sql.append(" FROM contaReceberRegistroArquivo crra ");
        sql.append(" left join registrodetalhe on crra.contaReceber = registrodetalhe.codigocontareceber and registrodetalhe.codigo = (select codigo from registrodetalhe rd where crra.contaReceber = rd.codigocontareceber and registrodetalhe.codigo = rd.codigo and rd.valorpago > 0 limit 1 ) ");
        sql.append(" LEFT JOIN contaReceber on contaReceber.codigo = crra.contaReceber ");
        sql.append(" LEFT JOIN contaReceberNegociacaoRecebimento ON contaReceberNegociacaoRecebimento.contaReceber = contaReceber.codigo ");
        sql.append(" LEFT JOIN negociacaorecebimento ON negociacaorecebimento.codigo = contaReceberNegociacaoRecebimento.negociacaorecebimento ");
        sql.append(" LEFT JOIN matricula m ON crra.matriculaaluno = m.matricula ");
        sql.append(" LEFT JOIN pessoa p ON m.aluno = p.codigo ");
        sql.append(" LEFT JOIN pessoa resp ON contaReceber.responsavelFinanceiro = resp.codigo ");
        sql.append(" LEFT JOIN parceiro parc ON parc.codigo = crra.parceiro ");
        sql.append(" LEFT JOIN fornecedor ON fornecedor.codigo = contaReceber.fornecedor ");
        return sql;
    }

    public List<ContaReceberRegistroArquivoVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
        List<ContaReceberRegistroArquivoVO> vetResultado = new ArrayList<ContaReceberRegistroArquivoVO>(0);
        while (tabelaResultado.next()) {
            ContaReceberRegistroArquivoVO obj = new ContaReceberRegistroArquivoVO();
            montarDadosBasico(obj, tabelaResultado);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    private void montarDadosBasico(ContaReceberRegistroArquivoVO obj, SqlRowSet dadosSQL) throws Exception {
        obj.setCodigo(dadosSQL.getInt("crra.codigo"));
        obj.getContaReceberVO().setMatriculaPeriodo(dadosSQL.getInt("matriculaperiodo"));
        obj.getContaReceberVO().setTurma(getFacadeFactory().getTurmaFacade().consultarTurmaDoAlunoPorMatriculaPeriodo(obj.getContaReceberVO().getMatriculaPeriodo(), Uteis.NIVELMONTARDADOS_COMBOBOX, new UsuarioVO()));
        obj.getContaReceberVO().setNrDocumento(dadosSQL.getString("nrdocumento"));
        obj.getContaReceberVO().setNossoNumero(dadosSQL.getString("nossonumero"));
        obj.getContaReceberVO().setValor(dadosSQL.getDouble("valor"));
        obj.getContaReceberVO().setValorRecebido(dadosSQL.getDouble("valorRecebido"));
        obj.getContaReceberVO().setDataVencimento(dadosSQL.getDate("dataVencimento"));
        obj.getContaReceberVO().setDataCredito(dadosSQL.getDate("registrodetalhe.dataCredito"));
        obj.getContaReceberVO().setSituacao(dadosSQL.getString("crra.situacao"));
        obj.setMotivoRejeicao(dadosSQL.getString("motivoRejeicao"));
        obj.setContaReceberAgrupada(dadosSQL.getBoolean("contaReceberAgrupada"));
        obj.getContaReceberVO().setCodigo(dadosSQL.getInt("contareceber"));
        obj.getContaReceberVO().setTipoPessoa(dadosSQL.getString("tipoPessoa"));
    	obj.getContaReceberVO().setCr_recebidooutronossonumero(dadosSQL.getBoolean("cr_recebidooutronossonumero"));
    	obj.getContaReceberVO().setNossoNumeroOriginouBaixa(dadosSQL.getString("nossonumerobaixa"));
        obj.getContaReceberVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("crra.unidadeensino"));
        obj.getContaReceberVO().getUnidadeEnsinoFinanceira().setCodigo(dadosSQL.getInt("cr.unidadeensinofinanceira"));
        obj.getContaReceberVO().setObservacao(dadosSQL.getString("observacao"));
        obj.getContaReceberVO().setGerarNegociacaoRecebimento(dadosSQL.getBoolean("gerarnegociacaorecebimento"));
		obj.setContaRecebidaDuplicidade(dadosSQL.getBoolean("contaRecebidaDuplicidade"));		
		obj.setContaRecebidaNegociada(dadosSQL.getBoolean("contaRecebidaNegociada"));		
        obj.getContaReceberVO().getRegistroArquivoVO().setCodigo(dadosSQL.getInt("registroarquivo"));
        obj.getContaReceberVO().getMatriculaAluno().setMatricula(dadosSQL.getString("matriculaaluno"));
        obj.getContaReceberVO().getPessoa().setCodigo(dadosSQL.getInt("crra.pessoa"));
        obj.getContaReceberVO().getPessoa().setNome(dadosSQL.getString("p.nome"));
    	obj.getContaReceberVO().getMatriculaAluno().getAluno().setCodigo(obj.getContaReceberVO().getPessoa().getCodigo());
        obj.getContaReceberVO().getMatriculaAluno().getAluno().setNome(obj.getContaReceberVO().getPessoa().getNome());
        if (obj.getContaReceberVO().getSituacao().equals(SituacaoContaReceber.RECEBIDO.getValor())) {
	        //Monta os dados de Conta Receber para ser usado o relatorio de Controle de Cobrança        
	        obj.getContaReceberVO().setValor(dadosSQL.getDouble("cr.valor"));
	        obj.getContaReceberVO().setJuro(dadosSQL.getDouble("cr.juro"));	        
	        obj.getContaReceberVO().setMulta(dadosSQL.getDouble("cr.multa"));
	        obj.getContaReceberVO().setValorDescontoRecebido(dadosSQL.getDouble("cr.valordescontorecebido"));
	        obj.getContaReceberVO().setParcela(dadosSQL.getString("cr.parcela"));
	        obj.getContaReceberVO().setDataVencimento(dadosSQL.getDate("cr.datavencimento"));
	        obj.getContaReceberVO().setAcrescimo(dadosSQL.getDouble("cr.acrescimo"));
	        obj.setDataPrimeiroPagamento(dadosSQL.getDate("nr.dataRecebimento"));
	        obj.setValorPago(dadosSQL.getDouble("valorpago"));
	        if(!obj.getContaRecebidaDuplicidade()) {
	        	obj.getContaReceberVO().setValorRecebido(dadosSQL.getDouble("cr.valorrecebido"));
	        }
        }
		if (obj.getContaReceberVO().getTipoPessoa().equals("RF")) {
			obj.getContaReceberVO().getResponsavelFinanceiro().setCodigo(dadosSQL.getInt("resp.codigo"));
			obj.getContaReceberVO().getResponsavelFinanceiro().setNome(dadosSQL.getString("resp.nome"));
			obj.getContaReceberVO().getResponsavelFinanceiro().setCPF(dadosSQL.getString("resp.cpf"));
		} else if (obj.getContaReceberVO().getTipoPessoa().equals("PA")) {
			obj.getContaReceberVO().getParceiroVO().setCodigo(dadosSQL.getInt("parceiro"));
			obj.getContaReceberVO().getParceiroVO().setNome(dadosSQL.getString("parc.nome"));
			obj.getContaReceberVO().getParceiroVO().setCPF(dadosSQL.getString("parc.cpf"));
			obj.getContaReceberVO().getParceiroVO().setCNPJ(dadosSQL.getString("parc.cnpj"));
		} else if (obj.getContaReceberVO().getTipoPessoa().equals("FO")) {
			obj.getContaReceberVO().getFornecedor().setCodigo(dadosSQL.getInt("for.codigo"));
			obj.getContaReceberVO().getFornecedor().setNome(dadosSQL.getString("for.nome"));
			obj.getContaReceberVO().getFornecedor().setCPF(dadosSQL.getString("for.cpf"));
			obj.getContaReceberVO().getFornecedor().setCNPJ(dadosSQL.getString("for.cnpj"));
			obj.getContaReceberVO().getFornecedor().setTipoEmpresa(dadosSQL.getString("for.tipoEmpresa"));    	
		}else if (obj.getContaReceberVO().getObservacao().equals("Conta Não Localizada!") && !Uteis.isAtributoPreenchido(obj.getContaReceberVO().getPessoa().getCodigo())) {
			obj.getContaReceberVO().setTipoPessoa(dadosSQL.getString("tipoPessoa"));
			obj.getContaReceberVO().getPessoa().setNome(dadosSQL.getString("registrodetalhe.sacadonome"));
		}
    }
    
     /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um objeto da classe <code>BoletoBancarioRelVO</code>.
     *
     * @return O objeto da classe <code>BoletoBancarioRelVO</code> com os dados devidamente montados.
     */
//    public void montarDados(SqlRowSet dadosSQL, ContaReceberRegistroArquivoVO obj) throws Exception {        
//        obj.getContaReceberVO().setCodigo(dadosSQL.getInt("contareceber_codigo"));
//        obj.getContaReceberVO().setCodigoBarra(dadosSQL.getString("contareceber_codigobarra"));
//        obj.getContaReceberVO().getMatriculaAluno().setMatricula(dadosSQL.getString("contareceber_matriculaaluno"));
//        obj.getContaReceberVO().getMatriculaAluno().getAluno().setCodigo(dadosSQL.getInt("matricula_aluno"));
//        obj.getContaReceberVO().setData(dadosSQL.getDate("contareceber_data"));
//        obj.getContaReceberVO().setTipoOrigem(dadosSQL.getString("contareceber_tipoorigem"));
//        obj.getContaReceberVO().setCodOrigem(dadosSQL.getString("contareceber_codorigem"));
//        obj.getContaReceberVO().setTipoPessoa(dadosSQL.getString("contareceber_tipopessoa"));
//        obj.getContaReceberVO().setMatriculaPeriodo(dadosSQL.getInt("contareceber_matriculaperiodo"));
//        obj.getContaReceberVO().getPessoa().setCodigo(dadosSQL.getInt("contareceber_pessoa"));
//        obj.getContaReceberVO().getCandidato().setCodigo(dadosSQL.getInt("contareceber_candidato"));
//        obj.getContaReceberVO().getFuncionario().setCodigo(dadosSQL.getInt("contareceber_funcionario"));
//        obj.getContaReceberVO().getParceiroVO().setCodigo(dadosSQL.getInt("contareceber_parceiro"));
//        obj.getContaReceberVO().getFornecedor().setCodigo(dadosSQL.getInt("contareceber_fornecedor"));
//        obj.getContaReceberVO().getCentroReceita().setCodigo(dadosSQL.getInt("contareceber_centroreceita"));
//        obj.getContaReceberVO().getConvenio().setCodigo(dadosSQL.getInt("contareceber_convenio"));
//        
//        obj.getContaReceberVO().setDescontoProgressivoUtilizado(FaixaDescontoProgressivo.getEnum(dadosSQL.getString("descontoprogressivoutilizado")));
//        obj.getContaReceberVO().setImpressaoBoletoRealizada(dadosSQL.getBoolean("impressaoBoletoRealizada"));
//        obj.getContaReceberVO().setDescricaoPagamento(dadosSQL.getString("contareceber_descricaopagamento"));
//        obj.getContaReceberVO().setDataVencimento(dadosSQL.getDate("contareceber_datavencimento"));
//        obj.getContaReceberVO().setValor(dadosSQL.getDouble("contareceber_valor"));
//        obj.getContaReceberVO().setValorRecebido(dadosSQL.getDouble("contareceber_valorrecebido"));
//        obj.getContaReceberVO().setValorDesconto(dadosSQL.getDouble("contareceber_valordesconto"));
//        obj.getContaReceberVO().setAcrescimo(dadosSQL.getDouble("contareceber_acrescimo"));
//        obj.getContaReceberVO().setTipoDesconto(dadosSQL.getString("contareceber_tipodesconto"));
//        obj.getContaReceberVO().setJuro(dadosSQL.getDouble("contareceber_juro"));
//        obj.getContaReceberVO().setJuroPorcentagem(dadosSQL.getDouble("contareceber_juroporcentagem"));
//        obj.getContaReceberVO().setMulta(dadosSQL.getDouble("contareceber_multa"));
//        obj.getContaReceberVO().setMultaPorcentagem(dadosSQL.getDouble("contareceber_multaporcentagem"));
//        obj.getContaReceberVO().setNrDocumento(dadosSQL.getString("contareceber_nrdocumento"));
//        obj.getContaReceberVO().setNossoNumero(dadosSQL.getString("contareceber_nossonumero"));
//        obj.getContaReceberVO().setSituacao(dadosSQL.getString("contareceber_situacao"));
//        obj.getContaReceberVO().setParcela(dadosSQL.getString("contareceber_parcela"));
//        obj.getContaReceberVO().setOrigemNegociacaoReceber(dadosSQL.getInt("contareceber_origemnegociacaoreceber"));
//        obj.getContaReceberVO().setValorDescontoLancadoRecebimento(dadosSQL.getDouble("valorDescontoLancadoRecebimento"));
//        obj.getContaReceberVO().setValorCalculadoDescontoLancadoRecebimento(dadosSQL.getDouble("valorCalculadoDescontoLancadoRecebimento"));
//        obj.getContaReceberVO().setTipoDescontoLancadoRecebimento(dadosSQL.getString("tipoDescontoLancadoRecebimento"));
//        obj.getContaReceberVO().setValorDescontoAlunoJaCalculado(dadosSQL.getDouble("valorDescontoAlunoJaCalculado"));
//        obj.getContaReceberVO().setImpressaoBoletoRealizada(dadosSQL.getBoolean("impressaoBoletoRealizada"));
//        obj.getContaReceberVO().setTituloImportadoSistemaLegado(dadosSQL.getBoolean("tituloImportadoSistemaLegado"));
//        obj.getContaReceberVO().setJustificativaDesconto(dadosSQL.getString("justificativaDesconto"));
//        obj.getContaReceberVO().setRecebimentoBancario((Boolean) dadosSQL.getObject("recebimentoBancario"));
//        obj.getContaReceberVO().setContaReceberAgrupada(dadosSQL.getInt("contareceberagrupada"));
//        
//        obj.getContaReceberVO().setContaCorrente(dadosSQL.getInt("contareceber_contacorrente"));
//        obj.getContaReceberVO().setValorDescontoInstituicao(dadosSQL.getDouble("contareceber_descontoinstituicao"));
//        obj.getContaReceberVO().setValorDescontoConvenio(dadosSQL.getDouble("contareceber_descontoconvenio"));
//        obj.getContaReceberVO().setUsaDescontoCompostoPlanoDesconto(dadosSQL.getBoolean("contareceber_usadescontocompostoplanodesconto"));
//        
//        obj.getContaReceberVO().getMatriculaAluno().getPlanoFinanceiroAluno().setDescontoValidoAteDataParcela(dadosSQL.getBoolean("descontoValidoatedataparcela"));
//        obj.getContaReceberVO().getMatriculaAluno().getPlanoFinanceiroAluno().setCodigo(dadosSQL.getInt("planofinanceiroaluno_codigo"));
//        
//        obj.getContaReceberVO().setLinhaDigitavelCodigoBarras(dadosSQL.getString("contareceber_linhadigitavelcodigobarras"));
//        obj.getContaReceberVO().getDescontoProgressivo().setCodigo(dadosSQL.getInt("contareceber_descontoprogressivo"));
//        obj.getContaReceberVO().getDescontoProgressivo().setDiaLimite1(dadosSQL.getInt("descontoprogressivo_dialimite1"));
//        obj.getContaReceberVO().getDescontoProgressivo().setDiaLimite2(dadosSQL.getInt("descontoprogressivo_dialimite2"));
//        obj.getContaReceberVO().getDescontoProgressivo().setDiaLimite3(dadosSQL.getInt("descontoprogressivo_dialimite3"));
//        obj.getContaReceberVO().getDescontoProgressivo().setDiaLimite4(dadosSQL.getInt("descontoprogressivo_dialimite4"));
//        obj.getContaReceberVO().getDescontoProgressivo().setPercDescontoLimite1(dadosSQL.getDouble("descontoprogressivo_percdescontolimite1"));
//        obj.getContaReceberVO().getDescontoProgressivo().setPercDescontoLimite2(dadosSQL.getDouble("descontoprogressivo_percdescontolimite2"));
//        obj.getContaReceberVO().getDescontoProgressivo().setPercDescontoLimite3(dadosSQL.getDouble("descontoprogressivo_percdescontolimite3"));
//        obj.getContaReceberVO().getDescontoProgressivo().setPercDescontoLimite4(dadosSQL.getDouble("descontoprogressivo_percdescontolimite4"));
//        obj.getContaReceberVO().getDescontoProgressivo().setValorDescontoLimite1(dadosSQL.getDouble("descontoprogressivo_valordescontolimite1"));
//        obj.getContaReceberVO().getDescontoProgressivo().setValorDescontoLimite2(dadosSQL.getDouble("descontoprogressivo_valordescontolimite2"));
//        obj.getContaReceberVO().getDescontoProgressivo().setValorDescontoLimite3(dadosSQL.getDouble("descontoprogressivo_valordescontolimite3"));
//        obj.getContaReceberVO().getDescontoProgressivo().setValorDescontoLimite4(dadosSQL.getDouble("descontoprogressivo_valordescontolimite4"));
//        obj.getContaReceberVO().getUnidadeEnsino().setCodigo(dadosSQL.getInt("contareceber_unidadeensino"));
//        
//        
//        obj.getContaReceberVO().setTipoBoleto(dadosSQL.getString("tiboboletocontareceber"));
//        obj.setDiasVariacaoDataVencimento(dadosSQL.getInt("diasVariacaoDataVencimento"));
//        obj.getContaReceberVO().setOrdemConvenio(dadosSQL.getInt("contareceber_ordemConvenio"));
//        obj.getContaReceberVO().setOrdemConvenioValorCheio(dadosSQL.getBoolean("contareceber_ordemConvenioValorCheio"));
//        obj.getContaReceberVO().setOrdemDescontoAluno(dadosSQL.getInt("contareceber_ordemDescontoAluno"));
//        obj.getContaReceberVO().setOrdemDescontoAlunoValorCheio(dadosSQL.getBoolean("contareceber_ordemDescontoAlunoValorCheio"));
//        obj.getContaReceberVO().setOrdemDescontoProgressivo(dadosSQL.getInt("contareceber_ordemDescontoProgressivo"));
//        obj.getContaReceberVO().setOrdemDescontoProgressivoValorCheio(dadosSQL.getBoolean("contareceber_ordemDescontoProgressivoValorCheio"));
//        obj.getContaReceberVO().setOrdemPlanoDesconto(dadosSQL.getInt("contareceber_ordemPlanoDesconto"));
//        obj.getContaReceberVO().setOrdemPlanoDescontoValorCheio(dadosSQL.getBoolean("contareceber_ordemPlanoDescontoValorCheio"));
//        obj.getContaReceberVO().getResponsavelFinanceiro().setCodigo(dadosSQL.getInt("contareceber_responsavelfinanceiro"));
//        
//        
//        obj.getContaReceberVO().getMatriculaAluno().setSituacao(dadosSQL.getString("situacaoMatricula"));
//        
//        
//        
//    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public boolean consultSeExisteContaReceberRegistroArquivoComSituacaoReceber(ControleCobrancaVO controleCobranca, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
		StringBuilder sqlStr = new StringBuilder(" select count (contareceberregistroarquivo.codigo) QTDE ");
		sqlStr.append(" from contareceberregistroarquivo ");
		sqlStr.append(" inner join registroarquivo  on registroarquivo.codigo  = contareceberregistroarquivo.registroarquivo ");
		sqlStr.append(" inner join controlecobranca on controlecobranca.registroarquivo  = registroarquivo.codigo");
		sqlStr.append(" where  controlecobranca.codigo = ").append(controleCobranca.getCodigo()).append(" ");
		sqlStr.append(" and contareceberregistroarquivo.situacao  = 'AR' ");
		sqlStr.append(" and contareceberregistroarquivo.observacao  = 'Conta Localizada!' ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
		return Uteis.isAtributoPreenchido(tabelaResultado, Uteis.QTDE,TipoCampoEnum.INTEIRO);
	}
    
    @Override
    public Boolean verificarExistenciaContaReceberRecebidaDuplicidade(Integer contaReceber, Integer registroArquivoNaoConsiderar, Date dataRecebimento){
    	StringBuilder sql =  new StringBuilder("select contareceberregistroarquivo.codigo from contareceberregistroarquivo inner join contareceber on contareceber.codigo = contareceberregistroarquivo.contareceber  where registroarquivo  != ? and contareceber.codigo = ? and contareceber.situacao = 'RE' limit 1 ");
    	return getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), registroArquivoNaoConsiderar, contaReceber).next();
    }
    
	@Override
	public void montarDadosAtualizacaoContaReceber(List<ContaReceberRegistroArquivoVO> contaReceberRegistroArquivoVOs, UsuarioVO usuarioVO) throws Exception {
		for (ContaReceberRegistroArquivoVO contaReceberRegistroArquivoVO : contaReceberRegistroArquivoVOs) {
			if (contaReceberRegistroArquivoVO.getContaReceberVO().getCodigo() != 0) {
				try {
					ConfiguracaoFinanceiroVO configFinVO = getFacadeFactory().getConfiguracaoFinanceiroFacade().consultarPorUnidadeEnsino(contaReceberRegistroArquivoVO.getContaReceberVO().getUnidadeEnsinoFinanceira().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuarioVO);
					getFacadeFactory().getContaReceberFacade().carregarDados(contaReceberRegistroArquivoVO.getContaReceberVO(), configFinVO, usuarioVO);
				} catch (Exception e) {
					contaReceberRegistroArquivoVO.getContaReceberVO().setObservacao("Conta Não Localizada!");					
				}
			}
		}
	}
	
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void alterarContaReceberRegistroArquivoAReceberJaRecebida(Integer contaReceber, UsuarioVO usuario) throws Exception {
        try {
            try {
                final StringBuilder sqlStr = new StringBuilder();
                sqlStr.append("update contareceberregistroarquivo set situacao = 'RE' , gerarnegociacaorecebimento =  false, observacao = 'Conta já Recebida!' from ( ");
                sqlStr.append(" select contareceberregistroarquivo.codigo, contareceberregistroarquivo.contareceber ");
                sqlStr.append(" from contareceberregistroarquivo ");
                sqlStr.append(" inner join contareceber on contareceber.codigo = contareceberregistroarquivo.contareceber");
                sqlStr.append(" where contareceber.situacao  = 'RE'");
                sqlStr.append(" and contareceberregistroarquivo.situacao = 'AR'");
                sqlStr.append(" and contareceberregistroarquivo.gerarnegociacaorecebimento");
                sqlStr.append(" and contareceber.codigo  = ?");
                sqlStr.append(" ) as t where t.codigo = contareceberregistroarquivo.codigo;").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));             
                getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                    public PreparedStatement createPreparedStatement(Connection cnctn) throws SQLException {
                        PreparedStatement sqlAlterar = cnctn.prepareStatement(sqlStr.toString());
                        sqlAlterar.setInt(1, contaReceber);
                        return sqlAlterar;
                    }
                });
            } finally {
            }
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Throwable.class, propagation = Propagation.REQUIRED)
    public void removerVinculoComMatricula(String matricula, UsuarioVO usuario) throws Exception {
    	getConexao().getJdbcTemplate().update("update contareceberregistroarquivo set matriculaaluno = null where matriculaaluno = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), matricula);
}
    
    
    @Override
   	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
   	public void alterarUnidadeEnsino (TurmaVO turmaVO, boolean validarAcesso, UsuarioVO usuarioVO) throws Exception {
   		 final StringBuilder sqlStr = new StringBuilder();
   		 sqlStr.append(" UPDATE contareceberregistroarquivo SET unidadeensino  = t.unidadeensino  FROM ( ");
   		 sqlStr.append("SELECT ");
   		 sqlStr.append(" contareceber.codigo AS contareceber,turma.unidadeensino  ");
   		 sqlStr.append("FROM contareceber ");
   		 sqlStr.append(" INNER JOIN contareceberregistroarquivo ON contareceber.codigo = contareceberregistroarquivo.contareceber ");
   		 sqlStr.append(" INNER JOIN turma   					ON contareceber.turma = turma.codigo ");
   		 sqlStr.append("WHERE turma.codigo = ? AND contareceberregistroarquivo.unidadeensino is not null ");
   		 sqlStr.append(") AS t WHERE contareceberregistroarquivo.contareceber = t.contareceber;");
   		 sqlStr.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

   		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
   			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
   				PreparedStatement sqlAlterar = arg0.prepareStatement(sqlStr.toString());
   				int i = 0;
   				Uteis.setValuePreparedStatement(turmaVO.getCodigo(), ++i, sqlAlterar);
   				return sqlAlterar;
   			}
   		});
   	}
       
    
}
