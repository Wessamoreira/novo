package negocio.comuns.academico;

import negocio.comuns.administrativo.FuncionarioVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.utilitarias.ConsistirException;

public class TrabalhoConclusaoCursoMembroBancaVO extends SuperVO {

	private static final long serialVersionUID = -356246938786832194L;
	private Integer codigo;
	private Boolean convidado;
	private String nome;
	private FuncionarioVO membro;
	private TrabalhoConclusaoCursoVO trabalhoConclusaoCurso;
	
	

	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public TrabalhoConclusaoCursoVO getTrabalhoConclusaoCurso() {
		if(trabalhoConclusaoCurso == null){
			trabalhoConclusaoCurso = new TrabalhoConclusaoCursoVO();
		}
		return trabalhoConclusaoCurso;
	}

	public void setTrabalhoConclusaoCurso(TrabalhoConclusaoCursoVO trabalhoConclusaoCurso) {
		this.trabalhoConclusaoCurso = trabalhoConclusaoCurso;
	}

	public Boolean getConvidado() {
		if (convidado == null) {
			convidado = false;
			
		}
		return convidado;
	}

	public void setConvidado(Boolean convidado) {
		this.convidado = convidado;
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

	public FuncionarioVO getMembro() {
		if (membro == null) {
			membro = new FuncionarioVO();
		}
		return membro;
	}

	public void setMembro(FuncionarioVO membro) {
		this.membro = membro;
	}
	
    public static void validarDados(TrabalhoConclusaoCursoMembroBancaVO obj) throws ConsistirException {
        if (obj.getNome().trim().isEmpty()) {
            throw new ConsistirException("O campo NOME (Membro Banca) deve ser informado.");
        }
        if (!obj.getConvidado() && obj.getMembro().getCodigo().intValue() == 0) {
        	throw new ConsistirException("O campo MEMBRO (Membro Banca) deve ser informado.");
        }
    }

}
