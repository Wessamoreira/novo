package negocio.comuns.financeiro;

import java.util.Date;

import negocio.comuns.academico.DescontoProgressivoVO;
import negocio.comuns.academico.MatriculaPeriodoVO;
import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.academico.TurmaVO;
import negocio.comuns.administrativo.AuditVO;
import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.administrativo.UnidadeEnsinoVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.compras.FornecedorVO;
import negocio.comuns.utilitarias.dominios.SituacaoContaReceber;
import negocio.comuns.utilitarias.dominios.TipoOrigemContaReceber;
import negocio.comuns.utilitarias.dominios.TipoPessoa;

/**
 * Reponsável por manter os dados da entidade ContaReceber. Classe do tipo VO -
 * Value Object composta pelos atributos da entidade com visibilidade protegida
 * e os métodos de acesso a estes atributos. Classe utilizada para apresentar e
 * manter em memória os dados desta entidade.
 * 
 * @see SuperVO
 */
public class ConsultaLogContaReceberVO extends SuperVO {

	private AuditVO auditVO;

	public static final long serialVersionUID = 1L;

	public ConsultaLogContaReceberVO() {
		super();
	}

	public AuditVO getAuditVO() {
		if (auditVO == null) {
			auditVO = new AuditVO();
		}
		return auditVO;
	}

	public void setAuditVO(AuditVO auditVO) {
		this.auditVO = auditVO;
	}

	

}
