package negocio.facade.jdbc.financeiro;
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
import negocio.comuns.financeiro.ItemCondicaoRenegociacaoVO;
import negocio.comuns.financeiro.enumerador.TipoFinanciamentoEnum;
import negocio.comuns.financeiro.enumerador.TipoParcelaNegociarEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.financeiro.ItemCondicaoRenegociacaoInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>ItemCondicaoRenegociacaoVO</code>.
 * Responsável por implementar operações como incluir, alterar, excluir e consultar pertinentes a classe <code>ItemCondicaoRenegociacaoVO</code>.
 * Encapsula toda a interação com o banco de dados.
 * @see ItemCondicaoRenegociacaoVO
 * @see SuperEntidade
 * @see CondicaoRenegociacao
*/

@Repository
@Scope("singleton")
@Lazy
@SuppressWarnings("unchecked")
public class ItemCondicaoRenegociacao extends ControleAcesso implements ItemCondicaoRenegociacaoInterfaceFacade {
	
	private static final long serialVersionUID = 2909984355775794522L;

	protected static String idEntidade;
	
    public ItemCondicaoRenegociacao() throws Exception {
        super();
        setIdEntidade("CondicaoRenegociacao");
    }

    /**
     * Operação responsável por incluir no banco de dados um objeto da classe <code>ItemCondicaoRenegociacaoVO</code>.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>incluir</code> da superclasse.
     * @param obj  Objeto da classe <code>ItemCondicaoRenegociacaoVO</code> que será gravado no banco de dados.
     * @exception Exception Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final ItemCondicaoRenegociacaoVO obj) throws Exception {
        validarDados(obj);

        final String sql = " INSERT INTO ItemCondicaoRenegociacao( valorInicial, valorFinal, parcelaInicial, parcelaFinal, faixaentradainicial, juro, desconto, status, condicaoRenegociacao, tipoFinanciamento, "
        		+ " tipoOrigemMatricula, tipoOrigemMensalidade, tipoOrigemBiblioteca, tipoOrigemDevolucaoCheque, tipoOrigemNegociacao, tipoOrigemBolsaCusteadaConvenio, tipoOrigemContratoReceita, "
        		+ " tipoOrigemOutros, tipoOrigemInclusaoReposicao, tipoParcelaNegociar, valorMinimoPorParcela, qtdeInicialDiasAtraso, qtdeFinalDiasAtraso, utilizarVisaoAdministrativa, utilizarVisaoAluno, qtdeDiasEntrada, gerarParcelas30DiasAposDataEntrada, "
        		+ " faixaEntradaFinal, definirNumeroDiasVencimentoPrimeiraParcela, numeroDiasAposVencimentoEntrada, isentarJuroParcela, isentarMultaParcela, isentarIndiceReajustePorAtrasoParcela, descricao )"
        		+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? ) returning codigo";
        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);                
                sqlInserir.setDouble( 1, obj.getValorInicial().doubleValue() );
                sqlInserir.setDouble( 2, obj.getValorFinal().doubleValue() );
                sqlInserir.setInt( 3, obj.getParcelaInicial().intValue() );
                sqlInserir.setInt( 4, obj.getParcelaFinal().intValue() );
                sqlInserir.setDouble( 5, obj.getFaixaEntradaInicial());
                sqlInserir.setDouble( 6, obj.getJuro().doubleValue() );
                sqlInserir.setInt( 7, obj.getDesconto().intValue() );
                sqlInserir.setString( 8, obj.getStatus());
                if (obj.getCondicaoRenegociacao().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(9, obj.getCondicaoRenegociacao().getCodigo().intValue() );
                } else {
                    sqlInserir.setNull(9, 0);
                }
                sqlInserir.setString(10, obj.getTipoFinanciamentoEnum().name());
                sqlInserir.setBoolean(11, obj.getTipoOrigemMatricula());
                sqlInserir.setBoolean(12, obj.getTipoOrigemMensalidade());
                sqlInserir.setBoolean(13, obj.getTipoOrigemBiblioteca());
                sqlInserir.setBoolean(14, obj.getTipoOrigemDevolucaoCheque());
                sqlInserir.setBoolean(15, obj.getTipoOrigemNegociacao());
                sqlInserir.setBoolean(16, obj.getTipoOrigemBolsaCusteadaConvenio());
                sqlInserir.setBoolean(17, obj.getTipoOrigemContratoReceita());
                sqlInserir.setBoolean(18, obj.getTipoOrigemOutros());
                sqlInserir.setBoolean(19, obj.getTipoOrigemInclusaoReposicao());
                sqlInserir.setString(20, obj.getTipoParcelaNegociar().name());
                sqlInserir.setBigDecimal(21, obj.getValorMinimoPorParcela());
                sqlInserir.setInt(22, obj.getQtdeInicialDiasAtraso());
                sqlInserir.setInt(23, obj.getQtdeFinalDiasAtraso());
                sqlInserir.setBoolean(24, obj.getUtilizarVisaoAdministrativa());
                sqlInserir.setBoolean(25, obj.getUtilizarVisaoAluno());
                sqlInserir.setInt(26, obj.getQtdeDiasEntrada());
                sqlInserir.setBoolean(27, obj.getGerarParcelas30DiasAposDataEntrada());

                sqlInserir.setDouble(28, obj.getFaixaEntradaFinal());
                sqlInserir.setBoolean(29, obj.getDefinirNumeroDiasVencimentoPrimeiraParcela());
                sqlInserir.setInt(30, obj.getNumeroDiasAposVencimentoEntrada());
                sqlInserir.setBoolean(31, obj.getIsentarJuroParcela());
                sqlInserir.setBoolean(32, obj.getIsentarMultaParcela());
                sqlInserir.setBoolean(33, obj.getIsentarIndiceReajustePorAtrasoParcela());
                sqlInserir.setString(34, obj.getDescricao());

                return sqlInserir;
            }
        }, new ResultSetExtractor() {
            public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
                if (arg0.next()) {
                    //obj.setNovoObj(Boolean.FALSE);
                    return arg0.getInt("codigo");
                }
                return null;
            }
        }));
        //obj.setNovoObj(Boolean.FALSE);
    }

    /**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>ItemCondicaoRenegociacaoVO</code>.
     * Sempre utiliza a chave primária da classe como atributo para localização do registro a ser alterado.
     * Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>alterar</code> da superclasse.
     * @param obj    Objeto da classe <code>ItemCondicaoRenegociacaoVO</code> que será alterada no banco de dados.
     * @exception Execption Caso haja problemas de conexão, restrição de acesso ou validação de dados.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final ItemCondicaoRenegociacaoVO obj) throws Exception {
        validarDados(obj);
       
        final String sql = "UPDATE ItemCondicaoRenegociacao set valorInicial=?, valorFinal=?, parcelaInicial=?, parcelaFinal=?, faixaentradainicial=?, juro=?, desconto=?, status=?, condicaoRenegociacao=?, tipoFinanciamento=?,"
        		+ "tipoOrigemMatricula=?, tipoOrigemMensalidade=?, tipoOrigemBiblioteca=?, tipoOrigemDevolucaoCheque=?, tipoOrigemNegociacao=?, tipoOrigemBolsaCusteadaConvenio=?, "
        		+ "tipoOrigemContratoReceita=?, tipoOrigemOutros=?, tipoOrigemInclusaoReposicao=?, tipoParcelaNegociar=?, valorMinimoPorParcela=?, qtdeInicialDiasAtraso=?, qtdeFinalDiasAtraso=?, "
        		+ " utilizarVisaoAdministrativa=?, utilizarVisaoAluno=?, qtdeDiasEntrada = ?, gerarParcelas30DiasAposDataEntrada = ?, faixaentradafinal=?, definirnumerodiasvencimentoprimeiraparcela = ?, "
        		+ " numerodiasaposvencimentoentrada =?, isentarjuroparcela =?, isentarmultaparcela = ?, isentarindicereajusteporatrasoparcela = ?, descricao = ? WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                sqlAlterar.setDouble( 1, obj.getValorInicial().doubleValue() );
                sqlAlterar.setDouble( 2, obj.getValorFinal().doubleValue() );
                sqlAlterar.setInt( 3, obj.getParcelaInicial().intValue() );
                sqlAlterar.setInt( 4, obj.getParcelaFinal().intValue() );
                sqlAlterar.setDouble( 5, obj.getFaixaEntradaInicial());
                sqlAlterar.setDouble( 6, obj.getJuro().doubleValue() );
                sqlAlterar.setInt( 7, obj.getDesconto().intValue() );
                sqlAlterar.setString( 8, obj.getStatus());
                if (obj.getCondicaoRenegociacao().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(9, obj.getCondicaoRenegociacao().getCodigo().intValue() );
                } else {
                    sqlAlterar.setNull(9, 0);
                }
                sqlAlterar.setString(10, obj.getTipoFinanciamentoEnum().name());
                sqlAlterar.setBoolean(11, obj.getTipoOrigemMatricula());
                sqlAlterar.setBoolean(12, obj.getTipoOrigemMensalidade());
                sqlAlterar.setBoolean(13, obj.getTipoOrigemBiblioteca());
                sqlAlterar.setBoolean(14, obj.getTipoOrigemDevolucaoCheque());
                sqlAlterar.setBoolean(15, obj.getTipoOrigemNegociacao());
                sqlAlterar.setBoolean(16, obj.getTipoOrigemBolsaCusteadaConvenio());
                sqlAlterar.setBoolean(17, obj.getTipoOrigemContratoReceita());
                sqlAlterar.setBoolean(18, obj.getTipoOrigemOutros());
                sqlAlterar.setBoolean(19, obj.getTipoOrigemInclusaoReposicao());
                sqlAlterar.setString(20, obj.getTipoParcelaNegociar().name());
                sqlAlterar.setBigDecimal(21, obj.getValorMinimoPorParcela());
                sqlAlterar.setInt(22, obj.getQtdeInicialDiasAtraso());
                sqlAlterar.setInt(23, obj.getQtdeFinalDiasAtraso());
                sqlAlterar.setBoolean(24, obj.getUtilizarVisaoAdministrativa());
                sqlAlterar.setBoolean(25, obj.getUtilizarVisaoAluno());
                sqlAlterar.setInt(26, obj.getQtdeDiasEntrada());
                sqlAlterar.setBoolean(27, obj.getGerarParcelas30DiasAposDataEntrada());
                sqlAlterar.setDouble(28, obj.getFaixaEntradaFinal());
                sqlAlterar.setBoolean(29, obj.getDefinirNumeroDiasVencimentoPrimeiraParcela());
                sqlAlterar.setInt(30, obj.getNumeroDiasAposVencimentoEntrada());
                sqlAlterar.setBoolean(31, obj.getIsentarJuroParcela());
                sqlAlterar.setBoolean(32, obj.getIsentarMultaParcela());
                sqlAlterar.setBoolean(33, obj.getIsentarIndiceReajustePorAtrasoParcela());
                sqlAlterar.setString(34, obj.getDescricao());
                sqlAlterar.setInt(35, obj.getCodigo().intValue() );
                return sqlAlterar;
            }});
    }

    /**
     * Operação responsável por excluir no BD um objeto da classe <code>ItemCondicaoRenegociacaoVO</code>.
     * Sempre localiza o registro a ser excluído através da chave primária da entidade.
     * Primeiramente verifica a conexão com o banco de dados e a permissão do usuário
     * para realizar esta operacão na entidade.
     * Isto, através da operação <code>excluir</code> da superclasse.
     * @param obj    Objeto da classe <code>ItemCondicaoRenegociacaoVO</code> que será removido no banco de dados.
     * @exception Execption Caso haja problemas de conexão ou restrição de acesso.
    */
    public void excluir(ItemCondicaoRenegociacaoVO obj) throws Exception {
        ItemCondicaoRenegociacao.excluir(getIdEntidade());
        String sql = "DELETE FROM ItemCondicaoRenegociacao WHERE ((codigo = ?))";
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
       
    }
    /**
     * Operação responsável por validar os dados de um objeto da classe <code>ItemCondicaoRenegociacaoVO</code>.
     * Todos os tipos de consistência de dados são e devem ser implementadas neste método.
     * São validações típicas: verificação de campos obrigatórios, verificação de valores válidos para os atributos.
     * @exception ConsistirExecption Se uma inconsistência for encontrada aumaticamente é gerada uma exceção descrevendo
     * o atributo e o erro ocorrido.
    */
    public void validarDados(ItemCondicaoRenegociacaoVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        ConsistirException consistir = new ConsistirException();
        if(obj.getDescricao().trim().isEmpty()) {
        	consistir.adicionarListaMensagemErro("O campo DESCRIÇÃO do item da condição de negociação deve ser informado.");
        }
        if(obj.getValorInicial() > obj.getValorFinal()) {
        	consistir.adicionarListaMensagemErro("O campo FAIXA VALOR DE não pode ter o valor inicial maior que o valor final.");
        }
        if(obj.getQtdeInicialDiasAtraso() > obj.getQtdeFinalDiasAtraso()) {
        	consistir.adicionarListaMensagemErro("O campo QTDE. DIAS ATRASO DE não pode ter o valor inicial maior que o valor final.");
        }
        if(obj.getFaixaEntradaInicial() > 100.0) {
        	consistir.adicionarListaMensagemErro("O campo ENTRADA(%) não pode ser maior que 100%.");
        }
        if(obj.getFaixaEntradaInicial().equals(100.0)) {
        	obj.setParcelaInicial(0);
        	obj.setParcelaFinal(0);
        }
        if(obj.getFaixaEntradaInicial() > 100.0 || obj.getFaixaEntradaFinal() > 100.0 ) {
        	consistir.adicionarListaMensagemErro("O campo FAIXA DE ENTRADA(%) não pode ser maior ou igual a 100%.");
        }
        if(obj.getDesconto() >= 100.0) {
        	consistir.adicionarListaMensagemErro("O campo DESCONTO(%) não pode ser maior ou igual a 100%.");
        }
        if(obj.getFaixaEntradaInicial() < 100.0 && obj.getParcelaInicial() <= 0) {
        	consistir.adicionarListaMensagemErro("O campo Nº DE PARCELA não pode iniciar em 0.");
        }
        if(obj.getFaixaEntradaInicial().equals(0.0)) {
        	obj.setQtdeDiasEntrada(0);
        	obj.setGerarParcelas30DiasAposDataEntrada(false);
        }
        if (consistir.existeErroListaMensagemErro()) {
            throw consistir;
        }
    
    }
    /**
     * Operação responsável por validar a unicidade dos dados de um objeto da classe <code>ItemCondicaoRenegociacaoVO</code>.
    */
    public void validarUnicidade(List<ItemCondicaoRenegociacaoVO> lista, ItemCondicaoRenegociacaoVO obj) throws ConsistirException {
        for (ItemCondicaoRenegociacaoVO repetido : lista) {
            if (repetido.getCodigo().intValue() == obj.getCodigo().intValue()) {
                throw new ConsistirException(UteisJSF.internacionalizar("msg_itemCondicaoRenegociacaoVO_unicidade_codigo"));
            }
        }
    }
     
