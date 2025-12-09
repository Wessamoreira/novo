package negocio.comuns.basico.enumeradores;

import negocio.comuns.utilitarias.UteisJSF;


public enum LayoutComprovanteMatriculaEnum {
     LAYOUT_01, LAYOUT_02;
     
     public String getValorApresentar(){
         return UteisJSF.internacionalizar("enum_LayoutComprovanteMatriculaEnum_"+this.toString());
     }
     
     public String getUrl(){
         if(this.equals(LayoutComprovanteMatriculaEnum.LAYOUT_02)){
             return "./../../resources/imagens/layout/layoutComprovanteMatricula2.png";
         }
         	 return "./../../resources/imagens/layout/layoutComprovanteMatricula1.png";
     }
}
