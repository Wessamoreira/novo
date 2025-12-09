package negocio.facade.jdbc.planoorcamentario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
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

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.planoorcamentario.DetalhamentoPeriodoOrcamentoVO;
import negocio.comuns.planoorcamentario.DetalhamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.ItemMensalDetalhamentoPlanoOrcamentarioVO;
import negocio.comuns.planoorcamentario.ItemSolicitacaoOrcamentoPlanoOrcamentarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.planoorcamentario.ItemMensalDetalhamentoPlanoOrcamentoInterfaceFacade;

/**
 *
 * @author Carlos
 */
@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class ItemMensalDetalhamentoPlanoOrcamentario extends ControleAcesso implements ItemMensalDetalhamentoPlanoOrcamentoInterfaceFacade {

	private static final long serialVersionUID = 6500152400738937888L;

	private static String idEntidade;

    public ItemMensalDetalhamentoPlanoOrcamentario() {
        super();
        setIdEntidade("ItemMensalDetalhamentoPlanoOrcamentario");
    }

    @SuppressWarnings("rawtypes")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ItemMensalDetalhamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj);
        final String sql = "INSERT INTO ItemMensalDetalhamentoPlanoOrcamentario( mes, valor, detalhamentoPlanoOrcamentario, valorConsumidoMes) VALUES ( ?, ?, ?, ? ) returning codigo"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);

                sqlInserir.setString(1, obj.getMes());
                sqlInserir.setDouble(2, obj.getValor());
                if (obj.getDetalhamentoPlanoOrcamentarioVO().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(3, obj.getDetalhamentoPlanoOrcamentarioVO().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(3, 0);
                }
                sqlInserir.setDouble(4, obj.getValorConsumidoMes());
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
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ItemMensalDetalhamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj);
        final String sql = "UPDATE ItemMensalDetalhamentoPlanoOrcamentario set mes=?, valor=?, detalhamentoPlanoOrcamentario=?, valorConsumidoMes=? WHERE (codigo = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setString(1, obj.getMes());
                sqlAlterar.setDouble(2, obj.getValor());
                if (obj.getDetalhamentoPlanoOrcamentarioVO().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(3, obj.getDetalhamentoPlanoOrcamentarioVO().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(3, 0);
                }
                sqlAlterar.setDouble(4, obj.getValorConsumidoMes());
                sqlAlterar.setInt(5, obj.getCodigo().intValue());
                return sqlAlterar;
            }
        });
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(ItemSolicitacaoOrcamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        ItemSolicitacaoOrcamentoPlanoOrcamentario.excluir(getIdEntidade());
        String sql = "DELETE FROM ItemMensalDetalhamentoPlanoOrcamentario WHERE ((codigo = ?))"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    public List<ItemMensalDetalhamentoPlanoOrcamentarioVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ItemMensalDetalhamentoPlanoOrcamentario WHERE codigo >= " + valorConsulta.intValue() + " ORDER BY codigo";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr);
        return (montarDadosConsulta(tabelaResultado, nivelMontarDados, usuario));
    }

    public List<ItemMensalDetalhamentoPlanoOrcamentarioVO> consultarItemMensalDetalhamentoPorDetalhamentoPlanoOrcamentario(Integer detalhamentoPlanoOrcamentario, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        List<ItemMensalDetalhamentoPlanoOrcamentarioVO> objetos = new ArrayList<>(0);
        String sql = "SELECT * FROM ItemMensalDetalhamentoPlanoOrcamentario WHERE detalhamentoPlanoOrcamentario = ? order by codigo";
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{detalhamentoPlanoOrcamentario});
        while (resultado.next()) {
            objetos.add(montarDados(resultado, Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
        }
        return objetos;
    }

    public static List<ItemMensalDetalhamentoPlanoOrcamentarioVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        List<ItemMensalDetalhamentoPlanoOrcamentarioVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    public static ItemMensalDetalhamentoPlanoOrcamentarioVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) throws Exception {
        ItemMensalDetalhamentoPlanoOrcamentarioVO obj = new ItemMensalDetalhamentoPlanoOrcamentarioVO();
        obj.setCodigo(new Integer(dadosSQL.getInt("codigo")));
        obj.setMes(dadosSQL.getString("mes"));
        obj.setValor(dadosSQL.getDouble("valor"));
        obj.setValorConsumidoMes(dadosSQL.getDouble("valorConsumidoMes"));
        obj.getDetalhamentoPlanoOrcamentarioVO().setCodigo(dadosSQL.getInt("detalhamentoPlanoOrcamentario"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }
        montarDadosDetalhamentoPlanoOrcamentario(obj, usuario);
        return obj;
    }

    public void validarDados(ItemMensalDetalhamentoPlanoOrcamentarioVO obj) {
    }

    public static void montarDadosDetalhamentoPlanoOrcamentario(ItemMensalDetalhamentoPlanoOrcamentarioVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getDetalhamentoPlanoOrcamentarioVO().getCodigo().intValue() == 0) {
            obj.setDetalhamentoPlanoOrcamentarioVO(new DetalhamentoPlanoOrcamentarioVO());
            return;
        }
        obj.setDetalhamentoPlanoOrcamentarioVO(getFacadeFactory().getDetalhamentoPlanoOrcamentarioFacade().consultarPorChavePrimaria(obj.getDetalhamentoPlanoOrcamentarioVO().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, usuario));
    }

    public static String getIdEntidade() {
        return idEntidade;
    }

    public static void setIdEntidade(String aIdEntidade) {
        idEntidade = aIdEntidade;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirItemMensalDetalhamentoPlanoOrcamentario(Integer detalhamentoPlano, List<ItemMensalDetalhamentoPlanoOrcamentarioVO> objetos, UsuarioVO usuario) throws Exception {
        Iterator<ItemMensalDetalhamentoPlanoOrcamentarioVO> e = objetos.iterator();
        while (e.hasNext()) {
            ItemMensalDetalhamentoPlanoOrcamentarioVO obj = (ItemMensalDetalhamentoPlanoOrcamentarioVO) e.next();
            obj.getDetalhamentoPlanoOrcamentarioVO().setCodigo(detalhamentoPlano);
            incluir(obj, usuario);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarItemMensalDetalhamentoPlanoOrcamentario(Integer detalhamentoPlano, List<ItemMensalDetalhamentoPlanoOrcamentarioVO> objetos, UsuarioVO usuario) throws Exception {
        excluirItemMensalDetalhamentoPlanoOrcamentario(detalhamentoPlano, usuario);
        incluirItemMensalDetalhamentoPlanoOrcamentario(detalhamentoPlano, objetos, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirItemMensalDetalhamentoPlanoOrcamentario(Integer detalhamentoPlano, UsuarioVO usuario) throws Exception {
        DetalhamentoPlanoOrcamentario.excluir(getIdEntidade());
        String sql = "DELETE FROM ItemMensalDetalhamentoPlanoOrcamentario WHERE (detalhamentoPlanoOrcamentario = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{detalhamentoPlano});
    }

    /**
     * Método responsável por montar a lista de meses dependendo do Período do Plano Orcamentario e calcular o valor para cada mês.
     * Isso por departamento.
     * @param obj
     * @param dataInicio
     * @param dataFim
     * @return
     * @throws Exception
     */
    public List<ItemMensalDetalhamentoPlanoOrcamentarioVO> executarDistribuicaoValorItemMensal(DetalhamentoPlanoOrcamentarioVO obj, Date dataInicio, Date dataFim) throws Exception {
        List<ItemMensalDetalhamentoPlanoOrcamentarioVO> listaItemMensalDetalhamentoPlanoOrcamentarioVOs = new ArrayList<ItemMensalDetalhamentoPlanoOrcamentarioVO>(0);
        Integer quantidadeMeses = Uteis.obterQuantidadeMesesEntreDatas(dataInicio, dataFim).intValue();
        Double valorDistribuido = Uteis.arrendondarForcando2CadasDecimais(calcularValorDistribuicaoPeriodo(obj, dataInicio, dataFim, quantidadeMeses));
        Boolean somar = verificarSomarResto(obj.getOrcamentoTotalDepartamento(), valorDistribuido, quantidadeMeses);
        Double restoValorQuebrado = calcularRestoValorQuebrado(obj.getOrcamentoTotalDepartamento(), valorDistribuido, quantidadeMeses, somar);
        Boolean primeiraVez = Boolean.TRUE;

        while (dataInicio.before(dataFim) || dataInicio.equals(dataFim)) {
            ItemMensalDetalhamentoPlanoOrcamentarioVO itemMensalDetalhamentoPlanoOrcamentarioVO = new ItemMensalDetalhamentoPlanoOrcamentarioVO();

            String mes = Uteis.getMesReferenciaExtenso(String.valueOf(Uteis.getMesData(dataInicio)));
            itemMensalDetalhamentoPlanoOrcamentarioVO.setMes(mes.toUpperCase() + " - " + Uteis.getAnoData(dataInicio));
            if (primeiraVez && restoValorQuebrado != 0) {
                if (somar) {
                    itemMensalDetalhamentoPlanoOrcamentarioVO.setValor(valorDistribuido + restoValorQuebrado);
                } else {
                    itemMensalDetalhamentoPlanoOrcamentarioVO.setValor(valorDistribuido - restoValorQuebrado);
                }
            } else {
                itemMensalDetalhamentoPlanoOrcamentarioVO.setValor(valorDistribuido);
            }

            dataInicio = Uteis.getDataFutura(dataInicio, GregorianCalendar.MONTH, 1);

            listaItemMensalDetalhamentoPlanoOrcamentarioVOs.add(itemMensalDetalhamentoPlanoOrcamentarioVO);

            primeiraVez = Boolean.FALSE;
        }
        return listaItemMensalDetalhamentoPlanoOrcamentarioVOs;
    }

    public Double calcularValorDistribuicaoPeriodo(DetalhamentoPlanoOrcamentarioVO obj, Date dataInicio, Date dataFim, Integer quantidadeMeses) {
        return obj.getOrcamentoTotalDepartamento() / quantidadeMeses;
    }

    public Double calcularRestoValorQuebrado(Double orcamentoTotalPrevisto, Double valorDistribuido, Integer quantidadeMeses, Boolean somar) {
        Double resto = 0.0;
        Double valorMultiplicadoMes = Uteis.arrendondarForcando2CadasDecimais(valorDistribuido * quantidadeMeses);
        if (orcamentoTotalPrevisto > valorMultiplicadoMes) {
            somar = Boolean.TRUE;
            resto = orcamentoTotalPrevisto - valorMultiplicadoMes;
            return resto;
        } else if (orcamentoTotalPrevisto < valorMultiplicadoMes) {
            somar = Boolean.FALSE;
            resto = valorMultiplicadoMes - orcamentoTotalPrevisto;
            return Uteis.arrendondarForcando2CadasDecimais(resto);
        }
        return 0.0;
    }

    public Boolean verificarSomarResto(Double orcamentoTotalPrevisto, Double valorDistribuido, Integer quantidadeMeses) {
        Double valorMultiplicadoMes = Uteis.arrendondarForcando2CadasDecimais(valorDistribuido * quantidadeMeses);
        if (orcamentoTotalPrevisto > valorMultiplicadoMes) {
            return Boolean.TRUE;
        } else if (orcamentoTotalPrevisto < valorMultiplicadoMes) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    /**
     * Validar Dados responsável por verificar apenas a lista mensal por Departamento
     * @param valorDetalhamento
     * @param listaItemMensal
     * @param listaDetalhamentoPlanoOrcamentarioVOs
     * @param listaDetalhamentoPeriodoOrcamentoVOs
     * @throws Exception
     */
    public void validarDadosValorItemMensalDetalhamentoPlanoOrcamentario(Double valorDetalhamento, List<ItemMensalDetalhamentoPlanoOrcamentarioVO> listaItemMensal, List<DetalhamentoPlanoOrcamentarioVO> listaDetalhamentoPlanoOrcamentarioVOs, List<DetalhamentoPeriodoOrcamentoVO> listaDetalhamentoPeriodoOrcamentoVOs) throws Exception {
        Double valorFinal = 0.0;
        for (ItemMensalDetalhamentoPlanoOrcamentarioVO itemMensal : listaItemMensal) {
            valorFinal = valorFinal + itemMensal.getValor();
        }
        DecimalFormat df = new DecimalFormat("###,###,##0.00");
        if (Uteis.arrendondarForcando2CadasDecimais(valorFinal) > Uteis.arrendondarForcando2CadasDecimais(valorDetalhamento)) {
            throw new Exception("A soma dos valores mensais informado (R$ " + df.format(valorFinal) + "), está superior ao valor do Orçamento requerido pelo Gestor (R$ " + df.format(valorDetalhamento) + "). ");
        }
        if (Uteis.arrendondarForcando2CadasDecimais(valorFinal) < Uteis.arrendondarForcando2CadasDecimais(valorDetalhamento)) {
            throw new Exception("A soma dos valores mensais informado (R$ " + df.format(valorFinal) + "), está inferior ao valor do Orçamento requerido pelo Gestor (R$ " + df.format(valorDetalhamento) + ").");
        }

        validarDadosValorPorDepartamentoPlanoOrcamentario(listaDetalhamentoPlanoOrcamentarioVOs, listaDetalhamentoPeriodoOrcamentoVOs);
    }

    /**
     * 
     * @param listaDetalhamentoPlanoOrcamentarioVOs
     * @param listaDetalhamentoPeriodoOrcamentoVOs
     * @throws Exception
     */
    public void validarDadosValorPorDepartamentoPlanoOrcamentario(List<DetalhamentoPlanoOrcamentarioVO> listaDetalhamentoPlanoOrcamentarioVOs, List<DetalhamentoPeriodoOrcamentoVO> listaDetalhamentoPeriodoOrcamentoVOs) throws Exception {
        Map<String, Double> map = new HashMap<String, Double>();
        Boolean primeiraVez = Boolean.TRUE;
        for (DetalhamentoPlanoOrcamentarioVO detalhamentoPlanoOrcamentarioVO : listaDetalhamentoPlanoOrcamentarioVOs) {

            for (ItemMensalDetalhamentoPlanoOrcamentarioVO itemMensal : detalhamentoPlanoOrcamentarioVO.getListaItemMensalDetalhamentoPlanoOrcamentarioVOs()) {
                if (primeiraVez) {
                    map.put(itemMensal.getMes().toUpperCase(), itemMensal.getValor());
                } else {
                	if (Uteis.isAtributoPreenchido(map.get(itemMensal.getMes().toUpperCase()))) {
                		map.put(itemMensal.getMes().toUpperCase(), map.get(itemMensal.getMes().toUpperCase()) + itemMensal.getValor());
                	}
                }
            }
            primeiraVez = Boolean.FALSE;
        }

        validarDadosValorMensalComValorMensalTotalPrevisto(map, listaDetalhamentoPeriodoOrcamentoVOs);

    }

    public void validarDadosValorMensalComValorMensalTotalPrevisto(Map<String, Double> map, List<DetalhamentoPeriodoOrcamentoVO> listaDetalhamentoPeriodoOrcamentoVOs) throws Exception {
        DecimalFormat df = new DecimalFormat("###,###,##0.00");
        for (DetalhamentoPeriodoOrcamentoVO detalhamentoPeriodoOrcamentoVO : listaDetalhamentoPeriodoOrcamentoVOs) {
            if (map.containsKey(detalhamentoPeriodoOrcamentoVO.getMes().name())) {
				Double valorMensalDepartamento = map.get(detalhamentoPeriodoOrcamentoVO.getMes().name());

                if (valorMensalDepartamento > detalhamentoPeriodoOrcamentoVO.getOrcamentoTotal()) {
                    throw new Exception("Valor ultrapassado permitido para o mês de " + detalhamentoPeriodoOrcamentoVO.getMes() + ". Valor permitido = R$ " + df.format(detalhamentoPeriodoOrcamentoVO.getOrcamentoTotal()) + ". Soma Mensal = R$ " + df.format(valorMensalDepartamento) + "");
                }
            }
        }
    }

    public Double consultarTotalMesAutorizacaoRequisicao(Date data, Integer departamento, Integer unidadeEnsino, String mes) {
        StringBuilder sb = new StringBuilder();
        sb.append("select itemMensal.valor from planoorcamentario ");
        sb.append(" inner join detalhamentoplanoorcamentario detalhamentoPlano on detalhamentoPlano.planoOrcamentario = planoOrcamentario.codigo ");
        sb.append(" inner join itemmensaldetalhamentoplanoorcamentario itemMensal on itemMensal.detalhamentoplanoorcamentario = detalhamentoplano.codigo ");
        sb.append(" where '");
        sb.append(data);
        sb.append("' >= dataInicio and '");
        sb.append(data);
        sb.append("' <= dataFinal ");
        sb.append(" and detalhamentoPlano.departamento = ");
        sb.append(departamento);
        sb.append(" and detalhamentoPlano.unidadeEnsino = ");
        sb.append(unidadeEnsino);
        sb.append(" and itemMensal.mes = '");
        sb.append(mes);
        sb.append("'");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (tabelaResultado.next()) {
            return tabelaResultado.getDouble("valor");
        }
        return 0.00;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarValorConsumidoMes(Double valorTotalProdutos, Integer departamento, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        String mes = iniciliazarDadosMesItemMensalData(new Date());
        Double valorTotal = 0.0;
        ItemMensalDetalhamentoPlanoOrcamentarioVO itemMensal = consultarItemMensalPorDepartamentoUnidadeEnsinoMesEDataPlanoOrcamentario(mes, new Date(), departamento, unidadeEnsino, usuario);
        if (Uteis.isAtributoPreenchido(itemMensal)) {
            valorTotal = itemMensal.getValorConsumidoMes() + valorTotalProdutos;
            alterarValorConsumidoMes(valorTotal, itemMensal.getCodigo());
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void atualizarEstornoValorConsumidoMes(Double valorTotalProdutos, Integer departamento, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
    	String mes = iniciliazarDadosMesItemMensalData(new Date());
    	Double valorTotal = 0.0;
    	ItemMensalDetalhamentoPlanoOrcamentarioVO itemMensal = consultarItemMensalPorDepartamentoUnidadeEnsinoMesEDataPlanoOrcamentario(mes, new Date(), departamento, unidadeEnsino, usuario);
    	if (Uteis.isAtributoPreenchido(itemMensal)) {
    		valorTotal = itemMensal.getValorConsumidoMes() - valorTotalProdutos;
    		alterarValorConsumidoMes(valorTotal, itemMensal.getCodigo());
    	}
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public String iniciliazarDadosMesItemMensalData(Date data) {
        String mes = Uteis.getMesReferenciaExtenso(String.valueOf(Uteis.getMesData(data)));
        int ano = Uteis.getAnoData(data);
        return mes.toUpperCase() + " - " + ano;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public ItemMensalDetalhamentoPlanoOrcamentarioVO consultarItemMensalPorDepartamentoUnidadeEnsinoMesEDataPlanoOrcamentario(String mes, Date data, Integer departamento, Integer unidadeEnsino, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select itemmensal.* from itemmensaldetalhamentoplanoorcamentario itemmensal ");
        sb.append(" inner join detalhamentoplanoorcamentario detalhamento on detalhamento.codigo = itemmensal.detalhamentoplanoorcamentario ");
        sb.append(" inner join planoorcamentario on planoorcamentario.codigo = detalhamento.planoorcamentario ");
        sb.append(" where '");
        sb.append(data);
        sb.append("' >= planoorcamentario.datainicio and '");
        sb.append(data);
        sb.append("' <= planoorcamentario.datafinal ");
        if (Uteis.isAtributoPreenchido(departamento)) {
        	sb.append(" and detalhamento.departamento = ");
        	sb.append(departamento);
        }
        sb.append(" and detalhamento.unidadeensino = ");
        sb.append(unidadeEnsino);
        sb.append(" and itemmensal.mes = '");
        sb.append(mes);
        sb.append("'");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (!tabelaResultado.next()) {
            return null;
        }
        return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarValorConsumidoMes(final Double valorConsumidoMes, final Integer codigo) throws Exception {

        final String sql = "UPDATE itemmensaldetalhamentoplanoorcamentario set valorConsumidoMes=? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setDouble(1, valorConsumidoMes);
                sqlAlterar.setInt(2, codigo.intValue());
                return sqlAlterar;
            }
        });
    }

    public ItemMensalDetalhamentoPlanoOrcamentarioVO consultarPorDetalhamentoPlanoOrcamentarioMes(Integer detalhamentoPlano, String mes, UsuarioVO usuario) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("select * from itemmensaldetalhamentoplanoorcamentario ");
        sb.append(" where detalhamentoplanoorcamentario = ");
        sb.append(detalhamentoPlano);
        sb.append(" and mes = '");
        sb.append(mes);
        sb.append("'");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (!tabelaResultado.next()) {
            return null;
        }
        return montarDados(tabelaResultado, Uteis.NIVELMONTARDADOS_TODOS, usuario);
    }
}
