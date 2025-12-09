package negocio.comuns.utilitarias.dominios;

import static negocio.comuns.utilitarias.dominios.TipoNivelEducacional.IS_BASICO;
import static negocio.comuns.utilitarias.dominios.TipoNivelEducacional.IS_EXTENSAO;
import static negocio.comuns.utilitarias.dominios.TipoNivelEducacional.IS_SEQUENCIAL;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Stream.of;

import javax.faces.model.SelectItem;

import negocio.comuns.utilitarias.Uteis;
import negocio.comuns.utilitarias.UtilPropriedadesDoEnum;

/**
 *
 * @author Diego
 */
public enum NivelFormacaoAcademica {
	INFANTIL("IN", "Educação Infantil", 0, "", new TipoNivelEducacional[]{TipoNivelEducacional.INFANTIL}),
    FUNDAMENTAL("EF", "Ensino Fundamental", 1, "" , new TipoNivelEducacional[]{TipoNivelEducacional.BASICO} ),
    MEDIO("EM", "Ensino Médio", 2, "" , new TipoNivelEducacional[]{TipoNivelEducacional.MEDIO}),
    TECNICO("TE", "Técnico/Profissionalizante", 3, "" , new TipoNivelEducacional[]{TipoNivelEducacional.PROFISSIONALIZANTE}),
    EXTENSAO("EX", "Extensão", 3, "Ex" , new TipoNivelEducacional[]{TipoNivelEducacional.EXTENSAO} ),
    GRADUACAO("GR", "Graduação", 4, "Gr" , new TipoNivelEducacional[]{TipoNivelEducacional.SUPERIOR, TipoNivelEducacional.GRADUACAO_TECNOLOGICA}),
    ESPECIALIZACAO("EP", "Especialização", 5, "Esp" , null),
    POS_GRADUACAO("PO", "Pós-graduação", 6, "Pós-graduação" , new TipoNivelEducacional[]{TipoNivelEducacional.POS_GRADUACAO}),
    MESTRADO("MS", "Mestrado", 7, "Ms" , new TipoNivelEducacional[]{TipoNivelEducacional.MESTRADO}),
    DOUTORADO("DR", "Doutorado", 8, "Dr" , null),
    POS_DOUTORADO("PD", "Pós-Doutorado", 9, "PhD" , null);
    private final String valor;
    private final String descricao;
    private final int nivel;
    private final String sigla;
    private final TipoNivelEducacional[] tipoNivelEducacional;

    NivelFormacaoAcademica(String valor, String descricao, int nivel, String sigla ,TipoNivelEducacional[] tipoNivelEducacional) {
        this.valor = valor;
        this.descricao = descricao;
        this.nivel = nivel;
        this.sigla = sigla;
        this.tipoNivelEducacional = tipoNivelEducacional;
    }

