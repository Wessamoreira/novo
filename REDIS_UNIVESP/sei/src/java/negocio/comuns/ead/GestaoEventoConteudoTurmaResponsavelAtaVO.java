package negocio.comuns.ead;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.enumeradores.FuncaoResponsavelAtaEnum;

/**
 * @author Victor Hugo de Paula Costa - 30 de jun de 2016
 *
 */
public class GestaoEventoConteudoTurmaResponsavelAtaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	/**
	 * @author Victor Hugo de Paula Costa - 30 de jun de 2016
	 */
	private Integer codigo;
	private GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO;
	private PessoaVO aluno;
	private FuncaoResponsavelAtaEnum funcao;

	public Integer getCodigo() {
		if (codigo == null) {
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public GestaoEventoConteudoTurmaVO getGestaoEventoConteudoTurmaVO() {
		if (gestaoEventoConteudoTurmaVO == null) {
			gestaoEventoConteudoTurmaVO = new GestaoEventoConteudoTurmaVO();
		}
		return gestaoEventoConteudoTurmaVO;
	}

	public void setGestaoEventoConteudoTurmaVO(GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO) {
		this.gestaoEventoConteudoTurmaVO = gestaoEventoConteudoTurmaVO;
	}

	public PessoaVO getAluno() {
		if (aluno == null) {
			aluno = new PessoaVO();
		}
		return aluno;
	}

	public void setAluno(PessoaVO aluno) {
		this.aluno = aluno;
	}

	public FuncaoResponsavelAtaEnum getFuncao() {
		if (funcao == null) {
			funcao = FuncaoResponsavelAtaEnum.PALESTRANTE;
		}
		return funcao;
	}

	public void setFuncao(FuncaoResponsavelAtaEnum funcao) {
		this.funcao = funcao;	
	}
	
	public boolean equalsGestaoEventoConteudoAta(GestaoEventoConteudoTurmaResponsavelAtaVO obj) {
		return obj.getAluno().getCodigo().equals(getAluno().getCodigo()) && obj.getFuncao().equals(getFuncao());
	}
	
	
}
