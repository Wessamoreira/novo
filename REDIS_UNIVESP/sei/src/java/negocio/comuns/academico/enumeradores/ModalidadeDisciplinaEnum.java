package negocio.comuns.academico.enumeradores;

import java.util.ArrayList;
import java.util.List;

import javax.faces.model.SelectItem;

import negocio.comuns.administrativo.enumeradores.TipoCampoEnum;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UteisJSF;


public enum ModalidadeDisciplinaEnum {

    PRESENCIAL, ON_LINE, AMBAS, HIBRIDO;    
    public String getValorApresentar(){
        return UteisJSF.internacionalizar("enum_ModalidadeDisciplinaEnum_"+this.name());
    }
    
    private static List<SelectItem> listaSelectItemModalidadeDisciplina;
    public static List<SelectItem> getListaSelectItemModalidadeDisciplina(){
        if(listaSelectItemModalidadeDisciplina == null){
            listaSelectItemModalidadeDisciplina = new ArrayList<SelectItem>(0);
            listaSelectItemModalidadeDisciplina.add(new SelectItem(ModalidadeDisciplinaEnum.PRESENCIAL, ModalidadeDisciplinaEnum.PRESENCIAL.getValorApresentar()));
            listaSelectItemModalidadeDisciplina.add(new SelectItem(ModalidadeDisciplinaEnum.ON_LINE, ModalidadeDisciplinaEnum.ON_LINE.getValorApresentar()));
            listaSelectItemModalidadeDisciplina.add(new SelectItem(ModalidadeDisciplinaEnum.AMBAS, ModalidadeDisciplinaEnum.AMBAS.getValorApresentar()));            
        }
        return listaSelectItemModalidadeDisciplina;
    }
    
    private static List<SelectItem> listaSelectItemModalidadeDisciplinaEscolhaMatricula;
    public static List<SelectItem> getListaSelectItemModalidadeDisciplinaEscolhaMatricula(){
        if(listaSelectItemModalidadeDisciplinaEscolhaMatricula == null){
            listaSelectItemModalidadeDisciplinaEscolhaMatricula = new ArrayList<SelectItem>(0);
            listaSelectItemModalidadeDisciplinaEscolhaMatricula.add(new SelectItem(ModalidadeDisciplinaEnum.PRESENCIAL, ModalidadeDisciplinaEnum.PRESENCIAL.getValorApresentar()));
            listaSelectItemModalidadeDisciplinaEscolhaMatricula.add(new SelectItem(ModalidadeDisciplinaEnum.ON_LINE, ModalidadeDisciplinaEnum.ON_LINE.getValorApresentar()));  
            listaSelectItemModalidadeDisciplinaEscolhaMatricula.add(new SelectItem(ModalidadeDisciplinaEnum.HIBRIDO, ModalidadeDisciplinaEnum.HIBRIDO.getValorApresentar()));
        }
        return listaSelectItemModalidadeDisciplinaEscolhaMatricula;
    }
    
    public boolean isPresencial(){
		return Uteis.isAtributoPreenchido(name()) && name().equals(ModalidadeDisciplinaEnum.PRESENCIAL.name()); 
	}
    
    public boolean isOnline(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(ModalidadeDisciplinaEnum.ON_LINE.name()); 
    }
    
    public boolean isAmbas(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(ModalidadeDisciplinaEnum.AMBAS.name()); 
    }
    
    public boolean isHibrido(){
    	return Uteis.isAtributoPreenchido(name()) && name().equals(ModalidadeDisciplinaEnum.HIBRIDO.name()); 
    }
    
    
    
}
