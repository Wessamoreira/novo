package negocio.comuns.administrativo;

import java.util.Date;

import negocio.comuns.administrativo.enumeradores.OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum;


public class PainelGestorMonitoramentoAcademicoVO {
    
    private OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum opcao;
    private String titulo;    
    private Integer quantidade;
    private Integer codigo;
    
    private Integer codigoAluno;
    private String nomeAluno;
    private String email;
    private String sexo;
    private String telefone;
    private String matricula;
    private Integer codigoTurno;
    private String nomeTurno;
    private Integer codigoTurma;
    private String nomeTurma;
    private Integer codigoCurso;
    private String nomeCurso;
    private Integer codigoProfessor;
    private String nomeProfessor;
    private Integer codigoUnidadeEnsino;
    private String nomeUnidadeEnsino;
    
    private String informacaoAdicional;
    private String consultor;
    private Date  data;
    private String textoPadraoDeclaracao_Descricao; 
    
    public PainelGestorMonitoramentoAcademicoVO() {
        super();     
    }
    
    public PainelGestorMonitoramentoAcademicoVO(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum opcao, String titulo, Integer quantidade, Integer codigo) {
        super();
        this.opcao = opcao;
        this.titulo = titulo;
        this.quantidade = quantidade;
        this.codigo = codigo;
    }

    public OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum getOpcao() {
        return opcao;
    }
    
    public void setOpcao(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum opcao) {
        this.opcao = opcao;
    }
    
    public String getTitulo() {
        if(titulo == null){
            titulo = "";
        }
        return titulo;
    }
    
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }
    
    
    
    public Integer getQuantidade() {
        if(quantidade == null){
            quantidade = 0;
        }
        return quantidade;
    }
    
    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
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

    
    public Integer getCodigoAluno() {
        return codigoAluno;
    }

    
    public void setCodigoAluno(Integer codigoAluno) {
        this.codigoAluno = codigoAluno;
    }

    
    public String getNomeAluno() {
        return nomeAluno;
    }

    
    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    
    public String getEmail() {
        return email;
    }

    
    public void setEmail(String email) {
        this.email = email;
    }

    
    public String getTelefone() {
        return telefone;
    }

    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    
    public String getMatricula() {
        return matricula;
    }

    
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    
    public Integer getCodigoTurno() {
        return codigoTurno;
    }

    
    public void setCodigoTurno(Integer codigoTurno) {
        this.codigoTurno = codigoTurno;
    }

    
    public String getNomeTurno() {
        return nomeTurno;
    }

    
    public void setNomeTurno(String nomeTurno) {
        this.nomeTurno = nomeTurno;
    }

    
    public Integer getCodigoTurma() {
        return codigoTurma;
    }

    
    public void setCodigoTurma(Integer codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    
    public String getNomeTurma() {
        return nomeTurma;
    }

    
    public void setNomeTurma(String nomeTurma) {
        this.nomeTurma = nomeTurma;
    }

    
    public Integer getCodigoCurso() {
        return codigoCurso;
    }

    
    public void setCodigoCurso(Integer codigoCurso) {
        this.codigoCurso = codigoCurso;
    }

    
    public String getNomeCurso() {
        return nomeCurso;
    }

    
    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }

    
    public Integer getCodigoProfessor() {
        return codigoProfessor;
    }

    
    public void setCodigoProfessor(Integer codigoProfessor) {
        this.codigoProfessor = codigoProfessor;
    }

    
    public String getNomeProfessor() {
        return nomeProfessor;
    }

    
    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

    
    public Integer getCodigoUnidadeEnsino() {
        return codigoUnidadeEnsino;
    }

    
    public void setCodigoUnidadeEnsino(Integer codigoUnidadeEnsino) {
        this.codigoUnidadeEnsino = codigoUnidadeEnsino;
    }

    
    public String getNomeUnidadeEnsino() {
        return nomeUnidadeEnsino;
    }

    
    public void setNomeUnidadeEnsino(String nomeUnidadeEnsino) {
        this.nomeUnidadeEnsino = nomeUnidadeEnsino;
    }

    
    public String getSexo() {
        return sexo;
    }

    
    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    
    public String getInformacaoAdicional() {
        if(informacaoAdicional == null){
            informacaoAdicional = "";
        }
        return informacaoAdicional;
    }

    
    public void setInformacaoAdicional(String informacaoAdicional) {
        this.informacaoAdicional = informacaoAdicional;
    }

    /**
     * @return the consultor
     */
    public String getConsultor() {
        if(consultor == null){
            consultor = "";
        }
        return consultor;
    }

    /**
     * @param consultor the consultor to set
     */
    public void setConsultor(String consultor) {
        this.consultor = consultor;
    }

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}
    
	
	public Boolean getApresentarColunaData(){
		return opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.CANCELADO)
				|| opcao.equals(OpcoesFiltroPainelGestorMonitoramentoAcademicoEnum.DETALHE_CANCELAMENTO);
		
	}

	public String getTextoPadraoDeclaracao_Descricao() {
		if(textoPadraoDeclaracao_Descricao == null){
			textoPadraoDeclaracao_Descricao = "";
		}
		return textoPadraoDeclaracao_Descricao;
	}

	public void setTextoPadraoDeclaracao_Descricao(String textoPadraoDeclaracao_Descricao) {
		this.textoPadraoDeclaracao_Descricao = textoPadraoDeclaracao_Descricao;
	}
    

}
