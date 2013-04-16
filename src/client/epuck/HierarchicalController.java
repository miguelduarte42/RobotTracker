package client.epuck;

public class HierarchicalController extends CTRNNMultilayer {
	
	private RoboticController[] subControllers;
	public int lastSubController = 0;
	public double[] outputs;

	public HierarchicalController(int inputs, int hidden, int outputs, int[] inputIndex, String name, double[] weights) {
		super(inputs, hidden, outputs, inputIndex, name, weights);
	}
	
	public void setSubControllers(RoboticController[] subControllers) {
		this.subControllers = subControllers;
	}
	
	public double[] update(double[] inputValues, int numberOutputs) {
		
		outputs = super.update(inputValues, numberOutputs);

		int maxIndex = 0;
		
		for(int i = 1 ; i < outputs.length ; i++)
			maxIndex = outputs[i] > outputs[maxIndex] ? i : maxIndex;
			
			RoboticController chosenNetwork = subControllers[maxIndex];
			
		if(maxIndex != lastSubController)
			chosenNetwork.reset();
		
		lastSubController = maxIndex;
		
		return chosenNetwork.update(inputValues, numberOutputs);
	}
}