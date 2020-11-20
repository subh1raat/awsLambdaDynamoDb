package com.techprimers.serverless;
import java.util.HashMap;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDB;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
public class RegisterUserHandler implements RequestHandler<RegisterUser, RegisterUserResponse>{
	
	
	  private DynamoDB dynamoDb; 
	  AmazonDynamoDB ddb ;
	  private String DYNAMODB_TABLE_NAME = "User";
	  private Regions REGION = Regions.US_EAST_2;
	  
	 

	@Override
	public RegisterUserResponse handleRequest(RegisterUser user, Context context) {
		  	this.initDynamoDbClient();
	        persistData(user);
	        RegisterUserResponse registrationresponse = new RegisterUserResponse();
	        registrationresponse.setMessage("Saved Successfully!!!");
	        return registrationresponse;
	}

	  private void persistData(RegisterUser user) 
		      throws ConditionalCheckFailedException {
		  
		  HashMap<String,AttributeValue> item_values =
				    new HashMap<String,AttributeValue>();

				item_values.put("userId", new AttributeValue(user.getUserId()));
				item_values.put("userpassword", new AttributeValue(user.getUserpassword()));
				item_values.put("address", new AttributeValue(user.getAddress()));
			/*
			 * return ((com.amazonaws.services.dynamodbv2.document.DynamoDB)
			 * this.dynamoDb).getTable(DYNAMODB_TABLE_NAME). putItem(new
			 * PutItemSpec().withItem(new Item().withString("userId", user.getUserId())
			 * .withString("userpassword",user.getUserpassword())));
			 */
		  
		  this.ddb.putItem(DYNAMODB_TABLE_NAME, item_values);
		    }
		 
			private void initDynamoDbClient() {
				AmazonDynamoDBClient client = new AmazonDynamoDBClient();
		        client.setRegion(Region.getRegion(REGION));
		        //this.dynamoDb = new DynamoDB(client);;
		        this.ddb =AmazonDynamoDBClientBuilder.defaultClient();
		    }
}
