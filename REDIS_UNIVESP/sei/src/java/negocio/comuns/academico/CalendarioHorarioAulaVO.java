/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio.comuns.academico;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import negocio.comuns.academico.enumeradores.MesAnoEnum;
import negocio.comuns.basico.PessoaEmailInstitucionalVO;
import negocio.comuns.contabil.ConfiguracaoContabilRegraPlanoContaVO;
import negocio.comuns.gsuite.PessoaGsuiteVO;
import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.dominios.DiaSemana;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import webservice.servicos.objetos.DataEventosRSVO;

/**
 *
 * @author Otimize-Not
 */
public class CalendarioHorarioAulaVO<T> implements Serializable {

    public MesAnoEnum mesAno;
    private String ano;
    private List<T> calendarioHorarioAulaDomingo;
    private List<T> calendarioHorarioAulaSegunda;
    private List<T> calendarioHorarioAulaTerca;
    private List<T> calendarioHorarioAulaQuarta;
    private List<T> calendarioHorarioAulaQuinta;
    private List<T> calendarioHorarioAulaSexta;
    private List<T> calendarioHorarioAulaSabado;
    private Object objetoSelecionado;
    private List<PessoaGsuiteVO> listaPessoaGsuiteVO;
    private List<PessoaEmailInstitucionalVO> listaPessoaEmailInstitucionalVO;
    private static final long serialVersionUID = 1L;

    public String getCampoOrdenacao() {
        return getAno() + getMesAno().getKey();
    }

    public String getTituloCalendario() {
        return getMesAno().getMes() + "/" + getAno();
    }

    public String getTituloCalendarioAbreviado() {
        return getMesAno().getMesAbreviado() + "/" + (getAno().isEmpty()?"":getAno().substring(2, getAno().length()));
    }
    
    private String mesAnoAnterior; 
    public String getMesAnoAnterior(){
       if(mesAnoAnterior == null){
           mesAnoAnterior = getMesAno().getMesAnoAnterior(getAno());
       }
       return mesAnoAnterior;
    }
    
    private String mesAnoAnteriorAbreviado; 
    public String getMesAnoAnteriorAbreviado(){
       if(mesAnoAnteriorAbreviado == null){
           mesAnoAnteriorAbreviado = getMesAno().getMesAnoAnteriorAbreviado(getAno());
       }
       return mesAnoAnteriorAbreviado;
    }
    
    private String mesAnoPosterior;
    public String getMesAnoPosterior(){
        if(mesAnoPosterior == null){
            mesAnoPosterior = getMesAno().getMesAnoPosterior(getAno());
        }
        return mesAnoPosterior;
    }
    
    private String mesAnoPosteriorAbreviado;
    public String getMesPosteriorAbreviado(){
        if(mesAnoPosteriorAbreviado == null){
            mesAnoPosteriorAbreviado = getMesAno().getMesAnoPosteriorAbreviado(getAno());
        }
        return mesAnoPosteriorAbreviado;
    }
    

