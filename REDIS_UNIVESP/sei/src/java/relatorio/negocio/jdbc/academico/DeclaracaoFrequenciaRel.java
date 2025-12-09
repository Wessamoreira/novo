package relatorio.negocio.jdbc.academico;

import java.io.File;
import java.util.Date;
import java.util.Optional;

import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.facade.jdbc.academico.MatriculaPeriodoTurmaDisciplina;

import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import relatorio.negocio.comuns.academico.DeclaracaoFrequenciaVO;
import relatorio.negocio.interfaces.academico.DeclaracaoFrequenciaRelInterfaceFacade;
import relatorio.negocio.jdbc.arquitetura.SuperRelatorio;

@SuppressWarnings("unchecked")
@Repository
@Scope("singleton")
@Lazy
public class DeclaracaoFrequenciaRel extends SuperRelatorio implements DeclaracaoFrequenciaRelInterfaceFacade {


	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.DeclaracaoFrequenciaRelInterfaceFacade#consultarPorCodigoAluno(negocio.comuns .academico.MatriculaVO, java.lang.Integer)
	 */
	public DeclaracaoFrequenciaVO consultarPorCodigoAluno(DeclaracaoFrequenciaVO obj, String ano, String semestre, Date dataDeclaracao, int disciplina, int nivelMontarDados, UsuarioVO usuario) throws Exception {
		MatriculaPeriodoVO matPeriodo = new MatriculaPeriodoVO();
		matPeriodo = getFacadeFactory().getMatriculaPeriodoFacade().consultaRapidaPorMatriculaSemestreAno(obj.getMatricula(), semestre, ano, true, false, Optional.ofNullable(null), Optional.ofNullable(null), null);
		matPeriodo.setMatriculaVO(getFacadeFactory().getMatriculaFacade().consultaRapidaPorMatriculaUnica(obj.getMatricula(), 0, false, null));
                matPeriodo.getMatriculaVO().getUnidadeEnsino().setCidade(getFacadeFactory().getCidadeFacade().consultarPorChavePrimaria(matPeriodo.getMatriculaVO().getUnidadeEnsino().getCidade().getCodigo(), false, usuario));
		return montarDados(matPeriodo, dataDeclaracao, obj, disciplina);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see relatorio.negocio.jdbc.academico.DeclaracaoFrequenciaRelInterfaceFacade#montarDados(negocio.comuns.basico.PessoaVO , negocio.comuns.academico.MatriculaPeriodoVO,
	 * negocio.comuns.academico.MatriculaVO)
	 */
	public DeclaracaoFrequenciaVO montarDados(MatriculaPeriodoVO matPeriodo, Date dataDeclaracao, DeclaracaoFrequenciaVO obj, int disciplina) throws Exception {
		obj.setMatricula(matPeriodo.getMatricula());
		obj.setNome(matPeriodo.getMatriculaVO().getAluno().getNome());
                if (obj.getTipoDeclaracao().intValue() == 0){
                    if (matPeriodo.getMatriculaVO().getCurso().getNivelEducacionalPosGraduacao()) {
                            obj.setPeriodoLetivo("PERÍODO INTEGRAL");
                    } else {
                            obj.setPeriodoLetivo(matPeriodo.getPeridoLetivo().getDescricao());
                    }		
                }
                if (disciplina != 0 || obj.getTipoDeclaracao().intValue() == 0){
                    obj.setCurso(matPeriodo.getMatriculaVO().getCurso().getNome());
                }
                obj.setCpf(matPeriodo.getMatriculaVO().getAluno().getCPF());
                obj.setRg(matPeriodo.getMatriculaVO().getAluno().getRG());
                obj.setData(Uteis.getDataCidadeDiaMesPorExtensoEAno(matPeriodo.getMatriculaVO().getUnidadeEnsino().getCidade().getNome(), dataDeclaracao, false));
                if (obj.getTipoDeclaracao().intValue() == 1 && disciplina != 0){
                    consultaPeriodoRealizacaoCurso(obj, disciplina);
                }
                return obj;
	}


