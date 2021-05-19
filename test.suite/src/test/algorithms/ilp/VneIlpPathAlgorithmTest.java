package test.algorithms.ilp;

import java.util.Set;
import algorithms.ilp.VneIlpPathAlgorithm;
import model.SubstrateNetwork;
import model.VirtualNetwork;
import test.algorithms.generic.AAlgorithmMultipleVnsTest;

/**
 * Test class for the VNE ILP algorithm implementation.
 * 
 * @author Maximilian Kratz {@literal <maximilian.kratz@stud.tu-darmstadt.de>}
 */
public class VneIlpPathAlgorithmTest extends AAlgorithmMultipleVnsTest {

  @Override
  public void initAlgo(final SubstrateNetwork sNet, final Set<VirtualNetwork> vNets) {
    algo = new VneIlpPathAlgorithm(sNet, vNets);
  }

}
