package negocio.facade.jdbc.compras;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.compras.MovimentacaoEstoqueVO;
import negocio.comuns.compras.OperacaoEstoqueVO;
import negocio.comuns.compras.ProdutoServicoVO;
import negocio.comuns.compras.enumeradores.OperacaoEstoqueEnum;
import negocio.comuns.compras.enumeradores.TipoOperacaoEstoqueOrigemEnum;
import negocio.comuns.financeiro.CentroResultadoOrigemVO;
import negocio.comuns.financeiro.CentroResultadoVO;
import negocio.comuns.financeiro.enumerador.TipoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoMovimentacaoCentroResultadoOrigemEnum;
import negocio.comuns.financeiro.enumerador.TipoNivelCentroResultadoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.compras.MovimentacaoEstoqueInterfaceFacade;


@Repository
@Scope("singleton")
@Lazy 
public class MovimentacaoEstoque extends ControleAcesso implements MovimentacaoEstoqueInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = -2550826488954575786L;
	protected static String idEntidade;

    public MovimentacaoEstoque() {
        
        setIdEntidade("MovimentacaoEstoque");
    }

     
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)    
    public void incluir(final MovimentacaoEstoqueVO obj,UsuarioVO usuario) throws Exception {
        try {
            MovimentacaoEstoqueVO.validarDados(obj);
            MovimentacaoEstoque.incluir(getIdEntidade(), true, usuario);
            obj.realizarUpperCaseDados();
            final String sql = "INSERT INTO MovimentacaoEstoque( data, responsavel, tipoMovimentacao, produto, unidadeEnsino,  quantidade, motivo, precoUnitario,  unidadeEnsinodestino, centroresultadoestoque, centroresultadoestoquedestino) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )returning codigo";
            obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                    sqlInserir.setTimestamp(1, Uteis.getDataJDBCTimestamp(obj.getData()));
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(2, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(2, 0);
                    }
                    sqlInserir.setString(3, obj.getTipoMovimentacao());
                    if (obj.getProduto().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(4, obj.getProduto().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(4, 0);
                    }
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlInserir.setInt(5, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlInserir.setNull(5, 0);
                    }
                    sqlInserir.setDouble(6, obj.getQuantidade().doubleValue());
                    sqlInserir.setString(7, obj.getMotivo());
                    sqlInserir.setDouble(8, obj.getPrecoUnitario());
                    int i = 8;
                    Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoDestino(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getCentroResultadoEstoque(), ++i, sqlInserir);
                    Uteis.setValuePreparedStatement(obj.getCentroResultadoEstoqueDestino(), ++i, sqlInserir);
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Object>() {
                public Object extractData(ResultSet arg0) throws SQLException {
                    if (arg0.next()) {
                        obj.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));

            atualizarMovimentacaoEstoque(obj,usuario);
            alterarValorUltimaCompraProdutoServico(obj,usuario);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
        	obj.setCodigo(0);
            obj.setNovoObj(Boolean.TRUE);
            throw e;
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarMovimentacaoEstoque(MovimentacaoEstoqueVO obj,UsuarioVO usuario) throws Exception {
    	List<OperacaoEstoqueVO> listaOperacaoEstoque = null;
    	if (obj.isTipoMovimentacaoEntrada()) {
    		listaOperacaoEstoque = Estoque.manipularEstoque(obj.getCodigo().toString(), TipoOperacaoEstoqueOrigemEnum.MOVIMENTACAO_ESTOQUE, obj.getProduto().getCodigo(), obj.getQuantidade(), obj.getPrecoUnitario(), null, obj.getUnidadeEnsino().getCodigo(), OperacaoEstoqueEnum.INCLUIR,  usuario);
    		atualizarCentroResultadoEstoque(obj, listaOperacaoEstoque, TipoMovimentacaoCentroResultadoOrigemEnum.ENTRADA, true, usuario);
    	} else if (obj.isTipoMovimentacaoSaida()) {
        	listaOperacaoEstoque = Estoque.manipularEstoque(obj.getCodigo().toString(), TipoOperacaoEstoqueOrigemEnum.MOVIMENTACAO_ESTOQUE, obj.getProduto().getCodigo(), obj.getQuantidade(), 0.0, null, obj.getUnidadeEnsino().getCodigo(), OperacaoEstoqueEnum.EXCLUIR, usuario);
        	atualizarCentroResultadoEstoque(obj, listaOperacaoEstoque, TipoMovimentacaoCentroResultadoOrigemEnum.SAIDA, true, usuario);
        } else if (obj.isTipoMovimentacaoTransferencia()) {
    		listaOperacaoEstoque = Estoque.manipularEstoque(obj.getCodigo().toString(), TipoOperacaoEstoqueOrigemEnum.MOVIMENTACAO_ESTOQUE, obj.getProduto().getCodigo(), obj.getQuantidade(), 0.0, null, obj.getUnidadeEnsino().getCodigo(), OperacaoEstoqueEnum.EXCLUIR, usuario);
    		atualizarCentroResultadoEstoque(obj, listaOperacaoEstoque, TipoMovimentacaoCentroResultadoOrigemEnum.SAIDA, true, usuario);
    		List<OperacaoEstoqueVO> listaOperacaoEstoqueInclusao = new ArrayList<>();
    		for (OperacaoEstoqueVO operacaoEstoqueVO : listaOperacaoEstoque) {
    			listaOperacaoEstoqueInclusao.addAll(Estoque.manipularEstoque(obj.getCodigo().toString(), TipoOperacaoEstoqueOrigemEnum.MOVIMENTACAO_ESTOQUE, obj.getProduto().getCodigo(), operacaoEstoqueVO.getQuantidade(), operacaoEstoqueVO.getEstoqueVO().getPrecoUnitario() , null, obj.getUnidadeEnsinoDestino().getCodigo(), OperacaoEstoqueEnum.INCLUIR, usuario));	
    		}
    		atualizarCentroResultadoEstoque(obj, listaOperacaoEstoqueInclusao, TipoMovimentacaoCentroResultadoOrigemEnum.ENTRADA, false, usuario);
    	}
    }
    
    @Transactional(readOnly = false, isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarCentroResultadoEstoque(MovimentacaoEstoqueVO obj, List<OperacaoEstoqueVO> listaOperacaoEstoque, TipoMovimentacaoCentroResultadoOrigemEnum tipoMovimentacaoCentroResultadoOrigemEnum, boolean isOrigem, UsuarioVO usuario) {
		try {
			CentroResultadoOrigemVO cro = new CentroResultadoOrigemVO();
			if (obj.getProduto().getCategoriaProduto().getCategoriaDespesa().getExigeCentroCustoRequisitante()) {
				cro.setFuncionarioCargoVO(obj.getFuncionarioCargoVO());
			}
			cro.setTipoMovimentacaoCentroResultadoOrigemEnum(tipoMovimentacaoCentroResultadoOrigemEnum);
			cro.setCentroResultadoAdministrativo(isOrigem ? obj.getCentroResultadoEstoque() : obj.getCentroResultadoEstoqueDestino());
			cro.setUnidadeEnsinoVO(isOrigem ? obj.getUnidadeEnsino() : obj.getUnidadeEnsinoDestino());
			cro.setCategoriaDespesaVO(obj.getProduto().getCategoriaProduto().getCategoriaDespesa());
			cro.setDepartamentoVO(getFacadeFactory().getDepartamentoFacade().consultarDepartamentoControlaEstoquePorCentroResultado(cro.getCentroResultadoAdministrativo().getCodigo(), Uteis.NIVELMONTARDADOS_COMBOBOX, usuario));
			cro.setPorcentagem(100.0);
			if(Uteis.isAtributoPreenchido(cro.getDepartamentoVO())){
				cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.DEPARTAMENTO);			
			}else{
				cro.setTipoNivelCentroResultadoEnum(TipoNivelCentroResultadoEnum.UNIDADE_ENSINO);
			}
			realizarCalculoCentroResultadoEstoque(cro, listaOperacaoEstoque, usuario);
			getFacadeFactory().getCentroResultadoOrigemInterfaceFacade().persistir(cro, obj.getCodigo().toString(), TipoCentroResultadoOrigemEnum.MOVIMENTACAO_ESTOQUE, false, false, usuario, false);
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}

	private void realizarCalculoCentroResultadoEstoque(CentroResultadoOrigemVO cro, List<OperacaoEstoqueVO> listaOperacaoEstoque, UsuarioVO usuario) {
		try {
			Map<Double, List<OperacaoEstoqueVO>> collect = listaOperacaoEstoque.stream().collect(Collectors.groupingBy(p -> p.getEstoqueVO().getPrecoUnitario()));
			for (Map.Entry<Double, List<OperacaoEstoqueVO>> mapaOperacaoEstoqueVO : collect.entrySet()) {
				Double quantidadePorPreco = mapaOperacaoEstoqueVO.getValue().stream().mapToDouble(OperacaoEstoqueVO::getQuantidade).reduce(0D, (a, b) -> Uteis.arrendondarForcando2CadasDecimais(a + b));
				cro.setQuantidade(Uteis.arrendondarForcando2CadasDecimais(cro.getQuantidade() + quantidadePorPreco));
				cro.setValor(Uteis.arrendondarForcando2CadasDecimais(cro.getValor() + (quantidadePorPreco * mapaOperacaoEstoqueVO.getKey())));
			}
			Uteis.checkState(!Uteis.isAtributoPreenchido(cro.getQuantidade()), "A Quantidade Total do Estoque (CentroResultadoOrigem) deve ser informado.");
			Uteis.checkState(!Uteis.isAtributoPreenchido(cro.getValor()), "O Valor Total do Estoque (CentroResultadoOrigem) deve ser informado.");
		} catch (Exception e) {
			throw new StreamSeiException(e);
		}
	}
    
    private void alterarValorUltimaCompraProdutoServico(MovimentacaoEstoqueVO obj,UsuarioVO usuario) throws Exception {
        if(obj.isTipoMovimentacaoEntrada()){
        	getFacadeFactory().getProdutoServicoFacade().atualizarValorUltimaCompraProdutoServico(obj.getPrecoUnitario(), obj.getProduto().getCodigo());	
        }
    }

   
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)    
    public void alterar(final MovimentacaoEstoqueVO obj,UsuarioVO usuarioVO) throws Exception {
        try {
            MovimentacaoEstoqueVO.validarDados(obj);
            MovimentacaoEstoque.alterar(getIdEntidade(), true, usuarioVO);
            obj.realizarUpperCaseDados();
            final String sql = "UPDATE MovimentacaoEstoque set data=?, responsavel=?, tipoMovimentacao=?, produto=?, unidadeEnsino=?, quantidade=?, motivo = ?, precoUnitario=?, unidadeensinodestino=?, centroresultadoestoque=?, centroresultadoestoquedestino=? WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setDate(1, Uteis.getDataJDBC(obj.getData()));
                    if (obj.getResponsavel().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(2, obj.getResponsavel().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(2, 0);
                    }
                    sqlAlterar.setString(3, obj.getTipoMovimentacao());
                    if (obj.getProduto().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(4, obj.getProduto().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(4, 0);
                    }
                    if (obj.getUnidadeEnsino().getCodigo().intValue() != 0) {
                        sqlAlterar.setInt(5, obj.getUnidadeEnsino().getCodigo().intValue());
                    } else {
                        sqlAlterar.setNull(5, 0);
                    }
                    sqlAlterar.setInt(6, obj.getQuantidade().intValue());
                    sqlAlterar.setString(7, obj.getMotivo());
                    sqlAlterar.setDouble(8, obj.getPrecoUnitario().doubleValue());
                    int i = 8;
                    Uteis.setValuePreparedStatement(obj.getUnidadeEnsinoDestino(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getCentroResultadoEstoque(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getCentroResultadoEstoqueDestino(), ++i, sqlAlterar);
                    Uteis.setValuePreparedStatement(obj.getCodigo(), ++i, sqlAlterar);
                    return sqlAlterar;
                }
            });
        } catch (Exception e) {
            throw e;
        }
    }    

    
    
    public List<MovimentacaoEstoqueVO> consultarPorNomeProduto(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), true,usuario);
        String sqlStr = "SELECT MovimentacaoEstoque.* FROM MovimentacaoEstoque, ProdutoServico WHERE MovimentacaoEstoque.produto = ProdutoServico.codigo and upper( ProdutoServico.nome ) like('" + valorConsulta.toUpperCase() + "%')";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT MovimentacaoEstoque.* FROM MovimentacaoEstoque, ProdutoServico WHERE MovimentacaoEstoque.produto = ProdutoServico.codigo and upper( ProdutoServico.nome ) like('" + valorConsulta.toUpperCase() + "%') and MovimentacaoEstoque.unidadeEnsino = " + unidadeEnsino.intValue();
        }
        sqlStr += " AND MovimentacaoEstoque.data >= '" + Uteis.getDataJDBC(dataIni) + "' ";
        sqlStr += " AND MovimentacaoEstoque.data <= '" + Uteis.getDataJDBC(dataFim) + "' ";
        sqlStr += " ORDER BY ProdutoServico.nome";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario);
    }

    
    
    public List<MovimentacaoEstoqueVO> consultarPorTipoMovimentacao(String valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MovimentacaoEstoque WHERE upper( tipoMovimentacao ) like('" + valorConsulta.toUpperCase() + "%')";
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT * FROM MovimentacaoEstoque WHERE upper( tipoMovimentacao ) like('" + valorConsulta.toUpperCase() + "%') and unidadeEnsino = " + unidadeEnsino.intValue();
        }
        sqlStr += " AND MovimentacaoEstoque.data >= '" + Uteis.getDataJDBC(dataIni) + "' ";
        sqlStr += " AND MovimentacaoEstoque.data <= '" + Uteis.getDataJDBC(dataFim) + "' ";
        sqlStr += " ORDER BY tipoMovimentacao";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    
    
    
    
    public List<MovimentacaoEstoqueVO> consultarPorCodigo(Integer valorConsulta, Date dataIni, Date dataFim, Integer unidadeEnsino, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM MovimentacaoEstoque WHERE codigo >= " + valorConsulta.intValue();
        if (unidadeEnsino.intValue() != 0) {
            sqlStr = "SELECT * FROM MovimentacaoEstoque WHERE codigo >= " + valorConsulta.intValue() + " and unidadeEnsino = " + unidadeEnsino.intValue();
        }
        sqlStr += " AND MovimentacaoEstoque.data >= '" + Uteis.getDataJDBC(dataIni) + "' ";
        sqlStr += " AND MovimentacaoEstoque.data <= '" + Uteis.getDataJDBC(dataFim) + "' ";
        sqlStr += " ORDER BY codigo";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados,usuario));
    }

    
    public static List<MovimentacaoEstoqueVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        List<MovimentacaoEstoqueVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados,usuario));
        }
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>MovimentacaoEstoqueVO</code>.
     * @return  O objeto da classe <code>MovimentacaoEstoqueVO</code> com os dados devidamente montados.
     */
    public static MovimentacaoEstoqueVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        MovimentacaoEstoqueVO obj = new MovimentacaoEstoqueVO();
        obj.setNovoObj(Boolean.FALSE);
        obj.setCodigo((dadosSQL.getInt("codigo")));
        obj.setData(dadosSQL.getDate("data"));
        obj.setMotivo(dadosSQL.getString("motivo"));
        obj.getResponsavel().setCodigo((dadosSQL.getInt("responsavel")));
        obj.setTipoMovimentacao(dadosSQL.getString("tipoMovimentacao"));
        obj.getProduto().setCodigo((dadosSQL.getInt("produto")));
        obj.getUnidadeEnsino().setCodigo((dadosSQL.getInt("unidadeEnsino")));
        obj.getUnidadeEnsinoDestino().setCodigo((dadosSQL.getInt("unidadeEnsinoDestino")));
        obj.setQuantidade((dadosSQL.getDouble("quantidade")));
        obj.setPrecoUnitario((dadosSQL.getDouble("precoUnitario")));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        obj.setCentroResultadoEstoque(Uteis.montarDadosVO(dadosSQL.getInt("centroResultadoEstoque"), CentroResultadoVO.class, p -> getFacadeFactory().getCentroResultadoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
        obj.setCentroResultadoEstoqueDestino(Uteis.montarDadosVO(dadosSQL.getInt("centroResultadoEstoqueDestino"), CentroResultadoVO.class, p -> getFacadeFactory().getCentroResultadoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
        obj.setUnidadeEnsinoDestino(Uteis.montarDadosVO(dadosSQL.getInt("unidadeEnsinoDestino"), UnidadeEnsinoVO.class, p -> getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(p, false, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario)));
        montarDadosResponsavel(obj, Uteis.NIVELMONTARDADOS_DADOSMINIMOS,usuario);
        montarDadosProduto(obj, nivelMontarDados,usuario);
        montarDadosUnidadeEnsino(obj, nivelMontarDados,usuario);
        return obj;
    }

   
    public static void montarDadosUnidadeEnsino(MovimentacaoEstoqueVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getUnidadeEnsino().getCodigo().intValue() == 0) {
            obj.setUnidadeEnsino(new UnidadeEnsinoVO());
            return;
        }
        obj.setUnidadeEnsino(getFacadeFactory().getUnidadeEnsinoFacade().consultarPorChavePrimaria(obj.getUnidadeEnsino().getCodigo(), false, nivelMontarDados,usuario));
    }

    
    public static void montarDadosProduto(MovimentacaoEstoqueVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getProduto().getCodigo().intValue() == 0) {
            obj.setProduto(new ProdutoServicoVO());
            return;
        }
        obj.setProduto(getFacadeFactory().getProdutoServicoFacade().consultarPorChavePrimaria(obj.getProduto().getCodigo(), false, nivelMontarDados,usuario));
    }

  
    public static void montarDadosResponsavel(MovimentacaoEstoqueVO obj, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        if (obj.getResponsavel().getCodigo().intValue() == 0) {
            obj.setResponsavel(new UsuarioVO());
            return;
        }
        obj.setResponsavel(getFacadeFactory().getUsuarioFacade().consultarPorChavePrimaria(obj.getResponsavel().getCodigo(), nivelMontarDados,usuario));
    }    
    
    public MovimentacaoEstoqueVO consultarPorChavePrimaria(Integer codigoPrm, boolean controlarAcesso, int nivelMontarDados,UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sql = "SELECT * FROM MovimentacaoEstoque WHERE codigo = ?";

        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigoPrm});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( MovimentacaoEstoque ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados,usuario));
    }


    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
     */
    public static String getIdEntidade() {
        return MovimentacaoEstoque.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
     */
    public void setIdEntidade(String idEntidade) {
        MovimentacaoEstoque.idEntidade = idEntidade;
    }
}
