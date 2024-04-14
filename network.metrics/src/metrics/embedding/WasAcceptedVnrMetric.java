package metrics.embedding;

import metrics.IMetric;
import model.SubstrateNetwork;
import model.VirtualNetwork;

public class WasAcceptedVnrMetric implements IMetric {

	private int value;

	public WasAcceptedVnrMetric(final SubstrateNetwork sNet, final String vNetId) {
		this.value = 0;
		VirtualNetwork vNet = (VirtualNetwork) facade.getNetworkById(vNetId);
		if (vNet.getHost() != null) {
			this.value = 1;
		}
	}

	@Override
	public double getValue() {
		return value;
	}

}
