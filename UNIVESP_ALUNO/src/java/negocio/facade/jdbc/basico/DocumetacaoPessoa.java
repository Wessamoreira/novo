package negocio.facade.jdbc.basico;


import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

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
import negocio.comuns.basico.ConfiguracaoGEDVO;
import negocio.comuns.basico.DocumetacaoPessoaVO;
import negocio.comuns.academico.TipoDocumentoVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.DocumetacaoPessoaVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.ConverterImgToPdf;
import negocio.comuns.utilitarias.UnificadorPDF;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.OrigemArquivo;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.SituacaoArquivo;
import negocio.facade.jdbc.academico.Matricula;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.arquitetura.FacadeFactory;
import negocio.interfaces.basico.DocumetacaoPessoaInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>DocumetacaoPessoaVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>DocumetacaoPessoaVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see DocumetacaoPessoaVO
 * @see ControleAcesso
 * @see Matricula
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class DocumetacaoPessoa extends ControleAcesso implements DocumetacaoPessoaInterfaceFacade {

    protected static String idEntidade;

    public DocumetacaoPessoa() throws Exception {
        super();
        setIdEntidade("Pessoa");
    }

    public DocumetacaoPessoaVO novo() throws Exception {
        DocumetacaoPessoa.incluir(getIdEntidade());
        DocumetacaoPessoaVO obj = new DocumetacaoPessoaVO();
        return obj;
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>DocumetacaoPessoaVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>DocumetacaoPessoaVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final DocumetacaoPessoaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        try {
            ////System.out.println("++++++++++++++++++++++ INCLUI DOCUMENTACAO MATRICULA +++++++++++++++++++++++++++");
            DocumetacaoPessoaVO.validarDados(obj);
            final String sql = "INSERT INTO DocumetacaoPessoa( tipoDocumento, situacao, pessoa, entregue, dataEntrega, usuario, arquivo, arquivoverso, assinardigitalmente ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";

            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setInt(1, obj.getTipoDeDocumentoVO().getCodigo().intValue());
                    sqlInserir.setString(2, obj.getSituacao());
                    sqlInserir.setInt(3, obj.getPessoa());
                    sqlInserir.setBoolean(4, obj.isEntregue().booleanValue());
                    if (obj.isEntregue().booleanValue()) {
                        sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataEntrega()));
                        sqlInserir.setInt(6, obj.getUsuario().getCodigo());
                        if (!obj.getArquivoVO().getCodigo().equals(0)) {
                            sqlInserir.setInt(7, obj.getArquivoVO().getCodigo());
                        } else {
                            sqlInserir.setNull(7, 0);
                        }
                        if (!obj.getArquivoVOVerso().getCodigo().equals(0)) {
                            sqlInserir.setInt(8, obj.getArquivoVOVerso().getCodigo());
                        } else {
                            sqlInserir.setNull(8, 0);
                        }
                    } else {
                        sqlInserir.setNull(5, 0);
                        sqlInserir.setNull(6, 0);
                        sqlInserir.setNull(7, 0);
                        sqlInserir.setNull(8, 0);
                    }
                    sqlInserir.setBoolean(9, obj.getAssinarDigitalmente());
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
            if (!obj.getArquivoVO().getNome().equals("") && !obj.getExcluirArquivo()) {
            	obj.getArquivoVO().setCodOrigem(obj.getCodigo());
                if (!obj.getArquivoVO().getCodigo().equals(0)) {
                    getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoVO(), false, usuario, configuracaoGeralSistema);
                } else {
//                    obj.getArquivoVO().setNome(obj.getArquivoVO().getNome().replace(obj.getArquivoVO().getNome().substring(0, obj.getArquivoVO().getNome().indexOf(".")),Uteis.retirarAcentuacaoAndCaracteresEspeciasRegex(obj.getArquivoVO().getCpfAlunoDocumentacao()+obj.getTipoDeDocumentoVO().getNome())));
                    getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), false, usuario, configuracaoGeralSistema);
                    alterarCodigoArquivo(obj, obj.getArquivoVO().getCodigo(), usuario);
                }
            }
            
            if (!obj.getArquivoVOVerso().getNome().equals("") && !obj.getExcluirArquivo()) {
				obj.getArquivoVOVerso().setCodOrigem(obj.getCodigo());
				if(!obj.getArquivoVOVerso().getDescricao().contains("_VERSO")) {
					obj.getArquivoVOVerso().setDescricao(obj.getArquivoVOVerso().getDescricao() + "_VERSO");
					obj.getArquivoVOVerso().setDescricaoAntesAlteracao(obj.getArquivoVOVerso().getDescricao());
				}
				if (!obj.getArquivoVOVerso().getCodigo().equals(0)) {
					getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoVOVerso(), false, usuario, configuracaoGeralSistema);
				} else {
					getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVOVerso(), false, usuario, configuracaoGeralSistema);
					alterarCodigoArquivoVerso(obj, obj.getArquivoVOVerso().getCodigo(), usuario);
				}
			}
