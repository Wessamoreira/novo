package controle.arquitetura;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import jakarta.faces. model.SelectItem;



public class ControleConsultaTurma extends DataModelo {


    /**
	 * 
	 */
	private static final long serialVersionUID = 5024808803129428668L;
	private String situacaoTurma;
    private String tipoTurma;
    private Integer periodoLetivoTurma;
    private String digitoTurma;
    private String valorConsultaCurso;
    private List<SelectItem> listaSelectItemTurma;
    private List<SelectItem> listaSelectItemTipoTurma;
    private List<SelectItem> listaSelectItemSituacaoTurma;    

	public String getSituacaoTurma() {
		if(situacaoTurma == null) {
			situacaoTurma =  "AB";
		}
		return situacaoTurma;
	}

	public void setSituacaoTurma(String situacaoTurma) {
		this.situacaoTurma = situacaoTurma;
	}

	public String getTipoTurma() {
		return tipoTurma;
	}

	public void setTipoTurma(String tipoTurma) {
		this.tipoTurma = tipoTurma;
	}

	public Integer getPeriodoLetivoTurma() {
		return periodoLetivoTurma;
	}

	public void setPeriodoLetivoTurma(Integer periodoLetivoTurma) {
		this.periodoLetivoTurma = periodoLetivoTurma;
	}
	
	public static String identificadorTurma = "identificadorTurma";
	public static String identificador = "Identificador";
	public static String nomeCurso = "nomeCurso";
	public static String curso = "Curso";
	public static String normal = "normal";	
	public static String geral = "geral";
	public static String pratica = "pratica";
	public static String teorica = "teorica";
	public static String agrupada = "agrupada";
	public static String agrupadapratica = "agrupadapratica";
	public static String agrupadateorica = "agrupadateorica";
	public static String aberta = "AB";
	public static String fechada = "FE";

	public List<SelectItem> getListaSelectItemTurma() {
		if(listaSelectItemTurma == null) {
			listaSelectItemTurma =  new ArrayList<SelectItem>(0);
			listaSelectItemTurma.add(new SelectItem(identificadorTurma, identificador));
			listaSelectItemTurma.add(new SelectItem(nomeCurso, curso));			
		}
		return listaSelectItemTurma;
	}
	
	public List<SelectItem> getListaSelectItemTipoTurma() {
		if(listaSelectItemTipoTurma == null) {
			listaSelectItemTipoTurma =  new ArrayList<SelectItem>(0);
			listaSelectItemTipoTurma.add(new SelectItem("", ""));
			listaSelectItemTipoTurma.add(new SelectItem(normal, "Turma Base"));
			listaSelectItemTipoTurma.add(new SelectItem(geral, "Sub-Turma Geral"));			
			listaSelectItemTipoTurma.add(new SelectItem(pratica, "Sub-Turma Prática"));			
			listaSelectItemTipoTurma.add(new SelectItem(teorica, "Sub-Turma Teórica"));			
			listaSelectItemTipoTurma.add(new SelectItem(agrupada, "Turma Agrupada"));			
			listaSelectItemTipoTurma.add(new SelectItem(agrupadapratica, "Sub-Turma Agrupada Prática"));			
			listaSelectItemTipoTurma.add(new SelectItem(agrupadateorica, "Sub-Turma Agrupada Teórica"));			
		}
		return listaSelectItemTipoTurma;
	}
	public boolean isIdentificadorTurma() {
		return getCampoConsulta().isEmpty() || getCampoConsulta().equals(identificadorTurma);
	}
	public boolean isCurso() {
		return getCampoConsulta().equals(nomeCurso);
	}
	public boolean isNormal() {
		return getTipoTurma().equals(normal);
	}
	public boolean isSubTurmaGeral() {
		return getTipoTurma().equals(geral);
	}
	public boolean isSubTurmaPratica() {
		return getTipoTurma().equals(pratica);
	}
	public boolean isSubTurmaTeorica() {
		return getTipoTurma().equals(teorica);
	}
	public boolean isAgrupada() {
		return getTipoTurma().equals(agrupada);
	}
	public boolean isAgrupadaPratica() {
		return getTipoTurma().equals(agrupadapratica);
	}
	
	public boolean isAgrupadaTeorica() {
		return getTipoTurma().equals(agrupadateorica);
	}
	
	public boolean isFechada() {
		return getSituacaoTurma().equals(fechada);
	}
	public boolean isAberta() {
		return getSituacaoTurma().equals(aberta);
	}

	public List<SelectItem> getListaSelectItemSituacaoTurma() {
		if(listaSelectItemSituacaoTurma == null) {
			listaSelectItemSituacaoTurma =  new ArrayList<SelectItem>(0);
			listaSelectItemSituacaoTurma.add(new SelectItem("", ""));
			listaSelectItemSituacaoTurma.add(new SelectItem(aberta, "Aberta"));
			listaSelectItemSituacaoTurma.add(new SelectItem(fechada, "Fechada"));					
		}
		return listaSelectItemSituacaoTurma;
	}
	
	public void scroll() throws Exception {
				
	}

	public String getDigitoTurma() {
		if(digitoTurma == null) {
			digitoTurma =  "";
		}
		return digitoTurma;
	}

	public void setDigitoTurma(String digitoTurma) {
		this.digitoTurma = digitoTurma;
	}	
	
	public List<String> listaDigitoTurma;		
	public List<String> getListaDigitoTurma(){	
		if(listaDigitoTurma == null) {
			listaDigitoTurma = IntStream.rangeClosed('A', 'Z')
	            .mapToObj(i -> Character.toString ((char) i)).collect(Collectors.toList());
		
			listaDigitoTurma.add(0, "");
		}
		return listaDigitoTurma;
		
		
	}
	
	public void limparListaConsulta() {
		getListaConsulta().clear();
	}

	public String getValorConsultaCurso() {
		if(valorConsultaCurso == null) {
			valorConsultaCurso =  "";
		}
		return valorConsultaCurso;
	}

	public void setValorConsultaCurso(String valorConsultaCurso) {
		this.valorConsultaCurso = valorConsultaCurso;
	}
	
	
	
}
