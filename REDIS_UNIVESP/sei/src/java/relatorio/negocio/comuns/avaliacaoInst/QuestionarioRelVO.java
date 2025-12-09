/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package relatorio.negocio.comuns.avaliacaoInst;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.administrativo.CargoVO;
import negocio.comuns.administrativo.DepartamentoVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.utilitarias.dominios.PublicoAlvoAvaliacaoInstitucional;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;

import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 *
 * @author Rodrigo
 */
public class QuestionarioRelVO {

    protected String nome;
    protected Integer codigo;
    protected Integer codigoUnidadeEnsino;
    protected String nomeUnidadeEnsino;
    protected Integer codigoCurso;
    protected String nomeCurso;
    protected Integer codigoTurma;
    protected String identificadorTurma;
    protected Integer codigoDisciplina;
    protected String nomeDisciplina;
    protected Integer codigoProfessor;
    protected String nomeProfessor;
    protected Integer codigoRespondente;
    protected String nomeRespondente;
    protected String escopo;
    private PessoaVO coordenador;
    private CargoVO cargo;
    private DepartamentoVO departamento;
    protected List<PerguntaRelVO> perguntaRelVOs;
    private String link;
    private String nomeLink;
    private Integer qtdeRelatorio;
    private Integer qtdeRespondentes;
    private JRDataSource perguntasJR;
    

    public QuestionarioRelVO() {
    }

    public void inicializarDadosRespostaQuestionario(SqlRowSet rs) throws SQLException {
        for (PerguntaRelVO objExistente : getPerguntaRelVOs()) {
            if (objExistente.getCodigo().intValue() == rs.getInt("Pergunta_codigo")) {
                objExistente.inicializarDadosRespostaQuestionario(rs);
            }
        }
    }

    public JRDataSource getPerguntasJR() {
    	if(perguntasJR == null){
    		perguntasJR = new JRBeanArrayDataSource(getPerguntaRelVOs().toArray());
    	}
    	return perguntasJR;
    }
    
    public void setPerguntasJR(JRDataSource perguntasJR) {    	
    	this.perguntasJR = perguntasJR;
    }
    
    public void gerarGrafico() {
        for (PerguntaRelVO obj : getPerguntaRelVOs()) {
            obj.criarGraficoResposta();
        }
    }

    public PerguntaRelVO consultarPerguntaRelVOs(Integer pergunta) {
        for (PerguntaRelVO obj : getPerguntaRelVOs()) {
            if (obj.getCodigo().intValue() == pergunta.intValue()) {
                return obj;
            }
        }
        PerguntaRelVO obj = new PerguntaRelVO();
        return obj;
    }

    public void adiconarPerguntaRelVOs(PerguntaRelVO obj) {
        int index = 0;
        for (PerguntaRelVO objExistente : getPerguntaRelVOs()) {
            if (objExistente.getCodigo().intValue() == obj.getCodigo().intValue()) {
            	getPerguntaRelVOs().set(index, obj);
                return;
            }
            index++;
        }
        getPerguntaRelVOs().add(obj);
    }

    public String getNomeApresentar() {
    	if (getCoordenador().getCodigo() > 0) {
    		return " - Unidade Ensino:" + getNomeUnidadeEnsino() + " Coordenador Curso:" + getCoordenador().getNome();
    	}
    	if (getCargo().getCodigo() > 0) {
    		return " - Unidade Ensino:" + getNomeUnidadeEnsino() + " Cargo:" + getCargo().getNome();
    	}
    	if (getDepartamento().getCodigo() > 0) {
    		return " - Unidade Ensino:" + getNomeUnidadeEnsino() + " Departamento:" + getDepartamento().getNome();
    	}
        if (getEscopo().equals("CU")) {
            return " - Unidade Ensino:" + getNomeUnidadeEnsino() + " Curso:" + getNomeCurso();
        }
        if (getEscopo().equals("DI")) {
            return " - Unidade Ensino:" + getNomeUnidadeEnsino() + " - Curso:" + getNomeCurso() + " - " + getNomeDisciplina();
        }
        if (getEscopo().equals("UE")) {
            return " - Unidade Ensino:" + getNomeUnidadeEnsino();
        }
        return getNome();


    }

    public Boolean getApresentarDisciplina() {
        if (!getNomeDisciplina().trim().equals("")) {
            return true;
        }
        return false;
    }

    public Boolean getApresentarProfessor() {
        if (!getNomeProfessor().trim().equals("")) {
            return true;
        }
        return false;
    }

    public Boolean getApresentarUnidadeEnsino() {
        if (!getNomeUnidadeEnsino().trim().equals("")) {
            return true;
        }
        return false;
    }

    public Boolean getApresentarCurso() {
        if (!getNomeCurso().trim().equals("")) {
            return true;
        }
        return false;
    }

    public Integer getCodigoCurso() {
        if (codigoCurso == null) {
            codigoCurso = 0;
        }
        return codigoCurso;
    }

