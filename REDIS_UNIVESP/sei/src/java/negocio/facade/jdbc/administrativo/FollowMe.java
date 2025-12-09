package negocio.facade.jdbc.administrativo;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.administrativo.ComunicacaoInternaVO;
import negocio.comuns.administrativo.ComunicadoInternoDestinatarioVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.administrativo.FollowMeCategoriaDespesaVO;
import negocio.comuns.administrativo.FollowMeDepartamentoVO;
import negocio.comuns.administrativo.FollowMeGrupoDestinatarioVO;
import negocio.comuns.administrativo.FollowMeUnidadeEnsinoVO;
import negocio.comuns.administrativo.FollowMeVO;
import negocio.comuns.administrativo.FuncionarioGrupoDestinatariosVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.administrativo.enumeradores.TipoOrigemComunicacaoInternaEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.financeiro.enumerador.ReceitaDespesaEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.dominios.PastaBaseArquivoEnum;
import negocio.comuns.utilitarias.dominios.PrioridadeComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoComunicadoInterno;
import negocio.comuns.utilitarias.dominios.TipoNivelEducacional;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.administrativo.FollowMeInterfaceFacade;
import relatorio.arquitetura.GeradorRelatorio;
import relatorio.negocio.comuns.arquitetura.SuperParametroRelVO;
import relatorio.negocio.comuns.arquitetura.enumeradores.TipoRelatorioEnum;
import relatorio.negocio.comuns.painelGestor.FollowMeCategoriaDespesaPadraoAlteradoVO;
import relatorio.negocio.comuns.painelGestor.FollowMeFinanceiroAcademicoRelVO;
import relatorio.negocio.comuns.painelGestor.FollowMeGraficoRelVO;
import relatorio.negocio.comuns.painelGestor.FollowMeRelVO;
import relatorio.negocio.comuns.painelGestor.enumeradores.TipoAreaFollowMeEnum;
import relatorio.negocio.comuns.painelGestor.enumeradores.TipoRelatorioAreaFollowMe;
import relatorio.negocio.comuns.painelGestor.enumeradores.TipoRelatorioFollowMeEnum;

@Repository
@Lazy
public class FollowMe extends ControleAcesso implements FollowMeInterfaceFacade {

    /**
     * 
     */
    private static final long serialVersionUID = -6740327750375865169L;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void persistir(FollowMeVO followMeVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
        validarDados(followMeVO);
        if (followMeVO.isNovoObj()) {
            incluir(followMeVO, controlarAcesso, usuarioVO);
        } else {
            alterar(followMeVO, controlarAcesso, usuarioVO);
        }

    }

