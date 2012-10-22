package client;

public class HierarchicalController extends CTRNNMultilayer {
	
	private CTRNNMultilayer subControllers[];
	public int lastSubController = 0;
	public boolean master = false;
	public double[] outputs;

	public HierarchicalController(int inputs, int hidden, int outputs, int[] inputIndex, double[] weights) {
		super(inputs, hidden, outputs, inputIndex, weights);
	}
	
	public void setSubControllers(CTRNNMultilayer subControllers[]) {
		this.subControllers = subControllers;
	}
	
	public double[] propagateInputs(double[] inputValues) {
		outputs = super.propagateInputs(inputValues);

		int maxIndex = 0;
		
		for(int i = 1 ; i < outputs.length ; i++)
			maxIndex = outputs[i] > outputs[maxIndex] ? i : maxIndex;
			
		CTRNNMultilayer chosenNetwork = subControllers[maxIndex];
			
		if(maxIndex != lastSubController)
			chosenNetwork.reset();
		
		lastSubController = maxIndex;
		
		if(master)
			System.out.println(lastSubController);
		
		return chosenNetwork.propagateInputs(inputValues);
	}
}