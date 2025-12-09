package negocio.facade.jdbc.secretaria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import negocio.comuns.academico.MatriculaPeriodoTurmaDisciplinaVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.academico.enumeradores.SituacaoMatriculaPeriodoEnum;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.secretaria.AtividadeComplementarMatriculaVO;
import negocio.comuns.secretaria.AtividadeComplementarVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.secretaria.AtividadeComplementarMatriculaInterfaceFacade;

/**
 *
 * @author Carlos
 */
@Repository
@Scope("singleton")
@Lazy
public class AtividadeComplementarMatricula extends ControleAcesso implements AtividadeComplementarMatriculaInterfaceFacade {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7827393426663196802L;
	protected static String idEntidade;

    public AtividadeComplementarMatricula() {
        super();
        setIdEntidade("AtividadeComplementarMatricula");
    }

    public void validarDadosConsulta(Integer turma, Integer disciplina) throws Exception {
        if (turma.equals(0)) {
            throw new Exception("O campo (TURMA) deve ser informado.");
        }
        if (disciplina.equals(0)) {
            throw new Exception("O campo (DISCIPLINA) deve ser informado.");
        }
    }

    public void validarDados(AtividadeComplementarMatriculaVO obj) throws Exception {
        if (obj.getMatriculaVO().getMatricula().equals("")) {
            throw new Exception("O campo MATRÍCULA deve ser informado!");
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluir(final AtividadeComplementarMatriculaVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj);
        final String sql = "INSERT INTO AtividadeComplementarMatricula( atividadeComplementar, matricula, alunoSelecionado, horaComplementar, matriculaperiodoturmadisciplina) VALUES ( ?, ?, ?, ?, ? )"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

        getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlInserir = arg0.prepareStatement(sql);
                if (obj.getAtividadeComplementarVO().getCodigo().intValue() != 0) {
                    sqlInserir.setInt(1, obj.getAtividadeComplementarVO().getCodigo().intValue());
                } else {
                    sqlInserir.setNull(1, 0);
                }
                sqlInserir.setString(2, obj.getMatriculaVO().getMatricula());
                sqlInserir.setBoolean(3, obj.getAlunoSelecionado());
                sqlInserir.setInt(4, obj.getHoraComplementar());
                sqlInserir.setInt(5, obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
                return sqlInserir;
            }
        });
        obj.setNovoObj(Boolean.FALSE);
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterar(final AtividadeComplementarMatriculaVO obj, UsuarioVO usuario) throws Exception {
        validarDados(obj);
        final String sql = "UPDATE AtividadeComplementarMatricula set atividadeComplementar=?, matricula=?, alunoSelecionado=?,  horaComplementar=?, matriculaperiodoturmadisciplina = ? where codigo = ?"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);

