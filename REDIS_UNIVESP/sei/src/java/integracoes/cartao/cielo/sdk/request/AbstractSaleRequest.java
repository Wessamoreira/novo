package integracoes.cartao.cielo.sdk.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream;

import negocio.comuns.arquitetura.ExcluirJsonStrategy;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import integracoes.cartao.cielo.Environment;
import integracoes.cartao.cielo.Merchant;

/**
 * Abstraction to reuse most of the code that send and receive the HTTP
 * messages.
 */
public abstract class AbstractSaleRequest<Request, Response> {
	final Environment environment;
	private final Merchant merchant;
	private HttpClient httpClient;

	AbstractSaleRequest(Merchant merchant, Environment environment) {
		this.merchant = merchant;
		this.environment = environment;
	}

	public abstract Response execute(Request param) throws IOException, CieloRequestException;

	public void setHttpClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	/**
	 * Send the HTTP request to Cielo with the mandatory HTTP Headers set
	 *
	 * @param request
	 *            The POST, PUT, GET request and its content is defined by the
	 *            derivations
	 * @return the HTTP response returned by Cielo
	 * @throws IOException
	 *             yeah, deal with it
	 */
	HttpResponse sendRequest(HttpUriRequest request) throws IOException {
		if (httpClient == null) {
			httpClient = HttpClientBuilder.create().build();
		}

		request.addHeader("Accept", "application/json");
		request.addHeader("Accept-Encoding", "gzip");
		request.addHeader("Content-Type", "application/json");
		request.addHeader("User-Agent", "CieloEcommerce/3.0 Android SDK");
		request.addHeader("MerchantId", merchant.getId());
		request.addHeader("MerchantKey", merchant.getKey());
		request.addHeader("RequestId", UUID.randomUUID().toString());

		return httpClient.execute(request);
	}

	/**
	 * Read the response body sent by Cielo
	 *
	 * @param response
	 *            HttpResponse by Cielo, with headers, status code, etc.
	 * @return An instance of Sale with the response entity sent by Cielo.
	 * @throws IOException
	 *             yeah, deal with it
	 * @throws CieloRequestException
	 */
	Response readResponse(HttpResponse response, Class<Response> responseClassOf)
			throws IOException, CieloRequestException {
		HttpEntity responseEntity = response.getEntity();
		InputStream responseEntityContent = responseEntity.getContent();

		Header contentEncoding = response.getFirstHeader("Content-Encoding");

		if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
			responseEntityContent = new GZIPInputStream(responseEntityContent);
		}

		BufferedReader responseReader = new BufferedReader(new InputStreamReader(responseEntityContent));
		StringBuilder responseBuilder = new StringBuilder();
		String line;

		while ((line = responseReader.readLine()) != null) {
			responseBuilder.append(line);
		}

		return parseResponse(response.getStatusLine().getStatusCode(), responseBuilder.toString(), responseClassOf);
	}

	/**
	 * Just decode the JSON into a Sale or create the exception chain to be
	 * thrown
	 *
	 * @param statusCode
	 *            The status code of response
	 * @param responseBody
	 *            The response sent by Cielo
	 * @return An instance of Sale or null
	 * @throws CieloRequestException
	 */
	private Response parseResponse(int statusCode, String responseBody, Class<Response> responseClassOf)
			throws CieloRequestException {
		Response response = null;
		Gson gson = new Gson();

		System.out.println(responseBody);

		switch (statusCode) {
		case 200:
		case 201:			
			response = new GsonBuilder().setExclusionStrategies(new ExcluirJsonStrategy()).create().fromJson(responseBody, responseClassOf);			
			break;
		case 401:
			throw new CieloRequestException("Unauthorized", new CieloError(401, "Unauthorized"), null);
		case 400:
			CieloRequestException exception = null;
			CieloError[] errors = gson.fromJson(responseBody, CieloError[].class);

			for (CieloError error : errors) {
				System.out.printf("%s: %s", "Cielo Error [" + error.getCode() + "]", error.getMessage());

				exception = new CieloRequestException(getMensagem(error, error.getMessage()), error, exception);
			}

			throw exception;		
		case 404:
			throw new CieloRequestException("Not found", new CieloError(404, "Not found"), null);
		default:
			System.out.printf("%s: %s", "Cielo", "Unknown status: " + statusCode);
		}

		return response;
	}
	
	private static final Map<Integer, String> MAP_ERROS = inicializarMapErros();

	private static Map<Integer, String> inicializarMapErros() {
		Map<Integer, String> erros = new HashMap<>();
		erros.put(307, "Transação não encontrada ou não existente no ambiente.");
		erros.put(308, "Transação não pode ser capturada. Por favor, confira os dados do cartão e tente novamente, ou entre em contato com o suporte Cielo.");
		erros.put(309, "Transação não pode ser Cancelada. Entre em contato com o suporte Cielo.");
		erros.put(310, "Comando enviado não suportado pelo meio de pagamento.");
		erros.put(311, "Cancelamento após 24 horas não liberado para o lojista.");
		erros.put(312, "Transação não permite cancelamento após 24 horas. Entre em contato com o suporte Cielo.");
		erros.put(313, "Transação recorrente não encontrada ou não disponivel no ambiente.");
		erros.put(316, "Não é permitido alterar a data da recorrência para uma data passada.");
		erros.put(319, "Recorrencia não vinculada ao cadastro do lojista.");
		return Collections.unmodifiableMap(erros);
	}

	private String getMensagem(CieloError cieloError, String mensagemPadrao) {
		return cieloError != null && MAP_ERROS.containsKey(cieloError.getCode()) ? MAP_ERROS.get(cieloError.getCode()) : mensagemPadrao;
	}
}
