package examples.algorithms;

import algorithms.ilp.VneIlpPathAlgorithm;
import facade.ModelFacade;
import facade.config.ModelFacadeConfig;
import generators.OneTierNetworkGenerator;
import generators.TwoTierNetworkGenerator;
import generators.config.OneTierConfig;
import generators.config.TwoTierConfig;
import model.SubstrateNetwork;
import model.VirtualNetwork;

/**
 * Runnable example for the VNE ILP algorithm implementation.
 * 
 * @author Maximilian Kratz {@literal <maximilian.kratz@stud.tu-darmstadt.de>}
 */
public class VneIlpPathAlgorithmExampleMedium {

  /**
   * Main method to start the example. String array of arguments will be ignored.
   * 
   * @param args Will be ignored.
   */
  public static void main(final String[] args) {
    // Setup
    ModelFacadeConfig.MIN_PATH_LENGTH = 2;
    ModelFacadeConfig.MAX_PATH_LENGTH = 4;

    final long start = System.nanoTime();

    // Substrate network = one tier network
    final OneTierConfig rackConfig = new OneTierConfig(10, 1, false, 10, 10, 10, 10);
    final TwoTierConfig substrateConfig = new TwoTierConfig();
    substrateConfig.setRack(rackConfig);
    substrateConfig.setCoreBandwidth(100);
    substrateConfig.setNumberOfCoreSwitches(1);
    substrateConfig.setNumberOfRacks(6);
    final TwoTierNetworkGenerator subGen = new TwoTierNetworkGenerator(substrateConfig);
    subGen.createNetwork("sub", false);

    for (int i = 0; i < 10; i++) {
      // Virtual network = one tier network
      final OneTierConfig virtualConfig = new OneTierConfig(6, 1, false, 10, 1, 1, 1);
      final OneTierNetworkGenerator virtGen = new OneTierNetworkGenerator(virtualConfig);
      virtGen.createNetwork("virt_" + i, true);

      final SubstrateNetwork sNet =
          (SubstrateNetwork) ModelFacade.getInstance().getNetworkById("sub");
      final VirtualNetwork vNet =
          (VirtualNetwork) ModelFacade.getInstance().getNetworkById("virt_" + i);

      // Create and execute algorithm
      final VneIlpPathAlgorithm algo = new VneIlpPathAlgorithm(sNet, vNet);
      algo.execute();
    }

    final long end = System.nanoTime();

    // Save model to file
    ModelFacade.getInstance().persistModel();
    System.out.println("=> Execution finished.");
    System.out.println("=> Elapsed time: " + (end - start) / 1_000_000_000 + " seconds");
  }

}