    @Override
    public boolean equals(Object objComparado) {
        if (objComparado instanceof CalendarioHorarioAulaVO) {
            if (((CalendarioHorarioAulaVO) (objComparado)).getAno().equals(getAno())
                    && ((CalendarioHorarioAulaVO) (objComparado)).getMesAno().equals(getMesAno())) {
                return true;
            }
        }
        return false;

    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 37 * hash + (this.mesAno != null ? this.mesAno.hashCode() : 0);
        hash = 37 * hash + (this.ano != null ? this.ano.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return getTituloCalendario();
    }

    public String getAno() {
        if (ano == null) {
            ano = "";
        }
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public List<T> consultarListaCalendarioPorDiaSemana(DiaSemana diaSemana) {
        switch (diaSemana) {
            case DOMINGO:
                return getCalendarioHorarioAulaDomingo();
            case SEGUNGA:
                return getCalendarioHorarioAulaSegunda();
            case TERCA:
                return getCalendarioHorarioAulaTerca();
            case QUARTA:
                return getCalendarioHorarioAulaQuarta();
            case QUINTA:
                return getCalendarioHorarioAulaQuinta();
            case SEXTA:
                return getCalendarioHorarioAulaSexta();
            case SABADO:
                return getCalendarioHorarioAulaSabado();
            default:
                return null;
        }
    }
    
    public void adicionarItemListaCalendarioPorDiaSemana(T itemListaCalendario, DiaSemana diaSemana) {
        consultarListaCalendarioPorDiaSemana(diaSemana).add(itemListaCalendario);
    }
    
    public void atualizarItemListaCalendarioPorDiaSemana(T itemListaCalendario, DiaSemana diaSemana) {
    	List<T> lista = consultarListaCalendarioPorDiaSemana(diaSemana);
    	int index = 0;
    	for (T t : lista) {
    		if(t.equals(itemListaCalendario)) {
    			lista.set(index, itemListaCalendario);
    		}
    		index++;
		}
    	lista.add(itemListaCalendario);
    }

    public void adicionarListaCalendarioPorDiaSemana(List<T> listaCalendario, DiaSemana diaSemana) {
        switch (diaSemana) {
            case DOMINGO: {
                setCalendarioHorarioAulaDomingo(listaCalendario);
                break;
            }
            case SEGUNGA: {
                setCalendarioHorarioAulaSegunda(listaCalendario);
                break;
            }
            case TERCA: {
                setCalendarioHorarioAulaTerca(listaCalendario);
                break;
            }
            case QUARTA: {
                setCalendarioHorarioAulaQuarta(listaCalendario);
                break;
            }
            case QUINTA: {
                setCalendarioHorarioAulaQuinta(listaCalendario);
                break;
            }
            case SEXTA: {
                setCalendarioHorarioAulaSexta(listaCalendario);
                break;
            }
            case SABADO: {
                setCalendarioHorarioAulaSabado(listaCalendario);
                break;
            }

        }
    }
    
    
    public DataEventosRSVO executarMontagemDataEventoRSVOLivre(int dia, MesAnoEnum mesAno, Integer ano) throws ParseException {
		if (dia <= 9) {
			return new DataEventosRSVO(Uteis.getData("0" + dia + "/" + mesAno.getKey() + "/" + ano, "dd/MM/yyyy"), "#ffffff", "#000000", "colunaHorarioLivre");
		} else {
			return new DataEventosRSVO(Uteis.getData(dia + "/" + mesAno.getKey() + "/" + ano, "dd/MM/yyyy"), "#ffffff", "#000000", "colunaHorarioLivre");
		}
	}
	
    public void executarMontagemSemanaInicialCalendarioHorarioAulaDataEventoRSVO(CalendarioHorarioAulaVO<DataEventosRSVO> calendarioHorarioAula, Date data) {
		DiaSemana diaSemanaInicial = Uteis.getDiaSemanaEnum(data);
		switch (diaSemanaInicial) {
            case DOMINGO:
                break;
            case SEGUNGA:
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 1), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.DOMINGO);
                break;
            case TERCA:
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 1), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SEGUNGA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 2), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.DOMINGO);
                break;
            case QUARTA:
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 1), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.TERCA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 2), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SEGUNGA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 3), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.DOMINGO);
                break;
            case QUINTA:
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 1), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.QUARTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 2), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.TERCA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 3), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SEGUNGA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 4), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.DOMINGO);
                break;                
            case SEXTA:
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 1), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.QUINTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 2), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.QUARTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 3), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.TERCA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 4), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SEGUNGA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 5), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.DOMINGO);
                break;
            case SABADO:
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 1), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SEXTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 2), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.QUINTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 3), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.QUARTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 4), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.TERCA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 5), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SEGUNGA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAntiga(data, 6), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.DOMINGO);
                break;
            default:
                break;
        }
	}
	
	public void executarMontagemSemanaFinalCalendarioHorarioAulaDataEventoRSVO(CalendarioHorarioAulaVO<DataEventosRSVO> calendarioHorarioAula, Date data) {
		DiaSemana diaSemanaFinal = Uteis.getDiaSemanaEnum(data);
		switch (diaSemanaFinal) {
            case DOMINGO:
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 1), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SEGUNGA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 2), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.TERCA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 3), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.QUARTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 4), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.QUINTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 5), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SEXTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 6), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SABADO);
                break;
            case SEGUNGA:
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 1), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.TERCA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 2), "#b8b8b8","#000000", "colunaOutroMes"), DiaSemana.QUARTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 3), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.QUINTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 4), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SEXTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 5), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SABADO);
                break;
            case TERCA:
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 1), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.QUARTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 2), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.QUINTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 3), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SEXTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 4), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SABADO);
                break;
            case QUARTA:
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 1), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.QUINTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 2), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SEXTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 3), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SABADO);
                break;
            case QUINTA:
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 1), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SEXTA);
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 2), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SABADO);
                break;                
            case SEXTA:
                calendarioHorarioAula.adicionarItemListaCalendarioPorDiaSemana(new DataEventosRSVO(Uteis.obterDataAvancada(data, 1), "#b8b8b8", "#000000", "colunaOutroMes"), DiaSemana.SABADO);
                break;
            case SABADO:                
                break;
            default:
                break;
        }
	}
    
    

    public List<T> getCalendarioHorarioAulaDomingo() {
        if (calendarioHorarioAulaDomingo == null) {
            calendarioHorarioAulaDomingo = new ArrayList<T>();
        }
        return calendarioHorarioAulaDomingo;
    }

    public void setCalendarioHorarioAulaDomingo(List<T> calendarioHorarioAulaDomingo) {
        this.calendarioHorarioAulaDomingo = calendarioHorarioAulaDomingo;
    }

    public List<T> getCalendarioHorarioAulaQuarta() {
        if (calendarioHorarioAulaQuarta == null) {
            calendarioHorarioAulaQuarta = new ArrayList<T>();
        }
        return calendarioHorarioAulaQuarta;
    }

    public void setCalendarioHorarioAulaQuarta(List<T> calendarioHorarioAulaQuarta) {
        this.calendarioHorarioAulaQuarta = calendarioHorarioAulaQuarta;
    }

    public List<T> getCalendarioHorarioAulaQuinta() {
        if (calendarioHorarioAulaQuinta == null) {
            calendarioHorarioAulaQuinta = new ArrayList<T>();
        }
        return calendarioHorarioAulaQuinta;
    }

    public void setCalendarioHorarioAulaQuinta(List<T> calendarioHorarioAulaQuinta) {
        this.calendarioHorarioAulaQuinta = calendarioHorarioAulaQuinta;
    }

    public List<T> getCalendarioHorarioAulaSabado() {
        if (calendarioHorarioAulaSabado == null) {
            calendarioHorarioAulaSabado = new ArrayList<T>();
        }
        return calendarioHorarioAulaSabado;
    }

    public void setCalendarioHorarioAulaSabado(List<T> calendarioHorarioAulaSabado) {
        this.calendarioHorarioAulaSabado = calendarioHorarioAulaSabado;
    }

    public List<T> getCalendarioHorarioAulaSegunda() {
        if (calendarioHorarioAulaSegunda == null) {
            calendarioHorarioAulaSegunda = new ArrayList<T>();
        }
        return calendarioHorarioAulaSegunda;
    }

    public void setCalendarioHorarioAulaSegunda(List<T> calendarioHorarioAulaSegunda) {
        this.calendarioHorarioAulaSegunda = calendarioHorarioAulaSegunda;
    }

    public List<T> getCalendarioHorarioAulaSexta() {
        if (calendarioHorarioAulaSexta == null) {
            calendarioHorarioAulaSexta = new ArrayList<T>();
        }
        return calendarioHorarioAulaSexta;
    }

    public void setCalendarioHorarioAulaSexta(List<T> calendarioHorarioAulaSexta) {
        this.calendarioHorarioAulaSexta = calendarioHorarioAulaSexta;
    }

    public JRBeanArrayDataSource getCalendarioHorarioAulaTercaJR() {
        return new JRBeanArrayDataSource(getCalendarioHorarioAulaTerca().toArray());
    }

    public JRBeanArrayDataSource getCalendarioHorarioAulaSegundaJR() {
        return new JRBeanArrayDataSource(getCalendarioHorarioAulaSegunda().toArray());
    }

    public JRBeanArrayDataSource getCalendarioHorarioAulaDomingoJR() {
        return new JRBeanArrayDataSource(getCalendarioHorarioAulaDomingo().toArray());
    }

    public JRBeanArrayDataSource getCalendarioHorarioAulaQuartaJR() {
        return new JRBeanArrayDataSource(getCalendarioHorarioAulaQuarta().toArray());
    }

    public JRBeanArrayDataSource getCalendarioHorarioAulaQuintaJR() {
        return new JRBeanArrayDataSource(getCalendarioHorarioAulaQuinta().toArray());
    }

    public JRBeanArrayDataSource getCalendarioHorarioAulaSextaJR() {
        return new JRBeanArrayDataSource(getCalendarioHorarioAulaSexta().toArray());
    }

    public JRBeanArrayDataSource getCalendarioHorarioAulaSabadoJR() {
        return new JRBeanArrayDataSource(getCalendarioHorarioAulaSabado().toArray());
    }

    public List<T> getCalendarioHorarioAulaTerca() {
        if (calendarioHorarioAulaTerca == null) {
            calendarioHorarioAulaTerca = new ArrayList<T>();
        }
        return calendarioHorarioAulaTerca;
    }

    public void setCalendarioHorarioAulaTerca(List<T> calendarioHorarioAulaTerca) {
        this.calendarioHorarioAulaTerca = calendarioHorarioAulaTerca;
    }

    public MesAnoEnum getMesAno() {
        if (mesAno == null) {
            mesAno = MesAnoEnum.JANEIRO;
        }
        return mesAno;
    }
    
    public void setMesAno(MesAnoEnum mesAno) {
        this.mesAno = mesAno;
    }

	public Object getObjetoSelecionado() {
		return objetoSelecionado;
	}

	public void setObjetoSelecionado(Object objetoSelecionado) {
		this.objetoSelecionado = objetoSelecionado;
	}

	public List<PessoaGsuiteVO> getListaPessoaGsuiteVO() {
		if (listaPessoaGsuiteVO == null) {
			listaPessoaGsuiteVO = new ArrayList<PessoaGsuiteVO>();
		}
		return listaPessoaGsuiteVO;
	}

	public void setListaPessoaGsuiteVO(List<PessoaGsuiteVO> listaPessoaGsuiteVO) {
		this.listaPessoaGsuiteVO = listaPessoaGsuiteVO;
	}
	
	public String getListaPessoaGsuiteVO_ApresentarHtml() {
		StringBuilder sb = new StringBuilder("");
		if (Uteis.isAtributoPreenchido(getListaPessoaGsuiteVO())) {
			sb.append("<table cellspacing=0 cellpadding=1 style=\"width:100%\">");
			sb.append("<tr><th style=\"text-align:left;border:none;\">Conta Google Gsuite</th></tr>");
			for (PessoaGsuiteVO pessoaGsuite : getListaPessoaGsuiteVO()) {
				sb.append("<tr><td style=\"width:100%\">").append(pessoaGsuite.getEmail()).append("</td></tr>");
			}
			sb.append("</table>");
		} 
		return sb.toString();
	}

	public List<PessoaEmailInstitucionalVO> getListaPessoaEmailInstitucionalVO() {
		if (listaPessoaEmailInstitucionalVO == null) {
			listaPessoaEmailInstitucionalVO = new ArrayList<>();
		}
		return listaPessoaEmailInstitucionalVO;
	}

	public void setListaPessoaEmailInstitucionalVO(List<PessoaEmailInstitucionalVO> listaPessoaEmailInstitucionalVO) {
		this.listaPessoaEmailInstitucionalVO = listaPessoaEmailInstitucionalVO;
	}
	
	public String getListaPessoaEmailInstitucionalVO_ApresentarHtml() {
		StringBuilder sb = new StringBuilder("");
		if (Uteis.isAtributoPreenchido(getListaPessoaEmailInstitucionalVO())) {
			sb.append("<table cellspacing=0 cellpadding=1 style=\"width:100%\">");
			sb.append("<tr><th style=\"text-align:left;border:none;\">Conta Email Institucional</th></tr>");
			for (PessoaEmailInstitucionalVO pei : getListaPessoaEmailInstitucionalVO()) {
				sb.append("<tr><td style=\"width:100%\">").append(pei.getEmail()).append("</td></tr>");
			}
			sb.append("</table>");
		} 
		return sb.toString();
	}
	
	
	
	
    
    
    
    
}
