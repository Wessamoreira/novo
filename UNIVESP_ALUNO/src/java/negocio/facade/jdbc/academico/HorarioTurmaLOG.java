/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

/**
 *
 * @author Carlos
 */
@Repository
@Scope("singleton")
@Lazy
public class HorarioTurmaLOG extends ControleAcesso  {

//    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//    public void incluir(final HorarioTurmaLOGVO obj) throws Exception {
//        final String sql = "INSERT INTO HorarioTurmaLog( usuario, data, acao, resultadoAcao, horarioTurma ) VALUES ( ?, ?, ?, ?, ? ) returning codigo";
//        obj.setCodigo((Integer) getConexao().getJdbcTemplate().query(new PreparedStatementCreator() {
//
//            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
//                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
//                if (obj.getUsuarioVO().getCodigo() != 0) {
//                    sqlInserir.setInt(1, obj.getUsuarioVO().getCodigo());
//                } else {
//                    sqlInserir.setNull(1, 0);
//                }
//                sqlInserir.setTimestamp(2, Uteis.getDataJDBCTimestamp(obj.getData()));
//                sqlInserir.setString(3, obj.getAcao());
//                sqlInserir.setString(4, obj.getResultadoAcao());
//                if (obj.getHorarioTurma().getCodigo() != 0) {
//                    sqlInserir.setInt(5, obj.getHorarioTurma().getCodigo());
//                } else {
//                    sqlInserir.setNull(5, 0);
//                }
//                return sqlInserir;
//            }
//        }, new ResultSetExtractor() {
//
//            public Object extractData(ResultSet arg0) throws SQLException, DataAccessException {
//                if (arg0.next()) {
//                    obj.setNovoObj(Boolean.FALSE);
//                    return arg0.getInt("codigo");
//                }
//                return null;
//            }
//        }));
//    }

//    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
//    public List<HorarioTurmaLOGVO> consultarLogPorTurmaPeriodoAnoSemestre(Integer turma, Date dataInicio, Date dataFim, String ano, String semestre) throws Exception {
//        throw new UnsupportedOperationException("Not supported yet.");
//    }

