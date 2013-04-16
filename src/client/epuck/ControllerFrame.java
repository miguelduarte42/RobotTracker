package client.epuck;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Displays the inputs and outputs of a neural-network
 * based controller in real time.
 * 
 * @author miguelduarte
 *
 */
public class ControllerFrame extends JFrame{
	
	private Controller c;
	private GraphingData inputsGraph;
	private GraphingData outputsGraph;
	
	public ControllerFrame(Controller controller) {

		this.c = controller;
		
		addKeyListener(new KeyListener() {
			public void keyTyped(KeyEvent arg0) {}
			
			public void keyReleased(KeyEvent arg0) {
				if(arg0.getKeyChar()=='l') {
					if(c != null)
						c.sensor = false;
				}
			}
			
			public void keyPressed(KeyEvent arg0) {
				if(arg0.getKeyChar()=='l')
					if(c != null)
						c.sensor = true;
			}
		});
		
		inputsGraph = new GraphingData(controller.INPUTS);
		inputsGraph.setSize(500,400);
		outputsGraph = new GraphingData(controller.OUTPUTS);
		outputsGraph.setSize(500,400);
		JPanel masterPanel = new JPanel(new BorderLayout());

		masterPanel.setSize(1000,450);

		JPanel graphsPanel = new JPanel(new GridLayout(1,2));
		graphsPanel.add(inputsGraph);
		graphsPanel.add(outputsGraph);
		masterPanel.add(graphsPanel,BorderLayout.CENTER);
		
		add(masterPanel);
		setSize(1000,450);

		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void updateGraphs(double[] inputs, double outputs[]) {

		for(int i = 0 ; i < inputs.length ; i++)
			inputsGraph.addData(inputs[i],i);
		for(int i = 0 ; i < outputs.length ; i++)
			outputsGraph.addData(outputs[i],i);
	}
}
