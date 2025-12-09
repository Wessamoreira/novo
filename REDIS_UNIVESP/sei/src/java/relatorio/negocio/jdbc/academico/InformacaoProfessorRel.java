package relatorio.negocio.jdbc.academico;

import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Ordenacao;
import negocio.facade.jdbc.arquitetura.ControleAcesso;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.InformacaoProfessorRelVO;
import relatorio.negocio.interfaces.academico.InformacaoProfessorRelInterfaceFacade;

@Repository
@Scope("singleton")
@Lazy
public class InformacaoProfessorRel extends ControleAcesso implements InformacaoProfessorRelInterfaceFacade {
	
	public List<InformacaoProfessorRelVO> criarObjeto(Integer unidadeEnsino, Integer curso, Integer pessoa, Integer disciplina) {
		List<InformacaoProfessorRelVO> informacaoProfessorRelVOs = new ArrayList<InformacaoProfessorRelVO>(0);
		informacaoProfessorRelVOs.addAll(executarConsultaParametrizada(unidadeEnsino, curso, pessoa, disciplina));
		Ordenacao.ordenarLista(informacaoProfessorRelVOs, "nome");
		Ordenacao.ordenarLista(informacaoProfessorRelVOs, "disciplina");
		return informacaoProfessorRelVOs;
	}

	private List<InformacaoProfessorRelVO> executarConsultaParametrizada(Integer unidadeEnsino, Integer curso, Integer pessoa, Integer disciplina) {
		StringBuilder sql = new StringBuilder();
		List<InformacaoProfessorRelVO> listaInteracaoFollowUpRelVO = new ArrayList<InformacaoProfessorRelVO>(0);
		sql.append("select distinct pessoa.codigo AS codigoProfessor, pessoa.nome AS nomeprofessor, pessoa.email AS emailprofessor, pessoa.celular AS celularProfessor, disciplina.nome AS nomeDisciplina, turma.identificadorturma AS identificadorTurma from pessoa "); 
		sql.append("inner join horarioturmaprofessordisciplina on horarioturmaprofessordisciplina.professor = pessoa.codigo ");
		sql.append("inner join disciplina on horarioturmaprofessordisciplina.disciplina = disciplina.codigo ");
		sql.append("inner join funcionario on funcionario.pessoa = pessoa.codigo ");
		sql.append("inner join funcionariocargo on funcionariocargo.funcionario = funcionario.codigo ");
		sql.append("inner join turma on horarioturmaprofessordisciplina.turma = turma.codigo ");
		sql.append("inner join curso on curso.codigo = turma.curso ");
		sql.append("funcionariocargo.unidadeEnsino = ").append(unidadeEnsino).append(" ");
		if (curso != 0) {
			sql.append("curso.codigo = ").append(curso).append(" ");
		}
		if (pessoa != 0) {	
			sql.append("pessoa.codigo = ").append(pessoa).append(" ");
		}
		if (disciplina != 0) {
			sql.append("disciplina.codigo = ").append(disciplina).append(" ");
		}
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sql.toString());
		while (tabelaResultado.next()) {
			listaInteracaoFollowUpRelVO.add(montarDados(tabelaResultado));
		}
        return listaInteracaoFollowUpRelVO;
	}
	
	private InformacaoProfessorRelVO montarDados(SqlRowSet dadosSQL) {
		InformacaoProfessorRelVO obj = new InformacaoProfessorRelVO();
		obj.setCodigoProfessor(dadosSQL.getInt("codigoProfessor"));
		obj.setNome(dadosSQL.getString("nomeProfessor"));
		obj.setCelular(dadosSQL.getString("celularProfessor"));
        obj.setEmail(dadosSQL.getString("emailProfessor"));
        obj.setDisciplinaMinistrada(dadosSQL.getString("nomeDisciplina"));
        obj.setTurmaDisciplinaMinistrada(dadosSQL.getString("identificadorTurma"));
		return obj;
	}
	
    public static void montarDadosFormacaoAcademica(InformacaoProfessorRelVO obj, UsuarioVO usuario) throws Exception {
        if (obj.getCodigoProfessor() == 0) {
            obj.setFormacaoAcademicaVOs(new ArrayList(0));
            return;
        }
        obj.setFormacaoAcademicaVOs(getFacadeFactory().getFormacaoAcademicaFacade().consultarPorCodigo(obj.getCodigoProfessor(), false, usuario));
    }

}
