package integracoes.cartao.cielo.sdk.request;

import java.io.IOException;

import negocio.comuns.arquitetura.ExcluirJsonStrategy;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;

import com.google.gson.GsonBuilder;

import integracoes.cartao.cielo.Environment;
import integracoes.cartao.cielo.Merchant;
import integracoes.cartao.cielo.sdk.Sale;

/**
 * Create any kind of sale
 */
public class CreateSaleRequest extends AbstractSaleRequest<Sale, Sale> {
	public CreateSaleRequest(Merchant merchant, Environment environment) {
		super(merchant, environment);
	}

	@Override
	public Sale execute(Sale param) throws IOException, CieloRequestException {
		String url = environment.getApiUrl() + "1/sales/";
		HttpPost request = new HttpPost(url);

		//request.setEntity(new StringEntity(new GsonBuilder().create().toJson(param)));

		//request.setEntity(new StringEntity(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(param)));
		String a  = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).create().toJson(param);
		System.out.println(a);
		request.setEntity(new StringEntity(a));
		HttpResponse response = sendRequest(request);

		return readResponse(response, Sale.class);
	}
}
