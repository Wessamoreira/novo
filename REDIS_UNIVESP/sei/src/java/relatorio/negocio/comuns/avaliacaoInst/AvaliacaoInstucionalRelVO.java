/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.avaliacaoInst;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import relatorio.negocio.comuns.avaliacaoInst.enumeradores.NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum;

/**
 *
 * @author Rodrigo
 */
public class AvaliacaoInstucionalRelVO {

    protected String nome;
    protected List<QuestionarioRelVO> questionarioRelVOs;

    public AvaliacaoInstucionalRelVO() {
    }

    public QuestionarioRelVO consultaQuestionarioRelVOs(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDatalhamento, Integer codigoUnidadeEnsinoParam, Integer codigoQuestionario, Integer unidadeEnsino, Integer curso, Integer disciplina, Integer codigoProfessor, String escopo, Integer coordenador, Integer cargo, Integer departamento, Integer turma) {
        for (QuestionarioRelVO obj : getQuestionarioRelVOs()) {
			if (nivelDatalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.GERAL)) {
				if (obj.getCodigo().intValue() == codigoQuestionario) {
					return obj;
				}
			}else {
				if (obj.getCodigo().intValue() == codigoQuestionario 
						&& obj.getCodigoUnidadeEnsino().equals(unidadeEnsino) 
						&& obj.getCodigoCurso().equals(curso) && obj.getEscopo().equals(escopo) 
						&& obj.getCodigoTurma().equals(turma)
						&& obj.getCodigoDisciplina().equals(disciplina) 
						&& obj.getCodigoProfessor().intValue() == codigoProfessor
						&& obj.getCoordenador().getCodigo().equals(coordenador)
						&& obj.getCargo().getCodigo().equals(cargo)
						&& obj.getDepartamento().getCodigo().equals(departamento)) {
					return obj;
				}
			}
		}
        QuestionarioRelVO obj = new QuestionarioRelVO();
        return obj;
    }

    public QuestionarioRelVO consultaQuestionarioRelRespondenteVOs(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDatalhamento, Integer codigoUnidadeEnsinoParam, Integer codigoQuestionario, Integer unidadeEnsino, Integer curso, Integer disciplina, Integer codigoProfessor, String escopo, Integer codigoRespondente, Integer coordenador, Integer departamento, Integer cargo, Integer turma) {
    	for (QuestionarioRelVO obj : getQuestionarioRelVOs()) {
    		if (nivelDatalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.GERAL)) {
    			if (obj.getCodigo().intValue() == codigoQuestionario
    					&& ((codigoUnidadeEnsinoParam != 0 && obj.getCodigoUnidadeEnsino().equals(unidadeEnsino)) || codigoUnidadeEnsinoParam == 0)) {
    				return obj;
    			}
    		}else {
    			if (obj.getCodigo().intValue() == codigoQuestionario
    					&& obj.getCodigoUnidadeEnsino().equals(unidadeEnsino)
    					&& obj.getCodigoCurso().equals(curso)
    					&& obj.getEscopo().equals(escopo)
    					&& obj.getCodigoDisciplina().equals(disciplina)
    					&& obj.getCodigoTurma().equals(turma)
    					&& obj.getCodigoProfessor().intValue() == codigoProfessor
    					&& obj.getCoordenador().getCodigo().intValue() == coordenador
    					&& obj.getDepartamento().getCodigo().intValue() == departamento
    					&& obj.getCargo().getCodigo().intValue() == cargo
    					&& obj.getCodigoRespondente().intValue() == codigoRespondente) {
    				return obj;
    			} 
    		}
    	}
    	QuestionarioRelVO obj = new QuestionarioRelVO();
    	return obj;
    }
    
    public void adicionarQuestionarioRelVOs(QuestionarioRelVO obj, NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum nivelDatalhamento, Integer unidadeEnsino) {
        int index = 0;
        for (QuestionarioRelVO objExistente : getQuestionarioRelVOs()) {
			if (nivelDatalhamento.equals(NivelDetalhamentoResultadoAvaliacaoInstitucionalEnum.GERAL)) {
				if (objExistente.getCodigo().intValue() == obj.getCodigo().intValue()				
				) {
					getQuestionarioRelVOs().set(index, obj);
					return;
				}            
			} else {
                if (objExistente.getCodigo().intValue() == obj.getCodigo().intValue()
                        && obj.getCodigoUnidadeEnsino().equals(objExistente.getCodigoUnidadeEnsino())
                        && obj.getCodigoCurso().equals(objExistente.getCodigoCurso())
                        && obj.getEscopo().equals(objExistente.getEscopo())
                        && obj.getCodigoDisciplina().equals(objExistente.getCodigoDisciplina())
                        && obj.getCodigoProfessor().equals(objExistente.getCodigoProfessor())
                        && obj.getCoordenador().getCodigo().equals(objExistente.getCoordenador().getCodigo())
                        && obj.getCargo().getCodigo().equals(objExistente.getCargo().getCodigo())
                        && obj.getDepartamento().getCodigo().equals(objExistente.getDepartamento().getCodigo())
                        && obj.getCodigoTurma().equals(objExistente.getCodigoTurma())
                        && obj.getCodigoRespondente().intValue() == objExistente.getCodigoRespondente().intValue()) {
                	
                    getQuestionarioRelVOs().set(index, obj);
                    return;
                }
            }
            index++;
        }
        getQuestionarioRelVOs().add(obj);
    }

    public void inicializarDadosRespostaQuestionario(SqlRowSet rs) throws SQLException {
        for (QuestionarioRelVO objExistente : getQuestionarioRelVOs()) {
            if (objExistente.getCodigo().intValue() == rs.getInt("Questionario_codigo")) {
                objExistente.inicializarDadosRespostaQuestionario(rs);
            }
        }
    }

    public void gerarGrafico() {
        for (QuestionarioRelVO obj : getQuestionarioRelVOs()) {
            obj.gerarGrafico();
        }
    }

    public JRDataSource getQuestionarioJR() {
        return new JRBeanArrayDataSource(getQuestionarioRelVOs().toArray());
    }

    public String getNome() {
        if (nome == null) {
            nome = "";
        }
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<QuestionarioRelVO> getQuestionarioRelVOs() {
        if (questionarioRelVOs == null) {
            questionarioRelVOs = new ArrayList<QuestionarioRelVO>(0);
        }
        return questionarioRelVOs;
    }

    public void setQuestionarioRelVOs(List<QuestionarioRelVO> questionarioRelVOs) {
        this.questionarioRelVOs = questionarioRelVOs;
    }
}
