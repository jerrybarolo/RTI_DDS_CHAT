package com.rti.RtiChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.RETCODE_ERROR;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.Publisher;
import com.rti.dds.topic.Topic;

public class RtiChatPublisher {
	
	private DomainParticipant _participant;
	private RtiChatDataWriter _dataWriter;

	//public static void main(String[] args) {
		
	public RtiChatPublisher(){
		// Create the DDS Domain participant on domain ID 0
		_participant = DomainParticipantFactory.get_instance().create_participant(
                0, // Domain ID = 0
                DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT, 
                null, // listener
                StatusKind.STATUS_MASK_NONE);
        if (_participant == null) {
            System.err.println("Unable to create domain participant");
            return;
        }
        
        RtiChatTypeSupport.register_type(_participant, 
				RtiChatTypeSupport.get_type_name());

        // Create the topic "RtiChat_Topic" for the String type
        Topic topic = _participant.create_topic(
                "RtiChat_Topic", 
                RtiChatTypeSupport.get_type_name(), 
                DomainParticipant.TOPIC_QOS_DEFAULT, 
                null, // listener
                StatusKind.STATUS_MASK_NONE);
        if (topic == null) {
            System.err.println("Unable to create topic.");
            return;
        }
        
     // Create the data writer using the default publisher
        _dataWriter =
            (RtiChatDataWriter) _participant.create_datawriter(
                topic, 
                Publisher.DATAWRITER_QOS_DEFAULT,
                null, // listener
                StatusKind.STATUS_MASK_NONE);
        if (_dataWriter == null) {
            System.err.println("Unable to create data writer\n");
            return;
        }
	}
        
        //System.out.println("Ready to write data.");
        //System.out.println("When the subscriber is ready, you can start writing.");
        //System.out.print("Press CTRL+C to terminate or enter an empty line to do a clean shutdown.\n\n");

	void sendMessage(String msg)
	{	
        //BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            //while (true) {
            //System.out.print("Please type a message> ");

            RtiChat toWrite = new RtiChat();
            toWrite.sender = "Gerardo Fiorletta";
            toWrite.message = msg;

            _dataWriter.write(toWrite, InstanceHandle_t.HANDLE_NIL);
            //}
        //} catch (IOException e) {
            // This exception can be thrown from the BufferedReader class
          //  e.printStackTrace();
        } catch (RETCODE_ERROR e) {
            // This exception can be thrown from DDS write operation
            e.printStackTrace();
        }
	}
	
	void close()
	{
        System.out.println("Exiting...");
        _participant.delete_contained_entities();
        DomainParticipantFactory.get_instance().delete_participant(_participant);
	}
	//}

}
