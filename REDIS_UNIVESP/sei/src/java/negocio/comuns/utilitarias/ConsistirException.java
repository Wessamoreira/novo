package negocio.comuns.utilitarias;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import negocio.comuns.arquitetura.SuperVO; 

public class ConsistirException extends Exception implements Serializable{
    private List<String> listaMensagemErro;
    private Boolean referenteChoqueHorario;
    private Boolean referentePreRequisito;
    private SuperVO objetoOrigem;
    private Boolean referenteDescontoContaReceber;
    
    public SuperVO getObjetoOrigem() {
		return objetoOrigem;
	}

	public void setObjetoOrigem(SuperVO objetoOrigem) {
		this.objetoOrigem = objetoOrigem;
	}

	public ConsistirException(String msgErro) {
        super(msgErro);
    }
	
	public ConsistirException(String msgErro, SuperVO objetoOrigem) {
        super(msgErro);
        setObjetoOrigem(objetoOrigem);
    }

    public ConsistirException() {
        
    }

    public boolean existeErroListaMensagemErro() {
        if (listaMensagemErro != null && getListaMensagemErro().size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    public void adicionarListaMensagemErro(String erro) {
    	if (!this.getListaMensagemErro().contains(erro)) {
    		this.getListaMensagemErro().add(erro);
    	}
    }

    public List<String> getListaMensagemErro() {
    	if(listaMensagemErro == null){
    		this.listaMensagemErro = new ArrayList<String>(0);
    	}
        return listaMensagemErro;
    }

    public void setListaMensagemErro(List<String> listaMensagemErro) {
        this.listaMensagemErro = listaMensagemErro;
    }

    /**
     * @return the referenteChoqueHorario
     */
    public Boolean getReferenteChoqueHorario() {
        if (referenteChoqueHorario == null) {
            referenteChoqueHorario = Boolean.FALSE;
        }
        return referenteChoqueHorario;
    }

    /**
     * @param referenteChoqueHorario the referenteChoqueHorario to set
     */
    public void setReferenteChoqueHorario(Boolean referenteChoqueHorario) {
        this.referenteChoqueHorario = referenteChoqueHorario;
    }
    
    public String getToStringMensagemErro(){
    	if(getListaMensagemErro().isEmpty()){
    		return getMessage();
    	}
    	StringBuilder erros = new StringBuilder();
    	for(String erro: getListaMensagemErro() ){
    		if(erros.length() != 0){
    			erros.append("</br>");	
    		}
    		erros.append(erro);
    	}
    	return erros.toString();	
    }
	
    public Boolean getReferentePreRequisito() {
    	if (referentePreRequisito == null) {
    		referentePreRequisito = Boolean.FALSE;
    	}
    	return referentePreRequisito;
    }
    
    public void setReferentePreRequisito(Boolean referentePreRequisito) {
    	this.referentePreRequisito = referentePreRequisito;
    }

	public Boolean getReferenteDescontoContaReceber() {
		if (referenteDescontoContaReceber == null) {
			referenteDescontoContaReceber = Boolean.FALSE;
        }
		return referenteDescontoContaReceber;
	}

	public void setReferenteDescontoContaReceber(Boolean referenteDescontoContaReceber) {
		this.referenteDescontoContaReceber = referenteDescontoContaReceber;
	}
	
	public ConsistirException(String msgErro, Boolean referenteDescontoContaReceber) {
        super(msgErro);
        setReferenteDescontoContaReceber(referenteDescontoContaReceber);
    }
    
}