        if(getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {

            public PreparedStatement createPreparedStatement(Connection arg0) throws SQLException {
                PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                if (obj.getAtividadeComplementarVO().getCodigo().intValue() != 0) {
                    sqlAlterar.setInt(1, obj.getAtividadeComplementarVO().getCodigo().intValue());
                } else {
                    sqlAlterar.setNull(1, 0);
                }
                sqlAlterar.setString(2, obj.getMatriculaVO().getMatricula());
                sqlAlterar.setBoolean(3, obj.getAlunoSelecionado());
                sqlAlterar.setInt(4, obj.getHoraComplementar());
                sqlAlterar.setInt(5, obj.getMatriculaPeriodoTurmaDisciplinaVO().getCodigo());
                sqlAlterar.setInt(6, obj.getCodigo());
                return sqlAlterar;
            }
        })== 0) {
        	incluir(obj, usuario);
        	return;
        };
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluir(AtividadeComplementarVO obj, UsuarioVO usuario) throws Exception {
        AtividadeComplementar.excluir(getIdEntidade());
        String sql = "DELETE FROM AtividadeComplementar WHERE ((codigo = ?))"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{obj.getCodigo()});
    }

    public List<AtividadeComplementarMatriculaVO> consultarHoraComplementarAluno(AtividadeComplementarVO atividadeComplementarVO, TurmaVO turma, Integer disciplina, String ano, String semestre, boolean permitirRealizarLancamentoAlunosPreMatriculados, boolean permitirRealizarLancamentoAlunoPendenteFinanceiro, boolean trazerAlunoTranferencia, UsuarioVO usuarioVO) throws Exception {
        validarDadosConsulta(turma.getCodigo(), disciplina);        
        List<MatriculaPeriodoTurmaDisciplinaVO> listaMatriculaPeriodoTurmaDisciplina = getFacadeFactory().getMatriculaPeriodoTurmaDisciplinaFacade().
        		consultaRapidaPorCodigoTurmaDisciplinaSemestreAnoFiltroVisaoProfessor(null, disciplina, ano, semestre, "", false, false, 
        				permitirRealizarLancamentoAlunoPendenteFinanceiro, "", "", false, usuarioVO, turma, trazerAlunoTranferencia, 
        				permitirRealizarLancamentoAlunosPreMatriculados);  
        List<AtividadeComplementarMatriculaVO> vetResultado = new ArrayList<AtividadeComplementarMatriculaVO>(0);
        
        Predicate<MatriculaPeriodoTurmaDisciplinaVO> removeIf = mptdvo ->
        		mptdvo.getMatriculaPeriodoObjetoVO().getOrigemFechamentoMatriculaPeriodo().getBloquearRegistroAula() 
        		&& Uteis.isAtributoPreenchido(mptdvo.getMatriculaPeriodoObjetoVO().getDataFechamentoMatriculaPeriodo())
        		&& mptdvo.getMatriculaPeriodoObjetoVO().getDataFechamentoMatriculaPeriodo().before(atividadeComplementarVO.getDataAtividade());

        listaMatriculaPeriodoTurmaDisciplina.removeIf(removeIf);

        q:
        for(MatriculaPeriodoTurmaDisciplinaVO matriculaPeriodoTurmaDisciplinaVO: listaMatriculaPeriodoTurmaDisciplina) {
        	MatriculaPeriodoVO matriculaPeriodoVO = new MatriculaPeriodoVO();
        	matriculaPeriodoVO.setCodigo(matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodo());
        	matriculaPeriodoVO.getMatriculaVO().setMatricula(matriculaPeriodoTurmaDisciplinaVO.getMatricula());
        	getFacadeFactory().getMatriculaPeriodoFacade().carregarDados(matriculaPeriodoVO, null, usuarioVO);
        	matriculaPeriodoTurmaDisciplinaVO.setMatriculaPeriodoObjetoVO(matriculaPeriodoVO);
        	matriculaPeriodoTurmaDisciplinaVO.setMatriculaObjetoVO(matriculaPeriodoVO.getMatriculaVO());
        	for(AtividadeComplementarMatriculaVO atividadeComplementarMatriculaVO: atividadeComplementarVO.getListaAtividadeComplementarMatriculaVOs()) {
        		if(atividadeComplementarMatriculaVO.getMatriculaVO().getMatricula().equals(matriculaPeriodoTurmaDisciplinaVO.getMatricula())) {
        			atividadeComplementarMatriculaVO.setMatriculaVO(matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO());
        			atividadeComplementarMatriculaVO.setMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO);
        			vetResultado.add(atividadeComplementarMatriculaVO);
        			continue q;
        		}
        	}
        	 AtividadeComplementarMatriculaVO obj = new AtividadeComplementarMatriculaVO();
             obj.setMatriculaVO(matriculaPeriodoTurmaDisciplinaVO.getMatriculaObjetoVO());
             obj.setMatriculaPeriodoTurmaDisciplinaVO(matriculaPeriodoTurmaDisciplinaVO); 
             
             if((matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.ATIVA.getValor()) 
						|| matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FINALIZADA.getValor())
						|| matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.FORMADO.getValor())
						|| matriculaPeriodoTurmaDisciplinaVO.getMatriculaPeriodoObjetoVO().getSituacaoMatriculaPeriodo().equals(SituacaoMatriculaPeriodoEnum.PRE_MATRICULA.getValor()))
						 && atividadeComplementarVO.isNovoObj()){
            	 obj.setAlunoSelecionado(true);
             }else {
            	 obj.setAlunoSelecionado(false);
             }
             
             vetResultado.add(obj);
        }
        return vetResultado;
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void incluirAtividadeComplementarMatricula(Integer atividadeComplementar, List<AtividadeComplementarMatriculaVO>  atividadeComplementarMatriculaVOs, UsuarioVO usuario) throws Exception {
    	for(AtividadeComplementarMatriculaVO atividadeComplementarMatriculaVO: atividadeComplementarMatriculaVOs) {
    		atividadeComplementarMatriculaVO.getAtividadeComplementarVO().setCodigo(atividadeComplementar);
            incluir(atividadeComplementarMatriculaVO, usuario);
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void alterarAtividadeComplementarMatricula(Integer atividadeComplementar, List<AtividadeComplementarMatriculaVO> atividadeComplementarMatriculaVOs, UsuarioVO usuario) throws Exception {
        excluirAtividadeComplementarMatriculaVOs(atividadeComplementar, atividadeComplementarMatriculaVOs, usuario);
        for(AtividadeComplementarMatriculaVO atividadeComplementarMatriculaVO: atividadeComplementarMatriculaVOs) {
        	atividadeComplementarMatriculaVO.getAtividadeComplementarVO().setCodigo(atividadeComplementar);
        	if(Uteis.isAtributoPreenchido(atividadeComplementarMatriculaVO.getCodigo())) {
        		alterar(atividadeComplementarMatriculaVO, usuario);
        	}else {
        		incluir(atividadeComplementarMatriculaVO, usuario);
        	}
        }
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirAtividadeComplementarMatriculaVOs(Integer atividadeComplementar,  List<AtividadeComplementarMatriculaVO> atividadeComplementarMatriculaVOs, UsuarioVO usuario) throws Exception {
        String sql = "DELETE FROM AtividadeComplementarMatricula WHERE (atividadeComplementar= ?) and codigo not in (0 ";
        for(AtividadeComplementarMatriculaVO atividadeComplementarMatriculaVO: atividadeComplementarMatriculaVOs) {
        	if(Uteis.isAtributoPreenchido(atividadeComplementarMatriculaVO.getCodigo())) {
        		sql += ", "+atividadeComplementarMatriculaVO.getCodigo();	
        	}
        }
        sql += ") "+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{atividadeComplementar});
    }

    @Transactional(readOnly = false, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void excluirComBaseNaMatricula(String matricula, UsuarioVO usuario) throws Exception {
        String sql = "DELETE FROM AtividadeComplementarMatricula WHERE matricula = ?"+adicionarUsuarioLogadoComoComentarioParaLogTrigger(usuario);
        getConexao().getJdbcTemplate().update(sql, new Object[]{matricula});
    }
	
    public List<AtividadeComplementarMatriculaVO> consultarAtividadeComplementarMatriculaPorAtividadeComplementar(Integer atividadeComplementar, boolean controlarAcesso, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
        List<AtividadeComplementarMatriculaVO> objetos = new ArrayList<AtividadeComplementarMatriculaVO>(0);
        StringBuilder sb = new StringBuilder();
        sb.append("select distinct pessoa.nome, matricula.situacao as \"matricula.situacao\", AtividadeComplementarMatricula.* from AtividadeComplementarMatricula ");
        sb.append(" inner join matricula on matricula.matricula = AtividadeComplementarMatricula.matricula ");
        sb.append(" inner join pessoa on pessoa.codigo = matricula.aluno ");
        sb.append(" where AtividadeComplementarMatricula.atividadeComplementar = ").append(atividadeComplementar);
        sb.append(" ORDER BY pessoa.nome");
        SqlRowSet resultado = getConexao().getJdbcTemplate().queryForRowSet(sb.toString());
        while (resultado.next()) {
            objetos.add(montarDados(resultado, usuario));
        }
        return objetos;
    }

    public static AtividadeComplementarMatriculaVO montarDados(SqlRowSet dadosSQL, UsuarioVO usuario) throws Exception {
        AtividadeComplementarMatriculaVO obj = new AtividadeComplementarMatriculaVO();
        obj.setCodigo(dadosSQL.getInt("codigo"));
        obj.getAtividadeComplementarVO().setCodigo(dadosSQL.getInt("atividadeComplementar"));
        obj.getMatriculaVO().setMatricula(dadosSQL.getString("matricula"));
        obj.getMatriculaVO().setSituacao(dadosSQL.getString("matricula.situacao"));
        obj.getMatriculaVO().getAluno().setNome(dadosSQL.getString("nome"));
        obj.setAlunoSelecionado(dadosSQL.getBoolean("alunoSelecionado"));
        obj.setHoraComplementar(dadosSQL.getInt("horaComplementar"));
        obj.getMatriculaPeriodoTurmaDisciplinaVO().setCodigo(dadosSQL.getInt("matriculaperiodoturmadisciplina"));        
        obj.setNovoObj(Boolean.FALSE);
        return obj;
    }

    public AtividadeComplementarMatriculaVO consultarPorChavePrimaria(Integer codigo, UsuarioVO usuario) throws Exception {
        ControleAcesso.consultar(getIdEntidade(), false, usuario);
        String sql = "SELECT * FROM AtividadeComplementarMatricula WHERE codigo = ?";
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql, new Object[]{codigo});
        if (!tabelaResultado.next()) {
            throw new ConsistirException("Dados Não Encontrados.");
        }
        return (montarDados(tabelaResultado, usuario));
    }

    /**
     * Operação reponsável por retornar o identificador desta classe. Este
     * identificar é utilizado para verificar as permissões de acesso as
     * operações desta classe.
     */
    public static String getIdEntidade() {
        return AtividadeComplementarMatricula.idEntidade;
    }

    /**
     * Operação reponsável por definir um novo valor para o identificador desta
     * classe. Esta alteração deve ser possível, pois, uma mesma classe de
     * negócio pode ser utilizada com objetivos distintos. Assim ao se verificar
     * que Como o controle de acesso é realizado com base neste identificador,
     */
    public void setIdEntidade(String idEntidade) {
        AtividadeComplementarMatricula.idEntidade = idEntidade;
    }
}
