package com.rti.RtiChat;

import java.util.logging.Logger;
import com.rti.dds.infrastructure.RETCODE_ERROR;
import com.rti.dds.infrastructure.RETCODE_NO_DATA;
import com.rti.dds.subscription.DataReader;
import com.rti.dds.subscription.DataReaderListener;
import com.rti.dds.subscription.LivelinessChangedStatus;
import com.rti.dds.subscription.RequestedDeadlineMissedStatus;
import com.rti.dds.subscription.RequestedIncompatibleQosStatus;
import com.rti.dds.subscription.SampleInfo;
import com.rti.dds.subscription.SampleLostStatus;
import com.rti.dds.subscription.SampleRejectedStatus;
import com.rti.dds.subscription.SubscriptionMatchedStatus;

public class RtiChatListener implements DataReaderListener {

	@Override
	public void on_data_available(DataReader reader) {
		Logger.getAnonymousLogger().info("Data arrived");
		
		RtiChatDataReader rtiChatReader = (RtiChatDataReader) reader;
        SampleInfo info = new SampleInfo();
        for (;;) {
            try {
            	RtiChat sample = new RtiChat();
                rtiChatReader.take_next_sample(sample, info);
                
                if (info.valid_data) {
                    System.out.println("on_data_available" + sample.sender + " : " + sample.message);
                    
                }
            } catch (RETCODE_NO_DATA noData) {
                // No more data to read
                break;
            } catch (RETCODE_ERROR e) {
                // An error occurred
                e.printStackTrace();
            }
        }		
	}

	@Override
	public void on_liveliness_changed(DataReader arg0,
			LivelinessChangedStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_requested_deadline_missed(DataReader arg0,
			RequestedDeadlineMissedStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_requested_incompatible_qos(DataReader arg0,
			RequestedIncompatibleQosStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_sample_lost(DataReader arg0, SampleLostStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_sample_rejected(DataReader arg0, SampleRejectedStatus arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void on_subscription_matched(DataReader arg0,
			SubscriptionMatchedStatus arg1) {
		Logger.getAnonymousLogger().info("on_subscription_matched called");
		
	}

}
