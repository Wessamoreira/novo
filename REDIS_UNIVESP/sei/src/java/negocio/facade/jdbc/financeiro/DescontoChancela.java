package negocio.facade.jdbc.financeiro;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.facade.jdbc.arquitetura.SuperFacadeJDBC;
import negocio.interfaces.financeiro.DescontoChancelaInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class DescontoChancela extends SuperFacadeJDBC implements DescontoChancelaInterfaceFacade {

	
	/*
	 * (non-Javadoc)
	 * @see negocio.interfaces.financeiro.DescontoChancelaInterfaceFacade#executarGravarListaMatricula(java.util.Set)
	 */
	public void executarGravarListaMatricula(Set<MatriculaVO>  listaMatricula,UsuarioVO usuario) throws Exception{
		for (MatriculaVO matriculaVO : listaMatricula) {
			alterar(matriculaVO,usuario);
		}
		
	}
	
	/**
     * Operação responsável por alterar no BD os dados de um objeto da classe <code>MatriculaVO</code>. Sempre utiliza a chave primária da classe como atributo para localização do registro a ser
     * alterado. Primeiramente valida os dados (<code>validarDados</code>) do objeto. Verifica a conexão com o banco de dados e a permissão do usuário para realizar esta operacão na entidade. Isto,
     * através da operação <code>alterar</code> da superclasse.
     *
     * @param obj
     *            Objeto da classe <code>MatriculaVO</code> que será alterada no banco de dados.
     * @exception Execption
     *                Caso haja problemas de conexão, restrição de acesso ou validação de dados.
     */
    public void alterar(final MatriculaVO obj,UsuarioVO usuario) throws Exception {
        try {
          final String sql = "UPDATE Matricula set descontoChancela=? WHERE matricula = ?";
            getConexao().getJdbcTemplate().update(new PreparedStatementCreator() {
                public PreparedStatement createPreparedStatement(Connection arg0 ) throws SQLException {
                    PreparedStatement sqlAlterar = arg0.prepareStatement(sql);
                    sqlAlterar.setBoolean(1, obj.getDescontoChancela());
                    sqlAlterar.setString(2, obj.getMatricula());
                    return sqlAlterar;
                }
            });       

        } catch (Exception e) {
            throw e;
        }
    }
	
	
	public List<MatriculaVO> executarConsultaEscolhaDescontoChancela(Integer turmaCodigo,UsuarioVO usuario) throws Exception {
		List<MatriculaVO> lista = new ArrayList<MatriculaVO>(0);
		String selectStr = "SELECT "
				+ " matricula.matricula as matricula_aluno, curso.nome as nomecurso, pessoa.nome as nome_aluno, turno.nome  as turno_nome , matricula.descontochancela as desconto_chancela "
				+ " FROM negociacaorecebimento "
				+ " LEFT JOIN contarecebernegociacaorecebimento ON (negociacaorecebimento.codigo = contarecebernegociacaorecebimento.negociacaorecebimento) "
				+ " LEFT JOIN contareceber ON (contarecebernegociacaorecebimento.contareceber = contareceber.codigo) LEFT JOIN pessoa ON (negociacaorecebimento.pessoa = pessoa.codigo) "
				+ " LEFT JOIN contareceberrecebimento ON (contareceberrecebimento.contareceber = contareceber.codigo) "
				+ " LEFT JOIN formapagamentonegociacaorecebimento ON (formapagamentonegociacaorecebimento.negociacaorecebimento = negociacaorecebimento.codigo) "
				+ " LEFT JOIN formapagamento ON (formapagamento.codigo = formapagamentonegociacaorecebimento.formapagamento) "
				+ " LEFT JOIN parceiro ON (negociacaorecebimento.parceiro = parceiro.codigo) LEFT JOIN contacorrente ON (negociacaorecebimento.contacorrentecaixa = contacorrente.codigo) "
				+ " LEFT JOIN matriculaperiodo ON  contareceber.codorigem <> '' and (cast (contareceber.codorigem as integer) = matriculaperiodo.codigo) "
				+ " LEFT JOIN matriculaperiodoturmadisciplina ON (matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo) "
				+ " LEFT JOIN turma ON (matriculaperiodoturmadisciplina.turma = turma.codigo) "
				+ " LEFT JOIN matricula ON matriculaperiodo.matricula = matricula.matricula LEFT JOIN turno ON matricula.turno = turno.codigo "
				+ " LEFT JOIN curso ON matricula.curso = curso.codigo ";
		selectStr = montarFiltrosRelatorio(selectStr, turmaCodigo);
		selectStr += "GROUP BY matricula_aluno, nome_aluno, nome_aluno, nomecurso, turno_nome , desconto_chancela";
		//selectStr = montarOrdenacaoRelatorio(selectStr, 1, turma.getCodigo());
		SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(selectStr);
		while(tabelaResultado.next()){
			MatriculaVO matriculaVO = montarDados(tabelaResultado);
			lista.add(matriculaVO);
		}
		return lista;
	}
	
	  private MatriculaVO montarDados(SqlRowSet dadosSQL) {
		  	MatriculaVO matriculaVO = new MatriculaVO();
		  	matriculaVO.setMatricula(dadosSQL.getString("matricula_aluno"));
		  	matriculaVO.getAluno().setNome(dadosSQL.getString("nome_aluno"));
		  	matriculaVO.getCurso().setNome(dadosSQL.getString("nomecurso"));
		  	matriculaVO.getTurno().setNome(dadosSQL.getString("turno_nome"));
		  	matriculaVO.setDescontoChancela(dadosSQL.getBoolean("desconto_chancela"));
	        return matriculaVO;
	  }
	
	
	/*
	 * 
	 */
	private String montarFiltrosRelatorio(String selectStr, Integer turmaCodigo) {
        String where = " where ";
        selectStr += where + "((contareceber.tipoorigem = 'BCC') or (contareceber.tipoorigem = 'MAT') or (contareceber.tipoorigem = 'MEN'))";
        where = " and ";
        if (turmaCodigo != null && turmaCodigo != 0) {
            selectStr += where + "( turma.codigo = " + turmaCodigo  + ")";
            
        }
        return selectStr;
    }
	
	private String montarOrdenacaoRelatorio(String selectStr, Integer ordenacao, String descricaoFiltros) {
        String ordenacaoNova = ordenacao.toString();

        if (ordenacaoNova.equals("0")) {
            ordenacaoNova = "negociacaorecebimento.data";
        }
        if (ordenacaoNova.equals("1")) {
            ordenacaoNova = "turma.identificadorturma";
        }
        if (!ordenacaoNova.equals("")) {
            selectStr = selectStr + " ORDER BY " + ordenacaoNova;
        }
        return selectStr;
    }
}