    public void setCodigoCurso(Integer codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    public Integer getCodigoDisciplina() {
        if (codigoDisciplina == null) {
            codigoDisciplina = 0;
        }
        return codigoDisciplina;
    }

    public void setCodigoDisciplina(Integer codigoDisciplina) {
        this.codigoDisciplina = codigoDisciplina;
    }

    public Integer getCodigoUnidadeEnsino() {
        if (codigoUnidadeEnsino == null) {
            codigoUnidadeEnsino = 0;
        }
        return codigoUnidadeEnsino;
    }

    public void setCodigoUnidadeEnsino(Integer codigoUnidadeEnsino) {
        this.codigoUnidadeEnsino = codigoUnidadeEnsino;
    }

    public String getEscopo() {
        if (escopo == null) {
            escopo = "";
        }
        return escopo;
    }

    public void setEscopo(String escopo) {
        this.escopo = escopo;
    }

    public String getNomeCurso() {
        if (nomeCurso == null) {
            nomeCurso = "";
        }
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    public String getNomeDisciplina() {
        if (nomeDisciplina == null) {
            nomeDisciplina = "";
        }
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public String getNomeUnidadeEnsino() {
        if (nomeUnidadeEnsino == null) {
            nomeUnidadeEnsino = "";
        }
        return nomeUnidadeEnsino;
    }

    public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
        this.nomeUnidadeEnsino = nomeUnidadeEnsino;
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

    public List<PerguntaRelVO> getPerguntaRelVOs() {
        if (perguntaRelVOs == null) {
            perguntaRelVOs = new ArrayList<PerguntaRelVO>(0);
        }
        return perguntaRelVOs;
    }

    public void setPerguntaRelVOs(List<PerguntaRelVO> perguntaRelVOs) {
        this.perguntaRelVOs = perguntaRelVOs;
    }

    public Integer getCodigo() {
        if (codigo == null) {
            codigo = 0;
        }
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public Integer getCodigoProfessor() {
        if (codigoProfessor == null) {
            codigoProfessor = 0;
        }
        return codigoProfessor;
    }

    public void setCodigoProfessor(Integer codigoProfessor) {
        this.codigoProfessor = codigoProfessor;
    }

    public String getNomeProfessor() {
        if (nomeProfessor == null) {
            nomeProfessor = "";
        }
        return nomeProfessor;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

	public PessoaVO getCoordenador() {
		if (coordenador == null) {
			coordenador = new PessoaVO();
		}
		return coordenador;
	}

	public void setCoordenador(PessoaVO coordenador) {
		this.coordenador = coordenador;
	}

	public CargoVO getCargo() {
		if (cargo == null) {
			cargo = new CargoVO();
		}
		return cargo;
	}

	public void setCargo(CargoVO cargo) {
		this.cargo = cargo;
	}

	public DepartamentoVO getDepartamento() {
		if (departamento == null) {
			departamento = new DepartamentoVO();
		}
		return departamento;
	}

	public void setDepartamento(DepartamentoVO departamento) {
		this.departamento = departamento;
	}
    
    public String getOrdenacao(){
    	return getNomeUnidadeEnsino()+getNomeCurso()+getNomeDisciplina()+getNomeProfessor()+getCoordenador().getNome()+getDepartamento().getNome()+getCargo().getNome()+getIdentificadorTurma();
    }

	public Integer getCodigoRespondente() {
		if (codigoRespondente == null) {
			codigoRespondente = 0;
		}
		return codigoRespondente;
	}

	public void setCodigoRespondente(Integer codigoRespondente) {
		this.codigoRespondente = codigoRespondente;
	}

	public String getNomeRespondente() {
		if (nomeRespondente == null) {
			nomeRespondente = "";
		}
		return nomeRespondente;
	}

	public void setNomeRespondente(String nomeRespondente) {
		this.nomeRespondente = nomeRespondente;
	}
    

	public String getLink() {
		if(link == null){
			link = "";
		}
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getNomeLink() {
		if(nomeLink == null){
			nomeLink = "";
		}
		return nomeLink;
	}

	public void setNomeLink(String nomeLink) {
		this.nomeLink = nomeLink;
	}
	

	public Integer getQtdeRelatorio() {
		if(qtdeRelatorio == null){
			qtdeRelatorio = 0;
		}
		return qtdeRelatorio;
	}

	public void setQtdeRelatorio(Integer qtdeRelatorio) {
		this.qtdeRelatorio = qtdeRelatorio;
	}

	public Integer getQtdeRespondentes() {
		if(qtdeRespondentes == null){
			qtdeRespondentes = 0;
		}
		return qtdeRespondentes;
	}

	public void setQtdeRespondentes(Integer qtdeRespondentes) {
		this.qtdeRespondentes = qtdeRespondentes;
	}
	

	public Integer getCodigoTurma() {
		if(codigoTurma == null){
			codigoTurma = 0;
		}
		return codigoTurma;
	}

	public void setCodigoTurma(Integer codigoTurma) {
		this.codigoTurma = codigoTurma;
	}

	public String getIdentificadorTurma() {
		if(identificadorTurma == null){
			identificadorTurma = "";
		}
		return identificadorTurma;
	}

	public void setIdentificadorTurma(String identificadorTurma) {
		this.identificadorTurma = identificadorTurma;
	}
	
}
