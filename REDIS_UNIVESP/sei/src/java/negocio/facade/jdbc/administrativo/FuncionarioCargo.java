package negocio.facade.jdbc.administrativo;

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

import controle.arquitetura.DataModelo;
import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.FormaContratacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.PrevidenciaEnum;
import negocio.comuns.administrativo.enumeradores.SituacaoFuncionarioEnum;
import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.administrativo.enumeradores.TipoRecebimentoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.crm.enumerador.NivelExperienciaCargoEnum;
import negocio.comuns.recursoshumanos.GrupoLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.LancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.SalarioCompostoVO.EnumCampoConsultaSalarioComposto;
import negocio.comuns.recursoshumanos.SecaoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.TemplateLancamentoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.enumeradores.TipoContratacaoComissionadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.FuncionarioCargoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>FuncionarioCargoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>FuncionarioCargoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see FuncionarioCargoVO
 * @see ControleAcesso
 * @see funcionario
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Repository
@Scope("singleton")
@Lazy
public class FuncionarioCargo extends ControleAcesso implements FuncionarioCargoInterfaceFacade {

	private static final long serialVersionUID = 1L;

	protected static String idEntidade;

    public FuncionarioCargo() throws Exception {
        super();
        setIdEntidade("Funcionario");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>FuncionarioCargoVO</code>.
     */
    public FuncionarioCargoVO novo() throws Exception {
        FuncionarioCargo.incluir(getIdEntidade());
        FuncionarioCargoVO obj = new FuncionarioCargoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>FuncionarioCargoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>FuncionarioCargoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final FuncionarioCargoVO obj) throws Exception {
        FuncionarioCargoVO.validarDados(obj);
        validarDataDemissaoSituacaoFuncionario(obj);
		realizarAtivacaoConformeSituacaoFuncionario(obj);
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
            	
            	final StringBuilder sql = new StringBuilder("INSERT INTO FuncionarioCargo ")
                		.append("( funcionario, cargo, unidadeEnsino, nivelExperiencia, consultor, ")
                		.append(" gerente, dataAdmissao, dataDemissao, tipoRecebimento, formaContratacao, ")
                		.append(" situacaoFuncionario, jornada, salario, quantidadeCargoFuncionario, matriculacargo, ") 
                		.append(" utilizaRH, comissionado, secaoFolhaPagamento, cargoAtual, tipoContratacaoComissionado, nivelSalarial, faixaSalarial,")
                		.append(" dataBaseQuinquenio, previdencia, optanteTotal, departamento, salariocargoatual,sindicato, salariocomposto, curso, ativo)")
                		.append(" VALUES ( ?, ?, ?, ?, ?, ")
                		.append(" ?, ?, ?, ?, ?, ")
                		.append(" ?, ?, ?, ?, ?, ")
                		.append(" ?, ?, ?, ?, ?, ?, ?, ")
                		.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?)returning codigo ");

	            PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
                if (obj.getFuncionarioVO().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getFuncionarioVO().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                if (obj.getCargo().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(2, obj.getCargo().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(2, 0);
                }
                if (obj.getUnidade().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(3, obj.getUnidade().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(3, 0);
                }
                sqlInserir.setString(4, obj.getNivelExperiencia().toString());
                sqlInserir.setBoolean(5, obj.getConsultor());
                sqlInserir.setBoolean(6, obj.getGerente());
                
                sqlInserir.setDate(7, Uteis.getDataJDBC(obj.getDataAdmissao()));
                sqlInserir.setDate(8, Uteis.getDataJDBC(obj.getDataDemissao()));
                if (Uteis.isAtributoPreenchido(obj.getTipoRecebimento())) {
					sqlInserir.setString(9, obj.getTipoRecebimento().toString());
				} else {
					sqlInserir.setNull(9, 0);
				}

                if (Uteis.isAtributoPreenchido(obj.getFormaContratacao())) {
                	sqlInserir.setString(10, obj.getFormaContratacao().toString());
                } else {
                	sqlInserir.setNull(10, 0);
                }
                sqlInserir.setString(11, obj.getSituacaoFuncionario());
                sqlInserir.setInt(12, obj.getJornada());
                sqlInserir.setBigDecimal(13, obj.getSalario());
                if (Uteis.isAtributoPreenchido(obj.getMatriculaCargo())) {
                	if (!obj.getMatriculaCargo().contains(obj.getFuncionarioVO().getMatricula())) {                		
                		sqlInserir.setInt(14, (consultarProximoValorMaximoContadorDeRegistrosDoFuncionarioCargo(obj) - 1));
                	} else {
                		sqlInserir.setInt(14, (consultarProximoValorMaximoContadorDeRegistrosDoFuncionarioCargo(obj)));
                	}
                } else {
                	sqlInserir.setInt(14, (consultarProximoValorMaximoContadorDeRegistrosDoFuncionarioCargo(obj)));
                }
                sqlInserir.setString(15, gerarMatricula(obj));
                sqlInserir.setBoolean(16, obj.getUtilizaRH());
                sqlInserir.setBoolean(17, obj.getComissionado());

                if (Uteis.isAtributoPreenchido(obj.getSecaoFolhaPagamento().getCodigo()) ) {
                	sqlInserir.setInt(18, obj.getSecaoFolhaPagamento().getCodigo());                	
                } else {
                	sqlInserir.setNull(18, 0);
                }

                if (Uteis.isAtributoPreenchido(obj.getCargoAtual().getCodigo()) ) {
                	sqlInserir.setInt(19, obj.getCargoAtual().getCodigo());                	
                } else {
                	sqlInserir.setNull(19, 0);
                }

                if (obj.getComissionado() && Uteis.isAtributoPreenchido(obj.getTipoContratacaoComissionado())) {
                	sqlInserir.setString(20, obj.getTipoContratacaoComissionado().toString());
                } else {
                	sqlInserir.setNull(20, 0);
                }

                if (Uteis.isAtributoPreenchido(obj.getNivelSalarial().getCodigo()) ) {
                	sqlInserir.setInt(21, obj.getNivelSalarial().getCodigo());                	
                } else {
                	sqlInserir.setNull(21, 0);
                }

                if (Uteis.isAtributoPreenchido(obj.getFaixaSalarial().getCodigo()) ) {
                	sqlInserir.setInt(22, obj.getFaixaSalarial().getCodigo());                	
                } else {
                	sqlInserir.setNull(22, 0);
                }

                sqlInserir.setDate(23, UteisData.getDataJDBC(obj.getDataBaseQuinquenio()));

                if (Uteis.isAtributoPreenchido(obj.getPrevidencia())) {
                	sqlInserir.setString(24, obj.getPrevidencia().toString());
                } else {
                	sqlInserir.setNull(24, 0);
                }

                sqlInserir.setBoolean(25, obj.getOptanteTotal());

                if (Uteis.isAtributoPreenchido(obj.getDepartamento().getCodigo())) {
                	sqlInserir.setInt(26, obj.getDepartamento().getCodigo());
                } else {
                	sqlInserir.setNull(26, 0);
                }
                
                sqlInserir.setBigDecimal(27, obj.getSalarioCargoAtual());
                if (Uteis.isAtributoPreenchido(obj.getSindicatoVO().getCodigo())) {
                	sqlInserir.setInt(28, obj.getSindicatoVO().getCodigo());
                } else {
                	sqlInserir.setNull(28, 0);
                }
                sqlInserir.setBoolean(29, obj.getSalarioComposto());
                if (Uteis.isAtributoPreenchido(obj.getCursoVO().getCodigo())) {
					sqlInserir.setInt(30, obj.getCursoVO().getCodigo());
				} else {
					sqlInserir.setNull(30, 0);
				}
				sqlInserir.setBoolean(31, obj.getAtivo());
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
    }
    
    /**
	 * Gera a nova matricula do Cargo para o Funcionario
	 * @param obj
	 * @return
	 */
    private String gerarMatricula(FuncionarioCargoVO obj) {
    	if(Uteis.isAtributoPreenchido(obj.getMatriculaCargo())) {
    		return obj.getMatriculaCargo();
    	}

		obj.setQuantidadeCargoFuncionario(consultarProximoValorMaximoContadorDeRegistrosDoFuncionarioCargo(obj));
		StringBuilder matricula = new StringBuilder();
		matricula.append(obj.getFuncionarioVO().getMatricula()).append("-").append(obj.getQuantidadeCargoFuncionario());
		return matricula.toString();
	}
    
    /**
     * Consulta o contador maximo de Cargos do Funcionario para compor o matricula no novo Cargo Funcionario
     * 
     * @param obj
     * @return
     */
    public Integer consultarProximoValorMaximoContadorDeRegistrosDoFuncionarioCargo(FuncionarioCargoVO obj) {
    	if(obj.getQuantidadeCargoFuncionario() > 0 )
    		return obj.getQuantidadeCargoFuncionario();
		
		StringBuilder sb = new StringBuilder();
		sb.append(" SELECT coalesce(max(quantidadecargofuncionario), 0) as maximo from funcionariocargo where funcionario = ").append(obj.getFuncionarioVO().getCodigo());

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (tabelaResultado.next())
			return tabelaResultado.getInt("maximo") + 1;
		else
			return 1;
	}

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>FuncionarioCargoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>FuncionarioCargoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final FuncionarioCargoVO obj) throws Exception {
        FuncionarioCargoVO.validarDados(obj);
        FuncionarioCargo.alterar(getIdEntidade());
        validarDataDemissaoSituacaoFuncionario(obj);
		realizarAtivacaoConformeSituacaoFuncionario(obj);

        StringBuilder sql = new StringBuilder("UPDATE FuncionarioCargo set cargo=?, unidadeEnsino=?, nivelExperiencia=?, consultor=?, gerente=?, ")
        		.append(" dataAdmissao = ?, dataDemissao = ?, tipoRecebimento = ?,  formaContratacao=?, situacaoFuncionario=?, ")
        		.append(" jornada=?, salario=?, utilizaRH=?, matriculacargo=?, quantidadeCargoFuncionario=?, comissionado = ?, secaoFolhaPagamento = ?, ")
        		.append(" cargoAtual = ?, tipoContratacaoComissionado = ?, nivelSalarial = ?, faixaSalarial = ?,")
        		.append(" dataBaseQuinquenio = ?, previdencia = ?, optanteTotal = ?, departamento = ?, salariocargoatual = ?, sindicato =?, salarioComposto=?, curso =?, ativo = ? ")
        		.append(" WHERE codigo = ? ");

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

        	 public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                 PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
                 if (obj.getCargo().getCodigo().intValue() != 0) {
                     sqlAlterar.setInt(1, obj.getCargo().getCodigo().intValue());
                 } else {
                     sqlAlterar.setNull(1, 0);
                 }
                 if (obj.getUnidade().getCodigo().intValue() != 0) {
                     sqlAlterar.setInt(2, obj.getUnidade().getCodigo().intValue());
                 } else {
                     sqlAlterar.setNull(2, 0);
                 }
                 sqlAlterar.setString(3, obj.getNivelExperiencia().toString());
                 sqlAlterar.setBoolean(4, obj.getConsultor());
                 sqlAlterar.setBoolean(5, obj.getGerente());
                 
                 sqlAlterar.setDate(6, Uteis.getDataJDBC(obj.getDataAdmissao()));
                 sqlAlterar.setDate(7, Uteis.getDataJDBC(obj.getDataDemissao()));
                 if (Uteis.isAtributoPreenchido(obj.getTipoRecebimento())) {
                 	sqlAlterar.setString(8, obj.getTipoRecebimento().toString());
 				} else {
 					sqlAlterar.setNull(8, 0);
 				}

                 if (Uteis.isAtributoPreenchido(obj.getFormaContratacao())) {
                 	sqlAlterar.setString(9, obj.getFormaContratacao().toString());
                 } else {
                 	sqlAlterar.setNull(9, 0);
                 }
                 sqlAlterar.setString(10, obj.getSituacaoFuncionario());
                 sqlAlterar.setInt(11, obj.getJornada());
                 sqlAlterar.setBigDecimal(12, obj.getSalario());
                 sqlAlterar.setBoolean(13, obj.getUtilizaRH());
                 
                 sqlAlterar.setString(14, gerarMatricula(obj));
                 if (Uteis.isAtributoPreenchido(obj.getMatriculaCargo())) {
                 	if (!obj.getMatriculaCargo().contains(obj.getFuncionarioVO().getMatricula())) {                		
                 		sqlAlterar.setInt(15, (consultarProximoValorMaximoContadorDeRegistrosDoFuncionarioCargo(obj) -1));
                 	} else {
                 		sqlAlterar.setInt(15, (consultarProximoValorMaximoContadorDeRegistrosDoFuncionarioCargo(obj)));
                 	}
                 } else {
                 	sqlAlterar.setInt(15, (consultarProximoValorMaximoContadorDeRegistrosDoFuncionarioCargo(obj)));
                 }

                 sqlAlterar.setBoolean(16, obj.getComissionado());

                 if (Uteis.isAtributoPreenchido(obj.getSecaoFolhaPagamento().getCodigo()) ) {
                 	sqlAlterar.setInt(17, obj.getSecaoFolhaPagamento().getCodigo());                	
                 } else {
                 	sqlAlterar.setNull(17, 0);
                 }

                 if (Uteis.isAtributoPreenchido(obj.getCargoAtual().getCodigo()) ) {
                 	sqlAlterar.setInt(18, obj.getCargoAtual().getCodigo());                	
                 } else {
                 	sqlAlterar.setNull(18, 0);
                 }

                 if (obj.getComissionado() && Uteis.isAtributoPreenchido(obj.getTipoContratacaoComissionado())) {
                 	sqlAlterar.setString(19, obj.getTipoContratacaoComissionado().toString());
                 } else {
                 	sqlAlterar.setNull(19, 0);
                 }
                
                 if (Uteis.isAtributoPreenchido(obj.getNivelSalarial().getCodigo()) ) {
                 	sqlAlterar.setInt(20, obj.getNivelSalarial().getCodigo());                	
                 } else {
                 	sqlAlterar.setNull(20, 0);
                 }

                 if (Uteis.isAtributoPreenchido(obj.getFaixaSalarial().getCodigo()) ) {
                 	sqlAlterar.setInt(21, obj.getFaixaSalarial().getCodigo());                	
                 } else {
                 	sqlAlterar.setNull(21, 0);
                 }
                 
                 sqlAlterar.setDate(22, UteisData.getDataJDBC(obj.getDataBaseQuinquenio()));

                 if (Uteis.isAtributoPreenchido(obj.getPrevidencia())) {
                 	sqlAlterar.setString(23, obj.getPrevidencia().toString());
                 } else {
                 	sqlAlterar.setNull(23, 0);
                 }

                 sqlAlterar.setBoolean(24, obj.getOptanteTotal());

                 if (Uteis.isAtributoPreenchido(obj.getDepartamento().getCodigo())) {
                 	sqlAlterar.setInt(25, obj.getDepartamento().getCodigo());
                 } else {
                 	sqlAlterar.setNull(25, 0);
                 }
                 
                 sqlAlterar.setBigDecimal(26, obj.getSalarioCargoAtual());
                 
                 if (Uteis.isAtributoPreenchido(obj.getSindicatoVO().getCodigo())) {
                	 sqlAlterar.setInt(27, obj.getSindicatoVO().getCodigo());
                 } else {
                	 sqlAlterar.setNull(27, 0);
                 }
                 
                 sqlAlterar.setBoolean(28, obj.getSalarioComposto());

                 if (Uteis.isAtributoPreenchido(obj.getCursoVO().getCodigo())) {
 					sqlAlterar.setInt(29, obj.getCursoVO().getCodigo());
 				} else {
 					sqlAlterar.setNull(29, 0);
 				}
 				sqlAlterar.setBoolean(30, obj.getAtivo());
 				sqlAlterar.setInt(31, obj.getCodigo().intValue());
                 
                 return sqlAlterar;
             }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>FuncionarioCargoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>FuncionarioCargoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(FuncionarioCargoVO obj) throws Exception {
        FuncionarioCargo.excluir(getIdEntidade());
        String sql = "DELETE FROM FuncionarioCargo WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    public List consultarPorFuncionario(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM FuncionarioCargo WHERE funcionario = " + valorConsulta.intValue() + " ORDER BY quantidadeCargoFuncionario desc ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorCargo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM FuncionarioCargo WHERE cargo = " + valorConsulta.intValue() + " ORDER BY cargo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
    public List<FuncionarioCargoVO> consultarPorNomeFuncionario(DataModelo dataModelo, String valorConsulta,
			int nivelMontarDados, UsuarioVO usuario) throws Exception  {
		return consultarPorNomeFuncionario(dataModelo, valorConsulta,
				nivelMontarDados, usuario, false);
	}

    public List<FuncionarioCargoVO> consultarPorNomeFuncionario(DataModelo dataModelo, String valorConsulta,
			int nivelMontarDados, UsuarioVO usuario, boolean consultarApenasProfessor) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select funcionarioCargo.* from funcionarioCargo ");
        sb.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo ");
        sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
        sb.append(" where lower(pessoa.nome) like(?) ");
        if (consultarApenasProfessor) {
			sb.append(" and pessoa.professor = true");
		}

        sb.append(" order by pessoa.nome ");
        UteisTexto.addLimitAndOffset(sb, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), PERCENT + valorConsulta.toLowerCase() + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<FuncionarioCargoVO> consultarPorNomeFuncionarioAtivo(DataModelo dataModelo, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select funcionarioCargo.* from funcionarioCargo ");
    	sb.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo ");
    	sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
    	sb.append(" where lower(pessoa.nome) like(?) and situacaofuncionario = 'ATIVO' ");
    	if(Uteis.isAtributoPreenchido(dataModelo.getUnidadeEnsinoVO())){
    		sb.append(" and funcionarioCargo.unidadeensino = ").append(dataModelo.getUnidadeEnsinoVO().getCodigo());	
    	}
    	sb.append(" order by pessoa.nome ");
    	UteisTexto.addLimitAndOffset(sb, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), PERCENT + dataModelo.getValorConsulta().toLowerCase() + PERCENT);
    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public Integer consultarTotalPorNomeFuncionarioAtivo(DataModelo dataModelo, int nivelMontarDados, UsuarioVO usuario) {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select count(funcionarioCargo.codigo) as qtde from funcionarioCargo ");
    	sb.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo ");
    	sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
    	sb.append(" where lower(pessoa.nome) like(?) and situacaofuncionario = 'ATIVO' ");
    	if(Uteis.isAtributoPreenchido(dataModelo.getUnidadeEnsinoVO())){
    		sb.append(" and funcionarioCargo.unidadeensino = ").append(dataModelo.getUnidadeEnsinoVO().getCodigo());	
    	}
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), PERCENT + dataModelo.getValorConsulta().toLowerCase() + PERCENT);
    	return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
    }

    public List<FuncionarioCargoVO> consultarPorNomeFuncionarioUnidadeEnsinoSituacao(String valorConsulta, Integer unidadeEnsino, Boolean ativo, Boolean consultor, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select funcionarioCargo.* from funcionarioCargo ");
        sb.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo ");
        sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
        sb.append(" where lower(sem_acentos(pessoa.nome)) ilike(sem_acentos(?)) ");
        if(Uteis.isAtributoPreenchido(unidadeEnsino)){
        	sb.append(" and funcionarioCargo.unidadeensino = ").append(unidadeEnsino);	
        }
        if (ativo) {
        	sb.append(" and pessoa.ativo ") ;
        	sb.append(" and funcionarioCargo.ativo ") ;
        }
        if (consultor) {
        	sb.append(" and funcionarioCargo.consultor ") ;
        }
        sb.append(" order by pessoa.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), PERCENT + valorConsulta.toLowerCase() + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
    
   /* public List<FuncionarioCargoVO> consultarTodosFuncionariosCargoAtivo(int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select funcionarioCargo.* from funcionarioCargo ");
		sb.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo ");
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" where situacaofuncionario = 'ATIVO' ");
		sb.append(" order by pessoa.nome ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}*/

    public List<FuncionarioCargoVO> consultarPorNomeCargo(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select funcionarioCargo.* from funcionarioCargo ");
        sb.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");
        sb.append(" where lower(cargo.nome) like(?) ");
        sb.append(" order by cargo.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta.toLowerCase() + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<FuncionarioCargoVO> consultarPorNomeCargoUnidadeEnsinoSituacao(String valorConsulta, Integer unidadeEnsino, Boolean ativo, Boolean consultor, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select funcionarioCargo.* from funcionarioCargo ");
        sb.append(" inner join funcionario on funcionario.codigo = funcionarioCargo.funcionario ");
        sb.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");
        sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
        sb.append(" where lower(sem_acentos(cargo.nome)) ilike(sem_acentos(?)) ");
        if(Uteis.isAtributoPreenchido(unidadeEnsino)){
        	sb.append(" and funcionarioCargo.unidadeensino = ").append(unidadeEnsino);	
        }
        if (ativo) {
        	sb.append(" and pessoa.ativo ");
        	sb.append(" and funcionarioCargo.ativo ");
        }
        if (consultor) {
        	sb.append(" and funcionarioCargo.consultor ") ;
        }
        sb.append(" order by cargo.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta.toLowerCase() + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorNomeCargoUnico(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select funcionarioCargo.* from funcionarioCargo ");
        sb.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");
        sb.append(" where lower(cargo.nome) ='");
        sb.append(valorConsulta.toLowerCase());
        sb.append("' ");
        sb.append(" order by cargo.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorNomeCargoUnicoUnidadeEnsino(String valorConsulta, Integer unidadeEnsino, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select funcionarioCargo.* from funcionarioCargo ");
        sb.append(" inner join cargo on funcionariocargo.cargo = cargo.codigo ");
        sb.append(" INNER JOIN funcionario ON funcionariocargo.funcionario = funcionario.codigo");
        sb.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
        sb.append(" where lower(cargo.nome) ='");
        sb.append(valorConsulta.toLowerCase());
        sb.append("' and ");
        sb.append(" funcionarioCargo.unidadeensino = ");
        sb.append(unidadeEnsino);
        sb.append(" AND pessoa.ativo");
        sb.append(" AND funcionarioCargo.ativo");
        sb.append(" order by cargo.nome ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>FuncionarioCargo</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FuncionarioCargoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM FuncionarioCargo WHERE codigo = " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>FuncionarioCargoVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList();
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>FuncionarioCargoVO</code>.
     * @return  O objeto da classe <code>FuncionarioCargoVO</code> com os dados devidamente montados.
     */
    public static FuncionarioCargoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		FuncionarioCargoVO obj = new FuncionarioCargoVO();
		obj.setNovoObj(Boolean.FALSE);

		obj.setCodigo(dadosSQL.getInt("codigo"));
		obj.setMatriculaCargo(dadosSQL.getString("matriculacargo"));
		obj.getCargo().setCodigo(dadosSQL.getInt("cargo"));
		obj.getUnidade().setCodigo(dadosSQL.getInt("unidadeEnsino"));
		obj.setConsultor(dadosSQL.getBoolean("consultor"));
		obj.setGerente(dadosSQL.getBoolean("gerente"));
		obj.setNivelExperiencia(NivelExperienciaCargoEnum.valueOf(dadosSQL.getString("nivelExperiencia")));
		obj.setAtivo(dadosSQL.getBoolean("ativo"));

		obj.setDataAdmissao(dadosSQL.getDate("dataAdmissao"));
		obj.setDataDemissao(dadosSQL.getDate("dataDemissao"));

		if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoRecebimento"))) {
			obj.setTipoRecebimento(TipoRecebimentoEnum.getEnumPorValor(dadosSQL.getString("tipoRecebimento")));
		}

		if (Uteis.isAtributoPreenchido(dadosSQL.getString("formaContratacao"))) {
			obj.setFormaContratacao(FormaContratacaoFuncionarioEnum.getEnumPorValor(dadosSQL.getString("formaContratacao")));
		}

		obj.setSituacaoFuncionario(dadosSQL.getString("situacaoFuncionario"));
		obj.setJornada(dadosSQL.getInt("jornada"));
		obj.setSalario(dadosSQL.getBigDecimal("salario"));
		obj.setUtilizaRH(dadosSQL.getBoolean("utilizaRH"));
		obj.setQuantidadeCargoFuncionario(dadosSQL.getInt("quantidadeCargoFuncionario"));
		obj.setComissionado(dadosSQL.getBoolean("comissionado"));
		obj.getSecaoFolhaPagamento().setCodigo(dadosSQL.getInt("secaofolhapagamento"));
		obj.getCargoAtual().setCodigo(dadosSQL.getInt("cargoatual"));

		obj.getNivelSalarial().setCodigo(dadosSQL.getInt("nivelsalarial"));
		obj.getFaixaSalarial().setCodigo(dadosSQL.getInt("faixasalarial"));

		obj.setDataBaseQuinquenio(dadosSQL.getDate("dataBaseQuinquenio"));

		if (Uteis.isAtributoPreenchido(dadosSQL.getString("previdencia"))) {
			obj.setPrevidencia(PrevidenciaEnum.valueOf(dadosSQL.getString("previdencia")));
		}

		obj.setOptanteTotal(dadosSQL.getBoolean("optanteTotal"));
		obj.setSalarioComposto(dadosSQL.getBoolean("salariocomposto"));
		obj.setSalarioCargoAtual(dadosSQL.getBigDecimal("salariocargoatual"));
		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("departamento"))) {
			obj.setDepartamento(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(dadosSQL.getInt("departamento"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}
		if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoContratacaoComissionado"))) {
			obj.setTipoContratacaoComissionado(TipoContratacaoComissionadoEnum.valueOf(dadosSQL.getString("tipoContratacaoComissionado")));
		}

		if (Uteis.NIVELMONTARDADOS_PROCESSAMENTO == nivelMontarDados) {
			return obj;
		}

		obj.setFuncionarioVO(Uteis.montarDadosVO(dadosSQL.getInt("funcionario"), FuncionarioVO.class, p -> getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(p, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
		if (Uteis.NIVELMONTARDADOS_DADOSMINIMOS == nivelMontarDados) {
			montarDadosCargo(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
			return obj;
		}

		if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS)
			return obj;

		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("departamento"))) {
			obj.setDepartamento(getFacadeFactory().getDepartamentoFacade().consultarPorChavePrimaria(dadosSQL.getInt("departamento"), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
		}

		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("sindicato"))) {
			obj.setSindicatoVO(getFacadeFactory().getSindicatoInterfaceFacade().consultarPorChavePrimaria(dadosSQL.getInt("sindicato"), usuario, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
		}

		if (Uteis.isAtributoPreenchido(obj.getSecaoFolhaPagamento()))
			obj.setSecaoFolhaPagamento(Uteis.montarDadosVO(dadosSQL.getInt("secaofolhapagamento"),SecaoFolhaPagamentoVO.class, p -> getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade()
							.consultarPorChavePrimaria(p.longValue())));

		if (Uteis.isAtributoPreenchido(dadosSQL.getInt("curso"))) {
			obj.setCursoVO(getFacadeFactory().getCursoFacade().consultarPorChavePrimaria(dadosSQL.getInt("curso"), Uteis.NIVELMONTARDADOS_DADOSBASICOS, false, usuario));
		}

		montarDadosCargo(obj, nivelMontarDados, usuario);
		montarUnidadeEnsino(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);

		obj.montarCentroCusto(obj.getFuncionarioVO());

		return obj;
	}


    public static void montarUnidadeEnsino(FuncionarioCargoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUnidade().getCodigo().intValue() == 0) {
            obj.setUnidade(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidade(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidade().getCodigo(), false, nivelMontarDados, usuario));
    }

	public static void montarDadosCargo(FuncionarioCargoVO obj, int nivelMontarDados, UsuarioVO usuario)
			throws Exception {
		if (obj.getCargo().getCodigo().intValue() == 0) {
			obj.setCargo(new CargoVO());
			return;
		}
		obj.setCargo(getFacadeFactory().getCargoFacade().consultarPorChavePrimaria(obj.getCargo().getCodigo(), false,nivelMontarDados, usuario));
	}    

    /**
     * Operação responsável por excluir todos os objetos da <code>FuncionarioCargoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>FuncionarioCargo</code>.
     * @param <code>funcionario</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirFuncionarioCargos(Integer funcionario) throws Exception {
        String sql = "DELETE FROM FuncionarioCargo WHERE (funcionario = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{funcionario.intValue()});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>FuncionarioCargoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirFuncionarioCargos</code> e <code>incluirFuncionarioCargos</code> disponíveis na classe <code>FuncionarioCargo</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarFuncionarioCargos(FuncionarioVO funcionario, List objetos) throws Exception {
    	String str = "DELETE FROM FuncionarioCargo WHERE funcionario = " + funcionario.getCodigo();
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
            FuncionarioCargoVO objeto = (FuncionarioCargoVO)i.next();
            str += " AND codigo <> " + objeto.getCodigo().intValue();
        }
        getConexao().getJdbcTemplate().update(str);
        Iterator e = objetos.iterator();
        while (e.hasNext()) {        	
            FuncionarioCargoVO objeto = (FuncionarioCargoVO) e.next();


            if(objeto.getFuncionarioVO().getCodigo().equals(0)){
                 objeto.getFuncionarioVO().setCodigo(funcionario.getCodigo());                
            }
            objeto.setFuncionarioVO(funcionario);
            validarDados(objeto);
            persitir(objeto);

           	if (objeto.getUtilizaRH()) {
           		getFacadeFactory().getHistoricoFuncaoInterfaceFacade().persistirPorFuncionarioCargo(objeto, false);
	            getFacadeFactory().getHistoricoSalarialInterfaceFacade().persistirPorFuncionarioCargo(objeto, false);
	            getFacadeFactory().getHistoricoSecaoInterfaceFacade().persistirPorFuncionarioCargo(objeto, false);
	            getFacadeFactory().getHistoricoSituacaoInterfaceFacade().persistirPorFuncionarioCargo(objeto, false);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>FuncionarioCargoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>administrativo.funcionario</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirFuncionarioCargos(Integer funcionarioPrm, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            FuncionarioCargoVO obj = (FuncionarioCargoVO) e.next();
            obj.getFuncionarioVO().setCodigo(funcionarioPrm);
            validarDados(obj);
            persitir(obj);

           	if (obj.getUtilizaRH()) {
           		getFacadeFactory().getHistoricoFuncaoInterfaceFacade().persistirPorFuncionarioCargo(obj, false);
	            getFacadeFactory().getHistoricoSalarialInterfaceFacade().persistirPorFuncionarioCargo(obj, false);
	            getFacadeFactory().getHistoricoSecaoInterfaceFacade().persistirPorFuncionarioCargo(obj, false);
	            getFacadeFactory().getHistoricoSituacaoInterfaceFacade().persistirPorFuncionarioCargo(obj, false);
	            
            }
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persitir(FuncionarioCargoVO obj) throws Exception {
    	if (obj.getCodigo() == 0 ) {
    		incluir(obj);
			if (getAplicacaoControle().getCliente().getPermitirAcessoModuloRH() && obj.getUtilizaRH()) {
				getFacadeFactory().getPeriodoAquisitivoFeriasInterfaceFacade().realizarAbrirNovoPeriodoAquisitivoParaFuncionarioCargo(obj);
			}
    	} else {
    		alterar(obj);
    	}
    }
    
    @Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarSituacaoFuncionarioCargo(final FuncionarioCargoVO funcionarioCargoVO, UsuarioVO usuarioVO) throws Exception {
    	validarDataDemissaoSituacaoFuncionario(funcionarioCargoVO);
		realizarAtivacaoConformeSituacaoFuncionario(funcionarioCargoVO);
		final StringBuilder sql = new StringBuilder("UPDATE funcionariocargo SET ativo = ?, situacaoFuncionario = ?, ")
				.append(" dataDemissao = ? WHERE codigo = ? ")
				.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
				sqlAlterar.setBoolean(1, funcionarioCargoVO.getAtivo());
				sqlAlterar.setString(2, funcionarioCargoVO.getSituacaoFuncionario());
				sqlAlterar.setDate(3, Uteis.getDataJDBC(funcionarioCargoVO.getDataDemissao()));
				sqlAlterar.setInt(4, funcionarioCargoVO.getCodigo());
				return sqlAlterar;
			}
		});
    }

    public void validarDados(FuncionarioCargoVO obj) throws ConsistirException {
		if (obj.getComissionado() == null || !obj.getComissionado()) {
			obj.setCargoAtual(new CargoVO());
		}
		
		validarDadosDuplicidadeFuncionarioCargo(obj);
	}

    /**
     * Operação responsável por consultar todos os <code>FuncionarioCargoVO</code> relacionados a um objeto da classe <code>administrativo.funcionario</code>.
     * @param funcionario  Atributo de <code>administrativo.funcionario</code> a ser utilizado para localizar os objetos da classe <code>FuncionarioCargoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>FuncionarioCargoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Override
    public List consultarFuncionarioCargos(Integer funcionario, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        FuncionarioCargo.consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList();
        String sql = "SELECT * FROM FuncionarioCargo WHERE funcionario = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, funcionario);
        while (resultado.next()) {
            FuncionarioCargoVO novoObj = new FuncionarioCargoVO();
            novoObj = montarDados(resultado, nivelMontarDados, usuario);
            objetos.add(novoObj);
        }
        return objetos;
    }


    /**
     * Operação responsável por localizar um objeto da classe <code>FuncionarioCargoVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public FuncionarioCargoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM FuncionarioCargo WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( FuncionarioCargo ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public FuncionarioCargoVO consultarPorCodigo(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM FuncionarioCargo WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            return null;
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public Boolean verificaUsuarioDepartamento(Integer codigoDepartamento, Integer usuario) throws Exception {
        String sql = "select cargo.departamento from funcionariocargo  "
                + " inner join funcionario on funcionario.codigo = funcionariocargo.funcionario "
                + " inner join pessoa on pessoa.codigo = funcionario.pessoa "
                + " inner join usuario on pessoa.codigo = usuario.pessoa "
                + " inner join cargo on cargo.codigo = funcionariocargo.cargo "
                + " where cargo.departamento = ? "
                + " and usuario.codigo = ? ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoDepartamento.intValue(), usuario.intValue()});
        if (!tabelaResultado.next()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return FuncionarioCargo.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        FuncionarioCargo.idEntidade = idEntidade;
    }

	@Override
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarAtivo(final FuncionarioCargoVO funcionarioCargoVO, final Integer funcionario, UsuarioVO usuarioVO) throws Exception {
		final String sql = "UPDATE funcionariocargo SET ativo = ? WHERE cargo = ?   and funcionario = ?" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
		getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
			public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
				PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
				sqlAlterar.setBoolean(1, funcionarioCargoVO.getAtivo());
				sqlAlterar.setInt(2, funcionarioCargoVO.getCargo().getCodigo());
				sqlAlterar.setInt(3, funcionario);
				return sqlAlterar;
			}
		});
    }
	
	@Override
	public List<FuncionarioCargoVO> consultarCargoPorCodigoFuncionario(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT funcionariocargo.* FROM funcionariocargo ");
		sql.append(" left join cargo on cargo.codigo = funcionariocargo.codigo ");
		sql.append(" left join funcionario on funcionario.codigo = funcionariocargo.funcionario");
		sql.append(" INNER JOIN pessoa ON pessoa.codigo = funcionario.pessoa");
		sql.append(" WHERE funcionario.codigo = ? and funcionariocargo.ativo = true ");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[] { codigoPrm.intValue() });
		return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
	}
	
	public StringBuilder getSQLFuncionarioCargo() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * from funcionariocargo "); 
		sql.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario  "); 
		sql.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa  ");
		sql.append(" inner join cargo on cargo.codigo = funcionariocargo.cargo  "); 
		sql.append(" inner join unidadeensino on unidadeensino.codigo = funcionariocargo.unidadeensino ");
		
		return sql;
	}
	
	public StringBuilder getSQLTotalizadorFuncionarioCargo() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT count(funcionariocargo.codigo) as qtde from funcionariocargo "); 
		sql.append(" inner join funcionario on funcionario.codigo = funcionariocargo.funcionario  "); 
		sql.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa  ");
		sql.append(" inner join cargo on cargo.codigo = funcionariocargo.cargo  ");
		sql.append(" inner join unidadeensino on unidadeensino.codigo = funcionariocargo.unidadeensino ");
		
		return sql;
	}
	
	@Override
	public List<FuncionarioCargoVO> consultarFuncionarioCargoAtivoParaRH(DataModelo dataModelo, String situacaoFuncionario) throws Exception {
		return consultarFuncionarioCargoAtivoParaRH(dataModelo, situacaoFuncionario, false);
	}
	
	@Override
	public List<FuncionarioCargoVO> consultarFuncionarioCargoAtivoParaRH(DataModelo dataModelo,
			String situacaoFuncionario, boolean consultarSomenteProfessores) throws Exception {
		ControleAcesso.consultar(getIdEntidade(), dataModelo.isControlarAcesso(), dataModelo.getUsuario());
        
		StringBuilder sql = new StringBuilder(getSQLFuncionarioCargo());
		sql.append(filtroFuncionarioCargoAtivoParaRH(dataModelo, situacaoFuncionario, Boolean.FALSE, consultarSomenteProfessores));
		UteisTexto.addLimitAndOffset(sql, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
        
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(),  dataModelo.getListaFiltros().toArray());
        return (montarDadosConsulta(tabelaResultado, dataModelo.getNivelMontarDados(), dataModelo.getUsuario()));
    }
	
	public List consultarFaixaSalarialPorCargo(Integer codigoCargo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT funcionariocargo.*, faixasalarial.codigo as \"faixasalarial.codigo\", faixasalarial.descricao as \"faixasalarial.codigo\" FROM funcionariocargo");
        sql.append("INNER JOIN cargo ON cargo.codigo = funcionariocargo.cargo");
        sql.append("INNER JOIN progressaosalarialitem ON progressaosalarialitem.progressaosalarial = cargo.progressaosalarial");
        sql.append("INNER JOIN faixasalarial ON progressaosalarialitem.faixasalarial = faixasalarial.codigo");
        sql.append("WHERE cargo.codigo = ?;");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoCargo);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
	 
	 public List consultarNivelSalarialPorCargo(Integer codigoCargo, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT funcionariocargo.*, nivelsalarial.codigo as \"nivelsalarial.codigo\" FROM funcionariocargo");
        sql.append("INNER JOIN cargo ON cargo.codigo = funcionariocargo.cargo");
        sql.append("INNER JOIN progressaosalarialitem ON progressaosalarialitem.progressaosalarial = cargo.progressaosalarial");
        sql.append("INNER JOIN faixasalarial ON progressaosalarialitem.faixasalarial = faixasalarial.codigo");
        sql.append("WHERE cargo.codigo = ?;");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoCargo);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }
	 
	private static void validarDadosDuplicidadeFuncionarioCargo(FuncionarioCargoVO obj) throws ConsistirException {
		if (!obj.getMatriculaCargo().isEmpty()) {
			int totalFuncionarioCargo = consultarDadosTotalFuncionarioCargoPorMatricula(obj);

			if (totalFuncionarioCargo > 0) {
				throw new ConsistirException(UteisJSF.internacionalizar("msg_FuncionarioCargo_DuplicidadeMatricula"));
			}
		}
	}

	private static int consultarDadosTotalFuncionarioCargoPorMatricula(FuncionarioCargoVO obj) {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT COUNT(codigo) as qtde FROM funcionariocargo");
		sql.append(" WHERE matriculacargo = ?");

		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			sql.append(" AND codigo != ?");
		}

		SqlRowSet rs = null;

		if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getMatriculaCargo(),
					obj.getCodigo());
		} else {
			rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getMatriculaCargo());
		}

		if (rs.next()) {
			return rs.getInt("qtde");
		}

		return 0;
	}
	
	 public FuncionarioCargoVO consultarPorMatriculaCargo(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
	        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
	        StringBuilder sqlStr = new StringBuilder("SELECT * FROM FuncionarioCargo WHERE matriculacargo = ? ORDER BY cargo");
	        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta);
	        
	        if (tabelaResultado.next()) {
	           return montarDados(tabelaResultado, nivelMontarDados, usuario);
	        }
	        return new FuncionarioCargoVO();
	    }
	 
	 public FuncionarioCargoVO consultarPorMatriculaCargoProfessor(String valorConsulta, boolean controlarAcesso,
				int nivelMontarDados, UsuarioVO usuario) throws Exception {
			ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT * FROM FuncionarioCargo ");
			sql.append(" INNER JOIN funcionario on funcionario.codigo = funcionariocargo.funcionario ");
			sql.append(" INNER JOIN pessoa on pessoa.codigo = funcionario.pessoa ");
			sql.append(" WHERE matriculacargo = ? and pessoa.professor = true ORDER BY cargo ");
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), valorConsulta);
			
			if (tabelaResultado.next()) {
				return montarDados(tabelaResultado, nivelMontarDados, usuario);
			}
			return new FuncionarioCargoVO();
		}
	    
	    public List<FuncionarioCargoVO> consultarPorMatriculaCargo(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
	        StringBuilder sb = new StringBuilder();
	        sb.append(" SELECT * FROM FuncionarioCargo WHERE matriculacargo like ? ORDER BY cargo ");
	        valorConsulta = "%" + valorConsulta + "%";
	        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta);
	        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	    }

	    public List<FuncionarioCargoVO> consultarPorMatriculaCargoAtivo(String valorConsulta, int nivelMontarDados, UsuarioVO usuario) throws Exception {
	    	StringBuilder sb = new StringBuilder();
	    	sb.append(" SELECT * FROM FuncionarioCargo WHERE matriculacargo = ? and situacaofuncionario = 'ATIVO' ORDER BY cargo");
	    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), valorConsulta);
	    	return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	    }

	    public FuncionarioCargoVO consultarPorChavePrimariaUnica(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
	    	
			ControleAcesso.consultar(getIdEntidade(), false, usuario);
			
			StringBuilder sql = new StringBuilder("SELECT * FROM FuncionarioCargo WHERE codigo = ?");
			
			SqlRowSet tabelaResultado;
			tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoPrm);
			if (tabelaResultado.next()) {
				return (montarDados(tabelaResultado, nivelMontarDados, usuario));
			}
			return (new FuncionarioCargoVO());
		}

	    @Override
		public String consultarMatriculaPorFuncionarioCargo(Integer codigo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT matriculacargo FROM FuncionarioCargo WHERE codigo = ?");
	        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
	        if (tabelaResultado.next()) {
				return tabelaResultado.getString("matriculacargo");
			}
			return null;
		}
	    
	    @Override
	    public List<FuncionarioCargoVO> consultarCargoFuncionarioPorFiltrosLancamentoFolhaPagamento(LancamentoFolhaPagamentoVO lancamento) {
			
	        StringBuilder sb = new StringBuilder(" SELECT * FROM FuncionarioCargo ");
	        List parametros = new ArrayList<>();
	        
	        if(lancamento.getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO().getCodigo() > 0) {
	        	sb.append(" WHERE codigo = ? and utilizarh = true");
	        	parametros.add(lancamento.getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO().getCodigo());
	        } else {
	        	sb.append(" where utilizarh = true ");
	        	
	        	if(lancamento.getTemplateLancamentoFolhaPagamento().getSecaoFolhaPagamento().getCodigo() > 0) {
	        		sb.append(" and secaofolhapagamento = ? ");
	        		parametros.add(lancamento.getTemplateLancamentoFolhaPagamento().getSecaoFolhaPagamento().getCodigo());
	        	}
	        	
	        	if(!lancamento.getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().isEmpty()) {
	        		sb.append(" and formacontratacao in ( ");
	        		sb.append(converterNomeDaFormaDeContratacaoDoFuncionarioParaCodigo(lancamento.getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario()));
	        		sb.append(" ) ");
	        	}
	        	
				if(!lancamento.getTemplateLancamentoFolhaPagamento().getTipoRecebimento().isEmpty()) {
					sb.append(" and tiporecebimento in ( ");
					sb.append(converterNomeTipoRecebimentoDoFuncionarioParaCodigo(lancamento.getTemplateLancamentoFolhaPagamento().getTipoRecebimento()));
					sb.append(" ) ");
	        	}
				
				if(!lancamento.getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().isEmpty()) {
					sb.append(" and situacaofuncionario in ( ");
					sb.append(converterNomeDaSituacaoDoFuncionarioParaCodigo(lancamento.getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario()));
					sb.append(" ) ");
				}
	        }
	        
	        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), parametros.toArray());
	        
	        try {
				return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, null));
			} catch (Exception e) {
				e.printStackTrace();
				return new ArrayList<>();
			}		
		}
	    
	    @Override
	    public List<FuncionarioCargoVO> consultarCargoFuncionarioPorFiltrosGrupoLancamentoFolhaPagamento(GrupoLancamentoFolhaPagamentoVO grupoLancamento) {
	    	
	    	StringBuilder sb = new StringBuilder(" SELECT * FROM FuncionarioCargo ");
	    	List parametros = new ArrayList<>();
	    	
	    	if(grupoLancamento.getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO().getCodigo() > 0) {
	    		sb.append(" WHERE codigo = ? and utilizarh = true");
	    		parametros.add(grupoLancamento.getTemplateLancamentoFolhaPagamento().getFuncionarioCargoVO().getCodigo());
	    	} else {
	    		sb.append(" where utilizarh = true ");
	    		
	    		if(grupoLancamento.getTemplateLancamentoFolhaPagamento().getSecaoFolhaPagamento().getCodigo() > 0) {
	    			sb.append(" and secaofolhapagamento = ? ");
	    			parametros.add(grupoLancamento.getTemplateLancamentoFolhaPagamento().getSecaoFolhaPagamento().getCodigo());
	    		}
	    		
	    		if(!grupoLancamento.getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario().isEmpty()) {
	    			sb.append(" and formacontratacao in ( ");
	    			sb.append(converterNomeDaFormaDeContratacaoDoFuncionarioParaCodigo(grupoLancamento.getTemplateLancamentoFolhaPagamento().getFormaContratacaoFuncionario()));
	    			sb.append(" ) ");
	    		}
	    		
	    		if(!grupoLancamento.getTemplateLancamentoFolhaPagamento().getTipoRecebimento().isEmpty()) {
	    			sb.append(" and tiporecebimento in ( ");
	    			sb.append(converterNomeTipoRecebimentoDoFuncionarioParaCodigo(grupoLancamento.getTemplateLancamentoFolhaPagamento().getTipoRecebimento()));
	    			sb.append(" ) ");
	    		}
	    		
	    		if(!grupoLancamento.getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario().isEmpty()) {
	    			sb.append(" and situacaofuncionario in ( ");
	    			sb.append(converterNomeDaSituacaoDoFuncionarioParaCodigo(grupoLancamento.getTemplateLancamentoFolhaPagamento().getSituacaoFuncionario()));
	    			sb.append(" ) ");
	    		}
	    	}
	    	
		    SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), parametros.toArray());
		    
		    try {
				return (montarDadosConsulta(tabelaResultado, Uteis.NIVELMONTARDADOS_COMBOBOX, null));
			} catch (Exception e) {
				e.printStackTrace();
				return new ArrayList<>();
			}		
		}

	    public String converterNomeDaFormaDeContratacaoDoFuncionarioParaCodigo(String formasDeContratacao) {
			StringBuilder codigosFormaContratacao = new StringBuilder("");
			for(String forma : formasDeContratacao.split(";")) {
				if(codigosFormaContratacao.length() > 0) {
					codigosFormaContratacao.append(", ");
				}
				FormaContratacaoFuncionarioEnum formaContratacao = FormaContratacaoFuncionarioEnum.getEnumPorName(forma);
				codigosFormaContratacao.append("'");
				codigosFormaContratacao.append(formaContratacao.getValor());
				codigosFormaContratacao.append("'");
			}
			return codigosFormaContratacao.toString();
		}
		
	    public String converterNomeTipoRecebimentoDoFuncionarioParaCodigo(String tiposDeRecebimentos) {
			StringBuilder codigosTipoRecebimento = new StringBuilder("");
			for(String forma : tiposDeRecebimentos.split(";")) {
				if(codigosTipoRecebimento.length() > 0) {
					codigosTipoRecebimento.append(", ");
				}
				TipoRecebimentoEnum tipoRecebimento = TipoRecebimentoEnum.getEnumPorName(forma);
				codigosTipoRecebimento.append("'");
				codigosTipoRecebimento.append(tipoRecebimento.getValor());
				codigosTipoRecebimento.append("'");
			}
			return codigosTipoRecebimento.toString();
		}
		
	    public String converterNomeDaSituacaoDoFuncionarioParaCodigo(String situacoesDoFuncionario) {
			StringBuilder codigosSituacoes = new StringBuilder("");
			for(String forma : situacoesDoFuncionario.split(";")) {
				if(codigosSituacoes.length() > 0) {
					codigosSituacoes.append(", ");
				}
				SituacaoFuncionarioEnum situacaoFuncionario = SituacaoFuncionarioEnum.getEnumPorName(forma);
				codigosSituacoes.append("'");
				codigosSituacoes.append(situacaoFuncionario.getValor());
				codigosSituacoes.append("'");
			}
			return codigosSituacoes.toString();
		}
	    
	    @Override
		public List<FuncionarioCargoVO> consultarCargoFuncionarioPorFiltrosTemplateFolhaPagamento(TemplateLancamentoFolhaPagamentoVO templateLancamentoFolhaPagamento, Integer nivelMontarDados) throws Exception {
			
	    	StringBuilder sb = new StringBuilder(" SELECT * FROM FuncionarioCargo fc where 1=1 ");

	    	StringBuilder filtros = new StringBuilder(getFacadeFactory().getTemplateLancamentoFolhaPagamentoInterfaceFacade().getFiltrosDoTemplate(templateLancamentoFolhaPagamento));
	    	
	    	if(filtros.toString().trim().length() > 0)
	    		sb.append(filtros);
	    	else
	    		throw new ConsistirException(UteisJSF.internacionalizar("msg_erro_LancamentoFolhaPagamento_nenhumFuncionarioSelecionado"));
	    	
	    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		    
		    try {
				return (montarDadosConsulta(tabelaResultado, nivelMontarDados, null));
			} catch (Exception e) {
				e.printStackTrace();
				return new ArrayList<>();
			}
		}
	    
	    public void montarClausulaWhereComSituacaoFuncionarioCargo(String situacaoFuncionario, StringBuilder where) {
			if (situacaoFuncionario.equals("TODOS")) {
				where.append(" AND situacaofuncionario IN (");
				int contador = 1;
				for (SituacaoFuncionarioEnum situacao : SituacaoFuncionarioEnum.values()) {
					if (situacao.name().equals("ATIVO")) {
						where.append(" '").append(situacao.name()).append("'");
					} else {
						where.append(" '").append(situacao.getValor()).append("'");
					}

				}
				where.append(")");
			} else {
				where.append(" AND situacaoFuncionario = '").append(situacaoFuncionario).append("'");
			}
		}
	    
	    public Integer consultarTotalPorFuncionarioCargo(DataModelo dataModelo, String situacaoFuncionario) {
			return consultarTotalPorFuncionarioCargo(dataModelo, situacaoFuncionario, false);
		}

	    /**
		 * Consulta o total de funcionario cargo para o paginador das telas de consulta dos Eventos do RH.
		 */
		@Transactional(readOnly = false, rollbackFor = { Throwable.class }, propagation = Propagation.SUPPORTS)
		public Integer consultarTotalPorFuncionarioCargo(DataModelo dataModelo, String situacaoFuncionario, boolean consultarSomenteProfessores) {
			try {
				StringBuilder sqlStr = new StringBuilder(getSQLTotalizadorFuncionarioCargo());
				sqlStr.append(filtroFuncionarioCargoAtivoParaRH(dataModelo, situacaoFuncionario, Boolean.FALSE, consultarSomenteProfessores));
				SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), dataModelo.getListaFiltros().toArray());
				return (Integer) Uteis.getSqlRowSetTotalizador(rs, "qtde", TipoCampoEnum.INTEIRO);
			} catch (Exception e) {
				throw new StreamSeiException(e.getMessage());
			}
		}
		
		public StringBuilder filtroFuncionarioCargoAtivoParaRH(DataModelo dataModelo, String situacaoFuncionario, boolean totalizador) {
			return filtroFuncionarioCargoAtivoParaRH(dataModelo, situacaoFuncionario, totalizador, false);
		}
		
		/**
		 * Monta a consulta Sql para os eventos do funcionario para o RH.
		 * 
		 * @param dataModelo
		 * @param situacaoFuncionario
		 * @return
		 */
		public StringBuilder filtroFuncionarioCargoAtivoParaRH(DataModelo dataModelo, String situacaoFuncionario,
				boolean totalizador, boolean consultarSomenteProfessores) {
			StringBuilder where = new StringBuilder(" where utilizarh = true ");
			where.append(montarSituacaoFuncionarioCargo(situacaoFuncionario));
			
			if (consultarSomenteProfessores) {
				where.append(" and pessoa.professor = true");
			}

			StringBuilder orderBy = new StringBuilder("");

			switch (EnumCampoConsultaSalarioComposto.valueOf(dataModelo.getCampoConsulta())) {
			case FUNCIONARIO:
				dataModelo.setNivelMontarDados(Uteis.NIVELMONTARDADOS_COMBOBOX);
				dataModelo.getListaFiltros().clear();
				dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);
				where.append(" and lower(pessoa.nome) like(lower(?)) ");
				if (totalizador) {
					orderBy.append(" ORDER BY pessoa.nome asc ");
				}
				break;
			case MATRICULA_CARGO:
				dataModelo.setNivelMontarDados(Uteis.NIVELMONTARDADOS_COMBOBOX);
				dataModelo.getListaFiltros().clear();
				dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);

				where.append("  and funcionariocargo.matriculacargo like(?)  ");
				if (totalizador) {
					orderBy.append(" ORDER BY funcionariocargo.matriculacargo asc ");
				}

				break;
			case MATRICULA_FUNCIONARIO:
				dataModelo.setNivelMontarDados(Uteis.NIVELMONTARDADOS_COMBOBOX);
				dataModelo.getListaFiltros().clear();
				dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta() + PERCENT);

				where.append(" and funcionario.matricula like(?) ");
				if (totalizador) {
					orderBy.append(" ORDER BY funcionario.matricula asc ");
				}

				break;
			case CARGO :
				dataModelo.setNivelMontarDados(Uteis.NIVELMONTARDADOS_COMBOBOX);
				dataModelo.getListaFiltros().clear();
				dataModelo.getListaFiltros().add(PERCENT + dataModelo.getValorConsulta().toUpperCase() + PERCENT);

				where.append(" and upper(cargo.nome) like(upper(?)) ");
				if (totalizador) {
					orderBy.append(" ORDER BY cargo.nome asc ");
				}
			default:
				break;
			}
			return where.append(orderBy);
		}

		/**
		 * Monta o filtro sql de condição da situação do funcionario cargo.
		 *  
		 * @param situacaoFuncionario
		 * @param where
		 */
		private StringBuilder montarSituacaoFuncionarioCargo(String situacaoFuncionario) {
			StringBuilder sql = new StringBuilder();
			if (situacaoFuncionario.equals("TODOS")) {
				sql.append(" AND situacaofuncionario IN (");
				int contador = 1;
				for (SituacaoFuncionarioEnum situacao : SituacaoFuncionarioEnum.values()) {
					if (situacao.name().equals("ATIVO")) {
						sql.append(" '").append(situacao.name()).append("'");
					} else {
						sql.append(" '").append(situacao.getValor()).append("'");
					}

					if (SituacaoFuncionarioEnum.values().length != contador) {
						sql.append(",");
						contador++;
					}
				}
				sql.append(")");
			} else {
				sql.append(" AND situacaoFuncionario = '").append(situacaoFuncionario).append("'");
			}
			return sql;
		}
		
		/**
		 * Atualiza situacao do funcionario.
		 * 
		 * @param codigoFuncionarioCargo
		 * @param situacao - SituacaoFuncionarioEnum.ATIVO
		 * @param usuarioVO
		 * @throws Exception
		 */
		@Override
		@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
		public void alterarSituacaoFuncionario(Integer codigoFuncionarioCargo, String situacao, UsuarioVO usuarioVO) throws Exception {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE funcionariocargo SET situacaoFuncionario = ? WHERE codigo = ?").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));
			
			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setString(1, situacao);
					sqlAlterar.setInt(2, codigoFuncionarioCargo);
					return sqlAlterar;
				}
			});
		}

		/**
		 * Atualiza situacao do funcionario e data.
		 * 
		 * @param codigoFuncionarioCargo
		 * @param dataDemisssao 
		 * @param usuarioVO
		 * @throws Exception
		 */
		@Override
		@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
		public void alterarSituacaoFuncionarioDataDemissao(Integer codigoFuncionarioCargo, Date dataDemisssao, String situacaoFuncionario,
				UsuarioVO usuarioVO) throws Exception {
			StringBuilder sql = new StringBuilder();
			sql.append("UPDATE funcionariocargo SET situacaoFuncionario = ?, datademissao = ? WHERE codigo = ?")
					.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO));

			getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
				public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
					PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
					sqlAlterar.setString(1, situacaoFuncionario);
					sqlAlterar.setDate(2, Uteis.getDataJDBC(dataDemisssao));
					sqlAlterar.setInt(3, codigoFuncionarioCargo);
					return sqlAlterar;
				}
			});
		}
		
		/**
		 * Consulta lista com FuncionarioCargoVO pelos filtros
		 *  
		 * @param situacao
		 * @param utilizaRH
		 * @return
		 * @throws Exception
		 */
		@Override
		public List<FuncionarioCargoVO> consultarListaFuncionarioPorSituacao(SituacaoFuncionarioEnum situacao, Boolean utilizaRH, int nivelMontarDados) throws Exception{
			
			StringBuilder sql = new StringBuilder();
			
			sql.append(getSQLFuncionarioCargo());
			sql.append(" where situacaoFuncionario = ? ");
			sql.append(" and utilizaRH = ? order by pessoa.nome asc ");
			
			List<Object> filtros = new ArrayList<>();
			filtros.add(situacao.getValor());
			filtros.add(utilizaRH);
			
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());
			
		    return (montarDadosConsulta(tabelaResultado, nivelMontarDados, null));			
		}
		
		/**
		 * Consulta os funcionarios cargos agrupados por seção retornando uma lista de mapas
		 * contendo total de Funcionario por cada seção
		 * 
		 * @return List<Map> contendo as seguintes Chaves: "totalFuncionario" é "secao"
		 * @throws Exception
		 */
		@Override
		public List<Map<String, Object>> consultarPorSecao() throws Exception {
			StringBuilder sql = new StringBuilder();

			sql.append(" SELECT count(codigo) as totalFuncionario, secaofolhapagamento FROM funcionariocargo GROUP BY secaofolhapagamento");
			sql.append(" ORDER BY secaofolhapagamento");

			Map<String, Object> mapa = new HashMap<>();
			List<Map<String, Object>> lista = new ArrayList();

			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			while(tabelaResultado.next()) {
				mapa.put("totalFuncionario", tabelaResultado.getInt("totalFuncionario"));
				if (Uteis.isAtributoPreenchido(tabelaResultado.getInt("secaofolhapagamento"))) {
					mapa.put("secao", getFacadeFactory().getSecaoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(tabelaResultado.getLong("secaofolhapagamento")).getIdentificador());
				} else {
					mapa.put("secao", "");
				}
				lista.add(mapa);
				mapa = new HashMap<>();
			}
			return lista;
		}
		
		/**
		 * Consulta os funcionarios cargos agrupados por forma de contratação retornando uma lista de mapas
		 * contendo total de Funcionario por cada forma de contratação
		 * 
		 * @return List<Map> contendo as seguintes Chaves: "totalFuncionario" é "formacontratacao"
		 * @throws Exception
		 */
		@Override
		public List<Map<String, Object>> consultarPorFormaContratacao() throws Exception {
			StringBuilder sql = new StringBuilder();

			sql.append(" SELECT count(codigo) as totalFuncionario, formacontratacao FROM funcionariocargo GROUP BY formacontratacao");
			sql.append(" ORDER BY formacontratacao");

			Map<String, Object> mapa = new HashMap<>();
			List<Map<String, Object>> lista = new ArrayList();

			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			while(tabelaResultado.next()) {
				mapa.put("totalFuncionario", tabelaResultado.getInt("totalFuncionario"));
				if (Uteis.isAtributoPreenchido(tabelaResultado.getString("formacontratacao"))) {
					mapa.put("formacontratacao", FormaContratacaoFuncionarioEnum.valueOf( tabelaResultado.getString("formacontratacao")).getValorApresentar());
				} else {
					mapa.put("formacontratacao", "");
				}
				lista.add(mapa);
				mapa = new HashMap<>();
			}
			return lista;
		}

		/**
		 * Consulta os funcionarios cargos agrupados por situação do Funcionario retornando uma lista de mapas
		 * contendo total de Funcionario por cada situação
		 * 
		 * @return List<Map> contendo as seguintes Chaves: "totalFuncionario" é "situacaofuncionario"
		 * @throws Exception
		 */
		@Override
		public List<Map<String, Object>> consultarPorSituacaoFuncionarioCargo() throws Exception {
			StringBuilder sql = new StringBuilder();

			sql.append(" SELECT count(codigo) as totalFuncionario, situacaofuncionario FROM funcionariocargo GROUP BY situacaofuncionario");
			sql.append(" ORDER BY situacaofuncionario");

			Map<String, Object> mapa = new HashMap<>();
			List<Map<String, Object>> lista = new ArrayList();

			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
			while(tabelaResultado.next()) {
				mapa.put("totalFuncionario", tabelaResultado.getInt("totalFuncionario"));
				if (Uteis.isAtributoPreenchido(tabelaResultado.getString("situacaofuncionario"))) {
					mapa.put("situacaofuncionario", SituacaoFuncionarioEnum.valueOf( tabelaResultado.getString("situacaofuncionario")).getValorApresentar());
				} else {
					mapa.put("situacaofuncionario", "");
				}
				
				lista.add(mapa);
				mapa = new HashMap<>();
			}
			return lista;
		}
		
		/**
		 * Consulta os {@link FuncionarioCargoVO} pela situação do funcionário cargo informado e o 
		 * código da pessoa.
		 * 
		 */
		@Override
		public List<FuncionarioCargoVO> consultarPorPessoaESituacaoFuncionario(Integer codigo, SituacaoFuncionarioEnum ativo, int nivelmontardadosDadosbasicos, UsuarioVO usuarioLogado) throws Exception {
			StringBuilder sql = new StringBuilder();
			sql.append(" select fc.codigo, pessoa.nome, fc.matriculacargo from funcionariocargo fc ");
			sql.append(" left join funcionario f on f.codigo = fc.funcionario");
			sql.append(" left join pessoa on f.pessoa = pessoa.codigo");
			sql.append(" where pessoa.codigo = ? and fc.situacaofuncionario = ?;");

			List<FuncionarioCargoVO> lista = new ArrayList<>();
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo, ativo.toString());
			while(tabelaResultado.next()) {
				FuncionarioCargoVO obj = new FuncionarioCargoVO();
				obj.setCodigo(tabelaResultado.getInt("codigo"));
				obj.getFuncionarioVO().getPessoa().setNome(tabelaResultado.getString("nome"));
				obj.setMatriculaCargo(tabelaResultado.getString("matriculacargo"));
				lista.add(obj);
			}
			return lista;
		}

	@Override
	public List<FuncionarioCargoVO> consultarProfessoresCoordenador(String valorConsulta, String tipoConsulta, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select distinct pessoa.codigo as pessoa_codigo, pessoa.nome, funcionarioprofessor.matricula as matriculafuncionarioprofessor, funcionarioprofessor.codigo AS codigofuncionarioprofessor, pessoa.cpf, coordenador.nome as coordenador, coordenador.codigo, ");
		sb.append(" fc.codigo as fc_codigo, fc.matriculacargo as fc_matriculacargo, cargo.codigo as cargo_codigo, cargo.nome as cargo_nome");
		sb.append(" from turma ");
		sb.append(" inner join horarioturma on horarioturma.turma = turma.codigo ");
		sb.append(" inner join horarioturmadia on horarioturma.codigo = horarioturmadia.horarioturma ");
		sb.append(" inner join horarioturmadiaitem on horarioturmadiaitem.horarioturmadia = horarioturmadia.codigo ");
		sb.append(" inner join pessoa on horarioturmadiaitem.professor = pessoa.codigo ");
		sb.append(" inner join curso on ((turma.turmaagrupada = false and curso.codigo = turma.curso) ");
		sb.append(" or (turma.turmaagrupada and curso.codigo in (select t.curso from turmaagrupada ta inner join turma as t on t.codigo = ta.turma where ta.turmaorigem = turma.codigo) )) ");
		sb.append(" inner join funcionario as funcionarioprofessor on pessoa.codigo = funcionarioprofessor.pessoa ");
		sb.append(" inner join cursocoordenador on (cursocoordenador.turma is null and cursocoordenador.curso = curso.codigo) or ");
		sb.append(" (cursocoordenador.turma is not null and ( ");
		sb.append(" (turma.turmaagrupada = false and cursocoordenador.turma = turma.codigo) ");
		sb.append(" or (turma.turmaagrupada = false and turma.subturma and turma.turmaprincipal = cursocoordenador.turma) ");
		sb.append(" or (turma.turmaagrupada and cursocoordenador.turma in (select t.codigo from turmaagrupada ta inner join turma as t on t.codigo = ta.turma where ta.turmaorigem = turma.codigo)))) ");
		sb.append(" inner join funcionario on funcionario.codigo = cursocoordenador.funcionario ");
		sb.append(" inner join pessoa as coordenador on funcionario.pessoa = coordenador.codigo ");
		sb.append(" inner join funcionariocargo as fc on fc.funcionario = funcionarioprofessor.codigo ");
		sb.append(" left join cargo on cargo.codigo = fc.cargo");
		sb.append(" where coordenador.codigo = ").append(usuario.getPessoa().getCodigo());
		sb.append(" and fc.situacaofuncionario = 'ATIVO'");
		if (tipoConsulta.equals("nome")) {
			sb.append(" and lower (sem_acentos(pessoa.nome)) like(sem_acentos('" + valorConsulta.toLowerCase() + "%')) ");
		} else {
			sb.append(" and funcionarioprofessor.matricula = '").append(valorConsulta).append("'");
		}
		sb.append(" order by pessoa.nome");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<FuncionarioCargoVO> vetResultado = new ArrayList<FuncionarioCargoVO>(0);

		while (tabelaResultado.next()) {
			FuncionarioCargoVO obj = new FuncionarioCargoVO();
			obj.getFuncionarioVO().setCodigo(new Integer(tabelaResultado.getInt("codigofuncionarioprofessor")));
			obj.getFuncionarioVO().setMatricula(new String(tabelaResultado.getString("matriculafuncionarioprofessor")));
			obj.getFuncionarioVO().getPessoa().setNome(tabelaResultado.getString("nome"));
			obj.getFuncionarioVO().getPessoa().setCodigo(tabelaResultado.getInt("pessoa_codigo"));
			obj.getFuncionarioVO().getPessoa().setCPF(tabelaResultado.getString("cpf"));
			obj.setCodigo(tabelaResultado.getInt("fc_codigo"));
			obj.setMatriculaCargo(tabelaResultado.getString("fc_matriculacargo"));
			obj.getCargo().setCodigo(tabelaResultado.getInt("cargo_codigo"));
			obj.getCargo().setNome(tabelaResultado.getString("cargo_nome"));
			vetResultado.add(obj);
		}
		return vetResultado;
		
	}

	@Override
	public List<FuncionarioCargoVO> consultarPorNomeFuncionarioAtivoProfessor(DataModelo dataModelo, int nivelMontarDados,
			UsuarioVO usuario, boolean professor) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select funcionarioCargo.* from funcionarioCargo ");
		sb.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo ");
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		if (usuario.getIsApresentarVisaoCoordenador()) {
			sb.append(" inner join curso on funcionariocargo.curso = curso.codigo");
		}
		sb.append(" where lower(pessoa.nome) like(?) and situacaofuncionario = 'ATIVO' ");
		if (professor) {
			sb.append(" and pessoa.professor");
		}

		sb.append(" order by pessoa.nome ");
		UteisTexto.addLimitAndOffset(sb, dataModelo.getLimitePorPagina(), dataModelo.getOffset());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(), PERCENT + dataModelo.getValorConsulta().toLowerCase() + PERCENT);
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public Integer consultarTotalPorNomeFuncionarioAtivoProfessor(DataModelo dataModelo, int nivelMontarDados,
			UsuarioVO usuario, boolean professor) {
		StringBuilder sb = new StringBuilder();
		sb.append(" select count(funcionarioCargo.codigo) as qtde from funcionarioCargo ");
		sb.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo ");
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		if (usuario.getIsApresentarVisaoCoordenador()) {
			sb.append(" inner join curso on funcionariocargo.curso = curso.codigo");
		}
		sb.append(" where lower(pessoa.nome) like(?) and situacaofuncionario = 'ATIVO' ");
		if (professor) {
			sb.append(" and pessoa.professor");
		}

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString(),
				PERCENT + dataModelo.getValorConsulta().toLowerCase() + PERCENT);
		return (Integer) Uteis.getSqlRowSetTotalizador(tabelaResultado, "qtde", TipoCampoEnum.INTEIRO);
	}

	public List<FuncionarioCargoVO> consultarTodosFuncionariosCargoAtivo(int nivelMontarDados, UsuarioVO usuario) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append(" select funcionarioCargo.* from funcionarioCargo ");
		sb.append(" inner join funcionario on funcionariocargo.funcionario = funcionario.codigo ");
		sb.append(" inner join pessoa on pessoa.codigo = funcionario.pessoa ");
		sb.append(" where situacaofuncionario = 'ATIVO' ");
		sb.append(" order by pessoa.nome ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
	}
	
	@Override
	public List<FuncionarioCargoVO> consultarListaFuncionarioPorSituacaoDiferenteDeDemitidoLicencaSemVencimentoOutros(Boolean utilizaRH, int nivelMontarDados) throws Exception {
		StringBuilder sql = new StringBuilder();
		
		sql.append(getSQLFuncionarioCargo());
		sql.append(" where situacaoFuncionario  not in ('DEMITIDO', 'LICENCA_SEM_VENCIMENTO', 'OUTROS') ");
		sql.append(" and utilizaRH = ? order by pessoa.nome asc ");

		List<Object> filtros = new ArrayList<>();
		filtros.add(utilizaRH);

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), filtros.toArray());

		return (montarDadosConsulta(tabelaResultado, nivelMontarDados, null));
	}
	
	private void validarDataDemissaoSituacaoFuncionario(final FuncionarioCargoVO funcionarioCargoVO) throws ConsistirException {
		if(funcionarioCargoVO.getSituacaoFuncionario().equals(SituacaoFuncionarioEnum.DEMITIDO.getValor()) && funcionarioCargoVO.getDataDemissao() == null) {
			throw new ConsistirException(UteisJSF.internacionalizar("prt_FuncionarioCargo_DataDemissaoObrigatorio"));
		}
	}
	
	private void realizarAtivacaoConformeSituacaoFuncionario(final FuncionarioCargoVO obj) {
		SituacaoFuncionarioEnum situacaoFuncionarioEnum = SituacaoFuncionarioEnum.getEnumPorName(obj.getSituacaoFuncionario());
		obj.setAtivo(situacaoFuncionarioEnum != null && situacaoFuncionarioEnum.getAtivo());
	}
}