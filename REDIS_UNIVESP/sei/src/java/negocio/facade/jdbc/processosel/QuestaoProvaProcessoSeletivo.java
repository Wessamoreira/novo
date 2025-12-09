package negocio.facade.jdbc.processosel;

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

import negocio.comuns.ead.enumeradores.NivelComplexidadeQuestaoEnum;
import negocio.comuns.processosel.ProvaProcessoSeletivoVO;
import negocio.comuns.processosel.QuestaoProvaProcessoSeletivoVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.facade.jdbc.utilitarias.NivelMontarDados;
import negocio.interfaces.processosel.QuestaoProvaProcessoSeletivoInterfaceFacade;

@Repository
@Lazy
public class QuestaoProvaProcessoSeletivo extends ControleAcesso implements QuestaoProvaProcessoSeletivoInterfaceFacade {

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void incluir(final QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO) throws Exception {
        try {
            final StringBuilder sql = new StringBuilder("INSERT INTO questaoProvaProcessoSeletivo ");
            sql.append(" (questaoProcessoSeletivo, provaProcessoSeletivo, ordemApresentacao ");
            sql.append(" ) VALUES (?,?,?) ");
            sql.append(" returning codigo ");
            questaoProvaProcessoSeletivoVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                @Override
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement ps = arg0.prepareStatement(sql.toString());
                    int x = 1;
                    ps.setInt(x++, questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getCodigo());
                    ps.setInt(x++, questaoProvaProcessoSeletivoVO.getProvaProcessoSeletivo().getCodigo());
                    ps.setInt(x++, questaoProvaProcessoSeletivoVO.getOrdemApresentacao());
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
            questaoProvaProcessoSeletivoVO.setNovoObj(false);
        } catch (Exception e) {
            questaoProvaProcessoSeletivoVO.setNovoObj(true);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    private void alterar(final QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO) throws Exception {
        try {
            final StringBuilder sql = new StringBuilder("UPDATE questaoProvaProcessoSeletivo set ");
            sql.append(" questaoProcessoSeletivo =?, provaProcessoSeletivo = ?, ordemApresentacao = ? ");
            sql.append(" where codigo = ? ");
            if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                @Override
                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    PreparedStatement ps = arg0.prepareStatement(sql.toString());
                    int x = 1;
                    ps.setInt(x++, questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getCodigo());
                    ps.setInt(x++, questaoProvaProcessoSeletivoVO.getProvaProcessoSeletivo().getCodigo());
                    ps.setInt(x++, questaoProvaProcessoSeletivoVO.getOrdemApresentacao());
                    ps.setInt(x++, questaoProvaProcessoSeletivoVO.getCodigo());
                    return ps;
                }
            }) == 0) {
                incluir(questaoProvaProcessoSeletivoVO);
                return;
            }
            questaoProvaProcessoSeletivoVO.setNovoObj(false);
        } catch (Exception e) {
            questaoProvaProcessoSeletivoVO.setNovoObj(false);
            throw e;
        }
    }

    @Override
    public void incluirQuestaoProvaProcessoSeletivo(ProvaProcessoSeletivoVO provaProcessoSeletivoVO) throws Exception {
        for (QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO : provaProcessoSeletivoVO.getQuestaoProvaProcessoSeletivoVOs()) {
            questaoProvaProcessoSeletivoVO.setProvaProcessoSeletivo(provaProcessoSeletivoVO);
            validarDados(questaoProvaProcessoSeletivoVO);
            incluir(questaoProvaProcessoSeletivoVO);
        }

    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirQuestaoProvaProcessoSeletivo(ProvaProcessoSeletivoVO provaProcessoSeletivoVO) throws Exception {
        StringBuilder sb = new StringBuilder("DELETE FROM QuestaoProvaProcessoSeletivo where provaProcessoSeletivo =  ").append(provaProcessoSeletivoVO.getCodigo());
        sb.append(" and codigo not in ( 0 ");
        for (QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO : provaProcessoSeletivoVO.getQuestaoProvaProcessoSeletivoVOs()) {
            if (!questaoProvaProcessoSeletivoVO.isNovoObj()) {
                sb.append(", ").append(questaoProvaProcessoSeletivoVO.getCodigo());
            }
        }
        sb.append(")");
        getConexao().getJdbcTemplate().execute(sb.toString());
    }

    @Override
    public void alterarQuestaoProvaProcessoSeletivo(ProvaProcessoSeletivoVO provaProcessoSeletivoVO) throws Exception {
        excluirQuestaoProvaProcessoSeletivo(provaProcessoSeletivoVO);
        for (QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO : provaProcessoSeletivoVO.getQuestaoProvaProcessoSeletivoVOs()) {
            questaoProvaProcessoSeletivoVO.setProvaProcessoSeletivo(provaProcessoSeletivoVO);
            validarDados(questaoProvaProcessoSeletivoVO);
            if (questaoProvaProcessoSeletivoVO.isNovoObj()) {
                incluir(questaoProvaProcessoSeletivoVO);
            } else {
                alterar(questaoProvaProcessoSeletivoVO);
            }
        }

    }

    public String getSelectDadosBasico() {
        StringBuilder sb = new StringBuilder("SELECT QuestaoProvaProcessoSeletivo.*, ");
        sb.append(" QuestaoProcessoSeletivo.codigo as \"QuestaoProcessoSeletivo.codigo\",  ");
        sb.append(" QuestaoProcessoSeletivo.enunciado as \"QuestaoProcessoSeletivo.enunciado\",   ");
        sb.append(" QuestaoProcessoSeletivo.ajuda as \"QuestaoProcessoSeletivo.ajuda\",   ");
        sb.append(" QuestaoProcessoSeletivo.justificativa as \"QuestaoProcessoSeletivo.justificativa\",   ");
        sb.append(" QuestaoProcessoSeletivo.nivelComplexidadeQuestao as \"QuestaoProcessoSeletivo.nivelComplexidadeQuestao\",   ");
        sb.append(" DisciplinasProcSeletivo.codigo as \"DisciplinasProcSeletivo.codigo\",   ");
        sb.append(" DisciplinasProcSeletivo.nome as \"DisciplinasProcSeletivo.nome\"  ");
        sb.append(" FROM QuestaoProvaProcessoSeletivo ");
        sb.append(" inner join QuestaoProcessoSeletivo on  QuestaoProcessoSeletivo.codigo = QuestaoProvaProcessoSeletivo.QuestaoProcessoSeletivo ");
        sb.append(" inner join DisciplinasProcSeletivo on  DisciplinasProcSeletivo.codigo = QuestaoProcessoSeletivo.DisciplinaProcSeletivo ");
        return sb.toString();
    }

    public String getSelectDadosCompleto() {
        StringBuilder sb = new StringBuilder("SELECT QuestaoProvaProcessoSeletivo.*, ");
        sb.append(" QuestaoProcessoSeletivo.codigo as \"QuestaoProcessoSeletivo.codigo\",  ");
        sb.append(" QuestaoProcessoSeletivo.enunciado as \"QuestaoProcessoSeletivo.enunciado\",   ");
        sb.append(" QuestaoProcessoSeletivo.ajuda as \"QuestaoProcessoSeletivo.ajuda\",   ");
        sb.append(" QuestaoProcessoSeletivo.justificativa as \"QuestaoProcessoSeletivo.justificativa\",   ");
        sb.append(" QuestaoProcessoSeletivo.nivelComplexidadeQuestao as \"QuestaoProcessoSeletivo.nivelComplexidadeQuestao\" ,  ");
        sb.append(" orp.codigo as \"orp.codigo\", orp.opcaoResposta as \"orp.opcaoResposta\", orp.correta as \"orp.correta\", ");
        sb.append(" orp.questaoProcessoSeletivo as \"orp.questaoProcessoSeletivo\", orp.ordemApresentacao as \"orp.ordemApresentacao\", ");
        sb.append(" DisciplinasProcSeletivo.codigo as \"DisciplinasProcSeletivo.codigo\",   ");
        sb.append(" DisciplinasProcSeletivo.nome as \"DisciplinasProcSeletivo.nome\"  ");
        sb.append(" FROM QuestaoProvaProcessoSeletivo ");
        sb.append(" inner join QuestaoProcessoSeletivo on  QuestaoProcessoSeletivo.codigo = QuestaoProvaProcessoSeletivo.QuestaoProcessoSeletivo ");
        sb.append(" inner join DisciplinasProcSeletivo on  DisciplinasProcSeletivo.codigo = QuestaoProcessoSeletivo.DisciplinaProcSeletivo ");
        sb.append(" inner join OpcaoRespostaQuestaoProcessoSeletivo orp on  QuestaoProcessoSeletivo.codigo = orp.questaoProcessoSeletivo ");
        return sb.toString();
    }

    private List<QuestaoProvaProcessoSeletivoVO> montarDadosConsulta(SqlRowSet rs, NivelMontarDados nivelMontarDados) {
        List<QuestaoProvaProcessoSeletivoVO> questaoProvaProcessoSeletivoVOs = new ArrayList<QuestaoProvaProcessoSeletivoVO>(0);
        Map<Integer, QuestaoProvaProcessoSeletivoVO> qMap = new HashMap<Integer, QuestaoProvaProcessoSeletivoVO>(0);
        QuestaoProvaProcessoSeletivoVO obj = null;
        while (rs.next()) {
            if (!qMap.containsKey(rs.getInt("codigo"))) {
                obj = montarDadosBasicos(rs);
                qMap.put(obj.getCodigo(), obj);
            } else {
                obj = qMap.get(rs.getInt("codigo"));
            }
            if (NivelMontarDados.TODOS.equals(nivelMontarDados) && rs.getInt("orp.codigo") > 1) {
                obj.getQuestaoProcessoSeletivo().getOpcaoRespostaQuestaoProcessoSeletivoVOs().add(getFacadeFactory().getOpcaoRespostaQuestaoProcessoSeletivoFacade().montarDados(rs, "orp."));
            }
        }

        questaoProvaProcessoSeletivoVOs.addAll(qMap.values());
        Ordenacao.ordenarLista(questaoProvaProcessoSeletivoVOs, "ordemApresentacao");

        int x = 1;
        for (QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO : questaoProvaProcessoSeletivoVOs) {
            questaoProvaProcessoSeletivoVO.setOrdemApresentacao(x++);
            Ordenacao.ordenarLista(questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getOpcaoRespostaQuestaoProcessoSeletivoVOs(), "ordemApresentacao");
        }

        return questaoProvaProcessoSeletivoVOs;
    }

    private QuestaoProvaProcessoSeletivoVO montarDadosBasicos(SqlRowSet rs) {
        QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO = new QuestaoProvaProcessoSeletivoVO();
        questaoProvaProcessoSeletivoVO.setNovoObj(false);
        questaoProvaProcessoSeletivoVO.setCodigo(rs.getInt("codigo"));
        questaoProvaProcessoSeletivoVO.getProvaProcessoSeletivo().setCodigo(rs.getInt("provaProcessoSeletivo"));
        questaoProvaProcessoSeletivoVO.setOrdemApresentacao(rs.getInt("ordemApresentacao"));
        questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().setNovoObj(false);
        questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().setCodigo(rs.getInt("QuestaoProcessoSeletivo"));
        questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().setEnunciado(rs.getString("QuestaoProcessoSeletivo.enunciado"));
        questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().setAjuda(rs.getString("QuestaoProcessoSeletivo.ajuda"));
        questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().setJustificativa(rs.getString("QuestaoProcessoSeletivo.justificativa"));
        questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().setNivelComplexidadeQuestao(NivelComplexidadeQuestaoEnum.valueOf(rs.getString("questaoProcessoSeletivo.nivelComplexidadeQuestao")));        
        questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getDisciplinaProcSeletivo().setCodigo(rs.getInt("DisciplinasProcSeletivo.codigo"));
        questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getDisciplinaProcSeletivo().setNome(rs.getString("DisciplinasProcSeletivo.nome"));
                
        return questaoProvaProcessoSeletivoVO;
    }

    @Override
    public List<QuestaoProvaProcessoSeletivoVO> consultarPorProvaProcessoSeletivo(Integer provaProcessoSeletivo, NivelMontarDados nivelMontarDados) {
        StringBuilder sql = null;
        if (nivelMontarDados.equals(NivelMontarDados.BASICO)) {
            sql = new StringBuilder(getSelectDadosBasico());
            sql.append(" where provaProcessoSeletivo = ").append(provaProcessoSeletivo);
            sql.append(" order by ordemApresentacao ");
        } else {
            sql = new StringBuilder(getSelectDadosCompleto());
            sql.append(" where provaProcessoSeletivo = ").append(provaProcessoSeletivo);            
            sql.append(" order by ordemApresentacao ");
        }
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sql.toString()), nivelMontarDados);
    }

    @Override
    public void validarDados(QuestaoProvaProcessoSeletivoVO questaoProvaProcessoSeletivoVO) throws ConsistirException {
        ConsistirException ce = null;
        if (questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo() == null ||
                questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getCodigo() == null
                || questaoProvaProcessoSeletivoVO.getQuestaoProcessoSeletivo().getCodigo() == 0) {
            ce = ce == null ? new ConsistirException() : ce;
            ce.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_QuestaoListaExercicio_questao"));
        }

        if (ce != null) {
            throw ce;
        }

    }

    

}
