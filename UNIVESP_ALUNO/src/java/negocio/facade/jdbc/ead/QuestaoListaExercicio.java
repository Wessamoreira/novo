package negocio.facade.jdbc.ead;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.ListaExercicioVO;
import negocio.comuns.ead.QuestaoListaExercicioVO;
import negocio.comuns.ead.QuestaoVO;
import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.ead.enumeradores.SituacaoQuestaoEnum;
import negocio.comuns.ead.enumeradores.TipoGeracaoListaExercicioEnum;
import negocio.comuns.ead.enumeradores.TipoQuestaoEnum;
import negocio.comuns.ead.enumeradores.UsoQuestaoEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.ead.QuestaoListaExercicioInterfaceFacade;

@Repository
@Lazy
public class QuestaoListaExercicio extends ControleAcesso implements QuestaoListaExercicioInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void incluir(final QuestaoListaExercicioVO questaoListaExercicioVO) throws Exception {
        try {
            final StringBuilder sql = new StringBuilder("INSERT INTO questaoListaExercicio ");
            sql.append(" (questao, listaExercicio, ordemApresentacao ");
            sql.append(" ) VALUES (?,?,?) ");
            sql.append(" returning codigo ");
            questaoListaExercicioVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                @Override
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement ps = arg0.prepareStatement(sql.toString());
                    int x = 1;
                    ps.setInt(x++, questaoListaExercicioVO.getQuestao().getCodigo());
                    ps.setInt(x++, questaoListaExercicioVO.getListaExercicio().getCodigo());
                    ps.setInt(x++, questaoListaExercicioVO.getOrdemApresentacao());
                    return ps;
                }
            }, new ResultSetExtractor<Integer>() {

                @Override
                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        return arg0.getInt("codigo");
                    }
                    return null;
                }

            }));
            questaoListaExercicioVO.setNovoObj(false);
        } catch (Exception e) {
            questaoListaExercicioVO.setNovoObj(true);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void alterar(final QuestaoListaExercicioVO questaoListaExercicioVO) throws Exception {
        try {
            final StringBuilder sql = new StringBuilder("UPDATE questaoListaExercicio set ");
            sql.append(" questao =?, listaExercicio = ?, ordemApresentacao = ? ");
            sql.append(" where codigo = ? ");
            if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                @Override
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement ps = arg0.prepareStatement(sql.toString());
                    int x = 1;
                    ps.setInt(x++, questaoListaExercicioVO.getQuestao().getCodigo());
                    ps.setInt(x++, questaoListaExercicioVO.getListaExercicio().getCodigo());
                    ps.setInt(x++, questaoListaExercicioVO.getOrdemApresentacao());
                    ps.setInt(x++, questaoListaExercicioVO.getCodigo());
                    return ps;
                }
            }) == 0) {
                incluir(questaoListaExercicioVO);
                return;
            }
            questaoListaExercicioVO.setNovoObj(false);
        } catch (Exception e) {
            questaoListaExercicioVO.setNovoObj(false);
            throw e;
        }
    }

    @Override
    public void incluirQuestaoListaExercicio(ListaExercicioVO listaExercicioVO) throws Exception {
        for (QuestaoListaExercicioVO questaoListaExercicioVO : listaExercicioVO.getQuestaoListaExercicioVOs()) {
            questaoListaExercicioVO.setListaExercicio(listaExercicioVO);
            validarDados(questaoListaExercicioVO);
            incluir(questaoListaExercicioVO);
        }

    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirQuestaoListaExercicio(ListaExercicioVO listaExercicioVO) throws Exception {
        StringBuilder sb = new StringBuilder("DELETE FROM QuestaoListaExercicio where listaExercicio =  ").append(listaExercicioVO.getCodigo());
        sb.append(" and codigo not in ( 0 ");
        for (QuestaoListaExercicioVO questaoListaExercicioVO : listaExercicioVO.getQuestaoListaExercicioVOs()) {
            if (!questaoListaExercicioVO.isNovoObj()) {
                sb.append(", ").append(questaoListaExercicioVO.getCodigo());
            }
        }
        sb.append(")");
        getConexao().getJdbcTemplate().execute(sb.toString());
    }

    @Override
    public void alterarQuestaoListaExercicio(ListaExercicioVO listaExercicioVO) throws Exception {
        excluirQuestaoListaExercicio(listaExercicioVO);
        for (QuestaoListaExercicioVO questaoListaExercicioVO : listaExercicioVO.getQuestaoListaExercicioVOs()) {
            questaoListaExercicioVO.setListaExercicio(listaExercicioVO);
            validarDados(questaoListaExercicioVO);
            if (questaoListaExercicioVO.isNovoObj()) {
                incluir(questaoListaExercicioVO);
            } else {
                alterar(questaoListaExercicioVO);
            }
        }

    }

    public String getSelectDadosBasico() {
        StringBuilder sb = new StringBuilder("SELECT QuestaoListaExercicio.*, ");
        sb.append(" questao.codigo as \"questao.codigo\",  ");
        sb.append(" questao.enunciado as \"questao.enunciado\",   ");
        sb.append(" questao.ajuda as \"questao.ajuda\",   ");
        sb.append(" questao.justificativa as \"questao.justificativa\",   ");
        sb.append(" questao.nivelComplexidadeQuestao as \"questao.nivelComplexidadeQuestao\",   ");
        sb.append(" questao.tipoQuestaoEnum as \"questao.tipoQuestaoEnum\"  ");
        sb.append(" FROM QuestaoListaExercicio ");
        sb.append(" inner join Questao on  Questao.codigo = QuestaoListaExercicio.Questao ").append(" and questao.situacaoQuestaoEnum = '").append(SituacaoQuestaoEnum.ATIVA.name()).append("' ");
        return sb.toString();
    }

    public String getSelectDadosCompleto() {
        StringBuilder sb = new StringBuilder("SELECT QuestaoListaExercicio.*, ");
        sb.append(" questao.codigo as \"questao.codigo\",  ");
        sb.append(" questao.enunciado as \"questao.enunciado\",   ");
        sb.append(" questao.ajuda as \"questao.ajuda\",   ");
        sb.append(" questao.justificativa as \"questao.justificativa\",   ");
        sb.append(" questao.nivelComplexidadeQuestao as \"questao.nivelComplexidadeQuestao\",   ");
        sb.append(" questao.tipoQuestaoEnum as \"questao.tipoQuestaoEnum\",  ");
        sb.append(" orp.codigo as \"orp.codigo\", orp.opcaoResposta as \"orp.opcaoResposta\", orp.correta as \"orp.correta\", ");
        sb.append(" orp.questao as \"orp.questao\", orp.ordemApresentacao as \"orp.ordemApresentacao\" ");
        sb.append(" FROM QuestaoListaExercicio ");
        sb.append(" inner join Questao on  Questao.codigo = QuestaoListaExercicio.Questao ").append(" and questao.situacaoQuestaoEnum = '").append(SituacaoQuestaoEnum.ATIVA.name()).append("' ");
        sb.append(" inner join OpcaoRespostaQuestao orp on  Questao.codigo = orp.questao ");
        return sb.toString();
    }

    private List<QuestaoListaExercicioVO> montarDadosConsulta(SqlRowSet rs, NivelMontarDados nivelMontarDados) {
        List<QuestaoListaExercicioVO> questaoListaExercicioVOs = new ArrayList<QuestaoListaExercicioVO>(0);
        Map<Integer, QuestaoListaExercicioVO> qMap = new HashMap<Integer, QuestaoListaExercicioVO>(0);
        QuestaoListaExercicioVO obj = null;
        while (rs.next()) {
            if (!qMap.containsKey(rs.getInt("codigo"))) {
                obj = montarDadosBasicos(rs);
                qMap.put(obj.getCodigo(), obj);
            } else {
                obj = qMap.get(rs.getInt("codigo"));
            }
            if (NivelMontarDados.TODOS.equals(nivelMontarDados) && rs.getInt("orp.codigo") > 1) {
                obj.getQuestao().getOpcaoRespostaQuestaoVOs().add(getFacadeFactory().getOpcaoRespostaQuestaoFacade().montarDados(rs, "orp."));
            }
        }
        
        questaoListaExercicioVOs.addAll(qMap.values());
        Ordenacao.ordenarLista(questaoListaExercicioVOs, "ordemApresentacao");
        
            int x = 1;
            for(QuestaoListaExercicioVO questaoListaExercicioVO:questaoListaExercicioVOs){
                questaoListaExercicioVO.setOrdemApresentacao(x++);
                Ordenacao.ordenarLista(questaoListaExercicioVO.getQuestao().getOpcaoRespostaQuestaoVOs(), "ordemApresentacao");
            }
        
        
        return questaoListaExercicioVOs;
    }

    private QuestaoListaExercicioVO montarDadosBasicos(SqlRowSet rs) {
        QuestaoListaExercicioVO questaoListaExercicioVO = new QuestaoListaExercicioVO();
        questaoListaExercicioVO.setNovoObj(false);
        questaoListaExercicioVO.setCodigo(rs.getInt("codigo"));
        questaoListaExercicioVO.getListaExercicio().setCodigo(rs.getInt("listaExercicio"));
        questaoListaExercicioVO.setOrdemApresentacao(rs.getInt("ordemApresentacao"));
        questaoListaExercicioVO.getQuestao().setNovoObj(false);
        questaoListaExercicioVO.getQuestao().setCodigo(rs.getInt("questao"));
        questaoListaExercicioVO.getQuestao().setEnunciado(rs.getString("questao.enunciado"));
        questaoListaExercicioVO.getQuestao().setAjuda(rs.getString("questao.ajuda"));
        questaoListaExercicioVO.getQuestao().setJustificativa(rs.getString("questao.justificativa"));
        questaoListaExercicioVO.getQuestao().setNivelComplexidadeQuestao(NivelComplexidadeQuestaoEnum.valueOf(rs.getString("questao.nivelComplexidadeQuestao")));
        questaoListaExercicioVO.getQuestao().setTipoQuestaoEnum(TipoQuestaoEnum.valueOf(rs.getString("questao.tipoQuestaoEnum")));
        return questaoListaExercicioVO;
    }

    @Override
    public List<QuestaoListaExercicioVO> consultarPorListaExercicio(Integer listaExercicio, NivelMontarDados nivelMontarDados) {
        StringBuilder sql = null;
        if (nivelMontarDados.equals(NivelMontarDados.BASICO)) {
            sql = new StringBuilder(getSelectDadosBasico());
            sql.append(" where listaExercicio = ").append(listaExercicio);
            sql.append(" order by ordemApresentacao ");
        } else {
            sql = new StringBuilder(getSelectDadosCompleto());
            sql.append(" where listaExercicio = ").append(listaExercicio);
            sql.append(" and questao.situacaoQuestaoEnum = '").append(SituacaoQuestaoEnum.ATIVA.name()).append("' ");
            sql.append(" order by questaoListaExercicio.ordemApresentacao ");
        }
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados);
    }

    @Override
    public void validarDados(QuestaoListaExercicioVO questaoListaExercicioVO) throws ConsistirException {
        ConsistirException ce = null;
        if (questaoListaExercicioVO.getQuestao() == null ||
                questaoListaExercicioVO.getQuestao().getCodigo() == null
                || questaoListaExercicioVO.getQuestao().getCodigo() == 0) {
            ce = ce == null ? new ConsistirException() : ce;
            ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_QuestaoListaExercicio_questao"));
        }

        if (ce != null) {
            throw ce;
        }

    }

    @Override
    public void consultarPorListaExercicioParaRespostaAluno(ListaExercicioVO listaExercicio, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO, Integer codigoUnidadeConteudo, Integer codigoTemaAssunto, UsuarioVO usuarioVO) throws Exception {
        if (listaExercicio.getQuestaoListaExercicioVOs().isEmpty()) {
            if (listaExercicio.getTipoGeracaoListaExercicio().equals(TipoGeracaoListaExercicioEnum.FIXO)) {
                listaExercicio.setQuestaoListaExercicioVOs(consultarPorListaExercicio(listaExercicio.getCodigo(), NivelMontarDados.TODOS));
            } else {
                List<QuestaoVO> questaoVOs = getFacadeFactory().getQuestaoFacade().consultarQuestoesPorDisciplinaRandomicamente(matriculaPeriodoTurmaDisciplinaVO, codigoTemaAssunto, codigoUnidadeConteudo, listaExercicio.getDisciplina().getCodigo(), listaExercicio.getQuantidadeNivelQuestaoFacil(),
                        listaExercicio.getQuantidadeNivelQuestaoMedio(), listaExercicio.getQuantidadeNivelQuestaoDificil(),
                        listaExercicio.getQuantidadeQualquerNivelQuestao(), UsoQuestaoEnum.EXERCICIO, listaExercicio.getPoliticaSelecaoQuestaoEnum(), listaExercicio.getRegraDistribuicaoQuestaoEnum(), true, matriculaPeriodoTurmaDisciplinaVO.getConteudo().getCodigo(), listaExercicio.getCodigo(), listaExercicio.getRandomizarApenasQuestoesCadastradasPeloProfessor(), null, usuarioVO);
                QuestaoListaExercicioVO obj = null;
                int x = 1;
                for (QuestaoVO questaoVO : questaoVOs) {
                    obj = new QuestaoListaExercicioVO();
                    Ordenacao.ordenarLista(questaoVO.getOpcaoRespostaQuestaoVOs(), "ordemApresentacao");
                    obj.setQuestao(questaoVO);
                    obj.setOrdemApresentacao(x++);
                    listaExercicio.getQuestaoListaExercicioVOs().add(obj);
                }
            }
        }
    }

}
