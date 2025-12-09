package negocio.facade.jdbc.ead;

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

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.administrativo.ConfiguracaoGeralSistemaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.ead.DuvidaProfessorInteracaoVO;
import negocio.comuns.ead.DuvidaProfessorVO;
import negocio.comuns.ead.QuadroResumoDuvidaProfessorVO;
import negocio.comuns.ead.enumeradores.SituacaoDuvidaProfessorEnum;
import negocio.comuns.ead.enumeradores.TipoPessoaInteracaoDuvidaProfessorEnum;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;
import negocio.facade.jdbc.academico.Historico;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.ead.DuvidaProfessorInterfaceFacade;

@Repository
@Lazy
public class DuvidaProfessor extends ControleAcesso implements DuvidaProfessorInterfaceFacade {

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void persistir(DuvidaProfessorVO duvidaProfessorVO, Boolean controlarAcesso, UsuarioVO usuarioVO, String idEntidade) throws Exception {
        if (duvidaProfessorVO.isNovoObj()) {
            if (controlarAcesso) {
                incluir(idEntidade, controlarAcesso, usuarioVO);
            }
            duvidaProfessorVO.setDataCadastro(new Date());
            incluir(duvidaProfessorVO, usuarioVO);
        } else {
            if (controlarAcesso) {
                alterar(idEntidade, controlarAcesso, usuarioVO);
            }
            alterar(duvidaProfessorVO, usuarioVO);
        }

    }

