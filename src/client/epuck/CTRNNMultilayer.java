package client.epuck;

/**
 * Implementation of a Continuous-Time Recurrent Neural Network.
 * 
 * @author miguelduarte
 *
 */
public class CTRNNMultilayer extends RoboticController {
	private double    timeStep = 0.2;
	private double    tau      = 2.5;
	private int       numberOfHiddenNodes;
	private int 	  numberOfInputNeurons;
	private int 	  numberOfOutputNeurons;
	private double[]  hiddenDeltaStates;
	private double[]  hiddenStates;
	private double[]  inputToHiddenWeights;
	private double[]  hiddenBiases;
	private double[]  hiddenToHiddenWeights;
	private double[]  hiddenTaus;
	private double[]  hiddenToOutputWeights;
	private double[]  outputBiases;
	private double[]  outputNeuronStates;
	private double[] weights;
	private int[] inputIndex;
	private int[] outputIndex;

	public CTRNNMultilayer(int inputs, int hidden, int outputs, int[] inputIndex, String name, double[] weights) {
		super(name);
		this.numberOfInputNeurons = inputs;
		this.numberOfHiddenNodes = hidden;
		this.numberOfOutputNeurons = outputs;
		this.weights = weights;
		this.inputIndex = inputIndex;
		
		this.outputIndex = new int[outputs];
		for(int i = 0 ; i < outputs ; i++)
			outputIndex[i] = i;
		
		reset();
	}

	public CTRNNMultilayer(int inputs, int hidden, int outputs, int[] inputIndex, int[] outputIndex, String name, double[] weights) {
		this(inputs, hidden, outputs, inputIndex, name, weights);
		this.outputIndex = outputIndex;
	}

	public double[] update(double[] inputValues, int numberOutputs) {
		
		System.out.print(">> "+getName()+" ");
		
		// Update delta state of hidden layer from inputs:
		for(int i = 0; i < numberOfHiddenNodes; i++) {
			hiddenDeltaStates[i] = -hiddenStates[i];

			for (int j = 0; j < numberOfInputNeurons; j++) {
				// weight * sigmoid(state)
				hiddenDeltaStates[i] += inputToHiddenWeights[i * numberOfInputNeurons + j] * inputValues[inputIndex[j]] ;	  
			}	  
		}

		// Update delta state from hidden layer, self-recurrent connections:
		for (int i = 0; i < numberOfHiddenNodes; i++)
		{
			for (int j = 0; j < numberOfHiddenNodes; j++) {
				double z              = 1.0/(Math.exp(-(hiddenStates[j] + hiddenBiases[j])) + 1.0 );
				hiddenDeltaStates[i] += hiddenToHiddenWeights[i * numberOfHiddenNodes + j] * z;
			}
		}

		for(int  i = 0; i < numberOfHiddenNodes; i++) 
		{
			hiddenStates[i] += hiddenDeltaStates[i] * timeStep/hiddenTaus[i];
		}

		// Update the outputs layer::
		for (int i = 0; i < numberOfOutputNeurons; i++)
		{
			for (int j = 0; j < numberOfHiddenNodes; j++)
			{
				double z = ((1.0)/(Math.exp(-( hiddenStates[j] + hiddenBiases[j])) + 1.0 ));
				outputNeuronStates[i] += hiddenToOutputWeights[i * numberOfHiddenNodes + j] * z;
			}

			// Compute the activation function immediately, since this is
			// what we return and since the output layer is not recurrent:
			outputNeuronStates[i] = ((1.0)/(Math.exp(-(outputNeuronStates[i] + outputBiases[i])) + 1.0 ));
		}
		
		double[] returnOutputs = new double[numberOutputs];

//		System.out.println(numberOfOutputNeurons+" "+outputNeuronStates.length);
		for(int i = 0 ; i < outputNeuronStates.length ; i++) {
			returnOutputs[outputIndex[i]] = outputNeuronStates[i];
//			System.out.print(i+" "+outputIndex[i]+" "+outputNeuronStates[i]);
		}
//		System.out.println();
//		
//		for(int i = 0 ; i < returnOutputs.length ; i++)
//			System.out.print(returnOutputs[i]+" ");
//		
//		System.out.println();
		
		return returnOutputs;
	}

	public void reset() {

		if (weights != null) {
			int A = numberOfInputNeurons;
			int B = numberOfHiddenNodes;
			int C = numberOfOutputNeurons;

			inputToHiddenWeights  = new double[A * B];
			hiddenToHiddenWeights = new double[B * B];
			hiddenBiases          = new double[B];

			hiddenTaus            = new double[B];

			hiddenToOutputWeights = new double[B * C];
			outputBiases          = new double[C];
			
			outputNeuronStates	  = new double[C];

			int chromosomePosition = 0;
			for (int i = 0; i < A * B; i++) {
				inputToHiddenWeights[i] = weights[chromosomePosition++];
			}

			for (int i = 0; i < B * B; i++) {
				hiddenToHiddenWeights[i] = weights[chromosomePosition++];
			}

			for (int i = 0; i < B; i++) {
				hiddenBiases[i] = weights[chromosomePosition++];
			}

			for (int i = 0; i < B; i++) {
				hiddenTaus[i] = Math.pow(10, (-1.0 + (tau * (weights[chromosomePosition++] + 10.0) / 20)));
			}

			for (int i = 0; i < B * C; i++) {
				hiddenToOutputWeights[i] = weights[chromosomePosition++];
			}

			for (int i = 0; i < C; i++)
			{
				outputBiases[i] = weights[chromosomePosition++];
			}
			
			try{
			
			if(chromosomePosition != weights.length)
				throw new Exception("Invalid number of weights! "+numberOfInputNeurons+" "+numberOfHiddenNodes+" "+numberOfOutputNeurons);
			}catch(Exception e) {
				e.printStackTrace();
			}

			hiddenDeltaStates     = new double[B];
			hiddenStates          = new double[B];

			for (int i = 0; i < B; i++) {
				hiddenStates[i] = 0;
			}
		}
	}

	public double[] getHiddenStates() {
		return hiddenStates;
	}

	public double[] getHiddenTaus() {
		return hiddenTaus;
	}
}