    /**
     * Operação reponsável por realizar o UpperCase dos atributos do tipo String.
    */
    public void realizarUpperCaseDados(ItemCondicaoRenegociacaoVO obj) {
        if (!Uteis.realizarUpperCaseDadosAntesPersistencia) {
            return;
        }
    }

    /**
    * Rotina responsavel por executar as consultas disponiveis na Tela ItemCondicaoRenegociacaoCons.jsp.
    * Define o tipo de consulta a ser executada, por meio de ComboBox denominado campoConsulta, disponivel neste mesmo JSP.
    * Como resultado, disponibiliza um List com os objetos selecionados na sessao da pagina.
    */
    public List<ItemCondicaoRenegociacaoVO> consultar(String valorConsulta, String campoConsulta, boolean controlarAcesso,UsuarioVO usuario) throws Exception {
        if (campoConsulta.equals("codigo")) {
            if (valorConsulta.trim().equals("")) {
                throw new Exception(UteisJSF.internacionalizar("msg_validarCaracteresMinimosConsultaCodigo"));
            }
            if (valorConsulta.equals("")) {
                valorConsulta = "0";
            }
            int valorInt = Integer.parseInt(valorConsulta);
            return consultarPorCodigo(valorInt, controlarAcesso,usuario, Uteis.NIVELMONTARDADOS_TODOS);
        }
        return new ArrayList<>(0);
    }

