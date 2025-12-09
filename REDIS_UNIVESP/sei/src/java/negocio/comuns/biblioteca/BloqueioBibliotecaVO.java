package negocio.comuns.biblioteca;

import java.util.Date;

import negocio.comuns.academico.MatriculaVO;
import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.arquitetura.UsuarioVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.ConsistirException;
import negocio.comuns.utilitarias.Uteis;

public class BloqueioBibliotecaVO extends SuperVO {

    private Date data;
    private String tipoPessoa;
    private MatriculaVO matricula;
    private UsuarioVO atendente;
    private BibliotecaVO biblioteca;
    private PessoaVO pessoa;
    private String motivoBloqueio;
    private Date dataLimiteBloqueio;
    private Integer codigo;
    public static final long serialVersionUID = 1L;

    public BloqueioBibliotecaVO() {
        super();
    }

    public static void validarDados(BloqueioBibliotecaVO obj) throws ConsistirException {
        if (!obj.isValidarDados().booleanValue()) {
            return;
        }
        if (obj.getData() == null) {
            throw new ConsistirException("O campo DATA (Bloqueio Biblioteca) deve ser informado.");
        }
        if ((obj.getAtendente() == null) || (obj.getAtendente().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo ATENDENTE (Bloqueio Biblioteca) deve ser informado.");
        }
        if ((obj.getBiblioteca() == null) || (obj.getBiblioteca().getCodigo().intValue() == 0)) {
            throw new ConsistirException("O campo BIBLIOTECA (Bloqueio Biblioteca) deve ser informado.");
        }
    }

    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return (pessoa);
    }

    public void setPessoa(PessoaVO obj) {
        this.pessoa = obj;
    }

    public BibliotecaVO getBiblioteca() {
        if (biblioteca == null) {
            biblioteca = new BibliotecaVO();
        }
        return (biblioteca);
    }

    public void setBiblioteca(BibliotecaVO obj) {
        this.biblioteca = obj;
    }

    public UsuarioVO getAtendente() {
        if (atendente == null) {
            atendente = new UsuarioVO();
        }
        return (atendente);
    }

    public void setAtendente(UsuarioVO obj) {
        this.atendente = obj;
    }

    public Date getData() {
        if (data == null) {
            data = new Date();
        }
        return (data);
    }

    public String getData_Apresentar() {
        return (Uteis.getData(data));
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    public String getTipoPessoa() {
        if (tipoPessoa == null) {
            tipoPessoa = "AL";
        }
        return tipoPessoa;
    }

	public String getMotivoBloqueio() {
		if(motivoBloqueio == null){
			motivoBloqueio = "";
		}
		return motivoBloqueio;
	}

	public void setMotivoBloqueio(String motivoBloqueio) {
		this.motivoBloqueio = motivoBloqueio;
	}

	public Date getDataLimiteBloqueio() {		
		return dataLimiteBloqueio;
	}

	public void setDataLimiteBloqueio(Date dataLimiteBloqueio) {
		this.dataLimiteBloqueio = dataLimiteBloqueio;
	}

	public Integer getCodigo() {
		if(codigo == null){
			codigo = 0;
		}
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	public MatriculaVO getMatricula() {
		if(matricula == null){
			matricula = new MatriculaVO();
		}
		return matricula;
	}

	public void setMatricula(MatriculaVO matricula) {
		this.matricula = matricula;
	}
    

}
