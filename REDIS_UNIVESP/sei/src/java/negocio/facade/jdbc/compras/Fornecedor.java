package negocio.facade.jdbc.compras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.EmpresaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.financeiro.BancoVO;
import negocio.comuns.financeiro.enumerador.TipoIdentificacaoChavePixEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.FornecedorInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>FornecedorVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>FornecedorVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see FornecedorVO
 * @see ControleAcesso
 * @see EmpresaVO
 */
@Repository
@Scope("singleton")
@Lazy
public class Fornecedor extends ControleAcesso implements FornecedorInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6386776870592118191L;
	protected static String idEntidade;

    public Fornecedor() throws Exception {
        super();
        setIdEntidade("Fornecedor");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>FornecedorVO</code>.
     */
    public FornecedorVO novo() throws Exception {
        Fornecedor.incluir(getIdEntidade());
        FornecedorVO obj = new FornecedorVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>FornecedorVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>FornecedorVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    
    public void incluir(final FornecedorVO obj, UsuarioVO usuario) throws Exception {
        try {
            FornecedorVO.validarDados(obj);
            validarUnicidadeRegistroFornecedorVO(obj, usuario);
            Fornecedor.incluir(getIdEntidade(), true, usuario);
            final String sql = "INSERT INTO Fornecedor( nome, razaoSocial, endereco, setor, numero, complemento, cidade, CEP, tipoEmpresa, CNPJ, inscEstadual, RG, CPF, telComercial1, telComercial2, telComercial3, email, site, fax, nomeBanco, numeroBancoRecebimento, numeroAgenciaRecebimento, contaCorrenteRecebimento, contato, situacao,isentarTaxaBoleto, permiteenviarremessa, digitoAgenciaRecebimento, digitoCorrenteRecebimento ,isTemMei ,nomePessoaFisica ,cpfFornecedorMei, banco, observacao ,chaveEnderecamentoPix , tipoIdentificacaoChavePixEnum  ) VALUES (?,?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,? ,? ,?  ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setString(1, obj.getNome());
                    sqlInserir.setString(2, obj.getRazaoSocial());
                    sqlInserir.setString(3, obj.getEndereco());
                    sqlInserir.setString(4, obj.getSetor());
                    sqlInserir.setString(5, obj.getNumero());
                    sqlInserir.setString(6, obj.getComplemento());
                    sqlInserir.setInt(7, obj.getCidade().getCodigo().intValue());
                    sqlInserir.setString(8, obj.getCEP());
                    sqlInserir.setString(9, obj.getTipoEmpresa());
                    sqlInserir.setString(10, obj.getCNPJ());
                    sqlInserir.setString(11, obj.getInscEstadual());
                    sqlInserir.setString(12, obj.getRG());
                    sqlInserir.setString(13, obj.getCPF());
                    sqlInserir.setString(14, obj.getTelComercial1());
                    sqlInserir.setString(15, obj.getTelComercial2());
                    sqlInserir.setString(16, obj.getTelComercial3());
                    sqlInserir.setString(17, obj.getEmail());
                    sqlInserir.setString(18, obj.getSite());
                    sqlInserir.setString(19, obj.getFax());
                    sqlInserir.setString(20, obj.getNomeBanco());
                    sqlInserir.setString(21, obj.getNumeroBancoRecebimento());
                    sqlInserir.setString(22, obj.getNumeroAgenciaRecebimento());
                    sqlInserir.setString(23, obj.getContaCorrenteRecebimento());
                    sqlInserir.setString(24, obj.getContato());
                    sqlInserir.setString(25, obj.getSituacao());
                    sqlInserir.setBoolean(26, obj.getIsentarTaxaBoleto());
                    sqlInserir.setBoolean(27, obj.getPermiteenviarremessa());    
                    sqlInserir.setString(28, obj.getDigitoAgenciaRecebimento());
                    sqlInserir.setString(29, obj.getDigitoCorrenteRecebimento());
                    sqlInserir.setBoolean(30,obj.getIsTemMei());                    
                    sqlInserir.setString(31, obj.getNomePessoaFisica());
                    sqlInserir.setString(32, obj.getCpfFornecedor());
                    int i = 33;
                    Uteis.setValuePreparedStatement(obj.getBancoVO(), i++, sqlInserir);
                    sqlInserir.setString(i++, obj.getObservacao());
                    sqlInserir.setString(i++, obj.getChaveEnderecamentoPix());
                    if (Uteis.isAtributoPreenchido(obj.getTipoIdentificacaoChavePixEnum())) {
						sqlInserir.setString(i++, obj.getTipoIdentificacaoChavePixEnum().name());
					} else {
						sqlInserir.setNull(i++, 0);
					}
                   
                    
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

            getFacadeFactory().getFornecedorCategoriaProdutoFacade().incluirFornecedorCategoriaProdutos(obj.getCodigo(), obj.getFornecedorCategoriaProdutoVOs());
            obj.setNovoObj(Boolean.FALSE);
            if (obj.getCEP() != null && !obj.getCEP().equals("")) {
                getFacadeFactory().getEnderecoFacade().incluirNovoCep(obj, usuario);
            }
        } catch (Exception e) {
            obj.setNovoObj(Boolean.TRUE);
            obj.setCodigo(0);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>FornecedorVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>FornecedorVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    
    public void alterar(final FornecedorVO obj, UsuarioVO usuario) throws Exception {
        try {
            FornecedorVO.validarDados(obj);
            validarUnicidadeRegistroFornecedorVO(obj, usuario);
            Fornecedor.alterar(getIdEntidade(), true, usuario);
            final String sql = "UPDATE Fornecedor set nome=?, razaoSocial=?, endereco=?, setor=?, numero=?, complemento=?, cidade=?, CEP=?, tipoEmpresa=?, CNPJ=?, inscEstadual=?, RG=?, CPF=?, telComercial1=?, telComercial2=?, telComercial3=?, email=?, site=?, fax=?, nomeBanco=?, numeroBancoRecebimento=?, numeroAgenciaRecebimento=?, contaCorrenteRecebimento=?, contato=?, situacao=?, isentarTaxaBoleto=?, permiteenviarremessa=?, digitoAgenciaRecebimento=?, digitoCorrenteRecebimento=? ,isTemMei=? ,nomePessoaFisica=? ,cpfFornecedorMei=?, banco=?, observacao=?  , chaveEnderecamentoPix=? , tipoIdentificacaoChavePixEnum=?  WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setString(1, obj.getNome());
                    sqlAlterar.setString(2, obj.getRazaoSocial());
                    sqlAlterar.setString(3, obj.getEndereco());
                    sqlAlterar.setString(4, obj.getSetor());
                    sqlAlterar.setString(5, obj.getNumero());
                    sqlAlterar.setString(6, obj.getComplemento());
                    sqlAlterar.setInt(7, obj.getCidade().getCodigo().intValue());
                    sqlAlterar.setString(8, obj.getCEP());
                    sqlAlterar.setString(9, obj.getTipoEmpresa());
                    sqlAlterar.setString(10, obj.getCNPJ());
                    sqlAlterar.setString(11, obj.getInscEstadual());
                    sqlAlterar.setString(12, obj.getRG());
                    sqlAlterar.setString(13, obj.getCPF());
                    sqlAlterar.setString(14, obj.getTelComercial1());
                    sqlAlterar.setString(15, obj.getTelComercial2());
                    sqlAlterar.setString(16, obj.getTelComercial3());
                    sqlAlterar.setString(17, obj.getEmail());
                    sqlAlterar.setString(18, obj.getSite());
                    sqlAlterar.setString(19, obj.getFax());
                    sqlAlterar.setString(20, obj.getNomeBanco());
                    sqlAlterar.setString(21, obj.getNumeroBancoRecebimento());
                    sqlAlterar.setString(22, obj.getNumeroAgenciaRecebimento());
                    sqlAlterar.setString(23, obj.getContaCorrenteRecebimento());
                    sqlAlterar.setString(24, obj.getContato());
                    sqlAlterar.setString(25, obj.getSituacao());
                    sqlAlterar.setBoolean(26, obj.getIsentarTaxaBoleto());
                    sqlAlterar.setBoolean(27, obj.getPermiteenviarremessa());
                    sqlAlterar.setString(28, obj.getDigitoAgenciaRecebimento());
                    sqlAlterar.setString(29, obj.getDigitoCorrenteRecebimento());
                    sqlAlterar.setBoolean(30,obj.getIsTemMei());   
                    sqlAlterar.setString(31, obj.getNomePessoaFisica());
                    sqlAlterar.setString(32, obj.getCpfFornecedor());
                    int i = 33;
                    Uteis.setValuePreparedStatement(obj.getBancoVO(), i++, sqlAlterar);
                    sqlAlterar.setString(i++, obj.getObservacao());
                    sqlAlterar.setString(i++, obj.getChaveEnderecamentoPix());
                    if (Uteis.isAtributoPreenchido(obj.getTipoIdentificacaoChavePixEnum())) {
                    	sqlAlterar.setString(i++, obj.getTipoIdentificacaoChavePixEnum().name());
					} else {
						sqlAlterar.setNull(i++, 0);
					}
                    Uteis.setValuePreparedStatement(obj.getCodigo(), i++, sqlAlterar);
                   
                    return sqlAlterar;
                }
            });

            getFacadeFactory().getFornecedorCategoriaProdutoFacade().alterarFornecedorCategoriaProdutos(obj.getCodigo(), obj.getFornecedorCategoriaProdutoVOs());
            getFacadeFactory().getEnderecoFacade().incluirNovoCep(obj, usuario);
        } catch (Exception e) {
            throw e;
        }
    }
    
    @Override
    public void alterarSituacao(final FornecedorVO obj, UsuarioVO usuario) throws Exception {
    	Fornecedor.alterar(getIdEntidade());
        final String sql = "UPDATE Fornecedor set situacao=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setString(1, obj.getSituacao());
                sqlAlterar.setInt(2, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>FornecedorVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>FornecedorVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    
    public void excluir(FornecedorVO obj, UsuarioVO usuarioVO) throws Exception {
        try {
            Fornecedor.excluir(getIdEntidade(), true, usuarioVO);
            String sql = "DELETE FROM Fornecedor WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
            getFacadeFactory().getFornecedorCategoriaProdutoFacade().excluirFornecedorCategoriaProdutos(obj.getCodigo());
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Fornecedor</code> através do valor do atributo 
     * <code>String CPF</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FornecedorVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    
    public List<FornecedorVO> consultarPorCPF(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM Fornecedor WHERE (CPF like(?) OR cpffornecedormei like (?)) ");
        if (!situacao.equals("")) {
            sqlStr.append(" and situacao = '").append(situacao).append("' ");
        }
        sqlStr.append(" ORDER BY CPF , cpffornecedormei");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT, valorConsulta + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public FornecedorVO consultarPorCPFUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Fornecedor WHERE CPF like('" + valorConsulta + "%') AND (cpf IS NOT NULL OR cpf <> '') ORDER BY codigo LIMIT 1";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            return new FornecedorVO();
        }
        return montarDados(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>Fornecedor</code> através do valor do atributo 
     * <code>String RG</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FornecedorVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    
    public List<FornecedorVO> consultarPorRG(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM Fornecedor WHERE RG like(?) ");
        if (!situacao.equals("")) {
            sqlStr.append(" and situacao = '").append(situacao).append("' ");
        }
        sqlStr.append("ORDER BY RG");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Fornecedor</code> através do valor do atributo 
     * <code>String inscEstadual</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FornecedorVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    
    public List<FornecedorVO> consultarPorInscEstadual(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM Fornecedor WHERE inscEstadual like(?) ");
        if (!situacao.equals("")) {
            sqlStr.append(" and situacao = '").append(situacao).append("' ");
        }
        sqlStr.append("ORDER BY inscEstadual");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Fornecedor</code> através do valor do atributo 
     * <code>String CNPJ</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FornecedorVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    
    public List<FornecedorVO> consultarPorCNPJ(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM Fornecedor WHERE CNPJ like(?) ");
        if (!situacao.equals("")) {
            sqlStr.append(" and situacao = '").append(situacao).append("' ");
        }
        sqlStr.append("ORDER BY CNPJ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public FornecedorVO consultarPorCNPJUnico(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Fornecedor WHERE CNPJ like('" + valorConsulta + "%') ORDER BY codigo LIMIT 1";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            return new FornecedorVO();
        }
        return montarDados(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>Fornecedor</code> através do valor do atributo 
     * <code>nome</code> da classe <code>Cidade</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>FornecedorVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    
    public List<FornecedorVO> consultarPorNomeCidade(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT Fornecedor.* FROM Fornecedor, Cidade WHERE Fornecedor.cidade = Cidade.codigo and Cidade.nome like(?) ");
        if (!situacao.equals("")) {
            sqlStr.append(" and situacao = '").append(situacao).append("' ");
        }
        sqlStr.append("ORDER BY Cidade.nome");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>Fornecedor</code> através do valor do atributo 
     * <code>String razaoSocial</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FornecedorVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    
    public List<FornecedorVO> consultarPorRazaoSocial(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder("SELECT * FROM Fornecedor WHERE sem_acentos(upper(razaoSocial)) like(sem_acentos(upper(?))) ");
        if (!situacao.equals("")) {
            sqlStr.append(" and situacao = '").append(situacao).append("' ");
        }
        sqlStr.append("ORDER BY razaoSocial");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), valorConsulta + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Fornecedor</code> através do valor do atributo 
     * <code>String nome</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FornecedorVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    
    public List<FornecedorVO> consultarPorNome(String valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuilder sqlStr = new StringBuilder(" SELECT * FROM Fornecedor WHERE (sem_acentos(nome) ilike sem_acentos(?) ");
        sqlStr.append(" or sem_acentos(nomepessoafisica) ilike sem_acentos(?)) ");
        if (!situacao.equals("")) {
        	sqlStr.append(" and situacao = '").append(situacao).append("' ");
        }
        sqlStr.append("ORDER BY nome , nomepessoafisica ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), PERCENT + valorConsulta + PERCENT, PERCENT + valorConsulta + PERCENT);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Fornecedor</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>FornecedorVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    
    public List<FornecedorVO> consultarPorCodigo(Integer valorConsulta, String situacao, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Fornecedor WHERE codigo >= " + valorConsulta.intValue() + " ";
        if (!situacao.equals("")) {
            sqlStr += " and situacao = '" + situacao + "' ";
        }
        sqlStr += "ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    
    public List<FornecedorVO> consultarPorCategoriaProduto(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "select distinct(Fornecedor.*) from Fornecedor inner join FornecedorCategoriaProduto on FornecedorCategoriaProduto.fornecedor = Fornecedor.codigo"
                + " where FornecedorCategoriaProduto.categoriaProduto = " + valorConsulta.intValue() + " and fornecedor.situacao = 'AT' order by Fornecedor.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>FornecedorVO</code> resultantes da consulta.
     */
    public static List<FornecedorVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<FornecedorVO> vetResultado = new ArrayList<FornecedorVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>FornecedorVO</code>.
     * @return  O objeto da classe <code>FornecedorVO</code> com os dados devidamente montados.
     */
    public static FornecedorVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        FornecedorVO obj = new FornecedorVO();
        obj.setNovoObj(Boolean.FALSE);
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setNome(dadosSQL.getString("nome"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_COMBOBOX) {
            return obj;
        }
        obj.setRazaoSocial(dadosSQL.getString("razaoSocial"));
        obj.setCNPJ(dadosSQL.getString("CNPJ"));
        obj.setCPF(dadosSQL.getString("CPF"));
        obj.setContato(dadosSQL.getString("contato"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setTipoEmpresa(dadosSQL.getString("tipoEmpresa"));
        obj.setIsTemMei(dadosSQL.getBoolean("isTemMei"));
        obj.setNomePessoaFisica(dadosSQL.getString("nomePessoaFisica"));
        obj.setCpfFornecedor(dadosSQL.getString("cpfFornecedorMei"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
            return obj;
        }
        obj.getCidade().setCodigo(new Integer(dadosSQL.getInt("cidade")));
        
        montarDadosCidade(obj, usuario);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSMINIMOS) {
            return obj;
        }
        obj.setEndereco(dadosSQL.getString("endereco"));
        obj.setSetor(dadosSQL.getString("setor"));
        obj.setNumero(dadosSQL.getString("numero"));
        obj.setComplemento(dadosSQL.getString("complemento"));
        obj.setCEP(dadosSQL.getString("CEP"));        
        obj.setInscEstadual(dadosSQL.getString("inscEstadual"));
        obj.setRG(dadosSQL.getString("RG"));
        obj.setTelComercial1(dadosSQL.getString("telComercial1"));
        obj.setTelComercial2(dadosSQL.getString("telComercial2"));
        obj.setTelComercial3(dadosSQL.getString("telComercial3"));
        obj.setEmail(dadosSQL.getString("email"));
        obj.setSite(dadosSQL.getString("site"));
        obj.setFax(dadosSQL.getString("fax"));
        obj.setNomeBanco(dadosSQL.getString("nomeBanco"));
        obj.setNumeroBancoRecebimento(dadosSQL.getString("numeroBancoRecebimento"));
        obj.setNumeroAgenciaRecebimento(dadosSQL.getString("numeroAgenciaRecebimento"));
        obj.setContaCorrenteRecebimento(dadosSQL.getString("contaCorrenteRecebimento"));
        obj.setDigitoAgenciaRecebimento(dadosSQL.getString("digitoAgenciaRecebimento"));
        obj.setDigitoCorrenteRecebimento(dadosSQL.getString("digitoCorrenteRecebimento"));
        if (Uteis.isAtributoPreenchido(dadosSQL.getString("tipoIdentificacaoChavePixEnum"))) {
			obj.setTipoIdentificacaoChavePixEnum(TipoIdentificacaoChavePixEnum.valueOf(dadosSQL.getString("tipoIdentificacaoChavePixEnum")));
		}		
		obj.setChaveEnderecamentoPix(dadosSQL.getString("chaveEnderecamentoPix") );
        obj.setIsentarTaxaBoleto(dadosSQL.getBoolean("isentarTaxaBoleto"));
        obj.setPermiteenviarremessa(dadosSQL.getBoolean("permiteenviarremessa"));
        obj.setBancoVO(Uteis.montarDadosVO(dadosSQL.getInt("banco"), BancoVO.class, p -> getFacadeFactory().getBancoFacade().consultarPorChavePrimaria(p,false, Uteis.NIVELMONTARDADOS_TODOS, usuario)));
        obj.setObservacao(dadosSQL.getString("observacao"));
        
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setFornecedorCategoriaProdutoVOs(FornecedorCategoriaProduto.consultarFornecedorCategoriaProdutos(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>CidadeVO</code> relacionado ao objeto <code>FornecedorVO</code>.
     * Faz uso da chave primária da classe <code>CidadeVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public static void montarDadosCidade(FornecedorVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getCidade().getCodigo().intValue() == 0) {
            obj.setCidade(new CidadeVO());
            return;
        }
        obj.setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(obj.getCidade().getCodigo(), false, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>FornecedorVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    
    public FornecedorVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM Fornecedor WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados (Fornecedor).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Fornecedor.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        Fornecedor.idEntidade = idEntidade;
    }
    
	private void validarUnicidadeRegistroFornecedorVO(FornecedorVO fornecedorVO, UsuarioVO usuarioVO) throws Exception {
		String valorConsulta = "";
		if (fornecedorVO.getTipoEmpresa().equals("JU")) {
			valorConsulta = Uteis.retirarMascaraCNPJ(fornecedorVO.getCNPJ());
			String sqlStr = "SELECT codigo FROM Fornecedor WHERE regexp_replace(CNPJ , '[^0-9]*', '', 'g') = ? ";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta);
			while (tabelaResultado.next()) {
				Integer codigo = tabelaResultado.getInt("codigo");
				if (!codigo.equals(fornecedorVO.getCodigo())) {
					throw new ConsistirException("Já existe um registro com esse CNPJ (Fornecedor).");
				}
			}
		} else if (fornecedorVO.getTipoEmpresa().equals("FI")) {
			valorConsulta = Uteis.retirarMascaraCPF(fornecedorVO.getCPF());
			String sqlStr = "SELECT codigo FROM Fornecedor WHERE regexp_replace(CPF , '[^0-9]*', '', 'g') = ? ";
			SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta);
			while (tabelaResultado.next()) {
				Integer codigo = tabelaResultado.getInt("codigo");
				if (!codigo.equals(fornecedorVO.getCodigo())) {
					throw new ConsistirException("Já existe um registro com esse CPF (Fornecedor).");
				}
			}
		}
	}
	
	@Override
    public FornecedorVO consultarPorCnpjOuCpf(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Fornecedor WHERE CNPJ like('" + valorConsulta + "%') or CPF like('" + valorConsulta + "%')  ORDER BY codigo LIMIT 1";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            return new FornecedorVO();
        }
        return montarDados(tabelaResultado, nivelMontarDados, usuario);
    }
}
