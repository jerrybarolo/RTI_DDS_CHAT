package com.rti.RtiChat;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JButton;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextArea;

import com.rti.dds.infrastructure.*;
import com.rti.dds.subscription.SampleInfo;

import javax.swing.JComboBox;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.SwingConstants;
import javax.swing.JMenuItem;
import javax.swing.event.MenuKeyListener;
import javax.swing.event.MenuKeyEvent;

public class WindowMain {

	private JFrame frame;
	private JTextArea textArea_storico;
	private JTextArea textArea_message;
	private JComboBox<String> comboBox_contatti;
	private RtiChatPublisher _publisher;
	private RtiChatSubscriber _subscriber;
	private int _sampleCount = 0;
	
	private HashMap<String, ArrayList<String>> messageMap;

	public Map<String, ArrayList<String>> getMessageMap() {
		return messageMap;
	}

	public void putNewMessage(String sender, String message) {
		ArrayList<String> arr = new ArrayList<String>();
		if(this.messageMap.get(sender) != null)
		{
			arr = this.messageMap.get(sender);
		}
		arr.add(message);
		this.messageMap.put(sender, arr);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					WindowMain window = new WindowMain();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public WindowMain() {
		initialize();
	}
	
	public void newMessageArrived(RtiChat data)
	{
		putNewMessage(data.sender, data.message);
		if(comboBox_contatti.getSelectedItem().equals(data.sender))
		{
			textArea_storico.append(data.message + "\n");
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		messageMap = new HashMap <String, ArrayList<String>>();
		messageMap.clear();
		
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.addWindowListener(new WindowListener() {
			
			@Override
			public void windowOpened(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowIconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeiconified(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				_publisher.close();
				_subscriber.close();
			}
			
			@Override
			public void windowClosed(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void windowActivated(WindowEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		frame.getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Storico Messaggi");
		lblNewLabel.setBounds(239, 11, 148, 38);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnInvia = new JButton("Invia");
		btnInvia.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				_publisher.sendMessage(textArea_message.getText());
				textArea_message.setText("");
			}
		});
		btnInvia.setBounds(176, 228, 89, 23);
		frame.getContentPane().add(btnInvia);
		
		JScrollPane scroll = new JScrollPane ();
	    scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	    scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	    scroll.setBounds(177, 49, 233, 115);
	    frame.getContentPane().add(scroll);		
	    
	    textArea_storico = new JTextArea();
	    scroll.setViewportView(textArea_storico);
	    textArea_storico.setLineWrap(true);
	    
	    _subscriber = new RtiChatSubscriber();
		_publisher  = new RtiChatPublisher();
		
		Thread t = new Thread(new Runnable() {
	         public void run()
	         {
	        	 setNotifyWhenDataAvailable();
	         }
		});		
		t.start();
	    
	    comboBox_contatti = new JComboBox<String>();
	    comboBox_contatti.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				System.out.println("JComboBox actionPerformed called");
				
				textArea_storico.setText("");
				
				String key = comboBox_contatti.getSelectedItem().toString();
				ArrayList<String> arr = null;
				if(messageMap.get(key) != null)
				{
					arr = messageMap.get(key);
				
					for(String c : arr)
						textArea_storico.append(c + "\n");
				}
	        }
		});
	    	    
	    comboBox_contatti.setBounds(38, 76, 129, 20);
	    frame.getContentPane().add(comboBox_contatti);
	    
	    JLabel lblContatti = new JLabel("Contatti");
	    lblContatti.setBounds(38, 32, 72, 38);
	    frame.getContentPane().add(lblContatti);
	    
	    JLabel lblMessaggio = new JLabel("Nuovo Messaggio");
	    lblMessaggio.setBounds(38, 173, 129, 38);
	    frame.getContentPane().add(lblMessaggio);
	    
	    textArea_message = new JTextArea();
	    textArea_message.setLineWrap(true);
	    textArea_message.setBounds(176, 175, 234, 42);
	    frame.getContentPane().add(textArea_message);
	    
	    JMenuBar menuBar = new JMenuBar();
	    menuBar.setBounds(0, 0, 62, 21);
	    frame.getContentPane().add(menuBar);
	    
	    JMenu mnNewMenu = new JMenu("Contatto");
	    menuBar.add(mnNewMenu);
	    
	    JMenuItem mntmNewMenuItem = new JMenuItem("Aggiungi");
	    mntmNewMenuItem.addMenuKeyListener(new MenuKeyListener() {
	    	public void menuKeyPressed(MenuKeyEvent arg0) {
	    	}
	    	public void menuKeyReleased(MenuKeyEvent arg0) {
	    	}
	    	public void menuKeyTyped(MenuKeyEvent arg0) {
	    	}
	    });
	    mnNewMenu.add(mntmNewMenuItem);
	    
	    JMenuItem mntmElimina = new JMenuItem("Elimina");
	    mntmElimina.addMenuKeyListener(new MenuKeyListener() {
	    	public void menuKeyPressed(MenuKeyEvent e) {
	    	}
	    	public void menuKeyReleased(MenuKeyEvent e) {
	    	}
	    	public void menuKeyTyped(MenuKeyEvent e) {
	    	}
	    });
	    mnNewMenu.add(mntmElimina);
	}

	private void setNotifyWhenDataAvailable() {
		
		// Create and configure the WaitSet
		WaitSet waitset = new WaitSet();
		 
		StatusCondition condition = _subscriber.getDataReader().get_statuscondition();
		condition.set_enabled_statuses(StatusKind.DATA_AVAILABLE_STATUS);
		
		/* Attach Status Conditions */
        waitset.attach_condition(condition);
        
        // --- Wait for data --- //
        for (int count = 0; (_sampleCount == 0) || (count < _sampleCount); ++count) {
        	        
		    ConditionSeq active_conditions_seq = new ConditionSeq();
		    Duration_t wait_timeout = new Duration_t();
		    wait_timeout.sec = 1;
		    wait_timeout.nanosec = 500000000;
	        
	        try {
	            /* wait() blocks execution of the thread until one or more
	             * attached Conditions become true, or until a user- 
	             * specified timeout expires.
	             */
	            waitset.wait(active_conditions_seq, wait_timeout);
	            /* We get to timeout if no conditions were triggered */
	        } catch (RETCODE_TIMEOUT e) {
	            //System.out.println("Wait timed out!! No conditions were triggered.\n");
	            continue;
	        }
	        
	        if (active_conditions_seq.get(0) == condition) {
	        	
	        	RtiChatDataReader rtiChatReader = (RtiChatDataReader) _subscriber.getDataReader();
	            SampleInfo info = new SampleInfo();
	            for (;;) {
	                try {
	                	RtiChat sample = new RtiChat();
	                    rtiChatReader.take_next_sample(sample, info);
	                    
	                    if (info.valid_data) {
	                        System.out.println("setNotifyWhenDataAvailable Sender: " + sample.sender + " : " + sample.message);
	                        newMessageArrived(sample);
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
	        
	        try {
                Thread.sleep(1000);  // in millisec
            } catch (InterruptedException ix) {
                System.err.println("INTERRUPTED");
                break;
            }
        
        }
	}
}
