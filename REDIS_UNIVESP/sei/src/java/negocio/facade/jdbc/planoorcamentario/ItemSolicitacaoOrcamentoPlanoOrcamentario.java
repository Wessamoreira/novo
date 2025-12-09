package negocio.facade.jdbc.planoorcamentario;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.SerializationUtils;
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
import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.CategoriaDespesaVO;
import negocio.comuns.planoorcamentario.DetalhamentoPeriodoOrcamentoVO;
import negocio.comuns.planoorcamentario.HistoricoRemanejamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.ItemSolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.SolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.Curso;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.planoorcamentario.ItemSolicitacaoOrcamentoPlanoOrcamentarioInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>ItemSolicitacaoOrcamentoPlanoOrcamentarioVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>ItemSolicitacaoOrcamentoPlanoOrcamentarioVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ItemSolicitacaoOrcamentoPlanoOrcamentarioVO
 * @see ControleAcesso
 * @see Curso
 */
@SuppressWarnings({"unchecked", "rawtypes"})
@Repository
@Scope("singleton")
@Lazy
public class ItemSolicitacaoOrcamentoPlanoOrcamentario extends ControleAcesso implements ItemSolicitacaoOrcamentoPlanoOrcamentarioInterfaceFacade {

	private static final long serialVersionUID = -4192572792013927060L;

	protected static String idEntidade;

    public ItemSolicitacaoOrcamentoPlanoOrcamentario() throws Exception {
        super();
        setIdEntidade("SolicitacaoOrcamentoPlanoOrcamentario");
    }

    public ItemSolicitacaoOrcamentoPlanoOrcamentarioVO novo() throws Exception {
        ItemSolicitacaoOrcamentoPlanoOrcamentario.incluir(getIdEntidade());
        ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj = new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO();
        return obj;
    }

