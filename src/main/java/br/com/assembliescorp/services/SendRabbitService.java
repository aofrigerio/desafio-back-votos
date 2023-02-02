package br.com.assembliescorp.services;

public interface SendRabbitService {
	
	/**
	 * Envia para o rabbit o resultado
	 * @param object Json qualquer
	 */
	void sendResultToRabbit(Object object);

}
