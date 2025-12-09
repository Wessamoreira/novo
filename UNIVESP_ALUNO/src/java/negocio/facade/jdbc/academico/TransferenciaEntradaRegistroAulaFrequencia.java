package negocio.facade.jdbc.academico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.TransferenciaEntradaRegistroAulaFrequenciaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.TransferenciaEntradaRegistroAulaFrequenciaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class TransferenciaEntradaRegistroAulaFrequencia extends ControleAcesso implements TransferenciaEntradaRegistroAulaFrequenciaInterfaceFacade {

	private static final long serialVersionUID = 1L;

	public TransferenciaEntradaRegistroAulaFrequencia() {
		super();
		setIdEntidade("TransferenciaEntrada");
	}
	
	public void validarDados(TransferenciaEntradaRegistroAulaFrequenciaVO obj) throws Exception {
		
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final TransferenciaEntradaRegistroAulaFrequenciaVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj);
        final String sql = "INSERT INTO TransferenciaEntradaRegistroAulaFrequencia( transferenciaEntrada, turma, disciplina, professor, dataRegistroAula, ano, semestre, horario, presente, abonado, conteudo, diaSemana, registroAula ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                sqlInserir.setInt(1, obj.getTransferenciaEntradaVO().getCodigo().intValue());
                sqlInserir.setInt(2, obj.getTurmaVO().getCodigo().intValue());
                sqlInserir.setInt(3, obj.getDisciplinaVO().getCodigo().intValue());
                sqlInserir.setInt(4, obj.getProfessorVO().getCodigo().intValue());
                sqlInserir.setDate(5, Uteis.getDataJDBC(obj.getDataRegistroAula()));
                sqlInserir.setString(6, obj.getAno());
                sqlInserir.setString(7, obj.getSemestre());
                sqlInserir.setString(8, obj.getHorario());
                sqlInserir.setBoolean(9, obj.getPresente());
                sqlInserir.setBoolean(10, obj.getAbonado());
                sqlInserir.setString(11, obj.getConteudo());
                sqlInserir.setString(12, obj.getDiaSemana());
                sqlInserir.setInt(13, obj.getRegistroAulaVO().getCodigo());
                return sqlInserir;
            }
        });

        obj.setNovoObj(Boolean.FALSE);
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirTransferenciaEntradaRegistroAulaFrequenciaVOs(Integer transferenciaEntrada, List objetos, UsuarioVO usuario) throws Exception {
        Iterator e = objetos.iterator();
        while (e.hasNext()) {
        	TransferenciaEntradaRegistroAulaFrequenciaVO obj = (TransferenciaEntradaRegistroAulaFrequenciaVO) e.next();
            obj.getTransferenciaEntradaVO().setCodigo(transferenciaEntrada);
            incluir(obj, usuario);
        }
    }
    
    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirTransferenciaEntradaRegistroAulaFrequenciaVOs(Integer transferenciaEntrada, UsuarioVO usuario) throws Exception {
        String sql = "DELETE FROM TransferenciaEntradaRegistroAulaFrequencia WHERE (transferenciaEntrada = ?)"+ adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{transferenciaEntrada});
    }
    
    public List<TransferenciaEntradaRegistroAulaFrequenciaVO> consultarRegistroAulaPorTurmaAnoSemestreTransferenciaInterna(Integer turma, String matricula, String ano, String semestre, UsuarioVO usuarioVO) {
		StringBuilder sb = new StringBuilder();
		sb.append("select registroaula.codigo, registroaula.data, registroaula.conteudo, registroaula.diasemana, registroaula.ano, registroaula.semestre, registroaula.horario, ");
		sb.append(" turma.codigo AS \"turma.codigo\", turma.identificadorturma, disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", ");
		sb.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\", turno.duracaoAula ");
		sb.append(" from registroaula ");
		sb.append(" inner join turma on turma.codigo = registroaula.turma ");
		sb.append(" inner join turno on turno.codigo = turma.turno ");
		sb.append(" inner join disciplina on disciplina.codigo = registroaula.disciplina and disciplina.codigo in(");
		sb.append(" select mptd.disciplina from matriculaPeriodoTurmaDisciplina mptd ");
		sb.append(" inner join matriculaPeriodo on matriculaPeriodo.codigo = mptd.matriculaPeriodo ");
		sb.append(" where mptd.matricula = '").append(matricula).append("' ");
		if (!ano.equals("")) {
			sb.append(" and matriculaPeriodo.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("")) {
			sb.append(" and matriculaPeriodo.semestre = '").append(semestre).append("' ");
		}
		sb.append(") ");
		sb.append(" inner join pessoa on pessoa.codigo = registroaula.professor ");
		sb.append(" where turma = ").append(turma);
		if (!ano.equals("")) {
			sb.append(" and registroAula.ano = '").append(ano).append("' ");
		}
		if (!semestre.equals("")) {
			sb.append(" and registroAula.semestre = '").append(semestre).append("' ");
		}
		sb.append(" order by data, disciplina.nome, horario");
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
		List<TransferenciaEntradaRegistroAulaFrequenciaVO> listaRegistroAulaVOs = new ArrayList<TransferenciaEntradaRegistroAulaFrequenciaVO>(0);
		while (tabelaResultado.next()) {
			TransferenciaEntradaRegistroAulaFrequenciaVO obj = new TransferenciaEntradaRegistroAulaFrequenciaVO();
			obj.getRegistroAulaVO().setCodigo(tabelaResultado.getInt("codigo"));
			obj.setDataRegistroAula(tabelaResultado.getDate("data"));
			obj.setAno(tabelaResultado.getString("ano"));
			obj.setSemestre(tabelaResultado.getString("semestre"));
			obj.setHorario(tabelaResultado.getString("horario"));
			obj.getTurmaVO().setCodigo(tabelaResultado.getInt("turma.codigo"));
			obj.getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
			obj.getTurmaVO().getTurno().setDuracaoAula(tabelaResultado.getInt("duracaoAula"));
			obj.getDisciplinaVO().setCodigo(tabelaResultado.getInt("disciplina.codigo"));
			obj.getDisciplinaVO().setNome(tabelaResultado.getString("disciplina.nome"));
			obj.getProfessorVO().setCodigo(tabelaResultado.getInt("pessoa.codigo"));
			obj.getProfessorVO().setNome(tabelaResultado.getString("pessoa.nome"));
			obj.setConteudo(tabelaResultado.getString("conteudo"));
			obj.setDiaSemana(obj.montarDiaSemanaAula());
			obj.setAbonado(Boolean.TRUE);
			obj.setPresente(Boolean.FALSE);
			listaRegistroAulaVOs.add(obj);
		}
		return listaRegistroAulaVOs;
		
	}
    
    public List<TransferenciaEntradaRegistroAulaFrequenciaVO> consultarPorTransferenciaEntrada(Integer transferenciaEntrada, UsuarioVO usuarioVO) throws Exception {
    	StringBuilder sb = new StringBuilder();
    	sb.append("select transferenciaRegistro.codigo, transferenciaRegistro.registroAula, transferenciaRegistro.transferenciaEntrada, turma.codigo AS  \"turma.codigo\", turma.identificadorTurma, disciplina.codigo AS \"disciplina.codigo\", disciplina.nome AS \"disciplina.nome\", ");
    	sb.append(" transferenciaRegistro.dataRegistroAula, transferenciaRegistro.ano, transferenciaRegistro.semestre, transferenciaRegistro.horario, ");
    	sb.append(" pessoa.codigo AS \"pessoa.codigo\", pessoa.nome AS \"pessoa.nome\",  transferenciaRegistro.presente, transferenciaRegistro.abonado ");
    	sb.append(" from TransferenciaEntradaRegistroAulaFrequencia transferenciaRegistro");
    	sb.append(" inner join turma on turma.codigo = transferenciaRegistro.turma ");
    	sb.append(" inner join disciplina on disciplina.codigo = transferenciaRegistro.disciplina ");
    	sb.append(" inner join pessoa on pessoa.codigo = transferenciaRegistro.professor ");
    	sb.append(" where transferenciaRegistro.transferenciaEntrada = ").append(transferenciaEntrada);
    	sb.append(" order by transferenciaRegistro.dataRegistroAula, disciplina.nome, horario ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
    	return montarDadosConsulta(tabelaResultado, usuarioVO);
    	
    }

    public static List montarDadosConsulta(SqlRowSet tabelaResultado, UsuarioVO usuario) throws Exception {
        List vetResultado = new ArrayList(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado, usuario));
        }
        return vetResultado;
    }

    public static TransferenciaEntradaRegistroAulaFrequenciaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
    	TransferenciaEntradaRegistroAulaFrequenciaVO obj = new TransferenciaEntradaRegistroAulaFrequenciaVO();
        obj.getTransferenciaEntradaVO().setCodigo(new Integer(dadosSQL.getInt("transferenciaEntrada")));
        obj.getRegistroAulaVO().setCodigo(dadosSQL.getInt("registroAula"));
        obj.getTurmaVO().setCodigo(dadosSQL.getInt("turma.codigo"));
        obj.getTurmaVO().setIdentificadorTurma(dadosSQL.getString("identificadorTurma"));
        obj.getDisciplinaVO().setCodigo(dadosSQL.getInt("disciplina.codigo"));
        obj.getDisciplinaVO().setNome(dadosSQL.getString("disciplina.nome"));
        obj.getProfessorVO().setCodigo(dadosSQL.getInt("pessoa.codigo"));
        obj.getProfessorVO().setNome(dadosSQL.getString("pessoa.nome"));
        obj.setDataRegistroAula(dadosSQL.getDate("dataRegistroAula"));
        obj.setAno(dadosSQL.getString("ano"));
        obj.setSemestre(dadosSQL.getString("semestre"));
        obj.setHorario(dadosSQL.getString("horario"));
        obj.setPresente(dadosSQL.getBoolean("presente"));
        obj.setAbonado(dadosSQL.getBoolean("abonado"));
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }
	
	public static String getIdEntidade() {
		return TransferenciaEntrada.idEntidade;
	}

	public void setIdEntidade(String idEntidade) {
		TransferenciaEntrada.idEntidade = idEntidade;
	}

}