    private void validarDados(DuvidaProfessorVO duvidaProfessorVO) throws ConsistirException {
        ConsistirException ex = new ConsistirException();
        if (duvidaProfessorVO.getDuvida().trim().isEmpty() || Uteis.retiraTags(duvidaProfessorVO.getDuvida()).trim().isEmpty()) {
            ex.adicionarListaMensagemErro(UteisJSF.internacionalizar("msg_DuvidaProfessor_duvida"));
        }
        if (!ex.getListaMensagemErro().isEmpty()) {
            throw ex;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void incluir(final DuvidaProfessorVO duvidaProfessorVO, UsuarioVO usuario) throws Exception {
        try {
            validarDados(duvidaProfessorVO);
            final StringBuilder sql = new StringBuilder("INSERT INTO DuvidaProfessor ");
            sql.append(" ( dataCadastro, dataAlteracao,  matricula,  aluno,  disciplina, turma,  ");
            sql.append(" permitePublicarDuvida, duvidaFrequente,  duvida, situacaoDuvidaProfessor) ");
            sql.append(" VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) returning codigo");
            duvidaProfessorVO.setCodigo(getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    int x = 1;
                    PreparedStatement sqlInserir = arg0.prepareStatement(sql.toString());
                    sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(duvidaProfessorVO.getDataCadastro()));
                    sqlInserir.setTimestamp(x++, Uteis.getDataJDBCTimestamp(duvidaProfessorVO.getDataCadastro()));
                    sqlInserir.setString(x++, duvidaProfessorVO.getMatricula());
                    sqlInserir.setInt(x++, duvidaProfessorVO.getAluno().getCodigo());
                    sqlInserir.setInt(x++, duvidaProfessorVO.getDisciplina().getCodigo());
                    sqlInserir.setInt(x++, duvidaProfessorVO.getTurma().getCodigo());
                    sqlInserir.setBoolean(x++, duvidaProfessorVO.getPermitePublicarDuvida());
                    sqlInserir.setBoolean(x++, duvidaProfessorVO.getDuvidaFrequente());
                    sqlInserir.setString(x++, duvidaProfessorVO.getDuvida());
                    sqlInserir.setString(x++, duvidaProfessorVO.getSituacaoDuvidaProfessor().name());
                    return sqlInserir;
                }
            }, new ResultSetExtractor<Integer>() {

                public Integer extractData(ResultSet arg0) throws SQLException, DataAccessException {
                    if (arg0.next()) {
                        duvidaProfessorVO.setNovoObj(Boolean.FALSE);
                        return arg0.getInt("codigo");
                    }
                    return null;
                }
            }));
        } catch (Exception e) {
            duvidaProfessorVO.setNovoObj(true);
            throw e;
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    private void alterar(final DuvidaProfessorVO duvidaProfessorVO, UsuarioVO usuario) throws Exception {
        try {
            validarDados(duvidaProfessorVO);
            final StringBuilder sql = new StringBuilder("UPDATE DuvidaProfessor SET ");
            sql.append(" dataAlteracao = ?, situacaoDuvidaProfessor = ?, permitePublicarDuvida = ?, duvidaFrequente= ?  ");
            sql.append(" where codigo = ? ");
            if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    int x = 1;
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                    sqlAlterar.setTimestamp(x++, Uteis.getDataJDBCTimestamp(new Date()));
                    sqlAlterar.setString(x++, duvidaProfessorVO.getSituacaoDuvidaProfessor().name());
                    sqlAlterar.setBoolean(x++, duvidaProfessorVO.getPermitePublicarDuvida());
                    sqlAlterar.setBoolean(x++, duvidaProfessorVO.getDuvidaFrequente());
                    sqlAlterar.setInt(x++, duvidaProfessorVO.getCodigo());
                    return sqlAlterar;
                }
            }) <= 0) {
                incluir(duvidaProfessorVO, usuario);
                return;
            }
            duvidaProfessorVO.setNovoObj(false);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void realizarRegistroDuvidaComoFrequente(final Integer duvidaProfessor, final Boolean frequente) throws Exception {
        try {

            final StringBuilder sql = new StringBuilder("UPDATE DuvidaProfessor SET ");
            sql.append(" duvidaFrequente= ?  ");
            sql.append(" where codigo = ? ");
            if (getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

                public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                    int x = 1;
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql.toString());
                    sqlAlterar.setBoolean(x++, frequente);
                    sqlAlterar.setInt(x++, duvidaProfessor);
                    return sqlAlterar;
                }
            }) <= 0) {

                return;
            }

        } catch (Exception e) {
            throw e;
        }

    }

    private String getSqlSelectConsulta(UsuarioVO usuarioVO, String matricula) {
        StringBuilder sb = new StringBuilder("");
        sb.append("select distinct DuvidaProfessor.*,");
        if (matricula != null && !matricula.trim().isEmpty()) {
            sb.append(" case situacaoDuvidaProfessor");
            sb.append(" when  'AGUARDANDO_RESPOSTA_ALUNO' then 1 ");
            sb.append(" when  'NOVA' then 2 ");
            sb.append(" when  'AGUARDANDO_RESPOSTA_PROFESSOR' then 3 ");
            sb.append(" when  'FINALIZADA' then 4 ");
            sb.append(" else 5 end, ");
        } else if (matricula == null || matricula.trim().isEmpty()) {
            sb.append(" case situacaoDuvidaProfessor");
            sb.append(" when  'NOVA' then 1 ");
            sb.append(" when  'AGUARDANDO_RESPOSTA_PROFESSOR' then 2 ");
            sb.append(" when  'AGUARDANDO_RESPOSTA_ALUNO' then 3 ");
            sb.append(" when  'FINALIZADA' then 4 ");
            sb.append(" else 5 end, ");
        }
        sb.append(" disciplina.nome as \"disciplina.nome\", turma.identificadorTurma as \"turma.identificadorTurma\",");
        sb.append(" aluno.nome AS \"aluno.nome\", aluno.cpf AS \"aluno.cpf\", arquivo.pastaBaseArquivo as \"arquivo.pastaBaseArquivo\", arquivo.codigo AS \"arquivo.codigo\",");
        sb.append(" arquivo.nome AS \"arquivo.nome\"");
        sb.append(" from DuvidaProfessor");
        sb.append(" inner join matricula on duvidaprofessor.matricula = matricula.matricula ");
        sb.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = matricula.matricula  and DuvidaProfessor.disciplina = matriculaperiodoturmadisciplina.disciplina and matriculaperiodoturmadisciplina.turma = DuvidaProfessor.turma ");
        sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
        sb.append(" inner join historico on  historico.matricula = matricula.matricula and  historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
        sb.append(" inner join disciplina  on DuvidaProfessor.disciplina = disciplina.codigo ");
        sb.append(" inner join turma  on DuvidaProfessor.turma = turma.codigo ");
        sb.append(" inner join pessoa as aluno  on DuvidaProfessor.aluno = aluno.codigo ");
        sb.append(" left join  Arquivo on Arquivo.codigo = aluno.arquivoImagem ");
        
        return sb.toString();
    }

    @Override
    public List<DuvidaProfessorVO> consutar(String matricula, Integer turma, Integer disciplina, SituacaoDuvidaProfessorEnum situacaoDuvidaProfessorEnum, Boolean frequente, Boolean trazerDuvidaDosColegas, Boolean controlarAcesso, UsuarioVO usuarioVO, Integer limite, Integer pagina, String ano, String semestre) throws Exception {
        StringBuilder sb = new StringBuilder(getSqlSelectConsulta(usuarioVO, matricula));
        montarFiltroConsulta(sb, matricula, turma, disciplina, situacaoDuvidaProfessorEnum, frequente, trazerDuvidaDosColegas, usuarioVO, ano, semestre);
        if (matricula != null && !matricula.trim().isEmpty()) {
            sb.append(" order by case situacaoDuvidaProfessor");
            sb.append(" when  'AGUARDANDO_RESPOSTA_ALUNO' then 1 ");
            sb.append(" when  'NOVA' then 2 ");
            sb.append(" when  'AGUARDANDO_RESPOSTA_PROFESSOR' then 3 ");
            sb.append(" when  'FINALIZADA' then 4 ");
            sb.append(" else 5 end, dataAlteracao desc");
        } else if (matricula == null || matricula.trim().isEmpty()) {
            sb.append(" order by case situacaoDuvidaProfessor");
            sb.append(" when  'NOVA' then 1 ");
            sb.append(" when  'AGUARDANDO_RESPOSTA_PROFESSOR' then 2 ");
            sb.append(" when  'AGUARDANDO_RESPOSTA_ALUNO' then 3 ");
            sb.append(" when  'FINALIZADA' then 4 ");
            sb.append(" else 5 end, dataAlteracao desc");
        }
        if (limite != null && limite > 0) {
            sb.append(" limit  ").append(limite).append(" offset ").append(pagina);
        }
        return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()));
    }

    private List<DuvidaProfessorVO> montarDadosConsulta(SqlRowSet rs) {
        List<DuvidaProfessorVO> duvidaProfessorVOs = new ArrayList<DuvidaProfessorVO>(0);
        while (rs.next()) {
            duvidaProfessorVOs.add(montarDados(rs));
        }
        return duvidaProfessorVOs;
    }

    private DuvidaProfessorVO montarDados(SqlRowSet rs) {
        DuvidaProfessorVO obj = new DuvidaProfessorVO();
        obj.setNovoObj(false);
        obj.setCodigo(rs.getInt("codigo"));
        obj.setDataAlteracao(rs.getTimestamp("dataAlteracao"));
        obj.setDataCadastro(rs.getTimestamp("dataCadastro"));
        obj.setDuvida(rs.getString("duvida"));
        obj.setDuvidaFrequente(rs.getBoolean("duvidaFrequente"));
        obj.setMatricula(rs.getString("matricula"));
        obj.setPermitePublicarDuvida(rs.getBoolean("permitePublicarDuvida"));
        obj.setSituacaoDuvidaProfessor(SituacaoDuvidaProfessorEnum.valueOf(rs.getString("situacaoDuvidaProfessor")));
        obj.getAluno().setCodigo(rs.getInt("aluno"));
        obj.getAluno().setNome(rs.getString("aluno.nome"));
        obj.getAluno().setCPF(rs.getString("aluno.cpf"));
        if (rs.getString("arquivo.pastaBaseArquivo") != null) {
            obj.getAluno().getArquivoImagem().setNome(rs.getString("arquivo.nome"));
            obj.getAluno().getArquivoImagem().setPastaBaseArquivo(rs.getString("arquivo.pastaBaseArquivo"));
        }
        obj.getTurma().setCodigo(rs.getInt("turma"));
        obj.getTurma().setIdentificadorTurma(rs.getString("turma.identificadorTurma"));
        obj.getDisciplina().setCodigo(rs.getInt("disciplina"));
        obj.getDisciplina().setNome(rs.getString("disciplina.nome"));
        return obj;
    }

    @Override
    public Integer consutarTotalRegistro(String matricula, Integer turma, Integer disciplina, SituacaoDuvidaProfessorEnum situacaoDuvidaProfessorEnum, Boolean frequente, Boolean trazerDuvidaDosColegas, UsuarioVO usuarioVO, String ano, String semestre) throws Exception {
        StringBuilder sb = new StringBuilder("");
        sb.append(" select count(distinct DuvidaProfessor.codigo) as qtde ");
        sb.append(" from DuvidaProfessor");
        sb.append(" inner join matricula on duvidaprofessor.matricula = matricula.matricula ");
        sb.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = matricula.matricula  and DuvidaProfessor.disciplina = matriculaperiodoturmadisciplina.disciplina and matriculaperiodoturmadisciplina.turma = DuvidaProfessor.turma ");
        sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
        sb.append(" inner join historico on  historico.matricula = matricula.matricula and  historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
        sb.append(" inner join disciplina  on DuvidaProfessor.disciplina = disciplina.codigo ");
        sb.append(" inner join turma  on DuvidaProfessor.turma = turma.codigo ");
        sb.append(" inner join pessoa as aluno  on DuvidaProfessor.aluno = aluno.codigo ");
        sb.append(" left join  Arquivo on Arquivo.codigo = aluno.arquivoImagem ");
        
        montarFiltroConsulta(sb, matricula, turma, disciplina, situacaoDuvidaProfessorEnum, frequente, trazerDuvidaDosColegas, usuarioVO, ano, semestre);
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        if (rs.next()) {
            return rs.getInt("qtde");
        }
        return 0;
    }
    
    public void montarFiltroConsulta(StringBuilder sb , String matricula, Integer turma, Integer disciplina, SituacaoDuvidaProfessorEnum situacaoDuvidaProfessorEnum, Boolean frequente, Boolean trazerDuvidaDosColegas, UsuarioVO usuarioVO, String ano, String semestre){
    	sb.append(" WHERE 1 = 1");
        if (matricula != null && !matricula.trim().isEmpty()) {
            if (trazerDuvidaDosColegas != null && trazerDuvidaDosColegas) {
                sb.append(" and DuvidaProfessor.matricula != '").append(matricula).append("' ");
            } else {
                sb.append(" and DuvidaProfessor.matricula = '").append(matricula).append("' ");
            }
        }
        if (turma != null && turma > 0) {
            sb.append(" and ((turma.codigo = ").append(turma).append(" or turma.codigo in (select turma from turmaagrupada where turmaagrupada.turmaorigem = ").append(turma).append(" ))");
            sb.append(" or (turma.codigo in (select t.turmaprincipal from turma t where t.codigo = ").append(turma).append(" )))");
        }
        if (disciplina != null && disciplina > 0) {
            sb.append(" and (disciplina.codigo = ").append(disciplina);
            sb.append(" or disciplina.codigo in (select disciplinaequivalente.equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina).append(") ");
            sb.append(" or disciplina.codigo in (select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina).append(") )");
        }
        if(usuarioVO.getIsApresentarVisaoProfessor()) {
        	sb.append(" and exists (").append(Historico.getSqlHorarioAulaAluno(usuarioVO.getPessoa().getCodigo(), "historico", false)).append(" ) ");
        } 
        if (situacaoDuvidaProfessorEnum != null && !situacaoDuvidaProfessorEnum.equals(SituacaoDuvidaProfessorEnum.TODAS) && !situacaoDuvidaProfessorEnum.equals(SituacaoDuvidaProfessorEnum.PENDENCIAS)) {
            sb.append(" and situacaoDuvidaProfessor = '").append(situacaoDuvidaProfessorEnum.name()).append("' ");
        }
        if (situacaoDuvidaProfessorEnum != null && situacaoDuvidaProfessorEnum.equals(SituacaoDuvidaProfessorEnum.PENDENCIAS)) {
            sb.append(" and situacaoDuvidaProfessor in ('NOVA', 'AGUARDANDO_RESPOSTA_PROFESSOR' ) ");
        }
        if (frequente != null && frequente) {
            sb.append(" and duvidaFrequente = ").append(frequente).append(" and permitePublicarDuvida = true ");
        }
        if (trazerDuvidaDosColegas != null && trazerDuvidaDosColegas) {
            sb.append(" and permitePublicarDuvida = ").append(trazerDuvidaDosColegas);
        }
        if(usuarioVO.getIsApresentarVisaoProfessor()) {
        	sb.append(" and matriculaperiodo.situacaomatriculaperiodo = 'AT' ");
        	
    		if(Uteis.isAtributoPreenchido(ano)) {
    			sb.append(" and (((turma.anual or turma.semestral) and matriculaperiodoturmadisciplina.ano = '").append(ano).append("') or (turma.anual = false and turma.semestral = false)) ");
    		}
    		if(Uteis.isAtributoPreenchido(semestre)) {
    			sb.append(" and ((turma.semestral and matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("') or turma.semestral = false) ");
    		}
        }
		
        sb.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
    }

    @Override
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirDuvidaProfessorInteracao(DuvidaProfessorVO duvidaProfessorVO, DuvidaProfessorInteracaoVO duvidaProfessorInteracaoVO, UsuarioVO usuarioVO) throws Exception {
        duvidaProfessorInteracaoVO.setDuvidaProfessor(duvidaProfessorVO);
        duvidaProfessorInteracaoVO.getPessoa().setCodigo(usuarioVO.getPessoa().getCodigo());
        duvidaProfessorInteracaoVO.getPessoa().setNome(usuarioVO.getPessoa().getNome());
        duvidaProfessorInteracaoVO.getPessoa().setArquivoImagem(usuarioVO.getPessoa().getArquivoImagem());
        duvidaProfessorInteracaoVO.setDataInteracao(new Date());
        if (usuarioVO.getIsApresentarVisaoAluno() || usuarioVO.getIsApresentarVisaoPais()) {
            if (!duvidaProfessorVO.getSituacaoDuvidaProfessor().equals(SituacaoDuvidaProfessorEnum.NOVA)) {
                duvidaProfessorVO.setSituacaoDuvidaProfessor(SituacaoDuvidaProfessorEnum.AGUARDANDO_RESPOSTA_PROFESSOR);
                getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoSolicitacaoRespostaProfessorDuvidaProfessor(duvidaProfessorVO.getCodigo(), usuarioVO);
            }
            duvidaProfessorInteracaoVO.setTipoPessoaInteracaoDuvidaProfessor(TipoPessoaInteracaoDuvidaProfessorEnum.ALUNO);
        } else {
            duvidaProfessorVO.setSituacaoDuvidaProfessor(SituacaoDuvidaProfessorEnum.AGUARDANDO_RESPOSTA_ALUNO);
            duvidaProfessorInteracaoVO.setTipoPessoaInteracaoDuvidaProfessor(TipoPessoaInteracaoDuvidaProfessorEnum.PROFESSOR);
            getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoSolicitacaoRespostaAlunoDuvidaProfessor(duvidaProfessorVO.getCodigo(), usuarioVO);
        }
        getFacadeFactory().getDuvidaProfessorInteracaoFacade().incluir(duvidaProfessorInteracaoVO);
        getFacadeFactory().getGestaoEnvioMensagemAutomaticaFacade().executarEnvioMensagemNotificacaoNovaInteracaoDuvidaProfessor(duvidaProfessorVO.getCodigo(), duvidaProfessorInteracaoVO.getTipoPessoaInteracaoDuvidaProfessor(), usuarioVO);
        alterar(duvidaProfessorVO, usuarioVO);
        duvidaProfessorVO.getDuvidaProfessorInteracaoVOs().add(0, duvidaProfessorInteracaoVO);

    }

    @Override
    public void finalizarDuvidaProfessor(DuvidaProfessorVO duvidaProfessorVO, UsuarioVO usuarioVO) throws Exception {
        SituacaoDuvidaProfessorEnum st = duvidaProfessorVO.getSituacaoDuvidaProfessor();
        try {
            duvidaProfessorVO.setSituacaoDuvidaProfessor(SituacaoDuvidaProfessorEnum.FINALIZADA);
            alterar(duvidaProfessorVO, usuarioVO);
        } catch (Exception e) {
            duvidaProfessorVO.setSituacaoDuvidaProfessor(st);
            throw e;
        }

    }

    @Override
    public List<QuadroResumoDuvidaProfessorVO> consultarResumoDuvidaProfessor(String matricula, Integer turma, Integer disciplina, UsuarioVO usuarioVO, String ano, String semestre) {
        List<QuadroResumoDuvidaProfessorVO> quadroResumoDuvidaProfessorVOs = new ArrayList<QuadroResumoDuvidaProfessorVO>(0);
        StringBuilder sb = new StringBuilder("");
        sb.append(" select situacaoDuvidaProfessor, count(distinct DuvidaProfessor.codigo) as quantidade ");
        sb.append(" from DuvidaProfessor");
        sb.append(" inner join matricula on duvidaprofessor.matricula = matricula.matricula ");
        sb.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = matricula.matricula  and DuvidaProfessor.disciplina = matriculaperiodoturmadisciplina.disciplina and matriculaperiodoturmadisciplina.turma = DuvidaProfessor.turma ");
        sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
        sb.append(" inner join historico on  historico.matricula = matricula.matricula and  historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");        
        sb.append(" inner join pessoa on pessoa.codigo = DuvidaProfessor.aluno");
        sb.append(" left join Arquivo on Arquivo.codigo = pessoa.arquivoImagem");
        sb.append(" inner join disciplina on DuvidaProfessor.disciplina = disciplina.codigo");
        sb.append(" inner join turma on DuvidaProfessor.turma = turma.codigo");
        sb.append(" WHERE 1 = 1");
        if (matricula != null && !matricula.trim().isEmpty()) {
            sb.append(" and DuvidaProfessor.matricula = '").append(matricula).append("' ");
        }
        if (turma != null && turma > 0) {
            sb.append(" and ((turma.codigo = ").append(turma).append(" or turma.codigo in (select turma from turmaagrupada where turmaagrupada.turmaorigem = ").append(turma).append(" ))");
            sb.append(" or (turma.codigo in (select t.turmaprincipal from turma t where t.codigo = ").append(turma).append(" )))");
        }
        if (disciplina != null && disciplina > 0) {
            sb.append(" and (disciplina.codigo = ").append(disciplina);
            sb.append(" or disciplina.codigo in (select disciplinaequivalente.equivalente from disciplinaequivalente where disciplinaequivalente.disciplina = ").append(disciplina).append(") ");
            sb.append(" or disciplina.codigo in (select disciplinaequivalente.disciplina from disciplinaequivalente where disciplinaequivalente.equivalente = ").append(disciplina).append(") )");
        }
        if(usuarioVO.getIsApresentarVisaoProfessor()) {

            sb.append(" and matriculaperiodo.situacaomatriculaperiodo = 'AT' ");
            	
    		if(Uteis.isAtributoPreenchido(ano)) {
    			sb.append(" and (((turma.anual or turma.semestral) and matriculaperiodoturmadisciplina.ano = '").append(ano).append("') or (turma.anual = false and turma.semestral = false)) ");
    		}
    		if(Uteis.isAtributoPreenchido(semestre)) {
    			sb.append(" and ((turma.semestral and matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("') or turma.semestral = false) ");
    		}
        	sb.append(" and exists (").append(Historico.getSqlHorarioAulaAluno(usuarioVO.getPessoa().getCodigo(), "historico", false)).append(" ) ");
        } 
        sb.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
        sb.append(" group by situacaoDuvidaProfessor");
        if (matricula != null && !matricula.trim().isEmpty()) {
            quadroResumoDuvidaProfessorVOs.add(new QuadroResumoDuvidaProfessorVO(SituacaoDuvidaProfessorEnum.AGUARDANDO_RESPOSTA_ALUNO, 0));
            quadroResumoDuvidaProfessorVOs.add(new QuadroResumoDuvidaProfessorVO(SituacaoDuvidaProfessorEnum.NOVA, 0));
            quadroResumoDuvidaProfessorVOs.add(new QuadroResumoDuvidaProfessorVO(SituacaoDuvidaProfessorEnum.AGUARDANDO_RESPOSTA_PROFESSOR, 0));
            quadroResumoDuvidaProfessorVOs.add(new QuadroResumoDuvidaProfessorVO(SituacaoDuvidaProfessorEnum.FINALIZADA, 0));

            sb.append("  order by case situacaoDuvidaProfessor");
            sb.append(" when  'AGUARDANDO_RESPOSTA_ALUNO' then 1 ");
            sb.append(" when  'NOVA' then 2 ");
            sb.append(" when  'AGUARDANDO_RESPOSTA_PROFESSOR' then 3 ");
            sb.append(" when  'FINALIZADA' then 4 ");
            sb.append(" else 5 end ");
        } else if (matricula == null || matricula.trim().isEmpty()) {
            quadroResumoDuvidaProfessorVOs.add(new QuadroResumoDuvidaProfessorVO(SituacaoDuvidaProfessorEnum.NOVA, 0));
            quadroResumoDuvidaProfessorVOs.add(new QuadroResumoDuvidaProfessorVO(SituacaoDuvidaProfessorEnum.AGUARDANDO_RESPOSTA_PROFESSOR, 0));
            quadroResumoDuvidaProfessorVOs.add(new QuadroResumoDuvidaProfessorVO(SituacaoDuvidaProfessorEnum.AGUARDANDO_RESPOSTA_ALUNO, 0));
            quadroResumoDuvidaProfessorVOs.add(new QuadroResumoDuvidaProfessorVO(SituacaoDuvidaProfessorEnum.FINALIZADA, 0));
            sb.append(" order by case situacaoDuvidaProfessor");
            sb.append(" when  'NOVA' then 1 ");
            sb.append(" when  'AGUARDANDO_RESPOSTA_PROFESSOR' then 2 ");
            sb.append(" when  'AGUARDANDO_RESPOSTA_ALUNO' then 3 ");
            sb.append(" when  'FINALIZADA' then 4 ");
            sb.append(" else 5 end");
        }
        SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        int qtdeTotal = 0;
        int qtdePendenciaProfessor = 0;
        q: while (rs.next()) {
            for (QuadroResumoDuvidaProfessorVO quadroResumoDuvidaProfessorVO : quadroResumoDuvidaProfessorVOs) {
                if (quadroResumoDuvidaProfessorVO.getSituacaoDuvidaProfessor().name().equals(rs.getString("situacaoDuvidaProfessor"))) {
                    qtdeTotal += rs.getInt("quantidade");
                    quadroResumoDuvidaProfessorVO.setQuantidade(rs.getInt("quantidade"));
                if (quadroResumoDuvidaProfessorVO.getSituacaoDuvidaProfessor().equals(SituacaoDuvidaProfessorEnum.AGUARDANDO_RESPOSTA_PROFESSOR) || quadroResumoDuvidaProfessorVO.getSituacaoDuvidaProfessor().equals(SituacaoDuvidaProfessorEnum.NOVA)) {
                	qtdePendenciaProfessor += rs.getInt("quantidade");
				}    
                    continue q;
                }
            }
        }
        quadroResumoDuvidaProfessorVOs.add(new QuadroResumoDuvidaProfessorVO(SituacaoDuvidaProfessorEnum.TODAS, qtdeTotal));
        quadroResumoDuvidaProfessorVOs.add(new QuadroResumoDuvidaProfessorVO(SituacaoDuvidaProfessorEnum.PENDENCIAS, qtdePendenciaProfessor));
        return quadroResumoDuvidaProfessorVOs;
    }
    
    @Override
    public Integer consultarQtdeAtualizacaoDuvidaPorUsuarioAluno(String matricula, MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO){
        try {
            if (matricula != null && !matricula.trim().isEmpty() && matriculaPeriodoTurmaDisciplinaVO!= null && matriculaPeriodoTurmaDisciplinaVO.getCodigo()>0) {
            	StringBuilder sb = new StringBuilder("select count(distinct duvidaprofessor.codigo) as qtde from duvidaprofessor ");
                sb.append(" inner join matricula on duvidaprofessor.matricula = matricula.matricula ");                
                sb.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = matricula.matricula  and DuvidaProfessor.disciplina = matriculaperiodoturmadisciplina.disciplina and matriculaperiodoturmadisciplina.turma = DuvidaProfessor.turma ");
                sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
                sb.append(" inner join historico on  historico.matricula = matricula.matricula and  historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
                sb.append(" where situacaoduvidaprofessor = 'AGUARDANDO_RESPOSTA_ALUNO'  ");                
                sb.append(" and matricula.matricula = '").append(matricula).append("' ");
                sb.append(" and duvidaprofessor.turma = ").append(matriculaPeriodoTurmaDisciplinaVO.getTurma().getCodigo());
                sb.append(" and duvidaprofessor.disciplina = ").append(matriculaPeriodoTurmaDisciplinaVO.getDisciplina().getCodigo());
                sb.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
                SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
                if (rs.next()) {
                    return rs.getInt("qtde");
                }         
            }
            return 0;                          
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public Integer consultarQtdeAtualizacaoDuvidaPorUsuarioProfessor(UsuarioVO usuario, Integer unidadeEnsino, ConfiguracaoGeralSistemaVO configuracaoGeralSistemaVO) {
        try {        
            if (usuario.getIsApresentarVisaoProfessor()) {
                StringBuilder sb = new StringBuilder("select count(distinct duvidaprofessor.codigo) as qtde from duvidaprofessor ");
                sb.append(" inner join matricula on duvidaprofessor.matricula = matricula.matricula ");
                sb.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = matricula.matricula  and DuvidaProfessor.disciplina = matriculaperiodoturmadisciplina.disciplina and matriculaperiodoturmadisciplina.turma = DuvidaProfessor.turma ");
                sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
                sb.append(" inner join historico on  historico.matricula = matricula.matricula and  historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
                sb.append(" inner join turma on matriculaperiodoturmadisciplina.turma = turma.codigo ");
                sb.append(" left join periodoauladisciplinaaluno(historico.codigo, false, "+usuario.getPessoa().getCodigo()+") as horario on horario.modalidadedisciplina in ('ON_LINE', 'PRESENCIAL') ");
                sb.append(" where ");
                sb.append(" situacaoduvidaprofessor in ('NOVA', 'AGUARDANDO_RESPOSTA_PROFESSOR')  ");
                sb.append(" and ((matriculaperiodoturmadisciplina.modalidadedisciplina = 'PRESENCIAL' and horario.professor_codigo is not null)  ");
                sb.append(" or (matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE' and matriculaperiodoturmadisciplina.professor = ").append(usuario.getPessoa().getCodigo()).append("))");
                sb.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
                sb.append(" and ((turma.semestral and matriculaperiodoturmadisciplina.ano = extract(year from current_date)::varchar and matriculaperiodoturmadisciplina.semestre = '").append(Uteis.getSemestreAtual()).append("')");
                sb.append(" or (turma.anual and matriculaperiodoturmadisciplina.ano = extract(year from current_date)::varchar )");
                sb.append(" or (turma.anual = false and turma.semestral = false))");
                SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
                if (rs.next()) {
                    return rs.getInt("qtde");
                }
            }
            return 0;                          
        } catch (Exception e) {
            return 0;
        }
    }
    
    @Override
    public SqlRowSet consultarProfessorQueTemDuvidasAResponder(UsuarioVO usuarioVO) throws Exception {
    	  	
    	StringBuilder sqlStr = new StringBuilder();
    	
    	sqlStr.append(" select professor.codigo as professor, professor.email, professor.nome as nomeprofessor,");
    	sqlStr.append(" aluno.codigo as aluno, aluno.email as emailaluno, aluno.nome as nomealuno,");
    	sqlStr.append(" duvidaprofessor.dataalteracao, notificacaoprofessordiasduvidasnaorespondidas, matricula.unidadeensino as unidadeensino ");
    	sqlStr.append(" from duvidaprofessor");
    	sqlStr.append(" inner join matricula on duvidaprofessor.matricula = matricula.matricula ");
    	sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = matricula.matricula  and DuvidaProfessor.disciplina = matriculaperiodoturmadisciplina.disciplina and matriculaperiodoturmadisciplina.turma = DuvidaProfessor.turma ");
    	sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
    	sqlStr.append(" inner join historico on  historico.matricula = matricula.matricula and  historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
    	if(usuarioVO != null && usuarioVO.getIsApresentarVisaoProfessor()){
    		sqlStr.append(" inner join periodoauladisciplinaaluno(historico.codigo, false, "+usuarioVO.getPessoa().getCodigo()+") as horario on horario.modalidadedisciplina in ('ON_LINE', 'PRESENCIAL') ");
        }else{
        	sqlStr.append(" inner join periodoauladisciplinaaluno(historico.codigo) as horario on horario.modalidadedisciplina in ('ON_LINE', 'PRESENCIAL') ");
        }
    	sqlStr.append(" inner join pessoa as professor on professor.codigo =  any (horario.professores_codigo) ");
    	sqlStr.append(" inner join pessoa as aluno on aluno.codigo = matricula.aluno");
    	sqlStr.append(" inner join turma on turma.codigo = duvidaprofessor.turma");
    	sqlStr.append(" inner join configuracaoead on configuracaoead.codigo = turma.configuracaoead");
    	sqlStr.append(" left join PersonalizacaoMensagemAutomatica as mensagem on mensagem.templateMensagemAutomaticaEnum = 'MENSAGEM_NOTIFICACAO_PROFESSOR_DUVIDAS_NAO_RESPONDIDAS'");
    	sqlStr.append(" where situacaoduvidaprofessor in ('AGUARDANDO_RESPOSTA_PROFESSOR' , 'NOVA')");
    	sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
    	sqlStr.append(" and notificarprofessorduvidasnaorespondidas");
    	sqlStr.append(" and (");
    	sqlStr.append(" EXTRACT( DAYS FROM (current_timestamp-dataalteracao)) =  notificacaoprofessordiasduvidasnaorespondidas and mensagem.desabilitarEnvioMensagemAutomatica = false");
    	sqlStr.append(" )");
    	
    	return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    }
    
    /**
     * 
     * @param codigoDuvidaProfessor
     * @return
     * @throws Exception
     */
    @Override
    public SqlRowSet consultarDadosEnvioNotificacaoDuvidaProfessor(Integer codigoDuvidaProfessor) throws Exception {
    	
    	StringBuilder sqlStr = new StringBuilder();
    	
    	sqlStr.append(" select professor.codigo as professor, professor.nome as nomeprofessor, professor.email as emailprofessor,");
    	sqlStr.append(" aluno.codigo as aluno, aluno.nome as nomealuno, aluno.email as emailaluno, duvidaprofessor.duvida as assunto, matricula.unidadeensino as unidadeensino");
    	sqlStr.append(" from duvidaprofessor");
    	sqlStr.append(" inner join matricula on duvidaprofessor.matricula = matricula.matricula ");
    	sqlStr.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = matricula.matricula  and DuvidaProfessor.disciplina = matriculaperiodoturmadisciplina.disciplina and matriculaperiodoturmadisciplina.turma = DuvidaProfessor.turma ");
    	sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
    	sqlStr.append(" inner join historico on  historico.matricula = matricula.matricula and  historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
    	sqlStr.append(" inner join periodoauladisciplinaaluno(historico.codigo) as horario on horario.modalidadedisciplina in ('ON_LINE', 'PRESENCIAL') ");
    	sqlStr.append(" inner join pessoa as professor on professor.codigo =  any (horario.professores_codigo) ");
    	sqlStr.append(" inner join pessoa as aluno on aluno.codigo = matricula.aluno");    	
    	sqlStr.append(" where duvidaprofessor.codigo = ").append(codigoDuvidaProfessor);
    	sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
    	
    	return getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
    }
    
    @Override
	public List<DuvidaProfessorVO> consultarAtualizacaoDuvidaPorUsuarioProfessor(UsuarioVO usuario, Integer unidadeEnsino, String ano, String semestre) throws Exception {
		StringBuilder sb = new StringBuilder(getSqlSelectConsulta(usuario, null));
		if (usuario.getIsApresentarVisaoProfessor()) {
			sb.append(" inner join periodoauladisciplinaaluno(historico.codigo, false, " + usuario.getPessoa().getCodigo() + ") as horario on horario.modalidadedisciplina in ('ON_LINE', 'PRESENCIAL') ");
			sb.append(" where ");
			sb.append(" situacaoduvidaprofessor in ('NOVA', 'AGUARDANDO_RESPOSTA_PROFESSOR')  ");
	        if(usuario.getIsApresentarVisaoProfessor()) {
	        	sb.append(" and matriculaperiodo.situacaomatriculaperiodo = 'AT' ");	
	    		if(Uteis.isAtributoPreenchido(ano)) {
	    			sb.append(" and (((turma.anual or turma.semestral) and matriculaperiodoturmadisciplina.ano = '").append(ano).append("') or (turma.anual = false and turma.semestral = false)) ");
	    		}
	    		if(Uteis.isAtributoPreenchido(semestre)) {
	    			sb.append(" and ((turma.semestral and matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("') or turma.semestral = false) ");
	    		}
	        }
			sb.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
		}
		return montarDadosConsulta(getConexao().getJdbcTemplate().queryForRowSet(sb.toString()));
	}
    
	@Override
	public Integer consultarTotalRegistroAtualizacaoDuvidaPorProfessor(UsuarioVO usuario, Integer unidadeEnsino, String ano, String semestre) throws Exception {
		StringBuilder sb = new StringBuilder("");
        sb.append("select count (duvidaprofessor.codigo) as qtde from duvidaprofessor  ");
        sb.append(" inner join matricula on duvidaprofessor.matricula = matricula.matricula ");
        sb.append(" inner join matriculaperiodoturmadisciplina on matriculaperiodoturmadisciplina.matricula = matricula.matricula  and DuvidaProfessor.disciplina = matriculaperiodoturmadisciplina.disciplina and matriculaperiodoturmadisciplina.turma = DuvidaProfessor.turma ");
        sb.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo ");
        sb.append(" inner join historico on  historico.matricula = matricula.matricula and  historico.matriculaperiodoturmadisciplina = matriculaperiodoturmadisciplina.codigo ");
        sb.append(" inner join periodoauladisciplinaaluno(historico.codigo, false, "+usuario.getPessoa().getCodigo()+") as horario on horario.modalidadedisciplina in ('ON_LINE', 'PRESENCIAL') ");
        sb.append(" inner join disciplina  on duvidaprofessor.disciplina = disciplina.codigo ");
        sb.append(" inner join turma  on duvidaprofessor.turma = turma.codigo ");
        sb.append(" where ");
        sb.append(" situacaoduvidaprofessor in ('NOVA', 'AGUARDANDO_RESPOSTA_PROFESSOR')  ");
        sb.append(" and ((matriculaperiodoturmadisciplina.modalidadedisciplina = 'PRESENCIAL' and horario.professor_codigo is not null)  ");
        sb.append(" or (matriculaperiodoturmadisciplina.modalidadedisciplina = 'ON_LINE' and matriculaperiodoturmadisciplina.professor = ").append(usuario.getPessoa().getCodigo()).append("))");
        if(usuario.getIsApresentarVisaoProfessor()) {
        	sb.append(" and matriculaperiodo.situacaomatriculaperiodo = 'AT' ");	
    		if(Uteis.isAtributoPreenchido(ano)) {
    			sb.append(" and (((turma.anual or turma.semestral) and matriculaperiodoturmadisciplina.ano = '").append(ano).append("') or (turma.anual = false and turma.semestral = false)) ");
    		}
    		if(Uteis.isAtributoPreenchido(semestre)) {
    			sb.append(" and ((turma.semestral and matriculaperiodoturmadisciplina.semestre = '").append(semestre).append("') or turma.anual = false or turma.semestral = false) ");
    		}
        }
        sb.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));   
		SqlRowSet rs = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		if (rs.next()) {
			return rs.getInt("qtde");
		}
		return 0;
	}
}
