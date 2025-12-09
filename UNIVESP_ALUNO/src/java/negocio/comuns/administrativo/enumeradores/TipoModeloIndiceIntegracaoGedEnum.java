package negocio.comuns.administrativo.enumeradores;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import jakarta.faces.model.SelectItem;

public enum TipoModeloIndiceIntegracaoGedEnum {

	TXT_SEPARADO_PONTO_VIRGULA("exemploTxtSeparadoPontoVirgulaGed.txt", "TXT Separado Por Ponto e Virgula"),
	TXT_JSON("exemploTxtJsonGed.txt", "TXT Json");

	TipoModeloIndiceIntegracaoGedEnum(String nomeArquivo, String descricao) {
		this.nomeArquivo = nomeArquivo;
		this.descricao = descricao;
	}

	private final String nomeArquivo;
	private final String descricao;

	public String getNomeArquivo() {
		return nomeArquivo;
	}

	public String getDescricao() {
		return descricao;
	}

	public static List<SelectItem> getListaSelectItemTipoModeloIndiceIntegracaoGed() {
		return new ArrayList<>(Arrays.asList(values())).stream().map(tipoModelo -> new SelectItem(tipoModelo, tipoModelo.getDescricao())).collect(Collectors.toList());
	}
}