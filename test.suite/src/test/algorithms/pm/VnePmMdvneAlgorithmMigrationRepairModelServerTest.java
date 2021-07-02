package test.algorithms.pm;

import java.util.Set;
import algorithms.pm.VnePmMdvneAlgorithmMigration;
import model.SubstrateNetwork;
import model.VirtualNetwork;

/**
 * Test class for the VNE pattern matching algorithm migration implementation for repairing a
 * removed substrate server in the model. This test should trigger the algorithm to re-embed all
 * virtual networks that had elements placed on the substrate server removed.
 * 
 * @author Maximilian Kratz {@literal <maximilian.kratz@stud.tu-darmstadt.de>}
 */
public class VnePmMdvneAlgorithmMigrationRepairModelServerTest
    extends VnePmMdvneAlgorithmRepairModelServerTest {

  @Override
  public void initAlgo(final SubstrateNetwork sNet, final Set<VirtualNetwork> vNets) {
    algo = VnePmMdvneAlgorithmMigration.prepare(sNet, vNets);
  }

}