    public static NivelFormacaoAcademica getEnum(String valor) {
        NivelFormacaoAcademica[] valores = values();
        for (NivelFormacaoAcademica obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getDescricao(String valor) {
        NivelFormacaoAcademica obj = getEnum(valor);
        if (obj != null) {
            return obj.getDescricao();
        }
        return valor;
    }

    public static NivelFormacaoAcademica getEnumPorNivel(Integer nivel) {
        NivelFormacaoAcademica[] valores = values();
        for (NivelFormacaoAcademica obj : valores) {
            if (obj.getNivel() == nivel.intValue()) {
                return obj;
            }
        }
        return null;
    }
    
    public static NivelFormacaoAcademica getEnumPorValor(String valor) {
        NivelFormacaoAcademica[] valores = values();
        for (NivelFormacaoAcademica obj : valores) {
            if (obj.getValor().equals(valor)) {
                return obj;
            }
        }
        return null;
    }

    public static String getValorPorNivel(Integer nivel) {
        NivelFormacaoAcademica obj = getEnumPorNivel(nivel);
        if (obj != null) {
            return obj.getValor();
        }
        return "";
    }

    public static String getDescricaoPorNivel(Integer nivel) {
        NivelFormacaoAcademica obj = getEnumPorNivel(nivel);
        if (obj != null) {
            return obj.getDescricao();
        }
        return "";
    }

    public static Integer getSiglaPorNivel(String valor) {
        NivelFormacaoAcademica obj = getEnum(valor);
        if (obj != null) {
            return obj.getNivel();
        }
        return 0;
    }
    
    public static String getNivelporSigla(Integer nivel) {
        NivelFormacaoAcademica obj = getEnumPorNivel(nivel);
        if (obj != null) {
            return obj.getSigla();
        }
        return "";
    }
    
    public static NivelFormacaoAcademica getEnumPorValorTipoNivelEducacional(String valor) {
    	NivelFormacaoAcademica[] valores = values();
        for (NivelFormacaoAcademica obj : valores) {
        	if(obj.getTipoNivelEducacional() != null) {
            for (TipoNivelEducacional tipoNivelEducacional : obj.getTipoNivelEducacional()) {
            	if(tipoNivelEducacional.getValor().equals(valor)) {
            		return obj;
            	}
            }
        	}
        }
        return null;
    }

    public String getValor() {
        return valor;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getNivel() {
        return nivel;
    }

    /**
     * @return the sigla
     */
    public String getSigla() {
        return sigla;
    }

	public TipoNivelEducacional[] getTipoNivelEducacional() {
		return tipoNivelEducacional;
	}

	public static List<SelectItem> getListaSelectItemTituloPorValorNivelFormacaoAcademica(String valor) {
		NivelFormacaoAcademica nivelFormacaoAcademica = getEnumPorValor(valor);
		if (Uteis.isAtributoPreenchido(nivelFormacaoAcademica) 
				&& nivelFormacaoAcademica != null 
				&& nivelFormacaoAcademica.getTipoNivelEducacional() != null 
				&& of(nivelFormacaoAcademica.getTipoNivelEducacional()).noneMatch(IS_BASICO.or(IS_EXTENSAO).or(IS_SEQUENCIAL))) {
			List<SelectItem> opcoes = new ArrayList<>(0);
			opcoes.add(new SelectItem("", ""));
			if (of(nivelFormacaoAcademica.getTipoNivelEducacional()).anyMatch(TipoNivelEducacional.PROFISSIONALIZANTE::equals)) {
				opcoes.add(new SelectItem(TituloCursoSuperior.TECNICO.getValor(), TituloCursoSuperior.TECNICO.getDescricao()));
			} else if (of(nivelFormacaoAcademica.getTipoNivelEducacional()).anyMatch(TipoNivelEducacional.MEDIO::equals)) {
				opcoes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TituloCursoMedio.class, true);
			} else if (of(nivelFormacaoAcademica.getTipoNivelEducacional()).anyMatch(TipoNivelEducacional.SUPERIOR::equals)) {
				opcoes.addAll(montaListaExcluindoValor(TituloCursoSuperior.TECNICO));
			} else if (of(nivelFormacaoAcademica.getTipoNivelEducacional()).anyMatch(TipoNivelEducacional.POS_GRADUACAO::equals)) {
				opcoes.add(new SelectItem(TituloCursoPos.LATO_SENSU.getValor(), TituloCursoPos.LATO_SENSU.getDescricao()));
				opcoes.add(new SelectItem(TituloCursoPos.RESIDENCIA_MEDICA.getValor(),TituloCursoPos.RESIDENCIA_MEDICA.getDescricao()));
				opcoes.add(new SelectItem(TituloCursoPos.STRICTO_SENSU.getValor(), TituloCursoPos.STRICTO_SENSU.getDescricao()));
			} else if (of(nivelFormacaoAcademica.getTipoNivelEducacional()).anyMatch(TipoNivelEducacional.MESTRADO::equals)) {
				opcoes.add(new SelectItem(TituloCursoPos.STRICTO_SENSU.getValor(), TituloCursoPos.STRICTO_SENSU.getDescricao()));
			} else if (of(nivelFormacaoAcademica.getTipoNivelEducacional()).anyMatch(TipoNivelEducacional.GRADUACAO_TECNOLOGICA::equals)) {
				opcoes = UtilPropriedadesDoEnum.getListaSelectItemDoEnum(TituloCursoSuperior.class, true);
			}
			return opcoes;
		}
		return new ArrayList<>(0);
	}

	private static List<SelectItem> montaListaExcluindoValor(TituloCursoSuperior tituloCursoSuperiorExcluir) {
		Predicate<TituloCursoSuperior> isExcluir = tituloCursoSuperiorExcluir::equals;
		return of(TituloCursoSuperior.values()).filter(isExcluir.negate())
				.map(t -> new SelectItem(t.getValor(), t.getDescricao())).collect(Collectors.toCollection(ArrayList::new));
	}
}
