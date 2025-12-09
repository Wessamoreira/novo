package negocio.comuns.biblioteca;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;

public class ReservaVO extends SuperVO {

    private Integer codigo;
    private PessoaVO pessoa;
    private CatalogoVO catalogo;
    private Date dataReserva;
    private Date dataTerminoReserva;
    private Integer emprestimo;
    private BibliotecaVO bibliotecaVO;
    private String tipoPessoa;
    // Campos transientes para ajudar na tela de empréstimo.
    private Integer nrExemplaresDisponiveisDesseCatalogo;
    private String situacao;
    private String matricula;
    // Campo Usando no metodo executarReservaCatalogos ( Catalogo.java ) 
    //para auxiliar na mensagem de tela da Reserva 
    private String mensagemListaCatalogoReservado;
    
    public static final long serialVersionUID = 1L;

    public ReservaVO() {
        super();
    }

    public PessoaVO getPessoa() {
        if (pessoa == null) {
            pessoa = new PessoaVO();
        }
        return pessoa;
    }

    public void setPessoa(PessoaVO pessoa) {
        this.pessoa = pessoa;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public CatalogoVO getCatalogo() {
        if (catalogo == null) {
            catalogo = new CatalogoVO();
        }
        return catalogo;
    }

    public void setCatalogo(CatalogoVO catalogo) {
        this.catalogo = catalogo;
    }

    public Date getDataReserva() {
        if (dataReserva == null) {
            dataReserva = new Date();
        }
        return dataReserva;
    }

    public void setDataReserva(Date dataReserva) {
        this.dataReserva = dataReserva;
    }

    public Date getDataTerminoReserva() {
//        if (dataTerminoReserva == null) {
//            dataTerminoReserva = new Date();
//        }
        return dataTerminoReserva;
    }

    public void setDataTerminoReserva(Date dataTerminoReserva) {
        this.dataTerminoReserva = dataTerminoReserva;
    }

    public Integer getEmprestimo() {
        if (emprestimo == null) {
            emprestimo = 0;
        }
        return emprestimo;
    }

    public void setEmprestimo(Integer emprestimo) {
        this.emprestimo = emprestimo;
    }

    public void setNrExemplaresDisponiveisDesseCatalogo(Integer nrExemplaresDisponiveisDesseCatalogo) {
        this.nrExemplaresDisponiveisDesseCatalogo = nrExemplaresDisponiveisDesseCatalogo;
    }

    public Integer getNrExemplaresDisponiveisDesseCatalogo() {
        if (nrExemplaresDisponiveisDesseCatalogo == null) {
            nrExemplaresDisponiveisDesseCatalogo = 0;
        }
        return nrExemplaresDisponiveisDesseCatalogo;
    }
     public BibliotecaVO getBibliotecaVO() {
    	if (bibliotecaVO == null) {
    		bibliotecaVO = new BibliotecaVO();
    	}
        return bibliotecaVO;
    }

    public void setBibliotecaVO(BibliotecaVO bibliotecaVO) {
        this.bibliotecaVO = bibliotecaVO;
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa (String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

	public String getSituacao() {
		if (situacao == null) {
			situacao = "";
		}
		return situacao;
	}

	public void setSituacao(String situacao) {
		this.situacao = situacao;
	}

	public String getMatricula() {
		if (matricula == null) {
			matricula = "";
		}
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}


	public String getMensagemListaCatalogoReservado() {
		if(mensagemListaCatalogoReservado == null){
			mensagemListaCatalogoReservado = "";
		}
		return mensagemListaCatalogoReservado;
	}

	public void setMensagemListaCatalogoReservado(String mensagemListaCatalogoReservado) {
		this.mensagemListaCatalogoReservado = mensagemListaCatalogoReservado;
	}
}