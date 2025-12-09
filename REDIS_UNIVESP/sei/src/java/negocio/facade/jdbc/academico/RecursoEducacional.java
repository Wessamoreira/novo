package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.RecursoEducacionalVO;
import negocio.comuns.academico.enumeradores.SituacaoRecursoEducacionalEnum;
import negocio.comuns.academico.enumeradores.TipoGraficoEnum;
import negocio.comuns.academico.enumeradores.TipoRecursoEducacionalEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.academico.RecursoEducacionalInterfaceFacade;

@Repository
@Lazy
public class RecursoEducacional extends ControleAcesso implements RecursoEducacionalInterfaceFacade {

    /**
     * 
     */
    private static final long serialVersionUID = -2899381741638120317L;

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(RecursoEducacionalVO recursoEducacionalVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        validarDados(recursoEducacionalVO);
        if (recursoEducacionalVO.isNovoObj()) {
            recursoEducacionalVO.setDataCadastro(new Date());
            recursoEducacionalVO.getUsuarioCadastro().setCodigo(usuario.getCodigo());
            recursoEducacionalVO.getUsuarioCadastro().setNome(usuario.getNome());
            incluir(recursoEducacionalVO, controlarAcesso, usuario);
        } else {
            recursoEducacionalVO.setDataAlteracao(new Date());
            recursoEducacionalVO.getUsuarioAlteracao().setCodigo(usuario.getCodigo());
            recursoEducacionalVO.getUsuarioAlteracao().setNome(usuario.getNome());
            alterar(recursoEducacionalVO, controlarAcesso, usuario);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void incluir(final RecursoEducacionalVO recursoEducacionalVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {

        try {

            final StringBuilder sql = new StringBuilder("INSERT INTO RecursoEducacional ");

            sql.append(" (tipoRecursoEducacional, titulo, texto, altura, largura, ");
            sql.append(" caminhoBaseRepositorio, nomeRealArquivo, nomeFisicoArquivo, conteudoUnidadePagina, dataCadastro, ");
            sql.append(" usuarioCadastro, descricao, disciplina, situacaoRecursoEducacional, conteudoUnidadePaginaRecursoEducacional,  ");
            sql.append(" tituloGrafico, tituloEixoX, tituloEixoY, apresentarLegenda, valorGrafico, tipoGrafico, categoriaGrafico ) ");
            sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
            recursoEducacionalVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    int x = 1;
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setString(x++, recursoEducacionalVO.getTipoRecursoEducacional().toString());
                    sqlInserir.setString(x++, recursoEducacionalVO.getTitulo());
                    sqlInserir.setString(x++, recursoEducacionalVO.getTexto());
                    sqlInserir.setInt(x++, recursoEducacionalVO.getAltura());
                    sqlInserir.setInt(x++, recursoEducacionalVO.getLargura());
                    sqlInserir.setString(x++, recursoEducacionalVO.getCaminhoBaseRepositorio());
                    sqlInserir.setString(x++, recursoEducacionalVO.getNomeRealArquivo());
                    sqlInserir.setString(x++, recursoEducacionalVO.getNomeFisicoArquivo());
                    if (recursoEducacionalVO.getConteudoUnidadePagina().getCodigo() > 0) {
                        sqlInserir.setInt(x++, recursoEducacionalVO.getConteudoUnidadePagina().getCodigo());
                    } else {
                        sqlInserir.setNull(x++, 0);
                    }
                    sqlInserir.setDate(x++, Uteis.getDataJDBC(recursoEducacionalVO.getDataCadastro()));
                    sqlInserir.setInt(x++, recursoEducacionalVO.getUsuarioCadastro().getCodigo());
                    sqlInserir.setString(x++, recursoEducacionalVO.getDescricao());
                    sqlInserir.setInt(x++, recursoEducacionalVO.getDisciplina().getCodigo());
                    sqlInserir.setString(x++, recursoEducacionalVO.getSituacaoRecursoEducacional().name());
                    if (recursoEducacionalVO.getConteudoUnidadePaginaRecursoEducacional().getCodigo() > 0) {
                        sqlInserir.setInt(x++, recursoEducacionalVO.getConteudoUnidadePaginaRecursoEducacional().getCodigo());
                    } else {
                        sqlInserir.setNull(x++, 0);
                    }
                    sqlInserir.setString(x++, recursoEducacionalVO.getTituloGrafico());
                    sqlInserir.setString(x++, recursoEducacionalVO.getTituloEixoX());
                    sqlInserir.setString(x++, recursoEducacionalVO.getTituloEixoY());
                    sqlInserir.setBoolean(x++, recursoEducacionalVO.getApresentarLegenda());
                    sqlInserir.setString(x++, recursoEducacionalVO.getValorGrafico());
                    sqlInserir.setString(x++, recursoEducacionalVO.getTipoGrafico().name());
                    sqlInserir.setString(x++, recursoEducacionalVO.getCategoriaGrafico());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        recursoEducacionalVO.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
        } catch (Exception e) {
            recursoEducacionalVO.setNovoObj(true);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterar(final RecursoEducacionalVO recursoEducacionalVO, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        try {
            final StringBuilder sql = new StringBuilder("UPDATE RecursoEducacional SET ");
            sql.append(" tipoRecursoEducacional = ? , titulo = ? , texto = ? , altura = ? , largura = ? , ");
            sql.append(" caminhoBaseRepositorio = ? , nomeRealArquivo = ? , nomeFisicoArquivo = ? , dataAlteracao = ? ,");
            sql.append(" usuarioAlteracao = ?, descricao = ?, disciplina = ?, situacaoRecursoEducacional = ?, ");
            sql.append(" tituloGrafico = ?, tituloEixoX = ?, tituloEixoY = ?, apresentarLegenda = ?, valorGrafico = ?, tipoGrafico=?, categoriaGrafico =? ");
            sql.append(" where codigo = ? ").append(adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario));
            if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    int x = 1;
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());

                    sqlAlterar.setString(x++, recursoEducacionalVO.getTipoRecursoEducacional().toString());
                    sqlAlterar.setString(x++, recursoEducacionalVO.getTitulo());
                    sqlAlterar.setString(x++, recursoEducacionalVO.getTexto());
                    sqlAlterar.setInt(x++, recursoEducacionalVO.getAltura());
                    sqlAlterar.setInt(x++, recursoEducacionalVO.getLargura());
                    sqlAlterar.setString(x++, recursoEducacionalVO.getCaminhoBaseRepositorio());
                    sqlAlterar.setString(x++, recursoEducacionalVO.getNomeRealArquivo());
                    sqlAlterar.setString(x++, recursoEducacionalVO.getNomeFisicoArquivo());
                    sqlAlterar.setDate(x++, Uteis.getDataJDBC(recursoEducacionalVO.getDataAlteracao()));
                    sqlAlterar.setInt(x++, recursoEducacionalVO.getUsuarioAlteracao().getCodigo());
                    sqlAlterar.setString(x++, recursoEducacionalVO.getDescricao());
                    sqlAlterar.setInt(x++, recursoEducacionalVO.getDisciplina().getCodigo());
                    sqlAlterar.setString(x++, recursoEducacionalVO.getSituacaoRecursoEducacional().name());
                    sqlAlterar.setString(x++, recursoEducacionalVO.getTituloGrafico());
                    sqlAlterar.setString(x++, recursoEducacionalVO.getTituloEixoX());
                    sqlAlterar.setString(x++, recursoEducacionalVO.getTituloEixoY());
                    sqlAlterar.setBoolean(x++, recursoEducacionalVO.getApresentarLegenda());
                    sqlAlterar.setString(x++, recursoEducacionalVO.getValorGrafico());
                    sqlAlterar.setString(x++, recursoEducacionalVO.getTipoGrafico().name());
                    sqlAlterar.setString(x++, recursoEducacionalVO.getCategoriaGrafico());
                    sqlAlterar.setInt(x++, recursoEducacionalVO.getCodigo());
                    return sqlAlterar;
                }
            }) <= 0) {
                incluir(recursoEducacionalVO, controlarAcesso, usuario);
                return;
            }
            recursoEducacionalVO.setNovoObj(false);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void validarDados(RecursoEducacionalVO recursoEducacionalVO) throws ConsistirException {
        ConsistirException consistirException = new ConsistirException();
        if (recursoEducacionalVO.getTitulo().trim().isEmpty()) {
            consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_RecursoEducacional_titulo"));
        }
        if (recursoEducacionalVO.getTipoRecursoEducacional() == null ||
                (recursoEducacionalVO.getTipoRecursoEducacional() != null &&
                ((recursoEducacionalVO.getTexto().trim().isEmpty() 
                        && (recursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.TEXTO_HTML)
                                || recursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.VIDEO_URL))
                        ) ||
                (recursoEducacionalVO.getNomeFisicoArquivo().trim().isEmpty() 
                        && !recursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.TEXTO_HTML)
                        && !recursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.VIDEO_URL)&& !recursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO))
                        || (recursoEducacionalVO.getTipoRecursoEducacional().equals(TipoRecursoEducacionalEnum.GRAFICO) && 
                                recursoEducacionalVO.getValorGrafico().trim().isEmpty()    )))) {
            consistirException.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_RecursoEducacional_conteudo"));
        }
        if (!consistirException.getListaMensagemErro().isEmpty()) {
            throw consistirException;
        }
    }

    @Override
    public RecursoEducacionalVO consultarRecursoEducacionalPorConteudoUnidadePaginaRecursoEducacional(Integer conteudoUnidadePaginaRecursoEducacional, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT RecursoEducacional.*, ");
        sql.append(" Disciplina.nome as \"disciplina.nome\" ");
        sql.append(" FROM RecursoEducacional ");
        sql.append(" INNER JOIN Disciplina on RecursoEducacional.disciplina = Disciplina.codigo ");
        sql.append(" WHERE conteudoUnidadePaginaRecursoEducacional = ").append(conteudoUnidadePaginaRecursoEducacional);
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (rs.next()) {
            return montarDados(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, controlarAcesso, usuario);
        }
        return new RecursoEducacionalVO();
    }

    @Override
    public RecursoEducacionalVO consultarRecursoEducacionalPorConteudoUnidadePagina(Integer conteudoUnidadePagina, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT RecursoEducacional.*, ");
        sql.append(" Disciplina.nome as \"disciplina.nome\" ");
        sql.append(" FROM RecursoEducacional ");
        sql.append(" INNER JOIN Disciplina on RecursoEducacional.disciplina = Disciplina.codigo ");
        sql.append(" WHERE conteudoUnidadePagina = ").append(conteudoUnidadePagina);
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (rs.next()) {
            return montarDados(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, controlarAcesso, usuario);
        }
        return new RecursoEducacionalVO();
    }

    @Override
    public List<RecursoEducacionalVO> consultarRecursoEducacional(String titulo, TipoRecursoEducacionalEnum tipoRecursoEducacional,
            SituacaoRecursoEducacionalEnum situacaoRecursoEducacional, Integer disciplina, String nomeDisciplina, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario, Integer limit, Integer offset) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT RecursoEducacional.*, ");
        sql.append(" Disciplina.nome as \"disciplina.nome\" ");
        sql.append(" FROM RecursoEducacional ");
        sql.append(" INNER JOIN Disciplina on RecursoEducacional.disciplina = Disciplina.codigo ");
        sql.append(" WHERE 1 = 1 ");
        if (titulo != null && !titulo.trim().isEmpty()) {
            sql.append(" and sem_acentos(upper(titulo)) like (sem_acentos(upper('%").append(titulo).append("%'))) ");
        }
        if (tipoRecursoEducacional != null) {
            if (tipoRecursoEducacional.equals(TipoRecursoEducacionalEnum.TEXTO_HTML)) {
                sql.append(" and tipoRecursoEducacional in ('").append(TipoRecursoEducacionalEnum.TEXTO_HTML).append("', ");
                sql.append(" '").append(TipoRecursoEducacionalEnum.IMAGEM).append("') ");
                
            }else{
                sql.append(" and tipoRecursoEducacional = '").append(tipoRecursoEducacional.name()).append("' ");
            }
            
        }
        if (situacaoRecursoEducacional != null) {
            sql.append(" and situacaoRecursoEducacional = '").append(situacaoRecursoEducacional.name()).append("' ");
        }
        
        if (disciplina != null && disciplina > 0) {
            sql.append(" and disciplina.codigo = ").append(disciplina);
        }
        if (nomeDisciplina != null && !nomeDisciplina.trim().isEmpty()) {
            sql.append(" and sem_acentos(upper(disciplina.nome)) like (sem_acentos(upper('%").append(nomeDisciplina).append("%'))) ");
        }
        sql.append(" ORDER BY disciplina.nome, tipoRecursoEducacional, titulo ");
        if (limit != null && limit > 0) {
            sql.append(" limit ").append(limit).append(" offset ").append(offset);
        }
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados, controlarAcesso, usuario);
    }

    @Override
    public Integer consultarTotalRecursoEducacional(String titulo, TipoRecursoEducacionalEnum tipoRecursoEducacional,
            SituacaoRecursoEducacionalEnum situacaoRecursoEducacional, Integer disciplina) throws Exception {
        StringBuilder sql = new StringBuilder("SELECT count(RecursoEducacional.codigo) as qtde ");
        sql.append(" FROM RecursoEducacional ");
        sql.append(" INNER JOIN Disciplina on RecursoEducacional.disciplina = Disciplina.codigo ");
        sql.append(" WHERE 1 = 1 ");
        if (titulo != null && !titulo.trim().isEmpty()) {
            sql.append(" and sem_acentos(upper(titulo)) like (sem_acentos(upper('%").append(titulo).append("%'))) ");
        }
        if (tipoRecursoEducacional != null) {
            sql.append(" and tipoRecursoEducacional = '").append(tipoRecursoEducacional.name()).append("' ");
        }
        if (situacaoRecursoEducacional != null) {
            sql.append(" and situacaoRecursoEducacional = '").append(situacaoRecursoEducacional.name()).append("' ");
        }
        if (disciplina != null && disciplina > 0) {
            sql.append(" and disciplina.codigo = ").append(disciplina);
        }

        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
        if (rs.next()) {
            return rs.getInt("qtde");
        }
        return 0;
    }

    private List<RecursoEducacionalVO> montarDadosConsulta(SqlRowSet rs, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        List<RecursoEducacionalVO> recursoEducacionalVOs = new ArrayList<RecursoEducacionalVO>(0);
        while (rs.next()) {
            recursoEducacionalVOs.add(montarDados(rs, nivelMontarDados, controlarAcesso, usuario));
        }
        return recursoEducacionalVOs;
    }

    private RecursoEducacionalVO montarDados(SqlRowSet rs, NivelMontarDados nivelMontarDados, Boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        RecursoEducacionalVO recursoEducacionalVO = new RecursoEducacionalVO();
        recursoEducacionalVO.setCodigo(rs.getInt("codigo"));
        recursoEducacionalVO.setAltura(rs.getInt("altura"));
        recursoEducacionalVO.setLargura(rs.getInt("largura"));
        recursoEducacionalVO.setCaminhoBaseRepositorio(rs.getString("caminhoBaseRepositorio"));
        recursoEducacionalVO.setNomeFisicoArquivo(rs.getString("nomeFisicoArquivo"));
        recursoEducacionalVO.setNomeRealArquivo(rs.getString("nomeRealArquivo"));
        recursoEducacionalVO.setTexto(rs.getString("texto"));
        recursoEducacionalVO.setDataAlteracao(rs.getDate("dataAlteracao"));
        recursoEducacionalVO.setDataCadastro(rs.getDate("dataCadastro"));
        recursoEducacionalVO.setTitulo(rs.getString("titulo"));
        recursoEducacionalVO.getConteudoUnidadePagina().setCodigo(rs.getInt("conteudounidadepagina"));
        recursoEducacionalVO.getConteudoUnidadePaginaRecursoEducacional().setCodigo(rs.getInt("conteudoUnidadePaginaRecursoEducacional"));
        recursoEducacionalVO.getUsuarioAlteracao().setCodigo(rs.getInt("usuarioAlteracao"));
        recursoEducacionalVO.getUsuarioCadastro().setCodigo(rs.getInt("usuarioCadastro"));
        recursoEducacionalVO.setTipoRecursoEducacional(TipoRecursoEducacionalEnum.valueOf(rs.getString("tipoRecursoEducacional")));
        recursoEducacionalVO.setDescricao(rs.getString("descricao"));
        recursoEducacionalVO.getDisciplina().setCodigo(rs.getInt("disciplina"));
        recursoEducacionalVO.getDisciplina().setNome(rs.getString("disciplina.nome"));
        recursoEducacionalVO.setSituacaoRecursoEducacional(SituacaoRecursoEducacionalEnum.valueOf(rs.getString("situacaoRecursoEducacional")));
        if (rs.getString("tipoGrafico") != null && !rs.getString("tipoGrafico").trim().isEmpty()) {
            recursoEducacionalVO.setTipoGrafico(TipoGraficoEnum.valueOf(rs.getString("tipoGrafico")));
        }
        recursoEducacionalVO.setTituloGrafico(rs.getString("tituloGrafico"));
        recursoEducacionalVO.setTituloEixoX(rs.getString("tituloEixoX"));
        recursoEducacionalVO.setTituloEixoY(rs.getString("tituloEixoY"));
        recursoEducacionalVO.setValorGrafico(rs.getString("valorGrafico"));
        recursoEducacionalVO.setCategoriaGrafico(rs.getString("categoriaGrafico"));
        recursoEducacionalVO.setApresentarLegenda(rs.getBoolean("apresentarLegenda"));
        recursoEducacionalVO.setNovoObj(false);
        return recursoEducacionalVO;
    }

}