    public static void validarDados(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj) throws ConsistirException {
		if (!obj.isValidarDados().booleanValue()) {
			return;
		}
		if (obj.getDescricao().equals("")) {
			throw new ConsistirException("O campo DESCRIÇÃO (Item Solicitação Orçamento Plano Orçamentário) deve ser informado.");
		}
		if (obj.getCategoriaDespesa().getCodigo().intValue() == 0) {
			throw new ConsistirException("O campo CATEGORIA DE DESPESA (Item Solicitação Orçamento Plano Orçamentário) deve ser informado.");
		}

		if (obj.getValor().doubleValue() == 0) {
			throw new ConsistirException("O campo VALOR (Item Solicitação Orçamento Plano Orçamentário) deve ser informado.");
		}

		/*if(!obj.getDetalhamentoPeriodoOrcamentoVOs().isEmpty()) {
			getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade().validarDadosValorMensalOrcamentoTotalPrevisto(obj.getValorBaseUtilizar(), obj.getDetalhamentoPeriodoOrcamentoVOs());
		}*/
	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void persistirTodos(List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> itemSolicitacaoOrcamentoPlanoOrcamentarioVOs, UsuarioVO usuario) throws Exception {
    	for (ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO : itemSolicitacaoOrcamentoPlanoOrcamentarioVOs) {
    		persistir(itemSolicitacaoOrcamentoPlanoOrcamentarioVO, usuario);
		}
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    @Override
    public void persistir(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
    	if (!Uteis.isAtributoPreenchido(obj.getCodigo())) {
    		incluir(obj, usuario);
    	} else {
    		alterar(obj, usuario);
    	}
    }

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj);
        final String sql = "INSERT INTO ItemSolicitacaoOrcamentoPlanoOrcamentario( solicitacaoOrcamentoPlanoOrcamentario, descricao, categoriaDespesa, unidadeEnsino, turma, valor, valorAutorizado) VALUES ( ? , ?, ?, ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getSolicitacaoOrcamentoPlanoOrcamentario().getCodigo());
                sqlInserir.setString(2, obj.getDescricao());
                sqlInserir.setInt(3, obj.getCategoriaDespesa().getCodigo().intValue());
                sqlInserir.setInt(4, obj.getUnidadeEnsino().intValue());
                sqlInserir.setInt(5, obj.getTurma().getCodigo().intValue());
                sqlInserir.setDouble(6, obj.getValor().doubleValue());
                sqlInserir.setBigDecimal(7, obj.getValorAutorizado());
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
        getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade().incluirDetalhamentoPeriodoOrcamentario(obj, usuario);
        obj.setNovoObj(Boolean.FALSE);
    }

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterar(final ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
		alterar(obj, usuario, false);
	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario, boolean remanejamento) throws Exception {
    	if (remanejamento) {
    		validarDados(obj);
    	}
        final String sql = "UPDATE ItemSolicitacaoOrcamentoPlanoOrcamentario set solicitacaoOrcamentoPlanoOrcamentario=?, descricao=?, categoriaDespesa=?, unidadeEnsino=?, turma=?, valor=?, valorAutorizado=? WHERE (codigo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

        if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setInt(1, obj.getSolicitacaoOrcamentoPlanoOrcamentario().getCodigo());
                sqlAlterar.setString(2, obj.getDescricao());
                sqlAlterar.setInt(3, obj.getCategoriaDespesa().getCodigo().intValue());
                sqlAlterar.setInt(4, obj.getUnidadeEnsino().intValue());
                sqlAlterar.setInt(5, obj.getTurma().getCodigo().intValue());
                sqlAlterar.setDouble(6, obj.getValor().doubleValue());
                sqlAlterar.setBigDecimal(7, obj.getValorAutorizado());
                sqlAlterar.setInt(8, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        }) == 0) {
        	incluir(obj, usuario);
        	return;
        }
        getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade().alterarDetalhamentoPeriodoOrcamentario(obj, usuario);
    }


    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        ItemSolicitacaoOrcamentoPlanoOrcamentario.excluir(getIdEntidade());
        String sql = "DELETE FROM ItemSolicitacaoOrcamentoPlanoOrcamentario WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    public List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ItemSolicitacaoOrcamentoPlanoOrcamentario WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, usuario, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
    }

    public List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> consultarItemSolicitacaoOrcamentarioPorPlanoOrcamentarioDepartamentoUnidadeEnsino(Integer planoOrcamentario, Integer departamento, Integer unidadeEnsino, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> objetos = new ArrayList<>(0);
        String sql = "select itemSolicitacao.* from itemsolicitacaoorcamentoplanoorcamentario itemSolicitacao "
                + "inner join solicitacaoorcamentoplanoorcamentario solicitacao on solicitacao.codigo  = itemSolicitacao.solicitacaoorcamentoplanoorcamentario "
                + "WHERE solicitacao.planoorcamentario = ? AND solicitacao.departamento = ? AND solicitacao.unidadeEnsino = ? ";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{planoOrcamentario, departamento, unidadeEnsino});
        while (resultado.next()) {
            objetos.add(montarDados(resultado, usuario, Uteis.NIVELMONTARDADOS_DADOSBASICOS));
        }
        return objetos;
    }

     /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>ItemSolicitacaoOrcamentoPlanoOrcamentarioVO</code> resultantes da consulta.
     */
    public static List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario, int nivelMontarDados) throws Exception {
        List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario, nivelMontarDados));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>ItemSolicitacaoOrcamentoPlanoOrcamentarioVO</code>.
     *
     * @return O objeto da classe <code>ItemSolicitacaoOrcamentoPlanoOrcamentarioVO</code> com os dados devidamente montados.
     */
    public static ItemSolicitacaoOrcamentoPlanoOrcamentarioVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario, int nivelMontarDados) throws Exception {
        ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj = new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        if (Uteis.isAtributoPreenchido(dadosSQL.getInt("solicitacaoOrcamentoPlanoOrcamentario"))) {
        	obj.getSolicitacaoOrcamentoPlanoOrcamentario().setCodigo(dadosSQL.getInt("solicitacaoOrcamentoPlanoOrcamentario"));
        }
        obj.setDescricao(dadosSQL.getString("descricao"));
        obj.getCategoriaDespesa().setCodigo(new Integer(dadosSQL.getInt("categoriaDespesa")));
        obj.getTurma().setCodigo(dadosSQL.getInt("turma"));
        obj.setUnidadeEnsino(dadosSQL.getInt("unidadeEnsino"));
        obj.setValor(dadosSQL.getDouble("valor"));
        obj.setValorAutorizado(dadosSQL.getBigDecimal("valorAutorizado"));
        obj.setNovoObj(Boolean.FALSE);
        montarDadosCategoriaDespesa(obj, usuario);
        montarDadosTurma(obj, usuario);
        
        if (Uteis.NIVELMONTARDADOS_TODOS == nivelMontarDados) {
        	obj.setDetalhamentoPeriodoOrcamentoVOs(getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade()
        			.consultarDetalhamentoPorItemSolicitacaoPlanoOrcamentario(dadosSQL.getInt("codigo"), nivelMontarDados, false, usuario));
        }
        return obj;
        
    }

    /**
     * Monta os dados com objetos transiente.
     * 
     * @param dadosSQL
     * @param usuario
     * @return
     * @throws Exception
     */
    public ItemSolicitacaoOrcamentoPlanoOrcamentarioVO montarDadosComTransite(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
    	ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj = new ItemSolicitacaoOrcamentoPlanoOrcamentarioVO();
    	obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
    	if (Uteis.isAtributoPreenchido(dadosSQL.getInt("solicitacaoOrcamentoPlanoOrcamentario"))) {
    		obj.getSolicitacaoOrcamentoPlanoOrcamentario().setCodigo(dadosSQL.getInt("solicitacaoOrcamentoPlanoOrcamentario"));
    		//Casusando lentidão ao editar plano orçamentario.
        	/*obj.setSolicitacaoOrcamentoPlanoOrcamentario(getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade()
        			.consultarPorChavePrimaria(dadosSQL.getInt("solicitacaoOrcamentoPlanoOrcamentario"), false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuario));*/
        }
    	obj.setDescricao(dadosSQL.getString("descricao"));
    	obj.getCategoriaDespesa().setCodigo(new Integer(dadosSQL.getInt("categoriaDespesa")));
    	obj.setValor(new Double(dadosSQL.getDouble("valor")));
    	obj.setValorConsumido(new Double(dadosSQL.getDouble("valorconsumido")));
    	obj.setValorAutorizado(dadosSQL.getBigDecimal("valorAutorizado"));
    	obj.setNovoObj(Boolean.FALSE);
    	obj.setDetalhamentoPeriodoOrcamentoVOs(getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade().consultarDetalhamentoPorItemSolicitacaoPlanoOrcamentario(obj.getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, false, usuario));
    	Ordenacao.ordenarLista(obj.getDetalhamentoPeriodoOrcamentoVOs(), "ordenacao");
    	montarDadosCategoriaDespesa(obj, usuario);
    	
    	return obj;
    }

    public static void montarDadosTurma(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getTurma().getCodigo().intValue() == 0) {
            obj.setTurma(new TurmaVO());
            return;
        }
        obj.setTurma(getFacadeFactory().getTurmaFacade().consultarPorChavePrimaria(obj.getTurma().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    public static void montarDadosCategoriaDespesa(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getCategoriaDespesa().getCodigo().intValue() == 0) {
            obj.setCategoriaDespesa(new CategoriaDespesaVO());
            return;
        }
        obj.setCategoriaDespesa(getFacadeFactory().getCategoriaDespesaFacade().consultarPorChavePrimaria(obj.getCategoriaDespesa().getCodigo(), false,Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirItemSolicitacaoOrcamentoPlanoOrcamentarios(Integer solicitacaoOrcamentoPlanoOrcamentario, List objetos, UsuarioVO usuario) throws Exception {
        ItemSolicitacaoOrcamentoPlanoOrcamentario.excluir(getIdEntidade());
        StringBuilder sql = new StringBuilder("DELETE FROM ItemSolicitacaoOrcamentoPlanoOrcamentario WHERE (solicitacaoOrcamentoPlanoOrcamentario = ?) and codigo not in (0 ");
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj = (ItemSolicitacaoOrcamentoPlanoOrcamentarioVO) e.next();         
            if(Uteis.isAtributoPreenchido(obj.getCodigo())){
            	sql.append(", ").append(obj.getCodigo());
            }
        }
        sql.append(") ");
        sql.append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
        getConexao().getJdbcTemplate().update(sql.toString(), new Object[]{solicitacaoOrcamentoPlanoOrcamentario});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarItemSolicitacaoOrcamentoPlanoOrcamentarios(Integer planoOrcamentario, List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> objetos, UsuarioVO usuario) throws Exception {
        excluirItemSolicitacaoOrcamentoPlanoOrcamentarios(planoOrcamentario, objetos, usuario);
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj = (ItemSolicitacaoOrcamentoPlanoOrcamentarioVO) e.next();
            obj.getSolicitacaoOrcamentoPlanoOrcamentario().setCodigo(planoOrcamentario);
            if(Uteis.isAtributoPreenchido(obj.getCodigo())){
            	alterar(obj, usuario);
            }else {
            	incluir(obj, usuario);
            }
        }        
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirItemSolicitacaoOrcamentoPlanoOrcamentarios(Integer solicitacaoOrcamentoPlanoOrcamentarioPrm, List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
            ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj = (ItemSolicitacaoOrcamentoPlanoOrcamentarioVO) e.next();
            obj.getSolicitacaoOrcamentoPlanoOrcamentario().setCodigo(solicitacaoOrcamentoPlanoOrcamentarioPrm);
            incluir(obj, usuario);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>ItemSolicitacaoOrcamentoPlanoOrcamentarioVO</code> relacionados a um objeto da classe
     * <code>academico.Curso</code>.
     *
     * @param curso
     *            Atributo de <code>academico.Curso</code> a ser utilizado para localizar os objetos da classe
     *            <code>ItemSolicitacaoOrcamentoPlanoOrcamentarioVO</code>.
     * @return List Contendo todos os objetos da classe <code>ItemSolicitacaoOrcamentoPlanoOrcamentarioVO</code> resultantes da consulta.
     * @exception Exception
     *                Erro de conexão com o BD ou restrição de acesso a esta operação.
     */
    public static List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> consultarItemSolicitacaoOrcamentoPlanoOrcamentarios(Integer planoOrcamentario, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> objetos = new ArrayList<>(0);
        String sql = "SELECT * FROM ItemSolicitacaoOrcamentoPlanoOrcamentario WHERE solicitacaoOrcamentoPlanoOrcamentario = ?";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, planoOrcamentario);
        while (resultado.next()) {
            objetos.add(ItemSolicitacaoOrcamentoPlanoOrcamentario.montarDados(resultado, usuario, Uteis.NIVELMONTARDADOS_TODOS));
        }
        return objetos;
    }

    public List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> consultarItemSolicitacaoOrcamentoPlanoOrcamentario(Integer codigo, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	List<ItemSolicitacaoOrcamentoPlanoOrcamentarioVO> objetos = new ArrayList<>(0);
    	StringBuilder sql = new StringBuilder();
    	sql.append(" SELECT ItemSolicitacaoOrcamentoPlanoOrcamentario.*, ( ");
    	sql.append(getFacadeFactory().getRequisicaoFacade().getSqlBaseValorConsumidoPlanoOrcamentario(null, null, null, null, null, null, null, null));
    	sql.append(" and requisicaoitem.itemsolicitacaoorcamentoplanoorcamentario = itemsolicitacaoorcamentoplanoorcamentario.codigo) as valorconsumido");
    	sql.append(" FROM ItemSolicitacaoOrcamentoPlanoOrcamentario ");
    	sql.append(" inner join solicitacaoorcamentoplanoorcamentario as solicitacaoorcamento on solicitacaoorcamento.codigo = ItemSolicitacaoOrcamentoPlanoOrcamentario.solicitacaoorcamentoplanoorcamentario");
    	sql.append(" WHERE solicitacaoOrcamentoPlanoOrcamentario = ? ");
    	
    	SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{codigo});
    	while (resultado.next()) {
    		objetos.add(montarDadosComTransite(resultado, usuario));
    	}
    	return objetos;
    }

    public ItemSolicitacaoOrcamentoPlanoOrcamentarioVO consultarPorChavePrimaria(Integer codigoPrm, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        StringBuilder sql = new StringBuilder("SELECT ItemSolicitacaoOrcamentoPlanoOrcamentario.*, (");  
        sql.append(getFacadeFactory().getRequisicaoFacade().getSqlBaseValorConsumidoPlanoOrcamentario(null, null, null, null, null, null, null, null));
    	sql.append(" and requisicaoitem.itemsolicitacaoorcamentoplanoorcamentario = itemsolicitacaoorcamentoplanoorcamentario.codigo) as valorconsumido");
    	sql.append(" FROM ItemSolicitacaoOrcamentoPlanoOrcamentario WHERE codigo = ? ");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados(ItemSolicitacaoOrcamentoPlanoOrcamentario - "+codigoPrm+").");
        }
        return (montarDadosComTransite(tabelaResultado, usuario));
    }

    public ItemSolicitacaoOrcamentoPlanoOrcamentarioVO consultarItemSolicitacaoOrcamentoPlanoOrcamentarioPorValorSolicitadoUnidadeEnsinoCategoriaDespesaDepartamento(Double valorSolicitado, UnidadeEnsinoVO unidadeEnsinoVO, DepartamentoVO departamentoVO, CategoriaDespesaVO categoriaDespesaVO, Date mesAnoConsiderar, UsuarioVO usuario) throws Exception {
		StringBuilder sql = new StringBuilder();
	
		sql.append(" with categoriadespesas as (select \"categoriadespesa.codigo\", \"categoriadespesa.descricao\", \"categoriadespesa.identificadorcategoriadespesa\", \"categoriadespesaprincipal.codigo\" from (");
		sql.append(" WITH RECURSIVE  cdSuperior (  \"categoriadespesa.codigo\", \"categoriadespesa.descricao\", \"categoriadespesa.identificadorcategoriadespesa\", \"categoriadespesaprincipal.codigo\"    )as (");
		sql.append(" SELECT categoriadespesa.codigo as \"categoriadespesa.codigo\", categoriadespesa.descricao as \"categoriadespesa.descricao\" , categoriadespesa.identificadorcategoriadespesa as \"categoriadespesa.identificadorcategoriadespesa\", categoriadespesa.categoriadespesaprincipal as \"categoriadespesaprincipal.codigo\"");
		sql.append(" FROM categoriadespesa  where categoriadespesa.codigo = ").append(categoriaDespesaVO.getCodigo());
		sql.append(" union ");
		sql.append(" SELECT categoriadespesa.codigo as \"categoriadespesa.codigo\", categoriadespesa.descricao as \"categoriadespesa.descricao\",  categoriadespesa.identificadorcategoriadespesa as \"categoriadespesa.identificadorcategoriadespesa\", categoriadespesa.categoriadespesaprincipal as \"categoriadespesaprincipal.codigo\"");
		sql.append(" FROM categoriadespesa");
		sql.append(" inner join cdSuperior on categoriadespesa.codigo = cdSuperior.\"categoriadespesaprincipal.codigo\" )");
		sql.append(" select * from cdSuperior order by   case when cdSuperior.\"categoriadespesaprincipal.codigo\" is null then 0   when cdSuperior.\"categoriadespesaprincipal.codigo\" > cdSuperior.\"categoriadespesa.codigo\" then cdSuperior.\"categoriadespesa.codigo\"   else cdSuperior.\"categoriadespesaprincipal.codigo\" end,   cdSuperior.\"categoriadespesa.codigo\"");
		sql.append(" ) as t )");
		sql.append(" select itemsolicitacaoorcamentoplanoorcamentario.*,  ");
		sql.append(" coalesce( (select sum(quantidadeautorizada * valorunitario) from requisicaoitem ");
		sql.append(" inner join requisicao on requisicao.codigo = requisicaoitem.requisicao"); 
		sql.append(" where requisicaoitem.itemsolicitacaoorcamentoplanoorcamentario = itemsolicitacaoorcamentoplanoorcamentario.codigo), 0.0) as valorconsumido ");
		sql.append(" from planoorcamentario ");
		sql.append(" inner join solicitacaoorcamentoplanoorcamentario on solicitacaoorcamentoplanoorcamentario.planoorcamentario = planoorcamentario.codigo");
		sql.append(" inner join departamento on departamento.codigo = solicitacaoorcamentoplanoorcamentario.departamento");
		sql.append(" inner join itemsolicitacaoorcamentoplanoorcamentario on itemsolicitacaoorcamentoplanoorcamentario.solicitacaoorcamentoplanoorcamentario = solicitacaoorcamentoplanoorcamentario.codigo");
		if(mesAnoConsiderar != null) {
			sql.append(" inner join detalhamentoperiodoorcamentario on itemsolicitacaoorcamentoplanoorcamentario.codigo = detalhamentoperiodoorcamentario.itemsolicitacaoorcamentoplanoorcamentario ");
			sql.append(" and detalhamentoperiodoorcamentario.ano = '").append(Uteis.getAno(mesAnoConsiderar)).append("' ");
			sql.append(" and detalhamentoperiodoorcamentario.mes = '").append(MesAnoEnum.getMesData(mesAnoConsiderar).name()).append("' ");
			sql.append(" and detalhamentoperiodoorcamentario.orcamentoTotal > 0 ");
			sql.append(" and  (detalhamentoperiodoorcamentario.orcamentoTotal::numeric - ");
			sql.append(" coalesce(( ");
			sql.append(getFacadeFactory().getRequisicaoFacade().getSqlBaseValorConsumidoPlanoOrcamentario(null, null, null, null, null, MesAnoEnum.getMesData(mesAnoConsiderar), Uteis.getAno(mesAnoConsiderar), null));
			sql.append(" and ( requisicaoitem.itemsolicitacaoorcamentoplanoorcamentario = itemsolicitacaoorcamentoplanoorcamentario.codigo)), 0.0) >= ").append(valorSolicitado).append(")");
			
		}
		sql.append(" inner join categoriadespesas on itemsolicitacaoorcamentoplanoorcamentario.categoriadespesa = categoriadespesas.\"categoriadespesa.codigo\"");
		sql.append(" where departamento.codigo = ").append(departamentoVO.getCodigo());
		sql.append(" and  solicitacaoorcamentoplanoorcamentario.unidadeensino = ").append(unidadeEnsinoVO.getCodigo());
		sql.append(" and  planoorcamentario.datainicio <= current_date and  planoorcamentario.datafinal >= current_date ");
		sql.append(" and  planoorcamentario.situacao = 'AT' ");
		sql.append(" and  itemsolicitacaoorcamentoplanoorcamentario.valorautorizado > 0 ");
		sql.append(" and  solicitacaoorcamentoplanoorcamentario.situacao = 'AP' ");
		if(mesAnoConsiderar != null) {
			
		}
		sql.append(" and  (itemsolicitacaoorcamentoplanoorcamentario.valorautorizado - ");
		sql.append(" coalesce(( ");
		sql.append(getFacadeFactory().getRequisicaoFacade().getSqlBaseValorConsumidoPlanoOrcamentario(null, null, null, null, null, null, null, null));
		sql.append(" and ( requisicaoitem.itemsolicitacaoorcamentoplanoorcamentario = itemsolicitacaoorcamentoplanoorcamentario.codigo)), 0.0) >= ").append(valorSolicitado).append(")");
		
		sql.append(" order by case when  categoriadespesas.\"categoriadespesa.codigo\" = ").append(categoriaDespesaVO.getCodigo());
		sql.append(" then 0 else 1 end limit 1 ");

		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if(tabelaResultado.next()) {
        	return montarDadosComTransite(tabelaResultado, usuario);
        }
        return null;
	}

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ItemSolicitacaoOrcamentoPlanoOrcamentario.idEntidade;
    }

    public void setIdEntidade(String idEntidade) {
        ItemSolicitacaoOrcamentoPlanoOrcamentario.idEntidade = idEntidade;
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void alterarValorAprovado(SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO, UsuarioVO usuarioVO) throws Exception{
		 for(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO: solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs()) {
			 getConexao().getJdbcTemplate().update("update itemSolicitacaoOrcamentoPlanoOrcamentario set valorAutorizado = ? where codigo = ? "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO), itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getValorAutorizado(), itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getCodigo());
			 getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade().alterarValorPorMesDeAcordoComValorAprovado(itemSolicitacaoOrcamentoPlanoOrcamentarioVO, usuarioVO);
		 }
	}
    
    @Override
    public void calcularValorRemanejamento(
    		SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO, 
    		ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSelecionadoRemanejamento,
    		ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemRemanejar, boolean executarDistribuicao) throws Exception {
    	Double valorRemanejar = 0.0;
    	boolean valorRemanejarMaiorLimite = false;
    	String mensagem = "";
    	for(DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO: itemSelecionadoRemanejamento.getDetalhamentoPeriodoOrcamentoVOs()) {
    		if(detalhamentoPeriodoOrcamentoVO.getValorRemanejar() > 0.0 && detalhamentoPeriodoOrcamentoVO.getValorRemanejar() > detalhamentoPeriodoOrcamentoVO.getValorDisponivel()) {
    			detalhamentoPeriodoOrcamentoVO.setValorRemanejar(0.0);
    			valorRemanejarMaiorLimite = true;
    			mensagem = "O VALOR REMANEJAR do mês "+detalhamentoPeriodoOrcamentoVO.getMes().getMes()+"/"+detalhamentoPeriodoOrcamentoVO.getAno()+" não pode ser maior que o valor limite disponível.";
    		}else if(detalhamentoPeriodoOrcamentoVO.getValorRemanejar() > 0.0) {
    			valorRemanejar += detalhamentoPeriodoOrcamentoVO.getValorRemanejar();
    		}
    	}

    	itemRemanejar.setValor(Uteis.arrendondarForcando2CadasDecimais(valorRemanejar));
    	itemRemanejar.setValorAutorizado(BigDecimal.valueOf(valorRemanejar));

    	if (valorRemanejarMaiorLimite) {
    		throw new Exception(mensagem);
    	}
    	
    	if(Uteis.arrendondarForcando2CadasDecimais(valorRemanejar) > itemSelecionadoRemanejamento.getValorDisponivel()) {
    		throw new Exception("O TOTAL VALOR REMANEJAR não pode ser maior que o valor disponível.");
    	}
    	if (executarDistribuicao) {
    		itemRemanejar.setDetalhamentoPeriodoOrcamentoVOs(getFacadeFactory().getDetalhamentoPeriodoOrcamentoFacade().executarDistribuicaoValorItemMensal( itemRemanejar.getValorBaseUtilizar(), solicitacaoOrcamentoPlanoOrcamentarioVO.getPlanoOrcamentario().getDataInicio(), solicitacaoOrcamentoPlanoOrcamentarioVO.getPlanoOrcamentario().getDataFinal()));
    	}
    }

	@Override
	public void gravarItemSolicitacao(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO itemSolicitacaoOrcamentoPlanoOrcamentarioVO,
			SolicitacaoOrcamentoPlanoOrcamentarioVO solicitacaoOrcamentoPlanoOrcamentarioVO, UsuarioVO usuarioVO, boolean realizarCalculoValorAprovado) throws Exception {
		if (itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getValorAutorizado().doubleValue() < 0.0) {
			throw new ConsistirException("A Valor para aprovar não pode ser negativo.");
		}
		BigDecimal valorAprovado = BigDecimal.ZERO;
		BigDecimal valorAnterior = BigDecimal.ZERO;
		for (ItemSolicitacaoOrcamentoPlanoOrcamentarioVO item :solicitacaoOrcamentoPlanoOrcamentarioVO.getItemSolicitacaoOrcamentoPlanoOrcamentarioVOs()) {
			if (item.getCodigo().equals(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getCodigo())) {
				valorAnterior = SerializationUtils.clone(item.getValorAutorizado());
			}
			valorAprovado = valorAprovado.add(item.getValorAutorizado());
		}

		getFacadeFactory().getItemSolicitacaoOrcamentoPlanoOrcamentarioFacade().persistir(itemSolicitacaoOrcamentoPlanoOrcamentarioVO, usuarioVO);

		solicitacaoOrcamentoPlanoOrcamentarioVO.setValorTotalAprovado(valorAprovado.doubleValue());
		getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().alterar(solicitacaoOrcamentoPlanoOrcamentarioVO, usuarioVO);
		
		getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().realizarCalculoValorAprovadoPorDetalhamentoPeriodo(solicitacaoOrcamentoPlanoOrcamentarioVO);
		getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().realizarCriacaoDetalhamentoPeriodoGeral(solicitacaoOrcamentoPlanoOrcamentarioVO);
		getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().alterar(solicitacaoOrcamentoPlanoOrcamentarioVO, usuarioVO);
		if (realizarCalculoValorAprovado) {
			getFacadeFactory().getSolicitacaoOrcamentoPlanoOrcamentarioFacade().realizarCalculoValorAprovadoPorCategoriaDespesa(solicitacaoOrcamentoPlanoOrcamentarioVO);
		}
		
		HistoricoRemanejamentoPlanoOrcamentarioVO historico = new HistoricoRemanejamentoPlanoOrcamentarioVO();
		historico.setUsuarioVO(usuarioVO);
		historico.setCategoriaDespesaRemanejado(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getCategoriaDespesa());
		historico.setCategoriaDespesaVO(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getCategoriaDespesa());
		historico.setMotivo("Remanejamento via perfil de acesso que permitir alterar valor aprovado.");
		historico.setValor(valorAnterior);
		historico.setValorRemanejado(valorAprovado);
		historico.setItemSolicitacaoOrcamentoPlanoOrcamentarioVO(itemSolicitacaoOrcamentoPlanoOrcamentarioVO);
		historico.setSolicitacaoOrcamentoPlanoOrcamentarioVO(itemSolicitacaoOrcamentoPlanoOrcamentarioVO.getSolicitacaoOrcamentoPlanoOrcamentario());
		
		getFacadeFactory().getHistoricoRemanejamentoPlanoOrcamentarioInterfaceFacade().persistir(historico, false, usuarioVO);
		
	}
}
