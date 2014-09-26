package com.rti.RtiChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.rti.dds.domain.DomainParticipant;
import com.rti.dds.domain.DomainParticipantFactory;
import com.rti.dds.infrastructure.DurabilityQosPolicyKind;
import com.rti.dds.infrastructure.HistoryQosPolicyKind;
import com.rti.dds.infrastructure.InstanceHandle_t;
import com.rti.dds.infrastructure.RETCODE_ERROR;
import com.rti.dds.infrastructure.ReliabilityQosPolicyKind;
import com.rti.dds.infrastructure.StatusKind;
import com.rti.dds.publication.DataWriterQos;
import com.rti.dds.publication.Publisher;
import com.rti.dds.topic.Topic;
import com.rti.dds.topic.TopicQos;

public class RtiChatPublisher {
	
	private DomainParticipant _participant;
	private RtiChatDataWriter _dataWriter;
		
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
        
        TopicQos topic_qos = new TopicQos();
		topic_qos = DomainParticipant.TOPIC_QOS_DEFAULT;
        
		topic_qos.history.kind    = HistoryQosPolicyKind.KEEP_ALL_HISTORY_QOS;
		topic_qos.durability.kind = DurabilityQosPolicyKind.TRANSIENT_LOCAL_DURABILITY_QOS;
		topic_qos.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;

        // Create the topic "RtiChat_Topic" for the String type
        Topic topic = _participant.create_topic(
                "RtiChat_Topic", 
                RtiChatTypeSupport.get_type_name(), 
                topic_qos, 
                null, // listener
                StatusKind.STATUS_MASK_NONE);
        if (topic == null) {
            System.err.println("Unable to create topic.");
            return;
        }
        
        DataWriterQos datawriter_qos = new DataWriterQos();
        datawriter_qos = Publisher.DATAWRITER_QOS_DEFAULT;
        
        datawriter_qos.history.kind    = HistoryQosPolicyKind.KEEP_ALL_HISTORY_QOS;
        datawriter_qos.durability.kind = DurabilityQosPolicyKind.TRANSIENT_LOCAL_DURABILITY_QOS;
        datawriter_qos.reliability.kind = ReliabilityQosPolicyKind.RELIABLE_RELIABILITY_QOS;     
        
     // Create the data writer using the default publisher
        _dataWriter =
            (RtiChatDataWriter) _participant.create_datawriter(
                topic, 
                datawriter_qos,
                null, // listener
                StatusKind.STATUS_MASK_NONE);
        if (_dataWriter == null) {
            System.err.println("Unable to create data writer\n");
            return;
        }
	}

	void sendMessage(String msg)
	{	
        try {
            RtiChat toWrite = new RtiChat();
            toWrite.sender = "Mario Rossi";
            toWrite.message = msg;

            _dataWriter.write(toWrite, InstanceHandle_t.HANDLE_NIL);
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
}