    /**
     * Método responsável por realizar o log do horário da turma no momento de remover um horario turma Dia.
     * @param horarioTurmaVO
     * @param professorRemovido
     * @param disciplinaRemovida
     * @param nomeProfessor
     * @param nomeDisciplina
     * @param usuarioLogado
     * @throws Exception
     * @Autor carlos
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarLogRemocaoHorarioTurmaDiaItemTelaHorarioDetalhadoDoDiaTelaPrincipal( Integer professorRemovido, Integer disciplinaRemovida, String nomeProfessor, String nomeDisciplina, UsuarioVO usuarioLogado) throws Exception {
//        HorarioTurmaLOGVO log = new HorarioTurmaLOGVO();
//        log.setHorarioTurma(horarioTurmaVO);
//        log.setAcao("Remoção horário turma item");
//        log.setData(new Date());
//        log.setUsuarioVO(usuarioLogado);
//        log.setResultadoAcao("Foi removido um Horário Turma Item no dia: " + Uteis.getDataComHora(log.getData()) + " referente ao dia " + Uteis.getData(disponibilidadeHorarioProfessorVO.getData(), "dd/MM/yyyy") + ", horário: " + hrProfDiaItem.getHorario() + ". ---- Código/nome do professor removido era: " + professorRemovido.intValue() + ""
//                + "/" + nomeProfessor + ". ---- Código/disciplina era: " + disciplinaRemovida + "/" + nomeDisciplina + " ---- Código/IdentificadorTurma era: " + horarioTurmaVO.getTurma().getCodigo() + "/" + horarioTurmaVO.getTurma().getIdentificadorTurma() + " ---- Usuário: " + usuarioLogado.getNome() + ""
//                + " ");
//        incluir(log);
    }

    /**
     * Método responsável por criar o log no momento de realizar a substituição dos professores, alterações de disciplinas.
     * @param horarioTurmaVO
     * @param usuarioLogado
     * @param nrAula
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarLogAlteracaoDisciplinaProfessorAtualParaDisciplinaProfessorSubstituto( UsuarioVO usuarioLogado, Integer nrAula) throws Exception {
//        HorarioTurmaLOGVO log = new HorarioTurmaLOGVO();
//        log.setHorarioTurma(horarioTurmaVO);
//        log.setAcao("Substituição Disciplina e Professor");
//        log.setData(new Date());
//        log.setUsuarioVO(usuarioLogado);
//        log.setResultadoAcao("No dia " + Uteis.getDataComHora(log.getData()) + ", foi feita alteração da Disciplina( Código/Disciplina(Atual): " + horarioTurmaVO.getDisciplinaAtual().getCodigo() + "/" + horarioTurmaVO.getDisciplinaAtual().getNome() + " "
//                + "pela Código/Disciplina(Substituta): " + horarioTurmaVO.getDisciplinaSubstituta().getCodigo() + "/" + horarioTurmaVO.getDisciplinaSubstituta().getNome() + " ) e do Professor( Código/Professor(Atual): "
//                + " " + horarioTurmaVO.getProfessorAtual().getCodigo() + "/" + horarioTurmaVO.getProfessorAtual().getNome() + " pelo Código/Professor(Substituto(a)): " + horarioTurmaVO.getProfessorSubstituto().getCodigo() + "/" + horarioTurmaVO.getProfessorSubstituto().getNome() + ""
//                + " ) pelo Código/Usuário: " + usuarioLogado.getCodigo() + "/ " + usuarioLogado.getNome() + "");
//        if (nrAula == 0) {
//            log.setResultadoAcao(log.getResultadoAcao().concat(" - alterado em Todas aulas"));
//        } else {
//            log.setResultadoAcao(log.getResultadoAcao().concat(" - alterado na Aula " + nrAula + "ª"));
//        }
//        incluir(log);
    }

    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void realizarCriacaoLogHorarioTurma(UsuarioVO usuarioLogado, String acao, String resultadoAcao) throws Exception {
//        HorarioTurmaLOGVO log = new HorarioTurmaLOGVO();
//        log.setHorarioTurma(horarioTurmaVO);
//        log.setAcao(acao);
//        log.setData(new Date());
//        log.setUsuarioVO(usuarioLogado);
//        log.setResultadoAcao(resultadoAcao);
//        incluir(log);
    }

    
    /**
     * Método responsável por criar o log do Horário da Turma no momento em q estiver removendo um horário turma dia pela tela do modal.
     * Será gravado no log as seguintes informações: O código do Horário da turma, a Ação q está sendo executado, a data de remoção, o usuário
     * responsável e as informações do horário Turma dia.
     * @param obj
     * @param usuarioLogado
     * @throws Exception
     * @Autor Carlos
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarLogExclusaoHorarioTurmaDiaTelaAlterarExcluirHorarioPorProfessorTelaModal( UsuarioVO usuarioLogado) throws Exception {
//        HorarioTurmaLOGVO log = new HorarioTurmaLOGVO();
//        log.getHorarioTurma().setCodigo(obj.getHorarioTurma().getCodigo());
//        log.setAcao("Alterar/Excluir Horário por Professor (Exclusão de todos horários do dia)");
//        log.setData(new Date());
//        log.setUsuarioVO(usuarioLogado);        
//        log.setResultadoAcao("Foi removido o horário do professor no dia: " + Uteis.getDataComHora(log.getData()) + ". ---- Código HorarioTurmaDia removido: " + obj.getCodigo() + ". "
//                + "---- HorárioTurmaDiaItem: (Código/Nome Disciplina): " + horarioItem.getDisciplinaVO().getCodigo() + "/ " + horarioItem.getDisciplinaVO().getNome() + ""
//                + "---- HorárioTurmaDiaItem: (Código/Nome Professor): " + horarioItem.getFuncionarioVO().getCodigo() + "/ " + horarioItem.getFuncionarioVO().getNome() + "."
//                + "---- Horário Aula: " + horarioItem.getHorario() + ", ----Data Aula: " + obj.getData_Apresentar() + ", ---- Dia da semana: " + obj.getDiaSemanaEnum() + ". "
//                + " ----Usuário responsável: " + usuarioLogado.getNome() + "");
//        if (horarioItem.getFuncionarioVO().getCodigo() != 0 && !horarioItem.getFuncionarioVO().getNome().equals("")) {
//        	incluir(log);
//        }
//        log.setResultadoAcao("");

    }

    /**
     * Método responsável por criar o log no momento de gravar um Horário Turma,
     * o Log consta o código do Horário da Turma a Data com hora, o código do Horário Turma Dia, o código da Disciplina com o nome da mesma,
     * o código do professor junto com o nome, o horário da aula, a data do dia da aula, o dia da semana que foi alterado e o usuário responsável pela
     * alteração.
     * @param obj
     * @param usuarioLogado
     * @throws Exception
     * @Autor Carlos
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarLogGravarAlteracao( UsuarioVO usuarioLogado) throws Exception {
//        HorarioTurmaLOGVO log = new HorarioTurmaLOGVO();
//        log.getHorarioTurma().setCodigo(obj.getHorarioTurma().getCodigo());
//        log.setAcao("Gravar/Alterar (Horário Turma)");
//        log.setData(new Date());
//        log.setUsuarioVO(usuarioLogado);
//        for (HorarioTurmaDiaItemVO horarioItem : obj.getHorarioTurmaDiaItemVOs()) {
//            log.setResultadoAcao("Alteração HORÁRIO TURMA - código " + obj.getHorarioTurma() + " no DIA: " + Uteis.getDataComHora(log.getData()) + ". ---- Código HORARIO TURMA DIA alterado: " + obj.getCodigo() + ". "
//                    + "---- HorárioTurmaDiaItem: (Código/Nome Disciplina): " + horarioItem.getDisciplinaVO().getCodigo() + "/ " + horarioItem.getDisciplinaVO().getNome() + ""
//                    + "---- HorárioTurmaDiaItem: (Código/Nome Professor): " + horarioItem.getFuncionarioVO().getCodigo() + "/ " + horarioItem.getFuncionarioVO().getNome() + "."
//                    + "---- Horário Aula: " + horarioItem.getHorario() + ", ----Data Aula: " + obj.getData_Apresentar() + ", ---- Dia da semana: " + obj.getDiaSemanaEnum() + ". "
//                    + " ----Usuário responsável: " + usuarioLogado.getNome() + "");
//            incluir(log);
//            log.setResultadoAcao("");
//
//        }
    }

    /**
     * Método responsável por realizar o log no momento da Inclusão de uma programação de aula (Horário Turma).
     * @param obj
     * @param usuarioLogado
     * @throws Exception
     * @autor Carlos
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarLogGravarInclusao( UsuarioVO usuarioLogado) throws Exception {
//        HorarioTurmaLOGVO log = new HorarioTurmaLOGVO();
//        log.getHorarioTurma().setCodigo(obj.getHorarioTurma().getCodigo());
//        log.setAcao("Gravar/Incluir (Horário Turma)");
//        log.setData(new Date());
//        log.setUsuarioVO(usuarioLogado);
//        for (HorarioTurmaDiaItemVO horarioItem : obj.getHorarioTurmaDiaItemVOs()) {
//            log.setResultadoAcao("Inclusão HORÁRIO TURMA - código " + obj.getHorarioTurma() + " no DIA: " + Uteis.getDataComHora(log.getData()) + ". ---- Código HORARIO TURMA DIA incluido: " + obj.getCodigo() + ". "
//                    + "---- HorárioTurmaDiaItem: (Código/Nome Disciplina): " + horarioItem.getDisciplinaVO().getCodigo() + "/ " + horarioItem.getDisciplinaVO().getNome() + ""
//                    + "---- HorárioTurmaDiaItem: (Código/Nome Professor): " + horarioItem.getFuncionarioVO().getCodigo() + "/ " + horarioItem.getFuncionarioVO().getNome() + "."
//                    + "---- Horário Aula: " + horarioItem.getHorario() + ", ----Data Aula: " + obj.getData_Apresentar() + ", ---- Dia da semana: " + obj.getDiaSemanaEnum() + ". "
//                    + " ----Usuário responsável: " + usuarioLogado.getNome() + "");
//            incluir(log);
//            log.setResultadoAcao("");
//
//        }
    }

    /**
     * Método responsável por realizar o Log no momento da exclusão da Programação de aula.
     * @param obj
     * @param usuarioLogado
     * @throws Exception
     */
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void executarLogExclusaoProgramacaoAula( UsuarioVO usuarioLogado) throws Exception {
//        HorarioTurmaLOGVO log = new HorarioTurmaLOGVO();
//        log.getHorarioTurma().setCodigo(obj.getCodigo());
//        log.setAcao("Excluir (Programação de Aula)");
//        log.setData(new Date());
//        log.setUsuarioVO(usuarioLogado);
//        for (HorarioTurmaDiaVO horarioDia : obj.getHorarioTurmaDiaVOs()) {
//            for (HorarioTurmaDiaItemVO horarioItem : horarioDia.getHorarioTurmaDiaItemVOs()) {
//                log.setResultadoAcao("Exclusão HORÁRIO TURMA - código " + obj.getCodigo() + " no DIA: " + Uteis.getDataComHora(log.getData()) + ". ---- Código HORARIO TURMA DIA incluido: " + obj.getCodigo() + ". "
//                    + "---- HorárioTurmaDiaItem: (Código/Nome Disciplina): " + horarioItem.getDisciplinaVO().getCodigo() + "/ " + horarioItem.getDisciplinaVO().getNome() + ""
//                    + "---- HorárioTurmaDiaItem: (Código/Nome Professor): " + horarioItem.getFuncionarioVO().getCodigo() + "/ " + horarioItem.getFuncionarioVO().getNome() + "."
//                    + "---- Horário Aula: " + horarioItem.getHorario() + ", ----Data Aula: " + horarioDia.getData_Apresentar() + ", ---- Dia da semana: " + horarioDia.getDiaSemanaEnum() + ". "
//                    + " ----Usuário responsável: " + usuarioLogado.getNome() + "");
//            if (horarioItem.getDisciplinaVO().getCodigo() != 0) {
//                incluir(log);
//            }
//            log.setResultadoAcao("");
//            }
//        }
    }
}
