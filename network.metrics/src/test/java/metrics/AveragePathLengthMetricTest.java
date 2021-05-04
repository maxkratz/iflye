package metrics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.Before;
import org.junit.Test;
import model.Path;
import model.SubstrateNetwork;

/**
 * Test class for the metric of the average path length.
 * 
 * @author Maximilian Kratz <maximilian.kratz@stud.tu-darmstadt.de>
 */
public class AveragePathLengthMetricTest extends AMetricTest {

  @Before
  public void setup() {
    createSubstrateNetwork();
    createVirtualNetwork();
  }

  @Test
  public void testNoEmbeddings() {
    final SubstrateNetwork sNet = (SubstrateNetwork) facade.getNetworkById("sub");

    final AveragePathLengthMetric metric = new AveragePathLengthMetric(sNet);
    assertEquals(0, metric.getValue());
  }

  @Test
  public void testEmbeddingSameHost() {
    facade.embedNetworkToNetwork("sub", "virt");
    facade.embedSwitchToNode("ssrv1", "vsw");
    facade.embedServerToServer("ssrv1", "vsrv1");
    facade.embedServerToServer("ssrv1", "vsrv2");
    facade.embedLinkToServer("ssrv1", "vln1");
    facade.embedLinkToServer("ssrv1", "vln2");
    facade.embedLinkToServer("ssrv1", "vln3");
    facade.embedLinkToServer("ssrv1", "vln4");

    final SubstrateNetwork sNet = (SubstrateNetwork) facade.getNetworkById("sub");

    final AveragePathLengthMetric metric = new AveragePathLengthMetric(sNet);
    assertEquals(0, metric.getValue());
  }

  @Test
  public void testEmbeddingTwoHosts() {
    facade.embedNetworkToNetwork("sub", "virt");
    facade.embedSwitchToNode("ssw", "vsw");
    facade.embedServerToServer("ssrv1", "vsrv1");
    facade.embedServerToServer("ssrv2", "vsrv2");
    facade.embedLinkToLink("sln1", "vln1");
    facade.embedLinkToLink("sln2", "vln2");
    facade.embedLinkToLink("sln3", "vln3");
    facade.embedLinkToLink("sln4", "vln4");

    final SubstrateNetwork sNet = (SubstrateNetwork) facade.getNetworkById("sub");

    final AveragePathLengthMetric metric = new AveragePathLengthMetric(sNet);
    assertEquals(1, metric.getValue());
  }

  @Test
  public void testEmbeddingTwoHops() {
    facade.createAllPathsForNetwork("sub");
    facade.embedNetworkToNetwork("sub", "virt");
    facade.embedSwitchToNode("ssrv2", "vsw");
    facade.embedServerToServer("ssrv1", "vsrv1");

    final Path pa = facade.getPathFromSourceToTarget(facade.getServerById("ssrv1"),
        facade.getServerById("ssrv2"));
    final Path pb = facade.getPathFromSourceToTarget(facade.getServerById("ssrv2"),
        facade.getServerById("ssrv1"));

    facade.embedLinkToPath(pa.getName(), "vln1");
    facade.embedLinkToPath(pb.getName(), "vln3");

    final SubstrateNetwork sNet = (SubstrateNetwork) facade.getNetworkById("sub");

    final AveragePathLengthMetric metric = new AveragePathLengthMetric(sNet);
    assertEquals(2, metric.getValue());
  }

}
