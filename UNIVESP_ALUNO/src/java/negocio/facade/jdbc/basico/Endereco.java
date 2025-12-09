package negocio.facade.jdbc.basico;

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

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;
import negocio.comuns.academico.EstagioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.BairroVO;
import negocio.comuns.basico.CidadeVO;
import negocio.comuns.basico.DadosComerciaisVO;
import negocio.comuns.basico.EnderecoVO;
import negocio.comuns.basico.EstadoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.basico.PossuiEndereco;
import negocio.comuns.estagio.ConcedenteVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Constantes;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.basico.EnderecoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>EnderecoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>EnderecoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see EnderecoVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class Endereco extends ControleAcesso implements EnderecoInterfaceFacade {

    protected static String idEntidade;

    public Endereco() throws Exception {
        super();
        setIdEntidade("Endereco");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>EnderecoVO</code>.
     */
    public EnderecoVO novo() throws Exception {
        Endereco.incluir(getIdEntidade());
        EnderecoVO obj = new EnderecoVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>EnderecoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>EnderecoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final EnderecoVO obj) throws Exception {
        try {
            EnderecoVO.validarDados(obj);
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO Endereco( bairrocodigo, cep, logradouro, completo ) VALUES ( ?, ?, ?, ? ) returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getBairrocodigo().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getBairrocodigo().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    sqlInserir.setString(2, obj.getCep());
                    sqlInserir.setString(3, obj.getLogradouro());
                    sqlInserir.setString(4, obj.getCompleto());

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
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>EnderecoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>EnderecoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final EnderecoVO obj) throws Exception {
        try {
            EnderecoVO.validarDados(obj);
            Endereco.alterar(getIdEntidade());
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE Endereco set bairrocodigo=?, cep=?, logradouro=?, completo=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    if (obj.getBairrocodigo().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(1, obj.getBairrocodigo().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(1, 0);
                    }
                    sqlAlterar.setString(2, obj.getCep());
                    sqlAlterar.setString(3, obj.getLogradouro());
                    sqlAlterar.setString(4, obj.getCompleto());
                    sqlAlterar.setInt(5, obj.getCodigo().intValue());
                    return sqlAlterar;
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>EnderecoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>EnderecoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(EnderecoVO obj) throws Exception {
        try {
            Endereco.excluir(getIdEntidade());
            String sql = "DELETE FROM Endereco WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por realizar uma consulta de <code>Endereco</code> através do valor do atributo 
     * <code>String logradouro</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>EnderecoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorLogradouro(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Endereco WHERE upper( logradouro ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY logradouro";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>Endereco</code> através do valor do atributo 
     * <code>String cep</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>EnderecoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    public List consultarPorCep(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        valorConsulta = valorConsulta.replace(".", "");
        valorConsulta = valorConsulta.replace("-", "");
        valorConsulta = valorConsulta.replace("/", "");
        valorConsulta = valorConsulta.replace(" ", "");        
        consultar(getIdEntidade(), controlarAcesso, usuario);
//        String sqlStr = "SELECT * FROM Endereco WHERE upper( cep ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY cep";
        String sqlStr = "SELECT * FROM Endereco WHERE cep = '" + valorConsulta.toUpperCase() + "'";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        List<EnderecoVO>  enderecoVOs = (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
        if(enderecoVOs.isEmpty()) {    		
    		HttpResponse<JsonNode> response = null;
        	try {        		
        		response = Unirest.get("https://opencep.com/v1/"+valorConsulta.trim()).asJson();
        	}catch (Exception e) {
        		e.printStackTrace();
        		return enderecoVOs;
			}
            if(!response.getBody().getObject().has("error")) {
            	JSONObject responseObj = response.getBody().getObject();            	
            	EnderecoVO enderecoVO = new EnderecoVO();
            	enderecoVO.setCep(responseObj.getString("cep").replace(".", "").replace("-", "").replace(" ", ""));
            	enderecoVO.setLogradouro(responseObj.getString("logradouro"));
            	if(enderecoVO.getLogradouro().isEmpty()) {
            		enderecoVO.setLogradouro("----");
            	}
            	enderecoVO.setCompleto(responseObj.getString("complemento"));            	
            	enderecoVO.getBairrocodigo().setDescricao(responseObj.getString("bairro"));
            	if(enderecoVO.getBairrocodigo().getDescricao().isEmpty()){
            		enderecoVO.getBairrocodigo().setDescricao("----");
            	}
            	enderecoVO.getBairrocodigo().getCidade().setNome(responseObj.getString("localidade"));
            	enderecoVO.getBairrocodigo().getCidade().getEstado().setSigla(responseObj.getString("uf"));
            	enderecoVO.getBairrocodigo().getCidade().setCodigoIBGE(responseObj.getString("ibge"));
            	CidadeVO cidadeVO = getFacadeFactory().getCidadeFacade().consultarPorNomeCidadeSiglaEstado(enderecoVO.getBairrocodigo().getCidade().getNome(), enderecoVO.getBairrocodigo().getCidade().getEstado().getSigla(), false, Uteis.NIVELMONTARDADOS_TODOS, usuario);
            	if(!Uteis.isAtributoPreenchido(cidadeVO)) {
            		EstadoVO estadoVO = getFacadeFactory().getEstadoFacade().consultarPorSigla(enderecoVO.getBairrocodigo().getCidade().getEstado().getSigla(), usuario);
            		if(!Uteis.isAtributoPreenchido(estadoVO)) {
            			return enderecoVOs;
            		}
            		enderecoVO.getBairrocodigo().getCidade().setEstado(estadoVO);
            		getFacadeFactory().getCidadeFacade().incluir(enderecoVO.getBairrocodigo().getCidade(), usuario);
            	}else {
            		enderecoVO.getBairrocodigo().setCidade(cidadeVO);
            	}
            	List<BairroVO> listaBairro = getFacadeFactory().getBairroFacade().consultarPorDescricao(enderecoVO.getBairrocodigo().getDescricao(), enderecoVO.getBairrocodigo().getCidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
            	if(listaBairro.isEmpty()) {
            		getFacadeFactory().getBairroFacade().incluir(enderecoVO.getBairrocodigo());
            	} else {
            		enderecoVO.getBairrocodigo().setCodigo(listaBairro.get(0).getCodigo());
            		enderecoVO.getBairrocodigo().setNovoObj(false);
            	}
            	incluir(enderecoVO);
            	enderecoVOs.add(enderecoVO);            
            }else {
            	if(getAplicacaoControle().getConfiguracaoGeralSistemaVO(null, usuario).getConfiguracaoAtualizacaoCadastralVO().getEnderecoObrigatorio()) {
            		throw new Exception("O CEP informado é inválido, informe um CEP válido.");
            	}
            }
        }
        return enderecoVOs;
        
    }

    public void carregarEndereco(PossuiEndereco vo, UsuarioVO usuario) throws Exception {
        if (vo == null || vo.getCEP() == null || vo.getCEP().equals("") || vo.getCEP().length() != 10) {
            vo.setEndereco("");
            vo.setSetor("");
            vo.setCidade(new CidadeVO());
            return;
        }
        String cep = vo.getCEP();
        cep = cep.replace(".", "");
        cep = cep.replace("-", "");
        List<EnderecoVO> listaEndereco = consultarPorCep(cep, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario);
        if (listaEndereco.size() == 0) {
            vo.setEndereco("");
            vo.setSetor("");
            vo.setCidade(new CidadeVO());
        } else {
            EnderecoVO enderecoVO = listaEndereco.get(0);
            vo.setEndereco(enderecoVO.getLogradouro());
            vo.setSetor(enderecoVO.getBairrocodigo().getDescricao());
            vo.setCidade(enderecoVO.getBairrocodigo().getCidade());
        }
    }

    public void carregarEnderecoEmpresa(DadosComerciaisVO dc, UsuarioVO usuarioLogado) throws Exception {
        if (dc == null || dc.getCepEmpresa() == null || dc.getCepEmpresa().equals("") || dc.getCepEmpresa().length() != 10) {
            dc.setEnderecoEmpresa("");
            dc.setSetorEmpresa("");
            dc.setCidadeEmpresa(new CidadeVO());
            return;
        }
        String cep = dc.getCepEmpresa();
        cep = cep.replace(".", "");
        cep = cep.replace("-", "");
        List<EnderecoVO> listaEndereco = consultarPorCep(cep, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuarioLogado);
        if (listaEndereco.size() == 0) {
            dc.setEnderecoEmpresa("");
            dc.setSetorEmpresa("");
            dc.setCidadeEmpresa(new CidadeVO());
        } else {
            EnderecoVO enderecoVO = listaEndereco.get(0);
            dc.setEnderecoEmpresa(enderecoVO.getLogradouro());
            dc.setSetorEmpresa(enderecoVO.getBairrocodigo().getDescricao());
            dc.setCidadeEmpresa(enderecoVO.getBairrocodigo().getCidade());
        }
    }
    @Override
    public void carregarEnderecoFiador(PessoaVO pessoa, UsuarioVO usuario) throws Exception {
        if (pessoa == null || pessoa.getCepFiador() == null || pessoa.getCepFiador().equals("") || pessoa.getCepFiador().length() != 10) {
            pessoa.setEnderecoFiador("");
            pessoa.setSetorFiador("");
            pessoa.setCidadeFiador(new CidadeVO());
            return;
        }
        String cep = pessoa.getCepFiador();
        cep = cep.replace(".", "");
        cep = cep.replace("-", "");
        List<EnderecoVO> listaEndereco = consultarPorCep(cep, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario);
        if (listaEndereco.size() == 0) {
        	pessoa.setEnderecoFiador("");
        	pessoa.setSetorFiador("");
        	pessoa.setComplementoFiador("");
        	pessoa.setCidadeFiador(new CidadeVO());
        } else {
            EnderecoVO enderecoVO = listaEndereco.get(0);
            pessoa.setEnderecoFiador(enderecoVO.getLogradouro());
            pessoa.setSetorFiador(enderecoVO.getBairrocodigo().getDescricao());
            pessoa.setCidadeFiador(enderecoVO.getBairrocodigo().getCidade());
            pessoa.setComplementoFiador(enderecoVO.getCompleto());
            
        }
    }
    
    public void carregarEnderecoEstagio(ConcedenteVO vo, UsuarioVO usuario) throws Exception {
        if (vo == null || vo.getCep() == null || vo.getCep().equals("") || vo.getCep().length() != 10) {
            vo.setEndereco("");
            vo.setBairro("");
            vo.setCidade("");
            return;
        }
        String cep = vo.getCep();
        cep = cep.replace(".", "");
        cep = cep.replace("-", "");
        List<EnderecoVO> listaEndereco = consultarPorCep(cep, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario);
        if (listaEndereco.size() == 0) {
            vo.setEndereco("");
            vo.setBairro("");
            vo.setCidade("");
        } else {
            EnderecoVO enderecoVO = listaEndereco.get(0);
            vo.setEndereco(enderecoVO.getLogradouro());
            vo.setBairro(enderecoVO.getBairrocodigo().getDescricao());
            vo.setCidade(enderecoVO.getBairrocodigo().getCidade().getNome());
        }
    }
    
    public void carregarEnderecoEstagioBeneficiario(EstagioVO vo, UsuarioVO usuario) throws Exception {
    	if (vo == null || vo.getCepBeneficiario() == null || vo.getCepBeneficiario().equals("") || vo.getCepBeneficiario().length() != 10) {
    		vo.setEnderecoBeneficiario("");
    		vo.setSetorBeneficiario("");
    		vo.setCidadeBeneficiario("");
    		vo.setEstadoBeneficiario("");
    		return;
    	}
    	String cep = vo.getCepBeneficiario();
    	cep = cep.replace(".", "");
    	cep = cep.replace("-", "");
    	List<EnderecoVO> listaEndereco = consultarPorCep(cep, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario);
    	if (listaEndereco.size() == 0) {
    		vo.setEnderecoBeneficiario("");
    		vo.setSetorBeneficiario("");
    		vo.setCidadeBeneficiario("");
    		vo.setEstadoBeneficiario("");
    	} else {
    		EnderecoVO enderecoVO = listaEndereco.get(0);
    		vo.setEnderecoBeneficiario(enderecoVO.getLogradouro());
    		vo.setSetorBeneficiario(enderecoVO.getBairrocodigo().getDescricao());
    		vo.setCidadeBeneficiario(enderecoVO.getBairrocodigo().getCidade().getNome());
    		vo.setEstadoBeneficiario(enderecoVO.getBairrocodigo().getCidade().getEstado().getSigla());
    	}
    }

    /**
     * Se necessário, inclui um novo CEP.
     * @param pessoa
     * @throws java.lang.Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirNovoCep(PossuiEndereco vo, UsuarioVO usuario) throws Exception {

        List<EnderecoVO> listaEndereco = consultarPorCep(vo.getCEP(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        if (listaEndereco.size() > 0) { // verifica se o cep já está cadastrado
            return;
        }

        BairroVO bairro = null;
        List<BairroVO> listaBairro = getFacadeFactory().getBairroFacade().consultarPorDescricao(vo.getSetor(), vo.getCidade(), false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario);
        if (vo.getCidade().getCodigo() == null || vo.getCidade().getCodigo().intValue() == 0) {
            return;
        }

        if (listaBairro.size() > 0) { // verifica se já existe um bairro com este nome
            bairro = listaBairro.get(0);
        } else {
            bairro = new BairroVO();
            bairro.setDescricao(vo.getSetor().toUpperCase());
            bairro.setCidade(vo.getCidade());
            getFacadeFactory().getBairroFacade().incluir(bairro);
        }

        EnderecoVO.validarCEP(vo.getCEP());
        String cep = vo.getCEP();
        cep = cep.replace(".", "");
        cep = cep.replace("-", "");

        EnderecoVO enderecoVO = new EnderecoVO();
        enderecoVO.setBairrocodigo(bairro);
        enderecoVO.setCep(cep);
        enderecoVO.setCompleto("");
        enderecoVO.setLogradouro(vo.getEndereco().toUpperCase());
        incluir(enderecoVO);
    }

    /**
     * Responsável por realizar uma consulta de <code>Endereco</code> através do valor do atributo 
     * <code>descricao</code> da classe <code>Bairro</code>
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @return  List Contendo vários objetos da classe <code>EnderecoVO</code> resultantes da consulta.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorDescricaoBairro(String valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT Endereco.* FROM Endereco, Bairro WHERE Endereco.bairrocodigo = Bairro.codigo and upper( Bairro.descricao ) like('" + valorConsulta.toUpperCase() + "%') ORDER BY Bairro.descricao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario);
    }

    /**
     * Responsável por realizar uma consulta de <code>Endereco</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>EnderecoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM Endereco WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>EnderecoVO</code> resultantes da consulta.
     */
    public  List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            EnderecoVO obj = new EnderecoVO();
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>EnderecoVO</code>.
     * @return  O objeto da classe <code>EnderecoVO</code> com os dados devidamente montados.
     */
    public  EnderecoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        EnderecoVO obj = new EnderecoVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getBairrocodigo().setCodigo(new Integer(dadosSQL.getInt("bairrocodigo")));
        obj.setCep(dadosSQL.getString("cep"));
        obj.setLogradouro(dadosSQL.getString("logradouro"));
        obj.setCompleto(dadosSQL.getString("completo"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        montarDadosBairrocodigo(obj, nivelMontarDados, usuario);
        return obj;
    }

    /**
     * Operação responsável por montar os dados de um objeto da classe <code>BairroVO</code> relacionado ao objeto <code>EnderecoVO</code>.
     * Faz uso da chave primária da classe <code>BairroVO</code> para realizar a consulta.
     * @param obj  Objeto no qual será montado os dados consultados.
     */
    public  void montarDadosBairrocodigo(EnderecoVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getBairrocodigo().getCodigo().intValue() == 0) {
            obj.setBairrocodigo(new BairroVO());
            return;
        }
        obj.setBairrocodigo(getFacadeFactory().getBairroFacade().consultarPorChavePrimaria(obj.getBairrocodigo().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>EnderecoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public EnderecoVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM Endereco WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( Endereco ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return Endereco.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        Endereco.idEntidade = idEntidade;
    }

    @Override
	@SuppressWarnings("unchecked")
	public void carregarEnderecoRegistradora(UnidadeEnsinoVO vo, Boolean mantenedoraRegistradora, Boolean mantenedora, UsuarioVO usuario) throws Exception {
		if (mantenedoraRegistradora) {
			if (vo == null || vo.getCepMantenedoraRegistradora() == null || vo.getCepMantenedoraRegistradora().equals(Constantes.EMPTY) || vo.getCepMantenedoraRegistradora().length() != 10) {
				vo.setEnderecoMantenedoraRegistradora(Constantes.EMPTY);
				vo.setBairroMantenedoraRegistradora(Constantes.EMPTY);
				vo.setCidadeMantenedoraRegistradora(new CidadeVO());
				return;
			}
			String cep = vo.getCepMantenedoraRegistradora();
			cep = cep.replace(".", Constantes.EMPTY);
			cep = cep.replace("-", Constantes.EMPTY);
			List<EnderecoVO> listaEndereco = consultarPorCep(cep, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario);
			if (listaEndereco.size() == 0) {
				vo.setEnderecoMantenedoraRegistradora(Constantes.EMPTY);
				vo.setBairroMantenedoraRegistradora(Constantes.EMPTY);
				vo.setCidadeMantenedoraRegistradora(new CidadeVO());
			} else {
				EnderecoVO enderecoVO = listaEndereco.get(0);
				vo.setEnderecoMantenedoraRegistradora(enderecoVO.getLogradouro());
				vo.setBairroMantenedoraRegistradora(enderecoVO.getBairrocodigo().getDescricao());
				vo.setCidadeMantenedoraRegistradora(enderecoVO.getBairrocodigo().getCidade());
			}
		} else if (mantenedora) {
			if (vo == null || vo.getCepMantenedora() == null || vo.getCepMantenedora().equals(Constantes.EMPTY) || vo.getCepMantenedora().length() != 10) {
				vo.setEnderecoMantenedora(Constantes.EMPTY);
				vo.setBairroMantenedora(Constantes.EMPTY);
				vo.setCidadeMantenedora(new CidadeVO());
				return;
			}
			String cep = vo.getCepMantenedora();
			cep = cep.replace(".", Constantes.EMPTY);
			cep = cep.replace("-", Constantes.EMPTY);
			List<EnderecoVO> listaEndereco = consultarPorCep(cep, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario);
			if (listaEndereco.size() == 0) {
				vo.setEnderecoMantenedora(Constantes.EMPTY);
				vo.setBairroMantenedora(Constantes.EMPTY);
				vo.setCidadeMantenedora(new CidadeVO());
			} else {
				EnderecoVO enderecoVO = listaEndereco.get(0);
				vo.setEnderecoMantenedora(enderecoVO.getLogradouro());
				vo.setBairroMantenedora(enderecoVO.getBairrocodigo().getDescricao());
				vo.setCidadeMantenedora(enderecoVO.getBairrocodigo().getCidade());
			}
		} else {
			if (vo == null || vo.getCepRegistradora() == null || vo.getCepRegistradora().equals(Constantes.EMPTY) || vo.getCepRegistradora().length() != 10) {
				vo.setEnderecoRegistradora(Constantes.EMPTY);
				vo.setBairroRegistradora(Constantes.EMPTY);
				vo.setCidadeRegistradora(new CidadeVO());
				return;
			}
			String cep = vo.getCepRegistradora();
			cep = cep.replace(".", Constantes.EMPTY);
			cep = cep.replace("-", Constantes.EMPTY);
			List<EnderecoVO> listaEndereco = consultarPorCep(cep, false, Uteis.NIVELMONTARDADOS_DADOSENTIDADESUBORDINADAS, usuario);
			if (listaEndereco.size() == 0) {
				vo.setEnderecoRegistradora(Constantes.EMPTY);
				vo.setBairroRegistradora(Constantes.EMPTY);
				vo.setCidadeRegistradora(new CidadeVO());
			} else {
				EnderecoVO enderecoVO = listaEndereco.get(0);
				vo.setEnderecoRegistradora(enderecoVO.getLogradouro());
				vo.setBairroRegistradora(enderecoVO.getBairrocodigo().getDescricao());
				vo.setCidadeRegistradora(enderecoVO.getBairrocodigo().getCidade());
			}
		}
	}
}
