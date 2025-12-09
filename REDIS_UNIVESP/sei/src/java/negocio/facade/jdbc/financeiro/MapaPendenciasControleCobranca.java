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

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.PlanoDescontoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.ConfiguracaoFinanceiroVO;
import negocio.comuns.financeiro.ContaReceberVO;
import negocio.comuns.financeiro.MapaPendenciasControleCobrancaVO;
import negocio.comuns.financeiro.enumerador.TipoContaCorrenteEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.financeiro.MapaPendenciasControleCobrancaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class MapaPendenciasControleCobranca extends ControleAcesso implements MapaPendenciasControleCobrancaInterfaceFacade {

    protected static String idEntidade;

    public MapaPendenciasControleCobranca() throws Exception {
        super();
        setIdEntidade("MapaPendenciasControleCobranca");
    }

    public MapaPendenciasControleCobrancaVO novo() throws Exception {
        MapaPendenciasControleCobranca.incluir(getIdEntidade());
        MapaPendenciasControleCobrancaVO obj = new MapaPendenciasControleCobrancaVO();
        return obj;
    }

    public void validarDados(MapaPendenciasControleCobrancaVO mapaPendenciasControleCobrancaVO) throws Exception {
//        if (mapaPendenciasControleCobrancaVO.getMatricula().getMatricula().equals("")) {
//            throw new ConsistirException("Informe a Matrícula.");
//        }
        if (mapaPendenciasControleCobrancaVO.getContaReceber().getCodigo().equals(0)) {
            throw new ConsistirException("Informe a Conta Receber.");
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final MapaPendenciasControleCobrancaVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
//			MapaPendenciasControleCobranca.incluir(getIdEntidade());
            final String sql = "INSERT INTO mapaPendenciasControleCobranca (matricula, contaReceber, valorDiferenca, selecionado, dataPagamento, dataProcessamento) VALUES (?, ?, ?, ?, ?, ?) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (!obj.getMatricula().getMatricula().equals("")) {
                    	sqlInserir.setString(1, obj.getMatricula().getMatricula());
                    } else {
                    	sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setInt(2, obj.getContaReceber().getCodigo());
                    sqlInserir.setDouble(3, obj.getValorDiferenca());
                    sqlInserir.setBoolean(4, obj.getSelecionado());
                    sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataPagamento()));
                    sqlInserir.setDate(6, Uteis.getDataJDBC(obj.getDataProcessamento()));
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
    public void alterar(final MapaPendenciasControleCobrancaVO obj, UsuarioVO usuario) throws Exception {
        try {
            validarDados(obj);
            MapaPendenciasControleCobranca.alterar(getIdEntidade());
            final String sql = "UPDATE MapaPendenciasControleCobranca set matricula=?, contaReceber=?, selecionado=?, dataPagamento=?, dataProcessamento=? WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getMatricula().getMatricula());
                    sqlAlterar.setInt(2, obj.getContaReceber().getCodigo());
                    sqlAlterar.setBoolean(3, obj.getSelecionado());
                    sqlAlterar.setDate(4, Uteis.getDataJDBC(obj.getDataPagamento()));
                    sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataProcessamento()));
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(MapaPendenciasControleCobrancaVO obj, UsuarioVO usuario) throws Exception {
        try {
//			MapaPendenciasControleCobranca.excluir(getIdEntidade());
            String sql = "DELETE FROM MapaPendenciasControleCobranca WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPorContaReceber(ContaReceberVO obj, UsuarioVO usuario) throws Exception {
        try {
            String sql = "DELETE FROM MapaPendenciasControleCobranca WHERE ((contaReceber = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPorMatriculaPeriodoContasNaoPagasENaoNegociadas(Integer matriculaPeriodo, UsuarioVO usuario) throws Exception {
        try {
            String sql = "DELETE FROM mapapendenciascontrolecobranca WHERE contareceber IN(select contareceber.codigo from contareceber where matriculaperiodo = ? and situacao <> 'RE' and situacao <> 'NE' and parcela not like '%N%')"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{matriculaPeriodo});
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSelecionado(Integer codigo, Boolean selecionado, UsuarioVO usuario) throws Exception {
        try {
            String sql = "UPDATE MapaPendenciasControleCobranca SET selecionado = ? WHERE (codigo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{selecionado, codigo});
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarSelecionadoPorRegistroArquivo(Integer registroArquivo, Boolean selecionado, UsuarioVO usuario) throws Exception {
        try {
            String sql = "UPDATE MapaPendenciasControleCobranca SET selecionado = ? WHERE MapaPendenciasControleCobranca.contaReceber in("
                       + "SELECT contaReceber FROM contaReceberRegistroArquivo WHERE registroArquivo =? )"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
            getConexao().getJdbcTemplate().update(sql, new Object[]{selecionado, registroArquivo});
        } catch (Exception e) {
            throw e;
        }
    }

    public List consultarPorMatricula(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        String sqlStr = "SELECT * FROM MapaPendenciasControleCobranca WHERE UPPER(matricula) = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{valorConsulta.toUpperCase()});
        try {
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
        } finally {
            sqlStr = null;
        }
    }

    public List<MapaPendenciasControleCobrancaVO> consultarPorListaContaReceber(List<ContaReceberVO> contaReceberVOs, ConfiguracaoFinanceiroVO confFinanVO, UsuarioVO usuarioLogado) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT distinct mpcc.codigo, mpcc.valorDiferenca, mpcc.dataPagamento, mpcc.dataProcessamento, m.matricula, mpcc.contareceber, cr.situacao, cr.valor, cr.matriculaperiodo, ");
        sqlStr.append("cr.valorrecebido, cr.valordescontorecebido, cr.valorDescontoAlunoJaCalculado, cr.descontoConvenio, cr.descontoInstituicao, cr.valorDescontoProgressivo, ");
        sqlStr.append("cr.datavencimento, cr.tipopessoa, p.codigo as \"p.codigo\", p.nome, cr.parcela, cr.parceiro, parc.nome as \"parc.nome\", parc.cpf as \"parc.cpf\", parc.cnpj as \"parc.cnpj\", ");
        sqlStr.append(" p.funcionario, p.professor, p.candidato, p.possuiacessovisaopais, p.membrocomunidade, p.requisitante, ");
        sqlStr.append("cr.codorigem, m.unidadeensino, mpcc.selecionado, ");
        sqlStr.append("resp.codigo as \"resp.codigo\", resp.nome as \"resp.nome\", resp.cpf as \"resp.cpf\" ");
        sqlStr.append("FROM MapaPendenciasControleCobranca mpcc ");
        sqlStr.append("LEFT JOIN matricula m ON mpcc.matricula = m.matricula ");
        sqlStr.append("LEFT JOIN pessoa p ON m.aluno = p.codigo ");
        sqlStr.append("LEFT JOIN contareceber cr ON mpcc.contareceber = cr.codigo ");
        sqlStr.append("LEFT JOIN parceiro parc ON parc.codigo = cr.parceiro ");
        sqlStr.append("LEFT JOIN pessoa resp ON cr.responsavelFinanceiro = resp.codigo ");
        sqlStr.append("WHERE 1 = 1 ");
//        for (ContaReceberVO contaReceberVO : contaReceberVOs) {
//            sqlStr.append("AND contareceber = ").append(contaReceberVO.getCodigo()).append(" ");
//        }
        if(!contaReceberVOs.isEmpty()){
            sqlStr.append("AND contareceber in(");
            for (ContaReceberVO contaReceberVO : contaReceberVOs) {
                if (contaReceberVO.getCodigo() != 0) {
                    sqlStr.append(contaReceberVO.getCodigo());
                    if(!contaReceberVO.getCodigo().equals(contaReceberVOs.get(contaReceberVOs.size()-1).getCodigo())){
                        sqlStr.append(", ");
                    }
                }
            }
            sqlStr.append(") ");
        }
        sqlStr.append(" ORDER BY p.nome, parc.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        try {
            return montarDadosConsultaTelaControleCobranca(tabelaResultado);
        } finally {
            sqlStr = null;
        }
    }

    public List<MapaPendenciasControleCobrancaVO> consultarPorContaReceberRegistroArquivoSelecionado(Integer registroArquivo, Integer qtde, Integer inicio, Boolean selecionado, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder(" SELECT distinct mpcc.codigo, mpcc.valorDiferenca, mpcc.dataPagamento, mpcc.dataProcessamento, m.matricula, mpcc.contareceber, cr.situacao, cr.valor, cr.matriculaperiodo, ");
        sqlStr.append(" cr.valorrecebido, cr.valordescontorecebido, cr.valorDescontoAlunoJaCalculado, cr.descontoConvenio, cr.descontoInstituicao, cr.valorDescontoProgressivo, ");
        sqlStr.append(" cr.datavencimento, cr.tipopessoa, p.codigo as \"p.codigo\", p.nome, p.aluno, cr.parcela, cr.parceiro, parc.nome as \"parc.nome\", parc.cpf as \"parc.cpf\", parc.cnpj as \"parc.cnpj\", ");
        sqlStr.append(" p.funcionario, p.professor, p.candidato, p.possuiacessovisaopais, p.membrocomunidade, p.requisitante, ");
        sqlStr.append(" cr.codorigem, m.unidadeensino, mpcc.selecionado, ");
        sqlStr.append(" fornecedor.codigo as \"for.codigo\", fornecedor.nome as \"for.nome\" , fornecedor.cnpj as \"for.cnpj\" , fornecedor.cpf as \"for.cpf\",  fornecedor.tipoEmpresa as \"for.tipoEmpresa\", ");
        sqlStr.append(" resp.codigo as \"resp.codigo\", resp.nome as \"resp.nome\", resp.cpf as \"resp.cpf\" ");
        sqlStr.append(" FROM MapaPendenciasControleCobranca mpcc ");
        sqlStr.append(" LEFT JOIN matricula m ON mpcc.matricula = m.matricula ");
        sqlStr.append(" LEFT JOIN pessoa p ON m.aluno = p.codigo ");
        sqlStr.append(" LEFT JOIN contareceber cr ON mpcc.contareceber = cr.codigo ");
        sqlStr.append(" LEFT JOIN pessoa resp ON cr.responsavelFinanceiro = resp.codigo ");
        sqlStr.append(" LEFT JOIN parceiro parc ON parc.codigo = cr.parceiro ");
        sqlStr.append(" LEFT JOIN fornecedor ON fornecedor.codigo = cr.fornecedor ");
        
        sqlStr.append("WHERE cr.codigo in(");
        sqlStr.append("SELECT contaReceber FROM contaReceberRegistroArquivo ");
        sqlStr.append("WHERE registroArquivo = ").append(registroArquivo).append(") ");
        if(selecionado!=null){
            sqlStr.append("AND mpcc.selecionado = ").append(selecionado).append(" ");
        }
        sqlStr.append("ORDER BY p.nome, parc.nome ");
        if (qtde != null) {
            sqlStr.append(" LIMIT ").append(qtde);
            if (inicio != null) {
                sqlStr.append(" OFFSET ").append(inicio);
            }
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        try {
            return montarDadosConsultaTelaControleCobranca(tabelaResultado);
        } finally {
            sqlStr = null;
        }
    }

    public Integer consultarQtdeMapaPendenciaPorRegistroArquivo(Integer registroArquivo, UsuarioVO usuario) throws Exception {
        StringBuilder sqlStr = new StringBuilder("SELECT count(distinct mpcc.codigo) as qtde ");
        sqlStr.append("FROM MapaPendenciasControleCobranca mpcc ");
        sqlStr.append("LEFT JOIN matricula m ON mpcc.matricula = m.matricula ");
        sqlStr.append("LEFT JOIN pessoa p ON m.aluno = p.codigo ");
        sqlStr.append("LEFT JOIN contareceber cr ON mpcc.contareceber = cr.codigo ");
        sqlStr.append("WHERE cr.codigo in(");
        sqlStr.append("SELECT contaReceber FROM contaReceberRegistroArquivo ");
        sqlStr.append("WHERE registroArquivo = ").append(registroArquivo).append(") ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        if (tabelaResultado.next()) {
            return tabelaResultado.getInt("qtde");
        }
        return 0;
    }

    public static List<MapaPendenciasControleCobrancaVO> montarDadosConsultaTelaControleCobranca(SqlRowSet tabelaResultado) throws Exception {
        List<MapaPendenciasControleCobrancaVO> vetResultado = new ArrayList<MapaPendenciasControleCobrancaVO>(0);
        while (tabelaResultado.next()) {
        	
            vetResultado.add(montarDadosTelaControleCobranca(tabelaResultado));
        }
        return vetResultado;
    }

    public static MapaPendenciasControleCobrancaVO montarDadosTelaControleCobranca(SqlRowSet dadosSQL) throws Exception {
        MapaPendenciasControleCobrancaVO obj = new MapaPendenciasControleCobrancaVO();
        obj.getContaReceber().setCodigo(dadosSQL.getInt("contareceber"));
        obj.getContaReceber().getMatriculaAluno().setMatricula(dadosSQL.getString("matricula"));
        obj.getContaReceber().getMatriculaAluno().getAluno().setCodigo(dadosSQL.getInt("p.codigo"));
        obj.getContaReceber().getMatriculaAluno().getAluno().setNome(dadosSQL.getString("nome"));
        obj.getContaReceber().getMatriculaAluno().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino"));
        obj.getContaReceber().setTipoPessoa(dadosSQL.getString("tipopessoa"));
        obj.getContaReceber().setValorDescontoRecebido(dadosSQL.getDouble("valordescontorecebido"));
        obj.getContaReceber().setDataVencimento(dadosSQL.getTimestamp("datavencimento"));
        obj.getContaReceber().setSituacao(dadosSQL.getString("situacao"));
        obj.getContaReceber().setValor(dadosSQL.getDouble("valor"));
        obj.getContaReceber().setValorRecebido(dadosSQL.getDouble("valorrecebido"));
        obj.getContaReceber().setValorDescontoAlunoJaCalculado(dadosSQL.getDouble("valorDescontoAlunoJaCalculado"));
        obj.getContaReceber().setValorDescontoConvenio(dadosSQL.getDouble("descontoConvenio"));
        obj.getContaReceber().setValorDescontoInstituicao(dadosSQL.getDouble("descontoInstituicao"));
        obj.getContaReceber().setValorDescontoProgressivo(dadosSQL.getDouble("valorDescontoProgressivo"));
        obj.getContaReceber().setCodOrigem(dadosSQL.getString("codorigem"));
        obj.setDataPagamento(dadosSQL.getDate("dataPagamento"));
        obj.setDataProcessamento(dadosSQL.getDate("dataProcessamento"));
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setValorDiferenca(dadosSQL.getDouble("valorDiferenca"));
        obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
        obj.getMatricula().getUnidadeEnsino().setCodigo(dadosSQL.getInt("unidadeensino"));
        obj.getContaReceber().setCodigo(dadosSQL.getInt("contaReceber"));
        obj.getContaReceber().setMatriculaPeriodo(dadosSQL.getInt("matriculaperiodo"));
        obj.getContaReceber().setParcela(dadosSQL.getString("parcela"));
		if (obj.getContaReceber().getTipoPessoa().equals("PA")) {
			obj.getContaReceber().getParceiroVO().setCodigo(dadosSQL.getInt("parceiro"));
			obj.getContaReceber().getParceiroVO().setNome(dadosSQL.getString("parc.nome"));
			obj.getContaReceber().getParceiroVO().setCPF(dadosSQL.getString("parc.cpf"));
			obj.getContaReceber().getParceiroVO().setCNPJ(dadosSQL.getString("parc.cnpj"));
		} else if (obj.getContaReceber().getTipoPessoa().equals("FO")) {
			obj.getContaReceber().getFornecedor().setCodigo(dadosSQL.getInt("for.codigo"));
			obj.getContaReceber().getFornecedor().setNome(dadosSQL.getString("for.nome"));
			obj.getContaReceber().getFornecedor().setCPF(dadosSQL.getString("for.cpf"));
			obj.getContaReceber().getFornecedor().setCNPJ(dadosSQL.getString("for.cnpj"));
			obj.getContaReceber().getFornecedor().setTipoEmpresa(dadosSQL.getString("for.tipoEmpresa"));
		} else if (obj.getContaReceber().getTipoPessoa().equals("RF")) {
			//DADOS DO RESPONSÁVEL FINANCEIRO
			obj.getContaReceber().getResponsavelFinanceiro().setCodigo(dadosSQL.getInt("resp.codigo"));
			obj.getContaReceber().getResponsavelFinanceiro().setNome(dadosSQL.getString("resp.nome"));
			obj.getContaReceber().getResponsavelFinanceiro().setCPF(dadosSQL.getString("resp.cpf"));
			//DADOS DO ALUNO
			obj.getContaReceber().getPessoa().setCodigo(dadosSQL.getInt("p.codigo"));
			obj.getContaReceber().getPessoa().setNome(dadosSQL.getString("nome"));
			obj.getContaReceber().getPessoa().setAluno(dadosSQL.getBoolean("aluno"));
			obj.getContaReceber().getPessoa().setFuncionario(dadosSQL.getBoolean("funcionario"));
			obj.getContaReceber().getPessoa().setProfessor(dadosSQL.getBoolean("professor"));
			obj.getContaReceber().getPessoa().setCandidato(dadosSQL.getBoolean("candidato"));
			obj.getContaReceber().getPessoa().setPossuiAcessoVisaoPais(dadosSQL.getBoolean("possuiacessovisaopais"));
			obj.getContaReceber().getPessoa().setMembroComunidade(dadosSQL.getBoolean("membrocomunidade"));
			obj.getContaReceber().getPessoa().setRequisitante(dadosSQL.getBoolean("requisitante"));
		} else {
			obj.getContaReceber().getPessoa().setCodigo(dadosSQL.getInt("p.codigo"));
			obj.getContaReceber().getPessoa().setNome(dadosSQL.getString("nome"));
			obj.getContaReceber().getPessoa().setAluno(dadosSQL.getBoolean("aluno"));
			obj.getContaReceber().getPessoa().setFuncionario(dadosSQL.getBoolean("funcionario"));
			obj.getContaReceber().getPessoa().setProfessor(dadosSQL.getBoolean("professor"));
			obj.getContaReceber().getPessoa().setCandidato(dadosSQL.getBoolean("candidato"));
			obj.getContaReceber().getPessoa().setPossuiAcessoVisaoPais(dadosSQL.getBoolean("possuiacessovisaopais"));
			obj.getContaReceber().getPessoa().setMembroComunidade(dadosSQL.getBoolean("membrocomunidade"));
			obj.getContaReceber().getPessoa().setRequisitante(dadosSQL.getBoolean("requisitante"));
		}
        obj.setSelecionado(dadosSQL.getBoolean("selecionado"));

        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    public List consultarPorContaReceber(Integer codigoContaReceber, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MapaPendenciasControleCobranca WHERE contaReceber = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoContaReceber.intValue()});
        try {
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
        } finally {
            sqlStr = null;
        }
    }

    public List consultarPorCodigo(Integer codigoMapa, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MapaPendenciasControleCobranca WHERE codigo >= ? ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, new Object[]{codigoMapa.intValue()});
        try {
            return (montarDadosConsulta(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
        } finally {
            sqlStr = null;
        }
    }

    public List<MapaPendenciasControleCobrancaVO> consultarTodasPendencias(String matricula, String nome, Boolean todoPeriodo, Date dataInicio, Date dataFim, String ano, String semestre, String campoConsultaPeriodo, boolean controlarAcesso, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT distinct mpcc.codigo, mpcc.valorDiferenca, mpcc.dataPagamento, mpcc.dataProcessamento, m.matricula, mpcc.contareceber, cr.situacao, cr.valor, cr.matriculaperiodo, ");
        sqlStr.append("cr.valorrecebido, cr.valordescontorecebido, cr.valorDescontoAlunoJaCalculado, cr.descontoConvenio, cr.descontoInstituicao, cr.valorDescontoProgressivo, ");
        sqlStr.append("cr.datavencimento, cr.tipopessoa, p.codigo as \"p.codigo\", p.nome, p.aluno, cr.parcela, cr.parceiro, parc.nome as \"parc.nome\", parc.cpf as \"parc.cpf\", parc.cnpj as \"parc.cnpj\", ");
        sqlStr.append(" p.funcionario, p.professor, p.candidato, p.possuiacessovisaopais, p.membrocomunidade, p.requisitante, ");
        sqlStr.append("cr.codorigem, m.unidadeensino, mpcc.selecionado, ");
        sqlStr.append("contacorrente.numero as contacorrente_numero, contacorrente.tipoContaCorrente as contacorrente_tipoContaCorrente,  ");
        sqlStr.append("agencia.numeroagencia as agencia_numero, agencia.digito as agencia_digito, banco.nome as banco_nome, ");
        sqlStr.append(" fornecedor.codigo as \"for.codigo\", fornecedor.nome as \"for.nome\" , fornecedor.cnpj as \"for.cnpj\" , fornecedor.cpf as \"for.cpf\",  fornecedor.tipoEmpresa as \"for.tipoEmpresa\", ");
        sqlStr.append(" resp.codigo as \"resp.codigo\", resp.nome as \"resp.nome\", resp.cpf as \"resp.cpf\" ");
        sqlStr.append("FROM MapaPendenciasControleCobranca mpcc ");
        sqlStr.append("LEFT JOIN matricula m ON mpcc.matricula = m.matricula ");
        sqlStr.append("LEFT JOIN pessoa p ON m.aluno = p.codigo ");
        sqlStr.append("LEFT JOIN contareceber cr ON mpcc.contareceber = cr.codigo ");
        sqlStr.append("LEFT JOIN contacorrente ON contacorrente.codigo = cr.contacorrente ");
        sqlStr.append("LEFT JOIN agencia ON contacorrente.agencia = agencia.codigo ");
        sqlStr.append("LEFT JOIN banco ON agencia.banco = banco.codigo ");
        sqlStr.append("LEFT JOIN parceiro parc ON parc.codigo = cr.parceiro ");
        sqlStr.append("LEFT JOIN fornecedor ON fornecedor.codigo = cr.fornecedor ");
        sqlStr.append("LEFT JOIN matriculaPeriodo ON matriculaPeriodo.codigo = cr.matriculaPeriodo ");
        sqlStr.append("LEFT JOIN pessoa resp ON cr.responsavelFinanceiro = resp.codigo ");
        sqlStr.append("WHERE 1=1 ");
        if (unidadeEnsino != 0) {
            sqlStr.append("AND cr.unidadeensinofinanceira = ").append(unidadeEnsino);
        }
        if (!matricula.equals("")) {
        	sqlStr.append(" AND TRIM(m.matricula) ilike('").append(matricula.trim()).append("%') ");
        }
        if (!nome.equals("")) {
        	sqlStr.append(" AND TRIM(p.nome) ilike('").append(nome.trim()).append("%') ");
        }
        if (!todoPeriodo) {
        	if (campoConsultaPeriodo.equals("ANO_SEMESTRE")) {
        		if (!ano.equals("")) {
        			sqlStr.append(" AND ano = '").append(ano).append("' ");
        		}
        		if (!semestre.equals("")) {
        			sqlStr.append(" AND semestre = '").append(semestre).append("' ");
        		}
        	}
        	if (campoConsultaPeriodo.equals("DATA_VENCIMENTO")) {
        		sqlStr.append(" AND datavencimento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
        		sqlStr.append(" AND datavencimento <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
        	}
        	if (campoConsultaPeriodo.equals("DATA_PROCESSAMENTO")) {
        		sqlStr.append(" AND dataprocessamento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
        		sqlStr.append(" AND dataprocessamento <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
        	}
        	if (campoConsultaPeriodo.equals("DATA_PAGAMENTO")) {
        		sqlStr.append(" AND datapagamento >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
        		sqlStr.append(" AND datapagamento <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
        	}
        }
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        try {
        	List<MapaPendenciasControleCobrancaVO> vetResultado = new ArrayList<MapaPendenciasControleCobrancaVO>(0);
            while (tabelaResultado.next()) {
            	MapaPendenciasControleCobrancaVO mapaPendenciasControleCobrancaVO = montarDadosTelaControleCobranca(tabelaResultado);
            	mapaPendenciasControleCobrancaVO.getContaReceber().getContaCorrenteVO().setNumero(tabelaResultado.getString("contacorrente_numero"));
            	mapaPendenciasControleCobrancaVO.getContaReceber().getContaCorrenteVO().getAgencia().setNumeroAgencia(tabelaResultado.getString("agencia_numero"));
            	mapaPendenciasControleCobrancaVO.getContaReceber().getContaCorrenteVO().getAgencia().setDigito(tabelaResultado.getString("agencia_digito"));
            	mapaPendenciasControleCobrancaVO.getContaReceber().getContaCorrenteVO().getAgencia().getBanco().setNome(tabelaResultado.getString("banco_nome"));
            	mapaPendenciasControleCobrancaVO.getContaReceber().getContaCorrenteVO().setContaCaixa(false);
            	if (Uteis.isAtributoPreenchido(tabelaResultado.getString("contacorrente_tipoContaCorrente"))) {
            		mapaPendenciasControleCobrancaVO.getContaReceber().getContaCorrenteVO().setTipoContaCorrenteEnum(TipoContaCorrenteEnum.valueOf(tabelaResultado.getString("contacorrente_tipoContaCorrente")));
            	}
                vetResultado.add(mapaPendenciasControleCobrancaVO);
            }
            return vetResultado;
        } finally {
            sqlStr = null;
        }
    }

    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
        }
        return vetResultado;
    }

    public static MapaPendenciasControleCobrancaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        MapaPendenciasControleCobrancaVO obj = new MapaPendenciasControleCobrancaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getMatricula().setMatricula(dadosSQL.getString("matricula"));
        obj.getContaReceber().setCodigo(dadosSQL.getInt("contaReceber"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosMatricula(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        montarDadosContaReceber(obj, Uteis.NIVELMONTARDADOS_DADOSBASICOS, configuracaoFinanceiroVO, usuario);
        return obj;
    }

    public static void montarDadosMatricula(MapaPendenciasControleCobrancaVO obj, Integer nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (!obj.getMatricula().getMatricula().equals("")) {
            obj.setMatricula(getFacadeFactory().getMatriculaFacade().consultarPorChavePrimaria(obj.getMatricula().getMatricula(), 0, NivelMontarDados.getEnum(nivelMontarDados), usuario));
        }
    }

    public static void montarDadosContaReceber(MapaPendenciasControleCobrancaVO obj, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        if (obj.getContaReceber().getCodigo().intValue() == 0) {
            obj.setContaReceber(new ContaReceberVO());
            return;
        }
        obj.setContaReceber(getFacadeFactory().getContaReceberFacade().consultarPorChavePrimaria(obj.getContaReceber().getCodigo(), false, nivelMontarDados, configuracaoFinanceiroVO, usuario));
    }

    public MapaPendenciasControleCobrancaVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, ConfiguracaoFinanceiroVO configuracaoFinanceiroVO, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM MapaPendenciasControleCobranca WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( MapaPendenciasControleCobranca ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, configuracaoFinanceiroVO, usuario));
    }

    public static String getIdEntidade() {
        return MapaPendenciasControleCobranca.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        MapaPendenciasControleCobranca.idEntidade = idEntidade;
    }

    /**
     * Este método é reponsável em alterar os desconto da conta a receber o do plano de desconto do aluno conforme os descontos informados no mapa de pendência de controle de conbraça
     * @param mapaPendenciasControleCobrancaVO
     * @param contaReceberVOs
     * @param planoDescontoVO
     * @param descontoProgressivoVO
     * @param usuarioVO
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarAplicacaoDescontosContaReceber(MapaPendenciasControleCobrancaVO mapaPendenciasControleCobrancaVO, List<ContaReceberVO> contaReceberVOs, PlanoDescontoVO planoDescontoVO, DescontoProgressivoVO descontoProgressivoVO, UsuarioVO usuarioVO) throws Exception {
        try {
            getFacadeFactory().getPlanoFinanceiroAlunoFacade().realizarAlteracaoPlanoFinanceiroAlunoConformeMapaPendenciaControleCobranca(
                    mapaPendenciasControleCobrancaVO.getContaReceber().getMatriculaPeriodo(),
                    planoDescontoVO.getCodigo(),
                    descontoProgressivoVO.getCodigo(),
                    usuarioVO);
            getFacadeFactory().getContaReceberFacade().realizarAlteracaoContaReceberConformeMapaPendenciaControleCobranca(
                    contaReceberVOs,
                    planoDescontoVO,
                    descontoProgressivoVO,
                    usuarioVO);
            excluir(mapaPendenciasControleCobrancaVO, usuarioVO);
        } catch (Exception e) {
            throw e;
        } finally {
        }
    }
}
