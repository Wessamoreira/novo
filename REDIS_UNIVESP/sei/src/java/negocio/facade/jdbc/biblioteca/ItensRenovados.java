package negocio.facade.jdbc.biblioteca;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.biblioteca.EmprestimoVO;
import negocio.comuns.biblioteca.ExemplarVO;
import negocio.comuns.biblioteca.ItemEmprestimoVO;
import negocio.comuns.biblioteca.ItensRenovadosVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.biblioteca.ItensRenovadosInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe
 * <code>ItensRenovadosVO</code>. Responsável por implementar operações como incluir, alterar, excluir e consultar
 * pertinentes a classe <code>ItensRenovadosVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see ItensRenovadosVO
 * @see ControleAcesso
 * @see Titulo
 */
@Repository
@Scope("singleton")
@Lazy
public class ItensRenovados extends ControleAcesso implements ItensRenovadosInterfaceFacade {

    protected static String idEntidade;

    public ItensRenovados() throws Exception {
        super();
        setIdEntidade("Titulo");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>ItensRenovadosVO</code>.
     */
    public ItensRenovadosVO novo() throws Exception {
        ItensRenovados.incluir(getIdEntidade());
        ItensRenovadosVO obj = new ItensRenovadosVO();
        return obj;
    }

    /**
     * Calcula o número de ocorrências de um determinado Exemplar para uma determinada Pessoa no banco de dados.
     *
     * @param emprestimoVO
     * @param itemEmprestimoVO
     * @return
     * @throws Exception
     */
    public Integer calcularNrRenovacoesExemplar(EmprestimoVO emprestimoVO, ItemEmprestimoVO itemEmprestimoVO) throws Exception {
        String sqlStr = "SELECT nrRenovacao FROM itensRenovados WHERE pessoa = " + emprestimoVO.getPessoa().getCodigo()
                + " AND exemplar = " + itemEmprestimoVO.getExemplar().getCodigo() + " ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (!tabelaResultado.next()) {
            return 0;
        }
        return (new Integer(tabelaResultado.getInt(1)));
    }
    
    public Boolean executarValidarExistenciaItensRenovados(EmprestimoVO emprestimoVO, ItemEmprestimoVO itemEmprestimoVO) throws Exception {
        String sqlStr = "SELECT * FROM itensRenovados WHERE pessoa = " + emprestimoVO.getPessoa().getCodigo()
                + " AND exemplar = " + itemEmprestimoVO.getExemplar().getCodigo() + " ";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        if (tabelaResultado.next()) {
            return true;
        }
        return false;
    }