    /**
     * Responsável por realizar uma consulta de <code>ItemCondicaoRenegociacao</code> através do valor do atributo 
     * <code>Integer codigo</code>. Retorna os objetos com valores iguais ou superiores ao parâmetro fornecido.
     * Faz uso da operação <code>montarDadosConsulta</code> que realiza o trabalho de prerarar o List resultante.
     * @param   controlarAcesso Indica se a aplicação deverá verificar se o usuário possui permissão para esta consulta ou não.
     * @return  List Contendo vários objetos da classe <code>ItemCondicaoRenegociacaoVO</code> resultantes da consulta.
     * @exception Exception Caso haja problemas de conexão ou restrição de acesso.
     */
    public List<ItemCondicaoRenegociacaoVO> consultarPorCodigo(Integer valorConsulta, boolean controlarAcesso,UsuarioVO usuario, int nivelMontarDados) throws Exception {
        ItemCondicaoRenegociacao.consultar(getIdEntidade(), controlarAcesso, usuario);
        String sqlStr = "SELECT * FROM ItemCondicaoRenegociacao WHERE codigo >= ?  ORDER BY codigo";
        return (montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sqlStr, valorConsulta), nivelMontarDados));
    }

    /**
     * Responsável por montar os dados de vários objetos, resultantes de uma consulta ao banco de dados (<code>ResultSet</code>).
     * Faz uso da operação <code>montarDados</code> que realiza o trabalho para um objeto por vez.
     * @return  List Contendo vários objetos da classe <code>ItemCondicaoRenegociacaoVO</code> resultantes da consulta.
    */
    public static List<ItemCondicaoRenegociacaoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List<ItemCondicaoRenegociacaoVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados));
        }
        tabelaResultado = null;
        return vetResultado;
    }

    /**
     * Responsável por montar os dados resultantes de uma consulta ao banco de dados (<code>ResultSet</code>)
     * em um objeto da classe <code>ItemCondicaoRenegociacaoVO</code>.
     * @return  O objeto da classe <code>ItemCondicaoRenegociacaoVO</code> com os dados devidamente montados.
    */
    public static ItemCondicaoRenegociacaoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados) throws Exception {
        ItemCondicaoRenegociacaoVO obj = new ItemCondicaoRenegociacaoVO();
        obj.setCodigo( new Integer( dadosSQL.getInt("codigo")));
        obj.setValorInicial( new Double( dadosSQL.getDouble("valorInicial")));
        obj.setValorFinal( new Double( dadosSQL.getDouble("valorFinal")));
        obj.setParcelaInicial( new Integer( dadosSQL.getInt("parcelaInicial")));
        obj.setParcelaFinal( new Integer( dadosSQL.getInt("parcelaFinal")));
        obj.setFaixaEntradaInicial(new Double( dadosSQL.getDouble("faixaentradainicial")));
        obj.setJuro( new Double( dadosSQL.getDouble("juro")));
        obj.setDesconto( dadosSQL.getDouble("desconto"));
        obj.setStatus( new String( dadosSQL.getString("status")));
        obj.getCondicaoRenegociacao().setCodigo( new Integer( dadosSQL.getInt("condicaoRenegociacao")));
        obj.setTipoFinanciamentoEnum(TipoFinanciamentoEnum.valueOf(dadosSQL.getString("tipoFinanciamento")));
        obj.setTipoOrigemMatricula(dadosSQL.getBoolean("tipoOrigemMatricula"));
        obj.setTipoOrigemMensalidade(dadosSQL.getBoolean("tipoOrigemMensalidade"));
        obj.setTipoOrigemBiblioteca(dadosSQL.getBoolean("tipoOrigemBiblioteca"));
        obj.setTipoOrigemDevolucaoCheque(dadosSQL.getBoolean("tipoOrigemDevolucaoCheque"));
        obj.setTipoOrigemNegociacao(dadosSQL.getBoolean("tipoOrigemNegociacao"));
        obj.setTipoOrigemBolsaCusteadaConvenio(dadosSQL.getBoolean("tipoOrigemBolsaCusteadaConvenio"));
        obj.setTipoOrigemContratoReceita(dadosSQL.getBoolean("tipoOrigemContratoReceita"));
        obj.setTipoOrigemOutros(dadosSQL.getBoolean("tipoOrigemOutros"));
        obj.setTipoOrigemInclusaoReposicao(dadosSQL.getBoolean("tipoOrigemInclusaoReposicao"));
        obj.setQtdeInicialDiasAtraso(dadosSQL.getInt("qtdeInicialDiasAtraso"));
        obj.setQtdeFinalDiasAtraso(dadosSQL.getInt("qtdeFinalDiasAtraso"));
        obj.setUtilizarVisaoAdministrativa(dadosSQL.getBoolean("utilizarVisaoAdministrativa"));
        obj.setUtilizarVisaoAluno(dadosSQL.getBoolean("utilizarVisaoAluno"));
        obj.setGerarParcelas30DiasAposDataEntrada(dadosSQL.getBoolean("gerarParcelas30DiasAposDataEntrada"));
        obj.setQtdeDiasEntrada(dadosSQL.getInt("qtdeDiasEntrada"));
        if (dadosSQL.getString("tipoParcelaNegociar") != null) {
        	obj.setTipoParcelaNegociar(TipoParcelaNegociarEnum.valueOf(dadosSQL.getString("tipoParcelaNegociar")));
        }
        obj.setValorMinimoPorParcela(dadosSQL.getBigDecimal("valorMinimoPorParcela"));
        obj.setNovoObj(new Boolean(false));

        obj.setFaixaEntradaFinal(dadosSQL.getDouble("faixaentradafinal"));
        obj.setDefinirNumeroDiasVencimentoPrimeiraParcela(dadosSQL.getBoolean("definirnumerodiasvencimentoprimeiraparcela"));
        obj.setNumeroDiasAposVencimentoEntrada(dadosSQL.getInt("numerodiasaposvencimentoentrada"));
        obj.setIsentarJuroParcela(dadosSQL.getBoolean("isentarjuroparcela"));
        obj.setIsentarMultaParcela(dadosSQL.getBoolean("isentarmultaparcela"));
        obj.setIsentarIndiceReajustePorAtrasoParcela(dadosSQL.getBoolean("isentarindicereajusteporatrasoparcela"));
        obj.setDescricao(dadosSQL.getString("descricao"));
        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_DADOSBASICOS) {
            return obj;
        }

        return obj;
    }

    /**
     * Operação responsável por excluir todos os objetos da <code>ItemCondicaoRenegociacaoVO</code> no BD.
     * Faz uso da operação <code>excluir</code> disponível na classe <code>ItemCondicaoRenegociacao</code>.
     * @param <code>condicaoRenegociacao</code> campo chave para exclusão dos objetos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
    */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirItemCondicaoRenegociacaos( Integer condicaoRenegociacao, List<ItemCondicaoRenegociacaoVO> objetos ) throws Exception {
    	String sql = "DELETE FROM ItemCondicaoRenegociacao WHERE (condicaoRenegociacao = ?)";
        if(objetos != null && !objetos.isEmpty()){
            sql += " and codigo not in(";
            boolean primeiro = true;
            for(ItemCondicaoRenegociacaoVO obj:objetos){                
                if(primeiro){
                    sql+= obj.getCodigo();
                    primeiro = false;
                }else{
                    sql+= ", "+obj.getCodigo();
                }
            }
            sql += " )";
        }
        getConexao().getJdbcTemplate().update(sql, new Object[]{condicaoRenegociacao});
    }

    /**
     * Operação responsável por alterar todos os objetos da <code>ItemCondicaoRenegociacaoVO</code> contidos em um Hashtable no BD.
     * Faz uso da operação <code>excluirItemCondicaoRenegociacaos</code> e <code>incluirItemCondicaoRenegociacaos</code> disponíveis na classe <code>ItemCondicaoRenegociacao</code>.
     * @param objetos  List com os objetos a serem alterados ou incluídos no BD.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
    */
    @Override
    public void alterarItemCondicaoRenegociacaos( Integer condicaoRenegociacao, List<ItemCondicaoRenegociacaoVO> objetos ) throws Exception {
        excluirItemCondicaoRenegociacaos( condicaoRenegociacao, objetos );
        for(ItemCondicaoRenegociacaoVO obj:objetos){            
            obj.getCondicaoRenegociacao().setCodigo(condicaoRenegociacao);
            if(obj.getNovoObj()){                
                incluir(obj);
            }else{
                alterar(obj);
            }
        }
    }

    /**
     * Operação responsável por incluir objetos da <code>ItemCondicaoRenegociacaoVO</code> no BD.
     * Garantindo o relacionamento com a entidade principal <code>financeiro.CondicaoRenegociacao</code> através do atributo de vínculo.
     * @param objetos List contendo os objetos a serem gravados no BD da classe.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
    */
    @Override
    public void incluirItemCondicaoRenegociacaos( Integer condicaoRenegociacaoPrm, List<ItemCondicaoRenegociacaoVO> objetos ) throws Exception {
        for(ItemCondicaoRenegociacaoVO obj:objetos){            
            obj.getCondicaoRenegociacao().setCodigo(condicaoRenegociacaoPrm);            
            incluir(obj);
        }
    }

    /**
     * Operação responsável por consultar todos os <code>ItemCondicaoRenegociacaoVO</code> relacionados a um objeto da classe <code>financeiro.CondicaoRenegociacao</code>.
     * @param condicaoRenegociacao  Atributo de <code>financeiro.CondicaoRenegociacao</code> a ser utilizado para localizar os objetos da classe <code>ItemCondicaoRenegociacaoVO</code>.
     * @return List  Contendo todos os objetos da classe <code>ItemCondicaoRenegociacaoVO</code> resultantes da consulta.
     * @exception Exception  Erro de conexão com o BD ou restrição de acesso a esta operação.
    */
    public static List<ItemCondicaoRenegociacaoVO> consultarItemCondicaoRenegociacaos(Integer condicaoRenegociacao, int nivelMontarDados) throws Exception {
        ItemCondicaoRenegociacao.consultar(getIdEntidade());
        List<ItemCondicaoRenegociacaoVO> objetos = new ArrayList<>();
        String sql = "SELECT * FROM ItemCondicaoRenegociacao WHERE condicaoRenegociacao = "+condicaoRenegociacao;
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        while (resultado.next()) {
            ItemCondicaoRenegociacaoVO novoObj = new ItemCondicaoRenegociacaoVO();
            novoObj = ItemCondicaoRenegociacao.montarDados(resultado, nivelMontarDados);
            objetos.add(novoObj);
        }
        return objetos;
    }

    /**
     * Operação responsável por localizar um objeto da classe <code>ItemCondicaoRenegociacaoVO</code>
     * através de sua chave primária. 
     * @exception Exception Caso haja problemas de conexão ou localização do objeto procurado.
    */
    public ItemCondicaoRenegociacaoVO consultarPorChavePrimaria( Integer codigoPrm, int nivelMontarDados, UsuarioVO usuario ) throws Exception {
        ItemCondicaoRenegociacao.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM ItemCondicaoRenegociacao WHERE codigo = "+codigoPrm;
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql);
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados ( ItemCondicaoRenegociacao ).");
        }
        return (montarDados(tabelaResultado, nivelMontarDados));
    }

