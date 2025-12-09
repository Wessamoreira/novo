package negocio.comuns.ead;

import java.util.Date;

import negocio.comuns.arquitetura.SuperVO;
import negocio.comuns.basico.PessoaVO;
import negocio.comuns.ead.enumeradores.TipoPessoaInteracaoDuvidaProfessorEnum;


public class DuvidaProfessorInteracaoVO extends SuperVO {
    
    private Integer codigo;
    private PessoaVO pessoa;
    private String interacao;
    private Date dataInteracao;
    private TipoPessoaInteracaoDuvidaProfessorEnum tipoPessoaInteracaoDuvidaProfessor;
    private DuvidaProfessorVO duvidaProfessor;
    
    public Integer getCodigo() {
        if(codigo == null){
            codigo = 0;
        }
        return codigo;
    }
    
    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }
    
    public PessoaVO getPessoa() {
        if(pessoa == null){
            pessoa = new PessoaVO();
        }
        return pessoa;
    }
    
    public void setPessoa(PessoaVO pessoa) {
        this.pessoa = pessoa;
    }
    
    public String getInteracao() {
        if(interacao == null){
            interacao = "";
        }
        return interacao;
    }
    
    public void setInteracao(String interacao) {
        this.interacao = interacao;
    }
    
    public Date getDataInteracao() {
        if(dataInteracao == null){
            dataInteracao = new Date();
        }
        return dataInteracao;
    }
    
    public void setDataInteracao(Date dataInteracao) {
        this.dataInteracao = dataInteracao;
    }
    
    public TipoPessoaInteracaoDuvidaProfessorEnum getTipoPessoaInteracaoDuvidaProfessor() {
        if(tipoPessoaInteracaoDuvidaProfessor == null){
            tipoPessoaInteracaoDuvidaProfessor = tipoPessoaInteracaoDuvidaProfessor.PROFESSOR;
        }
        return tipoPessoaInteracaoDuvidaProfessor;
    }
    
    public void setTipoPessoaInteracaoDuvidaProfessor(TipoPessoaInteracaoDuvidaProfessorEnum tipoPessoaInteracaoDuvidaProfessor) {
        this.tipoPessoaInteracaoDuvidaProfessor = tipoPessoaInteracaoDuvidaProfessor;
    }
    
    public DuvidaProfessorVO getDuvidaProfessor() {
        if(duvidaProfessor == null){
            duvidaProfessor = new DuvidaProfessorVO();
        }
        return duvidaProfessor;
    }
    
    public void setDuvidaProfessor(DuvidaProfessorVO duvidaProfessor) {
        this.duvidaProfessor = duvidaProfessor;
    }
    
    

}