        public void consultaPeriodoRealizacaoCurso(DeclaracaoFrequenciaVO obj, int disciplina) throws Exception {
            StringBuilder sqlStr = new StringBuilder("");
            sqlStr.append(" select historico.codigo, (select datainicio from periodoauladisciplinaaluno(historico.codigo)) as dataInicio, (select datatermino from periodoauladisciplinaaluno(historico.codigo)) as datatermino,  historico.freguencia as frequencia, ");
            sqlStr.append(" unidadeensino.nome as unidadeensino, cidade.nome as cidade, estado.sigla as estado");
            sqlStr.append(" from historico");
            sqlStr.append(" inner join matricula on matricula.matricula = historico.matricula");
            sqlStr.append(" inner join matriculaperiodoturmadisciplina on matricula.matricula = matriculaperiodoturmadisciplina.matricula");
            sqlStr.append(" and matriculaperiodoturmadisciplina.codigo = historico.matriculaperiodoturmadisciplina");
            sqlStr.append(" inner join matriculaperiodo on matriculaperiodo.codigo = matriculaperiodoturmadisciplina.matriculaperiodo");
            sqlStr.append(" inner join turma on turma.codigo = matriculaperiodoturmadisciplina.turma");
            sqlStr.append(" inner join unidadeensino on unidadeensino.codigo = turma.unidadeensino");
            sqlStr.append(" inner join cidade on cidade.codigo = unidadeensino.cidade");
            sqlStr.append(" inner join estado on estado.codigo = cidade.estado");
            sqlStr.append(" where historico.matricula = ?  and historico.disciplina = ? ");
            sqlStr.append(MatriculaPeriodoTurmaDisciplina.getSqlFiltroBaseGradeCurricularAtual(" and "));
            sqlStr.append(" order by historico.anohistorico desc, historico.semestrehistorico desc, historico.codigo desc limit 1");
            SqlRowSet tabelaResultado = getConexao().getJdbcTemplate().queryForRowSet(sqlStr.toString(), obj.getMatricula(), disciplina);
            montarPeriodoRealizacaoCurso(obj, tabelaResultado);
        }

        public void montarPeriodoRealizacaoCurso(DeclaracaoFrequenciaVO obj, SqlRowSet tabelaResultado) throws Exception {
            try {
                StringBuilder periodo = null;
                                
                if (tabelaResultado.next()) {
                    obj.setNomeUnidadeEnsino(tabelaResultado.getString("unidadeEnsino"));
                    obj.setCidadeUnidadeEnsino(tabelaResultado.getString("cidade")+"-"+tabelaResultado.getString("estado"));
                    obj.setFrequencia(String.valueOf(tabelaResultado.getDouble("frequencia")));
                    String dataInicio  = Uteis.getDataAno4Digitos(Uteis.getDataJDBC(tabelaResultado.getDate("dataInicio")));
                    String dataTermino  = Uteis.getDataAno4Digitos(Uteis.getDataJDBC(tabelaResultado.getDate("datatermino")));
                    periodo = new StringBuilder("");
                    if(dataInicio != null && dataInicio.length() == 10 && dataTermino != null && dataTermino.length() == 10) {
                    	periodo.append("").append(dataInicio.subSequence(0,2)).append(" de ").append(Uteis.getMesReferenciaExtenso(dataInicio.substring(3,5))).append(" de ").append(dataInicio.subSequence(6, 10));
                    	periodo.append(" à ").append(dataTermino.subSequence(0,2)).append(" de ").append(Uteis.getMesReferenciaExtenso(dataTermino.substring(3,5))).append(" de ").append(dataTermino.subSequence(6, 10));
                    }
                    obj.setPeriodoLetivo(periodo.toString());
                }
               
            } catch (Exception e) {
                throw e;
            }
        }

	public static String getDesignIReportRelatorio(DeclaracaoFrequenciaVO obj) {
                if (obj.getTipoDeclaracao().intValue() == 1){
        		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + "AtestadoFrequenciaRel" + ".jrxml");
                }else {
                	return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator + getIdEntidade() + ".jrxml");
                }
	}
	
	public static String getCaminhoBaseRelatorio() {
		return ("relatorio" + File.separator + "designRelatorio" + File.separator + "academico" + File.separator);
	}

	public static String getIdEntidade() {
		return ("DeclaracaoFrequenciaRel");
	}
	
	public static void validarDados(DeclaracaoFrequenciaVO declaracao, String disciplina, Date dataDeclaracao) throws Exception {
		if (declaracao.getNome().equals("") || declaracao.getMatricula().equals("")) {
			throw new Exception("O Aluno deve ser informado para criação do relatório.");
		}
		if (!Uteis.isAtributoPreenchido(dataDeclaracao)) {
			throw new Exception("A data da declaração deve ser informada para criação do relatório.");
		}
                if (declaracao.getTipoDeclaracao().intValue() > 0){
                    if (declaracao.getFuncionarioPrincipalVO().getPessoa().getNome().equals("")) {
                            throw new Exception("O RESPONSÁVEL deve ser informado para criação do relatório.");
                    }
                }


        }
}
