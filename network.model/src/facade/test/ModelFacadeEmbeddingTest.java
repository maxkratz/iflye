package facade.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Ignore;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import facade.ModelFacade;
import model.SubstrateNetwork;
import model.SubstrateServer;
import model.VirtualServer;
import model.VirtualSwitch;
import model.SubstrateSwitch;
import model.VirtualLink;
import model.SubstrateLink;

/**
 * Test class for the ModelFacade that tests some embedding tasks.
 * 
 * @author Maximilian Kratz <maximilian.kratz@stud.tu-darmstadt.de>
 */
public class ModelFacadeEmbeddingTest {

	@BeforeEach
	public void resetModel() {
		ModelFacade.getInstance().resetAll();
		
		// Network setup
		ModelFacade.getInstance().addNetworkToRoot("sub", false);
		ModelFacade.getInstance().addNetworkToRoot("virt", true);
	}
	
	@Test
	public void testEmbedNetworkToNetwork() {
		// No guests before embedding anything
		assertTrue(((SubstrateNetwork) ModelFacade.getInstance().getNetworkById("sub"))
				.getGuests().isEmpty());
		
		ModelFacade.getInstance().embedNetworkToNetwork("sub", "virt");
		
		assertFalse(((SubstrateNetwork) ModelFacade.getInstance().getNetworkById("sub"))
				.getGuests().isEmpty());
		assertEquals("virt", ((SubstrateNetwork) ModelFacade.getInstance().getNetworkById("sub"))
				.getGuests().get(0).getName());
	}
	
	@Test
	public void testEmbedServerToServer() {
		ModelFacade.getInstance().addServerToNetwork("1", "sub", 1, 1, 1, 0);
		ModelFacade.getInstance().addServerToNetwork("2", "virt", 1, 1, 1, 0);
		
		ModelFacade.getInstance().embedServerToServer("1", "2");
		assertEquals(1, ((SubstrateServer) ModelFacade.getInstance().getServerById("1"))
				.getGuestServers().size());
		assertEquals("1", ((VirtualServer) ModelFacade.getInstance().getServerById("2"))
				.getHost().getName());
	}
	
	@Test
	public void testEmbedServerToServerRejectCpu() {
		ModelFacade.getInstance().addServerToNetwork("1", "sub", 1, 1, 1, 0);
		ModelFacade.getInstance().addServerToNetwork("2", "virt", 2, 1, 1, 0);
		
		Assertions.assertThrows(UnsupportedOperationException.class, () -> {
			ModelFacade.getInstance().embedServerToServer("1", "2");
		});
	}
	
	@Test
	public void testEmbedServerToServerRejectMemory() {
		ModelFacade.getInstance().addServerToNetwork("1", "sub", 1, 1, 1, 0);
		ModelFacade.getInstance().addServerToNetwork("2", "virt", 1, 2, 1, 0);
		
		Assertions.assertThrows(UnsupportedOperationException.class, () -> {
			ModelFacade.getInstance().embedServerToServer("1", "2");
		});
	}
	
	@Test
	public void testEmbedServerToServerRejectStorage() {
		ModelFacade.getInstance().addServerToNetwork("1", "sub", 1, 1, 1, 0);
		ModelFacade.getInstance().addServerToNetwork("2", "virt", 1, 1, 2, 0);
		
		Assertions.assertThrows(UnsupportedOperationException.class, () -> {
			ModelFacade.getInstance().embedServerToServer("1", "2");
		});
	}
	
	@Test
	public void testEmbedSwitchToServer() {
		ModelFacade.getInstance().addServerToNetwork("1", "sub", 0, 0, 0, 0);
		ModelFacade.getInstance().addSwitchToNetwork("2", "virt", 0);
		
		ModelFacade.getInstance().embedSwitchToNode("1", "2");
		assertEquals(1, ((SubstrateServer) ModelFacade.getInstance().getServerById("1"))
				.getGuestSwitches().size());
		assertEquals("1", ((VirtualSwitch) ModelFacade.getInstance().getSwitchById("2"))
				.getHost().getName());
	}
	
	@Test
	public void testEmbedSwitchtoSwitch() {
		ModelFacade.getInstance().addSwitchToNetwork("1", "sub", 0);
		ModelFacade.getInstance().addSwitchToNetwork("2", "virt", 0);
		
		ModelFacade.getInstance().embedSwitchToNode("1", "2");
		assertEquals(1, ((SubstrateSwitch) ModelFacade.getInstance().getSwitchById("1"))
				.getGuestSwitches().size());
		assertEquals("1", ((VirtualSwitch) ModelFacade.getInstance().getSwitchById("2"))
				.getHost().getName());
	}
	
	@Test
	public void testEmbedLinkToServer() {
		ModelFacade.getInstance().addServerToNetwork("1", "sub", 0, 0, 0, 0);
		
		ModelFacade.getInstance().addServerToNetwork("2", "virt", 0, 0, 0, 0);
		ModelFacade.getInstance().addServerToNetwork("3", "virt", 0, 0, 0, 0);
		ModelFacade.getInstance().addLinkToNetwork("4","virt", 0, "2", "3");
		
		ModelFacade.getInstance().embedLinkToServer("1", "4");
		assertEquals(1, ((SubstrateServer) ModelFacade.getInstance().getServerById("1"))
				.getGuestLinks().size());
		assertEquals("1", ((VirtualLink) ModelFacade.getInstance().getLinkById("4"))
				.getHosts().get(0).getName());
	}
	
	@Test
	public void testEmbedLinkToLink() {
		ModelFacade.getInstance().addServerToNetwork("1", "sub", 0, 0, 0, 0);
		ModelFacade.getInstance().addServerToNetwork("2", "sub", 0, 0, 0, 0);
		ModelFacade.getInstance().addLinkToNetwork("3","sub", 10, "1", "2");
		
		ModelFacade.getInstance().addServerToNetwork("4", "virt", 0, 0, 0, 0);
		ModelFacade.getInstance().addServerToNetwork("5", "virt", 0, 0, 0, 0);
		ModelFacade.getInstance().addLinkToNetwork("6","virt", 8, "4", "5");
		
		ModelFacade.getInstance().embedLinkToLink("3", "6");
		assertEquals(1, ((SubstrateLink) ModelFacade.getInstance().getLinkById("3"))
				.getGuestLinks().size());
		assertEquals("3", ((VirtualLink) ModelFacade.getInstance().getLinkById("6"))
				.getHosts().get(0).getName());
		assertEquals(2, ((SubstrateLink) ModelFacade.getInstance().getLinkById("3"))
				.getResidualBandwidth());
	}
	
	@Test
	public void testEmbedLinkToLinkReject() {
		ModelFacade.getInstance().addServerToNetwork("1", "sub", 0, 0, 0, 0);
		ModelFacade.getInstance().addServerToNetwork("2", "sub", 0, 0, 0, 0);
		ModelFacade.getInstance().addLinkToNetwork("3","sub", 10, "1", "2");
		
		ModelFacade.getInstance().addServerToNetwork("4", "virt", 0, 0, 0, 0);
		ModelFacade.getInstance().addServerToNetwork("5", "virt", 0, 0, 0, 0);
		ModelFacade.getInstance().addLinkToNetwork("6","virt", 12, "4", "5");
		
		Assertions.assertThrows(UnsupportedOperationException.class, () -> {
			ModelFacade.getInstance().embedLinkToLink("3", "6");
		});
	}
	
	@Ignore
	@Test
	public void testEmbedLinkToPath() {
		//TODO: Implement after creation of all paths is implemented.
	}
	
}