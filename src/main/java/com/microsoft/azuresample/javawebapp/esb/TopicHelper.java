package com.microsoft.azuresample.javawebapp.esb;

import com.google.gson.Gson;
import com.microsoft.azuresample.javawebapp.model.ToDo;
import com.microsoft.windowsazure.Configuration;
import com.microsoft.windowsazure.exception.ServiceException;
import com.microsoft.windowsazure.services.servicebus.ServiceBusConfiguration;
import com.microsoft.windowsazure.services.servicebus.ServiceBusContract;
import com.microsoft.windowsazure.services.servicebus.ServiceBusService;
import com.microsoft.windowsazure.services.servicebus.models.BrokeredMessage;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * Created by vazvadsk on 2017-01-05.
 */

@Component
public class TopicHelper {
    private static final Gson gson = new Gson();

    private static String SBUS_NAME = "";
    private static String SBUS_KEY = "";

    @PostConstruct
    public void init(){
        System.out.println("### INIT of TopicHelper called.");

        Map<String, String> env = System.getenv();
        SBUS_NAME = env.get("SBUS_NAME");
        SBUS_KEY = env.get("SBUS_KEY");
    }

    public void sendToDo(ToDo itm){
        try{
            Configuration config =
                    ServiceBusConfiguration.configureWithSASAuthentication(
                            // TODO: provide valid Service Bus name
                            SBUS_NAME,
                            "RootManageSharedAccessKey",
                            // TODO: provide valid KEY for Service Bus
                            SBUS_KEY,
                            ".servicebus.windows.net"
                    );

            ServiceBusContract service = ServiceBusService.create(config);
            try {
                System.out.println("### topic: sending message...: " + gson.toJson(itm));

                //Create topic message
                BrokeredMessage message = new BrokeredMessage(gson.toJson(itm));
                //Append category information to message (or any other property
                message.setProperty("Category", itm.getCategory());
                //send message to topic
                // TODO: provide valid Topic name
                service.sendTopicMessage("todotopic", message);
            } catch (ServiceException e) {
                System.out.println("### topic: error sending topic");
                e.printStackTrace();
            }
        }catch (Exception ex){
            System.out.println("### topic: error configuring service bus");
            ex.printStackTrace();
        }
    }
}