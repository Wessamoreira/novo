package negocio.facade.jdbc.academico;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.MapaDeTurmasEncerradasRelVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.arquitetura.ControleAcesso;
import negocio.interfaces.academico.MapaDeTurmasEncerradasRelInterfaceFacade;

/**
 * Classe de persistência que encapsula todas as operações de manipulação dos dados da classe <code>MapaDeTurmasEncerradasRelVO</code>. Responsável por implementar operações como incluir, alterar,
 * excluir e consultar pertinentes a classe <code>MapaDeTurmasEncerradasRelVO</code>. Encapsula toda a interação com o banco de dados.
 * 
 * @see MapaDeTurmasEncerradasRelVO
 * @see ControleAcesso
 */
@Repository
@Scope("singleton")
@Lazy
public class MapaDeTurmasEncerradasRel extends ControleAcesso implements MapaDeTurmasEncerradasRelInterfaceFacade {

	private static final long serialVersionUID = 1L;
	protected static String idEntidade;

  
    public MapaDeTurmasEncerradasRel() throws Exception {
        super();
        setIdEntidade("MapaDeTurmasEncerradasRel");
    }

    /**
     * Operação responsável por retornar um novo objeto da classe <code>TurmaVO</code>.
     */
    public MapaDeTurmasEncerradasRelVO novo() throws Exception {
    	MapaDeTurmasEncerradasRel.incluir(getIdEntidade());
        MapaDeTurmasEncerradasRelVO obj = new MapaDeTurmasEncerradasRelVO();
        return obj;
    }
    @Override
    public List<MapaDeTurmasEncerradasRelVO> consultar(UnidadeEnsinoVO unidadeEnsinoVO, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception{
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuffer sql = new StringBuffer();
    	sql.append("select turma.identificadorTurma,turma.codigo,unidadeensino.nome as nome_unidadeensino, ");
    	sql.append("(select count(m.matricula) from matriculaperiodo as mp inner join matricula m on m.matricula = mp.matricula  where m.situacao in ('FO','AT') and mp.turma = turma.codigo) as quantidadeDeAlunos , ");
    	sql.append("count(distinct matricula.matricula) as quantidadeDeAlunosSeremFormados,");
    	sql.append("(select count(m.matricula) from matriculaperiodo as mp inner join matricula m on m.matricula = mp.matricula  where m.situacao = 'FO' and mp.turma = turma.codigo) as quantidadeDeAlunosFormados, ");
    	sql.append("(select max(data) from horarioturmadia where horarioturmadia.horarioturma = horarioturma.codigo ) as dataUltimaAula ");
    	sql.append("from turma ");
    	sql.append("inner join curso on curso.codigo = turma.curso and curso.niveleducacional = 'PO' ");
    	sql.append("inner join horarioturma on horarioturma.turma = turma.codigo ");
    	sql.append("inner join matriculaperiodo on matriculaperiodo.turma = turma.codigo ");
    	sql.append("inner join matricula on matriculaperiodo.matricula = matricula.matricula and matricula.situacao = 'AT' ");
    	sql.append("inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
    	sql.append("where current_date > (select max(data) from horarioturmadia where horarioturmadia.horarioturma = horarioturma.codigo ) ");
    	if (!unidadeEnsinoVO.getCodigo().equals(0)) {
    		sql.append("and matricula.unidadeensino = ").append(unidadeEnsinoVO.getCodigo()).append(" ");
		}
    	sql.append("group by identificadorTurma, turma.codigo, horarioturma.codigo ,unidadeensino.nome ");
    	sql.append("order by identificadorTurma ");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	
    	return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados);
    }
    @Override
    public List<MapaDeTurmasEncerradasRelVO> consultarPorSituacaoMatriculaCodigoDaTurma(UnidadeEnsinoVO unidadeEnsinoVO, MapaDeTurmasEncerradasRelVO mapaDeTurmasEncerradasRelVO, boolean controlarAcesso, int nivelMontarDados, UsuarioVO usuario) throws Exception{
    	ControleAcesso.consultar(getIdEntidade(), controlarAcesso, usuario);
    	StringBuffer sql = new StringBuffer();
    	sql.append("select matricula.matricula , pessoa.nome as pessoa_nome , curso.nome as curso_nome, curso.abreviatura, turno.nome as turno_nome, unidadeensino.nome as nome_unidadeensino FROM turma ");
    	sql.append("inner join curso on curso.codigo = turma.curso and curso.niveleducacional = 'PO' ");
    	sql.append("inner join horarioturma on horarioturma.turma = turma.codigo ");
    	sql.append("inner join matriculaperiodo as mp on mp.turma = turma.codigo ");
    	sql.append("inner join matricula on mp.matricula = matricula.matricula and matricula.situacao in ('").append(mapaDeTurmasEncerradasRelVO.getSituacao()).append("')");
    	sql.append("inner join pessoa on pessoa.codigo = matricula.aluno ");
    	sql.append("inner join turno on turno.codigo=matricula.turno ");
    	sql.append("inner join unidadeensino on unidadeensino.codigo = matricula.unidadeensino ");
    	sql.append("where current_date > (select max(data) from horarioturmadia where horarioturmadia.horarioturma = horarioturma.codigo ) and turma.codigo=").append(mapaDeTurmasEncerradasRelVO.getTurmaVO().getCodigo());
    	sql.append(" order by identificadorTurma");
    	SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
    	return montarDadosConsultaBasica(tabelaResultado, nivelMontarDados);
    }
    
    public List<MapaDeTurmasEncerradasRelVO> montarDadosConsultaBasica(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception {
        List<MapaDeTurmasEncerradasRelVO> listaMapaDeTurmasEncerradasRelVO = new ArrayList<MapaDeTurmasEncerradasRelVO>(0);
        while (tabelaResultado.next()) {
        	MapaDeTurmasEncerradasRelVO obj = new MapaDeTurmasEncerradasRelVO();
            obj = montarDados(tabelaResultado,nivelMontarDados);
            listaMapaDeTurmasEncerradasRelVO.add(obj);
        }
        return listaMapaDeTurmasEncerradasRelVO;
    }
    
    
    public static MapaDeTurmasEncerradasRelVO montarDados(SqlRowSet tabelaResultado, int nivelMontarDados) throws Exception{
    	MapaDeTurmasEncerradasRelVO obj = new MapaDeTurmasEncerradasRelVO();
    	if (nivelMontarDados==Uteis.NIVELMONTARDADOS_DADOSCONSULTA) {
    		obj.getTurmaVO().setIdentificadorTurma(tabelaResultado.getString("identificadorTurma"));
        	obj.getTurmaVO().setCodigo(tabelaResultado.getInt("codigo"));
        	obj.setQuantidadeDeAlunos(tabelaResultado.getInt("quantidadeDeAlunos"));
        	obj.setQuantidadeDeAlunosFormados(tabelaResultado.getInt("quantidadeDeAlunosFormados"));
        	obj.setQuantidadeDeAlunosSeremFormados(tabelaResultado.getInt("quantidadeDeAlunosSeremFormados"));
        	obj.setDataUltimaAula(tabelaResultado.getDate("dataUltimaAula"));
        	obj.getMatriculaVO().getUnidadeEnsino().setNome(tabelaResultado.getString("nome_unidadeensino"));
		}
    	if (nivelMontarDados==Uteis.NIVELMONTARDADOS_DADOSCONSULTARTODOS) {
			obj.getMatriculaVO().setMatricula(tabelaResultado.getString("matricula"));
			obj.getMatriculaVO().getAluno().setNome(tabelaResultado.getString("pessoa_nome"));
			obj.getMatriculaVO().getCurso().setNome(tabelaResultado.getString("curso_nome"));
			obj.getMatriculaVO().getCurso().setAbreviatura(tabelaResultado.getString("abreviatura"));
			obj.getMatriculaVO().getTurno().setNome(tabelaResultado.getString("turno_nome"));
			obj.getMatriculaVO().getUnidadeEnsino().setNome(tabelaResultado.getString("nome_unidadeensino"));
		}
    	return obj;
    }
    
	public static String getIdEntidade() {
		return idEntidade;
	}

	public static void setIdEntidade(String idEntidade) {
		MapaDeTurmasEncerradasRel.idEntidade = idEntidade;
	}

   
}
