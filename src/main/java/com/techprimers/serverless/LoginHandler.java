package com.techprimers.serverless;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.GetItemRequest;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LoginHandler implements RequestHandler<LoginRequest,LoginResponse>{

	private DynamoDB dynamoDb; 
	private String DYNAMODB_TABLE_NAME = "User";
	private Regions REGION = Regions.US_EAST_2;
	private AmazonDynamoDB ddb ;
	GetItemRequest request = null;

	@Override
	public LoginResponse handleRequest(LoginRequest login, Context context) {
		// TODO Auto-generated method stub
		this.initDynamoDbClient();
		LoginResponse response = new LoginResponse();
		Map<String,AttributeValue> returned_item =ValidateUser(login);
		 if (returned_item != null) {
		        Set<String> keys = returned_item.keySet();
		        for (String key : keys) {
		            System.out.format("%s: %s\n",
		                    key, returned_item.get(key).toString());
		            response.setMessage("Login successful"+"welcome User: "+login.getUserId());
		        }
		    } else {
		        System.out.format("No item found with the key %s!\n", login.getUserId());
		        response.setMessage("Invalid credentials");
		    }
		
		return response;
	}

	private Map<String, AttributeValue>  ValidateUser(LoginRequest login) 
			throws ConditionalCheckFailedException {
		/*
		 * return ((com.amazonaws.services.dynamodbv2.document.DynamoDB)
		 * this.dynamoDb).getTable(DYNAMODB_TABLE_NAME). putItem(new
		 * PutItemSpec().withItem(new Item().withString("username", login.getUserName())
		 * .withString("password",login.getPassword())));
		 */
		HashMap<String,AttributeValue> key_to_get =
				new HashMap<String,AttributeValue>();
		key_to_get.put("userId",  new AttributeValue(login.getUserId()));

		this.request =new GetItemRequest()
				.withKey(key_to_get)
				.withTableName(DYNAMODB_TABLE_NAME);
		Map<String,AttributeValue> returned_item =
				ddb.getItem(this.request).getItem();
		return returned_item;

	}

	private void initDynamoDbClient() {
		AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		client.setRegion(Region.getRegion(REGION));
//		this.dynamoDb = (DynamoDB) new com.amazonaws.services.dynamodbv2.document.DynamoDB(client);
		this.ddb =AmazonDynamoDBClientBuilder.defaultClient();
	}
}
