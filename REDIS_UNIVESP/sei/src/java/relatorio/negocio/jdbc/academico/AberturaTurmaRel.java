package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.faces.model.SelectItem;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import relatorio.negocio.comuns.academico.AberturaTurmaRelVO;
import relatorio.negocio.interfaces.academico.AberturaTurmaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

/**
 *
 * @author Carlos
 */
@Repository
@Scope("singleton")
@Lazy
public class AberturaTurmaRel extends SuperRelatorio implements AberturaTurmaRelInterfaceFacade {

    public AberturaTurmaRel() {
    }

    public void validarDados(Date dataInicio, Date dataFim, Boolean telaMapaAbertura) throws Exception {
        if (dataInicio == null) {
            throw new Exception("A data início deve ser informada.");
        }
        if (!telaMapaAbertura) {
            if (dataFim == null) {
                throw new Exception("A data fim deve ser informada.");
            }
        }
    }

    public List<AberturaTurmaRelVO> realizarCriacaoOjbRel(Integer turma, List<UnidadeEnsinoVO> unidadeEnsinoVOs, Integer curso, String situacao, Date dataInicio, Date dataFim) throws Exception {
        StringBuilder sqlStr = new StringBuilder();
        sqlStr.append("select turmaabertura.data AS \"data\", curso.nome AS \"curso\", turma.identificadorturma AS \"turma\", unidadeensino.nome AS \"unidadeEnsino\", turmaabertura.situacao AS \"situacao\", turmaabertura.turma, ");
        sqlStr.append("(select count(matriculaperiodo.matricula) from matriculaperiodo where matriculaperiodo.turma = turmaabertura.turma  and matriculaperiodo.situacaoMatriculaperiodo = 'AT' ");
        sqlStr.append("AND (matriculaperiodo.situacao = 'PF' OR matriculaperiodo.situacao = 'AT' OR matriculaperiodo.situacao = 'CO') ) AS \"numeroMatriculado\" from turma ");
        sqlStr.append("inner join turmaabertura on turmaabertura.turma = turma.codigo ");
        sqlStr.append("inner join curso on curso.codigo = turma.curso ");
        sqlStr.append("inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino ");
        sqlStr.append("left join matriculaperiodo on matriculaperiodo.turma = turma.codigo ");
        sqlStr.append("WHERE turmaabertura.data >= '").append(Uteis.getDataJDBC(dataInicio)).append("' ");
        if (dataFim != null) {
            sqlStr.append("AND turmaabertura.data <= '").append(Uteis.getDataJDBC(dataFim)).append("' ");
        }
        
        sqlStr.append(adicionarFiltroUnidadeEnsino(unidadeEnsinoVOs));

        if (turma != null && turma != 0) {
            sqlStr.append("AND turma.codigo = ").append(turma).append(" ");
        }
        if (curso != null && curso != 0) {
            sqlStr.append("AND curso.codigo = ").append(curso).append(" ");
        }
        if (!situacao.equals("")) {
            sqlStr.append("AND turmaabertura.situacao = '").append(situacao).append("' ");
        }
        sqlStr.append("GROUP BY turma.identificadorturma, turmaabertura.data, curso.nome, unidadeensino.nome, turmaabertura.situacao, turmaabertura.turma ");
        sqlStr.append("ORDER BY turma.identificadorTurma");
        SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString());
        return (montarDadosConsulta(tabelaResultado));
    }

    public static List<AberturaTurmaRelVO> montarDadosConsulta(SqlRowSet tabelaResultado) throws Exception {
        List<AberturaTurmaRelVO> vetResultado = new ArrayList<AberturaTurmaRelVO>(0);
        while (tabelaResultado.next()) {
            vetResultado.add(montarDados(tabelaResultado));
        }
        return vetResultado;
    }

    public static AberturaTurmaRelVO montarDados(SqlRowSet dadosSql) throws Exception {
        AberturaTurmaRelVO obj = new AberturaTurmaRelVO();
        obj.setInauguracao(dadosSql.getDate("data"));
        obj.setCurso(dadosSql.getString("curso"));
        obj.setTurma(dadosSql.getString("turma"));
        obj.setSituacao(dadosSql.getString("situacao"));
        obj.setUnidadeEnsino(dadosSql.getString("unidadeEnsino"));
        obj.setNumeroMatriculado(dadosSql.getInt("numeroMatriculado"));
        return obj;
    }

    public List montarListaSelectItemUnidadeEnsino(UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado) throws Exception {
        List resultadoConsulta = null;
        Iterator i = null;

        resultadoConsulta = consultarUnidadeEnsinoPorNome("", unidadeEnsinoLogado, usuarioLogado);
        i = resultadoConsulta.iterator();
        List objs = new ArrayList(0);
        if (unidadeEnsinoLogado.getCodigo().equals(0)) {
            objs.add(new SelectItem(0, ""));
        }
        while (i.hasNext()) {
            UnidadeEnsinoVO obj = (UnidadeEnsinoVO) i.next();
            objs.add(new SelectItem(obj.getCodigo(), obj.getNome()));
        }
        return objs;
    }

    public List consultarUnidadeEnsinoPorNome(String nomePrm, UnidadeEnsinoVO unidadeEnsinoLogado, UsuarioVO usuarioLogado) throws Exception {
        List lista = getFacadeFactory().getUnidadeEnsinoFacade().consultarPorNome(nomePrm, unidadeEnsinoLogado.getCodigo(), false,  Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado);
        return lista;
    }

    public List consultarTurma(String campoConsultaTurma, UnidadeEnsinoVO unidadeEnsinoVO, String valorConsultaTurma, UsuarioVO usuarioLogado) throws Exception {

        if (campoConsultaTurma.equals("identificadorTurma")) {
            return getFacadeFactory().getTurmaFacade().consultaRapidaPorIdentificadorTurma(valorConsultaTurma, unidadeEnsinoVO.getCodigo(), false, false, "", false,
                    Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);
        }
        if (campoConsultaTurma.equals("nomeTurno")) {
            return getFacadeFactory().getTurmaFacade().consultaRapidaPorTurno(valorConsultaTurma, unidadeEnsinoVO.getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);
        }
        if (campoConsultaTurma.equals("nomeCurso")) {
            return getFacadeFactory().getTurmaFacade().consultaRapidaNomeCurso(valorConsultaTurma, unidadeEnsinoVO.getCodigo(), false, false, false, Uteis.NIVELMONTARDADOS_DADOSMINIMOS, usuarioLogado);
        }
        return new ArrayList(0);
    }

    public List consultarCurso(String campoConsultaCurso, UnidadeEnsinoVO unidadeEnsinoVO, String valorConsultaCurso, UsuarioVO usuarioLogado) throws Exception {

        if (campoConsultaCurso.equals("nome")) {
            return getFacadeFactory().getCursoFacade().consultaRapidaPorNomeEUnidadeDeEnsino(valorConsultaCurso,
                    unidadeEnsinoVO.getCodigo(), false, Uteis.NIVELMONTARDADOS_COMBOBOX, usuarioLogado);
        }
        return new ArrayList(0);
    }

    public String validarDadosSituacaoApresentar(String situacao) {
        if (situacao.equals("AC")) {
            return "A CONFIRMAR";
        }
        if (situacao.equals("AD")) {
            return "ADIADA";
        }
        if (situacao.equals("CO")) {
            return "CONFIRMADA";
        }
        if (situacao.equals("IN")) {
            return "INAUGURADA";
        }
        if (situacao.equals("CA")) {
            return "CANCELADA";
        }
        return "Todas as Situações";
    }

    public static String getDesignIReportRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
    }

    public static String getDesignIReportRelatorioExcel() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidadeExcel() + ".jrxml");
    }

    public static String getCaminhoBaseRelatorio() {
        return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
    }

    public static String getIdEntidade() {
        return ("AberturaTurmaRel");
    }

    public static String getIdEntidadeExcel() {
        return ("AberturaTurmaRelExcel");
    }
    
	private String adicionarFiltroUnidadeEnsino(List<UnidadeEnsinoVO> unidadeEnsinoVOs) {
		StringBuilder sql = new StringBuilder("");
		if(Uteis.isAtributoPreenchido(unidadeEnsinoVOs )){
		sql.append(" AND unidadeEnsino in (0");
		for (UnidadeEnsinoVO unidadeEnsinoVO : unidadeEnsinoVOs) {
			if (unidadeEnsinoVO.getFiltrarUnidadeEnsino()) {
				sql.append(", ").append(unidadeEnsinoVO.getCodigo());
			}
		}
		sql.append(") ");
		}
		return sql.toString();
	}
}