    /**
     * Monta o Objeto de ItensRenovados para gravar no Banco de dados. Se for um emprestimo novo, seta o nrRenovacao
     * para 0. Se for uma renovação, o método consulta no banco a existência de algum registro de um detemrinado
     * exemplar para uma determinada pessoa, calcula o numero de ocorrências e seta o nrRenovacao de acordo com essa
     * consulta.
     *
     * @param emprestimoVO
     * @param renovacao
     * @throws Exception
     */
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
	public void montarItensRenovacao(EmprestimoVO emprestimoVO, Boolean renovacao) throws Exception {
		ItensRenovadosVO itensRenovacaoVO = new ItensRenovadosVO();
		int nrRenovacoesExemplar;
		for (ItemEmprestimoVO itemEmprestimoVO : emprestimoVO.getItemEmprestimoVOs()) {
			// if (!itemEmprestimoVO.getAlterado()) {
			itensRenovacaoVO.setExemplar(itemEmprestimoVO.getExemplar());
			itensRenovacaoVO.setPessoa(emprestimoVO.getPessoa());
			nrRenovacoesExemplar = calcularNrRenovacoesExemplar(emprestimoVO, itemEmprestimoVO);
			if (itemEmprestimoVO.getRenovarEmprestimo()) {
				itensRenovacaoVO.setNrRenovacao(nrRenovacoesExemplar + 1);
				atualizarNrRenovacoes(itensRenovacaoVO);
				itemEmprestimoVO.setAlterado(true);
			} else {
				itensRenovacaoVO.setNrRenovacao(0);
				if (nrRenovacoesExemplar > 0) {
					atualizarNrRenovacoes(itensRenovacaoVO);
				} else {
					if (!executarValidarExistenciaItensRenovados(emprestimoVO, itemEmprestimoVO)) {
						incluir(itensRenovacaoVO);
					}
				}
				itemEmprestimoVO.setAlterado(true);
			}
			// }
		}
	}

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarNrRenovacoes(ItensRenovadosVO itensRenovadosVO) throws Exception {
        try {
            String sql = "UPDATE itensRenovados SET nrRenovacao = ? WHERE exemplar = ? AND pessoa = ? ";
            if(getConexao().getJdbcTemplate().update(
                    sql,
                    new Object[]{itensRenovadosVO.getNrRenovacao(), itensRenovadosVO.getExemplar().getCodigo(),
                        itensRenovadosVO.getPessoa().getCodigo()})==0){
            	incluir(itensRenovadosVO);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ItensRenovadosVO</code>.
     * Primeiramente valida os dados ( <code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a
     * permissão do usuário para realizar esta operacão na entidade. Isto, através da operação <code>incluir</code> da
     * superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ItensRenovadosVO</code> que será gravado no banco de dados.
     * @exception Exception
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ItensRenovadosVO obj) throws Exception {
        try {

            ItensRenovadosVO.validarDados(obj);
            /**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
            // ItensRenovados.incluir(getIdEntidade());
            final String sql = "INSERT INTO ItensRenovados ( exemplar, pessoa, nrRenovacao) VALUES ( ?, ?, ? ) ";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    if (obj.getExemplar().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(1, obj.getExemplar().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(1, 0);
                    }
                    if (obj.getPessoa().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getPessoa().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    sqlInserir.setInt(3, obj.getNrRenovacao());
                    return sqlInserir;
                }
            });

            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            obj.setNovoObj(true);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirItensRenovados(EmprestimoVO emprestimoVO, ItemEmprestimoVO itemEmprestimoVO) throws Exception {
        ItensRenovadosVO itensRenovadosVO = new ItensRenovadosVO();
        itensRenovadosVO.setExemplar(itemEmprestimoVO.getExemplar());
        itensRenovadosVO.setPessoa(emprestimoVO.getPessoa());
        excluir(itensRenovadosVO);
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ItensRenovadosVO</code>. Sempre localiza o
     * registro a ser excluído através da chave primária da entidade. Primeiramente verifica a conexão com o banco de
     * dados e a permissão do usuário para realizar esta operacão na entidade. Isto, através da operação
     * <code>excluir</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>ItensRenovadosVO</code> que será removido no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão ou restrição de acesso.
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(final ItensRenovadosVO obj) throws Exception {
        try {
        	/**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
            // ItensRenovados.excluir(getIdEntidade());
            final String sql = "DELETE FROM ItensRenovados WHERE ((exemplar = ?) AND (pessoa=?))";
            getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getExemplar().getCodigo(), obj.getPessoa().getCodigo()});
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (
     * <code>ResultSet</code>). Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por
     * vez.
     *
     * @return List Contendo vários objetos da classe <code>ItensRenovadosVO</code> resultantes da consulta.
     */
    public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>) em um
     * objeto da classe <code>ItensRenovadosVO</code>.
     *
     * @return O objeto da classe <code>ItensRenovadosVO</code> com os dados devidamente montados.
     */
    public static ItensRenovadosVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ItensRenovadosVO obj = new ItensRenovadosVO();
        obj.getExemplar().setCodigo(dadosSQL.getInt("exemplar"));
        obj.getPessoa().setCodigo(dadosSQL.getInt("pessoa"));
        obj.setNrRenovacao(dadosSQL.getInt("nrRenovacao"));
        obj.setNovoObj(Boolean.FALSE);
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosExemplar(obj, nivelMontarDados, usuario);
        montarDadosPessoa(obj, nivelMontarDados, usuario);
        return obj;
    }

    public static void montarDadosPessoa(ItensRenovadosVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getPessoa().getCodigo().intValue() == 0) {
            obj.setPessoa(new PessoaVO());
            return;
        }
        obj.setPessoa(getFacadeFactory().getPessoaFacade().consultaRapidaPorChavePrimaria(obj.getPessoa().getCodigo(), false, nivelMontarDados, usuario));
    }

    public static void montarDadosExemplar(ItensRenovadosVO obj, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        if (obj.getExemplar().getCodigo().intValue() == 0) {
            obj.setExemplar(new ExemplarVO());
            return;
        }
        obj.setExemplar(getFacadeFactory().getExemplarFacade().consultarPorChavePrimaria(obj.getExemplar().getCodigo(), nivelMontarDados, usuario));
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ItensRenovadosVO</code> através de sua chave
     * primária.
     *
     * @exception Exception
     *                Caso haja problemas de conexão ou localização do objeto procurado.
     */
    public ItensRenovadosVO consultarPorChavePrimaria(Integer exemplarPrm, Integer pessoaPrm, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ItensRenovados WHERE exemplar = ? AND pessoa = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{exemplarPrm, pessoaPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, nivelMontarDados, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este identificar é utilizado para verificar as
     * permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return ItensRenovados.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe. Esta alteração deve ser
     * possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos distintos. Assim ao se verificar que
     * Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        ItensRenovados.idEntidade = idEntidade;
    }
    
    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirPorExemplar(final Integer exemplar, UsuarioVO usuarioVO) throws Exception {
        try {
        	/**
    		 * @author Leonardo Riciolle 
    		 * Comentado 28/10/2014
    		 *  Classe Subordinada
    		 */
            // ItensRenovados.excluir(getIdEntidade());
            final String sql = "DELETE FROM ItensRenovados WHERE ((exemplar = ?)) "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuarioVO);
            getConexao().getJdbcTemplate().update(sql, exemplar);
        } catch (Exception e) {
            throw e;
        }
    }

}
