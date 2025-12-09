package negocio.comuns.biblioteca.enumeradores;

import jakarta.faces. model.SelectItem;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;


public enum TipoAutoriaEnum {

    AUTOR("AU", "Autor", "A."),
    CO_AUTOR("CA", "Co-Autor", "C.A."),
    COLABORADOR("CO", "Colaborador", "COL."),
    TRADUTOR("TR", "Tradutor", "TRAD."),
    COORDENADOR("CR", "Coordenador", "COORD."),
    ILUSTRADOR("IL", "Ilustrador", "ILUST."),
    ORGANIZADOR("OR", "Organizador", "ORG."),
    
    ADAPTADOR("AD", "Adaptador", "ADAPT."),
    EDITOR("ED", "Editor", "EDIT."),
    COMPILADOR("CP", "Compilador", "COMP."),
    AUTOR_ENTIDADE("AE", "Autor Entidade", "A.E."),
    DIRETOR("DI", "Diretor", "DIRET."),
    PRODUTOR("PR", "Produtor", "PROD."),
    ROTEIRISTA("RO", "Roteirista", "ROT."),
    CONSULTOR("CS", "Consultor", "CONS."),
    PSICOGRAFO("PS", "Psicógrafo", "PSIC."),
    ELABORADOR("EL", "Elaborador", "ELAB."),
    REDATOR("RE", "Redator", "REDA."),
    REVISOR("RV", "Revisor", "REVI."),
    ORIENTADOR("OD", "Orientador", "ORT."),
    SUPERVISOR("SU", "Supervisor", "SUPV.");
    
    
    private String key;
    private String value;
    private String sigla;
    
    private TipoAutoriaEnum(String key, String value, String sigla) {
        this.key = key;
        this.value = value;
        this.setSigla(sigla);
    }
    
    public String getKey() {
        return key;
    }
    
    public void setKey(String key) {
        this.key = key;
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}
	
	public static TipoAutoriaEnum getTipoAutoriaEnumPorKey(String key) {
		for(TipoAutoriaEnum tipoAutoriaEnum: values()) {
			if(tipoAutoriaEnum.getKey().equals(key)) {
				return tipoAutoriaEnum;
			}
		}
		return null;
	}
}