//    public List<ItemCondicaoRenegociacaoVO> consultarUnicidade(ItemCondicaoRenegociacaoVO obj, boolean alteracao) throws Exception {
//        super.verificarPermissaoConsultar(getIdEntidade(), false);
//        return new ArrayList(0);
//    }
    @Override
    public List<ItemCondicaoRenegociacaoVO> consultarItemCondicaoRenegociacaoAptaAlunoUtilizar(int condicaoRenegociacao, double valor) throws Exception{
        StringBuilder sb = new StringBuilder("select * from itemcondicaorenegociacao");
        sb.append(" where condicaorenegociacao = ").append(condicaoRenegociacao);
        sb.append(" and  valorinicial  <= ").append(valor).append(" and valorfinal  >= ").append(valor).append("");
        sb.append(" and itemcondicaorenegociacao.status = 'AT'");
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), Uteis.NIVELMONTARDADOS_TODOS);
    }
    
    /**
     * Operação reponsável por retornar o identificador desta classe.
     * Este identificar é utilizado para verificar as permissões de acesso as operações desta classe.
    */
    public static String getIdEntidade() {
        return ItemCondicaoRenegociacao.idEntidade;
    }
     
    /**
     * Operação reponsável por definir um novo valor para o identificador desta classe.
     * Esta alteração deve ser possível, pois, uma mesma classe de negócio pode ser utilizada com objetivos
     * distintos. Assim ao se verificar que Como o controle de acesso é realizado com base neste identificador, 
    */
    public void setIdEntidade( String idEntidade ) {
        ItemCondicaoRenegociacao.idEntidade = idEntidade;
    }
}