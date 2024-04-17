package metrics.time;

public class MlVneRuntimeMetric extends RuntimeMetric {

	private long embeddingStart = 0;
	private long embeddingCumulative = 0;
	private long inferenceCumulative = 0;

	public void startEmbeddingTime() {
		this.embeddingStart = System.nanoTime();
	}

	public void endEmbeddingTime() {
		embeddingCumulative += System.nanoTime() - embeddingStart;
		embeddingStart = 0;
	}

	public void addInferenceEmbeddingTime(long value) {
		inferenceCumulative += value;
	}
	
	public double getInferenceValue() {
		return inferenceCumulative;
	}

	public double getEmbeddingValue() {
		return embeddingCumulative;
	}

}