//            if (obj.getExcluirArquivo()) {
//                getFacadeFactory().getArquivoFacade().excluirPorDocumentacaoMatricula(obj.getArquivoVO(), false, "Upload", usuario, configuracaoGeralSistema);
//                alterarCodigoArquivo(obj, null, usuario);
//            }
            obj.setNovoObj(Boolean.FALSE);

        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>DocumetacaoPessoaVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>alterar</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>DocumetacaoPessoaVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final DocumetacaoPessoaVO obj, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        DocumetacaoPessoaVO.validarDados(obj);
        final String sql = "UPDATE DocumetacaoPessoa set tipoDocumento=?, situacao=?, pessoa=?, entregue=?, dataentrega=?, usuario=?, arquivo=?, arquivoverso = ? WHERE ((codigo = ?))";

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getTipoDeDocumentoVO().getCodigo().intValue());
                sqlAlterar.setString(2, obj.getSituacao());
                sqlAlterar.setInt(3, obj.getPessoa());
                sqlAlterar.setBoolean(4, obj.isEntregue().booleanValue());
                if (obj.isEntregue().booleanValue()) {
                    sqlAlterar.setDate(5, Uteis.getDataJDBC(obj.getDataEntrega()));
                    sqlAlterar.setInt(6, obj.getUsuario().getCodigo());
                    if (!obj.getArquivoVO().getCodigo().equals(0)) {
                        sqlAlterar.setInt(7, obj.getArquivoVO().getCodigo());
                    } else {
                        sqlAlterar.setNull(7, 0);
                    }
                    if (!obj.getArquivoVOVerso().getCodigo().equals(0)) {
                        sqlAlterar.setInt(8, obj.getArquivoVOVerso().getCodigo());
                    } else {
                        sqlAlterar.setNull(8, 0);
                    }
                } else {
                    sqlAlterar.setNull(5, 0);
                    sqlAlterar.setNull(6, 0);
                    sqlAlterar.setNull(7, 0);
                    sqlAlterar.setNull(8, 0);
                }
                sqlAlterar.setInt(9, obj.getCodigo().intValue());

                return sqlAlterar;
            }
        });
        if (!obj.getArquivoVO().getNome().equals("") && !obj.getExcluirArquivo()) {
        	obj.getArquivoVO().setCodOrigem(obj.getCodigo());
            if (!obj.getArquivoVO().getCodigo().equals(0)) {
                getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoVO(), false, usuario, configuracaoGeralSistema);
            } else {
                getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVO(), false, usuario, configuracaoGeralSistema);
                alterarCodigoArquivo(obj, obj.getArquivoVO().getCodigo(), usuario);
            }
        }
        
        if (!obj.getArquivoVOVerso().getNome().equals("") && !obj.getExcluirArquivo()) {
			obj.getArquivoVOVerso().setCodOrigem(obj.getCodigo());
			if(!obj.getArquivoVOVerso().getDescricao().contains("_VERSO")) {
				obj.getArquivoVOVerso().setDescricao(obj.getArquivoVOVerso().getDescricao() + "_VERSO");
				obj.getArquivoVOVerso().setDescricaoAntesAlteracao(obj.getArquivoVOVerso().getDescricao());
			}
			if (!obj.getArquivoVOVerso().getCodigo().equals(0)) {
				getFacadeFactory().getArquivoFacade().alterar(obj.getArquivoVOVerso(), false, usuario, configuracaoGeralSistema);
			} else {
				getFacadeFactory().getArquivoFacade().incluir(obj.getArquivoVOVerso(), false, usuario, configuracaoGeralSistema);
				alterarCodigoArquivoVerso(obj, obj.getArquivoVOVerso().getCodigo(), usuario);
			}
		}
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>DocumetacaoPessoaVO</code>. Sempre localiza o
     * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>DocumetacaoPessoaVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarCodigoArquivo(DocumetacaoPessoaVO obj, Integer codArquivo, UsuarioVO usuario) throws Exception {
        String sql = "UPDATE DocumetacaoPessoa set arquivo=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{codArquivo, obj.getCodigo()});
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarCodigoArquivoVerso(DocumetacaoPessoaVO obj, Integer codArquivo, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE DocumetacaoPessoa set arquivoVerso=? WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] { Uteis.isAtributoPreenchido(codArquivo) ? codArquivo : null, obj.getCodigo() });
	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(DocumetacaoPessoaVO obj, UsuarioVO usuario) throws Exception {
        DocumetacaoPessoa.excluir(getIdEntidade());
        String sql = "DELETE FROM DocumetacaoPessoa WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    /**
     * Responsável por realizar uma consulta de <code>DocumetacaoPessoa</code> através do valor do atributo
     * <code>String situacao</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
     * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>DocumetacaoPessoaVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorSituacao(String valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DocumetacaoPessoa WHERE situacao like('" + valorConsulta + "%') ORDER BY situacao";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List consultarPorSituacaoPessoa(String situacao, Integer pessoa, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT DocumetacaoPessoa.*, tipodocumento.* FROM DocumetacaoPessoa, tipodocumento";
        sqlStr += " WHERE situacao like('" + situacao + "%') AND DocumetacaoPessoa.pessoa = " + pessoa + "";
        sqlStr += " AND DocumetacaoPessoa.tipodedocumento = tipodocumento.codigo ORDER BY tipodocumento.nome";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>DocumetacaoPessoa</code> através do valor do atributo
     * <code>String tipoDocumento</code>. Retorna os objetos, com início do valor do atributo idêntico ao parâmetro
     * fornecido. Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List
     * resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>DocumetacaoPessoaVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorTipoDeDocumento(Integer valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DocumetacaoPessoa WHERE tipoDeDocumento >= " + valorConsulta + " ORDER BY tipoDeDocumento";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>DocumetacaoPessoa</code> através do valor do atributo
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso
     * da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>DocumetacaoPessoaVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorCodigo(Integer valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM DocumetacaoPessoa WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por realizar uma consulta de <code>DocumetacaoPessoa</code> através do valor do atributo
     * <code>String nome</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido. Faz uso da
     * operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     *
     * @param controlarAcesso
     *            Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return List Contendo vários objetos da classe <code>DocumetacaoPessoaVO</code> resultantes da consulta.
     * @exception Exception
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    public List consultarPorNomeDoAluno(String valorConsulta, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT pessoa.nome, tipodocumento.nome, DocumetacaoPessoa.* from DocumetacaoPessoa" + " LEFT JOIN pessoa ON( pessoa.pessoa = DocumetacaoPessoa.pessoa )"
                + " LEFT JOIN tipodocumento ON(tipodocumento.codigo = DocumetacaoPessoa.tipodedocumento)"
                + " WHERE upper(pessoa.nome) like ('" + valorConsulta.toUpperCase() + "%')";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>DocumetacaoPessoaVO</code> resultantes da consulta.
     */
    public  List montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>DocumetacaoPessoaVO</code>.
     *
     * @return O objeto da classe <code>DocumetacaoPessoaVO</code> com os dados devidamente montados.
     */
    public  DocumetacaoPessoaVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        DocumetacaoPessoaVO obj = new DocumetacaoPessoaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getTipoDeDocumentoVO().setCodigo(new Integer(dadosSQL.getInt("tipoDocumento")));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setPessoa(dadosSQL.getInt("pessoa"));
        obj.setEntregue(dadosSQL.getBoolean("entregue"));
        obj.setDataEntrega(dadosSQL.getDate("dataentrega"));
        obj.getUsuario().setCodigo(dadosSQL.getInt("usuario"));
        obj.getArquivoVO().setCodigo(dadosSQL.getInt("arquivo"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosArquivo(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        montarDadosTipoDeDocumento(obj, usuario);
        montarDadosUsuario(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        return obj;
    }

    public  DocumetacaoPessoaVO montarDadosPersonalizado(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        DocumetacaoPessoaVO obj = new DocumetacaoPessoaVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.getTipoDeDocumentoVO().setCodigo(new Integer(dadosSQL.getInt("tipoDocumento")));
        obj.getTipoDeDocumentoVO().setNome(dadosSQL.getString("tipoDocumento_nome"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setPessoa(dadosSQL.getInt("pessoa"));
        obj.setEntregue(dadosSQL.getBoolean("entregue"));
        obj.setDataEntrega(dadosSQL.getDate("dataentrega"));
        obj.getUsuario().setCodigo(dadosSQL.getInt("usuario"));
        obj.getArquivoVO().setCodigo(dadosSQL.getInt("arquivo"));
        obj.getArquivoVOVerso().setCodigo(dadosSQL.getInt("arquivoVerso"));
        
        obj.getArquivoVOVerso().setCodigo(dadosSQL.getInt("arquivoVerso"));
        obj.setAssinarDigitalmente(dadosSQL.getBoolean("assinardigitalmente"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosArquivo(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        montarDadosArquivoVerso(obj, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
        montarDadosTipoDeDocumento(obj, usuario);
        montarDadosUsuario(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario);
        return obj;
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>DocumetacaoPessoaVO</code> no BD. Faz uso da
     * operação <code>excluir</code> disponível na classe <code>DocumetacaoPessoa</code>.
     *
     * @param <code>matricula</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirDocumetacaoPessoas(Integer pessoa, UsuarioVO usuario) throws Exception {
        String sql = "DELETE FROM DocumetacaoPessoa WHERE (pessoa = ?)";
        getConexao().getJdbcTemplate().update(sql, new Object[]{pessoa});
    }
    
    public void excluirDocumentacaoPessoa(DocumetacaoPessoaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO, UsuarioVO usuario) throws Exception{
    	removerVinculoArquivoDocumentacaoPessoa(obj,usuario);
//    	String sql = "DELETE FROM DocumetacaoPessoa WHERE (codigo = ?)";
//        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
        
    	if(Uteis.isAtributoPreenchido(obj.getArquivoVO())) {
			getFacadeFactory().getArquivoFacade().excluirPorDocumentacaoMatriculaRequerimento(obj.getArquivoVO(), true, "Upload", usuario, configuracaoGeralSistemaVO);
		}
		if(Uteis.isAtributoPreenchido(obj.getArquivoVOVerso())) {
			getFacadeFactory().getArquivoFacade().excluirPorDocumentacaoMatriculaRequerimento(obj.getArquivoVOVerso(), true, "Upload", usuario, configuracaoGeralSistemaVO);	
		}
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	private void removerVinculoArquivoDocumentacaoPessoa(DocumetacaoPessoaVO obj, UsuarioVO usuario) throws Exception {
		String sql = "UPDATE DocumetacaoPessoa set situacao='PE', entregue = false, dataentrega = null,  arquivo = null, arquivoverso = null WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
		getConexao().getJdbcTemplate().update(sql, new Object[] {obj.getCodigo()});
	}

    /**
     * Operação responsável por alterar todos os objetos da <code>DocumetacaoPessoaVO</code> contidos em um Hashtable
     * no BD. Faz uso da operação <code>excluirDocumetacaoPessoas</code> e <code>incluirDocumetacaoPessoas</code>
     * disponíveis na classe <code>DocumetacaoPessoa</code>.
     *
     * @param objetos
     *            List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarDocumetacaoPessoas(PessoaVO pessoaVO, Integer codPessoa, List objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
//        getFacadeFactory().getArquivoFacade().excluirPorDocumentacaoMatricula(matricula);
        if (!objetos.isEmpty()) {
            excluirDocumetacaoPessoas(codPessoa, usuario);
            incluirDocumetacaoPessoas(pessoaVO, codPessoa, objetos, usuario, configuracaoGeralSistema);
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>DocumetacaoPessoaVO</code> no BD. Garantindo o
     * relacionamento com a entidade principal <code>academico.Matricula</code> através do atributo de vínculo.
     *
     * @param objetos
     *            List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirDocumetacaoPessoas(PessoaVO pessoaVO, Integer pessoaPrm, List objetos, UsuarioVO usuario, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            DocumetacaoPessoaVO obj = (DocumetacaoPessoaVO) e.next();
           
            if (obj.getEntregue() && !Uteis.isAtributoPreenchido(obj.getArquivoVO().getCodigo()) && !obj.getArquivoVO().getNome().equals("")) {
            	obj.setPessoa(pessoaPrm);
                obj.setDataEntrega(new Date());
                obj.setUsuario(usuario);
//                    obj.getArquivoVO().setCpfPessoaDocumentacao(pessoaVO.getCPF());
//                    obj.getArquivoVO().setSituacao(SituacaoArquivo.ATIVO.getValor());
//                    obj.getArquivoVO().setOrigem(OrigemArquivo.DOCUMENTACAO_PROFESSOR.getValor());
//                    obj.getArquivoVO().setPastaBaseArquivoEnum(PastaBaseArquivoEnum.DOCUMENTOS_TMP);
                	UnidadeEnsinoVO unidadeEnsinoVO = getFacadeFactory().getUnidadeEnsinoFacade().obterUnidadeMatriz(false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuario);
                	ConfiguracaoGEDVO confGED = getFacadeFactory().getConfiguracaoGEDFacade().consultarPorUnidadeEnsino(unidadeEnsinoVO.getCodigo(), false, usuario);
                	if(Uteis.isAtributoPreenchido(unidadeEnsinoVO.getCodigo())) {
                		 if(confGED.getConfiguracaoGedDocumentoProfessorVO().getAssinarDocumento() && !Uteis.isAtributoPreenchido(obj.getArquivoVO().getCodigo())){
                          	assinarDocumentoProfessor(obj,unidadeEnsinoVO, usuario, confGED, configuracaoGeralSistema);
                         }
                	}
                	if(Uteis.isAtributoPreenchido(obj.getCodigo())) {
   	            	 alterar(obj, usuario, configuracaoGeralSistema);
	   	             }else {
	   	            	 incluir(obj, usuario, configuracaoGeralSistema);
	   	             }
            }else {
            	if(Uteis.isAtributoPreenchido(obj.getCodigo())) {
	            	 alterar(obj, usuario, configuracaoGeralSistema);
	             }else {
	            	 obj.setPessoa(pessoaVO.getCodigo());
	            	 obj.setSituacao(obj.getEntregue() ? "OK" : "PE");
	            	 incluir(obj, usuario, configuracaoGeralSistema);
	             }
            }
           
        }
    }

    private void assinarDocumentoProfessor(DocumetacaoPessoaVO obj, UnidadeEnsinoVO unidadeEnsinoVO, UsuarioVO usuario,ConfiguracaoGEDVO confGED, ConfiguracaoGeralSistemaVO configuracaoGeralSistema) throws Exception {
    		if((!obj.getTipoDeDocumentoVO().getDocumentoObrigatorioFuncionario() && obj.getAssinarDigitalmente()) || obj.getTipoDeDocumentoVO().getDocumentoObrigatorioFuncionario()) {
    			File file =  unificarFrenteVersoDocumentoMatricula(obj, configuracaoGeralSistema, usuario);
        	    obj.getArquivoVO().setNome(file.getName());
        	    getFacadeFactory().getDocumentoAssinadoFacade().realizarAssinaturaDocumentacaoProfessor(unidadeEnsinoVO, obj.getArquivoVO() ,confGED, file, configuracaoGeralSistema, usuario);
        	    obj.setArquivoVOVerso(new ArquivoVO());
    		}
	}

	/**
     * Operação responsável por consultar todos os <code>DocumetacaoPessoaVO</code> relacionados a um objeto da
     * classe <code>academico.Matricula</code>.
     *
     * @param matricula
     *            Atributo de <code>academico.Matricula</code> a ser utilizado para localizar os objetos da classe
     *            <code>DocumetacaoPessoaVO</code>.
     * @return List Contendo todos os objetos da classe <code>DocumetacaoPessoaVO</code> resultantes da consulta.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public  List consultarDocumetacaoPessoas(Integer pessoa, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM DocumetacaoPessoa WHERE pessoa = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{pessoa});
        while (resultado.next()) {
            objetos.add(montarDados(resultado, nivelMontarDados, usuario));
        }
        return objetos;
    }

    public List consultarDocumetacaoPessoaPorPessoa(Integer pessoa, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM DocumetacaoPessoa WHERE pessoa = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{pessoa});
        while (resultado.next()) {
            objetos.add(montarDados(resultado, nivelMontarDados, usuario));
        }
        return objetos;
    }

    public List consultarDocumetacaoPessoaPorPessoaProfessorOuEntregue(Integer pessoa, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        String sql = "SELECT DocumetacaoPessoa.*, tipoDocumento.nome AS tipoDocumento_nome "
                + "FROM DocumetacaoPessoa "
                + "INNER JOIN TipoDocumento ON DocumetacaoPessoa.TipoDocumento = TipoDocumento.codigo "
                + "WHERE pessoa = ? ";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{pessoa});
        while (resultado.next()) {
            objetos.add(montarDadosPersonalizado(resultado, nivelMontarDados, usuario));
        }
        return objetos;
    }

    public List consultarDocumetacaoPessoaPorPessoaEntregue(Integer pessoa, int nivelMontarDados, boolean entregue, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        List objetos = new ArrayList(0);
        String sql = "SELECT * FROM DocumetacaoPessoa WHERE pessoa = ? AND entregue = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{pessoa, entregue});
        while (resultado.next()) {
            objetos.add(montarDados(resultado,nivelMontarDados, usuario));
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>DocumetacaoPessoaVO</code> através de sua chave
     * primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public DocumetacaoPessoaVO consultarPorChavePrimaria(Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM DocumetacaoPessoa WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    public  void montarDadosArquivo(DocumetacaoPessoaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getArquivoVO().getCodigo().intValue() == 0) {
            obj.setArquivoVO(new ArquivoVO());
            return;
        }
        obj.setArquivoVO(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoVO().getCodigo(), nivelMontarDados, usuario));
    }
    
    public  void montarDadosArquivoVerso(DocumetacaoPessoaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getArquivoVOVerso().getCodigo().intValue() == 0) {
            obj.setArquivoVOVerso(new ArquivoVO());
            return;
        }
        obj.setArquivoVOVerso(getFacadeFactory().getArquivoFacade().consultarPorChavePrimaria(obj.getArquivoVOVerso().getCodigo(), nivelMontarDados, usuario));
    }

    public  void montarDadosTipoDeDocumento(DocumetacaoPessoaVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getTipoDeDocumentoVO().getCodigo().intValue() == 0) {
            obj.setTipoDeDocumentoVO(new TipoDocumentoVO());
            return;
        }
        obj.setTipoDeDocumentoVO(getFacadeFactory().getTipoDeDocumentoFacade().consultarPorChavePrimaria(obj.getTipoDeDocumentoVO().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, usuario));
    }

    public  void montarDadosUsuario(DocumetacaoPessoaVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getUsuario().getCodigo().intValue() == 0) {
            obj.setUsuario(new UsuarioVO());
            return;
        }
        obj.setUsuario(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getUsuario().getCodigo(), nivelMontarDados, usuario));
    }

    public void montarDocumentacaoPessoaPorTipoDeDocumentos(Integer pessoa, List<TipoDocumentoVO> listaTipoDocumentoVO, List<DocumetacaoPessoaVO> listaDocumentacaoPessoaVO) throws Exception {
        for (TipoDocumentoVO obj : listaTipoDocumentoVO) {
            DocumetacaoPessoaVO documentacaoPessoa = new DocumetacaoPessoaVO();
            documentacaoPessoa.setTipoDeDocumentoVO(obj);
            documentacaoPessoa.setPessoa(pessoa);
            documentacaoPessoa.setEntregue(Boolean.FALSE);
            documentacaoPessoa.setSituacao("PE");
            documentacaoPessoa.setDataEntrega(null);
            listaDocumentacaoPessoaVO.add(documentacaoPessoa);
        }
    }

    private StringBuffer getSQLPadraoConsultaBasica() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT DocumetacaoPessoa.codigo, DocumetacaoPessoa.pessoa, DocumetacaoPessoa.entregue, DocumetacaoPessoa.situacao, ");
        sql.append("DocumetacaoPessoa.dataEntrega, tipoDocumento.codigo AS \"tipoDocumento.codigo\", tipoDocumento.nome AS \"tipoDocumento.nome\", ");
        sql.append("DocumetacaoPessoa.usuario, arquivo.codigo AS codArquivo, arquivo.pastaBaseArquivo, arquivo.nome AS nomeArquivo, ");
        sql.append("arquivo.descricao as descricaoArquivo, arquivo.cpfAlunoDocumentacao FROM DocumetacaoPessoa ");
        sql.append("INNER JOIN tipoDocumento ON tipoDocumento.codigo = DocumetacaoPessoa.tipodeDocumento ");
        sql.append("LEFT JOIN arquivo ON arquivo.codigo = DocumetacaoPessoa.arquivo ");
        return sql;
    }

    public List<DocumetacaoPessoaVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado) throws Exception {
        List<DocumetacaoPessoaVO> vetResultado = new ArrayList<DocumetacaoPessoaVO>(0);
        while (tabelaResultado.next()) {
            DocumetacaoPessoaVO obj = new DocumetacaoPessoaVO();
            montarDadosBasico(obj, tabelaResultado);
            vetResultado.add(obj);
        }
        return vetResultado;
    }

    private void montarDadosBasico(DocumetacaoPessoaVO obj, SqlRowSet dadosSQL) {
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setPessoa(dadosSQL.getInt("pessoa"));
        obj.setEntregue(dadosSQL.getBoolean("entregue"));
        obj.setSituacao(dadosSQL.getString("situacao"));
        obj.setDataEntrega(dadosSQL.getDate("dataEntrega"));
        obj.getTipoDeDocumentoVO().setCodigo(dadosSQL.getInt("tipoDocumento.codigo"));
        obj.getTipoDeDocumentoVO().setNome(dadosSQL.getString("tipoDocumento.nome"));
        obj.getUsuario().setCodigo(dadosSQL.getInt("usuario"));
        obj.getArquivoVO().setCodigo(dadosSQL.getInt("codArquivo"));
        obj.getArquivoVO().setPastaBaseArquivo(dadosSQL.getString("pastaBaseArquivo"));
        obj.getArquivoVO().setNome(dadosSQL.getString("nomeArquivo"));
        obj.getArquivoVO().setDescricao(dadosSQL.getString("descricaoArquivo"));
        obj.getArquivoVO().setDescricaoAntesAlteracao(dadosSQL.getString("descricaoArquivo"));
        obj.getArquivoVO().setCpfAlunoDocumentacao(dadosSQL.getString("cpfAlunoDocumentacao"));
    }

    public List<DocumetacaoPessoaVO> consultaRapidaPorPessoa(Integer pessoa, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), controlarAcesso, usuario);
        StringBuffer sql = getSQLPadraoConsultaBasica();
        sql.append(" WHERE upper(DocumetacaoPessoa.pessoa) = ").append(pessoa).append(" ");
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        return montarDadosConsultaBasica(resultado);
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return DocumetacaoPessoa.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        DocumetacaoPessoa.idEntidade = idEntidade;
    }

    public List consultarDocumetacaoPessoas(String pessoa, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List<DocumetacaoPessoaVO> consultarPorSituacaoPessoa(String string, String pessoa, int nivelMontarDados, boolean b, UsuarioVO usuario) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }


	@Override
	public void removerTipoDocumento(List<DocumetacaoPessoaVO> listaDocumentacaoPessoaProfessor,List<TipoDocumentoVO> listaTipoDocumento, TipoDocumentoVO tipoDocumentoRemover, UsuarioVO usuarioVO) throws Exception {
		Optional<DocumetacaoPessoaVO> obj = listaDocumentacaoPessoaProfessor.stream().filter(dp -> dp.getTipoDeDocumentoVO().getCodigo().equals(tipoDocumentoRemover.getCodigo())).findFirst();
		if (obj.isPresent()) {
			DocumetacaoPessoaVO documetacaoPessoaVO = obj.get();
			if(documetacaoPessoaVO.getEntregue()) {
				throw new Exception("Esse Tipo de Documento não pode ser excluído, pois possuí Documento Entregue Vinculado.");
			}
			Iterator<DocumetacaoPessoaVO> iterator = listaDocumentacaoPessoaProfessor.iterator();
			while(iterator.hasNext()) {
				DocumetacaoPessoaVO documetacaoPessoa = (DocumetacaoPessoaVO) iterator.next();
				if (documetacaoPessoa.getTipoDeDocumentoVO().getCodigo().equals(tipoDocumentoRemover.getCodigo())) {
					iterator.remove();
					if(Uteis.isAtributoPreenchido(documetacaoPessoaVO.getCodigo())) {
						excluir(documetacaoPessoaVO, usuarioVO);
					}
					break;
				}
			}
		}
		listaTipoDocumento.remove(tipoDocumentoRemover);
	}
	
	public File unificarFrenteVersoDocumentoMatricula(DocumetacaoPessoaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO , UsuarioVO usuarioVO) throws Exception {
		if(obj.getArquivoVO().getIsImagem()) {
			return unificarFrenteVersoDocumentoIMG(obj, configuracaoGeralSistemaVO,usuarioVO);
		}else{
			return unificarFrenteVersoDocumentoPDF(obj, configuracaoGeralSistemaVO, usuarioVO);
		}		
	}
	
	private File unificarFrenteVersoDocumentoIMG(DocumetacaoPessoaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO , UsuarioVO usuarioVO) throws Exception {
		List<String> files = new ArrayList<String>();
		String caminhoPdf ="";	
			String caminhoFrente = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() +File.separator+ obj.getArquivoVO().getPastaBaseArquivo()+File.separator +obj.getArquivoVO().getNome();
			String caminhoVerso = Uteis.isAtributoPreenchido(obj.getArquivoVOVerso().getNome()) ? configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() +File.separator+obj.getArquivoVOVerso().getPastaBaseArquivo() +File.separator + obj.getArquivoVOVerso().getNome() : "";
			
			files.add(caminhoFrente);
			if(Uteis.isAtributoPreenchido(caminhoVerso)) {
				files.add(caminhoVerso);
			}
			String nomeNovoArquivo = usuarioVO.getCodigo()+""+ (new Date().getTime()) + ".pdf";
			String caminhoBasePdf = configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() +File.separator+ obj.getArquivoVO().getPastaBaseArquivo() + File.separator;  
			caminhoPdf = caminhoBasePdf + nomeNovoArquivo;
		ConverterImgToPdf.realizarConversaoPdf(files, caminhoPdf);
		File  frente = new File(caminhoFrente);
		frente.delete();
		if(Uteis.isAtributoPreenchido(caminhoVerso)) {
			File  verso = new File(caminhoVerso);
			verso.delete();
		}
		return new File(caminhoPdf);
	}

	private File unificarFrenteVersoDocumentoPDF(DocumetacaoPessoaVO obj, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO , UsuarioVO usuarioVO) throws Exception {
		List<File> files = new ArrayList<File>(0);
		String caminhoPdf = "";
			files.add(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() +File.separator+ obj.getArquivoVO().getPastaBaseArquivo()+File.separator +obj.getArquivoVO().getNome()));
			if(Uteis.isAtributoPreenchido(obj.getArquivoVOVerso().getNome())) {
				files.add(new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() +File.separator + obj.getArquivoVOVerso().getPastaBaseArquivo() +File.separator + obj.getArquivoVOVerso().getNome()));
			}else {
				return new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() +File.separator+ obj.getArquivoVO().getPastaBaseArquivo()+File.separator +obj.getArquivoVO().getNome());
			}
			
			String nomeNovoArquivo = usuarioVO.getCodigo()+""+ (new Date().getTime()) + ".pdf";
			String caminhoBasePdf =configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() + File.separator + obj.getArquivoVO().getPastaBaseArquivo() + File.separator;  
			caminhoPdf = caminhoBasePdf + nomeNovoArquivo;
		UnificadorPDF.realizarUnificacaoListaPdf(files, caminhoPdf);
		File  frente = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() +File.separator+ obj.getArquivoVO().getPastaBaseArquivo()+File.separator +obj.getArquivoVO().getNome());
		frente.delete();
		if(Uteis.isAtributoPreenchido(obj.getArquivoVOVerso().getNome())) {
			File  verso = new File(configuracaoGeralSistemaVO.getLocalUploadArquivoFixo() +File.separator + obj.getArquivoVOVerso().getPastaBaseArquivo() +File.separator + obj.getArquivoVOVerso().getNome());
			verso.delete();
		}
		return new File(caminhoPdf);
	}

}
