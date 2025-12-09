package negocio.facade.jdbc.academico;

import controle.arquitetura.DataModelo;
import negocio.comuns.academico.MotivoIndeferimentoDocumentoAlunoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.enumeradores.StatusAtivoInativoEnum;
import negocio.comuns.utilitarias.StreamSeiException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.comuns.utilitarias.faturamento.nfe.UteisTexto;
import negocio.facade.jdbc.arquitetura.AtributoPersistencia;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.MotivoIndeferimentoDocumentoAlunoInterfaceFacade;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Lazy
@Scope("singleton")
public class MotivoIndeferimentoDocumentoAluno extends ControleAcesso implements MotivoIndeferimentoDocumentoAlunoInterfaceFacade {

    private static final long serialVersionUID = -4506848488766171348L;
    protected static String idEntidade;

    public MotivoIndeferimentoDocumentoAluno() {
        setIdEntidade("MotivoIndeferimentoDocumentoAluno");
    }

    public void setIdEntidade(String idEntidade) {
        MotivoIndeferimentoDocumentoAluno.idEntidade = idEntidade;
    }

    public static String getIdEntidade() {
        return MotivoIndeferimentoDocumentoAluno.idEntidade;
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(MotivoIndeferimentoDocumentoAlunoVO obj, boolean verificarAcesso, UsuarioVO usuarioVO) throws Exception {
        try {
            validarDados(obj);
            if (obj.getCodigo() == 0) {
                incluir(obj, verificarAcesso, usuarioVO);
            } else {
                alterar(obj, verificarAcesso, usuarioVO);
            }
        } catch (Exception e) {
            String erro = e.getMessage();
            if (e instanceof DataIntegrityViolationException && erro.contains("ck_motivoindeferimentodocumentoaluno_unicidadenome")) {
                erro = UteisJSF.internacionalizar("msg_MotivoIndeferimentoDocumentoAluno_nomeInformado");
            }
            throw new Exception(erro);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final MotivoIndeferimentoDocumentoAlunoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
        try {
            incluir(getIdEntidade(), verificarAcesso, usuario);
            incluir(obj, "motivoindeferimentodocumentoaluno", new AtributoPersistencia()
                    .add("nome", obj.getNome()).add("situacao", obj.getSituacao()), usuario);
            obj.setNovoObj(Boolean.FALSE);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final MotivoIndeferimentoDocumentoAlunoVO obj, boolean verificarAcesso, UsuarioVO usuario) throws Exception {
        try {
            alterar(getIdEntidade(), verificarAcesso, usuario);
            alterar(obj, "motivoindeferimentodocumentoaluno", new AtributoPersistencia()
                    .add("nome", obj.getNome())
                    .add("situacao", obj.getSituacao()), new AtributoPersistencia().add("codigo", obj.getCodigo()), usuario);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(MotivoIndeferimentoDocumentoAlunoVO obj, boolean verificarAcesso, UsuarioVO usuario) {
        try {
            excluir(getIdEntidade(), verificarAcesso, usuario);
            getConexao().getJdbcTemplate().update("DELETE FROM motivoindeferimentodocumentoaluno WHERE ((codigo = ?))" + adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario), obj.getCodigo());
        } catch (Exception e) {
            throw new StreamSeiException(e);
        }
    }

    private void validarDados(MotivoIndeferimentoDocumentoAlunoVO obj) throws Exception {
        if (!Uteis.isAtributoPreenchido(obj.getNome())) {
            throw new Exception(UteisJSF.internacionalizar("msg_MotivoIndeferimentoDocumentoAluno_nome"));
        }
        if (!Uteis.isAtributoPreenchido(obj.getSituacao())) {
            throw new Exception(UteisJSF.internacionalizar("msg_MotivoIndeferimentoDocumentoAluno_situacao"));
        }
    }

    @Override
    public void consultar(DataModelo dataModelo, MotivoIndeferimentoDocumentoAlunoVO obj) throws Exception {
        dataModelo.getListaConsulta().clear();
        dataModelo.getListaFiltros().clear();
        dataModelo.setListaConsulta(consultaRapidaPorFiltros(dataModelo, obj));
    }

    private List<MotivoIndeferimentoDocumentoAlunoVO> consultaRapidaPorFiltros(DataModelo retorno, MotivoIndeferimentoDocumentoAlunoVO filtro) throws Exception {
        consultar(getIdEntidade(), retorno.isControlarAcesso(), retorno.getUsuario());
        StringBuilder sql = getConsultaBasica(true);
        montarFiltrosConsultaPadrao(retorno, filtro, sql);
        sql.append(" ORDER BY codigo DESC ");
        UteisTexto.addLimitAndOffset(sql, retorno.getLimitePorPagina(), retorno.getOffset());
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), retorno.getListaFiltros().toArray());
        montarTotalizadorConsultaBasica(retorno, rs);
        return montarDadosConsulta(rs, retorno.getNivelMontarDados(), retorno.getUsuario());
    }

    private StringBuilder getConsultaBasica(Boolean isBuscarTotalRegistros) {
        StringBuilder sql = new StringBuilder("SELECT ");
        if (isBuscarTotalRegistros) {
            sql.append(" COUNT(*) OVER() as totalRegistroConsulta, ");
        }
        sql.append(" codigo, nome, situacao ");
        sql.append(" FROM motivoindeferimentodocumentoaluno WHERE 1=1 ");
        return sql;
    }

    private void montarFiltrosConsultaPadrao(DataModelo retorno, MotivoIndeferimentoDocumentoAlunoVO filtro, StringBuilder sql) {
        if (Uteis.isAtributoPreenchido(filtro.getCodigo())) {
            sql.append(" AND codigo = ? ");
            retorno.getListaFiltros().add(filtro.getCodigo());
        }
        if (Uteis.isAtributoPreenchido(filtro.getNome())) {
            sql.append(" AND lower(sem_acentos(nome)) like lower(sem_acentos(?))");
            retorno.getListaFiltros().add(PERCENT + filtro.getNome() + PERCENT);
        }
        if (Uteis.isAtributoPreenchido(filtro.getSituacao())) {
            sql.append(" AND situacao = ? ");
            retorno.getListaFiltros().add(filtro.getSituacao().name());
        }
    }

    private List<MotivoIndeferimentoDocumentoAlunoVO> montarDadosConsulta(SqlRowSet tabelaResultado, int nivelMontarDados, UsuarioVO usuario) {
        List<MotivoIndeferimentoDocumentoAlunoVO> vetResultado = new ArrayList<>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, nivelMontarDados, usuario));
        }
        return vetResultado;
    }

    private MotivoIndeferimentoDocumentoAlunoVO montarDados(SqlRowSet dadosSQL, int nivelMontarDados, UsuarioVO usuario) {
        MotivoIndeferimentoDocumentoAlunoVO obj = new MotivoIndeferimentoDocumentoAlunoVO();
        obj.setNovoObj(Boolean.FALSE);
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.setNome(dadosSQL.getString("nome"));
        obj.setSituacao(StatusAtivoInativoEnum.valueOf(dadosSQL.getString("situacao")));
        return obj;
    }

    @Override
    public List<MotivoIndeferimentoDocumentoAlunoVO> consultarMotivoIndeferimentoDocumentoAlunoPorSituacao(StatusAtivoInativoEnum situacao, Boolean verificaAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), verificaAcesso, usuario);
        StringBuilder sql = getConsultaBasica(false)
                .append(" AND situacao = ? ");
        sql.append(" ORDER BY nome ");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), situacao.name());
        return montarDadosConsulta(rs, Uteis.NIVELMONTARDADOS_TODOS, usuario);
    }
    
    @Override
    public void carregarMotivoIndeferimentoDocumentoAluno(MotivoIndeferimentoDocumentoAlunoVO obj, Boolean verificaAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), verificaAcesso, usuario);
        if (Uteis.isAtributoPreenchido(obj.getCodigo())) {
            StringBuilder sql = getConsultaBasica(false)
                    .append(" AND codigo = ? ");
            SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), obj.getCodigo());
            if (rs.next()) {
                obj.setNome(rs.getString("nome"));
                obj.setSituacao(StatusAtivoInativoEnum.valueOf(rs.getString("situacao")));
            }
        }
    }

    @Override
    public MotivoIndeferimentoDocumentoAlunoVO consultarPorChavePrimaria(Integer codigo, Boolean verificaAcesso, UsuarioVO usuario) throws Exception {
        consultar(getIdEntidade(), verificaAcesso, usuario);
        StringBuilder sql = getConsultaBasica(false)
                .append(" AND codigo = ? ");
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sql.toString(), codigo);
        MotivoIndeferimentoDocumentoAlunoVO obj = new MotivoIndeferimentoDocumentoAlunoVO();
        if (rs.next()) {
            obj.setNovoObj(false);
            obj.setCodigo(rs.getInt("codigo"));
            obj.setNome(rs.getString("nome"));
            obj.setSituacao(StatusAtivoInativoEnum.valueOf(rs.getString("situacao")));
        }
        return obj;
    }
}