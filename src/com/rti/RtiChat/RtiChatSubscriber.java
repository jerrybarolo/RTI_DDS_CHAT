package com.rti.RtiChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.topic.Topic;

public class RtiChatSubscriber {
	
	private DomainParticipant _participant;
	private RtiChatDataReader _dataReader;

	//public static void main(String[] args) {
		// TODO Auto-generated method stub
	
	public RtiChatDataReader getDataReader(){
		return _dataReader;
	}
	
	public RtiChatSubscriber(){
	
		_participant = 
                DomainParticipantFactory.get_instance().create_participant(
                    0,
                    DomainParticipantFactory.PARTICIPANT_QOS_DEFAULT,
                    null,       // listener
                    StatusKind.STATUS_MASK_NONE);
		
		if (_participant == null) {
		    System.err.println("! Unable to create DDS domain participant");
		    return;
		}
		
		RtiChatTypeSupport.register_type(_participant, 
				RtiChatTypeSupport.get_type_name());
		
		Topic topic = _participant.create_topic(
                "RtiChat_Topic",
                RtiChatTypeSupport.get_type_name(), 
                DomainParticipant.TOPIC_QOS_DEFAULT,
                null,   // listener
                StatusKind.STATUS_MASK_NONE);
		
		if (topic == null) {
		    System.err.println("! Unable to create topic " + "RtiChat_Topic");
		    return;
		}
		
		Subscriber subscriber = _participant.create_subscriber(
                DomainParticipant.SUBSCRIBER_QOS_DEFAULT, 
                null,           // listener
                StatusKind.STATUS_MASK_NONE);
		
		if (subscriber == null) {
		    System.err.println("! Unable to create DDS Subscriber");
		    throw new RuntimeException("HelloSubscriber creation failed");
		}     

		
		//RtiChatListener listener = new RtiChatListener();

		_dataReader = (RtiChatDataReader)
            subscriber.create_datareader(
            topic, 
            Subscriber.DATAREADER_QOS_DEFAULT, 
            null,
            StatusKind.STATUS_MASK_ALL);
		
		if (_dataReader == null) {
			System.err.println("! Unable to create DDS Data Reader");
			throw new RuntimeException("HelloSubscriber creation failed");
		}
		
		
//		System.out.println("-- Press enter to exit");
//		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
//		try {
//			br.readLine();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}			 
	
		
		}
        
		public void close(){
			/* Shutdown, when sampleCount is finite */
	        if(_participant != null) {
	        	_participant.delete_contained_entities();
	
	            DomainParticipantFactory.TheParticipantFactory.delete_participant(_participant);
	            DomainParticipantFactory.finalize_instance();
	        }	
		}
}
