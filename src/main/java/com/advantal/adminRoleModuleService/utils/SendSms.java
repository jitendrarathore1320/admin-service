package com.advantal.adminRoleModuleService.utils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;


public class SendSms {

	public static Integer sendOtp(Integer oldOtp) {
		Integer otp = oldOtp;
		
		WebClient webClient = WebClient.builder()
				.clientConnector(
						new ReactorClientHttpConnector(HttpClient.create(ConnectionProvider.newConnection())))
				.build();

		// String apiResponse="Statuscode=200";
		Entity payload = Entity.json(
				"{  \"Text\": \"Sample text\",  \"Number\": \"918109285632\",  \"SenderId\": \"SMSCOU\",  \"DRNotifyUrl\": \"https://www.domainname.com/notifyurl\",  \"DRNotifyHttpMethod\": \"POST\",  \"Tool\": \"API\"}");
		String apiResponse = webClient.post().uri("https://restapi.smscountry.com/v0.1/Accounts/authKey/SMSes/")
				.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
				.header("Authorization", "Basic STYzU2U2TzVNZ2FaeFcwYWgycnogQU5kOkt4bGlqTUZZcnFoQUkwVHlucnJvbElwczZFRUFxN2lrcFFRaktTTEc=")
//				.bodyValue(payload)
				.body(Mono.just(payload), Entity.class)
				.exchangeToMono(response -> {
					return response.bodyToMono(String.class);
				}).block();
		return otp;
	}
		
//		Client client = ClientBuilder.newClient();
//		Entity payload = Entity.json(
//				"{  \"Text\": \"Sample text\",  \"Number\": \"918109285632\",  \"SenderId\": \"SMSCOU\",  \"DRNotifyUrl\": \"https://www.domainname.com/notifyurl\",  \"DRNotifyHttpMethod\": \"POST\",  \"Tool\": \"API\"}");
//		Response response = client.target("https://restapi.smscountry.com/v0.1/Accounts/authKey/SMSes/")
//				.request(MediaType.APPLICATION_JSON_TYPE)
//				.header("Basic", "MmNHa0RGRDMxVG9VRWNDYmxVUHY6UGhhVGFkaEtKc2lVNEl1NmpsNlM2dGY3RmRxV2NOZFZTaWhiNTdPYg==")
//				.post(payload);
//
//		System.out.println("status: " + response.getStatus());
//		System.out.println("headers: " + response.getHeaders());
//		System.out.println("body:" + response.readEntity(String.class));
//		return oldOtp;
//	}

//	  public static void main(String[] args) throws Exception {
//		  Client client = ClientBuilder.newClient();
//		  Entity payload = Entity.json("{  \"Text\": \"Sample text\",  \"Number\": \"918109285632\",  \"SenderId\": \"SMSCOU\",  \"DRNotifyUrl\": \"https://www.domainname.com/notifyurl\",  \"DRNotifyHttpMethod\": \"POST\",  \"Tool\": \"API\"}");
//		  Response response = client.target("https://restapi.smscountry.com/v0.1/Accounts/authKey/SMSes/")
//		    .request(MediaType.APPLICATION_JSON_TYPE)
//		    .header("Basic", "MmNHa0RGRDMxVG9VRWNDYmxVUHY6UGhhVGFkaEtKc2lVNEl1NmpsNlM2dGY3RmRxV2NOZFZTaWhiNTdPYg==")
//		    .post(payload);
//
//		  System.out.println("status: " + response.getStatus());
//		  System.out.println("headers: " + response.getHeaders());
//		  System.out.println("body:" + response.readEntity(String.class));
//	}

}
