package negocio.comuns.ead;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.enumeradores.TipoPessoaInteracaoDuvidaProfessorEnum;

/**
 * @author Victor Hugo de Paula Costa - 30 de jun de 2016
 *
 */
public class GestaoEventoConteudoTurmaInteracaoAtaVO extends SuperVO {

	private static final long serialVersionUID = 1L;
	/**
	 * @author Victor Hugo de Paula Costa - 30 de jun de 2016
	 */
	private Integer codigo;
	private GestaoEventoConteudoTurmaVO gestaoEventoConteudoTurmaVO;
	private PessoaVO pessoaVO;
	private TipoPessoaInteracaoDuvidaProfessorEnum tipoPessoa;
	private String interacao;
	private Date dataInteracao;

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

	public PessoaVO getPessoaVO() {
		if (pessoaVO == null) {
			pessoaVO = new PessoaVO();
		}
		return pessoaVO;
	}

	public void setPessoaVO(PessoaVO pessoaVO) {
		this.pessoaVO = pessoaVO;
	}

	public TipoPessoaInteracaoDuvidaProfessorEnum getTipoPessoa() {
		if (tipoPessoa == null) {
			tipoPessoa = TipoPessoaInteracaoDuvidaProfessorEnum.ALUNO;
		}
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoaInteracaoDuvidaProfessorEnum tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getInteracao() {
		if (interacao == null) {
			interacao = "";
		}
		return interacao;
	}

	public void setInteracao(String interacao) {
		this.interacao = interacao;
	}

	public Date getDataInteracao() {
		if (dataInteracao == null) {
			dataInteracao = new Date();
		}
		return dataInteracao;
	}

	public void setDataInteracao(Date dataInteracao) {
		this.dataInteracao = dataInteracao;
	}
	
	public boolean equalsGestaoEventoConteudoInteracaoAta(GestaoEventoConteudoTurmaInteracaoAtaVO obj){
		return getInteracao().equals(obj.getInteracao());
	}

}