    private PreparedStatementCreator getPreparedStatementCreator(final FollowMeVO followMeVO, final String sql, final boolean incluir) {
        return new PreparedStatementCreator() {

            @Override
            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                int x = 1;
                PreparedStatement preparedStatement = arg0.prepareStatement(sql);
                preparedStatement.setString(x++, followMeVO.getDescricao());
                preparedStatement.setInt(x++, followMeVO.getPeriodoBase());
                preparedStatement.setInt(x++, followMeVO.getPeriodoBaseComparativo());
                preparedStatement.setBoolean(x++, followMeVO.getApresentarDadosTodasUnidades());
                preparedStatement.setBoolean(x++, followMeVO.getGastoPorDepartamento());
                preparedStatement.setBoolean(x++, followMeVO.getReceitaDespesa());
                preparedStatement.setBoolean(x++, followMeVO.getReceitaDespesaPorNivelEducacional());
                preparedStatement.setBoolean(x++, followMeVO.getPosicaoFinanceira());
                preparedStatement.setBoolean(x++, followMeVO.getInadimplencia());
                preparedStatement.setBoolean(x++, followMeVO.getGastoPorDepartamentoAnoAnterior());
                preparedStatement.setBoolean(x++, followMeVO.getGerarGraficoDepartamentoMesAMes());
                preparedStatement.setBoolean(x++, followMeVO.getReceitaDespesaAnoAnterior());
                preparedStatement.setBoolean(x++, followMeVO.getReceitaDespesaPorNivelEducacionalAnoAnterior());
                preparedStatement.setBoolean(x++, followMeVO.getReceitaDespesaPorNivelEducacionalMesMes());
                preparedStatement.setBoolean(x++, followMeVO.getGerarGraficoDespesa());
                preparedStatement.setBoolean(x++, followMeVO.getGerarGraficoCategoriaDespesa());
                preparedStatement.setBoolean(x++, followMeVO.getGerarGraficoCategoriaDespesaMesAMes());
                preparedStatement.setBoolean(x++, followMeVO.getGerarListaCategoriaDespesaPadraoAlterado());
                preparedStatement.setDouble(x++, followMeVO.getPercentualCategoriaDespesaPadraoAlterado());
                preparedStatement.setBoolean(x++, followMeVO.getApresentarDespesaDepartamentoNaoInformado());

                preparedStatement.setBoolean(x++, followMeVO.getGerarListaAcademicoFinanceiro());
                preparedStatement.setBoolean(x++, followMeVO.getApresentarCustoMedioPorAlunoRelatorioAcademicoFinanceiro());
                preparedStatement.setBoolean(x++, followMeVO.getApresentarValorMedioPorParcelaRelatorioAcademicoFinanceiro());
                preparedStatement.setBoolean(x++, followMeVO.getApresentarQtdeAlunoRelatorioAcademicoFinanceiro());
                preparedStatement.setBoolean(x++, followMeVO.getApresentarQtdeTurmaRelatorioAcademicoFinanceiro());
                preparedStatement.setBoolean(x++, followMeVO.getApresentarDespesaRelatorioAcademicoFinanceiro());
                preparedStatement.setBoolean(x++, followMeVO.getApresentarInadimplenciaRelatorioAcademicoFinanceiro());
                if (!incluir) {
                    preparedStatement.setInt(x++, followMeVO.getCodigo());
                }
                return preparedStatement;
            }
        };
    }

    private void alterar(final FollowMeVO followMeVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
        alterar("FollowMe", controlarAcesso, usuarioVO);
        StringBuilder sql = new StringBuilder("UPDATE followMe SET descricao = ?, periodoBase = ?, periodoBaseComparativo = ?, ");
        sql.append(" apresentarDadosTodasUnidades=?, gastoPorDepartamento=?, receitaDespesa=?, receitaDespesaPorNivelEducacional=?, ");
        sql.append(" posicaoFinanceira=?, inadimplencia=?, ");
        sql.append(" gastoPorDepartamentoAnoAnterior = ?, gerarGraficoDepartamentoMesAMes =?, receitaDespesaAnoAnterior=?, ");
        sql.append(" receitaDespesaPorNivelEducacionalAnoAnterior = ?, receitaDespesaPorNivelEducacionalMesMes =?, gerarGraficoDespesa=?, ");
        sql.append(" gerarGraficoCategoriaDespesa = ?, gerarGraficoCategoriaDespesaMesAMes =?, gerarListaCategoriaDespesaPadraoAlterado=?, ");
        sql.append(" percentualCategoriaDespesaPadraoAlterado = ?, apresentarDespesaDepartamentoNaoInformado = ?,  ");
        sql.append(" gerarListaAcademicoFinanceiro = ?, ");
        sql.append(" apresentarCustoMedioPorAlunoRelatorioAcademicoFinanceiro = ?,");
        sql.append(" apresentarValorMedioPorParcelaRelatorioAcademicoFinanceiro = ?,");
        sql.append(" apresentarQtdeAlunoRelatorioAcademicoFinanceiro = ?,");
        sql.append(" apresentarQtdeTurmaRelatorioAcademicoFinanceiro = ?,");
        sql.append(" apresentarDespesaRelatorioAcademicoFinanceiro  = ?,");
        sql.append(" apresentarInadimplenciaRelatorioAcademicoFinanceiro = ? ");
        sql.append(" WHERE codigo = ? ");
        getConexao().getJdbcTemplate().update(getPreparedStatementCreator(followMeVO, sql.toString(), false));
        getFacadeFactory().getFollowMeGrupoDestinatarioFacade().excluirFollowMeGrupoDestinatario(followMeVO);
        getFacadeFactory().getFollowMeUnidadeEnsinoFacade().excluirFollowMeUnidadeEnsino(followMeVO);
        for (FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO : followMeVO.getFollowMeGrupoDestinatarioVOs()) {
            getFacadeFactory().getFollowMeGrupoDestinatarioFacade().persistir(followMeGrupoDestinatarioVO, followMeVO);
        }
        for (FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO : followMeVO.getFollowMeUnidadeEnsinoVOs()) {
            if (followMeUnidadeEnsinoVO.getSelecionar()) {
                getFacadeFactory().getFollowMeUnidadeEnsinoFacade().persistir(followMeUnidadeEnsinoVO, followMeVO);
            }
        }
        for (FollowMeCategoriaDespesaVO followMeCategoriaDespesaVO : followMeVO.getFollowMeCategoriaDespesaVOs()) {
            if (followMeCategoriaDespesaVO.getSelecionado()) {
                getFacadeFactory().getFollowMeCategoriaDespesaFacade().persistir(followMeCategoriaDespesaVO, followMeVO);
            }
        }
        for (FollowMeDepartamentoVO followMeDepartamentoVO : followMeVO.getFollowMeDepartamentoVOs()) {
            if (followMeDepartamentoVO.getSelecionado()) {
                getFacadeFactory().getFollowMeDepartamentoFacade().persistir(followMeDepartamentoVO, followMeVO);
            }
        }
    }

    private void incluir(final FollowMeVO followMeVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
        try {
            incluir("FollowMe", controlarAcesso, usuarioVO);
            StringBuilder sql = new StringBuilder("INSERT INTO followMe (descricao, periodoBase, periodoBaseComparativo, ");
            sql.append(" apresentarDadosTodasUnidades, gastoPorDepartamento, receitaDespesa, receitaDespesaPorNivelEducacional, ");
            sql.append(" posicaoFinanceira, inadimplencia, ");
            sql.append(" gastoPorDepartamentoAnoAnterior , gerarGraficoDepartamentoMesAMes, receitaDespesaAnoAnterior, ");
            sql.append(" receitaDespesaPorNivelEducacionalAnoAnterior , receitaDespesaPorNivelEducacionalMesMes, gerarGraficoDespesa, ");
            sql.append(" gerarGraficoCategoriaDespesa , gerarGraficoCategoriaDespesaMesAMes, gerarListaCategoriaDespesaPadraoAlterado, ");
            sql.append(" percentualCategoriaDespesaPadraoAlterado , apresentarDespesaDepartamentoNaoInformado,  ");
            sql.append(" gerarListaAcademicoFinanceiro, ");
            sql.append(" apresentarCustoMedioPorAlunoRelatorioAcademicoFinanceiro,");
            sql.append(" apresentarValorMedioPorParcelaRelatorioAcademicoFinanceiro,");
            sql.append(" apresentarQtdeAlunoRelatorioAcademicoFinanceiro,");
            sql.append(" apresentarQtdeTurmaRelatorioAcademicoFinanceiro,");
            sql.append(" apresentarDespesaRelatorioAcademicoFinanceiro,");
            sql.append(" apresentarInadimplenciaRelatorioAcademicoFinanceiro");
            sql.append(") VALUES (?,?,?,?,?,?,?,?,?,?, ");// 1-10
            sql.append(" ?, ?,?,?,?,?,?,?,?,?, ");// 11-20
            sql.append(" ?, ?, ?, ?, ?, ?, ?) returning codigo");
            followMeVO.setCodigo(getConexao().getJdbcTemplate().query(getPreparedStatementCreator(followMeVO, sql.toString(), true),
                    new ResultSetExtractor<Integer>() {

                        public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                            if (arg0.next()) {
                                followMeVO.setNovoObj(Boolean.FALSE);
                                return arg0.getInt("codigo");
                            }
                            return null;
                        }
                    }));
            for (FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO : followMeVO.getFollowMeGrupoDestinatarioVOs()) {
                getFacadeFactory().getFollowMeGrupoDestinatarioFacade().persistir(followMeGrupoDestinatarioVO, followMeVO);
            }
            for (FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO : followMeVO.getFollowMeUnidadeEnsinoVOs()) {
                if (followMeUnidadeEnsinoVO.getSelecionar()) {
                    getFacadeFactory().getFollowMeUnidadeEnsinoFacade().persistir(followMeUnidadeEnsinoVO, followMeVO);
                }
            }
            for (FollowMeCategoriaDespesaVO followMeCategoriaDespesaVO : followMeVO.getFollowMeCategoriaDespesaVOs()) {
                if (followMeCategoriaDespesaVO.getSelecionado()) {
                    getFacadeFactory().getFollowMeCategoriaDespesaFacade().persistir(followMeCategoriaDespesaVO, followMeVO);
                }
            }
            for (FollowMeDepartamentoVO followMeDepartamentoVO : followMeVO.getFollowMeDepartamentoVOs()) {
                if (followMeDepartamentoVO.getSelecionado()) {
                    getFacadeFactory().getFollowMeDepartamentoFacade().persistir(followMeDepartamentoVO, followMeVO);
                }
            }
        } catch (Exception e) {
            followMeVO.setNovoObj(Boolean.TRUE);
            for (FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO : followMeVO.getFollowMeGrupoDestinatarioVOs()) {
                followMeGrupoDestinatarioVO.setNovoObj(true);
            }
            for (FollowMeCategoriaDespesaVO followMeCategoriaDespesaVO : followMeVO.getFollowMeCategoriaDespesaVOs()) {
                followMeCategoriaDespesaVO.setSelecionado(false);
            }
            for (FollowMeDepartamentoVO followMeDepartamentoVO : followMeVO.getFollowMeDepartamentoVOs()) {
                followMeDepartamentoVO.setSelecionado(false);
            }
            throw e;
        }
    }

    private void validarDados(FollowMeVO followMeVO) throws ConsistirException {
        ConsistirException consistirException = null;
        if (followMeVO.getDescricao().trim().isEmpty() || followMeVO.getDescricao() == "") {
            consistirException = consistirException == null ? consistirException = new ConsistirException() : consistirException;
            consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_FollowMe_descricao"));
        }
        boolean existe = false;
        for (FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO : followMeVO.getFollowMeUnidadeEnsinoVOs()) {
            if (followMeUnidadeEnsinoVO.getSelecionar()) {
                existe = true;
                break;
            }
        }
        if (!existe) {
            consistirException = consistirException == null ? consistirException = new ConsistirException() : consistirException;
            consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_FollowMe_unidadeEnsino"));
        }
		if (consistirException != null && !consistirException.getListaMensagemErro().isEmpty()) {
			throw consistirException;
		}
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void excluir(FollowMeVO followMeVO, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
        try {
            excluir("FollowMe", controlarAcesso, usuarioVO);
            String sql = "DELETE FROM followMe WHERE ((codigo = ?))";
            getConexao().getJdbcTemplate().update(sql, followMeVO.getCodigo());
        } catch (Exception e) {
            throw e;
        }

    }

    @Override
    public List<FollowMeVO> consultar(String descricao, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuarioVO, Integer limite, Integer pagina) throws Exception {
        StringBuilder sb = new StringBuilder("SELECT * FROM followMe");
        if (descricao != null && descricao.trim().isEmpty()) {
            sb.append(" where sem_acentos(upper(descricao)) like (sem_acentos(upper('%").append(descricao).append("%'))) ");
        }
        sb.append(" order by descricao");
        if (limite != null && limite > 0) {
            sb.append(" limit ").append(limite).append(" offset ").append(pagina);
        }
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), nivelMontarDados, usuarioVO);
    }

    @Override
    public Integer consultarTotalRegistros(String descricao) throws Exception {
        StringBuilder sb = new StringBuilder("SELECT count(followMe.codigo) as qtde FROM followMe");
        if (descricao != null && descricao.trim().isEmpty()) {
            sb.append(" where sem_acentos(upper(descricao)) like (sem_acentos(upper('%").append(descricao).append("%'))) ");
        }
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (rs.next()) {
            return rs.getInt("qtde");
        }
        return 0;
    }

    private List<FollowMeVO> montarDadosConsulta(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        List<FollowMeVO> followMeVOs = new ArrayList<FollowMeVO>(0);
        while (rs.next()) {
            followMeVOs.add(montarDados(rs, nivelMontarDados, usuarioVO));
        }
        return followMeVOs;
    }

    private FollowMeVO montarDados(SqlRowSet rs, int nivelMontarDados, UsuarioVO usuarioVO) throws Exception {
        FollowMeVO obj = new FollowMeVO();
        obj.setNovoObj(false);
        obj.setCodigo(rs.getInt("codigo"));
        obj.setDescricao(rs.getString("descricao"));
        obj.setPeriodoBase(rs.getInt("periodoBase"));
        obj.setPeriodoBaseComparativo(rs.getInt("periodoBaseComparativo"));
        obj.setApresentarDadosTodasUnidades(rs.getBoolean("apresentarDadosTodasUnidades"));
        obj.setGastoPorDepartamento(rs.getBoolean("gastoPorDepartamento"));
        obj.setReceitaDespesa(rs.getBoolean("receitaDespesa"));
        obj.setReceitaDespesaPorNivelEducacional(rs.getBoolean("receitaDespesaPorNivelEducacional"));
        obj.setPosicaoFinanceira(rs.getBoolean("posicaoFinanceira"));
        obj.setInadimplencia(rs.getBoolean("inadimplencia"));

        obj.setApresentarDespesaDepartamentoNaoInformado(rs.getBoolean("apresentarDespesaDepartamentoNaoInformado"));
        obj.setGastoPorDepartamentoAnoAnterior(rs.getBoolean("gastoPorDepartamentoAnoAnterior"));
        obj.setGerarGraficoCategoriaDespesaMesAMes(rs.getBoolean("gerarGraficoCategoriaDespesaMesAMes"));
        obj.setReceitaDespesaAnoAnterior(rs.getBoolean("receitaDespesaAnoAnterior"));
        obj.setReceitaDespesaPorNivelEducacionalAnoAnterior(rs.getBoolean("receitaDespesaPorNivelEducacionalAnoAnterior"));
        obj.setReceitaDespesaPorNivelEducacionalMesMes(rs.getBoolean("receitaDespesaPorNivelEducacionalMesMes"));
        obj.setGerarGraficoDespesa(rs.getBoolean("gerarGraficoDespesa"));
        obj.setGerarGraficoCategoriaDespesa(rs.getBoolean("gerarGraficoCategoriaDespesa"));
        obj.setGerarGraficoDepartamentoMesAMes(rs.getBoolean("gerarGraficoDepartamentoMesAMes"));
        obj.setGerarListaCategoriaDespesaPadraoAlterado(rs.getBoolean("gerarListaCategoriaDespesaPadraoAlterado"));
        obj.setPercentualCategoriaDespesaPadraoAlterado(rs.getDouble("percentualCategoriaDespesaPadraoAlterado"));
        obj.setGerarListaAcademicoFinanceiro(rs.getBoolean("gerarListaAcademicoFinanceiro"));
        obj.setApresentarValorMedioPorParcelaRelatorioAcademicoFinanceiro(rs.getBoolean("apresentarValorMedioPorParcelaRelatorioAcademicoFinanceiro"));
        obj.setApresentarCustoMedioPorAlunoRelatorioAcademicoFinanceiro(rs.getBoolean("apresentarCustoMedioPorAlunoRelatorioAcademicoFinanceiro"));
        obj.setApresentarInadimplenciaRelatorioAcademicoFinanceiro(rs.getBoolean("apresentarInadimplenciaRelatorioAcademicoFinanceiro"));
        obj.setApresentarQtdeAlunoRelatorioAcademicoFinanceiro(rs.getBoolean("apresentarQtdeAlunoRelatorioAcademicoFinanceiro"));
        obj.setApresentarQtdeTurmaRelatorioAcademicoFinanceiro(rs.getBoolean("apresentarQtdeTurmaRelatorioAcademicoFinanceiro"));
        obj.setApresentarDespesaRelatorioAcademicoFinanceiro(rs.getBoolean("apresentarDespesaRelatorioAcademicoFinanceiro"));

        if (nivelMontarDados == Uteis.NIVELMONTARDADOS_TODOS) {
            obj.setFollowMeGrupoDestinatarioVOs(getFacadeFactory().getFollowMeGrupoDestinatarioFacade().consultarPorFollowMe(obj.getCodigo(), nivelMontarDados));
            obj.setFollowMeUnidadeEnsinoVOs(getFacadeFactory().getFollowMeUnidadeEnsinoFacade().consultarPorFollowMe(obj.getCodigo()));
            obj.setFollowMeCategoriaDespesaVOs(getFacadeFactory().getFollowMeCategoriaDespesaFacade().consultarPorFollowMe(obj.getCodigo()));
            obj.setFollowMeDepartamentoVOs(getFacadeFactory().getFollowMeDepartamentoFacade().consultarPorFollowMe(obj.getCodigo()));
        }
        return obj;
    }

    @Override
    public void adicionarFollowMeGrupoDestinatarioVO(FollowMeVO followMeVO, FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO) throws Exception {
        getFacadeFactory().getFollowMeGrupoDestinatarioFacade().validarDados(followMeGrupoDestinatarioVO);
        if (followMeGrupoDestinatarioVO.getOrdem() == 0) {
            followMeGrupoDestinatarioVO.setOrdem(followMeVO.getFollowMeGrupoDestinatarioVOs().size() + 1);
            followMeVO.getFollowMeGrupoDestinatarioVOs().add(followMeGrupoDestinatarioVO);
        } else {
            followMeVO.getFollowMeGrupoDestinatarioVOs().set(followMeGrupoDestinatarioVO.getOrdem() - 1, followMeGrupoDestinatarioVO);
        }

    }

    @Override
    public void removerFollowMeGrupoDestinatarioVO(FollowMeVO followMeVO, FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO) {
        followMeVO.getFollowMeGrupoDestinatarioVOs().remove(followMeGrupoDestinatarioVO.getOrdem() - 1);
        int x = 1;
        for (FollowMeGrupoDestinatarioVO followMeGrupoDestinatario : followMeVO.getFollowMeGrupoDestinatarioVOs()) {
            followMeGrupoDestinatario.setOrdem(x++);
        }
    }

    @Override
    public FollowMeVO consultarPorChavePrimaria(Integer followMe, int nivelMontarDados, boolean controlarAcesso, UsuarioVO usuarioVO) throws Exception {
        StringBuilder sb = new StringBuilder("SELECT * FROM followMe where codigo = ").append(followMe);
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (rs.next()) {
            return montarDados(rs, nivelMontarDados, usuarioVO);
        }
        throw new Exception("Dados não encontrados(Follow-ME).");
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public void realizarEnvioDadosFollowMeAgora(FollowMeVO followMeVO, ConfiguracaoGeralSistemaVO confUpload) throws Exception {
        try {
            if (confUpload == null) {
                confUpload = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo();
            }
            followMeVO.getFollowMeRelVOs().clear();
            gerarRelatorioFollowMeDiretoriaFinanceira(followMeVO, confUpload);
            gerarComunicadoInternoFollowMe(followMeVO, confUpload);
        } catch (Exception e) {
            throw e;
        }
    }

    public void gerarComunicadoInternoFollowMe(FollowMeVO followMeVO, ConfiguracaoGeralSistemaVO confUpload) throws Exception {
        if (!followMeVO.getFollowMeGrupoDestinatarioVOs().isEmpty() && !followMeVO.getFollowMeRelVOs().isEmpty()) {
            ConfiguracaoGeralSistemaVO confEmail = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesEnvioEmailMaisResponsavelPadrao();
            ComunicacaoInternaVO comunicacaoInternaVO = new ComunicacaoInternaVO();
            comunicacaoInternaVO.setAssunto("Follow-Me - " + Uteis.getDataAtual());
            StringBuilder msg = new StringBuilder();
            msg.append("Prezado(a), </br></br>");
            msg.append("   Segue abaixo os links para download dos relatórios gerenciais. </br></br>");
            msg.append("<ul>");
            for (FollowMeRelVO followMeRelVO : followMeVO.getFollowMeRelVOs()) {
                String url = confUpload.getUrlExternoDownloadArquivo() + "/" + followMeRelVO.getCaminhoCompletoDownload();
                msg.append("<li><a href=\"").append(url).append("\">");
                msg.append(followMeRelVO.getTipoRelatorioAreaFollowMe().getValorApresentar());
                msg.append("</a></li>");
            }
            msg.append("</ul>");
            comunicacaoInternaVO.setMensagem(comunicacaoInternaVO.getMensagemComLayout(msg.toString()));
            comunicacaoInternaVO.setEnviarEmail(true);
            comunicacaoInternaVO.setData(new Date());
            comunicacaoInternaVO.setResponsavel(confEmail.getResponsavelPadraoComunicadoInterno());
            comunicacaoInternaVO.setTipoOrigemComunicacaoInternaEnum(TipoOrigemComunicacaoInternaEnum.FOLLOW_ME);
            comunicacaoInternaVO.setTipoComunicadoInterno(TipoComunicadoInterno.SOMENTE_LEITURA.getValor());
            comunicacaoInternaVO.setPrioridade(PrioridadeComunicadoInterno.NORMAL.getValor());
            comunicacaoInternaVO.setTipoMarketing(Boolean.FALSE);
            comunicacaoInternaVO.setTipoLeituraObrigatoria(Boolean.FALSE);
            comunicacaoInternaVO.setDigitarMensagem(Boolean.TRUE);
            comunicacaoInternaVO.setTipoDestinatario("FU");
            for (FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO : followMeVO.getFollowMeGrupoDestinatarioVOs()) {
                if (followMeGrupoDestinatarioVO.getGrupoDestinatario().getListaFuncionariosGrupoDestinatariosVOs().isEmpty()) {
                    followMeGrupoDestinatarioVO.getGrupoDestinatario().setListaFuncionariosGrupoDestinatariosVOs(getFacadeFactory().getFuncionarioGrupoDestinatariosFacade().consultarPorCodigoGrupoDestinatarios(followMeGrupoDestinatarioVO.getGrupoDestinatario().getCodigo(), Uteis.NIVELMONTARDADOS_TODOS, null));
                }
                for (FuncionarioGrupoDestinatariosVO funcionarioGrupoDestinatariosVO : followMeGrupoDestinatarioVO.getGrupoDestinatario().getListaFuncionariosGrupoDestinatariosVOs()) {
                    ComunicadoInternoDestinatarioVO comunicadoInternoDestinatarioVO = new ComunicadoInternoDestinatarioVO();
                    comunicadoInternoDestinatarioVO.setDestinatario(funcionarioGrupoDestinatariosVO.getFuncionario().getPessoa());
                    comunicadoInternoDestinatarioVO.setEmail(funcionarioGrupoDestinatariosVO.getFuncionario().getPessoa().getEmail());
                    comunicadoInternoDestinatarioVO.setNome(funcionarioGrupoDestinatariosVO.getFuncionario().getPessoa().getNome());
                    comunicadoInternoDestinatarioVO.setTipoComunicadoInterno(comunicacaoInternaVO.getTipoComunicadoInterno());
                    comunicacaoInternaVO.adicionarObjComunicadoInternoDestinatarioVOs(comunicadoInternoDestinatarioVO);
                }
            }
            getFacadeFactory().getComunicacaoInternaFacade().incluir(comunicacaoInternaVO, false, new UsuarioVO(), confEmail,null);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public void realizarEnvioDadosFollowMe() throws Exception {
        try {
            List<FollowMeVO> followMeVOs = consultarFollowMeParaGeracaoRelatorio();
            if (!followMeVOs.isEmpty()) {
                ConfiguracaoGeralSistemaVO confUpload = getFacadeFactory().getConfiguracaoGeralSistemaFacade().consultarConfiguraoesLocalUploadArquivoFixo();
                for (FollowMeVO followMeVO : followMeVOs) {
                    realizarEnvioDadosFollowMeAgora(followMeVO, confUpload);
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }

    private List<FollowMeVO> consultarFollowMeParaGeracaoRelatorio() throws Exception {
        StringBuilder sb = new StringBuilder("SELECT followme.*, followmegrupodestinatario.codigo as \"followmegrupodestinatario.codigo\", followmegrupodestinatario.grupodestinatario as \"followmegrupodestinatario.grupodestinatario\" from followmegrupodestinatario");
        sb.append(" inner join  followme on followme.codigo = followmegrupodestinatario.followme");
        sb.append(" where");
        sb.append(" (frequenciaenviofollowme =  'SEMANAL' and diasemana::INT-1 = extract(DOW from current_date))");
        sb.append(" or");
        sb.append(" (frequenciaenviofollowme =  'MENSAL' and diaDoMes = extract(DAY from current_date))");
        sb.append(" or");
        sb.append(" (frequenciaenviofollowme =  'DATA_ESPECIFICA' and extract(DAY from diaMesEspecifico) = extract(DAY from current_date)");
        sb.append(" and extract(MONTH from diaMesEspecifico) = extract(MONTH from current_date)) order by followme.codigo");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        List<FollowMeVO> followMeVOs = new ArrayList<FollowMeVO>(0);
        FollowMeVO obj = null;
        while (rs.next()) {
            if (obj == null || !obj.getCodigo().equals(rs.getInt("codigo"))) {
                obj = new FollowMeVO();
                obj = montarDados(rs, Uteis.NIVELMONTARDADOS_DADOSBASICOS, null);
                obj.setFollowMeUnidadeEnsinoVOs(getFacadeFactory().getFollowMeUnidadeEnsinoFacade().consultarPorFollowMe(obj.getCodigo()));
                followMeVOs.add(obj);
            }
            FollowMeGrupoDestinatarioVO followMeGrupoDestinatarioVO = new FollowMeGrupoDestinatarioVO();
            followMeGrupoDestinatarioVO.getGrupoDestinatario().setCodigo(rs.getInt("followmegrupodestinatario.grupodestinatario"));
            followMeGrupoDestinatarioVO.getGrupoDestinatario().setListaFuncionariosGrupoDestinatariosVOs(getFacadeFactory().getFuncionarioGrupoDestinatariosFacade().consultarPorCodigoGrupoDestinatarios(followMeGrupoDestinatarioVO.getGrupoDestinatario().getCodigo(), Uteis.NIVELMONTARDADOS_DADOSBASICOS, null));
            followMeGrupoDestinatarioVO.setCodigo(rs.getInt("followmegrupodestinatario.codigo"));
            obj.getFollowMeGrupoDestinatarioVOs().add(followMeGrupoDestinatarioVO);
        }
        return followMeVOs;
    }

    public void gerarRelatorioFollowMeDiretoriaFinanceira(FollowMeVO followMeVO, ConfiguracaoGeralSistemaVO conf) throws Exception {

        if (followMeVO.getGastoPorDepartamento()) {
            FollowMeRelVO followMeRelVO = new FollowMeRelVO();
            followMeRelVO.setFollowMe(followMeVO);
            followMeRelVO.setTipoRelatorioAreaFollowMe(TipoRelatorioAreaFollowMe.GASTO_POR_DEPARTAMENTO);
            followMeRelVO.setTipoAreaFollowMeEnum(TipoAreaFollowMeEnum.DIRETORIA_FINANCEIRA);
            consultarDadosGastoPorDepartamento(followMeRelVO);
            gerarRelatorioFollowMePorTipo(followMeRelVO, conf);
            if (!followMeRelVO.getFollowMeGraficoRelVOs().isEmpty()) {
                incluirFollowMeRel(followMeRelVO);
                followMeVO.getFollowMeRelVOs().add(followMeRelVO);
            }
        }

        if (followMeVO.getGerarGraficoDespesa()) {
            FollowMeRelVO followMeRelVO = new FollowMeRelVO();
            followMeRelVO.setFollowMe(followMeVO);
            followMeRelVO.setTipoRelatorioAreaFollowMe(TipoRelatorioAreaFollowMe.DESPESA);
            followMeRelVO.setTipoAreaFollowMeEnum(TipoAreaFollowMeEnum.DIRETORIA_FINANCEIRA);
            consultarDespesa(followMeRelVO);
            gerarRelatorioFollowMePorTipo(followMeRelVO, conf);
            if (!followMeRelVO.getFollowMeGraficoRelVOs().isEmpty()) {
                incluirFollowMeRel(followMeRelVO);
                followMeVO.getFollowMeRelVOs().add(followMeRelVO);
            }
        }
        if (followMeVO.getReceitaDespesa()) {
            FollowMeRelVO followMeRelVO = new FollowMeRelVO();
            followMeRelVO.setFollowMe(followMeVO);
            followMeRelVO.setTipoRelatorioAreaFollowMe(TipoRelatorioAreaFollowMe.RECEITA_DESPESA);
            followMeRelVO.setTipoAreaFollowMeEnum(TipoAreaFollowMeEnum.DIRETORIA_FINANCEIRA);
            consultarReceitaDespesa(followMeRelVO);
            gerarRelatorioFollowMePorTipo(followMeRelVO, conf);
            if (!followMeRelVO.getFollowMeGraficoRelVOs().isEmpty()) {
                incluirFollowMeRel(followMeRelVO);
                followMeVO.getFollowMeRelVOs().add(followMeRelVO);
            }
        }
        if (followMeVO.getGerarGraficoCategoriaDespesa()) {
            FollowMeRelVO followMeRelVO = new FollowMeRelVO();
            followMeRelVO.setFollowMe(followMeVO);
            followMeRelVO.setTipoRelatorioAreaFollowMe(TipoRelatorioAreaFollowMe.CATEGORIA_DESPESA);
            followMeRelVO.setTipoAreaFollowMeEnum(TipoAreaFollowMeEnum.DIRETORIA_FINANCEIRA);
            consultarDadosCategoriaDespesa(followMeRelVO);
            gerarRelatorioFollowMePorTipo(followMeRelVO, conf);
            if (!followMeRelVO.getFollowMeGraficoRelVOs().isEmpty()) {
                incluirFollowMeRel(followMeRelVO);
                followMeVO.getFollowMeRelVOs().add(followMeRelVO);
            }
        }
        if (followMeVO.getGerarListaCategoriaDespesaPadraoAlterado()) {
            FollowMeRelVO followMeRelVO = new FollowMeRelVO();
            followMeRelVO.setFollowMe(followMeVO);
            followMeRelVO.setTipoRelatorioAreaFollowMe(TipoRelatorioAreaFollowMe.CATEGORIA_DESPESA_PADRAO_ALTERADO);
            followMeRelVO.setTipoAreaFollowMeEnum(TipoAreaFollowMeEnum.DIRETORIA_FINANCEIRA);
            consultarDadosCategoriaDespesaPadraoAlterado(followMeRelVO);
            gerarRelatorioFollowMePorTipo(followMeRelVO, conf);
            if (!followMeRelVO.getFollowMeCategoriaDespesaPadraoAlteradoVOs().isEmpty()) {
                incluirFollowMeRel(followMeRelVO);
                followMeVO.getFollowMeRelVOs().add(followMeRelVO);
            }
        }
        if (followMeVO.getReceitaDespesaPorNivelEducacional()) {
            FollowMeRelVO followMeRelVO = new FollowMeRelVO();
            followMeRelVO.setFollowMe(followMeVO);
            followMeRelVO.setTipoRelatorioAreaFollowMe(TipoRelatorioAreaFollowMe.RECEITA_DESPESA_NIVEL_EDUCACIONAL);
            followMeRelVO.setTipoAreaFollowMeEnum(TipoAreaFollowMeEnum.DIRETORIA_FINANCEIRA);
            consultarReceitaDespesaPorNivelEducacional(followMeRelVO);
            gerarRelatorioFollowMePorTipo(followMeRelVO, conf);
            if (!followMeRelVO.getFollowMeGraficoRelVOs().isEmpty()) {
                incluirFollowMeRel(followMeRelVO);
                followMeVO.getFollowMeRelVOs().add(followMeRelVO);
            }
        }
        if (followMeVO.getPosicaoFinanceira()) {
            FollowMeRelVO followMeRelVO = new FollowMeRelVO();
            followMeRelVO.setFollowMe(followMeVO);
            followMeRelVO.setTipoRelatorioAreaFollowMe(TipoRelatorioAreaFollowMe.POSICAO_FINANCEIRA);
            followMeRelVO.setTipoAreaFollowMeEnum(TipoAreaFollowMeEnum.DIRETORIA_FINANCEIRA);
            consultarPosicaoFinanceira(followMeRelVO);
            gerarRelatorioFollowMePorTipo(followMeRelVO, conf);
            if (!followMeRelVO.getFollowMeGraficoRelVOs().isEmpty()) {
                incluirFollowMeRel(followMeRelVO);
                followMeVO.getFollowMeRelVOs().add(followMeRelVO);
            }
        }

        if (followMeVO.getInadimplencia()) {
            FollowMeRelVO followMeRelVO = new FollowMeRelVO();
            followMeRelVO.setFollowMe(followMeVO);
            followMeRelVO.setTipoRelatorioAreaFollowMe(TipoRelatorioAreaFollowMe.INADIMPLENCIA);
            followMeRelVO.setTipoAreaFollowMeEnum(TipoAreaFollowMeEnum.DIRETORIA_FINANCEIRA);
            consultarInandimplencia(followMeRelVO);
            gerarRelatorioFollowMePorTipo(followMeRelVO, conf);
            if (!followMeRelVO.getFollowMeGraficoRelVOs().isEmpty()) {
                incluirFollowMeRel(followMeRelVO);
                followMeVO.getFollowMeRelVOs().add(followMeRelVO);
            }
        }

        if (followMeVO.getGerarListaAcademicoFinanceiro()) {
            FollowMeRelVO followMeRelVO = new FollowMeRelVO();
            followMeRelVO.setFollowMe(followMeVO);
            followMeRelVO.setTipoRelatorioAreaFollowMe(TipoRelatorioAreaFollowMe.ACADEMICO_FINANCEIRO);
            followMeRelVO.setTipoAreaFollowMeEnum(TipoAreaFollowMeEnum.DIRETORIA_FINANCEIRA);
            consultarRelatorioAcademicoFinanceiro(followMeRelVO);
            gerarRelatorioFollowMePorTipo(followMeRelVO, conf);
            if (!followMeRelVO.getFollowMeFinanceiroAcademicoRelVOs().isEmpty()) {
                incluirFollowMeRel(followMeRelVO);
                followMeVO.getFollowMeRelVOs().add(followMeRelVO);
            }
        }

    }

    public String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor" + File.separator);
    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "painelGestor" + File.separator + "followMe.jrxml");
    }

    public void gerarRelatorioFollowMePorTipo(FollowMeRelVO followMeRelVO, ConfiguracaoGeralSistemaVO conf) throws Exception {
        if (!followMeRelVO.getFollowMeGraficoRelVOs().isEmpty() 
                || !followMeRelVO.getFollowMeCategoriaDespesaPadraoAlteradoVOs().isEmpty()
                || !followMeRelVO.getFollowMeFinanceiroAcademicoRelVOs().isEmpty()) {

            followMeRelVO.setNomeFisicoArquivo(followMeRelVO.getTipoRelatorioAreaFollowMe() + "_" + Uteis.getData(followMeRelVO.getDataInicio(), "dd_MM_yyyy") + "_a_" + Uteis.getData(followMeRelVO.getDataTermino(), "dd_MM_yyyy") + ".pdf");
            followMeRelVO.setCaminhoBaseArquivo(PastaBaseArquivoEnum.FOLLOW_ME.getValue() + File.separator + followMeRelVO.getFollowMe().getCodigo() + File.separator + followMeRelVO.getTipoAreaFollowMeEnum().name());
            String caminhoRelatorio = conf.getLocalUploadArquivoFixo() + File.separator + followMeRelVO.getCaminhoBaseArquivo();
            File diretorio = new File(caminhoRelatorio);
            if (!diretorio.exists()) {
                diretorio.mkdirs();
            }
            SuperParametroRelVO superParametroRelVO = new SuperParametroRelVO();
            superParametroRelVO.setCaminhoBaseRelatorio(getCaminhoBaseRelatorio());
            superParametroRelVO.setNomeDesignIreport(getDesignIReportRelatorio());
            superParametroRelVO.setTipoRelatorioEnum(TipoRelatorioEnum.PDF);
            superParametroRelVO.setSubReport_Dir(getCaminhoBaseRelatorio());
            superParametroRelVO.adicionarParametro("subtitulo", followMeRelVO.getTipoRelatorioAreaFollowMe().getValorApresentar());
            superParametroRelVO.adicionarParametro("periodo", "Período " + Uteis.getData(followMeRelVO.getDataInicio()) + " até " + Uteis.getData(followMeRelVO.getDataTermino()));
            superParametroRelVO.setTituloRelatorio(followMeRelVO.getTipoAreaFollowMeEnum().getValorApresentar());
            List<FollowMeRelVO> followMeRelVOs = new ArrayList<FollowMeRelVO>(0);
            followMeRelVOs.add(followMeRelVO);
            superParametroRelVO.setListaObjetos(followMeRelVOs);

            GeradorRelatorio.realizarExportacaoPDF(superParametroRelVO, caminhoRelatorio, followMeRelVO.getNomeFisicoArquivo());

        }

    }

    public void consultarDadosGastoPorDepartamento(FollowMeRelVO followMeRelVO) throws Exception {
        Date dataBase = null;
        StringBuilder sb = null;
        SqlRowSet rs = null;
        Map<Integer, Integer> ordenador = null;
        try {
            Boolean existeDepartamento = false;
            for (FollowMeDepartamentoVO obj : followMeRelVO.getFollowMe().getFollowMeDepartamentoVOs()) {
                if (obj.getSelecionado()) {
                    existeDepartamento = true;
                    break;
                }
            }
            if (existeDepartamento) {
                sb = new StringBuilder("select ROW_NUMBER() OVER(order by case when sum(valor) is null then 0 else sum(valor) end desc, departamento.nome ) as ordem, departamento.codigo from departamento ");
                sb.append(" left join contapagar on departamento.codigo = contapagar.departamento and ");
                sb.append(realizarGeracaoWherePeriodo(followMeRelVO.getDataInicio(), Uteis.getDataUltimoDiaMes(followMeRelVO.getDataInicio()), "datavencimento", false));
                sb.append(" and situacao in ('AP', 'PP', 'PA') ");
                sb.append(" group by departamento.codigo, departamento.nome ");
                rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
                ordenador = new HashMap<Integer, Integer>();
                while (rs.next()) {
                    ordenador.put(rs.getInt("ordem"), rs.getInt("codigo"));
                }
                if (!ordenador.isEmpty()) {
                    sb = new StringBuilder("");
                    if (followMeRelVO.getFollowMe().getApresentarDadosTodasUnidades()) {
                        if (followMeRelVO.getFollowMe().getGerarGraficoDepartamentoMesAMes()) {
                            dataBase = followMeRelVO.getDataInicio();
                            while (dataBase.compareTo(followMeRelVO.getDataTermino()) <= 0) {
                                if (!sb.toString().trim().isEmpty()) {
                                    sb.append(" union all ");
                                }
                                sb.append(realizarMontagemSqlGastoPorDepartamento(dataBase, ordenador, null, followMeRelVO.getFollowMe().getFollowMeDepartamentoVOs(), followMeRelVO.getFollowMe().getApresentarDespesaDepartamentoNaoInformado()));
                                if (followMeRelVO.getFollowMe().getGastoPorDepartamentoAnoAnterior()) {
                                    sb.append(" union all ");
                                    sb.append(realizarMontagemSqlGastoPorDepartamento(Uteis.getDataFutura(dataBase, Calendar.YEAR, -1), ordenador, null, followMeRelVO.getFollowMe().getFollowMeDepartamentoVOs(), followMeRelVO.getFollowMe().getApresentarDespesaDepartamentoNaoInformado()));

                                }
                                dataBase = Uteis.getDataFutura(dataBase, Calendar.MONTH, 1);
                            }
                        } else {
                            sb.append(realizarMontagemSqlGastoPorDepartamentoGeral(followMeRelVO.getDataInicio(), Uteis.getDataUltimoDiaMes(followMeRelVO.getDataTermino()), ordenador, null, followMeRelVO.getFollowMe().getFollowMeDepartamentoVOs(), followMeRelVO.getFollowMe().getApresentarDespesaDepartamentoNaoInformado()));
                            if (followMeRelVO.getFollowMe().getGastoPorDepartamentoAnoAnterior()) {
                                sb.append(" union all ");
                                sb.append(realizarMontagemSqlGastoPorDepartamentoGeral(Uteis.getDataFutura(followMeRelVO.getDataInicio(), Calendar.YEAR, -1), Uteis.getDataFutura(Uteis.getDataUltimoDiaMes(followMeRelVO.getDataTermino()), Calendar.YEAR, -1), ordenador, null, followMeRelVO.getFollowMe().getFollowMeDepartamentoVOs(), followMeRelVO.getFollowMe().getApresentarDespesaDepartamentoNaoInformado()));

                            }
                        }
                    }

                    for (FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO : followMeRelVO.getFollowMe().getFollowMeUnidadeEnsinoVOs()) {
                        if (followMeUnidadeEnsinoVO.getSelecionar()) {
                            if (followMeRelVO.getFollowMe().getGerarGraficoDepartamentoMesAMes()) {
                                dataBase = followMeRelVO.getDataInicio();
                                while (dataBase.compareTo(followMeRelVO.getDataTermino()) <= 0) {
                                    if (!sb.toString().trim().isEmpty()) {
                                        sb.append(" union all ");
                                    }
                                    sb.append(realizarMontagemSqlGastoPorDepartamento(dataBase, ordenador, followMeUnidadeEnsinoVO.getUnidadeEnsino(), followMeRelVO.getFollowMe().getFollowMeDepartamentoVOs(), followMeRelVO.getFollowMe().getApresentarDespesaDepartamentoNaoInformado()));
                                    if (followMeRelVO.getFollowMe().getGastoPorDepartamentoAnoAnterior()) {
                                        sb.append(" union all ");
                                        sb.append(realizarMontagemSqlGastoPorDepartamento(Uteis.getDataFutura(dataBase, Calendar.YEAR, -1), ordenador, followMeUnidadeEnsinoVO.getUnidadeEnsino(), followMeRelVO.getFollowMe().getFollowMeDepartamentoVOs(), followMeRelVO.getFollowMe().getApresentarDespesaDepartamentoNaoInformado()));

                                    }
                                    dataBase = Uteis.getDataFutura(dataBase, Calendar.MONTH, 1);
                                }
                            } else {
                                if (!sb.toString().trim().isEmpty()) {
                                    sb.append(" union all ");
                                }
                                sb.append(realizarMontagemSqlGastoPorDepartamentoGeral(followMeRelVO.getDataInicio(), Uteis.getDataUltimoDiaMes(followMeRelVO.getDataTermino()), ordenador, followMeUnidadeEnsinoVO.getUnidadeEnsino(), followMeRelVO.getFollowMe().getFollowMeDepartamentoVOs(), followMeRelVO.getFollowMe().getApresentarDespesaDepartamentoNaoInformado()));
                                if (followMeRelVO.getFollowMe().getGastoPorDepartamentoAnoAnterior()) {
                                    sb.append(" union all ");
                                    sb.append(realizarMontagemSqlGastoPorDepartamentoGeral(Uteis.getDataFutura(followMeRelVO.getDataInicio(), Calendar.YEAR, -1), Uteis.getDataFutura(Uteis.getDataUltimoDiaMes(followMeRelVO.getDataTermino()), Calendar.YEAR, -1), ordenador, followMeUnidadeEnsinoVO.getUnidadeEnsino(), followMeRelVO.getFollowMe().getFollowMeDepartamentoVOs(), followMeRelVO.getFollowMe().getApresentarDespesaDepartamentoNaoInformado()));

                                }
                            }
                        }
                    }
                    montarDadosGraficoFollowMe(followMeRelVO, getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), true);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            dataBase = null;
            sb = null;
            rs = null;
            ordenador = null;
        }
    }

    public void consultarDadosCategoriaDespesa(FollowMeRelVO followMeRelVO) throws Exception {
        Date dataBase = null;
        StringBuilder sb = null;
        SqlRowSet rs = null;
        Map<Integer, Integer> ordenador = null;
        try {
            boolean existeCategoriaDespesa = false;
            for (FollowMeCategoriaDespesaVO obj : followMeRelVO.getFollowMe().getFollowMeCategoriaDespesaVOs()) {
                if (obj.getSelecionado()) {
                    existeCategoriaDespesa = true;
                    break;
                }
            }
            if (existeCategoriaDespesa) {
                sb = new StringBuilder("select ROW_NUMBER() OVER(order by case when sum(valor) is null then 0 else sum(valor) end desc, categoriadespesa.descricao ) as ordem, categoriadespesa.codigo from categoriadespesa ");
                sb.append(" inner join contapagar on categoriadespesa.codigo = contapagar.centrodespesa where ");
                sb.append(realizarGeracaoWherePeriodo(followMeRelVO.getDataInicio(), Uteis.getDataUltimoDiaMes(followMeRelVO.getDataInicio()), "datavencimento", false));
                sb.append(" and situacao in ('AP', 'PP', 'PA') ");
                sb.append(" and categoriadespesa.codigo in (0");
                for (FollowMeCategoriaDespesaVO followMeCategoriaDespesaVO : followMeRelVO.getFollowMe().getFollowMeCategoriaDespesaVOs()) {
                    if (followMeCategoriaDespesaVO.getSelecionado()) {
                        sb.append(", ").append(followMeCategoriaDespesaVO.getCategoriaDespesa().getCodigo());
                    }
                }
                sb.append(") ");
                sb.append(" group by categoriadespesa.codigo, categoriadespesa.descricao ");
                rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
                ordenador = new HashMap<Integer, Integer>();
                while (rs.next()) {
                    ordenador.put(rs.getInt("ordem"), rs.getInt("codigo"));
                }
                if (!ordenador.isEmpty()) {
                    sb = new StringBuilder("");
                    if (followMeRelVO.getFollowMe().getApresentarDadosTodasUnidades()) {
                        if (followMeRelVO.getFollowMe().getGerarGraficoCategoriaDespesaMesAMes()) {
                            dataBase = followMeRelVO.getDataInicio();
                            while (dataBase.compareTo(followMeRelVO.getDataTermino()) <= 0) {
                                if (!sb.toString().trim().isEmpty()) {
                                    sb.append(" union all ");
                                }
                                sb.append(realizarMontagemSqlGastoPorCategoriaDespesa(dataBase, ordenador, null, followMeRelVO.getFollowMe().getFollowMeCategoriaDespesaVOs()));
                                dataBase = Uteis.getDataFutura(dataBase, Calendar.MONTH, 1);
                            }
                        } else {
                            sb.append(realizarMontagemSqlGastoPorCategoriaDespesaAgrupado(followMeRelVO.getDataInicio(), Uteis.getDataUltimoDiaMes(followMeRelVO.getDataTermino()), ordenador, null, followMeRelVO.getFollowMe().getFollowMeCategoriaDespesaVOs()));
                        }
                    }

                    for (FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO : followMeRelVO.getFollowMe().getFollowMeUnidadeEnsinoVOs()) {
                        if (followMeUnidadeEnsinoVO.getSelecionar()) {
                            if (followMeRelVO.getFollowMe().getGerarGraficoCategoriaDespesaMesAMes()) {
                                dataBase = followMeRelVO.getDataInicio();
                                while (dataBase.compareTo(followMeRelVO.getDataTermino()) <= 0) {
                                    if (!sb.toString().trim().isEmpty()) {
                                        sb.append(" union all ");
                                    }
                                    sb.append(realizarMontagemSqlGastoPorCategoriaDespesa(dataBase, ordenador, followMeUnidadeEnsinoVO.getUnidadeEnsino(), followMeRelVO.getFollowMe().getFollowMeCategoriaDespesaVOs()));
                                    dataBase = Uteis.getDataFutura(dataBase, Calendar.MONTH, 1);
                                }
                            } else {
                            	if (!sb.toString().trim().isEmpty()) {
                                    sb.append(" union all ");
                                }
                                sb.append(realizarMontagemSqlGastoPorCategoriaDespesaAgrupado(followMeRelVO.getDataInicio(), Uteis.getDataUltimoDiaMes(followMeRelVO.getDataTermino()), ordenador, followMeUnidadeEnsinoVO.getUnidadeEnsino(), followMeRelVO.getFollowMe().getFollowMeCategoriaDespesaVOs()));

                            }
                        }
                    }
                    montarDadosGraficoFollowMe(followMeRelVO, getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), true);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            dataBase = null;
            sb = null;
            rs = null;
            ordenador = null;
        }
    }

    public void consultarDadosCategoriaDespesaPadraoAlterado(FollowMeRelVO followMeRelVO) throws Exception {

        StringBuilder sb = null;
        SqlRowSet rs = null;
        Map<Integer, Integer> ordenador = null;
        followMeRelVO.getFollowMeCategoriaDespesaPadraoAlteradoVOs().clear();
        try {
            sb = new StringBuilder("");
            int ordem = 0;
            if (followMeRelVO.getFollowMe().getApresentarDadosTodasUnidades()) {
                sb.append(realizarMontagemSqlGastoPorCategoriaDespesaPadraoAlterado(ordem++, followMeRelVO.getDataInicio(), followMeRelVO.getDataTermino(), null, followMeRelVO.getFollowMe().getPercentualCategoriaDespesaPadraoAlterado(), followMeRelVO.getFollowMe().getPeriodoBaseComparativo()));
            }
            for (FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO : followMeRelVO.getFollowMe().getFollowMeUnidadeEnsinoVOs()) {
                if (followMeUnidadeEnsinoVO.getSelecionar()) {
                    if (!sb.toString().trim().isEmpty()) {
                        sb.append(" union all ");
                    }
                    sb.append(realizarMontagemSqlGastoPorCategoriaDespesaPadraoAlterado(ordem++, followMeRelVO.getDataInicio(), followMeRelVO.getDataTermino(), followMeUnidadeEnsinoVO.getUnidadeEnsino(), followMeRelVO.getFollowMe().getPercentualCategoriaDespesaPadraoAlterado(), followMeRelVO.getFollowMe().getPeriodoBaseComparativo()));
                }
            }
            if (!sb.toString().trim().isEmpty()) {
                sb.append(" order by ordem, categoriadespesa");

                rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
                FollowMeCategoriaDespesaPadraoAlteradoVO obj = null;
                while (rs.next()) {
                    obj = new FollowMeCategoriaDespesaPadraoAlteradoVO();
                    obj.setCategoriaDespesa(rs.getString("categoriaDespesa"));
                    obj.setTitulo(rs.getString("titulo"));
                    if(rs.getDouble("valorMesAtual")>=rs.getDouble("valorMedio")){
                        obj.setPercentualDesviado(rs.getDouble("percentualDesviado")-100);
                    }else{
                        obj.setPercentualDesviado(rs.getDouble("percentualDesviado")-100);
                    }
                    obj.setValorMedio(rs.getDouble("valorMedio"));
                    obj.setValorMesAtual(rs.getDouble("valorMesAtual"));
                    followMeRelVO.getFollowMeCategoriaDespesaPadraoAlteradoVOs().add(obj);
                }
            }

        } catch (Exception e) {
            throw e;
        } finally {
            sb = null;
            rs = null;
            ordenador = null;
        }
    }

    public void montarDadosGraficoFollowMe(FollowMeRelVO followMeRelVO, SqlRowSet rs, Boolean concatenarValorSerie) {
        while (rs.next()) {
            montarDadosBasicosGraficoFollowMe(followMeRelVO, rs, concatenarValorSerie);
        }
    }

    public void montarDadosBasicosGraficoFollowMe(FollowMeRelVO followMeRelVO, SqlRowSet rs, Boolean concatenarValorSerie) {
        FollowMeGraficoRelVO followMeGraficoRelVO = new FollowMeGraficoRelVO();
        followMeGraficoRelVO.setCategoria(rs.getString("categoria"));
        followMeGraficoRelVO.setTituloGrafico(rs.getString("tituloGrafico"));
        followMeGraficoRelVO.setValor(rs.getDouble("valor"));
        if (concatenarValorSerie) {
            followMeGraficoRelVO.setSerie(rs.getString("serie") + " - R$" + Uteis.getDoubleFormatado(followMeGraficoRelVO.getValor()));
        } else {
            followMeGraficoRelVO.setSerie(rs.getString("serie"));
        }
        followMeRelVO.getFollowMeGraficoRelVOs().add(followMeGraficoRelVO);
    }

    private StringBuilder realizarMontagemSqlGastoPorCategoriaDespesa(Date dataBase, Map<Integer, Integer> ordenador, UnidadeEnsinoVO unidadeEnsino, List<FollowMeCategoriaDespesaVO> followMeCategoriaDespesaVOs) throws Exception {
        StringBuilder sb = new StringBuilder(" select ROW_NUMBER() OVER( order by case categoriadespesa.codigo ");
        for (Integer codigo : ordenador.keySet()) {
            sb.append(" when ").append(ordenador.get(codigo)).append(" then ").append(codigo);
        }
        sb.append(" else 10000 end ) as ordem,  ");
        sb.append(" categoriadespesa.descricao as serie, '").append(MesAnoEnum.getMesData(dataBase).getMesAbreviado() + "/" + Uteis.getAnoData(dataBase)).append("' as categoria, ");
        sb.append(" case when sum(valor) is null then 0 else sum(valor) end  as valor,");
        if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
            sb.append(" '").append(TipoRelatorioFollowMeEnum.CATEGORIA_DESPESA_TODAS_UNIDADES.getValorApresentar().replace("{1}", MesAnoEnum.getMesData(dataBase).getMesAbreviado() + "/" + Uteis.getAnoData(dataBase))).append("' as tituloGrafico ");
        } else {
            sb.append(" '").append(TipoRelatorioFollowMeEnum.CATEGORIA_DESPESA_UNIDADE_ESPECIFICA.getValorApresentar().replace("{1}", unidadeEnsino.getNome()).replace("{2}", MesAnoEnum.getMesData(dataBase).getMesAbreviado() + "/" + Uteis.getAnoData(dataBase))).append("' as tituloGrafico ");
        }
        sb.append(" from   categoriadespesa");
        sb.append(" left join contapagar on categoriadespesa.codigo = contapagar.centrodespesa where ");
        sb.append(realizarGeracaoWherePeriodo(dataBase, Uteis.getDataUltimoDiaMes(dataBase), "datavencimento", false));
        sb.append(" and contapagar.situacao in ('AP', 'PP', 'PA') ");
        sb.append(" and categoriadespesa.codigo in (0");
        for (FollowMeCategoriaDespesaVO followMeCategoriaDespesaVO : followMeCategoriaDespesaVOs) {
            if (followMeCategoriaDespesaVO.getSelecionado()) {
                sb.append(", ").append(followMeCategoriaDespesaVO.getCategoriaDespesa().getCodigo());
            }
        }
        sb.append(") ");
        if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and contapagar.unidadeEnsino =  ").append(unidadeEnsino.getCodigo());
        }

        sb.append(" group by categoriadespesa.descricao, categoriadespesa.codigo");

        // sb.append(" union all");
        //
        // sb.append(" select 1000 as ordem,");
        // sb.append(" 'Cat. Desp. Não Inf.' as serie, '").append(Uteis.getData(dataBase, "MM/yy")).append("' as categoria,");
        // sb.append(" case when sum(valor) is null then 0 else sum(valor) end  as valor,");
        // if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
        // sb.append(" '").append(TipoRelatorioFollowMeEnum.CATEGORIA_DESPESA_TODAS_UNIDADES.getValorApresentar().replace("{1}", MesAnoEnum.getMesData(dataBase).getMesAbreviado() +
        // "/" + Uteis.getAnoData(dataBase))).append("' as tituloGrafico ");
        // } else {
        // sb.append(" '").append(TipoRelatorioFollowMeEnum.CATEGORIA_DESPESA_UNIDADE_ESPECIFICA.getValorApresentar().replace("{1}", unidadeEnsino.getNome()).replace("{2}",
        // MesAnoEnum.getMesData(dataBase).getMesAbreviado() + "/" + Uteis.getAnoData(dataBase))).append("' as tituloGrafico ");
        // }
        // sb.append(" from   contapagar where centrodespesa is null and ");
        // sb.append(realizarGeracaoWherePeriodo(dataBase, Uteis.getDataUltimoDiaMes(dataBase), "datavencimento", false));
        // sb.append(" and contapagar.situacao in ('AP', 'PP', 'PA') ");
        // if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
        // sb.append(" and contapagar.unidadeEnsino =  ").append(unidadeEnsino.getCodigo());
        // }
        return sb;
    }

    private StringBuilder realizarMontagemSqlGastoPorDepartamento(Date dataBase, Map<Integer, Integer> ordenador, UnidadeEnsinoVO unidadeEnsino, List<FollowMeDepartamentoVO> followMeDepartamentoVOs, Boolean apresentarDespesaDepartamentoNaoInformado) throws Exception {
        StringBuilder sb = new StringBuilder(" select ROW_NUMBER() OVER( order by case departamento.codigo ");
        for (Integer codigo : ordenador.keySet()) {
            sb.append(" when ").append(ordenador.get(codigo)).append(" then ").append(codigo);
        }
        sb.append(" else 10000 end ) as ordem,  ");
        sb.append(" departamento.nome as serie, 'Despesas em ").append(MesAnoEnum.getMesData(dataBase).getMesAbreviado() + "/" + Uteis.getAnoData(dataBase)).append("' as categoria, ");
        sb.append(" case when sum(valor) is null then 0 else sum(valor) end  as valor,");
        if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
            sb.append(" '").append(TipoRelatorioFollowMeEnum.GASTO_POR_DEPARTAMENTO_TODAS_UNIDADES.getValorApresentar().replace("{1}", MesAnoEnum.getMesData(dataBase).getMesAbreviado() + "/" + Uteis.getAnoData(dataBase))).append("' as tituloGrafico ");
        } else {
            sb.append(" '").append(TipoRelatorioFollowMeEnum.GASTO_POR_DEPARTAMENTO_UNIDADE_ESPECIFICA.getValorApresentar().replace("{1}", unidadeEnsino.getNome()).replace("{2}", MesAnoEnum.getMesData(dataBase).getMesAbreviado() + "/" + Uteis.getAnoData(dataBase))).append("' as tituloGrafico ");
        }
        sb.append(" from   departamento");
        sb.append(" inner join contapagar on departamento.codigo = contapagar.departamento and ");
        sb.append(realizarGeracaoWherePeriodo(dataBase, Uteis.getDataUltimoDiaMes(dataBase), "datavencimento", false));
        sb.append(" and contapagar.situacao in ('AP', 'PP', 'PA') ");
        sb.append(" and departamento.codigo in (-1 ");
        for (FollowMeDepartamentoVO obj : followMeDepartamentoVOs) {
            if (obj.getSelecionado()) {
                sb.append(",  ").append(obj.getDepartamento().getCodigo());
            }
        }
        sb.append(" ) ");
        if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and contapagar.unidadeEnsino =  ").append(unidadeEnsino.getCodigo());
        }
        sb.append(" group by departamento.nome, departamento.codigo");
        if (apresentarDespesaDepartamentoNaoInformado) {
            sb.append(" union all");

            sb.append(" select 1000 as ordem,");
            sb.append(" 'Dep. Não Inf.' as serie, 'Despesas em ").append(MesAnoEnum.getMesData(dataBase).getMesAbreviado() + "/" + Uteis.getAnoData(dataBase)).append("' as categoria,");
            sb.append(" case when sum(valor) is null then 0 else sum(valor) end  as valor,");
            if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.GASTO_POR_DEPARTAMENTO_TODAS_UNIDADES.getValorApresentar().replace("{1}", MesAnoEnum.getMesData(dataBase).getMesAbreviado() + "/" + Uteis.getAnoData(dataBase))).append("' as tituloGrafico ");
            } else {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.GASTO_POR_DEPARTAMENTO_UNIDADE_ESPECIFICA.getValorApresentar().replace("{1}", unidadeEnsino.getNome()).replace("{2}", MesAnoEnum.getMesData(dataBase).getMesAbreviado() + "/" + Uteis.getAnoData(dataBase))).append("' as tituloGrafico ");
            }
            sb.append(" from   contapagar where departamento is null and ");
            sb.append(realizarGeracaoWherePeriodo(dataBase, Uteis.getDataUltimoDiaMes(dataBase), "datavencimento", false));
            sb.append(" and contapagar.situacao in ('AP', 'PP', 'PA') ");
            if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
                sb.append(" and contapagar.unidadeEnsino =  ").append(unidadeEnsino.getCodigo());
            }
        }
        return sb;
    }

    private StringBuilder realizarMontagemSqlGastoPorDepartamentoGeral(Date dataInicio, Date dataTermino, Map<Integer, Integer> ordenador, UnidadeEnsinoVO unidadeEnsino, List<FollowMeDepartamentoVO> followMeDepartamentoVOs, Boolean apresentarDespesaDepartamentoNaoInformado) throws Exception {
        StringBuilder sb = new StringBuilder(" select ROW_NUMBER() OVER( order by case departamento.codigo ");
        for (Integer codigo : ordenador.keySet()) {
            sb.append(" when ").append(ordenador.get(codigo)).append(" then ").append(codigo);
        }
        sb.append(" else 10000 end ) as ordem,  ");
        sb.append(" departamento.nome as serie, 'Despesa' as categoria, ");
        sb.append(" case when sum(valor) is null then 0 else sum(valor) end  as valor,");
        if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
            sb.append(" '").append(TipoRelatorioFollowMeEnum.GASTO_POR_DEPARTAMENTO_TODAS_UNIDADES.getValorApresentar().replace("{1}", MesAnoEnum.getMesData(dataInicio).getMesAbreviado() + "/" + Uteis.getAnoData(dataInicio) + " até " + MesAnoEnum.getMesData(dataTermino).getMesAbreviado() + "/" + Uteis.getAnoData(dataTermino))).append("' as tituloGrafico ");
        } else {
            sb.append(" '").append(TipoRelatorioFollowMeEnum.GASTO_POR_DEPARTAMENTO_UNIDADE_ESPECIFICA.getValorApresentar().replace("{1}", unidadeEnsino.getNome()).replace("{2}", MesAnoEnum.getMesData(dataInicio).getMesAbreviado() + "/" + Uteis.getAnoData(dataInicio) + " até " + MesAnoEnum.getMesData(dataTermino).getMesAbreviado() + "/" + Uteis.getAnoData(dataTermino))).append("' as tituloGrafico ");
        }
        sb.append(" from   departamento");
        sb.append(" inner join contapagar on departamento.codigo = contapagar.departamento and ");
        sb.append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "datavencimento", false));
        sb.append(" and contapagar.situacao in ('AP', 'PP', 'PA') ");
        if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and contapagar.unidadeEnsino =  ").append(unidadeEnsino.getCodigo());
        }
        sb.append(" and departamento.codigo in (-1 ");
        for (FollowMeDepartamentoVO obj : followMeDepartamentoVOs) {
            if (obj.getSelecionado()) {
                sb.append(",  ").append(obj.getDepartamento().getCodigo());
            }
        }
        sb.append(" ) ");
        sb.append(" group by departamento.nome, departamento.codigo");
        if (apresentarDespesaDepartamentoNaoInformado) {
            sb.append(" union all");

            sb.append(" select 1000 as ordem,");
            sb.append(" 'Dep. Não Inf.' as serie, 'Despesa' as categoria,");
            sb.append(" case when sum(valor) is null then 0 else sum(valor) end  as valor,");
            if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.GASTO_POR_DEPARTAMENTO_TODAS_UNIDADES.getValorApresentar().replace("{1}", MesAnoEnum.getMesData(dataInicio).getMesAbreviado() + "/" + Uteis.getAnoData(dataInicio) + " até " + MesAnoEnum.getMesData(dataTermino).getMesAbreviado() + "/" + Uteis.getAnoData(dataTermino))).append("' as tituloGrafico ");
            } else {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.GASTO_POR_DEPARTAMENTO_UNIDADE_ESPECIFICA.getValorApresentar().replace("{1}", unidadeEnsino.getNome()).replace("{2}", MesAnoEnum.getMesData(dataInicio).getMesAbreviado() + "/" + Uteis.getAnoData(dataInicio) + " até " + MesAnoEnum.getMesData(dataTermino).getMesAbreviado() + "/" + Uteis.getAnoData(dataTermino))).append("' as tituloGrafico ");
            }
            sb.append(" from   contapagar where departamento is null and ");
            sb.append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "datavencimento", false));
            sb.append(" and contapagar.situacao in ('AP', 'PP', 'PA') ");
            if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
                sb.append(" and contapagar.unidadeEnsino =  ").append(unidadeEnsino.getCodigo());
            }
        }
        return sb;
    }

    private StringBuilder realizarMontagemSqlGastoPorCategoriaDespesaAgrupado(Date dataInicio, Date dataTermino, Map<Integer, Integer> ordenador, UnidadeEnsinoVO unidadeEnsino, List<FollowMeCategoriaDespesaVO> followMeCategoriaDespesaVOs) throws Exception {
        StringBuilder sb = new StringBuilder(" select ROW_NUMBER() OVER( order by case categoriadespesa.codigo ");
        for (Integer codigo : ordenador.keySet()) {
            sb.append(" when ").append(ordenador.get(codigo)).append(" then ").append(codigo);
        }
        sb.append(" else 10000 end ) as ordem,  ");
        sb.append(" categoriadespesa.descricao as serie, 'Despesa Por Categoria' as categoria, ");
        sb.append(" case when sum(valor) is null then 0 else sum(valor) end  as valor,");
        if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
            sb.append(" '").append(TipoRelatorioFollowMeEnum.CATEGORIA_DESPESA_TODAS_UNIDADES_AGRUPADO.getValorApresentar().replace("{1}", MesAnoEnum.getMesData(dataInicio).getMesAbreviado() + "/" + Uteis.getAnoData(dataInicio) + " até " + MesAnoEnum.getMesData(dataTermino).getMesAbreviado() + "/" + Uteis.getAnoData(dataTermino))).append("' as tituloGrafico ");
        } else {
            sb.append(" '").append(TipoRelatorioFollowMeEnum.CATEGORIA_DESPESA_UNIDADE_ESPECIFICA_AGRUPADO.getValorApresentar().replace("{1}", unidadeEnsino.getNome()).replace("{2}", MesAnoEnum.getMesData(dataInicio).getMesAbreviado() + "/" + Uteis.getAnoData(dataInicio) + " até " + MesAnoEnum.getMesData(dataTermino).getMesAbreviado() + "/" + Uteis.getAnoData(dataTermino))).append("' as tituloGrafico ");
        }
        sb.append(" from   categoriadespesa");
        sb.append(" inner join contapagar on categoriadespesa.codigo = contapagar.centrodespesa where ");
        sb.append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "datavencimento", false));
        sb.append(" and contapagar.situacao in ('AP', 'PP', 'PA') ");
        if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and contapagar.unidadeEnsino =  ").append(unidadeEnsino.getCodigo());
        }
        sb.append(" and categoriadespesa.codigo in (0");
        for (FollowMeCategoriaDespesaVO followMeCategoriaDespesaVO : followMeCategoriaDespesaVOs) {
            if (followMeCategoriaDespesaVO.getSelecionado()) {
                sb.append(", ").append(followMeCategoriaDespesaVO.getCategoriaDespesa().getCodigo());
            }
        }
        sb.append(") ");
        sb.append(" group by categoriadespesa.descricao, categoriadespesa.codigo");

        // sb.append(" union all");
        //
        // sb.append(" select 1000 as ordem,");
        // sb.append(" 'Dep. Não Inf.' as serie, 'Despesa Por Categoria' as categoria,");
        // sb.append(" case when sum(valor) is null then 0 else sum(valor) end  as valor,");
        // if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
        // sb.append(" '").append(TipoRelatorioFollowMeEnum.CATEGORIA_DESPESA_TODAS_UNIDADES_AGRUPADO.getValorApresentar().replace("{1}", Uteis.getData(dataInicio, "MM/yy") +
        // " até " + Uteis.getData(dataTermino, "MM/yy"))).append("' as tituloGrafico ");
        // } else {
        // sb.append(" '").append(TipoRelatorioFollowMeEnum.CATEGORIA_DESPESA_UNIDADE_ESPECIFICA_AGRUPADO.getValorApresentar().replace("{1}", Uteis.getData(dataInicio, "MM/yy") +
        // " até " + Uteis.getData(dataTermino, "MM/yy"))).append("' as tituloGrafico ");
        // }
        // sb.append(" from   contapagar where centrodespesa is null and ");
        // sb.append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "datavencimento", false));
        // sb.append(" and contapagar.situacao in ('AP', 'PP', 'PA') ");
        // if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
        // sb.append(" and contapagar.unidadeEnsino =  ").append(unidadeEnsino.getCodigo());
        // }
        return sb;
    }

    private StringBuilder realizarMontagemSqlGastoPorCategoriaDespesaPadraoAlterado(Integer ordem, Date dataInicio, Date dataTermino, UnidadeEnsinoVO unidadeEnsino, Double porcentagemAlteracao, Integer qtdeMesesComparativo) throws Exception {
        StringBuilder sb = new StringBuilder("  ");
        sb.append(" select ").append(ordem).append(" as ordem, sum(valor)::NUMERIC(20,2) as valorMesAtual, ");
        sb.append(" ((select sum(valor) from contapagar as cp");
        sb.append(" where cp.centrodespesa = categoriadespesa.codigo");
        sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "cp.dataVencimento::DATE", false));
        if (unidadeEnsino != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and cp.unidadeEnsino = ").append(unidadeEnsino.getCodigo());
        }
        sb.append(" )/ ").append(qtdeMesesComparativo).append(" )::NUMERIC(20,2)as valorMedio,");
        sb.append(" (sum(valor)*100/(select sum(valor)/").append(qtdeMesesComparativo).append(" from contapagar as cp ");
        sb.append(" where cp.centrodespesa = categoriadespesa.codigo");
        sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "cp.dataVencimento::DATE", false));
        if (unidadeEnsino != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and cp.unidadeEnsino = ").append(unidadeEnsino.getCodigo());
        }
        sb.append(" ))::NUMERIC(20,2) as percentualDesviado,");
        sb.append(" categoriadespesa.descricao as categoriaDespesa, ");
        if (unidadeEnsino != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" '").append(TipoRelatorioFollowMeEnum.CATEGORIA_DESPESA_UNIDADE_ESPECIFICA_PADRAO_ALTERADO.getValorApresentar().replace("{1}", unidadeEnsino.getNome())).append("' as titulo ");
        } else {
            sb.append(" '").append(TipoRelatorioFollowMeEnum.CATEGORIA_DESPESA_TODAS_UNIDADES_PADRAO_ALTERADO.getValorApresentar()).append("' as titulo ");
        }
        sb.append(" from categoriadespesa");
        sb.append(" inner join contapagar  on contapagar.centrodespesa = categoriadespesa.codigo");
        sb.append(" where contapagar.situacao in ('AP', 'PP', 'PA')");
        sb.append(" and ").append(realizarGeracaoWherePeriodo(Uteis.getDataPrimeiroDiaMes(dataTermino), dataTermino, "contaPagar.dataVencimento::DATE", false));
        if (unidadeEnsino != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and contapagar.unidadeEnsino = ").append(unidadeEnsino.getCodigo());
        }
        sb.append(" group by categoriadespesa.descricao, categoriadespesa.codigo");
        sb.append(" having sum(valor)*100/(select sum(valor)/").append(qtdeMesesComparativo).append(" from contapagar as cp ");
        sb.append(" where cp.centrodespesa = categoriadespesa.codigo");
        if (unidadeEnsino != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and cp.unidadeEnsino = ").append(unidadeEnsino.getCodigo());
        }
        sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "cp.dataVencimento::DATE", false));
        sb.append(" )  >= ").append(100 + porcentagemAlteracao).append(" or sum(valor)*100/(select sum(valor)/").append(qtdeMesesComparativo).append(" from contapagar as cp");
        sb.append(" where cp.centrodespesa = categoriadespesa.codigo");
        if (unidadeEnsino != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and cp.unidadeEnsino = ").append(unidadeEnsino.getCodigo());
        }
        sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "cp.dataVencimento::DATE", false));
        sb.append(" )  <= ").append(100 - porcentagemAlteracao).append(" ");

        return sb;
    }

    public void consultarReceitaDespesa(FollowMeRelVO followMeRelVO) throws Exception {
        StringBuilder sb = new StringBuilder("");
        int ordem = 1;
        if (followMeRelVO.getFollowMe().getApresentarDadosTodasUnidades()) {
            sb.append(realizarMontagemSqlReceitaDespesa(ordem, followMeRelVO.getDataInicio(), followMeRelVO.getDataTermino(), null, false));
            if (followMeRelVO.getFollowMe().getReceitaDespesaAnoAnterior()) {
                sb.append(" union all ");
                ordem++;
                sb.append(realizarMontagemSqlReceitaDespesa(ordem, Uteis.getDataFutura(followMeRelVO.getDataInicio(), Calendar.YEAR, -1), Uteis.getDataFutura(followMeRelVO.getDataTermino(), Calendar.YEAR, -1), null, true));
            }
        }

        for (FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO : followMeRelVO.getFollowMe().getFollowMeUnidadeEnsinoVOs()) {
            if (followMeUnidadeEnsinoVO.getSelecionar()) {
                ordem++;
                if (!sb.toString().trim().isEmpty()) {
                    sb.append(" union all ");
                }
                sb.append(realizarMontagemSqlReceitaDespesa(ordem, followMeRelVO.getDataInicio(), followMeRelVO.getDataTermino(), followMeUnidadeEnsinoVO.getUnidadeEnsino(), false));
                if (followMeRelVO.getFollowMe().getReceitaDespesaAnoAnterior()) {
                    sb.append(" union all ");
                    ordem++;
                    sb.append(realizarMontagemSqlReceitaDespesa(ordem, Uteis.getDataFutura(followMeRelVO.getDataInicio(), Calendar.YEAR, -1), Uteis.getDataFutura(followMeRelVO.getDataTermino(), Calendar.YEAR, -1), followMeUnidadeEnsinoVO.getUnidadeEnsino(), true));
                }
            }
        }
        if (!sb.toString().trim().isEmpty()) {
            sb.append(" order by ordem, ano, mes");
            montarDadosGraficoFollowMe(followMeRelVO, getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), false);

        }
    }

    private StringBuilder realizarMontagemSqlDespesa(Integer ordem, Date dataInicio, Date dataTermino, UnidadeEnsinoVO unidadeEnsino, Boolean anoAnterior) throws Exception {
        StringBuilder sb = new StringBuilder("");
        sb.append(" select " + ordem + " as ordem, sum(trunc(valor::NUMERIC,2)) as valor, tipo as serie, ");
        sb.append(" case mes when 1 then 'JAN' when 2 then 'FEV' when 3 then 'MAR' when 4 then 'ABR' ");
        sb.append(" when 5 then 'MAI' when 6 then 'JUN' when 7 then 'JUL' when 8 then 'AGO' ");
        sb.append(" when 9 then 'SET' when 10 then 'OUT' when 11 then 'NOV' when 12 then 'DEZ' end ");
        sb.append("  ||'/'||ano::VARCHAR as categoria,  ano, mes, ");
        if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
            if (anoAnterior) {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.DESPESA_TODAS_UNIDADES_PERIODO_ANTERIOR.getValorApresentar()).append("' as tituloGrafico");
            } else {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.DESPESA_TODAS_UNIDADES.getValorApresentar()).append("' as tituloGrafico");
            }
        } else {
            if (anoAnterior) {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.DESPESA_UNIDADE_ESPECIFICA_PERIODO_ANTERIOR.getValorApresentar().replace("{1}", unidadeEnsino.getNome())).append("' as tituloGrafico");
            } else {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.DESPESA_UNIDADE_ESPECIFICA.getValorApresentar().replace("{1}", unidadeEnsino.getNome())).append("' as tituloGrafico");
            }
        }
        sb.append("  from (");
        sb.append(" select distinct contapagar.codigo, valorPago as valor, '").append(ReceitaDespesaEnum.DESPESA.toString()).append("'::VARCHAR as tipo, extract(year from max(negociacaopagamento.data))::INT as ano,");
        sb.append(" extract(month from max(negociacaopagamento.data))::INT as mes");
        sb.append(" from contapagar ");
        sb.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.contapagar = contapagar.codigo  ");
        sb.append(" inner join negociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo");
        sb.append(" where  situacao = 'PA' and negociacaopagamento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and negociacaopagamento.data <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");

        if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and contapagar.unidadeEnsino =  ").append(unidadeEnsino.getCodigo());
        }

        sb.append(" group by contapagar.codigo, valorPago) as t group by ano, mes, tipo  ");
        return sb;
    }

    public void consultarDespesa(FollowMeRelVO followMeRelVO) throws Exception {
        StringBuilder sb = new StringBuilder("");
        int ordem = 1;
        if (followMeRelVO.getFollowMe().getApresentarDadosTodasUnidades()) {
            sb.append(realizarMontagemSqlDespesa(ordem, followMeRelVO.getDataInicio(), followMeRelVO.getDataTermino(), null, false));
            if (followMeRelVO.getFollowMe().getGerarGraficoDespesaAnoAnterior()) {
                sb.append(" union all ");
                ordem++;
                sb.append(realizarMontagemSqlDespesa(ordem, Uteis.getDataFutura(followMeRelVO.getDataInicio(), Calendar.YEAR, -1), Uteis.getDataFutura(followMeRelVO.getDataTermino(), Calendar.YEAR, -1), null, true));
            }
        }

        for (FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO : followMeRelVO.getFollowMe().getFollowMeUnidadeEnsinoVOs()) {
            if (followMeUnidadeEnsinoVO.getSelecionar()) {
                ordem++;
                if (!sb.toString().trim().isEmpty()) {
                    sb.append(" union all ");
                }
                sb.append(realizarMontagemSqlDespesa(ordem, followMeRelVO.getDataInicio(), followMeRelVO.getDataTermino(), followMeUnidadeEnsinoVO.getUnidadeEnsino(), false));
                if (followMeRelVO.getFollowMe().getGerarGraficoDespesaAnoAnterior()) {
                    sb.append(" union all ");
                    ordem++;
                    sb.append(realizarMontagemSqlDespesa(ordem, Uteis.getDataFutura(followMeRelVO.getDataInicio(), Calendar.YEAR, -1), Uteis.getDataFutura(followMeRelVO.getDataTermino(), Calendar.YEAR, -1), followMeUnidadeEnsinoVO.getUnidadeEnsino(), true));
                }
            }
        }
        if (!sb.toString().trim().isEmpty()) {
            sb.append(" order by ordem, serie desc, ano, mes");
            montarDadosGraficoFollowMe(followMeRelVO, getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), false);
        }
    }

    private StringBuilder realizarMontagemSqlReceitaDespesa(Integer ordem, Date dataInicio, Date dataTermino, UnidadeEnsinoVO unidadeEnsino, Boolean anoAnterior) throws Exception {
        StringBuilder sb = new StringBuilder(" select " + ordem + " as ordem, sum(trunc(valor::NUMERIC,2)) as valor, tipo as serie, ");
        sb.append(" case mes when 1 then 'JAN' when 2 then 'FEV' when 3 then 'MAR' when 4 then 'ABR' ");
        sb.append(" when 5 then 'MAI' when 6 then 'JUN' when 7 then 'JUL' when 8 then 'AGO' ");
        sb.append(" when 9 then 'SET' when 10 then 'OUT' when 11 then 'NOV' when 12 then 'DEZ' end ");
        sb.append("  ||'/'||ano::VARCHAR as categoria ,  ano, mes, ");

        if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
            if (anoAnterior) {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.RECEITA_DESPESA_TODAS_UNIDADES_PERIODO_ANTERIOR.getValorApresentar()).append("' as tituloGrafico");
            } else {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.RECEITA_DESPESA_TODAS_UNIDADES.getValorApresentar()).append("' as tituloGrafico");
            }
        } else {
            if (anoAnterior) {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.RECEITA_DESPESA_UNIDADE_ESPECIFICA_PERIODO_ANTERIOR.getValorApresentar().replace("{1}", unidadeEnsino.getNome())).append("' as tituloGrafico");
            } else {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.RECEITA_DESPESA_UNIDADE_ESPECIFICA.getValorApresentar().replace("{1}", unidadeEnsino.getNome())).append("' as tituloGrafico");
            }
        }

        sb.append("  from (");
        sb.append(" select distinct contareceber.codigo, valorRecebido as valor, '").append(ReceitaDespesaEnum.RECEITA.toString()).append("'::VARCHAR as tipo, extract(year from max(negociacaorecebimento.data))::INT as ano,");
        sb.append(" extract(month from max(negociacaorecebimento.data))::INT as mes");
        sb.append(" from contareceber ");
        sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo  ");
        sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo");

        sb.append(" where  situacao = 'RE' and negociacaorecebimento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and negociacaorecebimento.data <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");

        if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and contareceber.unidadeEnsino =  ").append(unidadeEnsino.getCodigo());
        }

        sb.append(" group by contareceber.codigo, valorRecebido) as t group by ano, mes, tipo");
        sb.append(" union all ");
        sb.append(" select " + ordem + " as ordem, sum(trunc(valor::NUMERIC,2)) as valor, tipo as serie, ");
        sb.append(" case mes when 1 then 'JAN' when 2 then 'FEV' when 3 then 'MAR' when 4 then 'ABR' ");
        sb.append(" when 5 then 'MAI' when 6 then 'JUN' when 7 then 'JUL' when 8 then 'AGO' ");
        sb.append(" when 9 then 'SET' when 10 then 'OUT' when 11 then 'NOV' when 12 then 'DEZ' end ");
        sb.append("  ||'/'||ano::VARCHAR as categoria,  ano, mes, ");
        if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
            if (anoAnterior) {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.RECEITA_DESPESA_TODAS_UNIDADES_PERIODO_ANTERIOR.getValorApresentar()).append("' as tituloGrafico");
            } else {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.RECEITA_DESPESA_TODAS_UNIDADES.getValorApresentar()).append("' as tituloGrafico");
            }
        } else {
            if (anoAnterior) {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.RECEITA_DESPESA_UNIDADE_ESPECIFICA_PERIODO_ANTERIOR.getValorApresentar().replace("{1}", unidadeEnsino.getNome())).append("' as tituloGrafico");
            } else {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.RECEITA_DESPESA_UNIDADE_ESPECIFICA.getValorApresentar().replace("{1}", unidadeEnsino.getNome())).append("' as tituloGrafico");
            }
        }
        sb.append("  from (");
        sb.append(" select distinct contapagar.codigo, valorPago as valor, '").append(ReceitaDespesaEnum.DESPESA.toString()).append("'::VARCHAR as tipo, extract(year from max(negociacaopagamento.data))::INT as ano,");
        sb.append(" extract(month from max(negociacaopagamento.data))::INT as mes");
        sb.append(" from contapagar ");
        sb.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.contapagar = contapagar.codigo  ");
        sb.append(" inner join negociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo");
        sb.append(" where  situacao = 'PA' and negociacaopagamento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and negociacaopagamento.data <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");

        if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and contapagar.unidadeEnsino =  ").append(unidadeEnsino.getCodigo());
        }

        sb.append(" group by contapagar.codigo, valorPago) as t group by ano, mes, tipo  ");
        return sb;
    }

    public void consultarReceitaDespesaPorNivelEducacional(FollowMeRelVO followMeRelVO) throws Exception {
        StringBuilder sb = new StringBuilder("");
        int ordem = 0;
        for (TipoNivelEducacional tipoNivelEducacional : TipoNivelEducacional.values()) {
            if (followMeRelVO.getFollowMe().getApresentarDadosTodasUnidades()) {
                if (!sb.toString().trim().isEmpty()) {
                    sb.append(" union all ");
                }
                sb.append(realizarMontagemSqlReceitaDespesaPorNivelEducacional(ordem++, followMeRelVO.getDataInicio(), followMeRelVO.getDataTermino(), tipoNivelEducacional, null, false, followMeRelVO.getFollowMe().getReceitaDespesaPorNivelEducacionalMesMes()));
                if (followMeRelVO.getFollowMe().getReceitaDespesaPorNivelEducacionalAnoAnterior()) {
                    sb.append(" union all ");
                    sb.append(realizarMontagemSqlReceitaDespesaPorNivelEducacional(ordem++, Uteis.getDataFutura(followMeRelVO.getDataInicio(), Calendar.YEAR, -1), Uteis.getDataFutura(followMeRelVO.getDataTermino(), Calendar.YEAR, -1), tipoNivelEducacional, null, true, followMeRelVO.getFollowMe().getReceitaDespesaPorNivelEducacionalMesMes()));
                }
            }

            for (FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO : followMeRelVO.getFollowMe().getFollowMeUnidadeEnsinoVOs()) {
                if (followMeUnidadeEnsinoVO.getSelecionar()) {
                    if (!sb.toString().trim().isEmpty()) {
                        sb.append(" union all ");
                    }

                    sb.append(realizarMontagemSqlReceitaDespesaPorNivelEducacional(ordem++, followMeRelVO.getDataInicio(), followMeRelVO.getDataTermino(), tipoNivelEducacional, followMeUnidadeEnsinoVO.getUnidadeEnsino(), false, followMeRelVO.getFollowMe().getReceitaDespesaPorNivelEducacionalMesMes()));
                    if (followMeRelVO.getFollowMe().getReceitaDespesaPorNivelEducacionalAnoAnterior()) {
                        sb.append(" union all ");
                        sb.append(realizarMontagemSqlReceitaDespesaPorNivelEducacional(ordem++, Uteis.getDataFutura(followMeRelVO.getDataInicio(), Calendar.YEAR, -1), Uteis.getDataFutura(followMeRelVO.getDataTermino(), Calendar.YEAR, -1), tipoNivelEducacional, followMeUnidadeEnsinoVO.getUnidadeEnsino(), true, followMeRelVO.getFollowMe().getReceitaDespesaPorNivelEducacionalMesMes()));
                    }
                }
            }
        }
        if (!sb.toString().trim().isEmpty()) {
            sb.append(" order by ordem, ano, mes");

            montarDadosGraficoFollowMe(followMeRelVO, getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), false);
        }
    }

    private StringBuilder realizarMontagemSqlReceitaDespesaPorNivelEducacional(Integer ordem, Date dataInicio, Date dataTermino, TipoNivelEducacional tipoNivelEducacional, UnidadeEnsinoVO unidadeEnsino, Boolean anoAnterior, Boolean gerarMesAMes) throws Exception {
        StringBuilder sb = new StringBuilder(" select " + ordem + " as ordem, sum(trunc(valor::NUMERIC,2)) as valor, tipo as serie, ");
        if (gerarMesAMes) {
            sb.append(" case mes when 1 then 'JAN' when 2 then 'FEV' when 3 then 'MAR' when 4 then 'ABR' ");
            sb.append(" when 5 then 'MAI' when 6 then 'JUN' when 7 then 'JUL' when 8 then 'AGO' ");
            sb.append(" when 9 then 'SET' when 10 then 'OUT' when 11 then 'NOV' when 12 then 'DEZ' end ");
            sb.append("  ||'/'||ano::VARCHAR as categoria ,  ano, mes, ");
        } else {
            sb.append("  '").append(tipoNivelEducacional.getDescricao()).append("' as categoria ,  '' as ano, '' as mes, ");
        }

        if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
            if (anoAnterior) {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.RECEITA_DESPESA_POR_NIVEL_EDUCACIONAL_TODAS_UNIDADES_PERIODO_ANTERIOR.getValorApresentar().replace("{1}", tipoNivelEducacional.getDescricao())).append("' as tituloGrafico");
            } else {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.RECEITA_DESPESA_POR_NIVEL_EDUCACIONAL_TODAS_UNIDADES.getValorApresentar().replace("{1}", tipoNivelEducacional.getDescricao())).append("' as tituloGrafico");
            }
        } else {
            if (anoAnterior) {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.RECEITA_DESPESA_POR_NIVEL_EDUCACIONAL_UNIDADE_ESPECIFICA_PERIODO_ANTERIOR.getValorApresentar().replace("{1}", unidadeEnsino.getNome()).replace("{2}", tipoNivelEducacional.getDescricao())).append("' as tituloGrafico");
            } else {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.RECEITA_DESPESA_POR_NIVEL_EDUCACIONAL_UNIDADE_ESPECIFICA.getValorApresentar().replace("{1}", unidadeEnsino.getNome()).replace("{2}", tipoNivelEducacional.getDescricao())).append("' as tituloGrafico");
            }
        }

        sb.append("  from (");
        sb.append(" select distinct contareceber.codigo, valorRecebido as valor, '").append(ReceitaDespesaEnum.RECEITA.toString()).append("'::VARCHAR  as tipo, extract(year from max(negociacaorecebimento.data))::INT as ano,");
        sb.append(" extract(month from max(negociacaorecebimento.data))::INT as mes ");
        sb.append(" from contareceber ");
        sb.append(" inner join contarecebernegociacaorecebimento on contarecebernegociacaorecebimento.contareceber = contareceber.codigo  ");
        sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo");
        sb.append(" inner join matricula on matricula.matricula = contareceber.matriculaaluno");
        sb.append(" inner join curso on matricula.curso = curso.codigo ");
        sb.append(" where  contareceber.situacao = 'RE' and negociacaorecebimento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and negociacaorecebimento.data <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
        sb.append(" and curso.nivelEducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
        if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and contareceber.unidadeEnsino =  ").append(unidadeEnsino.getCodigo());
        }

        sb.append(" group by contareceber.codigo, valorRecebido) as t ");
        sb.append(" group by ano, mes, tipo");

        sb.append(" union all ");
        sb.append(" select " + ordem + " as ordem, sum(trunc(valor::NUMERIC,2)) as valor, tipo as serie, ");
        if (gerarMesAMes) {
            sb.append(" case mes when 1 then 'JAN' when 2 then 'FEV' when 3 then 'MAR' when 4 then 'ABR' ");
            sb.append(" when 5 then 'MAI' when 6 then 'JUN' when 7 then 'JUL' when 8 then 'AGO' ");
            sb.append(" when 9 then 'SET' when 10 then 'OUT' when 11 then 'NOV' when 12 then 'DEZ' end ");
            sb.append("  ||'/'||ano::VARCHAR as categoria,  ano, mes, ");
        } else {
            sb.append("  '").append(tipoNivelEducacional.getDescricao()).append("' ,  '' as ano, '' as mes, ");
        }

        if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
            if (anoAnterior) {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.RECEITA_DESPESA_POR_NIVEL_EDUCACIONAL_TODAS_UNIDADES_PERIODO_ANTERIOR.getValorApresentar().replace("{1}", tipoNivelEducacional.getDescricao())).append("' as tituloGrafico");
            } else {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.RECEITA_DESPESA_POR_NIVEL_EDUCACIONAL_TODAS_UNIDADES.getValorApresentar().replace("{1}", tipoNivelEducacional.getDescricao())).append("' as tituloGrafico");
            }
        } else {
            if (anoAnterior) {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.RECEITA_DESPESA_POR_NIVEL_EDUCACIONAL_UNIDADE_ESPECIFICA_PERIODO_ANTERIOR.getValorApresentar().replace("{1}", unidadeEnsino.getNome()).replace("{2}", tipoNivelEducacional.getDescricao())).append("' as tituloGrafico");
            } else {
                sb.append(" '").append(TipoRelatorioFollowMeEnum.RECEITA_DESPESA_POR_NIVEL_EDUCACIONAL_UNIDADE_ESPECIFICA.getValorApresentar().replace("{1}", unidadeEnsino.getNome()).replace("{2}", tipoNivelEducacional.getDescricao())).append("' as tituloGrafico");
            }
        }

        sb.append("  from (");
        sb.append(" select distinct contapagar.codigo, valorPago as valor, '").append(ReceitaDespesaEnum.DESPESA.toString()).append("'::VARCHAR  as tipo, extract(year from max(negociacaopagamento.data))::INT as ano,");
        sb.append(" extract(month from max(negociacaopagamento.data))::INT as mes");
        sb.append(" from contapagar ");
        sb.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.contapagar = contapagar.codigo  ");
        sb.append(" inner join negociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo");
        sb.append(" inner join curso on curso.codigo = contapagar.curso");
        sb.append(" where  contapagar.situacao = 'PA' and negociacaopagamento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and negociacaopagamento.data <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");
        sb.append(" and curso.nivelEducacional = '").append(tipoNivelEducacional.getValor()).append("' ");
        if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and contapagar.unidadeEnsino =  ").append(unidadeEnsino.getCodigo());
        }

        sb.append(" group by contapagar.codigo, valorPago) as t group by ano, mes, tipo  ");

        return sb;
    }

    public void consultarPosicaoFinanceira(FollowMeRelVO followMeRelVO) throws Exception {
        Date dataBase = null;
        StringBuilder sb = new StringBuilder("");
        SqlRowSet rs = null;
        Map<Integer, Integer> ordenador = new HashMap<Integer, Integer>();
        try {
            dataBase = followMeRelVO.getDataInicio();
            while (dataBase.compareTo(followMeRelVO.getDataTermino()) <= 0) {
                if (!sb.toString().trim().isEmpty()) {
                    sb.append(" union all ");
                }
                sb.append(realizarMontagemSqlPosicaoFinanceira(dataBase, ordenador));

                if (ordenador.isEmpty()) {
                    int index = 1;
                    sb.append(" order by  ordem, valor desc, serie");
                    rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
                    while (rs.next()) {
                        montarDadosBasicosGraficoFollowMe(followMeRelVO, rs, true);
                        ordenador.put(index++, rs.getInt("contacorrente"));
                    }
                    sb = new StringBuilder("");
                }
                dataBase = Uteis.getDataFutura(dataBase, Calendar.MONTH, 1);
            }
            if (!sb.toString().isEmpty()) {
                montarDadosGraficoFollowMe(followMeRelVO, getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), true);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            dataBase = null;
            sb = null;
            rs = null;

        }
    }

    private StringBuilder realizarMontagemSqlPosicaoFinanceira(Date dataBase, Map<Integer, Integer> ordenador) throws Exception {
        StringBuilder sb = new StringBuilder(" ");

        if (ordenador != null && !ordenador.isEmpty()) {
            sb.append(" select ROW_NUMBER() OVER( order by case t.contacorrente ");
            for (Integer codigo : ordenador.keySet()) {
                sb.append(" when ").append(ordenador.get(codigo)).append(" then ").append(codigo);
            }
            sb.append(" else 10000 end ) as ordem,  ");
        } else {
            sb.append(" select 1 as ordem, ");
        }
        sb.append(" trunc((case when t2.contacorrente is null then 0.0 else t2.saldo end) + (sum(entrada) - sum(saida))::NUMERIC, 2) as valor, serie, categoria, mes, ano, t.contacorrente,");
        sb.append(" '").append(TipoRelatorioFollowMeEnum.POSICAO_FINANCEIRA.getValorApresentar().replace("{1}", Uteis.getData(dataBase, "MM/yy"))).append("' as tituloGrafico ");
        sb.append(" from (");
        sb.append(" select case when tipomovimentacaofinanceira = 'SAIDA' then sum(valor) else 0.0 end as saida,");
        sb.append(" case when tipomovimentacaofinanceira = 'ENTRADA' then sum(valor) else 0.0 end as entrada,");
        sb.append(" banco.nome ||': '||contacorrente.numero||'-'||contacorrente.digito  as serie,");
        sb.append(" case extract(month from extratocontacorrente.data)::INT when 1 then 'JAN' when 2 then 'FEV' when 3 then 'MAR' when 4 then 'ABR'");
        sb.append(" when 5 then 'MAI' when 6 then 'JUN' when 7 then 'JUL' when 8 then 'AGO'");
        sb.append(" when 9 then 'SET' when 10 then 'OUT' when 11 then 'NOV' when 12 then 'DEZ' end");
        sb.append(" ||'/'||extract(year from extratocontacorrente.data)::INT::VARCHAR as categoria,");
        sb.append(" extract(month from data) as mes,");
        sb.append(" extract(year from data) as ano,");
        sb.append(" contacorrente.codigo as contacorrente");
        sb.append(" from extratocontacorrente");
        sb.append(" inner join contacorrente on contacorrente.codigo =  extratocontacorrente.contacorrente");
        sb.append(" inner join agencia on contacorrente.agencia =  agencia.codigo");
        sb.append(" inner join banco on banco.codigo =  agencia.banco");
        sb.append(" where ").append(realizarGeracaoWherePeriodo(dataBase, Uteis.getDataUltimoDiaMes(dataBase), "data", true));
        sb.append(" group by  data, banco.nome, tipomovimentacaofinanceira, contacorrente.numero, contacorrente.digito, contacorrente.codigo");
        sb.append(" ) as t");
        sb.append(" left join (");
        sb.append(" select trunc((sum(entrada) - sum(saida))::NUMERIC, 2) saldo,  contacorrente from (");
        sb.append(" select case when tipomovimentacaofinanceira = 'SAIDA' then sum(valor) else 0.0 end as saida,");
        sb.append(" case when tipomovimentacaofinanceira = 'ENTRADA' then sum(valor) else 0.0 end as entrada,");
        sb.append(" contacorrente");
        sb.append(" from extratocontacorrente");
        sb.append(" where data::DATE  < '" + Uteis.getDataJDBC(dataBase) + "'");
        sb.append(" group by  data, tipomovimentacaofinanceira, contacorrente");
        sb.append(" ) as saldoAnterior group by contacorrente) as t2 on t.contacorrente = t2.contacorrente");
        sb.append(" group by serie, categoria, mes, ano, t.contacorrente, t2.contacorrente, t2.saldo");

        sb.append(" union all");

        sb.append(" select 10000 as ordem, ");
        sb.append(" sum(saldoFinal) as valor, 'Caixas'::VARCHAR as serie,");
        sb.append(" case extract(month from dataabertura)::INT when 1 then 'JAN' when 2 then 'FEV' when 3 then 'MAR' when 4 then 'ABR'");
        sb.append(" when 5 then 'MAI' when 6 then 'JUN' when 7 then 'JUL' when 8 then 'AGO'");
        sb.append(" when 9 then 'SET' when 10 then 'OUT' when 11 then 'NOV' when 12 then 'DEZ' end");
        sb.append(" ||'/'||extract(year from dataabertura)::INT::VARCHAR as categoria,");
        sb.append(" extract(month from dataabertura) as mes,");
        sb.append(" extract(year from dataabertura) as ano, 0 as contacorrente, ");
        sb.append(" '").append(TipoRelatorioFollowMeEnum.POSICAO_FINANCEIRA.getValorApresentar().replace("{1}", Uteis.getData(dataBase, "MM/yy"))).append("' as tituloGrafico ");
        sb.append(" from fluxocaixa");
        sb.append(" where fluxocaixa.codigo in (");
        sb.append(" select max(codigo) as codigo");
        sb.append(" from fluxocaixa");
        sb.append(" where ").append(realizarGeracaoWherePeriodo(dataBase, Uteis.getDataUltimoDiaMes(dataBase), "dataabertura", true));
        sb.append(" group by  extract(year from dataabertura) ,");
        sb.append(" extract(month from dataabertura),");       
        sb.append(" contacaixa");
        sb.append(" ) group by extract(year from dataabertura), extract(month from dataabertura), ");
        sb.append(" case extract(month from dataabertura)::INT when 1 then 'JAN' when 2 then 'FEV' when 3 then 'MAR' when 4 then 'ABR'");
        sb.append(" when 5 then 'MAI' when 6 then 'JUN' when 7 then 'JUL' when 8 then 'AGO'");
        sb.append(" when 9 then 'SET' when 10 then 'OUT' when 11 then 'NOV' when 12 then 'DEZ' end");
        sb.append(" ||'/'||extract(year from dataabertura)::INT::VARCHAR");
        return sb;
    }

    public void consultarInandimplencia(FollowMeRelVO followMeRelVO) throws Exception {
        StringBuilder sb = new StringBuilder("");
        int ordem = 1;
        if (followMeRelVO.getFollowMe().getApresentarDadosTodasUnidades()) {
            sb.append(realizarMontagemSqlInadimplencia(ordem, followMeRelVO.getDataInicio(), followMeRelVO.getDataTermino(), null));
        }
        for (FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO : followMeRelVO.getFollowMe().getFollowMeUnidadeEnsinoVOs()) {
            if (followMeUnidadeEnsinoVO.getSelecionar()) {
                ordem++;
                if (!sb.toString().trim().isEmpty()) {
                    sb.append(" union all ");
                }
                sb.append(realizarMontagemSqlInadimplencia(ordem, followMeRelVO.getDataInicio(), followMeRelVO.getDataTermino(), followMeUnidadeEnsinoVO.getUnidadeEnsino()));
            }
        }

        if (!sb.toString().isEmpty()) {
            sb.append(" order by ordem, ano, mes");
            montarDadosGraficoFollowMe(followMeRelVO, getConexao().getJdbcTemplate().queryForRowSet(sb.toString()), false);
        }
    }

    private StringBuilder realizarMontagemSqlInadimplencia(Integer ordem, Date dataInicio, Date dataTermino, UnidadeEnsinoVO unidadeEnsino) throws Exception {
        StringBuilder sb = new StringBuilder(" ");
        sb.append(" select " + ordem + " as ordem, sum(trunc(contareceber.valor::NUMERIC,2)) as valor, 'Inadimplência' as serie, ");
        sb.append(" case extract(month from datavencimento)::INT when 1 then 'JAN' when 2 then 'FEV' when 3 then 'MAR' when 4 then 'ABR' ");
        sb.append(" when 5 then 'MAI' when 6 then 'JUN' when 7 then 'JUL' when 8 then 'AGO' ");
        sb.append(" when 9 then 'SET' when 10 then 'OUT' when 11 then 'NOV' when 12 then 'DEZ' end ");
        sb.append("  ||'/'||extract(year from datavencimento)::INT::VARCHAR as categoria,");
        sb.append("  extract(year from datavencimento)::INT as ano, ");
        sb.append("  extract(month from datavencimento)::INT as mes,");
        if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
            sb.append(" '").append(TipoRelatorioFollowMeEnum.INADIMPLENCIA_TODAS_UNIDADES.getValorApresentar()).append("' as tituloGrafico");
        } else {
            sb.append(" '").append(TipoRelatorioFollowMeEnum.INADIMPLENCIA_UNIDADE_ESPECIFICA.getValorApresentar().replace("{1}", unidadeEnsino.getNome())).append("' as tituloGrafico");
        }
        sb.append(" from contaReceber");
        sb.append(" where ").append(realizarGeracaoWherePeriodo(dataInicio, Uteis.getDataUltimoDiaMes(dataTermino), "datavencimento", false));
        sb.append(" and contaReceber.situacao = 'AR' and datavencimento < current_date");
        // sb.append(" or (contaReceber.situacao = 'RE' and datavencimento < (select max(data) from contarecebernegociacaorecebimento");
        // sb.append(" inner join negociacaorecebimento on contarecebernegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo");
        // sb.append(" where contarecebernegociacaorecebimento.contareceber = contareceber.codigo  ) ))");
        if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and  contaReceber.unidadeensino = ").append(unidadeEnsino.getCodigo());
        }
        sb.append(" group by extract(year from datavencimento)::INT , extract(month from datavencimento)::INT, ");
        sb.append(" case extract(month from datavencimento)::INT when 1 then 'JAN' when 2 then 'FEV' when 3 then 'MAR' when 4 then 'ABR' ");
        sb.append(" when 5 then 'MAI' when 6 then 'JUN' when 7 then 'JUL' when 8 then 'AGO' ");
        sb.append(" when 9 then 'SET' when 10 then 'OUT' when 11 then 'NOV' when 12 then 'DEZ' end ");
        sb.append("  ||'/'||extract(year from datavencimento)::INT::VARCHAR");
        return sb;
    }

    public void consultarRelatorioAcademicoFinanceiro(FollowMeRelVO followMeRelVO) throws Exception {
        followMeRelVO.getFollowMeFinanceiroAcademicoRelVOs().clear();
        StringBuilder sb = new StringBuilder("");
        int ordem = 0;
        if (followMeRelVO.getFollowMe().getApresentarDadosTodasUnidades()) {
            sb.append(realizarMontagemSqlAlunoTurmaVersoDespesa(ordem++, followMeRelVO.getDataInicio(), followMeRelVO.getDataTermino(),
                    null, followMeRelVO.getFollowMe().getApresentarQtdeAlunoRelatorioAcademicoFinanceiro(),
                    followMeRelVO.getFollowMe().getApresentarQtdeTurmaRelatorioAcademicoFinanceiro(),
                    followMeRelVO.getFollowMe().getApresentarDespesaRelatorioAcademicoFinanceiro(),
                    followMeRelVO.getFollowMe().getApresentarValorMedioPorParcelaRelatorioAcademicoFinanceiro(),
                    followMeRelVO.getFollowMe().getApresentarCustoMedioPorAlunoRelatorioAcademicoFinanceiro(),
                    followMeRelVO.getFollowMe().getApresentarInadimplenciaRelatorioAcademicoFinanceiro()));
        }
        for (FollowMeUnidadeEnsinoVO followMeUnidadeEnsinoVO : followMeRelVO.getFollowMe().getFollowMeUnidadeEnsinoVOs()) {
            if (followMeUnidadeEnsinoVO.getSelecionar()) {

                if (!sb.toString().trim().isEmpty()) {
                    sb.append(" union all ");
                }
                sb.append(realizarMontagemSqlAlunoTurmaVersoDespesa(ordem++, followMeRelVO.getDataInicio(),
                        followMeRelVO.getDataTermino(), followMeUnidadeEnsinoVO.getUnidadeEnsino(),
                        followMeRelVO.getFollowMe().getApresentarQtdeAlunoRelatorioAcademicoFinanceiro(),
                        followMeRelVO.getFollowMe().getApresentarQtdeTurmaRelatorioAcademicoFinanceiro(),
                        followMeRelVO.getFollowMe().getApresentarDespesaRelatorioAcademicoFinanceiro(),
                        followMeRelVO.getFollowMe().getApresentarValorMedioPorParcelaRelatorioAcademicoFinanceiro(),
                        followMeRelVO.getFollowMe().getApresentarCustoMedioPorAlunoRelatorioAcademicoFinanceiro(),
                        followMeRelVO.getFollowMe().getApresentarInadimplenciaRelatorioAcademicoFinanceiro()));
            }
        }

        if (!sb.toString().isEmpty()) {
            sb.append(" order by ordem, ano, mes");
            SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
            FollowMeFinanceiroAcademicoRelVO obj = null;
            while (rs.next()) {
                obj = new FollowMeFinanceiroAcademicoRelVO();
                obj.setTitulo(rs.getString("titulo"));
                obj.setQtdeAluno(rs.getInt("qtdeAluno"));
                obj.setQtdeTurma(rs.getInt("qtdeTurma"));
                obj.setAno(rs.getInt("ano"));
                obj.setDespesaMes(rs.getDouble("despesaMes"));
                obj.setInadimplenciaMes(rs.getDouble("inadimplenciaMes"));
                obj.setCustoMedioAluno(rs.getDouble("custoMedioAluno"));
                obj.setValorMedioParcelaAluno(rs.getDouble("valorMedioParcelaAluno"));
                obj.setMesAno(MesAnoEnum.getEnum(String.valueOf(rs.getInt("mes"))));
                followMeRelVO.getFollowMeFinanceiroAcademicoRelVOs().add(obj);
            }
        }
    }

    private StringBuilder realizarMontagemSqlAlunoTurmaVersoDespesa(Integer ordem, Date dataInicio, Date dataTermino,
            UnidadeEnsinoVO unidadeEnsino,
            Boolean apresentarQdeAluno,
            Boolean apresentarQdeTurma,
            Boolean apresentarDespesa,
            Boolean valorMedioPorParcela,
            Boolean custoMedioPorAluno,
            Boolean apresentarInadimplencia) throws Exception {
        StringBuilder sb = new StringBuilder(" ");
        // Junta os dados do Academico
        String titulo = "";
        if (unidadeEnsino == null || unidadeEnsino.getCodigo() == null || unidadeEnsino.getCodigo() == 0) {
            titulo = " '"+TipoRelatorioFollowMeEnum.ACADEMICO_FINANCEIRO_TODAS_UNIDADES.getValorApresentar()+"' ";
        } else {
            titulo = " '"+TipoRelatorioFollowMeEnum.ACADEMICO_FINANCEIRO_UNIDADE_ESPECIFICA.getValorApresentar().replace("{1}", unidadeEnsino.getNome())+"' ";            
        }
        sb.append(" select " + ordem + " as ordem, "+titulo+" as titulo, sum(qtdeAluno)::INT as qtdeAluno, sum(qtdeTurma)::INT as qtdeTurma, ");
        sb.append(" sum(despesaMes) as despesaMes, sum(inadimplenciaMes) as inadimplenciaMes, sum(custoMedioAluno) as custoMedioAluno,");
        sb.append(" sum(valorMedioParcelaAluno) as valorMedioParcelaAluno, ano::INT, mes::INT from ( ");
      
        String union = "";
        if (apresentarQdeAluno != null && apresentarQdeAluno != null) {
            sb.append(realizarMontagemSqlQtdeAluno(dataInicio, dataTermino, unidadeEnsino));
            union = " union all ";
        }
        if (apresentarQdeTurma != null && apresentarQdeTurma) {
            sb.append(union);
            sb.append(realizarMontagemSqlQtdeTurma(dataInicio, dataTermino, unidadeEnsino));
            union = " union all ";
        }
        if (apresentarDespesa != null && apresentarDespesa) {
            sb.append(union);
            sb.append(realizarMontagemSqlDespesaAcademicoFinanceiro(dataInicio, dataTermino, unidadeEnsino));
            union = " union all ";
        }
        if (valorMedioPorParcela != null && valorMedioPorParcela) {
            sb.append(union);
            sb.append(realizarMontagemSqlValorMedioPorParcela(dataInicio, dataTermino, unidadeEnsino));
            union = " union all ";
        }

        if (custoMedioPorAluno != null && custoMedioPorAluno) {
            sb.append(union);
            sb.append(realizarMontagemSqlCustoMedioPorAluno(dataInicio, dataTermino, unidadeEnsino));
            union = " union all ";
        }

        if (apresentarInadimplencia != null && apresentarInadimplencia) {
            sb.append(union);
            sb.append(realizarMontagemSqlInadimplenciaAcademicoFinanceiro(dataInicio, dataTermino, unidadeEnsino));
            union = " union all ";
        }
        sb.append(") as t group by ano, mes");
        return sb;
    }

    private StringBuilder realizarMontagemSqlInadimplenciaAcademicoFinanceiro(Date dataInicio, Date dataTermino, UnidadeEnsinoVO unidadeEnsino) throws Exception {
        StringBuilder sb = new StringBuilder(" ");
        sb.append(" select 0 as qtdeAluno, 0 as qtdeTurma, 0 as despesaMes, sum(trunc(contareceber.valor::NUMERIC,2)) as inadimplenciaMes, 0 as custoMedioAluno, 0 as valorMedioParcelaAluno, ");
        sb.append("  extract(year from datavencimento)::INT as ano, ");
        sb.append("  extract(month from datavencimento)::INT as mes ");
        sb.append(" from contaReceber");
        sb.append(" where ").append(realizarGeracaoWherePeriodo(dataInicio, Uteis.getDataUltimoDiaMes(dataTermino), "datavencimento", false));
        sb.append(" and contaReceber.situacao = 'AR' and datavencimento < current_date");
        if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and  contaReceber.unidadeensino = ").append(unidadeEnsino.getCodigo());
        }
        sb.append(" group by extract(year from datavencimento)::INT , extract(month from datavencimento)::INT");
        return sb;
    }

    private StringBuilder realizarMontagemSqlCustoMedioPorAluno(Date dataInicio, Date dataTermino, UnidadeEnsinoVO unidadeEnsino) throws Exception {
        StringBuilder sb = new StringBuilder(" ");
        int numeroMes = Uteis.getCalculaQuantidadeMesesEntreDatas(dataInicio, dataTermino);
        Date dataInicioTmp = null;
        Date dataTerminoTmp = null;
        sb.append(" select 0 as qtdeAluno, 0 as qtdeTurma, 0 as despesaMes, 0 as inadimplenciaMes, sum(custoMedioAluno) as custoMedioAluno, 0 as valorMedioParcelaAluno, ano, mes from ( ");
        for (int y = 0; y <= numeroMes; y++) {
            if (y == 0) {
                dataInicioTmp = Uteis.getDataVencimentoPadrao(Uteis.getDiaMesData(dataInicio), dataInicio, (y));
            } else {
                sb.append(" union all");
                dataInicioTmp = Uteis.getDataVencimentoPadrao(1, dataInicio, (y));
            }
            if (y == numeroMes) {
                dataTerminoTmp = dataTermino;
            } else {
                dataTerminoTmp = Uteis.getDataUltimoDiaMes(dataInicioTmp);
            }

            sb.append(" select  trunc(sum(trunc(valor::NUMERIC, 2))/ ");
            sb.append(" (select count(distinct matriculaperiodo.matricula) from matriculaperiodo ");
            sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula");
            sb.append(" where ((matriculaperiodo.datafechamentomatriculaperiodo > '" + Uteis.getDataJDBC(dataInicioTmp) + "' and  matriculaperiodo.data <= '" + Uteis.getDataJDBC(dataTerminoTmp) + "' )");
            sb.append(" or  (matriculaperiodo.datafechamentomatriculaperiodo is null and matriculaperiodo.situacaomatriculaperiodo = 'AT' and  matriculaperiodo.data <= '" + Uteis.getDataJDBC(dataTerminoTmp) + "')) ");
            if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
                sb.append(" and  matricula.unidadeensino = ").append(unidadeEnsino.getCodigo());
            }
            sb.append(") , 2) as custoMedioAluno,");
            sb.append(" extract(month from dataVencimento) as mes,    extract(year from dataVencimento) as ano ");
            
            sb.append(" from contapagar");
            sb.append(" where contapagar.situacao <> 'NE'");
            sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicioTmp, dataTerminoTmp, "dataVencimento", false));
            if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
                sb.append(" and  contapagar.unidadeensino = ").append(unidadeEnsino.getCodigo());
            }
            sb.append(" group by extract(month from dataVencimento), extract(year from dataVencimento) ");
        }
        sb.append(" ) as t group by ano, mes");
        return sb;
    }

    private StringBuilder realizarMontagemSqlQtdeAluno(Date dataInicio, Date dataTermino, UnidadeEnsinoVO unidadeEnsino) throws Exception {
        StringBuilder sb = new StringBuilder(" ");
        int numeroMes = Uteis.getCalculaQuantidadeMesesEntreDatas(dataInicio, dataTermino);
        Date dataInicioTmp = null;
        Date dataTerminoTmp = null;
        sb.append(" select count(distinct codigo) as qtdeAluno, 0 as qtdeTurma, 0 as despesaMes, 0 as inadimplenciaMes, 0 as custoMedioAluno, 0 as valorMedioParcelaAluno, ano, mes from ( ");
        for (int y = 0; y <= numeroMes; y++) {
            if (y == 0) {
                dataInicioTmp = Uteis.getDataVencimentoPadrao(Uteis.getDiaMesData(dataInicio), dataInicio, (y));
            } else {
                sb.append(" union all");
                dataInicioTmp = Uteis.getDataVencimentoPadrao(1, dataInicio, (y));
            }
            if (y == numeroMes) {
                dataTerminoTmp = dataTermino;
            } else {
                dataTerminoTmp = Uteis.getDataUltimoDiaMes(dataInicioTmp);
            }
            sb.append(" select distinct matriculaperiodo.matricula  as codigo, extract(month from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as mes,");
            sb.append(" extract(year from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as ano from matriculaperiodo ");
            sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
            sb.append(" where (matricula.dataAlunoConcluiuDisciplinasRegulares is null or dataAlunoConcluiuDisciplinasRegulares::DATE > '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("' ) AND  ((datafechamentomatriculaperiodo > '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("' ");
            sb.append(" and  matriculaperiodo.data <= '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("' ) ");
            sb.append(" or  (datafechamentomatriculaperiodo is null and matriculaperiodo.situacaomatriculaperiodo = 'AT' and matricula.situacao = 'AT' and  matriculaperiodo.data <= '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("')) ");
            if (unidadeEnsino != null && unidadeEnsino.getCodigo() > 0) {
                sb.append(" and matricula.unidadeensino = ").append(unidadeEnsino.getCodigo());
            }
        }
        sb.append(" ) as t group by ano, mes");
        return sb;
    }

    private StringBuilder realizarMontagemSqlQtdeTurma(Date dataInicio, Date dataTermino, UnidadeEnsinoVO unidadeEnsino) throws Exception {
        StringBuilder sb = new StringBuilder(" ");
        int numeroMes = Uteis.getCalculaQuantidadeMesesEntreDatas(dataInicio, dataTermino);
        Date dataInicioTmp = null;
        Date dataTerminoTmp = null;
        sb.append(" select 0 as qtdeAluno, count(distinct codigo)  as qtdeTurma, 0 as despesaMes,  0 as inadimplenciaMes, 0 as custoMedioAluno, 0 as valorMedioParcelaAluno,  ano, mes from ( ");
        for (int y = 0; y <= numeroMes; y++) {
            if (y == 0) {
                dataInicioTmp = Uteis.getDataVencimentoPadrao(Uteis.getDiaMesData(dataInicio), dataInicio, (y));
            } else {
                sb.append(" union all");
                dataInicioTmp = Uteis.getDataVencimentoPadrao(1, dataInicio, (y));
            }
            if (y == numeroMes) {
                dataTerminoTmp = dataTermino;
            } else {
                dataTerminoTmp = Uteis.getDataUltimoDiaMes(dataInicioTmp);
            }

            sb.append(" select distinct matriculaperiodo.turma  as codigo, extract(month from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as mes,");
            sb.append(" extract(year from '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("'::DATE) as ano from matriculaperiodo ");
            sb.append(" inner join matricula on matricula.matricula = matriculaperiodo.matricula ");
            sb.append(" where (matricula.dataAlunoConcluiuDisciplinasRegulares is null or dataAlunoConcluiuDisciplinasRegulares::DATE > '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("' ) AND  ((datafechamentomatriculaperiodo > '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("' ");
            sb.append(" and  matriculaperiodo.data <= '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("' ) ");
            sb.append(" or  (datafechamentomatriculaperiodo is null and matriculaperiodo.situacaomatriculaperiodo = 'AT' and matricula.situacao = 'AT' and  matriculaperiodo.data <= '").append(Uteis.getDataJDBC(dataTerminoTmp)).append("')) ");
            if (unidadeEnsino != null && unidadeEnsino.getCodigo() > 0) {
                sb.append(" and matricula.unidadeensino = ").append(unidadeEnsino.getCodigo());
            }
        }
        sb.append(" ) as t group by ano, mes");
        return sb;
    }

    private StringBuilder realizarMontagemSqlDespesaAcademicoFinanceiro(Date dataInicio, Date dataTermino, UnidadeEnsinoVO unidadeEnsino) throws Exception {
        StringBuilder sb = new StringBuilder(" ");
        sb.append(" select 0 as qtdeAluno, 0 as qtdeTurma, sum(trunc(valor::NUMERIC,2)) as despesaMes,  0 as inadimplenciaMes, 0 as custoMedioAluno, 0 as valorMedioParcelaAluno, ano, mes ");
        sb.append("  from (");
        sb.append(" select distinct contapagar.codigo, valorPago as valor, '").append(ReceitaDespesaEnum.DESPESA.toString()).append("'::VARCHAR as tipo, extract(year from max(negociacaopagamento.data))::INT as ano,");
        sb.append(" extract(month from max(negociacaopagamento.data))::INT as mes");
        sb.append(" from contapagar ");
        sb.append(" inner join contapagarnegociacaopagamento on contapagarnegociacaopagamento.contapagar = contapagar.codigo  ");
        sb.append(" inner join negociacaopagamento on contapagarnegociacaopagamento.negociacaocontapagar = negociacaopagamento.codigo");
        sb.append(" where  situacao = 'PA' and negociacaopagamento.data >= '").append(Uteis.getDataJDBC(dataInicio)).append(" 00:00:00' and negociacaopagamento.data <= '").append(Uteis.getDataJDBC(dataTermino)).append(" 23:59:59'");

        if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and contapagar.unidadeEnsino =  ").append(unidadeEnsino.getCodigo());
        }

        sb.append(" group by contapagar.codigo, valorPago) as t group by ano, mes  ");

        return sb;
    }

    private StringBuilder realizarMontagemSqlValorMedioPorParcela(Date dataInicio, Date dataTermino, UnidadeEnsinoVO unidadeEnsino) throws Exception {
        StringBuilder sb = new StringBuilder(" ");
        sb.append(" select  0 as qtdeAluno, 0 as qtdeTurma, 0 as despesaMes, 0 as inadimplenciaMes, 0 as custoMedioAluno, trunc(sum(trunc(valor::NUMERIC, 2))/count(distinct matriculaaluno), 2) as valorMedioParcelaAluno,");
        sb.append(" extract(year from dataVencimento) as ano, extract(month from dataVencimento) as mes     ");
        sb.append(" from contareceber");
        sb.append(" where tipoorigem  in ('MAT', 'MEN') and situacao <> 'NE'");
        sb.append(" and ").append(realizarGeracaoWherePeriodo(dataInicio, dataTermino, "dataVencimento", false));
        if (unidadeEnsino != null && unidadeEnsino.getCodigo() != null && unidadeEnsino.getCodigo() > 0) {
            sb.append(" and  contaReceber.unidadeensino = ").append(unidadeEnsino.getCodigo());
        }
        sb.append(" group by extract(month from dataVencimento), extract(year from dataVencimento) ");

        return sb;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    private void incluirFollowMeRel(final FollowMeRelVO followMeRelVO) {
        StringBuilder sb = new StringBuilder("SELECT codigo from followMeRel where followme = ");
        sb.append(followMeRelVO.getFollowMe().getCodigo());
        sb.append(" and tipoRelatorioAreaFollowMe = '").append(followMeRelVO.getTipoRelatorioAreaFollowMe().name()).append("' ");
        sb.append(" and dataInicio = '").append(Uteis.getDataJDBC(followMeRelVO.getDataInicio())).append("' ");
        sb.append(" and dataTermino = '").append(Uteis.getDataJDBC(followMeRelVO.getDataTermino())).append("' ");
        if (!getConexao().getJdbcTemplate().queryForRowSet(sb.toString()).next()) {
            final StringBuilder sql = new StringBuilder("INSERT INTO followMeRel (caminhoBaseArquivo, nomeFisicoArquivo, tipoAreaFollowMe, ");
            sql.append(" followMe, dataInicio, dataTermino, dataGeracao, tipoRelatorioAreaFollowMe ");
            sql.append(") VALUES (?,?,?,?,?,?,?, ?) returning codigo");
            followMeRelVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                @Override
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    int x = 1;
                    PreparedStatement preparedStatement = arg0.prepareStatement(sql.toString());
                    preparedStatement.setString(x++, followMeRelVO.getCaminhoBaseArquivo());
                    preparedStatement.setString(x++, followMeRelVO.getNomeFisicoArquivo());
                    preparedStatement.setString(x++, followMeRelVO.getTipoAreaFollowMeEnum().name());
                    preparedStatement.setInt(x++, followMeRelVO.getFollowMe().getCodigo());
                    preparedStatement.setDate(x++, Uteis.getDataJDBC(followMeRelVO.getDataInicio()));
                    preparedStatement.setDate(x++, Uteis.getDataJDBC(followMeRelVO.getDataTermino()));
                    preparedStatement.setDate(x++, Uteis.getDataJDBC(followMeRelVO.getDataGeracao()));
                    preparedStatement.setString(x++, followMeRelVO.getTipoRelatorioAreaFollowMe().name());
                    return preparedStatement;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        followMeRelVO.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
        }

    }

    @Override
    public List<FollowMeRelVO> consultarFollowMeRelPorFollowMe(Integer followMe, Integer limit, Integer pagina) {
        StringBuilder sql = new StringBuilder("SELECT * FROM followMeRel ");
        sql.append(" where followMe =").append(followMe);
        sql.append(" order by dataGeracao desc ");
        if (limit != null && limit > 0) {
            sql.append(" limit ").append(limit).append(" offset ").append(pagina);
        }
        List<FollowMeRelVO> followMeRelVOs = new ArrayList<FollowMeRelVO>(0);
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        while (rs.next()) {
            FollowMeRelVO followMeRelVO = new FollowMeRelVO();
            followMeRelVO.setCodigo(rs.getInt("codigo"));
            followMeRelVO.setCaminhoBaseArquivo(rs.getString("caminhoBaseArquivo"));
            followMeRelVO.setNomeFisicoArquivo(rs.getString("nomeFisicoArquivo"));
            followMeRelVO.setTipoAreaFollowMeEnum(TipoAreaFollowMeEnum.valueOf(rs.getString("tipoAreaFollowMe")));
            followMeRelVO.setTipoRelatorioAreaFollowMe(TipoRelatorioAreaFollowMe.valueOf(rs.getString("tipoRelatorioAreaFollowMe")));
            followMeRelVO.setDataGeracao(rs.getDate("dataGeracao"));
            followMeRelVO.setDataInicio(rs.getDate("dataInicio"));
            followMeRelVO.setDataTermino(rs.getDate("dataTermino"));
            followMeRelVO.getFollowMe().setCodigo(rs.getInt("followMe"));
            followMeRelVO.setNovoObj(false);
            followMeRelVOs.add(followMeRelVO);
        }
        return followMeRelVOs;

    }

    @Override
    public Integer consultarTotalRegistroFollowMeRelPorFollowMe(Integer followMe) {
        StringBuilder sql = new StringBuilder("SELECT count(codigo) as qtde FROM followMeRel ");
        sql.append(" where followMe =").append(followMe);
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (rs.next()) {
            return rs.getInt("qtde");
        }
        return 0;

    }

}
