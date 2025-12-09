package negocio.comuns.crm;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.facade.jdbc.crm.Prospects;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import relatorio.negocio.comuns.arquitetura.CrosstabVO;

/**

 * @see SuperVO
 * @see Prospects
 * @author Kennedy Souza
*/

public class ConsultorPorMatriculaRelVO extends SuperVO {
	
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private List<CrosstabVO> crosstabVOs;
	private String turma;
	private List<FuncionarioVO> consultor;
	private Integer totalAlunos;
	private String unidadeEnsino;
	
	  public List<CrosstabVO> getCrosstabVOs() {
			if (crosstabVOs == null) {
				crosstabVOs = new ArrayList<CrosstabVO>(0);
			}
			return crosstabVOs;
		}

		public void setCrosstabVOs(List<CrosstabVO> crosstabVOs) {
			this.crosstabVOs = crosstabVOs;
		}
		

		public String getTurma() {
			if (turma == null) {
				turma = "";
			}
			return turma;
		}

		public void setTurma(String turma) {
			this.turma = turma;
		}

		public JRDataSource getListaConsultor() {
		        return new JRBeanArrayDataSource(getConsultor().toArray());
	    }
		 
		public List<FuncionarioVO> getConsultor() {
			if (consultor == null) {
				consultor = new ArrayList<FuncionarioVO>();
			}
			return consultor;
		}

		public void setConsultor(List<FuncionarioVO> consultor) {
			this.consultor = consultor;
		}

		public Integer getTotalAlunos() {
			if (totalAlunos == null) {
				totalAlunos = 0;
				for(FuncionarioVO funcionarioVO : getConsultor()) {
					totalAlunos += funcionarioVO.getMatriculaConsultorVO().size();
				}
			}
			return totalAlunos;
		}

		public void setTotalAlunos(Integer totalAlunos) {
			this.totalAlunos = totalAlunos;
		}

		public String getUnidadeEnsino() {
			return unidadeEnsino;
		}

		public void setUnidadeEnsino(String unidadeEnsino) {
			this.unidadeEnsino = unidadeEnsino;
		}		
		
}