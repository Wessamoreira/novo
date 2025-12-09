package negocio.comuns.crm;

import negocio.comuns.academico.FiliacaoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;

/**
 * 
 * @author Leonardo Riciolle - 10/02/2014 
 * Reponsavel por manter os dados sobre os familares de Pais, Irmao ou Responsavel que já estudou na instituição alguma vez. 
 * Value Object USADO SOMENTE PARA MONTAR OS DADOS DOS FAMILIARES.
 */
public class FamiliaresVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	private PessoaVO familiar;
	private MatriculaVO matriculaVO;
	private FiliacaoVO filiacaoVO;

	public MatriculaVO getMatriculaVO() {
		if (matriculaVO == null) {
			matriculaVO = new MatriculaVO();
		}
		return matriculaVO;
	}

	public void setMatriculaVO(MatriculaVO matriculaVO) {
		this.matriculaVO = matriculaVO;
	}

	public FiliacaoVO getFiliacaoVO() {
		if (filiacaoVO == null) {
			filiacaoVO = new FiliacaoVO();
		}
		return filiacaoVO;
	}

	public void setFiliacaoVO(FiliacaoVO filiacaoVO) {
		this.filiacaoVO = filiacaoVO;
	}

	public PessoaVO getFamiliar() {
		if (familiar == null) {
			familiar = new PessoaVO();
		}
		return familiar;
	}

	public void setFamiliar(PessoaVO familiar) {
		this.familiar = familiar;
	}
}
