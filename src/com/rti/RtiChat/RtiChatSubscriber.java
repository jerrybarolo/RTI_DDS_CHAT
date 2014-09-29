package com.rti.RtiChat;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.DurabilityQosPolicyKind;
import com.rti.dds.infrastructure.HistoryQosPolicyKind;
import com.rti.dds.infrastructure.ReliabilityQosPolicyKind;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.subscription.DataReaderQos;
import com.rti.dds.subscription.Subscriber;
import com.rti.dds.topic.Topic;
import com.rti.dds.topic.TopicQos;

public class RtiChatSubscriber {
	
	private DomainParticipant _participant;
	private RtiChatDataReader _dataReader;
	
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
		
		TopicQos topic_qos = new TopicQos();
		_participant.get_default_topic_qos(topic_qos);
		        
		topic_qos.history.kind    = HistoryQosPolicyKind.KEEP_ALL_HISTORY_QOS;
		topic_qos.durability.kind = DurabilityQosPolicyKind.TRANSIENT_LOCAL_DURABILITY_QOS;
		topic_qos.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;
		
		Topic topic = _participant.create_topic(
                "RtiChat_Topic",
                RtiChatTypeSupport.get_type_name(), 
                topic_qos,
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

		
		DataReaderQos datareader_qos = new DataReaderQos();
		subscriber.get_default_datareader_qos(datareader_qos);
		       
        datareader_qos.history.kind    = HistoryQosPolicyKind.KEEP_ALL_HISTORY_QOS;
        datareader_qos.durability.kind = DurabilityQosPolicyKind.TRANSIENT_LOCAL_DURABILITY_QOS;
        datareader_qos.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;

		_dataReader = (RtiChatDataReader)
            subscriber.create_datareader(
            topic, 
            datareader_qos, 
            null,
            StatusKind.STATUS_MASK_ALL);
		
		if (_dataReader == null) {
			System.err.println("! Unable to create DDS Data Reader");
			throw new RuntimeException("HelloSubscriber creation failed");
		}		
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
