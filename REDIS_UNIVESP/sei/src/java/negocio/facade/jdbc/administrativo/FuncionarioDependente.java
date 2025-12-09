package negocio.facade.jdbc.administrativo;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.script.ScriptEngine;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.FuncionarioDependenteVO;
import negocio.comuns.administrativo.FuncionarioCargoVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.enumeradores.GrauParentescoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.recursoshumanos.ContraChequeVO;
import negocio.comuns.recursoshumanos.EventoFolhaPagamentoVO;
import negocio.comuns.recursoshumanos.FormulaFolhaPagamentoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.faturamento.nfe.UteisData;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.FuncionarioDependenteInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>FuncionarioCargoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>FuncionarioCargoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see FuncionarioCargoVO
 * @see ControleAcesso
 * @see funcionario
 */
@Lazy
@Repository
@Scope("singleton")
@SuppressWarnings({"unchecked", "rawtypes"})
public class FuncionarioDependente extends ControleAcesso implements FuncionarioDependenteInterfaceFacade {

	private static final long serialVersionUID = -5943128458938271132L;
	
	protected static String idEntidade;

    public FuncionarioDependente() throws Exception {
        super();
        setIdEntidade("Funcionario");
    }
    
    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return FuncionarioDependente.idEntidade;
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>FuncionarioCargoVO</code>.
     */
    public FuncionarioDependenteVO novo() throws Exception {
        FuncionarioDependente.incluir(getIdEntidade());
        return new FuncionarioDependenteVO();
    }

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final FuncionarioDependenteVO obj) throws Exception {
		this.validarDados(obj);
        
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
            	
            	final StringBuilder sql = new StringBuilder("INSERT INTO FuncionarioDependente ")
                		.append("( funcionario, cpf, parentesco, sexo, estadocivil, ")
                		.append(" datanascimento, localnascimento, cartorio, registro, livro, ")
                		.append(" folha, dataentregacertidao, numerosus, universitarioescolatecnica, cartaovacina, ")
                		.append(" frequenciaescolar, telefone, email, nome, pensao, ")
                		.append(" inss, irrf, salariofamilia, tipo, percentual, ")
                		.append(" banco, agencia, conta, operacao, nomeresponsavel, ")
                		.append(" datainiciodesconto, cpfresponsavel , formulafolhapagamento, eventoFolhaPagamento) ")
                		.append(" VALUES ( ?, ?, ?, ?, ?, ")
                		.append(" ?, ?, ?, ?, ?, ")
                		.append(" ?, ?, ?, ?, ?, ")
                		.append(" ?, ?, ?, ?, ?, ")
                		.append(" ?, ?, ?, ?, ?, ")
                		.append(" ?, ?, ?, ?, ?, ")
                		.append(" ?, ? , ?, ?) returning codigo ");
            	
                PreparedStatement sqlInserir = con.prepareStatement(sql.toString());
                int i = 0;
                Uteis.setValuePreparedStatement(obj.getFuncionarioVO(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getCpf(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getParentesco(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getSexo(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getEstadoCivil(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(Uteis.getDataJDBC(obj.getDataNascimento()), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getLocalNascimento(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getCartorio(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getRegistro(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getLivro(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getFolha(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(Uteis.getDataJDBC(obj.getDataEntregaCertidao()), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getNumeroSus(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getUniversitarioEscolaTecnica(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getCartaoVacina(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getFrequenciaEscolar(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getTelefone(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getEmail(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getNome(), ++i, sqlInserir);
                
                Uteis.setValuePreparedStatement(obj.getPensao(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getInss(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getIrrf(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getSalarioFamilia(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getTipo(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getPercentual(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getBanco(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getAgencia(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getConta(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getOperacao(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getNomeResponsavel(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getDataInicioDesconto(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getCpfResponsavel(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getFormulaCalculo(), ++i, sqlInserir);
                Uteis.setValuePreparedStatement(obj.getEventoFolhaPagamento(), ++i, sqlInserir);

                return sqlInserir;
            }
        }, new ResultSetExtractor() {

            public Object extractData(ResultSet rs) throws SQLException {
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
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>FuncionarioCargoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>FuncionarioCargoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final FuncionarioDependenteVO obj) throws Exception {
    	this.validarDados(obj);
        FuncionarioDependente.alterar(getIdEntidade());

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

            	StringBuilder sql = new StringBuilder("UPDATE FuncionarioDependente set parentesco=?, sexo=?, estadocivil=?, datanascimento=?, localnascimento=?, ")
                		.append(" cartorio=?, registro=?, livro=?, folha=?, dataentregacertidao=?, ")
                		.append(" numerosus=?, universitarioescolatecnica=?, cartaovacina=?, frequenciaescolar=?, telefone=?, ")
                		.append(" email=?, nome=?, pensao=?, inss=?, irrf=?, ") 
                		.append(" salarioFamilia=?, tipo=?, percentual=?, banco=?, ") 
                		.append(" agencia=?, conta=?, operacao=?, nomeresponsavel=?, ")
                		.append(" datainiciodesconto=?, cpfresponsavel=?, formulafolhapagamento=? , eventoFolhaPagamento=?, cpf = ? ").append(" WHERE codigo = ? ");
            	
            	PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
            	int i = 0;
            	Uteis.setValuePreparedStatement(obj.getParentesco(), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(obj.getSexo(), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(obj.getEstadoCivil(), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(Uteis.getDataJDBC(obj.getDataNascimento()), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(obj.getLocalNascimento(), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(obj.getCartorio(), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(obj.getRegistro(), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(obj.getLivro(), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(obj.getFolha(), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(Uteis.getDataJDBC(obj.getDataEntregaCertidao()), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(obj.getNumeroSus(), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(obj.getUniversitarioEscolaTecnica(), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(obj.getCartaoVacina(), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(obj.getFrequenciaEscolar(), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(obj.getTelefone(), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(obj.getEmail(), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(obj.getNome(), ++i, sqlAlterar);
                
                Uteis.setValuePreparedStatement(obj.getPensao(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getInss(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getIrrf(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getSalarioFamilia(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getTipo(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getPercentual(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getBanco(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getAgencia(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getConta(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getOperacao(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getNomeResponsavel(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getDataInicioDesconto(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getCpfResponsavel(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getFormulaCalculo(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getEventoFolhaPagamento(), ++i, sqlAlterar);
                Uteis.setValuePreparedStatement(obj.getCpf(), ++i, sqlAlterar);
                
                Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
                
                return sqlAlterar;
            }
        });
    }
    
    @Override
    public void validarDados(FuncionarioDependenteVO obj) throws ConsistirException {

		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if ((obj.getNome() == null) || (obj.getNome().trim().isEmpty())) {
			throw new ConsistirException("O campo NOME deve ser informado.");
		}
		if ((obj.getParentesco() == null) || (obj.getParentesco().trim().isEmpty())) {
			throw new ConsistirException("O campo GRAU DE PARENTESCO deve ser informado.");
		}
		if ((obj.getSexo() == null) || (obj.getSexo().trim().isEmpty())) {
			throw new ConsistirException("O campo SEXO deve ser informado.");
		}

		if (obj.getTelefone().length() > 20) {
			throw new ConsistirException("Campo TELEFONE inválido.");
		}
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
    public void excluir(FuncionarioDependenteVO obj) throws Exception {
        FuncionarioDependente.excluir(getIdEntidade());
        String sql = "DELETE FROM FuncionarioDependente WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, obj.getCodigo());
    }

    public List consultarPorFuncionario(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM FuncionarioDependente WHERE funcionario = ? ORDER BY codigo desc ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.intValue());
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
        String sqlStr = "SELECT * FROM FuncionarioDependente WHERE codigo = ? ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta.intValue());
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
     * Responsavel por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>FuncionarioCargoVO</code>.
     * @return  O objeto da classe <code>FuncionarioCargoVO</code> com os dados devidamente montados.
     */
    public static FuncionarioDependenteVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
    	FuncionarioDependenteVO obj = new FuncionarioDependenteVO();
    	obj.setNovoObj(Boolean.FALSE);
    	
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setCpf(dadosSQL.getString("cpf"));
        obj.setEmail(dadosSQL.getString("email"));
        obj.setTelefone(dadosSQL.getString("telefone"));
        obj.getFuncionarioVO().setCodigo(dadosSQL.getInt("funcionario"));
        
        montarDadosFuncionario(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        
        obj.setParentesco(dadosSQL.getString("parentesco"));
        obj.setSexo(dadosSQL.getString("sexo"));
        obj.setEstadoCivil(dadosSQL.getString("estadoCivil"));
        obj.setDataNascimento(dadosSQL.getDate("DataNascimento"));
        obj.setLocalNascimento(dadosSQL.getString("localNascimento"));
        obj.setCartorio(dadosSQL.getString("cartorio"));
        obj.setRegistro(dadosSQL.getString("registro"));
        obj.setLivro(dadosSQL.getString("livro"));
        obj.setFolha(dadosSQL.getString("folha"));
        obj.setDataEntregaCertidao(dadosSQL.getDate("dataEntregaCertidao"));
        obj.setNumeroSus(dadosSQL.getString("numeroSus"));
        obj.setUniversitarioEscolaTecnica(dadosSQL.getBoolean("universitarioEscolaTecnica"));
        obj.setCartaoVacina(dadosSQL.getBoolean("cartaoVacina"));
        obj.setFrequenciaEscolar(dadosSQL.getBoolean("frequenciaEscolar"));
        
        //Sprint 2 - Dados de pensao
        obj.setPensao(dadosSQL.getBoolean("pensao"));
        obj.setInss(dadosSQL.getBoolean("inss"));
        obj.setIrrf(dadosSQL.getBoolean("irrf"));
        obj.setSalarioFamilia(dadosSQL.getBoolean("salarioFamilia"));
        obj.setTipo(dadosSQL.getString("tipo"));
        obj.setPercentual(dadosSQL.getBigDecimal("percentual"));
        obj.setBanco(dadosSQL.getString("banco"));
        obj.setAgencia(dadosSQL.getString("agencia"));
        obj.setConta(dadosSQL.getString("conta"));
        obj.setOperacao(dadosSQL.getString("operacao"));
        obj.setNomeResponsavel(dadosSQL.getString("nomeresponsavel"));
        obj.setDataInicioDesconto(dadosSQL.getDate("datainiciodesconto"));
        obj.setCpfResponsavel(dadosSQL.getString("cpfresponsavel"));
        obj.setFormulaCalculo(Uteis.montarDadosVO(dadosSQL.getInt("formulafolhapagamento"), FormulaFolhaPagamentoVO.class, p -> getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p)));
        obj.setEventoFolhaPagamento(Uteis.montarDadosVO(dadosSQL.getInt("eventoFolhaPagamento"), EventoFolhaPagamentoVO.class, p -> getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().consultarPorChavePrimaria(p, usuario, nivelMontarDados)));

        return obj;
    }

    public static void montarDadosFuncionario(FuncionarioDependenteVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getFuncionarioVO().getCodigo().intValue() == 0) {
            obj.setFuncionarioVO(new FuncionarioVO());
            return;
        }
        obj.setFuncionarioVO(getFacadeFactory().getFuncionarioFacade().consultarPorChavePrimariaUnica(obj.getFuncionarioVO().getCodigo(), false, nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>FuncionarioCargoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>FuncionarioCargo</code>.
     * @param <code>funcionario</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirFuncionarioDependente(Integer funcionario) throws Exception {
        String sql = "DELETE FROM FuncionarioDependente WHERE (funcionario = ?)";
        getConexao().getJdbcTemplate().update(sql, funcionario);
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>FuncionarioCargoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirFuncionarioCargos</code> e <code>incluirFuncionarioCargos</code> disponíveis na classe <code>FuncionarioCargo</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarFuncionarioDependentes(Integer funcionario, List objetos) throws Exception {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM FuncionarioDependente WHERE funcionario = ?");
        Iterator i = objetos.iterator();
        while (i.hasNext()) {
        	FuncionarioDependenteVO objeto = (FuncionarioDependenteVO)i.next();
            sql.append(" AND codigo <> ").append(objeto.getCodigo());
        }
        getConexao().getJdbcTemplate().update(sql.toString(), funcionario);
        Iterator e = objetos.iterator();
        while (e.hasNext()) {            
        	FuncionarioDependenteVO objeto = (FuncionarioDependenteVO) e.next();
            if(objeto.getFuncionarioVO().getCodigo().equals(0)){
                 objeto.getFuncionarioVO().setCodigo(funcionario);                
            }
            if (objeto.getCodigo().equals(0)) {
                incluir(objeto);
            } else {
                alterar(objeto);
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
    public void incluirFuncionarioDependentes(Integer funcionarioPrm, List objetos) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
        	FuncionarioDependenteVO obj = (FuncionarioDependenteVO) e.next();
            obj.getFuncionarioVO().setCodigo(funcionarioPrm);
            incluir(obj);
        }
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>FuncionarioCargoVO</code>
     * através de sua chave primária.
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public FuncionarioDependenteVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM FuncionarioDependente WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm.intValue()});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public FuncionarioDependenteVO consultarPorCodigo(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM FuncionarioDependente WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, codigoPrm.intValue());
        if (!tabelaResultado.next()) {
            return null;
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }
    
    /**
     * Consulta a quantidade de pendentes no IRRF cadastrados para o funcionario
     * 
     * @param codigoFuncionario
     * @return
     * @throws Exception
     */
    public Integer consultarQuantidadeDependentesDoFuncinarioNoIRRF(Integer codigoFuncionario){
        StringBuilder sql = new StringBuilder(" select count(codigo) as qtdDependente from funcionariodependente where funcionario = ? and irrf = true ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoFuncionario);
        if (!tabelaResultado.next()) {
            return 0;
        }
        return tabelaResultado.getInt("qtdDependente");
    }
    
    /**
     * Consulta a quantidade de pendentes para o Salario Familia cadastrados para o funcionario
     * 
     * @param codigoFuncionario
     * @return
     * @throws Exception
     */
    public Integer consultarQuantidadeDependentesDoFuncinarioNoSalarioFamilia(Integer codigoFuncionario){
        StringBuilder sql = new StringBuilder(" select count(codigo) as qtdDependente from funcionariodependente where funcionario = ? and salariofamilia = true ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigoFuncionario);
        if (!tabelaResultado.next()) {
            return 0;
        }
        return tabelaResultado.getInt("qtdDependente");
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        FuncionarioDependente.idEntidade = idEntidade;
    }
    
    /**
     * Valida se dependente tem idade superior a um ano.
     * 
     * @param funcionarioDependenteVO
     * @return
     */
    @Override
    public void validarDadosDependente(FuncionarioDependenteVO funcionarioDependenteVO) {
    	if (Uteis.isAtributoPreenchido(funcionarioDependenteVO.getDataNascimento())) {

    		long idade = UteisData.calcularIdade(funcionarioDependenteVO.getDataNascimento());
	
    		validarSalarioFamilia(idade, funcionarioDependenteVO);
	    	validarFrequenciaEscolar(idade, funcionarioDependenteVO);
			validaIdadeDependente(idade, funcionarioDependenteVO);
			validarIdadeDependendeMaiorVinteUm(idade, funcionarioDependenteVO);
			validarIdadeDependendeMaiorVinteCincoUniversitario(idade, funcionarioDependenteVO);
    	}    	
    }

    /**
     * Regra de Validação de diretiro de salario familia.
     * 
     * @param idade
     * @param funcionarioDependenteVO
     */
	private void validarSalarioFamilia(long idade, FuncionarioDependenteVO funcionarioDependenteVO) {
		if (funcionarioDependenteVO.getParentesco().equals(GrauParentescoEnum.FILHO_INVALIDO.name())) {
			funcionarioDependenteVO.setSalarioFamilia(Boolean.TRUE);
		}

		if (funcionarioDependenteVO.getParentesco().equals(GrauParentescoEnum.FILHO_VALIDO.name()) && idade < 1) {
			funcionarioDependenteVO.setSalarioFamilia(Boolean.TRUE);
		}

		if ( (funcionarioDependenteVO.getParentesco().equals(GrauParentescoEnum.FILHO_VALIDO.name()) 
				|| funcionarioDependenteVO.getParentesco().equals(GrauParentescoEnum.ENTEADO.name())) 
				&& idade < 7 && funcionarioDependenteVO.getCartaoVacina()) {
			funcionarioDependenteVO.setSalarioFamilia(Boolean.TRUE);
		}

		if ( (funcionarioDependenteVO.getParentesco().equals(GrauParentescoEnum.FILHO_VALIDO.name()) 
				|| funcionarioDependenteVO.getParentesco().equals(GrauParentescoEnum.ENTEADO.name()))  
				&& (idade > 7 && idade <= 14 )  
				&& funcionarioDependenteVO.getFrequenciaEscolar()) {
			funcionarioDependenteVO.setSalarioFamilia(Boolean.TRUE);
		}
	}

	private void validarFrequenciaEscolar(long idade, FuncionarioDependenteVO funcionarioDependenteVO) {
		if (idade >= 7) {
			funcionarioDependenteVO.setFrequenciaEscolar(true);
		} else {
			funcionarioDependenteVO.setFrequenciaEscolar(false);			
		}
	}

	public void validaIdadeDependente(long idade, FuncionarioDependenteVO funcionarioDependenteVO) {
		if (idade >= 1) {
			funcionarioDependenteVO.setCartaoVacina(true);
		} else {
			funcionarioDependenteVO.setCartaoVacina(false);			
		}
	}

	public void validarIdadeDependendeMaiorVinteUm(long idade, FuncionarioDependenteVO funcionarioDependenteVO) {
		if (idade >= 21) {
			funcionarioDependenteVO.setInss(false);
		}
	}

	public void validarIdadeDependendeMaiorVinteCincoUniversitario(long idade, FuncionarioDependenteVO funcionarioDependenteVO) {
		if (idade >= 25 || funcionarioDependenteVO.getUniversitarioEscolaTecnica()) {
			funcionarioDependenteVO.setInss(false);
		}
	}
	
	@Override
	public void adicionarEventosDePensaoDoFuncionario(List<EventoFolhaPagamentoVO> listaDeEventosDoFuncionario, ContraChequeVO contraChequeVO, FuncionarioCargoVO funcionarioCargo, UsuarioVO usuario) {
		
		StringBuilder sql = new StringBuilder();
		sql.append(" select distinct on (fd.codigo) e.codigo as eventofolhapagamento, cce.codigo as contraChequeEvento, fd.* from FuncionarioDependente fd ");
		sql.append(" inner join eventofolhapagamento e on e.codigo = fd.eventoFolhaPagamento and situacao like 'ATIVO' ");
		sql.append(" left join contrachequeevento cce on cce.eventoFolhaPagamento = e.codigo and cce.contracheque = ? ");
		sql.append(" where fd.funcionario = ? and pensao = true ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), contraChequeVO.getCodigo(), funcionarioCargo.getFuncionarioVO().getCodigo());

		while(tabelaResultado.next()) {
			try {
				EventoFolhaPagamentoVO obj = getFacadeFactory().getEventoFolhaPagamentoInterfaceFacade().montarDadosDoEventoParaContraCheque(tabelaResultado.getInt("eventofolhapagamento"), tabelaResultado.getInt("contraChequeEvento"));
				if((Uteis.isAtributoPreenchido(obj) &&  (!listaDeEventosDoFuncionario.contains(obj)) || obj.getPermiteDuplicarContraCheque())) {
					tratarEventosDePensaoCadastradosNoFuncionario(tabelaResultado, obj, funcionarioCargo, usuario);
					listaDeEventosDoFuncionario.add(obj);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Calcula o valor da pensao caso tenha formula cadastrada na pensao, seta o valor da formula no evento
	 * 
	 * @param tabelaResultado
	 * @param obj
	 */
	private void tratarEventosDePensaoCadastradosNoFuncionario(SqlRowSet tabelaResultado, EventoFolhaPagamentoVO obj, FuncionarioCargoVO funcionarioCargo, UsuarioVO usuario) throws Exception {
		
		FuncionarioDependenteVO dependentePensao = montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSCONSULTA, null);
		Object resultadoFormula;
		BigDecimal valorPensao = BigDecimal.ZERO;
		
		try {
			ScriptEngine engine = getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().inicializaEngineFormula();

			resultadoFormula = getFacadeFactory().getFormulaFolhaPagamentoInterfaceFacade().executarFormula(dependentePensao.getFormulaCalculo(), funcionarioCargo, usuario, engine);
			
			if(Uteis.isAtributoPreenchido(resultadoFormula)) {
				valorPensao = new BigDecimal(resultadoFormula.toString());
				//Percentual preechido
				if(dependentePensao.getPercentual().compareTo(BigDecimal.ZERO) > 0) {
					valorPensao = valorPensao.multiply(dependentePensao.getPercentual()).divide(new BigDecimal(100));	
				}
				obj.setValorTemporario(valorPensao);
				obj.setReferencia(dependentePensao.getPercentual().toString());
				Boolean informadoManual = obj.getValorTemporario().compareTo(BigDecimal.ZERO) > 0 || Uteis.isAtributoPreenchido(obj.getReferencia());
				obj.setValorInformado(informadoManual);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<FuncionarioDependenteVO> consultarFuncionariosDependentes() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append(getSqlBasico());
		List<FuncionarioDependenteVO> lista = new ArrayList<>();
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        while (tabelaResultado.next()) {
        	lista.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
        }
		return lista;
	}

	private StringBuilder getSqlBasico() {
		StringBuilder sql = new StringBuilder();
		sql.append(" SELECT * FROM public.funcionariodependente");
		sql.append(" LEFT JOIN funcionario on funcionario.codigo = funcionariodependente.funcionario");
		sql.append(" LEFT JOIN pessoa on pessoa.codigo = funcionario.pessoa;");
		return sql;
	}
	
	public void alterarFuncionariosDependentes(FuncionarioDependenteVO obj) {
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {

            	StringBuilder sql = new StringBuilder("UPDATE FuncionarioDependente set cartaovacina=?, frequenciaescolar=?, inss=? WHERE codigo = ? ");

            	PreparedStatement sqlAlterar = con.prepareStatement(sql.toString());
            	int i = 0;
            	Uteis.setValuePreparedStatement(obj.getCartaoVacina(), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(obj.getFrequenciaEscolar(), ++i, sqlAlterar);
            	Uteis.setValuePreparedStatement(obj.getInss(), ++i, sqlAlterar);

                Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);

                return sqlAlterar;
            }
        });
    }

}